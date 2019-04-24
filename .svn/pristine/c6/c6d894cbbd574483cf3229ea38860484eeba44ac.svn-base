package app;

import java.math.BigDecimal;
import java.util.ArrayList;

import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FramePaymentResultListener {
}

public class FramePaymentResult extends VirtualUIFrame implements FrameCommonBasketListener {
	TemplateBuilder m_oTemplateBuilder;
	
	// Check Action Events
	public static final String PAYMENT_TYPE_TIPS = "payment_tips_type";
	public static final String PAYMENT_TYPE_CHANGES = "payment_change_type";
	
	// Result page
	private FrameCommonBasket m_oCommonBasket;
	private VirtualUILabel m_oLabelTitle;
	private VirtualUILabel m_oLabelCheckNoHeader;
	private VirtualUILabel m_oLabelCheckNo;
	private VirtualUILabel m_oLabelTotalHeader;
	private VirtualUILabel m_oLabelTotal;
	private VirtualUILabel m_oLabelTipsHeader;
	private VirtualUILabel m_oLabelTips;
	private VirtualUILabel m_oLabelSurchargeHeader;
	private VirtualUILabel m_oLabelSurcharge;
	private VirtualUILabel m_oLabelResidueHeader;
	private VirtualUILabel m_oLabelResidue;
	private VirtualUILabel m_oLabelChangeHeader;
	private VirtualUILabel m_oLabelChange;
	private VirtualUILabel m_oLabelTotalDueHeader;
	private VirtualUILabel m_oLabelTotalDue;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FramePaymentResultListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FramePaymentResultListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FramePaymentResultListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
	public FramePaymentResult(String sTemplateFile){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FramePaymentResultListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate(sTemplateFile);
		
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
		
		m_oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTitle, "lblResultTitle");
		m_oLabelTitle.setValue(AppGlobal.g_oLang.get()._("pay_successfully", ""));
		this.attachChild(m_oLabelTitle);
		
