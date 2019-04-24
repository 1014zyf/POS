package app;

import commonui.FormDialogBox;
import core.Controller;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

public class FormReservationInput extends VirtualUIForm implements FrameReservationInputListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameReservationInput m_oFrameReservationInput;
	
	private String m_sResvNum;
	private String m_sResvDate;
	
	public FormReservationInput(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		m_sResvNum = null;
		m_sResvDate = null;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmReservationInput.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);

		m_oFrameReservationInput = new FrameReservationInput();
		m_oTemplateBuilder.buildFrame(m_oFrameReservationInput, "fraReservationInput");
		m_oFrameReservationInput.addListener(this);
		this.attachChild(m_oFrameReservationInput);
	}
	
	public String getResvNum() {
		return m_sResvNum;
	}
	
	public String getResvDate() {
		return m_sResvDate;
	}

	@Override
	public void FrameReservationInput_clickCancel() {
		this.finishShow();
	}

	@Override
	public void FrameReservationInput_clickConfirm() {
		m_sResvNum = m_oFrameReservationInput.getResvNum();
		m_sResvDate = m_oFrameReservationInput.getResvDate();

		if(m_sResvNum.isEmpty()) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
	        oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
	        oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("not_allow_blank_confirmation_number"));
	        oFormDialogBox.show();

			m_sResvNum = null;
			m_sResvDate = null;
	        return;
		}
		
		this.finishShow();
	}
}
