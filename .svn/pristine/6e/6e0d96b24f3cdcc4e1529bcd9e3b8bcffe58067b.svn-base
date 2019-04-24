package externaldevice;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;
import app.AppThreadManager;
import app.FormMain;
import app.FrameSearchMemberFunction;
import app.FuncActionLog;
import app.FuncCheck;
import app.FuncCheckItem;
import app.FuncCheckListener;
import app.FuncCheckParty;
import app.FuncMenu;
import app.FuncMenuItem;
import app.FuncMessageQueue;
import app.FuncMixAndMatch;
import app.FuncOutlet;
import app.FuncPMS;
import app.FuncPayment;
import app.FuncStation;
import app.FuncUser;
import app.FuncVGS;
import externallib.StringLib;
import lang.LangResource;
import om.InfInterface;
import om.InfVendor;
import om.MemMember;
import om.MemMemberList;
import om.MenuItem;
import om.MenuItemCourse;
import om.MenuItemCourseList;
import om.MenuMenu;
import om.MenuSetMenuLookup;
import om.MenuSetMenuLookupList;
import om.OmWsClient;
import om.OmWsClientGlobal;
import om.OutCurrency;
import om.OutSpecialHour;
import om.PosActionLog;
import om.PosAttributeType;
import om.PosAttributeTypeList;
import om.PosBusinessDay;
import om.PosCheck;
import om.PosCheckAttribute;
import om.PosCheckDiscount;
import om.PosCheckDiscountItem;
import om.PosCheckExtraInfo;
import om.PosCheckExtraInfoList;
import om.PosCheckItem;
import om.PosCheckItemList;
import om.PosCheckParty;
import om.PosCheckPayment;
import om.PosCheckTable;
import om.PosConfig;
import om.PosCustomType;
import om.PosCustomTypeList;
import om.PosDiscountType;
import om.PosDiscountTypeList;
import om.PosFunction;
import om.PosFunctionList;
import om.PosInterfaceConfig;
import om.PosInterfaceConfigList;
import om.PosOutletItem;
import om.PosOutletItemList;
import om.PosOverrideCondition;
import om.PosPaymentMethod;
import om.PosPaymentMethodList;
import om.PosStation;
import om.PosVoidReason;
import om.UserUser;

public class DeviceThreadMain implements FuncCheckListener {
	// *********************************
	// For internal use, stored the processing send check
	private HashMap<String, DateTime> m_oProcessingSendChecks;
	// *********************************
	
	// Function list
	private PosFunctionList m_oFunctionList;
	private FuncCheck m_oFuncCheck;
	private FuncPayment m_oFuncPayment;
	
	private FuncStation m_oFuncStation;
	private FuncUser m_oFuncUser;
	private FuncOutlet m_oFuncOutlet;
	private int m_iLangIndex;
	private OmWsClient m_omWsClient;
	private HashMap<String, HashMap<String, PosConfig>> m_oPosConfigList;
	private FuncMenu m_oFuncMenu;
	private FuncMixAndMatch m_oFuncMixAndMatch;
	private FuncActionLog m_oActionLog;
	private PosInterfaceConfigList m_oPosInterfaceConfigList;
	private LangResource m_oLangResource;
	private Integer m_iCurrentLangIndex;
	private String m_sResultForAutoFunction;
	
	private int m_iAppliedUserId;
	private String m_sOriFuncKey;
	private String m_sTargetCheckNumber;
	private String m_sTargetTableNumber;
	
	// Operation mode
	// 0 : Fine dining mode
	// 1 : Fast food mode
	// 2 : Stock delivery mode
	private AppGlobal.OPERATION_MODE m_eOperationMode = AppGlobal.OPERATION_MODE.fine_dining;
	
	// *********************************
	// For internal use, stored set menu
	private HashMap<Integer, MenuSetMenuLookupList> m_oStoredSetMenuLookupList;
	private ArrayList<FuncCheckItem> m_oStoredFuncCheckItemList;
	
	// Item Course List
	private MenuItemCourseList m_oMenuItemCourseList;
	
	private String m_sLastErrorCode;
	private String m_sLastErrorMessage;
	
	private HashMap<String, Object> m_oResponsePacket;
	
	private Semaphore m_oDaemonSemaphore;
	private Queue<JSONObject> m_oMessageJSONobjectQueue;
	
//	private String m_sLogin;
//	private String m_sPassword;
//	private String m_sPath;
//	private String m_sNode;
//	private boolean m_bSupportHttps;
	
	private FuncMessageQueue m_oFuncMessageQueue;
	
	public static String FUNC_RESULT_SUCCESS = "s";
	public static String FUNC_RESULT_CANCEL_BY_USER = "c";
	public static String FUNC_RESULT_FAIL = "f";
	public static String FUNC_RESULT_NO_SUCH_RECORD = "r";
	
	public static String STRING_TRUE = "true";
	
	public DeviceThreadMain(String sCheckNumber, String sTableNumber, FuncMessageQueue oFuncMessageQueue) {
		m_oDaemonSemaphore = new Semaphore(1);
		m_oMessageJSONobjectQueue = new LinkedList<JSONObject>();
		
		//Initial internal usage variable
		m_oProcessingSendChecks = new HashMap<String, DateTime>();
		m_oResponsePacket = new HashMap<String, Object>();
		m_oStoredSetMenuLookupList = new HashMap<Integer, MenuSetMenuLookupList>();
		m_oPosConfigList = AppGlobal.g_oPosConfigList.get();
		m_oFuncMenu = AppGlobal.g_oFuncMenu.get();
		m_oStoredFuncCheckItemList = new ArrayList<FuncCheckItem>();
		m_oFuncMixAndMatch = AppGlobal.g_oFuncMixAndMatch.get();
		m_oActionLog = AppGlobal.g_oActionLog.get();
		m_oPosInterfaceConfigList = AppGlobal.g_oPosInterfaceConfigList.get();
		m_oLangResource = AppGlobal.g_oLang.get();
		m_iCurrentLangIndex = AppGlobal.g_oCurrentLangIndex.get();
		
		m_oFuncOutlet = AppGlobal.g_oFuncOutlet.get();
		m_oFuncStation = AppGlobal.g_oFuncStation.get();
		m_oFuncUser = AppGlobal.g_oFuncUser.get();
		m_omWsClient = OmWsClientGlobal.g_oWsClient.get();
		m_iLangIndex =  AppGlobal.g_oCurrentLangIndex.get();
		m_sResultForAutoFunction = AppGlobal.g_sResultForAutoFunction.get();
		
		m_oFuncMessageQueue = new FuncMessageQueue();
		m_sTargetCheckNumber = sCheckNumber;
		m_sTargetTableNumber = sTableNumber;
		
		m_iAppliedUserId = 0;
		m_sOriFuncKey = "";
		m_sLastErrorCode = "";
		m_sLastErrorMessage = "";
	}
	
	public void init(FuncMessageQueue oMsgQueue, FuncPayment oFuncPayment, PosFunctionList oFunctionList) {
		m_oFuncPayment = oFuncPayment;
		m_oFunctionList = oFunctionList;
		
		// Connect to MQ
		m_oFuncMessageQueue = oMsgQueue;
	}
	
