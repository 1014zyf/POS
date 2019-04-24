package app;

import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import java.util.ArrayList;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;

import templatebuilder.TemplateBuilder;

/** interface for the listeners/observers callback method */
interface FrameChangeOverrideConditionListener {
	void fraChangeOverrideCondition_clickExit();
	void fraChangeOverrideCondition_clickSave();
	void fraChangeOverrideCondition_prevPage();
	void fraChangeOverrideCondition_nextPage();
	void fraChangeOverrideCondition_activeStatusClicked(int iItemIndex);
//KingsleyKwan20170918ByNick		-----Start-----
//	void fraChangeOverrideCondition_susptendedStatusClicked(int iItemIndex);
//KingsleyKwan20170918ByNick		-----End-----
}

public class FrameChangeOverrideCondition extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;

	private FrameTitleHeader m_oFrameTitleHeader;
	private FrameCommonBasket m_oFrameOverrideConditionList;
	
	private VirtualUIFrame m_oFramePage = null;
	private VirtualUILabel m_oLblPage = null;
	private VirtualUIImage m_oImgButtonPrevPage = null;
	private VirtualUIImage m_oImgButtonNextPage = null;
	private VirtualUIButton m_oButtonSave;
//KingsleyKwan20170918ByNick		-----Start-----
	public static final String STATUS_ICON_ACTIVE = AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/toggle_on.png";
//	public static final String STATUS_ICON_ACTIVE_ClICKED = AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/status_active_clicked.png";
	public static final String STATUS_ICON_SUSPENDED = AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/toggle_off.png";
//	public static final String STATUS_ICON_SUSPENDED_CLICKED = AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/status_suspended_clicked.png";
//KingsleyKwan20170918ByNick		-----End-----
	
	public static final String BACKGROUND_COLOR_SELECTED = "#FFF97D";
	public static final String BACKGROUND_COLOR_UNSELECTED = "#FFFFFF";

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameChangeOverrideConditionListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameChangeOverrideConditionListener listener) {
		listeners.add(listener);
	}

    /** remove a ModelListener observer for this Model */
	public void removeListener(FrameChangeOverrideConditionListener listener) {
		listeners.remove(listener);
	}

    /** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FrameChangeOverrideCondition() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameChangeOverrideConditionListener>();

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraChangeOverrideCondidtion.xml");

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
	    m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
	    m_oFrameTitleHeader.init(true);
	    m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("override_condition_activation"));
	    m_oFrameTitleHeader.addListener(this);
	    this.attachChild(m_oFrameTitleHeader);

		// Override Condition list
		m_oFrameOverrideConditionList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFrameOverrideConditionList, "fraOverrideConditionList");
		m_oFrameOverrideConditionList.init();
		m_oFrameOverrideConditionList.addListener(this);
		this.attachChild(m_oFrameOverrideConditionList);

		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
//KingsleyKwan20170918ByNick		-----Start-----
		iFieldWidths.add(626);
		sFieldValues.add(AppGlobal.g_oLang.get()._("name"));
		iFieldWidths.add(208);
		sFieldValues.add(AppGlobal.g_oLang.get()._("priority"));
		iFieldWidths.add(72);
		sFieldValues.add(AppGlobal.g_oLang.get()._("status"));
//KingsleyKwan20170918ByNick		-----End-----
		
		m_oFrameOverrideConditionList.addHeader(iFieldWidths, sFieldValues);
//KingsleyKwan20170918ByNick		-----Start-----
		m_oFrameOverrideConditionList.setHeaderFormat(57, 24, "14,0,0,0");
//KingsleyKwan20170918ByNick		-----End-----
		m_oFrameOverrideConditionList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);

		// Create prev page button
		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevPage");
//KingsleyKwan20170918ByNick		-----Start-----
		m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
//KingsleyKwan20170918ByNick		-----End-----
		m_oImgButtonPrevPage.allowClick(true);
		m_oImgButtonPrevPage.setClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.allowLongClick(true);
		m_oImgButtonPrevPage.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oImgButtonPrevPage);
		
		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
//KingsleyKwan20170918ByNick		-----Start-----
		m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
//KingsleyKwan20170918ByNick		-----End-----
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.allowLongClick(true);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oImgButtonNextPage);
		
		//Create Page label
		m_oFramePage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePage, "fraPage");
//KingsleyKwan20170918ByNick		-----Start-----
/*
		oImage = new VirtualUIImage();
		oImage.setWidth(m_oFramePage.getWidth());
		oImage.setHeight(m_oFramePage.getHeight());
		oImage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_page_bg.png");
		m_oFramePage.attachChild(oImage);
*/
//KingsleyKwan20170918ByNick		-----Start-----
		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oLblPage.setWidth(m_oFramePage.getWidth());
		m_oLblPage.setHeight(m_oFramePage.getHeight());
		m_oFramePage.attachChild(m_oLblPage);
		
		this.attachChild(m_oFramePage);

		// Save button
		m_oButtonSave = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSave, "btnSave");
		m_oButtonSave.setValue(AppGlobal.g_oLang.get()._("save"));
		m_oButtonSave.setVisible(true);
		this.setSaveButtonEnabled(false);	// Set "Save" button to unclicked
		this.attachChild(m_oButtonSave);
	}

	public void addOverrideConditionRecord(int iIndex, String sPriority, String sName, boolean bActive) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		ArrayList<String> sFieldTypes = new ArrayList<String>();

		String sActiveImage = STATUS_ICON_ACTIVE;
