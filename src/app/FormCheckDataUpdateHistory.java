package app;

import java.util.ArrayList;
import core.Controller;
import om.SystemDataProcessLog;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

public class FormCheckDataUpdateHistory extends VirtualUIForm implements FrameCheckDataUpdateHistoryListiner {
	private TemplateBuilder m_oTemplateBuilder;
	private FrameCheckDataUpdateHistory m_oFrameCheckDataUpdateHistory;
	
	public FormCheckDataUpdateHistory(Controller oParentController) {
		super(oParentController);
		// TODO Auto-generated constructor stub
	}
	public boolean init(ArrayList<SystemDataProcessLog> oDataProcessLogsJSONArrayList ) {
		
		m_oTemplateBuilder = new TemplateBuilder();
		m_oTemplateBuilder.loadTemplate("frmCheckDataUpdateHistory.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameCheckDataUpdateHistory = new FrameCheckDataUpdateHistory();
		m_oTemplateBuilder.buildFrame(m_oFrameCheckDataUpdateHistory, "fraCheckDataUpdateHistory");
		m_oFrameCheckDataUpdateHistory.addListener(this);
		m_oFrameCheckDataUpdateHistory.init(oDataProcessLogsJSONArrayList);
		this.attachChild(m_oFrameCheckDataUpdateHistory);
		return true;
	}
	
	@Override
	public void frameCheckDataUpdateHistory_finishFrame() {
		// TODO Auto-generated method stub
		this.finishShow();
	}
}