	private boolean fcnSwitch(String sFuncKey, JSONObject oFuncParamJSONObj) {
		PosFunction oPosFunction = null;
		//Prepare reply packet
		HashMap<String, Object> oResultHashMap = new HashMap<String, Object>();
		DateTime oCurrentDateTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMddHHmmss");
		String sTableNo = "", sTableExtension = "", sCheckNo = "", sVerifyKey = "";
		String sDateTime = dateFormat.print(oCurrentDateTime);
		String sClientMacAddress = oFuncParamJSONObj.optString("mac");
		oResultHashMap.put("time", sDateTime);
		oResultHashMap.put("mac", sClientMacAddress);
		m_sOriFuncKey = sFuncKey;

		HashMap<String, String> oTableInfo = this.getTableNoAndTableExtension(oFuncParamJSONObj.optString("tableno"));
		if (oTableInfo.containsKey("tableNo")) {
			if (oTableInfo.get("tableNo").length() > 9) {
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("table_is_not_exist");
				oResultHashMap.put("code", "-32025");
				oResultHashMap.put("message", m_sLastErrorMessage);
				m_oResponsePacket.put("error", oResultHashMap);
				return false;
			}
			if (!oFuncParamJSONObj.optString("tableno").equals(oTableInfo.get("tableNo"))) {
				try {
					oFuncParamJSONObj.put("tableno", oTableInfo.get("tableNo"));
					if (oTableInfo.containsKey("tableExt")
							&& !oFuncParamJSONObj.optString("tableextension").equals(oTableInfo.get("tableExt"))) {
						if (oTableInfo.get("tableExt").length() > 5) {
							m_sLastErrorMessage = AppGlobal.g_oLang.get()._("table_is_not_exist");
							oResultHashMap.put("code", "-32025");
							oResultHashMap.put("message", m_sLastErrorMessage);
							m_oResponsePacket.put("error", oResultHashMap);
							return false;
						}
						oFuncParamJSONObj.put("tableextension", oTableInfo.get("tableExt").toUpperCase());
					}
				} catch (JSONException jsone) {
					AppGlobal.stack2Log(jsone);
				}
			}
		}
		
		try {
			// Special handling for iPad function
			// Skip checking authority for "get_soldout_item_list" and "" function
			if (sFuncKey.equals(DeviceMain.FUNC_LIST.get_soldout_item_list.name()) || sFuncKey.equals(DeviceMain.FUNC_LIST.get_item_count_list.name()) || sFuncKey.equals(DeviceMain.FUNC_LIST.get_check_information.name())  ||  sFuncKey.equals(DeviceMain.FUNC_LIST.get_all_opened_checks.name()) || sFuncKey.equals(DeviceMain.FUNC_LIST.lock_table.name())) {
				oPosFunction = new PosFunction();
				
				//Write application log
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Function: " + sFuncKey);
			} else {
				if(sFuncKey.equals(DeviceMain.FUNC_LIST.apply_discount.name())) {
					sFuncKey = "check_discount";
					if(oFuncParamJSONObj.has("type") && oFuncParamJSONObj.optString("type", "check").equals("item"))
						sFuncKey = "item_discount";
				} else if(sFuncKey.equals(DeviceMain.FUNC_LIST.apply_extra_charge.name()))
					sFuncKey = "check_extra_charge";
				else if(sFuncKey.equals(DeviceMain.FUNC_LIST.send_and_pay.name()))
					sFuncKey = "send_check";
				
				oPosFunction = m_oFunctionList.getFunctionByKey(sFuncKey);
				if(oPosFunction == null) {
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_such_function");
					oResultHashMap.put("code", "-32012");
					oResultHashMap.put("message", m_sLastErrorMessage);
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				
				//Write application log
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Function: " + oPosFunction.getKey());
			}
			
			//Get table number / table extension / check no
			sTableNo = oFuncParamJSONObj.optString("tableno", "");
			sTableExtension = oFuncParamJSONObj.optString("tableextension", "");
			sCheckNo = oFuncParamJSONObj.optString("checkno", "");
			sVerifyKey = oFuncParamJSONObj.optString("verifykey", "");
			
			//Process function
			//Send Check
			if(oPosFunction.getKey().equals(DeviceMain.FUNC_LIST.send_check.name())) {
				int iCover = 1;
				if(oFuncParamJSONObj.has("cover")) {
					if(!oFuncParamJSONObj.optString("cover").isEmpty())
						iCover = Integer.valueOf(oFuncParamJSONObj.optString("cover"));
					else
						iCover = 0;
				}
				
				//Check Applied User Number
				String sAppliedUserNo = "";
				if(oFuncParamJSONObj.has("applieduserno"))
					sAppliedUserNo = oFuncParamJSONObj.optString("applieduserno");
				if(!this.getAppliedUserId(sAppliedUserNo)){
					//Return error, applied user not valid
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", "-32208");
					oResultHashMap.put("message", "Invalid user");
						
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				
				//Open Check first
				m_sLastErrorMessage = openCheck(sTableNo, sTableExtension, sCheckNo , iCover, true, sVerifyKey);
				if(!m_sLastErrorMessage.isEmpty()) {
					//Return error, cannot open check
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				
				boolean bSendSuccess = processOrderCheck(oFuncParamJSONObj, oResultHashMap, sTableNo);
				
				// return if it is fail to send check
				if(!bSendSuccess)
					return false;
				
				//Send Check
				if(m_sOriFuncKey.equals(DeviceMain.FUNC_LIST.send_and_pay.name())) {
					//apply extra charge
					if(oFuncParamJSONObj.has("extracharges")){
						String sDiscountType = oFuncParamJSONObj.optString("type", "check");
						if(!preProcessForApplyDiscount(true, sDiscountType, PosDiscountType.USED_FOR_EXTRA_CHARGE, null, oFuncParamJSONObj.optJSONArray("extracharges"))) {
							oResultHashMap.put("tableno", sTableNo);
							oResultHashMap.put("code", m_sLastErrorCode);
							oResultHashMap.put("message", m_sLastErrorMessage);
							m_oResponsePacket.put("error", oResultHashMap);
							return false;
						}
					}
					
					if(!payCheck(true, oFuncParamJSONObj, sVerifyKey)){
						oResultHashMap.put("time", "");
						oResultHashMap.put("code", m_sLastErrorCode);
						oResultHashMap.put("message", m_sLastErrorMessage);
						oResultHashMap.put("servertime", "");
						oResultHashMap.put("challenge", "");
						m_oResponsePacket.put("error", oResultHashMap);
						return false;
					}else{
						JSONObject oCheckDetail = fromCheckDetailResponse();
						m_oResponsePacket.put("result", oCheckDetail);
					}
					
				}else {
					m_sLastErrorMessage = sendCheck();
					if(!m_sLastErrorMessage.isEmpty()) {
						//Send check fail
						oResultHashMap.put("tableno", sTableNo);
						oResultHashMap.put("code", m_sLastErrorCode);
						oResultHashMap.put("message", m_sLastErrorMessage);
						
						m_oResponsePacket.put("error", oResultHashMap);
						return false;
					}
					else {
						//Send check success
						oResultHashMap.put("tableno", sTableNo+sTableExtension);
						if(sTableNo.equals("0") && !sTableExtension.isEmpty())
							oResultHashMap.put("tableno", sTableExtension);
						if(sTableNo.isEmpty() && sTableExtension.isEmpty())
							oResultHashMap.put("tableno", m_oFuncCheck.getTableNoWithExtensionForDisplay());
						oResultHashMap.put("checkno", m_oFuncCheck.getCheckPrefixNo());
						oResultHashMap.put("checktotal", m_oFuncCheck.getCheckTotal().toPlainString());
						
						oResultHashMap.put("businessdate", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInStringWithFormat("yyyy-MM-dd"));
						
						m_oResponsePacket.put("result", oResultHashMap);
					}
				}
			}
			//Preview check
			else if(oPosFunction.getKey().equals(DeviceMain.FUNC_LIST.guest_check_preview.name())) {
				//Open Check first
				m_sLastErrorMessage = openCheck(sTableNo, sTableExtension, null, 1, true, sVerifyKey);
				if(!m_sLastErrorMessage.equals("")) {
					//No check exists
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				
				JSONObject oPreviewJSONObject = guestCheckPreview(AppGlobal.g_oCurrentLangIndex.get());
				if(oPreviewJSONObject == null || oPreviewJSONObject.optString("url", "").isEmpty()) {
					//Error, no url return
					m_oFuncCheck.unlockTable(false, false);
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				else {
					//Reply with the link
					oResultHashMap.put("checkurl", oPreviewJSONObject.optString("url", ""));
					oResultHashMap.put("viewcontent", oPreviewJSONObject.optString("viewContent", ""));
					m_oResponsePacket.put("result", oResultHashMap);
				}
			}
			//Stock sold out
			else if(sFuncKey.equals(DeviceMain.FUNC_LIST.get_soldout_item_list.name())) {
				ArrayList<Object> oResultArrayList = getOutletItemStockList(false, true);
				if(oResultArrayList != null && oResultArrayList.size() >= 0) {
					//Return success
					oResultHashMap.put("items", oResultArrayList);
					
					m_oResponsePacket.put("result", oResultHashMap);
				}
				else {
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
			}
			//Get item count list
			else if(sFuncKey.equals(DeviceMain.FUNC_LIST.get_item_count_list.name())) {
				ArrayList<Object> oResultArrayList = getOutletItemStockList(true, false);
				if(oResultArrayList.size() > 0) {
					//Return success
					oResultHashMap.put("items", oResultArrayList);
					
					m_oResponsePacket.put("result", oResultHashMap);
				}
				else {
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
			}
			else if(sFuncKey.equals(DeviceMain.FUNC_LIST.get_check_information.name())) {
				sTableNo = "";
				sTableExtension = "";
				sCheckNo = "";
				if(oFuncParamJSONObj.has("tableno") && !oFuncParamJSONObj.optString("tableno").equals(""))
					sTableNo = oFuncParamJSONObj.optString("tableno");
				if(oFuncParamJSONObj.has("tableextension") && !oFuncParamJSONObj.optString("tableextension").equals(""))
					sTableExtension = oFuncParamJSONObj.optString("tableextension");
				if(oFuncParamJSONObj.has("checkno") && !oFuncParamJSONObj.optString("checkno").equals(""))
					sCheckNo = oFuncParamJSONObj.optString("checkno");
				
				m_sLastErrorMessage = getCheckDetail(sTableNo, sTableExtension, sCheckNo);
				if(!m_sLastErrorCode.equals("")) {
					//No check exists
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				else
					m_oResponsePacket.put("result", new JSONObject(m_sLastErrorMessage));
			}
			else if (sFuncKey.equals(DeviceMain.FUNC_LIST.get_all_opened_checks.name())) {
				String sOpenUserLogin = "";
				
				// Get type : 0 - Get open check list opened by special employee, 1 - Get all open check
				String sGetType = PosCheck.GET_CHECK_LISTING_BY_SPECIFIC_USER;
				// Default Language Index
				Integer iLangidx = 1;
				
				if(oFuncParamJSONObj.has("langidx") && !oFuncParamJSONObj.optString("langidx").isEmpty() && Integer.valueOf(oFuncParamJSONObj.optString("langidx")) > 0 && Integer.valueOf(oFuncParamJSONObj.optString("langidx")) < 6)
					iLangidx = Integer.valueOf(oFuncParamJSONObj.optString("langidx"));
				if(oFuncParamJSONObj.has("openuserlogin") && !oFuncParamJSONObj.optString("openuserlogin").isEmpty())
					sOpenUserLogin = oFuncParamJSONObj.optString("openuserlogin");
				if(oFuncParamJSONObj.has("gettype") && !oFuncParamJSONObj.optString("gettype").isEmpty())
					sGetType = oFuncParamJSONObj.optString("gettype");
				
				m_sLastErrorMessage = getOpenCheckList(iLangidx, sGetType, sOpenUserLogin, sDateTime);
				if(!m_sLastErrorCode.isEmpty()) {
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				else
					m_oResponsePacket.put("result", new JSONObject(m_sLastErrorMessage));
			}
			//Check table exist
			else if(oPosFunction.getKey().equals(DeviceMain.FUNC_LIST.check_table_exist.name())) {
				int iCover = oFuncParamJSONObj.optInt("cover", 1);
				if(iCover <= 0)
					iCover = 1;
				//Try to open check
				m_sLastErrorMessage = openCheck(sTableNo, sTableExtension, null, iCover, true, sVerifyKey);
				if(!m_sLastErrorMessage.equals("")) {
					if (m_sLastErrorCode.equals("-32240")) {
						// Invalid verification key
						m_sLastErrorCode = "-32241";
						m_sLastErrorMessage = AppGlobal.g_oLang.get()
								._("table_is_occupied")+". "+AppGlobal.g_oLang.get()._("please_provide_a_valid_verification_key_for_the_check_no");
						
					}

					//No check exists
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				
				if(m_oFuncCheck.isOldCheck()) {
					//Old check, reply with check number
					m_oFuncCheck.unlockTable(false, false);
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("checkno", m_oFuncCheck.getCheckNo());
					
					m_oResponsePacket.put("result", oResultHashMap);
				}
				else {
					//New check
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", "-32042");
					oResultHashMap.put("message", AppGlobal.g_oLang.get()._("cannot_load_new_check"));
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
			}
			//pay
			else if(oPosFunction.getKey().equals(DeviceMain.FUNC_LIST.paid.name())) {
				String sAppliedUserNo = "";
				if(oFuncParamJSONObj.has("applieduserno"))
					sAppliedUserNo = oFuncParamJSONObj.optString("applieduserno");
				if(!this.getAppliedUserId(sAppliedUserNo)){
					//Return error, applied user not valid
					oResultHashMap.put("time", "");
					oResultHashMap.put("code", "-32208");
					oResultHashMap.put("message", "Invalid user");	
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				if(!payCheck(false, oFuncParamJSONObj, sVerifyKey)){
					oResultHashMap.put("time", "");
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					oResultHashMap.put("servertime", "");
					oResultHashMap.put("challenge", "");
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}else{
					oResultHashMap.put("time", "");
					oResultHashMap.put("businessdate", "");
					oResultHashMap.put("servertime", "");
					oResultHashMap.put("challenge", "");
					m_oResponsePacket.put("result", oResultHashMap);
				}
			}
			//apply discount or extra charge
			else if(oPosFunction.getKey().equals(AppGlobal.FUNC_LIST.check_discount.name()) || oPosFunction.getKey().equals(AppGlobal.FUNC_LIST.check_extra_charge.name())) {
				String sDiscountType = oFuncParamJSONObj.optString("type", "check");
				
				//Open Check first
				m_sLastErrorMessage = openCheck(sTableNo, sTableExtension, sCheckNo , 1, true, sVerifyKey);
				if(!m_sLastErrorMessage.isEmpty()) {
					//Return error, cannot open check
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				
				//apply discount
				boolean bResult = false;
				if(oPosFunction.getKey().equals(AppGlobal.FUNC_LIST.check_discount.name()))
					bResult = preProcessForApplyDiscount(false, sDiscountType, PosDiscountType.USED_FOR_DISCOUNT, null, oFuncParamJSONObj.optJSONArray("discounts")); 
				else
					bResult = preProcessForApplyDiscount(false, sDiscountType, PosDiscountType.USED_FOR_EXTRA_CHARGE, null, oFuncParamJSONObj.optJSONArray("extracharges")); 
				
				if(!bResult){
					//fail to apply
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					m_oResponsePacket.put("error", oResultHashMap);
				}else {
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("checkno", m_oFuncCheck.getCheckPrefixNo());
					oResultHashMap.put("checktotal", m_oFuncCheck.getCheckTotal().toPlainString());
					oResultHashMap.put("businessdate", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInStringWithFormat("yyyy-MM-dd"));
					m_oResponsePacket.put("result", oResultHashMap);
				}
				
				quitCheck();
				return bResult;
			}
			
			//apply item discount
			else if(oPosFunction.getKey().equals(AppGlobal.FUNC_LIST.item_discount.name())) {
				String sDiscountType = oFuncParamJSONObj.optString("type", "item");
				boolean bResult = true;
				
				//Open Check first
				m_sLastErrorMessage = openCheck(sTableNo, sTableExtension, sCheckNo , 1, true, sVerifyKey);
				if(!m_sLastErrorMessage.equals("")) {
					//Return error, cannot open check
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				
				if(oFuncParamJSONObj.optJSONArray("discounts") != null) {
					JSONArray oDiscountInfos = oFuncParamJSONObj.optJSONArray("discounts");
					
					//Check whether have duplicate item in same request
					ArrayList<HashMap<String, Integer>> oCheckingItemList = new ArrayList<HashMap<String, Integer>>();
					for(int i = 0; i < oDiscountInfos.length(); i++) {
						int iSeatNo = oDiscountInfos.optJSONObject(i).optInt("itemseat", -1);
						int iItemIndex = oDiscountInfos.optJSONObject(i).optInt("itemseq", -1);
						
						for(HashMap<String, Integer> oTempItem : oCheckingItemList) {
							if(iSeatNo == oTempItem.get("sectionId") && iItemIndex == oTempItem.get("itemIndex")) {
								//Return error, repeated item
								oResultHashMap.put("tableno", sTableNo);
								oResultHashMap.put("code", "-32233");
								oResultHashMap.put("message", "Duplicate item found for item discount");
								
								m_oResponsePacket.put("error", oResultHashMap);
								return false;
							}
						}
						
						HashMap<String, Integer> oTempSelectedItem = new HashMap<String, Integer>();
						oTempSelectedItem.put("partySeq", m_oFuncCheck.getCurrentCheckPartySeq());
						oTempSelectedItem.put("sectionId", iSeatNo);
						oTempSelectedItem.put("itemIndex", iItemIndex);
						oCheckingItemList.add(oTempSelectedItem);
					}
					
					for(int i = 0; i < oDiscountInfos.length(); i++) {
						ArrayList<HashMap<String, Integer>> oSelectedItems = new ArrayList<HashMap<String, Integer>>();
						int iSeatNo = oDiscountInfos.optJSONObject(i).optInt("itemseat", -1);
						int iItemIndex = oDiscountInfos.optJSONObject(i).optInt("itemseq", -1);
						HashMap<String, Integer> oTempSelectedItem = new HashMap<String, Integer>();
						oTempSelectedItem.put("partySeq", m_oFuncCheck.getCurrentCheckPartySeq());
						oTempSelectedItem.put("sectionId", iSeatNo);
						oTempSelectedItem.put("itemIndex", iItemIndex);
						oSelectedItems.add(oTempSelectedItem);
						
						JSONArray oDiscountArray = new JSONArray();
						JSONObject oDiscountObject = new JSONObject();
						oDiscountObject.put("code", oDiscountInfos.optJSONObject(i).optString("code", ""));
						if(oDiscountInfos.optJSONObject(i).has("amount"))
							oDiscountObject.put("amount", oDiscountInfos.optJSONObject(i).optString("amount", ""));
						oDiscountArray.put(oDiscountObject);
						
						//apply item discount
						bResult = preProcessForApplyDiscount(false, sDiscountType, PosDiscountType.USED_FOR_DISCOUNT, oSelectedItems, oDiscountArray);
						
						if(!bResult)
							break;
					}
				}
				
				if(!bResult){
					//fail to apply
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					m_oResponsePacket.put("error", oResultHashMap);
				}else {
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("checkno", m_oFuncCheck.getCheckPrefixNo());
					oResultHashMap.put("checktotal", m_oFuncCheck.getCheckTotal().toPlainString());
					oResultHashMap.put("businessdate", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInStringWithFormat("yyyy-MM-dd"));
					m_oResponsePacket.put("result", oResultHashMap);
				}
				
				quitCheck();
				return bResult;
			}
			//print check
			else if(sFuncKey.equals(DeviceMain.FUNC_LIST.print_check.name())) {
				//TODO: open and lock check 
				// if there is check number, table number and extension
				// will be replaced by the check which get by check number
				m_sLastErrorMessage = openCheck(sTableNo, sTableExtension, sCheckNo, 0, false, sVerifyKey);
				
				//TODO: return error if cannot open check
				if(!m_sLastErrorMessage.equals("")) {
					//No check exists
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				
				if(!this.printCheck()) {
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to print check");
					return false;
				}
			}
			//adjust tips
			else if(sFuncKey.equals(DeviceMain.FUNC_LIST.adjust_tips.name())) {
				if(sCheckNo.isEmpty()) {
					m_sLastErrorCode = "-32227";
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("check_is_not_saved_completely");
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				
				m_oFuncCheck = new FuncCheck();
				m_oFuncCheck.addListener(this);
				if (!m_oFuncCheck.getCheckByCheckNum(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(),
						AppGlobal.g_oFuncOutlet.get().getOutletId(), sCheckNo, true)) {
					// Fail to get check information
					m_sLastErrorCode = "-32055";
					m_sLastErrorMessage = m_oFuncCheck.getLastErrorMessage();
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				
				m_oFuncCheck.initBusinessDaySetup(AppGlobal.g_oFuncOutlet.get());
				if (!checkIsFullPaid(m_oFuncCheck)) {
					return false;
				}
				
				// Check whether payment records exist
				if (m_oFuncCheck.getCheckTotal().compareTo(m_oFuncCheck.getPaymentRecordPayTotal()) != 0) {
					m_sLastErrorCode = "-32234";
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("check_is_not_saved_completely");
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				
				if (m_oFuncCheck.isInUsedByOthers()) {
					// Check is used by others
					m_sLastErrorCode = "-32015";
					m_sLastErrorMessage = m_oFuncCheck.getLastErrorMessage();
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				
				if (!m_oFuncCheck.lockCheck())
					return false;
				
				// construct payment sequence with new tips amount
				JSONArray oPayments = oFuncParamJSONObj.optJSONArray("payments");
				if(oPayments == null) {
					m_sLastErrorCode = "-32231";
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("missing_payment_method");
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					m_oResponsePacket.put("error", oResultHashMap);
					m_oFuncCheck.unlockCheck();
					return false;
				}
				
				HashMap<Integer, String> oAdjustPaymentInfo = new HashMap<>();
				for(int i = 0; i < oPayments.length(); i++) {
					BigDecimal dNewTips = new BigDecimal(oPayments.getJSONObject(i).optString("newtips"));
					int iSeq = oPayments.getJSONObject(i).optInt("seq");
					// negative tips checking
					if(dNewTips.compareTo(BigDecimal.ZERO) < 0) {
						m_sLastErrorCode = "-32238"; 
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("tips_should_be_greater_than_zero");
						oResultHashMap.put("code", m_sLastErrorCode);
						oResultHashMap.put("message", m_sLastErrorMessage);
						m_oResponsePacket.put("error", oResultHashMap);
						m_oFuncCheck.unlockCheck();
						return false;
					}
					
					if(iSeq == 0) {
						m_sLastErrorCode = "-32237"; 
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("payment_not_found");
						oResultHashMap.put("code", m_sLastErrorCode);
						oResultHashMap.put("message", m_sLastErrorMessage);
						m_oResponsePacket.put("error", oResultHashMap);
						m_oFuncCheck.unlockCheck();
						return false;
					}
					oAdjustPaymentInfo.put(oPayments.getJSONObject(i).optInt("seq"), oPayments.getJSONObject(i).optString("newtips"));
				}
				
				if(!this.adjustTips(oAdjustPaymentInfo)) {
					//fail to adjust tips
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					m_oResponsePacket.put("error", oResultHashMap);
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to adjust tips");
					m_oFuncCheck.unlockCheck();
					return false;
				}
				
			} else if (sFuncKey.equals(DeviceMain.FUNC_LIST.lock_table.name())) {
				// Check if there is a verify key provided to lock the table
				if (sVerifyKey.isEmpty()) {
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", "-32245");
					oResultHashMap.put("message", AppGlobal.g_oLang.get()._("missing_verification_key"));

					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}

				m_oFuncCheck = new FuncCheck();
				// Load Check
				if (sCheckNo != null && !sCheckNo.isEmpty()) {
					if (m_oFuncCheck.getCheckByCheckNum(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(),
							AppGlobal.g_oFuncOutlet.get().getOutletId(), sCheckNo, true)) {
						sTableNo = m_oFuncCheck.getTableNo();
						sTableExtension = m_oFuncCheck.getTableExtension();
					}
				}
				m_oFuncCheck.addListener(this);

				// Get stationId of the station which is locking the table
				int iStatId = m_oFuncCheck.getLockTableStationIdWithoutLoadCheck(
						AppGlobal.g_oFuncOutlet.get().getOutletId(), sTableNo, sTableExtension);
				if (iStatId == AppGlobal.g_oFuncStation.get().getStationId()) {
					// Target table is locked by the current station already
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", "-32243");
					oResultHashMap.put("message", AppGlobal.g_oLang.get()._("table_is_locked_already"));

					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}

				// Try to lock table
				if (!m_oFuncCheck.lockTable(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(),
						AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(),
						AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(),
						sTableNo, sTableExtension, false, false, PosCheck.ORDERING_MODE_FINE_DINING, sVerifyKey, true)) {
					m_sLastErrorMessage = m_oFuncCheck.getLastErrorMessage();
					if (m_sLastErrorMessage.contains(AppGlobal.g_oLang.get()._("table_is_locked_by_station")))
						m_sLastErrorCode = "-32244";
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);

					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				} else {
					// Add log to action log list
					AppGlobal.g_oActionLog.get().addActionLog(DeviceMain.FUNC_LIST.lock_table.name(),
							PosActionLog.ACTION_RESULT_SUCCESS, sTableNo + sTableExtension,
							AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(),
							AppGlobal.g_oFuncOutlet.get().getOutletId(),
							AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(),
							AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(),
							AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", "");
					// handle action log
					AppGlobal.g_oActionLog.get().handleActionLog(false);

					m_oResponsePacket.put("result", oResultHashMap);
				}
			} else if (oPosFunction.getKey().equals(DeviceMain.FUNC_LIST.unlock_table.name())) {
				// Check if there is a verify key provided to unlock the table
				if (sVerifyKey.isEmpty()) {
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", "-32245");
					oResultHashMap.put("message", AppGlobal.g_oLang.get()._("missing_verification_key"));

					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}

				m_oFuncCheck = new FuncCheck();
				// Load Check
				if (sCheckNo != null && !sCheckNo.isEmpty()) {
					if (m_oFuncCheck.getCheckByCheckNum(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(),
							AppGlobal.g_oFuncOutlet.get().getOutletId(), sCheckNo, true)) {
						sTableNo = m_oFuncCheck.getTableNo();
						sTableExtension = m_oFuncCheck.getTableExtension();
					}
				}
				m_oFuncCheck.addListener(this);

				// Get information of the station which is locking the table
				int iLockStationId = m_oFuncCheck.getLockTableStationIdWithoutLoadCheck(
						AppGlobal.g_oFuncOutlet.get().getOutletId(), sTableNo, sTableExtension);
				if (iLockStationId > 0 && iLockStationId != AppGlobal.g_oFuncStation.get().getStationId()) {
					// Target table is locked by other stations
					FuncStation oLockStation = new FuncStation();
					if (oLockStation.loadStationById(iLockStationId))
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("table_is_locked_by_station") + " "
								+ oLockStation.getName(AppGlobal.g_oCurrentLangIndex.get());
					else
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("table_is_locked_by_station") + " " + iLockStationId;
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", "-32244");
					oResultHashMap.put("message", m_sLastErrorMessage);

					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}

				if (!m_oFuncCheck.getAndUnlockTableWithoutLoadCheck(AppGlobal.g_oFuncOutlet.get().getShopId(),
						AppGlobal.g_oFuncOutlet.get().getOutletId(), sTableNo, sTableExtension, sVerifyKey)) {
					m_sLastErrorMessage = m_oFuncCheck.getLastErrorMessage();
					if (m_sLastErrorMessage.equals(AppGlobal.g_oLang.get()._("invalid_verification_key")))
						m_sLastErrorCode = "-32240";
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);

					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				} else if (m_oFuncCheck.getOutletTableStationId() == 0) {
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", "-32242");
					oResultHashMap.put("message", AppGlobal.g_oLang.get()._("this_table_is_not_locked"));

					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				} else {
					// Add log to action log list
					AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.unlock_table.name(),
							PosActionLog.ACTION_RESULT_SUCCESS, sTableNo + sTableExtension,
							AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(),
							AppGlobal.g_oFuncOutlet.get().getOutletId(),
							AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(),
							AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(),
							AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", "");
					// handle action log
					AppGlobal.g_oActionLog.get().handleActionLog(false);

					m_oResponsePacket.put("result", oResultHashMap);
				}
			}
		}
		catch (Exception e) {
			AppGlobal.stack2Log(e);
			oResultHashMap.put("code", "-32001");
			oResultHashMap.put("message", e.toString());
			m_oResponsePacket.put("error", oResultHashMap);
			return false;
		}
		
		return true;
	}
	
	protected void triggerDaemon() {
		if (!m_oDaemonSemaphore.tryAcquire())
			return;
		
		Thread oThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject oMessageJSONObject;
					while (true) {
						synchronized (m_oMessageJSONobjectQueue) {
							oMessageJSONObject = m_oMessageJSONobjectQueue.poll();
						}
						
						if (oMessageJSONObject == null)
							break;
						
						m_sLastErrorCode = "";
						m_sLastErrorMessage = "";
						m_oResponsePacket.clear();
						
						AppGlobal.g_oFuncStation.set(m_oFuncStation);
						AppGlobal.g_oFuncOutlet.set(m_oFuncOutlet);
						AppGlobal.g_oCurrentLangIndex.set(m_iLangIndex);
						AppGlobal.g_oFuncUser.set(m_oFuncUser);
						AppGlobal.g_oFuncMixAndMatch.set(m_oFuncMixAndMatch);
						OmWsClientGlobal.g_oWsClient.set(m_omWsClient);
						AppGlobal.g_oLang.set(m_oLangResource);
						
						// Initialize the action log list
						AppGlobal.g_oActionLog.set(m_oActionLog);
						AppGlobal.g_oPosInterfaceConfigList.set(m_oPosInterfaceConfigList);
						
						// Initialize pos config list
						AppGlobal.g_oPosConfigList.set(m_oPosConfigList);
						
						// Initialize the menu item and menu cache
						AppGlobal.g_oFuncMenu.set(m_oFuncMenu);
						
						AppGlobal.g_oCurrentLangIndex.set(m_iCurrentLangIndex);
						
						// Initialize the result for auto function
						AppGlobal.g_sResultForAutoFunction.set(m_sResultForAutoFunction);
						
						//Write application log
						String sResponseQueue = oMessageJSONObject.optString("responseQueue");
						JSONObject oRequestJSONObject = new JSONObject(oMessageJSONObject.optString("request"));
						String sFuncKey = oRequestJSONObject.optString("funckey");
						String sPacketId = oRequestJSONObject.optString("id");
						JSONObject oParamsJSONObj = oRequestJSONObject.optJSONObject("params");
						
						AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Handle request:"+oRequestJSONObject.toString());
						m_oResponsePacket.put("funckey", sFuncKey);
						m_oResponsePacket.put("id", sPacketId);
						
						fcnSwitch(sFuncKey, oParamsJSONObj);
						JSONObject tempJSONObj = new JSONObject(m_oResponsePacket);
						
						if(m_oFuncMessageQueue.isConnected())
							m_oFuncMessageQueue.publishMessage("", sResponseQueue, tempJSONObj.toString(), null);
						AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Request response:"+tempJSONObj.toString());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} finally {
					m_oDaemonSemaphore.release();
				}
			}
		});
		oThread.start();
	}
	
	protected void addPosting(JSONObject oMessageJSONObject) {
		synchronized (m_oMessageJSONobjectQueue) {
			try {
				JSONObject oTempMessageJSON = new JSONObject(oMessageJSONObject.toString());
				m_oMessageJSONobjectQueue.add(oTempMessageJSON);
			}catch (JSONException e) {
				
			}
		}
		
		triggerDaemon();
	}
	
	synchronized private void addProcessCheck(String sStoredProcessingCheckKey){
		m_oProcessingSendChecks.put(sStoredProcessingCheckKey, AppGlobal.getCurrentTime(false));
	}
	
	synchronized private void removeProcessCheck(String sStoredProcessingCheckKey){
		// Finish send check, remove process to stored processing check list
		if(m_oProcessingSendChecks.containsKey(sStoredProcessingCheckKey))
			m_oProcessingSendChecks.remove(sStoredProcessingCheckKey);
	}
	
	//Pre-checking for open check process
	private boolean openCheckPreChecking() {
		// Check if period is defined
		if(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod() == null || AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId().compareTo("") == 0){
			m_sLastErrorCode = "-32022";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("missing_period_setting");
			return false;
		}
		
		return true;
	}

	private String openCheck(String sTableNo, String sTableExtension, String sCheckNo, int iDefaultCover, boolean bAllowSendNewCheck, String sVerifyKey) {
		String sErrorMsg = "";
		int iCover = 0;
		
		//Pre-checking before open check
		if(!openCheckPreChecking()) {
			return m_sLastErrorMessage;
		}
		
		// ***** DEBUG *****
		// Show memory usage
		if(AppGlobal.g_iDebugMode > 0)
			AppGlobal.showMemory();
		// *****************
		
		//Memory check
		AppGlobal.checkMemory();
		
		if(AppGlobal.g_iLogLevel >= 9){
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Start open check");
		}
		
		m_oFuncCheck = new FuncCheck();
		if(sCheckNo != null && !sCheckNo.isEmpty()) {
			if(m_oFuncCheck.getCheckByCheckNum(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), sCheckNo, false)) { 
				sTableNo = m_oFuncCheck.getTableNo();
				sTableExtension = m_oFuncCheck.getTableExtension();
			}
		}
		m_oFuncCheck.addListener(this);
		//Init business date setup
		m_oFuncCheck.initBusinessDaySetup(AppGlobal.g_oFuncOutlet.get());
		
		if(AppGlobal.g_iLogLevel >= 9){
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Before update stock");
		}
		
		// Get current item stock list
		//*****************************************************************
		// Create thread to get current item stock list
		AppThreadManager oAppThreadManager = new AppThreadManager();
		
		// Add the method to the thread manager
		// Thread 1 :
		Object[] oParameters = new Object[3];
		oParameters[0] = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oParameters[1] = PosOutletItem.CHECK_STOCK_YES;
		oParameters[2] = PosOutletItem.SOLDOUT_YES;
		oAppThreadManager.addThread(1, m_oFuncCheck, "getCurrentItemStockList", oParameters);
		
		if(AppGlobal.g_bNotCheckStock == false){
			oAppThreadManager.runThread();
		}
		
		//Check if period is defined
		String sPeriodId = AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId();
		
		if(AppGlobal.g_iLogLevel >= 9){
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Before lock table");
		}
		
		//Try to lock table
		//For old check, load the old check
		if(m_oFuncCheck.lockTable(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), sPeriodId, AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), sTableNo, sTableExtension, true, false, PosCheck.ORDERING_MODE_FINE_DINING, sVerifyKey, false)) {
			if(AppGlobal.g_iLogLevel >= 9){
				AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "After lock table");
			}
			
			// Wait for loading the current item stock list
			oAppThreadManager.waitForThread();
			
			if(AppGlobal.g_iLogLevel >= 9){
				AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "After update stock");
			}
		}
		else {
			//Wait for loading the current item stock list
			oAppThreadManager.waitForThread();
			//Fail to lock table
			sErrorMsg = m_oFuncCheck.getLastErrorMessage();
			if (sErrorMsg.equals(AppGlobal.g_oLang.get()._("invalid_verification_key")))
				m_sLastErrorCode = "-32240";
			else if (sErrorMsg.contains(AppGlobal.g_oLang.get()._("table_is_locked_by_station")))
				m_sLastErrorCode = "-32244";
			return sErrorMsg;
		}
		
		//New check
		if(!m_oFuncCheck.isOldCheck()) {
			if (AppGlobal.g_oFuncStation.get().getNotAllowOpenNewCheck() || !bAllowSendNewCheck) {
				//Not allow to open new check
				sErrorMsg = AppGlobal.g_oLang.get()._("not_allow_to_send_new_check");
				m_oFuncCheck.unlockTable(false, false);
				return sErrorMsg;
			}
			
			iCover = iDefaultCover;
			m_oFuncCheck.setCover(iCover, false);
		}
		
		if(m_oFuncCheck.isOldCheck())
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Open old table " + sTableNo + "");
		else {
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Open new table " + sTableNo + "");
		}
		
		if(AppGlobal.g_iLogLevel >= 9){
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Finish open check");
		}
		
		return "";
	}
	
	//Add hot item - 1st step
	private String addHotItem(int iId, String sItemCode, int iTargetPriceLevel, BigDecimal dQty, String sOpenDesc, BigDecimal dOpenPrice, String sIsTakeAway, int iSeatNo, int iCourseNo) {
		String sErrorMsg = "";
		int iPriceLevel = AppGlobal.g_oFuncOutlet.get().getPriceLevel();
		FuncMenuItem oFuncMenuItem = null;
		
		//qty should not be zero
		if(dQty.compareTo(BigDecimal.ZERO) == 0) {
			//return error, item quantity invalid
			m_sLastErrorCode = "-32602";
			sErrorMsg = AppGlobal.g_oLang.get()._("quantity_must_be_larger_than_zero");
			return sErrorMsg;
		}
		
		if(iId > 0)
			oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(iId);
		else if(!sItemCode.isEmpty()) {
			oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemCode(sItemCode);
			if(oFuncMenuItem != null)
				iId = oFuncMenuItem.getMenuItem().getItemId();
		}
		if(oFuncMenuItem == null) {
			//return error, item cannot be found
			m_sLastErrorCode = "-32024";
			sErrorMsg = AppGlobal.g_oLang.get()._("item_cannot_be_found");
			return sErrorMsg;
		}
		
		if(iTargetPriceLevel >= 0)
			iPriceLevel = iTargetPriceLevel;
		
		String sAddItemResult = addItem(null, iId, dQty, new BigDecimal("1.0"), sOpenDesc, dOpenPrice, false, false, iPriceLevel, sIsTakeAway, iSeatNo, iCourseNo, 0);
		
		if(sAddItemResult.equals("")) {
			//Add hot item success
			String sResult = finishAddItem();
			if(!sResult.equals("")) {
				sErrorMsg = sResult;
				return sErrorMsg;
			}
		}
		else {
			sErrorMsg = sAddItemResult;
			return sErrorMsg;
		}
		
		return "";
	}
	
	//Add item - core part
	private String addItem(FuncCheckItem oParentFuncCheckItem, int iId, BigDecimal dQty, BigDecimal dBaseQty, String sOpenDesc, BigDecimal dOpenPrice, boolean bModifier, boolean bChildItem, int iPriceLevel, String sIsTakeAway, int iSeatNo, int iCourseNo, int iChildItemSeq) {
		String sErrorMsg = "";
		
		// Check errors : 	Retrieve item from menu error,
		//					disable or inactive,
		//					item sold out,
		//					item count
		FuncCheckItem oFuncCheckItem = this.addItemPreChecking(oParentFuncCheckItem, iId, dQty, dBaseQty, bModifier, bChildItem, iPriceLevel);
		if (oFuncCheckItem == null) {
			//Error occur
			sErrorMsg = m_sLastErrorMessage;
			return sErrorMsg;
		}
		
		//TODO
		//Check order level
		
		//Check if item is open description
		if(oFuncCheckItem.isOpenDescription()) {
			if(sOpenDesc.length() == 0) {
				m_sLastErrorCode = "-32027";
				sErrorMsg = AppGlobal.g_oLang.get()._("not_allow_blank_item_description");
				return sErrorMsg;
			}
			
			oFuncCheckItem.setItemDesc(false, sOpenDesc);
		}
		
		//Check if item is append description
		if(oFuncCheckItem.isAppendOpenDescription()) {
			oFuncCheckItem.setItemDesc(true, sOpenDesc);
			oFuncCheckItem.setItemShortDesc(true, sOpenDesc);
		}
		
		//Check if item is open price
		if(oFuncCheckItem.isOpenPrice()) {
			BigDecimal dMinPrice = null, dMaxPrice = null;
			if(bChildItem) {
				dMinPrice = oFuncCheckItem.getMenuItem().getChildMinPrice();
				dMaxPrice = oFuncCheckItem.getMenuItem().getChildMaxPrice();
			}
			else {
				dMinPrice = oFuncCheckItem.getMenuItem().getBasicMinPrice();
				dMaxPrice = oFuncCheckItem.getMenuItem().getBasicMaxPrice();
			}
			
			if(dMinPrice != null && dOpenPrice.compareTo(dMinPrice) < 0) {
				m_sLastErrorCode = "-32028";
				sErrorMsg = AppGlobal.g_oLang.get()._("open_price_need_to_be_larger_than")+ " " + dMinPrice;
				return sErrorMsg;
			}
			else if(dMaxPrice != null && dOpenPrice.compareTo(dMaxPrice) < 0) {
				m_sLastErrorCode = "-32029";
				sErrorMsg = AppGlobal.g_oLang.get()._("open_price_need_to_be_less_than")+ " " + dMaxPrice;
				return sErrorMsg;
			}
			
			oFuncCheckItem.setOpenPriceToItem(dOpenPrice);
		}
		
		//Check if force modifier should be applied or not
		if(AppGlobal.OPERATION_MODE.stock_delivery.equals(m_eOperationMode) == false && oFuncCheckItem.isForceModifier()) {
			
		}
		
		oFuncCheckItem.getCheckItem().setOrderingType(m_oFuncCheck.getOrderingType());
		
		//Check whether included in applied check discount
		sErrorMsg = checkDiscountForNewlyAddedItem(oFuncCheckItem);
		if(!sErrorMsg.equals("")) {
			return sErrorMsg;
		}
		
		//Add to item to list according to type
		if(bChildItem && oParentFuncCheckItem != null) {
			oFuncCheckItem.getCheckItem().setOrderUserId(AppGlobal.g_oFuncUser.get().getUserId());
			oParentFuncCheckItem.addChildItemToList(oFuncCheckItem, true);
			m_oStoredFuncCheckItemList.add(oFuncCheckItem);
		} else if(bModifier && oParentFuncCheckItem != null) {
			oParentFuncCheckItem.addModifierToList(oFuncCheckItem, true);
		} else {
			m_oStoredFuncCheckItemList.add(oFuncCheckItem);
		}
		
		if(sIsTakeAway.equals("1"))
			oFuncCheckItem.takeout(true);
		oFuncCheckItem.getCheckItem().setCourseId(iCourseNo);
		oFuncCheckItem.getCheckItem().setSeatNo(iSeatNo);
		oFuncCheckItem.setChildItemSeqInSetMenuLookup(iChildItemSeq);
		
		return "";
	}
	
	private FuncCheckItem addItemPreChecking(FuncCheckItem oParentFuncCheckItem, int iId, BigDecimal dQty, BigDecimal dBaseQty, boolean bModifier, boolean bChildItem, int iPriceLevel) {
		FuncCheckItem oFuncCheckItem = new FuncCheckItem();
		
		//Create func check item from menu item
		if(!oFuncCheckItem.retieveItemFromMenu(iId, dQty, dBaseQty, oParentFuncCheckItem, bModifier, bChildItem, iPriceLevel)) {
			//retrieve error
			m_sLastErrorCode = "-32024";
			m_sLastErrorMessage = oFuncCheckItem.getLastErrorMessage();
			return null;
		}

		if(m_iAppliedUserId == 0)
			oFuncCheckItem.getCheckItem().setOrderUserId(AppGlobal.g_oFuncUser.get().getUserId());
		else
			oFuncCheckItem.getCheckItem().setOrderUserId(m_iAppliedUserId);
		oFuncCheckItem.getCheckItem().setBusinessDayId(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
		
		//Check if disable or inactive
		if(oFuncCheckItem.isDisable() || oFuncCheckItem.isInactive()) {
			//Check with menu module again
			oFuncCheckItem.updateFuncCheckItemFromDB();
			if(oFuncCheckItem.isDisable() || oFuncCheckItem.isInactive()) {
				//item disable or inactive
				m_sLastErrorMessage = oFuncCheckItem.getLastErrorMessage();
				return null;
			}
		}
		
		//Check whether item is sold out
		if(!m_eOperationMode.equals(AppGlobal.OPERATION_MODE.stock_delivery) && m_oFuncCheck.isSoldout(oFuncCheckItem.getMenuItemId())) {
			//Item sold out
			m_sLastErrorCode = "-32025";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("item_soldout");
			return null;
		}
		
		//Update item count
		String updateItemCntResult = m_oFuncCheck.updateItemCount(oFuncCheckItem.getMenuItemId(), dQty, true, false, false);
		if(updateItemCntResult.equals("s")) {
			//Error
			m_sLastErrorCode = "-32025";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("item_soldout");
			return null;
		}
		else if(updateItemCntResult.equals("<")) {
			//Not enough stock
			m_sLastErrorCode = "-32026";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("dont_have_enough_stock");
			return null;
		}
		else if(updateItemCntResult.equals("-s") || updateItemCntResult.equals("-<")) {
			m_oFuncCheck.updateItemCount(oFuncCheckItem.getMenuItemId(), dQty, true, true, false);
		}
		
		return oFuncCheckItem;
	}
	
	private String finishAddItem() {
		int iSphrId = 0;
		OutSpecialHour oSpecialHour = AppGlobal.g_oFuncOutlet.get().getCurrentSpecialPeriod();
		if(oSpecialHour != null)
			iSphrId = oSpecialHour.getSphrId();
		
		//Add the item to check
		int iSelectedSeatNo = 0;
		
		for(FuncCheckItem oFuncCheckItem : m_oStoredFuncCheckItemList) {
			iSelectedSeatNo = 0;
			if(oFuncCheckItem.getCheckItem().getSeatNo() > 0)
				iSelectedSeatNo = oFuncCheckItem.getCheckItem().getSeatNo();
			int iCurrentItemCount = m_oFuncCheck.getItemListCount(iSelectedSeatNo);
			
			// Do override checking
			DateTime oCheckOpenDateTime = null;
			DateTime oItemOrderDateTime = AppGlobal.getCurrentTime(false);
			Time oCheckOpenTime = null, oItemOrderTime = null;
			DateTimeFormatter oTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss");
			SimpleDateFormat oSimpleTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
			if(m_oFuncCheck.isOldCheck()) 
				oCheckOpenDateTime = m_oFuncCheck.getOpenLocTime();
			else
				oCheckOpenDateTime = AppGlobal.getCurrentTime(false);
			try {
				oCheckOpenTime = new Time(oSimpleTimeFormat.parse(oTimeFormatter.print(oCheckOpenDateTime)).getTime());
				oItemOrderTime = new Time(oSimpleTimeFormat.parse(oTimeFormatter.print(oItemOrderDateTime)).getTime());
			}catch (ParseException exception) {
				exception.printStackTrace();
				AppGlobal.stack2Log(exception);
			}
			//get discount type id list
			List<Integer> oDtypeIdList = new ArrayList<Integer>();
			if(oFuncCheckItem.getItemDiscountList().size() > 0) {
				for(PosCheckDiscount oCheckDiscount : oFuncCheckItem.getItemDiscountList())
					oDtypeIdList.add(oCheckDiscount.getDtypId());
			}
			
			if(m_oFuncCheck.getCurrentPartyAppliedCheckDiscount().size() > 0) {
				for(PosCheckDiscount oCheckDiscount : m_oFuncCheck.getCurrentPartyAppliedCheckDiscount())
					oDtypeIdList.add(oCheckDiscount.getDtypId());
			}
			
			boolean bFastFoodCheck = (m_eOperationMode.equals(AppGlobal.OPERATION_MODE.fast_food)
					|| m_eOperationMode.equals(AppGlobal.OPERATION_MODE.self_order_kiosk)
					|| m_eOperationMode.equals(AppGlobal.OPERATION_MODE.bar_tab));
			if(m_oFuncCheck.getTableNo() != null)
				AppGlobal.g_oFuncOverrideList.get(AppGlobal.g_oFuncOutlet.get().getOutletId()).checkPriceOverrideForItem(oFuncCheckItem, bFastFoodCheck, oCheckOpenTime, oItemOrderTime,
						Integer.valueOf(m_oFuncCheck.getTableNo()).intValue(), m_oFuncCheck.getTableExtension(), iSphrId, m_oFuncCheck.getCustomTypeId(), oDtypeIdList);
			else
				AppGlobal.g_oFuncOverrideList.get(AppGlobal.g_oFuncOutlet.get().getOutletId()).checkPriceOverrideForItem(oFuncCheckItem, bFastFoodCheck, oCheckOpenTime,
						oItemOrderTime, -1, "", iSphrId, m_oFuncCheck.getCustomTypeId(), oDtypeIdList);
			
			if(iCurrentItemCount > 0) {
				int iLastItemIndex = iCurrentItemCount - 1;
				FuncCheckItem oLastFuncCheckItem = m_oFuncCheck.getCheckItem(iSelectedSeatNo, iLastItemIndex);
				
				//Check if the item can be merge to previous item
				boolean bCanMerge = true;
				//Check if the last item is old item
				if(oLastFuncCheckItem.isOldItem())
					bCanMerge = false;
				//Check if menu id is matched or not
				else if(oFuncCheckItem.getMenuItemId() != oLastFuncCheckItem.getMenuItemId())
					bCanMerge = false;
				//Cannot merge if have modifier
				else if(oFuncCheckItem.getModifierList().size() > 0 || oLastFuncCheckItem.getModifierList().size() > 0)
					bCanMerge = false;
				//Cannot merge if have child
				else if(oFuncCheckItem.isSetMenu() || oFuncCheckItem.getChildItemList().size() > 0 || oLastFuncCheckItem.getChildItemList().size() > 0)
					bCanMerge = false;
				//Cannot merge if act as child item
				else if(oFuncCheckItem.isSetMenuItem())
					bCanMerge = false;
				//Cannot merge if have discount
				else if(oFuncCheckItem.hasItemDiscount(false) || oLastFuncCheckItem.hasItemDiscount(false))
					bCanMerge = false;
				//Cannot merge if open description
				else if(oFuncCheckItem.isOpenDescription() || oLastFuncCheckItem.isOpenDescription() || oFuncCheckItem.isAppendOpenDescription() || oLastFuncCheckItem.isAppendOpenDescription())
					bCanMerge = false;
				//Cannot merge if open price
				else if(oFuncCheckItem.isOpenPrice() || oLastFuncCheckItem.isOpenPrice())
					bCanMerge = false;
				//Check if the price is matched or not
				else if(oFuncCheckItem.getCheckItem().getPrice().compareTo(oLastFuncCheckItem.getCheckItem().getPrice()) != 0)
					bCanMerge = false;
				
				if(bCanMerge) {
					BigDecimal dNewQty = oLastFuncCheckItem.getCheckItem().getQty();
					dNewQty = dNewQty.add(oFuncCheckItem.getCheckItem().getQty());
					
					if(!m_oFuncCheck.changeItemQty(false, iSelectedSeatNo, iLastItemIndex, m_oFuncCheck.getTableNo(), "", dNewQty, false)) {
						//Error
						return oFuncCheckItem.getLastErrorMessage();
					}
					continue;
				}
			}
			
			//Add to item list with given seat no.
			m_oFuncCheck.addItemToItemList(iSelectedSeatNo, iCurrentItemCount+1, oFuncCheckItem);
			
			calculateCheck();
		}
		
		//Recalculate the check
		calculateCheck();
		
		if(m_oStoredFuncCheckItemList.size() > 0)
			m_oStoredFuncCheckItemList.clear();
		
		return "";
	}
	
	//Add Hot modifier
	private String addHotModifier(int iModifierId, int iModifierListIndex, String sOpenDesc, int iSeatNo) {
		String sErrorMsg = "";
		
		int iSelectedSeatNo = 0;
		if(iSeatNo > 0)
			iSelectedSeatNo = iSeatNo;
		int iCurrentItemIndex = m_oFuncCheck.getItemListCount(iSelectedSeatNo) - 1;
		List<FuncCheckItem> oSelectedSectionList = m_oFuncCheck.getItemList(iSelectedSeatNo);
		
		if(oSelectedSectionList.size() == 0) {
			//Error, no item ordered
			m_sLastErrorCode = "-32031";
			sErrorMsg = AppGlobal.g_oLang.get()._("no_item_ordered");
			return sErrorMsg;
		}
		
		FuncCheckItem oLastCheckItem = m_oFuncCheck.getCheckItem(iSelectedSeatNo, iCurrentItemIndex);
		
		//No new item on the list
		if(oLastCheckItem.isOldItem()) {
			//No new item, return error
			m_sLastErrorCode = "-32032";
			sErrorMsg = AppGlobal.g_oLang.get()._("no_new_item_ordered");
			return sErrorMsg;
		}
		if(oLastCheckItem.isOpenModifier()) {
			m_sLastErrorCode = "-32034";
			sErrorMsg = AppGlobal.g_oLang.get()._("this_item_already_have_open_price_modifier")+", "+AppGlobal.g_oLang.get()._("cannot_apply_modifier_again");
			return sErrorMsg;
		}
		
		//Prepare items for adding modifier
		ArrayList<FuncCheckItem> oTempCheckItemList = new ArrayList<FuncCheckItem>();	
		if(oLastCheckItem.isSetMenu()) {
			if(oLastCheckItem.getChildItemList().size() > 0)
				oTempCheckItemList = oLastCheckItem.getChildItemList();
		}else 
			oTempCheckItemList.add(oLastCheckItem);
		
		for(FuncCheckItem oTempCheck:oTempCheckItemList) {
			String sResult = addModifier(oTempCheck, iModifierId, iModifierListIndex, new BigDecimal("1.0"), sOpenDesc);
			if(!sResult.isEmpty()) {
				//Add modifier error, return error and code
				sErrorMsg = sResult;
				return sErrorMsg;
			}
		}
		//Add hot modifier success
		finishAddModifier();
		return "";
	}
	
	//Add modifier
	private String addModifier(FuncCheckItem oParentFuncCheckItem, int iId, int iModifierListIdx, BigDecimal dQty, String sOpenDesc) {
		String sErrorMsg = "";
		FuncCheckItem oModiFuncCheckItem = new FuncCheckItem();
		
		//Create the modifier func check item from menu item
		if(!oModiFuncCheckItem.retieveItemFromMenu(iId, dQty, new BigDecimal("1.0"), oParentFuncCheckItem, true, false, oParentFuncCheckItem.getCheckItem().getPriceLevel())) {
			//Return error, retrieve fail
			m_sLastErrorCode = "-32035";
			sErrorMsg = oModiFuncCheckItem.getLastErrorMessage();
			return sErrorMsg;
		}
		
		//Set Parent Tab Index
		if(iModifierListIdx != 0)			//TODO, 0 means common modifier
			oModiFuncCheckItem.setPanelLookupContent(iModifierListIdx, -1, null);
		
		//Check if item is open description
		if(oModiFuncCheckItem.isOpenDescription()) {
			if(sOpenDesc.length() == 0) {
				m_sLastErrorCode = "-32036";
				sErrorMsg = AppGlobal.g_oLang.get()._("not_allow_blank_item_description");
				return sErrorMsg;
			}
			oModiFuncCheckItem.setItemDesc(false, sOpenDesc);
		}
		
		if(oModiFuncCheckItem.isMinimumChargeItem()) {
			m_sLastErrorCode = "-32036";
			sErrorMsg = AppGlobal.g_oLang.get()._("cannot_perform_this_function_on_minimum_charge_item");
			return sErrorMsg;	
		}
		
/*		//TODO
		//Check if item is open price
		if(oModiFuncCheckItem.isOpenModifier()) {
			
		}*/
		
		//TODO
		//Check order level
		
		oModiFuncCheckItem.getCheckItem().setOrderUserId(AppGlobal.g_oFuncUser.get().getUserId());
		
		//Add modifier to parent's item
		oParentFuncCheckItem.addModifierToList(oModiFuncCheckItem, true);
		
		// Add log to action log list
		String sLogRemark = "Add modifierId:"+iId+","+oModiFuncCheckItem.getItemDescriptionByIndex(AppGlobal.g_oCurrentLangIndex.get());
		oParentFuncCheckItem.addActionLog(AppGlobal.FUNC_LIST.item_modifier.name(), PosActionLog.ACTION_RESULT_SUCCESS, m_oFuncCheck.getTableNoWithExtensionForDisplay(), AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), m_oFuncCheck.getCheckId(), "", "", "", "", sLogRemark);
		
		return "";
	}
	
	private void finishAddModifier() {
		calculateCheck();
	}
	
	//Add Set Menu child item
	private String addSetMenuChildItem(int iChildItemId, int iPriceLevel, int iSeatNo, BigDecimal dBaseQty, int iChildItemSeq) {
		String sErrorMsg = "";
		
		int iSelectedSeatNo = 0;
		if(iSeatNo > 0)
			iSelectedSeatNo = iSeatNo;
		int iCurrentItemIndex = m_oFuncCheck.getItemListCount(iSelectedSeatNo) - 1;
		List<FuncCheckItem> oSelectedSectionList = m_oFuncCheck.getItemList(iSelectedSeatNo);
		
		if(oSelectedSectionList.size() == 0) {
			//Error, no item orderd
			m_sLastErrorCode = "-32031";
			sErrorMsg = AppGlobal.g_oLang.get()._("no_item_ordered");
			return sErrorMsg;
		}
		
		FuncCheckItem oLastCheckItem = m_oFuncCheck.getCheckItem(iSelectedSeatNo, iCurrentItemIndex);
		
		//No new item on the list
		if(oLastCheckItem.isOldItem()) {
			//No new item, return error
			m_sLastErrorCode = "-32032";
			sErrorMsg = AppGlobal.g_oLang.get()._("no_new_item_ordered");
			return sErrorMsg;
		}
		
		//Add child item
		sErrorMsg = addItem(oLastCheckItem, iChildItemId, new BigDecimal("1.0"), dBaseQty, "", null, false, true, iPriceLevel, "", 0 ,0, iChildItemSeq);
		if(!sErrorMsg.equals("")) {
			return sErrorMsg;
		}
		
		return "";
	}
	
	//Calculate check
	private void calculateCheck() {
		//Recalculate the check
		m_oFuncCheck.calcCheck();
	}
	
	private String checkDiscountForNewlyAddedItem(FuncCheckItem oNewFuncCheckItem) {
		String sErrMsg = "";
		List<Integer> oItemDiscountGrpList = new ArrayList<Integer>();
		List<PosCheckDiscount> oAppliedCheckPartyDiscountList = null;
		HashMap<Integer, Boolean> oDiscountAllowance = new HashMap<Integer, Boolean>();
		
		// get the discount allowance
		// Pre-checking if the item is missing in menu
		if(oNewFuncCheckItem.getMenuItem() == null){
			m_sLastErrorCode = "-32030";
			sErrMsg = AppGlobal.g_oLang.get()._("item_is_missing_in_menu_setup") + " (" + oNewFuncCheckItem.getItemDescriptionByIndex(AppGlobal.g_oCurrentLangIndex.get()) + ")";
			return sErrMsg;
		}
		
		oAppliedCheckPartyDiscountList = m_oFuncCheck.getCurrentPartyAppliedCheckDiscount();
		if(oAppliedCheckPartyDiscountList == null || oAppliedCheckPartyDiscountList.size() == 0)
			return "";
		
		for(PosCheckDiscount oAppliedCheckDisc : oAppliedCheckPartyDiscountList){
			int iCheckDiscountIndex = oAppliedCheckDisc.getSeq();
			PosDiscountType oDiscountType = new PosDiscountType();
			oDiscountType.readById(oAppliedCheckDisc.getDtypId());
			
			if(oNewFuncCheckItem.getMenuItem().getDiscountItemGroupId() != 0 && !oItemDiscountGrpList.contains(oNewFuncCheckItem.getMenuItem().getDiscountItemGroupId())) {
				boolean bDiscountAllowance = AppGlobal.g_oFuncDiscountAclList.get(AppGlobal.g_oFuncOutlet.get().getOutletId()).checkDiscountAcl(oNewFuncCheckItem.getMenuItem(), oDiscountType);
				oDiscountAllowance.put(oNewFuncCheckItem.getMenuItem().getDiscountItemGroupId(), bDiscountAllowance);
			}
			
			//check the selected discount is available for the selected item
			/*if(oSelectedDiscType.getDiscountAllowance(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), dateFormat.format(oBusinessDay.getDate()), oBusinessDay.isHoliday(), oBusinessDay.isDayBeforeHoliday(), oBusinessDay.isSpecialDay(), oBusinessDay.isDayBeforeSpecialDay(), oBusinessDay.getDayOfWeek()) == false) 
				continue;*/
			if(oNewFuncCheckItem.getMenuItem().getDiscountItemGroupId() == 0)
				continue;
			if(!oDiscountAllowance.containsKey(oNewFuncCheckItem.getMenuItem().getDiscountItemGroupId()))
				continue;
			if(oDiscountAllowance.get(oNewFuncCheckItem.getMenuItem().getDiscountItemGroupId()) == false)
				continue;
			
			//check whether the selected item is available for applying discount
			if(oNewFuncCheckItem.getCheckItem().getPrice().compareTo(BigDecimal.ZERO) == 0) 
				continue;
			
			oAppliedCheckDisc.setModified(true);
			PosCheckDiscountItem oCheckDiscountItem = new PosCheckDiscountItem();
			oCheckDiscountItem.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
			oCheckDiscountItem.setCitmId(oNewFuncCheckItem.getCheckItem().getCitmId());
			if(oAppliedCheckDisc.getCdisId().compareTo("") > 0)
				oCheckDiscountItem.setCdisId(oAppliedCheckDisc.getCdisId());
			oNewFuncCheckItem.addCheckDiscountItemToList(iCheckDiscountIndex, oCheckDiscountItem);
			oAppliedCheckDisc.addCheckDiscountItemCount(1);
		}
		return "";
	}
	
	//Mix and match function
	private void mixAndMatchFunction(){
		if(AppGlobal.OPERATION_MODE.stock_delivery.equals(m_eOperationMode))
			// Stock invoice mode
			return;
		
		if(!AppGlobal.g_oFuncMixAndMatch.get().isSupportMixAndMatch())
			// Not support mix and match
			return;
		
		// Split multiple quantity new item to single quantity
		m_oFuncCheck.splitMultipleQtyNewItemToSingleQty();
		
		// Perform mix and match
		AppGlobal.g_oFuncMixAndMatch.get().processMixAndMatch(m_oFuncCheck);
		
		// Update screen
		this.calculateCheck();
		
		// Update database for old check
		if(m_oFuncCheck.isOldCheck()){
			// Update old item price
			m_oFuncCheck.updateCheck(false, true, PosCheckItem.SEND_MODE_OLD_ITEM, 0, false, 0, 0, 0, "", false, false, false, false, 0, 0, false, false);
		}
	}
	
	//Send check
	private String sendCheck() {
		String sErrorMsg = "";
		boolean bIsOldCheck = false;
		int iChosenCheckPfmtId = 0;
		
		bIsOldCheck = m_oFuncCheck.isOldCheck();
		
		if(!m_oFuncCheck.isNewCheckWithNoItem()) {
			//Is old check or is new check with new item
			//Update items according to database first
			m_oFuncCheck.updateSystemMenuItemList();
			
			// Set the check open time and do override
			m_oFuncCheck.setCheckOpenTimeValue(AppGlobal.g_oFuncOutlet.get().getOutletId(), false, m_iAppliedUserId);
			
			//Mix and match function
			mixAndMatchFunction();
			
			//Add the process to stored list
			String sStoredProcessingCheckKey = m_oFuncCheck.getTableNoWithExtensionForDisplay();
			this.addProcessCheck(sStoredProcessingCheckKey);
			
			sErrorMsg = processSendCheck(false, false, AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getOutletNameByIndex(AppGlobal.g_oCurrentLangIndex.get()),
					AppGlobal.g_oFuncStation.get().getCheckPrtqId(), iChosenCheckPfmtId, false,
					sStoredProcessingCheckKey, bIsOldCheck);
			
			if(!sErrorMsg.equals("")) {
				m_oFuncCheck.unlockTable(false, false);
				return sErrorMsg;
			}
		}
		else {
			//Quit without send check if it is new check and no new item
			m_sLastErrorCode = "-32032";
			sErrorMsg = AppGlobal.g_oLang.get()._("no_new_item_ordered");
			m_oFuncCheck.unlockTable(false, false);
			return sErrorMsg;
		}
		
		return "";
	}
	
	//Process send Check
	private String processSendCheck(boolean bPrintGuestCheck, boolean bPayCheck, int iOutletId, String sOutletName, int iCheckPrintQueueId, int iCheckFormatId, boolean bDetailCheck, String sStoredProcessingCheckKey, boolean bIsOldCheck) {
		String sErrorMsg = "";
		
		//Send check
		String sOrderingMode = PosCheck.ORDERING_MODE_FINE_DINING;
		
		// Calculate the Loyalty transaction
		m_oFuncCheck.calculateLoyaltyTransaction();
		
		if(!m_oFuncCheck.sendCheck(bPrintGuestCheck, bPayCheck, false, iOutletId, sOutletName, iCheckPrintQueueId, iCheckFormatId, bDetailCheck, 0, 0, 0, sOrderingMode, false, false).equals(PosCheck.API_RESULT_SUCCESS)) {
			//Fail to send check
			//Write application log
			m_sLastErrorCode = "-32037";
			sErrorMsg = m_oFuncCheck.getLastErrorMessage();
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "ERROR: Fail to send check, table " + m_oFuncCheck.getTableNo() + m_oFuncCheck.getTableExtension());
			return sErrorMsg;
		}
		else {
			//Send success
			//Write application log
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Finish send check, table " + m_oFuncCheck.getTableNo() + m_oFuncCheck.getTableExtension());
		}
		
		//Finish send check, remove process to stored processing check list
		this.removeProcessCheck(sStoredProcessingCheckKey);
		
		return "";
	}
	
	
	// Quit check
	private void quitCheck(){
		// Add log to action log list
		AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.cancel.name(), PosActionLog.ACTION_RESULT_SUCCESS, m_oFuncCheck.getTableNoWithExtensionForDisplay(), AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), m_oFuncCheck.getCheckId(), "", "", "", "", "");
		//handle action log
		AppGlobal.g_oActionLog.get().handleActionLog(false);
		
		if(m_oFuncCheck.isOldCheck()) {
			m_oFuncCheck.removeAllNewItemFormItemList(true, true);
			
			// Mix and match function
			mixAndMatchFunction();
			
			this.calculateCheck();
		}else {
			m_oFuncCheck.updateItemCountForQuitNewCheck();
			m_oFuncCheck.updateCouponItemStatusForQuitNewCheck();
		}
		
		// Remove un-necessary check extra info
		if(m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_PREORDER, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0))
			m_oFuncCheck.removeCheckExtraInfoFromList(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PREORDER, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER);
		
		// Add the process to stored list
		String sStoredProcessingCheckKey = m_oFuncCheck.getTableNoWithExtensionForDisplay();
		this.addProcessCheck(sStoredProcessingCheckKey);
		
		// Unlock the table
		// *****************************************************************
		// Create thread to unlock table
		AppThreadManager oAppThreadManager = new AppThreadManager();

		// Add the method to the thread manager
		// Thread 1 : Unlock table
		Object[] oParameters = new Object[2];
		oParameters[0] = m_oFuncCheck;
		oParameters[1] = sStoredProcessingCheckKey;
		oAppThreadManager.addThread(1, this, "processQuitCheck", oParameters);
		
		oAppThreadManager.runThread();
		
		//For not partial send check, the check is send to database but unable to back to floorplan. Renew the
		//m_oFuncCheck so that the user will not change the check content unexpectedly
		m_oFuncCheck = new FuncCheck();
	}
		
	// Thread for process quit check
	private void processQuitCheck(FuncCheck oFuncCheck, String sStoredProcessingCheckKey){
		try{
			oFuncCheck.unlockTable(true, true);
		}catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
		
		// Finish quit check, remove process to stored processing check list
		this.removeProcessCheck(sStoredProcessingCheckKey);
		
		// Generate customer display interface files for Payment
		oFuncCheck.updateCustomerDisplayDataUpdateTimestamp();
		oFuncCheck.generateCustomerDisplayInterfaceFiles(3);
	}
	
	//Guest check preview
	private JSONObject guestCheckPreview(int iLangIdx) {
		String sCheckUrl = "";
		JSONObject oPreviewJSONObject = null;
		
		if(!m_oFuncCheck.isOldCheck()) {
			m_sLastErrorCode = "-32038";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_preview_new_check");
			return null;
		}

		// if check items are modified, cannot review check
		if(m_oFuncCheck.isModified()) {
			m_sLastErrorCode = "-32039";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_preview_modified_check");
			return null;
		}

		//Get detail check format
		int iChosenCheckPfmtId = 0;
		if(iLangIdx > 0 && iLangIdx <= 5) {
			iChosenCheckPfmtId = AppGlobal.g_oFuncStation.get().getStation().getCheckPfmtId(iLangIdx);
			if(iChosenCheckPfmtId <= 0) {
				m_sLastErrorCode = "-32040";
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_print_format_is_defined");
				return null;
			}
		}
		
		//Get guest check for preview
		oPreviewJSONObject = m_oFuncCheck.previewGuestCheck(iChosenCheckPfmtId, false);
		sCheckUrl = oPreviewJSONObject.optString("url", "");
		if(sCheckUrl.length() == 0) {
			m_sLastErrorCode = "-32041";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_create_guest_check_image");
			return null;
		}
		
		//Unlock table
		m_oFuncCheck.unlockTable(false, false);
		
		// Add log to action log list
		AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.guest_check_preview.name(), PosActionLog.ACTION_RESULT_SUCCESS, m_oFuncCheck.getTableNoWithExtensionForDisplay(), AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), m_oFuncCheck.getCheckId(), "", "", "", "", "");
		//handle action log
		AppGlobal.g_oActionLog.get().handleActionLog(false);
		
		return oPreviewJSONObject;
	}
	
	//get soldout list
	private ArrayList<Object> getOutletItemStockList(boolean bItemCount, boolean bSoldout) {
		ArrayList<Object> oResultArrayList = new ArrayList<Object>();
		
		//Get the items that with stock
		PosOutletItemList oOutletItemStockList = new PosOutletItemList();
		oOutletItemStockList.readOutletItemListByCheckStockSoldout(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosOutletItem.CHECK_STOCK_YES, PosOutletItem.SOLDOUT_YES);
		
		if(bItemCount && !bSoldout) {
			//Return Item count item list
			for(PosOutletItem oOutletItem : oOutletItemStockList.getOutletItemList()) {
				if(!oOutletItem.getSoldout().equals("s") && oOutletItem.getStockQty().compareTo(new BigDecimal(0.0)) <= 0) {
					HashMap <String, Object> oTempHashMap = new HashMap<String, Object>();
					oTempHashMap.put("id", Integer.toString(oOutletItem.getItemId()));
					oTempHashMap.put("count", oOutletItem.getStockQty());
					
					oResultArrayList.add(oTempHashMap);
				}
			}
		}
		else if(bSoldout && !bItemCount) {
			//Return sold out item list
			for(PosOutletItem oOutletItem : oOutletItemStockList.getOutletItemList()) {
				if(oOutletItem.getSoldout().equals("s")) {
					HashMap<String, String> oTempHashMap = new HashMap<String, String>();
					oTempHashMap.put("id", Integer.toString(oOutletItem.getItemId()));
					MenuItem oMenuItem = new MenuItem();
					// Read menu item from OM
					if(!oMenuItem.readById(oOutletItem.getItemId(), AppGlobal.g_oFuncOutlet.get().getOutletId()))
						oTempHashMap.put("code", "");
					else
						oTempHashMap.put("code", oMenuItem.getCode());
					oResultArrayList.add(oTempHashMap);
				}
			}
		}
		
		return oResultArrayList;
	}
	
	//get check details
	private String getCheckDetail(String sTableNo, String sTableExtension, String sCheckNo) {
		String sErrorMsg = "";
		PosCheck oPosCheck = new PosCheck();
		JSONObject oResult = null;
		
		JSONObject oResultSONObject = new JSONObject(), oTaxJSONObject = null, oItemJSONObject = null;
		JSONObject oCourseJSONObject = null, oModifyJSONObject = null, oChildItemJSONObject = null;
		JSONObject oDiscountJSONObject = null, oEmployeeJSONObject = null, oPaymentJSONObject = null, oMemberJSONObject = null;
		JSONObject oExtraInfoJSONObject = null;
		JSONArray oTaxArray = new JSONArray(), oItemArray = new JSONArray(), oModifyArray = new JSONArray();
		JSONArray oChildItemArray = new JSONArray(), oDiscountArray = new JSONArray(), oExtraChargeArray = new JSONArray(), oPaymentArray = new JSONArray();
		JSONArray oCheckRefArray = new JSONArray();
		if(!sTableNo.isEmpty() || !sTableExtension.isEmpty()) {
			oPosCheck = new PosCheck(AppGlobal.g_oFuncUser.get().getUserId());
			oResult = oPosCheck.getCheckDetailByTable(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBdayId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), Integer.parseInt(sTableNo), sTableExtension, 1);
			if(oResult == null){
				m_sLastErrorCode = "-32043";
				sErrorMsg = AppGlobal.g_oLang.get()._("table_is_not_exist");
				
				return sErrorMsg;
			}
		}
		else if(!sCheckNo.isEmpty()) {
			oResult = oPosCheck.getCheckDetailByCheckNo(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), sCheckNo, 1, false);
			if (oResult == null) {
				m_sLastErrorCode = "-32044";
				sErrorMsg = AppGlobal.g_oLang.get()._("check_is_not_found");
				return sErrorMsg;
			}
		}
		else{
			m_sLastErrorCode = "-32044";
			sErrorMsg = AppGlobal.g_oLang.get()._("check_is_not_found");
			return sErrorMsg;
		}
		DateTime oCurrentDateTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMddHHmmss");
		String sDateTime = dateFormat.print(oCurrentDateTime);
		boolean bHavePendingItem = false;
		
		try {
			oResultSONObject.put("time", sDateTime);
			oResultSONObject.put("outletcode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
			
			// Table information
			PosCheckTable oCheckTable = new PosCheckTable(oResult);
			if(oCheckTable.getTable() == 0)
				oResultSONObject.put("tableno", oCheckTable.getTableExt());
			else
				oResultSONObject.put("tableno", Integer.toString(oCheckTable.getTable()) + oCheckTable.getTableExt());
			String sTableName = "";
			if(AppGlobal.g_oFuncOutlet.get().isTableNameExist(Integer.toString(oCheckTable.getTable()), oCheckTable.getTableExt())){
				HashMap<String, String> oTableName = new HashMap<String, String>();
				oTableName = AppGlobal.g_oFuncOutlet.get().getTableNameInAllLang(Integer.toString(oCheckTable.getTable()), oCheckTable.getTableExt());
				sTableName = oTableName.get("tableName");
			}
			oResultSONObject.put("tablename", sTableName);
			oResultSONObject.put("checkno", oPosCheck.getCheckPrefixNo());
			oResultSONObject.put("cover", Integer.toString(oPosCheck.getGuests()));
			
			// Open employee's detail
			if(oResult.has("OpenCheckUser")){
				UserUser oUser = new UserUser(oResult.getJSONObject("OpenCheckUser"));
				oResultSONObject.put("openemployeecode", oUser.getNumber());
				oResultSONObject.put("openemployeename", oUser.getFirstName(AppGlobal.g_oCurrentLangIndex.get())+" "+oUser.getLastName(AppGlobal.g_oCurrentLangIndex.get()));
			}
			
			// Station Information
			if(oResult.has("OpenStationDetail")){
				PosStation oPosStation = new PosStation(oResult.getJSONObject("OpenStationDetail"));
				oResultSONObject.put("openstationcode", oPosStation.getCode());
			}
			
			// Check time information
			if(oPosCheck.getOpenTime() != null && !oPosCheck.getOpenTime().equals(""))
				oResultSONObject.put("opentime", oPosCheck.getOpenTime().substring(oPosCheck.getOpenTime().length() - 8));
			if(oPosCheck.getCloseTime() != null && !oPosCheck.getCloseTime().equals(""))
				oResultSONObject.put("closetime", oPosCheck.getCloseTime().substring(oPosCheck.getCloseTime().length() - 8));
			if(oPosCheck.getVoidTime() != null && !oPosCheck.getVoidTime().equals(""))
				oResultSONObject.put("voidtime", oPosCheck.getVoidTime().substring(oPosCheck.getVoidTime().length() - 8));
			
			BigDecimal dServiceChargeTotal = BigDecimal.ZERO;
			for(int i=1; i<=5; i++)
				dServiceChargeTotal = dServiceChargeTotal.add(oPosCheck.getSc(i));
			oResultSONObject.put("servicechargetotal", dServiceChargeTotal.toString());
			
			// Tax information
			oTaxArray = new JSONArray();
			for(int i=1; i<=25; i++){
				if(oPosCheck.getTax(i).compareTo(BigDecimal.ZERO) != 0){
					oTaxJSONObject = new JSONObject();
					oTaxJSONObject.put("index", Integer.toString(i));
					oTaxJSONObject.put("name", AppGlobal.g_oLang.get()._("tax")+" "+Integer.toString(i));
					oTaxJSONObject.put("amount", oPosCheck.getTax(i).toString());
					oTaxArray.put(oTaxJSONObject);
				}
			}
			if(oTaxArray.length()>0)
				oResultSONObject.put("taxes", oTaxArray);
			
			oResultSONObject.put("checktotal", oPosCheck.getCheckTotal().toString());
			
			// Check Item information
			oItemArray = new JSONArray();
			PosCheckItemList oCheckItemList = new PosCheckItemList(oResult);
			for(PosCheckItem oPosCheckItem:oCheckItemList.getCheckItemList()){
				if(oPosCheckItem.isDeleted())
					continue;
				
				if(oPosCheckItem.isPendingItem())
					bHavePendingItem = true;
				
				oItemJSONObject = new JSONObject();
				oItemJSONObject.put("code", oPosCheckItem.getCode());
				oItemJSONObject.put("name", oPosCheckItem.getName(AppGlobal.g_oCurrentLangIndex.get()));
				oItemJSONObject.put("qty", oPosCheckItem.getQty().toString());
				oItemJSONObject.put("unitprice", oPosCheckItem.getPrice().toString());
				oItemJSONObject.put("total", oPosCheckItem.getTotal());
				if(oPosCheckItem.getOrderingType().equals(PosCheckItem.ORDERING_TYPE_TAKEOUT))
					oItemJSONObject.put("takeaway", "true");
				else if(oPosCheckItem.getOrderingType().equals(PosCheckItem.ORDERING_TYPE_FINE_DINING))
					oItemJSONObject.put("takeaway", "false");
				oItemJSONObject.put("seatnumber", Integer.toString(oPosCheckItem.getSeatNo()));
				oItemJSONObject.put("sequence", Integer.toString(oPosCheckItem.getSeq() - 1));
				
				// Item Course Information
				if(oResult.has("MenuItemCourse")){
					MenuItemCourseList oMenuItemCourseList = new MenuItemCourseList(oResult.getJSONArray("MenuItemCourse"));
					if(!oMenuItemCourseList.getItemCourseList().isEmpty()){
						for(MenuItemCourse oMenuItemCourse:oMenuItemCourseList.getItemCourseList()){
							if(oMenuItemCourse.getIcouId() == oPosCheckItem.getCourseId()){
								oCourseJSONObject = new JSONObject();
								oCourseJSONObject.put("code", oMenuItemCourse.getCode());
								oCourseJSONObject.put("name", oMenuItemCourse.getName(AppGlobal.g_oCurrentLangIndex.get()));
								oItemJSONObject.put("course", oCourseJSONObject);
								break;
							}
						}
					}
				}
				
				// Item Modify information
				oModifyArray = new JSONArray();
				for(PosCheckItem oModifier:oPosCheckItem.getModifierList()){
					if(oModifier.isDeleted())
						continue;
					
					oModifyJSONObject = new JSONObject();
					oModifyJSONObject.put("code", oModifier.getCode());
					oModifyJSONObject.put("name", oModifier.getName(AppGlobal.g_oCurrentLangIndex.get()));
					oModifyJSONObject.put("qty", oModifier.getQty());
					oModifyJSONObject.put("total", oModifier.getTotal());
					oModifyArray.put(oModifyJSONObject);
				}
				if(oModifyArray.length()>0)
					oItemJSONObject.put("modifiers", oModifyArray);
				
				// Item child item information
				oChildItemArray = new JSONArray();
				for(PosCheckItem oChildItem:oPosCheckItem.getChildItemList()){
					if(oChildItem.isDeleted())
						continue;
					
					if(oChildItem.isPendingItem())
						bHavePendingItem = true;
					
					oChildItemJSONObject = new JSONObject();
					oChildItemJSONObject.put("code", oChildItem.getCode());
					oChildItemJSONObject.put("name", oChildItem.getName(AppGlobal.g_oCurrentLangIndex.get()));
					oChildItemJSONObject.put("qty", oChildItem.getQty());
					oChildItemJSONObject.put("unitprice", oChildItem.getUnitCost());
					oChildItemJSONObject.put("total", oChildItem.getTotal());
					
					// Child Item Modify information
					oModifyArray = new JSONArray();
					for(PosCheckItem oModifier:oChildItem.getModifierList()){
						oModifyJSONObject = new JSONObject();
						oModifyJSONObject.put("code", oModifier.getCode());
						oModifyJSONObject.put("name", oModifier.getName(AppGlobal.g_oCurrentLangIndex.get()));
						oModifyJSONObject.put("qty", oModifier.getQty());
						oModifyJSONObject.put("total", oModifier.getTotal());
						oModifyArray.put(oModifyJSONObject);
					}
					if(oModifyArray.length()>0)
						oChildItemJSONObject.put("modifiers", oModifyArray);
					
					oChildItemArray.put(oChildItemJSONObject);
				}
				if(oChildItemArray.length()>0)
					oItemJSONObject.put("childitems", oChildItemArray);
				
				// Item discount information
				oDiscountArray = new JSONArray();
				for(PosCheckDiscount oDiscount:oPosCheckItem.getItemDiscountList()){
					if(oDiscount.isDeleted())
						continue;
					
					oDiscountJSONObject = new JSONObject();
					PosDiscountType oPosDiscountType = new PosDiscountType();
					if(oResult.has("DiscountType")){
						for( int i = 0; i < oResult.getJSONArray("DiscountType").length(); i++){
							oPosDiscountType = new PosDiscountType(oResult.getJSONArray("DiscountType").getJSONObject(i));
							if(oPosDiscountType.getDtypId() != oDiscount.getDtypId())
								continue;
							else if (oPosDiscountType.getDtypId() == oDiscount.getDtypId())
								break;
						}
						oDiscountJSONObject.put("code", oPosDiscountType.getCode());
					}
					
					oDiscountJSONObject.put("name", oDiscount.getName(AppGlobal.g_oCurrentLangIndex.get()));
					oDiscountJSONObject.put("amount", oDiscount.getTotal());
					if(oDiscount.getMethod().equals(PosCheckDiscount.METHOD_FIX_AMOUNT_DISCOUNT_PER_ITEM) || oDiscount.getMethod().equals(PosCheckDiscount.METHOD_FIX_AMOUNT_DISCOUNT_PER_CHECK))
						oDiscountJSONObject.put("type", "amount");
					else if (oDiscount.getMethod().equals(PosCheckDiscount.METHOD_PERCENTAGE_DISCOUNT)){
						oDiscountJSONObject.put("type", "percentage");
						oDiscountJSONObject.put("rate", oDiscount.getRate().toString());
					}
					oDiscountArray.put(oDiscountJSONObject);
				}
				if(oDiscountArray.length()>0)
					oItemJSONObject.put("discounts", oDiscountArray);
				
				// Order information
				oEmployeeJSONObject = new JSONObject();
				if(oResult.has("OrderItemUser")){
					UserUser oUserUser = new UserUser();
					for( int i = 0; i < oResult.getJSONArray("OrderItemUser").length(); i++){
						oUserUser = new UserUser(oResult.getJSONArray("OrderItemUser").getJSONObject(i));
						if(oUserUser.getUserId() != oPosCheckItem.getOrderUserId())
							continue;
						else if (oUserUser.getUserId()  == oPosCheckItem.getOrderUserId())
							break;
					}
					oEmployeeJSONObject.put("code", oUserUser.getNumber());
					oEmployeeJSONObject.put("name", oUserUser.getFirstName(AppGlobal.g_oCurrentLangIndex.get())+" "+oUserUser.getLastName(AppGlobal.g_oCurrentLangIndex.get()));
				}
				
				oItemJSONObject.put("employee", oEmployeeJSONObject);
				oItemJSONObject.put("ordertime", oPosCheckItem.getOrderLocTime().toString("HH:mm:ss"));
				
				oItemArray.put(oItemJSONObject);
			}
			if(oItemArray.length()>0)
				oResultSONObject.put("items", oItemArray);
			
			//Check discount information
			oDiscountArray = new JSONArray();
			oExtraChargeArray = new JSONArray();
			for(PosCheckParty oParty : oPosCheck.getCheckPartiesArrayList()) {
				for(PosCheckDiscount oCheckDiscount:oParty.getCheckDiscountList()){
					if(oCheckDiscount.isDeleted())
						continue;
					
					oDiscountJSONObject = new JSONObject();
					PosDiscountType oPosDiscountType = new PosDiscountType();
					
					if(oResult.has("DiscountType")){
						for( int i = 0; i < oResult.getJSONArray("DiscountType").length(); i++){
							oPosDiscountType = new PosDiscountType(oResult.getJSONArray("DiscountType").getJSONObject(i));
							if(oPosDiscountType.getDtypId() != oCheckDiscount.getDtypId())
								continue;
							else if (oPosDiscountType.getDtypId() == oCheckDiscount.getDtypId())
								break;
						}
						oDiscountJSONObject.put("code", oPosDiscountType.getCode());
					}
					
					oDiscountJSONObject.put("name", oCheckDiscount.getName(AppGlobal.g_oCurrentLangIndex.get()));
					oDiscountJSONObject.put("amount", oCheckDiscount.getTotal());
					if(oCheckDiscount.getMethod().equals(PosCheckDiscount.METHOD_FIX_AMOUNT_DISCOUNT_PER_ITEM) || oCheckDiscount.getMethod().equals(PosCheckDiscount.METHOD_FIX_AMOUNT_DISCOUNT_PER_CHECK))
						oDiscountJSONObject.put("type", "amount");
					else if (oCheckDiscount.getMethod().equals(PosCheckDiscount.METHOD_PERCENTAGE_DISCOUNT)){
						oDiscountJSONObject.put("type", "percentage");
						oDiscountJSONObject.put("rate", oCheckDiscount.getRate().toString());
					}
					if(oCheckDiscount.isUsedForDiscount())
						oDiscountArray.put(oDiscountJSONObject);
					else
						oExtraChargeArray.put(oDiscountJSONObject);
				}
				if(oDiscountArray.length()>0)
					oResultSONObject.put("discounts", oDiscountArray);
				if(oExtraChargeArray.length()>0)
					oResultSONObject.put("extracharges", oExtraChargeArray);
			}
			
			//Check references
			JSONArray oTempExtraInfoArray = oResult.optJSONArray("PosCheckExtraInfo");
			if(oTempExtraInfoArray != null && oTempExtraInfoArray.length() > 0) {
				PosCheckExtraInfoList oCheckLevExtraInfoList = new PosCheckExtraInfoList(oTempExtraInfoArray);
				oCheckRefArray = new JSONArray();
				
				for(PosCheckExtraInfo oCheckLevExtraInfo : oCheckLevExtraInfoList.getCheckExtraInfoList()){
					if(oCheckLevExtraInfo != null 
							&& oCheckLevExtraInfo.equalToBySectionIndexVariable(PosCheckExtraInfo.BY_CHECK, "", 0, PosCheckExtraInfo.VARIABLE_CHECK_INFO)) {
						oExtraInfoJSONObject = new JSONObject();
						oExtraInfoJSONObject.put("index", oCheckLevExtraInfo.getIndex());
						oExtraInfoJSONObject.put("value", oCheckLevExtraInfo.getValue());
						oCheckRefArray.put(oExtraInfoJSONObject);
					}
				}
				if(oCheckRefArray.length() > 0)
					oResultSONObject.put("references", oCheckRefArray);
			}
			
			//Check cardNumber
			JSONArray oTempCardNoArray = oResult.optJSONArray("PosCheckExtraInfo");
			if(oTempCardNoArray != null && oTempCardNoArray.length() > 0) {
				PosCheckExtraInfoList oCheckLevExtraInfoList = new PosCheckExtraInfoList(oTempCardNoArray);
				
				for(PosCheckExtraInfo oCheckLevExtraInfo : oCheckLevExtraInfoList.getCheckExtraInfoList()){
					if(oCheckLevExtraInfo != null && 
							oCheckLevExtraInfo.equalToBySectionIndexVariable(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, 0, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER)) {
						try {
							JSONObject oMemberJsonObject = new JSONObject(oCheckLevExtraInfo.getValue());
							if (oMemberJsonObject.has("cardnumber")
									&& !oMemberJsonObject.optString("cardnumber").isEmpty()) {
								oResultSONObject.put("cardnumber", oMemberJsonObject.get("cardnumber"));
							}
						} catch (JSONException e) {
						}
						break;
					}
				}
			}
			
			//Check payment information
			oPaymentArray = new JSONArray();
			PosPaymentMethodList oPosPaymentMethodList = new PosPaymentMethodList();
			PosBusinessDay oBusinessDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay();
			DateTimeFormatter dateFormatForpayment = DateTimeFormat.forPattern("yyyy-MM-dd");
			BigDecimal dPaymentTotal = BigDecimal.ZERO;
			oPosPaymentMethodList.readAllWithAccessControl(AppGlobal.g_oFuncStation.get().getStationId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), dateFormatForpayment.print(oBusinessDay.getDate()), oBusinessDay.isHoliday(), oBusinessDay.isDayBeforeHoliday(), oBusinessDay.isSpecialDay(), oBusinessDay.isDayBeforeSpecialDay(), oBusinessDay.getDayOfWeek());
			
			for(PosCheckPayment oCheckPayment:oPosCheck.getCheckPaymentArrayList()){
				if(oCheckPayment.isDelete())
					continue;
				oPaymentJSONObject = new JSONObject();
				
				PosPaymentMethod oPosPaymentMethod = oPosPaymentMethodList.getPaymentMethod(oCheckPayment.getPaymentMethodId());
				oPaymentJSONObject.put("code", oPosPaymentMethod.getPaymentCode());
				oPaymentJSONObject.put("name", oPosPaymentMethod.getName(AppGlobal.g_oCurrentLangIndex.get()));
				oPaymentJSONObject.put("amount", oCheckPayment.getPayTotal());
				oPaymentJSONObject.put("tips", oCheckPayment.getPayTips());
				oPaymentJSONObject.put("surchargeamount", oCheckPayment.getSurcharge());
				oPaymentJSONObject.put("seq", oCheckPayment.getPaySeq());
				
				JSONArray oPayRefData = oCheckPayment.getRefDataInJson(1);
				if(oPayRefData.length() > 0)
					oPaymentJSONObject.put("references", oPayRefData);
				
				oPaymentArray.put(oPaymentJSONObject);
				
				dPaymentTotal = dPaymentTotal.add(oCheckPayment.getPayTotal());
				
				if(oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_CARD_NO, 0) != null && !oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_CARD_NO, 0).getValue().isEmpty())
					oPaymentJSONObject.put("cardnumber", oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_CARD_NO, 0).getValue());
			}
			if(oPaymentArray.length()>0)
				oResultSONObject.put("payments", oPaymentArray);
			oResultSONObject.put("paymenttotal", dPaymentTotal);
			
			//ready to pay
			if(oPaymentArray.length() > 0 || bHavePendingItem)
				oResultSONObject.put("readyToPay", "0");
			else {
				if(AppGlobal.g_oFuncStation.get().isSkipPrintCheckForPayment())
					oResultSONObject.put("readyToPay", "1");
				else {
					if(oResult.has("OthersInfo") && oResult.optJSONObject("OthersInfo").has("isPrinted") && oResult.optJSONObject("OthersInfo").optInt("isPrinted", 0) == 1)
						oResultSONObject.put("readyToPay", "1");
					else
						oResultSONObject.put("readyToPay", "0");
				}
			}
			
			// Member information
			if(oPosCheck.getMemberId() > 0){
				oMemberJSONObject = new JSONObject();
				MemMember oMember = new MemMember();
				oMember.readById(oPosCheck.getMemberId());
				oMemberJSONObject.put("number", oMember.getCardNumber());
				oMemberJSONObject.put("name", oMember.getName());
				oResultSONObject.put("member", oMemberJSONObject);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return oResultSONObject.toString();
	}
	
	private JSONObject fromCheckDetailResponse() {
		JSONObject oResultSONObject = new JSONObject(), oTaxJSONObject = null, oItemJSONObject = null;
		JSONObject oCourseJSONObject = null, oModifyJSONObject = null, oChildItemJSONObject = null;
		JSONObject oDiscountJSONObject = null, oEmployeeJSONObject = null, oPaymentJSONObject = null, oMemberJSONObject = null;
		JSONObject oExtraInfoJSONObject = null;
		JSONArray oTaxArray = new JSONArray(), oItemArray = new JSONArray(), oModifyArray = new JSONArray();
		JSONArray oChildItemArray = new JSONArray(), oDiscountArray = new JSONArray(), oExtraChargeArray = new JSONArray(), oPaymentArray = new JSONArray();
		JSONArray oCheckRefArray = new JSONArray();	
		
		if(m_oFuncCheck == null)
			return null;
		
		DateTime oCurrentDateTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMddHHmmss");
		DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm:ss");
		String sDateTime = dateFormat.print(oCurrentDateTime);
		
		try {
			oResultSONObject.put("time", sDateTime);
			oResultSONObject.put("outletcode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
			
			// Table information
			oResultSONObject.put("tableno", m_oFuncCheck.getTableNoWithExtensionForDisplay());
			oResultSONObject.put("tablename", m_oFuncCheck.getTableName()[AppGlobal.g_oCurrentLangIndex.get()-1]);
			oResultSONObject.put("checkno", m_oFuncCheck.getCheckPrefixNo());
			oResultSONObject.put("cover", Integer.toString(m_oFuncCheck.getCover()));
			
			UserUser oAppliedUser = null;
			if(m_iAppliedUserId != 0) {
				oAppliedUser = new UserUser();
				oAppliedUser.readByUserId(m_iAppliedUserId);
			}else
				oAppliedUser = AppGlobal.g_oFuncUser.get().getUser();
			
			// Open employee's detail
			oResultSONObject.put("openemployeecode", oAppliedUser.getNumber());
			oResultSONObject.put("openemployeename", oAppliedUser.getFirstName(AppGlobal.g_oCurrentLangIndex.get())+" "+oAppliedUser.getLastName(AppGlobal.g_oCurrentLangIndex.get()));
			
			// Station Information
			oResultSONObject.put("openstationcode", AppGlobal.g_oFuncStation.get().getCode());
			
			// Check time information
			if(m_oFuncCheck.getOpenLocTime() != null)
				oResultSONObject.put("opentime", timeFormatter.print(m_oFuncCheck.getOpenLocTime()));
			if(m_oFuncCheck.getCloseLocTime() != null)
				oResultSONObject.put("closetime", timeFormatter.print(m_oFuncCheck.getCloseLocTime()));
			
			oResultSONObject.put("servicechargetotal", m_oFuncCheck.getServiceChargeTotal().toPlainString());
			
			// Tax information
			oTaxArray = new JSONArray();
			for(int i=1; i<=25; i++){
				if(m_oFuncCheck.getTaxTotal(i).compareTo(BigDecimal.ZERO) != 0){
					oTaxJSONObject = new JSONObject();
					oTaxJSONObject.put("index", Integer.toString(i));
					oTaxJSONObject.put("name", AppGlobal.g_oLang.get()._("tax")+" "+Integer.toString(i));
					oTaxJSONObject.put("amount", m_oFuncCheck.getTaxTotal(i).toPlainString());
					oTaxArray.put(oTaxJSONObject);
				}
			}
			if(oTaxArray.length()>0)
				oResultSONObject.put("taxes", oTaxArray);
			
			oResultSONObject.put("checktotal", m_oFuncCheck.getCheckTotal().toPlainString());
			
			// Check Item information
			oItemArray = new JSONArray();
			MenuItemCourseList oMenuItemCourseList = new MenuItemCourseList();
			oMenuItemCourseList.readItemCourseList();
			for (FuncCheckParty oFuncCheckParty : m_oFuncCheck.getCheckPartyList()) {
				for (List<FuncCheckItem> oItemListForSingleSeat : oFuncCheckParty.getWholeItemList()) {
					for (FuncCheckItem oFuncCheckItem : oItemListForSingleSeat) {
						PosCheckItem oPosCheckItem = oFuncCheckItem.getCheckItem();
						if(oFuncCheckItem.isSetMenuItem() || oPosCheckItem.isDeleted())
							continue;
						
						oItemJSONObject = new JSONObject();
						oItemJSONObject.put("code", oPosCheckItem.getCode());
						oItemJSONObject.put("name", oPosCheckItem.getName(AppGlobal.g_oCurrentLangIndex.get()));
						oItemJSONObject.put("qty", oPosCheckItem.getQty().toString());
						oItemJSONObject.put("unitprice", oPosCheckItem.getPrice().toString());
						oItemJSONObject.put("total", oPosCheckItem.getTotal());
						if(oPosCheckItem.getOrderingType().equals(PosCheckItem.ORDERING_TYPE_TAKEOUT))
							oItemJSONObject.put("takeaway", "true");
						else if(oPosCheckItem.getOrderingType().equals(PosCheckItem.ORDERING_TYPE_FINE_DINING))
							oItemJSONObject.put("takeaway", "false");
						oItemJSONObject.put("seatnumber", Integer.toString(oPosCheckItem.getSeatNo()));
						oItemJSONObject.put("sequence", Integer.toString(oPosCheckItem.getSeq() - 1));
				
						// Item Course Information
						if(oPosCheckItem.getCourseId() > 0 && !oMenuItemCourseList.getItemCourseList().isEmpty()){
							for(MenuItemCourse oMenuItemCourse:oMenuItemCourseList.getItemCourseList()){
								if(oMenuItemCourse.getIcouId() == oPosCheckItem.getCourseId()){
									oCourseJSONObject = new JSONObject();
									oCourseJSONObject.put("code", oMenuItemCourse.getCode());
									oCourseJSONObject.put("name", oMenuItemCourse.getName(AppGlobal.g_oCurrentLangIndex.get()));
									oItemJSONObject.put("course", oCourseJSONObject);
									break;
								}
							}
						}
				
						// Item Modify information
						oModifyArray = new JSONArray();
						for(PosCheckItem oModifier:oPosCheckItem.getModifierList()){
							if(oModifier.isDeleted())
								continue;
							
							oModifyJSONObject = new JSONObject();
							oModifyJSONObject.put("code", oModifier.getCode());
							oModifyJSONObject.put("name", oModifier.getName(AppGlobal.g_oCurrentLangIndex.get()));
							oModifyJSONObject.put("qty", oModifier.getQty());
							oModifyJSONObject.put("total", oModifier.getTotal());
							oModifyArray.put(oModifyJSONObject);
						}
						if(oModifyArray.length()>0)
							oItemJSONObject.put("modifiers", oModifyArray);
						
						// Item child item information
						if(oFuncCheckItem.isSetMenu()) {
							oChildItemArray = new JSONArray();
							for(FuncCheckItem oFuncChildItem:oFuncCheckItem.getChildItemList()){
								PosCheckItem oChildItem = oFuncChildItem.getCheckItem();
								if(oChildItem.isDeleted())
									continue;
								
								oChildItemJSONObject = new JSONObject();
								oChildItemJSONObject.put("code", oChildItem.getCode());
								oChildItemJSONObject.put("name", oChildItem.getName(AppGlobal.g_oCurrentLangIndex.get()));
								oChildItemJSONObject.put("qty", oChildItem.getQty());
								oChildItemJSONObject.put("unitprice", oChildItem.getUnitCost());
								oChildItemJSONObject.put("total", oChildItem.getTotal());
								
								// Child Item Modify information
								oModifyArray = new JSONArray();
								for(PosCheckItem oModifier:oChildItem.getModifierList()){
									oModifyJSONObject = new JSONObject();
									oModifyJSONObject.put("code", oModifier.getCode());
									oModifyJSONObject.put("name", oModifier.getName(AppGlobal.g_oCurrentLangIndex.get()));
									oModifyJSONObject.put("qty", oModifier.getQty());
									oModifyJSONObject.put("total", oModifier.getTotal());
									oModifyArray.put(oModifyJSONObject);
								}
								if(oModifyArray.length()>0)
									oChildItemJSONObject.put("modifiers", oModifyArray);
								
								oChildItemArray.put(oChildItemJSONObject);
							}
							if(oChildItemArray.length()>0)
								oItemJSONObject.put("childitems", oChildItemArray);
						}
						
						// Item discount information
						oDiscountArray = new JSONArray();
						for(PosCheckDiscount oDiscount:oPosCheckItem.getItemDiscountList()){
							if(oDiscount.isDeleted())
								continue;
							
							
							oDiscountJSONObject = new JSONObject();
							PosDiscountType oPosDiscountType = new PosDiscountType();
							oPosDiscountType.readById(oDiscount.getDtypId());
							if(oPosDiscountType != null)
								oDiscountJSONObject.put("code", oPosDiscountType.getCode());
							
							oDiscountJSONObject.put("name", oDiscount.getName(AppGlobal.g_oCurrentLangIndex.get()));
							oDiscountJSONObject.put("amount", oDiscount.getTotal());
							if(oDiscount.getMethod().equals(PosCheckDiscount.METHOD_FIX_AMOUNT_DISCOUNT_PER_ITEM) || oDiscount.getMethod().equals(PosCheckDiscount.METHOD_FIX_AMOUNT_DISCOUNT_PER_CHECK))
								oDiscountJSONObject.put("type", "amount");
							else if (oDiscount.getMethod().equals(PosCheckDiscount.METHOD_PERCENTAGE_DISCOUNT)){
								oDiscountJSONObject.put("type", "percentage");
								oDiscountJSONObject.put("rate", oDiscount.getRate().toString());
							}
							oDiscountArray.put(oDiscountJSONObject);
						}
						if(oDiscountArray.length()>0)
							oItemJSONObject.put("discounts", oDiscountArray);
						
						// Order information
						oEmployeeJSONObject = new JSONObject();
						oEmployeeJSONObject.put("code", oAppliedUser.getNumber());
						oEmployeeJSONObject.put("name", oAppliedUser.getFirstName(AppGlobal.g_oCurrentLangIndex.get())+" "+oAppliedUser.getLastName(AppGlobal.g_oCurrentLangIndex.get()));
						
						oItemJSONObject.put("employee", oEmployeeJSONObject);
						oItemJSONObject.put("ordertime", oPosCheckItem.getOrderLocTime().toString("HH:mm:ss"));
						
						oItemArray.put(oItemJSONObject);
					}
				}
			}
			if(oItemArray.length()>0)
				oResultSONObject.put("items", oItemArray);
			
			//Check discount information
			oDiscountArray = new JSONArray();
			oExtraChargeArray = new JSONArray();
			for(PosCheckDiscount oCheckDiscount:m_oFuncCheck.getCheckDiscountList()){
				if(oCheckDiscount.isDeleted())
					continue;
				
				oDiscountJSONObject = new JSONObject();
				PosDiscountType oPosDiscountType = new PosDiscountType();
				oPosDiscountType.readById(oCheckDiscount.getDtypId());
				if(oPosDiscountType != null)
					oDiscountJSONObject.put("code", oPosDiscountType.getCode());
				oDiscountJSONObject.put("name", oCheckDiscount.getName(AppGlobal.g_oCurrentLangIndex.get()));
				oDiscountJSONObject.put("amount", oCheckDiscount.getTotal());
				if(oCheckDiscount.getMethod().equals(PosCheckDiscount.METHOD_FIX_AMOUNT_DISCOUNT_PER_ITEM) || oCheckDiscount.getMethod().equals(PosCheckDiscount.METHOD_FIX_AMOUNT_DISCOUNT_PER_CHECK))
					oDiscountJSONObject.put("type", "amount");
				else if (oCheckDiscount.getMethod().equals(PosCheckDiscount.METHOD_PERCENTAGE_DISCOUNT)){
					oDiscountJSONObject.put("type", "percentage");
					oDiscountJSONObject.put("rate", oCheckDiscount.getRate().toString());
				}
				if(oCheckDiscount.isUsedForDiscount())
					oDiscountArray.put(oDiscountJSONObject);
				else
					oExtraChargeArray.put(oDiscountJSONObject);
			}
			if(oDiscountArray.length()>0)
				oResultSONObject.put("discounts", oDiscountArray);
			if(oExtraChargeArray.length()>0)
				oResultSONObject.put("extracharges", oExtraChargeArray);
			
			//Check references
			oCheckRefArray = new JSONArray();	
			for(PosCheckExtraInfo oCheckLevExtraInfo : m_oFuncCheck.getCheckExtraInfoList()){
				if(oCheckLevExtraInfo != null 
						&& oCheckLevExtraInfo.equalToBySectionIndexVariable(PosCheckExtraInfo.BY_CHECK, "", 0, PosCheckExtraInfo.VARIABLE_CHECK_INFO)) {
					oExtraInfoJSONObject = new JSONObject();
					oExtraInfoJSONObject.put("index", oCheckLevExtraInfo.getIndex());
					oExtraInfoJSONObject.put("value", oCheckLevExtraInfo.getValue());
					oCheckRefArray.put(oExtraInfoJSONObject);
				}
				else if(oCheckLevExtraInfo != null && 
						oCheckLevExtraInfo.equalToBySectionIndexVariable(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, 0, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER)) {
					try {
						JSONObject oMemberJsonObject = new JSONObject(oCheckLevExtraInfo.getValue());
						if (oMemberJsonObject.has("cardnumber")
								&& !oMemberJsonObject.optString("cardnumber").isEmpty()) {
							oResultSONObject.put("cardnumber", oMemberJsonObject.get("cardnumber"));
						}
					} catch (JSONException e) {}
				}
			}
			if(oCheckRefArray.length() > 0)
				oResultSONObject.put("references", oCheckRefArray);			
			
			//Check payment information
			oPaymentArray = new JSONArray();
			PosPaymentMethodList oPosPaymentMethodList = new PosPaymentMethodList();
			PosBusinessDay oBusinessDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay();
			DateTimeFormatter dateFormatForpayment = DateTimeFormat.forPattern("yyyy-MM-dd");
			BigDecimal dPaymentTotal = BigDecimal.ZERO;
			oPosPaymentMethodList.readAllWithAccessControl(AppGlobal.g_oFuncStation.get().getStationId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), dateFormatForpayment.print(oBusinessDay.getDate()), oBusinessDay.isHoliday(), oBusinessDay.isDayBeforeHoliday(), oBusinessDay.isSpecialDay(), oBusinessDay.isDayBeforeSpecialDay(), oBusinessDay.getDayOfWeek());
			
			for(PosCheckPayment oCheckPayment:m_oFuncCheck.getCheckPaymentList()){
				if(oCheckPayment.isDelete())
					continue;
				oPaymentJSONObject = new JSONObject();
				
				PosPaymentMethod oPosPaymentMethod = oPosPaymentMethodList.getPaymentMethod(oCheckPayment.getPaymentMethodId());
				oPaymentJSONObject.put("code", oPosPaymentMethod.getPaymentCode());
				oPaymentJSONObject.put("name", oPosPaymentMethod.getName(AppGlobal.g_oCurrentLangIndex.get()));
				oPaymentJSONObject.put("amount", oCheckPayment.getPayTotal());
				oPaymentJSONObject.put("tips", oCheckPayment.getPayTips());
				oPaymentJSONObject.put("surchargeamount", oCheckPayment.getSurcharge());
				oPaymentJSONObject.put("seq", oCheckPayment.getPaySeq());
				
				JSONArray oPayRefData = oCheckPayment.getRefDataInJson(1);
				if(oPayRefData.length() > 0)
					oPaymentJSONObject.put("references", oPayRefData);
				
				oPaymentArray.put(oPaymentJSONObject);
				
				dPaymentTotal = dPaymentTotal.add(oCheckPayment.getPayTotal());
				
				if(oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_CARD_NO, 0) != null && !oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_CARD_NO, 0).getValue().isEmpty())
					oPaymentJSONObject.put("cardnumber", oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_CARD_NO, 0).getValue());
			}
			if(oPaymentArray.length()>0)
				oResultSONObject.put("payments", oPaymentArray);
			oResultSONObject.put("paymenttotal", dPaymentTotal);
			
