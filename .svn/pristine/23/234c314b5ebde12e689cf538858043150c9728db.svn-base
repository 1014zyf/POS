package app;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import commonui.FrameTitleHeader;
import externallib.StringLib;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameSmartCardOperationListener {
    void FrameSmartCardOperation_clickCancel();
    void FrameSmartCardOperation_forward(String sResponse);
    void FrameSmartCardOperation_disconnect();
    void FrameSmartCardOperation_timeout();
}

public class FrameSmartCardOperation extends VirtualUIFrame {
	
	TemplateBuilder m_oTemplateBuilder;

	private FrameTitleHeader m_oFrameTitleHeader;
	private FrameCommonBasket m_oCardInformationCommonBasket;
	private VirtualUILabel m_oLabelInformation;
	private VirtualUIImage m_oImageReadCard;
	
	private VirtualUIButton m_oButtonCancel;
	private VirtualUIImage m_oButtonExit;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameSmartCardOperationListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameSmartCardOperationListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameSmartCardOperationListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
	public void init(){	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameSmartCardOperationListener>();
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraSmartCardOperation.xml");

		m_oFrameTitleHeader = new FrameTitleHeader();
        m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
        m_oFrameTitleHeader.init(false);
        this.attachChild(m_oFrameTitleHeader);
		
		// Information
		m_oLabelInformation = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelInformation, "lblInformation");
		this.attachChild(m_oLabelInformation);
		
