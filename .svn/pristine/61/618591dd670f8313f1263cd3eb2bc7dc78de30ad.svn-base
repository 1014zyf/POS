package virtualui;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import core.Controller;

import app.AppGlobal;
import app.AppThreadManager;
import app.ClsGlobalElement;

/** interface for the listeners/observers callback method */
interface VirtualUIListener {
	boolean clicked(int iId, String sNote);
	boolean longClicked(int iId, String sNote);
	boolean swipeTop(int iId, String sNote);
	boolean swipeBottom(int iId, String sNote);
	boolean swipeRight(int iId, String sNote);
	boolean swipeLeft(int iId, String sNote);
	boolean timer(int iClientSockId, int iId, String sNote);
	boolean valueChanged(int iId, String sNote);
	boolean forward(int iId, String sNote, String sStatus);
	boolean keyboard(int iId, String sNote);
}

//public class VirtualUIForm extends VirtualUIBasicElement {
public class VirtualUIForm extends Controller {
	
	private VirtualUITerm m_oParentTerm;
	private boolean m_bShow;
	
	// Break listen flag
	private boolean m_bBreakListen;
	
	// List of child basic elements
	private ArrayList<VirtualUIFrame> m_oChildFrameList;
	
	// Timer client socket ID list
	private HashMap<Integer, Integer> m_oTimerClientSocketIDList;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<VirtualUIListener> listeners;
	
	/** list of listeners for remove */
	private ArrayList<VirtualUIListener> listenersForRemove;
	
	private boolean m_bWriteSendPacketLog = false;
	
	/** called internally when you need to tell the observer stuff changed */
	private void fireClicked(int iId, String sNote) {
		for (VirtualUIListener listener : listeners) {
			// Target frame
			if(listener.clicked(iId, sNote))
				break;
		}
		
		for (VirtualUIListener listener : listenersForRemove) {
			this.removeListener(listener);
		}
		
		listenersForRemove.clear();
	}
	
	private void fireLongClicked(int iId, String sNote) {
		for (VirtualUIListener listener : listeners) {
			// Target frame
			if(listener.longClicked(iId, sNote))
				break;
		}
		
		for (VirtualUIListener listener : listenersForRemove) {
			this.removeListener(listener);
		}
		
		listenersForRemove.clear();
	}
	
	private void fireSwipeTop(int iId, String sNote) {
		for (VirtualUIListener listener : listeners) {
			// Target frame
			if(listener.swipeTop(iId, sNote))
				break;
		}
		
		for (VirtualUIListener listener : listenersForRemove) {
			this.removeListener(listener);
		}
		
		listenersForRemove.clear();
	}
	
	private void fireSwipeBottom(int iId, String sNote) {
		for (VirtualUIListener listener : listeners) {
			// Target frame
			if(listener.swipeBottom(iId, sNote))
				break;
		}
		
		for (VirtualUIListener listener : listenersForRemove) {
			this.removeListener(listener);
		}
		
		listenersForRemove.clear();
	}
	
	private void fireSwipeRight(int iId, String sNote) {
		for (VirtualUIListener listener : listeners) {
			// Target frame
			if(listener.swipeRight(iId, sNote))
				break;
		}
		
		for (VirtualUIListener listener : listenersForRemove) {
			this.removeListener(listener);
		}
		
		listenersForRemove.clear();
	}
	
	private void fireSwipeLeft(int iId, String sNote) {
		for (VirtualUIListener listener : listeners) {
			// Target frame
			if(listener.swipeLeft(iId, sNote))
				break;
		}
		
		for (VirtualUIListener listener : listenersForRemove) {
			this.removeListener(listener);
		}
		
		listenersForRemove.clear();
	}
	
	private void fireTimer(int iClientSockId, String sId, String sNote) {
		boolean bTimerLaunch = false;
		
		String args[] = sId.split("_");
		int iId = Integer.valueOf(args[args.length-1]);
		
		if(AppGlobal.g_oTerm.get().performGlobalTimerRunnable(iClientSockId, iId)) {
			// Handle by each system element function
			bTimerLaunch = true;
		} else {
			for (VirtualUIListener listener : listeners) {
				// Target frame
				if(listener.timer(iClientSockId, sId.hashCode(), sNote)){
					bTimerLaunch = true;
					break;
				}
			}
		}
		
		if(!bTimerLaunch)
			// No timer matched, close the socket
			AppGlobal.g_oTCP.get().closeClient(iClientSockId);
	}
	
