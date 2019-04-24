package app;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.TreeMap;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.FuncMembershipInterface.MemberInterfaceResponseInfo;
import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import commonui.FormInputBox;
import commonui.FormProcessBox;
import commonui.FormSelectionBox;
import core.Controller;
import externallib.StringLib;
import om.InfInterface;
import om.InfVendor;
import om.MenuPriceLevelList;
import om.PosActionLog;
import om.PosActionPrintQueue;
import om.PosBusinessDay;
import om.PosCheck;
import om.PosCheckDiscount;
import om.PosCheckExtraInfo;
import om.PosCheckExtraInfoList;
import om.PosCheckItem;
import om.PosCheckPayment;
import om.PosDiscountType;
import om.PosDiscountTypeList;
import om.PosInterfaceConfig;
import om.PosPaymentMethod;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

interface FormMembershipInterfaceListener{
	String formMembershipInterface_addItem(String sItemCode, BigDecimal dItemPrice, ArrayList <PosCheckExtraInfo> oPosCheckExtraInfoList);
	String formMembershipInterface_addDefaultPayment(FuncMembershipInterface.MemberInterfaceVoucherListInfo oVoucherInfo, String sInterfaceId);
	void formMembershipInterface_updatedBasket();
	void formMembershipInterface_updateOrderingBasket(List<HashMap<String, Integer>> oNeedDelectItems, List<FuncCheckItem> oDeletedFuncCheckItemList);
}

public class FormMembershipInterface extends VirtualUIForm implements FrameMembershipInterfaceListener{
	TemplateBuilder m_oTemplateBuilder;
	
	FrameMembershipInterface m_oFrameMembershipInterface;
	FormProcessBox m_oFormProcessBox;
	
	FuncMembershipInterface m_oFuncMembershipInterface;
	PosInterfaceConfig m_oPosInterfaceConfig;
	JSONObject m_oInterfaceSetup;
	FuncCheck m_oFuncCheck;
	FuncPayment m_oFuncPayment;
	boolean m_bIsActionAbort;
	boolean m_bNeedUpdateBasket;
	String m_sEnquiryAccNumber;
	String m_sEnquiryAccName;
	String m_sSelectedVoucher;
	String m_sSelectedInterfaceID;
	String m_sSelectedPriceLevel;
	String m_sSelectedAccountNumber;
	String m_sSelectedMemberNumber;
	String m_sSelectedMemberFirstName;
	String m_sSelectedMemberLastName;
	String m_sSelectedExpiryDate;
	String m_sSelectPointBalance;
	MenuPriceLevelList m_oMenuPriceLevelList;
	HashMap<String, Integer> m_oRemovedItemHashMap;
	HashMap<String, String> m_oRedemptionInfoList;
	
	int m_iPriceLevel;
	boolean m_bIsAttachMember;
	boolean m_bIsClearMember;
	boolean m_bIsCancelVoucherRedemption;
	String m_sCurrentOperation;
	String m_sFunctionName;
	String m_sLastErrorMessage;
	
	String m_sSendPacketString;
	boolean m_bProcessSuccess;
	boolean m_bProcessingEvent;
	String m_sOperatingFunction;
	HashMap<String, String> m_oMemberDetail;
	
	boolean m_bForwardFromRegistration;
	boolean m_bIsSuccessfullySetMember; // to identify whether have successfully set member
	
	final static String BACKGROUND_COLOUR_SELECTED = "#FFFF00";

	/** list of interested listeners (observers, same thing) */
	ArrayList<FormMembershipInterfaceListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FormMembershipInterfaceListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FormMembershipInterfaceListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FormMembershipInterface(FuncCheck oFuncCheck, FuncPayment oFuncPayment, String sTitle, Controller oParentController) {
		super(oParentController);
		
		m_oFuncCheck = oFuncCheck;
		m_oFuncPayment = oFuncPayment;
		m_bIsCancelVoucherRedemption = false;
		m_bIsActionAbort = false;
		m_bNeedUpdateBasket = false;
		m_sEnquiryAccNumber = "";
		m_sEnquiryAccName = "";
		m_sSelectedVoucher = "";
		m_sSelectedInterfaceID = "";
		m_sSelectedPriceLevel = "";
		m_sSelectedAccountNumber = "";
		m_sSelectedMemberFirstName = "";
		m_sSelectedMemberLastName = "";
		m_sSelectedExpiryDate = "";
		m_sSelectedMemberNumber = "";
		m_sSelectPointBalance = "";
		m_sLastErrorMessage = "";
		m_oRemovedItemHashMap = new HashMap<String, Integer>();
		m_oRedemptionInfoList = new HashMap<String, String>();
		m_oMemberDetail = new HashMap<String, String>();
		m_oFormProcessBox = new FormProcessBox(this);
		
		m_iPriceLevel = -1;
		m_bIsAttachMember = false;
		m_bIsClearMember = false;
		m_sFunctionName = sTitle;
		
		m_sSendPacketString = "";
		m_bProcessSuccess = false;
		m_bProcessingEvent = false;
		m_sCurrentOperation = FrameMembershipInterface.OPERATION_ENQUIRY;
		
		m_sOperatingFunction = "";
		
		m_bForwardFromRegistration = false;
		m_bIsSuccessfullySetMember = false;

		m_oTemplateBuilder = new TemplateBuilder();
		m_oTemplateBuilder.loadTemplate("frmMembershipInterface.xml");

		listeners = new ArrayList<FormMembershipInterfaceListener>();
	}
	
	public void init(PosInterfaceConfig oPosInterfaceConfig, String sDefaultValue, String sOperation, MenuPriceLevelList oMenuPriceLevelList, boolean bShowInFloorPlan, String sTableNo, boolean bHidePrintButton) {
		m_sCurrentOperation = sOperation;
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameMembershipInterface = new FrameMembershipInterface();
		m_oFrameMembershipInterface.setForwardFromRegistration(m_bForwardFromRegistration);
		m_oFrameMembershipInterface.init(oPosInterfaceConfig, sDefaultValue, sOperation, m_sFunctionName, bShowInFloorPlan, sTableNo, bHidePrintButton);
		m_oFrameMembershipInterface.addListener(this);
		
		if(m_sCurrentOperation.equals(FrameMembershipInterface.OPERATION_VOUCHER_REDEMPTION) && oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_CHINETEK))
			m_oTemplateBuilder.buildFrame(m_oFrameMembershipInterface, "fraChinetekMembershipInterface");
		else
			m_oTemplateBuilder.buildFrame(m_oFrameMembershipInterface, "fraMembershipInterface");
		
		m_oMenuPriceLevelList = oMenuPriceLevelList;
		