		// Card information list
		m_oCardInformationCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oCardInformationCommonBasket, "bktCardInformation");
		m_oCardInformationCommonBasket.init();
		this.attachChild(m_oCardInformationCommonBasket);
		
		// Read card image
		m_oImageReadCard = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageReadCard, "imgReadCard");
		m_oImageReadCard.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath()+"/icons/read_smart_card_icon.png");
		this.attachChild(m_oImageReadCard);
		
		// Create Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "btnCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		this.attachChild(m_oButtonCancel);
		
		// Create Exit Button
		m_oButtonExit = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oButtonExit, "imgExit");
		m_oButtonExit.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath()+"/buttons/exit_button.png");
		m_oButtonExit.allowClick(true);
		this.attachChild(m_oButtonExit);
	}
	
	public void addShowCancelButtonTimer(){
		// Show cancel button after 10s
		this.addTimer("show_cancel_button", 10000, false, "", false, true, null);
	}
	
	public void setShowCancelButtonTimer(boolean bStart){
		this.controlTimer("show_cancel_button", bStart);
	}
	
	public void showInitScreen(){
		m_oLabelInformation.setVisible(true);
		m_oCardInformationCommonBasket.setVisible(false);
		m_oImageReadCard.setVisible(false);
		m_oButtonCancel.setVisible(false);
		m_oButtonExit.setVisible(false);

		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("smart_card_initialization"));
		m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("loading")+"...");
	}
	
	public void showReadCardScreen(){
		m_oLabelInformation.setVisible(true);
		m_oLabelInformation.setTop(m_oFrameTitleHeader.getTop() + m_oFrameTitleHeader.getHeight());
		m_oLabelInformation.setWidth(this.getWidth());
		m_oCardInformationCommonBasket.setVisible(false);
		m_oImageReadCard.setVisible(true);
		m_oImageReadCard.setTop(m_oLabelInformation.getTop() + m_oLabelInformation.getHeight() + 20);
		m_oImageReadCard.setLeft((this.getWidth() - m_oImageReadCard.getWidth()) / 2 + 50);
		m_oButtonCancel.setVisible(true);
		m_oButtonExit.setVisible(false);

		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("read_smart_card"));
		m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("please_place_the_smart_card_on_the_card_reader"));
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
	}
	
	public void showReadCardForLoginScreen(){
		this.setBackgroundColor("#F0FBFF");
		m_oLabelInformation.setVisible(true);
		m_oLabelInformation.setTop(m_oFrameTitleHeader.getTop() + m_oFrameTitleHeader.getHeight());
		m_oLabelInformation.setWidth(this.getWidth());
		m_oLabelInformation.setTextSize(40);
		m_oCardInformationCommonBasket.setVisible(false);
		m_oImageReadCard.setVisible(true);
		m_oImageReadCard.setTop(m_oLabelInformation.getTop() + m_oLabelInformation.getHeight() + 50);
		m_oImageReadCard.setLeft((this.getWidth() - m_oImageReadCard.getWidth()) / 2 + 50);
		m_oButtonCancel.setVisible(false);
		m_oFrameTitleHeader.setVisible(false);
		m_oButtonExit.setVisible(true);
		
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("read_smart_card"));
		m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("please_place_the_smart_card_on_the_card_reader"));
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
	}
	
	public void showStopScreen(){
		m_oLabelInformation.setVisible(true);
		m_oCardInformationCommonBasket.setVisible(false);
		m_oImageReadCard.setVisible(false);
		m_oButtonCancel.setVisible(false);
		m_oButtonExit.setVisible(false);
		
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("stop_smart_card"));
		m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("loading")+"...");
	}
	
	public void showReadCardResultScreen(FuncSmartCard oFuncSmartCard){
		SimpleDateFormat oDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		
		m_oLabelInformation.setVisible(false);
		m_oCardInformationCommonBasket.setVisible(true);
		m_oImageReadCard.setVisible(false);
		m_oButtonCancel.setVisible(true);
		m_oButtonExit.setVisible(false);
		
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("read_smart_card"));
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("exit"));
		
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
    	ArrayList<String> sFieldValues = new ArrayList<String>();
    	ArrayList<String> sFieldAligns = new ArrayList<String>();
	   	iFieldWidths.add(374);
    	sFieldValues.add("");
    	sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
    	iFieldWidths.add(374);
    	sFieldValues.add("");
    	sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
    	
    	m_oCardInformationCommonBasket.addHeader(iFieldWidths, sFieldValues);
    	m_oCardInformationCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
    	
    	int iRowIndex = 0;
    	// Add card information
    	sFieldValues.clear();
    	sFieldValues.add(AppGlobal.g_oLang.get()._("card_no"));
    	sFieldValues.add(oFuncSmartCard.getCardNo());
    	m_oCardInformationCommonBasket.addItem(0, iRowIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
    	iRowIndex++;
    	sFieldValues.clear();
    	sFieldValues.add(AppGlobal.g_oLang.get()._("cash_dollar"));
    	sFieldValues.add(oFuncSmartCard.getCashAmount().setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString());
    	m_oCardInformationCommonBasket.addItem(0, iRowIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
    	iRowIndex++;
    	sFieldValues.clear();
    	sFieldValues.add(AppGlobal.g_oLang.get()._("free_dollar"));
    	sFieldValues.add(oFuncSmartCard.getFreeAmount().setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString());
    	m_oCardInformationCommonBasket.addItem(0, iRowIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
    	iRowIndex++;
    	sFieldValues.clear();
    	sFieldValues.add(AppGlobal.g_oLang.get()._("issue_date")+" (MM/DD/CCYY)");
    	sFieldValues.add(oDateFormat.format(oFuncSmartCard.getIssueDate()));
    	m_oCardInformationCommonBasket.addItem(0, iRowIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
    	iRowIndex++;
    	sFieldValues.clear();
    	sFieldValues.add(AppGlobal.g_oLang.get()._("expiry_date")+" (MM/DD/CCYY)");
    	sFieldValues.add(oDateFormat.format(oFuncSmartCard.getExpiryDate()));
    	m_oCardInformationCommonBasket.addItem(0, iRowIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
    	iRowIndex++;
    	sFieldValues.clear();
    	sFieldValues.add(AppGlobal.g_oLang.get()._("last_visit")+" (MM/DD/CCYY)");
    	sFieldValues.add(oDateFormat.format(oFuncSmartCard.getLastVisitDate()));
    	m_oCardInformationCommonBasket.addItem(0, iRowIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
    	iRowIndex++;
    	sFieldValues.clear();
    	sFieldValues.add(AppGlobal.g_oLang.get()._("visit_counter"));
    	sFieldValues.add(String.valueOf(oFuncSmartCard.getVisitCount()));
    	m_oCardInformationCommonBasket.addItem(0, iRowIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
    	iRowIndex++;
    	sFieldValues.clear();
    	sFieldValues.add(AppGlobal.g_oLang.get()._("bonus_points"));
    	sFieldValues.add(String.valueOf(oFuncSmartCard.getBonus()));
    	m_oCardInformationCommonBasket.addItem(0, iRowIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
    	iRowIndex++;
    	sFieldValues.clear();
    	sFieldValues.add(AppGlobal.g_oLang.get()._("last_earned_bonus"));
    	sFieldValues.add(String.valueOf(oFuncSmartCard.getLastEarnBonus()));
    	m_oCardInformationCommonBasket.addItem(0, iRowIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
    	iRowIndex++;
    	sFieldValues.clear();
    	sFieldValues.add(AppGlobal.g_oLang.get()._("last_redeem_bonus"));
    	sFieldValues.add(String.valueOf(oFuncSmartCard.getLastRedeemBonus()));
    	m_oCardInformationCommonBasket.addItem(0, iRowIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
    	iRowIndex++;
    	sFieldValues.clear();
    	sFieldValues.add(AppGlobal.g_oLang.get()._("card_balance"));
    	sFieldValues.add((oFuncSmartCard.getCashAmount().add(oFuncSmartCard.getFreeAmount())).setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString());
    	m_oCardInformationCommonBasket.addItem(0, iRowIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
    	iRowIndex++;

	}
	
	public void showCancelButton(int iClientSockId){
		// Set the last client socket ID
		AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);

		m_oButtonCancel.setVisible(true);

		// Send the UI packet to client and the thread is finished
		super.getParentForm().finishUI(true);
	}
	
	public void hideCancelButton(){
		m_oButtonCancel.setVisible(false);
		
		// Stop the show cancel button timer
		this.setShowCancelButtonTimer(false);
	}
	
	public void createForwardEvent(String sValue, int iTimeout, int iDelay){
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestValue(sValue);
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(iTimeout);
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestDelay(iDelay);
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
        if (iChildId == m_oButtonCancel.getId() || iChildId == m_oButtonExit.getId()) {
        	for (FrameSmartCardOperationListener listener : listeners) {
        		// Raise the event to parent
           		listener.FrameSmartCardOperation_clickCancel();
            }
        	
        	bMatchChild = true;
        }
        
        return bMatchChild;
    }
	
	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if(iId == super.getIDForPosting().hashCode()) {
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
        	for (FrameSmartCardOperationListener listener : listeners) {
				if(sNote.equals(AppGlobal.g_oDeviceManagerElement.get().getForwardServerRequestNote())){
					// Current event's version is up-to-date

	        		// Raise the event to parent
	        		if(sStatus.equals("disconnected"))
	        			listener.FrameSmartCardOperation_disconnect();
	        		else
	        		if(sStatus.equals("time_out"))
	            		listener.FrameSmartCardOperation_timeout();
	        		else
	        			listener.FrameSmartCardOperation_forward(AppGlobal.g_oDeviceManagerElement.get().getValue());
				}
            }
        	
        	bMatchChild = true;
        }
        
        return bMatchChild;
	}
	
}