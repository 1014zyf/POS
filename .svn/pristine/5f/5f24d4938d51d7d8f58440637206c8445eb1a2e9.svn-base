package app.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import app.model.InfVendor;
import app.model.PosCheckExtraInfo;
import app.model.PosCheckPayment;
import app.model.PosInterfaceConfig;
import sun.misc.BASE64Encoder;

public class FuncMembershipInterface {
	class LpsSvcPostingInfo {
		int iInterfaceId;
		String sRequestCode;
		int iOutletId;
		String sHotelCode;
		int iTraceId;
		String sStationId;
		String sSVAN;
		String sCheckNumber;
		BigDecimal dLocalBalance;
		BigDecimal dAccountBalance;
		String sPassword;
		BigDecimal dPointsBalance;
		int iCover;
		HashMap<String, BigDecimal> oDiscountList;
		HashMap<String, String> oCouponList;
		String sResponseCode;
		BigDecimal dPaymentAmount;
		int iPaymentId;
		String sPaymentName;
		String sEmployeeNumber;
		BigDecimal dTips;
		String sDate;
		String sTime;
		BigDecimal dPreviousPaymentTotal;
		int iPointPosting;
	}
	
	class LpsSvcResponseInfo {
		String sHotelCode = "";
		String sTraceId = "";
		String sTerminalId = "";
		String sAmount = "";
		String sTipAmount = "";
		String sSVAN = "";
		String sAccountName = "";
		String sPointsBalance = "";
		String sPoints = "";
		String sLocalBalance = "";
		String sAccountBalance = "";
		String sResponseCode = "";
		String[] sPrintLines;
		String sEnglishName = "";
		String sCardTypeName = "";
		String sCardLevelName = "";
		String sCardSN = "";
		String sExpiryDate = "";
		String sDisplayMessage = "";
		String sBirthday = "";
		String sFoodDisc = "";
		String sBevDisc = "";
		String sMiscDisc = "";
		ArrayList<String> sCouponList;
	}
	
	static public String PAY_CHECK_SUCCESS = "s";
	static public String PAY_CHECK_FAIL = "f";
	
	private PosInterfaceConfig m_oMembershipInterface;
	private String m_sLastErrorMessage;
	public LpsSvcResponseInfo m_oLastLpsSvcResponseInfo;
	
	public FuncMembershipInterface(PosInterfaceConfig oMembershipInterface) {
		m_oMembershipInterface = oMembershipInterface;
		m_sLastErrorMessage = "";
	}
	
	// LPS SVC Enquiry
	public boolean svcEnquiry(int iInterfaceId, String sEnqValue, String sPassword, int iCheckId) {
		PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
		String sEncryptedPwd = this.getMd5String("UTF-8", sPassword);
		JSONObject oResultJSONObject = oInterfaceConfig.svcEnquiry(iInterfaceId, sEnqValue, sEncryptedPwd, iCheckId, AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getOutletCode(), AppGlobal.g_oFuncStation.get().getStation().getCode(), AppGlobal.g_oFuncUser.get().getUserNumber());
		m_sLastErrorMessage = oInterfaceConfig.getLastErrorMessage();
		if(oResultJSONObject == null) {
			m_sLastErrorMessage = oInterfaceConfig.getLastErrorMessage();
			
			return false;
		}
		
		if(!oResultJSONObject.has("enquiryResult")) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("result_error");
			return false;
		}
		
		if(oResultJSONObject.optBoolean("enquiryResult", false) == false) {
			m_sLastErrorMessage = getErrorMessage(oResultJSONObject.optInt("errorCode", 0));
			return false;
		}
		
		oResultJSONObject = oResultJSONObject.optJSONObject("memberDetails");
		if(oResultJSONObject == null) {
			m_oLastLpsSvcResponseInfo = new LpsSvcResponseInfo();
			m_oLastLpsSvcResponseInfo.sPrintLines = new String[2];
			m_oLastLpsSvcResponseInfo.sCouponList = new ArrayList<String>();
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_such_member");
			return true;
		}
		
