package externaldevice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import externallib.StringLib;
import externallib.TCPLib;
import externallib.TCPLibForPayAtTable;
import om.InfInterface;
import om.InfVendor;
import om.MemMemberModuleInfo;
import om.PosBusinessDay;
import om.PosCheck;
import om.PosCheckDiscount;
import om.PosCheckExtraInfo;
import om.PosCheckItem;
import om.PosCheckPayment;
import om.PosCheckTable;
import om.PosDiscountType;
import om.PosDiscountTypeList;
import om.PosFunctionList;
import om.PosInterfaceConfig;
import om.PosOverrideCondition;
import om.PosPaymentMethod;
import om.PosPaymentMethodList;
import om.PosStationDevice;
import om.PosVoidReason;
import app.AppGlobal;
import app.AppThreadManager;
import app.FormMain;
import app.FuncCheck;
import app.FuncCheckItem;
import app.FuncCheckListener;
import app.FuncOutlet;
import app.FuncPMS;
import app.FuncPayAtTable;
import app.FuncPayment;
import app.FuncPaymentInterface;
import app.FuncStation;
import app.FuncUser;
import app.FuncVGS;

public class AutoDeviceThreadMain implements FuncCheckListener{
	private boolean m_bTerminate;
	private PosInterfaceConfig m_oInterfaceConfig;
	private String m_sLastErrorMessage;
	private FuncCheck m_oFuncCheck;
	private FuncPayment m_oFuncPayment;
	private FuncPayAtTable m_oFuncPayAtTable;
	private HashMap<Integer, String> m_oPaymentInterfaceSupportedPaytype;
	private PosFunctionList m_oFunctionList;

	private TCPLib m_oTCPLib;
	private TCPLibForPayAtTable m_oTCPLibForPayAtTable;
	private List<Integer> m_oClientSockIdList;
	
	private boolean m_bBreakListen;
	private HashMap<String, Object> m_oResponsePacket;
	
	// *********************************
	// For internal use, stored the processing send check
	private HashMap<String, DateTime> m_oProcessingSendChecks;
	// *********************************
	
	public static String FUNC_RESULT_SUCCESS = "s";
	public static String FUNC_RESULT_CANCEL_BY_USER = "c";
	public static String FUNC_RESULT_FAIL = "f";
	public static String FUNC_RESULT_NO_SUCH_RECORD = "r";
	
	public AutoDeviceThreadMain(PosInterfaceConfig oPosInterfaceConfig) {
		m_oFuncCheck = new FuncCheck();
		m_oFuncPayment = new FuncPayment();
		m_oInterfaceConfig = oPosInterfaceConfig;
		m_bTerminate = false;
		m_sLastErrorMessage = "";
		m_oPaymentInterfaceSupportedPaytype = new HashMap<Integer, String>();
		m_oProcessingSendChecks = new HashMap<String, DateTime>();
		m_oFunctionList = new PosFunctionList();
		
		// prepare the payment supported in payment interface
		if(m_oInterfaceConfig != null) {
			JSONObject tempJSONObject = m_oInterfaceConfig.getInterfaceConfig().optJSONObject("payment_type_support");
			if(tempJSONObject != null) {
				JSONObject oSupportedPaytypes = tempJSONObject.optJSONObject("params");
				if(oSupportedPaytypes != null) {
					Iterator<?> keys = oSupportedPaytypes.keys();
					while( keys.hasNext() ) {
						String key = (String) keys.next();
						int iType = oSupportedPaytypes.optJSONObject(key).optInt("value", 0);
						m_oPaymentInterfaceSupportedPaytype.put(iType, key);
					}
				}
			}
		}
	}
	
