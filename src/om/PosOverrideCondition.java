//Database: pos_override_conditions - Override conditions for print queues, prices, S.C., taxes
package om;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;

public class PosOverrideCondition {
	private int overId;
	private int shopId;
	private int oletId;
	private String[] name;
	private int fromPrtqId;
	private int toPrtqId;
	private int fromPriceLevel;
	private int toPriceLevel;
	private String[] chargeSc;
	private String[] chargeTax;
	private int dpanId;
	private String checkOrderingType;
	private int priority;
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
	private int startTable;
	private int endTable;
	private String startTableExt;
	private String endTableExt;
	private int stgpId;
	private String orderingType;
	private String checkOrderingMode;
	private int perdId;
	private int sphrId;
	private String idgpIds;
	private String dtypIds;
	private String status;
	private int ctypId;
	
	private DateTimeFormatter m_oDTFormatterYMD;
	
	// chargeSc
	public static String CHARGE_SC_NO_CHANGE = "";
	public static String CHARGE_SC_CHARGE = "c";
	public static String CHARGE_SC_WAIVE = "x";
	
	// chargeTax
	public static String CHARGE_TAX_NO_CHANGE = "";
	public static String CHARGE_TAX_CHARGE = "c";
	public static String CHARGE_TAX_WAIVE = "x";
	public static String INCLUSIVE_TAX_NO_BREAKDOWN = "n";
	
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
	
	// orderingType
	public static String ORDERING_TYPE_NO_USE = "";
	public static String ORDERING_TYPE_TAKEOUT = "t";	
	
	public static String WEEKDAY_NOT_ALLOW = "0";
	public static String WEEKDAY_ALLOW = "1";
	
	// ordering type
	public static String CHECK_ORDERING_TYPE_NO_USE = "";
	public static String CHECK_ORDERING_TYPE_TAKEOUT = "t";

	// ordering mode
	public static final String CHECK_ORDERING_MODE_FINE_DINING = "";
	public static final String CHECK_ORDERING_MODE_FAST_FOOD = "f";
	public static final String CHECK_ORDERING_MODE_SELF_ORDER_KIOSK = "k";
	
	
	public static final String STATUS_ACTIVE = "";
	public static final String STATUS_SUSPENDED = "s";
	
	//init object with initialize value
	public PosOverrideCondition () {
		this.init();
	}
	
	//Init object from JSONObject
	public PosOverrideCondition (JSONObject overrideConditionJSONObject) {
		this.readDataFromJson(overrideConditionJSONObject);
	}
	
	//Init object from database by over_id
	public PosOverrideCondition (int iOverId) {
		this.init();
		
		this.overId = iOverId;
	}
	
	public PosOverrideCondition(PosOverrideCondition oPosOverrideCondition) {
		this.init();
		
		this.overId = oPosOverrideCondition.overId;
		this.shopId = oPosOverrideCondition.shopId;
		this.oletId = oPosOverrideCondition.oletId;
		for (int i = 0; i < 5; i++)
			this.name[i] = oPosOverrideCondition.name[i];

		this.fromPrtqId = oPosOverrideCondition.fromPrtqId;
		this.toPrtqId = oPosOverrideCondition.toPrtqId;
		this.fromPriceLevel = oPosOverrideCondition.fromPriceLevel;
		this.toPriceLevel = oPosOverrideCondition.toPriceLevel;
		for(int i = 0; i < 5; i++)
			this.chargeSc[i] = oPosOverrideCondition.chargeSc[i];
		for(int i = 0; i < 25; i++)
			this.chargeTax[i] = oPosOverrideCondition.chargeTax[i];
		this.dpanId = oPosOverrideCondition.dpanId;
		this.checkOrderingType = oPosOverrideCondition.checkOrderingType;
		this.priority = oPosOverrideCondition.priority;
		if (oPosOverrideCondition.startDate != null)
			this.startDate = new DateTime(oPosOverrideCondition.startDate);
		if (oPosOverrideCondition.endDate != null)
			this.endDate = new DateTime(oPosOverrideCondition.endDate);
		if (oPosOverrideCondition.startTime != null)
			this.startTime = new Time(oPosOverrideCondition.startTime.getTime());
		if (oPosOverrideCondition.endTime != null)
			this.endTime = new Time(oPosOverrideCondition.endTime.getTime());
		this.timeBy = oPosOverrideCondition.timeBy;
		this.weekMask = oPosOverrideCondition.weekMask;
		this.holiday = oPosOverrideCondition.holiday;
		this.dayBeforeHoliday = oPosOverrideCondition.dayBeforeHoliday;
		this.specialDay = oPosOverrideCondition.specialDay;
		this.dayBeforeSpecialDay = oPosOverrideCondition.dayBeforeHoliday;
		this.startTable = oPosOverrideCondition.startTable;
		this.endTable = oPosOverrideCondition.endTable;
		this.startTableExt = oPosOverrideCondition.startTableExt;
		this.endTableExt = oPosOverrideCondition.endTableExt;
		this.stgpId = oPosOverrideCondition.stgpId;
		this.orderingType = oPosOverrideCondition.orderingType;
		this.perdId = oPosOverrideCondition.perdId;
		this.sphrId = oPosOverrideCondition.sphrId;
		this.idgpIds = oPosOverrideCondition.idgpIds;
		this.dtypIds = oPosOverrideCondition.dtypIds;
		this.status = oPosOverrideCondition.status;
		this.ctypId = oPosOverrideCondition.ctypId;
	}
	
