package externallib;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class StringLib {
	public static String fillZero(String text, int size) {
        StringBuilder builder = new StringBuilder(text);
        while (builder.length() < size) {
            builder.append('0');
        }
        return builder.toString();
    }
	
	public static String fillZeroAtBegin(String text, int size) {
        StringBuilder builder = new StringBuilder(text);
        while (builder.length() < size) {
            builder.insert(0, '0');
        }
        return builder.toString();
    }

	public static String fillSpace(String text, int size) {
        StringBuilder builder = new StringBuilder(text);
        while (builder.length() < size) {
            builder.append(' ');
        }
        return builder.toString();
    }
	
	public static String byteListToString(List<Byte> l) {
	    if (l == null) {
	        return "" ;
	    }
	    ByteArrayOutputStream bout = new ByteArrayOutputStream(l.size());

	    for (Byte current : l) {
	        bout.write(current);
	    }

	    return bout.toString();
	}
	
	public static String BigDecimalToString(BigDecimal dValue, Integer iDecimal) {
		return dValue.setScale(iDecimal, RoundingMode.HALF_UP).toPlainString();
	}
	
	public static String DoubleToStringWithoutZeroDecimal(Double dDoubleValue) {
		BigDecimal dValue = BigDecimal.valueOf(dDoubleValue).stripTrailingZeros();
		BigDecimal result = dValue.subtract(dValue.setScale(0, RoundingMode.FLOOR)).movePointRight(dValue.scale());
		if(result.compareTo(BigDecimal.ZERO) == 0)
			return dValue.setScale(0, RoundingMode.FLOOR).toPlainString();
		else
			return BigDecimalToString(dValue, dValue.scale());
	}
	
	public static String BigDecimalToStringWithoutZeroDecimal(BigDecimal dBigDecimalValue) {
		BigDecimal result = dBigDecimalValue.subtract(dBigDecimalValue.setScale(0, RoundingMode.FLOOR)).movePointRight(dBigDecimalValue.scale());
		if(result.compareTo(BigDecimal.ZERO) == 0)
			return dBigDecimalValue.setScale(0, RoundingMode.FLOOR).toPlainString();
		else
			return BigDecimalToString(dBigDecimalValue, dBigDecimalValue.stripTrailingZeros().scale());
	}
	
	public static String BigDecimalToStringWithThousandSepartor(BigDecimal dBigDecimalValue, char cSeperator) {
		DecimalFormat oFormatter = new DecimalFormat();
		DecimalFormatSymbols oSymbol = new DecimalFormatSymbols();
		oSymbol.setGroupingSeparator(cSeperator);
		oFormatter.setDecimalFormatSymbols(oSymbol);
		return oFormatter.format(dBigDecimalValue);
	}
	
	public static String IntToStringWithLeadingZero(int iValue, int iLength) {
		String format = String.format("%%0%dd", iLength);
		String result = String.format(format, iValue);
		return result;
	}
	
	//change DateTime object to string for database insertion/update
	public static String dateTimeToString (DateTime oDateTime) {
		if (oDateTime == null)
			return "";
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		String timeString = fmt.print(oDateTime);

		return timeString;
	}
	
	public static String stringToHex(String string) {
		StringBuilder buf = new StringBuilder();
		for (char ch: string.toCharArray()) {
			buf.append(String.format("%02x", (int) ch));
		}
		return buf.toString();
	}
	
	public static String[] createStringArray(int iLength, String... sValue) {
		String[] sValueArray = new String[iLength];
		String sCurrValue = "";
		for (int i=0; i<iLength; i++) {
			if (i < sValue.length) {
				sCurrValue = sValue[i];
			}
			sValueArray[i] = sCurrValue;
		}
		return sValueArray;
	}
	
	public static String[] appendStringArray(Object... oAppendObjects) {
		int iReturnArrayLength = 1;
		for (int i=0; i<oAppendObjects.length; i++) {
			if (oAppendObjects[i] == null)
				break;
			
			if (oAppendObjects[i].getClass().isArray()) {
				int iCurrentLang = ((String[]) oAppendObjects[i]).length;
				if (iCurrentLang > iReturnArrayLength)
					iReturnArrayLength = iCurrentLang;
			}
		}
		
		String[] oValueList = createStringArray(iReturnArrayLength, "");
		
		for (int i=0; i<oAppendObjects.length; i++) {
			if (oAppendObjects[i] == null)
				break;
			
			if (oAppendObjects[i].getClass().isArray()) {
				// Handle the append array
				String []oAppendStrings = (String[]) oAppendObjects[i];
				for (int j=0; j<iReturnArrayLength; j++) {
					if (j >= oAppendStrings.length)
						oValueList[j] += oAppendStrings[oAppendStrings.length-1];
					else
						oValueList[j] += oAppendStrings[j];
				}
			} else {
				// Handle the append string
				for (int j=0; j<iReturnArrayLength; j++) {
					oValueList[j] += String.valueOf(oAppendObjects[i]);
				}
			}
		}
		
		return oValueList;
	}
	
}
