package om;

import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

public class InfInterface {
	private int intfId;
	private String type;
	private String sCode;
	private int ivdrId;
	private String name[];
	private int seq;
	private String settings;
	private int expiryTime;
	private int retryTimes;
	private int retryDelay;
	private String status;
	
	private InfVendor m_oInfVendor;
	
	// type
	public static String TYPE_PMS = "pms";
	public static String TYPE_PERIPHERAL_DEVICE = "peripheral_device";
	public static String TYPE_PAYMENT_INTERFACE = "payment_interface";
	public static String TYPE_MEMBERSHIP_INTERFACE = "membership_interface";
	public static String TYPE_PORTAL_INTERFACE = "portal_interface";
	public static String TYPE_INVENTORY_INTERFACE = "inventory_interface";
	public static String TYPE_SURVEILLANCE_INTERFACE = "surveillance_interface";
	public static String TYPE_GAMING_INTERFACE = "gaming_interface";
	public static String TYPE_VOUCHER_INTERFACE = "voucher_interface";
	
	public static String TYPE_EMAIL = "email";
	public static String TYPE_SMS = "sms";
	public static String TYPE_TMS = "tms";
	public static String TYPE_LOYALTY_INTERFACE = "loyalty_interface";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	public static String STATUS_SUSPENDED = "s";
	
	public InfInterface() {
		this.init();
	}
	
	public InfInterface(JSONObject interfaceJSONObject) {
		readDataFromJson(interfaceJSONObject);
	}
	
	public JSONObject pmsEnquiry(HashMap<String, String> oEnquiryInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		// from the request information
		try {
			for(Entry<String, String> oEntry:oEnquiryInfo.entrySet()) 
				requestJSONObject.put(oEntry.getKey(), oEntry.getValue());
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "pmsEnquiry", requestJSONObject.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("enquiryResult")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("enquiryResult")) {
				return null;
			}
		}
		
		return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	public JSONObject paymentInterfacePosting(JSONObject oPostingInfoJSONObject) {
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "paymentInterfacePosting", oPostingInfoJSONObject.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("postingResult")) {
				return null;
			}
		}
		
		return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	public JSONObject paymentInterfaceVoidPosting(JSONObject oPostingInfoJSONObject) {
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "paymentInterfaceVoidPosting", oPostingInfoJSONObject.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("postingResult")) {
				return null;
			}
		}
		
		return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}

	public JSONObject paymentInterfaceTip(JSONObject oPostingInfoJSONObject) {
				if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "paymentInterfaceTip", oPostingInfoJSONObject.toString(), false))
					return null;
				else {
					if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
						return null;
					if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
						OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
						return null;
					}
					
					if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("postingResult")) {
						return null;
					}
				}
				
				return OmWsClientGlobal.g_oWsClient.get().getResponse();
			}

	
	//get interface by code
	public boolean getInterfaceByCode(String sCode) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("code", sCode);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "interface", "getInterfaceByCode", requestJSONObject.toString());
	}
	
	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bResult = true;
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bResult = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("interface")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}

			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("interface");
			if(tempJSONObject.isNull("InfInterface")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}
	
	// card authorization
	public JSONObject paymentInterfaceCardAuth(JSONObject oPostingInfoJSONObject) {
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "paymentInterfaceCardAuthorization", oPostingInfoJSONObject.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("postingResult"))
				return null;
		}
		return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	// card top-up authorization
	public JSONObject paymentInterfaceCardTopupAuth(JSONObject oPostingInfoJSONObject) {
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "paymentInterfaceCardTopUpAuth", oPostingInfoJSONObject.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("postingResult"))
				return null;
		}
		return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	// void card top-up authorization
	public JSONObject paymentInterfaceVoidTopupAuth(JSONObject oPostingInfoJSONObject) {
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "paymentInterfaceVoidCardTopUpAuth", oPostingInfoJSONObject.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("postingResult"))
				return null;
		}
		return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	// void card authorization
	public JSONObject paymentInterfaceVoidCardAuth(JSONObject oPostingInfoJSONObject) {
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "paymentInterfaceVoidCardAuth", oPostingInfoJSONObject.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("postingResult"))
				return null;
		}
		return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	//void card complete authorization
	public JSONObject paymentInterfaceVoidCompleteAuth(JSONObject oPostingInfoJSONObject) {
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "paymentInterfaceVoidCompleteAuth", oPostingInfoJSONObject.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("postingResult"))
				return null;
		}
		return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	//card complete authorization
	public JSONObject paymentInterfaceCompleteAuth(JSONObject oPostingInfoJSONObject) {
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "interface", "paymentInterfaceCompleteAuthorization", oPostingInfoJSONObject.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("postingResult")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("postingResult"))
				return null;
		}
		return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.intfId = 0;
		this.type = "";
		this.sCode = "";
		this.ivdrId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		this.seq = 0;
		this.settings = null;
		this.expiryTime = 0;
		this.retryTimes = 0;
		this.retryDelay = 0;
		this.status = InfInterface.STATUS_ACTIVE;
		
		m_oInfVendor = new InfVendor();
	}
	
	public void readDataFromJson(JSONObject interfaceJSONObject) {
		int i=0;
		JSONObject oInterfaceVendorJSONObject = new JSONObject();
		
		this.init();
		
		if(interfaceJSONObject.has("InfVendor")) {
			oInterfaceVendorJSONObject = interfaceJSONObject.optJSONObject("InfVendor");
			m_oInfVendor = new InfVendor(oInterfaceVendorJSONObject);
		}
		
		if(interfaceJSONObject.has("InfInterface"))
			interfaceJSONObject = interfaceJSONObject.optJSONObject("InfInterface");
	
		this.intfId = interfaceJSONObject.optInt("intf_id");
		this.type = interfaceJSONObject.optString("intf_type");
		this.ivdrId = interfaceJSONObject.optInt("intf_vdor_id");
		this.sCode = interfaceJSONObject.optString("intf_code");
		for(i=1; i<=5; i++) {
			if(!interfaceJSONObject.isNull("intf_name_l"+i))
				this.name[(i-1)] = interfaceJSONObject.optString("intf_name_l"+i);
		}
		this.seq = interfaceJSONObject.optInt("intf_seq");
		if(!interfaceJSONObject.isNull("intf_settings"))
			this.settings = interfaceJSONObject.optString("intf_settings");
		this.expiryTime = interfaceJSONObject.optInt("intf_expiry_time", 0);
		this.retryTimes = interfaceJSONObject.optInt("intf_retry_times", 0);
		this.retryDelay = interfaceJSONObject.optInt("intf_retry_delay", 0);
		this.status = interfaceJSONObject.optString("intf_status");
	}
	
	public int getInterfaceId() {
		return this.intfId;
	}
	
	protected String getInterfaceType() {
		return this.type;
	}
	
	public String getInterfaceCode() {
		return this.sCode;
	}
	
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	public String getVendorKey() {
		return this.m_oInfVendor.getKey();
	}
	
	public JSONObject getSetting() {
		if(this.settings == null)
			return null;
		else {
			JSONObject oSettingJSONObject = null;
			try {
				oSettingJSONObject = new JSONObject(this.settings);
				return oSettingJSONObject;
			}catch(JSONException jsone) {
				return null;
			}
		}
	}
	
	public int getExpiryTime() {
		return this.expiryTime;
	}
	
	public int getRetryTimes() {
		return this.retryTimes;
	}
	
	public int getRetryDelay() {
		return this.retryDelay;
	}
}
