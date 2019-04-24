package app;

import java.math.BigDecimal;
import java.util.ArrayList;

import commonui.FrameNumberPad;
import commonui.FrameNumberPadListener;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FrameStockOperationListener {
    void frameStockOperation_clickSave();
    void frameStockOperation_clickExit();
    void frameStockOperation_clickAddItemToStockControl();
    void frameStockOperation_clickDeleteItemInStockControl();
    void frameStockOperation_clickEditStockSequence();
    boolean frameStockOperation_clickBasketMenuItem(int iNewSectionId, int iNewItemIndex, int iNewFieldIndex, String sPrevValue);
    void frameStockOperation_clickSearchByName(String sValue);
    void frameStockOperation_clickSearchByDepartment();
    void frameStockOperation_clickSearchByCategory();
    void frameStockOperation_clickSearchByPanel();
    void frameStockOperation_clickShowAllResult();
    void frameStockOperation_clickPrevPage();
    void frameStockOperation_clickNextPage();
}

public class FrameStockOperation extends VirtualUIFrame implements FrameNumberPadListener, FrameCommonBasketListener, FrameTitleHeaderListener {
	TemplateBuilder m_oTemplateBuilder;
	private FrameTitleHeader m_oTitleHeader;
//KingsleyKwan20171016StockOperationByJack		-----Start-----
	private FrameCommonBasket m_oFrameItemList;
	
	private VirtualUIFrame m_oFrameLeftContent;
	private VirtualUIFrame m_oFrameRightContent;
	
	private VirtualUIButton m_oButtonAddItemToStockControl;
	private VirtualUIButton m_oButtonDeleteItemInStockControl;
	private VirtualUIButton m_oButtonEditStockSequence;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonNextPage;

	private VirtualUITextbox m_oTextboxSearchValue;
	private VirtualUIButton m_oButtonSearchByName;
	private VirtualUIButton m_oButtonSearchByCategory;
	private VirtualUIButton m_oButtonSearchByDepartment;
	private VirtualUIButton m_oButtonSearchByPanel;
	private VirtualUIButton m_oButtonShowAllResult;
	
	private FrameNumberPad m_oFrameNumberPad;
	private VirtualUIButton m_oButtonSave;
	private VirtualUIButton m_oButtonExit;
//KingsleyKwan20171016StockOperationByJack		----- End -----
	private int m_iCurrentItemListSection;
	private int m_iCurrentItemListItemIndex;
	private int m_iCurrentItemListFieldIndex;
	private String m_sPreItemAmount;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameStockOperationListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameStockOperationListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameStockOperationListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
	public FrameStockOperation(){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameStockOperationListener>();
//KingsleyKwan20171016StockOperationByJack		-----Start-----
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraStockOperation.xml");

		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("stock_operation"));
		m_oTitleHeader.addListener(this);
		this.attachChild(m_oTitleHeader);

