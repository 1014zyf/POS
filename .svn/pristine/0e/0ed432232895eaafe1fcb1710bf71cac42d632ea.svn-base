package external_prg.auto_daily_close;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;

import lang.LangResource;

import om.OmWsClient;
import om.OmWsClientGlobal;
import om.PosConfig;
import om.PosInterfaceConfigList;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import externallib.HeroSecurity;
import externallib.IniReader;
import externallib.TCPLib;

import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUITerm;

import app.AppGlobal;
import app.FuncActionLog;
import app.FuncMenu;
import app.FuncMixAndMatch;
import app.FuncOctopus;
import app.FuncOutlet;
import app.FuncStation;
import app.FuncUser;

public class Main {
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int iOutletId = 0;
		int iStationId = 0;
		
		try{
			if(args.length > 0){
				iOutletId = Integer.parseInt(args[0]);
				if(args.length > 1)
					iStationId = Integer.parseInt(args[1]);
			}
		}catch (Exception e) {
		}
		
		if(iOutletId == 0){
			// No outlet ID
			writeLog("Main", new Exception().getStackTrace()[0].getMethodName(), "Error: missing outlet ID");
			return;
		}
		
		if(iStationId == 0){
			// No station ID
			writeLog("Main", new Exception().getStackTrace()[0].getMethodName(), "Error: missing station ID");
			return;
		}
		
		// Read setup from the setup file
		IniReader iniReader;
		try {
			iniReader = new IniReader("cfg" + java.io.File.separator + "config.ini");
		} catch (IOException e) {
			writeLog("Main", new Exception().getStackTrace()[0].getMethodName(), "Error: Fail to read " + "cfg" + java.io.File.separator + "config.ini");
			return;
		}
		
		String sLogin = iniReader.getValue("setup", "login");
		if(sLogin == null){
			writeLog("Main", new Exception().getStackTrace()[0].getMethodName(), "Error: Missing login setup");
			return;
		}
		
		String sPassword = iniReader.getValue("setup", "password");
		if(sPassword == null){
			writeLog("Main", new Exception().getStackTrace()[0].getMethodName(), "Error: Missing password setup");
			return;
		}
		
		String sPasswordEncrypted = iniReader.getValue("setup", "encrypted");
		if(sPasswordEncrypted == null){
			// Decrypt the password
			try {
				sPassword = HeroSecurity.decryptString(sPassword);
			} catch (DataLengthException e) {
				AppGlobal.stack2Log(e);
			} catch (IllegalStateException e) {
				AppGlobal.stack2Log(e);
			} catch (InvalidCipherTextException e) {
				AppGlobal.stack2Log(e);
			} catch (UnsupportedEncodingException e) {
				AppGlobal.stack2Log(e);
			}
		}else{			
			if(sPasswordEncrypted.equals("1")){
				// Decrypt the password
				try {
					sPassword = HeroSecurity.decryptString(sPassword);
				} catch (DataLengthException e) {
					AppGlobal.stack2Log(e);
				} catch (IllegalStateException e) {
					AppGlobal.stack2Log(e);
				} catch (InvalidCipherTextException e) {
					AppGlobal.stack2Log(e);
				} catch (UnsupportedEncodingException e) {
					AppGlobal.stack2Log(e);
				}
			}
		}
		
		writeLog("Main", new Exception().getStackTrace()[0].getMethodName(), "Begin: auto daily close for outlet ID: " + iOutletId);
		
		// Inital all static variables
		AppGlobal.g_oLang = new ThreadLocal<LangResource>();
		AppGlobal.g_oFuncMenu = new ThreadLocal<FuncMenu>();
		AppGlobal.g_oTerm = new ThreadLocal<VirtualUITerm>();
		OmWsClientGlobal.g_oWsClient = new ThreadLocal<OmWsClient>();
		OmWsClientGlobal.g_oWsClientForHq = new ThreadLocal<OmWsClient>();
		AppGlobal.g_oTCP = new ThreadLocal<TCPLib>();
		AppGlobal.g_oSelectorForTCP = new ThreadLocal<Selector>();
		AppGlobal.g_oSelectorKeyForTCP = new ThreadLocal<SelectionKey>();
		AppGlobal.g_oCurrentLangIndex = new ThreadLocal<Integer>();
		AppGlobal.g_oFuncStation = new ThreadLocal<FuncStation>();
		AppGlobal.g_oFuncOutlet = new ThreadLocal<FuncOutlet>();
		AppGlobal.g_oFuncUser = new ThreadLocal<FuncUser>();
		AppGlobal.g_oFuncMixAndMatch = new ThreadLocal<FuncMixAndMatch>();
		AppGlobal.g_oActionLog = new ThreadLocal<FuncActionLog>();
		AppGlobal.g_oLang.set(new LangResource());
		AppGlobal.g_oDeviceManagerElement = new ThreadLocal<VirtualUIBasicElement>();
		AppGlobal.g_oPosInterfaceConfigList = new ThreadLocal<PosInterfaceConfigList>();
		AppGlobal.g_oPosConfigList = new ThreadLocal<HashMap<String, HashMap<String, PosConfig>>>();
		AppGlobal.g_oFuncOctopus = new ThreadLocal<FuncOctopus>();
		AppGlobal.g_sResultForAutoFunction = new ThreadLocal<String>();
		
