package om;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class PosInterfaceConfigList {
	private List<PosInterfaceConfig> m_oInterfaceConfigList;
	
	public PosInterfaceConfigList() {
		this.m_oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
	}
	
	public PosInterfaceConfigList(JSONArray posInterfaceConfigJSONArray) {
		if (posInterfaceConfigJSONArray ==null)
			return;
		
		this.m_oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		for(int i = 0; i < posInterfaceConfigJSONArray.length(); i++) {
			if (posInterfaceConfigJSONArray.isNull(i))
				continue;
			
			PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig(posInterfaceConfigJSONArray.optJSONObject(i));
			this.m_oInterfaceConfigList.add(oPosInterfaceConfig);
		}
	}
	
	public void getInterfaceConfigList(int iShopId, int iOletId, int iStatId, String sBy) {
		PosInterfaceConfig oTempnterfaceConfig = new PosInterfaceConfig();
		JSONArray oInterfaceConfigJSONArray = oTempnterfaceConfig.getInterfaceConfigsByShopOutletIdStationId(iShopId, iOletId, iStatId, sBy);
		if (oInterfaceConfigJSONArray != null) {
			for (int i = 0; i < oInterfaceConfigJSONArray.length(); i++) {
				if (oInterfaceConfigJSONArray.isNull(i))
					continue;
				
				PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig(oInterfaceConfigJSONArray.optJSONObject(i));
				m_oInterfaceConfigList.add(oInterfaceConfig);
			}
		}
	}
	
	public List<PosInterfaceConfig> getInterfaceConfigListByInterfaceTypeAndVendorKey(String sInfType, String sKey) {
		List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		
		for(PosInterfaceConfig oInterfaceConfig:m_oInterfaceConfigList) {
			if(oInterfaceConfig.isInfInterfaceNull())
				continue;
			
			if(oInterfaceConfig.getInterfaceType().contentEquals(sInfType) && oInterfaceConfig.getInterfaceVendorKey().equals(sKey)) 
				oInterfaceConfigList.add(oInterfaceConfig);
		}
		
		return oInterfaceConfigList;
	}
	
	public List<PosInterfaceConfig> getInterfaceConfigListByInterfaceType(String sInfType) {
		List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		
		for(PosInterfaceConfig oInterfaceConfig:m_oInterfaceConfigList) {
			if(oInterfaceConfig.isInfInterfaceNull())
				continue;
			
			if(oInterfaceConfig.getInterfaceType().contentEquals(sInfType)) 
				oInterfaceConfigList.add(oInterfaceConfig);
		}
		
		return oInterfaceConfigList;
	}
	
	public PosInterfaceConfig getInterfaceConfigListById(int iInftId) {
		PosInterfaceConfig oTargetInterfaceConfig = null;
		
		for(PosInterfaceConfig oInterfaceConfig:m_oInterfaceConfigList) {
			if(oInterfaceConfig.getInterfaceId() == iInftId) {
				oTargetInterfaceConfig = oInterfaceConfig;
				break;
			}
		}
		
		return oTargetInterfaceConfig;
	}
	
	public int getInterfaceConfigCount(){
		return this.m_oInterfaceConfigList.size();
	}
	
	public void clearInterfaceConfigList() {
		this.m_oInterfaceConfigList.clear();
	}
	
	public List<PosInterfaceConfig> getInterfaceConfigurationList() {
		return m_oInterfaceConfigList;
	}
}