	//read a list of data from API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray overrideConditionJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("overrideConditions")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("overrideConditions"))
				return null;
			
			overrideConditionJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("overrideConditions");
		}
		return overrideConditionJSONArray;
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
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllOverrideConditionsByOutletId", requestJSONObject.toString());
		return responseJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject overrideConditionJSONObject) {
		JSONObject resulOverrideCondition = null;
		int i;
		SimpleDateFormat oTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		
		resulOverrideCondition = overrideConditionJSONObject.optJSONObject("PosOverrideCondition");
		if(resulOverrideCondition == null)
			resulOverrideCondition = overrideConditionJSONObject;
			
		this.init();

		this.overId = resulOverrideCondition.optInt("over_id");
		this.shopId = resulOverrideCondition.optInt("over_shop_id");
		this.oletId = resulOverrideCondition.optInt("over_olet_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resulOverrideCondition.optString("over_name_l"+i);
		this.fromPrtqId = resulOverrideCondition.optInt("over_from_prtq_id");
		this.toPrtqId = resulOverrideCondition.optInt("over_to_prtq_id");
		this.fromPriceLevel = resulOverrideCondition.optInt("over_from_price_level");
		this.toPriceLevel = resulOverrideCondition.optInt("over_to_price_level");
		for(i=1; i<=5; i++)
			this.chargeSc[(i-1)] = resulOverrideCondition.optString("over_charge_sc"+i, PosOverrideCondition.CHARGE_SC_NO_CHANGE);
		for(i=1; i<=25; i++)
			this.chargeTax[(i-1)] = resulOverrideCondition.optString("over_charge_tax"+i, PosOverrideCondition.CHARGE_TAX_NO_CHANGE);
		this.dpanId = resulOverrideCondition.optInt("over_dpan_id");
		this.checkOrderingType = resulOverrideCondition.optString("over_check_ordering_type");
		this.priority = resulOverrideCondition.optInt("over_priority");

		try {
			String sStartDate = resulOverrideCondition.optString("over_start_date");
			if(!sStartDate.isEmpty())
				this.startDate = m_oDTFormatterYMD.parseDateTime(sStartDate);
			
			String sEndDate = resulOverrideCondition.optString("over_end_date");
			if(!sEndDate.isEmpty())
				this.endDate = m_oDTFormatterYMD.parseDateTime(sEndDate);

			String sStartTime = resulOverrideCondition.optString("over_start_time");
			if(!sStartTime.isEmpty())
				this.startTime = new Time(oTimeFormat.parse(sStartTime).getTime());
			
			String sEndTime = resulOverrideCondition.optString("over_end_time");
			if(!sEndTime.isEmpty())
				this.endTime = new Time(oTimeFormat.parse(sEndTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		this.timeBy = resulOverrideCondition.optString("over_time_by", PosOverrideCondition.TIME_BY_ITEM);
		this.weekMask = resulOverrideCondition.optString("over_week_mask");
		this.holiday = resulOverrideCondition.optString("over_holiday", PosOverrideCondition.HOLIDAY_NO_CHECKING);
		this.dayBeforeHoliday = resulOverrideCondition.optString("over_day_before_holiday", PosOverrideCondition.BEFORE_HOLIDAY_NO_CHECKING);
		this.specialDay = resulOverrideCondition.optString("over_special_day", PosOverrideCondition.SPECIAL_DAY_NO_CHECKING);
		this.dayBeforeSpecialDay = resulOverrideCondition.optString("over_day_before_special_day", PosOverrideCondition.BEFORE_SPECIAL_DAY_NO_CHECKING);
		this.startTable = resulOverrideCondition.optInt("over_start_table");
		this.endTable = resulOverrideCondition.optInt("over_end_table");
		this.startTableExt = resulOverrideCondition.optString("over_start_table_ext", "");
		this.endTableExt = resulOverrideCondition.optString("over_end_table_ext", "");
		this.stgpId = resulOverrideCondition.optInt("over_stgp_id");
		this.orderingType = resulOverrideCondition.optString("over_ordering_type", PosOverrideCondition.ORDERING_TYPE_NO_USE);
		this.perdId = resulOverrideCondition.optInt("over_perd_id");
		this.sphrId = resulOverrideCondition.optInt("over_sphr_id");
		this.idgpIds = resulOverrideCondition.optString("over_idgp_ids");
		this.dtypIds = resulOverrideCondition.optString("over_dtyp_ids");
		this.status = resulOverrideCondition.optString("over_status");
		this.ctypId = resulOverrideCondition.optInt("over_ctyp_id");
	}
	
	//construct the save request JSON
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("over_id", overId);
			addSaveJSONObject.put("over_shop_id", shopId);
			addSaveJSONObject.put("over_olet_id", oletId);
			for(int i = 1; i <= 5; i++) {
				if(!name[(i - 1)].isEmpty())
					addSaveJSONObject.put("over_name_l"+i, name[(i - 1)]);
			}
			addSaveJSONObject.put("over_from_prtq_id", fromPrtqId);
			addSaveJSONObject.put("over_to_prtq_id", toPrtqId);
			addSaveJSONObject.put("over_from_price_level", fromPriceLevel);
			addSaveJSONObject.put("over_to_price_level", toPriceLevel);
			for(int i = 1; i <= 5; i++) {
				if (!chargeSc[(i - 1)].isEmpty())
				 addSaveJSONObject.put("over_charge_sc"+i, chargeSc[(i-1)]);
			}
			for(int i = 1; i <= 25; i++) {
				if (!chargeTax[(i - 1)].isEmpty())
					addSaveJSONObject.put("over_charge_tax"+i, chargeTax[(i - 1)]);
			}
			addSaveJSONObject.put("over_dpan_id", dpanId);
			addSaveJSONObject.put("over_check_ordering_type", checkOrderingType);
			addSaveJSONObject.put("over_priority", priority);

			if (startDate != null)
				addSaveJSONObject.put("over_start_date", dateFormat.format(startDate));
			
			if (endDate != null)
				addSaveJSONObject.put("over_end_date", endDate);
			
			if (startTime != null)
				addSaveJSONObject.put("over_start_time", startTime);
			
			if (endTime != null)
				addSaveJSONObject.put("over_end_time", endTime);
			
			addSaveJSONObject.put("over_time_by", timeBy);
			addSaveJSONObject.put("over_week_mask", weekMask);
			addSaveJSONObject.put("over_holiday", holiday);
			addSaveJSONObject.put("over_day_before_holiday", dayBeforeHoliday);
			addSaveJSONObject.put("over_special_day", specialDay);
			addSaveJSONObject.put("over_day_before_special_day", dayBeforeSpecialDay);
			addSaveJSONObject.put("over_start_table", startTable);
			addSaveJSONObject.put("over_end_table", endTable);
			addSaveJSONObject.put("over_start_table_ext", startTableExt);
			addSaveJSONObject.put("over_end_table_ext", endTableExt);
			addSaveJSONObject.put("over_stgp_id", stgpId);
			addSaveJSONObject.put("over_ordering_type", orderingType);
			addSaveJSONObject.put("over_perd_id", perdId);
			addSaveJSONObject.put("over_sphr_id", sphrId);
			addSaveJSONObject.put("over_idgp_id", idgpIds);
			addSaveJSONObject.put("over_dtyp_id", dtypIds);
			addSaveJSONObject.put("over_status", status);
			addSaveJSONObject.put("over_ctyp_id", ctypId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	protected JSONArray constructMultipleItemAddSaveJSON(List<PosOverrideCondition> oOverrideConditions) {
		JSONArray overrideConditionJSONArray = new JSONArray();

		try {
			for (int i = 0; i < oOverrideConditions.size(); i++) {
				PosOverrideCondition oOverrideCondition = oOverrideConditions.get(i);
	
				boolean bUpdate = (oOverrideCondition.getOverId() > 0)? true: false;
				JSONObject tempOverrideConditionJSONObject = oOverrideCondition.constructAddSaveJSON(bUpdate);
				JSONObject oOverrideConditionJSONObject = new JSONObject();
					oOverrideConditionJSONObject.put("PosOverrideCondition", tempOverrideConditionJSONObject);
				overrideConditionJSONArray.put(oOverrideConditionJSONObject);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return overrideConditionJSONArray;
	}

	//construct the save request JSON
	public String addUpdate(List<PosOverrideCondition> oPosOverrideConditionList) {
		JSONObject addSaveJSONObject = new JSONObject();
		JSONArray addSaveJSONArray = constructMultipleItemAddSaveJSON(oPosOverrideConditionList);
		
		try {
			addSaveJSONObject.put("PosOverrideConditions", addSaveJSONArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//send data to server
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveMultipleOverrideConditions", addSaveJSONObject.toString(), false))
			return OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
		else
			return "";
	}
	
	// init value
	public void init() {
		int i=0;
		m_oDTFormatterYMD = DateTimeFormat.forPattern("yyyy-MM-dd");
		
		this.overId = 0;
		this.shopId = 0;
		this.oletId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = ""; 
		this.fromPrtqId = 0;
		this.toPrtqId = 0;
		this.fromPriceLevel = 0;
		this.toPriceLevel = 0;
		if(this.chargeSc == null)
			this.chargeSc = new String[5];
		for(i=0; i<5; i++)
			this.chargeSc[i] = PosOverrideCondition.CHARGE_SC_NO_CHANGE;
		if(this.chargeTax == null)
			this.chargeTax = new String[25];
		for(i=0; i<25; i++)
			this.chargeTax[i] = PosOverrideCondition.CHARGE_TAX_NO_CHANGE;
		this.dpanId = 0;
		this.checkOrderingType = PosOverrideCondition.CHECK_ORDERING_TYPE_NO_USE;
		this.priority = 0;
		this.startDate = null;
		this.endDate = null;
		this.startTime = null;
		this.endTime = null;
		this.timeBy = PosOverrideCondition.TIME_BY_ITEM;
		this.weekMask = "";
		this.holiday = PosOverrideCondition.HOLIDAY_NO_CHECKING;
		this.dayBeforeHoliday = PosOverrideCondition.BEFORE_HOLIDAY_NO_CHECKING;
		this.specialDay = PosOverrideCondition.SPECIAL_DAY_NO_CHECKING;
		this.dayBeforeSpecialDay = PosOverrideCondition.BEFORE_SPECIAL_DAY_NO_CHECKING;
		this.startTable = 0;
		this.endTable = 0;
		this.startTableExt = "";
		this.endTableExt = "";
		this.stgpId = 0;
		this.orderingType = PosOverrideCondition.ORDERING_TYPE_NO_USE;
		this.checkOrderingMode = PosOverrideCondition.CHECK_ORDERING_MODE_FINE_DINING;
		this.perdId = 0;
		this.sphrId = 0;
		this.idgpIds = "";
		this.dtypIds = "";
		this.status = PosOverrideCondition.STATUS_ACTIVE;
		this.ctypId = 0;
	}
	
	public void setName(int iIndex, String sName) {
		this.name[iIndex-1] = sName;
	}
	
	public void setChargeSc(int iIndex, String sChargeSc) {
		this.chargeSc[iIndex-1] = sChargeSc;
	}
	
	public void setCheckOrderingType(String sCheckOrderingType) {
		this.checkOrderingType = sCheckOrderingType;
	}
	
	public void setPriority(int iPriority) {
		this.priority = iPriority;
	}
	
	public void setCheckOrderingMode(String sOrderingMode) {
		this.checkOrderingMode = sOrderingMode;
	}
	
	public void setWeekMask(String sWeekMask) {
		this.weekMask = sWeekMask;
	}
	
	public void setOrderingType(String sOrderingType) {
		this.orderingType = sOrderingType;
	}
	
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	//get overId
	public int getOverId() {
		return this.overId;
	}
	
	//get shop id
	protected int getShopId() {
		return this.shopId;
	}
	
	//get olet id
	protected int getOletId() {
		return this.oletId;
	}
	
	//get name by lang index
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get from prtq id
	protected int getFromPrtqId() {
		return this.fromPrtqId;
	}
	
	//get to prtq id
	protected int getToPrtqId() {
		return this.toPrtqId;
	}
	
	//get from price level
	public int getFromPriceLevel() {
		return this.fromPriceLevel;
	}
	
	//get to price level
	public int getToPriceLevel() {
		return this.toPriceLevel;
	}
	
	//get all sc override setup
	public String[] getAllChargeSc() {
		return this.chargeSc;
	}
	
	//get charge sc
	public String getChargeSc(int iIndex) {
		return this.chargeSc[(iIndex-1)];
	}
	
	//get all tax override setup
	public String[] getAllChargeTax() {
		return this.chargeTax;
	}
	
	//get charge tax
	public String getChargeTax(int iIndex) {
		return this.chargeTax[(iIndex-1)];
	}
	
	//get dpan id
	public int getDpanId() {
		return this.dpanId;
	}
	
	//get override ordering type
	public String getCheckOrderingType() {
		return this.checkOrderingType;
	}
	
	//get priority
	public int getPriority() {
		return this.priority;
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
	protected String getTimeBy() {
		return this.timeBy;
	}
	
	//check whether time be is check create time
	public boolean isCheckCreatedTimeBy() {
		if(this.timeBy.equals(PosOverrideCondition.TIME_BY_CHECK))
			return true;
		else
			return false;
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
	
	//get start table
	public int getStartTable() {
		return this.startTable;
	}
	
	//get end table
	public int getEndTable() {
		return this.endTable;
	}
	
	//get start table extension
	public String getStartTableExt(){
		return this.startTableExt; 	
	}
	
	//get end table extension
	public String getEndTableExt(){
		return this.endTableExt;
	}
	
	//get stgp id
	public int getStgpId() {
		return this.stgpId;
	}
	
	public boolean isNoRuleForStationGroup() {
		if(this.stgpId == 0)
			return true;
		else
			return false;
	}
	
	//get ordering type
	public String getOrderingType() {
		return this.orderingType;
	}
	
	//check whether no rule for ordering type
	public boolean isNoRuleForOrderingType() {
		if(this.orderingType.isEmpty())
			return true;
		else
			return false;
	}
	
	//check whether takeout ordering type
	public boolean isTakeoutOrderingType() {
		if(this.orderingType.equals(PosOverrideCondition.ORDERING_TYPE_TAKEOUT))
			return true;
		else
			return false;
	}

	//get perd id
	public int getPerdId() {
		return this.perdId;
	}
	
	//check whether no rule for period
	public boolean isNoRuleForPeriod() {
		if(this.perdId == 0)
			return true;
		else
			return false;
	}
	
	public int getCtypId() {
		return this.ctypId;
	}
	public boolean isNoRuleForCustomType() {
		if(this.ctypId == 0)
			return true;
		else
			return false;
	}
	
	//get sphr id
	public int getSphrId() {
		return this.sphrId;
	}
	
	//get idgp ids
	public String[] getIdgpIds() {
		if(this.idgpIds.isEmpty())
			return null;
		String[] itemDepGroupIds = this.idgpIds.split(",");
		return itemDepGroupIds;
	}
	
	//get dtyp id
	public String[] getDtypIds() {
		if(this.dtypIds.isEmpty())
			return null;
		String[] discountTypeIds = this.dtypIds.split(",");
		return discountTypeIds;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public boolean isActive() {
		return status.equals(STATUS_ACTIVE);
	}
	
	//check whether no rule for special hour
	public boolean isNoRuleForSpecialHour() {
		if(this.sphrId == 0)
			return true;
		else
			return false;
	}
	
	public String getOrderingMode() {
		return this.checkOrderingMode;
	}
	
	public boolean isFineDiningOrderingMode() {
		return this.checkOrderingMode.equals(PosOverrideCondition.CHECK_ORDERING_MODE_FINE_DINING);
	}
	
	public boolean isFastFoodOrderingMode() {
		return this.checkOrderingMode.equals(PosOverrideCondition.CHECK_ORDERING_MODE_FAST_FOOD);
	}
}
