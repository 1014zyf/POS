package app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import om.InfInterface;
import om.InfVendor;
import om.PosInterfaceConfig;

public class FuncCreditCardOperation {
	static class CreditCardSpectraStructure {
		String key;
		int length;
		
		public CreditCardSpectraStructure(String sKey, int iLength) {
			key = sKey;
			length = iLength;
		}
	}
	
	static final CreditCardSpectraStructure[] m_oCCSpectraSalesReqStructures = {
		new CreditCardSpectraStructure("requestType", 1),
		new CreditCardSpectraStructure("ecrRefNum", 16),
		new CreditCardSpectraStructure("transactionAmount", 12),
		new CreditCardSpectraStructure("tipsAmount", 12)
	};

	static final CreditCardSpectraStructure[] m_oCCSpectraVoidReqStructures = {
		new CreditCardSpectraStructure("requestType", 1),
		new CreditCardSpectraStructure("traceNum", 6),
		new CreditCardSpectraStructure("password", 6),
		new CreditCardSpectraStructure("transactionType", 1)
	};

	static final CreditCardSpectraStructure[] m_oCCSpectraAdjustTipsReqStructures = {
		new CreditCardSpectraStructure("requestType", 1),
		new CreditCardSpectraStructure("traceNum", 6),
		new CreditCardSpectraStructure("password", 6),
		new CreditCardSpectraStructure("newTransactionAmount", 12),
		new CreditCardSpectraStructure("newTipsAmount", 12)
	};
	
	static final CreditCardSpectraStructure[] m_oCCSpectraDccOptOutReqStructures = {
		new CreditCardSpectraStructure("requestType", 1),
		new CreditCardSpectraStructure("ecrRefNum", 16),
		new CreditCardSpectraStructure("traceNum", 6),
		new CreditCardSpectraStructure("password", 6),
	};
	
	static final CreditCardSpectraStructure[] m_oCreditCardSpectraStuctures = {
		new CreditCardSpectraStructure("responseType", 1),
		new CreditCardSpectraStructure("ecrRefNum", 16),
		new CreditCardSpectraStructure("paymentAmount", 12),
		new CreditCardSpectraStructure("tipsAmount", 12),
		new CreditCardSpectraStructure("responseCode", 2),
		new CreditCardSpectraStructure("responseText", 20),
		new CreditCardSpectraStructure("transactionDateTime", 12),
		new CreditCardSpectraStructure("cardType", 10),
		new CreditCardSpectraStructure("cardNumber", 19),
		new CreditCardSpectraStructure("expiryDate", 4),
		new CreditCardSpectraStructure("holderName", 23),
		new CreditCardSpectraStructure("terminalNumber", 8),
		new CreditCardSpectraStructure("merchantNum", 15),
		new CreditCardSpectraStructure("traceNum", 6),
		new CreditCardSpectraStructure("batchNum", 6),
		new CreditCardSpectraStructure("approvalCode", 6),
		new CreditCardSpectraStructure("retrievalRefNum", 12),
		new CreditCardSpectraStructure("entryMode", 1),
		new CreditCardSpectraStructure("emvApplicationId", 32),
		new CreditCardSpectraStructure("emvTransactionCrypt", 16),
		new CreditCardSpectraStructure("emvApplicationName", 16),
		new CreditCardSpectraStructure("dccAmount", 12),
		new CreditCardSpectraStructure("dccTips", 12),
		new CreditCardSpectraStructure("exchangeRate", 8),
		new CreditCardSpectraStructure("localCurrencyName", 3),
		new CreditCardSpectraStructure("foreignCurrencyName", 3),
		new CreditCardSpectraStructure("dccPrintText", 1),
		new CreditCardSpectraStructure("exchangeRateFormat", 1),
		new CreditCardSpectraStructure("markUpRateText", 21)
	};
	
	static final CreditCardSpectraStructure[] m_oCreditCardSpectraPR608ModelStuctures = {
		new CreditCardSpectraStructure("responseType", 1),
		new CreditCardSpectraStructure("ecrRefNum", 16),
		new CreditCardSpectraStructure("paymentAmount", 12),
		new CreditCardSpectraStructure("tipsAmount", 12),
		new CreditCardSpectraStructure("responseCode", 2),
		new CreditCardSpectraStructure("responseText", 20),
		new CreditCardSpectraStructure("transactionDateTime", 12),
		new CreditCardSpectraStructure("cardType", 10),
		new CreditCardSpectraStructure("cardNumber", 19),
		new CreditCardSpectraStructure("expiryDate", 4),
		new CreditCardSpectraStructure("holderName", 23),
		new CreditCardSpectraStructure("terminalNumber", 8),
		new CreditCardSpectraStructure("merchantNum", 15),
		new CreditCardSpectraStructure("traceNum", 6),
		new CreditCardSpectraStructure("batchNum", 6),
		new CreditCardSpectraStructure("approvalCode", 6),
		new CreditCardSpectraStructure("retrievalRefNum", 12),
		new CreditCardSpectraStructure("entryMode", 1),
		new CreditCardSpectraStructure("emvApplicationId", 32),
		new CreditCardSpectraStructure("emvTransactionCrypt", 16),
		new CreditCardSpectraStructure("emvApplicationName", 16),
		new CreditCardSpectraStructure("emvNoSignatureRequiredIndicator", 1),
		new CreditCardSpectraStructure("dccAmount", 12),
		new CreditCardSpectraStructure("dccTips", 12),
		new CreditCardSpectraStructure("exchangeRate", 8),
		new CreditCardSpectraStructure("localCurrencyName", 3),
		new CreditCardSpectraStructure("foreignCurrencyName", 3),
		new CreditCardSpectraStructure("dccPrintText", 1),
		new CreditCardSpectraStructure("exchangeRateFormat", 1),
		new CreditCardSpectraStructure("markUpRateText", 21)
	};
	
