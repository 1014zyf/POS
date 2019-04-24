package om;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;

public class PosTipsTrackTransactions {
	private String tiptId;
	private String bdayId;
	private int shopId;
	private int oletId;
	private String type;
	private BigDecimal amount;
	private int fromUserId;
	private int toUserId;
	private DateTime transactionTime;
	private DateTime transactionLoctime;
	private int transactionStatId;
	private String status;
	private String operation;
	
	public static String OPERATION_NEW = "n";
	public static String OPERATION_DELETE = "d";
	public static String OPERATION_UPDATE = "";
	
	public static String TYPE_SERVICE_CHARGE = "g";
	public static String TYPE_TIPS = "t";
	public static String TYPE_DIRECT_TIPS = "d";
	
	//init object with initialize value
	public PosTipsTrackTransactions() {
		this.init();
	}
	
	//init obejct with JSONObject
	public PosTipsTrackTransactions(JSONObject posTipsTrackingTransactions) {
		readDataFromJson(posTipsTrackingTransactions);
	}
	
	//reset the object
	private void init() {
		this.tiptId = "";
		this.bdayId = "";
		this.shopId = 0;
		this.oletId = 0;
		this.type = "";
		this.amount = BigDecimal.ZERO;
		this.fromUserId = 0;
		this.toUserId = 0;
		this.transactionTime = null;
		this.transactionLoctime = null;
		this.transactionStatId = 0;
		this.status = "";
		this.operation = null;
	}
	
