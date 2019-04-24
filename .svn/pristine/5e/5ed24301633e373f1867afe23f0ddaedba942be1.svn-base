//Database: pos_discount_acls - Access control lists for discount groups, item discount groups, outlets & dates
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

public class PosDiscountAcl {
	private int daclId;
	private int dgrpId;
	private int digpId;
	private int oletId;
	private String allow;
	private DateTime startDate;
	private DateTime endDate;
	private Time startTime;
	private Time endTime;
	private String weekMask;
	private String holiday;
	private String dayBeforeHoliday;
	private String specialDay;
	private String dayBeforeSpecialDay;
	
	// allow 
	public static String ALLOW_NO = "";
	public static String ALLOW_YES = "y";
	
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
	
	//init object with initialize value
	public PosDiscountAcl () {
		this.daclId = 0;
		this.dgrpId = 0;
		this.digpId = 0;
		this.oletId = 0;
		this.allow = "";
		this.startDate = null;
		this.endDate = null;
		this.startTime = null;
		this.endTime = null;
		this.weekMask = "0000000";
		this.holiday = "";
		this.dayBeforeHoliday = "";
		this.specialDay = "";
		this.dayBeforeSpecialDay = "";
	}
	
	//init object from database by dacl_id
	public PosDiscountAcl (int iDaclId) {
		this.daclId = iDaclId;
	}
	
	//read data from database by dacl_id
	public void readById (int iDaclId) {
		this.daclId = iDaclId;
	}
	
	//Init object from JSONObject
	public PosDiscountAcl (JSONObject discountAclJSONObject) {
		this.readDataFromJson(discountAclJSONObject);
	}
	
	//read all conditions by outlet ID
	public JSONArray readAllByOutletId(int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("outletId", iOutletId);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllDiscountAclsByOutlet", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	//read a list of data from API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray discountAclJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("discount_acls")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("discount_acls"))
				return null;
			
			discountAclJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("discount_acls");
		}
		
