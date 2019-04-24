package om;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosReport {
	private int rptsId;
	private String[] name;
	private int guid;
	private String filename;
	private String printFormat;
	private String version;
	private String status;
	
	// printFormat
	public static String PRINT_FORMAT_DESKTOP = "";
	public static String PRINT_FORMAT_SLIP = "s";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	public PosReport() {
		this.rptsId = 0;
		this.name = new String[5];
		for(int i=0; i<5; i++)
			this.name[i] = "";
		this.guid = 0;
		this.filename = "";
		this.printFormat = PosReport.PRINT_FORMAT_DESKTOP;
		this.version = "";
		this.status = PosReport.STATUS_ACTIVE;
	}
	
	public HashMap<String, String> generateDirectReport(int iOutletId, String sBusinessDayId, int iUserId, int iLangIndex, String sReportType) {
		HashMap<String, String> oResultDetails = new HashMap<String, String>();
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("businessDayId", sBusinessDayId);
			requestJSONObject.put("userId", iUserId);
			requestJSONObject.put("langIndex", iLangIndex);
			requestJSONObject.put("reportType", sReportType);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "generateDirectReport", requestJSONObject.toString(), true))
			return null;
		else {
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("url") || OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("url") 
					|| !OmWsClientGlobal.g_oWsClient.get().getResponse().has("printUrl") || OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("printUrl")
					|| !OmWsClientGlobal.g_oWsClient.get().getResponse().has("mediaType") || OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("mediaType")) 
				return null;
			
			oResultDetails.put("url", OmWsClientGlobal.g_oWsClient.get().getResponse().optString("url"));
			oResultDetails.put("printUrl", OmWsClientGlobal.g_oWsClient.get().getResponse().optString("printUrl"));
			oResultDetails.put("mediaType", OmWsClientGlobal.g_oWsClient.get().getResponse().optString("mediaType"));
		}
		
		return oResultDetails;
	}

	public static String getWebReportBaseURL() {
		String sURL = "";
		JSONObject obj = new JSONObject();
		
		try {
			obj.put("section", "system");
			obj.put("scfg_variable", "web_service_path");
			
			OmWsClient omWsClient = OmWsClientGlobal.g_oWsClient.get();
			if (!omWsClient.call("gm", "report", "getConfig", obj.toString(), true))
				return "";
			else {
				JSONObject resObj = omWsClient.getResponse();
				if(!resObj.has("rptConfig") || resObj.isNull("rptConfig")) 
					return "";
				
				JSONArray oRptConfigJSONArray = resObj.optJSONArray("rptConfig");
				if (oRptConfigJSONArray.length() > 0) {
					if (oRptConfigJSONArray.optJSONObject(0) != null && oRptConfigJSONArray.optJSONObject(0).has("RptConfig")) {
						JSONObject rptConfig = oRptConfigJSONArray.optJSONObject(0).optJSONObject("RptConfig");
						if (!rptConfig.isNull("scfg_value"))
							sURL = rptConfig.getString("scfg_value");
					}
				}
			}
		}catch(JSONException jsone) {
			jsone.printStackTrace();
			return "";			
		}		
		return sURL;
	}	

	protected int getRptsId() {
		return this.rptsId;
	}
	
	protected String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	protected int getGuid() {
		return this.guid;
	}
	
	protected String getFilename() {
		return this.filename;
	}
	
	protected String getPrintFormat() {
		return this.printFormat;
	}
	
	protected String getVersion() {
		return this.version;
	}
	
	protected String getStatus() {
		return this.status;
	}
}
