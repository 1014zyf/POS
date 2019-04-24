package om;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

public class PosTaiwanGuiTran {
	private String twtxId;
	private int shopId;
	private int oletId;
	private String chksId;
	private int twcfId;
	private String type;
	private String prefix;
	private int tguiNum;
	private int txn;
	private String refNum;
	private String carrier;
	private BigDecimal printTotal;
	private BigDecimal vatTotal;
	private int printCount;
	private int newTguiNum;
	private String newPrefix;
	private String printTime;
	private DateTime printLocTime;
	private int printUserId;
	private int printStatId;
	private String voidTime;
	private DateTime voidLocTime;
	private int voidUserId;
	private int voidStatId;
	private String status;

	static public String STATUS_ACTIVE = "";
	static public String STATUS_DELETED = "d";
	
	static public String TYPE_NORMAL = "a";
	static public String TYPE_HAVE_GUI = "b";
	static public String TYPE_SPECIAL = "e";
	static public String TYPE_NO_TAX = "d";
	static public String TYPE_WAIVE_TAX = "c";
	static public String TYPE_CHARITY = "f";
	
	static public String BY_STATION = "byStation";
	static public String BY_OUTLET = "byOutlet";
	
	//init object with initialize value
	public PosTaiwanGuiTran () {
		this.init();
	}
	
	//init object with JSON Object
	public PosTaiwanGuiTran(JSONObject taiwanGuiTranJSONObject) {
		readDataFromJson(taiwanGuiTranJSONObject);
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject taiwanGuiTranJSONObject) {
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		JSONObject resultTaiwanGuiTran = null;
		
		resultTaiwanGuiTran = taiwanGuiTranJSONObject.optJSONObject("PosTaiwanGuiTran");
		if(resultTaiwanGuiTran == null)
			resultTaiwanGuiTran = taiwanGuiTranJSONObject;
		
		this.init();
		this.twtxId = resultTaiwanGuiTran.optString("twtx_id");
		this.shopId = resultTaiwanGuiTran.optInt("twtx_shop_id");
		this.oletId = resultTaiwanGuiTran.optInt("twtx_olet_id");
		this.chksId = resultTaiwanGuiTran.optString("twtx_chks_id");
		this.twcfId = resultTaiwanGuiTran.optInt("twtx_twcf_id");
		this.type = resultTaiwanGuiTran.optString("twtx_type");
		this.prefix = resultTaiwanGuiTran.optString("twtx_prefix");
		this.tguiNum = resultTaiwanGuiTran.optInt("twtx_num");
		this.txn = resultTaiwanGuiTran.optInt("twtx_txn");
		this.refNum = resultTaiwanGuiTran.optString("twtx_ref_num");
		this.carrier = resultTaiwanGuiTran.optString("twtx_carrier");
		this.printTotal = new BigDecimal(resultTaiwanGuiTran.optString("twtx_print_total", "0.0"));
		this.vatTotal = new BigDecimal(resultTaiwanGuiTran.optString("twtx_vat_total", "0.0"));
		this.printCount = resultTaiwanGuiTran.optInt("twtx_print_count");
		this.newPrefix = resultTaiwanGuiTran.optString("twtx_new_prefix");
		this.newTguiNum = resultTaiwanGuiTran.optInt("twtx_new_num");
		
		this.printTime = resultTaiwanGuiTran.optString("twtx_print_time", null);
		String sPrintLocTime = resultTaiwanGuiTran.optString("twtx_print_loctime");
		if(!sPrintLocTime.isEmpty())
			this.printLocTime = oFormatter.parseDateTime(sPrintLocTime);
		
		this.printUserId = resultTaiwanGuiTran.optInt("twtx_print_user_id");
		this.printStatId = resultTaiwanGuiTran.optInt("twtx_print_stat_id");

		this.voidTime = resultTaiwanGuiTran.optString("twtx_void_time", null);
		
		String sVoidLocTime = resultTaiwanGuiTran.optString("twtx_void_loctime");
		if(!sVoidLocTime.isEmpty())
			this.voidLocTime = oFormatter.parseDateTime(sVoidLocTime);
		
		this.voidUserId = resultTaiwanGuiTran.optInt("twtx_void_user_id");
		this.voidStatId = resultTaiwanGuiTran.optInt("twtx_void_stat_id");
		this.status = resultTaiwanGuiTran.optString("twtx_status");
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

			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("taiwanGuiTran")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("taiwanGuiTran")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("taiwanGuiTran");
			if(tempJSONObject.isNull("PosTaiwanGuiTran")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//construct the save request JSON
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("twtx_id", this.twtxId);
			addSaveJSONObject.put("twtx_shop_id", this.shopId);
			addSaveJSONObject.put("twtx_olet_id", this.oletId);
			addSaveJSONObject.put("twtx_chks_id", this.chksId);
			addSaveJSONObject.put("twtx_twcf_id", this.twcfId);
			addSaveJSONObject.put("twtx_type", this.type);
			addSaveJSONObject.put("twtx_prefix", this.prefix);
			addSaveJSONObject.put("twtx_num", this.tguiNum);
			addSaveJSONObject.put("twtx_txn", this.txn);
			addSaveJSONObject.put("twtx_ref_num", this.refNum);
			addSaveJSONObject.put("twtx_carrier", this.carrier);
			addSaveJSONObject.put("twtx_print_total", this.printTotal);
			addSaveJSONObject.put("twtx_vat_total", this.vatTotal);
			addSaveJSONObject.put("twtx_print_count", this.printCount);
			addSaveJSONObject.put("twtx_new_prefix", this.newPrefix);
			addSaveJSONObject.put("twtx_new_num", this.newTguiNum);

			if (this.printLocTime != null) {
				addSaveJSONObject.put("twtx_print_time", this.printTime);
				addSaveJSONObject.put("twtx_print_loctime", dateTimeToString(this.printLocTime));
			}
			addSaveJSONObject.put("twtx_print_user_id", this.printUserId);
			addSaveJSONObject.put("twtx_print_stat_id", this.printStatId);
			
			if (this.voidLocTime != null) {
				addSaveJSONObject.put("twtx_void_time", this.voidTime);
				addSaveJSONObject.put("twtx_void_loctime", dateTimeToString(this.voidLocTime));
			}
			if(this.voidUserId > 0)
				addSaveJSONObject.put("twtx_void_user_id", this.voidUserId);
			if(this.voidStatId > 0)
				addSaveJSONObject.put("twtx_void_stat_id", this.voidStatId);

			addSaveJSONObject.put("twtx_status", this.status);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}

		return addSaveJSONObject;
	}
	
