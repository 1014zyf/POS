package externallib;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.json.JSONException;
import org.json.JSONObject;

public class HeroHttpStreamClient implements HttpClientInterface {
//	private final int TIMEOUT = 120000;	// *** Timeout : 120s
	private final int DEFAULT_ABSOLUTE_ALIVE_TIME_LIMIT = 3 * 60 * 60 * 1000;	//	3 hour max alive time
	private final int DEFAULT_TIMEOUT_AFTER_LAST_REQUEST = 15 * 60 * 1000;		//	15 min alive time

	String _url = "http://127.0.0.1/hero/";
	
	String _id = "";
	String _password = "";
	
	int _loginedPort = 0;
	int _absoluteAliveTimeLimit = DEFAULT_ABSOLUTE_ALIVE_TIME_LIMIT;
	int _timeoutAfterLastRequest = DEFAULT_TIMEOUT_AFTER_LAST_REQUEST;
	
	boolean _isError = false;
	String _responseStr = "";
	String _errorMessage = "";
	String _warningMessage = "";
	JSONObject _licenseCertJSONObj = new JSONObject();
	long _lastRequestTime = 0;
	long _loginTime = 0;
	
	// Last error extra informations
	private HashMap<String, String> m_oErrorExtraInfos = new HashMap<String,String>();

	JsonFactory _jsonFactory = new JsonFactory();
	JsonParser _jsonParser;
	
	public HeroHttpStreamClient(String url) {
		_url = url;
	}
	
