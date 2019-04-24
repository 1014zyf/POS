package app;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import externallib.StringLib;

public class ClsTableModeTable implements Comparator<ClsTableModeTable> {
	private String[] m_sName;
	private String m_sTableStyle;
	private int m_iTable;
	private String m_sTableExtension;
	private int m_iType; // 0 or 1, 0 is the original table, 1 is the brand new table
	private String m_sForegroundColor;
	private String m_sBackgroundColor;
	private boolean m_bLocked;
	private boolean m_bPrinted;
	private int m_iTableExtensionCount;
	private int m_iTableSize;
	
	public ClsTableModeTable(String[] sName, String sTableStyle, int iTable, String sTableExtension, int iType, String sForegroundColor, String sBackgroundColor, int iTableSize){
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
		this.m_iTableSize = iTableSize;
	}
	
	public ClsTableModeTable(){
		this.m_sName = StringLib.createStringArray(5, "");
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
	
	public String[] getTableName(){
		String[] sTableName = null;
		if(m_iTable == 0){
			sTableName = StringLib.createStringArray(5, m_sTableExtension);
		}else{
			sTableName = StringLib.createStringArray(5, m_iTable+m_sTableExtension);
		}

		for (int i=0; i<m_sName.length; i++) {
			if (!m_sName[i].isEmpty())
				sTableName[i] = m_sName[i];
		}
		return sTableName;
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
	
	public int getTableSize() {
		return m_iTableSize;
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
	
	@Override
	public int compare(ClsTableModeTable oTable1, ClsTableModeTable oTable2) {
		String sPreTableName = oTable1.getTableName()[AppGlobal.g_oCurrentLangIndex.get()-1];
		String sCurTableName = oTable2.getTableName()[AppGlobal.g_oCurrentLangIndex.get()-1];
		
		int sPreNum = stringHandle(sPreTableName);
		int sCurNum = stringHandle(sCurTableName);
		
		if(sPreNum != -1 && sCurNum != -1){
			return sPreNum - sCurNum;
		}
		
		return sPreTableName.compareTo(sCurTableName);
	}
	
	public static int stringHandle(String sTableName){
		Pattern oPattern = Pattern.compile("(\\d+).*");
		Matcher oMatcher = oPattern.matcher(sTableName);
		Integer oInteger = -1;
		try {
			if(sTableName.substring(0,1).matches("[0-9]")){
			    if (oMatcher.find()) {
			    	oInteger = Integer.valueOf(oMatcher.group(1));
			    }
			}
		} catch (Exception e) {}
		return oInteger;
	}
}
