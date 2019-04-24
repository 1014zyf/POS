package app;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import om.OmWsClient;
import om.OmWsClientGlobal;
import om.PosInterfaceConfig;

public class FuncSurveillance {
	class PostingInfo {
		int iInterfaceId;
		String sEventType;
		String sOutletCode;
		String sStationCode;
		String sCheckNumber;
		String sEventTime;
		String sItemCode;
		String sItemName;
		String sEmployeeName;
		String sEmployeeNumber;
		BigDecimal dQuantity;
		String sDiscountName;
		BigDecimal dAmount;
		BigDecimal dAmountAdjust;
		String sModifierName;
		String sTaxName;
		String sPaymentMethodCode;
		String sPaymentMethodName;
		JSONArray sMergeCheckNumberFrom;
		String sMergeCheckNumberTo;
		String sSplitCheckNumberFrom;
		JSONArray sSplitCheckNumberTo;
		String sFromCheckNumber;
		String sTargetCheckNumber;
		String sPreviousOutletCode;
		String sCurrentOutletCode;
		String sAuthorizedEmployeeNumber;
		String sResponse;
		String sFunctionName;
	}
	
	private Semaphore m_oDaemonSemaphore;
	private Queue<PostingInfo> m_oPostingInfoQueue;
	
	private PosInterfaceConfig m_oSurveillanceInterfaceInterface;

	private OmWsClient m_omWsClient;
	
	private String m_sLastErrorMessage;
	
	// POS Event Types
	//public static final String SURVEILLANCE_TYPE_REV_CENTER_NAME = "revcentername";
	//public static final String SURVEILLANCE_TYPE_TERMINAL_NAME = "terminalname";
	//public static final String SURVEILLANCE_TYPE_EMPLOYEE_NAME = "employeename";
	
	// Check Action Events
	public static final String SURVEILLANCE_TYPE_NEW_CHECK = "newcheck";
	public static final String SURVEILLANCE_TYPE_SAVE_CHECK = "savecheck";
	public static final String SURVEILLANCE_TYPE_RECALL_CHECK = "recallcheck";
	public static final String SURVEILLANCE_TYPE_SPLIT_CHECK = "splitcheck";
	public static final String SURVEILLANCE_TYPE_COMBINE_CHECKS = "combinechecks";
	// public static final String SURVEILLANCE_TYPE_COMBINEC_CHECKS = "releasecheck";
	// public static final String SURVEILLANCE_TYPE_COMBINEC_CHECKS = "acceptcheck";
	// public static final String SURVEILLANCE_TYPE_COMBINEC_CHECKS = "transfercheck";
	public static final String SURVEILLANCE_TYPE_TRANSFER_ITEM = "transferitem";
	public static final String SURVEILLANCE_TYPE_REOPEN_CHECK = "reopencheck";
	public static final String SURVEILLANCE_TYPE_REPRINT_CHECK = "reprintcheck";
	
	// Check Detail Events
	public static final String SURVEILLANCE_TYPE_ITEMADD = "itemadd";
	public static final String SURVEILLANCE_TYPE_ITEM_OPTION = "itemoption";
	//public static final String SURVEILLANCE_TYPE_ITEM_NOTE = "itemnote";
	public static final String SURVEILLANCE_TYPE_ITEM_CANCEL = "itemcancel";
	public static final String SURVEILLANCE_TYPE_ITEM_VOID = "itemvoid";
	public static final String SURVEILLANCE_TYPE_DISCOUNT_APPLY = "discountapply";
	public static final String SURVEILLANCE_TYPE_DISCOUNT_CANCEL = "discountcancel";
	public static final String SURVEILLANCE_TYPE_DISCOUNT_VOID = "discountvoid";
	//public static final String SURVEILLANCE_TYPE_RETURN_ADD = "returnadd";
	//public static final String SURVEILLANCE_TYPE_RETURN_CANCEL = "returncancel";
	//public static final String SURVEILLANCE_TYPE_RETURN_VOID = "returnvoid";
	public static final String SURVEILLANCE_TYPE_TIP_ADD = "tipadd";
	public static final String SURVEILLANCE_TYPE_TIP_VOID = "tipvoid";
	//public static final String SURVEILLANCE_TYPE_FEE_ADD = "feeadd";
	//public static final String SURVEILLANCE_TYPE_FEE_CANCEL = "feecancel";
	//public static final String SURVEILLANCE_TYPE_FEE_VOID = "feevoid";
	public static final String SURVEILLANCE_TYPE_SUBTOTAL = "subtotal";
	public static final String SURVEILLANCE_TYPE_TAXDUE = "taxdue";
	public static final String SURVEILLANCE_TYPE_TOTALDUE = "totaldue";
	public static final String SURVEILLANCE_TYPE_TENDER_APPLY = "tenderapply";
	//public static final String SURVEILLANCE_TYPE_TENDER_AUTH = "tenderauth";
	public static final String SURVEILLANCE_TYPE_TENDER_ADJUST = "tenderadjust";
	public static final String SURVEILLANCE_TYPE_TENDER_VOID = "tendervoid";
	public static final String SURVEILLANCE_TYPE_CHANGE_DUE = "changedue";
	//public static final String SURVEILLANCE_TYPE_COMP_ISSUE = "compissue";
	//public static final String SURVEILLANCE_TYPE_COMP_REDEEM = "compredeem";
	//public static final String SURVEILLANCE_TYPE_CUSTOMER_DETAIL = "customerdetail";
	//public static final String SURVEILLANCE_TYPE_ROOM_DETAIL = "roomdetail";
	