			// Member information
			if(m_oFuncCheck.getMemberNumber() != null){
				oMemberJSONObject = new JSONObject();
				oMemberJSONObject.put("number", m_oFuncCheck.getMemberCardNumber());
				oMemberJSONObject.put("name", m_oFuncCheck.getMemberDisplayName());
				oResultSONObject.put("member", oMemberJSONObject);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return oResultSONObject;
	}
	
	//Open Check Listing
	private String getOpenCheckList(Integer iLangidx, String sGetType, String sOpenUserLogin, String sDateTime) {
		UserUser oUser = new UserUser();
		PosCheck oPosCheck = new PosCheck();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		
		JSONObject oResultSONObject = new JSONObject();
		JSONArray oOpenCheckList = new JSONArray();
		
		if (sGetType.equals(PosCheck.GET_CHECK_LISTING_BY_SPECIFIC_USER)) {
			if (sOpenUserLogin.isEmpty()) {
				m_sLastErrorCode = "-32045";
				return "Missing employee login name";
			}
			
			oUser = getUserByLogin(sOpenUserLogin);
			
			if (oUser == null) {
				m_sLastErrorCode =  "-32011";
				return "No such employee";
			}
		}
		
		try {
			// Get All Open Check List from POS API
			JSONArray oCheckListJSONArray = oPosCheck.getCheckListByBusinessDayPaid(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), 0, PosCheck.PAID_NOT_PAID, false);
			
			if (oCheckListJSONArray != null) {
				for (int j = 0; j < oCheckListJSONArray.length(); j++) {
					if (oCheckListJSONArray.isNull(j))
						continue;
					
					JSONObject oCheckJSONObject = new JSONObject();
					PosCheck oCheck = new PosCheck(oCheckListJSONArray.optJSONObject(j));
					
					if (sGetType.equals(PosCheck.GET_CHECK_LISTING_BY_SPECIFIC_USER)) {
						if (oUser.getUserId() != oCheck.getOpenUserId())
							continue;
					} else
						oUser = getUserById(oCheck.getOpenUserId());
	
					oCheckJSONObject.put("outletcode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
					
					oCheckJSONObject.put("tableno", Integer.toString(oCheck.getTable()));
					oCheckJSONObject.put("tablename", AppGlobal.g_oFuncOutlet.get().getTableName(Integer.toString(oCheck.getTable()), oCheck.getTableExtension())[iLangidx - 1]);
					oCheckJSONObject.put("checkno", oCheck.getCheckPrefixNo());
					oCheckJSONObject.put("cover", Integer.toString(oCheck.getGuests()));
					
					oCheckJSONObject.put("openemployeecode", oUser.getNumber());
					oCheckJSONObject.put("openemployeename", oUser.getFirstName(iLangidx) + " " + oUser.getLastName(iLangidx));
					
					oCheckJSONObject.put("opentime", timeFormat.format(oCheck.getOpenLocTime().toDate()));
					oCheckJSONObject.put("checktotal", StringLib.BigDecimalToString(oCheck.getCheckTotal(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getCheckDecimal()));
					
					oOpenCheckList.put(oCheckJSONObject);
				}
			}
			oResultSONObject.put("time", sDateTime);
			oResultSONObject.put("checks", oOpenCheckList);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return oResultSONObject.toString();
	}
	
	public static UserUser getUserById(int iId) {
		UserUser oUser = new UserUser();
		if (!oUser.readByUserId(iId))
			return null;
		
		return oUser;
	}
	
	public static UserUser getUserByLogin(String sOpenUserLogin) {
		UserUser oUser = new UserUser();
		if (!oUser.readByUserLogin(sOpenUserLogin))
			return null;
		
		return oUser;
	}
	
	//pay check
	private boolean payCheck(boolean bSendAndPay, JSONObject oCheckInfo, String sVerifyKey) {
		if(oCheckInfo == null) {
			m_sLastErrorCode = "-32038";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("missing_check_information");
			return false;
		}
		
		String sTableNo = "";
		String sTableExtension = "";
		String sCheckNo = "";
		//tableno
		if(oCheckInfo.has("tableno")) {
			sTableNo = oCheckInfo.optString("tableno", "");
			if(oCheckInfo.has("tableextension"))
				sTableExtension = oCheckInfo.optString("tableextension", "");
		}
		
		if(oCheckInfo.has("checkno"))
			sCheckNo = oCheckInfo.optString("checkno", "");
		
		// open check
		if(!bSendAndPay) {
			m_sLastErrorMessage = openCheck(sTableNo, sTableExtension, sCheckNo , 0, true, sVerifyKey);
			if(!m_sLastErrorMessage.isEmpty())
				return false;
			
			//check no. comparison
			// check check number
			if(!sCheckNo.isEmpty() && !sCheckNo.equals(m_oFuncCheck.getCheckPrefixNo())) {
				m_oFuncCheck.unlockTable(true, false);
				m_sLastErrorCode = "-32227";
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("check_number_not_matched");
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Check number not matched");
				return false;
			}
		}else {
			m_oFuncCheck.setCheckOpenTimeValue(AppGlobal.g_oFuncOutlet.get().getOutletId(), false, 0);
			String sPeriodId = "";
			if(oCheckInfo.has("mealperiod") && !oCheckInfo.optString("mealperiod", "").isEmpty()) {
				sPeriodId = AppGlobal.g_oFuncOutlet.get().getBusinessPeriodIdByCode(oCheckInfo.optString("mealperiod", ""));
				if(sPeriodId.isEmpty()) {
					m_oFuncCheck.unlockTable(true, false);
					m_sLastErrorCode = "-32227";
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("meal_period_not_find");
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Meal period not find:"+oCheckInfo.optString("mealperiod", ""));
					return false;
				}else
					m_oFuncCheck.setBusinessPeriod(sPeriodId);
			}
		}
		
		//get payment code and ID
		List<HashMap<String, String>> oPaymentInfoList = new ArrayList<HashMap<String, String>>();
		boolean bPaymentInfoReady = false;
		BigDecimal dPaymentsTotal = BigDecimal.ZERO;
		if(oCheckInfo.has("payments") && oCheckInfo.optJSONArray("payments") != null) {
			JSONArray oPaymentInfos = oCheckInfo.optJSONArray("payments");
			
			// assign current check total to payment if there is only one payment provided without amount
			if (bSendAndPay && oPaymentInfos.length() == 1 && !oPaymentInfos.optJSONObject(0).has("amount")) {
				try {
					oPaymentInfos.optJSONObject(0).put("amount", m_oFuncCheck.getCheckTotal());
				} catch (JSONException e) {
					AppGlobal.stack2Log(e);
				}
			}
			
			for(int i=0; i<oPaymentInfos.length(); i++) {
				String sPaymentCode = oPaymentInfos.optJSONObject(i).optString("code", "");
				if(sPaymentCode.isEmpty()) {
					m_sLastErrorCode = "-32228";
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_such_payment");
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "No such payment - code:"+sPaymentCode);
					break;
				}
				
				HashMap<Integer, PosPaymentMethod> oPaymentMethodList = m_oFuncPayment.getPaymentMethodList().getPaymentMethodList();
				PosPaymentMethod oMatchedPaymentMethod = null;
				for(PosPaymentMethod oPaymentMethod : oPaymentMethodList.values()){
					if(oPaymentMethod.getPaymentCode().equals(sPaymentCode)) {
						oMatchedPaymentMethod = oPaymentMethod;
						break;
					}
				}
				
				if(oMatchedPaymentMethod == null) {
					m_sLastErrorCode = "-32228";
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_such_payment");
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "No such payment - code:"+sPaymentCode);
					break;
				}
				
				if(!oPaymentInfos.optJSONObject(i).has("amount")) {
					m_sLastErrorCode = "-32228";
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_payment_amount");
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "No payment amuount provided");
					break;
				}
				
				BigDecimal dPaymentDiscount = BigDecimal.ZERO;
				if(oPaymentInfos.optJSONObject(i).has("discamount") && !oPaymentInfos.optJSONObject(i).optString("discamount").isEmpty())
					dPaymentDiscount = new BigDecimal(oPaymentInfos.optJSONObject(i).optString("discamount", "0.0"));
				
				//If exist discount, set as payment auto discount
				if(dPaymentDiscount.compareTo(BigDecimal.ZERO) > 0){
					int iAutoDiscountId = 0;
					
					// Check the payment include auto discount
					if (oMatchedPaymentMethod.getAutoDiscountTypeId().isEmpty() || oMatchedPaymentMethod.getAutoDiscountTypeId().equals("0")){
						m_oFuncCheck.unlockTable(true, false);
						m_sLastErrorCode = "-32229";
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_discount_attached_to_payment") + ": " + oMatchedPaymentMethod.getName(AppGlobal.g_oCurrentLangIndex.get());
						AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", 
								"No discount attached to payment:" + oMatchedPaymentMethod.getName(AppGlobal.g_oCurrentLangIndex.get()));
						return false;
					}
					
					iAutoDiscountId = Integer.valueOf(oMatchedPaymentMethod.getAutoDiscountTypeId());
					PosDiscountType oDiscountType = new PosDiscountType();
					
					// Check does the discount exist or not
					if (!(oDiscountType.readById(iAutoDiscountId))){
						m_oFuncCheck.unlockTable(true, false);
						m_sLastErrorCode = "-32230";
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_find_discount_type");
						AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Cannot not find the discount type");
						return false;
					}
					
					// Check is it post-discount
					if(!oDiscountType.getType().equals(PosDiscountType.TYPE_POST_DISCOUNT)) {
						m_oFuncCheck.unlockTable(true, false);
						m_sLastErrorCode = "-32217";
						m_sLastErrorMessage = "the_attached_discount_is_not_post_discount";
						AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Attached discount is not POST discount");
						return false;
					}
					
					// Check is it open discount
					boolean bOpenDiscount = false;
					if (oDiscountType.isFixAmountDiscountPerCheckMethod() || oDiscountType.isFixAmountDiscountPerItemMethod()) {
						if (oDiscountType.getFixAmount().compareTo(BigDecimal.ZERO) == 0)
							bOpenDiscount = true;
					} else if (oDiscountType.isPercentageDiscountMethod()) {
						if (oDiscountType.getRate().compareTo(BigDecimal.ZERO) == 0)
							bOpenDiscount = true;
					}
					if(!bOpenDiscount) {
						m_oFuncCheck.unlockTable(true, false);
						m_sLastErrorCode = "-32218";
						m_sLastErrorMessage = "the_attached_discount_is_not_open_discount";
						AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Attached discount is not OPEN discount");
						return false;
					}
				}
				
				HashMap<String, String> oPaymentInfo = new HashMap<String, String>();
				oPaymentInfo.put("paymentType", oMatchedPaymentMethod.getPaymentType());
				oPaymentInfo.put("paymentId", String.valueOf(oMatchedPaymentMethod.getPaymId()));
				oPaymentInfo.put("payTotal", oPaymentInfos.optJSONObject(i).optString("amount", "0.0"));
				if (oPaymentInfos.optJSONObject(i).has("discamount") && !oPaymentInfos.optJSONObject(i).optString("discamount").isEmpty())
					oPaymentInfo.put("discountTotal", oPaymentInfos.optJSONObject(i).optString("discamount", "0.0"));
				
				if(oPaymentInfos.optJSONObject(i).has("couponno"))
					oPaymentInfo.put("couponno", oPaymentInfos.optJSONObject(i).optString("couponno", ""));
				if(oPaymentInfos.optJSONObject(i).has("cardno"))
					oPaymentInfo.put("cardno", oPaymentInfos.optJSONObject(i).optString("cardno", ""));
				oPaymentInfo.put("tipsAmount", oPaymentInfos.optJSONObject(i).optString("tips", "0.0"));
				oPaymentInfo.put("surchargeAmount", oPaymentInfos.optJSONObject(i).optString("surchargeamount", "0.0"));
			
				if(oPaymentInfos.optJSONObject(i).has("roomno"))
					oPaymentInfo.put("roomno", oPaymentInfos.optJSONObject(i).optString("roomno",""));
				if(oPaymentInfos.optJSONObject(i).has("guestno"))
					oPaymentInfo.put("guestno",  oPaymentInfos.optJSONObject(i).optString("guestno",""));
				if(oPaymentInfos.optJSONObject(i).has("guestname"))
					oPaymentInfo.put("guestname",  oPaymentInfos.optJSONObject(i).optString("guestname",""));
			
				if(oPaymentInfos.optJSONObject(i).has("references") && oPaymentInfos.optJSONObject(i).optJSONArray("references") != null) {
					JSONArray oPaymentReference = oPaymentInfos.optJSONObject(i).optJSONArray("references");
					for(int j = 0 ; j < oPaymentReference.length(); j++) {
						if(oPaymentReference.optJSONObject(j).has("index") && oPaymentReference.optJSONObject(j).has("value"))
							oPaymentInfo.put("reference"+oPaymentReference.optJSONObject(j).optString("index", "1"), oPaymentReference.optJSONObject(j).optString("value", ""));
					}
				}
				
				oPaymentInfoList.add(oPaymentInfo);
				dPaymentsTotal = dPaymentsTotal.add(new BigDecimal(oPaymentInfos.optJSONObject(i).optString("amount", "0.0")));
				if(oPaymentInfos.optJSONObject(i).has("discamount") && !oPaymentInfos.optJSONObject(i).optString("discamount").isEmpty())
					dPaymentsTotal = dPaymentsTotal.add(new BigDecimal(oPaymentInfos.optJSONObject(i).optString("discamount", "0.0")));
			}
			
