package app;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import commonui.FormConfirmBox;
import commonui.FormDatePicker;
import commonui.FormDialogBox;
import commonui.FrameTitleHeader;
import externallib.StringLib;
import om.InfVendor;
import om.PosInterfaceConfig;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUICodeReader;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;
import virtualui.VirtualUITextbox;

interface FrameMembershipRegistrationListener {
	void FrameMembershipRegistration_clickOK(HashMap<String, String> oEnquiryInfo);
	void FrameMembershipRegistration_clickCancel();
	void FrameMembershipRegistration_valueChanged(String sValue);
	void FrameMembershipRegistration_clickPrint(HashMap<String, String> m_oCurrentMemberInfo);
	void FrameMembershipRegistration_clickAttachToTable();
	
}
public class FrameMembershipRegistration extends VirtualUIFrame {
	private TemplateBuilder m_oTemplateBuilder;
	
	private boolean m_bFromOrderingPanel;
	private VirtualUIFrame m_oRegistryFrame;
	private VirtualUIFrame m_oResultFrame;
	private VirtualUIButton m_oButtonAttachToTable;
	
	private VirtualUILabel m_oLabelNRIC;
	private VirtualUITextbox m_oTxtboxNRIC;
	private VirtualUILabel m_oLabelFirstName;
	private VirtualUITextbox m_oTxtBoxFirstName;
	private VirtualUILabel m_oLabelLastName;
	private VirtualUITextbox m_oTxtBoxLastName;

	private VirtualUILabel m_oLabelGender;
	private VirtualUIButton m_oButtonMr;
	private VirtualUIButton m_oButtonMs;
	private VirtualUILabel m_oLabelEmail;
	private VirtualUITextbox m_oTxtBoxEmail;
	private VirtualUILabel m_oLabelLang;
	private VirtualUITextbox m_oTxtBoxLang;
	private VirtualUILabel m_oLabelMemberNumber;
	private VirtualUITextbox m_oTxtBoxMemberNumber;
	private VirtualUILabel m_oLabelMobileNo;
	private VirtualUITextbox m_oTxtBoxMobileNo;
	private VirtualUITextbox m_oTxtBoxCountryCode;
	
	private VirtualUILabel m_oLabelBirthday;
	private VirtualUITextbox m_oTxtBoxBirthdayYear;
	private VirtualUILabel m_oLabelBirthdayHyphen1;
	private VirtualUITextbox m_oTxtBoxBirthdayMonth;
	private VirtualUILabel m_oLabelBirthdayHyphen2;
	private VirtualUITextbox m_oTxtBoxBirthdayDay;
	private VirtualUILabel m_oLabelBirthday_input;
	
	private VirtualUIButton m_oButtonOK;
	private VirtualUIButton m_oButtonCancel;

	private VirtualUIButton m_oButtonReset;
	private VirtualUIButton m_oButtonReInput;
	private VirtualUIButton m_oButtonAttach;
	private VirtualUIButton m_oButtonPrint; 
	
	private FrameTitleHeader m_oTitleHeader;
	private VirtualUICodeReader m_oCodeReader;
	private HashMap<String, String> m_oCurrentMemberInfo;
	
	private VirtualUIList m_oListMobileResult;
	private PosInterfaceConfig m_oPosInterfaceConfig;
	private String m_sInterfaceKey = "";
	private String m_sGender = "";
	private String m_sRegisteredMemberNumber;
	private String m_sRegisteredMemberSurname;
	
	private static final String GENDER_MR = "Mr";
	private static final String GENDER_MS = "Ms";
	
	private static final String COLOUR_SELECTED = "#0055B8";
	private static final String COLOUR_UNSELECTED = "#5B6F73";
	
	public static final String INPUT_KEY_IN = "key_in";
	public static final String INPUT_SCAN_BARCODE = "scan_barcode";
	
	public String m_oInputType;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameMembershipRegistrationListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameMembershipRegistrationListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameMembershipRegistrationListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public void init(PosInterfaceConfig oPosInterfaceConfig, String sFunctionName, boolean bFromOrderingPanel) {
		m_oPosInterfaceConfig = oPosInterfaceConfig;
		m_sInterfaceKey = oPosInterfaceConfig.getInterfaceVendorKey();
		m_bFromOrderingPanel = bFromOrderingPanel;
		m_sRegisteredMemberNumber = "";
		m_sRegisteredMemberSurname = "";
		m_oInputType = FrameMembershipRegistration.INPUT_KEY_IN;
		m_oCurrentMemberInfo = new HashMap<String, String>();
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameMembershipRegistrationListener>();
		// Load child elements from template
		// Load form from template file
		if(m_sInterfaceKey.equals(InfVendor.KEY_GENERAL_V2))
			m_oTemplateBuilder.loadTemplate("fraMembershipRegistration_generalV2.xml");
		else
			m_oTemplateBuilder.loadTemplate("fraMembershipRegistration.xml");

		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(false);
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("membership_registration"));
		this.attachChild(m_oTitleHeader);
		
