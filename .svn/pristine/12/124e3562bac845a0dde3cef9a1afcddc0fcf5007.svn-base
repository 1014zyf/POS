package core.controller;

import app.controller.AppController;
import core.Controller;
import core.Core;
import core.manager.ActiveClient;
import core.virtualui.HeroActionProtocol;
import core.virtualui.VirtualUIBasicElement;
import core.virtualui.VirtualUIEvent;

public class RootController extends Controller {
	
	public static final int LAUNCH_APP_ELEMENT_RESERVED_ID = -1;
	
	AppController m_oAppController;
	
//	private MSRController m_oControllerMSR;
//	private ForwarderController m_oControllerOctopus;
	
	VirtualUIBasicElement m_oLaunchAppElement;
//	VirtualUIBasicElement m_oUIStreamElement;
	
	public RootController(Controller oParentController) {
		super(oParentController);
		
//		ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient();
		
		//		MSR
//		m_oControllerMSR = new MSRController(this);
//		m_oControllerMSR.show2();
		//	bind to device manager
//		oActiveClient.g_oDeviceManager.msrManager.bindControllerMSR(m_oControllerMSR);
		
		//	Octopus
//		m_oControllerOctopus = new ForwarderController(this);
//		m_oControllerOctopus.show2();
		//	bind to device manager
//		oActiveClient.g_oDeviceManager.octopusManager.bindControllerOctopus(m_oControllerOctopus);
		
		// In this version, the root controller is used to handle the "launch" event from client
		m_oLaunchAppElement = new VirtualUIBasicElement(HeroActionProtocol.View.Type.BUTTON, LAUNCH_APP_ELEMENT_RESERVED_ID) {
			@Override
			public void clicked(int iId) {
				//	Start App Controller
				if (m_oAppController == null) {		//	First launch
					m_oAppController = new AppController(null);
					m_oAppController.show2();
				}
				else {
					ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient();	//	Resume
					oActiveClient.g_oUIManager.redrawScreen();
				}
			}
		};
		m_oLaunchAppElement.addEvent(new VirtualUIEvent(VirtualUIEvent.EVENT_CLICK));
		attachChild(m_oLaunchAppElement);
		
		//	for streaming UI update
//		m_oUIStreamElement = new VirtualUIBasicElement(HeroActionProtocol.View.Type.BUTTON) {
//			@Override
//			public void timer(int iId) {
//				//	Do nothing to complete event, and then send UI JSON
//			}
//		};
//		VirtualUIEvent oUIStreamTimerEvent = new VirtualUIEvent(VirtualUIEvent.EVENT_TIMER);
//		oUIStreamTimerEvent.setTime(1000);
//		m_oUIStreamElement.addEvent(oUIStreamTimerEvent);
//		attachChild(m_oUIStreamElement);
	}
}
