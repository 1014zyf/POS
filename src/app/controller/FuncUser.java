package app.controller;

import java.util.List;

import app.model.*;

public class FuncUser implements Cloneable{
	private UserUser m_oUser;
	
	
	//login
	public String login(String loginId, String password) {
		
		// Logout previous user
		this.logout();
		
		m_oUser = new UserUser();
	
		String sErrorMessage = m_oUser.readByLoginPassword(loginId, password, AppGlobal.g_oPosLicenseControlModel);
		if(sErrorMessage.length() > 0){
			// Error
			AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", "", sErrorMessage);
		}
		return sErrorMessage;
	}
	
	// Login with card
	// *** for this function, the user in the session of the previous login is re-new to card owner 
	public boolean switchUserByEmployeeCard(String sCard) {
		m_oUser = new UserUser();
		
		// Logout other session
		this.logoutOtherSession();
		
		if (m_oUser.loginByUserCardNumber(sCard)) 
			return true;
		else
			return false;
		
	}
	
	//logout
	public void logout(){
		OmWsClientGlobal.g_oWsClient.get().logout();
	}
	
	//logout all session except the main thread one
	private void logoutOtherSession(){
		OmWsClientGlobal.g_oWsClient.get().logoutOtherSession();
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
	
	public UserUser getUser() {
		return m_oUser;
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
}
