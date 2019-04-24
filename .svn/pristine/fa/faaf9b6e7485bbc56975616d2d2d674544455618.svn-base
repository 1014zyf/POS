package app.controller;

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

import app.model.InfInterface;
import app.model.InfVendor;
import app.model.PosCheckExtraInfo;
import app.model.PosCheckPayment;
import app.model.PosInterfaceConfig;
import app.model.PosPaymentMethod;

public class FuncPMS {
	class PostingInfo {
		int iInterfaceId;
		String sInterfaceVendorKey;
		int iCheckPaymnetIndex;
		String sOutletCode;
		String sWorkStation;
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
		String sPaymentType;
		BigDecimal dPaymentAmount;
		BigDecimal dTips;
		BigDecimal dPostAmount;
		BigDecimal dPreviousPaymentAmount;
		String sCardNumber;
		String sClearText;
		String sGuestFileNumber;
		String sDate;
		String sTime;
	}
	
	public HashMap<String, String> m_oEnquiryInfo;
	public boolean m_bEnquiryResult;
	public String m_sLastErrorMessage;
	
	static private int POSTING_TAX_COUNT = 16;
	
	public static String ERROR_TYPE_CONNECTION = "i";
	public static String ERROR_TYPE_PMS = "r";
	
	static public String ENQUIRY_TYPE_ROOM = "F";
	static public String ENQUIRY_TYPE_AR = "A";
	static public String ENQUIRY_TYPE_REGISTER = "";
	
	static public String R8_PAYMENT_TYPE_CASH = "cash_settlement";
	static public String R8_PAYMENT_TYPE_ROOM = "room_settlement";
	static public String R8_PAYMENT_TYPE_TARGET_PAYMENT = "taget_payment_settlement";
	
	static public String TYPE_PAY_PMS = "pay_pms";
	static public String TYPE_VOID_PMS = "void_pms";
	
	public FuncPMS() {
		m_oEnquiryInfo = new HashMap<String, String>();
		m_bEnquiryResult = false;
		m_sLastErrorMessage = "";
	}
	
