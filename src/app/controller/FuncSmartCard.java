package app.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import externallib.StringLib;

import app.model.*;

public class FuncSmartCard {
	
	private class SmartCardStructure {
		private int m_iSector;
		private int m_iBlock;
		private int m_iPosition;
		private int m_iLength;
		
		public SmartCardStructure(int iSector, int iBlock, int iPosition, int iLength){
			m_iSector = iSector;
			m_iBlock = iBlock;
			m_iPosition = iPosition;
			m_iLength = iLength;
		}
		
		public int getSector(){
			return m_iSector;
		}
		
		public int getBlock(){
			return m_iBlock;
		}
		
		public int getPosition(){
			return m_iPosition;
		}
		
		public int getLength(){
			return m_iLength;
		}
	}
	
	// Support flag
	private boolean m_bSupportSmartCard;
	
	// Smart card device connection flag
	private boolean m_bIsConnected;
	
	// Smart Card Device
	private int m_iDevice;
	
	// Timeout setting
	private int m_iTimeout;
	
	// KeyA
	private String m_sKeyA;
	
	// Setup file
	private String m_sReaderInf;
	
	// Use smart card as employee card
	private boolean m_bUseSmartCardAsEmployeeCard;
	
	// Delay in ms for socket to device manager
	private int m_iSocketDelay;
	
	// Smart card structure
	private HashMap<String, SmartCardStructure> m_oSmartCardStructure;
	
	// Card information
	private String m_sSerialNo;
	private Date m_dtIssueDate;
	private Date m_dtExpiryDate;
	private Integer m_iMerchantId;
	private String m_sCardType;
	private String m_sFid;
	private String m_sCardNo;
	private String m_sCardStatus;
	private BigDecimal m_dCashAmount;
	private BigDecimal m_dFreeAmount;
	private long m_lBonus;
	private Date m_dtBonusExpiryDate;
	private long m_lLastEarn;
	private long m_lLastRedeem;
	private Date m_dtLastVisitDate;
	private long m_lVisitCnt;
		
