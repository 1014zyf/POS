package app;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import om.InfVendor;
import om.PosCheckExtraInfo;
import om.PosCheckPayment;
import om.PosInterfaceConfig;
import om.PosPaymentMethod;

public class FuncGamingInterface {
	class GamingInterfaceResponseInfo {
		//Executive Comp Inquiry Response
		String sUserNumber = "";
		BigDecimal dBalancePerTransaction = BigDecimal.ZERO;
		BigDecimal dBalancePerDay = BigDecimal.ZERO;
		BigDecimal dBalancePerMonth = BigDecimal.ZERO;
		
		//Patron Inquiry Response
		String sMemberNumber = "";
		String sMemberName = "";
		String sPointsTotal = "";
		BigDecimal dPointsDepartment = BigDecimal.ZERO;
		
		//Post Online Comp
		String sApprovedAmount = "";
		String sCompStatus = "";
		
		//Member Photo
		String sMemberPhoto = "";
		String sMemberSignature = "";
		
		String sCompNumber = "";
		
		//For encrepted Display
	}
	
	class GamingInterfaceCardEnquiryInfo{
		String sAccountNumber = "";
		String sCardType = "";
		String sClubState ="";
		String sTitle ="";
		String sFirstName ="";
		String sLastName ="";
		String sChineseName ="";
		String sDiscountPercentage ="";
		HashMap<String, String> oInfoList = new HashMap<String, String>();
	}

	public class PostingInfo {
		int iInterfaceId;
		String sInterfaceCode;
		String sOutletCode;
		String sStationCode;
		String sEmployee;
		String sCheckNumber;
		String sInputMethod;
		BigDecimal dPostAmount;
		String sMemberNumber;
		String sCardNumber;
	}
	
	class BallyPaymentPostingInfo{
		String sAccountNumber = "";
		String sPresentSlip = "";
		String sPrizeCode = "";
		String sOutletName = "";
		String sOutletCode = "";
		String sCheckNumber = "";
		String sPaymentCode = "";
		String sCurrency = "";
		String sTotalAmount = "";
		String sInterfaceId = "";
		String sInterfaceCode = "";
		String sOutletId = "";
		String sStationCode = "";
		String sPostingTimeZone = "";
	}
	
	class BallyPaymentResponseInfo{
		String sGpcTranId = "";
		String sCmpTranId = "";
	}

	class BallyPaymentVoidPostingInfo{
		String sAccountNumber = "";
		String sGpcTranId = "";
		String sStationCode ="";
		String sOutletName = "";
		String sOutletCode = "";
		String sOutletId ="";
		String sInterfaceCode = "";
		String sInterfaceId = "";
	}
	
	//payment methods of Gaming interface
	public static String ERROR_TYPE_FAIL_RESPONSE = "s";
	
	public static final String POST_EXECUTIVE_COMP = "post_executive_comp";
	public static final String POST_ONLINE_COMP = "post_online_comp";
	public static final String COMP_REDEMPTION = "comp_redemption";
	public static final String GIFT_CERTIFICATE_SALE = "gift_certificate_sale";
	public static final String GIFT_CERTIFICATE_REDEMPTION = "gift_certificate_redemption";
	public static final String COUPON_REDEMPTION = "coupon_redemption";
	
	public static final String PATRON_INQUIRY = "patron_inquiry";
	public static final String EXECUTIVE_COMP_INQUIRY = "executive_comp_inquiry";
	public static final String POST_SALE_DETAIL = "post_sale_detail";
	
	public static final String CARD_ENQUIRY = "card_enquiry";
	
	public static final String ONLINE_POSTING = "online_posting";
	public static final String OFFLINE_POSTING = "offline_posting";
	
	public static final String SWIPE_CARD = "swipe_card";
	public static final String KEYIN = "keyin";
	public static final String SCAN_QR_CODE = "scan_qr_code";
	
	public static final String PAYMENT_TYPE_DOLLAR = "d";
	public static final String PAYMENT_TYPE_COMP_SLIP = "c";
	
	public static final String PAYMENT_CONTROL_NO_CONTROL = "";
	public static final String PAYMENT_CONTROL_NOT_ALLOW_FOR_ITEM_DISC = "i";
	public static final String PAYMENT_CONTROL_NOT_ALLOW_FOR_CHECK_DISC = "c";
	public static final String PAYMENT_CONTROL_NOT_ALLOW_FOR_ITEM_CHECK_DISC = "b";
	
	//global variables
	private PosInterfaceConfig m_oGamingInterface;
	public GamingInterfaceResponseInfo m_oLastGamingPostResponseInfo;
	public GamingInterfaceCardEnquiryInfo m_oLastCardEnquiryResponseInfo;
	public BallyPaymentPostingInfo m_oBallyPaymentPostingInfo;
	public BallyPaymentResponseInfo m_oBallyPaymentResponseInfo;
	private String m_sLastErrorMessage;
	
