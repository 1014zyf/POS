package om;

import java.util.HashMap;

import org.json.JSONArray;

public class SystemModuleRegistryList {
	private HashMap<Integer, SystemModuleRegistries> m_oSystemModuleRegistryList;
	
	public SystemModuleRegistryList() {
		m_oSystemModuleRegistryList = new HashMap<Integer, SystemModuleRegistries>();
	}
	
	public void readByModuleIdAndSectionAndVariable(Integer iModuleId, String sSection, String sVariable) {
		SystemModuleRegistries oSystemModuleRegistryList = new SystemModuleRegistries(), oSystemModuleRegistry = null;
		JSONArray oJSONArray = oSystemModuleRegistryList.readByModuleIdAndSectionAndVariable(iModuleId, sSection, sVariable);
		if (oJSONArray != null) {
			for (int i = 0; i < oJSONArray.length(); i++) {
				if (oJSONArray.isNull(i))
					continue;
				oSystemModuleRegistry = new SystemModuleRegistries(oJSONArray.optJSONObject(i));
				m_oSystemModuleRegistryList.put(oSystemModuleRegistry.getModrId(), oSystemModuleRegistry);
			}
		}
	}
	
	public HashMap<Integer, SystemModuleRegistries> getSystemModuleRegistryList() {
		return this.m_oSystemModuleRegistryList;
	}
}
