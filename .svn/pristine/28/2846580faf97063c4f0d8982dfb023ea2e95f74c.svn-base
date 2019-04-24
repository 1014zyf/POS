package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Map;
import java.util.Collections;
import org.json.JSONException;
import org.json.JSONObject;
import om.PosDisplayPanelLookup;
import om.PosOutletItem;
import om.PosOutletItemList;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIHorizontalList;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameSetMenuListener {
	void frameSetMenuLookup_ButtonClicked(String sNote);
	void frameSetMenuLookup_itemClicked(String sNote, int iParentTabIndex);
	void frameSetMenuLookup_selectedItemClicked(int iSelectedItemIndex);
	void frameSetMenuLookup_updateButtonsStockQty(HashMap<Integer, Integer> oItemIdList);
}

public class FrameSetMenu extends VirtualUIFrame implements FrameCommonLookupButtonsListener, FrameCommonPageContainerListener {
	private TemplateBuilder m_oTemplateBuilder;
	private int m_iSelectedPanelLookupTab;
	private int m_iOldChildItemSize;

	private AppGlobal.OPERATION_MODE m_sOperationMode;
	private List<Integer> m_oDisplayedLookupList; // Order of Displaying Panel

	private VirtualUIFrame m_oFrameSetMenuHeader = null;
	private VirtualUIImage m_oImageItem = null;
	private VirtualUILabel m_oLabelItemDesc = null;
	private VirtualUIFrame m_oFraButtonClose = null;
	private VirtualUIHorizontalList m_oHorizontalSelectedItemList;
	
	private FrameCommonLookupButtons m_oLookupButtonsPanel;
	
	private VirtualUILabel m_oLabelMin = null;
	private VirtualUILabel m_oLabelMax = null;
	private VirtualUILabel m_oLabelSelected = null;
	private VirtualUILabel m_oLabelMustChoose = null;
	private VirtualUILabel m_oLabelSelect = null;
	
	private VirtualUIButton m_oButtonBack = null;
	private VirtualUIButton m_oButtonOK = null;
	private VirtualUIFrame m_oFrameError = null;
	private VirtualUIImage m_oImageError = null;
	private VirtualUILabel m_oLabelErrorDesc = null;
	
	VirtualUIFrame m_oFramePageContainer;
	FrameCommonPageContainer m_oFrameCommonPageContainer;
	
	private VirtualUIFrame m_oFramePage = null;
	private VirtualUILabel m_oLblPage = null;
	private VirtualUIImage m_oImgButtonPrevPage = null;
	private VirtualUIImage m_oImgButtonNextPage = null;
	private VirtualUIFrame m_oFrameButtonPrevPage;
	private VirtualUIFrame m_oFrameButtonNextPage;

	private List<VirtualUIFrame> m_oFrameLookupTabArray;
	private List<VirtualUIFrame> m_oSelectedItemList;
	private List<VirtualUIFrame> m_oPreDefinedItemList;
	
	// Used for stock operation & item soldout function
	private List<Integer> m_oTempSelectedItemList;
	private boolean m_bFromStockOperation;
	private PosOutletItemList m_oItemStockList;		// Current Item stock list
	
	private HashMap<Integer, List<FuncLookupButtonInfo>> m_oPanelLookupButtonInfoList;		// lookup button info list of different menu id

	private class SetMenuPanelInfo {
		private int seq;
		private int menuId;
		private String menuName;
		private int priceLevel;
		private int maxOrder;
		private int minOrder;
		private List<FuncLookupButtonInfo> selectedLookupButtonListInfoList;

		SetMenuPanelInfo(int iSeq, int iMenuId, String sMenuName, int iPriceLevel, int iMaxOrder, int iMinOrder) {
			seq = iSeq;
			menuId = iMenuId;
			menuName = sMenuName;
			priceLevel = iPriceLevel;
			maxOrder = iMaxOrder;
			minOrder = iMinOrder;
			selectedLookupButtonListInfoList = new ArrayList<FuncLookupButtonInfo>();
		}
	}

	private List<SetMenuPanelInfo> m_oPanelInfoList; // List of panel info

	private class SelectedItemInfo {
		private int setMenuLookupSeq;		// The seq of the Set menu lookup
		private int panelTab;				// parent tab index
		private int menuId;					// menu	id
		private int itemIndex;				// The item index in the page
		private int itemId;					// the menu id of the item
		private int itemBasketSeat;
		private int itemBasketSeq;
		private boolean oldSelectedItem;

		SelectedItemInfo(int iSetMenuLookupSeq, int iPanelTab, int iMenuId, int iItemIndex, int iItemId, boolean bOldSelectedItem, int iBasketSeat, int iBasketSeq) {
			setMenuLookupSeq = iSetMenuLookupSeq;
			panelTab = iPanelTab;
			menuId = iMenuId;
			itemIndex = iItemIndex;
			itemId = iItemId;
			itemBasketSeat = iBasketSeat;
			itemBasketSeq = iBasketSeq;
			oldSelectedItem = bOldSelectedItem;
		}
	}

	// List of selected item info
	private List<SelectedItemInfo> m_oSelectedItemInfoList;
	
	// List of removed old item info
	private List<FuncCheckItem> m_oRemovedOldItemList;

	private boolean m_bEditSetMenu = false;

	private int m_iRows;
	private int m_iCols;
	private int m_iRowsWithImage;
	private int m_iColsWithImage;
	private int m_iButtonDescFontSize;
	
	public static String SET_MENU_PANEL_MENU_ID = "id";
	public static String SET_MENU_PANEL_NAME = "name";
	public static String SET_MENU_PRICE_LEVEL = "priceLevel";
	public static String SET_MENU_PANEL_DATA_LIST = "dataList";
	public static String SET_MENU_PANEL_MAX = "max";
	public static String SET_MENU_PANEL_MIN = "min";
	public static String SET_MENU_PANEL_SEQ = "seq";

