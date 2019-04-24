package om;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;

public class OutPeriod {
	private int perdId;
	private int shopId;
	private int oletId;
	private String code;
	private String[] name;
	private String[] shortName;
	private int seq;
	private int pdtpId;
	private Time officialStartTime;
	private Time officialEndTime;
	private Time resvStartTime;
	private Time resvEndTime;
	private int resvInterval;
	private int flrpId;
	private String[] notice;
	private String[] greeting;
	private DateTime startDate;
	private DateTime endDate;
	private String weekMask;
	private String holiday;
	private String dayBeforeHoliday;
	private String specialDay;
	private String dayBeforeSpecialDay;
	private String status;

	// holiday
	static String HOLIDAY_NO_CHECKING = "";
	static String HOLIDAY_APPLY = "y";
	static String HOLIDAY_APPLY_WITH_WEEKMASK = "z";
	static String HOLIDAY_NOT_APPLY = "n";
	static String HOLIDAY_NOT_APPLY_WITH_WEEKMASK = "x";
	
	// dayBeforeHoliday
	static String DAY_BEFORE_HOLIDAY_NO_CHECKING = "";
	static String DAY_BEFORE_HOLIDAY_APPLY = "y";
	static String DAY_BEFORE_HOLIDAY_APPLY_WITH_WEEKMASK = "z";
	static String DAY_BEFORE_HOLIDAY_NOT_APPLY = "n";
	static String DAY_BEFORE_HOLIDAY_NOT_APPLY_WITH_WEEKMASK = "x";
	
	// specialDay
	static String SPECIAL_DAY_NO_CHECKING = "";
	static String SPECIAL_DAY_APPLY = "y";
	static String SPECIAL_DAY_APPLY_WITH_WEEKMASK = "z";
	static String SPECIAL_DAY_NOT_APPLY = "z";
	static String SPECIAL_DAY_NOT_APPLY_WITH_WEEKMASK = "x";
	
	// dayBeforeSpecialDay
	static String DAY_BEFORE_SPECIAL_DAY_NO_CHECKING = "";
	static String DAY_BEFORE_SPECIAL_DAY_APPLY = "y";
	static String DAY_BEFORE_SPECIAL_DAY_APPLY_WITH_WEEKMASK = "z";
	static String DAY_BEFORE_SPECIAL_DAY_NOT_APPLY = "n";
	static String DAY_BEFORE_SPECIAL_DAY_NOT_APPLY_WITH_WEEKMASK = "x";
	
	// status
	private static String STATUS_ACTIVE = "";
	private static String STATUS_DELETED = "d";
	
	//init object with initialized value
	public OutPeriod() {
		this.init();
	}

	public OutPeriod(int iShopId, int iOletId) {
		this.init();
		
		this.shopId = iShopId;
		this.oletId = iOletId;
	}
	
