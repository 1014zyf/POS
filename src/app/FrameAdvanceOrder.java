package app;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import commonui.FormDatePicker;
import commonui.FrameTitleHeader;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FrameAdvanceOrderListener {
	void FrameAdvanceOrder_clickCancel();

	void FrameAdvanceOrder_clickConfirm();
}

public class FrameAdvanceOrder extends VirtualUIFrame {
	TemplateBuilder m_oTemplateBuilder;

	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUILabel m_oLabelAdvanceOrderDateTitle;
	private VirtualUILabel m_oLabelAdvanceOrderDate;
	private VirtualUILabel m_oLabelAdvanceOrderNameTitle;
	private VirtualUITextbox m_oTextboxAdvanceOrderName;
	private VirtualUILabel m_oLabelAdvanceOrderPhoneTitle;
	private VirtualUITextbox m_oTextboxAdvanceOrderPhone;
	private VirtualUILabel m_oLabelAdvanceOrderFaxTitle;
	private VirtualUITextbox m_oTextboxAdvanceOrderFax;
	private VirtualUILabel m_oLabelAdvanceOrderNote1Title;
	private VirtualUITextbox m_oTextboxAdvanceOrderNote1;
	private VirtualUILabel m_oLabelAdvanceOrderNote2Title;
	private VirtualUITextbox m_oTextboxAdvanceOrderNote2;
	
	private VirtualUIButton m_oButtonConfirm;
	private VirtualUIButton m_oButtonCancel;
	
	private String m_sAdvanceOrderNum;
	private String m_sAdvanceOrderDate;
	private String m_sAdvanceOrderName;
	private String m_sAdvanceOrderPhone;
	private String m_sAdvanceOrderFax;
	private String m_sAdvanceOrderNote1;
	private String m_sAdvanceOrderNote2;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameAdvanceOrderListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameAdvanceOrderListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameAdvanceOrderListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameAdvanceOrder() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameAdvanceOrderListener>();
		
