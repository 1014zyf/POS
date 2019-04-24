package app;

import core.Controller;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormFirstPage extends VirtualUIForm implements FrameFirstPageListener {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameFirstPage m_oFrameFirstPage;  
	
	public FormFirstPage(Controller oParentController){
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmFirstPage.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Initial the frame to cover other frame
		m_oFrameFirstPage = new FrameFirstPage();
		m_oTemplateBuilder.buildFrame(m_oFrameFirstPage, "fraFirstPage");
		m_oFrameFirstPage.addListener(this);
		m_oFrameFirstPage.setVisible(true);
		this.attachChild(m_oFrameFirstPage);
	}
	
	@Override
	public void frameFirstPage_clickOK() {
		
		
		// Finish showing this form
		this.finishShow();
	}
}
