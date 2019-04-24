//Database: pos_functions - POS function list
package app.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosFunction {
	private int funcId;
	private String key;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String askPassword;
	private String askAuthority;
	private String askApproval;
	private String status;
	
	private List<PosFunctionGroupLookup> functionGroupLookupList;
	private List<PosFunctionAcl> functionAclList;
	
	// askPassword
	public static String ASK_PASSWORD_NO = "";
	public static String ASK_PASSWORD_YES = "y";
	
	// askAuthority
	public static String ASK_AUTHORITY_NO = "";
	public static String ASK_AUTHORITY_YES = "y";
	
	// askApproval
	public static String ASK_APPROVAL_NO = "";
	public static String ASK_APPROVAL_YES = "y";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosFunction () {
		this.init();
	}
	
	//init obejct with JSONObject
	public PosFunction(JSONObject functionJSONObject) {
		this.readDataFromJson(functionJSONObject);
	}
	
	//init object from database by func_id
	public PosFunction (int iFuncId) {
		this.init();
		
		this.funcId = iFuncId;
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray functionJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("functions")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("functions"))
				return null;
			
			functionJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("functions");
		}
		
		return functionJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject functionJSONObject) {
		int i;
		JSONObject tempFunctionJSONObject = null;
		
		tempFunctionJSONObject = functionJSONObject.optJSONObject("PosFunction");
		if(tempFunctionJSONObject == null)
			tempFunctionJSONObject = functionJSONObject;
		
		this.init();
		this.funcId = tempFunctionJSONObject.optInt("func_id");
		this.key = tempFunctionJSONObject.optString("func_key");
		for(i=1; i<=5; i++) 
			this.name[(i-1)] = tempFunctionJSONObject.optString("func_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = tempFunctionJSONObject.optString("func_short_name_l"+i);
		this.seq = tempFunctionJSONObject.optInt("func_seq");
		this.askPassword = tempFunctionJSONObject.optString("func_ask_password", PosFunction.ASK_PASSWORD_NO);
		this.askAuthority = tempFunctionJSONObject.optString("func_ask_authority", PosFunction.ASK_AUTHORITY_NO);
		this.askApproval = tempFunctionJSONObject.optString("func_ask_approval", PosFunction.ASK_APPROVAL_NO);
		this.status = tempFunctionJSONObject.optString("func_status", PosFunction.STATUS_ACTIVE);
		
		//handle corresponding's function group
		JSONArray funcGroupJSONArray = functionJSONObject.optJSONArray("PosFunctionGroupLookup");
		if(funcGroupJSONArray != null) {
			PosFunctionGroupLookup oFunctionGroupLookup = null;
			for(i=0; i<funcGroupJSONArray.length(); i++) {
				oFunctionGroupLookup = new PosFunctionGroupLookup(funcGroupJSONArray.optJSONObject(i));
				this.functionGroupLookupList.add(oFunctionGroupLookup);
			}
		}
		
		//handle corresponding's function access right 
		JSONArray funcAclJSONArray = functionJSONObject.optJSONArray("PosFunctionAcl");
		if(funcAclJSONArray != null) {
			PosFunctionAcl oFunctionAcl = null;
			for(i=0; i<funcAclJSONArray.length(); i++) {
				oFunctionAcl = new PosFunctionAcl(funcAclJSONArray.optJSONObject(i));
				this.functionAclList.add(oFunctionAcl);
			}
		}
	}
	
	//read all function records
	public JSONArray readAll(int iUserId, List<Integer> oUserGroupList, int iOutletId, List<Integer> oOutletGroupList) {
		JSONObject requestJSONObject = new JSONObject();
		JSONObject tempJSONObject = null;
		JSONArray responseJSONArray = null;
		JSONArray tempJSONArray = null;
		
		try {
			requestJSONObject.put("userId", iUserId);
			//form user group list
			tempJSONArray = new JSONArray();
			for(Integer iUserGroupId:oUserGroupList) {
				tempJSONObject = new JSONObject();
				tempJSONObject.put("userGroupId", iUserGroupId);
				tempJSONArray.put(tempJSONObject);
			}
			requestJSONObject.put("userGroupList", tempJSONArray);
			
			requestJSONObject.put("outletId", iOutletId);
			//form outlet group list
			tempJSONArray = new JSONArray();
			for(Integer iOutletGroupId:oOutletGroupList) {
				tempJSONObject = new JSONObject();
				tempJSONObject.put("outletGroupId", iOutletGroupId);
				tempJSONArray.put(tempJSONObject);
			}
			requestJSONObject.put("outletGroupList", tempJSONArray);
			
			requestJSONObject.put("recursive", 1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllFunctionsWithGenAclCacheByOutlet", requestJSONObject.toString());

		return responseJSONArray;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.funcId = 0;
		this.key = "";
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.askPassword = PosFunction.ASK_PASSWORD_NO;
		this.askAuthority = PosFunction.ASK_AUTHORITY_NO;
		this.askApproval = PosFunction.ASK_APPROVAL_NO;
		this.status = PosFunction.STATUS_ACTIVE;
		
		if(this.functionAclList == null)
			this.functionAclList = new ArrayList<PosFunctionAcl>();
		else
			this.functionAclList.clear();
		
		if(this.functionGroupLookupList == null)
			this.functionGroupLookupList = new ArrayList<PosFunctionGroupLookup>();
		else
			this.functionGroupLookupList.clear();
	}

	//get funcId
	public int getFuncId() {
		return this.funcId;
	}
	
	//get key
	public String getKey() {
		return this.key;
	}
	
	//get name by lang index
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get short name by lang index
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get seq
	protected int getSeq() {
		return this.seq;
	}
	
	//get ask password
	protected String getAskPassword() {
		return this.askPassword;
	}
	
	//get status
	protected String getStatus() {
		return this.status;
	}
	
	//get function access control list
	public List<PosFunctionAcl> getFunctionAclList() {
		return this.functionAclList;
	}
	
	// check whether need to ask password
	public boolean askPassword() {
		return this.askPassword.equals(PosFunction.ASK_PASSWORD_YES);
	}
	
	// check whether need to ask authority
	public boolean askAuthority() {
		return this.askAuthority.equals(PosFunction.ASK_AUTHORITY_YES);
	}
	
	// check whether need to ask authority
	public boolean askApproval() {
		return this.askApproval.equals(PosFunction.ASK_APPROVAL_YES);
	}
}
