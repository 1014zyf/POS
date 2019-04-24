package om;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosItemRemindRule {
	private int itrmId;
	private int shopId;
	private int oletId;
	private int itemId;
	private int menuId;
	
	//setting	
	private String suggestion; 
	private int minOrder;   //Min. limit of item selected if itrm_menu_id > 0 ;
							//0 – need to order all items in menu
							//> 0 - need to order x items in menu

	private String remindMode;
	
	private String timeMode; //empty – apply with no time limitation
							 //o – apply time checking with open check time
							 //c – apply time checking with current time

	private Time startTime; //null – no start time restriction
	
	private Time endTime; //null – no end time restriction
	
	private String status;
	
	
	//suggestion
	public final static String SUGGESTION_SUGGEST = "";
	public final static String SUGGESTION_FORCE = "f";
	
	//remind mode
	public final static String REMINDMODE_PRINT_CHRCK = "";
	public final static String REMINDMODE_PRINT_SEND_CHECK = "s";
	
	//time mode
	public final static String TIMEMODE_NO_LIMITATION = "";
	public final static String TIMEMODE_OPENCHECK_TIME = "o"; 
	public final static String TIMEMODE_CURRENT_TIME = "c";
	
	// status
	public final static String STATUS_ACTIVE = "";	//Active
	public final static String STATUS_SUSPENDED = "s"; 	//Suspended
	
	//init object with initialize value
	public PosItemRemindRule(){
		this.init();
	}
	
	//init object with JSONObject
	public PosItemRemindRule(JSONObject oItemRemindRuleJSONObject) {
		this.readDataFromJson(oItemRemindRuleJSONObject);
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray itemRemindRuleJSONArray = null;
		
		if (OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false) == false)
			return null;
		else {
			try {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
					return null;
				
				if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("itemRemindRules")) {
					OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
					return null;
				}
				
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("itemRemindRules"))
					return null;
				
				itemRemindRuleJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().getJSONArray("itemRemindRules");
				
			}catch(JSONException jsone) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.stackToString(jsone));
				return null;
			}
		}
		return itemRemindRuleJSONArray;
	}
	
	//get all by shop and outlet Id
	public JSONArray readAllByShopAndOutletId(int shopId, int outletId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
			
		try {
			requestJSONObject.put("outletId", outletId);
			requestJSONObject.put("shopId", shopId);	
			requestJSONObject.put("recursive", -1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
			
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllItemRemindRulesByOutletAndShopId", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	public void readDataFromJson(JSONObject oItemRemindRuleJSONObject) {
		JSONObject oTempJSONObject = null;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		
		oTempJSONObject = oItemRemindRuleJSONObject.optJSONObject("PosItemRemindRule");
		if(oTempJSONObject == null)
			oTempJSONObject = oItemRemindRuleJSONObject;
			
		this.init();
		this.itrmId = oTempJSONObject.optInt("itrm_id");
		this.shopId = oTempJSONObject.optInt("itrm_shop_id");
		this.oletId = oTempJSONObject.optInt("itrm_olet_id");
		this.itemId = oTempJSONObject.optInt("itrm_item_id");
		this.menuId = oTempJSONObject.optInt("itrm_menu_id");
		
		this.suggestion = oTempJSONObject.optString("itrm_suggestion", PosItemRemindRule.SUGGESTION_SUGGEST);
		this.minOrder = oTempJSONObject.optInt("itrm_min_order");
		this.remindMode = oTempJSONObject.optString("itrm_remind_mode", PosItemRemindRule.REMINDMODE_PRINT_CHRCK);
		this.timeMode = oTempJSONObject.optString("itrm_time_mode", PosItemRemindRule.TIMEMODE_NO_LIMITATION);
		
		String sStartTime = oTempJSONObject.optString("itrm_start_time");
		if(!sStartTime.isEmpty()){
			try {
				this.startTime = new Time(dateFormat.parse(sStartTime).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		String sEndTime = oTempJSONObject.optString("itrm_end_time");
		if(!sEndTime.isEmpty()){
			try {
				this.endTime = new Time(dateFormat.parse(sEndTime).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		this.status = oTempJSONObject.optString("itrm_status", PosItemRemindRule.STATUS_ACTIVE);
	}
	
	// init value
	public void init(){
		this.itrmId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.itemId = 0;
		this.menuId = 0;
		this.suggestion = PosItemRemindRule.SUGGESTION_SUGGEST;
		this.minOrder = 0;
		this.remindMode = PosItemRemindRule.REMINDMODE_PRINT_CHRCK;
		this.timeMode = PosItemRemindRule.TIMEMODE_NO_LIMITATION;
		this.startTime = null;
		this.endTime = null;
		this.status = PosItemRemindRule.STATUS_ACTIVE;
	}
	
	public int getItrmId(){
		return this.itrmId;
	}
	
	public int getShopId(){
		return this.shopId;
	}
	
	public int getOletId(){
		return this.oletId;
	}
	
	public int getItemId(){
		return this.itemId;
	}
	
	public int getMenuId(){
		return this.menuId;
	}
	
	public String getSuggestion(){
		return this.suggestion;
	}

	public int getMinOrder() {
		return this.minOrder;
	}

	public String getRemindMode() {
		return this.remindMode;
	}

	public String getTimeMode() {
		return this.timeMode;
	}

	public Time getStartTime() {
		return this.startTime;
	}

	public Time getEndTime() {
		return this.endTime;
	}

	public String getStatus() {
		return this.status;
	}
}
