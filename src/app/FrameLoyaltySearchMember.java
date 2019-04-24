package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import om.InfInterface;
import om.InfVendor;
import om.MemMember;
import om.MemMemberList;
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
interface FrameLoyaltySearchMemberListener {
	void frameSearchMemberFunction_clickCancel();

	void frameSearchMemberFunction_clickSearchResultRecord(int iIndex);

}

// Edit by King Cheung 2017-11-27 ---Start---

public class FrameLoyaltySearchMember extends VirtualUIFrame implements FrameNumberPadListener, FrameCommonBasketListener,
		FrameLoyaltyMemberDetailListener, FrameTitleHeaderListener, FrameCommonPageContainerListener {
	private TemplateBuilder m_oTemplateBuilder;

	private ArrayList<VirtualUITextbox> oSearchTextBoxList = new ArrayList<VirtualUITextbox>();
	private List<String> oSearchList = new ArrayList<String>();
	private int m_iMinimumLength = 0;
	private MemMemberList m_oMemberList = new MemMemberList();

	private VirtualUIFrame m_oFrameLeftContent;
	private VirtualUIFrame m_oFrameRightContent;
	
	private FrameTitleHeader m_oFrameTitleHeader;
	private FrameCommonPageContainer m_oCommonPageTabList;
	private VirtualUILabel m_oLabelSelectedTag;
	private VirtualUILabel m_oLabelUnSelectedTag;
	private VirtualUILabel m_oLabelSearchHeader;
	private FrameNumberPad m_oFrameNumberPad;
	//FrameHorizontalTabList m_oFrameHorizontalTabList;
	private FrameCommonBasket m_oBasketResultList;
	private FrameLoyaltyMemberDetail m_oFrameLoyaltyMemberDetail;
	private VirtualUILabel m_oLabelSearchTag;
	private VirtualUITextbox m_oTxtboxMemberNo;
	private VirtualUILabel m_oLabelMemberNo;
	private VirtualUILabel m_oLabelSearchingField;

	private VirtualUIFrame m_oFramePage;
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIImage m_oImgButtonNextPage;
	private int m_iCurrentPageStartNo;
	private int m_iScrollIndex;

	private final static int RESULT_COUNT = 13;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameLoyaltySearchMemberListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameLoyaltySearchMemberListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameLoyaltySearchMemberListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	// Constructor
	public FrameLoyaltySearchMember() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameLoyaltySearchMemberListener>();

		m_iCurrentPageStartNo = 0;
		m_iScrollIndex = 1;

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraLoyaltySearchMember.xml");
	}

	public void init(String sTitle, JSONObject oSearchSetupJSONObject) {
		// Load child elements from template
		/////////////////////////////////////////////////////////////////

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
	    m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
	    m_oFrameTitleHeader.init(true);
	    m_oFrameTitleHeader.setTitle(sTitle);
	    m_oFrameTitleHeader.addListener(this);
	    this.attachChild(m_oFrameTitleHeader);

		// Create Left Content Frame
		m_oFrameLeftContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftContent, "fraLeftContent");
		this.attachChild(m_oFrameLeftContent);
		
		// Selected Tag
		m_oLabelSelectedTag = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSelectedTag, "lblSelectedTag");
		this.attachChild(m_oLabelSelectedTag);
		
		// UnSelected Tag
		m_oLabelUnSelectedTag = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelUnSelectedTag, "lblUnSelectedTag");
		this.attachChild(m_oLabelUnSelectedTag);

		m_oCommonPageTabList = new FrameCommonPageContainer();
		m_oTemplateBuilder.buildFrame(m_oCommonPageTabList, "fraContent");
//JohnLiu 02112017 -- start
		m_oCommonPageTabList.init(m_oCommonPageTabList.getWidth(), m_oCommonPageTabList.getHeight(), 180, 58, 2, m_oLabelSelectedTag.getForegroundColor(), m_oLabelUnSelectedTag.getForegroundColor(), m_oLabelSelectedTag.getBackgroundColor(), m_oLabelUnSelectedTag.getBackgroundColor(), 30, false, true);
		m_oCommonPageTabList.setTagTextSize(m_oLabelSelectedTag.getTextSize(), m_oLabelUnSelectedTag.getTextSize());
