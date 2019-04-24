package app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import om.InfInterface;
import om.InfVendor;
import om.PosInterfaceConfig;
import om.PosPaymentMethodList;
import om.PosPaymentMethod;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import app.AppGlobal;

public class FuncRewriteCard {
	// Support flag
	private boolean m_bSupport;
	
	// Rewrite Card Model Type
	private String m_sModelType;
	
	// Smart Card Device
	private String m_sDevice;
	
	private int m_iBuadRate;
	
	// Timeout setting
	private int m_iTimeout;
	
	// Flow Control
	private int m_iFlowControl;
	
	
	// flag to need ask password
	private boolean m_bAskNeedPassword;
	
	
	// Rewrite card model type
	public static final String TYPE_MODEL_ONE_CARD_SLN = "one_card_sln";
	
	// response parameters
	public String m_sCardNumber;
	public String m_sCardNumber1;
	public String m_sCardID;
	public String m_sCardMemberName;
	public String m_sCardAmount;
	public boolean m_bIsNeedCardPassword;
	public String m_sCardPassword;
	public BigDecimal m_oPaymentAmount;
	public BigDecimal m_oAmountBeforeDeduct;
	public BigDecimal m_oAmountAfterDeduct;
	public String m_sTransactionNo;
	public String m_sCardType;
	public int m_iPaymentCardType;
	
	// Show card information list
	public HashMap<String, String> m_oShowCardInfoList = new LinkedHashMap<String, String>();
	
	private boolean init() {
		m_bSupport = false;
		m_sModelType = "";
		m_sDevice = "";
		m_iBuadRate = 9600;
		m_iTimeout = 1000;
		m_iFlowControl = 0;
		m_bAskNeedPassword = false;
		
		m_sCardNumber = "";
		m_sCardNumber1 = "";
		m_sCardID = "";
		m_sCardMemberName = "";
		m_sCardAmount = "";
		m_bIsNeedCardPassword = false;
		m_sCardPassword = "";
		m_oPaymentAmount = BigDecimal.ZERO;
		m_oAmountBeforeDeduct = BigDecimal.ZERO;
		m_oAmountAfterDeduct = BigDecimal.ZERO;
		m_sTransactionNo = "";
		m_sCardType = "";
		m_iPaymentCardType = 0;
		
		m_oShowCardInfoList.clear();
		
		return true;
	}
	
