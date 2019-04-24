package app;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import core.externallib.StringLib;
import externallib.IniReader;
import externallib.TCPLib;
import externallib.Util;
import lang.LangResource;
import om.FailoverStationGroup;
import om.InfInterface;
import om.InfVendor;
import om.MenuPriceLevelList;
import om.PosConfig;
import om.PosGeneral;
import om.PosInterfaceConfig;
import om.PosInterfaceConfigList;
import om.PosItemRemindRule;
import om.PosItemRemindRuleList;
import om.PosLicenseControlModel;
import om.PosOutletTable;
import om.PosOutletTableList;
import om.PosStationDevice;
import om.PosStationList;
import om.PrtPrintQueue;
import om.SystemConfig;
import om.SystemGeneral;
import om.SystemModule;
import om.WohAwardSettingList;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUITerm;

public class AppGlobal {
	public final static boolean ERROR_TO_CONSOLE = true;
	
	public enum FUNC_LIST{send_check, cancel, paid, print_check, print_and_paid, change_cover, change_table, logout, void_check, release_payment, delete_item, delete_last, change_language, guest_check_preview, change_quantity_last, item_modifier, item_discount, item_discount_multiple_items, unlock_table, check_history, waive_sc_tax, add_sc_tax, open_drawer, cashier_mode, web_report, item_void_discount_multiple_items, check_listing, split_item_to_other_table, merge_table, switch_user, daily_start, daily_close, reload_business_setting, reprint_receipt, open_share_table, testing_printer, mark_delivery, print_serving_list, rush_order, pantry_message, admin_mode, toggle_fast_food_mode, stock_delivery_invoice_setup, receive_stock_delivery_invoice, switch_outlet, preorder, set_member, takeout, increment_course, reprint_last_receipt, last_20_paid_check, stock_operation, toggle_non_revenue, toggle_revenue, check_takeout, check_dine_in, toggle_ordering_mode, item_discount_all_items, delete_whole_last_item, void_paid_check, receipt_preview, add_item_by_code, sell_coupon, new_item_no_kitchen_slip, stock_soldout, direct_report, adjust_tips, open_first_drawer, open_second_drawer, search_item, delete_multiple_item, park_order_by_station, retrieve_park_order_by_station, preorder_by_table,
		user_time_in_out, pms_enquiry, octopus_read_card, octopus_add_value, octopus_reconnect, octopus_generate_trans_file, cashier_settlement, restart_pms_shell, set_menu_replace_item, repeat_item, repeat_multiple_items, check_smart_card, split_table, stock_balance_chg, split_check_by_options, check_discount, void_check_discount, adjust_payments, stop_pms_shell, paid_in, paid_out, drop, retrieve_ordered_items_by_resv_confirm_no, toggle_self_order_kiosk_mode, set_call_number, insert_item, table_message_reminder, add_edit_check_info, manual_change_price_level, restart_payment_interface_shell, stop_payment_interface_shell, restart_auto_station, check_extra_charge, void_check_extra_charge, set_other_taiwan_gui_type, partial_send_check, reprint_last_taiwan_gui_receipt, reprint_taiwan_gui_receipt, set_taiwan_gui_next_trans_num, print_paid_check, select_pending_item, print_pending_item, reprint_guest_check, svc_enquiry, change_ordered_item_price_level, multiple_rush_order, override_condition_activation, change_password, search_panel_hot_item, svc_coupon_redeem_item, loyalty_member_bonus_redemption, print_past_date_check, item_cover_discount, check_cover_discount, assign_employee_card_number, pms_redeem_package, park_order_by_outlet, retrieve_park_order_by_outlet, membership_voucher_redempt, 
		membership_registration, print_detail_check, search_item_by_sku, toggle_ordering_basket_information, input_taiwan_gui_carrier, refund_item, split_item_by_seat, toggle_taiwan_gui_mode, edit_taiwan_gui_configure, assign_check_to_target_outlet, assign_check_type, transfer_check_to_target_outlet, ogs_get_preorder, add_advance_order, retrieve_advance_order, assign_default_payment, restart_portal_station, change_last_item_qty_by_electronic_scale, loyalty_start_redeem, loyalty_check_balance, loyalty_cancel_redeem, loyalty_search_member, loyalty_svc_add_value, loyalty_svc_issue_card, loyalty_svc_check_value, loyalty_svc_suspend_card, loyalty_svc_transfer_card, add_deposit, use_deposit, assign_table_attributes, payment_interface_card_authorization, payment_interface_cancel_card_authorization, payment_interface_transfer_card_authorization, payment_interface_cancel_complete_card_authorization, edit_check_gratuity, assign_drawer_ownership, take_drawer_ownership, change_meal_period, disable_mix_and_match_rules, search_advance_order, scanpay_status_enquiry, confirm_order_dialog, assign_ordering_type, edit_set_menu, mark_table_status_to_vacant, wastage_operation, search_item_stock, refresh_table_status_by_table_num, check_listing_by_table_reference, override_item_price, input_table_reference, 
		toggle_bar_tab_mode, set_minimum_charge, set_maximum_charge, payment_interface_card_authorization_and_send_check, repeat_round_items, void_payment, split_revenue, membership_issue_card, patron_inquiry, comp_inquiry, gaming_enquiry, duty_meal_on_credit_balance_enquiry, split_item_with_quantity, stock_soldout_by_shop, check_data_update_history, restart_surveillance_shell, stop_surveillance_shell, toggle_print_receipt, toggle_cover, own_tips_and_sc_out, tips_and_sc_out_from_other_employee, get_award_list, bonus_codes, membership_affiliation, set_closed_check_member, set_member_discount, search_membership_member, warning_message_list, toggle_ordering_basket_with_consolidate_items, lock_table, print_pending_item_with_quantity, first_page, display_information,add_reference_to_last_item};
	
	public enum OPERATION_MODE{fine_dining, fast_food, stock_delivery, self_order_kiosk, bar_tab};

	public static String FAST_FOOD_TABLE_EXTENSION = "#";
	public static String STOCK_INVOICE_TABLE_EXTENSION = "$";
	public static String BAR_TAB_TABLE_EXTENSION = "^";

	public enum DISPLAY_MODE{no_display, horizontal_desktop, vertical_mobile};
	
	public enum AUTO_FUNCTIONS_RESULT_LIST{success, fail, forced_quit};
	
	public static char TABLE_EXTENSION_START_LETTER = 'A';
	public static char TABLE_EXTENSION_END_LETTER = 'Z';

	public enum OPTIONAL_MODULE{member,signage,pos_interface,reservation, failover, woh};

	public static int LANGUAGE_COUNT = 5;

	// Version no.
	public static String g_sVersion = "1.0.24.0 Rev5710";
	public static String g_sHeroPlatformVersion = "1.2.24.0";
	// Release date
	public static String g_sReleaseDate = "20190225";

	// Max seat no
	public static int MAX_SEATS = 99;

	// SC / Tax count
	public static int SC_COUNT = 5;
	public static int TAX_COUNT = 25;
	public static int INCL_TAX_COUNT = 4;
	
	// Log level : 0 - Normal log; > 0 - More debug log
	public static int g_iLogLevel = 0;
	public static boolean g_bWriteClientConnectionLog = false;

	public static ThreadLocal<VirtualUITerm> g_oTerm;

	public static ThreadLocal<TCPLib> g_oTCP;
	public static ThreadLocal<Selector> g_oSelectorForTCP;
	public static ThreadLocal<SelectionKey> g_oSelectorKeyForTCP;

	public static ThreadLocal<Integer> g_oCurrentLangIndex;
	public static ThreadLocal<String> g_sDisplayMode;
	public static List<HashMap<String, String>> g_oSupportedLangList;
	public static String g_sSystemDataPath = ".";
	public static String g_sSystemDataUrl = "";
	public static String g_sMasterServerUrl = "";
	public static String g_sMasterServerAccount = "";
	public static String g_sLicenseCert = "";

