package app.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PrtPrintJob {
	private int pjobId;
	private int targetPrtqId;
	private int actualPrtqId;
	private int actualPdevId;
	private String createTime;
	private String processTime;
	private String filename;
	private int copies;
	private String status;
	
	public static String FILE_TYPE_RECEIPT = "RECEIPT";
	public static String FILE_TYPE_BILL = "BILL";
	public static String FILE_TYPE_OTHER = "OTHER";
	
	// status
	public static String STATUS_JUST_SUBMITTED = "";
	public static String STATUS_GOT_BY_PRINT_SERVICE = "r";
	public static String STATUS_SUBMITTED_TO_PRINTING_SYSTEM = "s";
	public static String STATUS_LOCAL_PRINT_JOB = "l";
	public static String STATUS_PRINTING_IN_PROGRESS = "i";
	public static String STATUS_PRINTED = "p";
	public static String STATUS_CANCELLED = "c";
	public static String STATUS_DETECT_FAILURE = "f";
	public static String STATUS_TIMEOUT = "t";
	public static String STATUS_REDIRECTION_IN_PROGRESS = "x";
	
	public PrtPrintJob () {
		this.init();
	}
	
	//construct the save request JSON
	private JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("pjob_id", this.pjobId);
			addSaveJSONObject.put("pjob_target_prtq_id", this.targetPrtqId);
			addSaveJSONObject.put("pjob_actual_prtq_id", this.actualPrtqId);
			addSaveJSONObject.put("pjob_actual_pdev_id", this.actualPdevId);
			
			if(this.createTime != null)
				addSaveJSONObject.put("pjob_create_time", this.createTime);
			
			if (this.processTime != null)
				addSaveJSONObject.put("pjob_process_time", this.processTime);
			
			addSaveJSONObject.put("pjob_filename", this.filename);
			addSaveJSONObject.put("pjob_copies", this.copies);
			addSaveJSONObject.put("pjob_status", this.status);
			
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("printJob")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("printJob")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("printJob");
			if(tempJSONObject.isNull("PrtPrintJob")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject printJobJSONObject) {
		JSONObject resultPrintJob = null;
		
		resultPrintJob = printJobJSONObject.optJSONObject("MenuItem");
		if(resultPrintJob == null)
			resultPrintJob = printJobJSONObject;
			
		this.init();

		this.pjobId = resultPrintJob.optInt("pjob_id");
		this.targetPrtqId = resultPrintJob.optInt("pjob_target_prtq_id");
		this.actualPrtqId = resultPrintJob.optInt("pjob_actual_prtq_id");
		this.actualPdevId = resultPrintJob.optInt("pjob_actual_pdev_id");
		this.createTime = resultPrintJob.optString("pjob_create_time");
		this.processTime = resultPrintJob.optString("pjob_process_time");
		this.filename = resultPrintJob.optString("pjob_filename");
		this.copies = resultPrintJob.optInt("pjob_copies");
		this.status = resultPrintJob.optString("pjob_status", PrtPrintJob.STATUS_JUST_SUBMITTED);
	}
	
	// init value
	public void init() {
		this.pjobId = 0;
		this.targetPrtqId = 0;
		this.actualPrtqId = 0;
		this.actualPdevId = 0;
		this.createTime = null;
		this.processTime = null;
		this.filename = "";
		this.copies = 0;
		this.status = PrtPrintJob.STATUS_JUST_SUBMITTED;
	}
	
	//read data from database by pjob_id 
	public boolean readById(int iPrintJobId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("id", Integer.toString(iPrintJobId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "printing", "getPrintJobById", requestJSONObject.toString());
	}
	
	//add or update a print job to database
	public boolean addUpdate(int iPrtqId, String sURL, String sMediaType, String sFileType) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("printJobFile", sURL);
			requestJSONObject.put("printQ", iPrtqId);
			requestJSONObject.put("printJobFileMediaType", sMediaType);
			requestJSONObject.put("printJobFileType", sFileType);
			
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "addPrintJob", requestJSONObject.toString(), true))
			return false;
		else
			return true;
	}
	
	//add or update print job with array list
	public boolean addUpdateWithMutlipleRecord(List<PrtPrintJob> alPrintJobs) {
		JSONObject tempPrintJobJSONObject = null;
		JSONArray printJobJSONArray = new JSONArray();
		
		for (int i = 0; i < alPrintJobs.size(); i++) {
			PrtPrintJob oPrintJob = alPrintJobs.get(i);
			if (oPrintJob.getPjobId() == 0)
				tempPrintJobJSONObject = oPrintJob.constructAddSaveJSON(false);
			else
				tempPrintJobJSONObject = oPrintJob.constructAddSaveJSON(true);
			printJobJSONArray.put(tempPrintJobJSONObject);
			
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "printing", "saveMultiPrintJobs", printJobJSONArray.toString(), true))
			return false;
		else
			return true;
	}
	
	//add instant printing command to printing module
	public boolean addInstantPrinterCommandJob(int iBdayId, int iShopId, int iOletId, int iActionUserId, int iActionStationId, int iTargetPrtqId, String sCommand){
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("businessDayId", iBdayId);
			requestJSONObject.put("shopId", iShopId);
			requestJSONObject.put("oletId", iOletId);
			requestJSONObject.put("actionUserId", iActionUserId);
			requestJSONObject.put("actionStationId", iActionStationId);
			requestJSONObject.put("printQ", iTargetPrtqId);
			requestJSONObject.put("printerCommand", sCommand);
			
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "printing", "addInstantPrinterCommandJob", requestJSONObject.toString(), true))
			return false;
		else
			return true;
	}
	
	//get pjob id
	protected int getPjobId() {
		return this.pjobId;
	}
	
	//get target prtq id
	protected int getTargetPrtqId() {
		return this.targetPrtqId;
	}
	
	//get actual prtq id
	protected int getActualPrtqId() {
		return this.actualPrtqId;
	}
	
	//get actual pdev id
	protected int getActualPdevId() {
		return this.actualPdevId;
	}
	
	//get create time
	protected String getCreateTime() {
		return this.createTime;
	}
	
	//get process time
	protected String getProcessTime() {
		return this.processTime;
	}
	
	//get file name
	protected String getFilename() {
		return this.filename;
	}
	
	//get copies
	protected int getCopies() {
		return this.copies;
	}
	
	//get status
	protected String getStatus() {
		return this.status;
	}
	
	//set target prtq id
	public void setTargetPrtqId(int iTargetPrtqId) {
		this.targetPrtqId = iTargetPrtqId;
	}
	
	//set filename
	public void setFilename(String sFilename) {
		this.filename = sFilename;
	}
	
	//set copy
	public void setCopies(int iCopy) {
		this.copies = iCopy;
	}
	
	//set create time
	public void setCreateTime(String sCreateTime) {
		this.createTime = sCreateTime;
	}
	
}
