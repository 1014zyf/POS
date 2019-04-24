package om;

import java.util.ArrayList;
import java.util.HashMap;

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

	public boolean readItemDeptGroupListByIds(ArrayList<String> oItemGroupList) {
		MenuItemDeptGroup oItemDeptGroup = new MenuItemDeptGroup(), oItemDeptGroupTemp;
		JSONArray oItemDeptGroupJSONArray = oItemDeptGroup.readAllByGroupIds(oItemGroupList);
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
	
	// Mapping item department group id and item department id
	public HashMap<Integer, ArrayList<Integer>> readAndGetAllItemDeptGroupList(){
		HashMap<Integer, ArrayList<Integer>> oItemDeparmentGroupLookList = new HashMap<Integer, ArrayList<Integer>>();
		MenuItemDeptGroupLookup oItemDeptGroupLookups = new MenuItemDeptGroupLookup();
		JSONArray oItemDeptGroupLookupJSONArray = oItemDeptGroupLookups.readAll();
		
		if(oItemDeptGroupLookupJSONArray != null){
			for(int i = 0; i < oItemDeptGroupLookupJSONArray.length(); i++){
				if (oItemDeptGroupLookupJSONArray.isNull(i))
					continue;
				MenuItemDeptGroupLookup oItemDeptGroupLookup = new MenuItemDeptGroupLookup(oItemDeptGroupLookupJSONArray.optJSONObject(i));
				ArrayList<Integer> oItemDepartmentIdList = new ArrayList<Integer>();
				
				if(oItemDeparmentGroupLookList.containsKey(oItemDeptGroupLookup.getDeptGrpId())) 
					oItemDepartmentIdList = oItemDeparmentGroupLookList.get(oItemDeptGroupLookup.getDeptGrpId());
				
				if(!oItemDepartmentIdList.contains(oItemDeptGroupLookup.getDeptId()))
					oItemDepartmentIdList.add(oItemDeptGroupLookup.getDeptId());
				oItemDeparmentGroupLookList.put(oItemDeptGroupLookup.getDeptGrpId(), oItemDepartmentIdList);
			}
		}
		
		return oItemDeparmentGroupLookList;
	}
	
	public ArrayList<MenuItemDeptGroup> getItemDeptGroupList() {
		return m_oItemDeptGroupList;
	}
}
