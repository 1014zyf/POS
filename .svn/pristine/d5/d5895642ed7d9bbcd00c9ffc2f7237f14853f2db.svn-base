//Database: pos_payment_methods Payment methods
package app.model;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import externallib.Util;

public class PosPaymentMethod {
	private int paymId;
	private String code;
	private String[] name;
	private String[] shortName;
	private int seq;
	private int paycId;
	private int paygId;
	private String paymentType;
	private String payForeignCurrency;
	private String currencyCode;
	private String currencyRound;
	private int currencyDecimal;
	private String tips;
	private int openDrawer1;
	private int openDrawer2;
	private String changeBackCurrency;
	private String special;
	private String nonRevenue;
	private String rules;
	private BigDecimal maxLimit;
	private String status;
	
	private PosInterfaceConfigList m_oInterfaceConfigList;
	
	//payment type
	public static String PAYMENT_TYPE_NO_SPECIFIED = "";
	public static String PAYMENT_TYPE_CASH = "cash";
	public static String PAYMENT_TYPE_CREDIT_CARD = "credit_card";
	public static String PAYMENT_TYPE_DUTY_MEAL = "duty_meal";
	public static String PAYMENT_TYPE_ON_CREDIT = "on_credit";
	public static String PAYMENT_TYPE_VOUCHER = "voucher";
	public static String PAYMENT_TYPE_COUPON = "coupon";
	public static String PAYMENT_TYPE_RESERVATION = "reservation_payment";
	public static String PAYMENT_TYPE_MEMBER = "member";
	public static String OCTOPUS_TYPE_COUPON = "octopus";
	
	// payforeignCurrency
	public static String PAY_FOREIGN_CURRENCY_LOCAL = "";
	public static String PAY_FOREIGN_CURRENCY_FOREIGN = "y";
	
	//non revenue
	public static String NON_REVENUE_NO = "";
	public static String NON_REVENUE_YES = "y";
	
	//tips
	public static String TIPS_NO_TIPS = "";
	public static String TIPS_HAVE_TIPS = "t";
	
	// changeBackCurrency
	public static String CHANGE_BACK_CURRENCY_LOCAL = "";
	public static String CHANGE_BACK_CURRENCY_FOREIGN = "f";
	
	// rounding
	private static String CURRENCY_ROUND_ROUND_OFF = "";
	private static String CURRENCY_ROUND_ROUND_UP = "1";
	private static String CURRENCY_ROUND_ROUND_DOWN = "2";
	private static String CURRENCY_ROUND_TO_NEARESET_5 = "3";
	private static String CURRENCY_ROUND_UP_TO_NEAREST_5 = "4";
	private static String CURRENCY_ROUND_DOWN_TO_NEAREST_5 = "5";
	
	// special
	public static String SPECIAL_STANDARD = "";
	public static String SPECIAL_TAIWAN_NON_GUI = "g";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	public static String STATUS_SUSPENDED = "s";
	
	//init object with initialize value
	public PosPaymentMethod () {
		this.init();
	}
	
	//init obejct with JSONObject
	public PosPaymentMethod(JSONObject paymentMethodJSONObject) {
		readDataFromJson(paymentMethodJSONObject);
	}
	
