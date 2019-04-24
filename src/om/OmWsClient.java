package om;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import externallib.HeroHttpClient;
import externallib.HeroHttpStreamClient;
import externallib.HttpClientInterface;
import externallib.IniReader;

public class OmWsClient {
	private boolean m_bIsStreamHttpClient = false;
	
	private String m_sURL = "";
	private String m_sLogin = "";
	private String m_sPassword = "";
	private Map<HttpClientInterface, Long> m_sSessionId;	//	Use map to compatible using keyset in java 8 jre
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
	
	// Last error extra information
	private static ThreadLocal<HashMap<String, String>> m_sErrorExtraInfo;
	
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
	
	// Set the error extra information
	public void setLastErrorExtraInfo(HashMap<String, String> oExtraErrorInfos){
		m_sErrorExtraInfo.set(oExtraErrorInfos);
	}
	
	// Return the error extra information
	public HashMap<String, String> getLastErrorExtraInfo(){
		return m_sErrorExtraInfo.get();
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
	
	public HttpClientInterface newHttpClient() {
		if (m_bIsStreamHttpClient)
			return new HeroHttpStreamClient(m_sURL);
		return new HeroHttpClient(m_sURL);
	}
	
	//init object
	public OmWsClient() {
		//init WsClient with web service url
		listeners = new ArrayList<OMListener>();
		m_bNeedRestart = false;
		m_bEnable = true;
		m_sSessionId = new ConcurrentHashMap<HttpClientInterface, Long>();	//	Use map to compatible using keyset in java 8 jre
		m_sLogin = "";
		m_sPassword = "";
		m_jResponse = new ThreadLocal<JSONObject>();
		m_sErrorMessage = new ThreadLocal<String>();
		m_sErrorExtraInfo = new ThreadLocal<HashMap<String, String>>();
		m_sWarningMessage = new ThreadLocal<String>();
		m_oLicenseCertJSONObject = new ThreadLocal<JSONObject>();
		
		try {
			// Read setup from the setup file
			IniReader iniReader = new IniReader("cfg/config.ini");
			
			String sFastWsdl = iniReader.getValue("connection", "fast_wsdl");
			if (sFastWsdl != null && sFastWsdl.contentEquals("true"))
				m_bIsStreamHttpClient = true;
		} 
		catch (IOException e) {}
	}
	
	//web service login
	public boolean login(String url, String sLogin, String sPassword, boolean bNewLogin) {
		// Get login ID
		m_sLogin = sLogin;
		// Get password
		m_sPassword = sPassword;
		
		m_sURL = url;
		
		// Get a new session
		//WsClient oWsClient = new WsClient();
		HttpClientInterface oWsClient = newHttpClient();
			
		if (!oWsClient.login(sLogin, sPassword)) {
			setLastErrorMessage("API error: " + oWsClient.getErrorMessageStr() + "; Internal error: " + oWsClient.getInternalErrorMessage());
			setLastErrorExtraInfo(oWsClient.getErrorExtraInfoStr());
			OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sLogin, getLastErrorMessage());
			return false;
		}
		else {
			// Set warning message if any
			setLastWarningMessage(oWsClient.getWarningMessage());
			
			// Set the license cert
			setLicenseCert(oWsClient.getLicenseCert());
			
			// Get session ID
			m_sSessionId.put(oWsClient, (long) 0);
		}
		
		if(bNewLogin){
			// Store last login session ID
//			m_sLastLoginSession = oWsClient;
			
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
		
		while(iRetryCount < 4){
			HttpClientInterface oWsClient = null;
			// Set session ID that is not used
			int iSessionRetryCount = 0;
			for (iSessionRetryCount = 0; iSessionRetryCount < 100; iSessionRetryCount++) {
				synchronized(this){
					for (HttpClientInterface httpClientInterface : m_sSessionId.keySet()) {
						if (!httpClientInterface.isLogined()) {
							m_sSessionId.keySet().remove(httpClientInterface);
							continue;
						}
						if (m_sSessionId.get(httpClientInterface) == 0) {
							oWsClient = httpClientInterface;
							m_sSessionId.put(httpClientInterface, Thread.currentThread().getId());
							break;
						}
					}
				}
				if (oWsClient != null)
					break;
				
				// No session available
				//	Create a new session
				this.login(m_sURL, m_sLogin, m_sPassword, false);
			}
			if(iSessionRetryCount >= 10){
				// Cannot get session
				String sErrorMessage = "Cannot get session - login ID: " + m_sLogin +  ", interface: " + sWsInterface + ", module: " + sModule + ", fcn: " + sFcnName + ", param: " + sParam;
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), m_sLogin, sErrorMessage);
				
				this.setLastErrorMessage("Session timeout");
				
				if (!bIgnoreErrorForceLogout) {
					m_bNeedRestart = true;
					fireRestartEvent();
				}
				
				return false;
			}
			
//			long startTime = System.currentTimeMillis();
			if (oWsClient.call(sWsInterface, sModule, sFcnName, sParam)) {
//				AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "["+sModule+"]"+sFcnName+" took " + (System.currentTimeMillis() - startTime) + "ms");
//				System.out.println("["+sModule+"]"+sFcnName+" took " + (System.currentTimeMillis() - startTime) + "ms");
				// Free the session ID
				if(m_sSessionId.containsKey(oWsClient)){
					m_sSessionId.put(oWsClient, (long) 0);
				}
				
				if(oWsClient.getResponseStr().equals("[]")) {
					setResponse(null);
					return true;
				}else {
					try {
						setResponse(new JSONObject(oWsClient.getResponseStr()));
						//if(sModule.equals("pos")){ // && sFcnName.equals("saveCheck")) {
						//	System.out.println(sModule+", RESPONSE: "+sFcnName+" ------- "+oWsClient.getResponseStr());
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
				if(m_sSessionId.containsKey(oWsClient)){
					m_sSessionId.put(oWsClient, (long) 0);
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
						this.login(m_sURL, m_sLogin, m_sPassword, false);
						
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
				
				//	Sleep for a while except first retry
				if (iRetryCount > 0) {
					try {
						Thread.sleep(500);
					} 
					catch (InterruptedException e) {}
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
		for (Map.Entry<HttpClientInterface, Long>entry : m_sSessionId.entrySet()){
			HttpClientInterface sSessionId = entry.getKey();
			
			//	For fast API only
			if (sSessionId instanceof HeroHttpStreamClient)
				sSessionId.logout();
		}
		
		// Clear the session pool
		m_sSessionId.clear();
		
		return true;
	}

	//logout other session
	public boolean logoutOtherSession() {
		HttpClientInterface availableSession = null;
		
		for (Map.Entry<HttpClientInterface, Long>entry : m_sSessionId.entrySet()){
			HttpClientInterface sSessionId = entry.getKey();
			Long threadId = entry.getValue();
			if (availableSession == null && threadId == 0) {
				availableSession = sSessionId;
				continue;
			}
			
			//	For fast API only
			if (sSessionId instanceof HeroHttpStreamClient)
				sSessionId.logout();
		}
		
		// Clear the session pool
		m_sSessionId.clear();
		
		// Add back the last login session
		if (availableSession != null)
			m_sSessionId.put(availableSession, (long) 0);
		
		return true;
	}
	
	public void fireRestartEvent(){
		for (OMListener listener : listeners) {
			listener.OmWsClient_Restart(getLastErrorMessage());
        }
	}
	
	public String getSessionID(){
		for(Map.Entry<HttpClientInterface, Long>entry:m_sSessionId.entrySet()){
			if(entry.getValue() == 0){
				return entry.getKey().getCakeSessionID();
			}
		}
		return null;
	}
	
	public void setLastLoginCardNo(String sCardNo){
		m_sLastLoginCardNo = sCardNo;
	}
	
	public void setEnable(boolean bEnable){
		m_bEnable = bEnable;
	}
	
	public String getLoginId() {
		return m_sLogin;
	}
	
	public String getPassword() {
		return m_sPassword;
	}
}
