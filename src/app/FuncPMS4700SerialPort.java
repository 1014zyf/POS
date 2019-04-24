package app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import externallib.StringLib;
import externallib.Util;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import om.MenuItemDeptGroup;
import om.MenuItemDeptGroupList;
import om.MenuItemDeptGroupLookup;
import om.PosCheckDiscount;
import om.PosCheckPayment;
import om.PosInterfaceConfig;

public class FuncPMS4700SerialPort {	
	private static final char SOH = '\u0001';
	private static final char STX = '\u0002';
	private static final char ETX = '\u0003';
	private static final char EOT = '\u0004';
	private static final char ACK = '\u0006';
	private static final char NAK = '\u0015';
	
	private static final String MESG_TYPE_ENQ = "1";
	private static final String MESG_TYPE_POST = "2";
	
	private SerialPort m_oSerialPort;
	private PosInterfaceConfig m_oPosInterfaceConfig;
	private JSONObject m_oInterfaceSetup;
	private ArrayList<String> m_oEnquiryRequestSeq;
	private HashMap<String, HashMap<String, String>> m_oEnquiryRequest;
	private ArrayList<String> m_oEnquiryResponseSeq;
	private HashMap<String, HashMap<String, String>> m_oEnquiryResponse;
	private ArrayList<String> m_oPostingRequestSeq;
	private HashMap<String, HashMap<String, String>> m_oPostingRequest;
	private ArrayList<String> m_oPostingResponseSeq;
	private HashMap<String, HashMap<String, String>> m_oPostingResponse;
	private byte[] m_oResponseBytes;
	private String m_sLastErrorType;
	private int m_iLastErrorCode;
	private String m_sLastErrorString;
	private String m_sLastResult;
	
	public FuncPMS4700SerialPort() {
		m_oSerialPort = null;
		m_oPosInterfaceConfig = null;
		m_oInterfaceSetup = null;
		m_oResponseBytes = new byte[146];
		m_sLastErrorType = "";
		m_iLastErrorCode = 0;
		m_sLastErrorString = "";
		m_sLastResult = "";
		
		m_oEnquiryRequestSeq = new ArrayList<String>();
		m_oEnquiryRequestSeq.add("messageType");
		m_oEnquiryRequestSeq.add("retransmitFlag");
		m_oEnquiryRequestSeq.add("inquiryMessage");
		m_oEnquiryRequestSeq.add("employeeNo");
		
		m_oEnquiryResponseSeq = new ArrayList<String>();
		m_oEnquiryResponseSeq.add("messageType");
		for(int i=1; i<=8; i++)
			m_oEnquiryResponseSeq.add("message"+i);
		
		m_oPostingRequestSeq = new ArrayList<String>();
		m_oPostingRequestSeq.add("messageType");
		m_oPostingRequestSeq.add("retransmitFlag");
		m_oPostingRequestSeq.add("accountID");
		m_oPostingRequestSeq.add("expiryDate");
		m_oPostingRequestSeq.add("fieldInformation");
		m_oPostingRequestSeq.add("fieldNumber");
		m_oPostingRequestSeq.add("postingEmployeeNum");
		m_oPostingRequestSeq.add("servingEmployeeNum");
		m_oPostingRequestSeq.add("outletCode");
		m_oPostingRequestSeq.add("servingPeriodNum");
		m_oPostingRequestSeq.add("guestCheckNumber");
		m_oPostingRequestSeq.add("stationCheckGroup");
		m_oPostingRequestSeq.add("covers");
		m_oPostingRequestSeq.add("paymentNumber");
		m_oPostingRequestSeq.add("paymentAmount");
		m_oPostingRequestSeq.add("itemizer1");
		m_oPostingRequestSeq.add("itemizer2");
		m_oPostingRequestSeq.add("itemizer3");
		m_oPostingRequestSeq.add("itemizer4");
		m_oPostingRequestSeq.add("itemizer5");
		m_oPostingRequestSeq.add("itemizer6");
		m_oPostingRequestSeq.add("itemizer7");
		m_oPostingRequestSeq.add("itemizer8");
		m_oPostingRequestSeq.add("itemizer9");
		m_oPostingRequestSeq.add("itemizer10");
		m_oPostingRequestSeq.add("discountTotal");
		m_oPostingRequestSeq.add("tipsAmount");
		m_oPostingRequestSeq.add("scTotal");
		m_oPostingRequestSeq.add("tax1Total");
		m_oPostingRequestSeq.add("tax2Total");
		m_oPostingRequestSeq.add("tax3Total");
		m_oPostingRequestSeq.add("reserved");
		m_oPostingRequestSeq.add("previousPaymentTotal");
		
		m_oPostingResponseSeq = new ArrayList<String>();
		m_oPostingResponseSeq.add("messageType");
		m_oPostingResponseSeq.add("acceptanceDenialMessage");
		for(int i=1; i<=8; i++)
			m_oPostingResponseSeq.add("message"+i);
	}
	
	public void init(PosInterfaceConfig oPosInterfaceConfig) {
		m_oSerialPort = null;
		m_oPosInterfaceConfig = oPosInterfaceConfig;
		m_oInterfaceSetup = oPosInterfaceConfig.getInterfaceConfig();
		initEnquiryStucture();
		initPostingStucture();
		m_sLastErrorType = "";
		m_iLastErrorCode = 0;
		m_sLastErrorString = "";
		m_sLastResult = "";
	}
	
