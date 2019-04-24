package app;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

public class MQCommandManager {
	
	private FuncMessageQueue m_oFuncMessageQueue;
	
	private static final String EXCHANGE_NAME = "pos";
	
	private static final String ROUTING_KEY_POS_CMD = "pos.cmd";
	private static final String ROUTING_KEY_POS_POLL = "pos.poll";
	private static final String ROUTING_KEY_POS_SYNC_INFO = "pos.sync_info";
	
	public static final String INFO_LIST_OVER_COOKING_TIME_TABLE = "over_cooking_time_table_list";
	
	private class MQCommandProtocol {
		public class Action {
			public static final String KEY = "action";
			
			public static final String KILL_STATION = "kill_station";
			public static final String KILL_OTHER_STATIONS = "kill_other_stations";
			public static final String RESTART_AUTO_STATION = "restart_auto_station";
			public static final String RESTART_PORTAL_STATION = "restart_portal_station";
			public static final String PMS_SHELL_OPERATION = "pms_shell_information";
		}
		public class Station {
			public static final String KEY = "station";
		}
		public class Reason {
			public static final String KEY = "reason";
		}
		public class Outlet {
			public static final String KEY = "outlet";
		}
		public class Udid {
			public static final String KEY = "udid";
		}
		public class Interface {
			public static final String FUNCION_KEY = "function";
			public static final String ID_KEY = "id";
			public static final String VENDOR_KEY = "vendor";
			
			public static final String FUNCTION_RESTART = "restart";
			public static final String FUNCTION_STOP = "stop";
		}
	}
	
	private class MQPollProtocol {
		public class Action {
			public static final String KEY = "action";
			
			public static final String UPDATE_POLL_FILE = "update_poll_file";
			public static final String DELETE_POLL_FILE = "delete_poll_file";
		}
		public class Folder {
			public static final String KEY = "folder";
		}
		public class File {
			public static final String KEY = "file";
		}
		public class Value {
			public static final String KEY = "value";
		}
	}

	private class MQGlobalInfoSynchronizeProtocol {
		public class Action {
			public static final String KEY = "action";
			
			public static final String ADD_UPDATE = "add_update";
			public static final String REMOVE = "remove";
		}
		public class Variable {
			public static final String KEY = "variable";
			public static final String OVER_COOKING_TIME_TABLE_LIST = "over_cooking_time_table_list";
		}
		public class Content {
			public static final String KEY = "content";
			public static final String OUTLET_ID = "outlet_id";
			public static final String TABLE_KEY = "table_key";
			public static final String OVER_TIME = "over_time";
		}
	}
	
	public MQCommandManager() {
	}
	
	public boolean init() {
		String sMqServerIP = "";
		int iMqPort = 0;
		int iMqSSLPort = 0;
		String sLogin = "heroadmin";
		String sPassword = "infrasys";
		
		if (!AppGlobal.g_oFuncSmartStation.isServiceMasterRole() && !AppGlobal.g_oFuncSmartStation.isWorkstationRole())
			// Not service master or workstation, no need MQ service
			return false;
		
		// Read setup from DB
		// Retrieve Service Master URL, port and ssl port
		//FailoverStationGroup oFailoverStationGroup = new FailoverStationGroup();
		//oFailoverStationGroup.readStationGroup();
		if (AppGlobal.g_oFuncSmartStation.isWorkstationRole())
			sMqServerIP = AppGlobal.g_oFuncSmartStation.getMasterIP();
		else
			sMqServerIP = "127.0.0.1";
		
		iMqPort = AppGlobal.g_oFuncSmartStation.getMQPort();
		iMqSSLPort = AppGlobal.g_oFuncSmartStation.getMQSSLPort();
		
		if (sMqServerIP.isEmpty() || iMqPort == 0)
			// No setup
			return false;
		
		m_oFuncMessageQueue = new FuncMessageQueue(sMqServerIP, iMqPort, sLogin, sPassword);
		
		for (int iRetryCount=0; iRetryCount<3; iRetryCount++) {
			// Initial connection (without SSL)
			m_oFuncMessageQueue.initConnection();
			if (m_oFuncMessageQueue.isConnected()) {
				m_oFuncMessageQueue.declareExchange(EXCHANGE_NAME, "topic");
				String sQueueName = "";
				if (!(sQueueName = m_oFuncMessageQueue.createQueue(sQueueName, EXCHANGE_NAME, ROUTING_KEY_POS_CMD, true)).isEmpty()) {
					m_oFuncMessageQueue.consumeQueue(sQueueName, new AppThread(new AppThreadManager(), this, "handleCommandFromMQ", null));
					
					/*sQueueName = "";
					if (!(sQueueName = m_oFuncMessageQueue.createQueue(sQueueName, EXCHANGE_NAME, ROUTING_KEY_POS_SYNC_INFO, true)).isEmpty())
						m_oFuncMessageQueue.consumeQueue(sQueueName, new AppThread(new AppThreadManager(), this, "handleSynchronizeGlobalInfo", null));*/
					return true;
				} else
					// Fail to create queue
					return false;
			} else {
				// Retry 3 times
				iRetryCount++;
			}
		}
		
		// Fail to connect to MQ server
		AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", "", "Fail to connect to SM MQ server");
		
		return false;
	}
	