	private void fireValueChanged(int iId, String sNote) {
		for (VirtualUIListener listener : listeners) {
			// Target frame
			if(listener.valueChanged(iId, sNote))
				break;
		}
		
		for (VirtualUIListener listener : listenersForRemove) {
			this.removeListener(listener);
		}
		
		listenersForRemove.clear();
	}
	
	private void fireForward(int iId, String sNote, String sStatus) {
		for (VirtualUIListener listener : listeners) {
			// Target frame
			if(listener.forward(iId, sNote, sStatus))
				break;
		}
		
		for (VirtualUIListener listener : listenersForRemove) {
			this.removeListener(listener);
		}
		
		listenersForRemove.clear();
	}
	
	private void fireKeyboard(int iId, String sNote) {
		for (VirtualUIListener listener : listeners) {
			// Target frame
			if(listener.keyboard(iId, sNote))
				break;
		}
		
		for (VirtualUIListener listener : listenersForRemove) {
			this.removeListener(listener);
		}
		
		listenersForRemove.clear();
	}
	
	/** add a new ModelListener observer for this Model */
	public void addListener(VirtualUIListener listener) {
		listeners.add(listener);
	}
	
	/** add the listener to the remove list and remove later */
	public void addListenerForRemove(VirtualUIListener listener) {
		listenersForRemove.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(VirtualUIListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	// Constructor
	public VirtualUIForm(Controller oParentController){
		super(oParentController);
		
		listeners = new ArrayList<VirtualUIListener>();
		listenersForRemove = new ArrayList<VirtualUIListener>();
		m_oChildFrameList = new ArrayList<VirtualUIFrame>();
		m_bBreakListen = false;
		m_oTimerClientSocketIDList = new HashMap<Integer, Integer>();
	}
	
	public void show(){
		
		m_bShow = true;
		
		// Attach the form to the term
		AppGlobal.g_oTerm.get().attachForm(this);
		
		// Handle system elements
		for (Entry<Integer, ClsGlobalElement> entry:AppGlobal.g_oTerm.get().getGlobalElementList().entrySet()) {
			ClsGlobalElement oElement = entry.getValue();
			oElement.getElement().show();
		}
		
		for(VirtualUIFrame oFrame : m_oChildFrameList){
			oFrame.show();
		}
		
		// Listen to server
		finishUI(false);
		
		// Close the UI
		removeUI();
		
		// Detach the form from the term
		AppGlobal.g_oTerm.get().detachForm(this);
	}
	
	public void showWithoutRemoveUI(){
		m_bShow = true;
		
		// Attach the form to the term
		AppGlobal.g_oTerm.get().attachForm(this);
		
		for(VirtualUIFrame oFrame : m_oChildFrameList){
			oFrame.show();
		}
		
		// Listen to server
		finishUI(false);
		
		// Close the UI
		//removeUI();
		
		// Detach the form from the term
		AppGlobal.g_oTerm.get().detachForm(this);
	}
	
	public void setShowForVirtualForm() {
		m_bShow = true;
	}
	
	public void closeShowWithoutRemoveUI(){
		// Close the UI
		removeUI();
	}
	
	/** method that results in a change in the model */
	public void finishUI(boolean bSendOnly) {
		String sEvent = "";
		String sNote = "";
		String sStatus = "";
		int iPrevClientSockId = 0;
		int iClientSockId = 0;
		boolean bTimerRequest = false;
		boolean bHaveView = false;
		boolean bHaveSystem = false;
		
		if(!bSendOnly)
			m_bBreakListen = false;
		
		while(!m_bBreakListen){
			if(bTimerRequest == false){
				
				if(m_bWriteSendPacketLog){
					AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), ">>> Before prepare json");
				}
				
				// Send the packet to client
				StringBuilder sViews = new StringBuilder();
//System.out.println("Before Send Time: " + new DateTime().toString());
				sViews.append("{");
				
				// View
				bHaveView = false;
				for(String sView:AppGlobal.g_oTerm.get().getPacket()){
					if(bHaveView)
						sViews.append(",");
					else
						sViews.append("\"" + HeroActionProtocol.View.KEY + "\":[");
					sViews.append(sView);
					bHaveView = true;
				}
				if(bHaveView)
					sViews.append("]");
				
				// System
				if (!AppGlobal.g_oTerm.get().getSystemPacket().isEmpty()) {
					sViews.append(",");
					sViews.append("\"" + HeroActionProtocol.System.KEY + "\":{");
					
					bHaveSystem = false;
					LinkedHashMap<String, JSONObject> systemPacket = AppGlobal.g_oTerm.get().getSystemPacket();
					for (String type : systemPacket.keySet()) {
						JSONObject params = systemPacket.get(type);
						if (bHaveSystem)
							sViews.append(",");
						sViews.append("\"" + type + "\":");
						sViews.append(params.toString());
						bHaveSystem = true;
					}
					sViews.append("}");
				}
				
				sViews.append("}");
				
				// Get the last incoming socket
				iPrevClientSockId = AppGlobal.g_oTerm.get().getClientSocketId();
				
				if(m_bWriteSendPacketLog){
					AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), ">>> Before write to client");
				}
				
//d("Thread (" + Thread.currentThread().getId() + ") Send Packet in socket (" + iPrevClientSockId + ") >>>>>>>>>> " + sViews.toString());
//d("Thread (" + Thread.currentThread().getId() + ") total elements: " + AppGlobal.g_oTerm.get().countUIElement());
				if(AppGlobal.g_oTCP.get().writePacket(iPrevClientSockId, sViews.toString()) == false){
					// Fail to write to client
					// Write error log
					AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Fail to send packet <" + sViews.toString() + ">");
				}
				
				if(m_bWriteSendPacketLog){
					AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), ">>> After write to client");
					m_bWriteSendPacketLog = false;
				}
				
