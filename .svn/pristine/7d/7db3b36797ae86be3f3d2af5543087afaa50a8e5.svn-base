package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

public class FuncMSR {

	private String m_sCardNo;
	private DateTime m_dtExpiryDate;
	private String m_sExpiryDate;
	private String m_sAmount;
	private String m_sVoucherNo;
	private String m_sName;
	private String m_sCustomField1Value;
	private String m_sCustomField2Value;
	
	private String m_sLastErrorMessage;
	
	static public int ERROR_CODE_NO_ERROR = 0;
	static public int ERROR_CODE_MISSING_SETUP = 1001;
	static public int ERROR_CODE_INCORRECT_EXPIRY_DATE_SETUP = 1002;
	static public int ERROR_CODE_CARD_EXPIRED = 1003;
	public static final String FRAME_SWIPE_CARD_DEFAULT				= "";
	public static final String FRAME_SWIPE_CARD_OPENTABLE			= "openTable";
	public static final String FRAME_SWIPE_CARD_OPENTABLE_QWERTY	= "openTable_qwerty";
	public static final String SWIPE_CARD_NO_CONTROL = "n";
	public static final String SWIPE_CARD_CONTROL_SWIPE_ONLY = "s";
	public static final String SWIPE_CARD_CONTROL_MANUAL_INPUT_ONLY = "m";
	public enum FIELD_LIST{card_no, expiry_date, card_holder_name, amount, voucher_no, custom_field1, custom_field2};
	
	public FuncMSR(){
		m_sCardNo = "";
		m_dtExpiryDate = null;
		m_sExpiryDate = "";
		m_sName = "";
		m_sAmount = "";
		m_sVoucherNo = "";
		m_sCustomField1Value = "";
		m_sCustomField2Value = "";
		m_sLastErrorMessage = "";
	}
	
