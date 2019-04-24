package app;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import app.FuncMembershipInterface.MemberInterfaceResponseInfo;
import commonui.FormDialogBox;
import commonui.FrameListMessageBox;
import commonui.FrameNumberPad;
import commonui.FrameNumberPadListener;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import om.InfInterface;
import om.InfVendor;
import om.PosBusinessDay;
import om.PosDiscountType;
import om.PosInterfaceConfig;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUICodeReader;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;
import virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FrameMembershipInterfaceListener {
	void frameMembershipInterfaceResult_clickBack();
	void frameMembershipInterfaceResult_clickEnquiry(HashMap<String, String> oEnquiryInfo);
	void frameMembershipInterfaceResult_clickSetMember(String sMemberNo);
	void frameMembershipInterfaceResult_clickApplyDisc();
	void frameMembershipVoucherRedemptionResult_voucherRedemption(String sVoucherInfo, int iItemIndex);
	void frameMembershipInterfaceResult_clickRemoveVoucher();
	void frameMembershipInterfaceResult_clickClearMember();
	void frameMembershipInterfaceResult_exchangeRate(String sType, String sValue, String sVoucherValue);
	void FrameMembershipInterface_disconnect();
	void FrameMembershipInterface_timeout();
	void FrameMembershipInterface_forward(String value);
	void frameMembershipInterfaceResult_clickReset();
}

public class FrameMembershipInterface extends VirtualUIFrame implements FrameCommonBasketListener, FrameHorizontalTabListListener, FrameTitleHeaderListener, FrameNumberPadListener {
	TemplateBuilder m_oTemplateBuilder;
	
	VirtualUITextbox m_oTxtboxEnquiryNumber;
	VirtualUIButton m_oButtonEnquiry;
	VirtualUIButton m_oButtonSetMember;
	VirtualUIButton m_oButtonClearMember;
	VirtualUIButton m_oButtonApplyDisc;
	
	VirtualUIButton m_oButtonMobileEnquiry;
	VirtualUIButton m_oButtonMemberEnquiry;
	VirtualUIButton m_oButtonCardEnquiry;
	VirtualUIButton m_oButtonNricEnquiry;
	
	VirtualUIButton m_oButtonGCVoucher;
	VirtualUIButton m_oButtonNoRedemption;
	VirtualUIButton m_oButtonResetPoint;
	VirtualUIButton m_oButtonConfirm;
	
	VirtualUIList m_oListMemberDetail;
	VirtualUIList m_oListCoupon;
	VirtualUIList m_oListOther;
	VirtualUIList m_oListMemberFurtherDetail;
	
	VirtualUITextbox m_oTxtboxNRIC;
	VirtualUITextbox m_oTxtboxMobileNo;
	VirtualUITextbox m_oTxtboxCardNo;
	VirtualUITextbox m_oTxtboxCustomerNo;
	VirtualUITextbox m_oTxtboxName;
	VirtualUITextbox m_oTxtboxEmail;
	
	VirtualUICodeReader m_oCodeReader;
	
	VirtualUITextbox m_oTxtboxGCVoucher;
	VirtualUITextbox m_oTxtboxPointsToUseForPayment;
	VirtualUITextbox m_oTxtboxAmountToPayWithPoints;
	
	FrameNumberPad m_oFrameNumberPad;
	
	VirtualUIFrame m_oFrameQR;
	VirtualUIButton m_oButtonPrint;
	VirtualUIButton m_oButtonRemoveVoucher;
	FrameCommonBasket m_oCardListCommonBasket;
	FrameCommonBasket m_oVoucherListCommonBasket;
	FrameCommonBasket m_oMemberListCommonBasket;
	
	FrameListMessageBox m_oFrameMemberListMessageBox;
	
	VirtualUIFrame m_oFramePage;
	VirtualUILabel m_oLblPage;
	
	VirtualUIImage m_oImgButtonPrevPage;
	VirtualUIImage m_oImgButtonNextPage;
	
	ArrayList <FuncMembershipInterface.MemberInterfaceVoucherListInfo> m_oVoucherListInfo;
	
	String m_sPanelType;
	int m_iCurrentPageStartNo;
	public int m_iPageRecordCount = 6;
	int m_iScrollIndex = 1;
	public int m_iLastChangeTxtbox;
	
	static String PANEL_TYPE_CARD_LIST = "card_list";
	static String PANEL_TYPE_VOUCHER_LIST = "voucher_list";
	static String PANEL_TYPE_MEMBER_LIST = "member_list";
	static ArrayList<String> m_sCardOrVoucherList;
	String m_sTableNo;
	PosInterfaceConfig m_oPosInterfaceConfig;
	
	String m_sSelectedButton;
	
	int m_iRequestTimeout;
	
	boolean m_bShowInFloorPlan;
	boolean m_bHidePrintButton;
	
	// Operation Mode
	String m_sCurrentOperation;
	
	FrameTitleHeader m_oTitleHeader;
	
	String m_sInterfaceKey;
	
	//for epos member summary
	FuncMembershipInterface.MemberInterfaceResponseInfo m_oCurrentMemberInfo;
	List<FuncMembershipInterface.MemberInterfaceResponseInfo> m_oMemberList;
	FrameHorizontalTabList m_oFrameHorizontalTabList;
	
	private FrameHorizontalTabList m_oFrameEnquiryHorizontalTabList;

	private boolean m_bForwardFromRegistration;

	public static final String BACKGROUND_COLOUR_SELECTED = "#A0B3B7";
	public static final String BACKGROUND_COLOUR_SELECTED_FOR_GC = "#031E3E";
	public static final String BACKGROUND_COLOUR_UNSELECTED = "#FFFFFF";
	public static final String BACKGROUND_COLOUR_UNSELECTED_FOR_GC = "#E0E0E0";
	
	public static final String FOREGROUND_COLOUR_SELECTED_FOR_GC = "#FFFFFF";
	public static final String FOREGROUND_COLOUR_UNSELECTED_FOR_GC = "#015384";
	
	public static final String INPUT_KEY_IN = "key_in";
	public static final String INPUT_SCAN_BARCODE = "scan_barcode";
	
	public static final String SELECTED_BUTTON_SHOW_ALL = "show_all";
	public static final String SELECTED_BUTTON_GIFT = "gift";
	public static final String SELECTED_BUTTON_COUPON = "coupon";
	
	// Current Operation
	public final static String OPERATION_ENQUIRY = "e";
	public final static String OPERATION_VOUCHER_REDEMPTION = "v";
	public final static String OPERATION_ISSUE_CARD = "i";
	public final static String OPERATION_SET_MEMBER_DISCOUNT = "d";

	static final String BACKGROUND_COLOUR_SELECTED_FOR_CHINETEK = "#0055B8";
	static final String BACKGROUND_COLOUR_UNSELECTED_FOR_CHINETEK = "#5B6F73";
	
	public String m_oInputType;

