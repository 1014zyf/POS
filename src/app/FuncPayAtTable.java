package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import om.PosBusinessDay;
import om.PosCheckExtraInfo;
import om.PosCheckPayment;
import om.PosDiscountType;
import om.PosDiscountTypeList;
import om.PosFunctionAclCache;
import om.PosInterfaceConfig;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import externallib.StringLib;

public class FuncPayAtTable {
	public static final String FUNCTION_LOGIN = "01";
	public static final String FUNCTION_LOGOUT = "02";
	public static final String FUNCTION_PRINT_CHECK = "03";
	public static final String FUNCTION_GET_CHECK_AMOUNT = "04";
	public static final String FUNCTION_CANCEL_PAYMENT = "05";
	public static final String FUNCTION_CONFIRM_PAYMENT = "06";
	public static final String FUNCTION_GET_RELEASE_PAYMENT_INFO = "07";
	public static final String FUNCTION_RELEASE_PAYMENT = "08";
	
	public static final String PAYMENT_TYPE_MULTIPLE  = "00";
	public static final String PAYMENT_TYPE_CASH  = "01";
	public static final String PAYMENT_TYPE_COUPON  = "02";
	public static final String PAYMENT_TYPE_CARD  = "03";
	public static final String PAYMENT_TYPE_GIFTCARD  = "04";
	public static final String PAYMENT_TYPE_ALIPAY = "05";
	public static final String PAYMENT_TYPE_WECHAT = "06";
	public static final String PAYMENT_TYPE_TAODIANDIAN = "07";
	
	public static final String RESPONSE_CODE_SUCCESS = "00";
	
	private JSONObject m_oRequestJSONObject;
	private PosInterfaceConfig m_oInterfaceConfig;
	private JSONObject m_oInterfaceSetup;
	private HashMap<Integer, FuncUser> m_oLoginedUserList;
	private JSONObject m_oPaidCheckInfo;
	private ArrayList<String> m_oGuestCheckStringList;
	
	private String m_sFunctionId;
	private String m_sHandTerminalId;
	private String m_sOperatorId;
	private String m_sOperatorPwd;
	private String m_sPackSeq;
	private String m_sOutletCode;
	private String m_sTableNum;
	private String m_sCheckNum;
	private String m_sDiscountId;
	private String m_sMemberNumber;
	private String m_sNextPackSeq;
	private String m_sOldPaymentReference;
	
	private String m_sTableNumWithoutExtension;
	private String m_sTableExtension;
	
	private String m_sErrorMsg;
	private int m_iStatus;
	
	public FuncPayAtTable(PosInterfaceConfig oPosInterfaceConfig) {
		m_oInterfaceConfig = oPosInterfaceConfig;
		m_oInterfaceSetup = m_oInterfaceConfig.getInterfaceConfig();
		m_oLoginedUserList = new HashMap<Integer, FuncUser>();
		init(null);
	}
	
	public void init(JSONObject oRequestJSONObject) {
		m_oRequestJSONObject = oRequestJSONObject;
		m_oPaidCheckInfo = null;
		m_sFunctionId = "";
		m_sHandTerminalId = "";
		m_sOperatorId = "";
		m_sOperatorPwd = "";
		m_sPackSeq = "";
		m_sOutletCode = "";
		m_sTableNum = "";
		m_sTableNumWithoutExtension = "";
		m_sTableExtension = "";
		m_sCheckNum = "";
		m_sDiscountId = "";
		m_sMemberNumber = "";
		m_sNextPackSeq = "";
		m_sOldPaymentReference = "";
		m_sErrorMsg = "OK";
		m_iStatus = 1;
	}
	
	// get the received JSON request from request string
	public static String getRequestStringPacket(String sPacket) {
		// Check the format of packet
		String sResult = null;
		//Pattern pattern2 = Pattern.compile("\\{\\\"(.+?)\\\"\\}");
		Pattern pattern2 = Pattern.compile("\\{([^!]+)\\}");
		Matcher matcher2 = pattern2.matcher(sPacket);
		if(matcher2.find()) {
			sResult = matcher2.group(0);
		}
		
		return sResult;
	}
	
