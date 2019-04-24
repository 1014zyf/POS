package app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import om.FailoverStationGroup;

public class FuncSmartStation {
	
	public final String ROLE_UNDEFINED = "u";
	public final String ROLE_SERVICE_MASTER = "sm";
	public final String ROLE_STANDALONE = "s";
	public final String ROLE_WORKSTATION = "ws";
	
	public final static String FAILOVER_ACTION_NO_ACTION = "";
	public final static String FAILOVER_ACTION_SELECT_TO_SERVICE_MASTER = "s";
	public final static String FAILOVER_ACTION_WAIT_SERVICE_MASTER = "w";
	public final static String FAILOVER_ACTION_SELECT_TO_STANDALONE = "a";
	public final static String FAILOVER_ACTION_SWITCH_TO_WORKSTATION = "t";
	
	private boolean m_bSmartStationModel;
	
	private String m_sStationRole;
	
	private String m_sFailoverActionType;
	
	private static FailoverStationGroup m_oFailoverStationGroup;
	
	private String m_sStationUdidShowingActionSelection;
	
	private String m_sErrorMessage;
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage;
	}
	
	public FuncSmartStation(){
		m_bSmartStationModel = false;
		m_sStationRole = ROLE_UNDEFINED;
		m_sFailoverActionType = FAILOVER_ACTION_NO_ACTION;
		m_oFailoverStationGroup = new FailoverStationGroup();
		m_sStationUdidShowingActionSelection = "";
	}
	
	public boolean isSmartStationModel() {
		return m_bSmartStationModel;
	}
	
	public boolean initSmartStationModel() {
		m_bSmartStationModel = true;
		
		// Load the Failover station group setup
		m_oFailoverStationGroup = new FailoverStationGroup();
		m_oFailoverStationGroup.readStationGroup();
		if (m_oFailoverStationGroup.getModel().isEmpty()) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_load_failover_station_group_setup");
			//Sunny
			//return false;
		}
		
		// Launch the Role Manager object
		//Sunny
		
		return true;
	}
	
	// Return the station udid showing the action selection screen
	public String getStationUdidShowingActionSelectionScreen() {
		synchronized (this) {
			if (m_sStationUdidShowingActionSelection.isEmpty()) {
				// Current station can show
				m_sStationUdidShowingActionSelection = AppGlobal.getActiveClient().getUDID();
			}
			
			return m_sStationUdidShowingActionSelection;
		}
	}
	
	// Return the type of failover
	public String getFailoverType() {
		return "";
	}
	
	public boolean switchToServiceMasterRole() {
		
		m_sStationRole = ROLE_SERVICE_MASTER;
		
		// Prepare communication channel with Service Master MQ server
//		if (!AppGlobal.initMQCommandManager()) {
//			m_sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_init_mq");
//			//Sunny
//			//return false;
//		}
System.out.println("testtest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Switch to service master");
		return true;
	}
	
	public boolean switchToWorkstationRole() {
		
		m_sStationRole = ROLE_WORKSTATION;
		
		// Prepare communication channel with Service Master MQ server
//		if (!AppGlobal.initMQCommandManager()) {
//			m_sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_init_mq");
//			//Sunny
//			//return false;
//		}
System.out.println("testtest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Switch to station");
		return true;
	}
	
	public boolean switchToStandaloneRole() {
		
		m_sStationRole = ROLE_STANDALONE;
		
		//Sunny
		
		// Force to logout
		
		
System.out.println("testtest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Switch to standalone");
		return true;
	}
	
	public boolean leaveStandaloneRole() {
		
		return true;
	}
	
	public boolean isUndefinedRole() {
		return m_sStationRole.equals(ROLE_UNDEFINED);
	}
	
	public boolean isServiceMasterRole() {
		return m_sStationRole.equals(ROLE_SERVICE_MASTER);
	}
	
	public boolean isWorkstationRole() {
		return m_sStationRole.equals(ROLE_WORKSTATION);
	}
	
	public boolean isStandaloneRole() {
		return m_sStationRole.equals(ROLE_STANDALONE);
	}
	
	public boolean isFailoverActionNoAction() {
		return m_sFailoverActionType.equals(FAILOVER_ACTION_NO_ACTION);
	}
	
	public String getFailoverActionType() {
		//Sunny
		return m_sFailoverActionType;
	}
	
//Sunny
public void setFailoverActionType() {
	try{
		String sFile = "C:\\temp\\sk1";
		InputStream is = new FileInputStream(sFile);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		boolean firstLine = true;

		while ((line = reader.readLine()) != null) {
			if (!firstLine)
				sb.append('\n');
			sb.append(line);
			firstLine = false;
		}

		is.close();
		
		m_sFailoverActionType = sb.toString();
System.out.println("testtest =============================================== " + m_sFailoverActionType);
		is.close();
	}catch(Exception e){};
}
	
	public int getFailoverStationGroupId() {
		return m_oFailoverStationGroup.getStgpId();
	}
	
	public String getMasterIP() {
		return m_oFailoverStationGroup.getCommonSettingsByKey("master_ip");
	}
	
	public int getMQPort() {
		return m_oFailoverStationGroup.getPort();
	}
	
	public int getMQSSLPort() {
		return m_oFailoverStationGroup.getSSLPort();
	}
}
