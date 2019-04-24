package app;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import externallib.StringLib;
import externallib.Util;
import om.OutFloorPlan;
import om.OutFloorPlanFunc;
import om.OutFloorPlanMap;
import om.OutFloorPlanTable;
import om.OutOutlet;
import om.OutShop;
import om.OutSpecialHour;
import om.OutSpecialHourList;
import om.OutTableSectionList;
import om.OutletGeneral;
import om.PosBusinessDay;
import om.PosBusinessPeriod;
import om.PosCheck;
import om.PosGeneral;
import om.PosGratuity;
import om.PosGratuityList;
import om.PosOutletItem;
import om.PosOutletSetting;
import om.PosItemPrintQueueList;
import om.PosStockTransaction;

public class FuncOutlet {
	// Outlet
	private OutOutlet m_oOutlet;
	
	// Shop
	private OutShop m_oShop;
	
	// Outlet setting
	private PosOutletSetting m_oOutletSetting;
	
	// Business day
	private PosBusinessDay m_oBusinessDay;
	
	// Business period setup
	private LinkedHashMap<String,PosBusinessPeriod> m_oBusinessPeriods;
	
	// Special period
	private OutSpecialHourList m_oSpecialPeriodList;
	
	// Panel
	private OutFloorPlanFunc m_oFloorPlanFunc;
	
	// Item Print Queue List
	private PosItemPrintQueueList m_oItemPrintQueueList;
	
	// Override check round
	private boolean m_bOverrideCheckRound;
	private String m_sOverrideCheckRoundMethod;
	private int m_iOverrideCheckRoundDecimal;
	
	// Count the number of Payment before print action slip for questionnaire
	private int m_iQuestionnaireCount = 0;
	
	// Table and table name pair list
	public List<HashMap<String, String>> m_oListTablesInfo;
	
	// Manual change price level
	// -1 : no change manually
	// >0 : Price level change manually
	private int m_iManualPriceLevel;
	
	//Sections list
	private OutTableSectionList m_oOutTableSectionList;
	
	// Store the Default Pos Gratuity List
	private List<PosGratuity> m_oPosGratuityList;
	
	// First new check of outlet (after switch outlet / login)
	private boolean m_bFirstNewCheck;
	
	// Last error message
	private String m_sErrorMessage;
	
