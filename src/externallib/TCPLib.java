package externallib;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;   // for IOException and Input/OutputStream

public class TCPLib {

	private ServerSocketChannel m_oServSock;
	private Socket m_oSock;
	
	private int m_iServerPortNo;
	private String m_sRecvPacket;
	
	private String m_sClientIPAddress;
	private String m_sServerIPAddress;
	private int m_iEndByte;
	private String m_sCharSet;
	
	private ConcurrentHashMap<Integer, Socket> m_oClientSockList;
	
	public TCPLib(){
		m_oServSock = null;
		m_oSock = null;
		m_oClientSockList = new ConcurrentHashMap<Integer, Socket>();
		m_sClientIPAddress = "";
		m_sServerIPAddress = "";
		m_iEndByte = 4;
		m_sCharSet = "";
	}
	
	// Return value :	Empty string - no error
	//					Error string - cannot init port
	public String initServer(String sClientIPAddress, int iPortNo, boolean bBlockingSocket){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		m_sClientIPAddress = sClientIPAddress;
		try {
			m_oServSock = ServerSocketChannel.open();
		} catch (IOException e1) {
			e1.printStackTrace(pw);
			return sw.toString();
		}
		try{
			m_oServSock.configureBlocking(bBlockingSocket);
		} catch (IOException e1) {
			e1.printStackTrace(pw);
			return sw.toString();
		}
		try{
			m_oServSock.socket().bind(new InetSocketAddress(iPortNo));
		} catch (IOException e1) {
			// Retry 5 times before give up
			int i;
			for(i=0; i<5; i++){
				try{
					m_oServSock.socket().bind(new InetSocketAddress(iPortNo));
				} catch (IOException e2) {
					// Fail
					continue;
				}
				
				// Success
				break;
			}
			if(i == 5){
				// Fail to bind port
				e1.printStackTrace(pw);
				return sw.toString();
			}
		}
		
		m_iServerPortNo = m_oServSock.socket().getLocalPort();
		return "";
	}
	
	public boolean initClient(String sServerIPAddress, int iPort, boolean bBlockingSocket) {
		m_iServerPortNo = iPort;
		m_sServerIPAddress = sServerIPAddress;
		try {
			m_oSock = new Socket(m_sServerIPAddress, m_iServerPortNo);
		} catch (IOException e1) {
			e1.printStackTrace();
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
		
		int recvMsgSize;					// Size of received message
		byte[] byteBuffer = new byte[1];	// Receive buffer
		
		try {
			oClientSocketChannel = m_oServSock.accept();
			oClientSock = oClientSocketChannel.socket();
		} catch (IOException e1) {
			e1.printStackTrace();
			return 0;
		} catch (Exception e2) {
			e2.printStackTrace();
			return 0;
		}
	
//d("Handling client at " + m_sClientIPAddress + " on port " +	m_iServerPortNo);
		
		// Store the socket to the list (Max 100 connection)
		for(int i=1; i<=100; i++){
			if(m_oClientSockList.containsKey(i))
				continue;
			
			m_oClientSockList.put(i, oClientSock);
			iClientSockId = i;
			break;
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

		if(m_sClientIPAddress.length() == 0)
			// FOR DEVELOPMENT/DEBUG ONLY
			m_sClientIPAddress = oClientSock.getInetAddress().getHostAddress();

		try {
			InputStream in = oClientSock.getInputStream();
		
			List<Byte> bytes = new ArrayList<Byte>();
			// Receive until client closes connection, indicated by -1 return
			while ((recvMsgSize = in.read(byteBuffer)) != -1){
				// Packet finish (EOT is got)
				if(byteBuffer[0] == m_iEndByte) {
					// for end character '}', it need to add into bytes, otherwise cannot convert to JSONObject
					if(m_iEndByte == 125)
						bytes.add(byteBuffer[0]);
					
					break;
				}

				bytes.add(byteBuffer[0]);
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
		if(m_oClientSockList.containsKey(iClientSockId)== false)
			return false;
		
		Socket oClientSock = m_oClientSockList.get(iClientSockId);
		if(oClientSock == null)
			return false;
		
		try {
			OutputStream out = oClientSock.getOutputStream();
			
			// Write through client socket
			if(m_sCharSet.isEmpty())
				out.write(sPacket.getBytes(), 0, sPacket.getBytes().length);
			else
				out.write(sPacket.getBytes(m_sCharSet), 0, sPacket.getBytes(m_sCharSet).length);
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean writePacketByBytes(int iClientSockId, byte[] sSendingByte){
		if(m_oClientSockList.containsKey(iClientSockId)== false)
			return false;
		
		Socket oClientSock = m_oClientSockList.get(iClientSockId);
		if(oClientSock == null)
			return false;
		
		try {
			OutputStream out = oClientSock.getOutputStream();
			
			// Write through client socket
			out.write(sSendingByte, 0, sSendingByte.length);
			
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
	
	public String readFromServer(String sServerLocale) throws ClassNotFoundException {
		String sReceiveString = "";
		BufferedInputStream oInputStream;
		InputStreamReader oInputStreamReader;
		StringBuffer instr = new StringBuffer();
		
		try {
			oInputStream = new BufferedInputStream(m_oSock.getInputStream());
			oInputStreamReader = new InputStreamReader(oInputStream, sServerLocale);
			
			int iReceiveData;
			//while((iReceiveData = oInputStreamReader.read()) != 4) {
			while(true) {
				iReceiveData = oInputStreamReader.read();
				instr.append( (char) iReceiveData);
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
		if(m_oClientSockList.containsKey(iClientSockId)== false)
			return;		
		
		Socket oClientSock = m_oClientSockList.get(iClientSockId);
		if(oClientSock == null)
			return;
		
		try {
			// Close the socket.  We are done with this client!
			if (!oClientSock.isClosed())
				oClientSock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		oClientSock = null;
		m_oClientSockList.remove(iClientSockId);
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
	
	private void d(String msg){
		System.out.println(msg);
	}
	
	public boolean isClientSocketConnected() {
		return m_oSock.isConnected();
	}
	
	public void setEndByte(int iEndByte) {
		m_iEndByte = iEndByte;
	}
	
	public void setCharSet(String sCharSet) {
		m_sCharSet = sCharSet;
	}
	
	public void getClientPort() {
		System.out.println();
	}
	
	public void printMesssige(SelectionKey oIncomingSelectionkey,Selector select) {
		ServerSocketChannel channel = (ServerSocketChannel) oIncomingSelectionkey.channel();
		try {
			SocketChannel sChannel = channel.accept();
			System.out.println(sChannel.socket().getLocalPort()+"_____________"+sChannel.socket().getRemoteSocketAddress());
			ByteBuffer rb= ByteBuffer.allocate(1024*10*2);
			
			StringBuilder bu = new StringBuilder();
			int i = sChannel.read(rb);
			rb.flip();
			while(i > 0) {
				String str = new String(rb.array(),0,i);
				System.out.println("從客戶端接收到數據為："+str);
				sChannel.register(select, SelectionKey.OP_READ);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
