package app.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import app.model.PosConfig;
import app.model.PosInterfaceConfig;
import app.model.PosInterfaceConfigList;
import app.model.PosLicenseControlModel;
import app.model.PosOutletTableList;
import app.model.SystemModule;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import core.lang.LangResource;
import core.externallib.TCPLib;
import core.virtualui.*;

public class AppGlobal {
	public enum FUNC_LIST{send_check, cancel, paid, print_check, print_and_paid, change_cover, change_table, logout, void_check, release_payment, delete_item, delete_last, change_language, guest_check_preview, change_quantity_last, item_modifier, item_discount, item_discount_multiple_items, unlock_table, check_history, waive_sc_tax, add_sc_tax, open_drawer, cashier_mode, web_report, item_void_discount_multiple_items, check_listing, split_item_to_other_table, merge_table, switch_user, daily_start, daily_close, reload_business_setting, reprint_receipt, open_share_table, testing_printer, mark_delivery, print_serving_list, rush_order, pantry_message, admin_mode, toggle_fast_food_mode, stock_delivery_invoice_setup, receive_stock_delivery_invoice, switch_outlet, preorder, set_member, takeout, increment_course, reprint_last_receipt, last_20_paid_check, stock_operation, toggle_non_revenue, toggle_revenue, check_takeout, check_dine_in, toggle_ordering_mode, item_discount_all_items, delete_whole_last_item, void_paid_check, receipt_preview, add_item_by_code, sell_coupon, new_item_no_kitchen_slip, stock_soldout, direct_report, adjust_tips, open_first_drawer, open_second_drawer, search_item, delete_multiple_item, park_order_by_station, retrieve_park_order_by_station, preorder_by_table,
		user_time_in_out, pms_enquiry, octopus_read_card, octopus_add_value, octopus_reconnect, octopus_generate_trans_file, cashier_settlement, restart_pms_shell, set_menu_replace_item, repeat_item, repeat_multiple_items, check_smart_card, split_table, stock_balance_chg, split_check_by_options, check_discount, void_check_discount, adjust_payments, stop_pms_shell, paid_in, paid_out, retrieve_ordered_items_by_resv_confirm_no, toggle_self_order_kiosk_mode, set_call_number, insert_item, table_message_reminder, add_edit_check_info, manual_change_price_level, restart_payment_interface_shell, stop_payment_interface_shell, restart_auto_station, check_extra_charge, void_check_extra_charge, set_other_taiwan_gui_type, partial_send_check, reprint_last_taiwan_gui_receipt, reprint_taiwan_gui_receipt, set_taiwan_gui_next_trans_num, print_paid_check, select_pending_item, print_pending_item, reprint_guest_check, svc_enquiry, change_ordered_item_price_level, multiple_rush_order};
	
	public enum OPERATION_MODE{fine_dining, fast_food, stock_delivery, self_order_kiosk};
	public static String FAST_FOOD_TABLE_EXTENSION = "#";
	public static String STOCK_INVOICE_TABLE_EXTENSION = "$";
	
	public enum DISPLAY_MODE{no_display, horizontal_desktop, vertical_mobile};
	
	public static char TABLE_EXTENSION_START_LETTER = 'A';
	public static char TABLE_EXTENSION_END_LETTER = 'Z';
	
	public enum OPTIONAL_MODULE{member,signage,pos_interface,reservation};
	
	// Version no.
	public static String g_sVersion = "0.0.0.37 Rev1443";
	// Release date
	public static String g_sReleaseDate = "20160125";
	
	// Max seat no
	public static int MAX_SEATS=99;
	
	// Log level : 0 - Normal log; > 0 - More debug log
	public static int g_iLogLevel = 0;
	public static boolean g_bWriteClientConnectionLog = false;
	
	public static VariableManager<VirtualUITerm> g_oTerm;
	
	public static VariableManager<TCPLib> g_oTCP;
	public static VariableManager<Selector> g_oSelectorForTCP;
	public static VariableManager<SelectionKey> g_oSelectorKeyForTCP;
	
