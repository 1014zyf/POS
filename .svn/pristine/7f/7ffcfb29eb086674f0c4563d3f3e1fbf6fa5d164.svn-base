package app;

import core.Controller;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormPreviewReceipt extends VirtualUIForm implements FramePreviewReceiptListener {

	TemplateBuilder m_oTemplateBuilder;
	
	private FramePreviewReceipt m_oFramePreviewReceipt;
	private boolean bClickReprintReceipt;
	private boolean bClickReleasePayment;
	private boolean bClickVoidPaidCheck;
	private boolean bClickAdjustTips;
	private boolean bClickAdjustPayments;
	private boolean bClickSetMemberForClosedCheck;

	public FormPreviewReceipt(Controller oParentController, boolean bPastDateCheck){
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmPreviewReceipt.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		// Check review frame
		m_oFramePreviewReceipt = new FramePreviewReceipt();
		m_oFramePreviewReceipt.setFunctionButtonVisible(bPastDateCheck);
		m_oTemplateBuilder.buildFrame(m_oFramePreviewReceipt, "fraPreviewReceipt");
		m_oFramePreviewReceipt.addListener(this);
		this.attachChild(m_oFramePreviewReceipt);
		
		bClickReprintReceipt = false;
		bClickReleasePayment = false;
		bClickVoidPaidCheck = false;
		bClickAdjustTips = false;
		bClickAdjustPayments = false;
		bClickSetMemberForClosedCheck = false;
	}
	
	public void setCheckURL(String sURL){
		m_oFramePreviewReceipt.setCheckURL(sURL);
	}

	public boolean isClickReprintReceipt(){
		return bClickReprintReceipt;
	}
	
	public boolean isClickReleasePayment(){
		return bClickReleasePayment;
	}
	
	public boolean isClickVoidPaidCheck(){
		return bClickVoidPaidCheck;
	}
	
	public boolean isClickAdjustTips(){
		return bClickAdjustTips;
	}
	
	public boolean isClickAdjustPayments(){
		return bClickAdjustPayments;
	}
	
	public void setSetMemberButtonVisible(boolean bVisible) {
		m_oFramePreviewReceipt.setSetMemberButtonVisible(bVisible);
	}
	
	public boolean isClickSetMemberForClosedCheck() {
		return bClickSetMemberForClosedCheck;
	}
	
	@Override
	public void framePreviewReceipt_clickExit() {
		// Finish showing this form
		this.finishShow();
	}

	@Override
	public void framePreviewReceipt_clickReprintReceipt() {
		bClickReprintReceipt = true;
		
		// Finish showing this form
		this.finishShow();
	}

	@Override
	public void framePreviewReceipt_clickReleasePayment() {
		bClickReleasePayment = true;
		
		// Finish showing this form
		this.finishShow();
	}

	@Override
	public void framePreviewReceipt_clickVoidPaidCheck() {
		bClickVoidPaidCheck = true;
		
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public void framePreviewReceipt_clickAdjustTips() {
		bClickAdjustTips = true;
		
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public void framePreviewReceipt_clickAdjustPayments() {
		bClickAdjustPayments = true;
		
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public void framePreviewReceipt_clickSetMemberForClosedCheck() {
		bClickSetMemberForClosedCheck = true;
		
		// Finish showing this form
		this.finishShow();
	}
}
