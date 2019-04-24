package om;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;
import app.ClsAlertMessage;

public class PosStationAlertSetting {
	
	private int id;
	private int shopId;
	private int outletId;
	private int stationId;
	private String typeKey;
	private JSONObject setting;
	private String status;
	
	public PosStationAlertSetting() {
		this.init();
	}
	
	public  PosStationAlertSetting(JSONObject oAlertSettingJSONObject) {
		this.readDataFromJson(oAlertSettingJSONObject);
	}
	//init Value
	public void init() {
		this.id = 0;
		this.shopId = 0;
		this.outletId = 0;
		this.stationId = 0;
		this.typeKey = "";
		this.setting = null;
		this.status = null;
	}
	
	public void readDataFromJson(JSONObject oAlertSettingJSONObject) {
		
		this.init();
		if(oAlertSettingJSONObject == null)
			return;
		
		JSONObject oTempAlertSettingJSON = oAlertSettingJSONObject.optJSONObject("PosStationAlertSetting");
		if(oTempAlertSettingJSON == null)
			return;
		System.out.println(oAlertSettingJSONObject);
		System.out.println(oTempAlertSettingJSON);
		if (oTempAlertSettingJSON.has("stas_id"))
			this.id = oTempAlertSettingJSON.optInt("stas_id",0);
		if (oTempAlertSettingJSON.has("stas_shop_id"))
			this.shopId = oTempAlertSettingJSON.optInt("stas_shop_id",0);
		if (oTempAlertSettingJSON.has("stas_olet_id"))
			this.outletId = oTempAlertSettingJSON.optInt("stas_olet_id",0);
		if (oTempAlertSettingJSON.has("stas_stat_id"))
			this.stationId = oTempAlertSettingJSON.optInt("stas_stat_id",0);
		if (oTempAlertSettingJSON.has("stas_type_key"))
			this.typeKey = oTempAlertSettingJSON.optString("stas_type_key","");
		if (oTempAlertSettingJSON.has("stas_settings")) { 
			 String sJSONstRING = oTempAlertSettingJSON.optString("stas_settings");
			 try {
				this.setting = new JSONObject(sJSONstRING);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				AppGlobal.stack2Log(e);
			}		 
		}
		if (oTempAlertSettingJSON.has("stas_status")) {
			this.status = oTempAlertSettingJSON.optString("stas_status","-");
		}
		
	}
	
	//read Data from POS API
	protected static JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam ,String indicator) {
		JSONArray oPosStationAlertSettingJSONArray = null;
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			//do error checking
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has(indicator)) {
				//OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().isNull(indicator))
				return null;
			
			System.out.println(OmWsClientGlobal.g_oWsClient.get().getResponse());
			oPosStationAlertSettingJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray(indicator);
		};
		return oPosStationAlertSettingJSONArray;
	}
	
	public static HashMap<String ,HashMap<Integer ,ArrayList<Integer>>> readConvertedTypeIdStationIdListTable() {
		JSONArray oResponJSONArray = readDataListFromApi("gm", "pos", "getConvertedTypeStationTable","","convertedTypeStationTable");
		HashMap<String ,HashMap<Integer, ArrayList<Integer>>> oResultList = new HashMap<String ,HashMap<Integer ,ArrayList<Integer>>>();
		HashMap<Integer , ArrayList<Integer>> oPrintStatus = new HashMap<Integer , ArrayList<Integer>>();
		if(oResponJSONArray != null) {
			for (int i = 0; i<oResponJSONArray.length(); i++) {
				if(oResponJSONArray.isNull(i))
					continue;
				 JSONObject oTempJSON = oResponJSONArray.optJSONObject(i);
				 String sType = oTempJSON.optString("type","");
				 int iId = oTempJSON.optInt("id",0);
				 JSONArray oStationJSONArray = oTempJSON.optJSONArray("stationIdList");
				 ArrayList <Integer> oStationList = new ArrayList<Integer>();
				 if (oStationJSONArray != null && oStationJSONArray.length()>0) {
					 for(int j = 0; j < oStationJSONArray.length() ;j++) {
						 oStationList.add(oStationJSONArray.optInt(j));
					 }
				 }
				 if (oResultList.containsKey(sType)) {
					 HashMap<Integer ,ArrayList<Integer>> oTempHashMap = oResultList.get(sType);
					 if (oTempHashMap.containsKey(iId)) { 
						 ArrayList<Integer> oSavedStationList = oTempHashMap.get(iId);
						 for (Integer oStationId :oStationList) {
							 if (!oSavedStationList.contains(oStationId)) {
								 oSavedStationList.add(oStationId);
							 }
						 }
					 }else 
						 oTempHashMap.put(iId, oStationList);
				 }else {
					 if(ClsAlertMessage.MESSAGE_KEY_PRINT_QUEUE_STATUS.equals(sType)) {
						 oPrintStatus.put(iId, oStationList);
						 oResultList.put(ClsAlertMessage.MESSAGE_KEY_PRINT_QUEUE_STATUS, oPrintStatus);
					 }
				 }
			}
		}
		return oResultList;
	}
	
	public static HashMap<Integer, ArrayList<Integer>> constructAlertMessageStationIdToPrintQueueIdTable(){
		HashMap<Integer ,ArrayList<Integer>> oAlertSettingTable = AppGlobal.g_oAlertMessageElementIdToStationIdsSettingTable.get(ClsAlertMessage.MESSAGE_KEY_PRINT_QUEUE_STATUS);
		HashMap<Integer, ArrayList<Integer>> oStationsPrintQueueList = new HashMap<>();
		if (oAlertSettingTable != null) {
			// loop all print queue to construct the mapping of station Id to print queue id
			for (Map.Entry<Integer, ArrayList<Integer>> entry : oAlertSettingTable.entrySet()) {
				// get print queue id
				Integer iPrintQueueId = entry.getKey();
				// get a set of station which belongs to current print queue
				ArrayList<Integer> oStationIds = entry.getValue();
				//store a set of print queue id which belongs to station
				ArrayList<Integer> oPrintQueueIds;
			
				for(Integer iStationId : oStationIds){
					if(oStationsPrintQueueList.containsKey(iStationId))
						oPrintQueueIds = oStationsPrintQueueList.get(iStationId);
					else
						oPrintQueueIds = new ArrayList<Integer>();
					//add the record into current station id
					oPrintQueueIds.add(iPrintQueueId);
					oStationsPrintQueueList.put(iStationId, oPrintQueueIds);
				}
			}
		}
		return oStationsPrintQueueList;
	}
	
	public JSONArray readAllActiveAlertSetting() {
		return PosStationAlertSetting.readDataListFromApi("gm", "pos", "getAllActiveAlertSetting","","posStationAlertSettings");
	}
	public int getId() {
		return id;
	}

	public int getShopId() {
		return shopId;
	}

	public int getOutletId() {
		return outletId;
	}

	public int getStationId() {
		return stationId;
	}

	public String getTypeKey() {
		return typeKey;
	}

	public JSONObject getSetting() {
		return setting;
	}

	public String getStatus() {
		return status;
	}

}
