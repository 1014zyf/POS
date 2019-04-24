package core.externallib;

import core.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Socket;
import java.util.HashMap;

import core.manager.LoggingManager;

public class Util {
	private static HashMap<String, Long> _startTime;
	
	static {
		_startTime = new HashMap<String, Long>();
	}
	
	public static void touch() {}
	
	public static void showClock(String remarks, boolean resetClock) {
		if (!Core.DEBUGGING || !Core.PROFILING)
			return;
		
		if (resetClock)
			_startTime.remove(remarks);
		
		long nowTime = System.currentTimeMillis();
		
		if (_startTime.get(remarks) != null) {
			long elapsedTime = nowTime - _startTime.get(remarks);	
			LoggingManager.d(remarks, " !!! Elapsed : ", elapsedTime, " ms !!!");	
		}
		else {
			_startTime.put(remarks, nowTime);
		}
	}
	
	//create folder if existence
	public static boolean createFolderIfNotExistence(String folderPath) {
		File folder = new File(folderPath);
		
		if(!folder.exists()) {
			try {
				if(!folder.mkdirs())
					return false;
				
			}catch (Exception e) {
				return false;
			}
		}
		
		return false;
	}
	
	public static BigDecimal HERORound(BigDecimal dValue, String sRoundMethod, Integer iDecimal){
		if(sRoundMethod.equals("1"))
			return RoundUp(dValue, iDecimal);
		else
		if(sRoundMethod.equals("2"))
			return RoundDown(dValue, iDecimal);
		else
		if(sRoundMethod.equals("3"))
			return RoundOffToNearest5(dValue, iDecimal);
		else
		if(sRoundMethod.equals("4"))
			return RoundUpToNearest5(dValue, iDecimal);
		else
		if(sRoundMethod.equals("5"))
			return RoundDownToNearest5(dValue, iDecimal);
		else
			return RoundOff(dValue, iDecimal);
	}
	
	public static BigDecimal RoundOff(BigDecimal dValue, Integer iDecimal)
	{
		BigDecimal dRoundTo = new BigDecimal("10.0").pow(iDecimal);
		BigDecimal b1 = dValue;
		BigDecimal b2 = new BigDecimal(dRoundTo.toString());
		BigDecimal b3 = new BigDecimal((b1.multiply(b2)).setScale(0, RoundingMode.HALF_UP).toString());
		return new BigDecimal((b3.divide(b2)).toString());
	}
	
	public static BigDecimal RoundUp(BigDecimal dValue, Integer iDecimal)
	{
		BigDecimal dRoundTo = new BigDecimal("10.0").pow(iDecimal);
		BigDecimal b1 = dValue;
		BigDecimal b2 = new BigDecimal(dRoundTo.toString());
		BigDecimal b3 = new BigDecimal((b1.multiply(b2)).setScale(0, RoundingMode.CEILING).toString());
		return new BigDecimal((b3.divide(b2)).toString());
	}
	
	public static BigDecimal RoundDown(BigDecimal dValue, Integer iDecimal)
	{
		BigDecimal dRoundTo = new BigDecimal("10.0").pow(iDecimal);
		BigDecimal b1 = dValue;
		BigDecimal b2 = new BigDecimal(dRoundTo.toString());
		BigDecimal b3 = new BigDecimal((b1.multiply(b2)).setScale(0, RoundingMode.FLOOR).toString());
		return new BigDecimal((b3.divide(b2)).toString());
	}
	
	public static BigDecimal RoundOffToNearest5(BigDecimal dValue, Integer iDecimal)
    {
		BigDecimal dRoundTo = (new BigDecimal("5")).divide((new BigDecimal("10.0").pow(iDecimal)));
    	BigDecimal b1 = dValue;
		BigDecimal b2 = new BigDecimal(dRoundTo.toString());
        BigDecimal b3 = new BigDecimal((b1.divide(b2)).setScale(0, RoundingMode.HALF_UP).toString());
        return new BigDecimal((b3.multiply(b2)).toString());
    }
	
	public static BigDecimal RoundUpToNearest5(BigDecimal dValue, Integer iDecimal)
    {
		BigDecimal dRoundTo = (new BigDecimal("5")).divide((new BigDecimal("10.0").pow(iDecimal)));
    	BigDecimal b1 = dValue;
		BigDecimal b2 = new BigDecimal(dRoundTo.toString());
        BigDecimal b3 = new BigDecimal((b1.divide(b2)).setScale(0, RoundingMode.CEILING).toString());
        return new BigDecimal((b3.multiply(b2)).toString());
    }
	
    public static BigDecimal RoundDownToNearest5(BigDecimal dValue, Integer iDecimal)
    {
    	BigDecimal dRoundTo = (new BigDecimal("5")).divide((new BigDecimal("10.0").pow(iDecimal)));
		BigDecimal b1 = dValue;
		BigDecimal b2 = new BigDecimal(dRoundTo.toString());
		BigDecimal b3 = new BigDecimal((b1.divide(b2)).setScale(0, RoundingMode.FLOOR).toString());
		return new BigDecimal((b3.multiply(b2)).toString());
    }
    
    public static String intGlue(int[] ints, String glue) 
    {
    	int size = ints.length;
    	if (size == 0)
    		return "";
    	StringBuilder result= new StringBuilder();
    	result.append(ints[0]);
    	for (int i = 1; i < size; i++)
    		result.append(glue).append(ints[i]);
    	return result.toString();
    }
    
    public static boolean isArrayIntAllEqual(int[] i, int j) {
    	for (int value : i)
    		if (value != j)
    			return false;
    	return true;
    }
	
	public static boolean isPortAvailable(int port) {
	    Socket testSocket = null;
	    try {
	    	testSocket = new Socket("localhost", port);
	    	//If the port is able to new, it is already opening
	    	//Can't be use again
	        testSocket.close();
	        return false;
	    } catch (IOException e) {
	        return true;
	    }
	}
	
	// Show memory usage
	public static void showMemory() {
		
		int mb = 1024*1024;
		
        //Getting the runtime reference from system
		Runtime runtime = Runtime.getRuntime();
		
		System.out.println("##### Heap utilization statistics [MB] #####");
		
		//Print used memory
		System.out.println("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb);
		
		//Print free memory
		System.out.println("Free Memory:" + runtime.freeMemory() / mb);
		
		//Print total available memory
		System.out.println("Total Memory:" + runtime.totalMemory() / mb);
		
		//Print Maximum available memory
		System.out.println("Max Memory:" + runtime.maxMemory() / mb);
		
		System.out.println("============================================");
    }
	
	// Check memory usage
	// If free memory under 10Mb, write debug log
	public static void checkMemory() {
		
		int mb = 1024*1024;
		
		//Getting the runtime reference from system
		Runtime runtime = Runtime.getRuntime();
		
		// Write log if under 10Mb
		if((runtime.freeMemory() / mb) > 10.0)
			return;
		
		StringBuilder sb = new StringBuilder();
		
		//Print used memory
		sb.append("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb + " ");
		
		//Print free memory
		sb.append("Free Memory:" + runtime.freeMemory() / mb + " ");
		
		//Print total available memory
		sb.append("Total Memory:" + runtime.totalMemory() / mb + " ");
		
		//Print Maximum available memory
		sb.append("Max Memory:" + runtime.maxMemory() / mb + "");
		
		LoggingManager.writeDebugLog("AppGlobal", new Exception().getStackTrace()[0].getMethodName(), sb.toString());
	}
	
	public static String stackToString(Exception e){
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
}