	private void setEnquiryInfo(int iInterfaceId, HashMap<String, String> oEnquiryInformation, String sCheckNumber, String sPaymentCode) {
		m_oEnquiryInfo.put("interfaceId", String.valueOf(iInterfaceId));
		m_oEnquiryInfo.put("outlet", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		m_oEnquiryInfo.put("workStation", AppGlobal.g_oFuncStation.get().getCode());
		m_oEnquiryInfo.put("employee", AppGlobal.g_oFuncUser.get().getUserNumber());
		if(oEnquiryInformation != null && oEnquiryInformation.containsKey("enquiryNumber"))
			m_oEnquiryInfo.put("enquiryNumber", oEnquiryInformation.get("enquiryNumber"));
		else
			m_oEnquiryInfo.put("enquiryNumber", "");
		if(oEnquiryInformation != null && oEnquiryInformation.containsKey("registerNumber"))
			m_oEnquiryInfo.put("register", oEnquiryInformation.get("registerNumber"));
		else
			m_oEnquiryInfo.put("register", "");
		if(oEnquiryInformation != null && oEnquiryInformation.containsKey("enquiryType"))
			m_oEnquiryInfo.put("enquiryType", oEnquiryInformation.get("enquiryType"));
		else
			m_oEnquiryInfo.put("enquiryType", "");
		if(sPaymentCode != null)
			m_oEnquiryInfo.put("paymentType", sPaymentCode);
		m_oEnquiryInfo.put("checkNumber", sCheckNumber);
		if(oEnquiryInformation.containsKey("postingAskInfo"))
			m_oEnquiryInfo.put("postingAskInfo", "1");
	}
	
	// pms enquiry
	public List<HashMap<String, String>> pmsEnquiry(int iInterfaceId, HashMap<String, String> oEnquiryInformation, String sCheckNumber, String sPaymentCode) {
		InfInterface oPMSInterface = new InfInterface();
		PosInterfaceConfig oPMSInterfaceConfig = null;
		String sInterfaceVendor = "";
		JSONObject enquiryResultJSONObject = null;
		List<HashMap<String, String>> oResultGuests = new ArrayList<HashMap<String, String>>();
		
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
		
		if (enquiryResultJSONObject == null)
			return null;
		
		try {
			if (enquiryResultJSONObject.has("enquiryResult") && enquiryResultJSONObject.getBoolean("enquiryResult") == false) {
				if(enquiryResultJSONObject.optInt("errorCode") != 0)
					m_sLastErrorMessage = getErrorMessage(enquiryResultJSONObject.optInt("errorCode"));
				else
					m_sLastErrorMessage = enquiryResultJSONObject.optString("errorMessage");
				return null;
			}else if(enquiryResultJSONObject.has("enquiryResult") && enquiryResultJSONObject.optBoolean("enquiryResult") == true) {
				JSONArray oGuestList = new JSONArray();
				
				if(enquiryResultJSONObject.isNull("guests"))
					return oResultGuests;
				
				oGuestList = enquiryResultJSONObject.getJSONArray("guests");
				for(int i=0; i<oGuestList.length(); i++) {
					HashMap<String, String> oGuest = new HashMap<String, String>();
					
					oGuest.put("guestNumber", oGuestList.getJSONObject(i).optString("guestNumber"));
					oGuest.put("guestName", oGuestList.getJSONObject(i).optString("guestName"));
					oGuest.put("roomNumber", oGuestList.getJSONObject(i).optString("roomNumber"));
					oGuest.put("arrivalDate", oGuestList.getJSONObject(i).optString("arrivalDate"));
					oGuest.put("departureDate", oGuestList.getJSONObject(i).optString("departureDate"));
					oGuest.put("guestFirstName", oGuestList.getJSONObject(i).optString("guestFirstName"));
					oGuest.put("guestLanguage", oGuestList.getJSONObject(i).optString("guestLanguage"));
					oGuest.put("guestGroupNumber", oGuestList.getJSONObject(i).optString("guestGroupNumber"));
					oGuest.put("guestTitle", oGuestList.getJSONObject(i).optString("guestTitle"));
					oGuest.put("guestVip", oGuestList.getJSONObject(i).optString("guestVip"));
					if(oGuestList.getJSONObject(i).has("balanceAmount"))
						oGuest.put("balanceAmount", new BigDecimal(oGuestList.getJSONObject(i).getDouble("balanceAmount")).toPlainString());
					else
						oGuest.put("balanceAmount", BigDecimal.ZERO.toPlainString());
					if(oGuestList.getJSONObject(i).has("creditLimit"))
						oGuest.put("creditLimit", new BigDecimal(oGuestList.getJSONObject(i).getDouble("creditLimit")).toPlainString());
					else
						oGuest.put("creditLimit", BigDecimal.ZERO.toPlainString());
					for(int j=0; j<oGuestList.getJSONObject(i).getJSONArray("userInfo").length(); j++) {
						if(oGuestList.getJSONObject(i).getJSONArray("userInfo").getJSONObject(j).has("index"))
							oGuest.put("info"+oGuestList.getJSONObject(i).getJSONArray("userInfo").getJSONObject(j).getInt("index"), oGuestList.getJSONObject(i).getJSONArray("userInfo").optJSONObject(j).getString("info"));
					}
					oGuest.put("registerNumber", oGuestList.getJSONObject(i).optString("registerNumber"));
					if(oGuestList.getJSONObject(i).has("guestFileNumber"))
						oGuest.put("guestFileNumber", String.valueOf(oGuestList.getJSONObject(i).getInt("guestFileNumber")));
					else
						oGuest.put("guestFileNumber", "0");
					if(oGuestList.getJSONObject(i).optBoolean("noPost") == true)
						oGuest.put("noPost", AppGlobal.g_oLang.get()._("allow"));
					else
						oGuest.put("noPost", AppGlobal.g_oLang.get()._("not_allow"));
					oGuest.put("targetPaymentMethod", oGuestList.getJSONObject(i).optString("targetPaymentMethod"));
					oGuest.put("line", oGuestList.getJSONObject(i).optString("line"));
					
					oResultGuests.add(oGuest);
				}
			}
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return oResultGuests;
	}
	
	// pms posting
	public boolean pmsPosting(FuncCheck oFuncCheck, PosCheckPayment oCheckPayment, PosPaymentMethod oPaymentMethod, BigDecimal dPreviousPaymentTotal) {
		int i=0, iCurrentPostingIndex = 0;
		boolean bFailToPost = false;
		List<PostingInfo> oPostingInfoList = new ArrayList<PostingInfo>();
		DateTime today = new DateTime();
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm:ss");
		String sCurrentTime = timeFmt.print(today);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String sCurrentDate = dateFmt.print(today);
		
		// build the pms posting list
		if(!oCheckPayment.havePmsPayment())
			return true;
		
		PostingInfo oPostingInfo = new PostingInfo();

		oPostingInfo.iCheckPaymnetIndex = i;
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.sWorkStation = AppGlobal.g_oFuncStation.get().getCode();
		oPostingInfo.sEmployee = AppGlobal.g_oFuncUser.get().getUserNumber();
		oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
		oPostingInfo.sCheckGroupNumber = "";
		oPostingInfo.iCover = oFuncCheck.getCover();
		if(oFuncCheck.getCheckBusinessPeriodId() == 0)
			oPostingInfo.sServingPeriod = AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getCode();
		else
			oPostingInfo.sServingPeriod = AppGlobal.g_oFuncOutlet.get().getBusinessPeriodById(oFuncCheck.getCheckBusinessPeriodId()).getCode();
		oPostingInfo.dPaymentAmount = oCheckPayment.getPayTotal();
		oPostingInfo.dTips = oCheckPayment.getPayTips();
		oPostingInfo.dPostAmount = oCheckPayment.getPayTotal().add(oCheckPayment.getPayTips());
		oPostingInfo.dPreviousPaymentAmount = dPreviousPaymentTotal;
		oPostingInfo.sCardNumber = "";
		oPostingInfo.sClearText = "";
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
				}else
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_ROOM)){
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sRoomNumber = oPosCheckExtraInfo.getValue();
					}
				}else
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_GUEST_NO)){
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sGuestNumber = oPosCheckExtraInfo.getValue();
					}
				}else
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_GUEST_NAME)){
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sGuestName = oPosCheckExtraInfo.getValue();
					}
				}else
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_EXPIRY_DATE)){
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sExpiryDate = oPosCheckExtraInfo.getValue();
					}
				}else
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_FIELD_NO)){
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sFieldNumber = oPosCheckExtraInfo.getValue();
					}
				}else
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_REGISTER_NO)){
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sRegisterNumber = oPosCheckExtraInfo.getValue();
					}
				}else
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_GUEST_FILE_NO)){
					String sGuestFileNo = oPosCheckExtraInfo.getValue();
					
					if(sGuestFileNo == null || sGuestFileNo.equals(""))
						oPostingInfo.sGuestFileNumber = "0";
					else
						oPostingInfo.sGuestFileNumber = sGuestFileNo;
				}else
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERNAL_USE)){
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
				}
			}
		}
		
		oPostingInfoList.add(oPostingInfo);

