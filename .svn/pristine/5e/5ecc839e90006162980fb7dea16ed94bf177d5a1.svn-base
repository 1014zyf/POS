package app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ScheduledFuture;

import externallib.IniReader;

public class AppBackgroundScheduleJob implements Runnable {
	private String sTaskType = "";
	private int iId = 0;
	private String pollingUrl = "";
	private String pollingIp = "";
	private String sLastUpdate = "default"; // "default" to force update for the
											// first time
	private long lLastHandleTimeInMillis = 0;
	private boolean bNeedUpdate = false;
	private boolean bStop = false;
	private ScheduledFuture<?> oScheduleFuture = null;
	private Boolean bIsLocalDataFolder = null;

	public static String TYPE_UPDATE_TABLE_STATUS = "update_table_status";
	public static String TYPE_UPDATE_SOLDOUT_STATUS = "update_soldout_status";
	public static String TYPE_UPDATE_ITEM_STOCK_QTY_STATUS = "update_item_stock_qty_status";
	public static String TYPE_UPDATE_IP_STATUS = "update_ip_status";
	public static String TYPE_UPDATE_MENU_ITEM = "update_menu_item";

	public AppBackgroundScheduleJob(String sTaskType, int iId) {
		this.sTaskType = sTaskType;
		this.iId = iId;

		try {
			String sWSDL = "";
			StringBuilder sTempString = new StringBuilder();
			IniReader iniReader = new IniReader("cfg/config.ini");
			if (AppGlobal.g_oFuncSmartStation.isStandaloneRole())
				sWSDL = iniReader.getValue("connection", "db_wsdl_standalone");
			else
				sWSDL = iniReader.getValue("connection", "db_wsdl");

			if (sWSDL.contains("http://")) {
				sWSDL = sWSDL.replace("http://", "");
				sTempString.append("http://");
			} else if (sWSDL.contains("https://")) {
				sWSDL = sWSDL.replace("https://", "");
				sTempString.append("https://");
			}

			this.pollingIp = sWSDL;
			pollingIp = pollingIp.replace("/hero/chi/http_interface/", "");
			pollingIp = pollingIp.replace("/hero/eng/http_interface/", "");
			pollingIp = pollingIp.replace("/hero/cn/http_interface/", "");
			pollingIp = pollingIp.replace("/hero/jpn/http_interface/", "");
			pollingIp = pollingIp.replace("/hero/kor/http_interface/", "");
			pollingIp = pollingIp.replace("http_interface/", "");

			int iTokenCount = 0;
			StringTokenizer oStrTok = new StringTokenizer(sWSDL, "/");
			while (oStrTok.hasMoreTokens()) {
				String sParams = oStrTok.nextToken();
				if (sParams.equals("http_interface") == false && sParams.equals("ws_interface") == false
						&& sParams.equals("chi") == false && sParams.equals("eng") == false) {
					if (iTokenCount != 0 && sTempString.length() > 0) {
						sTempString.append("/");
					}
					sTempString.append(sParams);
					iTokenCount++;
				} else
					break;
			}

			this.pollingUrl = sTempString.toString();
		} catch (IOException e) {
			this.pollingUrl = "";
		}
	}

	private static String convertStreamToString(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		boolean firstLine = true;

		while ((line = reader.readLine()) != null) {
			if (!firstLine)
				sb.append('\n');
			sb.append(line);
			firstLine = false;
		}

		is.close();

		return sb.toString();
	}

	private String getStatusLocally(int outletId) throws IOException {
		String sFile = "";
		if (this.sTaskType.equals(AppBackgroundScheduleJob.TYPE_UPDATE_TABLE_STATUS))
			sFile = AppGlobal.g_sSystemDataPath + "\\www\\pos_pollings\\alerts\\table_status_" + outletId + ".txt";
		else if (this.sTaskType.equals(AppBackgroundScheduleJob.TYPE_UPDATE_SOLDOUT_STATUS))
			sFile = AppGlobal.g_sSystemDataPath + "\\www\\pos_pollings\\alerts\\sold_out_items\\sold_out_" + outletId + ".txt";
		else if (this.sTaskType.equals(AppBackgroundScheduleJob.TYPE_UPDATE_ITEM_STOCK_QTY_STATUS))
			sFile = AppGlobal.g_sSystemDataPath + "\\www\\pos_pollings\\alerts\\item_stock_qty_" + outletId + ".txt";
		else if (this.sTaskType.equals(AppBackgroundScheduleJob.TYPE_UPDATE_MENU_ITEM))
			sFile = AppGlobal.g_sSystemDataPath + "\\www\\menu_pollings\\alerts\\menu_item.txt";
		InputStream is = new FileInputStream(sFile);
		String sLocalContent = convertStreamToString(is);
		is.close();
		return sLocalContent;
	}

