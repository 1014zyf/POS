//Database: pos_payment_acls - Access control list for payments, outlets & dates
package om;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import app.AppGlobal;

public class PosPaymentAcl {
	private int paclId;
	private int paygId;
	private int oletId;
	private String allow;
	private DateTime startDate;
	private DateTime endDate;
	private String weekMask;
	private String holiday;
	private String dayBeforeHoliday;
	private String specialDay;
	private String dayBeforeSpecialDay;
	private DateTimeFormatter m_oDTFormatterYMD;
	
	// allow 
	public static String ALLOW_NO = "";
	public static String ALLOW_YES = "y";
	
	// holiday
	public static String HOLIDAY_NO_CHECKING = "";
	public static String HOLIDAY_ALLOW = "y";
	public static String HOLIDAY_ALLOW_VALID_WEEK_MASK = "z";
	public static String HOLIDAY_NOT_ALLOW = "n";
	public static String HOLIDAY_NOT_ALLOW_NOT_VALID_WEEK_MASK = "x";
	
	// dayBeforeHoliday
	public static String DAY_BEFORE_HOLIDAY_NO_CHECKING = "";
	public static String DAY_BEFORE_HOLIDAY_ALLOW = "y";
	public static String DAY_BEFORE_HOLIDAY_ALLOW_VALID_WEEK_MASK = "z";
	public static String DAY_BEFORE_HOLIDAY_NOT_ALLOW = "n";
	public static String DAY_BEFORE_HOLIDAY_NOT_ALLOW_NOT_VALID_WEEK_MASK = "x";
	
	// specialDay
	public static String SPECIAL_DAY_NO_CHECKING = "";
	public static String SPECIAL_DAY_ALLOW = "y";
	public static String SPECIAL_DAY_ALLOW_VALID_WEEK_MASK = "z";
	public static String SPECIAL_DAY_NOT_ALLOW = "n";
	public static String SPECIAL_DAY_NOT_ALLOW_NOT_VALID_WEEK_MASK = "x";
	
	// dayBeforeSpecialDay
	public static String DAY_BEFORE_SPECIAL_DAY_NO_CHECKING = "";
	public static String DAY_BEFORE_SPECIAL_DAY_ALLOW = "y";
	public static String DAY_BEFORE_SPECIAL_DAY_ALLOW_VALID_WEEK_MASK = "z";
	public static String DAY_BEFORE_SPECIAL_DAY_NOT_ALLOW = "n";
	public static String DAY_BEFORE_SPECIAL_DAY_NOT_ALLOW_NOT_VALID_WEEK_MASK = "x";
	
	//init object with initialize value
	public PosPaymentAcl () {
		this.init();
	}

	//init object with JSON object
	public PosPaymentAcl(JSONObject payAclJSONObject) {
		this.readDataFromJson(payAclJSONObject);
	}

	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray paymentAclJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("paymentAcls")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("paymentAcls"))
				return null;
			
			paymentAclJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("paymentAcls");
		}
		
		return paymentAclJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject payAclJSONObject) {
		JSONObject resultPaymentAcl = null;
		
		resultPaymentAcl = payAclJSONObject.optJSONObject("PosPaymentAcl");
		if(resultPaymentAcl == null)
			resultPaymentAcl = payAclJSONObject;
			
		
		this.init();
		this.paclId = resultPaymentAcl.optInt("pacl_id");
		this.paygId = resultPaymentAcl.optInt("pacl_payg_id");
		this.oletId = resultPaymentAcl.optInt("pacl_olet_id");
		this.allow = resultPaymentAcl.optString("pacl_allow", PosPaymentAcl.ALLOW_NO);

		try {
			String sStartDate = resultPaymentAcl.optString("pacl_start_date");
			if(!sStartDate.isEmpty())
				this.startDate = m_oDTFormatterYMD.parseDateTime(sStartDate);
			
			String sEndDate = resultPaymentAcl.optString("pacl_end_date");
			if(!sEndDate.isEmpty())
				this.endDate = m_oDTFormatterYMD.parseDateTime(sEndDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.weekMask = resultPaymentAcl.optString("pacl_week_mask");
		this.holiday = resultPaymentAcl.optString("pacl_holiday", PosPaymentAcl.HOLIDAY_NO_CHECKING);
		this.dayBeforeHoliday = resultPaymentAcl.optString("pacl_day_before_holiday", PosPaymentAcl.DAY_BEFORE_HOLIDAY_NO_CHECKING);
		this.specialDay = resultPaymentAcl.optString("pacl_special_day", PosPaymentAcl.SPECIAL_DAY_NO_CHECKING);
		this.dayBeforeSpecialDay = resultPaymentAcl.optString("pacl_day_before_special_day", PosPaymentAcl.DAY_BEFORE_SPECIAL_DAY_NO_CHECKING);
	}

	// init value
	public void init () {
		m_oDTFormatterYMD = DateTimeFormat.forPattern("yyyy-MM-dd");
		
		this.paclId = 0;
		this.paygId = 0;
		this.oletId = 0;
		this.allow = PosPaymentAcl.ALLOW_NO;
		this.startDate = null;
		this.endDate = null;
		this.weekMask = "";
		this.holiday = PosPaymentAcl.HOLIDAY_NO_CHECKING;
		this.dayBeforeHoliday = PosPaymentAcl.DAY_BEFORE_HOLIDAY_NO_CHECKING;
		this.specialDay = PosPaymentAcl.SPECIAL_DAY_NO_CHECKING;
		this.dayBeforeSpecialDay = PosPaymentAcl.DAY_BEFORE_SPECIAL_DAY_NO_CHECKING;
	}

	//Init object from database by pacl_id
	public PosPaymentAcl (int iPaclId) {
		this.paclId = iPaclId;
	}
	
	//read data from database by pacl_id
	public void readById(int iPaclId) {
		this.paclId = iPaclId;
	}
	
	//add new payment acl record to database
	public boolean add() {
		return true;
	}
	
	//update payment acl record to database
	public boolean update() {
		return true;
	}
	
	//get paclId
	public int getPaclId() {
		return this.paclId;
	}
	
	//get payg id
	public int getPaygId() {
		return this.paygId;
	}
	
	//get olet id
	public int getOletId() {
		return this.oletId;
	}
	
	//get allow
	public String getAllow() {
		return this.allow;
	}
	
	//get start date
	public DateTime getStartDate() {
		return this.startDate;
	}
	
	//get end date
	public DateTime getEndDate() {
		return this.endDate;
	}
	
	//get week mask
	public String getWeekMask() {
		return this.weekMask;
	}
	
	//get holiday
	public String getHoliday() {
		return this.holiday;
	}
	
	//get day before holiday
	public String getDayBeforeHoliday() {
		return this.dayBeforeHoliday;
	}
	
	//get special day
	public String getSpecialDay() {
		return this.specialDay;
	}
	
	//get day before special day
	public String getDayBeforeSpecialDay() {
		return this.dayBeforeSpecialDay;
	}
}
