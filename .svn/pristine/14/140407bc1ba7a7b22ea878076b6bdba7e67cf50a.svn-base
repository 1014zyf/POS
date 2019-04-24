package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import om.InfInterface;
import om.InfVendor;
import om.PosCheckExtraInfo;
import om.PosCheckPayment;
import om.PosInterfaceConfig;
import om.PosPaymentGatewayTransactions;

public class FuncPaymentInterface {
	class PostingInfo {
		int iInterfaceId;
		int iOutletId;
		String sCheckNumber;
		String sAuthCode;
		String sAuthReferenceNumber;
		String sAuthorizeAmt;
		String sTotalAmt;
		String sSurchargeAmt;
		String sEmployeeNum;
		String sEmployeeName;
		String sTraceNumber;
		String sInvoiceNumber;
		String sIssuer;
		String sTransactionKey;
		String sVoidTransactionKey;
		String sPaymentType;
		String sEmployeeCode;
		String sStationCode;
		String sReference;
		BigDecimal dPostAmount;
		String sPostDateTime;
		String sEntryMode;
		String sGuestNo;
		String sToken;
	}
	
	class PostingResponse {
		boolean bSuccess;
		String sResultCode;
		String sErrorMessage;
		String sAccountNumber;
		String sCompanyName;
		String[] sRefernces;
		BigDecimal dPayAmount;
		BigDecimal dActualPayAmount;
		BigDecimal dInvoiceAmount;
		BigDecimal dDiscountAmount;
		BigDecimal dPointAmount;
		HashMap<String, String> oCardAuthorizationRepsonse;
		PosPaymentGatewayTransactions oPosPaymentGatewayTransactions;
		String sAuthorizationCode;
		String sIssuer;
		String sTraceNumber;
		String sInvoiceNumber;
		String sReferenceNumber;
		String sAcquirerMerchantNumber;
		String sAcquirerTerminal;
		String sAcquirerName;
		String sAcquierPrintData;
		String sEmv;
		String sEmvData;
		String sEntryMode;
		String sIcCardSeq;
		String sIntlCardTraceNumber;
		String sMaskedPan;
		BigDecimal dECashBalance;
		String sTerminalSeq;
		String sSignFree;
		String sEsignature;
		String sToken;
		String sCode;
	}
	
	static public int POSTING_RESPONSE_REF_NO = 3;
	
	static public String PAY_CHECK_SUCCESS = "s";
	static public String PAY_CHECK_FAIL = "f";
	
	static public String PRINT_SLIP_TYPE_SCAN_PAY_VOID = "scan_pay_void";
	static public String SCANPAY_QUERY_SALES_TYPE = "scanpay_sales_query";
	
	static public String ERROR_CODE_QUERY_TIMEOUT = "11";
	
	static public String TRANSACTION_ON_GOING_CODE = "X0";
	
	private PosInterfaceConfig m_oPaymentInterface;
	
	private JSONObject m_oPostingRequestJSON;
	
	public String m_sLastErrorMessage;
	private int m_iLastErrorCode;
	private PostingResponse m_oPostingResponse;
	
	public FuncPaymentInterface(PosInterfaceConfig oPaymentInterface) {
		m_oPaymentInterface = oPaymentInterface;
		m_sLastErrorMessage = "";
		m_iLastErrorCode = 0;
		m_oPostingResponse = new PostingResponse();
		m_oPostingResponse.bSuccess = false;
		m_oPostingResponse.sResultCode = "";
		m_oPostingResponse.sErrorMessage = "";
		m_oPostingResponse.sAccountNumber = "";
		m_oPostingResponse.sCompanyName = "";
		m_oPostingResponse.sRefernces = new String[FuncPaymentInterface.POSTING_RESPONSE_REF_NO];
		for(int i=0; i<FuncPaymentInterface.POSTING_RESPONSE_REF_NO; i++)
			m_oPostingResponse.sRefernces[i] = "";
		m_oPostingResponse.dPayAmount = BigDecimal.ZERO;
		m_oPostingResponse.dActualPayAmount = BigDecimal.ZERO;
		m_oPostingResponse.dActualPayAmount = BigDecimal.ZERO;
		m_oPostingResponse.dInvoiceAmount = BigDecimal.ZERO;
		m_oPostingResponse.dPointAmount = BigDecimal.ZERO;
		m_oPostingResponse.oCardAuthorizationRepsonse = new HashMap<String, String>();
		m_oPostingResponse.oPosPaymentGatewayTransactions = new PosPaymentGatewayTransactions();
		m_oPostingResponse.sCode = "";
		m_oPostingRequestJSON = null;
	}
	