	// parse the request JSON
	public boolean parseRequestJSON() {
		m_sFunctionId = m_oRequestJSONObject.optString("funID", "");
		m_sHandTerminalId = m_oRequestJSONObject.optString("htID", "");
		m_sOperatorId = m_oRequestJSONObject.optString("operID", "");
		int iPackSeq = Integer.valueOf(m_oRequestJSONObject.optString("packSeq", "0")).intValue();
		m_sPackSeq = String.valueOf(iPackSeq);
		m_sOutletCode = m_oRequestJSONObject.optString("outletCode", "");
		m_sTableNum = m_oRequestJSONObject.optString("tableNum", "");
		breakdownTableExtension();
		m_sCheckNum = m_oRequestJSONObject.optString("guestCheckNum", "");
		m_sDiscountId = m_oRequestJSONObject.optString("discountId", "");
		m_sNextPackSeq = m_oRequestJSONObject.optString("nextPackSeq", "");
		
		//checking required field existence
		if(requestFieldChecking() == false)
			return false;
		
		if(!m_oRequestJSONObject.optString("operPwd", "").isEmpty()) {
			String sCipherText = m_oRequestJSONObject.optString("operPwd");
			m_sOperatorPwd = getDecryptedString(true, sCipherText);
			int iLen = Integer.parseInt(m_sOperatorPwd.substring(0, 2));
			m_sOperatorPwd = m_sOperatorPwd.substring(2, 2+iLen);
		}else if(m_sFunctionId.equals(FUNCTION_LOGIN)) {
			String sCipherText = m_oRequestJSONObject.optString("operID");
			m_sOperatorId = getDecryptedString(true, sCipherText);
			int iLen = Integer.parseInt(m_sOperatorId.substring(0, 2));
			m_sOperatorId = m_sOperatorId.substring(2, 2+iLen);
		}
		
		//for function: confirm payment and release payment, get the payment list
		if(m_sFunctionId.equals(FUNCTION_CONFIRM_PAYMENT) || m_sFunctionId.equals(FUNCTION_RELEASE_PAYMENT)) {
			if(m_sFunctionId.equals(FUNCTION_CONFIRM_PAYMENT) && m_sTableNum.isEmpty()) {
				m_iStatus = 0;
				m_sErrorMsg = AppGlobal.g_oLang.get()._("missing_table_number");
				return false;
			}
			
			if(m_sFunctionId.equals(FUNCTION_RELEASE_PAYMENT) && m_sCheckNum.isEmpty()) {
				m_iStatus = 0;
				m_sErrorMsg = AppGlobal.g_oLang.get()._("missing_check_number");
				return false;
			}
			
			//prepare the payment list
			m_oPaidCheckInfo = new JSONObject();
			JSONArray oPaymentJSONArray = new JSONArray();
			ArrayList<String> sPaymentTypeList = new ArrayList<String>();
			BigDecimal dPayTotal = BigDecimal.ZERO;
		
			String sPaymentType = m_oRequestJSONObject.optString("paymentType", "");
			if(sPaymentType.isEmpty()) {
				m_iStatus = 0;
				m_sErrorMsg = AppGlobal.g_oLang.get()._("empty_payment_type");
				return false;
			}
		
			StringTokenizer oStrTok = new StringTokenizer(sPaymentType, "|");
			while (oStrTok.hasMoreElements())
				sPaymentTypeList.add(oStrTok.nextToken());
			
			// Check if it is multiple payment, replace the paymentTypeList with currentType in paymentDetail
			boolean bIsMultiplePayment = false, bWithTypeAddedDiscount = false;
			JSONArray oPaymentDetailArray = null;
			if (!sPaymentTypeList.isEmpty() && sPaymentTypeList.get(0).equals(PAYMENT_TYPE_MULTIPLE)){
				oPaymentDetailArray = m_oRequestJSONObject.optJSONArray("paymentDetail");
				if (oPaymentDetailArray != null){
					bIsMultiplePayment = true;
					sPaymentTypeList = new ArrayList<String>();
					for (int j = 0; j < oPaymentDetailArray.length(); j++){
						JSONObject oPaymentDetail = oPaymentDetailArray.optJSONObject(j);
						sPaymentTypeList.add(oPaymentDetail.optString("currentType", ""));
					}
				}
			}
			
			for(int i=0; i<sPaymentTypeList.size(); i++) {
				JSONObject oPaymentInfo = new JSONObject();
				HashMap<String, String> oCardPayments = new HashMap<String, String>();
				String sFieldName = "";
				String sPaymentAmount = "0";
				String sPaymentMethodCode = "";
				
				switch (sPaymentTypeList.get(i)) {
				case PAYMENT_TYPE_CASH:
					sFieldName = "payCashAmt";
					sPaymentMethodCode = m_oInterfaceSetup.optJSONObject("payment_setup").optJSONObject("params").optJSONObject("cash_payment_method").optString("value", "");
					break;
				case PAYMENT_TYPE_COUPON:
					sFieldName = "payCouponAmt";
					sPaymentMethodCode = m_oInterfaceSetup.optJSONObject("payment_setup").optJSONObject("params").optJSONObject("coupon_payment_method").optString("value", "");
					break;
				case PAYMENT_TYPE_CARD:
					sFieldName = "payCardAmt";
					sPaymentMethodCode = m_oInterfaceSetup.optJSONObject("payment_setup").optJSONObject("params").optJSONObject("bank_card_payment_method").optString("value", "");
					
					if(!bIsMultiplePayment) {
						if(!m_oRequestJSONObject.has("responseCode")) {
							m_sErrorMsg = AppGlobal.g_oLang.get()._("missing_card_response_code");
							return false;
						}else if (!m_oRequestJSONObject.optString("responseCode", "").equals(RESPONSE_CODE_SUCCESS)) {
							m_sErrorMsg = AppGlobal.g_oLang.get()._("fail_card_response_code_received");
							return false;
						}
					}
						
					if(!m_oInterfaceSetup.optJSONObject("payment_setup").optJSONObject("params").optJSONObject("bank_card_payment_method_matching").optString("value", "").isEmpty()) {
						String sCardPaymentsString = m_oInterfaceSetup.optJSONObject("payment_setup").optJSONObject("params").optJSONObject("bank_card_payment_method_matching").optString("value");
						StringTokenizer oStrTok2 = new StringTokenizer(sCardPaymentsString, ",");
						while (oStrTok2.hasMoreElements()) {
							String sCardPayment = (oStrTok2.nextToken());
							StringTokenizer oStrTok3 = new StringTokenizer(sCardPayment, ":");
							if(oStrTok3.countTokens() == 2) 
								oCardPayments.put(oStrTok3.nextToken(), oStrTok3.nextToken());
						}
					}
					break;
				case PAYMENT_TYPE_ALIPAY:
					sFieldName = "payCardAmt";
					sPaymentMethodCode = m_oInterfaceSetup.optJSONObject("payment_setup").optJSONObject("params").optJSONObject("alipay_payment_method").optString("value", "");
					break;
				case PAYMENT_TYPE_WECHAT:
					sFieldName = "payCardAmt";
					sPaymentMethodCode = m_oInterfaceSetup.optJSONObject("payment_setup").optJSONObject("params").optJSONObject("wechat_payment_method").optString("value", "");
					break;
				case PAYMENT_TYPE_TAODIANDIAN:
					sFieldName = "payCardAmt";
					sPaymentMethodCode = m_oInterfaceSetup.optJSONObject("payment_setup").optJSONObject("params").optJSONObject("toadiandian_payment_method").optString("value", "");
					break;
				}
				
				try {
					// Multiple payment - override sFieldName
					BigDecimal dPayAmounts = BigDecimal.ZERO;
					if (bIsMultiplePayment) {
						dPayAmounts = new BigDecimal(oPaymentDetailArray.optJSONObject(i).optString("currentAmt", "0"));
						sPaymentAmount = oPaymentDetailArray.optJSONObject(i).optString("currentAmt", "0");
					}else {
						dPayAmounts = new BigDecimal(m_oRequestJSONObject.optString(sFieldName, "0"));
						sPaymentAmount = m_oRequestJSONObject.optString(sFieldName, "0");
					}
					dPayTotal = dPayTotal.add(new BigDecimal(sPaymentAmount));
					oPaymentInfo.put("paymentType", sPaymentTypeList.get(i));
					oPaymentInfo.put("paymentMethodCode", sPaymentMethodCode);
					
					BigDecimal dPayTips = BigDecimal.ZERO;
					if(bIsMultiplePayment) {
						dPayTips = dPayTips.add(new BigDecimal(oPaymentDetailArray.optJSONObject(i).optString("currentTips", "0")));
						oPaymentInfo.put("totalTips", oPaymentDetailArray.optJSONObject(i).optString("currentTips", "0"));
					}else {
						dPayTips = dPayTips.add(new BigDecimal(m_oRequestJSONObject.optString("totalTips", "0")));
						oPaymentInfo.put("totalTips", m_oRequestJSONObject.optString("totalTips", "0"));
					}
					dPayAmounts = dPayAmounts.add(dPayTips.multiply(new BigDecimal("-1.0")));
					dPayTotal = dPayTotal.add(dPayTips.multiply(new BigDecimal("-1.0")));
					
					oPaymentInfo.put("payAmount", dPayAmounts.toPlainString());
					if(sPaymentTypeList.get(i).equals(PAYMENT_TYPE_COUPON)){
						if(m_oRequestJSONObject.has("payCouponCode"))
							oPaymentInfo.put("couponCode", m_oRequestJSONObject.optString("payCouponCode", "0"));
					}
					if(sPaymentTypeList.get(i).equals(PAYMENT_TYPE_CARD)){
						// Multiple payment - check type to add discount
						if (bIsMultiplePayment)
							bWithTypeAddedDiscount = true;
						
						if(m_oRequestJSONObject.has("payCardDiscontAmt")) {
							oPaymentInfo.put("discountAmount", m_oRequestJSONObject.optString("payCardDiscontAmt", "0"));
							dPayTotal = dPayTotal.add(new BigDecimal(m_oRequestJSONObject.optString("payCardDiscontAmt", "0")));
						}
						if(m_oRequestJSONObject.has("cardIsser")) {
							//match card issuer payment method
							String sCardIssuer = "";
							if (bIsMultiplePayment)
								sCardIssuer = oPaymentDetailArray.optJSONObject(i).optString("cardIsser", "");
							else
								sCardIssuer = m_oRequestJSONObject.optString("cardIsser", "");
							if(!sCardIssuer.isEmpty()) {
								if(sCardIssuer.length() > 4)
									sCardIssuer = sCardIssuer.substring(0, 4);
								if(oCardPayments.containsKey(sCardIssuer))
									oPaymentInfo.put("paymentMethodCode", oCardPayments.get(sCardIssuer));
							}
						}
					}
					if(sPaymentTypeList.get(i).equals(PAYMENT_TYPE_ALIPAY) && sPaymentTypeList.get(i).equals(PAYMENT_TYPE_WECHAT) && sPaymentTypeList.get(i).equals(PAYMENT_TYPE_TAODIANDIAN)){
						// Multiple payment - check type to add discount
						if (bIsMultiplePayment)
							bWithTypeAddedDiscount = true;
						
						if(m_oRequestJSONObject.has("payCardDiscontAmt")) {
							oPaymentInfo.put("discountAmount", m_oRequestJSONObject.optString("payCardDiscontAmt", "0"));
							dPayTotal = dPayTotal.add(new BigDecimal(m_oRequestJSONObject.optString("payCardDiscontAmt", "0")));
						}
					}
					
					// Multiple payment - With Discount but without Type to add discount, add back discount to the last type
					if (bIsMultiplePayment && i == sPaymentTypeList.size()-1 && m_oRequestJSONObject.has("payCardDiscontAmt") && !bWithTypeAddedDiscount)
						oPaymentInfo.put("discountAmount", m_oRequestJSONObject.optString("payCardDiscontAmt", "0"));
					
					oPaymentJSONArray.put(oPaymentInfo);
				}catch(JSONException e) {}
			}
			
			// Get check total for multiple payment, Paytotal  = payCardAmt for multiple payment
			if (bIsMultiplePayment)
				dPayTotal = new BigDecimal(m_oRequestJSONObject.optString("checkTotalAmount", "0"));
			
			//check whether have member attached
			if(m_oRequestJSONObject.has("memberCard")) {
				String sCipherText = m_oRequestJSONObject.optString("memberCard", "");
				m_sMemberNumber = getDecryptedString(false, sCipherText);
				int iLen = Integer.parseInt(m_sMemberNumber.substring(0, 2));
				m_sMemberNumber = m_sMemberNumber.substring(2, 2+iLen);
			}
			
			//from the payment information json
			try {
				m_oPaidCheckInfo.put("handTerminalId", m_sHandTerminalId);
				m_oPaidCheckInfo.put("table", m_sTableNumWithoutExtension);
				if(!m_sTableExtension.isEmpty())
					m_oPaidCheckInfo.put("tableExtension", m_sTableExtension);
				if(!m_sCheckNum.isEmpty())
					m_oPaidCheckInfo.put("checkNumber", m_sCheckNum);
				m_oPaidCheckInfo.put("checkTotal", dPayTotal.toPlainString());
				m_oPaidCheckInfo.put("paymentInfos", oPaymentJSONArray);
				m_oPaidCheckInfo.put("memberNumber", m_sMemberNumber);
			}catch(JSONException e) {}
		}
		
		if(m_sFunctionId.equals(FUNCTION_GET_RELEASE_PAYMENT_INFO) || m_sFunctionId.equals(FUNCTION_RELEASE_PAYMENT) ) {
			m_sOldPaymentReference = m_oRequestJSONObject.optString("oldPaymentRefer", "");
		}
		
		return true;
	}
	// generate the response byte array for write back to client through socket
	public byte[] generateResponseByte(int iLength, byte[] oDataByte, byte[] oMd5Byte) {
		byte[] oDataLenHexByte = new byte[2];
		byte[] oResponseByte = new byte[2 + oDataByte.length + oMd5Byte.length];
		
		int iLowByte = iLength % 256;
		int iHighByte = (int)(iLength / 256);
		
		oDataLenHexByte[0] = (byte)iHighByte;
		if(iLowByte > 126)
			iLowByte = (iLowByte - 256);
		oDataLenHexByte[1] = (byte)iLowByte;
		
		System.arraycopy(oDataLenHexByte, 0, oResponseByte, 0, oDataLenHexByte.length);
		System.arraycopy(oDataByte, 0, oResponseByte, oDataLenHexByte.length, oDataByte.length);
		System.arraycopy(oMd5Byte, 0, oResponseByte, (oDataLenHexByte.length + oDataByte.length), oMd5Byte.length);
		
		return oResponseByte;
	}
	
