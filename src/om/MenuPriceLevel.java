package om;

import org.json.JSONArray;
import org.json.JSONObject;

public class MenuPriceLevel {
	private int iprlId;
	private String[] name;
	private String[] shortName;
	private int level;
	private String status;
	
	//Status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	public MenuPriceLevel() {
		this.iprlId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(int i = 0; i < 5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(int i = 0; i < 5; i++)
			this.shortName[i] = "";
		this.level = 0;
		this.status = MenuPriceLevel.STATUS_ACTIVE;
	}
	
	public MenuPriceLevel(JSONObject priceLevelJSONObject) {
		this();
		this.readDataFromJson(priceLevelJSONObject);
	}
	
	//Get Table Message
	public JSONArray readAll() {
		JSONArray priceLevelJSONArray = null;
		priceLevelJSONArray = this.readDataListFromApi("gm", "menu", "getAllPriceLevels", "");
		return priceLevelJSONArray;
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String wsInterface, String module, String fcnName, String param) {
		JSONArray tableMessageJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(wsInterface, module, fcnName, param, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("price_levels")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("price_levels"))
				return null;
			
			tableMessageJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("price_levels");
		}
		return tableMessageJSONArray;
	}
	
	public void readDataFromJson(JSONObject tableMessageJSONObject) {
		int i = 0;
		JSONObject tempJSONObject = null;

		tempJSONObject = tableMessageJSONObject.optJSONObject("MenuPriceLevel");
		if(tempJSONObject == null)
			tempJSONObject = tableMessageJSONObject;

		this.iprlId = tempJSONObject.optInt("iprl_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = tempJSONObject.optString("iprl_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = tempJSONObject.optString("iprl_short_name_l"+i);
		this.level = tempJSONObject.optInt("iprl_level");
		this.status = tempJSONObject.optString("iprl_status", MenuPriceLevel.STATUS_ACTIVE);
	}
	
	//Get iprlId
	public int getIprlId() {
		return this.iprlId;
	}
	
	//Set iprlId
	public void setIprlId(int iIprlId) {
		this.iprlId = iIprlId;
	}
	
	//Get name by lang index
	public String getName(int iIndex) {
		return this.name[(iIndex - 1)];
	}

	//Get short name by lang index
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex - 1)];
	}
	
	//Get price level
	public int getLevel() {
		return this.level;
	}
	
	//Set price level
	public void setLevel(int iLevel) {
		this.level = iLevel;
	}
	
	//Get status
	protected String getStatus() {
		return this.status;
	}
}