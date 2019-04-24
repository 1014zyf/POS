package om;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import app.AppGlobal;

public class OmWsClientGlobal {
	public static ThreadLocal<OmWsClient> g_oWsClient;
	public static ThreadLocal<OmWsClient> g_oWsClientForHq;
	
	public static String stackToString(Exception e){
		return AppGlobal.stackToString(e);
	}
	
	//change DateTime object to string for database insertion/update
	public static String dateTimeToString(DateTime oDateTime) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		return fmt.print(oDateTime);
	}
	
	public static void writeErrorLog(String sClass, String sMethod, String sLogin, String sLog){
		AppGlobal.writeOMErrorLog(sClass, sLogin, sLog);
	}
}
