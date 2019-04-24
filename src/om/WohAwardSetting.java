package om;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WohAwardSetting {
	private int awdsId;
	private int shopId;
	private int oletId;
	private String type;
	private int idepId;
	private int paymId;
	private int dtypId;
	private int txscId;
	private int gratId;
	private String earningEligible;
	private String awardPayable;
	private String status;
	
	private String lastErrorMessage;
	
	private OmWsClient m_oOmWsClient;
	
	// type
	public static String TYPE_ITEM_DEPARTMENT = "d";
	public static String TYPE_PAYMENT_METHOD = "p";
	public static String TYPE_EXTRA_CHANGE = "x";
	public static String TYPE_SERVICE_CHANGE = "s";
	public static String TYPE_GRATUITY = "g";
	
	// earning eligible
	public static String EARNINGELIGIBLE_YES = "y";
	public static String EARNINGELIGIBLE_NO = "";
	
	// award payable
	public static String AWARDPAYABLE_YES = "y";
	public static String AWARDPAYABLE_NO = "";
	
	// status
	public static String STATUS_NORMAL = "";
	public static String STATUS_DELETED = "d";
	
	//init with initialized value
	public WohAwardSetting() {
		this.init();
	}
	
	//init object with JSONObject
	public WohAwardSetting(JSONObject oAwardSettingJSONObject) {
		this.readDataFromJson(oAwardSettingJSONObject);
	}
	
	//read data from API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray awardSettingsJSONArray = null;
		
		if (OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false) == false) {
			return null;
		}else {
			try {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
					return null;
				
				if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("record")) {
					OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
					return null;
				}
				
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("record"))
					return null;
				
				awardSettingsJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().getJSONArray("record");
				
			}catch(JSONException jsone) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.stackToString(jsone));
				return null;
			}
		}
		
		return awardSettingsJSONArray;
	}
	
	public JSONArray readAllByShopOutlet(int iShopId, int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("shopId", iShopId);
			requestJSONObject.put("outletId", iOutletId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		JSONArray responseJSONArray = this.readDataListFromApi("gm", "woh", "getAwardSettingByShopOutletId", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject oAwardSettingJSONObject) {
		JSONObject resultAwardSetting = null;
		resultAwardSetting = oAwardSettingJSONObject.optJSONObject("WohAwardSetting");
		if(resultAwardSetting == null)
			resultAwardSetting = oAwardSettingJSONObject;
		
		this.init();
		
		this.awdsId = resultAwardSetting.optInt("awds_id");
		this.shopId = resultAwardSetting.optInt("awds_shop_id");
		this.oletId = resultAwardSetting.optInt("awds_olet_id");
		this.type = resultAwardSetting.optString("awds_type");
		this.idepId = resultAwardSetting.optInt("awds_idep_id");
		this.paymId = resultAwardSetting.optInt("awds_paym_id");
		this.dtypId = resultAwardSetting.optInt("awds_dtyp_id");
		this.txscId = resultAwardSetting.optInt("awds_txsc_id");
		this.gratId = resultAwardSetting.optInt("awds_grat_id");
		this.earningEligible = resultAwardSetting.optString("awds_earning_eligible");
		this.awardPayable = resultAwardSetting.optString("awds_award_payable");
		this.status = resultAwardSetting.optString("awds_status");
	}
	
	//init value
	public void init() {
		m_oOmWsClient = OmWsClientGlobal.g_oWsClient.get();
		
		this.awdsId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.type = "";
		this.idepId = 0;
		this.paymId = 0;
		this.dtypId = 0;
		this.txscId = 0;
		this.gratId = 0;
		this.earningEligible = "";
		this.awardPayable = "";
		this.status = WohAwardSetting.STATUS_NORMAL;
		this.lastErrorMessage = "";
	}
	
	public void setOmWsClient(OmWsClient oOmWsClient) {
		m_oOmWsClient = oOmWsClient;
	}
	
	//set award setting id
	public void setAwardSettingId(int id) {
		this.awdsId = id;
	}
	
	//set shop id
	public void setStopId(int id) {
		this.shopId = id;
	}
	
	//set outlet id
	public void setOutletId(int id) {
		this.oletId = id;
	}
	
	//set type
	public void setType(String sType) {
		this.type = sType;
	}
	
	//set last name by lang index
	public void setItemDepartmentId(int id) {
		this.idepId = id;
	}
	
	//set payment method id
	public void setPaymentMethodId(int id) {
		this.paymId = id;
	}
	
	//set discount id 
	public void setDiscountId(int id) {
		this.dtypId = id;
	}
	
	//set tax / service charge id
	public void setTaxScId(int id) {
		this.txscId = id;
	}
	
	//set gratuity id
	public void setGratuityId(int id) {
		this.gratId = id;
	}
	
	//set earning eligible
	public void setEarningEligible(String sEarningEligible) {
		this.earningEligible = sEarningEligible;
	}
	
	//set award payable
	public void setAwardPayable(String sAwardPayable) {
		this.awardPayable = sAwardPayable;
	}
	
	//set status
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	public void setLastErrorMessage(String sErrMsg) {
		this.lastErrorMessage = sErrMsg;
	}
	
	//get award setting id
	public int getAwardSettingId() {
		return this.awdsId;
	}
	
	//get shop id
	public int getStopId() {
		return this.shopId;
	}
	
	//get outlet id
	public int getOutletId() {
		return this.oletId;
	}
	
	//get type
	public String getType() {
		return this.type;
	}
	
	//get last name by lang index
	public int getItemDepartmentId() {
		return this.idepId;
	}
	
	//get payment method id
	public int getPaymentMethodId() {
		return this.paymId;
	}
	
	//get discount id 
	public int getDiscountId() {
		return this.dtypId;
	}
	
	//get tax / service charge id
	public int getTaxScId() {
		return this.txscId;
	}
	
	//get gratuity id
	public int getGratuityId() {
		return this.gratId;
	}
	
	//get earning eligible
	public String getEarningEligible() {
		return this.earningEligible;
	}
	
	//get award payable
	public String getAwardPayable() {
		return this.awardPayable;
	}
	
	//get status
	public String getStatus() {
		return this.status;
	}
	
	//get last error message
	public String getLastErrorMessage() {
		return this.lastErrorMessage;
	}
	
	public boolean isNormal() {
		return this.status.equals(WohAwardSetting.STATUS_NORMAL);
	}
	
	public boolean isDeleted() {
		return this.status.equals(WohAwardSetting.STATUS_DELETED);
	}

}
