package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuItemPrintQueue {
	private int itpqId;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String status;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	public MenuItemPrintQueue() {
		this.init();
	}
	
	//init object with JSONObject
	public MenuItemPrintQueue(JSONObject oItemPrtQueueJSONObject) {
		this.readDataFromJson(oItemPrtQueueJSONObject);
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray itemPrintQueueJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("item_print_queues")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("item_print_queues"))
				return null;
			
			itemPrintQueueJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("item_print_queues");
		}
		
		return itemPrintQueueJSONArray;
	}
	
	//get all active
	public JSONArray readAll() {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("recursive", -1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "menu", "getAllMenuItemPrintQueues", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	public void readDataFromJson(JSONObject oItemPrtQueueJSONObject) {
		JSONObject oTempJSONObject = null;
		int i = 0;
		
		oTempJSONObject = oItemPrtQueueJSONObject.optJSONObject("MenuItemPrintQueue");
		if(oTempJSONObject == null)
			oTempJSONObject = oItemPrtQueueJSONObject;
		
		this.init();
		this.itpqId = oTempJSONObject.optInt("itpq_id");
		for(i=1; i<=5; i++) {
			this.name[i-1] = oTempJSONObject.optString("itpq_name_l"+i);
			this.shortName[i-1] = oTempJSONObject.optString("itpq_short_name_l"+i);
		}
		this.seq = oTempJSONObject.optInt("itpq_seq");
		this.status = oTempJSONObject.optString("itpq_status", MenuItemPrintQueue.STATUS_ACTIVE);
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.itpqId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.status = MenuItemPrintQueue.STATUS_ACTIVE;
	}
	
	protected void setItpqId(int iItpqId) {
		this.itpqId = iItpqId;
	}
	
	protected void setName(int iIndex, String sName) {
		this.name[iIndex-1] = sName;
	}
	
	protected void setShortName(int iIndex, String sShortName) {
		this.shortName[iIndex-1] = sShortName;
	}
	
	protected void setSeq(int iSeq) {
		this.seq = iSeq;
	}
	
	protected void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	protected int getItqpId() {
		return this.itpqId;
	}
	
	public String getName(int iIndex) {
		return this.name[iIndex-1];
	}
	
	protected String getShortName(int iIndex) {
		return this.shortName[iIndex-1];
	}
	
	protected int getSeq() {
		return this.seq;
	}
	
	protected String getStatus() {
		return this.status;
	}
}
