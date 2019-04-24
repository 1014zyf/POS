package app.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosDisplayPanelPage {
	private int dppgId;
	private int dpanId;
	private int dpzoId;
	private int seq;
	private String[] name;
	private String[] shortName;
	private String type;
	private String status;

	// type
	public static String TYPE_NORMAL = "";
	public static String TYPE_SUB_PANEL = "s";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosDisplayPanelPage () {
		this.init();
	}
	
	//init obejct with JSONObject
	public PosDisplayPanelPage(JSONObject displayPanelPageJSONObject) {
		readDataFromJson(displayPanelPageJSONObject);
	}
	
	//construct the save request JSON
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		int i=0;
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("dppg_id", Integer.toString(this.dppgId));
			if(this.dpanId > 0)
				addSaveJSONObject.put("dppg_dpan_id", this.dpanId);
			if(this.dpzoId > 0)
				addSaveJSONObject.put("dppg_dpzo_id", this.dpzoId);
			if(this.seq > 0)
				addSaveJSONObject.put("dppg_seq", this.seq);
			for(i=1; i<=5; i++) {
				if(!this.name[i-1].isEmpty())
					addSaveJSONObject.put("dppg_name_l"+i, this.name[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(!this.shortName[i-1].isEmpty())
					addSaveJSONObject.put("dppg_short_name_l"+i, this.shortName[(i-1)]);
			}
			if(!this.type.isEmpty())
				addSaveJSONObject.put("dppg_type", this.type);
			if(!this.status.isEmpty())
				addSaveJSONObject.put("dppg_status", this.status);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("displayPanelPage")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("displayPanelPage")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("displayPanelPage");
			if(tempJSONObject.isNull("PosDisplayPanelPage")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray displayPanelLookupJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("displayPanelPage")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("displayPanelPage"))
				return null;
			
			displayPanelLookupJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("displayPanelPage");
		}
		
		return displayPanelLookupJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject displayPanelPageJSONObject) {
		JSONObject resultDisplayPanelPage = null;
		int i;
		
		resultDisplayPanelPage = displayPanelPageJSONObject.optJSONObject("PosDisplayPanelPage");
		if(resultDisplayPanelPage == null)
			resultDisplayPanelPage = displayPanelPageJSONObject;
			
		this.init();
		this.dppgId = resultDisplayPanelPage.optInt("dppg_id");
		this.dpanId = resultDisplayPanelPage.optInt("dppg_dpan_id");
		this.dpzoId = resultDisplayPanelPage.optInt("dppg_dpzo_id");
		this.seq = resultDisplayPanelPage.optInt("dppg_seq");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultDisplayPanelPage.optString("dppg_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultDisplayPanelPage.optString("dppg_short_name_l"+i);
		this.type = resultDisplayPanelPage.optString("dppg_type", PosDisplayPanelPage.TYPE_NORMAL);
		this.status = resultDisplayPanelPage.optString("dppg_status", PosDisplayPanelPage.STATUS_ACTIVE);
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.dppgId = 0;
		this.dpanId = 0;
		this.dpzoId = 0;
		this.seq = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.type = PosDisplayPanelPage.TYPE_NORMAL;
		this.status = PosDisplayPanelPage.STATUS_ACTIVE;
	}
	
	/*** Getter and Setter methods ***/
	public int getPageId() {
		return this.dppgId;
	}
	
	public void setPageId(int iPageId){
		this.dppgId = iPageId;
	}
	
	public int getPanelId() {
		return this.dpanId;
	}
	
	public int getZoneId() {
		return this.dpzoId;
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
	
	//get type
	public String getType() {
		return this.type;
	}
	
	//set name by lang index
	public void setName(int iIndex, String sName) {
		this.name[(iIndex-1)] = sName;
	}
	
	//set short name by lang index
	public void setShortName(int iIndex, String sShortName) {
		this.shortName[(iIndex-1)] = sShortName;
	}
	
	//set status
	public void setStatus(String sStatus){
		this.status = sStatus;
	}

	//set type
	public void setType(String sType) {
		this.type = sType;
	}
}
