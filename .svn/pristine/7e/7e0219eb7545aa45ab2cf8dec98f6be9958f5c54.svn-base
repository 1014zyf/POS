package app.model;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

public class PosTaiwanGuiConfig {
	private int twcfId;
	private int shopId;
	private int oletId;
	private int statId;
	private String prefix;
	private int startNum;
	private int endNum;
	private Date startDate;
	private Date endDate;
	private int warningLimit;
	private String status;
	
	// Generate by
	public static String GENERATED_BY_OUTLET = "o";
	public static String GENERATED_BY_STATION = "s";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";

	//init object with initialize value
	public PosTaiwanGuiConfig() {
		this.init();
	}
	
	//read data from database by date and outlet_id
	public boolean readByDateAndOutlet(String sDate, int iOletId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("date", sDate);
			requestJSONObject.put("outletId", Integer.toString(iOletId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "getTaiwanGuiConfigByDateAndOutlet", requestJSONObject.toString());
	}

	//read data from database by stat_id
	public boolean readByDateAndStation(int iStatId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("stationId", Integer.toString(iStatId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "getTaiwanGuiConfigByStation", requestJSONObject.toString());
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

	//read data from response JSON
	private void readDataFromJson(JSONObject taiwanGuiConfigJSONObject) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
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
				this.startDate = new Date(dateFormat.parse(sStartDate).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		String sEndDate = resultTaiwanGuiConfig.optString("twcf_end_date");
		if (!sEndDate.isEmpty()) {
			try {
				this.endDate = new Date(dateFormat.parse(sEndDate).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		this.warningLimit = resultTaiwanGuiConfig.optInt("twcf_warning_limit");
		this.status = resultTaiwanGuiConfig.optString("twcf_status", PosTaiwanGuiConfig.STATUS_ACTIVE);		
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
		this.status = PosTaiwanGuiConfig.STATUS_ACTIVE;
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
}
