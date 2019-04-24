package app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosOutletItem {
	private int oitmId;
	private int shopId;
	private int oletId;
	private int itemId;
	private int sequence;
	private String stockControlLevel;
	private BigDecimal stockQty;
	private String checkStock;
	private String soldout;
	
	private String sStockAvailability;
	
	//stockControlLevel
	public static String STOCK_CONTROL_LEVEL_NO = "";
	public static String STOCK_CONTROL_LEVEL_CURRENT_ITEM = "1";
	
	// checkStock
	public static String CHECK_STOCK_NO = "";
	public static String CHECK_STOCK_YES = "y";
	
	// soldout
	public static String SOLDOUT_NO = "";
	public static String SOLDOUT_YES = "s";
	
	//init object with initialize value
	public PosOutletItem() {
		this.init();
	}
	
	public PosOutletItem(JSONObject outletItemJSONObject) {
		readDataFromJson(outletItemJSONObject);
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("outletItem")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("outletItem")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("outletItem");
			if(tempJSONObject.isNull("PosOutletItem")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		return bResult;
	}
	
	//read a list of data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray outletItemJSONArray = null;
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;

			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("outletItems")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("outletItems")) {
				return null;
			}
			
			outletItemJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("outletItems");
		}
		
		return outletItemJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject outletItemJSONObject) {
		JSONObject resultOutletItem = null;
		
		resultOutletItem = outletItemJSONObject.optJSONObject("PosOutletItem");
		if(resultOutletItem == null)
			resultOutletItem = outletItemJSONObject;
			
		this.init();
		this.oitmId = resultOutletItem.optInt("oitm_id");
		this.shopId = resultOutletItem.optInt("oitm_shop_id");
		this.oletId = resultOutletItem.optInt("oitm_olet_id");
		this.itemId = resultOutletItem.optInt("oitm_item_id");
		this.sequence = resultOutletItem.optInt("oitm_seq");
		this.stockControlLevel = resultOutletItem.optString("oitm_stock_control_level", PosOutletItem.STOCK_CONTROL_LEVEL_NO);
		this.stockQty = new BigDecimal(resultOutletItem.optString("oitm_stock_qty", "0"));
		this.checkStock = resultOutletItem.optString("oitm_check_stock", PosOutletItem.CHECK_STOCK_NO);
		this.soldout = resultOutletItem.optString("oitm_soldout", PosOutletItem.SOLDOUT_NO);
	}
	
	//construct the save JSON
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveObject = new JSONObject();
		
		try {
			if(bUpdate)
				addSaveObject.put("oitm_id", this.oitmId);
			addSaveObject.put("oitm_shop_id", this.shopId);
			addSaveObject.put("oitm_olet_id", this.oletId);
			addSaveObject.put("oitm_item_id", this.itemId);
			addSaveObject.put("oitm_seq", this.sequence);
			addSaveObject.put("oitm_stock_control_level", this.stockControlLevel);
			addSaveObject.put("oitm_stock_qty", this.stockQty);
			addSaveObject.put("oitm_soldout", this.soldout);
			addSaveObject.put("oitm_check_stock", this.checkStock);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveObject;
	}
	
	//get order item
	public boolean readByMenuItemId(int iOutletId, int iItemId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("itemId", iItemId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if(!this.readDataFromApi("gm", "pos", "getOutletItemByItemId", requestJSONObject.toString()))
			return false;
		
		return true;
	}
	
	//get item list
	public JSONArray readAllByStockControlLevel(int iOutletId, String sStockControlLevel) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("stockControlLevel", sStockControlLevel);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllPosOutletItemsByCtrlAndOletId", requestJSONObject.toString());

		return responseJSONArray;
	}

	//get item list
	public JSONArray readAllBySoldout(int iOutletId, String sSoldout) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("soldout", sSoldout);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllPosOutletItemsBySoldoutAndOletId", requestJSONObject.toString());

		return responseJSONArray;
	}

	//get item list
	public JSONArray readAllByCheckStockSoldout(int iOutletId, String sCheckStock, String sSoldout) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("checkStock", sCheckStock);
			requestJSONObject.put("soldout", sSoldout);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllPosOutletItemsByCheckStockSoldoutAndOletId", requestJSONObject.toString());

		return responseJSONArray;
	}
	
	public JSONArray readAllByItemIds(int iOutletId, List<Integer> oItemIdList) {
		JSONArray oItemIdArray = new JSONArray(), oResultJSONArray = null;
		JSONObject requestJSONObject = new JSONObject(), tempJSONObject = null;

		try {
			for(Integer iItemDiscGrpId:oItemIdList) {
				tempJSONObject = new JSONObject();
				tempJSONObject.put("id", iItemDiscGrpId);
				oItemIdArray.put(tempJSONObject);
			}
			requestJSONObject.put("itemIds", oItemIdArray);
			requestJSONObject.put("outletId", iOutletId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		oResultJSONArray = this.readDataListFromApi("gm", "pos", "getAllPosOutletItemsByItemIds", requestJSONObject.toString());
		
		return oResultJSONArray;
	}
	
	//get outlet item by oitm_id
	public boolean readById(int iOitmId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("id", Integer.toString(iOitmId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if(!this.readDataFromApi("gm", "pos", "getOutletItemById", requestJSONObject.toString()))
			return false;
		
		return true;
	}
	
	//add or update a outlet item to pos_outlet_item
	public boolean addUpdate(boolean bUpdate) {
		JSONObject requestJSONObject = new JSONObject();
		
		requestJSONObject = this.constructAddSaveJSON(bUpdate);
		
		if(!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveOutletItem", requestJSONObject.toString(), false))
			return false;
		else{
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("id")){
				if(!OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("id")) {
					JSONObject resultOutletItem = OmWsClientGlobal.g_oWsClient.get().getResponse();
					this.oitmId = resultOutletItem.optInt("id");
				}
			}
			return true;
		}
	}
	
	//update stock qty
	public boolean updateStockQty(BigDecimal dUpdateQty, boolean bSubtrack, boolean bForceUpdate) {
		JSONObject requestJSONObject = new JSONObject(), tempJSONObject = null;
		
		try{
			requestJSONObject.put("id", this.oitmId);
			requestJSONObject.put("updateQty", dUpdateQty.toString());
			requestJSONObject.put("checkStock", PosOutletItem.CHECK_STOCK_YES); // update item to check stock count
			if(bSubtrack)
				requestJSONObject.put("subtrack", 1);
			else
				requestJSONObject.put("subtrack", 0);
			if(bForceUpdate)
				requestJSONObject.put("forceToUpdate", 1);
			else
				requestJSONObject.put("forceToUpdate", 0);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveOutletItemStockQty", requestJSONObject.toString(), false))
			return false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("outletItem");
			if(tempJSONObject != null)
				this.readDataFromJson(tempJSONObject);

			this.sStockAvailability = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("availability");
			return true;
		}
	}
	
	//update stock qty
	public JSONObject updateMultipleStockQty(List<HashMap<String, String>> oUpdateStockInfos, boolean bSubtract, boolean bForceUpdate) {
		JSONObject requestJSONObject = new JSONObject(), tempJSONObject = null;
		JSONArray oOutletItemJSONArray = new JSONArray();
		
		try{
			for(HashMap<String, String> oUpdateStock:oUpdateStockInfos) {
				tempJSONObject = new JSONObject();
				tempJSONObject.put("id", Integer.parseInt(oUpdateStock.get("oitmId")));
				tempJSONObject.put("updateQty", oUpdateStock.get("qty"));
				oOutletItemJSONArray.put(tempJSONObject);
			}
			requestJSONObject.put("outletItems", oOutletItemJSONArray);
			requestJSONObject.put("checkStock", PosOutletItem.CHECK_STOCK_YES); // update item to check stock count
			if(bSubtract)
				requestJSONObject.put("subtract", 1);
			else
				requestJSONObject.put("subtract", 0);
			if(bForceUpdate)
				requestJSONObject.put("forceToUpdate", 1);
			else
				requestJSONObject.put("forceToUpdate", 0);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveMultipleOutletItemsStockQty", requestJSONObject.toString(), false))
			return null;
		else {
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("availability") || !OmWsClientGlobal.g_oWsClient.get().getResponse().has("outletItems")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
				
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
			
		}
	}

	//add or update multiple check payments record to database
	public boolean addUpdateWithMutlipleRecord(ArrayList<PosOutletItem> oPosOutletItems) {
		JSONObject tempOutletItemJSONObject = null, outletItemJSONObject = new JSONObject();
		JSONArray outletItemJSONArray = new JSONArray();
		
		for(PosOutletItem oPosOutletItem:oPosOutletItems){
			if(oPosOutletItem.getOitmId() == 0)
				tempOutletItemJSONObject = oPosOutletItem.constructAddSaveJSON(false);
			else
				tempOutletItemJSONObject = oPosOutletItem.constructAddSaveJSON(true);
			outletItemJSONArray.put(tempOutletItemJSONObject);
		}
		
		try {
			outletItemJSONObject.put("outletItems", outletItemJSONArray);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveMultiOutletItems", outletItemJSONObject.toString(), false))
			return false;
		else
			return true;
	}
	
	public JSONArray addUpdateWithMultipleRecordWithMenuAndItemIds(int iOutletId, List<Integer> oMenuIds, List<Integer> oItemIds, String sSoldout) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		JSONArray oMenuIdArray = new JSONArray(), oItemIdArray = new JSONArray();
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			for(Integer iMenuId: oMenuIds) {
				JSONObject tempJSONObject = new JSONObject();
				tempJSONObject.put("id", iMenuId);
				oMenuIdArray.put(tempJSONObject);
			}
			requestJSONObject.put("menuIds", oMenuIdArray);
			for(Integer iItemId: oItemIds) {
				JSONObject tempJSONObject = new JSONObject();
				tempJSONObject.put("id", iItemId);
				oItemIdArray.put(tempJSONObject);
			}
			requestJSONObject.put("itemIds", oItemIdArray);
			requestJSONObject.put("soldout", sSoldout);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "saveMultiOutletItemsByMenuAndItemIds", requestJSONObject.toString());

		return responseJSONArray;
	}
	
	//delete outlet item by oitm_id
	public boolean deleteById() {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("id", this.oitmId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "deleteOutletItemById", requestJSONObject.toString());

	}
	
	public boolean deleteOutletItemListByIds(List<Integer> oItemIds) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("itemIds", oItemIds);
		}
		catch (JSONException jsone){
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "deleteMultiOutletItemByIds", requestJSONObject.toString(), false))
			return false;
		else
			return true;
	}
	
	public boolean moveSequence(int ioitmId, int iDirection) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("itemId", ioitmId);
			requestJSONObject.put("direction", iDirection);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if(!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "moveSequence", requestJSONObject.toString(), false))
			return false;
		else{
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("result"))
				return false;
			
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("result"))
				return false;
			
			try {
				if (!OmWsClientGlobal.g_oWsClient.get().getResponse().getBoolean("result"))
					return false;
			}catch (JSONException jsone) {
				jsone.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public boolean resequence(int ioitmId, int iTargetSeq) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("itemId", ioitmId);
			requestJSONObject.put("seq", iTargetSeq);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if(!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "resequence", requestJSONObject.toString(), false))
			return false;
		else{
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("result"))
				return false;
			
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("result"))
				return false;
			
			try {
				if (!OmWsClientGlobal.g_oWsClient.get().getResponse().getBoolean("result"))
					return false;
			}catch (JSONException jsone) {
				jsone.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	// init value
	public void init() {
		this.oitmId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.itemId = 0;
		this.sequence = 0;
		this.stockControlLevel = PosOutletItem.STOCK_CONTROL_LEVEL_NO;
		this.stockQty = BigDecimal.ZERO;
		this.checkStock = PosOutletItem.CHECK_STOCK_NO;
		this.soldout = PosOutletItem.SOLDOUT_NO;
		this.sStockAvailability = "";
	}
	
	public void setOitmId(int iOitmId) {
		this.oitmId = iOitmId;
	}
	
	public void setShopId(int iShopId) {
		this.shopId = iShopId;
	}
	
	public void setOletId(int iOutletId) {
		this.oletId = iOutletId;
	}
	
	public void setItemId(int iItemId) {
		this.itemId = iItemId;
	}
	
	public void setStockControlLevel(String sStockControlLevel) {
		this.stockControlLevel = sStockControlLevel;
	}
	
	public void setStockQty(BigDecimal dStockQty) {
		this.stockQty = dStockQty;
	}
	
	public void setCheckStock(String sCheckStock) {
		this.checkStock = sCheckStock;
	}
	
	public void setSoldout(String sSoldout) {
		this.soldout = sSoldout;
	}
	
	public void setSequence(int iSeq) {
		this.sequence = iSeq;
	}
	
	protected void setStockAvailability(String sAvailability) {
		this.sStockAvailability = sAvailability;
	}
	
	public int getOitmId() {
		return this.oitmId;
	}
	
	protected int getShopId() {
		return this.shopId;
	}
	
	protected int getOletId() {
		return this.oletId;
	}
	
	public int getItemId() {
		return this.itemId;
	}
	
	protected String getStockControlLevel() {
		return this.stockControlLevel;
	}
	
	public BigDecimal getStockQty() {
		return this.stockQty;
	}
	
	public String getCheckStock() {
		return this.checkStock;
	}
	
	public String getSoldout() {
		return this.soldout;
	}
	
	public int getSequence() {
		return this.sequence;
	}
	
	public String getStockAvailability() {
		return this.sStockAvailability;
	}
	
	public boolean isSoldout() {
		return this.soldout.equals(PosOutletItem.SOLDOUT_YES);
	}
	
	public boolean isCheckStock() {
		return this.checkStock.equals(PosOutletItem.CHECK_STOCK_YES);
	}
}
