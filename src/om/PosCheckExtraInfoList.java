package om;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosCheckExtraInfoList {
	private ArrayList<PosCheckExtraInfo> m_oCheckExtraInfoList;
	private ArrayList<ArrayList<PosCheckExtraInfo>> m_oCheckExtraInfoListByCheckLevel;
	
	public PosCheckExtraInfoList() {
		m_oCheckExtraInfoList = new ArrayList<PosCheckExtraInfo>();
		m_oCheckExtraInfoListByCheckLevel = new ArrayList<ArrayList<PosCheckExtraInfo>>();
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
		m_oCheckExtraInfoListByCheckLevel = new ArrayList<ArrayList<PosCheckExtraInfo>>();
	}
	
	//Get all records by status
	public void readAllByCheckIds(String sConfigBy, ArrayList<String> oCheckIds, String sStatus) {
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
	
	//Get all records by check ids and section
	public void readAllBySectionVariableOutletIdBdayId(String sSection, String sVariable, int iOutletId, String sBDayId) {
		PosCheckExtraInfo oCheckExtraInfo = new PosCheckExtraInfo();
		JSONArray responseJSONArray = oCheckExtraInfo.readAllBySectionVariableOutletIdBdayId(sSection, sVariable, iOutletId, sBDayId);
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
	
	//Get all records by check id
	public void readAllByCheckId(String sCheckId) {
		PosCheckExtraInfo oCheckExtraInfo = new PosCheckExtraInfo();
		JSONArray responseJSONArray = oCheckExtraInfo.readAllByCheckId(sCheckId);
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
			
			return (!m_oCheckExtraInfoList.isEmpty());
		}
		
		return false;
	}

	public void readAllAdvancedOrderBySearchingInfo(JSONObject oSearchingInfoJSON) {
		PosCheckExtraInfo oCheckExtraInfo = new PosCheckExtraInfo();
		JSONArray responseJSONArray = oCheckExtraInfo.readAllAdvancedOrderBySearchingInfo(oSearchingInfoJSON);
		if (responseJSONArray == null)
			return;
		
		for (int i = 0; i < responseJSONArray.length(); i++) {
			if (responseJSONArray.isNull(i))
				continue;
			
			JSONArray oTmpJSONArray = responseJSONArray.optJSONArray(i);
			
			ArrayList<PosCheckExtraInfo> oPosCheckExtraList = new ArrayList<PosCheckExtraInfo>();
			for (int j = 0; j < oTmpJSONArray.length(); j++) {
				if (oTmpJSONArray.optJSONObject(j) == null)
					continue;
				JSONObject oTmpJsonObject = oTmpJSONArray.optJSONObject(j);
				PosCheckExtraInfo oTempCheckExtraInfo = new PosCheckExtraInfo(oTmpJsonObject);
				oPosCheckExtraList.add(oTempCheckExtraInfo);
			}
			m_oCheckExtraInfoListByCheckLevel.add(oPosCheckExtraList);
		}
	}
	
	public ArrayList<PosCheckExtraInfo> getCheckExtraInfoList() {
		return m_oCheckExtraInfoList;
	}
	
	public ArrayList<ArrayList<PosCheckExtraInfo>> getCheckExtraInfoListByCheckLevel() {
		return m_oCheckExtraInfoListByCheckLevel;
	}
}
