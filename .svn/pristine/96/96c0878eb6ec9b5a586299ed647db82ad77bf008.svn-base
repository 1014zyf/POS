//Database: pos_outlet_tables - Tables in the outlet for POS operation
package app.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosOutletTable {
	private int otblId;
	private int shopId;
	private int oletId;
	private int table;
	private String tableExt;
	private int statId;
	private int chksId;
	private JSONObject info;
	private String tag;
	private String status;
	
	//internal used variable
	private String  sLastGetRecordTime;
	
	// tag
	public static String TAG_NO = "";
	public static String TAG_ASSIGNED = "a";
	public static String TAG_SEAT_IN = "s";
	public static String TAG_BILLING = "b";
	
	// status
	public static String STATUS_VACANT = "";
	public static String STATUS_NEW_TABLE = "n";
	public static String STATUS_OCCUPIED = "o";
	public static String STATUS_CHECK_PRINTED = "p";
	//public static String STATUS_CLEANING_TABLE = "c";
	
	//init object with initialize value
	public PosOutletTable() {
		this.init();
	}
	
	//Init object from database by otbl_id
	public PosOutletTable(int iOtblId) {
		this.init();
		
		this.otblId = iOtblId;
	}
	
	//init object from database by otbl_olet_id, otbl_shop_id, otbl_table, otbl_table_ext
	public PosOutletTable(int iOletId, int iTable, String sTableExt) {
		this.init();
		this.readByOutletIdTable(iOletId, iTable, sTableExt);
	}
	
	//init object form JSO
	public PosOutletTable(JSONObject outletTableJSONObject) {
		readDataFromJson(outletTableJSONObject);
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("outletTable")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("outletTable")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("outletTable");
			if(tempJSONObject.isNull("PosOutletTable")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
			bResult = true;
			
		}
		
		return bResult;
	}

	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray outletTableJSONArray = null;

		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, true))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("getRecordTime"))
				this.sLastGetRecordTime = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("getRecordTime");

			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("outletTables")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(),	"", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("outletTables"))
				return null;
			
			outletTableJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("outletTables");
		}
		
		return outletTableJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject outletTableJSONObject) {
		JSONObject resultOutletTable = null;
		resultOutletTable = outletTableJSONObject.optJSONObject("PosOutletTable");
		if(resultOutletTable == null)
			resultOutletTable = outletTableJSONObject;
			
		this.init();
		this.otblId = resultOutletTable.optInt("otbl_id");
		this.shopId = resultOutletTable.optInt("otbl_shop_id");
		this.oletId = resultOutletTable.optInt("otbl_olet_id");
		this.table = resultOutletTable.optInt("otbl_table");
		this.tableExt = resultOutletTable.optString("otbl_table_ext");
		this.statId = resultOutletTable.optInt("otbl_stat_id");
		this.chksId = resultOutletTable.optInt("otbl_chks_id");
		try {
			this.info = new JSONObject(resultOutletTable.optString("otbl_info"));
		} catch (JSONException e) {
			this.info = null;
		}
		this.tag = resultOutletTable.optString("otbl_tag", PosOutletTable.TAG_NO);
		this.status = resultOutletTable.optString("otbl_status", PosOutletTable.STATUS_VACANT);
	}
	
	//construct the save request JSON
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("otbl_id", this.otblId);
			addSaveJSONObject.put("otbl_shop_id", this.shopId);
			addSaveJSONObject.put("otbl_olet_id", this.oletId);
			addSaveJSONObject.put("otbl_table", this.table);
			addSaveJSONObject.put("otbl_table_ext", this.tableExt);
			addSaveJSONObject.put("otbl_stat_id", this.statId);
			addSaveJSONObject.put("otbl_chks_id", this.chksId);
			if(this.info != null)
				addSaveJSONObject.put("otbl_info", this.info.toString());
			else
				addSaveJSONObject.put("otbl_info", JSONObject.NULL);
			addSaveJSONObject.put("otbl_tag", this.tag);
			addSaveJSONObject.put("otbl_status", this.status);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	// init value
	public void init() {
		this.otblId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.table = 0;
		this.tableExt = "";
		this.statId = 0;
		this.chksId = 0;
		this.info = null;
		this.tag = PosOutletTable.TAG_NO;
		this.status = PosOutletTable.STATUS_VACANT;
		this.sLastGetRecordTime = "";
	}
	
	public void copyFromOtherOutletTable(PosOutletTable oOutletTable) {
		this.otblId = oOutletTable.otblId;
		this.shopId = oOutletTable.shopId;
		this.oletId = oOutletTable.oletId;
		this.table = oOutletTable.table;
		this.tableExt = oOutletTable.tableExt;
		this.statId = oOutletTable.statId;
		this.chksId = oOutletTable.chksId;
		this.info = oOutletTable.info;
		this.tag = oOutletTable.tag;
		this.status = oOutletTable.status;
		this.sLastGetRecordTime = "";
	}
	
	//read data from database by otbl_id
	public boolean readById(int iOtblId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("id", iOtblId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "getOutletTableById", requestJSONObject.toString());

	}
	
	//read data from database by otbl_olet_id, otb_table, otbl_table_ext
	public boolean readByOutletIdTable(int iOletId, int iTable, String sTableExt) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("outletId", Integer.toString(iOletId));
			requestJSONObject.put("table", Integer.toString(iTable));
			if (sTableExt == null)
				requestJSONObject.put("tableExt", "");
			else
				requestJSONObject.put("tableExt", sTableExt);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataFromApi("gm", "pos", "getOutletTableByOutletTable", requestJSONObject.toString());
	}

	//get active outlet table lists from database
	public JSONArray getActiveOutletTableListByOutletId(int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("recursive", 0);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getOutletTableListByOletId", requestJSONObject.toString());
		
		return responseJSONArray;
		
	}
	
	//get active outlet table lists from database by last modified time
		public JSONArray getActiveOutletTableListByOutletIdModifiedTime(int iOutletId, String sModifiedTime) {
			JSONObject requestJSONObject = new JSONObject();
			JSONArray responseJSONArray = null;
			
			try {
				requestJSONObject.put("outletId", iOutletId);
				if(!sModifiedTime.isEmpty())
					requestJSONObject.put("modified", sModifiedTime);
				requestJSONObject.put("recursive", 0);
			}catch (JSONException jsone) {
				jsone.printStackTrace();
			}
			
			responseJSONArray = this.readDataListFromApi("gm", "pos", "getOutletTableListByModified", requestJSONObject.toString());
			
			return responseJSONArray;
			
		}
	
	//get active outlet table lists from database
	public JSONArray getActiveOutletTableListByTableNo(int iOutletId, int iTableNo) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("table", iTableNo);
			requestJSONObject.put("recursive", 0);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getOutletTableWithExtensionListByOutletTable", requestJSONObject.toString());
		
		return responseJSONArray;
		
	}
	
	//read and lock the table
	public boolean readAndLock() {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("otbl_shop_id", this.shopId);
			requestJSONObject.put("otbl_olet_id", this.oletId);
			requestJSONObject.put("otbl_table", this.table);
			if (this.tableExt == null)
				requestJSONObject.put("otbl_table_ext", "");
			else
				requestJSONObject.put("otbl_table_ext", this.tableExt);
			requestJSONObject.put("otbl_stat_id", this.statId);
			requestJSONObject.put("otbl_status", this.status);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataFromApi("gm", "pos", "getAndLockOutletTable", requestJSONObject.toString());
	}
	
	//read data from database by otbl_olet_id, otbl_shop_id, otb_table, otbl_table_ext
	public boolean readAndUnlock() {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("outletId", this.oletId);
			requestJSONObject.put("table", this.table);
			requestJSONObject.put("tableExtension", this.tableExt);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataFromApi("gm", "pos", "getAndUnlockOutletTable", requestJSONObject.toString());
	}
	
	//add or update a check item to database
	public boolean addUpdate(boolean update) {
		JSONObject requestJSONObject = new JSONObject();
		
		requestJSONObject = this.constructAddSaveJSON(update);
		return this.readDataFromApi("gm", "pos", "saveAndReadOutletTable", requestJSONObject.toString());
	}

	//read data from database by otbl_id
	public boolean deleteById() {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("id", this.otblId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "deleteOutletTableById", requestJSONObject.toString());

	}
	
	//read data from database by otbl_id
	public boolean deleteByOutletAndShopId() {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("outletId", this.oletId);
			requestJSONObject.put("shopId", this.shopId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "deleteOutletTableByOutletAndShopId", requestJSONObject.toString());

	}
	
	//set shop id with int value
	public void setShopId(int iShopId) {
		this.shopId = iShopId;
	}
	
	//set outlet id with int value
	public void setOutletId(int iOutletId) {
		this.oletId = iOutletId;
	}
	
	//set table id with int value
	public void setTable(int iTable) {
		this.table = iTable;
	}
	
	//set table extension with string value
	public void setTableExtension(String sTableExtension) {
		this.tableExt = sTableExtension;
	}
	
	//set station id with int value
	public void setStationId(int iStationId) {
		this.statId = iStationId;
	}
	
	//set table extension with string value
	public void setCheckId(int iCheckId) {
		this.chksId = iCheckId;
	}
	
	//set info with JSONObject value
	public void setInfo(JSONObject oInfoJSONObject) {
		this.info = oInfoJSONObject;
	}
	
	//set tag with string value
	public void setTag(String sTag) {
		this.tag = sTag;
	}
	
	//set station id with int value
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	//get otblId
	public int getOtblId() {
		return this.otblId;
	}
	
	//get shop id
	public int getShopId() {
		return this.shopId;
	}
	
	//get outlet id
	public int getOutletId() {
		return this.oletId;
	}
	
	//get table number
	public int getTable() {
		return this.table;
	}
	
	//get table extension
	public String getTableExtension() {
		return this.tableExt;
	}
	
	//get station id
	public int getStationId() {
		return this.statId;
	}
	
	//get check id
	public int getCheckId() {
		return this.chksId;
	}
	
	//get info
	public JSONObject getInfo() {
		return this.info;
	}
	
	//get tag
	public String getTag() {
		return this.tag;
	}
		
	//get status
	public String getStatus() {
		return this.status;
	}
	
	//get last get record time
	public String getLastGetRecordTime() {
		return this.sLastGetRecordTime;
	}
	
	public boolean isVacant() {
		return this.status.equals(PosOutletTable.STATUS_VACANT);
	}
	
	public boolean isCheckPrinted() {
		return this.status.equals(PosOutletTable.STATUS_CHECK_PRINTED);
	}
	
	public boolean isOccupied() {
		return this.status.equals(PosOutletTable.STATUS_OCCUPIED);
	}
	
	public boolean isNewTable() {
		return this.status.equals(PosOutletTable.STATUS_NEW_TABLE);
	}
	
	/*public boolean isCleaningTable() {
		return this.status.equals(PosOutletTable.STATUS_CLEANING_TABLE);
	}*/
	
	public boolean isSeatInTag() {
		return this.tag.equals(PosOutletTable.TAG_SEAT_IN);
	}
}
