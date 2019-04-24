package om;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;


public class SystemDataProcessLogList {
	private ArrayList<SystemDataProcessLog> m_oSystemDataProcessLogsList;
	public static String DATA_PROCEESS_LOG_KEY_UPDATE = "update";
	
	public SystemDataProcessLogList() {
		// TODO Auto-generated constructor stub
		m_oSystemDataProcessLogsList = new ArrayList<SystemDataProcessLog>();
	}
	
	public void readDataProcessLogsByLimitAndKey(int iLimit , String sLogKey) {
		SystemDataProcessLog oSystemDataProcessLog = new SystemDataProcessLog();
		JSONArray oDataProcessLogsJSONArray = new JSONArray();
		
		oDataProcessLogsJSONArray = oSystemDataProcessLog.readDataProcessLogsByLimitAndKey(iLimit , sLogKey);
		if(oDataProcessLogsJSONArray != null) {
			for(int i = 0; i < oDataProcessLogsJSONArray.length(); i++) {
				JSONObject oTempJSONObject = oDataProcessLogsJSONArray.optJSONObject(i);
				if (oTempJSONObject == null)
					continue;
				SystemDataProcessLog oTempSystemDataProcessLog = new SystemDataProcessLog(oTempJSONObject);
				m_oSystemDataProcessLogsList.add(oTempSystemDataProcessLog);
			}
		}
	}
	
	public ArrayList<SystemDataProcessLog> getSystemDataProcessLogList(){
		return this.m_oSystemDataProcessLogsList;
	}
	
}
