//Database: pos_check_extra_infos - Extra information of the check
package app.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosCheckExtraInfo {
	private int ckeiId;
	private String by;
	private int chksId;
	private int cptyId;
	private int citmId;
	private int cdisId;
	private int cpayId;
	private String section;
	private String variable;
	private int index;
	private String value;
	private String status;
	
	// by
	public static String BY_ITEM = "item";
	public static String BY_CHECK = "check";
	public static String BY_PAYMENT = "payment";
	// section
	public static String SECTION_PREORDER = "preorder";
	public static String SECTION_ONLINE_COUPON = "online_coupon";
	public static String SECTION_ITEM_TYPE = "item_type";
	public static String SECTION_PMS = "pms";
	public static String SECTION_PAYMENT_INTERFACE = "payment_interface";
	public static String SECTION_CREDIT_CARD = "credit_card";
	public static String SECTION_MEMBERSHIP_INTERFACE = "membership_interface";
	
	//variable
	public static String VARIABLE_ITEM = "item";
	public static String VARIABLE_REDEEM_COUNT = "redeem_count";
	public static String VARIABLE_CALL_NO = "call_no";
	public static String VARIABLE_TABLE_MESSAGE = "table_message";
	public static String VARIABLE_CHECK_INFO = "check_info";
	public static String VARIABLE_COUPON_ITEM = "coupon_item";
	public static String VARIABLE_SELL_START_COUPON = "sell_start_coupon";
	public static String VARIABLE_SELL_END_COUPON = "sell_end_coupon";
	public static String VARIABLE_INTERFACE_ID = "intf_id";
	public static String VARIABLE_GUEST_NAME = "guest_name";
	public static String VARIABLE_GUEST_NO = "guest_no";
	public static String VARIABLE_ROOM = "room";
	public static String VARIABLE_REGISTER_NO = "reg_no";
	public static String VARIABLE_GUEST_FILE_NO = "guest_file_no";
	public static String VARIABLE_EXPIRY_DATE = "exp_date";
	public static String VARIABLE_FIELD_NO = "field_no";
	public static String VARIABLE_INTERNAL_USE = "internal_use";
	public static String VARIABLE_REFERENCE = "reference";
	public static String VARIABLE_HT_ID = "ht_id";
	public static String VARIABLE_COUPON = "coupon";
	public static String VARIABLE_MEMBER_NUMBER = "member_number";
	public static String VARIABLE_CARD_NO = "card_no";
	public static String VARIABLE_CARD_HOLDER_NAME = "holder_name";
	public static String VARIABLE_OUT_TRADE_NUMBER= "outTradeNumber";
	public static String VARIABLE_EXPIRE_TIME = "expireTime";
	public static String VARIABLE_PAYTYPE = "paytype";
	public static String VARIABLE_ACCOUNT_NUMBER = "account_number";
	public static String VARIABLE_ACCOUNT_NAME = "account_name";
	public static String VARIABLE_TRACE_ID = "trace_id";
	public static String VARIABLE_AUTH_CODE = "auth_code";
	public static String VARIABLE_LOCAL_BALANCE = "local_balance";
	public static String VARIABLE_ENGLISH_NAME = "english_name";
	public static String VARIABLE_CARD_TYPE_NAME = "card_type_name";
	public static String VARIABLE_PRINT_LINE = "print_line";
	public static String VARIABLE_POINTS_BALANCE = "points_balance";
	public static String VARIABLE_POINTS_EARN = "points_earn";
	public static String VARIABLE_CARD_SN = "card_sn";
	public static String VARIABLE_CARD_LEVEL_NAME = "card_level_name";
	public static String VARIABLE_PASSWORD = "password";
	
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosCheckExtraInfo () {
		this.ckeiId = 0;
		this.by = "";
		this.chksId = 0;
		this.cptyId = 0;
		this.citmId = 0;
		this.cdisId = 0;
		this.cpayId = 0;
		this.section = "";
		this.variable = "";
		this.index = 0;
		this.value = null;
		this.status = PosCheckExtraInfo.STATUS_ACTIVE;
	}
	
	//init object with other PosCheckExtraInfo
	public PosCheckExtraInfo (PosCheckExtraInfo oPosCheckExtraInfo) {
		this();
		
		this.ckeiId = oPosCheckExtraInfo.ckeiId;
		this.by = oPosCheckExtraInfo.by;
		this.chksId = oPosCheckExtraInfo.chksId;
		this.cptyId = oPosCheckExtraInfo.cptyId;
		this.citmId = oPosCheckExtraInfo.citmId;
		this.cdisId = oPosCheckExtraInfo.cdisId;
		this.cpayId = oPosCheckExtraInfo.cpayId;
		this.section = oPosCheckExtraInfo.section;
		this.variable = oPosCheckExtraInfo.variable;
		this.index = oPosCheckExtraInfo.index;
		this.value = oPosCheckExtraInfo.value;
		this.status = oPosCheckExtraInfo.status;
	}
	
	//init object with JSONObject
	public PosCheckExtraInfo (JSONObject oJSONObject) {
		this();
		
		JSONObject oCheckExtraInfoJSONObject = null;
		oCheckExtraInfoJSONObject = oJSONObject.optJSONObject("PosCheckExtraInfo");
		if(oCheckExtraInfoJSONObject == null)
			oCheckExtraInfoJSONObject = oJSONObject;
		
		this.ckeiId = oCheckExtraInfoJSONObject.optInt("ckei_id");
		this.by = oCheckExtraInfoJSONObject.optString("ckei_by");
		this.chksId = oCheckExtraInfoJSONObject.optInt("ckei_chks_id");
		this.cptyId = oCheckExtraInfoJSONObject.optInt("ckei_cpty_id");
		this.citmId = oCheckExtraInfoJSONObject.optInt("ckei_citm_id");
		this.cdisId = oCheckExtraInfoJSONObject.optInt("ckei_cdis_id");
		this.cpayId = oCheckExtraInfoJSONObject.optInt("ckei_cpay_id");
		this.section = oCheckExtraInfoJSONObject.optString("ckei_section");
		this.variable = oCheckExtraInfoJSONObject.optString("ckei_variable");
		this.index = oCheckExtraInfoJSONObject.optInt("ckei_index");
		this.value = oCheckExtraInfoJSONObject.optString("ckei_value", null);
		this.status = oCheckExtraInfoJSONObject.optString("ckei_status", PosCheckExtraInfo.STATUS_ACTIVE);
	}
	
	//init object form database by ckei_id
	public PosCheckExtraInfo (int iCkeiId) {
		this();
		this.ckeiId = iCkeiId;
	}
	
	//construct the save request
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("ckei_id", this.ckeiId);
			addSaveJSONObject.put("ckei_by", this.by);
			addSaveJSONObject.put("ckei_chks_id", this.chksId);
			addSaveJSONObject.put("ckei_cpty_id", this.cptyId);
			addSaveJSONObject.put("ckei_citm_id", this.citmId);
			addSaveJSONObject.put("ckei_cdis_id", this.cdisId);
			addSaveJSONObject.put("ckei_cpay_id", this.cpayId);
			addSaveJSONObject.put("ckei_section", this.section);
			addSaveJSONObject.put("ckei_variable", this.variable);
			addSaveJSONObject.put("ckei_index", this.index);
			if(this.value != null)
				addSaveJSONObject.put("ckei_value", this.value);
			addSaveJSONObject.put("ckei_status", this.status);
			
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray checkTableMessageJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
					return null;
				
				if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("newCheckExtraInfos"))
					return null;
					
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("newCheckExtraInfos"))
					return null;
				
				checkTableMessageJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("newCheckExtraInfos");			
		}
		return checkTableMessageJSONArray;
	}
	
	public JSONArray addUpdateWithMutlipleRecords(ArrayList<PosCheckExtraInfo> oPosCheckExtraInfos) {
		JSONObject tempCheckExtraInfoJSONObject = null, checkExtraInfoJSONObject = new JSONObject();
		JSONArray checkExtraInfoJSONArray = new JSONArray();
		JSONArray oReturnJSONArray = null;
		
		for(PosCheckExtraInfo oPosCheckExtraInfo : oPosCheckExtraInfos) {
			if(oPosCheckExtraInfo.getCkeiId() == 0)
				tempCheckExtraInfoJSONObject = oPosCheckExtraInfo.constructAddSaveJSON(false);
			else
				tempCheckExtraInfoJSONObject = oPosCheckExtraInfo.constructAddSaveJSON(true);
			checkExtraInfoJSONArray.put(tempCheckExtraInfoJSONObject);
		}
		
		try {
			checkExtraInfoJSONObject.put("posCheckExtraInfo", checkExtraInfoJSONArray);
		}
		catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		oReturnJSONArray = this.readDataListFromApi("gm", "pos", "saveMultiCheckExtraInfos", checkExtraInfoJSONObject.toString());
		
		return oReturnJSONArray;
	}
	
	public JSONArray readAllByCheckIds(String sConfigBy, ArrayList<Integer> oCheckIds, String sStatus) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("configBy", sConfigBy);
			requestJSONObject.put("checkIds", oCheckIds);
			requestJSONObject.put("status", sStatus);
		}
		catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getCheckExtraInfoByCheckIds", requestJSONObject.toString());
		
		return responseJSONArray;
	}
	
	
	//get ckeiId
	public int getCkeiId() {
		return this.ckeiId;
	}
	
	public String getBy() {
		return this.by;
	}
	
	public int getChksId() {
		return this.chksId;
	}
	
	protected int getCptyId() {
		return this.cptyId;
	}
	
	protected int getCitmId() {
		return this.citmId;
	}
	
	protected int getCdisId() {
		return this.cdisId;
	}
	
	protected int getCpayId() {
		return this.cpayId;
	}
	
	public String getSection() {
		return this.section;
	}
	
	public String getVariable() {
		return this.variable;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setCkeiId(int iCkeiId) {
		this.ckeiId = iCkeiId;
	}
	
	public void setBy(String sBy) {
		this.by = sBy;
	}
	
	public void setCheckId(int iChksId) {
		this.chksId = iChksId;
	}
	
	public void setItemId(int iItemId) {
		this.citmId = iItemId;
	}
	
	public void setSection(String sSection) {
		this.section = sSection;
	}
	
	public void setVariable(String sVariable) {
		this.variable = sVariable;
	}
	
	public void setIndex(int iIndex) {
		this.index = iIndex;
	}
	
	public void setValue(String sValue) {
		this.value = sValue;
	}
	
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
}
