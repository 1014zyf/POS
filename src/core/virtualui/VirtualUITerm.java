package core.virtualui;

import core.virtualui.VirtualUIBasicElement;

public class VirtualUITerm  {	
	private String m_sClientImageURL;
	
	public VirtualUITerm() {
		m_sClientImageURL = "";
	}
	
	public void redrawScreen() {
		
	}
	
	public void processClientReconnectRelogin() {
	
	}
	
	public void removeAllUI() {
		
	}
	
	// Hide keyboard
	public void hideKeyboard() {
		
	}
	
	// // Hide keyboard
	public void showKeyboard() {
		
	}
	
	public void setClientSocketId(int iClientSockId) {
		
	}
	
	public void setClientImageURLPath(String sURL) {
		m_sClientImageURL = sURL;
	}
	
	public String getClientImageURLPath() {
		return m_sClientImageURL;
	}
	
	public void processKillRequest() {
		
	}
	
	public void closeTerm() {
		
	}
	
	public void addSystemElement(VirtualUIBasicElement oVirtualUIBasicElement) {
		
	}
}
