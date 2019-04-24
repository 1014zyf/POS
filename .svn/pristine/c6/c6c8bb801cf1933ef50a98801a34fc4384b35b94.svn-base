package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import om.InfInterface;
import om.InfVendor;
import om.PosInterfaceConfig;
import commonui.FormDialogBox;
import commonui.FrameNumberPad;
import commonui.FrameNumberPadListener;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FrameLoyaltySvcListener {
	void frameLoyaltySvc_clickCancel();
	void frameLoyaltySvc_clickConfirmAddValue();
	void frameLoyaltySvc_clickConfirmIssueCard();
	void frameLoyaltySvc_clickConfirmSuspendCard();
	void frameLoyaltySvc_clickSetMember(String sType, String sValue, String sPassword);
	void frameLoyaltySvc_clickClearMember();
	void frameLoyaltySvc_clickTransferCard();
	void frameLoyaltySvc_clickPrint(JSONObject oBalanceDetailJSON);
	PosInterfaceConfig frameLoyaltySvc_getPosInterfaceConfig(String sInterfaceType, ArrayList<String> sVendors, String sTitle);
	PosInterfaceConfig frameLoyaltySvc_getLoyaltyInterfaceConfig();
}

public class FrameLoyaltySvc extends VirtualUIFrame implements FrameNumberPadListener, FrameCommonBasketListener, FrameHorizontalTabListListener, FrameTitleHeaderListener {
	public static enum SEARCH_TYPE{number, cardNumber};
	
	public static final String ACTION_ISSUE_CARD = "issue_card";
	public static final String ACTION_ADD_VALUE = "add_value";
	public static final String ACTION_CHECK_VALUE = "check_value";
	public static final String ACTION_SUSPEND_CARD = "suspend_card";
	public static final String ACTION_TRANSFER_CARD = "transfer_card";
	
	private TemplateBuilder m_oTemplateBuilder;
	private VirtualUIFrame m_oFrameLeftContent;
	private FrameTitleHeader m_oFrameTitleHeader;
	private FrameCommonPageContainer m_oFrameCommonPage;
	private FrameCommonBasket<String> m_oFrameMemberDetail;
	private FrameCommonBasket<String> m_oFrameBouns;
	private FrameCommonBasket<String> m_oFrameUpgrade;
	private VirtualUILabel m_oLabelSelectedTag;
	private VirtualUILabel m_oLabelUnSelectedTag;
	private VirtualUILabel m_oLabelCardNumber;
	private VirtualUITextbox m_oTxtboxCardNumber;
	private VirtualUILabel m_oLabelPassword;
	private VirtualUITextbox m_oTxtboxPassword;
	private VirtualUILabel m_oLabelResultListHeader;
	private FrameNumberPad m_oFrameNumberPad;
	private FrameHorizontalTabList m_oFrameHorizontalTabList;
	private VirtualUILabel m_oLabelInformationHeader;
	private VirtualUIList m_oListMemberBasicInformation;
	private VirtualUIList m_oListSVCMemberBasicInformation;
	private VirtualUIList m_oListMemberBonusInformation;
	private VirtualUIList m_oListUpgradeInformation;
	private VirtualUIButton m_oButtonConfirmAddValue;
	private VirtualUIButton m_oButtonConfirmIssueCard;
	private VirtualUIButton m_oButtonConfirmIssueCardContent;
	private VirtualUIButton m_oButtonConfirmTransferCard;
	private VirtualUIButton m_oButtonConfirmSuspendCard;
	private VirtualUIButton m_oButtonPrint;
	
	private boolean m_bMultipleSearchType;
	private boolean m_bSwipeCardSearch;
	private String m_sAction = FrameLoyaltySvc.ACTION_ISSUE_CARD;
	private String m_sTitle = "";
	private JSONObject m_oBalanceDetailJSON;
	
	public static int INFORMATION_TYPE_BASIC = 1;
	public static int INFORMATION_TYPE_BOUNS = 2;
	public static int INFORMATION_TYPE_SVC	 = 3;
	public static int INFORMATION_TYPE_UPGRADE	 = 4;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameLoyaltySvcListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameLoyaltySvcListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameLoyaltySvcListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	//Constructor
	public FrameLoyaltySvc() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameLoyaltySvcListener>();
		m_bMultipleSearchType = false;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraLoyaltySvc.xml");
	}
	
	//Initialize
	public void init(Boolean bIsSvcCheckValue) {
		// Load child elements from template
		/////////////////////////////////////////////////////////////////

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
	    m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
	    m_oFrameTitleHeader.init(true);
	    m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._(m_sTitle));
	    m_oFrameTitleHeader.addListener(this);
	    this.attachChild(m_oFrameTitleHeader);
		
		//Create Left Content Frame
		m_oFrameLeftContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftContent, "fraLeftContent");
		//this.attachChild(m_oFrameLeftContent);
		
		// Selected Tag
		m_oLabelSelectedTag = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSelectedTag, "lblSelectedTag");
		this.attachChild(m_oLabelSelectedTag);
		
		// UnSelected Tag
		m_oLabelUnSelectedTag = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelUnSelectedTag, "lblUnSelectedTag");
		this.attachChild(m_oLabelUnSelectedTag);

