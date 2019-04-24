package app.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

public class FuncMSR {

	private String m_sCardNo;
	private Date m_dtExpiryDate;
	private String m_sName;
	private String m_sCustomField1Value;
	private String m_sCustomField2Value;
	
	private String m_sLastErrorMessage;
	
	public enum FIELD_LIST{card_no, expiry_date, card_holder_name, custom_field1, custom_field2};
	
	public FuncMSR(){
		m_sCardNo = "";
		m_dtExpiryDate = null;
		m_sName = "";
		m_sCustomField1Value = "";
		m_sCustomField2Value = "";
		m_sLastErrorMessage = "";
	}
	
	private HashMap<String, String> getFieldValue(int iTrack, String sTrackValue, JSONObject oReadSetupObject){
		
		HashMap<String, String> oReturnValuePair = new HashMap<>();
		
		try{
			
			ArrayList<ClsMagneticCardFieldFormat> oFieldSetupList = new ArrayList<>();
			for(int i=1; i<=5; i++){
				String sTrackFieldString = "track" + iTrack + "_field" + i;
				
				ClsMagneticCardFieldFormat oCardFieldFormat = new ClsMagneticCardFieldFormat();
				
				if(oReadSetupObject.has(sTrackFieldString)){
					JSONObject oTrackFieldObject = oReadSetupObject.getJSONObject(sTrackFieldString);
					if(oTrackFieldObject.has("params")){
						JSONObject oParams = oTrackFieldObject.getJSONObject("params");
						if(oParams.has("field")){
							JSONObject oParam = oParams.getJSONObject("field");
							if(oParam.has("value")){
								oCardFieldFormat.setFieldKey(oParam.optString("value", ""));
							}
						}
						if(oParams.has("read_start_pos")){
							JSONObject oParam = oParams.getJSONObject("read_start_pos");
							if(oParam.has("value")){
								oCardFieldFormat.setReadStartPos(oParam.optInt("value", -1));
							}
						}
						if(oParams.has("read_length")){
							JSONObject oParam = oParams.getJSONObject("read_length");
							if(oParam.has("value")){
								oCardFieldFormat.setReadLength(oParam.optInt("value", -1));
							}
						}
						if(oParams.has("token_character")){
							JSONObject oParam = oParams.getJSONObject("token_character");
							if(oParam.has("value")){
								oCardFieldFormat.setFieldTokenChar(Pattern.quote(oParam.optString("value", "")));
							}
						}
					}
				}
				
				
				oFieldSetupList.add(oCardFieldFormat);
			}
			
			String sProcessData = sTrackValue;
			for(ClsMagneticCardFieldFormat oCardFieldFormat:oFieldSetupList){
				if(oCardFieldFormat.getFieldKey().length() == 0)
					continue;
				if(oCardFieldFormat.getReadStartPos() == 0){
					AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Missing start position for key: " + oCardFieldFormat.getFieldKey());
					continue;
				}
				if(oCardFieldFormat.getReadLength() == 0){
					AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Missing read length for key: " + oCardFieldFormat.getFieldKey());
					continue;
				}
				String[] sSplitTrack = sProcessData.split(oCardFieldFormat.getFieldTokenChar(), 2);
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
					oReturnValuePair.put(oCardFieldFormat.getFieldKey(), sValue);
					
					if(sSplitTrack.length >= 2){
						sProcessData = sSplitTrack[1];
					}
				}
			}
		
		}catch(Exception e){
			AppGlobal.stack2Log(e);
		}
		
