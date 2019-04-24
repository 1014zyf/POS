package om;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

public class PosUserTimeInOutLog {
	private String utioId;
	private String bdayId;
	private int shopId;
	private int oletId;
	private int userId;
	private String inTime;
	private DateTime inLocTime;
	private String outTime;
	private DateTime outLocTime;
	
	private int iTimeInOutCount;
	
	public PosUserTimeInOutLog() {
		this.init();
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject userTimeInOutLogJSONObject) {
		JSONObject resultUserTimeInOutLog = null;
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		resultUserTimeInOutLog = userTimeInOutLogJSONObject.optJSONObject("PosUserTimeInOutLog");
		if(resultUserTimeInOutLog == null)
			resultUserTimeInOutLog = userTimeInOutLogJSONObject;
			
		this.init();
		this.utioId = resultUserTimeInOutLog.optString("utio_id");
		this.bdayId = resultUserTimeInOutLog.optString("utio_bday_id");
		this.shopId = resultUserTimeInOutLog.optInt("utio_shop_id");
		this.oletId = resultUserTimeInOutLog.optInt("utio_olet_id");
		this.userId = resultUserTimeInOutLog.optInt("utio_user_id");
		this.inTime = resultUserTimeInOutLog.optString("utio_in_time", null);
		
		String sOpenLocTime = resultUserTimeInOutLog.optString("utio_in_loctime");
		if(!sOpenLocTime.isEmpty())
			this.inLocTime = oFmt.parseDateTime(sOpenLocTime);
		
		this.outTime = resultUserTimeInOutLog.optString("utio_out_time", null);
		
		String oCloseLocTime = resultUserTimeInOutLog.optString("utio_out_loctime");
		if(!oCloseLocTime.isEmpty())
			this.outLocTime = oFmt.parseDateTime(oCloseLocTime);
		
		this.iTimeInOutCount = userTimeInOutLogJSONObject.optInt("count");
	}
	
	//construct the save request JSON
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("utio_id", this.utioId);
			addSaveJSONObject.put("utio_bday_id", this.bdayId);
			addSaveJSONObject.put("utio_shop_id", this.shopId);
			addSaveJSONObject.put("utio_olet_id", this.oletId);
			addSaveJSONObject.put("utio_user_id", this.userId);
			if(this.inTime != null)
				addSaveJSONObject.put("utio_in_time", this.inTime);
			if(this.inLocTime != null)
				addSaveJSONObject.put("utio_in_loctime", dateTimeToString(this.inLocTime));
			if(this.outTime != null)
				addSaveJSONObject.put("utio_out_time", this.outTime);
			if(this.outLocTime != null)
				addSaveJSONObject.put("utio_out_loctime", dateTimeToString(this.outLocTime));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}

	public boolean addUpdate(boolean bUpdate) {
		JSONObject requestJSONObject = new JSONObject();
		
		requestJSONObject = this.constructAddSaveJSON(bUpdate);
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveUserTimeInOutLog", requestJSONObject.toString(), false))
			return false;
		else {
			//After insert record to database, update BusinessDay's bday_id
			if (!bUpdate) {
				if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("id"))
					return false;
				
				this.bdayId = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("id");
			}
			
			return true;
		}
	}
	
	public boolean readByUserId(String sBdayId, int iUserId) {
		JSONObject requestJSONObject = new JSONObject();
		boolean bResult = false;
		 
		try {
			requestJSONObject.put("businessDayId", sBdayId);
			requestJSONObject.put("userId", iUserId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		bResult = this.readDataFromApi("gm", "pos", "getUserTimeInOutLogByUserId", requestJSONObject.toString());
		
		return bResult;
	}
	
	public boolean printUserTimeOutSlip(String sBdayId, int iOletId, int iUserId, int iPrintQueueId, int iLangIndex) {
		JSONObject requestJSONObject = new JSONObject();
		boolean bResult = false;
		 
		try {
			requestJSONObject.put("businessDayId", sBdayId);
			requestJSONObject.put("outletId", iOletId);
			requestJSONObject.put("userId", iUserId);
			requestJSONObject.put("printQueueId", iPrintQueueId);
			requestJSONObject.put("langIndex", iLangIndex);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		bResult = this.readDataFromApi("gm", "pos", "printUserTimeOutSlip", requestJSONObject.toString());
		
		return bResult;
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("user_time_in_out_log")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("user_time_in_out_log")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("user_time_in_out_log");
			if(tempJSONObject.isNull("PosUserTimeInOutLog")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}
	
	//change DateTime object to string for database insertion/update
	public String dateTimeToString (DateTime oDateTime) {
		if (oDateTime == null)
			return null;
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		String timeString = fmt.print(oDateTime);

		return timeString;
	}
	
	//init value
	public void init() {
		utioId = "";
		bdayId = "";
		shopId = 0;
		oletId = 0;
		userId = 0;
		inTime = null;
		inLocTime = null;
		outTime = null;
		outLocTime = null;
		iTimeInOutCount = 0;
	}
	
	public void setBusinessDayId(String sBdayId) {
		this.bdayId = sBdayId;
	}
	
	public void setShopId(int iShopId) {
		this.shopId = iShopId;
	}
	
	public void setOutletId(int iOutletId) {
		this.oletId = iOutletId;
	}
	
	public void setUserId(int iUserId) {
		this.userId = iUserId;
	}
	
	public void setOpenTime(String sOpenTime) {
		this.inTime = sOpenTime;
	}
	
	public void setOpenLocTime(DateTime oOpenLocTime) {
		this.inLocTime = oOpenLocTime;
	}
	
	public void setCloseTime(String sCloseTime) {
		this.outTime = sCloseTime;
	}
	
	public void setCloseLocTime(DateTime oCloseLocTime) {
		this.outLocTime = oCloseLocTime;
	}
	
	public String getUtioId() {
		return this.utioId;
	}
	
	public String getBusinessDayId() {
		return this.bdayId;
	}
	
	public int getShopId() {
		return this.shopId;
	}
	
	public int getOutletId() {
		return this.oletId;
	}
	
	public int getUserId() {
		return this.userId;
	}
	
	public String getOpenTime() {
		return this.inTime;
	}
	
	public DateTime getOpenLocTime() {
		return this.inLocTime;
	}
	
	public String getCloseTime() {
		return this.outTime;
	}
	
	public DateTime getCloseLocTime() {
		return this.outLocTime;
	}
	
	public int getTimeInOutCount() {
		return this.iTimeInOutCount;
	}
}
