package app.commonui;

import java.util.ArrayList;

import core.Controller;

import core.templatebuilder.TemplateBuilder;
import core.virtualui.*;

public class FormListMessageBox extends Controller implements FrameListMessageBoxListener {
	private String m_sResult;
	
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameListMessageBox m_oFrameListMessageBox;
	
	public FormListMessageBox(Controller oParentController) {
		super(oParentController);
		
		init();
	}

	public FormListMessageBox(String sBtnValue, Controller oParentController) {
		super(oParentController);
		
		init();
		// Create a button
		m_oFrameListMessageBox.addSingleButton(sBtnValue);
	}
	
	private boolean init() {
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmListMessagebox.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameListMessageBox = new FrameListMessageBox();
		m_oTemplateBuilder.buildFrame(m_oFrameListMessageBox, "fraListMessagebox");
		m_oFrameListMessageBox.init();
		
		// Add listener
		m_oFrameListMessageBox.addListener(this);
		this.attachChild(m_oFrameListMessageBox);
		
		return true;
	}
	
	public void addColumnHeader(String sColumnHeader , int iWidth){
		m_oFrameListMessageBox.addColumnHeder(sColumnHeader , iWidth);
	}
	
	public void addMessage(ArrayList<String> sMessage){
		m_oFrameListMessageBox.addMessage(sMessage);
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

	@Override
	public void FrameListMessageBox_click(String sResult) {
		m_sResult = sResult;
		this.finishShow();
	}
}
