package app.commonui;

import core.Controller;
import core.templatebuilder.TemplateBuilder;
import core.virtualui.*;

public class FormDialogBox extends Controller implements FrameDialogBoxListener {
	private String m_sResult;
	
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameDialogBox m_oFrameDialogBox;
	
	public FormDialogBox(Controller oParentController) {
		super(oParentController);
		
		init();
	}

	public FormDialogBox(String sBtnValue, Controller oParentController) {
		super(oParentController);
		
		init();
		
		// Create a button
		m_oFrameDialogBox.addSingleButton(sBtnValue);
	}
	
	private boolean init() {
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmDialogBox.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameDialogBox = new FrameDialogBox();
		m_oTemplateBuilder.buildFrame(m_oFrameDialogBox, "fraDialogBox");
		m_oFrameDialogBox.init();
		
		// Add listener
		m_oFrameDialogBox.addListener(this);
		this.attachChild(m_oFrameDialogBox);
		
		return true;
	}
		
	public void setTitle(String sTitle){
		m_oFrameDialogBox.setTitle(sTitle);
	}
	
	public void setMessage(String sMessage){
		m_oFrameDialogBox.setMessage(sMessage);
	}
	
	public void setDelayTime(float fDelayTime){
		m_oFrameDialogBox.setDelayTime(fDelayTime);
	}
	
	public String getResult(){
		return m_sResult;
	}
	
	public void setResult(String sResult){
		m_sResult = sResult;
	}

	@Override
	public void FrameDialogBox_click(String sResult) {
		m_sResult = sResult;
		this.finishShow();
	}
	
}