	// construct the basic response packet
	public HashMap<String, Object> constructBasicResponsePacket() {
		HashMap<String, Object> oResponse = new HashMap<String, Object>();
		
		oResponse.put("funID", m_sFunctionId);
		oResponse.put("htID", m_sHandTerminalId);
		oResponse.put("packSeq", m_sPackSeq);
		oResponse.put("operID", m_sOperatorId);
		oResponse.put("outletCode", m_sOutletCode);
		oResponse.put("status", Integer.toString(m_iStatus));
		oResponse.put("errMessage", m_sErrorMsg);
		
		return oResponse;
	}
	
	// construct the login function response packet
	public HashMap<String, Object> constructLoginResponsePacket() {
		HashMap<String, Object> oResponse = new HashMap<String, Object>();
		oResponse.put("funID", m_sFunctionId);
		oResponse.put("htID", m_sHandTerminalId);
		oResponse.put("packSeq", m_sPackSeq);
		oResponse.put("operID", m_sOperatorId);
		String sEmployeeName = "";
		for(Entry<Integer, FuncUser> oEntry: m_oLoginedUserList.entrySet()) {
			FuncUser oFuncUser = oEntry.getValue();
			if(oFuncUser.getUser().getLogin().equals(m_sOperatorId)) {
				sEmployeeName = oFuncUser.getUserName(AppGlobal.g_oCurrentLangIndex.get());
				break;
			}
		}
		oResponse.put("employeeName", sEmployeeName);
		oResponse.put("outletCode", m_sOutletCode);
		oResponse.put("status", Integer.toString(m_iStatus));
		oResponse.put("errMessage", m_sErrorMsg);
		
		// sPayType
		JSONObject oPaymentSetupJSONObject = m_oInterfaceSetup.optJSONObject("payment_setup").optJSONObject("params");
		String sCharSet = m_oInterfaceSetup.optJSONObject("general_setup").optJSONObject("params").optJSONObject("pax_locale").optString("value");
		String sSupportCashBankCard = oPaymentSetupJSONObject.optJSONObject("support_cash_and_bank_card").optString("value");
		String sSupportCoupon = oPaymentSetupJSONObject.optJSONObject("support_coupon").optString("value");
		String sSupportPrepaidCard = oPaymentSetupJSONObject.optJSONObject("support_prepaid_card").optString("value");
		String sSupportAlipay = oPaymentSetupJSONObject.optJSONObject("support_alipay").optString("value");
		String sSupportWeChat = oPaymentSetupJSONObject.optJSONObject("support_wechat").optString("value");
		String sSupportTaoDianDian = oPaymentSetupJSONObject.optJSONObject("support_taodiandian").optString("value");

		String sPayType = "";
		if(sSupportCashBankCard.compareTo("1") == 0)
			sPayType += PAYMENT_TYPE_CASH;
		
		if(sSupportCoupon.compareTo("1") == 0) {
			if(!sPayType.isEmpty())
				sPayType += "|";
			sPayType += PAYMENT_TYPE_COUPON;
		}
		
		if(sSupportCashBankCard.compareTo("1") == 0) {
			if(!sPayType.isEmpty())
				sPayType += "|";
			sPayType += PAYMENT_TYPE_CARD;
		}
		
		if(sSupportPrepaidCard.compareTo("1") == 0) {
			if(!sPayType.isEmpty())
				sPayType += "|";
			sPayType += PAYMENT_TYPE_GIFTCARD;
		}
		
		if(sSupportAlipay.compareTo("1") == 0) {
			if(!sPayType.isEmpty())
				sPayType += "|";
			sPayType += PAYMENT_TYPE_ALIPAY;
		}
		
		if(sSupportWeChat.compareTo("1") == 0) {
			if(!sPayType.isEmpty())
				sPayType += "|";
			sPayType += PAYMENT_TYPE_WECHAT;
		}
		
		if(sSupportTaoDianDian.compareTo("1") == 0) {
			if(!sPayType.isEmpty())
				sPayType += "|";
			sPayType += PAYMENT_TYPE_TAODIANDIAN;
		}
		
		oResponse.put("sPayType", sPayType);

		// needMemberCard, needCouponCode
		JSONObject oGeneralSetup = m_oInterfaceSetup.optJSONObject("general_setup").optJSONObject("params");
		String sNeedMemberCard = oGeneralSetup.optJSONObject("need_member_card").optString("value");
		String sNeedCouponCode = oGeneralSetup.optJSONObject("need_coupon_code").optString("value");
		oResponse.put("needMemberCard", sNeedMemberCard);
		oResponse.put("needCouponCode", sNeedCouponCode);
		
		// sdiscountList
		String sDiscountList = m_oInterfaceSetup.optJSONObject("discount_setup").optJSONObject("params").optJSONObject("discount_list").optString("value");
		if(!sDiscountList.isEmpty()) {
			int iDiscountNameLen = 14;
			String sDiscountCodeArray[] = sDiscountList.split(",");
			DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
			PosBusinessDay oBusinessDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay();
			PosDiscountTypeList oCheckDiscTypeList = new PosDiscountTypeList();
			oCheckDiscTypeList.readDiscountListByOutletId("check", AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncStation.get().getStationId(), dateFormat.print(oBusinessDay.getDate()), oBusinessDay.isHoliday(), oBusinessDay.isDayBeforeHoliday(), oBusinessDay.isSpecialDay(), oBusinessDay.isDayBeforeSpecialDay(), oBusinessDay.getDayOfWeek(), AppGlobal.g_oFuncUser.get().getUserGroupList(), false);
			PosDiscountTypeList oItemDiscTypeList = new PosDiscountTypeList();
			oItemDiscTypeList.readDiscountListByOutletId("item", AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncStation.get().getStationId(), dateFormat.print(oBusinessDay.getDate()), oBusinessDay.isHoliday(), oBusinessDay.isDayBeforeHoliday(), oBusinessDay.isSpecialDay(), oBusinessDay.isDayBeforeSpecialDay(), oBusinessDay.getDayOfWeek(), AppGlobal.g_oFuncUser.get().getUserGroupList(), false);
			
			String sResultDiscountList = "";
			for(int i = 0; i < sDiscountCodeArray.length; i++) {
				String sDiscountCode = sDiscountCodeArray[i];
				
				PosDiscountType oPosDiscountType = oCheckDiscTypeList.getDiscountTypeByCode(sDiscountCode);
				if(oPosDiscountType != null) {
					String sValue = "00";
					if(oPosDiscountType.getClassKey().compareTo("member_discount") == 0)
						sValue = "01";
					
					String sDiscountName = getSubtring(oPosDiscountType.getName(AppGlobal.g_oCurrentLangIndex.get()), iDiscountNameLen, sCharSet);
					sResultDiscountList += sDiscountName+oPosDiscountType.getCode()+sValue+"|";
				} else {
					oPosDiscountType = oItemDiscTypeList.getDiscountTypeByCode(sDiscountCode);
					if(oPosDiscountType != null) {
						String sValue = "00";
						if(!sResultDiscountList.isEmpty())
							sResultDiscountList += "|";
						if(oPosDiscountType.getClassKey().compareTo("member_discount") == 0)
							sValue = "01";
						String sDiscountName = getSubtring(oPosDiscountType.getName(AppGlobal.g_oCurrentLangIndex.get()), iDiscountNameLen, sCharSet);
						sResultDiscountList += sDiscountName+oPosDiscountType.getCode()+sValue+"|";
					}
				}
			}
			oResponse.put("discountList", sResultDiscountList);
		}
		
		return oResponse;
	}
	
