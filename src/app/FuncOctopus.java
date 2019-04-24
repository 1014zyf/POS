package app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.FormOctopusOperation.FUNC_CALL_LIST;

import om.*;

public class FuncOctopus {
	
	static public final int CUST_DISPLAY_SHOW_AMOUNT_RESULT = 1;
	static public final int CUST_DISPLAY_ADD_VALUE_ASK_CARD = 2;
	static public final int CUST_DISPLAY_ADD_VALUE_RESULT = 3;
	
	// Support flag
	private boolean m_bSupportOctopus;
	
	// Octopus device connection flag
	private boolean m_bIsConnected;
	
	// Flag to determine if Octopus display is needed to be cleared automatically
	private boolean m_bNeedAutoClearDisplay;
	
	// Add value amount setting
	private ArrayList<Integer> m_iAddValueAmountList;
	
	// Add value payment method 
	private String m_sPaymentMethodCode;
	private PosPaymentMethod m_oPosPaymentMethod;
	
	// Timeout setting
	private int m_iOctopusWaitTimeout;
	private int m_iOctopusPollTimeout; 
	
	// Add-value slip copies
	private int m_iOctopusAddValueSlipCopiesCount;
	
	// Delay in ms for socket to device manager
	private int m_iSocketDelay;
	
	// Next transaction number for the station
	private int m_iNextStationTransactionNumber;
	
	// Current machine device ID
	private String m_sDeviceId;
	
	private long m_sCustomerDisplayInfo_DataUpdate_Timestamp;
	private long m_sCustomerDisplayInfo_LastGenInterfaceFile_Timestamp;
	
