package app;

import java.math.BigDecimal;
import java.util.ArrayList;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import om.PosCheckExtraInfo;
import om.PosCheckPayment;
import om.PosPaymentMethodList;
import om.PosPaymentMethod;
import om.PosInterfaceConfig;
import om.InfVendor;
import om.InfInterface;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;
import org.json.JSONObject;

/** interface for the listeners/observers callback method */
interface FramePaymentDetailListener {
	void FramePaymentDetail_confirm();
	void FramePaymentDetail_cancel();
	void FramePaymentDetail_recordClicked(int iRow);
}

public class FramePaymentDetail extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;
	private VirtualUIFrame m_oFraCheckDetail;
	private VirtualUILabel m_oLabelWaiterNameHeader;
	private VirtualUILabel m_oLabelCheckNoHeader;
	private VirtualUILabel m_oLabelTableNoHeader;
	private VirtualUILabel m_oLabelCheckSubTotalHeader;
	private VirtualUILabel m_oLabelCheckScHeader;
	private VirtualUILabel m_oLabelCheckTaxHeader;
	private VirtualUILabel m_oLabelCheckDiscountHeader;
	private VirtualUILabel m_oLabelCheckRoundAmountHeader;
	private VirtualUILabel m_oLabelCheckTotalHeader;
	private VirtualUILabel m_oLabelWaiterName;
	private VirtualUILabel m_oLabelCheckNo;
	private VirtualUILabel m_oLabelTableNo;
	private VirtualUILabel m_oLabelCheckSubTotal;
	private VirtualUILabel m_oLabelCheckSc;
	private VirtualUILabel m_oLabelCheckTax;
	private VirtualUILabel m_oLabelCheckDiscount;
	private VirtualUILabel m_oLabelCheckRoundAmount;
	private VirtualUILabel m_oLabelCheckTotal;
	private VirtualUIFrame m_oFraRightFrame;
	private VirtualUIFrame m_ofraCheckDetailUnderline;

	private FrameCommonBasket m_oCommonBasket;
	private int m_iBasketRowHeight;
	
	private VirtualUIButton m_oButtonConfirm;
	private VirtualUIButton m_oButtonCancel;
	
	private FrameTitleHeader m_oTitleHeader;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FramePaymentDetailListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FramePaymentDetailListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FramePaymentDetailListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FramePaymentDetail() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FramePaymentDetailListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraPaymentDetail.xml");
		
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oTitleHeader.init(true);
		else
			m_oTitleHeader.init(false);
		m_oTitleHeader.addListener(this);
		this.attachChild(m_oTitleHeader);
		
		
		// Check Detail Frame
		m_oFraCheckDetail = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFraCheckDetail, "fraCheckDetail");
		this.attachChild(m_oFraCheckDetail);
		
		// Waiter Name Label
		m_oLabelWaiterNameHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelWaiterNameHeader, "lblWaiterNameHeader");
		m_oLabelWaiterNameHeader.setValue(AppGlobal.g_oLang.get()._("open_check_employee"));
		m_oFraCheckDetail.attachChild(m_oLabelWaiterNameHeader);
		
		// Check No. Label
		m_oLabelCheckNoHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckNoHeader, "lblCheckNoHeader");
		m_oLabelCheckNoHeader.setValue(AppGlobal.g_oLang.get()._("check_no"));
		m_oFraCheckDetail.attachChild(m_oLabelCheckNoHeader);
		
		// Table No. Label
		m_oLabelTableNoHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableNoHeader, "lblTableNoHeader");
		m_oLabelTableNoHeader.setValue(AppGlobal.g_oLang.get()._("table_no"));
		m_oFraCheckDetail.attachChild(m_oLabelTableNoHeader);
		
		// Sales Total Label
		m_oLabelCheckSubTotalHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckSubTotalHeader, "lblCheckSubTotalHeader");
		m_oLabelCheckSubTotalHeader.setValue(AppGlobal.g_oLang.get()._("sub_total"));
		m_oFraCheckDetail.attachChild(m_oLabelCheckSubTotalHeader);
		
		// S.C. Label
		m_oLabelCheckScHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckScHeader, "lblCheckScHeader");
		m_oLabelCheckScHeader.setValue(AppGlobal.g_oLang.get()._("sc_total"));
		m_oFraCheckDetail.attachChild(m_oLabelCheckScHeader);
		
		// Tax Label
		m_oLabelCheckTaxHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckTaxHeader, "lblCheckTaxHeader");
		m_oLabelCheckTaxHeader.setValue(AppGlobal.g_oLang.get()._("tax_total"));
		m_oFraCheckDetail.attachChild(m_oLabelCheckTaxHeader);
		
		// Discount Label
		m_oLabelCheckDiscountHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckDiscountHeader, "lblCheckDiscountHeader");
		m_oLabelCheckDiscountHeader.setValue(AppGlobal.g_oLang.get()._("discount"));
		m_oFraCheckDetail.attachChild(m_oLabelCheckDiscountHeader);
		
		// Round Amount Label
		m_oLabelCheckRoundAmountHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckRoundAmountHeader, "lblCheckRoundAmountHeader");
		m_oLabelCheckRoundAmountHeader.setValue(AppGlobal.g_oLang.get()._("round_amount"));
		m_oFraCheckDetail.attachChild(m_oLabelCheckRoundAmountHeader);
		
		// Check Total Label
		m_oLabelCheckTotalHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckTotalHeader, "lblCheckTotalHeader");
		m_oLabelCheckTotalHeader.setValue(AppGlobal.g_oLang.get()._("total"));
		m_oFraCheckDetail.attachChild(m_oLabelCheckTotalHeader);
		
		// Waiter Name
		m_oLabelWaiterName = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelWaiterName, "lblWaiterName");
		m_oFraCheckDetail.attachChild(m_oLabelWaiterName);
		
		// Check No.
		m_oLabelCheckNo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckNo, "lblCheckNo");
		m_oFraCheckDetail.attachChild(m_oLabelCheckNo);
		
		m_oLabelTableNo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableNo, "lblTableNo");
		m_oFraCheckDetail.attachChild(m_oLabelTableNo);
		
		m_oLabelCheckSubTotal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckSubTotal, "lblCheckSubTotal");
		m_oFraCheckDetail.attachChild(m_oLabelCheckSubTotal);
		
		m_oLabelCheckSc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckSc, "lblCheckSc");
		m_oFraCheckDetail.attachChild(m_oLabelCheckSc);
		
		m_oLabelCheckTax = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckTax, "lblCheckTax");
		m_oFraCheckDetail.attachChild(m_oLabelCheckTax);
		
		m_oLabelCheckDiscount = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckDiscount, "lblCheckDiscount");
		m_oFraCheckDetail.attachChild(m_oLabelCheckDiscount);
		
		m_oLabelCheckRoundAmount = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckRoundAmount, "lblCheckRoundAmount");
		m_oFraCheckDetail.attachChild(m_oLabelCheckRoundAmount);
		
		m_oLabelCheckTotal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckTotal, "lblCheckTotal");
		m_oFraCheckDetail.attachChild(m_oLabelCheckTotal);
		
		int iTop = 48;
		for (int i = 0; i < 8; i++) {
			m_ofraCheckDetailUnderline = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(m_ofraCheckDetailUnderline, "fraCheckDetailUnderline");
			m_ofraCheckDetailUnderline.setTop(62 + iTop * i);
			m_ofraCheckDetailUnderline.setLeft(15);
			m_oFraCheckDetail.attachChild(m_ofraCheckDetailUnderline);
		}

		m_oFraRightFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFraRightFrame, "fraRightFrame");
		this.attachChild(m_oFraRightFrame);	

		// Check Payment basket
		m_oCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oCommonBasket, "fraCheckPaymentBasket");
		m_oCommonBasket.init();
		m_oCommonBasket.addListener(this);
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oFraRightFrame.attachChild(m_oCommonBasket);
		else
			this.attachChild(m_oCommonBasket);
		
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_iBasketRowHeight = 50;
		else
			m_iBasketRowHeight = 0;
		
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		if (!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			iFieldWidths.add(240);
		else
			iFieldWidths.add(150);
		sFieldValues.add(AppGlobal.g_oLang.get()._("payment_type"));
		if (!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			iFieldWidths.add(220);
		else
			iFieldWidths.add(160);
		sFieldValues.add(AppGlobal.g_oLang.get()._("amount"));
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			iFieldWidths.add(80);
		else
			iFieldWidths.add(90);
		sFieldValues.add(AppGlobal.g_oLang.get()._("tips"));
		m_oCommonBasket.addHeader(iFieldWidths, sFieldValues);
		m_oCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		m_oCommonBasket.setBottomUnderlineVisible(false);
		
		// Confirm button
		m_oButtonConfirm = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonConfirm, "btnConfirm");
		m_oButtonConfirm.setValue(AppGlobal.g_oLang.get()._("confirm"));
		this.attachChild(m_oButtonConfirm);	
		
		// Cancel button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "btnCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		this.attachChild(m_oButtonCancel);	
	}
	
	public void setupValue(FuncCheck oFuncCheck, PosPaymentMethodList oPosPaymentMethodList, String sFunctionKey) {
		m_oLabelWaiterName.setValue(oFuncCheck.getOpenUserName());
		m_oLabelCheckNo.setValue(oFuncCheck.getCheckPrefixNo());
		m_oLabelTableNo.setValue(oFuncCheck.getTableNoWithTableName());
		m_oLabelCheckSubTotal.setValue(AppGlobal.g_oFuncOutlet.get().roundItemAmountToString(oFuncCheck.getItemTotal()));
		m_oLabelCheckSc.setValue(AppGlobal.g_oFuncOutlet.get().roundSCAmountToString(oFuncCheck.getServiceChargeTotal()));
		m_oLabelCheckTax.setValue(AppGlobal.g_oFuncOutlet.get().roundTaxAmountToString(oFuncCheck.getTaxTotal()));
		BigDecimal dItemDiscountTotal = BigDecimal.ZERO;
		dItemDiscountTotal = dItemDiscountTotal.subtract(oFuncCheck.getCheckDiscountTotal());
		dItemDiscountTotal = dItemDiscountTotal.subtract(oFuncCheck.getItemDiscountTotal());
		m_oLabelCheckDiscount.setValue(AppGlobal.g_oFuncOutlet.get().roundDiscAmountToString(dItemDiscountTotal));
		if (oFuncCheck.getRoundAmount().compareTo(BigDecimal.ZERO) == 0)
			m_oLabelCheckRoundAmount.setValue("0");
		else
			m_oLabelCheckRoundAmount.setValue(oFuncCheck.getRoundAmount().stripTrailingZeros().toPlainString());
		m_oLabelCheckDiscount.setValue(AppGlobal.g_oFuncOutlet.get().roundDiscAmountToString(dItemDiscountTotal));
		m_oLabelCheckTotal.setValue(AppGlobal.g_oFuncOutlet.get().roundCheckAmountToString(oFuncCheck.getCheckTotal()));
		
		int iCount = 0;
		for (PosCheckPayment oCheckPayment : oFuncCheck.getCheckPaymentList()) {
			ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
			ArrayList<String> sFieldValues = new ArrayList<String>();
			ArrayList<String> sFieldAligns = new ArrayList<String>();
			
			String sCheckPaymentName;
			if (oCheckPayment.getShortName(AppGlobal.g_oCurrentLangIndex.get()).isEmpty())
				sCheckPaymentName = oCheckPayment.getName(AppGlobal.g_oCurrentLangIndex.get());
			else
				sCheckPaymentName = oCheckPayment.getShortName(AppGlobal.g_oCurrentLangIndex.get());
			
			if (!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
				iFieldWidths.add(240);
			else
				iFieldWidths.add(150);
			sFieldValues.add(sCheckPaymentName);
			sFieldAligns.add("");
			if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
				iFieldWidths.add(220);
			else
				iFieldWidths.add(160);
			sFieldValues.add(AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(oCheckPayment.getPayTotal()));
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
			if (!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
				iFieldWidths.add(90);
			else
				iFieldWidths.add(90);
			sFieldValues.add(AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(oCheckPayment.getPayTips()));
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
			
			m_oCommonBasket.addItem(0, iCount++, m_iBasketRowHeight, iFieldWidths, sFieldValues, sFieldAligns, null, null);
		
			if (sFunctionKey.equals(AppGlobal.FUNC_LIST.void_payment.name())
					&& oPosPaymentMethodList.getPaymentMethod(oCheckPayment.getPaymentMethodId()).isNonRefund()) {
				// For non-refundable payment, set its color to dim color
				m_oCommonBasket.setAllFieldsForegroundColor(0, iCount - 1, "#A5ABB2");
				m_oCommonBasket.setAllFieldsBackgroundColor(0, iCount - 1, "#D8DBDF");
			} else
				m_oCommonBasket.setAllFieldsForegroundColor(0, iCount - 1, "#0055B8");
			
			// show reference and comp slip number for Bally Gaming Interface
			if (sFunctionKey.equals(AppGlobal.FUNC_LIST.void_payment.name())) {
				String sPaymentInfo1 = "", sPaymentInfo2 = "";
				if (oCheckPayment.getRefData(1) != null)
					sPaymentInfo1 = oCheckPayment.getRefDataValueByIndexWithoutKey(1);
				if (oCheckPayment.getRefData(2) != null)
					sPaymentInfo2 = oCheckPayment.getRefDataValueByIndexWithoutKey(2);
				
				int iPaymentMethodId = oCheckPayment.getPaymentMethodId();
				PosPaymentMethod oPaymentMethod = oPosPaymentMethodList.getPaymentMethod(iPaymentMethodId);
				if (oPaymentMethod.isGamingInterfacePayment(InfVendor.KEY_BALLY)) {
					// get payment method type (compslip/dollar)
					PosInterfaceConfig oSelectedGamingInterfaceConfig = oPaymentMethod
							.getInterfaceConfig(InfInterface.TYPE_GAMING_INTERFACE).get(0);
					JSONObject oPosInterfaceConfigJson = oSelectedGamingInterfaceConfig.getConfigValue();
					String sPaymentType = "";
					if (oPosInterfaceConfigJson.has("payment_setup")
							&& oPosInterfaceConfigJson.optJSONObject("payment_setup").has("params") && oPosInterfaceConfigJson
							.optJSONObject("payment_setup").optJSONObject("params").has("payment_type"))
						sPaymentType = oPosInterfaceConfigJson.optJSONObject("payment_setup").optJSONObject("params")
								.optJSONObject("payment_type").optString("value");
					//get compslip number
					if (sPaymentType.equals(FuncGamingInterface.PAYMENT_TYPE_COMP_SLIP)) {
						String sCompSlipNo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_GAMING_INTERFACE, PosCheckExtraInfo.VARIABLE_PAYMENT_INFO, 0).getValue();
						if (sPaymentInfo1.isEmpty())
							sPaymentInfo1 = AppGlobal.g_oLang.get()._("comp_slip_no") + ": " + sCompSlipNo;
						else
							sPaymentInfo2 = AppGlobal.g_oLang.get()._("comp_slip_no") + ": " + sCompSlipNo;
					}
				}
				if (!sPaymentInfo1.isEmpty())
					m_oCommonBasket.setFieldInfo1(0, iCount - 1, 0, sPaymentInfo1);
				if (!sPaymentInfo2.isEmpty())
					m_oCommonBasket.setFieldInfo2(0, iCount - 1, 0, sPaymentInfo2);
			}
		}
		
		if (sFunctionKey.equals(AppGlobal.FUNC_LIST.void_payment.name())) {
			m_oButtonConfirm.setVisible(false);
			m_oButtonCancel.setVisible(false);
		}
	}
	
	public void setTitle(String sTitle){
		m_oTitleHeader.setTitle(sTitle);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		if (iChildId == m_oButtonConfirm.getId()) { // Exit button clicked
			for (FramePaymentDetailListener listener : listeners) {
				listener.FramePaymentDetail_confirm();
				break;
			}
			return true;
		} else if (iChildId == m_oButtonCancel.getId()) { // Previous button clicked
			for (FramePaymentDetailListener listener : listeners) {
				listener.FramePaymentDetail_cancel();
				break;
			}
			return true;
		}
		
		return false;
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId,
			String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		for(FramePaymentDetailListener listener : listeners){
				listener.FramePaymentDetail_recordClicked(iItemIndex);
		}
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		
	}

	@Override
	public void FrameTitleHeader_close() {
		for(FramePaymentDetailListener listener : listeners) {
			listener.FramePaymentDetail_cancel();
			break;
		}
	}
}
