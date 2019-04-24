package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import commonui.FormSelectionBox;
import commonui.FrameNumberPad;
import commonui.FrameNumberPadListener;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import om.PosCheckPayment;
import om.PosPaymentMethod;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method **/
interface FrameAdjustTipsListener {
	void frameAdjustTips_ClickExit();
	void frameAdjustTips_ClickSave(List<HashMap<String, String>> oPaymentInfos);
	boolean frameAdjustTips_AskTipsConfirmation(BigDecimal dPaymentAmount, BigDecimal dTips);
	boolean frameAdjustTips_CheckTipsPercentage(BigDecimal dTipsPercentage);
}

public class FrameAdjustTips extends VirtualUIFrame implements FrameCommonBasketListener, FrameNumberPadListener, FrameTitleHeaderListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameCommonBasket m_oCommonBasketPaymentList;
	private VirtualUILabel m_oLabelCheckInfo;
	private VirtualUIFrame m_oFrameCheckInfoUnderlineBottom;
	private VirtualUILabel m_oLabelCheckNumberLabel;
	private VirtualUILabel m_oLabelCheckNumber;
	private VirtualUILabel m_oLabelTableNumberLabel;
	private VirtualUILabel m_oLabelTableNumber;
	private VirtualUILabel m_oLabelCheckCoverLabel;
	private VirtualUILabel m_oLabelCheckCover;
	private VirtualUILabel m_oLabelCheckTotalLabel;
	private VirtualUILabel m_oLabelCheckTotal;
	private VirtualUIButton m_oButtonSave;
	private FrameNumberPad m_oFrameNumberPad;
	
	private FrameTitleHeader m_oTitleHeader;
	
	private HashMap<String, Integer> m_oPaymentListWidths;
	private int m_iPaymentListHeight;
	private List<HashMap<String, String>> m_oPaymentInfos;
	private int m_iCurrentSectionId;
	private int m_iCurrentItemIndex;
	
	private int m_iSelectedColumnIndex = 1;
	
	//private static final String TIPS_INPUT_TYPE_DOLLAR = "$";
	private static final String TIPS_INPUT_TYPE_PERCENTAGE = "%";
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameAdjustTipsListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameAdjustTipsListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameAdjustTipsListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
	
	public FrameAdjustTips(List<HashMap<String, String>> oInfos, String sCheckPrefixNo, int iCheckGuest, BigDecimal dCheckTotal, String sTable) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameAdjustTipsListener>();
		m_oPaymentListWidths = new HashMap<String, Integer>();
		m_oPaymentInfos = oInfos;
		m_iCurrentSectionId = 0;
		m_iCurrentItemIndex = -1;
		
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
//KingsleyKwan20171013ByKing	-----Start-----
			m_oPaymentListWidths.put("paymentMethod", 150);
			m_oPaymentListWidths.put("tips", 120);
			m_oPaymentListWidths.put("tipsAmount", 120);
			m_oPaymentListWidths.put("tipsInputMode", 30);
			m_oPaymentListWidths.put("amount", 180);
			m_oPaymentListWidths.put("currency", 50);
			m_iPaymentListHeight = 50;
		}else {
			m_oPaymentListWidths.put("paymentMethod", 200);
			m_oPaymentListWidths.put("tips", 120);
			m_oPaymentListWidths.put("tipsAmount", 100);
			m_oPaymentListWidths.put("tipsInputMode", 50);
// Edit by King Cheung 2017-11-02
			m_oPaymentListWidths.put("amount", 150);
//KingsleyKwan20171013ByKing	----- End -----
// Edit by King Cheung 2017-11-02
			m_oPaymentListWidths.put("currency", 120);
			m_iPaymentListHeight = 0;
		}
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraAdjustTips.xml");
//KingsleyKwan20171013ByKing	-----Start-----
		
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		m_oTitleHeader.addListener(this);
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("adjust_tips"));
		this.attachChild(m_oTitleHeader);
		
		VirtualUIFrame oFrameLeft= new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameLeft, "fraLeftPanel");
		this.attachChild(oFrameLeft);
		
		// Payment list
		m_oCommonBasketPaymentList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oCommonBasketPaymentList, "fraPaymentsBasket");
		m_oCommonBasketPaymentList.init();
		m_oCommonBasketPaymentList.addListener(this);
		addPaymentListHeader();
		addPaymentRecords();
		oFrameLeft.attachChild(m_oCommonBasketPaymentList);
		
		VirtualUIFrame oFrameRight= new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameRight, "fraRightPanel");
		this.attachChild(oFrameRight);
		
		// Check info header
		m_oLabelCheckInfo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckInfo, "lblCheckInfo");
		m_oLabelCheckInfo.setValue(AppGlobal.g_oLang.get()._("check_information"));
		oFrameRight.attachChild(m_oLabelCheckInfo);
		
		// Check info underline
		m_oFrameCheckInfoUnderlineBottom = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCheckInfoUnderlineBottom, "lblCheckInfoUnderLineBottom");
		oFrameRight.attachChild(m_oFrameCheckInfoUnderlineBottom);
		
		// Check Number
		m_oLabelCheckNumberLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckNumberLabel, "lblCheckNumberLabel");
		m_oLabelCheckNumberLabel.setValue(AppGlobal.g_oLang.get()._("check_no"));
		oFrameRight.attachChild(m_oLabelCheckNumberLabel);
		m_oLabelCheckNumber = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckNumber, "lblCheckNumber");
		m_oLabelCheckNumber.setValue(sCheckPrefixNo);
		oFrameRight.attachChild(m_oLabelCheckNumber);
		
		// Table Number
		m_oLabelTableNumberLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableNumberLabel, "lblTableNumberLabel");
		m_oLabelTableNumberLabel.setValue(AppGlobal.g_oLang.get()._("table_no"));
		oFrameRight.attachChild(m_oLabelTableNumberLabel);
		m_oLabelTableNumber = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableNumber, "lblTableNumber");
		m_oLabelTableNumber.setValue(sTable);
		oFrameRight.attachChild(m_oLabelTableNumber);
		
		// Check Cover
		m_oLabelCheckCoverLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckCoverLabel, "lblCheckCoverLabel");
		m_oLabelCheckCoverLabel.setValue(AppGlobal.g_oLang.get()._("cover"));
		oFrameRight.attachChild(m_oLabelCheckCoverLabel);
		m_oLabelCheckCover = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckCover, "lblCheckCover");
		m_oLabelCheckCover.setValue(String.valueOf(iCheckGuest));
		oFrameRight.attachChild(m_oLabelCheckCover);
		
		// Check total
		m_oLabelCheckTotalLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckTotalLabel, "lblCheckTotalLabel");
		m_oLabelCheckTotalLabel.setValue(AppGlobal.g_oLang.get()._("check_total"));
		oFrameRight.attachChild(m_oLabelCheckTotalLabel);
		m_oLabelCheckTotal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckTotal, "lblCheckTotal");
		m_oLabelCheckTotal.setValue(StringLib.BigDecimalToString(dCheckTotal, AppGlobal.g_oFuncOutlet.get().getBusinessDay().getCheckDecimal()));
		oFrameRight.attachChild(m_oLabelCheckTotal);
		
		// Save Button
		m_oButtonSave = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSave, "butSave");
		m_oButtonSave.setValue(AppGlobal.g_oLang.get()._("save"));
		m_oButtonSave.setVisible(true);
