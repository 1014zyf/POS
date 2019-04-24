package core.externallib;

import core.*;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;   // for IOException and Input/OutputStream
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TCPLib {

	private ServerSocketChannel m_oServSock;
	private Socket m_oSock;
	
	private int m_iServerPortNo;
	private String m_sRecvPacket;
	
	private String m_sClientIPAddress;
	private String m_sServerIPAddress;
	
	private ConcurrentHashMap<Integer, Socket> m_oClientSockList;
	
	public TCPLib(){
		m_oServSock = null;
		m_oSock = null;
		m_oClientSockList = new ConcurrentHashMap<Integer, Socket>();
		m_sClientIPAddress = "";
		m_sServerIPAddress = "";
	}
	
	public boolean initServer(String sClientIPAddress, int iPortNo, boolean bBlockingSocket){
		m_iServerPortNo = iPortNo;
		m_sClientIPAddress = sClientIPAddress;
		try {
			m_oServSock = ServerSocketChannel.open();
			m_oServSock.configureBlocking(bBlockingSocket);
			m_oServSock.socket().bind(new InetSocketAddress(m_iServerPortNo));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean initClient(String sServerIPAddress, int iPort, boolean bBlockingSocket) {
		m_iServerPortNo = iPort;
		m_sServerIPAddress = sServerIPAddress;
		try {
			m_oSock = new Socket(m_sServerIPAddress, m_iServerPortNo);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		if(!m_oSock.isConnected()) 
			return false;
		
		return true;
	}
	
	public int listen(){

		SocketChannel oClientSocketChannel;
		Socket oClientSock;
		int iClientSockId = 0;
		
		m_sRecvPacket = "";
		
		try {
			oClientSocketChannel = m_oServSock.accept();
			oClientSock = oClientSocketChannel.socket();
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
		// Store the socket to the list (Max 100 connection)
		for(int i=1; i<=Core.g_oClientManager.g_iClientPortCount; i++){
			if(!m_oClientSockList.containsKey(i)) {
				m_oClientSockList.put(i, oClientSock);
				iClientSockId = i;
				break;
			}
		}
		if(iClientSockId == 0){
			// No socket is allowed
			try {
				oClientSock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			oClientSock = null;
			return 0;
		}

		if(m_sClientIPAddress.isEmpty())
			// FOR DEVELOPMENT/DEBUG ONLY
			m_sClientIPAddress = oClientSock.getInetAddress().getHostAddress();

		try {
			InputStream in = oClientSock.getInputStream();
		
			List<Byte> bytes = new ArrayList<Byte>();
			// Receive until client closes connection, indicated by -1 return
			byte data;
			while (true){
				// Packet finish (EOT is got)
				data = (byte)in.read();
				if(data == -1 || data == 4)
					break;

				bytes.add(data);
			}
			
			m_sRecvPacket = StringLib.byteListToString(bytes);
		}
		catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		
		return iClientSockId;
	}
	
	public boolean writePacket(int iClientSockId, String sPacket){
		if(!m_oClientSockList.containsKey(iClientSockId))
			return false;
		
		Socket oClientSock = m_oClientSockList.get(iClientSockId);
		if(oClientSock == null)
			return false;
		
		try {
			OutputStream out = oClientSock.getOutputStream();
			byte[] sPacketBytes = sPacket.getBytes();
			
			// Write through client socket
			out.write(sPacketBytes, 0, sPacketBytes.length);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean writeToServer(String sSendData) {
		DataOutputStream oClientOutputStream;
		
		if(!m_oSock.isConnected())
			return false;
		
		try {
			oClientOutputStream = new DataOutputStream(m_oSock.getOutputStream());
			oClientOutputStream.writeBytes(sSendData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public String readFromServer(String sServerLocale) {
		String sReceiveString = "";
		BufferedInputStream oInputStream;
		InputStreamReader oInputStreamReader;
		StringBuffer instr = new StringBuffer();
		
		try {
			oInputStream = new BufferedInputStream(m_oSock.getInputStream());
			oInputStreamReader = new InputStreamReader(oInputStream, sServerLocale);
			
			int iReceiveData;
			while(true) {
				iReceiveData = oInputStreamReader.read();
				instr.append((char)iReceiveData);
				if(iReceiveData == 4)
					break;
			}
			
			sReceiveString = instr.toString();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return sReceiveString;
	}
	
	public void closeClient(int iClientSockId){
		Socket oClientSock = m_oClientSockList.get(iClientSockId);
		if(oClientSock == null)
			return;
		
		try {
			oClientSock.close();
			oClientSock = null;
			
			m_oClientSockList.remove(iClientSockId);
		} catch (IOException e) {
			e.printStackTrace();
		}	// Close the socket.  We are done with this client!
	}
	
	public void closeClientSocket() {
		try {
			m_oSock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeListenSocket(){
		try {
			m_oServSock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		m_oServSock = null;
	}
	
	public String getPacket(){
		return m_sRecvPacket;
	}
		
	public String getClientAddress(){
		return m_sClientIPAddress;
	}

	public int getServerPort(){
		return m_iServerPortNo;
	}
	
	public ServerSocketChannel getSocketChannel(){
		return m_oServSock;
	}
	
	public boolean isClientSocketConnected() {
		return m_oSock.isConnected();
	}
}
