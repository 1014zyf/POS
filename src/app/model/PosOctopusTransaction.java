//Database: pos_business_periods - Running business periods on business day
package app.model;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

public class PosOctopusTransaction {
	private int octtId;
	private int bdayId;
	private int shopId;
	private int oletId;
	private int chksId;
	private int cpayId;
	private int paym_id;
	private int transactionNum;
	private String transactionType;
	private BigDecimal transactionAmount;
	private String deviceId;
	private String udsn;
	private String cardId;
	private String cardType;
	private BigDecimal cardPreviousAmount;
	private BigDecimal cardCurrentAmount;
	private String transactionTime;
	private DateTime transactionLocTime;
	private int transactionUserId;
	private int transactionStatId;
	private String status;
	
	// Transaction type
	public static String TYPE_ADD_VALUE = "a";
	public static String TYPE_DEDUCT_VALUE = "d";
	
	// status
	private static String STATUS_ACTIVE = "";
	private static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosOctopusTransaction() {
		init();		
	}
	
	//init object with JSON Object
	public PosOctopusTransaction(JSONObject periodsJSONObject) {
		this.readDataFromJson(periodsJSONObject);
	}
	
	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bResult = false;
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bResult = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("octopusTransaction")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("octopusTransaction")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("octopusTransaction");
			if(tempJSONObject.isNull("PosOctopusTransaction")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
			bResult = true;
			
		}
		
