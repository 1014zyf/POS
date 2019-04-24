package app;

import java.util.HashMap;
import java.util.Map;

public class AppThreadManager {

	private HashMap<Integer, AppThread> m_AppThreadArrayList;
	private HashMap<Integer, Thread> m_ThreadArrayList;

	public AppThreadManager(){
		m_AppThreadArrayList = new HashMap<Integer, AppThread>();
		m_ThreadArrayList = new HashMap<Integer, Thread>();
	}

	public void addThread(int iThreadId, Object oThreadObject, String sMethodName, Object[] oParameters){
		AppThread oAppThread = new AppThread(this, oThreadObject, sMethodName, oParameters);
		m_AppThreadArrayList.put(iThreadId, oAppThread);
	}
	
	public void runThread(){
		for (Map.Entry<Integer, AppThread> entry : m_AppThreadArrayList.entrySet()) {
			Thread oThread = new Thread(entry.getValue()); 
			oThread.start();
			
			m_ThreadArrayList.put(entry.getKey(), oThread);
		}
	}
	
	public void waitForThread(){
		for (Map.Entry<Integer, Thread> entry : m_ThreadArrayList.entrySet()) {
			try {
				Thread oThread = entry.getValue();
				oThread.join();
			} catch (InterruptedException e) {
				AppGlobal.stack2Log(e);
			}
		}
	}
	
	public void stopAllThread(){
		for (Map.Entry<Integer, Thread> entry : m_ThreadArrayList.entrySet()) {
			Thread oThread = entry.getValue();
			oThread.stop();
		}
		
		m_AppThreadArrayList.clear();
		m_ThreadArrayList.clear();
	}
	
	public void removeThread(int iThreadId){		
		if(m_AppThreadArrayList.containsKey(iThreadId) == false)
			return;

		m_AppThreadArrayList.remove(iThreadId);
		m_ThreadArrayList.remove(iThreadId);
	}
	
	public Object getResult(int iThreadId){
		
		if(m_AppThreadArrayList.containsKey(iThreadId) == false)
			return null;
		
		AppThread oAppThread = m_AppThreadArrayList.get(iThreadId);
		return oAppThread.getResult();
	}
	
}
