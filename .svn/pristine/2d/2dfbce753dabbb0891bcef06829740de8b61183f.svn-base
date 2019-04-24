package commonui;

import java.util.ArrayList;

import core.Controller;

import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormListMessageBox extends VirtualUIForm implements FrameListMessageBoxListener {
	private TemplateBuilder m_oTemplateBuilder;
	private FrameListMessageBox m_oFrameListMessageBox;
	
	private String m_sResult;
	
	public FormListMessageBox(boolean bHeaderDisplay, Controller oParentController) {
		super(oParentController);
		init(bHeaderDisplay);
	}
	
	public FormListMessageBox(boolean bHeaderDisplay, String sBtnValue, Controller oParentController) {
		super(oParentController);
		
		init(bHeaderDisplay);
		// Create a button
		m_oFrameListMessageBox.addSingleButton(sBtnValue);
	}
	
	private boolean init(boolean bHeaderDisplay) {
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmListMessagebox.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameListMessageBox = new FrameListMessageBox();
		m_oTemplateBuilder.buildFrame(m_oFrameListMessageBox, "fraListMessagebox");
		m_oFrameListMessageBox.init(bHeaderDisplay);
		
		// Add listener
		m_oFrameListMessageBox.addListener(this);
		this.attachChild(m_oFrameListMessageBox);
		
		return true;
	}
	
	public void addColumnHeader(String sColumnHeader , int iWidth){
		m_oFrameListMessageBox.addColumnHeader(sColumnHeader , iWidth);
	}
	
	public void setHeaderTextSize(int iTextSize) {
		m_oFrameListMessageBox.setHeaderTextSize(iTextSize);
	}
	
	public void setMessageTextSize(int iTextSize) {
		m_oFrameListMessageBox.setMessageTextSize(iTextSize);
	}
	
	public void setMessageTextAlign(String sAlign) {
		m_oFrameListMessageBox.setMessageTextAlign(sAlign);
	}
	
	public void setMessageTextAlign(ArrayList<String> sAligns) {
		m_oFrameListMessageBox.setMessageTextAlign(sAligns);
	}
	
	public void setMessagePadding(String sPadding) {
		m_oFrameListMessageBox.setMessagePadding(sPadding);
	}
	
	public void setMessagePadding(ArrayList<String> sPaddings) {
		m_oFrameListMessageBox.setMessagePadding(sPaddings);
	}
	
	public void setMessageColor(int iIndex, String sBackground, String sForeground){
		m_oFrameListMessageBox.setMessageColor(iIndex, sBackground, sForeground);
	}
	
	public void addMessage(ArrayList<String> sMessage){
		m_oFrameListMessageBox.addMessage(sMessage);
	}
	
	public void addMessageWithColumnOfTwo(String sTitle, String sMessage){
		ArrayList<String> sContentList = new ArrayList<String>();
		sContentList.add(sTitle);
		sContentList.add(sMessage);
		m_oFrameListMessageBox.addMessage(sContentList);
	}
	
	public void setTitle(String sTitle){
		m_oFrameListMessageBox.setTitle(sTitle);
	}
	
	public void setDelayTime(float fDelayTime){
		m_oFrameListMessageBox.setDelayTime(fDelayTime);
	}
	
	public String getResult(){
		return m_sResult;
	}
	
	public void setResult(String sResult){
		m_sResult = sResult;
	}
	
	public void setCloseButtonVisible(boolean bShow) {
		m_oFrameListMessageBox.setCloseButtonVisible(bShow);
	}
	
	public void setFrameMessageHeight(int iHeight) {
		m_oFrameListMessageBox.setFrameMessageHeight(iHeight);
	}
	
	@Override
	public void FrameListMessageBox_click(String sResult) {
		m_sResult = sResult;
		this.finishShow();
	}
}