		//Create Left Content Frame
    	m_oFrameLeftContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftContent, "fraLeftContent");
		this.attachChild(m_oFrameLeftContent);

		// Result list
		m_oFrameItemList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFrameItemList, "fraItemList");
		m_oFrameItemList.init();
		m_oFrameItemList.addListener(this);
		m_oFrameLeftContent.attachChild(m_oFrameItemList);
		
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();

		iFieldWidths.add(108);
		sFieldValues.add(AppGlobal.g_oLang.get()._("item_code"));
		if (AppGlobal.g_bNotCheckStock) {
			iFieldWidths.add(330);
			sFieldValues.add(AppGlobal.g_oLang.get()._("item_description"));
			iFieldWidths.add(0);
			sFieldValues.add(AppGlobal.g_oLang.get()._("storage"));
		} else {
			iFieldWidths.add(250);
			sFieldValues.add(AppGlobal.g_oLang.get()._("item_description"));
			iFieldWidths.add(80);
			sFieldValues.add(AppGlobal.g_oLang.get()._("storage"));
		}
		if (!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			iFieldWidths.add(85);
			sFieldValues.add(AppGlobal.g_oLang.get()._("self_stock_in"));
			iFieldWidths.add(85);
			sFieldValues.add(AppGlobal.g_oLang.get()._("stock_in"));
			iFieldWidths.add(85);
			sFieldValues.add(AppGlobal.g_oLang.get()._("wastage"));
			iFieldWidths.add(85);
			if (AppGlobal.g_bNotCheckStock)
				sFieldValues.add(AppGlobal.g_oLang.get()._("close_balance"));
			else
				sFieldValues.add(AppGlobal.g_oLang.get()._("spoilage"));
		}
    	
    	m_oFrameItemList.addHeader(iFieldWidths, sFieldValues);
    	m_oFrameItemList.setHeaderFormat(35, 18, "5,0,0,5");
    	m_oFrameItemList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
    	m_oFrameItemList.setBottomUnderlineVisible(false);
    	
    	// Add Item to Stock button
		m_oButtonAddItemToStockControl = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonAddItemToStockControl, "btnAddItemToStock");
		m_oButtonAddItemToStockControl.setValue(AppGlobal.g_oLang.get()._("add_item"));
		m_oButtonAddItemToStockControl.setVisible(true);
		m_oFrameLeftContent.attachChild(m_oButtonAddItemToStockControl);
		
		// Delete Item in Stock button
		m_oButtonDeleteItemInStockControl = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonDeleteItemInStockControl, "btnDeleteItemInStock");
		m_oButtonDeleteItemInStockControl.setValue(AppGlobal.g_oLang.get()._("delete_item"));
		m_oButtonDeleteItemInStockControl.setVisible(true);
		m_oFrameLeftContent.attachChild(m_oButtonDeleteItemInStockControl);
		
    	// Edit Stock Sequence button
		m_oButtonEditStockSequence = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonEditStockSequence, "btnEditStockSequence");
		m_oButtonEditStockSequence.setValue(AppGlobal.g_oLang.get()._("edit_sequence"));
		m_oButtonEditStockSequence.setVisible(true);
		m_oFrameLeftContent.attachChild(m_oButtonEditStockSequence);
		
		// Previous page button
		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevPage");
		m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
		m_oImgButtonPrevPage.allowClick(true);
		m_oImgButtonPrevPage.setClickServerRequestBlockUI(true);
		m_oImgButtonPrevPage.allowLongClick(false);
		m_oImgButtonPrevPage.setLongClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.setVisible(true);
		m_oFrameLeftContent.attachChild(m_oImgButtonPrevPage);
		
		m_oLblPage = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
    	m_oFrameLeftContent.attachChild(m_oLblPage);
		
		// Next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(true);
		m_oImgButtonNextPage.allowLongClick(false);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.setVisible(true);
		m_oFrameLeftContent.attachChild(m_oImgButtonNextPage);


		//Create Right Content Frame
		m_oFrameRightContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameRightContent, "fraRightContent");
		this.attachChild(m_oFrameRightContent);
		
		// Search value box
		m_oTextboxSearchValue = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxSearchValue, "txtboxSearchValue");
		m_oFrameRightContent.attachChild(m_oTextboxSearchValue);
		
		// Search button
		m_oButtonSearchByName = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSearchByName, "btnSearchByName");
		m_oButtonSearchByName.setValue(AppGlobal.g_oLang.get()._("search_by_name"));
		m_oButtonSearchByName.setVisible(true);
		m_oButtonSearchByName.addClickServerRequestSubmitElement(m_oTextboxSearchValue);
		m_oFrameRightContent.attachChild(m_oButtonSearchByName);
		
		// Department button
		m_oButtonSearchByDepartment = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSearchByDepartment, "btnSearchByDepartment");
		m_oButtonSearchByDepartment.setValue(AppGlobal.g_oLang.get()._("all_departments"));
		m_oButtonSearchByDepartment.setVisible(true);
		//TODO: Further development
		//this.attachChild(m_oButtonSearchByDepartment);
		
		// Category button
		m_oButtonSearchByCategory = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSearchByCategory, "btnSearchByCategory");
		m_oButtonSearchByCategory.setValue(AppGlobal.g_oLang.get()._("all_categories"));
		m_oButtonSearchByCategory.setVisible(true);
		//TODO: Further development
		//this.attachChild(m_oButtonSearchByCategory);
		
		// Search by panel Button
		m_oButtonSearchByPanel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSearchByPanel, "btnSearchByPanel");
		m_oButtonSearchByPanel.setValue(AppGlobal.g_oLang.get()._("search_by_panel"));
		m_oButtonSearchByPanel.setVisible(true);
		m_oFrameRightContent.attachChild(m_oButtonSearchByPanel);
		
		// Show all result button
		m_oButtonShowAllResult = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonShowAllResult, "btnShowAllResult");
		m_oButtonShowAllResult.setValue(AppGlobal.g_oLang.get()._("show_all"));
		m_oButtonShowAllResult.setVisible(true);
		m_oFrameRightContent.attachChild(m_oButtonShowAllResult);
    	
		// Number pad
		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.setEnterBlockUI(true);
		m_oFrameNumberPad.clearEnterSubmitId();
		m_oFrameNumberPad.addListener(this);
		m_oFrameRightContent.attachChild(m_oFrameNumberPad);
		
		// Save button
		m_oButtonSave = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSave, "btnSave");
		m_oButtonSave.setValue(AppGlobal.g_oLang.get()._("save"));
		m_oButtonSave.setForegroundColor("#999999");
		m_oButtonSave.allowClick(true);
		m_oButtonSave.setVisible(true);
		// Set "Save" button to unclicked
		this.setSaveButtonEnabled(false);
		m_oFrameRightContent.attachChild(m_oButtonSave);
		
		// Exit button
		m_oButtonExit = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonExit, "btnExit");
		m_oButtonExit.setValue(AppGlobal.g_oLang.get()._("exit"));
		m_oButtonExit.setVisible(true);
		m_oFrameRightContent.attachChild(m_oButtonExit);
		
		// Init as -ve
		m_iCurrentItemListSection = -1;
		m_iCurrentItemListItemIndex = -1;
		m_iCurrentItemListFieldIndex = -1;
	}
