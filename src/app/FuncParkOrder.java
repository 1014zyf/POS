package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.thoughtworks.xstream.XStream;

import om.PosCheckDiscount;

public class FuncParkOrder {

	public static String PARK_ORDER_FILE_PATH = "park" + java.io.File.separator;
	public static String PARK_ORDER_BY_OUTLET_FILE_PATH = "pos_park_orders" + java.io.File.separator;
	public static String PARK_ORDER_BY_OUTLET = "park_by_outlet";
	public static String PARK_ORDER_BY_STATION = "park_by_station";
	
	public enum PARK_MODE{station,outlet};
	public enum PARK_TYPE{item,discount};
	
	// Retrieve park order
	public ArrayList<String> getParkOrderByStation(int iStationId) {
		ArrayList<String> oParkOrderFileNameList = new ArrayList<String>();
		
		File oParkOrderDirectory = new File(PARK_ORDER_FILE_PATH);
		if(oParkOrderDirectory.exists() == false){
			return null;
		}
		
		// Loop the directory to retrieve parking orders
		File [] files = oParkOrderDirectory.listFiles();
		
		// Sort the files
		Arrays.sort(files, new Comparator<File>(){
			public int compare(File f1, File f2)
			{
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
			} });
		
		for (int i = 0; i < files.length; i++){
			if (files[i].isFile()){ //this line weeds out other directories/folders
				
				String sFileName = files[i].getName();
				String split[] = sFileName.split("_");
				if(split.length < 4)
					continue;
				
				// check if the file is station parking order
				String sFilePrefix = split[0];
				if(sFilePrefix.equals(PARK_MODE.station.name()) == false)
					continue;
				
				if(split.length == 5) {
					String sFilePrefixItem = split[4];
					if(sFilePrefixItem.equals(PARK_TYPE.discount.name()))
						continue;
					}
				
				// Check if the file is created by current station
				int iFileStationId = Integer.parseInt(split[2]);
				if(iFileStationId != iStationId)
					continue;

				oParkOrderFileNameList.add(files[i].getName());
			}
		}
		
		return oParkOrderFileNameList;
	}
	// Retrieve park order
	public ArrayList<String> getParkOrderByOutlet(int iOutletId) {
		ArrayList<String> oParkOrderFileNameList = new ArrayList<String>();
		String sFilePath = checkSystemDataPath() + PARK_ORDER_BY_OUTLET_FILE_PATH + iOutletId + java.io.File.separator;
		File oParkOrderDirectory = new File(sFilePath);
		if(oParkOrderDirectory.exists() == false){
			return null;
		}
		
		// Loop the directory to retrieve parking orders
		File [] files = oParkOrderDirectory.listFiles();
		
		// Sort the files
		Arrays.sort(files, new Comparator<File>(){
			public int compare(File f1, File f2)
			{
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
			} });
		
		for (int i = 0; i < files.length; i++){
			if (files[i].isFile()){ //this line weeds out other directories/folders
				
				String sFileName = files[i].getName();
				String split[] = sFileName.split("_");
				if(split.length < 4)
					continue;
				
				// check if the file is outlet parking order
				String sFilePrefix = split[0];
				if(sFilePrefix.equals(PARK_MODE.outlet.name()) == false)
					continue;
				
				if(split.length == 5) {
					String sFilePrefixItem = split[4];
					if(sFilePrefixItem.equals(PARK_TYPE.discount.name()))
					continue;
				}
				
				oParkOrderFileNameList.add(files[i].getName());	
			}
		}
		
		return oParkOrderFileNameList;
	}
	// Park order
	public boolean saveParkOrderByStation(FuncCheck oFuncCheck, int iStationId) {
		XStream xstream = new XStream();
		String sParkOrderXML = "";
		String sParkOrderCheckXML = "";
		try{
			File oParkOrderDirectory = new File(PARK_ORDER_FILE_PATH);
			if(oParkOrderDirectory.exists() == false){
				// Create folder if not exists
				oParkOrderDirectory.mkdir();
			}
		}catch(Exception e){
			AppGlobal.stack2Log(e);
			return false;
		}
		
		try{
			// Create a object XML
			sParkOrderXML = xstream.toXML(oFuncCheck.getWholeItemList());
			sParkOrderCheckXML = xstream.toXML(oFuncCheck.getCheckDiscountList());
		}catch(Exception e){
			AppGlobal.stack2Log(e);
			return false;
		}
		
		try{
			DateTime today = AppGlobal.getCurrentTime(false);
			DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMddHHmmss");
			String sCurrentTime = dateFormat.print(today);
			
			// Create file name
			// File format : station_yyyyMMddHHmmss_<station ID>_<park employee name>
			String sModifiedUserName = AppGlobal.g_oFuncUser.get().getUserName(AppGlobal.g_oCurrentLangIndex.get()).replace('_', ' ');
			String sFileName = PARK_ORDER_FILE_PATH + PARK_MODE.station.name() + "_" + sCurrentTime + "_" + iStationId + "_" + sModifiedUserName + "_" + PARK_TYPE.item.name();
			String sCheckFileName = PARK_ORDER_FILE_PATH + PARK_MODE.station.name() + "_" + sCurrentTime + "_" + iStationId + "_" + sModifiedUserName + "_" + PARK_TYPE.discount.name();
			
			// Save the XML to file for retrieve
			FileOutputStream fileOut = new FileOutputStream(sFileName);
			FileOutputStream fileOutCheck = new FileOutputStream(sCheckFileName);
			fileOut.write(sParkOrderXML.getBytes());
			fileOutCheck.write(sParkOrderCheckXML.getBytes());
			fileOut.close();
			fileOutCheck.close();
		}catch(IOException e){
			AppGlobal.stack2Log(e);
			return false;
		}
		
		return true;
	}
	
