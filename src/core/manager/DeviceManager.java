package core.manager;

import core.controller.ForwarderController;
import core.controller.MSRController;
import core.listener.MSRListener;
import core.listener.ForwarderListener;

public class DeviceManager {

	public OctopusManager octopusManager;
	public MSRManager msrManager;
	
	public DeviceManager() {
		octopusManager = new OctopusManager();
		msrManager = new MSRManager();
	}
	
	public class OctopusManager {
		
		ForwarderController m_oOctopusController;
		
		public OctopusManager() {
			
		}
		
		public void bindControllerOctopus(ForwarderController oControllerOctopus) {
			m_oOctopusController= oControllerOctopus;
		}
		
		public void initConnection(String sIPAddress, int iPortNo) {
			m_oOctopusController.initConnection(sIPAddress, iPortNo);
		}
		
		public void setForwardRequestTimeout(int iTimeout) {
			m_oOctopusController.setForwardRequestTimeout(iTimeout);
		}
		
		public void setForwardRequestValue(String sValue) {
			m_oOctopusController.setForwardRequestValue(sValue);
		}
		
		public void setForwardRequestDelay(int iDelay) {
			m_oOctopusController.setForwardRequestDelay(iDelay);
		}
		
		public String getValue() {
			return m_oOctopusController.getValue();
		}
		
		public String getForwardServerRequestNote() {
			return m_oOctopusController.getForwardServerRequestNote();
		}
		
		public void setListener(ForwarderListener listener) {
			m_oOctopusController.removeAllListener();
			m_oOctopusController.addListener(listener);
	    }
	    
	    public void removeListener(ForwarderListener listener) {
	    	m_oOctopusController.removeListener(listener);
	    }
	    
	    public void removeAllListener() {
	    	m_oOctopusController.removeAllListener();
	    }
	}
	
	public class MSRManager {
		
		MSRController m_oMSRController;
		
		public MSRManager() {
			
		}
		
		public void bindControllerMSR(MSRController oControllerMSR) {
			m_oMSRController = oControllerMSR;
		}
		
		public void setListener(MSRListener listener) {
			m_oMSRController.removeAllListener();
			m_oMSRController.addListener(listener);
	    }
	    
	    public void removeListener(MSRListener listener) {
	    	m_oMSRController.removeListener(listener);
	    }
	    
	    public void removeAllListener() {
	    	m_oMSRController.removeAllListener();
	    }
	}
}
