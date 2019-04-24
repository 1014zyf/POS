package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import core.Controller;
import om.PosCheckPayment;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;

public class FormAdjustTips extends VirtualUIForm implements FrameAdjustTipsListener{
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameAdjustTips m_oFrameAdjustTips;
	private List<HashMap<String, String>> m_oPaymentInfos;
	
	private ArrayList<PosCheckPayment> m_oPosCheckPaymentList;
	
	public FormAdjustTips(List<HashMap<String, String>> oPaymentInfos, String sCheckPrefixNo, int iCheckGuest, BigDecimal dCheckTotal, String sTable, Controller oParentController, ArrayList<PosCheckPayment> oPosCheckPaymentList) {
		super(oParentController);
		this.m_oPosCheckPaymentList = oPosCheckPaymentList;
		initForConstructorWithoutPosCheckPaymentList(oPaymentInfos, sCheckPrefixNo,iCheckGuest,  dCheckTotal, sTable,  oParentController);
	}
	
	private void initForConstructorWithoutPosCheckPaymentList(List<HashMap<String, String>> oPaymentInfos, String sCheckPrefixNo, int iCheckGuest, BigDecimal dCheckTotal, String sTable, Controller oParentController) {
			m_oTemplateBuilder = new TemplateBuilder();
			m_oTemplateBuilder.loadTemplate("frmAdjustTips.xml");
			m_oPaymentInfos = oPaymentInfos;
			
			// Adjust tips frame
			m_oFrameAdjustTips = new FrameAdjustTips(oPaymentInfos, sCheckPrefixNo, iCheckGuest, dCheckTotal, sTable);
			m_oTemplateBuilder.buildFrame(m_oFrameAdjustTips, "fraAdjustTips");
			m_oFrameAdjustTips.addListener(this);
			this.attachChild(m_oFrameAdjustTips);
			
	}
	
	public FormAdjustTips(List<HashMap<String, String>> oPaymentInfos, String sCheckPrefixNo, int iCheckGuest, BigDecimal dCheckTotal, String sTable, Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		m_oTemplateBuilder.loadTemplate("frmAdjustTips.xml");
		
		m_oPaymentInfos = null;
		// Adjust tips frame
		m_oFrameAdjustTips = new FrameAdjustTips(oPaymentInfos, sCheckPrefixNo, iCheckGuest, dCheckTotal, sTable);
		m_oTemplateBuilder.buildFrame(m_oFrameAdjustTips, "fraAdjustTips");
		m_oFrameAdjustTips.addListener(this);
		this.attachChild(m_oFrameAdjustTips);
	}
	
	public List<HashMap<String, String>> getPaymentInfos() {
		return m_oPaymentInfos;
	}
	
	@Override
	public void frameAdjustTips_ClickExit() {
		// Finish showing this form
		m_oPaymentInfos = null;
		this.finishShow();
	}
	
	@Override
	public void frameAdjustTips_ClickSave(List<HashMap<String, String>> oPaymentInfos) {
		m_oPaymentInfos = oPaymentInfos;
		this.finishShow();
	}
	
	@Override
	public boolean frameAdjustTips_AskTipsConfirmation(BigDecimal dPaymentAmount, BigDecimal dTips) {
		BigDecimal dHalfPayAmount = dPaymentAmount.multiply(new BigDecimal("0.5"));
		
		if(dTips.compareTo(BigDecimal.ZERO) < 0) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("tips_should_be_greater_than_zero"));
			oFormDialogBox.show();
			oFormDialogBox = null;
			return false;
		}
		
		if(dTips.compareTo(dHalfPayAmount) > 0) {
			// Tips Amount 	> 0.5 x Payment
			FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
			oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("tips_is_greater_than_half_of_the_payment")+", "+AppGlobal.g_oLang.get()._("continue")+"?");
			oFormConfirmBox.show();
			if(oFormConfirmBox.isOKClicked() == false)
				return false;
		}
		
		return true;
	}
	
	@Override
	public boolean frameAdjustTips_CheckTipsPercentage(BigDecimal dTipsPercentage) {
		// check tips percentage should be between 0 - 100
		if (dTipsPercentage.compareTo(new BigDecimal("100.00")) > 0) {
			String sErrMsg = AppGlobal.g_oLang.get()._("tips_percentage_should_be")+" 0 - 100";
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormDialogBox.setMessage(sErrMsg);
			oFormDialogBox.show();
			oFormDialogBox = null;
			return false;
		}
		return true;
	}
}
