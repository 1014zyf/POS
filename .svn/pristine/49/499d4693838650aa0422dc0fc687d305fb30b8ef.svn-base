package app.commonui;

import core.Controller;
import core.templatebuilder.TemplateBuilder;
import core.virtualui.*;

public class FormConfirmBox extends Controller implements FrameConfirmBoxListener {
	private boolean m_bOK;
	
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameConfirmBox m_oFrameConfirmBox;
	
	public FormConfirmBox(String sOKButtonValue, String sCancelButtonValue, Controller oParentController){
		super(oParentController);
		
		init(sOKButtonValue, sCancelButtonValue);
	}
	
	private boolean init(String sOKButtonValue, String sCancelButtonValue) {
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmConfirmBox.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameConfirmBox = new FrameConfirmBox();
		m_oTemplateBuilder.buildFrame(m_oFrameConfirmBox, "fraConfirmBox");
		m_oFrameConfirmBox.init(sOKButtonValue, sCancelButtonValue);
		
		// Add listener
		m_oFrameConfirmBox.addListener(this);
		this.attachChild(m_oFrameConfirmBox);
		
		m_bOK = false;
		
		return true;
	}
		
	public void setTitle(String sTitle){
		m_oFrameConfirmBox.setTitle(sTitle);
	}
	
	public void setMessage(String sMessage){
		m_oFrameConfirmBox.setMessage(sMessage);
	}
	
	public boolean isOKClicked(){
		return m_bOK;
	}
	
	@Override
	public void FrameConfirmBox_clickOK() {
		m_bOK = true;
		this.finishShow();
	}
	
	@Override
	public void FrameConfirmBox_clickCancel() {
		m_bOK = false;
		this.finishShow();
	}
	
}
