package app;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import externallib.Util;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import commonui.FormDialogBox;
import externallib.StringLib;
import om.*;

public class FuncPayment {
	
	// Check total
	private BigDecimal m_dCheckTotal;
	private BigDecimal m_dCurrentBalance;
	private BigDecimal m_dTipsTotal;
	private BigDecimal m_dResidueTotal;
	private BigDecimal m_dSurchargeTotal;
	private BigDecimal m_dTotalDue;
	private BigDecimal m_dChangeTotal;
	
	// Rounding method
	private String m_sCheckRoundMethod;
	private String m_sPayRoundMethod;
	private Integer m_iCheckRoundDecimal;
	private Integer m_iPayRoundDecimal;
	
	// Check payment list
	private ArrayList<PosCheckPayment> m_oPosCheckPaymentList;
	private boolean m_bSupportSurcharge;
	
	private boolean m_bIsNextDummyPayment;
	
	// Open drawer flag
	private HashMap<Integer, Boolean> m_oOpenDrawerFlag;
	
	// Payment method list
	private PosPaymentMethodList m_oPosPaymentMethodList;
	
	// Currency list
	private HashMap<String, OutCurrency> m_oCurrencyList;
	
	// Extra info list
	private ArrayList<PosCheckExtraInfo> m_oExtraInfoList;
	
	private String m_sTaiwanGUIRefNum;
	private int m_iTaiwanGUISkipTransNum;
	private String m_sTaiwanGUICarrier;
	
