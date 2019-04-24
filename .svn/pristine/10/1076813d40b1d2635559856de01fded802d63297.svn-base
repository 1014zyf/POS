package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PosAlertStatus {
	// alert element status by API returned
	// element type
	private String sType;
	// id
	private String sId;
	// sub element id
	private String sSubId;
	// element
	private String[] sName;
	// sub element name
	private String[] sSubName;
	// status
	private String sStatus;
	
	// alert element type
	public final static String ALERT_ELEMENT_PRINT_QUEUE = "print_queue";
	
	// status for print device
	public final static String PRINT_DEVICE_STATUS_NORMAL = "n";
	public final static String PRINT_DEVICE_STATUS_PAPER_NEAR_END = "p";
	public final static String PRINT_DEVICE_STATUS_PAPER_END = "e";
	public final static String PRINT_DEVICE_STATUS_OFFLINE = "o";
	
	// define language count
	public static int LANGUAGE_COUNT = 5;
	
	public PosAlertStatus() {
		this.init();
	}
	
	public PosAlertStatus(JSONObject oPosAlertStatus) {
		this.init();
		this.readDataFromJson(oPosAlertStatus);
	}
	
	public void init() {
		sType = "";
		sSubId = "";
		for (int i = 0; i < 5; i++) {
			sName = new String[this.LANGUAGE_COUNT];
			sSubName = new String[this.LANGUAGE_COUNT];
		}
		sStatus = "";
		sId = "";
	}
	
	private void readDataFromJson(JSONObject oPosAlertStatus) {
		JSONArray oAlertStatusJsonArray = null;
		if (oPosAlertStatus.has("printerDevices")) {
			try {
				oAlertStatusJsonArray = oPosAlertStatus.optJSONArray("printerDevices");
				this.sType = ALERT_ELEMENT_PRINT_QUEUE;
				// get print queue id
				this.sId = oPosAlertStatus.optJSONObject("printQueue").optString("prtq_id");

				if (oAlertStatusJsonArray.length() > 0) {
					int iIndex = 0;
					ArrayList<String> oPrinterDevices = new ArrayList<>();
					for (int i = 0; i < oAlertStatusJsonArray.length(); i++)
						oPrinterDevices.add(oAlertStatusJsonArray.getJSONObject(i).optString("pdst_status"));
					iIndex = this.printerDevicesStatusChecking(oPrinterDevices);
					this.sSubId = oAlertStatusJsonArray.getJSONObject(iIndex).optString("pdev_id");
					for (int i = 1; i <= 5; i++)
						this.sSubName[i - 1] = oAlertStatusJsonArray.getJSONObject(iIndex).optString("pdev_name_l" + i);
					this.sStatus = oAlertStatusJsonArray.getJSONObject(iIndex).optString("pdst_status");
				}
			} catch (JSONException jsone) {
				jsone.printStackTrace();
			}
		}
	}
	
	// return the appropriate index of the printer status
	private int printerDevicesStatusChecking(ArrayList<String> oPrinterDevices) {
		int iIndex = 0;
		if (oPrinterDevices.contains(PRINT_DEVICE_STATUS_NORMAL))
			iIndex = oPrinterDevices.indexOf(PRINT_DEVICE_STATUS_NORMAL);
		else if (oPrinterDevices.contains(PRINT_DEVICE_STATUS_PAPER_NEAR_END))
			iIndex = oPrinterDevices.indexOf(PRINT_DEVICE_STATUS_PAPER_NEAR_END);
		else if (oPrinterDevices.contains(PRINT_DEVICE_STATUS_PAPER_END))
			iIndex = oPrinterDevices.indexOf(PRINT_DEVICE_STATUS_PAPER_END);
		else if (oPrinterDevices.contains(PRINT_DEVICE_STATUS_OFFLINE))
			iIndex = oPrinterDevices.indexOf(PRINT_DEVICE_STATUS_OFFLINE);
		return iIndex;
	}
	
	// call API to get the status of all print devices of current print queue
	public JSONArray readPrintQueueIds(ArrayList<Integer> oPrintQueueIds) {
		JSONObject requestJSONObject = new JSONObject();
		try {
			requestJSONObject.put("printQueueIds", oPrintQueueIds);
		} catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataFromApi("gm", "printing", "getPrintQueuesByIds", requestJSONObject.toString());
	}
	
	private JSONArray readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray oAlertStatusList = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			if (!(OmWsClientGlobal.g_oWsClient.get().getResponse().has("printQueues"))) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("printQueues")) {
				return null;
			}
			
			oAlertStatusList = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("printQueues");
			return oAlertStatusList;
		}
	}
	
	public String getType() {
		return this.sType;
	}
	
	public String getId() {
		return this.sId;
	}
	
	public String getSubId() {
		return this.sSubId;
	}
	
	public String getSubName(int iIndex) {
		return this.sSubName[iIndex - 1];
	}
	
	public String getStatus() {
		return this.sStatus;
	}
}
