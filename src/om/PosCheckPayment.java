//Database: pos_check_payments - Sales check payment records
package om;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;

public class PosCheckPayment {
	private String cpayId;
	private String bdayId;
	private String bperId;
	private int shopId;
	private int oletId;
	private String chksId;
	private String cptyId;
	private int paymId;
	private String[] name;
	private String[] shortName;
	private String paymentType;
	private BigDecimal payTotal;
	private BigDecimal payTips;
	private BigDecimal payChange;
	private String payForeignCurrency;
	private BigDecimal payForeignTotal;
	private BigDecimal payForeignTips;
	private BigDecimal payForeignChange;
	private BigDecimal surcharge;
	private BigDecimal foreignSurcharge;
	private String currencyCode;
	private BigDecimal currencyRate;
	private String changeBackCurrency;
	private String special;
	private String nonRevenue;
	private int membId;
	private int mealUserId;
	private String[] refData;
	private String payTime;
	private DateTime payLocTime;
	private int payUserId;
	private int payStatId;
	private String voidTime;
	private DateTime voidLocTime;
	private int voidUserId;
	private int voidStatId;
	private int voidVdrsId;
	private int paySeq;
	private String status;
	
	private List<PosCheckExtraInfo> checkExtraInfoList;
	private BigDecimal userInputValue;
	private boolean postingSuccess;
	
	private boolean bIsChangeToDummy;
	private String dummyPaymentRelationship;
	
	private int iTimeZoneOffset;
	private String sTimeZoneName;
	
	// payment status for payment rounding
	public static String DUMMY_PAYMENT_RELATIONSHIP_NO = "";
	public static String DUMMY_PAYMENT_RELATIONSHIP_PARENT = "p";
	public static String DUMMY_PAYMENT_RELATIONSHIP_CHILD = "c";
	
	// payForeignCurrency
	public static String PAY_FOREIGN_CURRENCY_LOCAL = "";
	public static String PAY_FOREIGN_CURRENCY_FOREIGN = "y";
	public static String PAY_FOREIGN_CURRENCY_AUTO_DETECT = "a";
	
	// changeBackCurrency
	public static String CHANGE_BACK_CURRENCY_LOCAL = "";
	public static String CHANGE_BACK_CURRENCY_FOREIGN = "f";
	
	// special
	public static String SPECIAL_NO = "";
	
	// non revenue
	public static String NON_REVENUE_NO = "";
	public static String NON_REVENUE_YES = "y";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//payment type
	public static String PAYMENT_TYPE_CASH = "cash";
	public static String PAYMENT_TYPE_CREDIT_CARD = "credit_card";
	public static String PAYMENT_TYPE_DUTY_MEAL = "duty_meal";
	public static String PAYMENT_TYPE_ON_CREDIT = "on_credit";
	public static String PAYMENT_TYPE_VOUCHER = "voucher";
	public static String PAYMENT_TYPE_COUPON = "coupon";
	public static String PAYMENT_TYPE_OCTOPUS = "octopus";
	public static String PAYMENT_TYPE_REWRITE_CARD = "rewrite_card";
	
	// pos payment gateway transaction
	public PosPaymentGatewayTransactionsList m_oPosGatewayTransactionsList = new PosPaymentGatewayTransactionsList();
	
	public JSONObject m_oPaymentOtherInfo;
	
	//init object with initialize value
	public PosCheckPayment () {
		this.init();
	}
	
	//init object from database by cpay_id
	public PosCheckPayment (int iCpayId) {
		JSONObject requestJSONObject = new JSONObject();
		
		this.init();
		try {
			requestJSONObject.put("checkPaymentId", Integer.toString(iCpayId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		this.readDataFromApi("gm", "pos", "getCheckPaymentById", requestJSONObject.toString());
	}
	
	//init object from another PosCheckPayment object
	public PosCheckPayment(PosCheckPayment oPosCheckPayment) {
		int i=0;
		
		this.init();
		this.cpayId = oPosCheckPayment.cpayId;
		this.bdayId = oPosCheckPayment.bdayId;
		this.bperId = oPosCheckPayment.bperId;
		this.shopId = oPosCheckPayment.shopId;
		this.oletId = oPosCheckPayment.oletId;
		this.chksId = oPosCheckPayment.chksId;
		this.cptyId = oPosCheckPayment.cptyId;
		this.paymId = oPosCheckPayment.paymId;
		for(i=0; i<5; i++)
			this.name[i] = oPosCheckPayment.name[i];
		for(i=0; i<5; i++)
			this.shortName[i] = oPosCheckPayment.shortName[i];
		this.paymentType = oPosCheckPayment.paymentType;
		this.payTotal = new BigDecimal(oPosCheckPayment.payTotal.toPlainString());
		this.payTips = new BigDecimal(oPosCheckPayment.payTips.toPlainString());
		this.payChange = new BigDecimal(oPosCheckPayment.payChange.toPlainString());
		this.payForeignCurrency = oPosCheckPayment.payForeignCurrency;
		this.payForeignTotal = new BigDecimal(oPosCheckPayment.payForeignTotal.toPlainString());
		this.payForeignTips = new BigDecimal(oPosCheckPayment.payForeignTips.toPlainString());
		this.surcharge = new BigDecimal(oPosCheckPayment.surcharge.toPlainString());
		this.foreignSurcharge = new BigDecimal(oPosCheckPayment.foreignSurcharge.toPlainString());
		this.payForeignChange = new BigDecimal(oPosCheckPayment.payForeignChange.toPlainString());
		this.currencyCode = oPosCheckPayment.currencyCode;
		this.currencyRate = new BigDecimal(oPosCheckPayment.currencyRate.toPlainString());
		this.changeBackCurrency = oPosCheckPayment.changeBackCurrency;
		this.special = oPosCheckPayment.special;
		this.nonRevenue = oPosCheckPayment.nonRevenue;
		this.membId = oPosCheckPayment.membId;
		this.mealUserId = oPosCheckPayment.mealUserId;
		for(i=0; i<3; i++)
			this.refData[i] = oPosCheckPayment.refData[i];
		this.payTime = oPosCheckPayment.payTime;
		
		if (oPosCheckPayment.payLocTime != null)
			this.payLocTime = new DateTime(oPosCheckPayment.payLocTime);

		this.payUserId = oPosCheckPayment.payUserId;
		this.payStatId = oPosCheckPayment.payStatId;
		this.voidTime = oPosCheckPayment.voidTime;
		
		if (oPosCheckPayment.voidLocTime != null)
			this.voidLocTime = new DateTime(oPosCheckPayment.voidLocTime);

		this.voidUserId = oPosCheckPayment.voidUserId;
		this.voidStatId = oPosCheckPayment.voidStatId;
		this.voidVdrsId = oPosCheckPayment.voidVdrsId;
		this.paySeq = oPosCheckPayment.paySeq;
		this.status = oPosCheckPayment.status;
		
		//handle extra info
		if(!oPosCheckPayment.getCheckExtraInfoArrayList().isEmpty()) {
			for(PosCheckExtraInfo oExtraInfo:oPosCheckPayment.getCheckExtraInfoArrayList())
				this.checkExtraInfoList.add(new PosCheckExtraInfo(oExtraInfo));
		}
		
		m_oPosGatewayTransactionsList = new PosPaymentGatewayTransactionsList();
		m_oPosGatewayTransactionsList = oPosCheckPayment.getPosPaymentGatewayTransactionsList();
		
		userInputValue = BigDecimal.ZERO;
		postingSuccess = false;
		
		bIsChangeToDummy = false;
		this.dummyPaymentRelationship = PosCheckPayment.DUMMY_PAYMENT_RELATIONSHIP_NO;
	}
	
	//init object with JSON Object
	public PosCheckPayment(JSONObject checkPaymentJSONObject) {
		readDataFromJson(checkPaymentJSONObject);
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("check_payment")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("check_payment")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("check_payment");
			if(tempJSONObject.isNull("PosCheckPayment")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}
	
	//read data list from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray checkPaymentJSONArray = new JSONArray();
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, true))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;

			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("check_payments")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(),	"", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("check_payments"))
				return null;
			
			checkPaymentJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("check_payments");
		}
		
