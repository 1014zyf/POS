package app;

import java.util.HashMap;

import lang.LangResource;
import om.OmWsClient;
import om.OmWsClientGlobal;
import om.PosConfig;
import om.PosInterfaceConfigList;
import om.PosItemRemindRuleList;
import om.WohAwardSettingList;
import virtualui.VirtualUIForwarder;
import virtualui.VirtualUITerm;
import externallib.TCPLib;

public class Main {
	
	public static void main(String[] args) {
		int iPortNo = 6001;
		String sClientUDID = "";
		String sLoginID = "";
		String sLoginPassword = "";
		String sLoginCardNo = "";
		String sDisplayMode = "";
		String sAccessToken = "";
		int iSwitchOutletId = 0;
		
		if(args.length > 0){
			try{
				iPortNo = Integer.parseInt(args[0]);
//				System.out.println("-=-=--=-=-=-=-=-=-=-=" + iPortNo);
				if(args.length >= 2){
					sClientUDID = args[1];
//					System.out.println("-=-=--=-=-=-=-=-=-=-=" + sClientUDID);
					if(args.length >= 3){
						sLoginID = args[2];
//						System.out.println("-=-=--=-=-=-=-=-=-=-=-=" + sLoginID);
						if(args.length >= 4){
							sLoginPassword = args[3];
//							System.out.println("-=-=--=-=-=-=-=-=-=-=-=-=" + sLoginPassword);
							if(args.length >= 5){
								sLoginCardNo = args[4];
//								System.out.println("-=-=--=-=-=-=-=-=-=-=-=-=" + sLoginPassword);
								if(args.length >= 6){
									sDisplayMode = args[5];
//									System.out.println("-=-=--=-=-=-=-=-=-=-=-=-=" + sDisplayMode);
									if(args.length >= 7){
										sAccessToken = args[6];
//										System.out.println("-=-=--=-=-=-=-=-=-=-=-=-=" + sAccessToken);
										if(args.length >= 8){
											iSwitchOutletId = Integer.parseInt(args[7]);
//											System.out.println("-=-=--=-=-=-=-=-=-=-=-=-=" + iOutletId);
										}
									}
								}
							}
						}
					}
				}
			}catch (Exception e) {
				AppGlobal.stack2Log(e);
			}
		}

		// Un-comment to test code
		//if(testCode())
		//	return;
	
		// Initialize the language
		AppGlobal.g_oLang.set(new LangResource());
		AppGlobal.g_oLang.get().switchLocale("en");

		// Initialize the menu item and menu cache
		AppGlobal.g_oFuncMenu.set(new FuncMenu());
		
		// Initialize the term
		AppGlobal.g_oTerm.set(new VirtualUITerm());
		
		// Initialize the web service client
		OmWsClientGlobal.g_oWsClient.set(new OmWsClient());

		// Initialize the web service client for loyalty
		OmWsClientGlobal.g_oWsClientForHq.set(new OmWsClient());

		// Initialize the language index
		AppGlobal.g_oCurrentLangIndex.set(new Integer(1));
		
		// Initialize the action log
		AppGlobal.g_oActionLog.set(new FuncActionLog());
		
		// Initialize TCP object
		AppGlobal.g_oTCP.set(new TCPLib());
		
		// Initialize device manager connection element
		AppGlobal.g_oDeviceManagerElement.set(new VirtualUIForwarder());
		
		AppGlobal.g_oPosInterfaceConfigList.set(new PosInterfaceConfigList());
		
		AppGlobal.g_oWohAwardSettingList.set(new WohAwardSettingList());
		
		AppGlobal.g_oPosItemRemindRuleList.set(new PosItemRemindRuleList());
		
		AppGlobal.g_oPosConfigList.set(new HashMap<String, HashMap<String, PosConfig>>());
		
		// Initialize the octopus function
		AppGlobal.g_oFuncOctopus.set(new FuncOctopus());
		
		// Initialize the result for auto function
		AppGlobal.g_sResultForAutoFunction.set(AppGlobal.AUTO_FUNCTIONS_RESULT_LIST.fail.name());
		
		// Init the socket
		if (AppGlobal.g_oTerm.get().waitForClient(sClientUDID, iPortNo) == false){

			// Fail to init socket
			
			// Write error log
			
			return;
		}

		// POS operation
		// Show the main form
		FormMain oFormMain = new FormMain(null);
		if(oFormMain.init(sLoginID, sLoginPassword, sLoginCardNo, sDisplayMode, sAccessToken, iSwitchOutletId))
			oFormMain.show();
	
		// Quit program
		AppGlobal.g_oTerm.get().closeTerm();
	}

}
