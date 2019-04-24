//Database: pos_attribute_types attribute type methods
package om;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosAttributeType {
	private int atypId;
	private String atypType;
	private String atypKey;
	private String atypStatus;
	private String[] name;
	private String[] shortName;
	private List<HashMap<String, String>> m_oAttributeOptionList;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	// type
	public static String TYPE_CHECK = "check";
	
	//init object with initialize value
	public PosAttributeType() {
		this.init();
	}
	
	//init object with JSONObject
	public PosAttributeType(JSONObject attributeTypeJSONObject) {
		readDataFromJson(attributeTypeJSONObject);
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray attributeTypeJSONArray = null;
		
		if (OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false) == false)
			return null;
		else {
			try {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
					return null;
				
				if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("attributeTypes")) {
					OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
					return null;
				}
				
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("attributeTypes"))
					return null;
				
				attributeTypeJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().getJSONArray("attributeTypes");
				
			}catch(JSONException jsone) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.stackToString(jsone));
				return null;
			}
		}
		return attributeTypeJSONArray;
	}
	
	//get all by type
	public JSONArray readAllByType(String sType) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
			
		try {
			requestJSONObject.put("type", sType);
			requestJSONObject.put("recursive", -1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
			
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllAttributeTypesByType", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject attributeTypeJSONObject) {
		JSONObject oResultAttributeType = null, oAttributeOption = null;
		JSONArray oResultAttributeOptionJSONArray = null;
		oResultAttributeType = attributeTypeJSONObject.optJSONObject("PosAttributeType");
		if(oResultAttributeType == null)
			oResultAttributeType = attributeTypeJSONObject;
		
		this.init();
		this.atypId = oResultAttributeType.optInt("atyp_id");
		this.atypType = oResultAttributeType.optString("atyp_type");
		this.atypKey = oResultAttributeType.optString("atyp_key");
		for(int i=0; i<5; i++) {
			this.name[i] = oResultAttributeType.optString("atyp_name_l"+(i+1));
			this.shortName[i] = oResultAttributeType.optString("atyp_short_name_l"+(i+1));
		}
		this.atypStatus = oResultAttributeType.optString("atyp_status", PosAttributeType.STATUS_ACTIVE);
		
		oResultAttributeOptionJSONArray = attributeTypeJSONObject.optJSONArray("attributeOptions");
		if (oResultAttributeOptionJSONArray != null){
			for(int i=0; i<oResultAttributeOptionJSONArray.length(); i++) {
				try {
					oAttributeOption = oResultAttributeOptionJSONArray.getJSONObject(i);
					if (oAttributeOption.optInt("atto_id", 0) <= 0 || oAttributeOption.optInt("atto_seq", 0) <= 0)
						continue;
					HashMap<String, String> oHashMap = new HashMap<String, String>();
					oHashMap.put("attoId", oAttributeOption.optString("atto_id", ""));
					oHashMap.put("name1", oAttributeOption.optString("atto_name_l1", ""));
					oHashMap.put("name2", oAttributeOption.optString("atto_name_l2", ""));
					oHashMap.put("name3", oAttributeOption.optString("atto_name_l3", ""));
					oHashMap.put("name4", oAttributeOption.optString("atto_name_l4", ""));
					oHashMap.put("name5", oAttributeOption.optString("atto_name_l5", ""));
					oHashMap.put("attoSeq", oAttributeOption.optString("atto_seq", ""));
					this.m_oAttributeOptionList.add(oHashMap);
				}catch(JSONException jsone) {
					jsone.printStackTrace();
				}
			}
		}
		
	}
	
	// init value
	public void init () {
		int i=0;
		
		this.atypId = 0;
		this.atypType = "";
		this.atypKey = "";
		this.atypStatus = PosAttributeType.STATUS_ACTIVE;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		m_oAttributeOptionList = new ArrayList<HashMap<String, String>>();
	}
	
	public int getAtypId() {
		return atypId;
	}

	public String getAtypType() {
		return atypType;
	}

	public String getAtypKey() {
		return atypKey;
	}

	public String getAtypStatus() {
		return atypStatus;
	}

	public String[] getName() {
		return name;
	}

	public String[] getShortName() {
		return shortName;
	}
	
	public List<HashMap<String, String>> getAttributeOptionList() {
		return m_oAttributeOptionList;
	}
}
