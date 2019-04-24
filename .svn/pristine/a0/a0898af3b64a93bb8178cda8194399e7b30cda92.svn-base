//Database: pos_gratuities methods
package om;

import java.math.BigDecimal;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosGratuity {
	private int gratId;
	private String code;
	private String type;
	private int ggrpId;
	private int seq;
	private String method;
	private BigDecimal rate;
	private BigDecimal fixAmount;
	private String inputName;
	private String status;
	private String[] name;
	private String[] shortName;
	private int userGroupId;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	public static String STATUS_SUSPENDED = "s";
	
	// method
	public static String METHOD_PERCENTAGE = "";
	public static String METHOD_FIX_AMOUNT = "f";
	
	// input name
	public static String INPUT_NAME_DEFAULT = "";
	public static String INPUT_NAME_OPEN_DESC = "o";
	public static String INPUT_NAME_APPEND_DESC = "a";
	
	//init object with initialize value
	public PosGratuity() {
		this.init();
	}
	
	//init object with JSONObject
	public PosGratuity(JSONObject gratuityJSONObject) {
		readDataFromJson(gratuityJSONObject);
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray gratuityJSONArray = null;
		if (OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false) == false)
			return null;
		else {
			try {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
					return null;
				if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("gratuities")) {
					OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
					return null;
				}
				
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("gratuities"))
					return null;
				
				gratuityJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().getJSONArray("gratuities");
				
			}catch(JSONException jsone) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.stackToString(jsone));
				return null;
			}
		}
		return gratuityJSONArray;
	}
	
	//get all by Access Right
	public JSONArray readAllWithAccessRight(int iOutletId, String sBusinessDay, boolean bIsHoliday, boolean bIsDayBeforeHoliday, boolean bIsSpecialDay, boolean bIsDayBeforeSpecialDay, int iWeekday) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
			
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("businessDay", sBusinessDay);
			requestJSONObject.put("isHoliday", bIsHoliday);
			requestJSONObject.put("isDayBeforeHoliday", bIsDayBeforeHoliday);
			requestJSONObject.put("isSpecialDay", bIsSpecialDay);
			requestJSONObject.put("isDayBeforeSpecialDay", bIsDayBeforeSpecialDay);
			requestJSONObject.put("weekday", iWeekday);
			requestJSONObject.put("recursive", -1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
			
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllGratuitiesWithAccessControl", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	//get all by type
	public JSONArray readAllByType(String sType) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
			
		try {
			requestJSONObject.put("type", sType);
			requestJSONObject.put("recursive", -1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
			
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllGratuitiesByType", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject gratuityJSONArray) {
		JSONObject oResultGratuity = null, oResultGratuityAcl = null;
		oResultGratuity = gratuityJSONArray.optJSONObject("PosGratuity");
		if(oResultGratuity == null)
			oResultGratuity = gratuityJSONArray;
		
		this.init();
		this.gratId = oResultGratuity.optInt("grat_id");
		this.code = oResultGratuity.optString("grat_code");
		this.type = oResultGratuity.optString("grat_type");
		this.ggrpId = oResultGratuity.optInt("grat_ggrp_id");
		this.seq = oResultGratuity.optInt("grat_seq");
		this.method = oResultGratuity.optString("grat_method");
		this.rate = new BigDecimal(oResultGratuity.optString("grat_rate", "0.0"));
		this.fixAmount = new BigDecimal(oResultGratuity.optString("grat_fix_amount", "0.0"));
		this.inputName = oResultGratuity.optString("grat_input_name", PosGratuity.INPUT_NAME_DEFAULT);	
		this.status = oResultGratuity.optString("grat_status", PosGratuity.STATUS_ACTIVE);	
		for(int i=0; i<5; i++) {
			this.name[i] = oResultGratuity.optString("grat_name_l"+(i+1));
			this.shortName[i] = oResultGratuity.optString("grat_short_name_l"+(i+1));
		}
		oResultGratuityAcl = gratuityJSONArray.optJSONObject("PosGratuityAcl");
		if(oResultGratuityAcl != null)
			this.userGroupId = oResultGratuityAcl.optInt("gacl_ugrp_id");
	}
	
	// init value
	public void init () {
		int i=0;
		this.gratId = 0;
		this.code = "";
		this.type = "";
		this.ggrpId = 0;
		this.seq = 0;
		this.method = "";
		this.rate = BigDecimal.ZERO;
		this.fixAmount = BigDecimal.ZERO;
		this.inputName = "";
		this.status = PosGratuity.STATUS_ACTIVE;
		
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.userGroupId = 0;
	}

	public int getGratId() {
		return gratId;
	}

	public String getCode() {
		return code;
	}

	public String getType() {
		return type;
	}

	public int getGgrpId() {
		return ggrpId;
	}

	public int getSeq() {
		return seq;
	}

	public String getMethod() {
		return method;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public BigDecimal getFixAmount() {
		return fixAmount;
	}

	public String getInputName() {
		return inputName;
	}

	public String getStatus() {
		return status;
	}

	public String[] getName() {
		return name;
	}

	public String[] getShortName() {
		return shortName;
	}
	public int getUserGroupId() {
		return userGroupId;
	}
	public void setGratId(int gratId) {
		this.gratId = gratId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setGgrpId(int ggrpId) {
		this.ggrpId = ggrpId;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public void setFixAmount(BigDecimal fixAmount) {
		this.fixAmount = fixAmount;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setName(String[] name) {
		this.name = name;
	}

	public void setShortName(String[] shortName) {
		this.shortName = shortName;
	}
	
	public void setUserGroupId(int serGroupId) {
		this.userGroupId = serGroupId;
	}
}
