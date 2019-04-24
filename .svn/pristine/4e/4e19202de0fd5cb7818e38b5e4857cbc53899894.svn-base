//Database: pos_payment_gateway_transactions - Payment gateway transactions
package om;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosPaymentGatewayTransactions {
	// General Information
	private String pgtxId;
	private String bdayId;
	private int shopId;
	private int oletId;
	private String chksId;
	private String cpayId;
	private int intfId;
	private String type;
	// Transaction Information
	private String key;
	private String voidKey;
	private String parentAuthCode;
	private String authCode;
	private BigDecimal amount;
	private BigDecimal totalAmount;
	private BigDecimal tips;
	private BigDecimal surcharge;	//for internal used only
	private BigDecimal txnAmount;	// return for no adjustment from 3rd party device
	private String maskedPan;
	private String issuer;
	private String traceNo;
	private String invoiceNo;
	private String refNo;
	// Acquirer information
	private String acquirerMerchantId;
	private String acquirerTerminalId;
	private String acquirerName;
	private String acquirerData;
	private DateTime acquirerTransDate;
	// Other information
	private String currencyCode;
	private String emv;
	private String emvData;
	private String entryMode;
	private String icCardSeq;
	private String intlIcCardTraceNo;
	private String eCashBalance;
	private String termainalSeq;
	private String signFree;
	private String signFreeData;
	private String token;
	private String eSignature;
	private String merchantCopy;
	private String customerCopy;
	// Event
	private String actionTime;
	private DateTime actionLocTime;
	private int actionUserId;
	private String voidTime;
	private DateTime voidLocTime;
	private int voidUserId;
	// Status
	private String status;
	
	// type 
	public static String TYPE_AUTH = "credit_card_auth";
	public static String TYPE_TOPUP_AUTH = "credit_card_topup_auth";
	public static String TYPE_COMPLETE_AUTH = "credit_card_complete_auth";
	public static String TYPE_SALE = "credit_card_sale";
	public static String TYPE_ADJUST_TIP = "credit_card_adjust_tip";
	
	// EMV
	public static String EMV_NONE_EMV = "";
	public static String EMV_EMV = "e";
	
	// entry mode
	public static String ENTRY_MODE_UNDEFINED = "";
	public static String ENTRY_MODE_SWIPE_CARD = "s";
	public static String ENTRY_MODE_MANUAL_INPUIT = "m";
	public static String ENTRY_MODE_INSERT = "c";
	
	// sign free
	public static String SIGN_NO_SIGN_REQUIRED = "";
	public static String SIGN_SIGN_REQUIRED = "s";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_AUTHORIZE_COMPLETE = "c";
	public static String STATUS_VOIDED = "d";
	
	// payment index for payment gateway transaction
	private int iPgtxPayId;
	private boolean bDefaultPayAdded;

	//init object with initialize value
	public PosPaymentGatewayTransactions () {
		this.init();
	}

	//init object with JSON object
	public PosPaymentGatewayTransactions(JSONObject payGatewayTransJSONObject) {
		this.readDataFromJson(payGatewayTransJSONObject);
	}

	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray paymentGatewayTransJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("paymentGatewayTrans")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("paymentGatewayTrans"))
				return null;
			
			paymentGatewayTransJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("paymentAcls");
		}
		
		return paymentGatewayTransJSONArray;
	}
	
	//read data from database by check id, outlet id, shop id and business day id
	public JSONArray readAllByCheckId(String sCheckId, int iOutletId, int iShopId, String sBusinessDayId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray oPaymentGatewayTransactionsJSONArray;
		try {
			requestJSONObject.put("checkId", sCheckId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("shopId", iShopId);
			requestJSONObject.put("businessDayId", sBusinessDayId);
			
			
			oPaymentGatewayTransactionsJSONArray = this.readDataListFromApi("gm", "pos", "getPaymentGatewayTransactionsByCheckId", requestJSONObject.toString());
		}catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		
		return oPaymentGatewayTransactionsJSONArray;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject payGatewayTransJSONObject) {
		JSONObject resultPaymentGatewayTrans = null;
		
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		resultPaymentGatewayTrans = payGatewayTransJSONObject.optJSONObject("PosPaymentGatewayTrans");
		if(resultPaymentGatewayTrans == null)
			resultPaymentGatewayTrans = payGatewayTransJSONObject;
		
		this.init();
		this.pgtxId = resultPaymentGatewayTrans.optString("pgtx_id");
		this.bdayId = resultPaymentGatewayTrans.optString("pgtx_bday_id");
		this.shopId = resultPaymentGatewayTrans.optInt("pgtx_shop_id");
		this.oletId = resultPaymentGatewayTrans.optInt("pgtx_olet_id");
		this.chksId = resultPaymentGatewayTrans.optString("pgtx_chks_id");
		this.cpayId = resultPaymentGatewayTrans.optString("pgtx_cpay_id");
		this.intfId = resultPaymentGatewayTrans.optInt("pgtx_intf_id");
		this.type = resultPaymentGatewayTrans.optString("pgtx_type_key", PosPaymentGatewayTransactions.TYPE_AUTH);
		
		this.key = resultPaymentGatewayTrans.optString("pgtx_key");
		this.voidKey = resultPaymentGatewayTrans.optString("pgtx_void_key");
		this.parentAuthCode = resultPaymentGatewayTrans.optString("pgtx_parent_auth_code");
		this.authCode = resultPaymentGatewayTrans.optString("pgtx_auth_code");
		this.amount = new BigDecimal(resultPaymentGatewayTrans.optString("pgtx_amount", "0.0"));
		this.tips = new BigDecimal(resultPaymentGatewayTrans.optString("pgtx_tips", "0.0"));
		this.maskedPan = resultPaymentGatewayTrans.optString("pgtx_masked_pan");
		this.issuer = resultPaymentGatewayTrans.optString("pgtx_issuer");
		this.traceNo = resultPaymentGatewayTrans.optString("pgtx_trace_num");
		this.invoiceNo = resultPaymentGatewayTrans.optString("pgtx_invoice_num");
		this.refNo = resultPaymentGatewayTrans.optString("pgtx_ref_num");
		
		if(resultPaymentGatewayTrans.has("pgtx_acquirer_info")){
			JSONObject resultAcquirer = null;
			try {
				resultAcquirer = new JSONObject(resultPaymentGatewayTrans.optString("pgtx_acquirer_info"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(resultAcquirer != null){
				this.acquirerMerchantId = resultAcquirer.optString("merchant_id");
				this.acquirerTerminalId = resultAcquirer.optString("terminal");
				this.acquirerName = resultAcquirer.optString("name");
				this.acquirerData = resultAcquirer.optString("data");
				String sAcquirerTransDate = resultAcquirer.optString("datetime");
				if(!sAcquirerTransDate.isEmpty())
					this.acquirerTransDate = oFmt.parseDateTime(sAcquirerTransDate);
			}
		}
		
		this.entryMode = resultPaymentGatewayTrans.optString("pgtx_entry_mode", PosPaymentGatewayTransactions.ENTRY_MODE_UNDEFINED);
		if(resultPaymentGatewayTrans.has("pgtx_other_info")){
			JSONObject resultOtherInfo = null;
			try {
				resultOtherInfo = new JSONObject(resultPaymentGatewayTrans.optString("pgtx_other_info"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(resultOtherInfo != null){
				this.currencyCode = resultOtherInfo.optString("currency_code");
				this.emv = resultOtherInfo.optString("emv", PosPaymentGatewayTransactions.EMV_NONE_EMV);
				this.emvData = resultOtherInfo.optString("emv_data");
				this.icCardSeq = resultOtherInfo.optString("ic_card_seq");
				this.intlIcCardTraceNo = resultOtherInfo.optString("intl_card_trace_num");
				this.eCashBalance = resultOtherInfo.optString("ecash_balance");
				this.termainalSeq = resultOtherInfo.optString("terminal_seq");
				this.signFree = resultOtherInfo.optString("sign_free", PosPaymentGatewayTransactions.SIGN_NO_SIGN_REQUIRED);
				this.signFreeData = resultOtherInfo.optString("sign_free_data");
				this.token = resultOtherInfo.optString("token");
			}
		}
		this.eSignature = resultPaymentGatewayTrans.optString("pgtx_e_signature");
		this.merchantCopy = resultPaymentGatewayTrans.optString("pgtx_merchant_copy");
		this.customerCopy = resultPaymentGatewayTrans.optString("pgtx_customer_copy");
		this.actionTime = resultPaymentGatewayTrans.optString("pgtx_action_time");
		
		String sActionLocTime = resultPaymentGatewayTrans.optString("pgtx_action_loctime");
		if(!sActionLocTime.isEmpty())
			this.actionLocTime = oFmt.parseDateTime(sActionLocTime);
		
		this.actionUserId = resultPaymentGatewayTrans.optInt("pgtx_action_user_id");
		this.voidTime = resultPaymentGatewayTrans.optString("pgtx_void_time");
		
		String sVoidLocTime = resultPaymentGatewayTrans.optString("pgtx_void_loctime");
		if(!sVoidLocTime.isEmpty())
			this.voidLocTime = oFmt.parseDateTime(sVoidLocTime);
		
		this.voidUserId = resultPaymentGatewayTrans.optInt("pgtx_void_user_id");
		
		this.status = resultPaymentGatewayTrans.optString("pgtx_status");
		
	}

	// init value
	public void init () {
		this.pgtxId = "";
		this.bdayId = "";
		this.shopId = 0;
		this.oletId = 0;
		this.chksId = "";
		this.cpayId = "";
		this.intfId = 0;
		this.type = PosPaymentGatewayTransactions.TYPE_AUTH;
		
		this.key = "";
		this.voidKey = "";
		this.parentAuthCode = "";
		this.authCode = "";
		this.amount = BigDecimal.ZERO;
		this.totalAmount = BigDecimal.ZERO;
		this.tips = BigDecimal.ZERO;
		this.surcharge = BigDecimal.ZERO;
		this.txnAmount = BigDecimal.ZERO;
		this.maskedPan = "";
		this.issuer = "";
		this.traceNo = "";
		this.invoiceNo = "";
		this.refNo = "";
		
		this.acquirerMerchantId = "";
		this.acquirerTerminalId = "";
		this.acquirerName = "";
		this.acquirerData = "";
		this.acquirerTransDate = null;
		this.currencyCode = "";
		this.emv = PosPaymentGatewayTransactions.EMV_NONE_EMV;
		this.emvData = "";
		this.entryMode = PosPaymentGatewayTransactions.ENTRY_MODE_UNDEFINED;
		this.icCardSeq = "";
		this.intlIcCardTraceNo = "";
		this.eCashBalance = "";
		this.termainalSeq = "";
		this.signFree = PosPaymentGatewayTransactions.SIGN_NO_SIGN_REQUIRED;
		this.signFreeData = "";
		this.token = "";
		this.eSignature = "";
		this.merchantCopy = "";
		this.customerCopy = "";
		
		this.actionTime = null;
		this.actionLocTime = null;
		this.actionUserId = 0;
		this.voidTime = null;
		this.voidLocTime = null;
		this.voidUserId = 0;
		
		this.status = PosPaymentGatewayTransactions.STATUS_ACTIVE;

		this.iPgtxPayId = 0;
		this.bDefaultPayAdded = false;
	}
	
	//construct the save request
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("pgtx_id", this.pgtxId);
			addSaveJSONObject.put("pgtx_bday_id", this.bdayId);
			addSaveJSONObject.put("pgtx_shop_id", this.shopId);
			addSaveJSONObject.put("pgtx_olet_id", this.oletId);
			if(this.chksId != null && !this.chksId.isEmpty())
				addSaveJSONObject.put("pgtx_chks_id", this.chksId);
			else
				addSaveJSONObject.put("pgtx_chks_id", "");
			addSaveJSONObject.put("pgtx_cpay_id", this.cpayId);
			addSaveJSONObject.put("pgtx_intf_id", this.intfId);
			
			addSaveJSONObject.put("pgtx_type_key", this.type);
			addSaveJSONObject.put("pgtx_void_key", this.voidKey);
			addSaveJSONObject.put("pgtx_parent_auth_code", this.parentAuthCode);
			addSaveJSONObject.put("pgtx_auth_code", this.authCode);
			addSaveJSONObject.put("pgtx_amount", this.amount);
			addSaveJSONObject.put("pgtx_tips", this.tips);
			addSaveJSONObject.put("pgtx_masked_pan", this.maskedPan);
			addSaveJSONObject.put("pgtx_issuer", this.issuer);
			addSaveJSONObject.put("pgtx_trace_num", this.traceNo);
			addSaveJSONObject.put("pgtx_invoice_num", this.invoiceNo);
			addSaveJSONObject.put("pgtx_ref_num", this.refNo);
			
			JSONObject addSaveAcquirerJSONObject = new JSONObject();
			addSaveAcquirerJSONObject.put("merchant_id", this.acquirerMerchantId);
			addSaveAcquirerJSONObject.put("terminal", this.acquirerTerminalId);
			addSaveAcquirerJSONObject.put("name", this.acquirerName);
			addSaveAcquirerJSONObject.put("data", this.acquirerData);
			addSaveAcquirerJSONObject.put("datetime", oFmt.print(this.acquirerTransDate));
			addSaveJSONObject.put("pgtx_acquirer_info", addSaveAcquirerJSONObject);
			
			JSONObject addSaveOtherInfoJSONObject = new JSONObject();
			addSaveOtherInfoJSONObject.put("currency_code", this.currencyCode);
			addSaveOtherInfoJSONObject.put("emv", this.emv);
			addSaveOtherInfoJSONObject.put("emv_data", this.emvData);
			addSaveOtherInfoJSONObject.put("ic_card_seq", this.icCardSeq);
			addSaveOtherInfoJSONObject.put("intl_card_trace_num", this.intlIcCardTraceNo);
			addSaveOtherInfoJSONObject.put("ecash_balance", this.eCashBalance);
			addSaveOtherInfoJSONObject.put("terminal_seq", this.termainalSeq);
			addSaveOtherInfoJSONObject.put("sign_free", this.signFree);
			addSaveOtherInfoJSONObject.put("sign_free_data", this.signFreeData);
			addSaveOtherInfoJSONObject.put("token", this.token);
			addSaveJSONObject.put("pgtx_other_info", addSaveOtherInfoJSONObject);
			
			addSaveJSONObject.put("pgtx_entry_mode", this.entryMode);
			addSaveJSONObject.put("pgtx_e_signature", this.eSignature);
			addSaveJSONObject.put("pgtx_merchant_copy", this.merchantCopy);
			addSaveJSONObject.put("pgtx_customer_copy", this.customerCopy);
			
			if(this.actionTime != null)
				addSaveJSONObject.put("pgtx_action_time", this.actionTime);
			if(this.actionLocTime != null)
				addSaveJSONObject.put("pgtx_action_loctime", dateTimeToString(this.actionLocTime));
			addSaveJSONObject.put("pgtx_action_user_id", this.actionUserId);
			
			if(this.voidTime != null)
				addSaveJSONObject.put("pgtx_void_time", this.voidTime);
			if(this.voidLocTime != null)
				addSaveJSONObject.put("pgtx_void_loctime", dateTimeToString(this.voidLocTime));
			addSaveJSONObject.put("pgtx_void_user_id", this.voidUserId);
			
			addSaveJSONObject.put("pgtx_status", this.status);
			
		}catch (JSONException e) {
			e.printStackTrace();
		}
		return addSaveJSONObject;
	}
	
	protected JSONArray constructMultipleItemAddSaveJSON(List<PosPaymentGatewayTransactions> oPosPaymentGatewayTransactions) {
		JSONArray posPaymentGatewayTransactionJSONArray = new JSONArray();
		try {
			for (int i = 0; i < oPosPaymentGatewayTransactions.size(); i++) {
				PosPaymentGatewayTransactions oPaymentGatewayTransaction = oPosPaymentGatewayTransactions.get(i);
				JSONObject tempPosPaymentGatewayTransactionJSONObject = oPaymentGatewayTransaction.constructAddSaveJSON(true);
				
				JSONObject oOverrideConditionJSONObject = new JSONObject();
				oOverrideConditionJSONObject.put("PosPaymentGatewayTransaction", tempPosPaymentGatewayTransactionJSONObject);
				posPaymentGatewayTransactionJSONArray.put(oOverrideConditionJSONObject);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return posPaymentGatewayTransactionJSONArray;
	}
	
	public String addUpdate(List<PosPaymentGatewayTransactions> oPosPaymentGatewayTransactions) {
		JSONObject addSaveJSONObject = new JSONObject();
		JSONArray addSaveJSONArray = constructMultipleItemAddSaveJSON(oPosPaymentGatewayTransactions);
		
		try {
			addSaveJSONObject.put("PosPaymentGatewayTransaction", addSaveJSONArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//send data to server
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveMultiplePaymentGatewayTransactions", addSaveJSONObject.toString(), false))
			return OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
		else
			return "";
	}
	
	// General information get function
	//get pgtx id
	public String getPgtxId() {
		return this.pgtxId;
	}
	
	//get bday id
	public String getBdayId() {
		return this.bdayId;
	}
	
	//get shop id
	public int getShopId() {
		return this.shopId;
	}
	
	//get olet id
	public int getOletId() {
		return this.oletId;
	}
	
	//get chks id
	public String getChksId() {
		return this.chksId;
	}
	
	//get cpay id
	public String getCpayId() {
		return this.cpayId;
	}
	
	//get intf id
	public int getIntfId() {
		return this.intfId;
	}
	
	//get type
	public String getType() {
		return this.type;
	}
	
	// Transactions information get function
	//get key
	public String getKey() {
		return this.key;
	}
	
	//get void key
	public String getVoidKey() {
		return this.voidKey;
	}
	
	//get parent authorization code
	public String getParentAuthCode() {
		return this.parentAuthCode;
	}
	
	//get authorization code
	public String getAuthCode() {
		return this.authCode;
	}
	
	//get amount
	public BigDecimal getAmount() {
		return this.amount;
	}
	
	//get total amount
	public BigDecimal getTotalAmount(){
		return this.totalAmount;
	}
	

	//get tips
	public BigDecimal getTips() {
		return this.tips;
	}
	
	//get surcharge
	public BigDecimal getSurcharge() {
		return this.surcharge;
	}
	
	//get txnAmount
	public BigDecimal getTxnAmount() {
		return this.txnAmount;
	}
	
	//get masked PAN
	public String getMaskedPan() {
		return this.maskedPan;
	}
	
	//get issuer code
	public String getIssuer() {
		return this.issuer;
	}
	
	//get trace number
	public String getTraceNo() {
		return this.traceNo;
	}
	
	//get invoice number
	public String getInvoiceNo() {
		return this.invoiceNo;
	}
	
	//get reference number
	public String getRefNo() {
		return this.refNo;
	}
	
	//Acquirer information
	//get acquirer merchant ID
	public String getAcquirerMerchant() {
		return this.acquirerMerchantId;
	}
	
	//get acquirer termainal Id
	public String getAcquirerTerminal() {
		return this.acquirerTerminalId;
	}
	
	//get acquirer name
	public String getAcquirerName() {
		return this.acquirerName;
	}
	
	//get acquirer print data ro be printed
	public String getAcquirerData() {
		return this.acquirerData;
	}
	
	//get acquirer transcation date time
	public DateTime getAcquirerDatetime() {
		return this.acquirerTransDate;
	}

	//Other information
	//get currency code
	public String getCurrencyCode() {
		return this.currencyCode;
	}
	
	//get EMV indicator
	public String getEmv() {
		return this.emv;
	}
	
	//get EMV data to be printed
	public String getEmvData() {
		return this.emvData;
	}
	
	//get entry mode
	public String getEntryMode() {
		return this.entryMode;
	}
	
	//get IC card sequence number
	public String getIcCardSeq() {
		return this.icCardSeq;
	}
	
	//get international card trace number
	public String getIntlCardTraceNo() {
		return this.intlIcCardTraceNo;
	}
	
	//get E-cash balance
	public String getECashBalance() {
		return this.eCashBalance;
	}
	
	//get terminal sequence number
	public String getTerminalSeq() {
		return this.termainalSeq;
	}
	
	//get sign free indicator
	public String getSignFree() {
		return this.signFree;
	}
	
	//get sign free data to be printed
	public String getSignFreeData() {
		return this.signFreeData;
	}
	
	//get token
	public String getToken(){
		return this.token;
	}
	
	//get E-signature
	public String getESignature() {
		return this.eSignature;
	}
	
	//Event
	//get UTC time of the action
	public String getActionTime() {
		return this.actionTime;
	}
	
	//get local time of the action
	public DateTime getActionLocTime() {
		return this.actionLocTime;
	}
	
	//get action user id
	public int getActionUserId() {
		return this.actionUserId;
	}
	
	//get UTC time of the void
	public String getVoidTime() {
		return this.voidTime;
	}
	
	//get local time of the void
	public DateTime getVoidLocTime() {
		return this.voidLocTime;
	}
	
	//get void user id
	public int getVoiduserId() {
		return this.voidUserId;
	}
	
	//get status
	public String getStatus(){
		return this.status;
	}
	
	// get iPgtxPayId
	public int getPgtxPayId() {
		return this.iPgtxPayId;
	}
	
	// get bDefaultPayAdded
	public boolean getDefaultPayAdded() {
		return this.bDefaultPayAdded;
	}
	
	//setter
	public void setPgtxId(String pgtxId) {
		this.pgtxId = pgtxId;
	}
	
	public void setBdayId(String bdayId) {
		this.bdayId = bdayId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public void setOletId(int oletId) {
		this.oletId = oletId;
	}

	public void setChksId(String chksId) {
		this.chksId = chksId;
	}

	public void setCpayId(String cpayId) {
		this.cpayId = cpayId;
	}

	public void setIntfId(int intfId) {
		this.intfId = intfId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setVoidKey(String voidKey) {
		this.voidKey = voidKey;
	}
	
	public void setParentAuthCode(String parentAuthCode) {
		this.parentAuthCode = parentAuthCode;
	}
	
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setTips(BigDecimal tips) {
		this.tips = tips;
	}
	
	public void setSurchargeAmount(BigDecimal surcharge) {
		this.surcharge = surcharge;
	}
	
	public void setTxnAmount(BigDecimal txnAmount) {
		this.txnAmount = txnAmount;
	}
	
	public void setMaskedPan(String maskedPan) {
		this.maskedPan = maskedPan;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public void setAcquirerMerchantId(String acquirerMerchantId) {
		this.acquirerMerchantId = acquirerMerchantId;
	}

	public void setAcquirerTerminalId(String acquirerTerminalId) {
		this.acquirerTerminalId = acquirerTerminalId;
	}

	public void setAcquirerName(String acquirerName) {
		this.acquirerName = acquirerName;
	}

	public void setAcquirerData(String acquirerData) {
		this.acquirerData = acquirerData;
	}

	public void setAcquirerTransDate(DateTime acquirerTransDate) {
		this.acquirerTransDate = acquirerTransDate;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public void setEmv(String emv) {
		this.emv = emv;
	}

	public void setEmvData(String emvData) {
		this.emvData = emvData;
	}

	public void setEntryMode(String entryMode) {
		this.entryMode = entryMode;
	}

	public void setIcCardSeq(String icCardSeq) {
		this.icCardSeq = icCardSeq;
	}

	public void setIntlIcCardTraceNo(String intlIcCardTraceNo) {
		this.intlIcCardTraceNo = intlIcCardTraceNo;
	}

	public void seteCashBalance(String eCashBalance) {
		this.eCashBalance = eCashBalance;
	}

	public void setTermainalSeq(String termainalSeq) {
		this.termainalSeq = termainalSeq;
	}

	public void setSignFree(String signFree) {
		this.signFree = signFree;
	}

	public void setSignFreeData(String signFreeData) {
		this.signFreeData = signFreeData;
	}
	
	public void setToken(String tokenString) {
		this.token = tokenString;
	}

	public void seteSignature(String eSignature) {
		this.eSignature = eSignature;
	}
	
	public void setMerchantCopy(String merchantCopy) {
		this.merchantCopy = merchantCopy;
	}
	
	public void setCustomerCopy(String customerCopy) {
		this.customerCopy = customerCopy;
	}
	
	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}

	public void setActionLocTime(DateTime actionLocTime) {
		this.actionLocTime = actionLocTime;
	}

	public void setActionUserId(int actionUserId) {
		this.actionUserId = actionUserId;
	}
	
	public void setVoidTime(String voidTime) {
		this.voidTime = voidTime;
	}

	public void setVoidLocTime(DateTime voidLocTime) {
		this.voidLocTime = voidLocTime;
	}

	public void setVoidUserId(int voidUserId) {
		this.voidUserId = voidUserId;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setPgtxPayId(int iPgtxPayId){
		this.iPgtxPayId = iPgtxPayId;
	}
	
	public void setDefaultPayAdded(boolean bDefaultPayAdded) {
		this.bDefaultPayAdded = bDefaultPayAdded;
	}
	
	private String dateTimeToString (DateTime oDateTime) {
		if (oDateTime == null)
			return "";
		
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		String timeString = fmt.print(oDateTime);

		return timeString;
	}
}
