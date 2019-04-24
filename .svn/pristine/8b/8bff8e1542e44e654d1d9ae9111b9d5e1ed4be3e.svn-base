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

public class PosCoverDiscount {
	private int covdId;
	private int shopId;
	private int ogrpId;
	private int outletId;
	private String[] name;
	private	int seq;
	private String applyTo;
	private	String memberValid;
	private Time startTime;
	private Time endTime;
	private DateTime startDate;
	private DateTime endDate;
	private String weekMask;
	private String holiday;
	private String dayBeforeHoliday;
	private String specialDay;
	private String dayBeforeSpecialDay;
	private String status;
	
	// applyTo
	public static String APPLY_TO_ITEM = "";
	public static String APPLY_TO_CHECK = "c";
	
	// member validation
	public static String NO_MEMBER = "";
	public static String NEED_MEMBER = "m";
	
	public static String WEEKDAY_NOT_ALLOW = "0";
	public static String WEEKDAY_ALLOW = "1";
	
	// holiday
	public static String HOLIDAY_NO_CHECKING = "";
	public static String HOLIDAY_APPLY = "y";
	public static String HOLIDAY_APPLY_WITH_WEEKMASK = "z";
	public static String HOLIDAY_NOT_APPLY = "n";
	public static String HOLIDAY_NOT_APPLY_WITH_WEEKMASK = "x";
	
	// dayBeforeHoliday
	public static String BEFORE_HOLIDAY_NO_CHECKING = "";
	public static String BEFORE_HOLIDAY_APPLY = "y";
	public static String BEFORE_HOLIDAY_APPLY_WITH_WEEKMASK = "z";
	public static String BEFORE_HOLIDAY_NOT_APPLY = "n";
	public static String BEFORE_HOLIDAY_NOT_APPLY_WITH_WEEKMASK = "x";
	
	// specialDay
	public static String SPECIAL_DAY_NO_CHECKING = "";
	public static String SPECIAL_DAY_APPLY = "y";
	public static String SPECIAL_DAY_APPLY_WITH_WEEKMASK = "z";
	public static String SPECIAL_DAY_NOT_APPLY = "n";
	public static String SPECIAL_DAY_NOT_APPLY_WITH_WEEKMASK = "x";
	
	// dayBeforeSpecialDay
	public static String BEFORE_SPECIAL_DAY_NO_CHECKING = "";
	public static String BEFORE_SPECIAL_DAY_APPLY = "y";
	public static String BEFORE_SPECIAL_DAY_APPLY_WITH_WEEKMASK = "z";
	public static String BEFORE_SPECIAL_DAY_NOT_APPLY = "n";
	public static String BEFORE_SPECIAL_DAY_NOT_APPLY_WITH_WEEKMASK = "x";
	
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETE = "d";
	public static String STATUS_SUPPEND = "s";
	
	public PosCoverDiscount() {
		this.init();
	}
	
	public PosCoverDiscount(JSONObject jsonObject) {
		readDataFromJson(jsonObject);
	}
	

