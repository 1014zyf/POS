package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import om.InfInterface;
import om.InfVendor;
import om.MemMember;
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
interface FrameSearchMemberFunctionListener {
	void frameSearchMemberFunction_clickCancel();
	void frameSearchMemberFunction_clickSetMember();
	void frameSearchMemberFunction_clickClearMember();
	void frameSearchMemberFunction_clickSearch(String sType, String sValue);
	void frameSearchMemberFunction_clickSearchResultRecord(int iIndex);
	PosInterfaceConfig frameSearchMemberFunction_getPosInterfaceConfig(String sInterfaceType, 
			ArrayList<String> sVendors, String sTitle);
}

public class FrameSearchMemberFunction extends VirtualUIFrame implements FrameNumberPadListener, 
FrameCommonBasketListener, FrameHorizontalTabListListener, FrameMemberDetailListener, 
FrameCommonPageContainerListener, FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;

	private VirtualUIFrame m_oFrameLeftContent;
	private VirtualUIFrame m_oFrameRightContent;
	
	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUITextbox m_oTxtboxSearchValue;
	private VirtualUILabel m_oLabelSearchHeader;
	private VirtualUIButton m_oButtonSearchByMemberNo;
	private VirtualUIButton m_oButtonSearchByCardNo;
	private VirtualUIButton m_oButtonSearchByMemberName;
	private VirtualUIButton m_oButtonSearchByMemberPhone;
	private FrameNumberPad m_oFrameNumberPad;
	private FrameCommonPageContainer m_oCommonPageTabList;
	private VirtualUIFrame m_oCommonPageSelectedTabList;
	private VirtualUIFrame m_oCommonPageUnselectTabList;
	private VirtualUIFrame m_oSelectedButton;
	private VirtualUIFrame m_oUnselectedButton;
	private FrameCommonBasket m_oBasketResultList;
	private FrameMemberDetail m_oFrameMemberDetail;
	private FrameHorizontalTabList m_oFrameSearchTagList;
	private VirtualUITextbox m_oTxtboxGivenName;
	private VirtualUILabel m_oLabelGivenName;
	private VirtualUITextbox m_oTxtboxSurname;
	private VirtualUILabel m_oLabelSurname;
	private VirtualUITextbox m_oTxtChineseName;
	private VirtualUILabel m_oLabelChineseName;
	
	private VirtualUIFrame m_oFramePage;
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIImage m_oImgButtonNextPage;
	private int m_iCurrentPageStartNo;
	private int m_iScrollIndex;
	
	// Check the current search type
	private String m_sSearchType;
	
	private boolean m_bMultipleSearchType;
	private PosInterfaceConfig m_oPosInterfaceConfig;

	private boolean m_bSwipeCardSearch;
	private String m_sSwipeCardExpiryDate;
	
	public static enum SEARCH_TYPE{number, name, phone, cardNumber};
	
	public static enum SEARCH_MODE{offline, general, aspen}
	
	private final static int RESULT_COUNT = 8;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameSearchMemberFunctionListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameSearchMemberFunctionListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameSearchMemberFunctionListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	//Constructor
	public FrameSearchMemberFunction() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameSearchMemberFunctionListener>();
		m_bMultipleSearchType = false;
		
		m_iCurrentPageStartNo = 0;
		m_iScrollIndex = 1;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraSearchMemberFunction.xml");
	}
	
	public void init(String sTitle, boolean bShowClearMemberButton, boolean bMultipleSearchType, 
			PosInterfaceConfig oPosInterfaceConfig, String sEsignatureImg) {
		m_sSearchType = FrameSearchMemberFunction.SEARCH_TYPE.number.name();
		
		m_bMultipleSearchType = bMultipleSearchType;
		m_oPosInterfaceConfig = oPosInterfaceConfig;
		m_sSwipeCardExpiryDate = "";
		if(m_oPosInterfaceConfig != null && m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN)){
			m_oTemplateBuilder = new TemplateBuilder();
			m_oTemplateBuilder.loadTemplate("fraSearchMemberFunction_aspen.xml");
		}
		
		// Load child elements from template
		/////////////////////////////////////////////////////////////////
		// Title bar
		m_oFrameTitleHeader = new FrameTitleHeader();
        m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
        m_oFrameTitleHeader.init(true);
        m_oFrameTitleHeader.addListener(this);
        m_oFrameTitleHeader.setTitle(sTitle);
        this.attachChild(m_oFrameTitleHeader);
		
		m_oCommonPageSelectedTabList = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oCommonPageSelectedTabList, "fraSelectedListTab");
		this.attachChild(m_oCommonPageSelectedTabList);
		
		m_oCommonPageUnselectTabList = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oCommonPageUnselectTabList, "fraUnselectListTab");
		this.attachChild(m_oCommonPageUnselectTabList);
		
		//Create Left Content Frame
		m_oFrameLeftContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftContent, "fraLeftContent");
		this.attachChild(m_oFrameLeftContent);
		
		m_oCommonPageTabList = new FrameCommonPageContainer();
		m_oTemplateBuilder.buildFrame(m_oCommonPageTabList, "fraListTab");
		m_oCommonPageTabList.init(m_oCommonPageTabList.getWidth(), m_oCommonPageTabList.getHeight(), 
				m_oCommonPageSelectedTabList.getWidth(), m_oCommonPageSelectedTabList.getHeight(), 4, 
				m_oCommonPageSelectedTabList.getForegroundColor(), m_oCommonPageUnselectTabList.getForegroundColor(), 
				m_oCommonPageSelectedTabList.getBackgroundColor(), m_oCommonPageUnselectTabList.getBackgroundColor(), 
				48, false, true);
		m_oCommonPageTabList.setUnderlineColor("#0055B8");
		m_oCommonPageTabList.addListener(this);
		m_oFrameLeftContent.attachChild(m_oCommonPageTabList);
		
		VirtualUIFrame oFramePanelPageSeparator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFramePanelPageSeparator, "fraTagSeparator");
		
		m_oBasketResultList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oBasketResultList, "fraResultList");
		m_oBasketResultList.init();
		m_oBasketResultList.setTextSize(24);
		m_oBasketResultList.addListener(this);
		m_oFrameLeftContent.attachChild(m_oBasketResultList);
		
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();

		ArrayList<String> sFieldValues = new ArrayList<String>();
		iFieldWidths.add(250);
		sFieldValues.add(AppGlobal.g_oLang.get()._("member_no"));
		iFieldWidths.add(570);
		sFieldValues.add(AppGlobal.g_oLang.get()._("name"));
		m_oBasketResultList.addHeader(iFieldWidths, sFieldValues);
		m_oBasketResultList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		m_oFrameMemberDetail = new FrameMemberDetail();
		m_oTemplateBuilder.buildFrame(m_oFrameMemberDetail, "fraMemberDetail");	
		m_oFrameMemberDetail.init(bShowClearMemberButton, sEsignatureImg, m_oPosInterfaceConfig);
		m_oFrameMemberDetail.addListener(this);
		m_oFrameMemberDetail.setVisible(false);
		m_oFrameLeftContent.attachChild(m_oFrameMemberDetail);
		m_oFrameMemberDetail.changePageTab(0, false);
		
		//page up and down
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
		m_oFrameLeftContent.attachChild(m_oImgButtonPrevPage);
		
		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.allowLongClick(true);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		m_oFrameLeftContent.attachChild(m_oImgButtonNextPage);
		
		VirtualUIImage oImagePage = new VirtualUIImage();
		oImagePage.setWidth(m_oFramePage.getWidth());
		oImagePage.setHeight(m_oFramePage.getHeight());
		oImagePage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_page_bg.png");
		m_oFramePage.attachChild(oImagePage);
		
		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oLblPage.setWidth(m_oFramePage.getWidth());
		m_oLblPage.setHeight(m_oFramePage.getHeight());
		m_oFramePage.attachChild(m_oLblPage);
		m_oFrameLeftContent.attachChild(m_oFramePage);
		
		//Create Right Content Frame
		m_oFrameRightContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameRightContent, "fraRightContent");
		this.attachChild(m_oFrameRightContent);
		
		// Select Button frame
		m_oSelectedButton = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oSelectedButton, "fraSelected");
		this.attachChild(m_oSelectedButton);
		
		// Unselect Button frame
		m_oUnselectedButton = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oUnselectedButton, "fraUnselected");
		this.attachChild(m_oUnselectedButton);

		m_oTxtboxSearchValue = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxSearchValue, "txtInputBox");
		m_oTxtboxSearchValue.setFocusWhenShow(true);
		m_oFrameRightContent.attachChild(m_oTxtboxSearchValue);
		
		m_oLabelSearchHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSearchHeader, "lblSearchHeader");
		m_oLabelSearchHeader.setValue(AppGlobal.g_oLang.get()._("search_by")+":");
		m_oFrameRightContent.attachChild(m_oLabelSearchHeader);
		
		if (m_bMultipleSearchType) {
			m_oButtonSearchByCardNo = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonSearchByCardNo, "butSearchByCardNo");
			m_oButtonSearchByCardNo.addClickServerRequestSubmitElement(m_oTxtboxSearchValue);
			m_oButtonSearchByCardNo.setValue(AppGlobal.g_oLang.get()._("card_no"));
			m_oFrameRightContent.attachChild(m_oButtonSearchByCardNo);
			
			m_oButtonSearchByMemberNo = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonSearchByMemberNo, "butSearchByMemberNo");
			m_oButtonSearchByMemberNo.addClickServerRequestSubmitElement(m_oTxtboxSearchValue);
			m_oButtonSearchByMemberNo.setValue(AppGlobal.g_oLang.get()._("member_no"));
			m_oFrameRightContent.attachChild(m_oButtonSearchByMemberNo);
			
			m_oButtonSearchByMemberName = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonSearchByMemberName, "butSearchByMemberName");
			m_oButtonSearchByMemberName.addClickServerRequestSubmitElement(m_oTxtboxSearchValue);
			m_oButtonSearchByMemberName.setValue(AppGlobal.g_oLang.get()._("member_name"));
			m_oFrameRightContent.attachChild(m_oButtonSearchByMemberName);
			
			m_oButtonSearchByMemberPhone = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonSearchByMemberPhone, "butSearchByMemberPhone");
			m_oButtonSearchByMemberPhone.addClickServerRequestSubmitElement(m_oTxtboxSearchValue);
			m_oButtonSearchByMemberPhone.setValue(AppGlobal.g_oLang.get()._("member_phone"));
			m_oFrameRightContent.attachChild(m_oButtonSearchByMemberPhone);
		} else {
			// Search criteria only have member number, change top position of search text box and search header
			int iSearchHeaderLabelTop = m_oLabelSearchHeader.getTop();
			int iSearchTextBoxTop = m_oTxtboxSearchValue.getTop();
			
			m_oTxtboxSearchValue.setTop(iSearchHeaderLabelTop);
			m_oLabelSearchHeader.setTop(iSearchTextBoxTop);
			m_oLabelSearchHeader.setValue(AppGlobal.g_oLang.get()._("member_no")+":");
		}

		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.setEnterBlockUI(true);
		m_oFrameNumberPad.clearEnterSubmitId();
		m_oFrameNumberPad.setEnterSubmitId(m_oTxtboxSearchValue);
		m_oFrameNumberPad.addListener(this);
		m_oFrameRightContent.attachChild(m_oFrameNumberPad);
		this.updateButtonColor(FrameSearchMemberFunction.SEARCH_TYPE.number.name());
		
		if(m_oPosInterfaceConfig != null){
			if(m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN)){
				
				// Horizontal List
				m_oFrameSearchTagList = new FrameHorizontalTabList();
				m_oTemplateBuilder.buildFrame(m_oFrameSearchTagList, "fraSearchTagList");
				
				m_oFrameSearchTagList.init();
				
				m_oFrameSearchTagList.addListener(this);
				m_oFrameRightContent.attachChild(m_oFrameSearchTagList);
				List<String> oTabNameList = new ArrayList<String>();
				oTabNameList.add(AppGlobal.g_oLang.get()._("general_search"));
				oTabNameList.add(AppGlobal.g_oLang.get()._("name_search"));
				m_oFrameSearchTagList.addPageTabs(oTabNameList);
				m_oTxtboxGivenName = new VirtualUITextbox();
				m_oTemplateBuilder.buildTextbox(m_oTxtboxGivenName, "txtGivenName");
				m_oButtonSearchByMemberName.addClickServerRequestSubmitElement(m_oTxtboxGivenName);
				m_oFrameRightContent.attachChild(m_oTxtboxGivenName);
				
				m_oLabelGivenName = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(m_oLabelGivenName, "lblGivenName");
				m_oLabelGivenName.setValue(AppGlobal.g_oLang.get()._("given_name")+":");
				m_oFrameRightContent.attachChild(m_oLabelGivenName);
				
				m_oTxtboxSurname = new VirtualUITextbox();
				m_oTemplateBuilder.buildTextbox(m_oTxtboxSurname, "txtSurname");
				m_oTxtboxSurname.setFocusWhenShow(true);
				m_oButtonSearchByMemberName.addClickServerRequestSubmitElement(m_oTxtboxSurname);
				m_oFrameRightContent.attachChild(m_oTxtboxSurname);
				
				m_oLabelSurname = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(m_oLabelSurname, "lblSurname");
				m_oLabelSurname.setValue(AppGlobal.g_oLang.get()._("surname")+":");
				m_oFrameRightContent.attachChild(m_oLabelSurname);
				
				m_oTxtChineseName = new VirtualUITextbox();
				m_oTemplateBuilder.buildTextbox(m_oTxtChineseName, "txtChineseName");
				m_oButtonSearchByMemberName.addClickServerRequestSubmitElement(m_oTxtChineseName);
				m_oFrameRightContent.attachChild(m_oTxtChineseName);
				
				m_oLabelChineseName = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(m_oLabelChineseName, "lblChineseName");
				m_oLabelChineseName.setValue(AppGlobal.g_oLang.get()._("chinese_name")+":");
				m_oFrameRightContent.attachChild(m_oLabelChineseName);
				switchSearchTag(0);
			}
		}
		// Add tag to Common page container
		m_oCommonPageTabList.addButton(AppGlobal.g_oLang.get()._("search_result"), m_oBasketResultList);
		m_oCommonPageTabList.addButton(AppGlobal.g_oLang.get()._("member_detail"), m_oFrameMemberDetail);
	}
	
	public void addMemberToResultList(int iSectionId, int iItemIndex, String sMemberNo, String sMemberName){
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		int iRowHeight = 0;

		// Member no.
		iFieldWidths.add(250);
		sFieldValues.add(sMemberNo);
		sFieldAligns.add("");
		
		// Member name
		iFieldWidths.add(570);
		sFieldValues.add(sMemberName);
		sFieldAligns.add("");
		
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			iRowHeight = 50;
		m_oBasketResultList.addItem(iSectionId, iItemIndex, iRowHeight, iFieldWidths, sFieldValues, 
				sFieldAligns, null, null);
		updatePageUpDownVisibility();
	}
	
	public void showMemberDetail(MemMember oMember){
		// Update the member detail page
		m_oFrameMemberDetail.updateDetail(oMember);
		
		// Show the member detail page and hide the result page
		switchTag(1);
		
		// Hide the right content if mobile station
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oFrameRightContent.setVisible(false);
	}
	
	private void preSearchMember(String sType, String sValue){
		// Clear the result page
		m_oBasketResultList.clearAllSections();
		m_sSearchType = sType;
		// Show the result page and hide the member detail page
		switchTag(0);
		
		// Update Button color
		this.updateButtonColor(sType);
		
		// Trim the value to 80 character long for "card number" type
		if(sType.equals(FrameSearchMemberFunction.SEARCH_TYPE.cardNumber.name()) && sValue.length() > 80)
			sValue = sValue.substring(0, 80);
		
		if(m_oPosInterfaceConfig != null 
				&& m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN)
				&& sType.equals(FrameSearchMemberFunction.SEARCH_TYPE.name.name())
				&& m_oTxtboxSurname.getVisible()){
				sValue =  String.format("%s,%s,%s",m_oTxtboxSurname.getValue(), m_oTxtboxGivenName.getValue(), m_oTxtChineseName.getValue());
		}
			
		if(sType.equals(FrameSearchMemberFunction.SEARCH_TYPE.phone.name())) {
			if (sValue.isEmpty() || !sValue.matches("^[0-9]+$")) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
						this.getParentForm());
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("invalid_mobile"));
				oFormDialogBox.show();
				return ;
			}
		}
		
		for (FrameSearchMemberFunctionListener listener : listeners) {
			// Raise the event to parent
			listener.frameSearchMemberFunction_clickSearch(sType, sValue);
			break;
		}
		
	}
	
	public void switchTag(int iTagIndex) {
		m_oCommonPageTabList.clickTag(iTagIndex);
	}
	
	public void switchSearchTag(int iTagIndex) {
		m_oFrameSearchTagList.changePageTab(iTagIndex);
		
		switch (iTagIndex) {
			case 0:	// General Search Tag
				m_oTxtboxSearchValue.setFocus();
				m_oFrameNumberPad.setVisible(true);
				m_oFramePage.setVisible(false);
				m_oTxtboxSearchValue.setVisible(true);
				m_oLabelSearchHeader.setVisible(true);
				m_oButtonSearchByCardNo.setVisible(true);
				m_oButtonSearchByMemberNo.setVisible(true);
				if(m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN))
					m_oButtonSearchByMemberPhone.setVisible(true);	
				else
					m_oButtonSearchByMemberPhone.setVisible(false);
				m_oButtonSearchByMemberName.setVisible(false);
				m_oLabelGivenName.setVisible(false);
				m_oTxtboxGivenName.setVisible(false);
				m_oTxtboxGivenName.setValue("");
				m_oLabelSurname.setVisible(false);
				m_oTxtboxSurname.setVisible(false);
				m_oTxtboxSurname.setValue("");
				m_oLabelChineseName.setVisible(false);
				m_oTxtChineseName.setVisible(false);
				m_oTxtChineseName.setValue("");
				break;
			case 1:	// Member Name Tag
				m_oTxtboxSurname.setFocus();
				m_oFrameNumberPad.setVisible(false);
				m_oFramePage.setVisible(false);
				
				m_oTxtboxSearchValue.setVisible(false);
				m_oTxtboxSearchValue.setValue("");
				m_oLabelSearchHeader.setVisible(false);
				m_oButtonSearchByCardNo.setVisible(false);
				m_oButtonSearchByMemberNo.setVisible(false);
				m_oButtonSearchByMemberPhone.setVisible(false);
				m_oButtonSearchByMemberName.setVisible(true);
				m_oLabelGivenName.setVisible(true);
				m_oTxtboxGivenName.setVisible(true);
				m_oLabelSurname.setVisible(true);
				m_oTxtboxSurname.setVisible(true);
				m_oLabelChineseName.setVisible(true);
				m_oTxtChineseName.setVisible(true);
				break;
		}
	}

	//change Search Panel
	public void switchSearchingMode(String sMode){
		//in this phase, once offline mode turns on, never turns back to online mode 
		if(sMode.equals(FrameSearchMemberFunction.SEARCH_MODE.offline.name())){
			
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.horizontal_desktop.name())) {
				m_oTxtboxSearchValue.setTop(10);
				m_oLabelSearchHeader.setTop(70);
				m_oButtonSearchByMemberNo.setTop(110);
				m_oButtonSearchByMemberPhone.setTop(110);
				m_oButtonSearchByCardNo.setTop(110);
				m_oButtonSearchByMemberName.setTop(165);
			} else {
				m_oTxtboxSearchValue.setTop(15);
				m_oLabelSearchHeader.setTop(55);
				m_oButtonSearchByMemberNo.setTop(95);
				m_oButtonSearchByMemberPhone.setTop(95);
				m_oButtonSearchByCardNo.setTop(95);
				m_oButtonSearchByMemberName.setTop(140);
				m_oButtonSearchByMemberName.setLeft(m_oButtonSearchByMemberNo.getLeft());
			}
			m_oButtonSearchByMemberName.setLeft(m_oButtonSearchByMemberNo.getLeft());
			m_oFrameNumberPad.setVisible(true);
			m_oButtonSearchByMemberName.setWidth(m_oButtonSearchByMemberNo.getWidth());
			m_oButtonSearchByMemberName.setHeight(m_oButtonSearchByMemberNo.getHeight());
			m_oTxtboxSearchValue.setVisible(true);
			m_oLabelSearchHeader.setVisible(true);
			m_oButtonSearchByMemberNo.setVisible(true);
			m_oButtonSearchByCardNo.setVisible(true);
			m_oButtonSearchByMemberPhone.setVisible(true);
			m_oButtonSearchByMemberName.setVisible(true);
			
			if(m_oFrameSearchTagList != null){
				m_oFrameSearchTagList.setVisible(false);
				m_oLabelGivenName.setVisible(false);
				m_oTxtboxGivenName.setVisible(false);
				m_oLabelSurname.setVisible(false);
				m_oTxtboxSurname.setVisible(false);
				m_oLabelChineseName.setVisible(false);
				m_oTxtChineseName.setVisible(false);
			}
		}
	}
	
	// Update "Search", "Show All", Department and Category button color
	public void updateButtonColor(String sType) {
		if (!m_bMultipleSearchType)
			return;
		String sUnselectedBackgroundColor = m_oUnselectedButton.getBackgroundColor();
		String sUnselectedForegroundColor = m_oUnselectedButton.getForegroundColor();
		String sUnselectedStrokeColor = m_oUnselectedButton.getStrokeColor();
		String sSelectedBackgroundColor = m_oSelectedButton.getBackgroundColor();
		String sSelectedForegroundColor = m_oSelectedButton.getForegroundColor();
		String sSelectedStrokeColor = m_oSelectedButton.getStrokeColor();
		m_oButtonSearchByMemberNo.setBackgroundColor(sUnselectedBackgroundColor);
		m_oButtonSearchByMemberNo.setForegroundColor(sUnselectedForegroundColor);
		m_oButtonSearchByMemberNo.setStrokeColor(sUnselectedStrokeColor);
		
		m_oButtonSearchByCardNo.setBackgroundColor(sUnselectedBackgroundColor);
		m_oButtonSearchByCardNo.setForegroundColor(sUnselectedForegroundColor);
		m_oButtonSearchByCardNo.setStrokeColor(sUnselectedStrokeColor);
		
		m_oButtonSearchByMemberName.setBackgroundColor(sUnselectedBackgroundColor);
		m_oButtonSearchByMemberName.setForegroundColor(sUnselectedForegroundColor);
		m_oButtonSearchByMemberName.setStrokeColor(sUnselectedStrokeColor);
		
		m_oButtonSearchByMemberPhone.setBackgroundColor(sUnselectedBackgroundColor);
		m_oButtonSearchByMemberPhone.setForegroundColor(sUnselectedForegroundColor);
		m_oButtonSearchByMemberPhone.setStrokeColor(sUnselectedStrokeColor);
		
		
		VirtualUIButton oButton = m_oButtonSearchByMemberNo;
		if(sType.equals(FrameSearchMemberFunction.SEARCH_TYPE.name.name()))
			oButton = m_oButtonSearchByMemberName;
		else if (sType.equalsIgnoreCase(FrameSearchMemberFunction.SEARCH_TYPE.phone.name()))
			oButton = m_oButtonSearchByMemberPhone;
		else if(sType.equalsIgnoreCase(FrameSearchMemberFunction.SEARCH_TYPE.cardNumber.name()))
			oButton = m_oButtonSearchByCardNo;
		
		oButton.setBackgroundColor(sSelectedBackgroundColor);
		oButton.setForegroundColor(sSelectedForegroundColor);
		oButton.setStrokeColor(sSelectedStrokeColor);
	}
	
	public void showOnlineMemberDetail(HashMap<String, String> oMemberInfo) {
		// Update the member detail page
		m_oFrameMemberDetail.updateOnlineDetail(oMemberInfo);
		
		//Show the member detail page and hide the result page
		switchTag(1);
		
		//Hide the right content if mobile station
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oFrameRightContent.setVisible(false);
		
	}
	
	public FrameCommonBasket getBasketResultList(){
		return m_oBasketResultList;
	}
	
	public void updatePageUpDownVisibility() {
		boolean bShowPageUp = false;
		boolean bShowPageDown = false;
		int iPage = 0;
		int iCurrentPanelRecordCount = 0;
		
		iCurrentPanelRecordCount = m_oBasketResultList.getItemCount(0);
		
		if(iCurrentPanelRecordCount > RESULT_COUNT)
			iPage = (m_iCurrentPageStartNo/RESULT_COUNT)+1;
		
		if(m_iCurrentPageStartNo > 0)
			bShowPageUp = true;
		
		if(iCurrentPanelRecordCount > RESULT_COUNT && m_iCurrentPageStartNo+RESULT_COUNT < iCurrentPanelRecordCount)
			bShowPageDown = true;
		
		setPageNumber(iPage);
		showPageUp(bShowPageUp);
		showPageDown(bShowPageDown);
	}
	
	public void showPageUp(boolean bShow) {
		if(bShow)
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + 
					"/buttons/set_menu_prev_page_button.png");
		else
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + 
					"/buttons/set_menu_prev_page_button_unclick.png");
		m_oImgButtonPrevPage.setEnabled(bShow);
	}
	
	public void showPageDown(boolean bShow) {
		if(bShow)
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + 
					"/buttons/set_menu_next_page_button.png");
		else
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + 
					"/buttons/set_menu_next_page_button_unclick.png");
		m_oImgButtonNextPage.setEnabled(bShow);
	}
	
	public void setPageNumber(int iNumber) {
		int iTotalPage = 0;
		if(iNumber > 0) {
			iTotalPage = (int)Math.ceil(1.0*m_oBasketResultList.getItemCount(0)/RESULT_COUNT);
			m_oFramePage.setVisible(true);
			m_oLblPage.setValue(iNumber+"/"+iTotalPage);
			m_oLblPage.setVisible(true);
			m_oImgButtonPrevPage.setVisible(true);
			m_oImgButtonNextPage.setVisible(true);
		} else {
			m_oFramePage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
			m_oImgButtonNextPage.setVisible(false);
		}
	}
	
	public String getSwipeCardExpiryDateString() {
		return this.m_sSwipeCardExpiryDate;
	}
	
	public void showClearMemberButton(){
		m_oFrameMemberDetail.showClearMemberButton();
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (m_oButtonSearchByMemberNo != null && iChildId == m_oButtonSearchByMemberNo.getId()) {
			this.preSearchMember(FrameSearchMemberFunction.SEARCH_TYPE.number.name(), m_oTxtboxSearchValue.getValue());
			bMatchChild = true;
		} else if (m_oButtonSearchByMemberName != null && iChildId == m_oButtonSearchByMemberName.getId()) {
			this.preSearchMember(FrameSearchMemberFunction.SEARCH_TYPE.name.name(), m_oTxtboxSearchValue.getValue());
			bMatchChild = true;
		} 
		else if ((m_oButtonSearchByMemberPhone != null && iChildId == m_oButtonSearchByMemberPhone.getId())) {
			this.preSearchMember(FrameSearchMemberFunction.SEARCH_TYPE.phone.name(), m_oTxtboxSearchValue.getValue());
			bMatchChild = true;
		} else if (m_oButtonSearchByCardNo != null && iChildId == m_oButtonSearchByCardNo.getId()) {
			this.preSearchMember(FrameSearchMemberFunction.SEARCH_TYPE.cardNumber.name(), m_oTxtboxSearchValue.getValue());
			bMatchChild = true;
		}if(iChildId == m_oImgButtonPrevPage.getId()) {
			 // PAGE UP
			if(m_iCurrentPageStartNo-RESULT_COUNT >= 0) {
				m_iCurrentPageStartNo -= RESULT_COUNT;
				updatePageUpDownVisibility();
				m_iScrollIndex -= RESULT_COUNT;
				m_oBasketResultList.moveScrollToItem(0, m_iScrollIndex);
			}
			bMatchChild = true;
		}else if (iChildId == m_oImgButtonNextPage.getId()) {
			 // PAGE DOWN
			if(m_iCurrentPageStartNo + RESULT_COUNT < m_oBasketResultList.getItemCount(0)) {
				m_iCurrentPageStartNo += RESULT_COUNT;
				updatePageUpDownVisibility();
			}
			m_iScrollIndex += RESULT_COUNT;
			m_oBasketResultList.moveScrollToItem(0, m_iScrollIndex);
			bMatchChild = true;
		}		
		return bMatchChild;
	}

	@Override
	public void FrameNumberPad_clickEnter() {
		this.preSearchMember(m_sSearchType, m_oTxtboxSearchValue.getValue());
	}

	@Override
	public void FrameNumberPad_clickCancel() {
		m_oTxtboxSearchValue.setValue("");
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
		for (FrameSearchMemberFunctionListener listener : listeners) {
			listener.frameSearchMemberFunction_clickSearchResultRecord(iItemIndex);
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
		if(m_oFrameSearchTagList.getId() == iId)
			switchSearchTag(iTabIndex);
	}
	
	@Override
	public void frameMemberDetail_clickSetMember() {
		for (FrameSearchMemberFunctionListener listener : listeners) {
			listener.frameSearchMemberFunction_clickSetMember();
			break;
		}
	}

	@Override
	public void frameMemberDetail_clickClearMember() {
		for (FrameSearchMemberFunctionListener listener : listeners) {
			listener.frameSearchMemberFunction_clickClearMember();
			break;
		}
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
				// get member validation setting
				String sMemberValidateSetting = AppGlobal.g_oFuncStation.get().getMemberValidationSetting();
				String sMsrInterfaceCode = "";
				if (!sMemberValidateSetting.isEmpty()) {
					try {
						JSONObject oJsonObject = new JSONObject(sMemberValidateSetting);
						sMsrInterfaceCode = oJsonObject.optString("interface_code", "");
					} catch (JSONException e) {
						e.printStackTrace();
						AppGlobal.stack2Log(e);
					}
				}

				// special case for aspen, parser the card no.
				String sAspenSwapCardValue = "";
				if (m_oPosInterfaceConfig != null
						&& m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN)) {
					ArrayList<String> oVendors = new ArrayList<String>();
					oVendors.add(InfVendor.KEY_MSR);
					String sTitle = AppGlobal.g_oLang.get()._("please_select_the_msr_interface");
					PosInterfaceConfig oPosInterfaceConfig = null;
					for (FrameSearchMemberFunctionListener listener : listeners) {
						oPosInterfaceConfig = listener.frameSearchMemberFunction_getPosInterfaceConfig(
								InfInterface.TYPE_PERIPHERAL_DEVICE, oVendors, sTitle);
						break;
					}
					if (oPosInterfaceConfig != null) {
						// Capture information from swipe card
						FuncMSR oFuncMSR = new FuncMSR();
						if (oFuncMSR.processCardContent(sSwipeCardValue,
								oPosInterfaceConfig.getInterfaceConfig()) == FuncMSR.ERROR_CODE_NO_ERROR) {
							// Get the necessary value
							sAspenSwapCardValue = oFuncMSR.getCardNo();
							m_bSwipeCardSearch = true;
						} else {
							// Fail to process swipe card
							FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
									this.getParentForm());
							oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
							oFormDialogBox.setMessage(oFuncMSR.getLastErrorMessage());
							oFormDialogBox.show();
							oFormDialogBox = null;
							return false;
						}
					} else
						sAspenSwapCardValue = "";
				} else if (!sMsrInterfaceCode.isEmpty()) {
					m_sSwipeCardExpiryDate = "";
					InfInterface oMsrInterface = new InfInterface();
					oMsrInterface.getInterfaceByCode(sMsrInterfaceCode);
					if (oMsrInterface.getInterfaceId() > 0) {
						// Capture information from swipe card
						FuncMSR oFuncMSR = new FuncMSR();
						if (oFuncMSR.processCardContent(sSwipeCardValue,
								oMsrInterface.getSetting()) == FuncMSR.ERROR_CODE_NO_ERROR) {
							// Get the necessary value
							sAspenSwapCardValue = oFuncMSR.getCardNo();
							m_sSwipeCardExpiryDate = oFuncMSR.getExpiryDateString();
							m_bSwipeCardSearch = true;
						} else {
							// Fail to process swipe card
							FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
									this.getParentForm());
							oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
							oFormDialogBox.setMessage(oFuncMSR.getLastErrorMessage());
							oFormDialogBox.show();
							oFormDialogBox = null;
							return false;
						}
					}
				}

				if (!sAspenSwapCardValue.isEmpty())
					sSwipeCardValue = sAspenSwapCardValue;

				m_oTxtboxSearchValue.setValue(sSwipeCardValue);

				// do search member automatically
				if (!m_oTxtboxSearchValue.getValue().isEmpty())
					this.preSearchMember(FrameSearchMemberFunction.SEARCH_TYPE.cardNumber.name(),
							m_oTxtboxSearchValue.getValue());
			}
		}
		return bMatchChild;
	}

	public boolean isSwipeCard() {
		return m_bSwipeCardSearch;
	}
	
	@Override
	public void frameCommonPageContainer_changeFrame() {
		// TODO Auto-generated method stub
		if(m_oCommonPageTabList.getCurrentIndex() > 0 && AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oFrameRightContent.setVisible(false);
		else
			m_oFrameRightContent.setVisible(true);
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
	public void FrameTitleHeader_close() {
		for (FrameSearchMemberFunctionListener listener : listeners) {
			// Raise the event to parent
			listener.frameSearchMemberFunction_clickCancel();
		}
	}
	
	@Override
	public void frameCommonPageContainer_CloseImageClicked() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void frameCommonPageContainer_ClickButtonIndex(int iIndex) {
		// TODO Auto-generated method stub
	}
}
