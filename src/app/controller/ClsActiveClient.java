package app.controller;

public class ClsActiveClient {
	
	private long m_lCurrentThreadId;
	private String m_sUDID;
	private int m_iCurrentOutletId;
	private int m_iCurrentStationId;
	private boolean m_bAutoStation;
	
	public ClsActiveClient(){
		m_lCurrentThreadId = Thread.currentThread().getId();
		m_sUDID = "";
		m_iCurrentOutletId = 0;
		m_iCurrentStationId = 0;
		m_bAutoStation = false;
	}
	
	public long getCurrentThreadId(){
		return m_lCurrentThreadId;
	}
	
	public String getUDID(){
		return m_sUDID;
	}
	
	public void setUDID(String sUDID){
		m_sUDID = sUDID;
	}
	
	public int getCurrentOutletId(){
		return m_iCurrentOutletId;
	}
	
	public void setCurrentOutletId(int iOutletId){
		m_iCurrentOutletId = iOutletId;
	}
	
	public int getCurrentStationId(){
		return m_iCurrentStationId;
	}
	
	public void setCurrentStationId(int iStationId){
		m_iCurrentStationId = iStationId;
	}
	
	public boolean isAutoStation(){
		return m_bAutoStation;
	}
	
	public void setAutoStation(boolean bAutoStation){
		m_bAutoStation = bAutoStation;
	}
	
}
