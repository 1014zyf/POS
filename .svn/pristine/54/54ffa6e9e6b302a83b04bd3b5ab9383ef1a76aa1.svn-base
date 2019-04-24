package externaldevice;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lang.LangResource;
import om.InfInterface;
import om.MenuSetMenuLookupList;
import om.OmWsClient;
import om.OmWsClientGlobal;
import om.PosBusinessDayList;
import om.PosConfigList;
import om.PosFunctionList;
import om.PosInterfaceConfig;
import om.PosInterfaceConfigList;
import om.SystemConfig;
import om.SystemConfigList;

import org.json.JSONException;
import org.json.JSONObject;

import externallib.IniReader;
import externallib.TCPLib;
import app.AppGlobal;
import app.AppThreadManager;
import app.ClsActiveClient;
import app.FuncActionLog;
import app.FuncCheckItem;
import app.FuncDiscountAcl;
import app.FuncMenu;
import app.FuncMixAndMatch;
import app.FuncOctopus;
import app.FuncOutlet;
import app.FuncOverride;
import app.FuncStation;
import app.FuncUser;

public class AutoDeviceMain {
	public enum FUNC_LIST{paid, login, logout, print_check, get_pay_amount, cancel_payment, get_release_payment_info, release_payment};
	
	// Function list
	private PosFunctionList m_oFunctionList;
	
	// Business Day ID list of a month
	private ArrayList<String> m_oBusinessDayOfAMonthList;
	// Business Day ID list of a day
	private ArrayList<String> m_oBusinessDayOfADayList;
	
	// Flag to determine if the business date is correct or not
	private boolean m_bIncorrectBusinessDate;
	
	// Operation mode
	// 0 : Fine dining mode
	// 1 : Fast food mode
	// 2 : Stock delivery mode
	private AppGlobal.OPERATION_MODE m_eOperationMode = AppGlobal.OPERATION_MODE.fine_dining;
	
	// *********************************
	// For internal use, stored set menu
	private HashMap<Integer, MenuSetMenuLookupList> m_oStoredSetMenuLookupList;
	private ArrayList<FuncCheckItem> m_oStoredFuncCheckItemList;
	private ArrayList<PosInterfaceConfig> m_oInterfacesWithAutoAction;
	
	private String m_sStationAddress;
	
	//TODO: use error code instead
	private String m_sLastErrorMessage;
	
	
	public AutoDeviceMain(String sStationAddress, String sDisplayMode) {
		// Initialize the language
		AppGlobal.g_oLang.set(new LangResource());
		AppGlobal.g_oLang.get().switchLocale("en");

		// Initialize the menu item and menu cache
		AppGlobal.g_oFuncMenu.set(new FuncMenu());
		
		// Initialize the web service client
		OmWsClientGlobal.g_oWsClient.set(new OmWsClient());

		// Initialize the language index
		AppGlobal.g_oCurrentLangIndex.set(new Integer(1));
		
		// Initialize the action log
		AppGlobal.g_oActionLog.set(new FuncActionLog());
		
		// Initialize TCP object
		AppGlobal.g_oTCP.set(new TCPLib());
		
		AppGlobal.g_sDisplayMode.set(new String(sDisplayMode));
		
		// Initialize the octopus function
		AppGlobal.g_oFuncOctopus.set(new FuncOctopus());
		
		// Initialize the result for auto function
		AppGlobal.g_sResultForAutoFunction.set(AppGlobal.AUTO_FUNCTIONS_RESULT_LIST.fail.name());
		
		this.m_bIncorrectBusinessDate = false;
		this.m_sStationAddress = sStationAddress;
		m_sLastErrorMessage = "";
	}
	
