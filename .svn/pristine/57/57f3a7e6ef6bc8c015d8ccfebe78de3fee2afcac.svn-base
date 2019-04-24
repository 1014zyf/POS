package externallib;

import java.util.HashMap;

import org.json.JSONObject;

public interface HttpClientInterface {
	public boolean login(String id, String password);
	public boolean logout();
	public boolean isLogined();
	public boolean call(String sInterface, String sModule, String sFcn, String sParams);
	public String getResponseStr();
	public String getErrorMessageStr();
	public String getWarningMessage();
	public HashMap<String, String> getErrorExtraInfoStr();
	public JSONObject getLicenseCert();
	public String getInternalErrorMessage();
	public String getCakeSessionID();
}