/*System.out.println("DEBUG PMS POST ======================= " + oPostingInfo.iInterfaceId + ", " +
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
		if(oPostingInfoList.size() == 0)
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
			if(oPostingInfoList.get(i).sInterfaceVendorKey.equals(InfVendor.KEY_4700_SERIAL_PORT)) {
				//get interface setup
				PosInterfaceConfig oPMSInterfaceConfig = null;
				for(PosInterfaceConfig oPosInterfaceConfig:AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PMS)){
					if(oPosInterfaceConfig.getInterfaceId() == oPostingInfoList.get(i).iInterfaceId){
						oPMSInterfaceConfig = oPosInterfaceConfig;
						break;
					}
				}
				
				FuncPMS4700SerialPort oFuncPMS4700SerialPort = new FuncPMS4700SerialPort();
				oFuncPMS4700SerialPort.init(oPMSInterfaceConfig);
				bResult = oFuncPMS4700SerialPort.doPosting(oFuncCheck, oCheckPayment, fromPmsPostingJSONObject(oPostingInfoList.get(i)), false);
					
			}else 
				bResult = oInterfaceConfig.doPmsPosting(oCheckInformationJSONObject, fromPmsPostingJSONObject(oPostingInfoList.get(i)));
			
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
	
	// pms void posting
	public boolean pmsVoidPosting(FuncCheck oFuncCheck, PosCheckPayment oCheckPayment, PosPaymentMethod oPaymentMethod, BigDecimal dPreviousPaymentTotal) {
		int i=0;
		boolean bFailToPost = false;
		List<PostingInfo> oPostingInfoList = new ArrayList<PostingInfo>();
		DateTime today = new DateTime();
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm:ss");
		String sCurrentTime = timeFmt.print(today);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String sCurrentDate = dateFmt.print(today);
		
		// build the pms posting list
		if(!oCheckPayment.havePmsPayment())
			return true;
		
		PostingInfo oPostingInfo = new PostingInfo();

		oPostingInfo.iCheckPaymnetIndex = i;
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.sWorkStation = AppGlobal.g_oFuncStation.get().getCode();
		oPostingInfo.sEmployee = AppGlobal.g_oFuncUser.get().getUserNumber();
		oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
		oPostingInfo.sCheckGroupNumber = "";
		oPostingInfo.iCover = oFuncCheck.getCover();
		if(oFuncCheck.getCheckBusinessPeriodId() == 0)
			oPostingInfo.sServingPeriod = AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getCode();
		else
			oPostingInfo.sServingPeriod = AppGlobal.g_oFuncOutlet.get().getBusinessPeriodById(oFuncCheck.getCheckBusinessPeriodId()).getCode();
		oPostingInfo.dPaymentAmount = oCheckPayment.getPayTotal();
		oPostingInfo.dTips = oCheckPayment.getPayTips();
		oPostingInfo.dPostAmount = oCheckPayment.getPayTotal().add(oCheckPayment.getPayTips());
		oPostingInfo.dPreviousPaymentAmount = dPreviousPaymentTotal;
		oPostingInfo.sCardNumber = "";
		oPostingInfo.sClearText = "";
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
				}else
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_ROOM)){
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sRoomNumber = oPosCheckExtraInfo.getValue();
					}
				}else
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_GUEST_NO)){
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sGuestNumber = oPosCheckExtraInfo.getValue();
					}
				}else
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_GUEST_NAME)){
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sGuestName = oPosCheckExtraInfo.getValue();
					}
				}else
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_EXPIRY_DATE)){
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sExpiryDate = oPosCheckExtraInfo.getValue();
					}
				}else
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_FIELD_NO)){
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sFieldNumber = oPosCheckExtraInfo.getValue();
					}
				}else
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_REGISTER_NO)){
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sRegisterNumber = oPosCheckExtraInfo.getValue();
					}
				}else
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_GUEST_FILE_NO)){
					String sGuestFileNo = oPosCheckExtraInfo.getValue();
					
					if(sGuestFileNo == null || sGuestFileNo.equals(""))
						oPostingInfo.sGuestFileNumber = "0";
					else
						oPostingInfo.sGuestFileNumber = sGuestFileNo;
				}else
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERNAL_USE)){
					try{
						JSONObject oPMSInfoJsonObject = new JSONObject(oPosCheckExtraInfo.getValue());
						if(oPMSInfoJsonObject.has("table")){
							oPostingInfo.sTable = oPMSInfoJsonObject.optString("table");
						}
						if(oPMSInfoJsonObject.has("pm_code")){
							oPostingInfo.sPaymentType = oPMSInfoJsonObject.optString("pm_code");
						}
					}catch (JSONException jsone) {
						jsone.printStackTrace();
					}
				}
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
		if(oPostingInfoList.size() == 0)
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
			if(oPostingInfoList.get(i).sInterfaceVendorKey.equals(InfVendor.KEY_4700_SERIAL_PORT)) {
				PosInterfaceConfig oPMSInterfaceConfig = null;
				
				//get interface setup
				for(PosInterfaceConfig oPosInterfaceConfig:AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PMS)){
					if(oPosInterfaceConfig.getInterfaceId() == oPostingInfo.iInterfaceId){
						oPMSInterfaceConfig = oPosInterfaceConfig;
						break;
					}
				}
				
				FuncPMS4700SerialPort oFuncPMS4700SerialPort = new FuncPMS4700SerialPort();
				oFuncPMS4700SerialPort.init(oPMSInterfaceConfig);
				boolean bResult = oFuncPMS4700SerialPort.doPosting(oFuncCheck, oCheckPayment, fromPmsPostingJSONObject(oPostingInfoList.get(i)), true);
				if(bResult == false)
					bFailToPost = true;
			}else {
				JSONObject oCheckInformationJSONObject = oFuncCheck.contrustPmsPostingCheckInformation(oCheckPayment);
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
				
				if(!bNoPrepost && oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS) != null && oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).size() > 0) {
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
							if(oInterfaceConfig.getLastSuccessReulst().has("guestName") && !oInterfaceConfig.getLastSuccessReulst().optString("guestName", "").isEmpty())
								oPostingInfo.sGuestName = oInterfaceConfig.getLastSuccessReulst().optString("guestName");
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
			oPostingJSONobject.put("workstation", oPostingInfo.sWorkStation);
			oPostingJSONobject.put("employee", oPostingInfo.sEmployee);
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
			oPostingJSONobject.put("paymentType", oPostingInfo.sPaymentType);
			oPostingJSONobject.put("paymentAmount", oPostingInfo.dPaymentAmount.toPlainString());
			oPostingJSONobject.put("tips", oPostingInfo.dTips.toPlainString());
			oPostingJSONobject.put("postAmount", oPostingInfo.dPostAmount.toPlainString());
			oPostingJSONobject.put("previousPaymentAmount", oPostingInfo.dPreviousPaymentAmount.toPlainString());
			oPostingJSONobject.put("cardNumber", oPostingInfo.sCardNumber);
			oPostingJSONobject.put("clearText", oPostingInfo.sClearText);
			oPostingJSONobject.put("guestFileNumber", oPostingInfo.sGuestFileNumber);
			oPostingJSONobject.put("date", oPostingInfo.sDate);
			oPostingJSONobject.put("time", oPostingInfo.sTime);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		
		return oPostingJSONobject;
	}
	
	static boolean isSupportPMS() {
		if(AppGlobal.isModuleSupport(AppGlobal.OPTIONAL_MODULE.pos_interface.name()) == false)
			return false;
		if(AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PMS) == null || AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PMS).size() == 0)
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
		}else if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP) || oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_4700_TCPIP) || oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_4700_SERIAL_PORT)) {
			//Standard PMS and 4700 PMS
			JSONObject oPaymentInterfaceSetup = oPMSInterfaceConfig.getConfigValue();
			Integer iNoAskInfo;
			iNoAskInfo = oPaymentInterfaceSetup.optJSONObject("general").optJSONObject("params").optJSONObject("no_ask_info").optInt("value");
			if(iNoAskInfo == null || iNoAskInfo.intValue() == 0)
				return true;
			else
				return false;
		}
		
		return false;
	}
	
	static public boolean haveDefaultAccount(PosInterfaceConfig oPMSInterfaceConfig) {
		/***No default account setup for R8 PMS***/
		//Standard PMS
		if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP) || oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_4700_TCPIP) || oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_4700_SERIAL_PORT)) {
			JSONObject oPaymentInterfaceSetup = oPMSInterfaceConfig.getConfigValue();
			String sDefaultAccount = oPaymentInterfaceSetup.optJSONObject("general").optJSONObject("params").optJSONObject("default_acc_num").optString("value");
			if(sDefaultAccount == null || sDefaultAccount.isEmpty())
				return false;
			else
				return true;
		}
		
		return true;
	}
	
	static public ArrayList<PosCheckExtraInfo> createPaymentPMSExtraInfoForPostingWithDefaultAccount(ArrayList<PosCheckExtraInfo> oCheckPaymentExtraInfos, PosInterfaceConfig oPMSInterfaceConfig, String sTable, String sPaymentCode) {
		if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_R8)) {
			// Interface id
			PosCheckExtraInfo oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_INTERFACE_ID);
			oPosCheckExtraInfo.setValue(String.valueOf(oPMSInterfaceConfig.getInterfaceId()));
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Guest name
			oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_GUEST_NAME);
			oPosCheckExtraInfo.setValue("");
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Guest no.
			oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_GUEST_NO);
			oPosCheckExtraInfo.setValue("");
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Room
			oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_ROOM);
			oPosCheckExtraInfo.setValue("");
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Register no.
			oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_REGISTER_NO);
			oPosCheckExtraInfo.setValue("");
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Guest file no.
			oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_GUEST_FILE_NO);
			oPosCheckExtraInfo.setValue("");
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Expiry date
			oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_EXPIRY_DATE);
			oPosCheckExtraInfo.setValue("0");
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
			oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_INTERNAL_USE);
			oPosCheckExtraInfo.setValue(oPMSInfoJSONObject.toString());
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
		}else if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP) || oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_4700_TCPIP) || oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_4700_SERIAL_PORT)) {
			
			JSONObject setupJSONObject = oPMSInterfaceConfig.getConfigValue();
			
			// Interface id
			PosCheckExtraInfo oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_INTERFACE_ID);
			oPosCheckExtraInfo.setValue(String.valueOf(oPMSInterfaceConfig.getInterfaceId()));
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Guest name
			oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_GUEST_NAME);
			oPosCheckExtraInfo.setValue("");
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Guest no.
			oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_GUEST_NO);
			if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP))
				oPosCheckExtraInfo.setValue(setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_acc_num").optString("value"));
			else
				oPosCheckExtraInfo.setValue("");
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Room
			oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_ROOM);
			if(oPMSInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP))
				oPosCheckExtraInfo.setValue("");
			else
				oPosCheckExtraInfo.setValue(setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_acc_num").optString("value"));
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Register no.
			oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_REGISTER_NO);
			oPosCheckExtraInfo.setValue("");
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Guest file no.
			oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_GUEST_FILE_NO);
			oPosCheckExtraInfo.setValue("");
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Expiry date
			oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_EXPIRY_DATE);
			oPosCheckExtraInfo.setValue(setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_expiry_date").optString("value"));
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Profolio Number
			oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_FIELD_NO);
			if(setupJSONObject.optJSONObject("general").optJSONObject("params").has("default_selection_line"))
				oPosCheckExtraInfo.setValue(setupJSONObject.optJSONObject("general").optJSONObject("params").optJSONObject("default_selection_line").optString("value"));
			else
				oPosCheckExtraInfo.setValue("");
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			
			// Internal usage
			JSONObject oPMSInfoJSONObject = new JSONObject();
			try {
				oPMSInfoJSONObject.put("table", sTable);
				oPMSInfoJSONObject.put("pm_code", sPaymentCode);
			}catch(JSONException jsone){
				jsone.printStackTrace();
			}
			oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_PMS);
			oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_INTERNAL_USE);
			oPosCheckExtraInfo.setValue(oPMSInfoJSONObject.toString());
			oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
		}
		
		return oCheckPaymentExtraInfos;
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
}