		return discountAclJSONArray;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject overrideConditionJSONObject) {
		JSONObject resulDiscountAcl = null;
		DateTimeFormatter oDateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		SimpleDateFormat oTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		
		resulDiscountAcl = overrideConditionJSONObject.optJSONObject("PosDiscountAcl");
		if(resulDiscountAcl == null)
			resulDiscountAcl = overrideConditionJSONObject;
		
		this.init();
		
		this.daclId = resulDiscountAcl.optInt("dacl_id");
		this.dgrpId = resulDiscountAcl.optInt("dacl_dgrp_id");
		this.digpId = resulDiscountAcl.optInt("dacl_digp_id");
		this.oletId = resulDiscountAcl.optInt("dacl_olet_id");
		this.allow = resulDiscountAcl.optString("dacl_allow");
		
		try {
			String sStartDate = resulDiscountAcl.optString("dacl_start_date");
			if(!sStartDate.isEmpty())
				this.startDate = oDateFormat.parseDateTime(sStartDate);
			
			String sEndDate = resulDiscountAcl.optString("dacl_end_date");
			if(!sEndDate.isEmpty())
				this.endDate = oDateFormat.parseDateTime(sEndDate);
			
			String sStartTime = resulDiscountAcl.optString("dacl_start_time");
			if(!sStartTime.isEmpty())
				this.startTime = new Time(oTimeFormat.parse(sStartTime).getTime());
			
			String sEndTime = resulDiscountAcl.optString("dacl_end_time");
			if(!sEndTime.isEmpty())
				this.endTime = new Time(oTimeFormat.parse(sEndTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		this.weekMask = resulDiscountAcl.optString("dacl_week_mask");
		this.holiday = resulDiscountAcl.optString("dacl_holiday", PosDiscountAcl.HOLIDAY_NO_CHECKING);
		this.dayBeforeHoliday = resulDiscountAcl.optString("dacl_day_before_holiday", PosDiscountAcl.BEFORE_HOLIDAY_NO_CHECKING);
		this.specialDay = resulDiscountAcl.optString("dacl_special_day", PosDiscountAcl.SPECIAL_DAY_NO_CHECKING);
		this.dayBeforeSpecialDay = resulDiscountAcl.optString("dacl_day_before_special_day", PosDiscountAcl.BEFORE_SPECIAL_DAY_NO_CHECKING);
	}
	
	// init value
	public void init() {
		this.daclId = 0;
		this.dgrpId = 0;
		this.digpId = 0;
		this.oletId = 0;
		this.allow = "";
		this.startDate = null;
		this.endDate = null;
		this.startTime = null;
		this.endTime = null;
		this.weekMask = "";
		this.holiday = PosDiscountAcl.HOLIDAY_NO_CHECKING;
		this.dayBeforeHoliday = PosDiscountAcl.BEFORE_HOLIDAY_NO_CHECKING;
		this.specialDay = PosDiscountAcl.SPECIAL_DAY_NO_CHECKING;
		this.dayBeforeSpecialDay = PosDiscountAcl.BEFORE_SPECIAL_DAY_NO_CHECKING;
	}
	
	//get daclId
	public int getDaclId() {
		return this.daclId;
	}
	
	//get dgrp id
	public int getDgrpId() {
		return this.dgrpId;
	}
	
	//get digp id
	public int getDigpId() {
		return this.digpId;
	}
	
	//get olet id
	protected int getOletId() {
		return this.oletId;
	}
	
	//get allow
	protected String getAllow() {
		return this.allow;
	}
	
	public boolean isAllow() {
		return this.allow.equals(PosDiscountAcl.ALLOW_YES);
	}
	
	//get start date
	public DateTime getStartDate() {
		return this.startDate;
	}
	
	//get end date
	public DateTime getEndDate() {
		return this.endDate;
	}
	
	//get start time
	public Time getStartTime() {
		return this.startTime;
	}
	
	//get end time
	public Time getEndTime() {
		return this.endTime;
	}

	//get week mask
	protected String getWeekMask() {
		return this.weekMask;
	}
	
	//get weekday allowance
	public boolean getWeekdayAllowance(int iWeekday) {
		if(this.weekMask.length() > 0 && this.weekMask.substring(iWeekday, (iWeekday+1)).equals(PosOverrideCondition.WEEKDAY_ALLOW))
			return true;
		else
			return false;
	}
	
	//get holiday
	protected String getHoliday() {
		return this.holiday;
	}
	
	public boolean isNoRuleForHoliday() {
		if(this.holiday.equals(PosOverrideCondition.HOLIDAY_NO_CHECKING))
			return true;
		else
			return false;
	}
	
	public boolean isApplyOnHolidayWithoutWeekMask() {
		if(this.holiday.equals(PosOverrideCondition.HOLIDAY_APPLY))
			return true;
		else
			return false;
	}
	
	public boolean isApplyOnHolidayWithWeekMask() {
		if(this.holiday.equals(PosOverrideCondition.HOLIDAY_APPLY_WITH_WEEKMASK))
			return true;
		else
			return false;
	}
	
	public boolean isNotApplyOnHolidayWithoutWeekMask() {
		if(this.holiday.equals(PosOverrideCondition.HOLIDAY_NOT_APPLY))
			return true;
		else
			return false;
	}
	
	public boolean isNotApplyOnHolidayWithWeekMask() {
		if(this.holiday.equals(PosOverrideCondition.HOLIDAY_NOT_APPLY_WITH_WEEKMASK))
			return true;
		else
			return false;
	}
	
	//get day before holiday
	protected String getDayBeforeHoliday() {
		return this.dayBeforeHoliday;
	}
	
	public boolean isNoRuleForDayBeforeHoliday() {
		if(this.dayBeforeHoliday.equals(PosOverrideCondition.BEFORE_HOLIDAY_NO_CHECKING))
			return true;
		else
			return false;
	}
	
	public boolean isApplyOnDayBeforeHolidayWithoutWeekMask() {
		if(this.dayBeforeHoliday.equals(PosOverrideCondition.BEFORE_HOLIDAY_APPLY))
			return true;
		else
			return false;
	}
	
	public boolean isApplyOnDayBeforeHolidayWithWeekMask() {
		if(this.dayBeforeHoliday.equals(PosOverrideCondition.BEFORE_HOLIDAY_APPLY_WITH_WEEKMASK))
			return true;
		else
			return false;
	}
	
	public boolean isNotApplyOnDayBeforeHolidayWithoutWeekMask() {
		if(this.dayBeforeHoliday.equals(PosOverrideCondition.BEFORE_HOLIDAY_NOT_APPLY))
			return true;
		else
			return false;
	}
	
	public boolean isNotApplyOnDayBeforeHolidayWithWeekMask() {
		if(this.dayBeforeHoliday.equals(PosOverrideCondition.BEFORE_HOLIDAY_NOT_APPLY_WITH_WEEKMASK))
			return true;
		else
			return false;
	}
	
	//get special day
	protected String getSpecialDay() {
		return this.specialDay;
	}
	
	public boolean isNoRuleOnSpeiclaDay() {
		if(this.specialDay.equals(PosOverrideCondition.SPECIAL_DAY_NO_CHECKING))
			return true;
		else
			return false;
	}
	
	public boolean isApplyOnSpecialDayWithoutWeekMask() {
		if(this.specialDay.equals(PosOverrideCondition.SPECIAL_DAY_APPLY))
			return true;
		else
			return false;
	}
	
	public boolean isApplyOnSpecialDayWithWeekMask() {
		if(this.specialDay.equals(PosOverrideCondition.SPECIAL_DAY_APPLY_WITH_WEEKMASK))
			return true;
		else
			return false;
	}
	
	public boolean isNotApplyOnSpecialDayWithoutWeekMask() {
		if(this.specialDay.equals(PosOverrideCondition.SPECIAL_DAY_NOT_APPLY))
			return true;
		else
			return false;
	}
	
	public boolean isNotApplyOnSpecialDayWithWeekMask() {
		if(this.specialDay.equals(PosOverrideCondition.SPECIAL_DAY_NOT_APPLY_WITH_WEEKMASK))
			return true;
		else
			return false;
	}
	
	//get day before special day
	protected String getDayBeforeSpecialDay() {
		return this.dayBeforeSpecialDay;
	}
	
	public boolean isNoRuleOnDayBeforeSpeiclaDay() {
		if(this.dayBeforeSpecialDay.equals(PosOverrideCondition.BEFORE_SPECIAL_DAY_NO_CHECKING))
			return true;
		else
			return false;
	}
	
	public boolean isApplyOnDayBeforeSpecialDayWithoutWeekMask() {
		if(this.dayBeforeSpecialDay.equals(PosOverrideCondition.BEFORE_SPECIAL_DAY_APPLY))
			return true;
		else
			return false;
	}
	
	public boolean isApplyOnDayBeforeSpecialDayWithWeekMask() {
		if(this.dayBeforeSpecialDay.equals(PosOverrideCondition.BEFORE_SPECIAL_DAY_APPLY_WITH_WEEKMASK))
			return true;
		else
			return false;
	}
	
	public boolean isNotApplyOnDayBeforeSpecialDayWithoutWeekMask() {
		if(this.dayBeforeSpecialDay.equals(PosOverrideCondition.BEFORE_SPECIAL_DAY_NOT_APPLY))
			return true;
		else
			return false;
	}
	
	public boolean isNotApplyOnDayBeforeSpecialDayWithWeekMask() {
		if(this.dayBeforeSpecialDay.equals(PosOverrideCondition.BEFORE_SPECIAL_DAY_NOT_APPLY_WITH_WEEKMASK))
			return true;
		else
			return false;
	}
}
