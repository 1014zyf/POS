package om;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;

public class WohBonusCode {
	// General Information
	private int boncId;
	private int shopId;
	private int oletId;
	private String bonusCode;
	private DateTime startDate;
	private DateTime endDate;
	
	// Status
	private String status;
	
	//init object with initialize value
	public WohBonusCode() {
		this.init();
	}
	
	//init object with JSON object
	public WohBonusCode(JSONObject bonusCodeJSONObject) {
		this.readDataFromJson(bonusCodeJSONObject);
	}

	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray bonusCodeJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("bonusCodeRecord")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("bonusCodeRecord"))
				return null;
			
			bonusCodeJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("bonusCodeRecord");
		}
		
		return bonusCodeJSONArray;
	}
	
	//read all conditions by outlet ID
	public JSONArray readAllByOutletId(int iOutletId, int iShopId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("shopId", iShopId);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "woh", "getBonusCodeByOutletId", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject bonusCodeJSONArray) {
		JSONObject resultBonusCodes = null;
		
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		
		resultBonusCodes = bonusCodeJSONArray.optJSONObject("WohBonusCode");
		
		if(resultBonusCodes == null)
			resultBonusCodes = bonusCodeJSONArray;
		
		this.init();
		this.boncId = resultBonusCodes.optInt("bonc_id");
		this.shopId = resultBonusCodes.optInt("bonc_shop_id");
		this.oletId = resultBonusCodes.optInt("bonc_olet_id");
		this.bonusCode = resultBonusCodes.optString("bonc_code");
		String sStartDate = resultBonusCodes.optString("bonc_start_date");
		if(!sStartDate.isEmpty())
			try {
				this.startDate = dateFormat.parseDateTime(sStartDate);
			} catch (Exception e) {
				AppGlobal.stack2Log(e);
			}
		String sEndDate = resultBonusCodes.optString("bonc_end_date");
		if(!sEndDate.isEmpty())
			try {
				this.endDate = dateFormat.parseDateTime(sEndDate);
			} catch (Exception e) {
				AppGlobal.stack2Log(e);
			}
		
		this.status = resultBonusCodes.optString("bonc_status");
	}
	
	// init value
	public void init () {
		this.boncId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.bonusCode = "";
		this.startDate = null;
		this.endDate = null;
		this.status = "";
	}
	
	// get bonus ID
	public int getBonusId() {
		return this.boncId;
	}
	
	// get shop ID
	public int getShopId() {
		return this.shopId;
	}
	
	// get outlet ID
	public int getOutletId() {
		return this.oletId;
	}
	
	// get bonus code
	public String getBonusCode() {
		return this.bonusCode;
	}
	
	// get start date
	public DateTime getStartDate() {
		return this.startDate;
	}
	
	// get end date
	public DateTime getEndDate() {
		return this.endDate;
	}
	
	// get status
	public String getStatus() {
		return this.status;
	}
}
