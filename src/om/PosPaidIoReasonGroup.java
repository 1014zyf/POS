package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosPaidIoReasonGroup {
	private int piogId;
	private String[] name;
	private String[] shortName;
	private String type;
	private int seq;
	private String status;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";

	public PosPaidIoReasonGroup() {
		this.init();
	}
	
	public PosPaidIoReasonGroup(JSONObject paidInOutReasonCategoryJSONObject) {
		readDataFromJson(paidInOutReasonCategoryJSONObject);
	}

	//get check item lists from database by check id
	public JSONArray readAll() {
		JSONArray voidReasonJSONArray = null;
		voidReasonJSONArray = this.readDataListFromApi("gm", "pos", "getAllPaidInOutReasonCategories", "");
		return voidReasonJSONArray;
		
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject paidInOutReasonCategoryJSONObject) {
		JSONObject resultPaidInOutReasonCategory = null;
		int i;
		
		resultPaidInOutReasonCategory = paidInOutReasonCategoryJSONObject.optJSONObject("PosPaidInOutReasonCategory");
		if(resultPaidInOutReasonCategory == null)
			resultPaidInOutReasonCategory = paidInOutReasonCategoryJSONObject;
		
		this.init();
		
		this.piogId = resultPaidInOutReasonCategory.optInt("piog_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultPaidInOutReasonCategory.optString("piog_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultPaidInOutReasonCategory.optString("piog_short_name_l"+i);
		this.type = resultPaidInOutReasonCategory.optString("piog_type");
		this.seq = resultPaidInOutReasonCategory.optInt("piog_seq");
		this.status = resultPaidInOutReasonCategory.optString("piog_status", PosPaidIoReasonGroup.STATUS_ACTIVE);
	}

	//get check item lists from database by check id
	public JSONArray getPaidInOutReasonCategoryListByType(String sType) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("paidInOutReasonCategoryType", sType);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getPaidInOutReasonCategoryListByType", requestJSONObject.toString());
		
		return responseJSONArray;
		
	}
	
	//construct the save request JSON
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		int i=0;
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("piog_id", this.piogId);
			for(i=1; i<=5; i++) {
				if(!this.name[i-1].isEmpty())
					addSaveJSONObject.put("piog_name_l"+i, this.name[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(!this.shortName[i-1].isEmpty())
					addSaveJSONObject.put("piog_short_name_l"+i, this.shortName[i-1]);
			}
			addSaveJSONObject.put("piog_type", this.type);
			addSaveJSONObject.put("piog_seq", this.seq);
			addSaveJSONObject.put("piog_status", this.status);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}

	//read data from POS API
	private JSONArray readDataListFromApi(String wsInterface, String module, String fcnName, String param) {
		JSONArray voidReasonJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(wsInterface, module, fcnName, param, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("paid_in_out_reason_categories")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("paid_in_out_reason_categories")) {
				return null;
			}
			
			voidReasonJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("paid_in_out_reason_categories");
		}
		
		return voidReasonJSONArray;
	}

	private void init() {
		this.piogId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(int i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(int i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.status = PosPaidIoReasonGroup.STATUS_ACTIVE;
	}
	
	public int getPiogId() {
		return this.piogId;
	}
}
