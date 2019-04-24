//Database: pos_business_periods - Running business periods on business day
package app.model;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosBusinessPeriod {
	private int bperId;
	private int bdayId;
	private int shopId;
	private int oletId;
	private int perdId;
	private String code;
	private String[] name;
	private String[] shortName;
	private Time officialStartTime;
	private Time officialEndTime;
	private Time resvStartTime;
	private Time resvEndTime;
	private int resvInterval;
	private int pdtpId;
	private int flrpId;
	private String[] notice;
	private String[] greeting;
	private String startTime;
	private DateTime startLocTime;
	private String endTime;
	private DateTime endLocTime;
	private String lastRecallTime;
	private DateTime lastRecallLocTime;
	private int startUserId;
	private int endUserId;
	private int recallUserId;
	private int recallCount;
	private String status;
	
	// status
	public static String STATUS_PERIOD_STOPPED = "";
	public static String STATUS_PERIOD_RUNNING = "r";
	public static String STATUS_PERIOD_DELETED = "d";

	//init object with initialize value
	public PosBusinessPeriod() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		this.init();
		this.startLocTime = new DateTime();
		this.startTime = fmt.print(startLocTime.withZone(DateTimeZone.UTC));
	}
	
	//init object with passing value
	public PosBusinessPeriod(int iBdayId, int iShopId, int iOletId, int iPerdId, String sCode, String sName1, String sName2, String sName3, String sName4, String sName5, String sShortName1, String sShortName2, String sShortName3, String sShortName4, String sShortName5, Time oOfficalStartTime, Time oOfficialEndTime, Time oResvStartTime, Time oResvEndTime, int oResvInterval, int iPdtpId, int iFlrpId, String sNotice1, String sNotice2, String sNotice3, String sNotice4, String sNotice5, String sGreeting1, String sGreeting2, String sGreeting3, String sGreeting4, String sGreeting5, String sStartTime, DateTime oStartLocalTime, String sEndTime, DateTime oEndLocalTime, String sRecallTime, DateTime oRecallLocalTime, int iStartUserId, int iEndUserId, int iRecallUserId, int iRecallCount) {
		this.init();
		
		this.bperId = 0;
		this.bdayId = iBdayId;
		this.shopId = iShopId;
		this.oletId = iOletId;
		this.perdId = iPerdId;
		this.code = sCode;
		this.name[1] = sName1;
		this.name[2] = sName2;
		this.name[3] = sName3;
		this.name[4] = sName4;
		this.name[5] = sName5;
		this.shortName[1] = sShortName1;
		this.shortName[2] = sShortName2;
		this.shortName[3] = sShortName3;
		this.shortName[4] = sShortName4;
		this.shortName[5] = sShortName5;
		this.officialStartTime = oOfficalStartTime;
		this.officialEndTime = oOfficialEndTime;
		this.resvStartTime = oResvStartTime;
		this.resvEndTime = oResvEndTime;
		this.resvInterval = oResvInterval;
		this.pdtpId = iPdtpId;
		this.flrpId = iFlrpId;
		this.notice[1] = sNotice1;
		this.notice[2] = sNotice2;
		this.notice[3] = sNotice3;
		this.notice[4] = sNotice4;
		this.notice[5] = sNotice5;
		this.greeting[1] = sGreeting1;
		this.greeting[2] = sGreeting2;
		this.greeting[3] = sGreeting3;
		this.greeting[4] = sGreeting4;
		this.greeting[5] = sGreeting5;
		this.startTime = sStartTime;
		this.startLocTime = oStartLocalTime;
		this.endTime = sEndTime;
		this.endLocTime = oEndLocalTime;
		this.lastRecallTime = sRecallTime;
		this.lastRecallLocTime = oRecallLocalTime;
		this.startUserId = iStartUserId;
		this.endUserId = iEndUserId;
		this.recallUserId = iRecallUserId;
		this.recallCount = iRecallCount;
		this.status = PosBusinessPeriod.STATUS_PERIOD_RUNNING;
		
	}

	//init object with JSON Object
	public PosBusinessPeriod(JSONObject periodsJSONObject) {
		this.readDataFromJson(periodsJSONObject);
	}
	
	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bResult = false;
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bResult = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("businessPeriod")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("businessPeriod")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("businessPeriod");
			if(tempJSONObject.isNull("PosBusinessPeriod")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		
			bResult = true;
			
		}
		
		return bResult;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject periodsJSONObject) {
		JSONObject resultBusinessPeriod = null;
		SimpleDateFormat oTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		int i;
		
		resultBusinessPeriod = periodsJSONObject.optJSONObject("PosBusinessPeriod");
		if(resultBusinessPeriod == null)
			resultBusinessPeriod = periodsJSONObject;
			
		this.init();
		this.bperId = resultBusinessPeriod.optInt("bper_id");
		this.bdayId = resultBusinessPeriod.optInt("bper_bday_id");
		this.shopId = resultBusinessPeriod.optInt("bper_shop_id");
		this.oletId = resultBusinessPeriod.optInt("bper_olet_id");
		this.perdId = resultBusinessPeriod.optInt("bper_perd_id");
		this.code = resultBusinessPeriod.optString("bper_code");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultBusinessPeriod.optString("bper_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultBusinessPeriod.optString("bper_short_name_l"+i);
		this.pdtpId = resultBusinessPeriod.optInt("bper_pdtp_id");
		this.flrpId = resultBusinessPeriod.optInt("bper_flrp_id");

		try {
			String sOfficialStartTime = resultBusinessPeriod.optString("bper_official_start_time");
			if(!sOfficialStartTime.isEmpty())
				this.officialStartTime = new Time(oTimeFormat.parse(sOfficialStartTime).getTime());
			
			String sOfficialEndTime = resultBusinessPeriod.optString("bper_official_end_time");
			if (!sOfficialEndTime.isEmpty())
				this.officialEndTime = new Time(oTimeFormat.parse(sOfficialEndTime).getTime());
			
			String sResvStartTime = resultBusinessPeriod.optString("bper_resv_start_time");
			if (!sResvStartTime.isEmpty())
				this.resvStartTime = new Time(oTimeFormat.parse(sResvStartTime).getTime());
			
			String sResvEndTime = resultBusinessPeriod.optString("bper_resv_end_time");
			if (!sResvEndTime.isEmpty())
				this.resvEndTime = new Time(oTimeFormat.parse(sResvEndTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		this.resvInterval = resultBusinessPeriod.optInt("bper_resv_interval");
		this.flrpId = resultBusinessPeriod.optInt("bper_flrp_id");
		
		for(i=1; i<=5; i++)
			this.notice[(i-1)] = resultBusinessPeriod.optString("bper_notice_l"+i, null);
		
		for(i=1; i<=5; i++)
			this.greeting[(i-1)] = resultBusinessPeriod.optString("bper_greeting_l"+i, null);
		
		this.startTime = resultBusinessPeriod.optString("bper_start_time", null);

		String sStartLocTime = resultBusinessPeriod.optString("bper_start_loctime");
		if (!sStartLocTime.isEmpty())
			this.startLocTime = new DateTime(oFmt.parseDateTime(sStartLocTime));
		
		this.endTime = resultBusinessPeriod.optString("bper_end_time", null);
		
		String sEndLocTime = resultBusinessPeriod.optString("bper_end_loctime");
		if (!sEndLocTime.isEmpty())
			this.endLocTime = new DateTime(oFmt.parseDateTime(sEndLocTime));
		
		this.lastRecallTime = resultBusinessPeriod.optString("bper_last_recall_time", null);
		
		String sLastRecallLocTime = resultBusinessPeriod.optString("bper_last_recall_loctime");
		if (!sLastRecallLocTime.isEmpty())
			this.lastRecallLocTime = new DateTime(oFmt.parseDateTime(sLastRecallLocTime));
		
		this.startUserId = resultBusinessPeriod.optInt("bper_start_user_id");
		this.endUserId = resultBusinessPeriod.optInt("bper_end_user_id");
		this.recallUserId = resultBusinessPeriod.optInt("bper_recall_user_id");
		this.recallCount = resultBusinessPeriod.optInt("bper_recall_count");
		this.status = resultBusinessPeriod.optString("bper_status", PosBusinessPeriod.STATUS_PERIOD_STOPPED);
	}

	//construct the save request JSON
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		int i=0;
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("bper_id", this.bperId);
			addSaveJSONObject.put("bper_bday_id", this.bdayId);
			addSaveJSONObject.put("bper_shop_id", this.shopId);
			addSaveJSONObject.put("bper_olet_id", this.oletId);
			addSaveJSONObject.put("bper_perd_id", this.perdId);
			addSaveJSONObject.put("bper_code", this.code);
			for(i=1; i<=5; i++) {
				if(!this.name[i-1].isEmpty())
					addSaveJSONObject.put("bper_name_l"+i, this.name[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(!this.shortName[i-1].isEmpty())
					addSaveJSONObject.put("bper_short_name_l"+i, this.shortName[(i-1)]);
			}
			addSaveJSONObject.put("bper_official_start_time", this.officialStartTime);
			addSaveJSONObject.put("bper_official_end_time", this.officialEndTime);
			addSaveJSONObject.put("bper_resv_start_time", this.resvStartTime);
			addSaveJSONObject.put("bper_resv_end_time", this.resvEndTime);
			addSaveJSONObject.put("bper_resv_interval", this.resvInterval);
			addSaveJSONObject.put("bper_pdtp_id", this.pdtpId);
			addSaveJSONObject.put("bper_flrp_id", this.flrpId);
			for(i=1; i<=5; i++) {
				if(this.notice[(i-1)] != null)
					addSaveJSONObject.put("bper_notice_l"+i, this.notice[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(this.greeting[(i-1)] != null)
					addSaveJSONObject.put("bper_greeting_l"+i, this.greeting[(i-1)]);
			}
			if (this.startTime != null)
				addSaveJSONObject.put("bper_start_time", this.startTime);
			
			if (this.startLocTime != null)
				addSaveJSONObject.put("bper_start_loctime", this.startLocTime.toString("yyyy-MM-dd HH:mm:ss"));
			
			if (this.endTime != null)
				addSaveJSONObject.put("bper_end_time", this.endTime);
			
			if (this.endLocTime != null)
				addSaveJSONObject.put("bper_end_loctime", this.endLocTime.toString("yyyy-MM-dd HH:mm:ss"));
			
			if(this.lastRecallTime != null)
				addSaveJSONObject.put("bper_last_recall_time", this.lastRecallTime);
			
			if (this.lastRecallLocTime != null)
				addSaveJSONObject.put("bper_last_recall_loctime", this.lastRecallLocTime.toString("yyyy-MM-dd HH:mm:ss"));
			
			if(this.startUserId > 0)
				addSaveJSONObject.put("bper_start_user_id", this.startUserId);
			
			addSaveJSONObject.put("bper_end_user_id", this.endUserId);
			
			if(this.recallUserId > 0)
				addSaveJSONObject.put("bper_recall_user_id", this.recallUserId);
			if(this.recallCount > 0)
				addSaveJSONObject.put("bper_recall_count", this.recallCount);
			addSaveJSONObject.put("bper_status", this.status);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}

	//add or update a check item to database
	public boolean addUpdate(boolean bUpdate, int iIsDailyStart) {
		JSONObject requestJSONObject = new JSONObject();
		
		requestJSONObject = this.constructAddSaveJSON(bUpdate);
		if (bUpdate) {
			try {
				requestJSONObject.put("daily_start", iIsDailyStart);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveBusinessPeriod", requestJSONObject.toString(), false))
			return false;			
		else
			return true;
	}

	// init value
	public void init() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		int i=0;
		
		this.bperId = 0;
		this.bdayId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.perdId = 0;
		this.code = "";
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.officialStartTime = null;
		this.officialEndTime = null;
		this.resvStartTime = null;
		this.resvEndTime = null;
		this.resvInterval = 0;
		this.pdtpId = 0;
		this.flrpId = 0;
		if(this.notice == null)
			this.notice = new String[5];
		for(i=0; i<5; i++)
			this.notice[i] = null;
		if(this.greeting == null)
			this.greeting = new String[5];
		for(i=0; i<5; i++)
			this.greeting[i] = null;
		this.startLocTime = new DateTime();
		this.startTime = fmt.print(startLocTime.withZone(DateTimeZone.UTC));
		this.endTime = null;
		this.endLocTime = null;
		this.lastRecallTime = null;
		this.lastRecallLocTime = null;
		this.startUserId = 0;
		this.endUserId = 0;
		this.recallUserId = 0;
		this.recallCount = 0;
		this.status = PosBusinessPeriod.STATUS_PERIOD_STOPPED;
	}
	
	//read object data from database by bper_id
	public boolean readById(int iBperId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("id", iBperId);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataFromApi("gm", "pos", "getBusinessPeriodById", requestJSONObject.toString());
	}

	//get check item lists from database by business day id and outlet id
	public JSONArray getBusinessPeriodListByBdayIdOutletId(int iBdayId, int iOletId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("businessDayId", iBdayId);
			requestJSONObject.put("outletId", Integer.toString(iOletId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getBusinessPeriodListByBdayIdOutletId", requestJSONObject.toString());
		
		return responseJSONArray;
		
	}

	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray displayPanelLookupJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;

			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("businessPeriods")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("businessPeriods")) 
				return null;
			
			displayPanelLookupJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("businessPeriods");
		}
		
		return displayPanelLookupJSONArray;
	}
	
	//get bperId
	public int getBperId() {
		return this.bperId;
	}
	
	public int getBdayId() {
		return this.bdayId;
	}
	
	public int getBperShopId() {
		return this.shopId;
	}
	
	public int getBperOletId() {
		return this.oletId;
	}
	
	public int getPeriodId() {
		return this.perdId;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	public Time getOfficialStartTime() {
		return this.officialStartTime;
	}
	
	public Time getOfficialEndTime() {
		return this.officialEndTime;
	}
	
	public Time getResvStartTime() {
		return this.resvStartTime;
	}
	public Time getResvEndTime() {
		return this.resvEndTime;
	}
	
	public int getResvInterval() {
		return this.resvInterval;
	}
	
	public int getPeriodTypeId() {
		return this.pdtpId;
	}
	
	public int getFloorPlanId() {
		return this.flrpId;
	}
	
	public String getStartTime() {
		return this.startTime;
	}
	
	public DateTime getStartLocTime() {
		return this.startLocTime;
	}
	
	public String getEndTime() {
		return this.endTime;
	}
	
	public DateTime getEndLocTime() {
		return this.endLocTime;
	}
	
	public String getNotice(int iIndex) {
		return this.notice[(iIndex-1)];
	}

	public String getGreeting(int iIndex) {
		return this.greeting[(iIndex-1)];
	}
	
	public int getStartUserId() {
		return this.startUserId;
	}
	
	public int getEndUserId() {
		return this.endUserId;
	}
	
	public int getRecallCount() {
		return this.recallCount;
	}
	
	public int getRecallUserId() {
		return this.recallUserId;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setBperId(int iBperId) {
		this.bperId = iBperId;
	}
	
	public void setBdayId(int iBdayId) {
		this.bdayId = iBdayId;
	}
	
	public void setShopId(int iShopId) {
		this.shopId = iShopId;
	}
	
	public void setOutletId(int iOletId) {
		this.oletId = iOletId;
	}
	
	public void setPerdId(int iPerdId) {
		this.perdId = iPerdId;
	}
	
	public void setCode(String sCode) {
		this.code = sCode;
	}
	
	public void setName(int iIndex, String sName) {
		this.name[(iIndex-1)] = sName;
	}
	
	public void setShortName(int iIndex, String sShortName) {
		this.shortName[(iIndex-1)] = sShortName;
	}
	
	public void setOfficialStartTime(Time oOfficialStartTime) {
		this.officialStartTime = oOfficialStartTime;
	}
	
	public void setOfficialEndTime(Time oOfficialEndTime) {
		this.officialEndTime = oOfficialEndTime;
	}
	
	public void setResvStartTime(Time oResvStartTime) {
		this.resvStartTime = oResvStartTime;
	}
	
	public void setResvEndTime(Time oResvEndTime) {
		this.resvEndTime = oResvEndTime;
	}
	
	public void setResvInterval(int iResvInterval) {
		this.resvInterval = iResvInterval;
	}
	
	public void setPdtpId(int iPdtpId) {
		this.pdtpId = iPdtpId;
	}
	
	public void setFlrpId(int iFlrpId) {
		this.flrpId = iFlrpId;
	}
	
	public void setNotice(int iIndex, String sNotice) {
		this.notice[(iIndex-1)] = sNotice;
	}
	
	public void setGreeting(int iIndex, String sGreeting) {
		this.greeting[(iIndex-1)] = sGreeting;
	}
	
	public void setStartTime(String sStartTime) {
		this.startTime = sStartTime;
	}
	
	public void setStartLocTime(DateTime oStartLocTime) {
		this.startLocTime = oStartLocTime;
	}
	
	public void setEndTime(String sEndTime) {
		this.endTime = sEndTime;
	}
	
	public void setEndLocTime(DateTime oEndLocTime) {
		this.endLocTime = oEndLocTime;
	}
	
	public void setLastRecallTime(String sLastRecallTime) {
		this.lastRecallTime = sLastRecallTime;
	}
	
	public void setLastRecallLocTime(DateTime oLastRecallLocTime) {
		this.lastRecallLocTime = oLastRecallLocTime;
	}
	
	public void setStartUserId(int iStartUserId) {
		this.startUserId = iStartUserId;
	}
	
	public void setEndUserId(int iEndUserId) {
		this.endUserId = iEndUserId;
	}
	
	public void setRecallUserId(int iRecallUserId) {
		this.recallUserId = iRecallUserId;
	}
	
	public void setRecallCount(int iRecallCount) {
		this.recallCount = iRecallCount;
	}
	
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
}