	private void readDataFromJson(JSONObject jsonObject) {
		JSONObject resultCoverDiscounts = null;
		int i;
		
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		
		resultCoverDiscounts = jsonObject.optJSONObject("PosCoverDiscount");
		if(resultCoverDiscounts == null)
			resultCoverDiscounts = jsonObject;
		
		this.init();
		this.covdId = resultCoverDiscounts.optInt("covd_id");
		this.shopId = resultCoverDiscounts.optInt("covd_shop_id");
		this.ogrpId = resultCoverDiscounts.optInt("covd_ogrp_id");
		this.outletId = resultCoverDiscounts.optInt("covd_olet_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultCoverDiscounts.optString("covd_name_l"+i);

		this.seq = resultCoverDiscounts.optInt("covd_seq");
		this.applyTo = resultCoverDiscounts.optString("covd_apply_to", PosCoverDiscount.APPLY_TO_ITEM);
		this.memberValid = resultCoverDiscounts.optString("covd_member_validation", PosCoverDiscount.NO_MEMBER);
		
		String sStartDate = resultCoverDiscounts.optString("covd_start_date");
		if(!sStartDate.isEmpty()){
			try {
				this.startDate = dateFormat.parseDateTime(sStartDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		String sEndDate = resultCoverDiscounts.optString("covd_end_date");
		if(!sEndDate.isEmpty()){
			try {
				this.endDate = dateFormat.parseDateTime(sEndDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		String sStartTime = resultCoverDiscounts.optString("covd_start_time");
		if(!sStartTime.isEmpty()){
			try {
				this.startTime = new Time(timeFormat.parse(sStartTime).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		String sEndTime = resultCoverDiscounts.optString("covd_end_time");
		if(!sEndTime.isEmpty()){
			try {
				this.endTime = new Time(timeFormat.parse(sEndTime).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		this.weekMask = resultCoverDiscounts.optString("covd_week_mask");
		this.holiday = resultCoverDiscounts.optString("covd_holiday", PosCoverDiscount.HOLIDAY_NO_CHECKING);
		this.dayBeforeHoliday = resultCoverDiscounts.optString("covd_day_before_holiday", PosCoverDiscount.BEFORE_HOLIDAY_NO_CHECKING);
		this.specialDay = resultCoverDiscounts.optString("covd_special_day", PosCoverDiscount.SPECIAL_DAY_NO_CHECKING);
		this.dayBeforeSpecialDay = resultCoverDiscounts.optString("covd_day_before_special_day", PosCoverDiscount.BEFORE_SPECIAL_DAY_NO_CHECKING);

		this.status = resultCoverDiscounts.optString("covd_status", PosCoverDiscount.STATUS_ACTIVE);
		
	}

	private void init() {
		int i=0;
		
		this.covdId = 0;
		this.shopId = 0;
		this.ogrpId = 0;
		this.outletId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";

		this.seq = 0;
		this.applyTo = PosCoverDiscount.APPLY_TO_ITEM;
		this.memberValid = PosCoverDiscount.NO_MEMBER;
		
		this.startDate = null;
		this.endDate = null;
		this.startTime = null;
		this.endTime = null;
		
		this.weekMask = "";
		this.holiday = PosCoverDiscount.HOLIDAY_NO_CHECKING;
		this.dayBeforeHoliday = PosCoverDiscount.BEFORE_HOLIDAY_NO_CHECKING;
		this.specialDay = PosCoverDiscount.SPECIAL_DAY_NO_CHECKING;
		this.dayBeforeSpecialDay = PosCoverDiscount.BEFORE_SPECIAL_DAY_NO_CHECKING;
		
		this.status = PosCoverDiscount.STATUS_ACTIVE;
	}

	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray coverDiscountsJSONArray = null;
		
		if (OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false) == false) {
			return null;
		}else {
			try {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
					return null;
				
				if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("coverDiscounts")) {
					OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
					return null;
				}
				
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("coverDiscounts"))
					return null;
				
				coverDiscountsJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().getJSONArray("coverDiscounts");
				
			}catch(JSONException jsone) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.stackToString(jsone));
				return null;
			}
		}
		
		return coverDiscountsJSONArray;
	}
	
	public JSONArray readAllByShopOutletSeq(int iShopId, int iOutletId, int iSeq, String sApplyTo) {
		JSONObject requestJSONObject = new JSONObject();
		try {
			requestJSONObject.put("shopId", iShopId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("applyTo", sApplyTo);
			requestJSONObject.put("seq", iSeq);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		JSONArray responseJSONArray = this.readDataListFromApi("gm", "pos", "getCoverDiscountsWithShopOutletSeq", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	//get covd id
	public int getId(){
		return this.covdId;
	}
	
	
	//get name by lang index
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	private boolean isValidDate(DateTime oDate){
		if(startDate == null && endDate == null)
			return true;
		
		if(oDate.compareTo(startDate) > 0 && oDate.compareTo(endDate) < 0)
			return true;
		else
			return false;
	}
	
	private boolean isValidTime(Time oTime){
		if(startTime == null && endTime == null)
			return true;
		
		if(oTime.after(startTime) && oTime.before(endTime))
			return true;
		else
			return false;
	}
	
	public boolean isNeedMember(){
		if(this.memberValid.equals(PosCoverDiscount.NO_MEMBER))
			return false;
		else
			return true;
	}
	public boolean isApplyToCheck() {
		return this.applyTo.equals(PosCoverDiscount.APPLY_TO_CHECK);
	}
	
	public boolean isApplyToItem() {
		return this.applyTo.equals(PosCoverDiscount.APPLY_TO_ITEM);
	}
	
	//get week mask
	protected String getWeekMask() {
		return this.weekMask;
	}
	
	//get weekday allowance
	private boolean getWeekdayAllowance(int iWeekday) {
		if(this.weekMask.length() > 0 && this.weekMask.substring(iWeekday, (iWeekday+1)).equals(PosCoverDiscount.WEEKDAY_ALLOW))
			return true;
		else
			return false;
	}
	
	//get holiday
	protected String getHoliday() {
		return this.holiday;
	}
	
	private boolean isNoRuleForHoliday() {
		if(this.holiday.equals(PosCoverDiscount.HOLIDAY_NO_CHECKING))
			return true;
		else
			return false;
	}
	
	private boolean isApplyOnHolidayWithoutWeekMask() {
		if(this.holiday.equals(PosCoverDiscount.HOLIDAY_APPLY))
			return true;
		else
			return false;
	}
	
	private boolean isApplyOnHolidayWithWeekMask() {
		if(this.holiday.equals(PosCoverDiscount.HOLIDAY_APPLY_WITH_WEEKMASK))
			return true;
		else
			return false;
	}
	
	private boolean isNotApplyOnHolidayWithoutWeekMask() {
		if(this.holiday.equals(PosCoverDiscount.HOLIDAY_NOT_APPLY))
			return true;
		else
			return false;
	}
	
	private boolean isNotApplyOnHolidayWithWeekMask() {
		if(this.holiday.equals(PosCoverDiscount.HOLIDAY_NOT_APPLY_WITH_WEEKMASK))
			return true;
		else
			return false;
	}
	
	//get day before holiday
	protected String getDayBeforeHoliday() {
		return this.dayBeforeHoliday;
	}
	
	private boolean isNoRuleForDayBeforeHoliday() {
		if(this.dayBeforeHoliday.equals(PosCoverDiscount.BEFORE_HOLIDAY_NO_CHECKING))
			return true;
		else
			return false;
	}
	
	private boolean isApplyOnDayBeforeHolidayWithoutWeekMask() {
		if(this.dayBeforeHoliday.equals(PosCoverDiscount.BEFORE_HOLIDAY_APPLY))
			return true;
		else
			return false;
	}
	
	private boolean isApplyOnDayBeforeHolidayWithWeekMask() {
		if(this.dayBeforeHoliday.equals(PosCoverDiscount.BEFORE_HOLIDAY_APPLY_WITH_WEEKMASK))
			return true;
		else
			return false;
	}
	
	private boolean isNotApplyOnDayBeforeHolidayWithoutWeekMask() {
		if(this.dayBeforeHoliday.equals(PosCoverDiscount.BEFORE_HOLIDAY_NOT_APPLY))
			return true;
		else
			return false;
	}
	
	private boolean isNotApplyOnDayBeforeHolidayWithWeekMask() {
		if(this.dayBeforeHoliday.equals(PosCoverDiscount.BEFORE_HOLIDAY_NOT_APPLY_WITH_WEEKMASK))
			return true;
		else
			return false;
	}
	
	//get special day
	protected String getSpecialDay() {
		return this.specialDay;
	}
	
	private boolean isNoRuleOnSpeiclaDay() {
		if(this.specialDay.equals(PosCoverDiscount.SPECIAL_DAY_NO_CHECKING))
			return true;
		else
			return false;
	}
	
	private boolean isApplyOnSpecialDayWithoutWeekMask() {
		if(this.specialDay.equals(PosCoverDiscount.SPECIAL_DAY_APPLY))
			return true;
		else
			return false;
	}
	
	private boolean isApplyOnSpecialDayWithWeekMask() {
		if(this.specialDay.equals(PosCoverDiscount.SPECIAL_DAY_APPLY_WITH_WEEKMASK))
			return true;
		else
			return false;
	}
	
	private boolean isNotApplyOnSpecialDayWithoutWeekMask() {
		if(this.specialDay.equals(PosCoverDiscount.SPECIAL_DAY_NOT_APPLY))
			return true;
		else
			return false;
	}
	
	private boolean isNotApplyOnSpecialDayWithWeekMask() {
		if(this.specialDay.equals(PosCoverDiscount.SPECIAL_DAY_NOT_APPLY_WITH_WEEKMASK))
			return true;
		else
			return false;
	}
	
	//get day before special day
	protected String getDayBeforeSpecialDay() {
		return this.dayBeforeSpecialDay;
	}
	
	private boolean isNoRuleOnDayBeforeSpeiclaDay() {
		if(this.dayBeforeSpecialDay.equals(PosCoverDiscount.BEFORE_SPECIAL_DAY_NO_CHECKING))
			return true;
		else
			return false;
	}
	
	private boolean isApplyOnDayBeforeSpecialDayWithoutWeekMask() {
		if(this.dayBeforeSpecialDay.equals(PosCoverDiscount.BEFORE_SPECIAL_DAY_APPLY))
			return true;
		else
			return false;
	}
	
	private boolean isApplyOnDayBeforeSpecialDayWithWeekMask() {
		if(this.dayBeforeSpecialDay.equals(PosCoverDiscount.BEFORE_SPECIAL_DAY_APPLY_WITH_WEEKMASK))
			return true;
		else
			return false;
	}
	
	private boolean isNotApplyOnDayBeforeSpecialDayWithoutWeekMask() {
		if(this.dayBeforeSpecialDay.equals(PosCoverDiscount.BEFORE_SPECIAL_DAY_NOT_APPLY))
			return true;
		else
			return false;
	}
	
	private boolean isNotApplyOnDayBeforeSpecialDayWithWeekMask() {
		if(this.dayBeforeSpecialDay.equals(PosCoverDiscount.BEFORE_SPECIAL_DAY_NOT_APPLY_WITH_WEEKMASK))
			return true;
		else
			return false;
	}
	
	public boolean checkCondition(){
		int iWeekday = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDayOfWeek();
		boolean bIsSpecialDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay().isSpecialDay();
		boolean bIsDayBeforeSpecialDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay().isDayBeforeSpecialDay();
		boolean bIsHoliday = AppGlobal.g_oFuncOutlet.get().getBusinessDay().isHoliday();
		boolean bIsDayBeforeHoliday = AppGlobal.g_oFuncOutlet.get().getBusinessDay().isDayBeforeHoliday();
		boolean bSpecialControlChecking = false;
		boolean bOrCaseFulfill = false;
		
		DateTime oBusinessDate = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDate();
		Time oTime = null;
		//checking time range
		//ignore time by, using system time for checking time range
		DateTimeFormatter oTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss");
		SimpleDateFormat oSimpleTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		DateTime oNowDateTime = AppGlobal.getCurrentTime(false);
		try {
			oTime = new Time(oSimpleTimeFormat.parse(oTimeFormatter.print(oNowDateTime)).getTime());
		}catch (ParseException exception) {
			exception.printStackTrace();
			AppGlobal.stack2Log(exception);
		}
		
		//check special condition

		if(!this.isNoRuleOnSpeiclaDay()) {
			if(this.isApplyOnSpecialDayWithoutWeekMask() && bIsSpecialDay) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(this.isApplyOnSpecialDayWithWeekMask() && bIsSpecialDay && this.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(this.isNotApplyOnSpecialDayWithoutWeekMask() && bIsSpecialDay) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}else if(this.isNotApplyOnSpecialDayWithWeekMask() && bIsSpecialDay && this.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}
		}
		
		if(bOrCaseFulfill == false && !this.isNoRuleOnDayBeforeSpeiclaDay()) {
			if(this.isApplyOnDayBeforeSpecialDayWithoutWeekMask() && bIsDayBeforeSpecialDay) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(this.isApplyOnDayBeforeSpecialDayWithWeekMask() && bIsDayBeforeSpecialDay && this.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(this.isNotApplyOnDayBeforeSpecialDayWithoutWeekMask() && bIsDayBeforeSpecialDay) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}else if(this.isNotApplyOnDayBeforeSpecialDayWithWeekMask() && bIsDayBeforeSpecialDay && this.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}
		}
		
		if(bOrCaseFulfill == false && !this.isNoRuleForHoliday()) {
			if(this.isApplyOnHolidayWithoutWeekMask() && bIsHoliday) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(this.isApplyOnHolidayWithWeekMask() && bIsHoliday && this.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(this.isNotApplyOnHolidayWithoutWeekMask() && bIsHoliday) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}else if(this.isNotApplyOnHolidayWithWeekMask() && bIsHoliday && this.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}
		}

		if(bOrCaseFulfill == false && !this.isNoRuleForDayBeforeHoliday()) {
			if(this.isApplyOnDayBeforeHolidayWithoutWeekMask() && bIsDayBeforeHoliday) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(this.isApplyOnDayBeforeHolidayWithWeekMask() && bIsDayBeforeHoliday && this.getWeekdayAllowance(iWeekday)){
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(this.isNotApplyOnDayBeforeHolidayWithoutWeekMask() && bIsDayBeforeHoliday) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}else if(this.isNotApplyOnDayBeforeHolidayWithWeekMask() && bIsDayBeforeHoliday && this.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}
		}

		if(bOrCaseFulfill == false && this.getWeekdayAllowance(iWeekday))
			bSpecialControlChecking = true;
		
		if(this.isValidDate(oBusinessDate) && this.isValidTime(oTime) && bSpecialControlChecking)
			return true;
			
		return false;
	}
	
	
}