	private HashMap<String, String> getFieldValue(int iTrack, String sTrackValue, JSONObject oReadSetupObject){
		HashMap<String, String> oReturnValuePair = new HashMap<>();
		ArrayList<ClsMagneticCardFieldFormat> oFieldSetupList = new ArrayList<>();
		for (int i = 1; i <= 5; i++){
			String sTrackFieldString = "track" + iTrack + "_field" + i;
			
			ClsMagneticCardFieldFormat oCardFieldFormat = new ClsMagneticCardFieldFormat();
			
			if(oReadSetupObject.has(sTrackFieldString)){
				JSONObject oTrackFieldObject = oReadSetupObject.optJSONObject(sTrackFieldString);
				if(oTrackFieldObject.has("params")){
					JSONObject oParams = oTrackFieldObject.optJSONObject("params");
					if(oParams.has("field")){
						JSONObject oParam = oParams.optJSONObject("field");
						if(oParam.has("value")){
							oCardFieldFormat.setFieldKey(oParam.optString("value", ""));
						}
					}
					if(oParams.has("read_start_pos")){
						JSONObject oParam = oParams.optJSONObject("read_start_pos");
						if(oParam.has("value")){
							oCardFieldFormat.setReadStartPos(oParam.optInt("value", -1));
						}
					}
					if(oParams.has("read_length")){
						JSONObject oParam = oParams.optJSONObject("read_length");
						if(oParam.has("value")){
							oCardFieldFormat.setReadLength(oParam.optInt("value", -1));
						}
					}
					if(oParams.has("token_character")){
						JSONObject oParam = oParams.optJSONObject("token_character");
						if(oParam.has("value")){
							// *********** Add pattern.quote to handle if string contain special character (e.g. ")
							oCardFieldFormat.setFieldTokenChar(Pattern.quote(oParam.optString("value", "")));
						}
					}
					if(oParams.has("prefix_string_checking")){
						JSONObject oParam = oParams.optJSONObject("prefix_string_checking");
						if(oParam.has("value")){
							oCardFieldFormat.setPrefixStringChecking(oParam.optString("value", ""));
						}
					}
				}
			}
			
			oFieldSetupList.add(oCardFieldFormat);
		}
		
		String sProcessData = sTrackValue;
		for(ClsMagneticCardFieldFormat oCardFieldFormat:oFieldSetupList){
			if (oCardFieldFormat.getFieldKey().isEmpty())
				continue;
			
			// Add an empty value for the return
			oReturnValuePair.put(oCardFieldFormat.getFieldKey(), "");
			
			if(oCardFieldFormat.getReadStartPos() == 0){
				AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Missing start position for key: " + oCardFieldFormat.getFieldKey());
				continue;
			}
			if(oCardFieldFormat.getReadLength() == 0){
				AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Missing read length for key: " + oCardFieldFormat.getFieldKey());
				continue;
			}
			String[] sSplitTrack = {"",""};
			if(oCardFieldFormat.getFieldTokenChar() != null && oCardFieldFormat.getFieldTokenChar().equals("\\Q\\E") == false)
				sSplitTrack = sProcessData.split(oCardFieldFormat.getFieldTokenChar(), 2);
			else {
				sSplitTrack[0] = sProcessData;
				sSplitTrack[1] = sProcessData;
			}
			
			if(sSplitTrack.length >= 1){
				String sValue = "";
				if(oCardFieldFormat.getFieldTokenChar().equals("\\Q\\E") == false){
					if(sSplitTrack[0].length() == 0){
						AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "No data for key: " + oCardFieldFormat.getFieldKey());
						continue;
					}
					if(sSplitTrack[0].length() < oCardFieldFormat.getReadStartPos()){
						AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Read data is less than start position for key: " + oCardFieldFormat.getFieldKey());
						continue;
					}
//System.out.println("testtest 1111 ------------------------ key : " + oCardFieldFormat.getFieldKey() + ", Token : " + oCardFieldFormat.getFieldTokenChar() + ", split count : " + sSplitTrack.length + ", Start Pos : " + oCardFieldFormat.getReadStartPos() + ", Read Length : " + oCardFieldFormat.getReadLength() + ", Src :  " + sSplitTrack[0] + ", substring start : " + (oCardFieldFormat.getReadStartPos() - 1) + ", min : " + (oCardFieldFormat.getReadStartPos() + oCardFieldFormat.getReadLength() - 1) + ", " + (sSplitTrack[0].length()) );			
					sValue = sSplitTrack[0].substring(oCardFieldFormat.getReadStartPos() - 1, Math.min(oCardFieldFormat.getReadStartPos() + oCardFieldFormat.getReadLength() - 1, sSplitTrack[0].length()));
				}else{
					if(sSplitTrack.length == 1 || sSplitTrack[1].length() == 0){
						AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "No data for key: " + oCardFieldFormat.getFieldKey());
						continue;
					}
					if(sSplitTrack[1].length() < oCardFieldFormat.getReadStartPos()){
						AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Read data is less than start position for key: " + oCardFieldFormat.getFieldKey());
						continue;
					}
//System.out.println("testtest 2222 ------------------------ key : " + oCardFieldFormat.getFieldKey() + ", split count : " + sSplitTrack.length + ", Start Pos : " + oCardFieldFormat.getReadStartPos() + ", Read Length : " + oCardFieldFormat.getReadLength() + ", Src :  " + sSplitTrack[1] + ", substring start : " + (oCardFieldFormat.getReadStartPos() - 1) + ", min : " + (oCardFieldFormat.getReadStartPos() + oCardFieldFormat.getReadLength() - 1) + ", " + (sSplitTrack[1].length()) );					
					sValue = sSplitTrack[1].substring(oCardFieldFormat.getReadStartPos() - 1, Math.min(oCardFieldFormat.getReadStartPos() + oCardFieldFormat.getReadLength() - 1, sSplitTrack[1].length()));
				}
//System.out.println("testtest 3333 ------------------------ key : " + oCardFieldFormat.getFieldKey() + ", value : " + sValue);
				// Prefix string checking
				if (!oCardFieldFormat.getPrefixStringChecking().isEmpty() && !sValue.isEmpty()) {
					if (!sValue.startsWith(oCardFieldFormat.getPrefixStringChecking())) {
						// Cannot match prefix
						AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Read data is less than start position for key: " + oCardFieldFormat.getFieldKey());
						continue;
					}
				}
				oReturnValuePair.put(oCardFieldFormat.getFieldKey(), sValue);
				
				if(sSplitTrack.length >= 2){
					sProcessData = sSplitTrack[1];
				}
			}
		}
		
		return oReturnValuePair;
	}
	
	// Return value:
	// 0 - no error
	// > 0 - with error
	public int processCardContent(String sCardRawData, JSONObject oSetupJSONObject){
		int iReadTrackMethod = 0;
		String sExpiryDateFormat = "yyMM";
		
		String sTrack1PrefixChar = "";
		String sTrack1SuffixChar = "";
		String sTrack1TokenChar = "";
		String sTrack1Value = "";
		
		String sTrack2PrefixChar = "";
		String sTrack2SuffixChar = "";
		String sTrack2TokenChar = "";
		String sTrack2Value = "";
		
		String sTrack3PrefixChar = "";
		String sTrack3SuffixChar = "";
		String sTrack3TokenChar = "";
		String sTrack3Value = "";
		
		if (!oSetupJSONObject.has("general_setup") ||
				!oSetupJSONObject.has("track1_setup") ||
				!oSetupJSONObject.has("track2_setup") ||
				!oSetupJSONObject.has("track3_setup")){
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("missing_setup");
			return ERROR_CODE_MISSING_SETUP;
		}
		
		// Get read track method
		JSONObject oGeneralSetupObject = oSetupJSONObject.optJSONObject("general_setup");
		if (oGeneralSetupObject.has("params")) {
			JSONObject oParams = oGeneralSetupObject.optJSONObject("params");
			if (oParams.has("read_track_method")){
				JSONObject oParam = oParams.optJSONObject("read_track_method");
				if (oParam.has("value"))
					iReadTrackMethod = oParam.optInt("value", 0);
			}
			if (oParams.has("expiry_date_format")) {
				JSONObject oParam = oParams.optJSONObject("expiry_date_format");
				if (oParam.has("value")) {
					sExpiryDateFormat = oParam.optString("value", "yyMM");
					sExpiryDateFormat = sExpiryDateFormat.replace("Y", "y");
					sExpiryDateFormat = sExpiryDateFormat.replace("D", "d");
				}
			}
		}
		
		// Get read track setup
		JSONObject oTrack1SetupObject = oSetupJSONObject.optJSONObject("track1_setup");
		if (oTrack1SetupObject.has("params")) {
			JSONObject oParams = oTrack1SetupObject.optJSONObject("params");
			if (oParams.has("prefix_character")) {
				JSONObject oParam = oParams.optJSONObject("prefix_character");
				if(oParam.has("value"))
					sTrack1PrefixChar = Pattern.quote(oParam.optString("value", ""));
			}
			if (oParams.has("suffix_character")) {
				JSONObject oParam = oParams.optJSONObject("suffix_character");
				if (oParam.has("value"))
					sTrack1SuffixChar = Pattern.quote(oParam.optString("value", ""));
			}
			if (oParams.has("token_character")) {
				JSONObject oParam = oParams.optJSONObject("token_character");
				if (oParam.has("value"))
					sTrack1TokenChar = Pattern.quote(oParam.optString("value", ""));
			}
		}
		
		JSONObject oTrack2SetupObject = oSetupJSONObject.optJSONObject("track2_setup");
		if (oTrack2SetupObject.has("params")) {
			JSONObject oParams = oTrack2SetupObject.optJSONObject("params");
			if (oParams.has("prefix_character")) {
				JSONObject oParam = oParams.optJSONObject("prefix_character");
				if (oParam.has("value"))
					sTrack2PrefixChar = Pattern.quote(oParam.optString("value", ""));
			}
			if (oParams.has("suffix_character")) {
				JSONObject oParam = oParams.optJSONObject("suffix_character");
				if (oParam.has("value"))
					sTrack2SuffixChar = Pattern.quote(oParam.optString("value", ""));
			}
			if (oParams.has("token_character")) {
				JSONObject oParam = oParams.optJSONObject("token_character");
				if (oParam.has("value"))
					sTrack2TokenChar = Pattern.quote(oParam.optString("value", ""));
			}
		}
		
		JSONObject oTrack3SetupObject = oSetupJSONObject.optJSONObject("track3_setup");
		if (oTrack3SetupObject.has("params")) {
			JSONObject oParams = oTrack3SetupObject.optJSONObject("params");
			if (oParams.has("prefix_character")) {
				JSONObject oParam = oParams.optJSONObject("prefix_character");
				if (oParam.has("value"))
					sTrack3PrefixChar = Pattern.quote(oParam.optString("value", ""));
			}
			if (oParams.has("suffix_character")) {
				JSONObject oParam = oParams.optJSONObject("suffix_character");
				if (oParam.has("value"))
					sTrack3SuffixChar = Pattern.quote(oParam.optString("value", ""));
			}
			if (oParams.has("token_character")) {
				JSONObject oParam = oParams.optJSONObject("token_character");
				if (oParam.has("value"))
					sTrack3TokenChar = Pattern.quote(oParam.optString("value", ""));
			}
		}
		
		// Get track value
		String sProcessData = sCardRawData;
		if(iReadTrackMethod == 0){
			// Read track by prefix and suffix
			String[] sSplitTrack1 = sProcessData.split(sTrack1SuffixChar, 2);
			if (sSplitTrack1.length >= 1) {
				String[] sSplitValue = sSplitTrack1[0].split(sTrack1PrefixChar, 2);
				if (sSplitValue.length >= 2)
					sTrack1Value = sSplitValue[1];
				if (sSplitTrack1.length >= 2) {
					String[] sSplitTrack2 = sSplitTrack1[1].split(sTrack2SuffixChar, 2);
					if (sSplitTrack2.length >= 1) {
						String[] sSplitValue2 = sSplitTrack2[0].split(sTrack2PrefixChar, 2);
						if (sSplitValue2.length >= 2)
							sTrack2Value = sSplitValue2[1];
						if (sSplitTrack2.length >= 2) {
							String[] sSplitTrack3 = sSplitTrack2[1].split(sTrack3SuffixChar, 2);
							if (sSplitTrack3.length >= 1) {
								String[] sSplitValue3 = sSplitTrack3[0].split(sTrack3PrefixChar, 2);
								if (sSplitValue3.length >= 2)
									sTrack3Value = sSplitValue3[1];
							}
						}
					}
				}
			}
		}else{
			// Read track by token
			String[] sSplitTrack1 = sProcessData.split(sTrack1TokenChar, 2);
			if (sSplitTrack1.length >= 1) {
				sTrack1Value = sSplitTrack1[0];
				if (sSplitTrack1.length >= 2) {
					String [] sSplitTrack2 = sSplitTrack1[1].split(sTrack2TokenChar, 2);
					if (sSplitTrack2.length >= 1) {
						sTrack2Value = sSplitTrack2[0];
						if (sSplitTrack2.length >= 2) {
							String [] sSplitTrack3 = sSplitTrack2[1].split(sTrack3TokenChar, 2);
							if (sSplitTrack3.length >= 1)
								sTrack3Value = sSplitTrack3[0];
						}
					}
				}
			}
		}
		
//System.out.println("testtest 1111 ------------------------ sTrack1Value " + sTrack1Value);
//System.out.println("testtest 2222 ------------------------ sTrack2Value " + sTrack2Value);
//System.out.println("testtest 3333 ------------------------ sTrack3Value " + sTrack3Value);
		
		// Get Track Field value
		HashMap<String, String> oTrack1ValueHashMap = getFieldValue(1, sTrack1Value, oSetupJSONObject);
		HashMap<String, String> oTrack2ValueHashMap = getFieldValue(2, sTrack2Value, oSetupJSONObject);
		HashMap<String, String> oTrack3ValueHashMap = getFieldValue(3, sTrack3Value, oSetupJSONObject);
		
		HashMap<String, String> oCardValueHashMap = new HashMap<>();
		oCardValueHashMap.putAll(oTrack1ValueHashMap);
		oCardValueHashMap.putAll(oTrack2ValueHashMap);
		oCardValueHashMap.putAll(oTrack3ValueHashMap);
				
		if (oCardValueHashMap.containsKey(FIELD_LIST.card_no.name()))
			m_sCardNo = oCardValueHashMap.get(FIELD_LIST.card_no.name());
		
		if (oCardValueHashMap.containsKey(FIELD_LIST.card_holder_name.name()))
			m_sName = oCardValueHashMap.get(FIELD_LIST.card_holder_name.name());
		
		if (oCardValueHashMap.containsKey(FIELD_LIST.amount.name()))
			m_sAmount = oCardValueHashMap.get(FIELD_LIST.amount.name());
		
		if (oCardValueHashMap.containsKey(FIELD_LIST.voucher_no.name()))
			m_sVoucherNo = oCardValueHashMap.get(FIELD_LIST.voucher_no.name());
		
		if (oCardValueHashMap.containsKey(FIELD_LIST.custom_field1.name()))
			m_sCustomField1Value = oCardValueHashMap.get(FIELD_LIST.custom_field1.name());
		
		if (oCardValueHashMap.containsKey(FIELD_LIST.custom_field2.name()))
			m_sCustomField2Value = oCardValueHashMap.get(FIELD_LIST.custom_field2.name());
		
		if (oCardValueHashMap.containsKey(FIELD_LIST.expiry_date.name())) {
			m_sExpiryDate = oCardValueHashMap.get(FIELD_LIST.expiry_date.name());
			DateTimeFormatter formatter = DateTimeFormat.forPattern(sExpiryDateFormat);
			try {
				m_dtExpiryDate = formatter.parseDateTime(oCardValueHashMap.get(FIELD_LIST.expiry_date.name()));
			}catch(Exception e){
				AppGlobal.stack2Log(e);
			}
			
			if (m_dtExpiryDate == null) {
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("incorrect_expiry_date_setup");
				return ERROR_CODE_INCORRECT_EXPIRY_DATE_SETUP;
			}
			
			//validate expiry date
			DateTime oCurrentTime = AppGlobal.getCurrentTime(false);
			DateTime olastDayOfMonth = AppGlobal.getCurrentTime(false);
			
			if(sExpiryDateFormat.equals("yyMM")){
				olastDayOfMonth = olastDayOfMonth.plusMonths(1).minusDays(olastDayOfMonth.getDayOfMonth());
				
				if(oCurrentTime.compareTo(olastDayOfMonth) > 0) {
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("card_expired");
					return ERROR_CODE_CARD_EXPIRED;
				}
			}
			
			if(oCurrentTime.compareTo(olastDayOfMonth) > 0 || oCurrentTime.compareTo(m_dtExpiryDate) > 0) {
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("card_expired");
				return ERROR_CODE_CARD_EXPIRED;
			}
		}
		
		return ERROR_CODE_NO_ERROR;
	}
	
	public String getCardNo(){
		return m_sCardNo;
	}
	
	public DateTime getExpiryDate(){
		return m_dtExpiryDate;
	}
	
	public String getExpiryDateString(){
		return m_sExpiryDate;
	}
	
	public String getName(){
		return m_sName;
	}
	
	public String getAmount(){
		return m_sAmount;
	}
	
	public String getVoucherNo(){
		return m_sVoucherNo;
	}
	
	public String getCustomField1Value(){
		return m_sCustomField1Value;
	}
	
	public String getCustomField2Value(){
		return m_sCustomField2Value;
	}
	
	public String getLastErrorMessage(){
		return m_sLastErrorMessage;
	}
}
