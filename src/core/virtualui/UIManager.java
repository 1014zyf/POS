package core.virtualui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import core.Controller;
import core.Core;
import core.controller.RootController;
import core.externallib.SparseArray;
import core.manager.ActiveClient;
import core.manager.LoggingManager;
import core.manager.ResponsePacketManager;

public class UIManager {

	private Integer m_iAvaliableElementIdForNewElement;
	
	// Build the relationship between controller and elements
	private LinkedList<Controller> m_oControllerList;
	
	private SparseArray<VirtualUIBasicElement> m_oElementList;
	private ArrayList<VirtualUIBasicElement> m_oEditedElementSet;
//	private ArrayList<VirtualUIBasicElement> m_oEditedElementNoParentSet;	//	To handle new element set edited too early at creating time
	private HashMap<Long, Long> m_oIgnoreSendThreadIdList;
	
	private VirtualSystemAction m_oSystemAction;
	
	public UIManager() {
		m_iAvaliableElementIdForNewElement = 0;
		
		m_oControllerList = new LinkedList<Controller>();
		
		m_oElementList = new SparseArray<VirtualUIBasicElement>();
		m_oEditedElementSet = new ArrayList<VirtualUIBasicElement>();
//		m_oEditedElementNoParentSet = new ArrayList<VirtualUIBasicElement>();
		m_oIgnoreSendThreadIdList = new HashMap<Long, Long>();
		
		m_oSystemAction = new VirtualSystemAction();
	}
	
