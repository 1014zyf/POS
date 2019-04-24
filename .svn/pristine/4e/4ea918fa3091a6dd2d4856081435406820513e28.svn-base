package app.model;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

public class OutCurrency {
	private int currId;
	private int shopId;
	private int oletId;
	private String[] name;
	private String[] shortName;
	private String sign;
	private String code;
	private BigDecimal rate;
	private String status;
	
	// rounding
	private static String ROUND_ROUND_OFF = "";
	private static String ROUND_ROUND_UP = "1";
	private static String ROUND_ROUND_DOWN = "2";
	private static String ROUND_TO_NEARESET_5 = "3";
	private static String ROUND_UP_TO_NEAREST_5 = "4";
	private static String ROUND_DOWN_TO_NEAREST_5 = "5";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	public static String STATUS_SUSPENDED = "s";
	
	//init object with initialize value
	public OutCurrency () {
		this.init();
	}
	
	// init value
	public void init () {
		int i=0;
		
		this.currId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.sign = "";
		this.code = "";
		this.rate = BigDecimal.ZERO;
		this.status = OutCurrency.STATUS_ACTIVE;
	}
	
	//read data from database by shop id, outlet id and currency code
	public boolean readActiveByShopOutletIdAndCode(int iShopId, int iOutletId, String sCode) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("shopId", iShopId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("code", sCode);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "outlet", "getActiveCurrencyByShopOutletAndCode", requestJSONObject.toString());
	}
	
	//get currency id
	public int getCurrencyId(){
		return this.currId;
	}
	
	//get name
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get name
	public String getSign() {
		return this.sign;
	}
	
	//get code
	public String getCode() {
		return this.code;
	}
	
	//get rate
	public BigDecimal getRate() {
		return this.rate;
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("currency")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("currency")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("currency");
			if(tempJSONObject.isNull("OutCurrency")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject outCurrencyJSONObject) {
		JSONObject resultCurrency = null;
		int i=0;
		
		resultCurrency = outCurrencyJSONObject.optJSONObject("OutCurrency");
		if(resultCurrency == null)
			resultCurrency = outCurrencyJSONObject;
		
		this.init();
		this.currId = resultCurrency.optInt("curr_id");
		this.code = resultCurrency.optString("curr_code");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultCurrency.optString("curr_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultCurrency.optString("curr_short_name_l"+i);
		this.sign = resultCurrency.optString("curr_sign", "");
		this.code = resultCurrency.optString("curr_code", "");
		this.rate = new BigDecimal(resultCurrency.optString("curr_rate", "0.0"));
		this.status = resultCurrency.optString("curr_status", OutCurrency.STATUS_ACTIVE);
	}
}
