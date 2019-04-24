package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import om.InfInterface;
import om.InfVendor;
import om.PosCheckExtraInfo;
import om.PosCheckPayment;
import om.PosInterfaceConfig;
import om.PosPaymentMethod;

public class FuncPMS {
	class EnquiryResponse {
		String sLookupType;
		String sLookupIdContext;
		String sLookupId;
		List<HashMap<String, String>> oGuestList;
		HashMap<Integer, List<HashMap<String, String>>> oSubGuestList;
	}
	
	class PostingInfo {
		int iInterfaceId;
		String sInterfaceVendorKey;
		int iCheckPaymnetIndex;
		String sOutletCode;
		int iOutletId;
		String sWorkStation;
		int iStationID;
		String sEmployee;
		String sRoomNumber;
		String sGuestNumber;
		String sGuestName;
		String sExpiryDate;
		String sFieldNumber;
		String sRegisterNumber;
		String sCheckNumber;
		String sCheckGroupNumber;
		String sTable;
		int iCover;
		String sServingPeriod;
		//BigDecimal dServiceCharge;
		//BigDecimal[] dTaxes;
		int iPaymentSeq;
		String sPaymentType;
		String sPostingType;
		BigDecimal dSurcharge;
		BigDecimal dPaymentAmount;
		BigDecimal dTips;
		BigDecimal dPostAmount;
		BigDecimal dPreviousPaymentAmount;
		String sCardNumber;
		String sClearText;
		String sGuestFileNumber;
		String sDate;
		String sTime;
		String sBusinessDate;
		String sTraceId;
		String sPostingKey;
		String sCurrencyCode;
		boolean bAllowPostAfterPayment;
		boolean bSurchargeAddOnServiceChargeAmount;
		ArrayList<HashMap<String, String>> sOtherPmsPayments;
		ArrayList<HashMap<String, String>> sPreviousPmsPayments;
		String sLookupId;
		String sLookupIdContext;
		String sLookupType;
		String sBdateId;
		String sPostingString;
		boolean bCloseCheck;
	}
	
	public HashMap<String, String> m_oEnquiryInfo;
	public boolean m_bEnquiryResult;
	public String m_sLastErrorMessage;
	public boolean m_bForSinglePaymentPosting;
	
	static private int POSTING_TAX_COUNT = 16;
	
	public static String ERROR_TYPE_CONNECTION = "i";
	public static String ERROR_TYPE_PMS = "r";
	
	static public String ENQUIRY_TYPE_ROOM = "F";
	static public String ENQUIRY_TYPE_GROUP = "G";
	static public String ENQUIRY_TYPE_AR = "A";
	static public String ENQUIRY_TYPE_SUB_AR = "S";
	static public String ENQUIRY_TYPE_REGISTER = "";
	static public String ENQUIRY_TYPE_PACKAGE = "P";
	
	static public String R8_PAYMENT_TYPE_CASH = "cash_settlement";
	static public String R8_PAYMENT_TYPE_ROOM = "room_settlement";
	static public String R8_PAYMENT_TYPE_TARGET_PAYMENT = "taget_payment_settlement";
	
	static public String TYPE_PAY_PMS = "pay_pms";
	static public String TYPE_VOID_PMS = "void_pms";
	
	static public String PMS_TYPE_STANDARD = "standard";
	static public String PMS_TYPE_IMAGINE = "imagine";
	static public String PMS_TYPE_SHIJI = "shiji";
	
	static public String POSTING_TYPE_DEFAULT = "default";
	static public String POSTING_TYPE_ROOM = "room";
	static public String POSTING_TYPE_AR = "ar_posting";
	static public String POSTING_TYPE_GROUP = "group";
	
	static public String POSTING_MODE_PERIODIC = "periodic";
	
	static public String SHELL_OPERATION_RESTART = "restart";
	static public String SHELL_OPERATION_STOP = "stop";
	
	public FuncPMS() {
		m_oEnquiryInfo = new HashMap<String, String>();
		m_bEnquiryResult = false;
		m_sLastErrorMessage = "";
		m_bForSinglePaymentPosting = false;
	}
	
	public FuncPMS(boolean bForSinglePaymentPosting) {
		m_oEnquiryInfo = new HashMap<String, String>();
		m_bEnquiryResult = false;
		m_sLastErrorMessage = "";
		m_bForSinglePaymentPosting = bForSinglePaymentPosting;
	}
	
	public void setForSinglePaymentPosting(boolean bForSinglePaymentPosting) {
		m_bForSinglePaymentPosting = bForSinglePaymentPosting;
	}
	
	private void setEnquiryInfo(int iInterfaceId, HashMap<String, String> oEnquiryInformation, String sCheckNumber, String sPaymentCode) {
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm:ss");
		String sCurrentTime = timeFmt.print(today);
		
		m_oEnquiryInfo.put("interfaceId", String.valueOf(iInterfaceId));
		m_oEnquiryInfo.put("outlet", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		m_oEnquiryInfo.put("workStation", AppGlobal.g_oFuncStation.get().getCode());
		m_oEnquiryInfo.put("employee", AppGlobal.g_oFuncUser.get().getUserNumber());
		if(oEnquiryInformation != null && oEnquiryInformation.containsKey("enquiryNumber"))
			m_oEnquiryInfo.put("enquiryNumber", oEnquiryInformation.get("enquiryNumber"));
		else
			m_oEnquiryInfo.put("enquiryNumber", "");
		if(oEnquiryInformation != null && oEnquiryInformation.containsKey("enquiryAccountNumber"))
			m_oEnquiryInfo.put("enquiryAccountNumber", oEnquiryInformation.get("enquiryAccountNumber"));
		else
			m_oEnquiryInfo.put("enquiryAccountNumber", "");
		if(oEnquiryInformation != null && oEnquiryInformation.containsKey("registerNumber"))
			m_oEnquiryInfo.put("register", oEnquiryInformation.get("registerNumber"));
		else
			m_oEnquiryInfo.put("register", "");
		if(oEnquiryInformation != null && oEnquiryInformation.containsKey("enquiryType"))
			m_oEnquiryInfo.put("enquiryType", oEnquiryInformation.get("enquiryType"));
		else
			m_oEnquiryInfo.put("enquiryType", "");
		if(oEnquiryInformation != null && oEnquiryInformation.containsKey("givenName"))
			m_oEnquiryInfo.put("givenName", oEnquiryInformation.get("givenName"));
		else
			m_oEnquiryInfo.put("givenName", "");
		if(oEnquiryInformation != null && oEnquiryInformation.containsKey("surname"))
			m_oEnquiryInfo.put("surname", oEnquiryInformation.get("surname"));
		else
			m_oEnquiryInfo.put("surname", "");
		if(oEnquiryInformation != null && oEnquiryInformation.containsKey("arrivalDate"))
			m_oEnquiryInfo.put("arrivalDate", oEnquiryInformation.get("arrivalDate"));
		else
			m_oEnquiryInfo.put("arrivalDate", "");
		if(oEnquiryInformation != null && oEnquiryInformation.containsKey("departureDate"))
			m_oEnquiryInfo.put("departureDate", oEnquiryInformation.get("departureDate"));
		else
			m_oEnquiryInfo.put("departureDate", "");
		if(sPaymentCode != null)
			m_oEnquiryInfo.put("paymentType", sPaymentCode);
		m_oEnquiryInfo.put("checkNumber", sCheckNumber);
		if(oEnquiryInformation.containsKey("postingAskInfo"))
			m_oEnquiryInfo.put("postingAskInfo", "1");
		m_oEnquiryInfo.put("time", sCurrentTime);
	}
	
	// pms enquiry
	public EnquiryResponse pmsEnquiry(int iInterfaceId, HashMap<String, String> oEnquiryInformation, String sCheckNumber, String sPaymentCode) {
		InfInterface oPMSInterface = new InfInterface();
		PosInterfaceConfig oPMSInterfaceConfig = null;
		String sInterfaceVendor = "";
		JSONObject enquiryResultJSONObject = null;
		EnquiryResponse oEnquiryResponse = new EnquiryResponse();
		oEnquiryResponse.oGuestList = new ArrayList<HashMap<String, String>>();
		oEnquiryResponse.oSubGuestList = new HashMap<Integer, List<HashMap<String, String>>>();
		
		m_bEnquiryResult = false;
		m_sLastErrorMessage = "";
		setEnquiryInfo(iInterfaceId, oEnquiryInformation, sCheckNumber, sPaymentCode);
		
		//get interface setup
		for(PosInterfaceConfig oPosInterfaceConfig:AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PMS)){
			if(oPosInterfaceConfig.getInterfaceId() == iInterfaceId){
				sInterfaceVendor = oPosInterfaceConfig.getInterfaceVendorKey();
				oPMSInterfaceConfig = oPosInterfaceConfig;
				break;
			}
		}
		
		//really do pms enquiry
		if(sInterfaceVendor.equals(InfVendor.KEY_4700_SERIAL_PORT)) {
			FuncPMS4700SerialPort oFuncPMS4700SerialPort = new FuncPMS4700SerialPort();
			oFuncPMS4700SerialPort.init(oPMSInterfaceConfig);
			enquiryResultJSONObject = oFuncPMS4700SerialPort.doEnquiry(m_oEnquiryInfo);
		}else
			enquiryResultJSONObject = oPMSInterface.pmsEnquiry(m_oEnquiryInfo);
		
		if (enquiryResultJSONObject == null || !enquiryResultJSONObject.has("enquiryResult"))
			return null;
		
		if (!enquiryResultJSONObject.optBoolean("enquiryResult")) {
			if (sInterfaceVendor.equals(InfVendor.KEY_4700_SERIAL_PORT) || sInterfaceVendor.equals(InfVendor.KEY_HTNG)
					|| sInterfaceVendor.equals(InfVendor.KEY_4700_TCPIP)
					|| sInterfaceVendor.equals(InfVendor.KEY_STANDARD_TCPIP)) {
				int iErrorCode = enquiryResultJSONObject.optInt("errorCode");
				switch(iErrorCode){
					case 0:
						m_sLastErrorMessage = enquiryResultJSONObject.optString("errorMessage");
						break;
					case 8:
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("query_timeout");
						break;
					case 4:
					case 6:
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("pms_interface_is_offline_now") + System.lineSeparator();
					default:
						m_sLastErrorMessage += getErrorMessage(iErrorCode);
						break;
				}
			}
			return null;
		} else {
			if (enquiryResultJSONObject.isNull("guests"))
				return oEnquiryResponse;
			
			JSONArray oGuestList = enquiryResultJSONObject.optJSONArray("guests");
			for (int i = 0; i < oGuestList.length(); i++) {
				JSONObject oGuestJSONObject = oGuestList.optJSONObject(i);
				if (oGuestJSONObject == null)
					continue;
				
				HashMap<String, String> oGuest = new HashMap<String, String>();
				
				oGuest.put("guestIndex", String.valueOf(i+1));
				oGuest.put("guestNumber", oGuestJSONObject.optString("guestNumber"));
				oGuest.put("guestName", oGuestJSONObject.optString("guestName"));
				oGuest.put("roomNumber", oGuestJSONObject.optString("roomNumber"));
				if (oGuestJSONObject.has("remark"))
					oGuest.put("remark", oGuestJSONObject.optString("remark"));
				if (oGuestJSONObject.has("arrivalDate"))
					oGuest.put("arrivalDate", oGuestJSONObject.optString("arrivalDate"));
				if (oGuestJSONObject.has("departureDate"))
					oGuest.put("departureDate", oGuestJSONObject.optString("departureDate"));
				oGuest.put("guestFirstName", oGuestJSONObject.optString("guestFirstName"));
				oGuest.put("guestLanguage", oGuestJSONObject.optString("guestLanguage"));
				oGuest.put("guestGroupNumber", oGuestJSONObject.optString("guestGroupNumber"));
				oGuest.put("guestTitle", oGuestJSONObject.optString("guestTitle"));
				if (oGuestJSONObject.has("guestVip"))
					oGuest.put("guestVip", oGuestJSONObject.optString("guestVip"));
				if(oGuestJSONObject.has("balanceAmount"))
					oGuest.put("balanceAmount", new BigDecimal(oGuestJSONObject.optDouble("balanceAmount", 0.0)).toPlainString());
				else
					oGuest.put("balanceAmount", BigDecimal.ZERO.toPlainString());
				if(oGuestJSONObject.has("creditLimit"))
					oGuest.put("creditLimit", new BigDecimal(oGuestJSONObject.optDouble("creditLimit", 0.0)).toPlainString());
				else
					oGuest.put("creditLimit", BigDecimal.ZERO.toPlainString());
				JSONArray oUserInfo = oGuestJSONObject.optJSONArray("userInfo");
				if (oUserInfo != null) {
					for (int j = 0; j < oUserInfo.length(); j++) {
						if (!oUserInfo.isNull(j) && oUserInfo.optJSONObject(j).has("index"))
							oGuest.put("info"+oUserInfo.optJSONObject(j).optInt("index"), oUserInfo.optJSONObject(j).optString("info"));
					}
				}
				if (oGuestJSONObject.has("registerNumber"))
					oGuest.put("registerNumber", oGuestJSONObject.optString("registerNumber"));
				if(oGuestJSONObject.has("accountNumber"))
					oGuest.put("accountNumber", oGuestJSONObject.optString("accountNumber"));
				if(oGuestJSONObject.has("masterAccountNum"))
					oGuest.put("masterAccountNum", oGuestJSONObject.optString("masterAccountNum"));
				if(oGuestJSONObject.has("guestFileNumber"))
					oGuest.put("guestFileNumber", String.valueOf(oGuestJSONObject.optInt("guestFileNumber")));
				else
					oGuest.put("guestFileNumber", "0");
				if (!sInterfaceVendor.equals(InfVendor.KEY_STANDARD_TCPIP)) {
					if (oGuestJSONObject.has("noPost"))
						oGuest.put("noPost", oGuestJSONObject.optString("noPost"));
					else
						oGuest.put("noPost", "false");
				}
				oGuest.put("targetPaymentMethod", oGuestJSONObject.optString("targetPaymentMethod"));
				oGuest.put("line", oGuestJSONObject.optString("line"));
				if(oGuestJSONObject.has("guestImage"))
					oGuest.put("guestImage", oGuestJSONObject.optString("guestImage"));
				if(oGuestJSONObject.has("guestSignImage"))
					oGuest.put("guestSignImage", oGuestJSONObject.optString("guestSignImage"));
				
				//get sub guest list
				if (oGuestJSONObject.has("subGuests") && oGuestJSONObject.optJSONArray("subGuests").length() > 0) {
					JSONArray oSubGuests = oGuestJSONObject.optJSONArray("subGuests");
					ArrayList<HashMap<String, String>> oSubGuestList = new ArrayList<HashMap<String, String>>();
					
					for (int j = 0; j < oSubGuests.length(); j++) {
						HashMap<String, String> oSubGuest = new HashMap<String, String>();
						if (!oSubGuests.isNull(j) && oSubGuests.optJSONObject(j).has("name"))
							oSubGuest.put("guestName", oSubGuests.optJSONObject(j).optString("name"));
						if (!oSubGuests.isNull(j) && oSubGuests.optJSONObject(j).has("guestNumber"))
							oSubGuest.put("guestNumber", oSubGuests.optJSONObject(j).optString("guestNumber"));
						if (!oSubGuests.isNull(j) && oSubGuests.optJSONObject(j).has("accountNumber"))
							oSubGuest.put("accountNumber", oSubGuests.optJSONObject(j).optString("accountNumber"));
						oSubGuestList.add(oSubGuest);
					}
					
					oEnquiryResponse.oSubGuestList.put((i+1), oSubGuestList);
				}
				
				if (sInterfaceVendor.equals(InfVendor.KEY_PEGASUS)) {
					if(oGuestJSONObject.has("accountType")) {
						String sAccountTypeName = this.getAccountTypeName(oGuestJSONObject.optInt("accountType"));
						oGuest.put("accountType", sAccountTypeName);
					}

					if(oGuestJSONObject.has("packageName"))
						oGuest.put("packageName", oGuestJSONObject.optString("packageName"));

					if(oGuestJSONObject.has("packageCode"))
						oGuest.put("packageCode", oGuestJSONObject.optString("packageCode"));
					
					if(oGuestJSONObject.has("packageAmount"))
						oGuest.put("packageAmount", new BigDecimal(oGuestJSONObject.optDouble("packageAmount", 0.0)).toPlainString());
					
					if(oGuestJSONObject.has("packageType"))
						oGuest.put("packageType", oGuestJSONObject.optString("packageType"));
					
					if(oGuestJSONObject.has("packageQty"))
						oGuest.put("packageQty", Integer.toString(oGuestJSONObject.optInt("packageQty")));
				}
				
				oEnquiryResponse.oGuestList.add(oGuest);
			}
			
			//get lookup information
			if(enquiryResultJSONObject.has("lookupInfo") && !enquiryResultJSONObject.isNull("lookupInfo")) {
				oEnquiryResponse.sLookupType = enquiryResultJSONObject.optJSONObject("lookupInfo").optString("type", "");
				oEnquiryResponse.sLookupIdContext = enquiryResultJSONObject.optJSONObject("lookupInfo").optString("idContext", "");
				oEnquiryResponse.sLookupId = enquiryResultJSONObject.optJSONObject("lookupInfo").optString("id", "");
			}
		}
		
		return oEnquiryResponse;
	}
	