	//initialize function
	public FuncGamingInterface() {}
	
	public FuncGamingInterface(PosInterfaceConfig oGamingInterface) {
		m_oGamingInterface = oGamingInterface;
		m_sLastErrorMessage = "";
	}
	
	public boolean doGemsGamingInterfacePosting(HashMap<String, String> oPostingInfo) {
		oPostingInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oPostingInfo.put("stationCode", AppGlobal.g_oFuncStation.get().getCode());
		oPostingInfo.put("employeeNum",  AppGlobal.g_oFuncUser.get().getUserNumber());

		JSONObject oResultJSONObject = new JSONObject();
		m_oGamingInterface = new PosInterfaceConfig();
		oResultJSONObject = m_oGamingInterface.doGEMSGamingInterfacePosting(oPostingInfo);

		if (oResultJSONObject == null) {
			m_sLastErrorMessage = m_oGamingInterface.getLastErrorMessage();
				return false;
		} else {
			System.err.println(oResultJSONObject.toString());
			//if(!oResultJSONObject.has("postingResult") || !oResultJSONObject.has("resultDetails")) {
			if(oResultJSONObject.has("errorCode")) {
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("result_error");
				try{
					m_sLastErrorMessage = getErrorMessage(oResultJSONObject.getInt("errorCode"));
				}
				catch (JSONException jsone){
				}
				return false;
			} else 
				m_oLastGamingPostResponseInfo = new GamingInterfaceResponseInfo();

			oResultJSONObject = oResultJSONObject.optJSONObject("resultDetails");
			setGamingResponseInfo(oResultJSONObject);
		}
		return true;
	}
	
