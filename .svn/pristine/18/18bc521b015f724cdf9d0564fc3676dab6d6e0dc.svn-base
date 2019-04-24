package app.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosCheckExtraInfoList {
	private ArrayList<PosCheckExtraInfo> m_oCheckExtraInfoList;
	
	public PosCheckExtraInfoList() {
		m_oCheckExtraInfoList = new ArrayList<PosCheckExtraInfo>();
	}
	
	public PosCheckExtraInfoList(JSONArray oExtraInfoJSONArray) {
		if (oExtraInfoJSONArray == null)
			return;
		
		m_oCheckExtraInfoList = new ArrayList<PosCheckExtraInfo>();
		for (int i = 0; i < oExtraInfoJSONArray.length(); i++) {
			if (oExtraInfoJSONArray.isNull(i))
				continue;
			
			JSONObject oCheckExtraInfoJSONObject = oExtraInfoJSONArray.optJSONObject(i);
			PosCheckExtraInfo oCheckExtraInfo = new PosCheckExtraInfo(oCheckExtraInfoJSONObject);
			m_oCheckExtraInfoList.add(oCheckExtraInfo);
		}
	}
	
	//Get all records by status
	public void readAllByCheckIds(String sConfigBy, ArrayList<Integer> oCheckIds, String sStatus) {
		PosCheckExtraInfo oCheckExtraInfo = new PosCheckExtraInfo();
		JSONArray responseJSONArray = oCheckExtraInfo.readAllByCheckIds(sConfigBy, oCheckIds, sStatus);
		if (responseJSONArray == null)
			return;
		
		for (int i = 0; i < responseJSONArray.length(); i++) {
			if (responseJSONArray.isNull(i))
				continue;
			
			JSONObject checkExtraInfoJSONObject = responseJSONArray.optJSONObject(i);
			PosCheckExtraInfo oTempCheckExtraInfo = new PosCheckExtraInfo(checkExtraInfoJSONObject);
			m_oCheckExtraInfoList.add(oTempCheckExtraInfo);
		}
	}
	
	public boolean addUpdateWithMultipleRecords(ArrayList<PosCheckExtraInfo> oPosCheckExtraInfos) {
		PosCheckExtraInfo oPosCheckExtraInfoList = new PosCheckExtraInfo(), oPosChkExtraInfo = null;
		JSONArray responseJSONArray = null;
		
		responseJSONArray = oPosCheckExtraInfoList.addUpdateWithMutlipleRecords(oPosCheckExtraInfos);
		if(responseJSONArray != null) {
			for(int i = 0; i < responseJSONArray.length(); i++) {
				try {
					oPosChkExtraInfo = new PosCheckExtraInfo(responseJSONArray.getJSONObject(i));
					m_oCheckExtraInfoList.add(oPosChkExtraInfo);
				}
				catch(JSONException jsone) {
					jsone.printStackTrace();
					return false;
				}
			}
			
			if(!m_oCheckExtraInfoList.isEmpty())
				return true;
			else
				return false;
		}
		
		return false;
	}
	
	public ArrayList<PosCheckExtraInfo> getCheckExtraInfoList() {
		return m_oCheckExtraInfoList;
	}
}
