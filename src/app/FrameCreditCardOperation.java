package app;

import java.util.ArrayList;

import templatebuilder.TemplateBuilder;
import commonui.FrameTitleHeader;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameCreditCardOperationListener {
	void FrameCreditCardOperation_clickCancel();
	void FrameCreditCardOperation_forward(String sResponse);
	void FrameCreditCardOperation_disconnect();
	void FrameCreditCardOperation_timeout();
}

public class FrameCreditCardOperation extends VirtualUIFrame {
	
	TemplateBuilder m_oTemplateBuilder;
	
	//private VirtualUIFrame m_oFrameTitleBar;
	//private VirtualUILabel m_oLabelTitle;
	private FrameTitleHeader m_oTitleHeader;
	private VirtualUILabel m_oLabelValue1Header;
	private VirtualUILabel m_oLabelValue2Header;
	private VirtualUILabel m_oLabelValue3Header;
	private VirtualUILabel m_oLabelValue1Value;
	private VirtualUILabel m_oLabelValue2Value;
	private VirtualUILabel m_oLabelValue3Value;
	private VirtualUILabel m_oLabelInformation;
	
	private VirtualUIButton m_oButtonCancel;
	//private VirtualUIImage m_oButtonExit;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameCreditCardOperationListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameCreditCardOperationListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameCreditCardOperationListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public void init(int iShowCancelButtonInterval) {	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameCreditCardOperationListener>();
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraCreditCardOperation.xml");
				
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(false);
		this.attachChild(m_oTitleHeader);

		// Value 1
		m_oLabelValue1Header = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelValue1Header, "lblValue1Header");
		this.attachChild(m_oLabelValue1Header);
		
		m_oLabelValue1Value = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelValue1Value, "lblValue1Value");
		this.attachChild(m_oLabelValue1Value);

// Add by King Cheung 2017-12-13 --Start---
		VirtualUIFrame oFrameUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameUnderline, "fraInfoUnderline");
		oFrameUnderline.setTop(116);
		this.attachChild(oFrameUnderline);
// Add by King Cheung 2017-12-13 --End---
		
		// Value 2
		m_oLabelValue2Header = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelValue2Header, "lblValue2Header");
		this.attachChild(m_oLabelValue2Header);
		
		m_oLabelValue2Value = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelValue2Value, "lblValue2Value");
		this.attachChild(m_oLabelValue2Value);

// Add by King Cheung 2017-12-13 --Start---
		oFrameUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameUnderline, "fraInfoUnderline");
		oFrameUnderline.setTop(164);
		this.attachChild(oFrameUnderline);
// Add by King Cheung 2017-12-13 --End---
				
		// Value 3
		m_oLabelValue3Header = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelValue3Header, "lblValue3Header");
		this.attachChild(m_oLabelValue3Header);
		
		m_oLabelValue3Value = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelValue3Value, "lblValue3Value");
		this.attachChild(m_oLabelValue3Value);

// Add by King Cheung 2017-12-13 --Start---
		oFrameUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameUnderline, "fraInfoUnderline");
		oFrameUnderline.setTop(212);
		this.attachChild(oFrameUnderline);
