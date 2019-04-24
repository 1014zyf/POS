package app.model;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosTableMessage {
	private int tblmId;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String status;
	
	//Status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	public PosTableMessage() {
		this.tblmId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(int i = 0; i < 5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(int i = 0; i < 5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.status = PosTableMessage.STATUS_ACTIVE;
	}
	
	public PosTableMessage(JSONObject tableMessageJSONObject) {
		this();
		this.readDataFromJson(tableMessageJSONObject);
	}
	
	//Get Table Message
	public JSONArray readAll() {
		JSONArray tableMessageJSONArray = null;
		tableMessageJSONArray = this.readDataListFromApi("gm", "pos", "getAllTableMessages", "");
		return tableMessageJSONArray;
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String wsInterface, String module, String fcnName, String param) {
		JSONArray tableMessageJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(wsInterface, module, fcnName, param, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("table_messages")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("table_messages"))
				return null;
			
			tableMessageJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("table_messages");
		}
		return tableMessageJSONArray;
	}
	
	public void readDataFromJson(JSONObject tableMessageJSONObject) {
		int i = 0;
		JSONObject tempJSONObject = null;

		tempJSONObject = tableMessageJSONObject.optJSONObject("PosTableMessage");
		if(tempJSONObject == null)
			tempJSONObject = tableMessageJSONObject;

		this.tblmId = tempJSONObject.optInt("tblm_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = tempJSONObject.optString("tblm_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = tempJSONObject.optString("tblm_short_name_l"+i);
		this.seq = tempJSONObject.optInt("tblm_seq");
		this.status = tempJSONObject.optString("tblm_status", PosTableMessage.STATUS_ACTIVE);
	}
	
	//Get tblmId
	public int getTblmId() {
		return this.tblmId;
	}
	
	//TODO temp
	public void setTblmId(int iTableMsgId) {
		this.tblmId = iTableMsgId;
	}
	
	//Get name by lang index
	public String getName(int iIndex) {
		return this.name[(iIndex - 1)];
	}

	//Get short name by lang index
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex - 1)];
	}
	
	//Get display sequence
	public int getSeq() {
		return this.seq;
	}
	
	//TODO temp
	public void setSeq(int iSeq) {
		this.seq = iSeq;
	}
	
	//Get status
	protected String getStatus() {
		return this.status;
	}
}