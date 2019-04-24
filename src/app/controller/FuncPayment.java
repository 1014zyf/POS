package app.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;
import app.AppThreadManager;

import app.model.*;

public class FuncPayment {
	
	// Check total
	private BigDecimal m_dCheckTotal;
	private BigDecimal m_dCurrentBalance;
	private BigDecimal m_dTipsTotal;
	private BigDecimal m_dChangeTotal;
	
	// Rounding method
	private String m_sCheckRoundMethod;
	private String m_sPayRoundMethod;
	private Integer m_iCheckRoundDecimal;
	private Integer m_iPayRoundDecimal;
	
	// Check payment list
	private ArrayList<PosCheckPayment> m_oPosCheckPaymentList;
	
	// Open drawer flag
	private HashMap<Integer, Boolean> m_oOpenDrawerFlag;
	
	// Payment method list
	private PosPaymentMethodList m_oPosPaymentMethodList;
	
	// Currency list
	private HashMap<String, OutCurrency> m_oCurrencyList;
	
	// Extra info list
	private ArrayList<PosCheckExtraInfo> m_oExtraInfoList;
	
	// Taiwan GUI information
	private String m_sTaiwanGUIType;
	private String m_sTaiwanGUIRefNum;
	private int m_iTaiwanGUISkipTransNum;
	
