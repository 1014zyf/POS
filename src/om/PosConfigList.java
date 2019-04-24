package om;

import java.util.HashMap;

import org.json.JSONArray;

public class PosConfigList {
private HashMap<String, HashMap<String, PosConfig>> m_oPosConfigList;
	
	public PosConfigList() {
		m_oPosConfigList = new HashMap<String, HashMap<String, PosConfig>>();
	}
	
	public void getAllConfig() {
		PosConfig oPosConfigList = new PosConfig();
		String sPrevConfigSection = "";
		HashMap<String, PosConfig> oPosConfigSectionList = new HashMap<String, PosConfig>();
		
		JSONArray configsJSONArray = oPosConfigList.readAll();
		if (configsJSONArray != null) {
			for (int i = 0; i < configsJSONArray.length(); i++) {
				if (configsJSONArray.isNull(i))
					continue;
				
				PosConfig oPosConfig = new PosConfig(configsJSONArray.optJSONObject(i));
				
				if (!oPosConfig.getSection().equals(sPrevConfigSection)) {
					if(!oPosConfigSectionList.isEmpty())
						m_oPosConfigList.put(sPrevConfigSection, oPosConfigSectionList);
					
					sPrevConfigSection = oPosConfig.getSection();
					oPosConfigSectionList = new HashMap<String, PosConfig>();
				}
				
				oPosConfigSectionList.put(oPosConfig.getVariable(), oPosConfig);
			}
		}
		
		// Add last section list
		if(!sPrevConfigSection.isEmpty() && !m_oPosConfigList.containsKey(sPrevConfigSection) && !oPosConfigSectionList.isEmpty())
			m_oPosConfigList.put(sPrevConfigSection, oPosConfigSectionList);
	}
	
	public void getAllConfigsByStationOutletShop(int iStatId, int iOutletId, int iShopId) {
		PosConfig oPosConfigList = new PosConfig();
		JSONArray configsJSONArray = oPosConfigList.readAllByStationOutletShop(iStatId, iOutletId, iShopId);
		
		this.readDataFromJsonArray(configsJSONArray);
	}
	
	public void readDataFromJsonArray(JSONArray oJsonArray) {
		String sPrevConfigSection = "";
		HashMap<String, PosConfig> oPosConfigSectionList = new HashMap<String, PosConfig>();
		
		if (oJsonArray != null) {
			for (int i = 0; i < oJsonArray.length(); i++) {
				if (oJsonArray.isNull(i))
					continue;
				
				PosConfig oPosConfig = new PosConfig(oJsonArray.optJSONObject(i));
				if (!oPosConfig.getSection().equals(sPrevConfigSection)) {
					if(!oPosConfigSectionList.isEmpty())
						m_oPosConfigList.put(sPrevConfigSection, oPosConfigSectionList);
					
					sPrevConfigSection = oPosConfig.getSection();
					oPosConfigSectionList = new HashMap<String, PosConfig>();
				}
				
				oPosConfigSectionList.put(oPosConfig.getVariable(), oPosConfig);
			}
		}
		
		// Add last section list
		if(!sPrevConfigSection.isEmpty() && !m_oPosConfigList.containsKey(sPrevConfigSection) && !oPosConfigSectionList.isEmpty())
			m_oPosConfigList.put(sPrevConfigSection, oPosConfigSectionList);
	}
	
	public HashMap<String, HashMap<String, PosConfig>> getPosConfigList() {
		return this.m_oPosConfigList;
	}
}
