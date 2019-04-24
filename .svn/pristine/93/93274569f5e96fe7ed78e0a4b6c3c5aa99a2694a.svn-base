package core.manager;

import java.util.HashSet;

import core.Controller;
import core.controller.RootController;
import core.externallib.ThreadLocalSet;

public class ControllerManager {

	private ThreadLocalSet<Controller> m_oThreadActiveController;
	private RootController m_oRootController;

	public ControllerManager() {
		m_oThreadActiveController = new ThreadLocalSet<Controller>();
		m_oRootController = new RootController(null);
	}
	
	public void close() {
		//	Stop all controller thread
		HashSet<Controller> controllerSet = m_oThreadActiveController.getSetCopy();
		for (Controller controller : controllerSet) {
			//	Wake thread if waiting
			controller.forceClose();
		}
	}
	
	public void setThreadActiveController(Controller oController) {
		m_oThreadActiveController.set(oController);
	}
	
	public void removeThreadActiveController() {
		m_oThreadActiveController.remove();
	}
	
	public Controller getThreadActiveController() {
		return m_oThreadActiveController.get();
	}
	
	public Controller getRootController() {
		return m_oRootController;
	}
}
