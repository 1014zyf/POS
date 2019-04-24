package app;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
interface FrameLoyaltyListener {
	void frameLoyalty_clickCancel();
	void frameLoyalty_clickSetMember(String sType, String sValue);
	void frameLoyalty_clickClearMember();
	boolean frameLoyalty_clickSearch(String sType, String sValue);
	void frameLoyalty_clickUseBenefit(int iIndex);
	void frameLoyalty_prevPage();
	void frameLoyalty_nextPage();
	PosInterfaceConfig frameLoyalty_getPosInterfaceConfig(String sInterfaceType, ArrayList<String> sVendors, String sTitle);
}

public class FrameLoyalty extends VirtualUIFrame implements FrameNumberPadListener, FrameCommonBasketListener, FrameHorizontalTabListListener, FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;

	private VirtualUIFrame m_oFrameLeftContent;
	
	// Edit by King Cheung 2017-10-18
	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUITextbox m_oTxtboxSearchValue;
	private VirtualUILabel m_oLabelSearchHeader;
	private VirtualUILabel m_oLabelInformationHeader;
	private VirtualUILabel m_oLabelResultListHeader;
	private VirtualUIButton m_oButtonSearchByMemberNo;
	private VirtualUIButton m_oButtonSearchByCardNo;
	private FrameNumberPad m_oFrameNumberPad;
	private FrameHorizontalTabList m_oFrameHorizontalTabList;
	private FrameCommonBasket m_oBasketResultList;
	private FrameHorizontalTabList m_oFrameSearchTagList;
	private VirtualUIList m_oListMemberBasicInformation;
	private VirtualUIList m_oListMemberBonusInformation;
	private VirtualUITextbox m_oTxtboxGivenName;
	private VirtualUILabel m_oLabelGivenName;
	private VirtualUITextbox m_oTxtboxSurname;
	private VirtualUILabel m_oLabelSurname;
	private VirtualUITextbox m_oTxtChineseName;
	private VirtualUILabel m_oLabelChineseName;

	// Add by King Cheung 2017-10-18 ---Start---
	private VirtualUIFrame m_oFrameTab1;
	private VirtualUIFrame m_oFrameTab2;
	// Add by King Cheung 2017-10-18 ---End---
	
	private VirtualUIFrame m_oFramePage;
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIImage m_oImgButtonNextPage;
	private int m_iCurrentPageStartNo;
	private int m_iMaxPageNo;
	private boolean m_bSearchByNumber = true;
	
	private boolean m_bMultipleSearchType;

	private boolean m_bSwipeCardSearch;
	
	
	public static enum SEARCH_TYPE{number, cardNumber};
	
	public static int INFORMATION_TYPE_BASIC = 1;
	public static int INFORMATION_TYPE_BOUNS = 2;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameLoyaltyListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameLoyaltyListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameLoyaltyListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	//Constructor
	public FrameLoyalty() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameLoyaltyListener>();
		m_bMultipleSearchType = false;
		
		m_iCurrentPageStartNo = 0;
		m_iMaxPageNo = 1;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraLoyalty.xml");
	}
	
	public void init(String sTitle) {
		// Load child elements from template
		/////////////////////////////////////////////////////////////////
		
		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
	    m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
	    m_oFrameTitleHeader.init(true);
	    m_oFrameTitleHeader.addListener(this);
	    m_oFrameTitleHeader.setTitle(sTitle);
	    this.attachChild(m_oFrameTitleHeader);

		//Create Left Content Frame
		m_oFrameLeftContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftContent, "fraLeftContent");
		this.attachChild(m_oFrameLeftContent);

		// Horizontal List
		m_oFrameHorizontalTabList = new FrameHorizontalTabList();
		m_oTemplateBuilder.buildFrame(m_oFrameHorizontalTabList, "fraHorizontalList");
		m_oFrameHorizontalTabList.init();
		m_oFrameHorizontalTabList.addListener(this);
		m_oFrameLeftContent.attachChild(m_oFrameHorizontalTabList);
		
		List<String> oTabNameList = new ArrayList<String>();
		//oTabNameList.add(AppGlobal.g_oLang.get()._("search_result"));
		oTabNameList.add(AppGlobal.g_oLang.get()._("member_detail"));
		oTabNameList.add(AppGlobal.g_oLang.get()._("bonus_expiry_date"));
		m_oFrameHorizontalTabList.addPageTabs(oTabNameList);

		VirtualUIFrame oFramePanelPageSeparator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFramePanelPageSeparator, "fraTagSeparator");
		m_oFrameLeftContent.attachChild(oFramePanelPageSeparator);

		// Add by King Cheung 2017-10-18 ---Start---
		FrameCommonPageContainer oFramePageContainer = new FrameCommonPageContainer();
		m_oTemplateBuilder.buildFrame(oFramePageContainer, "fraLeftContent");
		oFramePageContainer.init(oFramePageContainer.getWidth(), oFramePageContainer.getHeight(), 0, 33, 4, "#0055B8", "#999999", "", "", 48, false, false);
		this.attachChild(oFramePageContainer);
		
		m_oFrameTab1 = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTab1, "fraLeftContent");
		//this.attachChild(oFrameTab1);

		m_oFrameTab2 = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTab2, "fraLeftContent");
		
		//this.attachChild(oFrameTab2);
		// Add by King Cheung 2017-10-18 ---End---
		
		// Information Title
		m_oLabelInformationHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelInformationHeader, "lblBasicInfo");
		m_oLabelInformationHeader.setValue(AppGlobal.g_oLang.get()._("general_information")+":");
		// Edit by King Cheung 2017-10-18
		m_oFrameTab1.attachChild(m_oLabelInformationHeader);
		
		// Add by King Cheung 2017-10-18 ---Start---
		m_oLabelInformationHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelInformationHeader, "lblBasicInfo");
		m_oLabelInformationHeader.setValue(AppGlobal.g_oLang.get()._("bonus_expiry_date")+":");
		m_oFrameTab2.attachChild(m_oLabelInformationHeader);
		// Add by King Cheung 2017-10-18 ---End---
		
		m_oListMemberBasicInformation = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListMemberBasicInformation, "listMemberBasicInfo");
		// Edit by King Cheung 2017-10-18
		m_oFrameTab1.attachChild(m_oListMemberBasicInformation);
		
		m_oListMemberBonusInformation = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListMemberBonusInformation, "listMemberBonusInfo");
		// Edit by King Cheung 2017-10-18
		m_oFrameTab2.attachChild(m_oListMemberBonusInformation);
		
		m_oBasketResultList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oBasketResultList, "fraResultList");
		m_oBasketResultList.init();
		m_oBasketResultList.addListener(this);
		// Edit by King Cheung 2017-10-18
		m_oFrameTab1.attachChild(m_oBasketResultList);
		
		// Add Benefit List Title
		m_oLabelResultListHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelResultListHeader, "lblResultList");
		m_oLabelResultListHeader.setValue(AppGlobal.g_oLang.get()._("benefit_list")+":");
		// Edit by King Cheung 2017-10-18
		m_oFrameTab1.attachChild(m_oLabelResultListHeader);
		
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		iFieldWidths.add(150);
		sFieldValues.add(AppGlobal.g_oLang.get()._("type"));
		iFieldWidths.add(470);
		sFieldValues.add(AppGlobal.g_oLang.get()._("description"));
		iFieldWidths.add(200);
		sFieldValues.add(AppGlobal.g_oLang.get()._("bonus"));
		m_oBasketResultList.addHeader(iFieldWidths, sFieldValues);
		m_oBasketResultList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		m_oBasketResultList.setHeaderTextAlign(1, HeroActionProtocol.View.Attribute.TextAlign.LEFT+", "+HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		m_oBasketResultList.setHeaderPadding(1, "0,0,0,30");
		
		addMemberInfo(AppGlobal.g_oLang.get()._("member_no"), "", INFORMATION_TYPE_BASIC);
   		addMemberInfo(AppGlobal.g_oLang.get()._("member_name"), "", INFORMATION_TYPE_BASIC);
   		addMemberInfo(AppGlobal.g_oLang.get()._("campaign_code"), "", INFORMATION_TYPE_BASIC);
   		addMemberInfo(AppGlobal.g_oLang.get()._("campaign_name"), "", INFORMATION_TYPE_BASIC);
   		addMemberInfo(AppGlobal.g_oLang.get()._("total_debit"), "", INFORMATION_TYPE_BASIC);
		
		//page up and down
		//Create Page label
		m_oFramePage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePage, "fraPage");
		
		// Create prev page button
		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevPage");
		// Edit by King Cheung 2017-10-18
		m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
		m_oImgButtonPrevPage.allowClick(true);
		m_oImgButtonPrevPage.setClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.allowLongClick(true);
		m_oImgButtonPrevPage.setLongClickServerRequestBlockUI(false);
		// Delete by King Cheung 2017-10-18
		//m_oImgButtonPrevPage.setVisible(false);
		// Edit by King Cheung 2017-10-18
		m_oFrameTab1.attachChild(m_oImgButtonPrevPage);
		
		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		// Edit by King Cheung 2017-10-18
		m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.allowLongClick(true);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		// Delete by King Cheung 2017-10-18
		//m_oImgButtonNextPage.setVisible(false);
		// Edit by King Cheung 2017-10-18
		m_oFrameTab1.attachChild(m_oImgButtonNextPage);
		
		// Delete by King Cheung 2017-10-18 ---Start---
		/*
		VirtualUIImage oImagePage = new VirtualUIImage();
		oImagePage.setWidth(m_oFramePage.getWidth());
		oImagePage.setHeight(m_oFramePage.getHeight());
		oImagePage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_page_bg.png");
		m_oFramePage.attachChild(oImagePage);
		*/
		// Delete by King Cheung 2017-10-18 ---End---
		
		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oLblPage.setWidth(m_oFramePage.getWidth());
		m_oLblPage.setHeight(m_oFramePage.getHeight());
		m_oFramePage.attachChild(m_oLblPage);
		// Edit by King Cheung 2017-10-18
		m_oFrameTab1.attachChild(m_oFramePage);
		// Delete by King Cheung 2017-10-18
		//m_oFramePage.setVisible(false);

		// Add by King Cheung 2017-10-18 ---Start---
		oFramePageContainer.addButton(AppGlobal.g_oLang.get()._("member_detail"), m_oFrameTab1);
		oFramePageContainer.addButton(AppGlobal.g_oLang.get()._("bonus_expiry_date"), m_oFrameTab2);
		// Add by King Cheung 2017-10-18 ---End---
		
		switchTag(0, false);
		this.updateButtonColor(FrameLoyalty.SEARCH_TYPE.number.name());
	}
	
	public void initNumberPad(String sTitle) {
		// Load child elements from template
		/////////////////////////////////////////////////////////////////

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
	    m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
	    m_oFrameTitleHeader.init(false);
	    m_oFrameTitleHeader.setTitle(sTitle);
	    m_oFrameTitleHeader.setWidth(this.getWidth());
	    m_oFrameTitleHeader.resetPostion();
	    this.attachChild(m_oFrameTitleHeader);
		
		// Create prev page button
		m_oImgButtonPrevPage = new VirtualUIImage();
		this.attachChild(m_oImgButtonPrevPage);
		
		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		this.attachChild(m_oImgButtonNextPage);
		
		m_oTxtboxSearchValue = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxSearchValue, "txtInputBox");
		m_oTxtboxSearchValue.setFocusWhenShow(true);
		this.attachChild(m_oTxtboxSearchValue);
		
		m_oLabelSearchHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSearchHeader, "lblSearchHeader");
		m_oLabelSearchHeader.setValue(AppGlobal.g_oLang.get()._("search_by")+":");
		this.attachChild(m_oLabelSearchHeader);
		
		m_oButtonSearchByCardNo = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSearchByCardNo, "butSearchByCardNo");
		m_oButtonSearchByCardNo.addClickServerRequestSubmitElement(m_oTxtboxSearchValue);
		m_oButtonSearchByCardNo.setValue(AppGlobal.g_oLang.get()._("card_no"));
		this.attachChild(m_oButtonSearchByCardNo);
		
		m_oButtonSearchByMemberNo = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSearchByMemberNo, "butSearchByMemberNo");
		m_oButtonSearchByMemberNo.addClickServerRequestSubmitElement(m_oTxtboxSearchValue);
		m_oButtonSearchByMemberNo.setValue(AppGlobal.g_oLang.get()._("member_no"));
		this.attachChild(m_oButtonSearchByMemberNo);

		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.setEnterBlockUI(true);
		m_oFrameNumberPad.clearEnterSubmitId();
		m_oFrameNumberPad.setEnterSubmitId(m_oTxtboxSearchValue);
		m_oFrameNumberPad.addListener(this);
		this.attachChild(m_oFrameNumberPad);
		
		if(m_bSearchByNumber)
			this.updateButtonColor(FrameLoyalty.SEARCH_TYPE.number.name());
		else
			this.updateButtonColor(FrameLoyalty.SEARCH_TYPE.cardNumber.name());
	}
	
	
	public void addMemberToResultList(int iSectionId, int iItemIndex, String sType, String sDesc, BigDecimal oAmount, Boolean bPercentage, Boolean bUsed){
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		int iRowHeight = 0;
		String sTypeName = "";
		
		if(sType.equals("I"))
			sTypeName = "Item";
		if(sType.equals("P") || sType.equals("C"))
			sTypeName = "Check";
		
		// Benefit Type
		iFieldWidths.add(150);
		sFieldValues.add(sTypeName);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		
		// Benefit Description
		iFieldWidths.add(470);
		sFieldValues.add(sDesc);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.LEFT+", "+HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		
		// Benefit Amount
		iFieldWidths.add(200);
		if(bPercentage)
			sFieldValues.add(oAmount.toString()+"%");
		else
			sFieldValues.add(oAmount.toString());
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER+", "+HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		
		
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			iRowHeight = 50;
		m_oBasketResultList.addItem(iSectionId, iItemIndex, iRowHeight, iFieldWidths, sFieldValues, sFieldAligns, null, null);
		m_oBasketResultList.setFieldPadding(iSectionId, iItemIndex, 1, "0,0,0,30");
		//updatePageUpDownVisibility();
	}
	
	public void showMemberDetail(FuncLoyalty oLoyalty){
		// Update the member detail page
		updateDetail(oLoyalty);
		
		// Show the member detail page and hide the result page
		switchTag(0, true);
		
	}
	
	public void updateDetail(FuncLoyalty oLoyalty) {
		if(m_oListMemberBasicInformation!= null)
			m_oListMemberBasicInformation.removeAllChildren();
		if(m_oListMemberBonusInformation != null)
			m_oListMemberBonusInformation.removeAllChildren();
		if(m_oBasketResultList != null)
			m_oBasketResultList.clearAllSections();
		
		for(Entry<String, BigDecimal> oEntry:oLoyalty.getBounsRecords().entrySet())
			addMemberInfo(oEntry.getKey(), oEntry.getValue().toString(), INFORMATION_TYPE_BOUNS);
		
		addMemberInfo(AppGlobal.g_oLang.get()._("member_no"), oLoyalty.getMemberNo(), INFORMATION_TYPE_BASIC);
		addMemberInfo(AppGlobal.g_oLang.get()._("member_name"), oLoyalty.getName(), INFORMATION_TYPE_BASIC);
		addMemberInfo(AppGlobal.g_oLang.get()._("campaign_code"), oLoyalty.getCampaignCode(), INFORMATION_TYPE_BASIC);
		addMemberInfo(AppGlobal.g_oLang.get()._("campaign_name"), oLoyalty.getCampaignName(), INFORMATION_TYPE_BASIC);
		addMemberInfo(AppGlobal.g_oLang.get()._("total_debit"), oLoyalty.getTotalBonusDebit().setScale(0, BigDecimal.ROUND_HALF_UP) + "/" + oLoyalty.getBalance(), INFORMATION_TYPE_BASIC);
	}
	
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
		else if (iType == INFORMATION_TYPE_BOUNS)
			m_oListMemberBonusInformation.attachChild(oFrameBasicDetail);
	}
	
	public void clearOverrideConditionRecords() {
		m_oBasketResultList.clearAllSections();
	}
	
	private void preSearchMember(String sType, String sValue){
		// Show the result page and hide the member detail page
		if(sValue.isEmpty())
			return;
		switchTag(0, true);
		
		// Update Button color
		this.updateButtonColor(sType);
		
		// Trim the value to 80 character long for "card number" type
		if(sType.equals(FrameLoyalty.SEARCH_TYPE.cardNumber.name()) && sValue.length() > 80)
			sValue = sValue.substring(0, 80);
		
		for (FrameLoyaltyListener listener : listeners) {
			// Raise the event to parent
			if(!listener.frameLoyalty_clickSearch(sType, sValue)){
				// Clear the result page
				m_oBasketResultList.clearAllSections();
			}
			break;
		}
	}

	public void switchTag(int iTagIndex, boolean bUpdateSelectedTabColor) {
		if (bUpdateSelectedTabColor)
			m_oFrameHorizontalTabList.changePageTab(iTagIndex);
		
		switch (iTagIndex) {
			case 0:	// Member Detail Tag
				m_oBasketResultList.setVisible(true);
				m_oBasketResultList.bringToTop();
				//m_oListMemberBasicInformation.setVisible(true);
				//m_oListMemberBonusInformation.setVisible(false);
				m_oLabelResultListHeader.setVisible(true);
				//m_oLabelInformationHeader.setValue(AppGlobal.g_oLang.get()._("general_information")+":");
				
				updatePageButton(m_iCurrentPageStartNo, m_iMaxPageNo);
				
				break;
			case 1: // Bonus Record Page
				m_oBasketResultList.setVisible(false);
				//m_oListMemberBasicInformation.setVisible(false);
				//m_oListMemberBonusInformation.setVisible(true);
				m_oLabelResultListHeader.setVisible(false);
				m_oLblPage.setVisible(false);
				m_oFramePage.setVisible(false);
				m_oImgButtonPrevPage.setVisible(false);
				m_oImgButtonNextPage.setVisible(false);
				//m_oLabelInformationHeader.setValue(AppGlobal.g_oLang.get()._("bonus_expiry_date")+":");
				
				break;
		}
	}
	
	// Update "Search", "Show All", Department and Category button color
	public void updateButtonColor(String sType) {
		if(m_oButtonSearchByMemberNo == null || m_oButtonSearchByCardNo == null)
			return;
		
		// Edit by King Cheung 2017-10-18 ---Start---
		String sUnselectedBackgroundColor = "#FFFFFF";
		String sUnselectedForegroundColor = "#333333";
		//String sUnselectedStrokeColor = "#868686";
		String sSelectedBackgroundColor = "#0055B8";
		String sSelectedForegroundColor = "#FFFFFF";
		//String sSelectedStrokeColor = "#005080";
		// Edit by King Cheung 2017-10-18 ---End---
		
		m_oButtonSearchByMemberNo.setBackgroundColor(sUnselectedBackgroundColor);
		m_oButtonSearchByMemberNo.setForegroundColor(sUnselectedForegroundColor);
		//m_oButtonSearchByMemberNo.setStrokeColor(sUnselectedStrokeColor);
		
		m_oButtonSearchByCardNo.setBackgroundColor(sUnselectedBackgroundColor);
		m_oButtonSearchByCardNo.setForegroundColor(sUnselectedForegroundColor);
		//m_oButtonSearchByCardNo.setStrokeColor(sUnselectedStrokeColor);
		
		VirtualUIButton oButton = m_oButtonSearchByMemberNo;
		if(sType.equals(FrameSearchMemberFunction.SEARCH_TYPE.number.name()))
			oButton = m_oButtonSearchByMemberNo;
		else if(sType.equalsIgnoreCase(FrameLoyalty.SEARCH_TYPE.cardNumber.name()))
			oButton = m_oButtonSearchByCardNo;
		
		oButton.setBackgroundColor(sSelectedBackgroundColor);
		oButton.setForegroundColor(sSelectedForegroundColor);
		//oButton.setStrokeColor(sSelectedStrokeColor);
	}
	
	public void updatePageButton(int iCurrentPage, int iTotalPage) {
		m_oLblPage.setValue(iCurrentPage + "/" + iTotalPage);
		m_oLblPage.setVisible(true);
		m_oFramePage.setVisible(true);
		// Add by King Cheung 2017-10-18 ---Start---
		m_oImgButtonPrevPage.setVisible(true);
		m_oImgButtonNextPage.setVisible(true);
		// Add by King Cheung 2017-10-18 ---End---
		m_iMaxPageNo = iTotalPage;
		m_iCurrentPageStartNo = iCurrentPage;
		
		// Edit by King Cheung 2017-10-18 ---Start---
		if (iTotalPage > 1) {
			m_oFramePage.setVisible(true);
			if (iCurrentPage > 1) {
				//m_oImgButtonPrevPage.setVisible(true);
				m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
				m_oImgButtonPrevPage.setEnabled(true);
			} else {
				//m_oImgButtonPrevPage.setVisible(false);
				m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
				m_oImgButtonPrevPage.setEnabled(false);
			}
			
			if (iCurrentPage < iTotalPage) {
				//m_oImgButtonNextPage.setVisible(true);
				m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
				m_oImgButtonNextPage.setEnabled(true);
			} else {
				//m_oImgButtonNextPage.setVisible(false);
				m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
				m_oImgButtonNextPage.setEnabled(false);
			}
			
		} else {
			//m_oFramePage.setVisible(false);
			//m_oImgButtonPrevPage.setVisible(false);
			//m_oImgButtonNextPage.setVisible(false);
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
			m_oImgButtonPrevPage.setEnabled(false);
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
			m_oImgButtonNextPage.setEnabled(false);
		}
		// Edit by King Cheung 2017-10-18 ---End---
	}
	
	// a function for displaying the benefits those are failed to redeem
	public void showFailedRedeemBenefit(List<HashMap<String, HashMap<String, String>>> oFailRedeemBenefitList){

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
	    m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
	    m_oFrameTitleHeader.init(true);
	    m_oFrameTitleHeader.addListener(this);
	    m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("error"));
	    m_oFrameTitleHeader.resetPostion();
	    this.attachChild(m_oFrameTitleHeader);
	    
		//Create Content Frame
		m_oFrameLeftContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftContent, "fraVerificationContent");
		this.attachChild(m_oFrameLeftContent);
		
		// Add Benefit List Title
		m_oLabelResultListHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelResultListHeader, "lblRedeptionFailed");
		m_oLabelResultListHeader.setValue(AppGlobal.g_oLang.get()._("reason") + " : " + AppGlobal.g_oLang.get()._("redemption_failed"));
		this.attachChild(m_oLabelResultListHeader);
		
		// Add Benefit List Title
		m_oLabelResultListHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelResultListHeader, "lblResultVerificationList");
		m_oLabelResultListHeader.setValue(AppGlobal.g_oLang.get()._("please_cancel_and_return_to_ordering_mode"));
		this.attachChild(m_oLabelResultListHeader);
		
		m_oBasketResultList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oBasketResultList, "fraResultVerificationList");
		m_oBasketResultList.init();
		this.attachChild(m_oBasketResultList);
		
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		iFieldWidths.add(150);
		sFieldValues.add(AppGlobal.g_oLang.get()._("type"));
		iFieldWidths.add(470);
		sFieldValues.add(AppGlobal.g_oLang.get()._("description"));
		iFieldWidths.add(200);
		sFieldValues.add(AppGlobal.g_oLang.get()._("amount"));
		m_oBasketResultList.addHeader(iFieldWidths, sFieldValues);
		m_oBasketResultList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		m_oBasketResultList.setHeaderTextAlign(1, HeroActionProtocol.View.Attribute.TextAlign.LEFT+", "+HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		m_oBasketResultList.setHeaderPadding(1, "0,0,0,30");
		
		//Create Page label
		m_oFramePage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePage, "fraVerificationPage");
		
		// Delete by King Cheung 2017-10-18 ---Start---
		/*
		VirtualUIImage oImagePage = new VirtualUIImage();
		oImagePage.setWidth(m_oFramePage.getWidth());
		oImagePage.setHeight(m_oFramePage.getHeight());
		oImagePage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_page_bg.png");
		m_oFramePage.attachChild(oImagePage);
		*/
		// Delete by King Cheung 2017-10-18 ---End---
		
		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oLblPage.setWidth(m_oFramePage.getWidth());
		m_oLblPage.setHeight(m_oFramePage.getHeight());
		m_oFramePage.attachChild(m_oLblPage);
		this.attachChild(m_oFramePage);
		// Delete by King Cheung 2017-10-18
		//m_oFramePage.setVisible(false);
		
		// Create prev page button
		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevVerificationPage");
		// Edit by King Cheung 2017-10-18
		m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
		m_oImgButtonPrevPage.allowClick(true);
		m_oImgButtonPrevPage.setClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.allowLongClick(true);
		m_oImgButtonPrevPage.setLongClickServerRequestBlockUI(false);
		// Delete by King Cheung 2017-10-18
		//m_oImgButtonPrevPage.setVisible(false);
		this.attachChild(m_oImgButtonPrevPage);
		
		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextVerificationPage");
		// Edit by King Cheung 2017-10-18
		m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.allowLongClick(true);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		// Delete by King Cheung 2017-10-18
		//m_oImgButtonNextPage.setVisible(false);
		this.attachChild(m_oImgButtonNextPage);
	}
	
	public boolean getAllowForKeyIn(){
		String sKeyInControls[] = null;
		boolean bResult = true;
		int iSetup = 0;
		sKeyInControls = FuncLoyalty.getKeyInControls();
		if(sKeyInControls != null)
		{
			if(sKeyInControls.length >= 1 && !sKeyInControls[0].isEmpty())
				iSetup = Integer.parseInt(sKeyInControls[0]);
		}
		if(iSetup == 1)
			bResult = false;
		
		return bResult;
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (m_oButtonSearchByMemberNo != null && iChildId == m_oButtonSearchByMemberNo.getId()) {
			if(!getAllowForKeyIn())
			{
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("support_swipe_card_only"));
				oFormDialogBox.show();
				oFormDialogBox = null;
				this.m_oTxtboxSearchValue.setValue("");
				return true;
			}
			
			m_bSearchByNumber = true;
			updateButtonColor(FrameSearchMemberFunction.SEARCH_TYPE.number.name());
			for (FrameLoyaltyListener listener : listeners){
				listener.frameLoyalty_clickSetMember(FrameLoyalty.SEARCH_TYPE.number.name(), m_oTxtboxSearchValue.getValue());
				break;
			}
			bMatchChild = true;
		} else if (m_oButtonSearchByCardNo != null && iChildId == m_oButtonSearchByCardNo.getId()) {
			if(!getAllowForKeyIn())
			{
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("support_swipe_card_only"));
				oFormDialogBox.show();
				oFormDialogBox = null;
				this.m_oTxtboxSearchValue.setValue("");
				return true;
			}
			
			m_bSearchByNumber = false;
			updateButtonColor(FrameSearchMemberFunction.SEARCH_TYPE.cardNumber.name());
			for (FrameLoyaltyListener listener : listeners){
				listener.frameLoyalty_clickSetMember(FrameLoyalty.SEARCH_TYPE.cardNumber.name(), m_oTxtboxSearchValue.getValue());
				break;
			}
			bMatchChild = true;
		} else if(iChildId == m_oImgButtonPrevPage.getId()) {
			 // PAGE UP
			for (FrameLoyaltyListener listener : listeners) 
				listener.frameLoyalty_prevPage();
			bMatchChild = true;
		}else if (iChildId == m_oImgButtonNextPage.getId()) {
			 // PAGE DOWN
			for (FrameLoyaltyListener listener : listeners) 
				listener.frameLoyalty_nextPage();
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
			this.m_oTxtboxSearchValue.setValue("");
			return;
		}
		
		for (FrameLoyaltyListener listener : listeners){
			if(m_bSearchByNumber)
				listener.frameLoyalty_clickSetMember(FrameLoyalty.SEARCH_TYPE.number.name(), m_oTxtboxSearchValue.getValue());
			else
				listener.frameLoyalty_clickSetMember(FrameLoyalty.SEARCH_TYPE.cardNumber.name(), m_oTxtboxSearchValue.getValue());
			break;
		}
	}

	@Override
	public void FrameNumberPad_clickCancel() {
		m_oTxtboxSearchValue.setValue("");
		for (FrameLoyaltyListener listener : listeners) {
			listener.frameLoyalty_clickCancel();
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
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex,
			int iItemIndex, int iFieldIndex, String sNote) {
		for (FrameLoyaltyListener listener : listeners) {
			listener.frameLoyalty_clickUseBenefit(iItemIndex);
			break;
		}
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
				List<PosInterfaceConfig> oLoyaltyInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
				// Get the configure from interface module
				oLoyaltyInterfaceConfigList = AppGlobal
						.getPosInterfaceConfigByInfType(InfInterface.TYPE_LOYALTY_INTERFACE);
				JSONObject oLoyaltyInterfaceConfig = null;
				String sMsrCode = "";
				String sSearchMode = FuncLoyalty.SWIPE_CARD_TYPE_LOYALTY_CARD;
				for (PosInterfaceConfig oPosInterfaceConfig : oLoyaltyInterfaceConfigList) {
					if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY)) {
						oLoyaltyInterfaceConfig = oPosInterfaceConfig.getInterfaceConfig();
						if (oLoyaltyInterfaceConfig.has("general_setup")
								&& oLoyaltyInterfaceConfig.optJSONObject("general_setup").has("params")
								&& oLoyaltyInterfaceConfig.optJSONObject("general_setup").optJSONObject("params")
										.has("msr_code")
								&& oLoyaltyInterfaceConfig.optJSONObject("general_setup").optJSONObject("params")
										.has("default_member_type")) {
							sSearchMode = oLoyaltyInterfaceConfig.optJSONObject("general_setup").optJSONObject("params")
									.optJSONObject("default_member_type").optString("value");
							sMsrCode = oLoyaltyInterfaceConfig.optJSONObject("general_setup").optJSONObject("params")
									.optJSONObject("msr_code").optString("value");
						}
					}
				}

				List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
				// Get the configure from interface module
				oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PERIPHERAL_DEVICE);
				String sCardNo = "";
				boolean bHaveError = false;
				if (!sMsrCode.isEmpty()) {
					for (PosInterfaceConfig oPosInterfaceConfig : oInterfaceConfigList) {
						if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_MSR)
								&& oPosInterfaceConfig.getInterfaceCode().equals(sMsrCode)) {
							// Capture information from swipe card
							FuncMSR oFuncMSR = new FuncMSR();
							int iErrorCode = oFuncMSR.processCardContent(sSwipeCardValue,
									oPosInterfaceConfig.getInterfaceConfig());

							// Get the necessary value
							sCardNo = oFuncMSR.getCardNo();
							if (iErrorCode == FuncMSR.ERROR_CODE_MISSING_SETUP) {
								FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
										this.getParentForm());
								oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
								oFormDialogBox.setMessage(oFuncMSR.getLastErrorMessage());
								oFormDialogBox.show();
								oFormDialogBox = null;

								bHaveError = true;

								break;
							}
						}
					}
				}

				m_oTxtboxSearchValue.setValue(sCardNo);
				if (!sMsrCode.isEmpty() && !m_oTxtboxSearchValue.getValue().isEmpty() && !bHaveError) {
					// do search member automatically
					if (sSearchMode.equals(FuncLoyalty.SWIPE_CARD_TYPE_LOYALTY_CARD)) {
						for (FrameLoyaltyListener listener : listeners) {
							listener.frameLoyalty_clickSetMember(FrameLoyalty.SEARCH_TYPE.number.name(),
									m_oTxtboxSearchValue.getValue());
							break;
						}
					} else {
						for (FrameLoyaltyListener listener : listeners) {
							listener.frameLoyalty_clickSetMember(FrameLoyalty.SEARCH_TYPE.cardNumber.name(),
									m_oTxtboxSearchValue.getValue());
							break;
						}
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
		return bMatchChild;
	}
	
	public void updateDebit(BigDecimal oBonusDebit, BigDecimal oBonusBalance){
		boolean iFound = false;
		for(VirtualUIBasicElement oBasicElement:m_oListMemberBasicInformation.getChilds()){
			for(VirtualUIBasicElement oVirtualUIBasicElement:oBasicElement.getChilds()){
				if(iFound)
					oVirtualUIBasicElement.setValue(oBonusDebit.setScale(0, BigDecimal.ROUND_HALF_UP)+"/"+oBonusBalance.toString());
				if(oVirtualUIBasicElement.getValue().equals(AppGlobal.g_oLang.get()._("total_debit")))
					iFound = true;
					
			}
		}
	}

	public boolean isSwipeCard() {
		return m_bSwipeCardSearch;
	}
	
	public FrameCommonBasket getBenefitList(){
		return m_oBasketResultList;
	}
	
	@Override
	public void FrameTitleHeader_close() {
		for (FrameLoyaltyListener listener : listeners) {
			// Raise the event to parent
			listener.frameLoyalty_clickCancel();
		}
	}
}
