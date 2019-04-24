package app;

import java.util.ArrayList;
import java.util.HashMap;

import commonui.FormDialogBox;
import commonui.FormInputBox;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import om.InfVendor;
import om.PosInterfaceConfig;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

interface FrameGamingInterfaceListener {
	void frameGamingInterfaceResult_clickBack();
	void frameGamingInterfacePaymentEnter_clicked();
	
	//For Execute Comp/Patron Inquiry & Post Online Comp 
	void frameGamingInterfaceResult_clickEnquiry(String sCardNumber, boolean bIsSwipeCard);
	void frameGamingInterfaceResult_clickSetMember();
	void frameGamingInterfaceResult_clickClearMember();
	void frameGamingInterfaceResult_clickSubmit(String sOnlineCompRedeem, String sDateOfBirth);
	
	//For Galaxy Bally Card Enquiry
	void frameGamingInterfaceResult_clickCardEnquiry(String sEnquiryValue, boolean bIsSwipeCard);
	void frameGamingInterfaceResult_clickApplyDiscount();
	void frameGamingInterfaceResult_clickClearDiscount();
	
	String frameGamingInterfaceResult_getMsrCardNo(String sCardContent);
	String frameGamingInterfaceResult_getEmployeeCardMsrCardNo(String sEmployeeCardContent);
}

public class FrameGamingInterface extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener, FrameCommonPageContainerListener {
	private TemplateBuilder m_oTemplateBuilder;
	
	//Enquiry Frame height
	//static final int ENQUIRY_ORIGINAL_HEIGHT = 210;
	static final int ENQUIRY_EXTENSION_HEIGHT = 550;
	
	// special type of GEMS enquiry screen
	static final String TYPE_GEMS_ENQUIRY_SCREEN_PATRON_INQUIRY = "screen_for_patron_inquiry";
	static final String TYPE_GEMS_ENQUIRY_SCREEN_COMP_INQUIRY = "screen_for_comp_enquiry";
	static final String TYPE_GEMS_ENQUIRY_SCREEN_POST_ONLINE_COMP_PAYMENT_ONLINE = "screen_for_post_online_comp_online_payment";
	static final String TYPE_GEMS_ENQUIRY_SCREEN_POST_ONLINE_COMP_PAYMENT_OFFLINE = "screen_for_post_online_comp_offline_payment";
	
	// Virtual UI Element
	private FrameTitleHeader m_oTitleHeader;
	private FrameCommonBasket m_oResultListCommonBasket;
	
	private VirtualUIFrame m_oEnquiryFrame;
	private VirtualUIFrame m_oEnquiryResultFrame;
	
	private VirtualUILabel m_oEnquiryNumberLabel;
	private VirtualUILabel m_oOnlineCompLabelTag;
	private VirtualUILabel m_oPartonDateLabelTag;
	private VirtualUILabel m_oRemarkLabelTag;
	private VirtualUILabel m_oLblSelectedRemark;
	private VirtualUITextbox m_oTxtboxEnquiryNumber;
	private VirtualUITextbox m_oTxtboxOnlineComp;
	private VirtualUITextbox m_oTxtboxPatronDate;

	private VirtualUIButton m_oButtonEnquiry;
	private VirtualUIButton m_oButtonSetMember;
	private VirtualUIButton m_oButtonClearMember;
	private VirtualUIButton m_oButtonSubmit;
	
	// Checking Variable
	private int m_iRequestTimeout;
	private boolean m_bIsSwipedCard;
	
	private FrameCommonPageContainer m_oCommonPageTabList;
	
	// Photo and Signature
	private VirtualUIImage m_oImageMemberPhoto;
	private VirtualUIImage m_oImageMemberSign;
	private VirtualUIFrame m_oFrameImage;
	
	private VirtualUIFrame m_oInputPanel;
	private VirtualUIFrame m_oDisplayPanel;
	
	private VirtualUITextbox m_oTxtboxRemark;
	private VirtualUILabel m_oLblSelectedReason;
	
	private VirtualUILabel m_oLblFirstTextBoxCoverFrame;
	
	private FrameCommonPageContainer m_oFramePageContainer;
	
	private FrameCommonBasket m_oBasketRemarkList;
	private VirtualUITextbox m_oTxtboxStaffId;
	private VirtualUITextbox m_oTxtboxNumOfPatrons;
	private VirtualUITextbox m_oTxtboxNumOfEmployees;
	private VirtualUITextbox m_oTxtboxReasonCode;
	
	private VirtualUITextbox m_oTxtboxGiftCertId;
	private VirtualUITextbox m_oTxtboxSecurityCode;
	
	private VirtualUITextbox m_oTxtboxPatronNum;
	private VirtualUITextbox m_oTxtboxCompNum;
	private VirtualUITextbox m_oTxtboxCouponId;
	
	private VirtualUIFrame m_oFramePage;
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIImage m_oImgButtonNextPage;
	
	private VirtualUIButton m_oButtonEnter;
	
	private FrameTitleHeader m_oInputPanelHeader;
	private FrameTitleHeader m_oDisplayPanelHeader;
	
	private VirtualUITextbox m_oTxtboxMemberCard;
	private VirtualUIButton m_oButtonCardEnquiry;
	private VirtualUIButton m_oButtonApplyDiscount;
	private VirtualUIButton m_oButtonClearDiscount;
	private FrameCommonBasket m_oMemberDetailCommonBasket;
	
	//Remark Variable
	private ArrayList<ArrayList<String>> m_oRemarkList;
	private ArrayList <VirtualUIFrame> m_oFrameReasonCode;
	
	private int m_iCurrentPage;
	private int m_iTotalPage;
	
	private String m_sInputControl = FuncMSR.SWIPE_CARD_NO_CONTROL;
	private String m_sType;
	private String m_sVendorKey;
	private String [] m_sReasonCode;
	
	private boolean m_bIsSwipeCard = false;
	private boolean m_bIsOnlinePosting = false;
	private boolean m_bIsTableFloorPlanVisible = false;
	
	private String m_sInputType;
	
