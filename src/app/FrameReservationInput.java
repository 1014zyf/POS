package app;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import commonui.FormDatePicker;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FrameReservationInputListener {
    void FrameReservationInput_clickCancel();

	void FrameReservationInput_clickConfirm();
}

public class FrameReservationInput extends VirtualUIFrame implements FrameTitleHeaderListener{
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oTitleHeader;
	private VirtualUILabel m_oLabelReservationNumberTitle;
	private VirtualUITextbox m_oTextboxReservationNumber;
	private VirtualUILabel m_oLabelReservationDateTitle;
	private VirtualUILabel m_oLabelReservationDate;
	
	private VirtualUIButton m_oButtonConfirm;
	private VirtualUIButton m_oButtonCancel;
	
	private String m_sResvNum;
	private String m_sResvDate;

	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameReservationInputListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameReservationInputListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameReservationInputListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
	
	public FrameReservationInput() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameReservationInputListener>();
		
		m_sResvNum = null;
		DateTime oDateTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		m_sResvDate = formatter.print(oDateTime);
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraReservationInput.xml");
		
		// Title Bar Frame
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(false);
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("retrieve_booking_record"));
		this.attachChild(m_oTitleHeader);
		
		// Reservation Number Title
		m_oLabelReservationNumberTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelReservationNumberTitle, "lblResvNumberTitle");
		m_oLabelReservationNumberTitle.setValue(AppGlobal.g_oLang.get()._("confirmation_number"));
		this.attachChild(m_oLabelReservationNumberTitle);
		
		// Reservation Number
		m_oTextboxReservationNumber = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxReservationNumber, "txtboxResvNumber");
		m_oTextboxReservationNumber.setFocusWhenShow(true);
		this.attachChild(m_oTextboxReservationNumber);
		
		// Reservation Date Title
		m_oLabelReservationDateTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelReservationDateTitle, "lblResvDateTitle");
		m_oLabelReservationDateTitle.setValue(AppGlobal.g_oLang.get()._("booking_date"));
		this.attachChild(m_oLabelReservationDateTitle);

		// Reservation Date
		m_oLabelReservationDate = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelReservationDate, "lblResvDate");
		m_oLabelReservationDate.allowClick(true);
		m_oLabelReservationDate.setClickServerRequestBlockUI(false);
		m_oLabelReservationDate.setValue(m_sResvDate);
		this.attachChild(m_oLabelReservationDate);

		// Create OK button
		m_oButtonConfirm = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonConfirm, "btnConfirm");
		m_oButtonConfirm.setValue(AppGlobal.g_oLang.get()._("confirm"));
		m_oButtonConfirm.addClickServerRequestSubmitElement(m_oTextboxReservationNumber);
		m_oButtonConfirm.setClickServerRequestBlockUI(true);
		this.attachChild(m_oButtonConfirm);	

		// Create Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "btnCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		this.attachChild(m_oButtonCancel);
	}

	public String getResvNum() {
		return m_sResvNum;
	}
	
	public String getResvDate() {
		return m_sResvDate;
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
        if (iChildId == m_oLabelReservationDate.getId()) {
        	DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        	DateTime oDateTime = formatter.parseDateTime(m_sResvDate);
        	
        	FormDatePicker oFormDatePicker = new FormDatePicker(oDateTime, this.getParentForm());
        	oFormDatePicker.show();
        	
        	if(oFormDatePicker.getDate() != null) {
        		m_sResvDate = oFormDatePicker.getDate();
        		m_oLabelReservationDate.setValue(m_sResvDate);
        	}
        	
        	bMatchChild = true;
        }
        else if (iChildId == m_oButtonCancel.getId()) {
        	for (FrameReservationInputListener listener : listeners) {        		
        		// Raise the event to parent
           		listener.FrameReservationInput_clickCancel();
            }
        	
        	bMatchChild = true;
        }
        else if (iChildId == m_oButtonConfirm.getId()) {
        	m_sResvNum = m_oTextboxReservationNumber.getValue();
        	m_sResvDate = m_oLabelReservationDate.getValue();
        	
        	for (FrameReservationInputListener listener : listeners) {        		
        		// Raise the event to parent
           		listener.FrameReservationInput_clickConfirm();
            }
        	
        	bMatchChild = true;
        }
        
        return bMatchChild;
	}
	
	@Override
	public void FrameTitleHeader_close() {
		// TODO Auto-generated method stub
		
	}
	
}
