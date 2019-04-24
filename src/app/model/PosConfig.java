//Database: pos_configs - System configuration for POS module
package app.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosConfig {
	private int scfgId;
	private String by;
	private int recordId;
	private String section;
	private String variable;
	private int index;
	private String value;
	private String remark;
	
	//init object with initialize value
	public PosConfig () {
		this.init();		
	}
	
	public PosConfig(JSONObject configJSONObject) {
		readDataFromJson(configJSONObject);
	}
	
	//init object from database with scfg_id
	public PosConfig (int iScfgId){
		this.init();
		
		this.scfgId = iScfgId;
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
				
				if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("config")) {
					OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
					this.init();
					return false;
				}
				
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("config")) {
					this.init();
					return false;
				}
				
				tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("config");
				if (tempJSONObject.isNull("PosConfig")) {
					this.init();
					return false;
				}
				
				readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray configJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("configs")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("configs")) 
				return null;
			
			configJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("configs");
		}
		
		return configJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject configJSONObject) {
		JSONObject resultConfig = null;
		resultConfig = configJSONObject.optJSONObject("PosConfig");
		if(resultConfig == null)
			resultConfig = configJSONObject;
		
		this.init();
		this.scfgId = resultConfig.optInt("scfg_id");
		this.by = resultConfig.optString("scfg_by");
		this.recordId = resultConfig.optInt("scfg_record_id");
		this.section = resultConfig.optString("scfg_section");
		this.variable = resultConfig.optString("scfg_variable");
		this.index = resultConfig.optInt("scfg_index");

		this.value = resultConfig.optString("scfg_value", null);
		this.remark = resultConfig.optString("scfg_remark", null);
	}
	
	// init value
	public void init() {
		this.scfgId = 0;
		this.by = "";
		this.recordId = 0;
		this.section = "";
		this.variable = "";
		this.index = 0;
		this.value = null;
		this.remark = null;
	}
	
	//read data from database by scfg_id
	public void readById (int iScfgId) {
		
	}
	
	//read data from database by stat_id
	public boolean readBySectionAndVariable(String sSection, String sVariable) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("section", sSection);
			requestJSONObject.put("variable", sVariable);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataFromApi("gm", "pos", "getConfigBySectionAndVariable", requestJSONObject.toString());
		
	}
	
	//read all config records
	public JSONArray readAll() {
		JSONArray responseJSONArray = null;
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllConfig", null);

		return responseJSONArray;
	}
	
	//read all config records
	public JSONArray readAllByStationOutletShop(int iStatId, int iOutletId, int iShopId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("stationId", iStatId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("shopId", iShopId);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataListFromApi("gm", "pos", "getAllConfigsByStationOutletShop", requestJSONObject.toString());
	}
	
	//add new system configuration to database
	public boolean add() {
		return true;
	}
	
	//update system configuration to database
	public boolean update() {
		return true;
	}
	
	//get scfgId
	protected int getScfgId() {
		return this.scfgId;
	}
	
	//get by
	protected String getBy() {
		return this.by;
	}
	
	//get record id
	protected int getRecordId() {
		return this.recordId;
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
	protected int getIndex() {
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
