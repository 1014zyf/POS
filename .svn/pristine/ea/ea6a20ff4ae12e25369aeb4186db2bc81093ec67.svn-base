package app;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import externallib.StringLib;
import om.OutFloorPlanTable;
import om.PosOutletTable;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIList;

interface FrameTableModeListener {
	void frameTableFloorPlan_TableClicked(String sTable, String sTableExtension);
    void frameTableFloorPlan_TableLongClicked(String sTable, String sTableExtension);
}

public class FrameTableMode extends VirtualUIFrame implements FrameTableButtonListener {
	TemplateBuilder m_oTemplateBuilder;
	private int m_iTotalFrameTableNum;
	private int m_iFrameTableNum;
	private VirtualUIList m_oListTableFrame;
	private VirtualUIFrame m_oFrameTable;
	private ArrayList<VirtualUIFrame> m_oArrayListFrameTable;
	private ArrayList<ClsTableModeTable> m_oListTable;
	private ArrayList<FrameTableButton> m_oArrayListTableButton;
	private HashMap<String, String> m_sTableForegroundColor;
	private HashMap<String, String> m_sTableBackgroundColor;
	private int m_iSkipReseqTable;
	
	public static final int TABLE_BUTTON_SPACE = 10;
	
	private ArrayList<FrameTableModeListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameTableModeListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameTableModeListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
  //set up the size of each row of VirtualUIList
    public void setFrameTableNum(int iFrameTableNum){
    	m_iFrameTableNum = iFrameTableNum;
    }
        
    public int getTableWidth(){
		return (m_oListTableFrame.getWidth() - (m_iFrameTableNum - 1) * TABLE_BUTTON_SPACE) / m_iFrameTableNum;
    }
    
    public int getTableHeight(){
		return (m_oListTableFrame.getHeight() - (AppGlobal.g_oFuncStation.get().getTableModeRowColumn("row") - 1) * TABLE_BUTTON_SPACE) / AppGlobal.g_oFuncStation.get().getTableModeRowColumn("row");
    }
    
    public int getTableLeft(){
    	if(m_iTotalFrameTableNum == 0)
    		return 0;
    	else
			return (getTableWidth() + TABLE_BUTTON_SPACE) * m_iTotalFrameTableNum;
    }
    
    public void init(){
    	m_oTemplateBuilder = new TemplateBuilder();
    	m_oTemplateBuilder.loadTemplate("fraTableMode.xml");
    	
    	m_iFrameTableNum = 0;
    	m_oFrameTable = new VirtualUIFrame();
    	
    	m_iSkipReseqTable = 0;
    	
    	listeners = new ArrayList<FrameTableModeListener>();
    	//Array list of all table class
    	m_oListTable = new ArrayList<ClsTableModeTable>();
    	m_oArrayListTableButton = new ArrayList<FrameTableButton>();
    	m_oArrayListFrameTable = new ArrayList<VirtualUIFrame>();
    	
    	m_sTableForegroundColor = new HashMap<String, String>();
		m_sTableBackgroundColor = new HashMap<String, String>();
    	
		m_oListTableFrame = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListTableFrame, "listVerticalTable");
		//m_oListTableFrame.setHeight(this.getHeight());
		//m_oListTableFrame.setWidth(this.getWidth());
		m_oListTableFrame.allowLongClick(true);
		m_oListTableFrame.allowClick(true);
		m_oListTableFrame.allowSwipeBottom(true);
		m_oListTableFrame.allowSwipeTop(true);
		this.attachChild(m_oListTableFrame);
		
		// Get Default Status Background Color Codes
		JSONObject oTableStatusBackgroundColor = AppGlobal.g_oFuncStation.get().getTableStatusBackgroundColor();
		