	public void handleEvent(JSONObject oEventJsonObject){
		// Process the packet JSON
		int iId = 0;
		int iVersion = 0;
		String sEvent = "";
//		String sNote = "";
		String sStatus = "";
		
		try {
			String sId = oEventJsonObject.getString("Id");
			try {
				iId = Integer.parseInt(sId);
			}
			catch (NumberFormatException e) {
				if (sId.contentEquals("_launch")) {
					iId = RootController.LAUNCH_APP_ELEMENT_RESERVED_ID;
				}
				else if (sId.contentEquals("_focus")) {
					iId = -2;
				}
				else {
					iId = 0;
				}
			}
			//iVersion = oEventJsonObject.getInt("Version");
			
			// Handle launch request from client

			// Locate the element that create the event
			VirtualUIBasicElement oElement = m_oElementList.get(iId);
			
			//	Ignore event if version is not updated
			if(oElement != null && oElement.getParentForm() != null/* && oElement.getVersion() <= iVersion*/){
				
				// Check if the element is in current form or not
				/*
				boolean bIsCurrentFormElement = false;
				if(m_oControllerElementsPairList.size() > 0){
					// Retrieve the primary parent element
					int iPrimaryParentElementId = oElement.getPrimaryParent();
					
					// Get the last insert form == current form
					HashMap<Integer,VirtualUIBasicElement> oElementListHashMap = new LinkedList<HashMap<Integer,VirtualUIBasicElement>>(m_oControllerElementsPairList.values()).getLast();
					if(oElementListHashMap.containsKey(iPrimaryParentElementId))
						bIsCurrentFormElement = true;
				}
				*/
//					boolean bIsCurrentFormElement = true;
				
//					if(bIsCurrentFormElement){
					sEvent = oEventJsonObject.optString("Event");
//					sNote = oEventJsonObject.optString("Note");
					sStatus = oEventJsonObject.optString("Status");
					
					// Get submit value and set value to target element
					JSONObject oSubmitJsonObject = oEventJsonObject.optJSONObject("Submit");
					if (oSubmitJsonObject != null) {
						Iterator<?> oSubmitValueIterator = oSubmitJsonObject.keys();
						String sElementId = "";
						String sValue = "";
						VirtualUIBasicElement oSubmitElement = null;
						while (oSubmitValueIterator.hasNext()) {
							sElementId = (String)oSubmitValueIterator.next();
							sValue = oSubmitJsonObject.optString(sElementId);
							if (sValue != null) {
								oSubmitElement = m_oElementList.get(Integer.parseInt(sElementId));
								if (oSubmitElement != null)
									oSubmitElement.setValue(sValue);
							}
						}
					}
					
					ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient();
					oActiveClient.g_oControllerManager.setThreadActiveController(oElement.getParentForm());		//	null check at the beginning
					
					oActiveClient.g_oControllerManager.getThreadActiveController().beforeEvent();
					
					if (!oActiveClient.g_oControllerManager.getThreadActiveController().interceptEvent()) {
						//	Handle click event --> logic part
						String eventType = "";
						if (sEvent.equals(VirtualUIEvent.EVENT_CLICK))
							eventType = VirtualUIEvent.EVENT_CLICK;
						else if (sEvent.equals(VirtualUIEvent.EVENT_LONG_CLICK))
							eventType = VirtualUIEvent.EVENT_LONG_CLICK;
						else if(sEvent.equals(VirtualUIEvent.EVENT_SWIPE_RIGHT))
							eventType = VirtualUIEvent.EVENT_SWIPE_RIGHT;
						else if(sEvent.equals(VirtualUIEvent.EVENT_SWIPE_LEFT))
							eventType = VirtualUIEvent.EVENT_SWIPE_LEFT;
						else if(sEvent.equals(VirtualUIEvent.EVENT_SWIPE_TOP))
							eventType = VirtualUIEvent.EVENT_SWIPE_TOP;
						else if(sEvent.equals(VirtualUIEvent.EVENT_SWIPE_BOTTOM))
							eventType = VirtualUIEvent.EVENT_SWIPE_BOTTOM;
						else if(sEvent.equals(VirtualUIEvent.EVENT_VALUE_CHANGED))
							eventType = VirtualUIEvent.EVENT_VALUE_CHANGED;
						else if(sEvent.equals(VirtualUIEvent.EVENT_TIMER))
							eventType = VirtualUIEvent.EVENT_TIMER;
						else if(sEvent.equals(VirtualUIEvent.EVENT_IDLE))
							eventType = VirtualUIEvent.EVENT_IDLE;
						else if(sEvent.equals(VirtualUIEvent.EVENT_FORWARD))
							eventType = VirtualUIEvent.EVENT_FORWARD;
						
						if (!eventType.isEmpty())
							oElement.doEvent(iId, eventType);
					}
					
					oActiveClient.g_oControllerManager.getThreadActiveController().afterEvent();
					
					oActiveClient.g_oControllerManager.removeThreadActiveController();
//					}
			}
			
		} catch (JSONException e) {
			LoggingManager.stack2Log(e);
			return;
		}
	}
	
//	private void resequenceEditedElementList(HashMap<Integer, VirtualUIBasicElement>oEditedElementList, ArrayList<VirtualUIBasicElement>oOrderedEditedElementList, VirtualUIBasicElement oCurrentElement){
//		if(oCurrentElement.getParent() != null){
//			VirtualUIBasicElement oParentElement = oCurrentElement.getParent();
//			if(oEditedElementList.containsKey(oParentElement.getId())){
//				this.resequenceEditedElementList(oEditedElementList, oOrderedEditedElementList, oParentElement);
//			}
//		}
//		
//		// Add current element to the ordered edited element list
//		oOrderedEditedElementList.add(oEditedElementList.remove(oCurrentElement.getId()));
//	}
	
	public VirtualUIBasicElement getElement(int iElementId) {
		return m_oElementList.get(iElementId);
	}
	
