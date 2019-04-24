package app;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;

import externallib.HeroSecurity;
import externallib.IniReader;
import externallib.StringLib;
import om.*;

public class FuncUser implements Cloneable{
	public static String RESULT_NO_SUCH_USER = "no_such_user";
	public static String RESULT_INCORRECT_PASSWORD = "incorrect_password";
	public static String RESULT_FAIL_TO_LOGIN_MASTER_SERVER = "fail_to_login_master_server";
	public static String RESULT_FAIL_TO_CHANGE_PASSWORD_IN_MASTER_SERVER = "fail_to_change_password_in_master_server";
	public static String RESULT_FAIL_TO_CHANGE_CARD_NUMBER_IN_MASTER_SERVER = "fail_to_change_card_number_in_master_server";
	public static String RESULT_FAIL_TO_CHANGE_CARD_EXCEED_MAX_LENGTH = "card_number_exceed_maximum_length";
	public static String RESULT_PASSWORD_NOT_MATCH = "password_not_match";
	public static String RESULT_PASSWORD_LESSTHAN_MIN_LENGTH = "password_less_than_the_min_length";
	public static String RESULT_PASSWORD_AT_LEAST_ONE_NON_ALPHABET = "password_at_least_one_non_alphabet";
	public static String RESULT_PASSWORD_AT_LEAST_ONE_ALPHABET = "password_at_least_one_alphabet";
	public static String RESULT_PASSWORD_AT_LEAST_ONE_UPPERCASE_ALPHABET = "password_at_least_one_upper_case_alphabet";
	public static String RESULT_PASSWORD_AT_LEAST_ONE_LOWERCASE_ALPHABET = "password_at_least_one_lower_case_alphabet";
	public static String RESULT_PASSWORD_AT_LEAST_ONE_SPECIAL_CHARACTER = "password_at_least_one_special_character";
	public static String RESULT_PASSWORD_AT_LEAST_ONE_DIGIT = "password_at_least_one_digit";
	public static String RESULT_PASSWORD_SAME_AS_LOGIN = "password_same_as_login";
	public static String RESULT_PASSWORD_CANNOT_REPEAT_WITH_PREIVOUS = "password_not_repeat_with_previous_password";
	
	private UserUser m_oUser;
	private String m_sServiceAccountLoginId = "";
	private String m_sServiceAccountPassword = "";
	private String m_sLastErrorMessage;
	
	private boolean m_bLoginStandalone = false;
	private boolean m_bLoginHq = false;

	public FuncUser() {
		// Load the internal ID and password from config.ini
		// Read setup from the setup file
		IniReader iniReader = null;
		try {
			iniReader = new IniReader("cfg" + java.io.File.separator + "config.ini");
			String sTmp = iniReader.getValue("setup", "login");
			if (sTmp != null) {
				m_sServiceAccountLoginId = sTmp;
			}
			
			boolean bPasswordEncrypted = true;
			sTmp = iniReader.getValue("setup", "encrypted");
			if (sTmp != null) {
				if (sTmp.equals("0")) {
					bPasswordEncrypted = false;
				}
			}
			
			sTmp = iniReader.getValue("setup", "password");
			if (sTmp != null) {
				if (bPasswordEncrypted) {
					try {
						m_sServiceAccountPassword = HeroSecurity.decryptString(sTmp);
					} catch (DataLengthException e) {
						AppGlobal.stack2Log(e);
					} catch (IllegalStateException e) {
						AppGlobal.stack2Log(e);
					} catch (InvalidCipherTextException e) {
						AppGlobal.stack2Log(e);
					}
				} else {
					m_sServiceAccountPassword = sTmp;
				}
			}
		} catch (IOException e) {
			// Fail to read config.ini
			AppGlobal.stack2Log(e);
		}
	}
	