//KingsleyKwan20170918ByNick		-----Start-----
		if (bActive)
			sActiveImage = STATUS_ICON_ACTIVE;
		else
			sActiveImage = STATUS_ICON_SUSPENDED;
   
		iFieldWidths.add(626);
		sFieldValues.add(sName);
		sFieldAligns.add("");
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		iFieldWidths.add(206);
		sFieldValues.add(sPriority);
		sFieldAligns.add("");
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		iFieldWidths.add(144);
		sFieldValues.add(sActiveImage);
		sFieldAligns.add("");
		sFieldTypes.add(HeroActionProtocol.View.Type.IMAGE);
		m_oFrameOverrideConditionList.addItem(0, iIndex, 52, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
//KingsleyKwan20170918ByNick		-----End-----
	}
	
	public void clearOverrideConditionRecords() {
		m_oFrameOverrideConditionList.removeAllItems(0);
	}

	public void setSaveButtonEnabled(boolean bEnabled) {
		this.m_oButtonSave.setEnabled(bEnabled);
//KingsleyKwan20170918ByNick		-----Start-----
		if(bEnabled)
			m_oButtonSave.setForegroundColor("#FFFFFF");
//KingsleyKwan20170918ByNick		-----End-----
		else
			m_oButtonSave.setForegroundColor("#A0A0A0");
	}
	
	public void updatePageButton(int iCurrentPage, int iTotalPage) {
		m_oLblPage.setValue(iCurrentPage + "/" + iTotalPage);
		
		if (iTotalPage > 1) {
			m_oFramePage.setVisible(true);
			if (iCurrentPage > 1)
				m_oImgButtonPrevPage.setVisible(true);
			else
				m_oImgButtonPrevPage.setVisible(false);
			
			if (iCurrentPage < iTotalPage)
				m_oImgButtonNextPage.setVisible(true);
			else
				m_oImgButtonNextPage.setVisible(false);
		} else {
			m_oFramePage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
			m_oImgButtonNextPage.setVisible(false);
		}
	}
	
	public void changeActionStatusIcon(int iItemIndex, String sImage) {
		m_oFrameOverrideConditionList.setFieldValue(0, iItemIndex, 2, sImage);
	}
	
	public void changeSuspendedStatusIcon(int iItemIndex, String sImage) {
		m_oFrameOverrideConditionList.setFieldValue(0, iItemIndex, 3, sImage);
	}
	
	/*
	 * bActive: true = active status field clicked,
	 * 			false = suspended status field clicked
	 * bChanged: true = status is changed
	 */
	public void updateRecordBackgroundColor(int iItemIndex, boolean bActive, boolean bChanged) {
		String sActiveFieldBackgroundColor = (bActive && bChanged)? BACKGROUND_COLOR_SELECTED: BACKGROUND_COLOR_UNSELECTED;
		String sSuspendedFieldBackgroundColor = (!bActive && bChanged)? BACKGROUND_COLOR_SELECTED: BACKGROUND_COLOR_UNSELECTED;
		m_oFrameOverrideConditionList.setFieldBackgroundColor(0, iItemIndex, 2 , sActiveFieldBackgroundColor);
		m_oFrameOverrideConditionList.setFieldBackgroundColor(0, iItemIndex, 3, sSuspendedFieldBackgroundColor);
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (iChildId == m_oButtonSave.getId()) {
			for (FrameChangeOverrideConditionListener listener : listeners)
				listener.fraChangeOverrideCondition_clickSave();
			bMatchChild = true;
		} else if (iChildId == m_oImgButtonPrevPage.getId()) {
			for (FrameChangeOverrideConditionListener listener : listeners)
				listener.fraChangeOverrideCondition_prevPage();
			bMatchChild = true;
		} else if (iChildId == m_oImgButtonNextPage.getId()) {
			for (FrameChangeOverrideConditionListener listener : listeners)
				listener.fraChangeOverrideCondition_nextPage();
			bMatchChild = true;
		}
		return bMatchChild;
	}
	
	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		if (iFieldIndex == 2)	// Click active icon
			for (FrameChangeOverrideConditionListener listener : listeners)
				listener.fraChangeOverrideCondition_activeStatusClicked(iItemIndex);
//KingsleyKwan20170918ByNick		-----Start-----
//		else if (iFieldIndex == 3)	// Click suspended icon
//			for (FrameChangeOverrideConditionListener listener : listeners)
//				listener.fraChangeOverrideCondition_susptendedStatusClicked(iItemIndex);
//KingsleyKwan20170918ByNick		-----End-----
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}
	
	@Override
	public void FrameTitleHeader_close() {
		for (FrameChangeOverrideConditionListener listener : listeners) {
			// Raise the event to parent
			listener.fraChangeOverrideCondition_clickExit();
		}
	}
}
