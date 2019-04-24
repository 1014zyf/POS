package app.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosVoidReasonList {
	private HashMap<Integer, PosVoidReason> m_oVoidReasonList;
	
	public PosVoidReasonList() {
		m_oVoidReasonList = new HashMap<Integer, PosVoidReason>();
	}
	
	//get check item lists from database by check id
	public void getVoidReasonsByType(String sType) {
		PosVoidReason oVoidReason = new PosVoidReason(), oTempVoidReason = null;
		
		JSONArray responseJSONArray = oVoidReason.getVoidReasonByType(sType);
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				JSONObject voidReasonJSONObject = responseJSONArray.optJSONObject(i);
				if (voidReasonJSONObject == null)
					continue;
				
				oTempVoidReason = new PosVoidReason(voidReasonJSONObject);
				m_oVoidReasonList.put(oTempVoidReason.getVdrsId(), oTempVoidReason);
			}
		}
	}
	
	//get all void reason
	public void readAll() {
		PosVoidReason oVoidReason = new PosVoidReason(), oTempVoidReason = null;
		JSONArray responseJSONArray = oVoidReason.readAll();
		
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				JSONObject voidReasonJSONObject = responseJSONArray.optJSONObject(i);
				if (voidReasonJSONObject == null)
					continue;
				
				oTempVoidReason = new PosVoidReason(voidReasonJSONObject);
				m_oVoidReasonList.put(oTempVoidReason.getVdrsId(), oTempVoidReason);
			}
		}
		
	}
	
	// Get void reason by type
	public ArrayList<PosVoidReason> getVoidReasonListByType(String sType){
		ArrayList<PosVoidReason> oVoidReasonList = new ArrayList<PosVoidReason>();
		
		for(PosVoidReason oVoidReason: m_oVoidReasonList.values()) {
			if(oVoidReason.getType().equals(sType))
				oVoidReasonList.add(oVoidReason);
		}
		
		return oVoidReasonList;
	}

	// Get void reason by type
	public PosVoidReason getVoidReasonListById(int iId){
		for(PosVoidReason oVoidReason: m_oVoidReasonList.values()) {
			if(oVoidReason.getVdrsId() == iId)
				return oVoidReason;
		}
		return null;
	}
	
	public HashMap<Integer, PosVoidReason> getVoidReasonList() {
		return this.m_oVoidReasonList;
	}
}