	private ArrayList<VirtualUIBasicElement> clearAndGetOrderedEditedElement() {
		ArrayList<VirtualUIBasicElement> oResultElementList = new ArrayList<VirtualUIBasicElement>();
		ArrayList<VirtualUIBasicElement> oLastResultElementList = new ArrayList<VirtualUIBasicElement>();
		
		//	Re-order element to let parent json init first
		synchronized(m_oEditedElementSet) {
			ArrayList<VirtualUIBasicElement> oNotOrderedEditedElementList = new ArrayList<VirtualUIBasicElement>();
			
			ArrayList<VirtualUIBasicElement> oOrderingElementList = new ArrayList<VirtualUIBasicElement>();
			oOrderingElementList.addAll(m_oEditedElementSet);
			m_oEditedElementSet.clear();
			
			while (true) {
				for (VirtualUIBasicElement oElement : oOrderingElementList) {
					if (oResultElementList.contains(oElement))
						continue;
					if (oElement.isRootElement()) {
						oResultElementList.add(oElement);
						continue;
					}
					if (m_oElementList.get(oElement.getId()) == null) {
						System.out.println("oElement ordering in UIManager bug bug alert 1");
						continue;
					}
					if (oLastResultElementList.contains(oElement.getParent()) || !oElement.getParent().isFullEdited())
						oResultElementList.add(oElement);
					else
						oNotOrderedEditedElementList.add(oElement);
				}
				
				if (oOrderingElementList.size() == oNotOrderedEditedElementList.size())
					break;
				oOrderingElementList = oNotOrderedEditedElementList;
				oNotOrderedEditedElementList = new ArrayList<VirtualUIBasicElement>();
				oLastResultElementList.clear();
				oLastResultElementList.addAll(oResultElementList);
			}
			
			if (!oNotOrderedEditedElementList.isEmpty())
				System.out.println("oElement ordering in UIManager bug bug alert 2");
		}
		
		return oResultElementList;
	}
	
	public void applyAllEdit(){
		ArrayList<VirtualUIBasicElement> oAllEditedElementList = new ArrayList<VirtualUIBasicElement>();
		JSONObject oSystemActionJsonObject = new JSONObject();

		if (m_oSystemAction.isEdited()) {
			synchronized(m_oSystemAction) {
				oSystemActionJsonObject = m_oSystemAction.buildJsonObject();
				m_oSystemAction.applyEdit();
			}
		}
		
		// Get the edit element list
		oAllEditedElementList.addAll(clearAndGetOrderedEditedElement());
		
		JSONObject oElementJsonObject = null;
		ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient();
		ResponsePacketManager oResponsePacketManager = oActiveClient.g_oResponsePacketManager;
		
		synchronized(oResponsePacketManager) {
			for(VirtualUIBasicElement oElement:oAllEditedElementList){
				if(!oElement.isShow())
					continue;
				if (!oElement.getExist())
					continue;
				
				oElementJsonObject = oElement.buildElementJsonObject();
				if (oElementJsonObject.length() > 1)	//	Ignore json only got {"i": 2}
					oResponsePacketManager.addViewJsonObject(oElementJsonObject);
				oElement.applyEdit();
			}
			
			oResponsePacketManager.setSystemJsonObject(oSystemActionJsonObject);
		}
	}
	
	public void addController(Controller oController) {
		synchronized(m_oControllerList) {
			m_oControllerList.add(oController);
		}
	}
	
	public void removeController(Controller oController) {
		synchronized(m_oControllerList) {
			m_oControllerList.removeLastOccurrence(oController);
		}
	}
	// Add element to UI manager
	public void addElement(VirtualUIBasicElement oElement){
		if (!oElement.isRootElement() && oElement.getParent() == null)
			return;
		if (oElement.getParent() != null && m_oElementList.get(oElement.getParent().getId()) == null)
			return;
		
		synchronized(m_oElementList) {
			m_oElementList.put(oElement.getId(), oElement);
			for (VirtualUIBasicElement oChildElement : oElement.getAllChildSet())
				m_oElementList.put(oChildElement.getId(), oChildElement);
		}
	}
	
	// Remove element from UI manager
	public void removeElement(VirtualUIBasicElement oElement){
		synchronized(m_oElementList) {
			m_oElementList.remove(oElement.getId());
			for (VirtualUIBasicElement oChildElement : oElement.getAllChildSet())
				m_oElementList.remove(oChildElement.getId());
		}
		
		//	setElementIgnoreEdited(oElement);	//	no need ignore, still need to proceed remove json
		setElementIgnoreEdited(oElement.getAllChildSet());
	}
	public void removeElement(Collection<VirtualUIBasicElement> oElementList){
		for (VirtualUIBasicElement oElement : oElementList)
			removeElement(oElement);
	}
	
