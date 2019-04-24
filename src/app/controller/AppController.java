package app.controller;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;

import app.model.OmWsClient;
import app.model.OmWsClientGlobal;
import app.model.PosConfig;
import app.model.PosInterfaceConfigList;
import core.Controller;
import core.Core;
import core.externallib.TCPLib;
import core.lang.LangResource;
import core.manager.ActiveClient;
import core.virtualui.VirtualUIBasicElement;
import core.virtualui.VirtualUIForwarder;
import core.virtualui.VirtualUITerm;

public class AppController extends Controller {
	
	public AppController(Controller oParentController) {
		super(oParentController);
		
		ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient();
		
		//Sunny
		// For compatible
		// Inital all static variables
		AppGlobal.g_oLang = new VariableManager<LangResource>();
		AppGlobal.g_oFuncMenu = new VariableManager<FuncMenu>();
		AppGlobal.g_oTerm = new VariableManager<VirtualUITerm>();
		OmWsClientGlobal.g_oWsClient = new VariableManager<OmWsClient>();
		AppGlobal.g_oTCP = new VariableManager<TCPLib>();
		AppGlobal.g_oSelectorForTCP = new VariableManager<Selector>();
		AppGlobal.g_oSelectorKeyForTCP = new VariableManager<SelectionKey>();
		AppGlobal.g_oCurrentLangIndex = new VariableManager<Integer>();
		AppGlobal.g_sDisplayMode = new VariableManager<String>();
		AppGlobal.g_oFuncStation = new VariableManager<FuncStation>();
		AppGlobal.g_oFuncOutlet = new VariableManager<FuncOutlet>();
		AppGlobal.g_oFuncUser = new VariableManager<FuncUser>();
		AppGlobal.g_oFuncOverride = new VariableManager<FuncOverride>();
		AppGlobal.g_oFuncMixAndMatch = new VariableManager<FuncMixAndMatch>();
		AppGlobal.g_oActionLog = new VariableManager<FuncActionLog>();
		AppGlobal.g_oLang.set(new LangResource());
		// Initialize the web service client
		OmWsClientGlobal.g_oWsClient.set(new OmWsClient());
		AppGlobal.g_oDeviceManagerElement = new VariableManager<VirtualUIBasicElement>();
		AppGlobal.g_oPosInterfaceConfigList = new VariableManager<PosInterfaceConfigList>();
		AppGlobal.g_oPosConfigList = new VariableManager<HashMap<String, HashMap<String, PosConfig>>>();
		AppGlobal.g_oFuncOctopus = new VariableManager<FuncOctopus>();
		
		// Initialize the language
		AppGlobal.g_oLang.set(new LangResource());
		AppGlobal.g_oLang.get().switchLocale("en");

		// Initialize the menu item and menu cache
		AppGlobal.g_oFuncMenu.set(new FuncMenu());
		
		// Initialize the term
		AppGlobal.g_oTerm.set(new VirtualUITerm());
		
		// Initialize the web service client
		OmWsClientGlobal.g_oWsClient.set(new OmWsClient());

		// Initialize the language index
		AppGlobal.g_oCurrentLangIndex.set(new Integer(1));
		
		// Initialize the action log
//		AppGlobal.g_oActionLog.set(new FuncActionLog());
		
		// Initialize TCP object
		// For compatible
		AppGlobal.g_oTCP.set(oActiveClient.g_oConnectionManager.getTCPLib());
		
		// Initialize device manager connection element
		AppGlobal.g_oDeviceManagerElement.set(new VirtualUIForwarder());
		
		AppGlobal.g_oPosInterfaceConfigList.set(new PosInterfaceConfigList());
		
		AppGlobal.g_oPosConfigList.set(new HashMap<String, HashMap<String, PosConfig>>());
		
		// Initialize the octopus function
//		AppGlobal.g_oFuncOctopus.set(new FuncOctopus());
		
//		FormMain oFormMain = new FormMain(this);
//		oFormMain.init(oActiveClient.g_sUserName, oActiveClient.g_sUserPassword, oActiveClient.g_sUserCardNo, oActiveClient.g_sClientDisplayMode);
//		oFormMain.show2();
	}
}