	// pms posting
	public boolean pmsPosting(FuncCheck oFuncCheck, PosCheckPayment oCheckPayment, PosPaymentMethod oPaymentMethod, int iCurrentPaymentSeq, BigDecimal dPreviousPaymentTotal, List<PosCheckPayment> oAllPaymentList, HashMap<String, Boolean> oPartialPayInfo) {
		int i=0, iCurrentPostingIndex = 0;
		boolean bFailToPost = false;
		boolean bCloseCheck = false;
		boolean bForceToPostCurrentPaymentWithRounding = false;
		List<PostingInfo> oPostingInfoList = new ArrayList<PostingInfo>();
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm:ss");
		String sCurrentTime = timeFmt.print(today);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String sCurrentDate = dateFmt.print(today);
		
		// build the pms posting list
		if(oCheckPayment != null && !oCheckPayment.havePmsPayment())
			return true;
		
		// check whether target posting is for each payment or not
		if(m_bForSinglePaymentPosting && oCheckPayment != null && isSinglePostingForMultiplePayments(oCheckPayment.getPmsPaymentIntfId()))
			return true;
		if(oPartialPayInfo != null && oPartialPayInfo.containsKey("closeCheck") && oPartialPayInfo.get("closeCheck"))
			bCloseCheck = true;
		if(oPartialPayInfo != null && oPartialPayInfo.containsKey("forcePosCurrentPaymenttWithRounding") && oPartialPayInfo.get("forcePosCurrentPaymenttWithRounding"))
			bForceToPostCurrentPaymentWithRounding = true;
		
		PostingInfo oPostingInfo = new PostingInfo();

		oPostingInfo.iCheckPaymnetIndex = i;
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.iOutletId = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oPostingInfo.sWorkStation = AppGlobal.g_oFuncStation.get().getCode();
		oPostingInfo.iStationID = AppGlobal.g_oFuncStation.get().getStationId();
		oPostingInfo.sEmployee = AppGlobal.g_oFuncUser.get().getUserNumber();
		oPostingInfo.sTraceId = "";
		oPostingInfo.sPostingKey = "";
		if(oCheckPayment == null) {
			for(PosCheckPayment oPayment : oAllPaymentList) {
				if(oPayment.havePmsPayment()) {
					PosCheckExtraInfo oPostingKey = oPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_POSTING_KEY, 0);
					if(oPostingKey == null || oPostingKey.getValue().isEmpty())
						continue;
					oPostingInfo.sPostingKey = oPostingKey.getValue();
					break;
				}
			}
		}
		oPostingInfo.sCurrencyCode = AppGlobal.g_oFuncOutlet.get().getCurrencyCode();
		oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
		if (!AppGlobal.g_oFuncOutlet.get().getBusinessDay().isCheckNumGeneratedByOutlet() && !oFuncCheck.isOldCheck())
			oPostingInfo.sCheckNumber = AppGlobal.g_oFuncStation.get().getNextCheckPrefixNumber(false);
		oPostingInfo.sCheckGroupNumber = "";
		oPostingInfo.iCover = oFuncCheck.getCover();
		if(oFuncCheck.getCheckBusinessPeriodId().compareTo("") == 0)
			oPostingInfo.sServingPeriod = AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getCode();
		else
			oPostingInfo.sServingPeriod = AppGlobal.g_oFuncOutlet.get().getBusinessPeriodById(oFuncCheck.getCheckBusinessPeriodId()).getCode();
		if(oCheckPayment == null) {
			oPostingInfo.dPaymentAmount = BigDecimal.ZERO;
			oPostingInfo.dTips = BigDecimal.ZERO;
			oPostingInfo.dPostAmount = BigDecimal.ZERO;
			for(PosCheckPayment oPayment : oAllPaymentList) {
				oPostingInfo.dTips = oPostingInfo.dTips.add(oPayment.getPayTips());
				oPostingInfo.dPostAmount = oPostingInfo.dPostAmount.add(oPayment.getPayTotal().add(oPayment.getPayTips()));
			}
		}else {
			oPostingInfo.dPaymentAmount = oCheckPayment.getPayTotal();
			oPostingInfo.dTips = oCheckPayment.getPayTips();
			oPostingInfo.dPostAmount = oCheckPayment.getPayTotal().add(oCheckPayment.getPayTips());
		}
		oPostingInfo.dSurcharge = BigDecimal.ZERO;
		oPostingInfo.dPreviousPaymentAmount = dPreviousPaymentTotal;
		oPostingInfo.sCardNumber = "";
		oPostingInfo.sClearText = "";
		oPostingInfo.sBusinessDate = AppGlobal.g_oFuncOutlet.get().getFormat1BusinessDayInString();
		oPostingInfo.sBdateId = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId();
		oPostingInfo.sDate = sCurrentDate+"T00:00:00";
		oPostingInfo.sTime = sCurrentTime;
		
		oPostingInfo.sInterfaceVendorKey = "";
		oPostingInfo.sRoomNumber = "";
		oPostingInfo.sGuestNumber = "";
		oPostingInfo.sGuestName = "";
		oPostingInfo.sExpiryDate = "";
		oPostingInfo.sFieldNumber = "";
		oPostingInfo.sRegisterNumber = "";
		oPostingInfo.sGuestFileNumber = "";
		oPostingInfo.sTable = "";
		oPostingInfo.iPaymentSeq = iCurrentPaymentSeq;
		oPostingInfo.sPaymentType = "";
		oPostingInfo.sPostingType = "";
		oPostingInfo.bCloseCheck = bCloseCheck;
		if(oCheckPayment == null) {
			for(PosCheckPayment oPayment : oAllPaymentList) {
				if(oPayment.havePmsPayment()) {
					oPostingInfo = setPostingInfoFromExtraInfo(oPostingInfo, oPayment);
					break;
				}
			}
		}else
			oPostingInfo = setPostingInfoFromExtraInfo(oPostingInfo, oCheckPayment);
		