	// Last error message
	private String m_sErrorMessage;
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage;
	}
	
	public FuncPayment() {
		m_oCurrencyList = new HashMap<String, OutCurrency>();
		m_oPosCheckPaymentList = new ArrayList<PosCheckPayment>();
		m_bSupportSurcharge = false; 
		m_oOpenDrawerFlag = new HashMap<Integer, Boolean>();
		m_oExtraInfoList = new ArrayList<PosCheckExtraInfo>();
		m_bIsNextDummyPayment = false;
	}

	public void readAllPaymentMethod(int shopId, int outletId, String sBusinessDay, boolean bIsHoliday, boolean bIsDayBeforeHoliday, boolean bIsSpecialDay, boolean bIsDayBeforeSpecialDay, int iWeekday){
		// Load payment method from OM
		m_oPosPaymentMethodList = new PosPaymentMethodList();
		m_oPosPaymentMethodList.readAllWithAccessControl(shopId, outletId, sBusinessDay, bIsHoliday, bIsDayBeforeHoliday, bIsSpecialDay, bIsDayBeforeSpecialDay, iWeekday);
		
		for(Map.Entry<Integer, PosPaymentMethod> entry:m_oPosPaymentMethodList.getPaymentMethodList().entrySet()) {
			PosPaymentMethod oPosPaymentMethod = entry.getValue();
			if (oPosPaymentMethod.getSurchargeRate().compareTo(BigDecimal.ZERO) != 0) {
				this.m_bSupportSurcharge = true;
				break;
			}
		}
	}
	
	// Init the structure for payment process
	public void init(BigDecimal dCheckTotal, String sCheckRoundMethod, int iCheckRoundDecimal, String sPayRoundMethod, int iPayRoundDecimal){
		m_dCheckTotal = dCheckTotal;
		m_dCurrentBalance = dCheckTotal;
		m_dTipsTotal = BigDecimal.ZERO;
		m_dResidueTotal = BigDecimal.ZERO;
		m_dSurchargeTotal = BigDecimal.ZERO;
		m_dTotalDue = BigDecimal.ZERO;
		m_dChangeTotal = BigDecimal.ZERO;
		m_oCurrencyList.clear();
		m_oPosCheckPaymentList.clear();
		m_oOpenDrawerFlag.clear();
		m_sCheckRoundMethod = sCheckRoundMethod;
		m_iCheckRoundDecimal = iCheckRoundDecimal;
		m_sPayRoundMethod = sPayRoundMethod;
		m_iPayRoundDecimal = iPayRoundDecimal;
		m_sTaiwanGUIRefNum = "";
		m_iTaiwanGUISkipTransNum = 0;
		m_sTaiwanGUICarrier = "";
	}
	
	// Retrieve payment method and assign to check payment structure
	// Return value :	-1 - error occur
	//					1 - Add payment successfully and there is remain balance
	//					2 - Add payment successfully and this should be the last payment 
	public int addPayment(int iPaymentMethodId, int iShopId, int iOutletId, String sCheckId, PosBusinessDay oBusinessDay, String iBperId, BigDecimal dPayTotal, BigDecimal dTipsTotal, int iEmployeeId, int iMemberId, String[] sRefData, ArrayList<PosCheckExtraInfo> oExtraInfoList, int iAppliedUserId) {
		int iRet;
		BigDecimal dFinalChangeTotal = BigDecimal.ZERO;
		BigDecimal dFinalPayTotal = BigDecimal.ZERO;
		BigDecimal dFinalTipsTotal = BigDecimal.ZERO;
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		m_sErrorMessage = "";
		
		if(m_oPosPaymentMethodList.getPaymentMethodList().containsKey(iPaymentMethodId) == false){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("payment_method_not_found");
			return -1;
		}
		
		// Get the payment method object
		PosPaymentMethod oPosPaymentMethod = m_oPosPaymentMethodList.getPaymentMethodList().get(iPaymentMethodId);
		if(m_dCurrentBalance.signum() != -1){
			//positive balance calculation
			// Check balance and calculate the tips or change
			if(dPayTotal.compareTo(m_dCurrentBalance) > 0){
				if((!oPosPaymentMethod.HaveTips() && !oPosPaymentMethod.isResidueTips()) || AppGlobal.g_oFuncStation.get().isPaymentSkipTips(oPosPaymentMethod.getPaymentCode())){
					// Remain is treated as change
					dFinalTipsTotal = dTipsTotal;
					dFinalChangeTotal = dPayTotal.subtract(m_dCurrentBalance);
				}else{
					// Remain is treated as tips
					dFinalTipsTotal = dTipsTotal;
					dFinalTipsTotal = dFinalTipsTotal.add(dPayTotal.subtract(m_dCurrentBalance));
				}
				
				dFinalPayTotal = m_dCurrentBalance;
				m_dCurrentBalance = BigDecimal.ZERO;
			}else{
				m_dCurrentBalance = m_dCurrentBalance.subtract(dPayTotal);
				dFinalTipsTotal = dTipsTotal;
				dFinalPayTotal = dPayTotal;
			}
		}else{
			//negative balance calculation
			// Check balance and calculate the tips or change
			if(dPayTotal.compareTo(m_dCurrentBalance) < 0){
				if((!oPosPaymentMethod.HaveTips() && !oPosPaymentMethod.isResidueTips()) || AppGlobal.g_oFuncStation.get().isPaymentSkipTips(oPosPaymentMethod.getPaymentCode())){
					// Remain is treated as change
					dFinalTipsTotal = dTipsTotal;
					dFinalChangeTotal = dPayTotal.subtract(m_dCurrentBalance);
				}else{
					// Remain is treated as tips
					dFinalTipsTotal = dTipsTotal;
					dFinalTipsTotal = dFinalTipsTotal.add(dPayTotal.subtract(m_dCurrentBalance));
				}
				
				dFinalPayTotal = m_dCurrentBalance;
				m_dCurrentBalance = BigDecimal.ZERO;
			}else{
				m_dCurrentBalance = m_dCurrentBalance.subtract(dPayTotal);
				dFinalTipsTotal = dTipsTotal;
				dFinalPayTotal = dPayTotal;
			}
		}
		
		if(!oPosPaymentMethod.isResidueTips())
			m_dResidueTotal = m_dResidueTotal.add(dFinalTipsTotal);
		else
			m_dTipsTotal = m_dTipsTotal.add(dFinalTipsTotal);
			
		m_dChangeTotal = dFinalChangeTotal;
		
		// Assign value to oCheckPayment
		PosCheckPayment oCheckPayment = new PosCheckPayment();
		oCheckPayment.setBdayId(oBusinessDay.getBdayId());
		oCheckPayment.setBperId(iBperId);
		oCheckPayment.setShopId(iShopId);
		oCheckPayment.setOletId(iOutletId);
		oCheckPayment.setChksId(sCheckId);
		oCheckPayment.setPaymId(oPosPaymentMethod.getPaymId());
		oCheckPayment.setUserInputValue(BigDecimal.ZERO);
		for(int i=1; i<=5; i++) {
			oCheckPayment.setName(i, oPosPaymentMethod.getName(i));
			oCheckPayment.setShortName(i, oPosPaymentMethod.getShortName(i));
		}
		oCheckPayment.setPaymentType(oPosPaymentMethod.getPaymentType());
		oCheckPayment.setPayTotal(dFinalPayTotal);
		oCheckPayment.setPayTips(dFinalTipsTotal);
		oCheckPayment.setPayChange(dFinalChangeTotal);
		if(oPosPaymentMethod.getPayForeignCurrency().equals(PosPaymentMethod.PAY_FOREIGN_CURRENCY_AUTO_DETECT)) {
			if(oPosPaymentMethod.getCurrencyCode().isEmpty() || oPosPaymentMethod.getCurrencyCode().equals(AppGlobal.g_oFuncOutlet.get().getCurrencyCode()))
				oCheckPayment.setPayForeignCurrency(PosPaymentMethod.PAY_FOREIGN_CURRENCY_LOCAL);
			else
				oCheckPayment.setPayForeignCurrency(PosPaymentMethod.PAY_FOREIGN_CURRENCY_FOREIGN);
		}else
			oCheckPayment.setPayForeignCurrency(oPosPaymentMethod.getPayForeignCurrency());
		oCheckPayment.setPayForeignTotal(BigDecimal.ZERO);
		oCheckPayment.setPayForeignTips(BigDecimal.ZERO);
		oCheckPayment.setPayForeignChange(BigDecimal.ZERO);
		if(oPosPaymentMethod.getCurrencyCode().isEmpty())
			oCheckPayment.setCurrencyCode(AppGlobal.g_oFuncOutlet.get().getCurrencyCode());
		else
			oCheckPayment.setCurrencyCode(oPosPaymentMethod.getCurrencyCode());
		if(oPosPaymentMethod.isPayByForeignCurrency(AppGlobal.g_oFuncOutlet.get().getCurrencyCode())) {
			OutCurrency oCurrency = m_oCurrencyList.get(oPosPaymentMethod.getCurrencyCode());
			oCheckPayment.setCurrencyRate(new BigDecimal(oCurrency.getRate().toPlainString()));
		}else
			oCheckPayment.setCurrencyRate(BigDecimal.ZERO);
		oCheckPayment.setChangeBackCurrency(oPosPaymentMethod.getChangebackCurrency());
		oCheckPayment.setSpecial(oPosPaymentMethod.getSpecial());
		oCheckPayment.setNonRevenue(oPosPaymentMethod.getNonRevenue());
		oCheckPayment.setMealUserId(iEmployeeId);
		oCheckPayment.setMemberId(iMemberId);
		for(int i=1; i<=3; i++) {
			if(sRefData[(i-1)] != null)
				oCheckPayment.setRefData(i, sRefData[(i-1)]);
		}
		oCheckPayment.setPayLocTime(AppGlobal.getCurrentTime(false));
		oCheckPayment.setPayTime(formatter.print(AppGlobal.convertTimeToUTC(oCheckPayment.getPayLocTime())));
		if(iAppliedUserId == 0)
			oCheckPayment.setPayUserId(AppGlobal.g_oFuncUser.get().getUserId());
		else
			oCheckPayment.setPayUserId(iAppliedUserId);
		oCheckPayment.setPayStatId(AppGlobal.g_oFuncStation.get().getStationId());
		oCheckPayment.setVoidTime(null);
		oCheckPayment.setVoidLocTime(null);
		oCheckPayment.setVoidUserId(0);
		oCheckPayment.setVoidStatId(0);
		oCheckPayment.setVoidVdrsId(0);
		oCheckPayment.setStatus(PosCheckPayment.STATUS_ACTIVE);
		
		if(oPosPaymentMethod.getSurchargeRate().compareTo(BigDecimal.ZERO) > 0){
			BigDecimal dSurchargePercentage = oPosPaymentMethod.getSurchargeRate().divide(new BigDecimal(100));
			BigDecimal dSurchargeAmount = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dFinalPayTotal.add(dFinalTipsTotal).multiply(dSurchargePercentage));
			oCheckPayment.setSurcharge(dSurchargeAmount);
			m_dSurchargeTotal = m_dSurchargeTotal.add(dSurchargeAmount);
		}
		oCheckPayment.setForeignSurcharge(BigDecimal.ZERO);
		
		for(PosCheckExtraInfo oPosCheckExtraInfo:oExtraInfoList){
			oCheckPayment.addExtraInfoToList(new PosCheckExtraInfo(oPosCheckExtraInfo));
		}
		
		// load the authorization info. on the payment gateway transaction
		PosPaymentGatewayTransactions oPosPaymentGatewayTransactions = new PosPaymentGatewayTransactions();
		oPosPaymentGatewayTransactions.init();
		
		for(PosCheckExtraInfo oPosCheckExtraInfo:oExtraInfoList){
			String sTmpValue = oPosCheckExtraInfo.getValue();
			
			if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SPA_STANDARD_MASKED_PAN))
				oPosPaymentGatewayTransactions.setMaskedPan(sTmpValue);
			if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SPA_STANDARD_ISSUER))
				oPosPaymentGatewayTransactions.setIssuer(sTmpValue);
			if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SPA_STANDARD_TIPS)) {
				BigDecimal dTips = new BigDecimal(sTmpValue);
				oPosPaymentGatewayTransactions.setTips(dTips);
			}
			if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SPA_STANDARD_PARENT_AUTH_CODE))
				oPosPaymentGatewayTransactions.setParentAuthCode(sTmpValue);
			if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SPA_STANDARD_AUTH_CODE))
				oPosPaymentGatewayTransactions.setAuthCode(sTmpValue);
			if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SPA_STANDARD_TYPE_KEY))
				oPosPaymentGatewayTransactions.setType(sTmpValue);
			if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SPA_STANDARD_PAY_AMOUNT)) {
				BigDecimal dAmount = new BigDecimal(sTmpValue);
				oPosPaymentGatewayTransactions.setAmount(dAmount);
			}
			if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SPA_STANDARD_PGTX_PAY_ID)) {
				int iTmpValue = Integer.parseInt(sTmpValue);
				oPosPaymentGatewayTransactions.setPgtxPayId(iTmpValue);
			}
			if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SPA_STANDARD_REF_NUM))
				oPosPaymentGatewayTransactions.setRefNo(sTmpValue);
			if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SPA_STANDARD_TRACE_NUM))
				oPosPaymentGatewayTransactions.setTraceNo(sTmpValue);
			if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SPA_STANDARD_INVOICE_NUM))
				oPosPaymentGatewayTransactions.setInvoiceNo(sTmpValue);
			if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SPA_STANDARD_TOKEN))
				oPosPaymentGatewayTransactions.setToken(sTmpValue);
			if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERFACE_ID))
				oPosPaymentGatewayTransactions.setIntfId(Integer.valueOf(sTmpValue));
		}
		PosPaymentGatewayTransactionsList oPosPaymentGatewayTransactionsList = new PosPaymentGatewayTransactionsList();
		oPosPaymentGatewayTransactionsList.add(oPosPaymentGatewayTransactions);
		
		oCheckPayment.setPaymentGatewayTransaction(oPosPaymentGatewayTransactionsList);
		
		m_oPosCheckPaymentList.add(oCheckPayment);
		
		if(m_dCurrentBalance.compareTo(BigDecimal.ZERO) == 0)
			// Finish payment
			iRet = 2;
		else
			iRet = 1;
		
		return iRet;
	}

	// Assign old payment to check payment structure
	// Return value :	-1 - error occur
	//					1 - Add payment successfully and there is remain balance 
	public int addOldPayment(PosCheckPayment oOldCheckPayment) {
		int iRet;
		BigDecimal dFinalChangeTotal = BigDecimal.ZERO;
		m_sErrorMessage = "";
		
		// Get the payment method object
		PosPaymentMethod oPosPaymentMethod = m_oPosPaymentMethodList.getPaymentMethodList().get(oOldCheckPayment.getPaymentMethodId());
		if(oPosPaymentMethod !=null && !oPosPaymentMethod.isResidueTips())
			m_dResidueTotal = m_dResidueTotal.add(oOldCheckPayment.getPayTips());
		else
			m_dTipsTotal = m_dTipsTotal.add(oOldCheckPayment.getPayTips());
		m_dChangeTotal = dFinalChangeTotal;
		
		m_oPosCheckPaymentList.add(oOldCheckPayment);
		iRet = 1;
		
		return iRet;
	}
	
	public boolean editPaymentRef(int iPosCheckPaymentIndex, String[] sRefData) {
		if (iPosCheckPaymentIndex >= m_oPosCheckPaymentList.size()) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("payment_not_found");
			return false;
		}
		
		PosCheckPayment oCheckPayment = m_oPosCheckPaymentList.get(iPosCheckPaymentIndex);
		for (int i = 1; i <= sRefData.length; i++) {
			if (sRefData[(i-1)] != null && !sRefData[(i-1)].isEmpty())
				oCheckPayment.setRefData(i, sRefData[(i-1)]);
		}
		
		return true;
	}
	//bNegativeCalculation should only be true when the payment is 0 but should calculate as negative payment
	public int editPayment(int iPosCheckPaymentIndex, BigDecimal dPayTotal, BigDecimal dTipsTotal, BigDecimal dSurcharge, ArrayList<PosCheckExtraInfo> oExtraInfoList, boolean bNegativeCalculation) {
		int iRet;
		BigDecimal dFinalChangeTotal = BigDecimal.ZERO;
		BigDecimal dFinalPayTotal = BigDecimal.ZERO;
		BigDecimal dFinalTipsTotal = BigDecimal.ZERO;
		BigDecimal dFinalChangeTotalInForeignCurrency = BigDecimal.ZERO;
		BigDecimal dFinalPayTotalInForeignCurrency = BigDecimal.ZERO;
		BigDecimal dFinalTipsTotalInForeignCurrency = BigDecimal.ZERO;
		BigDecimal dPayTotalInForeignCurrency = BigDecimal.ZERO;
		BigDecimal dCurrentBalanceInForeignCurrency = BigDecimal.ZERO;
		BigDecimal dCurrentForeignCurrencyRate = null;
		BigDecimal dUserInputValue = new BigDecimal(dPayTotal.toPlainString());
		
		m_sErrorMessage = "";
		
		if(iPosCheckPaymentIndex >= m_oPosCheckPaymentList.size()){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("payment_not_found");
			return -1;
		}
		
		PosCheckPayment oCheckPayment = m_oPosCheckPaymentList.get(iPosCheckPaymentIndex);
		
		// Get the payment method object
		PosPaymentMethod oPosPaymentMethod = m_oPosPaymentMethodList.getPaymentMethodList().get(oCheckPayment.getPaymentMethodId());
		
		// Change the current balance and input value back to local currency
		OutCurrency oCurrency = null;
		if(oPosPaymentMethod.isPayByForeignCurrency(AppGlobal.g_oFuncOutlet.get().getCurrencyCode())) {
			oCurrency = m_oCurrencyList.get(oCheckPayment.getCurrencyCode());
			dCurrentBalanceInForeignCurrency = m_dCurrentBalance.multiply(oCurrency.getRate());
			dCurrentForeignCurrencyRate = oCurrency.getRate();
			dCurrentBalanceInForeignCurrency = oPosPaymentMethod.currencyRoundUpAmountWithCurrenctDecimalToBigDecimal(dCurrentBalanceInForeignCurrency);
	
			if(!oCheckPayment.isOldPayment()){
				dPayTotalInForeignCurrency = new BigDecimal(dPayTotal.toPlainString());
				dPayTotal = dPayTotalInForeignCurrency.divide(oCurrency.getRate(), 10, RoundingMode.HALF_UP);
			}
			dPayTotal = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dPayTotal);
		}
		if(oCheckPayment.isOldPayment()) {
			m_dCurrentBalance = m_dCurrentBalance.subtract(dPayTotal);
			dFinalTipsTotal = dTipsTotal;
			dFinalPayTotal = dPayTotal;
		}else if(dPayTotal.signum() != -1 && !bNegativeCalculation){
			//positive balance calculation
			// Check balance and calculate the tips or change
			if(dPayTotal.compareTo(m_dCurrentBalance) > 0){
				if((!oPosPaymentMethod.HaveTips() && !oPosPaymentMethod.isResidueTips()) || AppGlobal.g_oFuncStation.get().isPaymentSkipTips(oPosPaymentMethod.getPaymentCode())){
					// Remain is treated as change
					dFinalTipsTotal = dTipsTotal;
					dFinalChangeTotal = dPayTotal.subtract(m_dCurrentBalance);
					
					if(oPosPaymentMethod.isPayByForeignCurrency(AppGlobal.g_oFuncOutlet.get().getCurrencyCode())) {
						dFinalTipsTotalInForeignCurrency = dFinalTipsTotal.multiply(oCurrency.getRate());
						dCurrentForeignCurrencyRate = oCurrency.getRate();
						dFinalTipsTotalInForeignCurrency = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dFinalTipsTotalInForeignCurrency);
						
						if(oPosPaymentMethod.isChangeBackInForeignCurrency()) {
							dFinalChangeTotalInForeignCurrency = dFinalChangeTotal.multiply(oCurrency.getRate());
							dFinalChangeTotalInForeignCurrency = oPosPaymentMethod.currencyRoundAmountToBigDecimal(dFinalChangeTotalInForeignCurrency);
						}
					}
				}else{
					// Remain is treated as tips
					if(oPosPaymentMethod.isPayByForeignCurrency(AppGlobal.g_oFuncOutlet.get().getCurrencyCode())) {
						dFinalTipsTotalInForeignCurrency = new BigDecimal(dTipsTotal.toPlainString());
						dFinalTipsTotalInForeignCurrency = dFinalTipsTotalInForeignCurrency.add(dPayTotalInForeignCurrency.subtract(dCurrentBalanceInForeignCurrency));
						
						BigDecimal dUserTotalPayInLocalCurrency = (dUserInputValue.add(dTipsTotal)).divide(oCurrency.getRate(), 10, RoundingMode.HALF_UP);
						dUserTotalPayInLocalCurrency = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dUserTotalPayInLocalCurrency);
						dCurrentForeignCurrencyRate = oCurrency.getRate();
						dTipsTotal = dUserTotalPayInLocalCurrency.subtract(m_dCurrentBalance);
						dTipsTotal = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dTipsTotal);
						dFinalTipsTotal = dTipsTotal;
					}else {
						dFinalTipsTotal = dTipsTotal;
						dFinalTipsTotal = dFinalTipsTotal.add(dPayTotal.subtract(m_dCurrentBalance));
					}
				}
				
				dFinalPayTotal = m_dCurrentBalance;
				if(oPosPaymentMethod.isPayByForeignCurrency(AppGlobal.g_oFuncOutlet.get().getCurrencyCode())) {
					dFinalPayTotalInForeignCurrency = dCurrentBalanceInForeignCurrency;
					if(!oPosPaymentMethod.isChangeBackInForeignCurrency()) {
						dFinalPayTotalInForeignCurrency = new BigDecimal(dUserInputValue.toPlainString());
						dFinalPayTotalInForeignCurrency = oPosPaymentMethod.currencyRoundUpAmountWithCurrenctDecimalToBigDecimal(dFinalPayTotalInForeignCurrency);
					}
				}
				m_dCurrentBalance = BigDecimal.ZERO;
			}else{
				if(oPosPaymentMethod.isPayByForeignCurrency(AppGlobal.g_oFuncOutlet.get().getCurrencyCode())) {
					dCurrentBalanceInForeignCurrency = dCurrentBalanceInForeignCurrency.subtract(dPayTotalInForeignCurrency);
					dFinalTipsTotalInForeignCurrency = new BigDecimal(dTipsTotal.toPlainString());
					dFinalPayTotalInForeignCurrency = dPayTotalInForeignCurrency;
					
					dTipsTotal = dTipsTotal.divide(oCurrency.getRate(), 10, RoundingMode.HALF_UP);
					dCurrentForeignCurrencyRate = oCurrency.getRate();
					dTipsTotal = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dTipsTotal);
				}
				
				m_dCurrentBalance = m_dCurrentBalance.subtract(dPayTotal);
				dFinalTipsTotal = dTipsTotal;
				dFinalPayTotal = dPayTotal;
			}
		}else{
			//negative balance calculation
			// Check balance and calculate the tips or change
			if(dPayTotal.compareTo(m_dCurrentBalance) < 0){
				if((!oPosPaymentMethod.HaveTips() && !oPosPaymentMethod.isResidueTips()) || AppGlobal.g_oFuncStation.get().isPaymentSkipTips(oPosPaymentMethod.getPaymentCode())){
					// Remain is treated as change
					dFinalTipsTotal = dTipsTotal;
					dFinalChangeTotal = dPayTotal.subtract(m_dCurrentBalance);
					
					if(oPosPaymentMethod.isPayByForeignCurrency(AppGlobal.g_oFuncOutlet.get().getCurrencyCode())) {
						dFinalTipsTotalInForeignCurrency = dFinalTipsTotal.multiply(oCurrency.getRate());
						dFinalTipsTotalInForeignCurrency = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dFinalTipsTotalInForeignCurrency);
						dCurrentForeignCurrencyRate = oCurrency.getRate();
						if(oPosPaymentMethod.isChangeBackInForeignCurrency()) {
							dFinalChangeTotalInForeignCurrency = dFinalChangeTotal.multiply(oCurrency.getRate());
							dFinalChangeTotalInForeignCurrency = oPosPaymentMethod.currencyRoundAmountToBigDecimal(dFinalChangeTotalInForeignCurrency);
						}
					}
				}else{
					// Remain is treated as tips
					if(oPosPaymentMethod.isPayByForeignCurrency(AppGlobal.g_oFuncOutlet.get().getCurrencyCode())) {
						dFinalTipsTotalInForeignCurrency = new BigDecimal(dTipsTotal.toPlainString());
						dFinalTipsTotalInForeignCurrency = dFinalTipsTotalInForeignCurrency.add(dPayTotalInForeignCurrency.subtract(dCurrentBalanceInForeignCurrency));
						dCurrentForeignCurrencyRate = oCurrency.getRate();
						BigDecimal dUserTotalPayInLocalCurrency = (dUserInputValue.add(dTipsTotal)).divide(oCurrency.getRate(), 10, RoundingMode.HALF_UP);
						dUserTotalPayInLocalCurrency = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dUserTotalPayInLocalCurrency);
						dTipsTotal = dUserTotalPayInLocalCurrency.subtract(m_dCurrentBalance);
						dTipsTotal = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dTipsTotal);
						dFinalTipsTotal = dTipsTotal;
					}else {
						dFinalTipsTotal = dTipsTotal;
						dFinalTipsTotal = dFinalTipsTotal.add(dPayTotal.subtract(m_dCurrentBalance));
					}
				}
				
				dFinalPayTotal = m_dCurrentBalance;
				if(oPosPaymentMethod.isPayByForeignCurrency(AppGlobal.g_oFuncOutlet.get().getCurrencyCode())) {
					dFinalPayTotalInForeignCurrency = dCurrentBalanceInForeignCurrency;
					if(!oPosPaymentMethod.isChangeBackInForeignCurrency()) {
						dFinalPayTotalInForeignCurrency = new BigDecimal(dUserInputValue.toPlainString());
						dFinalPayTotalInForeignCurrency = oPosPaymentMethod.currencyRoundUpAmountWithCurrenctDecimalToBigDecimal(dFinalPayTotalInForeignCurrency);
					}
				}
				m_dCurrentBalance = BigDecimal.ZERO;
			}else{
				if(oPosPaymentMethod.isPayByForeignCurrency(AppGlobal.g_oFuncOutlet.get().getCurrencyCode())) {
					dCurrentBalanceInForeignCurrency = dCurrentBalanceInForeignCurrency.subtract(dPayTotalInForeignCurrency);
					dFinalTipsTotalInForeignCurrency = new BigDecimal(dTipsTotal.toPlainString());
					dFinalPayTotalInForeignCurrency = dPayTotalInForeignCurrency;
					
					dCurrentForeignCurrencyRate = oCurrency.getRate();
					
					dTipsTotal = dTipsTotal.divide(oCurrency.getRate(), 10, RoundingMode.HALF_UP);
					dTipsTotal = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dTipsTotal);
				}
				
				m_dCurrentBalance = m_dCurrentBalance.subtract(dPayTotal);
				dFinalTipsTotal = dTipsTotal;
				dFinalPayTotal = dPayTotal;
			}
		}
		
		if(oPosPaymentMethod.isResidueTips())
			m_dResidueTotal = m_dResidueTotal.add(dFinalTipsTotal); 
		else
			m_dTipsTotal = m_dTipsTotal.add(dFinalTipsTotal);
		m_dChangeTotal = dFinalChangeTotal;
		
		oCheckPayment.setPayTotal(dFinalPayTotal);
		oCheckPayment.setPayTips(dFinalTipsTotal);
		oCheckPayment.setPayChange(dFinalChangeTotal);
		oCheckPayment.setPayForeignTotal(dFinalPayTotalInForeignCurrency);
		oCheckPayment.setPayForeignTips(dFinalTipsTotalInForeignCurrency);
		oCheckPayment.setPayForeignChange(dFinalChangeTotalInForeignCurrency);
		oCheckPayment.setUserInputValue(dUserInputValue);
		
		if(dSurcharge != null && dSurcharge.compareTo(BigDecimal.ZERO) != 0){
			oCheckPayment.setSurcharge(dSurcharge);
			m_dSurchargeTotal = m_dSurchargeTotal.add(dSurcharge);
		}else if(oPosPaymentMethod.getSurchargeRate().compareTo(BigDecimal.ZERO) > 0){
			BigDecimal dSurchargePercentage = oPosPaymentMethod.getSurchargeRate().divide(new BigDecimal(100));
			BigDecimal dSurchargeAmount = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dFinalPayTotal.add(dFinalTipsTotal).multiply(dSurchargePercentage));
			oCheckPayment.setSurcharge(dSurchargeAmount);
			m_dSurchargeTotal = m_dSurchargeTotal.add(dSurchargeAmount);
			m_dTotalDue = m_dTotalDue.add(dSurchargeAmount);
		}
		if(dCurrentForeignCurrencyRate != null)
			oCheckPayment.setForeignSurcharge(oCheckPayment.getSurcharge().multiply(dCurrentForeignCurrencyRate));
		
		// Calculate the total due
		m_dTotalDue = m_dTotalDue.add(dFinalPayTotal).add(dFinalTipsTotal);
		
		// Add extra info if any
		if (oExtraInfoList != null) {
			for(PosCheckExtraInfo oPosCheckExtraInfo:oExtraInfoList){
				oCheckPayment.addExtraInfoToList(new PosCheckExtraInfo(oPosCheckExtraInfo));
			}
		}
		
		if(m_dCurrentBalance.compareTo(BigDecimal.ZERO) == 0)
			// Finish payment
			iRet = 2;
		else
			iRet = 1;
		
		return iRet;
	}
	
	public boolean checkPaymentMethodAuthority(PosPaymentMethod oPosPaymentMethod, FuncUser oFuncUser) {
		boolean bHaveAuth = false;
		if(oPosPaymentMethod.getPaymentUserGroupId() == PosPaymentMethod.PAYMENT_USER_GROUP_ALLOW_ALL || oFuncUser.getUserGroupList().contains(oPosPaymentMethod.getPaymentUserGroupId()))
			bHaveAuth = true;
			
		return bHaveAuth;
	}
	
	// Get payment final total
	public BigDecimal getPaymentFinalPayTotal(int iPaymentIndex){
		if(iPaymentIndex >= m_oPosCheckPaymentList.size())
			return BigDecimal.ZERO;
		
		PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iPaymentIndex);
		
		if(oPosCheckPayment.isPayByForeignCurrency())
			return oPosCheckPayment.getPayForeignTotal();
		else
			return oPosCheckPayment.getPayTotal();
	}
	
	// Get payment final tips
	public BigDecimal getPaymentFinalPayTips(int iPaymentIndex){
		if(iPaymentIndex >= m_oPosCheckPaymentList.size())
			return BigDecimal.ZERO;
		
		PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iPaymentIndex);
		
		if(oPosCheckPayment.isPayByForeignCurrency())
			return oPosCheckPayment.getPayForeignTips();
		else
			return oPosCheckPayment.getPayTips();
	}
	
	// Get payment final change
	public BigDecimal getPaymentFinalPayChange(int iPaymentIndex){
		if(iPaymentIndex >= m_oPosCheckPaymentList.size())
			return BigDecimal.ZERO;
		
		PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iPaymentIndex);
		
		if(oPosCheckPayment.isPayByForeignCurrency())
			return oPosCheckPayment.getPayForeignChange();
		else
			return oPosCheckPayment.getPayChange();
	}
	
	// Get payment user input value
	public BigDecimal getPaymentUserInputValue(int iPaymentIndex){
		if(iPaymentIndex >= m_oPosCheckPaymentList.size())
			return BigDecimal.ZERO;
		
		PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iPaymentIndex);
		
		return oPosCheckPayment.getUserInputValue();
	}
	
	//Get payment type
	public String getPaymentType(int iPaymentIndex){
		if(iPaymentIndex >= m_oPosCheckPaymentList.size())
			return null;
		
		PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iPaymentIndex);
		
		return oPosCheckPayment.getPaymentType();
	}
	
	// Get payment reference data by key name
	public String getPaymentRefDataByKey(int iPaymentIndex, int iRefDataIndex, String sKey){
		if(iPaymentIndex >= m_oPosCheckPaymentList.size())
			return null;
		
		PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iPaymentIndex);
		
		return oPosCheckPayment.getRefDataByIndexAndKey(iRefDataIndex, sKey);
	}
	
	//Get payment method name
	public String getPaymentMethodName(int iPaymentIndex, int iLangIndex){
		if(iPaymentIndex >= m_oPosCheckPaymentList.size())
			return null;
		
		PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iPaymentIndex);
		PosPaymentMethod oPosPaymentMethod = m_oPosPaymentMethodList.getPaymentMethodList().get(oPosCheckPayment.getPaymentMethodId());
		
		return oPosPaymentMethod.getName(iLangIndex);
	}
	
