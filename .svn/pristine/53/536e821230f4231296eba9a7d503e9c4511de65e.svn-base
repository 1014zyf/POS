package app.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserUserModuleInfoList {
	private ArrayList<UserUserModuleInfo> m_oUserUserModuleInfoList;
	
	public UserUserModuleInfoList() {
		m_oUserUserModuleInfoList = new ArrayList<UserUserModuleInfo>();
	}
	
	public void initWithJSONArray(JSONArray oUserModuleInfoJSONArray) {
		if (oUserModuleInfoJSONArray.length() > 0) {
			for (int i = 0; i < oUserModuleInfoJSONArray.length(); i++) {
				if (oUserModuleInfoJSONArray.isNull(i))
					continue;
				JSONObject oUserModuleInfoJSONObject = oUserModuleInfoJSONArray.optJSONObject(i);
				if (oUserModuleInfoJSONObject == null)
					continue;
				
				UserUserModuleInfo oUserUserModuleInfo = new UserUserModuleInfo(oUserModuleInfoJSONObject);
				m_oUserUserModuleInfoList.add(oUserUserModuleInfo);
			}
		}
	}
	
	public void readByModuleAliasAndVariable(int iUserId, String sModuleAlias, String sVariable) {
		UserUserModuleInfo oUserModuleAlias = new UserUserModuleInfo();
		JSONArray oUserModuleInfoJSONArray = oUserModuleAlias.readByModuleAliasAndVariable(iUserId, sModuleAlias, sVariable);
		if (oUserModuleInfoJSONArray != null) {
			for (int i = 0; i < oUserModuleInfoJSONArray.length(); i++) {
				if (oUserModuleInfoJSONArray.isNull(i))
					continue;
				
				JSONObject oUserModuleInfoJSONObject = oUserModuleInfoJSONArray.optJSONObject(i).optJSONObject("UserUserModuleInfo");
				if (oUserModuleInfoJSONObject == null)
					continue;
				
				UserUserModuleInfo oTempUserModuleInfo = new UserUserModuleInfo(oUserModuleInfoJSONObject);
				m_oUserUserModuleInfoList.add(oTempUserModuleInfo);
			}
		}
	}
	
	public String getValueByModuleAliasAndVariable(String sModuleAlias, String sVariable) {
		String sValue = null;
			for(UserUserModuleInfo oUserUserModuleInfo:m_oUserUserModuleInfoList) {
				if (oUserUserModuleInfo.getModuleAlias().equals(sModuleAlias) && oUserUserModuleInfo.getVariable().equals(sVariable)) {
					sValue = oUserUserModuleInfo.getValue();
					break;
				}
			}
		
		return sValue;
	}
	
	public void clearUserModuleInfoList() {
		this.m_oUserUserModuleInfoList.clear();
	}
}
