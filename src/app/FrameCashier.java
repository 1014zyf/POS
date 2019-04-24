package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import commonui.FrameNumberPad;
import commonui.FrameNumberPadListener;
import externallib.StringLib;
import om.InfInterface;
import om.InfVendor;
import om.PosInterfaceConfig;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FrameCashierListener {
	void FrameCashier_loadCheck(String sCheck);
	void FrameCashier_loadTable();
	void FrameCashier_finishAskAmount(int iSectionId, int iItemIndex, BigDecimal dPaymentAmount, BigDecimal dTipsAmount);
	void FrameCashier_clickBasketPayment(int iSectionId, int iItemIndex);
	boolean FrameCashier_askTipsConfirmation(BigDecimal dCurrentPaymentAmount, BigDecimal dCurrentTipsAmount);
	void FrameCashier_cancel(boolean bForceDelete);
	boolean FrameCashier_exit();
	void FrameCashier_addDefaultPaymentForActivePosPaymentGatewayTransaction();
	boolean FrameCashier_doPaymentCardTopUpAuthorization(int iPgtxPayId, BigDecimal dTopUpAuthAmount, BigDecimal dTotalAuthAmount);
	void FrameCashier_doPartialPayment(String sTriggerBy);
}

public class FrameCashier extends VirtualUIFrame implements FrameNumberPadListener, FrameCommonBasketListener {
	private TemplateBuilder m_oTemplateBuilder;

	private VirtualUILabel m_oLabelCheckNoHeader;
	private VirtualUILabel m_oLabelCheckNo;
	private VirtualUITextbox m_oTextboxCheckNo;
	private VirtualUILabel m_oLabelTableHeader;
	private VirtualUILabel m_oLabelTable;
	private VirtualUIFrame m_oFrameAskTable;
	private VirtualUILabel m_oLabelTotalHeader;
	private VirtualUILabel m_oLabelTotal;
	private VirtualUILabel m_oLabelBalanceHeader;
	private VirtualUILabel m_oLabelBalance;
	private VirtualUILabel m_oLabelSurchargeHeader;
	private VirtualUILabel m_oLabelSurcharge;
	private VirtualUILabel m_oLabelTotalDueHeader;
	private VirtualUILabel m_oLabelTotalDue;
	private FrameCommonBasket m_oCommonBasket;
	private VirtualUILabel m_oButtonExit;
	private VirtualUILabel m_oButtonFinish;
	private VirtualUILabel m_oButtonClose;
	private FrameNumberPad m_oFrameNumberPad;

	// display extra information
	private VirtualUILabel m_oLabelOtherInformationHeader;
	private VirtualUILabel m_oLabelOtherInformation;
	private VirtualUILabel m_oLabelOtherInformationHeader2;
	private VirtualUILabel m_oLabelOtherInformation2;
	private VirtualUILabel m_oLabelOtherInformationHeader3;
	private VirtualUILabel m_oLabelOtherInformation3;
	private VirtualUILabel m_oLabelOtherInformationHeader4;
	private VirtualUILabel m_oLabelOtherInformation4;
	private VirtualUILabel m_oLabelOtherInformation5;
	private VirtualUILabel m_oLabelPartialPaymentBgColor;
	
	
	// For edit field
	// Current Payment Step
	// 1 - just add payment, go to ask payment amount
	// 2 - payment amount is added, go to ask tips amount
	// 3 - payment amount and tips amount is added, go to finish payment
	// 4 - finish processing a partial payment
	
	private int m_iCurrentPaymentStep;
	private boolean m_bCurrentNeedAskPaymentAmount;
	private boolean m_bCurrentNeedAskTipsAmount;
	private int m_iCurrentEditSectionIndex;
	private int m_iCurrentEditItemIndex;
	private int m_iCurrentEditFieldIndex;
	private BigDecimal m_dOriginalPaymentAmount;
	private BigDecimal m_dCurrentPaymentAmount;
	private BigDecimal m_dCurrentTipsAmount;
	
	// Flag to determine if the check is loaded or not
	private boolean m_bIsCheckLoaded;

	// Flag to determine cashier mode opened by cashier_mode or print_and_paid
	private boolean m_bIsClickByCashierMode; // true = click by cahier_mode function | false =  click by paid, print_and_paid function
	
	private boolean m_bIsFinishPaymentByAdjustPayment;
	
	private boolean m_bIsNeedCheckPaymentAmountByAuthAmount;
	private boolean m_bIsTipsInputDefaultZero;
	private boolean m_bIsNeedPaymentCardTopAuth;
	private int m_iPosPaymentGatewayPgtxId;
	private boolean m_bHideNumberPad;
	
	private String m_sCheckTotal = "";
	private String m_sPaidTotal = "";
	
	// finish payment trriger by
	public static String TRIGGER_BY_FINISH_PAYMENT_SELECTION = "finish_payment_selecion";
	public static String TRIGGER_BY_CLOSE_CHECK = "close_check";
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameCashierListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameCashierListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameCashierListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameCashier(boolean bIsCheckLoaded, boolean bHideNumberPad, boolean bSupportSurcharge) {
		m_iCurrentPaymentStep = 0;
		m_dOriginalPaymentAmount = BigDecimal.ZERO;
		m_dCurrentPaymentAmount = BigDecimal.ZERO;
		m_dCurrentTipsAmount = BigDecimal.ZERO;
		m_bHideNumberPad = bHideNumberPad;
		
		m_sCheckTotal = "";
		m_sPaidTotal = "";
		
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameCashierListener>();
				
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraCashier.xml");
		
		VirtualUIFrame oFrameHeaderGroup = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameHeaderGroup, "fraLabelGroupHeader");
		oFrameHeaderGroup.setCornerRadius("0,0,0,12");
		this.attachChild(oFrameHeaderGroup);
		
