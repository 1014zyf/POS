package app.controller;

import java.math.BigDecimal;

public class ClsOctopusTransaction {
	private String m_sProviderId;
	private BigDecimal m_dTransactionAmount;
	private String m_sTransactionTime;
	private String m_sMachineId;
	private String m_sServiceInfo;
	
	public ClsOctopusTransaction(){
		m_sProviderId = "";
		m_dTransactionAmount = BigDecimal.ZERO;
		m_sTransactionTime = "";
		m_sMachineId = "";
		m_sServiceInfo = "";
	}
	
	public void setProviderId(String sSPId){
		m_sProviderId = sSPId;
	}
	
	public String getProviderId(){
		return m_sProviderId;
	}
	
	public void setTransactionAmount(BigDecimal dTransactionAmount){
		m_dTransactionAmount = dTransactionAmount;
	}
	
	public BigDecimal getTransactionAmount(){
		return m_dTransactionAmount;
	}
	
	public void setTransactionTime(String sTransactionTime){
		m_sTransactionTime = sTransactionTime;
	}
	
	public String getTransactionTime(){
		return m_sTransactionTime;
	}
	
	public void setMachineId(String sMachineId){
		m_sMachineId = sMachineId;
	}
	
	public String getMachineId(){
		return m_sMachineId;
	}
	
	public void setServiceInfo(String sServiceInfo){
		m_sServiceInfo = sServiceInfo;
	}
	
	public String getServiceInfo(){
		return m_sServiceInfo;
	}
}
