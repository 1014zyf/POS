package app.model;

import java.util.ArrayList;

import org.json.JSONArray;

public class MenuItemCourseList {
	private ArrayList<MenuItemCourse> m_oItemCourseList;
	
	public MenuItemCourseList() {
		this.m_oItemCourseList = new ArrayList<MenuItemCourse>();
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
