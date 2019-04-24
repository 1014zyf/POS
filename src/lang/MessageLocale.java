package lang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageLocale {
	
	private HashMap<String, String> m_oMessageList;
	
	private static String MSG_ID_PATTERN = "^msgid\\s+\\\"(.*?)\\\"";
	private static String MSG_STR_PATTERN = "^msgstr\\s+\\\"(.*?)\\\"";
	
	public MessageLocale(String sFilePath) {
		m_oMessageList = new HashMap<String, String>();
		
		File oLocaleFile = new File(sFilePath);
		FileReader fileReader;
		try {
			fileReader = new FileReader(oLocaleFile);
		
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			String sReadLine;
			Pattern msgIdPattern = Pattern.compile(MSG_ID_PATTERN);
			Pattern msgStrPattern = Pattern.compile(MSG_STR_PATTERN);
			String sKey = "";
			while((sReadLine = bufferedReader.readLine()) != null) {
				Matcher matcher1 = msgIdPattern.matcher(sReadLine);
				Matcher matcher2 = msgStrPattern.matcher(sReadLine);
				
				if(matcher1.find()) {
					sKey = matcher1.group(1);
				} else if(!sKey.isEmpty()) {
					if(matcher2.find()) {
						m_oMessageList.put(sKey, matcher2.group(1));
					}
					
					sKey = "";
				}
			}
		bufferedReader.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}
	
	public String getMessage(String sKey) {
		if(!m_oMessageList.containsKey(sKey))
			m_oMessageList.put(sKey, sKey);
		
		return m_oMessageList.get(sKey);
	}

}
