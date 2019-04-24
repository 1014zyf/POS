//Database: pos_check_tables - Sales check's table
package app.model;

import org.json.JSONException;
import org.json.JSONObject;

public class MemMemberModuleInfo {
	private int minfId;
	private int membId;
	private String moduleAlias;
	private String variable;
	private String value;

	public static String UPDATE_METHOD_TYPE_ADD = "a";	// Add the new value to the original value (for integer, decimal)
	public static String UPDATE_METHOD_TYPE_APPEND = "p"; // Append the new value to the end of the original value (for string)
	public static String UPDATE_METHOD_TYPE_REPLACE = "r"; // Replace the original value with the new value
	
	public static String MODULE_POS = "pos";
	
	public static String VARIABLE_LIFE_TIME_SPENDING = "life_time_spending";
	
	//init object with initialize value
	public MemMemberModuleInfo () {
		this.init();
	}
	
	//init object with JSON Object
	public MemMemberModuleInfo(JSONObject checkItemJSONObject) {
		readDataFromJson(checkItemJSONObject);
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject memberJSONObject) {
		JSONObject tempJSONObject = null;
		
		tempJSONObject = memberJSONObject.optJSONObject("MemMemberModuleInfo");
		if(tempJSONObject == null)
			tempJSONObject = memberJSONObject;
			
		this.init();
		this.minfId = tempJSONObject.optInt("minf_id"); 
		this.membId = tempJSONObject.optInt("minf_memb_id");
		this.moduleAlias = tempJSONObject.optString("minf_module_alias");
		this.variable = tempJSONObject.optString("minf_variable");
		this.value = tempJSONObject.optString("minf_value");
	}
	
	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bUpdate = true;
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bUpdate = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("MemMemberModuleInfo")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("MemMemberModuleInfo")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("MemMemberModuleInfo");
			readDataFromJson(tempJSONObject);
		}
		
		return bUpdate;
	}
	
	// Function to update member info
	public boolean updateInfo(int iMemberId, String sModule, String sVariable, String sValue, String sUpdateMethod) {
		JSONObject requestJSONObject = new JSONObject();

		try {	
			requestJSONObject.put("memberId", iMemberId);
			requestJSONObject.put("module", sModule);
			requestJSONObject.put("variable", sVariable);
			requestJSONObject.put("value", sValue);
			requestJSONObject.put("action", sUpdateMethod);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "member", "updateMemberInfo", requestJSONObject.toString(), false))
			return false;
		else
			return true;
	}

	//init value
	public void init() {
		this.minfId = 0;
		this.membId = 0;
		this.moduleAlias = "";
		this.variable = "";
		this.value = "";
	}
	
	//get module alias
	public String getModuleAlias() {
		return this.moduleAlias;
	}
	
	//get variable
	public String getVariable() {
		return this.variable;
	}
	
	//get value
	public String getValue() {
		return this.value;
	}
	
}
