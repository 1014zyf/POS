package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import om.OutMediaObject;

import org.json.JSONException;
import org.json.JSONObject;

import externallib.StringLib;
import om.OutFloorPlanTable;
import om.PosOutletTable;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;

/** interface for the listeners/observers callback method */
interface FrameTableFloorPlanListener {
	boolean frameTableFloorPlan_TableClicked(String sTableNo, String sTableExtension, boolean bShowTableMode);
	void frameTableFloorPlan_TableLongClicked(String sTableNo, String sTableExtension);
	void frameTableFloorPlan_ActionCancelClicked();
	void frameTableFloorPlan_Refresh();
	void frameTableFloorPlan_CheckPeriodFloorPlan();
	void frameTableFloorPlan_ToggleOrderingModeClicked();
	void frameTableFloorPlan_TimeInOut();
	void frameTableFloorPlan_ShowAskTableScreen();
	void frameTableFloorPlan_FunctionBarOpenPanelClicked();
	void frameTableFloorPlan_FunctionBarButtonClicked(String sNote);
	void frameTableFloorPlan_MenuModeNewOrder(int iDefaultTableNo);
	void frameTableFloorPlan_MenuModeChangeLanguage();
	void frameTableFloorPlan_MenuModeCheckReview(int iDefaultTableNo);
	void frameTableFloorPlan_SwipeLeft();
	void frameTableFloorPlan_SwipeRight();
	void frameTableFloorPlan_HandleLogout();
}

public class FrameTableFloorPlan extends VirtualUIFrame implements FrameTableButtonListener, FrameTableModeListener, FrameTableFloorPlanFunctionBarListener, FrameMenuModeListener, FrameCommonPageContainerListener {
	TemplateBuilder m_oTemplateBuilder;
	
	class clsFloorPlanMap {
		double dWidthRatio;
		double dHeightRatio;
		VirtualUIFrame oFrameFloorPlanMap;
	}
	
	private ArrayList<clsFloorPlanMap> m_oFrameFloorPlanMapList;
	
	private ArrayList<VirtualUIButton> m_oButtonMapTabList;
	private ArrayList<String[]> m_sFloorDesc;
	private ArrayList<VirtualUILabel> m_oLabelFloorList;
	
	private HashMap<Integer, VirtualUIImage> m_oImageMapIndicatorList;
	
	private HashMap<Integer, HashMap<String, FrameTableButton>> m_oButtonMapTableList;
	private ArrayList<VirtualUIImage> m_oImageFloorBackGroundList;
	private int m_iCurrentMapIndex;
	private VirtualUIFrame m_oFrameFloorPlanHeader;
	private VirtualUIFrame m_oFrameMapIndicator;
	private VirtualUIFrame m_oScreenSaver;
	private VirtualUIImage m_oScreenSaverImage;
	private VirtualUIButton m_oButtonActionCancel;
	private VirtualUIFrame m_oFrameActionCancel;
	private VirtualUILabel m_oLabelActionCancel;
	private VirtualUILabel m_oLabelActionDesc;
	private VirtualUIList m_oListFloor;
	private VirtualUIImage m_oListFloorBackground;
	private VirtualUIImage m_oImageActionIcon;

	private VirtualUIFrame m_oFrameSwitchInfo;
	private VirtualUILabel m_oLabelSwitchInfo;
	private VirtualUIImage m_oImageSwitchInfo;
	private VirtualUIFrame m_oFrameCardView;
	private VirtualUILabel m_oLabelCardView;
	private VirtualUIImage m_oImageCardView;
	private VirtualUIFrame m_oFrameTableView;
	private VirtualUILabel m_oLabelTableView;
	private VirtualUIImage m_oImageTableView;
	
	private FrameTableFloorPlanFunctionBar m_oFloorPlanFunctionBar;
	
	private boolean m_bOpenFunctionPanel;
	private boolean m_bFunctionBarExist;
	private boolean m_bFunctionBarButtonClick;

	private HashMap<String, String> m_sTableForegroundColor;
	private HashMap<String, String> m_sTableBackgroundColor;

	private ArrayList<FrameTableMode> m_oFrameTableModeList;
	private boolean m_bShowTableMode;

	private String m_sLateUpdateTime = "";

	private FrameMenuMode m_oFrameMenuMode;

	private String m_sSwitchUserMode;
	
	private FrameCommonPageContainer m_oCommonPageContainer;
	
	private int m_iIdleTimeOut;
	
	public static final String SWITCH_USER_MODE_ON = "s";
	public static final String SWITCH_USER_MODE_OFF = "o";
	public static final String SWITCH_USER_MODE_WITH_LOGIN = "l";
	
	private final static String TIMER_UPDATE_TABLE_STATUS = "update_table_status";
	private final static String TIMER_CHECK_PERIOD_FLOOR_PLAN = "check_period_floor_plan";
	private final static String TIMER_CHECK_TABLE_STATUS_CLEANING = "check_table_status_cleaning";
	private final static String TIMER_SCREEN_SAVER = "screen_saver";
	private final static String IDLE_TIME_LOGOUT = "idle_time_logout";
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameTableFloorPlanListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameTableFloorPlanListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameTableFloorPlanListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public void init(List<FuncLookupButtonInfo> oData, int iTotalPageNum){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameTableFloorPlanListener>();
		m_oFrameFloorPlanMapList = new ArrayList<clsFloorPlanMap>();
		m_oButtonMapTabList = new ArrayList<VirtualUIButton>();
		m_sFloorDesc = new ArrayList<String[]>();
		m_oLabelFloorList = new ArrayList<VirtualUILabel>();
		m_oImageMapIndicatorList = new HashMap<Integer, VirtualUIImage>();
		m_oButtonMapTableList = new HashMap<Integer, HashMap<String, FrameTableButton>>();
		m_sTableForegroundColor = new HashMap<String, String>();
		m_sTableBackgroundColor = new HashMap<String, String>();
		m_oFrameTableModeList = new ArrayList<FrameTableMode>();
		m_oImageFloorBackGroundList = new ArrayList<VirtualUIImage>();
		m_sLateUpdateTime = "";
		m_bOpenFunctionPanel = false;
		m_bFunctionBarButtonClick = false;
		m_bShowTableMode = false;
		m_sSwitchUserMode = FrameTableFloorPlan.SWITCH_USER_MODE_OFF;
		
		m_iIdleTimeOut = AppGlobal.g_oFuncStation.get().getIdleTimeLogout(AppGlobal.g_oFuncUser.get().getUserGroupList());
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraTableFloorPlan.xml");
		
		// Header
		m_oFrameFloorPlanHeader = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameFloorPlanHeader, "fraFloorPlanHeader");
		m_oFrameFloorPlanHeader.setWidth(this.getWidth());
		m_oFrameFloorPlanHeader.setTop(this.getHeight()-m_oFrameFloorPlanHeader.getHeight());
		this.attachChild(m_oFrameFloorPlanHeader);
		
		m_oCommonPageContainer = new FrameCommonPageContainer();
		m_oTemplateBuilder.buildFrame(m_oCommonPageContainer, "fraFloorPlanContainer");
		
		m_oCommonPageContainer.init(this.getWidth(), this.getHeight()- m_oFrameFloorPlanHeader.getHeight(), 120, 64, 4, "#0055B8", "#ABABAB", "#00F0FBFF", "#00F0FBFF", 112, false, false);
		m_oCommonPageContainer.setTop(0);
		m_oCommonPageContainer.addListener(this);
		m_oCommonPageContainer.setVisible(true);
		this.attachChild(m_oCommonPageContainer);
		
