package launcher;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

import app.AppGlobal;
import app.AppThread;
import app.AppThreadManager;
import app.ClsActiveClient;
import app.FuncActionLog;
import app.FuncMenu;
import app.FuncMessageQueue;
import app.FuncMixAndMatch;
import app.FuncOctopus;
import app.FuncOutlet;
import app.FuncSignage;
import app.FuncStation;
import app.FuncUser;
import core.Core;
import core.manager.ActiveClient;
import externaldevice.AutoDeviceMain;
import externaldevice.DeviceMain;
import externallib.HeroSecurity;
import externallib.IniReader;
import externallib.TCPLib;
import lang.LangResource;
import om.FailoverStationGroup;
import om.OmWsClient;
import om.OmWsClientGlobal;
import om.PosConfig;
import om.PosGeneral;
import om.PosInterfaceConfigList;
import om.PosItemRemindRuleList;
import om.PosStation;
import om.PosStationAlertSetting;
import om.PosStationDevice;
import om.PrtPrintQueueList;
import om.SystemConfig;
import om.SystemConfigList;
import om.WohAwardSettingList;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUITerm;

public class Main {
	private static Service service = null;
	
	public static void main(String[] args){
		// Run in console
		service = new Service();
		service.run();
	}
	
	public static void stop(String[] args) {
		System.exit(0);
	}
}
class Service implements Runnable
{
	static final int NO_ERROR = 0;
	static final int LOGIN_FAILED = 1;
	static final int NO_SUCH_STATION = 2;
	static final int INTERNAL_ERROR = 3;
	
	private boolean m_bRunFlag = true;
	private TCPLib m_oTCP;
	private String m_sLoginId;
	private String m_sLoginPwd;
	private boolean m_bInternalLoginSuccess;
	private HashMap<String, Integer> m_oClientPortHashMap;
	private HashMap<String, Thread> m_oClientThreadHashMap;
	private FuncMessageQueue m_oFuncMessageQueue;
	
	public static void writeErrorLog(String sClass, String sMethod, String sLog){
		AppGlobal.write2LogFile("hero_launcher_err", sLog);
	}
	
	public static void writeLog(String fileName, String sClass, String sMethod, String sLog){
		
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);
		
		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/" + fileName + "." + sCurrentMonth;
		
		try{
			// Create file 
			FileWriter fstream = new FileWriter(sFilename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			sContent.append(sCurrentTime);
			sContent.append(" [");
			sContent.append(sClass);
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
			e.printStackTrace();
		}
	}
	
	/**
	 * Run flag
	 * @param runFlag
	 */
	public synchronized void setRunFlag(boolean runFlag)
	{
		this.m_bRunFlag = runFlag;
	}
	
	/**
	 * Get the run flag
	 * @param void
	 */
	private synchronized boolean getRunFlag()
	{
		return m_bRunFlag;
	}
	