	public static String SET_MENU_FROM_ORDERING = "o";
	public static String SET_MENU_FROM_SOLDOUT = "s";
	public static String SET_MENU_FROM_OPERATION = "p";
	public static final int ROW_NUM = 3;
	public static final int COL_NUM = 4;
	public static final int COL_NUM_FOR_MOBILE = 3;
	public static final int ROW_NUM_FOR_MOBILE = 3;
	public static final int ROW_NUM_WITH_IMAGE = 2;
	public static final int COLUMN_NUM_WITH_IMAGE = 6;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameSetMenuListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameSetMenuListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameSetMenuListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	// constructor
	public FrameSetMenu(boolean bEditSetMenu) {
		
		m_bEditSetMenu = bEditSetMenu;
		
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameSetMenuListener>();
		
		m_sOperationMode = AppGlobal.OPERATION_MODE.fine_dining;
		m_oDisplayedLookupList = new ArrayList<Integer>();
		
		m_oFrameLookupTabArray = new ArrayList<VirtualUIFrame>();
		m_oSelectedItemList = new ArrayList<VirtualUIFrame>();
		m_oPreDefinedItemList = new ArrayList<VirtualUIFrame>();
		m_oPanelLookupButtonInfoList = new HashMap<Integer, List<FuncLookupButtonInfo>>();
		
		m_iSelectedPanelLookupTab = -1;
		m_iOldChildItemSize = 0;
		
		m_oPanelInfoList = new ArrayList<SetMenuPanelInfo>();
		m_oSelectedItemInfoList = new ArrayList<SelectedItemInfo>();
		m_oRemovedOldItemList = new ArrayList<FuncCheckItem>();
		
		m_oTempSelectedItemList = new ArrayList<Integer>();
		m_oItemStockList = new PosOutletItemList();
		m_bFromStockOperation = false;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraSetMenu.xml");
		
		// Item image
		m_oImageItem = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageItem, "imgItem");
		