	public void setGamingResponseInfo(JSONObject oResultJSONObject) {
		//Comp Inquiry
		if (oResultJSONObject.has("userNumber") && !oResultJSONObject.optString("userNumber").isEmpty())
			m_oLastGamingPostResponseInfo.sUserNumber = oResultJSONObject.optString("userNumber");
		if (oResultJSONObject.has("balancePerTransaction") && !oResultJSONObject.optString("balancePerTransaction").isEmpty())
			m_oLastGamingPostResponseInfo.dBalancePerTransaction = BigDecimal.valueOf(oResultJSONObject.optDouble("balancePerTransaction")).setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), RoundingMode.CEILING);
		if (oResultJSONObject.has("balancePerDay") && !oResultJSONObject.optString("balancePerDay").isEmpty())
			m_oLastGamingPostResponseInfo.dBalancePerDay = BigDecimal.valueOf(oResultJSONObject.optDouble("balancePerDay")).setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), RoundingMode.CEILING);
		if (oResultJSONObject.has("balancePerMonth") && !oResultJSONObject.optString("balancePerMonth").isEmpty())
			m_oLastGamingPostResponseInfo.dBalancePerMonth = BigDecimal.valueOf(oResultJSONObject.optDouble("balancePerMonth")).setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), RoundingMode.CEILING);
		
		//Parton Inquiry
		if (oResultJSONObject.has("memberNumber") && !oResultJSONObject.optString("memberNumber").isEmpty())
			m_oLastGamingPostResponseInfo.sMemberNumber = oResultJSONObject.optString("memberNumber");
		if (oResultJSONObject.has("memberName") && !oResultJSONObject.optString("memberName").isEmpty())
			m_oLastGamingPostResponseInfo.sMemberName = oResultJSONObject.optString("memberName");
		if (oResultJSONObject.has("pointsTotal") && !oResultJSONObject.optString("pointsTotal").isEmpty())
			m_oLastGamingPostResponseInfo.sPointsTotal = oResultJSONObject.optString("pointsTotal");
		if (oResultJSONObject.has("pointsDepartment") && !oResultJSONObject.optString("pointsDepartment").isEmpty())
			m_oLastGamingPostResponseInfo.dPointsDepartment = BigDecimal.valueOf(oResultJSONObject.optDouble("pointsDepartment")).setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), RoundingMode.CEILING);
		
		//Member Photo
		if (oResultJSONObject.has("memberPhoto") && !oResultJSONObject.optString("memberPhoto").isEmpty())
			m_oLastGamingPostResponseInfo.sMemberPhoto = oResultJSONObject.optString("memberPhoto");
		if (oResultJSONObject.has("memberSignature") && !oResultJSONObject.optString("memberSignature").isEmpty())
			m_oLastGamingPostResponseInfo.sMemberSignature = oResultJSONObject.optString("memberSignature");
		
		//Post Online Comp
		if (oResultJSONObject.has("approvedAmount") && !oResultJSONObject.optString("approvedAmount").isEmpty())
			m_oLastGamingPostResponseInfo.sApprovedAmount = oResultJSONObject.optString("approvedAmount");
		if (oResultJSONObject.has("compStatus") && !oResultJSONObject.optString("compStatus").isEmpty())
			m_oLastGamingPostResponseInfo.sCompStatus = oResultJSONObject.optString("compStatus");
		
		// Comp Redemption
		if (oResultJSONObject.has("compNumber") && !oResultJSONObject.optString("compNumber").isEmpty())
			m_oLastGamingPostResponseInfo.sCompNumber = oResultJSONObject.optString("compNumber");
	}
	
	public boolean postSalesDetail(FuncCheck oFuncCheck, FuncPayment oFuncPayment, PosInterfaceConfig oPosInterfaceConfig) {
		PostingInfo oPostingInfo = new PostingInfo();
		
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.sStationCode = AppGlobal.g_oFuncStation.get().getCode();
		oPostingInfo.sEmployee = AppGlobal.g_oFuncUser.get().getUserNumber();
		m_oGamingInterface = new PosInterfaceConfig();
		m_oGamingInterface = oPosInterfaceConfig;
		oPostingInfo.iInterfaceId = m_oGamingInterface.getInterfaceId();
		oPostingInfo.sInterfaceCode = m_oGamingInterface.getInterfaceCode();

		if (oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_GAMING_INTERFACE,
				PosCheckExtraInfo.VARIABLE_INPUT_TYPE, 0))
			oPostingInfo.sInputMethod = oFuncCheck.getCheckExtraInfoBySectionAndVariable(
					PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_INPUT_TYPE, 0);
		if (oPostingInfo.sInputMethod != null && !oPostingInfo.sInputMethod.isEmpty()) {
			if (oPostingInfo.sInputMethod.equals("swipe") && oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(
					PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_CARD_NO, 0))
				oPostingInfo.sCardNumber = oFuncCheck.getCheckExtraInfoBySectionAndVariable(
						PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_CARD_NO, 0);
			else if (oPostingInfo.sInputMethod.equals("keyin")
					&& oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(
							PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0))
				oPostingInfo.sMemberNumber = oFuncCheck.getCheckExtraInfoBySectionAndVariable(
						PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0);
		}
		// get check number
		if (!AppGlobal.g_oFuncOutlet.get().getBusinessDay().isCheckNumGeneratedByOutlet() && !oFuncCheck.isOldCheck())
			oPostingInfo.sCheckNumber = AppGlobal.g_oFuncStation.get().getNextCheckPrefixNumber(false);
		else
			oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();

		JSONObject oCheckInformationJSONObject = oFuncCheck.contrustPmsPostingCheckInformation(null);
		JSONObject oCheckBusinessDayInformationJSONObject = new JSONObject();
		JSONArray oPaymentsJSONArray = new JSONArray();

		for (PosCheckPayment oPosCheckPayment : oFuncCheck.getCheckPaymentList()) {
			if (oPosCheckPayment.isDelete())
				continue;
			JSONObject oTempJSON = oPosCheckPayment.constructAddSaveJSON(true);
			
			// get payment method code
			PosPaymentMethod oPaymentMethod = oFuncPayment.getPaymentMethodList().getPaymentMethodList()
					.get(oPosCheckPayment.getPaymentMethodId());
			try {
				oTempJSON.put("paymentMethodCode", oPaymentMethod.getPaymentCode());
			} catch (JSONException jsone) {
				jsone.printStackTrace();
				return false;
			}
			if (oTempJSON != null)
				oPaymentsJSONArray.put(oTempJSON);
		}
		try {
			oCheckBusinessDayInformationJSONObject.put("itemDecimal", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getItemDecimal());
			oCheckBusinessDayInformationJSONObject.put("scDecimal", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getScDecimal());
			oCheckBusinessDayInformationJSONObject.put("taxDecimal", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getTaxDecimal());
			oCheckBusinessDayInformationJSONObject.put("discDecimal", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDiscDecimal());
			oCheckBusinessDayInformationJSONObject.put("checkDecimal", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getCheckDecimal());
			oCheckBusinessDayInformationJSONObject.put("payDecimal", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getPayDecimal());
			oCheckInformationJSONObject.put("payment", oPaymentsJSONArray);
			oCheckInformationJSONObject.put("businessDay", oCheckBusinessDayInformationJSONObject);
		} catch (JSONException jsone) {
			jsone.printStackTrace();
			return false;
		}
		JSONObject oResponseJSONObject = m_oGamingInterface.doGamingSalesDetailPost(oCheckInformationJSONObject,
				fromGamingPostingJSONObject(oPostingInfo));
		if (oResponseJSONObject == null) {
			m_sLastErrorMessage = m_oGamingInterface.getLastErrorMessage();
			return false;
		} else {
			System.err.println(oResponseJSONObject.toString());
			if (!oResponseJSONObject.has("postingResult") || !oResponseJSONObject.has("resultDetails")) {
				if (oResponseJSONObject.has("errorMessage"))
					m_sLastErrorMessage = oResponseJSONObject.optString("errorMessage");
				else
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("result_error");
				return false;
			}
		}
		return true;
	}

	private JSONObject fromGamingPostingJSONObject(PostingInfo oPostingInfo) {
		JSONObject oPostingJSONobject = new JSONObject();
		try {
			oPostingJSONobject.put("stationCode", oPostingInfo.sStationCode);
			oPostingJSONobject.put("outletCode", oPostingInfo.sOutletCode);
			oPostingJSONobject.put("employeeNum", oPostingInfo.sEmployee);
			oPostingJSONobject.put("memberCardNumber", oPostingInfo.sCardNumber);
			oPostingJSONobject.put("inputMethod", oPostingInfo.sInputMethod);
			oPostingJSONobject.put("memberNumber", oPostingInfo.sMemberNumber);
			oPostingJSONobject.put("checkNumber", oPostingInfo.sCheckNumber);
			oPostingJSONobject.put("interfaceId", oPostingInfo.iInterfaceId);
			oPostingJSONobject.put("interfaceCode", oPostingInfo.sInterfaceCode);
		} catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		return oPostingJSONobject;
	}
	
	public boolean cardEnquiry(String sInterfaceId, String sEnquiryValue, boolean bIsSwipeCard) {
		//Request
		HashMap<String, String> oEnquiryInfo = new HashMap<>();
		oEnquiryInfo.put("interfaceId", sInterfaceId);
		oEnquiryInfo.put("inputCardId", sEnquiryValue);
		oEnquiryInfo.put("inputMethod", bIsSwipeCard? FuncGamingInterface.SWIPE_CARD : FuncGamingInterface.KEYIN);
		return doGamingInterfaceCardEnquiry(oEnquiryInfo);
	}
	
	public boolean compSlipEnquiry(String sEnquiryValue) {
		HashMap<String, String> oEnquiryInfo = new HashMap<>();
		oEnquiryInfo.put("compSlipNumber", sEnquiryValue);
		oEnquiryInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oEnquiryInfo.put("stationCode", AppGlobal.g_oFuncStation.get().getCode());
		oEnquiryInfo.put("timeZoneOffset", String.valueOf(AppGlobal.g_oFuncOutlet.get().getShop().getTimezone()));
		
		JSONObject oResultJSONObject = new JSONObject();
		if(m_oGamingInterface == null)
			m_oGamingInterface = new PosInterfaceConfig();
		oResultJSONObject = m_oGamingInterface.doGamingInterfaceCompSlipEnquiry(oEnquiryInfo);
		
		// fail to do card enquiry
		if (oResultJSONObject == null) {
			if(m_oGamingInterface.getLastErrorCode() != 0)
				m_sLastErrorMessage = getErrorMessage(m_oGamingInterface.getLastErrorCode());
			else
				m_sLastErrorMessage = m_oGamingInterface.getLastErrorMessage();
			return false;
		}
			
		m_oLastCardEnquiryResponseInfo = new GamingInterfaceCardEnquiryInfo();
		setCardEnquiryResponseInfo(oResultJSONObject);
		
		return true;
	}
	
	public boolean doGamingInterfaceCardEnquiry(HashMap<String, String> oEnquiryInfo) {
		oEnquiryInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oEnquiryInfo.put("stationCode", AppGlobal.g_oFuncStation.get().getCode());
		oEnquiryInfo.put("timeZoneOffset", String.valueOf(AppGlobal.g_oFuncOutlet.get().getShop().getTimezone()));
		
		JSONObject oResultJSONObject = new JSONObject();
		JSONObject oResultInfoJSONObject = null;
		oResultJSONObject = m_oGamingInterface.doGamingInterfaceCardEnquiry(oEnquiryInfo);
		
		if (oResultJSONObject != null) {
			if(m_oGamingInterface.getInterfaceVendorKey().equals(InfVendor.KEY_BALLY) || m_oGamingInterface.getInterfaceVendorKey().equals(InfVendor.KEY_SJM)) {
				oResultInfoJSONObject = oResultJSONObject.optJSONObject("cardInfo");
				if(m_oGamingInterface.getInterfaceVendorKey().equals(InfVendor.KEY_SJM)) {
					JSONArray oBucketListJSONArray = oResultJSONObject.optJSONArray("bucketList");
					if (oBucketListJSONArray != null && oResultInfoJSONObject != null) {
						try {
							oResultInfoJSONObject.put("bucketList", oBucketListJSONArray);
						}catch(JSONException e) {
							AppGlobal.stack2Log(e);
						}
					}
				}
			}
		}
		// fail to do card enquiry
		if (oResultInfoJSONObject == null) {
			if(m_oGamingInterface.getLastErrorCode() != 0)
				m_sLastErrorMessage = getErrorMessage(m_oGamingInterface.getLastErrorCode());
			else
				m_sLastErrorMessage = m_oGamingInterface.getLastErrorMessage();
			return false;
		}	
		m_oLastCardEnquiryResponseInfo = new GamingInterfaceCardEnquiryInfo();
		setCardEnquiryResponseInfo(oResultInfoJSONObject);
		
		return true;
	}
	
	public void setCardEnquiryResponseInfo(JSONObject oResultJSONObject) {
		if (oResultJSONObject.has("acctNo") && !oResultJSONObject.optString("acctNo").isEmpty())
			m_oLastCardEnquiryResponseInfo.sAccountNumber = oResultJSONObject.optString("acctNo");
		
		if (oResultJSONObject.has("cardType") && !oResultJSONObject.optString("cardType").isEmpty())
			m_oLastCardEnquiryResponseInfo.sCardType = oResultJSONObject.optString("cardType");
		
		if (oResultJSONObject.has("clubState") && !oResultJSONObject.optString("clubState").isEmpty())
			m_oLastCardEnquiryResponseInfo.sClubState = oResultJSONObject.optString("clubState");
		
		if (oResultJSONObject.has("title") && !oResultJSONObject.optString("title").isEmpty())
			m_oLastCardEnquiryResponseInfo.sTitle = oResultJSONObject.optString("title");
		
		if (oResultJSONObject.has("firstName") && !oResultJSONObject.optString("firstName").isEmpty())
			m_oLastCardEnquiryResponseInfo.sFirstName = oResultJSONObject.optString("firstName");
		
		if (oResultJSONObject.has("lastName") && !oResultJSONObject.optString("lastName").isEmpty())
			m_oLastCardEnquiryResponseInfo.sLastName = oResultJSONObject.optString("lastName");
		
		if (oResultJSONObject.has("chineseName") && !oResultJSONObject.optString("chineseName").isEmpty())
			m_oLastCardEnquiryResponseInfo.sChineseName = oResultJSONObject.optString("chineseName");
		
		if (oResultJSONObject.has("discountPercentage") && !oResultJSONObject.optString("discountPercentage").isEmpty())
			m_oLastCardEnquiryResponseInfo.sDiscountPercentage = oResultJSONObject.optString("discountPercentage");
		
		if (oResultJSONObject.has("bucketList") && oResultJSONObject.optJSONArray("bucketList") != null) {
			JSONArray  oBucketListJSONArray = oResultJSONObject.optJSONArray("bucketList");
			if (oBucketListJSONArray != null) {
				for(int i = 0; i < oBucketListJSONArray.length(); i++) {
					JSONObject oBucketListJSONObject = oBucketListJSONArray.optJSONObject(i);
					if (oBucketListJSONObject.has("typeName") && !oBucketListJSONObject.optString("typeName").isEmpty() && oBucketListJSONObject.has("balance"))
						m_oLastCardEnquiryResponseInfo.oInfoList.put(oBucketListJSONObject.optString("typeName"), String.valueOf(oBucketListJSONObject.optDouble("balance")));
				}
			}
		}
	}
	
	public boolean ballyPaymentPosting(PosPaymentMethod oPaymentMethod, HashMap<String, String> oPostingInfoMap, FuncCheck oFuncCheck, List<PosCheckPayment> oCheckPayments) {
		if(!m_oGamingInterface.getInterfaceVendorKey().equals(InfVendor.KEY_BALLY))
			return false;
		
		boolean bFailToPost = false;
		List<BallyPaymentPostingInfo> oBallyPostingInfoList = new ArrayList<BallyPaymentPostingInfo>();
		BallyPaymentPostingInfo oBallyPostingInfo = new BallyPaymentPostingInfo();
		
		if (oPostingInfoMap.containsKey("prizeCode"))
			oBallyPostingInfo.sPrizeCode = oPostingInfoMap.get("prizeCode");
		if (oPostingInfoMap.containsKey("presentSlip"))
			oBallyPostingInfo.sPresentSlip = oPostingInfoMap.get("presentSlip");
		if (oPostingInfoMap.containsKey("accountNumber"))
			oBallyPostingInfo.sAccountNumber = oPostingInfoMap.get("accountNumber");
		if (oPostingInfoMap.containsKey("interfaceId"))
			oBallyPostingInfo.sInterfaceId = oPostingInfoMap.get("interfaceId");
		if (oPostingInfoMap.containsKey("interfaceCode"))
			oBallyPostingInfo.sInterfaceCode = oPostingInfoMap.get("interfaceCode");
		if (oPostingInfoMap.containsKey("outletLangIndex"))
			oBallyPostingInfo.sOutletName = AppGlobal.g_oFuncOutlet.get().getOutletNameByIndex(Integer.valueOf(oPostingInfoMap.get("outletLangIndex")));
		if (oPostingInfoMap.containsKey("totalAmount"))
			oBallyPostingInfo.sTotalAmount = oPostingInfoMap.get("totalAmount");
		if (!AppGlobal.g_oFuncOutlet.get().getBusinessDay().isCheckNumGeneratedByOutlet() && !oFuncCheck.isOldCheck())
			oBallyPostingInfo.sCheckNumber = AppGlobal.g_oFuncStation.get().getNextCheckPrefixNumber(false);
		else
			oBallyPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();

		oBallyPostingInfo.sCurrency = AppGlobal.g_oFuncOutlet.get().getCurrencyCode();
		oBallyPostingInfo.sOutletId = String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oBallyPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oBallyPostingInfo.sPaymentCode = oPaymentMethod.getPaymentCode();
		oBallyPostingInfo.sStationCode = AppGlobal.g_oFuncStation.get().getCode();
		oBallyPostingInfo.sPostingTimeZone = String.valueOf(AppGlobal.g_oFuncOutlet.get().getShop().getTimezone());
		
		oBallyPostingInfoList.add(oBallyPostingInfo);
		
		// no bally posting is needed
		if(oBallyPostingInfoList.isEmpty())
			return true;
		
		// do posting
		for (int i = 0; i < oBallyPostingInfoList.size(); i++) {
			JSONObject oCheckInformationJSONObject = oFuncCheck.contrustBallyPostingCheckInformation(oCheckPayments);
			PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
			boolean bResult = false;
			if (oPostingInfoMap.containsKey("presentSlip"))
				bResult = oInterfaceConfig.gamingCompSlipPosting(oCheckInformationJSONObject, formBallyPostingJSONObject(oBallyPostingInfoList.get(i)));
			else
				bResult = oInterfaceConfig.gamingRedeemDollarPosting(oCheckInformationJSONObject, formBallyPostingJSONObject(oBallyPostingInfoList.get(i)));

			if (!bResult) {
				if(oInterfaceConfig.getLastErrorType() != null && oInterfaceConfig.getLastErrorType().equals(FuncGamingInterface.ERROR_TYPE_FAIL_RESPONSE))
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("bally_return_error") + System.lineSeparator() + "[" + oInterfaceConfig.getLastErrorMessage() + "]";
				else if(oInterfaceConfig.getLastErrorCode() != 0)
					m_sLastErrorMessage = getErrorMessage(oInterfaceConfig.getLastErrorCode());
				else
					m_sLastErrorMessage = oInterfaceConfig.getLastErrorMessage();
				
				bFailToPost = true;
			}else {
				JSONObject oResultJSON = oInterfaceConfig.getLastSuccessResult();
				
				m_oBallyPaymentResponseInfo = new BallyPaymentResponseInfo();
				m_oBallyPaymentResponseInfo.sGpcTranId = oResultJSON.optString("gpcTranId", "");
				m_oBallyPaymentResponseInfo.sCmpTranId = oResultJSON.optString("cmpTranId", "");
			}
			if(bFailToPost)
				break;
		}
		
		if(bFailToPost)
			return false;
		else
			return true;
		
	}
	
	private JSONObject formBallyPostingJSONObject(BallyPaymentPostingInfo oBallyPostingInfo) {
		JSONObject oPostingJSONObject = new JSONObject();
		
		try {
			if(this.m_oGamingInterface.getInterfaceVendorKey().equals(InfVendor.KEY_BALLY)){
				oPostingJSONObject.put("acctNo", oBallyPostingInfo.sAccountNumber);
				if (!oBallyPostingInfo.sPrizeCode.isEmpty())
					oPostingJSONObject.put("prizeCode", oBallyPostingInfo.sPrizeCode);
				if (!oBallyPostingInfo.sPresentSlip.isEmpty())
					oPostingJSONObject.put("presentSlip", oBallyPostingInfo.sPresentSlip);
				oPostingJSONObject.put("checkNumber", oBallyPostingInfo.sCheckNumber);
				oPostingJSONObject.put("currency", oBallyPostingInfo.sCurrency);
				oPostingJSONObject.put("interfaceId", oBallyPostingInfo.sInterfaceId);
				oPostingJSONObject.put("interfaceCode", oBallyPostingInfo.sInterfaceCode);
				oPostingJSONObject.put("outletName", oBallyPostingInfo.sOutletName);
				oPostingJSONObject.put("outletId", oBallyPostingInfo.sOutletId);
				oPostingJSONObject.put("outletCode", oBallyPostingInfo.sOutletCode);
				oPostingJSONObject.put("paymentId", oBallyPostingInfo.sPaymentCode);
				oPostingJSONObject.put("stationCode", oBallyPostingInfo.sStationCode);
				oPostingJSONObject.put("totalAmount", oBallyPostingInfo.sTotalAmount);
				oPostingJSONObject.put("timeZoneOffset", oBallyPostingInfo.sPostingTimeZone);
			}
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		
		return oPostingJSONObject;
	}

	private JSONObject formBallyVoidPostingJSONObject(BallyPaymentVoidPostingInfo oBallyPostingInfo) {
		JSONObject oPostingJSONObject = new JSONObject();
		
		try {
			if(this.m_oGamingInterface.getInterfaceVendorKey().equals(InfVendor.KEY_BALLY)){
				oPostingJSONObject.put("acctNo", oBallyPostingInfo.sAccountNumber);
				oPostingJSONObject.put("gpcTranId", oBallyPostingInfo.sGpcTranId);
				oPostingJSONObject.put("stationCode", oBallyPostingInfo.sStationCode);
				oPostingJSONObject.put("outletName", oBallyPostingInfo.sOutletName);
				oPostingJSONObject.put("outletCode", oBallyPostingInfo.sOutletCode);
				oPostingJSONObject.put("outletId", oBallyPostingInfo.sOutletId);
				oPostingJSONObject.put("interfaceCode", oBallyPostingInfo.sInterfaceCode);
				oPostingJSONObject.put("interfaceId", oBallyPostingInfo.sInterfaceId);
			}
			
		} catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		
		return oPostingJSONObject;
	}
	
	public void addBallyPostingResult(PosCheckPayment oPosCheckPayment) {
		// GpcTranId
		oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_POSTING_KEY, 0, m_oBallyPaymentResponseInfo.sGpcTranId);
		// CmpTranId
		oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_REFERENCE, 0, m_oBallyPaymentResponseInfo.sCmpTranId);
	}
	
	// gaming void posting
	public boolean voidGamingPosting(HashMap<String, String> oPostingInfoMap, List<PosCheckPayment> oCheckPayments, PosInterfaceConfig oPosInterfaceConfig) {
		boolean bFailToPost = false;
		
		List<BallyPaymentVoidPostingInfo> oPostingInfoList = new ArrayList<BallyPaymentVoidPostingInfo>();
		PosCheckExtraInfo oTempExtraInfo = null;
		String sAccountNumber = "", sGpcTranId = "";
		
		for(PosCheckPayment oCheckPayment: oCheckPayments) {
			if(m_oGamingInterface.getInterfaceVendorKey().equals(InfVendor.KEY_BALLY)) {
				oTempExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER, 0);
				if(oTempExtraInfo != null)
					sAccountNumber = oTempExtraInfo.getValue();
				
				oTempExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_POSTING_KEY, 0);
				if(oTempExtraInfo != null)
					sGpcTranId = oTempExtraInfo.getValue();
			}
		}
		
		BallyPaymentVoidPostingInfo oPostingInfo = new BallyPaymentVoidPostingInfo();
		if (oPostingInfoMap.containsKey("outletLangIndex"))
			oPostingInfo.sOutletName = AppGlobal.g_oFuncOutlet.get().getOutletNameByIndex(Integer.valueOf(oPostingInfoMap.get("outletLangIndex")));
		oPostingInfo.sAccountNumber = sAccountNumber;
		oPostingInfo.sGpcTranId = sGpcTranId;
		oPostingInfo.sStationCode = AppGlobal.g_oFuncStation.get().getCode();
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.sOutletId = String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oPostingInfo.sInterfaceCode = oPosInterfaceConfig.getInterfaceCode();
		oPostingInfo.sInterfaceId = String.valueOf(oPosInterfaceConfig.getInterfaceId());
		
		// add to list
		oPostingInfoList.add(oPostingInfo);
		
		// no bally gaming posting is needed
		if(oPostingInfoList.isEmpty())
			return true;
		
		// do voiding posting
		PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
		if(oInterfaceConfig.doVoidGamingPosting(formBallyVoidPostingJSONObject(oPostingInfo)) == false) {
			bFailToPost = true;

			if(oInterfaceConfig.getLastErrorCode() != 0)
				m_sLastErrorMessage = getErrorMessage(oInterfaceConfig.getLastErrorCode());
			else
				m_sLastErrorMessage = oInterfaceConfig.getLastErrorMessage();
		}
		
		if(bFailToPost)
			return false;
		else
			return true;
	}
	
	// check whether this payment allow to use with discount according to setup
	public boolean allowPaymentWithDiscount(JSONObject oPaymentInterfaceConfig, boolean bHasCheckDisc, boolean bHasItemDisc) {
		if(!oPaymentInterfaceConfig.has("payment_setup") || !oPaymentInterfaceConfig.optJSONObject("payment_setup").optJSONObject("params").has("discount_existence_control"))
			return true;
		
		String sDiscControl = oPaymentInterfaceConfig.optJSONObject("payment_setup").optJSONObject("params").optJSONObject("discount_existence_control").optString("value");
		if((sDiscControl.equals(FuncGamingInterface.PAYMENT_CONTROL_NOT_ALLOW_FOR_ITEM_DISC) && bHasItemDisc) ||
			(sDiscControl.equals(FuncGamingInterface.PAYMENT_CONTROL_NOT_ALLOW_FOR_CHECK_DISC) && bHasCheckDisc) ||
			(sDiscControl.equals(FuncGamingInterface.PAYMENT_CONTROL_NOT_ALLOW_FOR_ITEM_CHECK_DISC) && (bHasItemDisc || bHasCheckDisc)))
			return false;
		else
			return true;
	}
	
	public PosInterfaceConfig getInterfaceConfig() {
		return m_oGamingInterface;
	}
	
	public String getResponseMemberName() {
		return m_oLastGamingPostResponseInfo.sMemberName;
	}
	
	public String getResponseMemberNumber() {
		return m_oLastGamingPostResponseInfo.sMemberNumber;
	}
	
	public String getResponseCompNumber() {
		return m_oLastGamingPostResponseInfo.sCompNumber;
	}
	public String getResponseCompStatus() {
		return m_oLastGamingPostResponseInfo.sCompStatus;
	}
	
	public GamingInterfaceResponseInfo getResponseInfo() {
		return this.m_oLastGamingPostResponseInfo;
	}
	
	public String getLastErrorMessage() {
		return m_sLastErrorMessage;
	}
	
	public String getErrorMessage(int iErrorCode) {
		String sErrorMessage = "";

		switch (iErrorCode) {
		case 1:
			sErrorMessage = AppGlobal.g_oLang.get()._("no_such_interface");
			break;
		case 2:
			sErrorMessage = AppGlobal.g_oLang.get()._("missing_interface_setup");
			break;
		case 3:
			sErrorMessage = AppGlobal.g_oLang.get()._("missing_setup");
			break;
		case 4:
			sErrorMessage = AppGlobal.g_oLang.get()._("interface_shell_has_not_been_built");
			break;
		case 5:
			sErrorMessage = AppGlobal.g_oLang.get()._("shell_is_not_alive");
			break;
		case 6:
			sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_build_connection");
			break;
		case 7:
			sErrorMessage = AppGlobal.g_oLang.get()._("incorrect_posting_information");
			break;
		case 8:
			sErrorMessage = AppGlobal.g_oLang.get()._("no_response");
			break;
		case 9:
			sErrorMessage = AppGlobal.g_oLang.get()._("empty_response");
			break;
		case 10:
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_response");
			break;
		case 11:
			sErrorMessage = AppGlobal.g_oLang.get()._("missing_interface_module");
			break;
		case 12:
			sErrorMessage = AppGlobal.g_oLang.get()._("zero_itemizer_setup");
			break;
		case 13:
			sErrorMessage = AppGlobal.g_oLang.get()._("no_itemizer_configuration");
			break;
		case 14:
			sErrorMessage = AppGlobal.g_oLang.get()._("missing_itemizer_setup");
			break;
		case 15:
			sErrorMessage = AppGlobal.g_oLang.get()._("pms_port_is_locked_by_other_station");
			break;
		case 16:
			sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_lock_pms_port");
			break;
		case 17:
			sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_send_request_packet");
			break;
		case 18:
			sErrorMessage = AppGlobal.g_oLang.get()._("enquiry_is_rejected");
			break;
		case 19:
			sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_receive_response_packet");
			break;
		case 20:
			sErrorMessage = AppGlobal.g_oLang.get()._("incorrect_response_message_type");
			break;
		case 21:
			sErrorMessage = AppGlobal.g_oLang.get()._("posting_is_rejected");
			break;
		case 22:
			sErrorMessage = AppGlobal.g_oLang.get()._("incorrect_posting_packet");
			break;
		case 23:
			sErrorMessage = AppGlobal.g_oLang.get()._("incorrect_receiving_packet_structure");
			break;
		case 24:
			sErrorMessage = AppGlobal.g_oLang.get()._("timeout_to_wait_receiving_packet");
			break;
		default:
		case 0:
			sErrorMessage = null;
			break;
		}

		return sErrorMessage;
	}
}