	public static VariableManager<Integer> g_oCurrentLangIndex;
	public static VariableManager<String> g_sDisplayMode;
	public static List<HashMap<String, String>> g_oSupportedLangList;
	public static String g_sSystemDataPath = ".";
	public static String g_sSystemDataUrl = "";
	
	// Global structure
	public static VariableManager<FuncStation> g_oFuncStation;
	public static VariableManager<FuncOutlet> g_oFuncOutlet;
	public static VariableManager<FuncUser> g_oFuncUser;
	public static VariableManager<FuncActionLog> g_oActionLog;
	public static VariableManager<FuncMenu> g_oFuncMenu;
	public static VariableManager<FuncOverride> g_oFuncOverride;
	public static VariableManager<FuncMixAndMatch> g_oFuncMixAndMatch;
	public static VariableManager<FuncOctopus> g_oFuncOctopus;
	
	// Language object
	public static VariableManager<LangResource> g_oLang;
	
	// Device manager connection element
	public static VariableManager<VirtualUIBasicElement> g_oDeviceManagerElement;
	
	// Station thread ID and station information list (share among all child thread)
	public static HashMap<Long, ClsActiveClient> g_lCurrentConnectClientList = new HashMap<Long, ClsActiveClient>();
	
	// Kill station and reason (share among all child thread)
	private static HashMap<Integer, String> g_oKilledStationIDAndReason = new HashMap<Integer, String>();
	
	// Flag during daily start / daily close / reload business setting
	private static ConcurrentHashMap<Integer, Date> g_oDailyOperationOutletList = new ConcurrentHashMap<Integer, Date>();
	
	// Store the last resume login id, password and card no. for each station
	public static HashMap<Long, String> g_oReconnectId = new HashMap<Long, String>();
	public static HashMap<Long, String> g_oReconnectPassword = new HashMap<Long, String>();
	public static HashMap<Long, String> g_oReconnectCardNo = new HashMap<Long, String>();
	
	// Store tender amount
	public static List<BigDecimal> g_oTenderAmountList = new ArrayList<BigDecimal>();
	
	// Support module flag
	private static boolean g_bSupportMember = false;
	private static boolean g_bSupportSignage = false;
	private static boolean g_bSupportPosInterface = false;
	private static boolean g_bSupportReservation = false;
	
	public static VariableManager<HashMap<String, HashMap<String, PosConfig>>> g_oPosConfigList;
	
	// Skip stock calculation
	public static boolean g_bNotCheckStock = false;
	
	// Background schedule jobs
	private static ScheduledExecutorService g_oScheduledService = Executors.newScheduledThreadPool(50);
	public static HashMap<String, AppBackgroundScheduleJob> g_oScheduledJobList = new HashMap<String, AppBackgroundScheduleJob>();
	public static HashMap<Integer, PosOutletTableList> g_oOutletTableList = new HashMap<Integer, PosOutletTableList>();
	
	// Check list for check payment result through payment interface
	public static HashMap<Integer, List<HashMap<String, String>>> g_oCheckListForPaymentInterface = new HashMap<Integer, List<HashMap<String, String>>>();
	
	public static HashMap<Integer, HashMap<String, String>> g_bPMS4700SerialPortLocking = new HashMap<Integer, HashMap<String, String>>();
	
	// Debug flag
	public static int g_iDebugMode = 0;		// 0 - no debug flow; 1 - auto order, payment
	
	// Store the interface config list
	public static VariableManager<PosInterfaceConfigList> g_oPosInterfaceConfigList;
		
	// License control model
	public static PosLicenseControlModel g_oPosLicenseControlModel = new PosLicenseControlModel();
	
