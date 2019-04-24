package app.controller;

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

public class FuncParkOrder {

	public static String PARK_ORDER_FILE_PATH = "park" + java.io.File.separator;
	public enum PARK_MODE{station};
	
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
				
				// Check if the file is created by current station
				int iFileStationId = Integer.parseInt(split[2]);
				if(iFileStationId != iStationId)
					continue;
				
				oParkOrderFileNameList.add(files[i].getName());
			}
		}
		
		return oParkOrderFileNameList;
	}
	
	// Park order
	public boolean saveParkOrderByStation(FuncCheck oFuncCheck, int iStationId) {
		XStream xstream = new XStream();
		String sParkOrderXML = "";
		
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
		}catch(Exception e){
			AppGlobal.stack2Log(e);
			return false;
		}
		
		try{
			DateTime today = new DateTime();
			DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMddHHmmss");
			String sCurrentTime = dateFormat.print(today);
			
			// Create file name
			// File format : station_yyyyMMddHHmmss_<station ID>_<park employee name>
			String sFileName = PARK_ORDER_FILE_PATH + PARK_MODE.station.name() + "_" + sCurrentTime + "_" + iStationId + "_" + AppGlobal.g_oFuncUser.get().getUserName(AppGlobal.g_oCurrentLangIndex.get());
			
			// Save the XML to file for retrieve
			FileOutputStream fileOut = new FileOutputStream(sFileName);
			fileOut.write(sParkOrderXML.getBytes());
			fileOut.close();
		}catch(IOException e){
			AppGlobal.stack2Log(e);
			return false;
		}
		
		return true;
	}
	
	public boolean loadParkOrderByStation(FuncCheck oFuncCheck, String sParkOrderFileName, boolean bDeleteFile) {
		XStream xstream = new XStream();
		StringBuilder sBuilder = new StringBuilder();
		String sParkOrderXML = "";
		
		// Retrieve the item list object from file
		File oParkOrderFile;
		try {
			oParkOrderFile = new File(PARK_ORDER_FILE_PATH + sParkOrderFileName);
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(oParkOrderFile);
			
			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			String sReadLine;
			while((sReadLine = bufferedReader.readLine()) != null) {
				sBuilder.append(sReadLine);
			}	
			
			// Always close files.
			bufferedReader.close();
		}
		catch(FileNotFoundException e) {
			AppGlobal.stack2Log(e);
			return false;
		}
		catch(IOException e) {
			AppGlobal.stack2Log(e);
			return false;
		}
		
		sParkOrderXML = sBuilder.toString();
		
		try{
			oFuncCheck.setWholeItemList((List<List<FuncCheckItem>>) xstream.fromXML(sParkOrderXML));
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
			}catch(Exception e){
				AppGlobal.stack2Log(e);
			}
		}
		
		return true;
	}
	
	// Remove all files under parking order folder during daily close
	public void cleanParkOrderFolder(){
		File oParkOrderDirectory = new File(PARK_ORDER_FILE_PATH);
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
			}
		}
	}
}
