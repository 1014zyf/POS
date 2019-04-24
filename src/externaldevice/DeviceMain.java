package externaldevice;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import lang.LangResource;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import om.MemMember;
import om.MemMemberList;
import om.MemMemberModuleInfo;
import om.MenuItem;
import om.MenuItemCourse;
import om.MenuItemCourseList;
import om.MenuMenu;
import om.MenuSetMenuLookup;
import om.MenuSetMenuLookupList;
import om.OmWsClient;
import om.OmWsClientGlobal;
import om.OutSpecialHour;
import om.PosActionLog;
import om.PosBusinessDay;
import om.PosBusinessDayList;
import om.PosCheck;
import om.PosCheckDiscount;
import om.PosCheckDiscountItem;
import om.PosCheckExtraInfo;
import om.PosCheckItem;
import om.PosCheckItemList;
import om.PosCheckParty;
import om.PosCheckPayment;
import om.PosCheckTable;
import om.PosConfigList;
import om.PosDiscountType;
import om.PosFunction;
import om.PosFunctionAclCache;
import om.PosFunctionList;
import om.PosInterfaceConfigList;
import om.PosOutletItem;
import om.PosOutletItemList;
import om.PosOverrideCondition;
import om.PosStation;
import om.PosStationDevice;
import om.PosVoidReason;
import om.PosPaymentMethod;
import om.SystemConfig;
import om.SystemConfigList;
import om.UserUser;
import om.PosPaymentMethodList;
import app.AppGlobal;
import app.AppThread;
import app.AppThreadManager;
import app.ClsActiveClient;
import app.FormMain;
import app.FrameSearchMemberFunction;
import app.FuncActionLog;
import app.FuncCheck;
import app.FuncCheckItem;
import app.FuncCheckListener;
import app.FuncDiscountAcl;
import app.FuncMenu;
import app.FuncMenuItem;
import app.FuncMessageQueue;
import app.FuncMixAndMatch;
import app.FuncOctopus;
import app.FuncOutlet;
import app.FuncOverride;
import app.FuncPayment;
import app.FuncStation;
import app.FuncUser;
import externallib.IniReader;
import externallib.StringLib;
import externallib.TCPLib;

public class DeviceMain implements FuncCheckListener {
	public enum FUNC_LIST{send_check, check_table_exist, get_check_information, guest_check_preview, paid, get_soldout_item_list, get_item_count_list, apply_discount, apply_extra_charge, print_check, get_all_opened_checks, adjust_tips, send_and_pay, lock_table, unlock_table};
	
	// *********************************
	// For internal use, stored the processing send check
	private HashMap<String, DateTime> m_oProcessingSendChecks;
	// *********************************
	
	private boolean m_bBreakListen;
	private HashMap<String, Object> m_oResponsePacket;
	
	// Current quantity
	private BigDecimal m_dQty;
	
	// Function list
	private PosFunctionList m_oFunctionList;
	private FuncCheck m_oFuncCheck;
	private FuncPayment m_oFuncPayment;
	// Business Day ID list of a month
	private ArrayList<String> m_oBusinessDayOfAMonthList;
	
	// Business Day ID list of a day
	private ArrayList<String> m_oBusinessDayOfADayList;
	
	// Flag to determine if the business date is correct or not
	
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
	
	private List<DeviceThreadMain> m_oCheckListForPortalStation;
	
	private String m_sStationAddress;
	private String m_sEmployeeId;
	private String m_sEmployeePwd;
	
	//TODO: use error code instead
	private String m_sLastErrorCode;
	private String m_sLastErrorMessage;
	
	//Message queue for portal station
	private FuncMessageQueue m_oFuncMessageQueue;
	private String m_sRequestQueueName;
	private AppThread m_oRequestHandler;
	
	public static String FUNC_RESULT_SUCCESS = "s";
	public static String FUNC_RESULT_CANCEL_BY_USER = "c";
	public static String FUNC_RESULT_FAIL = "f";
	public static String FUNC_RESULT_NO_SUCH_RECORD = "r";
	
