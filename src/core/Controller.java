package core;

import java.util.ArrayList;

import core.manager.ActiveClient;
import core.manager.AppThreadManager;
import core.virtualui.VirtualUIBasicElement;
import core.virtualui.VirtualUIFrame;

public class Controller {

	private Controller m_oParentController;
	private boolean m_bIsWait;
	private boolean m_bIsClosed;
	
//	LinkedHashSet<VirtualUIBasicElement> m_oHeadElementsList;
	VirtualUIFrame m_oRootElement;
	
	public Controller(Controller oParentController){
		m_bIsWait = false;
		m_bIsClosed = false;
		
		m_oParentController = oParentController;

//		m_oHeadElementsList = new LinkedHashSet<VirtualUIBasicElement>();
		m_oRootElement = new VirtualUIFrame();
		m_oRootElement.setExist(true);
		m_oRootElement.setController(this);
		m_oRootElement.setIsRootElement(true);
		m_oRootElement.setWidth(1280);
		m_oRootElement.setHeight(800);
		
		if (oParentController != null)
			oParentController.m_oRootElement.attachChild(m_oRootElement);
	}
	
	public void beforeEvent() {
		
	}
	
	public boolean interceptEvent() {
		return false;
	}
	
	public void afterEvent() {
		
	}
	
	public void setParentController(Controller oParentController) {
		m_oParentController = oParentController;
	}
	
	public void attachChild(VirtualUIBasicElement oElement){	
		// Create the relationship between controller and elements
//		synchronized(m_oRootElement) {
			m_oRootElement.attachChild(oElement);
			oElement.setController(this);
//			m_oHeadElementsList.add(oElement);
//		}
    }
	
	private void setWait(boolean bIsWait){
		m_bIsWait = bIsWait;
	}
	
	private boolean isWait(){
		return m_bIsWait;
	}
	
	public void show2(){
		if (m_oParentController != null)
			m_oParentController.show2();
		
		// Show the element
		m_oRootElement.show();
	}
	
	public void showAndWait(){
		ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient();
		m_oParentController = oActiveClient.g_oControllerManager.getThreadActiveController();	//	##################### should not here
		
		// Show the element
		this.show2();
		
		// Send the packet
		System.out.println("sendPacket showAndWait");
		synchronized(oActiveClient.g_oResponsePacketManager) {
			oActiveClient.g_oUIManager.applyAllEdit();
			oActiveClient.g_oConnectionManager.sendPacket(oActiveClient.g_oResponsePacketManager.getResponsePacket());
			oActiveClient.g_oResponsePacketManager.clear();
		}

		// No parent object
		if(m_oParentController == null)
			return;
		
		// Block here
		try {
			synchronized(m_oParentController){
				m_oParentController.setWait(true);
				m_oParentController.wait();
				m_oParentController.setWait(false);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//	Stop processing immediately if controller is stopped
		if (m_bIsClosed) {
			Thread.currentThread().interrupt();
		}
	}
	
	public void finishShow(){
		if (m_bIsClosed)
			return;
		
		// Loop all child elements
//		synchronized(m_oHeadElementsList) {
			// Remove UI
			m_oRootElement.removeMyself();
			
			// Remove the relationship between controller and the elements
//			m_oHeadElementsList.clear();
//		}
		
		// Parent form exist and waiting
		if(m_oParentController != null && m_oParentController.isWait()){
			ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient();
			// Add the thread ID to not send response in UI Manager
			oActiveClient.g_oUIManager.addCurrentThreadToIgnoreSendList();
			
			// Wake up parent object
			synchronized(m_oParentController){
				m_oParentController.notify();
			}
		}
	}
	
	public void forceClose() {
		m_bIsClosed = true;
		
		//	Wake up current controller (if waiting)
		synchronized(this){
			notify();
			
			//	And then controller thread will be killed after wake up
		}
	}
	
	public void top() {	//	############### replace element top
		m_oRootElement.top();
	}
	
	public void setCoverTop(int iTop) {
		m_oRootElement.setTop(iTop);
	}
	
	public void setCoverLeft(int iLeft) {
		m_oRootElement.setLeft(iLeft);
	}
	
	public void setCoverHeight(int iHeight) {
		m_oRootElement.setHeight(iHeight);
	}
	
	public void setCoverWidth(int iWidth) {
		m_oRootElement.setWidth(iWidth);
	}
	
	// For FormMain to override to process relogin
	public void processClientReconnectRelogin(){
		
	}
	
	//Sunny
	// For compatible
	public void addTimerThread(int iClientSockId, Object oThreadObject, String sMethodName, Object[] oParameters){
		ArrayList<Integer> oThreadIds = new ArrayList<>();
		AppThreadManager oAppThreadManager = new AppThreadManager();
		oThreadIds.add(oAppThreadManager.addThread(oThreadObject, sMethodName, oParameters));
		oAppThreadManager.runThread(oThreadIds);
		oAppThreadManager.waitForThread(oThreadIds);
	}
		
	public void closeShowWithoutRemoveUI() {
		
	}
	
	public void processKillRequest() {
		
	}
	
	public void removeUI() {
		
	}
	
	public void finishUI(boolean bSendOnly) {
		
	}
}
