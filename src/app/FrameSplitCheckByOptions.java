package app;

import commonui.FrameNumberPad;
import commonui.FrameNumberPadListener;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;

import java.util.ArrayList;

import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FrameSplitCheckByOptionsListener {
	void FrameSplitCheckByOptions_clickExit();
	void FrameSplitCheckByOptions_askTableInfo();
	void FrameSplitCheckByOptions_clickOK();
}

public class FrameSplitCheckByOptions extends VirtualUIFrame implements FrameCommonBasketListener, FrameNumberPadListener, FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;

	private FrameTitleHeader m_oFrameTitleHeader;
	
	private VirtualUILabel m_oLabelNumberOfTable;
	
	private VirtualUIFrame m_oFrameLeftFrame;
	private VirtualUIFrame m_oFrameRightFrame;
	
	private FrameCommonBasket m_oFrameTableInfoList;
	private VirtualUIFrame m_oFrameInput;
	private VirtualUILabel m_oLabelSubTitle;
	private VirtualUIFrame m_oFrameSubTitleUnderline;
	private VirtualUILabel m_oLabelDescription;
	private VirtualUITextbox m_oTextboxValue;
	private FrameNumberPad m_oFrameNumberPad;
	
	private static String TIMER_NAME = "ask_table_info";
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameSplitCheckByOptionsListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameSplitCheckByOptionsListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameSplitCheckByOptionsListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameSplitCheckByOptions() {
		listeners = new ArrayList<FrameSplitCheckByOptionsListener>();
		
		m_oTemplateBuilder = new TemplateBuilder();

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraSplitCheckByOptions.xml");
		
		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(true);
		m_oFrameTitleHeader.addListener(this);
		this.attachChild(m_oFrameTitleHeader);
		
		//Left frame
		m_oFrameLeftFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftFrame, "fraLeftFrame");
		m_oFrameLeftFrame.bringToTop();
		this.attachChild(m_oFrameLeftFrame);
		
		//Right frame
		m_oFrameRightFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameRightFrame, "fraRightFrame");
		m_oFrameRightFrame.bringToTop();
		this.attachChild(m_oFrameRightFrame);
		
		// Result list
		m_oFrameTableInfoList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFrameTableInfoList, "fraTableInfoList");
		m_oFrameTableInfoList.init();
		m_oFrameTableInfoList.addListener(this);
		this.attachChild(m_oFrameTableInfoList);
		
		m_oFrameInput = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameInput, "fraInput");
		m_oFrameInput.setVisible(false);
		this.attachChild(m_oFrameInput);
		
		m_oLabelSubTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSubTitle, "lblSubTitle");
		m_oFrameInput.attachChild(m_oLabelSubTitle);
		
		m_oFrameSubTitleUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameSubTitleUnderline, "fraSubTitleUnderline");
		m_oFrameInput.attachChild(m_oFrameSubTitleUnderline);
		
		m_oLabelDescription = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDescription, "lblDescription");
		m_oFrameInput.attachChild(m_oLabelDescription);
		
		m_oTextboxValue = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxValue, "txtboxValue");
		m_oFrameInput.attachChild(m_oTextboxValue);
		
		// Number pad
		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.setEnterBlockUI(true);
		m_oFrameNumberPad.addListener(this);
		m_oFrameNumberPad.clearEnterSubmitId();
		m_oFrameNumberPad.setEnterSubmitId(m_oTextboxValue);
		m_oFrameInput.attachChild(m_oFrameNumberPad);
	}
	
	// Set visible for the input frame on the right side
	public void setVisibleForInputFrame(boolean bIsVisible){
		m_oFrameInput.setVisible(bIsVisible);
		m_oTextboxValue.setFocusWhenShow(true);
	}
	
	// Create update basket timer
	public void addFinishShowTimer(){
		this.addTimer(TIMER_NAME, 0, false, "", true, false, null);
	}
	
	// Start to update basket
	public void startFinishShowTimer(){
		this.controlTimer(TIMER_NAME, true);
	}
	
	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if(iId == super.getIDForPosting().hashCode()) {
			// Ask drawing basket
			//Set the last client socket ID
			AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
			
			this.askTableInfo();
			
			// Send the UI packet to client and the thread is finished
			super.getParentForm().finishUI(true);

			return true;
		}
		
		return false;
	}

	public void askTableInfo() {
		for (FrameSplitCheckByOptionsListener listener : listeners) {
			// Raise the event to parent
			listener.FrameSplitCheckByOptions_askTableInfo();
			break;
		}
		
	}

	public void setTitle(String sTitle) {
		m_oFrameTitleHeader.setTitle(sTitle);
	}

	public void setNumberOfTableDesc(String sValue) {
		m_oLabelNumberOfTable.setValue(sValue);
	}
	
	public void setLabelSubTitle(String sValue) {
		m_oLabelSubTitle.setValue(sValue);
	}
	
	public void setLabelDescription(String sValue) {
		m_oLabelDescription.setValue(sValue);
	}
	
	public int getFrameTableInforListSize(){
		return m_oFrameTableInfoList.getItemCount(0);
	}
	
	public String getTextboxValue() {
		return m_oTextboxValue.getValue();
	}
	
	public void setTextboxValue(String sValue) {
		m_oTextboxValue.setValue(sValue);
	}
	
	public void clearTextboxValue() {
		m_oTextboxValue.setValue("");
	}
	
	public void setFocusTextboxValue() {
		m_oTextboxValue.setFocusWhenShow(true);
		m_oTextboxValue.setFocus();
	}
	
	public void addHeader(ArrayList<Integer> iFieldWidths, ArrayList<String> sFieldValues) {
		m_oFrameTableInfoList.addHeader(iFieldWidths, sFieldValues);
		m_oFrameTableInfoList.setHeaderFormat(48, 24, "10,0,0,4");
		m_oFrameTableInfoList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		m_oFrameTableInfoList.setBottomUnderlineVisible(false);
	}

	public void addTableInfo(int iSectionId, int iItemIndex, ArrayList<Integer> iFieldWidths, ArrayList<String> sFieldValues, ArrayList<String> sFieldAligns, ArrayList<String> sFieldTypes) {
		m_oFrameTableInfoList.addItem(iSectionId, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
		m_oFrameTableInfoList.setFieldPadding(iSectionId, iItemIndex, 0, "10,0,0,4");
		m_oFrameTableInfoList.setFieldPadding(iSectionId, iItemIndex, 1, "10,0,0,4");
		m_oFrameTableInfoList.setFieldPadding(iSectionId, iItemIndex, 2, "10,0,0,4");
		m_oFrameTableInfoList.moveScrollToItem(iSectionId, iItemIndex);
	}

	public void updateTableInfo(int iSectionId, int iItemIndex, int iFieldIndex, String sValue) {
		m_oFrameTableInfoList.setFieldValue(iSectionId, iItemIndex, iFieldIndex, sValue);
	}
	
	public void removeLastTableInfo(int iSectionId, int iItemIndex) {
		m_oFrameTableInfoList.removeItem(iSectionId, iItemIndex);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
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
	
	@Override
	public void FrameNumberPad_clickEnter() {
		for (FrameSplitCheckByOptionsListener listener : listeners) {
			// Raise the event to parent
			listener.FrameSplitCheckByOptions_clickOK();
			break;
		}
		this.setVisible(false);
		this.setVisible(true);
		this.setFocusTextboxValue();
	}

	@Override
	public void FrameNumberPad_clickCancel() {
		for (FrameSplitCheckByOptionsListener listener : listeners) {
			// Raise the event to parent
			listener.FrameSplitCheckByOptions_clickExit();
		}
	}

	@Override
	public void FrameNumberPad_clickNumber(String string) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void FrameTitleHeader_close() {
		for (FrameSplitCheckByOptionsListener listener : listeners) {
			// Raise the event to parent
			listener.FrameSplitCheckByOptions_clickExit();
		}
	}
}
