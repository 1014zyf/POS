package om;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuItemCategory {
	private int icatId;
	private String code;
	private int parentIcatId;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String status;
	
	private List<MenuItemCategory> childCategoryList;

	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";

	public MenuItemCategory() {
		this.init();
	}
	
	//init object with JSONObject
	public MenuItemCategory(JSONObject oItemCategoryJSONObject) {
		this.readDataFromJson(oItemCategoryJSONObject);
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("itemCategory")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("itemCategory")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("itemCategory");
			if(tempJSONObject.isNull("MenuItemCategory")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray itemCourseJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("item_categorys")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("item_categorys"))
				return null;
			
			itemCourseJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("item_categorys");
		}
		
		return itemCourseJSONArray;
	}
	
	//read data from database by item_id
	public boolean readById(int iCategoryId) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("id", Integer.toString(iCategoryId));
			requestJSONObject.put("recusive", -1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "menu", "getItemCategoryById", requestJSONObject.toString());
	}
	
	
	//get all active record with no parent
	public JSONArray readAll() {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("recursive", -1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "menu", "getAllMenuItemCategorys", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject oItemCategoryJSONObject) {
		JSONObject oTempJSONObject = null;
		int i = 0;
		
		oTempJSONObject = oItemCategoryJSONObject.optJSONObject("MenuItemCategory");
		if(oTempJSONObject == null)
			oTempJSONObject = oItemCategoryJSONObject;
			
		this.init();
		this.icatId = oTempJSONObject.optInt("icat_id");
		this.code = oTempJSONObject.optString("icat_code");
		this.parentIcatId = oTempJSONObject.optInt("icat_parent_icat_id");
		for(i=1; i<=5; i++) {
			this.name[i-1] = oTempJSONObject.optString("icat_name_l"+i);
			this.shortName[i-1] = oTempJSONObject.optString("icat_short_name_l"+i);
		}
		this.seq = oTempJSONObject.optInt("icat_seq");
		this.status = oTempJSONObject.optString("icat_status", MenuItemCategory.STATUS_ACTIVE);
		
		//handle item's child if exist
		JSONArray oChildItemJSONArray = oTempJSONObject.optJSONArray("ChildCategoryList");
		if(oChildItemJSONArray != null) {
			MenuItemCategoryList oChildCategoryList = new MenuItemCategoryList(oChildItemJSONArray);
			this.childCategoryList = oChildCategoryList.getItemCategoryList();
		}
	}
	
		
	// init value
	public void init() {
		int i=0;
		
		this.icatId = 0;
		this.code = "";
		this.parentIcatId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.status = MenuItemCategory.STATUS_ACTIVE;
		
		if(this.childCategoryList == null)
			this.childCategoryList = new ArrayList<MenuItemCategory>();
		else
			this.childCategoryList.clear();
	}
	
	public void setIcatId(int iIcatId) {
		this.icatId = iIcatId;
	}
	
	public void setParentIcatId(int iParentIcatId) {
		this.parentIcatId = iParentIcatId;
	}
	
	public void setName(int iIndex, String sName) {
		this.name[iIndex-1] = sName;
	}
	
	protected void setShortName(int iIndex, String sShortName) {
		this.shortName[iIndex-1] = sShortName;
	}
	
	protected void setSeq(int iSeq) {
		this.seq = iSeq;
	}
	
	protected void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	public int getIcatId() {
		return this.icatId;
	}
	
	public String getCode() {
		return this.code;
	}

	public int getParentIcatId() {
		return this.parentIcatId;
	}
	
	public String getName(int iIndex) {
		return this.name[iIndex-1];
	}
	
	public String[] getName() {
		return this.name;
	}
	
	public String getShortName(int iIndex) {
		return this.shortName[iIndex-1];
	}
	
	public int getSeq() {
		return this.seq;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public List<MenuItemCategory> getChildCategoryList() {
		return this.childCategoryList;
	}
}