// Add by King Cheung 2017-11-02
		m_oButtonSave.setLeft(m_oFrameCheckInfoUnderlineBottom.getLeft() + m_oFrameCheckInfoUnderlineBottom.getWidth() - m_oButtonSave.getWidth());
		oFrameRight.attachChild(m_oButtonSave);
		
		// Number pad
		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.setEnterBlockUI(true);
		m_oFrameNumberPad.addListener(this);
		oFrameRight.attachChild(m_oFrameNumberPad);
//KingsleyKwan20171013ByKing	----- End -----
		//Set first available tips to be edit
		if(m_iCurrentItemIndex >= 0 && !isContainCreditCardDccPayment())
			this.frameCommonBasketCell_FieldClicked(m_oCommonBasketPaymentList.getId(), m_iCurrentSectionId, m_iCurrentItemIndex, 1, "");
	}
	
	public void addPaymentListHeader() {
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		
		iFieldWidths.add(m_oPaymentListWidths.get("paymentMethod"));
		sFieldValues.add(AppGlobal.g_oLang.get()._("payment_method"));
		iFieldWidths.add(m_oPaymentListWidths.get("tips"));
		sFieldValues.add(AppGlobal.g_oLang.get()._("tips"));
		iFieldWidths.add(m_oPaymentListWidths.get("amount"));
		sFieldValues.add(AppGlobal.g_oLang.get()._("amount_and_tips"));
		iFieldWidths.add(m_oPaymentListWidths.get("currency"));
		sFieldValues.add(AppGlobal.g_oLang.get()._("currency"));
		
		m_oCommonBasketPaymentList.addHeader(iFieldWidths, sFieldValues);
		m_oCommonBasketPaymentList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
//KingsleyKwan20171013ByKing	-----Start-----
		m_oCommonBasketPaymentList.setBottomUnderlineVisible(false);
//KingsleyKwan20171013ByKing	----- End -----
	}
	
	public void addPaymentRecords() {
		// Add payment records to common basket
		int iItemIndex = 0, iTipsReviseCount = 0;
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		
		for(HashMap<String, String> oPaymentInfo:m_oPaymentInfos) {
			iFieldWidths.clear();
			sFieldValues.clear();
			sFieldAligns.clear();
			
			iFieldWidths.add(m_oPaymentListWidths.get("paymentMethod"));
			sFieldValues.add(oPaymentInfo.get("paymentName"));
//KingsleyKwan20171013ByKing	-----Start-----
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.LEFT);
//KingsleyKwan20171013ByKing	----- End -----
			iFieldWidths.add(m_oPaymentListWidths.get("tipsAmount"));
			if(oPaymentInfo.get("payHaveTips").contentEquals(PosPaymentMethod.TIPS_HAVE_TIPS)) {
				sFieldValues.add(oPaymentInfo.get("payNewTips"));
				iTipsReviseCount++;
				if(iTipsReviseCount == 1) 
					m_iCurrentItemIndex = iItemIndex; 
			}else
				sFieldValues.add(AppGlobal.g_oLang.get()._("n_a"));
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
//KingsleyKwan20171013ByKing	-----Start-----
			iFieldWidths.add(m_oPaymentListWidths.get("tipsInputMode"));
			
			sFieldValues.add(oPaymentInfo.get("payCurrencySign"));
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
			
			iFieldWidths.add(m_oPaymentListWidths.get("amount") - m_oPaymentListWidths.get("tipsInputMode"));
//KingsleyKwan20171013ByKing	----- End -----
			if(oPaymentInfo.get("payForeignCurrency").equals(PosCheckPayment.PAY_FOREIGN_CURRENCY_FOREIGN) && oPaymentInfo.get("dccOptOut").isEmpty())
				sFieldValues.add(new BigDecimal(oPaymentInfo.get("PayAmountInForeignCurrency")).add(new BigDecimal(oPaymentInfo.get("PayOriTipsInForeignCurrency"))).toPlainString());
			else
				sFieldValues.add(new BigDecimal(oPaymentInfo.get("payAmount")).add(new BigDecimal(oPaymentInfo.get("payOriTips"))).toPlainString());
//KingsleyKwan20171013ByKing	-----Start-----
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
//KingsleyKwan20171013ByKing	----- End -----
			iFieldWidths.add(m_oPaymentListWidths.get("currency"));
			
			if(oPaymentInfo.get("payForeignCurrency").equals(PosCheckPayment.PAY_FOREIGN_CURRENCY_FOREIGN) && oPaymentInfo.get("dccOptOut").isEmpty())
				sFieldValues.add(oPaymentInfo.get("payCurrencyCode"));
			else
				sFieldValues.add(AppGlobal.g_oFuncOutlet.get().getCurrencyCode());
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
			
			m_oCommonBasketPaymentList.addItem(0, iItemIndex, m_iPaymentListHeight, iFieldWidths, sFieldValues, sFieldAligns, null, null);
			iItemIndex++;
		}
	}
	
	private boolean isContainCreditCardDccPayment() {
		for(HashMap<String, String> oPaymentInfo:m_oPaymentInfos) {
			if (oPaymentInfo.get("paytypeDcc").equals("dcc")) {
				return true;
			}
		}
		return false;
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		// Find the clicked button
		if (m_oButtonSave.getId() == iChildId) {
			for (FrameAdjustTipsListener listener : listeners) {
				// Raise the event to parent
				listener.frameAdjustTips_ClickSave(m_oPaymentInfos);
			}
			bMatchChild = true;
		}
    	return bMatchChild;
    }
	
	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
		
	}
	
	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		HashMap<String, String> oPaymentInfo = m_oPaymentInfos.get(iItemIndex);
		
		// select the tips input mode column ($ or %)
		if (iFieldIndex == 2 && oPaymentInfo.get("payHaveTips").contentEquals(PosPaymentMethod.TIPS_HAVE_TIPS)) {
			if (m_oCommonBasketPaymentList.getFieldValue(iSectionIndex, iItemIndex, 2).equals(oPaymentInfo.get("payCurrencySign"))) {
				m_oCommonBasketPaymentList.setFieldValue(iSectionIndex, iItemIndex, 2, FrameAdjustTips.TIPS_INPUT_TYPE_PERCENTAGE);
			}
			else if (m_oCommonBasketPaymentList.getFieldValue(iSectionIndex, iItemIndex, 2).equals(FrameAdjustTips.TIPS_INPUT_TYPE_PERCENTAGE)) {
				m_oCommonBasketPaymentList.setFieldValue(iSectionIndex, iItemIndex, 2, oPaymentInfo.get("payCurrencySign"));
			}
			m_iSelectedColumnIndex = 1;
		}
		
		if((iFieldIndex == 1 || iFieldIndex == 3) && oPaymentInfo.get("payHaveTips").contentEquals(PosPaymentMethod.TIPS_HAVE_TIPS)) {
			// Prompt message box to ask "Adjust Foreign Tips" or "Adjust Local Tips"
			// If this payment do opt-out before, program not need to prompt
			if (!oPaymentInfo.get("dccOptOut").equals("dcc") && oPaymentInfo.get("paytypeDcc").equals("dcc")) {
				ArrayList<String> oOptionList = new ArrayList<String>();
				boolean bNeedDccOptOut = false;
				
				oOptionList.add(AppGlobal.g_oLang.get()._("adjust_tips") +" (" + oPaymentInfo.get("payCurrencyCode") + ")");
				oOptionList.add(AppGlobal.g_oLang.get()._("adjust_tips") +" (" + AppGlobal.g_oFuncOutlet.get().getCurrencyCode() + ")");
				
				FormSelectionBox oFormSelectionBox = new FormSelectionBox(this.getParentForm());
				oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("dcc_action"), oOptionList, false);
				oFormSelectionBox.show();
				
				if (oFormSelectionBox.isUserCancel()) 
					return;
				else {
					ArrayList<Integer> oSelectionBoxResult = oFormSelectionBox.getResultList();
					if(oSelectionBoxResult.get(0) == 0) 
						bNeedDccOptOut = false;
					else if(oSelectionBoxResult.get(0) == 1) 
						bNeedDccOptOut = true;
				}
				// Do opt-out if select "Non-DCC Adjust Tips"
				if (bNeedDccOptOut) {
					// Update "Tips" and "Amount & Tips" fields in Local Currency if select the option in Local currency
					BigDecimal dPayTipsInLocalCurrency = new BigDecimal(oPaymentInfo.get("payOriTips"));
					BigDecimal dPayAmountWithTipsInLocalCurrency = new BigDecimal(oPaymentInfo.get("payAmount")).add(dPayTipsInLocalCurrency);
					m_oCommonBasketPaymentList.setFieldValue(iSectionIndex, iItemIndex, 1, StringLib.BigDecimalToString(dPayTipsInLocalCurrency, AppGlobal.g_oFuncOutlet.get().getBusinessDay().getPayDecimal()));
					m_oCommonBasketPaymentList.setFieldValue(iSectionIndex, iItemIndex, 3, StringLib.BigDecimalToString(dPayAmountWithTipsInLocalCurrency, AppGlobal.g_oFuncOutlet.get().getBusinessDay().getPayDecimal()));
					m_oCommonBasketPaymentList.setFieldValue(iSectionIndex, iItemIndex, 4, AppGlobal.g_oFuncOutlet.get().getCurrencyCode());
					
					m_oPaymentInfos.get(iItemIndex).put("selectDccOptOut", "true");
				}
				else {
					// Update "Tips" and "Amount & Tips" fields in Foreign Currency if select the option in Foreign currency
					BigDecimal dPayTipsInForeignCurrency = new BigDecimal(oPaymentInfo.get("PayOriTipsInForeignCurrency"));
					BigDecimal dPayAmountWithTipsInForeignCurrency = new BigDecimal(oPaymentInfo.get("PayAmountInForeignCurrency")).add(dPayTipsInForeignCurrency);
					m_oCommonBasketPaymentList.setFieldValue(iSectionIndex, iItemIndex, 1, StringLib.BigDecimalToString(dPayTipsInForeignCurrency, AppGlobal.g_oFuncOutlet.get().getBusinessDay().getPayDecimal()));
					m_oCommonBasketPaymentList.setFieldValue(iSectionIndex, iItemIndex, 3, StringLib.BigDecimalToString(dPayAmountWithTipsInForeignCurrency, AppGlobal.g_oFuncOutlet.get().getBusinessDay().getPayDecimal()));
					m_oCommonBasketPaymentList.setFieldValue(iSectionIndex, iItemIndex, 4, oPaymentInfo.get("payCurrencyCode"));
					
					m_oPaymentInfos.get(iItemIndex).put("selectDccOptOut", "false");
				}
			}
			
			m_iCurrentSectionId = iSectionIndex;
			m_iCurrentItemIndex = iItemIndex;
			// iFieldIndex = 1, click "Tips" column to edit tips
			// iFieldIndex = 3, click "Amount & Tips" column to edit amount & tips
			VirtualUIBasicElement oElement = m_oCommonBasketPaymentList.setEditField(iSectionIndex, iItemIndex, iFieldIndex);
			m_iSelectedColumnIndex = iFieldIndex;
			oElement.setFocusWhenShow(true);
			m_oFrameNumberPad.clearEnterSubmitId();
			m_oFrameNumberPad.setEnterSubmitId(oElement);
		}
	}
	
	@Override
	public void FrameNumberPad_clickEnter() {
		boolean bPassChecking = false, bNextFound = false;
		HashMap<String, String> oPaymentInfo = null;
		BigDecimal dNewTips = null;
		BigDecimal dPayAmount = null;
		BigDecimal dNewAmountWithTips = null;
		
		if(m_iCurrentItemIndex >= m_oPaymentInfos.size())
			return;
		if(m_oCommonBasketPaymentList.getEditFieldValue().isEmpty())
			return;
		
		if (m_oPaymentInfos.get(m_iCurrentItemIndex).get("selectDccOptOut").equals("true") || m_oPaymentInfos.get(m_iCurrentItemIndex).get("dccOptOut").equals("dcc")) 
			dPayAmount = new BigDecimal(m_oPaymentInfos.get(m_iCurrentItemIndex).get("payAmount"));
		else if(m_oPaymentInfos.get(m_iCurrentItemIndex).get("payForeignCurrency").equals(PosCheckPayment.PAY_FOREIGN_CURRENCY_FOREIGN))
			dPayAmount = new BigDecimal(m_oPaymentInfos.get(m_iCurrentItemIndex).get("PayAmountInForeignCurrency"));
		else
			dPayAmount = new BigDecimal(m_oPaymentInfos.get(m_iCurrentItemIndex).get("payAmount"));
		
		// get the tips amount if the record select "percentage" as input mode
		if (m_oCommonBasketPaymentList.getFieldValue(m_iCurrentSectionId, m_iCurrentItemIndex, 2).equals(FrameAdjustTips.TIPS_INPUT_TYPE_PERCENTAGE)) {
			BigDecimal dTipsPercentage = new BigDecimal(m_oCommonBasketPaymentList.getEditFieldValue());
			// check tips percentage should be between 0 - 100
			for(FrameAdjustTipsListener listener : listeners) {
				bPassChecking = listener.frameAdjustTips_CheckTipsPercentage(dTipsPercentage);
			}
			if (!bPassChecking)
				return;
			dTipsPercentage = dTipsPercentage.divide(new BigDecimal("100.0"));
			dNewTips = dPayAmount.multiply(dTipsPercentage);
		}
		
		// m_iSelectedFieldIndex = 1, click "Tips" column to edit tips
		// m_iSelectedFieldIndex = 3, click "Amount & Tips" column to edit amount & tips
		if (m_iSelectedColumnIndex == 1) {
			// get the tips amount if the record select "percentage" as input mode
			if (m_oCommonBasketPaymentList.getFieldValue(m_iCurrentSectionId, m_iCurrentItemIndex, 2).equals(FrameAdjustTips.TIPS_INPUT_TYPE_PERCENTAGE)) {
				BigDecimal dTipsPercentage = new BigDecimal(m_oCommonBasketPaymentList.getEditFieldValue());
				dTipsPercentage = dTipsPercentage.divide(new BigDecimal("100.0"));
				dNewTips = dPayAmount.multiply(dTipsPercentage);
			}
			else
				dNewTips = new BigDecimal(m_oCommonBasketPaymentList.getEditFieldValue());
			dNewTips = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dNewTips);
		}
		else if (m_iSelectedColumnIndex == 3) {
			dNewAmountWithTips = new BigDecimal(m_oCommonBasketPaymentList.getEditFieldValue());
			dNewAmountWithTips = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dNewAmountWithTips);
			dNewTips = dNewAmountWithTips.subtract(dPayAmount);
		}
		
		// Fire event to FormAdjustTips to handle tips input checking
		for(FrameAdjustTipsListener listener : listeners) {
			bPassChecking = listener.frameAdjustTips_AskTipsConfirmation(dPayAmount, dNewTips);
		}
		
		if (bPassChecking) {
			m_oCommonBasketPaymentList.setFieldValue(m_iCurrentSectionId, m_iCurrentItemIndex, 1, StringLib.BigDecimalToString(dNewTips, AppGlobal.g_oFuncOutlet.get().getBusinessDay().getPayDecimal()));
			m_oCommonBasketPaymentList.clearEditField();
			m_oPaymentInfos.get(m_iCurrentItemIndex).put("payNewTips", dNewTips.toPlainString());
			
			if (m_oPaymentInfos.get(m_iCurrentItemIndex).get("selectDccOptOut").equals("true") || m_oPaymentInfos.get(m_iCurrentItemIndex).get("dccOptOut").equals("dcc"))
				dNewAmountWithTips = new BigDecimal(m_oPaymentInfos.get(m_iCurrentItemIndex).get("payAmount")).add(dNewTips);
			else if(m_oPaymentInfos.get(m_iCurrentItemIndex).get("payForeignCurrency").equals(PosCheckPayment.PAY_FOREIGN_CURRENCY_FOREIGN))
				dNewAmountWithTips = new BigDecimal(m_oPaymentInfos.get(m_iCurrentItemIndex).get("PayAmountInForeignCurrency")).add(dNewTips);
			else
				dNewAmountWithTips = new BigDecimal(m_oPaymentInfos.get(m_iCurrentItemIndex).get("payAmount")).add(dNewTips);
			m_oCommonBasketPaymentList.setFieldValue(m_iCurrentSectionId, m_iCurrentItemIndex, 3, StringLib.BigDecimalToString(dNewAmountWithTips, AppGlobal.g_oFuncOutlet.get().getBusinessDay().getPayDecimal()));
			
			// restore the tips input as "fix amount"
			if (m_oCommonBasketPaymentList.getFieldValue(m_iCurrentSectionId, m_iCurrentItemIndex, 2).equals(FrameAdjustTips.TIPS_INPUT_TYPE_PERCENTAGE)) {
				m_oCommonBasketPaymentList.setFieldValue(m_iCurrentSectionId, m_iCurrentItemIndex, 2, m_oPaymentInfos.get(m_iCurrentItemIndex).get("payCurrencySign"));
			}
			
			// If the payment contain DCC payment, no need to auto jump to next tips adjustment record
			if (!isContainCreditCardDccPayment()) {
				m_iCurrentItemIndex++;
				if(m_iCurrentItemIndex < m_oPaymentInfos.size()) {
					do {
						oPaymentInfo = m_oPaymentInfos.get(m_iCurrentItemIndex);
						if(oPaymentInfo.get("payHaveTips").contentEquals(PosPaymentMethod.TIPS_NO_TIPS))
							if(m_iCurrentItemIndex == (m_oPaymentInfos.size() - 1))
								bNextFound = true;
							else 
								m_iCurrentItemIndex++;
						else {
							VirtualUIBasicElement oElement = m_oCommonBasketPaymentList.setEditField(m_iCurrentSectionId, m_iCurrentItemIndex, m_iSelectedColumnIndex);
							m_oFrameNumberPad.clearEnterSubmitId();
							m_oFrameNumberPad.setEnterSubmitId(oElement);
							bNextFound = true;
						}
					}while(!bNextFound);
				}
			}
			
		}else {
			VirtualUIBasicElement oElement = m_oCommonBasketPaymentList.setEditField(m_iCurrentSectionId, m_iCurrentItemIndex, m_iSelectedColumnIndex);
			m_oFrameNumberPad.clearEnterSubmitId();
			m_oFrameNumberPad.setEnterSubmitId(oElement);
			
			// restore the tips input as "fix amount"
			if (m_oCommonBasketPaymentList.getFieldValue(m_iCurrentSectionId, m_iCurrentItemIndex, 2).equals(FrameAdjustTips.TIPS_INPUT_TYPE_PERCENTAGE)) {
				m_oCommonBasketPaymentList.setFieldValue(m_iCurrentSectionId, m_iCurrentItemIndex, 2, m_oPaymentInfos.get(m_iCurrentItemIndex).get("payCurrencySign"));
			}
		}
	}

	@Override
	public void FrameNumberPad_clickCancel() {
		
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

	@Override
	public void FrameTitleHeader_close() {
		for (FrameAdjustTipsListener listener : listeners) {
			// Raise the event to parent
			listener.frameAdjustTips_ClickExit();
		}
	}
}
