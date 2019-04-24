package app.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuItemCourse {
	private int icouId;
	private String code;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String style;
	private String status;

	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";

	public MenuItemCourse() {
		this.init();
	}
	
	//init object with JSONObject
	public MenuItemCourse(JSONObject oItemCourseJSONObject) {
		JSONObject oTempJSONObject = null;
		int i = 0;
		
		oTempJSONObject = oItemCourseJSONObject.optJSONObject("MenuItemCourse");
		if(oTempJSONObject == null)
			oTempJSONObject = oItemCourseJSONObject;
			
		this.init();
		this.icouId = oTempJSONObject.optInt("icou_id");
		this.code = oTempJSONObject.optString("icou_code");
		for(i=1; i<=5; i++) {
			this.name[i-1] = oTempJSONObject.optString("icou_name_l"+i);
			this.shortName[i-1] = oTempJSONObject.optString("icou_short_name_l"+i);
		}
		this.seq = oTempJSONObject.optInt("icou_seq");
		this.style = oTempJSONObject.optString("icou_style", null);
		this.status = oTempJSONObject.optString("icou_status", MenuItemCourse.STATUS_ACTIVE);
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray itemCourseJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("item_courses")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("item_courses"))
				return null;
			
			itemCourseJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("item_courses");
		}
		
		return itemCourseJSONArray;
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
		
		responseJSONArray = this.readDataListFromApi("gm", "menu", "getAllMenuItemCourses", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.icouId = 0;
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
		this.style = null;
		this.status = MenuItemCourse.STATUS_ACTIVE;
	}
	
	protected void setIcouId(int iIcouId) {
		this.icouId = iIcouId;
	}
	
	protected void setCode(String sCode) {
		this.code = sCode;
	}
	
	protected void setName(int iIndex, String sName) {
		this.name[iIndex-1] = sName;
	}
	
	protected void setShortName(int iIndex, String sShortName) {
		this.shortName[iIndex-1] = sShortName;
	}
	
	protected void setSeq(int iSeq) {
		this.seq = iSeq;
	}
	
	protected void setStyle(String sStyle) {
		this.style = sStyle;
	}
	
	protected void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	public int getIcouId() {
		return this.icouId;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public String getName(int iIndex) {
		return this.name[iIndex-1];
	}
	
	public String getShortName(int iIndex) {
		return this.shortName[iIndex-1];
	}
	
	public int getSeq() {
		return this.seq;
	}
	
	public String getStyle() {
		return this.style;
	}
	
	public String getStatus() {
		return this.status;
	}
}
