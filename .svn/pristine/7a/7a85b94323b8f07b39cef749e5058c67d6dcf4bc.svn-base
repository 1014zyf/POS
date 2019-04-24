package om;

import java.util.ArrayList;

import org.json.JSONArray;

public class MenuItemGroupList {
	private ArrayList<MenuItemGroup> m_oItemGroupList;
	
	public MenuItemGroupList() {
		this.m_oItemGroupList = new ArrayList<MenuItemGroup>();
	}
	
	public boolean readItemGroupListByCode(ArrayList<String> oCodeList) {
		MenuItemGroup oItemGroup = new MenuItemGroup(), oItemGroupTemp;
		JSONArray oItemGroupJSONArray = oItemGroup.readByCodes(oCodeList);
		if (oItemGroupJSONArray != null) {
			for (int i = 0; i < oItemGroupJSONArray.length(); i++) {
				if (oItemGroupJSONArray.isNull(i))
					continue;
				
				oItemGroupTemp = new MenuItemGroup(oItemGroupJSONArray.optJSONObject(i));
				m_oItemGroupList.add(oItemGroupTemp);
			}
		}
		
		return true;
	}
	
	public MenuItemGroup getByCode(String sCode) {
		MenuItemGroup oTempGroup = null;
		for(int i=0; i<m_oItemGroupList.size(); i++) {
			if(m_oItemGroupList.get(i).getCode().equals(sCode)) {
				oTempGroup = m_oItemGroupList.get(i);
				break;
			}
		}
		
		return oTempGroup;
	}

	public ArrayList<MenuItemGroup> getItemGroupList() {
		return m_oItemGroupList;
	}
}
