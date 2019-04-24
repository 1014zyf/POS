package core.externallib;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
	
	public static String stackToString(Exception e){
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
}
