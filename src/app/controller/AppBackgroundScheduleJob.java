package app.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;
import java.util.concurrent.ScheduledFuture;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import externallib.IniReader;

public class AppBackgroundScheduleJob implements Runnable {
	private String sTaskType = "";
	private int iId = 0;
	private String pollingUrl = "";
	private DateTime oLastUpdate = null;
	private boolean bNeedUpdate = false;
	private boolean bStop = false;
	private ScheduledFuture<?> oScheduleFuture = null;
	
	public static String TYPE_UPDATE_TABLE_STATUS = "update_table_status";
	
	public AppBackgroundScheduleJob(String sTaskType, int iId) {
		this.sTaskType = sTaskType;
		this.iId = iId;
		
		try {
			String sWSDL = "";
			StringBuilder sTempString = new StringBuilder();
			IniReader iniReader = new IniReader("cfg/config.ini");
			sWSDL = iniReader.getValue("connection", "db_wsdl");
			
			if(sWSDL.contains("http://")) {
				sWSDL = sWSDL.replace("http://", "");
				sTempString.append("http://");
			}else if(sWSDL.contains("https://")) {
				sWSDL = sWSDL.replace("https://", "");
				sTempString.append("https://");
			}
			
			int iTokenCount = 0;
			StringTokenizer oStrTok = new StringTokenizer(sWSDL, "/");
			while(oStrTok.hasMoreTokens()) {
				String sParams = oStrTok.nextToken();
				if(sParams.equals("http_interface") == false &&
						sParams.equals("ws_interface") == false &&
						sParams.equals("chi") == false &&
						sParams.equals("eng") == false) {
					if(iTokenCount != 0 && sTempString.length() > 0){
						sTempString.append("/");
					}
					sTempString.append(sParams);
					iTokenCount++;
				}else
					break;
			}
			
			this.pollingUrl = sTempString.toString();
		}catch (IOException e) {
			this.pollingUrl = "";
		}
	}
	
	public void run() {
		if(this.bStop)
			return;
		
		if (this.sTaskType.equals(AppBackgroundScheduleJob.TYPE_UPDATE_TABLE_STATUS)) {
			if(this.pollingUrl.isEmpty() == false) {
				try {
					URL url = new URL(this.pollingUrl+AppGlobal.g_sSystemDataUrl+"/pos_pollings/alerts/table_status_"+this.iId+".txt");
					URLConnection connection = url.openConnection();
					BufferedReader oBufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					String sContent = oBufferedReader.readLine();
					
					DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
					DateTime oNewTime = oFormatter.parseDateTime(sContent);
						
					if(oLastUpdate == null || oLastUpdate.compareTo(oNewTime) < 0) {
						oLastUpdate = oNewTime;
						bNeedUpdate = true;
					}
	
				}catch (Exception e) {
					// no file exist, stop the schedule job
					AppGlobal.removeBackgroundScheduleTask(this.sTaskType+"_"+this.iId);
				}
			}else
				bNeedUpdate = true;
			
		}
	}
	
	public String getTaskType() {
		return this.sTaskType;
	}
	
	public int getId() {
		return this.iId;
	}
	
	synchronized public String getLastUpdate() {
		if(oLastUpdate == null)
			return "";
		else
			return this.oLastUpdate.toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
	}
	
	synchronized public boolean isNeedUpdate() {
		return this.bNeedUpdate;
	}
	
	synchronized public void setNeedUpdate(boolean bNeedUpdate) {
		this.bNeedUpdate = bNeedUpdate;
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
