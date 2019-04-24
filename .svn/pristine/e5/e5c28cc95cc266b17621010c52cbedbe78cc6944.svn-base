package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import om.PosDisplayPanelLookup;

import templatebuilder.*;
import virtualui.*;

/** interface for the listeners/observers callback method **/
interface FrameOrderingMenuLookupListener {
	void frameOrderingMenuLookup_functionClicked(String sNote);
	void frameOrderingMenuLookup_lookupClicked(String sNote);
	void frameOrderingMenuLookup_updateButtonsStockQty(HashMap<Integer, Integer> oItemIdList);
}

public class FrameOrderingMenuLookup extends VirtualUIFrame implements FrameCommonLookupButtonsListener {
	private TemplateBuilder m_oTemplateBuilder;
	private VirtualUIFrame m_oFrameTitleBar;
	private VirtualUILabel m_oLblLookupPath;
	
	private VirtualUIFrame m_oFraButtonClose;
	private VirtualUIFrame m_oFrameTitleSeperator;
	private VirtualUIButton m_oButtonBack;

	private VirtualUIFrame m_oFramePageButton;
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIImage m_oImgButtonNextPage;

	private FrameCommonLookupButtons m_oLookupButtonsPanel;

	private class SubLookupInfo {
		String menuName;
		List<FuncLookupButtonInfo> lookupButtonInfoList;

		SubLookupInfo(String sMenuName, List<FuncLookupButtonInfo> oLookupButtonInfoList) {
			menuName = sMenuName;
			lookupButtonInfoList = oLookupButtonInfoList;
		}
	}

	private List<SubLookupInfo> m_oSubLookupInfoList; // List for storing lookup path info

	private int m_iRows;
	private int m_iCols;
	private int m_iRowsWithImage;
	private int m_iColsWithImage;
	private int m_iButtonDescFontSize;

	public static final int ROW_NUM = 3;
	public static final int COLUMN_NUM = 4;
	public static final int ROW_NUM_WITH_IMAGE = 2;
	public static final int COLUMN_NUM_WITH_IMAGE = 4;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameOrderingMenuLookupListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameOrderingMenuLookupListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameOrderingMenuLookupListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameOrderingMenuLookup() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameOrderingMenuLookupListener>();
		m_oSubLookupInfoList = new ArrayList<SubLookupInfo>();

