//Database: pos_table_settings - POS table setting
package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosTableSetting {
	private int tblsId;
	private int shopId;
	private int oletId;
	private int tblsTable;
	private String tblsTableExt;
	private int[] tblsCheckAttrAutoId;
	private String status;
	
	public PosTableSetting () {
		this.init();
	}
	
	public PosTableSetting (JSONObject tableSettingJSONObject) {
		this.readDataFromJson(tableSettingJSONObject);
	}

	public JSONArray readAllTableSettingsByShopOutlet (int iShopId, int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		try {
			requestJSONObject.put("shopId", iShopId);
			requestJSONObject.put("outletId", iOutletId);
		} catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataListFromApi("gm", "pos", "getTableSettingsByShopIdOutletId", requestJSONObject.toString());
	}

	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray tempJSONArray = null;

		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false)) {
			return null;
		} else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("tableSettings")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return null;
			}

			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("tableSettings")) {
				this.init();
				return null;
			}

			tempJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("tableSettings");
			if(tempJSONArray == null) {
				this.init();
				return null;
			}
		}
		
		return tempJSONArray;
	}
	
/*	//init object from database by shop_id, outlet_id, table, table_ext
	public void readTableSettingByShopOutletTable(int iShopId, int iOutletId, String sTable, String sTableExt) {
		JSONObject requestJSONObject = new JSONObject();
		this.init();
		
		try {
			requestJSONObject.put("shopId", iShopId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("table", sTable);
			requestJSONObject.put("tableExt", sTableExt);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		this.readDataFromApi("gm", "pos", "getTableSettingbyShopIdOutletIdTableTableExt", requestJSONObject.toString());
	}*/
	
	//init object with initialize value 
	public void init(){
		this.tblsId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.tblsTable = 0;
		this.tblsTableExt = "";
		if(this.tblsCheckAttrAutoId == null)
			this.tblsCheckAttrAutoId = new int[10];
		for (int i = 0; i < 10; i++)
			this.tblsCheckAttrAutoId[i] = 0;
		this.status = "";
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("tableSetting")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("tableSetting")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("tableSetting");
			if(tempJSONObject.isNull("PosTableSetting")) {
				this.init();
				return false;
			}
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject tableSettingJSONObject) {
		JSONObject resultTableSetting = null;
		int i;
		
		resultTableSetting = tableSettingJSONObject.optJSONObject("PosTableSetting");
		if(resultTableSetting == null)
			resultTableSetting = tableSettingJSONObject;
		
		this.init();
		this.tblsId = resultTableSetting.optInt("tbls_id");
		this.shopId = resultTableSetting.optInt("tbls_shop_id");
		this.oletId = resultTableSetting.optInt("tbls_olet_id");
		this.tblsTable = resultTableSetting.optInt("tbls_table");
		this.tblsTableExt = resultTableSetting.optString("tbls_table_ext", "");
		for(i=1; i<=10; i++){
			String sAttoField = "tbls_check_attr" + i + "_atto_id";
			this.tblsCheckAttrAutoId[(i-1)] = resultTableSetting.optInt(sAttoField);
		}
		this.status = resultTableSetting.optString("tbls_status", PosStation.STATUS_ACTIVE);
	}
	
	public int getTblsTable() {
		return this.tblsTable;
	}

	public String getTblsTableExt() {
		return this.tblsTableExt;
	}
	
	public int getTblsId() {
		return this.tblsId;
	}
	
	public int[] getTblsCheckAttrAutoId() {
		return tblsCheckAttrAutoId;
	}
}