		if(oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_STANDARD_TCPIP)){
			oPostingInfo.bAllowPostAfterPayment = FuncPMS.isAllowPostAfterPayment(oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).get(0));
			oPostingInfo.bSurchargeAddOnServiceChargeAmount = FuncPMS.isSurchargeAddOnServiceChargeAmount(oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).get(0));
			if(oCheckPayment != null)
				oPostingInfo.dSurcharge = oCheckPayment.getSurcharge();
		}
		
		if(oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_HTNG) && oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).size() == 1 && oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).get(0).getConfigValue().optJSONObject("general").optJSONObject("params").has("payment_type")){
			String sType= FuncPMS.PMS_TYPE_IMAGINE;
			if(oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).get(0).getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").has("pms_type"))
				sType = oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).get(0).getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").optJSONObject("pms_type").optString("value");
			//only for htng shiji posting
			if(sType.equals(FuncPMS.PMS_TYPE_SHIJI)){
				oPostingInfo.sPostingType = oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).get(0).getConfigValue().optJSONObject("general").optJSONObject("params").optJSONObject("payment_type").optString("value");
				oPostingInfo.sLookupType = oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).get(0).getConfigValue().optJSONObject("general").optJSONObject("params").optJSONObject("default_unique_type").optString("value");
				//find how many times current posting had been made
				int iPaymentCounter = getPMSPostCount(oFuncCheck, oPostingInfo.sPostingType, oCheckPayment.getPayTotal(), false);
				if(iPaymentCounter > 9){
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("pms_posting_retry_over_limit");
					return false;
				}
				//append on the checknumber for posting as a key
				if (!oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).get(0).getInterfaceConfig()
						.optJSONObject("pms_setting").optJSONObject("params").has("check_no_with_posting_count")
						|| oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).get(0).getInterfaceConfig()
								.optJSONObject("pms_setting").optJSONObject("params")
								.optJSONObject("check_no_with_posting_count").optInt("value") == 1)
					oPostingInfo.sCheckNumber += iPaymentCounter;
			}
		}
		
		//check whether need to add one second for posting time
		if(oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_STANDARD_TCPIP) && iCurrentPaymentSeq > 1) {
			today = today.plusSeconds((iCurrentPaymentSeq - 1));
			oPostingInfo.sTime = timeFmt.print(today);
		}
		
		//construct all PMS payment information if necessary
		if(AppGlobal.g_oFuncStation.get().isPartialPayment()) {
			String sTargetVendorKey = "";
			PosCheckExtraInfo oTargetPaymentInterfaceId = oAllPaymentList.get(iCurrentPaymentSeq -1).getExtraInfoFromList(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0);
			if(oTargetPaymentInterfaceId != null && !oTargetPaymentInterfaceId.getValue().isEmpty())
				for(PosInterfaceConfig oPosInterfaceConfig:AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PMS)){
					if(oPosInterfaceConfig.getInterfaceId() == Integer.parseInt(oTargetPaymentInterfaceId.getValue())){
						sTargetVendorKey = oPosInterfaceConfig.getInterfaceVendorKey();
						break;
					}
				}
			if((bCloseCheck && iCurrentPaymentSeq == oAllPaymentList.size()) || bForceToPostCurrentPaymentWithRounding) {
				oPostingInfo.sPreviousPmsPayments = new ArrayList<HashMap<String, String>>();
				for(int j = 0;j <= oAllPaymentList.size()-1; j++){
					if(j == iCurrentPaymentSeq -1)
						continue;
					HashMap<String, String> oPreviousPMS = new HashMap<String, String>();
					PosCheckExtraInfo oPosCheckPaymentExtraInfoInterfaceId = oAllPaymentList.get(j).getExtraInfoFromList(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0);
					PosCheckExtraInfo oPosCheckPaymentExtraInfo = oAllPaymentList.get(j).getExtraInfoFromList(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_POSTING_STRING, 0);
					if(oPosCheckPaymentExtraInfoInterfaceId != null && !oPosCheckPaymentExtraInfoInterfaceId.getValue().isEmpty() && oPosCheckPaymentExtraInfo != null && !oPosCheckPaymentExtraInfo.getValue().isEmpty()) {
						oPreviousPMS.put("paymentSeq", String.valueOf(j+1));
						oPreviousPMS.put("payTotal", oAllPaymentList.get(j).getPayTotal().toPlainString());
						oPreviousPMS.put("tips", oAllPaymentList.get(j).getPayTips().toPlainString());
						oPreviousPMS.put("surcharge", oAllPaymentList.get(j).getSurcharge().toString());
						oPreviousPMS.put("postingString", oPosCheckPaymentExtraInfo.getValue());
						for(PosInterfaceConfig oPosInterfaceConfig:AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PMS)){
							if(oPosInterfaceConfig.getInterfaceId() == Integer.parseInt(oPosCheckPaymentExtraInfoInterfaceId.getValue())){
								oPreviousPMS.put("vendorKey", oPosInterfaceConfig.getInterfaceVendorKey());
								break;
							}
						}
						if(oPreviousPMS.containsKey("vendorKey") && sTargetVendorKey.equals(oPreviousPMS.get("vendorKey")))
							oPostingInfo.sPreviousPmsPayments.add(oPreviousPMS);
					}
				}
			}
		}
		
		//construct other PMS payment total if necessary
		oPostingInfo.sOtherPmsPayments = new ArrayList<HashMap<String, String>>();
		
		boolean bAllPaymentIsPMS = true;
		for(PosCheckPayment oTempCheckPayment: oAllPaymentList) {
			if(!oTempCheckPayment.havePmsPayment()) {
				bAllPaymentIsPMS = false;
			}
		}
		if((bAllPaymentIsPMS && oAllPaymentList.size() > 1) 
				|| oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_XMS)) {
			int iPaymentSeq = 0;
			for(PosCheckPayment oTempCheckPayment: oAllPaymentList) {
				iPaymentSeq++;
				if(iPaymentSeq == iCurrentPaymentSeq)
					continue;
				
				HashMap<String, String> oOtherPMS = new HashMap<String, String>();
				
				if(oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_XMS)) {
					if(oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_TRACE_ID, 0))
						oPostingInfo.sTraceId = oFuncCheck.getCheckExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_TRACE_ID, 0);
					if(oTempCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_ROOM, 0) != null)
						oOtherPMS.put("roomNumber", oTempCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_ROOM, 0).getValue().toString());
					if(oTempCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_GUEST_NO, 0) != null)
						oOtherPMS.put("guestNumber", oTempCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_GUEST_NO, 0).getValue().toString());
					if(oTempCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_SUB_ACCOUNT_NUMBER, 0) != null)
						oOtherPMS.put("subAccountNumber", oTempCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_SUB_ACCOUNT_NUMBER, 0).getValue().toString());
					if(oTempCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_INTERNAL_USE, 0) != null) {
						try {
							JSONObject oInternalUse = new JSONObject(oTempCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_INTERNAL_USE, 0).getValue());
							if(oInternalUse.has("posting_type"))
								oOtherPMS.put("postingType", oInternalUse.optString("posting_type", ""));
							if(oInternalUse.has("external_type"))
								oOtherPMS.put("externalType", oInternalUse.optString("external_type", ""));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					oOtherPMS.put("code", String.valueOf(oTempCheckPayment.getPaymentMethodId()));
				}
				oOtherPMS.put("paymentSeq", String.valueOf(iPaymentSeq));
				oOtherPMS.put("payTotal", oTempCheckPayment.getPayTotal().toPlainString());
				oPostingInfo.sOtherPmsPayments.add(oOtherPMS);
			}
		}
		
		oPostingInfoList.add(oPostingInfo);
