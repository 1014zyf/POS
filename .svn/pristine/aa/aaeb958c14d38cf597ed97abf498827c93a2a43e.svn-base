package app.model;

import java.util.HashMap;

import org.json.JSONArray;

public class PosMixAndMatchItemList {

	private HashMap<Integer, PosMixAndMatchItem> m_oMixAndMatchItemList;
	
	public PosMixAndMatchItemList(){
		m_oMixAndMatchItemList = new HashMap<Integer, PosMixAndMatchItem>();
	}
	
	//read all valid record
	public void readAllValidItems(JSONArray oRuleIdList, JSONArray oItemIdList, JSONArray oDeptIdList, JSONArray oCatIdList) {
		PosMixAndMatchItem oPosMixAndMatchItemList = new PosMixAndMatchItem(), oPosMixAndMatchItem = null;
		JSONArray responseJSONArray = oPosMixAndMatchItemList.readAllValidItems(oRuleIdList, oItemIdList, oDeptIdList, oCatIdList);
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				if (responseJSONArray.isNull(i))
					continue;
				
				oPosMixAndMatchItem = new PosMixAndMatchItem(responseJSONArray.optJSONObject(i));
				
				// Add to function list
				m_oMixAndMatchItemList.put(oPosMixAndMatchItem.getItemId(), oPosMixAndMatchItem);
			}
		}
	}
	
	public HashMap<Integer, PosMixAndMatchItem> getItemList(){
		return m_oMixAndMatchItemList;
	}
	
}
