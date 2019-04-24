package app;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import om.OmWsClient;
import om.OmWsClientGlobal;
import om.PosInterfaceConfig;

public class FuncTMS {
	class PostingInfo {
		int iInterfaceId;
		String sInterfaceVendorKey;
		String sOutletId;
		String sOutletCode;
		String sWorkStation;
		String sEmployee;
		String sCustomerId;
		String sCheckNumber;
		String sTableSection;
		String sTable;
		int iCover;
		String sServingPeriod;
		String sDate;
		String sTime;
		String sBusinessDate;
		String sTableStatus;
		JSONObject oData;
		String sCheckId;
		List<Integer> oCourseIdList;
		JSONObject oCheckInformationJSONObject;
	}
	
	private Semaphore m_oDaemonSemaphore;
	private Queue<PostingInfo> m_oPostingInfoQueue;
	
	private PosInterfaceConfig m_oTmsInterface;

	private JSONObject m_oDataJSONObject;
	private String m_sCheckId;
	private OmWsClient m_omWsClient;
	private ArrayList<String> m_oMarkedDeliveryItemIds;
	
	public static final String TABLE_STATUS_SEND_CHECK = "sendCheck";
	public static final String TABLE_STATUS_ORDER_ITEM = "orderItem";
	public static final String TABLE_STATUS_PRINT_CHECK = "printCheck";
	public static final String TABLE_STATUS_PAY_CHECK = "payCheck";
	public static final String TABLE_STATUS_SET_COURSE = "setCourse";
	public static final String TABLE_STATUS_SET_VACANT = "setVacant";
	public static final String TABLE_STATUS_DELAYED_VACANT = "delayedVacate";
	public static final String TABLE_STATUS_CHANGE_TABLE = "changeTable";
	public static final String TABLE_STATUS_DELETE_CHECK = "deleteCheck";
	public static final String TABLE_STATUS_PUT_CHECK = "putCheck";
	
	public FuncTMS(PosInterfaceConfig oTmsInterface) {
		m_oTmsInterface = oTmsInterface;
		m_oPostingInfoQueue = new LinkedList<PostingInfo>();
		m_oDaemonSemaphore = new Semaphore(1);
		m_oDataJSONObject = new JSONObject();
		m_sCheckId = "";
		m_omWsClient = OmWsClientGlobal.g_oWsClient.get();
		m_oMarkedDeliveryItemIds = new ArrayList<String>();
	}
	
