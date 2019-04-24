//Database: pos_display_styles - Display styles of the visual elements
package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosDisplayStyle {
	private int dstyId;
	private int sdevId;
	private String type;
	private String[] name;
	private String[] shortName;
	private String text;
	private int bgMediId;
	private int iconMediId;
	private String status;
	
	private MedMedia backgroundMedia;
	private MedMedia iconMedia;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosDisplayStyle () {
		this.init();
	}
	
	//init object from database by dsty_id
	public PosDisplayStyle (int iDstyId) {
		this.init();
		
		this.dstyId = iDstyId;
	}
	
	//init obejct with JSONObject
	public PosDisplayStyle(JSONObject displayStyleJSONObject) {
		this.readDataFromJson(displayStyleJSONObject);
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("display_style")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("display_style")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("display_style");
			if(tempJSONObject.isNull("PosDisplayStyle")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray displayStyleJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("display_styles")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("display_styles")) 
				return null;
			
			displayStyleJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("display_styles");
		}
		
		return displayStyleJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject displayStyleJSONObject) {
		JSONObject resultDisplayStyle = null;
		int i;
		
		resultDisplayStyle = displayStyleJSONObject.optJSONObject("PosDisplayStyle");
		if(resultDisplayStyle == null)
			resultDisplayStyle = displayStyleJSONObject;
		
		this.init();
		
		this.dstyId = resultDisplayStyle.optInt("dsty_id");
		this.sdevId = resultDisplayStyle.optInt("dsty_sdev_id");
		this.type = resultDisplayStyle.optString("dsty_type");
		for(i=1; i<=5; i++) {
			this.name[(i-1)] = resultDisplayStyle.optString("dsty_name_l"+i);
		}
		for(i=1; i<=5; i++) {
			this.shortName[(i-1)] = resultDisplayStyle.optString("dsty_short_name_l"+i);
		}
		this.text = resultDisplayStyle.optString("dsty_text", null);
		this.bgMediId = resultDisplayStyle.optInt("dsty_bg_medi_id");
		this.iconMediId = resultDisplayStyle.optInt("dsty_icon_medi_id");
		this.status = resultDisplayStyle.optString("dsty_status", PosDisplayStyle.STATUS_ACTIVE);
		
		if (resultDisplayStyle.has("BackgroundMedia"))
			this.backgroundMedia = new MedMedia(resultDisplayStyle.optJSONObject("BackgroundMedia"));
		
		if (resultDisplayStyle.has("IconMedia"))
			this.iconMedia = new MedMedia(resultDisplayStyle.optJSONObject("IconMedia"));
	}

	public JSONArray getDisplayStyleListByStationDevice(int iSdevId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		 
		try {
			requestJSONObject.put("stationDeviceId", iSdevId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getDisplayStyleListBySdevId", requestJSONObject.toString());
		
		return responseJSONArray;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.dstyId = 0;
		this.sdevId = 0;
		this.type = "";
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.text = null;
		this.bgMediId = 0;
		this.iconMediId = 0;
		this.status = PosDisplayStyle.STATUS_ACTIVE;
		
		this.backgroundMedia = null;
		this.iconMedia = null;
	}
	
	//read data from database by dsty_id
	public void readById (int iDstyId) {
		this.dstyId = iDstyId;
	}
	
	//add new display style to database
	public boolean add() {
		return true;
	}
	
	//update display style to database
	public boolean update() {
		return true;
	}
	
	//get dstyId
	public int getDstyId() {
		return this.dstyId;
	}
	
	//get sdev id
	protected int getSdevId() {
		return this.sdevId;
	}
	
	//get type
	protected String getType() {
		return this.type;
	}
	
	//get name by lang index
	protected String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get short name by lang index
	protected String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get text
	public String getText() {
		return this.text;
	}
	
	//get background medi id
	public int getBgMediId() {
		return this.bgMediId;
	}
	
	//get icon medi id
	public int getIconMediId() {
		return this.iconMediId;
	}
	
	//get background medi url
	public String getBackgroundMediUrl() {
		return (backgroundMedia == null)? "": this.backgroundMedia.getUrl();
	}
	
	//get icon medi url
	public String getIconMediUrl() {
		return (iconMedia == null)? "": this.iconMedia.getUrl();
	}
	
	//get status
	protected String getStatus() {
		return this.status;
	}
}
