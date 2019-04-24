package app;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;

public class ClsAlertMessageList {
	private ConcurrentHashMap <Long, ClsAlertMessage> m_oAlertMessageList;
	private AtomicBoolean m_oDeleteLock;
	private long m_lSeqNum = 0;
	private long m_lLastDelMessageSeqNum = 0;
	
	// Message Alive Time 24 Hour = 24*60*60*1000 = 86400000
	private static long m_lMessageAliveTime = 86400000;
	
	public ClsAlertMessageList() {
		m_oAlertMessageList = new ConcurrentHashMap <Long, ClsAlertMessage>();
		m_oDeleteLock = new AtomicBoolean();
	}
	
	public void readByJSON(JSONObject oAlertMessageJSON) {
		// Delete expiry message from the global list
		deleteExpiryMessage();
		
		Set<Long> oMessageSeqNumSet = m_oAlertMessageList.keySet();
		if (oMessageSeqNumSet != null && !oMessageSeqNumSet.isEmpty()){
			long lMaxSeqNum = Collections.max(oMessageSeqNumSet);
			m_lSeqNum =  lMaxSeqNum+1;
		}
		ClsAlertMessage oClsAlertMessage = new ClsAlertMessage();
		oClsAlertMessage.readDataFromJSON(oAlertMessageJSON);
		m_oAlertMessageList.put(m_lSeqNum, oClsAlertMessage);
		
		m_lSeqNum += 1;
	}
	
	public ConcurrentHashMap<Long, ClsAlertMessage> getAlertMessageList() {
		// Delete expiry message from the global list
		deleteExpiryMessage();

		return this.m_oAlertMessageList;
	}
	
	public HashMap<Long, ClsAlertMessage> getMessageList(ArrayList <Long> oSeqList) {
		HashMap<Long, ClsAlertMessage> oCopiedAlertMessageList = new HashMap<Long, ClsAlertMessage>();
		for (Long lMessageSeqNum : oSeqList) {
			if (m_oAlertMessageList.containsKey(lMessageSeqNum)) {
				ClsAlertMessage oTempMessage = new ClsAlertMessage(m_oAlertMessageList.get(lMessageSeqNum));
				oCopiedAlertMessageList.put(lMessageSeqNum ,oTempMessage);
			}
		}
		return oCopiedAlertMessageList;
	}
	
	public long getSmallestSeqNum() {
		Set<Long> oMessageSeqNumSet = m_oAlertMessageList.keySet();
		if (oMessageSeqNumSet != null && !oMessageSeqNumSet.isEmpty())
			 return Collections.min(oMessageSeqNumSet);
		return -1;
	}
	
	public long getNextAvailableSeqNum() {
		return this.m_lSeqNum;
	}

	private void deleteExpiryMessage() {
		if (m_oDeleteLock.compareAndSet(false, true)) {
			try {
				if (!m_oAlertMessageList.containsKey(m_lLastDelMessageSeqNum))
					m_lLastDelMessageSeqNum = getSmallestSeqNum();
				
				while (m_oAlertMessageList.containsKey(m_lLastDelMessageSeqNum)) {
					long lArrivalTime = m_oAlertMessageList.get(m_lLastDelMessageSeqNum).getMessageArrivalTime().getMillis();
					long lCurrentTime = System.currentTimeMillis();
					
					if (lCurrentTime - lArrivalTime > m_lMessageAliveTime) {
						m_oAlertMessageList.remove(m_lLastDelMessageSeqNum);
						m_lLastDelMessageSeqNum++;
					} else
						break;
				}
			} finally {m_oDeleteLock.set(false);}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sString = new StringBuilder();
		for (Entry entry : m_oAlertMessageList.entrySet()) {
			sString.append("{Key : "+entry.getKey().toString());
			sString.append(", ");
			sString.append(entry.getValue().toString());
			sString.append("}, ");
		}
		
		return sString.toString();
	}
}