			if(!m_sLastErrorMessage.isEmpty()) {
				m_oFuncCheck.unlockTable(true, false);
				return false;
			}
			
			if(oPaymentInfoList.size() == oPaymentInfos.length())
				bPaymentInfoReady = true;
		}
		
		if(!bPaymentInfoReady) { 
			m_oFuncCheck.unlockTable(true, false);
			m_sLastErrorCode = "-32231";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("missing_payment_method");
			return false;
		}
		
		if(!AppGlobal.g_oFuncStation.get().isPartialPayment() && dPaymentsTotal.compareTo(m_oFuncCheck.getCheckTotal()) < 0) {
			m_oFuncCheck.unlockTable(true, false);
			m_sLastErrorCode = "-32232";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("total_payment_amount_is_not_enough");
			return false;
		}
		
		// check whether member number provided
		if(oCheckInfo.has("memberno")) {
			if(!m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0))
				m_oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0, oCheckInfo.optString("memberno", ""));
			else
				m_oFuncCheck.updateCheckExtraInfoValue(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0, oCheckInfo.optString("memberno", ""));
		}
		
		// paid check
		AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Paid check:"+m_oFuncCheck.getCheckPrefixNo());
		if(!preparePaidCheck(bSendAndPay)) {
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to init check payment:"+m_sLastErrorMessage);
			m_oFuncCheck.unlockTable(true, false);
			return false;
		}
		
		int iPaymentCount = 0;
		boolean bIsApplyPaymentDiscount = true;
		for(HashMap<String, String> oPaymentInfo: oPaymentInfoList) {
			boolean bIsLastPayment = false;
			int iPaymentId = Integer.valueOf(oPaymentInfo.get("paymentId")).intValue(), iPayRefCnt = 3;
			BigDecimal dPaymentTotal = new BigDecimal(oPaymentInfo.get("payTotal"));
			BigDecimal dDiscountTotal = BigDecimal.ZERO, dTipsAmount = BigDecimal.ZERO, dSurchargeAmt = BigDecimal.ZERO;
			String[] sCheckPaymentRefArray = new String[3];
			
			if(oPaymentInfo.containsKey("discountTotal")) {
				if(bIsApplyPaymentDiscount) {
					dDiscountTotal = new BigDecimal(oPaymentInfo.get("discountTotal"));
					if(dDiscountTotal.compareTo(m_oFuncPayment.getCurrentBalance()) > 0) {
						dDiscountTotal = m_oFuncPayment.getCurrentBalance();
						bIsApplyPaymentDiscount = false;
					}
				}
			}
			
			if(oPaymentInfo.containsKey("tipsAmount")) {
				try {
					dTipsAmount = new BigDecimal(oPaymentInfo.get("tipsAmount"));
				}catch(Exception e) {}
			}
			if(oPaymentInfo.containsKey("surchargeAmount")) {
				try {
					dSurchargeAmt = new BigDecimal(oPaymentInfo.get("surchargeAmount"));
				}catch(Exception e) {}
			}
			for(int i=1; i<=iPayRefCnt; i++) {
				if(oPaymentInfo.containsKey("reference" + i))
					sCheckPaymentRefArray[i-1] = oPaymentInfo.get("reference" + i);
				else
					sCheckPaymentRefArray[i-1] = null;
			}
			
			List<HashMap<String, String>> oPaymentExtraInfoList = new ArrayList<HashMap<String, String>>();
			
			HashMap<String, String> oTempHashMap = null;
			//coupon
			if(oPaymentInfo.containsKey("couponno")) {
				oTempHashMap = new HashMap<String, String>();
				oTempHashMap.put("section", PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE);
				oTempHashMap.put("variable", PosCheckExtraInfo.VARIABLE_COUPON);
				oTempHashMap.put("value", oPaymentInfo.get("couponno"));
				oPaymentExtraInfoList.add(oTempHashMap);
			}
			//cardno
			if(oPaymentInfo.containsKey("cardno")) {
				oTempHashMap = new HashMap<String, String>();
				oTempHashMap.put("section", PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE);
				oTempHashMap.put("variable", PosCheckExtraInfo.VARIABLE_CARD_NO);
				oTempHashMap.put("value", oPaymentInfo.get("cardno"));
				oPaymentExtraInfoList.add(oTempHashMap);
			}
			//check is last payment
			if((oPaymentInfoList.size() - 1) == iPaymentCount)
				bIsLastPayment = true;
			
			HashMap<String, BigDecimal> oPaymentValues = new HashMap<String, BigDecimal>();
			oPaymentValues.put("paymentTotal", dPaymentTotal);
			oPaymentValues.put("discountTotal", dDiscountTotal);
			oPaymentValues.put("tipsAmount", dTipsAmount);
			oPaymentValues.put("surchargeAmount", dSurchargeAmt);
			
			HashMap<String, String> oPaymentInfos = new HashMap<String, String>();
			if(bIsLastPayment) 
				oPaymentInfos.put("lastPayment", "true");
			if(oPaymentInfo.containsKey("guestname"))
				oPaymentInfos.put("guestname", oPaymentInfo.get("guestname"));
			if(oPaymentInfo.containsKey("guestno"))
				oPaymentInfos.put("guestno", oPaymentInfo.get("guestno"));
			if(oPaymentInfo.containsKey("roomno"))
				oPaymentInfos.put("roomno", oPaymentInfo.get("roomno"));
			if(!handlePaymentKey(iPaymentId, "", oPaymentValues, oPaymentInfos, sCheckPaymentRefArray, oPaymentExtraInfoList)) {
				m_oFuncCheck.unlockTable(true, false);
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to handle payment key:"+m_sLastErrorMessage);
				return false;
			}
			iPaymentCount++;
		}
		
		return true;
	}
	
	private boolean handlePaymentKey(int iPaymentId, String sParameter, HashMap<String, BigDecimal> oPaymentValues, HashMap<String, String> oPaymentInfos, String[] sCheckPaymentRefArray, List<HashMap<String, String>> oPaymentExtraInfoList) {
		// Add the payment to cashier mode
		if(preProcessEachPayment(iPaymentId, oPaymentValues.get("discountTotal"), sCheckPaymentRefArray, oPaymentExtraInfoList, oPaymentInfos)){
			// Finish asking amount, edit the payment amount stored in PosCheckPayment list of FuncPayment
			if(editPayment(0, m_oFuncPayment.getCheckPaymentList().size()-1, oPaymentValues, oPaymentInfos, false) == false) {
				rollbackPayment();
				return false;
			}
		}else {
			rollbackPayment();
			return false;
		}
		
		return true;
	}
	
	private boolean preProcessEachPayment(int iPaymentId, BigDecimal dDiscountTotal, String[] sCheckPaymentRefArray, List<HashMap<String, String>> oPaymentExtraInfoList, HashMap<String, String> oPaymentInfos) {
		PosPaymentMethodList oPosPaymentMethodList = m_oFuncPayment.getPaymentMethodList();
		BigDecimal dPayTotal = BigDecimal.ZERO, dTipsTotal = BigDecimal.ZERO;
		int iRet = 0, iEmployeeId = 0, iMemberId = 0;
		ArrayList<PosCheckExtraInfo> oCheckPaymentExtraInfos = new ArrayList<PosCheckExtraInfo>();
		
		//get the payment method
		if(oPosPaymentMethodList.getPaymentMethodList().containsKey(iPaymentId) == false){
			// No payment method is found
			m_sLastErrorCode = "-32223";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_such_payment_method");
			return false;
		}
		
		for (HashMap<String, String> oExtrainfoMap: oPaymentExtraInfoList) {
			if(oExtrainfoMap.containsKey("section") && oExtrainfoMap.containsKey("variable") && oExtrainfoMap.containsKey("value")) {
				PosCheckExtraInfo oPosCheckExtraInfo = new PosCheckExtraInfo();
				oPosCheckExtraInfo.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
				oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
				oPosCheckExtraInfo.setSection(oExtrainfoMap.get("section"));
				oPosCheckExtraInfo.setVariable(oExtrainfoMap.get("variable"));
				oPosCheckExtraInfo.setValue(oExtrainfoMap.get("value"));
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			}
		}
		
		PosPaymentMethod oPosPaymentMethod = oPosPaymentMethodList.getPaymentMethodList().get(iPaymentId);
		
		//check whether have interface configure attached and interface module existence
		if(oPosPaymentMethod.hasInterfaceConfig()) {
			if(AppGlobal.isModuleSupport(AppGlobal.OPTIONAL_MODULE.pos_interface.name()) == false) {
				m_sLastErrorCode = "-32224";
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("interface_module_is_not_supported");
				return false;
			}
		}
		
		// Check if the payment non-revenue is match or not
		if(m_oFuncPayment.getCheckPaymentList().size() > 0){
			String sNonRevenue = m_oFuncPayment.getNonRevenue();
			if(!AppGlobal.g_oFuncStation.get().isAllowMixedPayment() && sNonRevenue.equals(oPosPaymentMethod.getNonRevenue()) == false){
				// Non revenue is not matched
				if(sNonRevenue.equals(PosCheckPayment.NON_REVENUE_YES)) {
					m_sLastErrorCode = "-32225";
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("non_revenue_check_with_revenue_payment");
				} else {
					m_sLastErrorCode = "-32226";
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("revenue_check_with_non_revenue_payment");
				}
				return false;
			}
		}
		else{
			// First payment checking
			
			// Check if the round method is override by payment method
			boolean bNeedRound = false;
			// *** need further development
			if(false){
				// Change the check rounding
				// *** need further development
				AppGlobal.g_oFuncOutlet.get().overrideCheckRoundMethod(true, "", 0);
				
				bNeedRound = true;
			}else
			if(AppGlobal.g_oFuncOutlet.get().isOverrideCheckRound()){
				// Roll back rounding
				AppGlobal.g_oFuncOutlet.get().overrideCheckRoundMethod(false, "", 0);
				bNeedRound = true;
			}
			
			if(bNeedRound){
				// Need do rounding
				
				// Recalculate the check
				m_oFuncCheck.calcCheck();
				
				if(m_oFuncCheck.isOldCheck()){
					// For old check, re-print guest check and update database
					int iChosenCheckPfmtId = 0;
					boolean bPrintCheck= false;
					boolean bDetailCheck = false;
					
					processSendCheck(m_oFuncCheck, bPrintCheck, false, AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getOutletNameByIndex(AppGlobal.g_oCurrentLangIndex.get()),
						AppGlobal.g_oFuncStation.get().getCheckPrtqId(), iChosenCheckPfmtId, bDetailCheck, "", true);
					
				}
				
				//update check total in cashier screen
				m_oFuncPayment.setCurrentBalance(m_oFuncCheck.getCheckTotal().subtract(m_oFuncPayment.getPaidBalance()));
			}
		}
		
		/*** Check Auto Discount ***/
		boolean bWaiveSC = false, bWaiveTax = false;
		int iAutoDiscId = 0;
		String sAutoDiscType = "check";
		
		//Waive SC and Tax as necessary
		if (oPosPaymentMethod.isAutoWaiveSc())
			bWaiveSC = true;
		
		if (oPosPaymentMethod.isAutoWaiveTax())
			bWaiveTax =true;
		
		//Check whether have auto discount and its discount type (item/check discount)
		if (!oPosPaymentMethod.getAutoDiscountTypeId().isEmpty()) {
			iAutoDiscId = Integer.valueOf(oPosPaymentMethod.getAutoDiscountTypeId());
			if(iAutoDiscId > 0 && !oPosPaymentMethod.isAutoCheckDiscountType()) {
				m_sLastErrorCode = "-32210";
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("not_allow_auto_item_discount_for_payment");
				return false;
			}
		}
		
		//Really waive SC/Tax
		if (bWaiveSC || bWaiveTax) {
			int i;
			boolean[] bChosenSc = new boolean[5];
			String[] sChosenTax = new String[25];
			List<HashMap<String, Integer>> oSelectedItem = new ArrayList<HashMap<String, Integer>>();
			
			oSelectedItem = m_oFuncCheck.getSectionItemIndexForCurrentOrderedItem();
			for(i=0; i<5; i++)
				bChosenSc[i] = bWaiveSC;
			for(i=0; i<25; i++){
				//bChosenTax[i] = bWaiveTax;
				if(bWaiveTax)
					sChosenTax[i] = PosOverrideCondition.CHARGE_TAX_WAIVE;
				else
					sChosenTax[i] = PosOverrideCondition.CHARGE_TAX_NO_CHANGE;
			}
			if(!m_oFuncCheck.addWaiveScTax(false, oSelectedItem, bChosenSc, sChosenTax))
				return false;
		}
		
		//Check whether have auto discount
		if(dDiscountTotal.compareTo(BigDecimal.ZERO) > 0 && iAutoDiscId == 0) {
			m_sLastErrorCode = "-32229";
			m_sLastErrorMessage = "no_discount_attached_to_payment"+": "+oPosPaymentMethod.getName(AppGlobal.g_oCurrentLangIndex.get());
			return false;
		}
		
		if (iAutoDiscId > 0) {
			String sApplyDiscountResult = autoDiscountForPayment(sAutoDiscType, iAutoDiscId, dDiscountTotal);
			if(sApplyDiscountResult.equals(FormMain.FUNC_RESULT_NO_SUCH_RECORD)) {
				m_sLastErrorCode = "-32207";
				m_sLastErrorMessage = "no_such_discount"+": "+iAutoDiscId;
			}else if (sApplyDiscountResult.equals(FormMain.FUNC_RESULT_FAIL))
				return false;
		}
		
		if (bWaiveSC || bWaiveTax || iAutoDiscId > 0) {
			//update check total in cashier screen
			m_oFuncPayment.setCurrentBalance(m_oFuncCheck.getCheckTotal().subtract(m_oFuncPayment.getPaidBalance()));
		}
		
		if (oPosPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS) != null
				&& oPosPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).size() > 0) {
			
			List<PosInterfaceConfig> oPosInterfaceConfigs = oPosPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS);
			PosInterfaceConfig oPosInterfaceConfig = oPosInterfaceConfigs.get(0);
			
			if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP)) {
				if (!FuncPMS.checkNeedAskInfo(oPosInterfaceConfig) && FuncPMS.haveDefaultAccount(oPosInterfaceConfig)
						&& FuncPMS.isAllowPostAfterPayment(oPosInterfaceConfig)) {
					// check whether have default account setting, posting with default account, and
					// use offline posting
					FuncPMS.createPaymentPMSExtraInfoForPostingWithDefaultAccount(oCheckPaymentExtraInfos,
							oPosInterfaceConfig, m_oFuncCheck.getTableNoWithExtensionForDisplay(),
							oPosPaymentMethod.getPaymentCode(), (m_oFuncPayment.getCheckPaymentList().size() + 1));
				}
				if (!FuncPMS.isAllowPostAfterPayment(oPosInterfaceConfig)) {
					// check whether using online posting
					if (!FuncPMS.checkNeedAskInfo(oPosInterfaceConfig)) {
						// check whether have default account
						FuncPMS.createPaymentPMSExtraInfoForPostingWithDefaultAccount(oCheckPaymentExtraInfos,
								oPosInterfaceConfig, m_oFuncCheck.getTableNoWithExtensionForDisplay(),
								oPosPaymentMethod.getPaymentCode(), (m_oFuncPayment.getCheckPaymentList().size() + 1));
					} else if (FuncPMS.checkNeedAskInfo(oPosInterfaceConfig)) {
						if (!oPaymentInfos.containsKey("guestno")
								&& !oPaymentInfos.containsKey("roomno")) {
							m_sLastErrorCode = "-32209";
							m_sLastErrorMessage = AppGlobal.g_oLang.get()._("missing_pms_posting_account");
							return false;
						}
						// check whether have default account and allow to ask info
						FuncPMS.createPaymentPMSExtraInfoForAPIPortalPostingWithoutDefaultAccount(
								oCheckPaymentExtraInfos, oPosInterfaceConfig, oPaymentInfos);
						
						//Internal usage
						JSONObject oPMSInfoJSONObject = new JSONObject();
						try {
							oPMSInfoJSONObject.put("table", m_oFuncCheck.getTableNoWithExtensionForDisplay());
							oPMSInfoJSONObject.put("pm_code", oPosPaymentMethod.getPaymentCode());
						} catch (JSONException jsone) {
							jsone.printStackTrace();
						}
						oCheckPaymentExtraInfos.add(
								this.constructCheckExtraInfo(PosCheckExtraInfo.BY_PAYMENT, PosCheckExtraInfo.SECTION_PMS,
										PosCheckExtraInfo.VARIABLE_INTERNAL_USE, oPMSInfoJSONObject.toString()));
					}
				}
		
			}
		}
		//for OGS payment handle ogs payment
		//*********
		
		// Add to check payment list first
		iRet = m_oFuncPayment.addPayment(iPaymentId, AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), m_oFuncCheck.getCheckId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), dPayTotal, dTipsTotal, iEmployeeId, iMemberId, sCheckPaymentRefArray, oCheckPaymentExtraInfos, m_iAppliedUserId);
		if(iRet < 0){
			// Fail to add payment
			m_sLastErrorCode = "-32222";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_add_payment");
			return false;
		}
		
		return true;
	}
	
	public boolean editPayment(int iSectionId, int iItemIndex, HashMap<String, BigDecimal> oPaymentValues, HashMap<String, String> oPaymentInfos, boolean bNotAllowFinishPayment){
		// Round the value
		BigDecimal dPaymentAmount = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(oPaymentValues.get("paymentTotal"));
		BigDecimal dTipsAmount = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(oPaymentValues.get("tipsAmount"));
		BigDecimal dSurchargeAmt = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(oPaymentValues.get("surchargeAmount"));
		
		boolean bLastPayment = false;
		if(oPaymentInfos.containsKey("lastPayment") && oPaymentInfos.get("lastPayment").equals(DeviceThreadMain.STRING_TRUE))
			bLastPayment = true;
		boolean bNegativeCalculation = false;
		if(dPaymentAmount.compareTo(BigDecimal.ZERO) == 0)
			bNegativeCalculation = (m_oFuncPayment.getCurrentBalance().signum() < 0);
		
		int iRet = m_oFuncPayment.editPayment(iItemIndex, dPaymentAmount, dTipsAmount, dSurchargeAmt, null, bNegativeCalculation);
		if(iRet < 0 || (iRet != 2 && !AppGlobal.g_oFuncStation.get().isPartialPayment() && bLastPayment)){
			// Fail to add payment
			m_sLastErrorCode = "-32222";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_add_payment");
			AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Fail to add payment");
			return false;
		}
		
		// Change the iRet to 2 for supporting partial payment and it is last payment of request
		if(AppGlobal.g_oFuncStation.get().isPartialPayment() && bLastPayment)
			iRet = 2;
		
		if(iRet == 2 && bNotAllowFinishPayment == false){
			boolean bCloseCheck = true;
			if(AppGlobal.g_oFuncStation.get().isPartialPayment() &&
					// old payment total and new payment total compare with check total to determine whether close check
					m_oFuncPayment.getAllPaymentTotal().add(m_oFuncCheck.getPaymentRecordPayTotal()).compareTo(m_oFuncCheck.getCheckTotal()) < 0)
				bCloseCheck = false;
			
			// Finish all payments
			if(finishPayment(bCloseCheck) == false)
				return false;
		}
		
		return true;
	}
	
	public boolean finishPayment(boolean bCloseCheck){
		boolean bPass = true;
		int iChosenReceiptPfmtId = 0;
		BigDecimal dAddedSurcharge = BigDecimal.ZERO;
		
		//get the receipt format from portal station
		for (int i = 1; i <= 5; i++){
			iChosenReceiptPfmtId = AppGlobal.g_oFuncStation.get().getStation().getReceiptPfmtId(i);
			if(iChosenReceiptPfmtId != 0)
				break;
		}
		
		DateTime oPaymentDateTime = AppGlobal.getCurrentTime(false);
		
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Start finish payment");
		
		boolean bSendAndPaid = false;
		if(m_sOriFuncKey.equals(DeviceMain.FUNC_LIST.send_and_pay.name()))
			bSendAndPaid = true;
		
		// Revenue and non-revenue handling
		// If check is revenue check and payments are revenue payment, no change
		// If check is revenue check and payments are non-revenue payment, check will become non-revenue check
		// If check is non-revenue check (in different type: e.g. advance order) and payments are revenue payment, no change on check and payments will become non-revenue payment (in different type: e.g. advance order)
		// If check is non-revenue check (in different type: e.g. advance order) and payments are non-revenue payment, no change on check and payments will become non-revenue payment (in different type: e.g. advance order)
		if(m_oFuncCheck.isRevenue() && m_oFuncPayment.getNonRevenue().equals(PosCheck.NON_REVENUE_PAYMENT))
			m_oFuncCheck.setNonRevenue(PosCheck.NON_REVENUE_PAYMENT, false);
		else{
			for(PosCheckPayment oPosCheckPayment:m_oFuncPayment.getCheckPaymentList())
				oPosCheckPayment.setNonRevenue(m_oFuncCheck.getNonRevenue());
		}
		
		// TODO - Do PMS posting
		FuncPMS oFuncPMS = new FuncPMS();
		int iSuccessPMSPaymentCount = 0, iPaymentSeq = 0;
		BigDecimal dPreviousPaymentTotal = BigDecimal.ZERO;
		for(PosCheckPayment oPosCheckPayment:m_oFuncPayment.getCheckPaymentList()) {
			iPaymentSeq++;
			if(oPosCheckPayment.isOldPayment())
				continue;
			
			HashMap<String, Boolean> oPartialPaymentInfo = new HashMap<String, Boolean>();
			oPartialPaymentInfo.put("closeCheck", bCloseCheck);
			
			PosPaymentMethod oPaymentMethod = m_oFuncPayment.getPaymentMethodList().getPaymentMethodList().get(oPosCheckPayment.getPaymentMethodId());
			if(oFuncPMS.pmsPosting(m_oFuncCheck, oPosCheckPayment, oPaymentMethod, iPaymentSeq, dPreviousPaymentTotal, m_oFuncPayment.getCheckPaymentList(), oPartialPaymentInfo) == false) {
				//void previous successful PMS posting
				if(iSuccessPMSPaymentCount > 0) {
					int iVoidChkPayment = 0;
					BigDecimal dVoidPreviousPaymentTotal = BigDecimal.ZERO;
					for(PosCheckPayment oVoidPMSChkPayment:m_oFuncPayment.getCheckPaymentList()) {
						if(oVoidPMSChkPayment.havePmsPayment()) {
							PosPaymentMethod oVoidPaymentMethod = m_oFuncPayment.getPaymentMethodList().getPaymentMethodList().get(oVoidPMSChkPayment.getPaymentMethodId());
							oFuncPMS.pmsVoidPosting(m_oFuncCheck, oVoidPMSChkPayment, oVoidPaymentMethod, iPaymentSeq, dVoidPreviousPaymentTotal, m_oFuncPayment.getCheckPaymentList(), null);
						}
						iVoidChkPayment++;
						if(iSuccessPMSPaymentCount == iVoidChkPayment)
							break;
						dVoidPreviousPaymentTotal = dVoidPreviousPaymentTotal.add(oVoidPMSChkPayment.getPayTotal());
					}
				}
				
				//roll back added surcharge
				if(dAddedSurcharge.compareTo(BigDecimal.ZERO) > 0)
					m_oFuncCheck.setSurcharge(m_oFuncCheck.getSurcharge().subtract(dAddedSurcharge));
				
				//show PMS error message
				m_sLastErrorCode = "-32049";
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_post_pms");
				if(!oFuncPMS.getLastErrorMessage().isEmpty())
					m_sLastErrorMessage = oFuncPMS.getLastErrorMessage();
				return false;
			}
			iSuccessPMSPaymentCount++;
			dPreviousPaymentTotal = dPreviousPaymentTotal.add(oPosCheckPayment.getPayTotal());
			
			// Update the check level surcharge total
			if(oPosCheckPayment.getSurcharge().compareTo(BigDecimal.ZERO) != 0) {
				m_oFuncCheck.setSurcharge(m_oFuncCheck.getSurcharge().add(oPosCheckPayment.getSurcharge()));
				dAddedSurcharge = dAddedSurcharge.add(oPosCheckPayment.getSurcharge());
			}
		}

		// Get VGS e-invoice QR code
		if (bCloseCheck && AppGlobal.isModuleSupport(AppGlobal.OPTIONAL_MODULE.pos_interface.name())) {
			List<PosInterfaceConfig> oPosInterfaceConfigs = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PAYMENT_INTERFACE);
			for (PosInterfaceConfig oPosInterfaceConfig : oPosInterfaceConfigs)
				if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_VGS_STANDARD)) {
					getVgsQrCode(oPosInterfaceConfig);
					break;
				}
		}

		// Save all payments
		m_oFuncPayment.setPaymentDateTime(oPaymentDateTime);
		
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Before open drawer");
		
		// Open drawer, payment will be updated in "updatePaymentInfo" function
		boolean bOpenDrawerOnly = true;
		if(bPass && m_oFuncPayment.saveMultipleCheckPayments(m_oFuncCheck.getCheckPrefixNo(), AppGlobal.g_oFuncStation.get().getStation().getReceiptPrtqId(), iChosenReceiptPfmtId, bOpenDrawerOnly) == false){
			// Fail to add payment
			m_sLastErrorCode = "-32220";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_save_payments");
			AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Fail to save payments");
			bPass = false;
		}
		
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "After open drawer");
		
		// Build the list under FuncCheck to update payment
		for(PosCheckPayment oPosCheckPayment:m_oFuncPayment.getCheckPaymentList()){
			if(oPosCheckPayment.isOldPayment())
				continue;
			m_oFuncCheck.addNewCheckPaymentToList(new PosCheckPayment(oPosCheckPayment));
		}
		
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Before update database");
		
		// Wait for previous check processing finish
		waitForProcessCheckFinish("", "");
		
		// Save check
		String sPeriodId = AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId();
		boolean bIsFastFoodMode = false;
		boolean bIsSelfOrderKioskMode = false;
		boolean bIsBarTabMode = false;

		// Send loyalty closeTrans
		if (bCloseCheck && bPass) {
			boolean bResult = m_oFuncCheck.closeLoyaltyTransaction();
			if (!bResult && !m_oFuncCheck.getLastErrorMessage().isEmpty()) {
				m_sLastErrorCode = "-32221";
				m_sLastErrorMessage = m_oFuncCheck.getLastErrorMessage();
				return false;
			}
		}
		
		//get the current check number
		//**** it is for reference only ****
		if(bPass && m_oFuncCheck.updatePaymentInfo(AppGlobal.g_oFuncOutlet.get().getOutletId(), sPeriodId, oPaymentDateTime, AppGlobal.g_oFuncStation.get().getStation().getReceiptPrtqId(), iChosenReceiptPfmtId, bSendAndPaid, bIsFastFoodMode, bIsSelfOrderKioskMode, bIsBarTabMode, false, bCloseCheck, m_iAppliedUserId) == false){
			// Fail to add payment
			m_sLastErrorCode = "-32220";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_save_payments");
			AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Fail to save payments");
			
			bPass = false;
		}
		
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "After update database");
		
		if(bPass && m_oFuncCheck.hasMember()){
			// Update member spending
			updateMemberSpending(m_oFuncCheck.getMemberId(), m_oFuncCheck.getCheckTotal());
		}
		
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Before update customer display");
		
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "After update customer display");
		
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.g_bWriteClientConnectionLog = false;
		
		return true;
	}

	// get VGS QR code
	private void getVgsQrCode(PosInterfaceConfig oPosInterfaceConfig) {
		FuncVGS oFuncVGS = new FuncVGS(oPosInterfaceConfig, m_oFuncCheck, m_oFuncPayment);
		String sPayUrl = oFuncVGS.getPayUrl();
		if (!oFuncVGS.getLastErrorMessage().isEmpty()) {
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(),
					"VGS connection error:" + oFuncVGS.getLastErrorMessage());
		} else {
			String sQrcode = m_oFuncCheck.getCheckExtraInfoBySectionAndVariable(
					PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_QR_CODE, 0);
			if (sQrcode == null || sQrcode.isEmpty())
				m_oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE,
						PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0,
						Integer.toString(oPosInterfaceConfig.getInterfaceId()));
			m_oFuncCheck.addOrUpdateCheckExtraInfoValue(3, PosCheckExtraInfo.BY_CHECK,
					PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_QR_CODE, 0, sPayUrl);
		}
	}

	// Update member spending
	private void updateMemberSpending(int iMemberId, BigDecimal dPayAmount){
		
		if(AppGlobal.isModuleSupport(AppGlobal.OPTIONAL_MODULE.member.name()) == false){
			return;
		}
		
		// *****************************************************************
		// Create thread to update member spending
		AppThreadManager oAppThreadManager = new AppThreadManager();
		
		// Thread 1 : Update member spending
		// Create parameter array
		Object[] oParameters = new Object[2];
		oParameters[0] = iMemberId;
		oParameters[1] = dPayAmount;
		oAppThreadManager.addThread(1, this, "processUpdateMemberSpending", oParameters);
		
		// Run all of the threads
		oAppThreadManager.runThread();
	}
	
	private void waitForProcessCheckFinish(String sTable, String sTableExtension){
		// Timeout for wait = 20 seconds
		int iTimeout = 20000;
		int iSleepInterval = 50;
		int iWaitTime = 0;
		
		// Check if the check is processing or not
		while(m_oProcessingSendChecks.size() > 0){
			// If the thread is still processing, sleep for a while until finish
			try {
				// If table and table extension is passed to this function, check if this check is under send check ONLY, not concern other check
				if(sTable.length() > 0){
					boolean bFound = false;
					for(Entry<String, DateTime> entry:m_oProcessingSendChecks.entrySet()){
						String sKey = sTable + sTableExtension;
						if(entry.getKey().equals(sKey)){
							bFound = true;
							break;
						}
					}
					if(bFound == false)
						// Check is not under sending, no need to wait
						return;
				}
				
				Thread.sleep(iSleepInterval);
			} catch (InterruptedException e) {
				AppGlobal.stack2Log(e);
				
				break;
			}
			
			iWaitTime += iSleepInterval;
			if(iWaitTime >= iTimeout){
				// Timeout, write log and continue operation
				StringBuilder sBuilder = new StringBuilder();
				if(m_oFuncCheck != null) {
					sBuilder.append("Current check: <Check # : " + m_oFuncCheck.getCheckPrefixNo() +
							", table : " + m_oFuncCheck.getTableNoWithExtensionForDisplay() +
							", new item count : " + m_oFuncCheck.getNewItemCount(false).toPlainString() + ">	");
					sBuilder.append("Processing check: ");
					for(Entry<String, DateTime> entry:m_oProcessingSendChecks.entrySet()){
						sBuilder.append("<");
						sBuilder.append(entry.getKey());
						sBuilder.append("> ");
					}
				} else {
					sBuilder.append("Current check: null");
				}
				
				AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[1].getMethodName(), sBuilder.toString());
				
				// Clear all process data
				m_oProcessingSendChecks.clear();
				
				break;
			}
		}
	}
	
	// Process send check in Thread
	private boolean processSendCheck(FuncCheck oFuncCheck, boolean bPrintGuestCheck, boolean bPayCheck, int iOutletId, String sOutletName, int iCheckPrintQueueId, int iCheckFormatId, boolean bDetailCheck, String sStoredProcessingCheckKey, boolean bIsOldCheck){
		boolean bResult = false;

		// Send check
		try{
			String sOrderingMode;
			sOrderingMode = PosCheck.ORDERING_MODE_FINE_DINING;
			
			m_oFuncCheck.calculateLoyaltyTransaction();
			
			String sResult = oFuncCheck.sendCheck(bPrintGuestCheck, bPayCheck, false, iOutletId, sOutletName, iCheckPrintQueueId, iCheckFormatId, bDetailCheck, 0, 0, 0, sOrderingMode, false, false);
			
			if(!sResult.equals(PosCheck.API_RESULT_SUCCESS)){
				// Fail to send check
				// Write application log
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "ERROR: Fail to send check, table " + oFuncCheck.getTableNo() + oFuncCheck.getTableExtension());
				return false;
			}else{
				// Write application log
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Finish send check, table " + oFuncCheck.getTableNo() + oFuncCheck.getTableExtension());
				bResult = true;
			}
		}catch (Exception e) {
			AppGlobal.stack2Log(e);
			
			// Fail to send check
			// Write application log
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "ERROR: Fail to send check, table " + oFuncCheck.getTableNo() + oFuncCheck.getTableExtension());
		}
		
		return bResult;
	}
	
	private String autoDiscountForPayment(String sDiscountType, int iAutoDiscId, BigDecimal dDiscountValue) {
		PosDiscountType oDiscountType = new PosDiscountType();
		
		if(dDiscountValue.compareTo(BigDecimal.ZERO) == 0)
			return FUNC_RESULT_SUCCESS;
		
		// Check if there is restriction on applying multiple discounts
		if(!m_oFuncCheck.checkDiscountApplyRestriction(true)){
			// Cannot apply, skip apply
			return FUNC_RESULT_SUCCESS;
		}
		
		BigDecimal dDiscountRateAmt = BigDecimal.ZERO;
		List<HashMap<String, Integer>> oSelectedItems = new ArrayList<HashMap<String, Integer>>();
		
		if(!(oDiscountType.readById(iAutoDiscId)))
			return FUNC_RESULT_NO_SUCH_RECORD;
		
		if(!oDiscountType.getType().equals(PosDiscountType.TYPE_POST_DISCOUNT)) {
			m_sLastErrorCode = "-32216";
			m_sLastErrorMessage = "the_attached_discount_is_not_post_discount";
			return FUNC_RESULT_FAIL;
		}
		
		if(oDiscountType.isFixAmountDiscountPerCheckMethod() == false) {
			m_sLastErrorCode = "-32217";
			m_sLastErrorMessage = "the_attached_discount_is_not_fix_amount_check_discount";
			return FUNC_RESULT_FAIL;
		}
		
		if(oDiscountType.getFixAmount().compareTo(BigDecimal.ZERO) != 0) {
			m_sLastErrorCode = "-32218";
			m_sLastErrorMessage = "the_attached_discount_is_not_open_discount";
			return FUNC_RESULT_FAIL;
		}
		
		if (sDiscountType.equals("item") && oDiscountType.isFixAmountDiscountPerItemMethod()) {
			if(oDiscountType.getFixAmount().compareTo(BigDecimal.ZERO) == 0) 
				dDiscountRateAmt = dDiscountValue;
			else
				dDiscountRateAmt = oDiscountType.getFixAmount();
		}else if (sDiscountType.equals("check") && (oDiscountType.isFixAmountDiscountPerCheckMethod() || oDiscountType.isFixAmountDiscountPerItemMethod())) {
			if(oDiscountType.getFixAmount().compareTo(BigDecimal.ZERO) == 0)
				dDiscountRateAmt = dDiscountValue;
			else
				dDiscountRateAmt = oDiscountType.getFixAmount();
		}else if (oDiscountType.isPercentageDiscountMethod()) {
			if (oDiscountType.getRate().compareTo(BigDecimal.ZERO) == 0) 
				dDiscountRateAmt = dDiscountValue;
			else
				dDiscountRateAmt = oDiscountType.getRate();
		}
		
		if (oDiscountType.isPercentageDiscountMethod() && dDiscountRateAmt.compareTo(new BigDecimal("100.0")) > 0) {
			m_sLastErrorCode = "-32219";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("invalid_discount_value");
			return FUNC_RESULT_FAIL;
		}
		
		dDiscountRateAmt = dDiscountRateAmt.multiply(new BigDecimal("-1.0"));
		
		oSelectedItems = m_oFuncCheck.getSectionItemIndexForCurrentOrderedItem();
		
		//apply discount
		return applyDiscount(sDiscountType, oSelectedItems, oDiscountType, dDiscountRateAmt);
	}
	
	// preHandlingForApplyDiscount
	private boolean preProcessForApplyDiscount(boolean bBySendCheck, String sDiscountType, String sUsedFor,
			List<HashMap<String, Integer>> oSelectedItems, JSONArray oTargetDiscounts) {
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		PosBusinessDay oBusinessDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay();
		PosDiscountTypeList oDiscTypeList = new PosDiscountTypeList();
		
		//If new check and it is thru function key 'apply_discount', now allow apply discount on new check 
		if(!bBySendCheck && !m_oFuncCheck.isOldCheck()) {
			m_sLastErrorCode = "-32041";
			m_sLastErrorMessage = "cannot apply on new check";
			return false;
		}
		
		if (sDiscountType.equals("check") && oSelectedItems == null) {
			// form the selection items list for check discount
			oSelectedItems = new ArrayList<HashMap<String, Integer>>();
			int iSeatNo = 0, iItemIndex = 0;
			List<List<FuncCheckItem>> oPartyWholeItems = m_oFuncCheck.getWholeItemList();
			for (List<FuncCheckItem> oItemListForSingleSeat : oPartyWholeItems) {
				for (iItemIndex = 0; iItemIndex < oItemListForSingleSeat.size(); iItemIndex++) {
					HashMap<String, Integer> oTempSelectedItem = new HashMap<String, Integer>();
					oTempSelectedItem.put("partySeq", m_oFuncCheck.getCurrentCheckPartySeq());
					oTempSelectedItem.put("sectionId", iSeatNo);
					oTempSelectedItem.put("itemIndex", iItemIndex);
					oSelectedItems.add(oTempSelectedItem);
				}
				iSeatNo++;
			}
		}

		if (oSelectedItems.size() <= 0) {
			m_sLastErrorCode = "-32200";
			m_sLastErrorMessage = "no selected item for discount";
			return false;
		}
		

		// check whether any control on apply discount
		if(AppGlobal.g_oFuncStation.get().getApplyDiscountRestriction() > 0 && oTargetDiscounts.length() > 1) {
			m_sLastErrorCode = "-32079";
			m_sLastErrorMessage = "can only apply one check discount";
			return false;
		}
		
		if (sUsedFor.equals(PosDiscountType.USED_FOR_DISCOUNT) && !m_oFuncCheck.checkDiscountApplyRestriction(sDiscountType.equals("check"))) {
			m_sLastErrorCode = "-32079";
			m_sLastErrorMessage = "discount_had_been_applied_before";
			return false;
		}
		
		// get the available discount
		oDiscTypeList.readDiscountListByOutletId(sDiscountType, AppGlobal.g_oFuncOutlet.get().getShopId(),
				AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncStation.get().getStationId(),
				dateFormat.print(oBusinessDay.getDate()), oBusinessDay.isHoliday(), oBusinessDay.isDayBeforeHoliday(),
				oBusinessDay.isSpecialDay(), oBusinessDay.isDayBeforeSpecialDay(), oBusinessDay.getDayOfWeek(),
				AppGlobal.g_oFuncUser.get().getUserGroupList(), false);
		
		if(oDiscTypeList.getPosDiscountTypeList().size() == 0) {
			m_sLastErrorCode = "-32071";
			m_sLastErrorMessage = "no discount(s) defined";
			return false;
		}
		
		// get target discount
		ArrayList<HashMap<String, Object>> oCheckedDiscs = new ArrayList<HashMap<String, Object>>();
		
		for(int i=0; i<oTargetDiscounts.length(); i++) {
			JSONObject oTargetDisc = oTargetDiscounts.optJSONObject(i);
			PosDiscountType oSelectedDiscType = null;
			String sDiscountCode = "";
			int iDiscId = 0;
			
			if(oTargetDisc.has("id") && oTargetDisc.optInt("id", 0) > 0)
				iDiscId = oTargetDisc.optInt("id");
			if(oTargetDisc.has("code") && !oTargetDisc.optString("code", "").isEmpty())
				sDiscountCode = oTargetDisc.optString("code");
			
			if(iDiscId == 0 && sDiscountCode.isEmpty()) {
				m_sLastErrorCode = "-32602";
				m_sLastErrorMessage = "missing discount code or id";
				return false;
			}
			
			for (PosDiscountType oDiscType : oDiscTypeList.getPosDiscountTypeList()) {
				if((iDiscId != 0 && oDiscType.getDtypId() == iDiscId) || (!sDiscountCode.isEmpty()) && oDiscType.getCode().equals(sDiscountCode)) {
					oSelectedDiscType = oDiscType;
					break;
				}
			}
			
			if(oSelectedDiscType == null) {
				m_sLastErrorCode = "-32602";
				m_sLastErrorMessage = (iDiscId != 0)? "no such discount, id:"+iDiscId : "no such discount, code:"+sDiscountCode;
				return false;
			}
			
			if ((sUsedFor.equals(PosDiscountType.USED_FOR_DISCOUNT) && !oSelectedDiscType.isUsedForDiscount())
					|| (sUsedFor.equals(PosDiscountType.USED_FOR_EXTRA_CHARGE)
							&& !oSelectedDiscType.isUsedForExtraCharge())) {
				m_sLastErrorCode = "-32191";
				m_sLastErrorMessage = (sUsedFor.equals(PosDiscountType.USED_FOR_DISCOUNT))? 
						"cannot apply extra charge for discount action" : "cannot apply discount type for extra charge action";
				return false;
			}
			
			// extra charge not support by item level
			if (sUsedFor.equals(PosDiscountType.USED_FOR_EXTRA_CHARGE) && oSelectedDiscType.isUsedForExtraCharge() 
					&& (oSelectedDiscType.isApplyToItem() || oSelectedDiscType.isFixAmountDiscountPerItemMethod())) {
				m_sLastErrorCode = "-32191";
				m_sLastErrorMessage = "cannot apply item extra charge";
				return false;
			}

			if ((sDiscountType.equals("item") && oSelectedDiscType.isApplyToCheck())
					|| (sDiscountType.equals("check") && oSelectedDiscType.isApplyToItem())
					|| (sDiscountType.equals("item") && oSelectedDiscType.isFixAmountDiscountPerCheckMethod())
					|| (sDiscountType.equals("check") && oSelectedDiscType.isFixAmountDiscountPerItemMethod())) {
				m_sLastErrorCode = "-32191";
				m_sLastErrorMessage = (sDiscountType.equals("item"))? "cannot apply check discount type for item discount" : "cannot apply item discount type for check discount";
				return false;
			}
		
			if (oSelectedDiscType.isPercentageDiscountMethod()) {
				m_sLastErrorCode = "-32191";
				m_sLastErrorMessage = "cannot apply percentage discount";
				return false;
			}
			
			if (oSelectedDiscType.getFixAmount().compareTo(BigDecimal.ZERO) == 0 && !oTargetDisc.has("amount")) {
				m_sLastErrorCode = "-32602";
				m_sLastErrorMessage = "missing discount amount for open discount";
				return false;
			}
			
			BigDecimal dDiscountRateAmt = BigDecimal.ZERO;
			if(oTargetDisc.has("amount")) {
				try {
					Double.valueOf(oTargetDisc.optString("amount", ""));
				} catch (NumberFormatException e) {
					m_sLastErrorCode = "-32602";
					m_sLastErrorMessage = "invalid discount amount";
					return false;
				}
				dDiscountRateAmt = new BigDecimal(oTargetDisc.optString("amount", ""));
				if (!oSelectedDiscType.isUsedForExtraCharge())
					dDiscountRateAmt = dDiscountRateAmt.multiply(new BigDecimal("-1.0"));
			} else
				dDiscountRateAmt = oSelectedDiscType.getFixAmount();
			
			if (!sUsedFor.equals(PosDiscountType.USED_FOR_EXTRA_CHARGE) && dDiscountRateAmt.compareTo(BigDecimal.ZERO) >= 0) {
				m_sLastErrorCode = "-32219";
				m_sLastErrorMessage = "invalid discount value";
				return false;
			}
			
			HashMap<String, Object> oTemp = new HashMap<String, Object>();
			oTemp.put("discountType", oSelectedDiscType);
			oTemp.put("discountAmt", dDiscountRateAmt);
			oCheckedDiscs.add(oTemp);
		}
		
		for(HashMap<String, Object> oTempDisc : oCheckedDiscs) {
			PosDiscountType oSelectedDiscType = (PosDiscountType) oTempDisc.get("discountType");
			BigDecimal dDiscountRateAmt = (BigDecimal) oTempDisc.get("discountAmt");
			
			String sResult = applyDiscount(sDiscountType, oSelectedItems, oSelectedDiscType, dDiscountRateAmt);
			if(sResult.equals(FUNC_RESULT_FAIL))
				return false;
		}
		
		return true;
	}
	
	// apply discount
	public String applyDiscount(String sApplyTo, List<HashMap<String, Integer>> oSelectedItems, PosDiscountType oDiscountType, BigDecimal dDiscountRateAmt) {
		// get the discount allowance
		List<Integer> oItemDiscountGrpList = new ArrayList<Integer>();
		HashMap<Integer, Boolean> oDiscountAllowance = new HashMap<Integer, Boolean>();
		for(HashMap<String, Integer> oSelectedItem:oSelectedItems) {
			FuncCheckItem oParentFuncCheckItem = m_oFuncCheck.getCheckItem(oSelectedItem.get("partySeq"), oSelectedItem.get("sectionId"), oSelectedItem.get("itemIndex"));
			
			// Pre-checking if the item is missing in menu
			if(oParentFuncCheckItem == null) {
				m_sLastErrorCode = "-32211";
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("item_is_missing_in_menu_setup");
				return FUNC_RESULT_FAIL;
			}
			
			if(oParentFuncCheckItem.getMenuItem() == null){
				m_sLastErrorCode = "-32030";
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("item_is_missing_in_menu_setup") + " (" + oParentFuncCheckItem.getItemDescriptionByIndex(AppGlobal.g_oCurrentLangIndex.get()) + ")";
				return FUNC_RESULT_FAIL;
			}
			
			if(sApplyTo.equals("item") && oParentFuncCheckItem.hasItemDiscount(true)) {
				m_sLastErrorCode = "-32079";
				m_sLastErrorMessage = "discount_had_been_applied_before";
				return FUNC_RESULT_FAIL;
			}
			
			if(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId() != 0 && !oItemDiscountGrpList.contains(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId())) {
				boolean bDiscountAllowance = AppGlobal.g_oFuncDiscountAclList.get(AppGlobal.g_oFuncOutlet.get().getOutletId()).checkDiscountAcl(oParentFuncCheckItem.getMenuItem(), oDiscountType);
				oDiscountAllowance.put(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId(), bDiscountAllowance);
			}
		}
		
		List<HashMap<String, Integer>> oItemIndexList = new ArrayList<HashMap<String, Integer>>();
		for(HashMap<String, Integer> oSelectedItem:oSelectedItems) {
			FuncCheckItem oFuncCheckItem = m_oFuncCheck.getCheckItem(oSelectedItem.get("partySeq"), oSelectedItem.get("sectionId"), oSelectedItem.get("itemIndex"));

			if(oFuncCheckItem.getMenuItem().getDiscountItemGroupId() == 0)
				continue;
			if (!oDiscountAllowance.containsKey(oFuncCheckItem.getMenuItem().getDiscountItemGroupId()))
				continue;
			if(oDiscountAllowance.get(oFuncCheckItem.getMenuItem().getDiscountItemGroupId()) == false)
				continue;
			
			//check whether the selected item is available for applying discount
			if(sApplyTo.equals("item") && oFuncCheckItem.getCheckItem().getTotal().compareTo(BigDecimal.ZERO) == 0) 
				continue;
			
			if(sApplyTo.equals("item") && oFuncCheckItem.hasItemDiscount(false)) 
				continue;
			
			oItemIndexList.add(oSelectedItem);
		}
		
		if(!m_oFuncCheck.applyDiscount(sApplyTo, PosDiscountType.USED_FOR_DISCOUNT, oItemIndexList, oDiscountType, dDiscountRateAmt, null, m_iAppliedUserId))
			return FUNC_RESULT_FAIL;
		
		return FUNC_RESULT_SUCCESS;
	}
	
	// Pay check
	private boolean preparePaidCheck(boolean bSendAndPaid) {
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.g_bWriteClientConnectionLog = true;
		
		if(!preCheckingForPayment(bSendAndPaid))
			return false;
		
		// Mix and match function
		mixAndMatchFunction();
		
		// Init FuncPayment for payment process
		m_oFuncPayment.init(m_oFuncCheck.getCheckTotal(), AppGlobal.g_oFuncOutlet.get().getCheckRoundMethod(), AppGlobal.g_oFuncOutlet.get().getCheckRoundDecimal(), AppGlobal.g_oFuncOutlet.get().getPayRoundMethod(), AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal());
		
		return true;
	}
	
	// Before payment checking
	private boolean preCheckingForPayment(boolean bSendAndPaid){
		if(!bSendAndPaid && !m_oFuncCheck.isOldCheck()) {
			m_sLastErrorCode = "-32212";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_pay_new_check");
			return false;
		}
		
		if(m_oFuncCheck.isPaid(false)) {
			m_sLastErrorCode = "-32213";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("check_is_already_paid");
			return false;
		}
		
		if(m_oFuncCheck.havePendingItems()) {
			m_sLastErrorCode = "-32214";
			m_sLastErrorMessage =  AppGlobal.g_oLang.get()._("cannot_pay_check_with_pending_items");
			return false;
		}
		
		if(AppGlobal.g_oFuncStation.get().isSkipPrintCheckForPayment() == false){
			if(!m_oFuncCheck.isPrinted()) {
				m_sLastErrorCode = "-32046";
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("check_is_not_printed");
				return false;
			}
			
			if(!m_oFuncCheck.isCheckTotalEqualToPrintTotal()) {
				m_sLastErrorCode = "-32215";
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("have_unprint_item");
				return false;
			}
		}
		
		// prepare a item list snap shot
		m_oFuncCheck.generateItemSnapShotList(false);
		
		return true;
	}
	
	// Get Applied User Id
	private boolean getAppliedUserId(String sAppliedUserNo){
		if(!sAppliedUserNo.isEmpty()){
			UserUser oUserList = new UserUser();
			JSONArray responseJSONArray = oUserList.searchUser(sAppliedUserNo);
			if (responseJSONArray != null) {
				for (int i = 0; i < responseJSONArray.length(); i++) {
					if (responseJSONArray.isNull(i))
						continue;
					if (responseJSONArray.optJSONObject(i).optJSONObject("UserUser").optString("user_number").equals(sAppliedUserNo)){
						m_iAppliedUserId = responseJSONArray.optJSONObject(i).optJSONObject("UserUser").optInt("user_id");
						break;
					}
				}
			}else
				return false;
		}
		return true;
	}
	
	// Add/Edit check info
	public boolean editCheckInfo(boolean bySendCheck, JSONArray oChkReferences) {
		int iChkInfoCount = 5;
		HashMap<Integer, String> oChkInfoList = m_oFuncCheck.getCheckExtraInfoValueListWithIndexBySectionVariable("",
				PosCheckExtraInfo.VARIABLE_CHECK_INFO);
		HashMap<Integer, String> oTargetChkInfoList = new HashMap<Integer, String>();
		
		if(bySendCheck && (oChkReferences == null || oChkReferences.length() > 5)) {
			m_sLastErrorCode = "-32201";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("empty_or_exceed_maximum_check_info_counts");
			return false;
		}
		
		boolean bPassChecking = true;
		for(int i = 0; i < oChkReferences.length(); i++) {
			JSONObject oChkReference = oChkReferences.optJSONObject(i);
			if(oChkReference == null)
				continue;
			
			if(!oChkReference.has("index") || !oChkReference.has("value")) {
				bPassChecking = false;
				continue;
			}
			
			if(oChkReference.optInt("index") < 1 || oChkReference.optInt("index") > iChkInfoCount) {
				bPassChecking = false;
				continue;
			}
			
			if(oChkReference.optString("value", "").isEmpty()) {
				bPassChecking = false;
				continue;
			}
			
			oTargetChkInfoList.put(oChkReference.optInt("index"), oChkReference.optString("value"));
		}
		
		if(!bPassChecking) {
			m_sLastErrorCode = "-32202";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("invalid_check_references");
			return false;
		}
		
		if(oTargetChkInfoList.size() == 0)
			return true;
			
		for (Entry<Integer, String> entry : oTargetChkInfoList.entrySet()) {
			int iIndex = entry.getKey();
			String sValue = entry.getValue();
			String sLogRemark = "";
			if (!oChkInfoList.containsKey(iIndex) && !sValue.isEmpty()) {
				m_oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, "",
						PosCheckExtraInfo.VARIABLE_CHECK_INFO, (iIndex), sValue);
				sLogRemark = "Add Check Info" + (iIndex) + ": " + sValue;
			} else if (oChkInfoList.containsKey(iIndex) && (oChkInfoList.get(iIndex) == null
					|| !oChkInfoList.get(iIndex).equals(sValue))) {
				m_oFuncCheck.updateCheckExtraInfoValue(PosCheckExtraInfo.BY_CHECK, "",
						PosCheckExtraInfo.VARIABLE_CHECK_INFO, (iIndex), sValue);
				sLogRemark = "Edit Check Info" + (iIndex) + ": " + sValue;
			}

			// Add "add_edit_check_info" log to action log list
			if (!sLogRemark.isEmpty())
				AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.add_edit_check_info.name(),
						PosActionLog.ACTION_RESULT_SUCCESS, m_oFuncCheck.getTableNoWithExtensionForDisplay(),
						AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(),
						AppGlobal.g_oFuncOutlet.get().getOutletId(),
						AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(),
						AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(),
						AppGlobal.g_oFuncStation.get().getStationId(), m_oFuncCheck.getCheckId(), "", "", "", "",
						sLogRemark);
		}
		
		return true;
	}
	
	// attach member
	public boolean setMember(JSONObject oMemberJSONObject) {
		String sInterfaceCode = "";
		if(oMemberJSONObject.has("interfacecode") && !oMemberJSONObject.optString("interfacecode").isEmpty())
			sInterfaceCode = oMemberJSONObject.optString("interfacecode");
		
		if(sInterfaceCode.isEmpty()) {
			//get setup of member validation
			String sMemberValidateSetting = AppGlobal.g_oFuncStation.get().getMemberValidationSetting();
			boolean bValidateWithMembModule = true;
			if(!sMemberValidateSetting.isEmpty()) {
				try {
					JSONObject jsonObject = new JSONObject(sMemberValidateSetting);
					if(jsonObject.optString("no_member_validation_in_set_member", "").equals("y"))
						bValidateWithMembModule = false;
				} catch (JSONException e) {
					e.printStackTrace();
					AppGlobal.stack2Log(e);
				}
			}
			
			// attach member under HERO's member module
			if(bValidateWithMembModule) {
				MemMemberList oMemberList = new MemMemberList();
				if(oMemberJSONObject.has("number") && !oMemberJSONObject.optString("number").isEmpty())
					oMemberList.searchMember(FrameSearchMemberFunction.SEARCH_TYPE.number.name(), oMemberJSONObject.optString("number"), "", 1, 1, MemMember.SEARCH_ALL_ACTIVE);
				if(oMemberList.getMemberList().size() == 1){
					for(MemMember oMemMember: oMemberList.getMemberList().values()) {
						if(oMemMember.getMemberNo().equals(oMemberJSONObject.optString("number"))) {
							m_oFuncCheck.setMember(oMemMember.getMemberId(), null, false, false);
							break;
						}
					}
				}
			}else {
				if(oMemberJSONObject.has("number") && !oMemberJSONObject.optString("number").isEmpty()) {
					m_oFuncCheck.addOrUpdateCheckExtraInfoValue(3, PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0, oMemberJSONObject.optString("number"));
					m_oFuncCheck.addOrUpdateCheckExtraInfoValue(3, PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_EXPIRY_DATE, 0, "");
				}
			}
			
		}else {
			// attach external party member without online enquiry
			String sCardNum = "", sMemberNum = "", sAccountNum = "";
			if(oMemberJSONObject.has("number") && !oMemberJSONObject.optString("number").isEmpty())
				sMemberNum = oMemberJSONObject.optString("number");
			if(oMemberJSONObject.has("cardnumber") && !oMemberJSONObject.optString("cardnumber").isEmpty())
				sCardNum = oMemberJSONObject.optString("cardnumber");
			if(oMemberJSONObject.has("accountnumber") && !oMemberJSONObject.optString("accountnumber").isEmpty())
				sAccountNum = oMemberJSONObject.optString("accountnumber");
			if(sMemberNum.isEmpty() && sCardNum.isEmpty() && sAccountNum.isEmpty()) {
				m_sLastErrorCode = "-32602";
				m_sLastErrorMessage = "missing member or card or account number";
				return false;
			}
			
			// get the corresponding membership interface config, return error if not found
			PosInterfaceConfig oTargetMemberInterface = null;
			List<PosInterfaceConfig> oMemberInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_MEMBERSHIP_INTERFACE);
			for(PosInterfaceConfig oMemberInterfaceConfig : oMemberInterfaceConfigList) {
				if(oMemberInterfaceConfig.getInterfaceCode().equals(sInterfaceCode)) {
					oTargetMemberInterface = oMemberInterfaceConfig;
					break;
				}
			}
			if(oTargetMemberInterface == null) {
				m_sLastErrorCode = "-32156";
				m_sLastErrorMessage = "no interface find with code:" + sInterfaceCode;
				return false;
			}
			
			// check whether have member attached with other interface
			int iAttachedIntfId = 0;
			try {
				iAttachedIntfId = Integer.valueOf(m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID));
			}catch(Exception e) {
				iAttachedIntfId = 0;
			}
			if(m_oFuncCheck.getMemberId() > 0 || (iAttachedIntfId > 0 && oTargetMemberInterface.getInterfaceId() != iAttachedIntfId)) {
				m_sLastErrorCode = "-32162";
				if(m_oFuncCheck.getMemberId() > 0)
					m_sLastErrorMessage = "have hero member attached";
				else
					m_sLastErrorMessage = "have another membership interface member attached";
				return false;
			}
			
			// add / update check's extra info to check
			m_oFuncCheck.addOrUpdateCheckExtraInfoValue(3, PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0, String.valueOf(oTargetMemberInterface.getInterfaceId()));
			if(!sMemberNum.isEmpty())
				m_oFuncCheck.addOrUpdateCheckExtraInfoValue(3, PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0, sMemberNum);
			if(oMemberJSONObject.has("name") && !oMemberJSONObject.optString("name", "").isEmpty())
				m_oFuncCheck.addOrUpdateCheckExtraInfoValue(3, PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NAME, 0, oMemberJSONObject.optString("name"));
			if(!sCardNum.isEmpty())
				m_oFuncCheck.addOrUpdateCheckExtraInfoValue(3, PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_CARD_NO, 0, sCardNum);
			if(!sAccountNum.isEmpty())
				m_oFuncCheck.addOrUpdateCheckExtraInfoValue(3, PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER, 0, sAccountNum);
			if(oMemberJSONObject.has("accountname") && !oMemberJSONObject.optString("accountname", "").isEmpty())
				m_oFuncCheck.addOrUpdateCheckExtraInfoValue(3, PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NAME, 0, oMemberJSONObject.optString("accountname"));
		}
		
		return true;
	}
	
	public String getCheckNo() {
		if (m_oFuncCheck == null)
			return "";
		
		return this.m_oFuncCheck.getCheckPrefixNo();
	}
	
	public String getTargetCheckNo() {
		return m_sTargetCheckNumber;
	}
	
	public String getTableNo() {
		if (m_oFuncCheck == null)
			return "";
		
		return m_oFuncCheck.getTableNoWithExtensionForDisplay();
	}
	
	public String getTargetTableNo() {
		return m_sTargetTableNumber;
	}

	public HashMap<String, String> getTableNoAndTableExtension(String sTableNo) {
		String sTableExtension = "";
		HashMap<String, String> oResultMap = new HashMap<String, String>();
		int iAlphaIndex = -1;
		for (int iTableNo = 0; iTableNo < sTableNo.length(); iTableNo++) {
			char cCurrentChar = sTableNo.charAt(iTableNo);
			if (Character.isLetter(cCurrentChar)) {
				iAlphaIndex = iTableNo;
				break;
			}
		}
		if (iAlphaIndex != -1) {
			sTableExtension = sTableNo.substring(iAlphaIndex);
			sTableNo = sTableNo.substring(0, iAlphaIndex);
		}
		if (sTableNo.isEmpty() && !sTableExtension.isEmpty())
			sTableNo = "0";
		oResultMap.put("tableNo", sTableNo);
		oResultMap.put("tableExt", sTableExtension);
		return oResultMap;
	}
	
	private boolean printCheck() {
		HashMap<String, Object> oResultHashMap = new HashMap<String, Object>();
		
		//TODO: check whether have pending items
		if (m_oFuncCheck.havePendingItems()) {
			//TODO: return error if there is pending item
			m_sLastErrorCode = "-32206";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_print_check_with_pending_items");
			oResultHashMap.put("code", m_sLastErrorCode);
			oResultHashMap.put("message", m_sLastErrorMessage);
			m_oResponsePacket.put("error", oResultHashMap);
			m_oFuncCheck.unlockTable(true, false);
			return false;
		}
		
		//TODO: check print queue of station exist or not
		//get print queue id
		if(AppGlobal.g_oFuncStation.get().getStation().getCheckPrtqId() == 0) {
			m_sLastErrorCode = "-32204";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_print_queue_is_defined");
			oResultHashMap.put("code", m_sLastErrorCode);
			oResultHashMap.put("message", m_sLastErrorMessage);
			m_oResponsePacket.put("error", oResultHashMap);
			m_oFuncCheck.unlockTable(true, false);
			return false;
		}
		
		int iCheckPfmtId = 0;
		//TODO: check whether there is the check format 1 in portal station
		if (AppGlobal.g_oFuncStation.get().getStation().getCheckPfmtId(1) > 0)
			iCheckPfmtId = AppGlobal.g_oFuncStation.get().getStation().getCheckPfmtId(1);
		else {
			m_sLastErrorCode = "-32205";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_print_format_is_defined");
			oResultHashMap.put("code", m_sLastErrorCode);
			oResultHashMap.put("message", m_sLastErrorMessage);
			m_oResponsePacket.put("error", oResultHashMap);
			m_oFuncCheck.unlockTable(true, false);
			return false;
		}
		
		// Calculate the Loyalty transaction
		m_oFuncCheck.calculateLoyaltyTransaction();
		
		//print check function:
		String sResult = m_oFuncCheck.sendCheck(true, false, false, AppGlobal.g_oFuncOutlet.get().getOutletId(),
				AppGlobal.g_oFuncOutlet.get().getOutletNameByIndex(AppGlobal.g_oCurrentLangIndex.get()),
				AppGlobal.g_oFuncStation.get().getCheckPrtqId(), iCheckPfmtId,
				false, 0, 0, 0, PosCheck.ORDERING_MODE_FINE_DINING, false, false);
		
		if(!sResult.equals(PosCheck.API_RESULT_SUCCESS)){
			m_sLastErrorCode = "-32206";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_print_check");
			oResultHashMap.put("code", m_sLastErrorCode);
			oResultHashMap.put("message", m_sLastErrorMessage);
			m_oResponsePacket.put("error", oResultHashMap);
			m_oFuncCheck.unlockTable(true, false);
			return false;
		}
		return true;
	}
	
	private boolean checkIsFullPaid(FuncCheck oFuncCheck) {
		if (oFuncCheck == null || !oFuncCheck.isOldCheck())
			return false;
		
		if (!oFuncCheck.isPaid(false)) {
			// Check is not paid
			HashMap<String, Object> oResultHashMap = new HashMap<String, Object>();
			m_sLastErrorCode = "-32235";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("check_is_not_paid");
			oResultHashMap.put("code", m_sLastErrorCode);
			oResultHashMap.put("message", m_sLastErrorMessage);
			m_oResponsePacket.put("error", oResultHashMap);
			return false;
		}
		return true;
	}
	
	private boolean adjustTips(HashMap<Integer, String> oAdjustPaymentInfo) {
		ArrayList <HashMap<String, String>> oPaymentInfos = new ArrayList<>();
		
		if(!constructionPaymentInfo(oPaymentInfos, oAdjustPaymentInfo))
			return false;
		
		if (!m_oFuncCheck.adjustTips(oPaymentInfos, m_oFuncPayment.getPaymentMethodList())) {
			// fail to adjust tips
			m_sLastErrorCode = "-32239";
			m_sLastErrorMessage = m_oFuncCheck.getLastErrorMessage();
			return false;
		}
		
		return true;
	}
	
	private boolean constructionPaymentInfo(ArrayList<HashMap<String, String>> oPaymentInfos, HashMap<Integer, String> oAdjustPaymentInfo){
		
		PosPaymentMethodList oPaymentMethodList = m_oFuncPayment.getPaymentMethodList();
		
		for (PosCheckPayment oCheckPayment : m_oFuncCheck.getCheckPaymentList()) {
			HashMap<String, String> oPaymentInfo = new HashMap<String, String>();
			PosCheckExtraInfo oCheckExtraInfoDccOptOut = oCheckPayment.getExtraInfoFromList(
					PosCheckExtraInfo.SECTION_CREDIT_CARD, PosCheckExtraInfo.VARIABLE_DCC_OPT_OUT, 0);

			oPaymentInfo.put("payId", String.valueOf(oCheckPayment.getCpayId()));
			oPaymentInfo.put("paymentName", oCheckPayment.getName(AppGlobal.g_oCurrentLangIndex.get()));
			
			if (oPaymentMethodList.getPaymentMethodList().containsKey(oCheckPayment.getPaymentMethodId())
					&& oPaymentMethodList.getPaymentMethod(oCheckPayment.getPaymentMethodId()).HaveTips())
				oPaymentInfo.put("payHaveTips", PosPaymentMethod.TIPS_HAVE_TIPS);
			else
				oPaymentInfo.put("payHaveTips", PosPaymentMethod.TIPS_NO_TIPS);
			
			oPaymentInfo.put("payOriTips", StringLib.BigDecimalToString(oCheckPayment.getPayTips(),
					AppGlobal.g_oFuncOutlet.get().getBusinessDay().getPayDecimal()));
			
			if(oAdjustPaymentInfo.containsKey(oCheckPayment.getPaySeq())) {
				if(!oPaymentInfo.get("payHaveTips").equals(PosPaymentMethod.TIPS_HAVE_TIPS)) {
					m_sLastErrorCode = "-32236";
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("contains_no_tips_payment");
					return false;
				}
				oPaymentInfo.put("payNewTips", oAdjustPaymentInfo.get(oCheckPayment.getPaySeq()));
				oAdjustPaymentInfo.remove(oCheckPayment.getPaySeq());
			}
			else {
				if (oCheckPayment.isPayByForeignCurrency() && oCheckExtraInfoDccOptOut == null)
					oPaymentInfo.put("payNewTips", StringLib.BigDecimalToString(oCheckPayment.getPayForeignTips(),
							AppGlobal.g_oFuncOutlet.get().getBusinessDay().getPayDecimal()));
				else
					oPaymentInfo.put("payNewTips", StringLib.BigDecimalToString(oCheckPayment.getPayTips(),
							AppGlobal.g_oFuncOutlet.get().getBusinessDay().getPayDecimal()));
			}
			
			oPaymentInfo.put("payAmount", StringLib.BigDecimalToString(oCheckPayment.getPayTotal(),
					AppGlobal.g_oFuncOutlet.get().getBusinessDay().getPayDecimal()));
			oPaymentInfo.put("payForeignCurrency", oCheckPayment.getPayForeignCurreny());
			oPaymentInfo.put("PayAmountInForeignCurrency",
					StringLib.BigDecimalToString(oCheckPayment.getPayForeignTotal(),
							AppGlobal.g_oFuncOutlet.get().getBusinessDay().getPayDecimal()));
			oPaymentInfo.put("PayOriTipsInForeignCurrency", StringLib.BigDecimalToString(
					oCheckPayment.getPayForeignTips(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getPayDecimal()));
			// paytypeDcc :credit card paytype with dcc payment
			PosCheckExtraInfo oCheckExtraInfoDcc = oCheckPayment
					.getExtraInfoFromList(PosCheckExtraInfo.SECTION_CREDIT_CARD, PosCheckExtraInfo.VARIABLE_PAYTYPE, 0);
			oPaymentInfo.put("paytypeDcc", (oCheckExtraInfoDcc == null) ? "" : oCheckExtraInfoDcc.getValue());
			// dccOptOut : credit card paytype already dcc opt out
			oPaymentInfo.put("dccOptOut",
					(oCheckExtraInfoDccOptOut == null) ? "" : oCheckExtraInfoDccOptOut.getValue());
			// selectDccOptOut : select credit card paytype as dcc opt out
			oPaymentInfo.put("selectDccOptOut", "false");
			oPaymentInfo.put("payCurrencyCode", oCheckPayment.getCurrencyCode());
			// Find currency sign by currency code
			String sCurrencySign = "$";
			OutCurrency oOutCurrency = new OutCurrency();
			if (oOutCurrency.readActiveByShopOutletIdAndCode(AppGlobal.g_oFuncOutlet.get().getShopId(),
					AppGlobal.g_oFuncOutlet.get().getOutletId(), oCheckPayment.getCurrencyCode())) {
				if (!oOutCurrency.getSign().isEmpty())
					sCurrencySign = oOutCurrency.getSign();
			}
			oPaymentInfo.put("payCurrencySign", sCurrencySign);
			oPaymentInfo.put("chksCheckPrefixNum", m_oFuncCheck.getCheckPrefixNo());
			PosCheckExtraInfo oCheckExtraInfoTraceNum = oCheckPayment.getExtraInfoFromList(
					PosCheckExtraInfo.SECTION_CREDIT_CARD, PosCheckExtraInfo.VARIABLE_TRACE_ID, 0);
			oPaymentInfo.put("traceNum", (oCheckExtraInfoTraceNum == null) ? "" : oCheckExtraInfoTraceNum.getValue());
			oPaymentInfos.add(oPaymentInfo);
		}
		
		// payment sequence number checking
		if(!oAdjustPaymentInfo.isEmpty()) {
			m_sLastErrorCode = "-32237";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("payment_not_found");
			return false;
		}
		
		return true;
	}
	
	private void rollbackPayment() {
		if ((m_oFuncCheck != null && !m_oFuncCheck.isPaid(false))) {
			List<PosCheckPayment> oCheckPaymentList = m_oFuncPayment.getCheckPaymentList();
			for (PosCheckPayment oCheckPayment : oCheckPaymentList)
				rollbackAutoDiscount(oCheckPayment, new PosVoidReason(0));
			for(int i = 0 ; i < m_oFuncPayment.getCheckPaymentListCount() ; i++)
				m_oFuncPayment.deletePayment(i);
		}
	}
	
	private void rollbackAutoDiscount(PosCheckPayment oCheckPayment, PosVoidReason oVoidReason) {
		int iAutoDiscId = 0;
		String sAutoDiscType = "item";
		PosPaymentMethodList oPosPaymentMethodList = m_oFuncPayment.getPaymentMethodList();
		
		if (oPosPaymentMethodList.getPaymentMethodList().containsKey(oCheckPayment.getPaymentMethodId()) == false)
			return;
		PosPaymentMethod oPosPaymentMethod = oPosPaymentMethodList.getPaymentMethodList()
				.get(oCheckPayment.getPaymentMethodId());
		
		// Check whether have auto discount
		if (!oPosPaymentMethod.getAutoDiscountTypeId().equals("")) {
			iAutoDiscId = Integer.valueOf(oPosPaymentMethod.getAutoDiscountTypeId());
			if (oPosPaymentMethod.isAutoCheckDiscountType())
				sAutoDiscType = "check";
		}
		
		// roll back the attached discount
		if (iAutoDiscId > 0) {
			if (sAutoDiscType.equals("check")) {
				List<HashMap<String, String>> oItemSnapShotList = m_oFuncCheck.getItemSnapShotList();
				String sCheckDiscSnapShot = "";
				for (HashMap<String, String> oItemSnapShot : oItemSnapShotList) {
					if (oItemSnapShot.containsKey("checkDiscountInfo")) {
						sCheckDiscSnapShot = oItemSnapShot.get("checkDiscountInfo");
						break;
					}
				}
				
				int iDiscountIndex = -1;
				HashMap<Integer, Integer> oCheckDiscSnapShotList = new HashMap<Integer, Integer>();
				StringTokenizer oStrTok = new StringTokenizer(sCheckDiscSnapShot, ",");
				while (oStrTok.hasMoreElements()) {
					String oTempString = oStrTok.nextToken();
					StringTokenizer oStrTok2 = new StringTokenizer(oTempString, ":");
					if (oStrTok2.hasMoreElements()) {
						String sDiscountIndex = oStrTok2.nextToken();
						String sDiscountTypeId = oStrTok2.nextToken();
						oCheckDiscSnapShotList.put(Integer.valueOf(sDiscountIndex).intValue(),
								Integer.valueOf(sDiscountTypeId).intValue());
					}
				}
				
				int iCurrentPartySeq = m_oFuncCheck.getCurrentCheckPartySeq();
				List<PosCheckDiscount> oCheckDiscList = m_oFuncCheck.getCheckPartyBySeq(iCurrentPartySeq)
						.getPartyCheckDiscount();
				for (PosCheckDiscount oCheckDiscount : oCheckDiscList) {
					if (oCheckDiscount.getDtypId() == iAutoDiscId) {
						if (!oCheckDiscSnapShotList.containsKey(oCheckDiscount.getSeq())) {
							iDiscountIndex = oCheckDiscount.getSeq();
							break;
						}
					}
				}
				
				// really roll back and void the check discount
				if (iDiscountIndex != -1)
					m_oFuncCheck.voidDiscount(sAutoDiscType, PosDiscountType.USED_FOR_DISCOUNT, null, iDiscountIndex,
							oVoidReason.getVdrsId(), "");
			}
		}
	}
	
	// process ordering check
	private boolean processOrderCheck(JSONObject oFuncParamJSONObj, HashMap<String, Object> oResultHashMap, String sTableNo) {
		try {
			if(m_oFuncCheck.isPrinted()) {
				if(oFuncParamJSONObj.optString("sendoverride").equals("0")) {
					//Return error, check printed
					quitCheck();
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", "-32106");
					oResultHashMap.put("message", "Check is printed and not allow to send check again");
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
			}
			
			//Set checks Open User Id and modified user id as Applied User Id
			if(m_iAppliedUserId != 0){
				if(!m_oFuncCheck.isOldCheck())
					m_oFuncCheck.setOpenUserId(m_iAppliedUserId);
				List<PosCheckDiscount> oPosCheckDiscountList = m_oFuncCheck.getCheckDiscountList();
				if(oPosCheckDiscountList != null){
					for (PosCheckDiscount oDiscount : m_oFuncCheck.getCheckDiscountList())
						oDiscount.setApplyUserId(m_iAppliedUserId);
				}
			}
			
			//attach member
			if(oFuncParamJSONObj.has("member")){
				if(!setMember(oFuncParamJSONObj.optJSONObject("member"))) {
					//Return error
					quitCheck();
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
			}
			
			if(oFuncParamJSONObj.has("attributes")) {
				// load attribute setting from table
				PosAttributeTypeList oPosAttributeTypeList = new PosAttributeTypeList();
				oPosAttributeTypeList.readAttributeTypesByType(PosAttributeType.TYPE_CHECK);
				// check if attributes setup empty
				if (oPosAttributeTypeList.getAttributeTypeList().size() <= 0) {
					//Return error
					quitCheck();
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", "-32207");
					oResultHashMap.put("message", AppGlobal.g_oLang.get()._("invalid_format_of_attribute_type_or_option"));
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				JSONArray oAttJSONArray = oFuncParamJSONObj.optJSONArray("attributes");
				//get old attributes
				PosCheckAttribute oPosCheckAttribute = m_oFuncCheck.getCheckAttribute();
				//buffer the old attributes
				int attoId[] = new int[10];
				for(int i = 0; i < 10; i++) 
					attoId[i] = oPosCheckAttribute.getAttrAttoId()[i];
				
				//update attributes
				for (int i = 0; i < oAttJSONArray.length(); i++) {
					JSONObject oAttJSONObject = oAttJSONArray.getJSONObject(i);
					int iAttKey = -1;//attribute key ID: pointer to the 10 registers for storing iAttCode
					int iAttCode = -1;//storing the attribute option ID
					try {
						iAttKey = oAttJSONObject.getInt("key");
						iAttCode = oAttJSONObject.getInt("code");
					}catch(Exception e) {
						//Return error
						quitCheck();
						oResultHashMap.put("tableno", sTableNo);
						oResultHashMap.put("code", "-32207");
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("invalid_format_of_attribute_type_or_option"));
						m_oResponsePacket.put("error", oResultHashMap);
						return false;
					}
					//attribute exist check
					if (iAttKey < 1 || iAttKey > 10 || !oPosAttributeTypeList.attExist(iAttCode)) {
						//Return error
						quitCheck();
						oResultHashMap.put("tableno", sTableNo);
						oResultHashMap.put("code", "-32207");
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("invalid_format_of_attribute_type_or_option"));
						m_oResponsePacket.put("error", oResultHashMap);
						return false;
					}
					//set attribute
					attoId[iAttKey-1] = iAttCode;
				}
				oPosCheckAttribute.setAttrAttoId(attoId);
				oPosCheckAttribute.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
				m_oFuncCheck.setCheckAttribute(oPosCheckAttribute);
			}
			
			JSONArray oItemsJSONArray = oFuncParamJSONObj.optJSONArray("items");
			//Add hot items
			for(int i = 0; i < oItemsJSONArray.length(); i++) {
				JSONObject oItemJSONObject = oItemsJSONArray.getJSONObject(i);
				int iItemId = 0, iTargetPriceLevel = -1;
				String sItemCode = "", sOpenDesc = "";
				BigDecimal dQty, dOpenPrice = null;
				List<Integer> oPredefinedChildItemSeqList = new ArrayList<Integer>();
				
				//get itemId and itemCode
				if(oItemJSONObject.has("id") && !oItemJSONObject.optString("id").isEmpty()) {
					iItemId = Integer.valueOf(oItemJSONObject.getString("id"));
				}
				else {
					if(!oItemJSONObject.optString("code", "").isEmpty())
						sItemCode = oItemJSONObject.optString("code", "");
					else {
						//Missing parameters
						quitCheck();
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("missing_parameters");
						//m_oFuncCheck.unlockTable(false, false);
						oResultHashMap.put("tableno", sTableNo);
						oResultHashMap.put("code", "-32602");
						oResultHashMap.put("message", m_sLastErrorMessage);
						
						m_oResponsePacket.put("error", oResultHashMap);
						return false;
					}
				}
				//get quantity
				dQty = BigDecimal.ONE;
				if(oItemJSONObject.has("qty")) {
					if(!oItemJSONObject.optString("qty", "1.0").isEmpty())
						dQty = new BigDecimal(oItemJSONObject.optString("qty", "1.0"));
				}
				sOpenDesc = oItemJSONObject.optString("opendesc", "");
				if(!oItemJSONObject.optString("openprice").isEmpty())
					dOpenPrice = new BigDecimal(oItemJSONObject.getString("openprice"));
				//get price level
				if(oItemJSONObject.has("pricelevel")) {
					iTargetPriceLevel = oItemJSONObject.optInt("pricelevel", -1);
					if(iTargetPriceLevel > 10 || iTargetPriceLevel < -1)
						iTargetPriceLevel = -1;
				}
				
				if(iTargetPriceLevel == -1 && oFuncParamJSONObj.has("pricelevel")) {
					iTargetPriceLevel = oFuncParamJSONObj.optInt("pricelevel", -1);
					if(iTargetPriceLevel > 10 || iTargetPriceLevel < -1)
						iTargetPriceLevel = -1;
				}
				
				String sIsTakeAway = "", sCourseNo = "";
				int iSeatNo = 0, iCourseNo = 0;
				if(oItemJSONObject.has("istakeaway"))
					sIsTakeAway = oItemJSONObject.optString("istakeaway");
				if(oItemJSONObject.has("seatno"))
					iSeatNo = oItemJSONObject.optInt("seatno", 0);
				if(oItemJSONObject.has("courseno"))
					sCourseNo = oItemJSONObject.optString("courseno");
				if(!sCourseNo.isEmpty()) {
					if(m_oMenuItemCourseList == null) {
						m_oMenuItemCourseList = new MenuItemCourseList();
						m_oMenuItemCourseList.readItemCourseList();
					}
					for(MenuItemCourse oMenuItemCourse:m_oMenuItemCourseList.getItemCourseList()){
						if(oMenuItemCourse.getCode().equals(sCourseNo))
						{
							iCourseNo = oMenuItemCourse.getIcouId();
							break;
						}
					}
				}
				m_sLastErrorMessage = addHotItem(iItemId, sItemCode, iTargetPriceLevel, dQty, sOpenDesc, dOpenPrice, sIsTakeAway, iSeatNo, iCourseNo);
				
				if(!m_sLastErrorMessage.isEmpty()) {
					//Add item error
					//m_oFuncCheck.unlockTable(false, false);
					quitCheck();
					ArrayList<Object> oItemsArrayList = new ArrayList<Object>();
					HashMap<String, Object> oItemsHashMap = new HashMap<String, Object>();
					oItemsHashMap.put("id", Integer.toString(iItemId));
					oItemsArrayList.add(oItemsHashMap);
					
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					oResultHashMap.put("items", oItemsArrayList);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				
				//Add default set menu item
				FuncCheckItem oParentFuncCheckItem = m_oFuncCheck.getCheckItem(iSeatNo, m_oFuncCheck.getItemListCount(iSeatNo) - 1);
				boolean bHaveChildItem = false;
				if(oParentFuncCheckItem.isSetMenu()) {
					if(!m_oStoredSetMenuLookupList.containsKey(oParentFuncCheckItem.getMenuItemId())) {
						MenuSetMenuLookupList oMenuSetMenuLookupList = new MenuSetMenuLookupList();
						oMenuSetMenuLookupList.readMenuSetMenuLookupListById(oParentFuncCheckItem.getMenuItemId());
						m_oStoredSetMenuLookupList.put(oParentFuncCheckItem.getMenuItemId(), oMenuSetMenuLookupList);
					}
					
					if(m_oStoredSetMenuLookupList.get(oParentFuncCheckItem.getMenuItemId()).getLookupList().size() > 0) {
						for(MenuSetMenuLookup oSetMenuLookup : m_oStoredSetMenuLookupList.get(oParentFuncCheckItem.getMenuItemId()).getLookupList()) {
							// Check whether it is default child item
							if(oSetMenuLookup.getType().equals(MenuSetMenuLookup.TYPE_CHILD_ITEM)) {
								oPredefinedChildItemSeqList.add(oSetMenuLookup.getSeq());
								m_sLastErrorMessage = addSetMenuChildItem(oSetMenuLookup.getChildItemId(), oParentFuncCheckItem.getCheckItem().getPriceLevel(), iSeatNo, oSetMenuLookup.getChildItemBaseQty(), oSetMenuLookup.getSeq());
								if(!m_sLastErrorMessage.isEmpty()) {
									quitCheck();
									oResultHashMap.put("tableno", sTableNo);
									oResultHashMap.put("code", m_sLastErrorCode);
									oResultHashMap.put("message", m_sLastErrorMessage);
									m_oResponsePacket.put("error", oResultHashMap);
									return false;
								}
								bHaveChildItem = true;
							}
						}
					}
				}
				
				//Add hot modifiers
				JSONArray oModiJSONArray = oItemJSONObject.optJSONArray("modifiers");
				if(oModiJSONArray != null) {
					for(int j = 0; j < oModiJSONArray.length(); j++) {
						JSONObject oModiJSONObject = oModiJSONArray.getJSONObject(j);
						String sModifierId = oModiJSONObject.optString("modiid");
						String sModifierCode = oModiJSONObject.optString("code");
						if(sModifierId.length() == 0 && sModifierCode.length() != 0) {
							MenuItem oMenuItem = new MenuItem();
							oMenuItem.readByItemCode(sModifierCode);
							if(oMenuItem != null)
								sModifierId = String.valueOf(oMenuItem.getItemId());
						}
						String sOpenModiDesc = oModiJSONObject.optString("opendesc");
						int iModifierListIndex = oModiJSONObject.optInt("modilistindex");
						if(!sModifierId.equals("")) {
							m_sLastErrorMessage = addHotModifier(Integer.valueOf(sModifierId), iModifierListIndex, sOpenModiDesc, iSeatNo);
							if(!m_sLastErrorMessage.equals("")) {
								//Add modifier error
								quitCheck();
								oResultHashMap.put("tableno", sTableNo);
								oResultHashMap.put("code", m_sLastErrorCode);
								oResultHashMap.put("message", m_sLastErrorMessage);
								
								m_oResponsePacket.put("error", oResultHashMap);
								return false;
							}
						}
					}
				}
				
				//Add self select set menu item
				JSONArray oChildsJSONArray = oItemJSONObject.optJSONArray("childitems");
				if(oChildsJSONArray != null) {
					// Retrieve the set menu content lookup
					List<MenuSetMenuLookup> oSetMenuLookupsList = null;
					if(m_oStoredSetMenuLookupList.containsKey(iItemId)) {
						oSetMenuLookupsList = m_oStoredSetMenuLookupList.get(iItemId).getLookupList();
					} else {
						sItemCode = oItemJSONObject.optString("code", "");
						if(iItemId == 0 && sItemCode.length() > 0) {
							MenuItem oMenuItem = new MenuItem();
							oMenuItem.readByItemCode(sItemCode);
							if(oMenuItem != null)
								iItemId = oMenuItem.getItemId();
						}
						MenuSetMenuLookupList oMenuSetMenuLookupList = new MenuSetMenuLookupList();
						oMenuSetMenuLookupList.readMenuSetMenuLookupListById(iItemId);
						oSetMenuLookupsList = oMenuSetMenuLookupList.getLookupList();
						m_oStoredSetMenuLookupList.put(iItemId, oMenuSetMenuLookupList);
						
						// Create thread to load items and menu
						AppThreadManager oAppThreadManager = new AppThreadManager();
						int iThreadCount = 0;
						for(int cnt = 0; cnt < oSetMenuLookupsList.size(); cnt++) {
							MenuSetMenuLookup oMenuSetMenuLookup = oSetMenuLookupsList.get(cnt);
							if(oMenuSetMenuLookup.isChildItem()) {
								int iChildItemId = oMenuSetMenuLookup.getChildItemId();
								
								// Add the method to the thread manager
								// Thread : load item
								Object[] oParameters = new Object[1];
								oParameters[0] = iChildItemId;
								oAppThreadManager.addThread(iThreadCount, AppGlobal.g_oFuncMenu.get(), "getFuncMenuItemByItemId", oParameters);
								
								iThreadCount++;
							}
							else if(oMenuSetMenuLookup.isSelfSelectMenu()) {
								int iMenuId = oMenuSetMenuLookup.getSelectMenuId();
								
								// Add the method to the thread manager
								// Thread : load menu
								Object[] oParameters = new Object[1];
								oParameters[0] = iMenuId;
								oAppThreadManager.addThread(iThreadCount, AppGlobal.g_oFuncMenu.get(), "getMenuAndContentById", oParameters);
								
								iThreadCount++;
							}
						}
						
						if(iThreadCount > 0) {
							oAppThreadManager.runThread();
							oAppThreadManager.waitForThread();
						}
					}
					
					
					for(int k = 0; k < oChildsJSONArray.length(); k++) {
						JSONObject oChildJSONObject = oChildsJSONArray.getJSONObject(k);
						String sChildItemId = "";
						String sChildItemCode = "";
						if(oChildJSONObject.has("childitemid"))
							sChildItemId = oChildJSONObject.optString("childitemid");
						if(oChildJSONObject.has("code"))
							sChildItemCode = oChildJSONObject.optString("code", "");
						if(sChildItemId.equals("") && sChildItemCode.length() > 0) {
							MenuItem oMenuItem = new MenuItem();
							oMenuItem.readByItemCode(sChildItemCode);
							if(oMenuItem != null)
								sChildItemId = Integer.toString(oMenuItem.getItemId());
						}
						
						int iChildItemListIndex = oChildJSONObject.optInt("belongtolulist");
						if (!sChildItemId.isEmpty()) {
							int iChildItemSeq = 0;
							if (oChildJSONObject.has("itemsequence"))
								iChildItemSeq = oChildJSONObject.optInt("itemsequence");
							//skip predefined child item first
							if (oPredefinedChildItemSeqList.contains(iChildItemSeq))
								continue;
							
							//Find Child Max Order and Price Level
							int iPriceLevel = oParentFuncCheckItem.getCheckItem().getPriceLevel();
							if(iChildItemListIndex > 0) {		//Index of self select list should start from 1
								for(int cnt = 0; cnt < oSetMenuLookupsList.size(); cnt++) {
									MenuSetMenuLookup oMenuSetMenuLookup = oSetMenuLookupsList.get(cnt);
									if(oMenuSetMenuLookup.isSelfSelectMenu() && oMenuSetMenuLookup.getSeq() == iChildItemListIndex) {
										MenuMenu oSelfSelectMenu = AppGlobal.g_oFuncMenu.get().getMenuAndContentById(oMenuSetMenuLookup.getSelectMenuId());
										int iMaxOrder = oSelfSelectMenu.getChildMaxOrder();
										if(iMaxOrder != 0 && k >= iMaxOrder){
											//Exceed child max order limit
											continue;
										}
										
										if(oMenuSetMenuLookup.isChangePriceLevel())
											iPriceLevel = oMenuSetMenuLookup.getPriceLevel();
									}
								}
								
							}
							
							// add self-select child items in set menu
							m_sLastErrorMessage = addSetMenuChildItem(Integer.valueOf(sChildItemId), iPriceLevel, iSeatNo, BigDecimal.ONE, 0);
							if(!m_sLastErrorMessage.equals("")) {
								//Add child item error
								//m_oFuncCheck.unlockTable(false, false);
								quitCheck();
								oResultHashMap.put("tableno", sTableNo);
								oResultHashMap.put("code", m_sLastErrorCode);
								oResultHashMap.put("message", m_sLastErrorMessage);
								
								m_oResponsePacket.put("error", oResultHashMap);
								return false;
							}
							
							// add self-select set menu child item modifier
							JSONArray oChildModiJSONArray = oChildJSONObject.optJSONArray("modifiers");
							if (oChildModiJSONArray != null) {
								for (int j = 0; j < oChildModiJSONArray.length(); j++) {
									JSONObject oModiJSONObject = oChildModiJSONArray.getJSONObject(j);
									String sModifierId = oModiJSONObject.optString("modiid");
									String sModifierCode = oModiJSONObject.optString("code");
									if (sModifierId.length() == 0 && sModifierCode.length() != 0) {
										MenuItem oMenuItem = new MenuItem();
										oMenuItem.readByItemCode(sModifierCode);
										if (oMenuItem != null)
											sModifierId = String.valueOf(oMenuItem.getItemId());
									}
									String sOpenModiDesc = oModiJSONObject.optString("opendesc");
									int iModifierListIndex = oModiJSONObject.optInt("modilistindex");
									
									if (!sModifierId.isEmpty()) {
										int iCurrentItemIndex = m_oFuncCheck.getItemListCount(iSeatNo) - 1;
										FuncCheckItem oLastCheckItem = m_oFuncCheck.getCheckItem(iSeatNo, iCurrentItemIndex);
										ArrayList<FuncCheckItem> oTempCheckItemList = new ArrayList<FuncCheckItem>();
										FuncCheckItem oLastChildItem = null;
										if (oLastCheckItem.isSetMenu()) {
											oTempCheckItemList = oLastCheckItem.getChildItemList();
											oLastChildItem = oTempCheckItemList.get(oTempCheckItemList.size() - 1);
										}
										
										String m_sLastErrorMessage = addModifier(oLastChildItem, Integer.valueOf(sModifierId), iModifierListIndex, new BigDecimal("1.0"), sOpenModiDesc);
										if (!m_sLastErrorMessage.isEmpty()) {
											//Add modifier error
											quitCheck();
											oResultHashMap.put("tableno", sTableNo);
											oResultHashMap.put("code", m_sLastErrorCode);
											oResultHashMap.put("message", m_sLastErrorMessage);
											
											m_oResponsePacket.put("error", oResultHashMap);
											return false;
										}
									}
								}
							}
							bHaveChildItem = true;
						}
					}
					
					// add pre-defined set menu item modifiers only, not need to add predefine child item again
					for (int k = 0; k < oChildsJSONArray.length(); k++) {
						JSONObject oChildJSONObject = oChildsJSONArray.getJSONObject(k);
						int iItemSeq = 0;
						if (oChildJSONObject.has("itemsequence"))
							iItemSeq = oChildJSONObject.optInt("itemsequence");
						if (oPredefinedChildItemSeqList.contains(iItemSeq)) {
							JSONArray oChildModiJSONArray = oChildJSONObject.optJSONArray("modifiers");
							if (oChildModiJSONArray != null) {
								for (int j = 0; j < oChildModiJSONArray.length(); j++) {
									JSONObject oModiJSONObject = oChildModiJSONArray.getJSONObject(j);
									String sModifierId = oModiJSONObject.optString("modiid");
									String sModifierCode = oModiJSONObject.optString("code");
									if (sModifierId.length() == 0 && sModifierCode.length() != 0) {
										MenuItem oMenuItem = new MenuItem();
										oMenuItem.readByItemCode(sModifierCode);
										if (oMenuItem != null)
											sModifierId = String.valueOf(oMenuItem.getItemId());
									}
									String sOpenModiDesc = oModiJSONObject.optString("opendesc");
									int iModifierListIndex = oModiJSONObject.optInt("modilistindex");
									
									if (!sModifierId.isEmpty()) {
										int iCurrentItemIndex = m_oFuncCheck.getItemListCount(iSeatNo) - 1;
										FuncCheckItem oLastCheckItem = m_oFuncCheck.getCheckItem(iSeatNo, iCurrentItemIndex);
										ArrayList<FuncCheckItem> oTempCheckItemList = new ArrayList<FuncCheckItem>();
										if (oLastCheckItem.isSetMenu())
											oTempCheckItemList = oLastCheckItem.getChildItemList();
										for (FuncCheckItem oChildItem : oTempCheckItemList) {
											if (oChildItem.getChildItemSeqInSetMenuLookup() == iItemSeq) {
												String m_sLastErrorMessage = addModifier(oChildItem, Integer.valueOf(sModifierId), iModifierListIndex, new BigDecimal("1.0"), sOpenModiDesc);
												if (!m_sLastErrorMessage.isEmpty()) {
													//Add modifier error
													quitCheck();
													oResultHashMap.put("tableno", sTableNo);
													oResultHashMap.put("code", m_sLastErrorCode);
													oResultHashMap.put("message", m_sLastErrorMessage);
													
													m_oResponsePacket.put("error", oResultHashMap);
													return false;
												}
											}
										}
									}
								}
							}
						}
					}
				}
				
				//Add all child items in m_oStoredFuncCheckItemList to item list in check party
				if(bHaveChildItem)
					this.finishAddItem();
			}
			
			if(oFuncParamJSONObj.has("pending") && oFuncParamJSONObj.optString("pending").equals("1")) {
				for(int i=0; i<=AppGlobal.MAX_SEATS; i++) {
					if(m_oFuncCheck.getItemList(i).isEmpty())
						continue;
					for(FuncCheckItem oFuncCheckItem:m_oFuncCheck.getItemList(i)) {
						oFuncCheckItem.setPendingItem(PosCheckItem.PENDING_PENDING_ITEM);
						// Add log to action log list
						oFuncCheckItem.addActionLog(AppGlobal.FUNC_LIST.select_pending_item.name(), PosActionLog.ACTION_RESULT_SUCCESS, m_oFuncCheck.getTableNoWithExtensionForDisplay(), AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), m_oFuncCheck.getCheckId(), "", "", "", "",  "Pending");
						
					}
				}
			}
			
			//Apply check discount
			if(oFuncParamJSONObj.has("discounts") && oFuncParamJSONObj.optJSONArray("discounts").length() > 0) {
				if(preProcessForApplyDiscount(true, "check", PosDiscountType.USED_FOR_DISCOUNT, null, oFuncParamJSONObj.optJSONArray("discounts")) == false){
					//fail to apply
					quitCheck();
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
			}
			
			//Edit check info
			if(oFuncParamJSONObj.has("references")) {
				if(!editCheckInfo(true, oFuncParamJSONObj.optJSONArray("references"))) {
					//fail to edit
					quitCheck();
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
			}
			
			if(oFuncParamJSONObj.has("typecode")) {
				PosCustomTypeList oPosCustomTypeList  = new PosCustomTypeList();
				oPosCustomTypeList.getCustomTypesByType(PosCustomType.TYPE_CHECK);
				boolean bMatch = false;
				//check if check custom type code is valid
				for (Entry<Integer, PosCustomType> oEntries : oPosCustomTypeList.getTypeList().entrySet()) {
					if(oFuncParamJSONObj.optString("typecode").equals(oEntries.getValue().getCtypCode())) {
						m_oFuncCheck.setCustomType(oEntries.getKey());
						bMatch = true;
						break;
					}
				}
				
				if(!bMatch) {
					quitCheck();
					m_sLastErrorCode = "-32206";
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("invalid_check_type");
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
			}
		}catch(JSONException e) {
			AppGlobal.stack2Log(e);
			oResultHashMap.put("code", "-32001");
			oResultHashMap.put("message", e.toString());
			m_oResponsePacket.put("error", oResultHashMap);
			return false;
		}
		
		return true;
	}
	
	private PosCheckExtraInfo constructCheckExtraInfo(String sBy, String sSection, String sVariable, String sValue) {
		PosCheckExtraInfo oPosCheckExtraInfo = new PosCheckExtraInfo();
		oPosCheckExtraInfo.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oPosCheckExtraInfo.setBy(sBy);
		oPosCheckExtraInfo.setSection(sSection);
		oPosCheckExtraInfo.setVariable(sVariable);
		oPosCheckExtraInfo.setValue(sValue);
		return oPosCheckExtraInfo;
	}
	
	@Override
	public void FuncCheck_updateItemStockQty(int iItemId) {
		
	}
	
	@Override
	public void FuncCheck_finishSendCheck(String sStoredProcessingCheckKey) {
		//Finish send check, remove process to stored processing check list
		this.removeProcessCheck(sStoredProcessingCheckKey);
	}
	
	@Override
	public boolean FuncCheck_creditCardSpectraVoidPayment(PosCheckPayment oCheckPayment, String sCreditCardMethodType,
			boolean bIsFirstPosting) {
		return false;
	}
	
	@Override
	public boolean FuncCheck_creditCardSpectraAdjustTips(String sTraceNo, BigDecimal oNewPayTotal, BigDecimal oNewTips,
			boolean bIsFirstPosting) {
		return false;
	}
	
	@Override
	public boolean FuncCheck_creditCardSpectraDccOptOut(String sChksCheckPrefixNum, String sTraceNum,
			JSONObject oRefDataJSONObject, boolean bIsFirstPosting) {
		return false;
	}
	
	@Override
	public int FuncCheck_getReceiptFormat() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void FuncCheck_updateProcessingCheckInfo(String sStoredProcessingCheckKey, JSONObject oSendJSONRequest) {
		
	}
	
	@Override
	public void FuncCheck_rollbackTaxAndSCForReleasePayment(FuncCheck oFuncheck, PosCheckPayment oPosCheckPayment,
			PosVoidReason oPosVoidReason) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean FuncCheck_selectContinuousPrint() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean FuncCheck_confirmToVoidPayment(String sPaymentMethodName, String sPaymentAmount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void FuncCheck_updateBasketExtendBarCheckTotal() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean FuncCheck_isRollbackNeededForCheckMaximum(FuncCheck oFuncCheck, BigDecimal dAdditionalAmount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void FuncCheck_updateBasketItemPrice(int iSeatNo, int iItemIndex, FuncCheckItem oFuncCheckItem) {
		// TODO Auto-generated method stub
		
	}
}