	// Set element to be edited
	public void setElementEdited(VirtualUIBasicElement oElement){
		if(oElement == null)
			return;
		
		if (m_oElementList.get(oElement.getId()) == null)
			return;

		synchronized(m_oEditedElementSet) {
			m_oEditedElementSet.add(oElement);
		}
	}
	public void setElementEdited(Collection<VirtualUIBasicElement> oElementList){
		if(oElementList == null || oElementList.isEmpty())
			return;
		
		for (VirtualUIBasicElement oElement : oElementList)
			setElementEdited(oElement);
	}
	
	//	Set element to be ignored from edited
	public void setElementIgnoreEdited(VirtualUIBasicElement oElement){
		if(oElement == null)
			return;
		
		synchronized(m_oEditedElementSet) {
			m_oEditedElementSet.remove(oElement);
		}
	}
	public void setElementIgnoreEdited(Collection<VirtualUIBasicElement> oElementList){
		if(oElementList == null || oElementList.isEmpty())
			return;
		
		synchronized(m_oEditedElementSet) {
			m_oEditedElementSet.removeAll(oElementList);
		}
	}
	
	// Get a unique element ID
	public synchronized Integer getElementId(){
		m_iAvaliableElementIdForNewElement++;
		return m_iAvaliableElementIdForNewElement;
	}
	
	public boolean isCurrentThreadInIgnoreSendList() {
		return m_oIgnoreSendThreadIdList.containsKey(Thread.currentThread().getId());
	}
	
	public boolean isIgnoreSendListEmpty() {
		return m_oIgnoreSendThreadIdList.isEmpty();
	}
	
	public void waitUntilIgnoreSendListEmpty() throws InterruptedException {
		if (isIgnoreSendListEmpty()) 
			return;
		
		synchronized (m_oIgnoreSendThreadIdList) {
			m_oIgnoreSendThreadIdList.wait();
		}
	}
	
	// Set current thread do not send UI update
	public void addCurrentThreadToIgnoreSendList(){
		synchronized (m_oIgnoreSendThreadIdList) {
			m_oIgnoreSendThreadIdList.put(Thread.currentThread().getId(), Thread.currentThread().getId());
		}
	}
	
	// Remove current thread from ignore send list
	public void removeCurrentThreadToIgnoreSendList(){
		synchronized (m_oIgnoreSendThreadIdList) {
			m_oIgnoreSendThreadIdList.remove(Thread.currentThread().getId());
			
			//	Tell waitUntilIgnoreSendListEmpty to wake up
			if (isIgnoreSendListEmpty())
				m_oIgnoreSendThreadIdList.notifyAll();
		}
	}
	
//	public VirtualSystemAction getSystemAction() {
//		return m_oSystemAction;
//	}
	
	public void hideKeyboard() {
		m_oSystemAction.setHideKeyboard(true);
		m_oSystemAction.setShowKeyboard(false);
	}
	
	public void showKeyboard() {
		m_oSystemAction.setHideKeyboard(false);
		m_oSystemAction.setShowKeyboard(true);
	}
	
	public void logout() {
		m_oSystemAction.setLogout(true);
	}
	
	// Redraw all elements
	public void redrawScreen(){
		LinkedList<VirtualUIBasicElement> oElementList = new LinkedList<VirtualUIBasicElement>();
		
		//	Add all head element to list
		int size = m_oElementList.size();
		VirtualUIBasicElement oElement = null;
		for (int i = 0; i < size; i++) {
			oElement = m_oElementList.valueAt(i);
			if (oElement.getParent() == null)
				oElementList.add(oElement);
		}
		
		//	Add parent to edited and then child
		while (!oElementList.isEmpty()) {
			oElement = oElementList.poll();
			oElementList.addAll(oElement.getChilds());
			oElement.setEdited(true);
		}
	}
}
