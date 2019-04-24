package core.launcher;

import app.AppGlobal;
import core.*;
import java.io.IOException;
import java.net.SocketException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;

import core.manager.ActiveClient;
import core.manager.LoggingManager;

import org.boris.winrun4j.AbstractService;
import org.boris.winrun4j.ServiceException;
import org.json.JSONObject;

import core.externallib.IniReader;
import core.externallib.TCPLib;
import core.externallib.Util;

public class Main extends AbstractService {
	private static Thread thread=null;
	private static Service service = null;

	public static void main(String[] args){
		// Run in console
		service = new Service();
		service.run();
		
		Util.touch();
	}
	
	// For installing service by WinRun4J
	public int serviceMain(String[] args) throws ServiceException {
		// Create the service to run the main thread
		service = new Service();
		thread=new Thread(service);
		try
		{
			// Set the thread to daemon
			thread.setDaemon(false);

			// Start the main thread
			thread.start();
			
			while (!shutdown) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					
				}
			}
		}
		catch(SecurityException se)
		{

		}
		
		return 0;
	}
	
	// For installing service by JavaService.exe
	/**
	* Stop service
	* @param args
	*/
	public static void StopService(String[] args)
	{
		service.setRunFlag(false);
	}
	/**
	* Start service
	* @param args
	*/
	public static void StartService(String[] args)
	{
		// Create the service to run the main thread
		service = new Service();
		thread=new Thread(service);
		try
		{
			// Set the thread to daemon
			thread.setDaemon(false);

			// Start the main thread
			thread.start();
		}
		catch(SecurityException se)
		{

		}
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
		//String sWSDL = "";
		String sTmp = "";
		int iPortNo = 0;
		String sErrorMessage = "";
		int iErrorNo = NO_ERROR;
		
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
				LoggingManager.writeLauncherErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
				return;
			}
			iPortNo = Integer.parseInt(sTmp);
			
			sTmp = iniReader.getValue("connection", "client_starting_port");
			if(sTmp == null){
				sErrorMessage = "Missing setup for client start port no";
				iErrorNo = INTERNAL_ERROR;
				// Write log
				LoggingManager.writeLauncherErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
				return;
			}
			int iClientStartingPort = Integer.parseInt(sTmp);
			sTmp = iniReader.getValue("connection", "client_port_count");
			if(sTmp == null){
				sErrorMessage = "Missing setup for client port count";
				iErrorNo = INTERNAL_ERROR;
				// Write log
				LoggingManager.writeLauncherErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
				return;
			}
			int iClientPortCount = Integer.parseInt(sTmp);
			Core.g_oClientManager.setPortConfig(iClientStartingPort, iClientPortCount);
			
			//Open Launcher localhost Port
			m_oTCP = new TCPLib();
			if(m_oTCP.initServer("127.0.0.1", iPortNo, false) == false) {
				sErrorMessage = "Fail to init port " + iPortNo;
				LoggingManager.writeLauncherErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
				iErrorNo = INTERNAL_ERROR;
				return;
			}
			
			// Register the server socket channel
			ServerSocketChannel oChannel = m_oTCP.getSocketChannel();
			SelectionKey oLauncherKey = null;
			oLauncherKey = oChannel.register(oSelector, SelectionKey.OP_ACCEPT);

			while(getRunFlag())
			{
				int n = oSelector.select(100);
				if(n == 0)
                {
					continue;
                }
                java.util.Iterator<SelectionKey> iterator = oSelector.selectedKeys().iterator();
                while(iterator.hasNext()) {
                    SelectionKey oIncomingSelectionkey = (SelectionKey)iterator.next();
                    
                    iterator.remove();
                    
                    // Client connect
                    if(oIncomingSelectionkey.isAcceptable() && oIncomingSelectionkey == oLauncherKey) {	
						// Listen the port to handle client request from web service
						iErrorNo = NO_ERROR;
						int iClientSockId = m_oTCP.listen();

						System.out.println("New client - incoming message : " + m_oTCP.getPacket());
						
						if(iClientSockId > 0) {						//client socket is available
							JSONObject jRequestPacket = new JSONObject(m_oTCP.getPacket());
							
							// Check if this is "execute" request
							String sTask = jRequestPacket.optString("task");
							if (!sTask.isEmpty()) {
								if (sTask.equals("terminateServer")) {
									String sParams = jRequestPacket.optString("params");
									if (sParams.isEmpty()) {
										// Error case
										responseToClient(iClientSockId, 0, 1);
										continue;
									}
									
									JSONObject jParams = new JSONObject(sParams);
									String sUDID = jParams.optString("Udid");
									if (sUDID.isEmpty()) {
										// Error case
										responseToClient(iClientSockId, 0, 1);
										continue;
									}
									
									ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient(sUDID);
									if (oActiveClient != null) {
										oActiveClient.close();
										Core.g_oClientManager.removeActiveClient(sUDID);
									}
									
									responseToClient(iClientSockId, 0, 0);
								}
								else {
									// Error case
									responseToClient(iClientSockId, 0, 1);
								}
								
								// Finish handle task
								continue;
							}
							
							//	Get incoming client UDID, login, password, card no
							String sIncomingClientUDID = jRequestPacket.getString("udid");
							String sIncomingClientLoginID = jRequestPacket.getString("login");
							String sIncomingClientLoginPassword = jRequestPacket.getString("password");
							String sIncomingClientSwipeCardLogin = jRequestPacket.optString("swipe_card_login", "");
							String sIncomingClientDisplayMode = jRequestPacket.optString("display_mode", "horizontal_desktop");
							
							//	Generate/retrieve port number for device
							int iClientPort;
							boolean bNewConnectionBoolean = false;
							ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient(sIncomingClientUDID);
							if (oActiveClient != null) {
								iClientPort = oActiveClient.g_iPort;
								bNewConnectionBoolean = Util.isPortAvailable(iClientPort);	//if the port is not using
							}
							else{
								iClientPort = Core.g_oClientManager.getNextAvailablePort();
								if (iClientPort < 0) {
//									########### error, no available port
								}
								
								bNewConnectionBoolean = true;
							}

							if (bNewConnectionBoolean) {					//if the port is not using
								if (sIncomingClientDisplayMode.equals(AppGlobal.DISPLAY_MODE.no_display.name())) {
									//	Reserve for auto station
								} 
								else {
									// Launch app with IP and Port
									oActiveClient = new ActiveClient(iClientPort, sIncomingClientUDID, sIncomingClientLoginID, sIncomingClientLoginPassword, sIncomingClientSwipeCardLogin, sIncomingClientDisplayMode);
									Core.g_oClientManager.addActiveClient(sIncomingClientUDID, oActiveClient);
								
									oActiveClient.start();
								}
							}
							else{
								System.out.println("resume");
							}
							
							//	No Error
							responseToClient(iClientSockId, iClientPort, iErrorNo);
						}
						else {
							//Error connection
							sErrorMessage = "Cannot retrieve client socket";
							LoggingManager.writeLauncherErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
						}
                    }
                }
			}
		}
		catch (SocketException sce) {
			sErrorMessage = "Socket Error";
			LoggingManager.writeLauncherErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
			return;
		}
		catch (IOException e) {
			sErrorMessage = "Missing setup file (cfg/config.ini)";
			// Write log
			LoggingManager.writeLauncherErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
			return;
		}
		catch (Exception e){
			e.printStackTrace();
			sErrorMessage = "Unknown Error";
			LoggingManager.writeLauncherErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
			return;
		}
	}
	
