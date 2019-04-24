package app.model;

import org.json.JSONObject;

public class InfVendor {
	private int ivdrId;
	private String type;
	private String name[];
	private int seq;
	private String key;
	private String status;
	
	public static String TYPE_PMS = "pms";
	public static String KEY_R8 = "r8";
	public static String KEY_STANDARD_TCPIP = "standard_tcpip";
	public static String KEY_4700_TCPIP = "4700_tcpip";
	public static String KEY_4700_SERIAL_PORT = "4700_serial_port";
	
	public static String TYPE_PERIPHERAL_DEVICE = "peripheral_device";
	public static String KEY_DEVICE_MANAGER = "device_manager";
	public static String KEY_MSR = "msr";
	
	public static String TYPE_PAYMENT_INTERFACE = "payment_interface";
	public static String KEY_OGS = "ogs";
	public static String KEY_PAY_AT_TABLE = "pay_at_table";
	
	public static String TYPE_MEMBERSHIP_INTERFACE = "membership_interface";
	public static String KEY_LPS_SVC = "lps_svc";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	public InfVendor() {
		this.init();
	}
	
	public InfVendor(JSONObject interfaceJSONObject) {
		int i=0;
		
		if(interfaceJSONObject.has("InfVendor"))
			interfaceJSONObject = interfaceJSONObject.optJSONObject("InfVendor");
		
		this.init();
		
		this.ivdrId = interfaceJSONObject.optInt("vdor_id");
		this.type = interfaceJSONObject.optString("vdor_type");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = interfaceJSONObject.optString("vdor_name_l"+i);
		this.seq = interfaceJSONObject.optInt("vdor_seq");
		this.key = interfaceJSONObject.optString("vdor_key");
		this.status = interfaceJSONObject.optString("vdor_status");
		
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.ivdrId = 0;
		this.type = "";
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		this.seq = 0;
		this.key = "";
		this.status = InfVendor.STATUS_ACTIVE;
	}
	
	public String getKey() {
		return this.key;
	}
}
