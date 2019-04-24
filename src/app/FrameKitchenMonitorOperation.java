package app;

import java.util.ArrayList;

import commonui.FrameTitleHeader;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameKitchenMonitorOperationListener {
	void FrameKitchenMonitorOperation_forward(String sResponse);
	void FrameKitchenMonitorOperation_timeout();
	void FrameKitchenMonitorOperation_disconnect();
}

public class FrameKitchenMonitorOperation extends VirtualUIFrame {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUILabel m_oLabelInformation;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameKitchenMonitorOperationListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameKitchenMonitorOperationListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameKitchenMonitorOperationListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public void init(){	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameKitchenMonitorOperationListener>();
		
		// Load child elements from template
		// Load form from template file
		//m_oTemplateBuilder.loadTemplate("fraKitchenMonitorOperation.xml");

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
	    m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
	    m_oFrameTitleHeader.init(false);
	    this.attachChild(m_oFrameTitleHeader);
		
		// Information
		m_oLabelInformation = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelInformation, "lblInformation");
		this.attachChild(m_oLabelInformation);
	}
	
	public void showInitScreen() {
		// Resume information label position
		m_oTemplateBuilder.buildLabel(m_oLabelInformation, "lblInformation");
		m_oLabelInformation.setVisible(true);
		//m_oButtonCancel.setVisible(false);
		
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("kitchen_monitor_initialization"));
		m_oLabelInformation.setValue(AppGlobal.g_oLang.get()._("loading")+"...");
		
	}
	
	public void createForwardEvent(String sValue, int iTimeout, int iDelay) {
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestValue(sValue);
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(iTimeout);
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestDelay(iDelay);
	}
	
	@Override
	public boolean forward(int iChildId, String sNote, String sStatus) {
		boolean bMatchChild = false;
		
		if (iChildId == AppGlobal.g_oDeviceManagerElement.get().getId()) {
			for (FrameKitchenMonitorOperationListener listener : listeners) {
				if (sNote.equals(AppGlobal.g_oDeviceManagerElement.get().getForwardServerRequestNote())) {
					// Current event's version is up-to-date
					
					// Raise the event to parent
					if (sStatus.equals("disconnected")){
						listener.FrameKitchenMonitorOperation_disconnect();
					}else
					if (sStatus.equals("time_out")) {
						listener.FrameKitchenMonitorOperation_timeout();
					} else{
						listener.FrameKitchenMonitorOperation_forward(AppGlobal.g_oDeviceManagerElement.get().getValue());
					}
				}
			}
			
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
}
