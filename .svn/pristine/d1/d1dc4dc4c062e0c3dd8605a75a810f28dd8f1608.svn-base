package app;

import java.util.ArrayList;

import commonui.FrameNumberPad;
import commonui.FrameNumberPadListener;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FrameStockEditSequenceListener {
	void FrameStockEditSequence_clickExit();
	void FrameStockEditSequence_clickBasketMenuItem(int iNewSectionId, int iNewItemIndex, int iNewFieldIndex);
	void FrameStockEditSequence_clickUp(int iCurrSectionId, int iCurrItemIndex);
	void FrameStockEditSequence_clickDown(int iCurrSectionId, int iCurrItemIndex);
	void FrameStockEditSequence_clickToSeq(int iCurrSectionId, int iCurrItemIndex, String sTargetSeq);
}

public class FrameStockEditSequence extends VirtualUIFrame implements FrameNumberPadListener, FrameCommonBasketListener, FrameTitleHeaderListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUIFrame m_oFrameLeftContent;
	private VirtualUIFrame m_oFrameRightContent;
	
	private FrameCommonBasket m_oFrameItemList;
	
	private VirtualUIButton m_oButtonUp;
	private VirtualUIButton m_oButtonDown;
	private VirtualUILabel m_oLblSeq;
	private VirtualUITextbox m_oTextboxSeqValue;
	private FrameNumberPad m_oFrameNumberPad;
	
	private int m_iCurrentItemListSection;
	private int m_iCurrentItemListItemIndex;
	private int m_iCurrentItemListFieldIndex;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameStockEditSequenceListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameStockEditSequenceListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameStockEditSequenceListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameStockEditSequence(){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameStockEditSequenceListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraStockEditSequence.xml");

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(true);
		m_oFrameTitleHeader.addListener(this);
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("edit_sequence"));
		this.attachChild(m_oFrameTitleHeader);
		
		VirtualUIFrame oFrameLeftCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameLeftCover, "fraLeftContent");
		oFrameLeftCover.setTop(oFrameLeftCover.getTop() + 100);
		oFrameLeftCover.setHeight(oFrameLeftCover.getHeight() - 99);
		oFrameLeftCover.setCornerRadius("0");
		this.attachChild(oFrameLeftCover);
		
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
		iFieldWidths.add(150);
		sFieldValues.add(AppGlobal.g_oLang.get()._("sequence"));
		iFieldWidths.add(380);
    	sFieldValues.add(AppGlobal.g_oLang.get()._("item_description"));
    	
    	m_oFrameItemList.addHeader(iFieldWidths, sFieldValues);
    	m_oFrameItemList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
    	m_oFrameItemList.setBottomUnderlineVisible(false);
		VirtualUIFrame oFrameRightCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameRightCover, "fraRightContent");
		oFrameRightCover.setTop(oFrameRightCover.getTop() + 100);
		oFrameRightCover.setHeight(oFrameRightCover.getHeight() - 99);
		oFrameRightCover.setCornerRadius("0");
		this.attachChild(oFrameRightCover);
    	
		//Create Right Content Frame
		m_oFrameRightContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameRightContent, "fraRightContent");
		this.attachChild(m_oFrameRightContent);
		
    	//Create Up Button
		m_oButtonUp = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonUp, "btnUp");
		m_oButtonUp.setValue(AppGlobal.g_oLang.get()._("move_up"));
		m_oButtonUp.setVisible(true);
		m_oFrameRightContent.attachChild(m_oButtonUp);
		
		//Create Down Button
		m_oButtonDown = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonDown, "btnDown");
		m_oButtonDown.setValue(AppGlobal.g_oLang.get()._("move_down"));
		m_oButtonDown.setVisible(true);
		m_oFrameRightContent.attachChild(m_oButtonDown);
		
		//Create Sequence Label
		m_oLblSeq = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLblSeq, "lblSeq");
    	m_oLblSeq.setValue(AppGlobal.g_oLang.get()._("to_sequence")+":");
    	m_oFrameRightContent.attachChild(m_oLblSeq);
    	
		// Search value box
    	m_oTextboxSeqValue = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxSeqValue, "txtboxSeqValue");
		m_oFrameRightContent.attachChild(m_oTextboxSeqValue);
				    	
		// Number pad
		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.setEnterBlockUI(true);
		m_oFrameNumberPad.clearEnterSubmitId();
		m_oFrameNumberPad.setEnterSubmitId(m_oTextboxSeqValue);
		m_oFrameNumberPad.addListener(this);
		m_oFrameRightContent.attachChild(m_oFrameNumberPad);
		
		// Init as -ve
		m_iCurrentItemListSection = -1;
		m_iCurrentItemListItemIndex = -1;
	}
	
	public void addRecord(int iSectionId, int iItemIndex, String sItemSeq, String sItemDesc) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();

		// Item Desc
		iFieldWidths.add(150);
		sFieldValues.add(sItemSeq);
		sFieldAligns.add("");
		iFieldWidths.add(380);
		sFieldValues.add(sItemDesc);
		sFieldAligns.add("");
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.LEFT);

		m_oFrameItemList.addItem(iSectionId, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);

		// Change the color of the fields that are not editable to grey
		m_oFrameItemList.setFieldBackgroundColor(iSectionId, iItemIndex, 2, "#D9D9D9");
	}
	
	public void changeValue(int iSectionId, int iItemIndex, String sItemDesc){
		m_oFrameItemList.setFieldValue(iSectionId, iItemIndex, 1, sItemDesc);
	}
	
	public void setResultLineVisible(ArrayList<Integer> oLineIndex, boolean bVisible){
		for(Integer iLineIndex:oLineIndex){
			m_oFrameItemList.setLineVisible(0, iLineIndex, bVisible);
		}
	}
	
	public void setCellFieldBackgroundColorEdited(int iSectionId, int iItemIndex, int iFieldIndex, boolean bEdited) {
		if(bEdited)
			m_oFrameItemList.setFieldBackgroundColor(iSectionId, iItemIndex, iFieldIndex, "#66A6F1");
		else
			m_oFrameItemList.setFieldBackgroundColor(iSectionId, iItemIndex, iFieldIndex, "#FFFFFF");
	}
	
	public void clearToSeqValue(){
		m_oTextboxSeqValue.setValue("");
	}
	
	public void clearAllRecords() {
		m_oFrameItemList.removeAllItems(0);
	}
	
	public Integer getCurrItemIdx() {
		return m_iCurrentItemListItemIndex;
	}
	
	public void setCurrItemIdx(int iCurrentItemListItemIndex) {
		m_iCurrentItemListItemIndex = iCurrentItemListItemIndex;
	}
	
	public void itemListScrollToIdx(int iItemIndex) {
		m_oFrameItemList.moveScrollToItem(0, iItemIndex);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		// Find the clicked button
		if (iChildId == m_oButtonUp.getId()) {
			for (FrameStockEditSequenceListener listener : listeners) {
				listener.FrameStockEditSequence_clickUp(m_iCurrentItemListSection, m_iCurrentItemListItemIndex);
			}
			bMatchChild = true;
		} else if (iChildId == m_oButtonDown.getId()) {
			for (FrameStockEditSequenceListener listener : listeners) {
				listener.FrameStockEditSequence_clickDown(m_iCurrentItemListSection, m_iCurrentItemListItemIndex);
			}
			bMatchChild = true;
		}

		return bMatchChild;
	}

	@Override
	public void FrameNumberPad_clickEnter() {
		for (FrameStockEditSequenceListener listener : listeners) {
			// Raise the event to parent
			listener.FrameStockEditSequence_clickToSeq(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, m_oTextboxSeqValue.getValue());
		}
	}

	@Override
	public void FrameNumberPad_clickCancel() {
		m_oTextboxSeqValue.setValue("");
		if (m_iCurrentItemListFieldIndex != 1)
			return;
		if (m_iCurrentItemListItemIndex != -1)
			setCellFieldBackgroundColorEdited(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, m_iCurrentItemListFieldIndex, false);
		m_iCurrentItemListItemIndex = -1;
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
		if (iFieldIndex == 0)
			return;
		
		m_iCurrentItemListFieldIndex = iFieldIndex;
		
		if (m_iCurrentItemListItemIndex == iItemIndex) {
			setCellFieldBackgroundColorEdited(iSectionIndex, iItemIndex, iFieldIndex, false);
			m_iCurrentItemListItemIndex = -1;
		}
		else {
			if (m_iCurrentItemListItemIndex != -1)
				setCellFieldBackgroundColorEdited(iSectionIndex, m_iCurrentItemListItemIndex, iFieldIndex, false);
			setCellFieldBackgroundColorEdited(iSectionIndex, iItemIndex, iFieldIndex, true);
			
			m_iCurrentItemListItemIndex = iItemIndex;
			
			m_oFrameNumberPad.clearEnterSubmitId();
			m_oFrameNumberPad.setEnterSubmitId(m_oTextboxSeqValue);
			m_oTextboxSeqValue.setFocus();
		}
		
		m_iCurrentItemListSection = iSectionIndex;
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
		for (FrameStockEditSequenceListener listener : listeners) {
			// Raise the event to parent
			listener.FrameStockEditSequence_clickExit();
		}
	}
	
}