	private String getStatusHttp(int outletId) throws IOException {
		String sURL = "";
		if (this.sTaskType.equals(AppBackgroundScheduleJob.TYPE_UPDATE_TABLE_STATUS))
			sURL = this.pollingUrl + AppGlobal.g_sSystemDataUrl + "/pos_pollings/alerts/table_status_" + outletId + ".txt";
		else if (this.sTaskType.equals(AppBackgroundScheduleJob.TYPE_UPDATE_SOLDOUT_STATUS))
			sURL = this.pollingUrl + AppGlobal.g_sSystemDataUrl + "/pos_pollings/alerts/sold_out_items/sold_out_" + outletId + ".txt";
		else if (this.sTaskType.equals(AppBackgroundScheduleJob.TYPE_UPDATE_ITEM_STOCK_QTY_STATUS))
			sURL = this.pollingUrl + AppGlobal.g_sSystemDataUrl + "/pos_pollings/alerts/item_stock_qty_" + outletId + ".txt";
		else if (this.sTaskType.equals(AppBackgroundScheduleJob.TYPE_UPDATE_MENU_ITEM))
			sURL = this.pollingUrl + AppGlobal.g_sSystemDataUrl + "/menu_pollings/alerts/menu_item.txt";
		URL url = new URL(sURL);
		URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();
		String sHttpContent = convertStreamToString(is);
		is.close();
		return sHttpContent;
	}

	private boolean isReachable(final String ip, final long timeout) {
		String result;
		try {
			result = execCmdForResult("ping -n 1 -w " + timeout + " " + ip);
			if (result == null) {
//				AppGlobal.writeErrorLog(this.getClass().getSimpleName(),
//						new Exception().getStackTrace()[0].getMethodName(), "", "",
//						"Ping " + ip + " null result");
				return false;
			}
			result = result.toLowerCase();
			if (!result.contains("ttl=")) {
//				AppGlobal.writeErrorLog(this.getClass().getSimpleName(),
//						new Exception().getStackTrace()[0].getMethodName(), "", "",
//						"Ping " + ip + " disconnected");
				return false;
			}
		} catch (Exception e) {
//			AppGlobal.writeErrorLog(this.getClass().getSimpleName(),
//					new Exception().getStackTrace()[0].getMethodName(), "", "",
//					"Ping " + ip + " exeception: "+e.toString());
			return false;
		}
		
		return true;
	}

	private String execCmdForResult(String cmd) throws IOException {
		cmd = "cmd /c " + cmd;

		String line = "";
		String result = "";
		Process p = execCmdForProcess(cmd);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		while ((line = bufferedReader.readLine()) != null) {
			result += (line + '\n');
		}
		bufferedReader.close();
		return result;
	}

	private Process execCmdForProcess(String cmd) throws IOException {
		return Runtime.getRuntime().exec(cmd);
	}

