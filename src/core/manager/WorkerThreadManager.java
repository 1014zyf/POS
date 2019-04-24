package core.manager;

import java.util.concurrent.ConcurrentHashMap;

import core.externallib.WorkerThread;

public class WorkerThreadManager {
	ConcurrentHashMap<WorkerThread, WorkerThread> m_oWorkerThreadList;

	public WorkerThreadManager() {
		m_oWorkerThreadList = new ConcurrentHashMap<WorkerThread, WorkerThread>();
	}
	
	public WorkerThread addThread(Object oThreadObject, String sMethodName, Object[] oParameters) {
		WorkerThread oAppThread = new WorkerThread(oThreadObject, sMethodName, oParameters);
		m_oWorkerThreadList.put(oAppThread, oAppThread);
		oAppThread.startThread();
		
		return oAppThread;
	}
	
	public void stopAllThread() {
		for (WorkerThread workerThread : m_oWorkerThreadList.values()) {
			workerThread.stop();
		}
	}
}
