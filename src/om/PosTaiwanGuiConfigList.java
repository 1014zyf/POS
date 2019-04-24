package om;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosTaiwanGuiConfigList {
	private List<PosTaiwanGuiConfig> m_oTaiwanGuiConfigList;
	
	public PosTaiwanGuiConfigList() {
		this.m_oTaiwanGuiConfigList = new ArrayList<PosTaiwanGuiConfig>();
	}
	
	//	read all function records
	public void readAllByDateAndOutlet(String sDate, int iOletId) {	
		PosTaiwanGuiConfig oPosTaiwanGuiConfig = new PosTaiwanGuiConfig(), oConfig = null;
		JSONArray responseJSONArray = oPosTaiwanGuiConfig.readAllByDateAndOutlet(sDate, iOletId);	
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				JSONObject oConfigJSONObject = responseJSONArray.optJSONObject(i);
				if (oConfigJSONObject == null)
					continue;
				oConfig = new PosTaiwanGuiConfig(oConfigJSONObject);
				
				// Add to function list
				m_oTaiwanGuiConfigList.add(oConfig);
			}
		}
	}
	
	public void readAllByDateAndStation(int iStatId) {	
		PosTaiwanGuiConfig oPosTaiwanGuiConfig = new PosTaiwanGuiConfig(), oConfig = null;
		JSONArray responseJSONArray = oPosTaiwanGuiConfig.readAllByDateAndStation(iStatId);
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				JSONObject oConfigJSONObject = responseJSONArray.optJSONObject(i);
				if (oConfigJSONObject == null)
					continue;
				oConfig = new PosTaiwanGuiConfig(oConfigJSONObject);
				
				// Add to function list
				m_oTaiwanGuiConfigList.add(oConfig);
			}
		}
	}
	
	public void loadAllByStation(int iStatId) {	
		PosTaiwanGuiConfig oPosTaiwanGuiConfig = new PosTaiwanGuiConfig(), oConfig = null;
		JSONArray responseJSONArray = oPosTaiwanGuiConfig.readAllByStation(iStatId);	
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				JSONObject oConfigJSONObject = responseJSONArray.optJSONObject(i);
				if (oConfigJSONObject == null)
					continue;
				oConfig = new PosTaiwanGuiConfig(oConfigJSONObject);
				
				// Add to function list
				m_oTaiwanGuiConfigList.add(oConfig);
			}
		}
	}
	
	public void addConfig(PosTaiwanGuiConfig oPosTaiwanGuiConfig) {
		m_oTaiwanGuiConfigList.add(oPosTaiwanGuiConfig);
	}
	
	public void removeConfigByType(String sType) {
		for(int i = 0; i< m_oTaiwanGuiConfigList.size(); i++) {
			if (m_oTaiwanGuiConfigList.get(i).getType().equals(sType)) {
				m_oTaiwanGuiConfigList.remove(i);
				break;
			}
		}
	}
	
	public List<PosTaiwanGuiConfig> getConfigList() {	
		return m_oTaiwanGuiConfigList;
	}
	
	public PosTaiwanGuiConfig getConfigByType(String sType) {
		PosTaiwanGuiConfig oTmpPosTaiwanGuiConfig = new PosTaiwanGuiConfig() ;
		for (PosTaiwanGuiConfig oPosTaiwanGuiConfig:m_oTaiwanGuiConfigList) {
			if (oPosTaiwanGuiConfig.getType().equals(sType)) {
				oTmpPosTaiwanGuiConfig = oPosTaiwanGuiConfig;
				break;
			}
		}
		return oTmpPosTaiwanGuiConfig;
	}
}