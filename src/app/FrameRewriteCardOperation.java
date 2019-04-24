package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import commonui.FrameTitleHeader;
import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameRewriteCardOperationListener {
    void FrameRewriteCardOperation_forward(String sResponse);
	void FrameRewriteCardOperation_disconnect();
	void FrameRewriteCardOperation_timeout();
	void FrameRewriteCardOperation_clickOK();
	void FrameRewriteCardOperation_clickRetry();
	void FrameRewriteCardOperation_clickCancel();

}

public class FrameRewriteCardOperation extends VirtualUIFrame {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUILabel m_oLabelMessage;
	private VirtualUIButton m_oButtonOK;
	private VirtualUIButton m_oButtonRetry;
	private VirtualUIButton m_oButtonCancel;
	private FrameCommonBasket m_oCardInfoListCommonBasket;
	private VirtualUIImage m_oPaymentImage;
	private VirtualUILabel m_oLabelRemainingBalance;
	private boolean m_bNeedResizeImageAfterTimeout;

	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameRewriteCardOperationListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameRewriteCardOperationListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameRewriteCardOperationListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
    public FrameRewriteCardOperation(){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameRewriteCardOperationListener>();
	}
	
	public void init(String sMediaUrl, String sRemainingBalance) {
		// Load child elements from template
		// Load form from template file
		if (sRemainingBalance.isEmpty()) {
			m_bNeedResizeImageAfterTimeout = false;
			// Load form from template file
			m_oTemplateBuilder.loadTemplate("fraRewriteCardOperation.xml");
		} else {
			m_bNeedResizeImageAfterTimeout = true;
			// Load form from template file
			m_oTemplateBuilder.loadTemplate("fraRewriteCardOperationLarge.xml");
			// Remaining Amount
			m_oLabelRemainingBalance = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelRemainingBalance, "lblRemainingBalance");
			m_oLabelRemainingBalance.setValue(AppGlobal.g_oLang.get()._("remaining_balance") + ": "+ AppGlobal.g_oFuncOutlet.get().getCurrencySign() + sRemainingBalance);
			m_oLabelRemainingBalance.setVisible(true);
			this.attachChild(m_oLabelRemainingBalance);
		}
		