/** TenderApply ~ new method to get payment code */
	//get payment method code
	public String getPaymentMethodCode(int iPaymentIndex){
		if(iPaymentIndex >= m_oPosCheckPaymentList.size())
			return null;
		
		PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iPaymentIndex);
		PosPaymentMethod oPosPaymentMethod = m_oPosPaymentMethodList.getPaymentMethodList().get(oPosCheckPayment.getPaymentMethodId());
		
		return oPosPaymentMethod.getPaymentCode();
	}
	
	//check whether is old payment
	public boolean isOldPayment(int iPaymentIndex){
		if(iPaymentIndex >= m_oPosCheckPaymentList.size())
			return false;
		
		PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iPaymentIndex);
		if(oPosCheckPayment.isOldPayment())
			return true;
		else
			return false;
	}
	
	// Delete new add payment
	public void deletePayment(int iPaymentIndex){
		if(iPaymentIndex >= m_oPosCheckPaymentList.size())
			return;
		
		PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iPaymentIndex);
		PosPaymentMethod oPosPaymentMethod = m_oPosPaymentMethodList.getPaymentMethodList().get(oPosCheckPayment.getPaymentMethodId());
		
		if(oPosCheckPayment.isCouponPaymentType()) {
			FuncCoupon oRedeemCoupon = new FuncCoupon();
			JSONObject oPaymentRefData = null;
			
			//update coupon status
			try {
				oPaymentRefData = new JSONObject(oPosCheckPayment.getRefData(1));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (oPaymentRefData.has("coupon_number") && !oPaymentRefData.isNull("coupon_number")) {
				oRedeemCoupon.setStartCoupon(oPaymentRefData.optString("coupon_number"));
				oRedeemCoupon.setEndCoupon(oPaymentRefData.optString("coupon_number"));
				if(oPaymentRefData.has("redeem_item_code") && !oPaymentRefData.isNull("redeem_item_code"))
					oRedeemCoupon.setRedeemItem("");
				
				oRedeemCoupon.setCouponCurrentStatus(FuncCoupon.COUPON_STATUS_LOCKED);
				oRedeemCoupon.setCouponNextStatus(FuncCoupon.COUPON_STATUS_SOLD);
				oRedeemCoupon.updateCoupon(FuncCoupon.UPDATE_COUPON_TYPE_USE_AS_PAYMENT);
			}
		}
		
		m_dCurrentBalance = m_dCurrentBalance.add(oPosCheckPayment.getPayTotal());
		if(oPosPaymentMethod.isResidueTips())
			m_dResidueTotal = m_dResidueTotal.subtract(oPosCheckPayment.getPayTips());
		else
			m_dTipsTotal = m_dTipsTotal.subtract(oPosCheckPayment.getPayTips());
		
		// Remove payment from the list
		m_oPosCheckPaymentList.remove(iPaymentIndex);
	}
	
	public void addOldCheckPayment(PosCheckPayment oCheckPayment){
		m_oPosCheckPaymentList.add(oCheckPayment);
	}
	
	// Save check payment(s)
	public boolean saveMultipleCheckPayments(String sCheckPrefixNo, int iReceiptPrtqId, int iReceiptFormatId, boolean bOpenDrawerOnly) {
		int iShopId = 0, iOutletId = 0, iUserId = 0;
		String sBusinessDayId = "", sBusinessPeriodId = "";
		if(iReceiptPrtqId > 0){
			for(PosCheckPayment oCheckPayment:m_oPosCheckPaymentList){
				// Get the payment method object
				PosPaymentMethod oPosPaymentMethod = m_oPosPaymentMethodList.getPaymentMethodList().get(oCheckPayment.getPaymentMethodId());
				
				// Open drawer
				// Check open drawer
				if(oPosPaymentMethod.getOpenDrawer(1) > 0){
					// Drawer 1
					if(m_oOpenDrawerFlag.containsKey(1) == false){
						// Add drawer 1 (key) but not open yet (value: false) 
						m_oOpenDrawerFlag.put(1, false);
					}
				}
				if(oPosPaymentMethod.getOpenDrawer(2) > 0){
					// Drawer 2
					if(m_oOpenDrawerFlag.containsKey(2) == false){
						// Add drawer 2 (key) but not open yet (value: false) 
						m_oOpenDrawerFlag.put(2, false);
					}
				}
				
				iShopId = oCheckPayment.getShopId();
				iOutletId = oCheckPayment.getOutletId();
				sBusinessDayId = oCheckPayment.getBusinessDayId();
				sBusinessPeriodId = oCheckPayment.getBusinessPeriodId();
				iUserId = oCheckPayment.getPayUserId();
			}
			
			// Process open drawer
			// *****************************************************************
			// Create thread to open drawer
			AppThreadManager oAppThreadManager = new AppThreadManager();
					
			// Add the method to the thread manager
			// Thread 1 : Open drawer

			// Create parameter array
/** NoSale & DrawerOpen [saveMultipleCheckPayments] ~ thread add param (for consistency) */
			//Object[] oParameters = new Object[7];
			Object[] oParameters = new Object[8];
			oParameters[0] = iReceiptPrtqId;
			oParameters[1] = iShopId;
			oParameters[2] = iOutletId;
			oParameters[3] = sBusinessDayId;
			oParameters[4] = sBusinessPeriodId;
			oParameters[5] = iUserId;
			oParameters[6] = AppGlobal.g_oFuncStation.get().getStationId();
			oParameters[7] = true;	// "false" is changed, because it is from cashier, not no sale
			oAppThreadManager.addThread(1, this, "openDrawer", oParameters);
			
			// Run the thread without wait
			oAppThreadManager.runThread();
		}
		
		if(bOpenDrawerOnly)
			return true;
		
		PosCheckPayment oCheckPayment = new PosCheckPayment();
		oCheckPayment.setTimeZone(AppGlobal.g_oFuncOutlet.get().getTimeZone(), AppGlobal.g_oFuncOutlet.get().getTimeZoneName());
		return oCheckPayment.addUpdateWithMutlipleRecord(m_oPosCheckPaymentList, sCheckPrefixNo, AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncStation.get().getStationId());
	}
	
	// Get payment method list
	public PosPaymentMethodList getPaymentMethodList(){
		return m_oPosPaymentMethodList;
	}
	
	// Get payment method have tips or not
	public boolean getPaymentMethodHaveTips(int iPaymentIndex){
		if(iPaymentIndex >= m_oPosCheckPaymentList.size())
			return false;
		
		PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iPaymentIndex);
		
		if(m_oPosPaymentMethodList.getPaymentMethodList().containsKey(oPosCheckPayment.getPaymentMethodId()) == false){
			return false;
		}

		// Get the payment method object
		PosPaymentMethod oPosPaymentMethod = m_oPosPaymentMethodList.getPaymentMethodList().get(oPosCheckPayment.getPaymentMethodId());
		return oPosPaymentMethod.HaveTips();
	}
	
	// Get payment list
	public ArrayList<PosCheckPayment> getCheckPaymentList(){
		return m_oPosCheckPaymentList;
	}
	
	// Get payment list count
	public int getCheckPaymentListCount() {
		return m_oPosCheckPaymentList.size();
	}
	
	// Get paid balance
	public BigDecimal getPaidBalance() {
		BigDecimal dPaidBalance = BigDecimal.ZERO;
		if (!m_oPosCheckPaymentList.isEmpty()) {
			for(PosCheckPayment oCheckPayment:m_oPosCheckPaymentList)
				dPaidBalance = dPaidBalance.add(oCheckPayment.getPayTotal());
		}
		
		return dPaidBalance;
	}
	
	public void setCurrentBalance(BigDecimal dBalance) {
		m_dCurrentBalance = dBalance;
	}
	
	public void setSurcharge(BigDecimal dSurcharge) {
		this.m_dSurchargeTotal = dSurcharge;
	}
	
	public void setTotalDue(BigDecimal dTotalDue) {
		this.m_dTotalDue = dTotalDue;
	}
	
	// Get current balance
	public BigDecimal getCurrentBalance(){
		return m_dCurrentBalance;
	}
	
	// Get current balance
	public BigDecimal getSurchargeTotal(){
		return m_dSurchargeTotal;
	}
	
	// Get current balance
	public BigDecimal getTotalDue(){
		return m_dTotalDue;
	}
	
	// Get current balance in foreign currency
	public BigDecimal getCurrenctBalanceInForeignCurrency(PosPaymentMethod oPosPaymentMethod) {
		String sCurrencyCode = oPosPaymentMethod.getCurrencyCode();
		OutCurrency oCurrency = m_oCurrencyList.get(sCurrencyCode);
		BigDecimal dCurrentBalanceInForeignCurrency = BigDecimal.ZERO;
		BigDecimal dForeignCurrencyRate = new BigDecimal(oCurrency.getRate().toPlainString());
		
		dCurrentBalanceInForeignCurrency = m_dCurrentBalance.multiply(dForeignCurrencyRate);
		dCurrentBalanceInForeignCurrency = oPosPaymentMethod.currencyRoundUpAmountWithCurrenctDecimalToBigDecimal(dCurrentBalanceInForeignCurrency);
		
		return dCurrentBalanceInForeignCurrency;
	}
	
	// Get tips total
	public BigDecimal getTipsTotal(){
		return m_dTipsTotal;
	}
	
	// Get residue total
	public BigDecimal getResidueTotal(){
		return m_dResidueTotal;
	}
	
	// Get change total
	public BigDecimal getChangeTotal(){
		int iLastPaymentIndex = m_oPosCheckPaymentList.size() - 1;
		if(iLastPaymentIndex >= 0) {
			PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iLastPaymentIndex);
			if(oPosCheckPayment.isPayByForeignCurrency() && oPosCheckPayment.isChangeBackInForeignCurrency())
				return oPosCheckPayment.getPayForeignChange();
		}
		
		return m_dChangeTotal;
	}
	
	// Get existing extra info list
	public ArrayList<PosCheckExtraInfo> getExtraInfoList() {
		return m_oExtraInfoList;
	}
	
	// Manually turn on open drawer flag
	public void setOpenDrawer(int iDrawerIndex){
		if(iDrawerIndex == 1){
			// Drawer 1
			if(m_oOpenDrawerFlag.containsKey(1) == false){
				// Add drawer 1 (key) but not open yet (value: false) 
				m_oOpenDrawerFlag.put(1, false);
			}
		}
		if(iDrawerIndex == 2){
			// Drawer 2
			if(m_oOpenDrawerFlag.containsKey(2) == false){
				// Add drawer 2 (key) but not open yet (value: false) 
				m_oOpenDrawerFlag.put(2, false);
			}
		}
	}
	
	// Process open drawer
