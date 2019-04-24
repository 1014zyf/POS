package app.model;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosPantryMessageList {
	private HashMap<Integer, PosPantryMessage> m_oDPantryMessageList;
	
	public PosPantryMessageList() {
		m_oDPantryMessageList = new HashMap<Integer, PosPantryMessage>();
	}
	
	//get all void reason
	public void readAll() {
		PosPantryMessage oPantryMesg = new PosPantryMessage(), oTempPantryMesg = null;
		JSONArray responseJSONArray = oPantryMesg.readAll();
		
		if(responseJSONArray == null)
			return;
			
		for (int i = 0; i < responseJSONArray.length(); i++) {
			if (responseJSONArray.isNull(i))
				continue;
		
			JSONObject pantryMessageJSONObject = responseJSONArray.optJSONObject(i);
			oTempPantryMesg = new PosPantryMessage(pantryMessageJSONObject);
			m_oDPantryMessageList.put(oTempPantryMesg.getPanmId(), oTempPantryMesg);
		}
		
	}
	
	public PosPantryMessage getPosPantryMessageByIndex(int iIndex){
		return this.m_oDPantryMessageList.get(iIndex);
	}
	
	public HashMap<Integer, PosPantryMessage> getPosPantryMessageList() {
		return this.m_oDPantryMessageList;
	}

}
