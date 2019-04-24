package app;

import java.math.BigDecimal;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import commonui.FrameNumberPad;
import commonui.FrameNumberPadListener;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FrameStockBalanceChangeListener {
	void FrameStockBalanceChange_clickSave();
	void FrameStockBalanceChange_clickExit();
	void FrameStockBalanceChange_clickBasketMenuItem(int iNewSectionId, int iNewItemIndex, int iNewFieldIndex, String sPrevValue);
	void FrameStockBalanceChange_clickSearchByName(String sValue);
	void FrameStockBalanceChange_clickSearchByDepartment();
	void FrameStockBalanceChange_clickSearchByCategory();
	void FrameStockBalanceChange_clickShowAllResult();
	void FrameStockBalanceChange_clickPrevPage();
	void FrameStockBalanceChange_clickNextPage();
	void FrameStockBalanceChange_clickDate(String sValue);
}

public class FrameStockBalanceChange extends VirtualUIFrame implements FrameNumberPadListener, FrameCommonBasketListener, FrameTitleHeaderListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oFrameTitleHeader;
	private FrameCommonBasket m_oFrameItemList;
	
	private VirtualUIFrame m_oFrameLeftContent;
	private VirtualUIFrame m_oFrameRightContent;
	
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIFrame m_oFramePage;
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonNextPage;

	private VirtualUITextbox m_oTextboxYrValue;
	private VirtualUITextbox m_oTextboxMthValue;
	private VirtualUITextbox m_oTextboxDayValue;
	private VirtualUIButton m_oButtonDate;
	
	private VirtualUITextbox m_oTextboxSearchValue;
	private VirtualUIButton m_oButtonSearchByName;
	private VirtualUIButton m_oButtonSearchByCategory;
	private VirtualUIButton m_oButtonSearchByDepartment;
	private VirtualUIButton m_oButtonShowAllResult;
	
	private FrameNumberPad m_oFrameNumberPad;
	private VirtualUIButton m_oButtonSave;
	
	private int m_iCurrentItemListSection;
	private int m_iCurrentItemListItemIndex;
	private int m_iCurrentItemListFieldIndex;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameStockBalanceChangeListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameStockBalanceChangeListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameStockBalanceChangeListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameStockBalanceChange(){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameStockBalanceChangeListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraStockBalanceChange.xml");

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(true);
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("stock_balance_change"));
		m_oFrameTitleHeader.addListener(this);
		this.attachChild(m_oFrameTitleHeader);
		
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
		iFieldWidths.add(120);
		sFieldValues.add(AppGlobal.g_oLang.get()._("item_code"));
		iFieldWidths.add(506);
		sFieldValues.add(AppGlobal.g_oLang.get()._("item_description"));
		iFieldWidths.add(100);
		sFieldValues.add(AppGlobal.g_oLang.get()._("close_balance"));
		iFieldWidths.add(100);
		sFieldValues.add(AppGlobal.g_oLang.get()._("current_count"));

		m_oFrameItemList.addHeader(iFieldWidths, sFieldValues);
		m_oFrameItemList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		// Previous page button
		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevPage");
		m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");

		m_oImgButtonPrevPage.allowClick(true);
		m_oImgButtonPrevPage.setClickServerRequestBlockUI(true);
		m_oImgButtonPrevPage.allowLongClick(false);
		m_oImgButtonPrevPage.setLongClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.setVisible(false);
		m_oFrameLeftContent.attachChild(m_oImgButtonPrevPage);
		
		//Create Page Label
		m_oFramePage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePage, "fraPage");
		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oLblPage.setWidth(m_oFramePage.getWidth());
		m_oLblPage.setHeight(m_oFramePage.getHeight() - 2);
		m_oFramePage.attachChild(m_oLblPage);
		m_oFrameLeftContent.attachChild(m_oFramePage);
		
		// Next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(true);
		m_oImgButtonNextPage.allowLongClick(false);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.setVisible(false);
		m_oFrameLeftContent.attachChild(m_oImgButtonNextPage);

		//Create Right Content Frame
		m_oFrameRightContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameRightContent, "fraRightContent");
		this.attachChild(m_oFrameRightContent);
		
		// Search Date boxes /////////////////////////////////////////////////////
		m_oTextboxYrValue = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxYrValue, "txtboxYrValue");
		m_oTextboxYrValue.setHint(AppGlobal.g_oLang.get()._("year"));
		m_oFrameRightContent.attachChild(m_oTextboxYrValue);
		
		m_oTextboxMthValue = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxMthValue, "txtboxMthValue");
		m_oTextboxMthValue.setHint(AppGlobal.g_oLang.get()._("month"));
		m_oFrameRightContent.attachChild(m_oTextboxMthValue);
		
		m_oTextboxDayValue = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxDayValue, "txtboxDayValue");
		m_oTextboxDayValue.setHint(AppGlobal.g_oLang.get()._("day"));
		m_oFrameRightContent.attachChild(m_oTextboxDayValue);
		
		m_oButtonDate = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonDate, "btnDate");
		m_oButtonDate.setValue(AppGlobal.g_oLang.get()._("search"));
		m_oButtonDate.setVisible(true);
		m_oButtonDate.addClickServerRequestSubmitElement(m_oTextboxYrValue);
		m_oButtonDate.addClickServerRequestSubmitElement(m_oTextboxMthValue);
		m_oButtonDate.addClickServerRequestSubmitElement(m_oTextboxDayValue);
		m_oFrameRightContent.attachChild(m_oButtonDate);
		//////////////////////////////////////////////////////////////////////////
		
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
		m_oFrameNumberPad.setEnterSubmitId(m_oTextboxYrValue);
		m_oFrameNumberPad.setEnterSubmitId(m_oTextboxMthValue);
		m_oFrameNumberPad.setEnterSubmitId(m_oTextboxDayValue);
		m_oFrameNumberPad.addListener(this);
		m_oFrameRightContent.attachChild(m_oFrameNumberPad);
		
		// Save button
		m_oButtonSave = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSave, "btnSave");
		m_oButtonSave.setValue(AppGlobal.g_oLang.get()._("save"));
		m_oButtonSave.setVisible(true);
		// Set "Save" button to unclicked
		this.setSaveButtonEnabled(false);
		m_oFrameRightContent.attachChild(m_oButtonSave);
		
		// Init as -ve
		m_iCurrentItemListSection = -1;
		m_iCurrentItemListItemIndex = -1;
		m_iCurrentItemListFieldIndex = -1;
		
		this.updateSearchButtonColor();
		
		m_oTextboxYrValue.setFocusWhenShow(true);
	}
	
	public void addRecord(int iSectionId, int iItemIndex, String sItemCode, String sItemDesc, BigDecimal dEndStock, BigDecimal dNewEndStock){
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();

		String sEndStock, sNewEndStock;
		sEndStock = StringLib.BigDecimalToStringWithoutZeroDecimal(dEndStock);
		sNewEndStock = StringLib.BigDecimalToStringWithoutZeroDecimal(dNewEndStock);

		// Item Desc
		iFieldWidths.add(120);
		sFieldValues.add(sItemCode);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		iFieldWidths.add(506);
		sFieldValues.add(sItemDesc);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		iFieldWidths.add(100);
		sFieldValues.add(sEndStock);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		iFieldWidths.add(100);
		sFieldValues.add(sNewEndStock);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);

		m_oFrameItemList.addItem(iSectionId, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);

		// Change the color of the fields that are not editable to grey
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
		if(bEdited) {
			m_oFrameItemList.setFieldBackgroundColor(iSectionId, iItemIndex, iFieldIndex, "#FFFF00");

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
		m_iCurrentItemListSection = -1;
		m_iCurrentItemListItemIndex = -1;
		m_iCurrentItemListFieldIndex = -1;
		
		m_oFrameItemList.removeAllItems(0);
	}
	
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
			m_oButtonSave.setForegroundColor("#FFFFFF");
			m_oButtonSave.setBackgroundColor("#02428B");
		}
		else{
			m_oButtonSave.setForegroundColor("#A0A0A0");
			m_oButtonSave.setBackgroundColor("#5B6F73");
		}
	}
	
	public void updateSearchButtonColor() {
		String sUnselectedBackgroundColor = "#5B6F73";
		String sUnselectedForegroundColor = "#FFFFFF";
		String sUnselectedStrokeColor = "#868686";
		String sSelectedBackgroundColor = "#02428B";
		String sSelectedForegroundColor = "#FFFFFF";
		String sSelectedStrokeColor = "#005080";
		if(!m_oTextboxSearchValue.getValue().isEmpty()) {
			m_oButtonSearchByName.setBackgroundColor(sSelectedBackgroundColor);
			m_oButtonSearchByName.setForegroundColor(sSelectedForegroundColor);
			m_oButtonSearchByName.setStrokeColor(sSelectedStrokeColor);
			m_oButtonShowAllResult.setBackgroundColor(sUnselectedBackgroundColor);
			m_oButtonShowAllResult.setForegroundColor(sUnselectedForegroundColor);
			m_oButtonShowAllResult.setStrokeColor(sUnselectedStrokeColor);
		} else {
			m_oButtonSearchByName.setBackgroundColor(sUnselectedBackgroundColor);
			m_oButtonSearchByName.setForegroundColor(sUnselectedForegroundColor);
			m_oButtonSearchByName.setStrokeColor(sUnselectedStrokeColor);
			m_oButtonShowAllResult.setBackgroundColor(sSelectedBackgroundColor);
			m_oButtonShowAllResult.setForegroundColor(sSelectedForegroundColor);
			m_oButtonShowAllResult.setStrokeColor(sSelectedStrokeColor);
		}
	}
	
	public void setPageNumber(int iTotalPage, int iPageNumber) {
		if (iTotalPage > 1) { // item count > page record number, show page number label
			m_oFramePage.setVisible(true);
			m_oLblPage.setValue(Integer.toString(iPageNumber) + "/" + Integer.toString(iTotalPage));
			m_oLblPage.setVisible(true);
			m_oImgButtonPrevPage.setVisible(true);
			m_oImgButtonNextPage.setVisible(true);
		} else {
			m_oFramePage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
			m_oImgButtonNextPage.setVisible(false);
		}
	}
	
	private void updatePreviousFieldValue() {
		if(m_iCurrentItemListItemIndex < 0 || m_iCurrentItemListFieldIndex < 0)
			return;
		if(m_iCurrentItemListFieldIndex != 3 && m_iCurrentItemListFieldIndex != 4 && m_iCurrentItemListFieldIndex != 5 && m_iCurrentItemListFieldIndex != 6)
			return;
		
		// Get current value in edit field 
		String sNewValue = m_oFrameItemList.getEditFieldValue();
		BigDecimal dNewValue = BigDecimal.ZERO;
		if(!sNewValue.isEmpty()) {
			dNewValue = new BigDecimal(sNewValue.trim());
		}

		String sValue = StringLib.BigDecimalToStringWithoutZeroDecimal(dNewValue);
		m_oFrameItemList.setFieldValue(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, m_iCurrentItemListFieldIndex, sValue);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		// Find the clicked button
		if (m_oButtonSave.getId() == iChildId) {

			// Press "ENTER" first to prevent user forget to press
			FrameNumberPad_clickEnter();

			for (FrameStockBalanceChangeListener listener : listeners) {
				// Raise the event to parent
				listener.FrameStockBalanceChange_clickSave();
			}

			m_iCurrentItemListItemIndex = -1;
			m_iCurrentItemListFieldIndex = -1;
			m_oFrameNumberPad.clearEnterSubmitId();
			m_oFrameItemList.clearEditField();
			
			// Set "Save" button to unclicked
			this.setSaveButtonEnabled(false);
			
			bMatchChild = true;
		} else if (m_oButtonSearchByName.getId() == iChildId) {
			for (FrameStockBalanceChangeListener listener : listeners) {
				// Raise the event to parent
				listener.FrameStockBalanceChange_clickSearchByName(m_oTextboxSearchValue.getValue());
			}
			this.updateSearchButtonColor();
			bMatchChild = true;
		} else if (m_oButtonSearchByDepartment.getId() == iChildId) {
			for (FrameStockBalanceChangeListener listener : listeners) {
				// Raise the event to parent
				listener.FrameStockBalanceChange_clickSearchByDepartment();
			}
			bMatchChild = true;
		} else if (m_oButtonSearchByCategory.getId() == iChildId) {
			for (FrameStockBalanceChangeListener listener : listeners) {
				// Raise the event to parent
				listener.FrameStockBalanceChange_clickSearchByCategory();
			}
			bMatchChild = true;
		} else if (m_oButtonShowAllResult.getId() == iChildId) {
			for (FrameStockBalanceChangeListener listener : listeners) {
				// Raise the event to parent
				listener.FrameStockBalanceChange_clickShowAllResult();
			}
			this.updateSearchButtonColor();
			bMatchChild = true;
		} else if (iChildId == m_oImgButtonNextPage.getId()) {
			for (FrameStockBalanceChangeListener listener : listeners) {
				listener.FrameStockBalanceChange_clickNextPage();
			}
			bMatchChild = true;
		} else if (iChildId == m_oImgButtonPrevPage.getId()) {
			for (FrameStockBalanceChangeListener listener : listeners) {
				listener.FrameStockBalanceChange_clickPrevPage();
			}
			bMatchChild = true;
		} else if (iChildId == m_oButtonDate.getId()) {
			for (FrameStockBalanceChangeListener listener : listeners) {
				int iYear = Integer.parseInt(m_oTextboxYrValue.getValue());
				int iMonth = Integer.parseInt(m_oTextboxMthValue.getValue());
				int iDay = Integer.parseInt(m_oTextboxDayValue.getValue());

				Calendar calendar = Calendar.getInstance();
				calendar.set(iYear, iMonth - 1, iDay);

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
				Date date = calendar.getTime();

				listener.FrameStockBalanceChange_clickDate(dateFormat.format(date));
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
		else {
			if (m_oTextboxYrValue.getValue().isEmpty())
				m_oTextboxYrValue.setFocus();
			else if (m_oTextboxMthValue.getValue().isEmpty())
				m_oTextboxMthValue.setFocus();
			else if (m_oTextboxDayValue.getValue().isEmpty())
				m_oTextboxDayValue.setFocus();
			return;
		}
		
		//Move to next field index
		Boolean bSetNextField = true;
		switch(m_iCurrentItemListFieldIndex) {
			case 3:
				if(m_iCurrentItemListItemIndex < m_oFrameItemList.getItemCellCount(0)-1) {
					m_iCurrentItemListFieldIndex = 3;
					m_iCurrentItemListItemIndex++;
				} else {			//Last row
					bSetNextField = false;
					m_iCurrentItemListItemIndex = -1;
					m_iCurrentItemListFieldIndex = -1;
				}
				break;
			default:
				m_iCurrentItemListFieldIndex = 3;
				break;
		}
		
		for (FrameStockBalanceChangeListener listener : listeners) {
			// Raise the event to parent
			listener.FrameStockBalanceChange_clickBasketMenuItem(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, m_iCurrentItemListFieldIndex, sPrevValue);
		}
		
		//Move to new field to edit
		m_oFrameNumberPad.clearEnterSubmitId();
		if(bSetNextField) {
			VirtualUIBasicElement oElement = m_oFrameItemList.setEditField(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, m_iCurrentItemListFieldIndex);
			m_oFrameNumberPad.setEnterSubmitId(oElement);
			m_oButtonSave.addClickServerRequestSubmitElement(oElement);
		}
		else
			m_oFrameItemList.clearEditField();
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
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		updatePreviousFieldValue();
		//Update indexes in FormStockOperation
		String sPrevValue = null;
		if(m_iCurrentItemListItemIndex >= 0 && m_iCurrentItemListFieldIndex >= 0) {
			sPrevValue = m_oFrameItemList.getFieldValue(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, m_iCurrentItemListFieldIndex);
		}
		for (FrameStockBalanceChangeListener listener : listeners) {
			// Raise the event to parent
			listener.FrameStockBalanceChange_clickBasketMenuItem(iSectionIndex, iItemIndex, iFieldIndex, sPrevValue);
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
		for (FrameStockBalanceChangeListener listener : listeners) {
			// Raise the event to parent
			listener.FrameStockBalanceChange_clickExit();
		}
	}
	
}