// NoSale & DrawerOpen [openDrawer] ~ add 1 new param
	public void openDrawer(int iTargetPrtqId, int iShopId, int iOutletId, String sBusinessDayId, String sBusinessPeriodId, int iUserId, int iStationId, boolean bIsFromCashier){
		PosPaymentMethod oPaymentMethod = new PosPaymentMethod();
		oPaymentMethod.setTimezone(AppGlobal.g_oFuncOutlet.get().getTimeZone(), AppGlobal.g_oFuncOutlet.get().getTimeZoneName());
		String sOpenDrawerName = new String();
		
		String sSuccessOpenDrawer = new String();
		boolean bUserAllowToOpenDrawerOne = false;
		boolean bUserAllowToOpenDrawerTwo = false;
		if (AppGlobal.g_oFuncStation.get().getCheckDrawerOwnership()) {
			PosUserDrawerOwnershipList oPosUserDrawerOwnershipList = new PosUserDrawerOwnershipList();
			oPosUserDrawerOwnershipList.findAllByConditions(AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncStation.get().getStationId(), 0);
			 List<PosUserDrawerOwnership> m_oUserDrawerOwnershipList = oPosUserDrawerOwnershipList.getPosUserDrawerOwnership();
			for (PosUserDrawerOwnership oPosUserDrawerOwnership : m_oUserDrawerOwnershipList) {
				if (oPosUserDrawerOwnership.getShopId() == AppGlobal.g_oFuncOutlet.get().getShopId()
						&& oPosUserDrawerOwnership.getOletId() == AppGlobal.g_oFuncOutlet.get().getOutletId()
						&& oPosUserDrawerOwnership.getStatId() == AppGlobal.g_oFuncStation.get().getStationId()
						&& oPosUserDrawerOwnership.getUserId() == AppGlobal.g_oFuncUser.get().getUserId()) {
					if (oPosUserDrawerOwnership.getDrawer() == 1)
						bUserAllowToOpenDrawerOne = true;
					else if (oPosUserDrawerOwnership.getDrawer() == 2)
						bUserAllowToOpenDrawerTwo = true;
				}
			}
		}
		
		// Drawer 1
		if (m_oOpenDrawerFlag.containsKey(1)) {
			Boolean bIsOpen = m_oOpenDrawerFlag.get(1);
			sOpenDrawerName = "OpenDrawer1";
			
			if (bIsOpen == false) {
				if (!AppGlobal.g_oFuncStation.get().getCheckDrawerOwnership()
						|| (AppGlobal.g_oFuncStation.get().getCheckDrawerOwnership() && bUserAllowToOpenDrawerOne)) {
					
					// Drawer 1 is not opened yet
					oPaymentMethod.openDrawer(sBusinessDayId, iShopId, iOutletId, iUserId, iStationId, iTargetPrtqId,
							"OpenDrawer1");
					m_oOpenDrawerFlag.put(1, true);
					sSuccessOpenDrawer = PosActionLog.ACTION_RESULT_SUCCESS;
				} else {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), null);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("user_not_allow_to_open_drawer_one"));
					oFormDialogBox.show();
					oFormDialogBox = null;
					sSuccessOpenDrawer = PosActionLog.ACTION_RESULT_REJECTED;
				}
			}
			// create action log
			AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.open_first_drawer.name(),
					sSuccessOpenDrawer, "", iUserId, iShopId, iOutletId, sBusinessDayId,
					sBusinessPeriodId, iStationId, "", "", "", "", "", sOpenDrawerName);
		
		}
		// Drawer 2
		if (m_oOpenDrawerFlag.containsKey(2)) {
			Boolean bIsOpen = m_oOpenDrawerFlag.get(2);
			sOpenDrawerName = "OpenDrawer2";
			
			if (bIsOpen == false) {
				if (!AppGlobal.g_oFuncStation.get().getCheckDrawerOwnership()
						|| (AppGlobal.g_oFuncStation.get().getCheckDrawerOwnership() && bUserAllowToOpenDrawerTwo)) {
					// Drawer 2 is not opened yet
					oPaymentMethod.openDrawer(sBusinessDayId, iShopId, iOutletId, iUserId, iStationId, iTargetPrtqId,
							"OpenDrawer2");
					m_oOpenDrawerFlag.put(2, true);
					sSuccessOpenDrawer = PosActionLog.ACTION_RESULT_SUCCESS;
				} else {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), null);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("user_not_allow_to_open_drawer_two"));
					oFormDialogBox.show();
					oFormDialogBox = null;
					sSuccessOpenDrawer = PosActionLog.ACTION_RESULT_REJECTED;
				}
			}
			// create action log
			AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.open_second_drawer.name(),
					sSuccessOpenDrawer, "", iUserId, iShopId, iOutletId, sBusinessDayId,
					sBusinessPeriodId, iStationId, "", "", "", "", "", sOpenDrawerName);
		}
		
