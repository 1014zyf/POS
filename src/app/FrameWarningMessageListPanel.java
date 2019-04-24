package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;

import java.util.Map.Entry;

import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameWarningMessageListPanelListener {
	void frameWarningMessageListPanel_ButtonClickRefresh(String sPanelType, int iWarningMessageListingType);
}

public class FrameWarningMessageListPanel extends VirtualUIFrame implements FrameCommonBasketListener {
	
	//List Class
	class PrinterStatus {
		int iId = 0;
		String sPrinterName = "";
		String sPrinterHealth = "";
		String sPrinterStatus = "";
		
		String sPrinterMessage = "";
	}
	
	public static String TYPE_PRINTER_STATUS = "p";
	
	public static String COLUMN_HEADER_PRINT_QUEUE = "pq";
	public static String COLUMN_HEADER_MESSAGE = "m";
	
	public static String SORT_ORDER_NONE = "";
	public static String SORT_ORDER_ASCENDING = "a";
	public static String SORT_ORDER_DESCENDING = "d";
	
	//Message Status 
	public static String PRINTER_STATUS_ONLINE = "o";
	public static String PRINTER_STATUS_OFFLINE = "f";
	public static String PRINTER_STATUS_ALMOST_PAPER_OUT = "a";
	public static String PRINTER_STATUS_PAPER_OUT = "p";
	
	//Original List
	private HashMap<Integer, PrinterStatus> m_oPrinterStatusList;
	
	//DIsplay List
	private HashMap<Integer, PrinterStatus> m_oDisplayPrinterStatusList;
	
	//Message List
	public HashMap<String, String> m_oPrinterStatusMessageList;

	// UI element
	private TemplateBuilder m_oTemplateBuilder;
	private FrameCommonBasket m_oWarningMessageListCommonBasket;
	
	private VirtualUIFrame m_oFramePage;
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIImage m_oImgButtonNextPage;
	
	private VirtualUIButton m_oButtonShowWarning;
	private VirtualUIButton m_oButtonViewAll;
	private VirtualUIButton m_oButtonRefresh;
	
	private List<String> m_oColumnHeaderList;
	private HashMap<String, String> m_oColumnHeaderSortStatusList;
	
	// Others
	private String m_sPanelType;
	private int m_iWarningMessageListingType;
	
	private int m_iCurrentPageStartNo;
	public int m_iPageRecordCount;
	
	private int m_iWarningMessageListRowHeight;