	public boolean loadParkOrderByStation(FuncCheck oFuncCheck, String sParkOrderFileName, boolean bDeleteFile) {
		XStream xstream = new XStream();
		String sParkOrderXML = "", sParkOrderDiscountXML = "";
		
		// Retrieve the item list and discount object from file
		File oParkOrderFile = new File(PARK_ORDER_FILE_PATH + sParkOrderFileName);
		sParkOrderXML = this.getFileContent(oParkOrderFile);
		if (sParkOrderXML == null)
			return false;
		
		String split[] = sParkOrderFileName.split("_");
		String sParkOrderDiscountFile = split[0] + "_" + split[1] + "_" + split[2] + "_" + split[3] + "_" + PARK_TYPE.discount.name();
		File oParkOrderCheckFile = new File(PARK_ORDER_FILE_PATH + sParkOrderDiscountFile);
		if(split.length == 5 && oParkOrderCheckFile.exists()) {
			sParkOrderDiscountXML = this.getFileContent(oParkOrderCheckFile);
			if (sParkOrderDiscountXML == null)
				return false;
		}
		
		try{
			oFuncCheck.setWholeItemList((List<List<FuncCheckItem>>) xstream.fromXML(sParkOrderXML));
			if (!sParkOrderDiscountXML.isEmpty())
				oFuncCheck.setCheckDiscountList((List<PosCheckDiscount>)xstream.fromXML(sParkOrderDiscountXML));
		}catch(Exception e){
			AppGlobal.stack2Log(e);
			return false;
		}
		
		// Delete the file
		if(bDeleteFile){
			try{		
				if(oParkOrderFile.delete() == false){
					// Fail to delete the file
					AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Fail to delete park order file (" + PARK_ORDER_FILE_PATH + sParkOrderFileName + ")");
				}
				if(split.length == 5 && oParkOrderCheckFile.exists()) {
					if(oParkOrderCheckFile.delete() == false){
						// Fail to delete the file
						AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Fail to delete park order file (" + PARK_ORDER_FILE_PATH + sParkOrderFileName + ")");
					}
				}
			}catch(Exception e){
				AppGlobal.stack2Log(e);
			}
		}
		
		return true;
	}
	
