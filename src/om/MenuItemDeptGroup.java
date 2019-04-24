package om;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuItemDeptGroup {
	private int idgpId;
	private String code;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String status;
	private ArrayList<MenuItemDeptGroupLookup> m_oItemDeptGroupLookups;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	public MenuItemDeptGroup() {
		this.init();
	}
	
	//init object with JSONObject
	public MenuItemDeptGroup(JSONObject oItemDeptGroupJSONObject) {
		JSONObject oTempJSONObject = null;
		int i = 0;

		oTempJSONObject = oItemDeptGroupJSONObject.optJSONObject("MenuItemDeptGroup");
		if(oTempJSONObject == null)
			oTempJSONObject = oItemDeptGroupJSONObject;

		this.init();
		this.idgpId = oTempJSONObject.optInt("idgp_id");
		this.code = oTempJSONObject.optString("idgp_code");
		for(i=1; i<=5; i++) {
			this.name[i-1] = oTempJSONObject.optString("idgp_name_l"+i);
			this.shortName[i-1] = oTempJSONObject.optString("idgp_short_name_l"+i);
		}
		this.seq = oTempJSONObject.optInt("idgp_seq");
		this.status = oTempJSONObject.optString("idgp_status", MenuItemDept.STATUS_ACTIVE);
		
		if(oItemDeptGroupJSONObject.has("MenuItemDeptGroupLookup")) {
			for(i=0; i<oItemDeptGroupJSONObject.optJSONArray("MenuItemDeptGroupLookup").length(); i++) {
				JSONObject oDeptGroupLookupJSONObject = oItemDeptGroupJSONObject.optJSONArray("MenuItemDeptGroupLookup").optJSONObject(i);
				MenuItemDeptGroupLookup oTempDeptGroupLookup = new MenuItemDeptGroupLookup(oDeptGroupLookupJSONObject);
				this.m_oItemDeptGroupLookups.add(oTempDeptGroupLookup);
			}
		}
	}

	//get all active by ids
	public JSONArray readAllByGroupIds(ArrayList<String> oItemDeptGroupList) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			JSONArray oJSONArray = new JSONArray();
			
			for(int i=0; i<oItemDeptGroupList.size(); i++) {
				JSONObject oTempJSONObject = new JSONObject();
				oTempJSONObject.put("groupId", oItemDeptGroupList.get(i));
				oJSONArray.put(oTempJSONObject);
			}
			
			requestJSONObject.put("itemGroupIds", oJSONArray);
			requestJSONObject.put("recursive", 1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataListFromApi("gm", "menu", "getAllItemDeptByDeptGroupIds", requestJSONObject.toString());
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray itemCourseJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("item_dept_groups")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("item_dept_groups"))
				return null;
			
			itemCourseJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("item_dept_groups");
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
		
		responseJSONArray = this.readDataListFromApi("gm", "menu", "getItemDeptGroupsByCode", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.idgpId = 0;
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
		
		this.m_oItemDeptGroupLookups = new ArrayList<MenuItemDeptGroupLookup>();
	}
	
	public String getCode() {
		return this.code;
	}
	
	public ArrayList<MenuItemDeptGroupLookup> getItemDeptGroupLookupList() {
		return this.m_oItemDeptGroupLookups;
	}
}
