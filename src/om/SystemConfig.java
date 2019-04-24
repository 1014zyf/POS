package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SystemConfig {
	private int scfgId;
	private String section;
	private String variable;
	private int index;
	private String value;
	private String remark;
	
	public SystemConfig() {
		this.init();
	}
	
	public SystemConfig(JSONObject systemConfigJSONObject) {
		this.readDataFromJson(systemConfigJSONObject);
	}
	
	//read data from POS API
	protected JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray sysConfigJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("configs")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("configs")) {
				this.init();
				return null;
			}
			
			sysConfigJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("configs");
		}
		
		return sysConfigJSONArray;
	}
	
	public JSONArray readBySectionAndVariable(String sSection, String sVariable) {
		SystemConfig sysConfig = new SystemConfig();
		JSONObject requestJSONObject = new JSONObject();
		JSONArray configsJSONArray = new JSONArray();
		
		try {
			requestJSONObject.put("section", sSection);
			requestJSONObject.put("variable", sVariable);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		configsJSONArray = sysConfig.readDataListFromApi("gm", "system", "getSystemConfigBySectionAndVariable", requestJSONObject.toString());
		
		return configsJSONArray;
	}
	
	public void readDataFromJson(JSONObject configJSONObject) {
		JSONObject tempJSONObject = null;
		
		tempJSONObject = configJSONObject.optJSONObject("SysConfig");
		if(tempJSONObject == null)
			tempJSONObject = configJSONObject;
			
		this.init();
		this.scfgId = tempJSONObject.optInt("scfg_id");
		this.section = tempJSONObject.optString("scfg_section");
		this.variable = tempJSONObject.optString("scfg_variable");
		this.index = tempJSONObject.optInt("scfg_index");
		
		this.value = tempJSONObject.optString("scfg_value", null);
		
		this.remark = tempJSONObject.optString("scfg_remark", null);
	}
	
	// init value
	public void init() {
		this.scfgId = 0;
		this.section = "";
		this.variable = "";
		this.index = 0;
		this.value = null;
		this.remark = null;
	}

	//get scfg id
	protected int getScfgId() {
		return this.scfgId;
	}
	
	//get section
	public String getSection() {
		return this.section;
	}
	//get variable
	public String getVariable() {
		return this.variable;
	}
	
	//get index
	public int getIndex() {
		return this.index;
	}
	
	//get value
	public String getValue() {
		return this.value;
	}
	
	//get remark
	protected String getRemark() {
		return this.remark;
	}
}
