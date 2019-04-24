package externallib;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * Usage:
 * 		HeroHttpClient heroHttpClient = new HeroHttpClient();
		heroHttpClient.login("http://192.168.99.40/gm4g_demo/chi/http_interface/", user, password);
		heroHttpClient.call("gm", "pos", "getAddress", "");
 */

public class HeroHttpClient implements HttpClientInterface {
	private final int TIMEOUT = 300000;	// *** Timeout : 300s
	
    private String m_sURL = "";
	private String m_sCakeSessionId = "";
	private String m_sCookie = "";
	
	// Response message
	private String m_sResponseStr = "";
	
	// Last error extra informations
	private HashMap<String, String> m_oErrorExtraInfos = new HashMap<String,String>();
	
	// Last error message from API (e.g. packet format error, logic error)
	private String m_sErrorMessage = "";
	
	// Last warning message from API
	private String m_sWarningMessage = "";
	
	// Cert
	private JSONObject m_oLicenseCertJsonObject;
	
	// Internal error message (e.g. exception, network error)
	private String m_sInternalErrorMessage = "";
	
	private HttpURLConnection m_oConn;
    
	public HeroHttpClient(String url) {
		m_sURL = url + "chi/http_interface/";
	}
	
	/**
	 * Initialize before every request
	 */
	private void callInit() {
		m_sResponseStr = "";
		m_sErrorMessage = "";
		m_oErrorExtraInfos = new HashMap<String,String>();
		m_sWarningMessage = "";
		m_oLicenseCertJsonObject = new JSONObject();
		m_sInternalErrorMessage = "";
	}
	
	/**
	 * Get web service raw return.
	 * @return Raw response in String.
	 */
	public String getResponseStr() {
		return m_sResponseStr;
	}
	
	/**
	 * Get web service raw return.
	 * @return Raw error message in String.
	 */
	public String getErrorMessageStr() {
		return m_sErrorMessage;
	}
	
	public HashMap<String, String> getErrorExtraInfoStr(){
		return m_oErrorExtraInfos;
	}
	
	public String getWarningMessage(){
		return m_sWarningMessage;
	}
	
	public JSONObject getLicenseCert(){
		return m_oLicenseCertJsonObject;
	}
	
	// Return the internal error message
	public String getInternalErrorMessage(){
		return m_sInternalErrorMessage;
	}
	
	// Get session ID
	public String getCakeSessionID(){
		return m_sCakeSessionId;
	}
	
	// Set session ID
	public void setCakeSessionID(String sCakeSessionID){
		m_sCakeSessionId = sCakeSessionID;
		if (sCakeSessionID.isEmpty())
			m_sCookie = "";
	}
	
	/**
	 * Login and obtain session id for web service call
	 * @return True for login success
	 */
	public boolean login(String sId, String sPassword) {
		if (isLogined())
			return true;
		else
			callInit();
		LinkedHashMap<String,String> msgParams = new LinkedHashMap<String,String>();
		msgParams.put("username", sId);
		msgParams.put("password", sPassword);
		
		//	Allow login by service role
		JSONObject paramsJSONO = new JSONObject();
		try {
			paramsJSONO.put("allow_service_role", 1);
		} 
		catch (JSONException jsone) {}
		msgParams.put("params", paramsJSONO.toString());

		if (!baseRequest("login", msgParams))
			return false;

		if (!getErrorMessageStr().isEmpty())
			return false;
		
		try {
			JSONObject responseJSONObj = new JSONObject(m_sResponseStr);
			m_sCakeSessionId = responseJSONObj.getString("sessionId");
		}
		catch (Exception e) {
			m_sCakeSessionId = "";
		}

		return true;
	}
	
	
	/**
	 * Logout web service session
	 * @return True for lgout success
	 */
	public boolean logout() {
		if (!isLogined())
			return true;
		
//		if (!baseRequest("logout", null))
//			return false;
		
		m_sCookie = "";
		m_sCakeSessionId = "";
		return true;
	}
	
