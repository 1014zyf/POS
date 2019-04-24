package om;

import org.json.JSONArray;
import org.json.JSONObject;

public class OutTableSection {
	private int sectId;
	private int sectShopId;
	private int sectOletId;
	private String sectCode;
	private String[] sectName;
	private String[] sectShortName;
	private int sectSeq;
	private String sectStatus;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public OutTableSection () {
		this.init();
	}
	
	//init obejct with JSONObject
	public OutTableSection(JSONObject oTableSectionJSONObject) {
		readDataFromJson(oTableSectionJSONObject);
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject oTableSectionJSONObject) {
		JSONObject oTempJSONObject = null;
		
		oTempJSONObject = oTableSectionJSONObject.optJSONObject("OutOutletSection");
		if(oTempJSONObject == null)
			oTempJSONObject = oTableSectionJSONObject;
			
		this.init();
		this.sectId = oTempJSONObject.optInt("sect_id");
		this.sectShopId = oTempJSONObject.optInt("sect_shop_id");
		this.sectOletId = oTempJSONObject.optInt("sect_olet_id");
		this.sectCode = oTempJSONObject.optString("sect_code");
		this.sectStatus = oTempJSONObject.optString("sect_status");
		for(int i=0; i<5; i++) {
			this.sectName[i] = oTempJSONObject.optString("sect_name_l"+(i+1));
			this.sectShortName[i] = oTempJSONObject.optString("sect_short_name_l"+(i+1));
		}
		this.sectSeq = oTempJSONObject.optInt("sect_seq");	
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray functionJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {	
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("tableSections")) {
				// Return a list of sections
				
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("tableSections"))
					return null;
				
				functionJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("tableSections");
			} else if (OmWsClientGlobal.g_oWsClient.get().getResponse().has("tableSections")) {
				// Return a single section
				
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("tableSections"))
					return null;
				
				functionJSONArray = new JSONArray();
				functionJSONArray.put(OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("tableSections"));
			}
		}		
		return functionJSONArray;
	}
	
	public JSONArray getAllSections(int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try{
			requestJSONObject.put("outletId", iOutletId);
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "outlet", "findAllTableSections", requestJSONObject.toString());

		return responseJSONArray;		
	}
	
	//init object
	public void init() {
		int i=0;
		
		this.sectId = 0;
		this.sectShopId = 0;
		this.sectOletId = 0;
		this.sectCode = "";
		if(this.sectName == null)
			this.sectName = new String[5];
		for(i=0; i<5; i++)
			this.sectName[i] = "";
		if(this.sectShortName == null)
			this.sectShortName = new String[5];
		for(i=0; i<5; i++)
			this.sectShortName[i] = "";
		this.sectStatus = OutTableSection.STATUS_ACTIVE;
		this.sectSeq = 0;
	}
	
	public int getSectId() {
		return sectId;
	}

	public void setSectId(int sectId) {
		this.sectId = sectId;
	}

	public int getSectShopId() {
		return sectShopId;
	}

	public void setSectShopId(int sectShopId) {
		this.sectShopId = sectShopId;
	}

	public int getSectOletId() {
		return sectOletId;
	}

	public void setSectOletId(int sectOletId) {
		this.sectOletId = sectOletId;
	}

	public String getSectCode() {
		return sectCode;
	}

	public void setSectCode(String sectCode) {
		this.sectCode = sectCode;
	}

	public String getSectName(int iIndex) {
		return sectName[(iIndex-1)];
	}

	public void setSectName(String[] sectName) {
		this.sectName = sectName;
	}

	public String getSectShortName(int iIndex) {
		return sectShortName[iIndex-1];
	}

	public void setSectShortNameL(String[] sectShortName) {
		this.sectShortName = sectShortName;
	}

	public int getSectSeq() {
		return sectSeq;
	}

	public void setSectSeq(int sectSeq) {
		this.sectSeq = sectSeq;
	}

	public String getSectStatus() {
		return sectStatus;
	}

	public void setSectStatus(String sectStatus) {
		this.sectStatus = sectStatus;
	}
}
