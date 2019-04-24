package core.manager;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import core.Core;
import core.externallib.IniReader;

public class WebServiceManager {
	
	private String m_sWSDL;
	private ConcurrentHashMap<String, WebServiceClient> m_oWebServiceClientList;
	
	public WebServiceManager(){
		m_sWSDL = "";
		m_oWebServiceClientList = new ConcurrentHashMap<String, WebServiceClient>();
	}
	
	public boolean init(){
		IniReader iniReader;
		String sWSDL = "";
		
		try {
			// Read setup from the setup file
			iniReader = new IniReader("cfg/config.ini");
			
			sWSDL = iniReader.getValue("connection", "db_wsdl");
			
		} catch (IOException e) {
			LoggingManager.writeOMErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "Missing setup file (cfg/config.ini)");
			return false;
		}
		
		m_sWSDL = sWSDL;
		
		return true;
	}
	
	public WebServiceClient getWebServiceClient(){
		WebServiceClient oWebServiceClient = null;
		
		// Get an available web service client
		boolean bFound = false;
		for(Map.Entry<String, WebServiceClient>entry:m_oWebServiceClientList.entrySet()){
System.out.println("testtest ==================================== Check session " + m_oWebServiceClientList.size() + ", " + entry.getKey());
			oWebServiceClient = entry.getValue();
			if(oWebServiceClient.isLock() == false){
				// Web service client is available
				oWebServiceClient.setLock(true);
				bFound = true;
				break;
			}
		}
		
		if(bFound == false){
System.out.println("testtest ==================================== Get a new session");
			ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient();

			// No client is available, create a new web service client and perform login
			oWebServiceClient = new WebServiceClient(m_sWSDL);
			// Login a new session
			if(oWebServiceClient.login(oActiveClient.g_sUserName, oActiveClient.g_sUserPassword)){
System.out.println("testtest ==================================== new session: " + oWebServiceClient.getSessionId());
				oWebServiceClient.setLock(true);
				m_oWebServiceClientList.put(oWebServiceClient.getSessionId(), oWebServiceClient);
			}else{
				// Login failed
				oWebServiceClient = null;
			}
		}
		
		return oWebServiceClient;
	}
	
	public void freeWebServiceClient(WebServiceClient oWebServiceClient){
		// Free the client and allow other to use
		oWebServiceClient.setLock(false);
System.out.println("testtest ==================================== Free a session");
	}
	
}