		m_oLabelCheckNoHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckNoHeader, "lblResultCheckNoHeader");
		m_oLabelCheckNoHeader.setValue(AppGlobal.g_oLang.get()._("check_no", ""));
		this.attachChild(m_oLabelCheckNoHeader);
		
		m_oLabelCheckNo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckNo, "lblResultCheckNo");
		this.attachChild(m_oLabelCheckNo);
		
		m_oLabelTotalHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTotalHeader, "lblResultTotalHeader");
		m_oLabelTotalHeader.setValue(AppGlobal.g_oLang.get()._("total", ""));
		this.attachChild(m_oLabelTotalHeader);
		
		m_oLabelTotal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTotal, "lblResultTotal");
		this.attachChild(m_oLabelTotal);
		
		m_oLabelTipsHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTipsHeader, "lblResultTipsHeader");
		m_oLabelTipsHeader.setValue(AppGlobal.g_oLang.get()._("tips", ""));
		this.attachChild(m_oLabelTipsHeader);
		
		m_oLabelTips = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTips, "lblResultTips");
		this.attachChild(m_oLabelTips);
		
		m_oLabelSurchargeHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSurchargeHeader, "lblResultSurchargeHeader");
		m_oLabelSurchargeHeader.setValue(AppGlobal.g_oLang.get()._("surcharge", ""));
		this.attachChild(m_oLabelSurchargeHeader);
		
		m_oLabelSurcharge = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSurcharge, "lblResultSurcharge");
		this.attachChild(m_oLabelSurcharge);
		
		m_oLabelResidueHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelResidueHeader, "lblResultResidueHeader");
		m_oLabelResidueHeader.setValue(AppGlobal.g_oLang.get()._("residue", ""));
		this.attachChild(m_oLabelResidueHeader);
		
		m_oLabelResidue = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelResidue, "lblResultResidue");
		this.attachChild(m_oLabelResidue);
		
		m_oLabelTotalDueHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTotalDueHeader, "lblResultTotalDueHeader");
		m_oLabelTotalDueHeader.setValue(AppGlobal.g_oLang.get()._("total_due", ""));
		this.attachChild(m_oLabelTotalDueHeader);
		
		m_oLabelTotalDue = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTotalDue, "lblResultTotalDue");
		this.attachChild(m_oLabelTotalDue);
		
		m_oLabelChangeHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelChangeHeader, "lblResultChangeHeader");
		m_oLabelChangeHeader.setValue(AppGlobal.g_oLang.get()._("change", ""));
		this.attachChild(m_oLabelChangeHeader);
		
		m_oLabelChange = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelChange, "lblResultChange");
		this.attachChild(m_oLabelChange);
	}
	
	public void addPayment(int iSectionId, int iItemIndex, String sDesc, String sInfo1, String sInfo2, BigDecimal dPayAmt, BigDecimal dTips, BigDecimal dPaymentTotal, boolean bDefaultPaymentTotal){
    	ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
    	ArrayList<String> sFieldValues = new ArrayList<String>();
    	ArrayList<String> sFieldAligns = new ArrayList<String>();
    	iFieldWidths.add(m_oCommonBasket.getWidth()/4*2);
    	sFieldValues.add(sDesc);
    	sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
    	iFieldWidths.add(m_oCommonBasket.getWidth()/4);
    	String sValue;
    	if(bDefaultPaymentTotal) {
    		if(dPayAmt.compareTo(dPaymentTotal) > 0)
    			sValue = dPaymentTotal.stripTrailingZeros().toPlainString();
    		else if(dPayAmt.compareTo(BigDecimal.ZERO) == 0)
    			sValue = (BigDecimal.ZERO).stripTrailingZeros().toPlainString();
    		else
    			sValue = dPayAmt.stripTrailingZeros().toPlainString();
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
    	
    	// Add information
    	if(sInfo1 != null && sInfo1.length() > 0){
    		m_oCommonBasket.setFieldInfo1(iSectionId, iItemIndex, 0, sInfo1);
    		if(sInfo2 != null && sInfo2.length() > 0){
    			m_oCommonBasket.setFieldInfo2(iSectionId, iItemIndex, 0, sInfo2);
    		}
    	}
    	
    	m_oCommonBasket.moveScrollToItem(iSectionId, iItemIndex);
    }
	
	public void updateBasketPaymentAmount(int iSectionId, int iItemIndex, String sPayAmt){
    	m_oCommonBasket.setFieldValue(iSectionId, iItemIndex, 1, sPayAmt);
    }
    
    public void updateBasketPaymentTips(int iSectionId, int iItemIndex, String sTips){
    	m_oCommonBasket.setFieldValue(iSectionId, iItemIndex, 2, sTips);
    }
	
	public void removePayment(int iSectionId, int iItemIndex){
    	m_oCommonBasket.removeItem(iSectionId, iItemIndex);
    }
	
	public void showPaymentResult(boolean bShow, String sCheckNo, String sTable, String sTableExtension, String sCheckTotal, String sTipsTotal, String sSurchargeTotal, String sResidueTotal, String sChangeTotal, boolean bChangeInForeignCurrency, String[] sCurrencyName, String sCurrencySign){
		if(bShow){
			BigDecimal dResidueTotal = new BigDecimal(sResidueTotal);
			m_oLabelCheckNo.setValue(sCheckNo);
			m_oLabelTotal.setValue(sCheckTotal);
			
			boolean bResidueVisible = m_oLabelResidueHeader.getVisible();
			int iTopOfTotalDueHeader = m_oLabelTotalDueHeader.getTop();
			if(dResidueTotal.compareTo(BigDecimal.ZERO) == 0){
				if(bResidueVisible)
					iTopOfTotalDueHeader -= 40;
				m_oLabelResidueHeader.setVisible(false);
				m_oLabelResidue.setVisible(false);
			}else {
				if(!bResidueVisible)
					iTopOfTotalDueHeader += 40;
				m_oLabelResidueHeader.setVisible(true);
				m_oLabelResidue.setValue(sResidueTotal);
				m_oLabelResidue.setVisible(true);
			}
			m_oLabelTotalDueHeader.setTop(iTopOfTotalDueHeader);
			m_oLabelTotalDue.setTop(iTopOfTotalDueHeader);
			m_oLabelChangeHeader.setTop(iTopOfTotalDueHeader + 40);
			m_oLabelChange.setTop(iTopOfTotalDueHeader + 70);
			
			m_oLabelTips.setValue(sTipsTotal);
			m_oLabelSurcharge.setValue(sSurchargeTotal);
			BigDecimal dTotalDue = new BigDecimal(sCheckTotal).add(new BigDecimal(sTipsTotal)).add(new BigDecimal(sSurchargeTotal)).add(new BigDecimal(sResidueTotal));
			m_oLabelTotalDue.setValue(StringLib.BigDecimalToString(dTotalDue, AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal()));
			if(bChangeInForeignCurrency) {
				m_oLabelChangeHeader.setValue(AppGlobal.g_oLang.get()._("change", " [", sCurrencyName, "]"));
				m_oLabelChange.setValue(sCurrencySign + sChangeTotal);
			}else {
				m_oLabelChangeHeader.setValue(AppGlobal.g_oLang.get()._("change", ""));
				m_oLabelChange.setValue(sChangeTotal);
			}
		} else
			this.clearPaymentBasket();
	}
	
	public void clearPaymentBasket(){
		m_oCommonBasket.removeAllSections();
		m_oCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
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
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId,
			int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
	}
}
