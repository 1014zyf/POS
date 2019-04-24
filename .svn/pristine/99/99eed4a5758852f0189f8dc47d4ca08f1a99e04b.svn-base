//Database: pos_check_tables - Sales check's table
package app.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosCheckTable {
	private int ctblId;
	private int chksId;
	private int table;
	private String tableExt;
	private int tableSize;
	private String additional;
	private int ptblId;
	
	// additional
	public static String ADDITIONAL_NO = "";
	public static String ADDITIONAL_YES = "y";

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
		this.ctblId = resultCheckTable.optInt("ctbl_id");
		this.chksId = resultCheckTable.optInt("ctbl_chks_id");
		this.table = resultCheckTable.optInt("ctbl_table");
		this.tableExt = resultCheckTable.optString("ctbl_table_ext");
		this.tableSize = resultCheckTable.optInt("ctbl_table_size");
		this.additional = resultCheckTable.optString("ctbl_additional", PosCheckTable.ADDITIONAL_NO);
		this.ptblId = resultCheckTable.optInt("ctbl_ptbl_id");
	}
	
	//construct the save request JSON
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("ctbl_id", this.ctblId);
			addSaveJSONObject.put("ctbl_chks_id", this.chksId);
			addSaveJSONObject.put("ctbl_table", this.table);
			addSaveJSONObject.put("ctbl_table_ext", this.tableExt);
			if(this.tableSize > 0)
				addSaveJSONObject.put("ctbl_table_size", this.tableSize);
			if(!this.additional.isEmpty())
				addSaveJSONObject.put("ctbl_additional", this.additional);
			if(this.ptblId > 0)
				addSaveJSONObject.put("ctbl_ptbl_id", this.ptblId);

		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	//read data from database with ctbl_chks_id
	public boolean readByCheckId (int iCtblId) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("checkId", Integer.toString(iCtblId));
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if(!this.readDataFromApi("gm", "pos", "getCheckTableByCheckId", requestJSONObject.toString()))
			return false;
		
		return true;
	}

	//add or update a check item to database
	public boolean addUpdate(boolean update, int iActionUserId, int iActionStationId) {
		JSONObject requestJSONObject = new JSONObject();
		
		requestJSONObject = this.constructAddSaveJSON(update);
		
		// Set action user id and station id
		try {
			requestJSONObject.put("actionUserId", iActionUserId);
			requestJSONObject.put("actionStationId", iActionStationId);
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
		this.ctblId = 0;
		this.chksId = 0;
		this.table = 0;
		this.tableExt = "";
		this.tableSize = 0;
		this.additional = PosCheckTable.ADDITIONAL_NO;
		this.ptblId = 0;
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
	
	public void setCheckId(int iCheckId) {
		this.chksId = iCheckId;
	}
	
	//get ctblId
	public int getCtblId() {
		return this.ctblId;
	}
	
	protected int getChksId() {
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
	
	public int getPtblId() {
		return this.ptblId;
	}
}
