package om;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuItemDept {
	private int idepId;
	private String code;
	private int parentIdepId;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String status;

	private List<MenuItemDept> childDeptList;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";

	public MenuItemDept() {
		this.init();
	}
	
	//init object with JSONObject
	public MenuItemDept(JSONObject oItemDeptJSONObject) {
		this.readDataFromJson(oItemDeptJSONObject);
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("itemDepartment")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("itemDepartment")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("itemDepartment");
			if(tempJSONObject.isNull("MenuItemDept")) {
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("item_depts")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("item_depts"))
				return null;
			
			itemCourseJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("item_depts");
		}
		
		return itemCourseJSONArray;
	}
	
	//read data from database by item_id
	public boolean readById(int iDepartmentId) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("id", Integer.toString(iDepartmentId));
			requestJSONObject.put("recusive", -1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "menu", "getItemDepartmentById", requestJSONObject.toString());
	}
	
	//get all active
	public JSONArray readAll() {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("recursive", -1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "menu", "getAllMenuItemDepts", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	//get all child item department by dept list
	public JSONArray readAllChildItemDeptByDeptList(ArrayList<String> sItemDepartmentList) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("recursive", -1);
			requestJSONObject.put("itemDepartmentList", sItemDepartmentList);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "menu", "getAllChildMenuItemDeptsByDeptList", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject oItemDeptJSONObject) {
		JSONObject oTempJSONObject = null;
		int i = 0;

		oTempJSONObject = oItemDeptJSONObject.optJSONObject("MenuItemDept");
		if(oTempJSONObject == null)
			oTempJSONObject = oItemDeptJSONObject;

		this.init();
		this.idepId = oTempJSONObject.optInt("idep_id");
		this.code = oTempJSONObject.optString("idep_code");
		this.parentIdepId = oTempJSONObject.optInt("idep_parent_idep_id");
		for(i=1; i<=5; i++) {
			this.name[i-1] = oTempJSONObject.optString("idep_name_l"+i);
			this.shortName[i-1] = oTempJSONObject.optString("idep_short_name_l"+i);
		}
		this.seq = oTempJSONObject.optInt("idep_seq");
		this.status = oTempJSONObject.optString("idep_status", MenuItemDept.STATUS_ACTIVE);
		
		//handle item's child if exist
		JSONArray oChildItemJSONArray = oTempJSONObject.optJSONArray("ChildDeptList");
		if(oChildItemJSONArray != null) {
			MenuItemDeptList oChildDeptList = new MenuItemDeptList(oChildItemJSONArray);
			this.childDeptList = oChildDeptList.getItemDeptList();
		}
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.idepId = 0;
		this.code = "";
		this.parentIdepId = 0;
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
		
		if(this.childDeptList == null)
			this.childDeptList = new ArrayList<MenuItemDept>();
		else
			this.childDeptList.clear();
	}
	
	public void setIdepId(int iIdepId) {
		this.idepId = iIdepId;
	}
	
	protected void setCode(String sCode) {
		this.code = sCode;
	}
	
	public void setParentIdepId(int iParentIdepId) {
		this.parentIdepId = iParentIdepId;
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
	
	public int getIdepId() {
		return this.idepId;
	}
	
	public String getCode() {
		return this.code;
	}

	public int getParentIdepId() {
		return this.parentIdepId;
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
	
	public List<MenuItemDept> getChildDeptList() {
		return this.childDeptList;
	}
}
