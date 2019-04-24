package om;

import java.math.BigDecimal;
import java.util.Locale;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;

public class ResvResv {
	private String resvId;
	private int shopId;
	private int oletId;
	private int txn;
	private String refnoPrefix;
	private int refno;
	private String refnoWithPrefix;
	private DateTime bookDate;
	private Time bookTime;
	private int guests;
	private int children;
	private String lastName;
	private String firstName;
	private String salutation;
	private String gender;
	private String phone;
	private String phoneAreaCode;
	private String email;
	private String member;
	private String request;
	private String[] acceptTC;
	private String remark;
	private int ordrId;
	private int menuId;
	private DateTime createTime;
	private DateTime modifyTime;
	private DateTime expiryDate;
	private BigDecimal paymentTotal;
	private String paymentStatus;
	private String cancelStatus;
	private String bookingStatus;
	private String arrivalStatus;
	private DateTime arrivalTime;
	private String log;
	private String status;
	private DateTimeFormatter m_oDTFormatterYMD;
	
	// salutation
	public static String SALUTATION_NOT_PROVIDED = "";
	
	// gender
	public static String GENDER_NOT_PROVIDED = "";
	public static String GENDER_MALE = "m";
	public static String GENDER_FEMALE = "f";
	
	// acceptTC
	public static String ACCEPT_TERMS_AND_CONDITIONS_NO = "";
	public static String ACCEPT_TERMS_AND_CONDITIONS_YES = "y";
	
	// paymentStatus
	public static String PAYMENT_STATUS_NO_PAYMENT = "";
	public static String PAYMENT_STATUS_PAID = "p";
	
	// cancelStatus
	public static String CANCEL_STATUS_NORMAL = "";
	public static String CANCEL_STATUS_CANCELLED = "c";
	public static String CANCEL_STATUS_VOID = "v";
	
	// bookingStatus
	public static String BOOKING_STATUS_NORMAL = "";
	public static String BOOKING_STATUS_OVERBOOK_LIST = "o";
	public static String BOOKING_STATUS_WAITLIST = "w";
	
	
	// arrivalStatus
	public static String ARRIVAL_STATUS_NA = "";
	public static String ARRIVAL_STATUS_MARK_ARRIVAL = "a";
	public static String ARRIVAL_STATUS_NO_SHOW = "n";
	
	// status
	public static String STATUS_ACITVE = "";
	public static String STATUS_EXPIRED = "e";
	
	// requestInfo
	public static String REQUEST_INFO_RESERVATION = "r";
	public static String REQUEST_INFO_PAYMENT = "p";
	public static String REQUEST_INFO_PREORDER = "o";
	
	public ResvResv() {
		this.init();
	}
	
	public ResvResv(JSONObject oResvJSONObject) {
		this.readDataFromJson(oResvJSONObject);
	}