		// Initialize the language
		AppGlobal.g_oLang.set(new LangResource());
		AppGlobal.g_oLang.get().switchLocale("en");
		
		// Initialize the web service client
		OmWsClientGlobal.g_oWsClient.set(new OmWsClient());

		// Initialize the web service client for loyalty
		OmWsClientGlobal.g_oWsClientForHq.set(new OmWsClient());
		
		// Initialize the language index
		AppGlobal.g_oCurrentLangIndex.set(new Integer(1));
		
		// Initialize the result for auto function
		AppGlobal.g_sResultForAutoFunction.set(AppGlobal.AUTO_FUNCTIONS_RESULT_LIST.fail.name());
		
		// Perform login
		FuncUser oFuncUser = new FuncUser();
		String sErrorMessage = oFuncUser.login(sLogin, sPassword, true);
		if(sErrorMessage.length() > 0){
			// login fail
			writeLog("Main", new Exception().getStackTrace()[0].getMethodName(), "Error: fail to login - " + sErrorMessage);
			return;
		}
		
		// Assign the user to global
		AppGlobal.g_oFuncUser.set(oFuncUser);
		
		// Load station setup
		if(loadOutletConfigSetup(iOutletId) == false){
			// Fail to daily close
			return;
		}
		
		// Load outlet setting
		if(loadStationConfigSetup(iStationId) == false){
			// Fail to daily close
			return;
		}
		
		// Perform daily close
		if(dailyClose() == false){			
			// Fail to daily close
			return;
		}
		
		// Start killing all stations
		AppGlobal.createExternalDailyOperationFile(iOutletId, AppGlobal.g_oLang.get()._("daily_close_process_begins"));
		
		AppGlobal.removeExternalDailyOperationFile(iOutletId);
		
		writeLog("Main", new Exception().getStackTrace()[0].getMethodName(), "Success: auto daily close for outlet ID: " + iOutletId);
		
	}
	
	private static boolean loadStationConfigSetup(int iStationId) {
		String sErrMsg = "";
		
		// Load station setup
		AppGlobal.g_oFuncStation.set(new FuncStation());
		if(AppGlobal.g_oFuncStation.get().loadStationById(iStationId) == false){
			// Load station error
			// Prompt error
			sErrMsg = AppGlobal.g_oFuncStation.get().getLastErrorMessage();
			writeLog("Main", new Exception().getStackTrace()[0].getMethodName(), "Error: " + sErrMsg);
			return false;
		}
		
		return true;
	}
	
	private static boolean loadOutletConfigSetup(int iOutletId) {
		String sErrMsg = "";
		
		while(true){			
			// Load outlet setup
			AppGlobal.g_oFuncOutlet.set(new FuncOutlet());
			int iRet = AppGlobal.g_oFuncOutlet.get().loadOutlet(iOutletId, true);
			if(iRet == 1){
				// Load outlet error
				// Prompt error
				sErrMsg = AppGlobal.g_oFuncOutlet.get().getLastErrorMessage();
				writeLog("Main", new Exception().getStackTrace()[0].getMethodName(), "Error: " + sErrMsg);
				return false;
			}
			
			if(iRet == 2){
				// Not yet daily start
				sErrMsg = AppGlobal.g_oFuncOutlet.get().getLastErrorMessage();
				writeLog("Main", new Exception().getStackTrace()[0].getMethodName(), "Error: " + sErrMsg);
				return false;
			}
			
			break;
		}
				
		return true;
	}
	
	// Daily close process
	private static boolean dailyClose(){
		String sErrMsg = "";
		
		// Daily close
		if(AppGlobal.g_oFuncOutlet.get().dailyClose(AppGlobal.g_oFuncOutlet.get().getOutletId()) == false){
			// Fail to daily start
			sErrMsg = AppGlobal.g_oFuncOutlet.get().getLastErrorMessage();
			writeLog("Main", new Exception().getStackTrace()[0].getMethodName(), "Error: " + sErrMsg);
			return false;
		}else{
			// Daily close success
			return true;
		}
	}
	
	public static void writeLog(String sClass, String sMethod, String sLog){
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);
		
		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/hero_auto_daily_close." + sCurrentMonth;
		
		try{
			// Create file 
			FileWriter fstream = new FileWriter(sFilename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			sContent.append(sCurrentTime);
			sContent.append(" [");
			sContent.append(sClass);
			sContent.append(":");
			sContent.append(sMethod);
			sContent.append("] ");
			sContent.append(sLog);
			sContent.append("\n");
			
			out.write(sContent.toString());
		
			//Close the output stream
			out.close();
			fstream.close();
		}catch (Exception e){//Catch exception if any
		}
	}

}
