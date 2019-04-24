package om;

import java.math.BigDecimal;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;
import app.FuncGamingInterface;
import app.FuncInventoryInterface;

public class PosInterfaceConfig {
	private int pifcId;
	private int shopId;
	private int oletId;
	private int statId;
	private int intfId;
	private String by;
	private int recordId;
	private String value;
	private String status;
	
	private InfInterface m_oInfInterface;
	private String m_sLastErrorType;
	private int m_iLastErrorCode;
	private String m_sLastErrorMessage;
	private JSONObject m_oLastSuccessResult;
	
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	public PosInterfaceConfig() {
		this.init();
	}
	
	public PosInterfaceConfig(JSONObject interfaceConfigJSONObject) {
		JSONObject infInterfaceJSONObject = null;
		
		this.init();
		infInterfaceJSONObject = interfaceConfigJSONObject.optJSONObject("InfInterface");
			if(infInterfaceJSONObject != null) 
				m_oInfInterface = new InfInterface(infInterfaceJSONObject);
		
		interfaceConfigJSONObject = interfaceConfigJSONObject.optJSONObject("PosInterfaceConfig");
		
		this.pifcId = interfaceConfigJSONObject.optInt("icfg_id");
		this.shopId = interfaceConfigJSONObject.optInt("icfg_shop_id");
		this.oletId = interfaceConfigJSONObject.optInt("icfg_olet_id");
		this.statId = interfaceConfigJSONObject.optInt("icfg_stat_id");
		this.intfId = interfaceConfigJSONObject.optInt("icfg_intf_id");
		this.by = interfaceConfigJSONObject.optString("icfg_by");
		this.recordId = interfaceConfigJSONObject.optInt("icfg_record_id");
		this.value = interfaceConfigJSONObject.optString("icfg_value", null);
		this.status = interfaceConfigJSONObject.optString("icfg_status", PosInterfaceConfig.STATUS_ACTIVE);
	}
	
