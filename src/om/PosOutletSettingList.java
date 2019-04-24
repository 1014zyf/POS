package om;

import java.util.ArrayList;

import org.json.JSONArray;

public class PosOutletSettingList {
	private ArrayList<PosOutletSetting> m_oPosOutletSettingList;
	
	public PosOutletSettingList(){
		m_oPosOutletSettingList = new ArrayList<PosOutletSetting>();
	}
	
	//read all function records
	public void readAll(int iShopId) {
		PosOutletSetting oPosOutletSettingList = new PosOutletSetting();
		JSONArray responseJSONArray = null;
		
		responseJSONArray = oPosOutletSettingList.readAllByShopId(iShopId);
		if(responseJSONArray == null)
			return;
		
		for(int i = 0; i < responseJSONArray.length(); i++) {
			if (responseJSONArray.isNull(i))
				continue;
			
			PosOutletSetting oPosOutletSetting = new PosOutletSetting(responseJSONArray.optJSONObject(i));
			
			// Add to function list
			m_oPosOutletSettingList.add(oPosOutletSetting);
		}
	}
	
	public ArrayList<PosOutletSetting> getPosOutletSettingList() {
		return m_oPosOutletSettingList;
	}
}