		if(!oResultJSONObject.optString("responseCode", "").equals("A")) {
			if(!oResultJSONObject.optString("displayMessage", "").equals(""))
				m_sLastErrorMessage = oResultJSONObject.optString("displayMessage");
			else
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("membership_system_error");
			return false;
		}
		
		m_oLastLpsSvcResponseInfo = new LpsSvcResponseInfo();
		m_oLastLpsSvcResponseInfo.sPrintLines = new String[2];
		m_oLastLpsSvcResponseInfo.sCouponList = new ArrayList<String>();
		
		m_oLastLpsSvcResponseInfo.sSVAN = oResultJSONObject.optString("svan", "");
		m_oLastLpsSvcResponseInfo.sAccountName = oResultJSONObject.optString("accountName", "");
		m_oLastLpsSvcResponseInfo.sCardTypeName = oResultJSONObject.optString("cardType", "");
		m_oLastLpsSvcResponseInfo.sLocalBalance = oResultJSONObject.optString("localBalance", "");
		m_oLastLpsSvcResponseInfo.sAccountBalance = oResultJSONObject.optString("accountBalance", "");
		m_oLastLpsSvcResponseInfo.sBirthday = oResultJSONObject.optString("birthday", "");
		m_oLastLpsSvcResponseInfo.sPointsBalance = oResultJSONObject.optString("pointsBalance", "");
		if(oResultJSONObject.has("discount")) {
			m_oLastLpsSvcResponseInfo.sFoodDisc = oResultJSONObject.optJSONObject("discount").optString("foodDiscount", "");
			m_oLastLpsSvcResponseInfo.sBevDisc = oResultJSONObject.optJSONObject("discount").optString("beveDiscount", "");
			m_oLastLpsSvcResponseInfo.sMiscDisc = oResultJSONObject.optJSONObject("discount").optString("miscDiscount", "");
		}
		m_oLastLpsSvcResponseInfo.sExpiryDate = oResultJSONObject.optString("expiryDate", "");
		m_oLastLpsSvcResponseInfo.sCardLevelName = oResultJSONObject.optString("cardLevelName", "");
		m_oLastLpsSvcResponseInfo.sCardSN = oResultJSONObject.optString("cardSN", "");
		
		//coupon list
		if(oResultJSONObject.has("coupons")) {
			for(int i=0; i<oResultJSONObject.optJSONArray("coupons").length(); i++) {
				JSONObject oCouponJSON = oResultJSONObject.optJSONArray("coupons").optJSONObject(i);
				m_oLastLpsSvcResponseInfo.sCouponList.add(oCouponJSON.optString("name")+"("+oCouponJSON.optString("type")+") X "+oCouponJSON.optString("numberOfCoupon"));				
			}
		}
		
