package om;

import org.json.JSONObject;
import org.json.JSONException;

public class OutletGeneral {
	OutShop m_oOutShop;
	OutFloorPlanFunc m_oOutFloorPlanFunc;
	OutTableSectionList m_oOutTableSectionList;
	
	public OutletGeneral() {
		m_oOutShop = null;
		m_oOutFloorPlanFunc = null;
		m_oOutTableSectionList = null;
	}
	
	//read shop and floor plan by shop_id and outlet_id
	public void readShopFloorPlanOutletSection(int iShopId, int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("shopId", iShopId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("recursive", 1);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		this.readDataFromApi("gm", "outlet", "getShopFloorPlanOutletSection", requestJSONObject.toString());
	}
	
	//read data from POS API
	private void readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return ;
		else {
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse();
			if(tempJSONObject == null)
				return ;
			
			if(!tempJSONObject.has("shop"))
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
			else if(!tempJSONObject.isNull("shop")) {
				JSONObject tempJSONObject2 = tempJSONObject.optJSONObject("shop");
				if (!tempJSONObject2.isNull("OutShop")) {
					m_oOutShop = new OutShop(tempJSONObject2);
				}
			}
			
			if(!tempJSONObject.has("floorPlan")) 
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
			else if(!tempJSONObject.isNull("floorPlan")) {
				m_oOutFloorPlanFunc = new OutFloorPlanFunc();
				m_oOutFloorPlanFunc.readDataFromJSONArray(tempJSONObject.optJSONArray("floorPlan"));
			}
			
			if(tempJSONObject.has("tableSections") || tempJSONObject.has("tableSection")) {
				String sKeyName = "tableSections";
				if(tempJSONObject.has("tableSection"))
					sKeyName = "tableSection";
				
				if(!tempJSONObject.isNull(sKeyName)) {
					m_oOutTableSectionList = new OutTableSectionList();
					m_oOutTableSectionList.getAllTableSectionsByJSONArray(tempJSONObject.optJSONArray(sKeyName));
				}
			}
		}
	}
	
	public OutShop getOutShop() {
		return m_oOutShop;
	}
	
	public OutFloorPlanFunc getOutFloorPlanFunc() {
		return m_oOutFloorPlanFunc;
	}
	
	public OutTableSectionList getOutTableSectionList() {
		return m_oOutTableSectionList;
	}
}
