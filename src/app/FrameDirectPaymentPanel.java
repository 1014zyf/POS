package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import om.OutCurrency;
import om.PosDisplayPanelLookup;
import om.PosPaymentMethod;
import commonui.FormDialogBox;
import commonui.FrameNumberPad;
import commonui.FrameNumberPadListener;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FrameDirectPaymentPanelListener {
	void FrameDirectPaymentPanel_finishAskAmount(BigDecimal dCurrentPaymentAmount, BigDecimal dCurrentTipsAmount);
	boolean FrameDirectPaymentPanel_askTipsConfirmation(BigDecimal dCurrentPaymentAmount, BigDecimal dCurrentTipsAmount);
	void FrameDirectPaymentPanel_updateCashierHeader(boolean bAdd, String sValue, BigDecimal dPaymentAmt, BigDecimal dTipsAmt);
	void FrameDirectPaymentPanel_cancel();
}

public class FrameDirectPaymentPanel extends VirtualUIFrame implements FrameNumberPadListener, FrameCommonBasketListener, FrameCommonLookupButtonsListener, FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUIFrame m_oFrameBackground;
	private VirtualUIFrame m_oFrameBackground2;
	
	private VirtualUILabel m_oLabelCheckNoHeader;
	private VirtualUILabel m_oLabelCheckNo;
	private VirtualUILabel m_oLabelTableHeader;
	private VirtualUILabel m_oLabelTable;
	
	private VirtualUILabel m_oLabelPaymentMethodHeader;
	private VirtualUILabel m_oLabelPaymentMethod;
	private VirtualUILabel m_oLabelTotalHeader;
	private VirtualUILabel m_oLabelTotal;
	private VirtualUILabel m_oLabelTipsHeader;
	private VirtualUILabel m_oLabelTips;
	private VirtualUILabel m_oLabelSurchargeHeader;
	private VirtualUILabel m_oLabelSurcharge;
	private VirtualUITextbox m_oTextboxField;
	
	private FrameNumberPad m_oFrameNumberPad;
	
	private FrameCommonBasket m_oPaymentRecordCommonBasket;
	private VirtualUILabel m_oLabelPartialPaymentBgColor;
	
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
	
	private VirtualUIFrame m_oFrameResizePanel;
	
	private PosPaymentMethod m_oPosPaymentMethod;
	private OutCurrency m_oCurrency;
	private BigDecimal m_dOriginalPaymentAmount;
	private BigDecimal m_dCurrentPaymentAmount;
	private BigDecimal m_dCurrentTipsAmount;
	
	private boolean m_bPayInForeignCurrency;
	private boolean m_bCurrentNeedAskTipsAmount;
	private boolean m_bNumberPadKeyProcessing;	// flag for indicate whether handling key event now or not 
	private int m_iCurrentPaymentStep;
	private boolean m_bSupportSurcharge;
	private boolean m_bHideNumberPad;
	
	private FrameCommonLookupButtons m_oTenderAmountButtons;
	
	private String[] m_sDefaultTenderAmountArray = { "0.1", "0.2", "0.5", "1", "2", "5", "10", "20", "50", "100", "500", "1000" };
	
	private List<BigDecimal> m_oTenderAmountList;
	
	private boolean m_bNumberPad;
	
	private FrameTitleHeader m_oTitleHeader;
	
	private int m_iTenderAmountButtonsTop;
	private int m_iTenderAmountButtonsHeight;
	
	private int m_iPaymentRecordCommonBasketTop;
	private int m_iFrameNumberPadTop;
	
	private static final int SURCHARGE_HEIGHT_MARGIN = 30;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameDirectPaymentPanelListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameDirectPaymentPanelListener listener) {
		listeners.add(listener);
	}

	public FrameDirectPaymentPanel(boolean bHideNumberPad, boolean bSupportSurcharge) {

		m_bSupportSurcharge = bSupportSurcharge;
		m_bHideNumberPad = bHideNumberPad;
		m_oTenderAmountList = new ArrayList<BigDecimal>();
		
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameDirectPaymentPanelListener>();
				
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraDirectPaymentPanel.xml");
		this.setVisible(false);

		// Frame background
		m_oFrameBackground = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameBackground, "fraBackground");
		this.attachChild(m_oFrameBackground);
		
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oTitleHeader.init(false);
		else
			m_oTitleHeader.init(true);
		m_oTitleHeader.addListener(this);
		this.attachChild(m_oTitleHeader);
		
		// Check Info
		m_oLabelCheckNoHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckNoHeader, "lblCheckNoHeader");
		m_oLabelCheckNoHeader.setValue(AppGlobal.g_oLang.get()._("check_no", ""));
		this.attachChild(m_oLabelCheckNoHeader);
		
		m_oLabelCheckNo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckNo, "lblCheckNo");
		this.attachChild(m_oLabelCheckNo);
		
		m_oLabelTableHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableHeader, "lblTableHeader");
		m_oLabelTableHeader.setValue(AppGlobal.g_oLang.get()._("table", ""));
		this.attachChild(m_oLabelTableHeader);
		
		m_oLabelTable = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTable, "lblTable");
		this.attachChild(m_oLabelTable);
		
		// Edit field
		m_oTextboxField = new VirtualUITextbox();
		m_oTextboxField.setExist(true);
		m_oTextboxField.setVisible(false);
		m_oTextboxField.setFocusWhenShow(true);
		this.attachChild(m_oTextboxField);
		
		// Payment Method
		m_oLabelPaymentMethodHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelPaymentMethodHeader, "lblPaymentMethodHeader");
		m_oLabelPaymentMethodHeader.setValue(AppGlobal.g_oLang.get()._("payment_type", ""));
		this.attachChild(m_oLabelPaymentMethodHeader);
		
		m_oLabelPaymentMethod = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelPaymentMethod, "lblPaymentMethod");
		this.attachChild(m_oLabelPaymentMethod);
		
		// Payment Total
		m_oLabelTotalHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTotalHeader, "lblTotalHeader");
		m_oLabelTotalHeader.setValue(AppGlobal.g_oLang.get()._("total", ""));
		this.attachChild(m_oLabelTotalHeader);
		
		m_oLabelTotal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTotal, "lblTotal");
		this.attachChild(m_oLabelTotal);

		// Payment Tips
		m_oLabelTipsHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTipsHeader, "lblTipsHeader");
		m_oLabelTipsHeader.setValue(AppGlobal.g_oLang.get()._("tips", ""));
		this.attachChild(m_oLabelTipsHeader);
		
		m_oLabelTips = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTips, "lblTips");
		this.attachChild(m_oLabelTips);
		
		// Payment Surcharge
		m_oLabelSurchargeHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSurchargeHeader, "lblSurchargeHeader");
		m_oLabelSurchargeHeader.setValue(AppGlobal.g_oLang.get()._("surcharge", ""));
		m_oLabelSurchargeHeader.setVisible(false);
		this.attachChild(m_oLabelSurchargeHeader);
		
		m_oLabelSurcharge = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSurcharge, "lblSurcharge");
		m_oLabelSurcharge.setVisible(false);
		this.attachChild(m_oLabelSurcharge);
		
		VirtualUIFrame oFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrame, "fraBackground2");

		if (m_bSupportSurcharge && !AppGlobal.g_oFuncStation.get().supportTaiwanGui()) 
			oFrame.setTop(oFrame.getTop()+30);
		
		// extend the height for Taiwan GUI
		if (AppGlobal.g_oFuncStation.get().supportTaiwanGui()) {
			oFrame.setHeight(oFrame.getHeight()+125);
			oFrame.setTop(oFrame.getTop()-12);
		}
		this.attachChild(oFrame);
		m_oFrameBackground2 = oFrame;

		m_oFrameResizePanel = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameResizePanel, "fraResizePanel");
		
		// Other Information
		m_oLabelOtherInformationHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformationHeader, "lblOtherInformationHeader");
		m_oLabelOtherInformationHeader.setVisible(false);
		oFrame.attachChild(m_oLabelOtherInformationHeader);

		m_oLabelOtherInformation = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformation, "lblOtherInformation");
		m_oLabelOtherInformation.setVisible(false);
		oFrame.attachChild(m_oLabelOtherInformation);

		m_oLabelOtherInformationHeader2 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformationHeader2, "lblOtherInformationHeader2");
		m_oLabelOtherInformationHeader2.setVisible(false);
		oFrame.attachChild(m_oLabelOtherInformationHeader2);

		m_oLabelOtherInformation2 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformation2, "lblOtherInformation2");
		m_oLabelOtherInformation2.setVisible(false);
		oFrame.attachChild(m_oLabelOtherInformation2);
		
		m_oLabelOtherInformationHeader3 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformationHeader3, "lblOtherInformationHeader3");
		m_oLabelOtherInformationHeader3.setVisible(false);
		oFrame.attachChild(m_oLabelOtherInformationHeader3);

		m_oLabelOtherInformation3 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformation3, "lblOtherInformation3");
		m_oLabelOtherInformation3.setVisible(false);
		oFrame.attachChild(m_oLabelOtherInformation3);
		
		m_oLabelOtherInformationHeader4 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformationHeader4, "lblOtherInformationHeader4");
		m_oLabelOtherInformationHeader4.setVisible(false);
		oFrame.attachChild(m_oLabelOtherInformationHeader4);

		m_oLabelOtherInformation4 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformation4, "lblOtherInformation4");
		m_oLabelOtherInformation4.setVisible(false);
		oFrame.attachChild(m_oLabelOtherInformation4);
		
		m_oLabelOtherInformation5 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherInformation5, "lblOtherInformation5");
		m_oLabelOtherInformation5.setVisible(false);
		oFrame.attachChild(m_oLabelOtherInformation5);
		// Number pad
		m_oFrameNumberPad = new FrameNumberPad();
		if (m_bHideNumberPad)
			m_oFrameNumberPad.hideNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		// shorten the height for Taiwan GUI
		if (AppGlobal.g_oFuncStation.get().supportTaiwanGui()){
			m_oFrameNumberPad.setTop(m_oFrameNumberPad.getTop()+50);
			m_oFrameNumberPad.setCustomHeight(155);

			m_oLabelOtherInformation5.setWidth(m_oLabelOtherInformation5.getWidth()+60);
			m_oLabelOtherInformation4.setWidth(m_oLabelOtherInformation4.getWidth()+10);
			m_oLabelOtherInformation3.setWidth(m_oLabelOtherInformation3.getWidth()+10);
			m_oLabelOtherInformation2.setWidth(m_oLabelOtherInformation2.getWidth()+10);
			m_oLabelOtherInformation.setWidth(m_oLabelOtherInformation.getWidth()+10);
			
			m_oLabelOtherInformation5.setTop(m_oLabelOtherInformation5.getTop()-32);
			m_oLabelOtherInformation4.setTop(m_oLabelOtherInformation4.getTop()-32);
			m_oLabelOtherInformation3.setTop(m_oLabelOtherInformation3.getTop()-30);
			m_oLabelOtherInformation2.setTop(m_oLabelOtherInformation2.getTop()-28);
			m_oLabelOtherInformation.setTop(m_oLabelOtherInformation.getTop()-26);
			
			m_oLabelOtherInformationHeader4.setTop(m_oLabelOtherInformationHeader4.getTop()-32);
			m_oLabelOtherInformationHeader3.setTop(m_oLabelOtherInformationHeader3.getTop()-30);
			m_oLabelOtherInformationHeader2.setTop(m_oLabelOtherInformationHeader2.getTop()-28);
			m_oLabelOtherInformationHeader.setTop(m_oLabelOtherInformationHeader.getTop()-26);
			
		}
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.setEnterBlockUI(true);
		m_oFrameNumberPad.addListener(this);
		this.attachChild(m_oFrameNumberPad);

		int iRow = 4, iCol = 3;
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			iRow = 1;
			iCol = 4;
		}
		m_oTenderAmountButtons = new FrameCommonLookupButtons();
		m_oTemplateBuilder.buildFrame(m_oTenderAmountButtons, "fraTenderAmountButtons");
		m_oTenderAmountButtons.init();
		m_oTenderAmountButtons.setConfig(iRow, iCol, 0);
		m_oTenderAmountButtons.setSingleSelection(true);
		m_oTenderAmountButtons.addListener(this);
		initTenderAmountButtons();
		this.attachChild(m_oTenderAmountButtons);

		m_iTenderAmountButtonsTop = m_oTenderAmountButtons.getTop();
		m_iTenderAmountButtonsHeight = m_oTenderAmountButtons.getHeight();
		
		// Payment basket
		m_oPaymentRecordCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oPaymentRecordCommonBasket, "fraPaymentBasket");
		m_oPaymentRecordCommonBasket.init();
		
		m_oPaymentRecordCommonBasket.addListener(this);
		this.attachChild(m_oPaymentRecordCommonBasket);
		
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String[]> sFieldValues = new ArrayList<String[]>();
		iFieldWidths.add(m_oPaymentRecordCommonBasket.getWidth()/4*2);
		sFieldValues.add(AppGlobal.g_oLang.get()._("payment_type", ""));
		iFieldWidths.add(m_oPaymentRecordCommonBasket.getWidth()/4);
		sFieldValues.add(AppGlobal.g_oLang.get()._("amount", ""));
		iFieldWidths.add(m_oPaymentRecordCommonBasket.getWidth()/4);
		sFieldValues.add(AppGlobal.g_oLang.get()._("tips", ""));
		m_oPaymentRecordCommonBasket.addHeader(iFieldWidths, sFieldValues);
		m_oPaymentRecordCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		m_oPaymentRecordCommonBasket.setBottomUnderlineVisible(false);
		m_oPaymentRecordCommonBasket.setCashierCommonBasketHeight(260);
		m_oPaymentRecordCommonBasket.setHeaderFormat(40, 16, "5,0,0,14");
		
		//If support Surcharge, adjust position
		if (m_bSupportSurcharge && !AppGlobal.g_oFuncStation.get().supportTaiwanGui()) 
			m_oPaymentRecordCommonBasket.setTop(m_oPaymentRecordCommonBasket.getTop()+40);
		
		// get partial payment background color from xml
		m_oLabelPartialPaymentBgColor = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelPartialPaymentBgColor, "lblPartialPaymentBgColor");
		
		m_iPaymentRecordCommonBasketTop = m_oPaymentRecordCommonBasket.getTop();
		m_iFrameNumberPadTop = m_oFrameNumberPad.getTop();
	}
	
	public void resetNumpadHeight() {
		if (m_bHideNumberPad) {
			m_oFrameNumberPad.setCustomHeight(80);
			m_oFrameNumberPad.setTop(this.getHeight() - m_oFrameNumberPad.getCustomHeight());
			
			m_oFrameBackground2.setTop(m_oFrameNumberPad.getTop() - m_oFrameBackground2.getHeight());
		}
	}
	
	public void resizeDirectPaymentPanel(boolean bReset) {
		if(!bReset) {
			//If support Surcharge, adjust position
			if (m_bSupportSurcharge && !AppGlobal.g_oFuncStation.get().supportTaiwanGui()) 
				m_oPaymentRecordCommonBasket.setTop(m_iPaymentRecordCommonBasketTop + m_oFrameResizePanel.getWidth());
			else
				m_oPaymentRecordCommonBasket.setTop(m_iPaymentRecordCommonBasketTop * 2);
			m_oFrameNumberPad.setTop(m_oFrameNumberPad.getTop() + m_oFrameResizePanel.getHeight());
			m_oFrameBackground.setHeight(m_oFrameBackground.getHeight() + m_oFrameResizePanel.getHeight());
			m_oFrameBackground2.setHeight(m_oFrameBackground2.getHeight() + m_oFrameResizePanel.getHeight());
		}else {
			m_oPaymentRecordCommonBasket.setTop(m_iPaymentRecordCommonBasketTop);
			m_oFrameNumberPad.setTop(m_iFrameNumberPadTop);
		}
	}
	
	public void initTenderAmountButtons() {
		List<FuncLookupButtonInfo> oContentLookupList = new ArrayList<FuncLookupButtonInfo>();
		List<VirtualUIBasicElement> oSubmitElementList = new ArrayList<VirtualUIBasicElement>();
		
		List<String> oTenderAmountList = new ArrayList<String>();
		if (!AppGlobal.g_oTenderAmountList.isEmpty()) { // If PosConfig has tender amount setup
			for (int i = 0; i < AppGlobal.g_oTenderAmountList.size(); i++) {
				oTenderAmountList.add(AppGlobal.g_oTenderAmountList.get(i).toPlainString());
			}
		} else {
			for (int i = 0; i < m_sDefaultTenderAmountArray.length; i++) {
				oTenderAmountList.add(m_sDefaultTenderAmountArray[i]);
			}
		}
		
		for (int i = 0; i < oTenderAmountList.size(); i++) {
			FuncLookupButtonInfo oLookupButtonInfo = new FuncLookupButtonInfo();
			oLookupButtonInfo.setName(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, oTenderAmountList.get(i)));
			oLookupButtonInfo.setType(PosDisplayPanelLookup.TYPE_HOT_ITEM);
			oLookupButtonInfo.setId(i);
			oLookupButtonInfo.setSeq(i+1);
			m_oTenderAmountList.add(new BigDecimal(oTenderAmountList.get(i)));
			oContentLookupList.add(oLookupButtonInfo);
			
			oSubmitElementList.add(m_oTextboxField);
		}
		
		m_oTenderAmountButtons.updateLookupButtons(oContentLookupList, oSubmitElementList);
	}

	public void setTitle(String[] sTitle){
		m_oTitleHeader.setTitle(sTitle);
	}
	
	public void showCheckInfo(String sCheckNo, String sTable) {
		m_oLabelCheckNo.setValue(sCheckNo);
		m_oLabelTable.setValue(sTable);
	}
	
	// Initial the screen and show the frame
	public void showPanel(String sPaymentMethodDesc, BigDecimal dPayAmt, BigDecimal dTips, boolean bNeedAskTips, boolean isPayByForeignCurrency, PosPaymentMethod oPosPaymentMethod, OutCurrency oCurrency, BigDecimal dPaymentAmtInForeignCurrency, BigDecimal dTipsAmtInForeignCurrency){
		synchronized(this) {
			m_bNumberPadKeyProcessing = false;
		}
		m_bPayInForeignCurrency = isPayByForeignCurrency;
		m_bCurrentNeedAskTipsAmount = bNeedAskTips;
		m_dOriginalPaymentAmount = dPayAmt;
		m_dCurrentPaymentAmount = dPayAmt;
		m_dCurrentTipsAmount = dTips;
		
		m_oLabelPaymentMethod.setValue(sPaymentMethodDesc);
		
		m_oPosPaymentMethod = oPosPaymentMethod;
		m_oCurrency = oCurrency;
		if (m_bPayInForeignCurrency)
			updatePaymentTotalAndTipsLabelValue(dPaymentAmtInForeignCurrency, dTipsAmtInForeignCurrency);
		else
			updatePaymentTotalAndTipsLabelValue(dPayAmt, dTips);
		
		boolean bShowSurcharge = false;
		int iTop = m_iTenderAmountButtonsTop, iHeight = m_iTenderAmountButtonsHeight;
		if (oPosPaymentMethod.getSurchargeRate().compareTo(BigDecimal.ZERO) > 0) {
			bShowSurcharge = true;
			String sSurcharge = calculateSurcharge(m_dCurrentPaymentAmount, m_dCurrentTipsAmount);
			m_oLabelSurcharge.setValue(sSurcharge);
			for (FrameDirectPaymentPanelListener listener : listeners)
				// Raise the event to parent
				listener.FrameDirectPaymentPanel_updateCashierHeader(true, sSurcharge, m_dCurrentPaymentAmount, m_dCurrentTipsAmount);
			iTop += SURCHARGE_HEIGHT_MARGIN;
			iHeight -= SURCHARGE_HEIGHT_MARGIN;
		}
		
		m_oLabelSurchargeHeader.setVisible(bShowSurcharge);
		m_oLabelSurcharge.setVisible(bShowSurcharge);
		// only need to reset top position when tender amount buttons frame's top position is different from before
		if (m_oTenderAmountButtons.getTop() != iTop) {
			m_oTenderAmountButtons.setTop(iTop);
			
			// only desktop view need to update frame height
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.horizontal_desktop.name()))
				m_oTenderAmountButtons.setHeight(iHeight);
		}
		
		clearPaymentBasket();
		
		// Initial the step
		m_iCurrentPaymentStep = 1;
		VirtualUIBasicElement oElement = setEditField(1, true);
		m_oFrameNumberPad.clearEnterSubmitId();
		m_oFrameNumberPad.setEnterSubmitId(oElement);
		
		this.setVisible(true);
		this.bringToTop();
		
		m_bNumberPad = true;
	}
	
	private String calculateSurcharge(BigDecimal oPayTotal, BigDecimal oTips) {
		BigDecimal dSurchargePercentage = m_oPosPaymentMethod.getSurchargeRate().divide(new BigDecimal(100));
		String sSurchargeAmount = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(oPayTotal.add(oTips).multiply(dSurchargePercentage));
		return sSurchargeAmount;
	}
	
	public void updatePaymentTotalAndTipsLabelValue(BigDecimal dPayAmt, BigDecimal dTips) {
		BigDecimal oAmount = (dPayAmt.compareTo(BigDecimal.ZERO) == 0)? BigDecimal.ZERO: dPayAmt;
		setFieldValue(1, oAmount.stripTrailingZeros().toPlainString());
		oAmount = (dTips.compareTo(BigDecimal.ZERO) == 0)? BigDecimal.ZERO: dTips;
		setFieldValue(2, oAmount.stripTrailingZeros().toPlainString());
	}
	
	public void addPaymentRecord(int iSectionId, int iItemIndex, String sDesc, String sInfo1, String sInfo2, BigDecimal dTips, BigDecimal dPaymentTotal){
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		
		iFieldWidths.add(m_oPaymentRecordCommonBasket.getWidth() / 4 * 2);
		sFieldValues.add(sDesc);
		sFieldAligns.add("");
		iFieldWidths.add(m_oPaymentRecordCommonBasket.getWidth() / 4);
		
		String sValue = dPaymentTotal.stripTrailingZeros().setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_DOWN).toPlainString();

		sFieldValues.add(sValue);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
		iFieldWidths.add(m_oPaymentRecordCommonBasket.getWidth()/4);
		if(dTips.compareTo(BigDecimal.ZERO) == 0)
			sValue = (BigDecimal.ZERO).stripTrailingZeros().setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_DOWN).toPlainString();
		else
			sValue = dTips.stripTrailingZeros().setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_DOWN).toPlainString();
		sFieldValues.add(sValue);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
		m_oPaymentRecordCommonBasket.addItem(iSectionId, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
		
		for (int i = 0; i <= 2 ; i++ ) 
			m_oPaymentRecordCommonBasket.setFieldTextSize(iSectionId, iItemIndex, i, 16);
		m_oPaymentRecordCommonBasket.setFieldPadding(iSectionId, iItemIndex, 0, "0,0,0,18");
		m_oPaymentRecordCommonBasket.setAllFieldsForegroundColor(iSectionId, iItemIndex, "#0055B8");
		m_oPaymentRecordCommonBasket.setAllFieldsBackgroundColor(iSectionId, iItemIndex, "#FFFFFF");
		
		// Add information
		if(sInfo1 != null && sInfo1.length() > 0){
			m_oPaymentRecordCommonBasket.setFieldInfo1(iSectionId, iItemIndex, 0, sInfo1);
			if(sInfo2 != null && sInfo2.length() > 0){
				m_oPaymentRecordCommonBasket.setFieldInfo2(iSectionId, iItemIndex, 0, sInfo2);
			}
		}
		
		m_oPaymentRecordCommonBasket.setCashierCommonBasketHeight(260);
		
		m_oPaymentRecordCommonBasket.moveScrollToItem(iSectionId, iItemIndex);
	}
	
	public void clearPaymentBasket(){
		m_oPaymentRecordCommonBasket.removeAllSections();
		m_oPaymentRecordCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		m_oPaymentRecordCommonBasket.setUpperUnderLineShow(false);
		m_oPaymentRecordCommonBasket.setCashierCommonBasketHeight(260);
	}
	
	public void updatePartialPaymentBasketFieldBackgroundColor(int iSectionId, int iItemIndex){
		for (int i = 0; i <= 2 ; i++ ) 
			m_oPaymentRecordCommonBasket.setFieldBackgroundColor(0, iItemIndex, i, m_oLabelPartialPaymentBgColor.getBackgroundColor());
	}
	
	public void updatePartialPaymentInfo1BackgroundColor(int iSectionId, int iItemIndex){
		for (int i = 0; i <= 2 ; i++ ) 
			m_oPaymentRecordCommonBasket.setFieldInfo1BackgroundColor(0, iItemIndex, i, m_oLabelPartialPaymentBgColor.getBackgroundColor());
	}
	
	public void updatePartialPaymentInfo2BackgroundColor(int iSectionId, int iItemIndex){
		for (int i = 0; i <= 2 ; i++ ) 
			m_oPaymentRecordCommonBasket.setFieldInfo2BackgroundColor(0, iItemIndex, i, m_oLabelPartialPaymentBgColor.getBackgroundColor());
	}

	public void showOtherInformation(boolean bShow) {
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
	
	public VirtualUIBasicElement setEditField(int iFieldIndex, boolean bEdit) {
		VirtualUILabel oLabel;
		
		if(iFieldIndex == 1)
			oLabel = m_oLabelTotal;
		else
			oLabel = m_oLabelTips;
		
		if (bEdit == false) {
			oLabel.setVisible(true);
			m_oTextboxField.setVisible(false);
		} else {
			oLabel.setVisible(false);
			m_oTextboxField.setValue(oLabel.getValue());
			m_oTextboxField.setTop(oLabel.getTop());
			m_oTextboxField.setLeft(oLabel.getLeft());
			m_oTextboxField.setWidth(oLabel.getWidth());
			m_oTextboxField.setHeight(oLabel.getHeight());
			m_oTextboxField.setForegroundColor(oLabel.getForegroundColor());
			m_oTextboxField.setTextSize(oLabel.getTextSize());
			m_oTextboxField.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.DECIMAL);
			m_oTextboxField.setCornerRadius("5");
			m_oTextboxField.setClickHideKeyboard(true);
			m_oTextboxField.setVisible(true);
			m_oTextboxField.bringToTop();
			m_oTextboxField.setFocus();
		}
		
		return m_oTextboxField;
	}
	
	public void setFieldValue(int iFieldIndex, String sValue) {
		VirtualUILabel oLabel;
		if(iFieldIndex == 1)
			oLabel = m_oLabelTotal;
		else
			oLabel = m_oLabelTips;

		oLabel.setValue(sValue);
		
		if (m_oPosPaymentMethod.getSurchargeRate().compareTo(BigDecimal.ZERO) > 0) {
			String sSurcharge = calculateSurcharge(m_dCurrentPaymentAmount, m_dCurrentTipsAmount);
			m_oLabelSurcharge.setValue(sSurcharge);
			for (FrameDirectPaymentPanelListener listener : listeners)
				// Raise the event to parent
				listener.FrameDirectPaymentPanel_updateCashierHeader(true, sSurcharge, m_dCurrentPaymentAmount, m_dCurrentTipsAmount);
		}
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId,
			String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex,
			int iItemIndex, int iFieldIndex, String sNote) {
	}

	@Override
	public void FrameNumberPad_clickEnter() {
		//In order to prevent fire two key event together, it need to check whether processing any key event first
		//If it is not handling any key event, set the m_bNumberPadKeyProcessing to true
		synchronized(this) {
			if(m_bNumberPadKeyProcessing)
				return;
			else
				m_bNumberPadKeyProcessing = true;
		}
		
		if(m_iCurrentPaymentStep == 1){
			// Press enter to confirm payment amount
			if(!m_oTextboxField.getValue().isEmpty()){
				try {
					m_dCurrentPaymentAmount = new BigDecimal(m_oTextboxField.getValue());
				} catch (NumberFormatException e) {
					m_dCurrentPaymentAmount = BigDecimal.ZERO;
				}
				String sValue;
				if(m_dCurrentPaymentAmount.compareTo(BigDecimal.ZERO) == 0)
					sValue = BigDecimal.ZERO.stripTrailingZeros().toPlainString();
				else
					sValue = m_dCurrentPaymentAmount.stripTrailingZeros().toPlainString();
				
				// Check the payment amount for mobile view as not allow multiple payments
				if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
					if(m_dOriginalPaymentAmount.compareTo(m_dCurrentPaymentAmount) > 0) {
						synchronized(this){
							m_bNumberPadKeyProcessing = false;
						}
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("not_allow_multiple_payments"));
						oFormDialogBox.show();
						oFormDialogBox = null;
						
						sValue = m_dOriginalPaymentAmount.stripTrailingZeros().toPlainString();
						m_oTextboxField.setValue(sValue);
						m_oTextboxField.setFocus();
						
						return;
					}
				}
				setFieldValue(1, sValue);
			}
			setEditField(1, false);
			
			if (m_bCurrentNeedAskTipsAmount) {
				m_iCurrentPaymentStep = 2;
				if (m_bPayInForeignCurrency) {
					BigDecimal dOriginalPaymentAmtInForeignCurrency = m_dOriginalPaymentAmount.multiply(m_oCurrency.getRate());
					dOriginalPaymentAmtInForeignCurrency = m_oPosPaymentMethod.currencyRoundUpAmountWithCurrenctDecimalToBigDecimal(dOriginalPaymentAmtInForeignCurrency);
					
					if ((m_dCurrentPaymentAmount.compareTo(dOriginalPaymentAmtInForeignCurrency)) > 0) {
						m_dCurrentTipsAmount = m_dCurrentPaymentAmount.subtract(dOriginalPaymentAmtInForeignCurrency);
						m_dCurrentPaymentAmount = dOriginalPaymentAmtInForeignCurrency;
						updatePaymentTotalAndTipsLabelValue(m_dCurrentPaymentAmount, m_dCurrentTipsAmount);
					}
				} else {
					if ((m_dCurrentPaymentAmount.abs().compareTo(m_dOriginalPaymentAmount.abs())) > 0) {
						m_dCurrentTipsAmount = m_dCurrentPaymentAmount.subtract(m_dOriginalPaymentAmount);
						m_dCurrentPaymentAmount = m_dOriginalPaymentAmount;
						updatePaymentTotalAndTipsLabelValue(m_dCurrentPaymentAmount, m_dCurrentTipsAmount);
					}
				}
				VirtualUIBasicElement oElement = setEditField(2, true);
				m_oFrameNumberPad.clearEnterSubmitId();
				m_oFrameNumberPad.setEnterSubmitId(oElement);
			} else
				m_iCurrentPaymentStep = 3;
		} else if (m_iCurrentPaymentStep == 2) {
			// Press enter to confirm tips amount
			if (!m_oTextboxField.getValue().isEmpty()) {
				m_dCurrentTipsAmount = new BigDecimal(m_oTextboxField.getValue());
				
				boolean bPass = false;
				// Fire event to FormMain to handle the tips input
				for (FrameDirectPaymentPanelListener listener : listeners) {
					bPass = listener.FrameDirectPaymentPanel_askTipsConfirmation(m_dCurrentPaymentAmount,
							m_dCurrentTipsAmount);
					if (bPass == false)
						break;
				}
				if (bPass == false) {
					synchronized (this) {
						m_bNumberPadKeyProcessing = false;
					}
					return;
				}
				
				BigDecimal oTips = (m_dCurrentTipsAmount.compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO: m_dCurrentTipsAmount;
				setFieldValue(2, oTips.stripTrailingZeros().toPlainString());
			}
			setEditField(2, false);

			m_iCurrentPaymentStep = 3;
		}
		if(m_iCurrentPaymentStep == 3){
			// Finish asking value
			// Fire event to FormMain to handle the payment
			for (FrameDirectPaymentPanelListener listener : listeners) {
				listener.FrameDirectPaymentPanel_finishAskAmount(m_dCurrentPaymentAmount, m_dCurrentTipsAmount);
			}
		}
		
		synchronized(this){
			m_bNumberPadKeyProcessing = false;
		}
	}

	@Override
	public void FrameNumberPad_clickCancel() {
		//In order to prevent fire two key event together, it need to check whether processing any key event first
		//If it is not handling any key event, set the m_bNumberPadKeyProcessing to true
		synchronized(this){
			if(m_bNumberPadKeyProcessing)
				return;
			else
				m_bNumberPadKeyProcessing = true;
		}
		
		for (FrameDirectPaymentPanelListener listener : listeners) {
			listener.FrameDirectPaymentPanel_cancel();
		}
	}

	@Override
	public void frameCommonLookupButtons_addItem(String sNote) {
		BigDecimal oCurrentTotal;

		int iIndex = 0;
		try {
			JSONObject oJSONObject = new JSONObject(sNote);
			iIndex = oJSONObject.optInt(FrameLookupButton.BUTTON_NOTE_SEQ);
		} catch (JSONException e) {
			AppGlobal.stack2Log(e);
		}
		
		if(m_bNumberPad) {
			oCurrentTotal = m_oTenderAmountList.get(iIndex);
			m_bNumberPad = false;
		} else {
			String sValue = "0.0";
			if(m_oTextboxField.getValue().trim().length() > 0)
				sValue = m_oTextboxField.getValue();
			oCurrentTotal = new BigDecimal(sValue);
			oCurrentTotal = oCurrentTotal.add(m_oTenderAmountList.get(iIndex));
		}
		
		m_oTextboxField.setValue(oCurrentTotal.toPlainString());
		m_oTextboxField.setReplaceValue("^.*$", oCurrentTotal.toPlainString()+"<select></select>"); // move cursor to the end

		if (m_oPosPaymentMethod.getSurchargeRate().compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal oPayTotal, oTips;
			if (m_oTextboxField.getTop() == this.m_oLabelTips.getTop()) {
				oPayTotal = m_dCurrentPaymentAmount;
				oTips = oCurrentTotal;
			} else {
				oPayTotal = oCurrentTotal;
				oTips = m_dCurrentTipsAmount;
			}
				m_oLabelSurcharge.setValue(calculateSurcharge(oPayTotal, oTips));
		}
	}

	@Override
	public void frameCommonLookupButtons_deleteItem(String sNote) {
	}

	@Override
	public void FrameNumberPad_clickNumber(String string) {
		if(!m_bNumberPad) {
			m_oTextboxField.setReplaceValue("^.*$", string+"<select></select>");
			m_bNumberPad = true;
		}
	}
	
	// ***** DEBUG *****
	public void autoClickEnter(){
		if(AppGlobal.g_iDebugMode == 1)
			m_oFrameNumberPad.getEnterButton().doAutoClick(100);
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId,
			int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
	}

	@Override
	public void frameCommonLookupButtons_swipePage(boolean bLeft) {
		if (bLeft)
			m_oTenderAmountButtons.prevPage();
		else
			m_oTenderAmountButtons.nextPage();
	}

	@Override
	public void FrameTitleHeader_close() {
		for (FrameDirectPaymentPanelListener listener : listeners) {
			// Find the clicked button
			listener.FrameDirectPaymentPanel_cancel();
		}
	}
}