	// Log level
	public static final String LOG_LEVEL_OFF	= "OFF";
	public static final String LOG_LEVEL_FATAL	= "FATAL";
	public static final String LOG_LEVEL_ERROR	= "ERROR";
	public static final String LOG_LEVEL_WARN	= "WARN";
	public static final String LOG_LEVEL_INFO	= "INFO";
	public static final String LOG_LEVEL_DEBUG	= "DEBUG";
	public static final String LOG_LEVEL_TRACE	= "TRACE";
	public static final String LOG_LEVEL_ALL	= "ALL";
	
	// Log format
	public static final String LOG_FORMAT_POS	= "POS";
	public static final String LOG_FORMAT_OM	= "OM";
	
	// Global structure
	public static ThreadLocal<FuncStation> g_oFuncStation;
	public static ThreadLocal<FuncOutlet> g_oFuncOutlet;
	public static ThreadLocal<FuncUser> g_oFuncUser;
	public static ThreadLocal<FuncActionLog> g_oActionLog;
	public static ThreadLocal<FuncMenu> g_oFuncMenu;
	public static ThreadLocal<FuncMixAndMatch> g_oFuncMixAndMatch;
	public static ThreadLocal<FuncOctopus> g_oFuncOctopus;
	public static ThreadLocal<FuncSignage> g_oFuncSignage;
	
	public static ConcurrentHashMap<Integer, FuncOverride> g_oFuncOverrideList = new ConcurrentHashMap<Integer, FuncOverride>();
	public static ConcurrentHashMap<Integer, FuncDiscountAcl> g_oFuncDiscountAclList = new ConcurrentHashMap<Integer, FuncDiscountAcl>();
	
	public static ConcurrentHashMap<Integer, HashMap<String, String>> g_oInterfaceSessions = new ConcurrentHashMap<Integer, HashMap<String, String>>();
	
	// Language object
	public static ThreadLocal<LangResource> g_oLang;

	// Device manager connection element
	public static ThreadLocal<VirtualUIBasicElement> g_oDeviceManagerElement;

	// Station thread ID and station information list (share among all child thread)
	public static HashMap<Long, ClsActiveClient> g_lCurrentConnectClientList = new HashMap<Long, ClsActiveClient>();

	// Kill station and reason (share among all child thread)
	private static ConcurrentHashMap<Integer, String> g_oKilledStationIDAndReason = new ConcurrentHashMap<Integer, String>();

	// Flag during daily start / daily close / reload business setting
	private static ConcurrentHashMap<Integer, DateTime> g_oDailyOperationOutletList = new ConcurrentHashMap<Integer, DateTime>();

	// Store the last resume login id, password and card no. for each station
	public static HashMap<Long, String> g_oReconnectId = new HashMap<Long, String>();
	public static HashMap<Long, String> g_oReconnectPassword = new HashMap<Long, String>();
	public static HashMap<Long, String> g_oReconnectCardNo = new HashMap<Long, String>();
	
	// Store active auto and portal station list, for web daily start and close to kill and launch auto and portal station
	public static ConcurrentHashMap<Integer, String> g_oAutoPortalStationList = new ConcurrentHashMap<Integer, String>();
	
	// Store tender amount
	public static List<BigDecimal> g_oTenderAmountList = new ArrayList<BigDecimal>();

	// Support module flag
	private static boolean g_bSupportMember = false;
	private static boolean g_bSupportSignage = false;
	private static boolean g_bSupportPosInterface = false;
	private static boolean g_bSupportReservation = false;
	private static boolean g_bSupportFailover = false;
	private static boolean g_bSupportWoh = false;

	public static ThreadLocal<HashMap<String, HashMap<String, PosConfig>>> g_oPosConfigList;

	// Skip stock calculation
	public static boolean g_bNotCheckStock = false;

	// Background schedule jobs
	private static ScheduledExecutorService g_oScheduledService = Executors.newScheduledThreadPool(50);
	public static HashMap<String, AppBackgroundScheduleJob> g_oScheduledJobList = new HashMap<String, AppBackgroundScheduleJob>();
	public static HashMap<Integer, PosOutletTableList> g_oOutletTableList = new HashMap<Integer, PosOutletTableList>();

	// Check list for check payment result through payment interface
	public static HashMap<Integer, List<HashMap<String, String>>> g_oCheckListForPaymentInterface = new HashMap<Integer, List<HashMap<String, String>>>();

	public static HashMap<Integer, List<FuncTMS>> g_oCheckListForTmsInterface = new HashMap<Integer, List<FuncTMS>>();

	public static HashMap<Integer, HashMap<String, String>> g_bPMS4700SerialPortLocking = new HashMap<Integer, HashMap<String, String>>();

	public static HashMap<Integer, HashMap<String, String>> g_b2700SerialPortLocking = new HashMap<Integer, HashMap<String, String>>();

	public static HashMap<Integer, HashMap<String, DateTime>> g_oOverCookingTimeTableList = new HashMap<Integer, HashMap<String, DateTime>>();

	public static HashMap<Integer, HashMap<String, DateTime>> g_oTableStatusCleaningList = new HashMap<Integer, HashMap<String, DateTime>>();

	public static HashMap<Integer, FuncSurveillance> g_oListForSurveillanceInterface = new HashMap<Integer, FuncSurveillance>();
	
	// Debug flag
	public static int g_iDebugMode = 0;		// 0 - no debug flow; 1 - auto order, payment

	// Store the interface config list
	public static ThreadLocal<PosInterfaceConfigList> g_oPosInterfaceConfigList;
	
	// Store the interface config list
	public static ThreadLocal<WohAwardSettingList> g_oWohAwardSettingList;

	// Store the item remind list
	public static ThreadLocal<PosItemRemindRuleList> g_oPosItemRemindRuleList;
	
	// Store the interface config list
	public static ClsAlertMessageList g_oAlertMessageList = new ClsAlertMessageList();

	// License control model
	public static PosLicenseControlModel g_oPosLicenseControlModel = new PosLicenseControlModel();

	// License control model for HQ Loyalty
	public static PosLicenseControlModel g_oPosLicenseControlModelForHq = new PosLicenseControlModel();

	// Generate unique UI id
	public static AtomicInteger g_oUIElementGenerator = new AtomicInteger();

	// Portal station processing job
	public static List<String> g_oProtalProcessingJobs = new ArrayList<String>();

	// Result from auto-function
	public static ThreadLocal<String> g_sResultForAutoFunction;

	// HERO Workstation 2.0
	public static FuncSmartStation g_oFuncSmartStation = new FuncSmartStation();
	
	// MQ manager
	public static MQCommandManager g_oMQCommandManager;

	public static boolean g_bConnectSuccess = true;

	public static GeneralStyleMap g_oStyleMap;
	
	// Menu price level list
	public static MenuPriceLevelList g_oMenuPriceLevelList;
	
	// Pos Station List
	public static PosStationList g_oPosStationList;
	
	// Alert Message Element Id (e.g. print queue Id, item department Id, ...) map to Station Id setting table
	// transpose database (pos_station_alert_settings) into new relationship between alert message element and station
	public static HashMap<String, HashMap<Integer, ArrayList<Integer>>> g_oAlertMessageElementIdToStationIdsSettingTable = new HashMap<String, HashMap<Integer, ArrayList<Integer>>>();
	
	// Station id monitor print queues list for alert message
	public static HashMap<Integer, ArrayList<Integer>> g_oAlertMessageStationIdToPrintQueueIdsTable = new HashMap<Integer, ArrayList<Integer>>();
	
	// List for reading all print queue information
	public static HashMap<Integer, PrtPrintQueue> g_oPrintQueueList = new HashMap<Integer, PrtPrintQueue>();

	// For performance checking
	private static long g_lLastCurrentTimeinMillis = 0;
	private static String g_sLastPerformanceLog = "";
	public static long g_lExceedMillisShowPerformanceLog = 20;
	
