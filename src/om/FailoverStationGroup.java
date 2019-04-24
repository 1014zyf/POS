package om;

import org.json.JSONException;
import org.json.JSONObject;

public class FailoverStationGroup {
	private int stgpId;
	private int shopId;
	private String[] name;
	private JSONObject settings;
	private String status;
	
	private final static String STATUS_ACTIVE = "";
	
	private String model;
	private int port;
	private int sslPort;
	
	public FailoverStationGroup() {
		this.init();
	}
	
	public FailoverStationGroup(JSONObject oFailoverStationGroupJSONObject) {
		this.readDataFromJson(oFailoverStationGroupJSONObject);
	}
	
	public boolean readStationGroup() {
		return this.readDataFromApi("gm", "failover", "getStationGroupByRetrievingUDID", "");
	}
	
	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bResult = true;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bResult = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("StationGroup")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("StationGroup"))
				return false;
			
			this.readDataFromJson(OmWsClientGlobal.g_oWsClient.get().getResponse());
		}
		
		return bResult;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject stationGroupJSONObject) {
		JSONObject resultStationGroup = stationGroupJSONObject.optJSONObject("StationGroup");
		if(resultStationGroup == null)
			resultStationGroup = stationGroupJSONObject;
		
		this.init();
		
		this.stgpId = resultStationGroup.optInt("stgp_id");
		this.shopId = resultStationGroup.optInt("stgp_shop_id");
		for(int i = 1; i <= 5; i++)
			this.name[(i-1)] = resultStationGroup.optString("stgp_name_l"+i);
		
		try {
			this.settings = new JSONObject(resultStationGroup.optString("stgp_settings"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.status = resultStationGroup.optString("stgp_status", FailoverStationGroup.STATUS_ACTIVE);
		
		JSONObject workstationJSONObject = stationGroupJSONObject.optJSONObject("Workstation");
		if(workstationJSONObject != null){
			this.port = workstationJSONObject.optInt("mq_port", 0);
			this.sslPort = workstationJSONObject.optInt("mq_ssl_port", 0);
			this.model = workstationJSONObject.optString("model", "");
		}
	}
	
	// init value
	private void init() {
		this.stgpId = 0;
		this.shopId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(int i = 0; i < 5; i++)
			this.name[i] = "";
		this.settings = null;
		this.status = FailoverStationGroup.STATUS_ACTIVE;
		
		this.model = "";
		this.port = 0;
		this.sslPort = 0;
	}
	
	public int getStgpId() {
		return this.stgpId;
	}
	
	public int getShopId() {
		return this.shopId;
	}
	
	public String getCommonSettingsByKey(String sKey) {
		JSONObject oCommonJSONObject = settings.optJSONObject("common");
		if(oCommonJSONObject == null)
			return "";
		
		return oCommonJSONObject.optString(sKey);
	}
	
	public String getModel() {
		return this.model;
	}
	
	public int getPort(){
		return this.port;
	}
	
	public int getSSLPort(){
		return this.sslPort;
	}
}