	public void run() {
		if (this.bStop)
			return;
		
		if (this.sTaskType.equals(AppBackgroundScheduleJob.TYPE_UPDATE_IP_STATUS)) {
			int iMaxCount = 5;
			for (int i = 0; i < iMaxCount; i++) {
				boolean bConnect = isReachable(this.pollingIp, 1000);
				if (bConnect) {	// Success
					AppGlobal.g_bConnectSuccess = bConnect;
					break;
				}
				
				if (i == iMaxCount - 1) {	// last time
					AppGlobal.g_bConnectSuccess = bConnect;
					return;
				}
			}
		}
		else {
			if (this.pollingUrl.isEmpty()) {
				setNeedUpdate(true);
				return;
			}
			
			// Test if support local read, only test once
			if (bIsLocalDataFolder == null) {
				// If content read from local same as http, then should be same file
				// read through http takes priority
				String sLocalContent;
				try {
					sLocalContent = getStatusLocally(iId);
				} catch (IOException e) {
					sLocalContent = null;
				}
				
				String sHttpContent;
				try {
					sHttpContent = getStatusHttp(iId);
				} catch (IOException e) {
					sHttpContent = null;
				}
				
				if (sLocalContent != null && !sLocalContent.isEmpty() && sLocalContent.contentEquals(sHttpContent))
					bIsLocalDataFolder = true;
				else
					bIsLocalDataFolder = false;
			}
			
			// Get flag content
			String sContent = "";
			try {
				if (bIsLocalDataFolder)
					sContent = getStatusLocally(iId);
				else
					sContent = getStatusHttp(iId);
				
				// Log in error to show recover from error
				if (sLastUpdate.isEmpty())
					AppGlobal.writeErrorLog(this.getClass().getSimpleName(),
							new Exception().getStackTrace()[0].getMethodName(), "", "",
							"Success to get " + this.sTaskType + " pollings alert");
			} catch (IOException e) {
				if (!sLastUpdate.isEmpty()) {
					// Log error for first error only
					AppGlobal.stack2Log(e);
					AppGlobal.writeErrorLog(this.getClass().getSimpleName(),
							new Exception().getStackTrace()[0].getMethodName(), "", "",
							"Fail to get " + this.sTaskType + " pollings alert");
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
				}
			}
			
			// Handle flag content
			if (sContent.isEmpty()) {
				// Cannot read from flag, force update
				setNeedUpdate(true);
			} else if (!sLastUpdate.equals(sContent)) {
				// Flag updated, update
				setNeedUpdate(true);
				
				if (this.sTaskType.equals(AppBackgroundScheduleJob.TYPE_UPDATE_SOLDOUT_STATUS)) {
					// Update all active clients with outlet ID = current ID
					for (Map.Entry<Long, ClsActiveClient> entry : AppGlobal.g_lCurrentConnectClientList.entrySet()) {
						ClsActiveClient oActiveClient = entry.getValue();
						if (oActiveClient.getCurrentOutletId() == iId)
							oActiveClient.setNeedUpdateSoldoutStatus(true);
					}
				} else if (this.sTaskType.equals(AppBackgroundScheduleJob.TYPE_UPDATE_ITEM_STOCK_QTY_STATUS)) {
					// Update all active clients with outlet ID = current ID
					for (Map.Entry<Long, ClsActiveClient> entry : AppGlobal.g_lCurrentConnectClientList.entrySet()) {
						ClsActiveClient oActiveClient = entry.getValue();
						if (oActiveClient.getCurrentOutletId() == iId)
							oActiveClient.setNeedUpdateItemStockQtyStatus(true);
					}
				}
			}
			
			sLastUpdate = sContent;
		}
	}

	public String getTaskType() {
		return this.sTaskType;
	}

	public int getId() {
		return this.iId;
	}

	synchronized public String getLastUpdate() {
		return this.sLastUpdate;
	}

	synchronized public boolean getNeedUpdate() {
		// Get the need update flag in this >> Synchronized << function
		return this.bNeedUpdate;
	}

	synchronized public void setNeedUpdate(boolean bNeedUpdate) {
		this.bNeedUpdate = bNeedUpdate;
		
		if (!bNeedUpdate)
			// Need update flag is set to false mean the update was handled
			// Store the last handle time for further usage
			lLastHandleTimeInMillis = System.currentTimeMillis();
	}

	synchronized public long getLastHandleTimeInMillis() {
		return lLastHandleTimeInMillis;
	}
	
	synchronized public boolean isStop() {
		return this.bStop;
	}

	synchronized public void setStop(boolean bStop) {
		this.bStop = bStop;
	}

	public void setScheduledFuture(ScheduledFuture<?> oScheduledFuture) {
		this.oScheduleFuture = oScheduledFuture;
	}

	public ScheduledFuture<?> getScheduledFuture() {
		return this.oScheduleFuture;
	}
}
