//Database: pos_stations - Workstations list
package om;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosStation {
	private int statId;
	private String code;
	private String[] name;
	private String[] shortName;
	private int stgpId;
	private int sdevId;
	private int failoverStatId;
	private String address;
	private int stationPrinter1PrtqId;
	private int stationPrinter2PrtqId;
	private int checkPrtqId;
	private int receiptPrtqId;
	private int reportPrtqId;
	private int reportSlipPrtqId;
	private int[] checkPfmtId;
	private int[] detailCheckPfmtId;
	private int servingCheckPfmtId;
	private int[] receiptPfmtId;
	private int oletId;
	private int dpanId;
	private String orderingMode;
	private int lang;
	private String autoSignOut;
	private String checkPrefix;
	private JSONObject params;
	private int startCheckNum;
	private int endCheckNum;
	private String status;
	
	private PosPrintFormatList printFormatList;
	
	private PosStationDevice stationDevice;
	
	// orderingMode
	private static String ORDERING_MODE_FINE_DINING = "";
	private static String ORDERING_MODE_FAST_FOOD = "f";
	private static String ORDERING_MODE_DELIVERY = "d";
	private static String ORDERING_MODE_SELF_ORDER_KIOSK = "k";
	private static String ORDERING_MODE_BAR_TAB = "b";
	
	// autoSignOut
	public static String AUTO_SIGN_OUT_HOLD_USER_ID_SWITCH = "";
	public static String AUTO_SIGN_OUT_SWITCH = "y";
	public static String AUTO_SIGN_OUT_HOLD_USER_ID_NOT_SWITCH = "h";
	public static String AUTO_SIGN_OUT_NOT_SWITCH = "o";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_SUSPENDED = "s";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosStation () {
		this.init();
	}
	
	//init object from database by stat_id
	public PosStation(int iStatId) {
		this.readById(iStatId);
	}
	
	//init object from database by stat_address
	public PosStation(String sAddress) {
		JSONObject requestJSONObject = new JSONObject();

		this.init();
		
		try {
			requestJSONObject.put("address", sAddress);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		this.readDataFromApi("gm", "pos", "getStationByAddress", requestJSONObject.toString());
	}
	
	public PosStation(JSONObject oStationJSONObject) {
		readDataFromJson(oStationJSONObject);
	}

	//construct the save request JSON
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		int i=0;
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("stat_id", this.statId);
			addSaveJSONObject.put("stat_code", this.code);
			for(i=1; i<=5; i++) {
				if(!this.name[i-1].isEmpty())
					addSaveJSONObject.put("stat_name_l"+i, this.name[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(!this.shortName[i-1].isEmpty())
					addSaveJSONObject.put("stat_short_name_l"+i, this.shortName[i-1]);
			}
			if(this.stgpId > 0)
				addSaveJSONObject.put("stat_stgp_id", this.stgpId);
			if(this.sdevId > 0)
				addSaveJSONObject.put("stat_sdev_id", this.sdevId);
			addSaveJSONObject.put("stat_address", this.address);
			if(this.stationPrinter1PrtqId > 0)
				addSaveJSONObject.put("stat_station_printer1_prtq_id", this.stationPrinter1PrtqId);
			if(this.stationPrinter2PrtqId > 0)
				addSaveJSONObject.put("stat_station_printer2_prtq_id", this.stationPrinter2PrtqId);
			if(this.checkPrtqId > 0)
				addSaveJSONObject.put("stat_check_prtq_id", this.checkPrtqId);
			if(this.receiptPrtqId > 0)
				addSaveJSONObject.put("stat_receipt_prtq_id", this.receiptPrtqId);
			if(this.reportPrtqId > 0)
				addSaveJSONObject.put("stat_report_prtq_id", this.reportPrtqId);
			if(this.reportSlipPrtqId > 0)
				addSaveJSONObject.put("stat_report_slip_prtq_id", this.reportSlipPrtqId);
			for(i=1; i<=5; i++) {
				if(this.checkPfmtId[i-1] > 0)
					addSaveJSONObject.put("stat_check"+i+"_pfmt_id", this.checkPfmtId[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(this.detailCheckPfmtId[i-1] > 0)
					addSaveJSONObject.put("stat_detail_check"+i+"_pfmt_id", this.detailCheckPfmtId[(i-1)]);
			}
			if(this.servingCheckPfmtId > 0)
				addSaveJSONObject.put("stat_serving_check_pfmt_id", this.servingCheckPfmtId);
			for(i=1; i<=5; i++) {
				if(this.receiptPfmtId[i-1] > 0)
					addSaveJSONObject.put("stat_receipt"+i+"_pfmt_id", this.receiptPfmtId[(i-1)]);
			}
			addSaveJSONObject.put("stat_olet_id", this.oletId);
			addSaveJSONObject.put("stat_dpan_id", this.dpanId);
			addSaveJSONObject.put("stat_ordering_mode", this.orderingMode);
			addSaveJSONObject.put("stat_lang", this.lang);
			addSaveJSONObject.put("stat_auto_sign_out", this.autoSignOut);
			if(!this.checkPrefix.isEmpty())
				addSaveJSONObject.put("stat_check_prefix", this.checkPrefix);
			if(this.failoverStatId > 0)
				addSaveJSONObject.put("stat_failover_stat_id", this.failoverStatId);
			if(this.params != null)
				addSaveJSONObject.put("stat_params", this.params.toString());
			if(this.startCheckNum > 0)
				addSaveJSONObject.put("stat_start_check_num", this.startCheckNum);
			if(this.endCheckNum > 0)
				addSaveJSONObject.put("stat_end_check_num", this.endCheckNum);
			addSaveJSONObject.put("stat_status", this.status);
			
			if(this.printFormatList != null) {
			}
			
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("station")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("station")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("station");
			if(tempJSONObject.isNull("PosStation")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray stationJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			try {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
					return null;
				
				if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("stations")) {
					OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
					return null;
				}
				
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("stations")) 
					return null;
				
				stationJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().getJSONArray("stations");
			}catch(JSONException jsone) {
				try {
					if (!OmWsClientGlobal.g_oWsClient.get().getResponse().getBoolean("stations"))
						this.init();
					
				}catch (JSONException jsone2) {
					jsone.printStackTrace();
					jsone2.printStackTrace();
				}
			}
			
		}
		
		return stationJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject stationJSONObject) {
		JSONObject resultStation = null;
		int i;
		
		resultStation = stationJSONObject.optJSONObject("PosStation");
		if(resultStation == null)
			resultStation = stationJSONObject;
			
		this.init();
		this.statId = resultStation.optInt("stat_id");
		this.code = resultStation.optString("stat_code", "");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultStation.optString("stat_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultStation.optString("stat_short_name_l"+i);
		this.stgpId = resultStation.optInt("stat_stgp_id");
		this.sdevId = resultStation.optInt("stat_sdev_id");
		this.address = resultStation.optString("stat_address");
		this.stationPrinter1PrtqId = resultStation.optInt("stat_station_printer1_prtq_id");
		this.stationPrinter2PrtqId = resultStation.optInt("stat_station_printer2_prtq_id");
		this.checkPrtqId = resultStation.optInt("stat_check_prtq_id");
		this.receiptPrtqId = resultStation.optInt("stat_receipt_prtq_id");
		this.reportPrtqId = resultStation.optInt("stat_report_prtq_id");
		this.reportSlipPrtqId = resultStation.optInt("stat_report_slip_prtq_id");
		for(i=1; i<=5; i++)
			this.checkPfmtId[(i-1)] = resultStation.optInt("stat_check"+i+"_pfmt_id");
		for(i=1; i<=5; i++)
			this.detailCheckPfmtId[(i-1)] = resultStation.optInt("stat_detail_check"+i+"_pfmt_id");
		this.servingCheckPfmtId = resultStation.optInt("stat_serving_check_pfmt_id");
		for(i=1; i<=5; i++)
			this.receiptPfmtId[(i-1)] = resultStation.optInt("stat_receipt"+i+"_pfmt_id");
		this.oletId = resultStation.optInt("stat_olet_id");
		this.dpanId = resultStation.optInt("stat_dpan_id");
		this.orderingMode = resultStation.optString("stat_ordering_mode", PosStation.ORDERING_MODE_FINE_DINING);
		this.lang = resultStation.optInt("stat_lang");
		this.autoSignOut = resultStation.optString("stat_auto_sign_out", PosStation.AUTO_SIGN_OUT_HOLD_USER_ID_SWITCH);
		this.checkPrefix = resultStation.optString("stat_check_prefix");
		this.failoverStatId = resultStation.optInt("stat_failover_stat_id");
		try {
			this.params = new JSONObject(resultStation.optString("stat_params"));
		} catch (JSONException e) {
			this.params = null;
		}
		this.startCheckNum = resultStation.optInt("stat_start_check_num");
		this.endCheckNum = resultStation.optInt("stat_end_check_num");
		this.status = resultStation.optString("stat_status", PosStation.STATUS_ACTIVE);
		
		JSONArray oPrintFormatJSONArray = stationJSONObject.optJSONArray("print_format");
		if(oPrintFormatJSONArray != null)
			this.printFormatList.addMultiPrintFormatToList(oPrintFormatJSONArray);
		
		if (stationJSONObject.has("station_device") && !stationJSONObject.isNull("station_device")) 
			stationDevice = new PosStationDevice(stationJSONObject.optJSONObject("station_device"));
	}
	
	//add or update check discount with HashMap
	public boolean addUpdateWithMutlipleRecord(HashMap<Integer, PosStation> oStationList) {
		JSONObject tempCheckDiscJSONObject = null;
		JSONArray stationJSONArray = new JSONArray();
		
		for(PosStation oStation:oStationList.values()) {
			
			if(oStation.getStatId() == 0)
				tempCheckDiscJSONObject = oStation.constructAddSaveJSON(false);
			else
				tempCheckDiscJSONObject = oStation.constructAddSaveJSON(true);
			stationJSONArray.put(tempCheckDiscJSONObject);
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveMultiStations", stationJSONArray.toString(), false))
			return false;
		else
			return true;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.statId = 0;
		this.code = "";
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.stgpId = 0;
		this.sdevId = 0;
		this.address = "";
		this.stationPrinter1PrtqId = 0;
		this.stationPrinter2PrtqId = 0;
		this.checkPrtqId = 0;
		this.receiptPrtqId = 0;
		this.reportPrtqId = 0;
		this.reportSlipPrtqId = 0;
		if(this.checkPfmtId == null)
			this.checkPfmtId = new int[5];
		for(i=0; i<5; i++)
			this.checkPfmtId[i] = 0;
		if(this.detailCheckPfmtId == null)
			this.detailCheckPfmtId = new int[5];
		for(i=0; i<5; i++)
			this.detailCheckPfmtId[i] = 0;
		this.servingCheckPfmtId = 0;
		if(this.receiptPfmtId == null)
			this.receiptPfmtId = new int[5];
		for(i=0; i<5; i++)
			this.receiptPfmtId[i] = 0;
		this.oletId = 0;
		this.dpanId = 0;
		this.orderingMode = PosStation.ORDERING_MODE_FINE_DINING;
		this.lang = 0;
		this.autoSignOut = PosStation.AUTO_SIGN_OUT_HOLD_USER_ID_SWITCH;
		this.checkPrefix = "";
		this.failoverStatId = 0;
		this.params = null;
		this.startCheckNum = 0;
		this.endCheckNum = 0;
		this.status = PosStation.STATUS_ACTIVE;
		
		if(this.printFormatList == null)
			this.printFormatList = new PosPrintFormatList();
		else
			this.printFormatList.clearPrintFormatList();
		stationDevice = null;
	}
	
	//read data from database by stat_id
	public boolean readById(int iStatId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("id", Integer.toString(iStatId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataFromApi("gm", "pos", "getStationById", requestJSONObject.toString());
		
	}
	
	//read data from database by stat_address
	public boolean readByAddress(String sAddress, boolean bAllowAutoAssign) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("address", sAddress);
			requestJSONObject.put("allowAutoAssign", bAllowAutoAssign);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataFromApi("gm", "pos", "getStationByAddress", requestJSONObject.toString());
		
	}
	
	//get station lists by outlet id and device key
	public JSONArray getStationByOutletIdDeviceKey(int iOletId, String sDeviceKey) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("outletId", Integer.toString(iOletId));
			requestJSONObject.put("deviceKey", sDeviceKey);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getStationByOutletIdDeviceKey", requestJSONObject.toString());
		
		return responseJSONArray;
		
	}
	
	//get station lists by outlet id and device key
	public JSONArray getStationByDeviceKey(String sDeviceKey) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("deviceKey", sDeviceKey);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getStationByDeviceKey", requestJSONObject.toString());
		
		return responseJSONArray;
		
	}
	
	//get station list by outlet id
	public JSONArray getStationByOutletId(int iOutletId){
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("outletId", iOutletId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getStationListByOutletId", requestJSONObject.toString());
		
		return responseJSONArray;
	}	
	
	//get statId
	public int getStatId() {
		return this.statId;
	}
	
	//get station code
	public String getCode() {
		return this.code;
	}
	
	//get name by lang index
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get name
	public String[] getName() {
		return this.name;
	}
	
	//get short name by lang index
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get stgp id
	public int getStgpId() {
		return this.stgpId;
	}
	
	//get sdev id
	public int getSdevId() {
		return this.sdevId;
	}
	
	//get address
	public String getAddress() {
		return this.address;
	}
	
	//get station printer 1 prtq id
	protected int getStationPrinter1PrtqId() {
		return this.stationPrinter1PrtqId;
	}
	
	//get station printer 2 prtq id
	protected int getStationPrinter2PrtqId() {
		return this.stationPrinter2PrtqId;
	}
	
	//get check print queue id
	public int getCheckPrtqId() {
		return this.checkPrtqId;
	}
	
	//get receipt prtq id
	public int getReceiptPrtqId() {
		return this.receiptPrtqId;
	}
	
	//get report prtq id
	public int getReportPrtqId() {
		return this.reportPrtqId;
	}
	
	//get report prtq id
	public int getReportSlipPrtqId() {
		return this.reportSlipPrtqId;
	}
	
	//get check pfmt id
	public int getCheckPfmtId(int iIndex) {
		return this.checkPfmtId[(iIndex-1)];
	}
		
	//get detail check pfmt id
	public int getDetailCheckPfmtId(int iIndex) {
		return this.detailCheckPfmtId[(iIndex-1)];
	}
	
	//get serving check pfmt id
	public int getServingCheckPfmtId() {
		return this.servingCheckPfmtId;
	}
	
	//get receipt pfmt id
	public int getReceiptPfmtId(int iIndex) {
		return this.receiptPfmtId[(iIndex-1)];
	}
	
	//get olet id
	public int getOletId() {
		return this.oletId;
	}
	
	//get dpan id
	public int getDpanId() {
		return this.dpanId;
	}
	
	//get ordering mode
	public String getOrderingMode() {
		return this.orderingMode;
	}
	
	//get lang
	protected int getLang() {
		return this.lang;
	}
	
	//get auto sign out
	public String getAutoSignOut() {
		return this.autoSignOut;
	}
	
	//get check prefix
	public String getCheckPrefix() {
		return this.checkPrefix;
	}
	
	public int getFailoverStatId() {
		return this.failoverStatId;
	}
	
	//get params
	public JSONObject getParams() {
		return this.params;
	}
	
	//get start check number
	public int getStartCheckNumber() {
		return this.startCheckNum;
	}
	
	//get end check number
	public int getEndCheckNumber() {
		return this.endCheckNum;
	}
	
	//get status
	protected String getStatus() {
		return this.status;
	}
	
	//get print format list
	public String getPrintFormatName(int iPfmtId, int iIndex) {
		PosPrintFormat oPrintFormat = this.printFormatList.getPrintFormatByPfmtId(iPfmtId);
		
		if(oPrintFormat == null)
			return null;
		return oPrintFormat.getName(iIndex);
	}
	
	 public PosStationDevice getStationDevice() {
		 return stationDevice;
	 }
	 
	//set auto sign out
	public void setAutoSignOut(String sAutoSignOut) {
		this.autoSignOut = sAutoSignOut;
	}
	
	public boolean isFastFoodOrderingMode() {
		return this.orderingMode.equals(PosStation.ORDERING_MODE_FAST_FOOD);
	}
	
	public boolean isSelfOrderKioskOrderingMode() {
		return this.orderingMode.equals(PosStation.ORDERING_MODE_SELF_ORDER_KIOSK);
	}

	public boolean isBarTabOrderingMode() {
		return this.orderingMode.equals(PosStation.ORDERING_MODE_BAR_TAB);
	}
}
