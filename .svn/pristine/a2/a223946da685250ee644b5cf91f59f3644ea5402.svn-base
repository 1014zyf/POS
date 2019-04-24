//Database: pos_check_tables - Sales check's table
package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosCheckTable {
	private String ctblId;
	private int oletId;
	private String chksId;
	private int table;
	private String tableExt;
	private int tableSize;
	private String additional;
	private String ptblId;
	private String status;
	
	// additional
	public static String ADDITIONAL_NO = "";
	public static String ADDITIONAL_YES = "y";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosCheckTable () {
		this.init();
	}
	
	//init object with JSON Object
	public PosCheckTable(JSONObject checkItemJSONObject) {
		readDataFromJson(checkItemJSONObject);
	}
	
	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bUpdate = true;
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bUpdate = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("checkTable")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("checkTable")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("checkTable");
			if(tempJSONObject.isNull("PosCheckTable")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bUpdate;
	}	

	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray checkTableJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("checkTable")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("checkTable")) 
				return null;
			
			checkTableJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("checkTable");
		}
		
		return checkTableJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject checkTableJSONObject) {
		JSONObject resultCheckTable = null;
		
		resultCheckTable = checkTableJSONObject.optJSONObject("PosCheckTable");
		if(resultCheckTable == null)
			resultCheckTable = checkTableJSONObject;
		
		this.init();
		this.ctblId = resultCheckTable.optString("ctbl_id");
		this.oletId = resultCheckTable.optInt("ctbl_olet_id");
		this.chksId = resultCheckTable.optString("ctbl_chks_id");
		this.table = resultCheckTable.optInt("ctbl_table");
		this.tableExt = resultCheckTable.optString("ctbl_table_ext");
		this.tableSize = resultCheckTable.optInt("ctbl_table_size");
		this.additional = resultCheckTable.optString("ctbl_additional", PosCheckTable.ADDITIONAL_NO);
		this.ptblId = resultCheckTable.optString("ctbl_ptbl_id");
		this.status = resultCheckTable.optString("ctbl_status");
	}
	
	//construct the save request JSON
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("ctbl_id", this.ctblId);
			addSaveJSONObject.put("ctbl_olet_id", this.oletId);
			addSaveJSONObject.put("ctbl_chks_id", this.chksId);
			addSaveJSONObject.put("ctbl_table", this.table);
			addSaveJSONObject.put("ctbl_table_ext", this.tableExt);
			if(this.tableSize > 0)
				addSaveJSONObject.put("ctbl_table_size", this.tableSize);
			if(!this.additional.isEmpty())
				addSaveJSONObject.put("ctbl_additional", this.additional);
			if(this.ptblId.compareTo("") > 0)
				addSaveJSONObject.put("ctbl_ptbl_id", this.ptblId);
			addSaveJSONObject.put("ctbl_status", this.status);

		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	//read data from database with ctbl_chks_id
	public boolean readByCheckId (String sCtblId) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("checkId", sCtblId);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if(!this.readDataFromApi("gm", "pos", "getCheckTableByCheckId", requestJSONObject.toString()))
			return false;
		
		return true;
	}

	//add or update a check item to database
	public boolean addUpdate(boolean update, int iActionUserId, int iActionStationId, int iTimeZoneOffset, String sTimeZoneName) {
		JSONObject requestJSONObject = new JSONObject();
		
		requestJSONObject = this.constructAddSaveJSON(update);
		
		// Set action user id and station id
		try {
			requestJSONObject.put("actionUserId", iActionUserId);
			requestJSONObject.put("actionStationId", iActionStationId);
			requestJSONObject.put("timezoneOffset", iTimeZoneOffset);
			if(!sTimeZoneName.isEmpty())
				requestJSONObject.put("timezoneName", sTimeZoneName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveCheckTable", requestJSONObject.toString(), false))
			return false;
		else
			return true;
	}

	// init value
	public void init() {
		this.ctblId = "";
		this.oletId = 0;
		this.chksId = "";
		this.table = 0;
		this.tableExt = "";
		this.tableSize = 0;
		this.additional = PosCheckTable.ADDITIONAL_NO;
		this.ptblId = "";
		this.status = PosCheckTable.STATUS_ACTIVE;
	}
	
	//add new check table record to database
	public boolean add() {
		return true;
	}
	
	//update check table record to database
	public boolean update() {
		return true;
	}
	
	public void setTable(int iTable) {
		this.table = iTable;
	}
	
	public void setTableExt(String sTableExt) {
		this.tableExt = sTableExt;
	}
	
	public void setOutletId(int iOutletId) {
		this.oletId = iOutletId;
	}
	
	public void setCheckId(String sCheckId) {
		this.chksId = sCheckId;
	}
	
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	//get ctblId
	public String getCtblId() {
		return this.ctblId;
	}
	
	public int getOutletId() {
		return this.oletId;
	}
	
	protected String getChksId() {
		return this.chksId;
	}
	
	public int getTable() {
		return this.table;
	}
	
	public String getTableExt() {
		return this.tableExt;
	}
	
	public int getTableSize() {
		return this.tableSize;
	}
	
	public String getAdditional() {
		return this.additional;
	}
	
	public String getPtblId() {
		return this.ptblId;
	}
	
	public String getStatus() {
		return this.status;
	}
}