	private String m_sResultMessage;
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage;
	}
	
	public String getLastResultMessage() {
		return m_sResultMessage;
	}
	
	public FuncOutlet() {
		m_oOutlet = new OutOutlet();
		m_oShop = new OutShop();
		m_oOutletSetting = new PosOutletSetting();
		m_oBusinessDay = new PosBusinessDay();
		m_oBusinessPeriods = new LinkedHashMap<String, PosBusinessPeriod>();
		m_oSpecialPeriodList = new OutSpecialHourList();
		m_oFloorPlanFunc = new OutFloorPlanFunc();
		m_oItemPrintQueueList = new PosItemPrintQueueList();
		
		m_bOverrideCheckRound = false;
		m_sOverrideCheckRoundMethod = "";
		m_iOverrideCheckRoundDecimal = 0;
		
		m_iManualPriceLevel = -1;
		
		m_oListTablesInfo = new ArrayList<HashMap<String,String>>();
		m_oPosGratuityList = new ArrayList<PosGratuity>();
		m_bFirstNewCheck = false;
		
		m_sResultMessage = "";
		
		m_iQuestionnaireCount = 0;
	}
	
	// Load outlet
	// Return value:	0 - No error
	//					1 - Loading error
	//					2 - Not yet daily start
	public int loadOutlet(int iOutletId, boolean bLoadOutletOnly) {
		
		m_bOverrideCheckRound = false;
		m_sOverrideCheckRoundMethod = "";
		m_iOverrideCheckRoundDecimal = 0;
		m_sErrorMessage = "";

		// Read outlet from OM
		if(!m_oOutlet.readById(iOutletId)){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("missing_outlet");
			return 1;
		}
		
		if (m_oOutlet.getOletId() == 0){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("missing_outlet");
			return 1;
		}
		
		if(bLoadOutletOnly)
			return 0;
		
		// Create thread to load detail
		AppThreadManager oAppThreadManager = new AppThreadManager();
		
		Object[] oParameters = new Object[2];
		oParameters[0] = m_oOutlet.getOutletShopId();
		oParameters[1] = m_oOutlet.getOletId();
		oAppThreadManager.addThread(1, this, "loadOutletAndPosBasicSetup", oParameters);
		
		// Run all of the threads
		oAppThreadManager.runThread();

		// Wait for the thread to finish
		oAppThreadManager.waitForThread();
		
		ArrayList<?> oReturnValues = (ArrayList<?>) oAppThreadManager.getResult(1);
		if (oReturnValues.contains(1)) {
			// Error
			m_sErrorMessage = AppGlobal.g_oLang.get()._("daily_start_has_not_been_carried_out");
			return 2;
		} else {
			loadBusinessPeriod();
			// Load Gratuity List with Access Control after finishing loading business setting
			loadGratuityListWithAccessControl();
		}
		
		// fail to load floor plan
		if (oReturnValues.contains(2)) {
			// Error
			m_sErrorMessage = AppGlobal.g_oLang.get()._("missing_floor_plan");
			return 1;
		}
		
		// fail to load item print list
		if (oReturnValues.contains(3)) {
			// Error
			m_sErrorMessage = AppGlobal.g_oLang.get()._("no_item_print_queue");
			return 1;
		}
		
		return 0;
	}
	
	public OutOutlet getOutlet() {
		return m_oOutlet;
	}
	
	public OutShop getShop() {
		return m_oShop;
	}
	
	public String getShopCode() {
		return m_oShop.getShopCode();
	}
	
	public int getTimeZone() {
		return m_oShop.getTimezone();
	}
	
	public String getTimeZoneName() {
		return m_oShop.getTimezoneName();
	}
	
	public int getOutletId(){
		return m_oOutlet.getOletId();
	}
	
	public String getOutletCode(){
		return m_oOutlet.getCode();
	}
	
	public String getOutletNameByIndex(int iIndex) {
		return m_oOutlet.getName(iIndex);
	}
	
	public String[] getOutletName() {
		return m_oOutlet.getName();
	}
	
	public int getShopId(){
		return m_oOutlet.getOutletShopId();
	}
	
	public String getCurrencySign() {
		return m_oOutlet.getCurrencySign();
	}
	
	public String getCurrencyCode() {
		return m_oOutlet.getCurrencyCode();
	}
	
	public List<Integer> getOutletGroupList() {
		return m_oOutlet.getOutletGroupList();
	}
	
	// load shop, outlet floor plan func, table section / business day, outlet setting, item print queues
	/* oReturnValue: 
	 * 	1 - load business day failed
	 *  2 - load floor plan func failed
	 *  3 - load item print queue failed
	 */
	public ArrayList<?> loadOutletAndPosBasicSetup(int iShopId, int iOutletId) {
		ArrayList<Integer> oReturnValue = new ArrayList<Integer>();
		OutletGeneral oOutletGeneral = new OutletGeneral();
		oOutletGeneral.readShopFloorPlanOutletSection(iShopId, iOutletId);
		
		// Shop
		if(oOutletGeneral.getOutShop() != null)
			m_oShop = oOutletGeneral.getOutShop();
		
		// Floor Plan
		if(oOutletGeneral.getOutFloorPlanFunc() != null) {
			m_oFloorPlanFunc = oOutletGeneral.getOutFloorPlanFunc();
			if (m_oFloorPlanFunc.getFloorPlanCount() == 0)
				oReturnValue.add(2);
			
			// Build table name list
			buildTableNameList();
			
			if(oOutletGeneral.getOutTableSectionList() != null)
				m_oOutTableSectionList = oOutletGeneral.getOutTableSectionList();
		} else
			oReturnValue.add(2);
		
		PosGeneral oPosGeneral = new PosGeneral();
		oPosGeneral.readBusinessDayOutletSettingItemPrintQueue(iShopId, iOutletId);
		
		if (oPosGeneral.getPosBusinessDay() != null) {
			m_oBusinessDay = oPosGeneral.getPosBusinessDay();
			
			if (m_oBusinessDay.getBdayId().isEmpty())
				oReturnValue.add(1);
		} else
			oReturnValue.add(1);
		
		if (oPosGeneral.getPosOutletSetting() != null) {
			m_oOutletSetting = oPosGeneral.getPosOutletSetting();
			
			if (m_oOutletSetting.isGenerateCheckNumByStation())
				m_bFirstNewCheck = true;
		}
		
		if (oPosGeneral.getPosItemPrintQueueList() != null)
			m_oItemPrintQueueList = oPosGeneral.getPosItemPrintQueueList();
		else
			oReturnValue.add(3);
		
		return oReturnValue;
	}
	
	// Load business day
	public boolean loadBusinessDay(int iOutletId){
		m_sErrorMessage = "";
		
		// Read business day
		if(!m_oBusinessDay.readByOutletId(iOutletId)) {
			return false;
		}
		
		if(m_oBusinessDay.getBdayId().compareTo("") == 0) {
			return false;
		}
		
		return true;
	}

	// check business day is started more than one time in one day
	public boolean checkBusinessDay(Date date, int iOutletId){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		
		m_sErrorMessage = "";
		
		// Read business day
		if(!m_oBusinessDay.readByDateOutletId(dateFormat.format(date), iOutletId)) {
			return false;
		}
		
		if(m_oBusinessDay.getBdayId().compareTo("") == 0) {
			return false;
		}
		
		return true;
	}
	
	public PosBusinessDay getBusinessDay(){
		return m_oBusinessDay;
	}
	
	// Format: yyyy-MM-dd
	public String getFormat1BusinessDayInString(){
		return m_oBusinessDay.getDateInString();
	}
	
	public PosBusinessPeriod getBusinessPeriod() {
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm:ss");
		String sCurrentTime = fmt.print(today);
		
		for(Map.Entry<String, PosBusinessPeriod> entry:m_oBusinessPeriods.entrySet()){
			PosBusinessPeriod oPosBusinessPeriod = entry.getValue();

			String sStartTime = oPosBusinessPeriod.getOfficialStartTime().toString();
			String sEndTime = oPosBusinessPeriod.getOfficialEndTime().toString();
			
			if (sStartTime.compareTo(sEndTime) <= 0) {
				if (sCurrentTime.compareTo(sStartTime) >= 0 && 
					sCurrentTime.compareTo(sEndTime) <= 0) {
					return oPosBusinessPeriod;
				}
			}
			else {
				if (sCurrentTime.compareTo(sStartTime) >= 0 ||
					sCurrentTime.compareTo(sEndTime) <= 0) {
					return oPosBusinessPeriod;
				}
			}
		}
		
		// No period found
		return (new PosBusinessPeriod());
	}
	
	// get business period by id
	public PosBusinessPeriod getBusinessPeriodById(String sBperId) {
		return m_oBusinessPeriods.get(sBperId);
	}
	
	// get outlet business period
	public LinkedHashMap<String,PosBusinessPeriod> getOuletBusinessPeriod() {
		return m_oBusinessPeriods;
	}
	
	// get business period by code
	public String getBusinessPeriodIdByCode(String sCode) {
		String sPeriodId = "";
		for(Map.Entry<String, PosBusinessPeriod> entry:m_oBusinessPeriods.entrySet()) {
			if(entry.getValue().getCode().equals(sCode)) {
				sPeriodId = entry.getValue().getBperId();
				break;
			}
		}
		return sPeriodId;
	}
	
	// get formated business day
	public String[] getFormatBusinessDay() {
		String[] sNameArray = StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, "");
		
		for (int i=0; i<AppGlobal.LANGUAGE_COUNT; i++) {
			String langCode = "en";
			int langIndex = 1;
			for(HashMap<String, String> oLangInfo:AppGlobal.g_oSupportedLangList) {
				langIndex = Integer.parseInt(oLangInfo.get("index"));
				if(langIndex == (i+1)) {
					langCode = oLangInfo.get("code");
					break;
				}
			}
			
			DateTimeFormatter dateFormat;
			if(langCode.equals("zh-hk"))
				dateFormat = DateTimeFormat.forPattern("EEE , yyyy/MM/dd").withLocale(Locale.CHINESE);
			else
			if(langCode.equals("zh-cn"))
				dateFormat = DateTimeFormat.forPattern("EEE , yyyy/MM/dd").withLocale(Locale.CHINA);
			else
			if(langCode.equals("jpn"))
				dateFormat = DateTimeFormat.forPattern("EEE , yyyy/MM/dd").withLocale(Locale.JAPAN);
			else
			if(langCode.equals("kor"))
				dateFormat = DateTimeFormat.forPattern("EEE , yyyy/MM/dd").withLocale(Locale.KOREA);
			else
				dateFormat = DateTimeFormat.forPattern("EEE , yyyy/MM/dd").withLocale(Locale.ENGLISH);
			sNameArray[i] = dateFormat.print(m_oBusinessDay.getDate());
		}
		
		return sNameArray;
	}
	
	// Load business day period
	public boolean loadBusinessPeriod() {
		String sBusinessDayId = m_oBusinessDay.getBdayId();
		int iOutletId = m_oOutlet.getOletId();
		
		m_sErrorMessage = "";
		
		PosBusinessPeriod oBusinessPeriod = new PosBusinessPeriod();
		JSONArray oBusinessPeriodJSONArray = oBusinessPeriod.getBusinessPeriodListByBdayIdOutletId(sBusinessDayId, iOutletId);
		if(oBusinessPeriodJSONArray == null)
			return false;
		for (int i = 0; i < oBusinessPeriodJSONArray.length(); i++) {
			JSONObject oBusinessPeriodJSONObject = oBusinessPeriodJSONArray.optJSONObject(i);
			if (oBusinessPeriodJSONObject == null)
				continue;
			
			PosBusinessPeriod tempBusinessPeriod = new PosBusinessPeriod(oBusinessPeriodJSONObject);
			m_oBusinessPeriods.put(tempBusinessPeriod.getBperId(), tempBusinessPeriod);
		}
		
		// load special period
		JSONObject oCheckingCriteria  = new JSONObject();
		try {
			oCheckingCriteria.put("businessDay", m_oBusinessDay.getDateInString());
			oCheckingCriteria.put("specialDay", m_oBusinessDay.getSpecialDayCaldId());
			oCheckingCriteria.put("beforeSpecialDay", m_oBusinessDay.getDayBeforeSpecialDayCaldId());
			oCheckingCriteria.put("holiday", m_oBusinessDay.getHolidayCaldId());
			oCheckingCriteria.put("beforeHoliday", m_oBusinessDay.getDayBeforeHolidayCaldId());
			oCheckingCriteria.put("weekday", m_oBusinessDay.getDayOfWeek());
		}catch(JSONException jsone) {
			AppGlobal.stack2Log(jsone);
		}
		if(!m_oSpecialPeriodList.readByOutletId(m_oOutlet.getOletId(), oCheckingCriteria))
			return false;
		
		return true;
	}
	
	// Update business period
	// type:	1 - Daily start
	//			2 - Daily close
	public void updateBusinessPeriod(String sBusinessDayId, int outletId, int type) {
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		int isDailyStart = 0;
		
		PosBusinessPeriod oBusinessPeriod = new PosBusinessPeriod();
		JSONArray tempBusinessPeriodJSONArray = oBusinessPeriod.getBusinessPeriodListByBdayIdOutletId(sBusinessDayId, outletId);
		if(tempBusinessPeriodJSONArray != null) {
			for (int i = 0; i < tempBusinessPeriodJSONArray.length(); i++) {
				JSONObject tempJSONObject = tempBusinessPeriodJSONArray.optJSONObject(i);
				if (tempJSONObject == null)
					continue;
				
				PosBusinessPeriod tempBusinessPeriod = new PosBusinessPeriod(tempJSONObject);
				if (type == 1) {
					tempBusinessPeriod.setStartUserId(AppGlobal.g_oFuncUser.get().getUserId());
					tempBusinessPeriod.setEndUserId(0);
					
					tempBusinessPeriod.setLastRecallTime(fmt.print(AppGlobal.convertTimeToUTC(today)));
					tempBusinessPeriod.setLastRecallLocTime(today);
					tempBusinessPeriod.setRecallUserId(AppGlobal.g_oFuncUser.get().getUserId());
					tempBusinessPeriod.setRecallCount(tempBusinessPeriod.getRecallCount()+1);
					tempBusinessPeriod.setStatus(PosBusinessPeriod.STATUS_PERIOD_RUNNING);
					
					isDailyStart = 1;
				} else
				if (type == 2) {
					//close the running business period
					tempBusinessPeriod.setEndUserId(AppGlobal.g_oFuncUser.get().getUserId());
					tempBusinessPeriod.setStatus(PosBusinessPeriod.STATUS_PERIOD_STOPPED);
					
					isDailyStart = 0;
				}
				
				tempBusinessPeriod.addUpdate(true, isDailyStart);
			}
		}
	}
	
	// Load Floor Plan
	public boolean loadFloorPlan(int iOutletId){
		
		m_sErrorMessage = "";
		
		// Read Floor Plan
		if(!m_oFloorPlanFunc.readByOutletId(iOutletId)) {
			return false;
		}
		
		if(m_oFloorPlanFunc.getFloorPlanCount() == 0) {
			return false;
		}
		
		// Build table name list
		buildTableNameList();
		
		m_oOutTableSectionList = new OutTableSectionList();
		m_oOutTableSectionList.getAllTableSections(this.getOutletId());
		
		return true;
	}
	
	// Load Item Print Queue
	public boolean loadItemPrintQueue(int iOutletId, int iShopId){
		m_sErrorMessage = "";
		
		// Read Item Print Queue
		if(!m_oItemPrintQueueList.readItemQueueListByShopAndOutletId(iShopId, iOutletId)) {
			return false;
		}
		
		return true;
	}
	
	// Load Gratuity List
	public boolean loadGratuityListWithAccessControl() {
		m_sErrorMessage = "";
		PosGratuityList oPosGratuityList = new PosGratuityList();
		
		//Read Gratuity List
		PosBusinessDay oBusinessDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay();
		DateTimeFormatter dateFormatForGratuity = DateTimeFormat.forPattern("yyyy-MM-dd");
		oPosGratuityList.readAllWithAccessControl(AppGlobal.g_oFuncOutlet.get().getOutletId(), dateFormatForGratuity.print(oBusinessDay.getDate()), oBusinessDay.isHoliday(), oBusinessDay.isDayBeforeHoliday(), oBusinessDay.isSpecialDay(), oBusinessDay.isDayBeforeSpecialDay(), oBusinessDay.getDayOfWeek());
		
		m_oPosGratuityList = oPosGratuityList.getGratuityList();
		
		return true;
	}
	
	// Get settlement count for printing questionnaire
	public int getSettlementCount() {
		return m_iQuestionnaireCount;
	}
	
	// Set settlement count for printing questionnaire
	public void setSettlementCount(int iCount) {
		m_iQuestionnaireCount = iCount;
	}
	
	// Load Outlet Setting
	public boolean loadOutletSetting(int iOutletId) {
		m_sErrorMessage = "";
		
		//Read Outlet Setting
		if(!m_oOutletSetting.readByOutletId(iOutletId)) {
			return false;
		}else if(m_oOutletSetting.isGenerateCheckNumByStation())
			m_bFirstNewCheck = true;
		
		return true;
	}
	
	// Load Outlet Shop
	public boolean loadShop(int iShopId) {
		if(m_oShop.readById(iShopId) == false) {
			return false;
		}
		return true;
	}
	
	public int getFloorPlanCount(){
		return m_oFloorPlanFunc.getFloorPlanCount();
	}
	
	public OutFloorPlan getFloorPlan(int iFloorPlanId){
		return m_oFloorPlanFunc.getFloorPlan(iFloorPlanId);
	}
	
	public HashMap<Integer, OutFloorPlan> getFloorPlanList(){
		return m_oFloorPlanFunc.getFloorPlanList();
	}
	
	public OutTableSectionList getOutTableSectionList() {
		return m_oOutTableSectionList;
	}
	
	public List<PosGratuity> getPosGratuityList() {
		return m_oPosGratuityList;
	}
	
	// Check if there is running business day or business day can be recalled or not
	// Return value 			=	0	# There is running business day
	//								1	# No running business day and not allow recall
	//								2	# No running business day and MUST recall (same business day)
	//								3	# No running business day and within the yesterday last period, allow user to select if recall or not
	public int dailyStartBusinessDayPreChecking(){
		return m_oBusinessDay.dailyStartBusinessDayPreChecking(m_oOutlet.getOletId());
	}
	
	public boolean dailyStart(int iOutletId, boolean bRecallLastBusinessDay) {
		m_sErrorMessage = "";
		m_sResultMessage = "";
		
		// Read outlet from OM
		if(!m_oOutlet.readById(iOutletId)){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_get_record");
			return false;
		}
		
		if (m_oOutlet.getOletId() == 0){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("no_such_record");
			return false;
		}
		
		// Kill other stations
		AppGlobal.startOutletOperationKillOtherStations(iOutletId, AppGlobal.g_oLang.get()._("daily_start_process_begins"));

		PosBusinessDay oBusinessDay = new PosBusinessDay();
		int iResult = oBusinessDay.dailyStart(m_oOutlet.getOletId(), AppGlobal.g_oFuncUser.get().getUserId(), bRecallLastBusinessDay);
		if (iResult == -1) {
			m_sErrorMessage = oBusinessDay.getLastErrorMessage();
			if(oBusinessDay.getLastErrorMessage().isEmpty())
				m_sErrorMessage = AppGlobal.g_oLang.get()._("daily_start_is_unsuccessful");
			
			return false;
		} else if (iResult > 0) {
			m_sResultMessage = iResult + " " + AppGlobal.g_oLang.get()._("checks_carry_forward_to_current_business_date");
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "",
					AppGlobal.g_oFuncUser.get().getUserId() + "", iResult+" check numbers carry forward to current business date");
		}
		
		// Finish the operation
		AppGlobal.endOutletOperation(iOutletId);
		
		return true;
	}
	
	public boolean dailyClose(int iOutletId) {
		
		m_sErrorMessage = "";
		
		// Read outlet from OM
		if(!m_oOutlet.readById(iOutletId)){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_get_record");
			return false;
		}
		
		if (m_oOutlet.getOletId() == 0){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("no_such_record");
			return false;
		}

		// Check whether daily start already operated
		if(!loadBusinessDay(m_oOutlet.getOletId())){
			// Error
			m_sErrorMessage = AppGlobal.g_oLang.get()._("daily_start_has_not_been_carried_out");
			return false;
		}
		
		// Check whether there is any unpaid check
		JSONObject oForceDailyCloseJSONObject = AppGlobal.g_oFuncStation.get().getForceDailyClose();
		String sCarryForward = "";	// empty - no need to carry forward; c - carry forward
		if (oForceDailyCloseJSONObject != null)
			sCarryForward = oForceDailyCloseJSONObject.optString("carryForward", "");
		
		if (sCarryForward.equals("")) {
			PosCheck check = new PosCheck(AppGlobal.g_oFuncUser.get().getUserId());
			JSONArray oCheckList = check.getCheckListByBusinessDayPaid(m_oBusinessDay.getBdayId(), 0, PosCheck.PAID_NOT_PAID, false);
			if (oCheckList != null && oCheckList.length() > 0) {
				m_sErrorMessage = AppGlobal.g_oLang.get()._("some_checks_have_not_paid");
				return false;
			}
		}
		
		// Stop the thread for checking update of table status
		AppGlobal.removeBackgroundScheduleTask(AppBackgroundScheduleJob.TYPE_UPDATE_TABLE_STATUS+"_"+m_oOutlet.getOletId());
		
		// Stop the thread for checking update of soldout status
		AppGlobal.removeBackgroundScheduleTask(AppBackgroundScheduleJob.TYPE_UPDATE_SOLDOUT_STATUS+"_"+m_oOutlet.getOletId());
		
		// Stop the thread for checking update of item stock quantity status
		AppGlobal.removeBackgroundScheduleTask(AppBackgroundScheduleJob.TYPE_UPDATE_ITEM_STOCK_QTY_STATUS+"_"+m_oOutlet.getOletId());
		
		// Kill other stations
		AppGlobal.startOutletOperationKillOtherStations(iOutletId, AppGlobal.g_oLang.get()._("daily_close_process_begins"));
		

		PosBusinessDay oBusinessDay = new PosBusinessDay();
		JSONObject resultJSONObject = oBusinessDay.dailyClose(m_oOutlet.getOletId(), AppGlobal.g_oFuncStation.get().getStationId(), AppGlobal.g_oFuncUser.get().getUserId());
		if(resultJSONObject == null) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("daily_close_is_unsuccessful");
			return false;
		}
		
		// Housekeeping of parking order
		FuncParkOrder oFuncParkOrder = new FuncParkOrder();
		oFuncParkOrder.cleanParkOrderFolder();
		
		// Finish the operation
		AppGlobal.endOutletOperation(iOutletId);
		
		return true;
	}
	
	// Auto-balance stock transaction and update outlet item qty
	public void autoBalanceOutletItemStock(){
		ArrayList<Integer> oMenuItemIds = new ArrayList<Integer>();
		
		// Get the item that with stock
		// Load current stock
		FuncCheck oFuncCheck = new FuncCheck();
		oFuncCheck.getCurrentInSellItemStockList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosOutletItem.SOLDOUT_NO);
		for(PosOutletItem oOutletItem: oFuncCheck.getItemStockList()) {
			oMenuItemIds.add(oOutletItem.getItemId());
		}
		
		if(!oMenuItemIds.isEmpty()){
			// Auto-balance item stock transaction
			PosStockTransaction oPosStockTransaction = new PosStockTransaction();
			oPosStockTransaction.autoBalanceOutletItemStock(m_oBusinessDay.getBdayId(), m_oOutlet.getShopId(), m_oOutlet.getOletId(), oMenuItemIds, AppGlobal.g_oFuncUser.get().getUserId(), 0);
		}
	}
	
	public boolean reloadBusinessSetting(int iOutletId) {
		m_sErrorMessage = "";
		
		// Read outlet from OM
		if(!m_oOutlet.readById(iOutletId)){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_get_record");
			return false;
		}
		
		if (m_oOutlet.getOletId() == 0){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("no_such_record");
			return false;
		}

		// Check whether daily start already operated
		if(!loadBusinessDay(m_oOutlet.getOletId())){
			// Error
			m_sErrorMessage = AppGlobal.g_oLang.get()._("daily_start_has_not_been_carried_out");
			return false;
		}
		
		// Kill other stations
		AppGlobal.startOutletOperationKillOtherStations(iOutletId, AppGlobal.g_oLang.get()._("reload_business_setting_process_begins"));
		
		// wait until kill all target station
		String sCannotKillStationUUID = AppGlobal.waitStationBeKilled(iOutletId, "", AppGlobal.g_oFuncStation.get().getStationId());
		if(!sCannotKillStationUUID.isEmpty()){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("station") + "(" + sCannotKillStationUUID + ") " + AppGlobal.g_oLang.get()._("cannot be killed");
			AppGlobal.writeErrorLog(AppGlobal.class.getSimpleName(),
					new Exception().getStackTrace()[0].getMethodName(),
					AppGlobal.g_oFuncStation.get().getStationId() + "",
					Integer.toString(AppGlobal.g_oFuncUser.get().getUserId()), "Fail to kill station " + sCannotKillStationUUID);
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "",
					"Station(" + sCannotKillStationUUID + ") cannot be killed in reload business setting");
		}

		PosBusinessDay oBusinessDay = new PosBusinessDay();
		if(!oBusinessDay.reloadBusinessSetting(m_oBusinessDay.getBdayId(), AppGlobal.g_oFuncUser.get().getUserId())) {
			m_sErrorMessage = oBusinessDay.getLastErrorMessage();
			if(oBusinessDay.getLastErrorMessage().isEmpty())
				m_sErrorMessage = AppGlobal.g_oLang.get()._("reload_business_setting_is_unsuccessful");
			
			return false;
		}
		
		// Finish the operation
		AppGlobal.endOutletOperation(iOutletId);
		
		return true;
	}
	
	public void changeDate (int year, int month, int day) {
		DateTime date = new DateTime(year, month, day, 0, 0);
		m_oBusinessDay.setDate(date);
	}
	
	// check whether today is after business day/business hour
	public boolean checkCrossDay() {
		if(m_oBusinessPeriods.isEmpty())
			return false;
		
		SimpleDateFormat oDateFormat= new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		SimpleDateFormat oTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		
		DateTime oBusinessDay = m_oBusinessDay.getDate();

		PosBusinessPeriod[] oPeriodArray = m_oBusinessPeriods.values().toArray(new PosBusinessPeriod[0]);
		PosBusinessPeriod oEndPeriod = oPeriodArray[oPeriodArray.length-1];
		Time oLastPeriodStartTime = oEndPeriod.getOfficialStartTime();
		Time oLastPeriodEndTime = oEndPeriod.getOfficialEndTime();
		
		boolean bCrossDatePeriod = oLastPeriodStartTime.after(oLastPeriodEndTime);
		
		// Get the end period time of the current business date
		DateTime oCurrentBusinessDateEndTime;
		String sCurrentBusinessDateEndTime;
		if(bCrossDatePeriod){
			// Cross date period
			sCurrentBusinessDateEndTime = oDateFormat.format(oBusinessDay.plusDays(1).toDate()) + "T" + oTimeFormat.format(oLastPeriodEndTime);
		}else{
			// Not Cross date period
			sCurrentBusinessDateEndTime = oDateFormat.format(oBusinessDay.toDate()) + "T" + oTimeFormat.format(oLastPeriodEndTime);
		}
		oCurrentBusinessDateEndTime = new DateTime(sCurrentBusinessDateEndTime);
		
		return !oCurrentBusinessDateEndTime.isAfterNow();
	}
	
	public int getPrintQueueByMenuItpqId(int iMenuItpqId) {
		int iPrtqId = 0;
		if(m_oItemPrintQueueList.getItemPrintQueueByIndex(iMenuItpqId) != null)
			iPrtqId = m_oItemPrintQueueList.getItemPrintQueueByIndex(iMenuItpqId).getPrtqId();
		
		return iPrtqId;
	}
	
	public int getPriceLevel() {
		if(m_iManualPriceLevel >= 0)
			return m_iManualPriceLevel;
		else
			return this.m_oBusinessDay.getPriceLevel();
	}
	
	public void setManualPriceLevel(int iManualPriceLevel) {
		m_iManualPriceLevel = iManualPriceLevel;
	}
	
	public boolean isPriceLevelChangeManually() {
		return (m_iManualPriceLevel >= 0);
	}
	
	public String[] getCurrentBusinessPeriodName() {
		PosBusinessPeriod oPosBusinessPeriod = this.getBusinessPeriod();
		return oPosBusinessPeriod.getName();
	}
	
	public OutSpecialHour getCurrentSpecialPeriod() {
		OutSpecialHour oCurrentSpecialHour = null;
		Calendar oCurrentTime = Calendar.getInstance();
		Calendar oStartTime = Calendar.getInstance(), oEndTime = Calendar.getInstance();
		
		for(OutSpecialHour oSpecialHour:this.m_oSpecialPeriodList.getSpecialHourList()){
			boolean bStartTimeResult = true, bEndTimeResult = true;
			if(oSpecialHour.getOfficalStartTime() != null) {
				oStartTime.setTime(oSpecialHour.getOfficalStartTime());
				oStartTime.set(Calendar.DAY_OF_MONTH, oCurrentTime.get(Calendar.DAY_OF_MONTH));
				oStartTime.set(Calendar.MONTH, oCurrentTime.get(Calendar.MONTH));
				oStartTime.set(Calendar.YEAR, oCurrentTime.get(Calendar.YEAR));
				
				if(oCurrentTime.compareTo(oStartTime) < 0)
					bStartTimeResult = false;
			}
			
			if(oSpecialHour.getOfficalEndTime() != null) {
				oEndTime.setTime(oSpecialHour.getOfficalEndTime());
				oEndTime.set(Calendar.DAY_OF_MONTH, oCurrentTime.get(Calendar.DAY_OF_MONTH));
				oEndTime.set(Calendar.MONTH, oCurrentTime.get(Calendar.MONTH));
				oEndTime.set(Calendar.YEAR, oCurrentTime.get(Calendar.YEAR));
				
				if(oCurrentTime.compareTo(oEndTime) > 0)
					bEndTimeResult = false;
			}
			
			if(bStartTimeResult && bEndTimeResult) {
				oCurrentSpecialHour = oSpecialHour;
				break;
			}
		}
		
		return oCurrentSpecialHour;
	}
	
	public String getMediaUrl(String sType){
		return m_oOutlet.getMediaUrl(sType);
	}
	
	public String getMediaFilename(String sType){
		return m_oOutlet.getMediaFilename(sType);
	}
	
	public int getSoldoutMenuId() {
		return m_oOutletSetting.getSoldoutMenuId();
	}
	
	public int getPaidInOutPaymId() {
		return m_oOutletSetting.getPaidInOutPaymId();
	}
	
	public int getCheckPfmtId(int iIndex) {
		return m_oOutletSetting.getCheckPfmtId(iIndex);
	}
	
	public int getDetailCheckPfmtId(int iIndex) {
		return m_oOutletSetting.getDetailCheckPfmtId(iIndex);
	}
	
	public int getReceiptPfmtId(int iIndex) {
		return m_oOutletSetting.getReceiptPfmtId(iIndex);
	}
	
	public int getServingCheckPfmtId() {
		return m_oOutletSetting.getServingCheckPfmtId();
	}
	

	public String getPrintFormatNameByPfmtId(int iPfmtId, int iIndex) {
		return m_oOutletSetting.getPrintFormatName(iPfmtId, iIndex);
	}
	
	public int getBilingualLangIndexByeLangIndex(int iIndex) {
		String langCode = "en";
		int langIndex = 1;
		String sBilingualLangCode = m_oOutletSetting.getBilingualLangCode(iIndex);
		
		if (sBilingualLangCode.isEmpty())
			return 0;

		for(HashMap<String, String> oLangInfo:AppGlobal.g_oSupportedLangList) {
			langCode = oLangInfo.get("code");
			if (sBilingualLangCode.equals(langCode)) {
				langIndex = Integer.parseInt(oLangInfo.get("index"));
				break;
			}
		}
		
		return langIndex;
	}
	
	public int getOutletStartCheckNumber() {
		return this.m_oBusinessDay.getOutletStartCheckNumber();
	}
	
	public int getOutletFailoverStationGroupId(){
		return this.m_oOutletSetting.getFailoverStgpId();
	}
	
	// Rounding method
	public BigDecimal roundItemAmountToBigDecimal(BigDecimal dAmount){
		String sRoundMethod = this.getBusinessDay().getItemRound();
		int iRoundDecimal = this.getBusinessDay().getItemDecimal();
		
		//if amount is negative, change to positive to calculate
		//change result back to negative
		if(dAmount.signum() == -1)
			return Util.HERORound(dAmount.negate(), sRoundMethod, iRoundDecimal).negate();
		else
			return Util.HERORound(dAmount, sRoundMethod, iRoundDecimal);
	}
	
	public String roundItemAmountToString(BigDecimal dAmount){
		int iRoundDecimal = this.getBusinessDay().getItemDecimal();
		return StringLib.BigDecimalToString(roundItemAmountToBigDecimal(dAmount), iRoundDecimal);
	}
	
	public BigDecimal roundSCAmountToBigDecimal(BigDecimal dAmount){
		String sRoundMethod = this.getBusinessDay().getScRound();
		int iRoundDecimal = this.getBusinessDay().getScDecimal();
		
		//if amount is negative, change to positive to calculate
		//change result back to negative
		if(dAmount.signum() == -1)
			return Util.HERORound(dAmount.negate(), sRoundMethod, iRoundDecimal).negate();
		else
			return Util.HERORound(dAmount, sRoundMethod, iRoundDecimal);
	}
	
	public String roundSCAmountToString(BigDecimal dAmount){
		int iRoundDecimal = this.getBusinessDay().getScDecimal();
		return StringLib.BigDecimalToString(roundSCAmountToBigDecimal(dAmount), iRoundDecimal);
	}
	
	public BigDecimal roundTaxAmountToBigDecimal(BigDecimal dAmount){
		String sRoundMethod = this.getBusinessDay().getTaxRound();
		int iRoundDecimal = this.getBusinessDay().getTaxDecimal();
		
		//if amount is negative, change to positive to calculate
		//change result back to negative
		if(dAmount.signum() == -1)
			return Util.HERORound(dAmount.negate(), sRoundMethod, iRoundDecimal).negate();
		else
			return Util.HERORound(dAmount, sRoundMethod, iRoundDecimal);
	}
	
	public String roundTaxAmountToString(BigDecimal dAmount){
		int iRoundDecimal = this.getBusinessDay().getTaxDecimal();
		return StringLib.BigDecimalToString(roundTaxAmountToBigDecimal(dAmount), iRoundDecimal);
	}
	
	public BigDecimal roundDiscAmountToBigDecimal(BigDecimal dAmount){
		String sRoundMethod = this.getBusinessDay().getDiscRound();
		int iRoundDecimal = this.getBusinessDay().getDiscDecimal();
		
		//if amount is negative, change to positive to calculate
		//change result back to negative
		if(dAmount.signum() == -1)
			return Util.HERORound(dAmount.negate(), sRoundMethod, iRoundDecimal).negate();
		else
			return Util.HERORound(dAmount, sRoundMethod, iRoundDecimal);
	}
	
	public String roundDiscAmountToString(BigDecimal dAmount){
		int iRoundDecimal = this.getBusinessDay().getDiscDecimal();
		return StringLib.BigDecimalToString(roundDiscAmountToBigDecimal(dAmount), iRoundDecimal);
	}
	
	public BigDecimal roundCheckAmountToBigDecimal(BigDecimal dAmount){
		String sRoundMethod;
		int iRoundDecimal;
		
		if(m_bOverrideCheckRound){
			sRoundMethod = m_sOverrideCheckRoundMethod;
			iRoundDecimal = m_iOverrideCheckRoundDecimal;
		}else{
			sRoundMethod = this.getBusinessDay().getCheckRound();
			iRoundDecimal = this.getBusinessDay().getCheckDecimal();
		}
		
		//if amount is negative, change to positive to calculate
		//change result back to negative
		if(dAmount.signum() == -1)
			return Util.HERORound(dAmount.negate(), sRoundMethod, iRoundDecimal).negate();
		else
			return Util.HERORound(dAmount, sRoundMethod, iRoundDecimal);
	}
	
	public String roundCheckAmountToString(BigDecimal dAmount){
		int iRoundDecimal;
		
		if(m_bOverrideCheckRound)
			iRoundDecimal = m_iOverrideCheckRoundDecimal;
		else
			iRoundDecimal = this.getBusinessDay().getCheckDecimal();
		return StringLib.BigDecimalToString(roundCheckAmountToBigDecimal(dAmount), iRoundDecimal);
	}
	
	public BigDecimal roundPaymentAmountToBigDecimal(BigDecimal dAmount){
		String sRoundMethod = this.getBusinessDay().getPayRound();
		int iRoundDecimal = this.getBusinessDay().getPayDecimal();
		
		//if amount is negative, change to positive to calculate
		//change result back to negative
		if(dAmount.signum() == -1)
			return Util.HERORound(dAmount.negate(), sRoundMethod, iRoundDecimal).negate();
		else
			return Util.HERORound(dAmount, sRoundMethod, iRoundDecimal);
	}
	
	public String roundPaymentAmountToString(BigDecimal dAmount){
		int iRoundDecimal = this.getBusinessDay().getPayDecimal();
		return StringLib.BigDecimalToString(roundPaymentAmountToBigDecimal(dAmount), iRoundDecimal);
	}
	
	public BigDecimal roundGratuityAmountToBigDecimal(BigDecimal dAmount){
		String sRoundMethod = this.getBusinessDay().getGratuityRound();
		int iRoundDecimal = this.getBusinessDay().getGratuityDecimal();
		
		//if amount is negative, change to positive to calculate
		//change result back to negative
		if(dAmount.signum() == -1)
			return Util.HERORound(dAmount.negate(), sRoundMethod, iRoundDecimal).negate();
		else
			return Util.HERORound(dAmount, sRoundMethod, iRoundDecimal);
	}
	
	public String roundGratuityAmountToString(BigDecimal dAmount){
		int iRoundDecimal = this.getBusinessDay().getGratuityDecimal();
		return StringLib.BigDecimalToString(roundGratuityAmountToBigDecimal(dAmount), iRoundDecimal);
	}
	
	public String getItemRoundMethod(){
		return this.getBusinessDay().getItemRound();
	}
	
	public int getItemRoundDecimal() {
		return this.getBusinessDay().getItemDecimal();
	}
	
	public String getDiscRoundMethod(){
		return this.getBusinessDay().getDiscRound();
	}
	
	public int getDiscRoundDecimal() {
		return this.getBusinessDay().getDiscDecimal();
	}
	
	public String getCheckRoundMethod(){
		if(m_bOverrideCheckRound)
			return m_sOverrideCheckRoundMethod;
		else
			return this.getBusinessDay().getCheckRound();
	}
	
	public int getCheckRoundDecimal(){
		if(m_bOverrideCheckRound)
			return m_iOverrideCheckRoundDecimal;
		else
			return this.getBusinessDay().getCheckDecimal();
	}
	
	public String getPayRoundMethod(){
		return this.getBusinessDay().getPayRound();
	}
	
	public int getPayRoundDecimal(){
		return this.getBusinessDay().getPayDecimal();
	}
	
	public String getServiceChargeRoundMethod(){
		return this.getBusinessDay().getScRound();
	}
	
	public int getServiceChargeRoundDecimal() {
		return this.getBusinessDay().getScDecimal();
	}
	
	public String getTaxRoundMethod(){
		return this.getBusinessDay().getTaxRound();
	}
	
	public int getTaxRoundDecimal() {
		return this.getBusinessDay().getTaxDecimal();
	}
	
	public String getGratuityRoundMethod(){
		return this.getBusinessDay().getGratuityRound();
	}
	
	public int getGratuityRoundDecimal() {
		return this.getBusinessDay().getGratuityDecimal();
	}
	
	// Override check rounding method
	public void overrideCheckRoundMethod(boolean bOverride, String sRoundMethod, int iDecimal){
		m_bOverrideCheckRound = bOverride;
		m_sOverrideCheckRoundMethod = sRoundMethod;
		m_iOverrideCheckRoundDecimal = iDecimal;
	}
	
	// Check if the check round is overrided
	public boolean isOverrideCheckRound() {
		return m_bOverrideCheckRound;
	}
	
	public int getOutletPeriodIdByBusinessPeriodId(String sBperId) {
		int iOutletPerdId = 0;
		if(this.m_oBusinessPeriods.containsKey(sBperId))
			iOutletPerdId = this.m_oBusinessPeriods.get(sBperId).getPeriodId();
		
		return iOutletPerdId;
	}
	
	public boolean isGenerateCheckNumByStation() {
		return m_oOutletSetting.isGenerateCheckNumByStation();
	}
	
	public boolean isResetCheckNum() {
		return m_oOutletSetting.isResetCheckNum();
	}
	
	public void buildTableNameList(){
		m_oListTablesInfo = new ArrayList<HashMap<String,String>>();
		for(Entry<Integer, OutFloorPlan> entry: getFloorPlanList().entrySet()){
			// Build Floor Plan
			OutFloorPlan oOutFloorPlan = entry.getValue();
			for(int i = 0; i<oOutFloorPlan.getMapCount(); i++){
				OutFloorPlanMap oOutFloorPlanMap = oOutFloorPlan.getMap(i);
				for(int j=0; j<oOutFloorPlanMap.getTableCount(); j++){
					OutFloorPlanTable oOutFloorPlanTable = oOutFloorPlanMap.getTable(j);
					HashMap<String, String> oFloorPlanTable = new HashMap<String, String>();
					oFloorPlanTable.put("table", Integer.toString(oOutFloorPlanTable.getTable()));
					oFloorPlanTable.put("tableExt", oOutFloorPlanTable.getTableExt());
					oFloorPlanTable.put("tableName", oOutFloorPlanTable.getName(AppGlobal.g_oCurrentLangIndex.get()));
					oFloorPlanTable.put("tableName1", oOutFloorPlanTable.getName(1));
					oFloorPlanTable.put("tableName2", oOutFloorPlanTable.getName(2));
					oFloorPlanTable.put("tableName3", oOutFloorPlanTable.getName(3));
					oFloorPlanTable.put("tableName4", oOutFloorPlanTable.getName(4));
					oFloorPlanTable.put("tableName5", oOutFloorPlanTable.getName(5));
					oFloorPlanTable.put("tableSize", Integer.toString(oOutFloorPlanTable.getSize()));
					m_oListTablesInfo.add(oFloorPlanTable);
				}
			}
		}
		
		//sorting the list by table Name
		if (!m_oListTablesInfo.isEmpty()) {
			Collections.sort(m_oListTablesInfo, new Comparator<Map<String, String>>() {
				@Override
				public int compare(Map<String, String> first, Map<String, String> second) {
					 return first.get("tableName").compareTo(second.get("tableName"));
				}
			});
		}
	}
	
	public List<HashMap<String,String>> getTableNameList(){
		List<HashMap<String, String>> m_oListTable = new ArrayList<HashMap<String, String>>();
		for(HashMap<String, String> oFloorPlanTable:m_oListTablesInfo) {
			if(!oFloorPlanTable.get("tableName").isEmpty()){
				HashMap<String, String> oTable = new HashMap<String, String>();
				oTable.put("table",oFloorPlanTable.get("table"));
				oTable.put("tableExt", oFloorPlanTable.get("tableExt"));
				oTable.put("tableName", oFloorPlanTable.get("tableName"));
				oTable.put("tableName1", oFloorPlanTable.get("tableName1"));
				oTable.put("tableName2", oFloorPlanTable.get("tableName2"));
				oTable.put("tableName3", oFloorPlanTable.get("tableName3"));
				oTable.put("tableName4", oFloorPlanTable.get("tableName4"));
				oTable.put("tableName5", oFloorPlanTable.get("tableName5"));
				oTable.put("tableSize", oFloorPlanTable.get("tableSize"));
				m_oListTable.add(oTable);
			}
		}
		
		return m_oListTable;
	}
	
	public boolean isTableNameExist(String sTableNo, String sTableExtension) {
		String sTableName ="";
		List<HashMap<String,String>> oTable = this.getTableNameList();
		for(HashMap<String, String> oFloorPlanTable:oTable) {
			if(oFloorPlanTable.get("table").equals(sTableNo) && oFloorPlanTable.get("tableExt").equals(sTableExtension)) {
				sTableName = oFloorPlanTable.get("tableName");
				break;
			}
		}
		
		if(sTableName.isEmpty())
			return false;
		else
			return true;
	}
	
	public String getTableSize(String sTableNo, String sTableExtension){
		String sTableSize = null;
		
		List<HashMap<String,String>> oTable = this.getTableNameList();
		for(HashMap<String, String> oFloorPlanTable:oTable) {
			if(oFloorPlanTable.get("table").equals(sTableNo) && oFloorPlanTable.get("tableExt").equals(sTableExtension)) {
				if (oFloorPlanTable.get("tableSize").length() > 0)
					sTableSize = oFloorPlanTable.get("tableSize");
				break;
			}
		}

		return sTableSize;
	}
	
	public String[] getTableName(String sTableNo, String sTableExtension){
		String[] sTableName = null;
		if(Integer.parseInt(sTableNo) != 0)
			sTableName = StringLib.createStringArray(5, sTableNo+sTableExtension);
		else
			sTableName = StringLib.createStringArray(5, sTableExtension);
		
		 
		List<HashMap<String,String>> oTable = this.getTableNameList();
		for(HashMap<String, String> oFloorPlanTable:oTable) {
			if(oFloorPlanTable.get("table").equals(sTableNo) && oFloorPlanTable.get("tableExt").equals(sTableExtension)) {
				if (oFloorPlanTable.get("tableName1").length() > 0)
					sTableName[0] = oFloorPlanTable.get("tableName1");
				if (oFloorPlanTable.get("tableName2").length() > 0)
					sTableName[1] = oFloorPlanTable.get("tableName2");
				if (oFloorPlanTable.get("tableName3").length() > 0)
					sTableName[2] = oFloorPlanTable.get("tableName3");
				if (oFloorPlanTable.get("tableName4").length() > 0)
					sTableName[3] = oFloorPlanTable.get("tableName4");
				if (oFloorPlanTable.get("tableName5").length() > 0)
					sTableName[4] = oFloorPlanTable.get("tableName5");
				break;
			}
		}

		return sTableName;
	}
	
	public String[] getTableNameWithTableNo(String sTableNo, String sTableExtension){
		String sTable = "";
		String[] sTableName = null;
		if(Integer.parseInt(sTableNo) != 0){
			sTable = sTableNo + sTableExtension;
			sTableName = StringLib.createStringArray(5, sTableNo+sTableExtension);
		}	
		else{
			sTable = sTableExtension;
			sTableName = StringLib.createStringArray(5, sTableExtension);
		}
			
		List<HashMap<String,String>> oTable = this.getTableNameList();
		for(HashMap<String, String> oFloorPlanTable:oTable) {
			if(oFloorPlanTable.get("table").equals(sTableNo) && oFloorPlanTable.get("tableExt").equals(sTableExtension)) {
				if (oFloorPlanTable.get("tableName1").length() > 0 && !sTable.equals(oFloorPlanTable.get("tableName1")))
					sTableName[0] = sTable + "(" + oFloorPlanTable.get("tableName1") + ")";
				if (oFloorPlanTable.get("tableName2").length() > 0 && !sTable.equals(oFloorPlanTable.get("tableName2")))
					sTableName[1] = sTable + "(" + oFloorPlanTable.get("tableName2") + ")";
				if (oFloorPlanTable.get("tableName3").length() > 0 && !sTable.equals(oFloorPlanTable.get("tableName3")))
					sTableName[2] = sTable + "(" + oFloorPlanTable.get("tableName3") + ")";
				if (oFloorPlanTable.get("tableName4").length() > 0 && !sTable.equals(oFloorPlanTable.get("tableName4")))
					sTableName[3] = sTable + "(" + oFloorPlanTable.get("tableName4") + ")";
				if (oFloorPlanTable.get("tableName5").length() > 0 && !sTable.equals(oFloorPlanTable.get("tableName5")))
					sTableName[4] = sTable + "(" + oFloorPlanTable.get("tableName5") + ")";
				break;
			}
		}

		return sTableName;
	}
	
	public HashMap<String, String> getTableNameInAllLang(String sTableNo, String sTableExtension){
		HashMap<String, String> oTableNameList = null;
		List<HashMap<String,String>> oTable = this.getTableNameList();
		for(HashMap<String, String> oFloorPlanTable:oTable) {
			if(oFloorPlanTable.get("table").equals(sTableNo) && oFloorPlanTable.get("tableExt").equals(sTableExtension)) {
				oTableNameList = new HashMap<String, String>();
				oTableNameList.put("tableName1", oFloorPlanTable.get("tableName1"));
				oTableNameList.put("tableName2", oFloorPlanTable.get("tableName2"));
				oTableNameList.put("tableName3", oFloorPlanTable.get("tableName3"));
				oTableNameList.put("tableName4", oFloorPlanTable.get("tableName4"));
				oTableNameList.put("tableName5", oFloorPlanTable.get("tableName5"));
				break;
			}
		}
		
		return oTableNameList;
	}
	
	// Purge current buiness date transaction data
	public void purgeCurrentBusinessDateTransactionData() {
		m_oBusinessDay.purgeTransactionData(m_oBusinessDay.getBdayId());
	}
	
	// get whether it is first new check
	public boolean isFirstNewCheck() {
		return m_bFirstNewCheck;
	}
	
	// set first new check
	public void setFirstNewCheck(boolean bSetup) {
		m_bFirstNewCheck = bSetup;
	}
}