	//Init object from database by paym_id
	public PosPaymentMethod (int iPaymId) {
		int i=0;
		JSONObject requestJSONObject = new JSONObject();
		
		this.init();
		this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		try {
			requestJSONObject.put("paymentMethodId", Integer.toString(iPaymId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		this.readDataFromApi("gm", "pos", "getPaymentMethodById", requestJSONObject.toString());
	}

	//add instant printing command to printing module
	public boolean openDrawer(int iBdayId, int iShopId, int iOletId, int iActionUserId, int iActionStationId, int iTargetPrtqId, String sCommand){
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("businessDayId", iBdayId);
			requestJSONObject.put("shopId", iShopId);
			requestJSONObject.put("oletId", iOletId);
			requestJSONObject.put("actionUserId", iActionUserId);
			requestJSONObject.put("actionStationId", iActionStationId);
			requestJSONObject.put("printQ", iTargetPrtqId);
			requestJSONObject.put("printerCommand", sCommand);
			
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "openDrawer", requestJSONObject.toString(), true))
			return false;
		else
			return true;
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("payment_method")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("payment_method")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("payment_method");
			if(tempJSONObject.isNull("PosPaymentMethod")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);			
		}
		
		return bResult;
	}

	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray paymentMethodJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("paymentMethods")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("paymentMethods")) 
				return null;
			
			paymentMethodJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("paymentMethods");
		}
		
		return paymentMethodJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject paymentMethodJSONObject) {
		JSONObject resultPaymentMethod = null;
		int i=0;
		
		resultPaymentMethod = paymentMethodJSONObject.optJSONObject("PosPaymentMethod");
		if(resultPaymentMethod == null)
			resultPaymentMethod = paymentMethodJSONObject;
		
		this.init();
		this.paymId = resultPaymentMethod.optInt("paym_id");
		this.code = resultPaymentMethod.optString("paym_code");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultPaymentMethod.optString("paym_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultPaymentMethod.optString("paym_short_name_l"+i);
		this.seq = resultPaymentMethod.optInt("paym_seq");
		this.paycId = resultPaymentMethod.optInt("paym_payc_id");
		this.paygId = resultPaymentMethod.optInt("paym_payg_id");
		this.paymentType = resultPaymentMethod.optString("paym_payment_type", PosPaymentMethod.PAYMENT_TYPE_NO_SPECIFIED);
		this.payForeignCurrency = resultPaymentMethod.optString("paym_pay_foreign_currency", PosPaymentMethod.PAY_FOREIGN_CURRENCY_LOCAL);
		this.currencyCode = resultPaymentMethod.optString("paym_currency_code");
		this.currencyRound = resultPaymentMethod.optString("paym_currency_round", PosPaymentMethod.CURRENCY_ROUND_ROUND_OFF);
		this.currencyDecimal = resultPaymentMethod.optInt("paym_currency_decimal", 0);
		this.tips = resultPaymentMethod.optString("paym_tips", PosPaymentMethod.TIPS_NO_TIPS);
		this.openDrawer1 = resultPaymentMethod.optInt("paym_open_drawer1");
		this.openDrawer2 = resultPaymentMethod.optInt("paym_open_drawer2");
		this.changeBackCurrency = resultPaymentMethod.optString("paym_change_back_currency", PosPaymentMethod.CHANGE_BACK_CURRENCY_LOCAL);
		this.special = resultPaymentMethod.optString("paym_special", PosPaymentMethod.SPECIAL_STANDARD);
		this.nonRevenue = resultPaymentMethod.optString("paym_non_revenue", PosPaymentMethod.NON_REVENUE_NO);
		this.rules = resultPaymentMethod.optString("paym_rules", null);
		this.maxLimit = new BigDecimal(resultPaymentMethod.optString("paym_max_limit", "0.0"));
		this.status = resultPaymentMethod.optString("paym_status", PosPaymentMethod.STATUS_ACTIVE);
		
		if(paymentMethodJSONObject.has("paymentConfigs")) 
			this.m_oInterfaceConfigList = new PosInterfaceConfigList(paymentMethodJSONObject.optJSONArray("paymentConfigs"));
		else
			this.m_oInterfaceConfigList = null;
	}

	//construct the save request JSON
	private JSONObject constructAddSaveJSON(boolean bUpdate) {
		int i=0;
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("paym_id", this.paymId);
			addSaveJSONObject.put("paym_code", this.code);
			for(i=1; i<=5; i++)
				addSaveJSONObject.put("paym_name_l"+i, this.name[(i-1)]);
			for(i=1; i<=5; i++)
				addSaveJSONObject.put("paym_short_name_l"+i, this.shortName[(i-1)]);
			addSaveJSONObject.put("paym_seq", this.seq);
			addSaveJSONObject.put("paym_payc_id", this.paycId);
			addSaveJSONObject.put("paym_payg_id", this.paygId);
			addSaveJSONObject.put("paym_payment_type", this.paymentType);
			if (this.payForeignCurrency.isEmpty())
				addSaveJSONObject.put("paym_pay_foreign_currency", "");
			else
				addSaveJSONObject.put("paym_pay_foreign_currency", this.payForeignCurrency);
			addSaveJSONObject.put("paym_currency_code", this.currencyCode);
			addSaveJSONObject.put("paym_currency_round", this.currencyRound);
			addSaveJSONObject.put("paym_currency_decimal", this.currencyDecimal);
			if (this.tips.isEmpty())
				addSaveJSONObject.put("paym_tips", "");
			else
				addSaveJSONObject.put("paym_tips", this.tips);
			addSaveJSONObject.put("paym_open_drawer1", this.openDrawer1);
			addSaveJSONObject.put("paym_open_drawer2", this.openDrawer2);
			if (this.changeBackCurrency.isEmpty())
				addSaveJSONObject.put("paym_change_back_currency", "");
			else
				addSaveJSONObject.put("paym_change_back_currency", this.changeBackCurrency);
			if (this.special.isEmpty())
				addSaveJSONObject.put("paym_special", "");
			else
				addSaveJSONObject.put("paym_special", this.special);
			if (this.nonRevenue.isEmpty())
				addSaveJSONObject.put("paym_non_revenue", "");
			else
				addSaveJSONObject.put("paym_non_revenue", this.nonRevenue);
			if (this.rules != null)
				addSaveJSONObject.put("paym_rules", this.rules);
			else
				addSaveJSONObject.put("paym_rules", "");
			addSaveJSONObject.put("paym_max_limit", this.maxLimit);
			addSaveJSONObject.put("paym_status", this.status);

		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	// init value
	public void init () {
		int i=0;
		
		this.paymId = 0;
		this.code = "";
		this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.paycId = 0;
		this.paygId = 0;
		this.paymentType = PosPaymentMethod.PAYMENT_TYPE_NO_SPECIFIED;
		this.payForeignCurrency = PosPaymentMethod.PAY_FOREIGN_CURRENCY_LOCAL;
		this.currencyCode = "";
		this.currencyRound = PosPaymentMethod.CURRENCY_ROUND_ROUND_OFF;
		this.currencyDecimal = 0;
		this.tips = PosPaymentMethod.TIPS_NO_TIPS;
		this.openDrawer1 = 0;
		this.openDrawer2 = 0;
		this.changeBackCurrency = PosPaymentMethod.CHANGE_BACK_CURRENCY_LOCAL;
		this.special = PosPaymentMethod.SPECIAL_STANDARD;
		this.nonRevenue = PosPaymentMethod.NON_REVENUE_NO;
		this.rules = null;
		this.maxLimit = BigDecimal.ZERO;
		this.status = PosPaymentMethod.STATUS_ACTIVE;
		
		if(this.m_oInterfaceConfigList == null)
			this.m_oInterfaceConfigList = new PosInterfaceConfigList();
		else
			this.m_oInterfaceConfigList.clearInterfaceConfigList();
	}

	//read data from database by paym_id
	public boolean readPaymentMethodById(int iPaymentMethodId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("paymentMethodId", iPaymentMethodId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "getPaymentMethodById", requestJSONObject.toString());
	}
	
	//read all function records
	public JSONArray readAll() {
		JSONArray responseJSONArray = null;
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllPaymentMethods", null);

		return responseJSONArray;
	}
	
	//read all function records
	public JSONArray readAllWithAccessRight(int shopId, int outletId, String sBusinessDay, boolean bIsHoliday, boolean bIsDayBeforeHoliday, boolean bIsSpecialDay, boolean bIsDayBeforeSpecialDay, int iWeekday) {
		JSONArray responseJSONArray = null;
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("shopId", shopId);
			requestJSONObject.put("outletId", outletId);
			requestJSONObject.put("businessDay", sBusinessDay);
			requestJSONObject.put("isHoliday", bIsHoliday);
			requestJSONObject.put("isDayBeforeHoliday", bIsDayBeforeHoliday);
			requestJSONObject.put("isSpecialDay", bIsSpecialDay);
			requestJSONObject.put("isDayBeforeSpecialDay", bIsDayBeforeSpecialDay);
			requestJSONObject.put("weekday", iWeekday);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllPaymentMethodsWithAccessControl", requestJSONObject.toString());

		return responseJSONArray;
	}

	//set name with index
	protected void setName(int iIndex, String sName) {
		this.name[(iIndex-1)] = sName;
	}
	
	//set short name with index
	protected void setShortName(int iIndex, String sShortName) {
		this.shortName[(iIndex-1)] = sShortName;
	}
	
	//set payment code
	protected void setPaymentCode(String sCode) {
		this.code = sCode;
	}
	
	//set sequence
	protected void setSequence(int iSeq) {
		this.seq = iSeq;
	}
	
	//set payment class id
	protected void setPaymentClassId(int iPaycId) {
		this.paycId = iPaycId;
	}
	
	//set payment group id
	protected void setPaymentGroupId(int iPaygId) {
		this.paygId = iPaygId;
	}
	
	//set payment type 
	protected void setPaymentType(String sPaymentType) {
		this.paymentType = sPaymentType;
	}
	
	//set tips
	protected void setTips(String sTips) {
		this.tips = sTips;
	}
	
	//set foreign currency
	protected void setForeignCurrency(String sPayForeignCurrency) {
		this.payForeignCurrency = sPayForeignCurrency;
	}
	
	//set currency code
	protected void setCurrencyCode(String sCurrencyCode) {
		this.currencyCode = sCurrencyCode;
	}
	
	//set change back currency
	protected void setChangeBackCurrency(String sChangeBackCurrency) {
		this.changeBackCurrency = sChangeBackCurrency;
	}
	
	//set max limit
	protected void setMaxLimit(BigDecimal dMaxLimit) {
		this.maxLimit = dMaxLimit;
	}
	
	//get paymId
	public int getPaymId() {
		return this.paymId;
	}

	
	//get payment code
	public String getPaymentCode() {
		return this.code;
	}
	
	//get name with index
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get short name with index
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get payment sequence
	public int getPaymentSequence() {
		return this.seq;
	}
	
	//get payment class id
	public int getPaymentClassId() {
		return this.paycId;
	}
	
	//get payment group id
	public int getPaymentGroupId() {
		return this.paygId;
	}
	
	//get payment type
	public String getPaymentType() {
		return this.paymentType;
	}

	//get foreign currency
	public String getPayForeignCurrency() {
		return this.payForeignCurrency;
	}
	
	//get currency code
	public String getCurrencyCode() {
		return this.currencyCode;
	}
	
	//get tips
	public String getTips() {
		return this.tips;
	}
	
	//get open drawer with index
	public int getOpenDrawer(int iIndex) {
		int iOpenDrawer = 0;
		
		switch(iIndex) {
			case 1:
				iOpenDrawer = this.openDrawer1;
				break;
			case 2:
				iOpenDrawer = this.openDrawer2;
				break;
		}
		
		return iOpenDrawer;
	}
	
	//get change back currency
	public String getChangebackCurrency() {
		return this.changeBackCurrency;
	}
	
	//get special
	public String getSpecial() {
		return this.special;
	}
	
	//get rules
	public String getRules() {
		return this.rules;
	}
	
	// get rules by key
	public String getRuleByKey(String sKey) {
		if(this.rules == null)
			return "";
		
		try {
			JSONObject oRules = new JSONObject(this.rules);
			if (oRules.has(sKey) && !oRules.isNull(sKey)) {
				return oRules.getString(sKey);
			}else
				return "";
		}catch (JSONException jsone) {
			return "";
		}
	}
	
	//get max limit
	public BigDecimal getMaxLimit() {
		return this.maxLimit;
	}
	
	//get non revenue
	public String getNonRevenue() {
		return this.nonRevenue;
	}
	
	//whether have interface configs
	public boolean hasInterfaceConfig() {
		if(this.m_oInterfaceConfigList != null && this.m_oInterfaceConfigList.getInterfaceConfigCount() > 0)
			return true;
		else
			return false;
	}
	
	//get payment configs
	public List<PosInterfaceConfig> getInterfaceConfig(String sInterfaceType) {
		if(this.m_oInterfaceConfigList == null)
			return null;
		else
			return this.m_oInterfaceConfigList.getInterfaceConfigListByInterfaceType(sInterfaceType);
	}
	
	public boolean HaveTips() {
		return this.tips.equals(PosPaymentMethod.TIPS_HAVE_TIPS);
	}
	
	//is payment type equal to cash
	public boolean isCashPaymentType() {
		return this.paymentType.equals(PAYMENT_TYPE_CASH);
	}
	
	//is payment type equal to credit card
	public boolean isCreditCardPaymentType() {
		return this.paymentType.equals(PAYMENT_TYPE_CREDIT_CARD);
	}
	
	//is payment type equal to duty meal
	public boolean isDutyMealPaymentType() {
		return this.paymentType.equals(PAYMENT_TYPE_DUTY_MEAL);
	}
	
	//is payment type equal to on-credit
	public boolean isOnCreditPaymentType() {
		return this.paymentType.equals(PAYMENT_TYPE_ON_CREDIT);
	}
	
	//is payment type equal to voucher
	public boolean isVoucherPaymentType() {
		return this.paymentType.equals(PAYMENT_TYPE_VOUCHER);
	}
	
	//is payment type equal to coupon
	public boolean isCouponPaymentType() {
		return this.paymentType.equals(PAYMENT_TYPE_COUPON);
	}
	
	//is payment type equal to Octopus
	public boolean isOctopusPaymentType() {
		return this.paymentType.equals(OCTOPUS_TYPE_COUPON);
	}
	
	//is payment type equal to reservation payment
	public boolean isReservationPayment() {
		return this.paymentType.equals(PAYMENT_TYPE_RESERVATION);
	}
	
	//is payment type equal to member payment
	public boolean isMemberPaymentType() {
		return this.paymentType.equals(PAYMENT_TYPE_MEMBER);
	}
	
	// Check if it is Taiwan non-GUI payment by special field
	public boolean isTaiwanNonGuiPayment() {
		return this.special.equals(SPECIAL_TAIWAN_NON_GUI);
	}
	
	//is payment with membership interface - LPS SVC payment
	public boolean isMembershipInterfaceLpsSvcPayment() {
		if(getInterfaceConfig(InfInterface.TYPE_MEMBERSHIP_INTERFACE) != null && 
				!getInterfaceConfig(InfInterface.TYPE_MEMBERSHIP_INTERFACE).isEmpty() &&
				getInterfaceConfig(InfInterface.TYPE_MEMBERSHIP_INTERFACE).get(0).getInterfaceVendorKey().equals(InfVendor.KEY_LPS_SVC))
			return true;
		else
			return false;
	}
	
	//is foreign currency payment
	public boolean isPayByForeignCurrency() {
		if(this.payForeignCurrency.equals(PosPaymentMethod.PAY_FOREIGN_CURRENCY_FOREIGN))
			return true;
		else
			return false;
	}
	
	//is change back in foreign currency
	public boolean isChangeBackInForeignCurrency() {
		if(this.changeBackCurrency.equals(PosPaymentMethod.CHANGE_BACK_CURRENCY_FOREIGN))
			return true;
		else
			return false;
	}
	
	public BigDecimal currencyRoundUpAmountWithCurrenctDecimalToBigDecimal(BigDecimal dAmount){
		String sRoundMethod = "1"; // round up
		
		return Util.HERORound(dAmount, sRoundMethod, this.currencyDecimal);
	}
	
	public BigDecimal currencyRoundAmountToBigDecimal(BigDecimal dAmount) {
		return Util.HERORound(dAmount, this.currencyRound, this.currencyDecimal);
	}
}