		VirtualUIFrame oFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrame, "fraNewTable");
		m_sTableForegroundColor.put(PosOutletTable.STATUS_NEW_TABLE, oFrame.getForegroundColor());
		m_sTableBackgroundColor.put(PosOutletTable.STATUS_NEW_TABLE, this.getTableStatusBackgroundColor(oFrame.getBackgroundColor(), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_VACANT));
		
		m_oTemplateBuilder.buildFrame(oFrame, "fraVacantTable");
		m_sTableForegroundColor.put(PosOutletTable.STATUS_VACANT, oFrame.getForegroundColor());
		m_sTableBackgroundColor.put(PosOutletTable.STATUS_VACANT, this.getTableStatusBackgroundColor(oFrame.getBackgroundColor(), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_VACANT));
		
		m_oTemplateBuilder.buildFrame(oFrame, "fraCleaningTable");
		m_sTableForegroundColor.put(PosOutletTable.STATUS_CLEANING_TABLE, oFrame.getForegroundColor());
		m_sTableBackgroundColor.put(PosOutletTable.STATUS_CLEANING_TABLE, this.getTableStatusBackgroundColor(oFrame.getBackgroundColor(), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_CLEANING));
		
		m_oTemplateBuilder.buildFrame(oFrame, "fraOccupiedTable");
		m_sTableForegroundColor.put(PosOutletTable.STATUS_OCCUPIED, oFrame.getForegroundColor());
		m_sTableBackgroundColor.put(PosOutletTable.STATUS_OCCUPIED, this.getTableStatusBackgroundColor(oFrame.getBackgroundColor(), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_OCCUPIED));
		
		m_oTemplateBuilder.buildFrame(oFrame, "fraPrintedTable");
		m_sTableForegroundColor.put(PosOutletTable.STATUS_CHECK_PRINTED, oFrame.getForegroundColor());
		m_sTableBackgroundColor.put(PosOutletTable.STATUS_CHECK_PRINTED, this.getTableStatusBackgroundColor(oFrame.getBackgroundColor(), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_PRINTED));
		
		m_oTemplateBuilder.buildFrame(oFrame, "fraCookingOvertimeTable");
		m_sTableForegroundColor.put(PosOutletTable.STATUS_COOKING_OVERTIME, oFrame.getForegroundColor());
		m_sTableBackgroundColor.put(PosOutletTable.STATUS_COOKING_OVERTIME, oFrame.getBackgroundColor());
		
		m_oTemplateBuilder.buildFrame(oFrame, "fraReservationTable");
		m_sTableForegroundColor.put(PosOutletTable.STATUS_TABLE_RESERVATION, oFrame.getForegroundColor());
		m_sTableBackgroundColor.put(PosOutletTable.STATUS_TABLE_RESERVATION, this.getTableStatusBackgroundColor(oFrame.getBackgroundColor(), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_SEAT_IN));
	}
	
	//initial one button
	private FrameTableButton createTableButton(String[] sName, String sTableStyle, int iTable, String sTableExtension, int iTableSize) {
		FrameTableButton oFrameTableButton = new FrameTableButton();
		oFrameTableButton.setExist(true);
		oFrameTableButton.setTop(0);
		oFrameTableButton.setLeft(getTableLeft());
		oFrameTableButton.setWidth(getTableWidth());
		oFrameTableButton.setHeight(getTableHeight());
		String[] sDisplayName = Arrays.copyOfRange(sName, 0, sName.length);
		for (int i = 0; i < sDisplayName.length; i++) {
			if (sDisplayName[i].length() == 0) {
				if (iTable == 0) {
					sDisplayName[i] = sTableExtension;
				}else
					sDisplayName[i] = iTable + sTableExtension;
			}
		}
		oFrameTableButton.setupTableButton(sDisplayName, sTableStyle, iTable, sTableExtension);
		oFrameTableButton.allowClick(true);
		oFrameTableButton.allowLongClick(true);
		oFrameTableButton.setClickServerRequestNote(iTable + "_" + sTableExtension);
		oFrameTableButton.setLongClickServerRequestNote(iTable + "_" + sTableExtension);
		oFrameTableButton.m_oFrameBackground.setShadowColor("#19000000");
		oFrameTableButton.m_oFrameBackground.setShadowLeft(0);
		oFrameTableButton.m_oFrameBackground.setShadowRadius(6);
		oFrameTableButton.m_oFrameBackground.setShadowTop(5);
		oFrameTableButton.addListener(this);
		
		if (m_sTableForegroundColor.containsKey(PosOutletTable.STATUS_NEW_TABLE)) {
			oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(PosOutletTable.STATUS_NEW_TABLE));
			oFrameTableButton.setTableColor(m_sTableBackgroundColor.get(PosOutletTable.STATUS_NEW_TABLE), PosOutletTable.STATUS_NEW_TABLE, AppGlobal.g_oLang.get()._("vacant", ""));
		} else {
			oFrameTableButton.setTableColor("#FFFFFF", PosOutletTable.STATUS_NEW_TABLE, AppGlobal.g_oLang.get()._("vacant", ""));
		}
		
		JSONObject oSwitchCheckInfo = AppGlobal.g_oFuncStation.get().getSwitchCheckInfoSetting();
		boolean bIsTurnOffAllSwitchCheckInfo = this.isTurnOffAllSwitchCheckInfo(oSwitchCheckInfo);
		
		oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_OPEN_TIME, AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/floor_check_open_time.png", false);
		oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_COVER_NO, AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_main_cover.png", false);
		if (bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_TOTAL).equals("y")))
			oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_CHECK_TOTAL, "", false);
		if (bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_MEMBER_NUMBER).equals("y")))
			oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_MEMBER_NUMBER, "", false);
		if (bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_MEMBER_NAME).equals("y")))
			oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_MEMBER_NAME, "", false);
		if (bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_OWNER_NAME).equals("y")))
			oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_OWNER_NAME, "", false);
		if (AppGlobal.g_oFuncStation.get().isAllowShowTableSize()) {
			oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_TABLE_SIZE, "", false);
		}
		
		if (oSwitchCheckInfo != null) {
			if (oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_INFO_ONE).equals("y"))
				oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_CHECK_INFO_ONE, "", false);
			if (oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_INFO_TWO).equals("y"))
				oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_CHECK_INFO_TWO, "", false);
			if (oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_INFO_THREE).equals("y"))
				oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_CHECK_INFO_THREE, "", false);
			if (oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_INFO_FOUR).equals("y"))
				oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_CHECK_INFO_FOUR, "", false);
			if (oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_INFO_FIVE).equals("y"))
				oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_CHECK_INFO_FIVE, "", false);
		}
		if (oFrameTableButton.isOnlySkipInfo())
			oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_EMPTY, "", false);
		
		String sDefaultDisplay = "";
		if ((oSwitchCheckInfo != null))
			sDefaultDisplay = oSwitchCheckInfo.optString("default_display");
		if (!sDefaultDisplay.isEmpty()) {
			if (!sDefaultDisplay.equals(FrameTableButton.STATUS_OPEN_TIME) &&
					!sDefaultDisplay.equals(FrameTableButton.STATUS_COVER_NO) &&
					!sDefaultDisplay.equals(FrameTableButton.STATUS_TABLE_SIZE))
				oFrameTableButton.setCurrentDetailByKey(sDefaultDisplay);
		} else
			oFrameTableButton.setTableDetailByIndex(this.getCurrentTableDetailIndex());

		return oFrameTableButton;
	}
	
	//initial one frame as a row of VirtualUIList
	public void addTableModeRow() {
		if (m_iTotalFrameTableNum == m_iFrameTableNum) {
			m_iTotalFrameTableNum = 0;
		}
		m_oFrameTable = new VirtualUIFrame();
		m_oFrameTable.setExist(true);
		m_oFrameTable.allowClick(false);
		m_oFrameTable.setTop(0);
		m_oFrameTable.setLeft(0);
		m_oFrameTable.setHeight(getTableHeight() + TABLE_BUTTON_SPACE);
		m_oFrameTable.setWidth(m_oListTableFrame.getWidth());
	}
	
	//attach table button to the VirtualUIList
	public void addTable(String[] sName, String sTableStyle, int iTable, String sTableExtension, int iType, String sForegroundColor, String sBackgroundColor, int iTableSize) {
		boolean bCheckTable = true;
		boolean bRefreshFrameFlag = true;
		
		//check the new table whether exist
		if (!m_oListTable.isEmpty()) {
			if (isTableButtonExist(iTable, sTableExtension)) {
				bCheckTable = false;
			}
		}
		//check the new table whether is bigger than the last table no.
		if (!m_oListTable.isEmpty()) {
			String sNewTableName = "";
			if (sName.length > 0)
				sNewTableName = sName[AppGlobal.g_oCurrentLangIndex.get() - 1];
			
			if (sNewTableName.equals("")) {
				if (iTable == 0)
					sNewTableName = sTableExtension;
				else
					sNewTableName = iTable + sTableExtension;
			}
			String sLastTableName = m_oListTable.get(m_oListTable.size() - 1).getTableName()[AppGlobal.g_oCurrentLangIndex.get() - 1];
			
			if (ClsTableModeTable.stringHandle(sNewTableName) != -1 && ClsTableModeTable.stringHandle(sLastTableName) != -1) {
				if (ClsTableModeTable.stringHandle(sNewTableName) > ClsTableModeTable.stringHandle(sLastTableName))
					bRefreshFrameFlag = false;
			} else if (ClsTableModeTable.stringHandle(sNewTableName) == -1 && ClsTableModeTable.stringHandle(sLastTableName) == -1) {
				if (sNewTableName.compareTo(sLastTableName) > 0)
					bRefreshFrameFlag = false;
			}
//			if (iTable > m_oListTable.get(m_oListTable.size()-1).getTableNo())
//				bRefreshFrameFlag = false;
		}
		
		if (bCheckTable == true) {
			if (m_iTotalFrameTableNum == 0 || m_iTotalFrameTableNum == m_iFrameTableNum) {
				addTableModeRow();
				m_oListTableFrame.attachChild(m_oFrameTable);
				m_oArrayListFrameTable.add(m_oFrameTable);
			}
			
			FrameTableButton oFrameTableButton = createTableButton(sName, sTableStyle, iTable, sTableExtension, iTableSize);
			// Set corner radius when create table
			oFrameTableButton.setCornerRadius("8,8,0,0");
			//oFrameTableButton.setShowDetail(true);
			m_oArrayListTableButton.add(oFrameTableButton);
			
			ClsTableModeTable oTable = new ClsTableModeTable(sName, sTableStyle, iTable, sTableExtension, iType, sForegroundColor, sBackgroundColor, iTableSize);
			
			m_oListTable.add(oTable);
			m_iTotalFrameTableNum++;
			
			//if the table doesn't exist in the list and smaller than the last table no., refresh the location of table button
			if(bRefreshFrameFlag == true){
				if (m_iSkipReseqTable == 0) {
					refreshFrameTableList();
				} else {
					m_iSkipReseqTable = 2;		// Turn to 2 for enable re-sequence after all add table
				}
	    	}
			
			m_oFrameTable.attachChild(oFrameTableButton);
		}
	}
	
	public void deleteTable(int iChangePosition) {
		int iLastTable = m_oArrayListTableButton.size() - 1;
		m_oFrameTable.removeChild(m_oArrayListTableButton.get(iLastTable).getId());
		m_oArrayListTableButton.remove(iLastTable);
		m_iTotalFrameTableNum--;
		if (m_iTotalFrameTableNum == 0) {
			m_iTotalFrameTableNum = m_iFrameTableNum;
			if (m_oArrayListFrameTable.size() > 1) {
				m_oFrameTable = m_oArrayListFrameTable.get(m_oArrayListFrameTable.size() - 2);
				m_oListTableFrame.removeChild(m_oArrayListFrameTable.get(m_oArrayListFrameTable.size() - 1).getId());
				m_oArrayListFrameTable.remove(m_oArrayListFrameTable.size() - 1);
			} else {
				m_oListTableFrame.removeChild(m_oArrayListFrameTable.get(m_oArrayListFrameTable.size() - 1).getId());
				m_oArrayListFrameTable.remove(m_oArrayListFrameTable.size() - 1);
			}
		}
		m_oListTable.remove(iChangePosition);
		
		this.setTableButtonsInfo(iChangePosition);
	}
	
	public void skipReseqTable(boolean bSkip) {
		if (bSkip) {
			m_iSkipReseqTable = 1;
		} else {
			if (m_iSkipReseqTable == 2) {
				// Do re-sequence as table is added
				refreshFrameTableList();
			}
			m_iSkipReseqTable = 0;
		}
	}
	
	//relocate the table button after adding tables
	public void refreshFrameTableList() {
		int iChangePosition = 0;
		
		//sort the table list by table name
		Collections.sort(m_oListTable, new ClsTableModeTable());
		
		this.setTableButtonsInfo(iChangePosition);
	}
    
	public void setTableButtonsInfo(int iChangePosition) {
		for(int j = iChangePosition; j < m_oListTable.size(); j++){
			ClsTableModeTable oTableModeTable = m_oListTable.get(j);
			FrameTableButton oFrameTableButton = m_oArrayListTableButton.get(j);
			oFrameTableButton.setTableName(oTableModeTable.getTableName());
			oFrameTableButton.setLabelForegroundColor(oTableModeTable.getForegroundColor());
			
			String sStatus = "";
			for(String sTempColor : m_sTableBackgroundColor.keySet()) {
				if(m_sTableBackgroundColor.get(sTempColor).equals(oTableModeTable.getBackgroundColor())) {
					sStatus = sTempColor;
					break;
				}
			}
			
			String[] sStatusName = new String[AppGlobal.LANGUAGE_COUNT];
			if(PosOutletTable.STATUS_CHECK_PRINTED.equals(sStatus))
				sStatusName = AppGlobal.g_oLang.get()._("print_check", "");
			else if(PosOutletTable.STATUS_NEW_TABLE.equals(sStatus))
				sStatusName = AppGlobal.g_oLang.get()._("vacant", "");
			else if(PosOutletTable.STATUS_OCCUPIED.equals(sStatus))
				sStatusName = AppGlobal.g_oLang.get()._("send_check", "");
			else if(PosOutletTable.STATUS_VACANT.equals(sStatus))
				sStatusName = AppGlobal.g_oLang.get()._("vacant", "");
			else if(PosOutletTable.STATUS_TABLE_RESERVATION.equals(sStatus))
				sStatusName = AppGlobal.g_oLang.get()._("vacant", "");
			
			oFrameTableButton.setTableColor(oTableModeTable.getBackgroundColor(), sStatus, sStatusName);
			oFrameTableButton.setLocked(oTableModeTable.isLocked());
			oFrameTableButton.setPrinted(oTableModeTable.isPrinted());
			if(AppGlobal.g_oFuncStation.get().isAllowShowTableSize()){
				String sTableSize = "x"+oTableModeTable.getTableSize();
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_OPEN_TIME, StringLib.createStringArray(5, sTableSize));
			}else
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_OPEN_TIME, StringLib.createStringArray(5, ""));
			
			JSONObject oSwitchCheckInfo = AppGlobal.g_oFuncStation.get().getSwitchCheckInfoSetting();
			boolean bIsTurnOffAllSwitchCheckInfo = this.isTurnOffAllSwitchCheckInfo(oSwitchCheckInfo);
			
			oFrameTableButton.setTableDetail(FrameTableButton.STATUS_COVER_NO, StringLib.createStringArray(5, ""));
			if(bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_TOTAL).equals("y")))
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_CHECK_TOTAL, StringLib.createStringArray(5, ""));
			if(bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_MEMBER_NUMBER).equals("y")))
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_MEMBER_NUMBER, StringLib.createStringArray(5, ""));
			if(bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_MEMBER_NAME).equals("y")))
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_MEMBER_NAME, StringLib.createStringArray(5, ""));
			if(oSwitchCheckInfo != null ){
				if(oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_INFO_ONE).equals("y"))
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_CHECK_INFO_ONE, StringLib.createStringArray(5, ""));
				if(oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_INFO_TWO).equals("y"))
					oFrameTableButton.setTableDetail(FrameTableButton.STATUS_CHECK_INFO_TWO, StringLib.createStringArray(5, ""));
				if(oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_INFO_THREE).equals("y"))
					oFrameTableButton.setTableDetail(FrameTableButton.STATUS_CHECK_INFO_THREE, StringLib.createStringArray(5, ""));
				if(oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_INFO_FOUR).equals("y"))
					oFrameTableButton.setTableDetail(FrameTableButton.STATUS_CHECK_INFO_FOUR, StringLib.createStringArray(5, ""));
				if(oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_INFO_FIVE).equals("y"))
					oFrameTableButton.setTableDetail(FrameTableButton.STATUS_CHECK_INFO_FIVE, StringLib.createStringArray(5, ""));
			}
			if(oFrameTableButton.isOnlySkipInfo())
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_EMPTY, StringLib.createStringArray(5, ""));
			oFrameTableButton.setClickServerRequestNote(oTableModeTable.getTableNo() + "_" + oTableModeTable.getTableExtension());
			oFrameTableButton.setLongClickServerRequestNote(oTableModeTable.getTableNo() + "_" + oTableModeTable.getTableExtension());
			oFrameTableButton.setTableDetailByIndex(this.getCurrentTableDetailIndex());
		}
	}
	
	public boolean isTableButtonExist(int iTable, String sTableExtension) {
		for (int i = 0; i < m_oListTable.size(); i++) {
			if (m_oListTable.get(i).getTableNo() == iTable && m_oListTable.get(i).getTableExtension().equals(sTableExtension))
				return true;
		}
		return false;
	}
	
	
	//ALL Tables Update Functions
	public void addTableDetailType(String sKey, String sIconURL) {
		for (FrameTableButton oFrameTableButton : m_oArrayListTableButton) {
			oFrameTableButton.addTableDetailType(sKey, sIconURL, false);
		}
	}
	
	public void updateTableDetail(int iTable, String sTableExtension, String sKey, String[] sDetail, boolean bForceIconVisible){
		for(int i=0; i<m_oListTable.size(); i++){
			if (m_oListTable.get(i).getTableNo() == iTable && m_oListTable.get(i).getTableExtension().equalsIgnoreCase(sTableExtension)) {
				m_oArrayListTableButton.get(i).setIconVisible(bForceIconVisible);
				m_oArrayListTableButton.get(i).setTableDetail(sKey, sDetail);
			}
		}
	}
	
	public Integer getCurrentTableDetailIndex(){
		for(FrameTableButton oFrameTableButton:m_oArrayListTableButton){
			return oFrameTableButton.getCurrentTableDetailIndex();
		}
		return 0;
	}
	
	public ArrayList<FrameTableButton> getTableButtonList(){
		return m_oArrayListTableButton;
	}
	
	public void updateTableStatus(int iTable, String sTableExtension, String sTag, String sStatus, int iLockStationId, int iTableExtensionCount){
		// Get Default Status Background Color Codes
		JSONObject oTableStatusBackgroundColor = AppGlobal.g_oFuncStation.get().getTableStatusBackgroundColor();
		
		// Add table button if not exist
		if(m_oListTable.isEmpty() || !isTableButtonExist(iTable, sTableExtension)){
			if(sStatus.equals(PosOutletTable.STATUS_OCCUPIED) || sStatus.equals(PosOutletTable.STATUS_CHECK_PRINTED)
					|| sStatus.equals(PosOutletTable.STATUS_COOKING_OVERTIME) || sStatus.equals(PosOutletTable.STATUS_CLEANING_TABLE)){
				String sForegroundColor = m_sTableForegroundColor.get(PosOutletTable.STATUS_NEW_TABLE);
				String sBackgroundColor = m_sTableBackgroundColor.get(PosOutletTable.STATUS_NEW_TABLE);
				if(sTableExtension.length() == 0){
					sForegroundColor = m_sTableForegroundColor.get(sStatus);
					sBackgroundColor = m_sTableBackgroundColor.get(sStatus);
				}
				String[] sTableName = AppGlobal.g_oFuncOutlet.get().getTableName(Integer.toString(iTable), sTableExtension);
				// *** New table in table mode has no setup in floor plan, so no table size can be provided
				addTable(sTableName, OutFloorPlanTable.SHAPE_RECTANGLE, iTable, sTableExtension, 1, sForegroundColor, sBackgroundColor, 0);
			}
		}
		
		for(int i=0; i<m_oListTable.size(); i++){
			FrameTableButton oFrameTableButton;
			ClsTableModeTable oTableModeTable = m_oListTable.get(i);
			if(oTableModeTable.getTableNo() == iTable){
				if(oTableModeTable.getTableExtension().equalsIgnoreCase(sTableExtension)){
					oFrameTableButton = m_oArrayListTableButton.get(i);
					if (sStatus.equals(PosOutletTable.STATUS_NEW_TABLE)) {
						if (sTag.equals(PosOutletTable.TAG_SEAT_IN)) {
							oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#00A2E8", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_SEAT_IN), PosOutletTable.STATUS_TABLE_RESERVATION, AppGlobal.g_oLang.get()._("vacant", ""));
							oFrameTableButton.setLabelForegroundColor("#FFFFFF");
						} else {
							if(oTableModeTable.getTableType() == 0){
								if(m_sTableForegroundColor.containsKey(sStatus)){
									oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
									oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor(m_sTableBackgroundColor.get(sStatus), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_VACANT), PosOutletTable.STATUS_NEW_TABLE, AppGlobal.g_oLang.get()._("vacant", ""));
								}else
									oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#FFFFFF", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_VACANT), PosOutletTable.STATUS_NEW_TABLE, AppGlobal.g_oLang.get()._("vacant", ""));
							}
							if(oTableModeTable.getTableType() == 1){
								deleteTable(i);
							}
						}
						oFrameTableButton.setPrinted(false);
						oFrameTableButton.setLocked(false);
					} else if (sStatus.equals(PosOutletTable.STATUS_VACANT)) {
						if (sTag.equals(PosOutletTable.TAG_SEAT_IN)) {
							oFrameTableButton.setLabelForegroundColor("#FFFFFF");
							oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#00A2E8", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_SEAT_IN), PosOutletTable.STATUS_TABLE_RESERVATION, AppGlobal.g_oLang.get()._("vacant", ""));
						} else {
							if (oTableModeTable.getTableType() == 0) {
								if (m_sTableForegroundColor.containsKey(sStatus)) {
									oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
									oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor(m_sTableBackgroundColor.get(sStatus), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_VACANT), PosOutletTable.STATUS_VACANT, AppGlobal.g_oLang.get()._("vacant", ""));
								} else 
									oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#FFFFFF", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_VACANT), PosOutletTable.STATUS_VACANT, AppGlobal.g_oLang.get()._("vacant", ""));
							}
							if (oTableModeTable.getTableType() == 1) {
								deleteTable(i);
							}
						}
						oFrameTableButton.setPrinted(false);
						oFrameTableButton.setLocked(false);
					} else if (sStatus.equals(PosOutletTable.STATUS_OCCUPIED)) {
						if (m_sTableForegroundColor.containsKey(sStatus)) {
							oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
							oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor(m_sTableBackgroundColor.get(sStatus), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_OCCUPIED), PosOutletTable.STATUS_OCCUPIED, AppGlobal.g_oLang.get()._("send_check", ""));
						} else
							oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#FF0000", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_OCCUPIED), PosOutletTable.STATUS_OCCUPIED, AppGlobal.g_oLang.get()._("send_check", ""));
						oFrameTableButton.setPrinted(false);
						oFrameTableButton.setLocked(false);
						
					} else if (sStatus.equals(PosOutletTable.STATUS_CHECK_PRINTED)) {
						if (m_sTableForegroundColor.containsKey(sStatus)) {
							oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
							oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor(m_sTableBackgroundColor.get(sStatus), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_PRINTED), PosOutletTable.STATUS_CHECK_PRINTED, AppGlobal.g_oLang.get()._("print_check", ""));
						} else
							oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#00FF00", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_PRINTED), PosOutletTable.STATUS_CHECK_PRINTED, AppGlobal.g_oLang.get()._("print_check", ""));
						oFrameTableButton.setPrinted(true);
						oFrameTableButton.setLocked(false);
					} else if (sStatus.equals(PosOutletTable.STATUS_COOKING_OVERTIME)) {
						JSONObject oTempJSONObject = AppGlobal.g_oFuncStation.get().getTableFloorPlanSetting();
						if (oTempJSONObject != null && oTempJSONObject.optString("support_cooking_overtime").equals("y")) {
							String sColorCode = oTempJSONObject.optString("cooking_overtime_status_color", "");
							oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
							if (!sColorCode.isEmpty())
								oFrameTableButton.setTableColor(sColorCode, PosOutletTable.STATUS_COOKING_OVERTIME, AppGlobal.g_oLang.get()._("cooking_overtime", ""));
							else if (m_sTableForegroundColor.containsKey(sStatus))
								oFrameTableButton.setTableColor(m_sTableBackgroundColor.get(sStatus), PosOutletTable.STATUS_COOKING_OVERTIME, AppGlobal.g_oLang.get()._("cooking_overtime", ""));
							else
								oFrameTableButton.setTableColor("#FF8100", PosOutletTable.STATUS_COOKING_OVERTIME, AppGlobal.g_oLang.get()._("cooking_overtime", ""));
							oFrameTableButton.setPrinted(true);
							oFrameTableButton.setLocked(false);
						}
					} else if (sStatus.equals(PosOutletTable.STATUS_CLEANING_TABLE)) {
						JSONObject oTempJSONObject = AppGlobal.g_oFuncStation.get().getTableFloorPlanSetting();
						if (oTempJSONObject != null && oTempJSONObject.optString("support_table_status_cleaning").equals("y")) {
							oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
							if (m_sTableForegroundColor.containsKey(sStatus))
								oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor(m_sTableBackgroundColor.get(sStatus), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_CLEANING), PosOutletTable.STATUS_CLEANING_TABLE, AppGlobal.g_oLang.get()._("cleaning", ""));
							else
								oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#6600CC", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_CLEANING), PosOutletTable.STATUS_CLEANING_TABLE, AppGlobal.g_oLang.get()._("cleaning", ""));
							oFrameTableButton.setPrinted(false);
							oFrameTableButton.setLocked(false);
						}
					}
					
					if (iLockStationId > 0) {
						oFrameTableButton.setLocked(true);
					}
					
					oTableModeTable.setForegroundColor(oFrameTableButton.getLabelForegroundColor());
					oTableModeTable.setBackgroundColor(oFrameTableButton.getTableColor());
					oTableModeTable.setLocked(oFrameTableButton.isLocked());
					oTableModeTable.setPrinted(oFrameTableButton.isPrinted());
				}
			}
		}
	}
	
	public void toggleTableDetail() {
		int iCurrentDetailIndex = getCurrentTableDetailIndex();
		iCurrentDetailIndex++;
		for(FrameTableButton oTableButton:m_oArrayListTableButton) {
			if(oTableButton.getShowFUllDetail()) {
				iCurrentDetailIndex = oTableButton.getCorrectNextIndex(iCurrentDetailIndex);
			}
				oTableButton.setTableDetailByIndex(iCurrentDetailIndex);
		}
	}
	
	public boolean isTurnOffAllSwitchCheckInfo(JSONObject oSwitchCheckInfo) {
		if (oSwitchCheckInfo == null)
			return true;
		Iterator<String> keys = oSwitchCheckInfo.keys();
		while(keys.hasNext()){
			String sKey = keys.next();
			try {
				if(oSwitchCheckInfo.get(sKey).equals("y"))
					return false;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public String getTableStatusBackgroundColor(String sOriginalColor, JSONObject oTableStatusBackgroundColor, String sStatus) {
		String sTableStatusBgColor = sOriginalColor;
		if (oTableStatusBackgroundColor == null)
			return sTableStatusBgColor;
		
		if(oTableStatusBackgroundColor.has(sStatus) && !oTableStatusBackgroundColor.optString(sStatus).isEmpty())
			sTableStatusBgColor = "#"+oTableStatusBackgroundColor.optString(sStatus);
		
		return sTableStatusBgColor;
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		return bMatchChild;
	}
	
	@Override
	public void frameTableFloorPlan_TableClicked(String sTable, String sTableExtension) {
		for (FrameTableModeListener listener : listeners) {
			// Raise the event to parent
			listener.frameTableFloorPlan_TableClicked(sTable, sTableExtension);
		}
	}
	
	@Override
	public void frameTableFloorPlan_TableLongClicked(String sTable, String sTableExtension) {
		for (FrameTableModeListener listener : listeners) {
			// Raise the event to parent
			listener.frameTableFloorPlan_TableLongClicked(sTable, sTableExtension);
		}
	}
}
