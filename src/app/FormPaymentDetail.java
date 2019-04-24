package app;

import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import core.Controller;
import om.PosCheckPayment;
import om.PosPaymentMethod;
import om.PosPaymentMethodList;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

public class FormPaymentDetail extends VirtualUIForm implements FramePaymentDetailListener{
	TemplateBuilder m_oTemplateBuilder;

	private boolean m_bConfirmRelease;
	private FramePaymentDetail m_oFramePaymentDetail;
	
	private PosPaymentMethodList m_oPosPaymentMethodList;
	private String m_sFunctionKey;
	private FuncCheck m_oFuncCheck;
	private int m_iSelectedPaymentIndex;
	
	public FormPaymentDetail(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oFuncCheck = new FuncCheck();
		
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmPaymentDetail.xml");

		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Load form from template file
		m_oFramePaymentDetail = new FramePaymentDetail();
		m_oTemplateBuilder.buildFrame(m_oFramePaymentDetail, "fraPaymentDetail");
		
		// Add listener
		m_oFramePaymentDetail.addListener(this);
		this.attachChild(m_oFramePaymentDetail);
		
		m_iSelectedPaymentIndex = -1;
	}
	
	public void initWithFuncCheck(FuncCheck oFuncCheck, PosPaymentMethodList oPosPaymentMethodList, String sFunctionKey) {
		m_oFuncCheck = oFuncCheck;
		m_sFunctionKey = sFunctionKey;
		m_oPosPaymentMethodList = oPosPaymentMethodList;
		
		boolean bIsNonRefund = false;
		if (sFunctionKey.equals(AppGlobal.FUNC_LIST.void_payment.name())) {
			for (PosCheckPayment oCheckPayment : oFuncCheck.getCheckPaymentList()) {
				if (oPosPaymentMethodList.getPaymentMethod(oCheckPayment.getPaymentMethodId()).isNonRefund()) {
					bIsNonRefund = true;
					break;
				}
			}
			
			if (bIsNonRefund) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("this_check_contains_non_refundable_payment"));
				oFormDialogBox.show();
			}
		}
		
		m_oFramePaymentDetail.setupValue(oFuncCheck, oPosPaymentMethodList, sFunctionKey);
	}
	
	public void setTitle(String sTitle){
		m_oFramePaymentDetail.setTitle(sTitle);
	}
	
	private void closeFrame(boolean bConfirmRelease) {
		m_bConfirmRelease = bConfirmRelease;
		this.finishShow();
	}
	
	@Override
	public void FramePaymentDetail_confirm() {
		this.closeFrame(true);
	}
	
	public void FramePaymentDetail_cancel() {
		this.closeFrame(false);
	}

	public boolean confirmRelease() {
		return m_bConfirmRelease;
	}
	
	public int getSelectedPaymentIndex() {
		return m_iSelectedPaymentIndex;
	}

	@Override
	public void FramePaymentDetail_recordClicked(int iRow) {
		if (m_sFunctionKey.equals(AppGlobal.FUNC_LIST.void_payment.name())) {
			FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
			oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			
			PosPaymentMethod oPosPaymentMethod = m_oPosPaymentMethodList.getPaymentMethod(m_oFuncCheck.getCheckPaymentList().get(iRow).getPaymentMethodId());
			String sMessage = "";
			if (oPosPaymentMethod.isNonRefund())
				sMessage = AppGlobal.g_oLang.get()._("this_payment_is_non_refundable") + ", ";
			sMessage += AppGlobal.g_oLang.get()._("confirm_to_void_payment") + ": " + oPosPaymentMethod.getName(AppGlobal.g_oCurrentLangIndex.get()) + "?";
			oFormConfirmBox.setMessage(sMessage);
			oFormConfirmBox.show();
			if (oFormConfirmBox.isOKClicked() == false)
				return;
			
			m_iSelectedPaymentIndex = iRow;
			this.closeFrame(true);
		}
	}
}