	static final CreditCardSpectraStructure[] m_oCUPCreditCardSpectraStuctures = {
		new CreditCardSpectraStructure("responseType", 1),
		new CreditCardSpectraStructure("ecrRefNum", 16),
		new CreditCardSpectraStructure("paymentAmount", 12),
		new CreditCardSpectraStructure("tipsAmount", 12),
		new CreditCardSpectraStructure("responseCode", 2),
		new CreditCardSpectraStructure("responseText", 20),
		new CreditCardSpectraStructure("transactionDateTime", 12),
		new CreditCardSpectraStructure("cardType", 10),
		new CreditCardSpectraStructure("cardNumber", 19),
		new CreditCardSpectraStructure("expiryDate", 4),
		new CreditCardSpectraStructure("terminalNumber", 8),
		new CreditCardSpectraStructure("merchantNum", 15),
		new CreditCardSpectraStructure("traceNum", 6),
		new CreditCardSpectraStructure("batchNum", 6),
		new CreditCardSpectraStructure("approvalCode", 6),
		new CreditCardSpectraStructure("retrievalRefNum", 12),
		new CreditCardSpectraStructure("operatorID", 2),
		new CreditCardSpectraStructure("issuerRefNum", 11),
		new CreditCardSpectraStructure("acquirerRefNum", 11),
		new CreditCardSpectraStructure("posCenterRefNum", 8),
		new CreditCardSpectraStructure("hostResponseMsg", 60),
		new CreditCardSpectraStructure("originalRRN", 12),
		// Additional CUP-EMV data
		new CreditCardSpectraStructure("entryMode", 1),
		new CreditCardSpectraStructure("emvApplicationId", 32),
		new CreditCardSpectraStructure("emvTransactionCrypt", 16),
		new CreditCardSpectraStructure("emvApplicationName", 16),
		new CreditCardSpectraStructure("emvTerminalVerifyResult", 10),
		new CreditCardSpectraStructure("emvTransactionStatusInfo", 4),
		new CreditCardSpectraStructure("emvApplicationTransCounter", 4),
		// Additional CUP-Electronic Cash data from terminal for type "TYPE_CUP_SALES_REQ"
		new CreditCardSpectraStructure("ecMode", 1),
		new CreditCardSpectraStructure("transactionCurrency", 3),
		new CreditCardSpectraStructure("exchangeRate", 8),
		new CreditCardSpectraStructure("exchageAmount", 12),
		new CreditCardSpectraStructure("balance", 12),
		new CreditCardSpectraStructure("hotCardInformation", 14),
		new CreditCardSpectraStructure("emvAppicationInterchangeProfile", 4),
		new CreditCardSpectraStructure("emvCardVerificationResult", 8),
		new CreditCardSpectraStructure("emvUnpredictNum", 8),
		new CreditCardSpectraStructure("emvTransactionType", 2),
		new CreditCardSpectraStructure("cardPanSeqNum", 3)
	};
	
	// for model PR608
	static final CreditCardSpectraStructure[] m_oCUPCreditCardSpectraPR608ModelStuctures = {
		new CreditCardSpectraStructure("responseType", 1),
		new CreditCardSpectraStructure("ecrRefNum", 16),
		new CreditCardSpectraStructure("paymentAmount", 12),
		new CreditCardSpectraStructure("tipsAmount", 12),
		new CreditCardSpectraStructure("responseCode", 2),
		new CreditCardSpectraStructure("responseText", 20),
		new CreditCardSpectraStructure("transactionDateTime", 12),
		new CreditCardSpectraStructure("cardType", 10),
		new CreditCardSpectraStructure("cardNumber", 19),
		new CreditCardSpectraStructure("expiryDate", 4),
		new CreditCardSpectraStructure("terminalNumber", 8),
		new CreditCardSpectraStructure("merchantNum", 15),
		new CreditCardSpectraStructure("traceNum", 6),
		new CreditCardSpectraStructure("batchNum", 6),
		new CreditCardSpectraStructure("approvalCode", 6),
		new CreditCardSpectraStructure("retrievalRefNum", 12),
		new CreditCardSpectraStructure("operatorID", 2),
		new CreditCardSpectraStructure("issuerRefNum", 11),
		new CreditCardSpectraStructure("acquirerRefNum", 11),
		new CreditCardSpectraStructure("posCenterRefNum", 8),
		new CreditCardSpectraStructure("hostResponseMsg", 60),
		new CreditCardSpectraStructure("originalRRN", 12),
		// Additional CUP-EMV data
		new CreditCardSpectraStructure("entryMode", 1),
		new CreditCardSpectraStructure("emvApplicationId", 32),
		new CreditCardSpectraStructure("emvTransactionCrypt", 16),
		new CreditCardSpectraStructure("emvApplicationName", 16),
		new CreditCardSpectraStructure("emvTerminalVerifyResult", 10),
		new CreditCardSpectraStructure("emvTransactionStatusInfo", 4),
		new CreditCardSpectraStructure("emvApplicationTransCounter", 4)
	};
	
	static final CreditCardSpectraStructure[] m_oCreditCardSpectraAdjustmentStuctures = {
		new CreditCardSpectraStructure("responseType", 1),
		new CreditCardSpectraStructure("traceNum", 6),
		new CreditCardSpectraStructure("responseCode", 2),
		new CreditCardSpectraStructure("responseText", 20)
	};
	
	static final CreditCardSpectraStructure[] m_oCreditCardSpectraDccOptOutStuctures = {
		new CreditCardSpectraStructure("responseType", 1),
		new CreditCardSpectraStructure("ecrRefNum", 16),
		new CreditCardSpectraStructure("paymentAmount", 12),
		new CreditCardSpectraStructure("tipsAmount", 12),
		new CreditCardSpectraStructure("responseCode", 2),
		new CreditCardSpectraStructure("responseText", 20),
		new CreditCardSpectraStructure("transactionDateTime", 12),
		new CreditCardSpectraStructure("cardType", 10),
		new CreditCardSpectraStructure("cardNumber", 19),
		new CreditCardSpectraStructure("expiryDate", 4),
		new CreditCardSpectraStructure("holderName", 23),
		new CreditCardSpectraStructure("terminalNumber", 8),
		new CreditCardSpectraStructure("merchantNum", 15),
		new CreditCardSpectraStructure("traceNum", 6),
		new CreditCardSpectraStructure("batchNum", 6),
		new CreditCardSpectraStructure("approvalCode", 6),
		new CreditCardSpectraStructure("retrievalRefNum", 12),
	};
	
