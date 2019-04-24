package app;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;

import externallib.TCPLib;
import lang.LangResource;
import om.OmWsClient;
import om.OmWsClientGlobal;
import om.PosConfig;
import om.PosInterfaceConfigList;
import om.PosItemRemindRuleList;
import om.WohAwardSettingList;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUITerm;

public class AppThread implements Runnable {

	private AppThreadManager m_oParentThreadManager;
	private Object m_oThreadObject;
	private String m_sMethodName;
	private Class[] m_cParameterTypes;
	private Object[] m_oParameters;
	private Object m_oResult;
	
	private TCPLib m_oTCPLib;
	private Selector m_oSelectorForTCP;
	private SelectionKey m_oSelectionKeyForTCP;
	
	private LangResource m_oLangResource;
	private Integer m_iCurrentLangIndex;
	private String m_sDisplayMode;
	private OmWsClient m_oWsClient;
	private OmWsClient m_oWsClientForHq;
	private VirtualUITerm m_oVirtualUITerm;
	private FuncMenu m_oFuncMenu;
	private FuncUser m_oFuncUser;
	private FuncStation m_oFuncStation;
	private FuncOutlet m_oFuncOutlet;
	private FuncMixAndMatch m_oFuncMixAndMatch;
	private FuncActionLog m_oActionLog;
	private VirtualUIBasicElement m_oDeviceManagerElement;
	private PosInterfaceConfigList m_oPosInterfaceConfigList;
	private WohAwardSettingList m_oWohAwardSettingList;
	private HashMap<String, HashMap<String, PosConfig>> m_oPosConfigList;
	private PosItemRemindRuleList m_oPosItemRemindRuleList;
	private FuncOctopus m_oFuncOctopus;
	private String m_sResultForAutoFunction;
	
	public AppThread(AppThreadManager oParentThreadManager, Object oThreadObject, String sMethodName, Object[] oParameters){
		m_oParentThreadManager = oParentThreadManager;
		m_oThreadObject = oThreadObject;
		m_sMethodName = sMethodName;
		
		m_oTCPLib = AppGlobal.g_oTCP.get();
		m_oSelectorForTCP = AppGlobal.g_oSelectorForTCP.get();
		m_oSelectionKeyForTCP = AppGlobal.g_oSelectorKeyForTCP.get();
		
		m_oWsClient = OmWsClientGlobal.g_oWsClient.get();
		m_oWsClientForHq = OmWsClientGlobal.g_oWsClientForHq.get();
		m_oLangResource = AppGlobal.g_oLang.get();
		m_iCurrentLangIndex = AppGlobal.g_oCurrentLangIndex.get();
		m_sDisplayMode = AppGlobal.g_sDisplayMode.get();
		m_oVirtualUITerm = AppGlobal.g_oTerm.get();
		m_oFuncMenu = AppGlobal.g_oFuncMenu.get();
		m_oFuncUser = AppGlobal.g_oFuncUser.get();
		m_oFuncStation = AppGlobal.g_oFuncStation.get();
		m_oFuncOutlet = AppGlobal.g_oFuncOutlet.get();
		m_oFuncMixAndMatch = AppGlobal.g_oFuncMixAndMatch.get();
		m_oActionLog = AppGlobal.g_oActionLog.get();
		m_oDeviceManagerElement = AppGlobal.g_oDeviceManagerElement.get();
		m_oPosInterfaceConfigList = AppGlobal.g_oPosInterfaceConfigList.get();
		m_oWohAwardSettingList = AppGlobal.g_oWohAwardSettingList.get();
		m_oPosConfigList = AppGlobal.g_oPosConfigList.get();
		m_oPosItemRemindRuleList = AppGlobal.g_oPosItemRemindRuleList.get();
		m_oFuncOctopus = AppGlobal.g_oFuncOctopus.get();
		m_sResultForAutoFunction = AppGlobal.g_sResultForAutoFunction.get();
				
		Class oClass = m_oThreadObject.getClass();

		// Get method parameter types by method name
		for(final Method oMethod : oClass.getDeclaredMethods()){
            if(oMethod.getName().equals(sMethodName)){
            	m_cParameterTypes = oMethod.getParameterTypes();
            	break;
            }
        }		

		m_oParameters = oParameters;
		m_oResult = null;
	}
	
