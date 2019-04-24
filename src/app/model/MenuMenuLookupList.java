package app.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuMenuLookupList {

	private List<MenuMenuLookup> m_oLookupList;
	
	public MenuMenuLookupList(){
		m_oLookupList = new ArrayList<MenuMenuLookup>();
	}
	
	public void readMenuMenuLookupListById(int Id){
		MenuMenuLookup oMenuMenuLookupList = new MenuMenuLookup();
		JSONObject responseJsonObject = null;
		JSONArray subMenuJSONArray = null;
		JSONArray itemJSONArray = null;
		
		responseJsonObject = oMenuMenuLookupList.readMenuMenuLookupListById(Id);
		try {
			// if any sub_menu exists
			subMenuJSONArray = responseJsonObject.optJSONArray("sub_menus");
			if(subMenuJSONArray != null) {
				for(int i=0; i<subMenuJSONArray.length(); i++) {
					JSONObject menuMenuLUJsonObject = subMenuJSONArray.getJSONObject(i).optJSONObject("MenuMenuLookup");
					if(menuMenuLUJsonObject == null) {
						OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", "Missing menu menu lookup");
						continue;
					}
					
					JSONObject menuMenuJsonObject = subMenuJSONArray.getJSONObject(i).optJSONObject("MenuSubMenu");
					if(menuMenuJsonObject == null) {
						OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", "Missing menu menu");
						continue;
					}
					
					menuMenuLUJsonObject.put("MenuSubMenu", menuMenuJsonObject);
					if(!subMenuJSONArray.getJSONObject(i).isNull("media_objects"))
						menuMenuLUJsonObject.put("media_objects", subMenuJSONArray.getJSONObject(i).getJSONArray("media_objects"));
					MenuMenuLookup oMenuMenuLookup = new MenuMenuLookup(menuMenuLUJsonObject);
					m_oLookupList.add(oMenuMenuLookup);
				}
			}
			
			// if any item exists
			itemJSONArray = responseJsonObject.optJSONArray("items");
			if(itemJSONArray != null) {
				for(int i=0; i<itemJSONArray.length(); i++) {
					JSONObject menuItemLUJsonObject = itemJSONArray.getJSONObject(i).optJSONObject("MenuMenuLookup");
					if(menuItemLUJsonObject == null) {
						OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", "Missing menu menu lookup");
						continue;
					}
					
					JSONObject menuItemJsonObject = itemJSONArray.getJSONObject(i).optJSONObject("MenuItem");
					if(menuItemJsonObject == null) {
						OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", "Missing menu item");
						continue;
					}

					menuItemLUJsonObject.put("MenuItem", menuItemJsonObject);
					if(!itemJSONArray.getJSONObject(i).isNull("media_objects"))
						menuItemLUJsonObject.put("media_objects", itemJSONArray.getJSONObject(i).getJSONArray("media_objects"));
					MenuMenuLookup oMenuMenuLookup = new MenuMenuLookup(menuItemLUJsonObject);
					m_oLookupList.add(oMenuMenuLookup);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			this.init();
		}
	}
	
	// Get function
	public MenuMenuLookup getLookup(int iId){
		if(iId > m_oLookupList.size()-1)
			return null;
		
		return m_oLookupList.get(iId);
	}
	
	public List<MenuMenuLookup> getLookupList(){
		return m_oLookupList;
	}
	
	// init value
	public void init() {
		this.m_oLookupList = null;
	}
}