	static final CreditCardSpectraStructure[] m_oCreditCardCtbcModelStuctures = {
		new CreditCardSpectraStructure("transactionType", 2),
		new CreditCardSpectraStructure("hostId", 2),
		new CreditCardSpectraStructure("invoiceNumber", 6),
		new CreditCardSpectraStructure("cardNumber", 19),
		new CreditCardSpectraStructure("cardExpiryDate", 4),
		new CreditCardSpectraStructure("transactionAmount", 12),
		new CreditCardSpectraStructure("transactionDate", 6),
		new CreditCardSpectraStructure("transactionTime", 6),
		new CreditCardSpectraStructure("approvalCode", 9),
		new CreditCardSpectraStructure("amount1", 12),
		new CreditCardSpectraStructure("responseCode", 4),
		new CreditCardSpectraStructure("terminalId", 8),
		new CreditCardSpectraStructure("referenceNumber", 12),
		new CreditCardSpectraStructure("amount2", 12),
		new CreditCardSpectraStructure("storeId", 16),
		new CreditCardSpectraStructure("amount3", 12),
		new CreditCardSpectraStructure("amount4", 12),
		new CreditCardSpectraStructure("inquiryType", 2),
		new CreditCardSpectraStructure("productCode", 2),
		new CreditCardSpectraStructure("RIFlag", 1),
		new CreditCardSpectraStructure("chargeFeeFlag", 1),
		new CreditCardSpectraStructure("delayPaymentFlag", 1),
		new CreditCardSpectraStructure("reserved", 39),
	};
	
	static class CreditCardSpectraResponse {
		String responseType;
		String ecrRefNum;
		String paymentAmount;
		String tipsAmount;
		String responseCode;
		String responseText;
		String transactionDateTime;
		String cardType;
		String cardNumber;
		String expiryDate;
		String holderName;
		String terminalNumber;
		String merchantNum;
		String traceNum;
		String batchNum;
		String approvalCode;
		String retrievalRefNum;
		String entryMode;
		String emvApplicationId;
		String emvTransactionCrypt;
		String emvApplicationName;
		String emvNoSignatureRequiredIndicator;
		String dccAmount;
		String dccTips;
		String exchangeRate;
		String localCurrencyName;
		String foreignCurrencyName;
		String dccPrintText;
		String exchangeRateFormat;
		String operatorID;
		String issuerRefNum;
		String acquirerRefNum;
		String posCenterRefNum;
		String hostResponseMsg;
		String originalRRN;
		String emvTerminalVerifyResult;
		String emvTransactionStatusInfo;
		String emvApplicationTransCounter;
		String ecMode;
		String transactionCurrency;
		String exchageAmount;
		String balance;
		String hotCardInformation;
		String emvAppicationInterchangeProfile;
		String emvCardVerificationResult;
		String emvUnpredictNum;
		String emvTransactionType;
		String cardPanSeqNum;
		String markUpRateText;
		