	// Last error message
	private String m_sErrorMessage;
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage;
	}
	
	public FuncPayment() {
		m_oCurrencyList = new HashMap<String, OutCurrency>();
		m_oPosCheckPaymentList = new ArrayList<PosCheckPayment>();
		m_oOpenDrawerFlag = new HashMap<Integer, Boolean>();
		m_oExtraInfoList = new ArrayList<PosCheckExtraInfo>();
	}

	public void readAllPaymentMethod(int shopId, int outletId, String sBusinessDay, boolean bIsHoliday, boolean bIsDayBeforeHoliday, boolean bIsSpecialDay, boolean bIsDayBeforeSpecialDay, int iWeekday){
		// Load payment method from OM
		m_oPosPaymentMethodList = new PosPaymentMethodList();
		m_oPosPaymentMethodList.readAllWithAccessControl(shopId, outletId, sBusinessDay, bIsHoliday, bIsDayBeforeHoliday, bIsSpecialDay, bIsDayBeforeSpecialDay, iWeekday);
	}
	
	// Init the structure for payment process
	public void init(BigDecimal dCheckTotal, String sCheckRoundMethod, int iCheckRoundDecimal, String sPayRoundMethod, int iPayRoundDecimal){
		m_dCheckTotal = dCheckTotal;
		m_dCurrentBalance = dCheckTotal;
		m_dTipsTotal = BigDecimal.ZERO;
		m_dChangeTotal = BigDecimal.ZERO;
		m_oCurrencyList.clear();
		m_oPosCheckPaymentList.clear();
		m_oOpenDrawerFlag.clear();
		m_sCheckRoundMethod = sCheckRoundMethod;
		m_iCheckRoundDecimal = iCheckRoundDecimal;
		m_sPayRoundMethod = sPayRoundMethod;
		m_iPayRoundDecimal = iPayRoundDecimal;
		m_sTaiwanGUIType = PosTaiwanGuiTran.TYPE_NORMAL;
		m_sTaiwanGUIRefNum = "";
		m_iTaiwanGUISkipTransNum = 0;
	}
	
	// Retrieve payment method and assign to check payment structure
	// Return value :	-1 - error occur
	//					1 - Add payment successfully and there is remain balance
	//					2 - Add payment successfully and this should be the last payment 
	public int addPayment(int iPaymentMethodId, int iShopId, int iOutletId, int iCheckId, PosBusinessDay oBusinessDay, int iBperId, BigDecimal dPayTotal, BigDecimal dTipsTotal, int iEmployeeId, int iMemberId, String[] sRefData, ArrayList<PosCheckExtraInfo> oExtraInfoList) {
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

		// Check balance and calculate the tips or change
		if(dPayTotal.compareTo(m_dCurrentBalance) > 0){
			if(!oPosPaymentMethod.HaveTips()){
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
		
		m_dTipsTotal = m_dTipsTotal.add(dFinalTipsTotal);
		m_dChangeTotal = dFinalChangeTotal;
		
		// Assign value to oCheckPayment
		PosCheckPayment oCheckPayment = new PosCheckPayment();
		oCheckPayment.setBdayId(oBusinessDay.getBdayId());
		oCheckPayment.setBperId(iBperId);
		oCheckPayment.setShopId(iShopId);
		oCheckPayment.setOletId(iOutletId);
		oCheckPayment.setChksId(iCheckId);
		oCheckPayment.setPaymId(oPosPaymentMethod.getPaymId());
		for(int i=1; i<=5; i++) {
			oCheckPayment.setName(i, oPosPaymentMethod.getName(i));
			oCheckPayment.setShortName(i, oPosPaymentMethod.getShortName(i));
		}
		oCheckPayment.setPaymentType(oPosPaymentMethod.getPaymentType());
		oCheckPayment.setPayTotal(dFinalPayTotal);
		oCheckPayment.setPayTips(dFinalTipsTotal);
		oCheckPayment.setPayChange(dFinalChangeTotal);
		oCheckPayment.setPayForeignCurrency(oPosPaymentMethod.getPayForeignCurrency());
		oCheckPayment.setPayForeignTotal(BigDecimal.ZERO);
		oCheckPayment.setPayForeignTips(BigDecimal.ZERO);
		oCheckPayment.setPayForeignChange(BigDecimal.ZERO);
		oCheckPayment.setCurrencyCode(oPosPaymentMethod.getCurrencyCode());
		if(oPosPaymentMethod.isPayByForeignCurrency()) {
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
		oCheckPayment.setPayLocTime(new DateTime());
		oCheckPayment.setPayTime(formatter.print(oCheckPayment.getPayLocTime().withZone(DateTimeZone.UTC)));
		oCheckPayment.setPayUserId(AppGlobal.g_oFuncUser.get().getUserId());
		oCheckPayment.setPayStatId(AppGlobal.g_oFuncStation.get().getStationId());
		oCheckPayment.setVoidTime(null);
		oCheckPayment.setVoidLocTime(null);
		oCheckPayment.setVoidUserId(0);
		oCheckPayment.setVoidStatId(0);
		oCheckPayment.setVoidVdrsId(0);
		oCheckPayment.setStatus(PosCheckPayment.STATUS_ACTIVE);
		
		for(PosCheckExtraInfo oPosCheckExtraInfo:oExtraInfoList){
			oCheckPayment.addExtraInfoToList(new PosCheckExtraInfo(oPosCheckExtraInfo));
		}
		
		m_oPosCheckPaymentList.add(oCheckPayment);
		
		if(m_dCurrentBalance.compareTo(BigDecimal.ZERO) == 0)
			// Finish payment
			iRet = 2;
		else
			iRet = 1;
		
		return iRet;
	}
	
	public int editPayment(int iPosCheckPaymentIndex, BigDecimal dPayTotal, BigDecimal dTipsTotal) {
		int iRet;
		BigDecimal dFinalChangeTotal = BigDecimal.ZERO;
		BigDecimal dFinalPayTotal = BigDecimal.ZERO;
		BigDecimal dFinalTipsTotal = BigDecimal.ZERO;
		BigDecimal dFinalChangeTotalInForeignCurrency = BigDecimal.ZERO;
		BigDecimal dFinalPayTotalInForeignCurrency = BigDecimal.ZERO;
		BigDecimal dFinalTipsTotalInForeignCurrency = BigDecimal.ZERO;
		BigDecimal dPayTotalInForeignCurrency = BigDecimal.ZERO;
		BigDecimal dCurrentBalanceInForeignCurrency = BigDecimal.ZERO;
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
		if(oPosPaymentMethod.isPayByForeignCurrency()) {
			oCurrency = m_oCurrencyList.get(oCheckPayment.getCurrencyCode());
			dCurrentBalanceInForeignCurrency = m_dCurrentBalance.divide(oCurrency.getRate(), 10, RoundingMode.HALF_UP);
			dCurrentBalanceInForeignCurrency = oPosPaymentMethod.currencyRoundUpAmountWithCurrenctDecimalToBigDecimal(dCurrentBalanceInForeignCurrency);
			
			dPayTotalInForeignCurrency = new BigDecimal(dPayTotal.toPlainString());
			dPayTotal = dPayTotalInForeignCurrency.multiply(oCurrency.getRate());
			dPayTotal = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dPayTotal);
		}
		
		// Check balance and calculate the tips or change
		if(dPayTotal.compareTo(m_dCurrentBalance) > 0){
			if(!oPosPaymentMethod.HaveTips()){
				// Remain is treated as change
				dFinalTipsTotal = dTipsTotal;
				dFinalChangeTotal = dPayTotal.subtract(m_dCurrentBalance);
				
				if(oPosPaymentMethod.isPayByForeignCurrency()) {
					dFinalTipsTotalInForeignCurrency = dFinalTipsTotal.divide(oCurrency.getRate(), 10, RoundingMode.HALF_UP);
					dFinalTipsTotalInForeignCurrency = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dFinalTipsTotalInForeignCurrency);
					
					if(oPosPaymentMethod.isChangeBackInForeignCurrency()) {
						dFinalChangeTotalInForeignCurrency = dFinalChangeTotal.divide(oCurrency.getRate(), 10, RoundingMode.HALF_UP);
						dFinalChangeTotalInForeignCurrency = oPosPaymentMethod.currencyRoundAmountToBigDecimal(dFinalChangeTotalInForeignCurrency);
					}
				}
			}else{
				// Remain is treated as tips
				if(oPosPaymentMethod.isPayByForeignCurrency()) {
					dFinalTipsTotalInForeignCurrency = new BigDecimal(dTipsTotal.toPlainString());
					dFinalTipsTotalInForeignCurrency = dFinalTipsTotalInForeignCurrency.add(dPayTotalInForeignCurrency.subtract(dCurrentBalanceInForeignCurrency));
					
					BigDecimal dUserTotalPayInLocalCurrency = (dUserInputValue.add(dTipsTotal)).multiply(oCurrency.getRate());
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
			if(oPosPaymentMethod.isPayByForeignCurrency()) {
				dFinalPayTotalInForeignCurrency = dCurrentBalanceInForeignCurrency;
				if(!oPosPaymentMethod.isChangeBackInForeignCurrency()) {
					dFinalPayTotalInForeignCurrency = new BigDecimal(dUserInputValue.toPlainString());
					dFinalPayTotalInForeignCurrency = oPosPaymentMethod.currencyRoundUpAmountWithCurrenctDecimalToBigDecimal(dFinalPayTotalInForeignCurrency);
				}
			}
			m_dCurrentBalance = BigDecimal.ZERO;
		}else{
			if(oPosPaymentMethod.isPayByForeignCurrency()) {
				dCurrentBalanceInForeignCurrency = dCurrentBalanceInForeignCurrency.subtract(dPayTotalInForeignCurrency);
				dFinalTipsTotalInForeignCurrency = new BigDecimal(dTipsTotal.toPlainString());
				dFinalPayTotalInForeignCurrency = dPayTotalInForeignCurrency;
				
				dTipsTotal = dTipsTotal.multiply(oCurrency.getRate());
				dTipsTotal = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dTipsTotal);
			}
			
			m_dCurrentBalance = m_dCurrentBalance.subtract(dPayTotal);
			dFinalTipsTotal = dTipsTotal;
			dFinalPayTotal = dPayTotal;
		}
		
		m_dTipsTotal = m_dTipsTotal.add(dFinalTipsTotal);
		m_dChangeTotal = dFinalChangeTotal;
		
		oCheckPayment.setPayTotal(dFinalPayTotal);
		oCheckPayment.setPayTips(dFinalTipsTotal);
		oCheckPayment.setPayChange(dFinalChangeTotal);
		oCheckPayment.setPayForeignTotal(dFinalPayTotalInForeignCurrency);
		oCheckPayment.setPayForeignTips(dFinalTipsTotalInForeignCurrency);
		oCheckPayment.setPayForeignChange(dFinalChangeTotalInForeignCurrency);
		oCheckPayment.setUserInputValue(dUserInputValue);
		
		if(m_dCurrentBalance.compareTo(BigDecimal.ZERO) == 0)
			// Finish payment
			iRet = 2;
		else
			iRet = 1;
		
		return iRet;
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
	
	// Delete new add payment
	public void deletePayment(int iPaymentIndex){
		if(iPaymentIndex >= m_oPosCheckPaymentList.size())
			return;
		
		PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iPaymentIndex);
		
		if(oPosCheckPayment.isCouponPaymentType()) {
			FuncCoupon oRedeemCoupon = new FuncCoupon();
			JSONObject oPaymentRefData = null;
			
			//update coupon status
			try {
				oPaymentRefData = new JSONObject(oPosCheckPayment.getRefData(1));
				if(oPaymentRefData.has("coupon_number") && !oPaymentRefData.isNull("coupon_number")) {
					oRedeemCoupon.setStartCoupon(oPaymentRefData.getString("coupon_number"));
					oRedeemCoupon.setEndCoupon(oPaymentRefData.getString("coupon_number"));
					if(oPaymentRefData.has("redeem_item_code") && !oPaymentRefData.isNull("redeem_item_code"))
						oRedeemCoupon.setRedeemItem("");
					
					oRedeemCoupon.setCouponCurrentStatus(FuncCoupon.COUPON_STATUS_LOCKED);
					oRedeemCoupon.setCouponNextStatus(FuncCoupon.COUPON_STATUS_SOLD);
					oRedeemCoupon.updateCoupon(FuncCoupon.UPDATE_COUPON_TYPE_USE_AS_PAYMENT);
				}
			}catch (JSONException e) {
				e.printStackTrace();
				AppGlobal.stack2Log(e);
			}
		}
		
		m_dCurrentBalance = m_dCurrentBalance.add(oPosCheckPayment.getPayTotal());
		m_dTipsTotal = m_dTipsTotal.subtract(oPosCheckPayment.getPayTips());
		
		// Remove payment from the list
		m_oPosCheckPaymentList.remove(iPaymentIndex);
	}
	
	public void addOldCheckPayment(PosCheckPayment oCheckPayment){
		m_oPosCheckPaymentList.add(oCheckPayment);
	}
	
	// Save check payment(s)
	public boolean saveMultipleCheckPayments(String sCheckPrefixNo, int iReceiptPrtqId, int iReceiptFormatId, boolean bOpenDrawerOnly) {
		int iShopId = 0, iOutletId = 0, iBusinessDayId = 0, iBusinessPeriodId = 0, iUserId = 0;
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
				iBusinessDayId = oCheckPayment.getBusinessDayId();
				iBusinessPeriodId = oCheckPayment.getBusinessPeriodId();
				iUserId = oCheckPayment.getPayUserId();				
			}
			
			// Process open drawer
			// *****************************************************************
			// Create thread to open drawer
			AppThreadManager oAppThreadManager = new AppThreadManager();
					
			// Add the method to the thread manager
			// Thread 1 : Open drawer

			// Create parameter array
			Object[] oParameters = new Object[7];
			oParameters[0] = iReceiptPrtqId;
			oParameters[1] = iShopId;
			oParameters[2] = iOutletId;
			oParameters[3] = iBusinessDayId;
			oParameters[4] = iBusinessPeriodId;
			oParameters[5] = iUserId;
			oParameters[6] = AppGlobal.g_oFuncStation.get().getStationId();
			oAppThreadManager.addThread(1, this, "openDrawer", oParameters);
			
			// Run the thread without wait
			oAppThreadManager.runThread();
		}
		
		if(bOpenDrawerOnly)
			return true;
		
		PosCheckPayment oCheckPayment = new PosCheckPayment();
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
	
	// Get paid balance
	public BigDecimal getPaidBalance() {
		BigDecimal dPaidBalance = BigDecimal.ZERO;
		if (m_oPosCheckPaymentList.size() > 0) {
			for(PosCheckPayment oCheckPayment:m_oPosCheckPaymentList)
				dPaidBalance = dPaidBalance.add(oCheckPayment.getPayTotal());
		}
		
		return dPaidBalance;
	}
	
	public void setCurrentBalance(BigDecimal dBalance) {
		m_dCurrentBalance = dBalance;		
	}
	
	// Get current balance
	public BigDecimal getCurrentBalance(){
		return m_dCurrentBalance;
	}
	
	// Get current balance in foreign currency
	public BigDecimal getCurrenctBalanceInForeignCurrency(PosPaymentMethod oPosPaymentMethod) {
		String sCurrencyCode = oPosPaymentMethod.getCurrencyCode();
		OutCurrency oCurrency = m_oCurrencyList.get(sCurrencyCode);
		BigDecimal dCurrentBalanceInForeignCurrency = BigDecimal.ZERO;
		BigDecimal dForeignCurrencyRate = new BigDecimal(oCurrency.getRate().toPlainString());
		
		dCurrentBalanceInForeignCurrency = m_dCurrentBalance.divide(dForeignCurrencyRate, 10, RoundingMode.HALF_UP);
		dCurrentBalanceInForeignCurrency = oPosPaymentMethod.currencyRoundUpAmountWithCurrenctDecimalToBigDecimal(dCurrentBalanceInForeignCurrency);
		
		return dCurrentBalanceInForeignCurrency;
	}
	
	// Get tips total
	public BigDecimal getTipsTotal(){
		return m_dTipsTotal;
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
	public void openDrawer(int iTargetPrtqId, int iShopId, int iOutletId, int iBusinessDayId, int iBusinessPeriodId, int iUserId, int iStationId){
		PosPaymentMethod oPaymentMethod = new PosPaymentMethod();
		String sOpenDrawerName = new String();
		
		// Drawer 1
		if(m_oOpenDrawerFlag.containsKey(1)){
			Boolean bIsOpen = m_oOpenDrawerFlag.get(1);
			sOpenDrawerName = "OpenDrawer1";
			if(bIsOpen == false){
				// Drawer 1 is not opened yet
				oPaymentMethod.openDrawer(iBusinessDayId, iShopId, iOutletId, iUserId, iStationId, iTargetPrtqId, "OpenDrawer1");
				
				m_oOpenDrawerFlag.put(1, true);
			}
			
			//create action log
			AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.open_first_drawer.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", iUserId, iShopId, iOutletId, iBusinessDayId, iBusinessPeriodId, iStationId, 0, 0, 0, 0, 0, sOpenDrawerName);
		}
		// Drawer 2
		if(m_oOpenDrawerFlag.containsKey(2)){
			Boolean bIsOpen = m_oOpenDrawerFlag.get(2);
			sOpenDrawerName = "OpenDrawer2";
			if(bIsOpen == false){
				// Drawer 2 is not opened yet
				oPaymentMethod.openDrawer(iBusinessDayId, iShopId, iOutletId, iUserId, iStationId, iTargetPrtqId, "OpenDrawer2");
				
				m_oOpenDrawerFlag.put(2, true);
			}
			
			//create action log
			AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.open_second_drawer.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", iUserId, iShopId, iOutletId, iBusinessDayId, iBusinessPeriodId, iStationId, 0, 0, 0, 0, 0, sOpenDrawerName);
		}

		//handle action log
		AppGlobal.g_oActionLog.get().handleActionLog(false);
	}
	
	//payment time
	public void setPaymentDateTime(DateTime oPaymentDateTime) {
		DateTimeFormatter oPaymentFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		for(PosCheckPayment oCheckPayment:m_oPosCheckPaymentList) {
			if(oCheckPayment.isDelete())
				continue;
			
			oCheckPayment.setPayLocTime(oPaymentDateTime);
			oCheckPayment.setPayTime(oPaymentFormatter.print(oCheckPayment.getPayLocTime().withZone(DateTimeZone.UTC)));
		}
	}
	
	// Check if the check is non revenue or not
	public String getNonRevenue(){
		for(PosCheckPayment oCheckPayment:m_oPosCheckPaymentList) {
			return oCheckPayment.getNonRevenue();
		}
		
		return "";
	}
	
	// Set Taiwan GUI type
	public void setTaiwanGuiType(String sTaiwanGuiType) {
		m_sTaiwanGUIType = sTaiwanGuiType;
	}
	
	// Get Taiwan GUI type
	public String getTaiwanGuiType() {
		return m_sTaiwanGUIType;
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
	
	public String getChangesForeignCurrencyName() {
		String sName = "";
		int iLastPaymentIndex = m_oPosCheckPaymentList.size() - 1;
		if(iLastPaymentIndex >= 0) {
			PosCheckPayment oPosCheckPayment = m_oPosCheckPaymentList.get(iLastPaymentIndex);
			if(oPosCheckPayment.isPayByForeignCurrency() && oPosCheckPayment.isChangeBackInForeignCurrency())
				sName = m_oCurrencyList.get(oPosCheckPayment.getCurrencyCode()).getName(AppGlobal.g_oCurrentLangIndex.get());
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
}
