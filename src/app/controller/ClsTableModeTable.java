package app.controller;

public class ClsTableModeTable {
	private String m_sName;
	private String m_sTableStyle;
	private int m_iTable;
	private String m_sTableExtension;
	private int m_iType; // 0 or 1, 0 is the original table, 1 is the brand new table
	private String m_sForegroundColor;
	private String m_sBackgroundColor;
	private boolean m_bLocked;
	private boolean m_bPrinted;
	private int m_iTableExtensionCount;
	
	public ClsTableModeTable(String sName, String sTableStyle, int iTable, String sTableExtension, int iType, String sForegroundColor, String sBackgroundColor){
		this.m_sName = sName;
		this.m_sTableStyle = sTableStyle;
		this.m_iTable = iTable;
		this.m_sTableExtension = sTableExtension;
		this.m_iType = iType;
		this.m_sForegroundColor = sForegroundColor;
		this.m_sBackgroundColor = sBackgroundColor;
		this.m_bLocked = false;
		this.m_bPrinted = false;
		this.m_iTableExtensionCount = 0;
	}
	
	public ClsTableModeTable(){
		this.m_sName = "";
		this.m_sTableStyle = "";
		this.m_iTable = 0;
		this.m_sTableExtension ="";
		this.m_iType = 0;
		this.m_sForegroundColor = "";
		this.m_sBackgroundColor = "";
		this.m_bLocked = false;
		this.m_bPrinted = false;
		this.m_iTableExtensionCount = 0;
	}
	
	public String getTableName(){
		if(m_sName.isEmpty()){
			return m_iTable+m_sTableExtension;
		}
		else{
			return m_sName;
		}	
	}
	
	public String getTableStyle() {
		return m_sTableStyle;
	}
	
	public int getTableNo(){
		return m_iTable;	
	}
	
	public String getTableExtension(){
		return m_sTableExtension;
	}
	
	public int getTableType(){
		return m_iType;
	}
	
	public String getForegroundColor(){
		return m_sForegroundColor;
	}
	
	public String getBackgroundColor(){
		return m_sBackgroundColor;
	}
	
	public boolean isLocked(){
		return m_bLocked;
	}
	
	public boolean isPrinted(){
		return m_bPrinted;
	}
	
	public int getTableExtensionCount(){
		return m_iTableExtensionCount;
	}
	
	public void setForegroundColor(String sForegroundColor){
		m_sForegroundColor = sForegroundColor;
	}
	
	public void setBackgroundColor(String sBackgroundColor){
		m_sBackgroundColor = sBackgroundColor;
	}
	
	public void setLocked(boolean bLocked){
		m_bLocked = bLocked;
	}
	
	public void setPrinted(boolean bPrinted){
		m_bPrinted = bPrinted;
	}
	
	public void setTableExtensionCount(int iTableExtensionCount){
		m_iTableExtensionCount = iTableExtensionCount;
	}
}
