package app;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import app.FuncGamingInterface.GamingInterfaceResponseInfo;
import om.PosCheckExtraInfo;
import om.PosDiscountType;
import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import core.Controller;
import om.InfInterface;
import om.InfVendor;
import om.PosActionLog;
import om.PosCheckDiscount;
import om.PosInterfaceConfig;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

interface FormGamingInterfaceListener{
	boolean formGamingInterfaceResult_clickCardEnquiry(String sEnquiryValue, boolean bIsSwipeCard,
			FuncGamingInterface oFuncGamingInterface);
	public boolean formGamingInterfaceResult_clickApplyDiscount(String sDiscountCode, String sDiscountPercentage, ArrayList <PosCheckExtraInfo> oDiscountCheckExtraInfoList);
}
public class FormGamingInterface extends VirtualUIForm implements FrameGamingInterfaceListener{
	private TemplateBuilder m_oTemplateBuilder;
	
	private FrameGamingInterface m_oFrameGamingInterface;
	
	private FuncCheck m_oFuncCheck;
	private FuncPayment m_oFuncPayment;
	private PosInterfaceConfig m_oPosInterfaceConfig;
	
	private PosInterfaceConfig m_oMSRInterfaceConfig;
	private PosInterfaceConfig m_oEmployeeCardMSRInterfaceConfig;
	
	private String m_sType;
	private boolean m_bCancel = false;
	
	private String m_sLastMemberEnquiryInput;
	private String m_sLastInputType;
	private GamingInterfaceResponseInfo m_oLastGamingPostResponseInfo;
	
	//Post Online Comp amount
	private String m_sTargetValue;
	private String m_sDateOfBirth;
	
	private String m_sOfflineRemark;
	
	//Checking variable
	private boolean m_bIsTableFloorPlanVisible;
	private boolean m_bIsEnquiryCard;
	private String m_sInputControl;
	private String m_sPostingType;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FormGamingInterfaceListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FormGamingInterfaceListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FormGamingInterfaceListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	public FormGamingInterface(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		m_oTemplateBuilder.loadTemplate("frmGamingInterface.xml");
		
		m_oFrameGamingInterface = new FrameGamingInterface();
		
		initLastMemberInfo();
		
		m_bIsTableFloorPlanVisible = false;
		m_bIsEnquiryCard = false;
		
		m_sInputControl	= FuncMSR.SWIPE_CARD_NO_CONTROL;	// Default no limit
		m_sPostingType	= FuncGamingInterface.ONLINE_POSTING;	// Default online
		
		m_sTargetValue = "";
		m_sDateOfBirth = "";
		m_sOfflineRemark = "";
		listeners = new ArrayList<FormGamingInterfaceListener>();
		
		
	}
	
	public boolean init(FuncCheck oFuncCheck, FuncPayment oFuncPayment, PosInterfaceConfig oPaymentInterfaceConfig, String sInquiryType, boolean bIsTableFloorPlanVisible) {
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFuncCheck = oFuncCheck;
		m_oFuncPayment = oFuncPayment;
		m_bIsTableFloorPlanVisible = bIsTableFloorPlanVisible;
		m_oPosInterfaceConfig = oPaymentInterfaceConfig;
		String sInputControl = "", sPostingType = "", sType = "";
		String [] sReasonCode = null;
		
		ArrayList <String> oList;
		
		ArrayList<ArrayList <String>> oRemarkList = null;
		boolean bIsOnlinePosting = false;
		
		// read the setup for payment interface configuration
		if (sInquiryType == null || sInquiryType.isEmpty()) {
			if (oPaymentInterfaceConfig.getConfigValue() != null
					&& oPaymentInterfaceConfig.getConfigValue().has("general") && oPaymentInterfaceConfig
							.getConfigValue().optJSONObject("general").optJSONObject("params").has("payment_type"))
				sType = oPaymentInterfaceConfig.getConfigValue().optJSONObject("general").optJSONObject("params")
						.optJSONObject("payment_type").optString("value", "");
			
			if (oPaymentInterfaceConfig.getConfigValue() != null
					&& oPaymentInterfaceConfig.getConfigValue().has("general") && oPaymentInterfaceConfig
							.getConfigValue().optJSONObject("general").optJSONObject("params").has("input_control"))
				sInputControl = oPaymentInterfaceConfig.getConfigValue().optJSONObject("general")
						.optJSONObject("params").optJSONObject("input_control").optString("value", "");
			
			if (oPaymentInterfaceConfig.getConfigValue() != null
					&& oPaymentInterfaceConfig.getConfigValue().has("general") && oPaymentInterfaceConfig
							.getConfigValue().optJSONObject("general").optJSONObject("params").has("posting_type"))
				sPostingType = oPaymentInterfaceConfig.getConfigValue().optJSONObject("general").optJSONObject("params")
						.optJSONObject("posting_type").optString("value", "");
		} else {
			sInputControl = FuncMSR.SWIPE_CARD_NO_CONTROL;
			sPostingType = FuncGamingInterface.ONLINE_POSTING;
			sType = sInquiryType;
		}
		
		m_sInputControl = sInputControl;
		m_sPostingType = sPostingType;
		m_sType = sType;
		
		if(m_sPostingType.equals(FuncGamingInterface.ONLINE_POSTING))
			bIsOnlinePosting = true;
		
		// prepare the reason code and reason remark list for "gems" gaming interface only
		if (oPaymentInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GEMS)) {
			if (sType.equals(FuncGamingInterface.POST_EXECUTIVE_COMP) && bIsOnlinePosting) {
				sReasonCode = new String[3];
				oRemarkList = new ArrayList<ArrayList<String>>();
				
				// get reason code name
				for (int i = 1; i <= 3; i++) {
					if (oPaymentInterfaceConfig.getInterfaceConfig() != null
							&& oPaymentInterfaceConfig.getInterfaceConfig().has("reason_codes")
							&& oPaymentInterfaceConfig.getInterfaceConfig().optJSONObject("reason_codes")
									.optJSONObject("params").has("reason_code_description_" + i))
						sReasonCode[i - 1] = oPaymentInterfaceConfig.getInterfaceConfig().optJSONObject("reason_codes")
								.optJSONObject("params").optJSONObject("reason_code_description_" + i)
								.optString("value");
					
					if (sReasonCode[i - 1].isEmpty())
						sReasonCode[i - 1] = "Reason Code " + i;
					
					String sRemarks = "";
					if (oPaymentInterfaceConfig.getInterfaceConfig() != null
							&& oPaymentInterfaceConfig.getInterfaceConfig().has("remark_options")
							&& oPaymentInterfaceConfig.getInterfaceConfig().optJSONObject("remark_options")
									.optJSONObject("params").has("remark_option_for_reason_code_" + i))
						sRemarks = oPaymentInterfaceConfig.getInterfaceConfig().optJSONObject("remark_options")
								.optJSONObject("params").optJSONObject("remark_option_for_reason_code_" + i)
								.optString("value");
					
					String[] sRemarkList = sRemarks.split(System.lineSeparator());
					oList = new ArrayList<String>();
					
					for (int k = 0; k < sRemarkList.length; k++)
						if (!sRemarkList[k].isEmpty())
							oList.add(sRemarkList[k]);
					
					oList.add("Others");
					oRemarkList.add(oList);
				}
			} else if (!bIsOnlinePosting) {
				// handle offline posting
				sReasonCode = new String[1];
				oRemarkList = new ArrayList<ArrayList<String>>();
				
				if (oPaymentInterfaceConfig.getInterfaceConfig() != null
						&& oPaymentInterfaceConfig.getInterfaceConfig().has("offline_payment_setting")
						&& oPaymentInterfaceConfig.getInterfaceConfig().optJSONObject("offline_payment_setting")
								.optJSONObject("params").has("reason_code_description"))
					sReasonCode[0] = oPaymentInterfaceConfig.getInterfaceConfig()
							.optJSONObject("offline_payment_setting").optJSONObject("params")
							.optJSONObject("reason_code_description").optString("value");
				
				if (sReasonCode[0].isEmpty())
					sReasonCode[0] = "Reason Code";
				
				String sRemarks = "";
				if (oPaymentInterfaceConfig.getInterfaceConfig() != null
						&& oPaymentInterfaceConfig.getInterfaceConfig().has("offline_payment_setting")
						&& oPaymentInterfaceConfig.getInterfaceConfig().optJSONObject("offline_payment_setting")
								.optJSONObject("params").has("remark_option_for_reason_code"))
					sRemarks = oPaymentInterfaceConfig.getInterfaceConfig().optJSONObject("offline_payment_setting")
							.optJSONObject("params").optJSONObject("remark_option_for_reason_code").optString("value");
				
				String[] sRemarkList = sRemarks.split(System.lineSeparator());
				oList = new ArrayList<String>();
				
				for (int k = 0; k < sRemarkList.length; k++)
					if (!sRemarkList[k].isEmpty())
						oList.add(sRemarkList[k]);
				
				oList.add("Others");
				oRemarkList.add(oList);
			}
		}
		
