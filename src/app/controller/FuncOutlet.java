package app.controller;

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
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.controller.AppGlobal;
import externallib.StringLib;
import externallib.Util;
import app.model.OutFloorPlan;
import app.model.OutFloorPlanFunc;
import app.model.OutFloorPlanMap;
import app.model.OutFloorPlanTable;
import app.model.OutOutlet;
import app.model.OutShop;
import app.model.OutSpecialHour;
import app.model.OutSpecialHourList;
import app.model.PosBusinessDay;
import app.model.PosBusinessPeriod;
import app.model.PosCheck;
import app.model.PosOutletItem;
import app.model.PosOutletSetting;
import app.model.PosItemPrintQueueList;
import app.model.PosStockTransaction;
import app.AppThreadManager;

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
	private LinkedHashMap<Integer,PosBusinessPeriod> m_oBusinessPeriods;
	
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
	
	// Table and table name pair list
	public List<HashMap<String, String>> m_oListTablesInfo;
	
	// Manual change price level
	// -1 : no change manually
	// >0 : Price level change manually
	private int m_iManualPriceLevel;
	
	// Last error message
	private String m_sErrorMessage;
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage;
	}
	
	public FuncOutlet() {
		m_oOutlet = new OutOutlet();
		m_oShop = new OutShop();
		m_oOutletSetting = new PosOutletSetting();
		m_oBusinessDay = new PosBusinessDay();
		m_oBusinessPeriods = new LinkedHashMap<Integer, PosBusinessPeriod>();
		m_oSpecialPeriodList = new OutSpecialHourList();
		m_oFloorPlanFunc = new OutFloorPlanFunc();
		m_oItemPrintQueueList = new PosItemPrintQueueList();
		
		m_bOverrideCheckRound = false;
		m_sOverrideCheckRoundMethod = "";
		m_iOverrideCheckRoundDecimal = 0;
		
		m_iManualPriceLevel = -1;
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
		
		// Create parameter array
		Object[] oParameters = new Object[1];
		oParameters[0] = m_oOutlet.getOletId();
		
		Object[] oParameters1 = new Object[2];
		oParameters1[0] = m_oOutlet.getOletId();
		oParameters1[1] = m_oOutlet.getOutletShopId();
		
		Object[] oParameters2 = new Object[1];
		oParameters2[0] = m_oOutlet.getOutletShopId();
		
		// Add the method to the thread manager
		oAppThreadManager.addThread(1, this, "loadBusinessDay", oParameters);
		oAppThreadManager.addThread(2, this, "loadFloorPlan", oParameters);
		oAppThreadManager.addThread(3, this, "loadItemPrintQueue", oParameters1);
		oAppThreadManager.addThread(4, this, "loadOutletSetting", oParameters);
		oAppThreadManager.addThread(5, this, "loadShop", oParameters2);

		// Run all of the threads
		oAppThreadManager.runThread();

		// Wait for the thread to finish
		oAppThreadManager.waitForThread();		

		// Get the result
		if((Boolean)oAppThreadManager.getResult(1) == false){
			// Error
			m_sErrorMessage = AppGlobal.g_oLang.get()._("daily_start_has_not_been_carried_out");
			return 2;
		} else {
			if (!loadBusinessPeriod()) {
				m_sErrorMessage = AppGlobal.g_oLang.get()._("missing_business_period");
				return 0;
			}
		}
		
		if((Boolean)oAppThreadManager.getResult(2) == false){
			// Error
			m_sErrorMessage = AppGlobal.g_oLang.get()._("missing_floor_plan");
			return 1;
		}
		
		if((Boolean)oAppThreadManager.getResult(3) == false){
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
	
	public int getOutletId(){
		return m_oOutlet.getOletId();
	}
	
	public String getOutletCode(){
		return m_oOutlet.getCode();
	}
	
	public String getOutletNameByIndex(int iIndex) {
		return m_oOutlet.getName(iIndex);
	}
	
	public int getShopId(){
		return m_oOutlet.getOutletShopId();
	}
	
	public String getCurrencySign() {
		return m_oOutlet.getCurrencySign();
	}
	
	public List<Integer> getOutletGroupList() {
		return m_oOutlet.getOutletGroupList();
	}
	
	// Load business day
	public boolean loadBusinessDay(int iOutletId){
		m_sErrorMessage = "";
		
		// Read business day
		if(!m_oBusinessDay.readByOutletId(iOutletId)) {
			return false;
		}
		
		if(m_oBusinessDay.getBdayId() == 0) {
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
		
		if(m_oBusinessDay.getBdayId() == 0) {
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
		DateTime today = new DateTime();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm:ss");
		String sCurrentTime = fmt.print(today);
		
		for(Map.Entry<Integer, PosBusinessPeriod> entry:m_oBusinessPeriods.entrySet()){
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
	public PosBusinessPeriod getBusinessPeriodById(int iBperId) {
		return m_oBusinessPeriods.get(iBperId);
	}
	
	// get formated business day
	public String getFormatBusinessDay() {
		String langCode = "en";
		int langIndex = 1;
		for(HashMap<String, String> oLangInfo:AppGlobal.g_oSupportedLangList) {
			langIndex = Integer.parseInt(oLangInfo.get("index"));
			if(langIndex == AppGlobal.g_oCurrentLangIndex.get()) {
				langCode = oLangInfo.get("code");
				break;
			}
		}
		SimpleDateFormat dateFormat;
		if(langCode.equals("zh-hk"))
			dateFormat = new SimpleDateFormat("EEE , dd-MMM-yyyy", Locale.CHINESE);
		else
		if(langCode.equals("zh-cn"))
			dateFormat = new SimpleDateFormat("EEE , dd-MMM-yyyy", Locale.CHINA);
		else
		if(langCode.equals("jpn"))
			dateFormat = new SimpleDateFormat("EEE , dd-MMM-yyyy", Locale.JAPAN);
		else
		if(langCode.equals("kor"))
			dateFormat = new SimpleDateFormat("EEE , dd-MMM-yyyy", Locale.KOREA);
		else
			dateFormat = new SimpleDateFormat("EEE , dd-MMM-yyyy", Locale.ENGLISH);

		return dateFormat.format(m_oBusinessDay.getDate());
	}
	
	// Load business day period
	public boolean loadBusinessPeriod(){		
		int iBusinessDayId = m_oBusinessDay.getBdayId();
		int iOutletId = m_oOutlet.getOletId();
		
		m_sErrorMessage = "";
		
		PosBusinessPeriod oBusinessPeriod = new PosBusinessPeriod();
		JSONArray oBusinessPeriodJSONArray = oBusinessPeriod.getBusinessPeriodListByBdayIdOutletId(iBusinessDayId, iOutletId);
		if(oBusinessPeriodJSONArray == null)
			return false;
		for (int i = 0; i < oBusinessPeriodJSONArray.length(); i++) {
			try {
				JSONObject oBusinessPeriodJSONObject = oBusinessPeriodJSONArray.getJSONObject(i);
				PosBusinessPeriod tempBusinessPeriod = new PosBusinessPeriod(oBusinessPeriodJSONObject);
				m_oBusinessPeriods.put(tempBusinessPeriod.getBperId(), tempBusinessPeriod);
				
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
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
	public void updateBusinessPeriod(int businessDayId, int outletId, int type) {
		DateTime today = new DateTime();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		int isDailyStart = 0;
		
		PosBusinessPeriod oBusinessPeriod = new PosBusinessPeriod();
		JSONArray tempBusinessPeriodJSONArray = oBusinessPeriod.getBusinessPeriodListByBdayIdOutletId(businessDayId, outletId);
		if(tempBusinessPeriodJSONArray != null) {
			for (int i = 0; i < tempBusinessPeriodJSONArray.length(); i++) {
				try {
					JSONObject tempJSONObject = tempBusinessPeriodJSONArray.getJSONObject(i);
					PosBusinessPeriod tempBusinessPeriod = new PosBusinessPeriod(tempJSONObject);
					if (type == 1) {
						tempBusinessPeriod.setStartUserId(AppGlobal.g_oFuncUser.get().getUserId());
						tempBusinessPeriod.setEndUserId(0);
						
						tempBusinessPeriod.setLastRecallTime(fmt.print(today.withZone(DateTimeZone.UTC)));
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
				} catch (JSONException e) {
					AppGlobal.stack2Log(e);
				}
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
	
	// Load Outlet Setting
	public boolean loadOutletSetting(int iOutletId) {
		m_sErrorMessage = "";
		
		//Read Outlet Setting
		if(!m_oOutletSetting.readByOutletId(iOutletId)) {
			return false;
		}
		
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
		JSONObject resultJSONObject = oBusinessDay.dailyStart(m_oOutlet.getOletId(), AppGlobal.g_oFuncUser.get().getUserId(), bRecallLastBusinessDay);
		if(resultJSONObject == null) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("daily_start_is_unsuccessful");
			return false;
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
		PosCheck check = new PosCheck(AppGlobal.g_oFuncUser.get().getUserId());
		JSONArray oCheckList = check.getCheckListByBusinessDayPaid(m_oBusinessDay.getBdayId(), 0, PosCheck.PAID_NOT_PAID, false);
		if (oCheckList != null && oCheckList.length() > 0) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("some_checks_have_not_paid");
			return false;
		}
		
		// Stop the thread for checking update of table status
		AppGlobal.removeBackgroundScheduleTask(AppBackgroundScheduleJob.TYPE_UPDATE_TABLE_STATUS+"_"+m_oOutlet.getOletId());
		
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
		
		if(oMenuItemIds.size() > 0){
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

		PosBusinessDay oBusinessDay = new PosBusinessDay();
		JSONObject resultJSONObject = oBusinessDay.reloadBusinessSetting(m_oBusinessDay.getBdayId(), AppGlobal.g_oFuncUser.get().getUserId());
		if(resultJSONObject == null) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("reload_business_setting_is_unsuccessful");
			return false;
		}
		
		// Finish the operation
		AppGlobal.endOutletOperation(iOutletId);
		
		return true;
	}
	
	public void changeDate (int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		Date date = cal.getTime();
		m_oBusinessDay.setDate(date);
	}
	
	// check whether today is after business day/business hour
	public boolean checkCrossDay() {	    
		if(m_oBusinessPeriods.isEmpty())
			return false;
		
		SimpleDateFormat oDateFormat= new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		SimpleDateFormat oTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		
		DateTime oBusinessDay = new DateTime(m_oBusinessDay.getDate());

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
		if(m_iManualPriceLevel >= 0){
			return m_iManualPriceLevel;
		}else{
			return this.m_oBusinessDay.getPriceLevel();
		}
	}
	
	public void setManualPriceLevel(int iManualPriceLevel) {
		m_iManualPriceLevel = iManualPriceLevel;
	}
	
	public boolean isPriceLevelChangeManually() {
		return (m_iManualPriceLevel >= 0);
	}
	
	public String getCurrentBusinessPeriodName(int iIndex) {
		PosBusinessPeriod oPosBusinessPeriod = this.getBusinessPeriod();
		return oPosBusinessPeriod.getName(iIndex);
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
		return m_oOutletSetting.getStartCheckNumber();
	}
	
	// Rounding method
	public BigDecimal roundItemAmountToBigDecimal(BigDecimal dAmount){
		String sRoundMethod = this.getBusinessDay().getItemRound();
		int iRoundDecimal = this.getBusinessDay().getItemDecimal();
		
		return Util.HERORound(dAmount, sRoundMethod, iRoundDecimal);
	}
	
	public String roundItemAmountToString(BigDecimal dAmount){
		String sRoundMethod = this.getBusinessDay().getItemRound();
		int iRoundDecimal = this.getBusinessDay().getItemDecimal();
		
		return StringLib.BigDecimalToString(Util.HERORound(dAmount, sRoundMethod, iRoundDecimal), iRoundDecimal);
	}
	
	public BigDecimal roundSCAmountToBigDecimal(BigDecimal dAmount){
		String sRoundMethod = this.getBusinessDay().getScRound();
		int iRoundDecimal = this.getBusinessDay().getScDecimal();
		
		return Util.HERORound(dAmount, sRoundMethod, iRoundDecimal);
	}
	
	public String roundSCAmountToString(BigDecimal dAmount){
		String sRoundMethod = this.getBusinessDay().getScRound();
		int iRoundDecimal = this.getBusinessDay().getScDecimal();
		
		return StringLib.BigDecimalToString(Util.HERORound(dAmount, sRoundMethod, iRoundDecimal), iRoundDecimal);
	}
	
	public BigDecimal roundTaxAmountToBigDecimal(BigDecimal dAmount){
		String sRoundMethod = this.getBusinessDay().getTaxRound();
		int iRoundDecimal = this.getBusinessDay().getTaxDecimal();
		
		return Util.HERORound(dAmount, sRoundMethod, iRoundDecimal);
	}
	
	public String roundTaxAmountToString(BigDecimal dAmount){
		String sRoundMethod = this.getBusinessDay().getTaxRound();
		int iRoundDecimal = this.getBusinessDay().getTaxDecimal();
		
		return StringLib.BigDecimalToString(Util.HERORound(dAmount, sRoundMethod, iRoundDecimal), iRoundDecimal);
	}
	
	public BigDecimal roundDiscAmountToBigDecimal(BigDecimal dAmount){
		String sRoundMethod = this.getBusinessDay().getDiscRound();
		int iRoundDecimal = this.getBusinessDay().getDiscDecimal();
		
		return Util.HERORound(dAmount, sRoundMethod, iRoundDecimal);
	}
	
	public String roundDiscAmountToString(BigDecimal dAmount){
		String sRoundMethod = this.getBusinessDay().getDiscRound();
		int iRoundDecimal = this.getBusinessDay().getDiscDecimal();
		
		return StringLib.BigDecimalToString(Util.HERORound(dAmount, sRoundMethod, iRoundDecimal), iRoundDecimal);
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
		
		return Util.HERORound(dAmount, sRoundMethod, iRoundDecimal);
	}
	
	public String roundCheckAmountToString(BigDecimal dAmount){
		String sRoundMethod;
		int iRoundDecimal;
		
		if(m_bOverrideCheckRound){
			sRoundMethod = m_sOverrideCheckRoundMethod;
			iRoundDecimal = m_iOverrideCheckRoundDecimal;
		}else{
			sRoundMethod = this.getBusinessDay().getCheckRound();
			iRoundDecimal = this.getBusinessDay().getCheckDecimal();
		}
		
		return StringLib.BigDecimalToString(Util.HERORound(dAmount, sRoundMethod, iRoundDecimal), iRoundDecimal);
	}
	
	public BigDecimal roundPaymentAmountToBigDecimal(BigDecimal dAmount){
		String sRoundMethod = this.getBusinessDay().getPayRound();
		int iRoundDecimal = this.getBusinessDay().getPayDecimal();
		
		return Util.HERORound(dAmount, sRoundMethod, iRoundDecimal);
	}
	
	public String roundPaymentAmountToString(BigDecimal dAmount){
		String sRoundMethod = this.getBusinessDay().getPayRound();
		int iRoundDecimal = this.getBusinessDay().getPayDecimal();
		
		return StringLib.BigDecimalToString(Util.HERORound(dAmount, sRoundMethod, iRoundDecimal), iRoundDecimal);
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
	
	public int getOutletPeriodIdByBusinessPeriodId(int iBperId) {
		int iOutletPerdId = 0;
		if(this.m_oBusinessPeriods.containsKey(iBperId))
			iOutletPerdId = this.m_oBusinessPeriods.get(iBperId).getPeriodId();
		
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
		for(Entry<Integer, OutFloorPlan> entry:AppGlobal.g_oFuncOutlet.get().getFloorPlanList().entrySet()){					
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
					m_oListTablesInfo.add(oFloorPlanTable);
				}
			}
		}
		
		//sorting the list by table Name
		if (m_oListTablesInfo.size() > 0) {
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
	
	public String getTableName(String sTableNo, String sTableExtension){
		String sTableName ="";
		List<HashMap<String,String>> oTable = this.getTableNameList();
		for(HashMap<String, String> oFloorPlanTable:oTable) {
			if(oFloorPlanTable.get("table").equals(sTableNo) && oFloorPlanTable.get("tableExt").equals(sTableExtension)) {
				sTableName = oFloorPlanTable.get("tableName");
				break;
			}
		}
		if(sTableName.isEmpty()){
			return sTableNo+sTableExtension;
		}
		return sTableName;
	}
	
	public String getTableNameWithTableNo(String sTableNo, String sTableExtension){
		String sTable = sTableNo + sTableExtension;
		String sTableName = "";
		List<HashMap<String,String>> oTable = this.getTableNameList();
		for(HashMap<String, String> oFloorPlanTable:oTable) {
			if(oFloorPlanTable.get("table").equals(sTableNo) && oFloorPlanTable.get("tableExt").equals(sTableExtension)) {
				sTableName = oFloorPlanTable.get("tableName");
				break;
			}
		}
		
		if(!sTableName.isEmpty() && !sTable.equals(sTableName))
			sTable = sTable + "(" + sTableName + ")";
		
		return sTable;
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
}
