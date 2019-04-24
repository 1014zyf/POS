//Database: pos_business_days - Sales business day
package app.model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PosBusinessDay {
	private int bdayId;
	private int shopId;
	private int oletId;
	private Date date;
	private int week;
	private int dayOfWeek;
	private int holidayCaldId;
	private int dayBeforeHolidayCaldId;
	private int specialDayCaldId;
	private int dayBeforeSpecialDayCaldId;
	private String startTime;
	private DateTime startLocalTime;
	private String endTime;
	private DateTime endLocalTime;
	private String lastRecallTime;
	private DateTime lastRecallLocalTime;
	private int startUserId;
	private int endUserId;
	private int recallUserId;
	private int recallCount;
	private int priceLevel;
	private BigDecimal [] sc;
	private BigDecimal [] tax;
	private String itemRound;
	private String scRound;
	private String taxRound;
	private String discRound;
	private String checkRound;
	private String payRound;
	private int itemDecimal;
	private int scDecimal;
	private int taxDecimal;
	private int discDecimal;
	private int checkDecimal;
	private int payDecimal;
	private String [] taxScIncludeTaxSCMask;
	private String taxScIncludePreDisc;
	private String taxIncludeMidDisc;
	private String checkPrefix;
	private int startCheckNum;
	private int endCheckNum;
	private int nextCheckNum;
	private String generateCheckNumBy;
	private int sphrId;
	private String status;
	
	private OutCalendarList calendarList;
	
	// generate check number by
	private static String GENERATE_CHECK_NUM_BY_OUTLET = "";
	private static String GENERATE_CHECK_NUM_BY_STATION = "g";
	
	// status
	public static String STATUS_NOT_RUNNING = "";
	public static String STATUS_RUNNING = "r";
	
	//init object with initialize value
	public PosBusinessDay() {
		this.init();
	}

	//init object with initialize value
	public PosBusinessDay(int iOletId, int iShopId, int iStartUserId) {
		DateTime today = new DateTime();
		Date todayDate = new Date();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		this.init();
		
		this.shopId = iShopId;
		this.oletId = iOletId;
		this.date = todayDate;
		this.week = today.getWeekOfWeekyear();
		if(today.getDayOfWeek() == 7)
			this.dayOfWeek = 0;
		else
			this.dayOfWeek = today.getDayOfWeek();
		this.startTime = fmt.print(today.withZone(DateTimeZone.UTC));
		this.startLocalTime = today;
		this.startUserId = iStartUserId;
		this.status = PosBusinessDay.STATUS_RUNNING;
	}
	
	//init object with initialize value
	public PosBusinessDay(JSONObject oBusinessDayJSONObject) {
		this.readDataFromJson(oBusinessDayJSONObject);
	}
	
	//construct the save request JSON
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("bday_id", this.bdayId);
			addSaveJSONObject.put("bday_shop_id", this.shopId);
			addSaveJSONObject.put("bday_olet_id", this.oletId);
			
			if (this.date != null)
				addSaveJSONObject.put("bday_date", dateFormat.format(this.date));
			
			addSaveJSONObject.put("bday_week", this.week);
			addSaveJSONObject.put("bday_day_of_week", this.dayOfWeek);
			if(this.holidayCaldId > 0)
				addSaveJSONObject.put("bday_holiday_cald_id", this.holidayCaldId);
			if(this.dayBeforeHolidayCaldId > 0)
				addSaveJSONObject.put("bday_day_before_holiday_cald_id", this.dayBeforeHolidayCaldId);
			if(this.specialDayCaldId > 0)
				addSaveJSONObject.put("bday_special_day_cald_id", this.specialDayCaldId);
			if(this.dayBeforeSpecialDayCaldId > 0)
				addSaveJSONObject.put("bday_day_before_special_day_cald_id", this.dayBeforeSpecialDayCaldId);
			
			if (this.startTime != null)
				addSaveJSONObject.put("bday_start_time", this.startTime);
			
			if (this.startLocalTime != null)
				addSaveJSONObject.put("bday_start_loctime", this.startLocalTime.toString("yyyy-MM-dd HH:mm:ss"));
			
			if (this.endTime != null)
				addSaveJSONObject.put("bday_end_time", this.endTime);
			
			if (this.endLocalTime != null)
				addSaveJSONObject.put("bday_end_loctime", this.endLocalTime.toString("yyyy-MM-dd HH:mm:ss"));
			else
				addSaveJSONObject.put("bday_end_loctime", "");
			
			if (this.lastRecallTime != null)
				addSaveJSONObject.put("bday_last_recall_time", this.lastRecallTime);
			
			if (this.lastRecallLocalTime != null)
				addSaveJSONObject.put("bday_last_recall_loctime", this.lastRecallLocalTime.toString("yyyy-MM-dd HH:mm:ss"));
			
			if(this.startUserId > 0)
				addSaveJSONObject.put("bday_start_user_id", this.startUserId);
			
			addSaveJSONObject.put("bday_end_user_id", this.endUserId);
			
			if(this.recallUserId > 0)
				addSaveJSONObject.put("bday_recall_user_id", this.recallUserId);
			
			if(this.recallCount > 0)
				addSaveJSONObject.put("bday_recall_count", this.recallCount);
			
			addSaveJSONObject.put("bday_price_level", this.priceLevel);
			
			for(int i=1; i<=5; i++)
				addSaveJSONObject.put("bday_sc"+i+"_rate", this.sc[i-1]);
			
			for(int i=1; i<=25; i++)
				addSaveJSONObject.put("bday_tax"+i+"_rate", this.tax[i-1]);
			
			addSaveJSONObject.put("bday_item_round", this.itemRound);
			addSaveJSONObject.put("bday_sc_round", this.scRound);
			addSaveJSONObject.put("bday_tax_round", this.taxRound);
			addSaveJSONObject.put("bday_disc_round", this.discRound);
			addSaveJSONObject.put("bday_check_round", this.checkRound);
			addSaveJSONObject.put("bday_pay_round", this.payRound);
			addSaveJSONObject.put("bday_item_decimal", this.itemDecimal);
			addSaveJSONObject.put("bday_sc_decimal", this.scDecimal);
			addSaveJSONObject.put("bday_tax_decimal", this.taxDecimal);
			addSaveJSONObject.put("bday_disc_decimal", this.discDecimal);
			addSaveJSONObject.put("bday_check_decimal", this.checkDecimal);
			addSaveJSONObject.put("bday_pay_decimal", this.payDecimal);
			
			for(int i=1; i<=30; i++) {
				addSaveJSONObject.put("bday_tax_sc_include_tax_sc_mask"+i, this.taxScIncludeTaxSCMask[i-1]);
			}
			
			addSaveJSONObject.put("bday_tax_sc_include_pre_disc", this.taxScIncludePreDisc);
			
			addSaveJSONObject.put("bday_tax_include_mid_disc", this.taxIncludeMidDisc);
			addSaveJSONObject.put("bday_generate_check_num_by", this.generateCheckNumBy);
			addSaveJSONObject.put("bday_check_prefix", this.checkPrefix);
			if(this.startCheckNum > 0)
				addSaveJSONObject.put("bday_start_check_num", this.startCheckNum);
			if(this.endCheckNum > 0)
				addSaveJSONObject.put("bday_end_check_num", this.endCheckNum);
			if(this.nextCheckNum > 0)
				addSaveJSONObject.put("bday_next_check_num", this.nextCheckNum);
			if(this.sphrId > 0)
				addSaveJSONObject.put("bday_sphr_id", this.sphrId);
			addSaveJSONObject.put("bday_status", this.status);
			
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("businessDay")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("businessDay")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("businessDay");
			if(tempJSONObject.isNull("PosBusinessDay")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}
	
	//read a list of data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray businessDayJSONArray = new JSONArray();
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))			
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("businessDays")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("businessDays")) 
				return null;
			
			businessDayJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("businessDays");
		}
		
		return businessDayJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject businessDayJSONObject) {
		JSONObject resultBusinessDay = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		int i;
		
		resultBusinessDay = businessDayJSONObject.optJSONObject("PosBusinessDay");
		if(resultBusinessDay == null)
			resultBusinessDay = businessDayJSONObject;
			
		this.init();
		this.bdayId = resultBusinessDay.optInt("bday_id");
		this.shopId = resultBusinessDay.optInt("bday_shop_id");
		this.oletId = resultBusinessDay.optInt("bday_olet_id");
		
		String sDate = resultBusinessDay.optString("bday_date");
		if(!sDate.isEmpty()) {
			try {
				this.date = dateFormat.parse(sDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		this.week = resultBusinessDay.optInt("bday_week");
		this.dayOfWeek = resultBusinessDay.optInt("bday_day_of_week");
		this.holidayCaldId = resultBusinessDay.optInt("bday_holiday_cald_id");
		this.dayBeforeHolidayCaldId = resultBusinessDay.optInt("bday_day_before_holiday_cald_id");
		this.specialDayCaldId = resultBusinessDay.optInt("bday_special_day_cald_id");
		this.dayBeforeSpecialDayCaldId = resultBusinessDay.optInt("bday_day_before_special_day_cald_id");
		
		this.startTime = resultBusinessDay.optString("bday_start_time", null);
		
		String sStartLocalTime = resultBusinessDay.optString("bday_start_loctime");
		if(!sStartLocalTime.isEmpty())
			this.startLocalTime = dateTimeFormat.parseDateTime(sStartLocalTime);
		
		this.endTime = resultBusinessDay.optString("bday_end_time", null);
		
		String sEndLocalTime = resultBusinessDay.optString("bday_end_loctime");
		if(!sEndLocalTime.isEmpty())
			this.endLocalTime = dateTimeFormat.parseDateTime(sEndLocalTime);
		
		this.lastRecallTime = resultBusinessDay.optString("bday_last_recall_time", null);
		
		String sLastRecallLocalTime = resultBusinessDay.optString("bday_last_recall_loctime");
		if(!sLastRecallLocalTime.isEmpty())
			this.lastRecallLocalTime = dateTimeFormat.parseDateTime(sLastRecallLocalTime);
		
		this.startUserId = resultBusinessDay.optInt("bday_start_user_id");
		this.endUserId = resultBusinessDay.optInt("bday_end_user_id");
		this.recallUserId = resultBusinessDay.optInt("bday_recall_user_id");
		this.recallCount = resultBusinessDay.optInt("bday_recall_count");
		this.priceLevel = resultBusinessDay.optInt("bday_price_level");
		for(i=1; i<=5; i++)
			this.sc[i-1] = new BigDecimal(resultBusinessDay.optString("bday_sc"+i+"_rate", "0.0"));
		for(i=1; i<=25; i++)
			this.tax[i-1] = new BigDecimal(resultBusinessDay.optString("bday_tax"+i+"_rate", "0.0"));
		this.itemRound = resultBusinessDay.optString("bday_item_round");
		this.scRound = resultBusinessDay.optString("bday_sc_round");
		this.taxRound = resultBusinessDay.optString("bday_tax_round");
		this.discRound = resultBusinessDay.optString("bday_disc_round");
		this.checkRound = resultBusinessDay.optString("bday_check_round");
		this.payRound = resultBusinessDay.optString("bday_pay_round");
		this.itemDecimal = resultBusinessDay.optInt("bday_item_decimal");
		this.scDecimal = resultBusinessDay.optInt("bday_sc_decimal");
		this.taxDecimal = resultBusinessDay.optInt("bday_tax_decimal");
		this.discDecimal = resultBusinessDay.optInt("bday_disc_decimal");
		this.checkDecimal = resultBusinessDay.optInt("bday_check_decimal");
		this.payDecimal = resultBusinessDay.optInt("bday_pay_decimal");
		for(i=1; i<=30; i++)
			this.taxScIncludeTaxSCMask[i-1] = resultBusinessDay.optString("bday_tax_sc_include_tax_sc_mask"+i);
		this.taxScIncludePreDisc = resultBusinessDay.optString("bday_tax_sc_include_pre_disc");
		this.taxIncludeMidDisc = resultBusinessDay.optString("bday_tax_include_mid_disc");
		this.generateCheckNumBy = resultBusinessDay.optString("bday_generate_check_num_by");
		this.checkPrefix = resultBusinessDay.optString("bday_check_prefix");
		this.startCheckNum = resultBusinessDay.optInt("bday_start_check_num");
		this.endCheckNum = resultBusinessDay.optInt("bday_end_check_num");
		this.nextCheckNum = resultBusinessDay.optInt("bday_next_check_num");
		this.sphrId = resultBusinessDay.optInt("bday_sphr_id");
		this.status = resultBusinessDay.optString("bday_status", PosBusinessDay.STATUS_NOT_RUNNING);

		calendarList.readCalendarListByShopOutletDate(this.shopId, this.oletId, dateFormat.format(this.date));
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
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveBusinessDay", requestJSONObject.toString(), false))
			return false;
		else {
			//After insert record to database, update BusinessDay's bday_id
			if (!bUpdate) {
				if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null || !OmWsClientGlobal.g_oWsClient.get().getResponse().has("id")) {
					OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
					return false;
				}
				
				this.bdayId = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("id");
			}
			
			return true;
		}
	}

	// init
	public void init() {
		this.bdayId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.date = null;
		this.week = 0;
		this.dayOfWeek = 0;
		this.holidayCaldId = 0;
		this.dayBeforeHolidayCaldId = 0;
		this.specialDayCaldId = 0;
		this.dayBeforeSpecialDayCaldId = 0;
		this.startTime = null;
		this.startLocalTime = null;
		this.endTime = null;
		this.endLocalTime = null;
		this.lastRecallTime = null;
		this.lastRecallTime = null;
		this.startUserId = 0;
		this.endUserId = 0;
		this.recallUserId = 0;
		this.recallCount = 0;
		this.priceLevel = 0;
		if(this.sc == null)
			this.sc = new BigDecimal[5];
		for(int i=0; i<5; i++)
			this.sc[i] = BigDecimal.ZERO;
		if(this.tax == null)
			this.tax = new BigDecimal[25];
		for(int i=0; i<25; i++)
			this.tax[i] = BigDecimal.ZERO;
		this.itemRound = "";
		this.scRound = "";
		this.taxRound = "";
		this.discRound = "";
		this.checkRound = "";
		this.payRound = "";
		this.itemDecimal = 0;
		this.scDecimal = 0;
		this.taxDecimal = 0;
		this.discDecimal = 0;
		this.checkDecimal = 0;
		this.payDecimal = 0;
		if(this.taxScIncludeTaxSCMask == null)
			this.taxScIncludeTaxSCMask = new String[30];
		for(int i=0; i<30; i++)
			this.taxScIncludeTaxSCMask[i] = "";
		this.taxScIncludePreDisc = "";
		this.taxIncludeMidDisc = "";
		this.generateCheckNumBy = "";
		this.checkPrefix = "";
		this.startCheckNum = 0;
		this.endCheckNum = 0;
		this.nextCheckNum = 0;
		this.sphrId = 0;
		this.status = PosBusinessDay.STATUS_NOT_RUNNING;
		
		if(this.calendarList == null)
			calendarList = new OutCalendarList();
		else
			this.calendarList.clearCalendarList();
	}
	
	//read the object form database by bday_id
	public boolean readById(int iBdayId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("id", iBdayId);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataFromApi("gm", "pos", "getBusinessDayById", requestJSONObject.toString());		
	}
	
	//read the object form database by bday_olet_id
	public boolean readByOutletId(int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataFromApi("gm", "pos", "getBusinessDayByOutletId", requestJSONObject.toString());
		
	}
	
	// Check if there is running business day or business day can be recalled or not
	// Return value 			=	0	# There is running business day
	//								1	# No running business day and not allow recall
	//								2	# No running business day and MUST recall (same business day)
	//								3	# No running business day and within the yesterday last period, allow user to select if recall or not
	public int dailyStartBusinessDayPreChecking(int iOutletId){
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "dailyStartBusinessDayPreChecking", requestJSONObject.toString(), false))
			return 1;
		else {
			JSONObject oResponseJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse();
			if (oResponseJSONObject == null || !oResponseJSONObject.has("checkBusinessDayResult"))
				// Have running business day
				return 0;
			
			// Get check business day result
			// if not found "checkBusinessDayResult", return 1 (no running business day and not allow recall
			return oResponseJSONObject.optInt("checkBusinessDayResult", 1);
		}
	}
	
	//read the object form database by bday_olet_id
	public JSONObject dailyStart(int iOutletId, int iUserId, boolean bRecallLastBusinessDay) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("userId", iUserId);
			requestJSONObject.put("userRecallBusinessDay", bRecallLastBusinessDay);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "dailyStart", requestJSONObject.toString(), false))
			return null;
		else {
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("businessDay")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
				
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
		}
	}

	//read the object form database by bday_olet_id
	public JSONObject dailyClose(int iOutletId, int iStationId, int iUserId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("stationId", iStationId);
			requestJSONObject.put("userId", iUserId);
			requestJSONObject.put("stationId", iStationId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "dailyClose", requestJSONObject.toString(), false))
			return null;
		else {
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("businessDay")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
				
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
			
		}
	}
	
	//read the object form database by bday_olet_id
	public JSONObject reloadBusinessSetting(int iBusinessDayId, int iUserId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("businessDayId", iBusinessDayId);
			requestJSONObject.put("userId", iUserId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "reloadBusinessSetting", requestJSONObject.toString(), false))
			return null;
		else {
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("businessDay")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
				
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
			
		}
	}

	//read the object form database by bday_date, bday_olet_id
	public boolean readByDateOutletId(String sDate, int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("date", sDate);
			requestJSONObject.put("outletId", iOutletId);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataFromApi("gm", "pos", "getBusinessDayByDateOutletId", requestJSONObject.toString());
		
	}
	
	//read the object form database by bday_olet_id
	public boolean readLastBusinessDayByOutlet(int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataFromApi("gm", "pos", "getLastBusinessDayByDateOutletId", requestJSONObject.toString());
		
	}
	
	//read the object form database by bdat_olet_id, a period of time
	public JSONArray readListByOutletWithPeriodOfTime(int iOutletId, String sStartDate, String sEndDate) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("startDate", sStartDate);
			requestJSONObject.put("endDate", sEndDate);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getBusinessDaysByOutletAndTimeOfPeriod", requestJSONObject.toString());
		
		return responseJSONArray;
	}
	
	public boolean isHoliday() {
		if (this.holidayCaldId > 0)
			return true;
		else
			return false;
	}
	
	public boolean isDayBeforeHoliday() {
		if (this.dayBeforeHolidayCaldId > 0)
			return true;
		else
			return false;
	}
	
	public boolean isSpecialDay() {
		if (this.specialDayCaldId > 0)
			return true;
		else
			return false;
	}
	
	public boolean isCheckNumGeneratedByOutlet() {
		if (this.generateCheckNumBy.compareTo(GENERATE_CHECK_NUM_BY_OUTLET) == 0)
			return true;
		else
			return false;
	}
	
	public boolean isDayBeforeSpecialDay() {
		if (this.dayBeforeSpecialDayCaldId > 0)
			return true;
		else
			return false;
	}
	
	public void setOutletId(int iOletId) {
		this.oletId = iOletId;
	}
	
	public void setShopId(int iShopId) {
		this.shopId = iShopId;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setHolidayCaldId(int iHolidayCaldId) {
		this.holidayCaldId = iHolidayCaldId;
	}
	
	public void setDayBeforeHolidayCaldId(int iDayBeforeHolidayCaldId) {
		this.dayBeforeHolidayCaldId = iDayBeforeHolidayCaldId;
	}
	
	public void setSpecialCaldId(int iSpecialCaldId) {
		this.specialDayCaldId = iSpecialCaldId;
	}
	
	public void setDayBeforeSpecialCaldId(int iDayBeforeSpecialCaldId) {
		this.dayBeforeSpecialDayCaldId = iDayBeforeSpecialCaldId;
	}
	
	public void setStartTime(String sStartTime) {
		this.startTime = sStartTime;
	}
	
	public void setStartLocalTime(DateTime oStartLocalTime) {
		this.startLocalTime = new DateTime(oStartLocalTime);
	}
	
	public void setEndTime(String sEndTime) {
		this.endTime = sEndTime;
	}
	
	public void setEndLocalTime(DateTime oEndLocalTime) {
		this.endLocalTime = oEndLocalTime;
	}
	
	public void setStartUserId(int iStartUserId) {
		this.startUserId = iStartUserId;
	}
	
	public void setEndUserId(int iEndUserId) {
		this.endUserId = iEndUserId;
	}
	
	public void setItemRound(String sItemRound) {
		this.itemRound = sItemRound;
	}
	
	public void setPriceLevel(int iPriceLevel) {
		this.priceLevel = iPriceLevel;
	}
	
	public void setSc(int iIndex, BigDecimal dSc) {
		this.sc[iIndex] = dSc;
	}
	
	public void setTax(int iIndex, BigDecimal dTax) {
		this.tax[iIndex] = dTax; 
	}
	
	public void setScRound(String sScRound) {
		this.scRound = sScRound;
	}
	
	public void setTaxRound(String sTaxRound) {
		this.taxRound = sTaxRound;
	}
	
	public void setDiscRound(String sDiscRound) {
		this.discRound = sDiscRound;
	}
	
	public void setCheckRound(String sCheckRound) {
		this.checkRound = sCheckRound;
	}
	
	public void setPayRound(String sPayRound) {
		this.payRound = sPayRound;
	}
	
	public void setItemDecimal(int iItemDecimal) {
		this.itemDecimal = iItemDecimal;
	}
	
	public void setScDecimal(int iScDecimal) {
		this.scDecimal = iScDecimal;
	}
	
	public void setTaxDecimal(int iTaxDecimal) {
		this.taxDecimal = iTaxDecimal;
	}
	
	public void setDiscDecimal(int iDiscDecimal) {
		this.discDecimal = iDiscDecimal;
	}
	
	public void setCheckDecimal(int iCheckDecimal) {
		this.checkDecimal = iCheckDecimal;
	}
	
	public void setPayDecimal(int iPayDecimal) {
		this.payDecimal = iPayDecimal;
	}
	
	public void setTaxScIncludeTaxScMask(int iIndex, String sMask){
		this.taxScIncludeTaxSCMask[iIndex] = sMask;
	}
	
	public void setTaxScIncludePreDisc(String sTaxScIncludePreDisc){
		this.taxScIncludePreDisc = sTaxScIncludePreDisc;
	}
	
	public void setTaxIncludeMidDisc(String sTaxIncludeMidDisc){
		this.taxIncludeMidDisc = sTaxIncludeMidDisc;
	}
	
	public void setCheckPrefix(String sCheckPrefix) {
		this.checkPrefix = sCheckPrefix;
	}
	
	public void setStartCheckNum(int iStartCheckNum) {
		this.startCheckNum = iStartCheckNum;
	}
	
	public void setEndCheckNum(int iEndCheckNum) {
		this.endCheckNum = iEndCheckNum;
	}
	
	public void setNextCheckNum(int iNextCheckNum) {
		this.nextCheckNum = iNextCheckNum;
	}
	
	public void setLastRecallTime(String sLastRecallTime) {
		this.lastRecallTime = sLastRecallTime;
	}
	
	public void setLastRecallLocTime(DateTime oLastRecallLocTime) {
		this.lastRecallLocalTime = oLastRecallLocTime;
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

	//get bdayId
	public int getBdayId() {
		return this.bdayId;
	}

	public int getShopId() {
		return this.shopId;
	}
	
	public int getOletId() {
		return this.oletId;
	}
	
	//get date
	public Date getDate() {
		return this.date;
	}
	
	//get date in string
	public String getDateInString() {
		SimpleDateFormat oDateTime = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		return oDateTime.format(this.date);
	}
	
	//get date with format
	public String getDateInStringWithFormat(String sStringFormat) {
		SimpleDateFormat oDateTime = new SimpleDateFormat(sStringFormat, Locale.ENGLISH);
		return oDateTime.format(this.date);
	}
	
	//get day of week
	public int getDayOfWeek() {
		return this.dayOfWeek;
	}
	
	//get holiday cald id
	public int getHolidayCaldId() {
		return this.holidayCaldId;
	}
	
	//get day before holiday cald id
	public int getDayBeforeHolidayCaldId() {
		return this.dayBeforeHolidayCaldId;
	}
	
	//get special day cald id
	public int getSpecialDayCaldId() {
		return this.specialDayCaldId;
	}
	
	//get day beofre special day cald id
	public int getDayBeforeSpecialDayCaldId() {
		return this.dayBeforeSpecialDayCaldId;
	}

	//get start time
	public String getStartTime() {
		return this.startTime;
	}
	
	public DateTime getStartLocTime() {
		return this.startLocalTime;
	}
	
	//get start local time to string
	public String getStartLocTimeToString() {
		return this.startLocalTime.toString("yyyy-MM-dd HH:mm:ss");
	}

	//get end time
	public String getEndTime() {
		return this.endTime;
	}
	
	//get end local time to string
	public String getEndLocTimeToString() {
		return this.endLocalTime.toString("yyyy-MM-dd HH:mm:ss");
	}
	
	public String getLastRecallTime() {
		return this.lastRecallTime;
	}
	
	//get last recall local time to string
	public String getLastRecallLocTimeToString() {
		return this.lastRecallLocalTime.toString("yyyy-MM-dd HH:mm:ss");
	}
	
	public String getItemRound() {
		return this.itemRound;
	}
	
	public int getPriceLevel() {
		return this.priceLevel;
	}
	
	public BigDecimal getSc(int iIndex) {
		return this.sc[(iIndex-1)];
	}
	
	public BigDecimal[] getAllSc() {
		return this.sc;
	}
	
	public BigDecimal getTax(int iIndex) {
		return this.tax[(iIndex-1)];
	}
	
	public BigDecimal[] getAllTax() {
		return this.tax;
	}
		
	public String getScRound() {
		return this.scRound;
	}
	
	public String getTaxRound() {
		return this.taxRound;
	}
	
	public String getDiscRound() {
		return this.discRound;
	}
	
	public String getCheckRound() {
		return this.checkRound;
	}
	
	public String getPayRound() {
		return this.payRound;
	}
	
	public int getItemDecimal() {
		return this.itemDecimal;
	}
	
	public int getScDecimal() {
		return this.scDecimal;
	}
	
	public int getTaxDecimal() {
		return this.taxDecimal;
	}
	
	public int getDiscDecimal() {
		return this.discDecimal;
	}
	
	public int getCheckDecimal() {
		return this.checkDecimal;
	}
	
	public int getPayDecimal() {
		return this.payDecimal;
	}
	
	public int getRecallUserId() {
		return this.recallUserId;
	}
	
	public int getRecallCount() {
		return this.recallCount;
	}
	
	public int getNextCheckNum() {
		return this.nextCheckNum;
	}
	
	public String getTaxScIncludeTaxScMask(int iIndex) {
		return this.taxScIncludeTaxSCMask[(iIndex-1)];
	}
	
	public String getTaxScIncludePreDisc() {
		return this.taxScIncludePreDisc;
	}
	
	public String getTaxIncludeMidDisc() {
		return this.taxIncludeMidDisc;
	}
	
	public String getCheckPrefix() {
		return this.checkPrefix;
	}
}