//JohnLiu 02112017 -- end
		m_oCommonPageTabList.addListener(this);
		m_oFrameLeftContent.attachChild(m_oCommonPageTabList);
		
		/*
		// Horizontal List
		m_oFrameHorizontalTabList = new FrameHorizontalTabList();
		m_oTemplateBuilder.buildFrame(m_oFrameHorizontalTabList, "fraHorizontalList");
		m_oFrameHorizontalTabList.init();
		m_oFrameHorizontalTabList.addListener(this);
		//m_oFrameLeftContent.attachChild(m_oFrameHorizontalTabList);
		
		List<String> oTabNameList = new ArrayList<String>();
		oTabNameList.add(AppGlobal.g_oLang.get()._("search_result"));
		oTabNameList.add(AppGlobal.g_oLang.get()._("member_detail"));
		m_oFrameHorizontalTabList.addPageTabs(oTabNameList);

		VirtualUIFrame oFramePanelPageSeparator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFramePanelPageSeparator, "fraTagSeparator");
		//m_oFrameLeftContent.attachChild(oFramePanelPageSeparator);
		 */

		m_oBasketResultList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oBasketResultList, "fraResultList");
		m_oBasketResultList.init();
		m_oBasketResultList.addListener(this);
		//m_oFrameLeftContent.attachChild(m_oBasketResultList);

		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();

		iFieldWidths.add(160);
		sFieldValues.add(AppGlobal.g_oLang.get()._("member_number"));
		iFieldWidths.add(120);
		sFieldValues.add(AppGlobal.g_oLang.get()._("member_name"));
		iFieldWidths.add(120);
		sFieldValues.add(AppGlobal.g_oLang.get()._("member_type"));
		iFieldWidths.add(204);
		sFieldValues.add(AppGlobal.g_oLang.get()._("member_bonus_balance"));

		m_oBasketResultList.addHeader(iFieldWidths, sFieldValues);
// Add by King Cheung 2017-11-27 ---Start---
		m_oBasketResultList.setHeaderFormat(40, 18, "");
		m_oBasketResultList.setHeaderUnderlineColor("#999999");