// NoSale & DrawerOpen [openDrawer]
		if(sSuccessOpenDrawer.equals(PosActionLog.ACTION_RESULT_SUCCESS)) {
			// surveillance eConnect terminal event: NoSale & DrawerOpen
			HashMap<String, String> oSurveillanceEventInfo = new HashMap<String, String>();
			String sEventType = (bIsFromCashier) ? FuncSurveillance.SURVEILLANCE_TYPE_DRAWER_OPEN : FuncSurveillance.SURVEILLANCE_TYPE_NO_SALE;
			this.doSurveillanceEvent(sEventType, oSurveillanceEventInfo);
			/*
			List<PosInterfaceConfig> oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_SURVEILLANCE_INTERFACE);
			if (!oInterfaceConfigList.isEmpty()) {
				for (PosInterfaceConfig oPosInterfaceConfig : oInterfaceConfigList) {
					if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ECONNECT)) {
						FuncSurveillance oFuncSurveillance = new FuncSurveillance(oPosInterfaceConfig);
						String sEventType = (bIsFromCashier) ? FuncSurveillance.SURVEILLANCE_TYPE_DRAWER_OPEN : FuncSurveillance.SURVEILLANCE_TYPE_NO_SALE;
						HashMap<String, String> oSurveillanceEventInfo = new HashMap<String, String>();
						oSurveillanceEventInfo.put("eventType", sEventType);
						oFuncSurveillance.surveillanceEvent(oSurveillanceEventInfo);
					}
				}
			}
			*/
		}
		
		//handle action log
		AppGlobal.g_oActionLog.get().handleActionLog(false);
	}
	
	//payment time
	public void setPaymentDateTime(DateTime oPaymentDateTime) {
		DateTimeFormatter oPaymentFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		for(PosCheckPayment oCheckPayment:m_oPosCheckPaymentList) {
			if(oCheckPayment.isDelete() || oCheckPayment.isOldPayment())
				continue;
			
			oCheckPayment.setPayLocTime(oPaymentDateTime);
			oCheckPayment.setPayTime(oPaymentFormatter.print(AppGlobal.convertTimeToUTC(oCheckPayment.getPayLocTime())));
		}
	}
	
	// Check if the check is non revenue or not
	public String getNonRevenue() {
		if (!AppGlobal.g_oFuncStation.get().isAllowMixedPayment()) {
			for (PosCheckPayment oCheckPayment : m_oPosCheckPaymentList) {
				return oCheckPayment.getNonRevenue();
			}
		} else {
			if (m_oPosCheckPaymentList.size() > 0) {
				for (PosCheckPayment oCheckPayment : m_oPosCheckPaymentList) {
					if (oCheckPayment.getNonRevenue().equals(PosCheckPayment.NON_REVENUE_NO))
						return PosCheckPayment.NON_REVENUE_NO;
				}
				return PosCheckPayment.NON_REVENUE_YES;
			}
		}
		return PosCheckPayment.NON_REVENUE_NO;
	}
	
	// Set Taiwan GUI reference number
	public void setTaiwanGuiRefNum(String sTaiwanGuiRefNum) {
		m_sTaiwanGUIRefNum = sTaiwanGuiRefNum;
	}
	
	// Get Taiwan GUI reference number
	public String getTaiwanGuiRefNum() {
		return m_sTaiwanGUIRefNum;
	}
	
	// Set Taiwan GUI skip transaction number
	public void setTaiwanGuiSkipTransNum(int iTaiwanGuiTransNum) {
		m_iTaiwanGUISkipTransNum = iTaiwanGuiTransNum;
	}
	
	// Get Taiwan GUI skip transaction number
	public int getTaiwanGuiSkipTransNum() {
		return m_iTaiwanGUISkipTransNum;
	}

	// Set Taiwan GUI carrier
	public void setTaiwanGuiCarrier(String sTaiwanGuiCarrier) {
		m_sTaiwanGUICarrier = sTaiwanGuiCarrier;
	}
	
	// Get Taiwan GUI carrier
	public String getTaiwanGuiCarrier() {
		return m_sTaiwanGUICarrier;
	}
	
	// Check whether last payment is change back in foreign currency
	public boolean isChangesInForeignCurrency() {
		int iLastPaymentIndex = m_oPosCheckPaymentList.size() - 1;
		if(iLastPaymentIndex >= 0) {
			PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iLastPaymentIndex);
			if(oPosCheckPayment.isPayByForeignCurrency() && oPosCheckPayment.isChangeBackInForeignCurrency())
				return true;
		}
		
		return false;
	}
	
	public String[] getChangesForeignCurrencyName() {
		String[] sName = StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, "");
		int iLastPaymentIndex = m_oPosCheckPaymentList.size() - 1;
		if(iLastPaymentIndex >= 0) {
			PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iLastPaymentIndex);
			if(oPosCheckPayment.isPayByForeignCurrency() && oPosCheckPayment.isChangeBackInForeignCurrency())
				sName = m_oCurrencyList.get(oPosCheckPayment.getCurrencyCode()).getName();
		}
		
		return sName;
	}
	
	public String getChangesForeignCurrencySign() {
		String sSign = "";
		int iLastPaymentIndex = m_oPosCheckPaymentList.size() - 1;
		if(iLastPaymentIndex >= 0) {
			PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iLastPaymentIndex);
			if(oPosCheckPayment.isPayByForeignCurrency() && oPosCheckPayment.isChangeBackInForeignCurrency())
				sSign = m_oCurrencyList.get(oPosCheckPayment.getCurrencyCode()).getSign();
		}
		
		return sSign;
	}
	
	// Add currency to list
	public boolean addCurrencyToList(String sCurrencyCode) {
		if(m_oCurrencyList.containsKey(sCurrencyCode))
			return true;
		
		OutCurrency oOutCurrency = new OutCurrency();
		if(oOutCurrency.readActiveByShopOutletIdAndCode(AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), sCurrencyCode) == false) {
			return false;
		}else
			m_oCurrencyList.put(sCurrencyCode, oOutCurrency);
		
		return true;
	}
	
	// get currency rate
	public OutCurrency getCurrency(String sCurrencyCode) {
		return m_oCurrencyList.get(sCurrencyCode);
	}
	
	// whether have new payment exist
	public boolean isNewPaymentExist() {
		for(PosCheckPayment oCheckPayment : m_oPosCheckPaymentList) {
			if(!oCheckPayment.isOldPayment())
				return true;
		}
		
		return false;
	}
	
	// whether have new payment exist
	public BigDecimal getAllPaymentTotal() {
		BigDecimal oTotal = BigDecimal.ZERO;
		for(PosCheckPayment oCheckPayment : m_oPosCheckPaymentList)
			oTotal = oTotal.add(oCheckPayment.getPayTotal());
		
		return oTotal;
	}
	
	// Get tips total
	public void setTipsTotal(BigDecimal dTipsTotal){
		this.m_dTipsTotal = dTipsTotal;
	}
	
	public JSONObject getPaymentTotalByShopAndOutletAndUser(int iShopId, int iOutletId, int iEmployeeId, String sPaymentType, ArrayList<String> oBusinessDayIdList, boolean bOnlyEmployeeLimit) {
		JSONObject oResponseJSONObject = new JSONObject(), oTempJSONObject = new JSONObject(), requestJSONObject = new JSONObject();
		JSONArray oBDayIdJSONArray = new JSONArray();
		try {
			requestJSONObject.put("shopId", iShopId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("employeeId", iEmployeeId);
			requestJSONObject.put("paymentType", sPaymentType);
			for(String sBdayId:oBusinessDayIdList) {
				oTempJSONObject = new JSONObject();
				oTempJSONObject.put("businessDayId", sBdayId);
				oBDayIdJSONArray.put(oTempJSONObject);
			}
			requestJSONObject.put("businessDayIds", oBDayIdJSONArray);
			requestJSONObject.put("onlyEmployeeLimit", bOnlyEmployeeLimit);
			
			oResponseJSONObject = this.readDataFromApi("gm", "pos", "getCheckPaymentsByOutletIdTypeEmployeeAndBusinessDayIds", requestJSONObject.toString());
		}catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		return oResponseJSONObject;
	}
	
	//get check payment by payment index
	public PosCheckPayment getCheckPaymentByPaymentIndex(int iPaymentIndex){
		PosCheckPayment oCheckPayment = null;
		if(iPaymentIndex >= m_oPosCheckPaymentList.size())
			return oCheckPayment;
		oCheckPayment = m_oPosCheckPaymentList.get(iPaymentIndex);
		return oCheckPayment;
	}
	
	//get rounding method of the payment method
	public String getPaymentRoundingMethod(int iPaymentIndex){
		if(iPaymentIndex >= m_oPosCheckPaymentList.size())
			return "";
		
		PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iPaymentIndex);
		PosPaymentMethod oPosPaymentMethod = null;
		if(m_oPosPaymentMethodList.getPaymentMethodList().containsKey(oPosCheckPayment.getPaymentMethodId()))
			oPosPaymentMethod = m_oPosPaymentMethodList.getPaymentMethodList().get(oPosCheckPayment.getPaymentMethodId());
		return oPosPaymentMethod.getRoundingMethod();
	}
	
	public int getPaymentRoundDecimal(int iPaymentIndex){
		int iRoundDecimal = -1;
		if(iPaymentIndex >= m_oPosCheckPaymentList.size())
			return iRoundDecimal;
		PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iPaymentIndex);
		if(oPosCheckPayment == null)
			return iRoundDecimal;
		if(m_oPosPaymentMethodList.getPaymentMethodList().containsKey(oPosCheckPayment.getPaymentMethodId())){
			PosPaymentMethod oPosPaymentMethod = m_oPosPaymentMethodList.getPaymentMethodList().get(oPosCheckPayment.getPaymentMethodId());
			iRoundDecimal = oPosPaymentMethod.getRoundingDecimal();
		}
		return iRoundDecimal;
	}
	
	public int getDummyPaymentMappingId(int iPaymentIndex){
		int iDummyId = 0;
		if(iPaymentIndex >= m_oPosCheckPaymentList.size())
			return iDummyId;
		PosCheckPayment oPosCheckPayment = null;
		oPosCheckPayment = m_oPosCheckPaymentList.get(iPaymentIndex);
		
		if(oPosCheckPayment == null)
			return iDummyId;
			
		JSONArray oDummyArray = AppGlobal.g_oFuncStation.get().getDummyPaymentMapping();
		if(oDummyArray == null)
			return iDummyId;
		else{
			for(int i = 0; i < oDummyArray.length(); i++){
				JSONObject oMappingJSONObject = oDummyArray.optJSONObject(i);
				if (oMappingJSONObject == null)
					continue;
				if(oMappingJSONObject.optString("paym_id").equals( Integer.toString(oPosCheckPayment.getPaymentMethodId()))){
					iDummyId = Integer.parseInt(oMappingJSONObject.optString("dummy_paym_id"));
					break;
				}
			}
		}
		
		return iDummyId;
	}
	
	public static BigDecimal roundPaymentAmountToBigDecimal(BigDecimal dAmount, String sRoundMethod, int iRoundDecimal){
		//if amount is negative, change to positive to calculate
		//change result back to negative
		if(dAmount.signum() == -1)
			return Util.HERORound(dAmount.negate(), sRoundMethod, iRoundDecimal).negate();
		else
			return Util.HERORound(dAmount, sRoundMethod, iRoundDecimal);
	}
	
	//read data list from API
	private JSONObject readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONObject oResponseJSONObject = new JSONObject();
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, true))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			oResponseJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse();
		}
		return oResponseJSONObject;
	}
	
	// is support surcharge
	public boolean isSupportSurcharge() {
		return this.m_bSupportSurcharge;
	}
	
	// set next dummy payment
	public void setIsNextDummyPayment(boolean bIsNextDummy){
		m_bIsNextDummyPayment = bIsNextDummy;
	}
	
	// get next dummy payment
	public boolean isNextDummyPayment(){
		return this.m_bIsNextDummyPayment;
	}
	
	private void doSurveillanceEvent(String sEventType, HashMap<String, String> oSurveillanceEventInfo) {
		List<PosInterfaceConfig> oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_SURVEILLANCE_INTERFACE);
		if (!oInterfaceConfigList.isEmpty()) {
			for (PosInterfaceConfig oPosInterfaceConfig : oInterfaceConfigList) {
				if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ECONNECT)) {
					FuncSurveillance oFuncSurveillance = new FuncSurveillance(oPosInterfaceConfig);
					oSurveillanceEventInfo.put("eventType", sEventType);
					oFuncSurveillance.surveillanceEvent(oSurveillanceEventInfo, null);
				}
			}
		}
	}
	
	public String releaseVoucherPayment(PosCheckPayment oCheckPayment) {
		String sErrorMessage = "";
		// Void galaxy payment
		if(oCheckPayment.haveVoucherPayment()) {
			int iVoucherIntfId = oCheckPayment.getVoucherInterfaceId();
			if(iVoucherIntfId > 0) {
				PosInterfaceConfig oVoucherInterface = null;
				List<PosInterfaceConfig> oInterfaceConfigs = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_VOUCHER_INTERFACE);
				for(PosInterfaceConfig oVoucherInterfaceConfig : oInterfaceConfigs) {
					if(oVoucherInterfaceConfig.getInterfaceId() == iVoucherIntfId) {
						oVoucherInterface = oVoucherInterfaceConfig;
						break;
					}
				}
				
				if(oVoucherInterface != null && 
						oVoucherInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GALAXY)) {
					if(oCheckPayment.getPayTotal().compareTo(BigDecimal.ZERO) < 0) {
						sErrorMessage =  AppGlobal.g_oLang.get()._("cannot_void_negative_check_payment");
						return sErrorMessage;
					}
					FuncVoucherInterface oFuncVoucherInterface = new FuncVoucherInterface(oVoucherInterface);
					if(!oFuncVoucherInterface.voucherVoidPosting(oCheckPayment, null, false)){
						sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_void_galaxy_payment");
						if(oFuncVoucherInterface.getLastErrorMessage() != null && !oFuncVoucherInterface.getLastErrorMessage().isEmpty())
							sErrorMessage += ": " + oFuncVoucherInterface.getLastErrorMessage();	
					}
				}
			}
		}
		return sErrorMessage;
	}
}
