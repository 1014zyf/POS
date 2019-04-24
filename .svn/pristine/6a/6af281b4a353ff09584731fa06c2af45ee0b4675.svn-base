package core.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import core.externallib.AppThread;

public class AppThreadManager {

	private HashMap<Integer, AppThread> m_AppThreadArrayList;
//	private HashMap<Integer, Thread> m_ThreadArrayList;
	private Integer m_iThreadIdForNewThread;

	public AppThreadManager(){
		m_AppThreadArrayList = new HashMap<Integer, AppThread>();
//		m_ThreadArrayList = new HashMap<Integer, Thread>();
		m_iThreadIdForNewThread = 0;
	}

	public int addThread(Object oThreadObject, String sMethodName, Object[] oParameters){
		// Increment the thread id for new thread
		m_iThreadIdForNewThread++;
		
		AppThread oAppThread = new AppThread(this, m_iThreadIdForNewThread, oThreadObject, sMethodName, oParameters);
		m_AppThreadArrayList.put(m_iThreadIdForNewThread, oAppThread);
		
		return m_iThreadIdForNewThread;
	}
	
	public void runThread(ArrayList<Integer> oThreadIds){
		for(int iThreadId:oThreadIds){
			if(m_AppThreadArrayList.containsKey(iThreadId)){
				m_AppThreadArrayList.get(iThreadId).run();
			}
		}
	}
	
	public void waitForThread(ArrayList<Integer> oThreadIds){
		for(int iThreadId:oThreadIds){
			if(m_AppThreadArrayList.containsKey(iThreadId)){
				AppThread oAppThread = m_AppThreadArrayList.get(iThreadId);
				oAppThread.getResult();
			}
		}
	}
	
	public void stopAllThread(){
		for (Entry<Integer, AppThread> entry : m_AppThreadArrayList.entrySet()) {
			entry.getValue().stop();
		}
		
//		for (Map.Entry<Integer, Thread> entry : m_ThreadArrayList.entrySet()) {
//			Thread oThread = entry.getValue();
//			oThread.stop();
//		}
		
		m_AppThreadArrayList.clear();
//		m_ThreadArrayList.clear();
	}
	
	public void removeThread(int iThreadId){		
		if (!m_AppThreadArrayList.containsKey(iThreadId))
			return;
		
		if(m_AppThreadArrayList.containsKey(iThreadId)){
			AppThread oAppThread = m_AppThreadArrayList.get(iThreadId);
			oAppThread.stop();
		}
		
		m_AppThreadArrayList.remove(iThreadId);
//		m_ThreadArrayList.remove(iThreadId);
	}
	
	public Object getResult(int iThreadId){
		if(!m_AppThreadArrayList.containsKey(iThreadId))
			return null;
		
		AppThread oAppThread = m_AppThreadArrayList.get(iThreadId);
		return oAppThread.getResult();
	}
	
}
