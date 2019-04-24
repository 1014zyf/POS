package app;

import java.math.BigDecimal;

import org.json.JSONObject;

import om.PosCheckPayment;
import om.PosVoidReason;

public interface FuncCheckListener {
	void FuncCheck_updateItemStockQty(int iItemId);
	void FuncCheck_finishSendCheck(String sStoredProcessingCheckKey);
	boolean FuncCheck_creditCardSpectraVoidPayment(PosCheckPayment oCheckPayment, String sCreditCardMethodType, boolean bIsFirstPosting);
	boolean FuncCheck_creditCardSpectraAdjustTips(String sTraceNo, BigDecimal oNewPayTotal, BigDecimal oNewTips, boolean bIsFirstPosting);
	boolean FuncCheck_creditCardSpectraDccOptOut(String sChksCheckPrefixNum, String sTraceNum, JSONObject oRefDataJSONObject, boolean bIsFirstPosting);
	int FuncCheck_getReceiptFormat();
	void FuncCheck_updateProcessingCheckInfo(String sStoredProcessingCheckKey, JSONObject oSendJSONRequest);
	void FuncCheck_rollbackTaxAndSCForReleasePayment(FuncCheck oFuncCheck, PosCheckPayment oPosCheckPayment, PosVoidReason oPosVoidReason);
	public boolean FuncCheck_selectContinuousPrint();
	public boolean FuncCheck_confirmToVoidPayment(String sPaymentMethodName, String sPaymentAmount);
	void FuncCheck_updateBasketExtendBarCheckTotal();
	boolean FuncCheck_isRollbackNeededForCheckMaximum(FuncCheck oFuncCheck, BigDecimal dAdditionalAmount);
	void FuncCheck_updateBasketItemPrice(int iSeatNo, int iItemIndex, FuncCheckItem oFuncCheckItem);
}