/*		
System.out.println("DEBUG PMS POST ======================= " + oPostingInfo.iInterfaceId + ", " +
		oPostingInfo.sInterfaceVendorKey + ", " +
		oPostingInfo.iCheckPaymnetIndex + ", " +
		oPostingInfo.sOutletCode + ", " +
		oPostingInfo.sWorkStation + ", " +
		oPostingInfo.sEmployee + ", " +
		oPostingInfo.sRoomNumber + ", " +
		oPostingInfo.sGuestNumber + ", " +
		oPostingInfo.sGuestName + ", " +
		oPostingInfo.sExpiryDate + ", " +
		oPostingInfo.sRegisterNumber + ", " +
		oPostingInfo.sCheckNumber + ", " +
		oPostingInfo.sCheckGroupNumber + ", " +
		oPostingInfo.sTable + ", " +
		oPostingInfo.iCover + ", " +
		oPostingInfo.sServingPeriod + ", " +
		oPostingInfo.sPaymentType + ", " +
		oPostingInfo.dPaymentAmount + ", " +
		oPostingInfo.dTips + ", " +
		oPostingInfo.dPostAmount + ", " +
		oPostingInfo.dPreviousPaymentAmount + ", " +
		oPostingInfo.sCardNumber + ", " +
		oPostingInfo.sClearText + ", " +
		oPostingInfo.sGuestFileNumber + ", " +
		oPostingInfo.sDate + ", " +
		oPostingInfo.sTime);
*/
		
		// no pms posting is needed
		if(oPostingInfoList.isEmpty())
			return true;
		
		// do pre post
		for(i=0; i<oPostingInfoList.size(); i++) {
			if(!prePost(oFuncCheck, oCheckPayment, oPaymentMethod, oPostingInfoList.get(i), FuncPMS.TYPE_PAY_PMS))
				// Fail to pms pre-post, stop here
				return false;
		}
		
		// do posting
		for(i=0; i<oPostingInfoList.size(); i++) {
			JSONObject oCheckInformationJSONObject = oFuncCheck.contrustPmsPostingCheckInformation(oCheckPayment);
			PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
			boolean bResult = false;
			JSONObject oReturnInfo = null;
			if(oPostingInfoList.get(i).sInterfaceVendorKey.equals(InfVendor.KEY_4700_SERIAL_PORT) || oPostingInfoList.get(i).sInterfaceVendorKey.equals(InfVendor.KEY_ASPEN_PMS)) {
				//get interface setup
				PosInterfaceConfig oPMSInterfaceConfig = null;
				for(PosInterfaceConfig oPosInterfaceConfig:AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PMS)){
					if(oPosInterfaceConfig.getInterfaceId() == oPostingInfoList.get(i).iInterfaceId){
						oPMSInterfaceConfig = oPosInterfaceConfig;
						break;
					}
				}
				
				if(oPostingInfoList.get(i).sInterfaceVendorKey.equals(InfVendor.KEY_4700_SERIAL_PORT)){
					FuncPMS4700SerialPort oFuncPMS4700SerialPort = new FuncPMS4700SerialPort();
					oFuncPMS4700SerialPort.init(oPMSInterfaceConfig);
					bResult = oFuncPMS4700SerialPort.doPosting(oFuncCheck, oCheckPayment, fromPmsPostingJSONObject(oPostingInfoList.get(i)), false);
				}else if(oPostingInfoList.get(i).sInterfaceVendorKey.equals(InfVendor.KEY_ASPEN_PMS)){
					// aspen pms interface - share the same posting protocol as aspen membership interface
					List<PosCheckPayment> oCheckPaymentsList = new ArrayList<PosCheckPayment>();
					oCheckPaymentsList.add(oCheckPayment);
					FuncMembershipInterface oFuncMembershipInterfaceAspen = new FuncMembershipInterface(oPMSInterfaceConfig);
					HashMap<String, String> oAspenPostingInfo = new HashMap<String, String>();
					oAspenPostingInfo.put("memberNo", oPostingInfoList.get(i).sRoomNumber);
					oAspenPostingInfo.put("traceId", oPostingInfoList.get(i).sTraceId);
					oAspenPostingInfo.put("referenceNo", "");
					oAspenPostingInfo.put("voucehrType", "");
					oAspenPostingInfo.put("isFromPms", "true");
					bResult = oFuncMembershipInterfaceAspen.membershipPosting(oAspenPostingInfo, oFuncCheck, oCheckPaymentsList, null, false, false);
				}
			}else {
				oReturnInfo = oInterfaceConfig.doPmsPosting(oCheckInformationJSONObject, fromPmsPostingJSONObject(oPostingInfoList.get(i)));
				if(oReturnInfo == null)
					bResult = false;
				else{
					if (oReturnInfo.has("postingKey") && !oReturnInfo.optString("postingKey", "").isEmpty()) {
						if(oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_POSTING_KEY, 0) == null) {
							oCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_POSTING_KEY, 0, oReturnInfo.optString("postingKey"));
						}else
							oCheckPayment.updateExtraInfo(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_POSTING_KEY, 0, oReturnInfo.optString("postingKey"));
					} else if (oReturnInfo.has("traceId") && !oReturnInfo.optString("traceId", "").isEmpty()) {		// for Pegasus iPMS, save trans id
						if(oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_TRACE_ID, 0) == null) {
							oCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_TRACE_ID, 0, oReturnInfo.optString("traceId"));
						}else
							oCheckPayment.updateExtraInfo(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_TRACE_ID, 0, oReturnInfo.optString("traceId"));
					} else if (oReturnInfo.has("postingString") && !oReturnInfo.optString("postingString", "").isEmpty()) {		// for partial pay PMS handling, save postingString
						if(oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_POSTING_STRING, 0) == null) {
							oCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_POSTING_STRING, 0, oReturnInfo.optString("postingString"));
						}else
							oCheckPayment.updateExtraInfo(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_POSTING_STRING, 0, oReturnInfo.optString("postingString"));
					}
					
					if(oCheckPayment == null) {
						for(PosCheckPayment oPayment : oAllPaymentList) {
							if(oPayment.havePmsPayment())
								oPayment.setPostingSuccess(true);
						}
					}else
						oCheckPayment.setPostingSuccess(true);
					bResult = true;
				}
			}
			
			if(bResult == false) {
				if(oInterfaceConfig.getLastErrorType() != null && oInterfaceConfig.getLastErrorType().equals(FuncPMS.ERROR_TYPE_PMS))
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("pms_return_error") + ":[" + oInterfaceConfig.getLastErrorMessage() + "]";
				else if(oInterfaceConfig.getLastErrorCode() != 0)
					m_sLastErrorMessage = getErrorMessage(oInterfaceConfig.getLastErrorCode());
				else
					m_sLastErrorMessage = oInterfaceConfig.getLastErrorMessage();
				bFailToPost = true;
				iCurrentPostingIndex = i;
			}
			
			if(bFailToPost)
				break;
		}
		
		// void posting if fail to Post
		if(bFailToPost) {
			// do pre post
			for(i=0; i<iCurrentPostingIndex; i++) {
				if(!prePost(oFuncCheck, oCheckPayment, oPaymentMethod, oPostingInfoList.get(i), FuncPMS.TYPE_VOID_PMS))
					// Fail to pms pre-post, stop here
					return false;
			}
			
			// do void posting
			for(i=0; i<iCurrentPostingIndex; i++) {
				JSONObject oCheckInformationJSONObject = oFuncCheck.contrustPmsPostingCheckInformation(oCheckPayment);
				PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
				oInterfaceConfig.doPmsVoidPosting(oCheckInformationJSONObject, fromPmsPostingJSONObject(oPostingInfoList.get(i)));
			}
			
			return false;
		}
		
		return true;
	}
	
	// pms posting
	public boolean pmsQuantityPosting(HashMap<String, String> oPostingInfoMap, FuncCheck oFuncCheck) {
		int i=0, iCurrentPostingIndex = 0;
		boolean bFailToPost = false;
		List<PostingInfo> oPostingInfoList = new ArrayList<PostingInfo>();
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm:ss");
		String sCurrentTime = timeFmt.print(today);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String sCurrentDate = dateFmt.print(today);
		
		PostingInfo oPostingInfo = new PostingInfo();

		oPostingInfo.iCheckPaymnetIndex = i;
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.sWorkStation = AppGlobal.g_oFuncStation.get().getCode();
		oPostingInfo.sEmployee = AppGlobal.g_oFuncUser.get().getUserNumber();
		oPostingInfo.sTraceId = "";
		oPostingInfo.sPostingKey = oPostingInfoMap.get("postingKey");
		oPostingInfo.sCurrencyCode = AppGlobal.g_oFuncOutlet.get().getCurrencyCode();
		oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
		oPostingInfo.sCheckGroupNumber = "";
		oPostingInfo.iCover = oFuncCheck.getCover();
		if(oFuncCheck.getCheckBusinessPeriodId().compareTo("") == 0)
			oPostingInfo.sServingPeriod = AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getCode();
		else
			oPostingInfo.sServingPeriod = AppGlobal.g_oFuncOutlet.get().getBusinessPeriodById(oFuncCheck.getCheckBusinessPeriodId()).getCode();
		oPostingInfo.dPaymentAmount = BigDecimal.ZERO;
		oPostingInfo.dTips = BigDecimal.ZERO;
		oPostingInfo.dPostAmount = new BigDecimal(oPostingInfoMap.get("postAmount"));
		oPostingInfo.dPreviousPaymentAmount = BigDecimal.ZERO;
		oPostingInfo.sCardNumber = "";
		oPostingInfo.sClearText = "";
		oPostingInfo.sBusinessDate = AppGlobal.g_oFuncOutlet.get().getFormat1BusinessDayInString();
		oPostingInfo.sDate = sCurrentDate+"T00:00:00";
		oPostingInfo.sTime = sCurrentTime;

		oPostingInfo.iInterfaceId = Integer.parseInt(oPostingInfoMap.get("interfaceId"));
		oPostingInfo.sInterfaceVendorKey = oPostingInfoMap.get("interfaceVendorKey");
		oPostingInfo.sRoomNumber = oPostingInfoMap.get("roomNumber");
		oPostingInfo.sGuestNumber = oPostingInfoMap.get("guestNumber");
		oPostingInfo.sGuestName = "";
		oPostingInfo.sExpiryDate = "";
		oPostingInfo.sFieldNumber = "";
		oPostingInfo.sRegisterNumber = "";
		oPostingInfo.sGuestFileNumber = "";
		oPostingInfo.sTable = "";
		oPostingInfo.sPaymentType = oPostingInfoMap.get("paymentType");
		
		oPostingInfoList.add(oPostingInfo);
		
		// no pms posting is needed
		if(oPostingInfoList.isEmpty())
			return true;
		
		// do pre post
		PosCheckPayment oCheckPayment = new PosCheckPayment();
		PosPaymentMethod oPaymentMethod = new PosPaymentMethod();
		for(i=0; i<oPostingInfoList.size(); i++) {
			if(!prePost(oFuncCheck, oCheckPayment, oPaymentMethod, oPostingInfoList.get(i), FuncPMS.TYPE_PAY_PMS))
				// Fail to pms pre-post, stop here
				return false;
		}
		
		// do posting
		for(i=0; i<oPostingInfoList.size(); i++) {
			JSONObject oCheckInformationJSONObject = oFuncCheck.contrustPmsPostingCheckInformation(oCheckPayment);
			PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
			boolean bResult = false;
			JSONObject oReturnInfo = null;
			oReturnInfo = oInterfaceConfig.doPmsPosting(oCheckInformationJSONObject, fromPmsPostingJSONObject(oPostingInfoList.get(i)));
			if(oReturnInfo == null)
				bResult = false;
			else{
				bResult = true;
			}
			
			if(bResult == false) {
				if(oInterfaceConfig.getLastErrorType() != null && oInterfaceConfig.getLastErrorType().equals(FuncPMS.ERROR_TYPE_PMS))
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("pms_return_error") + ":[" + oInterfaceConfig.getLastErrorMessage() + "]";
				else if(oInterfaceConfig.getLastErrorCode() != 0)
					m_sLastErrorMessage = getErrorMessage(oInterfaceConfig.getLastErrorCode());
				else
					m_sLastErrorMessage = oInterfaceConfig.getLastErrorMessage();
				bFailToPost = true;
				iCurrentPostingIndex = i;
			}
			
			if(bFailToPost)
				break;
		}

		
		if(bFailToPost)
			return false;
		else
			return true;
	}
	
	// pms void posting
	public boolean pmsVoidPosting(FuncCheck oFuncCheck, PosCheckPayment oCheckPayment, PosPaymentMethod oPaymentMethod, int iCurrentPaymentSeq, BigDecimal dPreviousPaymentTotal, List<PosCheckPayment> oAllPaymentList, JSONObject oPMSItemDetailsForAdjustPayment) {
		int i=0;
		boolean bFailToPost = false;
		List<PostingInfo> oPostingInfoList = new ArrayList<PostingInfo>();
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm:ss");
		String sCurrentTime = timeFmt.print(today);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String sCurrentDate = dateFmt.print(today);
		
		// build the pms posting list
		if(oCheckPayment != null && !oCheckPayment.havePmsPayment())
			return true;
		
		// check whether target posting is for each payment or not
		if(m_bForSinglePaymentPosting && oCheckPayment != null && isSinglePostingForMultiplePayments(oCheckPayment.getPmsPaymentIntfId()))
			return true;
		
		PostingInfo oPostingInfo = new PostingInfo();

		oPostingInfo.iCheckPaymnetIndex = i;
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.sWorkStation = AppGlobal.g_oFuncStation.get().getCode();
		oPostingInfo.iOutletId = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oPostingInfo.iStationID = AppGlobal.g_oFuncStation.get().getStationId();
		oPostingInfo.sEmployee = AppGlobal.g_oFuncUser.get().getUserNumber();
		oPostingInfo.sTraceId = "";
		oPostingInfo.sPostingKey = "";
		oPostingInfo.sCurrencyCode = AppGlobal.g_oFuncOutlet.get().getCurrencyCode();
		oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
		if (!AppGlobal.g_oFuncOutlet.get().getBusinessDay().isCheckNumGeneratedByOutlet() && !oFuncCheck.isOldCheck())
			oPostingInfo.sCheckNumber = AppGlobal.g_oFuncStation.get().getNextCheckPrefixNumber(false);
		oPostingInfo.sCheckGroupNumber = "";
		oPostingInfo.iCover = oFuncCheck.getCover();
		if(oFuncCheck.getCheckBusinessPeriodId().compareTo("") == 0)
			oPostingInfo.sServingPeriod = AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getCode();
		else
			oPostingInfo.sServingPeriod = AppGlobal.g_oFuncOutlet.get().getBusinessPeriodById(oFuncCheck.getCheckBusinessPeriodId()).getCode();
		if(oCheckPayment == null) {
			oPostingInfo.dPaymentAmount = BigDecimal.ZERO;
			oPostingInfo.dTips = BigDecimal.ZERO;
			oPostingInfo.dPostAmount = BigDecimal.ZERO;
			for(PosCheckPayment oPayment : oAllPaymentList) {
				oPostingInfo.dTips = oPostingInfo.dTips.add(oPayment.getPayTips());
				oPostingInfo.dPostAmount = oPostingInfo.dPostAmount.add(oPayment.getPayTotal().add(oPayment.getPayTips()));
			}
		}else {
			oPostingInfo.dPaymentAmount = oCheckPayment.getPayTotal();
			oPostingInfo.dTips = oCheckPayment.getPayTips();
			oPostingInfo.dPostAmount = oCheckPayment.getPayTotal().add(oCheckPayment.getPayTips());
		}
		oPostingInfo.dSurcharge = BigDecimal.ZERO;
		oPostingInfo.dPreviousPaymentAmount = dPreviousPaymentTotal;
		oPostingInfo.sCardNumber = "";
		oPostingInfo.sClearText = "";
		oPostingInfo.sBusinessDate = AppGlobal.g_oFuncOutlet.get().getFormat1BusinessDayInString();
		oPostingInfo.sBdateId = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId();
		oPostingInfo.sDate = sCurrentDate+"T00:00:00";
		oPostingInfo.sTime = sCurrentTime;
		
		oPostingInfo.sInterfaceVendorKey = "";
		oPostingInfo.sRoomNumber = "";
		oPostingInfo.sGuestNumber = "";
		oPostingInfo.sGuestName = "";
		oPostingInfo.sExpiryDate = "";
		oPostingInfo.sFieldNumber = "";
		oPostingInfo.sRegisterNumber = "";
		oPostingInfo.sGuestFileNumber = "";
		oPostingInfo.sTable = "";
		oPostingInfo.sPaymentType = "";
		oPostingInfo.iPaymentSeq = iCurrentPaymentSeq;
		if(oCheckPayment == null) {
			for(PosCheckPayment oPayment : oAllPaymentList) {
				if(oPayment.havePmsPayment()) {
					oPostingInfo = setPostingInfoFromExtraInfo(oPostingInfo, oPayment);
					break;
				}
			}
		} else
			oPostingInfo = setPostingInfoFromExtraInfo(oPostingInfo, oCheckPayment);
		
		if(oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_STANDARD_TCPIP)){
			oPostingInfo.bAllowPostAfterPayment = FuncPMS.isAllowPostAfterPayment(oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).get(0));
			oPostingInfo.bSurchargeAddOnServiceChargeAmount = FuncPMS.isSurchargeAddOnServiceChargeAmount(oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).get(0));
			if(oCheckPayment != null)
				oPostingInfo.dSurcharge = oCheckPayment.getSurcharge();
		}
		
		if(oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_HTNG) && oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).size() == 1 && oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).get(0).getConfigValue().optJSONObject("general").optJSONObject("params").has("payment_type")){
			String sType= FuncPMS.PMS_TYPE_IMAGINE;
			if(oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).get(0).getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").has("pms_type"))
				sType = oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).get(0).getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").optJSONObject("pms_type").optString("value");
			//only for htng shiji posting
			if(sType.equals(FuncPMS.PMS_TYPE_SHIJI)){
				oPostingInfo.sPostingType = oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).get(0).getConfigValue().optJSONObject("general").optJSONObject("params").optJSONObject("payment_type").optString("value");
				oPostingInfo.sLookupType = oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).get(0).getConfigValue().optJSONObject("general").optJSONObject("params").optJSONObject("default_unique_type").optString("value");
				//find how many times current posting had been made
				int iPaymentCounter = getPMSPostCount(oFuncCheck, oPostingInfo.sPostingType, oCheckPayment.getPayTotal(), true);
				if(iPaymentCounter > 9){
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("pms_posting_retry_over_limit");
					return false;
				}
			}
		}
		
		//check whether need to add one second for posting time
		if(oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_STANDARD_TCPIP) && iCurrentPaymentSeq > 1) {
			today = today.plusSeconds((iCurrentPaymentSeq - 1));
			oPostingInfo.sTime = timeFmt.print(today);
		}
		
		//construct other PMS payment total if necessary
		oPostingInfo.sOtherPmsPayments = new ArrayList<HashMap<String, String>>();
		
		boolean bAllPaymentIsPMS = true;
		for(PosCheckPayment oTempCheckPayment: oAllPaymentList) {
			if(!oTempCheckPayment.havePmsPayment()) {
				bAllPaymentIsPMS = false;
			}
		}
		if((bAllPaymentIsPMS && oAllPaymentList.size() > 1)
				|| oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_XMS)) {
			int iPaymentSeq = 0;
			for(PosCheckPayment oTempCheckPayment: oAllPaymentList) {
				iPaymentSeq++;
				if(iPaymentSeq == iCurrentPaymentSeq)
					continue;
				
				HashMap<String, String> oOtherPMS = new HashMap<String, String>();
				if(oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_XMS)) {
					for(PosCheckExtraInfo oExtraInfo : oTempCheckPayment.getCheckExtraInfoArrayList()) {
						if(oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_TRACE_ID, 0))
							oPostingInfo.sTraceId = oFuncCheck.getCheckExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_TRACE_ID, 0);
						if(oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_ROOM))
							oOtherPMS.put("roomNumber", oExtraInfo.getValue());
						if(oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_GUEST_NO))
							oOtherPMS.put("guestNumber", oExtraInfo.getValue());
						if(oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERNAL_USE)) {
							try {
								JSONObject oInternalUse = new JSONObject(oTempCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_INTERNAL_USE, 0).getValue());
								if(oInternalUse.has("posting_type"))
									oOtherPMS.put("postingType", oInternalUse.optString("posting_type", ""));
								if(oInternalUse.has("external_type"))
									oOtherPMS.put("externalType", oInternalUse.optString("external_type", ""));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						oOtherPMS.put("code", String.valueOf(oTempCheckPayment.getPaymentMethodId()));
					}
				}
				oOtherPMS.put("paymentSeq", String.valueOf(iPaymentSeq));
				oOtherPMS.put("payTotal", oTempCheckPayment.getPayTotal().toPlainString());
				oPostingInfo.sOtherPmsPayments.add(oOtherPMS);
			}
		}
		
		oPostingInfoList.add(oPostingInfo);
		