	// Start daily operation, kill all other stations among the same outlet
	public static void startOutletOperationKillOtherStations(int iOutletId, String sReason){
		// Store the outlet ID which is doing the operation and the current time
		if (g_oDailyOperationOutletList.containsKey(iOutletId) == false)
			g_oDailyOperationOutletList.put(iOutletId, (AppGlobal.getCurrentTime(false)));

		if (g_oMQCommandManager == null) {
			for(Map.Entry<Long, ClsActiveClient>entry:AppGlobal.g_lCurrentConnectClientList.entrySet()){
				ClsActiveClient oActiveClient = entry.getValue();
				if(oActiveClient.getCurrentOutletId() == iOutletId && oActiveClient.getCurrentStationId() != AppGlobal.g_oFuncStation.get().getStationId()){
					startKillSingleStation(oActiveClient.getCurrentStationId(), sReason);
					if(oActiveClient.isPortalStation() || oActiveClient.isAutoStation())
						removeStationFromAutoAndPortalStationList(oActiveClient.getCurrentStationId());
				}
			}
		} else {
			// Fire command through MQ
			g_oMQCommandManager.fireKillOtherStationsCommand(AppGlobal.g_oFuncStation.get().getStationId(), sReason);
		}
	}

	// End daily operation
	public static void endOutletOperation(int iOutletId) {
		if (g_oDailyOperationOutletList.containsKey(iOutletId))
			g_oDailyOperationOutletList.remove(iOutletId);
	}

