package app.model;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class SystemConfigList {
	private HashMap<Integer, SystemConfig> m_oSystemConfigList;
	
	public SystemConfigList() {
		m_oSystemConfigList = new HashMap<Integer, SystemConfig>();
	}
	
	public void readBySectionAndVariable(String sSection, String sVariable) {
		SystemConfig oSystemConfigList = new SystemConfig(), oSystemConfig = null;
		JSONArray configsJSONArray = new JSONArray();
		
		configsJSONArray = oSystemConfigList.readBySectionAndVariable(sSection, sVariable);
		if (configsJSONArray != null) {
			for (int i = 0; i < configsJSONArray.length(); i++) {
				JSONObject oTempJSONObject = configsJSONArray.optJSONObject(i);
				if (oTempJSONObject == null)
					continue;
				
				oSystemConfig = new SystemConfig(oTempJSONObject);
				
				m_oSystemConfigList.put(oSystemConfig.getScfgId(), oSystemConfig);
			}
		}
	}
	
	public HashMap<Integer, SystemConfig> getSystemConfigList() {
		return this.m_oSystemConfigList;
	}
}
