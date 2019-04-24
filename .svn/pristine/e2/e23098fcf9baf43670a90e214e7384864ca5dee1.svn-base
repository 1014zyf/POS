//Database: pos_action_logs - Action logs
package om;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosActionLog {
	private String alogId;
	private String key;
	private int userId;
	private String actionTime;
	private DateTime actionLocalTime;
	private String actionResult;
	private String table;
	private int recordId;
	private int shopId;
	private int oletId;
	private String bdayId;
	private String bperId;
	private int statId;
	private String chksId;
	private String cptyId;
	private String citmId;
	private String cdisId;
	private String cpayId;
	private String remark;
	
	// actionResult
	public static String ACTION_RESULT_SUCCESS = "s";
	public static String ACTION_RESULT_ATTEMPT_ONLY = "a";
	public static String ACTION_RESULT_REJECTED = "r";
	public static String ACTION_RESULT_FAIL = "f";
	
	//init object with initialize value
	public PosActionLog() {
		this.init();
	}
	
	//init object from database by alog_id
	public PosActionLog(String sAlogId) {
		this.init();
		
		this.alogId = sAlogId;
	}
	
	//construct the save request JSON
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("alog_id", this.alogId);
			addSaveJSONObject.put("alog_key", this.key);
			addSaveJSONObject.put("alog_user_id", this.userId);
			addSaveJSONObject.put("alog_action_time", this.actionTime);
			addSaveJSONObject.put("alog_action_loctime", this.actionLocalTime.toString(oFormatter));
			addSaveJSONObject.put("alog_action_result", this.actionResult);
			if(this.table != null && !this.table.isEmpty())
				addSaveJSONObject.put("alog_table", this.table);
			if(this.recordId > 0)
				addSaveJSONObject.put("alog_record_id", this.recordId);
			addSaveJSONObject.put("alog_shop_id", this.shopId);
			addSaveJSONObject.put("alog_olet_id", this.oletId);
			addSaveJSONObject.put("alog_bday_id", this.bdayId);
			addSaveJSONObject.put("alog_bper_id", this.bperId);
			addSaveJSONObject.put("alog_stat_id", this.statId);
			addSaveJSONObject.put("alog_chks_id", this.chksId);
			if(this.cptyId.compareTo("") > 0)
				addSaveJSONObject.put("alog_cpty_id", this.cptyId);
			if(this.citmId.compareTo("") > 0)
				addSaveJSONObject.put("alog_citm_id", this.citmId);
			if(this.cdisId.compareTo("") > 0)
				addSaveJSONObject.put("alog_cdis_id", this.cdisId);
			if(this.cpayId.compareTo("") > 0)
				addSaveJSONObject.put("alog_cpay_id", this.cpayId);
			if(this.remark != null)
				addSaveJSONObject.put("alog_remark", this.remark);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("actionLog")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("actionLog")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("actionLog");
			if (tempJSONObject.isNull("PosActionLog")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject actionLogJSONObject) {
		JSONObject resultActionLog = null;
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		resultActionLog = actionLogJSONObject.optJSONObject("PosActionLog");
		if(resultActionLog == null)
			resultActionLog = actionLogJSONObject;
			
		this.init();
		
		this.alogId = resultActionLog.optString("alog_id");
		this.key = resultActionLog.optString("alog_key");
		this.userId = resultActionLog.optInt("alog_user_id");
		
		this.actionTime = resultActionLog.optString("alog_action_time", null);
		
		String sActionLocTime = resultActionLog.optString("alog_action_loctime");
		if(!sActionLocTime.isEmpty())
			this.actionLocalTime = oFormatter.parseDateTime(sActionLocTime);
		
		this.actionResult = resultActionLog.optString("alog_action_result");
		this.table = resultActionLog.optString("alog_table");
		this.recordId = resultActionLog.optInt("alog_record_id");
		this.shopId = resultActionLog.optInt("alog_shop_id");
		this.oletId = resultActionLog.optInt("alog_olet_id");
		this.bdayId = resultActionLog.optString("alog_bday_id");
		this.bperId = resultActionLog.optString("alog_bper_id");
		this.statId = resultActionLog.optInt("alog_stat_id");
		this.chksId = resultActionLog.optString("alog_chks_id");
		this.cptyId = resultActionLog.optString("alog_cpty_id");
		this.citmId = resultActionLog.optString("alog_citm_id");
		this.cdisId = resultActionLog.optString("alog_cdis_id");
		this.cpayId = resultActionLog.optString("alog_cpay_id");
		
		this.remark = resultActionLog.optString("alog_remark", null);
	}
	
	// init value
	public void init() {
		this.alogId = "";
		this.key = "";
		this.userId = 0;
		this.actionTime = null;
		this.actionLocalTime = null;
		this.actionResult = "";
		this.table = "";
		this.recordId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.bdayId = "";
		this.bperId = "";
		this.statId = 0;
		this.chksId = "";
		this.cptyId = "";
		this.citmId = "";
		this.cdisId = "";
		this.cpayId = "";
		this.remark = null;
	}
	
	//add or update print job with array list
	public boolean addUpdateWithMutlipleRecord(List<PosActionLog> oPosActionLogs) {
		JSONObject actionLogJSONObject = null;
		JSONArray actionLogJSONArray = new JSONArray();
		
		for(int i = 0; i < oPosActionLogs.size(); i++) {
			PosActionLog oActionLog = oPosActionLogs.get(i);
			if (oActionLog.getAlogId().compareTo("") == 0)
				actionLogJSONObject = oActionLog.constructAddSaveJSON(false);
			else
				actionLogJSONObject = oActionLog.constructAddSaveJSON(true);
			actionLogJSONArray.put(actionLogJSONObject);
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveMultiActionLogs", actionLogJSONArray.toString(), true))
			return false;
		else
			return true;
	}
	
	/******************************/
	/*	Getter and Setter Method  */
	/******************************/
	//get alogId
	protected String getAlogId() {
		return this.alogId;
	}

	protected String getKey() {
		return this.key;
	}
	
	protected int getUserId() {
		return this.userId;
	}
	
	protected String getActionTime() {
		return this.actionTime;
	}
	
	public DateTime getActionLocalTime() {
		return this.actionLocalTime;
	}
	
	protected String getActionResult() {
		return this.actionResult;
	}
	
	protected String getTable() {
		return this.table;
	}
	
	protected int getRecordId() {
		return this.recordId;
	}

	protected int getShopId() {
		return this.shopId;
	}
	
	protected int getOletId() {
		return this.oletId;
	}
	
	protected String getBdayId() {
		return this.bdayId;
	}
	
	protected String getBperId() {
		return this.bperId;
	}
	
	protected int getStatId() {
		return this.statId;
	}
	
	protected String getChksId() {
		return this.chksId;
	}
	
	protected String getCptyId() {
		return this.cptyId;
	}
	
	protected String getCitmId() {
		return this.citmId;
	}
	
	protected String getCdisId() {
		return this.cdisId;
	}
	
	protected String getCpayId() {
		return this.cpayId;
	}
	
	protected String getRemark() {
		return this.remark;
	}
	
	public void setKey(String sKey) {
		this.key = sKey;
	}
	
	public void setUserId(int iUserId) {
		this.userId = iUserId;
	}
	
	public void setActionTime(String sActionTime) {
		this.actionTime = sActionTime;
	}
	
	public void setActionLocalTime(DateTime oActionLocTime) {
		this.actionLocalTime = oActionLocTime;
	}
	
	public void setActionResult(String sActionResult) {
		this.actionResult = sActionResult;
	}
	
	public void setTable(String sTable) {
		this.table = sTable;
	}
	
	public void setRecordId(int iRecordId) {
		this.recordId = iRecordId;
	}
	
	public void setShopId(int iShopId) {
		this.shopId = iShopId;
	}
	
	public void setOletId(int iOletId) {
		this.oletId = iOletId;
	}
	
	public void setBdayId(String sBdayId) {
		this.bdayId = sBdayId;
	}
	
	public void setBperId(String sBperId) {
		this.bperId = sBperId;
	}
	
	public void setStatId(int iStatId) {
		this.statId = iStatId;
	}
	
	public void setChksId(String sChksId) {
		this.chksId = sChksId;
	}
	
	public void setCptyId(String sCptyId) {
		this.cptyId = sCptyId;
	}
	
	public void setCitmId(String sCitmId) {
		this.citmId = sCitmId;
	}
	
	public void setCdisId(String iCdisId) {
		this.cdisId = iCdisId;
	}
	
	public void setCpayId(String sCpayId) {
		this.cpayId = sCpayId;
	}
	
	public void setRemark(String sRemark) {
		this.remark = sRemark;
	}
	
}
