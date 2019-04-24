package commonui;

import java.util.ArrayList;

import app.AppGlobal;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIKeyboardReader;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameConfirmBoxListener {
    void FrameConfirmBox_clickOK();
    void FrameConfirmBox_clickCancel();
    boolean FrameConfirmBox_timeout();
}

public class FrameConfirmBox extends VirtualUIFrame {
	
	TemplateBuilder m_oTemplateBuilder;
	
	//private VirtualUILabel m_oLabelTitle;
	private VirtualUILabel m_oLabelMessage;
	private VirtualUIButton m_oButtonOK;
	private VirtualUIButton m_oButtonCancel;
	private VirtualUIKeyboardReader m_oKeyboardReaderForOK;
	
	// Loading screen
	private FrameProcessBox m_oFrameLoadingPanel;
	
	private FrameTitleHeader m_oTitleHeader;

	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameConfirmBoxListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameConfirmBoxListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameConfirmBoxListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
    public FrameConfirmBox(){	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameConfirmBoxListener>();
	}
	
    public void init(String sOKButtonValue, String sCancelButtonValue){
    	// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraConfirmBox.xml");
		
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(false);
		this.attachChild(m_oTitleHeader);
		
		// DialogBox Message
		m_oLabelMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMessage, "lblMessage");
		m_oLabelMessage.setTop(m_oTitleHeader.getHeight());
		m_oLabelMessage.setHeight(this.getHeight() - m_oTitleHeader.getHeight() - 56 - 20);
		this.attachChild(m_oLabelMessage);
//KingsleyKwan20171016ByKing	----- End -----
		// OK Button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "butOK");
		m_oButtonOK.setValue(sOKButtonValue);
		m_oButtonOK.setTop(this.getHeight() - m_oButtonOK.getHeight() - 20);
		// Delete by King Cheung 2017-10-19
		//m_oButtonOK.setLeft((this.getWidth()/2) - m_oButtonOK.getWidth() - 5);
		this.attachChild(m_oButtonOK);
		
		// Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "butCancel");
		m_oButtonCancel.setValue(sCancelButtonValue);
		m_oButtonCancel.setTop(this.getHeight() - m_oButtonCancel.getHeight() - 20);
		// Delete by King Cheung 2017-10-19
		//m_oButtonCancel.setLeft((this.getWidth()/2) + 5);
		m_oButtonCancel.setClickServerRequestBlockUI(false);
		this.attachChild(m_oButtonCancel);

		// Keyboard
		m_oKeyboardReaderForOK = new VirtualUIKeyboardReader();
		m_oKeyboardReaderForOK.addKeyboardKeyCode(13);
		this.attachChild(m_oKeyboardReaderForOK);
		
		// Resize the message label
		m_oLabelMessage.setHeight(this.getHeight() - m_oTitleHeader.getHeight() - (m_oButtonOK.getHeight() + 20));
		
		// Add refresh screen during click close button for emergency process
		m_oFrameLoadingPanel = new FrameProcessBox();
		m_oFrameLoadingPanel.setWidth(this.getWidth());
		m_oFrameLoadingPanel.setHeight(this.getHeight());
		m_oFrameLoadingPanel.setExist(true);
		m_oFrameLoadingPanel.setBackgroundColor(this.getBackgroundColor());
		m_oFrameLoadingPanel.init(false);
		m_oFrameLoadingPanel.setTitle(AppGlobal.g_oLang.get()._("loading") + "...");
		m_oFrameLoadingPanel.setMessage(AppGlobal.g_oLang.get()._("please_wait_for_processing"));
		m_oFrameLoadingPanel.addRefreshButton(AppGlobal.g_oLang.get()._("refresh"));
		m_oFrameLoadingPanel.setVisible(false);
		this.attachChild(m_oFrameLoadingPanel);
	}
	
	public void supportLoadingScreen() {
		m_oButtonOK.addClickVisibleElements(m_oFrameLoadingPanel, true);
	}

	public void setTitle(String sTitle){
		m_oTitleHeader.setTitle(sTitle);
	}
	
	public void setMessage(String sMessage){
		m_oLabelMessage.setValue(sMessage);
	}
    
	public void setTimeout(int iTimeout){
		this.addTimer("timeout", iTimeout, false, "timeout", true, true, null);
	}
	
	public void setTimeoutTimer(boolean bStart){
		this.controlTimer("timeout", bStart);
	}
	
	public void setMessageTextAlign(String sTextAlign) {
		// Add default padding to the lable
		m_oLabelMessage.setPaddingValue("10");
		m_oLabelMessage.setTextAlign(sTextAlign);
	}
	
	// Set the client timeout after click "OK" button (e.g. for SPA Auth waiting response)
	public void setConfirmTimeout(int iTimeout) {
		m_oButtonOK.setClickServerRequestTimeout(iTimeout);
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if(m_oButtonOK.getId() == iChildId){
			for (FrameConfirmBoxListener listener : listeners) {
				// Raise the event to parent
	       		listener.FrameConfirmBox_clickOK();
	       		bMatchChild = true;
	       		break;
			}
		}else
		if(m_oButtonCancel.getId() == iChildId){
			for (FrameConfirmBoxListener listener : listeners) {
				// Raise the event to parent
	       		listener.FrameConfirmBox_clickCancel();
	       		bMatchChild = true;
	       		break;
			}
		}
    	
    	return bMatchChild;
    }
	
	@Override
    public boolean keyboard(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
        if (iChildId == m_oKeyboardReaderForOK.getId()) {
        	for (FrameConfirmBoxListener listener : listeners) {        		
        		// Raise the event to parent
           		listener.FrameConfirmBox_clickOK();
            }
        	
        	bMatchChild = true;
        }
        
    	return bMatchChild;
	}
	
	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if(iId == super.getIDForPosting().hashCode()) {
			if(sNote.equals("timeout")){
				// Ordering timeout
				AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
				
				for (FrameConfirmBoxListener listener : listeners) {
		    		// Raise the event to parent
		       		listener.FrameConfirmBox_timeout();
		        }
				// Send the UI packet to client and the thread is finished
				super.getParentForm().finishUI(true);
			}
			return true;
		}
		return false;
	}
}
