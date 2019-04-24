package commonui;

import java.util.ArrayList;

import app.AppGlobal;
import app.ClsActiveClient;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameProcessBoxListener {
    void FrameProcessBox_finishShow();
}

public class FrameProcessBox extends VirtualUIFrame {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUILabel m_oLabelMessage;
	private FrameTitleHeader m_oTitleHeader;

	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameProcessBoxListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameProcessBoxListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameProcessBoxListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
    public FrameProcessBox(){	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameProcessBoxListener>();	
	}
	
    // Create update basket timer
	private void addFinishShowTimer(boolean bSetMaximumClientTimeout){
		if (bSetMaximumClientTimeout) {
			ClsActiveClient oActiveClient = AppGlobal.getActiveClient();
			if (oActiveClient != null) {
				this.addTimer("finish_show_timer", 0, false, "", false, false, null, oActiveClient.getMaximumClientTimeout());
				
				// To prevent the swipe card event trigger timeout
				oActiveClient.setMaximumClientTimeoutForSwipeCardEvent();
			} else 
				this.addTimer("finish_show_timer", 0, false, "", false, false, null);
		} else
			this.addTimer("finish_show_timer", 0, false, "", false, false, null);
	}
	
	// Start to update basket
	public void startFinishShowTimer(){		
		this.controlTimer("finish_show_timer", true);
	}
    
    public void init(boolean bSetMaximumClientTimeout){
    	// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraDialogBox.xml");
		
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(false);
		this.attachChild(m_oTitleHeader);
		
		// DialogBox Message
		m_oLabelMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMessage, "lblMessage");
		m_oLabelMessage.setTop(m_oTitleHeader.getHeight());
		m_oLabelMessage.setHeight(this.getHeight() - m_oTitleHeader.getHeight());
		this.attachChild(m_oLabelMessage);
		
		// Create timer
		addFinishShowTimer(bSetMaximumClientTimeout);
		
		// Start the timer
		startFinishShowTimer();
	}
	
	public void addRefreshButton(String sBtnValue) {
		VirtualUIButton oNewBtn = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(oNewBtn, "singleBtn");
		oNewBtn.setValue(sBtnValue);
		oNewBtn.setTop(this.getHeight() - oNewBtn.getHeight() - 20);
		oNewBtn.setLeft((this.getWidth()/2) - (oNewBtn.getWidth()/2));
		oNewBtn.setClickServerRequestBlockUI(false);
		this.attachChild(oNewBtn);
	
		// Resize the message label
		m_oLabelMessage.setHeight(this.getHeight() - m_oTitleHeader.getHeight() - (oNewBtn.getHeight() + 20));
	}

	public void setTitle(String sTitle){
		m_oTitleHeader.setTitle(sTitle);
	}
	
	public void setMessage(String sMessage){
		m_oLabelMessage.setValue(sMessage);
	}
    
	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if(iId == super.getIDForPosting().hashCode()) {
			// Ask drawing basket
			//Set the last client socket ID
			AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
			
			for (FrameProcessBoxListener listener : listeners) {
				// Raise the event to parent
				listener.FrameProcessBox_finishShow();
			}
			
			// Resume client timeout to default for swipe card event
			ClsActiveClient oActiveClient = AppGlobal.getActiveClient();
			if (oActiveClient != null)
				oActiveClient.resumeClientTimeoutForSwipeCardEvent();
			
			// Send the UI packet to client and the thread is finished
			super.getParentForm().finishUI(true);

			return true;
		}
		
		return false;
	}
}