	/** list of interested listeners (observers, same thing) */
	ArrayList<FrameMembershipInterfaceListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameMembershipInterfaceListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameMembershipInterfaceListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	} 
	
	public FrameMembershipInterface() {
		m_oTemplateBuilder = new TemplateBuilder();
		//m_oTemplateBuilder.loadTemplate("fraMembershipInterface.xml");
		
		listeners = new ArrayList<FrameMembershipInterfaceListener>();
		m_iRequestTimeout = 30;
		m_sPanelType = "";
		m_iCurrentPageStartNo = 0;
		m_sCardOrVoucherList = new ArrayList<String>();
		m_oMemberList = new ArrayList<FuncMembershipInterface.MemberInterfaceResponseInfo>();
		m_oInputType = FrameMembershipInterface.INPUT_KEY_IN;
		
		m_bShowInFloorPlan = false;
		m_bHidePrintButton = false;
		m_sTableNo = "";
		m_oPosInterfaceConfig = new PosInterfaceConfig();
		m_sSelectedButton = FrameMembershipInterface.SELECTED_BUTTON_SHOW_ALL;
		m_bForwardFromRegistration = false;
	}
	
	public void init(PosInterfaceConfig oPosInterfaceConfig, String sDefaultValue, String sOperation, String sTitleName, boolean bShowInFloorPlan, String sTableNo, boolean bHidePrintButton){
		m_oPosInterfaceConfig = oPosInterfaceConfig;
		m_sInterfaceKey = oPosInterfaceConfig.getInterfaceVendorKey();
		m_sTableNo = sTableNo;
		m_bShowInFloorPlan = bShowInFloorPlan;
		m_bHidePrintButton = bHidePrintButton;
		m_sCurrentOperation = sOperation;
		String sXmlFilename = "fraMembershipInterface.xml";
		if (m_sInterfaceKey.equals(InfVendor.KEY_HUARUNTONG))
			sXmlFilename = "fraMembershipInterface_huaRunTong.xml";

		
		m_oTemplateBuilder.loadTemplate(sXmlFilename);
		
		// Load child elements from template
		/////////////////////////////////////////////////////////////////
		m_oTitleHeader = new FrameTitleHeader();
		
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		m_oTitleHeader.setTitle(sTitleName);
		m_oTitleHeader.addListener(this);
		this.attachChild(m_oTitleHeader);
		
		// Horizontal List
		m_oFrameHorizontalTabList = new FrameHorizontalTabList();
		m_oTemplateBuilder.buildFrame(m_oFrameHorizontalTabList, "fraHorizontalList");
		
		VirtualUIFrame oFrameMemberEnquiryTab = new VirtualUIFrame();
		
		m_oFrameHorizontalTabList.init();
		m_oFrameHorizontalTabList.addListener(this);
		this.attachChild(m_oFrameHorizontalTabList);
		
		List<String> oTabNameList = new ArrayList<String>();
		
		oTabNameList.add(AppGlobal.g_oLang.get()._("member_detail"));
		oTabNameList.add(AppGlobal.g_oLang.get()._("further_detail"));
		oTabNameList.add(AppGlobal.g_oLang.get()._("card_list"));
		
		m_oFrameHorizontalTabList.addPageTabs(oTabNameList);
		VirtualUIFrame oFrameCoupon = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameCoupon, "fraCoupon");
		this.attachChild(oFrameCoupon);
		
		// enquiry number textbox
		m_oTxtboxEnquiryNumber = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxEnquiryNumber, "txtboxEnquiryNumber");
		m_oTxtboxEnquiryNumber.setFocusWhenShow(true);
		this.attachChild(m_oTxtboxEnquiryNumber);
		
		// apply discount button
		m_oButtonApplyDisc = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonApplyDisc, "butApplyDisc");
		m_oButtonApplyDisc.setValue(AppGlobal.g_oLang.get()._("apply_discount"));
		m_oButtonApplyDisc.setClickServerRequestBlockUI(true);
		this.attachChild(m_oButtonApplyDisc);
		
		//member details tag
		VirtualUILabel oLabelMemberDetailTag = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelMemberDetailTag, "lblMemberDetailTag");
		oLabelMemberDetailTag.setValue(AppGlobal.g_oLang.get()._("member_details"));
		this.attachChild(oLabelMemberDetailTag);
		
		VirtualUIFrame oMemberDetailTagIndicator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oMemberDetailTagIndicator, "fraMemberDetailTagIndicator");
		this.attachChild(oMemberDetailTagIndicator);
		
		VirtualUIFrame oFrameGuestDetail = new VirtualUIFrame();
		m_oFrameMemberListMessageBox = new FrameListMessageBox();
		
		m_oTemplateBuilder.buildFrame(oFrameGuestDetail, "fraMemberDetail");
		this.attachChild(oFrameGuestDetail);
		
		m_oListMemberDetail = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListMemberDetail, "listMemberDetail");
		
		oFrameGuestDetail.attachChild(m_oListMemberDetail);
		
		m_oListMemberFurtherDetail = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListMemberFurtherDetail, "listMemberDetail");
		oFrameGuestDetail.attachChild(m_oListMemberFurtherDetail);
		
		//coupon list tag
		VirtualUILabel oLabelCouponListTag = new VirtualUILabel();
		
		VirtualUIFrame oCouponListTagIndicator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCouponListTagIndicator, "fraCouponIndicator");
		this.attachChild(oCouponListTagIndicator);
		
		m_oListCoupon = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListCoupon, "listCoupon");
		oFrameCoupon.attachChild(m_oListCoupon);
		
		//others tag
		VirtualUILabel oLabelOtherListTag = new VirtualUILabel();
		
		VirtualUIFrame oOtherTagIndicator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oOtherTagIndicator, "fraOtherIndicator");
		this.attachChild(oOtherTagIndicator);
		
		VirtualUIFrame oFrameOther = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameOther, "fraOther");
		this.attachChild(oFrameOther);
		
		m_oListOther = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListOther, "listCoupon");
		oFrameOther.attachChild(m_oListOther);
		
		//Card list common basket
		m_oCardListCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oCardListCommonBasket, "fraCardListCommonBasket");
		m_oCardListCommonBasket.init();
		m_oCardListCommonBasket.addListener(this);
		this.attachChild(m_oCardListCommonBasket);
		
		//Voucher list common basket
		m_oVoucherListCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oVoucherListCommonBasket, "fraVoucherListCommonBasket");
		m_oVoucherListCommonBasket.init();
		m_oVoucherListCommonBasket.addListener(this);
		this.attachChild(m_oVoucherListCommonBasket);
		
		//Member list common basket
		m_oMemberListCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oMemberListCommonBasket, "fraMemberListCommonBasket");
		m_oMemberListCommonBasket.init();
		
		this.attachChild(m_oMemberListCommonBasket);
		// NRIC tag
		VirtualUILabel oLabelNRICTag = new VirtualUILabel();
		
		// NRIC textbox
		m_oTxtboxNRIC = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxNRIC, "txtboxNRIC");
		m_oTxtboxNRIC.setFocusWhenShow(true);
		
		this.attachChild(m_oTxtboxNRIC);
		
		// mobileNo tag
		VirtualUILabel oLabelMobileNoTag = new VirtualUILabel();
		
		// mobileNo textbox
		m_oTxtboxMobileNo = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxMobileNo, "txtboxMobile");
		m_oTxtboxMobileNo.setFocusWhenShow(false);
		
		this.attachChild(m_oTxtboxMobileNo);
		
		// cardNo tag
		VirtualUILabel oLabelCardNoTag = new VirtualUILabel();
		
		// cardNo textbox
		m_oTxtboxCardNo = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxCardNo, "txtboxCardNo");
		m_oTxtboxCardNo.setFocusWhenShow(false);
		
		this.attachChild(m_oTxtboxCardNo);
		
		// customer number tag
		VirtualUILabel oLabelCustomerNoTag = new VirtualUILabel();
		
		// customer number textbox
		m_oTxtboxCustomerNo = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxCustomerNo, "txtboxCustomerNo");
		
		m_oTxtboxCustomerNo.setFocusWhenShow(true);
		
		this.attachChild(m_oTxtboxCustomerNo);
		
		// email tag
		VirtualUILabel oLabelEmailTag = new VirtualUILabel();
		
		// email textbox
		m_oTxtboxEmail = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxEmail, "txtboxEmail");
		m_oTxtboxEmail.setFocusWhenShow(false);
		
		this.attachChild(m_oTxtboxEmail);
		
		// Name tag
		VirtualUILabel oLabelNameTag = new VirtualUILabel();
		
		attachLabelToFrame(this, oLabelNameTag, "lblNameTag", AppGlobal.g_oLang.get()._("name"));
		attachLabelToFrame(this, oLabelEmailTag, "lblEmailTag", AppGlobal.g_oLang.get()._("email"));
		attachLabelToFrame(this, oLabelCustomerNoTag, "lblCustomerNoTag", AppGlobal.g_oLang.get()._("customer_no"));
		attachLabelToFrame(this, oLabelCardNoTag, "lblCardNoTag", AppGlobal.g_oLang.get()._("card_no"));
		attachLabelToFrame(this, oLabelMobileNoTag, "lblMobileNoTag", AppGlobal.g_oLang.get()._("mobile_no"));
		attachLabelToFrame(this, oLabelNRICTag, "lblNRICTag", AppGlobal.g_oLang.get()._("nric_passport"));
		attachLabelToFrame(this, oLabelOtherListTag, "lblOtherTag", AppGlobal.g_oLang.get()._("others"));
		attachLabelToFrame(this, oLabelCouponListTag, "lblCouponTag", AppGlobal.g_oLang.get()._("coupon_list"));
		
		
		// name textbox
		m_oTxtboxName = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxName, "txtboxName");
		m_oTxtboxName.setFocusWhenShow(false);
		
		if(m_sInterfaceKey.equals(InfVendor.KEY_GENERAL_V2))
			oFrameMemberEnquiryTab.attachChild(m_oTxtboxName);
		else
			this.attachChild(m_oTxtboxName);
		
		// GC Voucher tag
		VirtualUILabel oLabelGCVoucher = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelGCVoucher, "lblGCVoucherTag");
		oLabelGCVoucher.setValue(AppGlobal.g_oLang.get()._("gc_voucher"));
		this.attachChild(oLabelGCVoucher);
		
		// Points To Use For Payment tag
		VirtualUILabel oLabelPointsToUseForPayment = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelPointsToUseForPayment, "lblPointsToUseForPaymentTag");
		oLabelPointsToUseForPayment.setValue(AppGlobal.g_oLang.get()._("points_to_use_for_payment"));
		this.attachChild(oLabelPointsToUseForPayment);
		
		// Points To Use For Payment textbox
		m_oTxtboxPointsToUseForPayment = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxPointsToUseForPayment, "txtboxPointsToUseForPayment");
		m_oTxtboxPointsToUseForPayment.allowValueChanged(true);
		m_oTxtboxPointsToUseForPayment.addValueChangedServerRequestSubmitElement(m_oTxtboxPointsToUseForPayment);
		m_oTxtboxPointsToUseForPayment.setVisible(false);
		m_oTxtboxPointsToUseForPayment.setClickHideKeyboard(true);
		m_oTxtboxPointsToUseForPayment.setValue("0");
		this.attachChild(m_oTxtboxPointsToUseForPayment);
		
		// Amount To Pay With Points tag
		VirtualUILabel oLabelAmountToPayWithPoints = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelAmountToPayWithPoints, "lblAmountToPayWithPointsTag");
		oLabelAmountToPayWithPoints.setValue(AppGlobal.g_oLang.get()._("amount_in") + " " + AppGlobal.g_oFuncOutlet.get().getCurrencyCode() + " " + AppGlobal.g_oLang.get()._("to_pay_with_points") + " (" + AppGlobal.g_oFuncOutlet.get().getCurrencySign() + ")");
		this.attachChild(oLabelAmountToPayWithPoints);
		
		// Amount To Pay With Points textbox
		m_oTxtboxAmountToPayWithPoints = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxAmountToPayWithPoints, "txtboxAmountToPayWithPoints");
		m_oTxtboxAmountToPayWithPoints.allowValueChanged(true);
		m_oTxtboxAmountToPayWithPoints.addValueChangedServerRequestSubmitElement(m_oTxtboxAmountToPayWithPoints);
		m_oTxtboxAmountToPayWithPoints.setVisible(false);
		m_oTxtboxAmountToPayWithPoints.setClickHideKeyboard(true);
		m_oTxtboxAmountToPayWithPoints.setValue("0");
		this.attachChild(m_oTxtboxAmountToPayWithPoints);
		
		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.clearFocusElementValueOnCancelClick(true);
		m_oFrameNumberPad.setEnterBlockUI(true);
		m_oFrameNumberPad.addListener(this);
		m_oFrameNumberPad.setVisible(false);
		this.attachChild(m_oFrameNumberPad);
		
		// QR code
		m_oFrameQR = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameQR, "fraQR");
		this.attachChild(m_oFrameQR);
		
		// enquiry button
		m_oButtonEnquiry = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonEnquiry, "butEnquiry");
		if (m_sInterfaceKey.equals(InfVendor.KEY_HUARUNTONG)) {
			m_oButtonEnquiry.addClickServerRequestSubmitElement(m_oTxtboxCustomerNo);
			m_oButtonEnquiry.addClickServerRequestSubmitElement(m_oTxtboxCardNo);
			m_oButtonEnquiry.addClickServerRequestSubmitElement(m_oTxtboxMobileNo);
		}
		
		m_oButtonEnquiry.setValue(AppGlobal.g_oLang.get()._("enquiry"));
		if(m_iRequestTimeout > 0)	//use the default value if m_iRequestTimeout == 0
			m_oButtonEnquiry.setClickServerRequestTimeout((m_iRequestTimeout + 10000));
		m_oButtonEnquiry.setClickServerRequestBlockUI(true);
		this.attachChild(m_oButtonEnquiry);
		
		// enquiry mobile button
		m_oButtonMobileEnquiry = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonMobileEnquiry, "butPhoneEnquiry");
		m_oButtonMobileEnquiry.addClickServerRequestSubmitElement(m_oTxtboxMobileNo);
		m_oButtonMobileEnquiry.setValue(AppGlobal.g_oLang.get()._("phone_enquiry"));
		if(m_iRequestTimeout > 0)	//use the default value if m_iRequestTimeout == 0
			m_oButtonMobileEnquiry.setClickServerRequestTimeout((m_iRequestTimeout + 10000));
		m_oButtonMobileEnquiry.setClickServerRequestBlockUI(true);
		this.attachChild(m_oButtonMobileEnquiry);
		
		// enquiry member button
		m_oButtonMemberEnquiry = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonMemberEnquiry, "butMemberEnquiry");
		m_oButtonMemberEnquiry.addClickServerRequestSubmitElement(m_oTxtboxMobileNo);
		m_oButtonMemberEnquiry.setValue(AppGlobal.g_oLang.get()._("member_enquiry"));
		
		if(m_iRequestTimeout > 0)	//use the default value if m_iRequestTimeout == 0
			m_oButtonMemberEnquiry.setClickServerRequestTimeout((m_iRequestTimeout + 10000));
		m_oButtonMemberEnquiry.setClickServerRequestBlockUI(true);
		this.attachChild(m_oButtonMemberEnquiry);
		
		// remove voucher button
		m_oButtonRemoveVoucher = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonRemoveVoucher, "butClearVoucher");
		m_oButtonRemoveVoucher.setValue(AppGlobal.g_oLang.get()._("clear_voucher"));
		m_oButtonRemoveVoucher.setClickServerRequestBlockUI(true);
		this.attachChild(m_oButtonRemoveVoucher);
		
		// enquiry card button
		m_oButtonCardEnquiry = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCardEnquiry, "butCardEnquiry");
		m_oButtonCardEnquiry.addClickServerRequestSubmitElement(m_oTxtboxMobileNo);
		m_oButtonCardEnquiry.setValue(AppGlobal.g_oLang.get()._("card_enquiry"));
		if (m_iRequestTimeout > 0)    //use the default value if m_iRequestTimeout == 0
			m_oButtonCardEnquiry.setClickServerRequestTimeout((m_iRequestTimeout + 10000));
		m_oButtonCardEnquiry.setClickServerRequestBlockUI(true);
		this.attachChild(m_oButtonCardEnquiry);
		
		// enquiry identify button
		m_oButtonNricEnquiry = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonNricEnquiry, "butNRICEnquiry");
		m_oButtonNricEnquiry.addClickServerRequestSubmitElement(m_oTxtboxMobileNo);
		m_oButtonNricEnquiry.setValue(AppGlobal.g_oLang.get()._("identify_enquiry"));
		if (m_iRequestTimeout > 0)    //use the default value if m_iRequestTimeout == 0
			m_oButtonNricEnquiry.setClickServerRequestTimeout((m_iRequestTimeout + 10000));
		m_oButtonNricEnquiry.setClickServerRequestBlockUI(true);
		this.attachChild(m_oButtonNricEnquiry);
		
		m_oButtonGCVoucher = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonGCVoucher, "butGCVoucher");
		m_oButtonGCVoucher.setValue(AppGlobal.g_oLang.get()._("gc_voucher"));
		m_oButtonGCVoucher.setClickServerRequestBlockUI(true);
		m_oButtonGCVoucher.setVisible(false);
		this.attachChild(m_oButtonGCVoucher);
		
		m_oButtonNoRedemption = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonNoRedemption, "butNoRedemption");
		m_oButtonNoRedemption.setValue(AppGlobal.g_oLang.get()._("no_redemption"));
		m_oButtonNoRedemption.setClickServerRequestBlockUI(true);
		m_oButtonNoRedemption.setVisible(false);
		this.attachChild(m_oButtonNoRedemption);
		
		m_oButtonResetPoint = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonResetPoint, "butSetPoint");
		m_oButtonResetPoint.setValue(AppGlobal.g_oLang.get()._("reset"));
		m_oButtonResetPoint.setClickServerRequestBlockUI(true);
		m_oButtonResetPoint.setVisible(false);
		this.attachChild(m_oButtonResetPoint);
		
		m_oButtonConfirm = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonConfirm, "butConfirm");
		m_oButtonConfirm.setValue(AppGlobal.g_oLang.get()._("confirm"));
		m_oButtonConfirm.setClickServerRequestBlockUI(true);
		m_oButtonConfirm.setVisible(false);
		this.attachChild(m_oButtonConfirm);
		
		//Create Page label
		m_oFramePage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePage, "fraPage");
		
		// Create prev page button
		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevPage");
		m_oImgButtonPrevPage.allowClick(true);
		m_oImgButtonPrevPage.setClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.allowLongClick(true);
		m_oImgButtonPrevPage.setLongClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.setVisible(false);
		this.attachChild(m_oImgButtonPrevPage);
		
		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.allowLongClick(true);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.setVisible(false);
		this.attachChild(m_oImgButtonNextPage);
		
		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oLblPage.setWidth(m_oFramePage.getWidth());
		m_oLblPage.setHeight(m_oFramePage.getHeight());
		m_oFramePage.setVisible(false);
		m_oFramePage.attachChild(m_oLblPage);
		this.attachChild(m_oFramePage);
		
		// init for  print, set member & clear member buttons. SPECIAL HANDLING to their attachments.
		// if it is golden circle mobile UI, attach to mobile result frame; if not attach to this.
		// print button
		m_oButtonPrint = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonPrint, "butPrint");
		
		m_oButtonPrint.setValue(AppGlobal.g_oLang.get()._("print"));
		
		m_oButtonPrint.setClickServerRequestBlockUI(true);
		m_oButtonPrint.setVisible(false);
		
		// set member button
		m_oButtonSetMember = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSetMember, "butSetMember");
		m_oButtonSetMember.setValue(AppGlobal.g_oLang.get()._("set_member"));
		m_oButtonSetMember.setClickServerRequestBlockUI(true);
		
		//clear member button
		m_oButtonClearMember = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonClearMember, "butClearMember");
		m_oButtonClearMember.setValue(AppGlobal.g_oLang.get()._("clear_member"));
		m_oButtonClearMember.setClickServerRequestBlockUI(true);
		m_oButtonClearMember.setVisible(false);
		
		// Attach to this frame
		this.attachChild(m_oButtonPrint);
		this.attachChild(m_oButtonSetMember);
		this.attachChild(m_oButtonClearMember);
		
		if (m_sInterfaceKey.equals(InfVendor.KEY_HUARUNTONG)) {
			m_oButtonSetMember.setValue(AppGlobal.g_oLang.get()._("set_member"));
			oLabelCustomerNoTag.setValue(AppGlobal.g_oLang.get()._("member_number"));
			oLabelMobileNoTag.setValue(AppGlobal.g_oLang.get()._("card_number"));
			oLabelMobileNoTag.setValue(AppGlobal.g_oLang.get()._("mobile"));
			oLabelOtherListTag.setValue(AppGlobal.g_oLang.get()._("member_list"));
			
			m_oButtonEnquiry.setVisible(true);
			m_oButtonSetMember.setVisible(false);
			m_oTitleHeader.setTitle(sTitleName);
			
			// show maximum 4 member record
			m_iPageRecordCount = 1;
		}
	}
	
	public void updatePageUpDownVisibility() {
		boolean bShowPageUp = false;
		boolean bShowPageDown = false;
		int iPage = 0;
		int iCurrentPanelRecordCount = 0;
		
		if (m_sPanelType.equals(FrameMembershipInterface.PANEL_TYPE_CARD_LIST)
				|| m_sPanelType.equals(FrameMembershipInterface.PANEL_TYPE_VOUCHER_LIST)
				|| m_sPanelType.equals(FrameMembershipInterface.PANEL_TYPE_MEMBER_LIST)) {
			if (m_sPanelType.equals(FrameMembershipInterface.PANEL_TYPE_CARD_LIST)) {
				iCurrentPanelRecordCount = m_oCardListCommonBasket.getItemCount(0);
				m_oCardListCommonBasket.setVisible(true);
			} else if (m_sPanelType.equals(FrameMembershipInterface.PANEL_TYPE_VOUCHER_LIST)) {
				iCurrentPanelRecordCount = m_oVoucherListCommonBasket.getItemCount(0);
				m_oVoucherListCommonBasket.setVisible(true);
			} else if (m_sPanelType.equals(FrameMembershipInterface.PANEL_TYPE_MEMBER_LIST)) {
					iCurrentPanelRecordCount = m_oMemberListCommonBasket.getItemCount(0);
				m_oMemberListCommonBasket.setVisible(true);
			}
			
			if (iCurrentPanelRecordCount > m_iPageRecordCount)
				iPage = (m_iCurrentPageStartNo / m_iPageRecordCount) + 1;
			
			if (m_iCurrentPageStartNo > 0)
				bShowPageUp = true;
			
			if (iCurrentPanelRecordCount > m_iPageRecordCount && m_iCurrentPageStartNo + m_iPageRecordCount < iCurrentPanelRecordCount)
				bShowPageDown = true;
		}
		setPageNumber(iPage);
		showPageUp(bShowPageUp);
		showPageDown(bShowPageDown);
	}
	
	public void showPageUp(boolean bShow) {
		if(bShow)
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
		else
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
		m_oImgButtonPrevPage.setEnabled(bShow);
	}
	
	public void showPageDown(boolean bShow) {
		if(bShow)
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
		else
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
		m_oImgButtonNextPage.setEnabled(bShow);
	}
	
	public void setPageNumber(int iNumber) {
		int iTotalPage = 0;
		if (iNumber > 0) {
			if (m_sPanelType.equals(FrameMembershipInterface.PANEL_TYPE_CARD_LIST))
				iTotalPage = (int) Math.ceil(1.0 * m_oCardListCommonBasket.getItemCount(0) / m_iPageRecordCount);
			else if (m_sPanelType.equals(FrameMembershipInterface.PANEL_TYPE_VOUCHER_LIST))
				iTotalPage = (int) Math.ceil(1.0 * m_oVoucherListCommonBasket.getItemCount(0) / m_iPageRecordCount);
			else if (m_sPanelType.equals(FrameMembershipInterface.PANEL_TYPE_MEMBER_LIST)) {
				
				iTotalPage = (int) Math.ceil(1.0 * m_oMemberListCommonBasket.getItemCount(0) / m_iPageRecordCount);
			}
			m_oFramePage.setVisible(true);
			m_oLblPage.setValue(iNumber + "/" + iTotalPage);
			m_oLblPage.setVisible(true);
			m_oImgButtonPrevPage.setVisible(true);
			m_oImgButtonNextPage.setVisible(true);
		} else {
			m_oFramePage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
			m_oImgButtonNextPage.setVisible(false);
		}
	}
	
	public void setCommonBasketBackgroundColor(int iRecordIndex, String sBackgroundColor) {
		int iFieldCount = 4;
		
		for (int i = 0; i <= iFieldCount; i++) {
			if (m_sPanelType == FrameMembershipInterface.PANEL_TYPE_CARD_LIST)
				m_oCardListCommonBasket.setFieldBackgroundColor(0, iRecordIndex, i, sBackgroundColor);
			else if (m_sPanelType.equals(FrameMembershipInterface.PANEL_TYPE_VOUCHER_LIST))
				m_oVoucherListCommonBasket.setFieldBackgroundColor(0, iRecordIndex, i, sBackgroundColor);
			else if (m_sPanelType.equals(FrameMembershipInterface.PANEL_TYPE_MEMBER_LIST)) {
				m_oMemberListCommonBasket.setFieldBackgroundColor(0, iRecordIndex, i, sBackgroundColor);
				if (i == 1)
					break;
			}
		}
	}
	
	public void cleanupMemberDetail() {
		m_oMemberListCommonBasket.removeAllSections();
		m_oCardListCommonBasket.removeAllSections();
		m_oVoucherListCommonBasket.removeAllSections();
		m_oListMemberDetail.removeAllChildren();
		m_oListCoupon.removeAllChildren();
		m_oListOther.removeAllChildren();
		m_oListMemberDetail.removeAllChildren();
		
		m_oListMemberFurtherDetail.removeAllChildren();
	}
	
	public void clearMemberListAndSetMemberButtonInvisible(){
		m_oMemberList = new ArrayList<FuncMembershipInterface.MemberInterfaceResponseInfo>();
		m_oCurrentMemberInfo = null;
		m_oButtonSetMember.setVisible(false);
		
		if (m_oButtonPrint != null)
			m_oButtonPrint.setVisible(false);
		
		// clear the page up page down visibility for golden circle
		if (m_sInterfaceKey.equals(InfVendor.KEY_GOLDEN_CIRCLE))
			updatePageUpDownVisibility();
	}
	
	public void updateMemberDetail_HuaRunTong(HashMap<String, String> oMemberInfo) {
		cleanupMemberDetail();
		changePageTab(0, true);
		
		// memberNumber
		addMemberDetail(AppGlobal.g_oLang.get()._("member_number"), oMemberInfo.get("memberNumber"));
		// memberName
		addMemberDetail(AppGlobal.g_oLang.get()._("member_name"), oMemberInfo.get("memberName"));
		// cardNumber
		addMemberDetail(AppGlobal.g_oLang.get()._("card_number"), oMemberInfo.get("cardNumber"));
		// memberTier
		addMemberDetail(AppGlobal.g_oLang.get()._("member_tier"), oMemberInfo.get("memberTier"));
		// memberStatus
		addMemberDetail(AppGlobal.g_oLang.get()._("member_status"), oMemberInfo.get("memberStatus"));
		// pointsBalance
		addMemberDetail(AppGlobal.g_oLang.get()._("member_points"), oMemberInfo.get("pointsBalance"));
		
		//set header
		ArrayList<Integer> iLabel1FieldWidths = new ArrayList<Integer>();
		ArrayList<String> sLabel1FieldValues = new ArrayList<String>();
		iLabel1FieldWidths.add(50);
		sLabel1FieldValues.add(AppGlobal.g_oLang.get()._("number"));
		iLabel1FieldWidths.add(225);
		sLabel1FieldValues.add(AppGlobal.g_oLang.get()._("member_number"));
		iLabel1FieldWidths.add(225);
		sLabel1FieldValues.add(AppGlobal.g_oLang.get()._("member_name"));
		
		m_oMemberListCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		m_oMemberListCommonBasket.addHeader(iLabel1FieldWidths, sLabel1FieldValues);
		m_oMemberListCommonBasket.setHeaderFormat(35, 18, "0,0,0,5");
		
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		ArrayList<String> sFieldTypes = new ArrayList<String>();
		
		iFieldWidths.add(50);
		sFieldValues.add("1");
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.LEFT + "," + HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		
		iFieldWidths.add(225);
		sFieldValues.add(oMemberInfo.get("memberNumber"));
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.LEFT + "," + HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		
		iFieldWidths.add(225);
		sFieldValues.add(oMemberInfo.get("memberName"));
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.LEFT + "," + HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		
		m_oMemberListCommonBasket.addItem(0, 0, 40, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
		
		m_oButtonSetMember.setVisible(true);
	}
	
	private void attachLabelToFrame(VirtualUIFrame oVirtualUIFrame, VirtualUILabel oVirtualUILabel, String sXmlName, String sLabelValue) {
		m_oTemplateBuilder.buildLabel(oVirtualUILabel, sXmlName);
		oVirtualUILabel.setValue(sLabelValue);
		oVirtualUIFrame.attachChild(oVirtualUILabel);
	}
	
	private void addMemberDetail(String sTitle, String sContent) {
		VirtualUIFrame oFrameMemberRecord = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameMemberRecord, "fraMemberRecord");
		VirtualUILabel oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelTitle, "lblMemberRecordTitle");
		
		oLabelTitle.setValue(sTitle);
		oFrameMemberRecord.attachChild(oLabelTitle);
		
		VirtualUILabel oLabelContent = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelContent, "lblMemberRecordContent");
		
		oLabelContent.setValue(sContent);
		
		
		oFrameMemberRecord.attachChild(oLabelContent);
		m_oListMemberDetail.attachChild(oFrameMemberRecord);
		
	}
	
	public void changePageTab(int iTagIndex, boolean bUpdateSelectedTabColor) {
		if (bUpdateSelectedTabColor)
			m_oFrameHorizontalTabList.changePageTab(iTagIndex);
		
		if (iTagIndex == 0) {
			m_oListMemberDetail.setVisible(true);
			m_oListMemberFurtherDetail.setVisible(false);
			m_oCardListCommonBasket.setVisible(false);
			
		} else if (iTagIndex == 1) {
			m_oListMemberFurtherDetail.setVisible(true);
			m_oListMemberDetail.setVisible(false);
			m_oCardListCommonBasket.setVisible(false);
		} else if (iTagIndex == 2) {
			m_oListMemberDetail.setVisible(false);
			m_oListMemberFurtherDetail.setVisible(false);
			if (m_oCardListCommonBasket.getChildCount() > 1)
				m_oCardListCommonBasket.setVisible(true);
		}
	}
	
	public void setSetMemberButtonVisible(boolean bVisible) {
		m_oButtonSetMember.setVisible(bVisible);
	}
	
	public void cleanupAllTextBox(){
		m_oTxtboxCardNo.setValue("");
		m_oTxtboxCustomerNo.setValue("");
		m_oTxtboxEmail.setValue("");
		m_oTxtboxEnquiryNumber.setValue("");
		m_oTxtboxMobileNo.setValue("");
		m_oTxtboxName.setValue("");
		m_oTxtboxNRIC.setValue("");
		
		m_oInputType = FrameMembershipInterface.INPUT_KEY_IN;
	}
	
	public void updateMemberNumber(String sMemberNum) {
		m_oTxtboxCustomerNo.setValue(sMemberNum);
	}
	
	//Build the enquiry structure before posting
	public HashMap<String, String> formEnquiryInfo() {
		HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();
		oEnquiryInfo.put("customerNumber", (!m_oTxtboxCustomerNo.getValue().isEmpty()) ? m_oTxtboxCustomerNo.getValue() : "");
		oEnquiryInfo.put("cardNumber", (!m_oTxtboxCardNo.getValue().isEmpty()) ? m_oTxtboxCardNo.getValue() : "");
		oEnquiryInfo.put("nric", (!m_oTxtboxNRIC.getValue().isEmpty()) ? m_oTxtboxNRIC.getValue() : "");
		oEnquiryInfo.put("name", (!m_oTxtboxName.getValue().isEmpty()) ? m_oTxtboxName.getValue() : "");
		oEnquiryInfo.put("mobileNumber", (!m_oTxtboxMobileNo.getValue().isEmpty()) ? m_oTxtboxMobileNo.getValue() : "");
		oEnquiryInfo.put("email", (!m_oTxtboxEmail.getValue().isEmpty()) ? m_oTxtboxEmail.getValue() : "");
		oEnquiryInfo.put("enquiryNumber", (!m_oTxtboxEnquiryNumber.getValue().isEmpty()) ? m_oTxtboxEnquiryNumber.getValue() : "");
		if (m_sInterfaceKey.equals(InfVendor.KEY_HUARUNTONG))
			oEnquiryInfo.put("memberNumber", (!m_oTxtboxCustomerNo.getValue().isEmpty()) ? m_oTxtboxCustomerNo.getValue() : "");
		return oEnquiryInfo;
	}
	
	public void createForwardEvent(String sValue, int iTimeout, int iDelay) {
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestValue(sValue);
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(iTimeout);
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestDelay(iDelay);
	}
	
	public void updatePage(FrameCommonBasket oFrameCommonBasket) {
		if (m_iCurrentPageStartNo + m_iPageRecordCount < oFrameCommonBasket.getItemCount(0)) {
			m_iCurrentPageStartNo += m_iPageRecordCount;
			updatePageUpDownVisibility();
		}
		m_iScrollIndex += m_iPageRecordCount;
		oFrameCommonBasket.moveScrollToItem(0, m_iScrollIndex);
	}
	
	public String getMSRCode(PosInterfaceConfig oTargetPosInterfaceConfig) {
		if (oTargetPosInterfaceConfig != null) {
			if (oTargetPosInterfaceConfig.getInterfaceConfig() != null && oTargetPosInterfaceConfig.getInterfaceConfig().has("general_setup") && oTargetPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").has("params") && oTargetPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").has("msr_code"))
				return oTargetPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").optJSONObject("msr_code").optString("value");
		}
		return null;
	}
	
	public PosInterfaceConfig getMSRConfigByMSRCode(String sMsrCode) {
		if (sMsrCode != null && !sMsrCode.isEmpty()) {
			List<PosInterfaceConfig> oPeripheralDeviceInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PERIPHERAL_DEVICE);
			for (PosInterfaceConfig oPosInterfaceConfig : oPeripheralDeviceInterfaceConfigList) {
				if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_MSR) && oPosInterfaceConfig.getInterfaceCode().equals(sMsrCode))
					return oPosInterfaceConfig;
			}
		}
		return null;
	}
	
	public String getMsrCardNo(String sCardContent, PosInterfaceConfig oMSRPosInterfaceConfig) {
		if (oMSRPosInterfaceConfig != null && sCardContent != null && !sCardContent.isEmpty()) {
			FuncMSR oFuncMSR = new FuncMSR();
			if (oFuncMSR.processCardContent(sCardContent, oMSRPosInterfaceConfig.getInterfaceConfig()) == FuncMSR.ERROR_CODE_NO_ERROR){
				return oFuncMSR.getCardNo();
			} else
				showDialogBox(AppGlobal.g_oLang.get()._("error"), oFuncMSR.getLastErrorMessage());
		}
		return "";
	}
	
	void showDialogBox(String sTitle, String sMessage) {
		if (sMessage.isEmpty())
			return;
		
		FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), null);
		oFormDialogBox.setTitle(sTitle);
		oFormDialogBox.setMessage(sMessage);
		oFormDialogBox.show();
	}
	
	/***********************/
	/*  Override Function  */
	/***********************/
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (iChildId == m_oButtonEnquiry.getId()) {
			for (FrameMembershipInterfaceListener listener : listeners) {
				//Raise the event to parent
				AppGlobal.g_oTerm.get().hideKeyboard();
				listener.frameMembershipInterfaceResult_clickEnquiry(formEnquiryInfo());
				
				break;
			}
			bMatchChild = true;
		}
		else if(iChildId == m_oButtonMobileEnquiry.getId()){
			//for yazuo interface
			for (FrameMembershipInterfaceListener listener : listeners) {
				VirtualUITextbox oTextbox = m_oTxtboxMobileNo;
				if(oTextbox.getValue().isEmpty()){
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), super.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("member_enquiry"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("please_fill_in_enquiry_info"));
					oFormDialogBox.show();
					return false;
				}
				
				//Raise the event to parent
				HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();
				oEnquiryInfo.put("mobileNumber", (!oTextbox.getValue().isEmpty())?oTextbox.getValue() : "");
				listener.frameMembershipInterfaceResult_clickEnquiry(oEnquiryInfo);
				break;
			}
			bMatchChild = true;
		}
		else if(iChildId == m_oButtonCardEnquiry.getId()){
			for (FrameMembershipInterfaceListener listener : listeners) {
				HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();
				
				if (m_oTxtboxMobileNo.getValue().isEmpty()) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), super.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("card_enquiry"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("please_fill_in_enquiry_info"));
					oFormDialogBox.show();
					return false;
				}
				
				//Raise the event to parent
				oEnquiryInfo.put("cardNumber", (!m_oTxtboxMobileNo.getValue().isEmpty()) ? m_oTxtboxMobileNo.getValue() : "");
				
				listener.frameMembershipInterfaceResult_clickEnquiry(oEnquiryInfo);
				break;
			}
			bMatchChild = true;
		}
		else if(iChildId == m_oButtonNricEnquiry.getId()){
			for (FrameMembershipInterfaceListener listener : listeners) {
				if(m_oTxtboxMobileNo.getValue().isEmpty()){
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), super.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("identify_enquiry"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("please_fill_in_enquiry_info"));
					oFormDialogBox.show();
					return false;
				}
				
				//Raise the event to parent
				HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();
				oEnquiryInfo.put("nric", (!m_oTxtboxMobileNo.getValue().isEmpty())?m_oTxtboxMobileNo.getValue() : "");
				
				listener.frameMembershipInterfaceResult_clickEnquiry(oEnquiryInfo);
				break;
			}
			bMatchChild = true;
		}
		else if (iChildId == m_oButtonSetMember.getId()) {
			for (FrameMembershipInterfaceListener listener : listeners) {
				// Raise the event to parent
				listener.frameMembershipInterfaceResult_clickSetMember(null);
				break;
			}
			bMatchChild = true;
		}else if (iChildId == m_oButtonApplyDisc.getId()) {
			for (FrameMembershipInterfaceListener listener : listeners) {
				// Raise the event to parent
				listener.frameMembershipInterfaceResult_clickApplyDisc();
				break;
			}
			bMatchChild = true;
		}else if (iChildId == m_oButtonRemoveVoucher.getId()) {
			for (FrameMembershipInterfaceListener listener : listeners) {
				// Raise the event to parent
				listener.frameMembershipInterfaceResult_clickRemoveVoucher();
				break;
			}
			bMatchChild = true;
		}
		else if (iChildId == m_oImgButtonPrevPage.getId()) {
			 // PAGE UP
			if(m_iCurrentPageStartNo-m_iPageRecordCount >= 0) {
				m_iCurrentPageStartNo -= m_iPageRecordCount;
				
				updatePageUpDownVisibility();
				m_iScrollIndex -= m_iPageRecordCount;
				if(m_sPanelType.equals(FrameMembershipInterface.PANEL_TYPE_CARD_LIST))
					m_oCardListCommonBasket.moveScrollToItem(0, m_iScrollIndex);
				else if(m_sPanelType.equals(FrameMembershipInterface.PANEL_TYPE_VOUCHER_LIST))
					m_oVoucherListCommonBasket.moveScrollToItem(0, m_iScrollIndex);
				else if(m_sPanelType.equals(FrameMembershipInterface.PANEL_TYPE_MEMBER_LIST)){
					m_oMemberListCommonBasket.moveScrollToItem(0, m_iScrollIndex);
				}
			}
			
			bMatchChild = true;
		} else if (iChildId == m_oImgButtonNextPage.getId()) {
			// PAGE DOWN
			if (m_sPanelType.equals(FrameMembershipInterface.PANEL_TYPE_CARD_LIST)) {
				this.updatePage(m_oCardListCommonBasket);
			} else if (m_sPanelType.equals(FrameMembershipInterface.PANEL_TYPE_VOUCHER_LIST)) {
				this.updatePage(m_oVoucherListCommonBasket);
			} else if (m_sPanelType.equals(FrameMembershipInterface.PANEL_TYPE_MEMBER_LIST)) {
				this.updatePage(m_oMemberListCommonBasket);
			}
			bMatchChild = true;
		} else if (m_oButtonResetPoint != null && iChildId == m_oButtonResetPoint.getId()) {
			for (FrameMembershipInterfaceListener listener : listeners) {
				listener.frameMembershipInterfaceResult_clickReset();
				break;
			}
			bMatchChild = true;
		} else if (m_oButtonClearMember != null && iChildId == m_oButtonClearMember.getId()) {
			for (FrameMembershipInterfaceListener listener : listeners) {
				listener.frameMembershipInterfaceResult_clickClearMember();
				break;
			}
			bMatchChild = true;
		}
		return bMatchChild;
	}
	
	@Override
	public boolean valueChanged(int iChildId, String sNote) {
		boolean bMatchChild = false;
		boolean bContinueToMSR = false;
		// Get the value by swipe card
		String sSwipeCardValue = "";
		String sMsrCode = "";
		String sCardNo = "";
		
		// Get the value by QR code
		if (m_oCodeReader != null && iChildId == m_oCodeReader.getId()) {
			bContinueToMSR = true;
			bMatchChild = true;
			m_oInputType = FrameMembershipInterface.INPUT_SCAN_BARCODE;
			
			sSwipeCardValue = m_oCodeReader.getValue().replace("\r", "").replace("\n", "");
		}
		else if(m_oTxtboxAmountToPayWithPoints != null && iChildId == m_oTxtboxAmountToPayWithPoints.getId()) {
			m_iLastChangeTxtbox = iChildId;
			bMatchChild = true;
		}
		else if(m_oTxtboxPointsToUseForPayment != null && iChildId == m_oTxtboxPointsToUseForPayment.getId()) {
			if(m_iLastChangeTxtbox != m_oTxtboxGCVoucher.getId()) {
				m_iLastChangeTxtbox = iChildId;
				bMatchChild = true;
			}
		}
		else if(m_oTxtboxGCVoucher != null && iChildId == m_oTxtboxGCVoucher.getId()) {
			m_iLastChangeTxtbox = iChildId;
			bMatchChild = true;
		}else if(m_oTxtboxEnquiryNumber != null && iChildId == m_oTxtboxEnquiryNumber.getId()) {
			m_iLastChangeTxtbox = iChildId;
			bMatchChild = true;
		}
		
		// Get the value by swipe card
		ClsActiveClient oActiveClient = AppGlobal.getActiveClient();
		if (oActiveClient != null && iChildId == oActiveClient.getSwipeCardReaderElement().getId()) {
			bMatchChild = true;
			sSwipeCardValue = oActiveClient.getSwipeCardReaderElement().getValue().replace("\r", "").replace("\n", "");
			
			//start
			List<PosInterfaceConfig> oInterfaceConfigWithMsrList = new ArrayList<PosInterfaceConfig>();
			// Get the configure from interface module
			oInterfaceConfigWithMsrList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_MEMBERSHIP_INTERFACE);
			JSONObject oInterfaceConfigWithMsr = null;
			
			for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigWithMsrList) {
				if (oPosInterfaceConfig.getInterfaceVendorKey() == m_sInterfaceKey) {
					oInterfaceConfigWithMsr = oPosInterfaceConfig.getInterfaceConfig();
					if (oInterfaceConfigWithMsr.has("general_setup") && 
							oInterfaceConfigWithMsr.optJSONObject("general_setup").has("params") && 
							oInterfaceConfigWithMsr.optJSONObject("general_setup").optJSONObject("params").has("msr_code")){
						sMsrCode = oInterfaceConfigWithMsr.optJSONObject("general_setup").optJSONObject("params").optJSONObject("msr_code").optString("value");
						break;
					}
				}
			}
			bContinueToMSR = true;
		}
		
		if(bContinueToMSR){
			//start
			List<PosInterfaceConfig> oInterfaceConfigWithMsrList = new ArrayList<PosInterfaceConfig>();
			// Get the configure from interface module
			oInterfaceConfigWithMsrList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_MEMBERSHIP_INTERFACE);
			JSONObject oInterfaceConfigWithMsr = null;
			
			for (PosInterfaceConfig oPosInterfaceConfig : oInterfaceConfigWithMsrList) {
				if (oPosInterfaceConfig.getInterfaceVendorKey() == m_sInterfaceKey) {
					oInterfaceConfigWithMsr = oPosInterfaceConfig.getInterfaceConfig();
					if (oInterfaceConfigWithMsr.has("general_setup") &&
							oInterfaceConfigWithMsr.optJSONObject("general_setup").has("params") &&
							oInterfaceConfigWithMsr.optJSONObject("general_setup").optJSONObject("params").has("msr_code")) {
						sMsrCode = oInterfaceConfigWithMsr.optJSONObject("general_setup").optJSONObject("params").optJSONObject("msr_code").optString("value");
						break;
					}
				}
			}
			
			if (!sMsrCode.isEmpty()) {
				List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
				// Get the configure from interface module
				oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PERIPHERAL_DEVICE);
				for (PosInterfaceConfig oMsrInterfaceConfig : oInterfaceConfigList) {
					if (oMsrInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_MSR) && oMsrInterfaceConfig.getInterfaceCode().equals(sMsrCode)) {
						// Capture information from swipe card
						FuncMSR oFuncMSR = new FuncMSR();
						int iErrorCode = oFuncMSR.processCardContent(sSwipeCardValue, oMsrInterfaceConfig.getInterfaceConfig());
						
						// Get the necessary value
						if (iErrorCode == FuncMSR.ERROR_CODE_NO_ERROR) {
							sCardNo = oFuncMSR.getCardNo();
						} else {
							FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
							oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
							oFormDialogBox.setMessage(oFuncMSR.getLastErrorMessage());
							oFormDialogBox.show();
							oFormDialogBox = null;
							break;
						}
					}
				}
			}
			if (!sCardNo.isEmpty())
				updateMemberNumber(sCardNo);
			else
				updateMemberNumber(sSwipeCardValue);
			
			if (!sMsrCode.isEmpty()) {
				List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
				// Get the configure from interface module
				oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PERIPHERAL_DEVICE);
				for (PosInterfaceConfig oMsrInterfaceConfig : oInterfaceConfigList) {
					if (oMsrInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_MSR) && oMsrInterfaceConfig.getInterfaceCode().equals(sMsrCode)) {
						// Capture information from swipe card
						FuncMSR oFuncMSR = new FuncMSR();
						int iErrorCode = oFuncMSR.processCardContent(sSwipeCardValue, oMsrInterfaceConfig.getInterfaceConfig());
						
						// Get the necessary value
						if (iErrorCode == FuncMSR.ERROR_CODE_NO_ERROR)
							sCardNo = oFuncMSR.getCardNo();
						else {
							FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
							oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
							oFormDialogBox.setMessage(oFuncMSR.getLastErrorMessage());
							oFormDialogBox.show();
							oFormDialogBox = null;
							break;
						}
					}
				}
			}
			if (!sCardNo.isEmpty())
				updateMemberNumber(sCardNo);
			else
				updateMemberNumber(sSwipeCardValue);
		}
		return bMatchChild;
	}
	
	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex,
			String sNote) {
		// TODO Auto-generated method stub
		String sItemValue = "";
		String []sValue = null;
		
		if (m_oCardListCommonBasket != null && iBasketId == m_oCardListCommonBasket.getId())
			m_sPanelType = FrameMembershipInterface.PANEL_TYPE_CARD_LIST;
		else if (m_oVoucherListCommonBasket != null && iBasketId == m_oVoucherListCommonBasket.getId())
			m_sPanelType = FrameMembershipInterface.PANEL_TYPE_VOUCHER_LIST;
		else if (m_oMemberListCommonBasket != null && iBasketId == m_oMemberListCommonBasket.getId())
			m_sPanelType = FrameMembershipInterface.PANEL_TYPE_MEMBER_LIST;
		
		for (FrameMembershipInterfaceListener listener : listeners) {
			// Raise the event to parent
			if (m_sPanelType == FrameMembershipInterface.PANEL_TYPE_CARD_LIST)
				listener.frameMembershipInterfaceResult_clickSetMember(sValue[0]);
			else if (m_sPanelType == FrameMembershipInterface.PANEL_TYPE_VOUCHER_LIST)
				listener.frameMembershipVoucherRedemptionResult_voucherRedemption(sItemValue, iItemIndex);
		}
	}
	public ArrayList<FuncMembershipInterface.MemberInterfaceVoucherListInfo> getVoucherListInfo() {
		return m_oVoucherListInfo;
	}
	
	public void setForwardFromRegistration(boolean bForwardFromRegistration) {
		m_bForwardFromRegistration = bForwardFromRegistration;
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
	
	@Override
	public void frameHorizontalTabList_clickTab(int iTabIndex, int iId) {
		changePageTab(iTabIndex, true);
	}
	
	@Override 
	public void FrameTitleHeader_close(){
		for (FrameMembershipInterfaceListener listener : listeners) {
			// Raise the event to parent
			listener.frameMembershipInterfaceResult_clickBack();
			break;
		}
	}
	
	@Override
	public void FrameNumberPad_clickEnter() {
		// TODO Auto-generated method stub
		if(m_oTxtboxPointsToUseForPayment.getEnabled() && m_oTxtboxAmountToPayWithPoints.getEnabled()) {
			if(m_oTxtboxGCVoucher.getValue().isEmpty())
				m_oTxtboxGCVoucher.setValue("0");
			if(m_oTxtboxAmountToPayWithPoints.getValue().isEmpty())
				m_oTxtboxAmountToPayWithPoints.setValue("0");
			if(m_oTxtboxPointsToUseForPayment.getValue().isEmpty())
				m_oTxtboxPointsToUseForPayment.setValue("0");
			
			if(m_iLastChangeTxtbox == m_oTxtboxAmountToPayWithPoints.getId()) {
				m_oTxtboxGCVoucher.setFocus();
				m_iLastChangeTxtbox = m_oTxtboxGCVoucher.getId();
				
				for (FrameMembershipInterfaceListener listener : listeners) {
					// Raise the event to parent
					listener.frameMembershipInterfaceResult_exchangeRate("amount", m_oTxtboxAmountToPayWithPoints.getValue(), m_oTxtboxGCVoucher.getValue());
				}
			}else if(m_iLastChangeTxtbox == m_oTxtboxPointsToUseForPayment.getId()) {
				m_oTxtboxAmountToPayWithPoints.setFocus();
				m_iLastChangeTxtbox = m_oTxtboxAmountToPayWithPoints.getId();
				
				for (FrameMembershipInterfaceListener listener : listeners) {
					// Raise the event to parent
					listener.frameMembershipInterfaceResult_exchangeRate("points", m_oTxtboxPointsToUseForPayment.getValue(), m_oTxtboxGCVoucher.getValue());
				}
			}else if(m_iLastChangeTxtbox == m_oTxtboxGCVoucher.getId() || m_iLastChangeTxtbox == 0) {
				m_oTxtboxPointsToUseForPayment.setFocus();
				m_iLastChangeTxtbox = m_oTxtboxPointsToUseForPayment.getId();
				
				for (FrameMembershipInterfaceListener listener : listeners) {
					// Raise the event to parent
					listener.frameMembershipInterfaceResult_exchangeRate("voucher", m_oTxtboxPointsToUseForPayment.getValue(), m_oTxtboxGCVoucher.getValue());
				}
			}
		}
	}
	
	@Override
	public void FrameNumberPad_clickCancel() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void FrameNumberPad_clickNumber(String string) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean forward(int iChildId, String sNote, String sStatus) {
		boolean bMatchChild = false;
		
		if (iChildId == AppGlobal.g_oDeviceManagerElement.get().getId()) {
			for (FrameMembershipInterfaceListener listener : listeners) {
				if (sNote.equals(AppGlobal.g_oDeviceManagerElement.get().getForwardServerRequestNote())) {
					// Raise the event to parent
					if (sStatus.equals("disconnected"))
						listener.FrameMembershipInterface_disconnect();
					else if (sStatus.equals("time_out"))
						listener.FrameMembershipInterface_timeout();
					else
						listener.FrameMembershipInterface_forward(AppGlobal.g_oDeviceManagerElement.get().getValue());
				}
			}
			
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
}
