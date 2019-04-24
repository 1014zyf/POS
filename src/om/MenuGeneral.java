package om;

import org.json.JSONObject;

public class MenuGeneral {
	MenuPriceLevelList m_oMenuPriceLevelList;
	MenuItemGroupLookupList m_oMenuItemGroupLookupList;
	
	public MenuGeneral() {
		m_oMenuPriceLevelList = null;
		m_oMenuItemGroupLookupList = null;
	}
	
	//read menu price level list and menu item group lookup list
	public void readMenuItemList() {
		this.readDataFromApi("gm", "menu", "getMenuItemList", "");
	}
	
	//read data from POS API
	private void readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return;
		
		JSONObject tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse();
		if (tempJSONObject == null)
			return;
		
		if (sFcnName.equals("getMenuItemList")) {
			if (!tempJSONObject.has("price_levels"))
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
			else if (!tempJSONObject.isNull("price_levels")) {
				m_oMenuPriceLevelList = new MenuPriceLevelList();
				m_oMenuPriceLevelList.readAllByJSONArray(tempJSONObject.optJSONArray("price_levels"));
			}
			
			if (tempJSONObject.has("menuItemGroupLookups") && !tempJSONObject.isNull("menuItemGroupLookups")) {
				m_oMenuItemGroupLookupList = new MenuItemGroupLookupList();
				m_oMenuItemGroupLookupList.readAllByJSONArray(tempJSONObject.optJSONArray("menuItemGroupLookups"));
			}
		}
	}
	
	public MenuPriceLevelList getMenuPriceLevelList() {
		return m_oMenuPriceLevelList;
	}
	
	public MenuItemGroupLookupList getMenuItemGroupLookupList() {
		return m_oMenuItemGroupLookupList;
	}
}
