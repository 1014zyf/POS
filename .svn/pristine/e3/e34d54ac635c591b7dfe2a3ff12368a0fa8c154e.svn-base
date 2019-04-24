package app.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.json.JSONException;

import externallib.HeroHttpClient;
import externallib.IniReader;
//import externallib.WsClient;

public class OmWsClient {
	
	private String m_sURL = "";
	private String m_sLogin = "";
	private String m_sPassword = "";
	private ConcurrentHashMap<String, Long> m_sSessionId;
	private String m_sLastLoginSession = "";
	private String m_sLastLoginCardNo = "";
	
	//protected int error;	//1: login error, 2: call error, 3: result return error
	protected static ThreadLocal<JSONObject> m_jResponse;
	
	// Flag to determine the failure of OM
	private boolean m_bNeedRestart;
	
	// Flag to stop ALL process of OM
	private boolean m_bEnable;
	
	// Last error message
	private static ThreadLocal<String> m_sErrorMessage;
	
	// Last warning message
	private static ThreadLocal<String> m_sWarningMessage;
	
	// License Cert
	private static ThreadLocal<JSONObject> m_oLicenseCertJSONObject;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<OMListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(OMListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(OMListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
	
	// Set the error message
	public void setLastErrorMessage(String sErrorMessage){
		m_sErrorMessage.set(sErrorMessage);
	}
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage.get();
	}
	
	// Set the warning message
	public void setLastWarningMessage(String sWarningMessage){
		m_sWarningMessage.set(sWarningMessage);
	}
	
	// Return the warning message
	public String getLastWarningMessage(){
		return m_sWarningMessage.get();
	}
	
	// Set the license cert
	public void setLicenseCert(JSONObject oLicenseCertJSONObject){
		m_oLicenseCertJSONObject.set(oLicenseCertJSONObject);
	}
	
	// Return the license cert
	public JSONObject getLicenseCert(){
		return m_oLicenseCertJSONObject.get();
	}
	
	// Is OM failed?
	public boolean needRestart(){
		return m_bNeedRestart;
	}
	
	// Set the response object
	public void setResponse(JSONObject oResponse){
		m_jResponse.set(oResponse);
	}
	
	// Get the response object
	public JSONObject getResponse(){
		return m_jResponse.get();
	}
	
	//init object
	public OmWsClient() {
		//init WsClient with web service url
		listeners = new ArrayList<OMListener>();
		m_bNeedRestart = false;
		m_bEnable = true;
		m_sSessionId = new ConcurrentHashMap<String, Long>();
		m_sLogin = "";
		m_sPassword = "";
		m_jResponse = new ThreadLocal<JSONObject>();
		m_sErrorMessage = new ThreadLocal<String>();
		m_sWarningMessage = new ThreadLocal<String>();
		m_oLicenseCertJSONObject = new ThreadLocal<JSONObject>();
	}
	
	//web service login
	public boolean login(String sLogin, String sPassword, boolean bNewLogin) {

		try {
			// Read setup from the setup file
			IniReader iniReader = new IniReader("cfg/config.ini");
			
			m_sURL = iniReader.getValue("connection", "db_wsdl");
			if (m_sURL != null && !m_sURL.isEmpty() && m_sURL.charAt(m_sURL.length() - 1) != '/')
				m_sURL += '/';
			m_sURL = m_sURL.replace("chi/http_interface", "");
			m_sURL = m_sURL.replace("eng/http_interface", "");
			m_sURL = m_sURL.replace("cn/http_interface", "");
			m_sURL = m_sURL.replace("jpn/http_interface", "");
			m_sURL = m_sURL.replace("kor/http_interface", "");
			m_sURL = m_sURL.replace("http_interface", "");
		} 
		catch (IOException e) {
			setLastErrorMessage("Missing setup file (cfg/config.ini)");
			OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sLogin, getLastErrorMessage());
			return false;
		}
		
		// Get a new session
		//WsClient oWsClient = new WsClient();
		HeroHttpClient oWsClient = new HeroHttpClient(m_sURL);
			
		if(!oWsClient.login(sLogin, sPassword)) {
			setLastErrorMessage(oWsClient.getErrorMessageStr());
			OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sLogin, getLastErrorMessage());
			return false;
		}else {
			// Set warning message if any
			setLastWarningMessage(oWsClient.getWarningMessage());
			
			// Set the license cert
			setLicenseCert(oWsClient.getLicenseCert());
			
			// Get session ID
			m_sSessionId.put(oWsClient.getCakeSessionID(), (long) 0);
		}