	// get payment void code
	public String getPaymentVoidCode() {
		JSONObject oPaymentSetupJSONObject = m_oInterfaceSetup.optJSONObject("payment_setup").optJSONObject("params");
		String sVoidReasonCode = oPaymentSetupJSONObject.optJSONObject("void_payment_reason_code").optString("value", "");
		return sVoidReasonCode;
	}
	
	// get discount void code
	public String getDiscountVoidCode() {
		JSONObject oPaymentSetupJSONObject = m_oInterfaceSetup.optJSONObject("discount_setup").optJSONObject("params");
		String sVoidReasonCode = oPaymentSetupJSONObject.optJSONObject("void_discount_reason_code").optString("value", "");
		return sVoidReasonCode;
	}
	
	// check whether login function
	public boolean isUserLogined() {
		//check operator logined
		boolean bLogined = false;
		for(Entry<Integer, FuncUser> oEntry: m_oLoginedUserList.entrySet()) {
			FuncUser oFuncUser = oEntry.getValue();
			if(oFuncUser.getUser().getLogin().equals(m_sOperatorId)) {
				bLogined = true;
				break;
			}
		}
		if(!bLogined) {
			m_iStatus = 0;
			m_sErrorMsg = AppGlobal.g_oLang.get()._("user_not_login");
			return false;
		}
		
		return true;
	}
	