	public void init(FuncOutlet oFuncOutlet, FuncStation oFuncStation, FuncUser oFuncUser, int iLangIndex) {
		boolean bSwitchUser = false;
		
		AppGlobal.g_oFuncOutlet.set(oFuncOutlet);
		AppGlobal.g_oFuncStation.set(oFuncStation);
		if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE)) { 
			String sLangCode= m_oInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").optJSONObject("pax_language_index").optString("value", "eng");
			int iInterfaceLangIndex = 1;
			for(HashMap<String, String> oLangInfo:AppGlobal.g_oSupportedLangList) {
				if(oLangInfo.get("code").equals(sLangCode))
					iInterfaceLangIndex = Integer.parseInt(oLangInfo.get("index"));
			}
			
			AppGlobal.g_oCurrentLangIndex.set(iInterfaceLangIndex);
			AppGlobal.g_oLang.get().switchLocale(sLangCode);
		} else
			AppGlobal.g_oCurrentLangIndex.set(iLangIndex);

		FuncUser oNewFuncUser = new FuncUser(); 
		String sLoginId = m_oInterfaceConfig.getInterfaceConfig().optJSONObject("auto_station_setup").optJSONObject("params").optJSONObject("user_id").optString("value", "");
		String sPassword = m_oInterfaceConfig.getInterfaceConfig().optJSONObject("auto_station_setup").optJSONObject("params").optJSONObject("user_password").optString("value", "");
		if(!sLoginId.isEmpty() && !sPassword.isEmpty() && !oFuncUser.getLoginId().equals(sLoginId)) {
			bSwitchUser = true;
			String sErrorMessage = oNewFuncUser.login(sLoginId, sPassword, true);
			if(sErrorMessage.length() > 0) {
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "User("+sLoginId+") login fail :"+sErrorMessage+". Use user of config.ini");
				bSwitchUser = false;
			}
		}
		if(bSwitchUser)
			AppGlobal.g_oFuncUser.set(oNewFuncUser);
		else
			AppGlobal.g_oFuncUser.set(oFuncUser);
		
		AppThreadManager oAppThreadManager = new AppThreadManager();
		PosBusinessDay oBusinessDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay();
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		Object[] oParameters = new Object[8];
		oParameters[0] = AppGlobal.g_oFuncOutlet.get().getShopId();
		oParameters[1] = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oParameters[2] = dateFormat.print(oBusinessDay.getDate());
		oParameters[3] = oBusinessDay.isHoliday();
		oParameters[4] = oBusinessDay.isDayBeforeHoliday();
		oParameters[5] = oBusinessDay.isSpecialDay();
		oParameters[6] = oBusinessDay.isDayBeforeSpecialDay();
		oParameters[7] = oBusinessDay.getDayOfWeek();
		oAppThreadManager.addThread(1, m_oFuncPayment, "readAllPaymentMethod", oParameters);

		// Add the method to the thread manager
		// Thread 1 : Load function list
		m_oFunctionList = new PosFunctionList();
		// Create parameter array
		Object[] oParameter1s = new Object[4];
		oParameter1s[0] = AppGlobal.g_oFuncUser.get().getUserId();
		oParameter1s[1] = AppGlobal.g_oFuncUser.get().getUserGroupList();
		oParameter1s[2] = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oParameter1s[3] = AppGlobal.g_oFuncOutlet.get().getOutletGroupList();
		oAppThreadManager.addThread(2, m_oFunctionList, "readAll", oParameter1s);
		
		// Run all of the threads
		oAppThreadManager.runThread();

		int iSleepPeriod = 20;
		if(m_oInterfaceConfig != null) {
			if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_OGS))
				iSleepPeriod = m_oInterfaceConfig.getInterfaceConfig().optJSONObject("auto_station_setup").optJSONObject("params").optJSONObject("polling_period").optInt("value", 20);
		}
		FuncPaymentInterface oFuncPaymentInterface = new FuncPaymentInterface(m_oInterfaceConfig);
		List<JSONObject> oPaidCheckList = null;

		// Wait for the thread to finish
		oAppThreadManager.waitForThread();
		
		//load the table and table name pair list
		AppGlobal.g_oFuncOutlet.get().buildTableNameList();

		if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE)) {
			m_oFuncPayAtTable = new FuncPayAtTable(m_oInterfaceConfig);
			this.initServer();
		}else {
			
			AppGlobal.addStationToAutoAndPortalStationList(AppGlobal.g_oFuncStation.get().getStationId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
			
			int iSleepCount = 0;
			do{
				try {
					// Sleep for 0.5s
					Thread.sleep(500);
				}catch (Exception e) {
					AppGlobal.stack2Log(e);
				}
	
				// Interval for checking of some business logic (e.g. be killed by other station during daily start/close/reload setting)
				intermediateBusinessChecking();
				
				// Check if kill signal is received
				String sKillReason = AppGlobal.getKilledReason();
				if(sKillReason.length() > 0) {
					// Station is killed
					// break the loop
					AppGlobal.finishBeingKilled();
					m_bTerminate = true;
					break;
				}
				
				iSleepCount++;
				if(iSleepCount >= (iSleepPeriod * 2)){
					// Perform polling function
					if(m_oInterfaceConfig != null && m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_OGS)) {
						oPaidCheckList = oFuncPaymentInterface.checkPaymentResult();
						payChecks(oPaidCheckList);
					}
					
					iSleepCount = 0;
				}
				
			}while(!m_bTerminate);
		}
	}

	public boolean initServer() {
		JSONObject oInterfaceSetup = m_oInterfaceConfig.getInterfaceConfig();
		String sCharSet = oInterfaceSetup.optJSONObject("general_setup").optJSONObject("params").optJSONObject("pax_locale").optString("value");
		
		JSONObject oPaymentInterfaceSetup = m_oInterfaceConfig.getConfigValue();
		int iPortNo = oPaymentInterfaceSetup.optJSONObject("general").optJSONObject("params").optJSONObject("auto_station_port").optInt("value");
		
		//Open Launcher localhost Port
		m_oTCPLibForPayAtTable = new TCPLibForPayAtTable();
		m_oTCPLibForPayAtTable.setEndByte(125); // '}' character
		m_oTCPLibForPayAtTable.setCharSet(sCharSet);
		m_oClientSockIdList = new ArrayList<Integer>();
		String sClientIPAddress = "127.0.0.1";
		String sTmp = m_oTCPLibForPayAtTable.initServer(sClientIPAddress, iPortNo, false);
		
		if(sTmp.isEmpty() == false) {
			this.m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_build_connection")+ ": " + iPortNo + ", " + AppGlobal.g_oLang.get()._("error") + ": " + sTmp;
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Cannot open socket server");
			return false;
		}

		m_oResponsePacket = new HashMap<String, Object>();
		// Selector
		Selector oSelector = null;
		try{
			oSelector = SelectorProvider.provider().openSelector();
		} catch ( Exception e ) {
			// Internal error
			AppGlobal.stack2Log(e);
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to open selector");
			return false;
		}

		// Register the server socket channel
		ServerSocketChannel oChannel = m_oTCPLibForPayAtTable.getSocketChannel();
		SelectionKey oLauncherKey = null;
		try {
			oLauncherKey = oChannel.register(oSelector, SelectionKey.OP_ACCEPT);
		} catch (ClosedChannelException e2) {
			e2.printStackTrace();
		}
		
		AppGlobal.addStationToAutoAndPortalStationList(AppGlobal.g_oFuncStation.get().getStationId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
		
		m_bBreakListen = false;
		AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Socket Start with interface ID:"+m_oInterfaceConfig.getInterfaceId()+" and listening port:"+iPortNo);
		int iCounter = 0;
		while(!m_bBreakListen)
		{
			int n = 0;
			iCounter += 1;
			try {
				n = oSelector.select(500);
			} catch (IOException e) {
				AppGlobal.stack2Log(e);
				return false;
			}
			
			// Check in every 3 minutes
			if(iCounter % 360 == 0) {
				AppGlobal.checkBusinessday(PosStationDevice.KEY_AUTO_STATION);
				iCounter = 0;
			}
			
			if (n == 0) {
				// Check if kill signal is received
				String sKillReason = AppGlobal.getKilledReason();
				if(sKillReason.length() > 0) {
					// Station is killed
					// break the loop
					AppGlobal.finishBeingKilled();
					m_bBreakListen = true;
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Station killed signal received. Reason:"+sKillReason);
					break;
				}
				
				continue;
			}

			java.util.Iterator<SelectionKey> iterator = oSelector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey oIncomingSelectionkey = (SelectionKey) iterator.next();
				
				iterator.remove();
				
				// Client connect
				if (oIncomingSelectionkey.isAcceptable() && oIncomingSelectionkey == oLauncherKey) {
					// Wait for the client connection
					int iClientSockId = m_oTCPLibForPayAtTable.listen();
					m_oClientSockIdList.add(iClientSockId);
					if (iClientSockId > 0) {
						AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "",
								AppGlobal.g_oFuncUser.get().getUserId() + "", "Client connection received");
						
						if(AppGlobal.g_bWriteClientConnectionLog){
							AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "<<< Before receive from client");
						}
						
						try {
							if(m_oTCPLibForPayAtTable.getPacket().length() == 0)
								continue;
							
							if(AppGlobal.g_bWriteClientConnectionLog){
								AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "<<< After receive from client");
							}

							//Check kill reason
							String sKillReason = AppGlobal.getKilledReason();
							if(sKillReason.length() > 0) {
								// Station is killed
								// break the loop
								AppGlobal.finishBeingKilled();
								m_bBreakListen = true;
								AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Station killed signal received. Reason:"+sKillReason);
								break;
							}
							
							String sResult = "";
							if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE))
								sResult = FuncPayAtTable.getRequestStringPacket(m_oTCPLibForPayAtTable.getPacket());
							
							// Invalid Packet Format
							if(sResult == null) {
								m_oResponsePacket.put("errMessage", "Incorrect response format");
								m_oResponsePacket.put("status", "0");
							}
							AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Read <<<< "+new String(sResult));
							
							JSONObject oRequestJSONObject = new JSONObject(sResult);
							String sFuncID = oRequestJSONObject.optString("funID");

							if(sFuncID.equals(FuncPayAtTable.FUNCTION_LOGIN)) 
								fcnSwitch(AutoDeviceMain.FUNC_LIST.login.name(), oRequestJSONObject);
							else if(sFuncID.equals(FuncPayAtTable.FUNCTION_LOGOUT))
								fcnSwitch(AutoDeviceMain.FUNC_LIST.logout.name(), oRequestJSONObject);
							else if(sFuncID.equals(FuncPayAtTable.FUNCTION_PRINT_CHECK))
								fcnSwitch(AutoDeviceMain.FUNC_LIST.print_check.name(), oRequestJSONObject);
							else if(sFuncID.equals(FuncPayAtTable.FUNCTION_GET_CHECK_AMOUNT))
								fcnSwitch(AutoDeviceMain.FUNC_LIST.get_pay_amount.name(), oRequestJSONObject);
							else if(sFuncID.equals(FuncPayAtTable.FUNCTION_CANCEL_PAYMENT))
								fcnSwitch(AutoDeviceMain.FUNC_LIST.cancel_payment.name(), oRequestJSONObject);
							else if(sFuncID.equals(FuncPayAtTable.FUNCTION_CONFIRM_PAYMENT))
								fcnSwitch(AutoDeviceMain.FUNC_LIST.paid.name(), oRequestJSONObject);
							else if(sFuncID.equals(FuncPayAtTable.FUNCTION_GET_RELEASE_PAYMENT_INFO))
								fcnSwitch(AutoDeviceMain.FUNC_LIST.get_release_payment_info.name(), oRequestJSONObject);
							else if(sFuncID.equals(FuncPayAtTable.FUNCTION_RELEASE_PAYMENT))
								fcnSwitch(AutoDeviceMain.FUNC_LIST.release_payment.name(), oRequestJSONObject);
							
							//sending response to client
							JSONObject responseJSONObject = m_oFuncPayAtTable.packingResponseJSON(m_oResponsePacket);
							this.writeToClient(iClientSockId, responseJSONObject, true);
							
						} catch (Exception e1) {
							AppGlobal.stack2Log(e1);
						}
					}
				}
			}
		}
		
		for(Integer iClientSockId: m_oClientSockIdList) {
			m_oTCPLibForPayAtTable.closeClient(iClientSockId);
		}
		m_oTCPLibForPayAtTable.closeListenSocket();
		try {
			oSelector.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return m_bBreakListen;
	}
	
	// Function to handle some business checking during idle time or click
	public void intermediateBusinessChecking() {

		String sReason = AppGlobal.getKilledReason();
		if(sReason.length() > 0){
			// The station is going to be killed
			// nothing to do
			return;
		}
		
		if(AppGlobal.g_oFuncOutlet == null || AppGlobal.g_oFuncOutlet.get() == null)
			// No outlet is set
			return;

		// Check if auto-daily operation is performing or not
		sReason = AppGlobal.readExternalDailyOperationFile(AppGlobal.g_oFuncOutlet.get().getOutletId());
		if(sReason.length() > 0){
			// Kill myself
			AppGlobal.startKillSingleStation(AppGlobal.g_oFuncStation.get().getStationId(), sReason);
		}
	}
	
	private boolean fcnSwitch(String sFuncKey, JSONObject oFuncParamJSONObj) {
		AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", sFuncKey);
		
		if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE)) {
			m_oFuncPayAtTable.init(oFuncParamJSONObj);
			if(m_oFuncPayAtTable.parseRequestJSON() == false) {
				m_oResponsePacket.put("status", "0");
				m_oResponsePacket.put("errMessage", m_oFuncPayAtTable.getLastErrorMessage());
				m_sLastErrorMessage = m_oFuncPayAtTable.getLastErrorMessage();
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to paid check:"+m_sLastErrorMessage);
				return false;
			}else
				m_oResponsePacket = m_oFuncPayAtTable.constructBasicResponsePacket();
			
			if (!m_oFuncPayAtTable.isLoginFunction()) {
				if (m_oFuncPayAtTable.isUserLogined() == false) {
					m_sLastErrorMessage = m_oFuncPayAtTable.getLastErrorMessage();
					m_oResponsePacket.put("status", "0");
					m_oResponsePacket.put("errMessage", m_sLastErrorMessage);
					return false;
				}
				
				// Check if user has function authority
				if (!m_oFuncPayAtTable.getFuncUser().isSystemAdmin()) {
					int sFuncId = 0;
					String sTempFuncKey = "";
					if (sFuncKey.equals(AutoDeviceMain.FUNC_LIST.print_check.name())) {
						sTempFuncKey = sFuncKey;
						sFuncId = m_oFunctionList.getFunctionIdByKey(sTempFuncKey);
					} else if (sFuncKey.equals(AutoDeviceMain.FUNC_LIST.paid.name())
							|| sFuncKey.equals(AutoDeviceMain.FUNC_LIST.get_pay_amount.name())) {
						sTempFuncKey = AutoDeviceMain.FUNC_LIST.paid.name();
						sFuncId = m_oFunctionList.getFunctionIdByKey(sTempFuncKey);
					} else if (sFuncKey.equals(AutoDeviceMain.FUNC_LIST.release_payment.name())
							|| sFuncKey.equals(AutoDeviceMain.FUNC_LIST.get_release_payment_info.name())) {
						sTempFuncKey = AutoDeviceMain.FUNC_LIST.release_payment.name();
						sFuncId = m_oFunctionList.getFunctionIdByKey(sTempFuncKey);
					}
					
					if (sFuncId != 0 && !sTempFuncKey.isEmpty()) {
						if (!m_oFuncPayAtTable.checkUserAuthority(sFuncId, sTempFuncKey)) {
							m_sLastErrorMessage = m_oFuncPayAtTable.getLastErrorMessage();
							m_oResponsePacket.put("status", "0");
							m_oResponsePacket.put("errMessage", m_sLastErrorMessage);
							return false;
						}
					}
				}
			}
		}
		
		// set FuncUser using current operID
		if(!sFuncKey.equals(AutoDeviceMain.FUNC_LIST.login.name()))
			AppGlobal.g_oFuncUser.set(m_oFuncPayAtTable.getFuncUser());
		
		// login
		if(sFuncKey.equals(AutoDeviceMain.FUNC_LIST.login.name())) {
			String sLogin = "", sPwd = "";
			if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE)) {
				sLogin = m_oFuncPayAtTable.getOperatorId();
				sPwd = m_oFuncPayAtTable.getOperatorPwd();
			}
			
			// Support swipe employee card to login
			String sCardNumber = "";
			if(sPwd.isEmpty())
				sCardNumber = sLogin;
			
			if(!this.systemLogin(sLogin, sPwd, sCardNumber)) {
				if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE)) {
					m_oResponsePacket.put("status", "0");
					m_oResponsePacket.put("errMessage", AppGlobal.g_oLang.get()._("fail_to_login"));
					return true;
				}
			}else {
				if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE))
					m_oResponsePacket = m_oFuncPayAtTable.constructLoginResponsePacket();
			}
		}
		// logout
		else if(sFuncKey.equals(AutoDeviceMain.FUNC_LIST.logout.name())) {
			String sLogin = "";
			if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE))
				sLogin = m_oFuncPayAtTable.getOperatorId();
			
			if(!systemLogout(sLogin)) {
				if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE)) {
					m_oResponsePacket.put("status", "0");
					m_oResponsePacket.put("errMessage", m_sLastErrorMessage);
					return true;
				}
			}
		}
		// print check
		else if(sFuncKey.equals(AutoDeviceMain.FUNC_LIST.print_check.name())) {
			m_oResponsePacket = m_oFuncPayAtTable.constructBasicResponsePacket();
			int iNextPackSeq = Integer.parseInt(m_oFuncPayAtTable.getNextPackSeq());
			
			if(iNextPackSeq == 1) {
				//open check
				if(!openCheck(m_oFuncPayAtTable.getTableNumWithoutExtension(), m_oFuncPayAtTable.getTableExtension(), 0)) {
					m_oResponsePacket.put("status", "0");
					m_oResponsePacket.put("errMessage", m_sLastErrorMessage);
					return true;
				}
				
				//apply discount if necessary
				if(!m_oFuncPayAtTable.getDiscountId().isEmpty()) {
					// Check if user has function authority
					int iFuncId = m_oFunctionList.getFunctionIdByKey(AppGlobal.FUNC_LIST.check_discount.name());
					if (!m_oFuncPayAtTable.checkUserAuthority(iFuncId, AppGlobal.FUNC_LIST.check_discount.name())) {
						unlockTable();
						m_sLastErrorMessage = m_oFuncPayAtTable.getLastErrorMessage();
						m_oResponsePacket.put("status", "0");
						m_oResponsePacket.put("errMessage", m_sLastErrorMessage);
						return false;
					}
					
					if(applyDiscount("check", m_oFuncPayAtTable.getDiscountId(), null) == false) {
						unlockTable();
						m_oResponsePacket.put("status", "0");
						m_oResponsePacket.put("errMessage", m_sLastErrorMessage);
						AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to apply check discount:"+m_oFuncPayAtTable.getDiscountId());
						return false;
					}
				}
				
				//print check
				String sGuestCheckUrl = "";
				if(!this.printCheck(m_oFuncPayAtTable.getTableNum(), m_oFuncPayAtTable.getDiscountId())) {
					unlockTable();
					m_oResponsePacket.put("status", "0");
					m_oResponsePacket.put("errMessage", m_sLastErrorMessage);
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to print check");
					return false;
				} else {
					sGuestCheckUrl = m_oFuncCheck.getGuestCheckUrl();
					m_oFuncPayAtTable.parseGuestCheck(sGuestCheckUrl);
				}
			}
			
			ArrayList<String> oGuestCheckStringList = m_oFuncPayAtTable.getGuestCheckStingList();
			m_oResponsePacket.put("totalPacketsNum", String.valueOf(oGuestCheckStringList.size()));
			m_oResponsePacket.put("currentPackSeq", String.valueOf(iNextPackSeq));
			m_oResponsePacket.put("checkPrintImage", oGuestCheckStringList.get(iNextPackSeq-1));
		} 
		// get payment amount information
		else if(sFuncKey.equals(AutoDeviceMain.FUNC_LIST.get_pay_amount.name())) {
			String sTableNo = "", sTableExtension = "";
			DateTimeFormatter oDateFormat = DateTimeFormat.forPattern("yyyyMMddHHmmss");
			if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE)) {
				sTableNo = m_oFuncPayAtTable.getTableNumWithoutExtension();
				sTableExtension = m_oFuncPayAtTable.getTableExtension();
			}
			
			if(!openCheck(sTableNo, sTableExtension, 0)) {
				m_oResponsePacket.put("status", "0");
				m_oResponsePacket.put("errMessage", m_sLastErrorMessage);
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", m_sLastErrorMessage);
				return false;
			}else {
				m_oResponsePacket.put("guestCheckNum", m_oFuncCheck.getCheckPrefixNo());
				m_oResponsePacket.put("tableNum", m_oFuncCheck.getTableNoWithExtensionForDisplay());
				m_oResponsePacket.put("checkTotalAmount", StringLib.BigDecimalToString(m_oFuncCheck.getCheckTotal(), 2));
				m_oResponsePacket.put("coversNum", Integer.toString(m_oFuncCheck.getCover()));
				m_oResponsePacket.put("merBillNum", oDateFormat.print(m_oFuncCheck.getOpenLocTime())+m_oFuncCheck.getCheckPrefixNo());
			}
		}
		// cancel payment
		else if(sFuncKey.equals(AutoDeviceMain.FUNC_LIST.cancel_payment.name())) {
			String sTableNo = "", sTableExtension = "", sDiscountCode = "", sDiscountVoidCode = "";
			if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE)) {
				sTableNo = m_oFuncPayAtTable.getTableNumWithoutExtension();
				sTableExtension = m_oFuncPayAtTable.getTableExtension();
				sDiscountCode = m_oFuncPayAtTable.getDiscountId();
				sDiscountVoidCode = m_oFuncPayAtTable.getDiscountVoidCode();
			}
			
			if(!this.cancelPayment(sTableNo, sTableExtension, sDiscountCode, sDiscountVoidCode)) {
				unlockTable();
				m_oResponsePacket.put("status", "0");
				m_oResponsePacket.put("errMessage", m_sLastErrorMessage);
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", m_sLastErrorMessage);
				return false;
			}
			
		}
		// paid check
		else if(sFuncKey.equals(AutoDeviceMain.FUNC_LIST.paid.name())) {
			m_oResponsePacket = m_oFuncPayAtTable.constructBasicResponsePacket();
			JSONObject oPaidCheckInfo = m_oFuncPayAtTable.getPaymentInfo();
			List<JSONObject> oPaidCheckList = new ArrayList<JSONObject>();
			oPaidCheckList.add(oPaidCheckInfo);
			boolean bResult = payChecks(oPaidCheckList);
			
			if(!bResult) {
				m_oResponsePacket.put("status", "0");
				m_oResponsePacket.put("errMessage", m_sLastErrorMessage);
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to paid check");
				return true;
			}else {
				String sPaymentRefer = m_oFuncPayAtTable.generatePaymentReference(oPaidCheckList.get(0).optString("closeTime", ""));
				m_oResponsePacket.put("paymentRefer", sPaymentRefer);
				return true;
			}
		// get payment info for release payment
		} else if(sFuncKey.equals(AutoDeviceMain.FUNC_LIST.get_release_payment_info.name())) {
			String sCheckNo = "";
			
			if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE)) 
				sCheckNo = m_oFuncPayAtTable.getCheckNum();
			
			if(!this.getReleasePaymentInfo(sCheckNo)) {
				m_oResponsePacket.put("status", "0");
				m_oResponsePacket.put("errMessage", m_sLastErrorMessage);
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to get release payment");
				return false;
			}
		//release payment
		} else if(sFuncKey.equals(AutoDeviceMain.FUNC_LIST.release_payment.name())) {
			String sCheckNo = "", sVoidReasonCode = "";
			if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE)) {
				sCheckNo = m_oFuncPayAtTable.getCheckNum();
				sVoidReasonCode = m_oFuncPayAtTable.getPaymentVoidCode();
			}
			
			/*if(!sCheckNo.equals(m_oFuncCheck.getCheckPrefixNo())) {
				//iStatus = 0;
				//sErrMsg = "Check no. not match";
				m_oResponsePacket.put("status", "0");
				m_oResponsePacket.put("errMessage", m_sLastErrorMessage);
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to apply check discount:"+m_oFuncPayAtTable.getDiscountId());
				return false;
			} else {*/
			if(!this.releasePayment(sCheckNo, sVoidReasonCode)) {
				m_oResponsePacket.put("status", "0");
				m_oResponsePacket.put("errMessage", m_sLastErrorMessage);
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to release payment");
				return false;
			}
		}
		
		return true;
	}
	
	// paid check
	private boolean payChecks(List<JSONObject> oPaidCheckList) {
		if(oPaidCheckList == null)
			return false;
		
		if(oPaidCheckList.size() == 0)
			return false;
		
		boolean bPaySuccess = true;
		for(JSONObject oPaidCheck: oPaidCheckList) {
			m_sLastErrorMessage = "";
			bPaySuccess = true;
			//get check id or table number
			String sCheckId = "";
			String sTable = "";
			String sTableExtension = "";
			if(oPaidCheck.has("table")) {
				sTable = oPaidCheck.optString("table", "");
				if(oPaidCheck.has("tableExtension"))
					sTableExtension = oPaidCheck.optString("tableExtension", "");
			}else if(oPaidCheck.has("checkId")) {
				sCheckId = oPaidCheck.optString("checkId", "");
				PosCheckTable oCheckTable = new PosCheckTable();
				oCheckTable.readByCheckId(sCheckId);
				sTable = oCheckTable.getTable()+oCheckTable.getTableExt();
			}
			
			//check whether check id exist for OGS interface
			if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_OGS) && oPaidCheck.optString("result").equals(FUNC_RESULT_FAIL)) {
				//print error slip
				m_oInterfaceConfig.printInterfaceAlertSlip(AppGlobal.g_oFuncStation.get().getStation().getReceiptPrtqId(), sCheckId, AppGlobal.g_oFuncOutlet.get().getOutletId(), false, 1, oPaidCheck.optString("errorCode", ""), oPaidCheck.optString("errorMessage", ""), AppGlobal.g_oCurrentLangIndex.get());
				
				//remove the check from list
				AppGlobal.removePrintedCheckToPaymentInterfaceCheckList(AppGlobal.g_oFuncOutlet.get().getOutletId(), sCheckId, oPaidCheck.optString("outTradeNumber", ""));
				continue;
			}
			
			if(!sTable.isEmpty()) {
				// open check
				if(openCheck(sTable, sTableExtension, 0) == false) {
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to open table:"+m_sLastErrorMessage);
					bPaySuccess = false;
					continue;
				}
				
				// check check ID
				if(!sCheckId.equals("")  && !m_oFuncCheck.getCheckId().equals(sCheckId)) {
					unlockTable();
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Check id not matched");
					bPaySuccess = false;
					continue;
				}
				
				// check check number
				if(oPaidCheck.has("checkNumber") && !oPaidCheck.optString("checkNumber").equals(m_oFuncCheck.getCheckPrefixNo())) {
					unlockTable();
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("check_number_not_matched");
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Check number not matched");
					bPaySuccess = false;
					continue;
				}
				
				//get payment code and ID
				List<HashMap<String, String>> oPaymentInfoList = new ArrayList<HashMap<String, String>>();
				boolean bPaymentInfoReady = false;
				if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_OGS)) {
					int iCheckInterfaceId = 0;
					int iCheckPaytype = 0;
					if(!(m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID)).isEmpty())
						iCheckInterfaceId = Integer.valueOf(m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID)).intValue();
					if(!(m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_PAYTYPE)).isEmpty())
						iCheckPaytype = Integer.valueOf(m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_PAYTYPE)).intValue();
					if(iCheckInterfaceId != m_oInterfaceConfig.getInterfaceId() || iCheckPaytype == 0) {
						unlockTable();
						AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Incorrect interface or paytype is zero");
						bPaySuccess = false;
						continue;
					}
				
					int iPaytype = iCheckPaytype;
					String sPaytypeKey = m_oPaymentInterfaceSupportedPaytype.get(Integer.valueOf(iPaytype));
					int iPaymentId = 0;
					String sPaymentCode = m_oInterfaceConfig.getInterfaceConfig().optJSONObject("payment_type_setup").optJSONObject("params").optJSONObject(sPaytypeKey+"_paytype").optString("value", "");
					if(sPaymentCode.isEmpty()) {
						unlockTable();
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_such_payment");
						AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "No such payment - code:"+sPaymentCode);
						bPaySuccess = false;
						continue;
					}
					HashMap<Integer, PosPaymentMethod> oPaymentMethodList = m_oFuncPayment.getPaymentMethodList().getPaymentMethodList();
					for(Entry<Integer, PosPaymentMethod> entry: oPaymentMethodList.entrySet()){
						if(entry.getValue().getPaymentCode().equals(sPaymentCode)) {
							iPaymentId = entry.getValue().getPaymId();
							break;
						}
					}
					if(iPaymentId == 0) {
						unlockTable();
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_such_payment");
						AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "No such payment - code:"+sPaymentCode);
						bPaySuccess = false;
						continue;
					}
					
					HashMap<String, String> oPaymentInfo = new HashMap<String, String>();
					oPaymentInfo.put("paymentId", String.valueOf(iPaymentId));
					oPaymentInfo.put("payTotal", oPaidCheck.optString("payTotal", "0.0"));
					oPaymentInfo.put("tipsAmount","0");
					oPaymentInfo.put("discountTotal", oPaidCheck.optString("discountTotal", "0.0"));
					oPaymentInfoList.add(oPaymentInfo);
					bPaymentInfoReady = true;
					
				}else if(oPaidCheck.has("paymentInfos") && oPaidCheck.optJSONArray("paymentInfos") != null){
					JSONArray oPaymentInfos = oPaidCheck.optJSONArray("paymentInfos");
					for(int i=0; i<oPaymentInfos.length(); i++) {
						int iPaymentId = 0;
						String sPaymentCode = oPaymentInfos.optJSONObject(i).optString("paymentMethodCode", "");
						if(sPaymentCode.isEmpty()) {
							m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_such_payment");
							AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "No such payment - code:"+sPaymentCode);
							break;
						}
						
						HashMap<Integer, PosPaymentMethod> oPaymentMethodList = m_oFuncPayment.getPaymentMethodList().getPaymentMethodList();
						for(Entry<Integer, PosPaymentMethod> entry: oPaymentMethodList.entrySet()){
							if(entry.getValue().getPaymentCode().equals(sPaymentCode)) {
								iPaymentId = entry.getValue().getPaymId();
								break;
							}
						}
						if(iPaymentId == 0) {
							m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_such_payment");
							AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "No such payment - code:"+sPaymentCode);
							break;
						}
						
						HashMap<String, String> oPaymentInfo = new HashMap<String, String>();
						oPaymentInfo.put("paymentType", oPaymentInfos.optJSONObject(i).optString("paymentType", ""));
						oPaymentInfo.put("paymentId", String.valueOf(iPaymentId));
						oPaymentInfo.put("payTotal", oPaymentInfos.optJSONObject(i).optString("payAmount", ""));
						oPaymentInfo.put("tipsAmount","0");
						oPaymentInfo.put("discountTotal", oPaymentInfos.optJSONObject(i).optString("discountAmount", "0.0"));
						if(oPaymentInfos.optJSONObject(i).has("totalTips"))
							oPaymentInfo.put("totalTips", oPaymentInfos.optJSONObject(i).optString("totalTips", "0.0"));
						if(oPaymentInfos.optJSONObject(i).has("couponCode"))
							oPaymentInfo.put("couponCode", oPaymentInfos.optJSONObject(i).optString("couponCode", ""));
						oPaymentInfoList.add(oPaymentInfo);
					}
					
					if(oPaymentInfoList.size() == oPaymentInfos.length())
						bPaymentInfoReady = true;
				}
				
				if(!bPaymentInfoReady) {
					bPaySuccess = false;
					continue;
				}
				
				// check whether member number provided
				if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE) && oPaidCheck.has("memberNumber")) {
					if(!m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0))
						m_oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0, oPaidCheck.optString("memberNumber", ""));
					else
						m_oFuncCheck.updateCheckExtraInfoValue(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0, oPaidCheck.optString("memberNumber", ""));
				}
				
				// paid check
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Paid check:"+m_oFuncCheck.getCheckPrefixNo());
				BigDecimal dOriCheckTotal = new BigDecimal(oPaidCheck.optString("checkTotal", "0.0"));
				if(payCheck(dOriCheckTotal) == false) {
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to init check payment:"+m_sLastErrorMessage);
					unlockTable();
					bPaySuccess = false;
					continue;
				}
				
				boolean bPaidSuccess = true;
				for(HashMap<String, String> oPaymentInfo: oPaymentInfoList) {
					int iPaymentId = Integer.valueOf(oPaymentInfo.get("paymentId")).intValue();
					BigDecimal dPaymentTotal = new BigDecimal(oPaymentInfo.get("payTotal"));
					BigDecimal dPayTips = BigDecimal.ZERO;
					if(oPaymentInfo.containsKey("totalTips"))
						dPayTips = new BigDecimal(oPaymentInfo.get("totalTips"));
					BigDecimal dDiscountTotal = BigDecimal.ZERO;
					if(oPaymentInfo.containsKey("discountTotal"))
						dDiscountTotal = new BigDecimal(oPaymentInfo.get("discountTotal"));
					
					//construct payment's extra info
					List<HashMap<String, String>> oPaymentExtraInfoList = new ArrayList<HashMap<String, String>>();
					if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE)) {
						HashMap<String, String> oTempHashMap = null;
						
						//interface id
						oTempHashMap = new HashMap<String, String>();
						oTempHashMap.put("section", PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE);
						oTempHashMap.put("variable", PosCheckExtraInfo.VARIABLE_INTERFACE_ID);
						oTempHashMap.put("value", String.valueOf(m_oInterfaceConfig.getInterfaceId()));
						oPaymentExtraInfoList.add(oTempHashMap);
						
						//hand terminal id
						if(oPaidCheck.has("handTerminalId")) {
							oTempHashMap = new HashMap<String, String>();
							oTempHashMap.put("section", PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE);
							oTempHashMap.put("variable", PosCheckExtraInfo.VARIABLE_HT_ID);
							oTempHashMap.put("value", oPaidCheck.optString("handTerminalId", ""));
							oPaymentExtraInfoList.add(oTempHashMap);
						}
						
						//payment reference
						String sTableWithExtension = sTable + sTableExtension;
						oTempHashMap = new HashMap<String, String>();
						oTempHashMap.put("section", PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE);
						oTempHashMap.put("variable", PosCheckExtraInfo.VARIABLE_REFERENCE);
						oTempHashMap.put("value", oPaymentInfo.get("paymentType")+StringLib.fillZeroAtBegin(sTableWithExtension, 6));
						oPaymentExtraInfoList.add(oTempHashMap);
						
						//coupon
						if(oPaymentInfo.containsKey("couponCode")) {
							oTempHashMap = new HashMap<String, String>();
							oTempHashMap.put("section", PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE);
							oTempHashMap.put("variable", PosCheckExtraInfo.VARIABLE_COUPON);
							oTempHashMap.put("value", oPaymentInfo.get("couponCode"));
							oPaymentExtraInfoList.add(oTempHashMap);
						}
					}
					
					if(handlePaymentKey(iPaymentId, "", dPaymentTotal, dPayTips , dDiscountTotal, oPaymentExtraInfoList) == false) {
						AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to handle payment key:"+m_sLastErrorMessage);
						bPaidSuccess = false;
						break;
					}
						
				}
				
				if(!bPaidSuccess) {
					bPaySuccess = false;
					if(!m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE))
						unlockTable();
					continue;
				}
				
				try {
					DateTime oCloseTime = m_oFuncCheck.getCloseLocTime();
					DateTimeFormatter fmt = DateTimeFormat.forPattern("yyMMddHHmmss"); 
					oPaidCheck.put("closeTime", fmt.print(oCloseTime));
				}catch(JSONException e) {}
				
				//print paid alert slip
				if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_OGS))
					m_oInterfaceConfig.printInterfaceAlertSlip(AppGlobal.g_oFuncStation.get().getStation().getReceiptPrtqId(), sCheckId, AppGlobal.g_oFuncOutlet.get().getOutletId(), true, 1, "", "", AppGlobal.g_oCurrentLangIndex.get());
				
				//remove the check from list
				if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_OGS))
					AppGlobal.removePrintedCheckToPaymentInterfaceCheckList(AppGlobal.g_oFuncOutlet.get().getOutletId(), m_oFuncCheck.getCheckId(), oPaidCheck.optString("outTradeNumber", ""));
				
			}else {
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Unable to find corresponding table record");
				bPaySuccess = false;
			}
		}
		
		return bPaySuccess;
	}
	
	private boolean openCheck(String sTableNo, String sTableExtension, int iDefaultCover) {
		//Pre-checking before open check
		if(!openCheckPreChecking()) {
			return false;
		}
		if(sTableNo.equals("")) {
			//Missing parameters
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("missing_table_no");
			return false;
		}
		
		// ***** DEBUG *****
		// Show memory usage
		if(AppGlobal.g_iDebugMode > 0)
			AppGlobal.showMemory();
		// *****************
		
		//Memory check
		AppGlobal.checkMemory();
		
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Start open check");
		
		m_oFuncCheck = new FuncCheck();
		m_oFuncCheck.addListener(this);
		//Init business date setup
		m_oFuncCheck.initBusinessDaySetup(AppGlobal.g_oFuncOutlet.get());
		
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Before update stock");
		
		//Check if period is defined
		String sPeriodId = AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId();
		
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Before lock table");
		
		//Try to lock table
		//For old check, load the old check
		if(m_oFuncCheck.lockTable(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), sPeriodId, AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), sTableNo, sTableExtension, true, false, PosCheck.ORDERING_MODE_FINE_DINING, "", false)) {
			if(AppGlobal.g_iLogLevel >= 9)
				AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "After lock table");
			
			//Load new check
			if(!m_oFuncCheck.isOldCheck()) {
				m_oFuncCheck.unlockTable(true, false);
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_load_new_check");
				return false;
			}
		}
		else {
			//Fail to lock table
			m_sLastErrorMessage = m_oFuncCheck.getLastErrorMessage();
			return false;
		}
		
		//New check
		if(!m_oFuncCheck.isOldCheck()) {
			m_oFuncCheck.unlockTable(true, false);
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_open_new_check");
			return false;
		}
		
		if(m_oFuncCheck.isOldCheck()) {
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Open old table " + sTableNo + "");
		}
		else {
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Open new table " + sTableNo + "");
		}
		
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Finish open check");
		
		return true;
	}
	
	private boolean printCheck(String sTableNo, String sDiscountCode) {
		// Calculate the Loyalty transaction
		m_oFuncCheck.calculateLoyaltyTransaction();
		
		String sResult = m_oFuncCheck.sendCheck(true, false, false, AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getOutletNameByIndex(AppGlobal.g_oCurrentLangIndex.get()), AppGlobal.g_oFuncStation.get().getCheckPrtqId(), AppGlobal.g_oFuncStation.get().getStation().getCheckPfmtId(1), false, 0, 0, 0, PosCheck.ORDERING_MODE_FINE_DINING, false, false);
		if(!sResult.equals(PosCheck.API_RESULT_SUCCESS)){
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_print_check");
			return false;
		}
		
		return true;
	}
	
	private boolean cancelPayment(String sTableNo, String sTableExtension, String sDiscountCode, String sVoidReasonCode) {
		if(openCheck(sTableNo, sTableExtension, 0) == false)
			return false;

		// void discount
		List<PosCheckDiscount> oAppliedCheckPartyDiscountList = m_oFuncCheck.getCurrentPartyAppliedCheckDiscount();
		if(!sDiscountCode.isEmpty() && !oAppliedCheckPartyDiscountList.isEmpty()) {
			PosVoidReason oVoidReason = new PosVoidReason();
			oVoidReason.readByTypeAndCode(PosVoidReason.TYPE_VOID_DISCOUNT, sVoidReasonCode);
			
			if(oVoidReason.getVdrsId() == 0) {
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_find_void_reason");
				return false;
			}
			
			int iDiscountIndex = 0;
			PosDiscountType oDiscountType = new PosDiscountType();
			oDiscountType.readByCode(sDiscountCode);
			
			if(oDiscountType.getDtypId() == 0) {
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_find_discount_type");
				return false;
			}
			
			for(PosCheckDiscount oCheckDiscount : oAppliedCheckPartyDiscountList) {
				if(oCheckDiscount.getDtypId() == oDiscountType.getDtypId()) {
					iDiscountIndex = oCheckDiscount.getSeq();
					break;
				}
			}
			
			if(iDiscountIndex == 0) {
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_find_check_discount");
				return false;
			}
			
			List<HashMap<String, Integer>> oUpdateItemIndexList = m_oFuncCheck.getSectionItemIndexWithAppliedCheckDiscount(iDiscountIndex);
			if(!m_oFuncCheck.voidDiscount("check", PosDiscountType.USED_FOR_DISCOUNT, oUpdateItemIndexList, iDiscountIndex, oVoidReason.getVdrsId(), oVoidReason.getName(AppGlobal.g_oCurrentLangIndex.get())))
				return false;
		}
		
		// unlock table
		this.unlockTable();
		
		String sTableNumWithExtension = sTableNo;
		if(!sTableExtension.isEmpty())
			sTableNumWithExtension = sTableNumWithExtension + sTableExtension;
		m_oResponsePacket.put("tableNum", sTableNumWithExtension);
		m_oResponsePacket.put("guestCheckNum", m_oFuncCheck.getCheckPrefixNo());
		
		return true;
	}
	
	private boolean getReleasePaymentInfo(String sCheckPrefixNum) {
		m_oFuncCheck = new FuncCheck();
		if(!m_oFuncCheck.getCheckByCheckNum(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), sCheckPrefixNum, true)) {
			// Fail to release payment
			m_sLastErrorMessage = m_oFuncCheck.getLastErrorMessage();
			return false;
		}
		
		// Init business date setup
		m_oFuncCheck.initBusinessDaySetup(AppGlobal.g_oFuncOutlet.get());

		if(!m_oFuncCheck.isPaid(false)) {
			// Fail to release payment
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("check_is_not_paid");
			return false;
		}

		// Check whether payment records exist
		if (m_oFuncCheck.getCheckTotal().compareTo(m_oFuncCheck.getPaymentRecordPayTotal()) != 0) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("check_is_not_saved_completely");
			return false;
		}
		
		// Check if there is Octopus payment
		// If yes, cannot release payment
		if(m_oFuncCheck.hasOctopusPayment()) {
			// There is Octopus payment
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("octopus_payment_cannot_be_voided");
			return false;
		}

		// Check if there is Rewrite Card payment
		// If yes, cannot release payment
		if(m_oFuncCheck.hasRewriteCardPayment()) {
			// There is Rewrite Card payment
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("rewrite_card_payment_cannot_be_voided");
			return false;
		}
		
		if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE)) {
			if(m_oFuncPayAtTable.checkPaymentReferenceForReleasePayment(m_oFuncCheck.getCheckPaymentList()) == false) {
				m_sLastErrorMessage = m_oFuncPayAtTable.getLastErrorMessage();
				return false;
			}else {
				m_oResponsePacket.put("guestCheckNum", m_oFuncCheck.getCheckPrefixNo());
				m_oResponsePacket.put("tableNum", m_oFuncCheck.getTableNoWithExtensionForDisplay());
			}
				
		}
		
		return true;
	}
	
	private boolean releasePayment(String sCheckPrefixNum, String sVoidReasonCode) {
		PosVoidReason oVoidReason = new PosVoidReason();
		oVoidReason.readByTypeAndCode(PosVoidReason.TYPE_VOID_PAYMENT, sVoidReasonCode);
		if(oVoidReason.getVdrsId() == 0) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_find_void_reason");
			return false;
		}
		
		FuncCheck oFuncCheck = new FuncCheck();
		if(!oFuncCheck.getCheckByCheckNum(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), sCheckPrefixNum, true)) {
			// Fail to release payment
			m_sLastErrorMessage = oFuncCheck.getLastErrorMessage();
			return false;
		}
		
		if(!AppGlobal.g_oFuncStation.get().isPartialPayment()) {
			if(!oFuncCheck.checkNegativeGalaxyPayment(-1)) {
				m_sLastErrorMessage = oFuncCheck.getLastErrorMessage();
				return false;
			}
		}
		
		if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE)) {
			if(m_oFuncPayAtTable.checkPaymentReferenceForReleasePayment(oFuncCheck.getCheckPaymentList()) == false) {
				oFuncCheck.unlockTable(false, false);
				m_sLastErrorMessage = m_oFuncPayAtTable.getLastErrorMessage();
				return false;
			}
		}
		
		String sTableNo = oFuncCheck.getTableNo();
		String sTableExtension = oFuncCheck.getTableExtension();
		
		// check if table is occupied
		FuncCheck oTmpFuncCheck = new FuncCheck();
		if(oTmpFuncCheck.isTableOccupied( AppGlobal.g_oFuncOutlet.get().getOutletId(), sTableNo, sTableExtension)) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("table_is_occupied");
			return false;
		}

		if(oFuncCheck.lockTable(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), sTableNo, sTableExtension, true, false, PosCheck.ORDERING_MODE_FINE_DINING, "", false) == true){
			boolean bVoidFullPayment = !AppGlobal.g_oFuncStation.get().isPartialPayment();
			if(oFuncCheck.releasePayment(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), sCheckPrefixNum, Integer.parseInt(sTableNo), sTableExtension, false, m_oFuncPayment.getPaymentMethodList(), oVoidReason, bVoidFullPayment, -1) == false){
				// Fail to release payment
				m_sLastErrorMessage = oFuncCheck.getLastErrorMessage();
				return false;
			}else{
				if(oFuncCheck.hasMember()){
					// Update member spending
					updateMemberSpending(oFuncCheck.getMemberId(), oFuncCheck.getCheckTotal().multiply(new BigDecimal("-1.0")));
				}
			}
		} else{
			// Error
			m_sLastErrorMessage = oFuncCheck.getLastErrorMessage();
			return false;
		}

		m_oResponsePacket.put("guestCheckNum", oFuncCheck.getCheckPrefixNo());
		m_oResponsePacket.put("tableNum", oFuncCheck.getTableNoWithExtensionForDisplay());
		return true;
	}
	
	private void unlockTable() {
		m_oFuncCheck.unlockTable(true, false);
	}
	
	// Process send check in Thread
	private boolean processSendCheck(FuncCheck oFuncCheck, boolean bPrintGuestCheck, boolean bPayCheck, int iOutletId, String sOutletName, int iCheckPrintQueueId, int iCheckFormatId, boolean bDetailCheck, String sStoredProcessingCheckKey, boolean bIsOldCheck){
		boolean bResult = false;

		// Send check
		try{
			String sOrderingMode;
			sOrderingMode = PosCheck.ORDERING_MODE_FINE_DINING;

			// Calculate the Loyalty transaction
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
	
	// Mix and match function
	private boolean mixAndMatchFunction(){
		if(AppGlobal.g_oFuncMixAndMatch.get().isSupportMixAndMatch() == false)
			// Not support mix and match
			return false;
		
		// Split multiple quantity new item to single quantity
		splitMultipleQtyNewItemToSingleQty();
		
		// Perform mix and match
		AppGlobal.g_oFuncMixAndMatch.get().processMixAndMatch(m_oFuncCheck);
		
		// Recalculate check
		m_oFuncCheck.calcCheck();
		
		// Update database for old check
		if(m_oFuncCheck.isOldCheck()){
			// Update old item price
			if(!m_oFuncCheck.updateCheck(false, true, PosCheckItem.SEND_MODE_OLD_ITEM, 0, false, 0, 0, 0, "", false, false, false, false, 0, 0, false, false).equals(PosCheck.API_RESULT_SUCCESS))
				return false;
		}
		
		return true;
	}
	
	// Split multiple quantity new item to single quantity
	private void splitMultipleQtyNewItemToSingleQty(){
		
		// Check if need split
		boolean bNeedSplit = false;
		for(int i = 0; i <= AppGlobal.MAX_SEATS; i++) {
			ArrayList<FuncCheckItem> oFuncCheckItemList = (ArrayList<FuncCheckItem>)m_oFuncCheck.getItemList(i);
			if(oFuncCheckItemList != null && oFuncCheckItemList.size() > 0) {
				for (int j=0; j<oFuncCheckItemList.size(); j++) {
					FuncCheckItem oFuncCheckItem = oFuncCheckItemList.get(j);
					if(oFuncCheckItem.isSetMenuItem()){
						// Not check child item
						continue;
					}
					
					if(oFuncCheckItem.getCheckItem().getQty().compareTo(BigDecimal.ONE) > 0){
						bNeedSplit = true;
						break;
					}
				}
				if(bNeedSplit)
					break;
			}
		}
		if(bNeedSplit == false)
			return;
		
		m_oFuncCheck.splitMultipleQtyNewItemToSingleQty();
	}
	
	// Before payment checking
	private boolean preCheckingForPayment(BigDecimal dOriCheckTotal){
		if(!m_oFuncCheck.isOldCheck()) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_pay_new_check");
			return false;
		}
		
		if(!m_oFuncCheck.isPrinted()) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("check_is_not_printed");
			return false;
		}
		
		if(m_oFuncCheck.isPaid(false)) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("check_is_already_paid");
			return false;
		}
		
		if(!m_oFuncCheck.isCheckTotalEqualToPrintTotal()) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("have_unprint_item");
			return false;
		}
		
		if(m_oFuncCheck.getCheckTotal().compareTo(dOriCheckTotal) != 0) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("target_payment_total_not_match");
			return false;
		}
		
		return true;
	}
	
	// Pay check
	private boolean payCheck(BigDecimal dOriCheckTotal) {
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.g_bWriteClientConnectionLog = true;
		
		if(preCheckingForPayment(dOriCheckTotal) == false)
			return false;
		
		// Mix and match function
		mixAndMatchFunction();
		
		// Init FuncPayment for payment process
		m_oFuncPayment.init(m_oFuncCheck.getCheckTotal(), AppGlobal.g_oFuncOutlet.get().getCheckRoundMethod(), AppGlobal.g_oFuncOutlet.get().getCheckRoundDecimal(), AppGlobal.g_oFuncOutlet.get().getPayRoundMethod(), AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal());
		
		return true;
	}
	
	public boolean handlePaymentKey(int iPaymId, String sParameter, BigDecimal dPaymentTotal, BigDecimal dPayTips, BigDecimal dDiscountTotal, List<HashMap<String, String>> oPaymentExtraInfoList){
		JSONObject oParameter = null;
		boolean bFullPayment = false;
		boolean bHaveValue = false;
	
		// Handle setup in button parameter
		if(sParameter.length() > 0){
			try {
				oParameter = new JSONObject(sParameter);
				if(oParameter.has("full_payment")){
					bFullPayment = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("error") + ":" + sParameter + ", " + AppGlobal.g_oLang.get()._("exception")+ ": " + AppGlobal.stackToString(e);
			}
		}
		
		// Add the payment to cashier mode
		if(preProcessEachPayment(iPaymId, dPayTips, dDiscountTotal, oPaymentExtraInfoList)){
			// Get payment amount from panel button or direct payment
			BigDecimal dPaymentAmount = BigDecimal.ZERO;
			BigDecimal dTipsAmount = dPayTips;
			
			if(bFullPayment){
				// Full payment
				dPaymentAmount = m_oFuncPayment.getCurrentBalance();
				bHaveValue = true;
			}else
			if(oParameter != null){
				// Handle setup in button parameter
				try {
					if(oParameter.has("default_amount")){
						dPaymentAmount = new BigDecimal(oParameter.getString("default_amount"));
						bHaveValue = true;
					}
				} catch (JSONException e) {
					e.printStackTrace();
					// Error setting
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Error parameter: " + sParameter + ", exception: " + AppGlobal.stackToString(e));
				}
			}else if(m_oInterfaceConfig.getInterfaceId() != 0){
				if(dPaymentTotal.compareTo(m_oFuncCheck.getCheckTotal()) > 0) {
					dTipsAmount = dPaymentTotal.subtract(m_oFuncCheck.getCheckTotal());
					dPaymentAmount = m_oFuncCheck.getCheckTotal();
				}else
					dPaymentAmount = dPaymentTotal;
				bHaveValue = true;
			}
			
			if(bHaveValue){
				// Finish asking amount, edit the payment amount stored in PosCheckPayment list of FuncPayment
				if(editPayment(0, m_oFuncPayment.getCheckPaymentList().size()-1, dPaymentAmount, dTipsAmount, false) == false)
					return false;
			}
			
		}else
			return false;
		
		return true;
	}
	
	public boolean preProcessEachPayment(int iPaymId, BigDecimal dTipsTotal, BigDecimal dDiscountValue, List<HashMap<String, String>> oPaymentExtraInfoList){
		PosPaymentMethodList oPosPaymentMethodList = m_oFuncPayment.getPaymentMethodList();
		BigDecimal dPayTotal = BigDecimal.ZERO;
		int iRet = 0, iEmployeeId = 0, iMemberId = 0;
		String[] sCheckPaymentRefData = new String[3];
		ArrayList<PosCheckExtraInfo> oCheckPaymentExtraInfos = new ArrayList<PosCheckExtraInfo>();

		for(int i=0; i<3; i++)
			sCheckPaymentRefData[i] = null;
		
		//get the payment method
		if(oPosPaymentMethodList.getPaymentMethodList().containsKey(iPaymId) == false){
			// No payment method is found
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_such_payment_method");
			return false;
		}
		PosPaymentMethod oPosPaymentMethod = oPosPaymentMethodList.getPaymentMethodList().get(iPaymId);
		
		//check whether have interface configure attached and interface module existence
		if(oPosPaymentMethod.hasInterfaceConfig()) {
			if(AppGlobal.isModuleSupport(AppGlobal.OPTIONAL_MODULE.pos_interface.name()) == false) {
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("interface_module_is_not_supported");
				return false;
			}
		}
		
		// Check if the payment non-revenue is match or not
		if(m_oFuncPayment.getCheckPaymentList().size() > 0){
			String sNonRevenue = m_oFuncPayment.getNonRevenue();
			if(!AppGlobal.g_oFuncStation.get().isAllowMixedPayment() && sNonRevenue.equals(oPosPaymentMethod.getNonRevenue()) == false){
				// Non revenue is not matched
				if(sNonRevenue.equals(PosCheckPayment.NON_REVENUE_YES)) 
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("non_revenue_check_with_revenue_payment");
				else 
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("revenue_check_with_non_revenue_payment");
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
		String sAutoDiscType = "item";
		
		//Waive SC and Tax as necessary
		if (oPosPaymentMethod.isAutoWaiveSc())
			bWaiveSC = true;
		
		if (oPosPaymentMethod.isAutoWaiveTax())
			bWaiveTax =true;
		
		//Check whether have auto discount and its discount type (item/check discount)
		if (!oPosPaymentMethod.getAutoDiscountTypeId().equals("")) {
			iAutoDiscId = Integer.valueOf(oPosPaymentMethod.getAutoDiscountTypeId());
			if(oPosPaymentMethod.isAutoCheckDiscountType())
				sAutoDiscType = "check";
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
				if(bWaiveTax)
					sChosenTax[i] = PosOverrideCondition.CHARGE_TAX_WAIVE;
				else
					sChosenTax[i] = PosOverrideCondition.CHARGE_TAX_NO_CHANGE;
			}
			if(!m_oFuncCheck.addWaiveScTax(false, oSelectedItem, bChosenSc, sChosenTax))
				return false;
		}
		
		//Check whether have auto discount
		if(dDiscountValue.compareTo(BigDecimal.ZERO) > 0 && iAutoDiscId == 0) {
			m_sLastErrorMessage = "no_discount_attached_to_payment"+": "+oPosPaymentMethod.getName(AppGlobal.g_oCurrentLangIndex.get());
			return false;
		}
		
		if (iAutoDiscId > 0) {
			String sApplyDiscountResult = autoDiscountForPayment(sAutoDiscType, iAutoDiscId, dDiscountValue);
			if(sApplyDiscountResult.equals(FormMain.FUNC_RESULT_NO_SUCH_RECORD)) {
				m_sLastErrorMessage = "no_such_discount"+": "+iAutoDiscId;
			}else if (sApplyDiscountResult.equals(FormMain.FUNC_RESULT_FAIL))
				return false;
		}
		
		if (bWaiveSC || bWaiveTax || iAutoDiscId > 0) {
			//update check total in cashier screen
			m_oFuncPayment.setCurrentBalance(m_oFuncCheck.getCheckTotal().subtract(m_oFuncPayment.getPaidBalance()));
		}
		
		// check payment need to ask PMS info
		if(oPosPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS) != null && oPosPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS).size() > 0) {
			List<PosInterfaceConfig> oPosInterfaceConfigs = oPosPaymentMethod.getInterfaceConfig(InfInterface.TYPE_PMS);
			
			for(PosInterfaceConfig oPosInterfaceConfig:oPosInterfaceConfigs) {
				if(FuncPMS.checkNeedAskInfo(oPosInterfaceConfig) == false) {
					if(!FuncPMS.haveDefaultAccount(oPosInterfaceConfig)) {
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_default_account_setup_for_direct_pms_posting");
						return false;
					}
					
					// PMS posting with default account		
					FuncPMS.createPaymentPMSExtraInfoForPostingWithDefaultAccount(oCheckPaymentExtraInfos, oPosInterfaceConfig, m_oFuncCheck.getTableNoWithExtensionForDisplay(), oPosPaymentMethod.getPaymentCode(), (m_oFuncPayment.getCheckPaymentList().size() + 1));
				}else {
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_support_pms_with_asking_info");
					return false;
				}
			}
		}
		
		// add payment extra info
		if(oPaymentExtraInfoList.size() > 0) {
			for(HashMap<String, String> oPaymentExtraInfo: oPaymentExtraInfoList) {
				PosCheckExtraInfo oPosCheckExtraInfo = new PosCheckExtraInfo();
				oPosCheckExtraInfo.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
				oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
				oPosCheckExtraInfo.setSection(oPaymentExtraInfo.get("section"));
				oPosCheckExtraInfo.setVariable(oPaymentExtraInfo.get("variable"));
				oPosCheckExtraInfo.setValue(oPaymentExtraInfo.get("value"));
				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
			}
		}
		
		// Add to check payment list first
		iRet = m_oFuncPayment.addPayment(iPaymId, AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), m_oFuncCheck.getCheckId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), dPayTotal, dTipsTotal, iEmployeeId, iMemberId, sCheckPaymentRefData, oCheckPaymentExtraInfos, 0);
		if(iRet < 0){
			// Fail to add payment
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_add_payment");
			return false;
		}
		
		return true;
	}
	
	public boolean editPayment(int iSectionId, int iItemIndex, BigDecimal dPaymentAmount, BigDecimal dTipsAmount, boolean bNotAllowFinishPayment){
		// Round the value
		dPaymentAmount = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dPaymentAmount);
		dTipsAmount = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dTipsAmount);
		
		boolean bNegativeCalculation = false;
		if(dPaymentAmount.compareTo(BigDecimal.ZERO) == 0)
			bNegativeCalculation = (m_oFuncPayment.getCurrentBalance().signum() < 0);
		
		int iRet = m_oFuncPayment.editPayment(iItemIndex, dPaymentAmount, dTipsAmount, null, null, bNegativeCalculation);
		if(iRet < 0){
			// Fail to add payment
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_add_payment");
			AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId() + "", "", "Fail to add payment");
			return false;
		}
		
		if(iRet == 2 && bNotAllowFinishPayment == false){
			// Finish all payments
			if(finishPayment() == false)
				return false;
		}
		
		return true;
	}
	
	public boolean finishPayment(){
		boolean bPass = true;
		int iChosenReceiptPfmtId = 0;
		DateTime oPaymentDateTime = AppGlobal.getCurrentTime(false);
		
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Start finish payment");
		
		boolean bSendAndPaid = false; 
		
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
			PosPaymentMethod oPaymentMethod = m_oFuncPayment.getPaymentMethodList().getPaymentMethodList().get(oPosCheckPayment.getPaymentMethodId());
			if(oFuncPMS.pmsPosting(m_oFuncCheck, oPosCheckPayment, oPaymentMethod, iPaymentSeq, dPreviousPaymentTotal, m_oFuncPayment.getCheckPaymentList(), null) == false) {
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
				
				//show PMS error message
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_post_pms");
				return false;
			}
			iSuccessPMSPaymentCount++;
			dPreviousPaymentTotal = dPreviousPaymentTotal.add(oPosCheckPayment.getPayTotal());
		}

		// Get VGS e-invoice QR code
		List<PosInterfaceConfig> oPosInterfaceConfigs = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PAYMENT_INTERFACE);
		for (PosInterfaceConfig oPosInterfaceConfig : oPosInterfaceConfigs) {
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
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_save_payments");
			AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Fail to save payments");
			bPass = false;
		}
		
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "After open drawer");
		
		// Build the list under FuncCheck to update payment
		for(PosCheckPayment oPosCheckPayment:m_oFuncPayment.getCheckPaymentList()){
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
		if (bPass) {
			boolean bResult = m_oFuncCheck.closeLoyaltyTransaction();
			System.out.println("bResult: "+bResult+", getLastErrorMessage: "+m_oFuncCheck.getLastErrorMessage());
			if (!bResult && !m_oFuncCheck.getLastErrorMessage().isEmpty()) {
				System.out.println("CLOSE ERROR");
			}
			
			bResult = m_oFuncCheck.closeLoyaltyTransaction();
			if (!bResult && !m_oFuncCheck.getLastErrorMessage().isEmpty()) {
				System.out.println("CLOSE ERROR");
			}
		}
		
		//get the current check number
		//**** it is for reference only ****
		if(bPass && m_oFuncCheck.updatePaymentInfo(AppGlobal.g_oFuncOutlet.get().getOutletId(), sPeriodId, oPaymentDateTime, AppGlobal.g_oFuncStation.get().getStation().getReceiptPrtqId(), iChosenReceiptPfmtId, bSendAndPaid, bIsFastFoodMode, bIsSelfOrderKioskMode, bIsBarTabMode, false, true, 0) == false){
			// Fail to add payment
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
		
		// Show the payment result
		String sCheckTotal = StringLib.BigDecimalToString(BigDecimal.ZERO, AppGlobal.g_oFuncOutlet.get().getCheckRoundDecimal());
		String sChangeTotal = StringLib.BigDecimalToString(BigDecimal.ZERO, AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal());
		String sTipsTotal = StringLib.BigDecimalToString(BigDecimal.ZERO, AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal());
		if(AppGlobal.g_oFuncOutlet.get().getCheckRoundDecimal() > AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal()){
			sCheckTotal = StringLib.BigDecimalToString(m_oFuncCheck.getCheckTotal(), AppGlobal.g_oFuncOutlet.get().getCheckRoundDecimal());
			if(m_oFuncPayment.getChangeTotal().compareTo(BigDecimal.ZERO) > 0)
				sChangeTotal = StringLib.BigDecimalToString(m_oFuncPayment.getChangeTotal(), AppGlobal.g_oFuncOutlet.get().getCheckRoundDecimal());
			if(m_oFuncPayment.getTipsTotal().compareTo(BigDecimal.ZERO) > 0)
				sTipsTotal = StringLib.BigDecimalToString(m_oFuncPayment.getTipsTotal(), AppGlobal.g_oFuncOutlet.get().getCheckRoundDecimal());
		}else{
			sCheckTotal = StringLib.BigDecimalToString(m_oFuncCheck.getCheckTotal(), AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal());
			if(m_oFuncPayment.getChangeTotal().compareTo(BigDecimal.ZERO) > 0)
				sChangeTotal = StringLib.BigDecimalToString(m_oFuncPayment.getChangeTotal(), AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal());
			if(m_oFuncPayment.getTipsTotal().compareTo(BigDecimal.ZERO) > 0)
				sTipsTotal = StringLib.BigDecimalToString(m_oFuncPayment.getTipsTotal(), AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal());
		}
		
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.g_bWriteClientConnectionLog = false;
		
		return true;
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
			m_sLastErrorMessage = "the_attached_discount_is_not_post_discount";
			return FUNC_RESULT_FAIL;
		}
		
		if(oDiscountType.isFixAmountDiscountPerCheckMethod() == false) {
			m_sLastErrorMessage = "the_attached_discount_is_not_fix_amount_check_discount";
			return FUNC_RESULT_FAIL;
		}
		
		if(oDiscountType.getFixAmount().compareTo(BigDecimal.ZERO) != 0) {
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
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("invalid_discount_value");
			return FUNC_RESULT_FAIL;
		}
		
		dDiscountRateAmt = dDiscountRateAmt.multiply(new BigDecimal("-1.0"));
		
		oSelectedItems = m_oFuncCheck.getSectionItemIndexForCurrentOrderedItem();
		
		// get the discount allowance
		List<Integer> oItemDiscountGrpList = new ArrayList<Integer>();
		HashMap<Integer, Boolean> oDiscountAllowance = new HashMap<Integer, Boolean>();
		for(HashMap<String, Integer> oSelectedItem:oSelectedItems) {
			FuncCheckItem oParentFuncCheckItem = m_oFuncCheck.getCheckItem(oSelectedItem.get("partySeq"), oSelectedItem.get("sectionId"), oSelectedItem.get("itemIndex"));

			// Pre-checking if the item is missing in menu
			if(oParentFuncCheckItem.getMenuItem() == null){
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("item_is_missing_in_menu_setup") + " (" + oParentFuncCheckItem.getItemDescriptionByIndex(AppGlobal.g_oCurrentLangIndex.get()) + ")";
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
			if(sDiscountType.equals("item") && oFuncCheckItem.getCheckItem().getTotal().compareTo(BigDecimal.ZERO) == 0) 
				continue;
			
			if(oFuncCheckItem.hasItemDiscount(false)) 
				continue;
			
			oItemIndexList.add(oSelectedItem);
		}
		
		if(!m_oFuncCheck.applyDiscount(sDiscountType, PosDiscountType.USED_FOR_DISCOUNT, oItemIndexList, oDiscountType, dDiscountRateAmt, null, 0))
			return FUNC_RESULT_FAIL;

		return FUNC_RESULT_SUCCESS;
	}
	
	private boolean applyDiscount(String sDiscountType, String sDiscountCode, List<HashMap<String, Integer>> oSelectedItems) {
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		PosBusinessDay oBusinessDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay();
		PosDiscountTypeList oDiscTypeList = new PosDiscountTypeList();
		PosDiscountType oChosenDiscountType = null;
		BigDecimal dDiscountRateAmt = BigDecimal.ZERO;
		
		// form the selection items list for check discount
		if(sDiscountType.equals("check")) {
			oSelectedItems = new ArrayList<HashMap<String, Integer>>();
			int iSeatNo = 0, iItemIndex = 0;
			List<List<FuncCheckItem>> oPartyWholeItems = m_oFuncCheck.getWholeItemList();
			for(List<FuncCheckItem> oItemListForSingleSeat:oPartyWholeItems) {
				for(iItemIndex=0; iItemIndex<oItemListForSingleSeat.size(); iItemIndex++) {
					HashMap<String, Integer> oTempSelectedItem = new HashMap<String, Integer>();
					oTempSelectedItem.put("partySeq", m_oFuncCheck.getCurrentCheckPartySeq());
					oTempSelectedItem.put("sectionId", iSeatNo);
					oTempSelectedItem.put("itemIndex", iItemIndex);
					oSelectedItems.add(oTempSelectedItem);
				}
				iSeatNo++;
			}
		}
		else if(oSelectedItems == null || oSelectedItems.size() == 0) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_selected_items");
			return false;
		}
		
		//find the discount type
		oDiscTypeList.readDiscountListByOutletId(sDiscountType, AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncStation.get().getStationId(), dateFormat.print(oBusinessDay.getDate()), oBusinessDay.isHoliday(), oBusinessDay.isDayBeforeHoliday(), oBusinessDay.isSpecialDay(), oBusinessDay.isDayBeforeSpecialDay(), oBusinessDay.getDayOfWeek(), AppGlobal.g_oFuncUser.get().getUserGroupList(), false);
		if(oDiscTypeList.getPosDiscountTypeList().size() == 0) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_discount_available");
			return false;
		}
		
		for(PosDiscountType oDiscountType:oDiscTypeList.getPosDiscountTypeList()) {
			if(oDiscountType.getCode().equals(sDiscountCode)) {
				oChosenDiscountType = oDiscountType;
				break;
			}
		}
		if(oChosenDiscountType == null) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("discount_not_found")+":"+sDiscountCode;
			return false;
		}
		
		//check discount whether open discount
		if(oChosenDiscountType.isFixAmountDiscountPerItemMethod()) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_apply_item_discount");
			return false;
		}
		if(oChosenDiscountType.isFixAmountDiscountPerCheckMethod()){
			if(oChosenDiscountType.getFixAmount().compareTo(BigDecimal.ZERO) == 0) {
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_apply_open_discount");
				return false;
			}else
				dDiscountRateAmt = oChosenDiscountType.getFixAmount();
		}
		if(oChosenDiscountType.isPercentageDiscountMethod()) {
			if(oChosenDiscountType.getRate().compareTo(BigDecimal.ZERO) == 0) {
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_apply_open_discount");
				return false;
			}else
				dDiscountRateAmt = oChosenDiscountType.getRate();
		}
		
		//get allowance
		// get the discount allowance
		List<Integer> oItemDiscountGrpList = new ArrayList<Integer>();
		HashMap<Integer, Boolean> oDiscountAllowance = new HashMap<Integer, Boolean>();
		for(HashMap<String, Integer> oSelectedItem:oSelectedItems) {
			FuncCheckItem oParentFuncCheckItem = m_oFuncCheck.getCheckItem(oSelectedItem.get("partySeq"), oSelectedItem.get("sectionId"), oSelectedItem.get("itemIndex"));

			// Pre-checking if the item is missing in menu
			if(oParentFuncCheckItem.getMenuItem() == null){
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("item_is_missing_in_menu_setup") + " (" + oParentFuncCheckItem.getItemDescriptionByIndex(AppGlobal.g_oCurrentLangIndex.get()) + ")";
				return false;
			}
			
			if(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId() != 0 && !oItemDiscountGrpList.contains(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId())) {
				boolean bDiscountAllowance = AppGlobal.g_oFuncDiscountAclList.get(AppGlobal.g_oFuncOutlet.get().getOutletId()).checkDiscountAcl(oParentFuncCheckItem.getMenuItem(), oChosenDiscountType);
				oDiscountAllowance.put(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId(), bDiscountAllowance);
			}
		}
		
		// check and prepare the item list for applying discount
		List<HashMap<String, Integer>> oItemIndexList = new ArrayList<HashMap<String, Integer>>();
		for(HashMap<String, Integer> oSelectedItem:oSelectedItems) {
			FuncCheckItem oParentFuncCheckItem = m_oFuncCheck.getCheckItem(oSelectedItem.get("partySeq"), oSelectedItem.get("sectionId"), oSelectedItem.get("itemIndex"));
			
			if(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId() == 0)
				continue;
			if(!oDiscountAllowance.containsKey(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId()))
				continue;
			if(oDiscountAllowance.get(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId()) == false)
				continue;
			
			//check whether the selected item is available for applying discount
			if(oParentFuncCheckItem.getCheckItem().getTotal().compareTo(BigDecimal.ZERO) == 0)
				continue;
			
			if(sDiscountType.equals("item") && oParentFuncCheckItem.hasItemDiscount(true))
				continue;
			
			oItemIndexList.add(oSelectedItem);
		}
		
		if(!m_oFuncCheck.applyDiscount(sDiscountType, PosDiscountType.USED_FOR_DISCOUNT, oItemIndexList, oChosenDiscountType, dDiscountRateAmt, null, 0)) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_apply_discount");
			return false;
		}
		
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
	
	private void processUpdateMemberSpending(int iMemberId, BigDecimal dPayAmount){
		MemMemberModuleInfo oMemberModuleInfo = new MemMemberModuleInfo();
		
		if(oMemberModuleInfo.updateInfo(iMemberId, MemMemberModuleInfo.MODULE_POS, MemMemberModuleInfo.VARIABLE_LIFE_TIME_SPENDING, dPayAmount.toPlainString(), MemMemberModuleInfo.UPDATE_METHOD_TYPE_ADD) == false){
			// Fail to update member spending
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to update member(" + iMemberId + ") spending = " + dPayAmount.toPlainString());
		}
	}
	
	//Pre-checking for open check process
	private boolean openCheckPreChecking() {
		// Check if period is defined
		if(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod() == null || AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId().compareTo("") == 0){
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("missing_period_setting");
			return false;
		}
		
		return true;
	}
	
	@Override
	public void FuncCheck_updateItemStockQty(int iItemId) {
	}

	@Override
	public void FuncCheck_finishSendCheck(String sStoredProcessingCheckKey) {
		//Finish send check, remove process to stored processing check list
		//this.removeProcessCheck(sStoredProcessingCheckKey);
	}
	
	private void setTerminate(boolean bTerminate) {
		m_bTerminate = true;
	}
	
	synchronized private void addProcessCheck(String sStoredProcessingCheckKey){
		m_oProcessingSendChecks.put(sStoredProcessingCheckKey, AppGlobal.getCurrentTime(false));
	}
	
	synchronized private void removeProcessCheck(String sStoredProcessingCheckKey){
		// Finish send check, remove process to stored processing check list
		if(m_oProcessingSendChecks.containsKey(sStoredProcessingCheckKey))
			m_oProcessingSendChecks.remove(sStoredProcessingCheckKey);
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
							", new item count : " + m_oFuncCheck.getNewItemCount(false).toPlainString() + ">     ");
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
	
	private boolean systemLogin(String sLogin, String sPassword, String sCardNumber) {
		FuncUser oFuncUser = new FuncUser();
		if(!sCardNumber.isEmpty()) {
			if(!oFuncUser.switchUserByEmployeeCard(sCardNumber))
				return false;
		}else {
			if(!oFuncUser.login(sLogin, sPassword, true).isEmpty())
				return false;
		}
		
		if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE))
			m_oFuncPayAtTable.userLogin(oFuncUser);
		
		return true;
	}
	
	private boolean systemLogout(String sLogin) {
		if(m_oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE))
			return m_oFuncPayAtTable.userLogout(sLogin);
		
		return false;
	}
	private boolean writeToClient(int iClientSocketId, JSONObject oResponsePacket, boolean bCloseSocket) throws UnsupportedEncodingException {
		JSONObject oInterfaceSetup = m_oInterfaceConfig.getInterfaceConfig();
		String sCharSet = oInterfaceSetup.optJSONObject("general_setup").optJSONObject("params").optJSONObject("pax_locale").optString("value");
		String sResponseString = oResponsePacket.toString();
		
		//String handling for printing
		if(m_oFuncPayAtTable.isPrintCheckFunction()) 
			sResponseString = m_oFuncPayAtTable.handleSpecialCharacterForCheckImage(sResponseString);

		String sMD5Msg = getMd5Message(sResponseString);
		
		int iLen = 0;
		iLen = sResponseString.getBytes(sCharSet).length + sMD5Msg.length();
		
		// get byte value of packet size
		byte[] oDataResponseBytes = m_oFuncPayAtTable.generateResponseByte(iLen, sResponseString.getBytes(sCharSet), sMD5Msg.getBytes());
		
		if(!m_oTCPLibForPayAtTable.writePacketByBytes(iClientSocketId, oDataResponseBytes)) 
			return false;
		AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Write >>>> "+new String(oDataResponseBytes));
		
		//Close socket and clear packet
		if(bCloseSocket) {
			m_oTCPLibForPayAtTable.closeClient(iClientSocketId);
			m_oResponsePacket.clear();
		}
		
		return true;
	}
	
	public String getMd5Message(String sMessage) {
		String hashtext = "";
		MessageDigest m;
		try {
			JSONObject oInterfaceSetup = m_oInterfaceConfig.getInterfaceConfig();
			String sProtocolStandardString = oInterfaceSetup.optJSONObject("general_setup").optJSONObject("params").optJSONObject("protocol_standard_string").optString("value");
			String sCharSet = oInterfaceSetup.optJSONObject("general_setup").optJSONObject("params").optJSONObject("pax_locale").optString("value");
			String sResultStr = sMessage + sProtocolStandardString;
			m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(sResultStr.getBytes(sCharSet));
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			hashtext = bigInt.toString(16);
			// Now we need to zero pad it if you actually want the full 32 chars.
			while(hashtext.length() < 32 ){
			  hashtext = "0"+hashtext;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return hashtext;
	}
	
	public boolean isJSONObject(String sStr) {
		try {
			new JSONObject(sStr);
		} catch (JSONException ex) {
			return false;
		}
		return true;
	}

	@Override
	public boolean FuncCheck_creditCardSpectraVoidPayment(PosCheckPayment oCheckPayment, String sCreditCardMethodType, boolean bIsFirstPosting) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean FuncCheck_creditCardSpectraAdjustTips(String sTraceNo, BigDecimal oNewPayTotal, BigDecimal oNewTips, boolean bIsFirstPosting) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean FuncCheck_creditCardSpectraDccOptOut(String sChksCheckPrefixNum, String sTraceNum, JSONObject oRefDataJSONObject, boolean bIsFirstPosting) {
		// TODO Auto-generated method stub
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
