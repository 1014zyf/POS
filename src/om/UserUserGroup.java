package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserUserGroup {
	
	private int userGroupId;
	private String userGroupCode;
	private String [] userGroupName;
	private String [] userGroupShortName;
	private int iUserGroupSeq;
	private String status;
	
	private String lastErrorMessage;
	private OmWsClient m_oOmWsClient;
	
	//init with initialized value
	public UserUserGroup() {
		this.init();
	}
	
	public UserUserGroup(JSONObject oUserJSONObject) {
		this.readDataFromJson(oUserJSONObject);
	}
	
	public void init() {
		m_oOmWsClient = OmWsClientGlobal.g_oWsClient.get();
		
		userGroupId = 0;
		userGroupCode = null;
		userGroupName = new String[5];
		userGroupShortName = new String[5];
		status = null;
		iUserGroupSeq = 0;
		
		lastErrorMessage = null;
	}

	// support single or multiple user group codes
	public JSONArray readByUserGroupCode(String [] sUserGroupCode) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("userGroupCode", sUserGroupCode);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		JSONObject responseJSONObject = this.readDataFromApi("gm", "user", "getUserGroupByUserGroupCode", requestJSONObject.toString());
		
		if(responseJSONObject != null && !responseJSONObject.isNull("userGroups"))
			return responseJSONObject.optJSONArray("userGroups");
		
		return null;
	}
	
	//read data from POS API
	private JSONObject readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONObject tempJSONObject = null;
		this.lastErrorMessage = "";
		
		if (m_oOmWsClient.call(sWsInterface, sModule, sFcnName, sParam, true)) {
			//no response
			if(m_oOmWsClient.getResponse() == null) {
				this.init();
				return tempJSONObject;
			}
			
			if(!m_oOmWsClient.getResponse().has("userGroups")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", m_oOmWsClient.getResponse().toString());
				this.init();
				return tempJSONObject;
			}
			
			if(m_oOmWsClient.getResponse().isNull("userGroups")) {
				this.init();
				return tempJSONObject;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse();
		}
		return tempJSONObject;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject oUserJSONObject) {
		JSONObject resultUser = null;
		int i;
		
		resultUser = oUserJSONObject.optJSONObject("UserUserGroup");
		if(resultUser == null)
			resultUser = oUserJSONObject;
			
		this.init();
		
		this.userGroupId = resultUser.optInt("ugrp_id");
		this.userGroupCode = resultUser.optString("ugrp_code");
		
		for(i = 1; i <= 5; i++)
		userGroupName[i - 1] = resultUser.optString("ugrp_name_l" + i);
		
		for(i = 1; i <= 5; i++)
			userGroupShortName[i - 1] = resultUser.optString("ugrp_name_l" + i);

		this.iUserGroupSeq = resultUser.optInt("ugrp_seq");
		this.status = resultUser.optString("ugrp_status");
	}

	public int getUserGroupId() {
		return userGroupId;
	}

	public String getUserGroupCode() {
		return userGroupCode;
	}

	public String[] getUserGroupName() {
		return userGroupName;
	}
	
	public String getUserGroupName(int iIndex) {
		return userGroupName[iIndex - 1];
	}

	public String[] getUserGroupShortName() {
		return userGroupShortName;
	}
	
	public String getUserGroupShortName(int iIndex) {
		return userGroupShortName[iIndex - 1];
	}
	
	public String getStatus() {
		return status;
	}
	
	//get last error message
	public String getLastErrorMessage() {
		return this.lastErrorMessage;
	}
	
	public void setUserGroupId(int userGroupId) {
		this.userGroupId = userGroupId;
	}
	
	public void setUserGroupCode(String userGroupCode) {
		this.userGroupCode = userGroupCode;
	}
	
	public void setUserGroupName(String[] userGroupName) {
		this.userGroupName = userGroupName;
	}

	public void setUserGroupShortName(String[] userGroupShortName) {
		this.userGroupShortName = userGroupShortName;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	
}
