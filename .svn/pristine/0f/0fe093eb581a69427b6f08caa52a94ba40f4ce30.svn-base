package app.commonui;

import java.util.ArrayList;
import java.util.List;

import core.Controller;

import app.controller.AppGlobal;
import core.templatebuilder.TemplateBuilder;
import core.virtualui.*;

public class FormInputBox extends Controller implements FrameInputBoxListener {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUIFrame m_oFrameCover;
	private FrameInputBox m_oFrameInputBox;
	private String m_sInputValue;
	private List<String> m_oInputValuesList;
	private String m_sSwipeCardValue;
	private boolean m_bUserCancel;
	
	public FormInputBox(Controller oParentController){
		super(oParentController);
		
		m_sInputValue = "";
		m_sSwipeCardValue = "";
		m_bUserCancel = false;
	}
	
	public boolean init() {
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmInputBox.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameInputBox = new FrameInputBox();
		m_oTemplateBuilder.buildFrame(m_oFrameInputBox, "fraInputBox");
		m_oFrameInputBox.init();
		// Add listener;
		m_oFrameInputBox.addListener(this);
		m_oFrameInputBox.setVisible(true);		// Show this frame during first initial
		oCoverFrame.attachChild(m_oFrameInputBox);
		
		// Show keyboard
		//AppGlobal.g_oTerm.get().showKeyboard(true);
		
		return true;
	}
	
	public boolean initWithInputNum(int iInputNum) {
		m_oTemplateBuilder = new TemplateBuilder();
		m_oInputValuesList = new ArrayList<String>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmInputBox.xml");
		
		// Background Cover Page
		m_oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCover, "fraCoverFrame");
		this.attachChild(m_oFrameCover);
		
		m_oFrameInputBox = new FrameInputBox();
		m_oTemplateBuilder.buildFrame(m_oFrameInputBox, "fraInputBox");
		m_oFrameInputBox.initWithInputNum(iInputNum);
		if(m_oFrameInputBox.getHeight() < m_oFrameCover.getHeight() && (m_oFrameInputBox.getTop() + m_oFrameInputBox.getHeight()) >= m_oFrameCover.getHeight()) {
			int iNewInputBoxTop = (m_oFrameCover.getHeight() - m_oFrameInputBox.getHeight()) / 2;
			m_oFrameInputBox.setTop(iNewInputBoxTop);
		}
		// Add listener;
		m_oFrameInputBox.addListener(this);
		m_oFrameInputBox.setVisible(true);		// Show this frame during first initial
		m_oFrameCover.attachChild(m_oFrameInputBox);
		
		// Show keyboard
		//AppGlobal.g_oTerm.get().showKeyboard(true);
		
		return true;
	}
	
	public void setInputBoxTop(int iTop) {
		if(iTop > 0)
			m_oFrameInputBox.setTop(iTop);
	}

	public void setDefaultInputValue(String sDefaultValue){
		m_oFrameInputBox.setDefaultInputValue(sDefaultValue);
	}
	
	public String getInputValue() {
		return m_sInputValue;
	}
	
	public void setDefaultInputValue(int iIndex, String sDefaultValue) {
		m_oFrameInputBox.setDefaultInputValue(iIndex, sDefaultValue);
	}
	
	public String getInputValue(int iIndex) {
		try {
			return m_oInputValuesList.get(iIndex);
		}
		catch(Exception e) {
			return "";
		}
	}
	
	public String getSwipeCardValue() {
		return m_sSwipeCardValue;
	}
	
	public void setTitle(String sTitle){
		m_oFrameInputBox.setTitle(sTitle);
	}
	
	public void setMessage(String sMessage){
		m_oFrameInputBox.setMessage(sMessage);
	}
	
	public void setMessages(List<String> oMsgList) {
		m_oFrameInputBox.setMessages(oMsgList);
	}
	
	public void setKeyboardType(String sType){
		m_oFrameInputBox.setKeyboardType(sType);
	}
	
	public void setKeyboardType(int iIndex, String sType){
		m_oFrameInputBox.setKeyboardType(iIndex, sType);
	}
	
	public void setEnterBlockUI(boolean bBlockUI){
		m_oFrameInputBox.setEnterBlockUI(bBlockUI);
    }
	
	public void setInputBoxSecurity(int iBoxIndex, boolean bSecurity) {
		m_oFrameInputBox.setInputBoxSecurity(iBoxIndex, bSecurity);
	}
	
	public void showKeyboard() {
		m_oFrameInputBox.showKeyboard();
	}
	
	public boolean isUserCancel() {
		return m_bUserCancel;
	}
	
	public void forceShowNumberPad(){
		m_oFrameInputBox.forceShowNumberPad();
	}
	
	@Override
	public void FrameInputBox_clickOK() {
		if(m_oFrameInputBox.getInputTxtboxCount() > 1) {
			for(int i = 0; i < m_oFrameInputBox.getInputTxtboxCount(); i++) {
				String sInput = m_oFrameInputBox.getInputValue(i);
				m_oInputValuesList.add(sInput);
			}
		}
		else {
			m_sInputValue = m_oFrameInputBox.getInputValue();
		}
		
		
		// Finish showing this form
		this.finishShow();
		
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}
	
	@Override
	public void FrameInputBox_clickCancel() {
		if(m_oFrameInputBox.getInputTxtboxCount() > 1) {
			m_oInputValuesList.clear();
		}
		else {
			m_sInputValue = null;
		}
		m_bUserCancel = true;
				
		// Finish showing this form
		this.finishShow();
		
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}

	@Override
	public void FrameInputBoxListener_swipeCard(String sSwipeCardValue) {
		// Get swipe card value
		m_sSwipeCardValue = sSwipeCardValue;
		
		// Finish showing this form
		this.finishShow();
		
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}
}
