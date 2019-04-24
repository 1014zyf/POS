package app.model;

import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class MenuPriceLevelList {
	private TreeMap<Integer, MenuPriceLevel> m_oPriceLevelList;
	
	public MenuPriceLevelList() {
		m_oPriceLevelList = new TreeMap<Integer, MenuPriceLevel>();
	}
	
	public TreeMap<Integer, MenuPriceLevel> getMenuPriceLevelList() {
		return this.m_oPriceLevelList;
	}
	
	//Get all menu price level
	public void readAll() {
		MenuPriceLevel oPriceLevel = new MenuPriceLevel();
		JSONArray responseJSONArray = oPriceLevel.readAll();
		
		if (responseJSONArray == null)
			return;
		
		for (int i = 0; i < responseJSONArray.length(); i++) {
			if (responseJSONArray.isNull(i))
				continue;
			
			JSONObject priceLevelJSONObject = responseJSONArray.optJSONObject(i);
			MenuPriceLevel oTempPriceLevel = new MenuPriceLevel(priceLevelJSONObject);
			m_oPriceLevelList.put(oTempPriceLevel.getLevel(), oTempPriceLevel);
		}
	}
	
}