	//read a list of data from API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray interfaceConfigJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("interfaceConfigs")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("interfaceConfigs")) {
				return null;
			}
			
			interfaceConfigJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("interfaceConfigs");
		}
		
		return interfaceConfigJSONArray;
	}
	
	public JSONArray getInterfaceConfigsByShopOutletIdStationId(int iShopId, int iOletId, int iStatId, String sBy) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("shopId", iShopId);
			requestJSONObject.put("outletId", iOletId);
			requestJSONObject.put("stationId", iStatId);
			requestJSONObject.put("by", sBy);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getInterfaceConfigsByShopOutletIdStationId", requestJSONObject.toString());
		return responseJSONArray;
	}

	public JSONArray getInterfaceConfigsByInterfaceCode(String sInterfaceCode) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;

		try {
			requestJSONObject.put("interfaceCode", sInterfaceCode);
		} catch (JSONException jsone) {
			AppGlobal.stack2Log(jsone);
		}
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getInterfaceConfigsByInterfaceCode",
				requestJSONObject.toString());
		return responseJSONArray;
	}

	/*************************/
	/***** PMS Interface *****/
	/*************************/
	// do PMS prepost
	public boolean doPmsPrepost(JSONObject oCheckInformation, JSONObject oPMSPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("check", oCheckInformation);
			requestJSONObject.put("postingInfo", oPMSPostingInfo);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "pmsPrepost", requestJSONObject.toString(), false))
			return false;
		else {
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult"))
				return false;
			
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("postingResult") == 0) {
				m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return false;
			}
			
			m_oLastSuccessResult = OmWsClientGlobal.g_oWsClient.get().getResponse();
			
			return true;
		}
	}
	
	// do PMS posting
	public JSONObject doPmsPosting(JSONObject oCheckInformation, JSONObject oPMSPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("check", oCheckInformation);
			requestJSONObject.put("postingInfo", oPMSPostingInfo);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "pmsPosting", requestJSONObject.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return null;
			}
							
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("postingResult") == 0) {
				m_sLastErrorType = "";
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return null;
			}
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse(); 
		}
	}
	
	// do PMS posting
	public boolean doPmsVoidPosting(JSONObject oCheckInformation, JSONObject oPMSPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("check", oCheckInformation);
			requestJSONObject.put("postingInfo", oPMSPostingInfo);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "pmsVoidPosting", requestJSONObject.toString(), false))
			return false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null || !OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult"))
				return false;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("postingResult") == 0) {
				m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return false;
			}
			
			return true;
		}
	}
	
	//restart PMS shell
	public boolean restartPMSShell(int iInterfaceId, String sVendorKey) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("interfaceId", iInterfaceId);
			requestJSONObject.put("vendorKey", sVendorKey);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "restartPmsShell", requestJSONObject.toString(), false))
			return false;
		else {
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("shellOpen")) {
				m_sLastErrorType = "i";
				m_iLastErrorCode = 5;
				m_sLastErrorMessage = "shell_is_not_alive";
				return false;
			} else {
				if (!OmWsClientGlobal.g_oWsClient.get().getResponse().optBoolean("shellOpen")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
					return false;
				}
				
				return true;
			}
		}
	}

	public String membershipGetSession(int iInterfaceId){
		JSONObject requestJSONObject = new JSONObject();
		
		// from the request information
		try {
			requestJSONObject.put("interfaceId", iInterfaceId);
			requestJSONObject.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
			requestJSONObject.put("outletId", AppGlobal.g_oFuncOutlet.get().getOutletId());
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "membershipGetSession", requestJSONObject.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("sessionId"))
				return OmWsClientGlobal.g_oWsClient.get().getResponse().optString("sessionId");
			else
				return null; 
		}
	}
	
	//stop PMS shell
	public boolean stopPMSShell(int iInterfaceId, String sVendorKey) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("interfaceId", iInterfaceId);
			requestJSONObject.put("vendorKey", sVendorKey);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "stopPmsShell", requestJSONObject.toString(), false))
			return false;
		else
			return true;
	}
	
	/*****************************/
	/***** Payment Interface *****/
	/*****************************/
	public JSONArray checkPaymentResults(String sOutletCode, JSONArray oPrintedCheckInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("interfaceId", this.intfId);
			requestJSONObject.put("outletCode", sOutletCode);
			requestJSONObject.put("checkInfos", oPrintedCheckInfo);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "getOgsPaymentResults", requestJSONObject.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("payInfos"))
				return OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("payInfos");
			else
				return null; 
		}
	}
		
	public boolean releaseOgsPayment(String sOutletCode, int iPayType, JSONObject oCheckInfo) {
		JSONObject requestJSONObject = new JSONObject();
		try {
			requestJSONObject.put("interfaceId", this.intfId);
			requestJSONObject.put("outletCode", sOutletCode);
			requestJSONObject.put("payType", iPayType);
			requestJSONObject.put("check", oCheckInfo);
		} catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "getOgsReleasePaymentResult", requestJSONObject.toString(), false))
			return false;
		else {
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("result"))
				return false;

			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("result") && OmWsClientGlobal.g_oWsClient.get().getResponse().optBoolean("result")) {
				m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return false;
			} else
				return true;
		}
	}
	
	//get pre-order
	public JSONObject getPreOrder(String sOutletCode, String sOrderNo) {
		JSONObject oRequestJSONObject = new JSONObject();
		
		try {
			oRequestJSONObject.put("interfaceId", this.intfId);
			oRequestJSONObject.put("outletCode", sOutletCode);
			oRequestJSONObject.put("orderNo", sOrderNo);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "getOgsPreOrder", oRequestJSONObject.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null; 
		}
		
		return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	//restart payment interface shell
	public boolean restartPaymentInterfaceShell(int iInterfaceId) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("interfaceId", iInterfaceId);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "restartPaymentInterfaceShell", requestJSONObject.toString(), false))
			return false;
		else {
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("result")) {
				m_sLastErrorType = "i";
				m_iLastErrorCode = 5;
				m_sLastErrorMessage = "shell_is_not_alive";
				return false;
			 }else if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("result") && OmWsClientGlobal.g_oWsClient.get().getResponse().optBoolean("result")) {
				m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return false;
			} else
				return true;
		}
	}
	
	//stop payment interface shell
	public boolean stopPaymentInterfaceShell(int iInterfaceId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("interfaceId", iInterfaceId);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "stopPaymentInterfaceShell", requestJSONObject.toString(), false))
			return false;
		else
			return true;
	}
	
	/**********************************/
	/***** Card Enquiry Interface *****/
	/**********************************/
	public JSONObject cardEnquiry(HashMap<String, String> oEnquiryInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("interfaceId", this.intfId);
			requestJSONObject.put("outletId", oEnquiryInfo.get("outletId"));
			requestJSONObject.put("outletCode", oEnquiryInfo.get("outletCode"));
			requestJSONObject.put("stationCode", oEnquiryInfo.get("stationCode"));
			requestJSONObject.put("employeeNum", oEnquiryInfo.get("employeeNum"));
			requestJSONObject.put("enqValue", oEnquiryInfo.get("memberNumber"));
			if(oEnquiryInfo.containsKey("cardNumber"))
				requestJSONObject.put("cardNumber", oEnquiryInfo.get("cardNumber"));
			if(oEnquiryInfo.containsKey("memberNumber"))
				requestJSONObject.put("memberNumber", oEnquiryInfo.get("memberNumber"));
			if(oEnquiryInfo.containsKey("inputType"))
				requestJSONObject.put("inputType", oEnquiryInfo.get("inputType"));
			if(oEnquiryInfo.containsKey("actionType"))
				requestJSONObject.put("actionType", oEnquiryInfo.get("actionType"));
			if(oEnquiryInfo.containsKey("shopId"))
				requestJSONObject.put("shopId", oEnquiryInfo.get("shopId"));
			if(oEnquiryInfo.containsKey("bdayId"))
				requestJSONObject.put("bdayId", oEnquiryInfo.get("bdayId"));
			if(oEnquiryInfo.containsKey("stationId"))
				requestJSONObject.put("stationId", oEnquiryInfo.get("stationId"));
			if(oEnquiryInfo.containsKey("userId"))
				requestJSONObject.put("userId", oEnquiryInfo.get("userId"));
			
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "membershipCardEnquiry", requestJSONObject.toString(), false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			return null;
		} else
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}


	/********************************/
	/***** Membership Interface *****/
	/********************************/
	public JSONObject memberEnquiry(HashMap<String, String> oEnquiryInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			//general information
			requestJSONObject.put("interfaceId", this.intfId);
			requestJSONObject.put("outletId", oEnquiryInfo.get("outletId"));
			requestJSONObject.put("outletCode", oEnquiryInfo.get("outletCode"));
			requestJSONObject.put("stationCode", oEnquiryInfo.get("stationCode"));
			requestJSONObject.put("employeeNum", oEnquiryInfo.get("employeeNum"));
			
			//optional information
			if(oEnquiryInfo.containsKey("businessDate"))
				requestJSONObject.put("businessDate", oEnquiryInfo.get("businessDate"));
			if(oEnquiryInfo.containsKey("checkId"))
				requestJSONObject.put("checkId", oEnquiryInfo.get("checkId"));
			if(oEnquiryInfo.containsKey("checkNumber"))
				requestJSONObject.put("checkNumber", oEnquiryInfo.get("checkNumber"));
			if(oEnquiryInfo.containsKey("checkAmount"))
				requestJSONObject.put("checkAmount", oEnquiryInfo.get("checkAmount"));
			if(oEnquiryInfo.containsKey("traceId"))
				requestJSONObject.put("traceId", oEnquiryInfo.get("traceId"));
			if(oEnquiryInfo.containsKey("cardNumber"))
				requestJSONObject.put("cardNumber", oEnquiryInfo.get("cardNumber"));
			if(oEnquiryInfo.containsKey("password"))
				requestJSONObject.put("password", oEnquiryInfo.get("password"));
			if(oEnquiryInfo.containsKey("timeZone"))
				requestJSONObject.put("timeZone", oEnquiryInfo.get("timeZone"));
			if(oEnquiryInfo.containsKey("memberNumber"))
				requestJSONObject.put("memberNumber", oEnquiryInfo.get("memberNumber"));
			if(oEnquiryInfo.containsKey("passportNumber"))
				requestJSONObject.put("passportNumber", oEnquiryInfo.get("passportNumber"));
			if(oEnquiryInfo.containsKey("mobileNumber"))
				requestJSONObject.put("mobileNumber", oEnquiryInfo.get("mobileNumber"));
			if(oEnquiryInfo.containsKey("email"))
				requestJSONObject.put("email", oEnquiryInfo.get("email"));
			if(oEnquiryInfo.containsKey("nric"))
				requestJSONObject.put("nric", oEnquiryInfo.get("nric"));
			if(oEnquiryInfo.containsKey("name"))
				requestJSONObject.put("name", oEnquiryInfo.get("name"));
			if(oEnquiryInfo.containsKey("customerNumber"))
				requestJSONObject.put("customerNumber", oEnquiryInfo.get("customerNumber"));
			if(oEnquiryInfo.containsKey("cardNumber"))
				requestJSONObject.put("cardNumber", oEnquiryInfo.get("cardNumber"));
			if(oEnquiryInfo.containsKey("memberName"))
				requestJSONObject.put("memberName", oEnquiryInfo.get("memberName"));
			if(oEnquiryInfo.containsKey("surname"))
				requestJSONObject.put("surname", oEnquiryInfo.get("surname"));
			if(oEnquiryInfo.containsKey("givenName"))
				requestJSONObject.put("givenName", oEnquiryInfo.get("givenName"));
			if(oEnquiryInfo.containsKey("chineseName"))
				requestJSONObject.put("chineseName", oEnquiryInfo.get("chineseName"));
			if(oEnquiryInfo.containsKey("lastName"))
				requestJSONObject.put("lastName", oEnquiryInfo.get("lastName"));
			if(oEnquiryInfo.containsKey("name"))
				requestJSONObject.put("name", oEnquiryInfo.get("name"));
			
			if(oEnquiryInfo.containsKey("memberName"))
				requestJSONObject.put("firstName", oEnquiryInfo.get("memberName"));
			if(oEnquiryInfo.containsKey("country"))
				requestJSONObject.put("country", oEnquiryInfo.get("country"));
			if(oEnquiryInfo.containsKey("city"))
				requestJSONObject.put("city", oEnquiryInfo.get("city"));
			if(oEnquiryInfo.containsKey("postalCode"))
				requestJSONObject.put("postalCode", oEnquiryInfo.get("postalCode"));
			if(oEnquiryInfo.containsKey("stateRegion"))
				requestJSONObject.put("stateRegion", oEnquiryInfo.get("stateRegion"));
			if(oEnquiryInfo.containsKey("enquiryNumber"))
				requestJSONObject.put("enquiryNumber", oEnquiryInfo.get("enquiryNumber"));
			if(oEnquiryInfo.containsKey("enquiryType"))
				requestJSONObject.put("enquiryType", oEnquiryInfo.get("enquiryType"));
			if(oEnquiryInfo.containsKey("sessionId"))
				requestJSONObject.put("sessionId", oEnquiryInfo.get("sessionId"));
			if (oEnquiryInfo.containsKey("customTypeId"))
				requestJSONObject.put("customTypeId", oEnquiryInfo.get("customTypeId"));
			if (oEnquiryInfo.containsKey("attributeId"))
				requestJSONObject.put("attributeId", oEnquiryInfo.get("attributeId"));
			if (oEnquiryInfo.containsKey("posPlatform"))
				requestJSONObject.put("posPlatform", oEnquiryInfo.get("posPlatform"));
			
			m_sLastErrorMessage = "";
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "membershipEnquiry", requestJSONObject.toString(), false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			return null;
		} else
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	public JSONObject membershipPrepaidCardEnquiry(HashMap<String, String> oEnquiryInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("interfaceId", this.intfId);
			requestJSONObject.put("outletId", oEnquiryInfo.get("outletId"));
			requestJSONObject.put("outletCode", oEnquiryInfo.get("outletCode"));
			requestJSONObject.put("stationCode", oEnquiryInfo.get("stationCode"));
			requestJSONObject.put("employeeNum", oEnquiryInfo.get("employeeNum"));
			requestJSONObject.put("prepaidCardNumber", oEnquiryInfo.get("prepaidCardNumber"));
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "membershipPrepaidCardEnquiry", requestJSONObject.toString(), false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			return null;
		} else
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
//	 do membership interface posting
	public boolean doMembershipPosting(JSONObject oCheckInformation, JSONObject oMemberPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("check", oCheckInformation);
			requestJSONObject.put("postingInfo", oMemberPostingInfo);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "membershipPosting", requestJSONObject.toString(), false))
			return false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return false;
			}
			
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("postingResult") == 0) {
				m_sLastErrorType = "";
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return false;
			}
			
			m_oLastSuccessResult = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("resultDetails");
			
			return true; 
		}
	}
	
	public JSONObject membershipAffiliation(HashMap<String, String> oEnquiryInfo) {
		JSONObject requestJSONObject = new JSONObject();
		JSONObject oFacilityInfo = new JSONObject();
		
		try {
			requestJSONObject.put("interfaceId", this.intfId);
			if (oEnquiryInfo.containsKey("facilityCode"))
				oFacilityInfo.put("code", oEnquiryInfo.get("facilityCode"));
			if (oEnquiryInfo.containsKey("facilityType"))
				oFacilityInfo.put("type", oEnquiryInfo.get("facilityType"));
			if (oEnquiryInfo.containsKey("memberNumber"))
				requestJSONObject.put("memberNumber", oEnquiryInfo.get("memberNumber"));
			if (oEnquiryInfo.containsKey("type"))
				requestJSONObject.put("type", oEnquiryInfo.get("type"));
			if (oFacilityInfo.has("code") && oFacilityInfo.has("type"))
				requestJSONObject.put("facility", oFacilityInfo);
			if (oEnquiryInfo.containsKey("outletCode"))
				requestJSONObject.put("outletCode", oEnquiryInfo.get("outletCode"));
			if (oEnquiryInfo.containsKey("stationCode"))
				requestJSONObject.put("stationCode", oEnquiryInfo.get("stationCode"));
			if (oEnquiryInfo.containsKey("employeeNum"))
				requestJSONObject.put("employeeNum", oEnquiryInfo.get("employeeNum"));
			if (oEnquiryInfo.containsKey("date"))
				requestJSONObject.put("date", oEnquiryInfo.get("date"));
			if (oEnquiryInfo.containsKey("time"))
				requestJSONObject.put("time", oEnquiryInfo.get("time"));
			if (oEnquiryInfo.containsKey("timeZone"))
				requestJSONObject.put("timeZone", oEnquiryInfo.get("timeZone"));
			if (oEnquiryInfo.containsKey("sessionId"))
				requestJSONObject.put("sessionId", oEnquiryInfo.get("sessionId"));
			m_sLastErrorMessage = "";
		} catch (JSONException jsone) {
			AppGlobal.stack2Log(jsone);
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "membershipAffiliation", requestJSONObject.toString(), false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			return null;
		} else
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	// do membership interface posting
	public boolean doMembershipPointEarn(JSONObject oCheckInformation, JSONObject oMemberPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("check", oCheckInformation);
			requestJSONObject.put("postingInfo", oMemberPostingInfo);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}

		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "membershipPointEarn", requestJSONObject.toString(), false))
			return false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("result")) {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return false;
			}
			
			m_oLastSuccessResult = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("result");
			
			return true; 
		}
	}
	
	// do membership interface posting
	public boolean doMembershipPointRefund(JSONObject oCheckInformationJSONObject, JSONObject oMemberPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("check", oCheckInformationJSONObject);
			requestJSONObject.put("postingInfo", oMemberPostingInfo);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "membershipPointRefund", requestJSONObject.toString(), false))
			return false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("resultDetails")) {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return false;
			}
			
			m_oLastSuccessResult = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("resultDetails");
			
			return true; 
		}
	}
		
	// do membership interface posting
	public boolean doMembershipVoidPosting(JSONObject oCheckInformation, JSONObject oPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("check", oCheckInformation);
			requestJSONObject.put("postingInfo", oPostingInfo);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "membershipVoidPosting", requestJSONObject.toString(), false))
			return false;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null || !OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult"))
				return false;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("postingResult") == 0) {
				m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return false;
			}
			else
				this.m_oLastSuccessResult = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("resultDetails");

			return true;
		}
	}
	
	//membership detail posting
	public boolean doMembershipDetailPosting(JSONObject oPostingInfo){
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("postingInfo", oPostingInfo);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "membershipDetailPosting", requestJSONObject.toString(), false))
			return false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return false;
			}
			
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("postingResult") == 0) {
				m_sLastErrorType = "";
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return false;
			}
			
			m_oLastSuccessResult = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("resultDetails");
		}
		return true;
	}
	
	//membership point enquiry
	public JSONObject doMembershipPointEnquiry(int iInterfaceId){
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("interfaceId", iInterfaceId);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "membershipPointEnquiry", requestJSONObject.toString(), false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			return null;
		}else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("enquiryResult")) {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return null;
			}
			
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().optBoolean("enquiryResult")) {
				m_sLastErrorType = "";
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return null;
			}
			
			m_oLastSuccessResult = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("conversionRateList");
		}
		return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	/**********************************/
	/***** Membership Registration Interface *****/
	/**********************************/
	public JSONObject membershipRegistration(HashMap<String, String> oEnquiryInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("interfaceId", this.intfId);
			requestJSONObject.put("outletId", oEnquiryInfo.get("outletId"));
			requestJSONObject.put("outletCode", oEnquiryInfo.get("outletCode"));
			requestJSONObject.put("stationCode", oEnquiryInfo.get("stationCode"));
			requestJSONObject.put("employeeNum", oEnquiryInfo.get("employeeNum"));
			requestJSONObject.put("enqValue", "");
			if(oEnquiryInfo.containsKey("memberNumber"))
				requestJSONObject.put("memberNumber", oEnquiryInfo.get("memberNumber"));
			if(oEnquiryInfo.containsKey("firstName"))
				requestJSONObject.put("firstName", oEnquiryInfo.get("firstName"));
			if(oEnquiryInfo.containsKey("lastName"))
				requestJSONObject.put("lastName", oEnquiryInfo.get("lastName"));
			if(oEnquiryInfo.containsKey("mobileNumber"))
				requestJSONObject.put("mobileNumber", oEnquiryInfo.get("mobileNumber"));
			if(oEnquiryInfo.containsKey("password"))
				requestJSONObject.put("password", oEnquiryInfo.get("password"));
			if(oEnquiryInfo.containsKey("birthday"))
				requestJSONObject.put("birthday", oEnquiryInfo.get("birthday"));
			if(oEnquiryInfo.containsKey("givenName"))
				requestJSONObject.put("givenName", oEnquiryInfo.get("givenName"));
			if(oEnquiryInfo.containsKey("surname"))
				requestJSONObject.put("surname", oEnquiryInfo.get("surname"));
			if(oEnquiryInfo.containsKey("gender"))
				requestJSONObject.put("gender", oEnquiryInfo.get("gender"));
			if(oEnquiryInfo.containsKey("email"))
				requestJSONObject.put("email", oEnquiryInfo.get("email"));
			if(oEnquiryInfo.containsKey("cardNumber"))
				requestJSONObject.put("cardNumber", oEnquiryInfo.get("cardNumber"));
			if(oEnquiryInfo.containsKey("inputType"))
				requestJSONObject.put("inputType", oEnquiryInfo.get("inputType"));
			if(oEnquiryInfo.containsKey("shopId"))
				requestJSONObject.put("shopId", oEnquiryInfo.get("shopId"));
			if(oEnquiryInfo.containsKey("bdayId"))
				requestJSONObject.put("bdayId", oEnquiryInfo.get("bdayId"));
			if(oEnquiryInfo.containsKey("stationId"))
				requestJSONObject.put("stationId", oEnquiryInfo.get("stationId"));
			if(oEnquiryInfo.containsKey("userId"))
				requestJSONObject.put("userId", oEnquiryInfo.get("userId"));
			if(oEnquiryInfo.containsKey("memberPrefix"))
				requestJSONObject.put("memberPrefix", oEnquiryInfo.get("memberPrefix"));
			if(oEnquiryInfo.containsKey("preferredLanguage"))
				requestJSONObject.put("preferredLanguage", oEnquiryInfo.get("preferredLanguage"));
			if(oEnquiryInfo.containsKey("sourceCode"))
				requestJSONObject.put("sourceCode", oEnquiryInfo.get("sourceCode"));
			if(oEnquiryInfo.containsKey("businessDate"))
				requestJSONObject.put("businessDate", oEnquiryInfo.get("businessDate"));
			if(oEnquiryInfo.containsKey("facilityCode") && oEnquiryInfo.containsKey("facilityType")) {
				JSONObject oTmpJSONObject = new JSONObject();
				try {
					oTmpJSONObject.put("code", oEnquiryInfo.get("facilityCode"));
					oTmpJSONObject.put("type", oEnquiryInfo.get("facilityType"));
				} catch (JSONException e) {
					AppGlobal.stack2Log(e);
				}
				requestJSONObject.put("facility", oTmpJSONObject);
			}
			if(oEnquiryInfo.containsKey("sessionId"))
				requestJSONObject.put("sessionId", oEnquiryInfo.get("sessionId"));
			if(oEnquiryInfo.containsKey("timeZone"))
				requestJSONObject.put("timeZone", oEnquiryInfo.get("timeZone"));
			if(oEnquiryInfo.containsKey("contestNumber"))
				requestJSONObject.put("contestNumber", oEnquiryInfo.get("contestNumber"));
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "membershipRegistration", requestJSONObject.toString(), false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			return null;
		} else
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	//do membership interface card coupon list enquiry
	public JSONObject doMembershipCardCouponListEnquiry(HashMap<String, String> oEnquiryInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("interfaceId", this.intfId);
			requestJSONObject.put("outletId", oEnquiryInfo.get("outletId"));
			requestJSONObject.put("outletCode", oEnquiryInfo.get("outletCode"));
			requestJSONObject.put("stationCode", oEnquiryInfo.get("stationCode"));
			requestJSONObject.put("employeeNum", oEnquiryInfo.get("employeeNum"));
			requestJSONObject.put("enqValue", oEnquiryInfo.get("memberNumber"));
			requestJSONObject.put("couponNumber", oEnquiryInfo.get("couponNumber"));
			requestJSONObject.put("timezone", oEnquiryInfo.get("timeZone"));
			if (oEnquiryInfo.containsKey("cardNumber"))
				requestJSONObject.put("cardNumber", oEnquiryInfo.get("cardNumber"));
			if (oEnquiryInfo.containsKey("memberNumber"))
				requestJSONObject.put("memberNumber", oEnquiryInfo.get("memberNumber"));
		} catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "membershipCouponEnquiry",
				requestJSONObject.toString(), false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			return null;
		} else
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}

	public JSONObject doMembershipVoucherRedeem(HashMap<String, String> oEnquiryInfo) {
		JSONObject requestJSONObject = new JSONObject();
		try {
			requestJSONObject.put("interfaceId", this.intfId);
			requestJSONObject.put("outletId", oEnquiryInfo.get("outletId"));
			requestJSONObject.put("outletCode", oEnquiryInfo.get("outletCode"));
			requestJSONObject.put("stationCode", oEnquiryInfo.get("stationCode"));
			requestJSONObject.put("employeeNum", oEnquiryInfo.get("employeeNum"));
			requestJSONObject.put("enqValue", oEnquiryInfo.get("memberNumber"));
			if(oEnquiryInfo.containsKey("couponNumber"))
				requestJSONObject.put("couponNumber", oEnquiryInfo.get("couponNumber"));
			requestJSONObject.put("timezone", oEnquiryInfo.get("timezone"));
			if (oEnquiryInfo.containsKey("cardNumber"))
				requestJSONObject.put("cardNumber", oEnquiryInfo.get("cardNumber"));
			if (oEnquiryInfo.containsKey("memberNumber"))
				requestJSONObject.put("memberNumber", oEnquiryInfo.get("memberNumber"));
			if(oEnquiryInfo.containsKey("nameId"))
				requestJSONObject.put("nameId", oEnquiryInfo.get("nameId"));
			if(oEnquiryInfo.containsKey("pointsUse"))
				requestJSONObject.put("pointsUse", oEnquiryInfo.get("pointsUse"));
		} catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "membershipCouponRedeem", requestJSONObject.toString(), false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			return null;
		} else
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	public JSONObject membershipExchangeRateEnquiry(HashMap<String, String> oEnquiryInfo) {
		JSONObject requestJSONObject = new JSONObject();
		try {
			requestJSONObject.put("interfaceId", this.intfId);
			requestJSONObject.put("outletId", oEnquiryInfo.get("outletId"));
			requestJSONObject.put("outletCode", oEnquiryInfo.get("outletCode"));
			requestJSONObject.put("stationCode", oEnquiryInfo.get("stationCode"));
			requestJSONObject.put("tierLevel", oEnquiryInfo.get("tierLevel"));
			if(oEnquiryInfo.containsKey("points"))
				requestJSONObject.put("points", oEnquiryInfo.get("points"));
			if(oEnquiryInfo.containsKey("amount"))
				requestJSONObject.put("amount", oEnquiryInfo.get("amount"));
		} catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "membershipExchangeRateEnquiry", requestJSONObject.toString(), false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			return null;
		} else
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	/**********************************/
	/*** Membership deposit balance ***/
	/**********************************/
	public JSONObject membershipDepositEnquiry(HashMap<String, String> oEnquiryInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("interfaceId", this.intfId);
			requestJSONObject.put("outletId", oEnquiryInfo.get("outletId"));
			requestJSONObject.put("outletCode", oEnquiryInfo.get("outletCode"));
			requestJSONObject.put("stationCode", oEnquiryInfo.get("stationCode"));
			if(oEnquiryInfo.containsKey("eventOrderNumber"))
				requestJSONObject.put("eventOrderNumber", oEnquiryInfo.get("eventOrderNumber"));

		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "membershipDepositEnquiry", requestJSONObject.toString(), false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			return null;
		} else
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	/**********************************/
	/*** Membership deposit balance ***/
	/**********************************/
	public boolean membershipAddDepositPosting(HashMap<String, String> oEnquiryInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("interfaceId", this.intfId);
			requestJSONObject.put("outletId", oEnquiryInfo.get("outletId"));
			requestJSONObject.put("outletCode", oEnquiryInfo.get("outletCode"));
			requestJSONObject.put("stationCode", oEnquiryInfo.get("stationCode"));
			if(oEnquiryInfo.containsKey("eventOrderNumber"))
				requestJSONObject.put("eventOrderNumber", oEnquiryInfo.get("eventOrderNumber"));
			if(oEnquiryInfo.containsKey("eventOrderDeposit"))
				requestJSONObject.put("eventOrderDeposit", oEnquiryInfo.get("eventOrderDeposit"));
			if(oEnquiryInfo.containsKey("paymentType"))
				requestJSONObject.put("paymentType", oEnquiryInfo.get("paymentType"));
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "membershipAddDepositPosting", requestJSONObject.toString(), false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			return false;
		} else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null || !OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult"))
				return false;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("postingResult") == 0) {
				m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return false;
			}
			
			return true;
		}
	}
	
	/**************************/
	/***** AwardRetrieval *****/
	/**************************/
	public JSONObject awardRetrieval(HashMap<String, String> oEnquiryInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			//general information
			requestJSONObject.put("interfaceId", this.intfId);
			requestJSONObject.put("outletId", oEnquiryInfo.get("outletId"));
			requestJSONObject.put("outletCode", oEnquiryInfo.get("outletCode"));
			requestJSONObject.put("stationCode", oEnquiryInfo.get("stationCode"));
			requestJSONObject.put("employeeNum", oEnquiryInfo.get("employeeNum"));
			requestJSONObject.put("businessDate", oEnquiryInfo.get("businessDate"));
			requestJSONObject.put("sessionId", oEnquiryInfo.get("sessionId"));
			requestJSONObject.put("timeZone", oEnquiryInfo.get("timeZone"));
		}catch(JSONException jsone) {
			AppGlobal.stack2Log(jsone);
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "membershipAwardsRetrieval", requestJSONObject.toString(), false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			return null;
		} else {
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().optBoolean("enquiryResult")) {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return null;
			}
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
		}
	}
	
	/*************************/
	/***** TMS Interface *****/
	/*************************/
	// do TMS open check
	public JSONObject doTmsOpenCheck(JSONObject oTmsPostingInfo) {
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";

		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "tmsOpenCheck", oTmsPostingInfo.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return null;
			}
							
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("postingResult") == 0) {
				m_sLastErrorType = "";
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return null;
			}
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse(); 
		}
	}
	
	// do TMS set status
	public JSONObject doTmsSetStatus(JSONObject oTmsPostingInfo) {
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "tmsSetStatus", oTmsPostingInfo.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return null;
			}
							
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("postingResult") == 0) {
				m_sLastErrorType = "";
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return null;
			}
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse(); 
		}
	}
	
	// do TMS change table
	public JSONObject doTmsChangeTable(JSONObject oTmsPostingInfo) {
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";

		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "tmsChangeTable", oTmsPostingInfo.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return null;
			}
							
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("postingResult") == 0) {
				m_sLastErrorType = "";
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return null;
			}
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse(); 
		}
	}
	
	// do TMS delete table
	public JSONObject doTmsDeleteCheck(JSONObject oTmsPostingInfo) {
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";

		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "tmsDeleteCheck", oTmsPostingInfo.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return null;
			}
							
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("postingResult") == 0) {
				m_sLastErrorType = "";
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return null;
			}			
			return OmWsClientGlobal.g_oWsClient.get().getResponse(); 
		}
	}
	
	// do TMS delete table
	public JSONObject doTmsPutCheck(JSONObject oCheckInformation, JSONObject oTmsPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("check", oCheckInformation);
			requestJSONObject.put("postingInfo", oTmsPostingInfo);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}

		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "tmsPutCheck", requestJSONObject.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return null;
			}
							
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("postingResult") == 0) {
				m_sLastErrorType = "";
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return null;
			}			
			return OmWsClientGlobal.g_oWsClient.get().getResponse(); 
		}
	}
	
	// do TMS delayed vacate
	public JSONObject doTmsDelayedVacate(JSONObject oTmsPostingInfo) {
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";

		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "tmsDelayedVacate", oTmsPostingInfo.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return null;
			}
							
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("postingResult") == 0) {
				m_sLastErrorType = "";
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return null;
			}			
			return OmWsClientGlobal.g_oWsClient.get().getResponse(); 
		}
	}
	
	/*****************************/
	/***** Loyalty Interface *****/
	/*****************************/
	// do Loyalty interface login
	public JSONObject doLoyaltyLogin(HashMap<String, String> oLoginInformation) {
		JSONObject requestJSONObject = new JSONObject();
		
		try{
			requestJSONObject.put("outletId", oLoginInformation.get("outletId"));
			requestJSONObject.put("outletCode", oLoginInformation.get("outletCode"));
			requestJSONObject.put("interfaceId", oLoginInformation.get("interfaceId"));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "loyaltyLogin", requestJSONObject.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("sessionId")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("sessionId"))
				return null;
			
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Login to Loyalty");
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
		}
	}
	
	// do Loyalty start redeem
	public JSONObject doLoyaltyStartRedeem(JSONObject requestJSONObject) {
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "loyaltyStartRedeem", requestJSONObject.toString(), false))
			return null;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
		}
	}
	
	// do Loyalty close transaction
	public boolean doLoyaltyCloseTransaction(HashMap<String, String> oCloseTransactionInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try{
			requestJSONObject.put("outletId", oCloseTransactionInfo.get("outletId"));
			requestJSONObject.put("outletCode", oCloseTransactionInfo.get("outletCode"));
			requestJSONObject.put("interfaceId", oCloseTransactionInfo.get("interfaceId"));
			requestJSONObject.put("sessionId", oCloseTransactionInfo.get("sessionId"));
			requestJSONObject.put("referenceId", oCloseTransactionInfo.get("referenceId"));
			requestJSONObject.put("bonusRedeemed", oCloseTransactionInfo.get("bonusRedeemed"));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "loyaltyCloseTransaction", requestJSONObject.toString(), false))
			return false;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
		}
		
		return true;
	}
	
	// do Loyalty check balance
	public JSONObject doLoyaltyCheckBalance(HashMap<String, String> oCheckBalanceInfo, String sVendorKey) {
		JSONObject requestJSONObject = new JSONObject();
		
		try{
			requestJSONObject.put("outletId", oCheckBalanceInfo.get("outletId"));
			requestJSONObject.put("outletCode", oCheckBalanceInfo.get("outletCode"));
			requestJSONObject.put("interfaceId", oCheckBalanceInfo.get("interfaceId"));
			requestJSONObject.put("interfaceCode", oCheckBalanceInfo.get("interfaceCode"));
			if(oCheckBalanceInfo.containsKey("memberNumber"))
				requestJSONObject.put("memberNumber", oCheckBalanceInfo.get("memberNumber"));
			
			if(oCheckBalanceInfo.containsKey("sessionId"))
				requestJSONObject.put("sessionId", oCheckBalanceInfo.get("sessionId"));
			
			if(oCheckBalanceInfo.containsKey("password"))
				requestJSONObject.put("password", oCheckBalanceInfo.get("password"));
			
			if(oCheckBalanceInfo.containsKey("svcCardNumber"))
				requestJSONObject.put("svcCardNumber", oCheckBalanceInfo.get("svcCardNumber"));
			
			if(oCheckBalanceInfo.containsKey("langCode"))
				requestJSONObject.put("langCode", oCheckBalanceInfo.get("langCode"));
			
			if(oCheckBalanceInfo.containsKey("transactionCode"))
				requestJSONObject.put("transactionCode", oCheckBalanceInfo.get("transactionCode"));
			
			if(oCheckBalanceInfo.containsKey("externalLogin"))
				requestJSONObject.put("externalLogin", oCheckBalanceInfo.get("externalLogin"));
			
			if(oCheckBalanceInfo.containsKey("externalPassword"))
				requestJSONObject.put("externalPassword", oCheckBalanceInfo.get("externalPassword"));
			
			if(oCheckBalanceInfo.containsKey("items"))
				requestJSONObject.put("items", new JSONObject(oCheckBalanceInfo.get("items")));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "loyaltyCheckBalance", requestJSONObject.toString(), false))
			return null;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			if(sVendorKey.equals(InfVendor.KEY_GM_LOYALTY) || sVendorKey.equals(InfVendor.KEY_GM_LOYALTY_SVC)){
				if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("member"))
					return null;
				
				if (OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("member"))
					return null;
			}
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
		}
	}
	
	// do Loyalty cancel redeem
	public boolean doLoyaltyCancelRedeem(HashMap<String, String> oCancelRedeemInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try{
			requestJSONObject.put("outletId", oCancelRedeemInfo.get("outletId"));
			requestJSONObject.put("outletCode", oCancelRedeemInfo.get("outletCode"));
			requestJSONObject.put("interfaceId", oCancelRedeemInfo.get("interfaceId"));
			requestJSONObject.put("interfaceCode", oCancelRedeemInfo.get("interfaceCode"));
			requestJSONObject.put("sessionId", oCancelRedeemInfo.get("sessionId"));
			requestJSONObject.put("referenceId", oCancelRedeemInfo.get("referenceId"));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "loyaltyCancelTransaction", requestJSONObject.toString(), false))
			return false;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorCode"))
				return false;
			
			return true;
		}
	}
	
	// do loyalty release transaction
	public boolean doLoyaltyReleaseTrans(HashMap<String, String> oReleaseTransInfo){
		JSONObject requestJSONObject = new JSONObject();
		
		try{
			requestJSONObject.put("outletId", oReleaseTransInfo.get("outletId"));
			requestJSONObject.put("outletCode", oReleaseTransInfo.get("outletCode"));
			requestJSONObject.put("interfaceId", oReleaseTransInfo.get("interfaceId"));
			requestJSONObject.put("interfaceCode", oReleaseTransInfo.get("interfaceCode"));
			requestJSONObject.put("sessionId", oReleaseTransInfo.get("sessionId"));
			requestJSONObject.put("referenceId", oReleaseTransInfo.get("referenceId"));
			requestJSONObject.put("bonusRedeemed", oReleaseTransInfo.get("bonusRedeemed"));
			requestJSONObject.put("cardNumber", oReleaseTransInfo.get("cardNumber"));
			requestJSONObject.put("amount", oReleaseTransInfo.get("amount"));
			requestJSONObject.put("transactionCode", oReleaseTransInfo.get("transactionCode"));
			requestJSONObject.put("authCode", oReleaseTransInfo.get("authCode"));
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
				if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "loyaltyReleaseTransaction", requestJSONObject.toString(), false))
			return false;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorCode") || OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")){
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode", 0);
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage", "");
				return false;
			}
			return true;
		}
	}
	
	// do Loyalty SVC add value or issue card
	public JSONObject doLoyaltySvcAddValueOrIssueCard(HashMap<String, String> oAddValueInfo) {
		JSONObject requestJSONObject = new JSONObject();
		try{
			requestJSONObject.put("interfaceId", oAddValueInfo.get("interfaceId"));
			requestJSONObject.put("svcInterfaceId", oAddValueInfo.get("svcInterfaceId"));
			requestJSONObject.put("outletId", oAddValueInfo.get("outletId"));
			requestJSONObject.put("outletCode", oAddValueInfo.get("outletCode"));
			requestJSONObject.put("sessionId", oAddValueInfo.get("sessionId"));
			requestJSONObject.put("svcSessionId", oAddValueInfo.get("sessionId"));
			requestJSONObject.put("transactionCode", oAddValueInfo.get("transactionCode"));
			requestJSONObject.put("externalLogin", oAddValueInfo.get("externalLogin"));
			requestJSONObject.put("externalPassword", oAddValueInfo.get("externalPassword"));
			requestJSONObject.put("langCode", oAddValueInfo.get("langCode"));
			requestJSONObject.put("action", oAddValueInfo.get("action"));
			
			JSONObject itemJSONObject = new JSONObject();
			itemJSONObject.put("svcCardNumber", oAddValueInfo.get("svcCardNumber"));
			itemJSONObject.put("memberNumber", oAddValueInfo.get("memberNumber"));
			itemJSONObject.put("traceId", oAddValueInfo.get("traceId"));
			itemJSONObject.put("businessDate", oAddValueInfo.get("businessDate"));
			itemJSONObject.put("bonusAmount", oAddValueInfo.get("bonusAmount"));
			itemJSONObject.put("employeeId", oAddValueInfo.get("employeeId"));
			itemJSONObject.put("password", oAddValueInfo.get("password"));
			itemJSONObject.put("memberValidThrough", oAddValueInfo.get("memberValidThrough"));
			itemJSONObject.put("svcMemberValidThrough", oAddValueInfo.get("svcMemberValidThrough"));
			itemJSONObject.put("issueDate", oAddValueInfo.get("issue_date"));
			itemJSONObject.put("cardIssueDT", oAddValueInfo.get("card_issue_dt"));
			itemJSONObject.put("cardIssueEmpID", oAddValueInfo.get("card_issue_emp_id"));
			itemJSONObject.put("cardSeller", oAddValueInfo.get("card_seller"));
			itemJSONObject.put("cardBuyerName", oAddValueInfo.get("card_buyer_name"));
			itemJSONObject.put("cardBuyerPassportID", oAddValueInfo.get("card_buyer_passport_id"));
			itemJSONObject.put("cardBuyerContactNumber", oAddValueInfo.get("card_buyer_contact_number"));
			itemJSONObject.put("memberType", oAddValueInfo.get("member_type"));
			itemJSONObject.put("assocMemberNo", oAddValueInfo.get("assoc_member_no"));
			itemJSONObject.put("firstSvcNumber", oAddValueInfo.get("first_svc_number"));
			itemJSONObject.put("cardHistory", oAddValueInfo.get("card_history"));
			itemJSONObject.put("creditDate", oAddValueInfo.get("credit_date"));
			itemJSONObject.put("remark", oAddValueInfo.get("remark"));
			
			requestJSONObject.put("items", itemJSONObject);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "loyaltySvcAddValueOrIssueCard", requestJSONObject.toString(), false))
			return null;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().has("svcBonusTransaction")){
				if (OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("svcBonusTransaction").has("errorCode") || 
						OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("svcBonusTransaction").has("errorType")){
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("svcBonusTransaction").optInt("errorCode");
					return null;
				}
			}
			// For GiveX
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("loyaltyResult")){
				if (OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorCode") || 
						OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")){
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
					return null;
				}
				return OmWsClientGlobal.g_oWsClient.get().getResponse();
			}
			return OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("svcBonusTransaction");
		}
	}
	
	// do Loyalty SVC Payment
	public Boolean doLoyaltySvcPayment(HashMap<String, String> oSvcPaymentInfo) {
		JSONObject requestJSONObject = new JSONObject();
		try{
			requestJSONObject.put("svcInterfaceId", oSvcPaymentInfo.get("svcInterfaceId"));
			requestJSONObject.put("outletId", oSvcPaymentInfo.get("outletId"));
			requestJSONObject.put("outletCode", oSvcPaymentInfo.get("outletCode"));
			requestJSONObject.put("svcSessionId", oSvcPaymentInfo.get("svcSessionId"));
			
			requestJSONObject.put("svcCardNumber", oSvcPaymentInfo.get("svcCardNumber"));
			requestJSONObject.put("traceId", oSvcPaymentInfo.get("traceId"));
			requestJSONObject.put("businessDate", oSvcPaymentInfo.get("businessDate"));
			requestJSONObject.put("bonusAmount", oSvcPaymentInfo.get("bonusAmount"));
			requestJSONObject.put("employeeId", oSvcPaymentInfo.get("employeeId"));
			requestJSONObject.put("password", oSvcPaymentInfo.get("password"));
			requestJSONObject.put("remark", oSvcPaymentInfo.get("remark"));
			
			//For auto top up from ledger account
			requestJSONObject.put("ledgerBonusAmount", oSvcPaymentInfo.get("ledgerBonusAmount"));
			requestJSONObject.put("ledgerNo", oSvcPaymentInfo.get("ledgerNo"));
			requestJSONObject.put("cardBalance", oSvcPaymentInfo.get("cardBalance"));
			requestJSONObject.put("topUpOutletCode", oSvcPaymentInfo.get("topUpOutletCode"));
			requestJSONObject.put("maxAllowedAmount", oSvcPaymentInfo.get("maxAllowedAmount"));
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "loyaltySVCPayment", requestJSONObject.toString(), false))
			return false;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().has("svcBonusTransaction")){
				if (OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("svcBonusTransaction").has("errorCode") || 
						OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("svcBonusTransaction").has("errorType")){
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("svcBonusTransaction").optInt("errorCode");
					return false;
				}
			}
			m_oLastSuccessResult = OmWsClientGlobal.g_oWsClient.get().getResponse();
			
			return true;
		}
	}
	
	// do loyalty SVC search card
	public JSONObject doLoyaltySvcSearchCard(JSONObject requestJSONObject){
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "loyalSvcSearchCard", requestJSONObject.toString(), false))
			return null;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorCode"))
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
		}
	}
	
	// do loyalty SVC suspend card
	public Boolean doLoyaltySvcSuspendCard(HashMap<String, String> oSuspendCardInfo) {
		JSONObject requestJSONObject = new JSONObject();
		try{
			requestJSONObject.put("svcInterfaceId", oSuspendCardInfo.get("svcInterfaceId"));
			requestJSONObject.put("outletId", oSuspendCardInfo.get("outletId"));
			requestJSONObject.put("outletCode", oSuspendCardInfo.get("outletCode"));
			requestJSONObject.put("svcSessionId", oSuspendCardInfo.get("svcSessionId"));
			
			requestJSONObject.put("svcCardNumber", oSuspendCardInfo.get("svcCardNumber"));
			requestJSONObject.put("cardStatus", oSuspendCardInfo.get("cardStatus"));
			requestJSONObject.put("cardSuspendDate", oSuspendCardInfo.get("cardSuspendDate"));
			requestJSONObject.put("cardSuspendRemarks", oSuspendCardInfo.get("cardSuspendRemarks"));
			requestJSONObject.put("cardSuspendEmpId", oSuspendCardInfo.get("cardSuspendEmpId"));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "loyaltySvcSuspendCard", requestJSONObject.toString(), false))
			return false;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorCode"))
				return false;
			
			return true;
		}
		
	}
	
	// do Loyalty SVC Transfer Card
	public Boolean doLoyaltySvcTransferCard(HashMap<String, String> oTransferCardInfo, HashMap<String, BigDecimal> oBalanceRecords) {
		JSONObject requestJSONObject = new JSONObject();

		try{
			requestJSONObject.put("interfaceId", oTransferCardInfo.get("interfaceId"));
			requestJSONObject.put("svcInterfaceId", oTransferCardInfo.get("svcInterfaceId"));
			requestJSONObject.put("outletId", oTransferCardInfo.get("outletId"));
			requestJSONObject.put("outletCode", oTransferCardInfo.get("outletCode"));
			requestJSONObject.put("sessionId", oTransferCardInfo.get("sessionId"));
			requestJSONObject.put("svcSessionId", oTransferCardInfo.get("svcSessionId"));

			if(oTransferCardInfo.containsKey("originalSvcNo"))
				requestJSONObject.put("originalSvcNo", oTransferCardInfo.get("originalSvcNo"));
			if(oTransferCardInfo.containsKey("destSvcNo"))
				requestJSONObject.put("destSvcNo", oTransferCardInfo.get("destSvcNo"));
			if(oTransferCardInfo.containsKey("cardIssueDate"))
				requestJSONObject.put("businessDateTime", oTransferCardInfo.get("cardIssueDate"));
			if(oTransferCardInfo.containsKey("cardIssueEmpID"))
				requestJSONObject.put("cardIssueEmpID", oTransferCardInfo.get("cardIssueEmpID"));
			if(oTransferCardInfo.containsKey("cardBuyerName"))
				requestJSONObject.put("cardBuyerName", oTransferCardInfo.get("cardBuyerName"));
			if(oTransferCardInfo.containsKey("cardBuyerPassportID"))
				requestJSONObject.put("cardBuyerPassportID", oTransferCardInfo.get("cardBuyerPassportID"));
			if(oTransferCardInfo.containsKey("cardBuyerContactNumber"))
				requestJSONObject.put("cardBuyerContactNumber", oTransferCardInfo.get("cardBuyerContactNumber"));
			if(oTransferCardInfo.containsKey("cardSeller"))
				requestJSONObject.put("cardSeller", oTransferCardInfo.get("cardSeller"));
			if(oTransferCardInfo.containsKey("memberValidThrough"))
				requestJSONObject.put("memberValidThrough", oTransferCardInfo.get("memberValidThrough"));
			if(oTransferCardInfo.containsKey("assocMemberNo"))
				requestJSONObject.put("assocMemberNo", oTransferCardInfo.get("assocMemberNo"));
			if(oTransferCardInfo.containsKey("transferReason"))
				requestJSONObject.put("transferReason", oTransferCardInfo.get("transferReason"));
			if(oTransferCardInfo.containsKey("cardTransferEmpID"))
				requestJSONObject.put("cardTransferEmpID", oTransferCardInfo.get("cardTransferEmpID"));
			if(oTransferCardInfo.containsKey("bonusBalance"))
				requestJSONObject.put("bonusBalance", oTransferCardInfo.get("bonusBalance"));
			if(oTransferCardInfo.containsKey("businessDate"))
				requestJSONObject.put("businessDate", oTransferCardInfo.get("businessDate"));
			if(oTransferCardInfo.containsKey("cardType"))
				requestJSONObject.put("cardType", oTransferCardInfo.get("cardType"));
			if(oTransferCardInfo.containsKey("loyaltyPoint"))
				requestJSONObject.put("loyaltyPoint", oTransferCardInfo.get("loyaltyPoint"));
			if(oTransferCardInfo.containsKey("printQueue"))
				requestJSONObject.put("printQueue", oTransferCardInfo.get("printQueue"));
			if(oTransferCardInfo.containsKey("languageIndex"))
				requestJSONObject.put("languageIndex", oTransferCardInfo.get("languageIndex"));
			JSONArray oBonusRecordsJson = new JSONArray();
			for(String sDate: oBalanceRecords.keySet()) {
				JSONObject oBonusJSONObject = new JSONObject();
				oBonusJSONObject.put("date", sDate);
				oBonusJSONObject.put("bonus", oBalanceRecords.get(sDate));
				oBonusRecordsJson.put(oBonusJSONObject);
			}
			requestJSONObject.put("bonusRecordsJsonArray", oBonusRecordsJson);

		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "loyaltySvcTransferCard", requestJSONObject.toString(), false))
			return false;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().has("svcUpdateProfileBatch1")){
				if (OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("svcUpdateProfileBatch1").has("errorCode") || 
						OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("svcUpdateProfileBatch1").has("errorType")){
						m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("svcUpdateProfileBatch1").optString("errorCode");
						return false;
				}
			}
			return true;
		}
	}
	
	// reverse the transaction of auto top-up of loyalty card and loyalty ledger account for release payment
	public JSONObject reverseLoyaltySvcAutoTopUpAndDeduction(HashMap<String, String> oPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		try{
			requestJSONObject.put("interfaceId", oPostingInfo.get("interfaceId"));
			requestJSONObject.put("svcInterfaceId", oPostingInfo.get("svcInterfaceId"));
			requestJSONObject.put("outletId", oPostingInfo.get("outletId"));
			requestJSONObject.put("outletCode", oPostingInfo.get("outletCode"));
			requestJSONObject.put("sessionId", oPostingInfo.get("sessionId"));
			requestJSONObject.put("svcSessionId", oPostingInfo.get("sessionId"));
			requestJSONObject.put("traceId", oPostingInfo.get("traceId"));
			requestJSONObject.put("businessDate", oPostingInfo.get("businessDate"));
			requestJSONObject.put("employeeId", oPostingInfo.get("employeeId"));
			requestJSONObject.put("autoTopUpInformation", oPostingInfo.get("autoTopUpInformation"));
			requestJSONObject.put("topUpOutletCode", oPostingInfo.get("topUpOutletCode"));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "loyaltySvcReverseAutoTopUpAndDeduction", requestJSONObject.toString(), false))
			return null;
		else {
			
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().has("svcBonusTransaction")){
				if (OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("svcBonusTransaction").has("errorCode") || 
						OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("svcBonusTransaction").has("errorType")){
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("svcBonusTransaction").optInt("errorCode");
					return null;
				}
			}
			// For GiveX
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("loyaltyResult")){
				if (OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorCode")){
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
					return null;
				}
			}
			//return OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("svcBonusTransaction");
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
		}
	}
	
	/*****************************/
	/***** 	 VGS Interface 	 *****/
	/*****************************/
	// do Vgs get PayUrl
	public JSONObject getVgsPayUrl(HashMap<String, String> oVgsGetPayUrlInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try{
			requestJSONObject.put("outletId", oVgsGetPayUrlInfo.get("outletId"));
			requestJSONObject.put("outletCode", oVgsGetPayUrlInfo.get("outletCode"));
			requestJSONObject.put("interfaceId", oVgsGetPayUrlInfo.get("interfaceId"));
			requestJSONObject.put("content", oVgsGetPayUrlInfo.get("content"));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "vgsGetPayUrl", requestJSONObject.toString(), false))
			return null;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorCode"))
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
		}
	}
	
	/********************************/
	/***** 	Inventory Interface *****/
	/********************************/
	// do Inventory interface add wastage
	public JSONObject doInventoryAddWastage(FuncInventoryInterface.PostingInfo oPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try{
			requestJSONObject.put("outletCode", oPostingInfo.sOutletCode);
			requestJSONObject.put("shopCode", oPostingInfo.sShopCode);
			requestJSONObject.put("interfaceId", oPostingInfo.iInterfaceId);
			requestJSONObject.put("itemId", oPostingInfo.sItemId);
			requestJSONObject.put("itemQty", oPostingInfo.dItemQty);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "inventoryAddWastage", requestJSONObject.toString(), false))
			return null;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorCode"))
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
		}
	}
	
	// do Inventory interface add sales
	
	public JSONObject doInventoryAddSales(JSONObject requestJSONObject) {
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "inventoryAddSales", requestJSONObject.toString(), false))
			return null;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorCode"))
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
		}
	}
	// do Inventory interface void Sales
	public JSONObject doInventoryVoidSales(FuncInventoryInterface.PostingInfo oPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try{
			requestJSONObject.put("outletCode", oPostingInfo.sOutletCode);
			requestJSONObject.put("shopCode", oPostingInfo.sShopCode);
			requestJSONObject.put("interfaceId", oPostingInfo.iInterfaceId);
			requestJSONObject.put("checkId", oPostingInfo.sCheckId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "inventoryVoidSales", requestJSONObject.toString(), false))
			return null;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorCode"))
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
		}
	}
	
	// do Inventory interface search item stock
	public JSONObject doInventorySearchItemStock(FuncInventoryInterface.PostingInfo oPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try{
			requestJSONObject.put("outletCode", oPostingInfo.sOutletCode);
			requestJSONObject.put("shopCode", oPostingInfo.sShopCode);
			requestJSONObject.put("interfaceId", oPostingInfo.iInterfaceId);
			requestJSONObject.put("itemId", oPostingInfo.sItemId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "inventorySearchItemStock", requestJSONObject.toString(), false))
			return null;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorCode"))
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
		}
	}
	
	/**********************************/
	/***** Survelliance Interface *****/
	/**********************************/
	// do FuncSurveillance posting
	public JSONObject doSurveillancePosting(JSONObject oSurveillancePostingInfo) {
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";

		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "surveillanceEvent", oSurveillancePostingInfo.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return null;
			}
							
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("postingResult") == 0) {
				m_sLastErrorType = "";
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return null;
			}
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse(); 
		}
	}
	
	//stop Survelliance shell
	public boolean stopSurveillanceShell(int iInterfaceId, String sVendorKey) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("interfaceId", iInterfaceId);
			requestJSONObject.put("vendorKey", sVendorKey);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "stopSurvellianceShell", requestJSONObject.toString(), false))
			return false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage").isEmpty())
				return true;
			
			return false;
		}
	}
	
	//restart Survelliance shell
	public boolean restartSurveillanceShell(int iInterfaceId, String sVendorKey) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("interfaceId", iInterfaceId);
			requestJSONObject.put("vendorKey", sVendorKey);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "restartSurveillanceShell", requestJSONObject.toString(), false))
			return false;
		else {
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("shellOpen")) {
				m_sLastErrorType = "i";
				m_iLastErrorCode = 5;
				m_sLastErrorMessage = "shell_is_not_alive";
				return false;
			} else if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("shellOpen") && OmWsClientGlobal.g_oWsClient.get().getResponse().optBoolean("shellOpen")) {
				m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return false;
			} else
				return true;
		}
	}
	
	/*****************************************/
	/*****	Galaxy Voucher Interface	******/
	/*****************************************/

	// Galaxy Voucher Enquiry
	public JSONObject voucherEnquiry(HashMap<String, String> oEnquiryInfo) {
		JSONObject requestJSONObject = new JSONObject();

		try {
			// put outletID, voucherNumber and interfaceID into JSON.
			requestJSONObject.put("outletId", oEnquiryInfo.get("outletId"));
			requestJSONObject.put("voucherNumber", oEnquiryInfo.get("voucherNumber"));
			requestJSONObject.put("interfaceId", this.intfId);
		} catch (JSONException e) {
			AppGlobal.stack2Log(e);
		}

		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "voucherEnquiry", requestJSONObject.toString(), false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			return null;
		} 
		else
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}

	// Galaxy Voucher posting
	public boolean doVoucherPosting(JSONObject oCheckInformationJSONObject, JSONObject oPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";

		try {
			// Put check info and postingInfo into JSON
			requestJSONObject.put("check", oCheckInformationJSONObject);
			requestJSONObject.put("postingInfo", oPostingInfo);
		} catch (JSONException e) {
			AppGlobal.stack2Log(e);
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "voucherPosting", requestJSONObject.toString(), false))
			return false;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				if (OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return false;
			}
			m_oLastSuccessResult = OmWsClientGlobal.g_oWsClient.get().getResponse();
			return true;
		}
	}
	
	public boolean doVoucherVoidPosting(JSONObject oPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";

		try {
			requestJSONObject.put("postingInfo", oPostingInfo);
		} catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "voucherVoidPosting", requestJSONObject.toString(),
				false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			return false;
		} else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")
					|| OmWsClientGlobal.g_oWsClient.get().getResponse().optBoolean("postingResult") == false) {
				m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType", "");
				m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return false;
			}
			m_oLastSuccessResult = OmWsClientGlobal.g_oWsClient.get().getResponse();
			return true;
		}
	}
	
	public void printInterfaceAlertSlip(int iPrtqId, String sCheckId, int iOutletId, boolean bResult, int iPaytype, String sErrorCode, String sErrorMessage, int iLangIndex) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("interfaceId", this.intfId);
			requestJSONObject.put("prtqId", iPrtqId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("checkId", sCheckId);
			requestJSONObject.put("paytype", iPaytype);
			if(bResult)
				requestJSONObject.put("result", 0);
			else
				requestJSONObject.put("result", 1);
			requestJSONObject.put("errorCode", sErrorCode);
			requestJSONObject.put("errorMessage", sErrorMessage);
			requestJSONObject.put("langIndex", iLangIndex);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "printInterfaceAlertSlip", requestJSONObject.toString(), false))
			return;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return; 
		}
		
		return;
	}
	
	public void printPaymentInterfaceAlertSlip(int iPrtqId, String sType, int iLangIndex, JSONObject oParamsJSON) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("prtqId", iPrtqId);
			requestJSONObject.put("type", sType);
			requestJSONObject.put("langIndex", iLangIndex);
			requestJSONObject.put("printInfo", oParamsJSON);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "printPaymentInterfaceSlip", requestJSONObject.toString(), false))
			return;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return; 
		}
		
		return;
	}
	
	//print special slip
	public boolean printSpecialSlip(String sType, JSONObject oHeader, JSONObject oInformation, int iCurrentLang, int iCitmId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("type", sType);
			requestJSONObject.put("checkId", "");
			if(iCitmId > 0)
				requestJSONObject.put("checkItemId", iCitmId);
			requestJSONObject.put("header", oHeader);
			requestJSONObject.put("info", oInformation);
			requestJSONObject.put("currentLang", iCurrentLang);
			
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "printSpecialSlip", requestJSONObject.toString(), true)) {
			return false;
		} else {
			return true;
		}
	}
	
	/********************************/
	/***** 	 Gaming Interface 	*****/
	/********************************/
	public JSONObject doGEMSGamingInterfacePosting(HashMap<String, String> oPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		JSONObject requestPostJSONObject = new JSONObject();
		String sPaymentType = "", sEnquiryType = "";
		
		String sPosFunction = "";
		if(oPostingInfo.containsKey("paymentType") && !oPostingInfo.get("paymentType").isEmpty())
			sPaymentType = oPostingInfo.get("paymentType");
		
		if(oPostingInfo.containsKey("enquiryType") && !oPostingInfo.get("enquiryType").isEmpty())
			sEnquiryType = oPostingInfo.get("enquiryType");
		
		if(sPaymentType.equals("") && sEnquiryType.equals(""))
			return null;
		
		switch (sEnquiryType) {
		case FuncGamingInterface.PATRON_INQUIRY:
			sPosFunction = "gamingPatronEnquiry";
			break;
		case FuncGamingInterface.EXECUTIVE_COMP_INQUIRY:
			sPosFunction = "gamingExecutiveCompInquiry";
			break;
		}
		
		switch (sPaymentType) {
		case FuncGamingInterface.POST_EXECUTIVE_COMP:
			sPosFunction = "gamingPostExecutiveComp";
			break;
		case FuncGamingInterface.POST_ONLINE_COMP:
			sPosFunction = "gamingPostOnlineComp";
			break;
		case FuncGamingInterface.COMP_REDEMPTION:
			sPosFunction = "gamingCompRedemption";
			break;
		case FuncGamingInterface.GIFT_CERTIFICATE_REDEMPTION:
			sPosFunction = "gamingGiftCertificateRedemption";
			break;
		case FuncGamingInterface.GIFT_CERTIFICATE_SALE:
			sPosFunction = "gamingGiftCertificateSale";
			break;
		case FuncGamingInterface.COUPON_REDEMPTION:
			sPosFunction = "gamingCouponRedemption";
			break;
		}
		
		try {
			//general information
			if(oPostingInfo.containsKey("outletCode") && !oPostingInfo.get("outletCode").isEmpty())
				requestJSONObject.put("outletCode", oPostingInfo.get("outletCode"));
			if(oPostingInfo.containsKey("stationCode") && !oPostingInfo.get("stationCode").isEmpty())
				requestJSONObject.put("stationCode", oPostingInfo.get("stationCode"));
			if(oPostingInfo.containsKey("employeeNum") && !oPostingInfo.get("employeeNum").isEmpty())
				requestJSONObject.put("employeeNum", oPostingInfo.get("employeeNum"));
			if(oPostingInfo.containsKey("checkNum") && !oPostingInfo.get("checkNum").isEmpty())
				requestJSONObject.put("checkNumber", oPostingInfo.get("checkNum"));
			
			//optional information
			if(oPostingInfo.containsKey("patronNum"))
				requestJSONObject.put("memberNumber", oPostingInfo.get("patronNum"));
			if(oPostingInfo.containsKey("cardNo"))
				requestJSONObject.put("memberCardNumber", oPostingInfo.get("cardNo"));
			if(oPostingInfo.containsKey("inputMethod") && !oPostingInfo.get("inputMethod").isEmpty())
				requestJSONObject.put("inputMethod", oPostingInfo.get("inputMethod"));
			if(oPostingInfo.containsKey("settleAmount") && !oPostingInfo.get("settleAmount").isEmpty())
				requestJSONObject.put("postAmount", oPostingInfo.get("settleAmount"));
			if(oPostingInfo.containsKey("interfaceId") && !oPostingInfo.get("interfaceId").isEmpty())
				requestJSONObject.put("interfaceId", oPostingInfo.get("interfaceId"));
			if(oPostingInfo.containsKey("interfaceCode") && !oPostingInfo.get("interfaceCode").isEmpty())
				requestJSONObject.put("interfaceCode", oPostingInfo.get("interfaceCode"));
			if(oPostingInfo.containsKey("compNum") && !oPostingInfo.get("compNum").isEmpty())
				requestJSONObject.put("userNumber", oPostingInfo.get("compNum"));
			if(oPostingInfo.containsKey("couponNumber") && !oPostingInfo.get("couponNumber").isEmpty())
				requestJSONObject.put("couponNumber", oPostingInfo.get("couponNumber"));
			if(oPostingInfo.containsKey("staffId") && !oPostingInfo.get("staffId").isEmpty())
				requestJSONObject.put("executiveStaffId", oPostingInfo.get("staffId"));
			if(oPostingInfo.containsKey("numOfPatrons") && !oPostingInfo.get("numOfPatrons").isEmpty())
				requestJSONObject.put("numOfPatrons", oPostingInfo.get("numOfPatrons"));
			if(oPostingInfo.containsKey("numOfEmployee") && !oPostingInfo.get("numOfEmployee").isEmpty())
				requestJSONObject.put("numOfEmployee", oPostingInfo.get("numOfEmployee"));
			if(oPostingInfo.containsKey("reasonCode") && !oPostingInfo.get("reasonCode").isEmpty())
				requestJSONObject.put("reasonCode", oPostingInfo.get("reasonCode"));
			
			if(oPostingInfo.containsKey("giftCertId") && !oPostingInfo.get("giftCertId").isEmpty())
				requestJSONObject.put("giftCertificateId", oPostingInfo.get("giftCertId"));
			if(oPostingInfo.containsKey("securityCode") && !oPostingInfo.get("securityCode").isEmpty())
				requestJSONObject.put("securityCode", oPostingInfo.get("securityCode"));
			
			if(oPostingInfo.containsKey("dateOfBirth") && !oPostingInfo.get("dateOfBirth").isEmpty())
				requestJSONObject.put("dateOfBirth", oPostingInfo.get("dateOfBirth"));
			
			requestPostJSONObject.put("postingInfo", requestJSONObject);
			
			m_sLastErrorMessage = "";
		}catch(JSONException jsone) {
				jsone.printStackTrace();
		}

		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", sPosFunction, requestPostJSONObject.toString(), false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
				return null;
		} 
		return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	public JSONObject doGamingInterfaceCardEnquiry(HashMap<String, String> oEnquiryInfo) {
		JSONObject requestJSONObject = new JSONObject();
		JSONObject requestPostJSONObject = new JSONObject();

		try {
			//general information
			if(oEnquiryInfo.containsKey("interfaceId"))
				requestJSONObject.put("interfaceId", oEnquiryInfo.get("interfaceId"));
			if(oEnquiryInfo.containsKey("outletCode"))
				requestJSONObject.put("outletCode", oEnquiryInfo.get("outletCode"));
			if(oEnquiryInfo.containsKey("stationCode"))
				requestJSONObject.put("stationCode", oEnquiryInfo.get("stationCode"));
			if(oEnquiryInfo.containsKey("timeZoneOffset"))
				requestJSONObject.put("timeZoneOffset", oEnquiryInfo.get("timeZoneOffset"));

			//optional information
			if(oEnquiryInfo.containsKey("inputCardId") && !oEnquiryInfo.get("inputCardId").isEmpty())
				requestJSONObject.put("inputCardId", oEnquiryInfo.get("inputCardId"));
			if(oEnquiryInfo.containsKey("inputMethod") && !oEnquiryInfo.get("inputMethod").isEmpty())
				requestJSONObject.put("inputMethod", oEnquiryInfo.get("inputMethod"));

			requestPostJSONObject.put("enquiryInfo", requestJSONObject);

		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "gamingInterfaceCardEnquiry", requestPostJSONObject.toString(), false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			return null;
		}
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().optBoolean("enquiryResult") == false) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return null;
			}
		}
		
		return OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("resultDetails");
	}
	
	//comp slip enquiry
	public JSONObject doGamingInterfaceCompSlipEnquiry(HashMap<String, String> oEnquiryInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			//general information
			String sIntfId = (this.intfId > 0)? String.valueOf(this.intfId) : "";
			if(oEnquiryInfo.containsKey("interfaceId"))
				sIntfId = oEnquiryInfo.get("interfaceId");
			requestJSONObject.put("interfaceId", sIntfId);
			if(oEnquiryInfo.containsKey("compSlipNumber"))
				requestJSONObject.put("compSlipNumber", oEnquiryInfo.get("compSlipNumber"));
			if(oEnquiryInfo.containsKey("outletCode"))
				requestJSONObject.put("outletCode", oEnquiryInfo.get("outletCode"));
			if(oEnquiryInfo.containsKey("stationCode"))
				requestJSONObject.put("stationCode", oEnquiryInfo.get("stationCode"));
			if(oEnquiryInfo.containsKey("timeZoneOffset"))
				requestJSONObject.put("timeZoneOffset", oEnquiryInfo.get("timeZoneOffset"));
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "gamingInterfaceCompSlipEnquiry", requestJSONObject.toString(), false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			return null;
		}else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().optBoolean("enquiryResult") == false) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return null;
			}
		}
		
		return OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("resultDetails");
	}
	
	// do bally gaming interface compslip posting
	public boolean gamingCompSlipPosting(JSONObject oCheckInformation, JSONObject oMemberPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("check", oCheckInformation);
			requestJSONObject.put("postingInfo", oMemberPostingInfo);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "gamingCompSlipPosting", requestJSONObject.toString(), false))
			return false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult") && !OmWsClientGlobal.g_oWsClient.get().getResponse().optBoolean("postingResult")) {
				if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorCode")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				} else {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return false;
			}
			
			m_oLastSuccessResult = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("resultDetails");
			
			return true; 
		}
	}
	
	// do bally gaming interface redeem dollar posting
	public boolean gamingRedeemDollarPosting(JSONObject oCheckInformation, JSONObject oMemberPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("check", oCheckInformation);
			requestJSONObject.put("postingInfo", oMemberPostingInfo);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "gamingRedeemDollarPosting", requestJSONObject.toString(), false))
			return false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult") && !OmWsClientGlobal.g_oWsClient.get().getResponse().optBoolean("postingResult")) {
				if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorCode")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				} else {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return false;
			}
			
			m_oLastSuccessResult = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("resultDetails");
			
			return true; 
		}
	}
	
	// do void gaming interface posting
	public boolean doVoidGamingPosting(JSONObject oPostingJSONObject) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		try {
			requestJSONObject.put("postingInfo", oPostingJSONObject);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "gamingVoidPosting", requestJSONObject.toString(), false))
			return false;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null || !OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult"))
				return false;

			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult") && !OmWsClientGlobal.g_oWsClient.get().getResponse().optBoolean("postingResult")) {
				m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorCode"))
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				return false;
			}
			
			m_oLastSuccessResult = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("resultDetails");
			return true;
		}
	}
	
	public JSONObject doGamingSalesDetailPost(JSONObject oCheckInformation, JSONObject oPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_iLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("check", oCheckInformation);
			requestJSONObject.put("postingInfo", oPostingInfo);
		} catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "gamingPostSaleDetail", requestJSONObject.toString(),
				false)) {
			m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			return null;
		} else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")
					|| OmWsClientGlobal.g_oWsClient.get().getResponse().optBoolean("postingResult") == false) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType", "");
					m_iLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return null;
			}
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
		}
	}
	
	// init value
	public void init() {
		this.pifcId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.statId = 0;
		this.intfId = 0;
		this.by = "";
		this.recordId = 0;
		this.value = null;
		this.status = PosInterfaceConfig.STATUS_ACTIVE;
		
		m_oInfInterface = null;
	}
	
	public int getInterfaceId() {
		return this.intfId;
	}
	
	public JSONObject getConfigValue() {
		JSONObject oConfigValue = null;
		
		if(this.value != null) {
			try {
				oConfigValue = new JSONObject(this.value);
			}catch (JSONException jsone) {
				jsone.printStackTrace();
			}
		}
		
		return oConfigValue;
	}
	
	public int getRecordId() {
		return this.recordId;
	}
	
	public String getInterfaceType() {
		return this.m_oInfInterface.getInterfaceType();
	}
	
	public String getInterfaceName(int iIndex) {
		return this.m_oInfInterface.getName(iIndex);
	}
	
	public String getInterfaceVendorKey() {
		return this.m_oInfInterface.getVendorKey();
	}
	
	public String getInterfaceCode() {
		return this.m_oInfInterface.getInterfaceCode();
	}
	
	public JSONObject getInterfaceConfig() {
		return this.m_oInfInterface.getSetting();
	}
	
	public int getInterfaceExpiryTime() {
		return this.m_oInfInterface.getExpiryTime();
	}
	
	public int getInterfaceRetryTimes() {
		return this.m_oInfInterface.getRetryTimes();
	}
	
	public int getInterfaceRetryDelay() {
		return this.m_oInfInterface.getRetryDelay();
	}
	
	public String getLastErrorType() {
		return m_sLastErrorType;
	}
	
	public int getLastErrorCode() {
		return m_iLastErrorCode;
	}
	
	public String getLastErrorMessage() {
		return m_sLastErrorMessage;
	}
	
	public JSONObject getLastSuccessResult() {
		return m_oLastSuccessResult;
	}
	
	public boolean isInfInterfaceNull() {
		if(m_oInfInterface == null)
			return true;
		else
			return false;
	}
}