package om;

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
		PosTableMessage oTableMessage = new PosTableMessage();
		JSONArray responseJSONArray = oTableMessage.readAll();
		
		this.readAllByJSONArray(responseJSONArray);
	}
	
	public void readAllByJSONArray(JSONArray oJsonArray) {
		if (oJsonArray == null)
			return;
		
		for (int i = 0; i < oJsonArray.length(); i++) {
			JSONObject tableMessageJSONObject = oJsonArray.optJSONObject(i);
			if (tableMessageJSONObject == null)
				continue;
			
			PosTableMessage oTempTableMessage = new PosTableMessage(tableMessageJSONObject);
			m_oDTableMessageList.put(oTempTableMessage.getTblmId(), oTempTableMessage);
		}
	}
	
}