	// Fire kill single station command to MQ
	public void fireKillSingleStationCommand(int iStationId, String sReason) {
		try {
			JSONObject oMessageJSONObject = new JSONObject();
			
			oMessageJSONObject.put(MQCommandProtocol.Action.KEY, MQCommandProtocol.Action.KILL_STATION);
			oMessageJSONObject.put(MQCommandProtocol.Station.KEY, iStationId);
			oMessageJSONObject.put(MQCommandProtocol.Reason.KEY, sReason);
			
			m_oFuncMessageQueue.publishMessage(EXCHANGE_NAME, ROUTING_KEY_POS_CMD, oMessageJSONObject.toString(), "3000");
		} catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	// Fire kill all other stations command to MQ
	public void fireKillOtherStationsCommand(int iStationId, String sReason) {
		try {
			JSONObject oMessageJSONObject = new JSONObject();
			
			oMessageJSONObject.put(MQCommandProtocol.Action.KEY, MQCommandProtocol.Action.KILL_OTHER_STATIONS);
			oMessageJSONObject.put(MQCommandProtocol.Station.KEY, iStationId);
			oMessageJSONObject.put(MQCommandProtocol.Reason.KEY, sReason);
			
			m_oFuncMessageQueue.publishMessage(EXCHANGE_NAME, ROUTING_KEY_POS_CMD, oMessageJSONObject.toString(), "3000");
		} catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	// Fire restart auto station message to MQ
	public void fireRestartAutoStationCommand(String sUDID) {
		try {
			JSONObject oMessageJSONObject = new JSONObject();
			
			oMessageJSONObject.put(MQCommandProtocol.Action.KEY, MQCommandProtocol.Action.RESTART_AUTO_STATION);
			oMessageJSONObject.put(MQCommandProtocol.Outlet.KEY, AppGlobal.g_oFuncStation.get().getOutletId());
			oMessageJSONObject.put(MQCommandProtocol.Udid.KEY, sUDID);
			
			m_oFuncMessageQueue.publishMessage(EXCHANGE_NAME, ROUTING_KEY_POS_CMD, oMessageJSONObject.toString(), "3000");
		} catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	// Fire restart portal station message to MQ
	public void fireRestartPortalStationCommand(String sUDID) {
		try {
			JSONObject oMessageJSONObject = new JSONObject();
			
			oMessageJSONObject.put(MQCommandProtocol.Action.KEY, MQCommandProtocol.Action.RESTART_PORTAL_STATION);
			oMessageJSONObject.put(MQCommandProtocol.Outlet.KEY, AppGlobal.g_oFuncStation.get().getOutletId());
			oMessageJSONObject.put(MQCommandProtocol.Udid.KEY, sUDID);
			
			m_oFuncMessageQueue.publishMessage(EXCHANGE_NAME, ROUTING_KEY_POS_CMD, oMessageJSONObject.toString(), "3000");
		} catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	// Fire PMS shell operation message to MQ
	public void firePmsShellOperationCommand(int iInterfaceId, String sVendorKey, String sFunction) {
		try {
			JSONObject oMessageJSONObject = new JSONObject();
			
			oMessageJSONObject.put(MQCommandProtocol.Action.KEY, MQCommandProtocol.Action.PMS_SHELL_OPERATION);
			oMessageJSONObject.put(MQCommandProtocol.Interface.FUNCION_KEY, sFunction);
			oMessageJSONObject.put(MQCommandProtocol.Interface.ID_KEY, iInterfaceId);
			oMessageJSONObject.put(MQCommandProtocol.Interface.VENDOR_KEY, sVendorKey);
			
			m_oFuncMessageQueue.publishMessage(EXCHANGE_NAME, ROUTING_KEY_POS_CMD, oMessageJSONObject.toString(), "3000");
		} catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	// Fire create or delete file under data path message to MQ
	public void fireCreateDeleteFileUnderDataPath(String sFolder, String sFile, String sValue, boolean bDelete) {
		try {
			JSONObject oMessageJSONObject = new JSONObject();
			
			if (bDelete)
				oMessageJSONObject.put(MQPollProtocol.Action.KEY, MQPollProtocol.Action.DELETE_POLL_FILE);
			else
				oMessageJSONObject.put(MQPollProtocol.Action.KEY, MQPollProtocol.Action.UPDATE_POLL_FILE);
			oMessageJSONObject.put(MQPollProtocol.Folder.KEY, sFolder);
			oMessageJSONObject.put(MQPollProtocol.File.KEY, sFile);
			oMessageJSONObject.put(MQPollProtocol.Value.KEY, sValue);
			
			m_oFuncMessageQueue.publishMessage(EXCHANGE_NAME, ROUTING_KEY_POS_POLL, oMessageJSONObject.toString(), "10000");
		} catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	// Fire synchronize global info message to MQ
	public void fireSynchronizeGlobalInfo(String sVariable, String sContent, boolean bRemove){
		try {
			JSONObject oMessageJSONObject = new JSONObject();
			
			if (!bRemove)
				oMessageJSONObject.put(MQGlobalInfoSynchronizeProtocol.Action.KEY, MQGlobalInfoSynchronizeProtocol.Action.ADD_UPDATE);
			else
				oMessageJSONObject.put(MQGlobalInfoSynchronizeProtocol.Action.KEY, MQGlobalInfoSynchronizeProtocol.Action.REMOVE);
			oMessageJSONObject.put(MQGlobalInfoSynchronizeProtocol.Variable.KEY, sVariable);
			
			if(sVariable.equals(MQGlobalInfoSynchronizeProtocol.Variable.OVER_COOKING_TIME_TABLE_LIST))
				oMessageJSONObject.put(MQGlobalInfoSynchronizeProtocol.Content.KEY, sContent);
			
			m_oFuncMessageQueue.publishMessage(EXCHANGE_NAME, ROUTING_KEY_POS_SYNC_INFO, oMessageJSONObject.toString(), "5000");
		} catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	// Close the connection to MQ
	public void close() {
		m_oFuncMessageQueue.closeConnection();
	}
	
	// Call back function of MQ to handle command
	private void handleCommandFromMQ() {
		try {
			String sRequestContent = m_oFuncMessageQueue.getRequest();
			if (!sRequestContent.isEmpty()) {
				JSONObject oCommandJSONObject = new JSONObject(sRequestContent);
				String sAction = "";
				int iStationId = 0;
				String sReason = "";
				int iOutletId = 0;
				String sUDID = "";
				
				// Action
				sAction = oCommandJSONObject.optString(MQCommandProtocol.Action.KEY, "");
				switch (sAction) {
				case MQCommandProtocol.Action.KILL_STATION:
					// Kill single station
					iStationId = oCommandJSONObject.optInt(MQCommandProtocol.Station.KEY, 0);
					sReason = oCommandJSONObject.optString(MQCommandProtocol.Reason.KEY, "");
					
					if (iStationId > 0)
						AppGlobal.killStationByMQCommand(iStationId, sReason, 1);
					
					break;
				case MQCommandProtocol.Action.KILL_OTHER_STATIONS:
					// Kill other stations
					iStationId = oCommandJSONObject.optInt(MQCommandProtocol.Station.KEY, 0);
					sReason = oCommandJSONObject.optString(MQCommandProtocol.Reason.KEY, "");
					
					if (iStationId > 0)
						AppGlobal.killStationByMQCommand(iStationId, sReason, 2);
					
					break;
				case MQCommandProtocol.Action.RESTART_AUTO_STATION:
					// Restart Auto Station
					iOutletId = oCommandJSONObject.optInt(MQCommandProtocol.Outlet.KEY, 0);
					sUDID = oCommandJSONObject.optString(MQCommandProtocol.Udid.KEY, "");
					
					if (AppGlobal.g_oFuncSmartStation.isServiceMasterRole() && iOutletId > 0){
						AppGlobal.launchAutoStation(iOutletId, sUDID, true);
					}
					break;
				case MQCommandProtocol.Action.RESTART_PORTAL_STATION:
					// Restart Auto Station
					iOutletId = oCommandJSONObject.optInt(MQCommandProtocol.Outlet.KEY, 0);
					sUDID = oCommandJSONObject.optString(MQCommandProtocol.Udid.KEY, "");
					
					if (AppGlobal.g_oFuncSmartStation.isServiceMasterRole() && iOutletId > 0){
						AppGlobal.launchPortalStation(iOutletId, sUDID, true);
					}
					break;
				case MQCommandProtocol.Action.PMS_SHELL_OPERATION:
					//PMS shell operation like restart or kill
					if(!AppGlobal.g_oFuncSmartStation.isServiceMasterRole())	//only for service master role
						break;
					
					String sFunction = oCommandJSONObject.optString(MQCommandProtocol.Interface.FUNCION_KEY, "");
					int iInterfaceId = oCommandJSONObject.optInt(MQCommandProtocol.Interface.ID_KEY, 0);
					String sVendorKey = oCommandJSONObject.optString(MQCommandProtocol.Interface.VENDOR_KEY, "");
					
					if(sFunction.isEmpty() || iInterfaceId <= 0 || sVendorKey.isEmpty())
						break;
					
					FuncPMS oFuncPMS = new FuncPMS();
					if(sFunction.equals(MQCommandProtocol.Interface.FUNCTION_RESTART))
						oFuncPMS.restartPMSShell(iInterfaceId, sVendorKey);
					else
						oFuncPMS.stopPMSShell(iInterfaceId, sVendorKey);
					
					break;
				default:
					// Incorrect action
					
					break;
				}
			}
			
		} catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	// Call back function of MQ to handle global info
	private void handleSynchronizeGlobalInfo(){
		try {
			String sRequestContent = m_oFuncMessageQueue.getRequest();
			if (!sRequestContent.isEmpty()) {
				JSONObject oCommandJSONObject = new JSONObject(sRequestContent);
				String sAction = "";
				int iOutletId = 0;
				String sTableKey = "";
				String sOverTime = "";
				DateTime oOverTime = null;
				
				// Action
				sAction = oCommandJSONObject.optString(MQGlobalInfoSynchronizeProtocol.Action.KEY, "");
				switch (sAction) {
				case MQGlobalInfoSynchronizeProtocol.Action.ADD_UPDATE:
					// Check whether below information exist
					iOutletId = oCommandJSONObject.optInt(MQGlobalInfoSynchronizeProtocol.Content.OUTLET_ID, 0);
					sTableKey = oCommandJSONObject.optString(MQGlobalInfoSynchronizeProtocol.Content.TABLE_KEY, "");
					sOverTime =  oCommandJSONObject.optString(MQGlobalInfoSynchronizeProtocol.Content.OVER_TIME, "");
					
					if(iOutletId == 0 || sTableKey.equals("") || sOverTime.equals(""))
						return;
					DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
					oOverTime = dateFormat.parseDateTime(sOverTime);
					
					AppGlobal.handleOverCookingTimeTable(iOutletId, sTableKey, oOverTime, false);
					break;
				
				case MQGlobalInfoSynchronizeProtocol.Action.REMOVE:
					// Check whether below information exist
					iOutletId = oCommandJSONObject.optInt(MQGlobalInfoSynchronizeProtocol.Content.OUTLET_ID, 0);
					sTableKey = oCommandJSONObject.optString(MQGlobalInfoSynchronizeProtocol.Content.TABLE_KEY, "");
					if(iOutletId == 0)
						return;
					else 
						AppGlobal.handleOverCookingTimeTable(iOutletId, sTableKey, null, true);
					break;
				
				default:
					// Incorrect action
					break;
				}
			}
			
		} catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
	}
}