		return oReturnValuePair;
	}
	
	public boolean processCardContent(String sCardRawData, JSONObject oSetupJSONObject){
		
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
		
//System.out.println("testtest ========================= " + sCardRawData);
		
		try {
			if(!oSetupJSONObject.has("general_setup") ||
					!oSetupJSONObject.has("track1_setup") ||
					!oSetupJSONObject.has("track2_setup") ||
					!oSetupJSONObject.has("track3_setup")){
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("missing_setup");
				return false;
			}
			
			// Get read track method
			JSONObject oGeneralSetupObject = oSetupJSONObject.getJSONObject("general_setup");
			if(oGeneralSetupObject.has("params")){
				JSONObject oParams = oGeneralSetupObject.getJSONObject("params");
				if(oParams.has("read_track_method")){
					JSONObject oParam = oParams.getJSONObject("read_track_method");
					if(oParam.has("value")){
						iReadTrackMethod = oParam.optInt("value", 0);
					}
				}
				if(oParams.has("expiry_date_format")){
					JSONObject oParam = oParams.getJSONObject("expiry_date_format");
					if(oParam.has("value")){
						sExpiryDateFormat = oParam.optString("value", "yyMM");
						sExpiryDateFormat = sExpiryDateFormat.replace("Y", "y");
					}
				}
			}
			
			// Get read track setup
			JSONObject oTrack1SetupObject = oSetupJSONObject.getJSONObject("track1_setup");
			if(oTrack1SetupObject.has("params")){
				JSONObject oParams = oTrack1SetupObject.getJSONObject("params");
				if(oParams.has("prefix_character")){
					JSONObject oParam = oParams.getJSONObject("prefix_character");
					if(oParam.has("value")){
						sTrack1PrefixChar = Pattern.quote(oParam.optString("value", ""));
					}
				}
				if(oParams.has("suffix_character")){
					JSONObject oParam = oParams.getJSONObject("suffix_character");
					if(oParam.has("value")){
						sTrack1SuffixChar = Pattern.quote(oParam.optString("value", ""));
					}
				}
				if(oParams.has("token_character")){
					JSONObject oParam = oParams.getJSONObject("token_character");
					if(oParam.has("value")){
						sTrack1TokenChar = Pattern.quote(oParam.optString("value", ""));
					}
				}
			}
			
			JSONObject oTrack2SetupObject = oSetupJSONObject.getJSONObject("track2_setup");
			if(oTrack2SetupObject.has("params")){
				JSONObject oParams = oTrack2SetupObject.getJSONObject("params");
				if(oParams.has("prefix_character")){
					JSONObject oParam = oParams.getJSONObject("prefix_character");
					if(oParam.has("value")){
						sTrack2PrefixChar = Pattern.quote(oParam.optString("value", ""));
					}
				}
				if(oParams.has("suffix_character")){
					JSONObject oParam = oParams.getJSONObject("suffix_character");
					if(oParam.has("value")){
						sTrack2SuffixChar = Pattern.quote(oParam.optString("value", ""));
					}
				}
				if(oParams.has("token_character")){
					JSONObject oParam = oParams.getJSONObject("token_character");
					if(oParam.has("value")){
						sTrack2TokenChar = Pattern.quote(oParam.optString("value", ""));
					}
				}
			}
			
			JSONObject oTrack3SetupObject = oSetupJSONObject.getJSONObject("track3_setup");
			if(oTrack3SetupObject.has("params")){
				JSONObject oParams = oTrack3SetupObject.getJSONObject("params");
				if(oParams.has("prefix_character")){
					JSONObject oParam = oParams.getJSONObject("prefix_character");
					if(oParam.has("value")){
						sTrack3PrefixChar = Pattern.quote(oParam.optString("value", ""));
					}
				}
				if(oParams.has("suffix_character")){
					JSONObject oParam = oParams.getJSONObject("suffix_character");
					if(oParam.has("value")){
						sTrack3SuffixChar = Pattern.quote(oParam.optString("value", ""));
					}
				}
				if(oParams.has("token_character")){
					JSONObject oParam = oParams.getJSONObject("token_character");
					if(oParam.has("value")){
						sTrack3TokenChar = Pattern.quote(oParam.optString("value", ""));
					}
				}
			}
			
			// Get track value
			String sProcessData = sCardRawData;
			if(iReadTrackMethod == 0){
				// Read track by prefix and suffix
				String[] sSplitTrack1 = sProcessData.split(sTrack1SuffixChar, 2);
				if(sSplitTrack1.length >= 1){
					String[] sSplitValue = sSplitTrack1[0].split(sTrack1PrefixChar, 2);
					if(sSplitValue.length >= 2){
						sTrack1Value = sSplitValue[1];
					}
					if(sSplitTrack1.length >= 2){
						String[] sSplitTrack2 = sSplitTrack1[1].split(sTrack2SuffixChar, 2);
						if(sSplitTrack2.length >= 1){
							String[] sSplitValue2 = sSplitTrack2[0].split(sTrack2PrefixChar, 2);
							if(sSplitValue2.length >= 2){
								sTrack2Value = sSplitValue2[1];
							}
							if(sSplitTrack2.length >= 2){
								String[] sSplitTrack3 = sSplitTrack2[1].split(sTrack3SuffixChar, 2);
								if(sSplitTrack3.length >= 1){
									String[] sSplitValue3 = sSplitTrack3[0].split(sTrack3PrefixChar, 2);
									if(sSplitValue3.length >= 2){
										sTrack3Value = sSplitValue3[1];
									}
								}
							}
						}
					}
				}
			}else{
				// Read track by token
				String[] sSplitTrack1 = sProcessData.split(sTrack1TokenChar, 2);
				if(sSplitTrack1.length >= 1){
					sTrack1Value = sSplitTrack1[0];
					if(sSplitTrack1.length >= 2){
						String [] sSplitTrack2 = sSplitTrack1[1].split(sTrack2TokenChar, 2);
						if(sSplitTrack2.length >= 1){
							sTrack2Value = sSplitTrack2[0];
							if(sSplitTrack2.length >= 2){
								String [] sSplitTrack3 = sSplitTrack2[1].split(sTrack3TokenChar, 2);
								if(sSplitTrack3.length >= 1){
									sTrack3Value = sSplitTrack3[0];
								}
							}
						}
					}
				}
			}
			
//System.out.println("testtest Track 1 ======================= " + sTrack1Value);
//System.out.println("testtest Track 2 ======================= " + sTrack2Value);
//System.out.println("testtest Track 3 ======================= " + sTrack3Value);
			
			// Get Track Field value
			HashMap<String, String> oTrack1ValueHashMap = getFieldValue(1, sTrack1Value, oSetupJSONObject);
			HashMap<String, String> oTrack2ValueHashMap = getFieldValue(2, sTrack2Value, oSetupJSONObject);
			HashMap<String, String> oTrack3ValueHashMap = getFieldValue(3, sTrack3Value, oSetupJSONObject);
			
			HashMap<String, String> oCardValueHashMap = new HashMap<>();
			oCardValueHashMap.putAll(oTrack1ValueHashMap);
			oCardValueHashMap.putAll(oTrack2ValueHashMap);
			oCardValueHashMap.putAll(oTrack3ValueHashMap);
			
			if(oCardValueHashMap.containsKey(FIELD_LIST.card_no.name())){
				m_sCardNo = oCardValueHashMap.get(FIELD_LIST.card_no.name());
			}
			
			if(oCardValueHashMap.containsKey(FIELD_LIST.expiry_date.name())){
				SimpleDateFormat formatter = new SimpleDateFormat(sExpiryDateFormat);
				try {
					m_dtExpiryDate = formatter.parse(oCardValueHashMap.get(FIELD_LIST.expiry_date.name()));
				}catch(Exception e){
					AppGlobal.stack2Log(e);
				}
			}
			
			if(oCardValueHashMap.containsKey(FIELD_LIST.card_holder_name.name())){
				m_sName = oCardValueHashMap.get(FIELD_LIST.card_holder_name.name());
			}
			
			if(oCardValueHashMap.containsKey(FIELD_LIST.custom_field1.name())){
				m_sCustomField1Value = oCardValueHashMap.get(FIELD_LIST.custom_field1.name());
			}
			
			if(oCardValueHashMap.containsKey(FIELD_LIST.custom_field2.name())){
				m_sCustomField2Value = oCardValueHashMap.get(FIELD_LIST.custom_field2.name());
			}
			
		} catch (JSONException e) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("internal_error");
			AppGlobal.stack2Log(e);
			return false;
		} catch (Exception e1) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("internal_error");
			AppGlobal.stack2Log(e1);
			return false;
		}
		
		
		return true;
	}
	
	public String getCardNo(){
		return m_sCardNo;
	}
	
	public Date getExpiryDate(){
		return m_dtExpiryDate;
	}
	
	public String getName(){
		return m_sName;
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
