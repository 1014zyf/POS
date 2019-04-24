package app.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class MenuItemCategoryList {
	private ArrayList<MenuItemCategory> m_oItemCategoryList;
	
	public MenuItemCategoryList() {
		this.m_oItemCategoryList = new ArrayList<MenuItemCategory>();
	}
	
	public MenuItemCategoryList(JSONArray oItemCategoryJSONArray) {
		JSONObject oItemCategoryJSONObject = null;
		
		m_oItemCategoryList = new ArrayList<MenuItemCategory>();
		for (int i = 0; i < oItemCategoryJSONArray.length(); i++) {
			JSONObject oTempJSONObject = oItemCategoryJSONArray.optJSONObject(i);
			if (oTempJSONObject == null)
				continue;
			
			if(!oTempJSONObject.isNull("MenuItemCategory"))
				oItemCategoryJSONObject = oTempJSONObject.optJSONObject("MenuItemCategory");
			else
				oItemCategoryJSONObject = oTempJSONObject;
			MenuItemCategory oItemCategory = new MenuItemCategory(oItemCategoryJSONObject);
			m_oItemCategoryList.add(oItemCategory);
		}
	}
	
	public boolean readItemCategoryList() {
		MenuItemCategory oItemCategoryList = new MenuItemCategory();
		JSONArray oItemCategoryJSONArray = oItemCategoryList.readAll();
		if (oItemCategoryJSONArray != null) {
			for (int i = 0; i < oItemCategoryJSONArray.length(); i++) {
				if (oItemCategoryJSONArray.isNull(i))
					continue;
				
				MenuItemCategory oItemCategory = new MenuItemCategory(oItemCategoryJSONArray.optJSONObject(i));
				m_oItemCategoryList.add(oItemCategory);
			}
		}
		
		return true;
	}
	
	public ArrayList<MenuItemCategory> getItemCategoryList() {
		return this.m_oItemCategoryList;
	}
}
