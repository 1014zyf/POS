package om;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class MenuItemDeptList {
	private ArrayList<MenuItemDept> m_oItemDeptList;
	
	public MenuItemDeptList() {
		this.m_oItemDeptList = new ArrayList<MenuItemDept>();
	}
	
	public MenuItemDeptList(JSONArray oItemDeptJSONArray) {
		JSONObject oItemDeptJSONObject = null;
		
		m_oItemDeptList = new ArrayList<MenuItemDept>();
		for (int i = 0; i < oItemDeptJSONArray.length(); i++) {
			JSONObject oTempJSONObject = oItemDeptJSONArray.optJSONObject(i);
			if (oTempJSONObject == null)
				continue;
			
			if(!oTempJSONObject.isNull("MenuItemDept"))
				oItemDeptJSONObject = oTempJSONObject.optJSONObject("MenuItemDept");
			else
				oItemDeptJSONObject = oTempJSONObject;
			MenuItemDept oItemDept = new MenuItemDept(oItemDeptJSONObject);
			m_oItemDeptList.add(oItemDept);
		}
	}
	
	public boolean readItemDeptList() {
		MenuItemDept oItemDeptList = new MenuItemDept();
		JSONArray oItemDeptJSONArray = new JSONArray();
		
		oItemDeptJSONArray = oItemDeptList.readAll();
		if (oItemDeptJSONArray != null) {
			for (int i = 0; i < oItemDeptJSONArray.length(); i++) {
				if (oItemDeptJSONArray.isNull(i))
					continue;
				
				MenuItemDept oItemDept = new MenuItemDept(oItemDeptJSONArray.optJSONObject(i));
				m_oItemDeptList.add(oItemDept);
			}
		}
		return true;
	}
	
	public boolean readChildItemDeptListByDeptList(ArrayList<String> sItemDepartmentList) {
		MenuItemDept oItemDeptList = new MenuItemDept();
		JSONArray oItemDeptJSONArray = new JSONArray();
		
		oItemDeptJSONArray = oItemDeptList.readAllChildItemDeptByDeptList(sItemDepartmentList);
		
		if (oItemDeptJSONArray != null) {
			for (int i = 0; i < oItemDeptJSONArray.length(); i++) {
				if (oItemDeptJSONArray.isNull(i))
					continue;
				
				MenuItemDept oItemDept = new MenuItemDept(oItemDeptJSONArray.optJSONObject(i));
				m_oItemDeptList.add(oItemDept);
			}
		}
		
		return true;
	}
	
	public ArrayList<MenuItemDept> getItemDeptList() {
		return this.m_oItemDeptList;
	}
}