		// Registry Frame
		m_oRegistryFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oRegistryFrame, "fraRegistry");
		//this.attachChild(m_oRegistryFrame);
		
		// Result List Frame - For Golden Circle
		if(m_sInterfaceKey.equals(InfVendor.KEY_GOLDEN_CIRCLE) && 
				AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())){
			m_oResultFrame = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(m_oResultFrame, "fraMobileResult");
			
			// List
			m_oListMobileResult = new VirtualUIList();
			m_oTemplateBuilder.buildList(m_oListMobileResult, "listMobileResult");
			m_oResultFrame.attachChild(m_oListMobileResult);
			
			m_oButtonAttachToTable = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonAttachToTable, "btnAttachToTable");
			m_oButtonAttachToTable.setValue(AppGlobal.g_oLang.get()._("attach_to_table"));
			m_oButtonAttachToTable.setVisible(false);
			m_oResultFrame.attachChild(m_oButtonAttachToTable);
			
			this.attachChild(m_oResultFrame);
		}
		
		// Label Prefix
		m_oLabelNRIC = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelNRIC, "lblNRIC");
		m_oLabelNRIC.setTop(m_oTitleHeader.getHeight() + 16);
		if (m_sInterfaceKey.equals(InfVendor.KEY_GENERAL_V2))
			m_oLabelNRIC.setValue(AppGlobal.g_oLang.get()._("prefix"));
		else
			m_oLabelNRIC.setValue(AppGlobal.g_oLang.get()._("nric_passport"));
		m_oRegistryFrame.attachChild(m_oLabelNRIC);
		
		// textbox Prefix
		m_oTxtboxNRIC = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxNRIC, "txtboxNRIC");
		m_oTxtboxNRIC.setLeft(m_oLabelNRIC.getWidth() + 15);
		m_oTxtboxNRIC.setTop(m_oTitleHeader.getHeight() + 16);
		m_oTxtboxNRIC.setFocusWhenShow(true);
		m_oRegistryFrame.attachChild(m_oTxtboxNRIC);
		
		// Label FirstName
		m_oLabelFirstName = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelFirstName, "lblFirstName");
		m_oLabelFirstName.setTop(m_oLabelNRIC.getTop() + m_oLabelNRIC.getHeight() + m_oLabelNRIC.getHeight() / 3);
		if (m_sInterfaceKey.equals(InfVendor.KEY_GENERAL_V2))
			m_oLabelFirstName.setValue(("* ") + AppGlobal.g_oLang.get()._("first_name"));
		else
			m_oLabelFirstName.setValue(AppGlobal.g_oLang.get()._("first_name"));
		m_oRegistryFrame.attachChild(m_oLabelFirstName);
		
		// textbox FirstName
		m_oTxtBoxFirstName = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtBoxFirstName, "txtboxFirstName");
		m_oTxtBoxFirstName.setLeft(m_oLabelFirstName.getWidth() + 15);
		m_oTxtBoxFirstName.setTop(m_oTxtboxNRIC.getTop() + m_oTxtboxNRIC.getHeight() + m_oTxtboxNRIC.getHeight() / 3);
		m_oRegistryFrame.attachChild(m_oTxtBoxFirstName);

		// Label LastName.
		m_oLabelLastName = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelLastName, "lblLastName");
		m_oLabelLastName.setTop(m_oLabelFirstName.getTop() + m_oLabelFirstName.getHeight() + m_oLabelFirstName.getHeight() / 3);
		if (m_sInterfaceKey.equals(InfVendor.KEY_GENERAL_V2)) 
			m_oLabelLastName.setValue(("* ") + AppGlobal.g_oLang.get()._("last_name"));
		else
			m_oLabelLastName.setValue(AppGlobal.g_oLang.get()._("last_name"));
		m_oRegistryFrame.attachChild(m_oLabelLastName);
		
		// textbox LastName
		m_oTxtBoxLastName = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtBoxLastName, "txtboxLastName");
		m_oTxtBoxLastName.setLeft(m_oLabelLastName.getWidth() + 15);
		m_oTxtBoxLastName.setTop(m_oTxtBoxFirstName.getTop() + m_oTxtBoxFirstName.getHeight() + m_oTxtBoxFirstName.getHeight() / 3);
		m_oRegistryFrame.attachChild(m_oTxtBoxLastName);
		
		// Label Gender
		m_oLabelGender = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelGender, "lblGender");
		m_oLabelGender.setValue(AppGlobal.g_oLang.get()._("gender"));
		m_oRegistryFrame.attachChild(m_oLabelGender);
		
		// Create Mr Button
		m_oButtonMr = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonMr, "btnMr");
		m_oButtonMr.setLeft(m_oLabelGender.getWidth() + 15);
		
		if (m_sInterfaceKey.equals(InfVendor.KEY_GOLDEN_CIRCLE))
			m_oButtonMr.setValue(AppGlobal.g_oLang.get()._("mr"));
		else
			m_oButtonMr.setValue(AppGlobal.g_oLang.get()._("mr."));
		
		m_oButtonMr.setClickServerRequestBlockUI(false);
		m_oButtonMr.setClickHideKeyboard(true);
		m_oRegistryFrame.attachChild(m_oButtonMr);
		
		// Create Ms Button
		m_oButtonMs = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonMs, "btnMs");
		m_oButtonMs.setLeft(m_oLabelGender.getWidth() + m_oButtonMr.getWidth() + 30);
		m_oButtonMs.setValue(AppGlobal.g_oLang.get()._("ms"));
		m_oButtonMs.setClickServerRequestBlockUI(false);
		m_oButtonMs.setClickHideKeyboard(true);
		m_oRegistryFrame.attachChild(m_oButtonMs);
		
		// Label Email
		m_oLabelEmail = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelEmail, "lblEmail");
		if (m_sInterfaceKey.equals(InfVendor.KEY_GENERAL_V2))
			m_oLabelEmail.setValue(("* ") + AppGlobal.g_oLang.get()._("email"));
		else
			m_oLabelEmail.setValue(AppGlobal.g_oLang.get()._("email"));
		m_oRegistryFrame.attachChild(m_oLabelEmail);
		
		// textbox Email
		m_oTxtBoxEmail = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtBoxEmail, "txtboxEmail");
		m_oTxtBoxEmail.setLeft(m_oLabelEmail.getWidth() + 15);
		m_oRegistryFrame.attachChild(m_oTxtBoxEmail);

		// Label Language
		m_oLabelLang = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelLang, "lblLanguage");
		m_oLabelLang.setValue(AppGlobal.g_oLang.get()._("language"));
		m_oRegistryFrame.attachChild(m_oLabelLang);
		
		// textbox Language
		m_oTxtBoxLang = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtBoxLang, "txtboxLanguage");
		m_oTxtBoxLang.setLeft(m_oLabelLang.getWidth() + 15);
		if (m_sInterfaceKey.equals(InfVendor.KEY_GENERAL_V2)) {
			if(m_oPosInterfaceConfig.getInterfaceConfig().has("general_setup")
					&& m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").has("default_language")
					&& !m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").optJSONObject("default_language").optString("value").isEmpty())
				m_oTxtBoxLang.setValue(m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").optJSONObject("default_language").optString("value"));
			else
				m_oTxtBoxLang.setHint(AppGlobal.g_oLang.get()._("en_if_nothing_entered"));
		}
		m_oRegistryFrame.attachChild(m_oTxtBoxLang);
		
		// Label Remark
		m_oLabelMemberNumber = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMemberNumber, "lblMemberNumber");
		if (m_sInterfaceKey.equals(InfVendor.KEY_GENERAL_V2))
			m_oLabelMemberNumber.setValue(AppGlobal.g_oLang.get()._("employee_contest"));
		else
			m_oLabelMemberNumber.setValue(AppGlobal.g_oLang.get()._("member_no"));
		m_oRegistryFrame.attachChild(m_oLabelMemberNumber);
		
		// textbox Remark
		m_oTxtBoxMemberNumber = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtBoxMemberNumber, "txtboxMemberNumber");
		m_oTxtBoxMemberNumber.setLeft(m_oLabelMemberNumber.getWidth() + 15);
		m_oRegistryFrame.attachChild(m_oTxtBoxMemberNumber);
		
		// Label MobileNo
		m_oLabelMobileNo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMobileNo, "lblMobileNo");
		m_oLabelMobileNo.setTop(m_oLabelLastName.getTop() + m_oLabelLastName.getHeight() + m_oLabelLastName.getHeight() / 3);
		m_oLabelMobileNo.setValue(AppGlobal.g_oLang.get()._("mobile_no"));
		m_oRegistryFrame.attachChild(m_oLabelMobileNo);
		
		// textbox MobileNo
		m_oTxtBoxMobileNo = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtBoxMobileNo, "txtboxMobileNo");
		m_oTxtBoxMobileNo.setLeft(m_oLabelMobileNo.getWidth() + 15);
		m_oTxtBoxMobileNo.setTop(m_oTxtBoxLastName.getTop() + m_oTxtBoxLastName.getHeight() + m_oTxtBoxLastName.getHeight() /3);
		m_oRegistryFrame.attachChild(m_oTxtBoxMobileNo);
		
		// textbox CountryCode
		m_oTxtBoxCountryCode = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtBoxCountryCode, "txtboxCountryCode");
		m_oRegistryFrame.attachChild(m_oTxtBoxCountryCode);
		
		// Label Birthday
		m_oLabelBirthday = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelBirthday, "lblBirthday");
		m_oLabelBirthday.setTop(m_oLabelMobileNo.getTop() + m_oLabelMobileNo.getHeight() + m_oLabelMobileNo.getHeight() / 3);
		m_oLabelBirthday.setValue(AppGlobal.g_oLang.get()._("birthday"));
		m_oRegistryFrame.attachChild(m_oLabelBirthday);
		
		// textbox Birthday Year
		m_oTxtBoxBirthdayYear = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtBoxBirthdayYear, "txtboxBirthdayYear");
		m_oTxtBoxBirthdayYear.setLeft(m_oLabelBirthday.getWidth() + 15);
		m_oTxtBoxBirthdayYear.setTop(m_oTxtBoxMobileNo.getTop() + m_oTxtBoxMobileNo.getHeight() + m_oTxtBoxMobileNo.getHeight() /3);
		m_oRegistryFrame.attachChild(m_oTxtBoxBirthdayYear);
		
		// Label Birthday hyphen
		m_oLabelBirthdayHyphen1 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelBirthdayHyphen1, "lblBirthdayHyphen");
		m_oLabelBirthdayHyphen1.setLeft(m_oLabelBirthday.getWidth() + m_oTxtBoxBirthdayYear.getWidth() + 15);
		m_oLabelBirthdayHyphen1.setTop(m_oLabelMobileNo.getTop() + m_oLabelMobileNo.getHeight() + m_oLabelMobileNo.getHeight() / 3);
		m_oLabelBirthdayHyphen1.setValue("-");
		m_oRegistryFrame.attachChild(m_oLabelBirthdayHyphen1);
		
		// textbox Birthday Month
		m_oTxtBoxBirthdayMonth = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtBoxBirthdayMonth, "txtboxBirthdayMonth");
		m_oTxtBoxBirthdayMonth.setLeft(m_oLabelBirthday.getWidth() + m_oTxtBoxBirthdayYear.getWidth() + m_oLabelBirthdayHyphen1.getWidth() + 15);
		m_oTxtBoxBirthdayMonth.setTop(m_oTxtBoxMobileNo.getTop() + m_oTxtBoxMobileNo.getHeight() + m_oTxtBoxMobileNo.getHeight() /3);
		m_oRegistryFrame.attachChild(m_oTxtBoxBirthdayMonth);
		
		// Label Birthday hyphen
		m_oLabelBirthdayHyphen2 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelBirthdayHyphen2, "lblBirthdayHyphen");
		m_oLabelBirthdayHyphen2.setLeft(m_oLabelBirthday.getWidth() + m_oTxtBoxBirthdayYear.getWidth() + m_oLabelBirthdayHyphen1.getWidth() +m_oTxtBoxBirthdayMonth.getWidth() + 15);
		m_oLabelBirthdayHyphen2.setTop(m_oLabelMobileNo.getTop() + m_oLabelMobileNo.getHeight() + m_oLabelMobileNo.getHeight() / 3);
		m_oLabelBirthdayHyphen2.setValue("-");
		m_oRegistryFrame.attachChild(m_oLabelBirthdayHyphen2);
		
		// textbox Birthday Day
		m_oTxtBoxBirthdayDay = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtBoxBirthdayDay, "txtboxBirthdayDay");
		m_oTxtBoxBirthdayDay.setLeft(m_oLabelBirthday.getWidth() + m_oTxtBoxBirthdayYear.getWidth() + m_oLabelBirthdayHyphen1.getWidth() +m_oTxtBoxBirthdayMonth.getWidth() + m_oLabelBirthdayHyphen2.getWidth() + 15);
		m_oTxtBoxBirthdayDay.setTop(m_oTxtBoxMobileNo.getTop() + m_oTxtBoxMobileNo.getHeight() + m_oTxtBoxMobileNo.getHeight() /3);
		m_oRegistryFrame.attachChild(m_oTxtBoxBirthdayDay);
		
		// Label Birthday Remark
		m_oLabelBirthday_input = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelBirthday_input, "lblBithday_input");
		m_oLabelBirthday_input.setLeft(m_oLabelBirthday.getWidth() + 15);
		m_oLabelBirthday_input.setTop(m_oTxtBoxBirthdayDay.getTop() + m_oTxtBoxBirthdayDay.getHeight() - 5);
		m_oLabelBirthday_input.setValue("( " + AppGlobal.g_oLang.get()._("birthday_format") + " : " + "YYYY-MM-DD" + " )");
		m_oRegistryFrame.attachChild(m_oLabelBirthday_input);
					
		// Create OK Button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "btnOK");
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("register"));
		m_oButtonOK.addClickServerRequestSubmitElement(this);
		m_oRegistryFrame.attachChild(m_oButtonOK);
		
		// Create Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "btnCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		m_oButtonCancel.setClickServerRequestBlockUI(false);
		m_oRegistryFrame.attachChild(m_oButtonCancel);

		// Create Reset Button
		m_oButtonReset = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonReset, "btnReset");
		m_oButtonReset.setValue(AppGlobal.g_oLang.get()._("reset_all"));
		m_oButtonReset.addClickServerRequestSubmitElement(this);
		m_oRegistryFrame.attachChild(m_oButtonReset);
		
		// Create ReInput Button
		m_oButtonReInput = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonReInput, "btnReInput");
		m_oButtonReInput.setValue(AppGlobal.g_oLang.get()._("re_input_card_number"));
		m_oButtonReInput.setClickServerRequestBlockUI(false);
		m_oRegistryFrame.attachChild(m_oButtonReInput);
		
		if(m_sInterfaceKey.equals(InfVendor.KEY_GOLDEN_CIRCLE) && 
				AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())){
			// QR code scanner
			m_oCodeReader = new VirtualUICodeReader();
			m_oTemplateBuilder.buildCodeReader(m_oCodeReader, "codeReader");
			m_oCodeReader.allowValueChanged(true);
			m_oCodeReader.addValueChangedServerRequestSubmitElement(m_oCodeReader);
			m_oRegistryFrame.attachChild(m_oCodeReader);
		}
		this.attachChild(m_oRegistryFrame);
		
		// Set Top only or set Visible or set Value only
		if (m_sInterfaceKey.equals(InfVendor.KEY_ASCENTIS_CRM)) {
			m_oLabelNRIC.setTop(m_oTitleHeader.getHeight() + 16);
			m_oTxtboxNRIC.setTop(m_oTitleHeader.getHeight() + 16);
			m_oLabelFirstName.setTop(m_oLabelNRIC.getTop() + m_oLabelNRIC.getHeight() + m_oLabelNRIC.getHeight() / 3);
			m_oTxtBoxFirstName.setTop(m_oTxtboxNRIC.getTop() + m_oTxtboxNRIC.getHeight() + m_oTxtboxNRIC.getHeight() / 3);
			m_oLabelLastName.setTop(m_oLabelFirstName.getTop() + m_oLabelFirstName.getHeight() + m_oLabelFirstName.getHeight() / 3);
			m_oTxtBoxLastName.setTop(m_oTxtBoxFirstName.getTop() + m_oTxtBoxFirstName.getHeight() + m_oTxtBoxFirstName.getHeight() / 3);
			m_oLabelGender.setVisible(false);
			m_oButtonMr.setVisible(false);
			m_oButtonMs.setVisible(false);
			m_oLabelEmail.setVisible(false);
			m_oTxtBoxEmail.setVisible(false);
			m_oLabelMemberNumber.setVisible(false);
			m_oTxtBoxMemberNumber.setVisible(false);
			
			m_oTxtBoxCountryCode.setVisible(false);
			m_oLabelMobileNo.setTop(m_oLabelLastName.getTop() + m_oLabelLastName.getHeight() + m_oLabelLastName.getHeight() / 3);
			m_oTxtBoxMobileNo.setTop(m_oTxtBoxLastName.getTop() + m_oTxtBoxLastName.getHeight() + m_oTxtBoxLastName.getHeight() /3);
			m_oLabelBirthday.setTop(m_oLabelMobileNo.getTop() + m_oLabelMobileNo.getHeight() + m_oLabelMobileNo.getHeight() / 3);
			m_oTxtBoxBirthdayYear.setTop(m_oTxtBoxMobileNo.getTop() + m_oTxtBoxMobileNo.getHeight() + m_oTxtBoxMobileNo.getHeight() /3);
			m_oLabelBirthdayHyphen1.setTop(m_oLabelMobileNo.getTop() + m_oLabelMobileNo.getHeight() + m_oLabelMobileNo.getHeight() / 3);
			m_oTxtBoxBirthdayMonth.setTop(m_oTxtBoxMobileNo.getTop() + m_oTxtBoxMobileNo.getHeight() + m_oTxtBoxMobileNo.getHeight() /3);
			m_oLabelBirthdayHyphen2.setTop(m_oLabelMobileNo.getTop() + m_oLabelMobileNo.getHeight() + m_oLabelMobileNo.getHeight() / 3);
			m_oTxtBoxBirthdayDay.setTop(m_oTxtBoxMobileNo.getTop() + m_oTxtBoxMobileNo.getHeight() + m_oTxtBoxMobileNo.getHeight() /3);
			m_oLabelBirthday_input.setTop(m_oTxtBoxBirthdayDay.getTop() + m_oTxtBoxBirthdayDay.getHeight() - 5);
			m_oButtonOK.setTop(m_oLabelBirthday_input.getTop() + m_oLabelBirthday_input.getHeight());
			m_oButtonCancel.setTop(m_oLabelBirthday_input.getTop() + m_oLabelBirthday_input.getHeight());
			m_oButtonReset.setVisible(false);
			m_oButtonReInput.setVisible(false);
			//m_oButtonAttach.setVisible(false);
			//this.setHeight(m_oButtonOK.getTop() + m_oButtonOK.getHeight() + m_oButtonOK.getHeight() /3);
		} else if (m_sInterfaceKey.equals(InfVendor.KEY_GOLDEN_CIRCLE)) {
			m_oTitleHeader.setTitle(sFunctionName);
			m_oLabelNRIC.setVisible(false);
			m_oTxtboxNRIC.setVisible(false);
			
			if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())){
				m_oLabelFirstName.setTop(10);
				m_oTxtBoxFirstName.setTop(10);
				m_oTxtBoxMobileNo.setWidth(205);
			}else{
				m_oLabelFirstName.setTop(m_oTitleHeader.getHeight() + 16);
				m_oTxtBoxFirstName.setTop(m_oTitleHeader.getHeight() + 16);
				m_oTxtBoxMobileNo.setWidth(255);
			}
			
			m_oLabelLastName.setTop(m_oLabelFirstName.getTop() + m_oLabelFirstName.getHeight() + m_oLabelFirstName.getHeight() / 3);
			m_oTxtBoxLastName.setTop(m_oTxtBoxFirstName.getTop() + m_oTxtBoxFirstName.getHeight() + m_oTxtBoxFirstName.getHeight() / 3);
			m_oLabelGender.setTop(m_oLabelLastName.getTop() + m_oLabelLastName.getHeight() + m_oLabelLastName.getHeight() / 3);
			m_oButtonMr.setTop(m_oTxtBoxLastName.getTop() + m_oTxtBoxLastName.getHeight() + m_oTxtBoxLastName.getHeight() / 3);
			m_oButtonMs.setTop(m_oTxtBoxLastName.getTop() + m_oTxtBoxLastName.getHeight() + m_oTxtBoxLastName.getHeight() / 3);
			
			m_oLabelMobileNo.setTop(m_oLabelGender.getTop() + m_oLabelGender.getHeight() + m_oLabelGender.getHeight() / 3);
			m_oTxtBoxCountryCode.setTop(m_oLabelGender.getTop() + m_oLabelGender.getHeight() + m_oLabelGender.getHeight() /3);
			m_oTxtBoxCountryCode.setLeft(m_oLabelMobileNo.getWidth() + 15);
			m_oTxtBoxMobileNo.setTop(m_oLabelGender.getTop() + m_oLabelGender.getHeight() + m_oLabelGender.getHeight() /3);
			m_oTxtBoxMobileNo.setLeft(m_oTxtBoxCountryCode.getLeft() + m_oTxtBoxCountryCode.getWidth() + 15);
			
			m_oLabelEmail.setTop(m_oLabelMobileNo.getTop() + m_oLabelMobileNo.getHeight() + m_oLabelMobileNo.getHeight() / 3);
			m_oTxtBoxEmail.setTop(m_oTxtBoxCountryCode.getTop() + m_oTxtBoxCountryCode.getHeight() + m_oTxtBoxCountryCode.getHeight() / 3);
			
			m_oLabelMemberNumber.setTop(m_oLabelEmail.getTop() + m_oLabelEmail.getHeight() + m_oLabelEmail.getHeight() / 3);
			m_oTxtBoxMemberNumber.setTop(m_oTxtBoxEmail.getTop() + m_oTxtBoxEmail.getHeight() + m_oTxtBoxEmail.getHeight() / 3);
			
			m_oLabelBirthday.setVisible(false);
			m_oTxtBoxBirthdayYear.setVisible(false);
			m_oLabelBirthdayHyphen1.setVisible(false);
			m_oTxtBoxBirthdayMonth.setVisible(false);
			m_oLabelBirthdayHyphen2.setVisible(false);
			m_oTxtBoxBirthdayDay.setVisible(false);
			m_oLabelBirthday_input.setVisible(false);
			
			m_oButtonOK.setTop(m_oTxtBoxMemberNumber.getTop() + m_oTxtBoxMemberNumber.getHeight() + m_oTxtBoxMemberNumber.getHeight() / 3);
			m_oButtonOK.setLeft(20);
			m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("enroll"));
			m_oButtonReInput.setTop(m_oTxtBoxMemberNumber.getTop() + m_oTxtBoxMemberNumber.getHeight() + m_oTxtBoxMemberNumber.getHeight() / 3);
			m_oButtonReInput.setLeft(m_oButtonOK.getLeft() + m_oButtonOK.getWidth() + m_oButtonOK.getWidth() /6);
			m_oButtonReset.setTop(m_oTxtBoxMemberNumber.getTop() + m_oTxtBoxMemberNumber.getHeight() + m_oTxtBoxMemberNumber.getHeight() / 3);
			m_oButtonReset.setLeft(m_oButtonReInput.getLeft() + m_oButtonReInput.getWidth() + m_oButtonReInput.getWidth() /6);
			m_oButtonCancel.setTop(m_oTxtBoxMemberNumber.getTop() + m_oTxtBoxMemberNumber.getHeight() + m_oTxtBoxMemberNumber.getHeight() / 3);
			m_oButtonCancel.setLeft(m_oButtonReset.getLeft() + m_oButtonReset.getWidth() + m_oButtonReInput.getWidth() /6);
			m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("exit"));
			//m_oButtonAttach.setVisible(false);
			
			if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())){
				m_oResultFrame.setVisible(false);
				m_oCodeReader.setLeft(m_oButtonOK.getLeft());
				m_oCodeReader.setTop(m_oTxtBoxMemberNumber.getTop() + m_oTxtBoxMemberNumber.getHeight() + m_oTxtBoxMemberNumber.getHeight() / 3);
				m_oButtonOK.setTop(m_oCodeReader.getTop() + m_oCodeReader.getHeight() + m_oCodeReader.getHeight() /6);
				m_oButtonReInput.setTop(m_oCodeReader.getTop() + m_oCodeReader.getHeight() + m_oCodeReader.getHeight() /6);
				m_oButtonReInput.setLeft(m_oButtonOK.getLeft() + m_oButtonOK.getWidth() + m_oButtonOK.getWidth() /6);
				m_oButtonReset.setTop(m_oCodeReader.getTop() + m_oCodeReader.getHeight() + m_oCodeReader.getHeight() /6);
				m_oButtonCancel.setTop(m_oCodeReader.getTop() + m_oCodeReader.getHeight() + m_oCodeReader.getHeight() /6);
			}
		} else if(m_sInterfaceKey.equals(InfVendor.KEY_GENERAL_V2)) {
			m_oTitleHeader.setTitle(sFunctionName);
			m_oLabelGender.setVisible(false);
			m_oLabelMobileNo.setVisible(false);
			m_oTxtBoxMobileNo.setVisible(false);
			m_oButtonMr.setVisible(false);
			m_oButtonMs.setVisible(false);
			m_oTxtBoxCountryCode.setVisible(false);
			m_oLabelBirthday.setVisible(false);
			m_oTxtBoxBirthdayYear.setVisible(false);
			m_oLabelBirthdayHyphen1.setVisible(false);
			m_oTxtBoxBirthdayMonth.setVisible(false);
			m_oLabelBirthdayHyphen2.setVisible(false);
			m_oTxtBoxBirthdayDay.setVisible(false);
			m_oLabelBirthday_input.setVisible(false);
			m_oButtonReInput.setVisible(false);
			
			m_oLabelEmail.setTop(m_oLabelLastName.getTop() + m_oLabelLastName.getHeight() + m_oLabelLastName.getHeight() / 3);
			m_oTxtBoxEmail.setTop(m_oTxtBoxLastName.getTop() + m_oTxtBoxLastName.getHeight() + m_oTxtBoxLastName.getHeight() /3);
			
			m_oLabelLang.setTop(m_oLabelEmail.getTop() + m_oLabelEmail.getHeight() + m_oLabelEmail.getHeight() / 3);
			m_oTxtBoxLang.setTop(m_oTxtBoxEmail.getTop() + m_oTxtBoxEmail.getHeight() + m_oTxtBoxEmail.getHeight() / 3);
			
			m_oLabelMemberNumber.setTop(m_oLabelLang.getTop() + m_oLabelLang.getHeight() + m_oLabelLang.getHeight() / 3);
			m_oTxtBoxMemberNumber.setTop(m_oTxtBoxLang.getTop() + m_oTxtBoxLang.getHeight() + m_oTxtBoxLang.getHeight() / 3);

			m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("enroll"));
			
			m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("exit"));
		}
	}
	
	private void changeGenderButtonColor(String sGender) {
		if (sGender.equals(FrameMembershipRegistration.GENDER_MR)) {
			m_sGender = FrameMembershipRegistration.GENDER_MR;
			m_oButtonMr.setBackgroundColor(FrameMembershipRegistration.COLOUR_SELECTED);
			m_oButtonMs.setBackgroundColor(FrameMembershipRegistration.COLOUR_UNSELECTED);
		} else {
			m_sGender = FrameMembershipRegistration.GENDER_MS;
			m_oButtonMr.setBackgroundColor(FrameMembershipRegistration.COLOUR_UNSELECTED);
			m_oButtonMs.setBackgroundColor(FrameMembershipRegistration.COLOUR_SELECTED);
		}
	}
	
	private void clearAllTextbox() {
		m_oTxtBoxBirthdayDay.setValue("");
		m_oTxtBoxBirthdayMonth.setValue("");
		m_oTxtBoxBirthdayYear.setValue("");
		m_oTxtBoxEmail.setValue("");
		m_oTxtBoxFirstName.setValue("");
		m_oTxtBoxLastName.setValue("");
		m_oTxtBoxMemberNumber.setValue("");
		m_oTxtBoxMobileNo.setValue("");
		m_oTxtboxNRIC.setValue("");
		m_oTxtBoxLang.setValue("");
		if(m_sInterfaceKey.equals(InfVendor.KEY_GOLDEN_CIRCLE) && 
				AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			resetCodeReader();
		m_oInputType = FrameMembershipRegistration.INPUT_KEY_IN;
		
		m_sGender = "";
		m_oButtonMs.setBackgroundColor(FrameMembershipRegistration.COLOUR_UNSELECTED);
		m_oButtonMr.setBackgroundColor(FrameMembershipRegistration.COLOUR_UNSELECTED);
	}
	
	public void setDefaultCountryCode(String sDefaultCountryCode) {
		m_oTxtBoxCountryCode.setValue(sDefaultCountryCode);
	}
	
	public void showMemberResult_GoldenCircle(HashMap<String, String> oMemberHashMap) {
		clearAllTextbox();
		m_oCurrentMemberInfo = oMemberHashMap;
		
		m_oLabelNRIC.setVisible(false);
		m_oTxtboxNRIC.setVisible(false);
		m_oResultFrame.setVisible(true);
		
		m_oListMobileResult.removeAllChildren();
		m_oListMobileResult.setVisible(true);
		m_oListMobileResult.setTop(0);
		
		int iRoundingDecimal = 0;
		
		if (m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("currency_setup").optJSONObject("params").has("rounding_decimal"))
			iRoundingDecimal = m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("currency_setup").optJSONObject("params").optJSONObject("rounding_decimal").optInt("value", 0);
		
		
		addMobileResult(null, AppGlobal.g_oLang.get()._("successfully_enrolled"), null, null, null, 0);
		addMobileResult(AppGlobal.g_oLang.get()._("member_information"), oMemberHashMap.get("memberNumber"), oMemberHashMap.get("memberType"), null, oMemberHashMap.get("expiryDate").substring(0, oMemberHashMap.get("expiryDate").length() - 9), 0);
		addMobileResult(null, oMemberHashMap.get("firstName") + " " + oMemberHashMap.get("lastName"), null, null, null, 0);
		
		//Call function from ordering panel
		if(!m_bFromOrderingPanel){
			//m_oResultFrame.setHeight(m_oListMobileResult.getHeight());
			m_oButtonAttachToTable.setTop(m_oListMobileResult.getHeight() - 40);
			m_oButtonAttachToTable.setVisible(true);
			m_oResultFrame.setHeight(m_oButtonAttachToTable.getTop() + m_oButtonAttachToTable.getHeight());
			m_sRegisteredMemberNumber = oMemberHashMap.get("memberNumber");
			m_sRegisteredMemberSurname = oMemberHashMap.get("lastName");
			m_oRegistryFrame.setTop(m_oResultFrame.getTop() + m_oResultFrame.getHeight());
		}else{
			String sPointsLocal = StringLib.BigDecimalToString(new BigDecimal(oMemberHashMap.get("pointsLocal")), iRoundingDecimal);
			String sPointsExpiringLocal = StringLib.BigDecimalToString(new BigDecimal(oMemberHashMap.get("pointsExpiringLocal")), iRoundingDecimal);
			addMobileResult(AppGlobal.g_oLang.get()._("member_points"), AppGlobal.g_oLang.get()._("current_points"), oMemberHashMap.get("pointsBalance"), AppGlobal.g_oFuncOutlet.get().getCurrencyCode(), sPointsLocal , 0);
			addMobileResult(null, AppGlobal.g_oLang.get()._("expire_this_year"), oMemberHashMap.get("pointsExpiring"), AppGlobal.g_oFuncOutlet.get().getCurrencyCode(), sPointsExpiringLocal, 0);
			
			
			String sPreference = this.preferenceMapping(oMemberHashMap.get("preference"));
			int iPreferenceHeight = 100;
			iPreferenceHeight = iPreferenceHeight + 100 * (sPreference.length() / 150);
			addMobileResult(AppGlobal.g_oLang.get()._("member_preference"), sPreference, null, null, null, iPreferenceHeight);
			
			
			m_oListMobileResult.setHeight(m_oListMobileResult.getHeight() * 4);
			m_oResultFrame.setHeight(m_oListMobileResult.getHeight());
			m_oRegistryFrame.setVisible(false);
			
			// Create "Attach" and "Print" button
			m_oButtonAttach = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonAttach, "btnAttach");
			m_oButtonAttach.setValue(AppGlobal.g_oLang.get()._("attach"));
			m_oButtonAttach.setClickServerRequestBlockUI(false);
			this.attachChild(m_oButtonAttach);
			
			// Print Button
			m_oButtonPrint = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonPrint, "btnPrint");
			m_oButtonPrint.setValue(AppGlobal.g_oLang.get()._("print"));
			m_oButtonPrint.setClickServerRequestBlockUI(false);
			this.attachChild(m_oButtonPrint);
		}
	}
	
	private void addMobileResult(String sTitle, String sContent1, String sContent2, String sContent3, String sContent4, int iHeight) {
		VirtualUIFrame oFrameMemberRecord;
		
		if (sTitle != null){
			VirtualUILabel oLabelTitle = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabelTitle, "lblMobileRecordHeader");
			oLabelTitle.setValue(sTitle);
			m_oListMobileResult.attachChild(oLabelTitle);
		}
		
		oFrameMemberRecord = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameMemberRecord, "fraMemberRecord");
		
		if (sContent1 == null)
			return;
		
		VirtualUILabel oLabelContent1 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelContent1, "lblMobileRecord");
		oLabelContent1.setValue(sContent1);
		oLabelContent1.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		oFrameMemberRecord.attachChild(oLabelContent1);
		
		if(iHeight != 0) {
			oLabelContent1.setHeight(iHeight);
			oFrameMemberRecord.setHeight(iHeight);
		}
		
		
		if (sContent2 == null){
			oLabelContent1.setWidth(400);
			m_oListMobileResult.attachChild(oFrameMemberRecord);
		}
		
		VirtualUILabel oLabelContent2 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelContent2, "lblMobileRecord");
		oLabelContent2.setLeft(oLabelContent1.getLeft() + oLabelContent1.getWidth());
		oLabelContent2.setWidth(130);
		oLabelContent2.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
		oLabelContent2.setValue(sContent2);
		oFrameMemberRecord.attachChild(oLabelContent2);
		
		if (sContent3 == null)
			m_oListMobileResult.attachChild(oFrameMemberRecord);
		
		VirtualUILabel oLabelContent3 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelContent3, "lblMobileRecord");
		oLabelContent3.setLeft(oLabelContent2.getLeft() + oLabelContent2.getWidth());
		oLabelContent3.setWidth(60);
		oLabelContent3.setValue(sContent3);
		oLabelContent3.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		oFrameMemberRecord.attachChild(oLabelContent3);
		
		if (sContent4 == null)
			m_oListMobileResult.attachChild(oFrameMemberRecord);
		
		VirtualUILabel oLabelContent4 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelContent4, "lblMobileRecord");
		oLabelContent4.setWidth(this.getWidth() - oLabelContent1.getWidth() - oLabelContent2.getWidth() - oLabelContent3.getWidth());
		oLabelContent4.setLeft(oLabelContent3.getLeft() + oLabelContent3.getWidth());
		oLabelContent4.setValue(sContent4);
		oLabelContent4.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
		oFrameMemberRecord.attachChild(oLabelContent4);
		
		m_oListMobileResult.attachChild(oFrameMemberRecord);
		
	}
	
	private void resetCodeReader(){
		//remove code reader
		m_oCodeReader.removeMyself();
		
		//new code reader
		m_oCodeReader = new VirtualUICodeReader();
		m_oTemplateBuilder.buildCodeReader(m_oCodeReader, "codeReader");
		m_oCodeReader.allowValueChanged(true);
		m_oCodeReader.allowClick(true);
		m_oCodeReader.addValueChangedServerRequestSubmitElement(m_oCodeReader);
		m_oRegistryFrame.attachChild(m_oCodeReader);
		
		m_oCodeReader.setTop(m_oTxtBoxMemberNumber.getTop() + m_oTxtBoxMemberNumber.getHeight() + m_oTxtBoxMemberNumber.getHeight() / 3);
	}
	
	public void updateMemberNumber(String sMemberNum){
		m_oTxtBoxMemberNumber.setValue(sMemberNum);
	}
	
	public String getRegisteredMemberNumber() {
		return m_sRegisteredMemberNumber;
	}
	
	public String getRegisteredMemberSurname() {
		return m_sRegisteredMemberSurname;
	}
	
	// Preference mapping for GC
	private String preferenceMapping(String sPreferenceList) {
		HashMap<String, String> oPreferenceHashMap = new HashMap<String, String>();
		HashMap<String, List<String>> oHashMap = new HashMap<String, List<String>>();
		if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GOLDEN_CIRCLE)) {
			// find preference mapping
			if(m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").has("preference_mapping_setting")) {
				String sPreferenceMapping = m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").optJSONObject("preference_mapping_setting").optString("value");
				String[] sPreferences = sPreferenceMapping.split("\\n");
				for(String sPreference : sPreferences) {
					sPreference = sPreference.trim();
					String[] sPreferenceMappingLists = sPreference.split("=");
					if (sPreferenceMappingLists.length < 2)
						continue;
						
					String[] sDetail = sPreferenceMappingLists[1].trim().split(":");
					List<String> oListInfo = new ArrayList<String>();
					oListInfo.add(sDetail[0]);
					if (sDetail.length >= 2)
						oListInfo.add(sDetail[1]);
					else
						oListInfo.add(sPreferenceMappingLists[0].trim());
					oHashMap.put(sPreferenceMappingLists[0], oListInfo);
				}
			}
		}
		
		// create formatted preference info
		String[] oPreferenceCodeList = sPreferenceList.split(",");
		for(int i = 0 ; i < oPreferenceCodeList.length ; i++) {
			oPreferenceCodeList[i] = oPreferenceCodeList[i].replace("[", "");
			oPreferenceCodeList[i] = oPreferenceCodeList[i].replace("]", "");
			oPreferenceCodeList[i] = oPreferenceCodeList[i].replace("\"", "");
			if(oHashMap.containsKey(oPreferenceCodeList[i])) {
				if(oPreferenceHashMap.containsKey(oHashMap.get(oPreferenceCodeList[i]).get(0)))
					oPreferenceHashMap.put(oHashMap.get(oPreferenceCodeList[i]).get(0), oPreferenceHashMap.get(oHashMap.get(oPreferenceCodeList[i]).get(0)) + ", " + oHashMap.get(oPreferenceCodeList[i]).get(1));
				else
					oPreferenceHashMap.put(oHashMap.get(oPreferenceCodeList[i]).get(0), oHashMap.get(oPreferenceCodeList[i]).get(0) + ": " + oHashMap.get(oPreferenceCodeList[i]).get(1));
			}
		}
		
		List<String> oSortedPreferences = new ArrayList<String>(oPreferenceHashMap.values());
		java.util.Collections.sort(oSortedPreferences);
		
		String sPrefernece = "";
		for (String sInfo : oSortedPreferences)
			sPrefernece = sPrefernece.concat(sInfo + System.lineSeparator() + System.lineSeparator());
			
		return sPrefernece;
	}
	
	public boolean isFromOrderingPanel() {
		return m_bFromOrderingPanel;
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();
		
		if (iChildId == m_oButtonOK.getId()) {
			AppGlobal.g_oTerm.get().hideKeyboard();
			
			if (m_sInterfaceKey.equals(InfVendor.KEY_ASCENTIS_CRM)) {
				if (m_oTxtboxNRIC.getValue().isEmpty()) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("nric_passport_is_empty"));
					oFormDialogBox.show();
					return false;
				}
				
				if (m_oTxtBoxFirstName.getValue().isEmpty()) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("first_name_is_empty"));
					oFormDialogBox.show();
					return false;
				}
				
				if (m_oTxtBoxLastName.getValue().isEmpty())  {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("last_name_is_empty"));
					oFormDialogBox.show();
					return false;
				}
				
				if (m_oTxtBoxMobileNo.getValue().isEmpty())  {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("mobile_no_is_empty"));
					oFormDialogBox.show();
					return false;
				}
				
				if (m_oTxtBoxBirthdayYear.getValue().isEmpty())  {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("birthday_year_is_empty"));
					oFormDialogBox.show();
					return false;
				}
				
				if (m_oTxtBoxBirthdayMonth.getValue().isEmpty())  {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("birthday_month_is_empty"));
					oFormDialogBox.show();
					return false;
				}
				
				if (m_oTxtBoxBirthdayDay.getValue().isEmpty())  {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("birthday_day_is_empty"));
					oFormDialogBox.show();
					return false;
				}
				
				String sDate = m_oTxtBoxBirthdayYear.getValue() + "-" + m_oTxtBoxBirthdayMonth.getValue() + "-" + m_oTxtBoxBirthdayDay.getValue();
				Date date = null;
				try {
					SimpleDateFormat oSdf = new SimpleDateFormat("yyyy-mm-dd");
					date = oSdf.parse(sDate);
					if (!sDate.equals(oSdf.format(date))) {
						date = null;
					}
				} catch (ParseException ex) {
				}

				if (date == null){
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("invalid_birthday_format"));
					oFormDialogBox.show();
					return false;
				}
				
				FormConfirmBox oComfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this.getParentForm());
				oComfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
				oComfirmBox.setMessage(AppGlobal.g_oLang.get()._("confirm_to_register") + " ?" + System.lineSeparator()
						+ System.lineSeparator() + AppGlobal.g_oLang.get()._("nric_passport") + ": "
						+ m_oTxtboxNRIC.getValue() + System.lineSeparator() + AppGlobal.g_oLang.get()._("first_name")
						+ ": " + m_oTxtBoxFirstName.getValue() + System.lineSeparator()
						+ AppGlobal.g_oLang.get()._("last_name") + ": " + m_oTxtBoxLastName.getValue()
						+ System.lineSeparator() + AppGlobal.g_oLang.get()._("mobile_no") + ": "
						+ m_oTxtBoxMobileNo.getValue() + System.lineSeparator() + AppGlobal.g_oLang.get()._("birthday")
						+ ": " + sDate);
				oComfirmBox.show();
				
				if (!oComfirmBox.isOKClicked())
					return false;
				
				if (m_oTxtboxNRIC.getValue().isEmpty())
					oEnquiryInfo.put("memberNumber", "");
				else
					oEnquiryInfo.put("memberNumber", m_oTxtboxNRIC.getValue());
				
				if (m_oTxtBoxFirstName.getValue().isEmpty())
					oEnquiryInfo.put("firstName", "");
				else
					oEnquiryInfo.put("firstName", m_oTxtBoxFirstName.getValue());
				
				if (m_oTxtBoxLastName.getValue().isEmpty())
					oEnquiryInfo.put("lastName", "");
				else
					oEnquiryInfo.put("lastName", m_oTxtBoxLastName.getValue());
				
				if (m_oTxtBoxMobileNo.getValue().isEmpty())
					oEnquiryInfo.put("mobileNumber", "");
				else
					oEnquiryInfo.put("mobileNumber", m_oTxtBoxMobileNo.getValue());
				
				oEnquiryInfo.put("birthday", sDate);
	
				String sPassword = m_oTxtBoxBirthdayDay.getValue() + m_oTxtBoxBirthdayMonth.getValue() + m_oTxtBoxBirthdayYear.getValue();
						
				oEnquiryInfo.put("password", sPassword);
			} else if (m_sInterfaceKey.equals(InfVendor.KEY_GOLDEN_CIRCLE)) {
				if (m_oTxtBoxFirstName.getValue().trim().isEmpty() || !m_oTxtBoxFirstName.getValue().matches("^[a-zA-Z ]+$")) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
							this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("invalid_first_name"));
					oFormDialogBox.show();
					return false;
				}

				if (m_oTxtBoxLastName.getValue().trim().isEmpty() || !m_oTxtBoxLastName.getValue().matches("^[a-zA-Z ]+$")) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
							this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("invalid_last_name"));
					oFormDialogBox.show();
					return false;
				}
				if (m_sGender.isEmpty()) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
							this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("gender_is_not_selected"));
					oFormDialogBox.show();
					return false;
				}
				// Need to be with ALL digits
				if (m_oTxtBoxCountryCode.getValue().isEmpty() || m_oTxtBoxMobileNo.getValue().isEmpty()
						|| !m_oTxtBoxCountryCode.getValue().matches("^[0-9]+$") || !m_oTxtBoxMobileNo.getValue().matches("^[0-9]+$")) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
							this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("invalid_mobile"));
					oFormDialogBox.show();
					return false;
				}
				if (m_oTxtBoxEmail.getValue().trim().isEmpty() || !m_oTxtBoxEmail.getValue().matches(".+[@].+[.].+")) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
							this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("invalid_email"));
					oFormDialogBox.show();
					return false;
				}
				if (m_oTxtBoxMemberNumber.getValue().trim().isEmpty() || m_oTxtBoxMemberNumber.getValue().length() != 12
						|| !m_oTxtBoxMemberNumber.getValue().matches("[0-9]{12}")) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
							this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("invalid_gc_number"));
					oFormDialogBox.show();
					return false;
				}
			
				// Check digit, Only sum of all digits % 7 equals the last digit
				int iDigitSum = 0, iLastDigit = 0;
				for(int i = 0; i < m_oTxtBoxMemberNumber.getValue().length() - 1; )
					iDigitSum += Integer.parseInt(m_oTxtBoxMemberNumber.getValue().substring(i, ++i)) * i;	// 1st * 1, 2nd * 2, ......
				iLastDigit = Integer.parseInt(m_oTxtBoxMemberNumber.getValue().substring(m_oTxtBoxMemberNumber.getValue().length() - 1, 
						m_oTxtBoxMemberNumber.getValue().length()));
				if (iDigitSum % 7 != iLastDigit) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
							this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("check_digit_failed"));
					oFormDialogBox.show();
					return false;
				}
				
				oEnquiryInfo.put("vendorKey", m_sInterfaceKey);
				oEnquiryInfo.put("givenName", m_oTxtBoxFirstName.getValue());
				oEnquiryInfo.put("surname", m_oTxtBoxLastName.getValue());
				oEnquiryInfo.put("gender", m_sGender);
				oEnquiryInfo.put("email", m_oTxtBoxEmail.getValue());
				oEnquiryInfo.put("cardNumber", m_oTxtBoxMemberNumber.getValue());
				oEnquiryInfo.put("mobileNumber", m_oTxtBoxCountryCode.getValue() + " " + m_oTxtBoxMobileNo.getValue());
				
				oEnquiryInfo.put("inputType", m_oInputType);
				oEnquiryInfo.put("shopId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getShopId()));
				oEnquiryInfo.put("bdayId", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
				oEnquiryInfo.put("stationId", String.valueOf(AppGlobal.g_oFuncStation.get().getStation().getStatId()));
				oEnquiryInfo.put("userId", String.valueOf(AppGlobal.g_oFuncUser.get().getUserId()));
			}
			else if (m_sInterfaceKey.equals(InfVendor.KEY_GENERAL_V2)) {
				// Check data validation of First Name, Last Name, Email and Language
				if (m_oTxtBoxFirstName.getValue().trim().isEmpty() || !m_oTxtBoxFirstName.getValue().matches("^[a-zA-Z ]+$")) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
							this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("invalid_first_name"));
					oFormDialogBox.show();
					return false;
				}

				if (m_oTxtBoxLastName.getValue().trim().isEmpty() || !m_oTxtBoxLastName.getValue().matches("^[a-zA-Z ]+$")) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
							this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("invalid_last_name"));
					oFormDialogBox.show();
					return false;
				}

				if (m_oTxtBoxEmail.getValue().trim().isEmpty() || !m_oTxtBoxEmail.getValue().matches(".+[@].+[.].+")) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
							this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("invalid_email"));
					oFormDialogBox.show();
					return false;
				}
				
				oEnquiryInfo.put("businessDate", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInStringWithFormat("yyyy-MM-dd"));
				oEnquiryInfo.put("timeZone", String.valueOf(AppGlobal.g_oFuncOutlet.get().getShop().getTimezone()));
				oEnquiryInfo.put("memberPrefix", m_oTxtboxNRIC.getValue());
				oEnquiryInfo.put("firstName", m_oTxtBoxFirstName.getValue());
				oEnquiryInfo.put("lastName", m_oTxtBoxLastName.getValue());
				oEnquiryInfo.put("email", m_oTxtBoxEmail.getValue());
				oEnquiryInfo.put("preferredLanguage", m_oTxtBoxLang.getValue());
				oEnquiryInfo.put("contestNumber", m_oTxtBoxMemberNumber.getValue());
			}
			for (FrameMembershipRegistrationListener listener : listeners) {
				// Raise the event to parent
				listener.FrameMembershipRegistration_clickOK(oEnquiryInfo);
			}
			
			bMatchChild = true;
		} else if (iChildId == m_oButtonCancel.getId()) {
			for (FrameMembershipRegistrationListener listener : listeners) {
				// Raise the event to parent
				listener.FrameMembershipRegistration_clickCancel();
			}
			
			AppGlobal.g_oTerm.get().hideKeyboard();
			
			bMatchChild = true;
		}else if (iChildId == m_oLabelBirthday_input.getId()){
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			DateTime oBirthday = null;
			
			if (m_oLabelBirthday_input.getValue() != null)
				oBirthday = formatter.parseDateTime(m_oLabelBirthday_input.getValue());
			
			FormDatePicker oFormDatePicker = new FormDatePicker(oBirthday, this.getParentForm());
			oFormDatePicker.show();
			
			if (oFormDatePicker.getDate() != null)
				m_oLabelBirthday_input.setValue(oFormDatePicker.getDate());
			
			bMatchChild = true;

		}else if (iChildId == m_oButtonMr.getId()){
			changeGenderButtonColor(FrameMembershipRegistration.GENDER_MR);
			//hide keyboard
			AppGlobal.g_oTerm.get().hideKeyboard();
			
			bMatchChild = true;
		}else if (iChildId == m_oButtonMs.getId()){
			changeGenderButtonColor(FrameMembershipRegistration.GENDER_MS);
			//hide keyboard
			AppGlobal.g_oTerm.get().hideKeyboard();
			
			bMatchChild = true;
		}else if (iChildId == m_oButtonReset.getId()){
			clearAllTextbox();
			AppGlobal.g_oTerm.get().hideKeyboard();
			
			bMatchChild = true;
		}else if (iChildId == m_oButtonReInput.getId()){
			m_oTxtBoxMemberNumber.setValue("");
			if(m_sInterfaceKey.equals(InfVendor.KEY_GOLDEN_CIRCLE) && 
					AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
				resetCodeReader();
			AppGlobal.g_oTerm.get().hideKeyboard();
			
			bMatchChild = true;
		}
		else if (m_oButtonPrint != null && iChildId == m_oButtonPrint.getId()){
			for (FrameMembershipRegistrationListener listener : listeners) {
				// Raise the event to parent
				listener.FrameMembershipRegistration_clickPrint(m_oCurrentMemberInfo);
			}
			AppGlobal.g_oTerm.get().hideKeyboard();
			
			bMatchChild = true;
		}
		else if(m_oButtonAttachToTable != null && iChildId == m_oButtonAttachToTable.getId()) {
			for (FrameMembershipRegistrationListener listener : listeners) {
				// Raise the event to parent
				listener.FrameMembershipRegistration_clickAttachToTable();
			}
			
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
	
	
	@Override
	public boolean valueChanged(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (m_oCodeReader != null && iChildId == m_oCodeReader.getId()) {
			for (FrameMembershipRegistrationListener listener : listeners) {
				m_oInputType = FrameMembershipRegistration.INPUT_SCAN_BARCODE;
				// Raise the event to parent
				listener.FrameMembershipRegistration_valueChanged(m_oCodeReader.getValue());
			}
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
}
