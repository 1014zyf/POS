package om;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class MenuItemCourseList {
	private ArrayList<MenuItemCourse> m_oItemCourseList;
	
	// Init empty object
	public MenuItemCourseList() {
		this.m_oItemCourseList = new ArrayList<MenuItemCourse>();
	}
	
	// Init by JSON
	public MenuItemCourseList(JSONArray oMenuItemCourseJSONArray) {
		JSONObject oMenuItemCourseJSONObject = null;
		
		m_oItemCourseList = new ArrayList<MenuItemCourse>();
		for (int i = 0; i < oMenuItemCourseJSONArray.length(); i++) {
			if (oMenuItemCourseJSONArray.isNull(i))
				continue;
			
			if (!oMenuItemCourseJSONArray.optJSONObject(i).isNull("MenuItemCourse"))
				oMenuItemCourseJSONObject = oMenuItemCourseJSONArray.optJSONObject(i).optJSONObject("MenuItemCourse");
			else
				oMenuItemCourseJSONObject = oMenuItemCourseJSONArray.optJSONObject(i);
			MenuItemCourse oCheckItem = new MenuItemCourse(oMenuItemCourseJSONObject);
			m_oItemCourseList.add(oCheckItem);
		}
	}
	
	public boolean readItemCourseList() {
		MenuItemCourse oItemCourseList = new MenuItemCourse(), oItemCourse = null;
		JSONArray oItemCourseJSONArray = oItemCourseList.readAll();
		if(oItemCourseJSONArray != null) {
			for (int i = 0; i < oItemCourseJSONArray.length(); i++) {
				if (oItemCourseJSONArray.isNull(i))
					continue;
				
				oItemCourse = new MenuItemCourse(oItemCourseJSONArray.optJSONObject(i));
				m_oItemCourseList.add(oItemCourse);
			}
		}
		
		return true;
	}
	
	public ArrayList<MenuItemCourse> getItemCourseList() {
		return this.m_oItemCourseList;
	}
}
