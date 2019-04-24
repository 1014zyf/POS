package app.commonui;

import org.joda.time.DateTime;

import core.Controller;

import core.templatebuilder.TemplateBuilder;

import core.virtualui.VirtualUIFrame;

public class FormDatePicker extends Controller implements FrameDatePickerListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameDatePicker m_oFrameDatePicker;
	
	private String m_sDate;
	
	public FormDatePicker(DateTime oDateTime, Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		m_sDate = null;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmDatePicker.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameDatePicker = new FrameDatePicker(oDateTime);
		m_oTemplateBuilder.buildFrame(m_oFrameDatePicker, "fraDatePicker");
		m_oFrameDatePicker.addListener(this);
		this.attachChild(m_oFrameDatePicker);
	}
	
	public String getDate() {
		return m_sDate;
	}

	@Override
	public void FrameDatePicker_clickCancel() {
		// TODO Auto-generated method stub
		this.finishShow();
	}

	@Override
	public void FrameDatePickerListener_clickOK() {
		// TODO Auto-generated method stub
		m_sDate = m_oFrameDatePicker.getDate();
		
		this.finishShow();
	}
}