	// Retrieve preorder by table number
	public JSONObject retrieveResvByDateConfirmno(int iOutletId, String sDate, String sRefNo, String sRequestInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("bookDate", sDate);
			requestJSONObject.put("confirmNo", sRefNo);
			requestJSONObject.put("requestInfo", sRequestInfo);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "reservation", "getResvByOutletConfirmNo", requestJSONObject.toString(), true))
			return null;
		else {
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("resv"))
				return null;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("resv"))
				return null;
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
		}
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject resvJSONObject) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		JSONObject resultResv = null;
		
		resultResv = resvJSONObject.optJSONObject("ResvResv");
		if(resultResv == null)
			resultResv = resvJSONObject;
			
		this.init();
		this.resvId = resultResv.optString("resv_id");
		this.shopId = resultResv.optInt("resv_shop_id");
		this.oletId = resultResv.optInt("resv_olet_id");
		this.txn = resultResv.optInt("'resv_txn");
		this.refnoPrefix = resultResv.optString("resv_refno_prefix");
		this.refno = resultResv.optInt("resv_refno");
		this.refnoWithPrefix = resultResv.optString("resv_refno_with_prefix");

		String sBookDate = resultResv.optString("resv_book_date");
		if (!sBookDate.isEmpty()) {
			try {
				this.bookDate = m_oDTFormatterYMD.parseDateTime(sBookDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String sBookTime = resultResv.optString("resv_book_time");
		if (!sBookTime.isEmpty()) {
			try {
				this.bookTime = new Time(timeFormat.parse(sBookTime).getTime());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
				
		this.guests = resultResv.optInt("resv_guests");
		this.children = resultResv.optInt("resv_children");
		this.lastName = resultResv.optString("resv_last_name");
		this.firstName = resultResv.optString("resv_first_name");
		this.salutation = resultResv.optString("resv_salutation", ResvResv.SALUTATION_NOT_PROVIDED);
		this.gender = resultResv.optString("resv_gender", ResvResv.GENDER_NOT_PROVIDED);
		this.phone = resultResv.optString("resv_phone");
		this.phoneAreaCode = resultResv.optString("resv_phone_area_code");
		this.email = resultResv.optString("resv_email");
		this.member = resultResv.optString("resv_member");
		this.request = resultResv.optString("resv_request");
		for (int i = 1; i <= 3; i++) {
			this.acceptTC[i-1] = resultResv.optString("'resv_accept_tc"+i, ResvResv.ACCEPT_TERMS_AND_CONDITIONS_NO);
		}
		this.remark = resultResv.optString("resv_remark");
		this.ordrId = resultResv.optInt("resv_ordr_id");
		this.menuId = resultResv.optInt("resv_menu_id");
		
		String sCreateTime = resultResv.optString("resv_create_time");
		if(!sCreateTime.isEmpty())
			this.createTime = oFmt.parseDateTime(sCreateTime);

		String sModifyTime = resultResv.optString("resv_modify_time");
		if(!sModifyTime.isEmpty())
			this.modifyTime = oFmt.parseDateTime(sModifyTime);

		String sExpiryDate = resultResv.optString("'resv_expiry_date");
		if (!sExpiryDate.isEmpty()) {
			try {
				this.expiryDate = m_oDTFormatterYMD.parseDateTime(sExpiryDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.paymentTotal = new BigDecimal(resultResv.optString("resv_payment_total", "0.0"));
		this.paymentStatus = resultResv.optString("resv_payment_status", ResvResv.PAYMENT_STATUS_NO_PAYMENT);
		this.cancelStatus = resultResv.optString("resv_cancel_status", ResvResv.CANCEL_STATUS_NORMAL);
		this.bookingStatus = resultResv.optString("resv_booking_status", ResvResv.BOOKING_STATUS_NORMAL);
		this.arrivalStatus = resultResv.optString("resv_arrival_status", ResvResv.ARRIVAL_STATUS_NA);

		String sArrivalTime = resultResv.optString("resv_arrival_time");
		if(!sArrivalTime.isEmpty())
			this.arrivalTime = oFmt.parseDateTime(sArrivalTime);
		
		this.log = resultResv.optString("resv_log");
		this.status = resultResv.optString("resv_status", ResvResv.STATUS_ACITVE);
	}
	
	private void init() {
		m_oDTFormatterYMD = DateTimeFormat.forPattern("yyyy-MM-dd");
		
		this.resvId = "";
		this.shopId = 0;
		this.oletId = 0;
		this.txn = 0;
		this.refnoPrefix = "";
		this.refno = 0;
		this.refnoWithPrefix = "";
		this.bookDate = null;
		this.bookTime = null;
		this.guests = 0;
		this.children = 0;
		this.lastName = "";
		this.firstName = "";
		this.salutation = ResvResv.SALUTATION_NOT_PROVIDED;
		this.gender = ResvResv.GENDER_NOT_PROVIDED;
		this.phone = "";
		this.phoneAreaCode = "";
		this.email = "";
		this.member = "";
		this.request = null;
		if(this.acceptTC == null)
			this.acceptTC = new String[3];
		for(int i=0; i<3; i++)
			this.acceptTC[i] = "";
		this.remark = null;
		this.ordrId = 0;
		this.menuId = 0;
		this.createTime = null;
		this.modifyTime = null;
		this.expiryDate = null;
		this.paymentTotal = BigDecimal.ZERO;
		this.paymentStatus = ResvResv.PAYMENT_STATUS_NO_PAYMENT;
		this.cancelStatus = ResvResv.CANCEL_STATUS_NORMAL;
		this.bookingStatus = ResvResv.BOOKING_STATUS_NORMAL;
		this.arrivalStatus = ResvResv.ARRIVAL_STATUS_NA;
		this.arrivalTime = null;
		this.log = null;
		this.status = ResvResv.STATUS_ACITVE;
	}
	
	public String getRefNoWithPrefix() {
		return this.refnoWithPrefix;
	}
	
	public DateTime getBookDate() {
		return this.bookDate;
	}
	
	public Time getBookTime() {
		return this.bookTime;
	}
	
	public int getGuests() {
		return this.guests;
	}
	
	public int getChildren() {
		return this.children;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getSalutation() {
		return this.salutation;
	}
	
	public String getGender() {
		return this.gender;
	}

	public String getPhone() {
		return this.phone;
	}
	
	public String getRequest() {
		return this.request;
	}
	
	public String getRemark() {
		return this.remark;
	}
	
	public BigDecimal getPaymentTotal() {
		return this.paymentTotal;
	}
}