	// Start daily operation, kill all other stations among the same outlet
	public static void startOutletOperationKillOtherStations(int iOutletId, String sReason){
		// Store the outlet ID which is doing the operation and the current time
		if(g_oDailyOperationOutletList.containsKey(iOutletId) == false)
			g_oDailyOperationOutletList.put(iOutletId, (new Date()));
		
		for(Map.Entry<Long, ClsActiveClient>entry:AppGlobal.g_lCurrentConnectClientList.entrySet()){
			ClsActiveClient oActiveClient = entry.getValue();
			if(oActiveClient.getCurrentOutletId() == iOutletId && oActiveClient.getCurrentStationId() != AppGlobal.g_oFuncStation.get().getStationId()){
				startKillSingleStation(oActiveClient.getCurrentStationId(), sReason);
			}
		}
	}
	
	// End daily operation
	public static void endOutletOperation(int iOutletId){
		if(g_oDailyOperationOutletList.containsKey(iOutletId))
			g_oDailyOperationOutletList.remove(iOutletId);
	}
	
	// Create if there is daily operation running
	public static boolean checkDailyOperationRunning(int iOutletId){
		if(g_oDailyOperationOutletList.containsKey(iOutletId)){
			Date oOperationTime = g_oDailyOperationOutletList.get(iOutletId);
			if((((new Date()).getTime() - oOperationTime.getTime())) > (120*1000)){
				// The operation time is 2 min before, should be expired
				// So no daily operation is running
				return false;
			}else {
				return true;
			}
		}
		
		return false;
	}
	