		public CreditCardSpectraResponse(String responseString, CreditCardSpectraStructure[] oCreditCardSpectraStructure) {
			int iIndex = 0;
			for (CreditCardSpectraStructure oCreditCardSpectraInfo : oCreditCardSpectraStructure) {
				Field field;
				try {
					field = getClass().getDeclaredField(oCreditCardSpectraInfo.key);
					if (iIndex+oCreditCardSpectraInfo.length >= responseString.length())
						break;
					
					field.set(this, responseString.substring(iIndex, iIndex + oCreditCardSpectraInfo.length).trim());
					iIndex += oCreditCardSpectraInfo.length;
				} 
				catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		public String getResponseType() {
			return responseType;
		}
		
		public String getTraceNumber() {
			return traceNum;
		}
		
		public String getHolderName() {
			return holderName;
		}
		
		public String getCardNumber() {
			return cardNumber;
		}
		
		public String getExpiryDate() {
			return expiryDate;
		}
	}
	
	static class CreditCardCtbcResponse {
		String transactionType;
		String hostId;
		String invoiceNumber;
		String cardNumber;
		String cardExpiryDate;
		String transactionAmount;
		String transactionDate;
		String transactionTime;
		String approvalCode;
		String amount1;
		String responseCode;
		String terminalId;
		String referenceNumber;
		String amount2;
		String storeId;
		String amount3;
		String amount4;
		String inquiryType;
		String productCode;
		String RIFlag;
		String chargeFeeFlag;
		String delayPaymentFlag;
		String reserved;
		
		public CreditCardCtbcResponse(String responseString, CreditCardSpectraStructure[] oCreditCardSpectraStructure) {
			int iIndex = 0;
			for (CreditCardSpectraStructure oCreditCardSpectraInfo : oCreditCardSpectraStructure) {
				Field field;
				try {
					field = getClass().getDeclaredField(oCreditCardSpectraInfo.key);
					if (iIndex+oCreditCardSpectraInfo.length >= responseString.length())
						break;
					
					field.set(this, responseString.substring(iIndex, iIndex + oCreditCardSpectraInfo.length).trim());
					iIndex += oCreditCardSpectraInfo.length;
				} 
				catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		public String getTransactionType() {
			return transactionType;
		}
		
		public String getHostId() {
			return hostId;
		}
		
		public String getInvoiceNumber() {
			return invoiceNumber;
		}
		
		public String getCardNumber() {
			return cardNumber;
		}
		
		public String getCardExpiryDate() {
			return cardExpiryDate;
		}
		
		public String getTransactionDate() {
			return transactionDate;
		}
		
		public String getTransactionTime() {
			return transactionTime;
		}
		
		public String getApprovalCode() {
			return approvalCode;
		}

		public String getResponseCode() {
			return responseCode;
		}
		
		public String getTerminalId() {
			return terminalId;
		}
		
		public String getReferenceNumber() {
			return referenceNumber;
		}
	}
	
	static class CreditCardSpectraRequest {
		private String requestType;
		private String ecrRefNum;
		private String transactionAmount;
		private String tipsAmount;
		private String traceNum;
		private String password;
		private String newTransactionAmount;
		private String newTipsAmount;
		private String transactionType;
		
		CreditCardSpectraRequest() {
			requestType = null;
			ecrRefNum = null;
			transactionAmount = null;
			tipsAmount = null;
			traceNum = null;
			password = null;
			newTransactionAmount = null;
			newTipsAmount = null;
			transactionType = null;
		}
		
		public void setSalesRequest(String sEcrRefNum, String sTransactionAmount, String sTipsAmount, String sCreditCardMethodType) {
			if (sCreditCardMethodType.equals(METHOD_TYPE_NORMAL_CREDIT_CARD))
				requestType = FuncCreditCardOperation.TYPE_SALES_REQ;
			else if (sCreditCardMethodType.equals(METHOD_TYPE_CUP_CREDIT_CARD))
				requestType = FuncCreditCardOperation.TYPE_CUP_SALES_REQ;
			ecrRefNum = sEcrRefNum;
			transactionAmount = sTransactionAmount;
			tipsAmount = sTipsAmount;
		}
		
		public void setVoidRequest(String sTraceNum, String sPassword, String sTransactionType, String sCreditCardMethodType) {
			if (sCreditCardMethodType.equals(METHOD_TYPE_NORMAL_CREDIT_CARD))
				requestType = FuncCreditCardOperation.TYPE_VOID_REQ;
			else if (sCreditCardMethodType.equals(METHOD_TYPE_CUP_CREDIT_CARD))
				requestType = FuncCreditCardOperation.TYPE_CUP_VOID_REQ;
			traceNum = sTraceNum;
			password = sPassword;
			if (sCreditCardMethodType.equals(METHOD_TYPE_CUP_CREDIT_CARD))
				sTransactionType = TYPE_CUP_SALES_REQ;
			transactionType = sTransactionType;
		}
		
		public void setAdjustTipsRequest(String sTraceNum, String sPassword, String sNewTransactionAmount, String sNewTipsAmount) {
			requestType = FuncCreditCardOperation.TYPE_ADJUST_TIPS_REQ;
			traceNum = sTraceNum;
			password = sPassword;
			newTransactionAmount = sNewTransactionAmount;
			newTipsAmount = sNewTipsAmount;
		}
		
		public void setDccOptOutRequest(String sChksCheckPrefixNum, String sTraceNum, String sPassword) {
			requestType = FuncCreditCardOperation.TYPE_DCC_OPT_OUT_REQ;
			ecrRefNum = sChksCheckPrefixNum;
			traceNum = sTraceNum;
			password = sPassword;
		}
		
		public String buildCreditCardSpectraRequestPacket(CreditCardSpectraStructure[] oCreditCardSpectraStructure) {
			String sResultString = "";
			for (CreditCardSpectraStructure oCreditCardSpectraInfo: oCreditCardSpectraStructure) {
				Field field;
				try {
					field = getClass().getDeclaredField(oCreditCardSpectraInfo.key);
					if (field.get(this) == null)
						continue;
					String sValue = (String)field.get(this);
					if (oCreditCardSpectraInfo.key.equals("transactionType") && sValue.isEmpty()) 
						continue;
					if (oCreditCardSpectraInfo.key.equals("requestType") || oCreditCardSpectraInfo.key.equals("transactionType"))
						sResultString += sValue;
					else if (oCreditCardSpectraInfo.key.equals("ecrRefNum") || oCreditCardSpectraInfo.key.equals("traceNum") || oCreditCardSpectraInfo.key.equals("password"))
						sResultString += String.format("%"+oCreditCardSpectraInfo.length+"s", sValue);
					else
						sResultString += String.format("%0"+oCreditCardSpectraInfo.length+"d", Integer.parseInt(sValue));
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
			return sResultString;
		}
	}
	
	static class CreditCardCtbcRequest {
		private String transactionType;
		private String hostId;
		private String invoiceNumber;
		private String transactionAmount;
		private String approvalCode;

		private String cardNumber;
		private String cardExpiryDate;
		private String transactionDate;
		private String transactionTime;
		private String amount1;
		private String responseCode;
		private String terminalId;
		private String referenceNumber;
		private String amount2;
		private String storeId;
		private String amount3;
		private String amount4;
		private String inquiryType;
		private String productCode;
		private String RIFlag;
		private String chargeFeeFlag;
		private String delayPaymentFlag;
		private String reserved;
		
		CreditCardCtbcRequest() {
			transactionType = null;
			hostId = null;
			invoiceNumber = null;
			transactionAmount = null;
			approvalCode = null;

			cardNumber = null;
			cardExpiryDate = null;
			transactionDate = null;
			transactionTime = null;
			amount1 = null;
			responseCode = null;
			terminalId = null;
			referenceNumber = null;
			amount2 = null;
			storeId = null;
			amount3 = null;
			amount4 = null;
			inquiryType = null;
			productCode = null;
			RIFlag = null;
			chargeFeeFlag = null;
			delayPaymentFlag = null;
			reserved = null;
		}
		
		public void setSalesRequest(String sEcrRefNum, String sTransactionAmount, String sTipsAmount, String sCreditCardMethodType) {
			transactionType = "01";
			if (sCreditCardMethodType.equals(METHOD_TYPE_NORMAL_CREDIT_CARD))
				hostId = "01";
			else if (sCreditCardMethodType.equals(METHOD_TYPE_CUP_CREDIT_CARD))
				hostId = "04";
			transactionAmount = sTransactionAmount;
		}
		
		public String buildCreditCardSpectraRequestPacket(CreditCardSpectraStructure[] oCreditCardSpectraStructure) {
			String sResultString = "";
			
			for (CreditCardSpectraStructure oCreditCardSpectraInfo: oCreditCardSpectraStructure) {
				Field field;
				try {
					field = getClass().getDeclaredField(oCreditCardSpectraInfo.key);
					String sValue = (String)field.get(this);
					
					if (sValue == null)
						 sValue = " ";
					if (oCreditCardSpectraInfo.key.equals("transactionType") && sValue.isEmpty()) 
						continue;
					if (oCreditCardSpectraInfo.key.equals("hostId") || oCreditCardSpectraInfo.key.equals("transactionType"))
						sResultString += sValue;
					else
						if(sValue.equals(" "))
							for(int i = 0 ; i < oCreditCardSpectraInfo.length ; i++)
								sResultString += " ";
						else
							sResultString += String.format("%0"+oCreditCardSpectraInfo.length+"d", Integer.parseInt(sValue));
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
			return sResultString;
		}
	}
	
	private CreditCardSpectraRequest m_oCreditCardSpectraRequest;
	private CreditCardSpectraResponse m_oCreditCardSpectraResponse;
	
	private CreditCardCtbcRequest m_oCreditCardCtbcRequest;
	private CreditCardCtbcResponse m_oCreditCardCtbcResponse;
	
	private int m_iInterfaceId;
	// Support flag
	private boolean m_bSupportSpectraCreditCard;

	private String m_sModelType;
	
	// Smart Card Device
	private String m_sDevice;
	
	private int m_iBuadRate;
	
	// Timeout setting
	private int m_iTimeout;
	
	// Retransmit limit
	private int m_iRetransmitLimit;
	
	// Flow Control
	private int m_iFlowControl;
	
	private String m_sVoidCreditCardPassword;
	private String m_sAdjustTipsPassword;
	private String m_sVoidCupPassword;
	
	// For DCC adjust tips handling
	private String m_sAdjustRequestPassword;
	private boolean m_bCreditCardDccEnable;
	private String m_sAdjustTipsInputTotal;
	
	// Delay in ms for socket to device manager
	private int m_iSocketDelay;
	
	private String m_sLastErrorCode;
	private String m_sLastErrorString;
	private boolean m_bReceivePacket;
	
	public static final String TYPE_MODEL_CREON4 = "creon4_model";
	public static final String TYPE_MODEL_PR608 = "pr608_model";
	public static final String TYPE_MODEL_CTBC = "ctbc_model";
	
	public static final String TYPE_SALES_REQ = "0";
	public static final String TYPE_VOID_REQ = "3";
	public static final String TYPE_CUP_SALES_REQ = "a";
	public static final String TYPE_CUP_VOID_REQ = "d";
	public static final String TYPE_ADJUST_TIPS_REQ = ";";
	public static final String TYPE_TRANSACTION_RETRIEVAL = "4";
	public static final String TYPE_DCC_OPT_OUT_REQ = "Z";
	
	public static final String TYPE_CUP_PRE_AUTH = "c";
	public static final String TYPE_CUP_PRE_AUTH_COMPLETE = "m";
	
	public static final String METHOD_TYPE_NORMAL_CREDIT_CARD = "normal_card";
	public static final String METHOD_TYPE_CUP_CREDIT_CARD = "cup_card";
	
	private static final String RESPONSE_TYPE_TERMINAL_BUSY = "X";
	private static final String RESPONSE_CODE_ACCEPT = "00";
	private static final String RESPONSE_CODE_VOIDED = "VD";
	
	public static final String TYPE_INPUT_TIPS_AMOUNT = "input_tips_amount";
	public static final String TYPE_INPUT_TIPS_AND_CALC_TIPS_AMOUNT = "input_total_amount_and_calc_tips_amount";
	
	public FuncCreditCardOperation() {
		m_iInterfaceId = 0;
		m_bSupportSpectraCreditCard = false;
		m_iTimeout = 2000;
		m_sDevice = "";
		m_iBuadRate = 9600;
		m_iRetransmitLimit = 2;
		m_iSocketDelay = 0;
		m_iFlowControl = 0;
		m_sVoidCreditCardPassword = "";
		m_sAdjustTipsPassword = "";
		m_sVoidCupPassword = "";
		m_sAdjustRequestPassword = "";
		m_bCreditCardDccEnable = false;
		m_sAdjustTipsInputTotal = TYPE_INPUT_TIPS_AMOUNT;
		
		m_oCreditCardSpectraResponse = null;
		m_oCreditCardSpectraRequest = new CreditCardSpectraRequest();
		
		m_oCreditCardCtbcResponse = null;
		m_oCreditCardCtbcRequest = new CreditCardCtbcRequest();
		
		m_sLastErrorCode = "";
		m_sLastErrorString = "";
		m_bReceivePacket = false;
	}
	
	public void readSetup() {
		List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		
		// Get the configure from interface module
		oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PERIPHERAL_DEVICE);
		for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList) {
			if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_DEVICE_MANAGER)) {
				try {
					JSONObject oInterfaceSetup = oPosInterfaceConfig.getInterfaceConfig();
					
					m_iInterfaceId = oPosInterfaceConfig.getInterfaceId();
					
					// Support flag
					if (oInterfaceSetup.has("credit_card_setup") && oInterfaceSetup.getJSONObject("credit_card_setup").getJSONObject("params").getJSONObject("support").getInt("value") == 1)
						m_bSupportSpectraCreditCard = true;
					else
						break;
					
					// Model Type
					m_sModelType = FuncCreditCardOperation.TYPE_MODEL_CREON4;
					if (oInterfaceSetup.has("credit_card_setup") && (oInterfaceSetup.optJSONObject("credit_card_setup").optJSONObject("params").has("model"))) {
						switch (oInterfaceSetup.getJSONObject("credit_card_setup").getJSONObject("params").getJSONObject("model").getInt("value")){
						case 1:
							m_sModelType = FuncCreditCardOperation.TYPE_MODEL_PR608;
							break;
						case 2:
							m_sModelType = FuncCreditCardOperation.TYPE_MODEL_CTBC;
							break;
						}
					}
					
					// Wait timeout
					m_iTimeout = Integer.parseInt(oInterfaceSetup.getJSONObject("credit_card_setup").getJSONObject("params").getJSONObject("process_timeout").getString("value"));

					//read setup
					if (oInterfaceSetup.optJSONObject("credit_card_setup").optJSONObject("params").has("device_name"))
						m_sDevice = oInterfaceSetup.optJSONObject("credit_card_setup").optJSONObject("params").optJSONObject("device_name").optString("value", "");
					if (oInterfaceSetup.optJSONObject("credit_card_setup").optJSONObject("params").has("baud_rate"))
						m_iBuadRate = oInterfaceSetup.optJSONObject("credit_card_setup").optJSONObject("params").optJSONObject("baud_rate").optInt("value", 9600);
					
					// Retransmit limit
					if (oInterfaceSetup.optJSONObject("credit_card_setup").optJSONObject("params").has("retransmit_limit"))
						m_iRetransmitLimit = oInterfaceSetup.optJSONObject("credit_card_setup").optJSONObject("params").optJSONObject("retransmit_limit").optInt("value", 0);
					
					// Delay for socket to device manager
					m_iSocketDelay = Integer.parseInt(oInterfaceSetup.getJSONObject("credit_card_setup").getJSONObject("params").getJSONObject("device_communication_interval").getString("value"));
					
					m_iFlowControl = Integer.parseInt(oInterfaceSetup.getJSONObject("credit_card_setup").getJSONObject("params").getJSONObject("flow_control").getString("value"));
					
					if (oInterfaceSetup.optJSONObject("credit_card_setup").optJSONObject("params").has("void_credit_card_password"))
						m_sVoidCreditCardPassword = oInterfaceSetup.getJSONObject("credit_card_setup").getJSONObject("params").getJSONObject("void_credit_card_password").getString("value");
					
					m_sAdjustTipsPassword = oInterfaceSetup.getJSONObject("credit_card_setup").getJSONObject("params").getJSONObject("adjust_tips_password").getString("value");
					
					m_sVoidCupPassword = oInterfaceSetup.getJSONObject("credit_card_setup").getJSONObject("params").getJSONObject("void_cup_password").getString("value");
					
					// For DCC adjust tips handling
					m_sAdjustRequestPassword = oInterfaceSetup.getJSONObject("credit_card_setup").getJSONObject("params").getJSONObject("adjust_request_password").getString("value");
					
					if(oInterfaceSetup.getJSONObject("credit_card_setup").getJSONObject("params").getJSONObject("credit_card_dcc_enable").getInt("value") == 1)
						m_bCreditCardDccEnable = true;
					
					if(oInterfaceSetup.getJSONObject("credit_card_setup").getJSONObject("params").has("adjust_tips_input_total"))
						m_sAdjustTipsInputTotal = oInterfaceSetup.getJSONObject("credit_card_setup").getJSONObject("params").getJSONObject("adjust_tips_input_total").getString("value");
				} catch (Exception e) {
					AppGlobal.stack2Log(e);
				}
			}
		}
	}
	
	public String spectraSalesRequest(String sInvoiceNum, BigDecimal oPaymentTotal, BigDecimal oTipsTotal, String sCreditCardMethodType) {
		BigDecimal oTmpPaymentTotal = new BigDecimal(oPaymentTotal.setScale(1, RoundingMode.CEILING).toString());
		String sPaymentTotal = Integer.toString(oTmpPaymentTotal.multiply(new BigDecimal("100")).intValue());
		BigDecimal oTmpTips = new BigDecimal(oTipsTotal.setScale(1, RoundingMode.CEILING).toString());
		String sTips = Integer.toString(oTmpTips.multiply(new BigDecimal("100")).intValue());

		m_oCreditCardSpectraRequest.setSalesRequest(sInvoiceNum, sPaymentTotal, sTips, sCreditCardMethodType);
		String sRequestString = m_oCreditCardSpectraRequest.buildCreditCardSpectraRequestPacket(m_oCCSpectraSalesReqStructures);
		return sRequestString;
	}
	
	public String ctbcSalesRequest(String sInvoiceNum, BigDecimal oPaymentTotal, BigDecimal oTipsTotal, String sCreditCardMethodType) {
		BigDecimal oTmpPaymentTotal = new BigDecimal(oPaymentTotal.setScale(1, RoundingMode.CEILING).toString());
		String sPaymentTotal = Integer.toString(oTmpPaymentTotal.multiply(new BigDecimal("100")).intValue());
		BigDecimal oTmpTips = new BigDecimal(oTipsTotal.setScale(1, RoundingMode.CEILING).toString());
		String sTips = Integer.toString(oTmpTips.multiply(new BigDecimal("100")).intValue());

		m_oCreditCardCtbcRequest.setSalesRequest(sInvoiceNum, sPaymentTotal, sTips, sCreditCardMethodType);
		String sRequestString = m_oCreditCardCtbcRequest.buildCreditCardSpectraRequestPacket(m_oCreditCardCtbcModelStuctures);
		return sRequestString;
	}
	
	public String spectraVoidRequest(String sTraceNum, String sPassword, String sTransactionType, String sCreditCardMethodType) {
		m_oCreditCardSpectraRequest.setVoidRequest(sTraceNum, sPassword, sTransactionType, sCreditCardMethodType);
		String sRequestString = m_oCreditCardSpectraRequest.buildCreditCardSpectraRequestPacket(m_oCCSpectraVoidReqStructures);
		return sRequestString;
	}
	
	public String spectraAdjustTipsRequest(String sTraceNum, String sPassword, BigDecimal oNewPaymentTotal, BigDecimal oNewTipsTotal) {
		BigDecimal oTmpPaymentTotal = new BigDecimal(oNewPaymentTotal.setScale(1, RoundingMode.CEILING).toString());
		String sPaymentTotal = Integer.toString(oTmpPaymentTotal.multiply(new BigDecimal("100")).intValue());
		BigDecimal oTmpTips = new BigDecimal(oNewTipsTotal.setScale(1, RoundingMode.CEILING).toString());
		String sTips = Integer.toString(oTmpTips.multiply(new BigDecimal("100")).intValue());
		m_oCreditCardSpectraRequest.setAdjustTipsRequest(sTraceNum, sPassword, sPaymentTotal, sTips);
		String sRequestString = m_oCreditCardSpectraRequest.buildCreditCardSpectraRequestPacket(m_oCCSpectraAdjustTipsReqStructures);
		return sRequestString;
	}
	
	public String spectraDccOptOutRequest(String sChksCheckPrefixNum, String sTraceNum, String sPassword) {
		m_oCreditCardSpectraRequest.setDccOptOutRequest(sChksCheckPrefixNum, sTraceNum, sPassword);
		String sRequestString = m_oCreditCardSpectraRequest.buildCreditCardSpectraRequestPacket(m_oCCSpectraDccOptOutReqStructures);
		return sRequestString;
	}
	
	public boolean breakPacketDataString(String sResponsePacket) {
		String sOperationType = "";
		switch(m_oCreditCardSpectraRequest.requestType) {
			case TYPE_SALES_REQ:
				sOperationType = "sales request";
				break;
			case TYPE_VOID_REQ:
				sOperationType = "void request";
				break;
			case TYPE_CUP_SALES_REQ:
				sOperationType = "CUP sales request";
				break;
			case TYPE_CUP_VOID_REQ:
				sOperationType = "void CUP request";
				break;
			case TYPE_ADJUST_TIPS_REQ:
				sOperationType = "adjust tips request";
				break;
			case TYPE_DCC_OPT_OUT_REQ:
				sOperationType = "dcc opt out request";
			default:
				break;
		}
		
		int iResult = Integer.parseInt(sResponsePacket.substring(0, 2));
		if (iResult <= 0) {
			writeErrorLog(sOperationType, sResponsePacket.substring(0, 2), getErrorMessage(iResult));
			return false;
		}
		
		String sResultResponsePacket = sResponsePacket.substring(2);
		this.writeInfoLog(sOperationType, sResultResponsePacket);
		
		CreditCardSpectraStructure[] oCreditCardSpectraStructure = null;
		
		if (m_oCreditCardSpectraRequest.requestType.equals(FuncCreditCardOperation.TYPE_ADJUST_TIPS_REQ))
			oCreditCardSpectraStructure = m_oCreditCardSpectraAdjustmentStuctures;
		else if (m_oCreditCardSpectraRequest.requestType.equals(FuncCreditCardOperation.TYPE_SALES_REQ) ||
				m_oCreditCardSpectraRequest.requestType.equals(FuncCreditCardOperation.TYPE_VOID_REQ)) {
			if (getModelType().equals(FuncCreditCardOperation.TYPE_MODEL_CREON4))
				oCreditCardSpectraStructure = m_oCreditCardSpectraStuctures;
			else if (getModelType().equals(FuncCreditCardOperation.TYPE_MODEL_PR608))
				oCreditCardSpectraStructure = m_oCreditCardSpectraPR608ModelStuctures;
		}
		else if (m_oCreditCardSpectraRequest.requestType.equals(FuncCreditCardOperation.TYPE_CUP_SALES_REQ) || 
				m_oCreditCardSpectraRequest.requestType.equals(FuncCreditCardOperation.TYPE_CUP_VOID_REQ)) {
			if (getModelType().equals(FuncCreditCardOperation.TYPE_MODEL_CREON4))
				oCreditCardSpectraStructure = m_oCUPCreditCardSpectraStuctures;
			else if (getModelType().equals(FuncCreditCardOperation.TYPE_MODEL_PR608))
				oCreditCardSpectraStructure = m_oCUPCreditCardSpectraPR608ModelStuctures;
		}
		else if (m_oCreditCardSpectraRequest.requestType.equals(FuncCreditCardOperation.TYPE_DCC_OPT_OUT_REQ))
			oCreditCardSpectraStructure = m_oCreditCardSpectraDccOptOutStuctures;
		
		
		m_oCreditCardSpectraResponse = new CreditCardSpectraResponse(sResultResponsePacket, oCreditCardSpectraStructure);
		m_bReceivePacket = true;

		if (m_oCreditCardSpectraRequest.requestType.equals(FuncCreditCardOperation.TYPE_SALES_REQ) 
				|| m_oCreditCardSpectraRequest.requestType.equals(FuncCreditCardOperation.TYPE_CUP_SALES_REQ)
				|| m_oCreditCardSpectraRequest.requestType.equals(FuncCreditCardOperation.TYPE_DCC_OPT_OUT_REQ))
			if (!m_oCreditCardSpectraResponse.ecrRefNum.equals(m_oCreditCardSpectraRequest.ecrRefNum)) {
				m_sLastErrorString = AppGlobal.g_oLang.get()._("ecr_reference_not_matched")+": "+m_oCreditCardSpectraRequest.ecrRefNum+", "+m_oCreditCardSpectraResponse.ecrRefNum;
				
				writeErrorLog(sOperationType, "", m_sLastErrorString);
				return false;
			}
		
		if (m_oCreditCardSpectraResponse.responseType.equals(FuncCreditCardOperation.RESPONSE_TYPE_TERMINAL_BUSY)) {
			m_sLastErrorString = AppGlobal.g_oLang.get()._("credit_card_terminal_is_busy");
			
			writeErrorLog(sOperationType, "", m_sLastErrorString);
			return false;
		}
		
		if (!m_oCreditCardSpectraResponse.responseType.equals(m_oCreditCardSpectraRequest.requestType)) {
			m_sLastErrorString = AppGlobal.g_oLang.get()._("incorrect_response_packet_type");
			
			writeErrorLog(sOperationType, "", m_sLastErrorString);
			return false;
		}
		
		if (m_oCreditCardSpectraRequest.requestType.equals(FuncCreditCardOperation.TYPE_VOID_REQ)
				&& m_oCreditCardSpectraResponse.responseCode.equals(FuncCreditCardOperation.RESPONSE_CODE_VOIDED))	// Check is successfully voided before (HERO timeout, but spectra machine successfully voided check)
			return true;
		else if (!m_oCreditCardSpectraResponse.responseCode.equals(FuncCreditCardOperation.RESPONSE_CODE_ACCEPT)) {
			m_sLastErrorCode = m_oCreditCardSpectraResponse.responseCode;
			m_sLastErrorString = m_oCreditCardSpectraResponse.responseText;
			
			writeErrorLog(sOperationType, m_sLastErrorCode, m_sLastErrorString);
			return false;
		}
		
		return true;
	}
	
	public boolean breakPacketDataStringForCtbc(String sResponsePacket) {
		String sOperationType = "sales request";
		
		// Valid packet = fix length 200 characters
		if(sResponsePacket.length() < 200) {
			m_sLastErrorString = AppGlobal.g_oLang.get()._("error");
			return false;
		}
		
		this.writeInfoLog(sOperationType, sResponsePacket);
		
		m_oCreditCardCtbcResponse = new CreditCardCtbcResponse(sResponsePacket, m_oCreditCardCtbcModelStuctures);
		m_bReceivePacket = true;
		
		if(!m_oCreditCardCtbcResponse.responseCode.equals("0000")) {
			if (m_oCreditCardCtbcResponse.responseCode.equals("0002"))
				m_sLastErrorString = AppGlobal.g_oLang.get()._("please_call_bank");
			else if (m_oCreditCardCtbcResponse.responseCode.equals("0003"))
				m_sLastErrorString = AppGlobal.g_oLang.get()._("timeout");
			else
				m_sLastErrorString = AppGlobal.g_oLang.get()._("error");
			writeErrorLog(sOperationType, "", m_sLastErrorString);
			return false;
		}
		
		return true;
	}
	
	private void writeInfoLog(String sFunction, String sMessage) {
		writeLog(m_iInterfaceId, "Online Credit Card (Spectra) - Success - Function: " + sFunction + ", Return message: " + sMessage);
	}

	private void writeErrorLog(String sFunction, String sErrorCode, String sErrorMessage) {
		writeLog(m_iInterfaceId, "Online Credit Card (Spectra) - Error - Function: " + sFunction + ", Error code: " + sErrorCode + ", Error message: " + sErrorMessage);
	}
	
	public void writeLog(int iInterfaceId, String sLog) {
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);

		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/credit_card_log." + sCurrentMonth;

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
	
	private String getErrorMessage(int iErrorCode) {
		String sErrorMessage = "";
		
		switch (iErrorCode) {
		case 0:
			sErrorMessage = AppGlobal.g_oLang.get()._("nothing_read_from_the_port");
			break;
		case -1:
			sErrorMessage = AppGlobal.g_oLang.get()._("time_out_at_waiting_for_return_message");
			break;
		case -2:
			sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_open_port");
			break;
		case -3:
			sErrorMessage = AppGlobal.g_oLang.get()._("received_unexpected_response_type");
			break;
		case -4:
			sErrorMessage = AppGlobal.g_oLang.get()._("cannot_sent_command_to_the_port");
			break;
		case -5:
			sErrorMessage = AppGlobal.g_oLang.get()._("cannot_open_dll_file");
			break;
		case -6:
			sErrorMessage = AppGlobal.g_oLang.get()._("terminal_busy");
			break;
		case -7:
			sErrorMessage = AppGlobal.g_oLang.get()._("wire_disconnected_in_10_seconds");
			break;
		default:
			sErrorMessage = null;
			break;
		}
		
		return sErrorMessage;
	}
	
	public void setDevice(String sDevice) {
		m_sDevice = sDevice;
	}
	
	public boolean isSupportSpectraCreditCard() {
		return m_bSupportSpectraCreditCard;
	}
	
	public String getModelType() {
		return m_sModelType;
	}
	
	public int getInterfaceId() {
		return m_iInterfaceId;
	}
	
	public int getTimeout() {
		return m_iTimeout;
	}
	
	public String getDevice() {
		return m_sDevice;
	}
	
	public int getBaudRate() {
		return m_iBuadRate;
	}
	
	public int getRetransmitLimit() {
		return m_iRetransmitLimit;
	}

	public int getSocketDelay() {
		return m_iSocketDelay;
	}
	
	public int getFlowControl() {
		return m_iFlowControl;
	}
	
	public String getVoidCreditCardPassword() {
		return m_sVoidCreditCardPassword;
	}
	
	public String getAdjustTipsPassword() {
		return m_sAdjustTipsPassword;
	}
	
	public String getVoidCupPassword() {
		return m_sVoidCupPassword;
	}
	
	// For DCC adjust tips handling
	public String getAdjustRequestPassword() {
		return m_sAdjustRequestPassword;
	}
	
	public boolean getCreditCardDccEnable() {
		return m_bCreditCardDccEnable;
	}
	
	public String getAdjustTipsInputTotal() {
		return m_sAdjustTipsInputTotal;
	}
	
	public String getLastErrorString() {
		return m_sLastErrorString;
	}
	
	public String getCardHolderName() {
		return m_oCreditCardSpectraResponse.holderName;
	}
	
	public String getCardNumber() {
		return m_oCreditCardSpectraResponse.cardNumber;
	}
	
	public String getCardType() {
		return m_oCreditCardSpectraResponse.cardType;
	}
	
	public String getCardExpiryDate() {
		return m_oCreditCardSpectraResponse.expiryDate;
	}
	
	public String getTraceNo() {
		return m_oCreditCardSpectraResponse.traceNum;
	}
	
	public String getTerminalNumber() {
		return m_oCreditCardSpectraResponse.terminalNumber;
	}
	
	public String getMerchantNumber() {
		return m_oCreditCardSpectraResponse.merchantNum;
	}
	
	public String getApprovalCode() {
		return m_oCreditCardSpectraResponse.approvalCode;
	}
	
	public String getBatchNumber() {
		return m_oCreditCardSpectraResponse.batchNum;
	}
	
	public String getReferenceNumber() {
		return m_oCreditCardSpectraResponse.retrievalRefNum;
	}
	
	public String getEntryMode() {
		return m_oCreditCardSpectraResponse.entryMode;
	}
	
	public String getEmvApplicationId() {
		return m_oCreditCardSpectraResponse.emvApplicationId;
	}
	
	public String getEmvTransactionCrypt() {
		return m_oCreditCardSpectraResponse.emvTransactionCrypt;
	}
	
	public String getEmvApplicationName() {
		return m_oCreditCardSpectraResponse.emvApplicationName;
	}
	
	public BigDecimal getDccAmount() {
		if (m_oCreditCardSpectraResponse.dccAmount != null) {
			BigDecimal dDccAmount = new BigDecimal(m_oCreditCardSpectraResponse.dccAmount);
			return dDccAmount.divide(new BigDecimal("100.00"));
		}else
			return null;
	}
	
	public BigDecimal getDccTips() {
		if (m_oCreditCardSpectraResponse.dccTips != null) {
			BigDecimal dDccTips = new BigDecimal(m_oCreditCardSpectraResponse.dccTips);
			return dDccTips.divide(new BigDecimal("100.00"));
		}else
			return null;
	}
	
	public BigDecimal getExchangeRate() {
		if (m_oCreditCardSpectraResponse.exchangeRate != null) {
			BigDecimal dExchangeRateFormat = new BigDecimal(getExchangeRateFormatText());
			Integer dDecimalIndicator = new Integer(m_oCreditCardSpectraResponse.exchangeRate.substring(0, 1));
			Integer dLen = new Integer(m_oCreditCardSpectraResponse.exchangeRate.length());
			BigDecimal dExchangeRate = new BigDecimal(m_oCreditCardSpectraResponse.exchangeRate.substring(1, dLen));

			dExchangeRate = dExchangeRate.movePointLeft(dDecimalIndicator);
			if (dExchangeRateFormat.compareTo(new BigDecimal("1")) == 0) 
				dExchangeRate = BigDecimal.ONE.divide(dExchangeRate, 4, RoundingMode.HALF_UP);
			return dExchangeRate;
		}else
			return null;
	}
	
	public String getLocalCurrencyName() {
		return m_oCreditCardSpectraResponse.localCurrencyName;
	}
	
	public String getForeignCurrencyName() {
		return m_oCreditCardSpectraResponse.foreignCurrencyName;
	}
	
	public String getPrintText() {
		return m_oCreditCardSpectraResponse.dccPrintText;
	}
	
	public String getExchangeRateFormatText() {
		return m_oCreditCardSpectraResponse.exchangeRateFormat;
	}
	
	public String getMarkUpRateText() {
		return m_oCreditCardSpectraResponse.markUpRateText;
	}
	
	public String getCtbcCardNumber() {
		return m_oCreditCardCtbcResponse.cardNumber;
	}
	
	public String getCtbcExpiryDate() {
		return m_oCreditCardCtbcResponse.cardExpiryDate;
	}
	
	public String getCtbcInvoiceNumber() {
		return m_oCreditCardCtbcResponse.invoiceNumber;
	}
	
	public String getCtbcApprovalCode() {
		return m_oCreditCardCtbcResponse.approvalCode;
	}
	
	public String getCtbcTerminalNumber() {
		return m_oCreditCardCtbcResponse.terminalId;
	}
	
	public String getCtbcReference() {
		return m_oCreditCardCtbcResponse.referenceNumber;
	}
	
	public boolean isTerminalBusy() {
		if (m_sModelType.equals(FuncCreditCardOperation.TYPE_MODEL_CTBC))
			return m_oCreditCardCtbcResponse.getResponseCode().equals(FuncCreditCardOperation.RESPONSE_TYPE_TERMINAL_BUSY);
		else
			return m_oCreditCardSpectraResponse.getResponseType().equals(FuncCreditCardOperation.RESPONSE_TYPE_TERMINAL_BUSY);
	}
	
	public boolean isReceivedPacket() {
		return m_bReceivePacket;
	}
}
