package app;

import core.Controller;
import virtualui.*;

public class FormDeviceManager extends VirtualUIForm implements FrameDeviceManagerListener {
	
	private FrameDeviceManager m_oFrameDeviceManager;
	
	public FormDeviceManager(Controller oParentController) {
		super(oParentController);
	}
	
	public void init(String sIPAddress, int iPortNo){
		m_oFrameDeviceManager = new FrameDeviceManager();
		m_oFrameDeviceManager.init(sIPAddress, iPortNo);
		// Add listener;
		m_oFrameDeviceManager.addListener(this);
		this.attachChild(m_oFrameDeviceManager);
	}

	@Override
	public void frameDeviceManager_finish() {
		// Finish showing this form
		this.finishShow();
	}

}
