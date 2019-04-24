package om;

import java.util.HashMap;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;

public class PosTaiwanGuiConfig {
	private int twcfId;
	private int shopId;
	private int oletId;
	private int statId;
	private String prefix;
	private int startNum;
	private int endNum;
	private DateTime startDate;
	private DateTime endDate;
	private int warningLimit;
	private String type;
	private DateTime createDate;
	private String status;
	
	// Generate by
	public static String GENERATED_BY_OUTLET = "o";
	public static String GENERATED_BY_STATION = "s";
	
	//Mode
	public static String GENERATED_MODE_EGUI = "e";//eGUI
	public static String GENERATED_MODE_RPU420 = "r"; //RPU420

	// type
	public final static String TYPE_NORMAL = "a";
	public final static String TYPE_HAVE_GUI = "b";
	public final static String TYPE_NO_TAX = "d";
	public final static String TYPE_WAIVE_TAX = "c";
	public final static String TYPE_CHARITY = "f";
	public final static String TYPE_SPECIAL = "e";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	public static String STATUS_SUSPENDED = "s";
	
	// configure type
	public static String CONFIGURE_TYPE_NORMAL = "a";
	public static String CONFIGURE_TYPE_SPECIAL = "e";

	//init object with initialize value
	public PosTaiwanGuiConfig() {
		this.init();
	}

	//init obejct with JSONObject
	public PosTaiwanGuiConfig(JSONObject configJSONObject) {
		readDataFromJson(configJSONObject);
	}
	
	//init obejct with HashMap
	public PosTaiwanGuiConfig(HashMap configMapObject) {
		readDataFromHashMap(configMapObject);
	}
	