	// Last error message
	private String m_sErrorMessage;
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage;
	}
	
	public FuncOctopus(){
		m_bSupportOctopus = false;
		m_bIsConnected = false;
		m_bNeedAutoClearDisplay = false;
		m_iAddValueAmountList = new ArrayList<Integer>();
		m_oPosPaymentMethod = null;
		m_iOctopusWaitTimeout = 3000;
		m_iOctopusPollTimeout = 5;
		m_iOctopusAddValueSlipCopiesCount = 1;
		m_iSocketDelay = 0;
		m_iNextStationTransactionNumber = 0;
		m_sDeviceId = "";
		
		// Init Customer Display Info TimeStamps
		m_sCustomerDisplayInfo_DataUpdate_Timestamp = 0;
		m_sCustomerDisplayInfo_LastGenInterfaceFile_Timestamp = 0;
	}
	
	public void readSetup(){
		List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		
		// Get the configure from interface module
		oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PERIPHERAL_DEVICE);
		for(PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList){
			if(oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_DEVICE_MANAGER)){
				try{
					JSONObject oInterfaceSetup = oPosInterfaceConfig.getInterfaceConfig();
					
					// Support flag
					if(oInterfaceSetup.getJSONObject("octopus_setup").getJSONObject("params").getJSONObject("support").getInt("value") == 1)
						m_bSupportOctopus = true;
					
					// Poll timeout
					m_iOctopusPollTimeout = (int)(Double.parseDouble(oInterfaceSetup.getJSONObject("octopus_setup").getJSONObject("params").getJSONObject("read_card_timeout").getString("value")) / 100);
					
					// Wait timeout
					m_iOctopusWaitTimeout = Integer.parseInt(oInterfaceSetup.getJSONObject("octopus_setup").getJSONObject("params").getJSONObject("process_timeout").getString("value"));
					
					// Add value amount options
					String sTmp = oInterfaceSetup.getJSONObject("octopus_setup").getJSONObject("params").getJSONObject("add_value_options").getString("value");
					String sSplit[] = sTmp.split(",");
					for(int i=0; i<sSplit.length; i++){
						m_iAddValueAmountList.add(Integer.parseInt(sSplit[i]));
					}
					
					// Octopus add value slip copies count
					m_iOctopusAddValueSlipCopiesCount = Integer.parseInt(oInterfaceSetup.getJSONObject("octopus_setup").getJSONObject("params").getJSONObject("add_value_slip_copies").getString("value"));
					
					// Octopus add value payment method
					m_sPaymentMethodCode = oInterfaceSetup.getJSONObject("octopus_setup").getJSONObject("params").getJSONObject("add_value_payment_method").getString("value");
										
					// Delay for socket to device manager
					m_iSocketDelay = Integer.parseInt(oInterfaceSetup.getJSONObject("octopus_setup").getJSONObject("params").getJSONObject("device_communication_interval").getString("value"));
					
				}catch (Exception e) {
					AppGlobal.stack2Log(e);
				}
			}
		}
	}
	
	public void assignAddValuePaymentMethod(FuncPayment oFuncPayment){
		if(m_sPaymentMethodCode.length() > 0){
			// Get payment method ID
			PosPaymentMethodList oPosPaymentMethodList = oFuncPayment.getPaymentMethodList();
			for(Map.Entry<Integer, PosPaymentMethod> entry:oPosPaymentMethodList.getPaymentMethodListForDisplay().entrySet()){
				PosPaymentMethod oPosPaymentMethod = entry.getValue();
				if(oPosPaymentMethod.getPaymentCode().equals(m_sPaymentMethodCode)){
					m_oPosPaymentMethod = oPosPaymentMethod;
					break;
				}
			}
		}
	}
	
	public void addOctopusTransaction(String sBdayId, int iShopId, int iOletId, String sChksId, String sCpayId, String sTransactionType, String sTransactionTime, BigDecimal dTransactionAmount, int iPaymId, String sDeviceId, String sUdsn,
										String sCardId, String sCardType, BigDecimal dCardPreviousAmount, BigDecimal dCardCurrentAmount, int iTransactionUserID, int iTransactionStatId,
										String sLastAddValueType, String sLastAddValueDate){
		PosOctopusTransaction oPosOctopusTransaction = new PosOctopusTransaction();
		
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime oCurrentDateTime = DateTime.parse(sTransactionTime, fmt);
		
		oPosOctopusTransaction.setTransactionLocTime(oCurrentDateTime);
		oPosOctopusTransaction.setTransactionTime(fmt.print(AppGlobal.convertTimeToUTC(oCurrentDateTime)));
		
		oPosOctopusTransaction.setBdayId(sBdayId);
		oPosOctopusTransaction.setShopId(iShopId);
		oPosOctopusTransaction.setOletId(iOletId);
		oPosOctopusTransaction.setChksId(sChksId);
		oPosOctopusTransaction.setCpayId(sCpayId);
		oPosOctopusTransaction.setTransactionType(sTransactionType);
		oPosOctopusTransaction.setTransactionAmount(dTransactionAmount);
		oPosOctopusTransaction.setPaymId(iPaymId);
		oPosOctopusTransaction.setDeviceId(sDeviceId);
		oPosOctopusTransaction.setUdsn(sUdsn);
		oPosOctopusTransaction.setCardId(sCardId);
		oPosOctopusTransaction.setCardType(sCardType);
		oPosOctopusTransaction.setCardPreviousAmount(dCardPreviousAmount);
		oPosOctopusTransaction.setCardCurrentAmount(dCardCurrentAmount);
		oPosOctopusTransaction.setTransactionUserId(iTransactionUserID);
		oPosOctopusTransaction.setTransactionStatId(iTransactionStatId);
		oPosOctopusTransaction.setTransactionNum(m_iNextStationTransactionNumber);
		
		oPosOctopusTransaction.addUpdate(false);
		
		// Print Octopus slip
		if(sTransactionType.equals(PosOctopusTransaction.TYPE_ADD_VALUE)){
			// *****************************************************************
			// Create thread to print Octopus slip
			AppThreadManager oAppThreadManager = new AppThreadManager();
					
			// Add the method to the thread manager
			
			// Thread 1 : Print Octopus slip
			for(int i=1; i<=m_iOctopusAddValueSlipCopiesCount; i++){
				Object[] oParameter1s = new Object[14];
				if(i == 1)
					oParameter1s[0] = false;
				else
					oParameter1s[0] = true;
				oParameter1s[1] = iOletId;
				oParameter1s[2] = sDeviceId;
				oParameter1s[3] = sUdsn;
				oParameter1s[4] = sCardId;
				oParameter1s[5] = sCardType;
				oParameter1s[6] = dTransactionAmount;
				oParameter1s[7] = dCardCurrentAmount;
				oParameter1s[8] = AppGlobal.g_oFuncStation.get().getCheckPrtqId();
				oParameter1s[9] = AppGlobal.g_oCurrentLangIndex.get();
				oParameter1s[10] = m_iNextStationTransactionNumber;
				oParameter1s[11] = sLastAddValueType;
				oParameter1s[12] = sLastAddValueDate;
				oParameter1s[13] = sTransactionTime;
				oAppThreadManager.addThread(i, oPosOctopusTransaction, "printOctopusSlip", oParameter1s);
			}
			
			// Run all of the threads
			oAppThreadManager.runThread();
		}
		
		// Increment the transaction number for the station
		m_iNextStationTransactionNumber++;
	}
	
	public void updateCustomerDisplayDataUpdateTimestamp() {
		long currentTimestamp=0;
		
		DateTime currentTime = AppGlobal.getCurrentTime(false);
		currentTimestamp = currentTime.getMillis();
		this.m_sCustomerDisplayInfo_DataUpdate_Timestamp = currentTimestamp;
	}
	
	// iCallType:	1 - Show remain amount function; 2 - Add value ask put card;  3 - Add value result
	public void generateCustomerDisplayInterfaceFiles(int iCallType, BigDecimal dTransactionAmount, BigDecimal dRemainAmount){
		if(!AppGlobal.isModuleSupport(AppGlobal.OPTIONAL_MODULE.signage.name()) || AppGlobal.g_oFuncSignage.get() == null || !AppGlobal.g_oFuncSignage.get().haveSignageSchedule(AppGlobal.g_oFuncOutlet.get().getOutletId()))
			return;
		
		String sCurrentTimestampStr;
		long lCurrentTimestamp=0;
		String sOutputBaseFolder, sOutputInfosFolder, sOutputAlertsFolder;
		String sTimestampFile, sDataFile;

		JSONObject oOctopusInfoJSONObject = new JSONObject();
		JSONObject oDataControlJSONObject = new JSONObject();
		JSONObject oOctopusOperationJSONObject = new JSONObject();
		JSONObject oOctopusPaymentsJSONObject = new JSONObject();
		JSONArray oPaymentsJSONArray = new JSONArray();
		JSONObject oPaymentJSONObject = new JSONObject();
		JSONArray oPaymentInfosJSONArray = new JSONArray();
		JSONObject oPaymentInfoJSONObject = new JSONObject();
		
		if (this.m_sCustomerDisplayInfo_DataUpdate_Timestamp > this.m_sCustomerDisplayInfo_LastGenInterfaceFile_Timestamp) {
			// New customer display information is generated. Generate interface files and update last gen timestamp afterwards

			// Data Control JsonObject
			DateTime oCurrentTime = AppGlobal.getCurrentTime(false);
			lCurrentTimestamp = oCurrentTime.getMillis();		// TimeStamp : in Milliseconds from Epoch of of 1970-01-01T00:00:00Z
			DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyyMMddHHmmSSS");			
			sCurrentTimestampStr = oFmt.print(oCurrentTime);

			try {				
				oDataControlJSONObject.put("TimeStamp", sCurrentTimestampStr);
				oDataControlJSONObject.put("InterfaceType", "o");
						
				// Octopus Operation JsonObject
				String sOriginalLangCode = "";
				for(HashMap<String, String> oLangInfo:AppGlobal.g_oSupportedLangList) {
					if(oLangInfo.get("index").equals(String.valueOf(AppGlobal.g_oCurrentLangIndex.get()))){
						sOriginalLangCode = oLangInfo.get("code");
					}				
					
					String sLangCode = oLangInfo.get("code");
					AppGlobal.g_oLang.get().switchLocale(sLangCode);
					
					int iIndex = Integer.parseInt(oLangInfo.get("index"));
					if(iIndex == 1){
						if(iCallType == CUST_DISPLAY_SHOW_AMOUNT_RESULT)
							// Read card
							oOctopusOperationJSONObject.put("Desc", AppGlobal.g_oLang.get()._("octopus_remaining_value"));
						else
						if(iCallType == CUST_DISPLAY_ADD_VALUE_ASK_CARD || iCallType == CUST_DISPLAY_ADD_VALUE_RESULT)
							// Add value
							oOctopusOperationJSONObject.put("Desc", AppGlobal.g_oLang.get()._("octopus_add_value_amount"));
					}else
					if(iIndex == 2){
						if(iCallType == CUST_DISPLAY_SHOW_AMOUNT_RESULT)
							// Read card
							oOctopusOperationJSONObject.put("Desc_lang2", AppGlobal.g_oLang.get()._("octopus_remaining_value"));
						else
						if(iCallType == CUST_DISPLAY_ADD_VALUE_ASK_CARD || iCallType == CUST_DISPLAY_ADD_VALUE_RESULT)
							// Add value
							oOctopusOperationJSONObject.put("Desc_lang2", AppGlobal.g_oLang.get()._("octopus_add_value_amount"));
					}else
					if(iIndex == 3){
						if(iCallType == CUST_DISPLAY_SHOW_AMOUNT_RESULT)
							// Read card
							oOctopusOperationJSONObject.put("Desc_lang3", AppGlobal.g_oLang.get()._("octopus_remaining_value"));
						else
						if(iCallType == CUST_DISPLAY_ADD_VALUE_ASK_CARD || iCallType == CUST_DISPLAY_ADD_VALUE_RESULT)
							// Add value
							oOctopusOperationJSONObject.put("Desc_lang3", AppGlobal.g_oLang.get()._("octopus_add_value_amount"));
					}
				}
				// Roll back the language
				AppGlobal.g_oLang.get().switchLocale(sOriginalLangCode);
				
				// 20190211, according to new Approval Requirement v20.1, add Octopus Type handling
				if (dTransactionAmount == null)
					oOctopusOperationJSONObject.put("Amount", "---");
				else
					oOctopusOperationJSONObject.put("Amount", dTransactionAmount.setScale(1).toPlainString());
					
				// Octopus Payments JsonObject
				if(m_oPosPaymentMethod != null && iCallType == CUST_DISPLAY_ADD_VALUE_RESULT){
					oPaymentJSONObject.put("PayDesc", m_oPosPaymentMethod.getName(1));
					oPaymentJSONObject.put("PayDesc_lang2", m_oPosPaymentMethod.getName(2));
					oPaymentJSONObject.put("PayDesc_lang3", m_oPosPaymentMethod.getName(3));
					// 20190211, according to new Approval Requirement v20.1, add Octopus Type handling
					if (dTransactionAmount == null)
						oPaymentJSONObject.put("PayAmt", "---");
					else
						oPaymentJSONObject.put("PayAmt", dTransactionAmount.setScale(1).toPlainString());
					
					// Payment Infos JsonObject
					sOriginalLangCode = "";
					for(HashMap<String, String> oLangInfo:AppGlobal.g_oSupportedLangList) {
						if(oLangInfo.get("index").equals(String.valueOf(AppGlobal.g_oCurrentLangIndex.get()))){
							sOriginalLangCode = oLangInfo.get("code");
						}				
						
						String sLangCode = oLangInfo.get("code");
						AppGlobal.g_oLang.get().switchLocale(sLangCode);
						
						int iIndex = Integer.parseInt(oLangInfo.get("index"));
						if(iIndex == 1){
							oPaymentInfoJSONObject.put("Desc", AppGlobal.g_oLang.get()._("octopus_remaining_value"));
						}else
						if(iIndex == 2){
							oPaymentInfoJSONObject.put("Desc_lang2", AppGlobal.g_oLang.get()._("octopus_remaining_value"));
						}else
						if(iIndex == 3){
							oPaymentInfoJSONObject.put("Desc_lang3", AppGlobal.g_oLang.get()._("octopus_remaining_value"));
						}
					}
					// Roll back the language
					AppGlobal.g_oLang.get().switchLocale(sOriginalLangCode);
					
					// 20190211, according to new Approval Requirement v20.1, add Octopus Type handling
					if (dRemainAmount == null)
						oPaymentInfoJSONObject.put("Value", "---");
					else
						oPaymentInfoJSONObject.put("Value", dRemainAmount.setScale(1).toPlainString());
					oPaymentInfosJSONArray.put(oPaymentInfoJSONObject);
					
					oPaymentJSONObject.put("PaymentInfos", oPaymentInfosJSONArray);
					oPaymentsJSONArray.put(oPaymentJSONObject);
					
					oOctopusPaymentsJSONObject.put("Payments", oPaymentsJSONArray);
				}
			
				// Whole JsonObject
				oOctopusInfoJSONObject.put("DataControl", oDataControlJSONObject);					
				oOctopusInfoJSONObject.put("OctopusOperation", oOctopusOperationJSONObject);
				oOctopusInfoJSONObject.put("OctopusPayments", oOctopusPaymentsJSONObject);
			}catch(JSONException jsone) {
				AppGlobal.stack2Log(jsone);
			}

			// Create interface files
			// Directly overwrite current files
			sOutputBaseFolder = AppGlobal.g_sSystemDataPath + "//www//signage_displays";
			sOutputAlertsFolder = sOutputBaseFolder + "//" + "alerts";
			sOutputInfosFolder = sOutputBaseFolder + "//" + "infos";
			sTimestampFile = Integer.toString(AppGlobal.g_oFuncStation.get().getStationId()) + ".txt";
			sDataFile = Integer.toString(AppGlobal.g_oFuncStation.get().getStationId()) + ".txt";
			String sFilename1 = sOutputAlertsFolder + "//" + sTimestampFile;
			String sFilename2 = sOutputInfosFolder + "//" + sDataFile;			
			File oTmpFile = null;
			FileWriter fWriter;

			try {
				// Update Info File
				oTmpFile = new File(sFilename2);
				fWriter = new FileWriter(oTmpFile, false);
				fWriter.write(oOctopusInfoJSONObject.toString());
				fWriter.close();
				
				// Update Alert File
				oTmpFile = new File(sFilename1);
				fWriter = new FileWriter(oTmpFile, false);
				fWriter.write(sCurrentTimestampStr);
				fWriter.close();
			}catch(IOException fileErr) {
				AppGlobal.stack2Log(fileErr);
			}					
			
			// Update the last interface file generation timestamp
			this.m_sCustomerDisplayInfo_LastGenInterfaceFile_Timestamp = lCurrentTimestamp;			
		}
	}
	
	// Init the next station transaction number
	public void initNextStationTransactionNumber(int iStationId){
		PosOctopusTransaction oPosOctopusTransaction = new PosOctopusTransaction();
		int iLastStationTransactionNumber = oPosOctopusTransaction.getLastTransactionNumberByStation(iStationId);
		m_iNextStationTransactionNumber = iLastStationTransactionNumber + 1;
	}
	
	public boolean isSupportOctopus(){
		return m_bSupportOctopus;
	}
	
	public void setOctopusConnected(boolean bConnected){
		m_bIsConnected = bConnected;
	}
	
	public boolean isConnected(){
		return m_bIsConnected;
	}
	
	synchronized public void setNeedAutoClearDisplay(boolean bNeedAutoClearDisplay){
		m_bNeedAutoClearDisplay = bNeedAutoClearDisplay;
	}
	
	synchronized public boolean needAutoClearDisplay(){
		return m_bNeedAutoClearDisplay;
	}
	
	public int getOctopusWaitTimeout(){
		return m_iOctopusWaitTimeout;
	}
	
	public int getOctopusPollTimeout(){
		return m_iOctopusPollTimeout;
	}
	
	public ArrayList<Integer> getAddValueAmountList(){
		return m_iAddValueAmountList;
	}
	
	public int getPaymentMethodId(){
		if(m_oPosPaymentMethod != null)
			return m_oPosPaymentMethod.getPaymId();
		else
			return 0;
	}
	
	public int getSocketDelay(){
		return m_iSocketDelay;
	}
	
	public int getNextStationTransactionNumber(){
		return m_iNextStationTransactionNumber;
	}
	
	public String getDeviceId(){
		return m_sDeviceId;
	}
	
	public void setDeviceId(String sDeviceId){
		m_sDeviceId = sDeviceId;
	}
	
	public String createWinFcnMapString(String sType, Object[] oParameters){
		StringBuilder sArgString = new StringBuilder();
		
		for(int i=0; i<oParameters.length; i++){
			if(oParameters[i] != null){
				if(i != 0){
					sArgString.append(";");
				}
				
				if(oParameters[i].getClass().equals(String.class)){
					String sString = (String) oParameters[i];
					StringBuilder sb = new StringBuilder();
					for (int j=0; j<sString.length(); j++) {
						sb.append(String.format("%02x", (int)sString.charAt(j)));
					}
					sArgString.append(sb.toString());
				}else{
					sArgString.append(oParameters[i]);
				}
			}
		}
		
		sArgString.append(";");
		
		return String.format("\001win_fcn_map\002%s\002%s\004", sType, sArgString.toString());
	}
	
	public String createClearOctopusDisplayRequestString(){
		Object[] oParameters = new Object[4];
		oParameters[0] = 0;
		oParameters[1] = -30000;
		oParameters[2] = 0;
		oParameters[3] = 0;
		return createWinFcnMapString(FUNC_CALL_LIST.RwlTxnAmt.name(), oParameters);
	}
}
