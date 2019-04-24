package app.model;

import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

public class InfInterface {
	private int intfId;
	private String type;
	private int ivdrId;
	private String name[];
	private int seq;
	private String settings;
	private String status;
	
	private InfVendor m_oInfVendor;
	
	// type
	public static String TYPE_PMS = "pms";
	public static String TYPE_PERIPHERAL_DEVICE = "peripheral_device";
	public static String TYPE_PAYMENT_INTERFACE = "payment_interface";
	public static String TYPE_MEMBERSHIP_INTERFACE = "membership_interface";
	public static String TYPE_EMAIL = "email";
	public static String TYPE_SMS = "sms";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	public static String STATUS_SUSPENDED = "s";
	
	public InfInterface() {
		this.init();
	}
	
	public InfInterface(JSONObject interfaceJSONObject) {
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
		for(i=1; i<=5; i++) {
			if(!interfaceJSONObject.isNull("intf_name_l"+i))
				this.name[(i-1)] = interfaceJSONObject.optString("intf_name_l"+i);
		}
		this.seq = interfaceJSONObject.optInt("intf_seq");
		if(!interfaceJSONObject.isNull("intf_settings"))
			this.settings = interfaceJSONObject.optString("intf_settings");
		this.status = interfaceJSONObject.optString("intf_status");
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
	
	// init value
	public void init() {
		int i=0;
		
		this.intfId = 0;
		this.type = "";
		this.ivdrId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		this.seq = 0;
		this.settings = null;
		this.status = InfInterface.STATUS_ACTIVE;
		
		m_oInfVendor = new InfVendor();
	}
	
	protected int getInterfaceId() {
		return this.intfId;
	}
	
	protected String getInterfaceType() {
		return this.type;
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
}
