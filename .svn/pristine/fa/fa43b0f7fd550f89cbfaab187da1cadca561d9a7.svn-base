package app;

import java.math.BigDecimal;
import java.util.ArrayList;

import externallib.StringLib;
import app.FormOctopusOperation.OPERATION_TYPE_LIST;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;
//JohnLiu 06112017 -- start
import commonui.FrameTitleHeader;
//JohnLiu 06112017 -- end

/** interface for the listeners/observers callback method */
interface FrameOctopusOperationListener {
    void FrameOctopusOperation_clickCancel(boolean bLoginRequired);
    void FrameOctopusOperation_forward(String sResponse);
    void FrameOctopusOperation_disconnect();
    void FrameOctopusOperation_timeout();
    void FrameOctopusOperation_handleNoResponse();
}
//JohnLiu 06112017 -- start
public class FrameOctopusOperation extends VirtualUIFrame{
//JohnLiu 06112017 -- end
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oTitleHeader;
	private VirtualUILabel m_oLabelValue1Header;
	private VirtualUILabel m_oLabelValue2Header;
	private VirtualUILabel m_oLabelValue1Value;
	private VirtualUILabel m_oLabelValue2Value;
	private VirtualUILabel m_oLabelInformation;
	private FrameCommonBasket m_oLastTransHistoryCommonBasket;
	
	private VirtualUIButton m_oButtonCancel;
	
	private String m_sResponse;
	
	// Flag to skip handle of timeout from client
	private boolean m_bSkipTimeout;
	
	// Flag to skip the posting version checking
	private boolean m_bSkipVersionCheck;
	
	// Flag to determine if user login is required after cancel
	private boolean m_bLoginRequired;
	
	// Operation type
	private String m_sOperationType;
	
	// Ask re-send packet count
	private int m_iResendCount;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameOctopusOperationListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameOctopusOperationListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameOctopusOperationListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
	public void init(){	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameOctopusOperationListener>();
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraOctopusOperation.xml");
//JohnLiu 06112017 -- start	
		m_oTitleHeader = new FrameTitleHeader();
        m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
        m_oTitleHeader.init(false);
        m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("octopus"));
        this.attachChild(m_oTitleHeader);
//JohnLiu 06112017 -- end
		
