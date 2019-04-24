package core.externallib;

import core.manager.AppThreadManager;

public class AppThread implements Runnable {

	private AppThreadManager m_oParentThreadManager;
	private int m_iThreadId;
	private Object m_oThreadObject;
	private String m_sMethodName;
//	private Class[] m_cParameterTypes;
	private Object[] m_oParameters;
	private Object m_oResult;
	
	private WorkerThread m_oWorkerThread = null;
	
	public AppThread(AppThreadManager oParentThreadManager, int iThreadId, Object oThreadObject, String sMethodName, Object[] oParameters){
		m_oParentThreadManager = oParentThreadManager;
		m_iThreadId = iThreadId;
		m_oThreadObject = oThreadObject;
		m_sMethodName = sMethodName;
		
//		Class oClass = m_oThreadObject.getClass();
		
		// Get method parameter types by method name
//		for(final Method oMethod : oClass.getDeclaredMethods()){
//            if(oMethod.getName().equals(sMethodName)){
//            	m_cParameterTypes = oMethod.getParameterTypes();
//            	break;
//            }
//        }

		m_oParameters = oParameters;
		m_oResult = null;
	}
	
	@Override
	public void run() {
		m_oWorkerThread = new WorkerThread(m_oThreadObject, m_sMethodName, m_oParameters);
		m_oWorkerThread.startThread();
		m_oResult = m_oWorkerThread.getResult(true);
//		Class oClass;// = null;
//
//		oClass = m_oThreadObject.getClass();
//		
//		//Class[] parameterTypes = new Class[]{String[].class};
//		final Method oMethod;
//		try {
//			oMethod = oClass.getDeclaredMethod(m_sMethodName, m_cParameterTypes);
//			
//			AccessController.doPrivileged(new PrivilegedAction() {
//				public Object run() {
//					oMethod.setAccessible(true);
//					return null; // nothing to return
//				}
//			});
//			
//			try {
//				
//				m_oResult = oMethod.invoke(m_oThreadObject, m_oParameters);
//
//			} catch (IllegalArgumentException e) {
//				LoggingManager.stack2Log(e);
//			} catch (IllegalAccessException e) {
//				LoggingManager.stack2Log(e);
//			} catch (InvocationTargetException e) {
//				// Stop by parent thread
//				LoggingManager.stack2Log(e);
//			} 
//			
//		} catch (SecurityException e) {
//			LoggingManager.stack2Log(e);
//		} catch (NoSuchMethodException e) {
//			LoggingManager.stack2Log(e);
//		}
		
		m_oParentThreadManager.removeThread(m_iThreadId);
	}
	
	public Object getResult(){
		return m_oResult;
	}

	public void stop() {
		m_oWorkerThread.stop();
	}
}
