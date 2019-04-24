package app;
import core.Controller;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormESignature extends VirtualUIForm implements FrameESignatureListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameESignature m_oFrameESignature;
	private String m_sESignature;
	private boolean m_bUserCancel;
	
	public FormESignature(Controller oParentController, String sPaymentMethodName) {
		super(oParentController);
		m_sESignature = "";
		m_bUserCancel = false;
		m_oTemplateBuilder = new TemplateBuilder();
		m_oTemplateBuilder.loadTemplate("frmESignature.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Check review frame
		m_oFrameESignature = new FrameESignature(sPaymentMethodName);
		m_oTemplateBuilder.buildFrame(m_oFrameESignature, "fraESignature");
		m_oFrameESignature.addListener(this);
		this.attachChild(m_oFrameESignature);
	}

	public void setCheckURL(String sURL){
		m_oFrameESignature.setCheckURL(sURL);
	}
	
	public String getESignture(){
		return m_sESignature;
	}
	
	public boolean isUserCancel() {
		return this.m_bUserCancel;
	}
	
	public void switchDisplayFrame(boolean toPreviewCheckFrame){
		m_oFrameESignature.switchDisplayFrame(toPreviewCheckFrame);
	}
	
	@Override
	public void frameESignature_clickExit() {
		this.m_bUserCancel = true;
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public void frameESignature_clickConfirm(String sESignature) {
		m_sESignature = sESignature;
		// Finish showing this form
		this.finishShow();
	}
}
