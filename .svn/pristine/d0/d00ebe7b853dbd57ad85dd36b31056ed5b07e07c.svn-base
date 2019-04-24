//Database: pos_check_payments - Sales check payment records
package app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosCheckPayment {
	private int cpayId;
	private int bdayId;
	private int bperId;
	private int shopId;
	private int oletId;
	private int chksId;
	private int cptyId;
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
	private String status;
	
	private List<PosCheckExtraInfo> checkExtraInfoList;
	private BigDecimal userInputValue;
	
	// payForeignCurrency
	public static String PAY_FOREIGN_CURRENCY_LOCAL = "";
	public static String PAY_FOREIGN_CURRENCY_FOREIGN = "y";
	
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
		this.status = oPosCheckPayment.status;
		
		//handle extra info
		if(!oPosCheckPayment.getCheckExtraInfoArrayList().isEmpty()) {
			for(PosCheckExtraInfo oExtraInfo:oPosCheckPayment.getCheckExtraInfoArrayList())
				this.checkExtraInfoList.add(new PosCheckExtraInfo(oExtraInfo));
		}
		
		userInputValue = BigDecimal.ZERO;
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
		
		this.cpayId = resultCheckPayment.optInt("cpay_id");
		this.bdayId = resultCheckPayment.optInt("cpay_bday_id");
		this.bperId = resultCheckPayment.optInt("cpay_bper_id");
		this.shopId = resultCheckPayment.optInt("cpay_shop_id");
		this.oletId = resultCheckPayment.optInt("cpay_olet_id");
		this.chksId = resultCheckPayment.optInt("cpay_chks_id");
		this.cptyId = resultCheckPayment.optInt("cpay_cpty_id");
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
		this.status = resultCheckPayment.optString("cpay_status", PosCheckPayment.STATUS_ACTIVE);
		
		//check whether pos check extra info record exist
		tempJSONArray = resultCheckPayment.optJSONArray("PosCheckExtraInfo");
		if(tempJSONArray != null) {
			PosCheckExtraInfoList oCheckExtraInfoList = new PosCheckExtraInfoList(tempJSONArray);
			this.checkExtraInfoList = oCheckExtraInfoList.getCheckExtraInfoList();
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
			if (!this.payForeignCurrency.isEmpty())
				addSaveJSONObject.put("cpay_pay_foreign_currency", this.payForeignCurrency);
			if(this.payForeignTotal.compareTo(BigDecimal.ZERO) != 0)
				addSaveJSONObject.put("cpay_pay_foreign_total", this.payForeignTotal);
			if(this.payForeignTips.compareTo(BigDecimal.ZERO) != 0)
				addSaveJSONObject.put("cpay_pay_foreign_tips", this.payForeignTips);
			if(this.payForeignChange.compareTo(BigDecimal.ZERO) != 0)
				addSaveJSONObject.put("cpay_pay_foreign_change", this.payForeignChange);
			addSaveJSONObject.put("cpay_currency_code", this.currencyCode);
			if(this.currencyRate.compareTo(BigDecimal.ZERO) != 0)
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
			addSaveJSONObject.putOnce("cpay_status", this.status);

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
					addSaveJSONObject.put("checkExtraInfos", chkExtraInfoJSONArray);
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
	public int getCpayId() {
		return this.cpayId;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.cpayId = 0;
		this.bdayId = 0;
		this.bperId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.chksId = 0;
		this.cptyId = 0;
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
		this.status = PosCheckPayment.STATUS_ACTIVE;
		
		if(this.checkExtraInfoList == null)
			this.checkExtraInfoList = new ArrayList<PosCheckExtraInfo>();
		else
			this.checkExtraInfoList.clear();
	}
	
	//read data from database by cpay_id
	public boolean readById (int iCpayId) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("checkPaymentId", Integer.toString(iCpayId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "getCheckPaymentById", requestJSONObject.toString());
	}
	
	//read data from database by cpay_olet_id, a list of cpay_bday_id
	public JSONArray readAllByOletIdTypeEmpAndBdayIds(int iOutletId, String sPaymentType, int iEmployeeId, ArrayList<Integer> oBusinessDayIdList) {
		JSONObject requestJSONObject = new JSONObject(), oTempJSONObject = null;
		JSONArray oBDayIdJSONArray = new JSONArray(), oCheckPaymentsJSONArray;
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("paymentType", sPaymentType);
			requestJSONObject.put("employeeId", iEmployeeId);
			for(Integer iBdayId:oBusinessDayIdList) {
				oTempJSONObject = new JSONObject();
				oTempJSONObject.put("businessDayId", iBdayId);
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
			if(oPosCheckPayment.getCpayId() == 0)
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
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveMultiCheckPayments", checkPaymentJSONObject.toString(), false))
			return false;
		else
			return true;
	}
	
	//set bday id
	public void setBdayId(int iBdayId) {
		this.bdayId = iBdayId;
	}
	
	//set bper id
	public void setBperId(int iBperId) {
		this.bperId = iBperId;
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
	public void setChksId(int iChksId) {
		this.chksId = iChksId;
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
	
	//set status
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	//add extra info to list
	public void addExtraInfoToList(PosCheckExtraInfo oCheckExtraInfo) {
		this.checkExtraInfoList.add(oCheckExtraInfo);
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
	public int getBusinessDayId() {
		return this.bdayId;
	}
	
	//get business period id
	public int getBusinessPeriodId() {
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
	public int getCheckId() {
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
	
	//check payment type equal to "octopus"
	public boolean isOctopusPaymentType() {
		if(this.paymentType.equals(PosCheckPayment.PAYMENT_TYPE_OCTOPUS))
			return true;
		else
			return false;
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
}
