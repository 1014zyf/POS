package app;

import java.util.ArrayList;

import commonui.FormDialogBox;
import core.Controller;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;


public class FormCommonBasketSelection extends VirtualUIForm implements FrameCommonBasketSelectionListener {
	private boolean m_bUserCancel;
	private boolean m_bMultiSelect;
	private ArrayList<Integer> m_oResultList;
	
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameCommonBasketSelection m_oFrameCommonBasketSelection;

	public FormCommonBasketSelection(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		m_oResultList = new ArrayList<Integer>();
		m_bMultiSelect = false;
		m_bUserCancel = false;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmCommonBasketSelection.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// SelectionBox Frame
		m_oFrameCommonBasketSelection = new FrameCommonBasketSelection();
		m_oTemplateBuilder.buildFrame(m_oFrameCommonBasketSelection, "fraCommonBasketSelection");
		m_oFrameCommonBasketSelection.init();
		
		// Add listener
		m_oFrameCommonBasketSelection.addListener(this);
		this.attachChild(m_oFrameCommonBasketSelection);
	}

	public void initWithMultiSelection(String sTitleValue, ArrayList<Integer> iHeaderFieldWidths, ArrayList<String> sHeaderFieldValues) {
		m_bMultiSelect = true;
		
		m_oFrameCommonBasketSelection.setTitle(sTitleValue);
		m_oFrameCommonBasketSelection.addHeader(iHeaderFieldWidths, sHeaderFieldValues);
		m_oFrameCommonBasketSelection.setMutliSelection(true);
	}
	
	public void addItem(int iItemIndex, ArrayList<Integer> iFieldWidths, ArrayList<String> sFieldValues, ArrayList<String> sFieldAligns, ArrayList<String> sFieldTypes, ArrayList<VirtualUIBasicElement> oSubmitIdElements) {
		m_oFrameCommonBasketSelection.addItem(iItemIndex, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, oSubmitIdElements);
	}
	
	public ArrayList<Integer> getResultList() {
		return m_oResultList;
	}

	public boolean isUserCancel() {
		return m_bUserCancel;
	}
	
	@Override
	public void frameCommonBasketSelection_RowSelected(int iItemIndex) {
		if (m_bMultiSelect) {
			if(m_oResultList.contains(iItemIndex)) {
				m_oFrameCommonBasketSelection.setRowColor(iItemIndex, FrameCommonBasketSelection.COLOR_UNSELECTED);
				m_oResultList.remove(Integer.valueOf(iItemIndex));
			} else {
				m_oFrameCommonBasketSelection.setRowColor(iItemIndex, FrameCommonBasketSelection.COLOR_SELECTED);
				m_oResultList.add(iItemIndex);
			}
		} else {
			m_oResultList.add(iItemIndex);
			this.finishShow();
		}
	}

	@Override
	public void frameCommonBasketSelection_closeClicked() {
		m_bUserCancel = true;
		this.finishShow();
	}

	@Override
	public void frameCommonBasketSelection_selectAll(boolean bSelectAll) {
		if(bSelectAll) {
			m_oResultList.clear();
			for(int i = 0; i < m_oFrameCommonBasketSelection.getItemCellCount(); i++) {
				m_oFrameCommonBasketSelection.setRowColor(i, FrameCommonBasketSelection.COLOR_SELECTED);
				m_oResultList.add(i);
			}
		} else {
			for(int i = 0; i < m_oFrameCommonBasketSelection.getItemCellCount(); i++) {
				m_oFrameCommonBasketSelection.setRowColor(i, FrameCommonBasketSelection.COLOR_UNSELECTED);
			}
			m_oResultList.clear();
		}
	}


	@Override
	public void frameCommonBasketSelection_confirmClicked() {
		if(m_oResultList.isEmpty()) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
            oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
            oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("please_select_at_least_one_item"));
            oFormDialogBox.show();
            return;
		}
		
		this.finishShow();
	}
}
