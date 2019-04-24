package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuItemDeptGroupLookup {
	private int idluId;
	private int idgpId;
	private int idepId;
	
	//init object with initialize value
	public MenuItemDeptGroupLookup() {
		this.init();
	}
	
	//init object with JSONObject 
	public MenuItemDeptGroupLookup(JSONObject deptGroupLookupJSONObject) {
		this.readDataFromJson(deptGroupLookupJSONObject);
	}
	
	//init object
	public void init() {
		this.idluId = 0;
		this.idgpId = 0;
		this.idepId = 0;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject deptGroupLookupJSONObject) {
		JSONObject tempJSONObject = null;
		
		tempJSONObject = deptGroupLookupJSONObject.optJSONObject("MenuItemDeptGroupLookup");
		if(tempJSONObject == null)
			tempJSONObject = deptGroupLookupJSONObject;
			
		this.init();
		this.idluId = tempJSONObject.optInt("idlu_id");
		this.idgpId = tempJSONObject.optInt("idlu_idgp_id");
		this.idepId = tempJSONObject.optInt("idlu_idep_id");
	}
	
	public int getDeptId() {
		return this.idepId;
	}
	
	public int getDeptGrpId() {
		return this.idgpId;
	}
	
	public JSONArray readAll() {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("recursive", -1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "menu", "getAllItemDeptGroupLookups", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray itemDeptGroupLookupJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("MenuItemDeptGroupLookups")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("MenuItemDeptGroupLookups"))
				return null;
			
			itemDeptGroupLookupJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("MenuItemDeptGroupLookups");
		}
		
		return itemDeptGroupLookupJSONArray;
	}
}
