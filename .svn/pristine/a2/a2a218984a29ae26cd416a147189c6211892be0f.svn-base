//Database: pos_access_rights - POS access right
package om;

import org.json.JSONException;
import org.json.JSONObject;

public class PosAccessRight {
	private int arigId;
	private String key;
	private String[] name;
	private String[] shortName;
	private String status;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosAccessRight() {
		this.init();
	}
	
	//init object with existing access right ID
	public PosAccessRight (int iArigId) {
		JSONObject requestJSONOjbect = new JSONObject();
		 
		this.init();
		
		try {
			requestJSONOjbect.put("accessRightId", Integer.toString(iArigId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		this.readDataFromApi("gm", "pos", "getAccessRightById", requestJSONOjbect.toString());
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("accessRight")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("accessRight")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("accessRight");
			if(tempJSONObject.isNull("PosAccessRight")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject accessRightJSONObject) {
		JSONObject resultAccessRight = null;
		int i;
		
		resultAccessRight = accessRightJSONObject.optJSONObject("PosAccessRight");
		if(resultAccessRight == null)
			resultAccessRight = accessRightJSONObject;
			
		this.init();

		this.arigId = resultAccessRight.optInt("arig_id");
		this.key = resultAccessRight.optString("arig_key");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultAccessRight.optString("arig_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultAccessRight.optString("arig_short_name_l"+i);
		this.status = resultAccessRight.optString("arig_status", PosAccessRight.STATUS_ACTIVE);
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.arigId = 0;
		this.key = "";
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.status = PosAccessRight.STATUS_ACTIVE;
	}
	
	//read object from database
	public void readById(int iArigId) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("accessRightId", Integer.toString(iArigId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		this.readDataFromApi("gm", "pos", "getAccessRightById", requestJSONObject.toString());
		
		return;
	}
	
	//read object from database by arig_key
	public void readByKey(String sKey) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("key", sKey);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		this.readDataFromApi("gm", "pos", "getAccessRightByKey", requestJSONObject.toString());
		
		return;
	}
	
	//add record access right to database 
	public boolean add() {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("name_l1", this.name[0]);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return true;
	}
	
	//update access right record to database
	public boolean update() {
		
		return true;
	}

	//get arigId
	protected int getArigId () {
		return this.arigId;
	}
	
	protected String getKey() {
		return this.key;
	}
	
	protected String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	protected String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	protected String getStatus() {
		return this.status;
	}
}