	// Last error message
	private String m_sErrorMessage;
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage;
	}
	
	public FuncSmartCard(){
		m_bSupportSmartCard = false;
		m_bIsConnected = false;
		m_iTimeout = 3000;
		m_sKeyA = "";
		m_sReaderInf = "";
		m_bUseSmartCardAsEmployeeCard = false;
		m_iSocketDelay = 0;
		
		m_oSmartCardStructure = new HashMap<String, SmartCardStructure>();
		m_oSmartCardStructure.put("issue_date", new SmartCardStructure(0, 1, 8, 4));
		m_oSmartCardStructure.put("expiry_date", new SmartCardStructure(0, 1, 12, 4));
		m_oSmartCardStructure.put("merchant_id", new SmartCardStructure(0, 2, 0, 2));
		m_oSmartCardStructure.put("card_type", new SmartCardStructure(0, 2, 2, 1));
		m_oSmartCardStructure.put("fid", new SmartCardStructure(0, 2, 4, 1));
		m_oSmartCardStructure.put("card_no", new SmartCardStructure(0, 2, 11, 4));
		m_oSmartCardStructure.put("card_status", new SmartCardStructure(0, 2, 15, 1));
		m_oSmartCardStructure.put("member_no1", new SmartCardStructure(1, 4, 0, 16));
		m_oSmartCardStructure.put("member_no2", new SmartCardStructure(1, 5, 0, 4));
		m_oSmartCardStructure.put("member_type", new SmartCardStructure(1, 5, 4, 1));
		m_oSmartCardStructure.put("member_gender", new SmartCardStructure(1, 5, 5, 1));
		m_oSmartCardStructure.put("member_phone", new SmartCardStructure(1, 5, 8, 8));
		m_oSmartCardStructure.put("member_bday", new SmartCardStructure(1, 6, 0, 4));
		m_oSmartCardStructure.put("member_phone2", new SmartCardStructure(1, 6, 4, 4));
		m_oSmartCardStructure.put("member_name1", new SmartCardStructure(1, 6, 8, 8));
		m_oSmartCardStructure.put("member_name2", new SmartCardStructure(2, 8, 0, 16));
		m_oSmartCardStructure.put("deposit_amt", new SmartCardStructure(2, 9, 0, 4));
		m_oSmartCardStructure.put("max_card_amt", new SmartCardStructure(2, 9, 4, 4));
		m_oSmartCardStructure.put("max_credit_amt", new SmartCardStructure(2, 9, 8, 4));
		m_oSmartCardStructure.put("next_trans_id", new SmartCardStructure(2, 9, 12, 3));
		m_oSmartCardStructure.put("next_history_id", new SmartCardStructure(2, 9, 15, 1));
		m_oSmartCardStructure.put("cash_amt", new SmartCardStructure(2, 10, 0, 8));
		m_oSmartCardStructure.put("free_amt", new SmartCardStructure(2, 10, 8, 8));
		m_oSmartCardStructure.put("digital_sign", new SmartCardStructure(3, 12, 0, 16));
		m_oSmartCardStructure.put("member_pwd", new SmartCardStructure(3, 13, 0, 16));
		m_oSmartCardStructure.put("bonus", new SmartCardStructure(3, 14, 0, 4));
		m_oSmartCardStructure.put("bonus_exp_date", new SmartCardStructure(3, 14, 4, 4));
		m_oSmartCardStructure.put("trans_counter", new SmartCardStructure(3, 14, 8, 4));
		m_oSmartCardStructure.put("last_earn", new SmartCardStructure(3, 14, 12, 4));
		m_oSmartCardStructure.put("last_redeem", new SmartCardStructure(4, 16, 0, 4));
		m_oSmartCardStructure.put("last_visit", new SmartCardStructure(4, 16, 4, 4));
		m_oSmartCardStructure.put("visit_cnt", new SmartCardStructure(4, 16, 8, 2));
		
		m_sSerialNo = "";
		m_dtIssueDate = null;
		m_dtExpiryDate = null;
		m_iMerchantId = 0;
		m_sCardType = "";
		m_sCardNo = "";
		m_sCardStatus = "";
		m_dCashAmount = BigDecimal.ZERO;
		m_dFreeAmount = BigDecimal.ZERO;
		m_lBonus = 0;
		m_dtBonusExpiryDate = null;
		m_lLastEarn = 0;
		m_lLastRedeem = 0;
		m_dtLastVisitDate = null;
		m_lVisitCnt = 0;
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
					if(oInterfaceSetup.has("smartcard_setup") && oInterfaceSetup.getJSONObject("smartcard_setup").getJSONObject("params").getJSONObject("support").getInt("value") == 1)
						m_bSupportSmartCard = true;
					else
						break;
					
					// KeyA
					m_sKeyA = oInterfaceSetup.getJSONObject("smartcard_setup").getJSONObject("params").getJSONObject("rf_keyA").getString("value");
					
					// Reader setup mwcard.inf
					m_sReaderInf = oInterfaceSetup.getJSONObject("smartcard_setup").getJSONObject("params").getJSONObject("setup_file_path").getString("value");
					
					// Use smart card as employee card
					if(oInterfaceSetup.getJSONObject("smartcard_setup").getJSONObject("params").getJSONObject("smart_card_as_employee_card").getInt("value") == 1)
						m_bUseSmartCardAsEmployeeCard = true;
					
					// Timeout
					m_iTimeout = Integer.parseInt(oInterfaceSetup.getJSONObject("smartcard_setup").getJSONObject("params").getJSONObject("process_timeout").getString("value"));
					
					// Delay for socket to device manager
					m_iSocketDelay = Integer.parseInt(oInterfaceSetup.getJSONObject("smartcard_setup").getJSONObject("params").getJSONObject("device_communication_interval").getString("value"));
					
				}catch (Exception e) {
					AppGlobal.stack2Log(e);
				}
			}
		}
	}
	
	private String decodeString(String sSource, int iDecodeLen){
		StringBuilder sResult = new StringBuilder();
		String sTmp = "";
		int iTmp = 0;
		
		try{
			sSource = StringLib.fillZero(sSource, iDecodeLen*2+2);
			
			for (int i=0; i<iDecodeLen; i++) {
				sTmp = sSource.substring(i*2, (i*2)+2);
				if(sTmp.length() == 0)
					break;
				iTmp = Integer.valueOf(sTmp, 16);
				char c = (char)(iTmp & 0xFF);
				sResult.append(c);
			}
		}catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
		
		return sResult.toString();
	}
	
	public boolean isSupportSmartCard(){
		return m_bSupportSmartCard;
	}
	
	public void setConnected(boolean bConnected){
		m_bIsConnected = bConnected;
	}
	
	public boolean isConnected(){
		return m_bIsConnected;
	}
	
	public void setDevice(int iDevice){
		m_iDevice = iDevice;
	}
	
	public void setSerialNo(String sSerialNo){
		m_sSerialNo = sSerialNo;
	}
	
	public void setCardValue(String sFieldName, String sBlockData){
		SimpleDateFormat oFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
		
		int iPosition = this.getFieldPosition(sFieldName);
		int iLength = this.getFieldLength(sFieldName);
		String sValue = decodeString(sBlockData, 32).substring(iPosition, iPosition+iLength);
		
		// Set value according to field
		StringBuilder oStringBuilder = new StringBuilder();
		for(int i=0; i<sValue.length(); i++){
			oStringBuilder.insert(0, String.format("%02X", Integer.valueOf(sValue.charAt(i))));
		}
		String sDecodeValue = oStringBuilder.toString();
		
		if(sFieldName.equals("issue_date")){
			try {
				if(Long.parseLong(sDecodeValue, 16) > 0)
					m_dtIssueDate = oFormat.parse(String.valueOf(Long.parseLong(sDecodeValue, 16)));
			} catch (NumberFormatException e) {
			} catch (ParseException e) {
			}
		}else
		if(sFieldName.equals("expiry_date")){
			try {
				if(Long.parseLong(sDecodeValue, 16) > 0)
					m_dtExpiryDate = oFormat.parse(String.valueOf(Long.parseLong(sDecodeValue, 16)));
			} catch (NumberFormatException e) {
			} catch (ParseException e) {
			}
		}else
		if(sFieldName.equals("merchant_id")){
			m_iMerchantId = Integer.parseInt(sDecodeValue, 16);
		}else
		if(sFieldName.equals("card_type")){
			m_sCardType = sDecodeValue.substring(sDecodeValue.length()-1);
		}else
		if(sFieldName.equals("fid")){
		}else
		if(sFieldName.equals("card_no")){
			m_sCardNo = String.valueOf(Integer.parseInt(sDecodeValue, 16));
		}else
		if(sFieldName.equals("card_status")){
			m_sCardStatus = sDecodeValue.substring(sDecodeValue.length()-1);
		}else
		if(sFieldName.equals("member_no1")){
			
		}else
		if(sFieldName.equals("member_no2")){
			
		}else
		if(sFieldName.equals("member_type")){
			
		}else
		if(sFieldName.equals("member_gender")){
			
		}else
		if(sFieldName.equals("member_phone")){
			
		}else
		if(sFieldName.equals("member_bday")){
			
		}else
		if(sFieldName.equals("member_phone2")){
			
		}else
		if(sFieldName.equals("member_name1")){
			
		}else
		if(sFieldName.equals("member_name2")){
			
		}else
		if(sFieldName.equals("deposit_amt")){
			
		}else
		if(sFieldName.equals("max_card_amt")){
			
		}else
		if(sFieldName.equals("max_credit_amt")){
			
		}else
		if(sFieldName.equals("next_trans_id")){
			
		}else
		if(sFieldName.equals("next_history_id")){
			
		}else
		if(sFieldName.equals("cash_amt")){
			m_dCashAmount = new BigDecimal(Double.longBitsToDouble(Long.parseLong(sDecodeValue, 16)));
		}else
		if(sFieldName.equals("free_amt")){
			m_dFreeAmount = new BigDecimal(Double.longBitsToDouble(Long.parseLong(sDecodeValue, 16)));
		}else
		if(sFieldName.equals("digital_sign")){
			
		}else
		if(sFieldName.equals("member_pwd")){
			
		}else
		if(sFieldName.equals("bonus")){
			m_lBonus = Long.parseLong(sDecodeValue, 16);
		}else
		if(sFieldName.equals("bonus_exp_date")){
			try {
				if(Long.parseLong(sDecodeValue, 16) > 0)
					m_dtBonusExpiryDate = oFormat.parse(String.valueOf(Long.parseLong(sDecodeValue, 16)));
			} catch (NumberFormatException e) {
			} catch (ParseException e) {
			}
		}else
		if(sFieldName.equals("trans_counter")){
			
		}else
		if(sFieldName.equals("last_earn")){
			m_lLastEarn = Long.parseLong(sDecodeValue, 16);
		}else
		if(sFieldName.equals("last_redeem")){
			m_lLastRedeem = Long.parseLong(sDecodeValue, 16);
		}else
		if(sFieldName.equals("last_visit")){
			try {
				if(Long.parseLong(sDecodeValue, 16) > 0)
					m_dtLastVisitDate = oFormat.parse(String.valueOf(Long.parseLong(sDecodeValue, 16)));
			} catch (NumberFormatException e) {
			} catch (ParseException e) {
			}
		}else
		if(sFieldName.equals("visit_cnt")){
			m_lVisitCnt = Long.parseLong(sDecodeValue, 16);
		}
	}
	
	public int getDevice(){
		return m_iDevice;
	}
	
	public int getTimeout(){
		return m_iTimeout;
	}
	
	public String getKeyA(){
		return m_sKeyA;
	}
	
	public String getReaderInf(){
		return m_sReaderInf;
	}
	
	public int getSocketDelay(){
		return m_iSocketDelay;
	}
	
	public String getSerialNo(){
		return m_sSerialNo;
	}
	
	public Date getIssueDate(){
		return m_dtIssueDate;
	}
	
	public Date getExpiryDate(){
		return m_dtExpiryDate;
	}
	
	public int getMerchantId(){
		return m_iMerchantId;
	}
	
	public String getCardType(){
		return m_sCardType;
	}
	
	public String getFId(){
		return m_sFid;
	}
	
	public String getCardNo(){
		return m_sCardNo;
	}
	
	public String getCardStatus(){
		return m_sCardStatus;
	}
	
	public BigDecimal getCashAmount(){
		return m_dCashAmount;
	}
	
	public BigDecimal getFreeAmount(){
		return m_dFreeAmount;
	}
	
	public long getBonus(){
		return m_lBonus;
	}
	
	public Date getBonusExpiryDate(){
		return m_dtBonusExpiryDate;
	}
	
	public long getLastEarnBonus(){
		return m_lLastEarn;
	}
	
	public long getLastRedeemBonus(){
		return m_lLastRedeem;
	}
	
	public Date getLastVisitDate(){
		return m_dtLastVisitDate;
	}
	
	public long getVisitCount(){
		return m_lVisitCnt;
	}
	
	public boolean useSmartCardAsEmployeeCard(){
		return m_bUseSmartCardAsEmployeeCard;
	}
	
	public int getFieldSector(String sField){
		SmartCardStructure oSmartCardStructure = m_oSmartCardStructure.get(sField);
		return oSmartCardStructure.getSector();
	}
	
	public int getFieldBlock(String sField){
		SmartCardStructure oSmartCardStructure = m_oSmartCardStructure.get(sField);
		return oSmartCardStructure.getBlock();
	}
	
	public int getFieldPosition(String sField){
		SmartCardStructure oSmartCardStructure = m_oSmartCardStructure.get(sField);
		return oSmartCardStructure.getPosition();
	}
	
	public int getFieldLength(String sField){
		SmartCardStructure oSmartCardStructure = m_oSmartCardStructure.get(sField);
		return oSmartCardStructure.getLength();
	}
}
