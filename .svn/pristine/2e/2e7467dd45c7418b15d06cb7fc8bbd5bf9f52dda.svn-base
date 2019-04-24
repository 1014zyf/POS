package app.commonui;

import app.controller.AppGlobal;

import core.templatebuilder.TemplateBuilder;
import core.virtualui.VirtualUIFrame;
import core.virtualui.VirtualUILabel;

public class FrameShowOnceProcessBox extends VirtualUIFrame {
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUILabel m_oLabelMessage;
	
	private boolean bFinish;
	
	public FrameShowOnceProcessBox(){	
		m_oTemplateBuilder = new TemplateBuilder();
	}
	
    // Create update basket timer
	private void addFinishShowTimer(){
		this.addTimer("check_status_timer", 500, true, "", false, false, null);
	}
	
	// Start to check status
	public void startFinishShowTimer(){		
		this.controlTimer("check_status_timer", true);
	}
	
	// Stop to check status
	public void stopFinishShowTimer(){		
		this.controlTimer("check_status_timer", false);
	}
    
    public void init(){
    	// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraShowOnceProcessBox.xml");
		
		// Process Message
		m_oLabelMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMessage, "lblMessage");
		this.attachChild(m_oLabelMessage);
		
		// Create timer
		addFinishShowTimer();
    }
	
	public void setMessage(String sMessage){
		m_oLabelMessage.setValue(sMessage);
	}
    
	public void finishShow(){
		bFinish = true;
		
		this.setVisible(false);
		this.stopFinishShowTimer();
	}
	
	public void showProcessBox(){
		
		bFinish = false;
		
		this.setVisible(true);
		// Start the timer
		startFinishShowTimer();
	}
	
	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if(iId == super.getIDForPosting().hashCode()){
			// Ask drawing basket
			//Set the last client socket ID
			AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
			
			if(bFinish){
				this.setVisible(false);
				this.stopFinishShowTimer();
			}
			
			// Send the UI packet to client and the thread is finished
			super.getParentForm().finishUI(true);

			return true;
		}
		
		return false;
	}
}
