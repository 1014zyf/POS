package commonui;

import java.util.ArrayList;

import core.Controller;

import app.AppGlobal;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormSelectionBox extends VirtualUIForm implements FrameSelectionBoxListener {
	private boolean m_bMultiSelect;
	private ArrayList<Integer> m_oResultList;
	private boolean m_bUserCancel;
	
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameSelectionBox m_oFrameSelectionBox;
	
	public FormSelectionBox(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		m_oResultList = new ArrayList<Integer>();
		m_bUserCancel = false;
		
		m_bMultiSelect = false;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmSelectionBox.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// SelectionBox Frame
		m_oFrameSelectionBox = new FrameSelectionBox();
		m_oTemplateBuilder.buildFrame(m_oFrameSelectionBox, "fraSelectionBox");
		m_oFrameSelectionBox.init();
		
		// Add listener
		m_oFrameSelectionBox.addListener(this);
		this.attachChild(m_oFrameSelectionBox);	
	}
	
	public void initWithSingleSelection(String sTitleValue, ArrayList<String> oOptionList, Boolean bHiddenCancelButton) {
		m_bMultiSelect = false;
		
		this.setTitle(sTitleValue);
		this.setOption(oOptionList);
		
		// SelectionBox Cancel Button
		if(!bHiddenCancelButton)
			m_oFrameSelectionBox.addButton(AppGlobal.g_oLang.get()._("cancel"), FrameSelectionBox.BUTTON_KEY_CANCEL);
	}
	
	public void initWithMultiSelection(String sTitleValue, ArrayList<String> oOptionList) {
		m_bMultiSelect = true;
		
		this.setTitle(sTitleValue);
		this.setOption(oOptionList);
		
		// SelectionBox SelectAll Button
		m_oFrameSelectionBox.addButton(AppGlobal.g_oLang.get()._("select_all"), FrameSelectionBox.BUTTON_KEY_SELECT);
	}
	
	public void addButton(String sDesc, String sKey) {
		m_oFrameSelectionBox.addHalfButton(sDesc, sKey);
	}
	
	public void setTitle(String sTitle) {
		m_oFrameSelectionBox.setTitle(sTitle);
	}
	
	public void setOption(ArrayList<String> oOptionList) {
		for (String sOptValue : oOptionList) {
			m_oFrameSelectionBox.addOption(sOptValue);
		}
	}
	
	public void setOptionTextAlign(String sAlign) {
		m_oFrameSelectionBox.setOptionTextAlign(sAlign);
	}
		
	public boolean isMultiSelect() {
		return m_bMultiSelect;
	}
		
	public ArrayList<Integer> getResultList() {
		return m_oResultList;
	}
	
	public void setResultList(ArrayList<Integer> oResultList) {
		m_oResultList = oResultList;
	}
	
	public boolean isUserCancel() {
		return m_bUserCancel;
	}
	
	@Override
	public void FrameSelectionBox_LabelSelected(int iOptIndex) {
		if (m_bMultiSelect) {
			Boolean bSelected = false;
			// Unselect the selected option
			for (int iCount=0; iCount<m_oResultList.size(); iCount++) {
				int iResultOpt = m_oResultList.get(iCount);
				if (iResultOpt == iOptIndex) {
					bSelected = true;
					m_oResultList.remove(iCount);
					m_oFrameSelectionBox.unselectOption(iOptIndex);
					break;
				}
			}
			// Select the option
			if (!bSelected) {
				m_oResultList.add(iOptIndex);
				m_oFrameSelectionBox.selectOption(iOptIndex);
			}
		}
		else {
			m_oResultList.add(iOptIndex);
			this.finishShow();
		}
	}
	
	@Override
	public void FrameSelectionBox_ButtonClicked(int iId, String sValue) {
		if (sValue.equals(FrameSelectionBox.BUTTON_KEY_SELECT)) {
			m_oResultList.clear();
			
			int iOptionSize = m_oFrameSelectionBox.getOptionListSize();
			for (int iCount=0; iCount<iOptionSize; iCount++) {
				m_oResultList.add(iCount);
			}

			m_oFrameSelectionBox.selectAllOptions();
		}
		else if (sValue.equals(FrameSelectionBox.BUTTON_KEY_CANCEL)) {
			m_bUserCancel = true;
			this.finishShow();
		}
	}
}