// Add by King Cheung 2017-11-29 ---Start---
		m_oFrameCommonPage = new FrameCommonPageContainer();
		m_oTemplateBuilder.buildFrame(m_oFrameCommonPage, "fraCommonPage");
		m_oFrameCommonPage.init(m_oFrameCommonPage.getWidth(), m_oFrameCommonPage.getHeight(), 200, 66, 3, m_oLabelSelectedTag.getForegroundColor(), m_oLabelUnSelectedTag.getForegroundColor(), m_oLabelSelectedTag.getBackgroundColor(), m_oLabelUnSelectedTag.getBackgroundColor(), 30, false, true);
		m_oFrameCommonPage.setTagTextSize(m_oLabelSelectedTag.getTextSize(), m_oLabelUnSelectedTag.getTextSize());
		this.attachChild(m_oFrameCommonPage);
// Add by King Cheung 2017-11-29 ---End---

		// Horizontal List
		m_oFrameHorizontalTabList = new FrameHorizontalTabList();
		m_oTemplateBuilder.buildFrame(m_oFrameHorizontalTabList, "fraHorizontalList");
		m_oFrameHorizontalTabList.init();
		m_oFrameHorizontalTabList.addListener(this);
		//m_oFrameLeftContent.attachChild(m_oFrameHorizontalTabList);
		
		List<String> oTabNameList = new ArrayList<String>();
		oTabNameList.add(AppGlobal.g_oLang.get()._("member_details"));
		oTabNameList.add(AppGlobal.g_oLang.get()._("bonus_expiry_date"));
		oTabNameList.add(AppGlobal.g_oLang.get()._("upgrade_information"));
		m_oFrameHorizontalTabList.addPageTabs(oTabNameList);

		VirtualUIFrame oFramePanelPageSeparator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFramePanelPageSeparator, "fraTagSeparator");
		//m_oFrameLeftContent.attachChild(oFramePanelPageSeparator);
		
		// Information Title
		m_oLabelInformationHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelInformationHeader, "lblBasicInfo");
		m_oLabelInformationHeader.setValue(AppGlobal.g_oLang.get()._("general_information")+":");
		m_oFrameLeftContent.attachChild(m_oLabelInformationHeader);
		
		m_oListMemberBasicInformation = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListMemberBasicInformation, "listMemberBasicInfo");
		m_oFrameLeftContent.attachChild(m_oListMemberBasicInformation);
		
		m_oListSVCMemberBasicInformation = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListSVCMemberBasicInformation, "listSVCMemberBasicInfo");
		m_oFrameLeftContent.attachChild(m_oListSVCMemberBasicInformation);
		
		m_oListMemberBonusInformation = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListMemberBonusInformation, "listMemberBonusInfo");
		m_oFrameLeftContent.attachChild(m_oListMemberBonusInformation);
		
		m_oListUpgradeInformation = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListUpgradeInformation, "listMemberBonusInfo");
		m_oFrameLeftContent.attachChild(m_oListUpgradeInformation);
		
		// Add Benefit List Title
		m_oLabelResultListHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelResultListHeader, "lblResultList");
		m_oLabelResultListHeader.setValue(AppGlobal.g_oLang.get()._("card_holder_information")+":");
		m_oFrameLeftContent.attachChild(m_oLabelResultListHeader);
		
		m_oButtonConfirmIssueCard = new VirtualUIButton();
		m_oButtonConfirmAddValue = new VirtualUIButton();
		m_oButtonConfirmTransferCard = new VirtualUIButton();
		
		//Create Print button
		m_oButtonPrint = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonPrint, "butPrint");
		m_oButtonPrint.setValue(AppGlobal.g_oLang.get()._("print"));
		this.attachChild(m_oButtonPrint);

		//Form Balance Details JSONObject
		m_oBalanceDetailJSON = new JSONObject();
		
// Add by King Cheung 2017-11-27 ---Start---
		m_oFrameMemberDetail = new FrameCommonBasket<String>();
		m_oTemplateBuilder.buildFrame(m_oFrameMemberDetail, "fraCommonBasket");
		m_oFrameMemberDetail.init();
		//m_oFrameLeftContent.attachChild(m_oFrameMemberDetail);

		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();

		iFieldWidths.add(250);
		sFieldValues.add(AppGlobal.g_oLang.get()._("general_information")+":");
		iFieldWidths.add(354);
		sFieldValues.add("");

		m_oFrameMemberDetail.addHeader(iFieldWidths, sFieldValues);
		m_oFrameMemberDetail.setHeaderFormat(40, 18, "");
		m_oFrameMemberDetail.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		m_oFrameBouns = new FrameCommonBasket<String>();
		m_oTemplateBuilder.buildFrame(m_oFrameBouns, "fraCommonBasket");
		m_oFrameBouns.init();

		// Add header
		iFieldWidths = new ArrayList<Integer>();
		sFieldValues = new ArrayList<String>();

		iFieldWidths.add(250);
		sFieldValues.add(AppGlobal.g_oLang.get()._("bonus_expiry_date")+":");
		iFieldWidths.add(354);
		sFieldValues.add("");

		m_oFrameBouns.addHeader(iFieldWidths, sFieldValues);
		m_oFrameBouns.setHeaderFormat(40, 18, "");
		m_oFrameBouns.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		m_oFrameUpgrade = new FrameCommonBasket<String>();
		m_oTemplateBuilder.buildFrame(m_oFrameUpgrade, "fraCommonBasket");
		m_oFrameUpgrade.init();

		// Add header
		iFieldWidths = new ArrayList<Integer>();
		sFieldValues = new ArrayList<String>();

		iFieldWidths.add(250);
		sFieldValues.add(AppGlobal.g_oLang.get()._("detail")+":");
		iFieldWidths.add(354);
		sFieldValues.add("");

		m_oFrameUpgrade.addHeader(iFieldWidths, sFieldValues);
		m_oFrameUpgrade.setHeaderFormat(40, 18, "");
		m_oFrameUpgrade.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		initDetail();
		
		m_oFrameCommonPage.addButton(AppGlobal.g_oLang.get()._("member_details"), m_oFrameMemberDetail);
		m_oFrameCommonPage.addButton(AppGlobal.g_oLang.get()._("bonus_expiry_date"), m_oFrameBouns);
		m_oFrameCommonPage.addButton(AppGlobal.g_oLang.get()._("upgrade_information"), m_oFrameUpgrade);
