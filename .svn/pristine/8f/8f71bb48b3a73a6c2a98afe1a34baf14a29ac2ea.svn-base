package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PrtPrintQueue {
	private int prtq_id;
	private String[] name;
	private String[] shortName;
	private int seq;
	private int [] device_id;
	private int device_switch_idx;
	private int [] redirect_prtq_id;
	private int additional_copies;
	private String additional_action;
	private String additional_settings;
	private String status;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_SUSPEND = "s";
	public static String STATUS_DELETED = "d";
	
	public PrtPrintQueue() {
		this.init();
	}
	
	public PrtPrintQueue(JSONObject oPrintQueueJSON) {
		this.readDataFromJson(oPrintQueueJSON);
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
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("printQueue")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("printQueue")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("printQueue");
			if(tempJSONObject == null || tempJSONObject.isNull("PrtPrintQueue")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	public JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("printQueues")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("printQueues")) {
				this.init();
				return null;
			}
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("printQueues");
		}
		
	
	}
	//read data from response JSON
	private void readDataFromJson(JSONObject printQueueJSONObject) {
		JSONObject resultPrintQueue = null;
		int i;
		resultPrintQueue = printQueueJSONObject.optJSONObject("PrtPrintQueue");

		if(resultPrintQueue == null)
			resultPrintQueue = printQueueJSONObject;
			
		this.init();

		this.prtq_id = resultPrintQueue.optInt("prtq_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultPrintQueue.optString("prtq_name_l"+i);
		
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultPrintQueue.optString("prtq_short_name_l"+i);	
		
		for(i=1; i<=10; i++)
			this.device_id[(i-1)] = resultPrintQueue.optInt("prtq_device"+i+"_pdev_id");	
		
		for(i=1; i<=20; i++)
			this.redirect_prtq_id[(i-1)] = resultPrintQueue.optInt("prtq_redirect"+i+"_prtg_id");
		
		this.seq = resultPrintQueue.optInt("prtq_seq");
		this.device_switch_idx = resultPrintQueue.optInt("prtq_device_switch_idx");
		this.additional_copies = resultPrintQueue.optInt("prtq_additional_copies");
		this.additional_action = resultPrintQueue.optString("prtq_additional_action", "");
		this.additional_settings = resultPrintQueue.optString("prtq_additional_settings", "");
		this.status = resultPrintQueue.optString("prtq_status", PrtPrintQueue.STATUS_ACTIVE);
	}
	
	// init value
	public void init() {
		int i=0;
		this.prtq_id = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		
		if(this.device_id == null)
			this.device_id = new int[10];
		for(i=0; i<10; i++)
			this.device_id[i] = 0;
		this.device_switch_idx = 0;
		if(this.redirect_prtq_id == null)
			this.redirect_prtq_id = new int[20];
		for(i=0; i<20; i++)
			this.redirect_prtq_id[i] = 0;
		this.additional_copies = 0;
		this.additional_action = "";
		this.additional_settings = "";
		this.status = PrtPrintQueue.STATUS_ACTIVE;
	}
	
	//read data from database by prtq_id 
	public boolean readById(int iPrintQueueId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("printQueueId", Integer.toString(iPrintQueueId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "printing", "getPrintQueueById", requestJSONObject.toString());
	}
	
	//read all add Print Queue 
	public JSONArray readAllActive() {
		return this.readDataListFromApi("gm", "printing", "getAllActivePrintQueue", "");
	}
	//get prtq id
	public int getPrtPrintQueueId() {
		return this.prtq_id;
	}

	//get name by index
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get name
	public String[] getName() {
		return this.name;
	}
	
	//get short name
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get sequence number
	public int getSequence() {
		return this.seq;
	}
	
	//get status
	public String getStatus() {
		return this.status;
	}	
}