	/**
	 * Check if web service is logined
	 * @return True for logined
	 */
	public boolean isLogined() {
		if (!m_sCookie.isEmpty())
			return true;
		if (!m_sCakeSessionId.isEmpty())
			return true;
		return false;
	}
	
	/**
	 * Request web service for specific operation with parameters
	 * @param params Parameters for web service call
	 * @return True for call success
	 */
	public boolean call(String sInterface, String sModule, String sFcn, String sParams) {
		callInit();
		
		if (!isLogined()) {
			m_sErrorMessage = "user_not_logged_in";
			return false;
		}
		
		LinkedHashMap<String,String> msgParams = new LinkedHashMap<String,String>();
		msgParams.put("interface", sInterface);
		msgParams.put("module", sModule);
		msgParams.put("fcn", sFcn);
		msgParams.put("params", sParams);
		
//		Profiling.start(this);
		boolean result = baseRequest("call", msgParams);
//		Profiling.end(this, "http request time");
		return result;
	}
	
	/**
	 * Base request web service
	 * @param operation Either "login", "call" or "logout"
	 * @param msgParams Parameters for web service
	 * @return True for request success
	 */
	private boolean baseRequest(String operation, LinkedHashMap<String, String> msgParams) {
		InputStream oReplyStream;
		
		try {
			if (m_oConn != null)
				m_oConn.disconnect();

			m_oConn = (HttpURLConnection)(new URL(m_sURL + operation)).openConnection();
			m_oConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
			if (!m_sCakeSessionId.isEmpty())
				m_oConn.setRequestProperty("Cookie", "CAKEPHP=" + m_sCakeSessionId + ";");
			else if (!m_sCookie.isEmpty())
				m_oConn.setRequestProperty("Cookie", m_sCookie);
			m_oConn.setConnectTimeout(TIMEOUT);
			m_oConn.setReadTimeout(TIMEOUT);
			m_oConn.setRequestMethod("POST");
			m_oConn.setDoOutput(true);
		}
		catch (Exception e) {
			m_sInternalErrorMessage = "exception - " + stackToString(e) + ", call path - " + m_sURL + operation;
		}
		
	    try {
	        String paramsContent = "";
	        
	        if (msgParams != null) {
		        int i = 0;
		        for (String key : msgParams.keySet()) {
		        	if(i > 0) {
		        		paramsContent += "&";
		            }
		        	paramsContent += key + "=" + URLEncoder.encode(msgParams.get(key), "UTF-8");
//System.out.println("(Thread ID: " + Thread.currentThread().getId() + ") Request: key = " + key + ", value = " + msgParams.get(key));
		        	i++;
		        }
	        }
	        
	        DataOutputStream dout = new DataOutputStream(m_oConn.getOutputStream());
	        dout.writeBytes(paramsContent);
	        dout.close();
	        
	        oReplyStream = m_oConn.getInputStream(); 
	    }
	    catch (Exception e) {
	    	m_sInternalErrorMessage = "exception - " + stackToString(e) + ", call path - " + m_sURL + operation;
	        return false;
	    }
		
	    //	Post processing stage
	    String contentEncoding = m_oConn.getHeaderField("Content-Encoding");
	    if (contentEncoding != null) {
		    try {
		    	if (contentEncoding.equalsIgnoreCase("gzip"))
		    		oReplyStream = new GZIPInputStream(oReplyStream);
				else if (contentEncoding.equalsIgnoreCase("deflate")) 
					oReplyStream = new InflaterInputStream(oReplyStream);
		    }
		    catch (Exception e) {
		    	m_sInternalErrorMessage = "exception - " + stackToString(e) + ", call path - " + m_sURL + operation;
				return false;
			}
	    }
	    
//		Record cookie
		String cookieHeader = m_oConn.getHeaderField("Set-Cookie");
		if (cookieHeader != null) {
			m_sCookie = cookieHeader;
		}
		
	    //	Post processing stage
	    try {
	    	Boolean bError = false;
	    	
	        /* Checking response */
	        if (oReplyStream != null) {
	            
	            String sResponse = convertStreamToString(oReplyStream);
//System.out.println("(Thread ID: " + Thread.currentThread().getId() + ") server response: " + sResponse);
	            if(sResponse.replaceAll("\\r|\\n", "").equals("[]")) 
	            	m_sResponseStr = sResponse.replaceAll("\\r|\\n", "");
	            else {
		            try{
			            JSONObject oResponseJSONObject = new JSONObject(sResponse);
			            if(oResponseJSONObject.has("error")) {
			            	m_sErrorMessage = oResponseJSONObject.getString("error");
			            	if(oResponseJSONObject.has("lockTime"))
			            		m_oErrorExtraInfos.put("lockTime", oResponseJSONObject.optString("lockTime"));
			            	bError = true;
			            } else {
			            	// Set warning message if any
			            	if(oResponseJSONObject.has("warning")){
			            		m_sWarningMessage = oResponseJSONObject.getString("warning");
			            	}
			            	
			            	// Set cert if any
			            	if(oResponseJSONObject.has("cert") && oResponseJSONObject.optJSONObject("cert") != null){
			            		String sCert = oResponseJSONObject.getJSONObject("cert").toString();
			            		if(sCert.length() > 0){
			            			m_oLicenseCertJsonObject = new JSONObject(sCert);
			            		}
			            	}
			            	
			            	m_sResponseStr = sResponse;
						}
		            }
		            catch (JSONException jsone){
		            	// Build the request string for log
		    	    	StringBuilder sRequestForLog = new StringBuilder();
		    	    	if (msgParams != null) {
		    		        for (String key : msgParams.keySet()){
		    		        	sRequestForLog.append(key);
		    		        	sRequestForLog.append(": ");
		    		        	sRequestForLog.append(msgParams.get(key));
		    		        	sRequestForLog.append("\n");
		    		        }
		    	    	}
		            	
		    	    	m_sInternalErrorMessage = "exception - " + stackToString(jsone) + ", call path - " + m_sURL + operation + ", request - " + sRequestForLog.toString() + ", response - " + sResponse;
		            	bError = true;
		            }
	            }

	            oReplyStream.close();
	        }
	        else {
	        	// Build the request string for log
    	    	StringBuilder sRequestForLog = new StringBuilder();
    	    	if (msgParams != null) {
    		        for (String key : msgParams.keySet()){
    		        	sRequestForLog.append(key);
    		        	sRequestForLog.append(": ");
    		        	sRequestForLog.append(msgParams.get(key));
    		        	sRequestForLog.append("\n");
    		        }
    	    	}
	        	
    	    	m_sInternalErrorMessage = "No response" + ", call path - " + m_sURL + operation + ", request - " + sRequestForLog.toString();
	        	bError = true;
	        }
	        
	        m_oConn.disconnect();

	        if(bError)
	        	// Error occurs
	        	return false;
	        else
	        	return true;
	    }
	    catch (Exception e) {
	    	// Build the request string for log
	    	StringBuilder sRequestForLog = new StringBuilder();
	    	if (msgParams != null) {
		        for (String key : msgParams.keySet()){
		        	sRequestForLog.append(key);
		        	sRequestForLog.append(": ");
		        	sRequestForLog.append(msgParams.get(key));
		        	sRequestForLog.append("\n");
		        }
	    	}
	    	
	    	m_sInternalErrorMessage = "exception - " + stackToString(e) + ", call path - " + m_sURL + operation + ", request - " + sRequestForLog.toString();
			return false;
		}
	}

	private static String convertStreamToString(InputStream is) {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is), 2 * 1024);
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	    finally {
	        try {
	            is.close();
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
	
	private static String stackToString(Exception e){
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
}