		// Item description label
		m_oLabelItemDesc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelItemDesc, "lblItemDesc");
		this.attachChild(m_oLabelItemDesc);
		
		// Create Close button
		m_oFraButtonClose = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFraButtonClose, "fraButClose");
		VirtualUIImage oImage = new VirtualUIImage();
		oImage.setWidth(m_oFraButtonClose.getWidth());
		oImage.setHeight(m_oFraButtonClose.getHeight());
		oImage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_esc_200-200.png");
		m_oFraButtonClose.attachChild(oImage);
		
		m_oFraButtonClose.allowClick(true);
		m_oFraButtonClose.setClickServerRequestNote("butClose");
		m_oFraButtonClose.setClickServerRequestBlockUI(false);
		
		VirtualUIFrame oFrameTitleSeparator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameTitleSeparator, "fraTitleSeparator");
		
		// SetMenu selected item list Horizontal List
		m_oHorizontalSelectedItemList = new VirtualUIHorizontalList();
		m_oTemplateBuilder.buildHorizontalList(m_oHorizontalSelectedItemList, "horListSelectedItem");
		this.attachChild(m_oHorizontalSelectedItemList);
		
		m_oLabelSelect = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSelect, "lblSelected");
		m_oLabelSelect.setValue(AppGlobal.g_oLang.get()._("selected")+" :");
		
		VirtualUIFrame oFrameSelectedListSeparator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameSelectedListSeparator, "fraSelectedListSeparator");
		
		m_oFrameSetMenuHeader = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameSetMenuHeader, "fraSetMenuHeader");
		m_oFrameSetMenuHeader.attachChild(m_oImageItem);
		m_oFrameSetMenuHeader.attachChild(m_oLabelSelect);
		m_oLabelSelect.setVisible(false);
		
		m_oFrameSetMenuHeader.attachChild(m_oLabelItemDesc);
		m_oFrameSetMenuHeader.attachChild(m_oFraButtonClose);
		m_oFrameSetMenuHeader.attachChild(oFrameTitleSeparator);
		m_oFrameSetMenuHeader.attachChild(m_oHorizontalSelectedItemList);
		m_oFrameSetMenuHeader.attachChild(oFrameSelectedListSeparator);
		this.attachChild(m_oFrameSetMenuHeader);
		
		// Lookup panel Min Indicator
		m_oLabelMin = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMin, "lblMin");
		
		// Lookup panel Max Indicator
		m_oLabelMax = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMax, "lblMax");
		
		// Lookup panel Selected Indicator
		m_oLabelSelected = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSelected, "lblSelectedAmount");
		
		// Lookup panel Must Choose Indicator
		m_oLabelMustChoose = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMustChoose, "lblMustChoose");
		m_oLabelMustChoose.setValue("*: " + AppGlobal.g_oLang.get()._("must_choose"));
		
		// Lookup panel BACK label
		m_oButtonBack = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonBack, "btnBack");
		m_oButtonBack.setValue(AppGlobal.g_oLang.get()._("back"));
		this.attachChild(m_oButtonBack);
		this.setBackButtonVisible(false);
		
		// OK button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "btnOK");
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("ok"));
		this.attachChild(m_oButtonOK);
		
		VirtualUIFrame oFramePanelLookupSeparator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFramePanelLookupSeparator, "fraLookupTabSeparator");
		oFramePanelLookupSeparator.setEnabled(false);
		this.attachChild(oFramePanelLookupSeparator);
		
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
		
		// Create Page label
		m_oFramePage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePage, "fraPageButton");
		
		// Create prev page button
		m_oFrameButtonPrevPage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameButtonPrevPage, "fraPrevPage");
		m_oFrameButtonPrevPage.allowClick(true);
		m_oFrameButtonPrevPage.setClickServerRequestBlockUI(false);
		m_oFrameButtonPrevPage.allowLongClick(true);
		m_oFrameButtonPrevPage.setLongClickServerRequestBlockUI(false);
		m_oFramePage.attachChild(m_oFrameButtonPrevPage);

		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevPage");
		m_oImgButtonPrevPage
				.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
		m_oFrameButtonPrevPage.attachChild(m_oImgButtonPrevPage);

		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oFramePage.attachChild(m_oLblPage);

		// Create next page button
		m_oFrameButtonNextPage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameButtonNextPage, "fraNextPage");
		m_oFrameButtonNextPage.allowClick(true);
		m_oFrameButtonNextPage.setClickServerRequestBlockUI(false);
		m_oFrameButtonNextPage.allowLongClick(true);
		m_oFrameButtonNextPage.setLongClickServerRequestBlockUI(false);
		m_oFramePage.attachChild(m_oFrameButtonNextPage);

		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage
				.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
		m_oFrameButtonNextPage.attachChild(m_oImgButtonNextPage);

		this.attachChild(m_oFramePage);
		
		// Set Menu Panel
		m_oLookupButtonsPanel = new FrameCommonLookupButtons();
		m_oTemplateBuilder.buildFrame(m_oLookupButtonsPanel, "scrfraCommonLookups");
		m_oLookupButtonsPanel.init();
		
		// Default Button number
		m_iRows = FrameSetMenu.ROW_NUM;
		m_iCols = FrameSetMenu.COL_NUM;
		m_iRowsWithImage = FrameSetMenu.ROW_NUM_WITH_IMAGE;
		m_iColsWithImage = FrameSetMenu.COLUMN_NUM_WITH_IMAGE;
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_iRows = FrameSetMenu.ROW_NUM_FOR_MOBILE;
			m_iCols = FrameSetMenu.COL_NUM_FOR_MOBILE;
			m_iRowsWithImage = FrameSetMenu.ROW_NUM_FOR_MOBILE;
			m_iColsWithImage = FrameSetMenu.COL_NUM_FOR_MOBILE;
		}
		m_iButtonDescFontSize = FrameLookupButton.BUTTON_DESC_FONT_SIZE;
		m_oLookupButtonsPanel.setConfig(m_iRows, m_iCols, m_iButtonDescFontSize);
		m_oLookupButtonsPanel.addListener(this);
		
		m_oFramePageContainer = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePageContainer, "fraPageContainer");
		m_oFramePageContainer.attachChild(m_oLookupButtonsPanel);
		m_oFramePageContainer.attachChild(m_oLabelMin);
		m_oFramePageContainer.attachChild(m_oLabelMax);
		m_oFramePageContainer.attachChild(m_oLabelSelected);
		m_oFramePageContainer.attachChild(m_oLabelMustChoose);
		
		m_oFrameCommonPageContainer = new FrameCommonPageContainer();
		m_oTemplateBuilder.buildFrame(m_oFrameCommonPageContainer, "scrfraSetMenuLookupPanel");
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.horizontal_desktop.name()))
			m_oFrameCommonPageContainer.init(m_oFrameCommonPageContainer.getWidth(),m_oFrameCommonPageContainer.getHeight(), 160, 56, 5, "#0055B8", "#999999", "", "", 212, true, true);
		else
			m_oFrameCommonPageContainer.init(m_oFrameCommonPageContainer.getWidth(),m_oFrameCommonPageContainer.getHeight(), 80, 60, 4, "#015384", "#66a1c1", "", "", 0, false, true);
		m_oFrameCommonPageContainer.setUpperlineColor("#DDDDDD");
		m_oFrameCommonPageContainer.setUnderlineColor("#DDDDDD");
		m_oFrameCommonPageContainer.addListener(this);
		this.attachChild(m_oFrameCommonPageContainer);
	}

	public void setConfig(int iRows, int iCols, int iRowsWithImage, int iColsWithImage, int iButtonDescFontSize) {
		m_iRows = iRows;
		m_iCols = iCols;
		m_iRowsWithImage = iRowsWithImage;
		m_iColsWithImage = iColsWithImage;
		m_iButtonDescFontSize = iButtonDescFontSize;
	}

	public void showPageButton() {
		int iCurrPage = m_oLookupButtonsPanel.getCurrentPage();
		int iPageCount = m_oLookupButtonsPanel.getPageCount();
		m_oLblPage.setValue(iCurrPage + "/" + iPageCount);

		if (iPageCount > 1) {
			m_oFramePage.setVisible(true);
			if (iCurrPage > 1)
				m_oImgButtonPrevPage
					.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
			else
				m_oImgButtonPrevPage
					.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
			
			if (iCurrPage < iPageCount)
				m_oImgButtonNextPage
					.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
			else
				m_oImgButtonNextPage
					.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
		} else {
			m_oFramePage.setVisible(false);
		}
	}
	
	public void setSetMenuImage(String sIconImageSource) {
		if(sIconImageSource!= null)
			m_oImageItem.setSource(sIconImageSource);
		else{
			m_oImageItem.setVisible(false);
			m_oLabelSelect.setVisible(true);
		}
	}

	public void setMaxMinSelectedQty(int iMinQty, int iMaxQty, int iSelectedQty) {
		m_oLabelMin.setValue(AppGlobal.g_oLang.get()._("min") + ": " + iMinQty);
		m_oLabelMax.setValue(AppGlobal.g_oLang.get()._("max") + ": " + iMaxQty);
		m_oLabelSelected.setValue(AppGlobal.g_oLang.get()._("selected") + ": " + iSelectedQty);
	}

	private int checkSetMenuMinSelect() {
		for (int i = 0; i < m_oPanelInfoList.size(); i++) {
			SetMenuPanelInfo oPanelInfo = m_oPanelInfoList.get(i);
			int iMinOrder = oPanelInfo.minOrder;

			int iSelectedOrder = 0;
			for (SelectedItemInfo oSelectedItemInfo : m_oSelectedItemInfoList) {
				if (oSelectedItemInfo.panelTab == i)
					iSelectedOrder++;
			}

			if (iMinOrder > 0 && iSelectedOrder < iMinOrder)
				return i;
		}

		return -1;
	}
	
	public void createTabsAndLookupButtons(List<HashMap<String, Object>> oLookupArray) {
		int iRows = m_iRows;
		int iCols = m_iCols;
		// Check whether the items contain image or not
		lFindItemImage:
		for (HashMap<String, Object> oLookup : oLookupArray) {
			List<FuncLookupButtonInfo> oLookupContentList = (List<FuncLookupButtonInfo>) oLookup
					.get(FrameSetMenu.SET_MENU_PANEL_DATA_LIST);
			for(FuncLookupButtonInfo oButtonInfo: oLookupContentList) {
				if (oButtonInfo.getImage() != null && !oButtonInfo.getImage().isEmpty()) {
					iRows = m_iRowsWithImage;
					iCols = m_iColsWithImage;
					break lFindItemImage;
				}
			}
		}
		m_oLookupButtonsPanel.setConfig(iRows, iCols, m_iButtonDescFontSize);
		
		for (HashMap<String, Object> oLookup : oLookupArray) {
			int iMenuId = (int) oLookup.get(FrameSetMenu.SET_MENU_PANEL_MENU_ID);
			String sName = (String) oLookup.get(FrameSetMenu.SET_MENU_PANEL_NAME);
			int iSeq = (int) oLookup.get(FrameSetMenu.SET_MENU_PANEL_SEQ);
			
			int iMaxOrder = (int) oLookup.get(FrameSetMenu.SET_MENU_PANEL_MAX);
			int iMinOrder = (int) oLookup.get(FrameSetMenu.SET_MENU_PANEL_MIN);
			int iPriceLevel = (int) oLookup.get(FrameSetMenu.SET_MENU_PRICE_LEVEL);
			List<FuncLookupButtonInfo> oLookupContentList = (List<FuncLookupButtonInfo>) oLookup
					.get(FrameSetMenu.SET_MENU_PANEL_DATA_LIST);
			if (iMinOrder > 0 && !m_bFromStockOperation)
				sName = "*"+sName;
			addSetMenuLookupPanelButtons(iMenuId, oLookupContentList);
			
			SetMenuPanelInfo oSetMenuInfo = new SetMenuPanelInfo(iSeq, iMenuId, sName, iPriceLevel, iMaxOrder, iMinOrder);
			m_oPanelInfoList.add(oSetMenuInfo);
			
			if (oLookupArray.indexOf(oLookup) == 0) {
				this.setMaxMinSelectedQty(iMinOrder, iMaxOrder, 0);
				m_oLookupButtonsPanel.updateLookupButtons(oLookupContentList, null);
				m_oFrameCommonPageContainer.addButton(sName, m_oFramePageContainer);
			} else
				m_oFrameCommonPageContainer.addButton(sName, null);
		}
		
		if (!oLookupArray.isEmpty())
			changeSelectedLookupTab(0);
	}

	public void addSetMenuLookupPanelButtons(int iSetMenuLUId, List<FuncLookupButtonInfo> oData) {
		for (FuncLookupButtonInfo oLookupButtonInfo : oData) {
			if (!oLookupButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_MENU_LOOKUP)) {
				oLookupButtonInfo.setAddMinusBtn(true);
				oLookupButtonInfo.setPriceInLeftTopCorner(true);
				oLookupButtonInfo.setStockQtyInRightTopCorner(true);
			}
		}
		m_oPanelLookupButtonInfoList.put(iSetMenuLUId, oData);
	}

	public void showErrorFrame(String sErrorMsg) {
		m_oLabelErrorDesc.setValue(sErrorMsg);
		m_oFrameError.setVisible(true);
	}

	public void setItemDescription(String sItemDesc) {
		m_oLabelItemDesc.setValue(sItemDesc);
	}
	
	public void setBackButtonVisible(boolean bVisible) {
		m_oButtonBack.setVisible(bVisible);
	}

	public boolean changeSelectedLookupTab(int iSelectedLookupTab) {
		if (iSelectedLookupTab >= m_oPanelInfoList.size()) // Page not found
			return false;
		
		int iMenuId = m_oPanelInfoList.get(iSelectedLookupTab).menuId;
		List<FuncLookupButtonInfo> oData = m_oPanelLookupButtonInfoList.get(iMenuId);
		
		// Create different panel
		m_oLookupButtonsPanel.updateLookupButtons(oData, null);

		m_iSelectedPanelLookupTab = iSelectedLookupTab;

		SetMenuPanelInfo oSetMenuPanelInfo = m_oPanelInfoList.get(m_iSelectedPanelLookupTab);
		setMaxMinSelectedQty(oSetMenuPanelInfo.minOrder, oSetMenuPanelInfo.maxOrder,
				oSetMenuPanelInfo.selectedLookupButtonListInfoList.size());

		m_oDisplayedLookupList.clear();
		m_oDisplayedLookupList.add(iMenuId);

		updatePageButtonsAndItemQuantities();

		return true;
	}

	// scan m_oSelectedItemInfoList to count selected item and update button
	// quantity label
	public void updateSetMenuLookupPanelButtonItemQuantities() {
		HashMap<Integer, Integer> oItemQuantityList = new HashMap<Integer, Integer>();
		for (SelectedItemInfo oSelectedItemInfo : m_oSelectedItemInfoList) {
			// Tab index or sub menu look id not match
			if (oSelectedItemInfo.panelTab != -1 && oSelectedItemInfo.panelTab == m_iSelectedPanelLookupTab
					&& oSelectedItemInfo.menuId == m_oDisplayedLookupList.get(m_oDisplayedLookupList.size() - 1)) {
				// Item is not in current page
				if (oSelectedItemInfo.itemIndex < m_oLookupButtonsPanel.getCurrentStartIndex()
						|| oSelectedItemInfo.itemIndex > m_oLookupButtonsPanel.getCurrentStartIndex()
								+ m_oLookupButtonsPanel.getNumberOfButtons())
					continue;

				int iButtonIndex = oSelectedItemInfo.itemIndex - m_oLookupButtonsPanel.getCurrentStartIndex();
				if (oItemQuantityList.containsKey(iButtonIndex))
					oItemQuantityList.put(iButtonIndex, oItemQuantityList.get(iButtonIndex) + 1);
				else
					oItemQuantityList.put(iButtonIndex, 1);
			}
		}

		for (Entry<Integer, Integer> entry : oItemQuantityList.entrySet())
			m_oLookupButtonsPanel.setItemQuantity(entry.getKey(), Integer.toString(entry.getValue()));
	}

	public void addSelectedItemToList(int iSeq, String[] sItemName, int iItemIndex, int iItemId, boolean allowClick, boolean bOldSelectedItem, int iBasketSeat, int iBasketSeq, int iSelectedPanelLookupTab) {
		VirtualUIFrame oFrameSelectedItem = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameSelectedItem, "fraSelectedItem");
		oFrameSelectedItem.allowClick(allowClick);
		oFrameSelectedItem.setClickServerRequestBlockUI(false);
		
		VirtualUIImage oImageDeleteSelectedItem = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(oImageDeleteSelectedItem, "imgDeleteSelectedItem");
		oImageDeleteSelectedItem.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_btn_delete.png");
		if (!allowClick)
			oImageDeleteSelectedItem.setVisible(false);
		oFrameSelectedItem.attachChild(oImageDeleteSelectedItem);
		
		VirtualUILabel oLabelSelectedItem = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelSelectedItem, "lblSelectedItem");
		oLabelSelectedItem.setLeft(oFrameSelectedItem.getWidth() - oImageDeleteSelectedItem.getLeft());
		oLabelSelectedItem.setWidth(oFrameSelectedItem.getWidth() - (oLabelSelectedItem.getLeft() * 2));
		oLabelSelectedItem.setHeight(oFrameSelectedItem.getHeight());
		oLabelSelectedItem.setValue(sItemName);
		oFrameSelectedItem.attachChild(oLabelSelectedItem);
		
		int iMenuId = 0;
		if (!m_oDisplayedLookupList.isEmpty())
			iMenuId = m_oDisplayedLookupList.get(m_oDisplayedLookupList.size() - 1);
		try {
			JSONObject oJSONObject = new JSONObject();
			oJSONObject.put(FrameLookupButton.BUTTON_NOTE_ID, iItemId);
			oJSONObject.put(FrameLookupButton.BUTTON_NOTE_SEQ, iItemIndex);
			oJSONObject.put(FrameLookupButton.BUTTON_NOTE_MENU_ID, iMenuId);
			oFrameSelectedItem.setClickServerRequestNote(oJSONObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if(!allowClick) {
			m_oPreDefinedItemList.add(oFrameSelectedItem);
			iSelectedPanelLookupTab = -1;
		}
		m_oSelectedItemList.add(oFrameSelectedItem);
		m_oHorizontalSelectedItemList.attachChild(oFrameSelectedItem);
		
		SelectedItemInfo oSelectedItemInfo = new SelectedItemInfo(iSeq, iSelectedPanelLookupTab, iMenuId, iItemIndex,
				iItemId, bOldSelectedItem, iBasketSeat, iBasketSeq);
		m_oSelectedItemInfoList.add(oSelectedItemInfo);
		m_oHorizontalSelectedItemList.scrollToIndex(m_oSelectedItemInfoList.size() - 1);
		resequenceHorizontalSelectedItemList();
	}
	
	public void resequenceHorizontalSelectedItemList() {
		ArrayList<VirtualUIFrame> oTempSelectedItemList = new ArrayList<VirtualUIFrame>();
		for(int i = 0 ; i < m_oPanelInfoList.size() ; i++) {
			for(int j = 0 ; j < m_oSelectedItemInfoList.size() ; j++) {
				if(m_oSelectedItemInfoList.get(j).panelTab == i) {
					oTempSelectedItemList.add(m_oSelectedItemList.get(j));
				}
			}
		}
		
		m_oHorizontalSelectedItemList.removeAllChildren();
		for(VirtualUIFrame oVirtualUIFrame : m_oPreDefinedItemList)
			m_oHorizontalSelectedItemList.attachChild(oVirtualUIFrame);
		for(VirtualUIFrame oVirtualUIFrame : oTempSelectedItemList)
			m_oHorizontalSelectedItemList.attachChild(oVirtualUIFrame);
	}

	public void showSubMenuPanel(int iSubMenuId) {
		List<FuncLookupButtonInfo> oData = m_oPanelLookupButtonInfoList.get(iSubMenuId);
		m_oLookupButtonsPanel.updateLookupButtons(oData, null);
		m_oDisplayedLookupList.add(iSubMenuId);
		updatePageButtonsAndItemQuantities();
		setBackButtonVisible(true);
	}

	public boolean containSubMenu(int iMenuId) {
		return m_oPanelLookupButtonInfoList.containsKey(iMenuId);
	}

	public void removeTopMenuPanel() {
		if (m_oDisplayedLookupList.size() > 1) {
			m_oDisplayedLookupList.remove(m_oDisplayedLookupList.size() - 1);

			// Update lookup button
			int iSubMenuId = m_oDisplayedLookupList.get(m_oDisplayedLookupList.size() - 1);
			List<FuncLookupButtonInfo> oData = m_oPanelLookupButtonInfoList.get(iSubMenuId);
			m_oLookupButtonsPanel.updateLookupButtons(oData, null);
			updatePageButtonsAndItemQuantities();
		}

		// back to the top page, remove "Back" button
		if (m_oDisplayedLookupList.size() == 1)
			setBackButtonVisible(false);
	}

	public void updatePageButtonsAndItemQuantities() {
		updateSetMenuLookupPanelButtonItemQuantities();
		showPageButton();
		updateItemStockQty();
	}

	public void setSetMenuItemStockQty(int iItemIndex, BigDecimal dQty) {
		m_oLookupButtonsPanel.setItemStockQty(iItemIndex, dQty);
	}

	public void setSetMenuItemStockQtyByItemId(int iItemId, BigDecimal dQty) {
		List<FuncLookupButtonInfo> oLookupButtonInfos = m_oLookupButtonsPanel.getCurrentLookupButtonInfos();
		for (int i = 0; i < oLookupButtonInfos.size(); i++) {
			FuncLookupButtonInfo oLookupButtonInfo = oLookupButtonInfos.get(i);
			if (oLookupButtonInfo.getId() == iItemId) {
				m_oLookupButtonsPanel.setItemStockQty(i, dQty);
				break;
			}
		}
	}
	
	public void setOldChildCount(int iChildItemSize){
		m_iOldChildItemSize = iChildItemSize;
	}
	
	public int getOldChildCount(){
		return m_iOldChildItemSize;
	}
	
	// clear all the list data
	public void clearData() {
		m_oHorizontalSelectedItemList.removeAllChildren();
		m_oSelectedItemList.clear();
		m_oPreDefinedItemList.clear();
		
		m_oFrameLookupTabArray.clear();
		
		m_oSelectedItemInfoList.clear();
		m_oRemovedOldItemList.clear();
		m_oPanelInfoList.clear();
		m_oPanelLookupButtonInfoList.clear();
		
		m_iSelectedPanelLookupTab = -1;
		
		m_oDisplayedLookupList.clear();
	}
	
	public List<Integer> getSelectedItemSetMenuLookupSeqList() {
		List<Integer> oSelectedItemTabIndexList = new ArrayList<Integer>();
		for (SelectedItemInfo oSelectedItemInfo : m_oSelectedItemInfoList)
			oSelectedItemTabIndexList.add(oSelectedItemInfo.setMenuLookupSeq);
		
		return oSelectedItemTabIndexList;
	}
	
	// get remove old item list
	public List<FuncCheckItem> getRemovedItemSetMenuLookupSeqList() {
		List<FuncCheckItem> oRemovedOldItemList = new ArrayList<FuncCheckItem>();
		HashMap<Integer, Map<Integer, FuncCheckItem>> oRemovedOldItemListBySeatSeq = new HashMap<Integer, Map<Integer, FuncCheckItem>>();
		for(FuncCheckItem oRemovedOldItem: m_oRemovedOldItemList){
			Map<Integer, FuncCheckItem> oRemovedOldItemListBySeq = new TreeMap(Collections.reverseOrder());
			if(oRemovedOldItemListBySeatSeq.containsKey(oRemovedOldItem.getCheckItem().getSeatNo()))
				oRemovedOldItemListBySeq = oRemovedOldItemListBySeatSeq.get(oRemovedOldItem.getCheckItem().getSeatNo());
			
			oRemovedOldItemListBySeq.put(oRemovedOldItem.getCheckItem().getSeq(), oRemovedOldItem);
			oRemovedOldItemListBySeatSeq.put(oRemovedOldItem.getCheckItem().getSeatNo(), oRemovedOldItemListBySeq);
		}
		for(Entry<Integer, Map<Integer, FuncCheckItem>> oEntryBySeatSeq : oRemovedOldItemListBySeatSeq.entrySet()){
			for(Map.Entry<Integer, FuncCheckItem> oRemovedOldItem : oEntryBySeatSeq.getValue().entrySet())
				oRemovedOldItemList.add(oRemovedOldItem.getValue());
		}
		
		return oRemovedOldItemList;
	}
	
	public void addRemovedItemSetMenuLookupSeqList(FuncCheckItem oChildItem) {
		m_oRemovedOldItemList.add(oChildItem);
	}

	public int getCurrentTabPriceLevel() {
		return m_oPanelInfoList.get(m_iSelectedPanelLookupTab).priceLevel;
	}
	
	public void removeSelectedItemByBasketSeatAndSeq(int iBasketSeat, int iBasketSeq){
		// it will not be -1 value when it is an new set menu item
		if(iBasketSeat < 0 || iBasketSeq < 0)
			return;
		
		int iIndex = 0;
		SelectedItemInfo oSelectedItemInfo = null;
		for(SelectedItemInfo oItemInfo : m_oSelectedItemInfoList){
			if((oItemInfo.itemBasketSeat == iBasketSeat) &&
					(oItemInfo.itemBasketSeq == iBasketSeq)){
				oSelectedItemInfo = m_oSelectedItemInfoList.get(iIndex);
				break;
			}
			iIndex ++;
		}
		
		if(oSelectedItemInfo == null)
			return;
		
		if (oSelectedItemInfo.panelTab == m_iSelectedPanelLookupTab
				&& oSelectedItemInfo.menuId == m_oDisplayedLookupList.get(m_oDisplayedLookupList.size() - 1)
				&& oSelectedItemInfo.itemIndex >= m_oLookupButtonsPanel.getCurrentStartIndex()
				&& oSelectedItemInfo.itemIndex <= m_oLookupButtonsPanel.getCurrentStartIndex()
						+ m_oLookupButtonsPanel.getNumberOfButtons()) {
			int iButtonIndex = oSelectedItemInfo.itemIndex - m_oLookupButtonsPanel.getCurrentStartIndex();
			int iQty = Integer.parseInt(m_oLookupButtonsPanel.getItemQunantity(iButtonIndex));
			m_oLookupButtonsPanel.setItemQuantity(iButtonIndex, Integer.toString(iQty - 1));
		}
		
		removeHorizontalSelectedItemListItem(iIndex);
		removeSelectedItemInfo(oSelectedItemInfo.panelTab, oSelectedItemInfo.itemId);
	}

	public void removeSelectedItem(int iIndex) {
		SelectedItemInfo oSelectedItemInfo = m_oSelectedItemInfoList.get(iIndex);
		if (oSelectedItemInfo.panelTab == m_iSelectedPanelLookupTab
				&& oSelectedItemInfo.menuId == m_oDisplayedLookupList.get(m_oDisplayedLookupList.size() - 1)
				&& oSelectedItemInfo.itemIndex >= m_oLookupButtonsPanel.getCurrentStartIndex()
				&& oSelectedItemInfo.itemIndex <= m_oLookupButtonsPanel.getCurrentStartIndex()
						+ m_oLookupButtonsPanel.getNumberOfButtons()) {
			int iButtonIndex = oSelectedItemInfo.itemIndex - m_oLookupButtonsPanel.getCurrentStartIndex();
			int iQty = Integer.parseInt(m_oLookupButtonsPanel.getItemQunantity(iButtonIndex));
			m_oLookupButtonsPanel.setItemQuantity(iButtonIndex, Integer.toString(iQty - 1));
		}
		removeHorizontalSelectedItemListItem(iIndex);
		removeSelectedItemInfo(oSelectedItemInfo.panelTab, oSelectedItemInfo.itemId);
		
		// Go back to the tab of deleted item
		m_oFrameCommonPageContainer.clickTag(oSelectedItemInfo.panelTab);
	}
	
	public boolean isSelectedOldItem(int iParentTab, int iParentTbItemSeq, int iMenuId){
		for(SelectedItemInfo oSelectedItemInfo : m_oSelectedItemInfoList){
			if (oSelectedItemInfo.panelTab == iParentTab
					&& oSelectedItemInfo.menuId == iMenuId
					&& oSelectedItemInfo.itemIndex == iParentTbItemSeq) {
				return oSelectedItemInfo.oldSelectedItem;
			}
		}
		return false;
	}

	public void removeHorizontalSelectedItemListItem(int iSelectedItemIndex) {
		VirtualUIFrame oFrameRemoveItem = m_oSelectedItemList.get(iSelectedItemIndex);
		// remove the item from list
		m_oSelectedItemList.remove(iSelectedItemIndex);

		// remove the item from UI
		m_oHorizontalSelectedItemList.removeChild(oFrameRemoveItem.getId());
			
		m_oSelectedItemInfoList.remove(iSelectedItemIndex);
	}

	public void removeSelectedItemInfo(int iPanelTab, int iItemId) {
		SetMenuPanelInfo oSetMenuPanelInfo = m_oPanelInfoList.get(iPanelTab);
		for (int i = 0; i < oSetMenuPanelInfo.selectedLookupButtonListInfoList.size(); i++) {
			FuncLookupButtonInfo oLookupButtonInfo = oSetMenuPanelInfo.selectedLookupButtonListInfoList.get(i);
			if (oLookupButtonInfo.getId() == iItemId) {
				oSetMenuPanelInfo.selectedLookupButtonListInfoList.remove(i);
				break;
			}
		}

		if (iPanelTab == m_iSelectedPanelLookupTab)
			setMaxMinSelectedQty(oSetMenuPanelInfo.minOrder, oSetMenuPanelInfo.maxOrder,
					oSetMenuPanelInfo.selectedLookupButtonListInfoList.size());
	}

	public void addSelectedItem(String sNote) {
		try {
			JSONObject oJSONObject = new JSONObject(sNote);
			int iItemIndex = oJSONObject.optInt(FrameLookupButton.BUTTON_NOTE_SEQ, -1);
			
			List<FuncLookupButtonInfo> oData = m_oPanelLookupButtonInfoList
					.get(m_oDisplayedLookupList.get(m_oDisplayedLookupList.size() - 1));
			FuncLookupButtonInfo oSelectedButtonInfo = oData.get(iItemIndex);
			
			if (oSelectedButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_HOT_ITEM)) {
				SetMenuPanelInfo oSetMenuPanelInfo = m_oPanelInfoList.get(m_iSelectedPanelLookupTab);
				int iMaxOrder = oSetMenuPanelInfo.maxOrder;
				int iSelectedOrder = oSetMenuPanelInfo.selectedLookupButtonListInfoList.size();
				if (iMaxOrder != 0 && iSelectedOrder == iMaxOrder)
					return;
			}
			
			for (FrameSetMenuListener listener : listeners) {
				listener.frameSetMenuLookup_itemClicked(sNote, m_iSelectedPanelLookupTab);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void finishAddSelectedItem(int iItemIndex, int iItemId, String[] sItemName, boolean bOldSelectedItem, int iItemBasketSeat, int iItemBasketSeq, boolean bAllowClick) {
		SetMenuPanelInfo oSetMenuPanelInfo = m_oPanelInfoList.get(m_iSelectedPanelLookupTab);
		List<FuncLookupButtonInfo> oData = m_oPanelLookupButtonInfoList
				.get(m_oDisplayedLookupList.get(m_oDisplayedLookupList.size() - 1));
		
		// For replaced item
		if(iItemIndex < 0){
			m_oTempSelectedItemList.add(iItemId);
			addSelectedItemToList(oSetMenuPanelInfo.seq, sItemName, iItemIndex,
					iItemId, bAllowClick, bOldSelectedItem, iItemBasketSeat, iItemBasketSeq, m_iSelectedPanelLookupTab);
			return;
		}
		FuncLookupButtonInfo oSelectedButtonInfo = oData.get(iItemIndex);
		
		addSelectedItemToList(oSetMenuPanelInfo.seq, oSelectedButtonInfo.getName(), iItemIndex,
				oSelectedButtonInfo.getId(), bAllowClick, bOldSelectedItem, iItemBasketSeat, iItemBasketSeq, m_iSelectedPanelLookupTab);
		
		int iButtonIndex = iItemIndex - m_oLookupButtonsPanel.getCurrentStartIndex();
		int iNewQty = Integer.parseInt(m_oLookupButtonsPanel.getItemQunantity(iButtonIndex)) + 1;
		m_oLookupButtonsPanel.setItemQuantity(iButtonIndex, Integer.toString(iNewQty));
		
		oSetMenuPanelInfo.selectedLookupButtonListInfoList.add(oSelectedButtonInfo);
		setMaxMinSelectedQty(oSetMenuPanelInfo.minOrder, oSetMenuPanelInfo.maxOrder,
				oSetMenuPanelInfo.selectedLookupButtonListInfoList.size());
		
			// selected item count reach to max limit, move to next tab
		while (oSetMenuPanelInfo.maxOrder != 0
					&& oSetMenuPanelInfo.selectedLookupButtonListInfoList.size() == oSetMenuPanelInfo.maxOrder) {
				if (m_iSelectedPanelLookupTab + 1 < m_oPanelInfoList.size()) {
					m_oFrameCommonPageContainer.clickTag(m_iSelectedPanelLookupTab+1);
					oSetMenuPanelInfo = m_oPanelInfoList.get(m_iSelectedPanelLookupTab);
					setMaxMinSelectedQty(oSetMenuPanelInfo.minOrder, oSetMenuPanelInfo.maxOrder,
							oSetMenuPanelInfo.selectedLookupButtonListInfoList.size());
					setBackButtonVisible(false);
				} else {	// current tab is the last tab, system need to check whether previous tabs has minimum item counts
					// Check whether it has missing item in each tab which have
					// minimium order
					int iMissingItemTab = checkSetMenuMinSelect();
					if (iMissingItemTab >= 0) {
						//changeSelectedLookupTab(iMissingItemTab);
						m_oFrameCommonPageContainer.clickTag(iMissingItemTab);
						String sMenuName = m_oPanelInfoList.get(iMissingItemTab).menuName;
						String sSetMenuErrorMsg = AppGlobal.g_oLang.get()._("missing_item_at") + " " + sMenuName;
						showErrorFrame(sSetMenuErrorMsg);
						return;
					}

					// No next page = finish ordering
				if(!bOldSelectedItem) {
					for (FrameSetMenuListener listener : listeners) {
						// Raise the event to parent
						listener.frameSetMenuLookup_ButtonClicked("OK");
					}
				}
				break;
			}
		}

		m_oTempSelectedItemList.add(iItemId);
	}
	
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

		for (FrameSetMenuListener listener : listeners) {
			listener.frameSetMenuLookup_updateButtonsStockQty(oItemIdList);
		}
	}

	public void setOperationMode(AppGlobal.OPERATION_MODE sOperationMode) {
		this.m_sOperationMode = sOperationMode;
	}

	public AppGlobal.OPERATION_MODE getOperationMode() {
		return this.m_sOperationMode;
	}
	
	public boolean isFromStockoperation() {
		return m_bFromStockOperation;
	}
	
	public void setFromStockoperation(boolean bFromStockoperation) {
		this.m_bFromStockOperation = bFromStockoperation;
	}
	
	public PosOutletItemList getItemStockList() {
		return m_oItemStockList;
	}
	
	public void setItemStockList(List<PosOutletItem> oItemStockList) {
		this.m_oItemStockList.setOutletItemList(oItemStockList);
	}
	
	public List<Integer> getTempSelectedItemList() {
		return m_oTempSelectedItemList;
	}
	
	public void setMinMaxVisible(boolean bVisible){
		m_oLabelMin.setVisible(bVisible);
		m_oLabelMax.setVisible(bVisible);
		m_oLabelSelected.setVisible(bVisible);
		m_oLabelMustChoose.setVisible(bVisible);
	}
	
	public boolean isEditSetmenu(){
		return m_bEditSetMenu;
	}
	
	public List<Integer> getDisplayedLookupList(){
		return m_oDisplayedLookupList;
	}
	
	public void changeSelectedTagButton(int iIndex){
		m_oFrameCommonPageContainer.setTagButton(iIndex);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		m_oFrameError.setVisible(false);

		boolean bMatchChild = false;
		if (iChildId == m_oButtonOK.getId()) {
			// Check whether it has missing item in each tab which have minimum
			// order
			boolean bSuccess = true;
			if (!m_bFromStockOperation) {		// No need to check minimum and maximum order when calling set menu in "Stock Operation" & "Item Soldout" function
				int iMissingItemTab = checkSetMenuMinSelect();
				if (iMissingItemTab >= 0) {
					changeSelectedLookupTab(iMissingItemTab);
					//changeSelectedLookupTab(iMissingItemTab);
					m_oFrameCommonPageContainer.clickTag(iMissingItemTab);
					setBackButtonVisible(false);
					String sMenuName = m_oPanelInfoList.get(iMissingItemTab).menuName;
					String sSetMenuErrorMsg = AppGlobal.g_oLang.get()._("missing_item_at") + " " + sMenuName;
					showErrorFrame(sSetMenuErrorMsg);
					bSuccess = false;
				}
			}
			if(bSuccess) {
				for (FrameSetMenuListener listener : listeners) {
					listener.frameSetMenuLookup_ButtonClicked("OK");
				}
			}
			bMatchChild = true;
		} else if (iChildId == m_oFraButtonClose.getId()) {
			for (FrameSetMenuListener listener : listeners) {
				this.setVisible(false);
				listener.frameSetMenuLookup_ButtonClicked("Cancel");
			}
			bMatchChild = true;
		} else if (iChildId == m_oButtonBack.getId()) {
			removeTopMenuPanel();
			bMatchChild = true;
		} else if (iChildId == m_oFrameButtonPrevPage.getId()) {
			m_oLookupButtonsPanel.prevPage();
			updatePageButtonsAndItemQuantities();
			bMatchChild = true;
		} else if (iChildId == m_oFrameButtonNextPage.getId()) {
			m_oLookupButtonsPanel.nextPage();
			updatePageButtonsAndItemQuantities();
			bMatchChild = true;
		} else {
			// check whether tab is clicked
			for (int iCount = 0; iCount < m_oFrameLookupTabArray.size(); iCount++) {
				if (iChildId == m_oFrameLookupTabArray.get(iCount).getId()) {
					// for self-kiosk mode and flag is off, not allow user to
					// click the previous tab
					// and not allow to go to next tab if minimum requirement
					if (AppGlobal.OPERATION_MODE.self_order_kiosk.equals(m_sOperationMode)
							&& AppGlobal.g_oFuncStation.get().getSelfKioskSetMenuNoGuidance() == false) {
						if (iCount <= m_iSelectedPanelLookupTab)
							return false;
						else {
							int iMissingItemTab = checkSetMenuMinSelect();
							if (iMissingItemTab >= 0) {
								//changeSelectedLookupTab(iMissingItemTab);
								m_oFrameCommonPageContainer.clickTag(iMissingItemTab);
								String sMenuName = m_oPanelInfoList.get(iMissingItemTab).menuName;
								String sSetMenuErrorMsg = AppGlobal.g_oLang.get()._("missing_item_at") + " " + sMenuName;
								showErrorFrame(sSetMenuErrorMsg);
								return false;
							}
						}
					}

					//changeSelectedLookupTab(iCount);
					m_oFrameCommonPageContainer.clickTag(iCount);
					setBackButtonVisible(false);
					
					bMatchChild = true;
					break;
				}
			}

			// Not click tab, search selected item list
			if (!bMatchChild) {
				for (int i = 0; i < m_oSelectedItemList.size(); i++) {
					VirtualUIFrame oFrameSelectedItem = m_oSelectedItemList.get(i);
					if (iChildId == oFrameSelectedItem.getId()) {
						for (FrameSetMenuListener listener : listeners) {
							listener.frameSetMenuLookup_selectedItemClicked(i);
						}
						bMatchChild = true;
						break;
					}
				}
			}
		}

		return bMatchChild;
	}

	@Override
	public void frameCommonLookupButtons_addItem(String sNote) {
		addSelectedItem(sNote);
	}

	@Override
	public void frameCommonLookupButtons_deleteItem(String sNote) {
		JSONObject oJSONObject = new JSONObject();
		try {
			oJSONObject = new JSONObject(sNote);
	
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}

		for (int i = 0; i < m_oSelectedItemInfoList.size(); i++) {
			SelectedItemInfo oSelectedItemInfo = m_oSelectedItemInfoList.get(i);
			if (oSelectedItemInfo.panelTab == -1) // predefined item
				continue;

			// Tab index not match
			if (oSelectedItemInfo.panelTab != m_iSelectedPanelLookupTab)
				continue;

			// Sub lookup not match
			if (oSelectedItemInfo.menuId != m_oDisplayedLookupList.get(m_oDisplayedLookupList.size() - 1))
				continue;

			if (oSelectedItemInfo.itemIndex == oJSONObject.optInt(FrameLookupButton.BUTTON_NOTE_SEQ)) {
				for (FrameSetMenuListener listener : listeners) {
					listener.frameSetMenuLookup_selectedItemClicked(i);
				}
				break;
			}
		}
	}
	
	@Override
	public void frameCommonLookupButtons_swipePage(boolean bLeft) {
		if (bLeft)
			m_oLookupButtonsPanel.prevPage();
		else
			m_oLookupButtonsPanel.nextPage();
		updatePageButtonsAndItemQuantities();
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
		if (AppGlobal.OPERATION_MODE.self_order_kiosk.equals(m_sOperationMode)
				&& AppGlobal.g_oFuncStation.get().getSelfKioskSetMenuNoGuidance() == false) {
			if (iIndex < m_iSelectedPanelLookupTab) {
				showErrorFrame(AppGlobal.g_oLang.get()._("can_not_select_previous_self_selection_menu"));
				m_oFrameCommonPageContainer.clickTag(m_iSelectedPanelLookupTab);
				return;
			} else if(iIndex == m_iSelectedPanelLookupTab)
				return;
			else {
				int iMissingItemTab = checkSetMenuMinSelect();
				if (iMissingItemTab >= 0 && iMissingItemTab < iIndex) {
					changeSelectedLookupTab(iMissingItemTab);
					String sMenuName = m_oPanelInfoList.get(iMissingItemTab).menuName;
					String sSetMenuErrorMsg = AppGlobal.g_oLang.get()._("missing_item_at") + " "
							+ sMenuName;
					m_oFrameCommonPageContainer.clickTag(m_iSelectedPanelLookupTab);
					showErrorFrame(sSetMenuErrorMsg);
					return;
				}
			}
		}

		changeSelectedLookupTab(iIndex);
		setBackButtonVisible(false);
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
