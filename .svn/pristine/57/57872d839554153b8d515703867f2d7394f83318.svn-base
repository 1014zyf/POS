package core.manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import core.virtualui.HeroActionProtocol;

public class ResponsePacketManager {
	JSONArray m_oViewJsonArray;
	JSONObject m_oSystemJsonObject;
	
	public ResponsePacketManager() {
		m_oViewJsonArray = new JSONArray();
		m_oSystemJsonObject = new JSONObject();
	}
	
	public void addViewJsonObject(JSONObject oViewJsonObject) {
		if (oViewJsonObject != null && oViewJsonObject.length() == 0)
			return;
		
		m_oViewJsonArray.put(oViewJsonObject);
	}
	
	public void setSystemJsonObject(JSONObject oSystemJsonObject) {
		if (oSystemJsonObject == null)
			return;
		m_oSystemJsonObject = oSystemJsonObject;
	}
	
//	public void setSystemOperation(String sOperation, JSONObject oOperationJsonObject) {
//		if (sOperation == null || sOperation.isEmpty())
//			return;
//		
//		if (oOperationJsonObject == null)
//			oOperationJsonObject = new JSONObject();
//		
//		try {
//			m_oSystemJsonObject.put(sOperation, oOperationJsonObject);
//		} 
//		catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
	
//	public void removeSystemOperation(String sOperation) {
//		if (sOperation == null || sOperation.isEmpty())
//			return;
//		m_oSystemJsonObject.remove(sOperation);
//	}
	
	public JSONObject getResponsePacket() {
		JSONObject oResponsePacketJsonObject = new JSONObject();
		try {
			if (m_oViewJsonArray.length() > 0)
				oResponsePacketJsonObject.put(HeroActionProtocol.View.KEY, m_oViewJsonArray);
			
			if (m_oSystemJsonObject.length() > 0)
				oResponsePacketJsonObject.put(HeroActionProtocol.System.KEY, m_oSystemJsonObject);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return oResponsePacketJsonObject;
	}
	
	public void clear() {
		m_oViewJsonArray = new JSONArray();
		m_oSystemJsonObject = new JSONObject();
	}
}
