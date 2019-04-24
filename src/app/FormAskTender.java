package app;

import java.math.BigDecimal;

import core.Controller;

import om.OutCurrency;
import om.PosPaymentMethod;

import app.AppGlobal;
import app.FrameDirectPaymentPanel;
import app.FrameDirectPaymentPanelListener;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormAskTender extends VirtualUIForm implements FrameDirectPaymentPanelListener {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameDirectPaymentPanel m_oFrameDirectPaymentPanel;
	private BigDecimal m_dPaymentAmount;
	private BigDecimal m_dTipsAmount;
	private boolean m_bCancelByUser;
	
	public FormAskTender(Controller oParentController){
		super(oParentController);
		
		m_dPaymentAmount = BigDecimal.ZERO;
		m_dTipsAmount = BigDecimal.ZERO;
		m_bCancelByUser = false;
	}
	
	public boolean init(String[] sTitle, FuncPayment oFuncPayment, int iPaymentMethodId, BigDecimal dPaymentAmount, boolean bNeedTips) {
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmAskTender.xml");
		
		m_oFrameDirectPaymentPanel = new FrameDirectPaymentPanel(false, oFuncPayment.isSupportSurcharge());
		m_oTemplateBuilder.buildFrame(m_oFrameDirectPaymentPanel, "fraDirectPaymentPanel");
		m_oFrameDirectPaymentPanel.setTitle(sTitle);
		m_oFrameDirectPaymentPanel.addListener(this);
		m_oFrameDirectPaymentPanel.setVisible(true);
		this.attachChild(m_oFrameDirectPaymentPanel);	
		
		VirtualUIFrame oFrameBackground = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameBackground, "fraWholeCover");
		oFrameBackground.setVisible(true);
		this.attachChild(oFrameBackground);
		
		BigDecimal dTipsAmount = BigDecimal.ZERO;
		boolean bPayByForeignCurrency = false;
		BigDecimal dFCRate = BigDecimal.ONE;
		BigDecimal dPaymentAmtInForeignCurrency = BigDecimal.ZERO;
		BigDecimal dTipsAmtInForeignCurrency = BigDecimal.ZERO;
		String sPaymentMethod;
		
		if(oFuncPayment.getPaymentMethodList().getPaymentMethodList().containsKey(iPaymentMethodId)){
			PosPaymentMethod oPosPaymentMethod = oFuncPayment.getPaymentMethodList().getPaymentMethodList().get(iPaymentMethodId);
			
			bPayByForeignCurrency = oPosPaymentMethod.isPayByForeignCurrency(AppGlobal.g_oFuncOutlet.get().getCurrencyCode());
			OutCurrency oCurrency = null;
			
			// Calculate the payment amount in foreign currency
			if(bPayByForeignCurrency) {
				oCurrency = oFuncPayment.getCurrency(oPosPaymentMethod.getCurrencyCode());
				dFCRate = oCurrency.getRate();
				dPaymentAmtInForeignCurrency = dPaymentAmount.multiply(dFCRate);
				dPaymentAmtInForeignCurrency = oPosPaymentMethod.currencyRoundUpAmountWithCurrenctDecimalToBigDecimal(dPaymentAmtInForeignCurrency);
				dTipsAmtInForeignCurrency = dTipsAmount.multiply(dFCRate);
				dTipsAmtInForeignCurrency = oPosPaymentMethod.currencyRoundUpAmountWithCurrenctDecimalToBigDecimal(dTipsAmtInForeignCurrency);
			}
			
			if(oPosPaymentMethod.getShortName(AppGlobal.g_oCurrentLangIndex.get()).isEmpty())
				sPaymentMethod = oPosPaymentMethod.getName(AppGlobal.g_oCurrentLangIndex.get());
			else
				sPaymentMethod = oPosPaymentMethod.getShortName(AppGlobal.g_oCurrentLangIndex.get());
			
			m_oFrameDirectPaymentPanel.showPanel(sPaymentMethod, dPaymentAmount, dTipsAmount, bNeedTips, bPayByForeignCurrency, oPosPaymentMethod, oCurrency, dPaymentAmtInForeignCurrency, dTipsAmtInForeignCurrency);
			
			return true;
		}else{
			return false;
		}
	}
	
	public BigDecimal getPaymentAmount(){
		return m_dPaymentAmount;
	}
	
	public BigDecimal getTipsAmount(){
		return m_dTipsAmount;
	}
	
	public boolean isUserCancel(){
		return m_bCancelByUser;
	}
	
	@Override
	public void FrameDirectPaymentPanel_finishAskAmount(
			BigDecimal dCurrentPaymentAmount, BigDecimal dCurrentTipsAmount) {
		m_dPaymentAmount = dCurrentPaymentAmount;
		m_dTipsAmount = dCurrentTipsAmount;
		this.finishShow();
	}

	@Override
	public boolean FrameDirectPaymentPanel_askTipsConfirmation(
			BigDecimal dCurrentPaymentAmount, BigDecimal dCurrentTipsAmount) {
		m_dPaymentAmount = dCurrentPaymentAmount;
		m_dTipsAmount = dCurrentTipsAmount;
		this.finishShow();
		
		return true;
	}

	@Override
	public void FrameDirectPaymentPanel_updateCashierHeader(boolean bAdd, String sSurcharge, BigDecimal dPaymentAmt, BigDecimal dTipsAmt) {
	}
	
	@Override
	public void FrameDirectPaymentPanel_cancel() {
		m_bCancelByUser = true;
		this.finishShow();
	}
	
	
}
