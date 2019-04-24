package app;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import commonui.FormInputBox;
import core.Controller;
import externallib.StringLib;
import om.PosStation;
import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FormLoginListener {
	void FormLogin_clickTimeInOut();
	void FormLogin_forceLogout();
}


public class FormLogin extends VirtualUIForm implements FrameLoginListener {

	TemplateBuilder m_oTemplateBuilder;
	
	private FuncUser m_oFuncUser;
	private FuncSmartCard m_oFuncSmartCard;
	
	private FrameLogin m_oFrameLogin;
	
	private String m_sLoginId;
	private String m_sLoginPassword;
	private String m_sCardNo;
	
	private Boolean m_bMustLogin;
	private Boolean m_bIsExit;
	private Boolean m_bHoldId;
	private Boolean m_bForceLogut;
	private Boolean m_bChangePasswordUserCancel;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FormLoginListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FormLoginListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FormLoginListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FormLogin(Controller oParentController){
		super(oParentController);
		
		m_sLoginId = "";
		m_sLoginPassword = "";
		m_bMustLogin = false;
		m_bIsExit = false;
		m_bHoldId = false;
		m_bForceLogut = false;
		listeners = new ArrayList<FormLoginListener>();
	}
	
	public boolean init(boolean bMustLogin, FuncSmartCard oFuncSmartCard){	
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmLogin.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Login Frame
		m_oFrameLogin = new FrameLogin();
		m_oTemplateBuilder.buildFrame(m_oFrameLogin, "fraLogin");
		m_oFrameLogin.init(false);
		// Add listener;
		m_oFrameLogin.addListener(this);
		this.attachChild(m_oFrameLogin);
		
		// Shiji Logo
		VirtualUIImage oImageShijiLogo = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(oImageShijiLogo, "imgShijiLogo");
		oImageShijiLogo.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/shiji_logo.png");
		oCoverFrame.attachChild(oImageShijiLogo);
		
		// Separator
		VirtualUIFrame oFrameSeparator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameSeparator, "fraLogoSeparator");
		oCoverFrame.attachChild(oFrameSeparator);
		
		// Infrasys Logo
		VirtualUIImage oImageInfrasysLogo = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(oImageInfrasysLogo, "imgInfrasysLogo");
		oImageInfrasysLogo.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/company_logo.png");
		oCoverFrame.attachChild(oImageInfrasysLogo);
		
		m_oFuncUser = new FuncUser();
		m_oFuncSmartCard = oFuncSmartCard;
		
		m_bMustLogin = bMustLogin;
		