	private JSONObject formPmsPostingJSONObject(PostingInfo oPostingInfo) {
		JSONObject oPostingJSONObject = new JSONObject();
		
		try {
			oPostingJSONObject.put("interfaceId", oPostingInfo.iInterfaceId);
			oPostingJSONObject.put("interfaceVendorKey", oPostingInfo.sInterfaceVendorKey);
			oPostingJSONObject.put("outletId", oPostingInfo.sOutletId);
			oPostingJSONObject.put("outletCode", oPostingInfo.sOutletCode);
			oPostingJSONObject.put("workstation", oPostingInfo.sWorkStation);
			oPostingJSONObject.put("employee", oPostingInfo.sEmployee);
			oPostingJSONObject.put("customerId", oPostingInfo.sCustomerId);
			oPostingJSONObject.put("data", oPostingInfo.oData);
			oPostingJSONObject.put("checkNumber", oPostingInfo.sCheckNumber);
			oPostingJSONObject.put("tableSection", oPostingInfo.sTableSection);
			oPostingJSONObject.put("table", oPostingInfo.sTable);
			oPostingJSONObject.put("cover", oPostingInfo.iCover);
			oPostingJSONObject.put("servingPeriod", oPostingInfo.sServingPeriod);
			oPostingJSONObject.put("businessDate", oPostingInfo.sBusinessDate);
			oPostingJSONObject.put("date", oPostingInfo.sDate);
			oPostingJSONObject.put("time", oPostingInfo.sTime);
			oPostingJSONObject.put("tableStatus", oPostingInfo.sTableStatus);
			oPostingJSONObject.put("checkId", oPostingInfo.sCheckId);
			oPostingJSONObject.put("courseIds", oPostingInfo.oCourseIdList);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		
		return oPostingJSONObject;
	}
	
	private void initPostingInfo(PostingInfo oPostingInfo) {
		oPostingInfo.iInterfaceId = 0;
		oPostingInfo.sInterfaceVendorKey = "";
		oPostingInfo.sOutletId = "";
		oPostingInfo.sOutletCode = "";
		oPostingInfo.sWorkStation = "";
		oPostingInfo.sEmployee = "";
		oPostingInfo.sCustomerId = "";
		oPostingInfo.sCheckNumber = "";
		oPostingInfo.sTableSection = "";
		oPostingInfo.sTable = "";
		oPostingInfo.iCover = 0;
		oPostingInfo.sServingPeriod = "";
		oPostingInfo.sDate = "";
		oPostingInfo.sTime = "";
		oPostingInfo.sBusinessDate = "";
		oPostingInfo.sTableStatus = "";
		oPostingInfo.oData = new JSONObject();
		oPostingInfo.sCheckId = "";
		oPostingInfo.oCourseIdList = new ArrayList<Integer>();
	}
	
	public void tmsOpenCheck(FuncCheck oFuncCheck) {
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm:ss");
		String sCurrentTime = timeFmt.print(today);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String sCurrentDate = dateFmt.print(today);
		
		PostingInfo oPostingInfo = new PostingInfo();
		initPostingInfo(oPostingInfo);
		oPostingInfo.iInterfaceId = m_oTmsInterface.getInterfaceId();
		oPostingInfo.sInterfaceVendorKey = m_oTmsInterface.getInterfaceVendorKey();
		oPostingInfo.sOutletId = Integer.toString(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.sWorkStation = AppGlobal.g_oFuncStation.get().getCode(); 
		oPostingInfo.sEmployee = AppGlobal.g_oFuncUser.get().getUserNumber();
		oPostingInfo.sCustomerId = "";
		oPostingInfo.oData = new JSONObject();
		oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
		oPostingInfo.iCover = oFuncCheck.getCover();
		oPostingInfo.sCheckId = oFuncCheck.getCheckId();
		if(oFuncCheck.getCheckBusinessPeriodId().compareTo("") == 0)
			oPostingInfo.sServingPeriod = AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getCode();
		else
			oPostingInfo.sServingPeriod = AppGlobal.g_oFuncOutlet.get().getBusinessPeriodById(oFuncCheck.getCheckBusinessPeriodId()).getCode();
		oPostingInfo.sBusinessDate = AppGlobal.g_oFuncOutlet.get().getFormat1BusinessDayInString();
		oPostingInfo.sDate = sCurrentDate;
		oPostingInfo.sTime = sCurrentTime;
		
		String sTableSection = "";
		if(oFuncCheck.getSectId() != 0){
			if(!AppGlobal.g_oFuncOutlet.get().getOutTableSectionList().getSectionsList().get(oFuncCheck.getSectId()).getSectCode().isEmpty())
				sTableSection = AppGlobal.g_oFuncOutlet.get().getOutTableSectionList().getSectionsList().get(oFuncCheck.getSectId()).getSectCode();
		}
		oPostingInfo.sTableSection = sTableSection;
		oPostingInfo.sTable = oFuncCheck.getTableNoWithExtensionForDisplay();
		
		oPostingInfo.sTableStatus = FuncTMS.TABLE_STATUS_SEND_CHECK;
		
		m_sCheckId = oFuncCheck.getCheckId();
		addPosting(oPostingInfo);
	}
	
	public void tmsChangeStatus(String sStatus, List<Integer> oItemCourseIdList) {
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm:ss");
		String sCurrentTime = timeFmt.print(today);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String sCurrentDate = dateFmt.print(today);
		
		PostingInfo oPostingInfo = new PostingInfo();
		initPostingInfo(oPostingInfo);
		oPostingInfo.iInterfaceId = m_oTmsInterface.getInterfaceId();
		oPostingInfo.sInterfaceVendorKey = m_oTmsInterface.getInterfaceVendorKey();
		oPostingInfo.sOutletId = Integer.toString(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.oData = new JSONObject();
		if (oItemCourseIdList != null)
			oPostingInfo.oCourseIdList = oItemCourseIdList;
		oPostingInfo.sBusinessDate = AppGlobal.g_oFuncOutlet.get().getFormat1BusinessDayInString();
		oPostingInfo.sDate = sCurrentDate;
		oPostingInfo.sTime = sCurrentTime;
		oPostingInfo.sTableStatus = sStatus;
		oPostingInfo.sCheckId = m_sCheckId;
		addPosting(oPostingInfo);
	}
	
	public void tmsChangeTable(FuncCheck oFuncCheck) {
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm:ss");
		String sCurrentTime = timeFmt.print(today);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String sCurrentDate = dateFmt.print(today);
		
		PostingInfo oPostingInfo = new PostingInfo();
		initPostingInfo(oPostingInfo);
		oPostingInfo.iInterfaceId = m_oTmsInterface.getInterfaceId();
		oPostingInfo.sInterfaceVendorKey = m_oTmsInterface.getInterfaceVendorKey();
		oPostingInfo.sOutletId = Integer.toString(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.oData = new JSONObject();
		oPostingInfo.sCheckId = oFuncCheck.getCheckId();
		if(oFuncCheck.getCheckBusinessPeriodId().compareTo("") == 0)
			oPostingInfo.sServingPeriod = AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getCode();
		else
			oPostingInfo.sServingPeriod = AppGlobal.g_oFuncOutlet.get().getBusinessPeriodById(oFuncCheck.getCheckBusinessPeriodId()).getCode();
		oPostingInfo.sDate = sCurrentDate;
		oPostingInfo.sTime = sCurrentTime;
		
		String sTableSection = "";
		if(oFuncCheck.getSectId() != 0){
			if(!AppGlobal.g_oFuncOutlet.get().getOutTableSectionList().getSectionsList().get(oFuncCheck.getSectId()).getSectCode().isEmpty())
				sTableSection = AppGlobal.g_oFuncOutlet.get().getOutTableSectionList().getSectionsList().get(oFuncCheck.getSectId()).getSectCode();
		}
		oPostingInfo.sTableSection = sTableSection;
		oPostingInfo.sTable = oFuncCheck.getTableNoWithExtensionForDisplay();
		
		oPostingInfo.sTableStatus = FuncTMS.TABLE_STATUS_CHANGE_TABLE;
		m_sCheckId = oFuncCheck.getCheckId();
		addPosting(oPostingInfo);
	}
	
	public void tmsDeleteTable() {
		PostingInfo oPostingInfo = new PostingInfo();
		initPostingInfo(oPostingInfo);
		oPostingInfo.iInterfaceId = m_oTmsInterface.getInterfaceId();
		oPostingInfo.sInterfaceVendorKey = m_oTmsInterface.getInterfaceVendorKey();
		oPostingInfo.sOutletId = Integer.toString(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.oData = new JSONObject();
		oPostingInfo.sCheckId = m_sCheckId;
		oPostingInfo.sTableStatus = FuncTMS.TABLE_STATUS_DELETE_CHECK;
		addPosting(oPostingInfo);
	}
	
	public void tmsPutCheck(FuncCheck oFuncCheck) {
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm:ss");
		String sCurrentTime = timeFmt.print(today);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String sCurrentDate = dateFmt.print(today);
		
		PostingInfo oPostingInfo = new PostingInfo();
		initPostingInfo(oPostingInfo);
		oPostingInfo.iInterfaceId = m_oTmsInterface.getInterfaceId();
		oPostingInfo.sInterfaceVendorKey = m_oTmsInterface.getInterfaceVendorKey();
		oPostingInfo.sOutletId = Integer.toString(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.sWorkStation = AppGlobal.g_oFuncStation.get().getCode();
		oPostingInfo.oData = new JSONObject();
		oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
		oPostingInfo.sCheckId = oFuncCheck.getCheckId();
		oPostingInfo.sDate = sCurrentDate;
		oPostingInfo.sTime = sCurrentTime;
		oPostingInfo.sBusinessDate = AppGlobal.g_oFuncOutlet.get().getFormat1BusinessDayInString();
		oPostingInfo.sTableStatus = FuncTMS.TABLE_STATUS_PUT_CHECK;
		oPostingInfo.oCheckInformationJSONObject = oFuncCheck.contrustSvcPostingCheckInformation(null, null, null);
		m_sCheckId = oFuncCheck.getCheckId();
		addPosting(oPostingInfo);
	}
	
	public void tmsDelayedVacate() {
		DateTime today = AppGlobal.getCurrentTime(false);
		today = today.plusMinutes(5);
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm:ss");
		String sCurrentTime = timeFmt.print(today);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String sCurrentDate = dateFmt.print(today);
		
		PostingInfo oPostingInfo = new PostingInfo();
		initPostingInfo(oPostingInfo);
		oPostingInfo.iInterfaceId = m_oTmsInterface.getInterfaceId();
		oPostingInfo.sInterfaceVendorKey = m_oTmsInterface.getInterfaceVendorKey();
		oPostingInfo.sOutletId = Integer.toString(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.oData = new JSONObject();
		oPostingInfo.sDate = sCurrentDate;
		oPostingInfo.sTime = sCurrentTime;
		oPostingInfo.sCheckId = m_sCheckId;
		oPostingInfo.sTableStatus = FuncTMS.TABLE_STATUS_DELAYED_VACANT;
		addPosting(oPostingInfo);
	}
	
	protected void triggerDaemon() {
		if (!m_oDaemonSemaphore.tryAcquire())
			return;
		Thread oThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					PostingInfo oPostingInfo;
					while (true) {
						synchronized (m_oPostingInfoQueue) {
							oPostingInfo = m_oPostingInfoQueue.poll();
						}
						
						if (oPostingInfo == null)
							break;
						
						//	handle request
						OmWsClientGlobal.g_oWsClient.set(m_omWsClient);
						JSONObject oResponseObject = null;
						if (oPostingInfo.sTableStatus.equals(FuncTMS.TABLE_STATUS_SEND_CHECK)) {
							oResponseObject = m_oTmsInterface.doTmsOpenCheck(formPmsPostingJSONObject(oPostingInfo));
							if(oResponseObject != null && oResponseObject.has("data"))
								m_oDataJSONObject = oResponseObject.optJSONObject("data");
						}else if(oPostingInfo.sTableStatus.equals(FuncTMS.TABLE_STATUS_PAY_CHECK)
								||oPostingInfo.sTableStatus.equals(FuncTMS.TABLE_STATUS_ORDER_ITEM)
								||oPostingInfo.sTableStatus.equals(FuncTMS.TABLE_STATUS_PRINT_CHECK)
								||oPostingInfo.sTableStatus.equals(FuncTMS.TABLE_STATUS_SET_COURSE)
								||oPostingInfo.sTableStatus.equals(FuncTMS.TABLE_STATUS_SET_VACANT)){
						
								oPostingInfo.oData = m_oDataJSONObject;
								m_oTmsInterface.doTmsSetStatus(formPmsPostingJSONObject(oPostingInfo));
						}else if(oPostingInfo.sTableStatus.equals(FuncTMS.TABLE_STATUS_CHANGE_TABLE)){
							oPostingInfo.oData = m_oDataJSONObject;
							oResponseObject = m_oTmsInterface.doTmsChangeTable(formPmsPostingJSONObject(oPostingInfo));
						}else if(oPostingInfo.sTableStatus.equals(FuncTMS.TABLE_STATUS_DELETE_CHECK)){
								oPostingInfo.oData = m_oDataJSONObject;
								m_oTmsInterface.doTmsDeleteCheck(formPmsPostingJSONObject(oPostingInfo));
						}else if(oPostingInfo.sTableStatus.equals(FuncTMS.TABLE_STATUS_PUT_CHECK)){
								oPostingInfo.oData = m_oDataJSONObject;
								m_oTmsInterface.doTmsPutCheck(oPostingInfo.oCheckInformationJSONObject, formPmsPostingJSONObject(oPostingInfo));
						}else if(oPostingInfo.sTableStatus.equals(FuncTMS.TABLE_STATUS_DELAYED_VACANT)){
							oPostingInfo.oData = m_oDataJSONObject;
							m_oTmsInterface.doTmsDelayedVacate(formPmsPostingJSONObject(oPostingInfo));
						}
					}
				} finally {
					m_oDaemonSemaphore.release();
				}
			}
		});
		oThread.start();
	}
	
	protected void addPosting(PostingInfo oPostingInfo) {
		synchronized (m_oPostingInfoQueue) {
			m_oPostingInfoQueue.add(oPostingInfo);
		}
		
		triggerDaemon();
	}
	
	public String getCheckId() {
		return this.m_sCheckId;
	}
	
	public void resetCheckId(String sCheckId){
		this.m_sCheckId = sCheckId;
	}
		
	public void resetResponseObject(JSONObject oJsonObject){
		this.m_oDataJSONObject = oJsonObject;
	}
	
	public JSONObject getResponseObject(){
		return m_oDataJSONObject;
	}
	
	public void setMarkedDeliveryItemIds(ArrayList<String> oItemIds){
		m_oMarkedDeliveryItemIds = oItemIds;
	}
	
	public ArrayList<String> getMarkedDeliveryItemIds(){
		return m_oMarkedDeliveryItemIds;
	}
}
