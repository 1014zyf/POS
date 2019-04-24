package om;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import app.AppGlobal;

public class SystemDataProcessLog {

	private int logId;
	private String logKey;
	private String logStartTime;
	private DateTime logStartLoctime;
	private String logEndTime;
	private DateTime logEndLoctime;
	private String logFilename ;
	private String logResult;
	private JSONObject logParams;
	
	public SystemDataProcessLog() {
		this.init();
	}
	
	public SystemDataProcessLog(JSONObject sysDataProcessLogJSONObject) {
		this.readDataFromJson(sysDataProcessLogJSONObject);
	}
	
	//init value
	public void init() {
		this.logId = 0;
		this.logKey = null;
		this.logStartTime = null;
		this.logStartLoctime = null;
		this.logEndTime = null;
		this.logEndLoctime = null;
		this.logFilename = null;
		this.logResult = null;
		this.logParams = null;
	}
	
	public void readDataFromJson(JSONObject sysDataProcessLogJSONObject) {
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		this.init();
		if (sysDataProcessLogJSONObject == null)
			return;
		
		JSONObject oTempDataProcessLogsRecordJSON = sysDataProcessLogJSONObject.optJSONObject("SysDataProcessLog");
		if (oTempDataProcessLogsRecordJSON == null)
			return;
		if (oTempDataProcessLogsRecordJSON.has("dlog_id"))
			this.logId = oTempDataProcessLogsRecordJSON.optInt("dlog_id",0);
		if (oTempDataProcessLogsRecordJSON.has("dlog_key"))
			this.logKey = oTempDataProcessLogsRecordJSON.optString("dlog_key","");
		if (oTempDataProcessLogsRecordJSON.has("dlog_start_time"))
			this.logStartTime = oTempDataProcessLogsRecordJSON.optString("dlog_start_time","");
		if (oTempDataProcessLogsRecordJSON.has("dlog_start_loctime")) {
			String sStartLocalTime = oTempDataProcessLogsRecordJSON.optString("dlog_start_loctime","");
			if (!sStartLocalTime.isEmpty()) 
				this.logStartLoctime = oFmt.parseDateTime(sStartLocalTime);
		}
		if (oTempDataProcessLogsRecordJSON.has("dlog_end_time"))
			this.logEndTime = oTempDataProcessLogsRecordJSON.optString("dlog_end_time","");
		if (oTempDataProcessLogsRecordJSON.has("dlog_end_loctime")) {
			String sEndLocalTime = oTempDataProcessLogsRecordJSON.optString("dlog_end_loctime","");
			if (!sEndLocalTime.isEmpty()) 
				this.logEndLoctime = oFmt.parseDateTime(sEndLocalTime);
		}
			
		if (oTempDataProcessLogsRecordJSON.has("dlog_filename"))
			this.logFilename = oTempDataProcessLogsRecordJSON.optString("dlog_filename","");
		if (oTempDataProcessLogsRecordJSON.has("dlog_result")) {
			String sResult = oTempDataProcessLogsRecordJSON.optString("dlog_result","");
			if(sResult.isEmpty()) 
				this.logResult = AppGlobal.g_oLang.get()._("unknown");
			else if(sResult.equals("f")) 
				this.logResult = AppGlobal.g_oLang.get()._("failed");
			else if(sResult.equals("s"))
				this.logResult =AppGlobal.g_oLang.get()._("successful");
		}
		
		if (oTempDataProcessLogsRecordJSON.has("dlog_params"))
			this.logParams = oTempDataProcessLogsRecordJSON.optJSONObject("dlog_params");
		
	}
	
	//read data from POS API
	protected JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray oSysDataProcessLogsJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			//do error checking
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("dataProcessLogs")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("dataProcessLogs"))
				return null;
			
			oSysDataProcessLogsJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("dataProcessLogs");
		}
		return oSysDataProcessLogsJSONArray;
	}
		
	public JSONArray readDataProcessLogsByLimitAndKey(int iLimit , String sLogKey) {
		JSONObject requestJSONObject = new JSONObject();
		try {
			requestJSONObject.put("limit", iLimit);
			requestJSONObject.put("logkey", sLogKey);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataListFromApi("gm", "system", "getDataProcessLogsByLimitAndKey", requestJSONObject.toString());
	}

	public int getLogId() {
		return logId;
	}
	
	public String getLogKey() {
		return logKey;
	}
	
	public String getLogStartTime() {
		return logStartTime;
	}
	
	public DateTime getLogStartLoctime() {
		return logStartLoctime;
	}
	
	public String getLogEndTime() {
		return logEndTime;
	}
	
	public DateTime getLogEndLoctime() {
		return logEndLoctime;
	}
	
	public String getLogFilename() {
		return logFilename;
	}
	
	public String getLogResult() {
		return logResult;
	}
	
	public JSONObject getLogParams() {
		return logParams;
	}
	
}
