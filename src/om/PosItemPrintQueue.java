//Database: pos_item_print_queues - POS print queues mapping
package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosItemPrintQueue {
	private int itpqId;
	private int shopId;
	private int oletId;
	private int seq;
	private int menuItpqId;
	private int stationPrinter;
	private int prtqId;
	private int pfmtId;
	private String status;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosItemPrintQueue() {
		this.init();	
	}
	
	//init object with JSONObject
	public PosItemPrintQueue(JSONObject oItemPrtQueueJSONObject) {
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
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("item_print_queues")) {
				return null;
			}
			
			itemPrintQueueJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("item_print_queues");
		}
		
		return itemPrintQueueJSONArray;
	}
	
	//get all by shop and outlet Id
	public JSONArray readAllByShopAndOutletId(int shopId, int outletId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("shopId", shopId);
			requestJSONObject.put("outletId", outletId);
			requestJSONObject.put("recursive", -1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getItemPrintQueuesByShopAndOutletId", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	//print testing printer slip
	public String printTestingPrinterSlip(int iUserId, int iStationId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("posItpqId", this.itpqId);
			requestJSONObject.put("userId", iUserId);
			requestJSONObject.put("stationId", iStationId);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "printTestingPrinterSlip", requestJSONObject.toString(), true)) 
			return "";
		else
			return OmWsClientGlobal.g_oWsClient.get().getResponse().optString("url");
	}
	
	public void readDataFromJson(JSONObject oItemPrtQueueJSONObject) {
		JSONObject oTempJSONObject = null;
		
		oTempJSONObject = oItemPrtQueueJSONObject.optJSONObject("PosItemPrintQueue");
		if(oTempJSONObject == null)
			oTempJSONObject = oItemPrtQueueJSONObject;
			
		this.init();
		this.itpqId = oTempJSONObject.optInt("itpq_id");
		this.shopId = oTempJSONObject.optInt("itpq_shop_id");
		this.oletId = oTempJSONObject.optInt("itpq_olet_id");
		this.seq = oTempJSONObject.optInt("itpq_seq");
		this.menuItpqId = oTempJSONObject.optInt("itpq_itpq_id");
		this.stationPrinter = oTempJSONObject.optInt("itpq_station_printer");
		this.prtqId = oTempJSONObject.optInt("itpq_prtq_id");
		this.pfmtId = oTempJSONObject.optInt("itpq_pfmt_id");
		this.status = oTempJSONObject.optString("itpq_status", PosItemPrintQueue.STATUS_ACTIVE);
	}
	
	// init value
	public void init() {
		this.itpqId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.seq = 0;
		this.menuItpqId = 0;
		this.stationPrinter = 0;
		this.prtqId = 0;
		this.pfmtId = 0;
		this.status = PosItemPrintQueue.STATUS_ACTIVE;
	}
	
	//set itpqId
	public void setItpqId(int iItpqId) {
		this.itpqId = iItpqId;
	}
	
	//get itpqId
	public int getItpqId() {
		return this.itpqId;
	}
	
	//get shop id
	protected int getShopId() {
		return this.shopId;
	}
	
	//get olet id
	protected int getOletId() {
		return this.oletId;
	}
	
	//get seq
	public int getSeq() {
		return this.seq;
	}
	
	//get menu itpq id
	public int getMenuItpqId() {
		return this.menuItpqId;
	}
	
	//get station printer
	public int getStationPrinter() {
		return this.stationPrinter;
	}
	
	//get prtq id
	public int getPrtqId() {
		return this.prtqId;
	}
	
	//get print format id
	protected int getPfmtId() {
		return this.pfmtId;
	}
	
	//get status
	protected String getStatus() {
		return this.status;
	}
}
