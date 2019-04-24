package app.commonui;

import java.util.ArrayList;

import app.controller.AppGlobal;
import core.templatebuilder.TemplateBuilder;
import core.virtualui.VirtualUIFrame;
import core.virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameProcessBoxListener {
    void FrameProcessBox_finishShow();
}

public class FrameProcessBox extends VirtualUIFrame {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUILabel m_oLabelTitle;
	private VirtualUILabel m_oLabelMessage;

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
	private void addFinishShowTimer(){
		this.addTimer("finish_show_timer", 0, false, "", false, false, null);
	}
	
	// Start to update basket
	public void startFinishShowTimer(){		
		this.controlTimer("finish_show_timer", true);
	}
    
    public void init(){
    	// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraDialogBox.xml");
		
		// DialogBox Title
		m_oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTitle, "lblTitle");
		m_oLabelTitle.setWidth(this.getWidth() - (m_oLabelTitle.getLeft() * 2));
		this.attachChild(m_oLabelTitle);
		
		// DialogBox Message
		m_oLabelMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMessage, "lblMessage");
		m_oLabelMessage.setTop(m_oLabelTitle.getHeight());
		m_oLabelMessage.setWidth(this.getWidth());
		m_oLabelMessage.setHeight(this.getHeight() - m_oLabelTitle.getHeight());
		this.attachChild(m_oLabelMessage);
		
		// Create timer
		addFinishShowTimer();
		
		// Start the timer
		startFinishShowTimer();
    }
    
	public void setTitle(String sTitle){
		m_oLabelTitle.setValue(sTitle);
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
			
			// Send the UI packet to client and the thread is finished
			super.getParentForm().finishUI(true);

			return true;
		}
		
		return false;
	}
}
