package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserUserModuleInfo {
	private int uinfId;
	private int userId;
	private String moduleAlias;
	private String variable;
	private String value;
	
	public UserUserModuleInfo() {
		this.init();
	}
	
	//init object with JSON Object
	public UserUserModuleInfo(JSONObject userModuleInfoJSONObject) {
		this.init();
		if (userModuleInfoJSONObject == null)
			return;
		
		this.uinfId = userModuleInfoJSONObject.optInt("uinf_id");
		this.userId = userModuleInfoJSONObject.optInt("uinf_user_id");
		this.moduleAlias = userModuleInfoJSONObject.optString("uinf_module_alias");
		this.variable = userModuleInfoJSONObject.optString("uinf_variable");
		this.value = userModuleInfoJSONObject.optString("uinf_value");
	}
	
	//read data list from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray userModuleInfoJSONArray = new JSONArray();
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, true))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("user_module_info")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(),	"", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("user_module_info"))
				return null;
			
			userModuleInfoJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("user_module_info");
		}
		
		return userModuleInfoJSONArray;
	}
		
	//read by variable
	public JSONArray readByModuleAliasAndVariable(int iUserId, String sModuleAlias, String sVariable) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray oUserModuleInfoJSONArray = null;
		
		try {
			requestJSONObject.put("userId", iUserId);
			requestJSONObject.put("moudleAlias", sModuleAlias);
			requestJSONObject.put("variable", sVariable);
			
			oUserModuleInfoJSONArray = this.readDataListFromApi("gm", "user", "getUserModuleInfoByModuleAndVariable", requestJSONObject.toString());
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return oUserModuleInfoJSONArray;
	}
	
	// init value
	public void init() {
		this.uinfId = 0;
		this.userId = 0; 
		this.moduleAlias = "";
		this.variable = "";
		this.value = "";
	}
	
	//get uinf_id
	protected int getUserInfoId() {
		return this.uinfId;
	}
	
	//get user id
	protected int getUserId() {
		return this.userId;
	}
	
	//get module alias
	protected String getModuleAlias() {
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
