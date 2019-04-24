package om;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SystemGeneral {
	private List<SystemConfig> m_oSystemConfigList;
	private List<SystemModule> m_oSystemModuleList;
	
	public SystemGeneral() {
		m_oSystemConfigList = new ArrayList<SystemConfig>();
		m_oSystemModuleList = new ArrayList<SystemModule>();
	}
	
	public JSONObject readByConfigVariablesAndModuleAlias(HashMap<String, ArrayList<String>> oConfigVariables, ArrayList<String> oModuleAlias) {
		JSONObject requestJSONObject = new JSONObject();
		JSONObject configVariablesJSONObject = new JSONObject();
		JSONArray configAliasJSONArray = new JSONArray();
		JSONArray moduleAliasJSONArray = new JSONArray();
		
		for(Entry<String, ArrayList<String>> entry:oConfigVariables.entrySet()) {
			try {
				configVariablesJSONObject.put("section", entry.getKey());
				configVariablesJSONObject.put("variables", entry.getValue());
				configAliasJSONArray.put(configVariablesJSONObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		for(String sAlias:oModuleAlias) {
			if(sAlias.equals("pos_interface"))
				moduleAliasJSONArray.put("interface");
			else
				moduleAliasJSONArray.put(sAlias);
		}
		
		try {
			requestJSONObject.put("configVariables", configAliasJSONArray);
			requestJSONObject.put("moduleAlias", moduleAliasJSONArray);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "system", "getSystemConfigAndModulesByVariables", requestJSONObject.toString(),
				false))
			return null;
		else
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}

	public boolean readConfigVariablesAndModuleAlias(HashMap<String, ArrayList<String>> oConfigVariables, ArrayList<String> oModuleAlias) {
		SystemConfig oSystemConfig = null; 
		SystemModule oSystemModule = null;
		JSONObject configsModulesJSONObject = new JSONObject();
		
		configsModulesJSONObject = readByConfigVariablesAndModuleAlias(oConfigVariables, oModuleAlias);
		if(configsModulesJSONObject == null || !configsModulesJSONObject.has("configs") || !configsModulesJSONObject.has("modules"))
			return false;
		JSONArray configsJSONArray, modulesJSONArray = new JSONArray();
		
		configsJSONArray = configsModulesJSONObject.optJSONArray("configs");
		modulesJSONArray = configsModulesJSONObject.optJSONArray("modules");
		
		if (configsJSONArray != null) {
			for (int i = 0; i < configsJSONArray.length(); i++) {
				JSONArray oTempJSONArray = configsJSONArray.optJSONArray(i);
				for(int j = 0; j < oTempJSONArray.length(); j++) {
					JSONObject oTempJSONObject = oTempJSONArray.optJSONObject(j);
					
					if (oTempJSONObject == null)
						continue;
					
					oSystemConfig = new SystemConfig(oTempJSONObject);
					m_oSystemConfigList.add(oSystemConfig);
				}
			}
		}
		if (modulesJSONArray != null) {
			for (int i = 0; i < modulesJSONArray.length(); i++) {
				JSONObject oTempJSONObject = modulesJSONArray.optJSONObject(i);
				if (oTempJSONObject == null)
					continue;
					
				oSystemModule = new SystemModule(oTempJSONObject);
				m_oSystemModuleList.add(oSystemModule);
			}
		}
		
			return true;
	}
	
	public List<SystemConfig> getSystemConfigList() {
		return this.m_oSystemConfigList;
	}
	
	public List<SystemModule> getSystemModuleList() {
		return this.m_oSystemModuleList;
	}
	
	public SystemConfig getSystemConfigBySectionAndVariable(String sSection, String sVariable) {
		for(SystemConfig oSystemConfig:m_oSystemConfigList) {
			if(oSystemConfig.getSection().equals(sSection) && oSystemConfig.getVariable().equals(sVariable) && oSystemConfig.getValue() != null)
				return oSystemConfig;
		}
		return null;
	}
}