	//init object with JSON Object
	public OutPeriod(JSONObject oJSONObject) {
		readDataFromJson(oJSONObject);		
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject periodsJSONObject) {
		int i=0;
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		JSONObject tempJSONObject = null;
		
		tempJSONObject = periodsJSONObject.optJSONObject("OutPeriod");
		if(tempJSONObject == null)
			tempJSONObject = periodsJSONObject;
		
		this.init();
			
		this.perdId = tempJSONObject.optInt("perd_id");
		this.shopId = tempJSONObject.optInt("perd_shop_id");
		this.oletId = tempJSONObject.optInt("perd_olet_id");
		this.code = tempJSONObject.optString("perd_code");
		
		for(i=1; i<=5; i++)
			this.name[(i-1)] = tempJSONObject.optString("perd_name_l"+i);
		
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = tempJSONObject.optString("perd_short_name_l"+i);
		
		this.seq = tempJSONObject.optInt("perd_seq");
		this.pdtpId = tempJSONObject.optInt("perd_pdtp_id");

		String sOfficialStartTime = tempJSONObject.optString("perd_official_start_time");
		if(!sOfficialStartTime.isEmpty()) {
			try {
				this.officialStartTime = new Time(timeFormat.parse(sOfficialStartTime).getTime());
			} catch (ParseException e2) {
				e2.printStackTrace();
			}
		}
		
		String sOfficialEndTime = tempJSONObject.optString("perd_official_end_time");
		if(!sOfficialEndTime.isEmpty()) {
			try {
				this.officialEndTime = new Time(timeFormat.parse(sOfficialEndTime).getTime());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}

		String sResvStartTime = tempJSONObject.optString("perd_resv_start_time");
		if(!sResvStartTime.isEmpty()) {
			try {
				this.resvStartTime = new Time(timeFormat.parse(sResvStartTime).getTime());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}

		String sResvEndTime = tempJSONObject.optString("perd_resv_end_time");
		if(!sResvEndTime.isEmpty()) {
			try {
				this.resvEndTime = new Time(timeFormat.parse(sResvEndTime).getTime());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		
		this.resvInterval = tempJSONObject.optInt("perd_resv_interval");
		this.flrpId = tempJSONObject.optInt("perd_flrp_id");
		
		for(i=1; i<=5; i++) {
			this.notice[(i-1)] = tempJSONObject.optString("perd_notice_l"+i, null);
		}
		
		for(i=1; i<=5; i++) {
			this.greeting[(i-1)] = tempJSONObject.optString("perd_greeting_l"+i, null);
		}
		
		String sStartDate = tempJSONObject.optString("perd_start_date");
		if(!sStartDate.isEmpty()) {
			try {
				this.startDate = dateFormat.parseDateTime(sStartDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		String sEndDate = tempJSONObject.optString("perd_end_date");
		if(!sEndDate.isEmpty()) {
			try {
				this.endDate = dateFormat.parseDateTime(sEndDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		this.weekMask = tempJSONObject.optString("perd_week_mask");
		this.holiday = tempJSONObject.optString("perd_holiday");
		this.dayBeforeHoliday = tempJSONObject.optString("perd_day_before_holiday");
		this.specialDay = tempJSONObject.optString("perd_special_day");
		this.dayBeforeSpecialDay = tempJSONObject.optString("perd_day_before_special_day");
		this.status = tempJSONObject.optString("perd_status", OutPeriod.STATUS_ACTIVE);
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("period")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("period")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("period");
			if(tempJSONObject.isNull("OutPeriod")) {
				this.init();
				return false;
			}
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//get period list from database by shop id and outlet id
	public JSONArray readPeriodListByShopIdAndOutletId(int iShopId, int iOletId, boolean bGetDeletedRecord) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("shopId", Integer.toString(iShopId));
			requestJSONObject.put("outletId", Integer.toString(iOletId));
			if (bGetDeletedRecord)
				requestJSONObject.put("getDeleted", Integer.toString(1));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "outlet", "getPeriodListByShopOutlet", requestJSONObject.toString());
		
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("period")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("period")) 
				return null;
			
			displayPanelLookupJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("period");			
		}
		
		return displayPanelLookupJSONArray;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.perdId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.code = "";
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.pdtpId = 0;
		this.officialStartTime = null;
		this.officialEndTime = null;
		this.resvStartTime = null;
		this.resvEndTime = null;
		this.resvInterval = 0;
		this.flrpId = 0;
		if(this.notice == null)
			this.notice = new String[5];
		for(i=0; i<5; i++)
			this.notice[i] = null;
		if(this.greeting == null)
			this.greeting = new String[5];
		for(i=0; i<5; i++)
			this.greeting[i] = null;
		this.startDate = null;
		this.endDate = null;
		this.weekMask = "";
		this.holiday = OutPeriod.HOLIDAY_NO_CHECKING;
		this.dayBeforeHoliday = OutPeriod.DAY_BEFORE_HOLIDAY_NO_CHECKING;
		this.specialDay = OutPeriod.SPECIAL_DAY_NO_CHECKING;
		this.dayBeforeSpecialDay = OutPeriod.DAY_BEFORE_SPECIAL_DAY_NO_CHECKING;
		this.status = OutPeriod.STATUS_ACTIVE;
	}

	//read data from database by perd_id
	public boolean readById(int iPerdId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("id", Integer.toString(iPerdId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataFromApi("gm", "outlet", "getPeriodById", requestJSONObject.toString());
	}
	
	//get period id
	public int getPerdId(){
		return this.perdId;
	}
	
	public int getShopId() {
		return this.shopId;
	}
	
	public int getOletId() {
		return this.oletId;
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
	
	public int getSeq() {
		return this.seq;
	}

	public int getPeriodTypeId() {
		return this.pdtpId;
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
	
	public int getFloorPlanId() {
		return this.flrpId;
	}
	
	public String getNotice(int iIndex) {
		return this.notice[(iIndex-1)];
	}
	
	public String getGreeting(int iIndex) {
		return this.greeting[(iIndex-1)];
	}
	
	public DateTime getStartDate() {
		return this.startDate;
	}
	
	public DateTime getEndDate() {
		return this.endDate;
	}

	public String getWeekMask() {
		return this.weekMask;
	}
	
	public String getHoliday() {
		return this.holiday;
	}

	public String getDayBeforeHoliday() {
		return this.dayBeforeHoliday;
	}
	
	public String getSpecialDay() {
		return this.specialDay;
	}
	
	public String getDayBeforeSpecialDay() {
		return this.dayBeforeSpecialDay;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public boolean isDeleted() {
		return this.status.equals(OutPeriod.STATUS_DELETED);
	}
}