	public boolean login(String id, String password) {
		URL url = null;
		try {
			url = new URL(_url + "chi/http_stream_interface/login");
		} 
		catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
		
		LinkedHashMap<String,String> msgParams = new LinkedHashMap<String,String>();
		msgParams.put("username", id);
		msgParams.put("password", password);
		//	Allow login by service role
		JSONObject paramsJSONO = new JSONObject();
		try {
			paramsJSONO.put("allow_service_role", 1);
		} 
		catch (JSONException jsone) {}
		msgParams.put("params", paramsJSONO.toString());
		String data = generateSubmitData(msgParams);
		
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		connection.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes().length));
		connection.setRequestProperty("Content-Language", "en-US");  
		connection.setRequestProperty("Transfer-Encoding", "chunked");
		connection.setUseCaches (false);
		connection.setDoInput(true);
		connection.setDoOutput(true);

		DataOutputStream wr = null;
		try {
			wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(data);
			wr.flush();
			wr.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		InputStream replyStream = null;
		try {
			replyStream = connection.getInputStream();
			if (connection.getContentEncoding() != null) {
				if (connection.getContentEncoding().equalsIgnoreCase("gzip"))
					replyStream = new GZIPInputStream(replyStream);
				else if (connection.getContentEncoding().equalsIgnoreCase("deflate")) 
					replyStream = new InflaterInputStream(replyStream);
			}
			
			_jsonParser = _jsonFactory.createJsonParser(replyStream);
			while (_jsonParser.nextToken() != JsonToken.END_OBJECT) {
				_jsonParser.nextValue();
				
				String currentName = _jsonParser.getCurrentName();
				if (currentName.contentEquals("port")) {
					_loginedPort = _jsonParser.getIntValue();
				}
				else if (currentName.contentEquals("absolute_alive_time_limit")) {
					_absoluteAliveTimeLimit = _jsonParser.getIntValue() * 1000;
				}
				else if (currentName.contentEquals("timeout_after_last_request")) {
					_timeoutAfterLastRequest = _jsonParser.getIntValue() * 1000;
				}
				else {
					_jsonParser.skipChildren();
				}
			}
			
			_loginTime = System.currentTimeMillis();
			_lastRequestTime = System.currentTimeMillis();
		} 
		catch (IllegalStateException e) {
			e.printStackTrace();
			return false;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		_id = id;
		_password = password;
		
		return true;
	}
	
	public boolean logout() {
		request("logout", "", "", "", "");
		_loginedPort = 0;
		try {
			_jsonParser.close();
		} 
		catch (IOException e) {}
		
		return true;	//	TODO logout ###########################
	}
	
	public boolean isLogined() {
		if (_loginedPort == 0)
			return false;
		
		if (System.currentTimeMillis() - _lastRequestTime > _timeoutAfterLastRequest)
			return false;
		
		if (System.currentTimeMillis() - _loginTime > _absoluteAliveTimeLimit)
			return false;
		
		return true;
	}
	
	public boolean call(String sInterface, String sModule, String sFcn, String sParams) {
		return request("call", sInterface, sModule, sFcn, sParams);
	}
	
	private boolean request(String operation, String sInterface, String sModule, String sFcn, String sParams) {
		URL url = null;
		try {
			url = new URL(_url + "fast_api.php");
		} 
		catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
		
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
		} 
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		JSONObject dataJSONObj = new JSONObject();
		try {
			dataJSONObj.put("operation", operation);
			dataJSONObj.put("interface", sInterface);
			dataJSONObj.put("module", sModule);
			dataJSONObj.put("fcn", sFcn);
			dataJSONObj.put("params", sParams);
		} 
		catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		
		LinkedHashMap<String,String> msgParams = new LinkedHashMap<String,String>();
		msgParams.put("port", "" + _loginedPort);
		msgParams.put("data", dataJSONObj.toString());
		String data = generateSubmitData(msgParams);
		
		connection.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes().length));
		connection.setRequestProperty("Content-Language", "en-US");  
		connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
		connection.setConnectTimeout(DEFAULT_ABSOLUTE_ALIVE_TIME_LIMIT);
		connection.setReadTimeout(DEFAULT_ABSOLUTE_ALIVE_TIME_LIMIT);
		connection.setUseCaches (false);
		connection.setDoInput(true);
		connection.setDoOutput(true);

		DataOutputStream wr = null;
		try {
			wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(data);
			wr.flush();
		} 
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		  
		InputStream replyStream = null;
		try {
			replyStream = connection.getInputStream();
			if (connection.getContentEncoding() != null) {
				if (connection.getContentEncoding().equalsIgnoreCase("gzip"))
					replyStream = new GZIPInputStream(replyStream);
				else if (connection.getContentEncoding().equalsIgnoreCase("deflate")) 
					replyStream = new InflaterInputStream(replyStream);
			}
			
			String responseStr = convertStreamToString(replyStream).trim();
//			System.out.println("" + new Date() + ": " + _loginedPort + ": " + responseStr);
			_lastRequestTime = System.currentTimeMillis();

			if(responseStr.replaceAll("\\r|\\n", "").equals("[]")) {
				_responseStr = responseStr.replaceAll("\\r|\\n", "");
			}
			else {
				try{
					JSONObject oResponseJSONObject = new JSONObject(responseStr);
					if (oResponseJSONObject.has("error")) {
						_errorMessage = oResponseJSONObject.getString("error");
						if(oResponseJSONObject.has("lockTime"))
		            		m_oErrorExtraInfos.put("lockTime", oResponseJSONObject.optString("lockTime"));
						_isError = true;
					}
					
					// Set warning message if any
					if (oResponseJSONObject.has("warning"))
						_warningMessage = oResponseJSONObject.getString("warning");
					
					// Set cert if any
					if (oResponseJSONObject.has("cert")) {
						JSONObject licenseCertJSONObj = oResponseJSONObject.optJSONObject("cert");
						if (licenseCertJSONObj != null)
							_licenseCertJSONObj = licenseCertJSONObj;
					}
					
					if (!_isError)
						_responseStr = responseStr;
				}
				catch (JSONException jsone) {
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
					
					_isError = true;
				}
			}
		}
		catch (IllegalStateException e) {
			e.printStackTrace();
			return false;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return !_isError;
	}
	
	private static String generateSubmitData(LinkedHashMap<String,String> msgParams) {
		String paramsContent = "";
		
		if (msgParams != null) {
			int i = 0;
			for (String key : msgParams.keySet()) {
				if (i > 0)
					paramsContent += "&";
				try {
					paramsContent += key + "=" + URLEncoder.encode(msgParams.get(key), "UTF-8");
				} 
				catch (UnsupportedEncodingException e) {}
				i++;
			}
		}
		return paramsContent;
	}
	
	public String getResponseStr() {
		return _responseStr;
	}
	
	/**
	 * Get web service raw return.
	 * @return Raw error message in String.
	 */
	public String getErrorMessageStr() {
		return _errorMessage;
	}
	
	public HashMap<String, String> getErrorExtraInfoStr(){
		return m_oErrorExtraInfos;
	}

	public String getWarningMessage(){
		return _warningMessage;
	}
	
	public JSONObject getLicenseCert(){
		return _licenseCertJSONObj;
	}
	
	// Return the internal error message
	public String getInternalErrorMessage(){
		return "";
	}
	
	
	public String getCakeSessionID() {
		//	Use normal http client to generate session for cake
		HeroHttpClient heroHttpClient = new HeroHttpClient(_url);
		if (heroHttpClient.login(_id, _password))
			return heroHttpClient.getCakeSessionID();
		return "";
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
}