		return bResult;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject transactionsJSONObject) {
		JSONObject resultOctopusTransaction = null;
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		resultOctopusTransaction = transactionsJSONObject.optJSONObject("PosOctopusTransaction");
		if(resultOctopusTransaction == null)
			resultOctopusTransaction = transactionsJSONObject;
			
		this.init();
		this.octtId = resultOctopusTransaction.optInt("octt_id");
		this.bdayId = resultOctopusTransaction.optInt("octt_bday_id");
		this.shopId = resultOctopusTransaction.optInt("octt_shop_id");
		this.oletId = resultOctopusTransaction.optInt("octt_olet_id");
		this.chksId = resultOctopusTransaction.optInt("octt_chks_id");
		this.cpayId = resultOctopusTransaction.optInt("octt_cpay_id");
		this.paym_id = resultOctopusTransaction.optInt("octt_paym_id");
		this.transactionType = resultOctopusTransaction.optString("octt_type");
		this.transactionAmount = new BigDecimal(resultOctopusTransaction.optString("octt_amount", "0.0"));
		this.deviceId = resultOctopusTransaction.optString("octt_device_id");
		this.udsn = resultOctopusTransaction.optString("octt_udsn");
		this.cardId = resultOctopusTransaction.optString("octt_card_id");
		this.cardType = resultOctopusTransaction.optString("octt_card_type");
		this.cardPreviousAmount = new BigDecimal(resultOctopusTransaction.optString("octt_card_previous_amount", "0.0"));
		this.cardCurrentAmount = new BigDecimal(resultOctopusTransaction.optString("octt_card_current_amount", "0.0"));
		this.transactionNum = resultOctopusTransaction.optInt("octt_transaction_num");
		String sLocTime = resultOctopusTransaction.optString("octt_transaction_loctime");
		if(!sLocTime.isEmpty())
			this.transactionLocTime = oFmt.parseDateTime(sLocTime);
		else
			this.transactionLocTime = new DateTime();
		this.transactionTime = resultOctopusTransaction.optString("octt_transaction_time", oFmt.print(transactionLocTime.withZone(DateTimeZone.UTC)));
		this.transactionUserId = resultOctopusTransaction.optInt("octt_transaction_user_id");
		this.transactionStatId = resultOctopusTransaction.optInt("octt_transaction_stat_id");
		
		this.status = resultOctopusTransaction.optString("octt_status", PosOctopusTransaction.STATUS_ACTIVE);
	}

	//construct the save request JSON
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("octt_id", this.octtId);
			addSaveJSONObject.put("octt_bday_id", this.bdayId);
			addSaveJSONObject.put("octt_shop_id", this.shopId);
			addSaveJSONObject.put("octt_olet_id", this.oletId);
			addSaveJSONObject.put("octt_chks_id", this.chksId);
			addSaveJSONObject.put("octt_cpay_id", this.cpayId);
			addSaveJSONObject.put("octt_paym_id", this.paym_id);
			addSaveJSONObject.put("octt_type", this.transactionType);
			addSaveJSONObject.put("octt_amount", this.transactionAmount);
			addSaveJSONObject.put("octt_device_id", this.deviceId);
			addSaveJSONObject.put("octt_udsn", this.udsn);
			addSaveJSONObject.put("octt_card_id", this.cardId);
			addSaveJSONObject.put("octt_card_type", this.cardType);
			addSaveJSONObject.put("octt_card_previous_amount", this.cardPreviousAmount);
			addSaveJSONObject.put("octt_card_current_amount", this.cardCurrentAmount);
			addSaveJSONObject.put("octt_transaction_num", this.transactionNum);
			if (this.transactionTime != null)
				addSaveJSONObject.put("octt_transaction_time", this.transactionTime);
			
			if (this.transactionLocTime != null)
				addSaveJSONObject.put("octt_transaction_loctime", this.transactionLocTime.toString("yyyy-MM-dd HH:mm:ss"));
			
			addSaveJSONObject.put("octt_transaction_user_id", this.transactionUserId);
			addSaveJSONObject.put("octt_transaction_stat_id", this.transactionStatId);
			
			addSaveJSONObject.put("octt_status", this.status);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}

	//add or update a check item to database
	public boolean addUpdate(boolean bUpdate) {
		JSONObject requestJSONObject = new JSONObject();
		
		requestJSONObject = this.constructAddSaveJSON(bUpdate);
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveOctopusTransaction", requestJSONObject.toString(), false))
			return false;
		else
			return true;
	}
	
	public boolean printOctopusSlip(boolean bDuplicate, int iOutletId, String sDeviceId, String sUdsn, String sCardId, BigDecimal dTransAmount, BigDecimal dRemainAmount, int iPrintQueueId, int iLangIndex, int iTransactionNum, String sLastAddValueType, String sLastAddValueDate, String sTransactionTime) {
		JSONObject requestJSONObject = new JSONObject();
		boolean bResult = false;
		 
		try {
			if(bDuplicate)
				requestJSONObject.put("duplicate", 1);
			else
				requestJSONObject.put("duplicate", 0);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("deviceId", sDeviceId);
			requestJSONObject.put("udsn", sUdsn);
			requestJSONObject.put("cardId", sCardId);
			requestJSONObject.put("transAmount", dTransAmount);
			requestJSONObject.put("remainAmount", dRemainAmount);
			requestJSONObject.put("printQueueId", iPrintQueueId);
			requestJSONObject.put("langIndex", iLangIndex);
			requestJSONObject.put("transactionNum", iTransactionNum);
			requestJSONObject.put("lastAddValueType", sLastAddValueType);
			requestJSONObject.put("lastAddValueDate", sLastAddValueDate);
			requestJSONObject.put("transactionTime", sTransactionTime);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		bResult = this.readDataFromApi("gm", "pos", "printOctopusSlip", requestJSONObject.toString());
		
		return bResult;
	}
	
	public int getLastTransactionNumberByStation(int iStationId){
		JSONObject requestJSONObject = new JSONObject();
		
		try{
			requestJSONObject.put("stationId", iStationId);
		}catch(Exception e){
			OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.stackToString(e));
		}
			
		if (OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "getLastOctopusTransactionNumber", requestJSONObject.toString(), false)) {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null ||
					!OmWsClientGlobal.g_oWsClient.get().getResponse().has("last_transaction_num"))
				return 0;
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("last_transaction_num");
		}	
		
		return 0;
	}
	
	// init value
	public void init() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		this.octtId = 0;
		this.bdayId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.chksId = 0;
		this.cpayId = 0;
		this.paym_id = 0;
		this.transactionType = "";
		this.transactionAmount = BigDecimal.ZERO;
		this.deviceId = "";
		this.udsn = "";
		this.cardId = "";
		this.cardType = "";
		this.cardPreviousAmount = BigDecimal.ZERO;
		this.cardCurrentAmount = BigDecimal.ZERO;
		this.transactionNum = 0;
		this.transactionLocTime = new DateTime();
		this.transactionTime = fmt.print(transactionLocTime.withZone(DateTimeZone.UTC));
		this.transactionUserId = 0;
		this.transactionStatId = 0;
		this.status = PosOctopusTransaction.STATUS_ACTIVE;
	}
	
	public int getOcttId() {
		return this.octtId;
	}
	
	public int getBdayId() {
		return this.bdayId;
	}
	
	public int getShopId() {
		return this.shopId;
	}
	
	public int getOletId() {
		return this.oletId;
	}
	
	public int getChksId() {
		return this.chksId;
	}
	
	public int getCpayId() {
		return this.cpayId;
	}
	
	public int getPaymId() {
		return this.paym_id;
	}
	
	public String getTransactionType() {
		return this.transactionType;
	}
	
	public BigDecimal getTransactionAmount() {
		return this.transactionAmount;
	}
	
	public String getDeviceId() {
		return this.deviceId;
	}
	
	public String getUdsn() {
		return this.udsn;
	}
	
	public String getCardId() {
		return this.cardId;
	}
	
	public String getCardType() {
		return this.cardType;
	}
	
	public BigDecimal getCardPreviousAmount() {
		return this.cardPreviousAmount;
	}
	
	public BigDecimal getCardCurrentAmount() {
		return this.cardCurrentAmount;
	}
	
	public String getTransactionTime() {
		return this.transactionTime;
	}
	
	public DateTime getTransactionLocTime() {
		return this.transactionLocTime;
	}
		
	public String getStatus() {
		return this.status;
	}
	
	public int getTransactionUserId() {
		return this.transactionUserId;
	}
	
	public int getTransactionStatId() {
		return this.transactionUserId;
	}
	
	public void setOcttId(int iOcttId) {
		this.octtId = iOcttId;
	}
	
	public void setBdayId(int iBdayId) {
		this.bdayId = iBdayId;
	}
	
	public void setShopId(int iShopId) {
		this.shopId = iShopId;
	}
	
	public void setOletId(int iOletId) {
		this.oletId = iOletId;
	}
	
	public void setChksId(int iChksId) {
		this.chksId = iChksId;
	}
	
	public void setCpayId(int iCpayId) {
		this.cpayId = iCpayId;
	}
	
	public void setPaymId(int iPaymId) {
		this.paym_id = iPaymId;
	}
	
	public void setTransactionType(String sTransactionType) {
		this.transactionType = sTransactionType;
	}
	
	public void setTransactionAmount(BigDecimal dTransactionAmount) {
		this.transactionAmount = dTransactionAmount;
	}
	
	public void setDeviceId(String sDeviceId) {
		this.deviceId = sDeviceId;
	}
	
	public void setUdsn(String sUdsn) {
		this.udsn = sUdsn;
	}
	
	public void setCardId(String sCardId) {
		this.cardId = sCardId;
	}
	
	public void setCardType(String sCardType) {
		this.cardType = sCardType;
	}
	
	public void setCardPreviousAmount(BigDecimal dCardPreviousAmount) {
		this.cardPreviousAmount = dCardPreviousAmount;
	}
	
	public void setCardCurrentAmount(BigDecimal dCardCurrentAmount) {
		this.cardCurrentAmount = dCardCurrentAmount;
	}
	
	public void setTransactionNum(int iTransactionNum) {
		this.transactionNum = iTransactionNum;
	}
	
	public void setTransactionTime(String sTransactionTime) {
		this.transactionTime = sTransactionTime;
	}
	
	public void setTransactionLocTime(DateTime oTransactionLocTime) {
		this.transactionLocTime = oTransactionLocTime;
	}
	
	public void setTransactionUserId(int iTransactionUserId) {
		this.transactionUserId = iTransactionUserId;
	}
	
	public void setTransactionStatId(int iTransactionStatId) {
		this.transactionStatId = iTransactionStatId;
	}
	
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
}
