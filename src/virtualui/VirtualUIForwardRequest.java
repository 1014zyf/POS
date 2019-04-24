package virtualui;

public class VirtualUIForwardRequest {
	
	public enum FORWARD_TYPE{socket};
	
	private String m_sType;
	private String m_sAddress;
	private int m_iPort;
	private String m_sValue;
	private int m_iTimeout;
	private int m_iDelay;
	private boolean m_bBlockUI;
	private long m_lEventVersion;
	
	// Constructor
	public VirtualUIForwardRequest(){
		m_sType = "";
		m_sAddress = "";
		m_iPort = 0;
		m_sValue = "";
		m_iTimeout = 0;
		m_iDelay = 0;
		m_bBlockUI = true;
		m_lEventVersion = 0;
	}
	
	public void setForwardRequestType(String sType){
		m_sType = sType;
	}
		
	public String getForwardRequestType(){
		return m_sType;
	}
	
	public void setForwardRequestAddress(String sAddress){
		m_sAddress = sAddress;
	}
		
	public String getForwardRequestAddress(){
		return m_sAddress;
	}
	
	public void setForwardRequestPort(int iPort){
		m_iPort = iPort;
	}
		
	public int getForwardRequestPort(){
		return m_iPort;
	}
	
	public void setForwardRequestValue(String sValue){
		m_sValue = sValue;
	}
		
	public String getForwardRequestValue(){
		return m_sValue;
	}
	
	public void setForwardRequestTimeout(int iTimeout){
		m_iTimeout = iTimeout;
	}
		
	public int getForwardRequestTimeout(){
		return m_iTimeout;
	}
	
	public void setForwardRequestDelay(int iDelay){
		m_iDelay = iDelay;
	}
		
	public int getForwardRequestDelay(){
		return m_iDelay;
	}
	
	public void setForwardRequestBlockUI(boolean bBlockUI){
		m_bBlockUI = bBlockUI;
	}
	
	public boolean getForwardRequestBlockUI(){
		return m_bBlockUI;
	}
	
	public void incrementForwardRequestVersion(){
		m_lEventVersion++;
		if(m_lEventVersion > 10000)
			// Prevent overflow
			m_lEventVersion = 1;
	}
	
	public long getForwardRequestVersion(){
		return m_lEventVersion;
	}
}
