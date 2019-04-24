//Database: pos_payment_methods Payment methods
package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosCustomType {
	private int ctypId;
	private int ctypSeq;
	private String ctypType;
	private String ctypCode;
	private String ctypStatus;
	private String[] name;
	private String[] shortName;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";

	// type
	public static String TYPE_CHECK = "check";
	
	//init object with initialize value
	public PosCustomType() {
		this.init();
	}
	
	//init obejct with JSONObject
	public PosCustomType(JSONObject customTypeJSONObject) {
		readDataFromJson(customTypeJSONObject);
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject customTypeJSONObject) {
		JSONObject oResultCustomTypes = null;
		oResultCustomTypes = customTypeJSONObject.optJSONObject("PosCustomType");
		if(oResultCustomTypes == null)
			oResultCustomTypes = customTypeJSONObject;
		
		this.init();
		this.ctypId = oResultCustomTypes.optInt("ctyp_id");
		this.ctypType = oResultCustomTypes.optString("ctyp_type");
		this.ctypCode = oResultCustomTypes.optString("ctyp_code");
		for(int i=0; i<5; i++) {
			this.name[i] = oResultCustomTypes.optString("ctyp_name_l"+(i+1));
			this.shortName[i] = oResultCustomTypes.optString("ctyp_short_name_l"+(i+1));
		}
		this.ctypSeq = oResultCustomTypes.optInt("ctyp_seq");
		this.ctypStatus = oResultCustomTypes.optString("ctyp_status", PosCustomType.STATUS_ACTIVE);
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray customTypeJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("customTypes")) {
				// Return a list of member
				
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("customTypes"))
				return null;
				
				customTypeJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("customTypes");
			} else if (OmWsClientGlobal.g_oWsClient.get().getResponse().has("customType")) {
				// Return a single member
				
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("customType"))
					return null;
				
				customTypeJSONArray = new JSONArray();
				customTypeJSONArray.put(OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("customType"));
			}
		}	
		return customTypeJSONArray;
	}
	
	// init value
	public void init () {
		int i=0;
		
		this.ctypId = 0;
		this.ctypSeq = 0;
		this.ctypType = "";
		this.ctypCode = "";
		this.ctypStatus = PosCustomType.STATUS_ACTIVE;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
	}
	
	// read custom types by types
	public JSONArray getCustomTypesByType(String sType) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try{
			requestJSONObject.put("type", sType);
		}catch(JSONException e){
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getCustomTypesByType", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	//get ctyp_id
	public int getCtypId() {
		return this.ctypId;
	}

	//get payment ctyp_code
	public String getCtypCode() {
		return this.ctypCode;
	}
	
	//get name with index
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get name array
	public String[] getNameArray() {
		return this.name;
	}
	
	//get short name with index
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get payment sequence
	public int getCustomTypeSequence() {
		return this.ctypSeq;
	}	
}
