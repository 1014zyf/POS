//Database: preorder_stations - Workstations list
package app.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PreorderStation {
	private int statId;
	private String[] name;
	private String[] shortName;
	private String type;
    private String publicUse;
    private String address;
    private String askLogin;
    private String orderLocalPrint;
    private int orderPrtqId;
    private int[] orderPfmtId;
    private int oletId;
    private int themId;
    private int lang;
    private String[] promotionMsg;
    private DateTime downloadLastAttemptTime;
    private DateTime downloadLastSuccessTime;
    private String status;
	
    // type
    public static String TYPE_KIOSK = "";
    public static String TYPE_WEB = "w";
    public static String TYPE_IPAD = "i";
    public static String TYPE_ANDROID = "a";
    
    // public use
    public static String PUBLIC_USE_NON_PUBLIC = "";
    public static String PUBLIC_USE_PUBLIC = "y";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_SUSPENDED = "s";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PreorderStation () {
		this.init();
	}

	//construct the save request JSON
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		int i=0;
		JSONObject addSaveJSONObject = new JSONObject();
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("stat_id", this.statId);
			for(i=1; i<=5; i++) {
				if(!this.name[i-1].isEmpty())
					addSaveJSONObject.put("stat_name_l"+i, this.name[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(!this.shortName[i-1].isEmpty())
					addSaveJSONObject.put("stat_short_name_l"+i, this.shortName[i-1]);
			}
            addSaveJSONObject.put("stat_public", this.publicUse);
            addSaveJSONObject.put("stat_address", this.address);
            addSaveJSONObject.put("stat_ask_login", this.askLogin);
            addSaveJSONObject.put("stat_order_local_print", this.orderLocalPrint);
            if(this.orderPrtqId > 0)
                addSaveJSONObject.put("stat_order_prtq_id", this.orderPrtqId);
            for(i=1; i<=5; i++) {
				if(this.orderPfmtId[i-1] > 0)
					addSaveJSONObject.put("stat_order"+i+"_pfmt_id", this.orderPfmtId[(i-1)]);
			}
            addSaveJSONObject.put("stat_olet_id", this.oletId);
            addSaveJSONObject.put("stat_them_id", this.themId);
            addSaveJSONObject.put("stat_lang", this.lang);
            for(i=1; i<=5; i++) {
                if(!this.promotionMsg[i-1].isEmpty())
                    addSaveJSONObject.put("stat_promotion_msg_l"+i, this.promotionMsg[(i-1)]);
            }
            addSaveJSONObject.put("stat_download_last_attempt_time", this.downloadLastAttemptTime.toString(oFormatter));
            addSaveJSONObject.put("stat_download_last_success_time", this.downloadLastSuccessTime.toString(oFormatter));
			addSaveJSONObject.put("stat_status", this.status);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}

	//read data from Preorder API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray stationJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("stations")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("stations")) 
				return null;
			
			stationJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("stations");
		}
		
		return stationJSONArray;
	}
	
	//get station address lists from database by type
	public JSONArray getStationListByType(String sType) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("type", sType);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "preorder", "getStationListByType", requestJSONObject.toString());
		
		return responseJSONArray;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.statId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.type = "";
        this.publicUse = "";
		this.address = "";
        this.askLogin = "";
		this.orderLocalPrint = "";
        this.orderPrtqId = 0;
        if(this.orderPfmtId == null)
            this.orderPfmtId = new int[5];
        for(i=0; i<5; i++)
            this.orderPfmtId[i] = 0;
        this.oletId = 0;
        this.themId = 0;
        this.lang = 0;
        if(this.promotionMsg == null)
            this.promotionMsg = new String[5];
        for(i=0; i<5; i++)
            this.promotionMsg[i] = "";
        this.downloadLastAttemptTime = null;
        this.downloadLastSuccessTime = null;
		this.status = PreorderStation.STATUS_ACTIVE;
	}
	
	//get statId
	public int getStatId() {
		return this.statId;
	}
	
	//get name by lang index
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get short name by lang index
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get type
    public String getType() {
        return this.type;
    }
    
    //get public use
    public String getPublicUse() {
        return this.publicUse;
    }
    
	//get address
	public String getAddress() {
		return this.address;
	}
    
    //get ask login
    public String getAskLogin() {
        return this.askLogin;
    }
	
	//get order local print
    public String getOrderLocalPrint() {
        return this.orderLocalPrint;
    }
	
	//get order print queue id
	public int getOrderPrtqId() {
		return this.orderPrtqId;
	}
	
	//get order pfmt id by index
	public int getOrderPfmtId(int iIndex) {
		return this.orderPfmtId[(iIndex-1)];
	}
	
	//get olet id
	public int getOletId() {
		return this.oletId;
	}
	
	//get them id
	public int getThemId() {
		return this.themId;
	}
	
	//get lang
	protected int getLang() {
		return this.lang;
	}
    
    //get promotion msg by index
    public String getPromotionMsg(int index) {
        return this.promotionMsg[(index-1)];
    }
	
	//get status
	protected String getStatus() {
		return this.status;
	}
}