	private boolean m_bRecordLoaded;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameWarningMessageListPanelListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameWarningMessageListPanelListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameWarningMessageListPanelListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	// constructor
	public FrameWarningMessageListPanel(String sPanelType, int iListingtype) {
		m_sPanelType = sPanelType;
		m_iWarningMessageListingType = iListingtype;
		
		m_bRecordLoaded = false;
		m_iCurrentPageStartNo = 0;
		m_oColumnHeaderList = new ArrayList<String>();
		m_oColumnHeaderSortStatusList = new HashMap<String, String>();
		
		//Original List
		m_oPrinterStatusList = new HashMap<>();
		
		//Display List
		m_oDisplayPrinterStatusList = new HashMap<>();
		
		//Message List
		m_oPrinterStatusMessageList = new HashMap<>();
		
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameWarningMessageListPanelListener>();

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraWarningMessageListPanel.xml");

		m_oWarningMessageListCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oWarningMessageListCommonBasket, "scrfraWarningMessageListPanel");
		m_oWarningMessageListCommonBasket.init();
		m_oWarningMessageListCommonBasket.addListener(this);
		this.attachChild(m_oWarningMessageListCommonBasket);
		
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
		this.attachChild(m_oImgButtonPrevPage);
		
		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.allowLongClick(true);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oImgButtonNextPage);
		
		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oLblPage.setWidth(m_oFramePage.getWidth());
		m_oLblPage.setHeight(m_oFramePage.getHeight());
		m_oFramePage.attachChild(m_oLblPage);
		this.attachChild(m_oFramePage);
		
		// Show Warning button
		m_oButtonShowWarning = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonShowWarning, "btnShowWarning");
		m_oButtonShowWarning.setValue(AppGlobal.g_oLang.get()._("show_warning"));
		this.attachChild(m_oButtonShowWarning);
		
		// View All button
		m_oButtonViewAll = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonViewAll, "btnViewAll");
		m_oButtonViewAll.setValue(AppGlobal.g_oLang.get()._("show_all"));
		this.attachChild(m_oButtonViewAll);
		
		// Refresh Button
		m_oButtonRefresh = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonRefresh, "btnRefresh");
		m_oButtonRefresh.setValue(AppGlobal.g_oLang.get()._("refresh"));
		this.attachChild(m_oButtonRefresh);
		
		// Set page record count
		m_iPageRecordCount = 8;
		
		m_iWarningMessageListRowHeight = 36;
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_iWarningMessageListRowHeight = 50;
			m_iPageRecordCount = 10;
		}
		
		//Printer status Message List init
		m_oPrinterStatusMessageList.put(PRINTER_STATUS_ONLINE, AppGlobal.g_oLang.get()._("normal"));
		m_oPrinterStatusMessageList.put(PRINTER_STATUS_OFFLINE, AppGlobal.g_oLang.get()._("offline"));
		m_oPrinterStatusMessageList.put(PRINTER_STATUS_ALMOST_PAPER_OUT, AppGlobal.g_oLang.get()._("almost_paper_out"));
		m_oPrinterStatusMessageList.put(PRINTER_STATUS_PAPER_OUT, AppGlobal.g_oLang.get()._("paper_out"));
	}
	
	public void initWarningList(int iPanelId, JSONArray oWarningMessageListJSONArray) {
		m_bRecordLoaded = true;

		if (iPanelId == FrameWarningMessageList.PANEL_PRINTER_STATUS) {
			m_oPrinterStatusList = new HashMap<>();
			m_oDisplayPrinterStatusList = new HashMap<>();
			
			for (int i =0; i < oWarningMessageListJSONArray.length(); i++) {
				PrinterStatus oPrinterStatus = new PrinterStatus();
				try {
					oPrinterStatus.iId = oWarningMessageListJSONArray.getJSONObject(i).optInt("id");
					oPrinterStatus.sPrinterName = oWarningMessageListJSONArray.getJSONObject(i).optJSONObject("printStatus").optString("pdev_name");
					oPrinterStatus.sPrinterHealth = oWarningMessageListJSONArray.getJSONObject(i).optJSONObject("printStatus").optString("pdev_health");
					oPrinterStatus.sPrinterStatus = oWarningMessageListJSONArray.getJSONObject(i).optJSONObject("printStatus").optString("pdev_status");
					oPrinterStatus.sPrinterMessage = warningMessageMapping(oPrinterStatus.sPrinterStatus);
				} catch (Exception e) {
					AppGlobal.stackToString(e);
				}
				m_oPrinterStatusList.put(i, oPrinterStatus);
			}
		}
	}
	
	public void addWarningMessageListPanelTitle(String sPanelType) {
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		
		if(sPanelType.equals(FrameWarningMessageListPanel.TYPE_PRINTER_STATUS)) {
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				iFieldWidths.add(235);
				sFieldValues.add(AppGlobal.g_oLang.get()._("print_queue"));
				iFieldWidths.add(235);				
				sFieldValues.add(AppGlobal.g_oLang.get()._("messages"));
				
				m_oColumnHeaderList.add(COLUMN_HEADER_PRINT_QUEUE);
				m_oColumnHeaderList.add(COLUMN_HEADER_MESSAGE);
			} else {
				iFieldWidths.add(100);
				sFieldValues.add("");
				iFieldWidths.add(425);
				sFieldValues.add(AppGlobal.g_oLang.get()._("print_queue"));
				iFieldWidths.add(425);
				sFieldValues.add(AppGlobal.g_oLang.get()._("messages"));
				
				m_oColumnHeaderList.add("");
				m_oColumnHeaderList.add(COLUMN_HEADER_PRINT_QUEUE);
				m_oColumnHeaderList.add(COLUMN_HEADER_MESSAGE);
			}
		}
		
		// mark default sort column header order
		for(String sColumnHeader: m_oColumnHeaderList) {
			m_oColumnHeaderSortStatusList.put(sColumnHeader, SORT_ORDER_NONE);
		}
		
		m_oWarningMessageListCommonBasket.addHeader(iFieldWidths, sFieldValues);
		m_oWarningMessageListCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		m_oWarningMessageListCommonBasket.setUpperlineVisible(true);
		
		m_oWarningMessageListCommonBasket.setHeaderFormat(36, 15, "");

		for (int i=0; i < m_oColumnHeaderList.size(); i++)
			m_oWarningMessageListCommonBasket.setHeaderTextAlign(i, HeroActionProtocol.View.Attribute.TextAlign.CENTER);

		m_oWarningMessageListCommonBasket.setBottomUnderlineVisible(true);
	}
	
	public boolean loadedRecord() {
		return this.m_bRecordLoaded;
	}
	
	public void setDisplayList(int iSelectedPanelTab, boolean bIsShowWarning) {
		m_iCurrentPageStartNo = 0;
		if(iSelectedPanelTab == FrameWarningMessageList.PANEL_PRINTER_STATUS) {
			m_oDisplayPrinterStatusList = new HashMap<>();
			if (bIsShowWarning) {
				for (int i = 0, iCount = 0; i < m_oPrinterStatusList.size(); i++) {
					if (!m_oPrinterStatusList.get(i).sPrinterStatus.equals(PRINTER_STATUS_ONLINE)) {
						m_oDisplayPrinterStatusList.put(iCount, m_oPrinterStatusList.get(i));
						iCount++;
					}
				}
			} else {
				for(Entry<Integer, PrinterStatus> entry: m_oPrinterStatusList.entrySet())
					m_oDisplayPrinterStatusList.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	
	
	private void addWarningMessageListRecord(String sType, int iItemIndex, int iListNum) {
		
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		ArrayList<String> sFieldTypes = new ArrayList<String>();

		if(sType.equals(FrameWarningMessageListPanel.TYPE_PRINTER_STATUS)) {
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				iFieldWidths.add(235);
				sFieldValues.add(m_oDisplayPrinterStatusList.get(iListNum).sPrinterName);
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iFieldWidths.add(235);
				sFieldValues.add(m_oDisplayPrinterStatusList.get(iListNum).sPrinterMessage);
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			}else {
				iFieldWidths.add(100);
				sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_printer.png");
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.IMAGE);
				iFieldWidths.add(425);
				sFieldValues.add(m_oDisplayPrinterStatusList.get(iListNum).sPrinterName);
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iFieldWidths.add(425);
				sFieldValues.add(m_oDisplayPrinterStatusList.get(iListNum).sPrinterMessage);
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			}
		}
		
		m_oWarningMessageListCommonBasket.addItem(0, iItemIndex, m_iWarningMessageListRowHeight, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);

		for(int i=0; i<sFieldValues.size(); i++)
			m_oWarningMessageListCommonBasket.setFieldTextSize(0, iItemIndex, i, 15);
	}

	// Add Warning Message Record 
	public void updateWarningMessageListRecord() {
		removeWarningMessageListRecord();
		if (m_sPanelType.equals(FrameWarningMessageListPanel.TYPE_PRINTER_STATUS)) {
			for(int i = m_iCurrentPageStartNo, j = 0; i < m_iCurrentPageStartNo+m_iPageRecordCount && i < m_oDisplayPrinterStatusList.size(); i++, j++) {
				this.addWarningMessageListRecord(FrameWarningMessageListPanel.TYPE_PRINTER_STATUS, j, i);
			}
		}
		updatePageUpDownVisibility();
	}
	
	public void removeWarningMessageListRecord() {
		m_oWarningMessageListCommonBasket.removeAllItems(0);
	}
	
	//Message Mapping Function
	private String warningMessageMapping (String sStatus) {
		if (m_sPanelType.equals(FrameWarningMessageListPanel.TYPE_PRINTER_STATUS))
			return m_oPrinterStatusMessageList.containsKey(sStatus) ? m_oPrinterStatusMessageList.get(sStatus) : "";
		
		return "";
	}
	
	public void updatePageUpDownVisibility() {
		boolean bShowPageUp = false;
		boolean bShowPageDown = false;
		int iPage = 0;
		int iCurrentPanelRecordCount = 0;
		
		if(m_sPanelType.equals(FrameWarningMessageListPanel.TYPE_PRINTER_STATUS))
			iCurrentPanelRecordCount = m_oDisplayPrinterStatusList.size();

		if(iCurrentPanelRecordCount > m_iPageRecordCount)
			iPage = (m_iCurrentPageStartNo / m_iPageRecordCount) + 1;
			
		if(m_iCurrentPageStartNo > 0)
			bShowPageUp = true;
			
		if(iCurrentPanelRecordCount > m_iPageRecordCount && m_iCurrentPageStartNo+m_iPageRecordCount < iCurrentPanelRecordCount)
			bShowPageDown = true;
		
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
		if(iNumber > 0) {
			if(m_sPanelType.equals(FrameWarningMessageListPanel.TYPE_PRINTER_STATUS))
				iTotalPage = (int)Math.ceil(1.0 * m_oDisplayPrinterStatusList.size() / m_iPageRecordCount);
			
			m_oFramePage.setVisible(true);
			m_oLblPage.setValue(iNumber + " / " + iTotalPage);
			m_oLblPage.setVisible(true);
			m_oImgButtonPrevPage.setVisible(true);
			m_oImgButtonNextPage.setVisible(true);
		} else {
			m_oFramePage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
			m_oImgButtonNextPage.setVisible(false);
		}
	}
	
	public void updateButtonColor(boolean bIsShowAll) {
		String sSelectedBackgroundColor				= "#0055B8";
		String sUnselectedBackgroundColor			= "#A0B3B7";
		String sUnselectedBackgroundColorForMobile	= "#E0E0E0";
		
		String sSelectedForegroundColorForMobile	= "#FFFFFF";
		String sUnselectedForegroundColorForMobile	= "#015384";
		
		if(!bIsShowAll) {
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				m_oButtonShowWarning.setBackgroundColor(sSelectedBackgroundColor);
				m_oButtonViewAll.setBackgroundColor(sUnselectedBackgroundColorForMobile);
				
				m_oButtonShowWarning.setForegroundColor(sSelectedForegroundColorForMobile);
				m_oButtonViewAll.setForegroundColor(sUnselectedForegroundColorForMobile);
				
			} else {
				m_oButtonShowWarning.setBackgroundColor(sSelectedBackgroundColor);
				m_oButtonViewAll.setBackgroundColor(sUnselectedBackgroundColor);
			}
		} else {
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				m_oButtonViewAll.setBackgroundColor(sSelectedBackgroundColor);
				m_oButtonShowWarning.setBackgroundColor(sUnselectedBackgroundColorForMobile);
				
				m_oButtonViewAll.setForegroundColor(sSelectedForegroundColorForMobile);
				m_oButtonShowWarning.setForegroundColor(sUnselectedForegroundColorForMobile);
			} else {
				m_oButtonViewAll.setBackgroundColor(sSelectedBackgroundColor);
				m_oButtonShowWarning.setBackgroundColor(sUnselectedBackgroundColor);
			}
		}
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (iChildId == m_oImgButtonPrevPage.getId()) {
			 // PAGE UP
			if(m_iCurrentPageStartNo-m_iPageRecordCount >= 0) {
				m_iCurrentPageStartNo -= m_iPageRecordCount;
				if(m_sPanelType.equals(FrameWarningMessageListPanel.TYPE_PRINTER_STATUS)){
					updateWarningMessageListRecord();
				}
			}
			bMatchChild = true;
		} else if (iChildId == m_oImgButtonNextPage.getId()) {
			 // PAGE DOWN
			if(m_sPanelType.equals(FrameWarningMessageListPanel.TYPE_PRINTER_STATUS)){
				if(m_iCurrentPageStartNo+m_iPageRecordCount < m_oDisplayPrinterStatusList.size()) {
					m_iCurrentPageStartNo += m_iPageRecordCount;
					updateWarningMessageListRecord();
				}
			}
			bMatchChild = true;
		} else if (iChildId == m_oButtonShowWarning.getId()) {
			// Set the display list to warning list & update UI
			this.setDisplayList(m_iWarningMessageListingType, true);
			this.updateWarningMessageListRecord();
			updateButtonColor(false);
			
			bMatchChild = true;
		} else if (iChildId == m_oButtonViewAll.getId()) {
			// Set the display list to original list & update UI
			this.setDisplayList(m_iWarningMessageListingType, false);
			this.updateWarningMessageListRecord();
			updateButtonColor(true);
			
			bMatchChild = true;
		} else if (iChildId == this.m_oButtonRefresh.getId()) {
			for (FrameWarningMessageListPanelListener listener : listeners) {
				listener.frameWarningMessageListPanel_ButtonClickRefresh(m_sPanelType, m_iWarningMessageListingType);
			}

			//Follow the same flow as m_oButtonViewAll
			this.setDisplayList(m_iWarningMessageListingType, false);
			this.updateWarningMessageListRecord();
			updateButtonColor(true);
			
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
	
	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId,
			String sNote) {
	}
	
	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex,
			int iItemIndex, int iFieldIndex, String sNote) {
	}
	
	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}
	
	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId,
			int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
	}
}

