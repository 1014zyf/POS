package app.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosStockDeliveryInvoice {
	private int sinvId;
	private int shopId;
	private int oletId;
	private String userRef;
	private String createTime;
	private DateTime createLocTime;
	private int createUserId;
	private int createStatId;
	private String receivingTime;
	private DateTime receivingLocTime;
	private int receivingUserId;
	private int receivingStatId;
	private String modifiedTime;
	private DateTime modifiedLocTime;
	private int modifiedUserId;
	private int modifiedStatId;
	private String voidTime;
	private DateTime voidLocTime;
	private int voidUserId;
	private int voidStatId;
	
	private String status;
	
	private ArrayList<PosStockDeliveryInvoiceItem> invoiceItems;
	
	// status
	public static String STATUS_OUTSTANDING = "";
	public static String STATUS_DELETED = "d";
	public static String STATUS_RECEIVED = "r";
	
	//init with initialized value
	public PosStockDeliveryInvoice() {
		this.init();
	}
	
	//init obejct with JSONObject
	public PosStockDeliveryInvoice(JSONObject invoiceJSONObject) {
		readDataFromJson(invoiceJSONObject);
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("stockDeliveryInvoice")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("stockDeliveryInvoice")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("stockDeliveryInvoice");
			if(tempJSONObject.isNull("PosStockDeliveryInvoice")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray functionJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("stockDeliveryInvoices")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("stockDeliveryInvoices"))
				return null;
			
			functionJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("stockDeliveryInvoices");
		}
		
		return functionJSONArray;
	}
	
	// init value
	public void init() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		this.sinvId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.userRef = "";
		this.createLocTime = new DateTime();
		this.createTime = fmt.print(createLocTime.withZone(DateTimeZone.UTC));
		this.createUserId = 0;
		this.createStatId = 0;
		this.receivingLocTime = null;
		this.receivingTime = null;
		this.receivingUserId = 0;
		this.receivingStatId = 0;
		this.modifiedLocTime = null;
		this.modifiedTime = null;
		this.modifiedUserId = 0;
		this.modifiedStatId = 0;
		this.voidLocTime = null;
		this.voidTime = null;
		this.voidUserId = 0;
		this.voidStatId = 0;
		this.status = PosStockDeliveryInvoice.STATUS_OUTSTANDING;
		
		if(this.invoiceItems == null)
			this.invoiceItems = new ArrayList<PosStockDeliveryInvoiceItem>();
		else
			this.invoiceItems.clear();
	}
	
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("sinv_id", this.sinvId);
			addSaveJSONObject.put("sinv_shop_id", this.shopId);
			addSaveJSONObject.put("sinv_olet_id", this.oletId);
			if(!this.userRef.isEmpty())
				addSaveJSONObject.put("sinv_user_ref", this.userRef);
			
			if (this.createLocTime != null) {
				addSaveJSONObject.put("sinv_create_loctime", dateTimeToString(this.createLocTime));
				addSaveJSONObject.put("sinv_create_time", this.createTime);
			}
			
			if(this.createUserId > 0)
				addSaveJSONObject.put("sinv_create_user_id", this.createUserId);
			if(this.createStatId > 0)
				addSaveJSONObject.put("sinv_create_stat_id", this.createStatId);
			
			if (this.receivingLocTime != null) {
				addSaveJSONObject.put("sinv_receiving_loctime", dateTimeToString(this.receivingLocTime));
				addSaveJSONObject.put("sinv_receiving_time", this.receivingTime);
			}
			
			if(this.receivingUserId > 0)
				addSaveJSONObject.put("sinv_receiving_user_id", this.receivingUserId);
			if(this.receivingStatId > 0)
				addSaveJSONObject.put("sinv_receiving_stat_id", this.receivingStatId);
			
			if (this.modifiedLocTime != null) {
				addSaveJSONObject.put("sinv_modified_loctime", dateTimeToString(this.modifiedLocTime));
				addSaveJSONObject.put("sinv_modified_time", this.modifiedTime);
			}
			
			if(this.modifiedUserId > 0)
				addSaveJSONObject.put("sinv_modified_user_id", this.modifiedUserId);
			if(this.modifiedStatId > 0)
				addSaveJSONObject.put("sinv_modified_stat_id", this.modifiedStatId);
			
			if (this.voidLocTime != null) {
				addSaveJSONObject.put("sinv_void_loctime", dateTimeToString(this.voidLocTime));
				addSaveJSONObject.put("sinv_void_time", this.voidTime);
			}
			
			if(this.voidUserId > 0)
				addSaveJSONObject.put("sinv_void_user_id", this.voidUserId);
			if(this.voidStatId > 0)
				addSaveJSONObject.put("sinv_void_stat_id", this.voidStatId);
			
			addSaveJSONObject.put("sinv_status", this.status);

		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject invoiceJSONObject) {
		JSONObject resultInvoice = null;
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		resultInvoice = invoiceJSONObject.optJSONObject("PosStockDeliveryInvoice");
		if(resultInvoice == null)
			resultInvoice = invoiceJSONObject;

		this.init();
		this.sinvId = resultInvoice.optInt("sinv_id");
		this.shopId = resultInvoice.optInt("sinv_shop_id");
		this.oletId = resultInvoice.optInt("sinv_olet_id");
		this.userRef = resultInvoice.optString("sinv_user_ref");				

		String sCreateLocTime = resultInvoice.optString("sinv_create_loctime");
		if(!sCreateLocTime.isEmpty())
			this.createLocTime = oFmt.parseDateTime(sCreateLocTime);
		
		this.createTime = resultInvoice.optString("sinv_create_time", oFmt.print(createLocTime.withZone(DateTimeZone.UTC)));
			
		this.createUserId = resultInvoice.optInt("sinv_create_user_id");
		this.createStatId = resultInvoice.optInt("sinv_create_stat_id");
		
		this.receivingTime = resultInvoice.optString("sinv_receiving_time", null);
		
		String sReceivingLocTime = resultInvoice.optString("sinv_receiving_loctime");
		if(!sReceivingLocTime.isEmpty())
			this.receivingLocTime = oFmt.parseDateTime(sReceivingLocTime);
		
		this.receivingUserId = resultInvoice.optInt("sinv_receiving_user_id");
		this.receivingStatId = resultInvoice.optInt("sinv_receiving_stat_id");
		
		this.modifiedTime = resultInvoice.optString("sinv_modified_time", null);
		
		String sModifiedLocTime = resultInvoice.optString("sinv_modified_loctime");
		if(!sModifiedLocTime.isEmpty())
			this.modifiedLocTime = oFmt.parseDateTime(sModifiedLocTime);
		
		this.modifiedUserId = resultInvoice.optInt("sinv_modified_user_id");
		this.modifiedStatId = resultInvoice.optInt("sinv_modified_stat_id");
		
		this.voidTime = resultInvoice.optString("sinv_void_time", null);
		
		String sVoidLocTime = resultInvoice.optString("sinv_void_loctime");
		if(!sVoidLocTime.isEmpty())
			this.voidLocTime = oFmt.parseDateTime(sVoidLocTime);
		
		this.voidUserId = resultInvoice.optInt("sinv_void_user_id");
		this.voidStatId = resultInvoice.optInt("sinv_void_stat_id");
		
		this.status = resultInvoice.optString("sinv_status", PosStockDeliveryInvoice.STATUS_OUTSTANDING);
		
		//check whether pos check item record exist
		JSONArray tempJSONArray = invoiceJSONObject.optJSONArray("PosStockDeliveryInvoiceItem");
		if (tempJSONArray != null) {
			for (int i = 0; i < tempJSONArray.length(); i++) {
				JSONObject oStockDeliveryInvoiceItem = tempJSONArray.optJSONObject(i);
				if(oStockDeliveryInvoiceItem != null) {
					PosStockDeliveryInvoiceItem oPosStockDeliveryInvoiceItem = new PosStockDeliveryInvoiceItem(oStockDeliveryInvoiceItem);
					this.invoiceItems.add(oPosStockDeliveryInvoiceItem);
				}
			}
		}
	}
	
	//save (add or update) record to database
	public boolean addUpdate(boolean bUpdate, boolean bUpdateOutletTable, PosOutletTable oOutletTable, List<PosStockDeliveryInvoiceItem> oNewItemList, int iBusinessDayId) {
		JSONObject requestJSONObject = new JSONObject();
		
		requestJSONObject = constructAddSaveJSON(bUpdate);
				
		//include new items if bWithItems is true
		if (!oNewItemList.isEmpty()) {
			PosStockDeliveryInvoiceItem oPosStockDeliveryInvoiceItem = new PosStockDeliveryInvoiceItem();
			try {
				requestJSONObject.put("items", oPosStockDeliveryInvoiceItem.constructMultipleItemAddSaveJSON(oNewItemList));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		//include outlet table information if bUpdateOutletTable is true
		if(bUpdateOutletTable && oOutletTable != null) {
			try {
				requestJSONObject.put("PosOutletTable", oOutletTable.constructAddSaveJSON(true));
			}catch (JSONException e) {
				e.printStackTrace();
			}
		}

		// Add business date ID
		try {
			requestJSONObject.put("businessDayId", iBusinessDayId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		//send data to server
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveStockDeliveryInvoice", requestJSONObject.toString(), false))
			return false;
		else {
			//After insert record to database, update Check's check id
			if (this.sinvId == 0) {
				if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null || !OmWsClientGlobal.g_oWsClient.get().getResponse().has("id"))
					return false;
				
				this.sinvId = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("id"); 

				// reset all items are unmodified
				for(PosStockDeliveryInvoiceItem oPosStockDeliveryInvoiceItem:oNewItemList) {
					oPosStockDeliveryInvoiceItem.setModified(false);
				}
			}
				
			return true;
		}

	}
	
	//change DateTime object to string for database insertion/update
	private String dateTimeToString (DateTime oDateTime) {
		if (oDateTime == null)
			System.out.println("null");
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		String timeString = fmt.print(oDateTime);

		return timeString;
	}
	
	//read data by sinv_id
	public boolean readById(int iSinvId, int iRecursive) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("id", Integer.toString(iSinvId));
			requestJSONObject.put("recursive", iRecursive);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataFromApi("gm", "pos", "getStockDeliveryInvoiceById", requestJSONObject.toString());	
	}
	
	//read all invoice
	public JSONArray readAll(int iOutletId, int iRecursive) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("outletId", Integer.toString(iOutletId));
			requestJSONObject.put("recursive", Integer.toString(iRecursive));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataListFromApi("gm", "pos", "getAllStockDeliveryInvoices", requestJSONObject.toString());	
	}

	//get sinv id
	public int getSinvId() {
		return this.sinvId;
	}
	
	//get shop id
	public int getShopId() {
		return this.shopId;
	}
	
	//get outlet id
	public int getOletId () {
		return this.oletId;
	}

	//get user ref
	public String getUserRef() {
		return this.userRef;
	}
	
	//get create loc time
	public DateTime getCreateLocTime() {
		return this.createLocTime;
	}
	
	//get receiving loc time
	public DateTime getReceivingLocTime() {
		return this.receivingLocTime;
	}
	
	//get modified loc time
	public DateTime getModifiedLocTime() {
		return this.modifiedLocTime;
	}
	
	//get void loc time
	public DateTime getVoidLocTime() {
		return this.voidLocTime;
	}
	
	//get receive user id
	public int getReceivingUserId() {
		return this.receivingUserId;
	}
	
	//get invoice item array list 
	public List<PosStockDeliveryInvoiceItem> getInvoiceItemsArrayList() {
		return this.invoiceItems;
	}
	
	public void setSinvId(int iSinvId) {
		this.sinvId = iSinvId;
	}
	
	public void setShopId(int iShopId) {
		this.shopId = iShopId;
	}
	
	public void setOletId(int iOletId) {
		this.oletId = iOletId;
	}
	
	public void setUserRef(String sUserRef) {
		this.userRef = sUserRef;
	}
	
	public void setCreateTime(String sCreateTime) {
		this.createTime = sCreateTime;
	}
	
	public void setCreateLocTime(DateTime oCreateTime) {
		this.createLocTime = oCreateTime;
	}
	
	public void setCreateUserId(int iUserId) {
		this.createUserId = iUserId;
	}
	
	public void setCreateStationId(int iStationId) {
		this.createStatId = iStationId;
	}
	
	public void setReceivingTime(String sReceivingTime) {
		this.receivingTime = sReceivingTime;
	}
	
	public void setReceivingLocTime(DateTime oReceivingTime) {
		this.receivingLocTime = oReceivingTime;
	}
	
	public void setReceivingUserId(int iUserId) {
		this.receivingUserId = iUserId;
	}
	
	public void setReceivingStationId(int iStationId) {
		this.receivingStatId = iStationId;
	}
	
	public void setModifiedTime(String sModifiedTime) {
		this.modifiedTime = sModifiedTime;
	}
	
	public void setModifiedLocTime(DateTime oModifiedTime) {
		this.modifiedLocTime = oModifiedTime;
	}
	
	public void setModifiedUserId(int iUserId) {
		this.modifiedUserId = iUserId;
	}
	
	public void setModifiedStationId(int iStationId) {
		this.modifiedStatId = iStationId;
	}
	
	public void setVoidTime(String sVoidTime) {
		this.voidTime = sVoidTime;
	}
	
	public void setVoidLocTime(DateTime oVoidTime) {
		this.voidLocTime = oVoidTime;
	}
	
	public void setVoidUserId(int iUserId) {
		this.voidUserId = iUserId;
	}
	
	public void setVoidStationId(int iStationId) {
		this.voidStatId = iStationId;
	}
	
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	public boolean haveNewItem() {
		for(PosStockDeliveryInvoiceItem oItem: this.invoiceItems) {
			if (oItem.getSitmId() == 0)
				return true;
		}
		
		return false;
	}
}
