package om;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;

public class PosStationList {
	private List<PosStation> m_oPosStationList;
	private HashMap<Integer, PosStation> m_oPosStationMapList;
	
	public PosStationList() {
		m_oPosStationList = new ArrayList<PosStation>();
		m_oPosStationMapList = new HashMap<Integer, PosStation>();
	}
	
	public void readStationListByDeviceKey(String sDeviceKey) {
		PosStation oPostStationList = new PosStation(), oPosStation = null;
		
		JSONArray oStationJSONArray = oPostStationList.getStationByDeviceKey(sDeviceKey);
		if (oStationJSONArray != null) {
			for(int i = 0; i < oStationJSONArray.length(); i++) {
				if (oStationJSONArray.isNull(i))
					continue;
				
				oPosStation = new PosStation(oStationJSONArray.optJSONObject(i));
				m_oPosStationList.add(oPosStation);
			}
		}
	}
	
	public void readStationListByOutletId(int iOutletId) {
		PosStation oPostStationList = new PosStation(), oPosStation = null;
		
		JSONArray oStationJSONArray = oPostStationList.getStationByOutletId(iOutletId);
		if (oStationJSONArray != null) {
			for(int i = 0; i < oStationJSONArray.length(); i++) {
				if (oStationJSONArray.isNull(i))
					continue;
				
				oPosStation = new PosStation(oStationJSONArray.optJSONObject(i));
				m_oPosStationList.add(oPosStation);
			}
		}
	}
	
	public List<PosStation> getPosStationList() {
		return m_oPosStationList;
	}
	
	public void readStationMappingListByOutletId(int iOutletId) {
		PosStation oPostStationList = new PosStation(), oPosStation = null;
		
		JSONArray oStationJSONArray = oPostStationList.getStationByOutletId(iOutletId);
		if (oStationJSONArray != null) {
			for(int i = 0; i < oStationJSONArray.length(); i++) {
				if (oStationJSONArray.isNull(i))
					continue;
				
				oPosStation = new PosStation(oStationJSONArray.optJSONObject(i));
				m_oPosStationMapList.put(oPosStation.getStatId(), oPosStation);
			}
		}
	}
	
	public HashMap<Integer, PosStation> getPosStationMapList() {
		return m_oPosStationMapList;
	}
	
}