		this.attachChild(m_oFrameMembershipInterface);
	}
	
	public void initForProcess(PosInterfaceConfig oPosInterfaceConfig){
		m_oFrameMembershipInterface = new FrameMembershipInterface();
		m_oFrameMembershipInterface.addListener(this);
		
		m_oFuncMembershipInterface = new FuncMembershipInterface(oPosInterfaceConfig);
		
		this.attachChild(m_oFrameMembershipInterface);
		m_bProcessingEvent = true;
	}
	
	public boolean isProcessSuccess() {
		return m_bProcessSuccess;
	}

	public void svcEnquiry(MenuPriceLevelList oMenuPriceLevelList, PosInterfaceConfig oInterfaceConfig, String sMemberNumber, String sLastName, boolean bShowInFloorPlan, String sTableNo, boolean bAutoFunction, boolean bHidePrintButton) {
		boolean bMemberAttached = false, bSkipAction = false;
		String sDefaultNumber = "", sDefaultName = "";
		
		if (oInterfaceConfig != null){
			// for gc registration showing information by enquire one more
			m_oPosInterfaceConfig = oInterfaceConfig;
			
			if(!m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2) || m_bForwardFromRegistration) {
				// for registration showing information by enquire one more
				bMemberAttached = true;
				bSkipAction = true;
			}
		}
		
		if(!bShowInFloorPlan) {
			// check whether have other membership id attached
			String sInterfaceId = "";
			if(m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0))
				sInterfaceId = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID);
			
			if(sInterfaceId != null && !sInterfaceId.isEmpty()){
				if(!m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID).equals(Integer.toString(m_oPosInterfaceConfig.getInterfaceId()))){
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("svc_enquiry"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("have_different_interface_member")
							+ System.lineSeparator() + AppGlobal.g_oLang.get()._("please_clear_current_member_first"));
					oFormDialogBox.show();
					m_bIsActionAbort = true;
					return;
				}
			}
			
			// pass default value
			if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_HUARUNTONG)){
				if (m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER) != null &&
						!m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER).isEmpty())
					sDefaultNumber = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER);
			}
			
			// Only allow gc for mobile mode
			if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
				return;
		}
		
		init(m_oPosInterfaceConfig, sDefaultNumber, FrameMembershipInterface.OPERATION_ENQUIRY, oMenuPriceLevelList, bShowInFloorPlan, sTableNo, bHidePrintButton);
		m_oInterfaceSetup = m_oPosInterfaceConfig.getInterfaceConfig();
		m_oFuncMembershipInterface = new FuncMembershipInterface(m_oPosInterfaceConfig);
		
		if (bAutoFunction) {
			HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();
			oEnquiryInfo.put("memberNumber", sMemberNumber);
			oEnquiryInfo.put("surname", sLastName);
			frameMembershipInterfaceResult_clickEnquiry(oEnquiryInfo);
			frameMembershipInterfaceResult_clickSetMember(sMemberNumber);
			return;
		}
		
		if (!bShowInFloorPlan) {
			//check whether having existing yazuo or concept or vienna member attached
			if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_HUARUNTONG)) {
				if (m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0) &&
						m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER) != null &&
						!m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER).isEmpty())
					bMemberAttached = true;
			} else {
				//check whether having existing lps-svc member attached
				if (m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER, 0) &&
						m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER) != null &&
						!m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER).isEmpty())
					bMemberAttached = true;
			}
		}
		
		if (bMemberAttached && m_oFuncPayment == null) {
			int iSelectedAction = 0;
			
			if (!bSkipAction) {
				ArrayList<String> oOptionList = new ArrayList<String>();
				oOptionList.add(AppGlobal.g_oLang.get()._("member_enquiry"));
				oOptionList.add(AppGlobal.g_oLang.get()._("clear_member"));
				oOptionList.add(AppGlobal.g_oLang.get()._("cancel"));
				
				FormSelectionBox oFormSelectionBox = new FormSelectionBox(this);
				oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("please_select_the_action_type"), oOptionList, false);
				oFormSelectionBox.show();
				
				if (oFormSelectionBox.isUserCancel()) {
					m_bIsActionAbort = true;
					return;
				}
				
				iSelectedAction = oFormSelectionBox.getResultList().get(0);
			} else {
				iSelectedAction = 0;
				sDefaultNumber = sMemberNumber;
				sDefaultName = sLastName;
				
			}
			
			if (iSelectedAction == 2) {
				//user cancel action
				m_bIsActionAbort = true;
				return;
			} else if (iSelectedAction == 1) {
				//user clear member
				clearMember();
				m_bIsActionAbort = true;
			} else {
				// Default sent enquiry
				if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_HUARUNTONG)) {
					HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();
					oEnquiryInfo.put("memberNumber", sDefaultNumber);
					
					frameMembershipInterfaceResult_clickEnquiry(oEnquiryInfo);
				}
			}
		}
	}
	
	public HashMap<String, String> getLastMemberInfo(){
		return m_oFuncMembershipInterface.getLastMemberInfo();
	}
	
	public void clearMember() {
		String sVendorKey = m_oPosInterfaceConfig.getInterfaceVendorKey();
		
		ArrayList<HashMap<String, String>> oCheckExtraInfos = new ArrayList<HashMap<String, String>>();
		if (sVendorKey.equals(InfVendor.KEY_HUARUNTONG)) {
			if (!m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0) ||
					m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER).isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("clear_member"));
				oFormDialogBox.setMessage("no_member_is_attached");
				oFormDialogBox.show();
				return;
			}
		} else if (!m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER, 0) ||
				m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER).isEmpty()) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("clear_member"));
			oFormDialogBox.setMessage("no_member_is_attached");
			oFormDialogBox.show();
			return;
		}
		
		boolean bShowClearConfirmation = true;
		String sAccountName = "", sAccountNumber = "", sAccountLastName = "";
		if (m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NAME, 0)
				&& m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NAME) != null)
			sAccountName = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NAME);
		
		if (sVendorKey.equals(InfVendor.KEY_HUARUNTONG)) {
			sAccountNumber = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER);
			sAccountName = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NAME);
		}
		
		// Ask clear member confirmation
		if (bShowClearConfirmation) {
			String sMessage = "", sAccountNameForDisplay = "";
			sAccountNameForDisplay = sAccountName;
			sMessage = AppGlobal.g_oLang.get()._("clear_current_member") + ": " + System.lineSeparator()
					+ AppGlobal.g_oLang.get()._("account_number") + ": " + sAccountNumber + System.lineSeparator()
					+ AppGlobal.g_oLang.get()._("account_name") + ": " + sAccountNameForDisplay + System.lineSeparator()
					+ AppGlobal.g_oLang.get()._("confirm_to_clear_member") + "?";
			
			FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
			oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("clear_member"));
			oFormConfirmBox.setMessage(sMessage);
			oFormConfirmBox.show();
			if (oFormConfirmBox.isOKClicked() == false)
				return;
		}
		
		//really clear member information
		HashMap<String, String> oInterfaceIdInfos = new HashMap<String, String>();
		oInterfaceIdInfos.put("variable", PosCheckExtraInfo.VARIABLE_INTERFACE_ID);
		oInterfaceIdInfos.put("index", "0");
		oInterfaceIdInfos.put("value", "");
		oCheckExtraInfos.add(oInterfaceIdInfos);
		
		HashMap<String, String> oMemberNameInfos = new HashMap<String, String>();
		oMemberNameInfos.put("variable", PosCheckExtraInfo.VARIABLE_ACCOUNT_NAME);
		oMemberNameInfos.put("index", "0");
		oMemberNameInfos.put("value", "");
		oCheckExtraInfos.add(oMemberNameInfos);
		
		
		if (sVendorKey.equals(InfVendor.KEY_HUARUNTONG)) {
			oCheckExtraInfos = this.addCheckExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, "0", "");
			oCheckExtraInfos = this.addCheckExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_POINTS_BALANCE, "0", "");
			oCheckExtraInfos = this.addCheckExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_MEMBER_TYPE, "0", "");
			oCheckExtraInfos = this.addCheckExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, "0", "");
			oCheckExtraInfos = this.addCheckExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_MEMBER_NAME, "0", "");
		}
		m_bIsClearMember = true;
		
		m_oFuncCheck.saveMembershipInterfaceExtraInfo(true, oCheckExtraInfos);
	}
	
	public boolean isActionAbort() {
		return m_bIsActionAbort;
	}
	
	public boolean isClearMember() {
		return m_bIsClearMember;
	}
	
	public void setPosInterfaceConfig(PosInterfaceConfig oPosInterfaceConfig) {
		m_oPosInterfaceConfig = oPosInterfaceConfig;
	}
	
	PosCheckExtraInfo constructCheckExtraInfo(String sBy, String sSection, String sVariable, String sValue) {
		PosCheckExtraInfo oPosCheckExtraInfo = new PosCheckExtraInfo();
		oPosCheckExtraInfo.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oPosCheckExtraInfo.setBy(sBy);
		oPosCheckExtraInfo.setSection(sSection);
		oPosCheckExtraInfo.setVariable(sVariable);
		oPosCheckExtraInfo.setValue(sValue);
		return oPosCheckExtraInfo;
	}
	
	void showDialogBox(String sTitle, String sMessage) {
		FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
		oFormDialogBox.setTitle(sTitle);
		oFormDialogBox.setMessage(sMessage);
		oFormDialogBox.show();
	}
	
	boolean getUserConfirm(String sTitle, String sMessage) {
		FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
		oFormConfirmBox.setTitle(sTitle);
		oFormConfirmBox.setMessage(sMessage);
		oFormConfirmBox.show();
		if(oFormConfirmBox.isOKClicked() == false)
			return false;
		return true;
	}

	/***********************/
	/*	Override Function  */
	/***********************/
	@Override
	public void frameMembershipInterfaceResult_clickBack() {
		m_bIsActionAbort = true;
		m_bIsAttachMember = true;
		m_bIsCancelVoucherRedemption = true;
		m_iPriceLevel = -1;
		this.finishShow();
	}
	
	@Override
	public void frameMembershipInterfaceResult_clickEnquiry(HashMap<String, String> oEnquiryInfo) {
		if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_HUARUNTONG)) {
			//Generate the enquiry information
			if (oEnquiryInfo.get("memberNumber").isEmpty() && oEnquiryInfo.get("mobileNumber").isEmpty() && oEnquiryInfo.get("cardNumber").isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("member_enquiry"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("please_fill_in_any_member_info"));
				oFormDialogBox.show();
				return;
			}
		} else {
			//Generate the enquiry information
			if(oEnquiryInfo.get("customerNumber").isEmpty() && oEnquiryInfo.get("cardNumber").isEmpty() &&
					oEnquiryInfo.get("nric").isEmpty() && oEnquiryInfo.get("name").isEmpty() &&
					oEnquiryInfo.get("mobileNumber").isEmpty() && oEnquiryInfo.get("email").isEmpty() &&
					oEnquiryInfo.get("enquiryNumber").isEmpty()){
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("member_enquiry"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("please_fill_in_any_member_info"));
				oFormDialogBox.show();
				return;
			}
		}
		
		if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_HUARUNTONG)) {
			m_oFrameMembershipInterface.cleanupMemberDetail();
			m_oFrameMembershipInterface.clearMemberListAndSetMemberButtonInvisible();
			
			// member enquiry
			if (!m_oFuncMembershipInterface.memberEnquiry(oEnquiryInfo)) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(m_oFuncMembershipInterface.getLastErrorMessage());
				oFormDialogBox.show();
				return;
			}
			
			if (m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberNumber.isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("no_such_member"));
				oFormDialogBox.setMessage(m_oFuncMembershipInterface.getLastErrorMessage());
				oFormDialogBox.show();
				return;
			}
			
			// member details
			HashMap<String, String> oMemberDetails = new HashMap<String, String>();
			oMemberDetails.put("memberNumber", m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberNumber);
			oMemberDetails.put("memberName", m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberName);
			oMemberDetails.put("cardNumber", m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sCardNumber);
			oMemberDetails.put("memberTier", m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sTier);
			oMemberDetails.put("memberStatus", m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberStatus);
			oMemberDetails.put("pointsBalance", m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sPointsBalance);
			
			m_oFrameMembershipInterface.updateMemberDetail_HuaRunTong(oMemberDetails);
		}
		m_oFrameMembershipInterface.cleanupAllTextBox();
	}
	
	public int getPriceLevel() {
		return m_iPriceLevel;
	}
	
	public boolean getIsAttachMember() {
		return m_bIsAttachMember;
	}
	
	public String getLastErrorMessage(){
		return m_sLastErrorMessage;
	}
	
	//get InterfaceConfig

	public PosInterfaceConfig getInterfaceConfig(String sInterfaceType, List<String> oVendors, String sTitle){
		List<PosInterfaceConfig> oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(sInterfaceType);
		List<PosInterfaceConfig> oPosInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		PosInterfaceConfig oTargetPosInterfaceConfig = null;
		if(!AppGlobal.isModuleSupport(AppGlobal.OPTIONAL_MODULE.pos_interface.name())){
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("interface_module_is_not_supported"));
			oFormDialogBox.show();
			return null;
		}
		if(oInterfaceConfigList.isEmpty()){
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("enquiry"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("no_membership_interface_setup"));
			oFormDialogBox.show();
			return null;
		}
		for(PosInterfaceConfig oPosInterfaceConfig : oInterfaceConfigList) {
			for(String sVendor : oVendors){
				if(oPosInterfaceConfig.getInterfaceVendorKey().equals(sVendor))
					oPosInterfaceConfigList.add(oPosInterfaceConfig);
			}
		}
		
		if(oPosInterfaceConfigList.isEmpty()){
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("enquiry"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("no_membership_interface_setup"));
			oFormDialogBox.show();
			return null;
		}
		
		if(oPosInterfaceConfigList.size() == 1)
			oTargetPosInterfaceConfig = oPosInterfaceConfigList.get(0);
		else {
			ArrayList<String> oOptionList = new ArrayList<String>();
			HashMap<Integer, HashMap<String, String>> oInterfaceIdList = new HashMap<Integer, HashMap<String, String>>();
			int iInterfaceCount = 0;
			
			for(PosInterfaceConfig oInterfaceConfig : oPosInterfaceConfigList) {
				oOptionList.add(oInterfaceConfig.getInterfaceName(AppGlobal.g_oCurrentLangIndex.get()));
				HashMap<String, String> oTempInterfaceInfo = new HashMap<String, String>();
				oTempInterfaceInfo.put("interfaceId", String.valueOf(oInterfaceConfig.getInterfaceId()));
				oInterfaceIdList.put(iInterfaceCount, oTempInterfaceInfo);
				iInterfaceCount++;
			}
			
			FormSelectionBox oFormSelectionBox = new FormSelectionBox(this);
			oFormSelectionBox.initWithSingleSelection(sTitle, oOptionList, false);
			oFormSelectionBox.show();
			
			if(oFormSelectionBox.isUserCancel())
				return null;
			else {
				HashMap<String, String> oTempInterfaceInfo = oInterfaceIdList.get(oFormSelectionBox.getResultList().get(0));
				int iInterfaceId = Integer.valueOf(oTempInterfaceInfo.get("interfaceId"));
				for(PosInterfaceConfig oInterfaceConfig : oPosInterfaceConfigList){
					if(oInterfaceConfig.getInterfaceId() == iInterfaceId)
						oTargetPosInterfaceConfig = oInterfaceConfig;
				}
			}
		}
		return oTargetPosInterfaceConfig;
	}
	
	// Get the value before MSR
	private String getValueAfterMsr(String sValue, String sGetType){
		List<PosInterfaceConfig> oInterfaceConfigWithMsrList = new ArrayList<PosInterfaceConfig>();
		// Get the configure from interface module
		oInterfaceConfigWithMsrList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_MEMBERSHIP_INTERFACE);
		JSONObject oInterfaceConfigWithMsr = null;
		String sMsrCode = "";
		String sReturnValue = sValue;
		
		if(sValue.isEmpty())
			return "";
		
		for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigWithMsrList) {
			if (oPosInterfaceConfig.getInterfaceId() == m_oPosInterfaceConfig.getInterfaceId()) {
				oInterfaceConfigWithMsr = oPosInterfaceConfig.getInterfaceConfig();
				if (oInterfaceConfigWithMsr.has("general_setup") &&
						oInterfaceConfigWithMsr.optJSONObject("general_setup").has("params") &&
						oInterfaceConfigWithMsr.optJSONObject("general_setup").optJSONObject("params").has("msr_code")){
					sMsrCode = oInterfaceConfigWithMsr.optJSONObject("general_setup").optJSONObject("params").optJSONObject("msr_code").optString("value");
					break;
				}
			}
		}
		
		if(sMsrCode.isEmpty())
			return sValue;
		
		List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		// Get the configure from interface module
		oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PERIPHERAL_DEVICE);
		for(PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList) {
			if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_MSR) && oPosInterfaceConfig.getInterfaceCode().equals(sMsrCode)) {
				sReturnValue = "";
				// Capture information from swipe card
				FuncMSR oFuncMSR = new FuncMSR();
				if(oFuncMSR.processCardContent(sValue, oPosInterfaceConfig.getInterfaceConfig()) == FuncMSR.ERROR_CODE_NO_ERROR){
					// Get the necessary value
					String sErrorMessage = "";
					if(sGetType.equals(FuncMSR.FIELD_LIST.card_no.name())){
						sReturnValue = oFuncMSR.getCardNo();
						sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_capture_card");
					} else if (sGetType.equals(FuncMSR.FIELD_LIST.card_holder_name.name())){
						sReturnValue = oFuncMSR.getName();
						sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_capture_card_holder");
					} else if (sGetType.equals(FuncMSR.FIELD_LIST.expiry_date.name())){
						sReturnValue = oFuncMSR.getExpiryDateString();
						sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_capture_card_expiry_date");
					} else if (sGetType.equals(FuncMSR.FIELD_LIST.custom_field1.name())){
						sReturnValue = oFuncMSR.getCustomField1Value();
						sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_capture_card_custom_field") + "1";
					} else if (sGetType.equals(FuncMSR.FIELD_LIST.custom_field2.name())){
						sReturnValue = oFuncMSR.getCustomField2Value();
						sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_capture_card_custom_field") + "2";
					} else {
						sReturnValue = "";
						sErrorMessage = AppGlobal.g_oLang.get()._("setup_format_error");
					}
					
					if (sReturnValue.isEmpty()) {
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(sErrorMessage);
						oFormDialogBox.show();
						oFormDialogBox = null;
						return "";
					}
					break;
				} else {
					// Fail to process swipe card
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(oFuncMSR.getLastErrorMessage());
					oFormDialogBox.show();
					oFormDialogBox = null;
					return "";
				}
			}
		}
		return sReturnValue;
	}

	public boolean checkVoucherRestriction(String sVoucherType, FuncPayment oFuncPayment){
		//check applied voucher type
		boolean bHasCash = false,bHasDDiscount = false, bHasBDiscount = false, bHasIDiscount = false;
		if(sVoucherType.equals("cash") && oFuncPayment != null){
			for(PosCheckPayment oPayment : oFuncPayment.getCheckPaymentList()){
				PosPaymentMethod oPaymentMethod = oFuncPayment.getPaymentMethodList().getPaymentMethod(oPayment.getPaymentMethodId());
				if(oPaymentMethod.isCashVoucherPaymentType()){
					if(oPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE, 0) != null){
						String voucherType = oPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE, 0).getValue();
						if(!voucherType.isEmpty())
							bHasCash = true;
					}
				}
			}
		}
		
		//read voucher from database
		PosCheckExtraInfoList oPosCheckExtraInfoList = new PosCheckExtraInfoList();
		oPosCheckExtraInfoList.readAllByCheckId(m_oFuncCheck.getCheckId());
		for(PosCheckExtraInfo oPosCheckExtraInfo : oPosCheckExtraInfoList.getCheckExtraInfoList()){
			if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE)){
				if(oPosCheckExtraInfo.getValue().compareToIgnoreCase("cash") == 0)
					bHasCash = true;
				else if(oPosCheckExtraInfo.getValue().compareToIgnoreCase("ddiscount") == 0)
					bHasDDiscount = true;
				else if(oPosCheckExtraInfo.getValue().compareToIgnoreCase("bddiscount") == 0)
					bHasBDiscount = true;
				else if(oPosCheckExtraInfo.getValue().compareToIgnoreCase("iddiscount") == 0)
					bHasIDiscount = true;
			}
		}
		
		if(!bHasDDiscount && !bHasBDiscount && !bHasCash && !bHasIDiscount)
			return true;
		
		if(AppGlobal.g_oFuncStation.get().getApplyDiscountRestriction() == 3){
			if (sVoucherType.compareToIgnoreCase("ddiscount") == 0) {
				if(bHasDDiscount == true){
					m_sLastErrorMessage = "ddiscount_is_applied";
					return false;
				}
			}
		}
		
		String sSecondRestriction = "n,n,n,n";
		if(sVoucherType.compareToIgnoreCase("cash") == 0)
			sSecondRestriction = m_oInterfaceSetup.optJSONObject("voucher_setup").optJSONObject("params").optJSONObject("second_cash_restriction").optString("value");
		else if(sVoucherType.compareToIgnoreCase("ddiscount") == 0)
			sSecondRestriction = m_oInterfaceSetup.optJSONObject("voucher_setup").optJSONObject("params").optJSONObject("second_ddiscount_restriction").optString("value");
		else if(sVoucherType.compareToIgnoreCase("bddiscount") == 0)
			sSecondRestriction = m_oInterfaceSetup.optJSONObject("voucher_setup").optJSONObject("params").optJSONObject("second_bdiscount_restriction").optString("value");
		else if(sVoucherType.compareToIgnoreCase("iddiscount") == 0)
			sSecondRestriction = m_oInterfaceSetup.optJSONObject("voucher_setup").optJSONObject("params").optJSONObject("second_iddiscount_restriction").optString("value");
			
		if(!sSecondRestriction.isEmpty()){
			String [] sRules = sSecondRestriction.split(",");
			if(sRules[0].equals("y")){
				if(bHasDDiscount == true)
					return true;
			}
			if(sRules[1].equals("y")){
				if(bHasBDiscount == true)
					return true;
			}
			if(sRules[2].equals("y")){
				if(bHasCash == true)
					return true;
			}
			if(sRules[3].equals("y")){
				if(bHasIDiscount == true)
					return true;
			}
		}
		
		if(bHasDDiscount == true)
			m_sLastErrorMessage = "ddiscount_voucher_is_applied";
		else if(bHasBDiscount == true)
			m_sLastErrorMessage = "bddiscount_voucher_is_applied";
		else if(bHasCash == true)
			m_sLastErrorMessage = "cash_voucher_is_applied";
		else if(bHasIDiscount == true)
			m_sLastErrorMessage = "iddiscount_is_applied";
		
		return false;
	}

	public void setForwardFromRegistration(boolean bForwardFromRegistration) {
		m_bForwardFromRegistration = bForwardFromRegistration;
	}
	
	//update the whole voucher list (for init user)
	public void updateVoucherListCommonBasket() {
		//original voucher list
		ArrayList <FuncMembershipInterface.MemberInterfaceVoucherListInfo> oVoucherInfoList = m_oFrameMembershipInterface.getVoucherListInfo();
		BigDecimal dPointUse = BigDecimal.ZERO;
		
		//update the item gift/ coupon quantity
		for (List<FuncCheckItem> oFuncCheckItems : m_oFuncCheck.getWholeItemList()) {
			for (FuncCheckItem oFuncCheckItem : oFuncCheckItems) {
				String sVoucherID = oFuncCheckItem.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_ID);
				if(sVoucherID == null || sVoucherID.isEmpty())
					continue;
				
				boolean bCoupon = false;
				//get the type (coupon or gift)
				String sVoucherType = oFuncCheckItem.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE);
				if (sVoucherType != null && sVoucherType.equals(FuncMembershipInterface.MEMBERSHIP_VOUCHER_TYPE_COUPON))
					bCoupon = true;
				
				//update the item quantity in voucher list
				for (FuncMembershipInterface.MemberInterfaceVoucherListInfo oVoucherInfo : oVoucherInfoList) {
					if (oVoucherInfo.sId.equals(sVoucherID) && oVoucherInfo.bCoupon == bCoupon) {
						oVoucherInfo.dCount = oVoucherInfo.dCount.subtract(BigDecimal.ONE);
						// count the total used bonus
						if (!oVoucherInfo.bCoupon)
							dPointUse = dPointUse.add(oVoucherInfo.dBonus);
						break;
					}
				}
			}
		}
		//update the cash gift/coupon quantity
		int iIndex = 1;
		while(m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_DEFAULT_PAYMENT, PosCheckExtraInfo.VARIABLE_DEFAULT_PAYMENT_DETAIL, iIndex)) {
			try {
				String sExtraInfo = m_oFuncCheck.getCheckExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_DEFAULT_PAYMENT, PosCheckExtraInfo.VARIABLE_DEFAULT_PAYMENT_DETAIL, iIndex);
				JSONObject oExtraInfo = new JSONObject(sExtraInfo);
				String sVoucherID;
				boolean bCoupon = false;
				if(oExtraInfo.has(PosCheckExtraInfo.VARIABLE_VOUCHER_ID) && oExtraInfo.has(PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE)) {
					//get the discount voucher id
					sVoucherID = oExtraInfo.optString(PosCheckExtraInfo.VARIABLE_VOUCHER_ID);
					//get the type (gift/ coupon)
					if(oExtraInfo.optString(PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE).equals(FuncMembershipInterface.MEMBERSHIP_VOUCHER_TYPE_COUPON))
						bCoupon = true;
					//update voucher list
					for(FuncMembershipInterface.MemberInterfaceVoucherListInfo oVoucherInfo : oVoucherInfoList) {
						if(oVoucherInfo.sId.equals(sVoucherID) && oVoucherInfo.bCoupon == bCoupon) {
							oVoucherInfo.dCount = oVoucherInfo.dCount.subtract(BigDecimal.ONE);
							//count the total used bonus
							if(!oVoucherInfo.bCoupon)
								dPointUse = dPointUse.add(oVoucherInfo.dBonus);
							break;
						}
					}
				}
			}
			catch(Exception e) {
				AppGlobal.stack2Log(e);
				break;
			}
			iIndex++;
		}
		
		//update the discount gift/coupon quantity
		for (PosCheckDiscount oPosCheckDiscount : m_oFuncCheck.getCheckDiscountList()) {
			
			//get the id from extra info
			String sVoucherID = oPosCheckDiscount.getCheckExtraInfoValueBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_ID);
			
			if(sVoucherID == null || sVoucherID.isEmpty())
				continue;
			
			//get the type (coupon or gift)
			boolean bCoupon = false;
			//get the type (coupon or gift)
			if(oPosCheckDiscount.getCheckExtraInfoValueBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE) != null)
				if(oPosCheckDiscount.getCheckExtraInfoValueBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE,
						PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE).equals(FuncMembershipInterface.MEMBERSHIP_VOUCHER_TYPE_COUPON))
				bCoupon = true;
			
			//update the item quantity in voucher list
			for(FuncMembershipInterface.MemberInterfaceVoucherListInfo oVoucherInfo : oVoucherInfoList) {
				if(oVoucherInfo.sId.equals(sVoucherID) && oVoucherInfo.bCoupon == bCoupon) {
					oVoucherInfo.dCount = oVoucherInfo.dCount.subtract(BigDecimal.ONE);
					//count the total used bonus
					if(!oVoucherInfo.bCoupon)
						dPointUse = dPointUse.add(oVoucherInfo.dBonus);
					
					break;
				}
			}
		}
		
		//update member bonus
		//get the original bonus
		BigDecimal dPoint = new BigDecimal(m_oFuncMembershipInterface.getLastMemberInfo().get("pointsBalance"));

		//store the updated remaining balance in the extra info
		if(m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_POSTED_POINTS_USE, 0))
			m_oFuncCheck.updateCheckExtraInfoValue(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_POSTED_POINTS_USE, 0, dPointUse.toPlainString());
		else
			m_oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_POSTED_POINTS_USE, 0, dPointUse.toPlainString());
		
		//return oVoucherInfoList;
	}
	
	@Override
	public void frameMembershipInterfaceResult_clickSetMember(String sMemberNo) {
		ArrayList<HashMap<String, String>> oCheckExtraInfos = new ArrayList<HashMap<String, String>>();
		
		if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_HUARUNTONG)) {
			m_sEnquiryAccNumber = m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberNumber;
			
			// Ask confirmation to reset member
			String sCurrentMemberNumber = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER);
			if (sCurrentMemberNumber != null && !sCurrentMemberNumber.isEmpty()
					&& !sCurrentMemberNumber.equals(m_sEnquiryAccNumber)) {
				String sMessage = AppGlobal.g_oLang.get()._("current_member") + ": " + sCurrentMemberNumber + System.lineSeparator();
				sMessage = sMessage.concat(AppGlobal.g_oLang.get()._("new_member") + ": " + m_sEnquiryAccNumber + System.lineSeparator());
				sMessage = sMessage.concat(AppGlobal.g_oLang.get()._("replace_member") + "?");
				
				FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
				oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("set_member"));
				oFormConfirmBox.setMessage(sMessage);
				oFormConfirmBox.show();
				if (oFormConfirmBox.isOKClicked() == false)
					return;
			}
			oCheckExtraInfos = this.addCheckExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, "0", Integer.toString(m_oPosInterfaceConfig.getInterfaceId()));
			oCheckExtraInfos = this.addCheckExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, "0", m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberNumber);
			oCheckExtraInfos = this.addCheckExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_MEMBER_NAME, "0", m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberName);
			oCheckExtraInfos = this.addCheckExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_CARD_NO, "0", m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sCardNumber);
			oCheckExtraInfos = this.addCheckExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_MEMBER_TYPE, "0", m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sTier);
			oCheckExtraInfos = this.addCheckExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_POINTS_BALANCE, "0", m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sPointsBalance);
			
			m_oFuncCheck.saveMembershipInterfaceExtraInfo(true, oCheckExtraInfos);
		}
		m_bIsSuccessfullySetMember = true;
		finishShow();
	}
	
	public ArrayList<PosCheckExtraInfo> addExtraInfoToList(ArrayList<PosCheckExtraInfo> oDiscountCheckExtraInfoList, String sBy, String sSection, String sVariable, String sValue, int iIndex) {
		PosCheckExtraInfo oCheckExtraInfo = new PosCheckExtraInfo();
		oCheckExtraInfo.setBy(sBy);
		oCheckExtraInfo.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oCheckExtraInfo.setCheckId(m_oFuncCheck.getCheckId());
		oCheckExtraInfo.setSection(sSection);
		oCheckExtraInfo.setVariable(sVariable);
		oCheckExtraInfo.setValue(sValue);
		oCheckExtraInfo.setIndex(iIndex);
		oDiscountCheckExtraInfoList.add(oCheckExtraInfo);
		
		return oDiscountCheckExtraInfoList;
	}
	
	public ArrayList<PosCheckExtraInfo> addDiscountCheckExtraInfoToList(ArrayList<PosCheckExtraInfo> oDiscountCheckExtraInfoList, String sSection, String sVariable, String sValue, int iIndex) {
		PosCheckExtraInfo oCheckExtraInfo = new PosCheckExtraInfo();
		oCheckExtraInfo.setBy(PosCheckExtraInfo.BY_DISCOUNT);
		oCheckExtraInfo.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oCheckExtraInfo.setCheckId(m_oFuncCheck.getCheckId());
		oCheckExtraInfo.setSection(sSection);
		oCheckExtraInfo.setVariable(sVariable);
		oCheckExtraInfo.setValue(sValue);
		oCheckExtraInfo.setIndex(iIndex);
		oDiscountCheckExtraInfoList.add(oCheckExtraInfo);
		
		return oDiscountCheckExtraInfoList;
	}
	
	public ArrayList<HashMap<String, String>> addCheckExtraInfoToList(ArrayList<HashMap<String, String>> oCheckExtraInfos, String sVariable, String sIndex, String sValue) {
		HashMap<String, String> oCheckExtraInfo = new HashMap<String, String>();
		oCheckExtraInfo.put("variable", sVariable);
		oCheckExtraInfo.put("index", sIndex);
		oCheckExtraInfo.put("value", sValue);
		oCheckExtraInfos.add(oCheckExtraInfo);
		
		return oCheckExtraInfos;
	}
	
	@Override
	public void frameMembershipInterfaceResult_clickApplyDisc() {
		String[] sDiscCode = new String[3];
		BigDecimal dFoodDiscValue = BigDecimal.ZERO;
		BigDecimal dBevDiscValue = BigDecimal.ZERO;
		BigDecimal dMiscDiscValue = BigDecimal.ZERO;
		BigDecimal dValue = null, dDiscountRateAmt = null;
		List<HashMap<String, Integer>> oSelectedItems = new ArrayList<HashMap<String, Integer>>();
		PosDiscountType oSelectedDiscType;
		
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		PosBusinessDay oBusinessDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay();
		PosDiscountTypeList oDiscTypeList = new PosDiscountTypeList();
		
		if(m_sEnquiryAccNumber.isEmpty()) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("apply_discount"));
			oFormDialogBox.setMessage((AppGlobal.g_oLang.get()._("please_do_enquiry_first")));
			oFormDialogBox.show();
			return;
		}
		
		sDiscCode[0] = "";
		sDiscCode[1] = "";
		sDiscCode[2] = "";
		if(!m_oInterfaceSetup.optJSONObject("disc_setting").optJSONObject("params").optJSONObject("apply_food_disc").optString("value").isEmpty())
			sDiscCode[0] = m_oInterfaceSetup.optJSONObject("disc_setting").optJSONObject("params").optJSONObject("apply_food_disc").optString("value");
		if(!m_oInterfaceSetup.optJSONObject("disc_setting").optJSONObject("params").optJSONObject("apply_beverage_disc").optString("value").isEmpty())
			sDiscCode[1] = m_oInterfaceSetup.optJSONObject("disc_setting").optJSONObject("params").optJSONObject("apply_beverage_disc").optString("value");
		if(!m_oInterfaceSetup.optJSONObject("disc_setting").optJSONObject("params").optJSONObject("apply_misc_disc").optString("value").isEmpty())
			sDiscCode[2] = m_oInterfaceSetup.optJSONObject("disc_setting").optJSONObject("params").optJSONObject("apply_misc_disc").optString("value");
		if(sDiscCode[0].isEmpty() && sDiscCode[1].isEmpty() && sDiscCode[2].isEmpty()) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("apply_discount"));
			oFormDialogBox.setMessage("no_discount_setup");
			oFormDialogBox.show();
			return;
		}
		
		if(!m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sFoodDisc.isEmpty())
			dFoodDiscValue = new BigDecimal(m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sFoodDisc);
		if(!m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sBevDisc.isEmpty())
			dBevDiscValue = new BigDecimal(m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sBevDisc);
		if(!m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMiscDisc.isEmpty())
			dMiscDiscValue = new BigDecimal(m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMiscDisc);
		if(dFoodDiscValue.compareTo(BigDecimal.ZERO) == 0 && dBevDiscValue.compareTo(BigDecimal.ZERO) == 0 && dMiscDiscValue.compareTo(BigDecimal.ZERO) == 0)
			return;
		
		// Get all item
		oSelectedItems = m_oFuncCheck.getSectionItemIndexForCurrentOrderedItem();
		if(oSelectedItems.size() <= 0)
			return;
		
		//form the lookup description
		oDiscTypeList.readDiscountListByOutletId("item", AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncStation.get().getStationId(), dateFormat.print(oBusinessDay.getDate()), oBusinessDay.isHoliday(), oBusinessDay.isDayBeforeHoliday(), oBusinessDay.isSpecialDay(), oBusinessDay.isDayBeforeSpecialDay(), oBusinessDay.getDayOfWeek(), AppGlobal.g_oFuncUser.get().getUserGroupList(), false);
		
		//loop the discount list
		for(int i=0; i<3; i++) {
			if(sDiscCode[i].isEmpty())
				continue;
			
			if((i == 0 && dFoodDiscValue.compareTo(BigDecimal.ZERO) == 0) ||
					(i == 1 && dBevDiscValue.compareTo(BigDecimal.ZERO) == 0) ||
					(i == 2 && dMiscDiscValue.compareTo(BigDecimal.ZERO) == 0))
				continue;
			if(i == 0)
				dValue = dFoodDiscValue;
			else if(i == 1)
				dValue = dBevDiscValue;
			else
				dValue = dMiscDiscValue;
			
			//check discount existence
			oSelectedDiscType = oDiscTypeList.getDiscountTypeByCode(sDiscCode[i]);
			if(oSelectedDiscType == null)
				continue;
			
			//check whether item discount
			if(oSelectedDiscType.isApplyToCheck())
				continue;
			
			//check whether is used for discount
			if(oSelectedDiscType.isUsedForExtraCharge())
				continue;
			
			//check whether open discount
			if(oSelectedDiscType.isFixAmountDiscountPerItemMethod() && oSelectedDiscType.getFixAmount().compareTo(BigDecimal.ZERO) != 0)
				continue;
			if(oSelectedDiscType.isPercentageDiscountMethod() && oSelectedDiscType.getRate().compareTo(BigDecimal.ZERO) != 0)
				continue;
			
			//check open amount
			if(oSelectedDiscType.isPercentageDiscountMethod() && dValue.compareTo(BigDecimal.ONE) > 0)
				continue;
			if(dValue.compareTo(BigDecimal.ZERO) == 0)
				continue;
			dDiscountRateAmt = dValue.multiply(new BigDecimal("-1.0"));
			
			// get the discount allowance
			List<Integer> oItemDiscountGrpList = new ArrayList<Integer>();
			HashMap<Integer, Boolean> oDiscountAllowance = new HashMap<Integer, Boolean>();
			for(HashMap<String, Integer> oSelectedItem:oSelectedItems) {
				FuncCheckItem oParentFuncCheckItem = m_oFuncCheck.getCheckItem(oSelectedItem.get("partySeq"), oSelectedItem.get("sectionId"), oSelectedItem.get("itemIndex"));

				// Pre-checking if the item is missing in menu
				if(oParentFuncCheckItem.getMenuItem() == null){
					String sErrMsg = AppGlobal.g_oLang.get()._("item_is_missing_in_menu_setup") + " (" + oParentFuncCheckItem.getItemDescriptionByIndex(AppGlobal.g_oCurrentLangIndex.get()) + ")";
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(sErrMsg);
					oFormDialogBox.show();
					return;
				}
				
				if(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId() != 0 && !oItemDiscountGrpList.contains(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId())) {
					boolean bDiscountAllowance = AppGlobal.g_oFuncDiscountAclList.get(AppGlobal.g_oFuncOutlet.get().getOutletId()).checkDiscountAcl(oParentFuncCheckItem.getMenuItem(), oSelectedDiscType);
					oDiscountAllowance.put(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId(), bDiscountAllowance);
				}
			}
			
			// check and prepare the item list for applying discount
			List<HashMap<String, Integer>> oItemIndexList = new ArrayList<HashMap<String, Integer>>();
			for(HashMap<String, Integer> oSelectedItem:oSelectedItems) {
				FuncCheckItem oParentFuncCheckItem = m_oFuncCheck.getCheckItem(oSelectedItem.get("partySeq"), oSelectedItem.get("sectionId"), oSelectedItem.get("itemIndex"));
				
				//check the selected discount is available for the selected item
				if(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId() == 0)
					continue;
				if(!oDiscountAllowance.containsKey(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId()))
					continue;
				if(oDiscountAllowance.get(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId()) == false)
					continue;
				
				//check whether the selected item is available for applying discount
				if(oParentFuncCheckItem.getCheckItem().getTotal().compareTo(BigDecimal.ZERO) == 0)
					continue;
				
				if(oParentFuncCheckItem.hasItemDiscount(true))
					continue;
				
				oItemIndexList.add(oSelectedItem);
			}
			
			if(!m_oFuncCheck.applyDiscount("item", PosDiscountType.USED_FOR_DISCOUNT, oItemIndexList, oSelectedDiscType, dDiscountRateAmt, null, 0))
				continue;
		}
		
		//set member
		frameMembershipInterfaceResult_clickSetMember(null);
		
		this.finishShow();
	}
	
	@Override
	public void frameMembershipVoucherRedemptionResult_voucherRedemption(String sVoucherInfo, int iItemIndex) {
		int i = -1;
		
		String[] sContent_Split = sVoucherInfo.split(",");
		m_oFrameMembershipInterface.setCommonBasketBackgroundColor(iItemIndex, FrameMembershipInterface.BACKGROUND_COLOUR_SELECTED);
		FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
		oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("apply_voucher"));
		oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("confirm_to_apply_voucher") + "?");
		oFormConfirmBox.show();
		if (oFormConfirmBox.isOKClicked() == false) {
			m_oFrameMembershipInterface.setCommonBasketBackgroundColor(iItemIndex, FrameMembershipInterface.BACKGROUND_COLOUR_UNSELECTED);
			return;
		}
		
		boolean bVoucherMatch = false;
		int iVoucherUsedQty = 0;
		//For CRM Interface: only have check discount and item coupon
		
		if (sContent_Split[1].compareToIgnoreCase("cash") == 0) {
			m_sSelectedVoucher = sVoucherInfo;
		} else if (sContent_Split[1].compareToIgnoreCase("freeitem") == 0) {
			m_sSelectedVoucher = sVoucherInfo;
			m_sSelectedInterfaceID = Integer.toString(m_oPosInterfaceConfig.getInterfaceId());
			m_sSelectedPriceLevel = String.valueOf(AppGlobal.g_oFuncOutlet.get().getPriceLevel());
			if (!m_oInterfaceSetup.optJSONObject("voucher_setup").optJSONObject("params").optJSONObject("price_level_for_free_item")
					.optString("value").isEmpty())
				m_sSelectedPriceLevel = m_oInterfaceSetup.optJSONObject("voucher_setup").optJSONObject("params")
						.optJSONObject("price_level_for_free_item").optString("value");
		} else {
			String[] sDiscCode = new String[5];
			BigDecimal dPerDiscValue = BigDecimal.ZERO;
			BigDecimal dDolDiscValue = BigDecimal.ZERO;
			BigDecimal dBirDiscValue = BigDecimal.ZERO;
			BigDecimal dItemDiscValue = BigDecimal.ZERO;
			BigDecimal dValue = null, dDiscountRateAmt = null;
			List<HashMap<String, Integer>> oSelectedItems = new ArrayList<HashMap<String, Integer>>();
			
			PosDiscountType oSelectedDiscType = null;
			
			DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
			PosBusinessDay oBusinessDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay();
			PosDiscountTypeList oDiscTypeList = new PosDiscountTypeList();
			
			if (m_sEnquiryAccNumber.isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("apply_discount"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("please_do_enquiry_first"));
				oFormDialogBox.show();
				return;
			}
			
			sDiscCode[0] = "";
			sDiscCode[1] = "";
			sDiscCode[2] = "";
			sDiscCode[3] = "";
			
			if (!m_oInterfaceSetup.optJSONObject("voucher_setup").optJSONObject("params").optJSONObject("percentage_discount_code")
					.optString("value").isEmpty())
				sDiscCode[0] = m_oInterfaceSetup.optJSONObject("voucher_setup").optJSONObject("params")
						.optJSONObject("percentage_discount_code").optString("value");
			if (!m_oInterfaceSetup.optJSONObject("voucher_setup").optJSONObject("params")
					.optJSONObject("dollar_discount_code").optString("value").isEmpty())
				sDiscCode[1] = m_oInterfaceSetup.optJSONObject("voucher_setup").optJSONObject("params")
						.optJSONObject("dollar_discount_code").optString("value");
			if (!m_oInterfaceSetup.optJSONObject("voucher_setup").optJSONObject("params").optJSONObject("birthday_discount_code")
					.optString("value").isEmpty())
				sDiscCode[2] = m_oInterfaceSetup.optJSONObject("voucher_setup").optJSONObject("params")
						.optJSONObject("birthday_discount_code").optString("value");
			if (!m_oInterfaceSetup.optJSONObject("voucher_setup").optJSONObject("params").optJSONObject("item_discount_code")
					.optString("value").isEmpty())
				sDiscCode[3] = m_oInterfaceSetup.optJSONObject("voucher_setup").optJSONObject("params")
						.optJSONObject("item_discount_code").optString("value");
			
			if (sDiscCode[0].isEmpty() && sDiscCode[1].isEmpty() && sDiscCode[2].isEmpty() && sDiscCode[3].isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("apply_discount"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("no_discount_setup"));
				oFormDialogBox.show();
				return;
			}
			
			dPerDiscValue = new BigDecimal(sContent_Split[2]);
			dDolDiscValue = new BigDecimal(sContent_Split[2]);
			dBirDiscValue = new BigDecimal(sContent_Split[2]);
			dItemDiscValue = new BigDecimal(sContent_Split[2]);
			
			if (dPerDiscValue.compareTo(BigDecimal.ZERO) == 0 && dDolDiscValue.compareTo(BigDecimal.ZERO) == 0
					&& dBirDiscValue.compareTo(BigDecimal.ZERO) == 0 && dItemDiscValue.compareTo(BigDecimal.ZERO) == 0) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("zero_discount_value"));
				oFormDialogBox.show();
				return;
			}
			
			// Get all item
			oSelectedItems = m_oFuncCheck.getSectionItemIndexForCurrentOrderedItem();
			if (oSelectedItems.size() <= 0) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("null_item_list"));
				oFormDialogBox.show();
				return;
			}
			
			// loop the discount list
			if (sContent_Split[1].equals("pdiscount"))
				i = 0;
			if (sContent_Split[1].equals("ddiscount"))
				i = 1;
			if (sContent_Split[1].equals("bddiscount"))
				i = 2;
			if (sContent_Split[1].equals("iddiscount"))
				i = 3;
			
			String sDiscountType = null;
			if (i == 0 || i == 1 || i == 2)
				sDiscountType = "check";
			if (i == 3)
				sDiscountType = "item";
			
			// form the lookup description
			oDiscTypeList.readDiscountListByOutletId(sDiscountType, AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(),
					AppGlobal.g_oFuncStation.get().getStationId(), dateFormat.print(oBusinessDay.getDate()), oBusinessDay.isHoliday(), oBusinessDay.isDayBeforeHoliday(),
					oBusinessDay.isSpecialDay(), oBusinessDay.isDayBeforeSpecialDay(), oBusinessDay.getDayOfWeek(),
					AppGlobal.g_oFuncUser.get().getUserGroupList(), false);
			
			if (!checkVoucherRestriction(sContent_Split[1], null)) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("failed_to_apply") + System.lineSeparator()
						+ AppGlobal.g_oLang.get()._(getLastErrorMessage()));
				oFormDialogBox.show();
				m_oFrameMembershipInterface.setCommonBasketBackgroundColor(iItemIndex, FrameMembershipInterface.BACKGROUND_COLOUR_UNSELECTED);
				return;
			}
			
			if (i == 0 || i == 1 || i == 2 || i == 3)
				if (sDiscCode[i].isEmpty()) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("empty_discount_code"));
					oFormDialogBox.show();
					m_oFrameMembershipInterface.setCommonBasketBackgroundColor(iItemIndex, FrameMembershipInterface.BACKGROUND_COLOUR_UNSELECTED);
					return;
				}
			
			if ((i == 0 && dPerDiscValue.compareTo(BigDecimal.ZERO) == 0)
					|| (i == 1 && dDolDiscValue.compareTo(BigDecimal.ZERO) == 0)
					|| (i == 2 && dBirDiscValue.compareTo(BigDecimal.ZERO) == 0)
					|| (i == 3 && dItemDiscValue.compareTo(BigDecimal.ZERO) == 0)) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("zero_discount_value"));
				oFormDialogBox.show();
				m_oFrameMembershipInterface.setCommonBasketBackgroundColor(iItemIndex, FrameMembershipInterface.BACKGROUND_COLOUR_UNSELECTED);
				return;
			}
			if (i == 0)
				dValue = dPerDiscValue;
			else if (i == 1)
				dValue = dDolDiscValue;
			else if (i == 2)
				dValue = dBirDiscValue;
			else if (i == 3)
				dValue = dItemDiscValue;
			
			// check discount existence
			oSelectedDiscType = oDiscTypeList.getDiscountTypeByCode(sDiscCode[i]);
			if (oSelectedDiscType == null) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("discount_not_exist"));
				oFormDialogBox.show();
				m_oFrameMembershipInterface.setCommonBasketBackgroundColor(iItemIndex, FrameMembershipInterface.BACKGROUND_COLOUR_UNSELECTED);
				return;
			}
			
			//check whether item discount
			if (i == 3)
				if (oSelectedDiscType.isApplyToCheck()) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("not_item_discount_code"));
					oFormDialogBox.show();
					m_oFrameMembershipInterface.setCommonBasketBackgroundColor(iItemIndex, FrameMembershipInterface.BACKGROUND_COLOUR_UNSELECTED);
					return;
				}
			
			// check whether check discount
			if (i == 0 || i == 1 || i == 2)
				if (oSelectedDiscType.isApplyToItem()) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("not_check_discount_code"));
					oFormDialogBox.show();
					m_oFrameMembershipInterface.setCommonBasketBackgroundColor(iItemIndex, FrameMembershipInterface.BACKGROUND_COLOUR_UNSELECTED);
					return;
				}
			
			// check whether is used for discount
			if (oSelectedDiscType.isUsedForExtraCharge()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("used_for_extra_charge"));
				oFormDialogBox.show();
				m_oFrameMembershipInterface.setCommonBasketBackgroundColor(iItemIndex, FrameMembershipInterface.BACKGROUND_COLOUR_UNSELECTED);
				return;
			}
			
			// check whether open discount
			if (i == 0) {
				if (oSelectedDiscType.isFixAmountDiscountPerItemMethod()) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fixed_amount_disc_type_is_not_allowed"));
					oFormDialogBox.show();
					m_oFrameMembershipInterface.setCommonBasketBackgroundColor(iItemIndex, FrameMembershipInterface.BACKGROUND_COLOUR_UNSELECTED);
					return;
				}
				dDiscountRateAmt = (dValue.divide(new BigDecimal("100.0"))).multiply(new BigDecimal("-1.0"));
			}
			
			// check open amount
			if (i == 1 || i == 2 || i == 3) {
				if (oSelectedDiscType.isPercentageDiscountMethod()) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("percentage_disc_type_is_not_allowed"));
					oFormDialogBox.show();
					m_oFrameMembershipInterface.setCommonBasketBackgroundColor(iItemIndex, FrameMembershipInterface.BACKGROUND_COLOUR_UNSELECTED);
					return;
				}
				dDiscountRateAmt = dValue.multiply(new BigDecimal("-1.0"));
			}
			
			List<Integer> oItemDiscountGrpList = new ArrayList<Integer>();
			HashMap<Integer, Boolean> oDiscountAllowance = new HashMap<Integer, Boolean>();
			List<HashMap<String, Integer>> oItemIndexList = new ArrayList<HashMap<String, Integer>>();
			
			// get the discount allowance
			for (HashMap<String, Integer> oSelectedItem : oSelectedItems) {
				FuncCheckItem oParentFuncCheckItem = m_oFuncCheck.getCheckItem(oSelectedItem.get("partySeq"),
						oSelectedItem.get("sectionId"), oSelectedItem.get("itemIndex"));
				
				// Pre-checking if the item is missing in menu
				if (oParentFuncCheckItem.getMenuItem() == null) {
					String sErrMsg = AppGlobal.g_oLang.get()._("item_is_missing_in_menu_setup") + " ("
							+ oParentFuncCheckItem.getItemDescriptionByIndex(AppGlobal.g_oCurrentLangIndex.get()) + ")";
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(sErrMsg);
					oFormDialogBox.show();
					m_oFrameMembershipInterface.setCommonBasketBackgroundColor(iItemIndex, FrameMembershipInterface.BACKGROUND_COLOUR_UNSELECTED);
					return;
				}
				
				if (oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId() != 0
						&& !oItemDiscountGrpList.contains(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId())) {
					boolean bDiscountAllowance = AppGlobal.g_oFuncDiscountAclList.get(AppGlobal.g_oFuncOutlet.get().getOutletId()).checkDiscountAcl(oParentFuncCheckItem.getMenuItem(), oSelectedDiscType);
					oDiscountAllowance.put(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId(), bDiscountAllowance);
				}
			}
			
			// check and prepare the item list for applying discount
			for (HashMap<String, Integer> oSelectedItem : oSelectedItems) {
				FuncCheckItem oParentFuncCheckItem = m_oFuncCheck.getCheckItem(oSelectedItem.get("partySeq"),
						oSelectedItem.get("sectionId"), oSelectedItem.get("itemIndex"));
				
				// check the selected discount is available for the selected item
				if (oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId() == 0)
					continue;
				if (!oDiscountAllowance.containsKey(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId()))
					continue;
				if (oDiscountAllowance.get(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId()) == false)
					continue;
				
				// check whether the selected item is available for applying
				// discount
				if (oParentFuncCheckItem.getCheckItem().getTotal().compareTo(BigDecimal.ZERO) == 0)
					continue;
				
				if (i == 3) {
					if (oParentFuncCheckItem.hasItemDiscount(true))
						continue;
					
					//checking of the tender code
					if (!oParentFuncCheckItem.getMenuItem().getCode().equals(sContent_Split[4]))
						continue;
					else {
						oItemIndexList.add(oSelectedItem);
						break;
					}
				}
				
				oItemIndexList.add(oSelectedItem);
			}
			
			if (oItemIndexList.isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("no_item_can_apply_discount"));
				oFormDialogBox.show();
				m_oFrameMembershipInterface.setCommonBasketBackgroundColor(iItemIndex, FrameMembershipInterface.BACKGROUND_COLOUR_UNSELECTED);
				return;
			}
			
			if (i == 3) {
				ArrayList<PosCheckExtraInfo> oExtraInfoList = new ArrayList<PosCheckExtraInfo>();
				oExtraInfoList = this.addDiscountCheckExtraInfoToList(oExtraInfoList, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, Integer.toString(m_oPosInterfaceConfig.getInterfaceId()), 0);
				oExtraInfoList = this.addDiscountCheckExtraInfoToList(oExtraInfoList, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER, sContent_Split[0], 0);
				oExtraInfoList = this.addDiscountCheckExtraInfoToList(oExtraInfoList, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE, sContent_Split[1], 0);
				oExtraInfoList = this.addDiscountCheckExtraInfoToList(oExtraInfoList, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_VALUE, sContent_Split[2], 0);
				
				
				if (!m_oFuncCheck.applyDiscount("item", PosDiscountType.USED_FOR_DISCOUNT, oItemIndexList,
						oSelectedDiscType, dDiscountRateAmt, oExtraInfoList, 0)) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_apply_discount"));
					oFormDialogBox.show();
					m_oFrameMembershipInterface.setCommonBasketBackgroundColor(iItemIndex, FrameMembershipInterface.BACKGROUND_COLOUR_UNSELECTED);
					return;
				}
				m_oFuncCheck.calcCheck();
			}
			
			if (i == 0 || i == 1 || i == 2) {
				ArrayList<PosCheckExtraInfo> oExtraInfoList = new ArrayList<PosCheckExtraInfo>();
				oExtraInfoList = this.addDiscountCheckExtraInfoToList(oExtraInfoList, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, Integer.toString(m_oPosInterfaceConfig.getInterfaceId()), 0);
				oExtraInfoList = this.addDiscountCheckExtraInfoToList(oExtraInfoList, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER, sContent_Split[0], 0);
				oExtraInfoList = this.addDiscountCheckExtraInfoToList(oExtraInfoList, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE, sContent_Split[1], 0);
				oExtraInfoList = this.addDiscountCheckExtraInfoToList(oExtraInfoList, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_VALUE, sContent_Split[2], 0);
				
				if (!m_oFuncCheck.applyDiscount("check", PosDiscountType.USED_FOR_DISCOUNT, oItemIndexList,
						oSelectedDiscType, dDiscountRateAmt, oExtraInfoList, 0)) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_apply_discount"));
					oFormDialogBox.show();
					m_oFrameMembershipInterface.setCommonBasketBackgroundColor(iItemIndex, FrameMembershipInterface.BACKGROUND_COLOUR_UNSELECTED);
					return;
				}
				m_oFuncCheck.calcCheck();
			}
		}
		
		finishShow();
	}
	
	@Override
	public void frameMembershipInterfaceResult_clickRemoveVoucher() {
		List<HashMap<String, Integer>> oSelectedItems = new ArrayList<HashMap<String, Integer>>();
		oSelectedItems = m_oFuncCheck.getSectionItemIndexForCurrentOrderedItem();
		
		HashMap <String, String> oHashMap = new HashMap <String, String>();
		ArrayList<HashMap <String, String>> oVoucherList = new ArrayList<HashMap <String, String>>();
		ArrayList<String> oOptionList = new ArrayList<String>();
		List<PosCheckDiscount> oAppliedCheckPartyDiscountList = m_oFuncCheck.getCurrentPartyAppliedCheckDiscount();

		//search for applied check discount voucher
		for(PosCheckDiscount oCheckDiscount: oAppliedCheckPartyDiscountList) {
			PosCheckExtraInfo oExtraInfo = null;
			oExtraInfo = oCheckDiscount.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0);
			if(oExtraInfo == null)
				continue;
			else {
				oExtraInfo = oCheckDiscount.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER, 0);
				oHashMap.put(PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER, oExtraInfo.getValue());
				oOptionList.add(oExtraInfo.getValue());
				oExtraInfo = oCheckDiscount.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE, 0);
				oHashMap.put(PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE, oExtraInfo.getValue());
				oExtraInfo = oCheckDiscount.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_VALUE, 0);
				oHashMap.put(PosCheckExtraInfo.VARIABLE_VOUCHER_VALUE, oExtraInfo.getValue());
			}
			if (!oHashMap.isEmpty())
				if (oHashMap.containsKey(PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER) &&
					oHashMap.containsKey(PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE) &&
					oHashMap.containsKey(PosCheckExtraInfo.VARIABLE_VOUCHER_VALUE) ){
						oVoucherList.add(oHashMap);
						oHashMap = new HashMap <String, String>();
				}
				
		}
		
		//search for applied item discount voucher
		for (List<FuncCheckItem> oFuncCheckItemList: m_oFuncCheck.getWholeItemList()) {
			for (FuncCheckItem oFuncCheckItem: oFuncCheckItemList) {
				List<PosCheckDiscount> oCurrentDiscountList = oFuncCheckItem.getItemDiscountList();
				
				for (PosCheckDiscount oPosCheckDiscount:oCurrentDiscountList) {
					for (PosCheckExtraInfo oPosCheckExtraInfo:oPosCheckDiscount.getCheckExtraInfoList()) {
						if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER)){
							oOptionList.add(oPosCheckExtraInfo.getValue() );
							oHashMap.put(PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER, oPosCheckExtraInfo.getValue());
						}
						if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE)){
							oHashMap.put(PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE, oPosCheckExtraInfo.getValue());
						}
						if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_VOUCHER_VALUE)){
							oHashMap.put(PosCheckExtraInfo.VARIABLE_VOUCHER_VALUE, oPosCheckExtraInfo.getValue());
							if (oHashMap.containsKey(PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER) &&
								oHashMap.containsKey(PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE) &&
								oHashMap.containsKey(PosCheckExtraInfo.VARIABLE_VOUCHER_VALUE) ){
									oVoucherList.add(oHashMap);
									oHashMap = new HashMap <String, String>();
							}
						}
					}
				}
			}
		}

		//search for applied free item voucher
		String sVoucherNumber = null;
		for (List<FuncCheckItem> oFuncCheckItemList: m_oFuncCheck.getWholeItemList()) {
			for (FuncCheckItem oFuncCheckItem: oFuncCheckItemList) {
				if(oFuncCheckItem.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER, 0)
						&& oFuncCheckItem.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE, 0)
						&& oFuncCheckItem.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_VALUE, 0)
						&& !oFuncCheckItem.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE).isEmpty()
						&& oFuncCheckItem.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE).compareToIgnoreCase("freeitem") == 0) {
					sVoucherNumber = oFuncCheckItem.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER);
					oOptionList.add(sVoucherNumber);
					
					oHashMap = new HashMap <String, String>();
					oHashMap.put(PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE, oFuncCheckItem.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE));
					oHashMap.put(PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER, sVoucherNumber);
					oHashMap.put(PosCheckExtraInfo.VARIABLE_VOUCHER_VALUE, oFuncCheckItem.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_VALUE));
					oVoucherList.add(oHashMap);
				}
			}
		}

		if (oVoucherList.size() <= 0){
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("no_voucher_is_applied"));
			oFormDialogBox.show();
			return;
		}
		
		FormSelectionBox oFormSelectionBox = new FormSelectionBox(this);
		oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("please_select_the_item"), oOptionList, false);
		oFormSelectionBox.show();
		if (oFormSelectionBox.isUserCancel()) 		// User cancel
			return;
		else {
			ArrayList<Integer> oSelectionBoxResult = oFormSelectionBox.getResultList();
			HashMap <String, String> oVoucherHashMap = oVoucherList.get(oSelectionBoxResult.get(0));
			
			//free item voucher
			if (oVoucherHashMap.get(PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE).compareToIgnoreCase("freeitem") == 0){
				oSelectedItems = m_oFuncCheck.getSectionItemIndexForCurrentOrderedItem();
				if (oSelectedItems.size() <= 0){
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("empty_item_list"));
					oFormDialogBox.show();
					return;
				}
				
				// check and prepare the item list for applying discount
				for (HashMap<String, Integer> oSelectedItem : oSelectedItems) {
					FuncCheckItem oParentFuncCheckItem = m_oFuncCheck.getCheckItem(oSelectedItem.get("partySeq"),
							oSelectedItem.get("sectionId"), oSelectedItem.get("itemIndex"));

					for (PosCheckExtraInfo oPosCheckExtraInfo:oParentFuncCheckItem.getExtraInfoList()) {
						if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER)){
							if (oPosCheckExtraInfo.getValue().equals(oVoucherHashMap.get(PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER))){
								if (oParentFuncCheckItem.getMenuItem().getCode().equals(oVoucherHashMap.get(PosCheckExtraInfo.VARIABLE_VOUCHER_VALUE)) ){
									Integer iSectionId = oSelectedItem.get("sectionId");
									Integer iItemIndex = oSelectedItem.get("itemIndex");

									if (m_oFuncCheck.deleItem(new BigDecimal("1.0"), oSelectedItem.get("sectionId"), oSelectedItem.get("itemIndex"), 0, null, false)){
										FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
										oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("success"));
										oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("voucher_is_voided"));
										oFormDialogBox.show();
										
										m_oRemovedItemHashMap.put("sectionId",iSectionId);
										m_oRemovedItemHashMap.put("itemIndex",iItemIndex);
										finishShow();
										return;
									}
								}
							}
						}
					}
				}
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._(m_oFuncCheck.getLastErrorMessage()));
				oFormDialogBox.show();
			//item discount
			} else if (oVoucherHashMap.get(PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE).compareToIgnoreCase("iddiscount") == 0){
				
				List<HashMap<String, Integer>> oItemIndexList = new ArrayList<HashMap<String, Integer>>();
				oSelectedItems = m_oFuncCheck.getSectionItemIndexForCurrentOrderedItem();
				// check and prepare the item list for voiding discount
				Boolean bIsfound = false;
				for (HashMap<String, Integer> oSelectedItem : oSelectedItems) {
					FuncCheckItem oParentFuncCheckItem = m_oFuncCheck.getCheckItem(oSelectedItem.get("partySeq"),
							oSelectedItem.get("sectionId"), oSelectedItem.get("itemIndex"));
					List<PosCheckDiscount> oCurrentDiscountList = oParentFuncCheckItem.getItemDiscountList();
					for (PosCheckDiscount oPosCheckDiscount:oCurrentDiscountList) {
						for (PosCheckExtraInfo oPosCheckExtraInfo:oPosCheckDiscount.getCheckExtraInfoList()) {
								if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER)){
									if (oPosCheckExtraInfo.getValue().equals(oVoucherHashMap.get(PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER))){
										oItemIndexList.add(oSelectedItem);
										bIsfound = true;
										break;
								}
								}
						}
					}
					if (bIsfound == true)
						break;
				}
				
				if (m_oFuncCheck.voidDiscount("item", PosDiscountType.USED_FOR_DISCOUNT, oItemIndexList, 0, 0, "")){
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("success"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("voucher_is_voided"));
					oFormDialogBox.show();
					finishShow();
					return;
				}
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("voucher_cannot_be_voided"));
				oFormDialogBox.show();
			
			//check discount
			} else {
				oSelectedItems = m_oFuncCheck.getSectionItemIndexForCurrentOrderedItem();
				Integer iDiscountIndex = -1;

				for(PosCheckDiscount oCheckDiscount: oAppliedCheckPartyDiscountList) {
					PosCheckExtraInfo oExtraInfo = null;
					oExtraInfo = oCheckDiscount.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0);
					if(oExtraInfo == null)
						continue;
					else {
						oExtraInfo = oCheckDiscount.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER, 0);
						if (oVoucherHashMap.get(PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER).equals(oExtraInfo.getValue())) {
							iDiscountIndex = oCheckDiscount.getSeq();
							break;
						}
					}
				}
				
				if(iDiscountIndex != -1){
					if (m_oFuncCheck.voidDiscount("check", PosDiscountType.USED_FOR_DISCOUNT, oSelectedItems, iDiscountIndex, 0, "")){
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("success"));
						oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("voucher_is_voided"));
						oFormDialogBox.show();
						finishShow();
						return;
					}
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("voucher_cannot_be_voided"));
					oFormDialogBox.show();
				}
			}
		}
	}
	
	@Override
	public void frameMembershipInterfaceResult_clickClearMember() {
		this.clearMember();
		this.finishShow();
	}
	
	@Override
	public void frameMembershipInterfaceResult_exchangeRate(String sType, String sValue, String sVoucherValue) {
	
	}

	@Override
	public void FrameMembershipInterface_disconnect() {
		// Disconnect
		if(m_bProcessSuccess == false){
			// Set the connection timeout to device manager to prevent timeout loop
			AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(0);
			
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("disconnect_from_smart_card_device");
			
			m_oFormProcessBox.removeUI();
			this.finishShow();
		}
	}
	
	@Override
	public void FrameMembershipInterface_timeout() {
		// Timeout
		if(m_bProcessSuccess == false){
			// Set the connection timeout to device manager to prevent timeout loop
			AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(0);
			
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("timeout");
			
			m_oFormProcessBox.removeUI();
			this.finishShow();
		}
	}

	@Override
	public void FrameMembershipInterface_forward(String sResponse) {
		// Close processing box
		m_oFormProcessBox.closeShowWithoutRemoveUI();
		
		// Set the connection timeout to device manager to prevent timeout loop
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(0);
		
		if (sResponse != null && !sResponse.isEmpty()) {
			String[] sResult = m_oFuncMembershipInterface.breakPacketDataString(m_sOperatingFunction, sResponse);
			if (sResult == null) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("enquiry"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("member_enquiry_failed"));
				oFormDialogBox.show();
				if(m_bProcessingEvent)
					this.finishShow();
				return;
			}
			
			m_bProcessSuccess = true;
			if (m_bProcessingEvent) {
				this.finishShow();
				return;
			}
			
			String sScreenMsg = m_oFuncMembershipInterface.getLastMemberInfo().get("screenMessage");
			if (sScreenMsg != null && !sScreenMsg.isEmpty()) {
				// Display screen message
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("message"));
				oFormDialogBox.setMessage(sScreenMsg);
				oFormDialogBox.show();
				oFormDialogBox = null;
			}
		} else {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("enquiry"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("member_enquiry_failed"));
			oFormDialogBox.show();
			if (m_bProcessingEvent)
				this.finishShow();
		}
	}
	
	@Override
	public void frameMembershipInterfaceResult_clickReset() {
		// get original max redemption points and max redemption amount
		String sPreviousMaxRedemptionPoints = "", sPreviousMaxRedemptionAmount = "", sVoucherValue = "";
		BigDecimal dPreviousMaxRedemptionAmount = BigDecimal.ZERO, dVoucherValue = BigDecimal.ZERO,
				dTempExchangeValue = BigDecimal.ZERO, dPreviousMaxRedemptionPoints = BigDecimal.ZERO;
		
		int iRoundingDecimal = 0;
		if(m_oPosInterfaceConfig.getInterfaceConfig().has("currency_setup") && m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("currency_setup").optJSONObject("params").has("rounding_decimal"))
			iRoundingDecimal = m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("currency_setup").optJSONObject("params").optJSONObject("rounding_decimal").optInt("value", 0);
		
		if(m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MAX_REDEMPT_AMOUNT, 0)
				&& !m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MAX_REDEMPT_AMOUNT).isEmpty())
			sPreviousMaxRedemptionAmount = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MAX_REDEMPT_AMOUNT);
		
		if(m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MAX_REDEMPT_AMOUNT, 0)
				&& !m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MAX_REDEMPT_AMOUNT).isEmpty())
			sPreviousMaxRedemptionPoints = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MAX_REDEMPT_POINTS);
		
		if(m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MAX_REDEMPT_AMOUNT, 0)
				&& !m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MAX_REDEMPT_AMOUNT).isEmpty())
			sVoucherValue = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_VALUE);
		
		if (!sPreviousMaxRedemptionAmount.isEmpty() && sPreviousMaxRedemptionAmount != null
				&& !sPreviousMaxRedemptionPoints.isEmpty() && sPreviousMaxRedemptionPoints != null
				&& !sVoucherValue.isEmpty() && sVoucherValue != null) {
			dPreviousMaxRedemptionAmount = new BigDecimal(sPreviousMaxRedemptionAmount);
			dPreviousMaxRedemptionPoints = new BigDecimal(sPreviousMaxRedemptionPoints);
			dVoucherValue = new BigDecimal(sVoucherValue);
			dPreviousMaxRedemptionAmount = dPreviousMaxRedemptionAmount.add(dVoucherValue);
			BigDecimal dExchangeRateAmountToPoints = m_oFuncMembershipInterface.getExchangeRate(true, null, m_oFuncCheck.getCheckTotal());
			dTempExchangeValue = dVoucherValue.multiply(dExchangeRateAmountToPoints).setScale(0, BigDecimal.ROUND_HALF_UP);
			dPreviousMaxRedemptionPoints = dPreviousMaxRedemptionPoints.add(dTempExchangeValue);
		} else {
			dPreviousMaxRedemptionAmount =  new BigDecimal(m_oRedemptionInfoList.get("maximumRedemptionAmount"));
			dPreviousMaxRedemptionPoints =  new BigDecimal(m_oRedemptionInfoList.get("maximumRedemptionPoints"));
		}
	}

}