package core.manager;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import core.externallib.Util;

public class ClientManager {

	public int g_iClientStartingPort;
	public int g_iClientPortCount;
	
	private ThreadLocal<ActiveClient> m_oActiveClient;
	private ConcurrentHashMap<String, ActiveClient> m_oUDIDActiveClient;
	
	public ClientManager() {
		m_oUDIDActiveClient = new ConcurrentHashMap<String, ActiveClient>();
		m_oActiveClient = new ThreadLocal<ActiveClient>();
	}
	
	public void setPortConfig(int iStartingPort, int iPortCount) {
		g_iClientStartingPort = iStartingPort;
		g_iClientPortCount = iPortCount;
	}
	
	public ActiveClient getActiveClient(String sUDID) {
		return m_oUDIDActiveClient.get(sUDID);
	}
	
	public ActiveClient getActiveClient() {
		return m_oActiveClient.get();
	}
	
	public void addActiveClient(String sUDID, ActiveClient oActiveClient) {
		m_oUDIDActiveClient.put(sUDID, oActiveClient);
	}
	
	public ActiveClient removeActiveClient(String sUDID) {
		return m_oUDIDActiveClient.remove(sUDID);
	}
	
	public int getActiveClientCount() {
		return m_oUDIDActiveClient.size();
	}
	
	public int getNextAvailablePort() {
		if (m_oUDIDActiveClient.size() <= g_iClientPortCount) {
			int iPortNo = 0;
			Random rand = new Random();
			rand.setSeed(new Date().getTime());
			
			while(true) {				//Port no is used by another client
				iPortNo = rand.nextInt(g_iClientPortCount + 1) + g_iClientStartingPort;
				if (!isClientPortOccupied(iPortNo) && Util.isPortAvailable(iPortNo))
					break;
			}
			return iPortNo;
		}
		else {
			//	No available port
			return -1;
		}
	}
	
	//	Must call after every logic thread to access Client resource
	public void registerCurrentThread(ActiveClient oActiveClient) {
		m_oActiveClient.set(oActiveClient);
	}
	
	public void unregisterCurrentThread() {
		m_oActiveClient.remove();
	}
	
	private boolean isClientPortOccupied(int port) {
		for (ActiveClient oActiveClient : m_oUDIDActiveClient.values()) {
			if (oActiveClient.g_iPort == port)
				return true;
		}
		return false;
	}
}
