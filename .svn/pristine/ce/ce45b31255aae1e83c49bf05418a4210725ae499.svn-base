//Database: pos_void_reasons - Void reason types
package app.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosVoidReason {
	private int vdrsId;
	private String code;
	private String[] name;
	private String[] shortName;
	private String type;
	private int seq;
	private String status;
	
	// type
	public static String TYPE_VOID_CHECK = "c";
	public static String TYPE_VOID_ITEM = "i";
	public static String TYPE_VOID_DISCOUNT = "d";
	public static String TYPE_VOID_PAYMENT = "p";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELTED = "d";
	
	//init object with initialize value
	public PosVoidReason () {
		this.init();
	}
	
	public PosVoidReason(JSONObject voidReasonJSONObject) {
		readDataFromJson(voidReasonJSONObject);
	}
	
	//Init object from database by vdrs_id
	public PosVoidReason (int iVdrsId) {
		this.init();
		
		this.vdrsId = iVdrsId;
	}

	public boolean readByTypeAndCode(String sType, String sCode) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("voidReasonType", sType);
			requestJSONObject.put("voidReasonCode", sCode);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}

		return this.readDataFromApi("gm", "pos", "getVoidReasonByTypeAndCode", requestJSONObject.toString());
	}
	
	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean result = true;
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			result = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("void_reason")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("void_reason")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("void_reason");
			if(tempJSONObject.isNull("PosVoidReason")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return result;
	}

	//read data from POS API
	private JSONArray readDataListFromApi(String wsInterface, String module, String fcnName, String param) {
		JSONArray voidReasonJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(wsInterface, module, fcnName, param, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("void_reasons")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("void_reasons"))
				return null;
			
			voidReasonJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("void_reasons");
		}
		
		return voidReasonJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject voidReasonJSONObject) {
		JSONObject resultVoidReason = null;
		int i;
		
		resultVoidReason = voidReasonJSONObject.optJSONObject("PosVoidReason");
		if(resultVoidReason == null)
			resultVoidReason = voidReasonJSONObject;

		this.init();
		this.vdrsId = resultVoidReason.optInt("vdrs_id");
		this.code = resultVoidReason.optString("vdrs_code", "");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultVoidReason.optString("vdrs_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultVoidReason.optString("vdrs_short_name_l"+i);
		this.type = resultVoidReason.optString("vdrs_type");
		this.seq = resultVoidReason.optInt("vdrs_seq");
		this.status = resultVoidReason.optString("vdrs_status", PosVoidReason.STATUS_ACTIVE);
	}
	
	//get check item lists from database by check id
	public JSONArray getVoidReasonByType(String sType) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("voidReasonType", sType);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getVoidReasonListByType", requestJSONObject.toString());
		
		return responseJSONArray;
		
	}
	
	//get check item lists from database by check id
	public JSONArray readAll() {
		JSONArray voidReasonJSONArray = null;
		voidReasonJSONArray = this.readDataListFromApi("gm", "pos", "getAllVoidReasons", "");
		return voidReasonJSONArray;
		
	}

	// init value
	public void init() {
		int i=0;
		
		this.vdrsId = 0;
		this.code = "";
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.type = "";
		this.seq = 0;
		this.status = PosVoidReason.STATUS_ACTIVE;
	}
	
	//read data from database by vdrs_id
	public void readById(int iVdrsId) {
		this.vdrsId = iVdrsId;
	}
	
	//add new void reason record to database
	public boolean add() {
		return true;
	}
	
	//update void reason record to database
	public boolean update() {
		return true;
	}
	
	//get vdrsId
	public int getVdrsId() {
		return this.vdrsId;
	}
	
	//get code
	public String getCode() {
		return this.code;
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
	protected String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get type
	public String getType() {
		return this.type;
	}
	
	//get seq
	public int getSeq() {
		return this.seq;
	}
	
	//get status
	protected String getStatus() {
		return this.status;
	}
}
