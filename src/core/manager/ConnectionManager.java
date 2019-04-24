package core.manager;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONException;
import org.json.JSONObject;

import core.externallib.TCPLib;
import core.externallib.Util;

public class ConnectionManager {

	private TCPLib m_oTCP;
	private Selector m_oSelectorForTCP;
	private SelectionKey m_oSelectorKeyForTCP;
	
	// Client socket list
	private Queue<Integer> m_oClientSocketIdList;
	
	// Flag to determine if the connection is ready
	private boolean m_bConnected;
	
	public ConnectionManager(){
		m_oTCP = new TCPLib();
		m_oClientSocketIdList = new LinkedList<Integer>();
		m_bConnected = false;
	}
	
	// First connection
    public boolean init(String sAddress, int iPortNo){
    	// Init the socket
    	if(!m_oTCP.initServer(sAddress, iPortNo, false)){
    		// Fail to init the port no.
    		return false;
    	}
		
    	// Selector
		try{
			m_oSelectorForTCP = SelectorProvider.provider().openSelector();
		} catch ( Exception e ) {
			// Internal error
			LoggingManager.stack2Log(e);
			return false;
		}
	
		// Register the server socket channel
		ServerSocketChannel oChannel = m_oTCP.getSocketChannel();
		try {
			m_oSelectorKeyForTCP = oChannel.register(m_oSelectorForTCP, SelectionKey.OP_ACCEPT);
		} catch (ClosedChannelException e) {
			LoggingManager.stack2Log(e);
			return false;
		} catch (Exception e) {
			LoggingManager.stack2Log(e);
			return false;
		}
		
		m_bConnected = true;
		
		return true;
    }
    
    public int getSocketCount() {
    	return m_oClientSocketIdList.size();
    }
	
    // Receive the packet
	public JSONObject recvPacket(){
		int n = 0;
		
		try {
			n = m_oSelectorForTCP.select(0);
		}
		catch (IOException e) {
			LoggingManager.stack2Log(e);
			n = -1;
		}
		catch (ClosedSelectorException e) {
			n = -1;
		}
		catch (Exception e) {
			LoggingManager.stack2Log(e);
			n = -1;
		}
		
		if(n <= 0)
        {
			// Timeout
			return null;
        }
		
        Iterator<SelectionKey> iterator = m_oSelectorForTCP.selectedKeys().iterator();
        while(iterator.hasNext()) 
        {
            SelectionKey oIncomingSelectionkey = (SelectionKey)iterator.next();
            
            iterator.remove();

            // Client connect
            if(!oIncomingSelectionkey.isAcceptable())
            	continue;
            
            if (oIncomingSelectionkey != m_oSelectorKeyForTCP)
            	continue;
            
    		int iClientSockId = m_oTCP.listen();
			if (iClientSockId <= 0)
				continue;
			
			LoggingManager.d("Thread (" + Thread.currentThread().getId() + ") Receive from client in socket (" + iClientSockId + ") <<<<<<<<<< " + m_oTCP.getPacket());		

			if(m_oTCP.getPacket().isEmpty())
				continue;

			try {
				// Turn the packet ot JSON object
				JSONObject recvJSONObj = new JSONObject(m_oTCP.getPacket());
				
				// Store the client socket ID
				m_oClientSocketIdList.offer(iClientSockId);
				
				return recvJSONObj;
				
			} catch (JSONException e) {
				LoggingManager.stack2Log(e);
			} catch (Exception e) {
				LoggingManager.stack2Log(e);
			}
        }
        
        return null;
	}
	
	// Send packet
	public void sendPacket(JSONObject oPacket){
		LoggingManager.d("Thread (" + Thread.currentThread().getId() + ") Send packet >>>>>>>>>>> " + oPacket.toString());
		if(m_oClientSocketIdList.isEmpty()){
			// No client socket is found ==> ERROR
			// Write error log
			LoggingManager.d("No socket");
			
			return;
		}
		
		// Get a client socket
		int iClientSockId = m_oClientSocketIdList.poll();
		String sPacket = "";
		if (oPacket != null)
			sPacket = oPacket.toString();
		
		if(m_oTCP.writePacket(iClientSockId, sPacket) == false){
			// Fail to write to client					
			// Write error log
			LoggingManager.d("Write packet error");
		}
		
		// Close client socket
		m_oTCP.closeClient(iClientSockId);
		
		Util.showClock("event last", false);
	}
	
	// Close connection
	public void close(){
		// Close listen port
		m_oTCP.closeListenSocket();
		
		// Close the selector
		try {
			m_oSelectorForTCP.close();
		}
		catch (IOException e) {
			LoggingManager.stack2Log(e);
		}
		
		m_bConnected = false;
	}
	
	// Check if connection is built or not
	public boolean isConnected(){
		return m_bConnected;
	}
	
	public String getAddress() {
		return m_oTCP.getClientAddress();
	}
	
	// For compatible
	public TCPLib getTCPLib() {
		return m_oTCP;
	}
}