		return checkPaymentJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject checkPaymentJSONObject) {
		JSONObject resultCheckPayment = null;
		JSONArray tempJSONArray = null;
		int i;
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		resultCheckPayment = checkPaymentJSONObject.optJSONObject("PosCheckPayment");
		if(resultCheckPayment == null)
			resultCheckPayment = checkPaymentJSONObject;
		
		this.init();
		
		this.cpayId = resultCheckPayment.optString("cpay_id");
		this.bdayId = resultCheckPayment.optString("cpay_bday_id");
		this.bperId = resultCheckPayment.optString("cpay_bper_id");
		this.shopId = resultCheckPayment.optInt("cpay_shop_id");
		this.oletId = resultCheckPayment.optInt("cpay_olet_id");
		this.chksId = resultCheckPayment.optString("cpay_chks_id");
		this.cptyId = resultCheckPayment.optString("cpay_cpty_id");
		this.paymId = resultCheckPayment.optInt("cpay_paym_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultCheckPayment.optString("cpay_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultCheckPayment.optString("cpay_short_name_l"+i);
		this.paymentType = resultCheckPayment.optString("cpay_payment_type");
		this.payTotal = new BigDecimal(resultCheckPayment.optString("cpay_pay_total", "0.0"));
		this.payTips = new BigDecimal(resultCheckPayment.optString("cpay_pay_tips", "0.0"));
		this.payChange = new BigDecimal(resultCheckPayment.optString("cpay_pay_change", "0.0"));
		this.payForeignCurrency = resultCheckPayment.optString("cpay_pay_foreign_currency", PosCheckPayment.PAY_FOREIGN_CURRENCY_LOCAL);
		this.payForeignTotal = new BigDecimal(resultCheckPayment.optString("cpay_pay_foreign_total", "0.0"));
		this.payForeignTips = new BigDecimal(resultCheckPayment.optString("cpay_pay_foreign_tips", "0.0"));
		this.payForeignChange = new BigDecimal(resultCheckPayment.optString("cpay_pay_foreign_change", "0.0"));
		this.surcharge = new BigDecimal(resultCheckPayment.optString("cpay_pay_surcharge", "0.0"));
		this.foreignSurcharge = new BigDecimal(resultCheckPayment.optString("cpay_pay_foreign_surcharge", "0.0"));
		this.currencyCode = resultCheckPayment.optString("cpay_currency_code");
		this.currencyRate = new BigDecimal(resultCheckPayment.optString("cpay_currency_rate", "0.0"));
		this.changeBackCurrency = resultCheckPayment.optString("cpay_change_back_currency", PosCheckPayment.CHANGE_BACK_CURRENCY_LOCAL);
		this.special = resultCheckPayment.optString("cpay_special", PosCheckPayment.SPECIAL_NO);
		this.nonRevenue = resultCheckPayment.optString("cpay_non_revenue", PosCheckPayment.NON_REVENUE_NO);
		this.membId = resultCheckPayment.optInt("cpay_memb_id");
		this.mealUserId = resultCheckPayment.optInt("cpay_meal_user_id");
		for(i=1; i<=3; i++) {
			this.refData[(i-1)] = resultCheckPayment.optString("cpay_ref_data"+i, null);
		}
		this.payTime = resultCheckPayment.optString("cpay_pay_time", null);
		 
		String sPayLocTime = resultCheckPayment.optString("cpay_pay_loctime");
		if(!sPayLocTime.isEmpty())
			this.payLocTime = oFormatter.parseDateTime(sPayLocTime);
		
		this.payUserId = resultCheckPayment.optInt("cpay_pay_user_id");
		this.payStatId = resultCheckPayment.optInt("cpay_pay_stat_id");
		
		this.voidTime = resultCheckPayment.optString("cpay_void_time", null);
		 
		String sVoidLocTime = resultCheckPayment.optString("cpay_void_loctime");
		if(!sVoidLocTime.isEmpty())
			this.voidLocTime = oFormatter.parseDateTime(sVoidLocTime);
		
		this.voidUserId = resultCheckPayment.optInt("cpay_void_user_id");
		this.voidStatId = resultCheckPayment.optInt("cpay_void_stat_id");
		this.voidVdrsId = resultCheckPayment.optInt("cpay_void_vdrs_id");
		this.paySeq = resultCheckPayment.optInt("cpay_seq");
		this.status = resultCheckPayment.optString("cpay_status", PosCheckPayment.STATUS_ACTIVE);
		
		//check whether pos check extra info record exist
		tempJSONArray = resultCheckPayment.optJSONArray("PosCheckExtraInfo");
		if(tempJSONArray != null) {
			PosCheckExtraInfoList oCheckExtraInfoList = new PosCheckExtraInfoList(tempJSONArray);
			this.checkExtraInfoList = oCheckExtraInfoList.getCheckExtraInfoList();
		}
		
		//check whether pos check payment gateway transaction record exist
		tempJSONArray = resultCheckPayment.optJSONArray("PosPaymentGatewayTransaction");
		if(tempJSONArray != null) {
			this.m_oPosGatewayTransactionsList = new PosPaymentGatewayTransactionsList(tempJSONArray);
		}
	}
	
	//construct the save request JSON
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		int i=0;
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("cpay_id", this.cpayId);
			addSaveJSONObject.put("cpay_bday_id", this.bdayId);
			addSaveJSONObject.put("cpay_bper_id", this.bperId);
			addSaveJSONObject.put("cpay_shop_id", this.shopId);
			addSaveJSONObject.put("cpay_olet_id", this.oletId);
			addSaveJSONObject.put("cpay_chks_id", this.chksId);
			addSaveJSONObject.put("cpay_cpty_id", this.cptyId);
			addSaveJSONObject.put("cpay_paym_id", this.paymId);
			for(i=1; i<=5; i++) {
				if(!this.name[i-1].isEmpty())
					addSaveJSONObject.put("cpay_name_l"+i, this.name[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(!this.shortName[i-1].isEmpty())
					addSaveJSONObject.put("cpay_short_name_l"+i, this.shortName[(i-1)]);
			}
			addSaveJSONObject.put("cpay_payment_type", this.paymentType);
			addSaveJSONObject.put("cpay_pay_total", this.payTotal);
			addSaveJSONObject.put("cpay_pay_tips", this.payTips);
			addSaveJSONObject.put("cpay_pay_change", this.payChange);
			addSaveJSONObject.put("cpay_pay_foreign_currency", this.payForeignCurrency);
			addSaveJSONObject.put("cpay_pay_foreign_total", this.payForeignTotal);
			addSaveJSONObject.put("cpay_pay_foreign_tips", this.payForeignTips);
			if(this.payForeignChange.compareTo(BigDecimal.ZERO) != 0)
				addSaveJSONObject.put("cpay_pay_foreign_change", this.payForeignChange);
			
			if(this.surcharge.compareTo(BigDecimal.ZERO) != 0)
				addSaveJSONObject.put("cpay_pay_surcharge", this.surcharge);
			if(this.foreignSurcharge.compareTo(BigDecimal.ZERO) != 0)
				addSaveJSONObject.put("cpay_pay_foreign_surcharge", this.foreignSurcharge);
			
			addSaveJSONObject.put("cpay_currency_code", this.currencyCode);
			addSaveJSONObject.put("cpay_currency_rate", this.currencyRate);
			if (!this.changeBackCurrency.isEmpty())
				addSaveJSONObject.put("cpay_change_back_currency", this.changeBackCurrency);
			if (!this.special.isEmpty())
				addSaveJSONObject.put("cpay_special", this.special);
			if (!this.nonRevenue.isEmpty())
				addSaveJSONObject.put("cpay_non_revenue", this.nonRevenue);
			if(this.membId > 0)
				addSaveJSONObject.put("cpay_memb_id", this.membId);
			if(this.mealUserId > 0)
				addSaveJSONObject.put("cpay_meal_user_id", this.mealUserId);
			for(i=1; i<=3; i++) {
				if(this.refData[(i-1)] != null)
					addSaveJSONObject.put("cpay_ref_data"+i, this.refData[(i-1)]);
			}
			if (this.payLocTime != null) {
				addSaveJSONObject.put("cpay_pay_time", this.payTime);
				addSaveJSONObject.put("cpay_pay_loctime", dateTimeToString(this.payLocTime));
			}
			addSaveJSONObject.put("cpay_pay_user_id", this.payUserId);
			addSaveJSONObject.put("cpay_pay_stat_id", this.payStatId);
			if (this.voidLocTime != null) {
				addSaveJSONObject.put("cpay_void_time", this.voidTime);
				addSaveJSONObject.put("cpay_void_loctime", dateTimeToString(this.voidLocTime));
			}
			if(this.voidUserId > 0)
				addSaveJSONObject.put("cpay_void_user_id", this.voidUserId);
			if(this.voidStatId > 0)
				addSaveJSONObject.put("cpay_void_stat_id", this.voidStatId);
			if(this.voidVdrsId > 0)
				addSaveJSONObject.put("cpay_void_vdrs_id", this.voidVdrsId);
			addSaveJSONObject.putOnce("cpay_seq", this.paySeq);
			addSaveJSONObject.putOnce("cpay_status", this.status);

			//construct the extra info list if exist
			if(this.checkExtraInfoList != null && !this.checkExtraInfoList.isEmpty()) {
				JSONArray chkExtraInfoJSONArray = new JSONArray();
				for(PosCheckExtraInfo oCheckExtraInfo:this.checkExtraInfoList) {
					if(oCheckExtraInfo.getCkeiId().compareTo("") == 0)
						chkExtraInfoJSONArray.put(oCheckExtraInfo.constructAddSaveJSON(false));
					else
						chkExtraInfoJSONArray.put(oCheckExtraInfo.constructAddSaveJSON(true));
				}
				try {
					addSaveJSONObject.put("checkExtraInfos", chkExtraInfoJSONArray);
				}
				catch(JSONException jsone) {
					jsone.printStackTrace();
				}
			}
			
			//construct the payment gateway transaction if exist
			if(this.m_oPosGatewayTransactionsList != null && !this.m_oPosGatewayTransactionsList.getPosPaymentGatewayTransactionsList().isEmpty()){
				JSONArray PayGatewayTransJSONArray = new JSONArray();
				for(PosPaymentGatewayTransactions oPaymentGatewayTrans:this.m_oPosGatewayTransactionsList.getPosPaymentGatewayTransactionsList()) {
					if(oPaymentGatewayTrans.getRefNo().isEmpty())
						continue;
					if(oPaymentGatewayTrans != null && oPaymentGatewayTrans.getPgtxId().isEmpty())
						PayGatewayTransJSONArray.put(oPaymentGatewayTrans.constructAddSaveJSON(false));
					else
						PayGatewayTransJSONArray.put(oPaymentGatewayTrans.constructAddSaveJSON(true));
				}
				try {
					addSaveJSONObject.put("paymentGatewayTransaction", PayGatewayTransJSONArray);
				}
				catch(JSONException jsone) {
					jsone.printStackTrace();
				}
			}
			
			//construct the payment other information if exist
			if(m_oPaymentOtherInfo != null && m_oPaymentOtherInfo.length() > 0) {
				try {
					addSaveJSONObject.put("paymentOtherInfo", m_oPaymentOtherInfo);
				}
				catch(JSONException jsone) {
					jsone.printStackTrace();
				}
			}
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}

	//change DateTime object to string for database insertion/update
	private String dateTimeToString (DateTime oDateTime) {
		if (oDateTime == null)
			return null;
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		String timeString = oFmt.print(oDateTime);
		
		return timeString;
	}

	//get cpayId
	public String getCpayId() {
		return this.cpayId;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.cpayId = "";
		this.bdayId = "";
		this.bperId = "";
		this.shopId = 0;
		this.oletId = 0;
		this.chksId = "";
		this.cptyId = "";
		this.paymId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.paymentType = "";
		this.payTotal = BigDecimal.ZERO;
		this.payTips = BigDecimal.ZERO;
		this.payChange = BigDecimal.ZERO;
		this.payForeignCurrency = PosCheckPayment.PAY_FOREIGN_CURRENCY_LOCAL;
		this.payForeignTotal = BigDecimal.ZERO;
		this.payForeignTips = BigDecimal.ZERO;
		this.payForeignChange = BigDecimal.ZERO;
		this.surcharge = BigDecimal.ZERO;
		this.foreignSurcharge = BigDecimal.ZERO;
		this.currencyCode = "";
		this.currencyRate = BigDecimal.ZERO;
		this.changeBackCurrency = PosCheckPayment.CHANGE_BACK_CURRENCY_LOCAL;
		this.special = PosCheckPayment.SPECIAL_NO;
		this.nonRevenue = PosCheckPayment.NON_REVENUE_NO;
		this.membId = 0;
		this.mealUserId = 0;
		if(this.refData == null)
			this.refData = new String[3];
		for(i=0; i<3; i++)
			this.refData[i] = null;
		this.payTime = null;
		this.payLocTime = null;
		this.payUserId = 0;
		this.payStatId = 0;
		this.voidTime = null;
		this.voidLocTime = null;
		this.voidUserId = 0;
		this.voidStatId = 0;
		this.voidVdrsId = 0;
		this.paySeq = 0;
		this.status = PosCheckPayment.STATUS_ACTIVE;
		
		if(this.checkExtraInfoList == null)
			this.checkExtraInfoList = new ArrayList<PosCheckExtraInfo>();
		else
			this.checkExtraInfoList.clear();
		
		if(this.m_oPaymentOtherInfo == null)
			this.m_oPaymentOtherInfo = new JSONObject();
		
		m_oPosGatewayTransactionsList = new PosPaymentGatewayTransactionsList();
		PosPaymentGatewayTransactions oPosPaymentGatewayTransactions = new PosPaymentGatewayTransactions();
		oPosPaymentGatewayTransactions.init();
		m_oPosGatewayTransactionsList.add(oPosPaymentGatewayTransactions);
		this.dummyPaymentRelationship = PosCheckPayment.DUMMY_PAYMENT_RELATIONSHIP_NO;
		bIsChangeToDummy = false;
	}
	
	//read data from database by cpay_id
	public boolean readById (String sCpayId, int iFindDeleted) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("checkPaymentId", sCpayId);
			requestJSONObject.put("findDeleted", iFindDeleted);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "getCheckPaymentById", requestJSONObject.toString());
	}
	
	//read data from database by cpay_olet_id, a list of cpay_bday_id
	public JSONArray readAllByOletIdTypeEmpAndBdayIds(int iOutletId, String sPaymentType, int iEmployeeId, ArrayList<String> oBusinessDayIdList) {
		JSONObject requestJSONObject = new JSONObject(), oTempJSONObject = null;
		JSONArray oBDayIdJSONArray = new JSONArray(), oCheckPaymentsJSONArray;
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("paymentType", sPaymentType);
			requestJSONObject.put("employeeId", iEmployeeId);
			for(String sBdayId:oBusinessDayIdList) {
				oTempJSONObject = new JSONObject();
				oTempJSONObject.put("businessDayId", sBdayId);
				oBDayIdJSONArray.put(oTempJSONObject);
			}
			requestJSONObject.put("businessDayIds", oBDayIdJSONArray);
			
			oCheckPaymentsJSONArray = this.readDataListFromApi("gm", "pos", "getCheckPaymentsByOutletIdTypeEmpAndBusinessDayIds", requestJSONObject.toString());
		}catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		
		return oCheckPaymentsJSONArray;
	}
	
	//read data from database by cpay_olet_id, a list of cpay_bday_id with all employee id
	public JSONArray readAllByOletIdTypeAndBdayIds(int iOutletId, String sPaymentType, ArrayList<String> oBusinessDayIdList) {
		JSONObject requestJSONObject = new JSONObject(), oTempJSONObject = null;
		JSONArray oBDayIdJSONArray = new JSONArray(), oCheckPaymentsJSONArray;
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("paymentType", sPaymentType);
			for(String sBdayId:oBusinessDayIdList) {
				oTempJSONObject = new JSONObject();
				oTempJSONObject.put("businessDayId", sBdayId);
				oBDayIdJSONArray.put(oTempJSONObject);
			}
			requestJSONObject.put("businessDayIds", oBDayIdJSONArray);
			
			oCheckPaymentsJSONArray = this.readDataListFromApi("gm", "pos", "getCheckPaymentsByOutletIdTypeAndBusinessDayIds", requestJSONObject.toString());
		}catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		
		return oCheckPaymentsJSONArray;
	}
	
	//add or update a check payment to database
	public boolean addUpdate(boolean bUpdate) {
		JSONObject requestJSONObject = this.constructAddSaveJSON(bUpdate);
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveCheckPayment", requestJSONObject.toString(), false))
			return false;
		else
			return true;
	}
	
	//add or update multiple check payments record to database
	public boolean addUpdateWithMutlipleRecord(ArrayList<PosCheckPayment> oPosCheckPayments, String sCheckPrefixNo, int iOletId, int iActionUserId, int iActionStationId) {
		JSONObject tempCheckPaymentJSONObject = null, checkPaymentJSONObject = new JSONObject();
		JSONArray checkPaymentJSONArray = new JSONArray();
		
		for(PosCheckPayment oPosCheckPayment:oPosCheckPayments){
			if(oPosCheckPayment.getCpayId().equals(""))
				tempCheckPaymentJSONObject = oPosCheckPayment.constructAddSaveJSON(false);
			else
				tempCheckPaymentJSONObject = oPosCheckPayment.constructAddSaveJSON(true);
			checkPaymentJSONArray.put(tempCheckPaymentJSONObject);
		}
		
		try {
			checkPaymentJSONObject.put("checkPayments", checkPaymentJSONArray);
			checkPaymentJSONObject.put("checkPrefixNum", sCheckPrefixNo);
			checkPaymentJSONObject.put("oletId", iOletId);
			checkPaymentJSONObject.put("actionUserId", iActionUserId);
			checkPaymentJSONObject.put("actionStationId", iActionStationId);
			checkPaymentJSONObject.put("timezoneOffset", iTimeZoneOffset);
			if(!sTimeZoneName.isEmpty())
				checkPaymentJSONObject.put("timezoneName", sTimeZoneName);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveMultiCheckPayments", checkPaymentJSONObject.toString(), false))
			return false;
		else
			return true;
	}
	
	public JSONObject getPaymentOtherInfo() {
		return this.m_oPaymentOtherInfo;
	}
	
	public void setPaymentOtherInfo(JSONObject oPaymentOtherInfo) {
		this.m_oPaymentOtherInfo = oPaymentOtherInfo;
	}
	
	//set bday id
	public void setBdayId(String sBdayId) {
		this.bdayId = sBdayId;
	}
	
	//set bper id
	public void setBperId(String sBperId) {
		this.bperId = sBperId;
	}
	
	//set shop id
	public void setShopId(int iShopId) {
		this.shopId = iShopId;
	}
	
	//set outlet id
	public void setOletId(int iOletId) {
		this.oletId = iOletId;
	}
	
	//set check id
	public void setChksId(String sChksId) {
		this.chksId = sChksId;
	}
	
	//set payment method id
	public void setPaymId(int iPaymId) {
		this.paymId = iPaymId;
	}
	
	//set name with index
	public void setName(int iIndex, String sName) {
		this.name[(iIndex-1)] = sName;
	}
	
	//set short name with index
	public void setShortName(int iIndex, String sShortName) {
		this.shortName[(iIndex-1)] = sShortName;
	}
	
	//set payment type
	public void setPaymentType(String sType) {
		this.paymentType = sType;
	}
	
	//set pay total
	public void setPayTotal(BigDecimal dPayTotal) {
		this.payTotal = dPayTotal;
	}
	
	//set pay tips
	public void setPayTips(BigDecimal dPayTips) {
		this.payTips = dPayTips;
	}
	
	//set pay change
	public void setPayChange(BigDecimal dPayChange) {
		this.payChange = dPayChange;
	}

	//set pay foreign currency
	public void setPayForeignCurrency(String sPayForeignCurrency) {
		this.payForeignCurrency = sPayForeignCurrency;
	}
	
	//set pay foreign total
	public void setPayForeignTotal(BigDecimal dPayForeignTotal) {
		this.payForeignTotal = dPayForeignTotal;
	}
	
	//set pay foreign tips
	public void setPayForeignTips(BigDecimal dPayForeignTips) {
		this.payForeignTips = dPayForeignTips;
	}
	
	//set pay foreign change
	public void setPayForeignChange(BigDecimal dPayForeignChange) {
		this.payForeignChange = dPayForeignChange;
	}
	
	public BigDecimal getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(BigDecimal dSurcharge) {
		this.surcharge = dSurcharge;
	}

	public BigDecimal getForeignSurcharge() {
		return foreignSurcharge;
	}

	public void setForeignSurcharge(BigDecimal dForeignSurcharge) {
		this.foreignSurcharge = dForeignSurcharge;
	}

	//set currency code
	public void setCurrencyCode(String sCurrencyCode) {
		this.currencyCode = sCurrencyCode;
	}
	
	//set currency rate
	public void setCurrencyRate(BigDecimal dCurrencyRate) {
		this.currencyRate = dCurrencyRate;
	}
	
	//set change back currency
	public void setChangeBackCurrency(String sChangeBackCurrency) {
		this.changeBackCurrency = sChangeBackCurrency;
	}
	
	//set special
	public void setSpecial(String sSpecial) {
		this.special = sSpecial;
	}
	
	//set non revenue
	public void setNonRevenue(String sNonRevenue) {
		this.nonRevenue = sNonRevenue;
	}
	
	//set member id
	public void setMemberId(int iMemberId) {
		this.membId = iMemberId;
	}
	
	//set user id
	public void setMealUserId(int iUserId) {
		this.mealUserId = iUserId;
	}
	
	//set reference data
	public void setRefData(int iIndex, String sRefData) {
		this.refData[(iIndex-1)] = sRefData;
	}
	
	//set pay time
	public void setPayTime(String sPayTime) {
		this.payTime = sPayTime;
	}
	
	//set pay local time
	public void setPayLocTime(DateTime oPayLocTime) {
		this.payLocTime = oPayLocTime;
	}
	
	//set pay user id
	public void setPayUserId(int iPayUserId) {
		this.payUserId = iPayUserId;
	}
	
	//set pay stat id
	public void setPayStatId(int iPayStatId) {
		this.payStatId = iPayStatId;
	}
	
	//set void time
	public void setVoidTime(String sVoidTime) {
		this.voidTime = sVoidTime;
	}
	
	//set void local time
	public void setVoidLocTime(DateTime oVoidLocTime) {
		this.voidLocTime = oVoidLocTime;
	}
	
	//set void user id
	public void setVoidUserId(int iVoidUserId) {
		this.voidUserId = iVoidUserId;
	}
	
	//set void stat id
	public void setVoidStatId(int iVoidStatId) {
		this.voidStatId = iVoidStatId;
	}

	//set void vdrs id
	public void setVoidVdrsId(int iVoidVdrsId) {
		this.voidVdrsId = iVoidVdrsId;
	}
	
	//set payment sequence
	public void setPaySeq(int iPaySeq) {
		this.paySeq = iPaySeq;
	}
	
	//set status
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	// set payment gateway transaction
	public void setPaymentGatewayTransaction(PosPaymentGatewayTransactionsList oPaymentGatewayTransactionsList){
		m_oPosGatewayTransactionsList = oPaymentGatewayTransactionsList;
	}
	
	// add payment gateway transaction to list
	public void addPaymentGatewayTransaction(PosPaymentGatewayTransactions oPaymentGatewayTransactions){
		m_oPosGatewayTransactionsList.add(oPaymentGatewayTransactions);
	}
	
	// void sale payment gateway transaction
	public void voidSalePaymentGatewayTransaction(){
		DateTime oCurrentTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		for(PosPaymentGatewayTransactions oPosPaymentGatewayTransactions : m_oPosGatewayTransactionsList.getPosPaymentGatewayTransactionsList()){
			if(oPosPaymentGatewayTransactions.getType().equals(PosPaymentGatewayTransactions.TYPE_SALE) || 
					oPosPaymentGatewayTransactions.getType().equals(PosPaymentGatewayTransactions.TYPE_ADJUST_TIP)){
				oPosPaymentGatewayTransactions.setStatus(PosPaymentGatewayTransactions.STATUS_VOIDED);
				oPosPaymentGatewayTransactions.setVoidTime(formatter.print(AppGlobal.convertTimeToUTC(oCurrentTime)));
				oPosPaymentGatewayTransactions.setVoidLocTime(oCurrentTime);
				oPosPaymentGatewayTransactions.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
			}
		}
	}
	
	//add extra info to list
	public void addExtraInfoToList(PosCheckExtraInfo oCheckExtraInfo) {
		this.checkExtraInfoList.add(oCheckExtraInfo);
	}
	
	//add extra info to list
	public void addExtraInfoToList(int iOutletId, String sSection, String sVariable, int iIndex, String sValue) {
		PosCheckExtraInfo oPosCheckExtraInfo = new PosCheckExtraInfo();
		oPosCheckExtraInfo.setOutletId(iOutletId);
		oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
		oPosCheckExtraInfo.setSection(sSection);
		oPosCheckExtraInfo.setVariable(sVariable);
		oPosCheckExtraInfo.setIndex(iIndex);
		oPosCheckExtraInfo.setValue(sValue);
		this.checkExtraInfoList.add(oPosCheckExtraInfo);
	}
	
	//update extra info
	public void updateExtraInfo(String sSection, String sVariable, int iIndex, String sValue){
		if(!checkExtraInfoList.isEmpty()) {
			for(PosCheckExtraInfo oTempExtraInfo: checkExtraInfoList) {
				if(oTempExtraInfo.getSection().equals(sSection) && oTempExtraInfo.getVariable().equals(sVariable) && oTempExtraInfo.getIndex() == iIndex) {
					oTempExtraInfo.setValue(sValue);
					break;
				}
			}
		}
	}
	
	// set user input value
	public void setUserInputValue(BigDecimal dUserInputValue) {
		this.userInputValue = dUserInputValue;
	}
	
	// set posting success
	public void setPostingSuccess(boolean bSuccess) {
		this.postingSuccess = bSuccess;
	}
	
	// set is change dummy payment
	public void setIsChangeDummyPayment(boolean bIsChange){
		this.bIsChangeToDummy = bIsChange;
	}
	
	// get is change dummy payment
	public boolean isChangeToDummyPayment(){
		return this.bIsChangeToDummy;
	}
	
	// set payment rounding status
	public void setDummyPaymentRelationship(String sDummyPaymentRelationship){
		this.dummyPaymentRelationship = sDummyPaymentRelationship;	
	}
	
	// get payment rounding relationship
	public String getDummyPaymentRelationship(){
		return this.dummyPaymentRelationship;
	}
	
	public boolean isCheckExtraInfoExistBySectionVariableAndIndex(String sSection, String sVariable, int iIndex) {
		boolean bFound = false;
		if(!checkExtraInfoList.isEmpty()) 
			for(PosCheckExtraInfo oCheckExtraInfo:checkExtraInfoList) {
				if(oCheckExtraInfo.getSection().equals(sSection) && oCheckExtraInfo.getVariable().equals(sVariable) && oCheckExtraInfo.getIndex() == iIndex) {
					bFound = true;
					break;
				}
			}
		return bFound;
	}
	
	//find extra info from list
	public PosCheckExtraInfo getExtraInfoFromList(String sSection, String sVariable, int iIndex) {
		PosCheckExtraInfo oPosCheckExtraInfo = null;
		if(!checkExtraInfoList.isEmpty()) {
			for(PosCheckExtraInfo oTempExtraInfo: checkExtraInfoList) {
				if(oTempExtraInfo.getSection().equals(sSection) && oTempExtraInfo.getVariable().equals(sVariable) && oTempExtraInfo.getIndex() == iIndex) {
					oPosCheckExtraInfo = oTempExtraInfo;
					break;
				}
			}
		}
		
		return oPosCheckExtraInfo;
	}
	
	//remove extra info from list
	public void removeExtraInfoFromList(String sSection, String sVariable, int iIndex) {
		int iTargetIndex = -1;
		if(!checkExtraInfoList.isEmpty()) {
			for(int i=0; i<checkExtraInfoList.size(); i++) {
				PosCheckExtraInfo oTempExtraInfo = checkExtraInfoList.get(i);
				if(oTempExtraInfo.getSection().equals(sSection) && oTempExtraInfo.getVariable().equals(sVariable) && oTempExtraInfo.getIndex() == iIndex) {
					iTargetIndex = i;
					break;
				}
			}
			
			if(iTargetIndex > 0)
				checkExtraInfoList.remove(iTargetIndex);
		}
	}
	
	//get business day id
	public String getBusinessDayId() {
		return this.bdayId;
	}
	
	//get business period id
	public String getBusinessPeriodId() {
		return this.bperId;
	}
	
	//get shop id
	public int getShopId() {
		return this.shopId;
	}
	
	//get outlet id
	public int getOutletId() {
		return this.oletId;
	}
	
	//get check id
	public String getCheckId() {
		return this.chksId;
	}
	
	//get payment method id
	public int getPaymentMethodId() {
		return this.paymId;
	}
	
	//get name with index
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get short name with index
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get payment type
	public String getPaymentType() {
		return this.paymentType;
	}
	
	//get member id
	public int getMemberId() {
		return this.membId;
	}
	
	//get user id
	public int getMealUserId() {
		return this.mealUserId;
	}
	
	//get reference data
	public String getRefData(int iIndex) {
		return this.refData[(iIndex-1)];
	}
	
	//get reference data in JSON format
	//iType = 1 : in JSON array format with index and value e.g. [{"index":1, "value":"ABC"}, {"index":2, "value":"BCD"}]
	//iType = 2 : in JSON array e.g. ["ABC","BCD]
	public JSONArray getRefDataInJson(int iType) {
		JSONArray oRefDataArray = new JSONArray();
		for(int i=0; i<3; i++) {
			if(refData[i] != null && !refData[i].isEmpty()) {
				JSONObject oTemp = new JSONObject();
				if(iType == 2)
					oRefDataArray.put(refData[i]);
				else {
					try {
						oTemp.put("index", (i+1));
						oTemp.put("value", refData[i]);
						oRefDataArray.put(oTemp);
					}catch(JSONException e) {}
				}
			}
		}
		
		return oRefDataArray;
	}
	
	//get reference data by index and key name
	public String getRefDataByIndexAndKey(int iIndex, String sKey) {
		String sValue = null;
		if(this.refData[(iIndex-1)] != null) {
			try {
				JSONObject oRefDataJSONObject = new JSONObject(this.refData[(iIndex-1)]);
				if(oRefDataJSONObject.has(sKey) && !oRefDataJSONObject.isNull(sKey))
					sValue = oRefDataJSONObject.getString(sKey);
			}catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		return sValue;
	}
	
	
	public String getRefDataValueByIndexWithoutKey(int iIndex){
		String sRefData = "";
		JSONObject oPaymentRefObject = null;
		try {
			oPaymentRefObject = new JSONObject(this.getRefData(iIndex));
		} catch (JSONException e) {
			// return the value if the ref data is not a json format
			return this.getRefData(iIndex);
		}
		
		Iterator<String> keys = oPaymentRefObject.keys();
		while (keys.hasNext()) {
			String sKey = (String)keys.next();
			try {
				sRefData = sRefData + " " + oPaymentRefObject.getString(sKey);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sRefData;
	}
	
	
	//get pay time
	public String getPayTime() {
		return this.payTime;
	}
	
	//get pay local time
	public DateTime getPayLocTime() {
		return this.payLocTime;
	}
	
	//get pay user id
	public int getPayUserId() {
		return this.payUserId;
	}
	
	//get pay station id
	public int getPayStatId() {
		return this.payStatId;
	}
	
	//get void time
	public String getVoidTime() {
		return this.voidTime;
	}
	
	//get void user id
	public int getVoidUserId() {
		return this.voidUserId;
	}
	
	//get void station id
	public int getVoidStatId() {
		return this.voidStatId;
	}
	
	//get non revenue
	public String getNonRevenue() {
		return this.nonRevenue;
	}
	
	//get payment total
	public BigDecimal getPayTotal() {
		return this.payTotal;
	}
	
	//get payment foreign total
	public BigDecimal getPayForeignTotal() {
		return this.payForeignTotal;
	}
	
	//get pay tips
	public BigDecimal getPayTips() {
		return this.payTips;
	}
	
	//get pay foreign tips
	public BigDecimal getPayForeignTips() {
		return this.payForeignTips;
	}
	
	//get pay change
	public BigDecimal getPayChange() {
		return this.payChange;
	}
	
	//get pay foreign change
	public BigDecimal getPayForeignChange() {
		return this.payForeignChange;
	}
	
	//get pay foreign currency
	public String getPayForeignCurreny() {
		return this.payForeignCurrency;
	}
	
	//get foreign currency code
	public String getCurrencyCode() {
		return this.currencyCode;
	}
	
	//get foreign currency rate
	public BigDecimal getCurrencyRate() {
		return this.currencyRate;
	}
	
	//get check extra info list
	public List<PosCheckExtraInfo> getCheckExtraInfoArrayList() {
		return this.checkExtraInfoList;
	}
	
	//get user input value
	public BigDecimal getUserInputValue() {
		return this.userInputValue;
	}

	//get posting success
	public boolean getPostingSuccess() {
		return this.postingSuccess;
	}
	
	//get payment seq
	public int getPaySeq() {
		return this.paySeq;
	}
	
	//check payment is old 
	public boolean isOldPayment() {
		if(!this.cpayId.isEmpty())
			return true;
		else
			return false;
	}
	
	//check payment is deleted
	public boolean isDelete() {
		if(this.status.equals(PosCheckPayment.STATUS_DELETED))
			return true;
		else
			return false;
	}
	
	//check payment type equal to "coupon"
	public boolean isCouponPaymentType() {
		if(this.paymentType.equals(PosCheckPayment.PAYMENT_TYPE_COUPON))
			return true;
		else
			return false;
	}
	
	//check payment whether have pms posting
	public boolean havePmsPayment() {
		for(PosCheckExtraInfo oCheckExtraInfo:this.checkExtraInfoList) {
			if(oCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_PMS)){
				return true;
			}
		}
		return false;
	}
	
	public int getPmsPaymentIntfId() {
		int iIntfId = 0;
		for(PosCheckExtraInfo oCheckExtraInfo:this.checkExtraInfoList) {
			if(oCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_PMS) && oCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERFACE_ID)){
				iIntfId = Integer.parseInt(oCheckExtraInfo.getValue());
			}
		}
		return iIntfId;
	}
	
	//check payment whether have membership posting
	public boolean haveMembershipPayment() {
		for(PosCheckExtraInfo oCheckExtraInfo:this.checkExtraInfoList) {
			if(oCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE)){
				return true;
			}
		}
		return false;
	}
	
	//check payment whether have voucher posting
	public boolean haveVoucherPayment() {
		for(PosCheckExtraInfo oCheckExtraInfo:this.checkExtraInfoList) {
			if(oCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_VOUCHER_INTERFACE)){
				return true;
			}
		}
		return false;
	}
	
	// check this payment have active authorization to post
	public boolean isActiveAuthorizedTransactionPayment() {
		for(PosCheckExtraInfo oCheckExtraInfo:this.checkExtraInfoList) {
			if (oCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SPA_STANDARD_TYPE_KEY)) {
				String sTempAuthType = oCheckExtraInfo.getValue();
				if (sTempAuthType.equals(PosPaymentGatewayTransactions.TYPE_AUTH))
					return true;
			}
				
		}
		return false;
	}
	
	//check payment type equal to "octopus"
	public boolean isOctopusPaymentType() {
		if(this.paymentType.equals(PosCheckPayment.PAYMENT_TYPE_OCTOPUS))
			return true;
		else
			return false;
	}
	
	//check payment type equal to "rewriteCard"
	public boolean isRewriteCardPaymentType() {
		if(this.paymentType.equals(PosCheckPayment.PAYMENT_TYPE_REWRITE_CARD))
			return true;
		else
			return false;
	}
	
	//check payment type equal to "credit_card"
	public boolean isCreditCardsPaymentType() {
		return paymentType.equals(PosCheckPayment.PAYMENT_TYPE_CREDIT_CARD);
	}
	
	public int getMembershipInterfaceId() {
		int iIntfId = 0;
		for(PosCheckExtraInfo oCheckExtraInfo:this.checkExtraInfoList) {
			if(oCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE) && oCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERFACE_ID)){
				iIntfId = Integer.parseInt(oCheckExtraInfo.getValue());
				break;
			}
		}
		
		return iIntfId;
	}
	
	public int getVoucherInterfaceId() {
		int iIntfId = 0;
		for(PosCheckExtraInfo oCheckExtraInfo:this.checkExtraInfoList) {
			if(oCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_VOUCHER_INTERFACE) && oCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERFACE_ID)){
				iIntfId = Integer.parseInt(oCheckExtraInfo.getValue());
				break;
			}
		}
		
		return iIntfId;
	}
	
	public int getLoyaltyInterfaceId() {
		int iIntfId = 0;
		for(PosCheckExtraInfo oCheckExtraInfo:this.checkExtraInfoList) {
			if(oCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_LOYALTY) && oCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERFACE_ID)){
				iIntfId = Integer.parseInt(oCheckExtraInfo.getValue());
				break;
			}
		}
		return iIntfId;
	}
	
	//get any payment interface attached id
	public int getPaymentInterfaceId() {
		int iIntfId = 0;
		for(PosCheckExtraInfo oCheckExtraInfo:this.checkExtraInfoList) {
			if(oCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE) && oCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERFACE_ID)){
				iIntfId = Integer.parseInt(oCheckExtraInfo.getValue());
				break;
			}
		}
		
		return iIntfId;
	}
	
	//get any gaming interface attached id
	public int getGamingInterfaceId() {
		int iIntfId = 0;
		for(PosCheckExtraInfo oCheckExtraInfo:this.checkExtraInfoList) {
			if(oCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_GAMING_INTERFACE) && oCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERFACE_ID)){
				iIntfId = Integer.parseInt(oCheckExtraInfo.getValue());
				break;
			}
		}
		
		return iIntfId;
	}
	
	// get the payment gateway transaction for this payment
	public PosPaymentGatewayTransactionsList getPosPaymentGatewayTransactionsList() {
		return this.m_oPosGatewayTransactionsList;
	}
	
	// check whether it is pay by foreign currency
	public boolean isPayByForeignCurrency() {
		if(payForeignCurrency.equals(PosCheckPayment.PAY_FOREIGN_CURRENCY_FOREIGN))
			return true;
		else
			return false;
	}
	
	// check whether it is change back foreign currency
	public boolean isChangeBackInForeignCurrency() {
		if(changeBackCurrency.equals(PosCheckPayment.CHANGE_BACK_CURRENCY_FOREIGN))
			return true;
		else
			return false;
	}
	
	// set timezone
	public void setTimeZone(int iOffset, String sName) {
		iTimeZoneOffset = iOffset;
		sTimeZoneName = sName;
	}
}