	// Create external daily operation file to kill all operating stations
	public static void createExternalDailyOperationFile(int iOutletId, String sReason){
		Path file = Paths.get("cfg" + java.io.File.separator + "dayclose" + iOutletId);
		try {
			Files.write(file, sReason.getBytes(), StandardOpenOption.CREATE_NEW);
		} catch (IOException e) {
			// Cannot create the file
		}
		
		// Sleep for a while for make sure all stations read the file
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// Check if auto-daily operation is performing by checking the existence of daily operation file
	public static String readExternalDailyOperationFile(int iOutletId)
	{
		Path file = Paths.get("cfg" + java.io.File.separator + "dayclose" + iOutletId);
		byte[] fileArray;
		try {
			fileArray = Files.readAllBytes(file);
			return new String(fileArray, "UTF-8");
		} catch (IOException e) {
			// Cannot read the file
		}
		return "";
	}
	
	// Remove external daily operation file
	public static void removeExternalDailyOperationFile(int iOutletId){
		File file = new File("cfg" + java.io.File.separator + "dayclose" + iOutletId);
		if (file.isFile()) {
			file.delete();
		}
	}
	
	// Kill a single station
	public static void startKillSingleStation(int iStationId, String sReason){
		if(g_oKilledStationIDAndReason.containsKey(iStationId) == false)
			g_oKilledStationIDAndReason.put(iStationId, sReason);
	}
	
	// Check if myself is killed or not
	public static String getKilledReason(){
		if(AppGlobal.g_oFuncStation == null || AppGlobal.g_oFuncStation.get() == null)
			return "";
		
		int iStationId = AppGlobal.g_oFuncStation.get().getStationId();
		if(g_oKilledStationIDAndReason.containsKey(iStationId)){			
			// Return the reason
			return g_oKilledStationIDAndReason.get(iStationId);
		}else
			return "";
	}
	
	// Finish being killed
	public static void finishBeingKilled(){
		int iStationId = AppGlobal.g_oFuncStation.get().getStationId();
		// Remove the kill command
		g_oKilledStationIDAndReason.remove(iStationId);
	}
	
	// Add reconnect Id and password of a station with UUID
	public static void addReconnectIdAndPasswordToStation(long lThreadId, String sLogin, String sPassword, String sCardNo){
		g_oReconnectId.put(lThreadId, sLogin);
		g_oReconnectPassword.put(lThreadId, sPassword);
		g_oReconnectCardNo.put(lThreadId, sCardNo);
	}
	
	// Get reconnect Id by thread Id
	public static String getReconnectId(long lThreadId){
		if(g_oReconnectId.containsKey(lThreadId)){
			return g_oReconnectId.get(lThreadId);
		}else
			return null;
	}
	
	// Get reconnect password by thread Id
	public static String getReconnectPassword(long lThreadId){
		if(g_oReconnectPassword.containsKey(lThreadId)){
			return g_oReconnectPassword.get(lThreadId);
		}else
			return null;
	}
	
	// Get reconnect card no by thread Id
	public static String getReconnectCardNo(long lThreadId){
		if(g_oReconnectCardNo.containsKey(lThreadId)){
			return g_oReconnectCardNo.get(lThreadId);
		}else
			return null;
	}
	
	public static void setPosConfigList(HashMap<String, HashMap<String, PosConfig>> oPosConfigList) {
		g_oPosConfigList.set(oPosConfigList);
	}
	
	public static PosConfig getPosConfig(String sSection, String sVariable) {
		HashMap<String, HashMap<String, PosConfig>> oConfigList = g_oPosConfigList.get();
		if(!oConfigList.containsKey(sSection))
			return null;
		
		if(!oConfigList.get(sSection).containsKey(sVariable))
			return null;
		
		return oConfigList.get(sSection).get(sVariable);
	}
	
	public static void setPosInterfaceConfigList(PosInterfaceConfigList oPosInterfaceConfigList) {
		g_oPosInterfaceConfigList.set(oPosInterfaceConfigList);
	}
	
	public static List<PosInterfaceConfig> getPosInterfaceConfigByInfType(String sInfType) {
		return g_oPosInterfaceConfigList.get().getInterfaceConfigListByInterfaceType(sInfType);
	}
	
	public static void writeActionLog(String sSationID, String sLogin, String sLog){
		
		DateTime today = new DateTime();
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);

		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/hero_app." + sSationID + "." + sCurrentMonth;

		try{
			// Create file 
			FileWriter fstream = new FileWriter(sFilename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			sContent.append(sCurrentTime);
			sContent.append(" [s:");
			sContent.append(sSationID);
			sContent.append(" u:");
			sContent.append(sLogin);
			sContent.append("] ");
			sContent.append(sLog);
			sContent.append("\n");
			
			out.write(sContent.toString());
			
			//Close the output stream
			out.close();
			fstream.close();
		}catch (Exception e){//Catch exception if any
			stack2Log(e);
		}
	}
	
	public static void writeErrorLog(String sClass, String sMethod, String sSationID, String sLogin, String sLog){
		
		DateTime today = new DateTime();
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);
		
		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/hero_app_err." + sCurrentMonth;

		try{
			// Create file 
			FileWriter fstream = new FileWriter(sFilename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			sContent.append(sCurrentTime);
			//sContent.append(" [");
			sContent.append(" [s:");
			sContent.append(sSationID);
			sContent.append(" u:");
			sContent.append(sLogin);
			sContent.append("] ["+sClass);
			sContent.append(":");
			sContent.append(sMethod);
			sContent.append("] ");
			sContent.append(sLog);
			sContent.append("\n");
			
			out.write(sContent.toString());
			
			//Close the output stream
			out.close();
			fstream.close();
		}catch (Exception e){//Catch exception if any
			stack2Log(e);
		}
	}
	
	public static void writeDebugLog(String sClass, String sMethod, String sLog){		
		DateTime today = new DateTime();
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss.SSS");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);
		
		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/hero_app_debug." + sCurrentMonth;

		try{
			// Create file 
			FileWriter fstream = new FileWriter(sFilename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			sContent.append(sCurrentTime);
			sContent.append(" [");
			sContent.append(sClass);
			sContent.append(":");
			sContent.append(sMethod);
			sContent.append(":");
			sContent.append(Thread.currentThread().getId());
			sContent.append("] ");
			sContent.append(sLog);
			sContent.append("\n");
			
			out.write(sContent.toString());
			
			//Close the output stream
			out.close();
			fstream.close();
		}catch (Exception e){//Catch exception if any
			stack2Log(e);
		}
	}
	
	public static void stack2Log(Exception e) {
		
		DateTime today = new DateTime();
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);
		
		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/hero_app_exception." + sCurrentMonth;
		
		try{
			// Create file 
			FileWriter fstream = new FileWriter(sFilename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			sContent.append(sCurrentTime);
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			
			sContent.append(" ");
			sContent.append(sw.toString());
			
			out.write(sContent.toString());
			
			//Close the output stream
			out.close();
			fstream.close();
		}catch (Exception e2){//Catch exception if any
			e2.printStackTrace();
		}
	}
	
	// Determine if the module is supported or not during start up
	public static void checkModuleExisting() {
		// Get the member module
		SystemModule oSystemModule = new SystemModule();
		if(oSystemModule.readByAlias(AppGlobal.OPTIONAL_MODULE.member.name()))
			// Support member module
			g_bSupportMember = true;
		else
			// Not support member module
			g_bSupportMember = false;
		
		if(oSystemModule.readByAlias(AppGlobal.OPTIONAL_MODULE.signage.name()))
			g_bSupportSignage = true;	// Support signage module
		else
			g_bSupportSignage = false;	// Not support signage module
		
		if(oSystemModule.readByAlias(AppGlobal.OPTIONAL_MODULE.pos_interface.name()))
			g_bSupportPosInterface = true;	// Support interface module
		else
			g_bSupportPosInterface = false;	// Not support interface module
		
		if(oSystemModule.readByAlias(AppGlobal.OPTIONAL_MODULE.reservation.name()))
			g_bSupportReservation = true;	// Support reservation module
		else
			g_bSupportReservation = false;	// Not support reservation module
	}
	
	// Check if the module is supported or not
	public static boolean isModuleSupport(String sModule) {
		boolean bRet = false;
		
		if(sModule.equals(AppGlobal.OPTIONAL_MODULE.member.name())){
			if(g_bSupportMember)
				bRet = true;
		}
		else if(sModule.equals(AppGlobal.OPTIONAL_MODULE.signage.name())){
			if(g_bSupportSignage)
				bRet = true;
		}
		else if(sModule.equals(AppGlobal.OPTIONAL_MODULE.pos_interface.name())) {
			if(g_bSupportPosInterface)
				bRet = true;
		}
		else if(sModule.equals(AppGlobal.OPTIONAL_MODULE.reservation.name())) {
			if(g_bSupportReservation)
				bRet = true;
		}
		
		return bRet;
	}
	
	public static String stackToString(Exception e){
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
	
	// Show memory usage
	public static void showMemory() {
		
		int mb = 1024*1024;
		
        //Getting the runtime reference from system
		Runtime runtime = Runtime.getRuntime();
		
		System.out.println("##### Heap utilization statistics [MB] #####");
		
		//Print used memory
		System.out.println("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb);
		
		//Print free memory
		System.out.println("Free Memory:" + runtime.freeMemory() / mb);
		
		//Print total available memory
		System.out.println("Total Memory:" + runtime.totalMemory() / mb);
		
		//Print Maximum available memory
		System.out.println("Max Memory:" + runtime.maxMemory() / mb);
		
		System.out.println("============================================");
    }
	
	// Check memory usage
	// If free memory under 10Mb, write debug log
	public static void checkMemory() {
		
		int mb = 1024*1024;
		
		//Getting the runtime reference from system
		Runtime runtime = Runtime.getRuntime();
		
		// Write log if under 10Mb
		if((runtime.freeMemory() / mb) > 10.0)
			return;
		
		StringBuilder sb = new StringBuilder();
		
		//Print used memory
		sb.append("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb + " ");
		
		//Print free memory
		sb.append("Free Memory:" + runtime.freeMemory() / mb + " ");
		
		//Print total available memory
		sb.append("Total Memory:" + runtime.totalMemory() / mb + " ");
		
		//Print Maximum available memory
		sb.append("Max Memory:" + runtime.maxMemory() / mb + "");
		
		AppGlobal.writeDebugLog("AppGlobal", new Exception().getStackTrace()[0].getMethodName(), sb.toString());
	}
	
	public static void addBackgroundScheduleTask(String sTaskType, int iId, int iPeriod) {
		//check whether schedule job exist
		String sKey = sTaskType+"_"+String.valueOf(iId);		
		if(g_oScheduledJobList.containsKey(sKey))
			return;
		
		//add the new background schedule job
		AppBackgroundScheduleJob oBackgroundJob = new AppBackgroundScheduleJob(sTaskType, iId);
		g_oScheduledJobList.put(sKey, oBackgroundJob);
		ScheduledFuture<?> oScheduledFuture = g_oScheduledService.scheduleWithFixedDelay(oBackgroundJob, 0, iPeriod, TimeUnit.SECONDS);
		oBackgroundJob.setScheduledFuture(oScheduledFuture);
	}
	
	public static void removeBackgroundScheduleTask(String sKey) {
		if(!g_oScheduledJobList.containsKey(sKey))
			return;
		
		g_oScheduledJobList.get(sKey).setStop(true);
		ScheduledFuture<?> oScheduledFuture = g_oScheduledJobList.get(sKey).getScheduledFuture();
		oScheduledFuture.cancel(false);
		g_oScheduledJobList.remove(sKey);
	}
	
	synchronized public static void setOutletTableList(int iOutletId, PosOutletTableList oPosOutletTableList) {
		g_oOutletTableList.put(Integer.valueOf(iOutletId), oPosOutletTableList);
	}
	
	synchronized public static PosOutletTableList getOutletTableList(int iOutletId) {
		if(!g_oOutletTableList.containsKey(Integer.valueOf(iOutletId)))
			return null;
		else
			return g_oOutletTableList.get(Integer.valueOf(iOutletId));
	}
	
	synchronized public static void addPrintedCheckToPaymentInterfaceCheckList(int iOutletId, HashMap<String, String> oPrintedCheckInfo) {
		if(!g_oCheckListForPaymentInterface.containsKey(iOutletId))
			g_oCheckListForPaymentInterface.put(iOutletId, new ArrayList<HashMap<String, String>>());
		g_oCheckListForPaymentInterface.get(iOutletId).add(oPrintedCheckInfo);
	}
	
	synchronized public static void removePrintedCheckToPaymentInterfaceCheckList(int iOutletId, int iCheckId,String sOutTradeNumber) {
		if(!g_oCheckListForPaymentInterface.containsKey(iOutletId))
			return;
		
		List<HashMap<String, String>> oPrintedCheckList = g_oCheckListForPaymentInterface.get(iOutletId);
		if(oPrintedCheckList.isEmpty())
			return;
		
		int iTargetIndex = -1;
		for(int i=0; i<oPrintedCheckList.size(); i++) {
			HashMap<String, String> oPrintedCheckInfo = oPrintedCheckList.get(i);
			if(oPrintedCheckInfo.containsKey("checkId") && Integer.valueOf(oPrintedCheckInfo.get("checkId")).intValue() == iCheckId && oPrintedCheckInfo.containsKey("outTradeNumber") && oPrintedCheckInfo.get("outTradeNumber").equals(sOutTradeNumber)) {
				iTargetIndex = i;
				break;
			}
		}
		
		if(iTargetIndex >= 0)
			oPrintedCheckList.remove(iTargetIndex);
	}
	
	public static ClsActiveClient getActiveClient(){
		if(g_lCurrentConnectClientList.containsKey(Thread.currentThread().getId())){
			return g_lCurrentConnectClientList.get(Thread.currentThread().getId());
		}else{
			return null;
		}
	}
	
	synchronized public static boolean isPrintedCheckListExist(int iOutletId) {
		if(!g_oCheckListForPaymentInterface.containsKey(iOutletId))
			return false;
		
		List<HashMap<String, String>> oPrintedCheckList = g_oCheckListForPaymentInterface.get(iOutletId);
		if(oPrintedCheckList == null)
			return false;
		
		return true;
	}
	
	public static String getLoginErrorMessage(String sErrorMessageFromAPI){
		switch (sErrorMessageFromAPI) {
		case "fail_to_get_platform_key":
			return AppGlobal.g_oLang.get()._("fail_to_get_platform_key");
		case "invalid_platform_key":
			return AppGlobal.g_oLang.get()._("invalid_platform_key");
		case "exceed_time_limit":
			return AppGlobal.g_oLang.get()._("exceed_time_limit");
		case "fail_to_load_model":
			return AppGlobal.g_oLang.get()._("fail_to_load_model");
		case "missing_license_cert":
			return AppGlobal.g_oLang.get()._("missing_license_cert");
		case "invalid_license_cert":
			return AppGlobal.g_oLang.get()._("invalid_license_cert");
		case "invalid_license_cert_format":
			return AppGlobal.g_oLang.get()._("invalid_license_cert_format");
		case "digital_signature_not_match":
			return AppGlobal.g_oLang.get()._("digital_signature_not_match");
		case "platform_key_not_match":
			return AppGlobal.g_oLang.get()._("platform_key_not_match");
		case "subscription_expired":
			return AppGlobal.g_oLang.get()._("subscription_expired");
		case "cert_expired":
			return AppGlobal.g_oLang.get()._("cert_expired");
		default:
			return AppGlobal.g_oLang.get()._("fail_to_login");
		}
	}
	
	public static String getLicenseErrorMessage(int iStationId){
		String sDisplayMessage = "";
		
		String sErrorMessageFromAPI = g_oPosLicenseControlModel.checkLicenseForPOSModule(iStationId);
		if(sErrorMessageFromAPI.length() > 0){
			switch (sErrorMessageFromAPI) {
			case "module_not_support":
				sDisplayMessage = AppGlobal.g_oLang.get()._("module_not_support");
				break;
			case "exceed_stations":
				sDisplayMessage = AppGlobal.g_oLang.get()._("exceed_stations");
				break;
			case "missing_license_cert":
				sDisplayMessage = AppGlobal.g_oLang.get()._("missing_license_cert");
				break;
			case "restriction_key_not_match":
				sDisplayMessage = AppGlobal.g_oLang.get()._("restriction_key_not_match");
				break;
			case "station_not_found":
				sDisplayMessage = AppGlobal.g_oLang.get()._("no_such_station");
				break;
			default:
				sDisplayMessage = AppGlobal.g_oLang.get()._("internal_error");
				break;
			}
		}
		
		return sDisplayMessage;
	}
	
	public static String getLicenseWarningMessage(){
		String sWarningMessage = "";
		
		long lLicenseCertDateToExpired = g_oPosLicenseControlModel.getLicenseCertDateToExpired();
		if(lLicenseCertDateToExpired >= 0){
			sWarningMessage = AppGlobal.g_oLang.get()._("hero_cert_will_be_expired_in") + " " + lLicenseCertDateToExpired + " " + AppGlobal.g_oLang.get()._("cert_expire_day");
		}
		
		return sWarningMessage;
	}
	
	public static String getLicenseExpireWarningMessage(){
		String sWarningMessage = "";
		
		long lLicenseCertDateToExpired = g_oPosLicenseControlModel.getLicenseCertDateToSubscriptionExpired();
		if(lLicenseCertDateToExpired > 0 && lLicenseCertDateToExpired <= 30){
			sWarningMessage = AppGlobal.g_oLang.get()._("your_annual_software_subscription_will_expire_in") + " " + lLicenseCertDateToExpired + " " + AppGlobal.g_oLang.get()._("cert_expire_day") + "\n" +
					AppGlobal.g_oLang.get()._("please_contact_your_local_suppliers_or") + "\n" + 
					AppGlobal.g_oLang.get()._("infrasys_international_limited_to_renew_the_subscription") + "\n" + 
					AppGlobal.g_oLang.get()._("you_cannot_use_the_system_if_you_do_not_renew") + "\n" + 
					AppGlobal.g_oLang.get()._("the_subscription_before_the_expiry_date");
		}
		
		return sWarningMessage;
	}
}