	//save (add or update) record to database
	// Return value =	error message
	//					Empty - no error
	public String addUpdate(boolean bUpdate, int iTargetTransNum, String sChkId) {
		JSONObject requestJSONObject = new JSONObject();
		
		requestJSONObject = constructAddSaveJSON(bUpdate);
		try {
			requestJSONObject.put("skipTransNum", iTargetTransNum);
			requestJSONObject.put("checkId", sChkId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//send data to server
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveTaiwanGuiTran", requestJSONObject.toString(), false))
			return OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
		else {
			if (this.twtxId.compareTo("") == 0) {
				if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("id"))
					return "internal_error";
				this.twtxId = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("id");
				this.tguiNum = OmWsClientGlobal.g_oWsClient.get().getResponse().optInt("tguiNum");
				this.prefix = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("prefix");
			}
				
			return "";
		}

	}
	
	//get last taiwan gui record by config id and by
	public boolean getLastTaiwanGuiTranByConfigAndBy(int iConfigId, int iStatId, int iOutletId, String sBy) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("configId", iConfigId);
			if(sBy.equals(PosTaiwanGuiTran.BY_STATION))
				requestJSONObject.put("stationId", iStatId);
			else
				requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("by", sBy);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "getLastTaiwanGuiTran", requestJSONObject.toString());
	}
		
	// init value
	public void init() {
		this.twtxId = "";
		this.chksId = "";
		this.shopId = 0;
		this.oletId = 0;
		this.twcfId = 0;
		this.type = "";
		this.prefix = "";
		this.tguiNum = 0;
		this.txn = 0;
		this.refNum = "";
		this.carrier = "";
		this.printTotal = BigDecimal.ZERO;
		this.vatTotal = BigDecimal.ZERO;
		this.printCount = 0;
		this.newPrefix = "";
		this.newTguiNum = 0;
		this.printLocTime = null;
		this.printUserId = 0;
		this.printStatId = 0;
		this.voidTime = null;
		this.voidLocTime = null;
		this.voidUserId = 0;
		this.voidStatId = 0;
		this.status = PosTaiwanGuiTran.STATUS_ACTIVE;
	}

	//change DateTime object to string for database insertion/update
	private String dateTimeToString (DateTime oDateTime) {
		if (oDateTime == null)
			return null;
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		String timeString = oFmt.print(oDateTime);
		
		return timeString;
	}

	public void setShopId(int iShopId) {
		this.shopId = iShopId;
	}
	
	public void setOletId(int iOletId) {
		this.oletId = iOletId;
	}
	
	public void setCheckId(String sCheckId) {
		this.chksId = sCheckId;
	}
	
	public void setTwcfId(int iTwcfId) {
		this.twcfId = iTwcfId;
	}
	
	public void setType(String sType) {
		this.type = sType;
	}
	
	public void setPrefix(String sPrefix) {
		this.prefix = sPrefix;
	}
	
	public void setTguiNum(int iTguiNum) {
		this.tguiNum = iTguiNum;
	}
	
	public void setRefNum(String sRefNum) {
		this.refNum = sRefNum;
	}

	public void setCarrier(String sCarrier) {
		this.carrier = sCarrier;
	}
	
	public void setPrintTotal(BigDecimal dPrintTotal) {
		this.printTotal = dPrintTotal;
	}
	
	public void setVatTotal(BigDecimal dVatTotal) {
		this.vatTotal = dVatTotal;
	}
	
	public void setPrintCount(int iPrintCount) {
		this.printCount = iPrintCount;
	}

	//set print time
	public void setPrintTime(String sPrintTime) {
		this.printTime = sPrintTime;
	}
	
	//set print local time
	public void setPrintLocTime(DateTime oPrintLocTime) {
		this.printLocTime = oPrintLocTime;
	}
	
	//set print user id
	public void setPrintUserId(int iUserId) {
		this.printUserId = iUserId;
	}
	
	//set print station id
	public void setPrintStatId(int iStationId) {
		this.printStatId = iStationId;
	}

	//set void time
	public void setVoidTime(String sVoidTime) {
		this.voidTime = sVoidTime;
	}
	
	//set void local time
	public void setVoidLocTime(DateTime oVoidLocTime) {
		this.voidLocTime = oVoidLocTime;
	}
	
	//set void user id
	public void setVoidUserId(int iVoidUserId) {
		this.voidUserId = iVoidUserId;
	}
	
	//set void stat id
	public void setVoidStatId(int iVoidStatId) {
		this.voidStatId = iVoidStatId;
	}
	
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}

	//get twtxId
	public String getTwtxId() {
		return this.twtxId;
	}
	
	protected String getChksId() {
		return this.chksId;
	}

	public int getTwcfId() {
		return this.twcfId;
	}
	
	public String getType() {
		return this.type;
	}

	public String getPrefix() {
		return this.prefix;
	}
	
	public int getTguiNum() {
		return this.tguiNum;
	}
	
	public String getRefNum() {
		return this.refNum;
	}
	
	public String getCarrier() {
		return this.carrier;
	}
	
	public BigDecimal getPrintTotal() {
		return this.printTotal;
	}
	
	public BigDecimal getVatTotal() {
		return this.vatTotal;
	}
	
	public int getPrintCount() {
		return this.printCount;
	}
	
	public String getPrintTime() {
		return this.printTime;
	}
	
	public DateTime getPrintLocTime() {
		return this.printLocTime;
	}
	
	public int getPrintUserId() {
		return this.printUserId;
	}
	
	public int getPrintStatId() {
		return this.printStatId;
	}
	
	public DateTime getVoidLocTime() {
		return this.voidLocTime;
	}
	
	public String getStatus() {
		return this.status;
	}
}