	private static int MAX_PAGE_RECORD = 6;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameGamingInterfaceListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameGamingInterfaceListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameGamingInterfaceListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	} 
	
	public FrameGamingInterface() {
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_iRequestTimeout = 30;
		m_bIsSwipedCard = false;
	
		m_sType = "";
		m_sVendorKey = "";
		
		listeners = new ArrayList<FrameGamingInterfaceListener>();
	}
	
	public void init(PosInterfaceConfig oPosInterfaceConfig, String sType, String sInputControl, boolean bIsOnlinePosting, String [] sReasonCode, ArrayList<ArrayList<String>> oRemarkList, boolean bIsTableFloorPlanVisible) {
		m_sType = sType;
		m_sVendorKey = oPosInterfaceConfig.getInterfaceVendorKey();
		m_sInputControl = sInputControl;
		m_bIsOnlinePosting = bIsOnlinePosting;
		m_bIsTableFloorPlanVisible = bIsTableFloorPlanVisible;
		
		//m_oTemplateBuilder.loadTemplate("fraGamingInterface_gems.xml");
		String sXmlFilename = "";
		
		switch (sType) {
		case FuncGamingInterface.EXECUTIVE_COMP_INQUIRY:
		case FuncGamingInterface.PATRON_INQUIRY:
		case FuncGamingInterface.POST_ONLINE_COMP:
			sXmlFilename = "fraGamingInterface_gems_inquiry.xml";
			break;
		case FuncGamingInterface.POST_EXECUTIVE_COMP:
		case FuncGamingInterface.COMP_REDEMPTION:
		case FuncGamingInterface.GIFT_CERTIFICATE_SALE:
		case FuncGamingInterface.GIFT_CERTIFICATE_REDEMPTION:
		case FuncGamingInterface.COUPON_REDEMPTION:
		case FuncGamingInterface.POST_SALE_DETAIL:
			sXmlFilename = "fraGamingInterface_gems.xml";
			break;
		case FuncGamingInterface.CARD_ENQUIRY:
			sXmlFilename = "fraGamingInterface_bally_and_sjm.xml";
		default:
			break;
		}
		
		m_oTemplateBuilder.loadTemplate(sXmlFilename);
		
		// Load child elements from template
		/////////////////////////////////////////////////////////////////
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		
		m_oTitleHeader.addListener(this);
		this.attachChild(m_oTitleHeader);
		
		/////////////////////////////////////////////////////////////////////////
		// Left container elements
		m_oInputPanel = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oInputPanel, "fraInputPanel");
		this.attachChild(m_oInputPanel);
		
		VirtualUILabel oLblStaffId = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblStaffId, "lblStaffId");
		oLblStaffId.setValue(AppGlobal.g_oLang.get()._("staff_id") + " :");
		m_oInputPanel.attachChild(oLblStaffId);
		
		VirtualUILabel oLblNumOfPatrons = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblNumOfPatrons, "lblNumOfPatrons");
		oLblNumOfPatrons.setValue(AppGlobal.g_oLang.get()._("number_of_patrons") + " :");
		m_oInputPanel.attachChild(oLblNumOfPatrons);
		
		VirtualUILabel oLblNumOfEmployees = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblNumOfEmployees, "lblNumOfEmployees");
		oLblNumOfEmployees.setValue(AppGlobal.g_oLang.get()._("number_of_employees") + " :");
		m_oInputPanel.attachChild(oLblNumOfEmployees);
		
		VirtualUILabel oLblReasonCode = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblReasonCode, "lblReasonCode");
		oLblReasonCode.setValue(AppGlobal.g_oLang.get()._("reason") + " :");
		m_oInputPanel.attachChild(oLblReasonCode);
		
		VirtualUILabel oLblRemarks = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblRemarks, "lblRemarks");
		oLblRemarks.setValue(AppGlobal.g_oLang.get()._("remark") + " :");
		m_oInputPanel.attachChild(oLblRemarks);
		
		m_oLblSelectedReason = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblSelectedReason, "lblSelectedReason");
		m_oLblSelectedReason.setValue(AppGlobal.g_oLang.get()._("please_select_the_type"));
		m_oInputPanel.attachChild(m_oLblSelectedReason);
		
		m_oTxtboxStaffId = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxStaffId, "txtboxStaffId");
		
		m_oTxtboxStaffId.setInputType(HeroActionProtocol.View.Attribute.InputType.PASSWORD);
		
		m_oInputPanel.attachChild(m_oTxtboxStaffId);
		
		m_oTxtboxNumOfPatrons = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxNumOfPatrons, "txtboxNumOfPatrons");
		m_oTxtboxNumOfPatrons.setFocusWhenShow(false);
		m_oInputPanel.attachChild(m_oTxtboxNumOfPatrons);
		
		m_oTxtboxNumOfEmployees = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxNumOfEmployees, "txtboxNumOfEmployees");
		m_oTxtboxNumOfEmployees.setFocusWhenShow(false);
		
		m_oInputPanel.attachChild(m_oTxtboxNumOfEmployees);
		
		m_oTxtboxReasonCode = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxReasonCode, "txtboxReasonCode");
		m_oTxtboxReasonCode.setEnabled(false);
		m_oInputPanel.attachChild(m_oTxtboxReasonCode);
		
		m_oTxtboxRemark = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxRemark, "txtboxRemarks");
		m_oTxtboxRemark.setEnabled(false);
		m_oInputPanel.attachChild(m_oTxtboxRemark);
		
		m_oLblSelectedRemark = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblSelectedRemark, "lblSelectedRemark");
		m_oLblSelectedRemark.setValue(AppGlobal.g_oLang.get()._("please_select_the_type"));
		m_oInputPanel.attachChild(m_oLblSelectedRemark);
		
		/////////////////////////////////////////////////////////////////////////
		// Right container element
		m_oDisplayPanel = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oDisplayPanel, "fraDisplayPanel");
		this.attachChild(m_oDisplayPanel);
		
		//***********************************************************************
		// Page Frame
		m_oFramePage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePage, "fraPage");
		
		// Create prev page button
		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevPage");
		m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
		m_oImgButtonPrevPage.allowClick(true);
		m_oImgButtonPrevPage.setClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.allowLongClick(true);
		m_oImgButtonPrevPage.setLongClickServerRequestBlockUI(false);
		m_oFramePage.attachChild(m_oImgButtonPrevPage);
		
		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.allowLongClick(true);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		m_oFramePage.attachChild(m_oImgButtonNextPage);
		
		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oFramePage.attachChild(m_oLblPage);
		//***********************************************************************
		
		//***********************************************************************
		// HashMap to store the reason code and remark (Key = reason code, Value = List of remark)
		m_oRemarkList = new ArrayList<ArrayList<String> > ();
		
		m_oFramePageContainer = new FrameCommonPageContainer();
		m_oTemplateBuilder.buildFrame(m_oFramePageContainer, "fraResonCode");
		int iTagWidth = 100;
		if (sReasonCode != null && sReasonCode.length == 1)
			iTagWidth = 200;
		m_oFramePageContainer.init(m_oFramePageContainer.getWidth(),
				m_oFramePageContainer.getHeight(), iTagWidth, 50, 3, "#0055B8", "#999999", "", "", 20, false, true);
		m_oFramePageContainer.addListener(this);
		m_oDisplayPanel.attachChild(m_oFramePageContainer);
		
		m_oDisplayPanel.attachChild(m_oFramePage);
		/////////////////////////////////////////////////////////////////////////
		
		m_oButtonEnter = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonEnter, "butEnter");
		m_oButtonEnter.setValue(AppGlobal.g_oLang.get()._("enter"));
		m_oButtonEnter.addClickServerRequestSubmitElement(m_oTxtboxStaffId);
		m_oButtonEnter.addClickServerRequestSubmitElement(m_oTxtboxNumOfEmployees);
		m_oButtonEnter.addClickServerRequestSubmitElement(m_oTxtboxNumOfPatrons);
		m_oButtonEnter.addClickServerRequestSubmitElement(m_oTxtboxReasonCode);
		m_oButtonEnter.addClickServerRequestSubmitElement(m_oTxtboxRemark);
		m_oButtonEnter.setClickServerRequestBlockUI(true);
		this.attachChild(m_oButtonEnter);
		
		VirtualUILabel oLblGiftCertId  = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblGiftCertId, "lblGiftCertId");
		oLblGiftCertId.setValue(AppGlobal.g_oLang.get()._("gift_cert_id") + " :");
		m_oInputPanel.attachChild(oLblGiftCertId);
		
		VirtualUILabel oLblSecurityCode = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblSecurityCode, "lblSecurityCode");
		oLblSecurityCode.setValue(AppGlobal.g_oLang.get()._("security_code") + " :");
		m_oInputPanel.attachChild(oLblSecurityCode);
		
		VirtualUILabel oLblReason = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblReason, "lblReason");
		oLblReason.setValue(AppGlobal.g_oLang.get()._("reason") + " :");
		m_oDisplayPanel.attachChild(oLblReason);
		
		m_oTxtboxGiftCertId = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxGiftCertId, "txtboxGiftCertId");
		m_oTxtboxGiftCertId.setFocusWhenShow(false);
		
		m_oInputPanel.attachChild(m_oTxtboxGiftCertId);
		
		m_oTxtboxSecurityCode = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxSecurityCode, "txtboxSecurityCode");
		m_oTxtboxSecurityCode.setFocusWhenShow(false);
		m_oInputPanel.attachChild(m_oTxtboxSecurityCode);
		
		m_oTxtboxPatronNum = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxPatronNum, "txtboxPatronNum");
		
		m_oTxtboxPatronNum.setInputType(HeroActionProtocol.View.Attribute.InputType.PASSWORD);
		
		m_oInputPanel.attachChild(m_oTxtboxPatronNum);
		
		VirtualUILabel oLblPatronNum = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblPatronNum, "lblPatronNum");
		oLblPatronNum.setValue(AppGlobal.g_oLang.get()._("patron_number"));
		m_oInputPanel.attachChild(oLblPatronNum);
		
		m_oTxtboxCompNum = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxCompNum, "txtboxCompNum");
		m_oInputPanel.attachChild(m_oTxtboxCompNum);
		
		VirtualUILabel oLblCompNum = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblCompNum, "lblCompNum");
		oLblCompNum.setValue(AppGlobal.g_oLang.get()._("comp_number"));
		m_oInputPanel.attachChild(oLblCompNum);
		
		m_oTxtboxCouponId = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxCouponId, "txtboxCouponId");
		m_oInputPanel.attachChild(m_oTxtboxCouponId);
		
		VirtualUILabel oLblCouponId = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblCouponId, "lblCouponId");
		oLblCouponId.setValue(AppGlobal.g_oLang.get()._("coupon_number"));
		m_oInputPanel.attachChild(oLblCouponId);
		
		
		VirtualUILabel oLabelStaffDetail = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelStaffDetail, "lblStaffDetail");
		oLabelStaffDetail.setValue(AppGlobal.g_oLang.get()._("staff_detail"));
		m_oDisplayPanel.attachChild(oLabelStaffDetail);
		
		m_oLblFirstTextBoxCoverFrame = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblFirstTextBoxCoverFrame, "lblTextBoxCoverFrame");
		m_oLblFirstTextBoxCoverFrame.setEnabled(false);
		m_oLblFirstTextBoxCoverFrame.setVisible(false);
		
		if(sInputControl.equals(FuncMSR.SWIPE_CARD_CONTROL_SWIPE_ONLY))
			m_oLblFirstTextBoxCoverFrame.setVisible(true);
		
		//hide the label if the input control is not set as "swipe card only"
		
		m_oInputPanel.attachChild(m_oLblFirstTextBoxCoverFrame);
		
		if(!bIsOnlinePosting || m_sType.equals(FuncGamingInterface.POST_EXECUTIVE_COMP)) 
			initDisplayFrame(oRemarkList, sReasonCode);
		
		// Enquiry Block
		m_oEnquiryFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oEnquiryFrame, "fraEnquiryBlock");
		this.attachChild(m_oEnquiryFrame);
		
		// Enquiry Label
		VirtualUILabel oLabelTag = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelTag, "lblEnquiryTag");
		oLabelTag.setValue(AppGlobal.g_oLang.get()._("enquiry"));
		m_oEnquiryFrame.attachChild(oLabelTag);
		
		VirtualUIFrame oEnquiryTagIndicator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oEnquiryTagIndicator, "fraEnquiryIndicator");
		m_oEnquiryFrame.attachChild(oEnquiryTagIndicator);
		
		oLabelTag = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelTag, "lblEnquiryFieldTag");
		if(sType.equals(FuncGamingInterface.EXECUTIVE_COMP_INQUIRY))
			oLabelTag.setValue(AppGlobal.g_oLang.get()._("staff_id")+" :");
		else if(sType.equals(FuncGamingInterface.PATRON_INQUIRY) || sType.equals(FuncGamingInterface.POST_ONLINE_COMP))
			oLabelTag.setValue(AppGlobal.g_oLang.get()._("patron_number")+" :");
		m_oEnquiryFrame.attachChild(oLabelTag);
		
		// Enquiry number textbox
		m_oTxtboxEnquiryNumber = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxEnquiryNumber, "txtboxEnquiryNumber");
		
		m_oTxtboxEnquiryNumber.setFocusWhenShow(true);
		if(sInputControl.equals(FuncMSR.SWIPE_CARD_CONTROL_SWIPE_ONLY))
			m_oTxtboxEnquiryNumber.setFocusWhenShow(false);
		
		m_oTxtboxEnquiryNumber.setInputType(HeroActionProtocol.View.Attribute.InputType.PASSWORD);
		
		m_oEnquiryFrame.attachChild(m_oTxtboxEnquiryNumber);
		
		// Enquiry Label
		m_oEnquiryNumberLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oEnquiryNumberLabel, "lblEnquiryNumber");
		m_oEnquiryNumberLabel.setVisible(false);
		if (m_sType.equals(FuncGamingInterface.POST_ONLINE_COMP) && !m_bIsOnlinePosting && m_sInputControl.equals(FuncMSR.SWIPE_CARD_CONTROL_SWIPE_ONLY)) {
			m_oEnquiryNumberLabel.setVisible(true);
		}
		m_oEnquiryFrame.attachChild(m_oEnquiryNumberLabel);
		
		// Enquiry Button
		m_oButtonEnquiry = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonEnquiry, "butEnquiry");
		m_oButtonEnquiry.addClickServerRequestSubmitElement(m_oTxtboxEnquiryNumber);
		m_oButtonEnquiry.setValue(AppGlobal.g_oLang.get()._("enquiry"));
		if (m_iRequestTimeout > 0) // use the default value if m_iRequestTimeout == 0
			m_oButtonEnquiry.setClickServerRequestTimeout((m_iRequestTimeout + 10000));
		m_oButtonEnquiry.setClickServerRequestBlockUI(true);
		m_oEnquiryFrame.attachChild(m_oButtonEnquiry);
		
		// Enter button for Enquiry
		m_oButtonSubmit = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSubmit, "butPaymentSubmit");
		if(sType.equals(FuncGamingInterface.POST_ONLINE_COMP) && !bIsOnlinePosting)
			m_oButtonSubmit.addClickServerRequestSubmitElement(m_oTxtboxEnquiryNumber);
		m_oButtonSubmit.addClickServerRequestSubmitElement(m_oTxtboxOnlineComp);
		m_oButtonSubmit.addClickServerRequestSubmitElement(m_oTxtboxPatronDate);
		m_oButtonSubmit.setValue(AppGlobal.g_oLang.get()._("enter"));
		m_oButtonSubmit.setClickServerRequestBlockUI(true);
		m_oButtonSubmit.setVisible(false);
		this.attachChild(m_oButtonSubmit);
		
		// Set member button
		m_oButtonSetMember = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSetMember, "butSetMember");
		m_oButtonSetMember.setValue(AppGlobal.g_oLang.get()._("set_member"));
		m_oButtonSetMember.setClickServerRequestBlockUI(true);
		
		m_oButtonSetMember.setVisible(false);
		m_oEnquiryFrame.attachChild(m_oButtonSetMember);
		
		
		// Clear member button
		m_oButtonClearMember = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonClearMember, "butClearMember");
		m_oButtonClearMember.setValue(AppGlobal.g_oLang.get()._("clear_member"));
		m_oButtonClearMember.setClickServerRequestBlockUI(true);
		
		m_oButtonClearMember.setVisible(false);
		m_oEnquiryFrame.attachChild(m_oButtonClearMember);
		
		// Online Comp Tag
		m_oOnlineCompLabelTag = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oOnlineCompLabelTag, "lblOnlineCompRedeemTag");
		m_oOnlineCompLabelTag.setValue(AppGlobal.g_oLang.get()._("online_comp_redeem")+" :");
		m_oOnlineCompLabelTag.setVisible(false);
		m_oEnquiryFrame.attachChild(m_oOnlineCompLabelTag);
		
		// Online Comp textbox
		m_oTxtboxOnlineComp = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxOnlineComp, "txtboxOnlineComp");
		
		m_oTxtboxOnlineComp.setVisible(false);
		m_oEnquiryFrame.attachChild(m_oTxtboxOnlineComp);
		
		// Patron Date Tag
		m_oPartonDateLabelTag = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oPartonDateLabelTag, "lblPatronDateOfBirthTag");
		m_oPartonDateLabelTag.setValue(AppGlobal.g_oLang.get()._("date_of_birth")+" (mmdd) :");
		m_oPartonDateLabelTag.setVisible(false);
		m_oEnquiryFrame.attachChild(m_oPartonDateLabelTag);
		
		// Patron Date textbox
		m_oTxtboxPatronDate = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxPatronDate, "txtboxPatronDateOfBirth");
		m_oTxtboxPatronDate.setVisible(false);
		m_oEnquiryFrame.attachChild(m_oTxtboxPatronDate);
		
		// Enquiry Result Block
		m_oEnquiryResultFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oEnquiryResultFrame, "fraEnquiryResultBlock");
		if(sType.equals(FuncGamingInterface.EXECUTIVE_COMP_INQUIRY))
			this.attachChild(m_oEnquiryResultFrame);
		
		// Enquiry Result Label
		oLabelTag = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelTag, "lblEnquiryResultTitle");
		
		if(sType.equals(FuncGamingInterface.EXECUTIVE_COMP_INQUIRY))
			oLabelTag.setValue(AppGlobal.g_oLang.get()._("staff_detail"));
		else 
			oLabelTag.setVisible(false);
		
		if(!bIsOnlinePosting)
			oLabelTag.setVisible(false);;
		this.attachChild(oLabelTag);
		
		// Enquiry Result list common basket
		m_oResultListCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oResultListCommonBasket, "fraResultListCommonBasket");
		m_oResultListCommonBasket.init();
		
		m_oResultListCommonBasket.setVisible(false);
		
		m_oEnquiryResultFrame.attachChild(m_oResultListCommonBasket);
		
		//Image Frame
		m_oFrameImage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameImage, "fraPhotoInfo");
		
		m_oImageMemberPhoto = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageMemberPhoto, "imagePhoto");
		m_oImageMemberPhoto.setVisible(false);
		m_oFrameImage.attachChild(m_oImageMemberPhoto);
			
		m_oImageMemberSign = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageMemberSign, "imageSign");
		m_oImageMemberSign.setVisible(false);
		m_oFrameImage.attachChild(m_oImageMemberSign);
		
		m_oCommonPageTabList = new FrameCommonPageContainer();
		m_oTemplateBuilder.buildFrame(m_oCommonPageTabList, "fraListTab");
		m_oCommonPageTabList.init(m_oCommonPageTabList.getWidth(), m_oCommonPageTabList.getHeight(), 
				200, 50, 4, "#0055B8","#999999", "#FFFFFF", "#FFFFFF", 48, false, true);
		m_oCommonPageTabList.setUnderlineColor("#0055B8");
		m_oCommonPageTabList.addListener(this);
		if (sType.equals(FuncGamingInterface.PATRON_INQUIRY) || sType.equals(FuncGamingInterface.POST_ONLINE_COMP)) {
			this.attachChild(m_oCommonPageTabList);
			m_oCommonPageTabList.addButton(AppGlobal.g_oLang.get()._("basic_information"), m_oEnquiryResultFrame);
			m_oCommonPageTabList.addButton(AppGlobal.g_oLang.get()._("photo_and_signature"), m_oFrameImage);
		}
		
		// Enter button for Post Online Comp
		m_oButtonSubmit = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSubmit, "butPaymentSubmit");
		if(sType.equals(FuncGamingInterface.POST_ONLINE_COMP) && !bIsOnlinePosting)
			m_oButtonSubmit.addClickServerRequestSubmitElement(m_oTxtboxEnquiryNumber);
		m_oButtonSubmit.addClickServerRequestSubmitElement(m_oTxtboxOnlineComp);
		m_oButtonSubmit.addClickServerRequestSubmitElement(m_oTxtboxPatronDate);
		m_oButtonSubmit.setValue(AppGlobal.g_oLang.get()._("enter"));
		m_oButtonSubmit.setClickServerRequestBlockUI(true);
		m_oButtonSubmit.setVisible(false);
		this.attachChild(m_oButtonSubmit);
		
		m_oInputPanelHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oInputPanelHeader, "fraInputPanelHeader");
		m_oInputPanelHeader.init(false);
		m_oInputPanel.attachChild(m_oInputPanelHeader);

		m_oDisplayPanelHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oDisplayPanelHeader, "fraDisplayPanelHeader");
		m_oDisplayPanelHeader.init(false);
		m_oDisplayPanel.attachChild(m_oDisplayPanelHeader);
		
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._(FuncGamingInterface.CARD_ENQUIRY));
		m_oInputPanelHeader.setTitle(AppGlobal.g_oLang.get()._("enquiry"));
		m_oDisplayPanelHeader.setTitle(AppGlobal.g_oLang.get()._("member_detail"));
		m_oDisplayPanelHeader.setTextSize(10);
		
		VirtualUILabel oLabelMemberCard = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelMemberCard, "lblMemberCard");
		oLabelMemberCard.setValue(AppGlobal.g_oLang.get()._("card_no") + " :");
		m_oInputPanel.attachChild(oLabelMemberCard);
		
		m_oTxtboxMemberCard = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxMemberCard, "txtboxMemberCard");
		m_oTxtboxMemberCard.setFocusWhenShow(false);
		m_oInputPanel.attachChild(m_oTxtboxMemberCard);
		
		m_oButtonCardEnquiry = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCardEnquiry, "butMemberEnquiry");
		m_oButtonCardEnquiry.addClickServerRequestSubmitElement(m_oTxtboxMemberCard);
		m_oButtonCardEnquiry.setValue(AppGlobal.g_oLang.get()._("search"));
		m_oInputPanel.attachChild(m_oButtonCardEnquiry);
		
		m_oButtonApplyDiscount = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonApplyDiscount, "butApplyDiscount");
		m_oButtonApplyDiscount.setValue(AppGlobal.g_oLang.get()._("apply_discount"));
		m_oButtonApplyDiscount.setVisible(false);
		m_oInputPanel.attachChild(m_oButtonApplyDiscount);
		
		m_oButtonClearDiscount = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonClearDiscount, "butClearDiscount");
		m_oButtonClearDiscount.setValue(AppGlobal.g_oLang.get()._("clear_discount"));
		m_oButtonClearDiscount.setVisible(false);
		m_oInputPanel.attachChild(m_oButtonClearDiscount);
		
		m_oMemberDetailCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oMemberDetailCommonBasket, "fraMemberDetailCommonBasket");
		m_oMemberDetailCommonBasket.init();
		m_oMemberDetailCommonBasket.setVisible(false);
		m_oDisplayPanel.attachChild(m_oMemberDetailCommonBasket);
		
		m_oEnquiryFrame.setVisible(false);
		m_oEnquiryResultFrame.setVisible(false);
		m_oCommonPageTabList.setVisible(false);
		
		if(m_sType.equals(FuncGamingInterface.POST_EXECUTIVE_COMP)) {
			m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("post_executive_comp"));
			m_oTxtboxGiftCertId.setVisible(false);
			m_oTxtboxSecurityCode.setVisible(false);
			oLblGiftCertId.setVisible(false);
			oLblSecurityCode.setVisible(false);
			
			m_oTxtboxPatronNum.setVisible(false);
			m_oTxtboxCompNum.setVisible(false);
			m_oTxtboxCouponId.setVisible(false);
			oLblPatronNum.setVisible(false);
			oLblCompNum.setVisible(false);
			oLblCouponId.setVisible(false);
			oLabelStaffDetail.setVisible(false);
			
			//hide the reason label and reason code if it is offline payment
			if(!bIsOnlinePosting) {
				oLblReason.setVisible(false);
				oLblReasonCode.setVisible(false);
				m_oTxtboxReasonCode.setVisible(false);
				oLblRemarks.setTop(oLblReasonCode.getTop());
				m_oTxtboxRemark.setTop(m_oTxtboxReasonCode.getTop());
				m_oLblSelectedReason.setVisible(false);
				m_oLblSelectedRemark.setTop(oLblRemarks.getTop() + oLblRemarks.getHeight());
			}
			
			if (!sInputControl.equals(FuncMSR.SWIPE_CARD_CONTROL_SWIPE_ONLY)) 
				m_oTxtboxStaffId.setFocusWhenShow(true);
		}
		else if(m_sType.equals(FuncGamingInterface.COMP_REDEMPTION)) {
			m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("comp_redemption"));
			m_oTxtboxStaffId.setVisible(false);
			m_oTxtboxNumOfPatrons.setVisible(false);
			m_oTxtboxNumOfEmployees.setVisible(false);
			m_oTxtboxReasonCode.setVisible(false);
			m_oLblSelectedReason.setVisible(false);

			m_oFramePage.setVisible(false);
			m_oLblPage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
			m_oImgButtonNextPage.setVisible(false);
			
			m_oTxtboxGiftCertId.setVisible(false);
			m_oTxtboxSecurityCode.setVisible(false);

			oLblStaffId.setVisible(false);
			oLblNumOfPatrons.setVisible(false);
			oLblNumOfEmployees.setVisible(false);
			oLblReasonCode.setVisible(false);
			oLblGiftCertId.setVisible(false);
			oLblSecurityCode.setVisible(false);
			oLblReason.setVisible(false);
			
			m_oTxtboxCouponId.setVisible(false);
			oLblCouponId.setVisible(false);
			oLabelStaffDetail.setVisible(false);
			
			//hide the remark list if it is online payment
			if(bIsOnlinePosting) {
				m_oFramePageContainer.setVisible(false);
				m_oTxtboxRemark.setVisible(false);
				oLblRemarks.setVisible(false);
				m_oLblSelectedRemark.setVisible(false);
				
			}else {
				oLblRemarks.setTop(m_oTxtboxCompNum.getTop() + m_oTxtboxCompNum.getHeight());
				m_oTxtboxRemark.setTop(oLblRemarks.getTop() + oLblRemarks.getHeight());
				m_oLblSelectedRemark.setTop(oLblRemarks.getTop() + 40);
			}
			
			m_oButtonEnter.addClickServerRequestSubmitElement(m_oTxtboxPatronNum);
			m_oButtonEnter.addClickServerRequestSubmitElement(m_oTxtboxCompNum);
			
			if (!sInputControl.equals(FuncMSR.SWIPE_CARD_CONTROL_SWIPE_ONLY)) 
				m_oTxtboxPatronNum.setFocusWhenShow(true);
		}
		else if(m_sType.equals(FuncGamingInterface.GIFT_CERTIFICATE_REDEMPTION)) {
			m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("gift_certificate_redemption"));
			
			m_oTxtboxGiftCertId.setVisible(true);
			m_oTxtboxSecurityCode.setVisible(true);
			
			m_oTxtboxStaffId.setVisible(false);
			m_oTxtboxNumOfPatrons.setVisible(false);
			m_oTxtboxNumOfEmployees.setVisible(false);
			m_oTxtboxReasonCode.setVisible(false);
			m_oLblSelectedReason.setVisible(false);
			
			m_oFramePage.setVisible(false);
			m_oLblPage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
			m_oImgButtonNextPage.setVisible(false);
			oLblReason.setVisible(false);
			
			m_oTxtboxPatronNum.setVisible(false);
			m_oTxtboxCompNum.setVisible(false);
			m_oTxtboxCouponId.setVisible(false);
			oLblPatronNum.setVisible(false);
			oLblCompNum.setVisible(false);
			oLblCouponId.setVisible(false);
			oLabelStaffDetail.setVisible(false);
			
			if(bIsOnlinePosting) {
				m_oFramePageContainer.setVisible(false);
				m_oTxtboxRemark.setVisible(false);
				oLblRemarks.setVisible(false);
				m_oLblSelectedRemark.setVisible(false);
				
			}else {
				oLblRemarks.setTop(m_oTxtboxSecurityCode.getTop() + m_oTxtboxSecurityCode.getHeight());
				m_oTxtboxRemark.setTop(oLblRemarks.getTop() + oLblRemarks.getHeight());
				m_oLblSelectedRemark.setTop(oLblRemarks.getTop() + 40);
			}
			
			oLblStaffId.setVisible(false);
			oLblNumOfPatrons.setVisible(false);
			oLblNumOfEmployees.setVisible(false);
			oLblReasonCode.setVisible(false);
			
			m_oButtonEnter.addClickServerRequestSubmitElement(m_oTxtboxGiftCertId);
			m_oButtonEnter.addClickServerRequestSubmitElement(m_oTxtboxSecurityCode);
			
			if (!sInputControl.equals(FuncMSR.SWIPE_CARD_CONTROL_SWIPE_ONLY)) 
				m_oTxtboxGiftCertId.setFocusWhenShow(true);
		}
		else if(m_sType.equals(FuncGamingInterface.COUPON_REDEMPTION)) {
			m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("coupon_redemption"));
			
			m_oTxtboxStaffId.setVisible(false);
			m_oTxtboxNumOfPatrons.setVisible(false);
			m_oTxtboxNumOfEmployees.setVisible(false);
			m_oTxtboxReasonCode.setVisible(false);
			m_oLblSelectedReason.setVisible(false);
			
			m_oFramePage.setVisible(false);
			m_oLblPage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
			m_oImgButtonNextPage.setVisible(false);
			
			m_oTxtboxGiftCertId.setVisible(false);
			m_oTxtboxSecurityCode.setVisible(false);
			oLblStaffId.setVisible(false);
			oLblNumOfPatrons.setVisible(false);
			oLblNumOfEmployees.setVisible(false);
			oLblReasonCode.setVisible(false);
			oLblGiftCertId.setVisible(false);
			oLblSecurityCode.setVisible(false);
			oLblReason.setVisible(false);
			
			m_oTxtboxCompNum.setVisible(false);
			oLblCompNum.setVisible(false);
			oLabelStaffDetail.setVisible(false);
			
			if(bIsOnlinePosting) {
				m_oFramePageContainer.setVisible(false);
				m_oTxtboxRemark.setVisible(false);
				oLblRemarks.setVisible(false);
				m_oLblSelectedRemark.setVisible(false);
			}else {
				oLblRemarks.setTop(m_oTxtboxCompNum.getTop() + m_oTxtboxCompNum.getHeight());
				m_oTxtboxRemark.setTop(oLblRemarks.getTop() + oLblRemarks.getHeight());
				m_oLblSelectedRemark.setTop(oLblRemarks.getTop() + 40);
			}
			
			m_oButtonEnter.addClickServerRequestSubmitElement(m_oTxtboxPatronNum);
			m_oButtonEnter.addClickServerRequestSubmitElement(m_oTxtboxCouponId);
			
			if (!sInputControl.equals(FuncMSR.SWIPE_CARD_CONTROL_SWIPE_ONLY)) 
				m_oTxtboxPatronNum.setFocusWhenShow(true);
		}
		else if (sType.equals(FuncGamingInterface.EXECUTIVE_COMP_INQUIRY) || sType.equals(FuncGamingInterface.PATRON_INQUIRY) || sType.equals(FuncGamingInterface.POST_ONLINE_COMP)) {
			//Set Header Title Based on the function type
			if (sType.equals(FuncGamingInterface.EXECUTIVE_COMP_INQUIRY))
				m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("executive_comp_inquiry"));
			else if (sType.equals(FuncGamingInterface.PATRON_INQUIRY))
				m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("patron_inquiry"));
			else if (sType.equals(FuncGamingInterface.POST_ONLINE_COMP))
				m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("post_online_comp"));
			
			// set invisible for non-GEMS Enquiry Frame Elements
			m_oTxtboxStaffId.setVisible(false);
			m_oTxtboxNumOfPatrons.setVisible(false);
			m_oTxtboxNumOfEmployees.setVisible(false);
			m_oTxtboxReasonCode.setVisible(false);
			m_oTxtboxRemark.setVisible(false);
			m_oLblSelectedReason.setVisible(false);
			
			m_oButtonEnter.setVisible(false);
			
			m_oFramePageContainer.setVisible(false);
			m_oTxtboxGiftCertId.setVisible(false);
			m_oTxtboxSecurityCode.setVisible(false);
			m_oLblSelectedRemark.setVisible(false);
			
			m_oTxtboxPatronNum.setVisible(false);
			m_oTxtboxCompNum.setVisible(false);
			m_oTxtboxCouponId.setVisible(false);
			
			oLblStaffId.setVisible(false);
			oLblNumOfPatrons.setVisible(false);
			oLblNumOfEmployees.setVisible(false);
			oLblReasonCode.setVisible(false);
			oLblRemarks.setVisible(false);
			oLblGiftCertId.setVisible(false);
			oLblSecurityCode.setVisible(false);
			
			oLblPatronNum.setVisible(false);
			oLblCompNum.setVisible(false);
			oLblCouponId.setVisible(false);
			oLblReason.setVisible(false);
			
			//Disable all non Enquiry element  
			m_oDisplayPanel.setVisible(false);
			
			//Enable Enquiry Frame
			m_oEnquiryFrame.setVisible(true);
			m_oEnquiryResultFrame.setVisible(true);
			m_oCommonPageTabList.setVisible(true);
			
			//attach the cover frame to Enquiry Frame
			if (sType.equals(FuncGamingInterface.POST_ONLINE_COMP)) 
				m_oEnquiryFrame.attachChild(m_oLblFirstTextBoxCoverFrame);
			
			//Special handel for Reamrk still added to the list
			m_oLblSelectedRemark.setValue("");
				
			//Offline posting
			if (sType.equals(FuncGamingInterface.POST_ONLINE_COMP) && !bIsOnlinePosting) {
					
				//Remark Label & TextBox & Selected Reamrk for offline posting
				m_oRemarkLabelTag = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(m_oRemarkLabelTag, "lblRemarks");
				m_oRemarkLabelTag.setValue(AppGlobal.g_oLang.get()._("remark") + " :");
				m_oRemarkLabelTag.setVisible(false);
				m_oEnquiryFrame.attachChild(m_oRemarkLabelTag);
				
				m_oTxtboxRemark = new VirtualUITextbox();
				m_oTemplateBuilder.buildTextbox(m_oTxtboxRemark, "txtboxRemarks");
				m_oTxtboxRemark.setEnabled(false);
			  
				m_oTxtboxRemark.setVisible(false);
				m_oEnquiryFrame.attachChild(m_oTxtboxRemark);
				
				m_oLblSelectedRemark = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(m_oLblSelectedRemark, "lblSelectedRemark");
				m_oLblSelectedRemark.setValue(AppGlobal.g_oLang.get()._("please_select_the_type"));
							
				m_oLblSelectedRemark.setVisible(false);
				m_oEnquiryFrame.attachChild(m_oLblSelectedRemark);
				
				//hide the result frame & show remark frame	 
				m_oCommonPageTabList.setVisible(false);
				m_oEnquiryResultFrame.setVisible(false);
				
				if (sInputControl.equals(FuncMSR.SWIPE_CARD_CONTROL_SWIPE_ONLY))
					m_oTxtboxOnlineComp.setFocusWhenShow(true);
				
				m_oFramePageContainer.setVisible(true);
				m_oDisplayPanel.setVisible(true);
			}
		}
		else if (sType.equals(FuncGamingInterface.CARD_ENQUIRY)) {
			m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("card_enquiry"));
			m_oInputPanelHeader.setTitle(AppGlobal.g_oLang.get()._("enquiry"));
			m_oDisplayPanelHeader.setTitle(AppGlobal.g_oLang.get()._("member_detail"));
			m_oTxtboxMemberCard.setFocusWhenShow(true);
		}
	}
	
	// Remark List Function
	public void initDisplayFrame(ArrayList<ArrayList<String>> oRemarkList, String [] sReasonCode) {
		m_oRemarkList = oRemarkList;
		m_sReasonCode = sReasonCode;
		m_oFrameReasonCode = new ArrayList<VirtualUIFrame>();
		
		// create frames with number of reason code
		for(int i = 0; i < m_oRemarkList.size(); i++){
			VirtualUIFrame oFrameReasonCode =  new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(oFrameReasonCode, "fraResonCode");
			m_oFrameReasonCode.add(oFrameReasonCode);
			//reason code name display as the common page container's tag
			m_oFramePageContainer.addButton(AppGlobal.g_oLang.get()._(m_sReasonCode[i]), oFrameReasonCode);
		}
		
		//default display the 1st reason code's remark list
		int iTagIndex = 0;
		int iCurrentPage = 1;
		
		m_iCurrentPage = iCurrentPage;
		m_iTotalPage = (int) (Math.ceil(m_oRemarkList.get(iTagIndex).size() / (double) MAX_PAGE_RECORD));
		
		this.updateRemarkCommonBasket(iTagIndex);
	}
	
	public void updateRemarkCommonBasket(int iTagIndex) {
		//clear the current frame all element
		m_oFrameReasonCode.get(iTagIndex).removeAllChildren();

		//create a new list
		m_oBasketRemarkList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oBasketRemarkList, "fraRemarkCommonBasket");
		m_oBasketRemarkList.init();
		m_oBasketRemarkList.addListener(this);
		
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		iFieldWidths.add(50);
		sFieldValues.add(AppGlobal.g_oLang.get()._("number"));
		iFieldWidths.add(550);
		sFieldValues.add(AppGlobal.g_oLang.get()._("remark"));
		m_oBasketRemarkList.addHeader(iFieldWidths, sFieldValues);
		m_oBasketRemarkList.setHeaderTextAlign(0, HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		m_oBasketRemarkList.setHeaderTextAlign(1, HeroActionProtocol.View.Attribute.TextAlign.LEFT+", "+HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		
//		m_oBasketRemakeList.setHeaderVisible(false);
//		m_oBasketRemakeList.setUnderlineFrameVisible(false);
		m_oBasketRemarkList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		m_oBasketRemarkList.setHeaderPadding(1, "0,0,0,30");
		
		ArrayList <String> oList = m_oRemarkList.get(iTagIndex);
		
		int iCount = 0;
		int iCurrentPage = m_iCurrentPage;
				
		for(int iIndex = (iCurrentPage - 1) * MAX_PAGE_RECORD;
				iIndex < iCurrentPage * MAX_PAGE_RECORD && iIndex < oList.size();
				iIndex++) {
			addRecordToRemarkList(0, iCount, iIndex + 1 ,oList.get(iIndex));
			iCount++;
		}
		m_oFrameReasonCode.get(iTagIndex).attachChild(m_oBasketRemarkList);
		
		changePrevNextPageColor();
	}
	
	public void changePrevNextPageColor() {
		int iTotalPage = m_iTotalPage;
		int iCurrentPage = m_iCurrentPage;
		if (iTotalPage > 1) {

			m_oFramePage.setVisible(true);
			if (iCurrentPage > 1) {
				m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
				m_oImgButtonPrevPage.setEnabled(true);
			} else {
				m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
				m_oImgButtonPrevPage.setEnabled(false);
			}

			if (iCurrentPage < iTotalPage) {
				m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
				m_oImgButtonNextPage.setEnabled(true);
			} else {
				m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
				m_oImgButtonNextPage.setEnabled(false);
			}

			m_oLblPage.setValue(iCurrentPage + "/" + iTotalPage);
		} else {
			m_oFramePage.setVisible(false);
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
			m_oImgButtonPrevPage.setEnabled(false);
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
			m_oImgButtonNextPage.setEnabled(false);
		}
	}
	
	public void addRecordToRemarkList(int iSectionId, int iItemIndex, int iRemarkIndex, String sRemark){
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		int iRowHeight = 0;
		
		// remark number
		iFieldWidths.add(50);
		sFieldValues.add(Integer.toString(iRemarkIndex));
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		
		// remark options
		iFieldWidths.add(550);
		sFieldValues.add(sRemark);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.LEFT+", "+HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		
		m_oBasketRemarkList.addItem(iSectionId, iItemIndex, iRowHeight, iFieldWidths, sFieldValues, sFieldAligns, null, null);
		m_oBasketRemarkList.setFieldPadding(iSectionId, iItemIndex, 1, "0,0,0,30");
	}
	
	
	public void inputBoxChecking() {
		String sErrMsg = "";
		
		//remark input box checking for offline payment 
		if(!m_bIsOnlinePosting) {
			if(m_oLblSelectedRemark.getValue().equals(AppGlobal.g_oLang.get()._("please_select_the_type"))) {
				showErrorMessage(AppGlobal.g_oLang.get()._("please_select_remark"));
				return;
			}
		}
		
		if(m_sType.equals(FuncGamingInterface.POST_EXECUTIVE_COMP)) {
			if((!m_bIsSwipeCard && m_oTxtboxStaffId.getValue().trim().isEmpty())
					|| (m_bIsSwipeCard && m_oTxtboxStaffId.getValue().isEmpty())
					|| m_oTxtboxNumOfPatrons.getValue().isEmpty()
					|| m_oTxtboxNumOfEmployees.getValue().isEmpty()
					|| m_oLblSelectedRemark.getValue().equals(AppGlobal.g_oLang.get()._("please_select_the_type")))
				sErrMsg = AppGlobal.g_oLang.get()._("missing_input_field");
			else if((!m_bIsSwipeCard && m_oTxtboxStaffId.getValue().trim().length() > 5)
					|| (m_bIsSwipeCard && m_oTxtboxStaffId.getValue().length() > 5)) 
				sErrMsg = AppGlobal.g_oLang.get()._("staff_id_exceed_5_chars");
			
			if(m_bIsOnlinePosting 
					&& m_oLblSelectedReason.getValue().equals(AppGlobal.g_oLang.get()._("please_select_the_type")))
					sErrMsg = AppGlobal.g_oLang.get()._("missing_input_field");
			
			if(sErrMsg.isEmpty()) {
				try {
					if(Integer.parseInt(m_oTxtboxNumOfPatrons.getValue()) >= 1000
							|| Integer.parseInt(m_oTxtboxNumOfEmployees.getValue()) >= 100)
						sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
					
				}catch (NumberFormatException e) {
					// TODO: handle exception
					sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
				}
			}
		}
		else if (m_sType.equals(FuncGamingInterface.GIFT_CERTIFICATE_REDEMPTION)) {	
			if(m_oTxtboxGiftCertId.getValue().trim().isEmpty()
					|| m_oTxtboxSecurityCode.getValue().trim().isEmpty())
				sErrMsg = AppGlobal.g_oLang.get()._("missing_input_field");
			else if (m_oTxtboxGiftCertId.getValue().trim().length() > 20)
				sErrMsg = AppGlobal.g_oLang.get()._("certificate_id_exceed_20_chars");
			else if(m_oTxtboxSecurityCode.getValue().trim().length() > 20)
				sErrMsg = AppGlobal.g_oLang.get()._("security_code_exceed_20_chars");
		}
		else if(m_sType.equals(FuncGamingInterface.COMP_REDEMPTION)) {
			if((!m_bIsSwipeCard && m_oTxtboxPatronNum.getValue().trim().isEmpty())
					|| (m_bIsSwipeCard && m_oTxtboxPatronNum.getValue().trim().isEmpty())
					|| m_oTxtboxCompNum.getValue().trim().isEmpty())
				sErrMsg = AppGlobal.g_oLang.get()._("missing_input_field");
		else if ((!m_bIsSwipeCard && m_oTxtboxPatronNum.getValue().trim().length() > 8)
					|| (m_bIsSwipeCard && m_oTxtboxPatronNum.getValue().trim().length() > 20)
					|| m_oTxtboxCompNum.getValue().trim().length() > 8)
				sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");

		}else if(m_sType.equals(FuncGamingInterface.COUPON_REDEMPTION)) {
			if((!m_bIsSwipeCard && m_oTxtboxPatronNum.getValue().trim().isEmpty())
					|| (m_bIsSwipeCard && m_oTxtboxPatronNum.getValue().trim().isEmpty())
					|| m_oTxtboxCouponId.getValue().trim().isEmpty())
				sErrMsg = AppGlobal.g_oLang.get()._("missing_input_field");
			else if((!m_bIsSwipeCard && m_oTxtboxPatronNum.getValue().trim().length() > 8)
					|| (m_bIsSwipeCard && m_oTxtboxPatronNum.getValue().trim().length() > 20)
					|| m_oTxtboxCouponId.getValue().trim().length() > 20)
				sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
		}
		if(!sErrMsg.isEmpty()) {
			showErrorMessage(sErrMsg);
			return;
		}
		
		for (FrameGamingInterfaceListener listener : listeners)
			listener.frameGamingInterfacePaymentEnter_clicked();
	}
	
	public void showErrorMessage(String sErrMsg) {
		FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), null);
		oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
		oFormDialogBox.setMessage(sErrMsg);
		oFormDialogBox.show();
	}
	
	public void disableInputbox(VirtualUITextbox oDisableInputBox, VirtualUITextbox oFocusInputBox) {
		oDisableInputBox.setValue("");
		m_oLblFirstTextBoxCoverFrame.setVisible(true);
		oFocusInputBox.setFocus();
	}
	
	public void updateCardEnquiryResult(HashMap<String, String> oResponse) {
		m_oMemberDetailCommonBasket.clearAllSections();
		m_oMemberDetailCommonBasket.setHeaderVisible(false);
		m_oMemberDetailCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		m_oMemberDetailCommonBasket.setBottomUnderlineVisible(false);
		m_oMemberDetailCommonBasket.setVisible(true);

		if (oResponse.containsKey("accountNumber"))
			addCardEnquiryResult(AppGlobal.g_oLang.get()._("account_number"), oResponse.get("accountNumber"), 0);
		
		if (m_sVendorKey.equals(InfVendor.KEY_BALLY))
			updateCardEnquiryResultForBally(oResponse);
		else if (m_sVendorKey.equals(InfVendor.KEY_SJM))
			updateCardEnquiryResultForSjm(oResponse);
		
	}
	
	private void updateCardEnquiryResultForBally(HashMap<String, String> oResponse) {
		if (oResponse.containsKey("cardType"))
			addCardEnquiryResult(AppGlobal.g_oLang.get()._("card_type"), oResponse.get("cardType"), 1);
		
		if (oResponse.containsKey("clubState"))
			addCardEnquiryResult(AppGlobal.g_oLang.get()._("state"), oResponse.get("clubState"), 2);
		
		if (oResponse.containsKey("title"))
			addCardEnquiryResult(AppGlobal.g_oLang.get()._("title"), oResponse.get("title"), 3);
		
		if (oResponse.containsKey("firstName"))
			addCardEnquiryResult(AppGlobal.g_oLang.get()._("first_name"), oResponse.get("firstName"), 4);
		
		if (oResponse.containsKey("lastName"))
			addCardEnquiryResult(AppGlobal.g_oLang.get()._("last_name"), oResponse.get("lastName"), 5);
		
		if (oResponse.containsKey("chineseName"))
			addCardEnquiryResult(AppGlobal.g_oLang.get()._("chinese_name"), oResponse.get("chineseName"), 6);
		
		if (oResponse.containsKey("discount"))
			addCardEnquiryResult(AppGlobal.g_oLang.get()._("discount"), oResponse.get("discount") + "%", 7);
	}
	
	private void updateCardEnquiryResultForSjm(HashMap<String, String> oResponse) {
		if (oResponse.containsKey("clubState"))
			addCardEnquiryResult(AppGlobal.g_oLang.get()._("state"), oResponse.get("clubState"), 1);
		
		if (oResponse.containsKey("firstName"))
			addCardEnquiryResult(AppGlobal.g_oLang.get()._("first_name"), oResponse.get("firstName"), 2);
		
		if (oResponse.containsKey("lastName"))
			addCardEnquiryResult(AppGlobal.g_oLang.get()._("last_name"), oResponse.get("lastName"), 3);
		
		if (oResponse.containsKey("chineseName"))
			addCardEnquiryResult(AppGlobal.g_oLang.get()._("chinese_name"), oResponse.get("chineseName"), 4);
		
		if (oResponse.containsKey("InfoListSize") && Integer.parseInt(oResponse.get("InfoListSize")) > 0) {
			int InfoListSize = Integer.parseInt(oResponse.get("InfoListSize"));
			for (int i = 0; i< InfoListSize; i++) {
				if(oResponse.containsKey("typeName"+i) && oResponse.containsKey("balance"+i))
					addCardEnquiryResult(oResponse.get("typeName"+i), oResponse.get("balance"+i), 5+i);
			}
		}
	}
	
	private void addCardEnquiryResult(String sTitle, String sContent, int iIndex) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();

		iFieldWidths.add(170);
		sFieldValues.add(sTitle);
		sFieldAligns.add("");

		iFieldWidths.add(400);
		sFieldValues.add(sContent);
		sFieldAligns.add("");
		
		m_oMemberDetailCommonBasket.addItem(0, iIndex, 45, iFieldWidths, sFieldValues, sFieldAligns, null, null);
		m_oMemberDetailCommonBasket.setFieldTextSize(0, iIndex, 0, 20);
		m_oMemberDetailCommonBasket.setFieldTextSize(0, iIndex, 1, 20);
	}
	
	public void updateEnquiryResult(HashMap<String, String> oResoponse) {
		m_oImageMemberPhoto.setVisible(false);
		m_oImageMemberSign.setVisible(false);
		
		m_oResultListCommonBasket.setVisible(true);
		m_oResultListCommonBasket.bringToTop();
		m_oResultListCommonBasket.clearAllSections();
		m_oResultListCommonBasket.setHeaderVisible(false);
		m_oResultListCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		m_oResultListCommonBasket.setBottomUnderlineVisible(false);
		
		// Executive Enquiry
		if (oResoponse.containsKey("userNumber") && oResoponse.get("userNumber") != null) 
			addEnquiryResult(AppGlobal.g_oLang.get()._("staff_id"), oResoponse.get("userNumber").replaceAll(".", "*"), 0);
		
		if (oResoponse.containsKey("balancePerTransaction") && oResoponse.get("balancePerTransaction") != null) 
			addEnquiryResult(AppGlobal.g_oLang.get()._("comp_balance_per_transaction"), oResoponse.get("balancePerTransaction"), 1);
		if (oResoponse.containsKey("balancePerDay") && oResoponse.get("balancePerDay") != null) 
			addEnquiryResult(AppGlobal.g_oLang.get()._("comp_balance_per_day"), oResoponse.get("balancePerDay"), 2);
		if (oResoponse.containsKey("balancePerMonth") && oResoponse.get("balancePerMonth") != null) 
			addEnquiryResult(AppGlobal.g_oLang.get()._("comp_balance_per_month"), oResoponse.get("balancePerMonth"), 3);
		
		// Patron Enquiry
		if (oResoponse.containsKey("memberNumber") && oResoponse.get("memberNumber") != null)
			addEnquiryResult(AppGlobal.g_oLang.get()._("patron_number"), oResoponse.get("memberNumber").replaceAll(".", "*"), 0);
		
		if (oResoponse.containsKey("memberName") && oResoponse.get("memberName") != null) 
			addEnquiryResult(AppGlobal.g_oLang.get()._("patron_name"), oResoponse.get("memberName"), 1);
		if (oResoponse.containsKey("pointsTotal") && oResoponse.get("pointsTotal") != null) 
			addEnquiryResult(AppGlobal.g_oLang.get()._("point_balance"), oResoponse.get("pointsTotal"), 2);
		if (oResoponse.containsKey("pointsDepartment") && oResoponse.get("pointsDepartment") != null) 
			addEnquiryResult(AppGlobal.g_oLang.get()._("point_department"), oResoponse.get("pointsDepartment"), 3);
	
		// Post Online Comp
		if (oResoponse.containsKey("checkBalance") && oResoponse.get("checkBalance") != null)
			addEnquiryResult(AppGlobal.g_oLang.get()._("check_balance"), oResoponse.get("checkBalance"), 4);
		
		if (m_sType.equals(FuncGamingInterface.PATRON_INQUIRY) ||
				m_sType.equals(FuncGamingInterface.POST_ONLINE_COMP)){
			
			if (oResoponse.containsKey("memberPhoto") && !oResoponse.get("memberPhoto").isEmpty())
				m_oImageMemberPhoto.setSource(oResoponse.get("memberPhoto").toString());
			m_oImageMemberPhoto.setVisible(true);
			if (oResoponse.containsKey("memberSignature") && !oResoponse.get("memberSignature").isEmpty()) 
				m_oImageMemberSign.setSource(oResoponse.get("memberSignature").toString());
			m_oImageMemberSign.setVisible(true);
		}
		
		if (m_sType.equals(FuncGamingInterface.PATRON_INQUIRY) || m_sType.equals(FuncGamingInterface.POST_ONLINE_COMP)) 
			setEnquiryNumberLabelValue(oResoponse.get("memberNumber").replaceAll(".", "*"));
	}
	
	private void addEnquiryResult(String sTitle, String sContent, int iIndex) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		
		if (m_sType.equals(FuncGamingInterface.EXECUTIVE_COMP_INQUIRY)) {
			iFieldWidths.add(360);
			sFieldValues.add(sTitle);
			sFieldAligns.add("");
	
			iFieldWidths.add(300);
			sFieldValues.add(sContent);
			sFieldAligns.add("");
		}
		if (m_sType.equals(FuncGamingInterface.PATRON_INQUIRY) ||
				m_sType.equals(FuncGamingInterface.POST_ONLINE_COMP)) {
			iFieldWidths.add(250);
			sFieldValues.add(sTitle);
			sFieldAligns.add("");
	
			iFieldWidths.add(380);
			sFieldValues.add(sContent);
			sFieldAligns.add("");
		}
		m_oResultListCommonBasket.addItem(0, iIndex, 60, iFieldWidths, sFieldValues, sFieldAligns, null, null);
	}
	
	/*
	 * TYPE_GEMS_ENQUIRY_SCREEN_PATRON_INQUIRY -> Enquiry in ordering panel, member is set (patron enquiry)
	 * TYPE_GEMS_ENQUIRY_SCREEN_COMP_INQUIRY -> Enquiry in ordering panel, no member is set (comp enquiry)
	 * TYPE_GEMS_ENQUIRY_SCREEN_POST_ONLINE_COMP_PAYMENT_ONLINE -> For Post Online Comp Enquiry
	 * TYPE_GEMS_ENQUIRY_SCREEN_POST_ONLINE_COMP_PAYMENT_OFFLINE For Post Online Comp Offline Posting
	 */
	public void changeSpecialEnquiryScreenForGems(String sScreenType) {
		if(sScreenType.equals(TYPE_GEMS_ENQUIRY_SCREEN_PATRON_INQUIRY)) {
			setMemberButtonVisible(true);
		} else if (sScreenType.equals(TYPE_GEMS_ENQUIRY_SCREEN_COMP_INQUIRY)) {
			setEnquiryTextBoxVisible(false);
			setEnquiryButtonVisible(false);
			setMemberButtonVisible(false);
			
			setEnquiryNumberLabelVisible(true);
			setClearButtonVisible(true);
		} else if (sScreenType.equals(TYPE_GEMS_ENQUIRY_SCREEN_POST_ONLINE_COMP_PAYMENT_ONLINE)) {
			setEnquiryTextBoxVisible(false);
			setEnquiryButtonVisible(false);
			
			setEnquiryNumberLabelVisible(true);
			setPostOnlineCompUI(true);
			m_oTxtboxOnlineComp.setFocus();
			
			//Resize Enquiry Frame
			m_oEnquiryFrame.setHeight(ENQUIRY_EXTENSION_HEIGHT);
		} else if (sScreenType.equals(TYPE_GEMS_ENQUIRY_SCREEN_POST_ONLINE_COMP_PAYMENT_OFFLINE)) {
			//Disable Member details list & show Offline Remark
			setEnquiryButtonVisible(false);
			setEnquiryResultListVisible(false);
			m_oCommonPageTabList.setVisible(false);;
			
			setPostOnlineCompUI(true);
			setRemarkLabelAndTextBoxVisible(true);
			
			//Resize Enquiry Frame
			m_oEnquiryFrame.setHeight(ENQUIRY_EXTENSION_HEIGHT);
		}
	}

	public void setPostOnlineCompUI(boolean bVisable) {
		m_oOnlineCompLabelTag.setVisible(true);
		m_oPartonDateLabelTag.setVisible(true);
		
		m_oTxtboxOnlineComp.setVisible(true);
		m_oTxtboxPatronDate.setVisible(true);
		
		m_oButtonSubmit.setVisible(true);
	}
	
	public void setGEMSTextBoxValue(String sType, String sValue) {
		if (sType.equals(FuncGamingInterface.POST_EXECUTIVE_COMP)) 
			m_oTxtboxStaffId.setValue(sValue);
		 else if (sType.equals(FuncGamingInterface.GIFT_CERTIFICATE_REDEMPTION)) 
			 m_oTxtboxGiftCertId.setValue(sValue);
		 else if (sType.equals(FuncGamingInterface.COMP_REDEMPTION)) 
			 m_oTxtboxPatronNum.setValue(sValue);
		 else if (sType.equals(FuncGamingInterface.COUPON_REDEMPTION)) 
			 m_oTxtboxPatronNum.setValue(sValue);
		 else if (sType.equals(FuncGamingInterface.POST_ONLINE_COMP)) 
			setEnquiryTextBoxValue(sValue);
	}

	public void setRemarkLabelAndTextBoxVisible(boolean bVisible) {
		m_oRemarkLabelTag.setVisible(bVisible);
		m_oTxtboxRemark.setVisible(bVisible);
		m_oLblSelectedRemark.setVisible(bVisible);
	}
	
	public void setEnquiryTextBoxVisible(boolean bVisible) {
		m_oTxtboxEnquiryNumber.setVisible(bVisible);
	}

	public String getEnquiryTextBoxValue() {
		return m_oTxtboxEnquiryNumber.getValue();
	}

	public void setEnquiryTextBoxValue(String sCardNumber) {
		if(m_oTxtboxEnquiryNumber.getVisible())
			m_oTxtboxEnquiryNumber.setValue(sCardNumber);
	}

	public void clearEnquiryTextBoxValue() {
		setEnquiryTextBoxValue("");
	}
	
	public void setOnlineCompTextBoxVisible(boolean bVisible) {
		m_oTxtboxOnlineComp.setVisible(bVisible);
	}

	public String getOnlineCompTextBoxValue() {
		return m_oTxtboxOnlineComp.getValue();
	}

	public void setOnlineCompTextBoxValue(String sCardNumber) {
		if(m_oTxtboxOnlineComp.getVisible())
			m_oTxtboxOnlineComp.setValue(sCardNumber);
	}

	public void clearOnlineCompTextBoxValue() {
		setOnlineCompTextBoxValue("");
	}
	
	public void setPatronDateTextBoxVisible(boolean bVisible) {
		m_oTxtboxPatronDate.setVisible(bVisible);
	}

	public String getPatronDateTextBoxValue() {
		return m_oTxtboxPatronDate.getValue();
	}

	public String getInputType() {
		return this.m_sInputType;
	}
	
	public void setInputType(String sInputType) {
		this.m_sInputType = sInputType;
	}
	
	public void setPatronDateTextBoxValue(String sCardNumber) {
		if(m_oTxtboxPatronDate.getVisible())
			m_oTxtboxPatronDate.setValue(sCardNumber);
	}

	public void clearPatronDateTextBoxValue() {
		setPatronDateTextBoxValue("");
	}
	
	public void setEnquiryNumberLabelVisible(boolean bVisible){
		m_oEnquiryNumberLabel.setVisible(bVisible);
	}
	
	public void setEnquiryNumberLabelValue(String sValue){
		m_oEnquiryNumberLabel.setValue(sValue);
	}
	
	public void setEnquiryResultListVisible(boolean bVisible) {
		m_oResultListCommonBasket.setVisible(bVisible);
	}

	public void clearEnquiryResultList() {
		m_oResultListCommonBasket.setVisible(false);
		m_oResultListCommonBasket.removeAllSections();
	}
	
	public void setEnquiryButtonVisible(boolean bVisible) {
		m_oButtonEnquiry.setVisible(bVisible);
	}
	
	public void setMemberButtonVisible(boolean bVisible) {
		m_oButtonSetMember.setVisible(bVisible);
	}
	
	public void setClearButtonVisible(boolean bVisible) {
		m_oButtonClearMember.setVisible(bVisible);
	}
	
	public void clearEnquiryElement() {
		m_oTxtboxStaffId.setValue("");
		m_oResultListCommonBasket.setVisible(false);
		m_oResultListCommonBasket.removeAllSections();
	}
	
	/***********************/
	/*	Override Function  */
	/***********************/
	@Override
	public boolean clicked(int iChildId, String sNote) {
		
		boolean bMatchChild = false;
		if (m_oImgButtonNextPage != null && iChildId == m_oImgButtonNextPage.getId()) {
			bMatchChild = true;
			m_iCurrentPage++;
			updateRemarkCommonBasket(m_oFramePageContainer.getCurrentIndex());
		}else if (m_oImgButtonPrevPage != null && iChildId == m_oImgButtonPrevPage.getId()) {
			bMatchChild = true;
			m_iCurrentPage--;
			updateRemarkCommonBasket(m_oFramePageContainer.getCurrentIndex());
		}else if (m_oButtonEnter != null && iChildId == m_oButtonEnter.getId()) {
			inputBoxChecking();
			bMatchChild = true;
		}
		else if (iChildId == m_oButtonEnquiry.getId()) {
			bMatchChild = true;
			
			if (m_oTxtboxEnquiryNumber.getValue().isEmpty()) {
				showErrorMessage(AppGlobal.g_oLang.get()._("missing_input_field"));
				return bMatchChild;
			}
			for (FrameGamingInterfaceListener listener : listeners) {
				// Raise the event to parent
				listener.frameGamingInterfaceResult_clickEnquiry(m_oTxtboxEnquiryNumber.getValue(),false);
			}
			
		}
		else if (iChildId == m_oButtonSetMember.getId()) {
			for (FrameGamingInterfaceListener listener : listeners) {
				// Raise the event to parent
				listener.frameGamingInterfaceResult_clickSetMember();
			}
			bMatchChild = true;
		}
		else if (iChildId == m_oButtonClearMember.getId()) {
			for (FrameGamingInterfaceListener listener : listeners) {
				// Raise the event to parent
				listener.frameGamingInterfaceResult_clickClearMember();
			}
			bMatchChild = true;
		}
		else if (iChildId == m_oButtonSubmit.getId()) {
			for (FrameGamingInterfaceListener listener : listeners) {
				// Raise the event to parent
				listener.frameGamingInterfaceResult_clickSubmit(getOnlineCompTextBoxValue(), getPatronDateTextBoxValue());
			}
			bMatchChild = true;
		}
		else if (iChildId == m_oButtonCardEnquiry.getId()) {
			bMatchChild = true;
			m_bIsSwipeCard = false;
			cardEnquiry(m_oTxtboxMemberCard.getValue());
			this.m_sInputType =	FuncGamingInterface.KEYIN;
		}
		else if (iChildId == m_oButtonApplyDiscount.getId()) {
			bMatchChild = true;
			for (FrameGamingInterfaceListener listener : listeners) {
				// Raise the event to parent
				listener.frameGamingInterfaceResult_clickApplyDiscount();
				break;
			}
		}
		else if(iChildId == m_oButtonClearDiscount.getId()){
			bMatchChild = true;
			//clear member card for "Card Enquiry" function
			//clear attached account and discount (remove extra info)
			for (FrameGamingInterfaceListener listener : listeners) {
				// Raise the event to parent
				listener.frameGamingInterfaceResult_clickClearDiscount();
				break;
			}
		}
		return bMatchChild;
	}
	
	public void cardEnquiry(String sEnquiryValue) {
		//empty input checking
		if (!m_bIsSwipeCard && m_oTxtboxMemberCard.getValue().trim().isEmpty()) {
			showErrorMessage(AppGlobal.g_oLang.get()._("invalid_input"));
			if(!this.getClearDiscountButtonVisible())
				clearMemberDetailDisplayAndApplyDiscountButton();
		}
		else {
			for (FrameGamingInterfaceListener listener : listeners) {
				// Raise the event to parent
				listener.frameGamingInterfaceResult_clickCardEnquiry(sEnquiryValue, m_bIsSwipeCard);
				break;
			}
		}
		this.m_oTxtboxMemberCard.setValue("");
	}
	
	public void clearMemberDetailDisplayAndApplyDiscountButton() {
		m_oButtonApplyDiscount.setVisible(false);
		m_oButtonClearDiscount.setVisible(false);
		m_oMemberDetailCommonBasket.setVisible(false);
	}
	
	@Override
	public boolean valueChanged(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if(m_sInputControl.equals(FuncMSR.SWIPE_CARD_CONTROL_MANUAL_INPUT_ONLY)) {
			bMatchChild = true;
			showErrorMessage(AppGlobal.g_oLang.get()._("support_key_in_input_only"));
			return bMatchChild;
		}
		
		String sSwipeCardValue = "";
		ClsActiveClient oActiveClient = AppGlobal.getActiveClient();
		if (oActiveClient != null) {
			if (iChildId == oActiveClient.getSwipeCardReaderElement().getId()
					&& oActiveClient.getSwipeCardReaderElement().getValueChangedServerRequestNote()
							.equals(FuncMSR.FRAME_SWIPE_CARD_DEFAULT)) {
					if (oActiveClient.getSwipeCardReaderElement().getValue().length() > 0) {
						sSwipeCardValue = oActiveClient.getSwipeCardReaderElement().getValue().replace("\r", "")
								.replace("\n", "");
						if (m_sVendorKey.equals(InfVendor.KEY_GEMS)) {
							if(m_bIsSwipeCard == false) {
								m_bIsSwipeCard = true;
								if(m_sInputControl.equals(FuncMSR.SWIPE_CARD_NO_CONTROL)) {
									switch(m_sType) {
									case FuncGamingInterface.POST_EXECUTIVE_COMP:
										disableInputbox(m_oTxtboxStaffId, m_oTxtboxNumOfPatrons);
										break;
									case FuncGamingInterface.COMP_REDEMPTION:
										disableInputbox(m_oTxtboxPatronNum, m_oTxtboxCompNum);
										break;
									case FuncGamingInterface.COUPON_REDEMPTION:
										disableInputbox(m_oTxtboxPatronNum, m_oTxtboxCouponId);
										break;
									case FuncGamingInterface.GIFT_CERTIFICATE_REDEMPTION:
										disableInputbox(m_oTxtboxGiftCertId, m_oTxtboxSecurityCode);
										break;
									case FuncGamingInterface.POST_ONLINE_COMP:
										if (!m_bIsOnlinePosting) 
											disableInputbox(m_oTxtboxEnquiryNumber, m_oTxtboxOnlineComp);
									case FuncGamingInterface.EXECUTIVE_COMP_INQUIRY:
									case FuncGamingInterface.PATRON_INQUIRY:
										break;
									}
								}
								for (FrameGamingInterfaceListener listener : listeners) {
									String sMsrCardNo = "";
									
									if (!sSwipeCardValue.isEmpty()) {
										switch(m_sType) {
										case FuncGamingInterface.POST_EXECUTIVE_COMP:
										case FuncGamingInterface.EXECUTIVE_COMP_INQUIRY:
											sMsrCardNo = listener.frameGamingInterfaceResult_getEmployeeCardMsrCardNo(sSwipeCardValue);
											break;
										default:
											sMsrCardNo = listener.frameGamingInterfaceResult_getMsrCardNo(sSwipeCardValue);
											break;
										}
									}
									
									if(!sMsrCardNo.isEmpty())
										sSwipeCardValue = sMsrCardNo;
									
									switch(m_sType) {
									case FuncGamingInterface.POST_ONLINE_COMP:
									case FuncGamingInterface.EXECUTIVE_COMP_INQUIRY:
									case FuncGamingInterface.PATRON_INQUIRY:
										if(m_sInputControl.equals(FuncMSR.SWIPE_CARD_NO_CONTROL)) {
											if(m_bIsOnlinePosting) 
												if(m_oTxtboxEnquiryNumber.getVisible()) 
													listener.frameGamingInterfaceResult_clickEnquiry(sSwipeCardValue, true);
										} else if (m_sInputControl.equals(FuncMSR.SWIPE_CARD_CONTROL_SWIPE_ONLY)) 
											if(m_bIsOnlinePosting && m_oTxtboxEnquiryNumber.getVisible()) 
												listener.frameGamingInterfaceResult_clickEnquiry(sSwipeCardValue, true);
										break;
									}
								}
							}
							setGEMSTextBoxValue(m_sType, sSwipeCardValue);
						}						
						else if (m_sVendorKey.equals(InfVendor.KEY_BALLY) || m_sVendorKey.equals(InfVendor.KEY_SJM)) {
							switch(m_sType) {
							case FuncGamingInterface.CARD_ENQUIRY:
								m_bIsSwipeCard = true;
								cardEnquiry(sSwipeCardValue);
								m_bIsSwipeCard = false;
								break;
							}
						}
					}
					bMatchChild = true;
			}
		}
		return bMatchChild;
	}
	
	@Override
	public void FrameTitleHeader_close() {
		// TODO Auto-generated method stub
		for (FrameGamingInterfaceListener listener : listeners) 
			// Raise the event to parent
			listener.frameGamingInterfaceResult_clickBack();
	}
		
	@Override
	public void frameCommonPageContainer_changeFrame() {
		// TODO Auto-generated method stub
		if (!m_sType.equals(FuncGamingInterface.PATRON_INQUIRY) && !m_sType.equals(FuncGamingInterface.POST_ONLINE_COMP) ) {
			if(m_bIsOnlinePosting) {
				int iTagIndex = m_oFramePageContainer.getCurrentIndex();
				int iCurrentPage = 1;

				m_iCurrentPage = iCurrentPage;
				m_iTotalPage = (int) (Math.ceil(m_oRemarkList.get(iTagIndex).size() / (double) MAX_PAGE_RECORD));

				updateRemarkCommonBasket(iTagIndex);

				m_oLblSelectedReason.setValue(m_sReasonCode[m_oFramePageContainer.getCurrentIndex()]);
				m_oLblSelectedRemark.setValue(AppGlobal.g_oLang.get()._("please_select_the_type"));
			}
		}
	}


	@Override
	public boolean frameCommonPageContainer_swipe(boolean bLeft, String sNote, int iChildId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void frameCommonPageContainer_updateFrame(int iOrigIndex, int iIndex, String sFrameName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void frameCommonPageContainer_CloseImageClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void frameCommonPageContainer_ClickButtonIndex(int iIndex) {
		// TODO Auto-generated method stub
		
	}	

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex,
			String sNote) {
		
		if (m_bIsOnlinePosting && m_oLblSelectedReason.getValue().equals(AppGlobal.g_oLang.get()._("please_select_the_type")))
			m_oLblSelectedReason.setValue(m_sReasonCode[0]);
		
		int iIndex = (m_iCurrentPage - 1) * MAX_PAGE_RECORD + iItemIndex + 1;
		int iCurrentListSize = m_oRemarkList.get(m_oFramePageContainer.getCurrentIndex()).size();

		if(iIndex == iCurrentListSize) {
			FormInputBox oFormInputBox = new FormInputBox(this.getParentForm());
			oFormInputBox.init();
			oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.DEFAULT);
			oFormInputBox.showKeyboard();
			oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("open_description"));
			oFormInputBox.setMessage(AppGlobal.g_oLang.get()._("please_input_the_description") + ":");
			oFormInputBox.show();
			
			String sInput = oFormInputBox.getInputValue();
			if(sInput !=null && !sInput.isEmpty())
				m_oLblSelectedRemark.setValue(sInput);
		}
		else
			m_oLblSelectedRemark.setValue(m_oRemarkList.get(m_oFramePageContainer.getCurrentIndex()).get(iIndex - 1));
		
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId, int iSectionIndex, int iItemIndex,
			int iFieldIndex, String sNote) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
		// TODO Auto-generated method stub
		
	}

	public String getType() {
		return this.m_sType;
	}
	
	public String getStaffId() {
		return this.m_oTxtboxStaffId.getValue();
	}
	
	public String getNumOfEmployees() {
		return this.m_oTxtboxNumOfEmployees.getValue();
	}
	
	public String getNumOfPatrons() {
		return this.m_oTxtboxNumOfPatrons.getValue();
	}
	
	public String getReasonCode() {
		if(m_bIsOnlinePosting && m_sType.equals(FuncGamingInterface.POST_EXECUTIVE_COMP))	
			return Integer.toString(this.m_oFramePageContainer.getCurrentIndex() + 1);
		return "";
	}
	
	public String getRemark() {
		return this.m_oLblSelectedRemark.getValue();
	}
	
	public boolean isSwipedCard() {
		return this.m_bIsSwipeCard;
	}
	
	public String getGiftCertId() {
		return this.m_oTxtboxGiftCertId.getValue();
	}
	
	public String getSecurityCode() {
		return this.m_oTxtboxSecurityCode.getValue();
	}
	
	public String getPatronNumber() {
		return m_oTxtboxPatronNum.getValue();
	}
	
	public String getPatronCardNumber() {
		return getPatronNumber();
	}
	
	public String getCompNumber() {
		return m_oTxtboxCompNum.getValue();
	}
	
	public String getCouponId() {
		return m_oTxtboxCouponId.getValue();
	}
	
	public String getMemberCardNumber() {
		return m_oTxtboxMemberCard.getValue();
	}
	public void setApplyDiscountButtonVisible(boolean bVisible) {
		this.m_oButtonApplyDiscount.setVisible(bVisible);
		this.m_oButtonClearDiscount.setVisible(!bVisible);
	}
	
	public boolean getClearDiscountButtonVisible() {
		return this.m_oButtonClearDiscount.getVisible();
	}
	
	public HashMap<String, String> getMemberDetail() {
		HashMap<String, String> oMemberDetail = new HashMap<>();
		
		oMemberDetail.put("accountNumber", m_oMemberDetailCommonBasket.getFieldValue(0, 0, 1));
		oMemberDetail.put("cardType", m_oMemberDetailCommonBasket.getFieldValue(0, 1, 1));
		oMemberDetail.put("clubState", m_oMemberDetailCommonBasket.getFieldValue(0, 2, 1));
		oMemberDetail.put("title", m_oMemberDetailCommonBasket.getFieldValue(0, 3, 1));
		oMemberDetail.put("firstName", m_oMemberDetailCommonBasket.getFieldValue(0, 4, 1));
		oMemberDetail.put("lastName", m_oMemberDetailCommonBasket.getFieldValue(0, 5, 1));
		oMemberDetail.put("chineseName", m_oMemberDetailCommonBasket.getFieldValue(0, 6, 1));
		oMemberDetail.put("discountPercentage", m_oMemberDetailCommonBasket.getFieldValue(0, 7, 1));
		
		return oMemberDetail;
	}
	
	public void setMemberDetailCommonBasketVisible(boolean bVisible){
		m_oMemberDetailCommonBasket.setVisible(bVisible);
	}

}