		m_iRows = ROW_NUM;
		m_iCols = COLUMN_NUM;
		m_iRowsWithImage = ROW_NUM_WITH_IMAGE;
		m_iColsWithImage = COLUMN_NUM_WITH_IMAGE;
		m_iButtonDescFontSize = FrameLookupButton.BUTTON_DESC_FONT_SIZE;

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraOrderingMenuLookup.xml");
	}

	/** View Related Methods **/
	// Create Function buttons
	public void createFunctionButtons() {
		VirtualUIImage oImage;

		m_oFrameTitleBar = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleBar, "fraTitleBar");
		m_oFrameTitleBar.setWidth(this.getWidth() - (m_oFrameTitleBar.getLeft() * 2));
		this.attachChild(m_oFrameTitleBar);

		// Title bar
		m_oLblLookupPath = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblLookupPath, "lblLookupPath");
		m_oFrameTitleBar.attachChild(m_oLblLookupPath);

		// Create Close button
		m_oFraButtonClose = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFraButtonClose, "fraButClose");
		oImage = new VirtualUIImage();
		oImage.setWidth(m_oFraButtonClose.getWidth());
		oImage.setHeight(m_oFraButtonClose.getHeight());
		oImage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_esc_sh.png");
		m_oFraButtonClose.attachChild(oImage);

		m_oFraButtonClose.setClickServerRequestNote("butClose");
		m_oFraButtonClose.allowClick(true);
		m_oFraButtonClose.setClickServerRequestBlockUI(false);
		
		m_oFrameTitleBar.attachChild(m_oFraButtonClose);

		// Title bar
		m_oFrameTitleSeperator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleSeperator, "fraTitleSeparator");
		m_oFrameTitleSeperator.setWidth(m_oFrameTitleBar.getWidth()-m_oFrameTitleSeperator.getLeft()*2);
		m_oFrameTitleBar.attachChild(m_oFrameTitleSeperator);
		m_oFraButtonClose.setLeft(m_oFrameTitleBar.getWidth() - m_oFraButtonClose.getWidth() - (m_oFrameTitleBar.getWidth() - m_oFrameTitleSeperator.getLeft() - m_oFrameTitleSeperator.getWidth()));

		// Create Back button
		m_oButtonBack = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonBack, "btnBack");
		m_oButtonBack.setValue(AppGlobal.g_oLang.get()._("back"));
		
		//m_oButtonBack.setClickServerRequestNote("butBack");
		//m_oButtonBack.setLongClickServerRequestNote("butBack");
		
		m_oButtonBack.setVisible(false);
		this.attachChild(m_oButtonBack);

		m_oLookupButtonsPanel = new FrameCommonLookupButtons();
		m_oTemplateBuilder.buildFrame(m_oLookupButtonsPanel, "fraPanel");

		m_oLookupButtonsPanel.setWidth(this.getWidth());
		m_oLookupButtonsPanel.setLeft(0);
		m_oLookupButtonsPanel.init();
		m_oLookupButtonsPanel.addListener(this);
		this.attachChild(m_oLookupButtonsPanel);

		// Create page bar
		m_oFramePageButton = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePageButton, "fraPageButton");

		m_oFramePageButton.setLeft(this.getWidth()/2 - m_oFramePageButton.getWidth()/2);
		this.attachChild(m_oFramePageButton);

		// Create prev page button
		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevPage");
		m_oImgButtonPrevPage.allowClick(true);
		m_oImgButtonPrevPage.setClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.allowLongClick(true);
		m_oImgButtonPrevPage.setLongClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.setVisible(false);
		if (!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_pageprevious.png");
			m_oFramePageButton.attachChild(m_oImgButtonPrevPage);
		} else {
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/lookup_prev_page_button.png");
			this.attachChild(m_oImgButtonPrevPage);
		}

		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		if (!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oLblPage.setLeft(m_oImgButtonPrevPage.getWidth() + 35);
			m_oLblPage.setHeight(m_oFramePageButton.getHeight());
			m_oFramePageButton.attachChild(m_oLblPage);
		} else
			this.attachChild(m_oLblPage);

		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.allowLongClick(true);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.setVisible(false);
		if (!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_pagenext.png");
			m_oImgButtonNextPage.setLeft(m_oLblPage.getLeft() + m_oLblPage.getWidth() + 37);
			m_oFramePageButton.attachChild(m_oImgButtonNextPage);
		} else {
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/lookup_next_page_button.png");
			this.attachChild(m_oImgButtonNextPage);
		}

		// Create Page label
		// m_oLblPage = new VirtualUILabel();
		// m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		// this.attachChild(m_oLblPage);
	}

	public void setConfig(int iRows, int iCols, int iRowsWithImage, int iColsWithImage, int iButtonDescFontSize) {
		m_iRows = iRows;
		m_iCols = iCols;
		m_iRowsWithImage = iRowsWithImage;
		m_iColsWithImage = iColsWithImage;
		m_iButtonDescFontSize = iButtonDescFontSize;
	}

	public void updateMenuLookupButtons(List<FuncLookupButtonInfo> oData) {
		boolean bHaveImage = false;
		for (FuncLookupButtonInfo oButtonInfo : oData) {
			if (oButtonInfo.getImage() != null && !oButtonInfo.getImage().isEmpty()) {
				bHaveImage = true;
				break;
			}
		}

		int iRows = m_iRows;
		int iCols = m_iCols;
		if (bHaveImage) {
			iRows = m_iRowsWithImage;
			iCols = m_iColsWithImage;
		}

		m_oLookupButtonsPanel.setConfig(iRows, iCols, m_iButtonDescFontSize);
		m_oLookupButtonsPanel.updateLookupButtons(oData, null);

		showPageButton();
		updateItemStockQty();
	}

	public void showPageButton() {
		int iCurrPage = m_oLookupButtonsPanel.getCurrentPage();
		int iPageCount = m_oLookupButtonsPanel.getPageCount();
		m_oLblPage.setValue(iCurrPage + "/" + iPageCount);

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

	// Show or hide Back button
	public void showBackButton(Boolean bShow) {
		m_oButtonBack.setVisible(bShow);
	}

	// Update Lookup Title Path
	public void updateLookupTitleBarNames() {
		String sFullPath = "";
		int counter = 0;

		if (m_oSubLookupInfoList.isEmpty())
			return;

		for (SubLookupInfo oSUbLookupInfo : m_oSubLookupInfoList) {
			sFullPath = sFullPath + oSUbLookupInfo.menuName;

			if (counter < m_oSubLookupInfoList.size() - 1)
				sFullPath = sFullPath + " > ";
			counter++;
		}

		m_oLblLookupPath.setValue(sFullPath);
	}

	// get button infos in current page and retrieve their new stock qty
	public void updateItemStockQty() {
		if (AppGlobal.g_bNotCheckStock)
			return;

		HashMap<Integer, Integer> oItemIdList = new HashMap<Integer, Integer>();
		List<FuncLookupButtonInfo> oLookupButtonInfos = m_oLookupButtonsPanel.getCurrentLookupButtonInfos();
		for (int i = 0; i < oLookupButtonInfos.size(); i++) {
			FuncLookupButtonInfo oLookupButtonInfo = oLookupButtonInfos.get(i);
			if (!oLookupButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_HOT_ITEM))
				continue;

			oItemIdList.put(i, oLookupButtonInfo.getId());
		}

		for (FrameOrderingMenuLookupListener listener : listeners) {
			listener.frameOrderingMenuLookup_updateButtonsStockQty(oItemIdList);
		}
	}

	public void setItemStockQty(int iItemIndex, BigDecimal dQty) {
		m_oLookupButtonsPanel.setItemStockQty(iItemIndex, dQty);
	}

	public void setItemStockQtyByItemId(int iItemId, BigDecimal dQty) {
		List<FuncLookupButtonInfo> oLookupButtonInfos = m_oLookupButtonsPanel.getCurrentLookupButtonInfos();
		for (int i = 0; i < oLookupButtonInfos.size(); i++) {
			FuncLookupButtonInfo oLookupButtonInfo = oLookupButtonInfos.get(i);
			if (oLookupButtonInfo.getId() == iItemId) {
				m_oLookupButtonsPanel.setItemStockQty(i, dQty);
				break;
			}
		}
	}

	public void addSubLookupInfo(String sMenuName, List<FuncLookupButtonInfo> oButtonInfoList, boolean bReset) {
		if (bReset) {// bReset = true, new menu lookup, need to clear current
						// lookup path
			m_oSubLookupInfoList.clear();
			showBackButton(false);
		}

		SubLookupInfo oSubLookupInfo = new SubLookupInfo(sMenuName, oButtonInfoList);
		m_oSubLookupInfoList.add(oSubLookupInfo);
		updateMenuLookupButtons(oSubLookupInfo.lookupButtonInfoList);
		updateLookupTitleBarNames();
	}

	public void changePage(boolean bLeft) {
		if (bLeft)
			m_oLookupButtonsPanel.prevPage();
		else
			m_oLookupButtonsPanel.nextPage();
		showPageButton();
		updateItemStockQty();
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (iChildId == m_oFraButtonClose.getId()) {
			FrameOrderingMenuLookupListener listener = listeners.get(0);
			listener.frameOrderingMenuLookup_functionClicked(sNote);
			bMatchChild = true;
		} else if (iChildId == m_oButtonBack.getId()) {
			m_oSubLookupInfoList.remove(m_oSubLookupInfoList.size() - 1);
			SubLookupInfo oSubLookupInfo = m_oSubLookupInfoList.get(m_oSubLookupInfoList.size() - 1);

			updateMenuLookupButtons(oSubLookupInfo.lookupButtonInfoList);
			updateLookupTitleBarNames();
			if (m_oSubLookupInfoList.size() == 1)
				showBackButton(false);
			bMatchChild = true;
		} else if (iChildId == m_oImgButtonPrevPage.getId()) {
			changePage(true);
			bMatchChild = true;
		} else if (iChildId == m_oImgButtonNextPage.getId()) {
			changePage(false);
			bMatchChild = true;
		}

		return bMatchChild;
	}

	@Override
	public boolean longClicked(int iChildId, String sNote) {
		// Same behavior as click
		return clicked(iChildId, sNote);
	}

	@Override
	public void frameCommonLookupButtons_addItem(String sNote) {
		FrameOrderingMenuLookupListener listener = listeners.get(0);
		listener.frameOrderingMenuLookup_lookupClicked(sNote);
	}

	@Override
	public void frameCommonLookupButtons_deleteItem(String sNote) {
	}

	@Override
	public void frameCommonLookupButtons_swipePage(boolean bLeft) {
		changePage(bLeft);
	}
}