		// Value 1
		m_oLabelValue1Header = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelValue1Header, "lblValue1Header");
		this.attachChild(m_oLabelValue1Header);
		
		m_oLabelValue1Value = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelValue1Value, "lblValue1Value");
		this.attachChild(m_oLabelValue1Value);
		
		// Value 2
		m_oLabelValue2Header = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelValue2Header, "lblValue2Header");
		this.attachChild(m_oLabelValue2Header);
		
		m_oLabelValue2Value = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelValue2Value, "lblValue2Value");
		this.attachChild(m_oLabelValue2Value);
		
		// Information
		m_oLabelInformation = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelInformation, "lblInformation");
		this.attachChild(m_oLabelInformation);
		
		// Last transaction history list
		m_oLastTransHistoryCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oLastTransHistoryCommonBasket, "bktLastTransHistory");
		m_oLastTransHistoryCommonBasket.init();
		this.attachChild(m_oLastTransHistoryCommonBasket);
		
		// Create Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "btnCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		this.attachChild(m_oButtonCancel);
		
		m_sResponse = "";
		m_bSkipTimeout = false;
		m_bSkipVersionCheck = false;
		m_bLoginRequired = false;
		m_sOperationType = "";
		m_iResendCount= 0;
	}
	
	public void addShowCancelButtonTimer(){
		// Show cancel button after 10s
		this.addTimer("show_cancel_button", 10000, false, "show_cancel_button", false, true, null);
	}
	
	public void setShowCancelButtonTimer(boolean bStart){
		this.controlTimer("show_cancel_button", bStart);
	}
	
	public void addRetryShowCancelButtonTimer(){
		// Show cancel button after 20s
		this.addTimer("show_retry_cancel_button", 20000, false, "show_retry_cancel_button", false, true, null);
	}
	
	public void setRetryShowCancelButtonTimer(boolean bStart){
		this.controlTimer("show_retry_cancel_button", bStart);
	}
	
	public void addShowEmergencyPageTimer(){
		// Retry after 4s
		this.addTimer("show_emergency_page", 4000, false, "show_emergency_page", false, true, null);
	}
	
	public void setEmergencyPageTimer(boolean bStart){
		if(bStart){
			m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("reading_card")+"...");
		}
		
		this.controlTimer("show_emergency_page", bStart);
	}
	
	public void addAutoCloseResultScreenTimer(){
		// Auto close result screen after 5s
		this.addTimer("auto_close_screen", 5000, false, "auto_close_screen", false, false, null);
	}
	
	public void setAutoCloseResultScreenTimer(boolean bStart){
		this.controlTimer("auto_close_screen", bStart);
	}
	
	public void setAllowSkipTimeoutHandling(boolean bSkip){
		m_bSkipTimeout = bSkip;
	}
	
	public void setSkipVersionChecking(boolean bSkip){
		m_bSkipVersionCheck = bSkip;
	}
	
	public void showInitScreen(){
		m_oLabelValue1Header.setVisible(false);
		m_oLabelValue2Header.setVisible(false);
		m_oLabelValue1Value.setVisible(false);
		m_oLabelValue2Value.setVisible(false);
		// Resume information label position
		m_oTemplateBuilder.buildLabel(m_oLabelInformation, "lblInformation");
		m_oLabelInformation.setVisible(true);
		m_oLastTransHistoryCommonBasket.setVisible(false);
		m_oButtonCancel.setVisible(false);
		
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("octopus_initialization"));
		m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("loading")+"...");
		
		m_sOperationType = FormOctopusOperation.OPERATION_TYPE_LIST.init.name();
	}
	
	public void showAddValueScreen(String sValue){
		m_oLabelValue1Header.setVisible(true);
		m_oLabelValue2Header.setVisible(false);
		m_oLabelValue1Value.setVisible(true);
		m_oLabelValue2Value.setVisible(false);
		// Resume information label position
		m_oTemplateBuilder.buildLabel(m_oLabelInformation, "lblInformation");
		m_oLabelInformation.setVisible(true);
		m_oLastTransHistoryCommonBasket.setVisible(false);
		// Not show cancel button until 10s
		m_oButtonCancel.setVisible(false);
		addShowCancelButtonTimer();
		addShowEmergencyPageTimer();
		addRetryShowCancelButtonTimer();
		addAutoCloseResultScreenTimer();
		
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("octopus_add_value"));
		m_oLabelValue1Header.setValue(AppGlobal.g_oLang.get()._("add_amount")+" :");
		m_oLabelValue1Value.setValue(sValue);
		m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("please_place_the_card_on_the_card_reader"));
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		
		m_sOperationType = FormOctopusOperation.OPERATION_TYPE_LIST.add_value.name();
	}
	
	public void showDeductValueScreen(String sValue){
		m_oLabelValue1Header.setVisible(true);
		m_oLabelValue2Header.setVisible(true);
		m_oLabelValue1Value.setVisible(true);
		m_oLabelValue2Value.setVisible(true);
		// Resume information label position
		m_oTemplateBuilder.buildLabel(m_oLabelInformation, "lblInformation");
		m_oLabelInformation.setVisible(true);
		m_oLastTransHistoryCommonBasket.setVisible(false);
		// Not show cancel button until 10s
		m_oButtonCancel.setVisible(false);
		addShowCancelButtonTimer();
		addShowEmergencyPageTimer();
		addRetryShowCancelButtonTimer();
//JohnLiu 06112017 -- start
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("octopus_deduct_value"));
		m_oLabelValue1Header.setValue(AppGlobal.g_oLang.get()._("deduct_amount")+" :");
		m_oLabelValue1Value.setValue(sValue);
		m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("please_place_the_card_on_the_card_reader"));
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
//JohnLiu 06112017 -- end		
		m_sOperationType = FormOctopusOperation.OPERATION_TYPE_LIST.deduct_value.name();
	}
	
	public void showReadCardScreen(){
		m_oLabelValue1Header.setVisible(false);
		m_oLabelValue2Header.setVisible(false);
		m_oLabelValue1Value.setVisible(false);
		m_oLabelValue2Value.setVisible(false);
		// Resume information label position
		m_oTemplateBuilder.buildLabel(m_oLabelInformation, "lblInformation");
		m_oLabelInformation.setVisible(true);
		m_oLastTransHistoryCommonBasket.setVisible(false);
		m_oButtonCancel.setVisible(false);
		addAutoCloseResultScreenTimer();
		
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("read_octopus_card"));
		m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("please_place_the_card_on_the_card_reader"));
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		
		m_sOperationType = FormOctopusOperation.OPERATION_TYPE_LIST.read_card.name();
	}
	
	public void showStopScreen(){
		m_oLabelValue1Header.setVisible(false);
		m_oLabelValue2Header.setVisible(false);
		m_oLabelValue1Value.setVisible(false);
		m_oLabelValue2Value.setVisible(false);
		// Resume information label position
		m_oTemplateBuilder.buildLabel(m_oLabelInformation, "lblInformation");
		m_oLabelInformation.setVisible(true);
		m_oLastTransHistoryCommonBasket.setVisible(false);
		m_oButtonCancel.setVisible(false);
		
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("stop_octopus"));
		m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("loading")+"...");
		
		m_sOperationType = FormOctopusOperation.OPERATION_TYPE_LIST.close.name();
	}
	
	public void showXFileScreen(){
		m_oLabelValue1Header.setVisible(false);
		m_oLabelValue2Header.setVisible(false);
		m_oLabelValue1Value.setVisible(false);
		m_oLabelValue2Value.setVisible(false);
		// Resume information label position
		m_oTemplateBuilder.buildLabel(m_oLabelInformation, "lblInformation");
		m_oLabelInformation.setVisible(true);
		m_oLastTransHistoryCommonBasket.setVisible(false);
		m_oButtonCancel.setVisible(false);
		
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("create_octopus_transaction_file"));
		m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("loading")+"...");
		
		m_sOperationType = FormOctopusOperation.OPERATION_TYPE_LIST.xfile.name();
	}
	
	public void showAddValueResultScreen(String sTransAmount, String sNewValue){
		// Resume information label position
		m_oTemplateBuilder.buildLabel(m_oLabelInformation, "lblInformation");
		m_oLabelInformation.setVisible(false);
		m_oLastTransHistoryCommonBasket.setVisible(false);
		m_oButtonCancel.setVisible(true);
		
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("octopus_add_value"));
		m_oLabelValue1Header.setVisible(true);
		m_oLabelValue1Value.setVisible(true);
		m_oLabelValue1Header.setValue(AppGlobal.g_oLang.get()._("amount_added")+" :");
		m_oLabelValue1Value.setValue(sTransAmount);
		if (!sNewValue.isEmpty()) {
			m_oLabelValue2Header.setVisible(true);
			m_oLabelValue2Value.setVisible(true);
			m_oLabelValue2Header.setValue(AppGlobal.g_oLang.get()._("remaining_value")+" :");
			m_oLabelValue2Value.setValue(sNewValue);
		} else {
			m_oLabelValue2Header.setVisible(false);
			m_oLabelValue2Value.setVisible(false);
		}
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("exit"));
		
		setAutoCloseResultScreenTimer(true);
	}
	