		m_oTemplateBuilder.buildFrame(m_oFrameGamingInterface, "fraGamingInterface");
		m_oFrameGamingInterface.init(oPaymentInterfaceConfig, sType, sInputControl, bIsOnlinePosting, sReasonCode, oRemarkList, bIsTableFloorPlanVisible);
		m_oFrameGamingInterface.addListener(this);
		
		// update the enquiry screen for gems enquiry function
		if (oPaymentInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GEMS)) {
			if (m_sType.equals(FuncGamingInterface.PATRON_INQUIRY) || m_sType.equals(FuncGamingInterface.EXECUTIVE_COMP_INQUIRY)) {
				//Check if member is attached for Parton Inquiry, if attached, update UI
				if (m_sType.equals(FuncGamingInterface.PATRON_INQUIRY) && !m_bIsTableFloorPlanVisible && m_oFuncCheck != null) {
					// check whether have other membership id attached
					String sInterfaceId = "";
					if(m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0))
						sInterfaceId = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID);
					
					if(sInterfaceId != null && !sInterfaceId.isEmpty()){
						if(!m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID).equals(Integer.toString(m_oPosInterfaceConfig.getInterfaceId()))){
							FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
							oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("patron_inquiry"));
							oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("have_different_interface_member")
									+ System.lineSeparator() + AppGlobal.g_oLang.get()._("please_clear_current_member_first"));
							oFormDialogBox.show();
							return true;
						} else {
							if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GEMS)) {
								String sMemberValue = "";
								
								if (m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER) != null &&
									!m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER).equals("")) {
									sMemberValue = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER);
								}
								
								
								//Re - Enquiry
								if(!sMemberValue.isEmpty())
									frameGamingInterfaceResult_clickEnquiry(sMemberValue, false);
							}
							
							//Update Parton Enquiry UI
							m_oFrameGamingInterface.changeSpecialEnquiryScreenForGems(FrameGamingInterface.TYPE_GEMS_ENQUIRY_SCREEN_COMP_INQUIRY);
						}
					}
				}
			} else if (!bIsOnlinePosting) {
				// Non Enquiry, offline posing, Update UI
				if (m_sType.equals(FuncGamingInterface.POST_ONLINE_COMP)) {
					//Update UI for Post online comp
					m_oFrameGamingInterface.changeSpecialEnquiryScreenForGems(FrameGamingInterface.TYPE_GEMS_ENQUIRY_SCREEN_POST_ONLINE_COMP_PAYMENT_OFFLINE);
					//Set Default Value to Check Balance
					if(m_oFuncPayment != null)
						m_oFrameGamingInterface.setOnlineCompTextBoxValue(m_oFuncPayment.getCurrentBalance().toPlainString());
				}
			}
		}else if (oPaymentInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_BALLY)) {
			if (m_sType.equals(FuncGamingInterface.CARD_ENQUIRY)) {
				//check whether card is already enquiry or not
				if(m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER, 0)
						&& m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0)
						&& m_oFuncCheck.getCheckExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0)
						.equals(Integer.toString(m_oPosInterfaceConfig.getInterfaceId()))){
					
					FuncGamingInterface oFuncGamingInterface = new FuncGamingInterface(oPaymentInterfaceConfig);
					String sEnquiryValue = m_oFuncCheck.getCheckExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER, 0);
					String sInterfaceId = Integer.toString(oPaymentInterfaceConfig.getInterfaceId());
					
					if(!sEnquiryValue.isEmpty()) {
						oFuncGamingInterface.cardEnquiry(sInterfaceId, sEnquiryValue, false);
						HashMap<String, String> oCardDetail = addCardDetail(oFuncGamingInterface.m_oLastCardEnquiryResponseInfo);
						m_oFrameGamingInterface.updateCardEnquiryResult(oCardDetail);
						m_oFrameGamingInterface.setApplyDiscountButtonVisible(false);
						m_bIsEnquiryCard = true;
					}
				}
			}
		}
		
		this.attachChild(m_oFrameGamingInterface);
		
		if(m_oPosInterfaceConfig != null) {
			String sMsrCode = "", sEmployeeCardMsrCode = "";
			if(m_oPosInterfaceConfig.getInterfaceConfig() != null && m_oPosInterfaceConfig.getInterfaceConfig().has("general_setup") && m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").has("params") && m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").has("msr_code"))
				sMsrCode = m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").optJSONObject("msr_code").optString("value");
			if(m_oPosInterfaceConfig.getInterfaceConfig() != null && m_oPosInterfaceConfig.getInterfaceConfig().has("general_setup") && m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").has("params") && m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").has("employee_card_msr_code"))
				sEmployeeCardMsrCode = m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").optJSONObject("employee_card_msr_code").optString("value");
			
			if(!sMsrCode.isEmpty()) {
				List<PosInterfaceConfig> oPeripheralDeviceInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PERIPHERAL_DEVICE);
				for (PosInterfaceConfig oPosInterfaceConfig : oPeripheralDeviceInterfaceConfigList) {
					if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_MSR) && oPosInterfaceConfig.getInterfaceCode().equals(sMsrCode))
						m_oMSRInterfaceConfig = oPosInterfaceConfig;
				}
			}
			
			if(!sEmployeeCardMsrCode.isEmpty()) {
				List<PosInterfaceConfig> oPeripheralDeviceInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PERIPHERAL_DEVICE);
				for (PosInterfaceConfig oPosInterfaceConfig : oPeripheralDeviceInterfaceConfigList) {
					if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_MSR) && oPosInterfaceConfig.getInterfaceCode().equals(sEmployeeCardMsrCode))
						m_oEmployeeCardMSRInterfaceConfig = oPosInterfaceConfig;
				}
			}
		}
		
		return true;
	}
	
	/***********************/
	/*	Helper Function    */
	/***********************/
	public void initLastMemberInfo() {
		m_sLastMemberEnquiryInput = "";
		m_sLastInputType = "";
		m_oLastGamingPostResponseInfo = null;
		m_sOfflineRemark = "";
	}
	
	private ArrayList<HashMap<String, String>> addExtraInfoToList(ArrayList<HashMap<String, String>> oExtraInfosList, String sVariable, String sIndex, String sValue ){
		HashMap<String, String> oExtraInfo = new HashMap<String, String>();
		oExtraInfo.put("variable", sVariable);
		oExtraInfo.put("index", sIndex);
		oExtraInfo.put("value", sValue);
		oExtraInfosList.add(oExtraInfo);
		return oExtraInfosList;
	}
	
	private void showDialogBox(String sTitle, String sMessage) {
		FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
		oFormDialogBox.setTitle(sTitle);
		oFormDialogBox.setMessage(sMessage);
		oFormDialogBox.show();
	}
	
	private boolean getUserConfirm(String sTitle, String sMessage) {
		FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
		oFormConfirmBox.setTitle(sTitle);
			oFormConfirmBox.setMessage(sMessage);
		oFormConfirmBox.show();
		if(oFormConfirmBox.isOKClicked() == false)
			return false;
		return true;
	}
	
	public boolean isUserCancel() {
		return this.m_bCancel;
	}
	
	public boolean isSwipedCard() {
		if (m_sType.equals(FuncGamingInterface.POST_ONLINE_COMP) || m_sType.equals(FuncGamingInterface.PATRON_INQUIRY) || m_sType.equals(FuncGamingInterface.EXECUTIVE_COMP_INQUIRY) )
			return m_sLastInputType.equals(FuncGamingInterface.SWIPE_CARD) ? true : false ;
		
		return this.m_oFrameGamingInterface.isSwipedCard();
	}
	
	public String getMsrCardNo(String sCardContent) {
		if (m_oMSRInterfaceConfig != null && !sCardContent.isEmpty()) {
			FuncMSR oFuncMSR = new FuncMSR();
			if(oFuncMSR.processCardContent(sCardContent, m_oMSRInterfaceConfig.getInterfaceConfig()) == FuncMSR.ERROR_CODE_NO_ERROR){
				// Get the necessary value 
				return oFuncMSR.getCardNo();
			} else
				showDialogBox(AppGlobal.g_oLang.get()._("error"), oFuncMSR.getLastErrorMessage());
		}
		return "";
	}
	
	public String getEmployeeCardMsrCardNo(String sEmployeeCardContent) {
		if (m_oEmployeeCardMSRInterfaceConfig != null && !sEmployeeCardContent.isEmpty()) {
			FuncMSR oFuncMSR = new FuncMSR();
			if(oFuncMSR.processCardContent(sEmployeeCardContent, m_oEmployeeCardMSRInterfaceConfig.getInterfaceConfig()) == FuncMSR.ERROR_CODE_NO_ERROR){
				// Get the necessary value 
				return oFuncMSR.getCardNo();
			} else
				showDialogBox(AppGlobal.g_oLang.get()._("error"), oFuncMSR.getLastErrorMessage());
		}
		return "";
	}
	
	public String getType() {
		if (m_sType.equals(FuncGamingInterface.POST_ONLINE_COMP) || m_sType.equals(FuncGamingInterface.PATRON_INQUIRY) || m_sType.equals(FuncGamingInterface.EXECUTIVE_COMP_INQUIRY) )
			return m_sType;
		return this.m_oFrameGamingInterface.getType();
	}
	
	public String getTargetValue() {
		return m_sTargetValue;
	}
	
	public String getDateOfBirth() {
		return m_sDateOfBirth;
	}


	public String getPatronCardNumber() {
		if (m_sType.equals(FuncGamingInterface.POST_ONLINE_COMP) || m_sType.equals(FuncGamingInterface.PATRON_INQUIRY) || m_sType.equals(FuncGamingInterface.EXECUTIVE_COMP_INQUIRY) )
			return m_sLastMemberEnquiryInput;
		return this.m_oFrameGamingInterface.getPatronCardNumber();
	}

	public String getPatronNumber() {
		if (m_sType.equals(FuncGamingInterface.POST_ONLINE_COMP) || m_sType.equals(FuncGamingInterface.PATRON_INQUIRY) || m_sType.equals(FuncGamingInterface.EXECUTIVE_COMP_INQUIRY) )
			return m_sLastMemberEnquiryInput;
		return this.m_oFrameGamingInterface.getPatronNumber();
	}

	
	public String getStaffId() {
		return this.m_oFrameGamingInterface.getStaffId();
	}
	
	public String getNumOfEmployees() {
		return this.m_oFrameGamingInterface.getNumOfEmployees();
	}
	
	public String getNumOfPatrons() {
		return this.m_oFrameGamingInterface.getNumOfPatrons();
	}
	
	public String getReasonCode() {
		return this.m_oFrameGamingInterface.getReasonCode();
	}
	
	public String getRemark() {
		return this.m_oFrameGamingInterface.getRemark();
	}
	
	public String getGiftCertId() {
		return this.m_oFrameGamingInterface.getGiftCertId();
	}
	
	public String getSecurityCode() {
		return this.m_oFrameGamingInterface.getSecurityCode();
	}
	
	public String getCompNumber() {
		return this.m_oFrameGamingInterface.getCompNumber();
	}
	
	public String getCouponId() {
		return this.m_oFrameGamingInterface.getCouponId();
	}
	
	/***********************/
	/*	Override Function  */
	/***********************/
	
	@Override
	public void frameGamingInterfaceResult_clickEnquiry(String sEnquiryValue, boolean bIsSwipeCard) {
		//Input Validation
		boolean bInputInvaild = false;
		String sErrorMessage = "";
		
		//init Last Enquiry Result
		initLastMemberInfo();
		
		if (m_sType.equals(FuncGamingInterface.EXECUTIVE_COMP_INQUIRY) || m_sType.equals(FuncGamingInterface.PATRON_INQUIRY) || m_sType.equals(FuncGamingInterface.POST_ONLINE_COMP)) {
			if(!bIsSwipeCard) {
				if(sEnquiryValue.length() > 8) {
					sErrorMessage = AppGlobal.g_oLang.get()._("card_number_exceed_8_chars");
					bInputInvaild = true;
				} else if (m_sInputControl.equals(FuncMSR.SWIPE_CARD_CONTROL_SWIPE_ONLY)) {
					sErrorMessage = AppGlobal.g_oLang.get()._("support_swipe_card_only");
					bInputInvaild = true;
				}
			} else if(bIsSwipeCard) {
				if (sEnquiryValue.length() > 20) {
					sErrorMessage = AppGlobal.g_oLang.get()._("card_number_exceed_20_chars");
					bInputInvaild = true;
				} else if (m_sInputControl.equals(FuncMSR.SWIPE_CARD_CONTROL_MANUAL_INPUT_ONLY)) {
					sErrorMessage = AppGlobal.g_oLang.get()._("support_key_in_input_only");
					bInputInvaild = true;
				}
			}
		}
		
		if(bInputInvaild) {
			showDialogBox(AppGlobal.g_oLang.get()._("error"), sErrorMessage);
			
			m_oFrameGamingInterface.setMemberButtonVisible(false);
			
			m_oFrameGamingInterface.clearEnquiryTextBoxValue();
			m_oFrameGamingInterface.clearEnquiryResultList();
			return;
		}
		
		//Request
		HashMap<String, String> oEnquiryInfo = new HashMap<>();
		
		oEnquiryInfo.put("interfaceId", String.valueOf(m_oPosInterfaceConfig.getInterfaceId()));
		oEnquiryInfo.put("interfaceCode", m_oPosInterfaceConfig.getInterfaceCode());
		
		if (m_sType.equals(FuncGamingInterface.EXECUTIVE_COMP_INQUIRY)) {
			oEnquiryInfo.put("compNum", sEnquiryValue);
			oEnquiryInfo.put("enquiryType", FuncGamingInterface.EXECUTIVE_COMP_INQUIRY);
		}
		
		if (m_sType.equals(FuncGamingInterface.PATRON_INQUIRY) || m_sType.equals(FuncGamingInterface.POST_ONLINE_COMP)) {
			oEnquiryInfo.put("cardNo", bIsSwipeCard ? sEnquiryValue : "");
			oEnquiryInfo.put("patronNum", bIsSwipeCard ? "" : sEnquiryValue);
			oEnquiryInfo.put("inputMethod", bIsSwipeCard ? FuncGamingInterface.SWIPE_CARD : FuncGamingInterface.KEYIN);
			oEnquiryInfo.put("enquiryType", FuncGamingInterface.PATRON_INQUIRY);
		}
		
		//Get Response
		boolean bIsSuccess = false;
		FuncGamingInterface oFuncGamingInterface = null;
		
		oFuncGamingInterface = new FuncGamingInterface();
		bIsSuccess = oFuncGamingInterface.doGemsGamingInterfacePosting(oEnquiryInfo);
		
		//Not Success
		if (!bIsSuccess) {
			sErrorMessage = "";
			if(oFuncGamingInterface == null)
				sErrorMessage = AppGlobal.g_oLang.get()._("gaming_interface_not_found");
			else if (oFuncGamingInterface != null)
				sErrorMessage = oFuncGamingInterface.getLastErrorMessage();
		
			showDialogBox(AppGlobal.g_oLang.get()._("error"), sErrorMessage);
			
			m_oFrameGamingInterface.setMemberButtonVisible(false);
			
			m_oFrameGamingInterface.clearEnquiryTextBoxValue();
			m_oFrameGamingInterface.clearEnquiryResultList();	
			
			return;
		}
		
		//Success
		if (bIsSuccess) {
			if (m_sType.equals(FuncGamingInterface.EXECUTIVE_COMP_INQUIRY)) {
				String sUserNumber = oFuncGamingInterface.m_oLastGamingPostResponseInfo.sUserNumber;
				String sBalancePerTransaction = oFuncGamingInterface.m_oLastGamingPostResponseInfo.dBalancePerTransaction.toPlainString();
				String sBalancePerDay = oFuncGamingInterface.m_oLastGamingPostResponseInfo.dBalancePerDay.toPlainString();
				String sBalancePerMonth = oFuncGamingInterface.m_oLastGamingPostResponseInfo.dBalancePerMonth.toPlainString();
				
				HashMap<String, String> oResponse = new HashMap<>();
				oResponse.put("userNumber", sUserNumber);
				oResponse.put("balancePerTransaction", sBalancePerTransaction);
				oResponse.put("balancePerDay", sBalancePerDay);
				oResponse.put("balancePerMonth", sBalancePerMonth);
				
				m_oFrameGamingInterface.updateEnquiryResult(oResponse);
				m_oFrameGamingInterface.clearEnquiryTextBoxValue();
			}
			
			if (m_sType.equals(FuncGamingInterface.PATRON_INQUIRY) || m_sType.equals(FuncGamingInterface.POST_ONLINE_COMP)) {
				String sMemberNumber = oFuncGamingInterface.m_oLastGamingPostResponseInfo.sMemberNumber;
				String sMemberName = oFuncGamingInterface.m_oLastGamingPostResponseInfo.sMemberName;
				String sPointsTotal = oFuncGamingInterface.m_oLastGamingPostResponseInfo.sPointsTotal;
				String sPointsDepartment = oFuncGamingInterface.m_oLastGamingPostResponseInfo.dPointsDepartment.toPlainString();
				
				HashMap<String, String> oResponse = new HashMap<>();
				oResponse.put("memberNumber", sMemberNumber);
				oResponse.put("memberName", sMemberName);
				oResponse.put("pointsTotal", sPointsTotal);
				oResponse.put("pointsDepartment", sPointsDepartment);
				
				String sMemberPhoto = "";
				String sMemberSignature = "";
				if(oFuncGamingInterface.m_oLastGamingPostResponseInfo.sMemberPhoto != null) {
					sMemberPhoto = oFuncGamingInterface.m_oLastGamingPostResponseInfo.sMemberPhoto;
					oResponse.put("memberPhoto", sMemberPhoto);
				}
				if(oFuncGamingInterface.m_oLastGamingPostResponseInfo.sMemberSignature != null) {
					sMemberSignature = oFuncGamingInterface.m_oLastGamingPostResponseInfo.sMemberSignature;
					oResponse.put("memberSignature", sMemberSignature);
				}
				
				//For Post Online Comp display check balance
				if (m_sType.equals(FuncGamingInterface.POST_ONLINE_COMP)) {
					if(m_oFuncCheck != null && m_oFuncPayment != null) {
						BigDecimal dRemainingCheckBalance = m_oFuncPayment.getCurrentBalance();
						if(dRemainingCheckBalance.compareTo(oFuncGamingInterface.m_oLastGamingPostResponseInfo.dPointsDepartment) == 1)
							oResponse.put("checkBalance", sPointsDepartment);
						else
							oResponse.put("checkBalance", dRemainingCheckBalance.toPlainString());
					}
				}
				
				m_oFrameGamingInterface.updateEnquiryResult(oResponse);
				m_oFrameGamingInterface.clearEnquiryTextBoxValue();
				
				//Record the Result
				m_sLastMemberEnquiryInput = sEnquiryValue;
				m_sLastInputType = bIsSwipeCard ? FuncGamingInterface.SWIPE_CARD : FuncGamingInterface.KEYIN;
				m_oLastGamingPostResponseInfo = oFuncGamingInterface.getResponseInfo();
				
				// Update UI based on the GEMS function
				if(m_sType.equals(FuncGamingInterface.PATRON_INQUIRY)  && !m_bIsTableFloorPlanVisible && m_oFuncCheck != null)
					m_oFrameGamingInterface.changeSpecialEnquiryScreenForGems(FrameGamingInterface.TYPE_GEMS_ENQUIRY_SCREEN_PATRON_INQUIRY);
				else if(m_sType.equals(FuncGamingInterface.POST_ONLINE_COMP)) {
					m_oFrameGamingInterface.changeSpecialEnquiryScreenForGems(FrameGamingInterface.TYPE_GEMS_ENQUIRY_SCREEN_POST_ONLINE_COMP_PAYMENT_ONLINE);
					//Set Default Value to Check Balance
					m_oFrameGamingInterface.setOnlineCompTextBoxValue(oResponse.get("checkBalance"));
				}
			}
		}
	}
	
	@Override
	public void frameGamingInterfaceResult_clickSetMember() {
		String sErrorMessage = "";
		
		//Ask Confirm to clear member
		if(!getUserConfirm(AppGlobal.g_oLang.get()._("set_member"), AppGlobal.g_oLang.get()._("confirm_to_set_member")+"?"))
			return;
		
		//Already have other interface / same interface but different vendors set the member, or handle as the init state?
		//Enter : Order Page,	Type: PARTON_INQUIRY,	FuncCheck:	Exist
		if(!(m_sType.equals(FuncGamingInterface.PATRON_INQUIRY) && !m_bIsTableFloorPlanVisible && m_oFuncCheck != null)) {
			sErrorMessage = AppGlobal.g_oLang.get()._("set_member_only_in_ordering_panel");
			showDialogBox(AppGlobal.g_oLang.get()._("error"), sErrorMessage);
			return;
		}
		//Member Enquiry Result Exist
		if(m_sLastMemberEnquiryInput.isEmpty() || m_sLastInputType.isEmpty() || m_oLastGamingPostResponseInfo == null) {
			sErrorMessage = AppGlobal.g_oLang.get()._("no_result_is_found");
			showDialogBox(AppGlobal.g_oLang.get()._("error"), sErrorMessage);
			return;
		}
		
		//Ready to Set Member
		ArrayList<HashMap<String, String>> oCheckExtraInfos = new ArrayList<HashMap<String, String>>();
		
		if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GEMS)) {
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_INTERFACE_ID, "0", Integer.toString(m_oPosInterfaceConfig.getInterfaceId()));
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_INPUT_TYPE, "0", m_sLastInputType);
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, "0", m_oLastGamingPostResponseInfo.sMemberNumber);
			if(m_sLastInputType.equals(FuncGamingInterface.SWIPE_CARD))
				oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_CARD_NO, "0", m_sLastMemberEnquiryInput);
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_MEMBER_NAME, "0", m_oLastGamingPostResponseInfo.sMemberName);
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_POINTS_BALANCE, "0", m_oLastGamingPostResponseInfo.sPointsTotal);
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_POINTS_DEPARTMENT, "0", m_oLastGamingPostResponseInfo.dPointsDepartment.toPlainString());
		}
		
		//Update the Check Extra Info
		if (!oCheckExtraInfos.isEmpty())
			m_oFuncCheck.saveGamingInterfaceExtraInfo(true, oCheckExtraInfos);
		
		this.finishShow();
	}
	
	@Override
	public void frameGamingInterfaceResult_clickClearMember() {
		//Ask Confirm to clear member
		if(!getUserConfirm(AppGlobal.g_oLang.get()._("clear_member"), AppGlobal.g_oLang.get()._("confirm_to_clear_member")+"?"))
			return;
		
		String sErrorMessage = "";
		//Enter : Order Page,	Type: PARTON_INQUIRY,	FuncCheck:	Exist
		if(!(m_sType.equals(FuncGamingInterface.PATRON_INQUIRY) && !m_bIsTableFloorPlanVisible && m_oFuncCheck != null)) {
			sErrorMessage = AppGlobal.g_oLang.get()._("cannot_clear_member");
			showDialogBox( AppGlobal.g_oLang.get()._("error"), sErrorMessage);
			return;
		}
		
		//Check if member attached
		String sInterfaceId = "";
		if(m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0))
			sInterfaceId = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID);
		
		if (sInterfaceId == null || sInterfaceId.isEmpty()) {
			sErrorMessage = AppGlobal.g_oLang.get()._("gaming_interface_not_found");
			showDialogBox( AppGlobal.g_oLang.get()._("error"), sErrorMessage);
			return;
		}
		
		if (!sInterfaceId.equals(Integer.toString(m_oPosInterfaceConfig.getInterfaceId()))) {
			sErrorMessage = AppGlobal.g_oLang.get()._("cannot_clear_member");
			showDialogBox( AppGlobal.g_oLang.get()._("error"), sErrorMessage);
			return;
		}
		
		if (!m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0) ||
				m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER).isEmpty()) {
			sErrorMessage = AppGlobal.g_oLang.get()._("no_member_is_attached");
			showDialogBox( AppGlobal.g_oLang.get()._("error"), sErrorMessage);
			return;
		}
		
		//Ready to Clear Member?
		ArrayList<HashMap<String, String>> oCheckExtraInfos = new ArrayList<HashMap<String, String>>();
		
		if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GEMS)) {
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_INTERFACE_ID, "0", "");
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_INPUT_TYPE, "0", "");
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, "0", "");
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_CARD_NO, "0", "");
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_MEMBER_NAME, "0", "");
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_POINTS_BALANCE, "0", "");
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_POINTS_DEPARTMENT, "0", "");
		}
		//Update the Check Extra Info
		if (!oCheckExtraInfos.isEmpty())
			m_oFuncCheck.saveGamingInterfaceExtraInfo(true ,oCheckExtraInfos);
		
		this.finishShow();
	}
	
	public void frameGamingInterfaceResult_clickSubmit(String sOnlineCompRedeem, String sDateOfBirth) {
		
		String sErrorMessage = "";
		
		if (sOnlineCompRedeem.isEmpty() || sDateOfBirth.isEmpty()) {
			sErrorMessage = AppGlobal.g_oLang.get()._("missing_input_field");
			showDialogBox(AppGlobal.g_oLang.get()._("error"), sErrorMessage);
			return;
		}
		
		//Checking for Date Format
		if (sDateOfBirth.length() != 4 || !Pattern.compile("^\\d+$").matcher(sDateOfBirth).find()) {
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_date_format");
			showDialogBox(AppGlobal.g_oLang.get()._("error"), sErrorMessage);
			return;
		}
		
		SimpleDateFormat oDateFormat = new SimpleDateFormat("MMdd");
		oDateFormat.setLenient(false);
		
		try {
			oDateFormat.parse(sDateOfBirth.trim());
		} catch (Exception e) {
			if(!sDateOfBirth.equals("0229")) {
				sErrorMessage = AppGlobal.g_oLang.get()._("invalid_date_format");
				showDialogBox(AppGlobal.g_oLang.get()._("error"), sErrorMessage);
				return;
			}
		}
		
		// Maximum Redeem Amount default online posting
		BigDecimal dMaxRedeemAmount = m_oFuncPayment.getCurrentBalance();
		
		if (m_sPostingType.equals(FuncGamingInterface.ONLINE_POSTING))
			dMaxRedeemAmount = m_oFuncPayment.getCurrentBalance()
					.compareTo(m_oLastGamingPostResponseInfo.dPointsDepartment) == 1
							? m_oLastGamingPostResponseInfo.dPointsDepartment : m_oFuncPayment.getCurrentBalance();
		
		BigDecimal dTargetAmount = BigDecimal.ZERO;
		
		// Checking for Redeem Amount
		try {
			dTargetAmount = new BigDecimal(sOnlineCompRedeem);
		} catch (NumberFormatException e) {
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_input");
			showDialogBox(AppGlobal.g_oLang.get()._("error"), sErrorMessage);
			return;
		}
		
		if (dTargetAmount.compareTo(BigDecimal.ZERO) < 0) {
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_input");
			showDialogBox(AppGlobal.g_oLang.get()._("error"), sErrorMessage);
			return;
		} else if (dTargetAmount.compareTo(dMaxRedeemAmount) > 0) {
			sErrorMessage = AppGlobal.g_oLang.get()._("redeem_amount_should_not_exceed_point_department");
			showDialogBox(AppGlobal.g_oLang.get()._("error"), sErrorMessage);
			return;
		}
		
		String sOfflineInput = "";
		
		if (m_sPostingType.equals(FuncGamingInterface.OFFLINE_POSTING)) {
			// Special handling for patron id & Remark checking in offline mode
			if (m_oFrameGamingInterface.isSwipedCard()) {
				sOfflineInput = m_oFrameGamingInterface.getEnquiryTextBoxValue();
				
				if (sOfflineInput.length() > 20) {
					sErrorMessage = AppGlobal.g_oLang.get()._("card_number_exceed_20_chars");
				}
			} else {
				sOfflineInput = m_oFrameGamingInterface.getEnquiryTextBoxValue();
				if (sOfflineInput.length() > 8) {
					sErrorMessage = AppGlobal.g_oLang.get()._("card_number_exceed_8_chars");
				}
			}
			
			if (sOfflineInput.isEmpty())
				sErrorMessage = AppGlobal.g_oLang.get()._("please_input_card_no");
			
			if (!sErrorMessage.isEmpty()) {
				showDialogBox(AppGlobal.g_oLang.get()._("error"), sErrorMessage);
				return;
			}
			
			if (m_oFrameGamingInterface.getRemark().equals(AppGlobal.g_oLang.get()._("please_select_the_type"))) {
				showDialogBox(AppGlobal.g_oLang.get()._("error"), AppGlobal.g_oLang.get()._("please_select_remark"));
				return;
			}
		}
		
		// Pass the checking, store the result
		m_sTargetValue = dTargetAmount.toPlainString(); // dTargetAmount.setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(),
														// BigDecimal.ROUND_DOWN).toString()
														// - For printing use
														// ROUND_DOWN, for
														// payment use
														// ROUND_HALF_UP...
		m_sDateOfBirth = sDateOfBirth;
		
		if (m_sPostingType.equals(FuncGamingInterface.OFFLINE_POSTING)) {
			m_sLastMemberEnquiryInput = sOfflineInput;
			m_sLastInputType = m_oFrameGamingInterface.isSwipedCard() ? FuncGamingInterface.SWIPE_CARD
					: FuncGamingInterface.KEYIN;
			m_sOfflineRemark = m_oFrameGamingInterface.getRemark();
		}
		
		this.finishShow();
	}
	
	@Override
	public void frameGamingInterfaceResult_clickBack() {
		m_bCancel = true;
		this.finishShow();
	}
	
	@Override
	public void frameGamingInterfacePaymentEnter_clicked() {
		// TODO Auto-generated method stub
		this.finishShow();
	}
	
	@Override
	public void frameGamingInterfaceResult_clickCardEnquiry(String sEnquiryValue, boolean bIsSwipeCard) {
		boolean bIsSuccess = false;
		FuncGamingInterface oFuncGamingInterface = new FuncGamingInterface(m_oPosInterfaceConfig);
		
		for(FormGamingInterfaceListener listener: listeners) {
			bIsSuccess = listener.formGamingInterfaceResult_clickCardEnquiry(sEnquiryValue, bIsSwipeCard, oFuncGamingInterface);
			break;
		}
		
		if(bIsSuccess) {
			m_oFrameGamingInterface.setInputType(FuncGamingInterface.SWIPE_CARD);
			
			if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_BALLY))
				m_oFrameGamingInterface.setApplyDiscountButtonVisible(bIsSuccess);
			
			HashMap<String, String> oResponse = addCardDetail(oFuncGamingInterface.m_oLastCardEnquiryResponseInfo);
			m_oFrameGamingInterface.updateCardEnquiryResult(oResponse);
		} else {
			if(!m_oFrameGamingInterface.getClearDiscountButtonVisible())
				m_oFrameGamingInterface.clearMemberDetailDisplayAndApplyDiscountButton();
		}
	}
	
	public HashMap<String, String> addCardDetail(FuncGamingInterface.GamingInterfaceCardEnquiryInfo oEnquiryInfo){
		HashMap<String, String> oResponse = new HashMap<>();
		
		if(m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_BALLY))
			oResponse = addCardDetailForBally(oEnquiryInfo);
		else if(m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_SJM))
			oResponse = addCardDetailForSjm(oEnquiryInfo);
		
		return oResponse;
	}
	
	private HashMap<String, String> addCardDetailForBally(FuncGamingInterface.GamingInterfaceCardEnquiryInfo oEnquiryInfo){
		HashMap<String, String> oResponse = new HashMap<>();
		oResponse.put("accountNumber", oEnquiryInfo.sAccountNumber);
		oResponse.put("cardType", oEnquiryInfo.sCardType);
		oResponse.put("clubState", oEnquiryInfo.sClubState);
		oResponse.put("title", oEnquiryInfo.sTitle);
		oResponse.put("firstName", oEnquiryInfo.sFirstName);
		oResponse.put("lastName", oEnquiryInfo.sLastName);
		oResponse.put("chineseName", oEnquiryInfo.sChineseName);
		oResponse.put("discount", oEnquiryInfo.sDiscountPercentage);
		
		return oResponse;
	}
	
	private HashMap<String, String> addCardDetailForSjm(FuncGamingInterface.GamingInterfaceCardEnquiryInfo oEnquiryInfo){
		HashMap<String, String> oResponse = new HashMap<>();
		oResponse.put("accountNumber", oEnquiryInfo.sAccountNumber);
		oResponse.put("clubState", oEnquiryInfo.sClubState);
		oResponse.put("title", oEnquiryInfo.sTitle);
		oResponse.put("firstName", oEnquiryInfo.sFirstName);
		oResponse.put("lastName", oEnquiryInfo.sLastName);
		oResponse.put("chineseName", oEnquiryInfo.sChineseName);
		
		
		if (oEnquiryInfo.oInfoList.size() > 0) {
			oResponse.put("InfoListSize", String.valueOf(oEnquiryInfo.oInfoList.size()));
			int iIndexOfBucket = 0;
			for (Entry<String, String> entry : oEnquiryInfo.oInfoList.entrySet()) {
				oResponse.put("typeName" + iIndexOfBucket, entry.getKey());
				oResponse.put("balance" + iIndexOfBucket, entry.getValue());
				iIndexOfBucket++;
			}
		}
		
		return oResponse;
	}
	
	@Override
	public void frameGamingInterfaceResult_clickApplyDiscount() {
		if(m_bIsEnquiryCard) {
			if(getUserConfirm(AppGlobal.g_oLang.get()._("replace_card"), AppGlobal.g_oLang.get()._("confirm_to_replace_card")+"?")) {
				if(!clearDiscount())
					//fail to clear discount / card
					return;
			}
			else
				//user cancel
				return;
		}

		String sDiscountPercentage = m_oFrameGamingInterface.getMemberDetail().get("discountPercentage").replace("%", "");
		String sDiscountCode = "";

		if(m_oPosInterfaceConfig.getInterfaceConfig() != null && m_oPosInterfaceConfig.getInterfaceConfig().has("general_setup")
				&& m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").has("discount_code"))
			sDiscountCode = m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup")
			.optJSONObject("params").optJSONObject("discount_code").optString("value", "");


		ArrayList <PosCheckExtraInfo> oDiscountCheckExtraInfoList = new ArrayList<PosCheckExtraInfo>();
		PosCheckExtraInfo oTempExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.BY_DISCOUNT,
				PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_DISCOUNT_RATE, sDiscountPercentage);
		oDiscountCheckExtraInfoList.add(oTempExtraInfo);

		for(FormGamingInterfaceListener listener: listeners) {
			//apply discount
			if(!listener.formGamingInterfaceResult_clickApplyDiscount(sDiscountCode, sDiscountPercentage, oDiscountCheckExtraInfoList))
				return;
			break;
		}

		//add account number into extra info
		HashMap<String, String> oMemberDetail = m_oFrameGamingInterface.getMemberDetail();
		ArrayList<HashMap<String, String>> oCheckExtraInfos = new ArrayList<HashMap<String, String>>();
		oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_INTERFACE_ID, "0", Integer.toString(m_oPosInterfaceConfig.getInterfaceId()));
		oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_CARD_TYPE_NAME, "0", oMemberDetail.get("cardType"));
		oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_MEMBER_NAME, "0", oMemberDetail.get("firstName"));
		oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_MEMBER_LAST_NAME, "0", oMemberDetail.get("lastName"));
		oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER, "0", oMemberDetail.get("accountNumber"));
		oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_INPUT_TYPE, "0", m_oFrameGamingInterface.getInputType());
		m_oFuncCheck.saveGamingInterfaceExtraInfo(true, oCheckExtraInfos);

		//add action log
		AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.gaming_enquiry.name(),
				PosActionLog.ACTION_RESULT_SUCCESS, m_oFuncCheck.getTableNoWithExtensionForDisplay(),
				AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(),
				AppGlobal.g_oFuncOutlet.get().getOutletId(),
				AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(),
				AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(),
				AppGlobal.g_oFuncStation.get().getStationId(), m_oFuncCheck.getCheckId(), "", "", "", "",
				"account number: " + oMemberDetail.get("accountNumber") + " discount code: " + sDiscountCode);

		// handle action log
		AppGlobal.g_oActionLog.get().handleActionLog(false);

		m_oFrameGamingInterface.setApplyDiscountButtonVisible(false);
		m_oFrameGamingInterface.setMemberDetailCommonBasketVisible(true);
		m_bIsEnquiryCard = true;
	}
	
	@Override
	public void frameGamingInterfaceResult_clickClearDiscount() {
		if(!getUserConfirm(AppGlobal.g_oLang.get()._("clear_discount"), AppGlobal.g_oLang.get()._("confirm_to_clear_discount")+"?"))
			return;
		clearDiscount();
	}
	public boolean clearDiscount() {
		if (voidDiscountAfterClearCardNumber(PosCheckExtraInfo.VARIABLE_DISCOUNT_RATE)) {
			m_oFuncCheck.removeCheckExtraInfoFromList(PosCheckExtraInfo.BY_DISCOUNT, PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_DISCOUNT_RATE);
			
			ArrayList<HashMap<String, String>> oCheckExtraInfos = new ArrayList<HashMap<String, String>>();
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, "0", "");
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER, "0", "");
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_INTERFACE_ID, "0", "");
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_CARD_TYPE_NAME, "0", "");
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_MEMBER_NAME, "0", "");
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos ,PosCheckExtraInfo.VARIABLE_MEMBER_LAST_NAME, "0", "");
			oCheckExtraInfos = addExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_INPUT_TYPE, "0", "");
			m_oFuncCheck.saveGamingInterfaceExtraInfo(true, oCheckExtraInfos);
			
			m_oFrameGamingInterface.clearMemberDetailDisplayAndApplyDiscountButton();
			m_bIsEnquiryCard = false;
			return true;
		}
		return false;
	}
	
	@Override
	public String frameGamingInterfaceResult_getMsrCardNo(String sCardContent) {
		String sMSRCardNumber = "";
		sMSRCardNumber = getMsrCardNo(sCardContent);
		return sMSRCardNumber;
	}
	
	private PosCheckExtraInfo constructCheckExtraInfo(String sBy, String sSection, String sVariable, String sValue) {
		PosCheckExtraInfo oPosCheckExtraInfo = new PosCheckExtraInfo();
		oPosCheckExtraInfo.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oPosCheckExtraInfo.setBy(sBy);
		oPosCheckExtraInfo.setSection(sSection);
		oPosCheckExtraInfo.setVariable(sVariable);
		oPosCheckExtraInfo.setValue(sValue);
		return oPosCheckExtraInfo;
	}

	private boolean voidDiscountAfterClearCardNumber(String sVariableName) {
		List<PosCheckDiscount> oDiscountList = m_oFuncCheck.getCheckDiscountList();
		List<HashMap<String, Integer>> oUpdateItemIndexList = new ArrayList<HashMap<String, Integer>>();
		int iDiscountIndex = 0;
		String sValue = "", sErrorMessage = "";
		String sInterfaceId = m_oFuncCheck.getCheckExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_GAMING_INTERFACE,
				PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0);
		String sAccountNumber = m_oFuncCheck.getCheckExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_GAMING_INTERFACE,
				PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER, 0);
		boolean bIsAppliedDiscount = false;
		
		//get applied discount
		for(PosCheckDiscount oDiscount : oDiscountList) {
			iDiscountIndex++;
			sValue = oDiscount.getCheckExtraInfoValueBySectionVariable(PosCheckExtraInfo.SECTION_GAMING_INTERFACE,
					PosCheckExtraInfo.VARIABLE_DISCOUNT_RATE);

			if(!sValue.isEmpty()) {
				if (sInterfaceId == null || sInterfaceId.isEmpty()) {
					sErrorMessage = AppGlobal.g_oLang.get()._("no_gaming_interface_attached_to_check");
					showDialogBox( AppGlobal.g_oLang.get()._("error"), sErrorMessage);
					return false;
				}
				
				else if (!sInterfaceId.equals(Integer.toString(m_oPosInterfaceConfig.getInterfaceId()))) {
					sErrorMessage = AppGlobal.g_oLang.get()._("clear_member_on_different_interface");
					showDialogBox( AppGlobal.g_oLang.get()._("error"), sErrorMessage);
					return false;
				}
				else if (!m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER, 0) ||
						m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER).isEmpty()) {
					sErrorMessage = AppGlobal.g_oLang.get()._("no_card_attached");
					showDialogBox( AppGlobal.g_oLang.get()._("error"), sErrorMessage);
					return false;
				}
				
				bIsAppliedDiscount = true;
				oUpdateItemIndexList = m_oFuncCheck.getSectionItemIndexWithAppliedCheckDiscount(iDiscountIndex);
				return m_oFuncCheck.voidDiscount("check", PosDiscountType.USED_FOR_DISCOUNT, oUpdateItemIndexList, iDiscountIndex, 0, null);
			}
		}
		
		// case: discount have been removed and card number is not yet removed
		if(!bIsAppliedDiscount && sInterfaceId != null && sInterfaceId.equals(Integer.toString(m_oPosInterfaceConfig.getInterfaceId()))
				&& sAccountNumber != null && !sAccountNumber.isEmpty()) {
			if(getUserConfirm(AppGlobal.g_oLang.get()._("clear_card"), AppGlobal.g_oLang.get()._("discount_is_removed") + System.lineSeparator() + AppGlobal.g_oLang.get()._("confirm_to_clear_card")+"?"))
				return true;
		}
		
		return false;
		
	}
	@Override
	public String frameGamingInterfaceResult_getEmployeeCardMsrCardNo(String sEmployeeCardContent) {
		String sEmployeeCardMSRCardNumber = "";
		sEmployeeCardMSRCardNumber = getEmployeeCardMsrCardNo(sEmployeeCardContent);
		return sEmployeeCardMSRCardNumber;
	}
	
}
