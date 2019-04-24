package commonui;

import core.Controller;

import java.util.ArrayList;
import java.util.List;

import app.AppGlobal;
import commonui.FormDialogBox;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormInputBoxList extends VirtualUIForm implements FrameInputBoxListListener {

	TemplateBuilder m_oTemplateBuilder;
	private VirtualUIFrame m_oFrameCover;
	private List<String> m_oInputValuesList;
	private FrameInputBoxList m_oFrameInputBoxList;
	private boolean m_bUserCancel;
	
	public FormInputBoxList(Controller oParentController) {
		super(oParentController);
		// TODO Auto-generated constructor stub
	}
	
	public boolean init(int iInputNum) {
		m_oTemplateBuilder = new TemplateBuilder();
		m_oInputValuesList = new ArrayList<String>();
		m_bUserCancel = false;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmInputBoxList.xml");
		
		// Background Cover Page
		m_oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCover, "fraCoverFrame");
		this.attachChild(m_oFrameCover);
		
		m_oFrameInputBoxList = new FrameInputBoxList();
		m_oTemplateBuilder.buildFrame(m_oFrameInputBoxList, "fraInputBoxList");
		m_oFrameInputBoxList.init(iInputNum);
		
		// Add listener;
		m_oFrameInputBoxList.addListener(this);
		m_oFrameInputBoxList.setVisible(true);		// Show this frame during first initial
		m_oFrameCover.attachChild(m_oFrameInputBoxList);
		return true;
	}
	
	public void setTitle(String sTitle){
		m_oFrameInputBoxList.setTitle(sTitle);
	}
	
	public void setMessages(List<String> oMsgList) {
		m_oFrameInputBoxList.setMessages(oMsgList);
	}
	
	public boolean isUserCancel() {
		return m_bUserCancel;
	}
	
	public void setInputValue(String[] InputValue) {
		m_oFrameInputBoxList.setInputValue(InputValue);
	}
	
	public String getInputValue(int iIndex) {
		try {
			return m_oInputValuesList.get(iIndex);
		}
		catch(Exception e) {
			return "";
		}
	}
	
	public void setTxtBoxFocus(int iIndex, boolean bFocus) {
		m_oFrameInputBoxList.setTxtBoxFocus(iIndex, bFocus);
	}
	
	@Override
	public void FrameInputBoxList_clickOK() {
		if(m_oFrameInputBoxList.getInputTxtboxCount() > 1) {
			for(int i = 0; i < m_oFrameInputBoxList.getInputTxtboxCount(); i++) {
				String sInput = m_oFrameInputBoxList.getInputValue(i);
				m_oInputValuesList.add(sInput);
			}
		}
		
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public void FrameInputBoxList_clickCancel() {
		m_oInputValuesList.clear();
		m_bUserCancel = true;
	// Finish showing this form
		this.finishShow();
	}

}

