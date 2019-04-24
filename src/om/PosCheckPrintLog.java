//Database: pos_check_print_logs - Sales check printing log
package om;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;

public class PosCheckPrintLog {
	private String plogId;
	private int oletId;
	private String chksId;
	private String cptyId;
	private String printTime;
	private DateTime printLocTime;
	private int printUserId;
	private int printStatId;
	private BigDecimal printCheckTotal;
	private BigDecimal printPartyTotal;
	private String status;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosCheckPrintLog () {
		this.init();
	}
	
	//init object with passing value
	public PosCheckPrintLog (String sCheckPartyId, int iOutletId, int iUserId, int iStationId, BigDecimal dPrintCheckTotal, BigDecimal dPrintPartyTotal) {
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("HH:mm:ss");
		
		this.init();
		 
		this.cptyId = sCheckPartyId;
		this.oletId = iOutletId;
		this.printLocTime = AppGlobal.getCurrentTime(false);
		this.printTime = oFormatter.print(AppGlobal.convertTimeToUTC(printLocTime));
		this.printUserId = iUserId;
		this.printStatId = iStationId;
		this.printCheckTotal = dPrintCheckTotal;
		this.printPartyTotal = dPrintPartyTotal;
		
	}
	
	//init object from database by plog ID
	public PosCheckPrintLog (String sPlogId) {
		this.init();
		
		this.plogId = sPlogId;
	}
	
	//construct the save request JSON
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("plog_id", this.plogId);
			addSaveJSONObject.put("plog_olet_id", this.oletId);
			if(this.chksId.compareTo("") > 0)
				addSaveJSONObject.put("plog_chks_id", this.chksId);
			if(this.cptyId.compareTo("") > 0)
				addSaveJSONObject.put("plog_cpty_id", this.cptyId);
			
			if(this.printTime != null)
				addSaveJSONObject.put("plog_print_time", this.printTime);
			
			if(this.printLocTime != null)
				addSaveJSONObject.put("plog_print_loctime", this.printLocTime.toString(oFormatter));
			
			if(this.printUserId > 0)
				addSaveJSONObject.put("plog_print_user_id", this.printUserId);
			if(this.printStatId > 0)
				addSaveJSONObject.put("plog_print_stat_id", this.printStatId);
			if(this.printCheckTotal.compareTo(BigDecimal.ZERO) != 0)
				addSaveJSONObject.put("plog_print_check_total", this.printCheckTotal);
			if(this.printPartyTotal.compareTo(BigDecimal.ZERO) != 0)
				addSaveJSONObject.put("plog_print_party_total", this.printPartyTotal);
			if(!this.status.isEmpty())
				addSaveJSONObject.put("plog_status", this.status);

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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("checkPrintLog")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("checkPrintLog")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("checkPrintLog");
			if(tempJSONObject.isNull("PosCheckPrintLog")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject checkPrintLogJSONObject) {
		JSONObject resultCheckPrintLog = null;
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		resultCheckPrintLog = checkPrintLogJSONObject.optJSONObject("PosCheckPrintLog");
		if(resultCheckPrintLog == null)
			resultCheckPrintLog = checkPrintLogJSONObject;
			
		this.init();

		this.plogId = resultCheckPrintLog.optString("plog_id");
		this.oletId = resultCheckPrintLog.optInt("plog_olet_id");
		this.chksId = resultCheckPrintLog.optString("plog_chks_id");
		this.cptyId = resultCheckPrintLog.optString("plog_cpty_id");
		 
		this.printTime = resultCheckPrintLog.optString("plog_print_time", null);
		
		String sPrintLocTime = resultCheckPrintLog.optString("plog_print_loctime");
		if(!sPrintLocTime.isEmpty())
			this.printLocTime = oFormatter.parseDateTime(sPrintLocTime);
		else
			this.printLocTime = null;

		this.printUserId = resultCheckPrintLog.optInt("plog_print_user_id");
		this.printStatId = resultCheckPrintLog.optInt("plog_print_stat_id");
		this.printCheckTotal = new BigDecimal(resultCheckPrintLog.optString("plog_print_check_total", "0.0"));
		this.printPartyTotal = new BigDecimal(resultCheckPrintLog.optString("plog_print_party_total", "0.0"));
		
		this.status = resultCheckPrintLog.optString("plog_status", PosCheckPrintLog.STATUS_ACTIVE);
	}
	
	// init value
	public void init() {
		this.plogId = "";
		this.oletId = 0;
		this.chksId = "";
		this.cptyId = "";
		this.printTime = null;
		this.printLocTime = null;
		this.printUserId = 0;
		this.printStatId = 0;
		this.printCheckTotal = BigDecimal.ZERO;
		this.printPartyTotal = BigDecimal.ZERO;
		this.status = PosCheckPrintLog.STATUS_ACTIVE;
	}
	
	//read latest record by check id
	public boolean readLatestByCheckId(String sChksId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try{
			requestJSONObject.put("checkId", sChksId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "getLatestCheckPrintLogByCheckId", requestJSONObject.toString());
		
	}
	
	//set olet id
	public void setOutletId(int iOutletId) {
		this.oletId = iOutletId;
	}
	
	//set chks id
	public void setChksId(String sChksId) {
		this.chksId = sChksId;
	}
	
	//set check party id
	public void setCptyId(String sCptyId) {
		this.cptyId = sCptyId;
	}
	
	//set print time
	public void setPrintTime(String sPrintTime) {
		this.printTime = sPrintTime;
	}
	
	//set print local time
	public void setPrintLocTime(DateTime dtPrintLocTime) {
		this.printLocTime = dtPrintLocTime;
	}
	
	//set print user id
	public void setPrintUserId(int iUserId) {
		this.printUserId = iUserId;
	}
	
	//set print station id
	public void setPrintStationId(int iStatId) {
		this.printStatId = iStatId;
	}
	
	//set print check total
	public void setPrintCheckTotal(BigDecimal dPrintCheckTotal) {
		this.printCheckTotal = dPrintCheckTotal;
	}
	
	//set print party total
	public void setPrintPartyTotal(BigDecimal dPrintPartyTotal) {
		this.printPartyTotal = dPrintPartyTotal;
	}
	
	//get plogId
	protected String getPlogId() {
		return this.plogId;
	}

	//get olet id
	protected int getOutletId() {
		return this.oletId;
	}
	
	//get check id
	protected String getChksId() {
		return this.chksId;
	}
	
	//get party id
	protected String getCptyId() {
		return this.cptyId;
	}
	
	//get print time
	protected String getPrintTime() {
		return this.printTime;
	}
	
	//get print local time
	public DateTime getPrintLocTime() {
		return this.printLocTime;
	}
	
	//get print user id
	protected int getPrintUserId() {
		return this.printUserId;
	}
	
	//get print station id
	protected int getPrintStationId() {
		return this.printStatId;
	}
	
	//get print check total
	public BigDecimal getPrintCheckTotal() {
		return this.printCheckTotal;
	}
	
	//get print party total
	public BigDecimal getPrintPartyTotal() {
		return this.printPartyTotal;
	}
}
