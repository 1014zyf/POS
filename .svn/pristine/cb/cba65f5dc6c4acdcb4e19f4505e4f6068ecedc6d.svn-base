package app.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	private int m_sLastErrorCode;
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
	
	/*************************/
	/***** PMS Interface *****/
	/*************************/
	// do PMS prepost
	public boolean doPmsPrepost(JSONObject oCheckInformation, JSONObject oPMSPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_sLastErrorCode = 0;
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
				m_sLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return false;
			}
			
			m_oLastSuccessResult = OmWsClientGlobal.g_oWsClient.get().getResponse();
			
			return true;
		}
	}
	
	// do PMS posting
	public boolean doPmsPosting(JSONObject oCheckInformation, JSONObject oPMSPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_sLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("check", oCheckInformation);
			requestJSONObject.put("postingInfo", oPMSPostingInfo);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "pmsPosting", requestJSONObject.toString(), false))
			return false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_sLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return false;
			}
							
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("postingResult") == 0) {
				m_sLastErrorType = "";
				m_sLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return false;
			}
			
			return true; 
		}
	}
	
	// do PMS posting
	public boolean doPmsVoidPosting(JSONObject oCheckInformation, JSONObject oPMSPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_sLastErrorCode = 0;
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
				m_sLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
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
		m_sLastErrorCode = 0;
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
				m_sLastErrorCode = 5;
				m_sLastErrorMessage = "shell_is_not_alive";
				return false;
			} else if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("shellOpen") && OmWsClientGlobal.g_oWsClient.get().getResponse().optBoolean("shellOpen")) {
				m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
				m_sLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return false;
			} else
				return true;
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
				m_sLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return false;
			} else
				return true;
		}
	}
	
	//restart payment interface shell
	public boolean restartPaymentInterfaceShell(int iInterfaceId) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_sLastErrorCode = 0;
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
				m_sLastErrorCode = 5;
				m_sLastErrorMessage = "shell_is_not_alive";
				return false;
			 }else if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("result") && OmWsClientGlobal.g_oWsClient.get().getResponse().optBoolean("result")) {
				m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
				m_sLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
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


	/********************************/
	/***** Membership Interface *****/
	/********************************/
	public JSONObject svcEnquiry(int iInterfaceId, String sEnqValue, String sPassword, int iCheckId, int iOutletId, String sOutletCode, String sStationCode, String sEmployeeNum) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("interfaceId", iInterfaceId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("outletCode", sOutletCode);
			requestJSONObject.put("stationCode", sStationCode);
			requestJSONObject.put("employeeNum", sEmployeeNum);
			requestJSONObject.put("checkId", iCheckId);
			requestJSONObject.put("enqValue", sEnqValue);
			requestJSONObject.put("password", sPassword);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "svcEnquiry", requestJSONObject.toString(), false))
			return null;
		else
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}

	// do LPS SVC posting
	public boolean doSvcPosting(JSONObject oCheckInformation, JSONObject oPMSPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_sLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("check", oCheckInformation);
			requestJSONObject.put("postingInfo", oPMSPostingInfo);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "svcPosting", requestJSONObject.toString(), false))
			return false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("errorMessage")) {
					m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
					m_sLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
					m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				}
				return false;
			}
							
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("postingResult") == 0) {
				m_sLastErrorType = "";
				m_sLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return false;
			}
			
			m_oLastSuccessResult = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("resultDetails");
			
			return true; 
		}
	}
	
	// do LPS SVC posting
	public boolean doSvcVoidPosting(JSONObject oCheckInformation, JSONObject oSVCPostingInfo) {
		JSONObject requestJSONObject = new JSONObject();
		m_sLastErrorType = "i";
		m_sLastErrorCode = 0;
		m_sLastErrorMessage = "";
		
		try {
			requestJSONObject.put("check", oCheckInformation);
			requestJSONObject.put("postingInfo", oSVCPostingInfo);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "svcVoidPosting", requestJSONObject.toString(), false))
			return false;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null || !OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult"))
				return false;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("postingResult") == 0) {
				m_sLastErrorType = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorType");
				m_sLastErrorCode = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("errorCode");
				m_sLastErrorMessage = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("errorMessage");
				return false;
			}

			return true;
		}
	}
	
	public void printInterfaceAlertSlip(int iPrtqId, int iCheckId, int iOutletId, boolean bResult, int iPaytype, String sErrorCode, String sErrorMessage, int iLangIndex) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("interfaceId", this.intfId);
			requestJSONObject.put("prtqId", iPrtqId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("checkId", iCheckId);
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
	
	public String getInterfaceType() {
		return this.m_oInfInterface.getInterfaceType();
	}
	
	public String getInterfaceName(int iIndex) {
		return this.m_oInfInterface.getName(iIndex);
	}
	
	public String getInterfaceVendorKey() {
		return this.m_oInfInterface.getVendorKey();
	}
	
	public JSONObject getInterfaceConfig() {
		return this.m_oInfInterface.getSetting();
	}

	public String getLastErrorType() {
		return m_sLastErrorType;
	}
	
	public int getLastErrorCode() {
		return m_sLastErrorCode;
	}
	
	public String getLastErrorMessage() {
		return m_sLastErrorMessage;
	}
	
	public JSONObject getLastSuccessReulst() {
		return m_oLastSuccessResult;
	}
	
	public boolean isInfInterfaceNull() {
		if(m_oInfInterface == null)
			return true;
		else
			return false;
	}
}
