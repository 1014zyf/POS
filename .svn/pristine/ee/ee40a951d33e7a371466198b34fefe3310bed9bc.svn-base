package om;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class PosTableSettingList {
	private List<PosTableSetting> m_oPosTableSettingList;
	
	public PosTableSettingList() {
		m_oPosTableSettingList = new ArrayList<>();
	}
	
	public void readAllTableSettingsByShopOutlet(int iShopId, int iOutletId) {
		PosTableSetting oPosTableSetting = new PosTableSetting();
		JSONArray responseJSONArray =  oPosTableSetting.readAllTableSettingsByShopOutlet(iShopId, iOutletId);
		if (responseJSONArray != null)
			this.updateTableSettingList(responseJSONArray);
	}
	
	public void updateTableSettingList(JSONArray tableSettingListJSONArray) {
		StringBuilder sTableAndExtension = new StringBuilder();
		try {
			if (tableSettingListJSONArray != null) {
				for (int i = 0; i < tableSettingListJSONArray.length(); i++) {
					sTableAndExtension.setLength(0);
					if (!tableSettingListJSONArray.getJSONObject(i).isNull("PosTableSetting")) {
						PosTableSetting oPosTableSetting = new PosTableSetting(tableSettingListJSONArray.getJSONObject(i));
						m_oPosTableSettingList.add(oPosTableSetting);
					}
				}
			}
		} catch (JSONException json) {
			json.printStackTrace();
		}
	}
	
	public PosTableSetting getPosTableSettingByTable (String sTableNo, String sTableExt) {
		if(sTableNo == null || sTableNo.isEmpty() || sTableExt == null)
			return null;
		
		for (PosTableSetting oPosTableSetting : m_oPosTableSettingList) {
			if(oPosTableSetting.getTblsTable() == Integer.valueOf(sTableNo) && oPosTableSetting.getTblsTableExt().equals(sTableExt))
				return oPosTableSetting;
		}
		
		return null;
	}
}