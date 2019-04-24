package om;

import java.util.TreeMap;
import java.util.Map.Entry;

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
		
		this.readAllByJSONArray(responseJSONArray);
	}
	
	public void readAllByJSONArray(JSONArray oJsonArray) {
		if(oJsonArray == null)
			return;
		
		for (int i = 0; i < oJsonArray.length(); i++) {
			if (oJsonArray.isNull(i))
				continue;
			
			JSONObject priceLevelJSONObject = oJsonArray.optJSONObject(i);
			MenuPriceLevel oTempPriceLevel = new MenuPriceLevel(priceLevelJSONObject);
			m_oPriceLevelList.put(oTempPriceLevel.getLevel(), oTempPriceLevel);
		}
	}
	
	//check whether have target price level exist
	public boolean isPriceLevelExist(int iTargetPriceLevel) {
		boolean bExist = false;
		
		for (Entry<Integer, MenuPriceLevel> entry : m_oPriceLevelList.entrySet()) {
			if(entry.getValue().getLevel() == iTargetPriceLevel) {
				bExist = true;
				break;
			}
		}
		
		return bExist;
	}
}