	public JSONObject doEnquiry(HashMap<String, String> oEnquiryInfo) {
		boolean bPostingAskInfo = false, bEnquiry = true;
		int iPrepostType = 0, iNumberOfItemizer = 4;
		String sMessageType = "";	
		String sSendDataString = "";
		HashMap<String, HashMap<String, String>> oReceiveData = null;
		JSONObject responseJSONObject = null;
		
		//get setting
		if(oEnquiryInfo.containsKey("postingAskInfo") && oEnquiryInfo.get("postingAskInfo").equals("1"))
			bPostingAskInfo = true;
		if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_prepost_type"))
			iPrepostType = m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_prepost_type").optInt("value", 0);
		if(m_oInterfaceSetup.optJSONObject("itemizer_setting").optJSONObject("params").has("itemizer_type"))
			iNumberOfItemizer = m_oInterfaceSetup.optJSONObject("itemizer_setting").optJSONObject("params").optJSONObject("itemizer_type").optInt("value", 4);
		
		//construct enquiry content
		if(!bPostingAskInfo || (bPostingAskInfo && iPrepostType != 0)) {
			sMessageType = FuncPMS4700SerialPort.MESG_TYPE_ENQ;
			m_oEnquiryRequest.get("messageType").put("value", sMessageType);
			m_oEnquiryRequest.get("retransmitFlag").put("value", " ");
			m_oEnquiryRequest.get("inquiryMessage").put("value", oEnquiryInfo.get("enquiryNumber"));
			m_oEnquiryRequest.get("employeeNo").put("value", oEnquiryInfo.get("employee"));
		}else {
			bEnquiry = false;
			sMessageType = FuncPMS4700SerialPort.MESG_TYPE_POST;
			m_oPostingRequest.get("messageType").put("value", sMessageType);
			m_oPostingRequest.get("accountID").put("value", subStringWithLength(oEnquiryInfo.get("enquiryNumber"), Integer.valueOf(m_oPostingRequest.get("accountID").get("length")).intValue()));
		}
		
		//build sending content
		sSendDataString = buildPacketDataString(bEnquiry, iNumberOfItemizer);
		
		//open serial port	
		if(!openSerialPort()) {
			try {
				responseJSONObject = new JSONObject();
				responseJSONObject.put("enquiryResult", false);
				responseJSONObject.put("errorCode", m_iLastErrorCode);
				writeLog(m_oPosInterfaceConfig.getInterfaceId(), "Fail to open port - "+m_sLastErrorString);
			}catch (JSONException e) {
				return null;
			}
			return responseJSONObject;
		}
				
		//send to serial port
		if(!pmsSend(sSendDataString)) {
			pmsClose();
			try {
				responseJSONObject = new JSONObject();
				responseJSONObject.put("enquiryResult", false);
				responseJSONObject.put("errorCode", m_iLastErrorCode);
				writeLog(m_oPosInterfaceConfig.getInterfaceId(), "Fail to send data - "+m_sLastErrorString);
			}catch (JSONException e) {
				return null;
			}
			return responseJSONObject;
		}
		
		//receive form serial port
		if(!pmsReceive()) {
			pmsClose();
			try {
				responseJSONObject = new JSONObject();
				responseJSONObject.put("enquiryResult", false);
				responseJSONObject.put("errorCode", m_iLastErrorCode);
				writeLog(m_oPosInterfaceConfig.getInterfaceId(), "Fail to receive data - "+m_sLastErrorString);
			}catch (JSONException e) {
				return null;
			}
			return responseJSONObject;
		}
		
		//close serial port
		pmsClose();
		
		//break the received string
		breakPacketDataString(bEnquiry);
		
		//construct the guest list
		if(!bPostingAskInfo || (bPostingAskInfo && iPrepostType != 0)) 
			oReceiveData = m_oEnquiryResponse;
		else 
			oReceiveData = m_oPostingResponse;
			
		if(!oReceiveData.get("messageType").get("value").equals(sMessageType)) {
			try {
				responseJSONObject = new JSONObject();
				responseJSONObject.put("enquiryResult", false);
				responseJSONObject.put("errorCode", 20);
				m_sLastErrorString = "Incorrect enquiry response message type:"+sMessageType;
				writeLog(m_oPosInterfaceConfig.getInterfaceId(), "Fail to send data - "+m_sLastErrorString);
			}catch (JSONException e) {
				return null;
			}
			return responseJSONObject;
		}
		
		boolean bSuccess = true;
		if(!bPostingAskInfo || (bPostingAskInfo && iPrepostType != 0)) {
			char cFirstChar = oReceiveData.get("message1").get("value").charAt(0);
			if(bPostingAskInfo && cFirstChar == '/') {
				m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
				m_iLastErrorCode = 21;
				m_sLastErrorString = "PMS posting is rejected";
				bSuccess = false;
			}
		}else {
			if(oReceiveData.get("acceptanceDenialMessage").get("value").isEmpty()) {
				m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
				m_iLastErrorCode = 9;
				m_sLastErrorString = "Empty posting response message";
				bSuccess = false;
			}else {
				char cFirstChar = oReceiveData.get("acceptanceDenialMessage").get("value").charAt(0);
				if(cFirstChar == '/') {
					String sErrorString = (oReceiveData.get("acceptanceDenialMessage").get("value").substring(1)).trim();
					bSuccess = false;
					if(sErrorString.isEmpty()) {
						m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
						m_iLastErrorCode = 21;
						m_sLastErrorString = "PMS posting is rejected";
					}else {
						m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
						m_iLastErrorCode = 0;
						m_sLastErrorString = sErrorString;
					}
				}
			}
		}
		
		if(!bSuccess) {
			try {
				responseJSONObject = new JSONObject();
				responseJSONObject.put("enquiryResult", false);
				responseJSONObject.put("errorCode", m_iLastErrorCode);
				responseJSONObject.put("errorMessage", m_sLastErrorString);
				writeLog(m_oPosInterfaceConfig.getInterfaceId(), "Enquiry error - "+m_sLastErrorString);
			}catch (JSONException e) {
				return null;
			}
			return responseJSONObject;
		}
		
		JSONArray oGuestListArray = new JSONArray();
		for(int i=1; i<=8; i++) {
			if(!oReceiveData.get("message"+i).get("value").isEmpty()) {
				JSONObject oGuest = new JSONObject();
				
				try {
					oGuest.put("guestNumber", "");
					oGuest.put("guestName", oReceiveData.get("message"+i).get("value"));
					oGuest.put("roomNumber", "");
					oGuest.put("arrivalDate", "");
					oGuest.put("departureDate", "");
					oGuest.put("guestFirstName", "");
					oGuest.put("guestLanguage", "");
					oGuest.put("guestGroupNumber", "");
					oGuest.put("guestTitle", "");
					oGuest.put("guestVip", "");
					oGuest.put("balanceAmount", 0);
					oGuest.put("creditLimit", 0);
					oGuest.put("userInfo", new JSONArray());
					oGuest.put("registerNumber", "");
					oGuest.put("guestFileNumber", 0);
					oGuest.put("noPost", false);
					oGuest.put("targetPaymentMethod", "");
					oGuest.put("line", i);
					
					oGuestListArray.put(oGuest);
				}catch(JSONException e) {}
			}
		}
		
		try {
			responseJSONObject = new JSONObject();
			responseJSONObject.put("enquiryResult", true);
			responseJSONObject.put("guests", oGuestListArray);
		}catch (JSONException e) {
			return null;
		}
		
		return responseJSONObject;
	}
	