// Add by King Cheung 2017-12-13 --End---
		
		// Information
		m_oLabelInformation = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelInformation, "lblInformation");
		this.attachChild(m_oLabelInformation);
		
		// Create Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "btnCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		this.attachChild(m_oButtonCancel);
		
		/*
		// Create Exit Button
		m_oButtonExit = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oButtonExit, "imgExit");
		m_oButtonExit.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath()+"/buttons/exit_button.png");
		m_oButtonExit.allowClick(true);
		this.attachChild(m_oButtonExit);
		*/
		
		// Show cancel button after defined timeout
		this.addTimer("show_cancel_button", iShowCancelButtonInterval, false, "show_cancel_button", false, true, null);
		this.controlTimer("show_cancel_button", true);
	}
	
	public void showReadCardScreen(String sCheckNo, String sPaymentAmount, String sTipsAmount) {
		m_oLabelInformation.setVisible(true);

		m_oButtonCancel.setVisible(false);
		
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("online_credit_card_payment"));
		m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("please_swipe_credit_card_and_wait_for_processing")+"...");
		m_oLabelValue1Header.setValue(AppGlobal.g_oLang.get()._("check_no")+" :");
		m_oLabelValue1Value.setValue(sCheckNo);
		m_oLabelValue2Header.setValue(AppGlobal.g_oLang.get()._("payment_amount")+" :");
		m_oLabelValue2Value.setValue(sPaymentAmount);
		m_oLabelValue3Header.setValue(AppGlobal.g_oLang.get()._("tips")+" :");
		m_oLabelValue3Value.setValue(sTipsAmount);
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
	}
	
	public void showVoidPaymentScreen(String sTraceNum) {
		m_oLabelValue1Header.setVisible(true);
		m_oLabelValue2Header.setVisible(true);
		m_oLabelValue1Value.setVisible(true);
		m_oLabelValue2Value.setVisible(true);
		m_oLabelInformation.setVisible(true);
		m_oButtonCancel.setVisible(false);
		
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("online_credit_card_payment")+" - "+AppGlobal.g_oLang.get()._("release_payment"));
		m_oLabelValue1Header.setValue(AppGlobal.g_oLang.get()._("trace_number")+":");
		m_oLabelValue1Value.setValue(sTraceNum);
		m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("please_wait_for_processing")+"...");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
	}
	
	public void showAdjustmentScreen(String sTraceNum, String sTipsAmount) {
		m_oLabelValue1Header.setVisible(true);
		m_oLabelValue2Header.setVisible(true);
		m_oLabelValue1Value.setVisible(true);
		m_oLabelValue2Value.setVisible(true);
		m_oTemplateBuilder.buildLabel(m_oLabelInformation, "lblInformation");
		m_oLabelInformation.setVisible(true);
		m_oButtonCancel.setVisible(false);
		
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("online_credit_card_payment")+" - "+AppGlobal.g_oLang.get()._("adjust_tips"));
		m_oLabelValue1Header.setValue(AppGlobal.g_oLang.get()._("trace_number")+":");
		m_oLabelValue1Value.setValue(sTraceNum);
		m_oLabelValue2Header.setValue(AppGlobal.g_oLang.get()._("tips")+" :");
		m_oLabelValue2Value.setValue(sTipsAmount);
		m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("please_wait_for_processing")+"...");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
	}
	
	public void showDccOptOutScreen() {
		m_oLabelValue1Header.setVisible(true);
		m_oLabelValue2Header.setVisible(true);
		m_oLabelValue1Value.setVisible(true);
		m_oLabelValue2Value.setVisible(true);
		m_oTemplateBuilder.buildLabel(m_oLabelInformation, "lblInformation");
		m_oLabelInformation.setVisible(true);
		m_oButtonCancel.setVisible(false);
		
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("online_credit_card_payment")+" - "+AppGlobal.g_oLang.get()._("dcc_opt_out"));
		m_oLabelValue1Header.setValue("");
		m_oLabelValue1Value.setValue("");
		m_oLabelValue2Header.setValue("");
		m_oLabelValue2Value.setValue("");
		m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("please_wait_for_processing")+"...");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
	}
	
	public void showCancelButton(int iClientSockId) {
		// Set the last client socket ID
		AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);

		m_oButtonCancel.setVisible(true);

		// Send the UI packet to client and the thread is finished
		super.getParentForm().finishUI(true);
	}
	
	public void createForwardEvent(String sValue, int iTimeout, int iDelay) {
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestValue(sValue);
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(iTimeout);
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestDelay(iDelay);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (iChildId == m_oButtonCancel.getId()) {
			for (FrameCreditCardOperationListener listener : listeners) {
				// Raise the event to parent
				listener.FrameCreditCardOperation_clickCancel();
			}
			
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
	
	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if (iId == super.getIDForPosting().hashCode()) {
			// Show cancel button
			//Create parameter array
			Object[] oParameters = new Object[1];
			oParameters[0] = iClientSockId;
			
			this.getParentForm().addTimerThread(iClientSockId, this, "showCancelButton", oParameters);
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean forward(int iChildId, String sNote, String sStatus) {
		boolean bMatchChild = false;
		
		if (iChildId == AppGlobal.g_oDeviceManagerElement.get().getId()) {
			for (FrameCreditCardOperationListener listener : listeners) {
				if (sNote.equals(AppGlobal.g_oDeviceManagerElement.get().getForwardServerRequestNote())) {
					// Current event's version is up-to-date
					
					// Raise the event to parent
					if (sStatus.equals("disconnected"))
						listener.FrameCreditCardOperation_disconnect();
					else
					if (sStatus.equals("time_out")) {
						// Ingore forward timeout
					} else
						listener.FrameCreditCardOperation_forward(AppGlobal.g_oDeviceManagerElement.get().getValue());
				}
			}
			
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
}