	@Override
	public void run()
	{
		AppGlobal.writeLauncherLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Launcher start");
		
		//String sWSDL = "";
		String sTmp = "";
		int iPortNo = 0;
		String sErrorMessage = "";
		int iErrorNo = NO_ERROR;
		
		// Inital all static variables
		AppGlobal.g_oLang = new ThreadLocal<LangResource>();
		AppGlobal.g_oFuncMenu = new ThreadLocal<FuncMenu>();
		AppGlobal.g_oTerm = new ThreadLocal<VirtualUITerm>();
		OmWsClientGlobal.g_oWsClient = new ThreadLocal<OmWsClient>();
		OmWsClientGlobal.g_oWsClientForHq = new ThreadLocal<OmWsClient>();
		AppGlobal.g_oTCP = new ThreadLocal<TCPLib>();
		AppGlobal.g_oSelectorForTCP = new ThreadLocal<Selector>();
		AppGlobal.g_oSelectorKeyForTCP = new ThreadLocal<SelectionKey>();
		AppGlobal.g_oCurrentLangIndex = new ThreadLocal<Integer>();
		AppGlobal.g_sDisplayMode = new ThreadLocal<String>();
		AppGlobal.g_oFuncStation = new ThreadLocal<FuncStation>();
		AppGlobal.g_oFuncOutlet = new ThreadLocal<FuncOutlet>();
		AppGlobal.g_oFuncSignage = new ThreadLocal<FuncSignage>();
		AppGlobal.g_oFuncUser = new ThreadLocal<FuncUser>();
		AppGlobal.g_oFuncMixAndMatch = new ThreadLocal<FuncMixAndMatch>();
		AppGlobal.g_oActionLog = new ThreadLocal<FuncActionLog>();
		AppGlobal.g_oLang.set(new LangResource());
		// Initialize the web service client
		OmWsClientGlobal.g_oWsClient.set(new OmWsClient());
		OmWsClientGlobal.g_oWsClientForHq.set(new OmWsClient());
		// Initialize the action log
//		AppGlobal.g_oActionLog.set(new FuncActionLog());
		AppGlobal.g_oDeviceManagerElement = new ThreadLocal<VirtualUIBasicElement>();
		AppGlobal.g_oPosInterfaceConfigList = new ThreadLocal<PosInterfaceConfigList>();
		AppGlobal.g_oWohAwardSettingList = new ThreadLocal<WohAwardSettingList>();
		AppGlobal.g_oPosItemRemindRuleList = new ThreadLocal<PosItemRemindRuleList>();
		AppGlobal.g_oPosConfigList = new ThreadLocal<HashMap<String, HashMap<String, PosConfig>>>();
		AppGlobal.g_oFuncOctopus = new ThreadLocal<FuncOctopus>();
		AppGlobal.g_sResultForAutoFunction = new ThreadLocal<String>();
		
		// Read setup from config.ini
		IniReader iniReader;
		try {
			// Selector
			Selector oSelector = null;
			try{
				oSelector = SelectorProvider.provider().openSelector();
			} catch ( IOException e ) {
				// Internal error
				System.exit(0);
			}
			
			// Read setup from the setup file
			iniReader = new IniReader("cfg" + java.io.File.separator + "config.ini");
			
			sTmp = iniReader.getValue("connection", "launcher_port");
			if(sTmp == null){
				sErrorMessage = "Missing setup for launcher port no";
				iErrorNo = INTERNAL_ERROR;
				// Write log
				writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
				return;
			}
			iPortNo = Integer.parseInt(sTmp);
			
			sTmp = iniReader.getValue("setup", "login");
			if(sTmp == null) {
				sErrorMessage = "Missing setup login";
				iErrorNo = INTERNAL_ERROR;
				// Write log
				writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
				m_sLoginId = "";
			}
			else {
				m_sLoginId = sTmp;
			}
			
			boolean bPasswordEncrypted = true;
			sTmp = iniReader.getValue("setup", "encrypted");
			if(sTmp != null){
				if(sTmp.equals("0")){
					bPasswordEncrypted = false;
				}
			}
			
			sTmp = iniReader.getValue("setup", "password");
			if(sTmp == null) {
				sErrorMessage = "Missing setup login password";
				iErrorNo = INTERNAL_ERROR;
				// Write log
				writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
				m_sLoginPwd = "";
			}
			else {
				if(bPasswordEncrypted){
					m_sLoginPwd = HeroSecurity.decryptString(sTmp);
				}else{
					m_sLoginPwd = sTmp;
				}
			}
			
			//Open Launcher localhost Port
			m_oTCP = new TCPLib();
			sTmp = m_oTCP.initServer("127.0.0.1", iPortNo, false);
			if(sTmp.isEmpty() == false) {
				sErrorMessage = "Fail to init port " + iPortNo + ", error: " + sTmp;
				writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
				iErrorNo = INTERNAL_ERROR;
				return;
			}
			
			// Register the server socket channel
			ServerSocketChannel oChannel = m_oTCP.getSocketChannel();
			SelectionKey oLauncherKey = null;
			oLauncherKey = oChannel.register(oSelector, SelectionKey.OP_ACCEPT);
			
			m_oClientPortHashMap = new HashMap<String, Integer>();
			m_oClientThreadHashMap = new HashMap<String, Thread>();
			m_bInternalLoginSuccess = false;
			
			String sLoginId = m_sLoginId;
			String sPassword = m_sLoginPwd;
			
			HashMap<String, String> oLoginPassword = this.getLoginPasswordFromConfigFile();
			if(oLoginPassword != null && oLoginPassword.containsKey("loginId") && !oLoginPassword.get("loginId").isEmpty()
					&& oLoginPassword.containsKey("password") && !oLoginPassword.get("password").isEmpty()) {
				sLoginId = oLoginPassword.get("loginId");
				sPassword = oLoginPassword.get("password");
			}
			
			// Internal login by config.ini setup account
			m_bInternalLoginSuccess = this.internalLogin(sLoginId, sPassword, false);
			
			// Check if standalone setup is existing
			// If yes, this is Smart Station model
			// If no, normal HERO server model
			iniReader = new IniReader("cfg/config.ini");
			sTmp = iniReader.getValue("connection", "db_wsdl_standalone");
			if (sTmp != null) {
				// Turn on Smart Station model
				if (!AppGlobal.g_oFuncSmartStation.initSmartStationModel()) {
					writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncSmartStation.getLastErrorMessage());
					return;
				}
			} else {
				// Only initialize portal stations for NOT Smart Station model
				// Initialize portal stations
				List<PosStation> oPosStationList = externaldevice.Main.getAllPortalStations();
				if (!oPosStationList.isEmpty()) {
					for (PosStation oPortalStation: oPosStationList) {
						DeviceManager deviceMgr = new DeviceManager(oPortalStation.getAddress(), sLoginId, sPassword, 0, AppGlobal.DISPLAY_MODE.no_display.name());
						deviceMgr.setDeviceKey(PosStationDevice.KEY_PORTAL_STATION);
						Thread oThread = new Thread(deviceMgr);
						oThread.start();
					}
				}
			}
			
			// Preparation for alert message
			// 1. Check if it is local mq server
			m_oFuncMessageQueue = new FuncMessageQueue();
			if (m_oFuncMessageQueue.isLocalServer()) {
				//2. Preparation for the alert message station setup
				AppGlobal.g_oAlertMessageElementIdToStationIdsSettingTable = PosStationAlertSetting.readConvertedTypeIdStationIdListTable();
				AppGlobal.g_oAlertMessageStationIdToPrintQueueIdsTable = PosStationAlertSetting.constructAlertMessageStationIdToPrintQueueIdTable();
				PrtPrintQueueList oPrtPrintQueueList = new PrtPrintQueueList();
				oPrtPrintQueueList.readAll();
				AppGlobal.g_oPrintQueueList = oPrtPrintQueueList.readPrintQueueList();
				
				//3. Prepare Queue for Alert Message
				AlertMessage oAlertMsg = new AlertMessage();
				oAlertMsg.init();
			}
			
			// Create thread to do auto restart pms shell
			AppThreadManager oAppThreadManager = new AppThreadManager();
			
			// Add the method to the thread manager
			// Thread 1 : auto restart pms shell
			oAppThreadManager.addThread(1, this, "triggerAutoRestartPMSShell", null);
			
			// Run the thread without wait
			oAppThreadManager.runThread();
			
			while(getRunFlag())
			{
				int n = oSelector.select(180000);
				if(n == 0)
				{
					if (!AppGlobal.g_oFuncSmartStation.isSmartStationModel() || AppGlobal.g_oFuncSmartStation.isServiceMasterRole()) {
						// Check if auto station and portal station are required to be started
						HashMap<PosStation, String> oStationIdBdayIdList = new HashMap<PosStation, String>();
						PosGeneral oPosGeneral = new PosGeneral();
						oPosGeneral.readActiveBusinessDayAutoAndPortalStations();
						JSONObject oActiveAutoPortalObj = oPosGeneral.getResponseJSONObject();
						
						if(oActiveAutoPortalObj != null) {
							JSONArray oUpdatedBdayId = oActiveAutoPortalObj.optJSONArray("businessDays");
							JSONArray oUpdatedStation = oActiveAutoPortalObj.optJSONArray("stations");
							
							for(int i = 0; i < oUpdatedBdayId.length(); i++) {
								PosStation oPosStation = new PosStation(oUpdatedStation.optJSONObject(i));
								oStationIdBdayIdList.put(oPosStation, oUpdatedBdayId.optString(i));
							}
							
							for (Entry<PosStation, String> entry2 : oStationIdBdayIdList.entrySet()) {
								PosStation oPosStation = entry2.getKey();
								boolean bFoundStation = false;
								boolean bIncorrectBday = false;
								for (Map.Entry<Integer, String> entry : AppGlobal.g_oAutoPortalStationList.entrySet()) {
									if (entry.getKey() == oPosStation.getStatId()) {
										if (!entry.getValue().equals(entry2.getValue()))
											bIncorrectBday = true;
										bFoundStation = true;
										break;
									}
								}
								
								if (!bFoundStation) {
									if(oPosStation.getStationDevice().getKey().equals(PosStationDevice.KEY_PORTAL_STATION))
										AppGlobal.launchPortalStation(oPosStation.getOletId(), oPosStation.getAddress(), false);
									else
										AppGlobal.launchAutoStation(oPosStation.getOletId(), oPosStation.getAddress(), false);
								}
								
								if (bIncorrectBday) {
									if(oPosStation.getStationDevice().getKey().equals(PosStationDevice.KEY_PORTAL_STATION))
										AppGlobal.stopPortalStation(oPosStation.getOletId());
									else
										AppGlobal.stopAutoStation(oPosStation.getOletId());
								}
							}
						}
					}
					
					continue;
				}
				System.out.println("來了一個客戶端");
				java.util.Iterator<SelectionKey> iterator = oSelector.selectedKeys().iterator();
				while(iterator.hasNext())
				{
					SelectionKey oIncomingSelectionkey = (SelectionKey)iterator.next();
					
					iterator.remove();
					//m_oTCP.printMesssige(oIncomingSelectionkey,oSelector);
					// Client connect
					if(oIncomingSelectionkey.isAcceptable() && oIncomingSelectionkey == oLauncherKey)
					{
						// Listen the port to handle client request from web service
						iErrorNo = NO_ERROR;
						int iClientSockId = m_oTCP.listen();
						
						System.out.println("New client - incoming message : " + m_oTCP.getPacket());
						
						if(iClientSockId <= 0) {						//client socket is available
							//Error connection
							sErrorMessage = "Cannot retrieve client socket";
							writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
							continue;
						}
						
						try {
							JSONObject jRequestPacket;
							try{
								jRequestPacket = new JSONObject(m_oTCP.getPacket());
								System.out.println("jRequestPacket"+jRequestPacket.toString());
							}catch(JSONException e){
								sErrorMessage = "Packet error: " + m_oTCP.getPacket();
								writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
								continue;
							}
							
							// Check if this is "execute" request
							if(jRequestPacket.has("task")){
								if(jRequestPacket.has("params")){
									JSONObject jParams = new JSONObject(jRequestPacket.getString("params"));
									if(jParams.has("Udid")){
										String sUDID = jParams.getString("Udid");
										boolean bFound = false;
										for(Map.Entry<Long, ClsActiveClient>entry:AppGlobal.g_lCurrentConnectClientList.entrySet()){
											ClsActiveClient oActiveClient = entry.getValue();
											if(sUDID.equals(oActiveClient.getUDID())){
												bFound = true;
												break;
											}
										}
										
										if(bFound){
											// Kill the client thread
											if(m_oClientThreadHashMap.containsKey(sUDID)){
												Thread oThread = m_oClientThreadHashMap.get(sUDID);
												oThread.interrupt();
												
												Thread.sleep(2000);
												
												if(oThread.isAlive())
													oThread.stop();
												
												Core.g_oClientManager.removeActiveClient(sUDID);
												m_oClientPortHashMap.remove(sUDID);
												m_oClientThreadHashMap.remove(sUDID);
												
												for(Map.Entry<Long, ClsActiveClient>entry:AppGlobal.g_lCurrentConnectClientList.entrySet()){
													ClsActiveClient oActiveClient = entry.getValue();
													if(sUDID.equals(oActiveClient.getUDID())){
														AppGlobal.g_lCurrentConnectClientList.remove(entry.getKey());
														break;
													}
												}
												
												writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Terminate by client (UDID: " + sUDID + ")");
											}
											
											// Kill success
											responseToClient(iClientSockId, 0, 0);
											
											continue;
										}else{
											// No such station, skip
											responseToClient(iClientSockId, 0, 0);
											
											continue;
										}
									}
								}
								
								// Error case
								responseToClient(iClientSockId, 0, 1);
								
								continue;
							}
							
							// Get incoming client UDID, login, password, card no
							String sIncomingClientUDID = jRequestPacket.getString("udid");
							String sIncomingClientLoginID = jRequestPacket.getString("login");
							String sIncomingClientLoginPassword = jRequestPacket.getString("password");
							String sIncomingClientSwipeCardLogin = "";
							if(jRequestPacket.has("swipe_card_login")){
								sIncomingClientSwipeCardLogin = jRequestPacket.getString("swipe_card_login");
							}
							String sIncomingClientDisplayMode = "horizontal_desktop";
							String sRequestDisplayMode = jRequestPacket.optString("display_mode");
							if(!sRequestDisplayMode.isEmpty())
								sIncomingClientDisplayMode = sRequestDisplayMode;
							String sRequestExtraInfo = jRequestPacket.optString("extra_info");
							JSONObject oIncomingClientExtraInfo = new JSONObject();
							if(!sRequestExtraInfo.isEmpty()) {
								try {
									oIncomingClientExtraInfo = new JSONObject(sRequestExtraInfo);
								}
								catch (JSONException e) {}
							}
							String sIncomingClientAccessToken = "";
							if(jRequestPacket.has("access_token")){
								sIncomingClientAccessToken = jRequestPacket.optString("access_token", "");
							}
							
							//	Write log for connect attempt
							String sConnectAttemptLog = "user:" + sIncomingClientLoginID + "; ";
							sConnectAttemptLog += "udid:" + sIncomingClientUDID + "; ";
							JSONObject oIncomingClientExtraInfoEnvironment = oIncomingClientExtraInfo.optJSONObject("Environment");
							if (oIncomingClientExtraInfoEnvironment != null) {
								sConnectAttemptLog += "os:" + oIncomingClientExtraInfoEnvironment.optString("OS", "") + "; ";
								sConnectAttemptLog += "program_version:" + oIncomingClientExtraInfoEnvironment.optString("ProgramVersion", "") + "; ";
								
								long lInstallDateTimestamp = oIncomingClientExtraInfoEnvironment.optLong("InstallDate", 0);
								DateTimeFormatter oInstallDateSDF = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
								DateTime dTarget = new DateTime(lInstallDateTimestamp);
								sConnectAttemptLog += "install_date:" + oInstallDateSDF.print(dTarget) + "; ";
							}
							AppGlobal.write2LogFile("hero_launcher_attempt", sConnectAttemptLog);
							
							// Switch to target outlet
							int iIncomingSwitchOutletId = 0;
							JSONObject oIncomingClientExtraInfoPos = oIncomingClientExtraInfo.optJSONObject("Pos");
							if (oIncomingClientExtraInfoPos != null)
								iIncomingSwitchOutletId = oIncomingClientExtraInfoPos.optInt("OutletId", 0);
							
							// Generate/retrieve port number for device
							int iClientPort;
							Boolean bNewConnectionBoolean = false;
							if(m_oClientPortHashMap.containsKey(sIncomingClientUDID)) {
								iClientPort = m_oClientPortHashMap.get(sIncomingClientUDID);
								
								if(isPortAvailable(iClientPort)) {					//if the port is not using
									bNewConnectionBoolean = true;
								}
							}else{
								iClientPort = getClientPortByUDID(sIncomingClientUDID);
								bNewConnectionBoolean = true;
							}
							
							if(bNewConnectionBoolean) {					//if the port is not using
								// For compatible with structure version 2
								ActiveClient oActiveClient = new ActiveClient(iClientPort, sIncomingClientUDID, sIncomingClientLoginID, sIncomingClientLoginPassword, sIncomingClientSwipeCardLogin, sIncomingClientDisplayMode);
								Core.g_oClientManager.addActiveClient(sIncomingClientUDID, oActiveClient);
								
								if(sIncomingClientDisplayMode.equals(AppGlobal.DISPLAY_MODE.no_display.name())) {
									if (!AppGlobal.g_oFuncSmartStation.isSmartStationModel() || AppGlobal.g_oFuncSmartStation.isServiceMasterRole()) {
										sLoginId = m_sLoginId;
										sPassword = m_sLoginPwd;
										
										oLoginPassword = this.getLoginPasswordFromConfigFile();
										if(oLoginPassword != null && oLoginPassword.containsKey("loginId") && !oLoginPassword.get("loginId").isEmpty()
												&& oLoginPassword.containsKey("password") && !oLoginPassword.get("password").isEmpty()) {
											sLoginId = oLoginPassword.get("loginId");
											sPassword = oLoginPassword.get("password");
										}
										
										if(!m_bInternalLoginSuccess) {
											//Try to login again
											m_bInternalLoginSuccess = this.internalLogin(sLoginId, sPassword, false);
										}
										
										if(m_bInternalLoginSuccess) {
											//Check if it is iPad connections OR third party station
											boolean bIsIpad = externaldevice.Main.checkAppleDevice(sIncomingClientUDID);
											boolean bIsThirdPartyStation = externaldevice.Main.checkThirdPartyStationDevice(sIncomingClientUDID);
											if(bIsIpad || bIsThirdPartyStation) {
												//DeviceManager deviceMgr = new DeviceManager(sIncomingClientUDID, m_sLoginId, m_sLoginPwd, iClientPort, sIncomingClientDisplayMode);
												DeviceManager deviceMgr = new DeviceManager(sIncomingClientUDID, sLoginId, sPassword, iClientPort, sIncomingClientDisplayMode);
												Thread oThread = new Thread(deviceMgr);
												oThread.start();
												
												// In order to let the server side ready, we need to wait for the port ready
												int iWaitCnt = 0;
												while(isPortAvailable(iClientPort) && iWaitCnt < 30) {
													if (!oThread.isAlive()) {
														writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "IPAD thread exit");
														
														// Error occur in IPad thread
														iClientPort = 0;
														iErrorNo = LOGIN_FAILED;
														break;
													}
													
													Thread.sleep(1000);
													iWaitCnt++;
												}
												
												//No Error
												responseToClient(iClientSockId, iClientPort, iErrorNo);
												continue;
											}
											
											//Check if it is auto station connections
											boolean bIsAutoStation = externaldevice.Main.checkAutoStationDevice(sIncomingClientUDID);
											if(bIsAutoStation) {
												// Auto Station
												//AutoDeviceManager autoDeviceMgr = new AutoDeviceManager(sIncomingClientUDID, m_sLoginId, m_sLoginPwd, iClientPort, sIncomingClientDisplayMode);
												AutoDeviceManager autoDeviceMgr = new AutoDeviceManager(sIncomingClientUDID, sLoginId, sPassword, iClientPort, sIncomingClientDisplayMode);
												Thread oThread = new Thread(autoDeviceMgr);
												oThread.start();
												
												//No Error
												responseToClient(iClientSockId, iClientPort, iErrorNo);
												continue;
											}
											
											//Check if it is portal station connections
											boolean bIsPortalStation = externaldevice.Main.checkPortalStationDevice(sIncomingClientUDID);
											if(bIsPortalStation) {
												DeviceManager deviceMgr = new DeviceManager(sIncomingClientUDID, sLoginId, sPassword, iClientPort, sIncomingClientDisplayMode);
												deviceMgr.setDeviceKey(PosStationDevice.KEY_PORTAL_STATION);
												Thread oThread = new Thread(deviceMgr);
												oThread.start();
												
												// In order to let the server side ready, we need to wait for the port ready
												int iWaitCnt = 0;
												while(isPortAvailable(iClientPort) && iWaitCnt < 30) {
													if (!oThread.isAlive()) {
														writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "IPAD thread exit");
														
														// Error occur in IPad thread
														iClientPort = 0;
														iErrorNo = LOGIN_FAILED;
														break;
													}
													
													Thread.sleep(1000);
													iWaitCnt++;
												}
												
												//No Error
												responseToClient(iClientSockId, iClientPort, iErrorNo);
												continue;
											}
										}
										else {
											// Error case
											responseToClient(iClientSockId, 0, LOGIN_FAILED);
											continue;
										}
									} else {
										// Not allow to create connection
										sErrorMessage = "Not Allow to create connection for non-service master server";
										writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
										responseToClient(iClientSockId, 0, LOGIN_FAILED);
										continue;
									}
								}
								else {
									// Launch app with IP and Port
									AppMain appMain = new AppMain(iClientPort, sIncomingClientUDID, sIncomingClientLoginID, sIncomingClientLoginPassword, sIncomingClientSwipeCardLogin, sIncomingClientDisplayMode, sIncomingClientAccessToken, iIncomingSwitchOutletId);
									
									Thread oThread = new Thread(appMain);
									oThread.start();
								}
								
							}else{
								for(Map.Entry<Long, ClsActiveClient> entry:AppGlobal.g_lCurrentConnectClientList.entrySet()){
									long lClientThreadID = entry.getKey();
									ClsActiveClient oActiveClient = entry.getValue();
									String sStationUUID = oActiveClient.getUDID();
									
									if(sIncomingClientUDID.equals(sStationUUID)){
										AppGlobal.addReconnectIdAndPasswordToStation(lClientThreadID, sIncomingClientLoginID, sIncomingClientLoginPassword, sIncomingClientSwipeCardLogin);
										break;
									}
								}
							}
							
							//No Error
							responseToClient(iClientSockId, iClientPort, iErrorNo);
						}
						finally {
							m_oTCP.closeClient(iClientSockId);
						}
					}
				}
			}
		}
		catch (SocketException sce) {
			sErrorMessage = "Socket Error";
			writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
			return;
		}
		catch (IOException e) {
			sErrorMessage = "Missing setup file (cfg/config.ini)";
			// Write log
			writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
			return;
		}
		catch (Exception e){
			e.printStackTrace();
			sErrorMessage = "Unknown Error";
			writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
			return;
		}
		
	}
	
	private HashMap<String, String> getLoginPasswordFromConfigFile() {
		IniReader iniReader;
		String sTmp = "";
		String sErrorMessage = "";
		int iErrorNo;
		HashMap<String, String> oLoginPassword = new HashMap<String, String>();
		
		try {
			// Read setup from the setup file
			iniReader = new IniReader("cfg" + java.io.File.separator + "config.ini");
			
			sTmp = iniReader.getValue("setup", "login");
			if(sTmp == null) {
				sErrorMessage = "Missing setup login";
				iErrorNo = INTERNAL_ERROR;
				// Write log
				writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
				return null;
			}
			else {
				oLoginPassword.put("loginId", sTmp);
			}
			
			boolean bPasswordEncrypted = true;
			sTmp = iniReader.getValue("setup", "encrypted");
			if(sTmp != null){
				if(sTmp.equals("0")){
					bPasswordEncrypted = false;
				}
			}
			
			sTmp = iniReader.getValue("setup", "password");
			if(sTmp == null) {
				sErrorMessage = "Missing setup login password";
				iErrorNo = INTERNAL_ERROR;
				// Write log
				writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
				return null;
			}
			else {
				if(bPasswordEncrypted){
					oLoginPassword.put("password", HeroSecurity.decryptString(sTmp));
				}else{
					oLoginPassword.put("password", sTmp);
				}
			}
		}
		catch (IOException e) {
			sErrorMessage = "Missing setup file (cfg/config.ini)";
			// Write log
			writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
			return null;
		}
		catch (Exception e){
			e.printStackTrace();
			sErrorMessage = "Unknown Error";
			writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
			return null;
		}
		
		return oLoginPassword;
	}
	
	class AppMain implements Runnable {
		private int m_iClientPort;
		private String m_sIPAddress;
		private String m_sUserName;
		private String m_sUserPassword;
		private String m_sUserCardNo;
		private String m_sDisplayMode;
		private String m_sAccessToken;
		private int m_iSwitchOutletId;
		
		public AppMain(int iPort, String sIPAddr, String sUsrName, String sUsrPW, String sUserCardNo, String sDisplayMode, String sAccessToken, int iSwitchOutletId) {
			m_iClientPort = iPort;
			m_sIPAddress = sIPAddr;
			m_sUserName = sUsrName;
			m_sUserPassword = sUsrPW;
			m_sUserCardNo = sUserCardNo;
			m_sDisplayMode = sDisplayMode;
			m_sAccessToken = sAccessToken;
			m_iSwitchOutletId = iSwitchOutletId;
		}
		
		@Override
		public void run() {
			String[] arguments = new String[] {Integer.toString(m_iClientPort), m_sIPAddress, m_sUserName, m_sUserPassword, m_sUserCardNo, m_sDisplayMode, m_sAccessToken, Integer.toString(m_iSwitchOutletId)};
			
			ClsActiveClient oActiveClient = new ClsActiveClient();
			oActiveClient.setUDID(m_sIPAddress);
			AppGlobal.g_lCurrentConnectClientList.put(Thread.currentThread().getId(), oActiveClient);
			m_oClientThreadHashMap.put(m_sIPAddress, Thread.currentThread());
			
			// For compatible with structure version 2
			ActiveClient oHeroActiveClient = Core.g_oClientManager.getActiveClient(m_sIPAddress);
			if (oHeroActiveClient == null){
				return;
			}
			
			Core.g_oClientManager.registerCurrentThread(oHeroActiveClient);
			if(oHeroActiveClient.init() == false){
				// Fail to initial client
				return;
			}
			
			try {
				app.Main.main(arguments);
			}
			catch (Exception e) {
				writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Starting app failed");
			}
			
			// Remove client from the list
			Core.g_oClientManager.removeActiveClient(m_sIPAddress);
			AppGlobal.g_lCurrentConnectClientList.remove(Thread.currentThread().getId());
			m_oClientPortHashMap.remove(m_sIPAddress);
			m_oClientThreadHashMap.remove(m_sIPAddress);
		}
	}
	
	class DeviceManager implements Runnable {
		private String m_sStationAddr;
		private String m_sLoginId;
		private String m_sLoginPwd;
		private int m_iPortNum;
		private String m_sDisplayMode;
		private DeviceMain m_oDeviceMain;
		private String m_sDeviceKey;
		
		public DeviceManager(String sStatAddr, String sLoginId, String sLoginPwd, int iPortNum, String sDisplayMode) {
			m_sStationAddr = sStatAddr;
			m_sLoginId = sLoginId;
			m_sLoginPwd = sLoginPwd;
			m_iPortNum = iPortNum;
			m_sDisplayMode = sDisplayMode;
			m_sDeviceKey = "";
		}
		
		public void setDeviceKey(String sDeviceKey) {
			m_sDeviceKey = sDeviceKey;
		}
		
		@Override
		public void run() {
			
			ClsActiveClient oActiveClient = new ClsActiveClient();
			oActiveClient.setUDID(m_sStationAddr);
			if (m_sDeviceKey.equals(PosStationDevice.KEY_PORTAL_STATION))
				oActiveClient.setPortalStation(true);
			AppGlobal.g_lCurrentConnectClientList.put(Thread.currentThread().getId(), oActiveClient);
			m_oClientThreadHashMap.put(m_sStationAddr, Thread.currentThread());
			
			try {
				m_oDeviceMain = new DeviceMain(m_sStationAddr, m_sDisplayMode);
				m_oDeviceMain.init(m_sLoginId, m_sLoginPwd, m_iPortNum, oActiveClient);
			}
			catch (Exception e) {
				writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Starting app failed");
			}
			
			// Remove client from the list
			AppGlobal.g_lCurrentConnectClientList.remove(Thread.currentThread().getId());
			m_oClientPortHashMap.remove(m_sStationAddr);
			m_oClientThreadHashMap.remove(m_sStationAddr);
		}
	}
	
	class AutoDeviceManager implements Runnable {
		private String m_sStationAddr;
		private String m_sLoginId;
		private String m_sLoginPwd;
		private int m_iPortNum;
		private String m_sDisplayMode;
		private AutoDeviceMain m_oAutoDeviceMain;
		
		public AutoDeviceManager(String sStatAddr, String sLoginId, String sLoginPwd, int iPortNum, String sDisplayMode) {
			m_sStationAddr = sStatAddr;
			m_sLoginId = sLoginId;
			m_sLoginPwd = sLoginPwd;
			m_iPortNum = iPortNum;
			m_sDisplayMode = sDisplayMode;
		}
		
		@Override
		public void run() {
			ClsActiveClient oActiveClient = new ClsActiveClient();
			oActiveClient.setUDID(m_sStationAddr);
			AppGlobal.g_lCurrentConnectClientList.put(Thread.currentThread().getId(), oActiveClient);
			m_oClientThreadHashMap.put(m_sStationAddr, Thread.currentThread());
			
			try {
				m_oAutoDeviceMain = new AutoDeviceMain(m_sStationAddr, m_sDisplayMode);
				m_oAutoDeviceMain.init(m_sLoginId, m_sLoginPwd, m_iPortNum, oActiveClient);
			}
			catch (Exception e) {
				writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Starting app failed");
			}
			
			// Remove client from the list
			AppGlobal.g_lCurrentConnectClientList.remove(Thread.currentThread().getId());
			m_oClientPortHashMap.remove(m_sStationAddr);
			m_oClientThreadHashMap.remove(m_sStationAddr);
		}
	}
	
	class AlertMessage{
		private FuncMessageQueue m_oFuncMessageQueue;
		private String m_sRequestQueueName;
		private AppThread m_oRequestHandler;
		
		public void init() {
			m_oFuncMessageQueue = new FuncMessageQueue();
			
			if(m_oFuncMessageQueue.initConnection()) {
				m_sRequestQueueName = FuncMessageQueue.QUEUE_NAME_ALERT_MESSAGE;
				m_oRequestHandler = new AppThread(new AppThreadManager(), this, "alertMessageHandling", null);
				m_oFuncMessageQueue.createQueue(m_sRequestQueueName, "", "", false);
				m_oFuncMessageQueue.consumeQueue(m_sRequestQueueName, m_oRequestHandler);
			}
		}
		
		public void alertMessageHandling() {
			String sRequestContent = m_oFuncMessageQueue.getRequest();
			if(sRequestContent.isEmpty())
				return;
			try{
				//save alert message to the global alert message list
				JSONObject oRequestJSONObject = new JSONObject(sRequestContent);
				AppGlobal.g_oAlertMessageList.readByJSON(oRequestJSONObject);
			}catch(Exception e) {
				writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Retrieved alert message fail");
			}
		}
	}
	
	public static void triggerAutoRestartPMSShell() {
		PosGeneral oPosGeneral = new PosGeneral();
		oPosGeneral.performAutoRestartPMSShell();
	}
	
	private int getClientPortByUDID(String sUDID) {
		int iPortNo = 0;
		
		if(m_oClientPortHashMap.get(sUDID) != null) {
			iPortNo = m_oClientPortHashMap.get(sUDID);
		}
		else {
			// Get a random port
			do{
				TCPLib oTcpLib = new TCPLib();
				oTcpLib.initServer("127.0.0.1", 0, false);
				iPortNo = oTcpLib.getServerPort();
				oTcpLib.closeListenSocket();
			}while(isClientPortOccupied(iPortNo));
			
			m_oClientPortHashMap.put(sUDID, iPortNo);
		}

//		System.out.println("--=-=-=-=-=" + iPortNo);
		
		return iPortNo;
	}
	
	private boolean isClientPortOccupied(int port) {
		for(Map.Entry<String, Integer> entry : m_oClientPortHashMap.entrySet()) {
			if(entry.getValue() == port) {
				return true;
			}
			continue;
		}
		return false;
	}
	
	private void responseToClient(int iCliSock, int iCliPort, int iErrorNum) {
		String packetString = new String();
		packetString = "{\"port\":" + iCliPort + ",\"error_no\":" + iErrorNum + "}";
//		System.out.println(packetString);
		
		//Return Port B to web service
		if(m_oTCP.writePacket(iCliSock, packetString) == false) {
			//Fail to send response
			writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Send respond packet failed");
		}
		
		m_oTCP.closeClient(iCliSock);
	}
	
	private boolean isPortAvailable(int port) {
		//System.out.println("--------------Testing port " + port);
		Socket testSocket = null;
		try {
			testSocket = new Socket("localhost", port);
			//If the port is able to new, it is already opening
			//Can't be use again
			//System.out.println("--------------Port " + port + " is not available");
			testSocket.close();
			return false;
			//return true; //for gigi only
		} catch (IOException e) {
			//System.out.println("--------------Port " + port + " is available");
			return true;
		}
	}
	
	private boolean internalLogin(String sLoginId, String sLoginPwd, boolean bLoginStandalone) {
		FuncUser oFuncUser = new FuncUser();
		
		if (bLoginStandalone == true)
			oFuncUser.setLoginStandalone(true);
		
		String sErrorMessage = oFuncUser.login(sLoginId, sLoginPwd, true);
		if(sErrorMessage.length() > 0) {
			return false;
		}
		else {
			AppGlobal.g_oFuncUser.set(oFuncUser);
			return true;
		}
	}
}

