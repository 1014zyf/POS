package core.externallib;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

public class HeroHttpClient {
	private final int TIMEOUT = 120000;	// *** Timeout : 120s
	
    private String m_sURL = "";
//	private String m_sCakeSessionId = "";
//	private String m_sCookie = "";
    private CookieSet m_oCookieSet = new CookieSet();
	
	// Response message
	private String m_sResponseStr = "";

	// Last error message from API (e.g. packet format error, logic error)
	private String m_sErrorMessage = "";
	
	// Internal error message (e.g. exception, network error)
	private String m_sInternalErrorMessage = "";
	
	private HttpURLConnection m_oConn;
	
	private Boolean m_bIsLocalConnection = false;
    
	public HeroHttpClient() {
		
	}
	
	/**
	 * Initialize before every request
	 */
	private void callInit() {
		m_sResponseStr = "";
		m_sErrorMessage = "";
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

	// Return the internal error message
	public String getInternalErrorMessage(){
		return m_sInternalErrorMessage;
	}
	
	// Get session ID
	public String getCakeSessionID(){
		return m_oCookieSet.getValue("CAKEPHP");
	}
	
	// Set session ID
	public void setCakeSessionID(String sCakeSessionID){
		m_oCookieSet.setValue("CAKEPHP", sCakeSessionID);
	}
	
	// Get URL
	public String getURL(){
		return m_sURL;
	}
	
	// Set URL
	public void setURL(String sURL){
		if (m_sURL != null && m_sURL.contentEquals(sURL))
			return;
		
		m_sURL = sURL;
		
		if (m_sURL != null && !m_sURL.isEmpty() && m_sURL.charAt(m_sURL.length() - 1) != '/')
			m_sURL += '/';
		
		if (sURL != null) {
		    Pattern pattern = Pattern.compile("http.*://([^/:]*)[:/]*");
		    Matcher matcher = pattern.matcher(sURL);
		    if (matcher.find()) {
		    	String sDomain = matcher.group(1);
		    	InetAddress oUrlAddr = null;
		    	InetAddress oAddr = null;
				try {
					oUrlAddr = InetAddress.getByName(sDomain);
			    	if (oUrlAddr != null) {
			    		m_bIsLocalConnection = false;
			    		m_bIsLocalConnection |= oUrlAddr.isAnyLocalAddress();
			    		m_bIsLocalConnection |= oUrlAddr.isLoopbackAddress();
			    		
			    		oAddr = InetAddress.getLocalHost();
			    		m_bIsLocalConnection |= oAddr.getHostAddress().contentEquals(sDomain);
			    		m_bIsLocalConnection |= oAddr.getHostName().contentEquals(sDomain);
			    	}
				} 
				catch (UnknownHostException e) {
					m_bIsLocalConnection = false;
					e.printStackTrace();
				}
		    }
		}
	}
	
	public boolean test(String sURL) {
		setURL(sURL);
		return baseRequest("", null);
	}
	
	/**
	 * Login and obtain session id for web service call
	 * @return True for login success
	 */
	public boolean login(String sURL, String sId, String sPassword) {
		if (isLogined())
			return true;
		else
			callInit();
		
		setURL(sURL);
		
		LinkedHashMap<String,String> msgParams = new LinkedHashMap<String,String>();
		msgParams.put("username", sId);
		msgParams.put("password", sPassword);

		if (!baseRequest("login", msgParams))
			return false;

		if (!getErrorMessageStr().isEmpty())
			return false;
		
		try {
			JSONObject responseJSONObj = new JSONObject(m_sResponseStr);
			m_oCookieSet.setValue("CAKEPHP", responseJSONObj.getString("sessionId"));
		}
		catch (Exception e) {
			
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
		
		if (!baseRequest("logout", null))
			return false;
		
		m_oCookieSet.reset();
		return true;
	}
	
	/**
	 * Check if web service is logined
	 * @return True for logined
	 */
	public boolean isLogined() {
		if (m_oCookieSet.getValue("CAKEPHP") != null)
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
			if (!m_bIsLocalConnection)
				m_oConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
			m_oConn.setRequestProperty("Cookie", m_oCookieSet.getCookieString());
			m_oConn.setConnectTimeout(TIMEOUT);
			m_oConn.setReadTimeout(TIMEOUT);
			m_oConn.setRequestMethod("POST");
			m_oConn.setDoOutput(true);
		}
		catch (Exception e) {
			e.printStackTrace();
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
	    	e.printStackTrace();
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
				return false;
			}
	    }
	    
//		Record cookie
		String cookieHeader = m_oConn.getHeaderField("Set-Cookie");
		if (cookieHeader != null) {
			m_oCookieSet.join(cookieHeader);
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
			            	bError = true;
			            } else {
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
		            	
		    	    	m_sInternalErrorMessage = "exception - " + StringLib.stackToString(jsone) + ", call path - " + m_sURL + operation + ", request - " + sRequestForLog.toString() + ", response - " + sResponse;
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
