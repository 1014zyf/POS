//Database: pos_configs - System configuration for POS module
package om;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;

public class PosStockTransaction {
	private String strsId;
	private String bdayId;
	private int shopId;
	private int oletId;
	private int itemId;
	private String stockTransactionType;
	private BigDecimal stockTransactionQty;
	private int userId;
	private DateTime actionTime;
	private DateTime actionLocTime;
	private String status;
	
	public static String TRANSACTION_TYPE_OPEN_BALANCE = "o";
	public static String TRANSACTION_TYPE_SELF_STOCK_IN = "c";
	public static String TRANSACTION_TYPE_STOCK_IN = "k";
	public static String TRANSACTION_TYPE_WASTAGE = "w";
	public static String TRANSACTION_TYPE_DAMAGE = "d";
	public static String TRANSACTION_TYPE_ADJUST = "a";
	public static String TRANSACTION_TYPE_CLOSE_BALANCE = "e";
	
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosStockTransaction () {
		this.init();
	}
	
	public PosStockTransaction(JSONObject stockTransactionJSONObject) {
		readDataFromJson(stockTransactionJSONObject);
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject stockTransactionJSONObject) {
		JSONObject tempJSONObject = null;
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		tempJSONObject = stockTransactionJSONObject.optJSONObject("PosStockTransaction");
		if(tempJSONObject == null)
			tempJSONObject = stockTransactionJSONObject;
			
		this.init();
		this.strsId = tempJSONObject.optString("strs_id"); 
		this.bdayId = tempJSONObject.optString("strs_bday_id");
		this.shopId = tempJSONObject.optInt("strs_shop_id");
		this.oletId = tempJSONObject.optInt("strs_olet_id");
		this.itemId = tempJSONObject.optInt("strs_item_id");
		this.stockTransactionType = tempJSONObject.optString("strs_stock_transaction_type");
		this.stockTransactionQty = new BigDecimal(tempJSONObject.optString("strs_stock_transaction_qty", "0.0"));
		this.userId = tempJSONObject.optInt("strs_user_id");
		
		String actionTime = tempJSONObject.optString("strs_action_time");
		if(!actionTime.isEmpty())
			this.actionTime = oFmt.parseDateTime(actionTime);
		
		String actionLocTime = tempJSONObject.optString("strs_action_loctime");
		if(!actionLocTime.isEmpty())
			this.actionLocTime = oFmt.parseDateTime(actionLocTime);
		this.status = tempJSONObject.optString("strs_status", PosStockTransaction.STATUS_ACTIVE);
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("config")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("config")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("config");
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray stockTransactionJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("stockTransactions"))
				return null;
				
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("stockTransactions"))
				return null;
			
			stockTransactionJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("stockTransactions");
		}
		
		return stockTransactionJSONArray;
	}
	
	// init value
	public void init() {
		this.strsId = "";
		this.bdayId = "";
		this.shopId = 0;
		this.oletId = 0;
		this.itemId = 0;
		this.stockTransactionType = "";
		this.stockTransactionQty = BigDecimal.ZERO;
		this.userId = 0;
		this.actionTime = null;
		this.actionLocTime = null;
		this.status = PosStockTransaction.STATUS_ACTIVE;
	}
	
	//read stock transaction records
	public JSONArray searchStockTransaction(String sBusinessDayId, int iOutletId, ArrayList<Integer> oItemIds, String sStatus) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("businessDayId", sBusinessDayId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("itemIds", oItemIds);
			requestJSONObject.put("status", sStatus);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getItemStockTransaction", requestJSONObject.toString());

		return responseJSONArray;
	}
	
	//read stock transaction records
	public JSONArray searchStockTransactionByItemIdAndType(String sBusinessDayId, int iOutletId, ArrayList<Integer> oItemIds, String sType) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("businessDayId", sBusinessDayId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("itemIds", oItemIds);
			requestJSONObject.put("transactionType", sType);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getItemStockTransactionByItemIdAndType", requestJSONObject.toString());

		return responseJSONArray;
	}
	
	//read stock transaction records by type
		public JSONArray searchStockTransactionByType(String sBusinessDayId, int iOutletId, String sType, String sDateMode) {
			JSONObject requestJSONObject = new JSONObject();
			JSONArray responseJSONArray = null;

			try {
				requestJSONObject.put("businessDayId", sBusinessDayId);
				requestJSONObject.put("outletId", iOutletId);
				requestJSONObject.put("transactionType", sType);
				requestJSONObject.put("dateMode", sDateMode);
			}catch (JSONException jsone) {
				jsone.printStackTrace();
			}
			
			responseJSONArray = this.readDataListFromApi("gm", "pos", "getItemStockTransactionByType", requestJSONObject.toString());
			
			return responseJSONArray;
		}
	
	// Dailt start / close operation
	/*public boolean dailyStartAndCloseStockOperation(int iBusinessDayId, int iShopId, int iOutletId, int iUserId, String sTransactionType) {
		JSONObject requestJSONObject = new JSONObject();
		
		DateTime oCurrentDateTime = new DateTime();
		
		try {
			requestJSONObject.put("businessDayId", iBusinessDayId);
			requestJSONObject.put("shopId", iShopId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("userId", iUserId);
			requestJSONObject.put("transactionType", sTransactionType);
			requestJSONObject.put("actionTime", OmWsClientGlobal.dateTimeToString(oCurrentDateTime.withZone(DateTimeZone.UTC)));
			requestJSONObject.put("actionLocTime", OmWsClientGlobal.dateTimeToString(oCurrentDateTime));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if(!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "dailyStartCloseStockOperation", requestJSONObject.toString(), false) == false)
			return false;
		else 
			return true;
	}*/
	
	// Update current item stock count
	// mode:	0 - auto-calculate the damage
	//			1 - auto-calculate the close balance
	public boolean autoBalanceOutletItemStock(String sBusinessDayId, int iShopId, int iOutletId, ArrayList<Integer> oItemIds, int iUserId, int iMode) {
		JSONObject requestJSONObject = new JSONObject();
		
		DateTime oCurrentDateTime = AppGlobal.getCurrentTime(false);
		
		try {
			requestJSONObject.put("businessDayId", sBusinessDayId);
			requestJSONObject.put("shopId", iShopId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("itemIds", oItemIds);
			requestJSONObject.put("userId", iUserId);
			requestJSONObject.put("actionTime", OmWsClientGlobal.dateTimeToString(AppGlobal.convertTimeToUTC(oCurrentDateTime)));
			requestJSONObject.put("actionLocTime", OmWsClientGlobal.dateTimeToString(oCurrentDateTime));
			requestJSONObject.put("mode", iMode);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "autoBalanceOutletItemStock", requestJSONObject.toString(), false))
			return false;
		else
			return true;
	}
	
	//construct the save JSON
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveObject = new JSONObject();
		
		try {
			if(bUpdate)
				addSaveObject.put("strs_id", this.strsId);
			addSaveObject.put("strs_bday_id", this.bdayId);
			addSaveObject.put("strs_shop_id", this.shopId);
			addSaveObject.put("strs_olet_id", this.oletId);
			addSaveObject.put("strs_item_id", this.itemId);
			addSaveObject.put("strs_stock_transaction_type", this.stockTransactionType);
			addSaveObject.put("strs_stock_transaction_qty", this.stockTransactionQty);
			addSaveObject.put("strs_user_id", this.userId);
			if (this.actionTime != null) {
				addSaveObject.put("strs_action_time", OmWsClientGlobal.dateTimeToString(this.actionTime));
			}
			if (this.actionLocTime != null) {
				addSaveObject.put("strs_action_loctime", OmWsClientGlobal.dateTimeToString(this.actionLocTime));
			}
			addSaveObject.put("strs_status", this.status);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveObject;
	}
	
	//add or update a outlet item to pos_outlet_item
	public boolean addUpdate(boolean bUpdate) {
		JSONObject requestJSONObject = new JSONObject();
		
		requestJSONObject = this.constructAddSaveJSON(bUpdate);
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveStockTransaction", requestJSONObject.toString(), false))
			return false;
		else 
			return true;
	}
	
	public JSONArray addUpdateWithMutlipleTransactions(ArrayList<PosStockTransaction> oPosMutliStockTransactions) {
		JSONObject tempStockTransJSONObject = null, stockTransJSONObject = new JSONObject();
		JSONArray stockTransactionsJSONArray = new JSONArray();
		JSONArray oReturnJSONArray = null;
		
		for(PosStockTransaction oPosStockTransaction : oPosMutliStockTransactions) {
			if(oPosStockTransaction.getTransactionId().equals(""))
				tempStockTransJSONObject = oPosStockTransaction.constructAddSaveJSON(false);
			else
				tempStockTransJSONObject = oPosStockTransaction.constructAddSaveJSON(true);
			stockTransactionsJSONArray.put(tempStockTransJSONObject);
		}
		
		try {
			stockTransJSONObject.put("posStockTransactions", stockTransactionsJSONArray);
		} catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveMultiStockTransactions", stockTransJSONObject.toString(), false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("newStockTransactions"))
				return null;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("newStockTransactions"))
				return null;
			
			oReturnJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("newStockTransactions");
			return oReturnJSONArray;
		}
	}
	
	public String getTransactionId(){
		return this.strsId;
	}
	
	public void setTransactionId(String sTransactionId){
		this.strsId = sTransactionId;
	}
	
	public String getBusinessDayId(){
		return this.bdayId;
	}
	
	public void setBusinessDayId(String sBusinessDayId){
		this.bdayId = sBusinessDayId;
	}
	
	public void setShopId(int iShopId){
		this.shopId = iShopId;
	}
	
	public void setOutletId(int iOutletId){
		this.oletId = iOutletId;
	}
	
	public int getItemId() {
		return this.itemId;
	}
	
	public void setItemId(int iItemId){
		this.itemId = iItemId;
	}
	
	public String getTransactionType(){
		return this.stockTransactionType;
	}
	
	public void setTransactionType(String sTransactionType){
		this.stockTransactionType = sTransactionType;
	}
	
	public BigDecimal getTransactionQty(){
		return this.stockTransactionQty;
	}
	
	public void setTransactionQty(BigDecimal dTransactionQty){
		this.stockTransactionQty = dTransactionQty;
	}
	
	public void setUserId(int iUserId){
		this.userId = iUserId;
	}
	
	public void setActionTime(DateTime oDateTime){
		this.actionTime = oDateTime;
	}
	
	public void setActionLocTime(DateTime oDateLocTime){
		this.actionLocTime = oDateLocTime;
	}
	
	public void setStatus(String sStatus){
		this.status = sStatus;
	}
}