	private String checkSystemDataPath(){
		String sSystemDataPath = AppGlobal.g_sSystemDataPath;
		if(!sSystemDataPath.endsWith("/")){
			sSystemDataPath += java.io.File.separator;
		}
		return sSystemDataPath;
	}

	// Park order
	public boolean saveParkOrderByOutlet(FuncCheck oFuncCheck, int iOutletId, int iStationId) {
		XStream xstream = new XStream();
		String sParkOrderXML = "";
		String sParkOrderCheckXML = "";
		String sFilePath = checkSystemDataPath() + PARK_ORDER_BY_OUTLET_FILE_PATH + iOutletId + java.io.File.separator;
		try{
			File oParkOrderDirectory = new File(sFilePath);
			if(oParkOrderDirectory.exists() == false){
				// Create folder if not exists
				oParkOrderDirectory.mkdirs();
			}
		}catch(Exception e){
			AppGlobal.stack2Log(e);
			return false;
		}
		
		try{
			// Create a object XML
			sParkOrderXML = xstream.toXML(oFuncCheck.getWholeItemList());
			sParkOrderCheckXML = xstream.toXML(oFuncCheck.getCheckDiscountList());
		}catch(Exception e){
			AppGlobal.stack2Log(e);
			return false;
		}
		
		try{
			DateTime today = AppGlobal.getCurrentTime(false);
			DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMddHHmmss");
			String sCurrentTime = dateFormat.print(today);
			
			// Create file name
			// File format : outlet_yyyyMMddHHmmss_<outlet ID>_<park employee name>
			String sModifiedUserName = AppGlobal.g_oFuncUser.get().getUserName(AppGlobal.g_oCurrentLangIndex.get()).replace('_', ' ');
			String sFileName = PARK_MODE.outlet.name() + "_" + sCurrentTime + "_" + iStationId + "_" + sModifiedUserName + "_" + PARK_TYPE.item.name();
			String sCheckFileName = PARK_MODE.outlet.name() + "_" + sCurrentTime + "_" + iStationId + "_" + sModifiedUserName + "_" + PARK_TYPE.discount.name();
			
			// Save the XML to file for retrieve
			FileOutputStream fileOut = new FileOutputStream(sFilePath + sFileName);
			FileOutputStream fileCheckOut = new FileOutputStream(sFilePath + sCheckFileName);
			fileOut.write(sParkOrderXML.getBytes());
			fileCheckOut.write(sParkOrderCheckXML.getBytes());
			fileOut.close();
			fileCheckOut.close();
			
			// Fire create park order file message to MQ
			if (AppGlobal.g_oMQCommandManager != null)
				AppGlobal.g_oMQCommandManager.fireCreateDeleteFileUnderDataPath(PARK_ORDER_BY_OUTLET_FILE_PATH + iOutletId + java.io.File.separator, sFileName, sParkOrderXML, false);
			
		}catch(IOException e){
			AppGlobal.stack2Log(e);
			return false;
		}
		
		return true;
	}
	