// Add by King Cheung 2017-11-27 ---End---
		
		if(m_sAction.equals(FrameLoyaltySvc.ACTION_ISSUE_CARD)) {
			m_oButtonConfirmIssueCard = new VirtualUIButton();
			m_oButtonConfirmAddValue = new VirtualUIButton();
			if(!bIsSvcCheckValue){
				m_oTemplateBuilder.buildButton(m_oButtonConfirmIssueCard, "butConfirmAddValue");			
				m_oButtonConfirmIssueCard.setValue(AppGlobal.g_oLang.get()._("confirm_to_issue_card"));
				this.attachChild(m_oButtonConfirmIssueCard);
			}
		}else if(m_sAction.equals(FrameLoyaltySvc.ACTION_TRANSFER_CARD)) {
			m_oButtonConfirmTransferCard = new VirtualUIButton();
			if(!bIsSvcCheckValue){
				m_oTemplateBuilder.buildButton(m_oButtonConfirmTransferCard, "butConfirmAddValue");
				m_oButtonConfirmTransferCard.setValue(AppGlobal.g_oLang.get()._("confirm_to_transfer_card"));
				this.attachChild(m_oButtonConfirmTransferCard);
			}
		}else {
			m_oButtonConfirmIssueCard = new VirtualUIButton();
	   		m_oButtonConfirmAddValue = new VirtualUIButton();
			if(!bIsSvcCheckValue){
				m_oTemplateBuilder.buildButton(m_oButtonConfirmAddValue, "butConfirmAddValue");
				m_oButtonConfirmAddValue.setValue(AppGlobal.g_oLang.get()._("confirm_to_add_value"));
				this.attachChild(m_oButtonConfirmAddValue);
			}
		}
		
		if(m_sAction.equals(FrameLoyaltySvc.ACTION_SUSPEND_CARD)) {
			m_oButtonConfirmSuspendCard = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonConfirmSuspendCard, "butConfirmSuspendCard");
			m_oButtonConfirmSuspendCard.setValue(AppGlobal.g_oLang.get()._("confirm_to_suspend_card"));
			this.attachChild(m_oButtonConfirmSuspendCard);
		}
		
		switchTag(0, false);
		
// Add by King Cheung 2017-11-29 ---Start---
		// Hide NumberPad
		m_oFrameNumberPad.setVisible(false);
		m_oLabelCardNumber.setVisible(false);
		m_oTxtboxCardNumber.setVisible(false);