//KingsleyKwan20171016StockOperationByJack		----- End -----
	
	public void addRecord(int iSectionId, int iItemIndex, String sItemCode, String sItemDesc, BigDecimal dCurrentStock, BigDecimal dPrevSelfStockIn, BigDecimal dStockIn, BigDecimal dPrevWastage, BigDecimal dPreviousDamage){
    	ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
    	ArrayList<String> sFieldValues = new ArrayList<String>();
    	ArrayList<String> sFieldAligns = new ArrayList<String>();
    	int iColumns = 3;
    	
    	String sCurrentStock, sPrevSelfStockIn, sStockIn, sPrevWastage, sPreviousDamage;
    	sCurrentStock = StringLib.BigDecimalToStringWithoutZeroDecimal(dCurrentStock);
    	sPrevSelfStockIn = StringLib.BigDecimalToStringWithoutZeroDecimal(dPrevSelfStockIn);
    	sStockIn = StringLib.BigDecimalToStringWithoutZeroDecimal(dStockIn);
    	sPrevWastage = StringLib.BigDecimalToStringWithoutZeroDecimal(dPrevWastage);
    	sPreviousDamage = StringLib.BigDecimalToStringWithoutZeroDecimal(dPreviousDamage);
//KingsleyKwan20171016StockOperationByJack		----Start-----
		iFieldWidths.add(108);
		sFieldValues.add(sItemCode);
		sFieldAligns.add("");
		if(AppGlobal.g_bNotCheckStock){
			iFieldWidths.add(330);
			sFieldValues.add(sItemDesc);
			sFieldAligns.add("");
			iFieldWidths.add(0);
			sFieldValues.add(sCurrentStock);
			sFieldAligns.add("");
		}else{
			iFieldWidths.add(250);
			sFieldValues.add(sItemDesc);
			sFieldAligns.add("");
			iFieldWidths.add(80);
			sFieldValues.add(sCurrentStock);
			sFieldAligns.add("");
		}
		if (!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			iColumns = 7;
			iFieldWidths.add(85);
			sFieldValues.add(sPrevSelfStockIn);
			sFieldAligns.add("");
			iFieldWidths.add(85);
			sFieldValues.add(sStockIn);
			sFieldAligns.add("");
			iFieldWidths.add(85);
			sFieldValues.add(sPrevWastage);
			sFieldAligns.add("");
			iFieldWidths.add(85);
			sFieldValues.add(sPreviousDamage);
			sFieldAligns.add("");
		}
		m_oFrameItemList.addItem(iSectionId, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
		for (int iFieldIndex = 0; iFieldIndex < iColumns; iFieldIndex++){
			m_oFrameItemList.setFieldTextSize(iSectionId, iItemIndex, iFieldIndex, 15);
			m_oFrameItemList.setFieldPadding(iSectionId, iItemIndex, iFieldIndex, "15,0,0,5");
		}
//KingsleyKwan20171016StockOperationByJack		----- End -----
		// Change the color of the fields that are not editable to grey
		if (!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oFrameItemList.setFieldBackgroundColor(iSectionId, iItemIndex, 2, "#D9D9D9");
	}
	
	public void setResultLineVisible(ArrayList<Integer> oLineIndex, boolean bVisible){
		for(Integer iLineIndex:oLineIndex){
			m_oFrameItemList.setLineVisible(0, iLineIndex, bVisible);
		}
	}
	
	public void setCategoryButtonDesc(String sDesc){
		m_oButtonSearchByCategory.setValue(sDesc);
	}
	
	public void setDepartmentButtonDesc(String sDesc){
		m_oButtonSearchByDepartment.setValue(sDesc);
	}
	
	public void setCellFieldBackgroundColorEdited(int iSectionId, int iItemIndex, int iFieldIndex, boolean bEdited) {
		if(bEdited && !AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oFrameItemList.setFieldBackgroundColor(iSectionId, iItemIndex, iFieldIndex, "#66A6F1");
			// Set "Save" button to clickable
			if(!this.m_oButtonSave.getEnabled()) {
				this.setSaveButtonEnabled(true);
			}
		}
		else {
			m_oFrameItemList.setFieldBackgroundColor(iSectionId, iItemIndex, iFieldIndex, "#FFFFFF");
		}
	}
	
	public void clearSearchValue(){
		m_oTextboxSearchValue.setValue("");
	}
	
	public void clearAllRecords() {
		m_oFrameItemList.removeAllItems(0);
	}
//KingsleyKwan20171016StockOperationByJack		-----Start-----
	public void showNextPageButton(boolean bShow) {
		if(bShow)
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
		else
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
		m_oImgButtonNextPage.setEnabled(bShow);
	}
	
	public void showPrevPageButton(boolean bShow) {
		if(bShow)
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
		else
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
		m_oImgButtonPrevPage.setEnabled(bShow);
	}
	
	public void setSaveButtonEnabled(boolean bEnabled) {
		this.m_oButtonSave.setEnabled(bEnabled);
		
		if(bEnabled){
			m_oButtonSave.setBackgroundColor("#0055B8");
			m_oButtonSave.setForegroundColor("#FFFFFF");
		}else{
			m_oButtonSave.setBackgroundColor("#FFFFFF");
			m_oButtonSave.setForegroundColor("#999999");
		}
	}
//KingsleyKwan20171016StockOperationByJack		----- End -----
	
	// iMode = SEARCH_MODE_SHOW_ALL : Press "Show All"
	// iMode = SEARCH_MODE_BY_NAME  : Press "Search By Name"
	// iMode = SEARCH_MODE_BY_PANEL : Press "Search By Panel"
	public void updateSearchButtonColor(String sSearchMode) {
		String sUnselectedBackgroundColor = "#E0E0E0";
		String sUnselectedForegroundColor = "#015384";
		String sUnselectedStrokeColor = "#868686";
		String sSelectedBackgroundColor = "#3481B0";
		String sSelectedForegroundColor = "#FFFFFF";
		String sSelectedStrokeColor = "#005080";
		if (sSearchMode.equals(FormStockOperation.SEARCH_MODE_SHOW_ALL)) {
			// Press "Show All"
			m_oButtonShowAllResult.setBackgroundColor(sSelectedBackgroundColor);
			m_oButtonShowAllResult.setForegroundColor(sSelectedForegroundColor);
			m_oButtonShowAllResult.setStrokeColor(sSelectedStrokeColor);
			m_oButtonSearchByName.setBackgroundColor(sUnselectedBackgroundColor);
			m_oButtonSearchByName.setForegroundColor(sUnselectedForegroundColor);
			m_oButtonSearchByName.setStrokeColor(sUnselectedStrokeColor);
			m_oButtonSearchByPanel.setBackgroundColor(sUnselectedBackgroundColor);
			m_oButtonSearchByPanel.setForegroundColor(sUnselectedForegroundColor);
			m_oButtonSearchByPanel.setStrokeColor(sUnselectedStrokeColor);
		}
		else if (sSearchMode.equals(FormStockOperation.SEARCH_MODE_BY_NAME)) {
			// Press "Search By Name"
			m_oButtonShowAllResult.setBackgroundColor(sUnselectedBackgroundColor);
			m_oButtonShowAllResult.setForegroundColor(sUnselectedForegroundColor);
			m_oButtonShowAllResult.setStrokeColor(sUnselectedStrokeColor);
			m_oButtonSearchByName.setBackgroundColor(sSelectedBackgroundColor);
			m_oButtonSearchByName.setForegroundColor(sSelectedForegroundColor);
			m_oButtonSearchByName.setStrokeColor(sSelectedStrokeColor);
			m_oButtonSearchByPanel.setBackgroundColor(sUnselectedBackgroundColor);
			m_oButtonSearchByPanel.setForegroundColor(sUnselectedForegroundColor);
			m_oButtonSearchByPanel.setStrokeColor(sUnselectedStrokeColor);
		}
		else if (sSearchMode.equals(FormStockOperation.SEARCH_MODE_BY_PANEL)) {
			// Press "Search By Panel"
			m_oButtonShowAllResult.setBackgroundColor(sUnselectedBackgroundColor);
			m_oButtonShowAllResult.setForegroundColor(sUnselectedForegroundColor);
			m_oButtonShowAllResult.setStrokeColor(sUnselectedStrokeColor);
			m_oButtonSearchByName.setBackgroundColor(sUnselectedBackgroundColor);
			m_oButtonSearchByName.setForegroundColor(sUnselectedForegroundColor);
			m_oButtonSearchByName.setStrokeColor(sUnselectedStrokeColor);
			m_oButtonSearchByPanel.setBackgroundColor(sSelectedBackgroundColor);
			m_oButtonSearchByPanel.setForegroundColor(sSelectedForegroundColor);
			m_oButtonSearchByPanel.setStrokeColor(sSelectedStrokeColor);
		}
	}
//KingsleyKwan20171016StockOperationByJack		-----Start-----
	public void setPageNumber(int iTotalPage, int iPageNumber) {
		if(iTotalPage > 1) { // item count > page record number, show page number label
			m_oLblPage.setValue(Integer.toString(iPageNumber) + " / " + Integer.toString(iTotalPage));
			m_oLblPage.setVisible(true);
			m_oImgButtonPrevPage.setVisible(true);
			m_oImgButtonNextPage.setVisible(true);
		}
		else {
			m_oLblPage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
			m_oImgButtonNextPage.setVisible(false);
		}
	}
//KingsleyKwan20171016StockOperationByJack		----- End -----
	private void updatePreviousFieldValue() {
		if(m_iCurrentItemListItemIndex < 0 || m_iCurrentItemListFieldIndex < 0)
			return;
		if(m_iCurrentItemListFieldIndex != 3 && m_iCurrentItemListFieldIndex != 4 && m_iCurrentItemListFieldIndex != 5 && m_iCurrentItemListFieldIndex != 6)
			return;
		
		// Get pre item stock value 
		String sPrevValue = null;
		if(AppGlobal.g_oFuncStation.get().getItemStockInputMode() == FuncStation.ITEM_STOCK_INPUT_ADD_ON)
			sPrevValue = m_sPreItemAmount;
		else		
			sPrevValue = m_oFrameItemList.getFieldValue(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, m_iCurrentItemListFieldIndex);
		
		BigDecimal dPrevValue = BigDecimal.ZERO;
		if(!sPrevValue.isEmpty()) {
			dPrevValue = new BigDecimal(sPrevValue.trim());
		}
		
		// Get current value in edit field 
		String sNewValue = m_oFrameItemList.getEditFieldValue();
		BigDecimal dNewValue = BigDecimal.ZERO;
		if(!sNewValue.isEmpty()) {
			dNewValue = new BigDecimal(sNewValue.trim());
		}
		
		String sFinalValue = "";
		if(AppGlobal.g_oFuncStation.get().getItemStockInputMode() == FuncStation.ITEM_STOCK_INPUT_REPLACE)
			sFinalValue = StringLib.BigDecimalToStringWithoutZeroDecimal(dNewValue);
		else
			sFinalValue = StringLib.BigDecimalToStringWithoutZeroDecimal(dPrevValue.add(dNewValue));
		
		m_oFrameItemList.setFieldValue(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, m_iCurrentItemListFieldIndex, sFinalValue);

	}
	
	public void hiddenItemList(boolean bHidden) {
		m_oFrameItemList.setVisible(!bHidden);
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		// Find the clicked button
		if (m_oButtonSave.getId() == iChildId) {

			// Press "ENTER" first to prevent user forget to press
			FrameNumberPad_clickEnter();
			
			for (FrameStockOperationListener listener : listeners) {
    			// Raise the event to parent
    	       	listener.frameStockOperation_clickSave();
    		}
			
			m_iCurrentItemListItemIndex = -1;
			m_iCurrentItemListFieldIndex = -1;
			m_oFrameNumberPad.clearEnterSubmitId();
			m_oFrameItemList.clearEditField();
			
			// Set "Save" button to unclicked
			this.setSaveButtonEnabled(false);
			
			bMatchChild = true;
        }
		else
//KingsleyKwan20171016StockOperationByJack		-----Start-----
    	if (m_oButtonExit.getId() == iChildId) {
//KingsleyKwan20171016StockOperationByJack		----- End -----
			for (FrameStockOperationListener listener : listeners) {
    			// Raise the event to parent
    	       	listener.frameStockOperation_clickExit();
    		}
			bMatchChild = true;
        }
    	else
    	if (m_oButtonAddItemToStockControl.getId() == iChildId) {
			for (FrameStockOperationListener listener : listeners) {
    			// Raise the event to parent
    	       	listener.frameStockOperation_clickAddItemToStockControl();
    		}
			bMatchChild = true;
        }
    	else if(iChildId == m_oButtonDeleteItemInStockControl.getId()) {
    		for(FrameStockOperationListener listener : listeners) {
    			listener.frameStockOperation_clickDeleteItemInStockControl();
    		}
    		bMatchChild = true;
    	}
    	else if (iChildId == m_oButtonEditStockSequence.getId()) {
    		for(FrameStockOperationListener listener : listeners) {
    			listener.frameStockOperation_clickEditStockSequence();
    		}
    		bMatchChild = true;
    	}
    	else
    	if (m_oButtonSearchByName.getId() == iChildId) {
			for (FrameStockOperationListener listener : listeners) {
    			// Raise the event to parent
    	       	listener.frameStockOperation_clickSearchByName(m_oTextboxSearchValue.getValue());
    		}
//KingsleyKwan20171016StockOperationByJack		-----Start-----
			bMatchChild = true;
//KingsleyKwan20171016StockOperationByJack		----- End -----
        }
    	else
		if (m_oButtonSearchByDepartment.getId() == iChildId) {
			for (FrameStockOperationListener listener : listeners) {
    			// Raise the event to parent
    	       	listener.frameStockOperation_clickSearchByDepartment();
    		}
			bMatchChild = true;
        }
    	else
    	if (m_oButtonSearchByCategory.getId() == iChildId) {
			for (FrameStockOperationListener listener : listeners) {
    			// Raise the event to parent
    	       	listener.frameStockOperation_clickSearchByCategory();
    		}
			bMatchChild = true;
        }
    	else
        if (m_oButtonSearchByPanel.getId() == iChildId) {
			for (FrameStockOperationListener listener : listeners) {
    			// Raise the event to parent
    	       	listener.frameStockOperation_clickSearchByPanel();
    		}
//KingsleyKwan20171016StockOperationByJack		-----Start-----
			bMatchChild = true;
		}
		else
		if (m_oButtonShowAllResult.getId() == iChildId) {
			for (FrameStockOperationListener listener : listeners) {
				// Raise the event to parent
				listener.frameStockOperation_clickShowAllResult();
			}
			bMatchChild = true;
//KingsleyKwan20171016StockOperationByJack		----- End -----
        }
    	else if(iChildId == m_oImgButtonNextPage.getId()) {
    		for(FrameStockOperationListener listener : listeners) {
    			listener.frameStockOperation_clickNextPage();
    		}
    		bMatchChild = true;
    	}
    	else if(iChildId == m_oImgButtonPrevPage.getId()) {
    		for(FrameStockOperationListener listener : listeners) {
    			listener.frameStockOperation_clickPrevPage();
    		}
    		bMatchChild = true;
    	}
        	
    	return bMatchChild;
    }

	@Override
	public void FrameNumberPad_clickEnter() {
		// Save previous value first
		updatePreviousFieldValue();
		String sPrevValue = "0.0";
		if(m_iCurrentItemListItemIndex >= 0 && m_iCurrentItemListFieldIndex >= 0) {
			sPrevValue = m_oFrameItemList.getFieldValue(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, m_iCurrentItemListFieldIndex);
		}
		else
			return;
		
		//Move to next field index
		Boolean bSetNextField = true;
		switch(m_iCurrentItemListFieldIndex) {
			case 3:
				m_iCurrentItemListFieldIndex = 4;
				break;
			case 4:
				m_iCurrentItemListFieldIndex = 5;
				break;
			case 5:
				m_iCurrentItemListFieldIndex = 6;
				break;
			case 6:
				if(m_iCurrentItemListItemIndex < m_oFrameItemList.getItemCellCount(0)-1) {
					m_iCurrentItemListFieldIndex = 3;
					m_iCurrentItemListItemIndex++;
				}
				else {			//Last row
					bSetNextField = false;
					m_iCurrentItemListItemIndex = -1;
					m_iCurrentItemListFieldIndex = -1;
				}
				break;
			default:
				m_iCurrentItemListFieldIndex = 3;
				break;
		}
		
		for (FrameStockOperationListener listener : listeners) {
			// Raise the event to parent
			bSetNextField = listener.frameStockOperation_clickBasketMenuItem(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, m_iCurrentItemListFieldIndex, sPrevValue);
		}
		
		if(AppGlobal.g_oFuncStation.get().getItemStockInputMode() == FuncStation.ITEM_STOCK_INPUT_ADD_ON){
			if(bSetNextField){
				m_sPreItemAmount = m_oFrameItemList.getFieldValue(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, m_iCurrentItemListFieldIndex);
				m_oFrameItemList.setFieldValue(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, m_iCurrentItemListFieldIndex, "0");
			
			}
		}
		
		//Move to new field to edit
		m_oFrameNumberPad.clearEnterSubmitId();
		if(bSetNextField) {
			VirtualUIBasicElement oElement = m_oFrameItemList.setEditField(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, m_iCurrentItemListFieldIndex);
			m_oFrameNumberPad.setEnterSubmitId(oElement);
			m_oButtonSave.addClickServerRequestSubmitElement(oElement);
		}
		else {
			m_iCurrentItemListSection = 0;
			m_iCurrentItemListItemIndex = 0;
			m_iCurrentItemListFieldIndex = 0;
			m_oFrameItemList.clearEditField();
		}
	}

	@Override
	public void FrameNumberPad_clickCancel() {
		m_iCurrentItemListItemIndex = -1;
		m_iCurrentItemListFieldIndex = -1;
		m_oFrameNumberPad.clearEnterSubmitId();
		m_oFrameItemList.clearEditField();
	}

	@Override
	public void FrameNumberPad_clickNumber(String string) {
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId,
			String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		boolean bSetNextField = true;
		
		updatePreviousFieldValue();
		//Update indexes in FormStockOperation
		String sPrevValue = null;
		if(m_iCurrentItemListItemIndex >= 0 && m_iCurrentItemListFieldIndex >= 0) {
			sPrevValue = m_oFrameItemList.getFieldValue(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, m_iCurrentItemListFieldIndex);
		}
		for (FrameStockOperationListener listener : listeners) {
			// Raise the event to parent
			bSetNextField = listener.frameStockOperation_clickBasketMenuItem(iSectionIndex, iItemIndex, iFieldIndex, sPrevValue);
		}
		
		if (!bSetNextField) {
			m_iCurrentItemListSection = 0;
			m_iCurrentItemListItemIndex = 0;
			m_iCurrentItemListFieldIndex = 0;
			m_oFrameNumberPad.clearEnterSubmitId();
			m_oFrameItemList.clearEditField();
			return;
		}
		
		if(AppGlobal.g_oFuncStation.get().getItemStockInputMode() == FuncStation.ITEM_STOCK_INPUT_ADD_ON){
			if(iFieldIndex > 2){
				m_sPreItemAmount = m_oFrameItemList.getFieldValue(iSectionIndex, iItemIndex, iFieldIndex);
				m_oFrameItemList.setFieldValue(iSectionIndex, iItemIndex, iFieldIndex, "0");
			}
		}
		
		//Move to new field
		if(iFieldIndex == 3 || iFieldIndex == 4 || iFieldIndex == 5 || iFieldIndex == 6) {
			VirtualUIBasicElement oElement = m_oFrameItemList.setEditField(iSectionIndex, iItemIndex, iFieldIndex);
			m_oFrameNumberPad.clearEnterSubmitId();
			m_oFrameNumberPad.setEnterSubmitId(oElement);
			m_oButtonSave.addClickServerRequestSubmitElement(oElement);
		}
		else {
			m_oFrameNumberPad.clearEnterSubmitId();
			m_oFrameItemList.clearEditField();
		}
		
		m_iCurrentItemListSection = iSectionIndex;
		m_iCurrentItemListItemIndex = iItemIndex;
		m_iCurrentItemListFieldIndex = iFieldIndex;
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId,
			int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
	}

	@Override
	public void FrameTitleHeader_close() {
		for (FrameStockOperationListener listener : listeners) {
			// Raise the event to parent
			listener.frameStockOperation_clickExit();
		}
	}
}
