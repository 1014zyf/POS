package om;

import org.json.JSONObject;

public class InfVendor {
	private int ivdrId;
	private String type;
	private String name[];
	private int seq;
	private String key;
	private String status;
	
	public static final String TYPE_PMS = "pms";
	public static final String KEY_R8 = "r8";
	public static final String KEY_STANDARD_TCPIP = "standard_tcpip";
	public static final String KEY_4700_TCPIP = "4700_tcpip";
	public static final String KEY_4700_SERIAL_PORT = "4700_serial_port";
	public static final String KEY_HTNG = "htng";
	public static final String KEY_PEGASUS = "pegasus";
	public static final String KEY_XMS = "xms";
	
	public static final String TYPE_PERIPHERAL_DEVICE = "peripheral_device";
	public static final String KEY_DEVICE_MANAGER = "device_manager";
	public static final String KEY_MSR = "msr";
	
	public static final String TYPE_PAYMENT_INTERFACE = "payment_interface";
	public static final String KEY_OGS = "ogs";
	public static final String KEY_PAY_AT_TABLE = "pay_at_table";
	public static final String KEY_SCAN_PAY = "scan_pay";
	public static final String KEY_VGS_STANDARD = "vgs_standard";
	
	public static final String TYPE_MEMBERSHIP_INTERFACE = "membership_interface";
	public static final String KEY_LPS_SVC = "lps_svc";
	public static final String KEY_LPS_SVC_COUPON = "lps_svc_coupon";
	public static final String KEY_GENERAL = "general";
	public static final String KEY_ASCENTIS_CRM = "ascentis_crm";
	public static final String KEY_EPOS = "epos";
	public static final String KEY_RESPAK = "respak";
	public static final String KEY_ASPEN = "aspen_xml";
	public static final String KEY_ASPEN_PMS = "aspen_pms_xml";
	public static final String KEY_YAZUO_CRM = "yazuo_crm";
	public static final String KEY_2700 = "2700";
	public static final String KEY_CONCEPT = "concept";
	public static final String KEY_VIENNA_CRM = "vienna_crm";
	public static final String TYPE_LOYALTY_INTERFACE = "loyalty_interface";
	public static final String KEY_GM_LOYALTY = "gm_loyalty";
	public static final String KEY_GM_LOYALTY_SVC = "gm_loyalty_svc";
	public static final String KEY_GIVEX = "givex";
	public static final String KEY_SPA_STANDARD = "spa_standard";
	public static final String KEY_GOLDEN_CIRCLE = "golden_circle";
	public static final String KEY_SMART_INTEGRAL = "smart_integral";
	public static final String KEY_CHINETEK = "chinetek";
	public static final String KEY_HUARUNTONG = "huaruntong";
	public static final String KEY_GENERAL_V2 = "general_v2";
	public static final String KEY_SPC = "spc";
	public static final String TYPE_INVENTORY_INTERFACE = "inventory_interface";
	public static final String KEY_COOKING_THE_BOOK = "cooking_the_book";
	
	public static final String TYPE_SURVEILLANCE_INTERFACE = "surveillance_interface";
	public static final String KEY_ECONNECT = "econnect";
	
	public static final String TYPE_GAMING_INTERFACE = "gaming_interface";
	public static final String KEY_GEMS = "gems";
	public static final String KEY_BALLY = "bally";
	public static final String KEY_SJM = "sjm";
	
	public static final String TYPE_VOUCHER_INTERFACE = "voucher_interface";
	public static final String KEY_GALAXY = "galaxy";
	
	// status
	public static final String STATUS_ACTIVE = "";
	public static final String STATUS_DELETED = "d";
	
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
	
	protected String getKey() {
		return this.key;
	}
}
