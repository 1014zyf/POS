package app.model;

import java.util.ArrayList;
import java.util.List;

public class PosActionLogList {
	private List<PosActionLog> m_oPosActionLogList;
	
	public PosActionLogList() {
		m_oPosActionLogList = new ArrayList<PosActionLog>();
	}
	
	// add new action log object to list
	public void add(PosActionLog oNewActionLog) {
		m_oPosActionLogList.add(oNewActionLog);
	}
	
	// number of existing action log
	public int size() {
		return m_oPosActionLogList.size();
	}
	
	public boolean addUpdateActionLogs() {
		PosActionLog oTmpActionLog = new PosActionLog();
		return oTmpActionLog.addUpdateWithMutlipleRecord(m_oPosActionLogList);
	}
	
	// cleanup the list
	public void cleanupList() {
		m_oPosActionLogList.clear();
	}
	
	
}