		// Map indicator frame
		m_oFrameMapIndicator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameMapIndicator, "fraMapIndicator");
		m_oFrameMapIndicator.setTop(m_oFrameFloorPlanHeader.getTop() + (m_oFrameFloorPlanHeader.getHeight() - 17) / 2);
		m_oFrameMapIndicator.setHeight((m_oFrameFloorPlanHeader.getHeight()));
		m_oFrameMapIndicator.setWidth(m_oFrameFloorPlanHeader.getWidth());
		this.attachChild(m_oFrameMapIndicator);
		
		// Floor List selection box
		m_oListFloor = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListFloor, "lstFloor");
		m_oListFloor.setVisible(false);
		this.attachChild(m_oListFloor);
		
		// Floor list selection box background
		m_oListFloorBackground = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oListFloorBackground, "imgFloor");
		m_oListFloorBackground.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/backgrounds/floor_list_background.png");
		m_oListFloorBackground.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_TO_FIT);
		m_oListFloorBackground.setVisible(false);
		this.attachChild(m_oListFloorBackground);
		
		// Table View
		m_oFrameTableView = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTableView, "frmTableView");
		m_oFrameTableView.allowClick(true);
		m_oFrameFloorPlanHeader.attachChild(m_oFrameTableView);
		
		m_oImageTableView = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageTableView, "ImgTableView");
		m_oImageTableView.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_main_tables.png");
		m_oImageTableView.allowClick(true);
		m_oFrameTableView.attachChild(m_oImageTableView);
		
		m_oLabelTableView = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableView, "lblTableView");
		m_oLabelTableView.setValue(AppGlobal.g_oLang.get()._("table_view", ""));
		m_oLabelTableView.allowClick(true);
		m_oFrameTableView.attachChild(m_oLabelTableView);
		
		VirtualUIFrame oFrameWhiteCol = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameWhiteCol, "fraWhiteCol");
		oFrameWhiteCol.setLeft(m_oLabelTableView.getLeft() + m_oLabelTableView.getWidth());
		m_oFrameTableView.attachChild(oFrameWhiteCol);
		
		// Card View
		m_oFrameCardView = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCardView, "frmCardView");
		m_oFrameCardView.allowClick(true);
		m_oFrameFloorPlanHeader.attachChild(m_oFrameCardView);
		
		m_oImageCardView = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageCardView, "ImgCardView");
		m_oImageCardView.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_main_cards.png");
		m_oImageCardView.allowClick(true);
		m_oFrameCardView.attachChild(m_oImageCardView);
		
		m_oLabelCardView = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCardView, "lblCardView");
		m_oLabelCardView.setValue(AppGlobal.g_oLang.get()._("card_view", ""));
		m_oLabelCardView.allowClick(true);
		m_oFrameCardView.attachChild(m_oLabelCardView);
		
		oFrameWhiteCol = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameWhiteCol, "fraWhiteCol");
		oFrameWhiteCol.setLeft(m_oLabelCardView.getLeft() + m_oLabelCardView.getWidth());
		m_oFrameCardView.attachChild(oFrameWhiteCol);
		
		// Switch Info
		m_oFrameSwitchInfo = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameSwitchInfo, "frmSwitchInfo");
		m_oFrameSwitchInfo.allowClick(true);
		m_oFrameFloorPlanHeader.attachChild(m_oFrameSwitchInfo);
		
		m_oImageSwitchInfo = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageSwitchInfo, "ImgSwitchInfo");
		m_oImageSwitchInfo.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_main_switch.png");
		m_oImageSwitchInfo.allowClick(true);
		m_oFrameSwitchInfo.attachChild(m_oImageSwitchInfo);
		
		m_oLabelSwitchInfo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSwitchInfo, "lblSwitchInfo");
		m_oLabelSwitchInfo.setValue(AppGlobal.g_oLang.get()._("switch_info", ""));
		m_oLabelSwitchInfo.allowClick(true);
		m_oFrameSwitchInfo.attachChild(m_oLabelSwitchInfo);
		
		// Action cancel button
		m_oButtonActionCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonActionCancel, "butActionCancel");
		m_oButtonActionCancel.setValue(AppGlobal.g_oLang.get()._("cancel", ""));
		m_oButtonActionCancel.setTop(m_oFrameFloorPlanHeader.getTop() + 5);
		m_oButtonActionCancel.setHeight(m_oFrameFloorPlanHeader.getHeight() - 10);
		this.attachChild(m_oButtonActionCancel);
		m_oButtonActionCancel.setVisible(false);
		
		// Action cancel button frame
		m_oFrameActionCancel = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameActionCancel, "fraActionCancel");
		m_oFrameActionCancel.setTop(m_oFrameFloorPlanHeader.getTop() + 5);
		m_oFrameActionCancel.setHeight(m_oFrameFloorPlanHeader.getHeight() - 10);
		m_oFrameActionCancel.allowClick(true);
		this.attachChild(m_oFrameActionCancel);
		m_oFrameActionCancel.setVisible(false);
		
		// Action cancel button label
		m_oLabelActionCancel =  new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelActionCancel, "lblActionCancel");
		m_oLabelActionCancel.setHeight(m_oFrameActionCancel.getHeight());
		m_oLabelActionCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		m_oFrameActionCancel.attachChild(m_oLabelActionCancel);
		
		// Action icon
		m_oImageActionIcon = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageActionIcon, "imgActionIcon");
		m_oImageActionIcon.setTop(m_oFrameFloorPlanHeader.getTop() + 7);
		m_oImageActionIcon.setEnabled(true);
		m_oImageActionIcon.allowClick(false);
		this.attachChild(m_oImageActionIcon);
		m_oImageActionIcon.setVisible(false);
		
		// Action description
		m_oLabelActionDesc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelActionDesc, "lblActionDesc");
		m_oLabelActionDesc.setTop(m_oFrameFloorPlanHeader.getTop() + 5);
		m_oLabelActionDesc.setLeft(m_oImageActionIcon.getLeft() + m_oImageActionIcon.getWidth() + 5);
		m_oLabelActionDesc.setHeight(m_oFrameFloorPlanHeader.getHeight() - 10);
		this.attachChild(m_oLabelActionDesc);
		m_oLabelActionDesc.setVisible(false);
		
		// Get Default Status Background Color Codes
		JSONObject oTableStatusBackgroundColor = AppGlobal.g_oFuncStation.get().getTableStatusBackgroundColor();
		
		// Get table color
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
		
		int iOpenTableScreenMode = AppGlobal.g_oFuncStation.get().getOpenTableScreenModeConfig();
		switch(iOpenTableScreenMode) {
			case 1: 
				m_oLabelTableView.setForegroundColor("#0055B8");
				m_oImageTableView.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_main_tables_active.png");
				break;
			case 2:
				m_oLabelCardView.setForegroundColor("#0055B8");
				m_oImageCardView.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_main_cards_active.png");
				break;
			case 3:	
				break;
			default:
				m_oLabelTableView.setForegroundColor("#0055B8");
				m_oImageTableView.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_main_tables_active.png");
				break;
		}
		
		if(oData != null) { // Function Panel of floor plan is set
			m_bFunctionBarExist = true;
			// Function Bar
			m_oFloorPlanFunctionBar = new FrameTableFloorPlanFunctionBar();
			m_oTemplateBuilder.buildFrame(m_oFloorPlanFunctionBar, "fraTableFloorPlanFunctionBar");
			m_oFloorPlanFunctionBar.setTop(m_oFrameFloorPlanHeader.getTop());
			m_oFloorPlanFunctionBar.setHeight(m_oFrameFloorPlanHeader.getHeight());
			m_oFloorPlanFunctionBar.init(oData, iTotalPageNum);
			m_oFloorPlanFunctionBar.addListener(this);
			this.attachChild(m_oFloorPlanFunctionBar);

		}
		
		// Screen Saver Screen
		m_oScreenSaver = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oScreenSaver, "fraScreenSaver");
		m_oScreenSaver.setVisible(false);
		m_oScreenSaver.allowClick(true);
		m_oScreenSaver.setBackgroundColor(AppGlobal.g_oFuncStation.get().getScreenSaverColor());
		this.attachChild(m_oScreenSaver);
		if (AppGlobal.g_oFuncStation.get().getScreenSaverTimeout() > 0)
			this.addScreenSaverTimer();
		
		// Screen Saver Screen
		m_oScreenSaverImage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oScreenSaverImage, "fraScreenSaverImage");
		m_oScreenSaverImage.setVisible(false);
		m_oScreenSaverImage.allowClick(true);
		m_oScreenSaverImage.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_ASPECT_FIT_CENTER);
		m_oScreenSaverImage.setSource(AppGlobal.g_oFuncOutlet.get().getMediaUrl(OutMediaObject.USED_FOR_PHOTO_GALLERY));
		this.attachChild(m_oScreenSaverImage);
		
		// Create thread to handle table status update
		this.addUpdateTableStatusTimer();
		addIdleTimeLogoutTimer();
		// Create thread to check if table floor plan should be changed
		this.addCheckPeriodFloorPlanTimer();
		
		if (AppGlobal.g_oFuncStation.get().getAutomaticallyChangeCleaningToVacantInterval() > 0) {
			// Create thread to handle check table status cleaning
			this.addCheckTableStatusCleaningTimer();
		}
	}
	
	// Add table floor plan map
	public void addMap(String[] sName, int iWidth, int iHeight, String sImageSource){
		// Create Panel Name button
		VirtualUIButton oButtonMapTap = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(oButtonMapTap, "butMapTab");
		oButtonMapTap.setTop(m_oFrameFloorPlanHeader.getTop() + 5);
		oButtonMapTap.setLeft(10+m_oButtonMapTabList.size()*(oButtonMapTap.getWidth()+10));
		oButtonMapTap.setValue(sName);
		oButtonMapTap.setVisible(true);
		
		// Add PanelName Listener
		this.attachChild(oButtonMapTap);
		m_oButtonMapTabList.add(oButtonMapTap);
		
		// Create a new frame
		// *** The frame top, left, width, height are calculated at CommonPageContainer class
		VirtualUIFrame oFrameFloorPlanMap = new VirtualUIFrame();
		oFrameFloorPlanMap.setExist(true);
		oFrameFloorPlanMap.allowSwipeLeft(true);
		oFrameFloorPlanMap.allowSwipeRight(true);
		oFrameFloorPlanMap.setSwipeLeftServerRequestBlockUI(false);
		oFrameFloorPlanMap.setSwipeRightServerRequestBlockUI(false);
		oFrameFloorPlanMap.setVisible(true);
		
		// Add DisplayPanel Listener
		m_oCommonPageContainer.addButton(sName, oFrameFloorPlanMap);
		
		// Load floor plan picture
		VirtualUIImage oImageFloorPlanMap = new VirtualUIImage();
		oImageFloorPlanMap.setExist(true);
		oImageFloorPlanMap.setWidth(oFrameFloorPlanMap.getWidth());
		oImageFloorPlanMap.setHeight(oFrameFloorPlanMap.getHeight());
		oImageFloorPlanMap.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_TO_FIT);
		oImageFloorPlanMap.setSource(sImageSource);
		oFrameFloorPlanMap.attachChild(oImageFloorPlanMap);
		
		m_oImageFloorBackGroundList.add(oImageFloorPlanMap);
		
		clsFloorPlanMap oFloorPlanMap = new clsFloorPlanMap();
		oFloorPlanMap.dWidthRatio = (double) oFrameFloorPlanMap.getWidth() / (double) iWidth;
		oFloorPlanMap.dHeightRatio = (double) oFrameFloorPlanMap.getHeight() / (double) iHeight;
		oFloorPlanMap.oFrameFloorPlanMap = oFrameFloorPlanMap;
		m_oFrameFloorPlanMapList.add(oFloorPlanMap);
		
		// Create table mode screen
		FrameTableMode oFrameTableMode = new FrameTableMode();
		
		oFrameTableMode.setExist(true);
		
		oFrameTableMode.setHeight(oFrameFloorPlanMap.getHeight());
		oFrameTableMode.setWidth(oFrameFloorPlanMap.getWidth());
		oFrameTableMode.setVisible(false);
		oFrameTableMode.allowClick(false);
		oFrameTableMode.init();
		oFrameTableMode.setFrameTableNum(AppGlobal.g_oFuncStation.get().getTableModeRowColumn("column"));
		oFrameTableMode.addListener(this);
		oFrameFloorPlanMap.attachChild(oFrameTableMode);
		
		m_oFrameTableModeList.add(oFrameTableMode);
		
		// Create the table list
		m_oButtonMapTableList.put(m_oButtonMapTabList.size(), new HashMap<String, FrameTableButton>());
		
		// Add Desc to array list
		m_sFloorDesc.add(sName);
	}
	
	// Remove all table floor plan map
	public void removeAllMap(){
		//remove table map's button
		for(HashMap<String, FrameTableButton> oButtonMapTableList:m_oButtonMapTableList.values()) {
			for(FrameTableButton oTableButton:oButtonMapTableList.values())
				this.removeChild(oTableButton.getId());
		}
		
		for(VirtualUILabel oUILabel:m_oLabelFloorList)
			m_oListFloor.removeChild(oUILabel.getId());
		
		//remove map
		for(VirtualUIButton oButton:m_oButtonMapTabList) 
			this.removeChild(oButton.getId());
		
		for(clsFloorPlanMap oFloorPlanMap:m_oFrameFloorPlanMapList) {
			VirtualUIFrame oFrame = oFloorPlanMap.oFrameFloorPlanMap;
			this.removeChild(oFrame.getId());
		}
		
		// Remove map indicator
		for(VirtualUIImage oMapIndicator:m_oImageMapIndicatorList.values())
			this.m_oFrameMapIndicator.removeChild(oMapIndicator.getId());
		
		// Remove floor list
		m_oListFloor.removeAllChildren();
		
		m_oFrameFloorPlanMapList.clear();
		m_oButtonMapTabList.clear();
		m_oButtonMapTableList.clear();
		m_sFloorDesc.clear();
		m_oLabelFloorList.clear();
		m_oImageMapIndicatorList.clear();
	}
	
	// Remove table floor plan map
	public void removeMap(int iMapIdx){
	
	}
	
	// Show floor plan action UI
	public void showFloorActionUI(String sIconSource, String[] sDesc) {
		if(m_bFunctionBarExist && m_oFloorPlanFunctionBar != null) 
			m_oFloorPlanFunctionBar.setVisible(false);
		
		m_oImageActionIcon.setSource(sIconSource);
		m_oImageActionIcon.setVisible(true);
		m_oLabelActionDesc.setValue(sDesc);
		m_oLabelActionDesc.setVisible(true);
		m_oButtonActionCancel.setVisible(true);
		m_oFrameActionCancel.setVisible(true);
	}
	
	// Show floor plan function button
	public void showFloorPlanFunctionButton() {
		m_oImageActionIcon.setVisible(false);
		m_oLabelActionDesc.setVisible(false);
		m_oButtonActionCancel.setVisible(false);
		m_oFrameActionCancel.setVisible(false);
		
		if(m_bFunctionBarExist && m_oFloorPlanFunctionBar != null)
			m_oFloorPlanFunctionBar.setVisible(true);
	}
	
	
	public clsFloorPlanMap getMap(int iMapIdx){
		int i;
		//Loop all panel frames
		i = 1;
		for(clsFloorPlanMap oFloorPlanMap : m_oFrameFloorPlanMapList) {
			if(i == iMapIdx)
				return oFloorPlanMap;
			
			i++;
		}
		return null;
	}
	
	public int getMapCount() {
		return m_oFrameFloorPlanMapList.size();
	}
	
	public void createMenuModeStartScreen(int iDefaultTableNo){
		m_oFrameMenuMode = new FrameMenuMode(iDefaultTableNo);
		m_oFrameMenuMode.setExist(true);
		m_oFrameMenuMode.setTop(0);
		m_oFrameMenuMode.setLeft(0);
		m_oFrameMenuMode.setHeight(this.getHeight());
		m_oFrameMenuMode.setWidth(this.getWidth());
		m_oFrameMenuMode.addListener(this);
		m_oFrameMenuMode.setVisible(true);
		this.attachChild(m_oFrameMenuMode);
		m_oFrameMenuMode.bringToTop();
	}
	
	public FrameTableMode getTableListFrame(int iTableListIdx){
		int i=1;
		for(FrameTableMode oFrameTableMode : m_oFrameTableModeList){
			if(i == iTableListIdx){
				return oFrameTableMode;
			}
			i++;
		}
		return null;
	}
	
	public int getTabButtonHeight() {
		return m_oCommonPageContainer.getCurrentButtonHeight();
	}
	
	public void addTable(int iMapIdx, int iTop, int iLeft, int iWidth, int iHeight, String[] sName, String sTableStyle, int iTable, String sTableExtension, int iTableSize) {
		// Get the page frame
		clsFloorPlanMap oFloorPlanMap = getMap(iMapIdx);
		VirtualUIFrame oFrameFloorPlanMap = oFloorPlanMap.oFrameFloorPlanMap;
		
		double dWidthRatio = oFloorPlanMap.dWidthRatio;
		double dHeightRatio = oFloorPlanMap.dHeightRatio;
		
		// Get the table list under this page
		HashMap<String, FrameTableButton> oTableList = m_oButtonMapTableList.get(iMapIdx);
		
		FrameTableButton oFrameTableButton = new FrameTableButton();
		oFrameTableButton.setExist(true);
		oFrameTableButton.setTop((int) ((iTop + 4) * dHeightRatio));
		oFrameTableButton.setLeft((int) ((iLeft + 4) * dWidthRatio));
		oFrameTableButton.setWidth((int) (iWidth * dWidthRatio));
		oFrameTableButton.setHeight((int) (iHeight * dHeightRatio));
		String[] sDisplayName = Arrays.copyOfRange(sName, 0, sName.length);
		for (int i=0; i<sDisplayName.length; i++) {
			if (sDisplayName[i].length() == 0) {
				if(iTable == 0){
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
		oFrameTableButton.addListener(this);
		
		if(m_sTableForegroundColor.containsKey(PosOutletTable.STATUS_NEW_TABLE)){
			oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(PosOutletTable.STATUS_NEW_TABLE));
			oFrameTableButton.setTableColor(m_sTableBackgroundColor.get(PosOutletTable.STATUS_NEW_TABLE), PosOutletTable.STATUS_NEW_TABLE, AppGlobal.g_oLang.get()._("vacant", ""));
		}else{
			oFrameTableButton.setTableColor("#FFFFFF", PosOutletTable.STATUS_NEW_TABLE, AppGlobal.g_oLang.get()._("vacant", ""));
		}
		
		// Assign table size to default detail if set
		String sTableSize = "x"+iTableSize;
		if(AppGlobal.g_oFuncStation.get().isAllowShowTableSize()){
			oFrameTableButton.setDefaultTableDetail(StringLib.createStringArray(5, sTableSize));
		}
		
		oFrameFloorPlanMap.attachChild(oFrameTableButton);
		
		// Add table to the table list under this page
		oTableList.put(iTable + "_" + sTableExtension, oFrameTableButton);
		
		// Add table to table mode
		FrameTableMode oFrameTableMode = getTableListFrame(iMapIdx);
		oFrameTableMode.addTable(sName, OutFloorPlanTable.SHAPE_RECTANGLE, iTable, sTableExtension, 0, oFrameTableButton.getLabelForegroundColor(), oFrameTableButton.getTableColor(), iTableSize);
		
		// If table cover limit is shown, add all type of table detail for vacant tables
		if(AppGlobal.g_oFuncStation.get().isAllowShowTableSize()){
			oFrameTableButton.setIconVisible(false);
			JSONObject oSwitchCheckInfo = AppGlobal.g_oFuncStation.get().getSwitchCheckInfoSetting();
			boolean bIsTurnOffAllSwitchCheckInfo = this.isTurnOffAllSwitchCheckInfo(oSwitchCheckInfo);
			if(bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_OPEN_TIME).equals("y"))){
				oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_OPEN_TIME, AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/floor_check_open_time.png", false);
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_OPEN_TIME, StringLib.createStringArray(5, sTableSize));
			}
			if(bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_COVER_NO).equals("y"))){
				oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_COVER_NO, AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/floor_cover_no.png", false);
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_COVER_NO, StringLib.createStringArray(5, sTableSize));
			}
			if(bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_TOTAL).equals("y"))){
				oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_CHECK_TOTAL, "", false);
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_CHECK_TOTAL, StringLib.createStringArray(5, sTableSize));
			}
			if(bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_MEMBER_NUMBER).equals("y"))){
				oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_MEMBER_NUMBER, "", false);
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_MEMBER_NUMBER, StringLib.createStringArray(5, sTableSize));
			}
			if(bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_MEMBER_NAME).equals("y"))){
				oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_MEMBER_NAME, "", false);
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_MEMBER_NAME, StringLib.createStringArray(5, sTableSize));
			}
			
			if(bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_OWNER_NAME).equals("y"))){
				oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_OWNER_NAME, "", false);
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_OWNER_NAME, StringLib.createStringArray(5, sTableSize));
			}
			
			oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_TABLE_SIZE, "", false);
			oFrameTableButton.setTableDetail(FrameTableButton.STATUS_TABLE_SIZE, StringLib.createStringArray(5, sTableSize));
			
			if(oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_INFO_ONE).equals("y")){
				oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_CHECK_INFO_ONE, "", false);
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_CHECK_INFO_ONE, StringLib.createStringArray(5, sTableSize));
			}
			if(oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_INFO_TWO).equals("y")){
				oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_CHECK_INFO_TWO, "", false);
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_CHECK_INFO_TWO, StringLib.createStringArray(5, sTableSize));
			}
			if(oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_INFO_THREE).equals("y")){
				oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_CHECK_INFO_THREE, "", false);
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_CHECK_INFO_THREE, StringLib.createStringArray(5, sTableSize));
			}
			if(oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_INFO_FOUR).equals("y")){
				oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_CHECK_INFO_FOUR, "", false);
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_CHECK_INFO_FOUR, StringLib.createStringArray(5, sTableSize));
			}
			if(oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_INFO_FIVE).equals("y")){
				oFrameTableButton.addTableDetailType(FrameTableButton.STATUS_CHECK_INFO_FIVE, "", false);
				oFrameTableButton.setTableDetail(FrameTableButton.STATUS_CHECK_INFO_FIVE, StringLib.createStringArray(5, sTableSize));
			}
		}
		
		oFrameTableButton.setTableDetailByIndex(this.getCurrentTableDetailIndex());
	}
	
	public void addTableDetailType(String sKey, String sIconURL){
		for(HashMap<String, FrameTableButton> oButtonMapTableList:m_oButtonMapTableList.values()) {
			for(FrameTableButton oFrameTableButton:oButtonMapTableList.values()){
				oFrameTableButton.addTableDetailType(sKey, sIconURL, false);
			}
		}
	}
	
	private void updateTableDetail(int iTable, String sTableExtension, String sKey, String[] sDetail, boolean bForceIconVisible){
		FrameTableButton oFrameTableButton;

		for(int i=0; i<m_oButtonMapTableList.size(); i++){
			HashMap<String, FrameTableButton> oTableList = m_oButtonMapTableList.get(i+1);
			if(oTableList.containsKey(iTable + "_" + sTableExtension)){
				oFrameTableButton = oTableList.get(iTable + "_" + sTableExtension);
				// force to set icon visible status
				if(AppGlobal.g_oFuncStation.get().isAllowShowTableSize())
					oFrameTableButton.setIconVisible(bForceIconVisible);
				oFrameTableButton.setTableDetail(sKey, sDetail);
			}
		}
		
		for(FrameTableMode oFrameTableMode: m_oFrameTableModeList){
			oFrameTableMode.updateTableDetail(iTable, sTableExtension, sKey, sDetail, bForceIconVisible);
		}
	}
	
	// update open time, cover no, check total, svc member label of each table button
	public void updateTableDetails(int iTable, String sTableExtension, String sOpenTime, String sGuest, String sCheckTotal, String sMemNo, String sMemName, String [] sCheckUserName, LinkedHashMap<String, String> oCheckInfoList) {
		JSONObject oSwitchCheckInfo = AppGlobal.g_oFuncStation.get().getSwitchCheckInfoSetting();
		boolean bIsTurnOffAllSwitchCheckInfo = this.isTurnOffAllSwitchCheckInfo(oSwitchCheckInfo);
		
		//Assign the icon visible status by checking the  label value of check total and guest number
		if(AppGlobal.g_oFuncStation.get().isAllowShowTableSize()){
			updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_TABLE_SIZE, null, true);
		}
		
		updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_COVER_NO, StringLib.createStringArray(5, sGuest), true);
		if(bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_OPEN_TIME).equals("y")))
			updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_OPEN_TIME, StringLib.createStringArray(5, sOpenTime), true);
		if(bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_CHECK_TOTAL).equals("y")))
			updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_CHECK_TOTAL, StringLib.createStringArray(5, sCheckTotal), true);
		if(bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_MEMBER_NUMBER).equals("y")))
			updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_MEMBER_NUMBER, StringLib.createStringArray(5, sMemNo), true);
		if(bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_MEMBER_NAME).equals("y")))
			updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_MEMBER_NAME, StringLib.createStringArray(5, sMemName), true);
		if(bIsTurnOffAllSwitchCheckInfo || (oSwitchCheckInfo != null && oSwitchCheckInfo.optString(FrameTableButton.STATUS_OWNER_NAME).equals("y")))
			updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_OWNER_NAME, sCheckUserName, true);
		
		for(Entry<String, String> entry : oCheckInfoList.entrySet()){
			if(oSwitchCheckInfo != null && oSwitchCheckInfo.optString(entry.getKey()).equals("y"))
				updateTableDetail(iTable, sTableExtension, entry.getKey(), StringLib.createStringArray(5, entry.getValue()), true);
		}
		updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_EMPTY, StringLib.createStringArray(5, ""), true);
	}
	
	public void clearTableDetails(int iTable, String sTableExtension) {
		updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_TABLE_SIZE, null, false);
		updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_OPEN_TIME, null, false);
		updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_COVER_NO, null, false);
		updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_CHECK_TOTAL, null, false);
		updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_MEMBER_NUMBER, null, false);
		updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_MEMBER_NAME, null, false);
		updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_OWNER_NAME, null, false);
		updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_CHECK_INFO_ONE, null, false);
		updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_CHECK_INFO_TWO, null, false);
		updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_CHECK_INFO_THREE, null, false);
		updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_CHECK_INFO_FOUR, null, false);
		updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_CHECK_INFO_FIVE, null, false);
		updateTableDetail(iTable, sTableExtension, FrameTableButton.STATUS_EMPTY, null, false);
	}
	
	public Integer getCurrentTableDetailIndex(){
		if (m_bShowTableMode) {
			// In table mode now
			for(FrameTableMode oFrameTableMode: m_oFrameTableModeList){
				return oFrameTableMode.getCurrentTableDetailIndex();
			}
		}else{		
			for(HashMap<String, FrameTableButton> oButtonMapTableList:m_oButtonMapTableList.values()) {
				for(FrameTableButton oTableButton:oButtonMapTableList.values()){
					return oTableButton.getCurrentTableDetailIndex();
				}
			}
		}
		
		return 0;
	}
	
	public void addMapIndicator(int iTotalMapCount) {
		int i = 0, iIndicatorWidth = 17, iTopMargin = 2, iRightMargin = 20;
		int iTotalWidth = (iIndicatorWidth * iTotalMapCount) + (iRightMargin * (iTotalMapCount - 1));
		
		m_oFrameMapIndicator.setLeft((this.getWidth() - iTotalWidth)/2);
		m_oFrameMapIndicator.setWidth(iTotalWidth);
		for(i=1; i<=iTotalMapCount; i++) {
			VirtualUIImage oMapIndicatorImage = new VirtualUIImage();
			m_oTemplateBuilder.buildImage(oMapIndicatorImage, "imgMapIndicator");
			
			oMapIndicatorImage.setTop(iTopMargin);
			oMapIndicatorImage.setLeft(((iIndicatorWidth + iRightMargin) * (i - 1)));
			oMapIndicatorImage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "icons/icon_map_indicator_grey.png");
			oMapIndicatorImage.setVisible(true);
			
			m_oFrameMapIndicator.attachChild(oMapIndicatorImage);
			m_oImageMapIndicatorList.put(i, oMapIndicatorImage);
			
		}
	}
	
	public void addUpdateTableStatusTimer(){
		// TODO : setup interval
		this.addTimer(TIMER_UPDATE_TABLE_STATUS, 1000, true, AppGlobal.g_oFuncOutlet.get().getOutletCode(), false, true, null);
	}
	
	public void addCheckPeriodFloorPlanTimer(){
		this.addTimer(TIMER_CHECK_PERIOD_FLOOR_PLAN, 10000, true, TIMER_CHECK_PERIOD_FLOOR_PLAN, false, true, null);
	}
	
	public void addCheckTableStatusCleaningTimer(){
		this.addTimer(TIMER_CHECK_TABLE_STATUS_CLEANING, 5000, true, TIMER_CHECK_TABLE_STATUS_CLEANING, false, true, null);
	}
	
	public void setUpdateTableStatusTimer(boolean bStart){
		this.controlTimer(TIMER_UPDATE_TABLE_STATUS, bStart);
		this.setCheckPeriodFloorPlanTimer(bStart);
		
		// Set timer to handle check table status cleaning
		if (AppGlobal.g_oFuncStation.get().getAutomaticallyChangeCleaningToVacantInterval() > 0)
			this.setCheckTableStatusCleaningTimer(bStart);
	}
	
	public void setCheckPeriodFloorPlanTimer(boolean bStart){
		this.controlTimer(TIMER_CHECK_PERIOD_FLOOR_PLAN, bStart);
	}
	
	public void setCheckTableStatusCleaningTimer(boolean bStart){
		this.controlTimer(TIMER_CHECK_TABLE_STATUS_CLEANING, bStart);
	}
	
	// Create update basket timer
	public void addScreenSaverTimer(){
		this.addTimer(TIMER_SCREEN_SAVER, AppGlobal.g_oFuncStation.get().getScreenSaverTimeout() * 60 * 1000, true, TIMER_SCREEN_SAVER, false, false, null);
	}
	
	// Start to update basket
	public void startScreenSaverTimer(boolean bStart){
		this.controlTimer(TIMER_SCREEN_SAVER, bStart);
	}
	
	public void addIdleTimeLogoutTimer() {
		this.addTimer(IDLE_TIME_LOGOUT, m_iIdleTimeOut * 1000, false, IDLE_TIME_LOGOUT, true, true, null);
	}
	
	public void setIdleTimeLogoutTimer(boolean bStart) {
		if (m_iIdleTimeOut > 0)
			this.controlTimer(IDLE_TIME_LOGOUT, bStart);
	}
		
	public void  updateIdleTimeLogoutTimer() {
		m_iIdleTimeOut = AppGlobal.g_oFuncStation.get().getIdleTimeLogout(AppGlobal.g_oFuncUser.get().getUserGroupList());
		this.updateTimerInterval(IDLE_TIME_LOGOUT, m_iIdleTimeOut * 1000);
	}

	public void setLastUpdateTime(String sLastUpdateTime) {
		this.m_sLateUpdateTime = sLastUpdateTime;
	}
	
	public void updateTableStatus(int iTable, String sTableExtension, String sTag, String sStatus, int iLockStationId, int iTableExtensionCount){
		FrameTableButton oFrameTableButton;
		
		// Get Default Status Background Color Codes
		JSONObject oTableStatusBackgroundColor = AppGlobal.g_oFuncStation.get().getTableStatusBackgroundColor();
		
		for(int i=0; i<m_oButtonMapTableList.size(); i++){
			HashMap<String, FrameTableButton> oTableList = m_oButtonMapTableList.get(i+1);
			if(oTableList.containsKey(iTable + "_" + sTableExtension)){
				oFrameTableButton = oTableList.get(iTable + "_" + sTableExtension);
				if(sStatus.equals(PosOutletTable.STATUS_NEW_TABLE)){
					if(sTag.equals(PosOutletTable.TAG_SEAT_IN)) {
						oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#00A2E8", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_SEAT_IN), PosOutletTable.STATUS_TABLE_RESERVATION, AppGlobal.g_oLang.get()._("vacant", ""));
						oFrameTableButton.setLabelForegroundColor("#FFFFFF");
					} else {
						if(m_sTableForegroundColor.containsKey(sStatus)){
							oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
							oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor(m_sTableBackgroundColor.get(sStatus), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_VACANT), PosOutletTable.STATUS_NEW_TABLE, AppGlobal.g_oLang.get()._("vacant", ""));
						}else
							oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#FFFFFF", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_VACANT), PosOutletTable.STATUS_NEW_TABLE, AppGlobal.g_oLang.get()._("vacant", ""));
					}
					oFrameTableButton.setPrinted(false);
					oFrameTableButton.setLocked(false);
				} else
				if(sStatus.equals(PosOutletTable.STATUS_VACANT)){
					if(sTag.equals(PosOutletTable.TAG_SEAT_IN)) {
						oFrameTableButton.setLabelForegroundColor("#FFFFFF");
						oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#00A2E8", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_SEAT_IN), PosOutletTable.STATUS_TABLE_RESERVATION, AppGlobal.g_oLang.get()._("vacant", ""));
					} else {
						if(m_sTableForegroundColor.containsKey(sStatus)){
							oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
							oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor(m_sTableBackgroundColor.get(sStatus), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_VACANT), PosOutletTable.STATUS_VACANT, AppGlobal.g_oLang.get()._("vacant", ""));
						}else
							oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#FFFFFF", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_VACANT), PosOutletTable.STATUS_VACANT, AppGlobal.g_oLang.get()._("vacant", ""));
					}
					oFrameTableButton.setPrinted(false);
					oFrameTableButton.setLocked(false);
				} else
				if(sStatus.equals(PosOutletTable.STATUS_OCCUPIED)){
					if(m_sTableForegroundColor.containsKey(sStatus)){
						oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
						oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor(m_sTableBackgroundColor.get(sStatus), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_OCCUPIED), PosOutletTable.STATUS_OCCUPIED, AppGlobal.g_oLang.get()._("send_check", ""));
					}else
						oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#FF0000", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_OCCUPIED), PosOutletTable.STATUS_OCCUPIED, AppGlobal.g_oLang.get()._("send_check", ""));
					oFrameTableButton.setPrinted(false);
					oFrameTableButton.setLocked(false);
				} else
				if(sStatus.equals(PosOutletTable.STATUS_CHECK_PRINTED)){
					if(m_sTableForegroundColor.containsKey(sStatus)){
						oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
						oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor(m_sTableBackgroundColor.get(sStatus), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_PRINTED), PosOutletTable.STATUS_CHECK_PRINTED, AppGlobal.g_oLang.get()._("print_check", ""));
					}else
						oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#00FF00", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_PRINTED), PosOutletTable.STATUS_CHECK_PRINTED, AppGlobal.g_oLang.get()._("print_check", ""));
					oFrameTableButton.setPrinted(true);
					oFrameTableButton.setLocked(false);
				} else 
				if(sStatus.equals(PosOutletTable.STATUS_COOKING_OVERTIME)){
					JSONObject oTempJSONObject = AppGlobal.g_oFuncStation.get().getTableFloorPlanSetting();
					if(oTempJSONObject != null && oTempJSONObject.optString("support_cooking_overtime").equals("y")) {
						String sColorCode = oTempJSONObject.optString("cooking_overtime_status_color", "");
						oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
						if(!sColorCode.isEmpty())
							oFrameTableButton.setTableColor(sColorCode, PosOutletTable.STATUS_COOKING_OVERTIME, AppGlobal.g_oLang.get()._("cooking_overtime", ""));
						else if(m_sTableForegroundColor.containsKey(sStatus))
							oFrameTableButton.setTableColor(m_sTableBackgroundColor.get(sStatus), PosOutletTable.STATUS_COOKING_OVERTIME, AppGlobal.g_oLang.get()._("cooking_overtime", ""));
						else
							oFrameTableButton.setTableColor("#FF8100", PosOutletTable.STATUS_COOKING_OVERTIME, AppGlobal.g_oLang.get()._("cooking_overtime", ""));
						oFrameTableButton.setPrinted(true);
						oFrameTableButton.setLocked(false);
					}
				} else 
				if(sStatus.equals(PosOutletTable.STATUS_CLEANING_TABLE)){
					if(m_sTableForegroundColor.containsKey(sStatus)) {
						oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
						oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor(m_sTableBackgroundColor.get(sStatus), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_CLEANING), PosOutletTable.STATUS_CLEANING_TABLE, AppGlobal.g_oLang.get()._("cleaning", ""));
					} else
						oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#C68C53", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_CLEANING), PosOutletTable.STATUS_CLEANING_TABLE, AppGlobal.g_oLang.get()._("cleaning", ""));
					oFrameTableButton.setPrinted(false);
					oFrameTableButton.setLocked(false);
				}
				if(iLockStationId > 0){
					oFrameTableButton.setLocked(true);
				}
				oFrameTableButton.setTableExtensionCount(iTableExtensionCount);
			}
		}
		
		// In table mode
		for(FrameTableMode oFrameTableMode: m_oFrameTableModeList){
			oFrameTableMode.updateTableStatus(iTable, sTableExtension, sTag, sStatus, iLockStationId, iTableExtensionCount);
		}
	}
	
	// If add table / update table status in a batch, skip the re-sequence of table mode tables first
	// Do re-sequence after finish add tables / update table status
	public void updateTableModeStatusPreProcess() {
		for(FrameTableMode oFrameTableMode: m_oFrameTableModeList){
			oFrameTableMode.skipReseqTable(true);
		}
	}
	
	// Do re-sequence after finish add tables / update table status
	public void updateTableModeStatusPostProcess() {
		for(FrameTableMode oFrameTableMode: m_oFrameTableModeList){
			oFrameTableMode.skipReseqTable(false);
		}
	}
	
	// Thread function - update table status
	public void refreshTableFloorPlan(int iClientSockId){
		// Set the last client socket ID
		AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
		
		for (FrameTableFloorPlanListener listener : listeners) {
			// Raise the event to parent
			listener.frameTableFloorPlan_Refresh();
		}
		
		// Send the UI packet to client and the thread is finished
		super.getParentForm().finishUI(true);
	}
	
	// Thread function - check if floor plan is changed
	public void checkTableFloorPlan(int iClientSockId){
		// Set the last client socket ID
		AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
		
		for (FrameTableFloorPlanListener listener : listeners) {
			// Raise the event to parent
			listener.frameTableFloorPlan_CheckPeriodFloorPlan();
		}
		
		// Send the UI packet to client and the thread is finished
		super.getParentForm().finishUI(true);
	}
	
	public int getCurrentMapIndex() {
		return this.m_iCurrentMapIndex;
	}
	
	public String getLastUpdateTime() {
		return this.m_sLateUpdateTime;
	}
	
	public void showTableMode(boolean bShow){
		if(bShow) {
			// Show the table mode
			for(VirtualUIImage oImageBackGround : m_oImageFloorBackGroundList){
				oImageBackGround.setVisible(false);
			}
			for(FrameTableMode oFrameTableMode: m_oFrameTableModeList){
				oFrameTableMode.setVisible(true);
				oFrameTableMode.bringToTop();
				for(FrameTableButton oFrameTableButton : oFrameTableMode.getTableButtonList()) {
					oFrameTableButton.m_oFrameBackground.setCornerRadius("8,8,0,0");
				}
			}
			for(HashMap<String, FrameTableButton> oButtonMapTableList:m_oButtonMapTableList.values()) {
				for(FrameTableButton oTableButton:oButtonMapTableList.values())
					oTableButton.setVisible(false);
			}
		}else{
			// Hide the table mode
			for(VirtualUIImage oImageBackGround : m_oImageFloorBackGroundList){
				oImageBackGround.setVisible(true);
			}
			for(FrameTableMode oFrameTableMode: m_oFrameTableModeList){
				oFrameTableMode.setVisible(false);
			}
			for(HashMap<String, FrameTableButton> oButtonMapTableList:m_oButtonMapTableList.values()) {
				for(FrameTableButton oTableButton:oButtonMapTableList.values()) {
					oTableButton.setVisible(true);
				}
			}
		}
		m_bShowTableMode = bShow;
	}
	
	public void showOpenFunctionPanel(boolean bShow) {
		m_bOpenFunctionPanel = bShow;
		if(m_oFloorPlanFunctionBar != null)
			m_oFloorPlanFunctionBar.updateBasketExtendBarImage(bShow);
	}
	
	public boolean isFunctionBarButtonClicked() {
		return m_bFunctionBarButtonClick;
	}
	
	public boolean isTableModeShowing() {
		return (m_bShowTableMode);
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
	
	private void toggleTableDetail() {
		if (m_bShowTableMode) {
			for(FrameTableMode oFrameTableMode: m_oFrameTableModeList)
				oFrameTableMode.toggleTableDetail();
		} else {
			int iCurrentDetailIndex = getCurrentTableDetailIndex() + 1;
			for(HashMap<String, FrameTableButton> oButtonMapTableList:m_oButtonMapTableList.values()) {
				for(FrameTableButton oTableButton:oButtonMapTableList.values())
					oTableButton.setTableDetailByIndex(iCurrentDetailIndex);
			}
		}
	}
	
	public void setFrameFloorPlanHeader(String sColor){
		m_oFrameFloorPlanHeader.setBackgroundColor(sColor);
	}
	
	public String getSwitchUserModeStatus(){
		return m_sSwitchUserMode;
	}	
	
	public void setSwitchUserModeStatus(String sStatus){
		m_sSwitchUserMode = sStatus;
	}
	
	public void setVisibleScreenSaver() {
		if(AppGlobal.g_oFuncStation.get().getScreenSaverImage()) {
			m_oScreenSaverImage.setVisible(true);
			m_oScreenSaverImage.bringToTop();
		} else {
			m_oScreenSaver.setVisible(true);
			m_oScreenSaver.bringToTop();
		}
	}
	
	public void setMapIndicator(int iMapId) {
	
		// Change the map indicator image for current page
		for(VirtualUIImage oMapIndicator:m_oImageMapIndicatorList.values())
			oMapIndicator.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "icons/icon_map_indicator_grey.png");
		
		VirtualUIImage oCurrentMapIndicator = m_oImageMapIndicatorList.get(iMapId);
		if (oCurrentMapIndicator != null)
			oCurrentMapIndicator.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "icons/icon_map_indicator_white.png");
		
	}
	
	public void setCurrentDetailByKey(String sKey){
		for(HashMap<String, FrameTableButton> oButtonMapTableList:m_oButtonMapTableList.values()) {
			for(FrameTableButton oFrameTableButton:oButtonMapTableList.values())
				oFrameTableButton.setCurrentDetailByKey(sKey);
		}
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if(m_oListFloor.getVisible())
			m_oListFloor.setVisible(false);
		if(m_oListFloorBackground.getVisible())
			m_oListFloorBackground.setVisible(false);
		
		if (this.getSwitchUserModeStatus().equals(FrameTableFloorPlan.SWITCH_USER_MODE_ON) && AppGlobal.g_oFuncStation.get().getScreenSaverTimeout() > 0)
			startScreenSaverTimer(true);
		
		// Status switch button is clicked
		if(m_oFrameSwitchInfo.getId() == iChildId || m_oLabelSwitchInfo.getId() == iChildId || m_oImageSwitchInfo.getId() == iChildId) {
			toggleTableDetail();
			return true;
		}
		
		if(m_oFrameCardView.getId() == iChildId || m_oLabelCardView.getId() == iChildId || m_oImageCardView.getId() == iChildId) {
			m_oImageCardView.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_main_cards_active.png");
			m_oImageTableView.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_main_tables.png");
			m_oLabelCardView.setForegroundColor("#0055B8");
			m_oLabelTableView.setForegroundColor("#FFFFFF");
			showTableMode(true);
			return true;
		}
		
		if(m_oFrameTableView.getId() == iChildId || m_oLabelTableView.getId() == iChildId || m_oImageTableView.getId() == iChildId) {
			m_oImageCardView.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_main_cards.png");
			m_oImageTableView.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_main_tables_active.png");
			m_oLabelCardView.setForegroundColor("#FFFFFF");
			m_oLabelTableView.setForegroundColor("#0055B8");
			showTableMode(false);
			return true;
		}
		
		// Action cancel button is clicked
		if(m_oButtonActionCancel.getId() == iChildId || m_oFrameActionCancel.getId() == iChildId || m_oLabelActionCancel.getId() == iChildId){
			for (FrameTableFloorPlanListener listener : listeners) {
				// Raise the event to parent
				listener.frameTableFloorPlan_ActionCancelClicked();
			}
			return true;
		}
		
		if(iChildId == m_oScreenSaver.getId() || iChildId == m_oScreenSaverImage.getId()) {
			m_oScreenSaver.setVisible(false);
			m_oScreenSaverImage.setVisible(false);
			startScreenSaverTimer(true);
			return true;
		}
		
		return bMatchChild;
	}
	
	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if(iId == super.getIDForPosting().hashCode()) {
			if (sNote.equals(TIMER_CHECK_PERIOD_FLOOR_PLAN)) {
				// Check if the floor plan is changed or not
				
				//Create parameter array
				Object[] oParameters = new Object[1];
				oParameters[0] = iClientSockId;
				
				this.getParentForm().addTimerThread(iClientSockId, this, "checkTableFloorPlan", oParameters);
			} else if (sNote.equals(TIMER_SCREEN_SAVER)) {
				AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
				
				//disable open panel
				if (m_oFloorPlanFunctionBar != null && m_bOpenFunctionPanel == true){
					this.frameFloorPlanFunctionBar_openPanelImageClicked();
					showOpenFunctionPanel(false);
				}
				
				setVisibleScreenSaver();
				startScreenSaverTimer(false);
				
				// Send the UI packet to client and the thread is finished
				super.getParentForm().finishUI(true);
			} else if (sNote.equals(TIMER_CHECK_TABLE_STATUS_CLEANING)) {
				AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
				
				AppGlobal.checkCleaningTable();
				
				// Send the UI packet to client and the thread is finished
				super.getParentForm().finishUI(true);
			} else if (sNote.equals(IDLE_TIME_LOGOUT)) {
				setIdleTimeLogoutTimer(false);
				for (FrameTableFloorPlanListener listener : listeners) {
 					listener.frameTableFloorPlan_HandleLogout();
 				}
				 super.getParentForm().finishUI(true);
			} else {
				// Update table status timer
				
				// Create parameter array
				Object[] oParameters = new Object[1];
				oParameters[0] = iClientSockId;
				
				this.getParentForm().addTimerThread(iClientSockId, this, "refreshTableFloorPlan", oParameters);
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void frameTableFloorPlan_TableClicked(String sTable, String sTableExtension) {
		for (FrameTableFloorPlanListener listener : listeners) {
			// Raise the event to parent
			listener.frameTableFloorPlan_TableClicked(sTable, sTableExtension, m_bShowTableMode);
		}
	}
	
	@Override
	public void frameTableFloorPlan_TableLongClicked(String sTable,
			String sTableExtension) {
		for (FrameTableFloorPlanListener listener : listeners) {
			// Raise the event to parent
			listener.frameTableFloorPlan_TableLongClicked(sTable, sTableExtension);
		}
	}
	
	@Override
	public void frameFloorPlanFunctionBar_openPanelImageClicked() {
		if(m_bOpenFunctionPanel)
			m_bOpenFunctionPanel = false;
		else
			m_bOpenFunctionPanel = true;
	
		for (FrameTableFloorPlanListener listener: listeners) {
			listener.frameTableFloorPlan_FunctionBarOpenPanelClicked();
		}
	}
	
	@Override
	public void frameFloorPlanFunctionBar_buttonClicked(String sNote) {
		m_bFunctionBarButtonClick = true;
		for (FrameTableFloorPlanListener listener: listeners) {
			listener.frameTableFloorPlan_FunctionBarButtonClicked(sNote);
		}
		m_bFunctionBarButtonClick = false;
	}
	
	@Override
	public void frameTableFloorPlan_MenuModeNewOrder(int iDefaultTableNo){
		for (FrameTableFloorPlanListener listener : listeners) {
			// Raise the event to parent
			listener.frameTableFloorPlan_MenuModeNewOrder(iDefaultTableNo);
		}
	}
	
	@Override
	public void frameTableFloorPlan_MenuModeChangeLanguage(){
		for (FrameTableFloorPlanListener listener: listeners) {
			// Raise the event to parent
			listener.frameTableFloorPlan_MenuModeChangeLanguage();
		}
	}
	
	@Override
	public void frameTableFloorPlan_MenuModeCheckReview(int iDefaultTableNo){
		// Raise the event to parent
		for (FrameTableFloorPlanListener listener: listeners) {
			listener.frameTableFloorPlan_MenuModeCheckReview(iDefaultTableNo);
		}
	}
	
	@Override
	public void frameCommonPageContainer_changeFrame() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean frameCommonPageContainer_swipe(boolean bLeft, String sNote, int iChildId) {
		boolean bMatchChild = false;
		if(this.getEnabled() && this.getVisible()){
			if(bLeft){
				int iCurrentTableFloorPlanIndex = m_oCommonPageContainer.getCurrentIndex();
				if(iCurrentTableFloorPlanIndex > 0) {
					m_oCommonPageContainer.clickTag(iCurrentTableFloorPlanIndex - 1);
					bMatchChild = true;
				}
			} else {
				int iCurrentTableFloorPlanIndex = m_oCommonPageContainer.getCurrentIndex();
				if(iCurrentTableFloorPlanIndex < m_oCommonPageContainer.getTotalIndex() - 1) {
					m_oCommonPageContainer.clickTag(iCurrentTableFloorPlanIndex + 1);
					bMatchChild = true;
				}
			}
			bMatchChild = true;
		}
	
		return bMatchChild;
	}
	
	@Override
	public void frameCommonPageContainer_updateFrame(int iOrigIndex, int iIndex, String sFrameName) {
		boolean bTableShowing = false;
		bTableShowing = isTableModeShowing();
		setUpdateTableStatusTimer(true);
		setIdleTimeLogoutTimer(true);
		showTableMode(bTableShowing);
		setMapIndicator(iIndex + 1);
	}
	
	@Override
	public void frameCommonPageContainer_CloseImageClicked() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void frameCommonPageContainer_ClickButtonIndex(int iIndex) {
		// TODO Auto-generated method stub
		
	}
}
