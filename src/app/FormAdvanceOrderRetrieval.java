package app;

import java.util.ArrayList;

import commonui.FormConfirmBox;
import core.Controller;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormAdvanceOrderRetrieval extends VirtualUIForm implements FrameAdvanceOrderRetrievalListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameAdvanceOrderRetrieval m_oFrameAdvanceOrderRetrieval;
	private boolean m_bClosed = false;
	
	public FormAdvanceOrderRetrieval(Controller oParentController){
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmAdvanceOrderRetrieval.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Check review frame
		m_oFrameAdvanceOrderRetrieval = new FrameAdvanceOrderRetrieval();
		m_oTemplateBuilder.buildFrame(m_oFrameAdvanceOrderRetrieval, "fraAdvanceOrderRetrieval");
		m_oFrameAdvanceOrderRetrieval.addListener(this);
		this.attachChild(m_oFrameAdvanceOrderRetrieval);
	}

	public void setCheckURL(String sURL){
		m_oFrameAdvanceOrderRetrieval.setCheckURL(sURL);
	}
	
	public void setCheckDeposit(String sCheckDepoist){
		m_oFrameAdvanceOrderRetrieval.setCheckDeposit(sCheckDepoist);
	}
	
	public void showAdvanceOrderDetail(ArrayList<String> oAdvanceOrderDetail){
		// Update the member detail page	
		m_oFrameAdvanceOrderRetrieval.setAdvanceOrderDetail(oAdvanceOrderDetail);	
	}	

	public String getDepositAmount(){
		// Update the member detail page
		return m_oFrameAdvanceOrderRetrieval.getDepositNumber();
	}
	
	public boolean isClosedClicked(){
		return m_bClosed;
	}
	
	@Override
	public void FrameAdvanceOrderRetrieval_clickExit() {
		FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
		oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
		oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("confirm_to_exit")+"?");
		oFormConfirmBox.show();
		
		if(oFormConfirmBox.isOKClicked() == false)
			return;
		else{
			this.finishShow();
			m_bClosed = true;
		}
	}
	
	@Override
	public void FrameAdvanceOrderRetrieval_clickEnter() {
		// TODO Auto-generated method stub
		this.finishShow();
	}
}