	public DeviceMain(String sStationAddress, String sDisplayMode) {
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
		
		this.m_sStationAddress = sStationAddress;
		
		m_oFuncPayment = new FuncPayment();
		
		m_oCheckListForPortalStation = new ArrayList<DeviceThreadMain>();
		
		m_sLastErrorCode = "";
		m_sLastErrorMessage = "";
		
//		System.out.println("||Thread id: " + Thread.currentThread().getId() + "|");
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
			if(!sErrorMsg.equals("")) {
				//Logout session
				m_sLastErrorMessage = sErrorMsg;
				AppGlobal.g_oFuncUser.get().logout();
				AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", m_sLastErrorMessage);
				return false;
			}
			
			sErrorMsg = loadOutletConfigSetup(AppGlobal.g_oFuncStation.get().getOutletId());
			if(!sErrorMsg.equals("")) {
				if (!(AppGlobal.g_oFuncStation.get().isPortalStation() && (sErrorMsg.equals(AppGlobal.g_oLang.get()._("daily_start_has_not_been_carried_out")) || sErrorMsg.contains(AppGlobal.g_oLang.get()._("daily_close_not_yet_completed"))))) {
					//Logout session
					m_sLastErrorMessage = sErrorMsg;
					AppGlobal.g_oFuncUser.get().logout();
					AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", m_sLastErrorMessage);
					return false;
				}
				AppGlobal.startKillSingleStation(AppGlobal.g_oFuncStation.get().getStationId(), sErrorMsg);
			}
			
			//Initial internal usage variable
			m_oProcessingSendChecks = new HashMap<String, DateTime>();
			m_oResponsePacket = new HashMap<String, Object>();
			m_oStoredSetMenuLookupList = new HashMap<Integer, MenuSetMenuLookupList>();
			
			String sClientIPAddress = "127.0.0.1";
			//Start server, listen and wait for client
			m_sLastErrorMessage = AppGlobal.g_oTCP.get().initServer(sClientIPAddress, iPortNo, false);
			if(!m_sLastErrorMessage.equals("")) {
				//Init port failed, return error
				AppGlobal.g_oFuncUser.get().logout();
				AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", m_sLastErrorMessage);
				return false;
			}
			
			// Write application log
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Function: login");
			
			//Add the information of the current station(station ID and outlet ID) to a global list for killing purpose
			if(oActiveClient != null){
				if (!oActiveClient.isPortalStation())	// if station is portal station, no need to set it to auto station type
					oActiveClient.setAutoStation(true);
				oActiveClient.setCurrentStationId(AppGlobal.g_oFuncStation.get().getStationId());
				oActiveClient.setCurrentOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
			}
			
			//Call message queue
			m_oFuncMessageQueue = null;
			m_sRequestQueueName = "";
			boolean bMQConnectionResult = false;
			String sCreateQueueResult = "";
			boolean bConsumeQueueResult = false;
			if(oActiveClient.isPortalStation()) {
				HashMap<String, String> oMessageQueueSetupList = this.loadMessageQueueSetup();
				String sLogin = (oMessageQueueSetupList.containsKey("login")) ? oMessageQueueSetupList.get("login") : "";
				String sPassword = (oMessageQueueSetupList.containsKey("password")) ? oMessageQueueSetupList.get("password") : "";
				String sPath = (oMessageQueueSetupList.containsKey("path")) ? oMessageQueueSetupList.get("path") : "";
				String sNode = (oMessageQueueSetupList.containsKey("node")) ? oMessageQueueSetupList.get("node") : "";
					
				if(!sLogin.isEmpty() && !sPassword.isEmpty()) {
					m_oFuncMessageQueue = new FuncMessageQueue();
					bMQConnectionResult = m_oFuncMessageQueue.initConnection();
					if(bMQConnectionResult) {
						String sProfileId = getLicenseProfileId();
						m_sRequestQueueName = "POS_portal_station_" + sProfileId + "_" + AppGlobal.g_oFuncOutlet.get().getShopCode() + "_" + AppGlobal.g_oFuncOutlet.get().getOutletCode();
						
						//define portal request handler
						m_oRequestHandler = new AppThread(new AppThreadManager(), this, "portalRequestHandling", null);
						
						sCreateQueueResult = m_oFuncMessageQueue.createQueue(m_sRequestQueueName, "", "", false);
						bConsumeQueueResult = m_oFuncMessageQueue.consumeQueue(m_sRequestQueueName, m_oRequestHandler);
						
						if(sCreateQueueResult.isEmpty() || !bConsumeQueueResult)
							AppGlobal.g_lCurrentConnectClientList.remove(Thread.currentThread().getId());
					}
					else
						AppGlobal.g_lCurrentConnectClientList.remove(Thread.currentThread().getId());
				}
			}
			
			//Selector
			Selector oSelector = null;
			try {
				oSelector = SelectorProvider.provider().openSelector();
			}
			catch (Exception e) {
				//Internal error
				AppGlobal.g_oFuncUser.get().logout();
				AppGlobal.stack2Log(e);
				return false;
			}
			
			//Register the server socket channel
			ServerSocketChannel oChannel = AppGlobal.g_oTCP.get().getSocketChannel();
			SelectionKey oClientIncomingRequestKey = null;
			try {
				oClientIncomingRequestKey = oChannel.register(oSelector, SelectionKey.OP_ACCEPT);
			}
			catch (ClosedChannelException ce) {
				AppGlobal.g_oFuncUser.get().logout();
				AppGlobal.stack2Log(ce);
				return false;
			}
			catch (Exception e) {
				AppGlobal.g_oFuncUser.get().logout();
				AppGlobal.stack2Log(e);
				return false;
			}
			
			//Initialize the selector for TCP connection
			AppGlobal.g_oSelectorForTCP.set(oSelector);
			AppGlobal.g_oSelectorKeyForTCP.set(oClientIncomingRequestKey);
			
			if(bMQConnectionResult && !sCreateQueueResult.isEmpty() && bConsumeQueueResult)
				AppGlobal.addStationToAutoAndPortalStationList(AppGlobal.g_oFuncStation.get().getStationId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
			else {
				AppGlobal.g_oFuncUser.get().logout();
				if(!bMQConnectionResult)
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Failed to create MQ connection");
				else if(sCreateQueueResult.isEmpty())
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Failed to create MQ queue");
				else if(!bConsumeQueueResult)
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Failed to consume MQ queue");
				return false;
			}
			
			m_bBreakListen = false;
			int iCounter = 0;
			while (!m_bBreakListen) {
				int n = 0;
				iCounter += 1;
				try {
					n = AppGlobal.g_oSelectorForTCP.get().select(100);
					// Check in every 3 minutes
					if(iCounter % 1800 == 0) {
						AppGlobal.checkBusinessday(PosStationDevice.KEY_PORTAL_STATION);
						iCounter = 0;
					}
					// Interval for checking of some business logic (e.g. be killed by other station during daily start/close/reload setting)
					intermediateBusinessChecking();
					
					// Check if kill signal is received
					String sKillReason = AppGlobal.getKilledReason();
					if(sKillReason.length() > 0) {
						// Station is killed
						// break the loop
						AppGlobal.g_oFuncUser.get().logout();
						AppGlobal.finishBeingKilled();
						AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Receive killing signal : " + sKillReason);
						m_bBreakListen = true;
						continue;
					}
					
					// Get JSONObject from message queue
					HashMap<String, String> oMessageQueueSetupList = this.loadMessageQueueSetup();
					if (!oMessageQueueSetupList.isEmpty()) {
						m_oResponsePacket.put("funckey", "");
						m_oResponsePacket.put("id", 0);
						
						//Check kill reason
						String sKillReason1 = AppGlobal.getKilledReason();
						if(sKillReason1.length() > 0) {
							//should kill myself, return error to ipad first, then die
							HashMap<String, Object> oErrorHashMap = new HashMap<String, Object>();
							oErrorHashMap.put("code", "-32601");
							oErrorHashMap.put("message", sKillReason1);
							m_oResponsePacket.put("error", oErrorHashMap);
							
							m_oFuncMessageQueue.closeConnection();
							AppGlobal.g_oFuncUser.get().logout();
							AppGlobal.finishBeingKilled();
							m_bBreakListen = true;
							
							continue;
						}
					}
				}
				catch (IOException ioe) {
					AppGlobal.g_oFuncUser.get().logout();
					AppGlobal.stack2Log(ioe);
					return false;
				}
				
				// Interval for checking of some business logic (e.g. be killed by other station during daily start/close/reload setting)
				intermediateBusinessChecking();
				
				// Check if kill signal is received
				String sKillReason = AppGlobal.getKilledReason();
				if(sKillReason.length() > 0) {
					// Station is killed
					// break the loop
					AppGlobal.g_oFuncUser.get().logout();
					AppGlobal.finishBeingKilled();
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Receive killing signal : " + sKillReason);
					m_bBreakListen = true;
					continue;
				}
				
				if(n == 0) {
					continue;
				}
				java.util.Iterator<SelectionKey> iterator = AppGlobal.g_oSelectorForTCP.get().selectedKeys().iterator();
				while(iterator.hasNext()) {
					SelectionKey oIncomingSelectionKey = (SelectionKey)iterator.next();
					iterator.remove();
					
					//Client connect
					if(oIncomingSelectionKey.isAcceptable() && oIncomingSelectionKey == AppGlobal.g_oSelectorKeyForTCP.get()) {
						int iClientSockId = AppGlobal.g_oTCP.get().listen();
						if(iClientSockId > 0) {
//							System.out.println("Port: |" + AppGlobal.g_oTCP.get().getServerPort() + "|id: |" + iClientSockId + "|" + AppGlobal.g_oTCP.get().getPacket());
							
							JSONObject recvJSONObj = null;
							JSONObject tempJSONObj = null;
							try {
								if(AppGlobal.g_oTCP.get().getPacket().length() == 0)
									continue;
								
								m_sLastErrorCode = "";
								m_sLastErrorMessage = "";
								m_oResponsePacket.clear();
								
								recvJSONObj = new JSONObject(AppGlobal.g_oTCP.get().getPacket());
								String sFuncKey = recvJSONObj.optString("funckey");
								if(sFuncKey.equals(""))
									continue;
								String sPacketId = recvJSONObj.optString("id");
								m_oResponsePacket.put("funckey", sFuncKey);
								m_oResponsePacket.put("id", sPacketId);
								
								//Check kill reason
								sKillReason = AppGlobal.getKilledReason();
								if(sKillReason.length() > 0) {
									//should kill myself, return error to ipad first, then die
									HashMap<String, Object> oErrorHashMap = new HashMap<String, Object>();
									oErrorHashMap.put("code", "-32601");
									oErrorHashMap.put("message", sKillReason);
									m_oResponsePacket.put("error", oErrorHashMap);
//									System.out.println("Response result |" + m_oResponsePacket + "|");
									
									tempJSONObj = new JSONObject(m_oResponsePacket);
									writeToClient(iClientSockId, tempJSONObj);
									
									AppGlobal.g_oFuncUser.get().logout();
									AppGlobal.finishBeingKilled();
									AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Receive killing signal : " + sKillReason);
									m_bBreakListen = true;
									continue;
								}
								
								JSONObject oParamsJSONObj = recvJSONObj.optJSONObject("params");
								if(oParamsJSONObj == null) {
									//Missing parameter
									HashMap<String, Object> oErrorHashMap = new HashMap<String, Object>();
									oErrorHashMap.put("code", "-32602");
									oErrorHashMap.put("message", AppGlobal.g_oLang.get()._("missing_parameters"));
									m_oResponsePacket.put("error", oErrorHashMap);
//									System.out.println("Response result |" + m_oResponsePacket + "|");
									
									tempJSONObj = new JSONObject(m_oResponsePacket);
									writeToClient(iClientSockId, tempJSONObj);
									continue;
								}
								
								//Set language index according to external device
								if(!oParamsJSONObj.optString("langidx").equals("")) {
									String sLangCode = "en";
									int iLangIdx = oParamsJSONObj.optInt("langidx", 1);
									
									for(HashMap<String, String> oLangInfo : AppGlobal.g_oSupportedLangList) {
										int iTmp = Integer.parseInt(oLangInfo.get("index"));
										if(iTmp == iLangIdx) {
											sLangCode = oLangInfo.get("code");
											break;
										}
									}
									
									AppGlobal.g_oCurrentLangIndex.set(new Integer(iLangIdx));
									AppGlobal.g_oLang.get().switchLocale(sLangCode);
								}
								
//								System.out.println("Employee Code |" + oParamsJSONObj.optString("empid") + "|, pw |" + oParamsJSONObj.optString("emppw") + "|");
								String sEmpId = oParamsJSONObj.optString("empid");
								String sEmpPwd = oParamsJSONObj.optString("emppw");
								
								if(sEmpPwd == null || sEmpPwd.length() == 0)
									sEmpPwd = oParamsJSONObj.optString("emppw_plain");
								
								if(!sEmpId.equals(m_sEmployeeId) || !sEmpPwd.equals(m_sEmployeePwd)) {
									//Use employee id and pw login system if it has changed
									FuncUser oFuncUser = processLogin(sEmpId, sEmpPwd);
									if(oFuncUser == null) {
										//Invalid employee
										HashMap<String, Object> oErrorHashMap = new HashMap<String, Object>();
										oErrorHashMap.put("code", "-32011");
										oErrorHashMap.put("message", AppGlobal.g_oLang.get()._("no_such_employee"));
										m_oResponsePacket.put("error", oErrorHashMap);
										
										tempJSONObj = new JSONObject(m_oResponsePacket);
										writeToClient(iClientSockId, tempJSONObj);
										continue;
									}
									//Assign the user to global
									AppGlobal.g_oFuncUser.set(oFuncUser);
									
									m_sEmployeeId = sEmpId;
									m_sEmployeePwd = sEmpPwd;
								}
								
								fcnSwitch(sFuncKey, oParamsJSONObj);
								tempJSONObj = new JSONObject(m_oResponsePacket);
								if(!writeToClient(iClientSockId, tempJSONObj)) {
									//Fail to write to client
									//Write error log
								}
								
								AppGlobal.g_oTCP.get().closeClient(iClientSockId);
							}
							catch (Exception e) {
								m_oResponsePacket.clear();
								
								HashMap<String, Object> oErrorHashMap = new HashMap<String, Object>();
								oErrorHashMap.put("code", "-32001");
								oErrorHashMap.put("message", e.toString());
								m_oResponsePacket.put("error", oErrorHashMap);
								tempJSONObj = new JSONObject(m_oResponsePacket);
								writeToClient(iClientSockId, tempJSONObj);
								
								AppGlobal.g_oTCP.get().closeClient(iClientSockId);
								
								AppGlobal.g_oFuncUser.get().logout();
								AppGlobal.stack2Log(e);
							}
							
						}
						
					}
				}
			}
			
			AppGlobal.g_oTCP.get().closeListenSocket();
			
			//Close message queue connection
			if(m_oFuncMessageQueue != null) {
				m_oFuncMessageQueue.closeConnection();
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "MQ connection is closed");
			}
			try {
				oSelector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			AppGlobal.stack2Log(e);
			// Logout session
			AppGlobal.g_oFuncUser.get().logout();
			
			return false;
		}
		
		AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Station terminated");
		return true;
		
	}
	
