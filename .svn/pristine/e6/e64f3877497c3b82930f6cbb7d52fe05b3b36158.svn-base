package app.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SystemModuleRegistries {
	private int modrId;
	private int moduId;
	private String section;
	private String variable;
	private int index;
	private String value;
	private String remark;
	
	public SystemModuleRegistries() {
		this.init();
	}
	
	public SystemModuleRegistries(JSONObject configJSONObject) {
		JSONObject tempJSONObject = null;
		
		tempJSONObject = configJSONObject.optJSONObject("SysModuleRegistry");
		if(tempJSONObject == null)
			tempJSONObject = configJSONObject;
			
		this.init();
		this.modrId = tempJSONObject.optInt("modr_id");
		this.moduId = tempJSONObject.optInt("modr_modu_id");
		this.section = tempJSONObject.optString("modr_section");
		this.variable = tempJSONObject.optString("modr_variable");
		this.index = tempJSONObject.optInt("modr_index");
		
		this.value = tempJSONObject.optString("modr_value", null);
		
		this.remark = tempJSONObject.optString("modr_remark", null);
	}
	
	//read data from POS API
	protected JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray sysConfigJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("moduleRegistries")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("moduleRegistries")) {
				this.init();
				return null;
			}
			
			sysConfigJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("moduleRegistries");
		}
		
		return sysConfigJSONArray;
	}
	
	public JSONArray readByModuleIdAndSectionAndVariable(Integer iModuleId, String sSection, String sVariable) {
		SystemModuleRegistries sysConfig = new SystemModuleRegistries();
		JSONObject requestJSONObject = new JSONObject();
		JSONArray configsJSONArray = new JSONArray();
		
		try {
			requestJSONObject.put("moduleId", iModuleId);
			requestJSONObject.put("section", sSection);
			requestJSONObject.put("variable", sVariable);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		configsJSONArray = sysConfig.readDataListFromApi("gm", "system", "getSystemModuleRegistryByModuleIdAndSectionAndVariable", requestJSONObject.toString());
		
		return configsJSONArray;
	}
	
	// init value
	public void init() {
		this.modrId = 0;
		this.moduId = 0;
		this.section = "";
		this.variable = "";
		this.index = 0;
		this.value = null;
		this.remark = null;
	}

	//get modr id
	protected int getModrId() {
		return this.modrId;
	}
	
	//get moduId
	protected int getModuleId() {
		return this.moduId;
	}
	
	//get section
	protected String getSection() {
		return this.section;
	}
	
	//get variable
	protected String getVariable() {
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
