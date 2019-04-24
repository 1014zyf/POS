package app;

import core.Controller;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormLargePopUpPanel extends VirtualUIForm implements FrameLargePopUpPanelListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameLargePopUpPanel m_oFrameLargePopUpPanel;
	private VirtualUIFrame m_oFrameCover;
	
	public FormLargePopUpPanel(String[] sTitle, String[] sDesc, Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmLargePopUpPanel.xml");
		
		// Background Cover Page
		m_oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCover, "fraCoverFrame");
		this.attachChild(m_oFrameCover);
		
		// FrameLargePopUpPanel
		m_oFrameLargePopUpPanel = new FrameLargePopUpPanel(sTitle, sDesc);
		m_oTemplateBuilder.buildFrame(m_oFrameLargePopUpPanel, "fraLargePopUpPanel");
		m_oFrameLargePopUpPanel.addListener(this);
		this.attachChild(m_oFrameLargePopUpPanel);
	}
	
	public void setPaymentImage(String sName){
		m_oFrameLargePopUpPanel.setPaymentImage(sName);
	}
	
	public void addFinishShowTimer(int iTimeoutSecond) {
		m_oFrameLargePopUpPanel.addFinishShowTimer(iTimeoutSecond);
		m_oFrameLargePopUpPanel.startFinishShowTimer(true);
	}
	
	@Override
	public void frameLargePopUpPanel_CloseImageClicked() {
		this.finishShow();
		m_oFrameCover.setVisible(false);
	}

	@Override
	public void FrameLargePopUpPanelListener_timeout() {
		this.finishShow();
		m_oFrameCover.setVisible(false);
	}
}
