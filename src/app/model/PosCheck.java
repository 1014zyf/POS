// Database : pos_checks - Sales Check
package app.model;

import java.math.BigDecimal;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosCheck {
	private int chksId;
	private int bdayId;
	private int bperId;
	private int shopId;
	private int oletId;
	private int sectId;
	private String checkPrefix;
	private int checkNo;
	private String checkPrefixNo;
	private int ctblId;
	private int guests;
	private int children;
	private int printCount;
	private int receiptPrintCount;
	private int partyCount;
	private BigDecimal checkTotal;
	private BigDecimal itemTotal;
	private BigDecimal [] sc;
	private BigDecimal [] tax;
	private BigDecimal [] inclTaxRef;
	private BigDecimal preDisc;
	private BigDecimal midDisc;
	private BigDecimal postDisc;
	private BigDecimal roundAmount;
	private BigDecimal paymentTotal;
	private BigDecimal tipsTotal;
	private String paid;
	private Date resvBookDate;
	private String refnoWithPrefix;
	private String orderingType;
	private String orderingMode;
	private String nonRevenue;
	private int membId;
	private String remark;
	private int settleShiftNo;
	private String openTime;
	private DateTime openLocTime;
	private int openUserId;
	private int openStatId;
	private String closeTime;
	private DateTime closeLocTime;
	private int closeUserId;
	private int closeStatId;
	private int closeBperId;
	private String modifiedTime;
	private DateTime modifiedLocTime;
	private int modifiedUserId;
	private int modifiedStatId;
	private String printTime;
	private DateTime printLocTime;
	private int printUserId;
	private int printStatId;
	private String lockTime;
	private DateTime lockLocTime;
	private int lockUserId;
	private int lockStatId;
	private String voidTime;
	private DateTime voidLocTime;
	private int voidUserId;
	private int voidStatId;
	private int voidVdrsId;
	private String status;
	
	private boolean bModified;
	private PosCheckTable checkTable;
	private List<PosTaiwanGuiTran> taiwanGuiTranList;
	private List<PosCheckParty> checkParties;
	private List<PosCheckPayment> checkPayments;
	private List<PosCheckDiscount> checkDiscountList;
	private List<PosCheckExtraInfo> checkExtraInfoList;
	
	private List<JSONObject> m_oPartyInfosList;
	private List<JSONObject> m_oNewItemInfosList;
	private List<Integer> m_oNewPaymentIdsList;
	private List<JSONObject> m_oNewCheckExtraInfoInfosList;
	private List<JSONObject> m_oCheckDiscountIdsList;
	
	private JSONArray m_oReturnJSONArray;
	
	// paid
	public static String PAID_NOT_PAID = "";
	public static String PAID_PARTIAL_PAID = "p";
	public static String PAID_FULL_PAID = "f";
	
	// ordering type
	public static String ORDERING_TYPE_NORMAL = "";
	public static String ORDERING_TYPE_TAKEOUT = "t";
	public static String ORDERING_TYPE_DELIVERY = "d";
	
	// ordering mode
	public static String ORDERING_MODE_FINE_DINING = "";
	public static String ORDERING_MODE_FAST_FOOD = "f";
	public static String ORDERING_MODE_DELIVERY = "d";
	public static String ORDERING_MODE_SELF_ORDER_KIOSK = "k";
	
	// non revenue
	public static String NON_REVENUE_NO = "";
	public static String NON_REVENUE_PAYMENT = "y";
	public static String NON_REVENUE_ADVANCE_ORDER = "a";
	
	// status
	public static String STATUS_NORMAL = "";
	public static String STATUS_DELETED = "d";
	public static String STATUS_LOCKED = "l";
	
	// API result status
	public static String API_RESULT_SUCCESS = "s";
	public static String API_RESULT_INVALID_BDAY = "i";
	public static String API_RESULT_MISSING_BDAY = "m";
	public static String API_RESULT_FAIL = "f";
	
	// Printing Type
	public static String PRINTING_NORMAL = "0";
	public static String PRINTING_PREVIEW = "1";
	public static String PRINTING_SERVING_LIST = "2";
	
	public PosCheck(){
		this.init();
	}

	//init object with default value and current user ID
	public PosCheck(int iUserId){
		this.init();
		
		this.openUserId = iUserId;
	}
	
	//init object form JSON
	public PosCheck(JSONObject checkJSONObject) {
		readDataFromJson(checkJSONObject);
	}
	
	//read data from POS API
	private String readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		String sResult = API_RESULT_SUCCESS;
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false)) {
			if (OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage().equals("invalid_business_day"))
				sResult = API_RESULT_INVALID_BDAY;
			else if (OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage().equals("missing_active_business_day"))
				sResult = API_RESULT_MISSING_BDAY;
			else
				sResult = API_RESULT_FAIL;
		} else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return API_RESULT_FAIL;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("check")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return API_RESULT_FAIL;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("check")) {
				this.init();
				return API_RESULT_FAIL;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("check");
			if(tempJSONObject.isNull("PosCheck")) {
				this.init();
				return API_RESULT_FAIL;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return sResult;
	}	

	//read data from response JSON
	private void readDataFromJson(JSONObject posCheckJSONObject) {
		JSONObject resultCheck = null;
		JSONArray tempJSONArray = null;
		PosCheckParty oCheckParty = null;
		PosCheckItem oCheckItem = null;
		PosCheckPayment oCheckPayment = null;
		PosCheckDiscount oCheckDiscount = null;
		
		int i;
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		
		resultCheck = posCheckJSONObject.optJSONObject("PosCheck");
		if(resultCheck == null)
			resultCheck = posCheckJSONObject;

		this.init();
		this.chksId = resultCheck.optInt("chks_id");
		this.bdayId = resultCheck.optInt("chks_bday_id");
		this.bperId = resultCheck.optInt("chks_bper_id");
		this.shopId = resultCheck.optInt("chks_shop_id");
		this.oletId = resultCheck.optInt("chks_olet_id");
		this.sectId = resultCheck.optInt("chks_sect_id");
		this.checkPrefix = resultCheck.optString("chks_check_prefix");
		this.checkNo = resultCheck.optInt("chks_check_num");
		this.checkPrefixNo = resultCheck.optString("chks_check_prefix_num");
		this.ctblId = resultCheck.optInt("chks_ctbl_id");
		this.guests = resultCheck.optInt("chks_guests");
		this.children = resultCheck.optInt("chks_children");
		this.printCount = resultCheck.optInt("chks_print_count");
		this.receiptPrintCount = resultCheck.optInt("chks_receipt_print_count");
		this.partyCount = resultCheck.optInt("chks_party_count");
		this.checkTotal = new BigDecimal(resultCheck.optString("chks_check_total", "0.0"));
		this.itemTotal = new BigDecimal(resultCheck.optString("chks_item_total", "0.0"));
		for (i = 1; i <= 5; i++) {
			this.sc[i-1] = new BigDecimal(resultCheck.optString("chks_sc"+i, "0.0"));
		}
		for (i = 1; i <= 25; i++) {
			this.tax[i-1] = new BigDecimal(resultCheck.optString("chks_tax"+i, "0.0"));
		}
		for (i = 1; i <= 4; i++) {
			this.inclTaxRef[i-1] = new BigDecimal(resultCheck.optString("chks_incl_tax_ref"+i, "0.0"));
		}
		this.preDisc = new BigDecimal(resultCheck.optString("chks_pre_disc", "0.0"));
		this.midDisc = new BigDecimal(resultCheck.optString("chks_mid_disc", "0.0"));
		this.postDisc = new BigDecimal(resultCheck.optString("chks_post_disc", "0.0"));
		this.roundAmount = new BigDecimal(resultCheck.optString("chks_round_amount", "0.0"));
		this.paymentTotal = new BigDecimal(resultCheck.optString("chks_payment_total", "0.0"));
		this.tipsTotal = new BigDecimal(resultCheck.optString("chks_tips_total", "0.0"));
		this.paid = resultCheck.optString("chks_paid", PosCheck.PAID_NOT_PAID);

		String sResvBookDate = resultCheck.optString("chks_resv_book_date");
		if (!sResvBookDate.isEmpty()) {
			try {
				this.resvBookDate = new Date(dateFormat.parse(sResvBookDate).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		this.refnoWithPrefix = resultCheck.optString("chks_resv_refno_with_prefix");
		this.orderingType = resultCheck.optString("chks_ordering_type", PosCheck.ORDERING_TYPE_NORMAL);
		this.orderingMode = resultCheck.optString("chks_ordering_mode", PosCheck.ORDERING_MODE_FINE_DINING);
		this.nonRevenue = resultCheck.optString("chks_non_revenue", PosCheck.NON_REVENUE_NO);
		this.membId = resultCheck.optInt("chks_memb_id");
		
		this.remark = resultCheck.optString("chks_remark", null);
		
		this.settleShiftNo = resultCheck.optInt("chks_settle_shift_num");
		
		String sOpenLocTime = resultCheck.optString("chks_open_loctime");
		if(!sOpenLocTime.isEmpty())
			this.openLocTime = oFmt.parseDateTime(sOpenLocTime);

		this.openTime = resultCheck.optString("chks_open_time", oFmt.print(openLocTime.withZone(DateTimeZone.UTC)));
		
		this.openUserId = resultCheck.optInt("chks_open_user_id");
		this.openStatId = resultCheck.optInt("chks_open_stat_id");
		
		this.closeTime = resultCheck.optString("chks_close_time", null);
		
		String sCloseLocTime = resultCheck.optString("chks_close_loctime");
		if(!sCloseLocTime.isEmpty())
			this.closeLocTime = oFmt.parseDateTime(sCloseLocTime);
		
		this.closeUserId = resultCheck.optInt("chks_close_user_id");
		this.closeStatId = resultCheck.optInt("chks_close_stat_id");
		this.closeBperId = resultCheck.optInt("chks_close_bper_id");
		
		this.modifiedTime = resultCheck.optString("chks_modified_time", null);
		String sModifiedLocTime = resultCheck.optString("chks_modified_loctime");
		if(!sModifiedLocTime.isEmpty())
			this.modifiedLocTime = oFmt.parseDateTime(sModifiedLocTime);
		
		this.modifiedUserId = resultCheck.optInt("chks_modified_user_id");
		this.modifiedStatId = resultCheck.optInt("chks_modified_stat_id");
		
		this.printTime = resultCheck.optString("chks_print_time", null);
		String sPrintLocTime = resultCheck.optString("chks_print_loctime");
		if(!sPrintLocTime.isEmpty())
			this.printLocTime = oFmt.parseDateTime(sPrintLocTime);
		
		this.printUserId = resultCheck.optInt("chks_print_user_id");
		this.printStatId = resultCheck.optInt("chks_print_stat_id");
		
		this.lockTime = resultCheck.optString("chks_lock_time", null);
		String sLockLocTime = resultCheck.optString("chks_lock_loctime");
		if(!sLockLocTime.isEmpty())
			this.lockLocTime = oFmt.parseDateTime(sLockLocTime);
		
		this.lockUserId = resultCheck.optInt("chks_lock_user_id");
		this.lockStatId = resultCheck.optInt("chks_lock_stat_id");
		
		this.voidTime = resultCheck.optString("chks_void_time", null);
		String sVoidLocTime = resultCheck.optString("chks_void_loctime");
		if(!sVoidLocTime.isEmpty())
			this.voidLocTime = oFmt.parseDateTime(sVoidLocTime);
		
		this.voidUserId = resultCheck.optInt("chks_void_user_id");
		this.voidStatId = resultCheck.optInt("chks_void_stat_id");
		this.voidVdrsId = resultCheck.optInt("chks_void_vdrs_id");
		this.status = resultCheck.optString("chks_status", PosCheck.STATUS_NORMAL);
		
		JSONArray taiwanGuiTranJSONArray = posCheckJSONObject.optJSONArray("PosTaiwanGuiTran");
		if(taiwanGuiTranJSONArray != null && taiwanGuiTranJSONArray.length() > 0) {
			if(this.taiwanGuiTranList == null)
				this.taiwanGuiTranList = new ArrayList<PosTaiwanGuiTran>();
			
			for(i=0; i<taiwanGuiTranJSONArray.length(); i++) {
				PosTaiwanGuiTran oTempTaiwanGuiTran = new PosTaiwanGuiTran(taiwanGuiTranJSONArray.optJSONObject(i));
				if(oTempTaiwanGuiTran != null)
					this.taiwanGuiTranList.add(oTempTaiwanGuiTran);
			}
		}
		
		//check whether pos check table
		JSONObject tempJSONObject = posCheckJSONObject.optJSONObject("PosCheckTable");
		if (tempJSONObject != null) {
			this.checkTable = new PosCheckTable(tempJSONObject);
		}
		
		HashMap<Integer, PosCheckParty> oPosCheckPartyList = new HashMap<Integer, PosCheckParty>();
		//default have a check party
		oCheckParty = new PosCheckParty();
		this.checkParties.add(oCheckParty);
		oPosCheckPartyList.put(0, oCheckParty);
		
		//check whether pos check party record exist
		tempJSONArray = posCheckJSONObject.optJSONArray("PosCheckParty");
		if (tempJSONArray != null) {
			for(i=0; i<tempJSONArray.length(); i++) {
				JSONObject oCheckPartyJSONObject = tempJSONArray.optJSONObject(i);
				if(oCheckPartyJSONObject != null) {
					oCheckParty = new PosCheckParty(oCheckPartyJSONObject);
					this.checkParties.add(oCheckParty);
					oPosCheckPartyList.put(oCheckParty.getCptyId(), oCheckParty);
				}
			}
		}
		
		//check whether pos check item record exist
		tempJSONArray = posCheckJSONObject.optJSONArray("PosCheckItem");
		if (tempJSONArray != null) {
			for(i=0; i<tempJSONArray.length(); i++) {
				JSONObject oCheckItemJSONObject = tempJSONArray.optJSONObject(i);
				if(oCheckItemJSONObject != null) {
					oCheckItem = new PosCheckItem(oCheckItemJSONObject);
					if(oPosCheckPartyList.containsKey(oCheckItem.getCptyId())){
						oCheckParty = oPosCheckPartyList.get(oCheckItem.getCptyId());
						oCheckParty.getCheckItemList().add(oCheckItem);
					}
				}
			}
		}
		
		//check whether pos check discount record exist
		tempJSONArray = posCheckJSONObject.optJSONArray("PosCheckDiscount");
		if(tempJSONArray != null) {
			for(i=0; i<tempJSONArray.length(); i++) {
				JSONObject oCheckDiscountJSONObject = tempJSONArray.optJSONObject(i);
				if(oCheckDiscountJSONObject != null) {
					oCheckDiscount = new PosCheckDiscount(oCheckDiscountJSONObject);
					oCheckParty.addCheckDiscountList(oCheckDiscount);
				}
			}
		}
		
		//TODO
		//check whether pos check payment
		tempJSONArray = posCheckJSONObject.optJSONArray("PosCheckPayment");
		if (tempJSONArray != null) {
			for(i=0; i<tempJSONArray.length(); i++) {
				JSONObject oCheckPaymentJSONObject = tempJSONArray.optJSONObject(i);
				if(oCheckPaymentJSONObject != null) {
					oCheckPayment = new PosCheckPayment(oCheckPaymentJSONObject);
					this.checkPayments.add(oCheckPayment);
				}
			}
		}
		
		//check whether pos check extra info record exist
		tempJSONArray = posCheckJSONObject.optJSONArray("PosCheckExtraInfo");
		if(tempJSONArray != null) {
			PosCheckExtraInfoList oCheckExtraInfoList = new PosCheckExtraInfoList(tempJSONArray);
			this.checkExtraInfoList = oCheckExtraInfoList.getCheckExtraInfoList();
		}
	}
		
	//change DateTime object to string for database insertion/update
	private String dateTimeToString (DateTime oDateTime) {
		if (oDateTime == null)
			return "";
		
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		String timeString = fmt.print(oDateTime);

		return timeString;
	}
	
	//read data form database with check number 
	public String readByCheckNo(int iBdayId, int iOletId, String sCheckPrefixNo, int isRecursive) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("businessDayId", iBdayId);
			requestJSONObject.put("outletId", iOletId);
			requestJSONObject.put("checkPrefixNum", sCheckPrefixNo);
			requestJSONObject.put("recursive", isRecursive);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "getCheckByCheckNum", requestJSONObject.toString());
	}
	
	public String getCheckIdByTable(int iBdayId, int iOletId, int iTable, String sTableExtension, int isRecursive) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("businessDayId", iBdayId);
			requestJSONObject.put("outletId", Integer.toString(iOletId));
			requestJSONObject.put("table", iTable);
			requestJSONObject.put("tableExtension", sTableExtension);
			requestJSONObject.put("recursive", isRecursive);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "getCheckByTable", requestJSONObject.toString());
	}

	public boolean getCheckByCloseUserId(int iBdayId, int iCloseUserId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("businessDayId", iBdayId);
			requestJSONObject.put("closeUserId", iCloseUserId);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if(!this.readDataFromApi("gm", "pos", "getCheckByCloseUserId", requestJSONObject.toString()).equals(API_RESULT_SUCCESS))
			return false;
		
		return true;
	}

	public boolean getCheckByResvDateRefNo(String sResvDate, String sRefno) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("resvDate", sResvDate);
			requestJSONObject.put("resvRefno", sRefno);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if(!this.readDataFromApi("gm", "pos", "getCheckByResvDateRefNo", requestJSONObject.toString()).equals(API_RESULT_SUCCESS))
			return false;
		
		return true;
	}
	
	public JSONArray getCheckListByBusinessDayPaid(int iBdayId, int iStatId, String sPaid, boolean bCheckHasCheckDiscount) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		 
		try {
			requestJSONObject.put("businessDayId", iBdayId);
			requestJSONObject.put("stationId", iStatId);
			requestJSONObject.put("paid", sPaid);
			if(bCheckHasCheckDiscount)
				requestJSONObject.put("checkHasCheckDiscount", true);
			requestJSONObject.put("recursive", "1");
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getCheckListByBusinessDayPaid", requestJSONObject.toString());
		
		return responseJSONArray;
	}

	public JSONArray getVoidCheckListByBusinessDayOutlet(int iBdayId, int iOletId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		 
		try {
			requestJSONObject.put("businessDayId", iBdayId);
			requestJSONObject.put("outletId", Integer.toString(iOletId));
			requestJSONObject.put("recursive", "0");
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getVoidCheckListByBusinessDayOutlet", requestJSONObject.toString());
		
		return responseJSONArray;
	}
	
	public JSONArray getLastPaidCheckListByStatId(int iBdayId, int iOletId, int iStatId, int iCheckCount) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		 
		try {
			requestJSONObject.put("businessDayId", iBdayId);
			requestJSONObject.put("outletId", Integer.toString(iOletId));
			requestJSONObject.put("statId", iStatId);
			requestJSONObject.put("checkCount", iCheckCount);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getLastPaidCheckListByStatId", requestJSONObject.toString());
		
		return responseJSONArray;
	}

	public JSONArray getCheckListByTable(int iBdayId, int iOletId, int iTable, String sTableExtension) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		 
		try {
			requestJSONObject.put("businessDayId", iBdayId);
			requestJSONObject.put("outletId", Integer.toString(iOletId));
			requestJSONObject.put("table", iTable);
			requestJSONObject.put("tableExtension", sTableExtension);
			requestJSONObject.put("recursive", 0);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getCheckListByTable", requestJSONObject.toString());
		
		return responseJSONArray;
	}

	public JSONArray getCheckListByCloseUserId(int iBdayId, int iCloseUserId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		 
		try {
			requestJSONObject.put("businessDayId", iBdayId);
			requestJSONObject.put("closeUserId", iCloseUserId);
			requestJSONObject.put("recursive", 1);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getCheckListByCloseUserId", requestJSONObject.toString());
		
		return responseJSONArray;
	}
	
	// get check list which is not paid and printed with payment interface
	public JSONArray getPrintedAndNonPaidCheckListByInterfaceIds(int iBdayId, int iOletId, List<String> oInterfaceIdList) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray oTmpJSONArray = new JSONArray();
		JSONArray responseJSONArray = null;
		 
		try {
			requestJSONObject.put("businessDayId", iBdayId);
			requestJSONObject.put("outletId", iOletId);
			requestJSONObject.put("section", InfVendor.TYPE_PAYMENT_INTERFACE);
			requestJSONObject.put("variable", "intf_id");
			
			for(String sValue: oInterfaceIdList) {
				JSONObject oTempJSONObject = new JSONObject();
				oTempJSONObject.put("value", sValue);
				oTmpJSONArray.put(oTempJSONObject);
			}
			requestJSONObject.put("values", oInterfaceIdList);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getPrintedAndNonPaidCheckListByInterfaceId", requestJSONObject.toString());
		
		return responseJSONArray;
	}
	
	// Get the active last sent check 
	public boolean getLastSentCheckByStatId(int iBdayId, int iOletId, int iStatId, boolean bCheckNumberReset) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("businessDayId", iBdayId);
			requestJSONObject.put("outletId", Integer.toString(iOletId));
			requestJSONObject.put("statId", iStatId);
			if(bCheckNumberReset)
				requestJSONObject.put("checkNumberReset", true);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if(!this.readDataFromApi("gm", "pos", "getLastSentCheckByStatId", requestJSONObject.toString()).equals(API_RESULT_SUCCESS))
			return false;
		
		return true;
	}
	
	// Update mix and match item id
	public boolean updateMixAndMatchMasterItemIds(HashMap<String, String> oMasterSlaveSeatSeqList) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("checkId", this.chksId);
			requestJSONObject.put("masterSlaveSeatSeqList", oMasterSlaveSeatSeqList);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "updateItemMixAndMatchItemId", requestJSONObject.toString(), true))
			return false;
		else 
			return true;
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray checkJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("checks")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("checks")) {
				this.init();
				return null;
			}
			
			checkJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("checks");
		}
		
		return checkJSONArray;
	}
	
	//read data from database 
	public String loadCheckById(int iBdayId, int iOutletId, int iChksId, PosOutletTable oOutletTable, int iUserId, int iStationId) {
		boolean bOutletTableUpdate = true;
		JSONObject requestJSONObject = new JSONObject();
		JSONObject checkLockJSONObject = new JSONObject();
		JSONObject outletTableJSONObject = new JSONObject();
		DateTime oLockDateTime = new DateTime();
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		if(oOutletTable.getOtblId() == 0)
			bOutletTableUpdate = false;
		else
			bOutletTableUpdate = true;
		
		try {
			//from the update request of outlet table
			outletTableJSONObject = oOutletTable.constructAddSaveJSON(bOutletTableUpdate);
			
			//form the check lock information request of check records
			checkLockJSONObject.put("chks_id", iChksId);
			checkLockJSONObject.put("chks_lock_time", oFormatter.print(oLockDateTime.withZone(DateTimeZone.UTC)));
			checkLockJSONObject.put("chks_lock_loctime", oLockDateTime.toString(oFormatter));
			checkLockJSONObject.put("chks_lock_user_id", iUserId);
			checkLockJSONObject.put("chks_lock_stat_id", iStationId);
			
			requestJSONObject.put("bdayId", iBdayId);
			requestJSONObject.put("oletId", iOutletId);
			requestJSONObject.put("checkId", Integer.toString(iChksId));
			requestJSONObject.put("check", checkLockJSONObject);
			requestJSONObject.put("outletTable", outletTableJSONObject);
			requestJSONObject.put("recursive", 1);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "updateOutletTableAndLoadChkByChkId", requestJSONObject.toString());
	}

	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("chks_id", this.chksId);
			addSaveJSONObject.put("chks_bday_id", this.bdayId);
			addSaveJSONObject.put("chks_bper_id", this.bperId);
			addSaveJSONObject.put("chks_shop_id", this.shopId);
			addSaveJSONObject.put("chks_olet_id", this.oletId);
			if(this.sectId > 0)
				addSaveJSONObject.put("chks_sect_id", this.sectId);
			addSaveJSONObject.put("chks_check_prefix", this.checkPrefix);
			addSaveJSONObject.put("chks_check_num", this.checkNo);
			addSaveJSONObject.put("chks_check_prefix_num", this.checkPrefixNo);
			addSaveJSONObject.put("chks_ctbl_id", this.ctblId);
			addSaveJSONObject.put("chks_guests", this.guests);
			if(this.children > 0)
				addSaveJSONObject.put("chks_childen", this.children);
			if(this.printCount > 0)
				addSaveJSONObject.put("chks_print_count", this.printCount);
			if(this.receiptPrintCount > 0)
				addSaveJSONObject.put("chks_receipt_print_count", this.receiptPrintCount);
			if(this.partyCount > 0)
				addSaveJSONObject.put("chks_party_count", this.partyCount);
			addSaveJSONObject.put("chks_check_total", this.checkTotal);
			addSaveJSONObject.put("chks_item_total", this.itemTotal);
			for (int i = 1; i <= 5; i++) {
				addSaveJSONObject.put("chks_sc"+i, this.sc[i-1]);
			}
			for (int i = 1; i <= 25; i++) {
				addSaveJSONObject.put("chks_tax"+i, this.tax[i-1]);
			}
			for (int i = 1; i <= 4; i++) {
				addSaveJSONObject.put("chks_incl_tax_ref"+i, this.inclTaxRef[i-1]);
			}
			addSaveJSONObject.put("chks_pre_disc", this.preDisc);
			addSaveJSONObject.put("chks_mid_disc", this.midDisc);
			addSaveJSONObject.put("chks_post_disc", this.postDisc);
			addSaveJSONObject.put("chks_round_amount", this.roundAmount);
			addSaveJSONObject.put("chks_payment_total", this.paymentTotal);
			addSaveJSONObject.put("chks_tips_total", this.tipsTotal);
			addSaveJSONObject.put("chks_paid", this.paid);
			if(this.resvBookDate != null)
				addSaveJSONObject.put("chks_resv_book_date", dateFormat.format(this.resvBookDate));
			if(!this.refnoWithPrefix.isEmpty())
				addSaveJSONObject.put("chks_resv_refno_with_prefix", this.refnoWithPrefix);
			addSaveJSONObject.put("chks_ordering_type", this.orderingType);
			addSaveJSONObject.put("chks_ordering_mode", this.orderingMode);
			addSaveJSONObject.put("chks_non_revenue", this.nonRevenue);
			addSaveJSONObject.put("chks_memb_id", this.membId);
			
			if(this.remark != null)
				addSaveJSONObject.put("chks_remark", this.remark);
			
			addSaveJSONObject.put("chks_settle_shift_num", this.settleShiftNo);
			
			if (this.openLocTime != null) {
				addSaveJSONObject.put("chks_open_loctime", dateTimeToString(this.openLocTime));
				addSaveJSONObject.put("chks_open_time", this.openTime);
			}
			
			if(this.openUserId > 0)
				addSaveJSONObject.put("chks_open_user_id", this.openUserId);
			if(this.openStatId > 0)
				addSaveJSONObject.put("chks_open_stat_id", this.openStatId);
			
			addSaveJSONObject.put("chks_close_loctime", dateTimeToString(this.closeLocTime));
			addSaveJSONObject.put("chks_close_time", this.closeTime);
			addSaveJSONObject.put("chks_close_user_id", this.closeUserId);
			addSaveJSONObject.put("chks_close_stat_id", this.closeStatId);
			addSaveJSONObject.put("chks_close_bper_id", this.closeBperId);
			
			if (this.modifiedLocTime != null) {
				addSaveJSONObject.put("chks_modified_loctime", dateTimeToString(this.modifiedLocTime));
				addSaveJSONObject.put("chks_modified_time", this.modifiedTime);
			}
			
			if(this.modifiedUserId > 0)
				addSaveJSONObject.put("chks_modified_user_id", this.modifiedUserId);
			if(this.modifiedStatId > 0)
				addSaveJSONObject.put("chks_modified_stat_id", this.modifiedStatId);
			
			if (this.printLocTime != null) {
				addSaveJSONObject.put("chks_print_loctime", dateTimeToString(this.printLocTime));
				addSaveJSONObject.put("chks_print_time", this.printTime);
			}
			
			if(this.printUserId > 0)
				addSaveJSONObject.put("chks_print_user_id", this.printUserId);
			if(this.printStatId > 0)
				addSaveJSONObject.put("chks_print_stat_id", this.printStatId);
			
			addSaveJSONObject.put("chks_lock_loctime", dateTimeToString(this.lockLocTime));
			if(this.lockTime == null)
				addSaveJSONObject.put("chks_lock_time", "");
			else
				addSaveJSONObject.put("chks_lock_time", this.lockTime);
			
			addSaveJSONObject.put("chks_lock_user_id", this.lockUserId);
			addSaveJSONObject.put("chks_lock_stat_id", this.lockStatId);
			
			if (this.voidLocTime != null) {
				addSaveJSONObject.put("chks_void_loctime", dateTimeToString(this.voidLocTime));
				addSaveJSONObject.put("chks_void_time", this.voidTime);
			}
			
			if(this.voidUserId > 0)
				addSaveJSONObject.put("chks_void_user_id", this.voidUserId);
			if(this.voidStatId > 0)
				addSaveJSONObject.put("chks_void_stat_id", this.voidStatId);
			if(this.voidVdrsId > 0)
				addSaveJSONObject.put("chks_void_vdrs_id", this.voidVdrsId);
			addSaveJSONObject.put("chks_status", this.status);
			
			this.bModified = false;
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	//save (add or update) record to database
	// *** iSendMode:	0 - Send new items only
	//					1 - Send old items only
	//					2 - Send both new and old items
	// *** iSaveCheckType:	0 - Normal Send Check
	//					1 - Split Check
	//					2 - Merge Check
	public String addUpdate(boolean bUpdate, int iOutletId, int iShopId, int iCurrentBdayId, boolean bWithItems, List<PosCheckParty> oCheckPartyList, int iSendMode, int iSaveCheckType, PosOutletTable oOutletTable, PosCheckTable oCheckTable, int iGetUpdateItems, int iActionUserId, int iActionStationId, int iReceiptPrtqId, int iReceiptFormatId, int iSplitMergeCheckId, boolean bWithPayments, ArrayList<PosCheckPayment> oCheckPaymentList, boolean bContructWithAllItemFields, String sChkIdCacheKey) {
		JSONObject requestJSONObject = new JSONObject();
		
		requestJSONObject = constructAddSaveJSON(bUpdate);
		//include current business day id for checking
		try {
			requestJSONObject.put("currentBdayId", iCurrentBdayId);
			requestJSONObject.put("currentOutletId", iOutletId);
			requestJSONObject.put("currentShopId", iShopId);
			requestJSONObject.put("saveCheckType", iSaveCheckType);
			requestJSONObject.put("splitMergeCheckId", iSplitMergeCheckId);
			requestJSONObject.put("chkIdCacheKey", sChkIdCacheKey);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (oCheckTable != null) { // When create new check, create CheckTable record
			try {
				JSONObject tableJSONObject = new JSONObject();
				tableJSONObject.put("ctbl_table", oCheckTable.getTable() + "");
				tableJSONObject.put("ctbl_table_ext", oCheckTable.getTableExt());
				tableJSONObject.put("ctbl_table_size", oCheckTable.getTableSize());
				tableJSONObject.put("ctbl_additional", oCheckTable.getAdditional());
				requestJSONObject.put("PosCheckTable", tableJSONObject);
			}catch (JSONException jsone) {
				jsone.printStackTrace();
			}
		}
		
		//include new items if bWithItems is true
		this.m_oPartyInfosList.clear();
		this.m_oNewItemInfosList.clear();
		this.m_oNewPaymentIdsList.clear();
		this.m_oNewCheckExtraInfoInfosList.clear();
		this.m_oCheckDiscountIdsList.clear();
		if(bWithItems && !oCheckPartyList.isEmpty()) {
			JSONObject tempCheckPartyJSONObject = null;
			JSONArray checkPartyJSONArray = new JSONArray();
			for(PosCheckParty oPosCheckParty:oCheckPartyList){
				if(oPosCheckParty.getSeq() == 0){
					// First check party, no need to create check party
					PosCheckItem oCheckItem = new PosCheckItem();
					try {
						requestJSONObject.put("items", oCheckItem.constructMultipleItemAddSaveJSON(oPosCheckParty.getCheckItemList(), iSendMode, bContructWithAllItemFields));
						
						//Build the party check discount list
						if(!oPosCheckParty.getCheckDiscountList().isEmpty()) {
							JSONArray oCheckDiscountJSONArray = new JSONArray();
							for(PosCheckDiscount oCheckDiscount:oPosCheckParty.getCheckDiscountList()) {
								if(oCheckDiscount.getCdisId() == 0)
									oCheckDiscountJSONArray.put(oCheckDiscount.constructAddSaveJSON(false));
								else
									oCheckDiscountJSONArray.put(oCheckDiscount.constructAddSaveJSON(true));
							}
							
							try {
								requestJSONObject.put("checkDiscounts", oCheckDiscountJSONArray);
							}catch(JSONException jsone) {
								jsone.printStackTrace();
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{
					// Check party seq > 0, create check party record
					if(oPosCheckParty.getCptyId() == 0)
						tempCheckPartyJSONObject = oPosCheckParty.constructAddSaveJSON(false, iSendMode);
					else
						tempCheckPartyJSONObject = oPosCheckParty.constructAddSaveJSON(true, iSendMode);
					checkPartyJSONArray.put(tempCheckPartyJSONObject);
				}
			}
			
			try {
				requestJSONObject.put("checkParties", checkPartyJSONArray);
			}catch (JSONException jsone) {
				jsone.printStackTrace();
			}
		}
		
		//include outlet table information if bUpdateOutletTable is true
		if(oOutletTable != null) {
			try {
				requestJSONObject.put("PosOutletTable", oOutletTable.constructAddSaveJSON(true));
			}catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		//get the updated record of check
		if(iGetUpdateItems > 0) {
			try {
				requestJSONObject.put("getUpdateItems", iGetUpdateItems);
			}catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		// Set action user id and station id
		try {
			requestJSONObject.put("actionUserId", iActionUserId);
			requestJSONObject.put("actionStationId", iActionStationId);

			if(taiwanGuiTranList != null && !taiwanGuiTranList.isEmpty()) {
				JSONArray oTaiwanGuiTranArray = new JSONArray();
				for(int i=0; i<taiwanGuiTranList.size(); i++) {
					if(taiwanGuiTranList.get(i).getTwtxId() > 0) {
						JSONObject oTraiGuiTranJSONObject = taiwanGuiTranList.get(i).constructAddSaveJSON(true);
						oTaiwanGuiTranArray.put((oTraiGuiTranJSONObject));
					}
				}
				requestJSONObject.put("PosTaiwanGuiTran", oTaiwanGuiTranArray);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// Build the payment list for update
		if(bWithPayments) {
			JSONObject tempCheckPaymentJSONObject = null;
			JSONArray checkPaymentJSONArray = new JSONArray();
			
			for(PosCheckPayment oPosCheckPayment:oCheckPaymentList){
				if(oPosCheckPayment.getCpayId() == 0)
					tempCheckPaymentJSONObject = oPosCheckPayment.constructAddSaveJSON(false);
				else
					tempCheckPaymentJSONObject = oPosCheckPayment.constructAddSaveJSON(true);
				checkPaymentJSONArray.put(tempCheckPaymentJSONObject);
			}
			
			try {
				requestJSONObject.put("checkPayments", checkPaymentJSONArray);
			}catch (JSONException jsone) {
				jsone.printStackTrace();
			}
		}
		
		// Set receipt print queue id and print format id
		if (iReceiptPrtqId > 0 && iReceiptFormatId > 0) {
			try {
				requestJSONObject.put("receiptPrtqId", iReceiptPrtqId);
				requestJSONObject.put("receiptFormatId", iReceiptFormatId);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		//construct the extra info list if exist
		if(this.checkExtraInfoList != null && !this.checkExtraInfoList.isEmpty()) {
			JSONArray chkExtraInfoJSONArray = new JSONArray();
			for(PosCheckExtraInfo oCheckExtraInfo:this.checkExtraInfoList) {
				if(oCheckExtraInfo.getCkeiId() == 0)
					chkExtraInfoJSONArray.put(oCheckExtraInfo.constructAddSaveJSON(false));
				else
					chkExtraInfoJSONArray.put(oCheckExtraInfo.constructAddSaveJSON(true));
			}
			try {
				requestJSONObject.put("checkExtraInfos", chkExtraInfoJSONArray);
			}
			catch(JSONException jsone) {
				jsone.printStackTrace();
			}
		}
		
		//System.out.println("~~~~~~addUpdate requestJSONObject: "+requestJSONObject.toString());
		//send data to server
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveCheck", requestJSONObject.toString(), false)) {
			if (OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage().equals("invalid_business_day"))
				return API_RESULT_INVALID_BDAY;
			else if (OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage().equals("missing_active_business_day"))
				return API_RESULT_MISSING_BDAY;
			else
				return API_RESULT_FAIL;
			//return false;
		} else {
			//After insert record to database, update Check's check id
			JSONObject oResponseJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse();
			if(iGetUpdateItems == 2) {
				try {
					this.readDataFromJson(oResponseJSONObject.getJSONObject("checkInfo"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (this.chksId == 0) {
				if (!oResponseJSONObject.has("id") || !oResponseJSONObject.has("posCheckTableId"))
					return API_RESULT_FAIL;
				
				this.chksId = oResponseJSONObject.optInt("id");
				this.ctblId = oResponseJSONObject.optInt("posCheckTableId");
				if(oResponseJSONObject.has("checkPrefix") && oResponseJSONObject.optString("checkPrefix").compareTo(this.checkPrefix) != 0)
					this.checkPrefix = oResponseJSONObject.optString("checkPrefix");
				if(oResponseJSONObject.has("checkNum") && oResponseJSONObject.optInt("checkNum") > 0)
					this.checkNo = oResponseJSONObject.optInt("checkNum");
				else
					this.checkNo = 0;
				if(oResponseJSONObject.has("checkPrefixNum") && !oResponseJSONObject.optString("checkPrefixNum").isEmpty())
					this.checkPrefixNo = oResponseJSONObject.optString("checkPrefixNum");
				else
					this.checkPrefixNo = "";

				// reset all items are unmodified
				for(PosCheckParty oPosCheckParty:oCheckPartyList){
					for(PosCheckItem oCheckItem:oPosCheckParty.getCheckItemList()) {
						oCheckItem.setModified(false);
					}
				}
			}

			if(iGetUpdateItems == 1) {
				try {
					m_oReturnJSONArray = oResponseJSONObject.getJSONArray("PosCheckItem");
				} catch (JSONException jsone) {
					jsone.printStackTrace();
					//return false;
					return API_RESULT_FAIL;
				}
			}
			
			// Get the new add items' item Id that is not belonged to party
			JSONArray oNewItemIdsJSONArray = oResponseJSONObject.optJSONArray("newItemInfos");
			if(oNewItemIdsJSONArray != null) {
				for(int i=0; i<oNewItemIdsJSONArray.length(); i++) {
					try {
						this.m_oNewItemInfosList.add(oNewItemIdsJSONArray.getJSONObject(i));
					}catch(JSONException jsone) {
						jsone.printStackTrace();
					}
				}
			}
			
			// Get all of the parties' party Id
			JSONArray oPartyIdsJSONArray = oResponseJSONObject.optJSONArray("partyInfos");
			if(oPartyIdsJSONArray != null) {
				for(int i=0; i<oPartyIdsJSONArray.length(); i++) {
					try {
						this.m_oPartyInfosList.add(oPartyIdsJSONArray.getJSONObject(i));
						
						// Get the new add items' item Id that is belonged to party
						oNewItemIdsJSONArray = oPartyIdsJSONArray.getJSONObject(i).optJSONArray("newItemInfos");
						if(oNewItemIdsJSONArray != null) {
							for(int j=0; j<oNewItemIdsJSONArray.length(); j++) {
								try {
									this.m_oNewItemInfosList.add(oNewItemIdsJSONArray.getJSONObject(j));
								}catch(JSONException jsone) {
									jsone.printStackTrace();
								}
							}
						}
					}catch(JSONException jsone) {
						jsone.printStackTrace();
					}
				}
			}
			
			// Get all of check discount and its corresponding discount item id
			JSONArray oNewCheckDiscountIdsJSONArray = oResponseJSONObject.optJSONArray("newCheckDiscountInfos");
			if(oNewCheckDiscountIdsJSONArray != null) {
				for(int i=0; i<oNewCheckDiscountIdsJSONArray.length(); i++) {
					try {
						this.m_oCheckDiscountIdsList.add(oNewCheckDiscountIdsJSONArray.getJSONObject(i));
					}catch(JSONException jsone) {
						jsone.printStackTrace();
					}
				}
			}
			
			// Get the new add payment Id
			JSONArray oNewPaymentIdsJSONArray = oResponseJSONObject.optJSONArray("newPaymentIds");
			if(oNewPaymentIdsJSONArray != null) {
				for(int i=0; i<oNewPaymentIdsJSONArray.length(); i++) {
					try {
						this.m_oNewPaymentIdsList.add(oNewPaymentIdsJSONArray.getInt(i));
					}catch(JSONException jsone) {
						jsone.printStackTrace();
					}
				}
			}
			
			// Update all check extra info
			JSONArray oCheckExtraInfosJSONArray = oResponseJSONObject.optJSONArray("newCheckExtraInfos");
			if(oCheckExtraInfosJSONArray != null) {
				for(int i=0; i<oCheckExtraInfosJSONArray.length(); i++) {
					try {
						this.m_oNewCheckExtraInfoInfosList.add(oCheckExtraInfosJSONArray.getJSONObject(i));
					}catch(JSONException jsone) {
						jsone.printStackTrace();
					}
				}
			}
			
			// Update all item's original value
			if(bWithItems && !oCheckPartyList.isEmpty()) {
				for(PosCheckParty oPosCheckParty:oCheckPartyList){
					for(int i=0; i<oPosCheckParty.getCheckItemList().size(); i++) {
						PosCheckItem oPosCheckItem = oPosCheckParty.getCheckItemList().get(i);
						oPosCheckItem.updateOriginalValue();
					}
				}
			}
			
			//return true;
			return API_RESULT_SUCCESS;
		}
	}

	//add or update multiple check payments record to database
	public boolean addUpdateWithMutlipleRecord(List<PosCheck> oUnsettledCheckList) {
		JSONObject tempCheckJSONObject = null, checkJSONObject = new JSONObject();
		JSONArray checkJSONArray = new JSONArray();
		
		for(PosCheck oPosCheck:oUnsettledCheckList){
			if(oPosCheck.getCheckId() == 0)
				tempCheckJSONObject = oPosCheck.constructAddSaveJSON(false);
			else
				tempCheckJSONObject = oPosCheck.constructAddSaveJSON(true);
			checkJSONArray.put(tempCheckJSONObject);
		}
		
		try {
			checkJSONObject.put("checks", checkJSONArray);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveMultiChecks", checkJSONObject.toString(), false))
			return false;
		else
			return true;
	}

	public boolean cashierSettlement(List<PosCheck> oUnsettledCheckList, int iShiftNo, String sBusinessDay, int iUserId, int iPrintQueueId, int iLangIndex) {
		JSONObject tempCheckJSONObject = null, requestJSONObject = new JSONObject();
		JSONArray checkJSONArray = new JSONArray();
		
		for(PosCheck oPosCheck:oUnsettledCheckList){
			if(oPosCheck.getCheckId() == 0)
				tempCheckJSONObject = oPosCheck.constructAddSaveJSON(false);
			else
				tempCheckJSONObject = oPosCheck.constructAddSaveJSON(true);
			checkJSONArray.put(tempCheckJSONObject);
		}
		
		try {
			requestJSONObject.put("businessDay", sBusinessDay);
			requestJSONObject.put("userId", iUserId);
			requestJSONObject.put("shiftNum", iShiftNo);
			requestJSONObject.put("langIndex", iLangIndex);
			requestJSONObject.put("printQueueId", iPrintQueueId);
			requestJSONObject.put("checks", checkJSONArray);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "cashierSettlement", requestJSONObject.toString(), false))
			return false;
		else
			return true;
	}

	//delete new check for send check fail
	public boolean deleteCheckForSendFail(String sTableNo, String sTableExtension) {
		JSONObject oRequestJSONObject = new JSONObject();
		
		try {
			oRequestJSONObject.put("businessDayId", this.bdayId);
			oRequestJSONObject.put("outletId", this.oletId);
			oRequestJSONObject.put("tableNo", sTableNo);
			oRequestJSONObject.put("tableExtension", sTableExtension);
			oRequestJSONObject.put("checkNo", this.checkNo);
			oRequestJSONObject.put("checkPrefix", this.checkPrefix);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "deleteWholeCheckByBusinessDayCheckNum", oRequestJSONObject.toString(), false)) 			
			return false;
		else 
			return true;
	}
	
	//print kitchen slip
	public boolean printKitchenSlip(int iCheckId, List<Integer> oNewItemIds, int iCurrentLang) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("checkId", iCheckId);
			requestJSONObject.put("currentLang", iCurrentLang);
			requestJSONObject.put("newItemIds", oNewItemIds);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "printKitchenSlip", requestJSONObject.toString(), true))
			return false;
		else 
			return true;
	}
	
	//print special slip
	public boolean printSpecialSlip(String sType, JSONObject oHeader, JSONObject oInformation, int iCurrentLang, int iCitmId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("type", sType);
			requestJSONObject.put("checkId", this.chksId);
			if(iCitmId > 0)
				requestJSONObject.put("checkItemId", iCitmId);
			requestJSONObject.put("header", oHeader);
			requestJSONObject.put("info", oInformation);
			requestJSONObject.put("currentLang", iCurrentLang);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "printSpecialSlip", requestJSONObject.toString(), true))
			return false;
		else 
			return true;
	}
	
	// Print special slip for multiple items
	public boolean printMultiSpecialSlip(int iCheckId, String sType, JSONObject oHeader, JSONObject oInformation, ArrayList<Integer> oItemIds, int iCurrentLang) {
		JSONObject requestJSONObject = new JSONObject(), oTempJSONObject;
		JSONArray oCitmIdArray = new JSONArray();
		
		try {
			requestJSONObject.put("type", sType);
			requestJSONObject.put("checkId", iCheckId);
			requestJSONObject.put("header", oHeader);

			for(Integer oItemId:oItemIds) {
				oTempJSONObject = new JSONObject();
				oTempJSONObject.put("id", oItemId.intValue());
				oCitmIdArray.put(oTempJSONObject);
			}
			oInformation.put("citmIds", oCitmIdArray);
			requestJSONObject.put("info", oInformation);
			requestJSONObject.put("currentLang", iCurrentLang);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
			
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "printSpecialSlip", requestJSONObject.toString(), true))
			return false;
		else
			return true;
	}
	
	// generate the printing slip
	// *** sPrintType:	0 - normal printing (i.e. print guest check / receipt
	//					1 - preview
	//					2 - serving list
	public JSONObject printGuestCheckWithPassingDetail(String sPrintType, int iCurrentLang, int iPrtqId, int iPrintFormatId, boolean bDetailCheck, boolean bPaymentInterfacePrinting, String sTableNo, String sTableExtension, String sChkIdCacheKey, boolean bVoidCheck, int iVoidCheckReasonId, PosStation oPosStation, UserUser oUserUser, OutShop oOutShop, OutOutlet oOutOutlet, String sOutletLogoFilename, PosBusinessDay oPosBusinessDay, PosBusinessPeriod oPosBusinessPeriod, PosCheckPrintLog oCheckPrintLog, 
			List<PosCheckItem> oCheckItems, HashMap<Integer, PosCheckDiscount> oCheckDiscounts, HashMap<String, String> oTableNameList, List<PosCheckPayment> oCheckPaymentList) {
		JSONObject requestJSONObject = new JSONObject(), tempJSONObject = null;
		boolean bReceipt = false;
		
		if(oCheckPaymentList != null && !oCheckPaymentList.isEmpty())
			bReceipt = true;
		
		try {
			// construct pos_check information
			requestJSONObject.put("posCheck", constructAddSaveJSON(true));
			
			// printing information
			tempJSONObject = new JSONObject();
			if(bReceipt)
				tempJSONObject.put("printReceipt", 1);
			tempJSONObject.put("printFormatId", iPrintFormatId);
			tempJSONObject.put("printQueueId", iPrtqId);
			tempJSONObject.put("currentLang", iCurrentLang);
			tempJSONObject.put("detailCheck", bDetailCheck);
			tempJSONObject.put("chkIdCacheKey", sChkIdCacheKey);
			if(bPaymentInterfacePrinting)
				tempJSONObject.put("paymentInterfacePrinting", 1);
			tempJSONObject.put("voidCheck", bVoidCheck);
			if(iVoidCheckReasonId > 0)
				tempJSONObject.put("voidCheckReasonId", iVoidCheckReasonId);
			if(sPrintType.equals(PosCheck.PRINTING_PREVIEW))
				tempJSONObject.put("preview", 1);
			else if(sPrintType.equals(PosCheck.PRINTING_SERVING_LIST))
				tempJSONObject.put("servingList", 1);
			requestJSONObject.put("printInfo", tempJSONObject);
			
			// table information
			tempJSONObject = new JSONObject();
			tempJSONObject.put("tableNumber", sTableNo);
			tempJSONObject.put("tableExtension", sTableExtension);
			if(oTableNameList != null) {
				for(int i=1; i<=5; i++) {
					String sName = "tableName"+i;
					if(oTableNameList.containsKey(sName))
						tempJSONObject.put(sName, oTableNameList.get(sName));
				}
			}
			requestJSONObject.put("tableInfo", tempJSONObject);
			
			// handle pos station
			requestJSONObject.put("posStation", oPosStation.constructAddSaveJSON(true));
			
			// handle current user
			requestJSONObject.put("currentUser", oUserUser.constructAddSaveJSON(true));
			
			//handle shop
			requestJSONObject.put("shop", oOutShop.constructAddSaveJSON(true));
			
			// handle outlet
			requestJSONObject.put("outlet", oOutOutlet.constructAddSaveJSON(true));
			requestJSONObject.put("outletLogoFilename", sOutletLogoFilename);
			
			// handle pos business day
			requestJSONObject.put("posBusinessDay", oPosBusinessDay.constructAddSaveJSON(true));
			
			// handle pos business period
			requestJSONObject.put("posBusinessPeriod", oPosBusinessPeriod.constructAddSaveJSON(true));
			
			// handle pos check item
			PosCheckItem oPosCheckItem = new PosCheckItem();
			requestJSONObject.put("items", oPosCheckItem.constructMultipleItemAddSaveJSON(oCheckItems, 2, true));
			
			// hanlde pos check discount
			if(!oCheckDiscounts.isEmpty()) {
				JSONArray oCheckDiscountJSONArray = new JSONArray();
				for(Entry<Integer, PosCheckDiscount> entry: oCheckDiscounts.entrySet()) 
					oCheckDiscountJSONArray.put(entry.getValue().constructAddSaveJSON(true));
				
				try {
					requestJSONObject.put("checkDiscounts", oCheckDiscountJSONArray);
				}catch(JSONException jsone) {
					jsone.printStackTrace();
				}
			}
			
			// handle pos check extra info
			if(!checkExtraInfoList.isEmpty()) {
				JSONArray oCheckExtraInfoJSONArray = new JSONArray();
				for(PosCheckExtraInfo oCheckExtraInfo:checkExtraInfoList) {
					oCheckExtraInfoJSONArray.put(oCheckExtraInfo.constructAddSaveJSON(false));
				}
				requestJSONObject.put("checkExtraInfos", oCheckExtraInfoJSONArray);
			}
			
			// handle pos check print log
			if(!bReceipt && sPrintType.equals(PosCheck.PRINTING_NORMAL))
				requestJSONObject.put("checkPrintLog", oCheckPrintLog.constructAddSaveJSON(true));
			
			// handle pos check payments
			if(oCheckPaymentList != null && !oCheckPaymentList.isEmpty()) {
				JSONObject tempPaymentJSONObject;
				JSONArray oPaymentJSONArray = new JSONArray();
				for(PosCheckPayment oCheckPayment:oCheckPaymentList) {
					tempPaymentJSONObject = new JSONObject();
					tempPaymentJSONObject = oCheckPayment.constructAddSaveJSON(false);
					oPaymentJSONArray.put(tempPaymentJSONObject);
				}
				requestJSONObject.put("checkPayments", oPaymentJSONArray);
			}
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "printGuestCheckWithPassingInformation", requestJSONObject.toString(), true))
			return null;
		else
			return OmWsClientGlobal.g_oWsClient.get().getResponse();
	}
	
	// Preview receipt
	public String previewReceipt(int iCheckId, int iReceiptFormatId, int iCurrentLang) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("chks_id", iCheckId);
			requestJSONObject.put("printQueueId", 0);
			requestJSONObject.put("printFormatId", iReceiptFormatId);
			requestJSONObject.put("currentLang", iCurrentLang);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "previewReceipt", requestJSONObject.toString(), true)) 
			return "";
		else
			return OmWsClientGlobal.g_oWsClient.get().getResponse().optString("url");
	}
	
	// Print receipt
	public HashMap<String, String> printReceipt(int iReceiptPrtqId, int iReceiptFormatId, int iCheckId, int iPosLangIndex, int iReprintType) {
		JSONObject requestJSONObject = new JSONObject();
		HashMap<String, String> oReturnInfo = null;

		try {
			requestJSONObject.put("receiptPrintQueueId", iReceiptPrtqId);
			requestJSONObject.put("receiptFormatId", iReceiptFormatId);
			requestJSONObject.put("receiptCheckId", iCheckId);
			requestJSONObject.put("currentLang", iPosLangIndex);
			if(iReprintType > 0)	// 1:reprint receipt, 2:reprint Taiwan GUI receipt
				requestJSONObject.put("reprintReceipt", iReprintType);
			else
				requestJSONObject.put("reprintReceipt", 0);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "printReceipt", requestJSONObject.toString(), true)) 
			return oReturnInfo;
		else {
			oReturnInfo = new HashMap<String, String>();
			oReturnInfo.put("url", OmWsClientGlobal.g_oWsClient.get().getResponse().optString("url"));
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("page"))
				oReturnInfo.put("page", String.valueOf(OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("page")));
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("error"))
				oReturnInfo.put("error", OmWsClientGlobal.g_oWsClient.get().getResponse().optString("error"));
			return oReturnInfo;
		}
	}
	
	public String printReceiptForFastFoodMode(int iCurrentLang, int iReceiptPrtqId, int iReceiptFormatId, boolean bReprint, String sCheckPrefixNo, String sTableNo, String sTableExtension, PosStation oPosStation, UserUser oUserUser, OutShop oOutShop, OutOutlet oOutOutlet, String sOutletLogoFilename, PosBusinessDay oPosBusinessDay, PosBusinessPeriod oPosBusinessPeriod, List<PosCheckItem> oCheckItems, HashMap<Integer, PosCheckDiscount> oCheckDiscounts, List<PosCheckPayment> oCheckPayments) {
		JSONObject requestJSONObject = new JSONObject(), tempJSONObject = null, tempPaymentJSONObject = null;
		JSONArray oPaymentJSONArray = new JSONArray(); 
		
		try {
			// construct pos_check information
			requestJSONObject = constructAddSaveJSON(true);
			if (sCheckPrefixNo != null)
				requestJSONObject.put("chks_check_prefix_num", sCheckPrefixNo);
			tempJSONObject = new JSONObject();
			tempJSONObject.put("tableNumber", sTableNo);
			tempJSONObject.put("tableExtension", sTableExtension);
			requestJSONObject.put("tableInfo", tempJSONObject);
			
			// handle pos station
			requestJSONObject.put("posStation", oPosStation.constructAddSaveJSON(true));
			
			// handle current user
			requestJSONObject.put("currentUser", oUserUser.constructAddSaveJSON(true));
			
			//handle shop
			requestJSONObject.put("shop", oOutShop.constructAddSaveJSON(true));
			
			// handle outlet
			requestJSONObject.put("outlet", oOutOutlet.constructAddSaveJSON(true));
			requestJSONObject.put("outletLogoFilename", sOutletLogoFilename);
			
			// handle pos business day
			requestJSONObject.put("posBusinessDay", oPosBusinessDay.constructAddSaveJSON(true));
			
			// handle pos business period
			requestJSONObject.put("posBusinessPeriod", oPosBusinessPeriod.constructAddSaveJSON(true));
			
			// handle pos check item
			PosCheckItem oPosCheckItem = new PosCheckItem();
			requestJSONObject.put("items", oPosCheckItem.constructMultipleItemAddSaveJSON(oCheckItems, 2, true));
			
			// hanlde pos check discount
			if(!oCheckDiscounts.isEmpty()) {
				JSONArray oCheckDiscountJSONArray = new JSONArray();
				for(Entry<Integer, PosCheckDiscount> entry: oCheckDiscounts.entrySet()) 
					oCheckDiscountJSONArray.put(entry.getValue().constructAddSaveJSON(true));
				
				try {
					requestJSONObject.put("checkDiscounts", oCheckDiscountJSONArray);
				}catch(JSONException jsone) {
					jsone.printStackTrace();
				}
			}
			
			// handle pos check payments
			for(PosCheckPayment oCheckPayment:oCheckPayments) {
				tempPaymentJSONObject = new JSONObject();
				tempPaymentJSONObject = oCheckPayment.constructAddSaveJSON(false);
				oPaymentJSONArray.put(tempPaymentJSONObject);
			}
			requestJSONObject.put("checkPayments", oPaymentJSONArray);
			
			// handle pos check extra info
			if(!checkExtraInfoList.isEmpty()) {
				JSONArray oCheckExtraInfoJSONArray = new JSONArray();
				for(PosCheckExtraInfo oCheckExtraInfo:checkExtraInfoList) {
					oCheckExtraInfoJSONArray.put(oCheckExtraInfo.constructAddSaveJSON(false));
				}
				requestJSONObject.put("checkExtraInfos", oCheckExtraInfoJSONArray);
			}
			
			// handle receipt print queue and format information
			tempJSONObject = new JSONObject();
			tempJSONObject.put("receiptPrintQueueId", iReceiptPrtqId);
			tempJSONObject.put("receiptFormatId", iReceiptFormatId);
			if(bReprint == true)
				tempJSONObject.put("reprintReceipt", 1);
			else
				tempJSONObject.put("reprintReceipt", 0);
			requestJSONObject.put("receiptInfo", tempJSONObject);
			requestJSONObject.put("currentLang", iCurrentLang);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "printReceiptForFastFoodMode", requestJSONObject.toString(), true))
			return "";
		else
			return OmWsClientGlobal.g_oWsClient.get().getResponse().optString("url");
	}
	
	// Call pms posting
	public JSONObject contrustPmsPostingCheckInformation(List<PosCheckItem> oCheckItemListForPmsPosting, PosCheckPayment oCheckPayment, List<PosCheckDiscount> oCheckDiscountList) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject = constructAddSaveJSON(true);
			
			// handle pos check item
			PosCheckItem oPosCheckItem = new PosCheckItem();
			requestJSONObject.put("items", oPosCheckItem.constructMultipleItemAddSaveJSON(oCheckItemListForPmsPosting, 2, true));
			
			requestJSONObject.put("payment", oCheckPayment.constructAddSaveJSON(true));
			
			if(!oCheckDiscountList.isEmpty()) {
				JSONArray oCheckDiscountJSONArray = new JSONArray();
				for(int i=0; i<oCheckDiscountList.size(); i++) 
					oCheckDiscountJSONArray.put(oCheckDiscountList.get(i).constructAddSaveJSON(true));
				requestJSONObject.put("checkDiscounts", oCheckDiscountJSONArray);
			}
				
		}catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		
		return requestJSONObject;
	}

	// Call svc posting
	public JSONObject contrustSvcPostingCheckInformation(List<PosCheckItem> oCheckItemListForPmsPosting, List<PosCheckPayment> oCheckPaymentList, List<PosCheckDiscount> oCheckDiscountList) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject = constructAddSaveJSON(true);
			
			// handle pos check item
			PosCheckItem oPosCheckItem = new PosCheckItem();
			requestJSONObject.put("items", oPosCheckItem.constructMultipleItemAddSaveJSON(oCheckItemListForPmsPosting, 2, true));
			
			if(!oCheckPaymentList.isEmpty()) {
				JSONArray oCheckDiscountJSONArray = new JSONArray();
				for(int i=0; i<oCheckPaymentList.size(); i++) 
					oCheckDiscountJSONArray.put(oCheckPaymentList.get(i).constructAddSaveJSON(true));
				requestJSONObject.put("payments", oCheckDiscountJSONArray);
			}
			
			if(!oCheckDiscountList.isEmpty()) {
				JSONArray oCheckDiscountJSONArray = new JSONArray();
				for(int i=0; i<oCheckDiscountList.size(); i++) 
					oCheckDiscountJSONArray.put(oCheckDiscountList.get(i).constructAddSaveJSON(true));
				requestJSONObject.put("checkDiscounts", oCheckDiscountJSONArray);
			}
				
		}catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		
		return requestJSONObject;
	}
	
	// init value
	public void init() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		this.chksId = 0;
		this.bdayId = 0;
		this.bperId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.sectId = 0;
		this.checkPrefix = "";
		this.checkNo = 0;
		this.checkPrefixNo = "";
		this.ctblId = 0;
		this.guests = 0;
		this.children = 0;
		this.printCount = 0;
		this.receiptPrintCount = 0;
		this.partyCount = 0;
		this.checkTotal = BigDecimal.ZERO;
		this.itemTotal = BigDecimal.ZERO;
		if(this.sc == null)
			this.sc = new BigDecimal[5];
		for (int i = 0; i < 5; i++) {
			this.sc[i] = BigDecimal.ZERO;
		}
		if(this.tax == null)
			this.tax = new BigDecimal[25];
		for (int i = 0; i < 25; i++) {
			this.tax[i] = BigDecimal.ZERO;
		}
		if(this.inclTaxRef == null)
			this.inclTaxRef = new BigDecimal[4];
		for (int i = 0; i < 4; i++) {
			this.inclTaxRef[i] = BigDecimal.ZERO;
		}
		this.preDisc = BigDecimal.ZERO;
		this.midDisc = BigDecimal.ZERO;
		this.postDisc = BigDecimal.ZERO;
		this.roundAmount = BigDecimal.ZERO;
		this.paymentTotal = BigDecimal.ZERO;
		this.tipsTotal = BigDecimal.ZERO;
		this.paid = PosCheck.PAID_NOT_PAID;
		this.resvBookDate = null;
		this.refnoWithPrefix = "";
		this.orderingType = PosCheck.ORDERING_TYPE_NORMAL;
		this.orderingMode = PosCheck.ORDERING_MODE_FINE_DINING;
		this.nonRevenue = PosCheck.NON_REVENUE_NO;
		this.membId = 0;
		this.remark = null;
		this.settleShiftNo = 0;
		this.openLocTime = new DateTime();
		this.openTime = fmt.print(openLocTime.withZone(DateTimeZone.UTC));
		this.openUserId = 0;
		this.openStatId = 0;
		this.closeTime = null;
		this.closeLocTime = null;
		this.closeUserId = 0;
		this.closeStatId = 0;
		this.closeBperId = 0;
		this.modifiedTime = null;
		this.modifiedLocTime = null;
		this.modifiedUserId = 0;
		this.modifiedStatId = 0;
		this.printTime = null;
		this.printLocTime = null;
		this.printUserId = 0;
		this.printStatId = 0;
		this.lockTime = null;
		this.lockLocTime = null;
		this.lockUserId = 0;
		this.lockStatId = 0;
		this.voidTime = null;
		this.voidLocTime = null;
		this.voidUserId = 0;
		this.voidStatId = 0;
		this.voidVdrsId = 0;
		this.status = PosCheck.STATUS_NORMAL;
		
		taiwanGuiTranList = null;
		
		if(this.checkTable == null)
			this.checkTable = new PosCheckTable();
		else
			this.checkTable.init();
		
		if(this.checkParties == null)
			this.checkParties = new ArrayList<PosCheckParty>();
		else
			this.checkParties.clear();
		
		if(this.checkPayments == null)
			this.checkPayments = new ArrayList<PosCheckPayment>();
		else
			this.checkPayments.clear();
		
		if(this.checkDiscountList == null)
			this.checkDiscountList = new ArrayList<PosCheckDiscount>();
		else
			this.checkDiscountList.clear();
		
		if(this.checkExtraInfoList == null)
			this.checkExtraInfoList = new ArrayList<PosCheckExtraInfo>();
		else
			this.checkExtraInfoList.clear();
		
		if(this.m_oPartyInfosList == null)
			this.m_oPartyInfosList = new ArrayList<JSONObject>();
		else
			this.m_oPartyInfosList.clear();
		
		if(this.m_oNewItemInfosList == null)
			this.m_oNewItemInfosList = new ArrayList<JSONObject>();
		else
			this.m_oNewItemInfosList.clear();
		
		if(this.m_oNewPaymentIdsList == null)
			this.m_oNewPaymentIdsList = new ArrayList<Integer>();
		else
			this.m_oNewPaymentIdsList.clear();
		
		if(this.m_oNewCheckExtraInfoInfosList == null)
			this.m_oNewCheckExtraInfoInfosList = new ArrayList<JSONObject>();
		else
			this.m_oNewCheckExtraInfoInfosList.clear();
		
		if(this.m_oCheckDiscountIdsList == null)
			this.m_oCheckDiscountIdsList = new ArrayList<JSONObject>();
		else
			this.m_oCheckDiscountIdsList.clear();
		
		this.bModified = false;
	}
	
	//get ws client error
	public String getLastWsClientError() {
		return OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
	}
	
	//set chksId
	public void setChksId(int iId) {
		this.chksId = iId;
		this.bModified = true;
	}
	
	public void setBusinessDayId(int iBusinessDayId) {
		this.bdayId = iBusinessDayId;
		this.bModified = true;
	}
	
	public void setBusinessPeriod(int iBusinessPeriodId) {
		this.bperId = iBusinessPeriodId;
		this.bModified = true;
	}
	
	//set outlet id
	public void setOutletId(int iOletId) {
		this.oletId = iOletId;
		this.bModified = true;
	}
	
	//set shop id
	public void setShopId(int iShopId) {
		this.shopId = iShopId;
		this.bModified = true;
	}
	
	protected void setSectId(int iSectId) {
		this.sectId = iSectId;
		this.bModified = true;
	}
	
	public void setCheckPrefix(String sCheckPrefix) {
		this.checkPrefix = sCheckPrefix;
		this.bModified = true;
	}
	
	//set check number
	public void setCheckNum(int iCheckNum) {
		this.checkNo = iCheckNum;
		this.bModified = true;
	}
	
	//dry check prefix with check number
	public String setCheckPrefixNo(String sCheckPrefixNo) {
		return this.checkPrefixNo;
	}
	
	public void setTableId(int iTableId) {
		this.ctblId = iTableId;
		this.bModified = true;
	}
	
	//set gusets
	public void setGuests(int iGuest) {
		this.guests = iGuest;
		this.bModified = true;
	}
	
	//set children
	public void setChildren(int iChilren) {
		this.children = iChilren;
		this.bModified = true;
	}
	
	//set print count
	public void setPrintCount(int iPrintCount) {
		this.printCount = iPrintCount;
		this.bModified = true;
	}
	
	//set receipt print count
	public void setReceiptPrintCount(int iReceiptPrintCount) {
		this.receiptPrintCount = iReceiptPrintCount;
		this.bModified = true;
	}
	
	//set check count
	public void setPartyCount(int iPartyCount) {
		this.partyCount = iPartyCount;
		this.bModified = true;
	}
	
	//set check total
	public void setCheckTotal(BigDecimal dCheckTotal) {
		this.checkTotal = dCheckTotal;
		this.bModified = true;
	}
	
	//set payment total
	public void setPaymentTotal(BigDecimal dPaymentTotal) {
		this.paymentTotal = dPaymentTotal;
		this.bModified = true;
	}

	//set tips total
	public void setTipsTotal(BigDecimal dTipsTotal) {
		this.tipsTotal = dTipsTotal;
		this.bModified = true;
	}
	
	//set item total
	public void setItemTotal(BigDecimal dItemTotal) {
		this.itemTotal = dItemTotal;
		this.bModified = true;
	}
	
	//set sc
	public void setSc(int iIndex, BigDecimal dScAmount) {
		this.sc[(iIndex-1)] = dScAmount;
		this.bModified = true;
	}
	
	//set tax
	public void setTax(int iIndex, BigDecimal dTaxAmount) {
		this.tax[(iIndex-1)] = dTaxAmount;
		this.bModified = true;
	}
	
	//set inclusive tax reference
	public void setInclusiveTaxRef(int iIndex, BigDecimal dInclusiveTaxRef) {
		this.inclTaxRef[(iIndex-1)] = dInclusiveTaxRef;
		this.bModified = true;
	}
	
	//set pre-discount
	public void setPreDisc(BigDecimal dPreDisc) {
		this.preDisc = dPreDisc;
		this.bModified = true;
	}
	
	//set mid-discount
	public void setMidDisc(BigDecimal dMidDisc) {
		this.midDisc = dMidDisc;
		this.bModified = true;
	}
	
	//set post-discount
	public void setPostDisc(BigDecimal dPostDisc) {
		this.postDisc = dPostDisc;
		this.bModified = true;
	}
	
	//set round amount
	public void setRoundAmount(BigDecimal dRoundAmt) {
		this.roundAmount = dRoundAmt;
		this.bModified = true;
	}
	
	//set paid
	public void setPaid(String sPaid) {
		this.paid = sPaid;
		this.bModified = true;
	}
	
	//set resvBookDate
	public void setResvBookDate(java.util.Date oResvBookDate) {
		this.resvBookDate = oResvBookDate;
		this.bModified = true;
	}
	
	//set refnoWithPrefix
	public void setRefnoWithPrefix(String sRefnoWithPrefix) {
		this.refnoWithPrefix = sRefnoWithPrefix;
		this.bModified = true;
	}
	
	//set ordering type
	public void setOrderingType(String sOrderingType) {
		this.orderingType = sOrderingType;
		this.bModified = true;
	}
	
	//set ordering mode
	public void setOrderingMode(String sOrderingMode) {
		this.orderingMode = sOrderingMode;
		this.bModified = true;
	}
	
	//set non revenue
	public void setNonRevenue(String sNonRevenue) {
		this.nonRevenue = sNonRevenue;
		this.bModified = true;
	}
	
	//set member id
	public void setMemberId(int iMemberId) {
		this.membId = iMemberId;
		this.bModified = true;
	}
	
	//set remark
	public void setRemark(String sRemark) {
		this.remark = sRemark;
		this.bModified = true;
	}
	
	//set settle shift num
	public void setSettleShiftNo(int iSettleShiftNo) {
		this.settleShiftNo = iSettleShiftNo;
		this.bModified = true;
	}
	
	//set open time
	public void setOpenTime(String sOpenTime) {
		this.openTime = sOpenTime;
		this.bModified = true;
	}
	
	//set open local time
	public void setOpenLocTime(DateTime oOpenLocTime) {
		this.openLocTime = oOpenLocTime;
		this.bModified = true;
	}
	
	//set open user id
	public void setOpenUserId(int iUserId) {
		this.openUserId = iUserId;
		this.bModified = true;
	}
	
	//set open station id
	public void setOpenStatId(int iStationId) {
		this.openStatId = iStationId;
		this.bModified = true;
	}
	
	//set close time
	public void setCloseTime(String sCloseTime) {
		this.closeTime = sCloseTime;
		this.bModified = true;
	}
	
	//set close local time
	public void setCloseLocTime(DateTime oCloseTime) {
		this.closeLocTime = oCloseTime;
		this.bModified = true;
	}
	
	//set close user id
	public void setCloseUserId(int iUserId) {
		this.closeUserId = iUserId;
		this.bModified = true;
	}
	
	//set close station id
	public void setCloseStationId(int iStationId) {
		this.closeStatId = iStationId;
		this.bModified = true;
	}
	
	//set close business period id
	public void setCloseBperId(int iBperId) {
		this.closeBperId = iBperId;
		this.bModified = true;
	}

	public void setVoidTime(String sVoidTime) {
		this.voidTime = sVoidTime;
		this.bModified = true;
	}
	
	public void setVoidLocTime(DateTime oVoidTime) {
		this.voidLocTime = oVoidTime;
		this.bModified = true;
	}
	
	public void setVoidUserId(int iUserId) {
		this.voidUserId = iUserId;
		this.bModified = true;
	}
	
	public void setVoidStationId(int iStationId) {
		this.voidStatId = iStationId;
		this.bModified = true;
	}
	
	public void setVoidVdrsId(int iVdrsId) {
		this.voidVdrsId = iVdrsId;
		this.bModified = true;
	}
	
	//set modified time
	public void setModifiedTime(String sModifiedTime) {
		this.modifiedTime = sModifiedTime;
		this.bModified = true;
	}
	
	//set modified local time
	public void setModifiedLocTime(DateTime oModifiedTime) {
		this.modifiedLocTime = oModifiedTime;
		this.bModified = true;
	}
	
	//set modified user id
	public void setModifiedUesrId(int iUserId) {
		this.modifiedUserId = iUserId;
		this.bModified = true;
	}
	
	//set modifier station id
	public void setModifiedStationId(int iStationId) {
		this.modifiedStatId = iStationId;
		this.bModified = true;
	}
	
	//set print time
	public void setPrintTime(String sPrintTime) {
		this.printTime = sPrintTime;
		this.bModified = true;
	}
	
	//set print local time
	public void setPrintLocTime(DateTime oPrintLocTime) {
		this.printLocTime = oPrintLocTime;
		this.bModified = true;
	}
	
	//set print user id
	public void setPrintUserId(int iUserId) {
		this.printUserId = iUserId;
		this.bModified = true;
	}
	
	//set print station id
	public void setPrintStationId(int iStationId) {
		this.printStatId = iStationId;
		this.bModified = true;
	}
	
	//set lock time
 	public void setLockTime(String sLockTime) {
		this.lockTime = sLockTime;
		this.bModified = true;
	}
	
	//set lock local time
	public void setLockLocTime(DateTime oLockLocTime) {
		this.lockLocTime = oLockLocTime;
		this.bModified = true;
	}
	
	//set lock user id
	public void setLockUserId(int iUserId) {
		this.lockUserId = iUserId;
		this.bModified = true;
	}
	
	//set lock station id
	public void setLockStationId(int iStationId) {
		this.lockStatId = iStationId;
		this.bModified = true;
	}
	
	//set check discount list
	public void setCheckDiscountList(List<PosCheckDiscount> oCheckDiscountList) {
		this.checkDiscountList.clear();
		for(PosCheckDiscount oCheckDiscount:oCheckDiscountList) {
			if(oCheckDiscount.isModified()) {
				this.checkDiscountList.add(oCheckDiscount);
				this.bModified = true;
			}
		}
	}
	
	//set check extra info list
	public void setCheckExtraInfoList(List<PosCheckExtraInfo> oCheckExtraInfoList) {
		this.checkExtraInfoList.clear();
		for(PosCheckExtraInfo oCheckExtraInfo:oCheckExtraInfoList) {
			this.checkExtraInfoList.add(oCheckExtraInfo);
		}
	}
	
	//set status
	public void setStatus(String sStatus) {
		this.status = sStatus;
		this.bModified = true;
	}
	
	public void addTaiwanGuiTran(PosTaiwanGuiTran oTaiwanGuiTran) {
		if(this.taiwanGuiTranList == null)
			this.taiwanGuiTranList = new ArrayList<PosTaiwanGuiTran>();
		
		this.taiwanGuiTranList.add(oTaiwanGuiTran);
	}
	
	public int getBusinessDayId() {
		return this.bdayId;
	}
	
	public int getBusinessPeriodId() {
		return this.bperId;
	}
	
	public int getShopId() {
		return this.shopId;
	}
	
	public int getCheckId() {
		return this.chksId;
	}
	
	protected int getSectId() {
		return this.sectId;
	}
	
	//get check no.
	public int getCheckNo() {
		return this.checkNo;
	}
	
	//get check no. prefix
	public String getCheckPrefix() {
		return this.checkPrefix;
	}
	
	//get check prefix with check number
	public String getCheckPrefixNo() {
		return this.checkPrefixNo;
	}
	
	//get table id
	public int getTableId() {
		return this.ctblId;
	}
	
	//get outlet id
	public int getOutletId() {
		return this.oletId;
	}
	
	//get guest
	public int getGuests() {
		return this.guests;
	}
	
	//get children
	public int getChildren() {
		return this.children;
	}
	
	//get print count
	public int getPrintCount() {
		return this.printCount;
	}
	
	//get receipt print count
	public int getReceiptPrintCount() {
		return this.receiptPrintCount;
	}
	
	//get party count
	public int getPartyCount() {
		return this.partyCount;
	}
	
	//get round amount
	public BigDecimal getRoundAmount() {
		return this.roundAmount;
	}
	
	//get payment total
	public BigDecimal getPaymentTotal() {
		return this.paymentTotal;
	}
	
	//get check total
	public BigDecimal getCheckTotal() {
		return this.checkTotal;
	}
	
	//get item total
	public BigDecimal getItemTotal() {
		return this.itemTotal;
	}
	
	//get tips total
	public BigDecimal getTipsTotal() {
		return this.tipsTotal;
	}
	
	//get sc
	public BigDecimal getSc(int iIndex) {
		return this.sc[(iIndex-1)];
	}
	
	//get tax
	public BigDecimal getTax(int iIndex) {
		return this.tax[(iIndex-1)];
	}
	
	//get inclusive tax reference
	public BigDecimal getInclusiveTaxRef(int iIndex) {
		return this.inclTaxRef[(iIndex-1)];
	}
	
	//get pre discount
	public BigDecimal getPreDisc() {
		return this.preDisc;
	}
	
	//get mid discount
	public BigDecimal getMidDisc() {
		return this.midDisc;
	}
	
	//get mid discount
	public BigDecimal getPostDisc() {
		return this.postDisc;
	}
	
	//get open time
	public String getOpenTime() {
		return this.openTime;
	}
	
	public String getCloseTime() {
		return this.closeTime;
	}
	
	//get open user id
	public int getOpenUserId() {
		return this.openUserId;
	}
	
	//get open station id
	public int getOpenStatId() {
		return this.openStatId;
	}
	
	//get paid
	public String getPaid() {
		return this.paid;
	}
	
	//get resvBookDate
	public Date getResvBookDate() {
		return this.resvBookDate;
	}
	
	//get refnoWithPrefix
	public String getRefnoWithPrefix() {
		return this.refnoWithPrefix;
	}
	
	//get ordering type
	public String getOrderingType() {
		return this.orderingType;
	}
	
	//get ordering mode
	public String getOrderingMode() {
		return this.orderingMode;
	}
	
	//get non revenue
	public String getNonRevenue() {
		return this.nonRevenue;
	}
	
	//get member Id
	public int getMemberId() {
		return this.membId;
	}
	
	//get remark
	public String getRemark() {
		return this.remark;
	}
	
	//get settle shift no
	public int getSettleShiftNum() {
		return this.settleShiftNo;
	}
	
	public DateTime getOpenLocTime() {
		return this.openLocTime;
	}
	//get close local time
	public DateTime getCloseLocTime() {
		return this.closeLocTime;
	}
	
	public int getCloseUserId() {
		return this.closeUserId;
	}
	
	public int getCloseStatId() {
		return this.closeStatId;
	}
	
	protected int getCloseBperId() {
		return this.closeBperId;
	}
	
	public String getVoidTime() {
		return this.voidTime;
	}
	
	public DateTime getVoidLocTime() {
		return this.voidLocTime;
	}
	
	public int getVoidUserId() {
		return this.voidUserId;
	}
	
	public int getVoidStatId() {
		return this.voidStatId;
	}
	
	public int getVoidVdrsId() {
		return this.voidVdrsId;
	}
	
	//get print time
	public String getPrintTime() {
		return this.printTime;
	}
	
	//get print local time
	public DateTime getPrintLocTime() {
		return this.printLocTime;
	}
	
	//get print user id
	public int getPrintUserId() {
		return this.printUserId;
	}
	
	//get print station id
	public int getPrintStationId() {
		return this.printStatId;
	}
	
	//get lock time
	public String getLockTime() {
		return this.lockTime;
	}
	
	//get lock local time
	public DateTime getLockLocTime() {
		return this.lockLocTime;
	}
	
	//get lock user id 
	public int getLockUserId() {
		return this.lockUserId;
	}
	
	//get lock station id
	public int getLockStationId() {
		return this.lockStatId;
	}
	
	//get status
	public String getStatus() {
		return this.status;
	}
	
	//get check party array list
	public List<PosCheckParty> getCheckPartiesArrayList() {
		return this.checkParties;
	}

	//get check payment array list
	public List<PosCheckPayment> getCheckPaymentArrayList() {
		return this.checkPayments;
	}
	
	//get check discount array list
	public List<PosCheckDiscount> getCheckDiscountArrayList() {
		return this.checkDiscountList;
	}
	
	//get check extra info list
	public List<PosCheckExtraInfo> getCheckExtraInfoArrayList() {
		return this.checkExtraInfoList;
	}
	
	//get updated JSONArray of webservice result
	public JSONArray getResultJSONArrayOfRequest() {
		return this.m_oReturnJSONArray;
	}
	
	//get last send parties' party id
	public List<JSONObject> getPartyInfos() {
		return this.m_oPartyInfosList;
	}
	
	//get new add items' item id
	public List<JSONObject> getNewAddItemInfos() {
		return this.m_oNewItemInfosList;
	}
	
	//get new add payment id
	public List<Integer> getNewPaymentIds() {
		return this.m_oNewPaymentIdsList;
	}
	
	//get new add check discount and discount item id
	public List<JSONObject> getNewCheckDiscountInfos() {
		return this.m_oCheckDiscountIdsList;
	}
	
	//get new add check's extra info id
	public List<JSONObject> getNewAddCheckExtraInfos() {
		return this.m_oNewCheckExtraInfoInfosList;
	}
	
	//get Table and table extension
	public int getTable(){
		if(checkTable != null){
			return checkTable.getTable();
		}else
			return 0;
	}
	
	public String getTableExtension(){
		if(checkTable != null){
			return checkTable.getTableExt();
		}else
			return "";
	}
	
	public List<PosTaiwanGuiTran> getTaiwanGuiTran() {
		return taiwanGuiTranList;
	}
	
	public PosTaiwanGuiTran getTaiwanGuiTran(int iIndex) {
		if(taiwanGuiTranList == null || taiwanGuiTranList.size() > iIndex)
			return null;
		
		return taiwanGuiTranList.get(iIndex-1);
	}
	
	//clear check party array list
	public void clearCheckPartyArrayList() {
		this.checkParties.clear();
	}
	
	//clear check discount array list
	public void clearCheckDiscountList() {
		this.checkDiscountList.clear();
	}
	
	//clear check payment array list
	public void clearCheckPaymentList() {
		this.checkPayments.clear();
	}
	
	//clear check extra info array list
	public void clearCheckExtraInfoList() {
		this.checkExtraInfoList.clear();
	}
	
	public boolean isNormal() {
		return this.status.equals(PosCheck.STATUS_NORMAL);
	}
	
	public boolean isDeleted() {
		return this.status.equals(PosCheck.STATUS_DELETED);
	}
	
	public boolean isTakeoutOrderingType() {
		return this.orderingType.equals(PosCheck.ORDERING_TYPE_TAKEOUT);
	}
	
	public boolean isNormalOrderingType() {
		return this.orderingType.equals(PosCheck.ORDERING_TYPE_NORMAL);
	}
	
	public boolean isRevenue() {
		return this.nonRevenue.equals(PosCheck.NON_REVENUE_NO);
	}
	
	public boolean isNotPaid() {
		return this.paid.equals(PosCheck.PAID_NOT_PAID);
	}
}