	// Check Termination Events
	public static final String SURVEILLANCE_TYPE_CLOSE_CHECK = "closecheck";
	public static final String SURVEILLANCE_TYPE_CANCEL_CHECK = "cancelcheck";
	public static final String SURVEILLANCE_TYPE_VOID_CHECK = "voidcheck";
	
	// Employee Events
	public static final String SURVEILLANCE_TYPE_CLOCK_IN = "clockin";
	public static final String SURVEILLANCE_TYPE_CLOCK_OUT = "clockout";
	public static final String SURVEILLANCE_TYPE_SIGN_IN = "signin";
	public static final String SURVEILLANCE_TYPE_SIGN_OUT = "signout";
	//public static final String SURVEILLANCE_TYPE_EMPLOYEE_STATUS = "employeestatus";
	//public static final String SURVEILLANCE_TYPE_EMPLOYEE_REPORT = "employeereport";
	
	// Terminal Events
	public static final String SURVEILLANCE_TYPE_NO_SALE = "nosale";
	public static final String SURVEILLANCE_TYPE_CASH_IN = "cashin";
	public static final String SURVEILLANCE_TYPE_CASH_OUT = "cashout";
	public static final String SURVEILLANCE_TYPE_DRAWER_OPEN = "draweropen";
	//public static final String SURVEILLANCE_TYPE_DRAWER_CLOSE = "drawerclose";
	public static final String SURVEILLANCE_TYPE_REV_CENTER_CHANGE = "revcenterchange";
	//public static final String SURVEILLANCE_TYPE_TERMINAL_UP = "terminalup";
	//public static final String SURVEILLANCE_TYPE_TERMINAL_DOWN = "terminaldown";
	//public static final String SURVEILLANCE_TYPE_DATA_LINK_UP = "datalinkup";
	//public static final String SURVEILLANCE_TYPE_DATA_LINK_DOWN = "datalinkdown";
	//public static final String SURVEILLANCE_TYPE_EMPLOYEE_ERROR = "employeeerror";
	//public static final String SURVEILLANCE_TYPE_SOFTWARE_ERROR = "softwareerror";
	//public static final String SURVEILLANCE_TYPE_HAREWARE_ERROR = "hardwareerror";
	
	// System Events
	public static final String SURVEILLANCE_TYPE_OVERRIDE_REQUEST = "overriderequest";
	public static final String SURVEILLANCE_TYPE_OVERRIDE_RESPONSE = "overrideresponse";
	public static final String SURVEILLANCE_TYPE_MANAGER_PROCEDURE = "managerprocedure";
	//public static final String SURVEILLANCE_TYPE_REPORT_GENERATED = "reportgenerated";
	public static final String SURVEILLANCE_TYPE_END_OF_DAY = "endofday";
	
	public FuncSurveillance(PosInterfaceConfig oSurveillanceInterface) {
		m_oSurveillanceInterfaceInterface = oSurveillanceInterface;
		m_oPostingInfoQueue = new LinkedList<PostingInfo>();
		m_oDaemonSemaphore = new Semaphore(1);
		m_omWsClient = OmWsClientGlobal.g_oWsClient.get();
		
		m_sLastErrorMessage = "";
		
	}
	