// Add by King Cheung 2017-11-29 ---End---
	}
	
	//Initialize number pad
	public void initNumberPad(String sAction, String sTitle) {
		// Load child elements from template
		/////////////////////////////////////////////////////////////////
		this.removeAllChildren();
		//this.setWidth(420);
		//this.setLeft(430);
		m_sAction = sAction;
		m_sTitle = sTitle;

		m_oFrameTitleHeader = new FrameTitleHeader();
	    m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
	    m_oFrameTitleHeader.init(false);
	    m_oFrameTitleHeader.setTitle(sTitle);
	    m_oFrameTitleHeader.setWidth(this.getWidth());
	    m_oFrameTitleHeader.resetPostion();
	    this.attachChild(m_oFrameTitleHeader);
		
		// Create Card Number Label
		m_oLabelCardNumber = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCardNumber, "lblCardNumber");
		if(m_sAction.equals(FrameLoyaltySvc.ACTION_TRANSFER_CARD))
			m_oLabelCardNumber.setValue(AppGlobal.g_oLang.get()._("original_card_no"));
		else 
			m_oLabelCardNumber.setValue(AppGlobal.g_oLang.get()._("card_no"));
		this.attachChild(m_oLabelCardNumber);
		
		// Create Card Number Textbox
		m_oTxtboxCardNumber = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxCardNumber, "txtCardNumberInputBox");
		m_oTxtboxCardNumber.setFocusWhenShow(true);
		
		this.attachChild(m_oTxtboxCardNumber);
		
		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.setCancelAndEnterToLeftAndRigth(true);
		m_oFrameNumberPad.setNumPadLeft(400);
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.setEnterBlockUI(true);
		m_oFrameNumberPad.clearEnterSubmitId();
		m_oFrameNumberPad.setEnterSubmitId(m_oTxtboxCardNumber);
		m_oFrameNumberPad.addListener(this);
		this.attachChild(m_oFrameNumberPad);
		
		/*
		if(sTitle.equals(AppGlobal.g_oLang.get()._("svc_add_value"))) {
			// Create Password Label
			m_oLabelPassword = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelPassword, "lblPassword");
			m_oLabelPassword.setValue(AppGlobal.g_oLang.get()._("password"));
			this.attachChild(m_oLabelPassword);
			
			// Create Password Textbox
			m_oTxtboxPassword = new VirtualUITextbox();
			m_oTemplateBuilder.buildTextbox(m_oTxtboxPassword, "txtPasswordInputBox");
			m_oTxtboxPassword.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.NUMBER);
			m_oTxtboxPassword.setInputType(HeroActionProtocol.View.Attribute.InputType.PASSWORD);
			this.attachChild(m_oTxtboxPassword);
		}else {
			//m_oFrameNumberPad.setTop(210);
			//this.setHeight(600);
		}
		*/
	}
	
	//show member details
	public void showMemberDetail(FuncLoyaltySvc oLoyaltySvc){
		// Update the member detail page
		updateDetail(oLoyaltySvc);
		
		// Show the member detail page and hide the result page
		switchTag(0, true);
		
	}
	
	public void initDetail() {
		m_oFrameMemberDetail.clearAllSections();

		ArrayList<String> oFieldTitle = new ArrayList<String>();
		ArrayList<String> oFieldValue = new ArrayList<String>();
		
		oFieldTitle.add(AppGlobal.g_oLang.get()._("card_no"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("status"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("balance"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("value_expire_in_this_month"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("value_expire_in_next_month"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("card_holder_information")+":");
		oFieldTitle.add(AppGlobal.g_oLang.get()._("member_no"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("last_name"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("first_name"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("balance"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("bonus_expire_in_this_month"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("bonus_expire_in_next_month"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("member_type"));
		oFieldValue.add("");
		oFieldValue.add("");
		oFieldValue.add("");
		oFieldValue.add("");
		oFieldValue.add("");
		oFieldValue.add("");
		oFieldValue.add("");
		oFieldValue.add("");
		oFieldValue.add("");
		oFieldValue.add("");
		oFieldValue.add("");
		oFieldValue.add("");
		oFieldValue.add("");
   		
		for(int i = 0 ; i < oFieldTitle.size() ; i++) {
			ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
			ArrayList<String> sFieldValues = new ArrayList<String>();
			ArrayList<String> sFieldAligns = new ArrayList<String>();
			
			iFieldWidths.add(250);
			sFieldValues.add(oFieldTitle.get(i));
			sFieldAligns.add("");

			iFieldWidths.add(354);
			sFieldValues.add(oFieldValue.get(i));
			sFieldAligns.add("");
			
			if(oFieldTitle.get(i).equals(AppGlobal.g_oLang.get()._("card_holder_information")+":")) {
				m_oFrameMemberDetail.addItem(0, i, 40, iFieldWidths, sFieldValues, sFieldAligns, null, null);
				m_oFrameMemberDetail.setFieldTextSize(0, i, 0, 18);
				m_oFrameMemberDetail.setFieldTextSize(0, i, 1, 16);
				continue;
			}
			
			m_oFrameMemberDetail.addItem(0, i, 38, iFieldWidths, sFieldValues, sFieldAligns, null, null);
			m_oFrameMemberDetail.setFieldTextSize(0, i, 0, 16);
			m_oFrameMemberDetail.setFieldTextSize(0, i, 1, 16);
		}
	}
	
	//update details
	public void updateDetail(FuncLoyaltySvc oLoyaltySvc) {
		if(m_oListMemberBasicInformation!= null)
			m_oListMemberBasicInformation.removeAllChildren();
		if(m_oListSVCMemberBasicInformation!= null)
			m_oListSVCMemberBasicInformation.removeAllChildren();
		if(m_oListMemberBonusInformation != null)
			m_oListMemberBonusInformation.removeAllChildren();
		if(m_oListUpgradeInformation != null)
			m_oListUpgradeInformation.removeAllChildren();
		
		m_oFrameMemberDetail.clearAllSections();
		m_oFrameBouns.clearAllSections();
		m_oFrameUpgrade.clearAllSections();
		
    	ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
    	ArrayList<String> sFieldValues = new ArrayList<String>();
    	ArrayList<String> sFieldAligns = new ArrayList<String>();
		
    	int iCount = 0;
		for(Entry<String, BigDecimal> oEntry:oLoyaltySvc.getBounsRecords().entrySet()) {
			//addMemberInfo(oEntry.getKey(), oEntry.getValue().toString(), INFORMATION_TYPE_BOUNS);

			iFieldWidths.add(250);
			sFieldValues.add(oEntry.getKey());
			sFieldAligns.add("");
			
			iFieldWidths.add(354);
			sFieldValues.add(oEntry.getValue().toString());
			sFieldAligns.add("");
			m_oFrameBouns.addItem(0, iCount, 38, iFieldWidths, sFieldValues, sFieldAligns, null, null);
			m_oFrameBouns.setFieldTextSize(0, iCount, 0, 16);
			m_oFrameBouns.setFieldTextSize(0, iCount, 1, 16);
			iCount++;
		}

		ArrayList<String> oFieldTitle = new ArrayList<String>();
		ArrayList<String> oFieldValue = new ArrayList<String>();
		
		oFieldTitle.add(AppGlobal.g_oLang.get()._("card_no"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("status"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("balance"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("value_expire_in_this_month"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("value_expire_in_next_month"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("card_holder_information")+":");
		oFieldValue.add(oLoyaltySvc.getCardNo());
		oFieldValue.add(oLoyaltySvc.getStatus());
		oFieldValue.add(oLoyaltySvc.getSVCBalance().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		oFieldValue.add(oLoyaltySvc.getBonusExpire(oLoyaltySvc.getBalanceRecords(), false).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
		oFieldValue.add(oLoyaltySvc.getBonusExpire(oLoyaltySvc.getBalanceRecords(), true).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
		oFieldValue.add("");
		
		//addMemberInfo(AppGlobal.g_oLang.get()._("card_no"), oLoyaltySvc.getCardNo(), INFORMATION_TYPE_SVC);
		//addMemberInfo(AppGlobal.g_oLang.get()._("status"), oLoyaltySvc.getStatus(), INFORMATION_TYPE_SVC);
		//addMemberInfo(AppGlobal.g_oLang.get()._("balance"), oLoyaltySvc.getSVCBalance().setScale(2, BigDecimal.ROUND_HALF_UP).toString(), INFORMATION_TYPE_SVC);
		//addMemberInfo(AppGlobal.g_oLang.get()._("value_expire_in_this_month"), oLoyaltySvc.getBonusExpire(oLoyaltySvc.getBalanceRecords(), false).setScale(0, BigDecimal.ROUND_HALF_UP).toString(), INFORMATION_TYPE_SVC);
		//addMemberInfo(AppGlobal.g_oLang.get()._("value_expire_in_next_month"), oLoyaltySvc.getBonusExpire(oLoyaltySvc.getBalanceRecords(), true).setScale(0, BigDecimal.ROUND_HALF_UP).toString(), INFORMATION_TYPE_SVC);

		oFieldTitle.add(AppGlobal.g_oLang.get()._("member_no"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("last_name"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("first_name"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("balance"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("bonus_expire_in_this_month"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("bonus_expire_in_next_month"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("member_type"));
		if(!oLoyaltySvc.getMemberNo().isEmpty()){
			oFieldValue.add(oLoyaltySvc.getMemberNo());
			oFieldValue.add(oLoyaltySvc.getLastName());
			oFieldValue.add(oLoyaltySvc.getFirstName());
			oFieldValue.add(oLoyaltySvc.getBalance().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			oFieldValue.add(oLoyaltySvc.getBonusExpire(oLoyaltySvc.getBounsRecords(), false).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			oFieldValue.add(oLoyaltySvc.getBonusExpire(oLoyaltySvc.getBounsRecords(), true).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			oFieldValue.add(oLoyaltySvc.readMemeberType(oLoyaltySvc.getType()));
			//addMemberInfo(AppGlobal.g_oLang.get()._("member_no"), oLoyaltySvc.getMemberNo(), INFORMATION_TYPE_BASIC);
			//addMemberInfo(AppGlobal.g_oLang.get()._("last_name"), oLoyaltySvc.getLastName(), INFORMATION_TYPE_BASIC);
			//addMemberInfo(AppGlobal.g_oLang.get()._("first_name"), oLoyaltySvc.getFirstName(), INFORMATION_TYPE_BASIC);
			//addMemberInfo(AppGlobal.g_oLang.get()._("balance"), oLoyaltySvc.getBalance().setScale(0, BigDecimal.ROUND_HALF_UP).toString(), INFORMATION_TYPE_BASIC);
			//addMemberInfo(AppGlobal.g_oLang.get()._("bonus_expire_in_this_month"), oLoyaltySvc.getBonusExpire(oLoyaltySvc.getBounsRecords(), false).setScale(0, BigDecimal.ROUND_HALF_UP).toString(), INFORMATION_TYPE_BASIC);
			//addMemberInfo(AppGlobal.g_oLang.get()._("bonus_expire_in_next_month"), oLoyaltySvc.getBonusExpire(oLoyaltySvc.getBounsRecords(), true).setScale(0, BigDecimal.ROUND_HALF_UP).toString(), INFORMATION_TYPE_BASIC);
			//addMemberInfo(AppGlobal.g_oLang.get()._("member_type"), oLoyaltySvc.readMemeberType(oLoyaltySvc.getType()), INFORMATION_TYPE_BASIC);
		}
		else{
			oFieldValue.add("");
			oFieldValue.add("");
			oFieldValue.add("");
			oFieldValue.add("");
			oFieldValue.add("");
			oFieldValue.add("");
			oFieldValue.add("");
			//addMemberInfo(AppGlobal.g_oLang.get()._("member_no"), "", INFORMATION_TYPE_BASIC);
			//addMemberInfo(AppGlobal.g_oLang.get()._("last_name"), "", INFORMATION_TYPE_BASIC);
			//addMemberInfo(AppGlobal.g_oLang.get()._("first_name"), "", INFORMATION_TYPE_BASIC);
			//addMemberInfo(AppGlobal.g_oLang.get()._("balance"), "", INFORMATION_TYPE_BASIC);
			//addMemberInfo(AppGlobal.g_oLang.get()._("bonus_expire_in_this_month"), "", INFORMATION_TYPE_BASIC);
			//addMemberInfo(AppGlobal.g_oLang.get()._("bonus_expire_in_next_month"), "", INFORMATION_TYPE_BASIC);
			//addMemberInfo(AppGlobal.g_oLang.get()._("member_type"), "", INFORMATION_TYPE_BASIC);
		}
		
		for(int i = 0 ; i < oFieldTitle.size() ; i++) {
			iFieldWidths = new ArrayList<Integer>();
			sFieldValues = new ArrayList<String>();
			sFieldAligns = new ArrayList<String>();
			
			iFieldWidths.add(250);
			sFieldValues.add(oFieldTitle.get(i));
			sFieldAligns.add("");

			iFieldWidths.add(354);
			sFieldValues.add(oFieldValue.get(i));
			sFieldAligns.add("");

			if(oFieldTitle.get(i).equals(AppGlobal.g_oLang.get()._("card_holder_information")+":")) {
				m_oFrameMemberDetail.addItem(0, i, 40, iFieldWidths, sFieldValues, sFieldAligns, null, null);
				m_oFrameMemberDetail.setFieldTextSize(0, i, 0, 18);
				m_oFrameMemberDetail.setFieldTextSize(0, i, 1, 16);
				continue;
			}
			
			m_oFrameMemberDetail.addItem(0, i, 38, iFieldWidths, sFieldValues, sFieldAligns, null, null);
			m_oFrameMemberDetail.setFieldTextSize(0, i, 0, 16);
			m_oFrameMemberDetail.setFieldTextSize(0, i, 1, 16);
		}
		
		oFieldTitle = new ArrayList<String>();
		oFieldValue = new ArrayList<String>();
		
		oFieldTitle.add(AppGlobal.g_oLang.get()._("life_time_spending"));
		oFieldValue.add(oLoyaltySvc.getTotalSpending());
		oFieldTitle.add(AppGlobal.g_oLang.get()._("remain_for_upgrade"));
		oFieldValue.add(oLoyaltySvc.getRemainForUpgrade());
		//addMemberInfo(AppGlobal.g_oLang.get()._("life_time_spending"), oLoyaltySvc.getTotalSpending(), INFORMATION_TYPE_UPGRADE);
		//addMemberInfo(AppGlobal.g_oLang.get()._("remain_for_upgrade"), oLoyaltySvc.getRemainForUpgrade(), INFORMATION_TYPE_UPGRADE);
		for(Entry<String, String> entry : oLoyaltySvc.getSpendingVariable().entrySet()){
			oFieldTitle.add(AppGlobal.g_oLang.get()._("last_upgrade_date"));
			oFieldValue.add(entry.getKey());
			oFieldTitle.add(AppGlobal.g_oLang.get()._("last_upgrade_information"));
			oFieldValue.add(entry.getValue());
			//addMemberInfo(AppGlobal.g_oLang.get()._("last_upgrade_date"), entry.getKey(), INFORMATION_TYPE_UPGRADE);
			//addMemberInfo(AppGlobal.g_oLang.get()._("last_upgrade_information"), entry.getValue(), INFORMATION_TYPE_UPGRADE);
		}
		
		for(int i = 0 ; i < oFieldTitle.size() ; i++) {
			iFieldWidths = new ArrayList<Integer>();
			sFieldValues = new ArrayList<String>();
			sFieldAligns = new ArrayList<String>();
			
			iFieldWidths.add(250);
			sFieldValues.add(oFieldTitle.get(i));
			sFieldAligns.add("");

			iFieldWidths.add(354);
			sFieldValues.add(oFieldValue.get(i));
			sFieldAligns.add("");
			
			m_oFrameUpgrade.addItem(0, i, 38, iFieldWidths, sFieldValues, sFieldAligns, null, null);
			m_oFrameUpgrade.setFieldTextSize(0, i, 0, 16);
			m_oFrameUpgrade.setFieldTextSize(0, i, 1, 16);
		}
		
		//form balance details json
		try {
			m_oBalanceDetailJSON.put("cardNumber", oLoyaltySvc.getCardNo());
			m_oBalanceDetailJSON.put("status", oLoyaltySvc.getStatus());
			m_oBalanceDetailJSON.put("cardBalance", oLoyaltySvc.getSVCBalance().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			m_oBalanceDetailJSON.put("balanceExpireThisMonth", oLoyaltySvc.getBonusExpire(oLoyaltySvc.getBalanceRecords(), false).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			m_oBalanceDetailJSON.put("balanceExpireNextMonth", oLoyaltySvc.getBonusExpire(oLoyaltySvc.getBalanceRecords(), true).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			m_oBalanceDetailJSON.put("outletId", AppGlobal.g_oFuncOutlet.get().getOutletId());
			m_oBalanceDetailJSON.put("shopId", AppGlobal.g_oFuncOutlet.get().getShopId());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//add member information
	private void addMemberInfo(String sTitle, String sContent, int iType) {
		VirtualUIFrame oFrameBasicDetail = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameBasicDetail, "fraMemberBasicInfo");
		
		VirtualUILabel oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelTitle, "lblInfoTitle");
		oLabelTitle.setValue(sTitle);
		oFrameBasicDetail.attachChild(oLabelTitle);

		VirtualUILabel oLabelContent = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelContent, "lblInfoContent");
		oLabelContent.setValue(sContent);
		oFrameBasicDetail.attachChild(oLabelContent);
		
		if(iType == INFORMATION_TYPE_BASIC)
			m_oListMemberBasicInformation.attachChild(oFrameBasicDetail);
		else if (iType == INFORMATION_TYPE_SVC)
			m_oListSVCMemberBasicInformation.attachChild(oFrameBasicDetail);
		else if (iType == INFORMATION_TYPE_BOUNS)
			m_oListMemberBonusInformation.attachChild(oFrameBasicDetail);
		else if (iType == INFORMATION_TYPE_UPGRADE)
			m_oListUpgradeInformation.attachChild(oFrameBasicDetail);
	}
	
	//switch tag
	public void switchTag(int iTagIndex, boolean bUpdateSelectedTabColor) {
		if (bUpdateSelectedTabColor)
			m_oFrameHorizontalTabList.changePageTab(iTagIndex);
		
		switch (iTagIndex) {
			case 0:	// Member Detail Tag
				m_oListMemberBasicInformation.setVisible(true);
				m_oListSVCMemberBasicInformation.setVisible(true);
				m_oListMemberBonusInformation.setVisible(false);
				m_oLabelResultListHeader.setVisible(true);
				if(m_sAction.equals(FrameLoyaltySvc.ACTION_ISSUE_CARD)) {
					m_oButtonConfirmIssueCard.setVisible(true);
					if(m_oButtonConfirmAddValue != null)
						m_oButtonConfirmAddValue.setVisible(false);
				}else {
					m_oButtonConfirmIssueCard.setVisible(false);
					if(m_oButtonConfirmAddValue != null)
						m_oButtonConfirmAddValue.setVisible(true);
				}
				
				if(m_sAction.equals(FrameLoyaltySvc.ACTION_SUSPEND_CARD))
					m_oButtonConfirmSuspendCard.setVisible(true);
				m_oListUpgradeInformation.setVisible(false);
				m_oLabelInformationHeader.setValue(AppGlobal.g_oLang.get()._("general_information")+":");
				break;
			case 1: // Bonus Record Page
				m_oListMemberBasicInformation.setVisible(false);
				m_oListSVCMemberBasicInformation.setVisible(false);
				m_oListMemberBonusInformation.setVisible(true);
				m_oLabelResultListHeader.setVisible(false);
				m_oButtonConfirmIssueCard.setVisible(true);
				if(m_oButtonConfirmAddValue != null)
					m_oButtonConfirmAddValue.setVisible(false);
				if(m_sAction.equals(FrameLoyaltySvc.ACTION_SUSPEND_CARD))
					m_oButtonConfirmSuspendCard.setVisible(true);
				m_oListUpgradeInformation.setVisible(false);
				m_oLabelInformationHeader.setValue(AppGlobal.g_oLang.get()._("bonus_expiry_date")+":");
				break;
			case 2: //Upgrade Information
				m_oListMemberBasicInformation.setVisible(false);
				m_oListSVCMemberBasicInformation.setVisible(false);
				m_oListMemberBonusInformation.setVisible(false);
				m_oLabelResultListHeader.setVisible(false);
				if(m_oButtonConfirmAddValue != null)
					m_oButtonConfirmAddValue.setVisible(false);
				if(m_sAction.equals(FrameLoyaltySvc.ACTION_SUSPEND_CARD))
					m_oButtonConfirmSuspendCard.setVisible(true);
				m_oListUpgradeInformation.setVisible(true);
				m_oLabelInformationHeader.setValue(AppGlobal.g_oLang.get()._("detail")+":");
				break;
		}
	}
	
	//update debit
	public void updateDebit(BigDecimal oBonusDebit, BigDecimal oBonusBalance){
		boolean iFound = false;
		for(VirtualUIBasicElement oBasicElement:m_oListMemberBasicInformation.getChilds()){
			for(VirtualUIBasicElement oVirtualUIBasicElement:oBasicElement.getChilds()){
				if(iFound)
					oVirtualUIBasicElement.setValue(oBonusDebit.toString()+"/"+oBonusBalance.toString());
				if(oVirtualUIBasicElement.getValue().equals(AppGlobal.g_oLang.get()._("total_debit")))
					iFound = true;
					
			}
		}
	}

	//check is swipe card
	public boolean isSwipeCard() {
		return m_bSwipeCardSearch;
	}
	
	public boolean getAllowForKeyIn(){
		String sKeyInControls[] = null;
		boolean bResult = true;
		int iSetup = 0;
		sKeyInControls = FuncLoyaltySvc.getKeyInControls();
		if(sKeyInControls != null)
		{
			switch(m_sAction)
			{
				case FrameLoyaltySvc.ACTION_ISSUE_CARD:
					if(sKeyInControls.length >= 5)
						iSetup = Integer.parseInt(sKeyInControls[4]);
					break;
				case ACTION_ADD_VALUE:
					if(sKeyInControls.length >= 6)
						iSetup = Integer.parseInt(sKeyInControls[5]);
					break;
				case ACTION_CHECK_VALUE:
					if(sKeyInControls.length >= 4)
						iSetup = Integer.parseInt(sKeyInControls[3]);
					break;
				case ACTION_SUSPEND_CARD:
					if(sKeyInControls.length >= 7)
						iSetup = Integer.parseInt(sKeyInControls[6]);
					break;
				case ACTION_TRANSFER_CARD:
					if(sKeyInControls.length >= 2)
						iSetup = Integer.parseInt(sKeyInControls[1]);

					break;
			}			
		}
		if(iSetup == 1)
			bResult = false;
		
		return bResult;
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (m_oButtonConfirmAddValue != null && iChildId == m_oButtonConfirmAddValue.getId()) {
			for (FrameLoyaltySvcListener listener : listeners) {
				listener.frameLoyaltySvc_clickConfirmAddValue();
				break;
			}
			bMatchChild = true;
		}
		else if (m_oButtonConfirmIssueCard != null && iChildId == m_oButtonConfirmIssueCard.getId()) {
			for (FrameLoyaltySvcListener listener : listeners) {
				listener.frameLoyaltySvc_clickConfirmIssueCard();
				break;
			}
			bMatchChild = true;
		}
		else if (m_oButtonConfirmSuspendCard != null && iChildId == m_oButtonConfirmSuspendCard.getId()) {
			for (FrameLoyaltySvcListener listener : listeners) {
				listener.frameLoyaltySvc_clickConfirmSuspendCard();
				break;
			}
			bMatchChild = true;
		}
		else if (m_oButtonConfirmTransferCard != null && iChildId == m_oButtonConfirmTransferCard.getId()) {
			for (FrameLoyaltySvcListener listener : listeners) {
				listener.frameLoyaltySvc_clickTransferCard();
				break;
			}
			bMatchChild = true;
		}
		else if (m_oButtonPrint != null && iChildId == m_oButtonPrint.getId()) {
			for (FrameLoyaltySvcListener listener : listeners) {
				listener.frameLoyaltySvc_clickPrint(m_oBalanceDetailJSON);
				break;
			}
			bMatchChild = true;
		}
		return bMatchChild;
	}

	@Override
	public void FrameNumberPad_clickEnter() {
		if(!getAllowForKeyIn())
		{
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("support_swipe_card_only"));
			oFormDialogBox.show();
			oFormDialogBox = null;
			this.m_oTxtboxCardNumber.setValue("");
			return;
		}
		
		for (FrameLoyaltySvcListener listener : listeners){
			listener.frameLoyaltySvc_clickSetMember(FrameLoyaltySvc.SEARCH_TYPE.number.name(), m_oTxtboxCardNumber.getValue(), "");
			break;
		}
	}

	@Override
	public void FrameNumberPad_clickCancel() {
		m_oTxtboxCardNumber.setValue("");
		for (FrameLoyaltySvcListener listener : listeners) {
			listener.frameLoyaltySvc_clickCancel();
			break;
		}
	}

	@Override
	public void FrameNumberPad_clickNumber(String string) {
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId,
			String sNote) {
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId,
			int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
	}

	@Override
	public void frameHorizontalTabList_clickTab(int iTabIndex, int iId) {
		if(m_oFrameHorizontalTabList.getId() == iId)
			switchTag(iTabIndex, false);
	}

	@Override
	public boolean valueChanged(int iChildId, String sNote) {
		boolean bMatchChild = false;

		String sSwipeCardValue = "";
		ClsActiveClient oActiveClient = AppGlobal.getActiveClient();
		if (oActiveClient != null) {
			if (iChildId == oActiveClient.getSwipeCardReaderElement().getId()
					&& oActiveClient.getSwipeCardReaderElement().getValueChangedServerRequestNote()
							.equals(FuncMSR.FRAME_SWIPE_CARD_DEFAULT)) {
				if (oActiveClient.getSwipeCardReaderElement().getValue().length() > 0) {
					sSwipeCardValue = oActiveClient.getSwipeCardReaderElement().getValue().replace("\r", "")
							.replace("\n", "");
				}
				bMatchChild = true;
				
				String sMsrCode = "";
				String sCardNo = "";
				
				//get the interface configure
				PosInterfaceConfig oCurrentLoyaltyInterfaceConfig = null;
				for (FrameLoyaltySvcListener listener : listeners) {
					oCurrentLoyaltyInterfaceConfig = listener.frameLoyaltySvc_getLoyaltyInterfaceConfig();
					break;
				}
				
				//Support MSR by checking interface vendor key: GIVE X / GM_LOYALTY_SVC
				if(oCurrentLoyaltyInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GIVEX) 
						|| oCurrentLoyaltyInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY_SVC)){
					JSONObject oPosInterfaceConfigJson = new JSONObject();
					oPosInterfaceConfigJson = oCurrentLoyaltyInterfaceConfig.getInterfaceConfig();
					//get msr code from interface setup
					if(oPosInterfaceConfigJson.has("general_setup") && oPosInterfaceConfigJson.optJSONObject("general_setup").has("params") && oPosInterfaceConfigJson.optJSONObject("general_setup").optJSONObject("params").has("msr_code"))
						sMsrCode = oPosInterfaceConfigJson.optJSONObject("general_setup").optJSONObject("params").optJSONObject("msr_code").optString("value");
					
					// Get the configure from interface module
					if(!sMsrCode.isEmpty()){
						List<PosInterfaceConfig> oMsrInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
						// Get the configure from interface module
						oMsrInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PERIPHERAL_DEVICE);
						
						for (PosInterfaceConfig oMsrInterfaceConfig : oMsrInterfaceConfigList) {
							if (oMsrInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_MSR)
									&& oMsrInterfaceConfig.getInterfaceCode().equals(sMsrCode)) {
								// Capture information from swipe card
								FuncMSR oFuncMSR = new FuncMSR();
								int iErrorCode = oFuncMSR.processCardContent(sSwipeCardValue,
										oMsrInterfaceConfig.getInterfaceConfig());
				
								// Get the necessary value
								sCardNo = oFuncMSR.getCardNo();
								if (iErrorCode == FuncMSR.ERROR_CODE_MISSING_SETUP) {
									FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
											this.getParentForm());
									oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
									oFormDialogBox.setMessage(oFuncMSR.getLastErrorMessage());
									oFormDialogBox.show();
									oFormDialogBox = null;
									break;
								}
							}
						}
					}
					
					m_oTxtboxCardNumber.setValue(sCardNo);
					if (!sMsrCode.isEmpty() && !m_oTxtboxCardNumber.getValue().isEmpty()) {
						for (FrameLoyaltySvcListener listener : listeners) {
							listener.frameLoyaltySvc_clickSetMember(FrameLoyaltySvc.SEARCH_TYPE.number.name(),
									m_oTxtboxCardNumber.getValue(), "");
							break;
						}
					} else {
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
								this.getParentForm());
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_capture_card"));
						oFormDialogBox.show();
						oFormDialogBox = null;
					}
				}
			}
		}
		return bMatchChild;
	}
	
	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex,
			String sNote) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void FrameTitleHeader_close() {
		for (FrameLoyaltySvcListener listener : listeners) {
			// Raise the event to parent
			listener.frameLoyaltySvc_clickCancel();
		}
	}
}