		return true;
	}

	public void setOutletLogo(String sImageURL){
		m_oFrameLogin.setOutletLogo(sImageURL);
	}
	
	public void setOutletDesc(String sOutletDesc){
		m_oFrameLogin.setOutletDesc(sOutletDesc);
		m_oFrameLogin.setFrameOutletDesc(sOutletDesc);
	}
	
	public void setHoldId(String sAutoSignOut) {
		if(sAutoSignOut.isEmpty()) {
			m_bHoldId = true;
			m_oFrameLogin.setSelected(1);
		} else if(sAutoSignOut.equals(PosStation.AUTO_SIGN_OUT_SWITCH))
			m_oFrameLogin.setSelected(2);
		else if(sAutoSignOut.equals(PosStation.AUTO_SIGN_OUT_HOLD_USER_ID_NOT_SWITCH))
			m_bHoldId = true;
	}
	
	public String getLoginId() {
		return m_sLoginId;
	}

	public String getLoginPassword() {
		return m_sLoginPassword;
	}
	
	public String getCardNo() {
		return m_sCardNo;
	}
	
	public FuncUser getFuncUser() {
		return m_oFuncUser;
	}

	public boolean isExit() {
		return m_bIsExit;
	}
	
	public boolean holdId() {
		return m_bHoldId;
	}
	
	public boolean isForceLogout(){
		return m_bForceLogut;
	}
	
	// Login by smart card
	private boolean loginBySmartCard(){
		
		if(m_oFuncSmartCard.useSmartCardAsEmployeeCard() == false || m_oFuncSmartCard.isConnected() == false)
			// Not support
			return false;
		
		while(true){
			if(m_oFuncSmartCard.isSupportSmartCard() == false){
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("not_support_smart_card"));
				oFormDialogBox.show();
				oFormDialogBox = null;
				
				return false;
			}
			
			if(m_oFuncSmartCard.isConnected() == false){
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_open_the_smart_card_device"));
				oFormDialogBox.show();
				oFormDialogBox = null;
				
				return false;
			}
			
			// Read card and show card information
			FormSmartCardOperation oFormSmartCardOperation = new FormSmartCardOperation(m_oFuncSmartCard, this);
			oFormSmartCardOperation.initForReadCardSerial();
			oFormSmartCardOperation.show();
			
			if(oFormSmartCardOperation.isCancelByUser())
				return false;
			
			if(oFormSmartCardOperation.isProcessSuccess()){
				String sSmartCardSerial = m_oFuncSmartCard.getSerialNo();
				if (m_oFuncUser.switchUserByEmployeeCard(sSmartCardSerial)) {
					// User is valid
					return true;
				}else{
					// Show error in the login frame
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_login"));
					oFormDialogBox.show();
					oFormDialogBox = null;
				}
			}
		}
	}
	
	private boolean changePassword() {
		List<String> oMsgList = new ArrayList<String>();
		String sOldPwd = "", sNewPwd = "", sRetypePwd = "";
		String sEncryptedOldPwd = "";
		String sEncryptedNewPwd = "";
		String sEncryptedRetypePwd = "";
		byte[] encrypted = null;
		boolean bBreakLoop = false;
		m_bChangePasswordUserCancel = false;
		
		oMsgList.add(AppGlobal.g_oLang.get()._("old_password"));
		oMsgList.add(AppGlobal.g_oLang.get()._("new_password"));
		oMsgList.add(AppGlobal.g_oLang.get()._("retype_new_password"));
		
		FormInputBox oFormInputBox = null;
		oFormInputBox = new FormInputBox(this);
		oFormInputBox.initWithInputNum(3);
		oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("change_password"));
		oFormInputBox.setMessages(oMsgList);
		oFormInputBox.setKeyboardType(0, HeroActionProtocol.View.Attribute.KeyboardType.NUMBER);
		oFormInputBox.setInputBoxSecurity(0, true);
		oFormInputBox.setDefaultInputValue(0, sOldPwd);
		oFormInputBox.setKeyboardType(1, HeroActionProtocol.View.Attribute.KeyboardType.NUMBER);
		oFormInputBox.setInputBoxSecurity(1, true);
		oFormInputBox.setDefaultInputValue(1, sNewPwd);
		oFormInputBox.setKeyboardType(2, HeroActionProtocol.View.Attribute.KeyboardType.NUMBER);
		oFormInputBox.setInputBoxSecurity(2, true);
		oFormInputBox.setDefaultInputValue(2, sRetypePwd);
		oFormInputBox.show();
		
		if(oFormInputBox.isUserCancel()) {
			m_bChangePasswordUserCancel = true;
			return false;
		}
		
		// Get input value
		sOldPwd = oFormInputBox.getInputValue(0);
		sNewPwd = oFormInputBox.getInputValue(1);
		sRetypePwd = oFormInputBox.getInputValue(2);
		
		// Password encryption with timestamp
		String sKey = "a89bc02cef9257ed";
		String sIV = "bcd1345abcab6612";
		DateTime oCurrentTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyyMMddHHmmss");
		String sCurrntTimeStamp = oFormatter.print(AppGlobal.convertTimeToUTC(oCurrentTime));
		
		try {
			IvParameterSpec oIV = new IvParameterSpec(sIV.getBytes("UTF-8"));
			SecretKeySpec oKeySpec = new SecretKeySpec(sKey.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, oKeySpec, oIV);

			encrypted = cipher.doFinal((sOldPwd + sCurrntTimeStamp.toString()).getBytes());
			sEncryptedOldPwd = DatatypeConverter.printBase64Binary(encrypted); 
			encrypted = cipher.doFinal((sNewPwd + sCurrntTimeStamp.toString()).getBytes());
			sEncryptedNewPwd = DatatypeConverter.printBase64Binary(encrypted); 
			encrypted = cipher.doFinal((sRetypePwd + sCurrntTimeStamp.toString()).getBytes());
			sEncryptedRetypePwd = DatatypeConverter.printBase64Binary(encrypted);           
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		String sErrorMsg = "";
		FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
		oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("message"));
		if(AppGlobal.g_oFuncUser.get().changePassword(sEncryptedOldPwd, sEncryptedNewPwd, sEncryptedRetypePwd)) {
			sErrorMsg = AppGlobal.g_oLang.get()._("change_password_succeed") + System.lineSeparator() + AppGlobal.g_oLang.get()._("please_login_again");
			oFormDialogBox.setMessage(sErrorMsg);
			oFormDialogBox.show();
			oFormDialogBox = null;
			return true;
		}
		else{
			if(AppGlobal.g_oFuncUser.get().getLastErrorMessage().equals(FuncUser.RESULT_INCORRECT_PASSWORD))
				sErrorMsg = AppGlobal.g_oLang.get()._("incorrect_current_password");
			else if(AppGlobal.g_oFuncUser.get().getLastErrorMessage().contains(FuncUser.RESULT_FAIL_TO_LOGIN_MASTER_SERVER))
				sErrorMsg = AppGlobal.g_oLang.get()._("fail_to_login_master_server");
			else if(AppGlobal.g_oFuncUser.get().getLastErrorMessage().contains(FuncUser.RESULT_FAIL_TO_CHANGE_PASSWORD_IN_MASTER_SERVER))
				sErrorMsg = AppGlobal.g_oLang.get()._("fail_to_change_password_in_master_server");
			else if(AppGlobal.g_oFuncUser.get().getLastErrorMessage().equals(FuncUser.RESULT_PASSWORD_NOT_MATCH))
				sErrorMsg = AppGlobal.g_oLang.get()._("new_and_retype_password_not_matched");
			else if(AppGlobal.g_oFuncUser.get().getLastErrorMessage().equals(FuncUser.RESULT_PASSWORD_LESSTHAN_MIN_LENGTH))
				sErrorMsg = AppGlobal.g_oLang.get()._("password_less_than_minimum_length");
			else if(AppGlobal.g_oFuncUser.get().getLastErrorMessage().equals(FuncUser.RESULT_PASSWORD_AT_LEAST_ONE_NON_ALPHABET))
				sErrorMsg = AppGlobal.g_oLang.get()._("password_at_least_contain_one_non_alphabet");
			else if(AppGlobal.g_oFuncUser.get().getLastErrorMessage().equals(FuncUser.RESULT_PASSWORD_AT_LEAST_ONE_ALPHABET))
				sErrorMsg = AppGlobal.g_oLang.get()._("password_at_least_contain_one_alphabet");
			else if(AppGlobal.g_oFuncUser.get().getLastErrorMessage().equals(FuncUser.RESULT_PASSWORD_AT_LEAST_ONE_UPPERCASE_ALPHABET))
				sErrorMsg = AppGlobal.g_oLang.get()._("password_at_least_contain_one_uppercase_alphabet");
			else if(AppGlobal.g_oFuncUser.get().getLastErrorMessage().equals(FuncUser.RESULT_PASSWORD_AT_LEAST_ONE_LOWERCASE_ALPHABET))
				sErrorMsg = AppGlobal.g_oLang.get()._("password_at_least_contain_one_lowercase_alphabet");
			else if(AppGlobal.g_oFuncUser.get().getLastErrorMessage().equals(FuncUser.RESULT_PASSWORD_AT_LEAST_ONE_SPECIAL_CHARACTER))
				sErrorMsg = AppGlobal.g_oLang.get()._("password_at_least_contain_one_special_character");
			else if(AppGlobal.g_oFuncUser.get().getLastErrorMessage().equals(FuncUser.RESULT_PASSWORD_AT_LEAST_ONE_DIGIT))
				sErrorMsg = AppGlobal.g_oLang.get()._("password_at_least_contain_one_digit");
			else if(AppGlobal.g_oFuncUser.get().getLastErrorMessage().equals(FuncUser.RESULT_PASSWORD_SAME_AS_LOGIN))
				sErrorMsg = AppGlobal.g_oLang.get()._("password_cannot_same_as_login");
			else if(AppGlobal.g_oFuncUser.get().getLastErrorMessage().equals(FuncUser.RESULT_PASSWORD_CANNOT_REPEAT_WITH_PREIVOUS))
				sErrorMsg = AppGlobal.g_oLang.get()._("password_cannot_same_with_previous_password");
			else
				sErrorMsg = AppGlobal.g_oLang.get()._("change_password_failed");
			oFormDialogBox.setMessage(sErrorMsg);
			oFormDialogBox.show();
			oFormDialogBox = null;
			return false;
		}
	}
	
	@Override
	public void FrameLogin_clickOK() {
		if(m_oFrameLogin.getLoginId().length() == 0){
			// Show error in the login frame
			m_oFrameLogin.setLoginId("");
			m_oFrameLogin.setLoginPassword("");
			m_oFrameLogin.setEnterDesc(AppGlobal.g_oLang.get()._("next", ""));
			m_oFrameLogin.setEnterBlockUI(false);
			m_oFrameLogin.setMessage(AppGlobal.g_oLang.get()._("fail_to_login"));
			
			return;
		}
		
		// Verify the ID and password through OM
		String sErrorMessage = m_oFuncUser.login(m_oFrameLogin.getLoginId(), m_oFrameLogin.getLoginPassword(), false); 
		if(sErrorMessage.length() > 0){
			if(m_oFuncUser.getErrorExtraInfos() != null && !m_oFuncUser.getErrorExtraInfos().isEmpty()){
				if(m_oFuncUser.getErrorExtraInfos().containsKey("lockTime")){
					String sLockTime = m_oFuncUser.getErrorExtraInfos().get("lockTime");
					if(sLockTime != null && !sLockTime.isEmpty()){
						int iMin = 0, iSecond = 0;
						
						//calculate the lock time
						iMin = Integer.parseInt(sLockTime) / 60;
						iSecond = Integer.parseInt(sLockTime) % 60;
						sLockTime = iMin + ":" + StringLib.IntToStringWithLeadingZero(iSecond, 2);
						
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("account_has_been_locked_for") + " "
								+ sLockTime + " " + AppGlobal.g_oLang.get()._("minutes") + System.lineSeparator()
								+ AppGlobal.g_oLang.get()._("please_try_later"));
						oFormDialogBox.show();
						oFormDialogBox = null;
					}	
				}
			}
			m_oFrameLogin.setLoginId("");
			m_oFrameLogin.setLoginPassword("");
			m_oFrameLogin.setEnterDesc(AppGlobal.g_oLang.get()._("next", ""));
			m_oFrameLogin.setEnterBlockUI(false);
			m_oFrameLogin.setMessage(AppGlobal.getLoginErrorMessage(sErrorMessage));
			return;
		}
		
		if(m_oFuncUser.needToChangePassword()){
			if(m_oFuncUser.isForceChangePassword()){
				boolean bChangePasswordResult = false;
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("message"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("you_have_not_changed_your_password_for_a_long_time") + System.lineSeparator()
						+AppGlobal.g_oLang.get()._("please_change_password"));
				oFormDialogBox.show();
				oFormDialogBox = null;
				do {
					bChangePasswordResult = changePassword();
					if(this.m_bChangePasswordUserCancel)
						break;
				}while(!bChangePasswordResult);
				
				if(this.m_bChangePasswordUserCancel) {
					m_oFrameLogin.FrameNumberPad_clickCancel();
					return;
				}else
					m_bForceLogut = true;
			}else{
				FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("change_password"), AppGlobal.g_oLang.get()._("ignore"), this);
				oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
				oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("you_have_not_changed_your_password_for_a_long_time") + System.lineSeparator()
						+AppGlobal.g_oLang.get()._("please_change_password"));
				oFormConfirmBox.show();
				if(oFormConfirmBox.isOKClicked()){
					if(changePassword())
						m_bForceLogut = true;
				}	
			}
		}
		
		if (sErrorMessage.length() == 0) {
			// User is valid
			m_sLoginId = m_oFuncUser.getLoginId();
			m_sLoginPassword = m_oFuncUser.getPassword();
			
			// Finish showing this form
			this.finishShow();
		}else{
			// Show error in the login frame
			m_oFrameLogin.setLoginId("");
			m_oFrameLogin.setLoginPassword("");
			m_oFrameLogin.setEnterDesc(AppGlobal.g_oLang.get()._("next", ""));
			m_oFrameLogin.setEnterBlockUI(false);
			m_oFrameLogin.setMessage(AppGlobal.getLoginErrorMessage(sErrorMessage));
		}
	}
	
	@Override
	public void FrameLogin_clickTimeInOut(){
		for(FormLoginListener listener : listeners) {
			// Raise the event to parent
			listener.FormLogin_clickTimeInOut();
		}
	}

	@Override
	public void FrameLogin_clickExit() {
		
		m_bIsExit = true;
		
		m_sLoginId = "";
		m_sLoginPassword = "";
		
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public void FrameLogin_clickSelectBox() {
		if(m_bHoldId) {
			m_oFrameLogin.setSelected(2);
			m_bHoldId = false;
		} else {
			m_oFrameLogin.setSelected(1);
			m_bHoldId = true;
		}
		
	}

	@Override
	public void FrameLogin_swipeCard(String sSwipeCardValue) {
		// Remove \r and \n in the return card no.
		sSwipeCardValue = sSwipeCardValue.replace("\r", "").replace("\n", "");

		//Verify the ID and password through OM
		if (sSwipeCardValue.length() > 0 && m_oFuncUser.switchUserByEmployeeCard(sSwipeCardValue)) {
			// User is valid
			m_sLoginId = m_oFuncUser.getLoginId();
			m_sLoginPassword = m_oFuncUser.getPassword();
			
			// Finish showing this form
			this.finishShow();
		}else{
			// Show error in the login frame
			m_oFrameLogin.setLoginId("");
			m_oFrameLogin.setLoginPassword("");
			m_oFrameLogin.setEnterDesc(AppGlobal.g_oLang.get()._("next", ""));
			m_oFrameLogin.setEnterBlockUI(false);
			m_oFrameLogin.setMessage(AppGlobal.g_oLang.get()._("fail_to_login"));
		}
	}
	
	@Override
	public void FrameLogin_clickShowSmartCard() {
		if(loginBySmartCard()){
			// Login success
			// User is valid
			m_sLoginId = m_oFuncUser.getLoginId();
			m_sLoginPassword = m_oFuncUser.getPassword();
			
			// Finish showing this form
			this.finishShow();
		}
	}
	
	@Override
	// If return false, not handle the click event
	public boolean childItemClicked() {
		// Check if myself is killed or not
		String sReason = AppGlobal.getKilledReason();
		if (sReason.length() > 0) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(sReason);
			oFormDialogBox.show();
			this.finishShow();
			for(FormLoginListener listener : listeners) {
				// Raise the event to parent
				listener.FormLogin_forceLogout();
			}
			AppGlobal.finishBeingKilled();
			return false;
		}
		return true;
	}
}
