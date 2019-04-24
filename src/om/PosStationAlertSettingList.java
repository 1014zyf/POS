package om;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;

public class PosStationAlertSettingList {
	private ArrayList<PosStationAlertSetting> m_oPosAlertSettingList;
	
	
	
	public PosStationAlertSettingList() {
		m_oPosAlertSettingList =  new ArrayList<PosStationAlertSetting>();
	}
	
	public void readAllActiveAlertSetting() {
		PosStationAlertSetting oPosAlertSettingList = new PosStationAlertSetting() ,oTemPosAlertSettingList = null ;
		JSONArray oJSONArray = oPosAlertSettingList.readAllActiveAlertSetting();
		
		if (oJSONArray != null) {
			for (int i = 0; i < oJSONArray.length(); i++) {
				if (oJSONArray.isNull(i))
					continue;
				oTemPosAlertSettingList = new PosStationAlertSetting(oJSONArray.optJSONObject(i));
				m_oPosAlertSettingList.add(oTemPosAlertSettingList);
			}
		}
	}
	
	public ArrayList<PosStationAlertSetting> getPosAlertSettingList(){
		return this.m_oPosAlertSettingList;
	}
	
}
