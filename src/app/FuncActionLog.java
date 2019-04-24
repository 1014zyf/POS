package app;

import java.util.LinkedList;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import om.PosActionLog;
import om.PosActionLogList;

public class FuncActionLog {
	private LinkedList<PosActionLog> m_oActionLogList;
	
	public FuncActionLog() {
		m_oActionLogList = new LinkedList<PosActionLog>();
	}
	
	// Handle action log by key
	synchronized public void handleActionLog(boolean bWait) {
		PosActionLogList oPosActionLogList = new PosActionLogList();
		
		try{
			while(!m_oActionLogList.isEmpty()){
				// Job is found
				PosActionLog oPosActionLog = m_oActionLogList.removeFirst();
				oPosActionLogList.add(oPosActionLog);
			}
	
			if(oPosActionLogList.size() > 0) {
				// *****************************************************************
				// Create thread to save action log
				AppThreadManager oAppThreadManager = new AppThreadManager();
				
				// Add the method to the thread manager
				//Thread 1 : Save Action Logs
				
				oAppThreadManager.addThread(1, oPosActionLogList, "addUpdateActionLogs", null);
				
				// Run the thread
				oAppThreadManager.runThread();
				
				if(bWait)
					// Wait for return
					oAppThreadManager.waitForThread();
			}
		}catch(Exception e){
			AppGlobal.stack2Log(e);
		}
	}
	
	synchronized public void addActionLog(String sKey, String sResult, String sTable, int iUserId, int iShopId, int iOletId, String sBdayId, String sBperId, int iStationId, String sChksId, String sCptyId, String sCitmId, String sCdisId, String sCpayId, String sRemark) {
		try {
			PosActionLog oNewActionLog = new PosActionLog();
			DateTime oCurrentDateTime = AppGlobal.getCurrentTime(false);
			DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
			
			oNewActionLog.setActionLocalTime(oCurrentDateTime);
			oNewActionLog.setActionTime(fmt.print(AppGlobal.convertTimeToUTC(oCurrentDateTime)));
			
			oNewActionLog.setKey(sKey);
			oNewActionLog.setUserId(iUserId);
			oNewActionLog.setActionResult(sResult);
			oNewActionLog.setTable(sTable);
			oNewActionLog.setRecordId(0);
			oNewActionLog.setShopId(iShopId);
			oNewActionLog.setOletId(iOletId);
			oNewActionLog.setBdayId(sBdayId);
			oNewActionLog.setBperId(sBperId);
			oNewActionLog.setStatId(iStationId);
			oNewActionLog.setChksId(sChksId);
			oNewActionLog.setCptyId(sCptyId);
			oNewActionLog.setCitmId(sCitmId);
			oNewActionLog.setCdisId(sCdisId);
			oNewActionLog.setCpayId(sCpayId);
			oNewActionLog.setRemark(sRemark);
			
			m_oActionLogList.addLast(oNewActionLog);
		}catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	public void addActionLogByList(LinkedList<PosActionLog> oActionLogList) {
		for (PosActionLog oActionLog:oActionLogList) {
			m_oActionLogList.addLast(oActionLog);
		}
	}
}
