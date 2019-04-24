package app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import om.InfInterface;
import om.InfVendor;
import om.PosInterfaceConfig;

public class FuncKitchenMonitor {
	private int m_iInterfaceId;
	// Support flag
	private boolean m_bSupportKitchenMonitor;

	private boolean m_bIsConnected;
	
	// Timeout setting
	private int m_iTimeout;
	
	private String m_sServerIp;
	
	private int m_iServerPort;
	
	private String m_sDestinationFineDining;
	
	private String m_sDestinationTakeout; 
	
	public FuncKitchenMonitor() {
		m_bSupportKitchenMonitor = false;
		m_bIsConnected = false;
		m_iTimeout = 3000;
		m_sServerIp = "";
		m_iServerPort = 0;
		m_sDestinationFineDining = "";
		m_sDestinationTakeout = "";
	}
	
	public void readSetup() {
		List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		
		// Get the configure from interface module
		oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PERIPHERAL_DEVICE);
		for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList) {
			if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_DEVICE_MANAGER)) {
				try {
					JSONObject oInterfaceSetup = oPosInterfaceConfig.getInterfaceConfig();
					
					m_iInterfaceId = oPosInterfaceConfig.getInterfaceId();
					
					// Support flag
					if (oInterfaceSetup.has("kitchen_monitor_setup") && oInterfaceSetup.getJSONObject("kitchen_monitor_setup").getJSONObject("params").getJSONObject("support").getInt("value") == 1)
						m_bSupportKitchenMonitor = true;
					else
						break;
					
					// Server IP
					if (oInterfaceSetup.optJSONObject("kitchen_monitor_setup").optJSONObject("params").has("server_ip"))
						m_sServerIp = oInterfaceSetup.getJSONObject("kitchen_monitor_setup").getJSONObject("params").getJSONObject("server_ip").getString("value");
					
					// Server Port
					if (oInterfaceSetup.optJSONObject("kitchen_monitor_setup").optJSONObject("params").has("server_port"))
						m_iServerPort = Integer.parseInt(oInterfaceSetup.getJSONObject("kitchen_monitor_setup").getJSONObject("params").getJSONObject("server_port").getString("value"));
					
					// Fine dining destination id
					if (oInterfaceSetup.optJSONObject("kitchen_monitor_setup").optJSONObject("params").has("fine_dining_destination_id"))
						m_sDestinationFineDining = oInterfaceSetup.optJSONObject("kitchen_monitor_setup").optJSONObject("params").getJSONObject("fine_dining_destination_id").getString("value");
					
					// take out destination id
					if (oInterfaceSetup.optJSONObject("kitchen_monitor_setup").optJSONObject("params").has("take_out_destination_id"))
						m_sDestinationTakeout = oInterfaceSetup.optJSONObject("kitchen_monitor_setup").optJSONObject("params").getJSONObject("take_out_destination_id").getString("value");						
				} catch (Exception e) {
					AppGlobal.stack2Log(e);
				}
			}
		}
	}
	
	public void writeLog(int iInterfaceId, String sLog, int iStationId) {
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);

		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/kitchen_monitor_log." + sCurrentMonth;

		try{
			// Create file 
			FileWriter fstream = new FileWriter(sFilename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			sContent.append(sCurrentTime);
			sContent.append(" [s:");
			sContent.append(iStationId);
			sContent.append(" i:");
			sContent.append(iInterfaceId);
			sContent.append("] ");
			sContent.append(sLog);
			sContent.append("\n");
			
			out.write(sContent.toString());
			
			//Close the output stream
			out.close();
			fstream.close();
		}catch (Exception e){}//Catch exception if any
	}
	
	public boolean isSupportKitchenMonitor() {
		return m_bSupportKitchenMonitor;
	}
	
	public void setKitchenMontiorConnected(boolean bConnected) {
		m_bIsConnected = bConnected;
	}

	public boolean isConnected(){
		return m_bIsConnected;
	}
	
	public int getInterfaceId() {
		return m_iInterfaceId;
	}
	
	public String getServerIP() {
		return m_sServerIp;
	}
	
	public int getServerPort() {
		return m_iServerPort;
	}
	
	public int getTimeout() {
		return m_iTimeout;
	}
	
	public String getDestinationIdFineDining() {
		return m_sDestinationFineDining;
	}
	
	public String getDestinationIdTakeOut() {
		return m_sDestinationTakeout;
	}
}
