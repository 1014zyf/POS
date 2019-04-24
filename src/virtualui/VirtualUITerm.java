package virtualui;

import java.io.IOException;
import java.net.Inet4Address;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import app.AppGlobal;
import app.ClsGlobalElement;
import app.ClsGlobalUIRunnable;

public class VirtualUITerm  {
	// Message
	// For View
	private HashMap<Long, ArrayList<String>> m_aPacket;
	// For System
	private HashMap<Long, LinkedHashMap<String, JSONObject>> m_aSystemPacket;
	private HashMap<Long, Integer> m_ClientSockIdList;
	private ArrayList<VirtualUIForm> m_FormList;
	private HashMap<Integer, ClsGlobalElement> m_oGlobalElementList;
	private String m_sClientImageURL;
	
	// UI Element ID
	private int m_iAvailableUIId;
	
	// Current language index
	private int m_iCurrentLanguageIndex;
	
	public VirtualUITerm(){
		m_aPacket = new HashMap<Long, ArrayList<String>>();
		m_aSystemPacket = new HashMap<Long, LinkedHashMap<String, JSONObject>>();
		m_ClientSockIdList = new HashMap<Long, Integer>();
		m_FormList = new ArrayList<VirtualUIForm>();
		m_oGlobalElementList = new HashMap<Integer, ClsGlobalElement>();
		m_sClientImageURL = "";
		m_iAvailableUIId = -1;
		m_iCurrentLanguageIndex = 0;
	}
	