	//read data from POS API
	private JSONObject readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONObject oTipsTrackTransactionsJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("posTipsTrackTransactions")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("posTipsTrackTransactions")) {
				return null;
			}
			
			oTipsTrackTransactionsJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("posTipsTrackTransactions");
		}
		
		return oTipsTrackTransactionsJSONObject;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject posTipsTrackTransactionsJSONObject) {
		JSONObject resultTipsTrackTransactions = null;
		
		resultTipsTrackTransactions = posTipsTrackTransactionsJSONObject.optJSONObject("PosTipsTrackTransaction");
		if(resultTipsTrackTransactions == null)
			resultTipsTrackTransactions = posTipsTrackTransactionsJSONObject;
		
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		this.init();
		this.tiptId = resultTipsTrackTransactions.optString("tipt_id");
		this.bdayId = resultTipsTrackTransactions.optString("tipt_bday_id");
		this.shopId = resultTipsTrackTransactions.optInt("tipt_shop_id");
		this.oletId = resultTipsTrackTransactions.optInt("tipt_olet_id");
		this.type = resultTipsTrackTransactions.optString("tipt_type");
		this.amount = new BigDecimal(resultTipsTrackTransactions.optString("tipt_amount", "0.0"));
		this.fromUserId = resultTipsTrackTransactions.optInt("tipt_from_user_id");
		this.toUserId = resultTipsTrackTransactions.optInt("tipt_to_user_id");
		
		String sTransactionTime = resultTipsTrackTransactions.optString("tipt_transaction_time");
		if(!sTransactionTime.isEmpty())
			this.transactionTime = oFmt.parseDateTime(sTransactionTime);
		
		String sTransactionLoctime = resultTipsTrackTransactions.optString("tipt_transaction_loctime");
		if(!sTransactionLoctime.isEmpty())
			this.transactionLoctime = oFmt.parseDateTime(sTransactionLoctime);
		
		this.status = resultTipsTrackTransactions.optString("tipt_status");
	}
	
	public JSONObject readByTypeBdayUserShopOlet(String sBusinessDayId, int iUserId, int iShopId, int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONObject responseJSONObject = null;
		
		try {
			requestJSONObject.put("bday_id", sBusinessDayId);
			requestJSONObject.put("user_id", Integer.toString(iUserId));
			requestJSONObject.put("shop_id", Integer.toString(iShopId));
			requestJSONObject.put("olet_id", Integer.toString(iOutletId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONObject = this.readDataListFromApi("gm", "pos", "getTipsTrackTransactionsByBdayUserShopOlet", requestJSONObject.toString());
		
		return responseJSONObject;
	}
	
	public boolean addUpdateWithMultipleTransactions(ArrayList<PosTipsTrackTransactions> oPosMultiTipsTrackTransactions) {
		JSONObject tempTipsTrackTransJSONObject = null, tipsTrackTransJSONObject = new JSONObject();
		JSONArray tipsTrackTransactionsJSONArray = new JSONArray();
		Boolean bResult = false;
		
		for(PosTipsTrackTransactions oPosTipsTrackTransactions : oPosMultiTipsTrackTransactions) {
			tempTipsTrackTransJSONObject = oPosTipsTrackTransactions.constructAddSaveJSON();
			tipsTrackTransactionsJSONArray.put(tempTipsTrackTransJSONObject);
		}
		try {
			tipsTrackTransJSONObject.put("posTipsTrackTransactions", tipsTrackTransactionsJSONArray);
		} catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveMultiTipsTrackTransactions", tipsTrackTransJSONObject.toString(), false))
			return false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("result"))
				return false;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("result"))
				return false;
			
			bResult = OmWsClientGlobal.g_oWsClient.get().getResponse().optBoolean("result");
			return bResult;
		}
	}
	
	//construct the save JSON
	protected JSONObject constructAddSaveJSON() {
		JSONObject addSaveObject = new JSONObject();
		DateTime oCurrentDateTime = AppGlobal.getCurrentTime(false);
		
		try {
			addSaveObject.put("tipt_from_user_id", this.fromUserId);
			addSaveObject.put("tipt_transaction_user_id", this.fromUserId);
			addSaveObject.put("tipt_to_user_id", this.toUserId);
			addSaveObject.put("tipt_type", this.type);
			addSaveObject.put("tipt_shop_id", this.shopId);
			addSaveObject.put("tipt_olet_id", this.oletId);
			addSaveObject.put("tipt_bday_id", this.bdayId);
			addSaveObject.put("tipt_transaction_stat_id", this.transactionStatId);
			addSaveObject.put("tipt_amount", this.amount);
			addSaveObject.put("tipt_transaction_loctime", OmWsClientGlobal.dateTimeToString(oCurrentDateTime));
			addSaveObject.put("tipt_transaction_time", OmWsClientGlobal.dateTimeToString(AppGlobal.convertTimeToUTC(oCurrentDateTime)));
			addSaveObject.put("operation", this.operation);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return addSaveObject;
	}
	
	//get tip track transaction id
	public String getTiptId() {
		return this.tiptId;
	}
	
	//get bday id
	public String getBdayId() {
		return this.bdayId;
	}
	
	//get type
	public String getType() {
		return this.type;
	}
	
	//get amount
	public BigDecimal getAmount() {
		return this.amount;
	}
	
	//get to user id
	public int getToUserId() {
		return this.toUserId;
	}
	
	//get from user id
	public int getFromUserId() {
		return this.fromUserId;
	}
	
	//set type
	public void setType(String sType) {
		//TYPE_SERVICE_CHARGE = "g"
		//TYPE_TIPS = "t"
		//TYPE_DIRECT_TIPS = "d"
		this.type = sType;
	}
	
	//set amount
	public void setAmount(BigDecimal dAmount) {
		this.amount = dAmount;
	}
	
	//set to user id
	public void setToUserId(int iToUserId) {
		this.toUserId = iToUserId;
	}
	
	//set from user id
	public void setFromUserId(int iFromUserId) {
		this.fromUserId = iFromUserId;
	}
	
	//set operation
	public void setOperation(String sOperation) {
		//OPERATION_NEW = "n"
		//OPERATION_DELETE = "d"
		//OPERATION_UPDATE = ""
		this.operation = sOperation;
	}
	
	//set shop id
	public void setShopId(int iShopId) {
		this.shopId = iShopId;
	}
	//set outlet id
	public void setOletId(int iOletId) {
		this.oletId = iOletId;
	}
	//set bday id
	public void setBdayId(String iBdayId) {
		this.bdayId = iBdayId;
	}
	//set transaction station id
	public void setTransactionStatId(int iTransactionStatId) {
		this.transactionStatId = iTransactionStatId;
	}
}

