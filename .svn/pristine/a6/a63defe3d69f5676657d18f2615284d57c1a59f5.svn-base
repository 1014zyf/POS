package app;

import java.util.ArrayList;

import externallib.StringLib;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;

import app.AppGlobal;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;

/** interface for the listeners/observers callback method */
interface FrameCommonBasketSelectionListener {
	void frameCommonBasketSelection_RowSelected(int iItemIndex);
	void frameCommonBasketSelection_closeClicked();
	void frameCommonBasketSelection_selectAll(boolean bSelectAll);
	void frameCommonBasketSelection_confirmClicked();
	
}

public class FrameCommonBasketSelection extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener {

	private TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oFrameTitleHeader;
	private FrameCommonBasket m_oFrameCommonBasket;
	private VirtualUIButton m_oButtonSelectAll;
	private VirtualUIButton m_oButtonConfirm;
	private VirtualUIButton m_oButtonCancel;
	
	public boolean m_bSelectAll;

	public static String COLOR_SELECTED = "#FF7F27";
	public static String COLOR_UNSELECTED = "#575757";

	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameCommonBasketSelectionListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameCommonBasketSelectionListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameCommonBasketSelectionListener listener) {
        listeners.remove(listener);
    }
    
	public void init() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameCommonBasketSelectionListener>();
		
		m_bSelectAll = true;

		// Load child elements from template
		m_oTemplateBuilder.loadTemplate("fraCommonBasketSelection.xml");

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
	    m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
	    m_oFrameTitleHeader.init(true);
	    m_oFrameTitleHeader.addListener(this);
	    this.attachChild(m_oFrameTitleHeader);
	    
		// Available Table List
		m_oFrameCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFrameCommonBasket, "fraCommonBasket");
		m_oFrameCommonBasket.init();
		m_oFrameCommonBasket.addListener(this);
		this.attachChild(m_oFrameCommonBasket);
    	
		// Select All button
		m_oButtonSelectAll = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSelectAll, "btnSelectAll");
		m_oButtonSelectAll.setValue(AppGlobal.g_oLang.get()._("select_all"));
		m_oButtonSelectAll.setVisible(false);
		this.attachChild(m_oButtonSelectAll);
    	
		// Confirm button
		m_oButtonConfirm = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonConfirm, "btnConfirm");
		m_oButtonConfirm.setValue(AppGlobal.g_oLang.get()._("confirm"));
		m_oButtonConfirm.setVisible(false);
		this.attachChild(m_oButtonConfirm);
		
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "btnCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		this.attachChild(m_oButtonCancel);
	}
	
	public void setTitle(String sTitle) {
		m_oFrameTitleHeader.setTitle(sTitle);
	}
	
	public void addHeader(ArrayList<Integer> iFieldWidths, ArrayList<String> sFieldValues) {
		m_oFrameCommonBasket.addHeader(iFieldWidths, sFieldValues);
		m_oFrameCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
	}
	
	public void setMutliSelection(boolean bMultiSelect) {
		int iFrameWidth = this.getWidth();
//KingsleyKwan20170918ByNick		-----Start-----
int iPadding = 22;
		
		if(bMultiSelect) {
			int iButtonCancelLeft = 30;
			int iButtonSelectAllLeft = iButtonCancelLeft + m_oButtonCancel.getWidth() + 520;
			int iButtonConfirmLeft = iButtonSelectAllLeft+m_oButtonSelectAll.getWidth()+iPadding;
		
			m_oButtonSelectAll.setLeft(iButtonSelectAllLeft);
			m_oButtonSelectAll.setVisible(true);
			
			m_oButtonConfirm.setLeft(iButtonConfirmLeft);
			m_oButtonConfirm.setVisible(true);
			
			m_oButtonCancel.setLeft(iButtonCancelLeft);
//KingsleyKwan20170918ByNick		----- End -----
		} else {
			int iButtonCancelLeft = (iFrameWidth - m_oButtonCancel.getWidth())/2;
			m_oButtonCancel.setLeft(iButtonCancelLeft);
		}
	}
	
	public void addItem(int iItemIndex, ArrayList<Integer> iFieldWidths, ArrayList<String> sFieldValues, ArrayList<String> sFieldAligns, ArrayList<String> sFieldTypes, ArrayList<VirtualUIBasicElement> oSubmitIdElements) {
		m_oFrameCommonBasket.addItem(0, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, oSubmitIdElements);
	}

	// Get number of item under the section
	public int getItemCellCount(){
		return m_oFrameCommonBasket.getItemCellCount(0);
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
//KingsleyKwan20170918ByNick		-----Start-----
		if(iChildId == m_oButtonCancel.getId()) {
//KingsleyKwan20170918ByNick		----- End -----
			for(FrameCommonBasketSelectionListener listener: listeners) {
				listener.frameCommonBasketSelection_closeClicked();
			}
			
			bMatchChild = true;
		} else if (iChildId == m_oButtonSelectAll.getId()) {
			for(FrameCommonBasketSelectionListener listener: listeners) {
				if(m_bSelectAll)
					m_oButtonSelectAll.setValue(AppGlobal.g_oLang.get()._("unselect_all"));
				else
					m_oButtonSelectAll.setValue(AppGlobal.g_oLang.get()._("select_all"));
				listener.frameCommonBasketSelection_selectAll(m_bSelectAll);
				break;
			}
        	m_bSelectAll = !m_bSelectAll;
			
			bMatchChild = true;
		} else if (iChildId == m_oButtonConfirm.getId()) {
			for(FrameCommonBasketSelectionListener listener: listeners) {
				listener.frameCommonBasketSelection_confirmClicked();
			}
			
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
		for(FrameCommonBasketSelectionListener listener: listeners) {
			listener.frameCommonBasketSelection_RowSelected(iItemIndex);
		}
	}

	public void setRowColor(int iItemIndex, String sBackgroundColor) {
		m_oFrameCommonBasket.setAllFieldsForegroundColor(0, iItemIndex, sBackgroundColor);
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
		for (FrameCommonBasketSelectionListener listener : listeners) {
			// Raise the event to parent
			listener.frameCommonBasketSelection_closeClicked();
		}
	}
}
