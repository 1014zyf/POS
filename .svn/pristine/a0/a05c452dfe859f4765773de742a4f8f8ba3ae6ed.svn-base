package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuItemGroupLookup {
	private int igluId;
	private int igrpId;
	private int itemId;
	
	//init object with initialize value
	public MenuItemGroupLookup() {
		this.init();
	}
	
	//init object with JSONObject 
	public MenuItemGroupLookup(JSONObject groupLookupJSONObject) {
		this.readDataFromJson(groupLookupJSONObject);
	}
	
	//init object
	public void init() {
		this.igluId = 0;
		this.igrpId = 0;
		this.itemId = 0;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject groupLookupJSONObject) {
		JSONObject tempJSONObject = null;
		
		tempJSONObject = groupLookupJSONObject.optJSONObject("MenuItemGroupLookup");
		if(tempJSONObject == null)
			tempJSONObject = groupLookupJSONObject;
			
		this.init();
		this.igluId = tempJSONObject.optInt("iglu_id");
		this.igrpId = tempJSONObject.optInt("iglu_igrp_id");
		this.itemId = tempJSONObject.optInt("iglu_item_id");
	}
	
	public int getGroupLookId() {
		return this.igluId;
	}
	
	public int getItemGroupId() {
		return this.igrpId;
	}
	
	public int getItemId() {
		return this.itemId;
	}
	
	public JSONArray readAll() {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("recursive", -1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "menu", "getAllMenuItemGroupLookup", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	//read data from POS API
		private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
			JSONArray itemGroupLookupJSONArray = null;
			
			if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
				return null;
			else {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
					return null;
				
				if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("menuItemGroupLookups")) {
					OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
					return null;
				}
				
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("menuItemGroupLookups"))
					return null;
				
				itemGroupLookupJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("menuItemGroupLookups");
			}
			
			return itemGroupLookupJSONArray;
		}
}