// Add by King Cheung 2017-11-27 ---End---
		m_oBasketResultList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);

		m_oFrameLoyaltyMemberDetail = new FrameLoyaltyMemberDetail();
		m_oTemplateBuilder.buildFrame(m_oFrameLoyaltyMemberDetail, "fraMemberDetail");
		m_oFrameLoyaltyMemberDetail.init(true);
		m_oFrameLoyaltyMemberDetail.addListener(this);
		m_oFrameLoyaltyMemberDetail.setVisible(false);
		//m_oFrameLeftContent.attachChild(m_oFrameLoyaltyMemberDetail);

		m_oFrameLoyaltyMemberDetail.changePageTab(0, false);

		// page up and down
		// Create Page label
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

		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oLblPage.setWidth(m_oFramePage.getWidth());
		m_oLblPage.setHeight(m_oFramePage.getHeight());
		m_oFramePage.attachChild(m_oLblPage);
		m_oFrameLeftContent.attachChild(m_oFramePage);

		// Add tag to Common page container
		m_oCommonPageTabList.addButton(AppGlobal.g_oLang.get()._("search_result"), m_oBasketResultList);
		m_oCommonPageTabList.addButton(AppGlobal.g_oLang.get()._("member_detail"), m_oFrameLoyaltyMemberDetail);
		
		// Create Right Content Frame
		m_oFrameRightContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameRightContent, "fraRightContent");
		this.attachChild(m_oFrameRightContent);

		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.setEnterBlockUI(true);
		m_oFrameNumberPad.clearEnterSubmitId();
		m_oFrameNumberPad.addListener(this);
		m_oFrameRightContent.attachChild(m_oFrameNumberPad);

		switchTag(0, false);
		if (!oSearchSetupJSONObject.optJSONObject("params").optJSONObject("search_field").optString("value")
				.equals("")) {
			m_iMinimumLength = oSearchSetupJSONObject.optJSONObject("params").optJSONObject("input_minimum_length")
					.optInt("value");
			String sSearchSetup = oSearchSetupJSONObject.optJSONObject("params").optJSONObject("search_field")
					.optString("value");
			String[] sSearchSplitLinesArray = sSearchSetup.split("\\r?\\n");

			for (int i = 0; i < sSearchSplitLinesArray.length; i++)
				oSearchList.addAll(Arrays.asList(sSearchSplitLinesArray[i].split(",")));

			// Read Searching Field From Setting
			int iHeight = 0;
			for (int i = AppGlobal.g_oCurrentLangIndex.get(); i < oSearchList.size(); i += 6) {
				VirtualUITextbox oTxtboxSearchingField = new VirtualUITextbox();
				m_oTemplateBuilder.buildTextbox(oTxtboxSearchingField, "txtSearchingField");
				oTxtboxSearchingField.setFocusWhenShow(true);
				oTxtboxSearchingField.setTop(oTxtboxSearchingField.getTop() + iHeight);
				oTxtboxSearchingField.setVisible(true);
				m_oFrameRightContent.attachChild(oTxtboxSearchingField);

				m_oFrameNumberPad.setEnterSubmitId(oTxtboxSearchingField);
				oSearchTextBoxList.add(oTxtboxSearchingField);

				m_oLabelSearchingField = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(m_oLabelSearchingField, "lblSearchingField");
				m_oLabelSearchingField.setValue(AppGlobal.g_oLang.get()._(oSearchList.get(i)) + ":");
				m_oLabelSearchingField.setTop(m_oLabelSearchingField.getTop() + iHeight);
				m_oFrameRightContent.attachChild(m_oLabelSearchingField);

				iHeight += 60;
			}

			if (m_oLabelMemberNo != null)
				m_oLabelMemberNo.setVisible(true);
		}

		// Default - Searching field - member no
		m_oTxtboxMemberNo = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxMemberNo, "txtMemberNo");
		m_oTxtboxMemberNo.setFocusWhenShow(true);
		m_oFrameRightContent.attachChild(m_oTxtboxMemberNo);
		m_oFrameNumberPad.setEnterSubmitId(m_oTxtboxMemberNo);

		m_oLabelMemberNo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMemberNo, "lblMemberNo");
		m_oLabelMemberNo.setValue(AppGlobal.g_oLang.get()._("member_number") + ":");
		m_oFrameRightContent.attachChild(m_oLabelMemberNo);

		// Search Tag Label
		m_oLabelSearchTag = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSearchTag, "lblSearchHeader");
		m_oLabelSearchTag.setValue(AppGlobal.g_oLang.get()._("loyalty_search"));
		m_oFrameRightContent.attachChild(m_oLabelSearchTag);
		
		VirtualUIFrame oFrameUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameUnderline, "fraTagUnderline");
		m_oFrameRightContent.attachChild(oFrameUnderline);
	}

	public void addMemberToResultList(int iSectionId, int iItemIndex, String sMemberNo, String sMemberName,
			String sMemberType, String sMemberBonus) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		int iRowHeight = 0;
//RayHuen20171031 -----Start Adjust the text position of the list------
		// Member no.
		iFieldWidths.add(160);
		sFieldValues.add(sMemberNo);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.LEFT + ", " + HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);

		// Member name
		iFieldWidths.add(120);
		sFieldValues.add(sMemberName);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.LEFT + ", "
				+ HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
//RayHuen20171031 -----End Adjust the text position of the list------		

		// Member type
		iFieldWidths.add(120);
		sFieldValues.add(sMemberType);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);

		// bonus balance
		iFieldWidths.add(204);
		sFieldValues.add(sMemberBonus);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);

		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			iRowHeight = 50;
		m_oBasketResultList.addItem(iSectionId, iItemIndex, iRowHeight, iFieldWidths, sFieldValues, sFieldAligns, null,
				null);
		m_oBasketResultList.setFieldPadding(iSectionId, iItemIndex, 1, "0,0,0,30");
// Add by King Cheung 2017-11-27 ---Start---
		m_oBasketResultList.setFieldTextSize(iSectionId, iItemIndex, 0, 16);
		m_oBasketResultList.setFieldTextSize(iSectionId, iItemIndex, 1, 16);
		m_oBasketResultList.setFieldTextSize(iSectionId, iItemIndex, 2, 16);
		m_oBasketResultList.setFieldTextSize(iSectionId, iItemIndex, 3, 16);
