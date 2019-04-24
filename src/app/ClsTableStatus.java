package app;

import om.PosCheck;
import om.PosOutletTable;

public class ClsTableStatus {
	private PosOutletTable m_oPosOutletTable;
	private PosCheck m_oPosCheck;
	
	private int m_iTableExtensionCount;
	private boolean m_bNeedUpdate;
	
	public ClsTableStatus(){
		m_oPosOutletTable = null;
		m_oPosCheck = null;
		m_iTableExtensionCount = 0;
		m_bNeedUpdate = true;
	}
	
	public void setPosOutletTable(PosOutletTable oPosOutletTable){
		m_oPosOutletTable = oPosOutletTable;
	}
	
	public PosOutletTable getPosOutletTable(){
		return m_oPosOutletTable;
	}
	
	public void setPosCheck(PosCheck oPosCheck){
		m_oPosCheck = oPosCheck;
	}
	
	public PosCheck getPosCheck(){
		return m_oPosCheck;
	}
	
	public void setTableExtensionCount(int iTableExtensionCount){
		m_iTableExtensionCount = iTableExtensionCount;
	}
	
	public int getTableExtensionCount(){
		return m_iTableExtensionCount;
	}
	
	public void setNeedUpdate(boolean bNeedUpdate){
		m_bNeedUpdate = bNeedUpdate;
	}
	
	public boolean getNeedUpdate(){
		return m_bNeedUpdate;
	}
}
