package core.controller;

import java.util.ArrayList;

import core.Controller;
import core.listener.ForwarderListener;
import core.virtualui.HeroActionProtocol;
import core.virtualui.VirtualUIBasicElement;

public class ForwarderController extends Controller {
	
	VirtualUIOctopus m_oVirtualUIOctopus;
    ArrayList<ForwarderListener> m_oListeners;
	
	public ForwarderController(Controller oParentController) {
		super(oParentController);
		
		this.setCoverWidth(0);
		this.setCoverHeight(0);
		m_oListeners = new ArrayList<ForwarderListener>();
		
		m_oVirtualUIOctopus = new VirtualUIOctopus();
		//m_oVirtualUIOctopus.allowForward(true);
		//m_oVirtualUIOctopus.addForwardServerRequestSubmitElement(m_oVirtualUIOctopus);
		//m_oVirtualUIOctopus.setForwardForwardRequestType(HeroActionProtocol.View.Event.Forward.ForwardRequest.Type.SOCKET);
		//m_oVirtualUIOctopus.setForwardServerRequestBlockUI(true);
		this.attachChild(m_oVirtualUIOctopus);
		
		// Create a timer to close current form
		addCloseFormTimer();
		
		startCloseForm();
	}
	
	public void initConnection(String sIPAddress, int iPortNo) {
		//m_oVirtualUIOctopus.setForwardForwardRequestAddress(sIPAddress);
		//m_oVirtualUIOctopus.setForwardForwardRequestPort(iPortNo);
	}
	
	public void setForwardRequestTimeout(int iTimeout) {
		//m_oVirtualUIOctopus.setForwardForwardRequestTimeout(iTimeout);
	}
	
	public void setForwardRequestValue(String sValue) {
		//m_oVirtualUIOctopus.setForwardForwardRequestValue(sValue);
	}
	
	public void setForwardRequestDelay(int iDelay) {
		//m_oVirtualUIOctopus.setForwardForwardRequestDelay(iDelay);
	}
	
	public String getValue() {
		return m_oVirtualUIOctopus.getValue();
	}
	
	public String getForwardServerRequestNote() {
		//return m_oVirtualUIOctopus.getForwardServerRequestNote();
		return "";
	}
    
    // Create a timer to close current form
	private void addCloseFormTimer(){
		//m_oVirtualUIOctopus.addTimer("close_form", 0, false, "", false, false, null);
	}
	
	// Start to update basket
	public void startCloseForm(){
		//m_oVirtualUIOctopus.controlTimer("close_form", true);
	}
	
    public void addListener(ForwarderListener listener) {
    	m_oListeners.add(listener);
    }
    
    public void removeListener(ForwarderListener listener) {
    	m_oListeners.remove(listener);
    }
    
    public void removeAllListener() {
    	m_oListeners.clear();
    }
	
    class VirtualUIOctopus extends VirtualUIBasicElement {
    	
    	public VirtualUIOctopus(){
    		super(HeroActionProtocol.View.Type.FORWARDER);
        }
    	

    	
    	@Override
    	public void timer(int iId){
//    		if(sId.equals(super.getIDForPosting())){
    			// Ask close form
    			//Set the last client socket ID
//    			Core.g_oTerm.get().setClientSocketId(iClientSockId);
    			
    			for (ForwarderListener oListener : m_oListeners) {
    				// Raise the event to parent
    				oListener.onFinish();
    			}
//        		showCancelButton();
    			
    			// Send the UI packet to client and the thread is finished
//    			super.getParentForm().finishUI(true);

//    			return true;
//    		}
    		
//    		return false;
    	}
    	

    	
    	@Override
        public void clicked(int iChildId) {
//    		boolean bMatchChild = false;
    		
//            if (iChildId == m_oButtonCancel.getId() || iChildId == m_oButtonExit.getId()) {
            	for (ForwarderListener oListener : m_oListeners) {
            		// Raise the event to parent
            		oListener.onCancel();
                }
            	
//            	bMatchChild = true;
//            }
            
            //	return bMatchChild;
        }
    	
    	@Override
    	public void forward(int iChildId, String sStatus) {
    		/*
//    		boolean bMatchChild = false;
    		
//            if (iChildId == Core.g_oDeviceManagerElement.get().getId()) {
            	for (ForwarderListener oListener : m_oListeners) {
    				if(sNote.equals(m_oVirtualUIOctopus.getForwardServerRequestNote())){
    					// Current event's version is up-to-date

    	        		// Raise the event to parent
    	        		if(sStatus.equals("disconnected"))
    	        			oListener.onDisconnect();
    	        		else
    	        		if(sStatus.equals("time_out"))
    	        			oListener.onTimeout();
    	        		else
    	        			oListener.onForward(m_oVirtualUIOctopus.getValue());
    				}
                }
            	
//            	bMatchChild = true;
//            }
            
            //	return bMatchChild;
             *
             */
    	}
    }
}