	public boolean doPrePost(FuncCheck oFuncCheck, PosCheckPayment oCheckPayment, JSONObject oPMSPostingInfo) {
		int iPrepostType = 0, iNumberOfItemizer = 4, iPacketDecimalPoint = 2;
		boolean bEnquiry = true;
		String sMessageType = "", sSendDataString = "";
		HashMap<String, HashMap<String, String>> oReceiveData = null;
		
		//get setup
		if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_prepost_type"))
			iPrepostType = m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_prepost_type").optInt("value", 0);
		if(m_oInterfaceSetup.optJSONObject("itemizer_setting").optJSONObject("params").has("itemizer_type"))
			iNumberOfItemizer = m_oInterfaceSetup.optJSONObject("itemizer_setting").optJSONObject("params").optJSONObject("itemizer_type").optInt("value", 4);
		if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_packet_decimal_point") && m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_packet_decimal_point").optInt("value", 2) <= 4)
			iPacketDecimalPoint = m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_packet_decimal_point").optInt("value", 2);
		
		//prepare the posting information
		preparePostingInfo(oFuncCheck, oCheckPayment, oPMSPostingInfo, true, false);
		
		//construct enquiry content
		if(iPrepostType > 0) {
			sMessageType = FuncPMS4700SerialPort.MESG_TYPE_ENQ;
			m_oEnquiryRequest.get("messageType").put("value", sMessageType);
			m_oEnquiryRequest.get("retransmitFlag").put("value", " ");
			m_oEnquiryRequest.get("inquiryMessage").put("value", oPMSPostingInfo.optString("roomNumber", ""));
			m_oEnquiryRequest.get("employeeNo").put("value", oPMSPostingInfo.optString("employee", ""));
		}else {
			bEnquiry = false;
			sMessageType = FuncPMS4700SerialPort.MESG_TYPE_POST;
			m_oPostingRequest.get("messageType").put("value", sMessageType);
			m_oPostingRequest.get("accountID").put("value", subStringWithLength(oPMSPostingInfo.optString("roomNumber", ""), Integer.valueOf(m_oPostingRequest.get("accountID").get("length")).intValue()));
			m_oPostingRequest.get("expiryDate").put("value", subStringWithLength(oPMSPostingInfo.optString("expiryDate", ""), Integer.valueOf(m_oPostingRequest.get("expiryDate").get("length")).intValue()));
			m_oPostingRequest.get("fieldInformation").put("value", "");
			m_oPostingRequest.get("fieldNumber").put("value", subStringWithLength(oPMSPostingInfo.optString("fieldNumber", ""), Integer.valueOf(m_oPostingRequest.get("fieldNumber").get("length")).intValue()));
			m_oPostingRequest.get("postingEmployeeNum").put("value", subStringWithLength(oPMSPostingInfo.optString("employee", ""), Integer.valueOf(m_oPostingRequest.get("postingEmployeeNum").get("length")).intValue()));
			m_oPostingRequest.get("servingEmployeeNum").put("value", subStringWithLength(oPMSPostingInfo.optString("employee", ""), Integer.valueOf(m_oPostingRequest.get("servingEmployeeNum").get("length")).intValue()));
			m_oPostingRequest.get("outletCode").put("value", subStringWithLength(oPMSPostingInfo.optString("outlet", ""), Integer.valueOf(m_oPostingRequest.get("outletCode").get("length")).intValue()));
			m_oPostingRequest.get("servingPeriodNum").put("value", subStringWithLength(oPMSPostingInfo.optString("servingPeriod", ""), Integer.valueOf(m_oPostingRequest.get("servingPeriodNum").get("length")).intValue()));
			m_oPostingRequest.get("guestCheckNumber").put("value", subStringWithLength(oPMSPostingInfo.optString("checkNumber", ""), Integer.valueOf(m_oPostingRequest.get("guestCheckNumber").get("length")).intValue()));
			m_oPostingRequest.get("stationCheckGroup").put("value", subStringWithLength(oPMSPostingInfo.optString("checkGroupNumber", ""), Integer.valueOf(m_oPostingRequest.get("stationCheckGroup").get("length")).intValue()));
			m_oPostingRequest.get("covers").put("value", subStringWithLength(oPMSPostingInfo.optString("cover", ""), Integer.valueOf(m_oPostingRequest.get("covers").get("length")).intValue()));
			m_oPostingRequest.get("paymentNumber").put("value", subStringWithLength(oPMSPostingInfo.optString("paymentType", ""), Integer.valueOf(m_oPostingRequest.get("paymentNumber").get("length")).intValue()));
			m_oPostingRequest.get("paymentAmount").put("value", stringWithFormat(oPMSPostingInfo.optString("postAmount", "0"), iPacketDecimalPoint));
			JSONArray oItemizers = oPMSPostingInfo.optJSONArray("itemizers");
			for(int i=1; i<=iNumberOfItemizer; i++) {
				boolean bFound = false;
				if(oItemizers != null && oItemizers.length() > 0) {
					for(int j=0; j<iNumberOfItemizer; j++) {
						if(Integer.valueOf(oItemizers.optJSONObject(j).optString("index", "0")) == i) {
							String sValue = oItemizers.optJSONObject(j).optString("total", "0");
							m_oPostingRequest.get("itemizer"+i).put("value", stringWithFormat(sValue, iPacketDecimalPoint));
							bFound = true;
							break;
						}
					}
				}
				
				if(!bFound)
					m_oPostingRequest.get("itemizer"+i).put("value", stringWithFormat("0", iPacketDecimalPoint));
			}
			JSONArray oDiscounts = oPMSPostingInfo.optJSONArray("discounts");
			for(int i=1; i<=1; i++) {
				boolean bFound = false;
				if(oDiscounts != null && oDiscounts.length() > 0) {				
					for(int j=0; j<1; j++) {
						if(Integer.valueOf(oDiscounts.optJSONObject(j).optString("index", "0")) == i) {
							String sValue = oDiscounts.optJSONObject(j).optString("total", "0");
							m_oPostingRequest.get("discountTotal").put("value", stringWithFormat(sValue, iPacketDecimalPoint));
							bFound = true;
							break;
						}
					}
				}
				
				if(!bFound)
					m_oPostingRequest.get("discountTotal").put("value", stringWithFormat("0", iPacketDecimalPoint));
			}
			m_oPostingRequest.get("tipsAmount").put("value", stringWithFormat(oPMSPostingInfo.optString("tips", "0"), iPacketDecimalPoint));
			m_oPostingRequest.get("scTotal").put("value", stringWithFormat(oPMSPostingInfo.optString("serviceCharge", "0"), iPacketDecimalPoint));
			JSONArray oTaxes = oPMSPostingInfo.optJSONArray("taxes");
			for(int i=1; i<=3; i++) {
				boolean bFound = false;
				if(oTaxes != null && oTaxes.length() > 0) {
					for(int j=0; j<3; j++) {
						if(Integer.valueOf(oTaxes.optJSONObject(j).optString("index", "0")) == i) {
							String sValue = oTaxes.optJSONObject(j).optString("total", "0");
							m_oPostingRequest.get("tax"+i+"Total").put("value", stringWithFormat(sValue, iPacketDecimalPoint));
							bFound = true;
							break;
						}
					}
				}
				
				if(!bFound)
					m_oPostingRequest.get("tax"+i+"Total").put("value", stringWithFormat("0", iPacketDecimalPoint));
			}
			m_oPostingRequest.get("reserved").put("value", stringWithFormat("0", iPacketDecimalPoint));
			m_oPostingRequest.get("previousPaymentTotal").put("value", stringWithFormat(oPMSPostingInfo.optString("previousPaymentAmount", "0"), iPacketDecimalPoint));
		}
		
		//build sending content
		sSendDataString = buildPacketDataString(bEnquiry, iNumberOfItemizer);
				
		//open serial port	
		if(!openSerialPort()) {
			writeLog(m_oPosInterfaceConfig.getInterfaceId(), "Fail to open port"+m_sLastErrorString);
			return false;
		}
		
		//send to serial port
		if(!pmsSend(sSendDataString)) {
			writeLog(m_oPosInterfaceConfig.getInterfaceId(), "Fail to send data"+m_sLastErrorString);
			pmsClose();
			return false;
		}
				
		//receive form serial port
		if(!pmsReceive()) {
			writeLog(m_oPosInterfaceConfig.getInterfaceId(), "Fail to receive data"+m_sLastErrorString);
			pmsClose();
			return false;
		}
		
		//close serial port
		pmsClose();
		
		//break the received string
		breakPacketDataString(bEnquiry);
		
		//result handling
		boolean bSuccess = true;
		if(iPrepostType > 0)
			oReceiveData = m_oEnquiryResponse;
		else 
			oReceiveData = m_oPostingResponse;
		
		if(!oReceiveData.get("messageType").get("value").equals(sMessageType)) {
			m_iLastErrorCode = 20;
			m_sLastErrorString = "Incorrect enquiry response message type:"+sMessageType;
			bSuccess = false;
		}
		
		if(iPrepostType > 0) {
			char cFirstChar = oReceiveData.get("message1").get("value").charAt(0);
			if(cFirstChar == '/') {
				m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
				m_iLastErrorCode = 21;
				m_sLastErrorString = "PMS posting is rejected";
				bSuccess = false;
			}
		}else {
			if(oReceiveData.get("acceptanceDenialMessage").get("value").isEmpty()) {
				m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
				m_iLastErrorCode = 9;
				m_sLastErrorString = "Empty posting response message";
				bSuccess = false;
			}else {
				char cFirstChar = oReceiveData.get("acceptanceDenialMessage").get("value").charAt(0);
				if(cFirstChar == '/') {
					String sErrorString = (oReceiveData.get("acceptanceDenialMessage").get("value").substring(1)).trim();
					bSuccess = false;
					if(sErrorString.isEmpty()) {
						m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
						m_iLastErrorCode = 21;
						m_sLastErrorString = "PMS posting is rejected";
					}else {
						m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
						m_iLastErrorCode = 0;
						m_sLastErrorString = sErrorString;
					}
				}
			}
		}
		
		if(!bSuccess) {
			writeLog(m_oPosInterfaceConfig.getInterfaceId(), m_sLastErrorString);
			return false;
		}
			
		m_sLastResult = oReceiveData.get("message"+oPMSPostingInfo.optString("fieldNumber", "1")).get("value");
		return true;
	}
	
	public boolean doPosting(FuncCheck oFuncCheck, PosCheckPayment oCheckPayment, JSONObject oPMSPostingInfo, boolean bVoiding) {
		String sSendDataString = "";
		int iPacketDecimalPoint = 2;
		int iNumberOfItemizer = 4;
		
		if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_packet_decimal_point") && m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_packet_decimal_point").optInt("value", 2) <= 4)
			iPacketDecimalPoint = m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_packet_decimal_point").optInt("value", 2);
		if(m_oInterfaceSetup.optJSONObject("itemizer_setting").optJSONObject("params").has("itemizer_type"))
			iNumberOfItemizer = m_oInterfaceSetup.optJSONObject("itemizer_setting").optJSONObject("params").optJSONObject("itemizer_type").optInt("value", 4);
		
		//prepare the posting information
		preparePostingInfo(oFuncCheck, oCheckPayment, oPMSPostingInfo, false, bVoiding);
		
		//construct the posting content
		m_oPostingRequest.get("messageType").put("value", MESG_TYPE_POST);
		m_oPostingRequest.get("accountID").put("value", subStringWithLength(oPMSPostingInfo.optString("roomNumber", ""), Integer.valueOf(m_oPostingRequest.get("accountID").get("length")).intValue()));
		m_oPostingRequest.get("expiryDate").put("value", subStringWithLength(oPMSPostingInfo.optString("expiryDate", ""), Integer.valueOf(m_oPostingRequest.get("expiryDate").get("length")).intValue()));
		m_oPostingRequest.get("fieldInformation").put("value", subStringWithLength(oPMSPostingInfo.optString("guestName", ""), Integer.valueOf(m_oPostingRequest.get("fieldInformation").get("length")).intValue()));
		m_oPostingRequest.get("fieldNumber").put("value", subStringWithLength(oPMSPostingInfo.optString("fieldNumber", ""), Integer.valueOf(m_oPostingRequest.get("fieldNumber").get("length")).intValue()));
		m_oPostingRequest.get("postingEmployeeNum").put("value", subStringWithLength(oPMSPostingInfo.optString("employee", ""), Integer.valueOf(m_oPostingRequest.get("postingEmployeeNum").get("length")).intValue()));
		m_oPostingRequest.get("servingEmployeeNum").put("value", subStringWithLength(oPMSPostingInfo.optString("employee", ""), Integer.valueOf(m_oPostingRequest.get("servingEmployeeNum").get("length")).intValue()));
		m_oPostingRequest.get("outletCode").put("value", subStringWithLength(oPMSPostingInfo.optString("outlet", ""), Integer.valueOf(m_oPostingRequest.get("outletCode").get("length")).intValue()));
		m_oPostingRequest.get("servingPeriodNum").put("value", subStringWithLength(oPMSPostingInfo.optString("servingPeriod", ""), Integer.valueOf(m_oPostingRequest.get("servingPeriodNum").get("length")).intValue()));
		m_oPostingRequest.get("guestCheckNumber").put("value", subStringWithLength(oPMSPostingInfo.optString("checkNumber", ""), Integer.valueOf(m_oPostingRequest.get("guestCheckNumber").get("length")).intValue()));
		m_oPostingRequest.get("stationCheckGroup").put("value", subStringWithLength(oPMSPostingInfo.optString("checkGroupNumber", ""), Integer.valueOf(m_oPostingRequest.get("stationCheckGroup").get("length")).intValue()));
		m_oPostingRequest.get("covers").put("value", subStringWithLength(oPMSPostingInfo.optString("cover", ""), Integer.valueOf(m_oPostingRequest.get("covers").get("length")).intValue()));
		m_oPostingRequest.get("paymentNumber").put("value", subStringWithLength(oPMSPostingInfo.optString("paymentType", ""), Integer.valueOf(m_oPostingRequest.get("paymentNumber").get("length")).intValue()));
		m_oPostingRequest.get("paymentAmount").put("value", stringWithFormat(oPMSPostingInfo.optString("postAmount", "0"), iPacketDecimalPoint));
		JSONArray oItemizers = oPMSPostingInfo.optJSONArray("itemizers");
		for(int i=1; i<=iNumberOfItemizer; i++) {
			boolean bFound = false;
			if(oItemizers != null && oItemizers.length() > 0) {
				for(int j=0; j<iNumberOfItemizer; j++) {
					if(Integer.valueOf(oItemizers.optJSONObject(j).optString("index", "0")) == i) {
						String sValue = oItemizers.optJSONObject(j).optString("total", "0");
						m_oPostingRequest.get("itemizer"+i).put("value", stringWithFormat(sValue, iPacketDecimalPoint));
						bFound = true;
						break;
					}
				}
			}
			
			if(!bFound)
				m_oPostingRequest.get("itemizer"+i).put("value", stringWithFormat("0", iPacketDecimalPoint));
		}
		JSONArray oDiscounts = oPMSPostingInfo.optJSONArray("discounts");
		for(int i=1; i<=1; i++) {
			boolean bFound = false;
			if(oDiscounts != null && oDiscounts.length() > 0) {				
				for(int j=0; j<1; j++) {
					if(Integer.valueOf(oDiscounts.optJSONObject(j).optString("index", "0")) == i) {
						String sValue = oDiscounts.optJSONObject(j).optString("total", "0");
						m_oPostingRequest.get("discountTotal").put("value", stringWithFormat(sValue, iPacketDecimalPoint));
						bFound = true;
						break;
					}
				}
			}
			
			if(!bFound)
				m_oPostingRequest.get("discountTotal").put("value", stringWithFormat("0", iPacketDecimalPoint));
		}
		m_oPostingRequest.get("tipsAmount").put("value", stringWithFormat(oPMSPostingInfo.optString("tips", "0"), iPacketDecimalPoint));
		m_oPostingRequest.get("scTotal").put("value", stringWithFormat(oPMSPostingInfo.optString("serviceCharge", "0"), iPacketDecimalPoint));
		JSONArray oTaxes = oPMSPostingInfo.optJSONArray("taxes");
		for(int i=1; i<=3; i++) {
			boolean bFound = false;
			if(oTaxes != null && oTaxes.length() > 0) {
				for(int j=0; j<3; j++) {
					if(Integer.valueOf(oTaxes.optJSONObject(j).optString("index", "0")) == i) {
						String sValue = oTaxes.optJSONObject(j).optString("total", "0");
						m_oPostingRequest.get("tax"+i+"Total").put("value", stringWithFormat(sValue, iPacketDecimalPoint));
						bFound = true;
						break;
					}
				}
			}
			
			if(!bFound)
				m_oPostingRequest.get("tax"+i+"Total").put("value", stringWithFormat("0", iPacketDecimalPoint));
		}
		m_oPostingRequest.get("reserved").put("value", stringWithFormat("0", iPacketDecimalPoint));
		m_oPostingRequest.get("previousPaymentTotal").put("value", stringWithFormat(oPMSPostingInfo.optString("previousPaymentAmount", "0"), iPacketDecimalPoint));
		
		//build sending content
		sSendDataString = buildPacketDataString(false, iNumberOfItemizer);
		
		//open serial port
		if(!openSerialPort()) {
			writeLog(m_oPosInterfaceConfig.getInterfaceId(), "Fail to open port - "+m_sLastErrorString);
			return false;
		}
		
		//send to serial port
		if(!pmsSend(sSendDataString)) {
			pmsClose();
			writeLog(m_oPosInterfaceConfig.getInterfaceId(), "Fail to send data - "+m_sLastErrorString);
			return false;
		}
		
		//receive form serial port
		if(!pmsReceive()) {
			pmsClose();
			writeLog(m_oPosInterfaceConfig.getInterfaceId(), "Fail to receive data - "+m_sLastErrorString);
			return false;
		}
		
		//close serial port
		pmsClose();
		
		//break the received string
		breakPacketDataString(false);
		
		//construct the guest list
		if(!m_oPostingResponse.get("messageType").get("value").equals(FuncPMS4700SerialPort.MESG_TYPE_POST)) {
			m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
			m_iLastErrorCode = 20;
			m_sLastErrorString = "Incorrect posting response message type:"+FuncPMS4700SerialPort.MESG_TYPE_POST;
			writeLog(m_oPosInterfaceConfig.getInterfaceId(), m_sLastErrorString);
			return false;
		}
		
		if(m_oPostingResponse.get("acceptanceDenialMessage").get("value").isEmpty()) {
			m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
			m_iLastErrorCode = 9;
			m_sLastErrorString = "Empty posting response message";
			writeLog(m_oPosInterfaceConfig.getInterfaceId(), m_sLastErrorString);
			return false;
		}else {
			char cFirstChar = m_oPostingResponse.get("acceptanceDenialMessage").get("value").charAt(0);
			if(cFirstChar == '/') {
				m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
				m_iLastErrorCode = 21;
				m_sLastErrorString = "PMS posting is rejected";
				writeLog(m_oPosInterfaceConfig.getInterfaceId(), m_sLastErrorString);
				return false;
			}else if(cFirstChar == '?') {
				m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
				m_iLastErrorCode = 22;
				m_sLastErrorString = "Incorrect posting response packet: ?";
				writeLog(m_oPosInterfaceConfig.getInterfaceId(), m_sLastErrorString);
				return false;
			}
		}
		
		return true;
	}
	
	public String getLastErrorType() {
		return this.m_sLastErrorType;
	}
	
	public int getLastErrorCode() {
		return this.m_iLastErrorCode;
	}
	
	public String getLastErrorString() {
		return this.m_sLastErrorString;
	}
	
	public String getLastResult() {
		return this.m_sLastResult;
	}
	
	/***************************/
	/**** Internal Function ****/
	/***************************/
	private void initEnquiryStucture() {
		HashMap<String, String> oTempHashMap;
		
		//init enquiry request
		m_oEnquiryRequest = new HashMap<String, HashMap<String, String>>();
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "2");
		m_oEnquiryRequest.put("messageType", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "1");
		m_oEnquiryRequest.put("retransmitFlag", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "16");
		m_oEnquiryRequest.put("inquiryMessage", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "4");
		m_oEnquiryRequest.put("employeeNo", oTempHashMap);
		
		//init enquiry response
		m_oEnquiryResponse = new HashMap<String, HashMap<String, String>>();
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "2");
		m_oEnquiryResponse.put("messageType", oTempHashMap);
		
		for(int i=1; i<=8; i++) {
			oTempHashMap = new HashMap<String, String>();
			oTempHashMap.put("value", "");
			oTempHashMap.put("length", "16");
			m_oEnquiryResponse.put("message"+i, oTempHashMap);
		}
	}
	
	private void initPostingStucture() {
		HashMap<String, String> oTempHashMap;
		
		//init posting request
		m_oPostingRequest = new HashMap<String, HashMap<String, String>>();
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "2");
		m_oPostingRequest.put("messageType", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "1");
		m_oPostingRequest.put("retransmitFlag", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "16");
		m_oPostingRequest.put("accountID", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "4");
		m_oPostingRequest.put("expiryDate", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "16");
		m_oPostingRequest.put("fieldInformation", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "1");
		m_oPostingRequest.put("fieldNumber", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "4");
		m_oPostingRequest.put("postingEmployeeNum", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "4");
		m_oPostingRequest.put("servingEmployeeNum", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "3");
		m_oPostingRequest.put("outletCode", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "3");
		m_oPostingRequest.put("servingPeriodNum", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "4");
		m_oPostingRequest.put("guestCheckNumber", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "4");
		m_oPostingRequest.put("stationCheckGroup", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "0");
		oTempHashMap.put("length", "4");
		m_oPostingRequest.put("covers", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "3");
		m_oPostingRequest.put("paymentNumber", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "0");
		oTempHashMap.put("length", "10");
		m_oPostingRequest.put("paymentAmount", oTempHashMap);
		
		for(int i=1; i<=10; i++) {
			oTempHashMap = new HashMap<String, String>();
			oTempHashMap.put("value", "0");
			oTempHashMap.put("length", "10");
			m_oPostingRequest.put("itemizer"+i, oTempHashMap);
		}
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "0");
		oTempHashMap.put("length", "10");
		m_oPostingRequest.put("discountTotal", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "0");
		oTempHashMap.put("length", "10");
		m_oPostingRequest.put("tipsAmount", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "0");
		oTempHashMap.put("length", "10");
		m_oPostingRequest.put("scTotal", oTempHashMap);
		
		for(int i=1; i<=3; i++) {
			oTempHashMap = new HashMap<String, String>();
			oTempHashMap.put("value", "0");
			oTempHashMap.put("length", "10");
			m_oPostingRequest.put("tax"+i+"Total", oTempHashMap);
		}
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "0");
		oTempHashMap.put("length", "10");
		m_oPostingRequest.put("reserved", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "0");
		oTempHashMap.put("length", "10");
		m_oPostingRequest.put("previousPaymentTotal", oTempHashMap);
		
		//init posting response
		m_oPostingResponse = new HashMap<String, HashMap<String, String>>();
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "2");
		m_oPostingResponse.put("messageType", oTempHashMap);
		
		oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put("value", "");
		oTempHashMap.put("length", "16");
		m_oPostingResponse.put("acceptanceDenialMessage", oTempHashMap);
		
		for(int i=1; i<=8; i++) {
			oTempHashMap = new HashMap<String, String>();
			oTempHashMap.put("value", "");
			oTempHashMap.put("length", "16");
			m_oPostingResponse.put("message"+i, oTempHashMap);
		}
	}
	
	private boolean checkPmsLock() {
		HashMap<String, String> oPMSExistingLock = null;
		HashMap<String, String> oPMSCheckLock = null;
		HashMap<String, String> oPMSNewLock = new HashMap<String, String>();
		long iLockTime = System.currentTimeMillis();
		
		if(AppGlobal.g_bPMS4700SerialPortLocking.containsKey(m_oPosInterfaceConfig.getInterfaceId()) == false) {
			//PMS lock not found
			oPMSNewLock.put("station", String.valueOf(AppGlobal.g_oFuncStation.get().getStationId()));
			oPMSNewLock.put("time", String.valueOf(iLockTime));
			AppGlobal.g_bPMS4700SerialPortLocking.put(m_oPosInterfaceConfig.getInterfaceId(), oPMSNewLock);
		}else {
			//PMS lock found
			long iLockLifeTime = m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_channel_lock_lifetime").optLong("value", 0);
			if(iLockLifeTime > 0)
				iLockLifeTime = iLockLifeTime * 1000;
			
			oPMSExistingLock = AppGlobal.g_bPMS4700SerialPortLocking.get(m_oPosInterfaceConfig.getInterfaceId());
			int iLastStationId = Integer.valueOf(oPMSExistingLock.get("station")).intValue();
			long iLastLockTime = Long.valueOf(oPMSExistingLock.get("time")).longValue();					
			
			//validate the existing lock
			if(iLastStationId == AppGlobal.g_oFuncStation.get().getStationId() || (iLockTime - iLastLockTime) > iLockLifeTime || (iLockTime - iLastLockTime) < 0) {
				if(iLastStationId != AppGlobal.g_oFuncStation.get().getStationId())
					writeLog(m_oPosInterfaceConfig.getInterfaceId(), "Unlink lock file created by <"+iLastStationId+">");
				
				//delete the old lock
				oPMSNewLock.put("station", String.valueOf(AppGlobal.g_oFuncStation.get().getStationId()));
				oPMSNewLock.put("time", String.valueOf(iLockTime));
				AppGlobal.g_bPMS4700SerialPortLocking.put(m_oPosInterfaceConfig.getInterfaceId(), oPMSNewLock);
				
			}else {
				m_iLastErrorCode = 15;
				m_sLastErrorString = "Fail to lock PMS port and port is locked by <"+iLastStationId+">";
				return false;
			}
		}
		
		//sleep target interval before double checking the existing lock file
		int iLockRecheckInterval = 0;
		if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_channel_lock_recheck_interval"))
			iLockRecheckInterval = m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_channel_lock_recheck_interval").optInt("value", 0);
		if(iLockRecheckInterval > 0) {
			try {
				Thread.sleep((iLockRecheckInterval * 1000));
			}catch(InterruptedException ex) {
				m_iLastErrorCode = 16;
				m_sLastErrorString = "Fail to create lock file";
				return false;
			}
		}
		
		//re-check the lock
		if(AppGlobal.g_bPMS4700SerialPortLocking.containsKey(m_oPosInterfaceConfig.getInterfaceId()) == false) {
			m_iLastErrorCode = 16;
			m_sLastErrorString = "Fail to create lock file>";
			return false;
		}else {
			oPMSCheckLock = AppGlobal.g_bPMS4700SerialPortLocking.get(m_oPosInterfaceConfig.getInterfaceId());
			int iLastStationId = Integer.valueOf(oPMSCheckLock.get("station")).intValue();
			
			if(iLastStationId != AppGlobal.g_oFuncStation.get().getStationId()) {
				m_iLastErrorCode = 15;
				m_sLastErrorString = "Fail to lock PMS port and port is locked by <"+iLastStationId+">";
				return false;
			}
		}
		
		return true;
	}
	
	private boolean releasePmsLock() {
		HashMap<String, String> oPMSExistingLock = null;
		int iLastStationId = 0;
		
		if(AppGlobal.g_bPMS4700SerialPortLocking.containsKey(m_oPosInterfaceConfig.getInterfaceId()) == false)
			return false;
		
		oPMSExistingLock = AppGlobal.g_bPMS4700SerialPortLocking.get(m_oPosInterfaceConfig.getInterfaceId());
		iLastStationId = Integer.valueOf(oPMSExistingLock.get("station")).intValue();
		
		if(iLastStationId == AppGlobal.g_oFuncStation.get().getStationId()) {
			AppGlobal.g_bPMS4700SerialPortLocking.remove(m_oPosInterfaceConfig.getInterfaceId());
			return true;
		}
		
		return false;
	}
	
	private String buildPacketDataString(boolean bEnquiry, int iNumberOfItemizer) {
		String sDataString = "";
		ArrayList<String> oDataSeq = null;
		HashMap<String, HashMap<String, String>> oData = null;

		String sCharset = m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_locale").optString("value", "UTF-8");
		
		if(bEnquiry) {
			oDataSeq = m_oEnquiryRequestSeq;
			oData = m_oEnquiryRequest;
		}else {
			oDataSeq = m_oPostingRequestSeq;
			oData = m_oPostingRequest;
		}
		
		for(int i=0; i<oDataSeq.size(); i++) {
			String sKey = oDataSeq.get(i);
			String sValue = oData.get(sKey).get("value");
			int iLength = Integer.valueOf(oData.get(sKey).get("length")).intValue();
			
			if(!bEnquiry && sKey.contains("itemizer")) {
				int iIndex = Integer.valueOf(sKey.replace("itemizer", "")).intValue();
				if(iIndex > iNumberOfItemizer)
					continue;
			}
			
			if(sValue == null || sValue.isEmpty()) 
				sDataString = sDataString + StringLib.fillSpace("", iLength);
			else {
				try {
			        byte[] oTextByte = sValue.getBytes(sCharset);
					int iValueLen = sValue.getBytes(sCharset).length;
					if(iValueLen > iLength)
						sValue = sValue.substring((iValueLen-iLength), iValueLen);

			        oTextByte = sValue.getBytes(sCharset);
			        byte[] oResultByte = new byte[iLength];
			        // fill byte array with zero first, then copy value from sValue
			        Arrays.fill(oResultByte, 0, iLength, (byte) 32);
			        System.arraycopy(oTextByte, 0, oResultByte, 0, oTextByte.length);
					sDataString = sDataString + new String(oResultByte, sCharset);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		return sDataString;
	}
	
	private void breakPacketDataString(boolean bEnquiry) {
		int iIndex = 0;
		ArrayList<String> oDataSeq = null;
		HashMap<String, HashMap<String, String>> oData = null;

		String sCharset = m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_locale").optString("value", "UTF-8");
		
		if(bEnquiry) {
			oDataSeq = m_oEnquiryResponseSeq;
			oData = m_oEnquiryResponse;
		}else {
			oDataSeq = m_oPostingResponseSeq;
			oData = m_oPostingResponse;
		}
		
		for(int i=0; i<oDataSeq.size(); i++) {
			String sKey = oDataSeq.get(i);
			int iLength = Integer.valueOf(oData.get(sKey).get("length")).intValue();
			byte[] oValueByte = Arrays.copyOfRange(m_oResponseBytes, iIndex, iIndex+iLength);
			
			String sValue = "";
			try {
				sValue = new String(oValueByte, sCharset);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sValue = sValue.trim();
			
			oData.get(sKey).put("value", sValue);
			iIndex = iIndex + iLength;
		}
		
		return;
	}
	
	private String subStringWithLength(String sString, int iLength) {
		String sNewString = "";
		int iStrLength = sString.length();
		
		if(iStrLength > 0 && iStrLength > iLength)
			sNewString = sString.substring((iStrLength-iLength));
		else
			sNewString = sString;
		
		return sNewString;
	}
	
	private String stringWithFormat(String sString, int iDecimalPoint) {
		if(sString.contains(".")) {
			int iPointDecimal = sString.indexOf(".");
			String sDecimalString = sString.substring((iPointDecimal+1));
			if(sDecimalString.length() < iDecimalPoint) {
				for(int i=1; i<=(iDecimalPoint-sDecimalString.length()); i++)
					sString = sString+"0";
			}
		}else {
			sString = sString + ".";
			for(int i=1; i<=iDecimalPoint; i++)
				sString = sString + "0";
		}
		return sString;
	}
	
	private String createCheckSum(byte[] oDataByte) {
		String sCheckSum = "    ";
		int iCheckSum = 0;
		int iStrLength = oDataByte.length;

		// convert byte to unsigned integer
		for(int i=0; i<iStrLength; i++) {
			iCheckSum += oDataByte[i] & 0xFF;
		}
		
		sCheckSum = String.format("%04X", iCheckSum);
		sCheckSum = sCheckSum.toUpperCase();
		return sCheckSum;
	}
	
	private JSONObject preparePostingInfo(FuncCheck oFuncCheck, PosCheckPayment oCheckPayment, JSONObject oPostingInfo, boolean bPrepost, boolean bVoidPosting) {
		int iPacketDecimalPoint = 2;
		int iNumberOfItemizer = 4;
		int iIncludeRoundAmtToItemizer = 0;
				
		if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_packet_decimal_point") && m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_packet_decimal_point").optInt("value", 2) <= 4)
			iPacketDecimalPoint = m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_packet_decimal_point").optInt("value", 2);
		if(m_oInterfaceSetup.optJSONObject("itemizer_setting").optJSONObject("params").has("itemizer_type"))
			iNumberOfItemizer = m_oInterfaceSetup.optJSONObject("itemizer_setting").optJSONObject("params").optJSONObject("itemizer_type").optInt("value", 4);
		if(m_oInterfaceSetup.optJSONObject("itemizer_setting").optJSONObject("params").has("itemizer_include_round_amt"))
			iIncludeRoundAmtToItemizer = m_oInterfaceSetup.optJSONObject("itemizer_setting").optJSONObject("params").optJSONObject("itemizer_include_round_amt").optInt("value", 0);
		
		//get the itemizer setup
		String[] oItemizerGroupCode = new String[iNumberOfItemizer];
		HashMap<Integer, ArrayList<Integer>> oItemizerConfig = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<String> oDeptCodeList = new ArrayList<String>();
		String sItemizerSetupString = "";
		if(m_oInterfaceSetup.optJSONObject("itemizer_setting").optJSONObject("params").has("itemizer_config"))
			sItemizerSetupString = m_oInterfaceSetup.optJSONObject("itemizer_setting").optJSONObject("params").optJSONObject("itemizer_config").optString("value","");
		String[] sItemizerSetups = sItemizerSetupString.split("\r\n");
		
		for(int i=0; i<iNumberOfItemizer; i++)
			oItemizerGroupCode[i] = "";
		
		for(int i=0; i<iNumberOfItemizer; i++) {
			String sKey = "";
			String sValue = "";
			int iMarkIndex = -1;
			int iItemizerKey = 0;
			
			StringTokenizer oStrTok = new StringTokenizer(sItemizerSetups[i], "=");
			if(oStrTok.countTokens() == 2) {
				sKey = oStrTok.nextToken();
				sValue = oStrTok.nextToken();
				iMarkIndex = sKey.indexOf("_");
				iItemizerKey = Integer.valueOf(sKey.substring((iMarkIndex+1)));
				
				oItemizerGroupCode[(iItemizerKey-1)] = sValue;
				if(!oDeptCodeList.contains(sValue))
					oDeptCodeList.add(sValue);
			}
		}
		
		//get department group content
		MenuItemDeptGroupList oMenuItemDeptGroupList = new MenuItemDeptGroupList();
		oMenuItemDeptGroupList.readItemDeptGroupListByCode(oDeptCodeList);
		
		//construct the itemizer configure
		for(int i=0; i<iNumberOfItemizer; i++) {
			ArrayList<Integer> oDeptIdList = new ArrayList<Integer>();
			MenuItemDeptGroup oItemDeptGroup = oMenuItemDeptGroupList.getByCode(oItemizerGroupCode[i]);
			
			if(oItemDeptGroup != null) {
				ArrayList<MenuItemDeptGroupLookup> oDeptGroupLookups = oItemDeptGroup.getItemDeptGroupLookupList();
				if(oDeptGroupLookups != null) {
					for(int j=0; j<oDeptGroupLookups.size(); j++) {
						if(oDeptGroupLookups.get(j).getDeptId() != 0)
							oDeptIdList.add(oDeptGroupLookups.get(j).getDeptId());
					}
				}
			}
			
			oItemizerConfig.put((i+1), oDeptIdList);
		}
		
		//calculate the check total for posting
		BigDecimal dCheckItemTotal = BigDecimal.ZERO;
		BigDecimal dNonSaleTotal = BigDecimal.ZERO;
		BigDecimal[] dTaxes = new BigDecimal[3];
		BigDecimal dServiceCharge = BigDecimal.ZERO;
		BigDecimal dRoundAmount = BigDecimal.ZERO;
		BigDecimal dCheckDiscountTotal = BigDecimal.ZERO;
		BigDecimal dDump = BigDecimal.ZERO;
		BigDecimal dTips = new BigDecimal(oPostingInfo.optString("tips", ""));
		BigDecimal[] dItemizers = new BigDecimal[iNumberOfItemizer];
		BigDecimal dSalesTotal = BigDecimal.ZERO;
		for(int i=0; i<3; i++)
			dTaxes[i] = BigDecimal.ZERO;
		for(int i=0; i<iNumberOfItemizer; i++)
			dItemizers[i] = BigDecimal.ZERO;
		BigDecimal dPaymentTotal = new BigDecimal(oCheckPayment.getPayTotal().toPlainString());
		BigDecimal dPostAmount = new BigDecimal(oPostingInfo.optString("postAmount", ""));
		BigDecimal dPreviousPaymentAmount = new BigDecimal(oPostingInfo.optString("previousPaymentAmount", ""));
		
		if(bPrepost) {
			dTips = BigDecimal.ZERO;
			dPaymentTotal = BigDecimal.ZERO;
			dPostAmount = BigDecimal.ZERO;
			dPreviousPaymentAmount = BigDecimal.ZERO;
			
		}else {
			dCheckItemTotal = oFuncCheck.getItemTotal();
			dServiceCharge = oFuncCheck.getServiceChargeTotal();
			dRoundAmount = oFuncCheck.getRoundAmount();
			dTaxes[0] = oFuncCheck.getTaxTotal(1);
			dTaxes[1] = oFuncCheck.getTaxTotal(2);
			for(int i=3; i<=25; i++)
				dTaxes[2] = dTaxes[2].add(oFuncCheck.getTaxTotal(i));
			List<PosCheckDiscount> oCheckDiscounts = oFuncCheck.getCurrentPartyAppliedCheckDiscount();
			if(!oCheckDiscounts.isEmpty()) {
				for(PosCheckDiscount oPosCheckDiscount: oCheckDiscounts)
					dCheckDiscountTotal = dCheckDiscountTotal.add(oPosCheckDiscount.getRoundTotal());
			}
			
			dSalesTotal = (((((dSalesTotal.add(dCheckItemTotal)).add(dNonSaleTotal)).add(dServiceCharge)).add(dTaxes[0])).add(dTaxes[1])).add(dTaxes[2]);
			dSalesTotal = (dSalesTotal.add(dCheckDiscountTotal)).add(dRoundAmount);
			
			//calculate the itemizer
			dItemizers = oFuncCheck.getItemizerTotal(iNumberOfItemizer, oItemizerConfig);
		
			//assign round amount
			if(iIncludeRoundAmtToItemizer == 0)
				dServiceCharge = dServiceCharge.add(dRoundAmount);
			else 
				dItemizers[(iIncludeRoundAmtToItemizer-1)] = dItemizers[(iIncludeRoundAmtToItemizer-1)].add(dRoundAmount);
			
			//calculate the payment ratio compare with whole check total
			BigDecimal dRatio = BigDecimal.ONE;
			if(dSalesTotal.compareTo(oCheckPayment.getPayTotal()) > 0) {
				dRatio = (oCheckPayment.getPayTotal()).divide(dSalesTotal, 4, RoundingMode.HALF_UP);
				
				dServiceCharge = dServiceCharge.multiply(dRatio);
				for(int i=0; i<3; i++)
					dTaxes[i] = dTaxes[i].multiply(dRatio);
				dCheckDiscountTotal = dCheckDiscountTotal.multiply(dRatio);
				for(int i=0; i<iNumberOfItemizer; i++)
					dItemizers[i] = dItemizers[i].multiply(dRatio);
			}
			
			//do rounding
			dPostAmount = Util.RoundUp(dPostAmount, iPacketDecimalPoint);
			dPaymentTotal = Util.RoundUp(dPaymentTotal, iPacketDecimalPoint);
			dTips = Util.RoundUp(dTips, iPacketDecimalPoint);
			dDump = Util.RoundUp(dPaymentTotal, iPacketDecimalPoint);
			dServiceCharge = Util.RoundUp(dServiceCharge, iPacketDecimalPoint);
			for(int i=0; i<3; i++)
				dTaxes[i] = Util.RoundUp(dTaxes[i], iPacketDecimalPoint);
			dCheckDiscountTotal = Util.RoundUp(dCheckDiscountTotal, iPacketDecimalPoint);
			for(int i=0; i<iNumberOfItemizer; i++)
				dItemizers[i] = Util.RoundUp(dItemizers[i], iPacketDecimalPoint);
			
			//calculate the rounding amount
			dDump = ((((dDump.subtract(dServiceCharge)).subtract(dTaxes[0])).subtract(dTaxes[1])).subtract(dTaxes[2])).subtract(dCheckDiscountTotal);
			for(int i=0; i<iNumberOfItemizer; i++)
				dDump = dDump.subtract(dItemizers[i]);
			
			//add rounding amount to itemizer
			if(dDump.compareTo(BigDecimal.ZERO) != 0) {
				if(iIncludeRoundAmtToItemizer > 0)
					dItemizers[(iIncludeRoundAmtToItemizer-1)] = dItemizers[(iIncludeRoundAmtToItemizer-1)].add(dDump);
				else {
					for(int i=0; i<iNumberOfItemizer; i++) {
						if(dItemizers[i].compareTo(BigDecimal.ZERO) > 0) {
							dItemizers[i] = dItemizers[i].add(dDump);
							break;
						}
					}
				}
			}
			
		}
		
		//assign value to posting JSONObject
		JSONArray oTempJSONArray = null;
		JSONObject oTempJSONObject = null;
		try {
			oPostingInfo.put("checkGroupNumber", oFuncCheck.getCheckPrefix());
			if(oFuncCheck.getCheckPrefixNo().contains(oFuncCheck.getCheckPrefix())) {
				int iPrefixLength = oFuncCheck.getCheckPrefix().length();
				oPostingInfo.put("checkNumber", (oFuncCheck.getCheckPrefixNo()).substring(iPrefixLength) );
			}else
				oPostingInfo.put("checkNumber", oFuncCheck.getCheckPrefixNo());
			if(oPostingInfo.getString("expiryDate").isEmpty())
				oPostingInfo.put("expiryDate", 0);
			if(bVoidPosting)
				oPostingInfo.put("serviceCharge", dServiceCharge.multiply(new BigDecimal("-1")).toPlainString());
			else
				oPostingInfo.put("serviceCharge", dServiceCharge.toPlainString());
			oTempJSONArray = new JSONArray();
			for(int i=0; i<3; i++) {
				oTempJSONObject = new JSONObject();
				oTempJSONObject.put("index", (i+1));
				if(bVoidPosting)
					oTempJSONObject.put("total", dTaxes[i].multiply(new BigDecimal("-1")).toPlainString());
				else
					oTempJSONObject.put("total", dTaxes[i].toPlainString());
				oTempJSONArray.put(oTempJSONObject);
			}
			oPostingInfo.put("taxes", oTempJSONArray);
			oTempJSONArray = new JSONArray();
			for(int i=0; i<iNumberOfItemizer; i++) {
				oTempJSONObject = new JSONObject();
				oTempJSONObject.put("index", (i+1));
				if(bVoidPosting)
					oTempJSONObject.put("total", dItemizers[i].multiply(new BigDecimal("-1")).toPlainString());
				else
					oTempJSONObject.put("total", dItemizers[i].toPlainString());
				oTempJSONArray.put(oTempJSONObject);
			}
			oPostingInfo.put("itemizers", oTempJSONArray);
			oTempJSONArray = new JSONArray();
			oTempJSONObject = new JSONObject();
			oTempJSONObject.put("index", 1);
			if(bVoidPosting)
				oTempJSONObject.put("total", dCheckDiscountTotal.multiply(new BigDecimal("-1")).toPlainString());
			else
				oTempJSONObject.put("total", dCheckDiscountTotal.toPlainString());
			oTempJSONArray.put(oTempJSONObject);
			oPostingInfo.put("discounts", oTempJSONArray);
			if(bVoidPosting)
				oPostingInfo.put("postAmount", dPostAmount.multiply(new BigDecimal("-1")).toPlainString());
			else
				oPostingInfo.put("postAmount", dPostAmount.toPlainString());
			if(bVoidPosting)
				oPostingInfo.put("paymentAmount", dPaymentTotal.multiply(new BigDecimal("-1")).toPlainString());
			else
				oPostingInfo.put("paymentAmount", dPaymentTotal.toPlainString());
			if(bVoidPosting)
				oPostingInfo.put("previousPaymentAmount", dPreviousPaymentAmount.multiply(new BigDecimal("-1")).toPlainString());
			else
				oPostingInfo.put("previousPaymentAmount", dPreviousPaymentAmount.toPlainString());
			if(bVoidPosting)
				oPostingInfo.put("tips", dTips.multiply(new BigDecimal("-1")).toPlainString());
			else
				oPostingInfo.put("tips", dTips.toPlainString());
			
		}catch (JSONException e) {}
		
		return oPostingInfo;
	}
	
	/********************************/
	/**** Serial Port Connection ****/
	/********************************/
 	private boolean openSerialPort() {
		String sDevice = "";
		int iBuadRate = 9600;
		int iDataBits = 8;
		int iParity = SerialPort.PARITY_NONE;
		int iRetryLockCount = 0;
		boolean bSuccessToLock = false;
		
		//lock the port
		if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_channel_retry_lock"))
			iRetryLockCount = m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_channel_retry_lock").optInt("value", 0);
		for(int iRetryCount=0; iRetryCount<=iRetryLockCount; iRetryCount++)	{
			if(checkPmsLock() == true) {
				bSuccessToLock = true;
				break;
			}
			
			try {
				Thread.sleep(1000);
			}catch(InterruptedException ex) {				
				break;
			}
		}
		
		if(!bSuccessToLock)
			return false;
		
		//read setup
		if(m_oInterfaceSetup.optJSONObject("connection_setup").optJSONObject("params").has("pms_device"))
			sDevice = m_oInterfaceSetup.optJSONObject("connection_setup").optJSONObject("params").optJSONObject("pms_device").optString("value", "");
		if(m_oInterfaceSetup.optJSONObject("connection_setup").optJSONObject("params").has("pms_baud_rate"))
			iBuadRate = m_oInterfaceSetup.optJSONObject("connection_setup").optJSONObject("params").optJSONObject("pms_baud_rate").optInt("value", 9600);
		if(m_oInterfaceSetup.optJSONObject("connection_setup").optJSONObject("params").has("pms_data_bits"))
			iDataBits = m_oInterfaceSetup.optJSONObject("connection_setup").optJSONObject("params").optJSONObject("pms_data_bits").optInt("value", 8);
		if(m_oInterfaceSetup.optJSONObject("connection_setup").optJSONObject("params").has("pms_parity")) {
			String sParity = m_oInterfaceSetup.optJSONObject("connection_setup").optJSONObject("params").optJSONObject("pms_parity").optString("value", "none");
			if(sParity.equals("odd"))
				iParity = SerialPort.PARITY_ODD;
			else if(sParity.equals("even"))
				iParity = SerialPort.PARITY_EVEN;
			else
				iParity = SerialPort.PARITY_NONE;
		}
		
		if(sDevice.isEmpty()) {
			m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
			m_iLastErrorCode = 3;
			m_sLastErrorString = "Missing setup";
			releasePmsLock();
			return false;
		}
		
		if(m_oSerialPort != null && m_oSerialPort.isOpened()) {
			m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
			m_iLastErrorCode = 6;
			m_sLastErrorString = "Fail to build connection";
			releasePmsLock();
			return false;
		}
		
		m_oSerialPort = new SerialPort(sDevice);		
		try {
			if(!m_oSerialPort.openPort()) {
				m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
				m_iLastErrorCode = 6;
				m_sLastErrorString = "Fail to build connection";
				releasePmsLock();
				return false;
			}
			
			if(!m_oSerialPort.setParams(iBuadRate, iDataBits, SerialPort.STOPBITS_1, iParity)) {
				m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
				m_oSerialPort.closePort();
				m_iLastErrorCode = 6;
				m_sLastErrorString = "Fail to build connection";
				releasePmsLock();
				return false;
			}

		}catch(SerialPortException e) {
			m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
			m_iLastErrorCode = 6;
			m_sLastErrorString = "Fail to build connection";			
			releasePmsLock();
			return false;
		}
		
		return true;
	}
	
	private boolean pmsSend(String sSendData) {
		String sSendString = "";
		String sCheckSum = "";
		String sStationCode = AppGlobal.g_oFuncStation.get().getCode();
		int iPacketTimeOut = 30;
		int iRetransmit = 0;
		int iWaitACKSuccess = 0, iIncorrectAckCnt = 0, iRecevingCnt = 0;
		boolean bSuccess = true, bSkipAckNak = false;
		
		if(!m_oSerialPort.isOpened()) {
			m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
			m_iLastErrorCode = 6;
			m_sLastErrorString = "Fail to build connection";
			return false;
		}
		
		if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_packet_request_timeout"))
			iPacketTimeOut = m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_packet_request_timeout").optInt("value", 30);
		if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_server_retransmit"))
			iRetransmit = m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_server_retransmit").optInt("value", 0);
		if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_skip_ack_nak") && m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_skip_ack_nak").optInt("value", 0) == 1)
			bSkipAckNak = true;		
		if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_incorrect_ack_count"))
			iIncorrectAckCnt = m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_incorrect_ack_count").optInt("value", 0);

		String sCharset = m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_locale").optString("value", "UTF-8");
		
		//handle station code
		if(sStationCode.length() > 18) 
			sStationCode = sStationCode.substring((sStationCode.length()-18), sStationCode.length());
		sStationCode = StringLib.fillSpace(sStationCode, 18);
		
		for(int iSendTime=0; iSendTime<=iRetransmit; iSendTime++) {
			bSuccess = true;
			
			if(iSendTime > 0) {
				writeLog(m_oPosInterfaceConfig.getInterfaceId(), "Send packet fail:"+m_sLastErrorString);
				writeLog(m_oPosInterfaceConfig.getInterfaceId(), "Packet send retry:"+iSendTime);
				sSendData = sSendData.substring(0, 2)+"R"+sSendData.substring(3);
			}
			
			//create check sum
			sSendString = sStationCode+STX+sSendData+ETX;
			try {
				sCheckSum = createCheckSum(sSendString.getBytes(sCharset));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			sSendString = SOH+sSendString+sCheckSum+EOT;
			
			try {
				//send to serial port
				if(!m_oSerialPort.writeBytes(sSendString.getBytes(sCharset))) {
					continue;
				}
				
				writePacketLog(m_oPosInterfaceConfig.getInterfaceId(), true, sSendString);
				
				//wait for ACK/NAK
				if(!bSkipAckNak) {
					byte[] buffers = null;
					
					do {
						iRecevingCnt = 0;
						buffers = null;
						
						try {
							buffers = m_oSerialPort.readBytes(1, (iPacketTimeOut * 1000));
						}catch (SerialPortTimeoutException et) {
							break;
						}
						
						char value = (char)buffers[0];
						switch (value) {
							case ' ':
							case '\0':
							case '\r':
							case '\n':
								iWaitACKSuccess = -3;
								break;
							case ACK:
								iWaitACKSuccess = 1;	//ACK received
								break;
							case NAK:
								iWaitACKSuccess = -1; 	//NAK received
								break;
							default:
								iRecevingCnt++;
								if(iRecevingCnt > iIncorrectAckCnt)
									iWaitACKSuccess = -2;
								break;
						}
					}while(iWaitACKSuccess == 0);
				}else
					iWaitACKSuccess = 1;
				
				if(iWaitACKSuccess != 1) {
					bSuccess = false;
					m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
					m_iLastErrorCode = 17;
					if(iWaitACKSuccess == -1) {
						m_iLastErrorCode = 18;
						m_sLastErrorString = "Receive NAK for sending packet";
					}else if(iWaitACKSuccess == -2)
						m_sLastErrorString = "Excess ACK count";
					else if(iWaitACKSuccess == -3)
						m_sLastErrorString = "Error in receiving ACK";
					else
						m_sLastErrorString = "Timeout to receive ACK for sending packet";
					
				}
			}
			catch(SerialPortException e) {} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			if(bSuccess)
				break;
		}
		
		if(!bSuccess)
			return false;
		
		return true;
	}
	
	private boolean pmsReceive() {
		int iPacketTimeOut = 30;
		String sSendString = "";
		boolean bSuccess = true, bNoCheckSum = false, bSkipAckNak = false;
		int iSendTime = 0, iRetransmit = 0;
		int iSohPosition = -1, iStxPosition = -1, iEtxPosition = -1, iEotPosition = -1;
		byte[] buffer = null;
		
		if(!m_oSerialPort.isOpened()) {
			m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
			m_iLastErrorCode = 6;
			m_sLastErrorString = "Fail to build connection";
			return false;
		}
		
		if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_packet_request_timeout"))
			iPacketTimeOut = m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_packet_request_timeout").optInt("value", 30);
		if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_server_retransmit"))
			iRetransmit = m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_server_retransmit").optInt("value", 0);
		if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_no_checksum") && m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_no_checksum").optInt("value", 0) == 1)
			bNoCheckSum = true;
		if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_skip_ack_nak") && m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_skip_ack_nak").optInt("value", 0) == 1)
			bSkipAckNak = true;
		
		String sCharset = m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_locale").optString("value", "UTF-8");
		
		//receive message from port
		byte[] tmp_bytes = new byte[1024];
		for(iSendTime=0; iSendTime<=iRetransmit; iSendTime++) {
			if(iSendTime > 0) {
				writeLog(m_oPosInterfaceConfig.getInterfaceId(), "Receive packet fail:"+m_sLastErrorString);
				writeLog(m_oPosInterfaceConfig.getInterfaceId(), "Packet receive retry:"+iSendTime);
			}
			
			try {
				bSuccess = false;

				int i = 0;
				do {
					buffer = null;
					
					try {
						buffer = m_oSerialPort.readBytes(1, (iPacketTimeOut * 1000));
					}catch (SerialPortTimeoutException et) {
						break;
					}
					
					char c = (char)buffer[0];
					if(c == EOT)
						bSuccess = true;

					tmp_bytes[i] = buffer[0];
					i++;
				}while(bSuccess == false);
	            
			}catch (SerialPortException e){}
			try {
				writePacketLog(m_oPosInterfaceConfig.getInterfaceId(), false, new String(tmp_bytes, sCharset));
			} catch (UnsupportedEncodingException e2) {
				e2.printStackTrace();
			}

			//checking syntax if received data
			iSohPosition = indexOfByte(tmp_bytes, 0x01); //SOH
			iStxPosition = indexOfByte(tmp_bytes, 0x02); //STX
			iEtxPosition = indexOfByte(tmp_bytes, 0x03); //ETX
			iEotPosition = indexOfByte(tmp_bytes, 0x04); //EOT
			
			if(iSohPosition == -1 || iStxPosition == -1 || iEtxPosition == -1 || iEotPosition == -1) {
				if(iEotPosition > 0) {
					//send NAK
					sSendString = Character.toString(NAK);
					try {
						m_oSerialPort.writeString(sSendString);
					}catch(SerialPortException e) {}
					m_sLastErrorString = "Incorrect receiving packet structure";
				}else
					m_sLastErrorString = "Timeout to wait receiving packet";
				m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
				m_iLastErrorCode = 19;
			}
			
			if(!bSuccess)
				continue;
			
			//checking check sum
			if(!bNoCheckSum) {
				byte[] oReceivedCheckSum = Arrays.copyOfRange(tmp_bytes, iEtxPosition+1, iEotPosition);
				byte[] oReceivedContentByte = Arrays.copyOfRange(tmp_bytes, iSohPosition+1, iEtxPosition+1);
				
				String sCheckSum = createCheckSum(oReceivedContentByte);
				String sReceivedCheckSum = "";
				try {
					sReceivedCheckSum = new String(oReceivedCheckSum, sCharset);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}

				if(sReceivedCheckSum.equals(sCheckSum) == false) {
					//send NAK
					sSendString = Character.toString(NAK);
					try {
						m_oSerialPort.writeString(sSendString);
					}catch(SerialPortException e) {}
					m_sLastErrorString = "Wrong Checksum for receiving packet";
					m_sLastErrorType = FuncPMS.ERROR_TYPE_CONNECTION;
					m_iLastErrorCode = 19;
				}
			}
			
			//sending ACK
			if(!bSkipAckNak) {
				sSendString = Character.toString(ACK);
				try {
					m_oSerialPort.writeString(sSendString);
				}catch(SerialPortException e) {}
			}
			
			if(bSuccess)
				break;
		}
		
		if(!bSuccess)
			return false;

		boolean bFlag = false;
		int iIndex = 0;
		for(int i = 0; i < tmp_bytes.length; i++) {
			if(tmp_bytes[i] == 0x02) {
				bFlag = true;
				continue;
			}
			
			if(tmp_bytes[i] == 0x03)
				break;
			
			if(bFlag && tmp_bytes[i] != 0x03) {
				m_oResponseBytes[iIndex] = tmp_bytes[i];
				iIndex++;
			}
		}
		
		return true;
	}
	
	public int indexOfByte(byte[] outerArray, int cSearchChar) {
	    for(int i = 0; i < outerArray.length; i++) {
           if (outerArray[i] == cSearchChar)
               return i;
        }
	   return -1;  
	}  
	
	private boolean pmsClose() {
		if(!m_oSerialPort.isOpened())
			return false;
		
		try {
			m_oSerialPort.closePort();
			
		}catch (SerialPortException e){
			releasePmsLock();
			return false;
		}
		releasePmsLock();
		
		return true;
	}
	
	/******************/
	/**** Log File ****/
	/******************/
	private void writePacketLog(int iInterfaceId, boolean bToPMS, String sLog){
		
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);

		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/pms_packet_log." + sCurrentMonth;

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
			if(bToPMS)
				sContent.append(" ToPMS >>>>> ");
			else
				sContent.append(" FromPMS <<<<< ");
			sContent.append(sLog);
			sContent.append("\n");
			
			out.write(sContent.toString());
			
			//Close the output stream
			out.close();
			fstream.close();
		}catch (Exception e){}//Catch exception if any
	}
	
	private void writeLog(int iInterfaceId, String sLog){
		
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);

		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/pms_log." + sCurrentMonth;

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
}