		// Get login ID
		m_sLogin = sLogin;
		// Get password
		m_sPassword = sPassword;
		
		if(bNewLogin){
			// Store last login session ID
			m_sLastLoginSession = oWsClient.getCakeSessionID();
			
			// Clear the last swipe card no.
			m_sLastLoginCardNo = "";
		}else{
			// Check if switch user information in session
			if(m_sLastLoginCardNo.length() > 0){
				JSONObject requestJSONObject = new JSONObject();
				
				try {
					requestJSONObject.put("cardNumber", m_sLastLoginCardNo);			
				}catch (JSONException jsone) {
					jsone.printStackTrace();
				}
				
				// Switch the user information				
				oWsClient.call("gm", "user", "loginByUserCardNumber", requestJSONObject.toString());
			}
		}

		return true;
	}
	
	//call web service
	public boolean call(String sWsInterface, String sModule, String sFcnName, String sParam, boolean bIgnoreErrorForceLogout) {
		int iRetryCount = 0;
		
		// OM may be disable by the kill process
		if(!m_bEnable)
			return false;
		
		while(iRetryCount <= 3){
		
			//WsClient oWsClient = new WsClient();
			HeroHttpClient oWsClient = new HeroHttpClient(m_sURL);
			// Set URL from previous login
			//oWsClient.setURL(m_sURL);
			// Set session ID that is not used
			int iSessionRetryCount = 0;
			for(iSessionRetryCount=0; iSessionRetryCount<10; iSessionRetryCount++){
				Boolean bFound = false;
				for(Map.Entry<String, Long>entry:m_sSessionId.entrySet()){
					if(entry.getValue() == 0){
						long lCurrentThreadID = Thread.currentThread().getId();
						oWsClient.setCakeSessionID(entry.getKey());
						entry.setValue(lCurrentThreadID);
						bFound = true;
						break;
					}
				}
				if(bFound == false){
					// No session available
					// Create a new session
					this.login(m_sLogin, m_sPassword, false);
				}else {
					break;
				}
			}
			if(iSessionRetryCount >= 10){
				// Cannot get session
				String sErrorMessage = "Cannot get session - login ID: " + m_sLogin +  ", interface: " + sWsInterface + ", module: " + sModule + ", fcn: " + sFcnName + ", param: " + sParam;
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), m_sLogin, sErrorMessage);
				
				this.setLastErrorMessage("Session timeout");
				
				m_bNeedRestart = true;
				fireRestartEvent();
				
				return false;
			}
			
			//long startTime = System.currentTimeMillis();
			//if(sFcnName.equals("saveCheck"))
			//	System.out.println(sParam);
			if (oWsClient.call(sWsInterface, sModule, sFcnName, sParam)) {
				// Free the session ID
				if(m_sSessionId.containsKey(oWsClient.getCakeSessionID())){
					m_sSessionId.put(oWsClient.getCakeSessionID(), (long) 0);
				}
				
				if(oWsClient.getResponseStr().equals("[]")) {
					setResponse(null);
					return true;
				}else {
					try {
						setResponse(new JSONObject(oWsClient.getResponseStr()));
						//if(sModule.equals("pos")){ // && sFcnName.equals("saveCheck")) {
						//	System.out.println(sModule+", RESPONSE: "+sFcnName+" ------- "+oWsClient.getResponseStr());

						//	long endTime = System.currentTimeMillis();

						//	System.out.println("["+sModule+"]"+sFcnName+" took " + (endTime - startTime) + " milliseconds | "+oWsClient.getResponseStr());
						//}
						return true;
					}catch (JSONException jsone) {
						String sErrorMessage = "Interface: " + sWsInterface + ", module: " + sModule + ", fcn: " + sFcnName + ", param: " + sParam + ", exception:" + OmWsClientGlobal.stackToString(jsone) + ", response - " + oWsClient.getResponseStr();
						OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), m_sLogin, sErrorMessage);
						return false;
					}
				}
			}else {
				// Free the session ID
				if(m_sSessionId.containsKey(oWsClient.getCakeSessionID())){
					m_sSessionId.put(oWsClient.getCakeSessionID(), (long) 0);
				}
				
				boolean bNeedRetry = false;
				if(oWsClient.getInternalErrorMessage().length() > 0){
					// Exception error
					setLastErrorMessage(oWsClient.getInternalErrorMessage());
					
					// Need retry
					bNeedRetry = true;
				}else{
					// Error return from API
					setLastErrorMessage(oWsClient.getErrorMessageStr());
					
					// Error = user_not_logged_in --> User session is missing
					// Error = empty --> may cause by MySQL problem
					if(oWsClient.getErrorMessageStr().equals("user_not_logged_in") || oWsClient.getErrorMessageStr().equals("")){
						// Perform re-login
						this.logout();
						this.login(m_sLogin, m_sPassword, false);
						
						// After login, need retry
						bNeedRetry = true;
					}else{					
						// No need to retry
						bNeedRetry = false;
					}
				}
				
				String sErrorMessage = "Interface: " + sWsInterface + ", module: " + sModule + ", fcn: " + sFcnName + ", param: " + sParam + ", error: " + this.getLastErrorMessage();
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), m_sLogin, sErrorMessage);
				
				if(!bNeedRetry)
					break;
				
				// Sleep for a while
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				// Support retry
				iRetryCount++;
			}
		}
		
		// Retry fail
		if(iRetryCount > 3 && !bIgnoreErrorForceLogout){
			String sErrorMessage = "Interface: " + sWsInterface + ", module: " + sModule + ", fcn: " + sFcnName + ", param: " + sParam + ", error: retry excess 3 times";
			OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), m_sLogin, sErrorMessage);
			
			m_bNeedRestart = true;
			fireRestartEvent();
		}
		
		return false;
	}
	
	//logout web service
	public boolean logout() {		
		
		//WsClient oWsClient = new WsClient();
		HeroHttpClient oWsClient = new HeroHttpClient(m_sURL);
		// Set URL from previous login
		//oWsClient.setURL(m_sURL);
		
		ArrayList<String> sSessionList = new ArrayList<String>();
		for(Map.Entry<String, Long>entry:m_sSessionId.entrySet()){
			sSessionList.add(entry.getKey());
		}
		
		while(sSessionList.size() > 0){
			String sSessionId = sSessionList.remove(0);
			
			// Set session ID from previous login
			oWsClient.setCakeSessionID(sSessionId);
			
			oWsClient.logout(); 
			
			
		}
		
		// Clear the session pool
		m_sSessionId.clear();
		
		return true;
	}

	//logout other session
	public boolean logoutOtherSession() {		
		
		//WsClient oWsClient = new WsClient();
		HeroHttpClient oWsClient = new HeroHttpClient(m_sURL);
		// Set URL from previous login
		//oWsClient.setURL(m_sURL);
		
		ArrayList<String> sSessionList = new ArrayList<String>();
		for(Map.Entry<String, Long>entry:m_sSessionId.entrySet()){
			if(entry.getKey().equals(m_sLastLoginSession))
				// Skip the last login session
				continue;
			
			sSessionList.add(entry.getKey());
		}
		
		while(sSessionList.size() > 0){
			String sSessionId = sSessionList.remove(0);
			
			// Set session ID from previous login
			oWsClient.setCakeSessionID(sSessionId);
			
			oWsClient.logout(); 
			
			
		}
		
		// Clear the session pool
		m_sSessionId.clear();
		
		// Add back the last login session
		m_sSessionId.put(m_sLastLoginSession, (long) 0);
		
		return true;
	}
	
	public void fireRestartEvent(){
		for (OMListener listener : listeners) {
			listener.OmWsClient_Restart(getLastErrorMessage());
        }
	}
	
	public String getSessionID(){
		for(Map.Entry<String, Long>entry:m_sSessionId.entrySet()){
			if(entry.getValue() == 0){
				return entry.getKey();
			}
		}
		return "";
	}
	
	public void setLastLoginCardNo(String sCardNo){
		m_sLastLoginCardNo = sCardNo;
	}
	
	public void setEnable(boolean bEnable){
		m_bEnable = bEnable;
	}
}
