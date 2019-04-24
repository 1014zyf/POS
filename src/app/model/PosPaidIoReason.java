package app.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosPaidIoReason {
	private int piorId;
	private String[] name;
	private String[] shortName;
	private String type;
	private int piogId;
	private int seq;
	private String status;
	
	// type
	public static String TYPE_PAID_IN = "i";
	public static String TYPE_PAID_OUT = "o";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";

	//init object with initialize value
	public PosPaidIoReason() {
		this.init();
	}

	public PosPaidIoReason(JSONObject paidInOutReasonJSONObject) {
		readDataFromJson(paidInOutReasonJSONObject);
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String wsInterface, String module, String fcnName, String param) {
		JSONArray voidReasonJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(wsInterface, module, fcnName, param, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("paid_io_reasons")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("paid_io_reasons"))
				return null;
			
			voidReasonJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("paid_io_reasons");
		}
		
		return voidReasonJSONArray;
	}

	//get check item lists from database by check id
	public JSONArray readAll() {
		JSONArray paidInOutReasonJSONArray = null;
		paidInOutReasonJSONArray = this.readDataListFromApi("gm", "pos", "getAllPaidIoReasons", "");
		return paidInOutReasonJSONArray;
		
	}

	//get check item lists from database by check id
	public JSONArray getPaidInOutReasonsByType(String sType) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("paidInOutReasonType", sType);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getPaidInOutReasonListByType", requestJSONObject.toString());
		
		return responseJSONArray;
		
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject paidInOutReasonJSONObject) {
		int i;
		JSONObject tempPaidInOutReasonJSONObject = null;
		
		tempPaidInOutReasonJSONObject = paidInOutReasonJSONObject.optJSONObject("PosPaidIoReason");
		if(tempPaidInOutReasonJSONObject == null)
			tempPaidInOutReasonJSONObject = paidInOutReasonJSONObject;
		
		this.init();
		this.piorId = tempPaidInOutReasonJSONObject.optInt("pior_id");
		for(i=1; i<=5; i++) 
			this.name[(i-1)] = tempPaidInOutReasonJSONObject.optString("pior_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = tempPaidInOutReasonJSONObject.optString("pior_short_name_l"+i);
		this.type = tempPaidInOutReasonJSONObject.optString("pior_type");
		this.piogId = tempPaidInOutReasonJSONObject.optInt("pior_piog_id");
		this.seq = tempPaidInOutReasonJSONObject.optInt("pior_seq");
		this.status = tempPaidInOutReasonJSONObject.optString("pior_status", PosPaidIoReason.STATUS_ACTIVE);
	}
	
	//construct the save request JSON
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		int i=0;
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("pior_id", this.piorId);
			for(i=1; i<=5; i++) {
				if(!this.name[i-1].isEmpty())
					addSaveJSONObject.put("pior_name_l"+i, this.name[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(!this.shortName[i-1].isEmpty())
					addSaveJSONObject.put("pior_short_name_l"+i, this.shortName[i-1]);
			}
			addSaveJSONObject.put("pior_type", this.type);
			if(this.piogId > 0)
				addSaveJSONObject.put("pior_piog_id", this.piogId);
			addSaveJSONObject.put("pior_seq", this.seq);
			addSaveJSONObject.put("pior_status", this.status);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}

	private void init() {
		this.piorId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(int i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(int i=0; i<5; i++)
			this.shortName[i] = "";
		this.piogId = 0;
		this.seq = 0;
		this.status = PosPaidIoReason.STATUS_ACTIVE;
	}

	//get name by lang index
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	public String getBilingualName(int iIndex, int iBilingualLangIndex) {
		String sName;
		if(this.getShortName(iIndex).isEmpty())
			sName = this.getName(iIndex);
		else
			sName = this.getShortName(iIndex);
		
		if (iBilingualLangIndex > 0 && iIndex != iBilingualLangIndex) {
			if(this.getShortName(iBilingualLangIndex).isEmpty())
				sName += "\n" + this.getName(iBilingualLangIndex);
			else
				sName += "\n" + this.getShortName(iBilingualLangIndex);
		}
		
		return sName;
	}
	
	//get short name by lang index
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	public int getPiorId() {
		return this.piorId;
	}
	
	public String getType() {
		return this.type;
	}
	
	public int getPiogId() {
		return this.piogId;
	}
	
	public int getSeq() {
		return this.seq;
	}
}
