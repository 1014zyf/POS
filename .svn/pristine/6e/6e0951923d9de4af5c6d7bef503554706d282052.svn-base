package externallib;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONObject;

public class Util {
	
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
		BigDecimal b3;
		if(dValue.signum() == -1)
			b3 = new BigDecimal((b1.multiply(b2)).setScale(0, RoundingMode.HALF_DOWN).toString());
		else
			b3 = new BigDecimal((b1.multiply(b2)).setScale(0, RoundingMode.HALF_UP).toString());
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
		BigDecimal b3;
		if(dValue.signum() == -1)
			b3 = new BigDecimal((b1.divide(b2)).setScale(0, RoundingMode.HALF_DOWN).toString());
		else
			b3 = new BigDecimal((b1.divide(b2)).setScale(0, RoundingMode.HALF_UP).toString());
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
	
	public static BigDecimal jsonObjectKeyToBigDecimal(JSONObject oJsonObject, String sKey) {
		if(oJsonObject == null || !oJsonObject.has(sKey))
			return null;
		try {
			BigDecimal dValue = new BigDecimal(oJsonObject.optString(sKey, ""));
			return dValue;
		}catch (Exception e) {
			return null;
		}
	}

	public static DateTime getCurrentLocalTimeByTimezoneName(String sTimezoneName) {
		if (sTimezoneName == null || sTimezoneName.isEmpty())
			return null;
		
		// get current local time by timezone name
		DateTimeZone oDateTimeZone = null;
		DateTime oDateTime = null;
		try {
			oDateTimeZone = DateTimeZone.forID(sTimezoneName);
			oDateTime = new DateTime(oDateTimeZone);
		} catch (Exception e) {}
		
		return oDateTime;
	}
	
	public static DateTime getLocalTimeByTimezoneName(DateTime oDateTime, String sTimezoneName) {
		if (sTimezoneName == null || sTimezoneName.isEmpty())
			return null;
		
		// get local time by timezone name
		DateTimeZone oDateTimeZone = null;
		try {
			oDateTimeZone = DateTimeZone.forID(sTimezoneName);
			oDateTime = oDateTime.withZone(oDateTimeZone);
		} catch (Exception e) {
			oDateTime = null;
		}
		
		return oDateTime;
	}
	
	public static DateTime getCurrentLocalTimeByTimezone(int iTimezone) {
		// get current local time by timezone offset(in minute)
		DateTimeZone oDateTimeZone = DateTimeZone.forOffsetMillis(iTimezone * 60 *1000);
		return new DateTime().withZone(oDateTimeZone);
	}
	
	public static DateTime getLocalTimeByTimezone(DateTime oDateTime, int iTimezone) {
		// get current local time by timezone offset(in minute)
		DateTimeZone oDateTimeZone = DateTimeZone.forOffsetMillis(iTimezone * 60 *1000);
		return oDateTime.withZone(oDateTimeZone);
	}
}