/*
System.out.println("DEBUG VOID PMS ======================= " + oPostingInfo.iInterfaceId + ", " +
		oPostingInfo.sInterfaceVendorKey + ", " +
		oPostingInfo.iCheckPaymnetIndex + ", " +
		oPostingInfo.sOutletCode + ", " +
		oPostingInfo.sWorkStation + ", " +
		oPostingInfo.sEmployee + ", " +
		oPostingInfo.sRoomNumber + ", " +
		oPostingInfo.sGuestNumber + ", " +
		oPostingInfo.sGuestName + ", " +
		oPostingInfo.sExpiryDate + ", " +
		oPostingInfo.iPortfolioNumber + ", " +
		oPostingInfo.sRegisterNumber + ", " +
		oPostingInfo.sCheckNumber + ", " +
		oPostingInfo.sCheckGroupNumber + ", " +
		oPostingInfo.sTable + ", " +
		oPostingInfo.iCover + ", " +
		oPostingInfo.sServingPeriod + ", " +
		oPostingInfo.sPaymentType + ", " +
		oPostingInfo.dPaymentAmount + ", " +
		oPostingInfo.dTips + ", " +
		oPostingInfo.dPostAmount + ", " +
		oPostingInfo.dPreviousPaymentAmount + ", " +
		oPostingInfo.sCardNumber + ", " +
		oPostingInfo.sClearText + ", " +
		oPostingInfo.sGuestFileNumber + ", " +
		oPostingInfo.sDate + ", " +
		oPostingInfo.sTime);
*/		
		
		// no pms posting is needed
		if(oPostingInfoList.isEmpty())
			return true;
		
		// do pre post
		for(i=0; i<oPostingInfoList.size(); i++) {
			if(!prePost(oFuncCheck, oCheckPayment, oPaymentMethod, oPostingInfoList.get(i), FuncPMS.TYPE_VOID_PMS))
				// Fail to pms pre-post, stop here
				bFailToPost = true;
		}
		
		if(bFailToPost)
			return false;
		
		// do voiding posting
		for(i=0; i<oPostingInfoList.size(); i++) {
			if(oPostingInfoList.get(i).sInterfaceVendorKey.equals(InfVendor.KEY_4700_SERIAL_PORT) || oPostingInfoList.get(i).sInterfaceVendorKey.equals(InfVendor.KEY_ASPEN_PMS)) {
				PosInterfaceConfig oPMSInterfaceConfig = null;
				
				//get interface setup
				for(PosInterfaceConfig oPosInterfaceConfig:AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PMS)){
					if(oPosInterfaceConfig.getInterfaceId() == oPostingInfo.iInterfaceId){
						oPMSInterfaceConfig = oPosInterfaceConfig;
						break;
					}
				}
				
				if(oPostingInfoList.get(i).sInterfaceVendorKey.equals(InfVendor.KEY_4700_SERIAL_PORT)){
					FuncPMS4700SerialPort oFuncPMS4700SerialPort = new FuncPMS4700SerialPort();
					oFuncPMS4700SerialPort.init(oPMSInterfaceConfig);
					boolean bResult = oFuncPMS4700SerialPort.doPosting(oFuncCheck, oCheckPayment, fromPmsPostingJSONObject(oPostingInfoList.get(i)), true);
					if(bResult == false)
						bFailToPost = true;
				}else if(oPostingInfoList.get(i).sInterfaceVendorKey.equals(InfVendor.KEY_ASPEN_PMS)){
					FuncMembershipInterface oFuncMembershipInterfaceAspen = new FuncMembershipInterface(oPMSInterfaceConfig);
					oFuncMembershipInterfaceAspen.membershipVoidPosting(oFuncCheck, oFuncCheck.getCheckPaymentList(), dPreviousPaymentTotal, false, 0);
				}
			}else {
				JSONObject oCheckInformationJSONObject = null;
				if(oPMSItemDetailsForAdjustPayment != null)
					oCheckInformationJSONObject = oPMSItemDetailsForAdjustPayment;
				else
					oCheckInformationJSONObject = oFuncCheck.contrustPmsPostingCheckInformation(oCheckPayment);
				PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
				if(oInterfaceConfig.doPmsVoidPosting(oCheckInformationJSONObject, fromPmsPostingJSONObject(oPostingInfoList.get(i))) == false) {
					bFailToPost = true;
				}
			}
		}
		
		if(bFailToPost)
			return false;
		else
			return true;
	}
	
	public boolean restartPMSShell(int iInterfaceId, String sVendorKey) {
		// For smart station 
		if(AppGlobal.g_oFuncSmartStation.isSmartStationModel() && !AppGlobal.g_oFuncSmartStation.isServiceMasterRole()) {
			if(AppGlobal.g_oFuncSmartStation.isWorkstationRole()) {
				AppGlobal.pmsShellOperation(iInterfaceId, sVendorKey, FuncPMS.SHELL_OPERATION_RESTART);
				return true;
			}else
				return false;
		}
		
		// For normal operation or service master of smart station
		PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
		boolean bResult = oInterfaceConfig.restartPMSShell(iInterfaceId, sVendorKey);
		if(!bResult) {
			if(oInterfaceConfig.getLastErrorMessage().equals("shell_is_not_alive"))
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_open_shell");
			else if(oInterfaceConfig.getLastErrorCode() > 0)
				m_sLastErrorMessage = getErrorMessage(oInterfaceConfig.getLastErrorCode());
			else 
				m_sLastErrorMessage = oInterfaceConfig.getLastErrorMessage();
		}
		
		return bResult;
	}
	
	public boolean stopPMSShell(int iInterfaceId, String sVendorKey) {
		// For smart station 
		if(AppGlobal.g_oFuncSmartStation.isSmartStationModel() && !AppGlobal.g_oFuncSmartStation.isServiceMasterRole()) {
			if(AppGlobal.g_oFuncSmartStation.isWorkstationRole()) {
				AppGlobal.pmsShellOperation(iInterfaceId, sVendorKey, FuncPMS.SHELL_OPERATION_STOP);
				return true;
			}else
				return false;
		}
		
		// For normal operation or service master of smart station
		PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
		return oInterfaceConfig.stopPMSShell(iInterfaceId, sVendorKey);
	}
	
	private boolean prePost(FuncCheck oFuncCheck, PosCheckPayment oCheckPayment, PosPaymentMethod oPaymentMethod, PostingInfo oPostingInfo, String sType) {
		// no pre post for R8
		if(oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_R8))
			return true;
		// no pre post for Standard PMS
		else if(oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_STANDARD_TCPIP))
			return true;
		// no pre post for HTNG PMS (iMagine PMS)
		else if(oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_HTNG))
			return true;
		// no pre post for Pegasus PMS
		else if(oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_PEGASUS))
			return true;
		// no pre post for XMS PMS
		else if(oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_XMS))
			return true;
		// no pre post for ASPEN PMS
		else if(oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_ASPEN_PMS))
			return true;
		//for 4700 PMS
		else if(oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_4700_TCPIP) || oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_4700_SERIAL_PORT)) {
			PosInterfaceConfig oPMSInterfaceConfig = null;
			if(sType.equals(FuncPMS.TYPE_PAY_PMS))
				return true;
			else {
				//get setup of void payment no prepost setup
				boolean bNoPrepost = false;
				for(PosInterfaceConfig oPosInterfaceConfig:AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PMS)){
					if(oPosInterfaceConfig.getInterfaceId() == oPostingInfo.iInterfaceId){
						oPMSInterfaceConfig = oPosInterfaceConfig;
						break;
					}
				}
				
				if(oPMSInterfaceConfig.getInterfaceConfig().optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_voidpay_no_prepost").has("value") &&
						oPMSInterfaceConfig.getInterfaceConfig().optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_voidpay_no_prepost").optInt("value", 0) == 1)
					bNoPrepost = true;
				
				if(!bNoPrepost && oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS) != null && !oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).isEmpty()) {
					List<PosInterfaceConfig> oPosInterfaceConfigs = oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS);
					for(PosInterfaceConfig oPosInterfaceConfig:oPosInterfaceConfigs) {
						if(oPosInterfaceConfig.getInterfaceId() == oPostingInfo.iInterfaceId) {
							JSONObject oPMSPaymentSetup = oPosInterfaceConfig.getConfigValue();
							if(oPMSPaymentSetup.optJSONObject("general").optJSONObject("params").optJSONObject("no_pre_post").optInt("value", 0) == 1)
								bNoPrepost = true;
							break;
						}
					}
				}
				
				if(!bNoPrepost) {
					if(oPostingInfo.sInterfaceVendorKey.equals(InfVendor.KEY_4700_TCPIP)) {
						PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
						JSONObject oCheckInformationJSONObject = oFuncCheck.contrustPmsPostingCheckInformation(oCheckPayment);
						boolean bResult = oInterfaceConfig.doPmsPrepost(oCheckInformationJSONObject, fromPmsPostingJSONObject(oPostingInfo));
						if(!bResult)
							return false;
						else {
							if(oInterfaceConfig.getLastSuccessResult().has("guestName") && !oInterfaceConfig.getLastSuccessResult().optString("guestName", "").isEmpty())
								oPostingInfo.sGuestName = oInterfaceConfig.getLastSuccessResult().optString("guestName");
							return true;
						}
					}else {
						FuncPMS4700SerialPort oFuncPMS4700SerialPort = new FuncPMS4700SerialPort();
						oFuncPMS4700SerialPort.init(oPMSInterfaceConfig);
						boolean bResult = oFuncPMS4700SerialPort.doPrePost(oFuncCheck, oCheckPayment, fromPmsPostingJSONObject(oPostingInfo));
						if(!bResult) {
							return false;
						}else {
							if(!oFuncPMS4700SerialPort.getLastResult().isEmpty())
								oPostingInfo.sGuestName = oFuncPMS4700SerialPort.getLastResult();
							return true;
						}
					}
				}
				
				return true;
			}
		}
					
		return false;
	}
	
	private JSONObject fromPmsPostingJSONObject(PostingInfo oPostingInfo) {
		JSONObject oPostingJSONobject = new JSONObject();
		
		try {
			oPostingJSONobject.put("interfaceId", oPostingInfo.iInterfaceId);
			oPostingJSONobject.put("interfaceVendorKey", oPostingInfo.sInterfaceVendorKey);
			oPostingJSONobject.put("outlet", oPostingInfo.sOutletCode);
			oPostingJSONobject.put("outletId", oPostingInfo.iOutletId);
			oPostingJSONobject.put("workstation", oPostingInfo.sWorkStation);
			oPostingJSONobject.put("stationId", oPostingInfo.iStationID);
			oPostingJSONobject.put("employee", oPostingInfo.sEmployee);
			oPostingJSONobject.put("traceId", oPostingInfo.sTraceId);
			oPostingJSONobject.put("postingKey", oPostingInfo.sPostingKey);
			oPostingJSONobject.put("roomNumber", oPostingInfo.sRoomNumber);
			oPostingJSONobject.put("guestNumber", oPostingInfo.sGuestNumber);
			oPostingJSONobject.put("guestName", oPostingInfo.sGuestName);
			oPostingJSONobject.put("expiryDate", oPostingInfo.sExpiryDate);
			oPostingJSONobject.put("fieldNumber", oPostingInfo.sFieldNumber);
			oPostingJSONobject.put("registerNumber", oPostingInfo.sRegisterNumber);
			oPostingJSONobject.put("checkNumber", oPostingInfo.sCheckNumber);
			oPostingJSONobject.put("checkGroupNumber", oPostingInfo.sCheckGroupNumber);
			oPostingJSONobject.put("table", oPostingInfo.sTable);
			oPostingJSONobject.put("cover", oPostingInfo.iCover);
			oPostingJSONobject.put("servingPeriod", oPostingInfo.sServingPeriod);
			oPostingJSONobject.put("paymentSeq", oPostingInfo.iPaymentSeq);
			oPostingJSONobject.put("paymentType", oPostingInfo.sPaymentType);
			oPostingJSONobject.put("paymentAmount", oPostingInfo.dPaymentAmount.toPlainString());
			oPostingJSONobject.put("tips", oPostingInfo.dTips.toPlainString());
			oPostingJSONobject.put("postAmount", oPostingInfo.dPostAmount.toPlainString());
			oPostingJSONobject.put("surcharge", oPostingInfo.dSurcharge.toPlainString());
			oPostingJSONobject.put("previousPaymentAmount", oPostingInfo.dPreviousPaymentAmount.toPlainString());
			oPostingJSONobject.put("cardNumber", oPostingInfo.sCardNumber);
			oPostingJSONobject.put("clearText", oPostingInfo.sClearText);
			oPostingJSONobject.put("guestFileNumber", oPostingInfo.sGuestFileNumber);
			oPostingJSONobject.put("businessDate", oPostingInfo.sBusinessDate);
			oPostingJSONobject.put("businessDateId", oPostingInfo.sBdateId);
			oPostingJSONobject.put("currencyCode", oPostingInfo.sCurrencyCode);
			oPostingJSONobject.put("allowPostAfterPayment", oPostingInfo.bAllowPostAfterPayment);
			oPostingJSONobject.put("surchargeAddOnServiceChargeAmount", oPostingInfo.bSurchargeAddOnServiceChargeAmount);
			oPostingJSONobject.put("date", oPostingInfo.sDate);
			oPostingJSONobject.put("time", oPostingInfo.sTime);
			oPostingJSONobject.put("lookupType", oPostingInfo.sLookupType);
			oPostingJSONobject.put("lookupIdContext", oPostingInfo.sLookupIdContext);
			oPostingJSONobject.put("lookupId", oPostingInfo.sLookupId);
			oPostingJSONobject.put("postingType", oPostingInfo.sPostingType);
			if (oPostingInfo.sPostingString != null)
				oPostingJSONobject.put("postingString", oPostingInfo.sPostingString);
			if(oPostingInfo.bCloseCheck)
				oPostingJSONobject.put("closeCheck", oPostingInfo.bCloseCheck);
			
			if(oPostingInfo.sOtherPmsPayments != null && oPostingInfo.sOtherPmsPayments.size() > 0) {
				JSONArray oOthersPmsPayment = new JSONArray();
				for(HashMap<String, String> oPaymentInfo: oPostingInfo.sOtherPmsPayments) {
					JSONObject oTempPaymentInfo = new JSONObject();
					oTempPaymentInfo.put("paymentSeq", oPaymentInfo.get("paymentSeq"));
					oTempPaymentInfo.put("paymentTotal", oPaymentInfo.get("payTotal"));
					oTempPaymentInfo.put("code", oPaymentInfo.get("code"));
					oTempPaymentInfo.put("roomNumber", oPaymentInfo.get("roomNumber"));
					oTempPaymentInfo.put("guestNumber", oPaymentInfo.get("guestNumber"));
					oTempPaymentInfo.put("subAccountNumber", oPaymentInfo.get("subAccountNumber"));
					oTempPaymentInfo.put("externalType", oPaymentInfo.get("externalType"));
					oTempPaymentInfo.put("postingType", oPaymentInfo.get("postingType"));
					oOthersPmsPayment.put(oTempPaymentInfo);
				}
				oPostingJSONobject.put("otherPMSPayment", oOthersPmsPayment);
			}
			if(oPostingInfo.sPreviousPmsPayments != null && oPostingInfo.sPreviousPmsPayments.size() > 0) {
				JSONArray oPreviousPmsPayment = new JSONArray();
				for(HashMap<String, String> oPaymentInfo: oPostingInfo.sPreviousPmsPayments) {
					JSONObject oTempPaymentInfo = new JSONObject();
					oTempPaymentInfo.put("paymentSeq", oPaymentInfo.get("paymentSeq"));
					oTempPaymentInfo.put("paymentTotal", oPaymentInfo.get("payTotal"));
					oTempPaymentInfo.put("tips", oPaymentInfo.get("tips"));
					oTempPaymentInfo.put("vendorKey", oPaymentInfo.get("vendorKey"));
					oTempPaymentInfo.put("surcharge", oPaymentInfo.get("surcharge"));
					oTempPaymentInfo.put("postingString", oPaymentInfo.get("postingString"));
					oPreviousPmsPayment.put(oTempPaymentInfo);
				}
				oPostingJSONobject.put("previousPmsPayment", oPreviousPmsPayment);
			}
		}catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		
		return oPostingJSONobject;
	}
	
	static boolean isSupportPMS() {
		if(AppGlobal.isModuleSupport(AppGlobal.OPTIONAL_MODULE.pos_interface.name()) == false)
			return false;
		if(AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PMS) == null || AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PMS).isEmpty())
			return false;
		
		return true;
	}
	
	static public boolean checkNeedAskInfo(PosInterfaceConfig oPMSInterfaceConfig) {
		if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_R8)) {
			//R8 PMS
			JSONObject oPaymentInterfaceSetup = oPMSInterfaceConfig.getConfigValue();
			try {
				if(oPaymentInterfaceSetup.getJSONObject("general").getJSONObject("params").getJSONObject("pms_payment_type").getString("value").equals(R8_PAYMENT_TYPE_CASH))
					return false;
				else
					return true;
			}catch(JSONException jsone) {
				jsone.printStackTrace();
			}
		}else if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP) || oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_4700_TCPIP) || oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_4700_SERIAL_PORT)
				|| oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_XMS)) {
			//Standard PMS, 4700 PMS, XMS PMS
			JSONObject oPaymentInterfaceSetup = oPMSInterfaceConfig.getConfigValue();
			Integer iNoAskInfo;
			iNoAskInfo = oPaymentInterfaceSetup.optJSONObject("general").optJSONObject("params").optJSONObject("no_ask_info").optInt("value");
			if(iNoAskInfo == null || iNoAskInfo.intValue() == 0)
				return true;
			else
				return false;
		}else if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_HTNG)) {
			//HTNG (iMagine) PMS
			String  sType= "imagine";
			if(oPMSInterfaceConfig.getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").has("pms_type"))
				sType = oPMSInterfaceConfig.getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").optJSONObject("pms_type").optString("value");
			if(sType != null && (sType.equals("standard") || sType.equals(FuncPMS.PMS_TYPE_SHIJI))) {
				JSONObject oPaymentInterfaceSetup = oPMSInterfaceConfig.getConfigValue();
				Integer iNoAskInfo;
				iNoAskInfo = oPaymentInterfaceSetup.optJSONObject("general").optJSONObject("params").optJSONObject("no_ask_info").optInt("value");
				if(iNoAskInfo == null || iNoAskInfo.intValue() == 0)
					return true;
				else
					return false;
			}
		}else if (oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PEGASUS))
			//Pegasus iPMS
			return true;
		else if (oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN_PMS)) {
			//Aspen PMS
			JSONObject oPaymentInterfaceSetup = oPMSInterfaceConfig.getConfigValue();
			Integer iNoAskInfo = oPaymentInterfaceSetup.optJSONObject("general").optJSONObject("params").optJSONObject("no_ask_info").optInt("value");
			if(iNoAskInfo == null || iNoAskInfo.intValue() == 0)
				return true;
			else
				return false;
		}
		return false;
	}
	
	static public boolean isAllowPostAfterPayment(PosInterfaceConfig oPMSInterfaceConfig) {
		if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP)) {
			//Standard PMS
			JSONObject oPaymentInterfaceSetup = oPMSInterfaceConfig.getConfigValue();
			Integer iAllowPostAfterPayment;
			if (!oPaymentInterfaceSetup.optJSONObject("general").optJSONObject("params").has("allow_posting_after_payment"))
				return false;
			iAllowPostAfterPayment = oPaymentInterfaceSetup.optJSONObject("general").optJSONObject("params").optJSONObject("allow_posting_after_payment").optInt("value", 0);
			if(iAllowPostAfterPayment == null || iAllowPostAfterPayment.intValue() == 0)
				return false;
			else
				return true;
		}
		
		return false;
	}
	
	static public boolean isSurchargeAddOnServiceChargeAmount(PosInterfaceConfig oPMSInterfaceConfig) {
		if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP)) {
			//Standard PMS
			JSONObject oPaymentInterfaceSetup = oPMSInterfaceConfig.getConfigValue();
			Integer iAllowPostAfterPayment;
			if(!oPaymentInterfaceSetup.has("posting_setup") || !oPaymentInterfaceSetup.optJSONObject("posting_setup").has("params"))
				return false;
			if (!oPaymentInterfaceSetup.optJSONObject("posting_setup").optJSONObject("params").has("surcharge_add_on_service_charge_amount"))
				return false;
			iAllowPostAfterPayment = oPaymentInterfaceSetup.optJSONObject("posting_setup").optJSONObject("params").optJSONObject("surcharge_add_on_service_charge_amount").optInt("value", 0);
			if(iAllowPostAfterPayment == null || iAllowPostAfterPayment.intValue() == 0)
				return false;
			else
				return true;
		}
		
		return false;
	}
	
	static public boolean haveDefaultAccount(PosInterfaceConfig oPMSInterfaceConfig) {
		/***No default account setup for R8 PMS***/
		/***2018-02-08 : Remove STANDARD_TCP_IP from checking list ***/
		
		if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_4700_TCPIP) || oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_4700_SERIAL_PORT)
				|| oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_XMS)) {
			JSONObject oPaymentInterfaceSetup = oPMSInterfaceConfig.getConfigValue();
			String sDefaultAccount = oPaymentInterfaceSetup.optJSONObject("general").optJSONObject("params").optJSONObject("default_acc_num").optString("value");
			if(sDefaultAccount == null || sDefaultAccount.isEmpty())
				return false;
			else
				return true;
		}else if (oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_HTNG)){
			String  sType= "imagine";
			if(oPMSInterfaceConfig.getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").has("pms_type"))
				sType = oPMSInterfaceConfig.getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").optJSONObject("pms_type").optString("value");
			if(sType != null && sType.equals("standard")) {
				JSONObject oPaymentInterfaceSetup = oPMSInterfaceConfig.getConfigValue();
				Integer iHaveDefaultAcc = oPaymentInterfaceSetup.optJSONObject("general").optJSONObject("params").optJSONObject("have_default_acc").optInt("value");
				if(iHaveDefaultAcc == 1) {
					String sAccountType = oPaymentInterfaceSetup.optJSONObject("general").optJSONObject("params").optJSONObject("default_unique_type").optString("value");
					String sAccountId = oPaymentInterfaceSetup.optJSONObject("general").optJSONObject("params").optJSONObject("default_unique_id").optString("value");
					String sAccountContext = oPaymentInterfaceSetup.optJSONObject("general").optJSONObject("params").optJSONObject("default_unique_id_context").optString("value");
					
					if(sAccountType != null && !sAccountType.isEmpty() && sAccountId != null && !sAccountId.isEmpty() && sAccountContext != null && !sAccountContext.isEmpty())
						return true;
					else
						return false;
				}else
					return false;
			}
		}
		return true;
	}
	
	// get posting type
	static public String getPostingType(PosInterfaceConfig oPMSInterfaceConfig) {
		String sPaymentType = FuncPMS.POSTING_TYPE_ROOM;
		
		if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_XMS)) {
			JSONObject oPaymentInterfaceSetup = oPMSInterfaceConfig.getConfigValue();
			sPaymentType = oPaymentInterfaceSetup.optJSONObject("general").optJSONObject("params").optJSONObject("posting_type").optString("value");
		}
		
		return sPaymentType;
	}

	static public ArrayList<PosCheckExtraInfo> createPaymentPMSExtraInfoForAPIPortalPostingWithoutDefaultAccount(ArrayList<PosCheckExtraInfo> oCheckPaymentExtraInfos, PosInterfaceConfig oPMSInterfaceConfig,HashMap<String, String> oPaymentInfos){
		if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP)) {
			PosCheckExtraInfo oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_INTERFACE_ID, String.valueOf(oPMSInterfaceConfig.getInterfaceId()));
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			if (oPaymentInfos.containsKey("guestname")) {
				oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_GUEST_NAME, oPaymentInfos.get("guestname"));
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			}
			
			if (oPaymentInfos.containsKey("guestno")) {
				oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_GUEST_NO, oPaymentInfos.get("guestno"));
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			}
			
			if (oPaymentInfos.containsKey("roomno")) {
				oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_ROOM, oPaymentInfos.get("roomno"));
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			}
		}
		return oCheckPaymentExtraInfos;
	}

	
	static public ArrayList<PosCheckExtraInfo> createPaymentPMSExtraInfoForPostingWithDefaultAccount(ArrayList<PosCheckExtraInfo> oCheckPaymentExtraInfos, PosInterfaceConfig oPMSInterfaceConfig, String sTable, String sPaymentCode, int iPaymentSequence) {
		if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_R8)) {
			// Interface id
			PosCheckExtraInfo oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_INTERFACE_ID, String.valueOf(oPMSInterfaceConfig.getInterfaceId()));
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Guest name
			oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_GUEST_NAME, "");
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Guest no.
			oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_GUEST_NO, "");
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Room
			oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_ROOM, "");
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Register no.
			oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_REGISTER_NO, "");
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Guest file no.
			oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_GUEST_FILE_NO, "");
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Expiry date
			oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_EXPIRY_DATE, "0");
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Internal usage
			JSONObject oPMSInfoJSONObject = new JSONObject();
			try {
				oPMSInfoJSONObject.put("p_no", "");	//for 4700 PMS
				oPMSInfoJSONObject.put("table", sTable);
				oPMSInfoJSONObject.put("pm_code", sPaymentCode);
			}catch(JSONException jsone){
				jsone.printStackTrace();
			}
			oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_INTERNAL_USE, oPMSInfoJSONObject.toString());
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
		} else if (oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP)
				|| oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_4700_TCPIP)
				|| oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_4700_SERIAL_PORT)
				|| oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_XMS)
				|| oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN_PMS)) {
			JSONObject setupJSONObject = oPMSInterfaceConfig.getConfigValue();
			
			// Interface id
			PosCheckExtraInfo oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_INTERFACE_ID, String.valueOf(oPMSInterfaceConfig.getInterfaceId()));
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Guest name
			oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_GUEST_NAME, "");
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			if(!oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN_PMS)){
				// Guest no.
				String sValue;
				if (oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP)
						|| oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_XMS))
					sValue = setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_acc_num").optString("value");
				else
					sValue = "";
				oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_GUEST_NO, sValue);
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
				
				// Guest file no.
				oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_GUEST_FILE_NO, "");
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
				
				// Expiry date
				oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_EXPIRY_DATE, setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_expiry_date").optString("value"));
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
				
				// Register no.
				oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_REGISTER_NO, "");
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
				
				// Porfolio Number
				if (setupJSONObject.optJSONObject("general").optJSONObject("params").has("default_selection_line"))
					sValue = setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_selection_line").optString("value");
				else
					sValue = "";
				oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_FIELD_NO, sValue);
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			}
			
			// Room
			String sValue;
			if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP) || oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_XMS))
				sValue = "";
			else if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN_PMS)){
				String sAccountNumber = "";
				JSONObject oPaymentInterfaceSetup = oPMSInterfaceConfig.getConfigValue();
				Integer iHaveDefaultAcc = oPaymentInterfaceSetup.optJSONObject("general").optJSONObject("params").optJSONObject("have_default_acc").optInt("value");
				if(iHaveDefaultAcc == 1) 
					sAccountNumber = oPaymentInterfaceSetup.optJSONObject("general").optJSONObject("params").optJSONObject("default_acc_num").optString("value");
				sValue = sAccountNumber;
			}else
				sValue = setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_acc_num").optString("value");
			oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_ROOM, sValue);
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Sub account number
			if(setupJSONObject.optJSONObject("general").optJSONObject("params").has("default_sub_acc_num")) {
				oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_SUB_ACCOUNT_NUMBER, setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_sub_acc_num").optString("value"));
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			}
			
			// Posting Key
			if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_XMS) 
					|| (oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP) && FuncPMS.isAllowPostAfterPayment(oPMSInterfaceConfig))) {
				DateTime oCurrentTime = AppGlobal.getCurrentTime(false);
				String sPostingKey = "";
				if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_XMS)) {
					DateTimeFormatter formatter2 = DateTimeFormat.forPattern("yyMMddHHmmss");
					sPostingKey = formatter2.print(oCurrentTime) + AppGlobal.g_oFuncStation.get().getCode();
				}else if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP) && FuncPMS.isAllowPostAfterPayment(oPMSInterfaceConfig)) {
					DateTimeFormatter formatter2 = DateTimeFormat.forPattern("yyyyMMdd_HHmmss");
					sPostingKey = formatter2.print(oCurrentTime) + "_" + AppGlobal.g_oFuncStation.get().getCode() + "_" + String.valueOf(iPaymentSequence);
				}
				oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_POSTING_KEY, sPostingKey);
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			}
			
			// Posting status
			if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP) && FuncPMS.isAllowPostAfterPayment(oPMSInterfaceConfig)) {
				oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_POST_STATUS, "false");
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			}
			
			// Mode
			if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP) && FuncPMS.isAllowPostAfterPayment(oPMSInterfaceConfig)) {
				oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_MODE, FuncPMS.POSTING_MODE_PERIODIC);
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			}
			
			// Internal usage
			JSONObject oPMSInfoJSONObject = new JSONObject();
			try {
				if(setupJSONObject.optJSONObject("general").optJSONObject("params").has("posting_type"))
					oPMSInfoJSONObject.put("posting_type", FuncPMS.getPostingType(oPMSInterfaceConfig));
				if(setupJSONObject.optJSONObject("general").optJSONObject("params").has("xms_pay_type"))
					oPMSInfoJSONObject.put("external_type", setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("xms_pay_type").optString("value"));
				oPMSInfoJSONObject.put("table", sTable);
				oPMSInfoJSONObject.put("pm_code", sPaymentCode);
			}catch(JSONException jsone){
				jsone.printStackTrace();
			}
			oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_INTERNAL_USE, oPMSInfoJSONObject.toString());
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
		}else if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_HTNG)) {
			String  sType= "imagine";
			if(oPMSInterfaceConfig.getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").has("pms_type"))
				sType =  oPMSInterfaceConfig.getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").optJSONObject("pms_type").optString("value");
			if(sType != null && (sType.equals("standard") || sType.equals(FuncPMS.PMS_TYPE_SHIJI))) {
				JSONObject setupJSONObject = oPMSInterfaceConfig.getConfigValue();
				
				// Interface id
				PosCheckExtraInfo oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_INTERFACE_ID, String.valueOf(oPMSInterfaceConfig.getInterfaceId()));
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
				
				// Default unique type
				oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_LOOKUP_TYPE, setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_unique_type").optString("value"));
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
				
				// Default unique id
				oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_LOOKUP_ID, setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_unique_id").optString("value"));
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
				
				// Default unique id context
				oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_LOOKUP_ID_CONTEXT, setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_unique_id_context").optString("value"));
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
				
				// Internal usage
				JSONObject oPMSInfoJSONObject = new JSONObject();
				try {
					oPMSInfoJSONObject.put("table", sTable);
					oPMSInfoJSONObject.put("pm_code", sPaymentCode);
				}catch(JSONException jsone){
					jsone.printStackTrace();
				}
				oPosCheckExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.VARIABLE_INTERNAL_USE, oPMSInfoJSONObject.toString());
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			}
		}
		
		return oCheckPaymentExtraInfos;
	}
	
	static public void updatePaymentPMSExtraInfoForPostingWithDefaultAccount(PosPaymentMethod oPosPaymentMethod, ArrayList<PosCheckExtraInfo> oCheckPaymentExtraInfos) {
		if(oPosPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS) == null || oPosPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).size() == 0)
			return;
		PosInterfaceConfig oPMSInterfaceConfig = oPosPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).get(0);
		
		if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_R8)) {
			for (PosCheckExtraInfo oPosCheckExtraInfo : oCheckPaymentExtraInfos) {
				if (oPosCheckExtraInfo.getBy().equals(PosCheckExtraInfo.BY_PAYMENT)
						&& oPosCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_PMS)) {
					// Interface id
					if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERFACE_ID))
						oPosCheckExtraInfo.setValue(String.valueOf(oPMSInterfaceConfig.getInterfaceId()));
					
					// Internal usage
					if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERNAL_USE)) {
						try {
							JSONObject oPMSInfoJSONObject = new JSONObject(oPosCheckExtraInfo.getValue());
							oPMSInfoJSONObject.put("pm_code", oPosPaymentMethod.getPaymentCode());
							oPosCheckExtraInfo.setValue(oPMSInfoJSONObject.toString());
						}catch(JSONException jsone){
							jsone.printStackTrace();
						}
					}
				}
			}
		} else if (oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP)
				|| oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_4700_TCPIP)
				|| oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_4700_SERIAL_PORT)
				|| oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_XMS)) {
			
			JSONObject setupJSONObject = oPMSInterfaceConfig.getConfigValue();
			
			for (PosCheckExtraInfo oPosCheckExtraInfo : oCheckPaymentExtraInfos) {
				if (oPosCheckExtraInfo.getBy().equals(PosCheckExtraInfo.BY_PAYMENT)
						&& oPosCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_PMS)) {
					// Interface id
					if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERFACE_ID))
						oPosCheckExtraInfo.setValue(String.valueOf(oPMSInterfaceConfig.getInterfaceId()));
					
					// Guest no.
					if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_GUEST_NO)) {
						if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP) || oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_XMS))
							oPosCheckExtraInfo.setValue(setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_acc_num").optString("value"));
						else
							oPosCheckExtraInfo.setValue("");
					}
					
					// Room
					if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_ROOM)) {
						if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP) || oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_XMS))
							oPosCheckExtraInfo.setValue("");
						else
							oPosCheckExtraInfo.setValue(setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_acc_num").optString("value"));
					}
					
					// Sub account number
					if(setupJSONObject.optJSONObject("general").optJSONObject("params").has("default_sub_acc_num")) {
						if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SUB_ACCOUNT_NUMBER))
							oPosCheckExtraInfo.setValue(setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_sub_acc_num").optString("value"));
					}
					
					// Expiry date
					if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_EXPIRY_DATE))
						if(setupJSONObject.optJSONObject("general").optJSONObject("params").has("default_expiry_date"))
							oPosCheckExtraInfo.setValue(setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_expiry_date").optString("value"));
					
					// Profolio Number
					if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_FIELD_NO)) {
						if(setupJSONObject.optJSONObject("general").optJSONObject("params").has("default_selection_line"))
							oPosCheckExtraInfo.setValue(setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_selection_line").optString("value"));
						else
							oPosCheckExtraInfo.setValue("");
					}
					
					// Internal usage
					if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERNAL_USE)) {
						try {
							JSONObject oPMSInfoJSONObject = new JSONObject(oPosCheckExtraInfo.getValue());
							oPMSInfoJSONObject.put("pm_code", oPosPaymentMethod.getPaymentCode());
							oPosCheckExtraInfo.setValue(oPMSInfoJSONObject.toString());
						}catch(JSONException jsone){
							jsone.printStackTrace();
						}
					}
				}
			}
		}else if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_HTNG)) {
			String  sType= "imagine";
			if(oPMSInterfaceConfig.getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").has("pms_type"))
				sType =  oPMSInterfaceConfig.getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").optJSONObject("pms_type").optString("value");
			if(sType != null && (sType.equals("standard") || sType.equals(FuncPMS.PMS_TYPE_SHIJI))) {
				for (PosCheckExtraInfo oPosCheckExtraInfo : oCheckPaymentExtraInfos) {
					if (oPosCheckExtraInfo.getBy().equals(PosCheckExtraInfo.BY_PAYMENT)
							&& oPosCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_PMS)) {
						// Interface id
						if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERFACE_ID))
							oPosCheckExtraInfo.setValue(String.valueOf(oPMSInterfaceConfig.getInterfaceId()));
						
						// Internal usage
						if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERNAL_USE)) {
							try {
								JSONObject oPMSInfoJSONObject = new JSONObject(oPosCheckExtraInfo.getValue());
								oPMSInfoJSONObject.put("pm_code", oPosPaymentMethod.getPaymentCode());
								oPosCheckExtraInfo.setValue(oPMSInfoJSONObject.toString());
							}catch(JSONException jsone){
								jsone.printStackTrace();
							}
						}
					}
				}
			}
		}else if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN_PMS)) {
			JSONObject setupJSONObject = oPMSInterfaceConfig.getConfigValue();
			for (PosCheckExtraInfo oPosCheckExtraInfo : oCheckPaymentExtraInfos) {
				if (oPosCheckExtraInfo.getBy().equals(PosCheckExtraInfo.BY_PAYMENT)
						&& oPosCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_PMS)) {
					// Interface id
					if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERFACE_ID))
						oPosCheckExtraInfo.setValue(String.valueOf(oPMSInterfaceConfig.getInterfaceId()));
					
					// Room
					if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_ROOM))
						oPosCheckExtraInfo.setValue(setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_acc_num").optString("value"));
					
					// Guest name
					if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_GUEST_NAME))
						oPosCheckExtraInfo.setValue("");
					
					// Internal usage
					if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERNAL_USE)) {
						try {
							JSONObject oPMSInfoJSONObject = new JSONObject(oPosCheckExtraInfo.getValue());
							oPMSInfoJSONObject.put("pm_code", oPosPaymentMethod.getPaymentCode());
							oPosCheckExtraInfo.setValue(oPMSInfoJSONObject.toString());
						}catch(JSONException jsone){
							jsone.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	public boolean checkNoPosting(String sInterfaceVendorKey, JSONObject oInterfaceSetup, JSONObject oPaymentPmsSetup, HashMap<String, String> oChosenGuestInfo) {
		if(sInterfaceVendorKey.equals(InfVendor.KEY_R8)) {
			try {
				if(!oPaymentPmsSetup.getJSONObject("general").getJSONObject("params").getJSONObject("pms_payment_type").getString("value").equals(FuncPMS.R8_PAYMENT_TYPE_CASH)) {
					// checking variable NP - no post
					if(oInterfaceSetup.getJSONObject("pms_setting").getJSONObject("params").getJSONObject("check_return_value_np").getString("value").equals("1") && oChosenGuestInfo.get("noPost").equals("1")) { 
						return false;
					}
				}
				
			}catch (JSONException jsone) {
				jsone.printStackTrace();
			}
		}
		
		if(sInterfaceVendorKey.equals(InfVendor.KEY_HTNG)) {
			if (oChosenGuestInfo.get("noPost").equals("true"))
				return false;
		}
		
		return true;
	}
	
	public boolean checkCreditLimit(String sInterfaceVendorKey, JSONObject oInterfaceSetup, JSONObject oPaymentPmsSetup) {
		if(sInterfaceVendorKey.equals(InfVendor.KEY_R8)) {
			try {
				if(!oPaymentPmsSetup.getJSONObject("general").getJSONObject("params").getJSONObject("pms_payment_type").getString("value").equals(FuncPMS.R8_PAYMENT_TYPE_CASH)) {
					// checking variable CL
					if(oInterfaceSetup.getJSONObject("pms_setting").getJSONObject("params").getJSONObject("check_return_value_cl").getString("value").equals("1")) { 
						return true;
					}
				}
				
			}catch (JSONException jsone) {
				jsone.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	//check the target PMS interface whether need to do single posting evening have multiple PMS payment
	public boolean isSinglePostingForMultiplePayments(int iInterfaceId) {
		String sInterfaceVendor = "";
		//get interface setup
		for(PosInterfaceConfig oPosInterfaceConfig:AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PMS)){
			if(oPosInterfaceConfig.getInterfaceId() == iInterfaceId){
				sInterfaceVendor = oPosInterfaceConfig.getInterfaceVendorKey();
				break;
			}
		}
		
		if(sInterfaceVendor.equals(InfVendor.KEY_XMS))
			return true;
		else
			return false;
	}
	
	//set the corresponding extra info to payment
	public PostingInfo setPostingInfoFromExtraInfo(PostingInfo oPostingInfo, PosCheckPayment oCheckPayment) {
		for(PosCheckExtraInfo oPosCheckExtraInfo:oCheckPayment.getCheckExtraInfoArrayList()){
			if(oPosCheckExtraInfo.getBy().equals(PosCheckExtraInfo.BY_PAYMENT) &&
					oPosCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_PMS)){
				
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERFACE_ID)){
					oPostingInfo.iInterfaceId = Integer.parseInt(oPosCheckExtraInfo.getValue());
					
					for(PosInterfaceConfig oPosInterfaceConfig:AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PMS)){
						if(oPosInterfaceConfig.getInterfaceId() == oPostingInfo.iInterfaceId){
							oPostingInfo.sInterfaceVendorKey = oPosInterfaceConfig.getInterfaceVendorKey();
							break;
						}
					}
				} else if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_ROOM)) {
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sRoomNumber = oPosCheckExtraInfo.getValue();
					}
				} else if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_GUEST_NO)) {
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sGuestNumber = oPosCheckExtraInfo.getValue();
					}
				} else if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_GUEST_NAME)) {
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sGuestName = oPosCheckExtraInfo.getValue();
					}
				} else if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_EXPIRY_DATE)) {
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sExpiryDate = oPosCheckExtraInfo.getValue();
					}
				} else if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_FIELD_NO)) {
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sFieldNumber = oPosCheckExtraInfo.getValue();
					}
				} else if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_REGISTER_NO)) {
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sRegisterNumber = oPosCheckExtraInfo.getValue();
					}
				} else if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_GUEST_FILE_NO)) {
					String sGuestFileNo = oPosCheckExtraInfo.getValue();
					
					if(sGuestFileNo == null || sGuestFileNo.equals(""))
						oPostingInfo.sGuestFileNumber = "0";
					else
						oPostingInfo.sGuestFileNumber = sGuestFileNo;
				} else if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERNAL_USE)) {
					try{
						JSONObject oPMSInfoJsonObject = new JSONObject(oPosCheckExtraInfo.getValue());
						if(oPMSInfoJsonObject.has("table")){
							oPostingInfo.sTable = oPMSInfoJsonObject.optString("table", "");
						}
						if(oPMSInfoJsonObject.has("pm_code")){
							oPostingInfo.sPaymentType = oPMSInfoJsonObject.optString("pm_code", "");
						}
					}catch (JSONException jsone) {
						jsone.printStackTrace();
					}
				} else if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_TRACE_ID)) {
					if(oPosCheckExtraInfo.getValue() != null)
						oPostingInfo.sTraceId = oPosCheckExtraInfo.getValue();
				} else if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_POSTING_KEY)) {
					if(oPosCheckExtraInfo.getValue() != null)
						oPostingInfo.sPostingKey = oPosCheckExtraInfo.getValue();
				} else if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_PAYTYPE)) {
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sPaymentType = oPosCheckExtraInfo.getValue();
					}
				} else if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_LOOKUP_TYPE)) {
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sLookupType = oPosCheckExtraInfo.getValue();
					}
				} else if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_LOOKUP_ID)) {
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sLookupId = oPosCheckExtraInfo.getValue();
					}
				} else if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_LOOKUP_ID_CONTEXT)) {
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sLookupIdContext = oPosCheckExtraInfo.getValue();
					}
				} else if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_POSTING_STRING)) {
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sPostingString = oPosCheckExtraInfo.getValue();
					}
				}
			}
		}
		return oPostingInfo;
	}
	
	private String getAccountTypeName(int iAccountType) {
		String sAccountTypeName = "";
		switch (iAccountType) {
			case 1:
				sAccountTypeName = AppGlobal.g_oLang.get()._("group_main_account");
				break;
			case 2:
				sAccountTypeName = AppGlobal.g_oLang.get()._("guest_main_account");
				break;
			case 3:
				sAccountTypeName = AppGlobal.g_oLang.get()._("shared_second_account");
				break;
			case 4:
				sAccountTypeName = AppGlobal.g_oLang.get()._("work_account");
				break;
			default:
				sAccountTypeName = AppGlobal.g_oLang.get()._("group_main_account");
				break;
		}
		return sAccountTypeName;
	}
	
	private String getErrorMessage(int iErrorCode) {
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
			sErrorMessage = AppGlobal.g_oLang.get()._("empty_response_from_pms");
			break;
		case 10:
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_response_from_pms");
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
		default:	
		case 0:
			sErrorMessage = null;
			break;
		}
		
		return sErrorMessage;
	}
	
	public String getLastErrorMessage() {
		return this.m_sLastErrorMessage;
	}
	
	public int getPMSPostCount(FuncCheck oFuncCheck, String sPostingType, BigDecimal sPayTotal, boolean bVoid) {
		int iPaymentCounter = 0;
		JSONArray oPayInfoJsonArray = new JSONArray();
		// check whether this pms payment is paid before, if paid then add the
		// key by 1 each time matched
		if (oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_PMS,
				PosCheckExtraInfo.VARIABLE_PAYMENT_INFO, 0)
				&& oFuncCheck.getCheckExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_PMS,
						PosCheckExtraInfo.VARIABLE_PAYMENT_INFO, 0) != null
				&& !oFuncCheck.getCheckExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_PMS,
						PosCheckExtraInfo.VARIABLE_PAYMENT_INFO, 0).isEmpty()) {
			try {
				oPayInfoJsonArray = new JSONArray(oFuncCheck.getCheckExtraInfoBySectionAndVariable(
						PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_PAYMENT_INFO, 0));
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}

		for (int i = 0; i < oPayInfoJsonArray.length(); i++) {
			try {
				if (oPayInfoJsonArray.get(i) != null) {
					JSONObject oJsonObject = new JSONObject(oPayInfoJsonArray.get(i).toString());
					if (oJsonObject.has("payType") && oJsonObject.has("payTotal")
							&& sPayTotal.compareTo(new BigDecimal(oJsonObject.optString("payTotal"))) == 0) {
						if (sPostingType.equals(oJsonObject.get("payType")))
							iPaymentCounter++;
					}
				}
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		if (!bVoid) {
			// save the current payment to extra info for match next time
			JSONObject oJsonObject = new JSONObject();
			try {
				oJsonObject.put("payTotal", sPayTotal.toPlainString());
				oJsonObject.put("payType", sPostingType);

				oPayInfoJsonArray.put(oJsonObject);
				if (oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_PMS,
						PosCheckExtraInfo.VARIABLE_PAYMENT_INFO, 0))
					oFuncCheck.updateCheckExtraInfoValue(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PMS,
							PosCheckExtraInfo.VARIABLE_PAYMENT_INFO, 0, oPayInfoJsonArray.toString());
				else
					oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PMS,
							PosCheckExtraInfo.VARIABLE_PAYMENT_INFO, 0, oPayInfoJsonArray.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return iPaymentCounter;
	}
	
	static private PosCheckExtraInfo constructCheckExtraInfo(String sVariable, String sValue) {
		PosCheckExtraInfo oPosCheckExtraInfo = new PosCheckExtraInfo();
		oPosCheckExtraInfo.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
		oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
		oPosCheckExtraInfo.setVariable(sVariable);
		oPosCheckExtraInfo.setValue(sValue);
		return oPosCheckExtraInfo;
	}
}