	public boolean init(String sLoginId, String sLoginPassword, int iPortNo, ClsActiveClient oActiveClient) {
		try {
			//Load config.ini setup
			loadConfigIniSetup();
			
			//Login process
			if(!login(sLoginId, sLoginPassword)) {
				//return error, fail to login
				return false;
			}
			
			//Check if other modules are supported or not
			//AppGlobal.checkModuleExisting();
			AppGlobal.checkModulesExistingAndGetSystemConfig();
			
			//Load Setup
			String sErrorMsg = loadStationConfigSetup();
			if(!sErrorMsg.isEmpty()) {
				//Logout session
				m_sLastErrorMessage = sErrorMsg;
				AppGlobal.g_oFuncUser.get().logout();
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", m_sLastErrorMessage);
				return false;
			}
			
			sErrorMsg = loadOutletConfigSetup(AppGlobal.g_oFuncStation.get().getOutletId());
			if(!sErrorMsg.isEmpty()) {
				//Logout session
				m_sLastErrorMessage = sErrorMsg;
				AppGlobal.g_oFuncUser.get().logout();
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", m_sLastErrorMessage);
				return false;
			}
			
			//Check business date
			m_bIncorrectBusinessDate = !verifyBusinessDate();
			
			//Check interface setup to build the thread process
			m_oInterfacesWithAutoAction = new ArrayList<PosInterfaceConfig>();
			if(AppGlobal.isModuleSupport(AppGlobal.OPTIONAL_MODULE.pos_interface.name())) {
				List<PosInterfaceConfig> oPaymentInterfaceList = new ArrayList<PosInterfaceConfig>();
				oPaymentInterfaceList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PAYMENT_INTERFACE);
				if(oPaymentInterfaceList.size() > 0) {
					for(PosInterfaceConfig oPaymentInterfaceConfig: oPaymentInterfaceList) {
						JSONObject oInterfaceSetup = oPaymentInterfaceConfig.getInterfaceConfig();
						if(oInterfaceSetup.has("auto_station_setup")) {
							String sNeedAutoStation = oInterfaceSetup.optJSONObject("auto_station_setup").optJSONObject("params").optJSONObject("need_auto_station").optString("value");
							if(sNeedAutoStation != null && sNeedAutoStation.equals("yes")) 
								m_oInterfacesWithAutoAction.add(oPaymentInterfaceConfig);
						}
					}
				}
			}
			
			//Add the information of the current station(station ID and outlet ID) to a global list for killing purpose
			if(oActiveClient != null){
				oActiveClient.setAutoStation(true);
				oActiveClient.setCurrentStationId(AppGlobal.g_oFuncStation.get().getStationId());
				oActiveClient.setCurrentOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
			}

			//create thread for each interface auto job
			AppThreadManager oAppThreadManager = new AppThreadManager();
			if(m_oInterfacesWithAutoAction.size() > 0) {
				Object[] oParameters = new Object[4];
				oParameters[0] = AppGlobal.g_oFuncOutlet.get();
				oParameters[1] = AppGlobal.g_oFuncStation.get();
				oParameters[2] = AppGlobal.g_oFuncUser.get();
				oParameters[3] = AppGlobal.g_oCurrentLangIndex.get();
				
				for(int i=0; i<m_oInterfacesWithAutoAction.size(); i++) {
					AutoDeviceThreadMain oAutoDeviceThreadMain = new AutoDeviceThreadMain(m_oInterfacesWithAutoAction.get(i));
					oAppThreadManager.addThread((i+1), oAutoDeviceThreadMain, "init", oParameters);
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Create auto job thread, interface id:"+m_oInterfacesWithAutoAction.get(i).getInterfaceId());
					
				}
			}
			
			// Run all of the threads
			oAppThreadManager.runThread();
			
			// Wait for the thread to finish
			oAppThreadManager.waitForThread();
			
			// Remove current station from the global list
			AppGlobal.g_lCurrentConnectClientList.remove(oActiveClient.getCurrentThreadId());
		}
		catch (Exception e) {
			AppGlobal.stack2Log(e);
			// Logout session
			AppGlobal.g_oFuncUser.get().logout();
			
			return false;
		}
		
