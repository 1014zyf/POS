package core.manager;

import org.json.JSONException;
import org.json.JSONObject;

import core.Core;
import core.externallib.HeroHttpClient;
import core.externallib.StringLib;

public class WebServiceClient {
	
	private String m_sWSDL;
	private String m_sSessionId;
	private String m_sErrorMessage;
	private boolean m_bLock;
	private JSONObject m_jResponse;
	
	public WebServiceClient(String sWSDL){
		m_sWSDL = sWSDL;
		m_sSessionId = "";
		m_sErrorMessage = "";
		m_bLock = false;
	}
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage;
	}
	
	// Return session ID
	public String getSessionId(){
		return m_sSessionId;
	}
	
	// Lock the client
	public void setLock(boolean bLock){
		m_bLock = bLock;
	}
	
	// Check if the client is locked by another
	public boolean isLock(){
		return m_bLock;
	}
	
	// Get JSON response
	public JSONObject getResponse(){
		return m_jResponse;
	}
	
	// Login
	public boolean login(String sLogin, String sPassword) {
		HeroHttpClient oWsClient = new HeroHttpClient();
		
		if(!oWsClient.login(m_sWSDL, sLogin, sPassword)) {
			m_sErrorMessage = oWsClient.getErrorMessageStr();
			LoggingManager.writeOMErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), getLastErrorMessage());
			return false;
		}
		
		// Store the session ID
		m_sSessionId = oWsClient.getCakeSessionID();
		
		return true;
	}
	
	// Call web service
	public boolean call(String sWsInterface, String sModule, String sFcnName, String sParam, boolean bIgnoreErrorForceLogout) {
		int iRetryCount = 0;
		
		ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient();
		
		while(iRetryCount <= 3){
		
			//WsClient oWsClient = new WsClient();
			HeroHttpClient oWsClient = new HeroHttpClient();
			// Set URL
			oWsClient.setURL(m_sWSDL);
			// Set session ID
			oWsClient.setCakeSessionID(m_sSessionId);
			
			//long startTime = System.currentTimeMillis();
			if (oWsClient.call(sWsInterface, sModule, sFcnName, sParam)) {
				if(oWsClient.getResponseStr().equals("[]")) {
					m_jResponse = null;
					return true;
				}else {
					try {
						m_jResponse = new JSONObject(oWsClient.getResponseStr());
						return true;
					}catch (JSONException jsone) {
						m_sErrorMessage = "Interface: " + sWsInterface + ", module: " + sModule + ", fcn: " + sFcnName + ", param: " + sParam + ", exception:" + StringLib.stackToString(jsone) + ", response - " + oWsClient.getResponseStr();
						LoggingManager.writeOMErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), m_sErrorMessage);
						return false;
					}
				}
			}else {
				boolean bNeedRetry = false;
				if(oWsClient.getInternalErrorMessage().length() > 0){
					// Exception error
					m_sErrorMessage = oWsClient.getInternalErrorMessage();
					
					// Need retry
					bNeedRetry = true;
				}else{
					// Error return from API
					m_sErrorMessage = oWsClient.getErrorMessageStr();
					
					// User session is missing
					if(oWsClient.getErrorMessageStr().equals("user_not_logged_in")){
						// Perform re-login
						this.logout();
						this.login(oActiveClient.g_sUserPassword, oActiveClient.g_sUserName);
						
						// After login, need retry
						bNeedRetry = true;
					}else{					
						// No need to retry
						bNeedRetry = false;
					}
				}
				
				String sErrorMessage = "Interface: " + sWsInterface + ", module: " + sModule + ", fcn: " + sFcnName + ", param: " + sParam + ", error: " + this.getLastErrorMessage();
				LoggingManager.writeOMErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
				
				if(!bNeedRetry)
					break;
				
				// Sleep for a while
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
				
				// Support retry
				iRetryCount++;
			}
		}
		
		// Retry fail
		if(iRetryCount > 3 && !bIgnoreErrorForceLogout){
			String sErrorMessage = "Interface: " + sWsInterface + ", module: " + sModule + ", fcn: " + sFcnName + ", param: " + sParam + ", error: retry excess 3 times";
			LoggingManager.writeOMErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sErrorMessage);
			
			// Need restart station
			//Sunny
			// TODO
		}
		
		return false;
	}
	
	// Logout
	public boolean logout() {		
		//WsClient oWsClient = new WsClient();
		HeroHttpClient oWsClient = new HeroHttpClient();
		// Set URL from previous login
		oWsClient.setURL(m_sWSDL);
		// Set session ID from previous login
		oWsClient.setCakeSessionID(m_sSessionId);
		
		oWsClient.logout(); 
		
		return true;
	}

}