		m_sAdvanceOrderNum = null;
		DateTime oDateTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		m_sAdvanceOrderDate = formatter.print(oDateTime);
		m_sAdvanceOrderName = null;
		m_sAdvanceOrderPhone = null;
		m_sAdvanceOrderFax = null;
		m_sAdvanceOrderNote1 = null;
		m_sAdvanceOrderNote2 = null;
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraAdvanceOrder.xml");

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
	    m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
	    m_oFrameTitleHeader.init(false);
	    m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("add_advance_order"));
	    this.attachChild(m_oFrameTitleHeader);
		
		// AdvanceOrder Date Title
		m_oLabelAdvanceOrderDateTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAdvanceOrderDateTitle, "lblAdvanceOrderDateTitle");
		m_oLabelAdvanceOrderDateTitle.setValue(AppGlobal.g_oLang.get()._("pickup_date")+("*"));
		this.attachChild(m_oLabelAdvanceOrderDateTitle);
		
		// AdvanceOrder Date
		m_oLabelAdvanceOrderDate = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAdvanceOrderDate, "lblAdvanceOrderDate");
		m_oLabelAdvanceOrderDate.allowClick(true);
		m_oLabelAdvanceOrderDate.setClickServerRequestBlockUI(false);
		m_oLabelAdvanceOrderDate.setValue(m_sAdvanceOrderDate);
		this.attachChild(m_oLabelAdvanceOrderDate);
		
		// AdvanceOrder Name Title
		m_oLabelAdvanceOrderNameTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAdvanceOrderNameTitle, "lblAdvanceOrderNameTitle");
		m_oLabelAdvanceOrderNameTitle.setValue(AppGlobal.g_oLang.get()._("name")+("*"));
		this.attachChild(m_oLabelAdvanceOrderNameTitle);
		
		// AdvanceOrder Name
		m_oTextboxAdvanceOrderName = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxAdvanceOrderName, "txtboxAdvanceOrderName");
		m_oTextboxAdvanceOrderName.setFocusWhenShow(true);
		this.attachChild(m_oTextboxAdvanceOrderName);
		
		// AdvanceOrder Phone Title
		m_oLabelAdvanceOrderPhoneTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAdvanceOrderPhoneTitle, "lblAdvanceOrderPhoneTitle");
		m_oLabelAdvanceOrderPhoneTitle.setValue(AppGlobal.g_oLang.get()._("phone_number")+("*"));
		this.attachChild(m_oLabelAdvanceOrderPhoneTitle);

		// AdvanceOrder Phone
		m_oTextboxAdvanceOrderPhone = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxAdvanceOrderPhone, "txtboxAdvanceOrderPhone");
		//m_oTextboxAdvanceOrderPhone.setFocusWhenShow(true);
		this.attachChild(m_oTextboxAdvanceOrderPhone);
		
		// AdvanceOrder Fax Title
		m_oLabelAdvanceOrderFaxTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAdvanceOrderFaxTitle, "lblAdvanceOrderFaxTitle");
		m_oLabelAdvanceOrderFaxTitle.setValue(AppGlobal.g_oLang.get()._("fax_number"));
		this.attachChild(m_oLabelAdvanceOrderFaxTitle);

		// AdvanceOrder Fax
		m_oTextboxAdvanceOrderFax = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxAdvanceOrderFax, "txtboxAdvanceOrderFax");
		//m_oTextboxAdvanceOrderFax.setFocusWhenShow(true);
		this.attachChild(m_oTextboxAdvanceOrderFax);
		
		
		// AdvanceOrder Note1 Title
		m_oLabelAdvanceOrderNote1Title = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAdvanceOrderNote1Title, "lblAdvanceOrderNote1Title");
		m_oLabelAdvanceOrderNote1Title.setValue(AppGlobal.g_oLang.get()._("note")+"1");
		this.attachChild(m_oLabelAdvanceOrderNote1Title);

		// AdvanceOrder Note1
		m_oTextboxAdvanceOrderNote1 = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxAdvanceOrderNote1, "txtboxAdvanceOrderNote1");
		//m_oTextboxAdvanceOrderNote1.setFocusWhenShow(true);
		this.attachChild(m_oTextboxAdvanceOrderNote1);
		
		// AdvanceOrder Note1 Title
		m_oLabelAdvanceOrderNote2Title = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAdvanceOrderNote2Title, "lblAdvanceOrderNote2Title");
		m_oLabelAdvanceOrderNote2Title.setValue(AppGlobal.g_oLang.get()._("note")+"2");
		this.attachChild(m_oLabelAdvanceOrderNote2Title);

		// AdvanceOrder Note2
		m_oTextboxAdvanceOrderNote2 = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxAdvanceOrderNote2, "txtboxAdvanceOrderNote2");
		//m_oTextboxAdvanceOrderNote2.setFocusWhenShow(true);
		this.attachChild(m_oTextboxAdvanceOrderNote2);
		
		// Create OK button
		m_oButtonConfirm = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonConfirm, "btnConfirm");
		m_oButtonConfirm.setValue(AppGlobal.g_oLang.get()._("confirm"));
		m_oButtonConfirm.addClickServerRequestSubmitElement(this);
		m_oButtonConfirm.setClickServerRequestBlockUI(true);
		this.attachChild(m_oButtonConfirm);	

		// Create Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "btnCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		this.attachChild(m_oButtonCancel);
	}

	public String getAdvanceOrderNum() {
		return m_sAdvanceOrderNum;
	}
	
	public String getAdvanceOrderDate() {
		return m_sAdvanceOrderDate;
	}
	
	public String getAdvanceOrderName() {
		return m_sAdvanceOrderName;
	}
	
	public String getAdvanceOrderPhone() {
		return m_sAdvanceOrderPhone;
	}
	
	public String getAdvanceOrderFax() {
		return m_sAdvanceOrderFax;
	}
	
	public String getAdvanceOrderNote1() {
		return m_sAdvanceOrderNote1;
	}
	
	public String getAdvanceOrderNote2() {
		return m_sAdvanceOrderNote2;
	}
	
	
	public void setAdvanceOrderDetail(String sAdvanceOrderDate, String sAdvanceOrderName, String sAdvanceOrderPhone, String sAdvanceOrderFax, String sAdvanceOrderNote1, String sAdvanceOrderNote2){
		m_oLabelAdvanceOrderDate.setValue(sAdvanceOrderDate);
		m_oTextboxAdvanceOrderName.setValue(sAdvanceOrderName);
		m_oTextboxAdvanceOrderPhone.setValue(sAdvanceOrderPhone);
		m_oTextboxAdvanceOrderFax.setValue(sAdvanceOrderFax);
		m_oTextboxAdvanceOrderNote1.setValue(sAdvanceOrderNote1);
		m_oTextboxAdvanceOrderNote2.setValue(sAdvanceOrderNote2);	
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (iChildId == m_oLabelAdvanceOrderDate.getId()) {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			DateTime oDateTime = formatter.parseDateTime(m_sAdvanceOrderDate);
			
			FormDatePicker oFormDatePicker = new FormDatePicker(oDateTime, this.getParentForm());
			oFormDatePicker.show();
			
			if(oFormDatePicker.getDate() != null) {
				m_sAdvanceOrderDate = oFormDatePicker.getDate();
				m_oLabelAdvanceOrderDate.setValue(m_sAdvanceOrderDate);
			}
			bMatchChild = true;
		}
		else if (iChildId == m_oButtonCancel.getId()) {
			for (FrameAdvanceOrderListener listener : listeners) {
				// Raise the event to parent
				listener.FrameAdvanceOrder_clickCancel();
			}
			
			bMatchChild = true;
		}
		else if (iChildId == m_oButtonConfirm.getId()) {
			m_sAdvanceOrderDate = m_oLabelAdvanceOrderDate.getValue();
			m_sAdvanceOrderName = m_oTextboxAdvanceOrderName.getValue();
			m_sAdvanceOrderPhone = m_oTextboxAdvanceOrderPhone.getValue();
			m_sAdvanceOrderFax = m_oTextboxAdvanceOrderFax.getValue();
			m_sAdvanceOrderNote1 = m_oTextboxAdvanceOrderNote1.getValue();
			m_sAdvanceOrderNote2 = m_oTextboxAdvanceOrderNote2.getValue();
			
			for (FrameAdvanceOrderListener listener : listeners) {
				// Raise the event to parent
				listener.FrameAdvanceOrder_clickConfirm();
			}
			bMatchChild = true;
		}
		return bMatchChild;
	}
}
