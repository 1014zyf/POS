package om;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuItemGroupLookupList {

	private List<MenuItemGroupLookup> m_oLookupList;
	
	public MenuItemGroupLookupList(){
		m_oLookupList = new ArrayList<MenuItemGroupLookup>();
	}
	
	public void readAll(){
		MenuItemGroupLookup oMenuItemGroupLookupList = new MenuItemGroupLookup();
		JSONArray responseJSONObject = oMenuItemGroupLookupList.readAll();
		readAllByJSONArray(responseJSONObject);
	}
	
	public void readAllByJSONArray(JSONArray oJsonArray) {
		if(oJsonArray == null)
			return;
		try {
			for(int i = 0; i < oJsonArray.length(); i++) {
				JSONObject menuItemGroupLookupJsonObject = oJsonArray.getJSONObject(i).getJSONObject("MenuItemGroupLookup");
				MenuItemGroupLookup oMenuItemGroupLookup = new MenuItemGroupLookup(menuItemGroupLookupJsonObject);
				m_oLookupList.add(oMenuItemGroupLookup);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			this.init();
		}
	}
	
	// Get function
	public MenuItemGroupLookup getLookup(int iId){
		if(iId > m_oLookupList.size()-1)
			return null;
		
		return m_oLookupList.get(iId);
	}
	
	public List<MenuItemGroupLookup> getLookupList(){
		return m_oLookupList;
	}
	
	public List<MenuItemGroupLookup> getLookupListByItemGroupIds(ArrayList<String> oItemGroupList){
		return m_oLookupList;
	}
	
	// init value
	public void init() {
		this.m_oLookupList = null;
	}
}
