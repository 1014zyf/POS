package app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import templatebuilder.TemplateBuilder;
import virtualui.*;

interface FrameCommonLookupListener {
	void frameCommonLookup_buttonExit();
	void frameCommonLookup_buttonFinish();
	void frameCommonLookup_buttonPrevious();
	void frameCommonLookup_tabClicked(int iIndex);
	void frameCommonLookup_lookupClicked(int iIndex);
	void frameCommonLookup_selectedItemClicked(int iSelectedItemIndex);
}

public class FrameCommonLookup extends VirtualUIFrame implements FrameCommonLookupButtonsListener, FrameCommonPageContainerListener, FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;

	private VirtualUIImage m_oImageItem;
	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUILabel m_oLabelDescBar;
	private VirtualUILabel m_oLabelBreadcrumbs;
	private VirtualUIHorizontalList m_oHorizontalSelectedItemList;
	private FrameCommonPageContainer m_oFrameCommonPageContainer;
	private List<VirtualUIFrame> m_oTabsArray;
	private FrameCommonLookupButtons m_oLookupButtonsPanel;
	private VirtualUIFrame m_oFramePageContainerContent;
	//private VirtualUILabel m_oLblMinMaxSelected;
	private VirtualUIButton m_oButtonFinish;
	private VirtualUIButton m_oButtonBack;

	private VirtualUILabel m_oLblMin;
	private VirtualUILabel m_oLblMax;
	private VirtualUILabel m_oLblSelectedAmount;
	private VirtualUILabel m_oLabelMustChoose;
									
	private VirtualUIFrame m_oFrameError;
	private VirtualUIImage m_oImageError;
	private VirtualUILabel m_oLabelErrorDesc;

	private VirtualUIFrame m_oFramePageButton;
									 
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIImage m_oImgButtonNextPage;

	private boolean m_bSingleSelect;
	private List<VirtualUIFrame> m_oSelectedButtonList;

	private static final int ROW_NUM = 4;
	private static final int ROW_NUM_FOR_MOBILE = 3;
	private static final int COLUMN_NUM = 6;
	private static final int COLUMN_NUM_FOR_MOBILE = 3;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameCommonLookupListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameCommonLookupListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameCommonLookupListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameCommonLookup() {
		m_oTemplateBuilder = new TemplateBuilder();
		this.listeners = new ArrayList<FrameCommonLookupListener>();

		m_oImageItem = new VirtualUIImage();
		m_oLabelDescBar = new VirtualUILabel();
		m_oLabelBreadcrumbs = new VirtualUILabel();
		m_oHorizontalSelectedItemList = new VirtualUIHorizontalList();
		//m_oHorizontalTabs = new VirtualUIHorizontalList();
		m_oTabsArray = new ArrayList<VirtualUIFrame>();
		// Create different panel
		int iRowCnt = ROW_NUM;
		int iColCnt = COLUMN_NUM;
		int iFontSize = FrameLookupButton.BUTTON_DESC_FONT_SIZE;
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			iRowCnt = ROW_NUM_FOR_MOBILE;
			iColCnt = COLUMN_NUM_FOR_MOBILE;
		}
		String sCommonLookupNumber = AppGlobal.g_oFuncStation.get().getCommonLookupButtonNumber();
		if (sCommonLookupNumber != null) {
			try {
				JSONObject oCommonLookupJSONObject = new JSONObject(sCommonLookupNumber);
				JSONObject oJSONObject;
				if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
					oJSONObject = oCommonLookupJSONObject.optJSONObject("mobile");
					iRowCnt = oJSONObject.optInt("row", FrameCommonLookup.ROW_NUM_FOR_MOBILE);
				} else {
					oJSONObject = oCommonLookupJSONObject.optJSONObject("tablet");
					iRowCnt = oJSONObject.optInt("row", FrameCommonLookup.ROW_NUM);
				}
				iColCnt = oJSONObject.optInt("column", FrameCommonLookup.COLUMN_NUM);
				iFontSize = oJSONObject.optInt("font_size", FrameLookupButton.BUTTON_DESC_FONT_SIZE);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		m_oTemplateBuilder.loadTemplate("fraCommonLookup.xml");
		
		m_oLookupButtonsPanel = new FrameCommonLookupButtons();
		m_oTemplateBuilder.buildFrame(m_oLookupButtonsPanel, "fraCommonLookupButtons");
		m_oLookupButtonsPanel.init(iRowCnt, iColCnt, iFontSize);
		m_oLookupButtonsPanel.addListener(this);
		
		m_oButtonFinish = new VirtualUIButton();
		
		m_oSelectedButtonList = new ArrayList<VirtualUIFrame>();

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
	    m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
	    m_oFrameTitleHeader.init(true);
	    m_oFrameTitleHeader.addListener(this);
	    this.attachChild(m_oFrameTitleHeader);
	    
		// Common lookup image
		m_oTemplateBuilder.buildImage(m_oImageItem, "imgItem");
		this.attachChild(m_oImageItem);
		
		// Description bar
		m_oTemplateBuilder.buildLabel(m_oLabelDescBar, "lblDescBar");
		this.attachChild(m_oLabelDescBar);
		
		// Breadcrumbs
		m_oTemplateBuilder.buildLabel(m_oLabelBreadcrumbs, "lblBreadcrumbs");
		this.attachChild(m_oLabelBreadcrumbs);
		
		// Horizontal selected item list
		m_oTemplateBuilder.buildHorizontalList(m_oHorizontalSelectedItemList, "horListSelectedItems");
		this.attachChild(m_oHorizontalSelectedItemList);
		
		// Min/Max Selected frame
		m_oLblMin = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblMin, "lblMin");
		
		// Min/Max Selected frame
		m_oLblMax = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblMax, "lblMax");
		
		// Min/Max Selected frame
		m_oLblSelectedAmount = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblSelectedAmount, "lblSelectedAmount");
		
		// Lookup panel Must Choose Indicator
		m_oLabelMustChoose = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMustChoose, "lblMustChoose");
		m_oLabelMustChoose.setValue("*: " + AppGlobal.g_oLang.get()._("must_choose"));
		
		m_oFramePageContainerContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePageContainerContent, "fraPageContainer");
		m_oFramePageContainerContent.attachChild(m_oLookupButtonsPanel);
		m_oFramePageContainerContent.attachChild(m_oLblMin);
		m_oFramePageContainerContent.attachChild(m_oLblMax);
		m_oFramePageContainerContent.attachChild(m_oLblSelectedAmount);
		m_oFramePageContainerContent.attachChild(m_oLabelMustChoose);
		
		m_oFrameCommonPageContainer = new FrameCommonPageContainer();
												 
		m_oTemplateBuilder.buildFrame(m_oFrameCommonPageContainer, "fraPageContainer");
		m_oFrameCommonPageContainer.init(m_oFrameCommonPageContainer.getWidth(),
				m_oFrameCommonPageContainer.getHeight(), 160, 44, 3, "#0055B8", "#999999", "", "", 120, false, true);
		m_oFrameCommonPageContainer.setUpperlineColor("#DDDDDD");
		m_oFrameCommonPageContainer.setUnderlineColor("#DDDDDD");
		m_oFrameCommonPageContainer.addListener(this);
		this.attachChild(m_oFrameCommonPageContainer);
		
		// Finish button
		m_oTemplateBuilder.buildButton(m_oButtonFinish, "butFinish");
		m_oButtonFinish.setValue(AppGlobal.g_oLang.get()._("done"));
		m_oButtonFinish.setClickServerRequestBlockUI(false);
		this.attachChild(m_oButtonFinish);
		
		m_oButtonBack = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonBack, "butBack");
		m_oButtonBack.setValue(AppGlobal.g_oLang.get()._("back"));
										 
		this.attachChild(m_oButtonBack);
		
		// Create page bar
		m_oFramePageButton = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePageButton, "fraPageButton");
		this.attachChild(m_oFramePageButton);
		
		// Create prev page button
		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevPage");
		m_oImgButtonPrevPage
				.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_pageprevious.png");
		m_oImgButtonPrevPage.allowClick(true);
		m_oImgButtonPrevPage.setClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.allowLongClick(true);
		m_oImgButtonPrevPage.setLongClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.setVisible(false);
		m_oFramePageButton.attachChild(m_oImgButtonPrevPage);
		
		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		//m_oLblPage.setWidth(m_oFramePage.getWidth());
		//m_oLblPage.setLeft(m_oImgButtonPrevPage.getWidth()+35);
		//m_oLblPage.setHeight(m_oFramePageButton.getHeight());
		m_oFramePageButton.attachChild(m_oLblPage);
		
		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage
				.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_pagenext.png");
		//m_oImgButtonNextPage.setLeft(m_oLblPage.getLeft()+m_oLblPage.getWidth()+37);
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.allowLongClick(true);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.setVisible(false);
		m_oFramePageButton.attachChild(m_oImgButtonNextPage);
		
		// Error frame
		m_oFrameError = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameError, "fraError");
		
		m_oImageError = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageError, "imgError");
		m_oImageError.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/error_icon.png");
		m_oFrameError.attachChild(m_oImageError);
		
		m_oLabelErrorDesc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelErrorDesc, "lblErrorDesc");
		m_oFrameError.attachChild(m_oLabelErrorDesc);
		
		m_oFrameError.setVisible(false);
		this.attachChild(m_oFrameError);
		
		// Set no image first
		setLookupImage(null);
		
		m_bSingleSelect = false;
	}

	public void addLookupTab(int iTabId, String sTabName) {
		if(m_oTabsArray.isEmpty())
			this.m_oFrameCommonPageContainer.addButton(sTabName, m_oFramePageContainerContent);
		else
			this.m_oFrameCommonPageContainer.addButton(sTabName, null);
	}

	public void updateLookupButtons(List<FuncLookupButtonInfo> oLookupList) {
		// Create different panel
		m_oLookupButtonsPanel.updateLookupButtons(oLookupList, null);
		showPageButton();

		// Set single select
		if (m_bSingleSelect)
			m_oLookupButtonsPanel.setSingleSelection(m_bSingleSelect);
	}

	public void addSelectedItem(int iPanelIndex, String sName) {
		VirtualUIFrame oFrameSelectedItem = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameSelectedItem, "fraSelectedItem");
		oFrameSelectedItem.allowClick(true);
		oFrameSelectedItem.setClickServerRequestNote(Integer.toString(iPanelIndex));
		oFrameSelectedItem.setClickServerRequestBlockUI(false);
		
		VirtualUIButton oBtnSelectedItem = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(oBtnSelectedItem, "butSelectedItem");
		oBtnSelectedItem.setWidth(oFrameSelectedItem.getWidth());
		oBtnSelectedItem.setHeight(oFrameSelectedItem.getHeight());
		oBtnSelectedItem.setValue(sName);
		oBtnSelectedItem.allowClick(false);
		oFrameSelectedItem.attachChild(oBtnSelectedItem);
		
		VirtualUIImage oImageDeleteSelectedItem = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(oImageDeleteSelectedItem, "imgDeleteSelectedItem");
		oImageDeleteSelectedItem.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_btn_delete.png");
		oFrameSelectedItem.attachChild(oImageDeleteSelectedItem);
		
		/*
		VirtualUILabel oLabelSelectedItem = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelSelectedItem, "lblSelectedItem");
		oLabelSelectedItem.setLeft(oFrameSelectedItem.getWidth() - oImageDeleteSelectedItem.getLeft());
		oLabelSelectedItem.setWidth(oFrameSelectedItem.getWidth() - (oLabelSelectedItem.getLeft() * 2));
		oLabelSelectedItem.setHeight(oFrameSelectedItem.getHeight());
		oLabelSelectedItem.setValue(sName);
		oFrameSelectedItem.attachChild(oLabelSelectedItem);
		*/
		
		m_oSelectedButtonList.add(oFrameSelectedItem);
		m_oHorizontalSelectedItemList.attachChild(oFrameSelectedItem);
		m_oHorizontalSelectedItemList.scrollToIndex(m_oSelectedButtonList.size() - 1);
	}
	
	public void clickTab(int iIndex) {
		m_oFrameCommonPageContainer.clickTag(iIndex);
	}

	public void showPageButton() {
		int iCurrPage = m_oLookupButtonsPanel.getCurrentPage();
		int iPageCount = m_oLookupButtonsPanel.getPageCount();
		
		m_oLblPage.setValue(iCurrPage + " / " + iPageCount);
		
		if (iPageCount > 1) {
			m_oLblPage.setVisible(true);
			if (iCurrPage > 1)
				m_oImgButtonPrevPage.setVisible(true);
			else
				m_oImgButtonPrevPage.setVisible(false);

			if (iCurrPage < iPageCount)
				m_oImgButtonNextPage.setVisible(true);
			else
				m_oImgButtonNextPage.setVisible(false);
		} else {
			m_oLblPage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
			m_oImgButtonNextPage.setVisible(false);
		}
	}

	public void removeSelectedItem(int iSelectedItemIndex) {
		VirtualUIFrame oButtonRemoveItem = m_oSelectedButtonList.get(iSelectedItemIndex);

		// remove the selected item
		m_oSelectedButtonList.remove(iSelectedItemIndex);

		// update the UI
		m_oHorizontalSelectedItemList.removeChild(oButtonRemoveItem.getId());
	}

	// Show or Hide Previous button
	public void showPreviousButton(boolean bShow) {
		m_oButtonBack.setVisible(bShow);
	}

	// Set to support single selection
	public void setSingleSelection(boolean bSingleSelect) {
		m_bSingleSelect = bSingleSelect;

		m_oButtonFinish.setVisible((!bSingleSelect));
		m_oLookupButtonsPanel.setSingleSelection(bSingleSelect);
	}

	// update maximum, minimum and selected count
	public void updateMinMaxSelection(int iMax, int iMin, int iCount) {
		/*
		String sCount = "";
		if (iMin > 0)
			sCount = sCount + AppGlobal.g_oLang.get()._("min") + ": " + iMin + "     ";
		if (iMax > 0)
			sCount = sCount + AppGlobal.g_oLang.get()._("max") + ": " + iMax + "     ";
		sCount = sCount + " " + AppGlobal.g_oLang.get()._("selected") + ": " + iCount;
		 */
		//m_oLblMinMaxSelected.setValue(sCount);
		m_oLblMin.setValue(AppGlobal.g_oLang.get()._("min") + ": " + iMin);
		m_oLblMax.setValue(AppGlobal.g_oLang.get()._("max") + ": " + iMax);
		m_oLblSelectedAmount.setValue(AppGlobal.g_oLang.get()._("selected") + ": " + iCount);
		
		if(iMin <= 0 && iMax <= 0) {
			m_oLblMin.setVisible(false);
			m_oLblMax.setVisible(false);
			m_oLblSelectedAmount.setLeft(m_oLblMin.getLeft());
			m_oLabelMustChoose.setLeft(m_oLblMax.getLeft());
		} else {
			if(!m_oLblMin.getVisible() && !m_oLblMax.getVisible()) {
				m_oLblSelectedAmount.setLeft(m_oLblMax.getLeft() + m_oLblMax.getWidth());
				m_oLabelMustChoose.setLeft(m_oLblSelectedAmount.getLeft() + m_oLblSelectedAmount.getWidth());
			}
			m_oLblMin.setVisible(true);
			m_oLblMax.setVisible(true);
		}
	}

	public void setLabelTitleBar(String sTitleBar) {
		m_oFrameTitleHeader.setTitle(sTitleBar);
	}

	public void setLabelDescBar(String sDescBar) {
		m_oLabelDescBar.setValue(sDescBar);
	}

	public void setLabelBreadcrumbs(String sBreadcrumbs) {
		m_oLabelBreadcrumbs.setValue(sBreadcrumbs);
	}

	public void setLookupImage(String sImageUrl) {
		if (sImageUrl == null) {
//JohnLiu 01112017 -- start
			m_oLabelDescBar.setLeft(24);
			m_oLabelBreadcrumbs.setLeft(24);
//JohnLiu 01112017 -- end
			m_oHorizontalSelectedItemList.setLeft(10);
			m_oImageItem.setVisible(false);
		} else {
			m_oLabelDescBar.setLeft(m_oImageItem.getLeft() + m_oImageItem.getWidth() + 10);
			m_oLabelBreadcrumbs.setLeft(m_oImageItem.getLeft() + m_oImageItem.getWidth() + 10);
			m_oHorizontalSelectedItemList.setLeft(m_oImageItem.getLeft() + m_oImageItem.getWidth() + 10);
			m_oImageItem.setSource(sImageUrl);
			m_oImageItem.setVisible(true);
		}
	}

	public void showErrorFrame(String sErrorMsg) {
		m_oLabelErrorDesc.setValue(sErrorMsg);
		m_oFrameError.setVisible(true);
	}

	public void hideCloseButton(boolean bHide) {
		m_oFrameTitleHeader.setButtonShow(!bHide);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		m_oFrameError.setVisible(false);

		if (iChildId == m_oButtonBack.getId()) { // Previous button
														// clicked
			for (FrameCommonLookupListener listener : listeners) {
				listener.frameCommonLookup_buttonPrevious();
				break;
			}
			return true;
		} else if (iChildId == m_oButtonFinish.getId()) { // Finish button
															// clicked
			for (FrameCommonLookupListener listener : listeners) {
				listener.frameCommonLookup_buttonFinish();
				break;
			}
			return true;
		} else if (iChildId == m_oImgButtonPrevPage.getId()) { // Previous
																// button
																// clicked
			m_oLookupButtonsPanel.prevPage();
			showPageButton();
			return true;
		} else if (iChildId == m_oImgButtonNextPage.getId()) { // New button
																// clicked
			m_oLookupButtonsPanel.nextPage();
			showPageButton();
			return true;
		} else {
			boolean bMatchChild = false;
			for (int i = 0; i < m_oHorizontalSelectedItemList.getChildCount(); i++) {
				VirtualUIFrame oButtonSelectedItem = (VirtualUIFrame) m_oHorizontalSelectedItemList.getChilds()
						.get(i);
				if (iChildId == oButtonSelectedItem.getId()) {
					for (FrameCommonLookupListener listener : listeners) {
						listener.frameCommonLookup_selectedItemClicked(i);
						break;
					}
					bMatchChild = true;
					break;
				}
			}

			if (!bMatchChild) {
				for (VirtualUIFrame oFraTab : m_oTabsArray) {
					if (iChildId == oFraTab.getId()) {
						for (FrameCommonLookupListener listener : listeners) {
							// Return to Common Lookup form
							listener.frameCommonLookup_tabClicked(m_oTabsArray.indexOf(oFraTab));
							break;
						}
						bMatchChild = true;
						break;
					}
				}
			}
			return bMatchChild;
		}
	}

	@Override
	public void frameCommonLookupButtons_addItem(String sNote) {
		m_oFrameError.setVisible(false);

		try {
			JSONObject oJSONObject = new JSONObject(sNote);
			int iIndex = oJSONObject.optInt(FrameLookupButton.BUTTON_NOTE_SEQ);
			for (FrameCommonLookupListener listener : listeners) {
				listener.frameCommonLookup_lookupClicked(iIndex);
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void frameCommonLookupButtons_deleteItem(String sNote) {
	}

	@Override
	public void frameCommonLookupButtons_swipePage(boolean bLeft) {
		if (bLeft)
			m_oLookupButtonsPanel.prevPage();
		else
			m_oLookupButtonsPanel.nextPage();
		showPageButton();
	}

	@Override
	public void frameCommonPageContainer_changeFrame() {
		// TODO Auto-generated method stub
		
	}

//KingsleyKwan20171031		-----Start-----
	@Override
	public boolean frameCommonPageContainer_swipe(boolean bLeft, String sNote, int iChildId) {
//KinngsleyKwan20171031		----- End -----
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void frameCommonPageContainer_updateFrame(int iOrigIndex, int iIndex, String sFrameName) {
		// TODO Auto-generated method stub
		
		for (FrameCommonLookupListener listener : listeners) {
			// Return to Common Lookup form
			listener.frameCommonLookup_tabClicked(iIndex);
			break;
		}
	}
	
	@Override
	public void FrameTitleHeader_close() {
		for (FrameCommonLookupListener listener : listeners) {
			// Raise the event to parent
			listener.frameCommonLookup_buttonExit();
		}
	}
	
//KingsleyKwan20171120		-----Start-----
	@Override
	public void frameCommonPageContainer_CloseImageClicked() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void frameCommonPageContainer_ClickButtonIndex(int iIndex) {
		// TODO Auto-generated method stub
		
	}
//KingsleyKwan20171120		----- End -----
}