	public boolean readSetup() {
		List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		
		// Get the configure from interface module
		oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PERIPHERAL_DEVICE);
		for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList) {
			if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_DEVICE_MANAGER)) {
				try {
					JSONObject oInterfaceSetup = oPosInterfaceConfig.getInterfaceConfig();
					
					// Support flag
					if (oInterfaceSetup.has("rewrite_card_setup") && oInterfaceSetup.getJSONObject("rewrite_card_setup").getJSONObject("params").getJSONObject("support").getInt("value") == 1)
						m_bSupport = true;
					else
						break;
					
					// get Model Type
					m_sModelType = FuncRewriteCard.TYPE_MODEL_ONE_CARD_SLN;
					if (oInterfaceSetup.has("rewrite_card_setup")) {
						switch (oInterfaceSetup.getJSONObject("rewrite_card_setup").getJSONObject("params").getJSONObject("model").getInt("value")){
						case 0:
							m_sModelType = FuncRewriteCard.TYPE_MODEL_ONE_CARD_SLN;
							break;
						}
					}
					
					// Wait timeout
					m_iTimeout = Integer.parseInt(oInterfaceSetup.getJSONObject("rewrite_card_setup").getJSONObject("params").getJSONObject("process_timeout").getString("value"));

					//read setup
					if (oInterfaceSetup.optJSONObject("rewrite_card_setup").optJSONObject("params").has("device_name"))
						m_sDevice = oInterfaceSetup.optJSONObject("rewrite_card_setup").optJSONObject("params").optJSONObject("device_name").optString("value", "");
					if (oInterfaceSetup.optJSONObject("rewrite_card_setup").optJSONObject("params").has("baud_rate"))
						m_iBuadRate = oInterfaceSetup.optJSONObject("rewrite_card_setup").optJSONObject("params").optJSONObject("baud_rate").optInt("value", 9600);
					m_iFlowControl = Integer.parseInt(oInterfaceSetup.getJSONObject("rewrite_card_setup").getJSONObject("params").getJSONObject("flow_control").getString("value"));
					if (oInterfaceSetup.optJSONObject("rewrite_card_setup").optJSONObject("params").has("need_ask_password"))
						if (oInterfaceSetup.getJSONObject("rewrite_card_setup").getJSONObject("params").getJSONObject("need_ask_password").getInt("value") == 1)
							m_bAskNeedPassword = true;
					
				} catch (Exception e) {
					AppGlobal.stack2Log(e);
				}
			}
		}
		
		return m_bSupport;
	}
	
	private String createWinFcnMapString(String sType, Object[] oParameters) {
		StringBuilder sArgString = new StringBuilder();
		
		for (int i = 0; i < oParameters.length; i++) {
			if (oParameters[i] != null) {
				if (i != 0)
					sArgString.append(";");

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
		sArgString.append((char)0x02);
		
		return String.format("\001win_fcn_map\002%s\002%s\004", sType, sArgString.toString());
	}
	
	public void copyFrom(FuncRewriteCard oCopyFuncRewriteCard) {
		m_bSupport = oCopyFuncRewriteCard.m_bSupport;
		m_sModelType = oCopyFuncRewriteCard.m_sModelType;
		m_sDevice = oCopyFuncRewriteCard.m_sDevice;
		m_iBuadRate = oCopyFuncRewriteCard.m_iBuadRate;
		m_iTimeout = oCopyFuncRewriteCard.m_iTimeout;
		m_iFlowControl = oCopyFuncRewriteCard.m_iFlowControl;
		m_bAskNeedPassword = oCopyFuncRewriteCard.m_bAskNeedPassword;
		
		m_sCardNumber = oCopyFuncRewriteCard.m_sCardNumber;
		m_sCardNumber1 = oCopyFuncRewriteCard.m_sCardNumber1;
		m_sCardID = oCopyFuncRewriteCard.m_sCardID;
		m_sCardMemberName = oCopyFuncRewriteCard.m_sCardMemberName;
		m_sCardAmount = oCopyFuncRewriteCard.m_sCardAmount;
		m_bIsNeedCardPassword = oCopyFuncRewriteCard.m_bIsNeedCardPassword;
		m_sCardPassword = oCopyFuncRewriteCard.m_sCardPassword;
		m_oPaymentAmount = oCopyFuncRewriteCard.m_oPaymentAmount;
		m_oAmountBeforeDeduct = oCopyFuncRewriteCard.m_oAmountBeforeDeduct;
		m_oAmountAfterDeduct = oCopyFuncRewriteCard.m_oAmountAfterDeduct;
		m_sTransactionNo = oCopyFuncRewriteCard.m_sTransactionNo;
		m_sCardType = oCopyFuncRewriteCard.m_sCardType;
		m_iPaymentCardType = oCopyFuncRewriteCard.m_iPaymentCardType;
		
		m_oShowCardInfoList = oCopyFuncRewriteCard.m_oShowCardInfoList;
	}
	
	public boolean isSupport() {
		return m_bSupport;
	}
	
	public String getModelType() {
		return m_sModelType;
	}
	
	public String getDevice() {
		return m_sDevice;
	}
	
	public int getBaudRate() {
		return m_iBuadRate;
	}
	
	public int getTimeout() {
		return m_iTimeout;
	}
	
	public int getFlowControl() {
		return m_iFlowControl;
	}
	
	public boolean getAskNeedPassword() {
		return m_bAskNeedPassword;
	}
	
	public boolean addCardInfoToList(String sKey, String sValue) {
		if(m_oShowCardInfoList.containsKey(sKey))
			return true;
		
		m_oShowCardInfoList.put(sKey, sValue);
		
		return true;
	}
	
	public HashMap<String, String> getCardInfoList() {
		return m_oShowCardInfoList;
	}
	
	public void writeLog(int iInterfaceId, String sLog) {
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);

		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/rewrite_card_log." + sCurrentMonth;

		try{
			// Create file 
			FileWriter fstream = new FileWriter(sFilename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			sContent.append(sCurrentTime);
			sContent.append(" [s:");
			sContent.append(AppGlobal.g_oFuncStation.get().getStationId());
			sContent.append(" i:");
			sContent.append(iInterfaceId);
			sContent.append("] ");
			sContent.append(sLog);
			sContent.append("\n");
			
			out.write(sContent.toString());
			
			//Close the output stream
			out.close();
			fstream.close();
		}catch (Exception e){}//Catch exception if any
	}

	public void setCardNumber(String sCardNumber) {
		m_sCardNumber = sCardNumber;
	}
	
	public void setCardNumber1(String sCardNumber) {
		m_sCardNumber1 = sCardNumber;
	}
	
	public void setCardID(String sCardID) {
		m_sCardID = sCardID;
	}
	
	public void setCardMemberName(String sCardMemberName) {
		m_sCardMemberName = sCardMemberName;
	}
	
	public void setCardAmount(String sCardAmount) {
		m_sCardAmount = sCardAmount;
	}
	
	public void setIsNeedPassword(boolean bIsNeedCardPassword) {
		m_bIsNeedCardPassword = bIsNeedCardPassword;
	}
	
	public void setCardPassword(String sCardPassword) {
		m_sCardPassword = sCardPassword;
	}
	
	public void setPaymentAmount(BigDecimal oPaymentAmount) {
		m_oPaymentAmount = oPaymentAmount;
	}
	
	public void setAmountBeforeDeduct(BigDecimal AmountBeforeDeduct) {
		m_oAmountBeforeDeduct = AmountBeforeDeduct;
	}
	
	public void setAmountAfterDeduct(BigDecimal oAmountAfterDeduct) {
		m_oAmountAfterDeduct = oAmountAfterDeduct;
	}
	
	public void setTransactionNo(String sTransactionNo) {
		m_sTransactionNo = sTransactionNo;
	}
	
	public void setCardType(int iCardType) {
		m_sCardType = "";
		
		// Convert the card type value to card type desc
		if (m_sModelType.equals(FuncRewriteCard.TYPE_MODEL_ONE_CARD_SLN)) {
			// For further development
			/*
			switch (iCardType) {
				case 1:
					m_sCardType = "";
					break;
				default:
					break;
			}
			*/
		}
	}
	
	public void setOutBuffInfo(String sOutBuff){
		// OutBuf[0-2] = transaction card number
		// OutBuf[3] = payment card type
		// OutBuf[4-7] = remaining total after payment
		
//		m_iTransactionCardNumber = Integer.parseInt(sOutBuff.substring(0,6), 16);
		if(!sOutBuff.isEmpty())
			m_iPaymentCardType = Integer.parseInt(sOutBuff.substring(6,8), 16);
//		m_iRemainingTotal = Integer.parseInt(sOutBuff.substring(8,16), 16);
		
	}
	
	public String getCardNumber() {
		return m_sCardNumber;
	}
	
	public String getCardNumber1() {
		return m_sCardNumber1;
	}
	
	public String getCardID() {
		return m_sCardID;
	}
	
	public String getCardMemberName() {
		return m_sCardMemberName;
	}
	
	public String getCardAmount() {
		return m_sCardAmount;
	}
	
	public boolean isNeedCardPassword() {
		return m_bIsNeedCardPassword;
	}
	
	public String getCardPassword() {
		return m_sCardPassword;
	}
	
	public BigDecimal getPaymentAmount() {
		return m_oPaymentAmount;
	}
	
	public BigDecimal getAmountBeforeDeduct() {
		return m_oAmountBeforeDeduct;
	}
	
	public BigDecimal getAmountAfterDeduct() {
		return m_oAmountAfterDeduct;
	}
	
	public String getTransactionNo() {
		return m_sTransactionNo;
	}
	
	public String getCardType() {
		return m_sCardType;
	}
	
	public int getPaymentCardType(){
		return this.m_iPaymentCardType;
	}
	
	public HashMap<String, ArrayList<String>> constructRewritePaymentCardMapping(String sRecord) {
		if (sRecord.isEmpty())
			return null;
		
		HashMap<String, ArrayList<String>> oPaymentCardMapping = new HashMap<>();
		String[] sCardPaymentMapping = sRecord.split("\\r?\\n");
		
		for (String oMapping : sCardPaymentMapping) {
			// split the string with "="
			String[] sTemp = oMapping.split("="), sCards = null;
			String sPaymentCode = "";
			ArrayList <String> oCards = new ArrayList<>();
			
			// invalid mapping format checking
			if (sTemp.length != 2)
				return null;
			
			sPaymentCode = sTemp[0].replaceAll("\\s", "");
			sCards = sTemp[1].replaceAll("\\s", "").split(",");
			for(int i =0; i<sCards.length; i++)
				oCards.add(sCards[i]);
			oPaymentCardMapping.put(sPaymentCode, oCards);
		}
		return oPaymentCardMapping;
	}
	
	public boolean rewriteCardPaymentChecking(String [] sPaymentCodes, PosPaymentMethodList oPosPaymentMethodList){
		
		for(String sPaymentCode : sPaymentCodes){
			PosPaymentMethod oPosPaymentMethod = oPosPaymentMethodList.getPaymentMethodByCode(sPaymentCode);
			if(oPosPaymentMethod == null || !oPosPaymentMethod.getPaymentType().equals(PosPaymentMethod.PAYMENT_TYPE_REWRITE_CARD))
				return false;
		}
		
		return true;
	}
}