	public boolean loadParkOrderByOutlet(FuncCheck oFuncCheck, int iOutletId,String sParkOrderFileName, boolean bDeleteFile) {
		XStream xstream = new XStream();
		String sParkOrderXML = "", sParkOrderDiscountXML = "";
		String sFilePath =  checkSystemDataPath() + PARK_ORDER_BY_OUTLET_FILE_PATH + iOutletId + java.io.File.separator;
		
		// Retrieve the item list and discount object from file
		File oParkOrderFile = new File(sFilePath + sParkOrderFileName);
		sParkOrderXML = this.getFileContent(oParkOrderFile);
		if (sParkOrderXML == null)
			return false;

		String split[] = sParkOrderFileName.split("_");
		String sParkOrderDiscountFile = split[0] + "_" + split[1]+ "_" + split[2] + "_" + split[3] + "_" + PARK_TYPE.discount.name();
		File oParkOrderCheckFile = new File(sFilePath + sParkOrderDiscountFile);
		if(split.length == 5 && oParkOrderCheckFile.exists()) {
			sParkOrderDiscountXML = this.getFileContent(oParkOrderCheckFile);
			if (sParkOrderDiscountXML == null)
				return false;
		}
		
		try{
			oFuncCheck.setWholeItemList((List<List<FuncCheckItem>>) xstream.fromXML(sParkOrderXML));
			if (!sParkOrderDiscountXML.isEmpty())
				oFuncCheck.setCheckDiscountList((List<PosCheckDiscount>)xstream.fromXML(sParkOrderDiscountXML));
		}catch(Exception e){
			AppGlobal.stack2Log(e);
			return false;
		}
		
		// Delete the file
		if(bDeleteFile){
			try{		
				if(oParkOrderFile.delete() == false){
					// Fail to delete the file
					AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Fail to delete park order file (" + PARK_ORDER_FILE_PATH + sParkOrderFileName + ")");
				}
				if(split.length == 5 && oParkOrderCheckFile.exists())
					if(oParkOrderCheckFile.delete() == false){
						//Fail to delete the file
						AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Fail to delete park order file (" + PARK_ORDER_FILE_PATH + sParkOrderFileName + ")");
					}
				
				// Fire delete park order file message to MQ
				if (AppGlobal.g_oMQCommandManager != null)
					AppGlobal.g_oMQCommandManager.fireCreateDeleteFileUnderDataPath(PARK_ORDER_BY_OUTLET_FILE_PATH + iOutletId + java.io.File.separator, sParkOrderFileName, "", true);
			}catch(Exception e){
				AppGlobal.stack2Log(e);
			}
		}
		
		return true;
	}
	
	// Remove all files under parking order folder during daily close
	public void cleanParkOrderFolder(){
		String sFilePath = "";
		for(int j = 0; j <= 1; j++){
			if(j == 0)
				sFilePath = PARK_ORDER_FILE_PATH;
			else
				sFilePath = checkSystemDataPath() + PARK_ORDER_BY_OUTLET_FILE_PATH + AppGlobal.g_oFuncOutlet.get().getOutletId() + java.io.File.separator;
			File oParkOrderDirectory = new File(sFilePath);
			if(oParkOrderDirectory.exists() == false){
				return;
			}
			
			// Loop the directory to retrieve parking orders
			File [] files = oParkOrderDirectory.listFiles();
			
			for (int i = 0; i < files.length; i++){
				if (files[i].isFile()){ //this line weeds out other directories/folders
					
					String sFileName = files[i].getName();
					
					try{		
						if(files[i].delete() == false){
							// Fail to delete the file
							AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Fail to delete park order file (" + PARK_ORDER_FILE_PATH + sFileName + ")");
						}
					}catch(Exception e){
						AppGlobal.stack2Log(e);
					}
					
					// Fire delete park order file message to MQ
					if (AppGlobal.g_oMQCommandManager != null)
						AppGlobal.g_oMQCommandManager.fireCreateDeleteFileUnderDataPath(PARK_ORDER_BY_OUTLET_FILE_PATH + AppGlobal.g_oFuncOutlet.get().getOutletId() + java.io.File.separator, sFileName, "", true);
				}
			}
		}
	}
	
	private String getFileContent(File oParkOrderFile) {
		FileReader fileCheckReader;
		try {
			fileCheckReader = new FileReader(oParkOrderFile);
			BufferedReader bufferedReader = new BufferedReader(fileCheckReader);
			StringBuilder sBuilder = new StringBuilder();
			
			String sReadLine;
			while((sReadLine = bufferedReader.readLine()) != null)
				sBuilder.append(sReadLine);
			
			bufferedReader.close();
			return sBuilder.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
