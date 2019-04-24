//Database: pos_pantry_messages - Pantry messages
package om;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosPantryMessage {
	private int panmId;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String status;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosPantryMessage () {
		this.init();
	}
	
	public PosPantryMessage(JSONObject pantryMesgJSONObject) {
		this.readDataFromJson(pantryMesgJSONObject);
	}
	
	//get check item lists from database by check id
	public JSONArray readAll() {
		JSONArray pantryMesgJSONArray = null;
		pantryMesgJSONArray = this.readDataListFromApi("gm", "pos", "getAllPantryMessages", "");
		return pantryMesgJSONArray;
		
	}

	//read data from POS API
	private JSONArray readDataListFromApi(String wsInterface, String module, String fcnName, String param) {
		JSONArray pantryMesgJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(wsInterface, module, fcnName, param, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("pantry_messages")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("pantry_messages"))
				return null;
			
			pantryMesgJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("pantry_messages");
		}
		
		return pantryMesgJSONArray;
	}
	
	public void readDataFromJson(JSONObject pantryMesgJSONObject) {
		int i=0;
		JSONObject tempJSONObject = null;

		tempJSONObject = pantryMesgJSONObject.optJSONObject("PosPantryMessage");
		if(tempJSONObject == null)
			tempJSONObject = pantryMesgJSONObject;

		this.init();
		this.panmId = tempJSONObject.optInt("panm_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = tempJSONObject.optString("panm_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = tempJSONObject.optString("panm_short_name_l"+i);
		this.seq = tempJSONObject.optInt("panm_seq");
		this.status = tempJSONObject.optString("panm_status", PosPantryMessage.STATUS_ACTIVE);
	}

	// init value
	public void init() {
		int i=0;
		
		this.panmId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.status = PosPantryMessage.STATUS_ACTIVE;
	}
	
	//Init object from database by panm_id
	public PosPantryMessage (int iPanmId) {
		this.panmId = iPanmId;
	}
	
	//read data from database by panm_id
	public void readById(int iPanmId) {
		this.panmId = iPanmId;
	}
	
	//add new pantry message record to database
	public boolean add() {
		return true;
	}
	
	//update pantry message record to database
	public boolean update() {
		return true;
	}
	
	//get panmId
	public int getPanmId() {
		return this.panmId;
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
			if(this.getShortName(iBilingualLangIndex).isEmpty()){
				if(!sName.equals(this.getName(iBilingualLangIndex)))
					sName += "\n" + this.getName(iBilingualLangIndex);
			}	
			else{
				if(!sName.equals(this.getShortName(iBilingualLangIndex)))
					sName += "\n" + this.getShortName(iBilingualLangIndex);
			}
		}
		
		return sName;
	}
	
	//get short name by lang index
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
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
