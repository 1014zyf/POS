package om;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;

public class PosLicenseControlModel {
	
	public static JSONObject m_oLicenseCertJSONObject;
	public static String m_sLicenseWarningMessage = "";
	
	synchronized public void setLicenseCert(JSONObject oCertJsonObject){
		try {
			m_oLicenseCertJSONObject = new JSONObject(oCertJsonObject.toString());
		} catch (JSONException e) {
			OmWsClientGlobal.stackToString(e);
		}
	}
	
	synchronized public void setLicenseWarning(String sWarningMessage){
		m_sLicenseWarningMessage = sWarningMessage;
	}
	
	public long getLicenseCertDateToExpired(){
		if (m_sLicenseWarningMessage.isEmpty())  // No warning message
			return -1;
		
		long lDatetoExpired = -1;
		if(m_oLicenseCertJSONObject != null){
			if(m_oLicenseCertJSONObject.has("hero_cert")){
				JSONObject oHeroCertJSONObject = m_oLicenseCertJSONObject.optJSONObject("hero_cert");
				if (oHeroCertJSONObject != null && oHeroCertJSONObject.has("content")) {
					JSONObject oContentJSONObject = oHeroCertJSONObject.optJSONObject("content");
					if (oContentJSONObject != null
							&& oContentJSONObject.has("application_alert_start_date") && oContentJSONObject.has("application_alert_end_date")){
						long MILLISECS_PER_DAY = 24 * 60 * 60 * 1000;
						DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
						
						String sApplicationAlertStartDate = oContentJSONObject.optString("application_alert_start_date");
						String sApplicationAlertEndDate = oContentJSONObject.optString("application_alert_end_date");
						String sHeroCertValidEndDate = oContentJSONObject.optString("hero_cert_vaild_end_date");
						DateTime dtToday = null;
						try {
							dtToday = (DateTime) format.parseDateTime(format.print(AppGlobal.getCurrentTime(false)));
						} catch (Exception e1) {
							OmWsClientGlobal.stackToString(e1);
						}
						
						DateTime dtApplicationAlertStartDate = null;
						DateTime dtApplicationAlertEndDate = null;
						DateTime dtHeroCertValidEndDate = null;
						try {       
							dtApplicationAlertStartDate = (DateTime) format.parseDateTime(sApplicationAlertStartDate);
							dtApplicationAlertEndDate = (DateTime) format.parseDateTime(sApplicationAlertEndDate);
							dtHeroCertValidEndDate = (DateTime) format.parseDateTime(sHeroCertValidEndDate);
							if((((dtToday.getMillis() - dtApplicationAlertStartDate.getMillis())/MILLISECS_PER_DAY) >= 0) && (((dtToday.getMillis() - dtApplicationAlertEndDate.getMillis())/MILLISECS_PER_DAY) <= 0)){
								lDatetoExpired = (dtHeroCertValidEndDate.getMillis() - dtToday.getMillis())/MILLISECS_PER_DAY;
							}
						} catch (Exception e) {
							OmWsClientGlobal.stackToString(e);
						}
					}
				}
			}
		}
		
		return lDatetoExpired;
	}
	
	public long getLicenseCertDateToSubscriptionExpired(){
		if (m_sLicenseWarningMessage.isEmpty()) 			// No warning message
			return -1;
		
		long lDatetoExpired = -1;
		if (m_oLicenseCertJSONObject != null){
			if (m_oLicenseCertJSONObject.has("hero_cert")){
				JSONObject oHeroCertJSONObject = m_oLicenseCertJSONObject.optJSONObject("hero_cert");
				if (oHeroCertJSONObject != null && oHeroCertJSONObject.has("content")) {
					JSONObject oContentJSONObject = oHeroCertJSONObject.optJSONObject("content");
					long MILLISECS_PER_DAY = 24 * 60 * 60 * 1000;
					DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
					
					String sHeroCertValidEndDate = oContentJSONObject.optString("subscription_end_date");
					DateTime dtToday = null;
					try {
						dtToday = (DateTime) format.parseDateTime(format.print(AppGlobal.getCurrentTime(false)));
					} catch (Exception e1) {
						OmWsClientGlobal.stackToString(e1);
					}
					
					DateTime dtHeroCertValidEndDate = null;
					try {
						dtHeroCertValidEndDate = (DateTime) format.parseDateTime(sHeroCertValidEndDate);
						lDatetoExpired = (dtHeroCertValidEndDate.getMillis() - dtToday.getMillis())/MILLISECS_PER_DAY;
					} catch (Exception e) {
						OmWsClientGlobal.stackToString(e);
					}
				}
			}
		}
		
		return lDatetoExpired;
	}
	
	public String checkLicenseForPOSModule(int iStationId){
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("stationId", Integer.toString(iStationId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "posLicenceCheck", requestJSONObject.toString(), true))
			return OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null || !OmWsClientGlobal.g_oWsClient.get().getResponse().has("error_msg"))		// No error
				return "";
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse().optString("error_msg", "internal_error");
		}
	}
}