	// Create if there is daily operation running
	public static boolean checkDailyOperationRunning(int iOutletId) {
		if (g_oDailyOperationOutletList.containsKey(iOutletId)) {
			DateTime oOperationTime = g_oDailyOperationOutletList.get(iOutletId);
			if ((((new DateTime()).getMillis() - oOperationTime.getMillis())) > (120 * 1000)) {
				// The operation time is 2 min before, should be expired
				// So no daily operation is running
				return false;
			} else {
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
		if(g_oKilledStationIDAndReason.containsKey(iStationId) == false) {
			if (g_oMQCommandManager == null) {
				g_oKilledStationIDAndReason.put(iStationId, sReason);
			} else {
				// Fire command through MQ
				g_oMQCommandManager.fireKillSingleStationCommand(iStationId, sReason);
			}
		}
	}
	
	// Restart Auto Station
	public static void startRestartAutoStation(String sUDID){
		// Fire command through MQ
		g_oMQCommandManager.fireRestartAutoStationCommand(sUDID);
	}
	
	// Restart Portal Station
	public static void startRestartPortalStation(String sUDID){
		// Fire command through MQ
		g_oMQCommandManager.fireRestartPortalStationCommand(sUDID);
	}
	
	// Kill station from MQ command
	// iType :	1 - Kill station with ID = iStationId
	//			2 - Kill all stations except station with ID = iStationId
	public static void killStationByMQCommand(int iStationId, String sReason, int iType) {
		// Check if the station is connected to this POS server
		for(Map.Entry<Long, ClsActiveClient>entry:AppGlobal.g_lCurrentConnectClientList.entrySet()){
			ClsActiveClient oActiveClient = entry.getValue();
			boolean bKill = false;
			if (oActiveClient.getCurrentStationId() == iStationId) {
				// Station found
				if (iType == 1)
					bKill = true;
			} else {
				// Not target station
				if (iType == 2)
					bKill = true;
			}
			
			if (bKill) {
				if(g_oKilledStationIDAndReason.containsKey(oActiveClient.getCurrentStationId()) == false) {
					g_oKilledStationIDAndReason.put(oActiveClient.getCurrentStationId(), sReason);
				}
			}
		}
	}
	
	// Restart Portal Station
	public static void pmsShellOperation(int iInterfaceId, String sVendorKey, String sFunction){
		// Fire command through MQ
		g_oMQCommandManager.firePmsShellOperationCommand(iInterfaceId, sVendorKey, sFunction);
	}
	
	// Check if myself is killed or not
	public static String getKilledReason(){
		if(AppGlobal.g_oFuncStation == null || AppGlobal.g_oFuncStation.get() == null)
			return "";

		int iStationId = AppGlobal.g_oFuncStation.get().getStationId();
		if (g_oKilledStationIDAndReason.containsKey(iStationId)) {
			// Return the reason
			return g_oKilledStationIDAndReason.get(iStationId);
		} else
			return "";
	}

	// Finish being killed
	public static void finishBeingKilled() {
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
	
	public static void setWohAwardSettingList(WohAwardSettingList oWohAwardSettingList) {
		g_oWohAwardSettingList.set(oWohAwardSettingList);
	}
	
	public static List<PosInterfaceConfig> getPosInterfaceConfigByInfType(String sInfType) {
		return g_oPosInterfaceConfigList.get().getInterfaceConfigListByInterfaceType(sInfType);
	}
	
	public static PosInterfaceConfig getPosInterfaceConfigById(int iIntfId){
		return g_oPosInterfaceConfigList.get().getInterfaceConfigListById(iIntfId);
	}
	
	public static void setPosItemRemindRuleList(PosItemRemindRuleList oPosItemRemindRuleList) {
		g_oPosItemRemindRuleList.set(oPosItemRemindRuleList);
	}
	
	public static List<PosItemRemindRule> getItemRemindRuleList() {
		return g_oPosItemRemindRuleList.get().getItemRemindRuleList();
	}
	
	public static void writeLauncherLog(String sClass, String sMethod, String sLog) {
		write2LogFile("hero_launcher", "[" + sClass + ":" + sMethod + "] " + sLog);
	}
	
	public static void writeActionLog(String sStationID, String sLogin, String sLog) {
		String fileName = "hero_app";
		if (sStationID != null && !sStationID.isEmpty())
			fileName += "_" + sStationID;
		//write2LogFile(fileName, "[s:" + sStationID + " u:" + sLogin + "] " + sLog);
		write2LogFile(fileName, sStationID, sLogin, LOG_FORMAT_POS, LOG_LEVEL_INFO, sLog);
	}
	
	public static void writeOMErrorLog(String sClass, String sLogin, String sLog) {
		//write2LogFile("hero_om_err", "[u:" + sLogin + "] " + sLog + "\r\n" + getStackTrace());
		write2LogFile("hero_om_err", "", sLogin, LOG_FORMAT_OM, LOG_LEVEL_ERROR, sLog + "\r\n" + getStackTrace());
	}
	
	//	For compatible
	public static void writeErrorLog(String sClass, String sMethod, String sStationID, String sLogin, String sLog){
		//writeErrorLog(sClass, sStationID, sLogin, sLog);
		write2LogFile("hero_app_err", sStationID, sLogin, LOG_FORMAT_POS, LOG_LEVEL_ERROR, sLog + "\r\n" + getStackTrace());
	}
	
	/*public static void writeErrorLog(String sClass, String sStationID, String sLogin, String sLog) {
		String fileName = "hero_app_err";
		if (sStationID != null && !sStationID.isEmpty())
			fileName += "_" + sStationID;
		write2LogFile(fileName, "[s:" + sStationID + " u:" + sLogin + "] " + sLog + "\r\n" + getStackTrace());
	}*/
	
	public static void writeDebugLog(String sClass, String sMethod, String sLog){
		//write2LogFile("hero_app_debug", "[" + sClass + ":" + sMethod + ":" + Thread.currentThread().getId() + "] " + sLog);
		write2LogFile("hero_app_debug", LOG_FORMAT_POS, LOG_LEVEL_ERROR, "[" + sClass + ":" + sMethod + ":" + Thread.currentThread().getId() + "] " + sLog);
	}
	
	public static void stack2Log(Exception e) {
		write2LogFile("hero_app_exception", stackToString(e));
	}
	
	private static String getStackTrace() {
		StringWriter sw = new StringWriter();
		new Throwable("").printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
	
	public static boolean write2LogFile(String sFileName, String sContent) {
		Date today = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		dateFormat.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
		String sCurrentTime = dateFormat.format(today);
		SimpleDateFormat monthFormat = new SimpleDateFormat("yyyyMM");
		String sCurrentYearMonth = monthFormat.format(today);
		
		try {
			String sFilename = "log/" + sCurrentYearMonth + "/" + sFileName + ".txt";
			File file = new File(sFilename);
			file.getParentFile().mkdirs();
			
			//	Write to file
			FileWriter fstream = new FileWriter(file, true);
			fstream.write(sCurrentTime + " " + sContent + "\r\n");
			if (ERROR_TO_CONSOLE)
				System.out.println(sCurrentTime + " " + sContent);
			fstream.close();
		}
		catch (Exception e) {
			//	Catch exception if any
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public static boolean write2LogFile(String sFileName, String sStationID, String sLogin, String sFormat, String sLogLevel, String sContent) {
		Date today = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
		String sCurrentTime = dateFormat.format(today);
		SimpleDateFormat monthFormat = new SimpleDateFormat("yyyyMM");
		String sCurrentYearMonth = monthFormat.format(today);
		String sLogContent = "[" + g_sHeroPlatformVersion + "]-[" + g_sMasterServerAccount + "]-[";
		
		if(g_oFuncOutlet != null && g_oFuncOutlet.get() != null && g_oFuncOutlet.get().getShop() != null)
			sLogContent = sLogContent.concat(g_oFuncOutlet.get().getShop().getShopName(1));
		sLogContent = sLogContent.concat("]-[");
		
		try {
			String sFilename = "log/" + sCurrentYearMonth + "/" + sFileName + ".txt";
			File file = new File(sFilename);
			file.getParentFile().mkdirs();
			
			//	Write to file
			FileWriter fstream = new FileWriter(file, true);
			
			if(sFormat.equals(LOG_FORMAT_POS)){
				// Write the outlet name
				if(g_oFuncOutlet != null && g_oFuncOutlet.get() != null)
					sLogContent = sLogContent.concat(g_oFuncOutlet.get().getOutletNameByIndex(1));
				sLogContent = sLogContent.concat("]-[");
				
				// Write the station name
				if(g_oFuncStation != null && g_oFuncStation.get() != null)
					sLogContent = sLogContent.concat(g_oFuncStation.get().getName(1));
				sLogContent = sLogContent.concat("]-[");
				
				// Write the request IP
				if(sStationID != null)
					sLogContent = sLogContent.concat(sStationID);
				sLogContent = sLogContent.concat("]-[");
			} else if (sFormat.equals(LOG_FORMAT_OM)){
				// Write the source
				sLogContent = sLogContent.concat("]-[]-[]-[");
			}
			
			// Write the User ID
			if(sLogin != null)
				sLogContent = sLogContent.concat(sLogin);
			sLogContent = sLogContent.concat("] ");
			
			// Write the content
			sLogContent = sLogContent.concat(sContent);
			fstream.write(sCurrentTime + " " + sLogLevel + ": " + sLogContent + "\r\n");
			if (ERROR_TO_CONSOLE)
				System.out.println(sCurrentTime + " [s:" + sStationID + " u:" + sLogin + "] " + sContent);
			fstream.close();
		}
		catch (Exception e) {
			//	Catch exception if any
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static boolean write2LogFile(String sFileName, String sFormat, String sLogLevel, String sContent) {
		return write2LogFile(sFileName, Integer.toString(g_oFuncStation.get().getStationId()), 
				Integer.toString(g_oFuncUser.get().getUserId()), sFormat, sLogLevel, sContent);
	}
	
	public static void writePerformanceLog(String sContent) {
		long lCurrTime = System.currentTimeMillis();
		
		if (g_lLastCurrentTimeinMillis == 0) {
			g_lLastCurrentTimeinMillis = lCurrTime;
			return;
		}
		
		long lTimeDiff = lCurrTime - g_lLastCurrentTimeinMillis;

		if (lTimeDiff >= g_lExceedMillisShowPerformanceLog) {
			String sLogContent = "PLOG - Exceed "
					+ g_lExceedMillisShowPerformanceLog
					+ " ["
					+ StringLib.IntToStringWithLeadingZero((int) Thread
							.currentThread().getId(), 10) + "] "
					+ StringLib.IntToStringWithLeadingZero((int) lTimeDiff, 10)
					+ " : " + g_sLastPerformanceLog + " >> " + sContent;
			
			write2LogFile("hero_app_performance", sLogContent);
		}
		
		g_sLastPerformanceLog = sContent;

		g_lLastCurrentTimeinMillis = lCurrTime;
	}

	public static String stackToString(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	// Determine if the module is supported or not during start up
	public static void checkModuleExisting() {
		// Get the member module
		SystemModule oSystemModule = new SystemModule();
		if (oSystemModule.readByAlias(AppGlobal.OPTIONAL_MODULE.member.name()))
			// Support member module
			g_bSupportMember = true;
		else
			// Not support member module
			g_bSupportMember = false;

		if (oSystemModule.readByAlias(AppGlobal.OPTIONAL_MODULE.signage.name()))
			g_bSupportSignage = true; // Support signage module
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
		
		if(oSystemModule.readByAlias(AppGlobal.OPTIONAL_MODULE.failover.name()))
			g_bSupportFailover = true;	// Support failover module
		else
			g_bSupportFailover = false;	// Not support failover module
		
		if(oSystemModule.readByAlias(AppGlobal.OPTIONAL_MODULE.woh.name()))
			g_bSupportWoh = true;	// Support woh module
		else
			g_bSupportWoh = false;	// Not support woh module
	}
	
	// Determine if the module is supported and get system config during start up
	public static void checkModulesExistingAndGetSystemConfig() {
		// Get the member module
		//SystemConfigList oSystemConfigList = new SystemConfigList();
		SystemGeneral oSystemGeneral = new SystemGeneral();
		ArrayList<String> oModuleAlias = new ArrayList<String> ();
		ArrayList<String> oSystemConfigVariableList = new ArrayList<String> ();
		
		HashMap<String, ArrayList<String>> oConfigVariables = new HashMap<String, ArrayList<String>>();
		
		oModuleAlias.add(AppGlobal.OPTIONAL_MODULE.member.name());
		oModuleAlias.add(AppGlobal.OPTIONAL_MODULE.signage.name());
		oModuleAlias.add(AppGlobal.OPTIONAL_MODULE.pos_interface.name());
		oModuleAlias.add(AppGlobal.OPTIONAL_MODULE.reservation.name());
		oModuleAlias.add(AppGlobal.OPTIONAL_MODULE.failover.name());
		oModuleAlias.add(AppGlobal.OPTIONAL_MODULE.woh.name());
		oSystemConfigVariableList.add("master_server_url");
		oSystemConfigVariableList.add("data_path");
		oSystemConfigVariableList.add("data_url");
		oSystemConfigVariableList.add("language_code");
		
		oConfigVariables.put("system", oSystemConfigVariableList);
		
		oSystemGeneral.readConfigVariablesAndModuleAlias(oConfigVariables, oModuleAlias);
		g_oSupportedLangList = new ArrayList<HashMap<String, String>>();
		if(oSystemGeneral.getSystemConfigBySectionAndVariable("system", "master_server_url") != null){
			g_sMasterServerUrl = oSystemGeneral.getSystemConfigBySectionAndVariable("system", "master_server_url").getValue();
			
			//Set the Master Server Account
			String[] sTemp = null;
			sTemp = AppGlobal.g_sMasterServerUrl.split("/");
			g_sMasterServerAccount = sTemp[sTemp.length - 1];
		}
		if(oSystemGeneral.getSystemConfigBySectionAndVariable("system", "data_path") != null)
			g_sSystemDataPath = oSystemGeneral.getSystemConfigBySectionAndVariable("system", "data_path").getValue();
		if(oSystemGeneral.getSystemConfigBySectionAndVariable("system", "data_url") != null)
			g_sSystemDataUrl = oSystemGeneral.getSystemConfigBySectionAndVariable("system", "data_url").getValue();
		for(SystemConfig oSystemConfig:oSystemGeneral.getSystemConfigList()) {
			if(oSystemConfig.getSection().equals("system") && oSystemConfig.getVariable().equals("language_code") && oSystemConfig.getValue() != null) {
				if(!oSystemConfig.getValue().isEmpty()) {
					try {
						HashMap<String, String> oTempLangInfo = new HashMap<String, String>();
						JSONObject tempJSONObject = new JSONObject(oSystemConfig.getValue());
						oTempLangInfo.put("index", String.valueOf(oSystemConfig.getIndex()));
						oTempLangInfo.put("name", tempJSONObject.getString("name"));
						oTempLangInfo.put("code", tempJSONObject.getString("code"));
						oTempLangInfo.put("url", tempJSONObject.getString("url"));
						g_oSupportedLangList.add(oTempLangInfo);
					} catch (JSONException jsone) {
						AppGlobal.stack2Log(jsone);
					}
				}
			}
		}
		// set the language
		String langCode = "en";
		int langIndex = 1;
		HashMap<Integer, String> oLangCodeList = new HashMap<Integer, String>();
		for (HashMap<String, String> oLangInfo : g_oSupportedLangList) {
			oLangCodeList.put(Integer.parseInt(oLangInfo.get("index")), oLangInfo.get("code"));

			if (Integer.parseInt(oLangInfo.get("index")) == g_oCurrentLangIndex.get()) {
				langIndex = Integer.parseInt(oLangInfo.get("index"));
				langCode = oLangInfo.get("code");
			}
		}
		g_oCurrentLangIndex.set(new Integer(langIndex));
		g_oLang.get().init(oLangCodeList);
		g_oLang.get().switchLocale(langCode);
		if (g_oTerm.get() != null)
			g_oTerm.get().changeLanguage(langIndex - 1);

		// check module is supported or not
		if(oSystemGeneral != null && !oSystemGeneral.getSystemModuleList().isEmpty()) {
			for (SystemModule oSystemModule : oSystemGeneral.getSystemModuleList()) {
				if(oSystemModule.getAlias().equals("member"))
					g_bSupportMember = true;
				else if(oSystemModule.getAlias().equals("signage"))
					g_bSupportSignage = true;
				else if(oSystemModule.getAlias().equals("interface"))
					g_bSupportPosInterface = true;
				else if(oSystemModule.getAlias().equals("reservation"))
					g_bSupportReservation = true;
				else if(oSystemModule.getAlias().equals("failover"))
					g_bSupportFailover = true;
				else if(oSystemModule.getAlias().equals("woh"))
					g_bSupportWoh = true;
			}
		}
	}
	
	public static void checkBusinessday(String sStationType) {
		FuncOutlet oTempFunOutlet = new FuncOutlet();
		// no running business day is found, kill the station
		if (!oTempFunOutlet.loadBusinessDay(AppGlobal.g_oFuncOutlet.get().getOutletId())) {
			if (sStationType.equals(PosStationDevice.KEY_PORTAL_STATION))
				AppGlobal.stopPortalStation(AppGlobal.g_oFuncOutlet.get().getOutletId());
			else
				AppGlobal.stopAutoStation(AppGlobal.g_oFuncOutlet.get().getOutletId());
		}
	}
	
	public static void addStationToAutoAndPortalStationList(int iStationId, String sBdayId){
		if (sBdayId.isEmpty())
			return;
		
		AppGlobal.g_oAutoPortalStationList.put(iStationId, sBdayId);
	}
	
	public static void removeStationFromAutoAndPortalStationList(int iStationId) {
		AppGlobal.g_oAutoPortalStationList.remove(iStationId);
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
		else if(sModule.equals(AppGlobal.OPTIONAL_MODULE.failover.name())) {
			if(g_bSupportFailover)
				bRet = true;
		}
		else if(sModule.equals(AppGlobal.OPTIONAL_MODULE.woh.name())) {
			if(g_bSupportWoh)
				bRet = true;
		}
		
		return bRet;
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
		ScheduledFuture<?> oScheduledFuture = g_oScheduledService.scheduleWithFixedDelay(oBackgroundJob, 0, iPeriod, TimeUnit.MILLISECONDS);
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
	
	synchronized public static void removePrintedCheckToPaymentInterfaceCheckList(int iOutletId, String sCheckId,String sOutTradeNumber) {
		if(!g_oCheckListForPaymentInterface.containsKey(iOutletId))
			return;
		
		List<HashMap<String, String>> oPrintedCheckList = g_oCheckListForPaymentInterface.get(iOutletId);
		if(oPrintedCheckList.isEmpty())
			return;
		
		int iTargetIndex = -1;
		boolean bBreak = false;
		//remove the item with null outTradeNumber
		do {
			iTargetIndex = -1;
			for(int i=0; i<oPrintedCheckList.size(); i++) {
				HashMap<String, String> oPrintedCheckInfo = oPrintedCheckList.get(i);
				if(oPrintedCheckInfo.containsKey("outTradeNumber") && oPrintedCheckInfo.get("outTradeNumber") != null) {
					iTargetIndex = i;
					break;
				}
			}
			
			if(iTargetIndex >= 0)
				oPrintedCheckList.remove(iTargetIndex);
			else
				bBreak = true;
		}while(!bBreak);
		
		//find the target outTradeNumber and remove it
		iTargetIndex = -1;
		for(int i=0; i<oPrintedCheckList.size(); i++) {
			HashMap<String, String> oPrintedCheckInfo = oPrintedCheckList.get(i);
			if(oPrintedCheckInfo.containsKey("checkId") && oPrintedCheckInfo.get("checkId") == sCheckId && oPrintedCheckInfo.containsKey("outTradeNumber") && oPrintedCheckInfo.get("outTradeNumber") != null && oPrintedCheckInfo.get("outTradeNumber").equals(sOutTradeNumber)) {
				iTargetIndex = i;
				break;
			}
		}
		
		if(iTargetIndex >= 0)
			oPrintedCheckList.remove(iTargetIndex);
	}
	
	synchronized public static void addCheckToTmsInterfaceCheckList(int iOutletId, FuncTMS oFuncTMS) {
		if (!g_oCheckListForTmsInterface.containsKey(iOutletId))
			g_oCheckListForTmsInterface.put(iOutletId, new ArrayList<FuncTMS>());
		g_oCheckListForTmsInterface.get(iOutletId).add(oFuncTMS);
	}
	
	synchronized public static void removeTmsInfoFromTmsInterfaceInfoList(int iOutletId, String sCheckId) {
		if (!g_oCheckListForTmsInterface.containsKey(iOutletId))
			return;
		
		List<FuncTMS> oFuncTMSList = g_oCheckListForTmsInterface.get(iOutletId);
		if (oFuncTMSList.isEmpty())
			return;
		
		int iTargetIndex = -1;
		for (int i = 0; i < oFuncTMSList.size(); i++) {
			FuncTMS oFuncTMS = oFuncTMSList.get(i);
			if(oFuncTMS.getCheckId().equals(sCheckId)) {
				iTargetIndex = i;
				break;
			}
		}
		
		if (iTargetIndex >= 0)
			g_oCheckListForTmsInterface.get(iOutletId).remove(iTargetIndex);
	}
	
	synchronized public static FuncTMS getTmsInfoFromTmsInterfaceInfoList(int iOutletId, String sCheckId) {
		if (!g_oCheckListForTmsInterface.containsKey(iOutletId))
			return null;
		
		List<FuncTMS> oFuncTMSList = g_oCheckListForTmsInterface.get(iOutletId);
		if (oFuncTMSList.isEmpty())
			return null;
		
		FuncTMS oTargetFuncTms = null;
		for (int i = 0; i < oFuncTMSList.size(); i++) {
			FuncTMS oFuncTMS = oFuncTMSList.get(i);
			if(oFuncTMS.getCheckId().equals(sCheckId)) {
				oTargetFuncTms = oFuncTMS;
				break;
			}
		}		
		return oTargetFuncTms;
	}
	
	public static ClsActiveClient getActiveClient(){
		if(g_lCurrentConnectClientList.containsKey(Thread.currentThread().getId())){
			return g_lCurrentConnectClientList.get(Thread.currentThread().getId());
		}else{
			return null;
		}
	}
	
	public static ClsActiveClient getActiveClient(String sUDID) {
		for(Map.Entry<Long, ClsActiveClient>entry:AppGlobal.g_lCurrentConnectClientList.entrySet()){
			ClsActiveClient oActiveClient = entry.getValue();
			if(sUDID.equals(oActiveClient.getUDID())){
				// Match address
				return oActiveClient;
			}
		}
		
		// Not found
		return null;
	}
	
	synchronized public static boolean isPrintedCheckListExist(int iOutletId) {
		if(!g_oCheckListForPaymentInterface.containsKey(iOutletId))
			return false;
		
		List<HashMap<String, String>> oPrintedCheckList = g_oCheckListForPaymentInterface.get(iOutletId);
		if(oPrintedCheckList == null)
			return false;
		
		return true;
	}

	public static String getLoginErrorMessage(String sErrorMessageFromAPI) {
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

	public static String getLicenseErrorMessage(int iStationId) {
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
					AppGlobal.g_oLang.get()._("shiji_information_technology_kong_kong_limited_to_renew_the_subscription") + "\n" + 
					AppGlobal.g_oLang.get()._("you_cannot_use_the_system_if_you_do_not_renew") + "\n" + 
					AppGlobal.g_oLang.get()._("the_subscription_before_the_expiry_date");
		}
		
		return sWarningMessage;
	}
	
	// Add the portal station send check handling job
	synchronized public static boolean addProtalHandlingJob(String sSendingContent) {
		boolean bNotContentFound = true;
		
		for(String sHandlingContent : g_oProtalProcessingJobs) {
			if(sHandlingContent.equals(sSendingContent)) {
				bNotContentFound = false;
				break;
			}
		}
		
		if(bNotContentFound) 
			g_oProtalProcessingJobs.add(sSendingContent);
		
		return bNotContentFound;
	}
	
	// Remove the portal station send check job if it is already passing 5 mins
	synchronized public static void removeTimeoutProtalHandlingJob() {
		if(g_oProtalProcessingJobs.size() == 0)
			return;
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		for(int i=(g_oProtalProcessingJobs.size()-1); i>=0; i--) {
			try {
				JSONObject oTempObject = new JSONObject(g_oProtalProcessingJobs.get(i));
				String sOpenTime = oTempObject.optString("chks_open_loctime", "");
				if(sOpenTime.isEmpty())
					continue;
				
				DateTime oOpenTime = formatter.parseDateTime(sOpenTime);
				DateTime oCurrentTime = AppGlobal.getCurrentTime(false);
				
				if(oOpenTime.isBefore(oCurrentTime)) {
					Interval oInterval = new Interval(oOpenTime, oCurrentTime);
					Duration oDuration = oInterval.toDuration();
					
					//if time interval greater than 5 mins, remove it from list
					if(oDuration.getStandardMinutes() >= 5)
						g_oProtalProcessingJobs.remove(i);
				}
			}catch (JSONException e) {
			}
		}
	}
	
	// get count of portal handling job
	synchronized public static int getProtalHandlingJobCount() {
		return g_oProtalProcessingJobs.size();
	}
	
	public static boolean launchAutoStation(int iOutletId, String sUDID, boolean bStopBeforeStart){
		// Check the case to start auto station
		if (AppGlobal.g_oFuncSmartStation.isSmartStationModel() && !AppGlobal.g_oFuncSmartStation.isServiceMasterRole())
			return false;
		
		// Check if there is auto station or not
		boolean bFound = false;
		for(Map.Entry<Long, ClsActiveClient>entry:AppGlobal.g_lCurrentConnectClientList.entrySet()){
			ClsActiveClient oActiveClient = entry.getValue();
			if(sUDID.equals(oActiveClient.getUDID())){
				bFound = true;
				break;
			}
		}
		
		if(bFound){
			// The auto station is running
			if(bStopBeforeStart){
				// Stop the previous auto station and start again
				AppGlobal.stopAutoStation(iOutletId);
			}else{
				// No need to start a new one
				return true;
			}
		}
		
		// Read setup from config.ini
		IniReader iniReader;
		
		// Read setup from the setup file
		try {
			iniReader = new IniReader("cfg" + java.io.File.separator + "config.ini");
		} catch (IOException e) {
			AppGlobal.writeErrorLog(AppGlobal.class.getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Missing setup file (cfg/config.ini)");
			return false;
		}
		
		String sTmp = iniReader.getValue("connection", "launcher_port");
		if(sTmp == null){
			AppGlobal.writeErrorLog(AppGlobal.class.getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Missing setup for launcher port no");
			return false;
		}
		int iPortNo = Integer.parseInt(sTmp);
		
		TCPLib oTCPLib = new TCPLib();
		oTCPLib.initClient("127.0.0.1", iPortNo, false);
		if(!oTCPLib.isClientSocketConnected()) {
			AppGlobal.writeErrorLog(AppGlobal.class.getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Fail to connect to launcher with port no = " + iPortNo);
			return false; 
		}
		
		// Create the launch auto station packet
		JSONObject oSendPacketJsonObject = new JSONObject();
		
		try {
			oSendPacketJsonObject.put("type", "Connect");
			oSendPacketJsonObject.put("udid", sUDID);
			oSendPacketJsonObject.put("login", "");
			oSendPacketJsonObject.put("password", "");
			oSendPacketJsonObject.put("swipe_card_login", "");
			oSendPacketJsonObject.put("display_mode", "no_display");
		} catch (JSONException e) {
			AppGlobal.writeErrorLog(AppGlobal.class.getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Internal error");
			return false;
		}
		
		// Send the launch auto station packet to launcher
		if((oTCPLib.writeToServer(oSendPacketJsonObject.toString() + (char)4)) == false)
			return false;
		
		return true;
	}
	
	public static boolean launchPortalStation(int iOutletId, String sUDID, boolean bStopBeforeStart){
		// Check the case to start portal station
		if (AppGlobal.g_oFuncSmartStation.isSmartStationModel() && !AppGlobal.g_oFuncSmartStation.isServiceMasterRole())
			return false;
		
		// Check if there is portal station or not
		boolean bFound = false;
		for(Map.Entry<Long, ClsActiveClient>entry:AppGlobal.g_lCurrentConnectClientList.entrySet()){
			ClsActiveClient oActiveClient = entry.getValue();
			if(sUDID.equals(oActiveClient.getUDID())){
				bFound = true;
				break;
			}
		}
		
		if(bFound){
			// The portal station is running
			if(bStopBeforeStart){
				// Stop the previous portal station and start again
				AppGlobal.stopPortalStation(iOutletId);
			}else{
				// No need to start a new one
				return true;
			}
		}
		
		// wait to make sure the portal station is being kill
		String sCannotKillStationUUID = AppGlobal.waitStationBeKilled(iOutletId, sUDID, 0);
		if(!sCannotKillStationUUID.isEmpty()){
			AppGlobal.writeErrorLog(AppGlobal.class.getSimpleName(), new Exception().getStackTrace()[0].getMethodName(),
					AppGlobal.g_oFuncStation.get().getStationId() + "", "", "Fail to kill station " + sCannotKillStationUUID);
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "",
					"Station(" + sCannotKillStationUUID + ") cannot be killed in launchPortalStation");
		}
		// Read setup from config.ini
		IniReader iniReader;
		
		// Read setup from the setup file
		try {
			iniReader = new IniReader("cfg" + java.io.File.separator + "config.ini");
		} catch (IOException e) {
			AppGlobal.writeErrorLog(AppGlobal.class.getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Missing setup file (cfg/config.ini)");
			return false;
		}
		
		String sTmp = iniReader.getValue("connection", "launcher_port");
		if(sTmp == null){
			AppGlobal.writeErrorLog(AppGlobal.class.getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Missing setup for launcher port no");
			return false;
		}
		int iPortNo = Integer.parseInt(sTmp);
		
		TCPLib oTCPLib = new TCPLib();
		oTCPLib.initClient("127.0.0.1", iPortNo, false);
		if(!oTCPLib.isClientSocketConnected()) {
			AppGlobal.writeErrorLog(AppGlobal.class.getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Fail to connect to launcher with port no = " + iPortNo);
			return false; 
		}
		
		// Create the launch portal station packet
		JSONObject oSendPacketJsonObject = new JSONObject();
		
		try {
			oSendPacketJsonObject.put("type", "Connect");
			oSendPacketJsonObject.put("udid", sUDID);
			oSendPacketJsonObject.put("login", "");
			oSendPacketJsonObject.put("password", "");
			oSendPacketJsonObject.put("swipe_card_login", "");
			oSendPacketJsonObject.put("display_mode", "no_display");
		} catch (JSONException e) {
			AppGlobal.writeErrorLog(AppGlobal.class.getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Internal error");
			return false;
		}
		
		// Send the launch portal station packet to launcher
		if((oTCPLib.writeToServer(oSendPacketJsonObject.toString() + (char)4)) == false)
			return false;
		
		return true;
	}
	
	// Stop auto station by outlet ID
	public static void stopAutoStation(int iOutletId){
		ArrayList<Integer> oKillStationIdList = new ArrayList<>();
		
		for(Map.Entry<Long, ClsActiveClient>entry:AppGlobal.g_lCurrentConnectClientList.entrySet()){
			ClsActiveClient oActiveClient = entry.getValue();
			int iCurrentStationId = oActiveClient.getCurrentStationId();
			int iCurrentOutletId = oActiveClient.getCurrentOutletId();
			if(oActiveClient.isAutoStation() && iCurrentOutletId == iOutletId){
				oKillStationIdList.add(iCurrentStationId);
			}
		}
		
		if(oKillStationIdList.size() > 0){
			for(int iKillStationId:oKillStationIdList){
				AppGlobal.startKillSingleStation(iKillStationId, "kill auto station");
				AppGlobal.removeStationFromAutoAndPortalStationList(iKillStationId);
			}
		}
	}
	
	// Stop auto station by outlet ID
	public static void stopPortalStation(int iOutletId) {
		ArrayList<Integer> oPortalStationIdList = new ArrayList<>();
		
		for(Map.Entry<Long, ClsActiveClient>entry:AppGlobal.g_lCurrentConnectClientList.entrySet()){
			ClsActiveClient oActiveClient = entry.getValue();
			int iCurrentStationId = oActiveClient.getCurrentStationId();
			int iCurrentOutletId = oActiveClient.getCurrentOutletId();
			if(oActiveClient.isPortalStation() && iCurrentOutletId == iOutletId)
				oPortalStationIdList.add(iCurrentStationId);
		}
		
		if (!oPortalStationIdList.isEmpty()) {
			for(int iPortalStationId: oPortalStationIdList){
				AppGlobal.startKillSingleStation(iPortalStationId, AppGlobal.g_oLang.get()._("station_is_suspended"));
				AppGlobal.removeStationFromAutoAndPortalStationList(iPortalStationId);
			}
		}
	}
	
	// Prepare communication channel with Service Master MQ server
	public static boolean initMQCommandManager() {
		if (g_oMQCommandManager != null) {
			// Close previous connection
			g_oMQCommandManager.close();
		}
		
		g_oMQCommandManager = new MQCommandManager();
		if (!g_oMQCommandManager.init()) {
			// No setup or cannot connect to MQ server
			g_oMQCommandManager = null;
			return false;
		}
		
		return true;
	}
	
	// Add / Remove record or list from list g_oOverCookingTimeTableList
	synchronized public static void handleOverCookingTimeTable(int iOutletId, String sTableKey, DateTime oOverTime, boolean bRemove){
		if(iOutletId == 0 || (!bRemove && (sTableKey.isEmpty() || oOverTime == null)))
			return;
		
		if(bRemove){
			//remove the list or record from list
			if(sTableKey == null || sTableKey.isEmpty())
				g_oOverCookingTimeTableList.remove(iOutletId);	
			else if(g_oOverCookingTimeTableList.containsKey(iOutletId))
				g_oOverCookingTimeTableList.get(iOutletId).remove(sTableKey);
		}
		else{
			//add record to list
			if(!g_oOverCookingTimeTableList.containsKey(iOutletId))
				g_oOverCookingTimeTableList.put(iOutletId, new HashMap<String, DateTime>());
			g_oOverCookingTimeTableList.get(iOutletId).put(sTableKey, oOverTime);
		}
	}
	
	// Check whether table cooking overtime
	synchronized public static boolean isTableOverCookingTime(int iOutletId, int iTable, String sTableExtension){
		if(iOutletId == 0 || (iTable == 0 && sTableExtension.isEmpty()))
			return false;
		
		String sTableKey = "";
		if(sTableExtension.isEmpty())
			sTableKey = Integer.toString(iTable);
		else if(iTable == 0 && !sTableExtension.isEmpty())
			sTableKey = "0_" + sTableExtension;
		else 
			sTableKey = Integer.toString(iTable) + "_" + sTableExtension;
		
		if(!g_oOverCookingTimeTableList.containsKey(iOutletId) || !g_oOverCookingTimeTableList.get(iOutletId).containsKey(sTableKey))
			return false;
		
		DateTime oEarliestFinishTime = g_oOverCookingTimeTableList.get(iOutletId).get(sTableKey);
		DateTime oCurrentTime = AppGlobal.getCurrentTime(false);
		if(oEarliestFinishTime != null && oCurrentTime.isAfter(oEarliestFinishTime))
			return true;
		else
			return false;
	}
	
	// Add record from list g_oTableStatusCleaningList
	synchronized public static void updateCleaningTableList(PosOutletTable oPosOutletTable, DateTime oCloseCheckTime){
		int iInterval = AppGlobal.g_oFuncStation.get().getAutomaticallyChangeCleaningToVacantInterval();
		if (iInterval <= 0)
			return;
		
		// Update list after finishing check payment
		if (oPosOutletTable != null && oCloseCheckTime != null)
			AppGlobal.addCleaningTableToList(Integer.toString(oPosOutletTable.getTable()), oPosOutletTable.getTableExtension(), oCloseCheckTime, iInterval);
		else {
			// Only call API when there is no outlet information in the list or no table for the outlet in the list
			if(g_oTableStatusCleaningList.containsKey(AppGlobal.g_oFuncOutlet.get().getOutletId()) && !g_oTableStatusCleaningList.get(AppGlobal.g_oFuncOutlet.get().getOutletId()).isEmpty())
				return;
			// Call API to obtain close check time for whole outlet table with cleaning status if outlet data is not in the list
			PosOutletTableList oPosOutletTableList = new PosOutletTableList();
			
			try {
				JSONArray oJsonArray = oPosOutletTableList.getCleaningTableCloseCheckTimeByOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
				for (int i = 0; i < oJsonArray.length(); i++) {
					JSONObject obj = oJsonArray.getJSONObject(i);
					DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
					if (!obj.optString("closeCheckLocTime").isEmpty())
						oCloseCheckTime = oFmt.parseDateTime(obj.optString("closeCheckLocTime"));
					AppGlobal.addCleaningTableToList(obj.optString("table"), obj.optString("tableExt"), oCloseCheckTime, iInterval);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// Check and Remove record from list g_oTableStatusCleaningList
	synchronized public static void checkCleaningTable(){
		DateTime oCurrentTime = AppGlobal.getCurrentTime(false);
		for (Integer iLoopOutletId : g_oTableStatusCleaningList.keySet()) {
			List<String> oNeedRemoveTableKeyList = new ArrayList<String>(); 
			for (Map.Entry<String, DateTime> oHashmap : g_oTableStatusCleaningList.get(iLoopOutletId).entrySet()) {
				if (oCurrentTime.isAfter(oHashmap.getValue())) {
					String[] sTable = oHashmap.getKey().split("_");
					
					// If table extension is empty, pass null to api
					PosOutletTable oPosOutletTable = new PosOutletTable(iLoopOutletId, Integer.parseInt(sTable[0]), (sTable.length >= 2) ? sTable[1] : null);
					oPosOutletTable.setStatus(PosOutletTable.STATUS_VACANT);
					if (oPosOutletTable.addUpdate(true))
						oNeedRemoveTableKeyList.add(oHashmap.getKey());
				}
			}
			for (String sKey : oNeedRemoveTableKeyList)
				g_oTableStatusCleaningList.get(iLoopOutletId).remove(sKey);
		}
	}
	
	public static void addCleaningTableToList(String sTable, String sTableExtension, DateTime oCloseCheckTime, int iInterval) {
		if (sTable.isEmpty() || oCloseCheckTime == null)
			return;
		
		String sTableKey = "";
		int iOutletId = AppGlobal.g_oFuncOutlet.get().getOutletId();
		
		if(sTableExtension.isEmpty())
			sTableKey = sTable;
		else 
			sTableKey = sTable + "_" + sTableExtension;
		
		if(!g_oTableStatusCleaningList.containsKey(iOutletId))
			g_oTableStatusCleaningList.put(iOutletId, new HashMap<String, DateTime>());
		g_oTableStatusCleaningList.get(iOutletId).put(sTableKey, oCloseCheckTime.plusSeconds(iInterval));
	}
	
	//to remove the target record from g_oTableStatusCleaningList by table no and extension
	public static void removeCleaningTableFromList(String sTable, String sTableExtension) {
		if (sTable.isEmpty())
			return;
		
		String sTableKey = "";
		int iOutletId = AppGlobal.g_oFuncOutlet.get().getOutletId();
		
		if(sTableExtension.isEmpty())
			sTableKey = sTable;
		else 
			sTableKey = sTable + "_" + sTableExtension;
		
		if(!g_oTableStatusCleaningList.containsKey(iOutletId))
			return;
		if(g_oTableStatusCleaningList.get(iOutletId).containsKey(sTableKey))
			g_oTableStatusCleaningList.get(iOutletId).remove(sTableKey);
	}
	
	// For internal use
	public static String getInterfaceSession(int iIntfId){
		String sSession = "";
		
		if(iIntfId == 0)
			return null;
		if(g_oInterfaceSessions.containsKey(iIntfId)){
			for(Map.Entry<String, String> entry : g_oInterfaceSessions.get(iIntfId).entrySet())
				sSession = entry.getKey();
		}
		return sSession;
	}
	
	// For update interface session, if empty sNewSessionId (need to update the session) 
	public static void generateUpdateInterfaceSession(int iIntfId, String sNewSessionId){
		if(iIntfId == 0)
			return;
		DateTime oCurrentTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		boolean bNeedUpdate = false;
		String sSession = "";
		// Check the interface session exist or not
		if(g_oInterfaceSessions.containsKey(iIntfId)){
			// If interface session exist, compare the last update time stamp to current time
			// If excess 12 hours then need to update the session id
			sSession = sNewSessionId;
			String sInterfaceSessionTime = "";
			if(sSession.isEmpty()){
				for(Map.Entry<String, String> entry : g_oInterfaceSessions.get(iIntfId).entrySet()){
					sInterfaceSessionTime = entry.getValue();
					if(!sInterfaceSessionTime.isEmpty()){
						DateTime oOpenTime = formatter.parseDateTime(sInterfaceSessionTime);
						long diffTime = oCurrentTime.getMillis() - oOpenTime.getMillis();
						if(TimeUnit.MILLISECONDS.toMinutes(diffTime) > 720)
							bNeedUpdate = true;
					}
				}
			}
		}else
			bNeedUpdate = true;
		
		if(!sSession.isEmpty() || bNeedUpdate){
			FuncMembershipInterface oFuncMembershipInterface = new FuncMembershipInterface(AppGlobal.getPosInterfaceConfigById(iIntfId));
			if(bNeedUpdate)
				sSession = oFuncMembershipInterface.membershipGetSession(iIntfId);
				
			if(sSession != null && !sSession.isEmpty()){
				HashMap<String, String> oTmpSessionMap = new HashMap<String, String>();
				oTmpSessionMap.put(sSession, formatter.print(oCurrentTime));
				g_oInterfaceSessions.put(iIntfId, oTmpSessionMap);
			}
		}
	} 
	
	// Start synchronize global info
	public static void startSynchronizeGlobalInfo(String sVariable, String sContent, Boolean bRemove){
		// Fire command through MQ
		if(g_oMQCommandManager != null)
			g_oMQCommandManager.fireSynchronizeGlobalInfo(sVariable, sContent, bRemove);
	}
	
	// Start waiting until the station is killed or timeout
	public static String waitStationBeKilled(int iOutletId, String sBeKillStationUDID, int iExcludeKillStationId){
		String sCannotKillStationUDID = "";
		
		// wait until kill all target stations or 60s
		for(int iCount = 0 ; iCount < 60 ; iCount++){
			sCannotKillStationUDID = "";
			for(Map.Entry<Long, ClsActiveClient>entry:AppGlobal.g_lCurrentConnectClientList.entrySet()){
				ClsActiveClient oActiveClient = entry.getValue();
				if(oActiveClient.getCurrentOutletId() == iOutletId){
					if(oActiveClient.getCurrentStationId() == iExcludeKillStationId)
						continue;
					
					if(!sBeKillStationUDID.isEmpty()){
						if(oActiveClient.getUDID().equals(sBeKillStationUDID)){
							sCannotKillStationUDID = sBeKillStationUDID;
							break;
						}
					} else {
						sCannotKillStationUDID = oActiveClient.getUDID();
						break;
					}
				}
			}
			if(!sCannotKillStationUDID.isEmpty()){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					AppGlobal.stack2Log(e);
				}
			}
			else
				return "";
		}
		return sCannotKillStationUDID;
	}
	
	public static void doSurveillanceEvent(HashMap<String, String> oSurveillanceEventInfo) {
		List<PosInterfaceConfig> oInterfaceConfigList = getPosInterfaceConfigByInfType(InfInterface.TYPE_SURVEILLANCE_INTERFACE);
		if (!oInterfaceConfigList.isEmpty()) {
			for (PosInterfaceConfig oPosInterfaceConfig : oInterfaceConfigList) {
				if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ECONNECT)) {
					FuncSurveillance oFuncSurveillance = new FuncSurveillance(oPosInterfaceConfig);
					oFuncSurveillance.surveillanceEvent(oSurveillanceEventInfo, null);
				}
			}
		}
	}
	
	synchronized public static void addNewEventToSurveillanceList(int iStationId, FuncSurveillance oFuncSurveillance) {
		if (!g_oListForSurveillanceInterface.containsKey(iStationId))
			g_oListForSurveillanceInterface.put(iStationId, oFuncSurveillance);
	}
	
	synchronized public static FuncSurveillance getSurveillanceInfoFromList(int iStationId, PosInterfaceConfig oSurveillanceInterfaceConfig ) {
		if (!g_oListForSurveillanceInterface.containsKey(iStationId))
			g_oListForSurveillanceInterface.put(iStationId, new FuncSurveillance(oSurveillanceInterfaceConfig));
		
		return g_oListForSurveillanceInterface.get(iStationId);
	}
	
	
	public static DateTime getCurrentTime(boolean bIsUTC) {
		if (bIsUTC)
			return new DateTime().withZone(DateTimeZone.UTC);
		else {
			if (AppGlobal.g_oFuncOutlet.get() == null)
				return new DateTime();
			
			String sTimeZoneName = AppGlobal.g_oFuncOutlet.get().getShop().getTimezoneName();
			int iTimeZone = AppGlobal.g_oFuncOutlet.get().getShop().getTimezone();
			if (sTimeZoneName == null || sTimeZoneName.isEmpty())
				return Util.getCurrentLocalTimeByTimezone(iTimeZone);
			else {
				DateTime oDateTime = Util.getCurrentLocalTimeByTimezoneName(sTimeZoneName);
				if (oDateTime != null)
					return oDateTime;
				else
					return Util.getCurrentLocalTimeByTimezone(iTimeZone);
			}
		}
	}
	
	public static DateTimeZone getOffsetTimeZone() {
		if (AppGlobal.g_oFuncOutlet.get() == null)
			return null;
		
		String sTimeZoneName = AppGlobal.g_oFuncOutlet.get().getShop().getTimezoneName();
		int iTimeZone = AppGlobal.g_oFuncOutlet.get().getShop().getTimezone();
		DateTimeZone oDateTimeZone = null;
		if (sTimeZoneName == null || sTimeZoneName.isEmpty())
			oDateTimeZone = DateTimeZone.forOffsetMillis(iTimeZone * 60 *1000);
		else {
			try {
				oDateTimeZone = DateTimeZone.forID(sTimeZoneName);
			} catch (Exception e) {}
			if (oDateTimeZone == null)
				oDateTimeZone = DateTimeZone.forOffsetMillis(iTimeZone * 60 *1000);
		}
		return oDateTimeZone;
	}
	
	public static DateTime convertTimeToUTC(DateTime oDateTime) {
		return oDateTime.withZone(DateTimeZone.UTC);
	}
	
	public static DateTime convertTimeToLocal(DateTime oDateTime) {
		return oDateTime.withZone(getOffsetTimeZone());
	}
}
