package commonui;

import org.joda.time.DateTime;

import app.AppGlobal;
import core.Controller;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormConfirmBox extends VirtualUIForm implements FrameConfirmBoxListener {
	private boolean m_bOK;
	
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameConfirmBox m_oFrameConfirmBox;
	private DateTime m_oStartShownTime;
	private int m_iShowTime = 0;
	
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
	
	public void setTimeout(int iTimeout){
		m_iShowTime = iTimeout;
		m_oFrameConfirmBox.setTimeout(iTimeout);
	}
	
	public void setTimeoutChecking(boolean bStart){
		if(m_iShowTime == 0)
			return;

		m_oFrameConfirmBox.setTimeoutTimer(bStart);
		if(bStart)
			m_oStartShownTime = AppGlobal.getCurrentTime(false);
		else
			m_oStartShownTime = null;
	}
	
	public boolean isTimeout(){
		boolean bTimeout = false;
		
		if(m_iShowTime == 0)
			return false;
		
		if(m_oStartShownTime != null){
			if(((AppGlobal.getCurrentTime(false).getMillis() - m_oStartShownTime.getMillis())) > m_iShowTime)
				bTimeout = true;
			else
				setTimeoutChecking(true);
		}
		
		return bTimeout;
	}
	
	public boolean isOKClicked(){
		return m_bOK;
	}
	
	// Set the client timeout after click "OK" button (e.g. for SPA Auth waiting response)
	public void setConfirmTimeout(int iTimeout) {
		m_oFrameConfirmBox.setConfirmTimeout(iTimeout);
	}
	
	public void setMessageTextAlign(String sTextAlign) {
		m_oFrameConfirmBox.setMessageTextAlign(sTextAlign);
	}
	
	// Support loading screen after click "OK"
	public void supportLoadingScreen() {
		m_oFrameConfirmBox.supportLoadingScreen();
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
	
	@Override
	public boolean FrameConfirmBox_timeout() {
		if(isTimeout()){
			m_bOK = false;
			this.finishShow();
			return true;
		}
		return false;
	}
}
