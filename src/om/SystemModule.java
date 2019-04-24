package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SystemModule {
	private int moduId;
	private String alias;
	private String code;
	private String[] name;
	private String[] desc;
	private String version;
	private String registerTime;
	private String deactivateTime;
	private String interfaceBaseURL;
	private int interfacePort;
	private String status;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	public SystemModule() {
		this.init();
	}
	
	public SystemModule(JSONObject configJSONObject) {
		this.readDataFromJson(configJSONObject);
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("module")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("module")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("module");
			if (tempJSONObject.isNull("SysModule")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}
	
	//read data from POS API
	protected JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray sysConfigJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("modules")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("modules"))
				return null;
			
			sysConfigJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("modules");
		}
		
		return sysConfigJSONArray;
	}
	
	public boolean readByAlias(String sAlias) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			if(sAlias.equals("pos_interface"))
				requestJSONObject.put("alias", "interface");
			else
				requestJSONObject.put("alias", sAlias);			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "system", "getModuleByAlias", requestJSONObject.toString());
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject systemModuleJSONObject) {
		JSONObject resultModule = null;
		int i;
		
		resultModule = systemModuleJSONObject.optJSONObject("SysModule");
		if(resultModule == null)
			resultModule = systemModuleJSONObject;
			
		this.init();

		this.moduId = resultModule.optInt("modu_id");
		this.alias = resultModule.optString("modu_alias");
		this.code = resultModule.optString("modu_code");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultModule.optString("modu_name_l"+i);
		for(i=1; i<=5; i++) {
			this.desc[(i-1)] = resultModule.optString("modu_desc_l"+i);
		}
		this.version = resultModule.optString("modu_version");
		this.registerTime = resultModule.optString("modu_register_time");
		this.deactivateTime = resultModule.optString("modu_deactivate_time");
		this.interfaceBaseURL = resultModule.optString("modu_interface_base_url");
		this.interfacePort = resultModule.optInt("modu_interface_port");
		this.status = resultModule.optString("modu_status", SystemModule.STATUS_ACTIVE);
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.moduId = 0;
		this.alias = "";
		this.code = "";
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.desc == null)
			this.desc = new String[5];
		for(i=0; i<5; i++)
			this.desc[i] = null;
		this.version = "";
		this.registerTime = null;
		this.deactivateTime = null;
		this.interfaceBaseURL = "";
		this.interfacePort = 0;
		this.status = SystemModule.STATUS_ACTIVE; 
	}

	//get modu id
	public int getModuId() {
		return this.moduId;
	}
	
	//get alias
	public String getAlias() {
		return this.alias;
	}
	
	//get code
	protected String getCode() {
		return this.code;
	}
	
	//get name by lang index
	protected String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get desc by lang index
	protected String getDesc(int iIndex) {
		return this.desc[(iIndex-1)];
	}
	
	//get version
	protected String getVersion() {
		return this.version;
	}
	
	//get register time
	protected String getRegisterTime() {
		return this.registerTime;
	}
	
	//get deactivate time
	protected String getDeactivateTime() {
		return this.deactivateTime;
	}
	
	//get interface base URL
	protected String getIntergaceBaseURL() {
		return this.interfaceBaseURL;
	}
	
	//get interface port
	protected int getIntergacePort() {
		return this.interfacePort;
	}
	
	//get status
	protected String getStatus() {
		return this.status;
	}
	
}