	private JSONObject formSurPostingJSONObject(PostingInfo oPostingInfo) {
		JSONObject oPostingJSONObject = new JSONObject();
		
		try {
			oPostingJSONObject.put("interfaceId", oPostingInfo.iInterfaceId);
			oPostingJSONObject.put("eventType", oPostingInfo.sEventType);
			oPostingJSONObject.put("revCenterNum", oPostingInfo.sOutletCode);
			oPostingJSONObject.put("amount", oPostingInfo.dAmount);
			oPostingJSONObject.put("prevRevCenter", oPostingInfo.sPreviousOutletCode);
			oPostingJSONObject.put("newRevCenter", oPostingInfo.sCurrentOutletCode);
			oPostingJSONObject.put("procedureDesc", oPostingInfo.sFunctionName);
			oPostingJSONObject.put("authEmployeeNum", oPostingInfo.sAuthorizedEmployeeNumber);
			oPostingJSONObject.put("response", oPostingInfo.sResponse);
			oPostingJSONObject.put("terminalNum", oPostingInfo.sStationCode);
			oPostingJSONObject.put("employeeNum", oPostingInfo.sEmployeeNumber);
			oPostingJSONObject.put("employeeName", oPostingInfo.sEmployeeName);
			oPostingJSONObject.put("checkNum", oPostingInfo.sCheckNumber);
			oPostingJSONObject.put("eventTime", oPostingInfo.sEventTime);
			oPostingJSONObject.put("itemId", oPostingInfo.sItemCode);
			oPostingJSONObject.put("itemName", oPostingInfo.sItemName);
			oPostingJSONObject.put("qty", oPostingInfo.dQuantity);
			oPostingJSONObject.put("optionName", oPostingInfo.sModifierName);
			oPostingJSONObject.put("discountName", oPostingInfo.sDiscountName);
			oPostingJSONObject.put("taxName", oPostingInfo.sTaxName);
			oPostingJSONObject.put("tenderTypeNum", oPostingInfo.sPaymentMethodCode);
			oPostingJSONObject.put("tenderTypeName", oPostingInfo.sPaymentMethodName);
			oPostingJSONObject.put("sourceCheckNums",oPostingInfo.sMergeCheckNumberFrom);
			oPostingJSONObject.put("combinedCheckNum",oPostingInfo.sMergeCheckNumberTo);
			oPostingJSONObject.put("amountAdjusted", oPostingInfo.dAmountAdjust);
			oPostingJSONObject.put("sourceCheckNum",oPostingInfo.sSplitCheckNumberFrom);
			oPostingJSONObject.put("splitCheckNums",oPostingInfo.sSplitCheckNumberTo);
			oPostingJSONObject.put("fromCheckNum",oPostingInfo.sFromCheckNumber);
			oPostingJSONObject.put("toCheckNum",oPostingInfo.sTargetCheckNumber);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		return oPostingJSONObject;
	}
	
	private void initPostingInfo(PostingInfo oPostingInfo) {
		DateTime today = AppGlobal.getCurrentTime(true);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		String sCurrentTime = dateFormat.print(today);
		oPostingInfo.iInterfaceId = 0;
		oPostingInfo.sOutletCode = "";
		oPostingInfo.sStationCode = "";
		oPostingInfo.sCheckNumber = "";
		oPostingInfo.sEventType = "";
		oPostingInfo.sEventTime = sCurrentTime;
		oPostingInfo.sItemCode = "";
		oPostingInfo.sItemName = "";
		oPostingInfo.sEmployeeNumber = "";
		oPostingInfo.sEmployeeName = "";
		oPostingInfo.dQuantity = BigDecimal.ZERO;
		oPostingInfo.sDiscountName = "";
		oPostingInfo.dAmount = BigDecimal.ZERO;
		oPostingInfo.sTaxName = "";
		oPostingInfo.sPaymentMethodCode = "";
		oPostingInfo.sPaymentMethodName = "";
		oPostingInfo.sModifierName = "";
		oPostingInfo.dAmountAdjust= BigDecimal.ZERO;
		oPostingInfo.sMergeCheckNumberFrom = new JSONArray();
		oPostingInfo.sMergeCheckNumberTo = "";
		oPostingInfo.sSplitCheckNumberFrom = "";
		oPostingInfo.sSplitCheckNumberTo = new JSONArray();
		oPostingInfo.sFromCheckNumber = "";
		oPostingInfo.sTargetCheckNumber = "";
		oPostingInfo.sPreviousOutletCode = "";
		oPostingInfo.sCurrentOutletCode = "";
		oPostingInfo.sAuthorizedEmployeeNumber = "";
		oPostingInfo.sFunctionName = "";
		oPostingInfo.sResponse = "";
	}
	
	public void surveillanceEvent(HashMap<String, String> oSurvInfoHashMap, HashMap<String, JSONArray> oSurvInfoJSONHashMap) {
		PostingInfo oPostingInfo = new PostingInfo();
		initPostingInfo(oPostingInfo);
		oPostingInfo.iInterfaceId = m_oSurveillanceInterfaceInterface.getInterfaceId();
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.sStationCode = AppGlobal.g_oFuncStation.get().getCode(); 
		if(oSurvInfoHashMap.containsKey("employeeNum"))
			oPostingInfo.sEmployeeNumber = oSurvInfoHashMap.get("employeeNum");
		else	
			oPostingInfo.sEmployeeNumber = AppGlobal.g_oFuncUser.get().getUserNumber();
		if(oSurvInfoHashMap.containsKey("eventType"))
			oPostingInfo.sEventType = oSurvInfoHashMap.get("eventType");
		if(oSurvInfoHashMap.containsKey("checkNo"))
			oPostingInfo.sCheckNumber = oSurvInfoHashMap.get("checkNo");
		
		if(oSurvInfoHashMap.containsKey("itemCode"))
			oPostingInfo.sItemCode = oSurvInfoHashMap.get("itemCode").substring(0, oSurvInfoHashMap.get("itemCode").length() <= 36 ? oSurvInfoHashMap.get("itemCode").length() : 36);
		if(oSurvInfoHashMap.containsKey("itemName"))
			oPostingInfo.sItemName = oSurvInfoHashMap.get("itemName").substring(0, oSurvInfoHashMap.get("itemName").length() <= 36 ? oSurvInfoHashMap.get("itemName").length() : 36);
		if (oSurvInfoHashMap.containsKey("employeeName"))
			oPostingInfo.sEmployeeName = oSurvInfoHashMap.get("employeeName");
		if (oSurvInfoHashMap.containsKey("qty"))
			oPostingInfo.dQuantity = new BigDecimal(oSurvInfoHashMap.get("qty"));
		if (oSurvInfoHashMap.containsKey("discountName"))
			oPostingInfo.sDiscountName = oSurvInfoHashMap.get("discountName");
		if (oSurvInfoHashMap.containsKey("amount"))
			oPostingInfo.dAmount = new BigDecimal(oSurvInfoHashMap.get("amount"));
		if (oSurvInfoHashMap.containsKey("amountAdjusted"))
			oPostingInfo.dAmountAdjust = new BigDecimal(oSurvInfoHashMap.get("amountAdjusted"));
		if (oSurvInfoHashMap.containsKey("taxName"))
			oPostingInfo.sTaxName = oSurvInfoHashMap.get("taxName");
		if(oSurvInfoHashMap.containsKey("optionName"))
			oPostingInfo.sModifierName = oSurvInfoHashMap.get("optionName");
		if(oSurvInfoHashMap.containsKey("tenderTypeName"))
			oPostingInfo.sPaymentMethodCode = oSurvInfoHashMap.get("tenderTypeNum");
		if(oSurvInfoHashMap.containsKey("tenderTypeName"))
			oPostingInfo.sPaymentMethodName = oSurvInfoHashMap.get("tenderTypeName");
		if(oSurvInfoHashMap.containsKey("newRevCenter"))
			oPostingInfo.sCurrentOutletCode = oSurvInfoHashMap.get("newRevCenter");
		if(oSurvInfoHashMap.containsKey("prevRevCenter"))
			oPostingInfo.sPreviousOutletCode = oSurvInfoHashMap.get("prevRevCenter");
		if(oSurvInfoHashMap.containsKey("authEmployeeNum"))
			oPostingInfo.sAuthorizedEmployeeNumber = oSurvInfoHashMap.get("authEmployeeNum");
		if(oSurvInfoHashMap.containsKey("response"))
			oPostingInfo.sResponse = oSurvInfoHashMap.get("response");
		if(oSurvInfoHashMap.containsKey("procedureDesc"))
			oPostingInfo.sFunctionName = oSurvInfoHashMap.get("procedureDesc");
		if(oSurvInfoHashMap.containsKey("checkNum"))
			oPostingInfo.sCheckNumber = oSurvInfoHashMap.get("checkNum");
		if(oSurvInfoJSONHashMap != null && oSurvInfoJSONHashMap.containsKey("sourceCheckNums"))
			oPostingInfo.sMergeCheckNumberFrom = oSurvInfoJSONHashMap.get("sourceCheckNums");
		if(oSurvInfoHashMap.containsKey("combinedCheckNum"))
			oPostingInfo.sMergeCheckNumberTo = oSurvInfoHashMap.get("combinedCheckNum");
		if(oSurvInfoHashMap.containsKey("sourceCheckNum"))
			oPostingInfo.sSplitCheckNumberFrom = oSurvInfoHashMap.get("sourceCheckNum");
		if(oSurvInfoJSONHashMap != null && oSurvInfoJSONHashMap.containsKey("splitCheckNums"))
			oPostingInfo.sSplitCheckNumberTo = oSurvInfoJSONHashMap.get("splitCheckNums");
		if(oSurvInfoHashMap.containsKey("fromCheckNum"))
			oPostingInfo.sFromCheckNumber = oSurvInfoHashMap.get("fromCheckNum");
		if(oSurvInfoHashMap.containsKey("toCheckNum"))
			oPostingInfo.sTargetCheckNumber = oSurvInfoHashMap.get("toCheckNum");
		
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
						m_oSurveillanceInterfaceInterface.doSurveillancePosting(formSurPostingJSONObject(oPostingInfo));
					}
				} finally {
					m_oDaemonSemaphore.release();
				}
			}
		});
		oThread.start();
	}
	
	/*
	protected static void  funcSurveilSetup(HashMap<String, String> oSurvInfoHashMap) {
		PosInterfaceConfig oSurveillanceInterface = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_SURVEILLANCE_INTERFACE).get(0);
		FuncSurveillance oFuncSurveillance = AppGlobal.getSurveillanceInfoFromList( AppGlobal.g_oFuncStation.get().getStationId(),oSurveillanceInterface);
		oFuncSurveillance.surveillanceEvent(oSurvInfoHashMap);
	}
	*/
	
	protected void addPosting(PostingInfo oPostingInfo) {
		synchronized (m_oPostingInfoQueue) {
			m_oPostingInfoQueue.add(oPostingInfo);
		}
		
		triggerDaemon();
	}
	
	public boolean stopSurveillanceShell(int iInterfaceId, String sVendorKey) {
		// For normal operation or service master of smart station
		return m_oSurveillanceInterfaceInterface.stopSurveillanceShell(iInterfaceId, sVendorKey);
	}
	
	public boolean restartSurveillanceShell(int iInterfaceId, String sVendorKey) {
		// For normal operation or service master of smart station
		boolean bResult = m_oSurveillanceInterfaceInterface.restartSurveillanceShell(iInterfaceId, sVendorKey);
		if(!bResult) {
			if(m_oSurveillanceInterfaceInterface.getLastErrorMessage().equals("shell_is_not_alive"))
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_open_shell");
			else if(m_oSurveillanceInterfaceInterface.getLastErrorCode() > 0)
				m_sLastErrorMessage = getErrorMessage(m_oSurveillanceInterfaceInterface.getLastErrorCode());
			else 
				m_sLastErrorMessage = m_oSurveillanceInterfaceInterface.getLastErrorMessage();
		}
		
		return bResult;
	}

	public String getLastErrorMessage() {
		return this.m_sLastErrorMessage;
	}
	
	private String getErrorMessage(int iErrorCode) {
		String sErrorMessage = "";
		
		switch (iErrorCode) {
		case 1:
			sErrorMessage = AppGlobal.g_oLang.get()._("no_such_interface");
			break;
		case 2:
			sErrorMessage = AppGlobal.g_oLang.get()._("missing_interface_setup");
			break;
		case 3:
			sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_build_connection");
			break;
		case 4:
			sErrorMessage = AppGlobal.g_oLang.get()._("no_response");
			break;
		case 5:
			sErrorMessage = AppGlobal.g_oLang.get()._("empty_response");
			break;
		case 6:
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_response");
			break;
		case 7:
			sErrorMessage = AppGlobal.g_oLang.get()._("missing_setup");
			break;
		case 8:
			sErrorMessage = AppGlobal.g_oLang.get()._("interface_shell_has_not_been_built");
			break;
		case 9:
			sErrorMessage = AppGlobal.g_oLang.get()._("shell_is_not_alive");
			break;
		default:	
		case 0:
			sErrorMessage = null;
			break;
		}
		
		return sErrorMessage;
	}
	
}