//	public void showDeductValueResultScreen(String sTransAmount, String sNewValue){
//		m_oLabelValue1Header.setVisible(true);
//		m_oLabelValue2Header.setVisible(true);
//		m_oLabelValue1Value.setVisible(true);
//		m_oLabelValue2Value.setVisible(true);
//		// Resume information label position
//		m_oTemplateBuilder.buildLabel(m_oLabelInformation, "lblInformation");
//		m_oLabelInformation.setVisible(false);
//		m_oLastTransHistoryCommonBasket.setVisible(false);
//		m_oButtonCancel.setVisible(true);
//		
//		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("octopus_deduct_value"));
//		m_oLabelValue1Header.setValue(AppGlobal.g_oLang.get()._("amount_deducted")+" :");
//		m_oLabelValue1Value.setValue(sTransAmount);
//		m_oLabelValue2Header.setValue(AppGlobal.g_oLang.get()._("remaining_value")+" :");
//		m_oLabelValue2Value.setValue(sNewValue);
//		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("exit"));
//	}
	
	public void showReadCardResultScreen(String sDeviceId, String sOctopusNo, String sRemainingValue, ArrayList<ClsOctopusTransaction> oOctopusTransactions){
		m_oLabelValue2Header.setVisible(false);
		m_oLabelValue2Value.setVisible(false);
		// Move the information label position
		m_oLabelInformation.setTop(m_oLastTransHistoryCommonBasket.getTop() - m_oLabelInformation.getHeight());
		m_oLabelInformation.setVisible(true);
		m_oLastTransHistoryCommonBasket.setVisible(true);
		m_oButtonCancel.setVisible(true);
		
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("read_octopus_card"));
		if (!sRemainingValue.isEmpty()) {
			m_oLabelValue1Header.setVisible(true);
			m_oLabelValue1Value.setVisible(true);
			m_oLabelValue1Header.setValue(AppGlobal.g_oLang.get()._("remaining_value")+" :");
			m_oLabelValue1Value.setValue(sRemainingValue);
		} else {
			m_oLabelValue1Header.setVisible(false);
			m_oLabelValue1Value.setVisible(false);
		}
		m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("octopus_no") + " : " + sOctopusNo);
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("exit"));
		
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
    	ArrayList<String> sFieldValues = new ArrayList<String>();
    	ArrayList<String> sFieldAligns = new ArrayList<String>();
	   	iFieldWidths.add(50);
    	sFieldValues.add("");
    	sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
    	iFieldWidths.add(285);
    	sFieldValues.add(AppGlobal.g_oLang.get()._("transaction_date_time"));
    	sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
    	iFieldWidths.add(220);
    	sFieldValues.add(AppGlobal.g_oLang.get()._("amount"));
    	sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
    	iFieldWidths.add(200);
    	sFieldValues.add(AppGlobal.g_oLang.get()._("device_id"));
    	sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
    	m_oLastTransHistoryCommonBasket.addHeader(iFieldWidths, sFieldValues);
    	m_oLastTransHistoryCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
    	// Add transaction records
    	for(int i=0; i<oOctopusTransactions.size(); i++){
    		ClsOctopusTransaction oOctopusTransaction = oOctopusTransactions.get(i);
    		
    		sFieldValues.clear();
    		StringBuilder sTransactionNo = new StringBuilder();
    		sTransactionNo.append(i+1);
    		if(sDeviceId.length() >= 4 && oOctopusTransaction.getMachineId().equals(sDeviceId.substring(2))){
    			sTransactionNo.append(" #");
    		}
	    	sFieldValues.add(sTransactionNo.toString());
	    	sFieldValues.add(oOctopusTransaction.getTransactionTime());
	    	StringBuilder sAmount = new StringBuilder();
	    	if(oOctopusTransaction.getTransactionAmount().compareTo(BigDecimal.ZERO) > 0){
	    		sAmount.append("+");
	    	}
	    	sAmount.append("$");
	    	sAmount.append(oOctopusTransaction.getTransactionAmount().setScale(1).toPlainString());
	    	sFieldValues.add(sAmount.toString());
	    	sFieldValues.add(oOctopusTransaction.getMachineId());
	    	
	    	m_oLastTransHistoryCommonBasket.addItem(0, i, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
    	}
    	
    	setAutoCloseResultScreenTimer(true);
	}
	
	public void showCancelButton(boolean bShow){
		m_oButtonCancel.setVisible(bShow);
	}
	
	public void showCancelButtonThread(int iClientSockId){
		// Set the last client socket ID
		AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);

		m_oButtonCancel.setVisible(true);

		// Send the UI packet to client and the thread is finished
		super.getParentForm().finishUI(true);
	}
	
	public void hideCancelButton(){
		m_oButtonCancel.setVisible(false);
		
		m_oLabelValue2Header.setValue("");
		m_oLabelValue2Value.setValue("");
		m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("please_place_the_card_on_the_card_reader"));
		
		// Stop the show cancel button timer
		this.setShowCancelButtonTimer(false);
		
		// Start the 20s show cancel button timer
		this.setRetryShowCancelButtonTimer(true);
	}
	
	public boolean isCancelButtonHidden() {
		return !m_oButtonCancel.getVisible();
	}
	
	public void showEmergencyPage(int iClientSockId){
		// Set the last client socket ID
		AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);

		m_oLabelValue2Header.setValue(">>>>>>>>>>>>>>>");
		m_oLabelValue2Value.setValue(AppGlobal.g_oLang.get()._("error"));
		if(m_sOperationType.equals(OPERATION_TYPE_LIST.deduct_value.name())){
			m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("if_octopus_amount_is_deducted") + System.lineSeparator()
							+ AppGlobal.g_oLang.get()._("please_use_octopus_offline_payment_to_settle_check"));
		}else
		if(m_sOperationType.equals(OPERATION_TYPE_LIST.add_value.name())){
			m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("add_value_record_is_lost_in_system"));
		}
		m_oButtonCancel.setVisible(true);

		m_bLoginRequired = true;
		
		// Send the UI packet to client and the thread is finished
		super.getParentForm().finishUI(true);
	}
	
	public void createForwardEvent(String sValue, int iTimeout, int iDelay){
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestValue(sValue);
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(iTimeout);
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestDelay(iDelay);
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
        if (iChildId == m_oButtonCancel.getId()) {
        	for (FrameOctopusOperationListener listener : listeners) {
        		// Raise the event to parent
           		listener.FrameOctopusOperation_clickCancel(m_bLoginRequired);
            }
        	
        	bMatchChild = true;
        }
        
        return bMatchChild;
    }
	
	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if(iId == super.getIDForPosting().hashCode()) {
			if(sNote.equals("show_cancel_button") || sNote.equals("show_retry_cancel_button")){
				// Show cancel button
				//Create parameter array
				Object[] oParameters = new Object[1];
				oParameters[0] = iClientSockId;
				
				this.getParentForm().addTimerThread(iClientSockId, this, "showCancelButtonThread", oParameters);
			}
			else if(sNote.equals("auto_close_screen")){
				//Set the last client socket ID
				AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
				
				for (FrameOctopusOperationListener listener : listeners) {
	        		// Raise the event to parent
	           		listener.FrameOctopusOperation_clickCancel(false);
	            }
				
				// Send the UI packet to client and the thread is finished
				super.getParentForm().finishUI(true);
			}
			else{
				if(m_iResendCount >= 3){
					// After retry 3 times
					// Show emergency page
					//Create parameter array
					Object[] oParameters = new Object[1];
					oParameters[0] = iClientSockId;
					
					this.getParentForm().addTimerThread(iClientSockId, this, "showEmergencyPage", oParameters);
					
					m_iResendCount = 0;
				}else{
					//Set the last client socket ID
					AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
					
					for (FrameOctopusOperationListener listener : listeners) {
		        		// Raise the event to parent
		           		listener.FrameOctopusOperation_handleNoResponse();
		            }
					
					// Send the UI packet to client and the thread is finished
					super.getParentForm().finishUI(true);
					
					m_iResendCount = m_iResendCount + 1;
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean forward(int iChildId, String sNote, String sStatus) {
		boolean bMatchChild = false;
		
        if (iChildId == AppGlobal.g_oDeviceManagerElement.get().getId()) {
        	for (FrameOctopusOperationListener listener : listeners) {
				if(m_bSkipVersionCheck || sNote.equals(AppGlobal.g_oDeviceManagerElement.get().getForwardServerRequestNote())){
					// Current event's version is up-to-date

	        		// Raise the event to parent
	        		if(sStatus.equals("disconnected"))
	        			listener.FrameOctopusOperation_disconnect();
	        		else
	        		if(sStatus.equals("time_out")){
	        			if(!m_bSkipTimeout)
	        				listener.FrameOctopusOperation_timeout();
	        			
	        			try{
	        				StringBuilder sb = new StringBuilder();
	        				sb.append("Timeout during operation");
	        				AppGlobal.writeDebugLog("FrameOctopusOperation", new Exception().getStackTrace()[0].getMethodName(), sb.toString());
	        			}catch(Exception e){
	        				AppGlobal.stack2Log(e);
	        			}
	        		}else{
	        			m_sResponse += AppGlobal.g_oDeviceManagerElement.get().getValue(); 
	        			if(!m_sResponse.isEmpty()){
	        				if(m_sResponse.charAt(m_sResponse.length() - 1) == 0) {
	        					// Copy the string to local variable to prevent double handle to the response if exception occur
	        					String sResponse = m_sResponse;
	        					m_sResponse = "";
	        					listener.FrameOctopusOperation_forward(sResponse.substring(0, sResponse.length() - 1));
	        				}
	        			}
	        		}
				}else{
					// Incorrect forwarder version no.
		        	try{
						StringBuilder sb = new StringBuilder();
						sb.append("Incorrect forwarder version, ");
						sb.append("in: " + sNote + ", current: " + AppGlobal.g_oDeviceManagerElement.get().getForwardServerRequestNote());
						AppGlobal.writeDebugLog("FrameOctopusOperation", new Exception().getStackTrace()[0].getMethodName(), sb.toString());
					}catch(Exception e){
						AppGlobal.stack2Log(e);
					}
				}
            }
        	
        	bMatchChild = true;
        }else{
        	// Incorrect forwarder ID
        	try{
				StringBuilder sb = new StringBuilder();
				sb.append("Incorrect forwarder ID, ");
				sb.append("in: " + iChildId + ", current: " + AppGlobal.g_oDeviceManagerElement.get().getId());
				AppGlobal.writeDebugLog("FrameOctopusOperation", new Exception().getStackTrace()[0].getMethodName(), sb.toString());
			}catch(Exception e){
				AppGlobal.stack2Log(e);
			}
        }
        
        return bMatchChild;
	}
	
}