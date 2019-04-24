package om;

import java.util.HashMap;

import org.json.JSONArray;

public class UserUserGroupList {
	
	private HashMap<Integer, UserUserGroup> m_oUserUserGroupList;
	
	public UserUserGroupList() {
		m_oUserUserGroupList = new HashMap<Integer, UserUserGroup>();
	}
	
	public void findUserGroupbyUserGroupCode(String [] sUserGroupCode) {
		UserUserGroup oUserUserGroupList = new UserUserGroup();
		JSONArray responseJSONArray = oUserUserGroupList.readByUserGroupCode(sUserGroupCode);
		
		if(responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				if (responseJSONArray.isNull(i))
					continue;
				
				UserUserGroup oUserUserGroup = new UserUserGroup(responseJSONArray.optJSONObject(i));
				
				// Add to function list
				m_oUserUserGroupList.put(oUserUserGroup.getUserGroupId(), oUserUserGroup);
			}
		}
	}
	
	public HashMap<Integer, UserUserGroup> getUserGroupList(){
		return this.m_oUserUserGroupList;
	}
	
}
