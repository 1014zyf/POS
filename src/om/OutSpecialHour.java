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

public class OutSpecialHour {
	private int sphrId;
	private int shopId;
	private int oletId;
	private String[] name;
	private String[] shortName;
	private int seq;
	private int pdtpId;
	private Time officalStartTime;
	private Time officalEndTime;
	private String[] notice;
	private String[] greeting;
	private String autoStart;
	private String startReminder;
	private String endReminder;
	private DateTime startDate;
	private DateTime endDate;
	private String weekMask;
	private String holiday;
	private String dayBeforeHoliday;
	private String specialDay;
	private String dayBeforeSpecialDay;
	private String status;

	// autoStart
	public static String AUTO_START_NO = "";
	public static String AUTO_START_YES = "y";
	
	// startReminder
	public static String START_REMINDER_NO = "";
	public static String START_REMINDER_YES = "y";
	
	// endReminder
	public static String END_REMINDER_NO = "";
	public static String END_REMINDER_YES = "y";
	
	// holiday
	public static String HOLIDAY_NO_CHECKING = "";
	public static String HOLIDAY_APPLY = "y";
	public static String HOLIDAY_APPLY_WITH_WEEKMASK = "z";
	public static String HOLIDAY_NOT_APPLY = "n";
	public static String HOLIDAY_NOT_APPLY_WITH_WEEKMASK = "x";
	
	// dayBeforeHoliday
	public static String DAY_BEFORE_HOLIDAY_NO_CHECKING = "";
	public static String DAY_BEFORE_HOLIDAY_APPLY = "y";
	public static String DAY_BEFORE_HOLIDAY_APPLY_WITH_WEEKMASK = "z";
	public static String DAY_BEFORE_HOLIDAY_NOT_APPLY = "n";
	public static String DAY_BEFORE_HOLIDAY_NOT_APPLY_WITH_WEEKMASK = "x";
	
	// specialDay
	public static String SPECIAL_DAY_NO_CHECKING = "";
	public static String SPECIAL_DAY_APPLY = "y";
	public static String SPECIAL_DAY_APPLY_WITH_WEEKMASK = "z";
	public static String SPECIAL_DAY_NOT_APPLY = "z";
	public static String SPECIAL_DAY_NOT_APPLY_WITH_WEEKMASK = "x";
	
	// dayBeforeSpecialDay
	public static String DAY_BEFORE_SPECIAL_DAY_NO_CHECKING = "";
	public static String DAY_BEFORE_SPECIAL_DAY_APPLY = "y";
	public static String DAY_BEFORE_SPECIAL_DAY_APPLY_WITH_WEEKMASK = "z";
	public static String DAY_BEFORE_SPECIAL_DAY_NOT_APPLY = "n";
	public static String DAY_BEFORE_SPECIAL_DAY_NOT_APPLY_WITH_WEEKMASK = "x";
	
	// status
	public static String STATUS_ACTIVE= "";
	public static String STATUS_SUSPENDED = "s";
	public static String STATUS_DELETED = "d";
	
	//init object with initial value
	public OutSpecialHour() {
		this.init();
	}
	
	public OutSpecialHour(JSONObject specialHourJSONObject) {
		this.readDataFromJson(specialHourJSONObject);
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray outletTableJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("specialHours")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("specialHours")) 
				return null;
			
			outletTableJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("specialHours");
		}
		
		return outletTableJSONArray;
	}
	
	public JSONArray readByOutletId(int iOletId, JSONObject oCheckCriteria) {
		JSONObject requestJSONObject = null;
		JSONArray responseJSONArray = null;
		
		try {
			if(oCheckCriteria != null)
				requestJSONObject = oCheckCriteria;
			else
				requestJSONObject = new JSONObject();
			requestJSONObject.put("outletId", iOletId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "outlet", "getSpecialHourByOutlet", requestJSONObject.toString());
		
		return responseJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject specialHourJSONObject) {
		JSONObject resultSpecialHour = null;
		int i;
		SimpleDateFormat oTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		DateTimeFormatter oDateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		
		resultSpecialHour = specialHourJSONObject.optJSONObject("OutSpecialHour");
		if(resultSpecialHour == null)
			resultSpecialHour = specialHourJSONObject;
			
		this.init();

		this.sphrId = resultSpecialHour.optInt("sphr_id");
		this.shopId = resultSpecialHour.optInt("sphr_shop_id");
		this.oletId = resultSpecialHour.optInt("sphr_olet_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultSpecialHour.optString("sphr_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultSpecialHour.optString("sphr_short_name_l"+i);
		this.seq = resultSpecialHour.optInt("sphr_seq");
		this.pdtpId = resultSpecialHour.optInt("sphr_pdtp_id");
		try {
			String sOfficialStartTime = resultSpecialHour.optString("sphr_official_start_time");
			if(!sOfficialStartTime.isEmpty())
				this.officalStartTime = new Time(oTimeFormat.parse(sOfficialStartTime).getTime());
			
			String sOfficialEndTime = resultSpecialHour.optString("sphr_official_end_time");
			if(!sOfficialEndTime.isEmpty())
				this.officalEndTime = new Time(oTimeFormat.parse(sOfficialEndTime).getTime());
		}catch (ParseException parsee) {
			parsee.printStackTrace();
		}
		for(i=1; i<=5; i++) {
			this.notice[(i-1)] = resultSpecialHour.optString("sphr_notice_l"+i, null);
		}
		for(i=1; i<=5; i++) {
			this.greeting[(i-1)] = resultSpecialHour.optString("sphr_greeting_l"+i, null);
		}
		this.autoStart = resultSpecialHour.optString("sphr_auto_start", OutSpecialHour.AUTO_START_NO);
		this.startReminder = resultSpecialHour.optString("sphr_start_reminder", OutSpecialHour.START_REMINDER_NO);
		this.endReminder = resultSpecialHour.optString("sphr_end_reminder", OutSpecialHour.END_REMINDER_NO);
		try {
			String sStartDate = resultSpecialHour.optString("sphr_start_date");
			if(!sStartDate.isEmpty())
				this.startDate = oDateFormat.parseDateTime(sStartDate);
			
			String sEndDate = resultSpecialHour.optString("sphr_end_date");
			if(!sEndDate.isEmpty())
				this.endDate = oDateFormat.parseDateTime(sEndDate);
		}catch (Exception parsee) {
			parsee.printStackTrace();
		}
		this.weekMask = resultSpecialHour.optString("sphr_week_mask");
		this.holiday = resultSpecialHour.optString("sphr_holiday", OutSpecialHour.HOLIDAY_NO_CHECKING);
		this.dayBeforeHoliday = resultSpecialHour.optString("sphr_day_before_holiday", OutSpecialHour.DAY_BEFORE_HOLIDAY_NO_CHECKING);
		this.specialDay = resultSpecialHour.optString("sphr_special_day", OutSpecialHour.SPECIAL_DAY_NO_CHECKING);
		this.dayBeforeSpecialDay = resultSpecialHour.optString("sphr_day_before_special_day", OutSpecialHour.DAY_BEFORE_SPECIAL_DAY_NO_CHECKING);
		this.status = resultSpecialHour.optString("sphr_status", OutSpecialHour.STATUS_ACTIVE);
	}
	
	//init the object
	public void init() {
		int i=0;
		
		this.sphrId = 0;
		this.shopId = 0;
		this.oletId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = ""; 
		this.pdtpId = 0;
		this.officalStartTime = null;
		this.officalEndTime = null;
		if(this.notice == null)
			this.notice = new String[5];
		for(i=0; i<5; i++)
			this.notice[i] = null;
		if(this.greeting == null)
			this.greeting = new String[5];
		for(i=0; i<5; i++)
			this.greeting[i] = null;
		this.autoStart = OutSpecialHour.AUTO_START_NO;
		this.startReminder = OutSpecialHour.START_REMINDER_NO;
		this.endReminder = OutSpecialHour.END_REMINDER_NO;
		this.startDate = null;
		this.endDate = null;
		this.weekMask = "";
		this.holiday = OutSpecialHour.HOLIDAY_NO_CHECKING;
		this.dayBeforeHoliday = OutSpecialHour.DAY_BEFORE_HOLIDAY_NO_CHECKING;
		this.specialDay = OutSpecialHour.SPECIAL_DAY_NO_CHECKING;
		this.dayBeforeSpecialDay = OutSpecialHour.DAY_BEFORE_SPECIAL_DAY_NO_CHECKING;
		this.status = OutSpecialHour.STATUS_ACTIVE;
	}
	
	public int getSphrId() {
		return this.sphrId;
	}
	
	protected int getShopId() {
		return this.shopId;
	}
	
	protected int getOletId() {
		return this.oletId;
	}
	
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];		
	}
	
	public String[] getName() {
		return this.name;		
	}
	
	protected String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	protected int getSeq() {
		return this.seq;
	}
	
	protected int getPdtpId() {
		return this.pdtpId;
	}
	
	public Time getOfficalStartTime() {
		return this.officalStartTime;
	}
	
	public Time getOfficalEndTime() {
		return this.officalEndTime;
	}
	
	protected String getNotice(int iIndex) {
		return this.notice[(iIndex-1)];
	}
	
	protected String getGreeting(int iIndex) {
		return this.greeting[(iIndex-1)];
	}
	
	protected String getAutoStart() {
		return this.autoStart;
	}
	
	protected String getStartReminder() {
		return this.startReminder;
	}
	
	protected String getendReminder() {
		return this.endReminder;
	}
	
	protected DateTime getStartDate() {
		return this.startDate;
	}
	
	protected DateTime getEndDate() {
		return this.endDate;
	}
	
	protected String getWeekMask() {
		return this.weekMask;
	}
	
	protected String getHoliday() {
		return this.holiday;
	}
	
	protected String getDayBeforeHoliday() {
		return this.dayBeforeHoliday;
	}
	
	protected String getSpecialDay() {
		return this.specialDay;
	}
	
	protected String getDayBeforeSpecialDay() {
		return this.dayBeforeSpecialDay;
	}
	
	protected String getStatus() {
		return this.status;
	}
}