//	class AppMain implements Runnable {
//		private int m_iClientPort;
//		private String m_sClientUDID;
//		private String m_sUserName;
//		private String m_sUserPassword;
//		private String m_sUserCardNo;
//		
//		public AppMain(int iPort, String sUDID, String sUsrName, String sUsrPW, String sUserCardNo) {
//			m_iClientPort = iPort;
//			m_sClientUDID = sUDID;
//			m_sUserName = sUsrName;
//			m_sUserPassword = sUsrPW;
//			m_sUserCardNo = sUserCardNo;
//		}
//		
//		@Override
//		public void run() {
//			String[] arguments = new String[] {Integer.toString(m_iClientPort), m_sClientUDID, m_sUserName, m_sUserPassword, m_sUserCardNo};
//			try {
//				app.Main.main(arguments);
//			}
//			catch (Exception e) {
//				LoggingManager.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Starting app failed");
//			}
//
//			// Remove client from the list
//			core.g_oClientManager.removeActiveClient(m_sClientUDID);
//		}
//	}
	
	private void responseToClient(int iCliSock, int iCliPort, int iErrorNum) {
		String packetString = new String();
		packetString = "{\"port\":" + iCliPort + ",\"error_no\":" + iErrorNum + "}";

		//	Return Port B to web service
		if(m_oTCP.writePacket(iCliSock, packetString) == false) {
			//	Fail to send response
			LoggingManager.writeLauncherErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Send respond packet failed");
		}
		
		m_oTCP.closeClient(iCliSock);
	}
}