	// user login and store the user to list
	public void userLogin(FuncUser oFuncUser) {
		m_sOperatorId = String.valueOf(oFuncUser.getLoginId());
		if(!m_oLoginedUserList.containsKey(oFuncUser.getUserId()))
			m_oLoginedUserList.put(oFuncUser.getUserId(), oFuncUser);
	}
	
	// user logout and remove the user from list
	public boolean userLogout(String sLogin) {
		boolean bUserFind = false;
		int iTargetUserId = 0;
		
		for(Entry<Integer, FuncUser> entry:m_oLoginedUserList.entrySet()){
			FuncUser oFuncUser = entry.getValue();
			if(oFuncUser.getUser().getLogin().equals(sLogin)) {
				iTargetUserId = entry.getKey();
				bUserFind = true;
				break;
			}
		}
		
		if(bUserFind) {
			m_oLoginedUserList.remove(iTargetUserId);
		}
		
		return true;
	}
	
	// generate payment reference for paid check
	public String generatePaymentReference(String sCloseTime) {
		String sPaymentRef = "";
		String sTable = StringLib.fillZeroAtBegin(m_sTableNum, 6);
		
		if(m_oPaidCheckInfo.optJSONArray("paymentInfos") != null && m_oPaidCheckInfo.optJSONArray("paymentInfos").length() > 0) {
			for(int i=0; i<m_oPaidCheckInfo.optJSONArray("paymentInfos").length(); i++) {
				JSONObject oPaymentInfo = m_oPaidCheckInfo.optJSONArray("paymentInfos").optJSONObject(i);
				sPaymentRef = sPaymentRef + oPaymentInfo.optString("paymentType", "  ") + sTable + sCloseTime + "|";
			}
		}
		
		return sPaymentRef;
	}
	
