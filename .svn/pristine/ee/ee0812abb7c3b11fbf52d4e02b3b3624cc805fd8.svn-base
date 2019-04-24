package commonui;

import org.joda.time.DateTime;

import core.Controller;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

public class FormDatePicker extends VirtualUIForm implements FrameDatePickerListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameDatePicker m_oFrameDatePicker;
	
	private String m_sDate;
	private boolean m_bUserCancel;
	
	public FormDatePicker(DateTime oDateTime, Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		m_sDate = null;
		m_bUserCancel = false;
		
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
	
	public void setTitle(String sTitle){
		m_oFrameDatePicker.setTitle(sTitle);
	}
	
	public boolean isUserCancel(){
		return m_bUserCancel;
	}
	
	@Override
	public void FrameDatePicker_clickCancel() {
		this.finishShow();
	}

	@Override
	public void FrameDatePickerListener_clickOK() {
		m_sDate = m_oFrameDatePicker.getDate();
		
		this.finishShow();
	}
}
