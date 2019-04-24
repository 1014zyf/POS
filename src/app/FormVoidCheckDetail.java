package app;

import core.Controller;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

public class FormVoidCheckDetail extends VirtualUIForm implements FrameVoidCheckDetailListener{
	TemplateBuilder m_oTemplateBuilder;

	private boolean m_bConfirmVoid;
	private FrameVoidCheckDetail m_oFrameVoidCheckDetail;
	
	public FormVoidCheckDetail(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmVoidCheckDetail.xml");

		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Load form from template file
		m_oFrameVoidCheckDetail = new FrameVoidCheckDetail();
		m_oTemplateBuilder.buildFrame(m_oFrameVoidCheckDetail, "fraVoidCheckDetail");
		
		// Add listener
		m_oFrameVoidCheckDetail.addListener(this);
		this.attachChild(m_oFrameVoidCheckDetail);
	}
	
	public void initWithFuncCheck(FuncCheck oFuncCheck) {
		m_oFrameVoidCheckDetail.setupValue(oFuncCheck);
	}
	
	public void setTitle(String sTitle){
		m_oFrameVoidCheckDetail.setTitle(sTitle);
	}
	
	@Override
	public void FrameVoidCheckDetail_confirm() {
		m_bConfirmVoid = true;
		this.finishShow();
	}
	
	public void FrameVoidCheckDetail_cancel() {
		m_bConfirmVoid = false;
		this.finishShow();
	}

	public boolean confirmVoid() {
		return m_bConfirmVoid;
	}
}
