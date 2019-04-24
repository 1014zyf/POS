package app.model;

import java.util.ArrayList;

import org.json.JSONArray;

public class MenuItemDeptGroupList {
	private ArrayList<MenuItemDeptGroup> m_oItemDeptGroupList;
	
	public MenuItemDeptGroupList() {
		this.m_oItemDeptGroupList = new ArrayList<MenuItemDeptGroup>();
	}
	
	public boolean readItemDeptGroupListByCode(ArrayList<String> oCodeList) {
		MenuItemDeptGroup oItemDeptGroup = new MenuItemDeptGroup(), oItemDeptGroupTemp;
		JSONArray oItemDeptGroupJSONArray = oItemDeptGroup.readByCodes(oCodeList);
		if (oItemDeptGroupJSONArray != null) {
			for (int i = 0; i < oItemDeptGroupJSONArray.length(); i++) {
				if (oItemDeptGroupJSONArray.isNull(i))
					continue;
				
				oItemDeptGroupTemp = new MenuItemDeptGroup(oItemDeptGroupJSONArray.optJSONObject(i));
				m_oItemDeptGroupList.add(oItemDeptGroupTemp);
			}
		}
		
		return true;
	}
	
	public MenuItemDeptGroup getByCode(String sCode) {
		MenuItemDeptGroup oTempDeptGroup = null;
		for(int i=0; i<m_oItemDeptGroupList.size(); i++) {
			if(m_oItemDeptGroupList.get(i).getCode().equals(sCode)) {
				oTempDeptGroup = m_oItemDeptGroupList.get(i);
				break;
			}
		}
		
		return oTempDeptGroup;
	}
}