		// Payment image
		m_oPaymentImage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oPaymentImage, "paymentImage");
		this.attachChild(m_oPaymentImage);
		
		m_oPaymentImage.setSource(sMediaUrl);
		m_oPaymentImage.setVisible(true);
		
		// DialogBox Title
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(false);
		this.attachChild(m_oFrameTitleHeader);
		
		// DialogBox Message
		m_oLabelMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMessage, "lblMessage");
		m_oLabelMessage.setWidth(this.getWidth());
		this.attachChild(m_oLabelMessage);
		
		// OK Button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "butOK");
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("confirm"));
		m_oButtonOK.setTop(this.getHeight() - m_oButtonOK.getHeight() - 20);
		m_oButtonOK.setLeft((this.getWidth()/3) - m_oButtonOK.getWidth() - 5);
		this.attachChild(m_oButtonOK);
		
		// Retry Button
		m_oButtonRetry = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonRetry, "butRetry");
		m_oButtonRetry.setValue(AppGlobal.g_oLang.get()._("retry"));
		m_oButtonRetry.setTop(this.getHeight() - m_oButtonRetry.getHeight() - 20);
		m_oButtonRetry.setLeft((this.getWidth()/2) - (m_oButtonRetry.getWidth()/2));
		this.attachChild(m_oButtonRetry);
		
		// Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "butCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		m_oButtonCancel.setTop(this.getHeight() - m_oButtonCancel.getHeight() - 20);
		m_oButtonCancel.setLeft((this.getWidth()/3)*2 + 5);
		m_oButtonCancel.setClickServerRequestBlockUI(false);
		this.attachChild(m_oButtonCancel);
		
		// Card information list common basket
		m_oCardInfoListCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oCardInfoListCommonBasket, "fraCardInfoList");
		m_oCardInfoListCommonBasket.init();
		this.attachChild(m_oCardInfoListCommonBasket);
		
		// Resize the message label
		//m_oLabelMessage.setHeight(this.getHeight() - m_oLabelTitle.getHeight() - (m_oButtonOK.getHeight() + 20));
	}
	
    public void addShowCancelButtonTimer(){
		// Show cancel button after 10s
		this.addTimer("show_cancel_button", 10000, false, "show_cancel_button", false, true, null);
	}
	
	public void setShowCancelButtonTimer(boolean bStart){
		this.controlTimer("show_cancel_button", bStart);
	}

	public void showReadCardScreen(String sDisplayMessage){
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("read_rewrite_card"));
		this.setMessage(sDisplayMessage);
		
		m_oCardInfoListCommonBasket.setVisible(false);
		showOKButton(false);
		showRetryButton(false);
		showCancelButton(false);
		addShowCancelButtonTimer();
		
		// Start the 5s show cancel button timer
		this.setShowCancelButtonTimer(true);
	}
	
	public void showCardInformationScreen(String sContent, HashMap <String, String> oCardInfoList){
		int iSectionId = 0;
		
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("kindly_reminder"));
		this.setMessage(sContent);
		
		int iWidth = m_oCardInfoListCommonBasket.getWidth()/2;
		m_oCardInfoListCommonBasket.clearAllSections();
		
		//Add Rewrite Card Info Title
		ArrayList<Integer> iFieldWidth = new ArrayList<Integer>();
		ArrayList<String> sFieldValue = new ArrayList<String>();
		
		iFieldWidth.add(iWidth);
		sFieldValue.add(AppGlobal.g_oLang.get()._("card_information"));
		iFieldWidth.add(iWidth);
		sFieldValue.add(AppGlobal.g_oLang.get()._("content"));
		
		m_oCardInfoListCommonBasket.addHeader(iFieldWidth, sFieldValue);
		m_oCardInfoListCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		m_oCardInfoListCommonBasket.setHeaderTextAlign(0, HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		m_oCardInfoListCommonBasket.setHeaderTextAlign(1, HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		
		int iRowHeight = 0;
		int iItemIndex = 0;
		for (Entry<String, String> entry : oCardInfoList.entrySet()) {
			//Add Card Information Records Details
			ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
			ArrayList<String> sFieldValues = new ArrayList<String>();
			ArrayList<String> sFieldAligns = new ArrayList<String>();
			ArrayList<String> sFieldTypes = new ArrayList<String>();
			
			// show key
			iFieldWidths.add(iWidth);
			sFieldValues.add(entry.getKey());
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			// show value
			iFieldWidths.add(iWidth);
			sFieldValues.add(entry.getValue());
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
				iRowHeight = 50;
			
			m_oCardInfoListCommonBasket.addItem(iSectionId, iItemIndex++, iRowHeight, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
		}
		
		m_oCardInfoListCommonBasket.setVisible(true);
		showOKButton(true);
		showRetryButton(false);
		showCancelButton(false);
		
	}
	
	public void setMessage(String sMessage){
		m_oLabelMessage.setValue(sMessage);
	}
	
	public void createForwardEvent(String sValue, int iTimeout, int iDelay) {
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestValue(sValue);
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(iTimeout);
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestDelay(iDelay);
	}
	
	public void showOKButton(boolean bShow) {
		m_oButtonOK.setVisible(bShow);
	}
	
	public void showRetryButton(boolean bShow) {
		m_oButtonRetry.setVisible(bShow);
	}
	
	public void showCancelButton(boolean bShow) {
		m_oButtonCancel.setVisible(bShow);
	}
	
	public void setRetryCancelButtonTimer(boolean bStart){
		this.controlTimer("show_cancel_button", bStart);
	}
	
	public void showRetryCancelButtonThread(int iClientSockId){
		// Set the last client socket ID
		AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);

		m_oButtonCancel.setVisible(true);
		m_oButtonRetry.setVisible(false);
		
		if(m_bNeedResizeImageAfterTimeout){
			m_oButtonCancel.setLeft(this.getWidth()/2 - m_oButtonCancel.getWidth()/2);
			m_oPaymentImage.setHeight(m_oPaymentImage.getHeight() - m_oButtonCancel.getHeight() - 24);
			m_oButtonCancel.setTop(m_oButtonCancel.getTop() + 5);
		}
		
		// Send the UI packet to client and the thread is finished
		super.getParentForm().finishUI(true);
	}
	
	public void hideCancelButton(){
		m_oButtonCancel.setVisible(false);
		m_oButtonRetry.setVisible(false);
		
		this.setMessage(AppGlobal.g_oLang.get()._("please_swipe_card"));
		
		// Start the 5s show cancel button timer
		this.setShowCancelButtonTimer(true);
	}
	
	public void showErrorImage(boolean bShow){
		this.m_oPaymentImage.setVisible(bShow);
		this.m_oPaymentImage.setTop(m_oLabelMessage.getTop() + m_oLabelMessage.getHeight() - m_oLabelMessage.getHeight()/3);
		this.m_oPaymentImage.setHeight(m_oPaymentImage.getHeight() - m_oButtonCancel.getHeight() - 24);
		this.m_oPaymentImage.setContentMode("scale_aspect_fill");
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if(m_oButtonOK.getId() == iChildId){
			for (FrameRewriteCardOperationListener listener : listeners) {
				// Raise the event to parent
	       		listener.FrameRewriteCardOperation_clickOK();
	       		bMatchChild = true;
	       		break;
			}
		}else
		if(m_oButtonRetry.getId() == iChildId){
			for (FrameRewriteCardOperationListener listener : listeners) {
				// Raise the event to parent
	       		listener.FrameRewriteCardOperation_clickRetry();
	       		bMatchChild = true;
	       		break;
			}
		}else
		if(m_oButtonCancel.getId() == iChildId){
			for (FrameRewriteCardOperationListener listener : listeners) {
				// Raise the event to parent
	       		listener.FrameRewriteCardOperation_clickCancel();
	       		bMatchChild = true;
	       		break;
			}
		}
    	
    	return bMatchChild;
    }
	
	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if(iId == super.getIDForPosting().hashCode()) {
			if(sNote.equals("show_cancel_button")){
				// Show cancel button
				//Create parameter array
				Object[] oParameters = new Object[1];
				oParameters[0] = iClientSockId;
				this.getParentForm().addTimerThread(iClientSockId, this, "showRetryCancelButtonThread", oParameters);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean forward(int iChildId, String sNote, String sStatus) {
		boolean bMatchChild = false;
		
		if (iChildId == AppGlobal.g_oDeviceManagerElement.get().getId()) {
			for (FrameRewriteCardOperationListener listener : listeners) {
				if (sNote.equals(AppGlobal.g_oDeviceManagerElement.get().getForwardServerRequestNote())) {
					// Current event's version is up-to-date
					
					// Raise the event to parent
					if (sStatus.equals("disconnected"))
						listener.FrameRewriteCardOperation_disconnect();
					else
					if (sStatus.equals("time_out")) {
						// Ingore forward timeout
					} else
						listener.FrameRewriteCardOperation_forward(AppGlobal.g_oDeviceManagerElement.get().getValue());
				}
			}
			
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
}
