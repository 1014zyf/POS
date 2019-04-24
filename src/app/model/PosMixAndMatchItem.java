package app.model;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosMixAndMatchItem {
	private int mamiId;
	private int mamrId;
	private String type;
	private int recordId;
	private int index;
	private String method;
	private BigDecimal fixAmount;
	private Integer priceLevel;
	private BigDecimal rate;
	
	// Record ID type
	private static String TYPE_MENU_ITEM = "i";
	private static String TYPE_DEPARTMENT = "d";
	private static String TYPE_CATEGORY = "c";
	
	// Promotion method
	private static String METHOD_FIX_PRICE = "f";
	private static String METHOD_ADD_PRICE = "a";
	private static String METHOD_PRICE_LEVEL = "p";
	private static String METHOD_RATE = "d";
	
	//init object with initialize value
	public PosMixAndMatchItem() {
		this.init();
	}
	
	public PosMixAndMatchItem(JSONObject oJSONObject) {
		readDataFromJson(oJSONObject);
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject mixAndMatchItemJSONObject) {
		JSONObject tempJSONObject = null;
		
		tempJSONObject = mixAndMatchItemJSONObject.optJSONObject("PosMixAndMatchItem");
		if(tempJSONObject == null)
			tempJSONObject = mixAndMatchItemJSONObject;
			
		this.init();
		this.mamiId = tempJSONObject.optInt("mami_id");
		this.mamrId = tempJSONObject.optInt("mami_mamr_id");
		this.type = tempJSONObject.optString("mami_type");
		this.recordId = tempJSONObject.optInt("mami_record_id");
		this.index = tempJSONObject.optInt("mami_index");
		this.method = tempJSONObject.optString("mami_method");
		
		String sFixAmount = tempJSONObject.optString("mami_fix_amount");
		if(!sFixAmount.isEmpty())
			this.fixAmount = new BigDecimal(sFixAmount);
		
		if(!tempJSONObject.isNull("mami_price_level"))
			this.priceLevel = tempJSONObject.optInt("mami_price_level");
		
		String sRate = tempJSONObject.optString("mami_rate");
		if(!sRate.isEmpty())
			this.rate = new BigDecimal(sRate);
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("PosMixAndMatchItem")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("PosMixAndMatchItem")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("PosMixAndMatchItem");
			readDataFromJson(tempJSONObject);		
		}
		
		return bResult;
	}
	
	//read a list of data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray oJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("mixAndMatchItems")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("mixAndMatchItems"))
				return null;
			
			oJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("mixAndMatchItems");
		}
		
		return oJSONArray;
	}
	
	// init value
	public void init() {
		this.mamiId = 0;
		this.mamrId = 0;
		this.type = "";
		this.recordId = 0;
		this.index = 0;
		this.method = "";
		this.fixAmount = null;
		this.priceLevel = null;
		this.rate = null;
	}
	
	//read data from database 
	public JSONArray readAllValidItems(JSONArray oRuleIdList, JSONArray oItemIdList, JSONArray oDeptIdList, JSONArray oCatIdList) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		 
		try {
			requestJSONObject.put("ruleIdList", oRuleIdList);
			requestJSONObject.put("itemIdList", oItemIdList);
			requestJSONObject.put("departmentIdList", oDeptIdList);
			requestJSONObject.put("categoryIdList", oCatIdList);			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllValidMixAndMatchRuleItems", requestJSONObject.toString());

		return responseJSONArray;
	}
	
	public boolean isMenuItem(){
		if(this.type.equals(TYPE_MENU_ITEM))
			return true;
		else
			return false;
	}
	
	public boolean isDepartment(){
		if(this.type.equals(TYPE_DEPARTMENT))
			return true;
		else
			return false;
	}
	
	public boolean isCategory(){
		if(this.type.equals(TYPE_CATEGORY))
			return true;
		else
			return false;
	}
	
	public boolean isChangeFixPrice(){
		if(this.method.equals(METHOD_FIX_PRICE))
			return true;
		else
			return false;
	}
	
	public boolean isAddPrice(){
		if(this.method.equals(METHOD_ADD_PRICE))
			return true;
		else
			return false;
	}
	
	public boolean isChangePriceLevel(){
		if(this.method.equals(METHOD_PRICE_LEVEL))
			return true;
		else
			return false;
	}
	
	public boolean isChangeByRate(){
		if(this.method.equals(METHOD_RATE))
			return true;
		else
			return false;
	}
	
	public int getItemId(){
		return this.mamiId;
	}
	
	public int getRuleId(){
		return this.mamrId;
	}
	
	public int getRecordId(){
		return this.recordId;
	}
	
	public int getIndex(){
		return this.index;
	}
	
	public BigDecimal getFixAmount(){
		return this.fixAmount;
	}
	
	public Integer getPriceLevel(){
		return this.priceLevel;
	}
	
	public BigDecimal getRate(){
		return this.rate;
	}
}
