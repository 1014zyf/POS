package app;

import core.Controller;
import om.ResvResv;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

public class FormReservationDetail extends VirtualUIForm implements FrameReservationDetailListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameReservationDetail m_oFrameReservationDetail;
	
	public FormReservationDetail(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmReservationDetail.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);

		m_oFrameReservationDetail = new FrameReservationDetail();
		m_oTemplateBuilder.buildFrame(m_oFrameReservationDetail, "fraReservationDetail");
		m_oFrameReservationDetail.addListener(this);
		this.attachChild(m_oFrameReservationDetail);
	}
   	
   	public void showResvDetail(ResvResv oResv){
   		// Update the member detail page
   		m_oFrameReservationDetail.updateDetail(oResv);	
   	}

	@Override
	public void frameReservationDetail_clickOK() {
		this.finishShow();
	}
}
