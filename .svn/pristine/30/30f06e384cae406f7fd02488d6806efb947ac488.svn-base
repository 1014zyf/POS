
//Database: pos_station_devices - Device of the workstation
package app.model;

import org.json.JSONException;
import org.json.JSONObject;

public class PosStationDevice {
	private int sdevId;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String key;
	private int width;
	private int height;
	private String status;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	public static String STATUS_SUSPENDED = "s";
	
	// key
	public static String KEY_ANDROID_TABLET = "android_tablet";
	public static String KEY_ANDROID_MOBILE = "android_mobile";
	public static String KEY_AUTO_STATION = "auto_station";
	public static String KEY_THIRD_PARTY_STATION = "third_party_station";
	
	//init object with initialize value
	public PosStationDevice () {
		this.init();
	}
	
	//Init object from database by sdev_id
	public PosStationDevice (int iSdevId) {
		this.init();
		
		this.sdevId = iSdevId;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.sdevId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = ""; 
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.key = "";
		this.width = 0;
		this.height = 0;
		this.status = PosStationDevice.STATUS_ACTIVE;
	}
	
	//read data from database by sdev_id
	public boolean readById(int iSdevId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("id", Integer.toString(iSdevId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataFromApi("gm", "pos", "getStationDeviceById", requestJSONObject.toString());
	}
	
	//add new station device record to database
	public boolean add() {
		return true;
	}
	
	//update station device record to database
	public boolean update() {
		return true;
	}
	
	//get sdevId
	public int getSdevId() {
		return this.sdevId;
	}
	
	//get name by lang index
	protected String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get shortName by lang index
	protected String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get seq
	protected int getSeq() {
		return this.seq;
	}
	
	//get key
	public String getKey() {
		return this.key;
	}
	
	//get width
	protected int getWidth() {
		return this.width;
	}
	
	//get height
	protected int getHeight() {
		return this.height;
	}
	
	//get status
	protected String getStatus() {
		return this.status;
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("stationDevice")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("stationDevice")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("stationDevice");
			if(tempJSONObject.isNull("PosStationDevice")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject statDevJSONObject) {
		JSONObject resultStatDev = null;
		int i;
		
		resultStatDev = statDevJSONObject.optJSONObject("PosStationDevice");
		if(resultStatDev == null)
			resultStatDev = statDevJSONObject;
			
		this.init();
		this.sdevId = resultStatDev.optInt("sdev_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultStatDev.optString("sdev_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultStatDev.optString("sdev_short_name_l"+i);
		this.seq = resultStatDev.optInt("sdev_seq");
		this.key = resultStatDev.optString("sdev_key");
		this.width = resultStatDev.optInt("sdev_width");
		this.height = resultStatDev.optInt("sdev_height");
		this.status = resultStatDev.optString("sdev_status", PosStation.STATUS_ACTIVE);
	}
}
