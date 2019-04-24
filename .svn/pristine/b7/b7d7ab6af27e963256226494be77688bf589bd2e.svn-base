package app;

import core.Controller;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormWebReport extends VirtualUIForm implements FrameWebReportListener {

	TemplateBuilder m_oTemplateBuilder;
	
	private FrameWebReport m_oFrameWebReport;
	
	public FormWebReport(Controller oParentController){
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmWebReport.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Check review frame
		m_oFrameWebReport = new FrameWebReport();
		m_oTemplateBuilder.buildFrame(m_oFrameWebReport, "fraWebReport");
		m_oFrameWebReport.addListener(this);
		this.attachChild(m_oFrameWebReport);
	}

	public void setCheckURL(String sURL){
		m_oFrameWebReport.setCheckURL(sURL);
	}
	
	@Override
	public void frameWebReport_clickOK() {
		// Finish showing this form
		this.finishShow();
	}
}
