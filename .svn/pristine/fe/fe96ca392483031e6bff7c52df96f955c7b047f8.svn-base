package om;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosMixAndMatchRule {
	private int mamrId;
	private int shopId;
	private int oletId;
	private String[] name;
	private int maxItemIndex;
	private int seq;
	private DateTime startDate;
	private DateTime endDate;
	private Time startTime;
	private Time endTime;
	private String timeBy;
	private String weekMask;
	private String holiday;
	private String dayBeforeHoliday;
	private String specialDay;
	private String dayBeforeSpecialDay;
	private String status;
	
	// timeBy
	public static String TIME_BY_ITEM = "";
	public static String TIME_BY_CHECK = "c";
	
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
	
	private ArrayList<PosMixAndMatchItem> m_oMixAndMatchItemList;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_SUSPENDED = "s";
	public static String STATUS_DELETED = "d";
	
	//init with initialized value
	public PosMixAndMatchRule() {
		this.init();
	}
	
	public PosMixAndMatchRule(JSONObject oJSONObject) {
		readDataFromJson(oJSONObject);
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject mixAndMatchRuleJSONObject) {
		JSONObject tempJSONObject = null;
		JSONArray tempJSONArray = null;
		PosMixAndMatchItem oPosMixAndMatchItem;
		DateTimeFormatter oDateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		SimpleDateFormat oTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		int i;
		
		tempJSONObject = mixAndMatchRuleJSONObject.optJSONObject("PosMixAndMatchRule");
		if(tempJSONObject == null)
			tempJSONObject = mixAndMatchRuleJSONObject;
			
		this.init();
		this.mamrId = tempJSONObject.optInt("mamr_id");
		this.shopId = tempJSONObject.optInt("mamr_shop_id");
		this.oletId = tempJSONObject.optInt("mamr_olet_id");
		for(i=1; i<=5; i++) 
			this.name[(i-1)] = tempJSONObject.optString("mamr_name_l"+i);
		this.maxItemIndex = tempJSONObject.optInt("mamr_max_item_index");
		this.seq = tempJSONObject.optInt("mamr_seq");
		String sStartTime = "";
		try {
			String sStartDate = tempJSONObject.optString("mamr_start_date");
			if(!sStartDate.isEmpty())
				this.startDate = oDateFormat.parseDateTime(sStartDate);
			
			String sEndDate = tempJSONObject.optString("mamr_end_date");
			if(!sEndDate.isEmpty())
				this.endDate = oDateFormat.parseDateTime(sEndDate);

			sStartTime = tempJSONObject.optString("mamr_start_time");
			if(!sStartTime.isEmpty())
				this.startTime = new Time(oTimeFormat.parse(sStartTime).getTime());
			
			String sEndTime = tempJSONObject.optString("mamr_end_time");
			if(!sEndTime.isEmpty())
				this.endTime = new Time(oTimeFormat.parse(sEndTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.timeBy = tempJSONObject.optString("mamr_time_by", PosMixAndMatchRule.TIME_BY_ITEM);
		this.weekMask = tempJSONObject.optString("mamr_week_mask");
		
		this.holiday = tempJSONObject.optString("mamr_holiday", PosMixAndMatchRule.HOLIDAY_NO_CHECKING);
		this.dayBeforeHoliday = tempJSONObject.optString("mamr_day_before_holiday", PosMixAndMatchRule.BEFORE_HOLIDAY_NO_CHECKING);
		this.specialDay = tempJSONObject.optString("mamr_special_day", PosMixAndMatchRule.SPECIAL_DAY_NO_CHECKING);
		this.dayBeforeSpecialDay = tempJSONObject.optString("mamr_day_before_special_day", PosMixAndMatchRule.BEFORE_SPECIAL_DAY_NO_CHECKING);
		this.status = tempJSONObject.optString("mamr_status", PosMixAndMatchRule.STATUS_ACTIVE);
		
		//check whether item record exist
		tempJSONArray = mixAndMatchRuleJSONObject.optJSONArray("PosMixAndMatchItem");
		if (tempJSONArray != null) {
			for(i=0; i<tempJSONArray.length(); i++) {
				JSONObject oMixAndMatchItem = tempJSONArray.optJSONObject(i);
				if(oMixAndMatchItem != null) {
					oPosMixAndMatchItem = new PosMixAndMatchItem(oMixAndMatchItem);
					this.m_oMixAndMatchItemList.add(oPosMixAndMatchItem);
				}
			}
		}
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("PosMixAndMatchRule")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("PosMixAndMatchRule")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("PosMixAndMatchRule");
			readDataFromJson(tempJSONObject);
		}
		
		return bUpdate;
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray oJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("mixAndMatchRules")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("mixAndMatchRules"))
				return null;
			
			oJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("mixAndMatchRules");
		}
		
		return oJSONArray;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.mamrId = 0;
		this.shopId = 0;
		this.oletId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		this.maxItemIndex = 0;		
		this.seq = 0;
		this.startDate = null;
		this.endDate = null;
		this.startTime = null;
		this.endTime = null;
		this.timeBy = PosMixAndMatchRule.TIME_BY_ITEM;
		this.weekMask = "";
		this.holiday = PosMixAndMatchRule.HOLIDAY_NO_CHECKING;
		this.dayBeforeHoliday = PosMixAndMatchRule.BEFORE_HOLIDAY_NO_CHECKING;
		this.specialDay = PosMixAndMatchRule.SPECIAL_DAY_NO_CHECKING;
		this.dayBeforeSpecialDay = PosMixAndMatchRule.BEFORE_SPECIAL_DAY_NO_CHECKING;
		this.status = PosMixAndMatchRule.STATUS_ACTIVE;
		
		if(this.m_oMixAndMatchItemList == null)
			this.m_oMixAndMatchItemList = new ArrayList<PosMixAndMatchItem>();
		else
			this.m_oMixAndMatchItemList.clear();
	}
	
	//read data from database 
	public JSONArray readAll(int iShopId, int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		 
		try {
			requestJSONObject.put("shopId", iShopId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("recursive", 1);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllMixAndMatchRules", requestJSONObject.toString());

		return responseJSONArray;
	}
	
	public int getRuleId() {
		return this.mamrId;
	}
	
	public int getShopId() {
		return this.shopId;
	}
	
	public int getOletId () {
		return this.oletId;
	}
	
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	public int getMaxItemIndex() {
		return this.maxItemIndex;
	}
	
	public int getSequence() {
		return this.seq;
	}
	
	public String getStatus() {
		return this.status;
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
	
	//get time by
	public String getTimeBy() {
		return this.timeBy;
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
	
	public ArrayList<PosMixAndMatchItem> getRuleItemList(){
		return this.m_oMixAndMatchItemList;
	}
}
