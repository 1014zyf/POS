//Database: pos_order_item_acls - Access control lists for user group, item order groups, outlets and dates
package om;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosOrderItemAcl {
	private int oaclId;
	private int ugrpId;
	private int oigpId;
	private int oletId;
	private String allow;
	private DateTime startDate;
	private DateTime endDate;
	private String weekMask;
	private String holiday;
	private String dayBeforeHoliday;
	private String specialDay;
	private String dayBeforeSpecialDay;
	
	public static final String ALLOW_NO = "";
	public static final String ALLOW_YES = "y";
	public static final String ALLOW_NO_ASK_FOR_ANOTHER_USER_AUTHORITY = "a";
	
	public static final String HOLIDAY_NO_CHECKING = "";
	public static final String HOLIDAY_ALLOW = "y";
	public static final String HOLIDAY_ALLOW_WITH_WEEKMASK = "z";
	public static final String HOLIDAY_NOT_ALLOW = "n";
	public static final String HOLIDAY_NOT_ALLOW_WITH_WEEKMASK = "x";
	
	public static final String BEFORE_HOLIDAY_NO_CHECKING = "";
	public static final String BEFORE_HOLIDAY_ALLOW = "y";
	public static final String BEFORE_HOLIDAY_ALLOW_WITH_WEEKMASK = "z";
	public static final String BEFORE_HOLIDAY_NOT_ALLOW = "n";
	public static final String BEFORE_HOLIDAY_NOT_ALLOW_WITH_WEEKMASK = "x";
	
	public static final String SPECIAL_NO_CHECKING = "";
	public static final String SPECIAL_ALLOW = "y";
	public static final String SPECIAL_ALLOW_WITH_WEEKMASK = "z";
	public static final String SPECIAL_NOT_ALLOW = "n";
	public static final String SPECIAL_NOT_ALLOW_WITH_WEEKMASK = "x";
	
	public static final String BEFORE_SPECIAL_NO_CHECKING = "";
	public static final String BEFORE_SPECIAL_ALLOW = "y";
	public static final String BEFORE_SPECIAL_ALLOW_WITH_WEEKMASK = "z";
	public static final String BEFORE_SPECIAL_NOT_ALLOW = "n";
	public static final String BEFORE_SPECIAL_NOT_ALLOW_WITH_WEEKMASK = "x";
	
	public static final String WEEKDAY_NOT_ALLOW = "0";
	public static final String WEEKDAY_ALLOW = "1";
	
	//init object with initialize value
	public PosOrderItemAcl () {
		this.init();
	}
	
	//init object from JSONObject
	public PosOrderItemAcl (JSONObject orderItemAclJSONObject) {
		this.readDataFromJson(orderItemAclJSONObject);
	}
	
	//init valud
	public void init() {
		this.oaclId = 0;
		this.ugrpId = 0;
		this.oigpId = 0;
		this.oletId = 0;
		this.allow = "";
		this.startDate = null;
		this.endDate = null;
		this.weekMask = "";
		this.holiday = "";
		this.dayBeforeHoliday = "";
		this.specialDay = "";
		this.dayBeforeSpecialDay = "";
	}
	
	//read a list of data from API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray orderItemAclJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("orderItemAcls")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("orderItemAcls")) {
				return null;
			}
			
			orderItemAclJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("orderItemAcls");
		}
		
		return orderItemAclJSONArray;
	}
	
	//real data from response JSON
	private void readDataFromJson(JSONObject orderItemAclJSONObject) {
		JSONObject resultOrderItemAcl = null;
		DateTimeFormatter oDateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		
		resultOrderItemAcl = orderItemAclJSONObject.optJSONObject("PosOrderItemAcl");
		if(resultOrderItemAcl == null)
			resultOrderItemAcl = orderItemAclJSONObject;
		this.init();
		
		this.oaclId = resultOrderItemAcl.optInt("oacl_id");
		this.ugrpId = resultOrderItemAcl.optInt("oacl_ugrp_id");
		this.oigpId = resultOrderItemAcl.optInt("oacl_oigp_id");
		this.oletId = resultOrderItemAcl.optInt("oacl_olet_id");
		this.allow = resultOrderItemAcl.optString("oacl_allow");
		try {
			String sStartDate = resultOrderItemAcl.optString("oacl_start_date");
			if(!sStartDate.isEmpty())
				this.startDate = oDateFormat.parseDateTime(sStartDate);
			
			String sEndDate = resultOrderItemAcl.optString("oacl_end_date");
			if(!sEndDate.isEmpty())
				this.endDate = oDateFormat.parseDateTime(sEndDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.weekMask = resultOrderItemAcl.optString("oacl_week_mask");
		this.holiday = resultOrderItemAcl.optString("oacl_holiday");
		this.dayBeforeHoliday = resultOrderItemAcl.optString("oacl_day_before_holiday");
		this.specialDay = resultOrderItemAcl.optString("oacl_special_day");
		this.dayBeforeSpecialDay = resultOrderItemAcl.optString("oacl_day_before_special_day");
	}
	
	//read all order item acls
	public JSONArray readAll() {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try{
			requestJSONObject.put("recursive", -1);		
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllOrderItemAcls", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	//check conditions matched
	public boolean checkRulesCondition(DateTime dBusinessDate, int iWeekday, boolean bSpecialDay, boolean bBeforeSpecialDay, boolean bHoliday, boolean bBeforeHoliday) {
		boolean bIngoreWeekday = false, bDateRangeChecking = false, bSpecialDayChecking = false, bBeforeSpecialDayChecking = false;
		boolean bHolidayChecking = false, bBeforeHolidayChecking = false, bWeekdayChecking = false;
		
		//date range
		if(this.startDate == null) {
			if(this.endDate == null || this.endDate.compareTo(dBusinessDate) >= 0)
				bDateRangeChecking = true;
		}else if(this.startDate.compareTo(dBusinessDate) == 0) {
			if(this.endDate == null || this.endDate.compareTo(dBusinessDate) >= 0)
				bDateRangeChecking = true;
		}else if(this.startDate.compareTo(dBusinessDate) < 0) {
			if(this.endDate == null || this.endDate.compareTo(dBusinessDate) >= 0)
				bDateRangeChecking = true;
		}
		
		//special day
		switch(this.specialDay) {
		case PosOrderItemAcl.SPECIAL_NO_CHECKING:
			bSpecialDayChecking = true; 
			break;
		case PosOrderItemAcl.SPECIAL_ALLOW:
			if(bSpecialDay) {
				bSpecialDayChecking = true;
				bIngoreWeekday = true;
			}
			break;
		case PosOrderItemAcl.SPECIAL_ALLOW_WITH_WEEKMASK:
			if(bSpecialDay && getWeekdayAllowance(iWeekday))
				bSpecialDayChecking = true;
			break;
		default:
			break;
		}
		
		
		//day before special day
		switch(this.dayBeforeSpecialDay) {
		case PosOrderItemAcl.BEFORE_SPECIAL_NO_CHECKING:
			bBeforeSpecialDayChecking = true;
			break;
		case PosOrderItemAcl.BEFORE_SPECIAL_ALLOW:
			if(bBeforeSpecialDay) {
				bBeforeSpecialDayChecking = true;
				bIngoreWeekday = true;
			}
			break;
		case PosOrderItemAcl.BEFORE_SPECIAL_ALLOW_WITH_WEEKMASK:
			if(bBeforeSpecialDay && getWeekdayAllowance(iWeekday))
				bBeforeSpecialDayChecking = true;
			break;
		default:
			break;
		}
		
		//holiday
		switch(this.holiday) {
		case PosOrderItemAcl.HOLIDAY_NO_CHECKING:
			bHolidayChecking = true;
			break;
		case PosOrderItemAcl.HOLIDAY_ALLOW:
			if(bHoliday) {
				bHolidayChecking = true;
				bIngoreWeekday = true;
			}
			break;
		case PosOrderItemAcl.HOLIDAY_ALLOW_WITH_WEEKMASK:
			if(bHoliday && getWeekdayAllowance(iWeekday))
				bHolidayChecking = true;
			break;
		default:
			break;
		}
		
		//day before holiday
		switch(this.dayBeforeHoliday) {
		case PosOrderItemAcl.BEFORE_HOLIDAY_NO_CHECKING:
			bBeforeHolidayChecking = true;
			break;
		case PosOrderItemAcl.BEFORE_HOLIDAY_ALLOW:
			if(bBeforeHoliday) {
				bBeforeHolidayChecking = true;
				bIngoreWeekday = true;
			}
			break;
		case PosOrderItemAcl.BEFORE_HOLIDAY_ALLOW_WITH_WEEKMASK:
			if(bBeforeHoliday && getWeekdayAllowance(iWeekday))
				bBeforeHolidayChecking = true;
			break;
		default:
			break;
		}
		
		//weekday
		if(bIngoreWeekday)
			bWeekdayChecking = true;
		else
			bWeekdayChecking = getWeekdayAllowance(iWeekday);
		
		return (bDateRangeChecking && bSpecialDayChecking && bBeforeSpecialDayChecking && bHolidayChecking && bBeforeHolidayChecking && bWeekdayChecking);
	}
	
	//get oaclId
	protected int getOaclId() {
		return this.oaclId;
	}
	
	//get ugrp id
	protected int getUgrpId() {
		return this.ugrpId;
	}
	
	//get oigp id
	protected int getOigpId() {
		return this.oigpId;
	}
	
	//get olet id
	protected int getOletId() {
		return this.oletId;
	}
	
	//get allow
	public String getAllow() {
		return this.allow;
	}
	
	//get start date
	protected DateTime getStartDate() {
		return this.startDate;
	}
	
	//get end date
	protected DateTime getEndDate() {
		return this.endDate;
	}
	
	//get week mask
	protected String getWeekMask() {
		return this.weekMask;
	}
	
	//get week mask allowance
	public boolean getWeekdayAllowance(int iWeekday) {
		if(this.weekMask.length() > 0 && this.weekMask.substring(iWeekday, (iWeekday+1)).equals(PosOrderItemAcl.WEEKDAY_ALLOW))
			return true;
		else
			return false;
	}
	
	//get holiday
	protected String getHoliday() {
		return this.holiday;
	}
	
	//get day before holiday
	protected String getDayBeforeHoliday() {
		return this.dayBeforeHoliday;
	}
	
	//get special day
	protected String getSpecialDay() {
		return this.specialDay;
	}
	
	//get day before special day
	protected String getDayBeforeSpecialDay() {
		return this.dayBeforeSpecialDay;
	}
}
