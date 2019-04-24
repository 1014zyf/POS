package app.model;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserUserList {
	private HashMap<Integer, UserUser> m_oUserUserList;
	
	public UserUserList() {
		m_oUserUserList = new HashMap<Integer, UserUser>();
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray functionJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else
			functionJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("user");
		
		return functionJSONArray;
	}
	
	public void readUserByIdList(HashMap<Integer, Integer> oUserIdList) {
		JSONObject requestJSONObject = new JSONObject();
		JSONObject tempJSONObject = null;
		JSONArray responseJSONArray = null;
		JSONArray tempJSONArray = null;
		
		try {
			tempJSONArray = new JSONArray();
			for(Integer iUserId:oUserIdList.values()) {
				tempJSONObject = new JSONObject();
				tempJSONObject.put("id", iUserId);
				tempJSONArray.put(tempJSONObject);
			}
			
			requestJSONObject.put("userIds", tempJSONArray);
			requestJSONObject.put("recursive", -1);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "user", "getMultiUserByUserIdList", requestJSONObject.toString());

		for (int i = 0; i < responseJSONArray.length(); i++) {
			JSONObject oUserJSONObject = responseJSONArray.optJSONObject(i);
			if (oUserJSONObject == null)
				continue;
			UserUser oUser = new UserUser(oUserJSONObject);

			// Add to user list
			m_oUserUserList.put(oUser.getUserId(), oUser);
		}
	}
	
	public HashMap<Integer, UserUser> getUserList() {
		return this.m_oUserUserList;
	}
}