	public List<JSONObject> checkPaymentResult() {
		// for OGS interface
		if(m_oPaymentInterface.getInterfaceVendorKey().equals(InfVendor.KEY_OGS)) {
			List<JSONObject> oPaidCheckList = checkOgsPaymentResult();
			return oPaidCheckList;
		}
		
		return null;
	}
	
	
	// check whether support payment interface
	static boolean isSupportPaymentInterface() {
		if(AppGlobal.isModuleSupport(AppGlobal.OPTIONAL_MODULE.pos_interface.name()) == false)
			return false;
		if(AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PAYMENT_INTERFACE) == null || AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PAYMENT_INTERFACE).isEmpty())
			return false;
		
		return true;
	}
	
	private void initPostingInfo(PostingInfo oPostingInfo) {
		oPostingInfo.iInterfaceId = 0;
		oPostingInfo.iOutletId = 0;
		oPostingInfo.sCheckNumber = "";
		oPostingInfo.sAuthReferenceNumber = "";
		oPostingInfo.sAuthorizeAmt = "";
		oPostingInfo.sTotalAmt = "";
		oPostingInfo.sSurchargeAmt = "";
		oPostingInfo.sEmployeeNum = "";
		oPostingInfo.sEmployeeName = "";
		oPostingInfo.sTraceNumber = "";
		oPostingInfo.sInvoiceNumber = "";
		oPostingInfo.sTransactionKey = "";
		oPostingInfo.sVoidTransactionKey = "";
		oPostingInfo.sPaymentType = "";
		oPostingInfo.sEmployeeCode = "";
		oPostingInfo.sStationCode = "";
		oPostingInfo.sReference = "";
		oPostingInfo.dPostAmount = BigDecimal.ZERO;
		oPostingInfo.sPostDateTime = "";
		oPostingInfo.sEntryMode = "";
		oPostingInfo.sGuestNo = "";
		oPostingInfo.sToken = "";
	}
	
	private JSONObject fromPaymentPostingJSONObject(PostingInfo oPostingInfo) {
		JSONObject oPostingJSONObject = new JSONObject();
		
		try {
			oPostingJSONObject.put("interfaceId", oPostingInfo.iInterfaceId);
			oPostingJSONObject.put("outletId", oPostingInfo.iOutletId);
			oPostingJSONObject.put("checkNumber", oPostingInfo.sCheckNumber);
			oPostingJSONObject.put("authReferenceNumber", oPostingInfo.sAuthReferenceNumber);
			oPostingJSONObject.put("employeeNum", oPostingInfo.sEmployeeNum);
			oPostingJSONObject.put("authorizeAmt", oPostingInfo.sAuthorizeAmt);
			oPostingJSONObject.put("authorizeTotalAmt", oPostingInfo.sTotalAmt);
			oPostingJSONObject.put("surchargeAmt", oPostingInfo.sSurchargeAmt);
			oPostingJSONObject.put("authTraceNumber", oPostingInfo.sTraceNumber);
			oPostingJSONObject.put("authInvoiceNumber", oPostingInfo.sInvoiceNumber);
			oPostingJSONObject.put("authorizationCode", oPostingInfo.sAuthCode);
			oPostingJSONObject.put("issuer", oPostingInfo.sIssuer);
			oPostingJSONObject.put("transactionKey", oPostingInfo.sTransactionKey);
			oPostingJSONObject.put("voidTransactionKey", oPostingInfo.sVoidTransactionKey);
			oPostingJSONObject.put("paymentType", oPostingInfo.sPaymentType);
			oPostingJSONObject.put("employeeCode", oPostingInfo.sEmployeeCode);
			oPostingJSONObject.put("stationCode", oPostingInfo.sStationCode);
			oPostingJSONObject.put("accountNumber", oPostingInfo.sReference);
			oPostingJSONObject.put("postAmount", oPostingInfo.dPostAmount.toPlainString());
			oPostingJSONObject.put("postDateTime", oPostingInfo.sPostDateTime);
			oPostingJSONObject.put("entryMode", oPostingInfo.sEntryMode);
			oPostingJSONObject.put("guestNo", oPostingInfo.sGuestNo);
			oPostingJSONObject.put("token", oPostingInfo.sToken);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		
		if(m_oPaymentInterface.getInterfaceVendorKey().equals(InfVendor.KEY_SCAN_PAY))
			m_oPostingRequestJSON = oPostingJSONObject;
		
		return oPostingJSONObject;
	}
	
	// check payment result with OGS interface
	private List<JSONObject> checkOgsPaymentResult() {
		List<JSONObject> oPaidCheckList = new ArrayList<JSONObject>();
		if(!AppGlobal.g_oCheckListForPaymentInterface.containsKey(AppGlobal.g_oFuncOutlet.get().getOutletId()))
			return oPaidCheckList;
		
		if(AppGlobal.g_oCheckListForPaymentInterface.get(AppGlobal.g_oFuncOutlet.get().getOutletId()).isEmpty())
			return oPaidCheckList;
		
		//Filter out the corresponding interface printed check with same interface ID and interface paytype
		List<HashMap<String,String>> oCheckToRemoveFromList = new ArrayList<HashMap<String,String>>();
		HashMap<Integer, List<HashMap<String, String>>> oPrintedCheckList = new HashMap<Integer, List<HashMap<String, String>>>();
		for(HashMap<String, String> oPrintedCheckInfo: AppGlobal.g_oCheckListForPaymentInterface.get(AppGlobal.g_oFuncOutlet.get().getOutletId())) {
			if(!oPrintedCheckInfo.containsKey("interfaceId") || (oPrintedCheckInfo.containsKey("interfaceId") && Integer.valueOf(oPrintedCheckInfo.get("interfaceId")) != m_oPaymentInterface.getInterfaceId()))
				continue;
			
			//check expired check
			if(oPrintedCheckInfo.containsKey("expireTime")) {
				DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMddHHmmss");
				DateTime dExpiryTime = formatter.parseDateTime(oPrintedCheckInfo.get("expireTime"));
				DateTime dCurrentTime = AppGlobal.getCurrentTime(false);
				if (dCurrentTime.compareTo(dExpiryTime) >= 0) {
					HashMap<String, String> oTempInfo = new HashMap<String, String>();
					oTempInfo.put("checkId", oPrintedCheckInfo.get("checkId"));
					oTempInfo.put("outTradeNumber",	oPrintedCheckInfo.get("outTradeNumber"));
					
					oCheckToRemoveFromList.add(oTempInfo);
					continue;
				}
			}
			
			Integer iPayType = Integer.valueOf(oPrintedCheckInfo.get("paytype"));
			if(!oPrintedCheckList.containsKey(iPayType))
				oPrintedCheckList.put(iPayType, new ArrayList<HashMap<String, String>>());
			oPrintedCheckList.get(iPayType).add(oPrintedCheckInfo);
		}
		
		//remove expired check
		if(!oCheckToRemoveFromList.isEmpty()) {
			for(HashMap<String, String> oCheckInfo: oCheckToRemoveFromList) {
				AppGlobal.removePrintedCheckToPaymentInterfaceCheckList(AppGlobal.g_oFuncOutlet.get().getOutletId(), oCheckInfo.get("checkId"), oCheckInfo.get("outTradeNumber"));
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Check expired, remove from list. CheckID:"+oCheckInfo.get("checkId")+", outTradeNumber:"+oCheckInfo.get("outTradeNumber"));
			}
		}
		
		if(oPrintedCheckList.isEmpty())
			return oPaidCheckList;
		
		JSONArray oPrintedCheckJSONArray = new JSONArray();
		for(Map.Entry<Integer, List<HashMap<String, String>>> entry : oPrintedCheckList.entrySet()) {
			try {
				JSONObject oPayTypeWithChecksJSONObject = new JSONObject();
				JSONArray oCheckInfos = new JSONArray();
				
				int iPayType = entry.getKey().intValue();
				List<HashMap<String, String>> oPrintedCheckListByType = entry.getValue();
				
				for(HashMap<String, String> oPrintedCheckInfo: oPrintedCheckListByType) {
					JSONObject oCheckInfoJSONObject = new JSONObject();
					oCheckInfoJSONObject.put("outTradeNumber", oPrintedCheckInfo.get("outTradeNumber"));
					oCheckInfos.put(oCheckInfoJSONObject);
				}
				
				oPayTypeWithChecksJSONObject.put("paytype", iPayType);
				oPayTypeWithChecksJSONObject.put("outTradeNumbers", oCheckInfos);
				oPrintedCheckJSONArray.put(oPayTypeWithChecksJSONObject);
			}catch(JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		
		//checking the payment result
		JSONArray oPaymentResults = null;
		oPaymentResults = m_oPaymentInterface.checkPaymentResults(AppGlobal.g_oFuncOutlet.get().getOutletCode(), oPrintedCheckJSONArray);
		
		//handle return
		if(oPaymentResults != null) {
			for(int i=0; i<oPaymentResults.length(); i++) {
				JSONObject oPaymentResult = oPaymentResults.optJSONObject(i);
				
				//check whether return is error
				 if (oPaymentResult != null && oPaymentResult.has("errorCode") && !oPaymentResult.optString("errorCode").isEmpty()) {
					String sOutTradeNumber = oPaymentResult.optString("outTradeNumber", "");
					
					if(oPaymentResult.has("errorMessage") && oPaymentResult.optString("errorMessage").equals("URL unavailable"))
						continue;
					
					for(HashMap<String, String> oPrintedCheckInfo: AppGlobal.g_oCheckListForPaymentInterface.get(AppGlobal.g_oFuncOutlet.get().getOutletId())) {
						if(oPrintedCheckInfo.containsKey("outTradeNumber") && oPrintedCheckInfo.get("outTradeNumber") != null && oPrintedCheckInfo.get("outTradeNumber").equals(sOutTradeNumber)) {
							JSONObject oTempInfo = new JSONObject();
							if(oPrintedCheckInfo.containsKey("checkId")) {
								try {
									oTempInfo.put("checkId", String.valueOf(oPrintedCheckInfo.get("checkId")));
									oTempInfo.put("outTradeNumber", oPrintedCheckInfo.get("outTradeNumber"));
									oTempInfo.put("result", PAY_CHECK_FAIL);
									oTempInfo.put("errorCode", oPaymentResult.optString("errorCode"));
									oTempInfo.put("errorMessage", oPaymentResult.optString("errorMessage"));
									oTempInfo.put("errorMemo", oPaymentResult.optString("errorMemo"));
								}catch(JSONException e) {}
							}
							oPaidCheckList.add(oTempInfo);
							break;
						}
					}
				} 
				//check whether check is paid
				else if(oPaymentResult != null && oPaymentResult.has("tradeStatus") && oPaymentResult.optInt("tradeStatus", -1) == 0) {
					String sOutTradeNumber = oPaymentResult.optString("outTradeNumber", "");
					
					for(HashMap<String, String> oPrintedCheckInfo: AppGlobal.g_oCheckListForPaymentInterface.get(AppGlobal.g_oFuncOutlet.get().getOutletId())) {
						if(oPrintedCheckInfo.containsKey("outTradeNumber") && oPrintedCheckInfo.get("outTradeNumber").equals(sOutTradeNumber)) {
							JSONObject oTempInfo = new JSONObject();
							if(oPrintedCheckInfo.containsKey("checkId")) {
								try {
									oTempInfo.put("checkId", String.valueOf(oPrintedCheckInfo.get("checkId")));
									oTempInfo.put("outTradeNumber", oPrintedCheckInfo.get("outTradeNumber"));
									oTempInfo.put("result", PAY_CHECK_SUCCESS);
									BigDecimal dCheckTotal = new BigDecimal(oPaymentResult.optString("payTotal"));
									dCheckTotal = dCheckTotal.add(new BigDecimal(oPaymentResult.optString("discountTotal")));
									oTempInfo.put("checkTotal", dCheckTotal.toPlainString());
									oTempInfo.put("payTotal", oPaymentResult.optString("payTotal"));
									oTempInfo.put("discountTotal", oPaymentResult.optString("discountTotal"));
								}catch (JSONException e) {}
							}
							oPaidCheckList.add(oTempInfo);
							break;
						}
					}
				}
			}
		}
		return oPaidCheckList;
	}
	
	// add extra info to check payment
	private void addUpdateExtraInfoToCheckPaymentAfterPosting(PosCheckPayment oPosCheckPayment, boolean bAdd, boolean bOnGoing) {
		if(m_oPaymentInterface.getInterfaceVendorKey().equals(InfVendor.KEY_SCAN_PAY)) {
			
			//On going
			if(bOnGoing){
				//save request json
				oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERNAL_USE_ENQUIRY, 0, m_oPostingRequestJSON.toString());
			}
			
			//point amount
			if(bAdd) {
				oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_POINTS_BALANCE, 0, m_oPostingResponse.dPointAmount.toPlainString());
			}else
				oPosCheckPayment.updateExtraInfo(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_POINTS_BALANCE, 0, m_oPostingResponse.dPointAmount.toPlainString());
			
			//account number
			if(bAdd) {
				oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER, 0, m_oPostingResponse.sAccountNumber);
			}else
				oPosCheckPayment.updateExtraInfo(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER, 0, m_oPostingResponse.sAccountNumber);
			
			//internal used
			if(bAdd) {
				JSONObject oInternalUsedJSONObject = new JSONObject();
				try {
					oInternalUsedJSONObject.put("merchantName", m_oPostingResponse.sCompanyName);
					oInternalUsedJSONObject.put("merchantId", m_oPaymentInterface.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").optJSONObject("merchant_id").optString("value", ""));
					oInternalUsedJSONObject.put("stationCode", AppGlobal.g_oFuncStation.get().getCode());
					oInternalUsedJSONObject.put("employeeCode", AppGlobal.g_oFuncUser.get().getUserNumber());
					oInternalUsedJSONObject.put("transactionTime", m_oPostingResponse.sRefernces[0]);
					oInternalUsedJSONObject.put("platformTransactionNum", m_oPostingResponse.sRefernces[1]);
					oInternalUsedJSONObject.put("transactionPayTotal", m_oPostingResponse.dPayAmount);
					oInternalUsedJSONObject.put("invoiceTotal", m_oPostingResponse.dInvoiceAmount);
					oInternalUsedJSONObject.put("channelTransactionNum", m_oPostingResponse.sRefernces[2]);
				}catch (JSONException e) {}
				
				oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERNAL_USE, 0, oInternalUsedJSONObject.toString());	
			}
			
		}
	}
	
	// get release payment result with OGS interface
	public boolean releaseOgsPayment(FuncCheck oFuncCheck) {
		if(!m_oPaymentInterface.getInterfaceVendorKey().equals(InfVendor.KEY_OGS))
			return false;

		boolean bResult = false;
		if(!oFuncCheck.getCheckExtraInfoList().isEmpty()) {
			int iPayType = 0;
			String sOutTradeNumber = "";
			for(PosCheckExtraInfo oCheckExtraInfo: oFuncCheck.getCheckExtraInfoList()) {
				if(oCheckExtraInfo.getBy().equals(PosCheckExtraInfo.BY_PAYMENT) &&
						oCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE)){
					if(oCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_PAYTYPE)){
						if(oCheckExtraInfo.getValue() != null)
							iPayType = Integer.parseInt(oCheckExtraInfo.getValue());
					}else if(oCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_OUT_TRADE_NUMBER)){
						if(oCheckExtraInfo.getValue() != null)
							sOutTradeNumber =  oCheckExtraInfo.getValue();
					}
				}
			}
			JSONObject oCheckInfo = new JSONObject();
			try {
				oCheckInfo.put("checkTotal", oFuncCheck.getCheckTotal());
				oCheckInfo.put("outTradeNumber", sOutTradeNumber);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			bResult = m_oPaymentInterface.releaseOgsPayment(AppGlobal.g_oFuncOutlet.get().getOutletCode(), iPayType, oCheckInfo);
			if(!bResult) {
				if(m_oPaymentInterface.getLastErrorCode() > 0)
					m_sLastErrorMessage = getErrorMessage(m_oPaymentInterface.getLastErrorCode());
				else 
					m_sLastErrorMessage = m_oPaymentInterface.getLastErrorMessage();
			}
		}
		
		return bResult;		
	}
	
	// do payment card related action, including
	// credit card authorization / topup / cancel auth / cancel topup / complete auth / cancel complete auth
	public boolean cardAuthorization(FuncCheck oFuncCheck, String sAuthorizationType, String sCancelAuthType, String sRefNum, String sAuthAmount, String sTotalAmount, String sSurchargeAmt, String sTraceNo, String sInvoiceNo, String sIssuer, String sAuthorizationCode, String sToken){
		InfInterface oInterface = new InfInterface();
		PostingInfo oPostingInfo = new PostingInfo();
		initPostingInfo(oPostingInfo);
		String sGuestNo = "";
		
		oPostingInfo.iInterfaceId = m_oPaymentInterface.getInterfaceId();
		oPostingInfo.iOutletId = AppGlobal.g_oFuncOutlet.get().getOutletId();
		if(oFuncCheck.getCheckPrefixNo() == null)
			oPostingInfo.sCheckNumber = "";
		else
			oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
		
		oPostingInfo.sAuthReferenceNumber = sRefNum;
		oPostingInfo.sStationCode = AppGlobal.g_oFuncStation.get().getCode();
		oPostingInfo.sEmployeeNum = AppGlobal.g_oFuncUser.get().getUserNumber();
		oPostingInfo.sEmployeeName = AppGlobal.g_oFuncUser.get().getUserName(AppGlobal.g_oCurrentLangIndex.get());
		
		sGuestNo = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_GUEST_NO);
		if(sGuestNo == ""){
			UUID uuid = UUID.fromString("400000-8cf0-11bd-b23e-10b96e4ef0");
			sGuestNo = uuid.randomUUID().toString().replace("-", "");
			oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_GUEST_NO, 0, sGuestNo);
		}
		
		oPostingInfo.sGuestNo = sGuestNo;
		oPostingInfo.sAuthorizeAmt = new BigDecimal(sAuthAmount).setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_UP).toPlainString();
		oPostingInfo.sTotalAmt = sTotalAmount;
		
		if(sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.complete_authorization.name()) || sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_complete_authorization.name())
			|| sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.sale.name()) || sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.adjust_tip.name()))
			oPostingInfo.sSurchargeAmt = sSurchargeAmt;
		
		oPostingInfo.sTraceNumber = sTraceNo;
		oPostingInfo.sInvoiceNumber = sInvoiceNo;
		oPostingInfo.sAuthCode = sAuthorizationCode;
		oPostingInfo.sIssuer = sIssuer;
		if(sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.complete_authorization.name()) || sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_complete_authorization.name()))
			oPostingInfo.dPostAmount = new BigDecimal(sAuthAmount).setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_UP);
		if(!sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name()) && !sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.manual_authorization.name()))
			oPostingInfo.sToken = sToken;
		
		// really call interface to do corresponding function
		JSONObject oResponseJSONObject = new JSONObject();
		if(sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name()))
			// CARD AUTHORIZATION
			oResponseJSONObject = oInterface.paymentInterfaceCardAuth(fromPaymentPostingJSONObject(oPostingInfo));
		else if(sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.manual_authorization.name())){
			// MANUAL AUTHORIZATION
			oPostingInfo.sEntryMode = "M";
			oResponseJSONObject = oInterface.paymentInterfaceCardAuth(fromPaymentPostingJSONObject(oPostingInfo));
		}
		else if(sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.top_up_authorization.name()))
			// TOP-UP AUTHORIZATION
			oResponseJSONObject = oInterface.paymentInterfaceCardTopupAuth(fromPaymentPostingJSONObject(oPostingInfo));
		else if(sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_authorization.name())){
			// CANCEL AUTH / TOP-UP
			
			//cancel card auth
			if(sCancelAuthType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name()))
				oResponseJSONObject = oInterface.paymentInterfaceVoidCardAuth(fromPaymentPostingJSONObject(oPostingInfo));
			
			//cancel top up auth
			else if(sCancelAuthType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.top_up_authorization.name()))
				oResponseJSONObject = oInterface.paymentInterfaceVoidTopupAuth(fromPaymentPostingJSONObject(oPostingInfo));
		}
		else if(sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_complete_authorization.name()))
			// CANCEL COMPLETE AUTH
			oResponseJSONObject = oInterface.paymentInterfaceVoidCompleteAuth(fromPaymentPostingJSONObject(oPostingInfo));
		else if(sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.complete_authorization.name()))
			// COMPLETE AUTH
			oResponseJSONObject = oInterface.paymentInterfaceCompleteAuth(fromPaymentPostingJSONObject(oPostingInfo));
		else if(sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.sale.name())){
			
			if(sCancelAuthType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.sale.name()))
				// void sale
				oResponseJSONObject = oInterface.paymentInterfaceVoidPosting(fromPaymentPostingJSONObject(oPostingInfo));
			else
				oResponseJSONObject = oInterface.paymentInterfacePosting(fromPaymentPostingJSONObject(oPostingInfo));
		}
		else if(sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.adjust_tip.name()))
			oResponseJSONObject = oInterface.paymentInterfaceTip(fromPaymentPostingJSONObject(oPostingInfo));
		
		//parsing returned response
		if(oResponseJSONObject == null)
			return false;
		else {
			if(oResponseJSONObject.optBoolean("postingResult", false) == false) {
				if(oResponseJSONObject.has("errorCode") && oResponseJSONObject.optInt("errorCode") > 0) {
					m_iLastErrorCode = oResponseJSONObject.optInt("errorCode");
					m_sLastErrorMessage = getErrorMessage(oResponseJSONObject.optInt("errorCode"));
				}else {
					m_iLastErrorCode = 0;
					if(oResponseJSONObject.has("errorMessage") && oResponseJSONObject.optString("errorMessage") != null && !oResponseJSONObject.optString("errorMessage").isEmpty())
						m_sLastErrorMessage = oResponseJSONObject.optString("errorMessage");
					else
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("invalid_response");
				}
				return false;
			}else{
				//success
				if(oResponseJSONObject.has("result")) {
					m_oPostingResponse.oPosPaymentGatewayTransactions.init();
					
					JSONObject oPostingResultJSON = oResponseJSONObject.optJSONObject("result");
					if(oPostingResultJSON.has("authorizationCode"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setAuthCode(oPostingResultJSON.optString("authorizationCode"));
					if(oPostingResultJSON.has("issuer"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setIssuer(oPostingResultJSON.optString("issuer"));
					if(oPostingResultJSON.has("traceNumber"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setTraceNo(oPostingResultJSON.optString("traceNumber"));
					if(oPostingResultJSON.has("invoiceNumber"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setInvoiceNo(oPostingResultJSON.optString("invoiceNumber"));
					if(oPostingResultJSON.has("referenceNumber"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setRefNo(oPostingResultJSON.optString("referenceNumber"));
					if(oPostingResultJSON.has("acquirerInfo") && oPostingResultJSON.optJSONObject("acquirerInfo") != null){
						JSONObject oAcquirerJSON = oPostingResultJSON.optJSONObject("acquirerInfo");
						if(oAcquirerJSON.has("Merchant"))
							m_oPostingResponse.oPosPaymentGatewayTransactions.setAcquirerMerchantId(oAcquirerJSON.optString("Merchant"));
						if(oAcquirerJSON.has("Terminal"))
							m_oPostingResponse.oPosPaymentGatewayTransactions.setAcquirerTerminalId(oAcquirerJSON.optString("Terminal"));
						if(oAcquirerJSON.has("name"))
							m_oPostingResponse.oPosPaymentGatewayTransactions.setAcquirerName(oAcquirerJSON.optString("name"));
						if(oAcquirerJSON.has("printData"))
							m_oPostingResponse.oPosPaymentGatewayTransactions.setAcquirerData(oAcquirerJSON.optString("printData"));
						if(oAcquirerJSON.has("datetime")){
							DateTimeFormatter dateTimeFmt2 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
							DateTimeFormatter dateTimeFmt3 = DateTimeFormat.forPattern("yyyyMMddHHmmss");
							if(oAcquirerJSON.optString("datetime").length() == 14)
								m_oPostingResponse.oPosPaymentGatewayTransactions.setAcquirerTransDate(dateTimeFmt3.parseDateTime(oAcquirerJSON.optString("datetime")));
							else
								m_oPostingResponse.oPosPaymentGatewayTransactions.setAcquirerTransDate(dateTimeFmt2.parseDateTime(oAcquirerJSON.optString("datetime")));
						}
					}
					if(oPostingResultJSON.has("emv"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setEmv(oPostingResultJSON.optString("emv"));
					if(oPostingResultJSON.has("emvData"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setEmvData(oPostingResultJSON.optString("emvData"));
					if(oPostingResultJSON.has("currencyCode"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setCurrencyCode(oPostingResultJSON.optString("currencyCode"));
					if(oPostingResultJSON.has("entryMode"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setEntryMode(oPostingResultJSON.optString("entryMode"));
					if(oPostingResultJSON.has("icCardSeq"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setIcCardSeq(oPostingResultJSON.optString("icCardSeq"));
					if(oPostingResultJSON.has("intlCardTraceNumber"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setIntlIcCardTraceNo(oPostingResultJSON.optString("intlCardTraceNumber"));
					if(oPostingResultJSON.has("maskedPan"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setMaskedPan(oPostingResultJSON.optString("maskedPan"));
					if(oPostingResultJSON.has("eCashBalance"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.seteCashBalance(oPostingResultJSON.optString("eCashBalance"));
					if(oPostingResultJSON.has("terminalSeq"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setTermainalSeq(oPostingResultJSON.optString("terminalSeq"));
					if(oPostingResultJSON.has("signFree"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setSignFree(oPostingResultJSON.optString("signFree"));
					if(oPostingResultJSON.has("signFreeData"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setSignFreeData(oPostingResultJSON.optString("signFreeData"));
					if(oPostingResultJSON.has("eSignature"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.seteSignature(oPostingResultJSON.optString("eSignature"));
					if(oPostingResultJSON.has("merchantCopy"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setMerchantCopy(oPostingResultJSON.optString("merchantCopy"));
					if(oPostingResultJSON.has("customerCopy"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setCustomerCopy(oPostingResultJSON.optString("customerCopy"));
					if(oPostingResultJSON.has("surchargeAmount")) 
						m_oPostingResponse.oPosPaymentGatewayTransactions.setSurchargeAmount(new BigDecimal(oPostingResultJSON.optString("surchargeAmount")));
					if(oPostingResultJSON.has("txnAmt"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setTxnAmount(new BigDecimal(oPostingResultJSON.optString("txnAmt")));
					if(oPostingResultJSON.has("tipsAmount"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setTips(new BigDecimal(oPostingResultJSON.optString("tipsAmount")));
					if(oPostingResultJSON.has("token"))
						m_oPostingResponse.oPosPaymentGatewayTransactions.setToken(oPostingResultJSON.optString("token"));
					// For backward compatible, just update the txnType when void complete author.
					if(oPostingResultJSON.has("txnType") && oPostingResultJSON.optInt("txnType") == 8)
						m_oPostingResponse.oPosPaymentGatewayTransactions.setType(PosPaymentGatewayTransactions.TYPE_COMPLETE_AUTH);
					if(sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.sale.name())){
						if(oPostingResultJSON.has("totalAmount")) {
							BigDecimal dSurcharge = m_oPostingResponse.oPosPaymentGatewayTransactions.getSurcharge();
							BigDecimal dTips = m_oPostingResponse.oPosPaymentGatewayTransactions.getTips();
							BigDecimal dReceivedTotal = new BigDecimal(oPostingResultJSON.optString("totalAmount"));
							
							if(dSurcharge.compareTo(BigDecimal.ZERO) == 0
									&& dTips.compareTo(BigDecimal.ZERO) == 0
									&& dReceivedTotal.compareTo(new BigDecimal(sTotalAmount)) == 1) {
								BigDecimal dExtraTip = dReceivedTotal.subtract(new BigDecimal(sTotalAmount));
								m_oPostingResponse.oPosPaymentGatewayTransactions.setTips(dExtraTip);
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	// do payment interface posting
	public boolean doPosting(FuncCheck oFuncCheck, PosCheckPayment oPosCheckPayment) {
		PostingInfo oPostingInfo = new PostingInfo();
		DateTimeFormatter dateTimeFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		int iInterfaceId = Integer.parseInt(oPosCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0).getValue());
		
		initPostingInfo(oPostingInfo);
		oPostingInfo.iInterfaceId = iInterfaceId;
		oPostingInfo.sTransactionKey = oPosCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_TRACE_ID, 0).getValue();
		oPostingInfo.sEmployeeCode = AppGlobal.g_oFuncUser.get().getUserNumber();
		oPostingInfo.sStationCode = AppGlobal.g_oFuncStation.get().getCode();
		oPostingInfo.sReference = oPosCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_REFERENCE, 0).getValue();
		oPostingInfo.dPostAmount = oPosCheckPayment.getPayTotal();
		oPostingInfo.sPostDateTime = dateTimeFmt.print(AppGlobal.getCurrentTime(false));
		
		if(m_oPaymentInterface.getInterfaceVendorKey().equals(InfVendor.KEY_SCAN_PAY)) {
			String sPaymentType = oPosCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_PAYTYPE, 0).getValue();
			if(sPaymentType != null)
				oPostingInfo.sPaymentType = sPaymentType;
		}
		
		JSONObject oPostingRequestJSON = new JSONObject();
		//for scan pay, posting status enquiry API
		if(m_oPaymentInterface.getInterfaceVendorKey().equals(InfVendor.KEY_SCAN_PAY) && m_oPostingRequestJSON != null)
			oPostingRequestJSON = m_oPostingRequestJSON;
		else
			oPostingRequestJSON = fromPaymentPostingJSONObject(oPostingInfo);
		
		
		InfInterface oInterface = new InfInterface();
		
		JSONObject oResponseJSONObject = oInterface.paymentInterfacePosting(oPostingRequestJSON);
		
		if(oResponseJSONObject == null)
			return false;
		else {
			if(oResponseJSONObject.optBoolean("postingResult", false) == false) {
				if(oResponseJSONObject.has("errorCode") && oResponseJSONObject.optInt("errorCode") > 0) {
					m_iLastErrorCode = oResponseJSONObject.optInt("errorCode");
					m_sLastErrorMessage = getErrorMessage(oResponseJSONObject.optInt("errorCode"));
				}else {
					m_iLastErrorCode = 0;
					if(oResponseJSONObject.has("errorMessage") && oResponseJSONObject.optString("errorMessage") != null && !oResponseJSONObject.optString("errorMessage").isEmpty())
						m_sLastErrorMessage = oResponseJSONObject.optString("errorMessage");
					else
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("invalid_response");
				}
				return false;
			}else {
				//success
				if(oResponseJSONObject.has("result")) {
					JSONObject oPostingResultJSON = oResponseJSONObject.optJSONObject("result");
					if(oPostingResultJSON.has("accountNumber"))
						m_oPostingResponse.sAccountNumber = oPostingResultJSON.optString("accountNumber");
					if(oPostingResultJSON.has("actualPayAmount"))
						m_oPostingResponse.dActualPayAmount = new BigDecimal(oPostingResultJSON.optString("actualPayAmount"));
					if(oPostingResultJSON.has("payAmount"))
						m_oPostingResponse.dPayAmount = new BigDecimal(oPostingResultJSON.optString("payAmount"));
					if(oPostingResultJSON.has("invoiceAmount"))
						m_oPostingResponse.dInvoiceAmount = new BigDecimal(oPostingResultJSON.optString("invoiceAmount"));
					if(oPostingResultJSON.has("discountAmount"))
						m_oPostingResponse.dDiscountAmount = new BigDecimal(oPostingResultJSON.optString("discountAmount"));
					if(oPostingResultJSON.has("pointAmount"))
						m_oPostingResponse.dPointAmount = new BigDecimal(oPostingResultJSON.optString("pointAmount"));
					if(oPostingResultJSON.has("companyName"))
						m_oPostingResponse.sCompanyName = oPostingResultJSON.optString("companyName");
					if(oPostingResultJSON.has("references")) {
						for(int i=0; i<FuncPaymentInterface.POSTING_RESPONSE_REF_NO; i++) {
							if(oPostingResultJSON.optJSONObject("references").has(String.valueOf((i+1))))
								m_oPostingResponse.sRefernces[i] = oPostingResultJSON.optJSONObject("references").optString(String.valueOf(i+1), "");
						}
					}
					
					boolean bOnGoingStatus = false;
					if (m_oPaymentInterface.getInterfaceVendorKey().equals(InfVendor.KEY_SCAN_PAY)) {
						if(oPostingResultJSON.has("code")){
							try {
								if(oPostingResultJSON.get("code").equals(FuncPaymentInterface.TRANSACTION_ON_GOING_CODE))
									bOnGoingStatus = true;
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							m_oPostingResponse.sCode = oPostingResultJSON.optString("code");
						}
					}
					
					//add extra info
					addUpdateExtraInfoToCheckPaymentAfterPosting(oPosCheckPayment, true , bOnGoingStatus);
					
				}
			}
		}
		
		return true;
	}
	
	// do void payment interface posting
	public boolean doVoidPosting(FuncCheck oFuncCheck, PosCheckPayment oPosCheckPayment) {
		PostingInfo oPostingInfo = new PostingInfo();
		DateTimeFormatter dateTimeFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

		if(oPosCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0) == null)
			return true;
		
		int iInterfaceId = Integer.parseInt(oPosCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0).getValue());
		
		if(m_oPaymentInterface.getInterfaceVendorKey().equals(InfVendor.KEY_PAY_AT_TABLE))
			return true;
		
		initPostingInfo(oPostingInfo);
		oPostingInfo.iInterfaceId = iInterfaceId;
		oPostingInfo.sTransactionKey = "R_"+oPosCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_TRACE_ID, 0).getValue();
		oPostingInfo.sVoidTransactionKey = oPosCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_TRACE_ID, 0).getValue();
		oPostingInfo.sEmployeeCode = AppGlobal.g_oFuncUser.get().getUserNumber();
		oPostingInfo.sStationCode = AppGlobal.g_oFuncStation.get().getCode();
		oPostingInfo.dPostAmount = oPosCheckPayment.getPayTotal();
		oPostingInfo.sPostDateTime = dateTimeFmt.print(AppGlobal.getCurrentTime(false));
		
		if(m_oPaymentInterface.getInterfaceVendorKey().equals(InfVendor.KEY_SCAN_PAY)) {
			String sPaymentType = oPosCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE, PosCheckExtraInfo.VARIABLE_PAYTYPE, 0).getValue();
			if(sPaymentType != null)
				oPostingInfo.sPaymentType = sPaymentType;
		}
		
		InfInterface oInterface = new InfInterface();
		JSONObject oResponseJSONObject = oInterface.paymentInterfaceVoidPosting(fromPaymentPostingJSONObject(oPostingInfo));
		
		if(oResponseJSONObject == null)
			return false;
		else {
			if(oResponseJSONObject.optBoolean("postingResult", false) == false) {
				if(oResponseJSONObject.has("errorCode") && oResponseJSONObject.optInt("errorCode") > 0) {
					m_iLastErrorCode = oResponseJSONObject.optInt("errorCode");
					m_sLastErrorMessage = getErrorMessage(oResponseJSONObject.optInt("errorCode"));
				}else {
					m_iLastErrorCode = 0;
					if(oResponseJSONObject.has("errorMessage") && oResponseJSONObject.optString("errorMessage") != null && !oResponseJSONObject.optString("errorMessage").isEmpty())
						m_sLastErrorMessage = oResponseJSONObject.optString("errorMessage");
					else
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("invalid_response");
				}
				return false;
			}else {
				//success
				if(oResponseJSONObject.has("result")) {
					JSONObject oPostingResultJSON = oResponseJSONObject.optJSONObject("result");
					if(oPostingResultJSON.has("accountNumber"))
						m_oPostingResponse.sAccountNumber = oPostingResultJSON.optString("accountNumber");
					if(oPostingResultJSON.has("actualPayAmount"))
						m_oPostingResponse.dActualPayAmount = new BigDecimal(oPostingResultJSON.optString("actualPayAmount"));
					if(oPostingResultJSON.has("companyName"))
						m_oPostingResponse.sCompanyName = oPostingResultJSON.optString("companyName");
					if(oPostingResultJSON.has("references")) {
						for(int i=0; i<FuncPaymentInterface.POSTING_RESPONSE_REF_NO; i++) {
							if(oPostingResultJSON.optJSONObject("references").has(String.valueOf((i+1))))
								m_oPostingResponse.sRefernces[i] = oPostingResultJSON.optJSONObject("references").optString(String.valueOf(i+1), "");
						}
					}
					
					if(oPostingResultJSON.has("code"))
						m_oPostingResponse.sCode = oPostingResultJSON.optString("code");
					
					//add extra info
					addUpdateExtraInfoToCheckPaymentAfterPosting(oPosCheckPayment, false, false);
					
				}
			}
		}
		
		return true;
	}
	
	// print void payment interface slip
	public void printVoidPostingSlip(String sType, List<PosCheckPayment> oCheckPayments) {
		JSONObject oPrintParams = new JSONObject();
		
		if(sType.equals(FuncPaymentInterface.PRINT_SLIP_TYPE_SCAN_PAY_VOID)) {
			JSONArray oJSONArray = new JSONArray();
			for(PosCheckPayment oCheckPayment: oCheckPayments)
				oJSONArray.put(oCheckPayment.constructAddSaveJSON(true));
			try {
				oPrintParams.put("checkPayments", oJSONArray);
			}catch (JSONException e) {}
		}
		
		PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
		oInterfaceConfig.printPaymentInterfaceAlertSlip(AppGlobal.g_oFuncStation.get().getCheckPrtqId(), sType, AppGlobal.g_oCurrentLangIndex.get(), oPrintParams);
	}
	
	//Get pre-order
	public JSONObject getPreOrder(String sOrderRefNo) {
		JSONObject oResultJSONObject = new JSONObject();
		oResultJSONObject = m_oPaymentInterface.getPreOrder(AppGlobal.g_oFuncOutlet.get().getOutletCode(), sOrderRefNo);
		return oResultJSONObject;
	}
	
	// restart payment interface shell
	public boolean restartPaymentInterfaceShell(int iInterfaceId) {
		PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
		boolean bResult = oInterfaceConfig.restartPaymentInterfaceShell(iInterfaceId);
		if(!bResult) {
			if(oInterfaceConfig.getLastErrorMessage().equals("shell_is_not_alive"))
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_open_shell");
			else if(oInterfaceConfig.getLastErrorCode() > 0)
				m_sLastErrorMessage = getErrorMessage(oInterfaceConfig.getLastErrorCode());
			else 
				m_sLastErrorMessage = oInterfaceConfig.getLastErrorMessage();
		}
		
		return bResult;
	}
	
	// stop payment interface shell
	public boolean stopPaymentInterfaceShell(int iInterfaceId) {
		PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
		boolean bResult = oInterfaceConfig.stopPaymentInterfaceShell(iInterfaceId);
		m_sLastErrorMessage = AppGlobal.g_oLang.get()._("shell_is_stopped");
		if(!bResult)
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("stop_shell_fail");
		
		return bResult;
	}
	
	public String getErrorMessage(int iErrorCode) {
		String sErrorMessage = "";
		
		switch (iErrorCode) {
		case 1:
			sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_build_connection");
			break;
		case 2:
			sErrorMessage = AppGlobal.g_oLang.get()._("no_such_interface");
			break;
		case 3:
			sErrorMessage = AppGlobal.g_oLang.get()._("missing_interface_setup");
			break;
		case 4:
			sErrorMessage = AppGlobal.g_oLang.get()._("missing_setup");
			break;
		case 5:
			sErrorMessage = AppGlobal.g_oLang.get()._("empty_response");
			break;
		case 6:
			sErrorMessage = AppGlobal.g_oLang.get()._("unmatch_out_trade_number");
			break;
		case 7:
			sErrorMessage = AppGlobal.g_oLang.get()._("incorrect_posting_information");
			break;
		case 8:
			sErrorMessage = AppGlobal.g_oLang.get()._("no_response");
			break;
		case 9:
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_response");
			break;
		case 10:
			sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_receive_response_packet");
			break;
		case 11:
			sErrorMessage = AppGlobal.g_oLang.get()._("query_timeout");
			break;
		default:
			sErrorMessage = null;
			break;
		}
		
		return sErrorMessage;
	}
	
	public int getLastErrorCode() {
		return this.m_iLastErrorCode;
	}
	
	public String getLastErrorMessage() {
		return this.m_sLastErrorMessage;
	}
	
	public BigDecimal getActaulPayAmount() {
		return this.m_oPostingResponse.dActualPayAmount;
	}
	
	public BigDecimal getDiscountAmount() {
		return this.m_oPostingResponse.dDiscountAmount;
	}
	
	public PosPaymentGatewayTransactions getPosPaymentGatewayTransactions() {
		return this.m_oPostingResponse.oPosPaymentGatewayTransactions;
	}
	
	public String getCode(){
		return this.m_oPostingResponse.sCode;
	}
	
	public JSONObject getPostingRequestJSON(){
		return this.m_oPostingRequestJSON;
	}
	
	public void setPostingRequestJSON(JSONObject oPostingJSON){
		this.m_oPostingRequestJSON = oPostingJSON;
	}
	
	public String[] getReferences(){
		return this.m_oPostingResponse.sRefernces;
	}
	
	public BigDecimal getPayAmount() {
		return this.m_oPostingResponse.dPayAmount;
	}
	
	public BigDecimal getInvoiceTotal() {
		return this.m_oPostingResponse.dInvoiceAmount;
	}
	
	public String getCompanyName(){
		return this.m_oPostingResponse.sCompanyName;
	}
}