		VirtualUIFrame oFrameHeader = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameHeader, "fraLabelHeader");
		oFrameHeader.setCornerRadius("12");
		this.attachChild(oFrameHeader);
		
		VirtualUIFrame oFrameHeaderLine = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameHeaderLine, "fraLabelHeader");
		oFrameHeaderLine.setBackgroundColor("#DDDDDD");
		oFrameHeaderLine.setHeight(3);
		oFrameHeaderLine.setTop(oFrameHeader.getHeight() / 2);
		this.attachChild(oFrameHeaderLine);
		
		oFrameHeaderLine = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameHeaderLine, "fraLabelHeader");
		oFrameHeaderLine.setBackgroundColor("#DDDDDD");
		oFrameHeaderLine.setWidth(3);
		oFrameHeaderLine.setLeft((oFrameHeader.getWidth()) / 2);
		this.attachChild(oFrameHeaderLine);
		
		// Detail
		m_oLabelCheckNoHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckNoHeader, "lblCheckNoHeader");
		m_oLabelCheckNoHeader.setValue(AppGlobal.g_oLang.get()._("check_no", ""));
		m_oLabelCheckNoHeader.allowClick(true);
		m_oLabelCheckNoHeader.setClickServerRequestBlockUI(false);
		this.attachChild(m_oLabelCheckNoHeader);
		
		m_oLabelCheckNo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckNo, "lblCheckNo");
		if(!bIsCheckLoaded)
			m_oLabelCheckNo.setVisible(false);
		this.attachChild(m_oLabelCheckNo);
		
		m_oTextboxCheckNo = new VirtualUITextbox();
		m_oTextboxCheckNo.setExist(true);
		
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oTextboxCheckNo.setTop(m_oLabelCheckNo.getTop() + 8);
			m_oTextboxCheckNo.setLeft(m_oLabelCheckNo.getLeft());
			m_oTextboxCheckNo.setWidth(m_oLabelCheckNo.getWidth());
			m_oTextboxCheckNo.setHeight(m_oLabelCheckNo.getHeight() - 16);
		}else {
			m_oTextboxCheckNo.setTop(m_oLabelCheckNo.getTop());
			m_oTextboxCheckNo.setLeft(m_oLabelCheckNo.getLeft());
			m_oTextboxCheckNo.setWidth(m_oLabelCheckNo.getWidth());
			m_oTextboxCheckNo.setHeight(m_oLabelCheckNo.getHeight());
		}
		m_oTextboxCheckNo.setTextSize(m_oLabelCheckNo.getTextSize());
		m_oTextboxCheckNo.setBackgroundColor("#FFFFFF");
		m_oTextboxCheckNo.setForegroundColor("#000000");
		
		if(bIsCheckLoaded)
			m_oTextboxCheckNo.setVisible(false);
		this.attachChild(m_oTextboxCheckNo);
		
		m_oLabelTableHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableHeader, "lblTableHeader");
		m_oLabelTableHeader.setValue(AppGlobal.g_oLang.get()._("table", ""));
		m_oLabelTableHeader.allowClick(true);
		m_oLabelTableHeader.setClickServerRequestBlockUI(false);
		this.attachChild(m_oLabelTableHeader);
		
		m_oLabelTable = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTable, "lblTable");
		if(!bIsCheckLoaded)
			m_oLabelTable.setVisible(false);
		this.attachChild(m_oLabelTable);
		
		m_oFrameAskTable = new VirtualUIFrame();
		m_oFrameAskTable.setExist(true);
		
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oFrameAskTable.setTop(m_oLabelTable.getTop() + 8);
			m_oFrameAskTable.setLeft(m_oLabelTable.getLeft());
			m_oFrameAskTable.setWidth(m_oLabelTable.getWidth());
			m_oFrameAskTable.setHeight(m_oLabelTable.getHeight() - 16);
		}else {
			m_oFrameAskTable.setTop(m_oLabelTable.getTop());
			m_oFrameAskTable.setLeft(m_oLabelTable.getLeft());
			m_oFrameAskTable.setWidth(m_oLabelTable.getWidth());
			m_oFrameAskTable.setHeight(m_oLabelTable.getHeight());
		}
		m_oFrameAskTable.setBackgroundColor("#FFFFFF");
		
		m_oFrameAskTable.allowClick(true);
		if(bIsCheckLoaded)
			m_oFrameAskTable.setVisible(false);
		this.attachChild(m_oFrameAskTable);
		
		m_oLabelTotalHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTotalHeader, "lblTotalHeader");
		m_oLabelTotalHeader.setValue(AppGlobal.g_oLang.get()._("total", ""));
		this.attachChild(m_oLabelTotalHeader);
		
		m_oLabelTotal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTotal, "lblTotal");
		this.attachChild(m_oLabelTotal);
		
		m_oLabelBalanceHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelBalanceHeader, "lblBalanceHeader");
		m_oLabelBalanceHeader.setValue(AppGlobal.g_oLang.get()._("balance", ""));
		this.attachChild(m_oLabelBalanceHeader);
		
		m_oLabelBalance = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelBalance, "lblBalance");
		this.attachChild(m_oLabelBalance);
		
		// Payment basket
		m_oCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oCommonBasket, "fraPaymentBasket");
		m_oCommonBasket.init();
		
		m_oCommonBasket.addListener(this);
		this.attachChild(m_oCommonBasket);
		
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String[]> sFieldValues = new ArrayList<String[]>();
		iFieldWidths.add(m_oCommonBasket.getWidth()/4*2);
		sFieldValues.add(AppGlobal.g_oLang.get()._("payment_type", ""));
		iFieldWidths.add(m_oCommonBasket.getWidth()/4);
		sFieldValues.add(AppGlobal.g_oLang.get()._("amount", ""));
		iFieldWidths.add(m_oCommonBasket.getWidth()/4);
		sFieldValues.add(AppGlobal.g_oLang.get()._("tips", ""));
		m_oCommonBasket.addHeader(iFieldWidths, sFieldValues);
		m_oCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		m_oCommonBasket.setBottomUnderlineVisible(false);
		m_oCommonBasket.setCashierCommonBasketHeight(260);
		m_oCommonBasket.setHeaderFormat(40, 16, "5,0,0,14");
		
		// Exit button
		m_oButtonExit = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oButtonExit, "btnExit");
		m_oButtonExit.setValue(AppGlobal.g_oLang.get()._("exit", ""));
		m_oButtonExit.allowClick(true);
		m_oButtonExit.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		this.attachChild(m_oButtonExit);
		
		// Finish button
		m_oButtonFinish = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oButtonFinish, "btnFinish");
		m_oButtonFinish.setValue(AppGlobal.g_oLang.get()._("finish", ""));
		m_oButtonFinish.allowClick(true);
		m_oButtonFinish.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		this.attachChild(m_oButtonFinish);

		//Close button
		m_oButtonClose = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oButtonClose, "btnClose");
		m_oButtonClose.setValue(AppGlobal.g_oLang.get()._("close", ""));
		m_oButtonClose.allowClick(true);
		m_oButtonClose.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		this.attachChild(m_oButtonClose);
		
		// Number pad
		m_oFrameNumberPad = new FrameNumberPad();
		if (m_bHideNumberPad)
			m_oFrameNumberPad.hideNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		// shorten the height for Taiwan GUI by outlet
		if (AppGlobal.g_oFuncStation.get().supportTaiwanGui()){
			m_oFrameNumberPad.setTop(480);
			m_oFrameNumberPad.setCustomHeight(120);
		}
		
		//Rearrange the position for support surcharge
		if (bSupportSurcharge && !AppGlobal.g_oFuncStation.get().supportTaiwanGui()) {
			m_oFrameNumberPad.setTop(440);
			m_oFrameNumberPad.setCustomHeight(160);
		}
		
		m_oFrameNumberPad.setCornerRadius("12,0,0,0");
		
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.setEnterBlockUI(true);
		m_oFrameNumberPad.addListener(this);
		this.attachChild(m_oFrameNumberPad);
		
		// Other Information
		m_oLabelOtherInformationHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformationHeader, "lblOtherInformationHeader");
		m_oLabelOtherInformationHeader.setVisible(false);
		this.attachChild(m_oLabelOtherInformationHeader);

		m_oLabelOtherInformation = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformation, "lblOtherInformation");
		m_oLabelOtherInformation.setVisible(false);
		this.attachChild(m_oLabelOtherInformation);

		m_oLabelOtherInformationHeader2 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformationHeader2, "lblOtherInformationHeader2");
		m_oLabelOtherInformationHeader2.setVisible(false);
		this.attachChild(m_oLabelOtherInformationHeader2);

		m_oLabelOtherInformation2 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformation2, "lblOtherInformation2");
		m_oLabelOtherInformation2.setVisible(false);
		this.attachChild(m_oLabelOtherInformation2);
		
		m_oLabelOtherInformationHeader3 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformationHeader3, "lblOtherInformationHeader3");
		m_oLabelOtherInformationHeader3.setVisible(false);
		this.attachChild(m_oLabelOtherInformationHeader3);

		m_oLabelOtherInformation3 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformation3, "lblOtherInformation3");
		m_oLabelOtherInformation3.setVisible(false);
		this.attachChild(m_oLabelOtherInformation3);
		
		m_oLabelOtherInformationHeader4 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformationHeader4, "lblOtherInformationHeader4");
		m_oLabelOtherInformationHeader4.setVisible(false);
		this.attachChild(m_oLabelOtherInformationHeader4);

		m_oLabelOtherInformation4 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformation4, "lblOtherInformation4");
		m_oLabelOtherInformation4.setVisible(false);
		this.attachChild(m_oLabelOtherInformation4);
		
		m_oLabelOtherInformation5 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformation5, "lblOtherInformation5");
		m_oLabelOtherInformation5.setVisible(false);
		this.attachChild(m_oLabelOtherInformation5);
		
		// get partial payment background color from xml
		m_oLabelPartialPaymentBgColor = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelPartialPaymentBgColor, "lblPartialPaymentBgColor");
		m_oLabelPartialPaymentBgColor.setVisible(false);
		
		m_bCurrentNeedAskPaymentAmount = false;
		m_bCurrentNeedAskTipsAmount = false;
		m_iCurrentEditSectionIndex = -1;
		m_iCurrentEditItemIndex = -1;
		m_iCurrentEditFieldIndex = -1;
		
		m_bIsCheckLoaded = bIsCheckLoaded;
		m_bIsClickByCashierMode = false;
		m_bIsFinishPaymentByAdjustPayment = false;
		
		//Rearrange the position for support surcharge
		
		if (bSupportSurcharge && !AppGlobal.g_oFuncStation.get().supportTaiwanGui()) {
			oFrameHeaderGroup.setHeight(oFrameHeaderGroup.getHeight() + 40);
			oFrameHeader.setHeight(oFrameHeader.getHeight() + 40);
			oFrameHeaderLine.setHeight(oFrameHeaderLine.getHeight() + 40);
			
			m_oCommonBasket.setTop(m_oCommonBasket.getTop() + 40);
			m_oButtonExit.setTop(m_oButtonExit.getTop() + 40);
			m_oButtonFinish.setTop(m_oButtonFinish.getTop() + 40);
			m_oButtonClose.setTop(m_oButtonClose.getTop() + 40);
			
			m_oLabelSurchargeHeader = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelSurchargeHeader, "lblSurchargeHeader");
			m_oLabelSurchargeHeader.setValue(AppGlobal.g_oLang.get()._("surcharge", ""));
			this.attachChild(m_oLabelSurchargeHeader);
			
			m_oLabelSurcharge = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelSurcharge, "lblSurcharge");
			this.attachChild(m_oLabelSurcharge);
			
			m_oLabelTotalDueHeader = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelTotalDueHeader, "lblTotalDueHeader");
			m_oLabelTotalDueHeader.setValue(AppGlobal.g_oLang.get()._("total_due", ""));
			this.attachChild(m_oLabelTotalDueHeader);
			
			m_oLabelTotalDue = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelTotalDue, "lblTotalDue");
			this.attachChild(m_oLabelTotalDue);
			
			VirtualUIFrame oFrameHeaderLine2 = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(oFrameHeaderLine2, "fraLabelHeader");
			oFrameHeaderLine2.setBackgroundColor("#DDDDDD");
			oFrameHeaderLine2.setHeight(3);
			oFrameHeaderLine2.setTop(m_oLabelSurchargeHeader.getTop());
			this.attachChild(oFrameHeaderLine2);
		}
	}
	
	public void resetNumpadHeight() {
		if (m_bHideNumberPad) {
			m_oFrameNumberPad.setCustomHeight(80);
			m_oFrameNumberPad.setTop(this.getHeight() - m_oFrameNumberPad.getCustomHeight());
			
			m_oButtonExit.setTop(m_oFrameNumberPad.getTop() - m_oButtonExit.getHeight() - 5);
		}
	}

	public void addPayment(int iSectionId, int iItemIndex, String sDesc, String sInfo1, String sInfo2, BigDecimal dPayAmt, BigDecimal dTips, BigDecimal dPaymentTotal, boolean bNeedAskAmount, boolean bNeedAskTips, boolean bDefaultPaymentTotal, boolean bOldPayment){
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		
		iFieldWidths.add(m_oCommonBasket.getWidth() / 4 * 2);
		sFieldValues.add(sDesc);
		sFieldAligns.add("");
		iFieldWidths.add(m_oCommonBasket.getWidth() / 4);
		
		String sValue;
		if(bDefaultPaymentTotal) {
			if(dPayAmt.compareTo(dPaymentTotal) > 0) {
				sValue = dPaymentTotal.stripTrailingZeros().toPlainString();
				dPayAmt = new BigDecimal(sValue);
			}else if(dPayAmt.compareTo(BigDecimal.ZERO) == 0)
				sValue = (BigDecimal.ZERO).stripTrailingZeros().toPlainString();
			else {
				if(bOldPayment) {
					sValue = dPaymentTotal.stripTrailingZeros().toPlainString();
					dPayAmt = new BigDecimal(sValue);
				}else
					sValue = dPayAmt.stripTrailingZeros().toPlainString();
			}
			m_dCurrentPaymentAmount = new BigDecimal(sValue);
		}else {
			if(dPayAmt.compareTo(BigDecimal.ZERO) == 0)
				sValue = (BigDecimal.ZERO).stripTrailingZeros().toPlainString();
			else
				sValue = dPayAmt.stripTrailingZeros().toPlainString();
		}
		sFieldValues.add(sValue);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
		iFieldWidths.add(m_oCommonBasket.getWidth()/4);
		if(dTips.compareTo(BigDecimal.ZERO) == 0)
			sValue = (BigDecimal.ZERO).stripTrailingZeros().toPlainString();
		else
			sValue = dTips.stripTrailingZeros().toPlainString();
		sFieldValues.add(sValue);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
		m_oCommonBasket.addItem(iSectionId, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
		
		m_oCommonBasket.setFieldTextSize(iSectionId, iItemIndex, 0, 16);
		m_oCommonBasket.setFieldTextSize(iSectionId, iItemIndex, 1, 16);
		m_oCommonBasket.setFieldTextSize(iSectionId, iItemIndex, 2, 16);
		m_oCommonBasket.setFieldPadding(iSectionId, iItemIndex, 0, "0,0,0,18");
		//m_oCommonBasket.setFieldPadding(iSectionId, iItemIndex, 1, "13,10,0,0");
		//m_oCommonBasket.setFieldPadding(iSectionId, iItemIndex, 2, "13,10,0,0");
		m_oCommonBasket.setAllFieldsForegroundColor(iSectionId, iItemIndex, "#0055B8");
		m_oCommonBasket.setAllFieldsBackgroundColor(iSectionId, iItemIndex, "#FFFFFF");
		
		// Add information
		if(sInfo1 != null && sInfo1.length() > 0){
			m_oCommonBasket.setFieldInfo1(iSectionId, iItemIndex, 0, sInfo1);
			if(sInfo2 != null && sInfo2.length() > 0){
				m_oCommonBasket.setFieldInfo2(iSectionId, iItemIndex, 0, sInfo2);
			}
		}
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oCommonBasket.setCashierCommonBasketHeight(180);
		else
			m_oCommonBasket.setCashierCommonBasketHeight(260);
		
		m_oCommonBasket.moveScrollToItem(iSectionId, iItemIndex);
		
		m_bCurrentNeedAskPaymentAmount = bNeedAskAmount;
		m_bCurrentNeedAskTipsAmount = bNeedAskTips;
		m_dOriginalPaymentAmount = dPayAmt;
		m_dCurrentPaymentAmount = dPayAmt;
		m_dCurrentTipsAmount = dTips;
		
		if(bNeedAskAmount){
			m_iCurrentPaymentStep = 1;
			VirtualUIBasicElement oElement = m_oCommonBasket.setEditField(iSectionId, iItemIndex, 1);
			oElement.setHeight(48);
			oElement.setBackgroundColor("#DDDDDD");
			m_oFrameNumberPad.clearEnterSubmitId();
			m_oFrameNumberPad.setEnterSubmitId(oElement);
		}else{
			if(bNeedAskTips){
				m_iCurrentPaymentStep = 2;
				VirtualUIBasicElement oElement = m_oCommonBasket.setEditField(iSectionId, iItemIndex, 2);
				oElement.setHeight(48);
				oElement.setBackgroundColor("#DDDDDD");
				m_oFrameNumberPad.clearEnterSubmitId();
				m_oFrameNumberPad.setEnterSubmitId(oElement);
			}else {
				m_iCurrentPaymentStep = 3;
				m_iCurrentEditSectionIndex = iSectionId;
				m_iCurrentEditItemIndex = iItemIndex;
				m_iCurrentEditFieldIndex = 1;
				FrameNumberPad_clickEnter();
			}
		}
		
		m_iCurrentEditSectionIndex = iSectionId;
		m_iCurrentEditItemIndex = iItemIndex;
		m_iCurrentEditFieldIndex = 1;
	}
	
	public void removePayment(int iSectionId, int iItemIndex){
		m_oCommonBasket.removeItem(iSectionId, iItemIndex);
		m_iCurrentEditItemIndex = m_oCommonBasket.getItemCount(iSectionId) - 1;
	}
	
	public void setCheckNo(String sCheckNo){
		m_oLabelCheckNo.setValue(sCheckNo);
	}
	
	public void setTable(String sTable){
		m_oLabelTable.setValue(sTable);
	}
	
	public void setTextboxCheckNo(String sCheckNo){
		m_oTextboxCheckNo.setValue(sCheckNo);
	}
	
	public void setTotal(String sTotal){
		m_oLabelTotal.setValue(sTotal);
	}
	
	public void setBalance(String sBalance){
		m_oLabelBalance.setValue(sBalance);
	}
	
	public void setSurcharge(String sSurcharge) {
		if(m_oLabelSurcharge == null)
			return;
		m_oLabelSurcharge.setValue(sSurcharge);
	}
	
	public void setTotalDue(String sTotalDue) {
		if(m_oLabelTotalDue == null)
			return;
		m_oLabelTotalDue.setValue(sTotalDue);
	}
	
	public void setCurrentStep(int iStep){
		m_iCurrentPaymentStep = iStep;
	}
	
	public int getCurrentStep(){
		return m_iCurrentPaymentStep;
	}
	
	public void setIsFinishPaymentByAdjustPayment(boolean bIsFinishPaymnetByAdjustPayment) {
		this.m_bIsFinishPaymentByAdjustPayment = bIsFinishPaymnetByAdjustPayment;
	}
	
	public boolean isFinishPaymentByAdjustPayment() {
		return this.m_bIsFinishPaymentByAdjustPayment;
	}

	public void setIsNeedCheckPaymentAmountByAuthAmount(boolean bIsNeedCheckPaymentAmountByAuthAmount) {
		this.m_bIsNeedCheckPaymentAmountByAuthAmount = bIsNeedCheckPaymentAmountByAuthAmount;
	}

	public boolean getIsNeedCheckPaymentAmountByAuthAmount() {
		return this.m_bIsNeedCheckPaymentAmountByAuthAmount;
	}

	public void setIsTipsInputDefaultZero(boolean bIsTipsInputDefaultZero) {
		this.m_bIsTipsInputDefaultZero = bIsTipsInputDefaultZero;
	}

	public boolean getIsTipsInputByUser() {
		return this.m_bIsTipsInputDefaultZero;
	}

	public void setIsNeedPaymentCardTopAuth(boolean bIsNeedPaymentCardTopAuth) {
		this.m_bIsNeedPaymentCardTopAuth = bIsNeedPaymentCardTopAuth;
	}

	public boolean getIsNeedPaymentCardTopAuth() {
		return this.m_bIsNeedPaymentCardTopAuth;
	}

	public void setPosPaymentGatewayPgtxId(int iPosPaymentGatewayPgtxId) {
		this.m_iPosPaymentGatewayPgtxId = iPosPaymentGatewayPgtxId;
	}

	public int getPosPaymentGatewayPgtxId() {
		return this.m_iPosPaymentGatewayPgtxId;
	}
	
	public void setIsCheckLoaded(boolean bIsCheckLoaded, boolean bFinishPayment){
		m_bIsCheckLoaded = bIsCheckLoaded;
		if(bIsCheckLoaded == false){
			// No need to clear header if for partial payment when the check has not yet settled
			if (!AppGlobal.g_oFuncStation.get().isPartialPayment() || 
					(AppGlobal.g_oFuncStation.get().isPartialPayment() && m_sPaidTotal.equals(m_sCheckTotal))){
				if(bFinishPayment)
					clearPaymentHeader(false); // clear Check No. Table, Total, Balance
				else
					clearPaymentHeader(true); // clear Check No. Table, Total, Balance
				m_oLabelCheckNo.setVisible(false);
				m_oLabelTable.setVisible(false);
				
				m_oTextboxCheckNo.setVisible(true);
				m_oFrameAskTable.setVisible(true);
			}
			
			// Add the textbox to number pad submit ID
			m_oFrameNumberPad.clearEnterSubmitId();
			m_oFrameNumberPad.setEnterSubmitId(this);
			
			if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				m_oLabelBalance.setBackgroundColor("#FFFFFF");
				m_oLabelTotal.setBackgroundColor("#FFFFFF");
				m_oLabelBalance.setHeight(m_oTextboxCheckNo.getHeight());
				m_oLabelTotal.setHeight(m_oTextboxCheckNo.getHeight());
				m_oLabelBalance.setTop(m_oTextboxCheckNo.getTop() + m_oTextboxCheckNo.getHeight() + 18);
				m_oLabelTotal.setTop(m_oFrameAskTable.getTop() + m_oFrameAskTable.getHeight() + 18);
				
				if(m_oLabelSurcharge != null) {
					m_oLabelSurcharge.setBackgroundColor("#FFFFFF");
					m_oLabelSurcharge.setHeight(m_oTextboxCheckNo.getHeight());
					m_oLabelSurcharge.setTop(m_oLabelTotal.getTop() + m_oLabelTotal.getHeight() + 16);
				}
				if(!bFinishPayment && m_oLabelTotalDue != null) {
					m_oLabelTotalDue.setBackgroundColor("#FFFFFF");
					m_oLabelTotalDue.setHeight(m_oTextboxCheckNo.getHeight());
					m_oLabelTotalDue.setTop(m_oLabelBalance.getTop() + m_oLabelBalance.getHeight() + 16);
				}
			}
		}else{
			m_oLabelCheckNo.setVisible(true);
			m_oLabelTable.setVisible(true);
			
			m_oTextboxCheckNo.setVisible(false);
			m_oFrameAskTable.setVisible(false);

			if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				m_oLabelBalance.setBackgroundColor("#00FFFFFF");
				m_oLabelTotal.setBackgroundColor("#00FFFFFF");
				m_oLabelBalance.setHeight(m_oLabelCheckNo.getHeight());
				m_oLabelTotal.setHeight(m_oLabelTable.getHeight());
				m_oLabelBalance.setTop(m_oLabelCheckNo.getTop() + m_oLabelCheckNo.getHeight());
				m_oLabelTotal.setTop(m_oLabelTable.getTop() + m_oLabelTable.getHeight());
				
				if(m_oLabelSurcharge != null) {
					m_oLabelSurcharge.setBackgroundColor("#00FFFFFF");
					m_oLabelSurcharge.setHeight(m_oLabelSurchargeHeader.getHeight());
					m_oLabelSurcharge.setTop(m_oLabelSurchargeHeader.getTop());
				}
				if(m_oLabelTotalDue != null) {
					m_oLabelTotalDue.setBackgroundColor("#00FFFFFF");
					m_oLabelTotalDue.setHeight(m_oLabelSurchargeHeader.getHeight());
					m_oLabelTotalDue.setTop(m_oLabelSurchargeHeader.getTop());
				}
			}
		}
	}
	
	public void setClickByCahsierMode(boolean bClickByCashierMode){
		m_bIsClickByCashierMode = bClickByCashierMode;
	}
	
	public boolean isClickByCahsierMode(){
		return m_bIsClickByCashierMode;
	}

	public void showOtherInformation(boolean bShow) {
		m_oLabelOtherInformationHeader.setVisible(bShow);
		m_oLabelOtherInformation.setVisible(bShow);
		m_oLabelOtherInformationHeader2.setVisible(bShow);
		m_oLabelOtherInformation2.setVisible(bShow);
		m_oLabelOtherInformationHeader.setVisible(bShow);
		m_oLabelOtherInformation.setVisible(bShow);
		m_oLabelOtherInformationHeader2.setVisible(bShow);
		m_oLabelOtherInformation2.setVisible(bShow);
		m_oLabelOtherInformationHeader3.setVisible(bShow);
		m_oLabelOtherInformation3.setVisible(bShow);
		m_oLabelOtherInformationHeader4.setVisible(bShow);
		m_oLabelOtherInformation4.setVisible(bShow);
		m_oLabelOtherInformation5.setVisible(bShow);
	}
	
	public void setOtherInformationHeader(String sHeader) {
		m_oLabelOtherInformationHeader.setValue(sHeader);
	}
	
	public void setOtherInformation(String sContent) {
		m_oLabelOtherInformation.setValue(sContent);
	}

	public void setOtherInformationHeader2(String sHeader) {
		m_oLabelOtherInformationHeader2.setValue(sHeader);
	}
	
	public void setOtherInformation2(String sContent) {
		m_oLabelOtherInformation2.setValue(sContent);
	}
	
	public void setOtherInformationHeader3(String sHeader) {
		m_oLabelOtherInformationHeader3.setValue(sHeader);
	}
	
	public void setOtherInformation3(String sContent) {
		m_oLabelOtherInformation3.setValue(sContent);
	}
	
	public void setOtherInformationHeader4(String sHeader) {
		m_oLabelOtherInformationHeader4.setValue(sHeader);
	}
	
	public void setOtherInformation4(String sContent) {
		m_oLabelOtherInformation4.setValue(sContent);
	}
	
	public void setOtherInformation5(String sContent) {
		m_oLabelOtherInformation5.setValue(sContent);
	}
	
	public String getOtherInformation() {
		return m_oLabelOtherInformation.getValue();
	}
	
	public void updateBasketPaymentAmount(int iSectionId, int iItemIndex, String sPayAmt){
		m_oCommonBasket.setFieldValue(iSectionId, iItemIndex, 1, sPayAmt);
	}
	
	public void updateBasketPaymentTips(int iSectionId, int iItemIndex, String sTips){
		m_oCommonBasket.setFieldValue(iSectionId, iItemIndex, 2, sTips);
	}
	
	public String getBastketPaymentTips(int iSectionId, int iItemIndex) {
		return m_oCommonBasket.getFieldValue(iSectionId, iItemIndex, 2);
	}
	
	public void updateBasketPaymentName(int iSectionId, int iItemIndex, String sName) {
		m_oCommonBasket.setFieldValue(iSectionId, iItemIndex, 0, sName);
	}
	
	public void clearPaymentBasket(){
		m_oCommonBasket.removeAllSections();
		m_oCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		m_oCommonBasket.setUpperUnderLineShow(false);
		m_oCommonBasket.setCashierCommonBasketHeight(260);
	}
	
	public void clearPaymentHeader(boolean bCleanupTotalDue){
		this.setTable("");
		this.setCheckNo("");
		this.setTextboxCheckNo("");
		this.setTotal("");
		this.setBalance("");
		this.setSurcharge("");
		if(bCleanupTotalDue)
			this.setTotalDue("");
		
		m_oTextboxCheckNo.setFocus();
	}

	public void showPaymentResult(boolean bShow, String sCheckNo, String sTable, String sTableExtension, String sCheckTotal, String sPaidTotal, String sUnpaidTotal, String sTipsTotal, String sSurchargeTotal, String sResidueTotal, String sChangeTotal, boolean bChangeInForeignCurrency, String[] sCurrencyName, String sCurrencySign , boolean bShowPaidAndUnpaidCheckTotal){
		if(bShow){
			int iIndex = m_iCurrentEditItemIndex + 1;
			
			ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
			ArrayList<String[]> sFieldValues = new ArrayList<String[]>();
			ArrayList<String> sFieldAligns = new ArrayList<String>();
			
			iFieldWidths.add(m_oCommonBasket.getWidth());
			sFieldValues.add(AppGlobal.g_oLang.get()._("pay_successfully", ""));
			
			sFieldAligns.add("");
			m_oCommonBasket.addItem(0, iIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
			
			m_oCommonBasket.setFieldBackgroundColor(0, iIndex, 0, "#E1ECF8");
			m_oCommonBasket.setFieldForegroundColor(0, iIndex, 0, "#333333");
			m_oCommonBasket.setFieldTextSize(0, iIndex, 0, 16);
			m_oCommonBasket.setFieldPadding(0, iIndex, 0, "0,0,0,18");
			
			iIndex++;
			iFieldWidths.clear();
			sFieldValues.clear();
			sFieldAligns.clear();
			iFieldWidths.add(m_oCommonBasket.getWidth()/2);
			sFieldValues.add(AppGlobal.g_oLang.get()._("check_no", ""));
			
			sFieldAligns.add("");
			iFieldWidths.add(m_oCommonBasket.getWidth()/2);
			sFieldValues.add(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, sCheckNo));
			
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
			m_oCommonBasket.addItem(0, iIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
			
			m_oCommonBasket.setFieldTextSize(0, iIndex, 0, 16);
			m_oCommonBasket.setFieldTextSize(0, iIndex, 1, 16);
			m_oCommonBasket.setFieldPadding(0, iIndex, 0, "0,0,0,18");
			// Set Upperline
			m_oCommonBasket.setUpperUnderLineShow(false);
			m_oCommonBasket.setAllFieldsForegroundColor(0, iIndex, "#999999");
			m_oCommonBasket.setAllFieldsBackgroundColor(0, iIndex, "#FFFFFF");
			
			iIndex++;
			iFieldWidths.clear();
			sFieldValues.clear();
			sFieldAligns.clear();
			iFieldWidths.add(m_oCommonBasket.getWidth()/2);
			sFieldValues.add(AppGlobal.g_oLang.get()._("total", ""));
			sFieldAligns.add("");
			iFieldWidths.add(m_oCommonBasket.getWidth()/2);
			sFieldValues.add(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, sCheckTotal));
			
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
			m_oCommonBasket.addItem(0, iIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
			
			m_oCommonBasket.setFieldTextSize(0, iIndex, 0, 16);
			m_oCommonBasket.setFieldTextSize(0, iIndex, 1, 16);
			m_oCommonBasket.setFieldPadding(0, iIndex, 0, "0,0,0,18");
			m_oCommonBasket.setAllFieldsForegroundColor(0, iIndex, "#999999");
			m_oCommonBasket.setAllFieldsBackgroundColor(0, iIndex, "#FFFFFF");
			
			// show paid and unpaid check total for partial payment
			if (AppGlobal.g_oFuncStation.get().isPartialPayment()) {
				m_sCheckTotal = sCheckTotal;
				m_sPaidTotal = sPaidTotal;
				
				if(bShowPaidAndUnpaidCheckTotal) {
					iIndex++;
					iFieldWidths.clear();
					sFieldValues.clear();
					sFieldAligns.clear();
					iFieldWidths.add(m_oCommonBasket.getWidth()/2);
					sFieldValues.add(AppGlobal.g_oLang.get()._("paid_check_total", ""));
					sFieldAligns.add("");
					iFieldWidths.add(m_oCommonBasket.getWidth()/2);
					sFieldValues.add(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, sPaidTotal));
					sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
					m_oCommonBasket.addItem(0, iIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
					m_oCommonBasket.setFieldTextSize(0, iIndex, 0, 16);
					m_oCommonBasket.setFieldTextSize(0, iIndex, 1, 16);
					
					m_oCommonBasket.setFieldPadding(0, iIndex, 0, "0,0,0,18");
					m_oCommonBasket.setAllFieldsForegroundColor(0, iIndex, "#999999");
					m_oCommonBasket.setAllFieldsBackgroundColor(0, iIndex, "#FFFFFF");
					
					iIndex++;
					iFieldWidths.clear();
					sFieldValues.clear();
					sFieldAligns.clear();
					iFieldWidths.add(m_oCommonBasket.getWidth()/2);
					sFieldValues.add(AppGlobal.g_oLang.get()._("unpaid_check_total", ""));
					sFieldAligns.add("");
					iFieldWidths.add(m_oCommonBasket.getWidth()/2);
					sFieldValues.add(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, sUnpaidTotal));
					sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
					m_oCommonBasket.addItem(0, iIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
					m_oCommonBasket.setFieldTextSize(0, iIndex, 0, 16);
					m_oCommonBasket.setFieldTextSize(0, iIndex, 1, 16);
					
					m_oCommonBasket.setFieldPadding(0, iIndex, 0, "0,0,0,18");
					m_oCommonBasket.setAllFieldsForegroundColor(0, iIndex, "#999999");
					m_oCommonBasket.setAllFieldsBackgroundColor(0, iIndex, "#FFFFFF");
				}
			}
			
			iIndex++;
			iFieldWidths.clear();
			sFieldValues.clear();
			sFieldAligns.clear();
			iFieldWidths.add(m_oCommonBasket.getWidth()/2);
			sFieldValues.add(AppGlobal.g_oLang.get()._("tips", ""));
			sFieldAligns.add("");
			iFieldWidths.add(m_oCommonBasket.getWidth()/2);
			sFieldValues.add(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, sTipsTotal));
			
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
			m_oCommonBasket.addItem(0, iIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
			
			m_oCommonBasket.setFieldTextSize(0, iIndex, 0, 16);
			m_oCommonBasket.setFieldTextSize(0, iIndex, 1, 16);
			
			m_oCommonBasket.setFieldPadding(0, iIndex, 0, "0,0,0,18");
			m_oCommonBasket.setAllFieldsForegroundColor(0, iIndex, "#999999");
			m_oCommonBasket.setAllFieldsBackgroundColor(0, iIndex, "#FFFFFF");
			
			BigDecimal dSurchargeTotal = new BigDecimal(sSurchargeTotal);
			if(dSurchargeTotal.compareTo(BigDecimal.ZERO) != 0) {
				iIndex++;
				iFieldWidths.clear();
				sFieldValues.clear();
				sFieldAligns.clear();
				iFieldWidths.add(m_oCommonBasket.getWidth()/2);
				sFieldValues.add(AppGlobal.g_oLang.get()._("surcharge", ""));
				sFieldAligns.add("");
				iFieldWidths.add(m_oCommonBasket.getWidth()/2);
				sFieldValues.add(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, sSurchargeTotal));
				
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
				m_oCommonBasket.addItem(0, iIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
				
				m_oCommonBasket.setFieldTextSize(0, iIndex, 0, 16);
				m_oCommonBasket.setFieldTextSize(0, iIndex, 1, 16);
				
				m_oCommonBasket.setFieldPadding(0, iIndex, 0, "0,0,0,18");
				m_oCommonBasket.setAllFieldsForegroundColor(0, iIndex, "#999999");
				m_oCommonBasket.setAllFieldsBackgroundColor(0, iIndex, "#FFFFFF");
			}
			
			BigDecimal dResidueTotal = new BigDecimal(sResidueTotal);
			if(dResidueTotal.compareTo(BigDecimal.ZERO) != 0) {
				iIndex++;
				iFieldWidths.clear();
				sFieldValues.clear();
				sFieldAligns.clear();
				iFieldWidths.add(m_oCommonBasket.getWidth()/2);
				sFieldValues.add(AppGlobal.g_oLang.get()._("residue", ""));
				sFieldAligns.add("");
				iFieldWidths.add(m_oCommonBasket.getWidth()/2);
				sFieldValues.add(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, sResidueTotal));
				
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
				m_oCommonBasket.addItem(0, iIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
				
				m_oCommonBasket.setFieldTextSize(0, iIndex, 0, 16);
				m_oCommonBasket.setFieldTextSize(0, iIndex, 1, 16);
				
				
				m_oCommonBasket.setFieldPadding(0, iIndex, 0, "0,0,0,18");
				m_oCommonBasket.setAllFieldsForegroundColor(0, iIndex, "#999999");
				m_oCommonBasket.setAllFieldsBackgroundColor(0, iIndex, "#FFFFFF");
				
			}
			
			iIndex++;
			iFieldWidths.clear();
			sFieldValues.clear();
			sFieldAligns.clear();
			iFieldWidths.add(m_oCommonBasket.getWidth()/2);
			if(bChangeInForeignCurrency)
				sFieldValues.add(AppGlobal.g_oLang.get()._("change", " [", sCurrencyName, "]"));
			else
				sFieldValues.add(AppGlobal.g_oLang.get()._("change", ""));
			sFieldAligns.add("");
			iFieldWidths.add(m_oCommonBasket.getWidth()/2);
			if(bChangeInForeignCurrency && !sCurrencySign.isEmpty())
				sFieldValues.add(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, sCurrencySign+sChangeTotal));
			else
				sFieldValues.add(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, sChangeTotal));
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
			m_oCommonBasket.addItem(0, iIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);

			m_oCommonBasket.setFieldTextSize(0, iIndex, 0, 18);
			m_oCommonBasket.setFieldTextSize(0, iIndex, 1, 18);
			m_oCommonBasket.setFieldPadding(0, iIndex, 0, "0,0,0,18");
			m_oCommonBasket.setFieldBackgroundColor(0, iIndex, 0, "#E1ECF8");
			m_oCommonBasket.setFieldBackgroundColor(0, iIndex, 1, "#E1ECF8");
			m_oCommonBasket.setFieldForegroundColor(0, iIndex, 0, "#333333");
			m_oCommonBasket.setFieldForegroundColor(0, iIndex, 1, "#333333");
			m_oCommonBasket.setCashierCommonBasketHeight(260);

			m_oCommonBasket.setUpperUnderLineShow(false);
			
			m_oCommonBasket.moveScrollToBottom();
		}else{
			this.clearPaymentBasket();
		}
		//if (!sChangeTotal.equals(""))
		//	this.doSurveillanceEventForChange(sChangeTotal);
	
	}
	
	private void doSurveillanceEventForChange(String sChangeTotal) {
		double dChangeTotal = 0;
		dChangeTotal = Double.parseDouble(sChangeTotal);
		
		if(dChangeTotal == 0) 
			return;
		
		List<PosInterfaceConfig> oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_SURVEILLANCE_INTERFACE);
		if (!oInterfaceConfigList.isEmpty()) {
			for (PosInterfaceConfig oPosInterfaceConfig : oInterfaceConfigList) {
				if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ECONNECT)) {
					FuncSurveillance oFuncSurveillance = new FuncSurveillance(oPosInterfaceConfig);
					HashMap<String, String> oSurveillanceEventInfo = new HashMap<String, String>();
					oSurveillanceEventInfo.put("checkNum", m_oLabelCheckNo.getValue());
					oSurveillanceEventInfo.put("eventType", FuncSurveillance.SURVEILLANCE_TYPE_CHANGE_DUE);
					oSurveillanceEventInfo.put("amount",String.valueOf(dChangeTotal));
					oFuncSurveillance.surveillanceEvent(oSurveillanceEventInfo, null);
				}
			}
		}
	}

	public void showPartialPaymentButton(boolean bShow) {
		this.m_oButtonFinish.setVisible(bShow);
		this.m_oButtonClose.setVisible(bShow);
	}
	
	public void clearEditField(){
		m_oCommonBasket.clearEditField();
	}
	
	public void clearCheckAndPaidTotal() {
		m_sCheckTotal = "";
		m_sPaidTotal = "";
	}
	
	public void updatePartialPaymentBasketFieldBackgroundColor(int iSectionId, int iItemIndex){
		for (int i = 0; i <= 2 ; i++ ) 
			m_oCommonBasket.setFieldBackgroundColor(0, iItemIndex, i, m_oLabelPartialPaymentBgColor.getBackgroundColor());
	}
	
	public void updatePartialPaymentInfo1BackgroundColor(int iSectionId, int iItemIndex){
		for (int i = 0; i <= 2 ; i++ ) 
			m_oCommonBasket.setFieldInfo1BackgroundColor(0, iItemIndex, i, m_oLabelPartialPaymentBgColor.getBackgroundColor());
	}
	
	public void updatePartialPaymentInfo2BackgroundColor(int iSectionId, int iItemIndex){
		for (int i = 0; i <= 2 ; i++ ) 
			m_oCommonBasket.setFieldInfo2BackgroundColor(0, iItemIndex, i, m_oLabelPartialPaymentBgColor.getBackgroundColor());
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		if(iChildId == m_oButtonExit.getId()) {					// Previous button clicked
			for(FrameCashierListener listener : listeners) {
				listener.FrameCashier_exit();
			}
			return true;
		}else
		if(iChildId == m_oFrameAskTable.getId()){
			for (FrameCashierListener listener : listeners) {
				listener.FrameCashier_loadTable();
			}
			return true;
		}
		else
		if(iChildId == m_oButtonFinish.getId() || iChildId == m_oButtonClose.getId()){
			String sErrMsg = "";
			//check if payment or tips are added 
			if (m_iCurrentPaymentStep == 1 || m_iCurrentPaymentStep == 2)
				sErrMsg = AppGlobal.g_oLang.get()._("please_confirm_the_payment_and_tips_amount");
			//check if completed partial payment process
			if (m_iCurrentPaymentStep == 4)
				sErrMsg = AppGlobal.g_oLang.get()._(AppGlobal.g_oLang.get()._("action_not_allowed")
						+ System.lineSeparator() +
						AppGlobal.g_oLang.get()._("please_exit_check"));
			
			if (!sErrMsg.isEmpty()){
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
						this.getParentForm());
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
				oFormDialogBox.setMessage(sErrMsg);
				oFormDialogBox.show();
				return false;
			}
			
			//trigger save payments
			String sTriggerBy = FrameCashier.TRIGGER_BY_FINISH_PAYMENT_SELECTION;
			if(iChildId == m_oButtonClose.getId())
				sTriggerBy = FrameCashier.TRIGGER_BY_CLOSE_CHECK;
			for (FrameCashierListener listener : listeners) {
				listener.FrameCashier_doPartialPayment(sTriggerBy);
			}
			return true;
		}
			
		return false;
	}

	@Override
	public void FrameNumberPad_clickEnter() {
		String sCheckNo = m_oLabelCheckNo.getValue();
		if(m_bIsCheckLoaded == false){
			// Check if check no. or table is click
			if(m_oTextboxCheckNo.getValue().length() > 0){
				// Fire event to FormMain to handle load check
				for (FrameCashierListener listener : listeners) {
					// Raise the event to parent
					listener.FrameCashier_loadCheck(m_oTextboxCheckNo.getValue());
				}
			}
		}else{
			if(m_iCurrentPaymentStep == 1){
				// Press enter to confirm payment amount
				if(m_oCommonBasket.getEditFieldValue().isEmpty() == false){
					m_dCurrentPaymentAmount = new BigDecimal(m_oCommonBasket.getEditFieldValue());
					
					// For the auth payment, program need to check the input payment amount cannot greater than the auth amount
					if (m_bIsNeedCheckPaymentAmountByAuthAmount) {
						if((m_dCurrentPaymentAmount.compareTo(m_dOriginalPaymentAmount)) > 0){
							FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"),
									AppGlobal.g_oLang.get()._("no"), super.getParentForm());
							oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("message"));
							oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("payment_amount") + " : " + "$"
									+ AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(m_dCurrentPaymentAmount)
									+ System.lineSeparator() + AppGlobal.g_oLang.get()._("cc_auth_total") + " : " + "$"
									+ AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(m_dOriginalPaymentAmount)
									+ System.lineSeparator() + AppGlobal.g_oLang.get()._("tips") + " : " + "$"
									+ AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(m_dCurrentPaymentAmount.subtract(m_dOriginalPaymentAmount))
									+ System.lineSeparator() + System.lineSeparator()
									+ AppGlobal.g_oLang.get()._("confirm") + "?");
							oFormConfirmBox.show();
							if (oFormConfirmBox.isOKClicked() == false)
								return;
						}
						setIsNeedCheckPaymentAmountByAuthAmount(false);
					}
					
					String sValue;
					if(m_dCurrentPaymentAmount.compareTo(BigDecimal.ZERO) == 0)
						sValue = BigDecimal.ZERO.stripTrailingZeros().toPlainString();
					else
						sValue = m_dCurrentPaymentAmount.stripTrailingZeros().toPlainString();
					//for negative check, change payment amount to negative
					if(m_dOriginalPaymentAmount.signum() == -1 && m_dCurrentPaymentAmount.signum() != -1)
						sValue = new BigDecimal(sValue).negate().toPlainString();
					m_oCommonBasket.setFieldValue(m_iCurrentEditSectionIndex, m_iCurrentEditItemIndex, 1, sValue);
				}
				m_oCommonBasket.clearEditField();
				
				if(m_dOriginalPaymentAmount.signum() == -1 && m_dCurrentPaymentAmount.signum() != -1)
					m_dCurrentPaymentAmount = m_dCurrentPaymentAmount.negate();
				
				if(m_bCurrentNeedAskTipsAmount){
					m_iCurrentPaymentStep = 2;
					
					if(m_dOriginalPaymentAmount.signum() != -1){
						//positive check comparison
						if((m_dCurrentPaymentAmount.compareTo(m_dOriginalPaymentAmount)) > 0){
							m_dCurrentTipsAmount = m_dCurrentPaymentAmount.subtract(m_dOriginalPaymentAmount);
							m_dCurrentPaymentAmount = m_dOriginalPaymentAmount;
							String sValue;
							if(m_dCurrentPaymentAmount.compareTo(BigDecimal.ZERO) == 0)
								sValue = BigDecimal.ZERO.stripTrailingZeros().toPlainString();
							else
								sValue = m_dCurrentPaymentAmount.stripTrailingZeros().toPlainString();
							m_oCommonBasket.setFieldValue(m_iCurrentEditSectionIndex, m_iCurrentEditItemIndex, 1, sValue);
							if(m_dCurrentTipsAmount.compareTo(BigDecimal.ZERO) == 0)
								sValue = BigDecimal.ZERO.stripTrailingZeros().toPlainString();
							else
								sValue = m_dCurrentTipsAmount.stripTrailingZeros().toPlainString();
							m_oCommonBasket.setFieldValue(m_iCurrentEditSectionIndex, m_iCurrentEditItemIndex, 2, sValue);
						}
					}else{
						//negative check comparison
						if((m_dCurrentPaymentAmount.compareTo(m_dOriginalPaymentAmount)) < 0){
	 
							m_dCurrentTipsAmount = m_dCurrentPaymentAmount.subtract(m_dOriginalPaymentAmount);
							m_dCurrentPaymentAmount = m_dOriginalPaymentAmount;
							String sValue;
							if(m_dCurrentPaymentAmount.compareTo(BigDecimal.ZERO) == 0)
								sValue = BigDecimal.ZERO.stripTrailingZeros().toPlainString();
							else
								sValue = m_dCurrentPaymentAmount.stripTrailingZeros().toPlainString();
							m_oCommonBasket.setFieldValue(m_iCurrentEditSectionIndex, m_iCurrentEditItemIndex, 1, sValue);
							if(m_dCurrentTipsAmount.compareTo(BigDecimal.ZERO) == 0)
								sValue = BigDecimal.ZERO.stripTrailingZeros().toPlainString();
							else
								sValue = m_dCurrentTipsAmount.stripTrailingZeros().toPlainString();
							m_oCommonBasket.setFieldValue(m_iCurrentEditSectionIndex, m_iCurrentEditItemIndex, 2, sValue);
						}
					}

					if (!m_bIsTipsInputDefaultZero) {
						if(m_dOriginalPaymentAmount.signum() != -1){
							//positive check comparison
							if((m_dCurrentPaymentAmount.compareTo(m_dOriginalPaymentAmount)) > 0){
								m_dCurrentTipsAmount = m_dCurrentPaymentAmount.subtract(m_dOriginalPaymentAmount);
								m_dCurrentPaymentAmount = m_dOriginalPaymentAmount;
								String sValue;
								if(m_dCurrentPaymentAmount.compareTo(BigDecimal.ZERO) == 0)
									sValue = BigDecimal.ZERO.stripTrailingZeros().toPlainString();
								else
									sValue = m_dCurrentPaymentAmount.stripTrailingZeros().toPlainString();
								m_oCommonBasket.setFieldValue(m_iCurrentEditSectionIndex, m_iCurrentEditItemIndex, 1, sValue);
								if(m_dCurrentTipsAmount.compareTo(BigDecimal.ZERO) == 0)
									sValue = BigDecimal.ZERO.stripTrailingZeros().toPlainString();
								else
									sValue = m_dCurrentTipsAmount.stripTrailingZeros().toPlainString();
								m_oCommonBasket.setFieldValue(m_iCurrentEditSectionIndex, m_iCurrentEditItemIndex, 2, sValue);
							}
						}else{
							//negative check comparison
							if((m_dCurrentPaymentAmount.compareTo(m_dOriginalPaymentAmount)) < 0){
								m_dCurrentTipsAmount = m_dCurrentPaymentAmount.subtract(m_dOriginalPaymentAmount);
								m_dCurrentPaymentAmount = m_dOriginalPaymentAmount;
								String sValue;
								if(m_dCurrentPaymentAmount.compareTo(BigDecimal.ZERO) == 0)
									sValue = BigDecimal.ZERO.stripTrailingZeros().toPlainString();
								else
									sValue = m_dCurrentPaymentAmount.stripTrailingZeros().toPlainString();
								m_oCommonBasket.setFieldValue(m_iCurrentEditSectionIndex, m_iCurrentEditItemIndex, 1, sValue);
								if(m_dCurrentTipsAmount.compareTo(BigDecimal.ZERO) == 0)
									sValue = BigDecimal.ZERO.stripTrailingZeros().toPlainString();
								else
									sValue = m_dCurrentTipsAmount.stripTrailingZeros().toPlainString();
								m_oCommonBasket.setFieldValue(m_iCurrentEditSectionIndex, m_iCurrentEditItemIndex, 2, sValue);
							}
						}
					}
					
					setIsTipsInputDefaultZero(false);
					VirtualUIBasicElement oElement = m_oCommonBasket.setEditField(m_iCurrentEditSectionIndex, m_iCurrentEditItemIndex, 2);
					m_oFrameNumberPad.clearEnterSubmitId();
					m_oFrameNumberPad.setEnterSubmitId(oElement);
				}else
					m_iCurrentPaymentStep = 3;
			}else
			if(m_iCurrentPaymentStep == 2){
				// Press enter to confirm tips amount
				if(m_oCommonBasket.getEditFieldValue().isEmpty() == false){
					m_dCurrentTipsAmount = new BigDecimal(m_oCommonBasket.getEditFieldValue());

					//for negative check, change tips amount to negative
					if(m_dOriginalPaymentAmount.signum() == -1 && m_dCurrentTipsAmount.signum() != -1)
						m_dCurrentTipsAmount = m_dCurrentTipsAmount.negate();
						
					boolean bPass = false;
					//Fire event to FormMain to handle the tips input
					for (FrameCashierListener listener : listeners) {
						// Raise the event to parent
						bPass = listener.FrameCashier_askTipsConfirmation(m_dCurrentPaymentAmount, m_dCurrentTipsAmount);
						// For card payment auth, if input payment amount and tips amount greater than original auth amount
						// program will do top-up auth transaction with exceed amount from tips
						if ((bPass) && (m_bIsNeedPaymentCardTopAuth)) {
							if (getInputAmountAndTips().compareTo(m_dOriginalPaymentAmount) > 0) {
								BigDecimal dCurrentTipsAmount = m_dCurrentTipsAmount;
								bPass = listener.FrameCashier_doPaymentCardTopUpAuthorization(getPosPaymentGatewayPgtxId(), getExceedTipsAmountForTopUp(), getInputAmountAndTips());
								setIsNeedPaymentCardTopAuth(false);
								// set the payment tips field if the tips is changed
								if (m_dCurrentTipsAmount.compareTo(dCurrentTipsAmount) != 0) {
									m_oCommonBasket.setFieldValue(m_iCurrentEditSectionIndex, m_iCurrentEditItemIndex,
											1, StringLib.BigDecimalToString(m_dCurrentTipsAmount,
													AppGlobal.g_oFuncOutlet.get().getBusinessDay().getPayDecimal()));
								}
								//bPass = false;
								if (!bPass) {
									listener.FrameCashier_cancel(true);
									listener.FrameCashier_exit();
								}
							}
						}
						if(bPass == false)
							break;
					}
					if(bPass == false)
						return;
					
					String sValue;
					if(m_dCurrentTipsAmount.compareTo(BigDecimal.ZERO) == 0)
						sValue = BigDecimal.ZERO.stripTrailingZeros().toPlainString();
					else
						sValue = m_dCurrentTipsAmount.stripTrailingZeros().toPlainString();
					m_oCommonBasket.setFieldValue(m_iCurrentEditSectionIndex, m_iCurrentEditItemIndex, 2, sValue);
				}
				m_oCommonBasket.clearEditField();
				m_iCurrentPaymentStep = 3;
			}
			if(m_iCurrentPaymentStep == 3){
				// Finish asking value
				// Fire event to FormMain to handle the payment
				for (FrameCashierListener listener : listeners) {
					// Raise the event to parent
					listener.FrameCashier_finishAskAmount(m_iCurrentEditSectionIndex, m_iCurrentEditItemIndex, m_dCurrentPaymentAmount, m_dCurrentTipsAmount);
					listener.FrameCashier_addDefaultPaymentForActivePosPaymentGatewayTransaction();
				}
			}
		}
	}
	
	@Override
	public void FrameNumberPad_clickCancel() {
		for (FrameCashierListener listener : listeners) {
			// Raise the event to parent
			listener.FrameCashier_cancel(false);
		}
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId,
			String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex,
			int iItemIndex, int iFieldIndex, String sNote) {
		if(m_iCurrentPaymentStep != 1 && m_iCurrentPaymentStep != 2){
			for (FrameCashierListener listener : listeners) {
				// Raise the event to parent
					listener.FrameCashier_clickBasketPayment(iSectionIndex, iItemIndex);
			}
		}
	}

	@Override
	public void FrameNumberPad_clickNumber(String string) {
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId,
			int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
	}
	
	private BigDecimal getInputAmountAndTips() {
		BigDecimal dRetValue = BigDecimal.ZERO;
		
		dRetValue = dRetValue.add(m_dCurrentPaymentAmount);
		dRetValue = dRetValue.add(m_dCurrentTipsAmount);
		
		return dRetValue;
	}
	
	private BigDecimal getExceedTipsAmountForTopUp() {
		BigDecimal dRetValue = BigDecimal.ZERO;
		
		dRetValue = dRetValue.add(getInputAmountAndTips());
		dRetValue = dRetValue.subtract(m_dOriginalPaymentAmount);
		
		return dRetValue;
	}
	
	public void setCurrentTipsAmount(BigDecimal dCurrentTipsAmount) {
		m_dCurrentTipsAmount = dCurrentTipsAmount;
	}
	
	public boolean isCheckLoaded() {
		return this.m_bIsCheckLoaded;
	}
	
}