	// parse guest check details
	public void parseGuestCheck(String sGuestCheckUrl) {
		int iImagePacketLength = 700 - 16 - 50; // (Maximum length - length of "<<START>><<END>>" - length of special character handling)
		String sCheckImageString = "";
		char cLF = (char)(0x0A);
		
		//clear the previous check image details
		m_oGuestCheckStringList = null;
		
		//get the check image content
		try {
			URL oGuestCheck = new URL(sGuestCheckUrl);
			BufferedReader oBufferedReader = new BufferedReader(new InputStreamReader(oGuestCheck.openStream()));
			
			String sInputLine;
			while ((sInputLine = oBufferedReader.readLine()) != null) 
				sCheckImageString = sCheckImageString + sInputLine + cLF;
			oBufferedReader.close();
		}catch(IOException e) {
			AppGlobal.stack2Log(e);
		}
		
		//calculate the number of packet
		int iStrLen = sCheckImageString.length();
		int iNumberOfSection = (iStrLen / iImagePacketLength) + 1;
		m_oGuestCheckStringList = new ArrayList<String>();
		
		//form the packet arrays
		for(int i=1; i<=iNumberOfSection; i++) {
			int iStartIndex = 0 + ((i-1) * iImagePacketLength);
			int iEndIndex = iImagePacketLength * i;
			String sImagePacket = "<<START>>";
			if(iEndIndex > iStrLen)
				sImagePacket = sImagePacket + sCheckImageString.substring(iStartIndex);
			else
				sImagePacket = sImagePacket + sCheckImageString.substring(iStartIndex, iEndIndex);
			sImagePacket = sImagePacket + "<<END>>";
			m_oGuestCheckStringList.add(sImagePacket);
		}
	}
	
	// handle guest check special character
	public String handleSpecialCharacterForCheckImage(String sCheckImage) {
		int iIndexOfStart = sCheckImage.indexOf("<<START>>");
		
		//change string '\n' to 0x0A
		int iTargetIndex = -1;
		do {
			iTargetIndex = sCheckImage.indexOf("\\n", iIndexOfStart);
			if(iTargetIndex >= 0) 
				sCheckImage = sCheckImage.substring(0, iTargetIndex) + (char)(0x0A) + sCheckImage.substring(iTargetIndex+2);
		}while(iTargetIndex >= 0);
		
		//change string  '\\' to '\'
		iTargetIndex = -1;
		do {
			iTargetIndex = sCheckImage.indexOf("\\\\", iIndexOfStart);
			if(iTargetIndex >= 0) 
				sCheckImage = sCheckImage.substring(0, iTargetIndex) + "\\" + sCheckImage.substring(iTargetIndex+2);
		}while(iTargetIndex >= 0);
		
		
		//change string  '%' to '%%%'
		iTargetIndex = -1;
		int iNextStartIndex = iIndexOfStart;
		do {
			iTargetIndex = sCheckImage.indexOf("%", iNextStartIndex);
			iNextStartIndex = iTargetIndex + 3;
			if(iTargetIndex >= 0) 
				sCheckImage = sCheckImage.substring(0, iTargetIndex) + "%%%" + sCheckImage.substring(iTargetIndex+1);
		}while(iTargetIndex >= 0);
		
		return sCheckImage;
	}
	
