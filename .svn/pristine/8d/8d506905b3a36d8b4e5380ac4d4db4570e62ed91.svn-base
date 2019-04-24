package core.manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import app.AppGlobal;
import core.Core;

public class LoggingManager {
	
	public LoggingManager() {
		init();
	}
	
	public void init() {
		
	}
	
	public void close() {
		
	}
	
	public static void writeAppErrorLog(String sClass, String sMethod, String sSationID, String sLogin, String sLog){
		
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);
		
		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/hero_app_err." + sCurrentMonth;

		try{
			// Create file 
			File file = new File(sFilename);
			file.createNewFile();
			FileWriter fstream = new FileWriter(sFilename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			sContent.append(sCurrentTime);
			sContent.append(" [s:");
			sContent.append(sSationID);
			sContent.append(" u:");
			sContent.append(sLogin);
			sContent.append(", ");
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
			stack2Log(e);
		}
	}
	
	public static void writeOMErrorLog(String sClass, String sMethod, String sLog){		
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss.SSS");
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
			stack2Log(e);
		}
	}
	
	public static void writeLauncherErrorLog(String sClass, String sMethod, String sLog){
		
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);
		
		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/hero_launcher_err." + sCurrentMonth;

		try{
			// Create file 
			File file = new File(sFilename);
			file.createNewFile();
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
			e.printStackTrace();
		}
	}
	
	public static void d(Object... data) {
		if (data != null) {
			if (Core.DEBUGGING) {
				String s = "";
				for (Object o : data)
					s += o;
				
				if (s.length() > 3000)
					for (int i = 0; i < s.length(); i += 3000)
						if (i + 3000 < s.length())
							System.out.println(s.substring(i, i + 3000));
						else
							System.out.println(s.substring(i, s.length()));
				else
					System.out.println(s);
			}
		}
	}
	
	public static void writeActionLog(String sSationID, String sLogin, String sLog){
		
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);

		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/hero_app." + sSationID + "." + sCurrentMonth;

		try{
			// Create file 
			File file = new File(sFilename);
			file.createNewFile();
			FileWriter fstream = new FileWriter(sFilename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			sContent.append(sCurrentTime);
			sContent.append(" [s:");
			sContent.append(sSationID);
			sContent.append(" u:");
			sContent.append(sLogin);
			sContent.append("] ");
			sContent.append(sLog);
			sContent.append("\n");
			
			out.write(sContent.toString());
			
			//Close the output stream
			out.close();
			fstream.close();
		}catch (Exception e){//Catch exception if any
			stack2Log(e);
		}
	}
	

	
	public static void writeDebugLog(String sClass, String sMethod, String sLog){		
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss.SSS");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);
		
		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/hero_app_debug." + sCurrentMonth;

		try{
			// Create file 
			File file = new File(sFilename);
			file.createNewFile();
			FileWriter fstream = new FileWriter(sFilename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			sContent.append(sCurrentTime);
			sContent.append(" [");
			sContent.append(sClass);
			sContent.append(":");
			sContent.append(sMethod);
			sContent.append(":");
			sContent.append(Thread.currentThread().getId());
			sContent.append("] ");
			sContent.append(sLog);
			sContent.append("\n");
			
			out.write(sContent.toString());
			
			//Close the output stream
			out.close();
			fstream.close();
		}catch (Exception e){//Catch exception if any
			stack2Log(e);
		}
	}

	public static void stack2Log(Exception e) {
		
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);
		
		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/hero_app_exception." + sCurrentMonth;
		
		try{
			// Create file 
			File file = new File(sFilename);
			file.createNewFile();
			FileWriter fstream = new FileWriter(file, true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			sContent.append(sCurrentTime);
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			
			sContent.append(" ");
			sContent.append(sw.toString());
			
			out.write(sContent.toString());
			
			//Close the output stream
			out.close();
			fstream.close();
		}catch (Exception e2){//Catch exception if any
			e2.printStackTrace();
		}
	}
}
