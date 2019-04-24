package app;

import java.util.ArrayList;

import virtualui.HeroActionProtocol;
import virtualui.VirtualUIFrame;

/** interface for the listeners/observers callback method */
interface FrameDeviceManagerListener {
    void frameDeviceManager_finish();
}

public class FrameDeviceManager extends VirtualUIFrame {
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameDeviceManagerListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameDeviceManagerListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameDeviceManagerListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }

    public void init(String sIPAddress, int iPortNo) {
		listeners = new ArrayList<FrameDeviceManagerListener>();
		
		// Create a forwarder
		AppGlobal.g_oDeviceManagerElement.get().allowForward(true);
		// Socket type
		AppGlobal.g_oDeviceManagerElement.get().addForwardServerRequestSubmitElement(AppGlobal.g_oDeviceManagerElement.get());
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestType(HeroActionProtocol.View.Event.Forward.ForwardRequest.Type.SOCKET);
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestAddress(sIPAddress);
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestPort(iPortNo);
		AppGlobal.g_oDeviceManagerElement.get().setForwardServerRequestBlockUI(true);
		if (AppGlobal.g_oDeviceManagerElement.get().getId() == 0)
			// Not yet added to system element, add to system element
			AppGlobal.g_oTerm.get().addGlobalElement(AppGlobal.g_oDeviceManagerElement.get());
		
		// Create a timer to close current form
		addCloseFormTimer();
		
		startCloseForm();
    }
    
    // Create a timer to close current form
	private void addCloseFormTimer(){
		this.addTimer("close_form", 0, false, "", false, false, null);
	}
	
	// Start to update basket
	public void startCloseForm(){
		this.controlTimer("close_form", true);
	}
	
	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if(iId == super.getIDForPosting().hashCode()) {
			// Ask close form
			//Set the last client socket ID
			AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
			
			for (FrameDeviceManagerListener listener : listeners) {
				// Raise the event to parent
				listener.frameDeviceManager_finish();
			}
			
			// Send the UI packet to client and the thread is finished
			super.getParentForm().finishUI(true);

			return true;
		}
		
		return false;
	}
}