	// First connection
	public boolean waitForClient(String sClientIPAddress, int iPortNo){
		// Init the socket
		String sErrorMsg = "";
		sErrorMsg = AppGlobal.g_oTCP.get().initServer(sClientIPAddress, iPortNo, false);
		if(sErrorMsg.isEmpty() == false){
			// Fail to init the port no.
			AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), sClientIPAddress, "", "Fail to init port: " + iPortNo + ", error : " + sErrorMsg);
			return false;
		}
		
		// Selector
		Selector oSelector = null;
		try{
			oSelector = SelectorProvider.provider().openSelector();
		} catch ( Exception e ) {
			// Internal error
			AppGlobal.stack2Log(e);
			return false;
		}
		
		// Register the server socket channel
		ServerSocketChannel oChannel = AppGlobal.g_oTCP.get().getSocketChannel();
		SelectionKey oClientIncomingRequestKey = null;
		try {
			oClientIncomingRequestKey = oChannel.register(oSelector, SelectionKey.OP_ACCEPT);
		} catch (ClosedChannelException e) {
			AppGlobal.stack2Log(e);
			return false;
		} catch (Exception e) {
			AppGlobal.stack2Log(e);
			return false;
		}
		
		// Initialize the selector for TCP connection
		AppGlobal.g_oSelectorForTCP.set(oSelector);
		AppGlobal.g_oSelectorKeyForTCP.set(oClientIncomingRequestKey);
		
		while(true)
		{
			int n = 0;
			try {
				n = AppGlobal.g_oSelectorForTCP.get().select(1000);
			} catch (IOException e) {
				AppGlobal.stack2Log(e);
				return false;
			}
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
					// Wait for the client connection
					int iClientSockId = AppGlobal.g_oTCP.get().listen();
					if(iClientSockId > 0){
						// Receive client connection
						
						// Set socket ID for main thread
						setClientSocketId(iClientSockId);
						
						return true;
					}else{
						// Error connection
					}
				}
			}
		}
	}
	
	public void appendPacket(JSONObject oPacket){
		long lCurrentThreadID = Thread.currentThread().getId();
		
		if(m_aPacket.containsKey(lCurrentThreadID)){
			m_aPacket.get(lCurrentThreadID).add(oPacket.toString());
		}
	}
	
	public ArrayList<String> getPacket(){
		long lCurrentThreadID = Thread.currentThread().getId();
		
		if(m_aPacket.containsKey(lCurrentThreadID)){
			return m_aPacket.get(lCurrentThreadID);
		}else
			return new ArrayList<String>();
	}
	
	public void clearPacket(){
		long lCurrentThreadID = Thread.currentThread().getId();
		
		if(m_aPacket.containsKey(lCurrentThreadID)){
			m_aPacket.get(lCurrentThreadID).clear();
		}
	}
	
	public void setSystemAction(String type, JSONObject params){
		long lCurrentThreadID = Thread.currentThread().getId();
		
		if(m_aSystemPacket.containsKey(lCurrentThreadID)){
			m_aSystemPacket.get(lCurrentThreadID).remove(type);	//	Remove first to preserve sequence
			m_aSystemPacket.get(lCurrentThreadID).put(type, params);
		}
	}
	
	public LinkedHashMap<String, JSONObject> getSystemPacket(){
		long lCurrentThreadID = Thread.currentThread().getId();
		
		if(m_aSystemPacket.containsKey(lCurrentThreadID)){
			return m_aSystemPacket.get(lCurrentThreadID);
		}else
			return new LinkedHashMap<String, JSONObject>();
	}
	
	public void clearSystemPacket(){
		long lCurrentThreadID = Thread.currentThread().getId();
		
		if(m_aSystemPacket.containsKey(lCurrentThreadID)){
			m_aSystemPacket.get(lCurrentThreadID).clear();
		}
	}
	
	public void attachForm(VirtualUIForm oForm){
		oForm.setParentTerm(this);
		
		m_FormList.add(oForm);
	}
	
	public void detachForm(VirtualUIForm oForm){
		m_FormList.remove(oForm);
	}
	
	public VirtualUIForm getLastShowForm() {
		if (m_FormList.size() > 0)
			return m_FormList.get(m_FormList.size() - 1);
		else
			return null;
	}
	
	public void addGlobalElement(VirtualUIBasicElement oVirtualUIBasicElement){
		VirtualUIForm oParentForm = new VirtualUIForm(null);
		oParentForm.setShowForVirtualForm();
		oVirtualUIBasicElement.setId(m_iAvailableUIId);
		oVirtualUIBasicElement.setParentForm(oParentForm);
		oVirtualUIBasicElement.setParentTerm(this);
		
		ClsGlobalElement oGlobalElement = new ClsGlobalElement(oVirtualUIBasicElement);
		m_oGlobalElementList.put(m_iAvailableUIId, oGlobalElement);
		
		m_iAvailableUIId--;
	}
	
	public HashMap<Integer, ClsGlobalElement> getGlobalElementList() {
		return m_oGlobalElementList;
	}
	
	public void registerGlobalElementRunnable(VirtualUIBasicElement oGlobalElement, ClsGlobalUIRunnable oRunnable) {
		for(Entry<Integer, ClsGlobalElement> entry: m_oGlobalElementList.entrySet()){
			ClsGlobalElement oExistingGlobalElement = entry.getValue();
			if (oExistingGlobalElement.getElement() == oGlobalElement) {
				oExistingGlobalElement.registerRunnable(oRunnable);
			}
		}
	}
	
	public void setGlobalElementValue(int iId, String sValue) {
		// Search child frames
		for(Entry<Integer, ClsGlobalElement> entry: m_oGlobalElementList.entrySet()){
			ClsGlobalElement oElement = entry.getValue();
			oElement.getElement().setValue(iId, sValue);
		}
	}
	
	public void setGlobalElementStatus(int iId, String sStatus) {
		// Search child frames
		for(Entry<Integer, ClsGlobalElement> entry: m_oGlobalElementList.entrySet()){
			ClsGlobalElement oElement = entry.getValue();
			oElement.getElement().setStatus(iId, sStatus);
		}
	}
	
	public boolean isSystemElement(int iId) {
		return m_oGlobalElementList.containsKey(iId);
	}
	
	public boolean performGlobalTimerRunnable(int iClientSockId, int iId) {
		for(Entry<Integer, ClsGlobalElement> entry: m_oGlobalElementList.entrySet()){
			ClsGlobalElement oElement = entry.getValue();
			if (oElement.getElement().getId() == iId) {
				oElement.performRunnable(iClientSockId);
				return true;
			}
		}
		
		// No system element handling
		return false;
	}
	
	public Integer getClientSocketId(){
		long lCurrentThreadID = Thread.currentThread().getId();
		
		if(m_ClientSockIdList.containsKey(lCurrentThreadID)){
			return m_ClientSockIdList.get(lCurrentThreadID);
		}else {
			return 0;
		}
	}
	
	public void setClientSocketId(int iClientSockId){
		long lCurrentThreadID = Thread.currentThread().getId();
		
		m_ClientSockIdList.put(lCurrentThreadID, iClientSockId);
		
		// Create packet list for the socket
		if(m_aPacket.containsKey(lCurrentThreadID) == false){
			m_aPacket.put(lCurrentThreadID, new ArrayList<String>());
		}
		if(m_aSystemPacket.containsKey(lCurrentThreadID) == false){
			m_aSystemPacket.put(lCurrentThreadID, new LinkedHashMap<String, JSONObject>());
		}
	}
	
	public void closeClientSocket(int iClientSockId){
		long lCurrentThreadID = Thread.currentThread().getId();
		
		if(m_ClientSockIdList.containsKey(lCurrentThreadID)){
			m_ClientSockIdList.remove(lCurrentThreadID);
		}
		if(m_aPacket.containsKey(lCurrentThreadID)){
			m_aPacket.remove(lCurrentThreadID);
		}
		if(m_aSystemPacket.containsKey(lCurrentThreadID)){
			m_aSystemPacket.remove(lCurrentThreadID);
		}
		
		AppGlobal.g_oTCP.get().closeClient(iClientSockId);
	}
	
	public void redrawScreen(){
		for(VirtualUIForm oForm:m_FormList){
			oForm.redrawChild();
		}
	}
	
	public void processClientReconnectRelogin(){
		for(VirtualUIForm oForm:m_FormList){
			oForm.processClientReconnectRelogin();
		}
	}
	
	public void rebuildSystemElement(){
		for(Entry<Integer, ClsGlobalElement> entry: m_oGlobalElementList.entrySet()){
			ClsGlobalElement oElement = entry.getValue();
			
			// Set the show flag to false first
			oElement.getElement().finishShow();
			
			// Show the element again
			oElement.getElement().show();
		}
	}
	
	public void removeAllUI(){
		for(VirtualUIForm oForm:m_FormList){
			oForm.removeUI();
		}
	}
	
	public void switchOutlet(String sInterfaceUrl, String sLogin, String sAccessToken, int iOutletId) {
		int iPrevClientSockId;
		JSONObject oView = new JSONObject();
		JSONObject oActionJSONObject = new JSONObject();
		
		// Switch Login
		try {
			oActionJSONObject.put(HeroActionProtocol.System.SwitchLogin.INTERFACE_URL, sInterfaceUrl);
			oActionJSONObject.put(HeroActionProtocol.System.SwitchLogin.LOGIN, sLogin);
			oActionJSONObject.put(HeroActionProtocol.System.SwitchLogin.ACCESS_TOKEN, sAccessToken);
			
			JSONObject oPosInfoJSONObject = new JSONObject();
			oPosInfoJSONObject.put("OutletId", iOutletId);
			JSONObject oExtraInfoJSONObject = new JSONObject();
			oExtraInfoJSONObject.put("Pos", oPosInfoJSONObject);
			oActionJSONObject.put(HeroActionProtocol.System.SwitchLogin.EXTRA_INFO, oExtraInfoJSONObject);
		}
		catch (Exception e) {}
		
		StringBuilder sViews = new StringBuilder();
		for(String sView:AppGlobal.g_oTerm.get().getPacket()){
			if(sViews.length() > 0)
				sViews.append(",");
			else
				sViews.append("\"" + HeroActionProtocol.View.KEY + "\":[");
			sViews.append(sView);
		}
		
		if(sViews.length() > 0) {
			sViews.append("]");
		}
		
		System.out.println("LOGOUT sViews: "+sViews);
		System.out.println("sViews: "+sViews.toString());
		StringBuilder sPacket = new StringBuilder();
		sPacket.append("{");
		sPacket.append(sViews);
		StringBuilder sActions = new StringBuilder();
		sActions.append(",\"" + HeroActionProtocol.System.KEY + "\":{\"" + HeroActionProtocol.System.SwitchLogin.KEY + "\":"+oActionJSONObject.toString()+"}");
		sViews.append(sActions.toString());
		sPacket.append(sActions);
		sPacket.append("}");
		
		// Get the last incoming socket
		iPrevClientSockId = AppGlobal.g_oTerm.get().getClientSocketId();
		
//d("Send Packet in socket (" + iPrevClientSockId + ") >>>>>>>>>> " + sPacket.toString());
		if(AppGlobal.g_oTCP.get().writePacket(iPrevClientSockId, sPacket.toString()) == false){
			// Fail to write to client	
			// Write error log
		}
		
		// Close the client connection
		AppGlobal.g_oTerm.get().closeClientSocket(iPrevClientSockId);
		AppGlobal.g_oTerm.get().clearPacket();
	}
	
	// Logout
	public void closeTerm(){
		int iPrevClientSockId;
		
		// Send the last packet to client
		StringBuilder sActions = new StringBuilder();
		sActions.append("\"" + HeroActionProtocol.System.KEY + "\":{\"" + HeroActionProtocol.System.Logout.KEY + "\":{}},");
		
		StringBuilder sViews = new StringBuilder();
		for(String sView:AppGlobal.g_oTerm.get().getPacket()){
			if(sViews.length() > 0)
				sViews.append(",");
			else
				sViews.append("\"" + HeroActionProtocol.View.KEY + "\":[");
			sViews.append(sView);
		}
		if(sViews.length() > 0)
			sViews.append("]");
		
		StringBuilder sPacket = new StringBuilder();
		sPacket.append("{");
		sPacket.append(sActions);
		sPacket.append(sViews);
		sPacket.append("}");
		
		// Get the last incoming socket
		iPrevClientSockId = AppGlobal.g_oTerm.get().getClientSocketId();
		
d("Send Packet in socket (" + iPrevClientSockId + ") >>>>>>>>>> " + sPacket.toString());
		if(AppGlobal.g_oTCP.get().writePacket(iPrevClientSockId, sPacket.toString()) == false){
			// Fail to write to client	
			// Write error log
		}
		
		// Close the client connection
		AppGlobal.g_oTerm.get().closeClientSocket(iPrevClientSockId);
		AppGlobal.g_oTerm.get().clearPacket();
		
		// Close listen port
		AppGlobal.g_oTCP.get().closeListenSocket();
		
		// Close the selector
		try {
			AppGlobal.g_oSelectorForTCP.get().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Hide keyboard
	public void hideKeyboard(){
		this.setSystemAction(HeroActionProtocol.System.HideKeyboard.KEY, new JSONObject());
	}
	
	// Hide keyboard
	public void showKeyboard(){
		this.setSystemAction(HeroActionProtocol.System.ShowKeyboard.KEY, new JSONObject());
	}
	
	public void setClientImageURLPath(String sURL){
		m_sClientImageURL = sURL;
	}
	
	public String getClientImageURLPath(){
		return m_sClientImageURL;
	}
	
	public void processKillRequest(){
		for(VirtualUIForm oForm:m_FormList){
			oForm.processKillRequest();
		}
	}
	
	public void changeLanguage(int iIndex) {
		m_iCurrentLanguageIndex = iIndex;
		
		// Change all elements' value
		for (VirtualUIForm oVirtualUIForm:m_FormList) {
			for (VirtualUIFrame oVirtualUIFrame:oVirtualUIForm.getChildFrames()) {
				oVirtualUIFrame.changeLanguageIndex(iIndex);
			}
		}
	}
	
	public int getCurrentLanguageIndex() {
		return m_iCurrentLanguageIndex;
	}
	
	public int countUIElement() {
		int iCount = 0;
		for(VirtualUIForm oForm:m_FormList){
			iCount += oForm.countUIElement();
		}
		return iCount;
	}
	
	private void d(String msg){
		System.out.println(msg);
	}
}