	// check payment reference for relesase payment
	public boolean checkPaymentReferenceForReleasePayment(ArrayList<PosCheckPayment> oPosCheckPayments) {
		int iPaymentReferenceCount = 0;
		String[] split = m_sOldPaymentReference.split("\\|");
		
		for(int i = 0; i < split.length; i++) {
			String sPaymentReference = split[i];
			
			boolean bInterfaceMatch = false, bReferenceMatch = false;
			for (PosCheckPayment oCheckPayment: oPosCheckPayments) {
				for(PosCheckExtraInfo oPosCheckExtraInfo: oCheckPayment.getCheckExtraInfoArrayList()){
					if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERFACE_ID)
							&& oPosCheckExtraInfo.getValue().equals(Integer.toString(m_oInterfaceConfig.getInterfaceId())))
						bInterfaceMatch = true;
					
					if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_REFERENCE) 
							&& oPosCheckExtraInfo.getValue().equals(sPaymentReference)){
						bReferenceMatch = true;
					}
				}
				
				if(bInterfaceMatch && bReferenceMatch) {
					iPaymentReferenceCount++;
					break;
				}
			}
		}
		
		if(iPaymentReferenceCount != split.length) {
			m_iStatus = 0;
			m_sErrorMsg = AppGlobal.g_oLang.get()._("payment_not_found");
			return false;
		}
		
		return true;
	}
	
	public String getLastErrorMessage() {
		return m_sErrorMsg;
	}
	
	public boolean isLoginFunction() {
		if(m_sFunctionId.equals(FUNCTION_LOGIN))
			return true;
		else
			return false;
	}
	
	public boolean isPrintCheckFunction() {
		if(m_sFunctionId.equals(FUNCTION_PRINT_CHECK))
			return true;
		else
			return false;
	}
	
	public String getOutletCode() {
		return m_sOutletCode;
	}
	
	public String getOperatorId() {
		return m_sOperatorId;
	}
	
	public String getOperatorPwd() {
		return m_sOperatorPwd;
	}
	
	public String getTableNum() {
		return m_sTableNum;
	}
	
	public String getTableNumWithoutExtension() {
		return m_sTableNumWithoutExtension;
	}
	
	public String getTableExtension() {
		return m_sTableExtension;
	}
	
	public String getCheckNum() {
		return m_sCheckNum;
	}
	
	public String getDiscountId() {
		return m_sDiscountId;
	}
	
	public String getNextPackSeq() {
		return m_sNextPackSeq;
	}
	
	public JSONObject getPaymentInfo() {
		return m_oPaidCheckInfo;
	}
	
	public ArrayList<String> getGuestCheckStingList() {
		return m_oGuestCheckStringList;
	}
	
	// Get the logged in user with operator id
	public FuncUser getFuncUser() {
		for(Entry<Integer, FuncUser> oEntry: m_oLoginedUserList.entrySet()) {
			FuncUser oFuncUser = oEntry.getValue();
			if(oFuncUser.getUser().getLogin().equals(m_sOperatorId))
				return oFuncUser;
		}
		m_iStatus = 0;
		m_sErrorMsg = AppGlobal.g_oLang.get()._("no_such_user");
		return null;
	}
	
	public boolean checkUserAuthority(int iFunctionId, String sFuncKey) {
		// Check input user authority
		FuncUser oTempFuncUser = this.getFuncUser();
		if(oTempFuncUser == null)
			return false;
		
		boolean bHaveAuthority = false;
		PosFunctionAclCache oTempFuncAclCache = new PosFunctionAclCache();
		if (oTempFuncAclCache.readByFuncKeyIdAndUserAndOutletId(iFunctionId, sFuncKey,
				oTempFuncUser.getUser().getUserId(), oTempFuncUser.getUser().getRole(), oTempFuncUser.getUser().getUserDeptId(),
				AppGlobal.g_oFuncOutlet.get().getOutletId())) {
			if (oTempFuncAclCache.isAllow())
				bHaveAuthority = true;
		}
		
		if (!bHaveAuthority) {
			m_iStatus = 0;
			m_sErrorMsg = AppGlobal.g_oLang.get()._("do_not_have_authority_to_perform_the_function");
			return false;
		}
		return true;
	}
	
	// packing response json
	public JSONObject packingResponseJSON(HashMap<String, Object> oResponsePacket) {
		JSONObject responseJSONObject = new JSONObject(oResponsePacket);
		// To make the response packet length has at least 256 characters
		if(responseJSONObject.toString().length() < 256) {
			String sDummyStr = "";
			for(int i = 0; i < 256-responseJSONObject.toString().length(); i++) {
				sDummyStr += "a";
			}
			
			try {
				responseJSONObject.put("dummy", sDummyStr);
			}catch (JSONException e) {}
		}
		
		return responseJSONObject;
	}
	
	// checking existence of requried field
	private boolean requestFieldChecking() {
		if(m_sFunctionId.isEmpty()) {
			m_iStatus = 0;
			m_sErrorMsg = AppGlobal.g_oLang.get()._("missing_function_id");
			return false;
		}
		
		if(!m_sOutletCode.isEmpty() && !m_sOutletCode.equals(AppGlobal.g_oFuncOutlet.get().getOutletCode())) {
			m_iStatus = 0;
			m_sErrorMsg = AppGlobal.g_oLang.get()._("invalid_outlet_code");
			return false;
		}
		
		if(m_sHandTerminalId.isEmpty()) {
			m_iStatus = 0;
			m_sErrorMsg = AppGlobal.g_oLang.get()._("missing_work_station");
			return false;
		}
		
		if(m_sPackSeq.isEmpty()) {
			m_iStatus = 0;
			m_sErrorMsg = AppGlobal.g_oLang.get()._("missing_sequence_no");
			return false;
		}
		
		if(m_sOperatorId.isEmpty()) {
			m_iStatus = 0;
			m_sErrorMsg = AppGlobal.g_oLang.get()._("missing_employee");
			return false;
		}
		
		return true;
	}
	
	// decrypt the user password or card number
	private String getDecryptedString(boolean bUserPassword, String src) {
		String sKey = "";
		if(bUserPassword)
			sKey = m_oInterfaceSetup.optJSONObject("encryption_key").optJSONObject("params").optJSONObject("user_password").optString("value");
		else
			sKey = m_oInterfaceSetup.optJSONObject("encryption_key").optJSONObject("params").optJSONObject("card_no").optString("value");
		
		try {
			byte[] tmp = hexToByte(sKey);
			byte[] key = new byte[24];
			System.arraycopy(tmp, 0, key, 0, 16);
			System.arraycopy(tmp, 0, key, 16, 8);
			Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "DESede"));
			byte[] plaintextByte = cipher.doFinal(hexToByte(src));
			
			String sPlainText = "";
			if (bUserPassword && src.length() > 16)
				sPlainText = hexToAscii(byteToHex(plaintextByte));
			else
				sPlainText = byteToHex(plaintextByte);
			return sPlainText;
		 } catch (java.security.NoSuchAlgorithmException e1) {
			 AppGlobal.stack2Log(e1);
		 } catch (javax.crypto.NoSuchPaddingException e2) {
			 AppGlobal.stack2Log(e2);
		 } catch (java.lang.Exception e3) {
			 AppGlobal.stack2Log(e3);
		 }
		
		return null;
	}
	
	private String hexToAscii(String hex) {
		StringBuilder sAscii = new StringBuilder();
		for (int i = 0; i < hex.length(); i+=2) {
			String str = hex.substring(i, i+2);
			sAscii.append((char)Integer.parseInt(str, 16));
		}
		
		return sAscii.toString();
	}
	
	private byte[] hexToByte(String hex) {
		if ((hex.length() & 0x01) == 0x01)
			throw new IllegalArgumentException();
		byte[] bytes = new byte[hex.length() / 2];
		for (int idx = 0; idx < bytes.length; ++idx) {
			int hi = Character.digit((int) hex.charAt(idx * 2), 16);
			int lo = Character.digit((int) hex.charAt(idx * 2 + 1), 16);
			if ((hi < 0) || (lo < 0))
				throw new IllegalArgumentException();
			bytes[idx] = (byte) ((hi << 4) | lo);
		}
		return bytes;
	}

	private String byteToHex(byte[] bytes) {
		char[] hex = new char[bytes.length * 2];
		for (int idx = 0; idx < bytes.length; ++idx) {
			int hi = (bytes[idx] & 0xF0) >>> 4;
			int lo = (bytes[idx] & 0x0F);
			hex[idx * 2] = (char) (hi < 10 ? '0' + hi : 'A' - 10 + hi);
			hex[idx * 2 + 1] = (char) (lo < 10 ? '0' + lo : 'A' - 10 + lo);
		}
		return new String(hex);
	}

	public String getSubtring(String sStr, int iLen, String sCharSet) {
		String sResultStr = "";

		if(sStr == null || sStr.isEmpty())
			return sStr;

		try {
			for (int j = 0, iByteLen = 0; j < sStr.length(); j++) {
				iByteLen += String.valueOf(sStr.charAt(j)).getBytes(sCharSet).length;

				if (	iByteLen > iLen) {
					sResultStr = sStr.substring(0, j);
					break;
				}
			}
			
			if(sResultStr.isEmpty())
				sResultStr = sStr;
		} catch (UnsupportedEncodingException e) {
			AppGlobal.stack2Log(e);
		}
	
		return sResultStr;
	}
	
	public void breakdownTableExtension() {
		int iStrLength = m_sTableNum.length();
		int iExtensionIndex = -1;
		
		for(int i=0; i<iStrLength; i++) {
			String sTableSubString = null;
			if(i == (iStrLength - 1))
				sTableSubString = m_sTableNum.substring(i);
			else
				sTableSubString = m_sTableNum.substring(i, (i+1));
			
			try {
				Integer dNumeric = Integer.parseInt(sTableSubString);
			}catch(NumberFormatException exception) {
				iExtensionIndex = i;
				AppGlobal.stack2Log(exception);
			}
		}
		
		if(iExtensionIndex > 0) {
			m_sTableNumWithoutExtension = m_sTableNum.substring(0, iExtensionIndex);
			m_sTableExtension = m_sTableNum.substring(iExtensionIndex);
		}else
			m_sTableNumWithoutExtension = m_sTableNum.substring(0);
		
		try {
			Integer dNumeric = Integer.parseInt(m_sTableNumWithoutExtension);
		}catch(NumberFormatException exception) {
			m_sTableNumWithoutExtension = "0";
			m_sTableExtension = m_sTableNum;
		}
		
	}
}