//System.out.println("After Send Time: " + new DateTime().toString());
				// Remove timer if this is a timer response
				removeSingleTimer(iPrevClientSockId);
				
				// Close the client connection and wait for another connection
				AppGlobal.g_oTerm.get().closeClientSocket(iPrevClientSockId);
				AppGlobal.g_oTerm.get().clearPacket();
				
				if(bSendOnly){
					// If only send without listen, return here
					return;
				}
				
			}else{
				// Previous request is timer request
				bTimerRequest = false;
			}
			
			while(true)
			{
				int n = 0;
				try {
					n = AppGlobal.g_oSelectorForTCP.get().select(500);
				} catch (IOException e) {
					AppGlobal.stack2Log(e);
					continue;
				}
				
				// Interval for checking of some business logic (e.g. be killed by other station during daily start/close/reload setting)
				intermediateBusinessChecking();
				
				if(n == 0)
				{
					continue;
				}
				java.util.Iterator<SelectionKey> iterator = AppGlobal.g_oSelectorForTCP.get().selectedKeys().iterator();
				while(iterator.hasNext()) 
				{
					SelectionKey oIncomingSelectionkey = (SelectionKey)iterator.next();
					
					iterator.remove();
					
					// Client connect
					if(oIncomingSelectionkey.isAcceptable() && oIncomingSelectionkey == AppGlobal.g_oSelectorKeyForTCP.get())
					{
						iClientSockId = AppGlobal.g_oTCP.get().listen();
						if(iClientSockId > 0){
							/*
							if (AppGlobal.g_bConnectSuccess == false) {
								// Clear the packet list (May be updated by other thread)
								AppGlobal.g_oTerm.get().clearPacket();
								
								// Set the last client socket ID
								AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
								
								AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", "", "Fail to connect to service master");
								continue;
							}
							*/
							
							if(AppGlobal.g_bWriteClientConnectionLog){
								AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "<<< Before receive from client");
							}
							
//d("Thread (" + Thread.currentThread().getId() + ") Port (" + AppGlobal.g_oTCP.get().getServerPort() + ") Receive from client in socket (" + iClientSockId + ") <<<<<<<<<< " + AppGlobal.g_oTCP.get().getPacket());
//System.out.println("Receive Event Time: " + new DateTime().toString());				
							JSONObject recvJSONObj = null;
							JSONObject tempJSONObj = null;
							JSONArray tempJSONArray = null;
							String sFormName;
							int iId;
							String sValue;
							try {
								if(AppGlobal.g_oTCP.get().getPacket().length() == 0)
									continue;
								
								if(AppGlobal.g_bWriteClientConnectionLog){
									AppGlobal.writeDebugLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "<<< After receive from client");
									m_bWriteSendPacketLog = true;
								}
								
								recvJSONObj = new JSONObject(AppGlobal.g_oTCP.get().getPacket());
								
								String sId = recvJSONObj.getString("Id");
								// Handle re-connect
								if(sId.equals("_launch")){
									// Clear the packet list (May be updated by other thread)
									AppGlobal.g_oTerm.get().clearPacket();
									
									// Set the last client socket ID
									AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
									
									// Re-build the system element
									AppGlobal.g_oTerm.get().rebuildSystemElement();
									
									// Refresh the screen
									AppGlobal.g_oTerm.get().redrawScreen();
									AppGlobal.g_oTerm.get().processClientReconnectRelogin();
									
									continue;
								}
								
								String args[] = sId.split("_");
								sFormName = args[0];
								iId = Integer.valueOf(args[args.length-1]);
								sEvent = recvJSONObj.getString("Event");
								if(recvJSONObj.has("Note"))
									sNote = recvJSONObj.getString("Note");
								if(recvJSONObj.has("Status"))
									sStatus = recvJSONObj.getString("Status");
								else
									sStatus = "";
								tempJSONObj = recvJSONObj.getJSONObject("Submit");
								tempJSONArray = tempJSONObj.names();
								if(tempJSONArray != null){
									for (int i = 0; i < tempJSONArray.length(); ++i) {
										args = tempJSONArray.getString(i).split("_");
										
										int iElementId = Integer.valueOf(args[args.length-1]);
										sValue = tempJSONObj.getString(tempJSONArray.getString(i));
										
										// Assign value to the element
										this.setValue(iElementId, sValue);
										
										// Assign status to the element
										this.setStatus(iElementId, sStatus);
										
										// Assign value to the system element
										AppGlobal.g_oTerm.get().setGlobalElementValue(iElementId, sValue);
										
										// Assign status to the system element
										AppGlobal.g_oTerm.get().setGlobalElementStatus(iElementId, sStatus);
									}
								}
								
								// Check if the request is come from the correct form
								if(sEvent.equals("forward") == false && sEvent.equals("valueChanged") == false && sFormName.equals(this.getClass().getSimpleName()) == false && !AppGlobal.g_oTerm.get().isSystemElement(iId)){
									// Clear the packet list (May be updated by other thread)
									AppGlobal.g_oTerm.get().clearPacket();
									
									// Set the last client socket ID
									AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
									
									continue;
								}
								
								// notify observers
								if(sEvent.equals("click")){
									// Clear the packet list (May be updated by other thread)
									AppGlobal.g_oTerm.get().clearPacket();
									
									// Set the last client socket ID
									AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
									
									// Child item of the form has event
									if(!childItemClicked())
										continue;
									
									fireClicked(iId, sNote);
								}else
								if(sEvent.equals("longClick")){
									// Clear the packet list (May be updated by other thread)
									AppGlobal.g_oTerm.get().clearPacket();
									
									// Set the last client socket ID
									AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
									
									// Child item of the form has event
									if(!childItemClicked())
										continue;
									
									fireLongClicked(iId, sNote);
								}else
								if(sEvent.equals("swipeTop")){
									// Clear the packet list (May be updated by other thread)
									AppGlobal.g_oTerm.get().clearPacket();
									
									// Set the last client socket ID
									AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
									
									// Child item of the form has event
									if(!childItemClicked())
										continue;
									
									fireSwipeTop(iId, sNote);
								}else
								if(sEvent.equals("swipeBottom")){
									// Clear the packet list (May be updated by other thread)
									AppGlobal.g_oTerm.get().clearPacket();
									
									// Set the last client socket ID
									AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
									
									// Child item of the form has event
									if(!childItemClicked())
										continue;
									
									fireSwipeBottom(iId, sNote);
								}else
								if(sEvent.equals("swipeRight")){
									// Clear the packet list (May be updated by other thread)
									AppGlobal.g_oTerm.get().clearPacket();
									
									// Set the last client socket ID
									AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
									
									// Child item of the form has event
									if(!childItemClicked())
										continue;
									
									fireSwipeRight(iId, sNote);
								}else
								if(sEvent.equals("swipeLeft")){
									// Clear the packet list (May be updated by other thread)
									AppGlobal.g_oTerm.get().clearPacket();
									
									// Set the last client socket ID
									AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
									
									// Child item of the form has event
									if(!childItemClicked())
										continue;
									
									fireSwipeLeft(iId, sNote);
								}else
								if(sEvent.equals("forward")){
									// Clear the packet list (May be updated by other thread)
									AppGlobal.g_oTerm.get().clearPacket();
									
									// Set the last client socket ID
									AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
									
									fireForward(iId, sNote, sStatus);
								}else
								if(sEvent.equals("timer")){
									// Set the last client socket ID
									AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
									
									// Child item of the form has event
									if(!childItemTimer()){
										continue;
									}
									
									fireTimer(iClientSockId, sId, sNote);
									// Set timer request flag to prevent sending nothing after loop back to above
									// Response will be sent by the thread itself
									bTimerRequest = true;
									
									// Add the client socket to list
									addTimer(iClientSockId);
								}else
								if(sEvent.equals("valueChanged")){
									// Clear the packet list (May be updated by other thread)
									AppGlobal.g_oTerm.get().clearPacket();
									
									// Set the last client socket ID
									AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
									
									// Child item of the form has event
									if(!childItemClicked())
										continue;
									
									fireValueChanged(iId, sNote);
								}else
								if(sEvent.equals("keyboard")){
									// Clear the packet list (May be updated by other thread)
									AppGlobal.g_oTerm.get().clearPacket();
									
									// Set the last client socket ID
									AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
									
									// Child item of the form has event
									if(!childItemClicked())
										continue;
									
									fireKeyboard(iId, sNote);
								}
								
							} catch (JSONException e) {
								AppGlobal.stack2Log(e);
							} catch (Exception e1) {
								AppGlobal.stack2Log(e1);
							}
						}
						else
							m_bBreakListen = true;
					}
				}
				
				break;
			}
		}
	}
	
	public void removeUI(){
		while(m_oChildFrameList.size() > 0){
			VirtualUIFrame oFrame = m_oChildFrameList.remove(0);
			
			// Set the show flag to false
			oFrame.finishShow();
			// Remove UI from client
			oFrame.removeMyself();
		}
		
		m_oChildFrameList.clear();
	}
	
	public void finishShow(){
		super.finishShow();
		
		// Break the listen
		m_bBreakListen = true;
	}
	
	public void redrawChild(){
		// Re-send the UI to client
		for(VirtualUIFrame oFrame : m_oChildFrameList){
			oFrame.show();
		}
	}
	
	// For FormMain to override to process relogin
	public void processClientReconnectRelogin(){
		
	}
	
	public void attachChild(VirtualUIFrame oFrame){
		// Get ID for frame
		oFrame.setId(this.getAvailableUIId());
		
		m_oChildFrameList.add(oFrame);
		oFrame.setParentForm(this);
		oFrame.setParentTerm(m_oParentTerm);
		
		if (m_bShow){
			// Show the child
			oFrame.show();
		}
	}
	
	public void removeChild(int iId){
		for(VirtualUIFrame oFrame : m_oChildFrameList){
			if(oFrame.getId() == iId){
				// Add the frame to pending remove list because the listener list is using, cannot remove immediately
				listenersForRemove.add(oFrame);
				
				m_oChildFrameList.remove(oFrame);
				
				break;
			}
		}
	}
	
	public VirtualUITerm getParentTerm(){
		return m_oParentTerm;
	}
	
	public void setParentTerm(VirtualUITerm oTerm){
		m_oParentTerm = oTerm;
		
		// Also set all children's parent term
		for(VirtualUIFrame oFrame : m_oChildFrameList){
			oFrame.setParentTerm(oTerm);
		}
	}
	
	public void setValue(int iId, String sValue){
		// Search child frames
		for(VirtualUIFrame oFrame : m_oChildFrameList){
			oFrame.setValue(iId, sValue);
		}
	}
	
	public void setStatus(int iId, String sStatus){
		// Search child frames
		for(VirtualUIFrame oFrame : m_oChildFrameList){
			oFrame.setStatus(iId, sStatus);
		}
	}
	
	public boolean isShow(){
		return m_bShow;
	}
	
	public int getAvailableUIId(){
		// Increment the available ID
		return AppGlobal.g_oUIElementGenerator.incrementAndGet();
	}
	
	public void addTimerThread(int iClientSockId, Object oThreadObject, String sMethodName, Object[] oParameters){
		AppThreadManager oAppThreadManager = new AppThreadManager();
		oAppThreadManager.addThread(iClientSockId, oThreadObject, sMethodName, oParameters);
		oAppThreadManager.runThread();
	}
	
	private void addTimer(int iClientSockId){
		m_oTimerClientSocketIDList.put(iClientSockId, iClientSockId);
	}
	
	private void removeSingleTimer(int iClientSockId){
		if(m_oTimerClientSocketIDList.containsKey(iClientSockId) == false)
			return;
		
		m_oTimerClientSocketIDList.remove(iClientSockId);
	}
	
	private void stopAllTimer(){
		// Close all other client socket
		java.util.Iterator<Entry<Integer, Integer>> it = m_oTimerClientSocketIDList.entrySet().iterator();
		while (it.hasNext())
			AppGlobal.g_oTCP.get().closeClient(it.next().getValue());
		m_oTimerClientSocketIDList.clear();
	}
	
	// Function to be override to handle click on this form
	public boolean childItemClicked() {
		if(AppGlobal.getKilledReason().length() > 0){
			AppGlobal.g_oTerm.get().processKillRequest();
			this.finishShow();
		}
		return true;
	}
	
	// Function to be override to handle timer on this form
	public boolean childItemTimer(){
		if(AppGlobal.getKilledReason().length() > 0){
			AppGlobal.g_oTerm.get().processKillRequest();
			this.finishShow();
		}
		return true;
	}
	
	// Function to be override to handle kill request from other station
	public void processKillRequest() {
	}
	
	// Function to handle some business checking during idle time or click
	public void intermediateBusinessChecking() {
		
		String sReason = AppGlobal.getKilledReason();
		if(sReason.length() > 0){
			// The station is going to be killed
			// nothing to do
			return;
		}
		
		if(AppGlobal.g_oFuncOutlet == null || AppGlobal.g_oFuncOutlet.get() == null)
			// No outlet is set
			return;
		
		// Check if auto-daily operation is performing or not
		sReason = AppGlobal.readExternalDailyOperationFile(AppGlobal.g_oFuncOutlet.get().getOutletId());
		if(sReason.length() > 0){
			// Kill myself
			AppGlobal.startKillSingleStation(AppGlobal.g_oFuncStation.get().getStationId(), sReason);
		}
	}
	
	private void d(String msg){
		System.out.println(msg);
	}
	
	public void moveChildToEnd(int iId) {
		int iPosition = -1;
		for(int i = 0; i < m_oChildFrameList.size(); i++) {
			VirtualUIBasicElement oBasicElement = m_oChildFrameList.get(i);
			if(iId == oBasicElement.getId()) {
				iPosition = i;
				break;
			}
		}
		
		if(iPosition > -1) {
			VirtualUIFrame oFrame = m_oChildFrameList.remove(iPosition);
			m_oChildFrameList.add(oFrame);
		}
	}
	
	// Return child frame arraylist
	public ArrayList<VirtualUIFrame> getChildFrames() {
		return m_oChildFrameList;
	}
	
	public int countUIElement() {
		int iCount = 0;
		for(VirtualUIFrame oFrame : m_oChildFrameList){
			iCount += oFrame.countUIElement();
		}
		return iCount;
	}
}
