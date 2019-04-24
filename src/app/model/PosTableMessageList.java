package app.model;

import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosTableMessageList {
	private TreeMap<Integer, PosTableMessage> m_oDTableMessageList;
	
	public PosTableMessageList() {
		m_oDTableMessageList = new TreeMap<Integer, PosTableMessage>();
	}
	
	public TreeMap<Integer, PosTableMessage> getPosTableMessageList() {
		return this.m_oDTableMessageList;
	}
	
	//Get all table message
	public void readAll() {
		PosTableMessage oTableMessage = new PosTableMessage(), oTempTableMessage = null;
		JSONObject tableMessageJSONObject = null;
		JSONArray responseJSONArray = oTableMessage.readAll();
		
		if (responseJSONArray == null)
			return;
		
		for (int i = 0; i < responseJSONArray.length(); i++) {
			tableMessageJSONObject = responseJSONArray.optJSONObject(i);
			if (tableMessageJSONObject == null)
				continue;
			
			oTempTableMessage = new PosTableMessage(tableMessageJSONObject);
			m_oDTableMessageList.put(oTempTableMessage.getTblmId(), oTempTableMessage);
		}
	}
	
}