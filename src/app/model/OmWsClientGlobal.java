package app.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import app.controller.VariableManager;

public class OmWsClientGlobal {
	public static VariableManager<OmWsClient> g_oWsClient;
	
	public static String stackToString(Exception e){
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
	
	//change DateTime object to string for database insertion/update
	public static String dateTimeToString (DateTime oDateTime) {
		if (oDateTime == null)
			System.out.println("null");
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		String timeString = fmt.print(oDateTime);

		return timeString;
	}
	
	public static void writeErrorLog(String sClass, String sMethod, String sLogin, String sLog){
		
		DateTime today = new DateTime();
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);
		
		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/hero_om_err." + sCurrentMonth;

		try{
			// Create file 
			FileWriter fstream = new FileWriter(sFilename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			sContent.append(sCurrentTime);
			if(sLogin.length() > 0){
				sContent.append(" User:");
				sContent.append(sLogin);
			}
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
		  e.printStackTrace();
	  }
	}
	
	public static void writePacketLog(boolean bTo, String sPacket){
		Calendar cCalendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd hh:mm:ss", Locale.ENGLISH);
		SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.ENGLISH);
		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/hero_om_pkg." + monthFormat.format(cCalendar.getTime());

		try{
			// Create file 
			FileWriter fstream = new FileWriter(sFilename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			sContent.append(dateFormat.format(cCalendar.getTime()));
			sContent.append(" ");
			if(bTo)
				sContent.append(">>> ");
			else {
				sContent.append("<<< ");
			}
			sContent.append(sPacket);
			sContent.append("\n");
			
			out.write(sContent.toString());
			
			//Close the output stream
			out.close();
			fstream.close();
	  }catch (Exception e){//Catch exception if any
		  e.printStackTrace();
	  }
	}
}