		return true;
	}
	
	// LPS SVC posting
	public boolean svcPosting(String sEnqValue, String sPassword, FuncCheck oFuncCheck, List<PosCheckPayment> oCheckPayments, boolean bPointPosting) {
		if(!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_LPS_SVC))
			return false;
		
		int i=0, iCurrentPostingIndex = 0;
		boolean bFailToPost = false;
		List<LpsSvcPostingInfo> oPostingInfoList = new ArrayList<LpsSvcPostingInfo>();
		DateTime today = new DateTime();
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HHmmss");
		String sCurrentTime = timeFmt.print(today);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyyMMdd");
		String sCurrentDate = dateFmt.print(today);
		
		LpsSvcPostingInfo oPostingInfo = new LpsSvcPostingInfo();
		oPostingInfo.iInterfaceId = m_oMembershipInterface.getInterfaceId();
		oPostingInfo.iOutletId = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oPostingInfo.sSVAN = sEnqValue;
		oPostingInfo.sPassword = this.getMd5String("UTF-8", sPassword);;
		oPostingInfo.sStationId = AppGlobal.g_oFuncStation.get().getStation().getCode();
		oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
		oPostingInfo.iCover = oFuncCheck.getCover();
		//oPostingInfo.dPaymentAmount = oCheckPayment.getPayTotal();
		//oPostingInfo.iPaymentId = oCheckPayment.getPaymentMethodId();
		//oPostingInfo.sPaymentName = oCheckPayment.getName(1);
		oPostingInfo.sEmployeeNumber = AppGlobal.g_oFuncUser.get().getUserNumber();
		//oPostingInfo.dTips = oCheckPayment.getPayTips();
		oPostingInfo.sDate = sCurrentDate;
		oPostingInfo.sTime = sCurrentTime;
		if(bPointPosting)
			oPostingInfo.iPointPosting = 1;
		else
			oPostingInfo.iPointPosting = 0;
		
		oPostingInfoList.add(oPostingInfo);
		
		// no svc posting is needed
		if(oPostingInfoList.size() == 0)
			return true;

		// do posting
		for(i=0; i<oPostingInfoList.size(); i++) {
			//JSONObject oCheckInformationJSONObject = oFuncCheck.contrustPmsPostingCheckInformation(oCheckPayment);
			JSONObject oCheckInformationJSONObject = oFuncCheck.contrustSvcPostingCheckInformation(oCheckPayments);
			PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
			boolean bResult = oInterfaceConfig.doSvcPosting(oCheckInformationJSONObject, fromSvcPostingJSONObject(oPostingInfoList.get(i)));
			
			if(bResult == false) {
				if(oInterfaceConfig.getLastErrorType() != null && oInterfaceConfig.getLastErrorType().equals(FuncPMS.ERROR_TYPE_PMS))
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("svc_return_error") + ":[" + oInterfaceConfig.getLastErrorMessage() + "]";
				else if(oInterfaceConfig.getLastErrorCode() != 0)
					m_sLastErrorMessage = getErrorMessage(oInterfaceConfig.getLastErrorCode());
				else
					m_sLastErrorMessage = oInterfaceConfig.getLastErrorMessage();
				bFailToPost = true;
				iCurrentPostingIndex = i;
			}else {
				JSONObject oResultJSON = oInterfaceConfig.getLastSuccessReulst();	
				
				if(!oResultJSON.optString("responseCode", "").equals("A")) {
					if(!oResultJSON.optString("displayMessage", "").equals(""))
						m_sLastErrorMessage = oResultJSON.optString("displayMessage");
					else
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("membership_system_error");
					return false;
				}
				
				m_oLastLpsSvcResponseInfo = new LpsSvcResponseInfo();
				m_oLastLpsSvcResponseInfo.sPrintLines = new String[2];
				m_oLastLpsSvcResponseInfo.sPrintLines[0] = "";
				m_oLastLpsSvcResponseInfo.sPrintLines[1] = "";
				
				m_oLastLpsSvcResponseInfo.sHotelCode = oResultJSON.optString("hotelCode", "");
				m_oLastLpsSvcResponseInfo.sTraceId = oResultJSON.optString("traceID", "");
				m_oLastLpsSvcResponseInfo.sTerminalId = oResultJSON.optString("terminalID", "");
				m_oLastLpsSvcResponseInfo.sAmount = oResultJSON.optString("amount", "");
				m_oLastLpsSvcResponseInfo.sTipAmount = oResultJSON.optString("tipAmount", "");
				m_oLastLpsSvcResponseInfo.sSVAN = oResultJSON.optString("svan", "");
				m_oLastLpsSvcResponseInfo.sAccountName = oResultJSON.optString("accountName", "");
				m_oLastLpsSvcResponseInfo.sPointsBalance = oResultJSON.optString("pointsBalance", "");
				m_oLastLpsSvcResponseInfo.sPoints = oResultJSON.optString("points", "");
				m_oLastLpsSvcResponseInfo.sLocalBalance = oResultJSON.optString("localBalance", "");
				m_oLastLpsSvcResponseInfo.sAccountBalance = oResultJSON.optString("accountBalance", "");
				m_oLastLpsSvcResponseInfo.sResponseCode = oResultJSON.optString("responseCode", "");
				if(oResultJSON.optJSONArray("printLines") != null && oResultJSON.optJSONArray("printLines").length() > 0) {
					for(int j=0; j<oResultJSON.optJSONArray("printLines").length() && j<2; j++)
						m_oLastLpsSvcResponseInfo.sPrintLines[j] = oResultJSON.optJSONArray("printLines").optJSONObject(j).optString("printLine");
				}
				m_oLastLpsSvcResponseInfo.sEnglishName = oResultJSON.optString("englishName", "");
				m_oLastLpsSvcResponseInfo.sCardTypeName = oResultJSON.optString("cardTypeName", "");
				m_oLastLpsSvcResponseInfo.sCardLevelName = oResultJSON.optString("cardLevelName", "");
				m_oLastLpsSvcResponseInfo.sCardSN = oResultJSON.optString("cardSN", "");
				m_oLastLpsSvcResponseInfo.sExpiryDate = oResultJSON.optString("expiryDate", "");
				m_oLastLpsSvcResponseInfo.sDisplayMessage = oResultJSON.optString("displayMessage", "");
			}
			
			if(bFailToPost)
				break;
		}
		
		if(bFailToPost)
			return false;
		else
			return true;
	}
	
	private JSONObject fromSvcPostingJSONObject(LpsSvcPostingInfo oPostingInfo) {
		JSONObject oPostingJSONObject = new JSONObject();
		
		try {
			oPostingJSONObject.put("interfaceId", oPostingInfo.iInterfaceId);
			oPostingJSONObject.put("requestCode", oPostingInfo.sRequestCode);
			oPostingJSONObject.put("hotelCode", oPostingInfo.sHotelCode);
			oPostingJSONObject.put("outletId", oPostingInfo.iOutletId);
			oPostingJSONObject.put("svan", oPostingInfo.sSVAN);
			oPostingJSONObject.put("password", oPostingInfo.sPassword);
			oPostingJSONObject.put("checkNumber", oPostingInfo.sCheckNumber);
			oPostingJSONObject.put("cover", oPostingInfo.iCover);
			//oPostingJSONObject.put("paymentAmount", oPostingInfo.dPaymentAmount.toPlainString());
			//oPostingJSONObject.put("paymentId", oPostingInfo.iPaymentId);
			//oPostingJSONObject.put("paymentName", oPostingInfo.sPaymentName);
			oPostingJSONObject.put("employeeNumber", oPostingInfo.sEmployeeNumber);
			//oPostingJSONObject.put("tips", oPostingInfo.dTips.toPlainString());
			oPostingJSONObject.put("traceId", oPostingInfo.iTraceId);
			oPostingJSONObject.put("terminalId", oPostingInfo.sStationId);
			oPostingJSONObject.put("localBalance", oPostingInfo.dLocalBalance);
			oPostingJSONObject.put("accountBalance", oPostingInfo.dAccountBalance);
			oPostingJSONObject.put("pointBalance", oPostingInfo.dPointsBalance);
			oPostingJSONObject.put("date", oPostingInfo.sDate);
			oPostingJSONObject.put("time", oPostingInfo.sTime);
			if(oPostingInfo.dPreviousPaymentTotal != null)
				oPostingJSONObject.put("previousAmount", oPostingInfo.dPreviousPaymentTotal.toPlainString());
			oPostingJSONObject.put("pointPosting", oPostingInfo.iPointPosting);

		}catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		
		return oPostingJSONObject;
	}
	
	public void addSvcPostingResult(PosCheckPayment oPosCheckPayment) {
		PosCheckExtraInfo oPosCheckExtraInfo;
		
		//trace id
		oPosCheckExtraInfo = new PosCheckExtraInfo();
		oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
		oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE);
		oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_TRACE_ID);
		oPosCheckExtraInfo.setValue(m_oLastLpsSvcResponseInfo.sTraceId);
		oPosCheckPayment.addExtraInfoToList(oPosCheckExtraInfo);
		
		//local balance
		oPosCheckExtraInfo = new PosCheckExtraInfo();
		oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
		oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE);
		oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_LOCAL_BALANCE);
		oPosCheckExtraInfo.setValue(m_oLastLpsSvcResponseInfo.sLocalBalance);
		oPosCheckPayment.addExtraInfoToList(oPosCheckExtraInfo);
		
		//account name
		oPosCheckExtraInfo = new PosCheckExtraInfo();
		oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
		oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE);
		oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_ACCOUNT_NAME);
		oPosCheckExtraInfo.setValue(m_oLastLpsSvcResponseInfo.sAccountName);
		oPosCheckPayment.addExtraInfoToList(oPosCheckExtraInfo);
		
		//english name
		oPosCheckExtraInfo = new PosCheckExtraInfo();
		oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
		oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE);
		oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_ENGLISH_NAME);
		oPosCheckExtraInfo.setValue(m_oLastLpsSvcResponseInfo.sEnglishName);
		oPosCheckPayment.addExtraInfoToList(oPosCheckExtraInfo);
		
		//card type name
		oPosCheckExtraInfo = new PosCheckExtraInfo();
		oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
		oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE);
		oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_CARD_TYPE_NAME);
		oPosCheckExtraInfo.setValue(m_oLastLpsSvcResponseInfo.sCardTypeName);
		oPosCheckPayment.addExtraInfoToList(oPosCheckExtraInfo);
		
		//print line 1-2
		for(int i=1; i<=2; i++) {
			oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_PRINT_LINE);
			oPosCheckExtraInfo.setIndex(i);
			oPosCheckExtraInfo.setValue(m_oLastLpsSvcResponseInfo.sPrintLines[(i-1)]);
			oPosCheckPayment.addExtraInfoToList(oPosCheckExtraInfo);
		}
		
		//points balance
		oPosCheckExtraInfo = new PosCheckExtraInfo();
		oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
		oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE);
		oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_POINTS_BALANCE);
		oPosCheckExtraInfo.setValue(m_oLastLpsSvcResponseInfo.sPointsBalance);
		oPosCheckPayment.addExtraInfoToList(oPosCheckExtraInfo);
		
		//points earn
		oPosCheckExtraInfo = new PosCheckExtraInfo();
		oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
		oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE);
		oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_POINTS_EARN);
		oPosCheckExtraInfo.setValue(m_oLastLpsSvcResponseInfo.sPoints);
		oPosCheckPayment.addExtraInfoToList(oPosCheckExtraInfo);
		
		//card sn
		oPosCheckExtraInfo = new PosCheckExtraInfo();
		oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
		oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE);
		oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_CARD_SN);
		oPosCheckExtraInfo.setValue(m_oLastLpsSvcResponseInfo.sCardSN);
		oPosCheckPayment.addExtraInfoToList(oPosCheckExtraInfo);
		
		//expiry date
		oPosCheckExtraInfo = new PosCheckExtraInfo();
		oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
		oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE);
		oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_EXPIRY_DATE);
		oPosCheckExtraInfo.setValue(m_oLastLpsSvcResponseInfo.sExpiryDate);
		oPosCheckPayment.addExtraInfoToList(oPosCheckExtraInfo);
		
		//card level name
		oPosCheckExtraInfo = new PosCheckExtraInfo();
		oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
		oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE);
		oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_CARD_LEVEL_NAME);
		oPosCheckExtraInfo.setValue(m_oLastLpsSvcResponseInfo.sCardLevelName);
		oPosCheckPayment.addExtraInfoToList(oPosCheckExtraInfo);
	}
	
	public void updateSvcPostingResult(PosCheckPayment oPosCheckPayment) {
		if(oPosCheckPayment == null)
			return;
		
		for(PosCheckExtraInfo oExtraInfo: oPosCheckPayment.getCheckExtraInfoArrayList()) {
			if(oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE) && oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_POINTS_BALANCE))
				oExtraInfo.setValue(m_oLastLpsSvcResponseInfo.sPointsBalance);
			if(oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE) && oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_POINTS_EARN))
				oExtraInfo.setValue(m_oLastLpsSvcResponseInfo.sPoints);
		}
		return;
	}
	
    // svc void posting
    public boolean svcVoidPosting(FuncCheck oFuncCheck, List<PosCheckPayment> oCheckPayments, BigDecimal dPreviousPaymentTotal, boolean bPointPosting) {
           boolean bFailToPost = false;

           List<LpsSvcPostingInfo> oPostingInfoList = new ArrayList<LpsSvcPostingInfo>();
           DateTime today = new DateTime();
           DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HHmmss");
           String sCurrentTime = timeFmt.print(today);
           DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyyMMdd");
           String sCurrentDate = dateFmt.print(today);
           String sEnqValue = "", sPassword = "";
           
           for(PosCheckPayment oCheckPayment: oCheckPayments) {
	           List<PosCheckExtraInfo> oCheckExtraInfos = oCheckPayment.getCheckExtraInfoArrayList();
	           for(PosCheckExtraInfo oPosCheckExtraInfo : oCheckExtraInfos) {
	        	   if(oPosCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE) &&
	        			   oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER))
	        		   sEnqValue = oPosCheckExtraInfo.getValue();
	        	   if(oPosCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE) &&
	        			   oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_PASSWORD))
	        		   sPassword = oPosCheckExtraInfo.getValue();
	           }
           }
           
           LpsSvcPostingInfo oPostingInfo = new LpsSvcPostingInfo();
           oPostingInfo.iInterfaceId = m_oMembershipInterface.getInterfaceId();
           oPostingInfo.iOutletId = AppGlobal.g_oFuncOutlet.get().getOutletId();
           oPostingInfo.sSVAN = sEnqValue;
           oPostingInfo.sPassword = this.getMd5String("UTF-8", sPassword);;
           oPostingInfo.sStationId = AppGlobal.g_oFuncStation.get().getStation().getCode();
           oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
           oPostingInfo.iCover = oFuncCheck.getCover();
           //oPostingInfo.dPaymentAmount = oCheckPayment.getPayTotal();
           //oPostingInfo.iPaymentId = oCheckPayment.getPaymentMethodId();
          // oPostingInfo.sPaymentName = oCheckPayment.getName(1);
           oPostingInfo.sEmployeeNumber = AppGlobal.g_oFuncUser.get().getUserNumber();
           //oPostingInfo.dTips = oCheckPayment.getPayTips();
           oPostingInfo.sDate = sCurrentDate;
           oPostingInfo.sTime = sCurrentTime;
           oPostingInfo.dPreviousPaymentTotal = dPreviousPaymentTotal;
           if(bPointPosting)
        	   oPostingInfo.iPointPosting = 1;
           else
        	   oPostingInfo.iPointPosting = 0;
           
           oPostingInfoList.add(oPostingInfo);
           
           // no svc posting is needed
           if(oPostingInfoList.size() == 0)
                  return true;

           // do voiding posting
           JSONObject oCheckInformationJSONObject = oFuncCheck.contrustSvcPostingCheckInformation(oCheckPayments);
           PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
           if(oInterfaceConfig.doSvcVoidPosting(oCheckInformationJSONObject, fromSvcPostingJSONObject(oPostingInfo)) == false) {
                  bFailToPost = true;
           }
           
           if(bFailToPost)
                  return false;
           else
                  return true;
    }
	
	public String getMd5String(String sCharSet, String sStr) {
		MessageDigest m;
		String sEncryptedStr = "";
		try {
			m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(sStr.getBytes(sCharSet));
			byte[] digest = m.digest();
			
			BASE64Encoder encoder = new BASE64Encoder();
			sEncryptedStr = encoder.encode(digest);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sEncryptedStr;
	}
	
	public String getLastErrorMessage() {
		return this.m_sLastErrorMessage;
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
