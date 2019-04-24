package core.manager;

import org.json.JSONObject;

import core.Core;
import core.lang.LangResource;
import core.virtualui.UIManager;

public class ActiveClient {
	
	public int g_iPort;
	public Thread g_oThread;
	
	public ConnectionManager g_oConnectionManager;
	public UIManager g_oUIManager;
	public ResponsePacketManager g_oResponsePacketManager;
	public LoggingManager g_oLoggingManager;
	public ControllerManager g_oControllerManager;
	public DeviceManager g_oDeviceManager;
	public WebServiceManager g_oWebServiceManager;
	public WorkerThreadManager g_oWorkerThreadManager;
	public LangResource g_oLangResource;
	
	public String g_sUDID;
	public String g_sUserName;
	public String g_sUserPassword;
	public String g_sUserCardNo;
	public String g_sClientDisplayMode;
	
	public ActiveClient(int iPort, String sUDID, String sUserName, String sUserPassword, String sUserCardNo, String sClientDisplayMode) {
		g_iPort = iPort;
		g_sUDID = sUDID;
		g_sUserName = sUserName;
		g_sUserPassword = sUserPassword;
		g_sUserCardNo = sUserCardNo;
		g_sClientDisplayMode = sClientDisplayMode;
	}
	
	public boolean init() {
		g_oConnectionManager = new ConnectionManager();
		g_oUIManager = new UIManager();
		g_oResponsePacketManager = new ResponsePacketManager();
		g_oLoggingManager = new LoggingManager();
		g_oDeviceManager = new DeviceManager();
		g_oControllerManager = new ControllerManager();
		g_oWebServiceManager = new WebServiceManager();
		if(!g_oWebServiceManager.init()){
			// Cannot initialize web service manager
			return false;
		}
		g_oWorkerThreadManager = new WorkerThreadManager();		
		g_oLangResource = new LangResource();
		
//		g_oDeviceManagerElement = new VirtualUIBasicElement(HeroActionProtocol.View.Type.FRAME);
		
		return true;
	}
	
	public void start() {
		if (g_oThread != null && g_oThread.isAlive())
			g_oThread.stop();
		
		g_oThread = new Thread(new Runnable() {
			@Override
			public void run() {
				//	Initialize
				ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient(g_sUDID);
				if (oActiveClient == null)
					return;
				
				Core.g_oClientManager.registerCurrentThread(oActiveClient);
				if (!oActiveClient.init())
					return;	// Fail to initial client
				
				// Initialize listen socket
				if (oActiveClient.g_oConnectionManager.init(g_sUDID, g_iPort) == false)
					return;	// Fail to initial socket
				
				// Main loop
				while (oActiveClient.g_oConnectionManager.isConnected()) {
					// Listen to client
					JSONObject oPacketJSONObject = oActiveClient.g_oConnectionManager.recvPacket();
					if (oPacketJSONObject != null) {
						// Packet is received
						// Create thread to handle client event
						Thread oThread = new Thread(new HandleEventThread(oActiveClient, oPacketJSONObject));
						oThread.start();
					}
				}
			}
		});
		g_oThread.start();
	}
	
	public void close() {
		//	Logout user side
		g_oUIManager.logout();
		
		//	Stop main listening connection thread
		g_oConnectionManager.close();
		g_oControllerManager.close();
		
		// Kill the client thread
		g_oThread.interrupt();

		// Remove client from client manager
		Core.g_oClientManager.removeActiveClient(g_sUDID);
	}
}