	//read data from database by date and outlet_id
	public JSONArray readAllByDateAndOutlet(String sDate, int iOletId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("date", sDate);
			requestJSONObject.put("outletId", Integer.toString(iOletId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataListFromApi("gm", "pos", "getTaiwanGuiConfigByDateAndOutlet", requestJSONObject.toString());
	}

	//read data from database by stat_id
	public JSONArray readAllByDateAndStation(int iStatId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("stationId", Integer.toString(iStatId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataListFromApi("gm", "pos", "getTaiwanGuiConfigByStation", requestJSONObject.toString());
	}
	
	public JSONArray readAllByStation(int iStatId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("stationId", Integer.toString(iStatId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return readTaiwanGUIConfigDataListFromApi("gm", "pos", "getAllTaiwanGuiConfigByStation", requestJSONObject.toString());
	}
	
	public String changeTaiwanGUIConfig(String sWsInterface, String sModule, String sFcnName, String sParam) {
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return "connection_failed";
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("id")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("id"))
				return OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			else
				return OmWsClientGlobal.g_oWsClient.get().getResponse().optString("id");
		}
		
	}
	
	public String activeTaiwanGuiConfigByOutletAndDate(int iOutletId, String sDate) {
		JSONObject oRequestJSONObject = new JSONObject();
		try {
			oRequestJSONObject.put("outletId", iOutletId);
			oRequestJSONObject.put("date", sDate);
		}catch(JSONException e) {}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "activeTaiwanGuiConfigByOutletAndDate", oRequestJSONObject.toString(), false))
			return OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return "connection_failed";
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("id")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("id"))
				return OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
			else
				return OmWsClientGlobal.g_oWsClient.get().getResponse().optString("id");
		}
		
	}
	
	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bResult = true;
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bResult = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;

			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("taiwanGuiConfig")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("taiwanGuiConfig")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("taiwanGuiConfig");
			if(tempJSONObject.isNull("PosTaiwanGuiConfig")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}
	
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bResult = true;
		JSONArray tempJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bResult = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return tempJSONArray;

			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("taiwanGuiConfig")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return tempJSONArray;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("taiwanGuiConfig")) {
				this.init();
				return tempJSONArray;
			}
			tempJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("taiwanGuiConfig");	
		}
		return tempJSONArray;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject taiwanGuiConfigJSONObject) {
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTimeFormatter dateFormatYMDHis = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		JSONObject resultTaiwanGuiConfig = null;
		
		resultTaiwanGuiConfig = taiwanGuiConfigJSONObject.optJSONObject("PosTaiwanGuiConfig");
		if(resultTaiwanGuiConfig == null)
			resultTaiwanGuiConfig = taiwanGuiConfigJSONObject;
			
		this.init();
		this.twcfId = resultTaiwanGuiConfig.optInt("twcf_id");
		this.statId = resultTaiwanGuiConfig.optInt("twcf_stat_id");
		this.shopId = resultTaiwanGuiConfig.optInt("twcf_shop_id");
		this.oletId = resultTaiwanGuiConfig.optInt("twcf_olet_id");
		this.prefix = resultTaiwanGuiConfig.optString("twcf_prefix");
		this.startNum = resultTaiwanGuiConfig.optInt("twcf_start_num");
		this.endNum = resultTaiwanGuiConfig.optInt("twcf_end_num");
		
		String sStartDate = resultTaiwanGuiConfig.optString("twcf_start_date");
		if (!sStartDate.isEmpty()) {
			try {
				this.startDate = dateFormat.parseDateTime(sStartDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String sEndDate = resultTaiwanGuiConfig.optString("twcf_end_date");
		if (!sEndDate.isEmpty()) {
			try {
				this.endDate = dateFormat.parseDateTime(sEndDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		String sCreateDate = resultTaiwanGuiConfig.optString("created");
		if (!sCreateDate.isEmpty()) {
			try {
				createDate = dateFormatYMDHis.withZoneUTC().parseDateTime(sCreateDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		this.warningLimit = resultTaiwanGuiConfig.optInt("twcf_warning_limit");
		this.type = resultTaiwanGuiConfig.optString("twcf_type", PosTaiwanGuiConfig.CONFIGURE_TYPE_NORMAL);
		this.status = resultTaiwanGuiConfig.optString("twcf_status", PosTaiwanGuiConfig.STATUS_ACTIVE);		
	}
	
	//read data from response HashMap
	private void readDataFromHashMap(HashMap<String, String> taiwanGuiConfigMapObject) {
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTimeFormatter dateFormatYMDHis = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		this.init();
		this.twcfId = Integer.parseInt(taiwanGuiConfigMapObject.get("twcfId"));
		this.statId = Integer.parseInt(taiwanGuiConfigMapObject.get("statId"));
		this.shopId = Integer.parseInt(taiwanGuiConfigMapObject.get("shopId"));
		this.oletId = Integer.parseInt(taiwanGuiConfigMapObject.get("oletId"));
		this.prefix = taiwanGuiConfigMapObject.get("prefix");
		this.startNum = Integer.parseInt(taiwanGuiConfigMapObject.get("startNumber"));
		this.endNum = Integer.parseInt(taiwanGuiConfigMapObject.get("endNumber"));
		String sStartDate = taiwanGuiConfigMapObject.get("startDate");
		if (!sStartDate.isEmpty()) {
			try {
				this.startDate = dateFormat.parseDateTime(sStartDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String sEndDate = taiwanGuiConfigMapObject.get("endDate");
		if (!sEndDate.isEmpty()) {
			try {
				this.endDate = dateFormat.parseDateTime(sEndDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String sCreateDate = taiwanGuiConfigMapObject.get("created");
		if (!sCreateDate.isEmpty()) {
			try {
				this.createDate = dateFormatYMDHis.withZoneUTC().parseDateTime(sCreateDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.warningLimit = Integer.parseInt(taiwanGuiConfigMapObject.get("warning"));
		this.type = taiwanGuiConfigMapObject.get("type");
		this.status = taiwanGuiConfigMapObject.get("status");
	}
	
	public JSONArray readTaiwanGUIConfigDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray displayPanelLookupJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("PosTaiwanGuiConfigs")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("PosTaiwanGuiConfigs"))
				return null;
		
			displayPanelLookupJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("PosTaiwanGuiConfigs");				
		}
		
		return displayPanelLookupJSONArray;
	}
	
	// init value
	private void init() {
		this.twcfId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.statId = 0;
		this.prefix = null;
		this.startNum = 0;
		this.endNum = 0;
		this.startDate = null;
		this.endDate = null;
		this.warningLimit = 0;
		this.type = PosTaiwanGuiConfig.CONFIGURE_TYPE_NORMAL;
		this.status = PosTaiwanGuiConfig.STATUS_ACTIVE;
	}
	
	public String getPrefix() {
		return this.prefix;
	}
	
	public int getStartNum() {
		return this.startNum;
	}
	
	public int getEndNum() {
		return this.endNum;
	}
	
	public int getWarningLimit() {
		return this.warningLimit;
	}
	
	public int getTaiwanGuiConfigId() {
		return this.twcfId;
	}

	public String getType(){
		return this.type;
	}
	
	public DateTime getStartDate() {
		return startDate;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}

	public DateTime getCreateDate() {
		return createDate;
	}
}
