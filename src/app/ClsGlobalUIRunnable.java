package app;

public class ClsGlobalUIRunnable implements Runnable {
	
	int m_iClientSockId;
	
	@Override
	public void run() {
		
	}
	
	public void setClientSockId(int iClientSockId) {
		m_iClientSockId = iClientSockId;
	}
	
	public int getClientSockId() {
		return m_iClientSockId;
	}
}
