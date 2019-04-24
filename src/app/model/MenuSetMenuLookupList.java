package app.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuSetMenuLookupList {

	private List<MenuSetMenuLookup> m_oLookupList;
	
	public MenuSetMenuLookupList(){
		m_oLookupList = new ArrayList<MenuSetMenuLookup>();
	}
	
	public void readMenuSetMenuLookupListById(int Id){
		MenuSetMenuLookup oMenuSetMenuLookupList = new MenuSetMenuLookup();
		JSONObject responseJsonObject = oMenuSetMenuLookupList.readByItemId(Id);
		try {
			JSONArray contentJSONArray = responseJsonObject.getJSONArray("self_select_menus");
			for(int i = 0; i < contentJSONArray.length(); i++) {
				JSONObject menuMenuSetLUJsonObject = contentJSONArray.getJSONObject(i);
				MenuSetMenuLookup oMenuSetMenuLookup = new MenuSetMenuLookup(menuMenuSetLUJsonObject);
				m_oLookupList.add(oMenuSetMenuLookup);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			this.init();
		}
	}
	
	// Get function
	public MenuSetMenuLookup getLookup(int iId){
		if(iId > m_oLookupList.size()-1)
			return null;
		
		return m_oLookupList.get(iId);
	}
	
	public List<MenuSetMenuLookup> getLookupList(){
		return m_oLookupList;
	}
	
	// init value
	public void init() {
		this.m_oLookupList = null;
	}
}
