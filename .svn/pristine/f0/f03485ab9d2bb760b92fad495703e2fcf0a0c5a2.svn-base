package core.externallib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

import core.Core;
import core.manager.ActiveClient;
import core.manager.LoggingManager;

public class WorkerThread {
	
	Thread m_oThread = null;
	Object m_oThreadObject;
	String m_sMethodName;
	Object[] m_oParameters;
	
	WorkerThreadResult m_oResult;
	
	public WorkerThread(Object oThreadObject, String sMethodName, Object[] oParameters) {
		m_oThreadObject = oThreadObject;
		m_sMethodName = sMethodName;
		m_oParameters = oParameters;
	}
	
	public boolean isRunning() {
		if (m_oThread != null)
			return true;
		return false;
	}
	
	public boolean startThread() {
		if (isRunning())
			return false;
		
		m_oResult = null;
		final ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient();
		
		m_oThread = new Thread(new Runnable() {
			@Override
			public void run() {
				//	Register current new worker thread to active client
				Core.g_oClientManager.registerCurrentThread(oActiveClient);
				
				Class<? extends Object> oClass = m_oThreadObject.getClass();
				
				//	Get method parameter types by method name
				Class[] cParameterTypes = new Class[0];
				for (Method oMethod : oClass.getDeclaredMethods()){
		            if (oMethod.getName().contentEquals(m_sMethodName)) {
		            	cParameterTypes = oMethod.getParameterTypes();
		            	break;
		            }
		        }	
				
				Object oReturn = null;
				Exception oException = null;
				
				// Start run
				long startTime = System.currentTimeMillis();
				try {
					final Method oMethod = oClass.getDeclaredMethod(m_sMethodName, cParameterTypes);
					
					//	Set method to be accessible
					AccessController.doPrivileged(new PrivilegedAction<Object>() {
						public Object run() {
							oMethod.setAccessible(true);
							return null;
						}
					});
					try {
						oReturn = oMethod.invoke(m_oThreadObject, m_oParameters);
					} 
					catch (IllegalArgumentException e) {
						oException = e;
						LoggingManager.stack2Log(e);
					} 
					catch (IllegalAccessException e) {
						oException = e;
						LoggingManager.stack2Log(e);
					} 
					catch (InvocationTargetException e) {
						oException = e;
						LoggingManager.stack2Log(e);
					}
				} 
				catch (SecurityException e) {
					oException = e;
					LoggingManager.stack2Log(e);
				} 
				catch (NoSuchMethodException e) {
					oException = e;
					LoggingManager.stack2Log(e);
				}
				
				//	Remove worker thread from active client
				Core.g_oClientManager.unregisterCurrentThread();
				
				//	Prepare result
				long timeElapsed = System.currentTimeMillis() - startTime;
				m_oResult = new WorkerThreadResult(oReturn, oException, timeElapsed);
				m_oThread = null;
				
			}
			
		});
		m_oThread.start();
		
		return true;
	}
	
	public WorkerThreadResult getResult(boolean blocking) {
		if (isRunning()) {	//	Still running
			if (!blocking)
				return null;
			
			//	Wait until complete
			try {
				m_oThread.join();
			} 
			catch (InterruptedException e) {
				LoggingManager.stack2Log(e);
			}
		}
		
		return m_oResult;
	}
	
	public void stop() {
		if (!isRunning())
			return;
		
		//	Kill thread
		m_oThread.interrupt();
	}
}