		return true;
	}
	
	//Load config.ini setup
	private void loadConfigIniSetup() {
		// Load the internal ID and password from config.ini
		// Read setup from the setup file
		IniReader iniReader = null;
		try {
			iniReader = new IniReader("cfg" + java.io.File.separator + "config.ini");
			String sTmp = iniReader.getValue("setup", "log_level");
			if(sTmp != null){
				AppGlobal.g_iLogLevel = Integer.parseInt(sTmp);
			}
		} catch (IOException e) {
			// Fail to read config.ini
			AppGlobal.stack2Log(e);
		}
	}
	
	private String loadStationConfigSetup() {
		String sErrorMsg = "";
		
		//Load station setup
		AppGlobal.g_oFuncStation.set(new FuncStation());
		if(!AppGlobal.g_oFuncStation.get().loadStation(m_sStationAddress, false)) {
			//return error, load station error
			sErrorMsg = AppGlobal.g_oFuncStation.get().getLastErrorMessage();
			return sErrorMsg;
		}
		
		// Check license
		sErrorMsg = AppGlobal.getLicenseErrorMessage(AppGlobal.g_oFuncStation.get().getStationId());
		if(sErrorMsg.length() > 0)
			return sErrorMsg;
		
		if(AppGlobal.g_oFuncStation.get().getStationStartCheckNumber() == 0) {
			//return error, load station check number error
			sErrorMsg = AppGlobal.g_oLang.get()._("start_check_number_cannot_be_zero");
			return sErrorMsg;
		}
		
		//Get the operation mode
		m_eOperationMode = AppGlobal.OPERATION_MODE.fine_dining;
		
		return "";
	}
	
	private String loadOutletConfigSetup(int iOutletId) {
		String sErrorMsg = "";
		
		//Load outlet setup
		AppGlobal.g_oFuncOutlet.set(new FuncOutlet());
		//Return value:	0 - No error
		//				1 - Loading error
		//				2 - Not yet daily start
		int iRet = AppGlobal.g_oFuncOutlet.get().loadOutlet(iOutletId, false);
		//Loading error
		if(iRet == 1) {
			//return error, load outlet failed
			sErrorMsg = AppGlobal.g_oFuncOutlet.get().getLastErrorMessage();
			return sErrorMsg;
		}
		
		// *****************************************************************
		// Create thread to load detail
		AppThreadManager oAppThreadManager = new AppThreadManager();
				
		// Add the method to the thread manager
		// Thread 1 : Get all system configuration
		PosConfigList oPosConfigList = new PosConfigList();
		Object[] oParameters = new Object[3];
		oParameters[0] = AppGlobal.g_oFuncStation.get().getStationId();
		oParameters[1] = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oParameters[2] = AppGlobal.g_oFuncOutlet.get().getShopId();
		oAppThreadManager.addThread(1, oPosConfigList, "getAllConfigsByStationOutletShop", oParameters);
		
		// Thread 2 : Load POS interface configuration setup
		PosInterfaceConfigList oInterfaceConfigList = new PosInterfaceConfigList();
		Object[] oParameter2s = new Object[4];
		oParameter2s[0] = AppGlobal.g_oFuncOutlet.get().getShopId();
		oParameter2s[1] = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oParameter2s[2] = AppGlobal.g_oFuncStation.get().getStationId();
		oParameter2s[3] = "system";
		oAppThreadManager.addThread(2, oInterfaceConfigList, "getInterfaceConfigList", oParameter2s);
		
		// Run all of the threads
		oAppThreadManager.runThread();

		// Wait for the thread to finish
		oAppThreadManager.waitForThread();
		
		AppGlobal.setPosConfigList(oPosConfigList.getPosConfigList());
		AppGlobal.setPosInterfaceConfigList(oInterfaceConfigList);
		
		// *****************************************************************
		// Create thread to load detail
		AppThreadManager oAppThreadManager2 = new AppThreadManager();
		
		// Thread 1 : Load function list
		m_oFunctionList = new PosFunctionList();
		// Create parameter array
		Object[] oParameter1s = new Object[4];
		oParameter1s[0] = AppGlobal.g_oFuncUser.get().getUserId();
		oParameter1s[1] = AppGlobal.g_oFuncUser.get().getUserGroupList();
		oParameter1s[2] = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oParameter1s[3] = AppGlobal.g_oFuncOutlet.get().getOutletGroupList();
		oAppThreadManager2.addThread(1, m_oFunctionList, "readAll", oParameter1s);
		
		// Thread 2 : Load the business day ID of a month
		m_oBusinessDayOfAMonthList = new ArrayList<String>();
		m_oBusinessDayOfADayList = new ArrayList<String>();
		oAppThreadManager2.addThread(2, this, "loadBusinessDayOfAMonth", null);
		
		// Thread 3 : Load override condition
		FuncOverride oFuncOverride = new FuncOverride();
		Object[] oParameter3s = new Object[1];
		oParameter3s[0] = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oAppThreadManager2.addThread(3, oFuncOverride, "readAllOverrideCondition", oParameter3s);
		
		// Thread 4 : Load mix and match rule
		AppGlobal.g_oFuncMixAndMatch.set(new FuncMixAndMatch());
		// Create parameter array
		Object[] oParameter4s = new Object[2];
		oParameter4s[0] = AppGlobal.g_oFuncOutlet.get().getShopId();
		oParameter4s[1] = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oAppThreadManager2.addThread(4, AppGlobal.g_oFuncMixAndMatch.get(), "loadRuleList", oParameter4s);
		
		// Thread 5 : Load discount allowance
		FuncDiscountAcl oFuncDiscountAcl = new FuncDiscountAcl();
		Object[] oParameter5s = new Object[1];
		oParameter5s[0] = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oAppThreadManager2.addThread(5, oFuncDiscountAcl, "readAllDiscountAclByOutlet", oParameter5s);
		
		// Run all of the threads
		oAppThreadManager2.runThread();
		
		// Wait for the thread to finish
		oAppThreadManager2.waitForThread();
		
		if(iRet == 0 && !this.crossDate()) {
			sErrorMsg = m_sLastErrorMessage;
			return sErrorMsg;
		}
		
		// Check if check stock operation should be skipped or not
		AppGlobal.g_bNotCheckStock = AppGlobal.g_oFuncStation.get().getNotCheckStock();
		
		AppGlobal.g_oFuncOverrideList.put(AppGlobal.g_oFuncOutlet.get().getOutletId(), oFuncOverride);
		AppGlobal.g_oFuncDiscountAclList.put(AppGlobal.g_oFuncOutlet.get().getOutletId(), oFuncDiscountAcl);
		
		//Not yet daily start
		if(iRet == 2) {
			sErrorMsg = AppGlobal.g_oFuncOutlet.get().getLastErrorMessage();
			return sErrorMsg;
		}
		
		return "";
	}
	
	//Login process
	private boolean login(String sLoginId, String sLoginPassword) {
		FuncUser oFuncUser = processLogin(sLoginId, sLoginPassword);
		if(oFuncUser == null) {
			//return error, fail to login
			return false;
		}
		
		//Get user defined language index
		if(oFuncUser.getUser().getLang() == 0) {
			AppGlobal.g_oCurrentLangIndex.set(new Integer(1));
		}
		else {
			AppGlobal.g_oCurrentLangIndex.set(new Integer(oFuncUser.getUser().getLang()));
		}
		
		// *****************************************************************
		// Create thread to load detail
		AppThreadManager oAppThreadManager = new AppThreadManager();
				
		// Add the method to the thread manager
		
		// Thread 2 : Load supported language information 
		SystemConfigList sysConfigList = new SystemConfigList();
		Object[] oParameter1s = new Object[2];
		oParameter1s[0] = "system";
		oParameter1s[1] = "language_code";
		oAppThreadManager.addThread(1, sysConfigList, "readBySectionAndVariable", oParameter1s);
		
		// Thread 3 : Load system data path
		SystemConfigList sysConfigList2 = new SystemConfigList();
		Object[] oParameter2s = new Object[2];
		oParameter2s[0] = "system";
		oParameter2s[1] = "data_path";
		oAppThreadManager.addThread(2, sysConfigList2, "readBySectionAndVariable", oParameter2s);
		
		// Run all of the threads
		oAppThreadManager.runThread();

		// Wait for the thread to finish
		oAppThreadManager.waitForThread();
		
		// Get language setup
		AppGlobal.g_oSupportedLangList = new ArrayList<HashMap<String, String>>();
		JSONObject tempJSONObject = null;
		for(SystemConfig sysConfig:sysConfigList.getSystemConfigList().values()) {
			HashMap<String, String> oTempLangInfo = new HashMap<String, String>();
			if(sysConfig.getValue() == null)
				continue;
			try {
				if(sysConfig.getValue().length() != 0){
					tempJSONObject = new JSONObject(sysConfig.getValue());
					oTempLangInfo.put("index", String.valueOf(sysConfig.getIndex()));
					oTempLangInfo.put("name", tempJSONObject.getString("name"));
					oTempLangInfo.put("code", tempJSONObject.getString("code"));
					oTempLangInfo.put("url", tempJSONObject.getString("url"));
					
					AppGlobal.g_oSupportedLangList.add(oTempLangInfo);
				}
			}catch (JSONException jsone) {
				AppGlobal.stack2Log(jsone);
			}
		}
		//set the language
		String langCode = "en";
		int langIndex = 1;
		for(HashMap<String, String> oLangInfo:AppGlobal.g_oSupportedLangList) {
			langIndex = Integer.parseInt(oLangInfo.get("index"));
			if(langIndex == AppGlobal.g_oCurrentLangIndex.get()) {
				langCode = oLangInfo.get("code");
				break;
			}
		}
		HashMap<Integer, String> oLangCodeList = new HashMap<Integer, String>();
		for(HashMap<String, String> oLangInfo:AppGlobal.g_oSupportedLangList) {
			oLangCodeList.put(Integer.parseInt(oLangInfo.get("index")), oLangInfo.get("code"));
		}
		AppGlobal.g_oLang.get().init(oLangCodeList);
		AppGlobal.g_oLang.get().switchLocale(langCode);
		
		// Get system data path
		for(SystemConfig sysConfig1:sysConfigList2.getSystemConfigList().values()) {
			if(sysConfig1.getValue() == null)
				continue;
			else {
				AppGlobal.g_sSystemDataPath = sysConfig1.getValue();
				break;
			}
		}
		
		// Assign the user to global
		AppGlobal.g_oFuncUser.set(oFuncUser);
		
		return true;
	}
	
	private FuncUser processLogin(String sLoginId, String sLoginPassword) {
		FuncUser oFuncUser = new FuncUser();
		
		if(sLoginId.length() == 0) {
			// Login by internal ID first
			String sErrorMessage = oFuncUser.login(oFuncUser.getServiceAccountLoginId(), oFuncUser.getServiceAccountPassword(), true);
			if(sErrorMessage.length() > 0){
				m_sLastErrorMessage = AppGlobal.getLoginErrorMessage(sErrorMessage);
				return null;
			}
		}
		else {
			String sErrorMessage = oFuncUser.login(sLoginId, sLoginPassword, true);
			if(sErrorMessage.length() > 0) {
				//return error
				m_sLastErrorMessage = AppGlobal.getLoginErrorMessage(sErrorMessage);
				return null;
			}
		}
		
		return oFuncUser;
	}
	
	// Load business day of a month
	private void loadBusinessDayOfAMonth() {
		PosBusinessDayList oBusinessDayList = new PosBusinessDayList();
		String sStartDate="", sEndDate="";
		BigInteger iTodayYear, iTodayMonth;
		
		//generate a range
		sStartDate = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString().substring(0, 8)+"01";
		iTodayYear = new BigInteger(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString().substring(0, 4));
		iTodayMonth = new BigInteger(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString().substring(5, 7));
		
		if (iTodayMonth.equals(new BigInteger("1")) || iTodayMonth.equals(new BigInteger("3")) || iTodayMonth.equals(new BigInteger("5")) || iTodayMonth.equals(new BigInteger("7")) || iTodayMonth.equals(new BigInteger("8")) || iTodayMonth.equals(new BigInteger("10")) || iTodayMonth.equals(new BigInteger("12")))
			sEndDate = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString().substring(0, 8)+"31";
		else if (iTodayMonth.equals(new BigInteger("4")) || iTodayMonth.equals(new BigInteger("6")) || iTodayMonth.equals(new BigInteger("9")) || iTodayMonth.equals(new BigInteger("11")))
			sEndDate = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString().substring(0, 8)+"30";
		else {
			if (iTodayYear.mod(new BigInteger("4")).equals(new BigInteger("0")))
				sEndDate = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString().substring(0, 8)+"29";
			else
				sEndDate = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString().substring(0, 8)+"28";
		}
		
		int iShopId = 0;
		if (AppGlobal.g_oFuncStation.get().getDutyMealOnCreditLimit("dutymeal_shop_limit") > 0 || AppGlobal.g_oFuncStation.get().getDutyMealOnCreditLimit("on_credit_shop_limit") > 0)
			iShopId = AppGlobal.g_oFuncOutlet.get().getShopId();
		oBusinessDayList.readBusinessDayByShopOrOutletPeriod(iShopId, AppGlobal.g_oFuncOutlet.get().getOutletId(), sStartDate, sEndDate);
		
		m_oBusinessDayOfAMonthList = oBusinessDayList.getListOfBusinessDayId();
		
		// load business day of a day
		sEndDate = sStartDate;
		oBusinessDayList.readBusinessDayByShopOrOutletPeriod(iShopId, AppGlobal.g_oFuncOutlet.get().getOutletId(), sStartDate,
				sEndDate);
		m_oBusinessDayOfADayList = oBusinessDayList.getListOfBusinessDayId();
	}
	
	//Check weather current time exceeds business hour but not yet daily close
	private boolean crossDate() {
		// daily close warning level
        // 1: pos not allowed
        // 2: allowed but passwd needed
        // 0: not warning
		// -1: no PosConfig record
		int iWarnLevel = AppGlobal.g_oFuncStation.get().getBusinessHourWarnLevel();
		if(iWarnLevel <= 0)	//No Warning
			return true;
		
		boolean bCrossDay = AppGlobal.g_oFuncOutlet.get().checkCrossDay();
		String sBusinessDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString();
		
		if(!bCrossDay)
			return true;
		
		if(iWarnLevel == 1) {
			//return error
			m_sLastErrorMessage = sBusinessDay + " " + AppGlobal.g_oLang.get()._("daily_close_not_yet_completed") + "!\n" + AppGlobal.g_oLang.get()._("hero_pos_not_allowed");
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", m_sLastErrorMessage);
			return false;
		}
		else {
			m_sLastErrorMessage =  AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString() + " " + AppGlobal.g_oLang.get()._("daily_close_not_yet_completed") + "!\n\n"
				+ AppGlobal.g_oLang.get()._("you_must_perform_daily_close_procedure") + "\n"
				+ AppGlobal.g_oLang.get()._("or_subsequent_sales_will_be_mixed_to") + " " + sBusinessDay;
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", m_sLastErrorMessage);
			return false;
		}
	}
	
	//Check business date
	private boolean verifyBusinessDate() {
		FuncOutlet oFuncOutlet = new FuncOutlet();
		if(oFuncOutlet.loadBusinessDay(AppGlobal.g_oFuncOutlet.get().getOutletId())) {
			// Check if loaded business date = database business date
			if(!AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId().equals(oFuncOutlet.getBusinessDay().getBdayId())){
				// Business date is incorrect, set the flag to force close
				return false;
			}
			else {
				return true;
			}
		}
		
		// Fail to retrieve the business date
		return false;
	}
}