// Add by King Cheung 2017-11-27 ---End---
		updatePageUpDownVisibility();
	}

	public void showMemberDetail(MemMember oMember) {
		// Update the member detail page
		m_oFrameLoyaltyMemberDetail.updateDetail(oMember);

		// Show the member detail page and hide the result page
		switchTag(1, true);

		// Hide the right content if mobile station
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oFrameRightContent.setVisible(false);
	}

	public void switchTag(int iTagIndex, boolean bUpdateSelectedTabColor) {
		//if (bUpdateSelectedTabColor)
			//m_oFrameHorizontalTabList.changePageTab(iTagIndex);

		switch (iTagIndex) {
		case 0: // Search Result Tag
			m_oBasketResultList.setVisible(true);
			m_oFrameLoyaltyMemberDetail.setVisible(false);

			m_oFrameRightContent.setVisible(true);
			updatePageUpDownVisibility();
			break;
		case 1: // Member Detail Tag
			m_oBasketResultList.setVisible(false);
			m_oFrameLoyaltyMemberDetail.setVisible(true);
			m_oFrameLoyaltyMemberDetail.changePageTab(0, true);

			if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
				m_oFrameRightContent.setVisible(false);

			m_oImgButtonNextPage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
			m_oFramePage.setVisible(false);
			break;
		}
		m_oCommonPageTabList.clickTag(iTagIndex);
	}

	public FrameCommonBasket getBasketResultList() {
		return m_oBasketResultList;
	}

	public void updatePageUpDownVisibility() {
		boolean bShowPageUp = false;
		boolean bShowPageDown = false;
		int iPage = 0;
		int iCurrentPanelRecordCount = 0;

		iCurrentPanelRecordCount = m_oBasketResultList.getItemCount(0);

		if (iCurrentPanelRecordCount > RESULT_COUNT)
			iPage = (m_iCurrentPageStartNo / RESULT_COUNT) + 1;

		if (m_iCurrentPageStartNo > 0)
			bShowPageUp = true;

		if (iCurrentPanelRecordCount > RESULT_COUNT && m_iCurrentPageStartNo + RESULT_COUNT < iCurrentPanelRecordCount)
			bShowPageDown = true;

		setPageNumber(iPage);
		showPageUp(bShowPageUp);
		showPageDown(bShowPageDown);
	}

	public void showPageUp(boolean bShow) {
		if (bShow)
			m_oImgButtonPrevPage.setSource(
					AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_prev_page_button.png");
		else
			m_oImgButtonPrevPage.setSource(
					AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_prev_page_button_unclick.png");
		m_oImgButtonPrevPage.setEnabled(bShow);
	}

	public void showPageDown(boolean bShow) {
		if (bShow)
			m_oImgButtonNextPage.setSource(
					AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_next_page_button.png");
		else
			m_oImgButtonNextPage.setSource(
					AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_next_page_button_unclick.png");
		m_oImgButtonNextPage.setEnabled(bShow);
	}

	public void setPageNumber(int iNumber) {
		int iTotalPage = 0;
		if (iNumber > 0) {
			iTotalPage = (int) Math.ceil(1.0 * m_oBasketResultList.getItemCount(0) / RESULT_COUNT);
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

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (iChildId == m_oImgButtonPrevPage.getId()) {
			// PAGE UP
			if (m_iCurrentPageStartNo - RESULT_COUNT >= 0) {
				m_iCurrentPageStartNo -= RESULT_COUNT;
				updatePageUpDownVisibility();
				m_iScrollIndex -= RESULT_COUNT;
				m_oBasketResultList.moveScrollToItem(0, m_iScrollIndex);
			}
			bMatchChild = true;
		} else if (iChildId == m_oImgButtonNextPage.getId()) {
			// PAGE DOWN
			if (m_iCurrentPageStartNo + RESULT_COUNT < m_oBasketResultList.getItemCount(0)) {
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
		ArrayList<String> oConditionList = new ArrayList<String>();

		if (!m_oTxtboxMemberNo.getValue().isEmpty() && !m_oTxtboxMemberNo.getValue().matches("^-?\\d+$")) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("invalid_input"));
			oFormDialogBox.show();
			oFormDialogBox = null;
			return;
		}

		if (m_oTxtboxMemberNo.getValue().length() < m_iMinimumLength) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("does_not_match_with_minimum_length"));
			oFormDialogBox.show();
			oFormDialogBox = null;
			return;
		}

		if (!oSearchTextBoxList.isEmpty()) {
			for (VirtualUITextbox oVirtualUITextbox : oSearchTextBoxList) {
				if (oVirtualUITextbox.getValue().length() > Integer
						.parseInt(oSearchList.get(oSearchTextBoxList.indexOf(oVirtualUITextbox) * 6))) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
							this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("does_not_match_with_maximum_length"));
					oFormDialogBox.show();
					oFormDialogBox = null;
					return;
				}

				if (oVirtualUITextbox.getValue().length() < m_iMinimumLength) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
							this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("does_not_match_with_minimum_length"));
					oFormDialogBox.show();
					oFormDialogBox = null;
					return;
				}
				oConditionList.add(oVirtualUITextbox.getValue());
			}
		}
		m_oMemberList.getLoyaltyMemberList().clear();
		m_oBasketResultList.clearAllSections();
		List<PosInterfaceConfig> oInterfaceConfigList = AppGlobal
				.getPosInterfaceConfigByInfType(InfInterface.TYPE_LOYALTY_INTERFACE);
		for (PosInterfaceConfig oPosInterfaceConfig : oInterfaceConfigList) {
			if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY)) {
				// Get the configure from interface module
				m_oMemberList.searchLoyaltyMember(oPosInterfaceConfig.getInterfaceId(), m_oTxtboxMemberNo.getValue(),
						oConditionList);

				if (m_oMemberList.getLoyaltyMemberList().isEmpty()) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), null);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_perform_gm_loyalty_member_search"));
					oFormDialogBox.show();
					oFormDialogBox = null;
					return;
				}
			}
		}

		for (int iIndex : m_oMemberList.getLoyaltyMemberList().keySet()) {
			// for 1 member only
			if (m_oMemberList.getLoyaltyMemberList().size() == 1) {
				switchTag(1, false);
				showMemberDetail(m_oMemberList.getLoyaltyMemberList().get(iIndex));
				addMemberToResultList(0, iIndex, m_oMemberList.getLoyaltyMemberList().get(iIndex).getMemberNo(),
						m_oMemberList.getLoyaltyMemberList().get(iIndex).getName(),
						m_oMemberList.getLoyaltyMemberList().get(iIndex).getType(),
						m_oMemberList.getLoyaltyMemberList().get(iIndex).getBonusBalance());
			} else {
				// more than 1 member return, show list
				switchTag(0, false);
				addMemberToResultList(0, iIndex, m_oMemberList.getLoyaltyMemberList().get(iIndex).getMemberNo(),
						m_oMemberList.getLoyaltyMemberList().get(iIndex).getName(),
						m_oMemberList.getLoyaltyMemberList().get(iIndex).getType(),
						m_oMemberList.getLoyaltyMemberList().get(iIndex).getBonusBalance());
			}
		}
	}

	@Override
	public void FrameNumberPad_clickCancel() {
	}

	@Override
	public void FrameNumberPad_clickNumber(String string) {
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex,
			String sNote) {
		for (FrameLoyaltySearchMemberListener listener : listeners) {
			listener.frameSearchMemberFunction_clickSearchResultRecord(iItemIndex);
			m_oCommonPageTabList.clickTag(1);
			break;
		}
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId, int iSectionIndex, int iItemIndex,
			int iFieldIndex, String sNote) {
	}

	/*
	@Override
	public void frameHorizontalTabList_clickTab(int iTabIndex, int iId) {
		//if (m_oFrameHorizontalTabList.getId() == iId)
			//switchTag(iTabIndex, false);
	}
	*/

	public MemMemberList getLoyaltyMemberList() {
		return m_oMemberList;
	}

	@Override
	public void frameLoyaltyMemberDetail_clickSetMember() {
		// TODO Auto-generated method stub
	}

	@Override
	public void frameLoyaltyMemberDetail_clickClearMember() {
		// TODO Auto-generated method stub
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

				if (sSearchMode.equals(FuncLoyalty.SWIPE_CARD_TYPE_SVC_CARD)) {
					if (oSearchTextBoxList.size() > 0) {
						oSearchTextBoxList.get(0).setValue(sCardNo);
					}
					m_oTxtboxMemberNo.setValue("");
				} else {
					for (int i = 0; i < oSearchTextBoxList.size(); i++) {
						oSearchTextBoxList.get(i).setValue("");
					}
					m_oTxtboxMemberNo.setValue(sCardNo);
				}

				if (!sMsrCode.isEmpty() && !sCardNo.isEmpty() && !bHaveError) {
					FrameNumberPad_clickEnter();
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
	
	@Override
	public void FrameTitleHeader_close() {
		for (FrameLoyaltySearchMemberListener listener : listeners) {
			// Raise the event to parent
			listener.frameSearchMemberFunction_clickCancel();
		}
	}

	@Override
	public void frameCommonPageContainer_changeFrame() {
		// TODO Auto-generated method stub
		
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
	// Edit by King Cheung 2017-11-27 ---End---
}
