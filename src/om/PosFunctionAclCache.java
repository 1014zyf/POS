package om;

import org.json.JSONException;
import org.json.JSONObject;

public class PosFunctionAclCache {
	private int faccId;
	private int userId;
	private int oletId;
	private int funcId;
	private String allow;
	
	private UserUser user;
	
	// allow
	private static String ALLOW_NO = "";
	private static String ALLOW_YES = "y";
	
	public PosFunctionAclCache() {
		this.init();
	}
	
	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean result = true;
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			result = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("function_acl_cache")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("function_acl_cache")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("function_acl_cache");
			if(tempJSONObject.isNull("PosFunctionAclCache")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return result;
	}
	
	//read data from database by func Id and outlet id
	public boolean readByFuncKeyIdAndOutletId(int iFuncId, String sFunctionKey, int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("funcId", iFuncId);
			requestJSONObject.put("functionKey", sFunctionKey);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("recursive", 0);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if(!this.readDataFromApi("gm", "pos", "getAndGenFuncAclCacheByFuncAndOutletId", requestJSONObject.toString())){
			return false;
		}
		
		return true;
	}
	
	//read data from database by user Id, user Role, func Id and outlet id
	public boolean readByFuncKeyIdAndUserAndOutletId(int iFuncId, String sFunctionKey, int sUserId, String sUserRole, int iUserDeptId, int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("funcId", iFuncId);
			requestJSONObject.put("functionKey", sFunctionKey);
			requestJSONObject.put("userId", sUserId);
			requestJSONObject.put("userRole", sUserRole);
			requestJSONObject.put("userDeptId", iUserDeptId);
			requestJSONObject.put("outletId", iOutletId);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if(!this.readDataFromApi("gm", "pos", "getAndGenFuncAclCacheByFuncAndUserAndOutletId", requestJSONObject.toString()))
			return false;
		
		return true;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject funcAclCacheJSONObject) {
		JSONObject resulFunctionAclCache = null;
		
		resulFunctionAclCache = funcAclCacheJSONObject.optJSONObject("PosFunctionAclCache");
		if(resulFunctionAclCache == null)
			resulFunctionAclCache = funcAclCacheJSONObject;
			
		this.init();
		
		this.faccId = resulFunctionAclCache.optInt("facc_id");
		this.userId = resulFunctionAclCache.optInt("facc_user_id");
		this.oletId = resulFunctionAclCache.optInt("facc_olet_id");
		this.funcId = resulFunctionAclCache.optInt("facc_func_id");
		this.allow = resulFunctionAclCache.optString("facc_allow", PosFunctionAclCache.ALLOW_NO);
		
		JSONObject tempJSONObject2 = funcAclCacheJSONObject.optJSONObject("UserUser");
		if (tempJSONObject2 != null && tempJSONObject2.has("user_id"))
			this.user = new UserUser(funcAclCacheJSONObject);
	}
	
	// init value
	public void init() {
		this.faccId = 0;
		this.userId = 0;
		this.oletId = 0;
		this.funcId = 0;
		this.allow = PosFunctionAclCache.ALLOW_NO;
		
		this.user = null;
	}
	
	protected int getFaccId() {
		return this.faccId;
	}
	
	protected int getUserId() {
		return this.userId;
	}
	
	protected int getOletId() {
		return this.oletId;
	}
	
	protected int getFuncId() {
		return this.funcId;
	}
	
	public String getAllow() {
		return this.allow;
	}
	
	public String getUserStatus() {
		if(this.user != null)
			return this.user.getStatus();
		else
			return UserUser.STATUS_DELETED;
	}

	public boolean isAllow() {
		return this.allow.equals(PosFunctionAclCache.ALLOW_YES);
	}
}
