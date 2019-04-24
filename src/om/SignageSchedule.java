package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignageSchedule {
	int schdId;
	int outletId;
	int dpsnId;
	int perdId;
	int themId;
	
	//init object with initialize value
	public SignageSchedule() {
		this.init();
	}
	
	//init obejct with JSONObject
	public SignageSchedule(JSONObject signageScheduleJSONObject) {
		readDataFromJson(signageScheduleJSONObject);
	}
	
	//reset the object
	private void init() {
		this.schdId = 0;
		this.outletId = 0;
		this.dpsnId = 0;
		this.perdId = 0;
		this.themId = 0;
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray signageScheduleJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("signageSchedules")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("signageSchedules")) {
				return null;
			}
			
			signageScheduleJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("signageSchedules");
		}
		
		return signageScheduleJSONArray;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject signageScheduleJSONObject) {
		JSONObject resultSignageSchedule = null;
		
		resultSignageSchedule = signageScheduleJSONObject.optJSONObject("SignSchedule");
		if(resultSignageSchedule == null)
			resultSignageSchedule = signageScheduleJSONObject;
		
		System.out.println("resultSignageSchedule: "+resultSignageSchedule.toString());
		
		this.init();
		this.schdId = resultSignageSchedule.optInt("schd_id");
		this.outletId = resultSignageSchedule.optInt("schd_olet_id");
		this.dpsnId = resultSignageSchedule.optInt("schd_dpsn_id");
		this.perdId = resultSignageSchedule.optInt("schd_perd_id");
		this.themId = resultSignageSchedule.optInt("schd_them_id");
	}
	
	public JSONArray readByShopId(int iShopId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("shopId", Integer.toString(iShopId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "signage", "getSignageScheduleListByShopId", requestJSONObject.toString());
		
		return responseJSONArray;
	}
	
	//get schdId
	public int getOutletId() {
		return this.outletId;
	}
}

