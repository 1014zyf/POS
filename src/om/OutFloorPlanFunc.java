package om;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OutFloorPlanFunc {

	private LinkedHashMap<Integer, OutFloorPlan> m_oOutFloorPlanList;
	private HashMap<Integer, Integer> m_oFloorPlanMediaIdList;
	
	//init with initialized value
	public OutFloorPlanFunc() {
		m_oOutFloorPlanList = new LinkedHashMap<Integer, OutFloorPlan>();
		m_oFloorPlanMediaIdList = new HashMap<Integer, Integer>();
	}
	
	public int getFloorPlanCount(){
		return m_oOutFloorPlanList.size();
	}
	
	public OutFloorPlan getFloorPlan(int iFloorPlanId){
		return m_oOutFloorPlanList.get(iFloorPlanId);
	}
	
	public HashMap<Integer, OutFloorPlan> getFloorPlanList(){
		return m_oOutFloorPlanList;
	}
	
	public boolean readByOutletId(int iOutletId) {
		int i = 0;
		boolean bSuccess = false;
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("recursive", "1");
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		bSuccess = this.readDataFromApi("gm", "outlet", "getFloorPlanByOutlet", requestJSONObject.toString());

		if (!m_oFloorPlanMediaIdList.isEmpty()) {
			MedMediaList oMediaList = new MedMediaList();
			oMediaList.readMediasByIdList(m_oFloorPlanMediaIdList);
			
			for(OutFloorPlan oOutFloorPlan:m_oOutFloorPlanList.values()) {
				for(i=0; i<oOutFloorPlan.getMapCount(); i++) {
					OutFloorPlanMap oFloorPlanMap = oOutFloorPlan.getMap(i);
					if(oFloorPlanMap.getMediaId() > 0)
						oFloorPlanMap.setFloorPlanMedia(oMediaList.getMediaById(oFloorPlanMap.getMediaId()));
				}
			}
		}
		
		return bSuccess;
	}
	
	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bResult = true;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bResult = false;
		else {
			try {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
					return false;
				
				if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("floor_plan")) {
					OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
					return false;
				}
				
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("floor_plan"))
					return false;
				
				JSONArray tempJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().getJSONArray("floor_plan");
				readDataFromJSONArray(tempJSONArray);
			}catch(JSONException jsone) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.stackToString(jsone));
				bResult = false;
			}
			
		}
		
		return bResult;
	}
	
	// read data from JSON Array
	public boolean readDataFromJSONArray(JSONArray oJsonArray) {
		int i = 0, j = 0, k = 0;
		boolean bResult = true;
		JSONObject tempJSONObject = null;
		JSONObject tempJSONObject2 = null;
		JSONObject tempJSONObject3 = null;
		JSONObject tempJSONObject4 = null;
		JSONObject tempJSONObject5 = null;
		JSONObject tempJSONObject6 = null;
		JSONArray tempJSONArray = null;
		JSONArray tempJSONArray2 = null;
		
		try {
			for (i = 0; i < oJsonArray.length(); i++) {
				tempJSONObject = oJsonArray.getJSONObject(i);
				tempJSONObject2 = tempJSONObject.getJSONObject("OutFloorPlan");
				
				OutFloorPlan oOutFloorPlan = new OutFloorPlan(tempJSONObject2);
				
				tempJSONArray = tempJSONObject.optJSONArray("OutFloorPlanMapList");
				if (tempJSONArray != null) {
					for (j = 0; j < tempJSONArray.length(); j++) {
						tempJSONObject3 = tempJSONArray.getJSONObject(j);
						tempJSONObject4 = tempJSONObject3.getJSONObject("OutFloorPlanMap");
						
						OutFloorPlanMap oOutFloorPlanMap = new OutFloorPlanMap(tempJSONObject4);
						// If floor plan image is set, put to the list for
						// process later
						if (oOutFloorPlanMap.getMediaId() > 0 && m_oFloorPlanMediaIdList.get(oOutFloorPlanMap.getMediaId()) == null)
							m_oFloorPlanMediaIdList.put(oOutFloorPlanMap.getMediaId(), oOutFloorPlanMap.getMediaId());
						
						if (tempJSONObject3.has("OutFloorPlanTableList")) {
							tempJSONArray2 = tempJSONObject3.getJSONArray("OutFloorPlanTableList");
							for (k = 0; k < tempJSONArray2.length(); k++) {
								tempJSONObject5 = tempJSONArray2.getJSONObject(k);
								tempJSONObject6 = tempJSONObject5.getJSONObject("OutFloorPlanTable");
								
								OutFloorPlanTable oOutFloorPlanTable = new OutFloorPlanTable(tempJSONObject6);
								oOutFloorPlanMap.addTable(oOutFloorPlanTable);
							}
						}
						
						oOutFloorPlan.addMap(oOutFloorPlanMap);
					}
				}
				
				m_oOutFloorPlanList.put(oOutFloorPlan.getFloorPlanId(), oOutFloorPlan);
			}

			if (!m_oFloorPlanMediaIdList.isEmpty())
				this.readFloorPlanMedia();
		}catch(JSONException jsone) {
			OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.stackToString(jsone));
			bResult = false;
		}
		
		return bResult;
	}
	
	public void readFloorPlanMedia(){
		if (!m_oFloorPlanMediaIdList.isEmpty()) {
			MedMediaList oMediaList = new MedMediaList();
			oMediaList.readMediasByIdList(m_oFloorPlanMediaIdList);
			
			for(OutFloorPlan oOutFloorPlan:m_oOutFloorPlanList.values()) {
				for(int i = 0; i < oOutFloorPlan.getMapCount(); i++) {
					OutFloorPlanMap oFloorPlanMap = oOutFloorPlan.getMap(i);
					if(oFloorPlanMap.getMediaId() > 0)
						oFloorPlanMap.setFloorPlanMedia(oMediaList.getMediaById(oFloorPlanMap.getMediaId()));
				}
			}
		}
	}
}