	@Override
	public void run() {
		Class oClass;// = null;

		// Initialize the language
		AppGlobal.g_oLang.set(m_oLangResource);
		AppGlobal.g_oCurrentLangIndex.set(m_iCurrentLangIndex);
		
		// Initialize the display mode
		AppGlobal.g_sDisplayMode.set(m_sDisplayMode);

		// Initialize the menu item and menu cache
		AppGlobal.g_oFuncMenu.set(m_oFuncMenu);
		
		// Initialize the user
		AppGlobal.g_oFuncUser.set(m_oFuncUser);
		
		// Initialize the station
		AppGlobal.g_oFuncStation.set(m_oFuncStation);
		
		// Initialize the outlet
		AppGlobal.g_oFuncOutlet.set(m_oFuncOutlet);
		
		// Initialize the mix and match function
		AppGlobal.g_oFuncMixAndMatch.set(m_oFuncMixAndMatch);
		
		// Initialize the action log list
		AppGlobal.g_oActionLog.set(m_oActionLog);
		
		// Initialize the term
		AppGlobal.g_oTerm.set(m_oVirtualUITerm);
		
		// Initialize the web service client
		OmWsClientGlobal.g_oWsClient.set(m_oWsClient);
		
		OmWsClientGlobal.g_oWsClientForHq.set(m_oWsClientForHq);
		
		// Initialize TCP object
		AppGlobal.g_oTCP.set(m_oTCPLib);
		
		// Initialize the selector for TCP connection
		AppGlobal.g_oSelectorForTCP.set(m_oSelectorForTCP);
		AppGlobal.g_oSelectorKeyForTCP.set(m_oSelectionKeyForTCP);
		
		// Initialize device manager connection element
		AppGlobal.g_oDeviceManagerElement.set(m_oDeviceManagerElement);

		AppGlobal.g_oPosInterfaceConfigList.set(m_oPosInterfaceConfigList);
		
		AppGlobal.g_oWohAwardSettingList.set(m_oWohAwardSettingList);
		
		// Initialize item remind list
		AppGlobal.g_oPosItemRemindRuleList.set(m_oPosItemRemindRuleList);
		
		// Initialize pos config list
		AppGlobal.g_oPosConfigList.set(m_oPosConfigList);
		
		// Initialize the octopus
		AppGlobal.g_oFuncOctopus.set(m_oFuncOctopus);
		
		// Initialize the result for auto function
		AppGlobal.g_sResultForAutoFunction.set(m_sResultForAutoFunction);
		
		oClass = m_oThreadObject.getClass();
		
		//Class[] parameterTypes = new Class[]{String[].class};
		final Method oMethod;
		try {
			oMethod = oClass.getDeclaredMethod(m_sMethodName, m_cParameterTypes);
			
			AccessController.doPrivileged(new PrivilegedAction() {
				public Object run() {
					oMethod.setAccessible(true);
					return null; // nothing to return
				}
			});
			
			try {
				
				m_oResult = oMethod.invoke(m_oThreadObject, m_oParameters);

			} catch (IllegalArgumentException e) {
				AppGlobal.stack2Log(e);
			} catch (IllegalAccessException e) {
				AppGlobal.stack2Log(e);
			} catch (InvocationTargetException e) {
				// Stop by parent thread
			} 
			
		} catch (SecurityException e) {
			AppGlobal.stack2Log(e);
		} catch (NoSuchMethodException e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	public Object getResult(){
		return m_oResult;
	}

}
