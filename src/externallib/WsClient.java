package externallib;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WsClient {
	private static final String NAMESPACE = "urn:WsnInterface";	
	private static final String SOAP_ACTION = "call";
	private final int TIMEOUT = 120000;	// *** Timeout : 120s
	
	private String m_sURL = "";
	private String m_sSessionId = "";
	
	// Response message
	private String m_sResponseStr = "";

	// Last error message
	private String m_sErrorMessage = "";
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage;
	}
	
	/**
	 * Initialize before every request
	 */
	private void callInit() {
		m_sResponseStr = "";
		m_sErrorMessage = "";
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

	// Get session ID
	public String getCakeSessionID(){
		return m_sSessionId;
	}
	
	// Set session ID
	public void setCakeSessionID(String sSessionID){
		m_sSessionId = sSessionID;
	}
	
	// Get URL
	public String getURL(){
		return m_sURL;
	}
	
	// Set URL
	public void setURL(String sURL){
		m_sURL = sURL;
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

		m_sURL = sURL;
		
		LinkedHashMap<String,String> msgParams = new LinkedHashMap<String,String>();
		msgParams.put("username", sId);
		msgParams.put("password", sPassword);

		if (!baseRequest("login", msgParams))
			return false;

		if (this.getErrorMessageStr().length() > 0)
			return false;
		
		m_sSessionId = this.getResponseStr();

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
		
		m_sSessionId = "";
		return true;
	}
	
	/**
	 * Check if web service is logined
	 * @return True for logined
	 */
	public boolean isLogined() {
		if (m_sSessionId.length() > 0)
			return true;
		return false;
	}
	
	/**
	 * Request web service for specific operation with parameters
	 * @param params Parameters for web service call
	 * @return True for call success
	 */
	public boolean call(String sInterface, String sModule, String sFcn, String sParams) {
//System.out.println("Start Time: " + new DateTime().toString());
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
		
		return baseRequest("call", msgParams);
	}
	
	/**
	 * Base request web service
	 * @param operation Either "login", "call" or "logout"
	 * @param msgParams Parameters for web service
	 * @return True for request success
	 */
	private boolean baseRequest(String operation, LinkedHashMap<String, String> msgParams) {
		SoapObject request = new SoapObject(NAMESPACE, operation);
//System.out.println("Start Time: " + new DateTime().toString());		
		if (msgParams != null) {
//System.out.println("--------- Params ----------------------------------------");
			for (String key : msgParams.keySet()) {
				String value = msgParams.get(key);
				request.addProperty(key, value);
//System.out.println(key + ": " + value);
			}
//System.out.println("---------------------------------------------------------");
		}
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(m_sURL, TIMEOUT);

		List<HeaderProperty> httpHeaders = new ArrayList<HeaderProperty>();
		if (m_sSessionId.length() > 0)
			httpHeaders.add(new HeaderProperty("Cookie", "CAKEPHP" + "=" + m_sSessionId));
		try {
			androidHttpTransport.call(SOAP_ACTION, envelope, httpHeaders);
			String sResponseStr = (String)envelope.getResponse().toString();
//System.out.println("server response: " + sResponseStr);

			if(sResponseStr.length() > 0){			
				//Process the response
				try {
					JSONArray responseJSONArray = new JSONArray(sResponseStr);
				
					//	Extract error (if any)
					if (!responseJSONArray.isNull(0) && responseJSONArray.getString(0).length() > 0) {
						// Error occur
						m_sErrorMessage = responseJSONArray.getString(0);
						return false;
					}
					
					//	Extract result (Plain-text and JSONArray if parse success)
					if (!responseJSONArray.isNull(1) && responseJSONArray.get(1).toString().length() > 0) {
						m_sResponseStr = responseJSONArray.get(1).toString();
					}
				}
				catch (JSONException jsone) {
					m_sErrorMessage = "Incorrect response - " + jsone.getMessage();
					jsone.printStackTrace();
				}
			}

//System.out.println("End Time: " + new DateTime().toString());
			return true;
		}
		catch (Exception e) {
			m_sErrorMessage = e.getMessage();
			return false;
		}
	}

}
