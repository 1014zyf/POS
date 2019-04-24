package commonui;

import core.Controller;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormProcessBox extends VirtualUIForm implements FrameProcessBoxListener {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameProcessBox m_oFrameProcessBox;
	
	public FormProcessBox(Controller oParentController) {
		super(oParentController);
		
		init(false);
	}
	
	public FormProcessBox(Controller oParentController, boolean bSetMaximumClientTimeout) {
		super(oParentController);
		
		init(bSetMaximumClientTimeout);
	}
	
	private boolean init(boolean bSetMaximumClientTimeout) {
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmDialogBox.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameProcessBox = new FrameProcessBox();
		m_oTemplateBuilder.buildFrame(m_oFrameProcessBox, "fraDialogBox");
		m_oFrameProcessBox.init(bSetMaximumClientTimeout);
		
		// Add listener
		m_oFrameProcessBox.addListener(this);
		this.attachChild(m_oFrameProcessBox);
		
		return true;
	}
		
	public void setTitle(String sTitle){
		m_oFrameProcessBox.setTitle(sTitle);
	}
	
	public void setMessage(String sMessage){
		m_oFrameProcessBox.setMessage(sMessage);
	}
	
	public void addRefreshButton(String sBtnValue) {
		m_oFrameProcessBox.addRefreshButton(sBtnValue);
	}
	
	@Override
	public void FrameProcessBox_finishShow() {
		this.finishShow();
	}
	
}
