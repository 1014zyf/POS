package om;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosStockDeliveryInvoiceItem {
	private int sitmId;
	private int sinvId;
	private int itemId;
	private BigDecimal expectDeliveryQty;
	private BigDecimal actualReceivedQty;
	public String status;
	
	private boolean bModified;

	// status
	public static String STATUS_OUTSTANDING = "";
	public static String STATUS_DELETED = "d";
	public static String STATUS_RECEIVED = "r";
	
	//init with initialized value
	public PosStockDeliveryInvoiceItem() {
		this.init();
	}
	
	//init object with JSON Object
	public PosStockDeliveryInvoiceItem(JSONObject itemJSONObject) {
		this.bModified = false;
		
		this.readDataFromJson(itemJSONObject);
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("stockDeliveryInvoiceItem")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("stockDeliveryInvoiceItem")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("stockDeliveryInvoiceItem");
			if(tempJSONObject.isNull("PosStockDeliveryInvoiceItem")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject stockDeliveryInvoiceItemJSONObject) {
		JSONObject resultInvoice = null;
		
		resultInvoice = stockDeliveryInvoiceItemJSONObject.optJSONObject("PosStockDeliveryInvoiceItem");
		if(resultInvoice == null)
			resultInvoice = stockDeliveryInvoiceItemJSONObject;
			
		this.init();

		this.sitmId = resultInvoice.optInt("sitm_id");
		this.sinvId = resultInvoice.optInt("sitm_sinv_id");
		this.itemId = resultInvoice.optInt("sitm_item_id");
		this.expectDeliveryQty = new BigDecimal(resultInvoice.optString("sitm_expect_delivery_qty", "0.0"));
		this.actualReceivedQty = new BigDecimal(resultInvoice.optString("sitm_actual_received_qty", "0.0"));
		this.status = resultInvoice.optString("sitm_status", PosStockDeliveryInvoiceItem.STATUS_OUTSTANDING);
	}
	
	//reset object value
	public void init() {
		this.sitmId = 0;
		this.sinvId = 0;
		this.itemId = 0;
		this.expectDeliveryQty = BigDecimal.ZERO;
		this.actualReceivedQty = BigDecimal.ZERO;
		this.status = PosStockDeliveryInvoiceItem.STATUS_OUTSTANDING;
		
		this.bModified = false;
	}
	
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("sitm_id", this.sitmId);
			addSaveJSONObject.put("sitm_sinv_id", this.sinvId);
			addSaveJSONObject.put("sitm_item_id", this.itemId);
			addSaveJSONObject.put("sitm_expect_delivery_qty", this.expectDeliveryQty);
			addSaveJSONObject.put("sitm_actual_received_qty", this.actualReceivedQty);
			addSaveJSONObject.put("sitm_status", this.status);

		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	protected JSONArray constructMultipleItemAddSaveJSON(List<PosStockDeliveryInvoiceItem> alStockDeliveryInvoiceItems) {
		int i=0;
		PosStockDeliveryInvoiceItem oItem = null;
		JSONObject tempItemJSONObject = null;
		JSONArray itemJSONArray = new JSONArray();
		
		for(i=0; i<alStockDeliveryInvoiceItems.size(); i++) {
			oItem = alStockDeliveryInvoiceItems.get(i);

			// if old item is not modified, skip it
			if (oItem.getSitmId() > 0 && !oItem.getModified())
				continue;
			
			if (oItem.getSitmId() == 0)
				tempItemJSONObject = oItem.constructAddSaveJSON(false);
			else{
				// Old item
				if(oItem.getModified() == false)
					continue;
				
				tempItemJSONObject = oItem.constructAddSaveJSON(true);
			}
			
			itemJSONArray.put(tempItemJSONObject);
		}

		return itemJSONArray;
	}
	
	//save (add or update) record to database
	public boolean addUpdate(boolean bUpdate, boolean bWithItems, List<PosStockDeliveryInvoiceItem> oNewItemList) {
		JSONObject requestJSONObject = new JSONObject();
		JSONObject responseJSONObject = null;
		
		requestJSONObject = constructAddSaveJSON(bUpdate);
		
		//send data to server
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveStockDeliveryInvoiceItem", requestJSONObject.toString(), false))
			return false;
		else {
			responseJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse();
			if (responseJSONObject == null)
				return false;
			
			if(bUpdate == false && !responseJSONObject.isNull("id")) 
				this.sitmId = responseJSONObject.optInt("id");
			return true;
		}
	}
	
	//get sitm id
	public int getSitmId() {
		return this.sitmId;
	}
	
	//get sinv id
	public int getSinvId() {
		return this.sinvId;
	}
	
	//get item id
	public int getItemId() {
		return this.itemId;
	}
	
	//get expect delivery qty
	public BigDecimal getExpectDeliveryQty() {
		return this.expectDeliveryQty;
	}
	
	//get actual delivery qty
	public BigDecimal getActualReceivedQty() {
		return this.actualReceivedQty;
	}
	
	//get status
	public String getStatus() {
		return this.status;
	}
	
	public boolean getModified() {
		return this.bModified;
	}
	
	public void setSinvId(int iSinvId) {
		this.sinvId = iSinvId;
	}
	
	public void setItemId(int iItemId) {
		this.itemId = iItemId;
	}
	
	public void setExpectDeliveryQty(BigDecimal dExpectDeliveryQty) {
		this.expectDeliveryQty = dExpectDeliveryQty;
	}
	
	public void setActualReceivedQty(BigDecimal dActualReceivedQty) {
		this.actualReceivedQty = dActualReceivedQty;
	}
	
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	public void setModified(boolean bModified) {
		this.bModified = bModified;
	}
	
}