	private boolean writeToClient(int iClientSocketId, JSONObject oResponsePacket) {
//		System.out.println("Response JSON |" + oResponsePacket + "|");
		if(!AppGlobal.g_oTCP.get().writePacket(iClientSocketId, oResponsePacket.toString())) {
			return false;
		}
		
		//Close socket and clear packet
		AppGlobal.g_oTCP.get().closeClient(iClientSocketId);
		m_oResponsePacket.clear();
		
		return true;
	}
	
	// Function to handle some business checking during idle time or click
	public void intermediateBusinessChecking() {
		String sReason = AppGlobal.getKilledReason();
		if(sReason.length() > 0){
			// The station is going to be killed
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
//			return true;
		}
		
//		return true;
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
		if(sErrorMsg.length() > 0){
			return sErrorMsg;
		}
		
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
		
		if (iRet == 2) {
			// Not yet daily start, return error
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
		
		// Thread 5: Load all payment methods
		PosBusinessDay oBusinessDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay();
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		Object[] oParameter5s = new Object[8];
		oParameter5s[0] = AppGlobal.g_oFuncOutlet.get().getShopId();
		oParameter5s[1] = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oParameter5s[2] = dateFormat.print(oBusinessDay.getDate());
		oParameter5s[3] = oBusinessDay.isHoliday();
		oParameter5s[4] = oBusinessDay.isDayBeforeHoliday();
		oParameter5s[5] = oBusinessDay.isSpecialDay();
		oParameter5s[6] = oBusinessDay.isDayBeforeSpecialDay();
		oParameter5s[7] = oBusinessDay.getDayOfWeek();
		oAppThreadManager2.addThread(5, m_oFuncPayment, "readAllPaymentMethod", oParameter5s);
		
		// Thread 6 : Load discount allowance
		FuncDiscountAcl oFuncDiscountAcl = new FuncDiscountAcl();
		Object[] oParameter6s = new Object[1];
		oParameter6s[0] = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oAppThreadManager2.addThread(6, oFuncDiscountAcl, "readAllDiscountAclByOutlet", oParameter6s);
		
		// Thread 7 : Load last check number if check number is generated by station
		PosCheck oLastSentPosCheck = new PosCheck();
		if (!AppGlobal.g_oFuncOutlet.get().getBusinessDay().isCheckNumGeneratedByOutlet() || AppGlobal.g_oFuncSmartStation.isStandaloneRole()) {
			// Assign the station check prefix to check object
			oLastSentPosCheck.setCheckPrefix(AppGlobal.g_oFuncStation.get().getCheckPrefix());
			
			// Thread 5 : Load the last sent check's check prefix and
			// number
			Object[] oParameter7s = new Object[7];
			oParameter7s[0] = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId();
			oParameter7s[1] = AppGlobal.g_oFuncOutlet.get().getOutletId();
			oParameter7s[2] = AppGlobal.g_oFuncStation.get().getStationId();
			oParameter7s[3] = AppGlobal.g_oFuncStation.get().getStationStartCheckNumber();
			oParameter7s[4] = AppGlobal.g_oFuncStation.get().getStationEndCheckNumber();
			oParameter7s[5] = AppGlobal.g_oFuncOutlet.get().isResetCheckNum();
			oParameter7s[6] = AppGlobal.g_oFuncOutlet.get().getBusinessDay().isCheckNumGeneratedByOutlet();
			oAppThreadManager2.addThread(7, oLastSentPosCheck, "getStationLastCheckNo", oParameter7s);
		}
		
		// Run all of the threads
		oAppThreadManager2.runThread();
	
		// Wait for the thread to finish
		oAppThreadManager2.waitForThread();
		
		if(iRet == 0 && !this.crossDate()) {
			sErrorMsg = m_sLastErrorMessage;
			return sErrorMsg;
		}
		
		// handle thread result
		if(iRet != 2 && (!AppGlobal.g_oFuncOutlet.get().getBusinessDay().isCheckNumGeneratedByOutlet() || AppGlobal.g_oFuncSmartStation.isStandaloneRole())) {
			if((Boolean)oAppThreadManager2.getResult(7) == false) {
				if (AppGlobal.g_oFuncSmartStation.isStandaloneRole())
					AppGlobal.g_oFuncStation.get().setLastCheckPrefixNumber(AppGlobal.g_oFuncStation.get().getOverrideCheckPrefix(), 0);
				else
					AppGlobal.g_oFuncStation.get().setLastCheckPrefixNumber(AppGlobal.g_oFuncStation.get().getCheckPrefix(), (AppGlobal.g_oFuncStation.get().getStationStartCheckNumber() - 1));
			} else {
				if (AppGlobal.g_oFuncSmartStation.isStandaloneRole())
					AppGlobal.g_oFuncStation.get().setLastCheckPrefixNumber(AppGlobal.g_oFuncStation.get().getOverrideCheckPrefix(), (oLastSentPosCheck.getCheckNo() % 10000));
				else
					AppGlobal.g_oFuncStation.get().setLastCheckPrefixNumber(oLastSentPosCheck.getCheckPrefix(), oLastSentPosCheck.getCheckNo());
			}
		}
		
		//load the table and table name pair list
		AppGlobal.g_oFuncOutlet.get().buildTableNameList();
		
		AppGlobal.g_oFuncOverrideList.put(AppGlobal.g_oFuncOutlet.get().getOutletId(), oFuncOverride);
		AppGlobal.g_oFuncDiscountAclList.put(AppGlobal.g_oFuncOutlet.get().getOutletId(), oFuncDiscountAcl);
		
		// Check if check stock operation should be skipped or not
		boolean bNotCheckStock = AppGlobal.g_oFuncStation.get().getNotCheckStock();
		AppGlobal.g_bNotCheckStock = bNotCheckStock;
		
		//Not yet daily start
		if(iRet == 2) {
			sErrorMsg = AppGlobal.g_oFuncOutlet.get().getLastErrorMessage();
			return sErrorMsg;
		}
		
		m_oStoredFuncCheckItemList = new ArrayList<FuncCheckItem>();
		
		// Initialize the quantity to 1.0
		m_dQty = new BigDecimal("1.0");
		
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
		//SystemConfigList sysConfigList = new SystemConfigList();
		//sysConfigList.readBySectionAndVariable("system", "language_code");
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
		//SystemConfigList sysConfigList2 = new SystemConfigList();
		//sysConfigList.readBySectionAndVariable("system", "data_path");
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

	private HashMap<String, String> loadMessageQueueSetup() {
		HashMap<String, String> oMessageQueueSetupList = new HashMap<String, String>();
		
		// Load the internal ID and password from config.ini
		// Read setup from the setup file
		IniReader iniReader = null;
		try {
			iniReader = new IniReader("cfg" + java.io.File.separator + "config.ini");
			String sTmp = iniReader.getValue("message queue", "login");
			if(sTmp != null) {
				String sLoginId = sTmp;
				oMessageQueueSetupList.put("login", sLoginId);
			}
			
			sTmp = iniReader.getValue("message queue", "password");
			if(sTmp != null) {
				String sPassword = sTmp;
				oMessageQueueSetupList.put("password", sPassword);
			}
			
			sTmp = iniReader.getValue("message queue", "path");
			if(sTmp != null) {
				String sPath = sTmp;
				oMessageQueueSetupList.put("path", sPath);
			}
			
			sTmp = iniReader.getValue("message queue", "node");
			if(sTmp != null) {
				String sNode = sTmp;
				oMessageQueueSetupList.put("node", sNode);
			}
			
			sTmp = iniReader.getValue("message queue", "support_https");
			if(sTmp != null) {
				String sSupportHttps = sTmp;
				oMessageQueueSetupList.put("supportHttps", sSupportHttps);
			}
		} catch (IOException e) {
			// Fail to read config.ini
			AppGlobal.stack2Log(e);
		}
		
		return oMessageQueueSetupList;
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
			return false;
		} else {
			// Skip warning if current station is portal station
			if (!AppGlobal.g_oFuncStation.get().isPortalStation()) {
				m_sLastErrorMessage = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString() + " " + AppGlobal.g_oLang.get()._("daily_close_not_yet_completed") + "!\n\n"
					+ AppGlobal.g_oLang.get()._("you_must_perform_daily_close_procedure") + "\n"
					+ AppGlobal.g_oLang.get()._("or_subsequent_sales_will_be_mixed_to") + " " + sBusinessDay;
				return false;
			}
		}
		
		return true;
	}
	
	// Handling portal request
	public void portalRequestHandling() {
//System.out.println("Portal request handling");
//System.out.println("portalRequestHandling - responseQueue:"+m_oFuncMessageQueue.getResponseQueue());
//System.out.println("portalRequestHandling - request:"+m_oFuncMessageQueue.getRequest());
		String sResponseQueue = m_oFuncMessageQueue.getResponseQueue();
		String sRequestContent = m_oFuncMessageQueue.getRequest();
		
		if(sResponseQueue.isEmpty() || sRequestContent.isEmpty())
			return;
		
		try {
			JSONObject oRequestJSONObject = new JSONObject(sRequestContent);
			HashMap<String, Object> oResponsePacket = new HashMap<String, Object>();
			
			String sFuncKey = oRequestJSONObject.optString("funckey");
			if(sFuncKey.equals(""))
				return;
			
			String sPacketId = oRequestJSONObject.optString("id");
			oResponsePacket.put("funckey", sFuncKey);
			oResponsePacket.put("id", sPacketId);
			
			//Missing parameter
			JSONObject oParamsJSONObj = oRequestJSONObject.optJSONObject("params");
			if(oParamsJSONObj == null) {
				//Missing parameter
				HashMap<String, Object> oErrorHashMap = new HashMap<String, Object>();
				oErrorHashMap.put("code", "-32602");
				oErrorHashMap.put("message", AppGlobal.g_oLang.get()._("missing_parameters"));
				oResponsePacket.put("error", oErrorHashMap);
				
				JSONObject oResponseJSONObject = new JSONObject(oResponsePacket);
				m_oFuncMessageQueue.publishMessage("", sResponseQueue, oResponseJSONObject.toString(), null);
				return;
			}
			
			//Set language index according to external device
			if(!oParamsJSONObj.optString("langidx").equals("")) {
				String sLangCode = "en";
				int iLangIdx = oParamsJSONObj.optInt("langidx", 1);
				
				for(HashMap<String, String> oLangInfo : AppGlobal.g_oSupportedLangList) {
					int iTmp = Integer.parseInt(oLangInfo.get("index"));
					if(iTmp == iLangIdx) {
						sLangCode = oLangInfo.get("code");
						break;
					}
				}
				
				AppGlobal.g_oCurrentLangIndex.set(new Integer(iLangIdx));
				AppGlobal.g_oLang.get().switchLocale(sLangCode);
			}
			
			String sEmpId = oParamsJSONObj.optString("empid");
			String sEmpPwd = oParamsJSONObj.optString("emppw");
			
			if(sEmpPwd == null || sEmpPwd.length() == 0)
				sEmpPwd = oParamsJSONObj.optString("emppw_plain");
			
			if(!sEmpId.equals(m_sEmployeeId) || !sEmpPwd.equals(m_sEmployeePwd)) {
				m_sEmployeeId = sEmpId;
				m_sEmployeePwd = sEmpPwd;
				
				//Use employee id and pw login system if it has changed
				FuncUser oFuncUser = processLogin(sEmpId, sEmpPwd);
				if(oFuncUser == null) {
					//Invalid employee
					HashMap<String, Object> oErrorHashMap = new HashMap<String, Object>();
					oErrorHashMap.put("code", "-32011");
					oErrorHashMap.put("message", AppGlobal.g_oLang.get()._("no_such_employee"));
					oResponsePacket.put("error", oErrorHashMap);
					
					JSONObject oResponseJSONObject = new JSONObject(oResponsePacket);
					m_oFuncMessageQueue.publishMessage("", sResponseQueue, oResponseJSONObject.toString(), null);
					return;
				}
				//Assign the user to global
				AppGlobal.g_oFuncUser.set(oFuncUser);
				
				
			}
			
			String sTableNo = oParamsJSONObj.optString("tableno");
			int iCover = 1;
			if(oParamsJSONObj.has("cover") && !oParamsJSONObj.optString("cover").equals(""))
				iCover = Integer.valueOf(oParamsJSONObj.optString("cover"));
			//Open Check first
			String sCheckNo = oParamsJSONObj.optString("checkno");
			
			HashMap<String, String> oTempHashMap = new HashMap<String, String>();
			oTempHashMap.put("responseQueue", sResponseQueue);
			oTempHashMap.put("request", sRequestContent);
			JSONObject oMessageJSONObject = new JSONObject(oTempHashMap);
			DeviceThreadMain oDeviceThreadMain = this.getCheckFromPortalStationCheckList(sCheckNo, sTableNo);
			if (oDeviceThreadMain == null) {
				oDeviceThreadMain = new DeviceThreadMain(sCheckNo, sTableNo, m_oFuncMessageQueue);
				oDeviceThreadMain.init(m_oFuncMessageQueue, m_oFuncPayment, m_oFunctionList);
				oDeviceThreadMain.addPosting(oMessageJSONObject);
				
				if(!(sCheckNo.isEmpty() && sTableNo.isEmpty()))
					this.addCheckToPortalStationCheckList(oDeviceThreadMain);
			} else
				oDeviceThreadMain.addPosting(oMessageJSONObject);
			
			if (sFuncKey.equals(FUNC_LIST.paid.name()))
				this.removeCheckFromPortalStationCheckList(sCheckNo);
		} catch (JSONException e) {
			// write log
			e.printStackTrace();
		}
	}
	
	private boolean fcnSwitch(String sFuncKey, JSONObject oFuncParamJSONObj) {
		PosFunction oPosFunction = null;
		PosFunctionAclCache oPosFuncAclCache = new PosFunctionAclCache();
		boolean bHaveAuthority = false;
		//Prepare reply packet
		HashMap<String, Object> oResultHashMap = new HashMap<String, Object>();
		DateTime oCurrentDateTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMddHHmmss");
		String sDateTime = dateFormat.print(oCurrentDateTime);
//		System.out.println("Current date time |" + sDateTime + "|");
		String sClientMacAddress = oFuncParamJSONObj.optString("mac");
		oResultHashMap.put("time", sDateTime);
		oResultHashMap.put("mac", sClientMacAddress);
		
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
			// Skip checking authority for "get_soldout_item_list" ,"get_item_count_list" and "get_check_information" function
			if (sFuncKey.equals(FUNC_LIST.get_soldout_item_list.name()) || sFuncKey.equals(FUNC_LIST.get_item_count_list.name()) || sFuncKey.equals(FUNC_LIST.get_check_information.name())  ||  sFuncKey.equals(FUNC_LIST.get_all_opened_checks.name())) {
				oPosFunction = new PosFunction();
				
				//Write application log
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Function: " + sFuncKey);
			} else {
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
				
				//Check user authority
				if(AppGlobal.g_oFuncUser.get().isSystemAdmin())
					bHaveAuthority = true;
				else if(oPosFuncAclCache.readByFuncKeyIdAndOutletId(oPosFunction.getFuncId(), sFuncKey, AppGlobal.g_oFuncOutlet.get().getOutletId())) {
					if(oPosFuncAclCache.isAllow()) {
						if(oPosFuncAclCache.getUserStatus().isEmpty())
							bHaveAuthority = true;
					}
				}
			}
			
			//Process function
			//Send Check
			if(oPosFunction.getKey().equals(FUNC_LIST.send_check.name())) {
				String sTableNo = oFuncParamJSONObj.optString("tableno");
				String sTableExtension = oFuncParamJSONObj.optString("tableextension");
				int iCover = 1;
				if(oFuncParamJSONObj.has("cover") && !oFuncParamJSONObj.optString("cover").equals(""))
					iCover = Integer.valueOf(oFuncParamJSONObj.optString("cover"));
				//Open Check first
				String sCheckNo = oFuncParamJSONObj.optString("checkno");
					m_sLastErrorMessage = openCheck(sTableNo, sTableExtension, sCheckNo , iCover);
				if(!m_sLastErrorMessage.equals("")) {
					//Return error, cannot open check
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				
				if(m_oFuncCheck.isPrinted()) {
					if(oFuncParamJSONObj.optString("sendoverride").equals("0")) {
						//Return error, check printed
						oResultHashMap.put("tableno", sTableNo);
						oResultHashMap.put("code", "-32106");
						oResultHashMap.put("message", "Check is printed and not allow to send check again");
						
						m_oResponsePacket.put("error", oResultHashMap);
						return false;
					}
				}
				
				JSONObject oMemberJSONObject = new JSONObject();
				if(oFuncParamJSONObj.has("member")){
					oMemberJSONObject = oFuncParamJSONObj.optJSONObject("member");
					MemMemberList oMemberList = new MemMemberList();
					if(oMemberJSONObject.has("number") && !oMemberJSONObject.optString("number").isEmpty())
						oMemberList.searchMember(FrameSearchMemberFunction.SEARCH_TYPE.number.name(), oMemberJSONObject.optString("number"), "", 1, 1, MemMember.SEARCH_ALL_ACTIVE);
					if(oMemberList.getMemberList().size() == 1){
						for(MemMember oMemMember: oMemberList.getMemberList().values()) {
							if(oMemMember.getMemberNo().equals(oMemberJSONObject.optString("number")))
							{
								m_oFuncCheck.setMember(oMemMember.getMemberId(), null, false, false);
								break;
							}
						}
					}
				}
				
				JSONArray oItemsJSONArray = oFuncParamJSONObj.optJSONArray("items");
				//Add hot items
				for(int i = 0; i < oItemsJSONArray.length(); i++) {
					JSONObject oItemJSONObject = oItemsJSONArray.getJSONObject(i);
					int iItemId = 0, iTargetPriceLevel = -1;
					String sItemCode = "", sOpenDesc = "";
					BigDecimal dQty, dOpenPrice = null;
					
					//get itemId and itemCode
					if(oItemJSONObject.has("id") && !oItemJSONObject.optString("id").equals("")) {
						iItemId = Integer.valueOf(oItemJSONObject.getString("id"));
					}
					else {
						if(!oItemJSONObject.optString("code", "").equals(""))
							sItemCode = oItemJSONObject.optString("code", "");
						else {
							//Missing parameters
							m_sLastErrorMessage = AppGlobal.g_oLang.get()._("missing_parameters");
							m_oFuncCheck.unlockTable(false, false);
							oResultHashMap.put("tableno", sTableNo);
							oResultHashMap.put("code", "-32602");
							oResultHashMap.put("message", m_sLastErrorMessage);
							
							m_oResponsePacket.put("error", oResultHashMap);
							return false;
						}
					}
					//get quantity
					dQty = BigDecimal.ONE;
					if(oItemJSONObject.has("qty"))
						if(!oItemJSONObject.optString("qty", "1.0").equals(""))
							dQty = new BigDecimal(oItemJSONObject.optString("qty", "1.0"));
					sOpenDesc = oItemJSONObject.optString("opendesc", "");
					if(!oItemJSONObject.optString("openprice").equals("")) {
						dOpenPrice = new BigDecimal(oItemJSONObject.getString("openprice"));
					}
					//get price level
					if(oItemJSONObject.has("pricelevel"))
						iTargetPriceLevel = oItemJSONObject.optInt("pricelevel", -1);
					
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
					
					if(!m_sLastErrorMessage.equals("")) {
						//Add item error
						m_oFuncCheck.unlockTable(false, false);
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
									m_oFuncCheck.unlockTable(false, false);
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
						}
						else {
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
							BigDecimal dChildItemQty = BigDecimal.ONE;
							if(oChildJSONObject.has("qty")) {
								if(!oChildJSONObject.optString("qty", "1.0").equals(""))
									dChildItemQty = new BigDecimal(oChildJSONObject.optString("qty", "1.0"));
							}
							String sOpenModiDesc = oChildJSONObject.optString("opendesc");
							int iChildItemListIndex = oChildJSONObject.optInt("belongtolulist");
							
							if(!sChildItemId.equals("")) {
								//Find Child Max Order and Price Level
								int iPriceLevel = AppGlobal.g_oFuncOutlet.get().getPriceLevel();
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

								m_sLastErrorMessage = addSetMenuChildItem(Integer.valueOf(sChildItemId), iPriceLevel,iSeatNo);
								if(!m_sLastErrorMessage.equals("")) {
									//Add child item error
									m_oFuncCheck.unlockTable(false, false);
									oResultHashMap.put("tableno", sTableNo);
									oResultHashMap.put("code", m_sLastErrorCode);
									oResultHashMap.put("message", m_sLastErrorMessage);
									
									m_oResponsePacket.put("error", oResultHashMap);
									return false;
								}
							}
						}
						
						//	Add all child items in m_oStoredFuncCheckItemList to item list in check party
						if (oChildsJSONArray.length() > 0)
							this.finishAddItem();
					}
					
				}
				
				if(oFuncParamJSONObj.has("pending") && oFuncParamJSONObj.optString("pending").equals("1")) {
					for(int i=0; i<=AppGlobal.MAX_SEATS; i++) {
						if(m_oFuncCheck.getItemList(i).isEmpty())
							continue;
						for(FuncCheckItem oFuncCheckItem:m_oFuncCheck.getItemList(i)) {
							oFuncCheckItem.setPendingItem(PosCheckItem.PENDING_PENDING_ITEM);
							// Add log to action log list
							oFuncCheckItem.addActionLog(AppGlobal.FUNC_LIST.select_pending_item.name(), PosActionLog.ACTION_RESULT_SUCCESS, m_oFuncCheck.getTableNoWithExtensionForDisplay(), AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), m_oFuncCheck.getCheckId(), "", "", "", "", "Pending");
						}
					}
				}
				
				//Send Check
				m_sLastErrorMessage = sendCheck();
				if(!m_sLastErrorMessage.equals("")) {
					//Send check fail
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				else {
					//Send check success
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("checkno", m_oFuncCheck.getCheckPrefixNo());
					oResultHashMap.put("checktotal", m_oFuncCheck.getCheckTotal().toPlainString());
					
					oResultHashMap.put("businessdate", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInStringWithFormat("yyyy-MM-dd"));
					
					m_oResponsePacket.put("result", oResultHashMap);
				}
			}
			//Preview check
			else if(oPosFunction.getKey().equals(FUNC_LIST.guest_check_preview.name())) {
				String sTableNo = oFuncParamJSONObj.optString("tableno");
				String sTableExtension = oFuncParamJSONObj.optString("tableextension");
				//Open Check first
				m_sLastErrorMessage = openCheck(sTableNo, sTableExtension, null, 1);
				if(!m_sLastErrorMessage.equals("")) {
					//No check exists
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}

				String sCheckUrl = guestCheckPreview(AppGlobal.g_oCurrentLangIndex.get());
				if(sCheckUrl.equals("")) {
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
//					System.out.println("Guest check link|" + sCheckUrl + "|");
					oResultHashMap.put("checkurl", sCheckUrl);
					
					m_oResponsePacket.put("result", oResultHashMap);
				}
			}
			//Stock sold out
			else if(sFuncKey.equals(FUNC_LIST.get_soldout_item_list.name())) {
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
			else if(sFuncKey.equals(FUNC_LIST.get_item_count_list.name())) {
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
			else if(sFuncKey.equals(FUNC_LIST.get_check_information.name())) {
				String sTableNo = "";
				String sTableExtension = "";
				String sCheckNo = "";
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
				String sShopcode = "";
				String sOutletcode = "";
				
				// Get type : 0 - Get open check list opened by specific employee, 1 - Get all open check
				String sGetType = PosCheck.GET_CHECK_LISTING_BY_SPECIFIC_USER;
				// Default Language Index
				Integer iLangIdx = 1;
				
				if(oFuncParamJSONObj.has("langidx") && !oFuncParamJSONObj.optString("langidx").isEmpty() && Integer.valueOf(oFuncParamJSONObj.optString("langidx")) > 0 && Integer.valueOf(oFuncParamJSONObj.optString("langidx")) < 6)
					iLangIdx = Integer.valueOf(oFuncParamJSONObj.optString("langidx"));
				if(oFuncParamJSONObj.has("openuserlogin") && !oFuncParamJSONObj.optString("openuserlogin").isEmpty())
					sOpenUserLogin = oFuncParamJSONObj.optString("openuserlogin");
				if(oFuncParamJSONObj.has("shopcode") && !oFuncParamJSONObj.optString("shopcode").isEmpty())
					sShopcode = oFuncParamJSONObj.optString("shopcode");
				if(oFuncParamJSONObj.has("outletcode") && !oFuncParamJSONObj.optString("outletcode").isEmpty())
					sOutletcode = oFuncParamJSONObj.optString("outletcode");
				if(oFuncParamJSONObj.has("gettype") && !oFuncParamJSONObj.optString("gettype").isEmpty())
					sGetType = oFuncParamJSONObj.optString("gettype");
				
				m_sLastErrorMessage = getOpenCheckList(iLangIdx, sGetType, sOpenUserLogin, sDateTime);
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
			else if(oPosFunction.getKey().equals(FUNC_LIST.check_table_exist.name())) {
				String sTableNo = oFuncParamJSONObj.optString("tableno");
				String sTableExtension = oFuncParamJSONObj.optString("tableextension");
				int iCover = oFuncParamJSONObj.optInt("cover", 1);
				if(iCover <= 0)
					iCover = 1;
				//Try to open check
				m_sLastErrorMessage = openCheck(sTableNo, sTableExtension, null, iCover);
				if(!m_sLastErrorMessage.equals("")) {
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
			else if(oPosFunction.getKey().equals(FUNC_LIST.paid.name())) {
				if(!payCheck(oFuncParamJSONObj)){
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
			//Get check information
			/*else if(sFuncKey.equals(FUNC_LIST.get_check_information.name())) {
				String sTableNo = "";
				String sCheckNo = null;
				if(oFuncParamJSONObj.has("tableno") && !oFuncParamJSONObj.optString("tableno").equals(""))
					sTableNo = oFuncParamJSONObj.optString("tableno");
				else if(oFuncParamJSONObj.has("checkno") && !oFuncParamJSONObj.optString("checkno").equals(""))
					sCheckNo = oFuncParamJSONObj.optString("checkno");
				
				m_sLastErrorMessage = openCheck(sTableNo, null, 1);
				if(!m_sLastErrorMessage.equals("")) {
					//No check exists
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}
				
				if(m_oMenuItemCourseList == null) {
					m_oMenuItemCourseList = new MenuItemCourseList();
					m_oMenuItemCourseList.readItemCourseList();
				}
				HashMap<String, Object> oResponseHashMap = m_oFuncCheck.getCheckDetail(m_oMenuItemCourseList);
				m_oFuncCheck.unlockTable(false, false);
				if(!m_sLastErrorMessage.equals("")) {
					//No check exists
					oResultHashMap.put("tableno", sTableNo);
					oResultHashMap.put("code", m_sLastErrorCode);
					oResultHashMap.put("message", m_sLastErrorMessage);
					
					m_oResponsePacket.put("error", oResultHashMap);
					return false;
				}else
					m_oResponsePacket.put("result", oResponseHashMap);
			}*/
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
	
/*	private String checkAuthorityAndApproval(boolean bHaveAuthority, PosFunction oPosFunction) {
		String sErrorMsg = "";
		if(!bHaveAuthority) {
			if(!oPosFunction.askAuthority()) {
				//Return error, not allow to run this function
				sErrorMsg = AppGlobal.g_oLang.get()._("Do not have authority to perform the function");
				return sErrorMsg;
			}
		}
		else {
			if(!oPosFunction.askApproval()) {
				//Return error, no approval to run this function
				sErrorMsg = AppGlobal.g_oLang.get()._("Do not have authority to perform the function");
				return sErrorMsg;
			}
		}
		return "";
	}*/
	
	synchronized private void addProcessCheck(String sStoredProcessingCheckKey){
		m_oProcessingSendChecks.put(sStoredProcessingCheckKey, AppGlobal.getCurrentTime(false));
	}
	
	synchronized private void removeProcessCheck(String sStoredProcessingCheckKey){
		// Finish send check, remove process to stored processing check list
		if(m_oProcessingSendChecks.containsKey(sStoredProcessingCheckKey))
			m_oProcessingSendChecks.remove(sStoredProcessingCheckKey);
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

	private String openCheck(String sTableNo, String sTableExtension, String sCheckNo, int iDefaultCover) {
		String sErrorMsg = "";
		int iCover = 0;
//		System.out.println("Open check start Time: " + new DateTime().toString());
		
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
/*		if(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod() == null || AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId() == 0) {
			//Wait for loading the current item stock list
			sErrorMsg = AppGlobal.g_oLang.get()._("Missing period setting");
			oAppThreadManager.waitForThread();
			return sErrorMsg;
		}
		else {
			iPeriodId = AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId();
		}*/
		
		if(AppGlobal.g_iLogLevel >= 9){
			AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Before lock table");
		}
		
		//Try to lock table
		//For old check, load the old check
		if(m_oFuncCheck.lockTable(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), sPeriodId, AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), sTableNo, sTableExtension, true, false, PosCheck.ORDERING_MODE_FINE_DINING, "", false)) {
			if(AppGlobal.g_iLogLevel >= 9){
				AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "After lock table");
			}
			
			// Wait for loading the current item stock list
			oAppThreadManager.waitForThread();
			
			if(AppGlobal.g_iLogLevel >= 9){
				AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "After update stock");
			}
			
			//Load old check
/*			if(!m_oFuncCheck.isOldCheck()) {
				m_oFuncCheck.unlockTable(true, false);
				sErrorMsg = AppGlobal.g_oLang.get()._("cannot_load_new_check");
				return sErrorMsg;
			}*/
			
/*			if(iDefaultCover > 0) {
				iCover = iDefaultCover;
			}
			else {
				//Invalid cover number
				sErrorMsg = AppGlobal.g_oLang.get()._("Invalid cover number");
				m_oFuncCheck.unlockTable(false, false);
				return sErrorMsg;
			}
			
			//Set Cover
			m_oFuncCheck.setCover(iCover);
			
			//Set current check number by logical check no.
			m_oFuncCheck.setCheckNo(AppGlobal.g_oFuncStation.get().getCurrentLogicalCheckNo());*/
		}
		else {
			//Wait for loading the current item stock list
			oAppThreadManager.waitForThread();
			//Fail to lock table
			sErrorMsg = m_oFuncCheck.getLastErrorMessage();
			return sErrorMsg;
		}
		
		//New check
		if(!m_oFuncCheck.isOldCheck()) {
			if (AppGlobal.g_oFuncStation.get().getNotAllowOpenNewCheck()) {
				//Not allow to open new check
			//	m_sLastErrorCode = "-32042";
				sErrorMsg = AppGlobal.g_oLang.get()._("not_allow_to_send_new_check");
				m_oFuncCheck.unlockTable(false, false);
				return sErrorMsg;
			}
			
			if(iDefaultCover > 0) {
				iCover = iDefaultCover;
				m_oFuncCheck.setCover(iCover, false);
			}
			else {
				//Invalid cover number
				m_sLastErrorCode = "-32023";
				sErrorMsg = AppGlobal.g_oLang.get()._("invalid_cover_number");
				m_oFuncCheck.unlockTable(false, false);
				return sErrorMsg;
			}
		}
		
		if(m_oFuncCheck.isOldCheck()) {
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Open old table " + sTableNo + sTableExtension);
		}
		else {
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Open new table " + sTableNo + sTableExtension);
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
		
		String sAddItemResult = addItem(null, iId, dQty, new BigDecimal("1.0"), sOpenDesc, dOpenPrice, false, false, iPriceLevel, sIsTakeAway, iSeatNo, iCourseNo);
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
	private String addItem(FuncCheckItem oParentFuncCheckItem, int iId, BigDecimal dQty, BigDecimal dBaseQty, String sOpenDesc, BigDecimal dOpenPrice, boolean bModifier, boolean bChildItem, int iPriceLevel, String sIsTakeAway, int iSeatNo, int iCourseNo) {
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
		}
		else if(bModifier && oParentFuncCheckItem != null) {
			oParentFuncCheckItem.addModifierToList(oFuncCheckItem, true);
		}
		else {
			m_oStoredFuncCheckItemList.add(oFuncCheckItem);
		}
		
		if(sIsTakeAway.equals("1"))
			oFuncCheckItem.takeout(true);
		oFuncCheckItem.getCheckItem().setCourseId(iCourseNo);
		oFuncCheckItem.getCheckItem().setSeatNo(iSeatNo);
		
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
		oFuncCheckItem.getCheckItem().setOrderUserId(AppGlobal.g_oFuncUser.get().getUserId());
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
				AppGlobal.g_oFuncOverrideList.get(AppGlobal.g_oFuncOutlet.get().getOutletId()).checkPriceOverrideForItem(oFuncCheckItem, bFastFoodCheck,
						oCheckOpenTime, oItemOrderTime, -1, "", iSphrId, m_oFuncCheck.getCustomTypeId(), oDtypeIdList);
			
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
		
		if(oLastCheckItem.isSetMenu()) {
			m_sLastErrorCode = "-32033";
			sErrorMsg = AppGlobal.g_oLang.get()._("cannot_add_modifier")+" "+AppGlobal.g_oLang.get()._("in_set_menu");
			return sErrorMsg;
		}
		
		if(oLastCheckItem.isOpenModifier()) {
			m_sLastErrorCode = "-32034";
			sErrorMsg = AppGlobal.g_oLang.get()._("this_item_already_have_open_price_modifier")+", "+AppGlobal.g_oLang.get()._("cannot_apply_modifier_again");
			return sErrorMsg;
		}
		
		String sResult = addModifier(oLastCheckItem, iModifierId, iModifierListIndex, new BigDecimal("1.0"), sOpenDesc);
		if(!sResult.equals("")) {
			//Add modifier error, return error and code
			sErrorMsg = sResult;
			return sErrorMsg;
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
		if(!oModiFuncCheckItem.retieveItemFromMenu(iId, dQty, new BigDecimal("1.0"), oParentFuncCheckItem, true, false, AppGlobal.g_oFuncOutlet.get().getPriceLevel())) {
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
	private String addSetMenuChildItem(int iChildItemId, int iPriceLevel, int iSeatNo) {
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
		sErrorMsg = addItem(oLastCheckItem, iChildItemId, new BigDecimal("1.0"), new BigDecimal("1.0"), "", null, false, true, iPriceLevel, "", 0 ,0);
		if(!sErrorMsg.equals("")) {
			return sErrorMsg;
		}
		
		return "";
	}
	
	private void finishAddChildItem() {
		calculateCheck();
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
			if(oNewFuncCheckItem.getCheckItem().getTotal().compareTo(BigDecimal.ZERO) == 0) 
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
			m_oFuncCheck.setCheckOpenTimeValue(AppGlobal.g_oFuncOutlet.get().getOutletId(), false, 0);
			
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
	
	//Guest check preview
	private String guestCheckPreview(int iLangIdx) {
		String sCheckUrl = "";
		
//		FuncCheck oFuncCheck = new FuncCheck();
//		PosOutletTable oPosOutletTable = new PosOutletTable(AppGlobal.g_oFuncOutlet.get().getOutletId(), iTable, sTableExt)
		
		if(!m_oFuncCheck.isOldCheck()) {
			m_sLastErrorCode = "-32038";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_preview_new_check");
			return "";
		}

		// if check items are modified, cannot review check
		if(m_oFuncCheck.isModified()) {
			m_sLastErrorCode = "-32039";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_preview_modified_check");
			return "";
		}

		//Get detail check format
		int iChosenCheckPfmtId = 0;
		if(iLangIdx > 0 && iLangIdx <= 5) {
			iChosenCheckPfmtId = AppGlobal.g_oFuncStation.get().getStation().getCheckPfmtId(iLangIdx);
			if(iChosenCheckPfmtId <= 0) {
				m_sLastErrorCode = "-32040";
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_print_format_is_defined");
				return "";
			}
		}
		
		//Get guest check for preview
		sCheckUrl = m_oFuncCheck.previewGuestCheck(iChosenCheckPfmtId, false).optString("url", "");
		if(sCheckUrl.length() == 0) {
			m_sLastErrorCode = "-32041";
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_create_guest_check_image");
			return "";
		}
		
		//Unlock table
		m_oFuncCheck.unlockTable(false, false);
		
		// Add log to action log list
		AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.guest_check_preview.name(), PosActionLog.ACTION_RESULT_SUCCESS, m_oFuncCheck.getTableNoWithExtensionForDisplay(), AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), m_oFuncCheck.getCheckId(), "", "", "", "", "");
		//handle action log
		AppGlobal.g_oActionLog.get().handleActionLog(false);
		
		return sCheckUrl;
	}
	
	//get item soldout list
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
		JSONArray oTaxArray = new JSONArray(), oItemArray = new JSONArray(), oModifyArray = new JSONArray();
		JSONArray oChildItemArray = new JSONArray(), oDiscountArray = new JSONArray(), oPaymentArray = new JSONArray();
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
			oResultSONObject.put("tableno", Integer.toString(oCheckTable.getTable()));
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
				oItemJSONObject.put("unitprice", oPosCheckItem.getUnitCost());
				oItemJSONObject.put("total", oPosCheckItem.getTotal());
				if(oPosCheckItem.getOrderingType().equals(PosCheckItem.ORDERING_TYPE_TAKEOUT))
					oItemJSONObject.put("takeaway", "true");
				else if(oPosCheckItem.getOrderingType().equals(PosCheckItem.ORDERING_TYPE_FINE_DINING))
					oItemJSONObject.put("takeaway", "false");
				oItemJSONObject.put("seatnumber", Integer.toString(oPosCheckItem.getSeatNo()));
				
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
						else if (oUserUser.getUserId() == oPosCheckItem.getOrderUserId())
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
					oDiscountArray.put(oDiscountJSONObject);
				}
				if(oDiscountArray.length()>0)
					oResultSONObject.put("discounts", oDiscountArray);
			}
			
			
			//Check payment information
			oPaymentArray = new JSONArray();
			PosPaymentMethodList oPosPaymentMethodList = new PosPaymentMethodList();
			PosBusinessDay oBusinessDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay();
			DateTimeFormatter dateFormatForpayment = DateTimeFormat.forPattern("yyyy-MM-dd");
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
				oPaymentArray.put(oPaymentJSONObject);
			}
			if(oPaymentArray.length()>0)
				oResultSONObject.put("payments", oPaymentArray);
			
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

	//Open Check Listing
	private String getOpenCheckList(Integer iLangIdx, String sGetType, String sOpenUserLogin, String sDateTime) {
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
					oCheckJSONObject.put("tablename", AppGlobal.g_oFuncOutlet.get().getTableName(Integer.toString(oCheck.getTable()), oCheck.getTableExtension())[iLangIdx - 1]);
					oCheckJSONObject.put("checkno", oCheck.getCheckPrefixNo());
					oCheckJSONObject.put("cover", Integer.toString(oCheck.getGuests()));
					
					oCheckJSONObject.put("openemployeecode", oUser.getNumber());
					oCheckJSONObject.put("openemployeename", oUser.getFirstName(iLangIdx) + " " + oUser.getLastName(iLangIdx));
					
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
	private boolean payCheck(JSONObject oCheckInfo) {
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
		
		//if(!sTableNo.isEmpty()) {
			// open check
			m_sLastErrorMessage = openCheck(sTableNo, sTableExtension, sCheckNo , 0);
			if(!m_sLastErrorMessage.equals(""))
				return false;
			
			//check no. comparison
			// check check number
			if(!sCheckNo.equals(m_oFuncCheck.getCheckPrefixNo())) {
				m_oFuncCheck.unlockTable(true, false);
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("check_number_not_matched");
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Check number not matched");
				return false;
			}
			
			//get payment code and ID
			List<HashMap<String, String>> oPaymentInfoList = new ArrayList<HashMap<String, String>>();
			boolean bPaymentInfoReady = false;
			BigDecimal dPaymentsTotal = BigDecimal.ZERO;
			if(oCheckInfo.has("payments") && oCheckInfo.optJSONArray("payments") != null) {
				JSONArray oPaymentInfos = oCheckInfo.optJSONArray("payments");
				for(int i=0; i<oPaymentInfos.length(); i++) {
					String sPaymentCode = oPaymentInfos.optJSONObject(i).optString("code", "");
					if(sPaymentCode.isEmpty()) {
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_such_payment");
						AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "No such payment - code:"+sPaymentCode);
						break;
					}
					
					HashMap<Integer, PosPaymentMethod> oPaymentMethodList = m_oFuncPayment.getPaymentMethodList().getPaymentMethodList();
					PosPaymentMethod oMatchedPaymentMothod = null;
					for(PosPaymentMethod oPaymentMethod : oPaymentMethodList.values()){
						if(oPaymentMethod.getPaymentCode().equals(sPaymentCode)) {
							oMatchedPaymentMothod = oPaymentMethod;
							break;
						}
					}
					
					if(oMatchedPaymentMothod == null) {
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_such_payment");
						AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "No such payment - code:"+sPaymentCode);
						break;
					}
					
					HashMap<String, String> oPaymentInfo = new HashMap<String, String>();
					oPaymentInfo.put("paymentType", oMatchedPaymentMothod.getPaymentType());
					oPaymentInfo.put("paymentId", String.valueOf(oMatchedPaymentMothod.getPaymId()));
					oPaymentInfo.put("payTotal", oPaymentInfos.optJSONObject(i).optString("amount", "0.0"));
					oPaymentInfo.put("tipsAmount","0");
					oPaymentInfo.put("discountTotal", oPaymentInfos.optJSONObject(i).optString("discountAmount", "0.0"));
					if(oPaymentInfos.optJSONObject(i).has("couponno"))
						oPaymentInfo.put("couponno", oPaymentInfos.optJSONObject(i).optString("couponno", ""));
					if(oPaymentInfos.optJSONObject(i).has("cardno"))
						oPaymentInfo.put("cardno", oPaymentInfos.optJSONObject(i).optString("cardno", ""));
					
					oPaymentInfoList.add(oPaymentInfo);
					dPaymentsTotal = dPaymentsTotal.add(new BigDecimal(oPaymentInfos.optJSONObject(i).optString("amount", "0.0")));
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
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("missing_payment_method");
				return false;
			}
			
			if(dPaymentsTotal.compareTo(m_oFuncCheck.getCheckTotal()) < 0) {
				m_oFuncCheck.unlockTable(true, false);
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
			//BigDecimal dOriCheckTotal = new BigDecimal(oCheckInfo.optString("checkTotal", "0.0"));
			if(!preparePaidCheck()) {
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to init check payment:"+m_sLastErrorMessage);
				m_oFuncCheck.unlockTable(true, false);
				return false;
			}
			
			for(HashMap<String, String> oPaymentInfo: oPaymentInfoList) {
				int iPaymentId = Integer.valueOf(oPaymentInfo.get("paymentId")).intValue();
				BigDecimal dPaymentTotal = new BigDecimal(oPaymentInfo.get("payTotal"));
				BigDecimal dDiscountTotal = BigDecimal.ZERO;
				if(oPaymentInfo.containsKey("discountTotal"))
					dDiscountTotal = new BigDecimal(oPaymentInfo.get("discountTotal"));
				
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

				if(!handlePaymentKey(iPaymentId, "", dPaymentTotal, dDiscountTotal, oPaymentExtraInfoList)) {
					AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Fail to handle payment key:"+m_sLastErrorMessage);
					return false;
				}
			}
		//}else {
			//m_sLastErrorMessage = AppGlobal.g_oLang.get()._("missing_table_number");
			//return false;
		//}
		
		return true;
	}
	
	private boolean handlePaymentKey(int iPaymentId, String sParameter, BigDecimal dPaymentTotal, BigDecimal dDiscountTotal, List<HashMap<String, String>> oPaymentExtraInfoList) {
		//JSONObject oParameter = null;
		//boolean bFullPayment = false;
		//boolean bHaveValue = false;
	
		// Handle setup in button parameter
//		if(sParameter.length() > 0){
//			try {
//				oParameter = new JSONObject(sParameter);
//				if(oParameter.has("full_payment")){
//					bFullPayment = true;
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("error") + ":" + sParameter + ", " + AppGlobal.g_oLang.get()._("exception")+ ": " + AppGlobal.stackToString(e);
//			}
//		}
		
		// Add the payment to cashier mode
		if(preProcessEachPayment(iPaymentId, dDiscountTotal, oPaymentExtraInfoList)){
			// Get payment amount from panel button or direct payment
			//BigDecimal dPaymentAmount = BigDecimal.ZERO;
			BigDecimal dTipsAmount = BigDecimal.ZERO;
			
			// Finish asking amount, edit the payment amount stored in PosCheckPayment list of FuncPayment
			if(editPayment(0, m_oFuncPayment.getCheckPaymentList().size()-1, dPaymentTotal, dTipsAmount, false) == false)
				return false;
			
		}else
			return false;
		
		return true;
	}

	private boolean preProcessEachPayment(int iPaymentId, BigDecimal dDiscountTotal, List<HashMap<String, String>> oPaymentExtraInfoList) {
		PosPaymentMethodList oPosPaymentMethodList = m_oFuncPayment.getPaymentMethodList();
		BigDecimal dPayTotal = BigDecimal.ZERO, dTipsTotal = BigDecimal.ZERO;
		int iRet = 0, iEmployeeId = 0, iMemberId = 0;
		String[] sCheckPaymentRefData = new String[3];
		ArrayList<PosCheckExtraInfo> oCheckPaymentExtraInfos = new ArrayList<PosCheckExtraInfo>();

		for(int i=0; i<3; i++)
			sCheckPaymentRefData[i] = null;
		
		//get the payment method
		if(oPosPaymentMethodList.getPaymentMethodList().containsKey(iPaymentId) == false){
			// No payment method is found
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_such_payment_method");
			return false;
		}
		
		PosPaymentMethod oPosPaymentMethod = oPosPaymentMethodList.getPaymentMethodList().get(iPaymentId);
		
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
		if(dDiscountTotal.compareTo(BigDecimal.ZERO) > 0 && iAutoDiscId == 0) {
			m_sLastErrorMessage = "no_discount_attached_to_payment"+": "+oPosPaymentMethod.getName(AppGlobal.g_oCurrentLangIndex.get());
			return false;
		}
		
		if (iAutoDiscId > 0) {
			String sApplyDiscountResult = autoDiscountForPayment(sAutoDiscType, iAutoDiscId, dDiscountTotal);
			if(sApplyDiscountResult.equals(FormMain.FUNC_RESULT_NO_SUCH_RECORD)) {
				m_sLastErrorMessage = "no_such_discount"+": "+iAutoDiscId;
			}else if (sApplyDiscountResult.equals(FormMain.FUNC_RESULT_FAIL))
				return false;
		}
		
		if (bWaiveSC || bWaiveTax || iAutoDiscId > 0) {
			//update check total in cashier screen
			m_oFuncPayment.setCurrentBalance(m_oFuncCheck.getCheckTotal().subtract(m_oFuncPayment.getPaidBalance()));
		}
		
		// add payment extra info
//		if(oPaymentExtraInfoList.size() > 0) {
//			for(HashMap<String, String> oPaymentExtraInfo: oPaymentExtraInfoList) {
//				PosCheckExtraInfo oPosCheckExtraInfo = new PosCheckExtraInfo();
//				oPosCheckExtraInfo.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
//				oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_PAYMENT);
//				oPosCheckExtraInfo.setSection(oPaymentExtraInfo.get("section"));
//				oPosCheckExtraInfo.setVariable(oPaymentExtraInfo.get("variable"));
//				oPosCheckExtraInfo.setValue(oPaymentExtraInfo.get("value"));
//				oCheckPaymentExtraInfos.add(oPosCheckExtraInfo);
//			}
//		}
		
		//for OGS payment handle ogs payment
		//*********
		
		// Add to check payment list first
		iRet = m_oFuncPayment.addPayment(iPaymentId, AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), m_oFuncCheck.getCheckId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), dPayTotal, dTipsTotal, iEmployeeId, iMemberId, sCheckPaymentRefData, oCheckPaymentExtraInfos, 0);
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
			AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Fail to add payment");
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
/*		FuncPMS oFuncPMS = new FuncPMS();
		int iSuccessPMSPaymentCount = 0;
		BigDecimal dPreviousPaymentTotal = BigDecimal.ZERO;
		for(PosCheckPayment oPosCheckPayment:m_oFuncPayment.getCheckPaymentList()) {
			PosPaymentMethod oPaymentMethod = m_oFuncPayment.getPaymentMethodList().getPaymentMethodList().get(oPosCheckPayment.getPaymentMethodId());
			if(oFuncPMS.pmsPosting(m_oFuncCheck, oPosCheckPayment, oPaymentMethod, dPreviousPaymentTotal) == false) {
				//void previous successful PMS posting
				if(iSuccessPMSPaymentCount > 0) {
					int iVoidChkPayment = 0;
					BigDecimal dVoidPreviousPaymentTotal = BigDecimal.ZERO;
					for(PosCheckPayment oVoidPMSChkPayment:m_oFuncPayment.getCheckPaymentList()) {
						if(oVoidPMSChkPayment.havePmsPayment()) {
							PosPaymentMethod oVoidPaymentMethod = m_oFuncPayment.getPaymentMethodList().getPaymentMethodList().get(oVoidPMSChkPayment.getPaymentMethodId());
							oFuncPMS.pmsVoidPosting(m_oFuncCheck, oVoidPMSChkPayment, oVoidPaymentMethod, dVoidPreviousPaymentTotal);
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
*/
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
			if (!bResult && !m_oFuncCheck.getLastErrorMessage().isEmpty()) {
				m_sLastErrorMessage = m_oFuncCheck.getLastErrorMessage();
				return false;
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
							", new item count : " + m_oFuncCheck.getNewItemCount(false).toPlainString() + ">	 ");
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
	
	// Pay check
	private boolean preparePaidCheck() {
		if(AppGlobal.g_iLogLevel >= 9)
			AppGlobal.g_bWriteClientConnectionLog = true;
		
		if(!preCheckingForPayment())
			return false;
		
		// Mix and match function
		mixAndMatchFunction();
		
		// Init FuncPayment for payment process
		m_oFuncPayment.init(m_oFuncCheck.getCheckTotal(), AppGlobal.g_oFuncOutlet.get().getCheckRoundMethod(), AppGlobal.g_oFuncOutlet.get().getCheckRoundDecimal(), AppGlobal.g_oFuncOutlet.get().getPayRoundMethod(), AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal());
		
		return true;
	}
	
	// Before payment checking
	private boolean preCheckingForPayment(){
		if(!m_oFuncCheck.isOldCheck()) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_pay_new_check");
			return false;
		}
		
		if(m_oFuncCheck.isPaid(false)) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("check_is_already_paid");
			return false;
		}
		
		if(m_oFuncCheck.havePendingItems()) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("cannot_pay_check_with_pending_items");
			return false;
		}
		
		if(AppGlobal.g_oFuncStation.get().isSkipPrintCheckForPayment() == false){
			if(!m_oFuncCheck.isPrinted()) {
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("check_is_not_printed");
				return false;
			}
			
			if(!m_oFuncCheck.isCheckTotalEqualToPrintTotal()) {
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("have_unprint_item");
				return false;
			}
		}
		
		return true;
	}
	
	synchronized public void addCheckToPortalStationCheckList(DeviceThreadMain oDeviceThreadMain) {
		m_oCheckListForPortalStation.add(oDeviceThreadMain);
	}
	
	synchronized public void removeCheckFromPortalStationCheckList(String sCheckNo) {
		if (m_oCheckListForPortalStation.isEmpty())
			return;
		
		if (sCheckNo == null || sCheckNo.isEmpty())
			return;
		
		int iTargetIndex = -1;
		for (int i = 0; i < m_oCheckListForPortalStation.size(); i++) {
			DeviceThreadMain oDeviceThreadMain = m_oCheckListForPortalStation.get(i);
			if(oDeviceThreadMain.getCheckNo().equals(sCheckNo)) {
				iTargetIndex = i;
				break;
			}
		}
		
		if (iTargetIndex >= 0)
			m_oCheckListForPortalStation.remove(iTargetIndex);
	}
	
	public DeviceThreadMain getCheckFromPortalStationCheckList(String sCheckNo, String sTableNo) {
		if (m_oCheckListForPortalStation.isEmpty())
			return null;
		
		DeviceThreadMain oTargetDeviceThreadMain = null;
		for (int i = 0; i < m_oCheckListForPortalStation.size(); i++) {
			DeviceThreadMain oDeviceThreadMain = m_oCheckListForPortalStation.get(i);
			if(sCheckNo != null && !sCheckNo.isEmpty() && oDeviceThreadMain.getTargetCheckNo().equals(sCheckNo) 
					|| (sTableNo != null && oDeviceThreadMain.getTargetTableNo().equals(sTableNo))) {
				oTargetDeviceThreadMain = oDeviceThreadMain;
				break;
			}
		}
		return oTargetDeviceThreadMain;
	}
	
	private String getLicenseProfileId() {
		// Get license cert from system config
		String sProfileId = "";
		if (AppGlobal.g_sLicenseCert.isEmpty()) {
			SystemConfigList sysConfigList = new SystemConfigList();
			sysConfigList.readBySectionAndVariable("system", "license_cert");
			if (!sysConfigList.getSystemConfigList().isEmpty()) {
				for (Entry<Integer, SystemConfig> entry: sysConfigList.getSystemConfigList().entrySet()) {
					SystemConfig oSystemConfig = entry.getValue();
					if(oSystemConfig.getValue() != null) {
						AppGlobal.g_sLicenseCert = oSystemConfig.getValue();
						break;
					}
				}
			} else {
				AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "No HERO License Cert found");
				return null;
			}
		}
		
		// Get Profile ID
		try {
			JSONObject oSystemConfigValue = new JSONObject(AppGlobal.g_sLicenseCert);
			sProfileId = oSystemConfigValue.optJSONObject("hero_cert").optJSONObject("content").optString("profile_id");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		// No profile ID, cannot define queue name
		if (sProfileId.isEmpty()) {
			AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "No profile id in HERO License");
			return null;
		}
		
		return sProfileId;
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
			if (sTableExtension.length() > 5)
				sTableExtension = sTableExtension.substring(0, 5);
		}
		if (sTableNo.length() > 9)
			sTableNo = sTableNo.substring(0, 9);
		else if (sTableNo.isEmpty() && !sTableExtension.isEmpty())
			sTableNo = "0";
		oResultMap.put("tableNo", sTableNo);
		oResultMap.put("tableExt", sTableExtension);
		return oResultMap;
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