	//login
	public String login(String loginId, String password, boolean bAllowServiceRole) {
		// Logout previous user
		this.logout();
		
		m_oUser = new UserUser();
		m_sLastErrorMessage = "";

		String sURL = "";
		try {
			// Read setup from the setup file
			IniReader iniReader = new IniReader("cfg/config.ini");
			
			if (m_bLoginStandalone == true || AppGlobal.g_oFuncSmartStation.isStandaloneRole())
				sURL = iniReader.getValue("connection", "db_wsdl_standalone");
			else
				sURL = iniReader.getValue("connection", "db_wsdl");
			if (sURL != null && !sURL.isEmpty() && sURL.charAt(sURL.length() - 1) != '/')
				sURL += '/';
			sURL = sURL.replace("chi/http_interface", "");
			sURL = sURL.replace("eng/http_interface", "");
			sURL = sURL.replace("cn/http_interface", "");
			sURL = sURL.replace("jpn/http_interface", "");
			sURL = sURL.replace("kor/http_interface", "");
			sURL = sURL.replace("http_interface", "");
		} 
		catch (IOException e) {
			String sErrMsg = "Missing setup file (cfg/config.ini)";
			OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), loginId, sErrMsg);
			return sErrMsg;
		}
		
		String sErrorMessage = m_oUser.readByLoginPassword(sURL, loginId, password, AppGlobal.g_oPosLicenseControlModel, bAllowServiceRole);
		if(sErrorMessage.length() > 0){
			// Error
			AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", "", sErrorMessage);
		}
		
		return sErrorMessage;
	}

	// login HQ
	public String loginHQ(String loginId, String password, boolean bAllowServiceRole) {
		// Logout previous user
		this.logoutHQ();
		
		UserUser oUser = new UserUser();
		m_sLastErrorMessage = "";
		
		oUser.setOmWsClient(OmWsClientGlobal.g_oWsClientForHq.get());
		String sErrorMessage = oUser.readByLoginPassword(AppGlobal.g_sMasterServerUrl, loginId, password, AppGlobal.g_oPosLicenseControlModelForHq, bAllowServiceRole);
		if(!sErrorMessage.isEmpty())	// Error
			AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", "", sErrorMessage);
		else
			m_bLoginHq = true;
		return sErrorMessage;
	}
	
	// Login with card
	// *** for this function, the user in the session of the previous login is re-new to card owner 
	public boolean switchUserByEmployeeCard(String sCard) {
		boolean bResult = false;
		
		synchronized (OmWsClientGlobal.g_oWsClient.get()) {
			String sErrorMessage = login(m_sServiceAccountLoginId, m_sServiceAccountPassword, true);
			if (!sErrorMessage.isEmpty()) {
				return false;
			}
			
			bResult = m_oUser.loginByUserCardNumber(sCard);
		}
		
		if (m_bLoginHq) {
			UserUser oHqUser = new UserUser();
			oHqUser.setOmWsClient(OmWsClientGlobal.g_oWsClientForHq.get());
			oHqUser.loginByUserCardNumber(sCard);
		}
		
		return bResult;
	}
	
	// Login with card
	// *** for this function, the user in the session of the previous login is re-new to card owner 
	public boolean switchUserByAccessToken(String sLogin, String sAccessToken) {
		m_oUser = new UserUser();
		
		// Logout other session
		this.logoutOtherSession();
		
		return m_oUser.loginByLoginAccessToken(sLogin, sAccessToken);
	}
	
	//logout
	public void logout(){
		OmWsClientGlobal.g_oWsClient.get().logout();
		if (m_bLoginHq)
			logoutHQ();
	}
	
	private void logoutHQ() {
		OmWsClientGlobal.g_oWsClientForHq.get().logout();
	}
	
	//logout all session except the main thread one
	private void logoutOtherSession() {
		OmWsClientGlobal.g_oWsClient.get().logoutOtherSession();
		if (m_bLoginHq)
			OmWsClientGlobal.g_oWsClientForHq.get().logoutOtherSession();
	}
	
	public void setLoginHq(boolean bLoginHq) {
		m_bLoginHq = bLoginHq;
	}
	
	//clone object
    public Object clone(){
    	Object obj = null;
    	
    	try{
        	 obj = super.clone();        	
        } catch (CloneNotSupportedException e){
        	AppGlobal.stack2Log(e);
        }
        
        return obj;
    }
	
	//get user id
	public int getUserId() {
		return m_oUser.getUserId();
	}
	
	//get user number
	public String getUserNumber() {
		return m_oUser.getNumber();
	}
	
	//get user role
	public String getUserRole() {
		return m_oUser.getRole();
	}
	
	//get login id
	public String getLoginId() {
		return m_oUser.getLogin();
	}
	
	//get password
	public String getPassword() {
		return m_oUser.getPassword();
	}
	
	//get user group list
	public List<Integer> getUserGroupList() {
		return m_oUser.getBelongGroupIdList();
	}
	
	public String getUserName(int iIndex) {
		String sName = m_oUser.getFirstName(AppGlobal.g_oCurrentLangIndex.get())+" "+m_oUser.getLastName(AppGlobal.g_oCurrentLangIndex.get());
		return sName;
	}
	
	public String[] getUserName() {
		String[] sNameArray = StringLib.appendStringArray(m_oUser.getFirstName(), " ", m_oUser.getLastName());
		return sNameArray;
	}
	
	public UserUser getUser() {
		return m_oUser;
	}
	
	public String getServiceAccountLoginId() {
		return m_sServiceAccountLoginId;
	}
	
	public String getServiceAccountPassword() {
		return m_sServiceAccountPassword;
	}
	
	public boolean isSystemAdmin() {
		if(m_oUser.getRole().equals(UserUser.ROLE_SYSTEM_ADMIN))
			return true;
		else
			return false;
	}
	
	//Check User Login and Password
	public boolean isValidUser(String sLogin, String sPassword) {
		m_oUser = new UserUser();
	
		if (m_oUser.readByUserLoginPassword(sLogin, sPassword)) 
			return true;
		else
			return false;
		
	}
	
	//Check User Card No
	public boolean isValidUserByCardNum(String sCardNum) {
		m_oUser = new UserUser();
	
		if (m_oUser.readByUserCardNumber(sCardNum)) 
			return true;
		else
			return false;
		
	}
	
	//Change password
	public boolean changePassword(String sOldPassword, String sNewPassword, String sRetypePassword) {
		m_sLastErrorMessage = "";
		if (m_oUser.changePassword(sOldPassword, sNewPassword, sRetypePassword)) 
			return true;
		else {
			m_sLastErrorMessage = m_oUser.getLastErrorMessage();
			return false;
		}
	}
	
	//Change employee card number
	public boolean changeEmployeeCardNumber(String sNewCardNumber){
		m_sLastErrorMessage = "";
		if (m_oUser.changeEmployeeCardNumber(sNewCardNumber)) {
			m_oUser.setCardNumber(sNewCardNumber);
			return true;
		}else {
			m_sLastErrorMessage = m_oUser.getLastErrorMessage();
			return false;
		}
	}
	
	public String getLastErrorMessage() {
		return m_sLastErrorMessage;
	}
	
	public boolean needToChangePassword(){
		return m_oUser.needToChangePassword();
	}
	
	public boolean isForceChangePassword(){
		return m_oUser.isForceChangePassword();
	}
	
	public HashMap<String, String> getErrorExtraInfos(){
		return m_oUser.getErrorExtraInfos();
	}
	
	public boolean isLoginHq() {
		return m_bLoginHq;
	}
	
	public void setLoginStandalone(boolean bLoginStandalone) {
		m_bLoginStandalone = bLoginStandalone;
	}
}
