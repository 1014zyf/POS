package om;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuItemGroup {
	private int igrpId;
	private int igtyId;
	private String code;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String status;
	private ArrayList<MenuItemGroupLookup> m_oItemGroupLookups;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	public MenuItemGroup() {
		this.init();
	}
	
	//init object with JSONObject
	public MenuItemGroup(JSONObject oItemGroupJSONObject) {
		JSONObject oTempJSONObject = null;
		int i = 0;

		oTempJSONObject = oItemGroupJSONObject.optJSONObject("MenuItemGroup");
		if(oTempJSONObject == null)
			oTempJSONObject = oItemGroupJSONObject;

		this.init();
		this.igrpId = oTempJSONObject.optInt("igrp_id");
		this.igtyId = oTempJSONObject.optInt("igrp_igty_id");
		this.code = oTempJSONObject.optString("igrp_code");
		for(i=1; i<=5; i++) {
			this.name[i-1] = oTempJSONObject.optString("igrp_name_l"+i);
			this.shortName[i-1] = oTempJSONObject.optString("igrp_short_name_l"+i);
		}
		this.seq = oTempJSONObject.optInt("igrp_seq");
		this.status = oTempJSONObject.optString("igrp_status", MenuItemGroup.STATUS_ACTIVE);
		
		if(oItemGroupJSONObject.has("MenuItemGroupLookup")) {
			for(i=0; i<oItemGroupJSONObject.optJSONArray("MenuItemGroupLookup").length(); i++) {
				JSONObject oItemGroupLookupJSONObject = oItemGroupJSONObject.optJSONArray("MenuItemGroupLookup").optJSONObject(i);
				MenuItemGroupLookup oTempGroupLookup = new MenuItemGroupLookup(oItemGroupLookupJSONObject);
				this.m_oItemGroupLookups.add(oTempGroupLookup);
			}
		}
	}
	
	//get all
	public JSONArray readAll(){
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("recursive", 1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		// Not yet development
		return this.readDataListFromApi("gm", "menu", "getAllItemGroups", requestJSONObject.toString());
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray itemCourseJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("item_groups")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("item_groups"))
				return null;
			
			itemCourseJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("item_groups");
		}
		
		return itemCourseJSONArray;
	}
	
	//get all active
	public JSONArray readByCodes(ArrayList<String> oCodeList) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			JSONArray oArray = new JSONArray();
			
			for(int i=0; i<oCodeList.size(); i++) {
				JSONObject oTemp = new JSONObject();
				oTemp.put("code", oCodeList.get(i));
				oArray.put(oTemp);
			}
			
			requestJSONObject.put("codes", oArray);
			requestJSONObject.put("recursive", 1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		// Not yet development
		//responseJSONArray = this.readDataListFromApi("gm", "menu", "getItemGroupsByCode", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.igrpId = 0;
		this.igtyId = 0;
		this.code = "";
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.status = MenuItemDept.STATUS_ACTIVE;
		
		this.m_oItemGroupLookups = new ArrayList<MenuItemGroupLookup>();
	}
	
	public String getCode() {
		return this.code;
	}
	
	public ArrayList<MenuItemGroupLookup> getItemGroupLookupList() {
		return this.m_oItemGroupLookups;
	}
}
