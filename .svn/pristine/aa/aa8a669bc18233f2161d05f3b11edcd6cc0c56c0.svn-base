package app;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import externallib.StringLib;
import om.PosTaxScType;
import om.PosDisplayPanelZone;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;
import app.AppGlobal;

/** interface for the listeners/observers callback method */
interface FrameOrderingBasketCellListener {
	void frameOrderingBasketCell_QtyClicked(int iCellId, String sNote);
	String frameOrderingBasketCell_TempQtyClicked(int iCellId, String sNote, String sOriQty);
	void frameOrderingBasketCell_ReceiveQtyClicked(int iCellId, String sNote);
	void frameOrderingBasketCell_DescClicked(int iCellId, String sNote);
	void frameOrderingBasketCell_DescLongClicked(int iCellId, String sNote);
	void frameOrderingBasketCell_PriceClicked(int iCellId, String sNote);
	void frameOrderingBasketCell_DeleteClicked(int iCellId, String sNote);
	void frameOrderingBasketCell_AddQtyClicked(int iCellId, String sNote);
	boolean frameOrderingBasketCell_AddQtyClickedForOldItem(int iCellId, String sNote, BigDecimal dNewQty);
	void frameOrderingBasketCell_MinusQtyClicked(int iCellId, String sNote);
	boolean frameOrderingBasketCell_MinusQtyClickedForOldItem(int iCellId, String sNote, BigDecimal dNewQty);
	void frameOrderingBasketChildItemCell_QtyClicked(int iCellId, int iIndex, String sNote);
	void frameOrderingBasketChildItemCell_DescClicked(int iCellId, int iIndex, String sNote);
	void frameOrderingBasketChildItemCell_PriceClicked(int iCellId, int iIndex, String sNote);
	void frameOrderingBasketModifierCell_QtyClicked(int iCellId, int iIndex, String sNote);
	void frameOrderingBasketModifierCell_DescClicked(int iCellId, int iIndex, String sNote);
	void frameOrderingBasketModifierCell_PriceClicked(int iCellId, int iIndex, String sNote);
}

public class FrameOrderingBasketCell extends VirtualUIFrame implements FrameOrderingBasketChildItemCellListener, FrameOrderingBasketModifierCellListener{
	TemplateBuilder m_oTemplateBuilder;
	
	private ArrayList<FrameOrderingBasketChildItemCell> m_oChildItemCellList;
	private ArrayList<FrameOrderingBasketModifierCell> m_oModifierCellList;
	
	// Basket Main List View
	private VirtualUIList m_oOrderingBasketList;

	private VirtualUIFrame m_oFrameModifierList;
	private VirtualUIFrame m_oFrameItemDisplayInformationList;
	private VirtualUILabel m_oLabelDiscount;
	private VirtualUILabel m_oLabelQty;
	private VirtualUILabel m_oLabelSeq;
	
	private VirtualUILabel m_oLabelTempQty;
	private VirtualUILabel m_oLabelQtyBg;
	private VirtualUILabel m_oLabelReceiveQty;
	private VirtualUILabel m_oLabelDesc;
	private VirtualUILabel m_oLabelInfo;
	private VirtualUILabel m_oLabelPantryMessage;
	private VirtualUILabel m_oLabelPrice;

	private VirtualUILabel m_oLabelAddTaxSc;
	private VirtualUILabel m_oLabelWaiveTaxSc;
	private VirtualUIButton m_oButtonDelete;
	
	private VirtualUILabel m_oLabelAddQty;
	private VirtualUILabel m_oLabelMinusQty;
	
	private VirtualUIImage m_oImageSelected;
	private VirtualUIImage m_oImageDeselected;
	
	private VirtualUILabel m_oLabelChildItemForegroundColor;
	private VirtualUIFrame m_oFrameUnderline;
	
	private boolean m_bIsOldItem;
	
	private boolean m_bIsFunctionButtonShow;
	
	private boolean m_bSelectMode;
	private boolean m_bIsConsolidateItemsBasket;
	
	private int m_iLabelWidth;
	private int m_iLabelLeft;
	private int m_iLabelMinusQtyLeft;
	private int m_iLabelQtyBgWidth ;
	private int m_iLabelQtyBgLeft ;
	private int m_iDescriptionLeft;
	private int m_iDescriptionLeftInSelection;
	private PosDisplayPanelZone m_oPosDisplayPanelZone;
	
	// Original color
	private String m_sInformationOriginalFontColor;
	
	private static final double RATIO_IMAGE_LEFT = 0.02;
	private static final double RATIO_LABEL_LEFT = 0.035;
	private static final double RATIO_QTY_WIDTH = 0.0875;
	
	private static final double RATIO_ADD_QTY_LEFT = 0.6925;
	private static final double RATIO_MINUS_QTY_LEFT = 0.545;
	private static final double RATIO_LABEL_QTY_LEFT = 0.6375;
	
	private static final double RATIO_ADD_QTY_LEFT_MOBILE = 0.7291;
	private static final double RATIO_MINUS_QTY_LEFT_MOBILE = 0.5416;
	private static final double RATIO_LABEL_QTY_LEFT_MOBILE = 0.6041;
	private static final double RATIO_QTY_WIDTH_MOBILE = 0.0625;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameOrderingBasketCellListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameOrderingBasketCellListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameOrderingBasketCellListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public void init(VirtualUIList oOrderingBasketList, boolean bIsOldItem, boolean bIsChildItem, PosDisplayPanelZone oOrderingBasketDisplayPanelZone, boolean bIsConsolidateItemsBasket) {
		this.m_bIsConsolidateItemsBasket = bIsConsolidateItemsBasket;
		
		m_oTemplateBuilder = new TemplateBuilder();
		m_oChildItemCellList = new ArrayList<FrameOrderingBasketChildItemCell>();
		m_oModifierCellList = new ArrayList<FrameOrderingBasketModifierCell>();
		listeners = new ArrayList<FrameOrderingBasketCellListener>();
		m_oPosDisplayPanelZone = oOrderingBasketDisplayPanelZone;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraOrderingBasketCell.xml");
		
		//Set the ration according to the version 
		double dLabelAddQtyWidthRatio, dLabelAddQtyLeftRatio, dLabelQtyLeftRatio, dLabelMinusQtyLeftRatio, dLabelMinusQtyWidthRatio;
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())){
			dLabelAddQtyLeftRatio = RATIO_ADD_QTY_LEFT_MOBILE;
			dLabelAddQtyWidthRatio = RATIO_QTY_WIDTH_MOBILE;
			dLabelQtyLeftRatio = RATIO_LABEL_QTY_LEFT_MOBILE;
			dLabelMinusQtyLeftRatio = RATIO_MINUS_QTY_LEFT_MOBILE;
			dLabelMinusQtyWidthRatio = RATIO_QTY_WIDTH_MOBILE;
		}else{
			dLabelAddQtyLeftRatio = RATIO_ADD_QTY_LEFT;
			dLabelAddQtyWidthRatio = RATIO_QTY_WIDTH;
			dLabelQtyLeftRatio = RATIO_LABEL_QTY_LEFT;
			dLabelMinusQtyLeftRatio = RATIO_MINUS_QTY_LEFT;
			dLabelMinusQtyWidthRatio = RATIO_QTY_WIDTH;
		}
		
		// Add Quantity image
		m_oLabelAddQty = new VirtualUILabel();
		
		m_oTemplateBuilder.buildLabel(m_oLabelAddQty, "lblAddQty");
		m_oLabelAddQty.setLeft((int)(dLabelAddQtyLeftRatio*this.getWidth()));
		m_oLabelAddQty.setWidth((int)(dLabelAddQtyWidthRatio * this.getWidth()));
		m_oLabelAddQty.allowClick(true);
		m_oLabelAddQty.setClickServerRequestBlockUI(false);
		m_oLabelAddQty.setValue("+");
		if(!bIsOldItem && !bIsChildItem)
			m_oLabelAddQty.setVisible(true);
		else
			m_oLabelAddQty.setVisible(false);
		this.attachChild(m_oLabelAddQty);

		// Section quantity background
		m_oLabelQtyBg = new VirtualUILabel();
		
		double dLabelQtyBgLeftRatio = 0.62;
		double dLabelQtyBgWidthRatio = RATIO_QTY_WIDTH;
		m_oTemplateBuilder.buildLabel(m_oLabelQtyBg, "lblQtyBg");

		m_oLabelQtyBg.setLeft((int)(dLabelQtyBgLeftRatio * this.getWidth()));
		m_oLabelQtyBg.setWidth((int)(dLabelQtyBgWidthRatio * this.getWidth()));
		m_oLabelQtyBg.setClickServerRequestBlockUI(false);
		if(!bIsOldItem && !bIsChildItem)
			m_oLabelQtyBg.setVisible(true);
		else 
			m_oLabelQtyBg.setVisible(false);
		this.attachChild(m_oLabelQtyBg);

		// Section quantity
		m_oLabelQty = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelQty, "lblQuantity");

		m_oLabelQty.setLeft((int)(dLabelQtyLeftRatio * this.getWidth()));
		if (m_oPosDisplayPanelZone != null && m_oPosDisplayPanelZone.getBasketItemQuantityFontSize() > 0)
			m_oLabelQty.setTextSize(m_oPosDisplayPanelZone.getBasketItemQuantityFontSize());
		m_oLabelQty.setEnabled(true);
		m_oLabelQty.allowClick(true);
		m_oLabelQty.setClickServerRequestBlockUI(false);
		this.attachChild(m_oLabelQty);
		// Temp Section quantity
		m_oLabelTempQty = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTempQty, "lblTempQuantity");

		m_oLabelTempQty.setLeft((int)(dLabelQtyLeftRatio * this.getWidth()));
		if (m_oPosDisplayPanelZone != null && m_oPosDisplayPanelZone.getBasketItemQuantityFontSize() > 0)
			m_oLabelTempQty.setTextSize(m_oPosDisplayPanelZone.getBasketItemQuantityFontSize());
		m_oLabelTempQty.setEnabled(true);
		m_oLabelTempQty.allowClick(true);
		m_oLabelTempQty.setClickServerRequestBlockUI(false);
		this.attachChild(m_oLabelTempQty);
		m_oLabelTempQty.setVisible(false);
		
		m_oLabelReceiveQty = new VirtualUILabel();
		
		// Minus Quantity image
		m_oLabelMinusQty = new VirtualUILabel();

		m_oTemplateBuilder.buildLabel(m_oLabelMinusQty, "lblMinusQty");

		m_oLabelMinusQty.setLeft((int)(dLabelMinusQtyLeftRatio * this.getWidth()));
		m_oLabelMinusQty.setWidth((int)(dLabelMinusQtyWidthRatio * this.getWidth()));
		m_oLabelMinusQty.allowClick(true);
		m_oLabelMinusQty.setClickServerRequestBlockUI(false);
		m_oLabelMinusQty.setValue("-");
		if(!bIsOldItem && !bIsChildItem)
			m_oLabelMinusQty.setVisible(true);
		else
			m_oLabelMinusQty.setVisible(false);
		this.attachChild(m_oLabelMinusQty);
		
		// Section description
		m_oLabelDesc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDesc, "lblDescription");

		double dLabelDescLeftRatio = RATIO_LABEL_LEFT;
		double dLabelDescWidthRatio = 0.45;
		m_oLabelDesc.setLeft((int)(dLabelDescLeftRatio * this.getWidth()));
		m_oLabelDesc.setWidth((int)(dLabelDescWidthRatio * this.getWidth()));
		m_oLabelDesc.setEnabled(true);
		m_oLabelDesc.allowClick(true);
		m_oLabelDesc.setClickServerRequestBlockUI(false);
		m_oLabelDesc.setLongClickServerRequestBlockUI(true);
		
		m_oLabelSeq = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSeq, "lblItemSeq");

		double dLabelSeqLeftRatio = RATIO_LABEL_LEFT;
		m_oLabelSeq.setLeft((int)(dLabelSeqLeftRatio*this.getWidth()));
		m_oLabelSeq.setWidth((int) (0.06 * this.getWidth()));
		m_oLabelSeq.setEnabled(true);
		if (m_oPosDisplayPanelZone != null && m_oPosDisplayPanelZone.getBasketItemDescriptionFontSize() > 0)
			m_oLabelSeq.setTextSize(m_oPosDisplayPanelZone.getBasketItemDescriptionFontSize());
		
		if (m_oPosDisplayPanelZone.isDisplayItemSequence()) {
			this.attachChild(m_oLabelSeq);
			m_oLabelDesc.setLeft(m_oLabelSeq.getLeft() + m_oLabelSeq.getWidth());
		}
		m_iDescriptionLeft = m_oLabelDesc.getLeft();
		if (m_oPosDisplayPanelZone != null && m_oPosDisplayPanelZone.getBasketItemDescriptionFontSize() > 0)
			m_oLabelDesc.setTextSize(m_oPosDisplayPanelZone.getBasketItemDescriptionFontSize());
		this.attachChild(m_oLabelDesc);
		
		VirtualUILabel m_oTempDesc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oTempDesc, "lblDescriptionInSelection");

		double dLabelTempDescLeftRatio = 0.13;
		m_oTempDesc.setLeft((int)(dLabelTempDescLeftRatio*this.getWidth()));
		m_iDescriptionLeftInSelection = m_oTempDesc.getLeft();

		// Section Information (e.g. delivered, pre-order)
		m_oLabelInfo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelInfo, "lblInfo");

		double dLabelInfoLeftRatio = RATIO_LABEL_LEFT;
		m_oLabelInfo.setLeft((int)(dLabelInfoLeftRatio*this.getWidth()));
		if (m_oPosDisplayPanelZone != null && m_oPosDisplayPanelZone.getBasketItemDescriptionFontSize() > 0)
			m_oLabelInfo.setTextSize(m_oPosDisplayPanelZone.getBasketItemDescriptionFontSize() - 2);
		m_oLabelInfo.setEnabled(true);
		m_oLabelInfo.allowClick(true);
		m_oLabelInfo.setClickServerRequestBlockUI(false);
		m_oLabelInfo.setLongClickServerRequestBlockUI(true);
		m_oLabelInfo.setVisible(false);
		this.attachChild(m_oLabelInfo);
		
		// Section Pantry Message 
		m_oLabelPantryMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelPantryMessage, "lblPantryMessage");

		double dLabelPantryMsgLeftRatio = RATIO_LABEL_LEFT;
		m_oLabelPantryMessage.setLeft((int)(dLabelPantryMsgLeftRatio * this.getWidth()));
		m_oLabelPantryMessage.setEnabled(true);
		m_oLabelPantryMessage.allowClick(true);
		m_oLabelPantryMessage.setClickServerRequestBlockUI(false);
		m_oLabelPantryMessage.setLongClickServerRequestBlockUI(true);
		m_oLabelPantryMessage.setVisible(false);
		this.attachChild(m_oLabelPantryMessage);
		
		// Display Information List
		m_oFrameItemDisplayInformationList = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameItemDisplayInformationList, "lblDisplayInformation");

		double dFrameItemDisplayLeftRatio = RATIO_LABEL_LEFT;
		m_oFrameItemDisplayInformationList.setLeft((int)(dFrameItemDisplayLeftRatio * this.getWidth()));
		m_oFrameItemDisplayInformationList.setEnabled(true);
		m_oFrameItemDisplayInformationList.allowClick(true);
		m_oFrameItemDisplayInformationList.setClickServerRequestBlockUI(false);
		m_oFrameItemDisplayInformationList.setLongClickServerRequestBlockUI(true);
		this.attachChild(m_oFrameItemDisplayInformationList);
		
		// Modifier list
		m_oFrameModifierList = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameModifierList, "fraModifierList");

		double dFrameModifierListLeftRatio = RATIO_LABEL_LEFT;
		m_oFrameModifierList.setLeft((int)(dFrameModifierListLeftRatio * this.getWidth()));
		m_oFrameModifierList.setEnabled(true);
		m_oFrameModifierList.allowClick(true);
		m_oFrameModifierList.setClickServerRequestBlockUI(false);
		m_oFrameModifierList.setLongClickServerRequestBlockUI(true);
		m_oFrameModifierList.setVisible(false);
		this.attachChild(m_oFrameModifierList);
		
		// Discount information
		m_oLabelDiscount = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDiscount, "lblModifierDiscount");

		double dLabelDiscountLeftRatio = RATIO_LABEL_LEFT;
		m_oLabelDiscount.setLeft((int)(dLabelDiscountLeftRatio * this.getWidth()));
		m_oLabelDiscount.setEnabled(true);
		m_oLabelDiscount.allowClick(true);
		m_oLabelDiscount.setClickServerRequestBlockUI(false);
		m_oLabelDiscount.setLongClickServerRequestBlockUI(true);
		m_oLabelDiscount.setVisible(false);
		this.attachChild(m_oLabelDiscount);
		
		// Store the original font color of information
		m_sInformationOriginalFontColor = m_oLabelInfo.getForegroundColor();
		
		// Section price
		m_oLabelPrice = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelPrice, "lblPrice");

		double dLabelPriceLeftRatio = 0.8;
		double dLabelPriceWidthRatio = 0.18;
		m_oLabelPrice.setLeft((int) (dLabelPriceLeftRatio * this.getWidth()));
		m_oLabelPrice.setWidth((int) (dLabelPriceWidthRatio * this.getWidth()));
		if (m_oPosDisplayPanelZone != null && m_oPosDisplayPanelZone.getBasketItemPriceFontSize() > 0)
			m_oLabelPrice.setTextSize(m_oPosDisplayPanelZone.getBasketItemPriceFontSize());
		m_oLabelPrice.setEnabled(true);
		m_oLabelPrice.allowClick(true);
		m_oLabelPrice.setClickServerRequestBlockUI(false);
		this.attachChild(m_oLabelPrice);
		
		m_oLabelChildItemForegroundColor = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelChildItemForegroundColor, "lblChildItemColor");

		double dLabelChildItemColorLeftRatio = 0.81;
		m_oLabelChildItemForegroundColor.setLeft((int) (dLabelChildItemColorLeftRatio * this.getWidth()));
		this.attachChild(m_oLabelChildItemForegroundColor);
		if (bIsChildItem) {
			m_oLabelQty.setForegroundColor(m_oLabelChildItemForegroundColor.getForegroundColor());
			m_oLabelDesc.setForegroundColor(m_oLabelChildItemForegroundColor.getForegroundColor());
			m_oLabelPrice.setForegroundColor(m_oLabelChildItemForegroundColor.getForegroundColor());
		}
		
		// Ordering basket cell underline
		m_oFrameUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameUnderline, "fraOrderingBasketUnderline");

		double dFrameUnderlineLeftRatio = RATIO_IMAGE_LEFT;
		m_oFrameUnderline.setLeft((int)(dFrameUnderlineLeftRatio * this.getWidth()));
		m_oFrameUnderline.setWidth((int)(this.getWidth() - 2 * m_oFrameUnderline.getLeft()));
		m_oFrameUnderline.setVisible(false);
		this.attachChild(m_oFrameUnderline);
		// Delete Button
		m_oButtonDelete = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonDelete, "butDelete");

		double dBtnDeleteLeftRatio = 0.81;
		m_oButtonDelete.setLeft((int)(dBtnDeleteLeftRatio * this.getWidth()));
		m_oButtonDelete.setValue(AppGlobal.g_oLang.get()._("delete", ""));
		m_oButtonDelete.setVisible(false);
		this.attachChild(m_oButtonDelete);
		
		// Selected image
		m_oImageSelected = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageSelected, "ImgSelected");

		double dImgSelectLeftRatio = RATIO_IMAGE_LEFT;
		m_oImageSelected.setLeft((int)(dImgSelectLeftRatio * this.getWidth()));
		m_oImageSelected.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/checkbox_h.png");
		m_oImageSelected.setVisible(false);
		m_oImageSelected.allowClick(true);
		m_oImageSelected.setClickServerRequestBlockUI(false);
		this.attachChild(m_oImageSelected);
		
		// Deselected image
		m_oImageDeselected = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageDeselected, "ImgDeselected");

		double dImgDeselectLeftRatio = RATIO_IMAGE_LEFT;
		m_oImageDeselected.setLeft((int)(dImgDeselectLeftRatio * this.getWidth()));
		m_oImageDeselected.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/checkbox_n.png");
		m_oImageDeselected.setVisible(false);
		m_oImageDeselected.allowClick(true);
		m_oImageDeselected.setClickServerRequestBlockUI(false);
		this.attachChild(m_oImageDeselected);
		
		// added tax or service charge label for
		m_oLabelAddTaxSc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAddTaxSc, "lblAddTaxSc");
		m_oLabelAddTaxSc.setEnabled(true);
		m_oLabelAddTaxSc.allowClick(true);
		m_oLabelAddTaxSc.setClickServerRequestBlockUI(false);
		m_oLabelAddTaxSc.setLongClickServerRequestBlockUI(true);
		m_oLabelAddTaxSc.setVisible(false);
		this.attachChild(m_oLabelAddTaxSc);
		
		// waived tax or service charge label
		m_oLabelWaiveTaxSc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelWaiveTaxSc, "lblWaiveTaxSc");
		m_oLabelWaiveTaxSc.setEnabled(true);
		m_oLabelWaiveTaxSc.allowClick(true);
		m_oLabelWaiveTaxSc.setClickServerRequestBlockUI(false);
		m_oLabelWaiveTaxSc.setLongClickServerRequestBlockUI(true);
		m_oLabelWaiveTaxSc.setVisible(false);
		this.attachChild(m_oLabelWaiveTaxSc);
		// Set Main List View
		m_oOrderingBasketList = oOrderingBasketList;
		
		// Select mode flag
		m_bSelectMode = false;
		
		m_bIsOldItem = bIsOldItem;
		
		//Set the original oLabelQty from fraorderingBasketCell.xml	
		m_iLabelWidth = m_oLabelQty.getWidth();
		m_iLabelLeft = m_oLabelQty.getLeft();
		m_iLabelMinusQtyLeft = m_oLabelMinusQty.getLeft();
			
		//Set the original oLabelQtyBg from fraorderingBasketCell.xml
		m_iLabelQtyBgWidth =  m_oLabelQtyBg.getWidth();
		m_iLabelQtyBgLeft =  m_oLabelQtyBg.getLeft();
		
		if(this.m_bIsConsolidateItemsBasket){
			this.setAddMinusVisible(false);
			m_oLabelQtyBg.setVisible(false);
		}
	}
	
	public void initForSimpleMode(VirtualUIList oOrderingBasketList, boolean bIsOldItem, boolean bIsConsolidateItemsBasket) {
		this.m_bIsConsolidateItemsBasket = bIsConsolidateItemsBasket;
		
		m_oTemplateBuilder = new TemplateBuilder();
		m_oChildItemCellList = new ArrayList<FrameOrderingBasketChildItemCell>();
		m_oModifierCellList = new ArrayList<FrameOrderingBasketModifierCell>();
		listeners = new ArrayList<FrameOrderingBasketCellListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraOrderingBasketCell.xml");
		
		// Add Quantity image
		m_oLabelAddQty = new VirtualUILabel();
		
		// Section quantity background
		m_oLabelQtyBg = new VirtualUILabel();
		
		// Section quantity
		m_oLabelQty = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelQty, "lblQuantity");
		m_oLabelQty.setEnabled(true);
		m_oLabelQty.allowClick(true);
		m_oLabelQty.setClickServerRequestBlockUI(false);
		if(bIsOldItem)
			m_oLabelQty.setForegroundColor("#555555");
		this.attachChild(m_oLabelQty);
		
		m_oLabelReceiveQty = new VirtualUILabel();
		
		// Minus Quantity image
		m_oLabelMinusQty = new VirtualUILabel();
		
		// Section description
		m_oLabelDesc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDesc, "lblDescription");
		m_oLabelDesc.setEnabled(true);
		m_oLabelDesc.allowClick(true);
		m_oLabelDesc.setClickServerRequestBlockUI(false);
		m_oLabelDesc.setLongClickServerRequestBlockUI(true);
		if(bIsOldItem)
			m_oLabelDesc.setForegroundColor("#555555");
		this.attachChild(m_oLabelDesc);
		
		// Section Information
		m_oLabelInfo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelInfo, "lblInfo");
		m_oLabelInfo.setEnabled(true);
		m_oLabelInfo.allowClick(true);
		m_oLabelInfo.setClickServerRequestBlockUI(false);
		m_oLabelInfo.setLongClickServerRequestBlockUI(true);
		m_oLabelInfo.setVisible(false);
		this.attachChild(m_oLabelInfo);

		// Section Pantry Message
		m_oLabelPantryMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelPantryMessage, "lblPantryMessage");
		m_oLabelPantryMessage.setEnabled(true);
		m_oLabelPantryMessage.allowClick(true);
		m_oLabelPantryMessage.setClickServerRequestBlockUI(false);
		m_oLabelPantryMessage.setLongClickServerRequestBlockUI(true);
		m_oLabelPantryMessage.setVisible(false);
		this.attachChild(m_oLabelPantryMessage);
		
		// Discount Information
		m_oLabelDiscount = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDiscount, "lblModifierDiscount");
		m_oLabelDiscount.setEnabled(true);
		m_oLabelDiscount.allowClick(true);
		m_oLabelDiscount.setClickServerRequestBlockUI(false);
		m_oLabelDiscount.setLongClickServerRequestBlockUI(true);
		m_oLabelDiscount.setVisible(false);
		this.attachChild(m_oLabelDiscount);
		
		// Section price
		m_oLabelPrice = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelPrice, "lblPrice");
		m_oLabelPrice.setEnabled(true);
		m_oLabelPrice.allowClick(true);
		m_oLabelPrice.setClickServerRequestBlockUI(false);
		this.attachChild(m_oLabelPrice);
		
		// Ordering basket cell underline
		m_oFrameUnderline = new VirtualUIFrame();
		
		// Delete Button
		m_oButtonDelete = new VirtualUIButton();
		
		// Selected image
		m_oImageSelected = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageSelected, "ImgSelected");
		m_oImageSelected.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/basket_selected_image.png");
		m_oImageSelected.setVisible(false);
		m_oImageSelected.allowClick(true);
		m_oImageSelected.setClickServerRequestBlockUI(false);
		this.attachChild(m_oImageSelected);
		
		// Deselected image
		m_oImageDeselected = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageDeselected, "ImgDeselected");
		m_oImageDeselected.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/basket_deselected_image.png");
		m_oImageDeselected.setVisible(false);
		m_oImageDeselected.allowClick(true);
		m_oImageDeselected.setClickServerRequestBlockUI(false);
		this.attachChild(m_oImageDeselected);
		
		// added tax or service charge label for
		m_oLabelAddTaxSc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAddTaxSc, "lblAddTaxSc");
		m_oLabelAddTaxSc.setEnabled(true);
		m_oLabelAddTaxSc.allowClick(true);
		m_oLabelAddTaxSc.setClickServerRequestBlockUI(false);
		m_oLabelAddTaxSc.setLongClickServerRequestBlockUI(true);
		m_oLabelAddTaxSc.setVisible(false);
		this.attachChild(m_oLabelAddTaxSc);
		
		// waived tax or service charge label
		m_oLabelWaiveTaxSc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelWaiveTaxSc, "lblWaiveTaxSc");
		m_oLabelWaiveTaxSc.setEnabled(true);
		m_oLabelWaiveTaxSc.allowClick(true);
		m_oLabelWaiveTaxSc.setClickServerRequestBlockUI(false);
		m_oLabelWaiveTaxSc.setLongClickServerRequestBlockUI(true);
		m_oLabelWaiveTaxSc.setVisible(false);
		this.attachChild(m_oLabelWaiveTaxSc);
		// Set Main List View
		m_oOrderingBasketList = oOrderingBasketList;
		
		// Select mode flag
		m_bSelectMode = false;
		
		m_bIsOldItem = bIsOldItem;
		
		if(this.m_bIsConsolidateItemsBasket){
			this.setAddMinusVisible(false);
			m_oLabelQtyBg.setVisible(false);
		}
	}
	
	public void initForStockDeliveryMode(VirtualUIList oOrderingBasketList, boolean bIsOldItem, boolean bHideReceiveQty, boolean bIsConsolidateItemsBasket) {
		this.m_bIsConsolidateItemsBasket = bIsConsolidateItemsBasket;
		m_oTemplateBuilder = new TemplateBuilder();
		m_oChildItemCellList = new ArrayList<FrameOrderingBasketChildItemCell>();
		m_oModifierCellList = new ArrayList<FrameOrderingBasketModifierCell>();
		listeners = new ArrayList<FrameOrderingBasketCellListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraStockDeliveryOrderingBasketCell.xml");
		
		// Section description
		m_oLabelDesc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDesc, "lblDescription");
		m_oLabelDesc.setEnabled(true);
		m_oLabelDesc.allowClick(true);
		m_oLabelDesc.setClickServerRequestBlockUI(false);
		m_oLabelDesc.setLongClickServerRequestBlockUI(true);
		if(bIsOldItem)
			m_oLabelDesc.setForegroundColor("#555555");
		this.attachChild(m_oLabelDesc);
		
		m_oLabelQtyBg = new VirtualUILabel();
		
		// Ordering basket cell underline
		m_oFrameUnderline = new VirtualUIFrame();
		
		// Section Information
		m_oLabelInfo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelInfo, "lblInfo");
		m_oLabelInfo.setEnabled(true);
		m_oLabelInfo.allowClick(true);
		m_oLabelInfo.setClickServerRequestBlockUI(false);
		m_oLabelInfo.setLongClickServerRequestBlockUI(true);
		m_oLabelInfo.setVisible(false);
		this.attachChild(m_oLabelInfo);

		m_oLabelPantryMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelPantryMessage, "lblPantryMessage");
		m_oLabelPantryMessage.setEnabled(true);
		m_oLabelPantryMessage.allowClick(true);
		m_oLabelPantryMessage.setClickServerRequestBlockUI(false);
		m_oLabelPantryMessage.setLongClickServerRequestBlockUI(true);
		m_oLabelPantryMessage.setVisible(false);
		this.attachChild(m_oLabelPantryMessage);
		
		// Modifier list
		m_oFrameModifierList = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameModifierList, "fraModifierList");
		m_oFrameModifierList.setEnabled(true);
		m_oFrameModifierList.allowClick(true);
		m_oFrameModifierList.setClickServerRequestBlockUI(false);
		m_oFrameModifierList.setLongClickServerRequestBlockUI(true);
		m_oFrameModifierList.setVisible(false);
		this.attachChild(m_oFrameModifierList);
		
		// Display Information List
		m_oFrameItemDisplayInformationList = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameItemDisplayInformationList, "lblDisplayInformation");
		m_oFrameItemDisplayInformationList.setEnabled(true);
		m_oFrameItemDisplayInformationList.allowClick(true);
		m_oFrameItemDisplayInformationList.setClickServerRequestBlockUI(false);
		m_oFrameItemDisplayInformationList.setLongClickServerRequestBlockUI(true);
		m_oFrameItemDisplayInformationList.setVisible(false);
		this.attachChild(m_oFrameItemDisplayInformationList);
		
		// Discount Information
		m_oLabelDiscount = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDiscount, "lblModifierDiscount");
		m_oLabelDiscount.setEnabled(true);
		m_oLabelDiscount.allowClick(true);
		m_oLabelDiscount.setClickServerRequestBlockUI(false);
		m_oLabelDiscount.setLongClickServerRequestBlockUI(true);
		m_oLabelDiscount.setVisible(false);
		this.attachChild(m_oLabelDiscount);
		
		// added tax or service charge label for
		m_oLabelAddTaxSc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAddTaxSc, "lblAddTaxSc");
		m_oLabelAddTaxSc.setEnabled(true);
		m_oLabelAddTaxSc.allowClick(true);
		m_oLabelAddTaxSc.setClickServerRequestBlockUI(false);
		m_oLabelAddTaxSc.setLongClickServerRequestBlockUI(true);
		m_oLabelAddTaxSc.setVisible(false);
		this.attachChild(m_oLabelAddTaxSc);
		
		// waived tax or service charge label
		m_oLabelWaiveTaxSc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelWaiveTaxSc, "lblWaiveTaxSc");
		m_oLabelWaiveTaxSc.setEnabled(true);
		m_oLabelWaiveTaxSc.allowClick(true);
		m_oLabelWaiveTaxSc.setClickServerRequestBlockUI(false);
		m_oLabelWaiveTaxSc.setLongClickServerRequestBlockUI(true);
		m_oLabelWaiveTaxSc.setVisible(false);
		this.attachChild(m_oLabelWaiveTaxSc);
		if(bHideReceiveQty){
			// Section delivery quantity
			m_oLabelQty = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelQty, "lblDeliveryQuantity");
			m_oLabelQty.setEnabled(true);
			m_oLabelQty.allowClick(true);
			m_oLabelQty.setClickServerRequestBlockUI(false);
			this.attachChild(m_oLabelQty);
			
			// Re-locate the elements
			m_oLabelDesc.setWidth(m_oLabelDesc.getWidth() + m_oLabelQty.getWidth());
			m_oLabelInfo.setWidth(m_oLabelInfo.getWidth() + m_oLabelQty.getWidth());
			m_oLabelDiscount.setWidth(m_oLabelDiscount.getWidth() + m_oLabelQty.getWidth());
			m_oLabelPantryMessage.setWidth(m_oLabelPantryMessage.getWidth() + m_oLabelQty.getWidth());

			m_oLabelAddTaxSc.setWidth(m_oLabelAddTaxSc.getWidth() + m_oLabelQty.getWidth());
			m_oLabelWaiveTaxSc.setWidth(m_oLabelWaiveTaxSc.getWidth() + m_oLabelQty.getWidth());
			
			// Section receive quantity
			m_oLabelReceiveQty = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelReceiveQty, "lblReceiveQuantity");
			m_oLabelQty.setLeft(m_oLabelReceiveQty.getLeft());
			m_oLabelQty.setWidth(m_oLabelReceiveQty.getWidth());
			
		}else{
			// Section delivery quantity
			m_oLabelQty = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelQty, "lblDeliveryQuantity");
			m_oLabelQty.setEnabled(true);
			m_oLabelQty.allowClick(true);
			m_oLabelQty.setClickServerRequestBlockUI(false);
			this.attachChild(m_oLabelQty);
			
			// Section receive quantity
			m_oLabelReceiveQty = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelReceiveQty, "lblReceiveQuantity");
			m_oLabelReceiveQty.setEnabled(true);
			m_oLabelReceiveQty.allowClick(true);
			m_oLabelReceiveQty.setClickServerRequestBlockUI(false);
			this.attachChild(m_oLabelReceiveQty);
		}
		
		// Dummy object
		m_oLabelAddQty = new VirtualUILabel();
		m_oLabelMinusQty = new VirtualUILabel();
		m_oLabelPrice = new VirtualUILabel();
		m_oButtonDelete = new VirtualUIButton();
		m_oImageSelected = new VirtualUIImage();
		m_oImageDeselected = new VirtualUIImage();
		m_oLabelTempQty = new VirtualUILabel();
		
		// Set Main List View
		m_oOrderingBasketList = oOrderingBasketList;
		
		// Select mode flag
		m_bSelectMode = false;
		
		m_bIsOldItem = bIsOldItem;
		
		if(this.m_bIsConsolidateItemsBasket){
			this.setAddMinusVisible(false);
			m_oLabelQtyBg.setVisible(false);
		}
	}
	
	public void addChildItem(int iViewSeq, int iChildItemIndex, BigDecimal dQty, String[] sDesc, int iItemDecimal, BigDecimal dTotal, boolean bIsOldItem) {
		FrameOrderingBasketChildItemCell oFrameCell = new FrameOrderingBasketChildItemCell();
		oFrameCell.init();
		if(bIsOldItem)
			m_oTemplateBuilder.buildFrame(oFrameCell, "fraOrderingBasketOldChildItemCell");
		else
			m_oTemplateBuilder.buildFrame(oFrameCell, "fraOrderingBasketChildItemCell");
		oFrameCell.setCellWidth(this.getWidth());
		oFrameCell.addListener(this);
		
		oFrameCell.setQuantity(dQty.stripTrailingZeros().toPlainString());
		oFrameCell.setDescription(" - " + sDesc);
		if(dTotal.compareTo(BigDecimal.ZERO) > 0)
			oFrameCell.setPrice(StringLib.BigDecimalToString(dTotal, iItemDecimal));
		else
			oFrameCell.setPrice("");
		oFrameCell.setViewSeq(iViewSeq);
		if(bIsOldItem)
			oFrameCell.setLabelForegroundColor("#555555");

		m_oChildItemCellList.add(iChildItemIndex, oFrameCell);
		
		// Add to main list view
		m_oOrderingBasketList.attachChild(oFrameCell);
	}
	
	public void removeChildItem(int iChildItemIndex) {
		if(iChildItemIndex > m_oChildItemCellList.size()-1)
			return;

		FrameOrderingBasketChildItemCell oFrameCell = (FrameOrderingBasketChildItemCell)m_oChildItemCellList.get(iChildItemIndex);
		// Remove the object from the list view
		m_oOrderingBasketList.removeChild(oFrameCell.getId());
		
		// Remove from the child item list
		m_oChildItemCellList.remove(oFrameCell);
	}
	
	public void removeAllChildItem() {
		for(FrameOrderingBasketChildItemCell oFrameCell:m_oChildItemCellList){
			// Remove the object from the list view
			m_oOrderingBasketList.removeChild(oFrameCell.getId());
		}
		
		// Remove from the child item list
		m_oChildItemCellList.clear();
	}
	
	public void updateChildItemInfo(int iChildItemIndex, String sInfo) {
		if(iChildItemIndex > m_oChildItemCellList.size()-1)
			return;

		FrameOrderingBasketChildItemCell oFrameCell = (FrameOrderingBasketChildItemCell)m_oChildItemCellList.get(iChildItemIndex);
		oFrameCell.setInformation(sInfo);
	}
	
	public void addModifier(int iViewSeq, int iModifierIndex, BigDecimal dQty, String[] sDesc, int iItemDecimal, BigDecimal dTotal) {
		String sItemDecimal = "";
		FrameOrderingBasketModifierCell oFrameCell = new FrameOrderingBasketModifierCell();
		oFrameCell.init();
		m_oTemplateBuilder.buildFrame(oFrameCell, "fraOrderingBasketModifierCell");
		oFrameCell.setCellWidth(this.getWidth());
		if (m_oPosDisplayPanelZone != null && m_oPosDisplayPanelZone.getBasketItemDescriptionFontSize() - 2 > 0)
			oFrameCell.setTextSize(m_oPosDisplayPanelZone.getBasketItemDescriptionFontSize() - 2);
		
		oFrameCell.addListener(this);
		
		for(int i = 1; i <= iItemDecimal; i++) {
			if (i == 1)
				sItemDecimal = ".";
			sItemDecimal += "0";
		}
		DecimalFormat qtyFormat = new DecimalFormat("#0.00");
		DecimalFormat priceFormat = new DecimalFormat("#0"+sItemDecimal);
		oFrameCell.setQuantity(qtyFormat.format(dQty));
		oFrameCell.setDescription(sDesc);
		oFrameCell.setPrice(priceFormat.format(dTotal));
		oFrameCell.setViewSeq(iViewSeq);
		
		m_oModifierCellList.add(iModifierIndex, oFrameCell);
		
		// Add to main list view
		m_oOrderingBasketList.attachChild(oFrameCell);
	}
	
	public void removeModifier(int iModifierIndex) {
		if(iModifierIndex > m_oModifierCellList.size()-1)
			return;
		
		FrameOrderingBasketModifierCell oFrameCell = (FrameOrderingBasketModifierCell)m_oModifierCellList.get(iModifierIndex);
		// Remove the object from the list view
		m_oOrderingBasketList.removeChild(oFrameCell.getId());
		
		// Remove from the child item list
		m_oModifierCellList.remove(oFrameCell);
	}
	
	public void removeAllModifier() {
		for(FrameOrderingBasketModifierCell oFrameCell:m_oModifierCellList){
			// Remove the object from the list view
			m_oOrderingBasketList.removeChild(oFrameCell.getId());
		}
				
		// Remove from the child item list
		m_oModifierCellList.clear();
	}
	
	public void setCellWidth(int iWidth) {
		this.setWidth(iWidth);
	}
	
	public void setQuantity(String sQty, boolean bDeliveryMode) {
		m_oLabelQty.setValue(sQty);
		if(m_bIsOldItem)
			m_oLabelQty.setBackgroundColor(m_oFrameItemDisplayInformationList.getBackgroundColor());
		if(!bDeliveryMode){
			if(sQty.length() >= 3  && sQty.length() <= 5 ) {
				m_oLabelQty.setWidth( m_iLabelWidth + sQty.length() * 5);
				m_oLabelQty.setLeft(m_iLabelLeft - sQty.length() * 2);
			}
		}
	}
	
	public void setNewQuantity(String sQty, boolean bDeliveryMode) {
		m_oLabelQty.setValue(sQty);
		if(bDeliveryMode != true){
			int iLen = sQty.length();
			if (iLen > 5)
				iLen = 5;
			if(iLen >= 3) {
				m_oLabelQty.setWidth( m_iLabelWidth + sQty.length()*5);
				m_oLabelQty.setLeft(m_iLabelLeft - sQty.length()*4);
				m_oLabelMinusQty.setLeft(m_iLabelMinusQtyLeft - sQty.length()*3);
				m_oLabelQtyBg.setWidth(m_iLabelQtyBgWidth + sQty.length()*4 );
				m_oLabelQtyBg.setLeft(m_iLabelQtyBgLeft - sQty.length()*3 );
			}else {
				m_oLabelQty.setWidth( m_iLabelWidth);
				m_oLabelQty.setLeft(m_iLabelLeft);
				m_oLabelMinusQty.setLeft(m_iLabelMinusQtyLeft);
				m_oLabelQtyBg.setWidth(m_iLabelQtyBgWidth);
				m_oLabelQtyBg.setLeft(m_iLabelQtyBgLeft);
			}
		}
	}
	
	public void setNewReceiveQuantity(String sQty) {
		m_oLabelReceiveQty.setValue(sQty);
	}
	
	public String getQuantity() {
		return m_oLabelQty.getValue();
	}

	public void setDescription(String[] sDesc) {
		m_oLabelDesc.setValue(sDesc);
	}
	
	public String getDescription() {
		return m_oLabelDesc.getValue();
	}
	
	public void setInformation(String[] sStatus, String[] sDiscount, ArrayList<FuncCheckItem> sModifiers, String[] sPantryMessage, String[] sDisplayInformation, ArrayList<PosTaxScType> sScTaxAddedInformation, ArrayList<PosTaxScType> sScTaxWaivedInformation, int iBasketHeight) {
		VirtualUILabel oLabelModifier;
		String[] sModifier;

		int iScTaxAddedCount = sScTaxAddedInformation.size();
		int iScTaxWaivedCount = sScTaxWaivedInformation.size();
		String[] sScTaxAddedList = StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, "");
		String[] sScTaxWaivedList = StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, "");
		if (AppGlobal.g_oFuncStation.get().isOrderingBasketShowAddWaiveScTaxInfo()) {
			for(int i = 0 ; i < AppGlobal.LANGUAGE_COUNT ; i++) {
				StringBuilder sScTaxAddedInfo = new StringBuilder();
				StringBuilder sScTaxWaivedInfo = new StringBuilder();
				
				for (PosTaxScType oPosTaxScType : sScTaxAddedInformation) {
					sScTaxAddedInfo.append(System.lineSeparator());
					sScTaxAddedInfo.append(oPosTaxScType.getName(i + 1));
				}
				
				for (PosTaxScType oPosTaxScType : sScTaxWaivedInformation) {
					sScTaxWaivedInfo.append(System.lineSeparator());
					sScTaxWaivedInfo.append(oPosTaxScType.getName(i + 1));
				}
				
				sScTaxAddedList[i] = sScTaxAddedInfo.toString();
				sScTaxWaivedList[i] = sScTaxWaivedInfo.toString();
			}
		}
		
		m_oLabelAddTaxSc.setVisible(false);
		m_oLabelWaiveTaxSc.setVisible(false);
		m_oLabelDiscount.setVisible(false);
		m_oLabelInfo.setVisible(false);
		m_oLabelPantryMessage.setVisible(false);
				
		if(m_oFrameModifierList.getChildCount() > 0){
			m_oFrameModifierList.removeAllChildren();
			m_oFrameModifierList.setHeight(0);
		}
		
		if(sDisplayInformation[0].length() > 0){
			m_oFrameItemDisplayInformationList.removeAllChildren();
			m_oFrameItemDisplayInformationList.setHeight(0);
		}
		
		int iModifiersTop = 0;
		
		int iLabelFontSize = 15;
		if (m_oPosDisplayPanelZone != null && m_oPosDisplayPanelZone.getBasketItemDescriptionFontSize() - 2 > 0)
			iLabelFontSize = m_oPosDisplayPanelZone.getBasketItemDescriptionFontSize() - 2;
		
		if(sModifiers.size() > 0){
			for(FuncCheckItem oModifier : sModifiers){
				oLabelModifier = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(oLabelModifier, "lblModifier");
				sModifier = oModifier.getItemShortDescription();
				oLabelModifier.setValue(StringLib.appendStringArray(" - ", sModifier));
				oLabelModifier.setTop(iModifiersTop);
				oLabelModifier.setTextSize(iLabelFontSize);
				oLabelModifier.setForegroundColor(m_oLabelDiscount.getForegroundColor());
				oLabelModifier.setClickServerRequestBlockUI(true);
				oLabelModifier.setLongClickServerRequestBlockUI(true);
				oLabelModifier.setVisible(true);
				
				iModifiersTop += oLabelModifier.getHeight();
				m_oFrameModifierList.setHeight(m_oFrameModifierList.getHeight()+oLabelModifier.getHeight());
				m_oFrameModifierList.attachChild(oLabelModifier);
			}
			
			m_oFrameModifierList.setVisible(true);
			iModifiersTop -= 10;
			this.setHeight(m_oFrameModifierList.getTop() + m_oFrameModifierList.getHeight());
			
			if(sScTaxAddedList[0].length() > 0) {
				m_oLabelAddTaxSc.setValue(StringLib.appendStringArray(AppGlobal.g_oLang.get()._("added") + ": ", sScTaxAddedList));
				m_oLabelAddTaxSc.setTop(iModifiersTop + m_oLabelDesc.getHeight());
				m_oLabelAddTaxSc.setHeight(20 * (iScTaxAddedCount + 1));
				m_oLabelAddTaxSc.setVisible(true);
				iModifiersTop += m_oLabelAddTaxSc.getHeight() + 2;
				this.setHeight(m_oLabelAddTaxSc.getTop() + m_oLabelAddTaxSc.getHeight());
			}
			
			if(sScTaxWaivedList[0].length() > 0) {
				m_oLabelWaiveTaxSc.setValue(StringLib.appendStringArray(AppGlobal.g_oLang.get()._("waived") + ": ", sScTaxWaivedList));
				m_oLabelWaiveTaxSc.setTop(iModifiersTop + m_oLabelDesc.getHeight());
				m_oLabelWaiveTaxSc.setHeight(20 * (iScTaxWaivedCount + 1));
				m_oLabelWaiveTaxSc.setVisible(true);
				iModifiersTop += m_oLabelWaiveTaxSc.getHeight() + 2;
				this.setHeight(m_oLabelWaiveTaxSc.getTop() + m_oLabelWaiveTaxSc.getHeight());
			}
			
			if(sDiscount[0].length() > 0){
				m_oLabelDiscount.setValue(sDiscount);
				m_oLabelDiscount.setTop(iModifiersTop +m_oLabelDesc.getHeight());
				m_oLabelDiscount.setVisible(true);
				iModifiersTop += m_oLabelDiscount.getHeight();
				this.setHeight(m_oLabelDiscount.getTop() + m_oLabelDiscount.getHeight());
			}
			
			if(sStatus[0].length() > 0){
				m_oLabelInfo.setValue(StringLib.appendStringArray("-", sStatus));
				m_oLabelInfo.setTop(iModifiersTop + m_oLabelDesc.getHeight());
				m_oLabelInfo.setVisible(true);
				iModifiersTop += m_oLabelInfo.getHeight();
				this.setHeight(m_oLabelInfo.getTop()+m_oLabelInfo.getHeight());
			}
			
			if (sPantryMessage != null && !sPantryMessage[0].isEmpty()) {
				m_oLabelPantryMessage.setValue(StringLib.appendStringArray("[", sPantryMessage, "]"));
				m_oLabelPantryMessage.setTop(iModifiersTop+m_oLabelDesc.getHeight());
				m_oLabelPantryMessage.setVisible(true);
				iModifiersTop += m_oLabelPantryMessage.getHeight();
				this.setHeight(m_oLabelPantryMessage.getTop()+m_oLabelPantryMessage.getHeight());
			}
			
			if (sDisplayInformation != null && sDisplayInformation[0].length() > 0) {
				int iAttributeTop = 0;
				int iTmpTop = iModifiersTop;
				oLabelModifier = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(oLabelModifier, "lblModifier");
				oLabelModifier.setValue(StringLib.appendStringArray("(", sDisplayInformation,")"));
				oLabelModifier.setTop(iAttributeTop);
				oLabelModifier.setTextSize(iLabelFontSize);
				oLabelModifier.setForegroundColor(m_oLabelDiscount.getForegroundColor());
				oLabelModifier.setClickServerRequestBlockUI(true);
				oLabelModifier.setLongClickServerRequestBlockUI(true);
				oLabelModifier.setVisible(true);
				
				iAttributeTop += oLabelModifier.getHeight();
				iModifiersTop += oLabelModifier.getHeight();
				m_oFrameItemDisplayInformationList.setHeight(m_oFrameItemDisplayInformationList.getHeight()+oLabelModifier.getHeight());
				m_oFrameItemDisplayInformationList.attachChild(oLabelModifier);
				m_oFrameItemDisplayInformationList.setTop(m_oLabelDesc.getHeight()+iTmpTop);
				m_oFrameItemDisplayInformationList.setVisible(true);
				this.setHeight(m_oFrameItemDisplayInformationList.getTop()+m_oFrameItemDisplayInformationList.getHeight());
			} else {
				m_oFrameItemDisplayInformationList.setHeight(0);
				m_oFrameItemDisplayInformationList.setVisible(false);
			}
		}else{
			iModifiersTop = m_oLabelDesc.getHeight() - 10;
			m_oFrameModifierList.setHeight(0);
			m_oFrameModifierList.setVisible(false);
			
			if(sScTaxAddedList[0].length() > 0) {
				m_oLabelAddTaxSc.setValue(StringLib.appendStringArray(AppGlobal.g_oLang.get()._("added")+": ", sScTaxAddedList));
				m_oLabelAddTaxSc.setTop(iModifiersTop);
				m_oLabelAddTaxSc.setHeight(20 * (iScTaxAddedCount + 1));
				m_oLabelAddTaxSc.setVisible(true);
				iModifiersTop += m_oLabelAddTaxSc.getHeight();
				this.setHeight(m_oLabelAddTaxSc.getTop() + m_oLabelAddTaxSc.getHeight());
			}else
				this.setHeight(m_oLabelDesc.getHeight());
			
			if(sScTaxWaivedList[0].length() > 0) {
				m_oLabelWaiveTaxSc.setValue(StringLib.appendStringArray(AppGlobal.g_oLang.get()._("waived") + ": ", sScTaxWaivedList));
				m_oLabelWaiveTaxSc.setTop(iModifiersTop);
				m_oLabelWaiveTaxSc.setHeight(20 * (iScTaxWaivedCount + 1));
				m_oLabelWaiveTaxSc.setVisible(true);
				iModifiersTop += m_oLabelWaiveTaxSc.getHeight();
				this.setHeight(m_oLabelWaiveTaxSc.getTop() + m_oLabelWaiveTaxSc.getHeight());
			}
			
			if(sDiscount[0].length() > 0){
				m_oLabelDiscount.setValue(sDiscount);
				m_oLabelDiscount.setTop(iModifiersTop);
				m_oLabelDiscount.setVisible(true);
				iModifiersTop += m_oLabelDiscount.getHeight();
				this.setHeight(m_oLabelDiscount.getTop()+m_oLabelDiscount.getHeight());
			}
			
			if(sStatus[0].length() > 0){
				m_oLabelInfo.setValue(StringLib.appendStringArray("-", sStatus));
				m_oLabelInfo.setTop(iModifiersTop);
				m_oLabelInfo.setVisible(true);
				iModifiersTop += m_oLabelInfo.getHeight();
				this.setHeight(m_oLabelInfo.getTop()+m_oLabelInfo.getHeight());
			}
			
			if (sPantryMessage != null && !sPantryMessage[0].isEmpty()) {
				m_oLabelPantryMessage.setValue(StringLib.appendStringArray("[", sPantryMessage, "]"));
				m_oLabelPantryMessage.setTop(iModifiersTop);
				m_oLabelPantryMessage.setVisible(true);
				this.setHeight(m_oLabelPantryMessage.getTop()+m_oLabelPantryMessage.getHeight());
			}
			if(sDisplayInformation[0].length() > 0) {
				int iDisplayInformationTop = 0;
				m_oFrameItemDisplayInformationList.setTop(iModifiersTop);
				m_oFrameItemDisplayInformationList.setVisible(true);
				oLabelModifier = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(oLabelModifier, "lblModifier");
				oLabelModifier.setValue(StringLib.appendStringArray("(", sDisplayInformation, ")"));
				oLabelModifier.setTop(iDisplayInformationTop);
				oLabelModifier.setTextSize(iLabelFontSize);
				oLabelModifier.setForegroundColor(m_oLabelDiscount.getForegroundColor());
				oLabelModifier.setClickServerRequestBlockUI(true);
				oLabelModifier.setLongClickServerRequestBlockUI(true);
				
				iDisplayInformationTop += oLabelModifier.getHeight();
				iModifiersTop += oLabelModifier.getHeight();
				m_oFrameItemDisplayInformationList.setHeight(m_oFrameItemDisplayInformationList.getHeight()+oLabelModifier.getHeight());
				m_oFrameItemDisplayInformationList.attachChild(oLabelModifier);
				m_oFrameItemDisplayInformationList.setVisible(true);
				this.setHeight(m_oFrameItemDisplayInformationList.getTop() + m_oFrameItemDisplayInformationList.getHeight());
			} else {
				m_oFrameItemDisplayInformationList.setHeight(0);
				m_oFrameItemDisplayInformationList.setVisible(false);
			}
		}
		
		if (m_oPosDisplayPanelZone != null) {
			int iNumberOfBasketItem = m_oPosDisplayPanelZone.getNumberOfBasketItem();
			if (iNumberOfBasketItem != 0) {
				if (this.getHeight() <= iBasketHeight / iNumberOfBasketItem)
					this.setHeight((iBasketHeight / iNumberOfBasketItem) - iNumberOfBasketItem);
			}
		}
		
	}
	
	public String getInformation(){
		return m_oLabelInfo.getValue();
	}
	
	public void setPrice(String sPrice) {
		m_oLabelPrice.setValue(sPrice);
	}
	
	public String getPrice() {
		return m_oLabelPrice.getValue();
	}
	
	public boolean isOldItem(){
		return m_bIsOldItem;
	}
	
	public int getChildCellCount(){
		int iChildCount = 0;
		
		iChildCount += m_oModifierCellList.size();
		iChildCount += m_oChildItemCellList.size();
		
		return iChildCount;
	}
	
	public int getChildModifierCellCount(){
		int iChildCount = 0;
		
		iChildCount += m_oModifierCellList.size();
		
		return iChildCount;
	}
	
	private void showFunctionButton(boolean bShow){
		if(bShow){
			//Show Delete button and hide Price label
			m_oButtonDelete.setVisible(true);
			m_oLabelPrice.setVisible(false);
		}else{
			//Hide Delete button and show Price label
			m_oButtonDelete.setVisible(false);
			m_oLabelPrice.setVisible(true);
		}
		
		m_bIsFunctionButtonShow = bShow;
	}
	
	// iSelectType	0: no restriction
	//				1: Select new item only
	//				2: Select old item only
	// iShowType	0 - Not show
	//				1 - Show selected
	//				2 - Show deselected
	public void setSelected(int iSelectType, int iShowType){
		if (iShowType != FrameOrderingBasket.SHOW_TYPE_HIDDEN) {
			if(iShowType == FrameOrderingBasket.SHOW_TYPE_UNSELECTWITHQUANTITY) {
				m_bSelectMode = true;
				m_oLabelAddQty.setVisible(false);
				m_oLabelMinusQty.setVisible(false);
				m_oLabelQty.setVisible(false);
				m_oLabelTempQty.setVisible(true);
				m_oLabelTempQty.setValue(m_oLabelQty.getValue());
				m_oLabelQtyBg.setVisible(false);
			}
			else if (iShowType == FrameOrderingBasket.SHOW_TYPE_SELECTWITHADDMINUS) {
				m_bSelectMode = true;
				m_oLabelAddQty.setVisible(true);
				m_oLabelMinusQty.setVisible(true);
				m_oLabelQty.setVisible(false);
				m_oLabelTempQty.setVisible(true);
				m_oLabelQtyBg.setVisible(true);
				m_oLabelQty.setVisible(false);
			}
			else {
				m_bSelectMode = true;
				m_oLabelQty.setVisible(false);
				m_oLabelAddQty.setVisible(false);
				m_oLabelMinusQty.setVisible(false);
				m_oLabelQtyBg.setVisible(false);
				m_oLabelTempQty.setVisible(false);
			}
			
			if (iSelectType == FrameOrderingBasket.SELECT_TYPE_NEW_ITEM && m_bIsOldItem) {
				this.setVisible(false);
				return;
			}
			if (iSelectType == FrameOrderingBasket.SELECT_TYPE_OLD_ITEM && m_bIsOldItem == false) {
				this.setVisible(false);
				return;
			}
		} else {
			this.setVisible(true);
			
			m_bSelectMode = false;
			
			m_oLabelQty.setVisible(true);
			m_oLabelTempQty.setVisible(false);
			m_oLabelQtyBg.setVisible(true);
			if(m_bIsOldItem == false){
				m_oLabelAddQty.setVisible(true);
				m_oLabelMinusQty.setVisible(true);
			} else {
				// force to set the visibility of add, minus, quantity bg of old item to false.
				m_oLabelAddQty.setVisible(false);
				m_oLabelMinusQty.setVisible(false);
				m_oLabelQtyBg.setVisible(false);
			}
		}
		
		switch (iShowType) {
			case FrameOrderingBasket.SHOW_TYPE_SELECT:
				m_oImageSelected.setVisible(true);
				m_oImageDeselected.setVisible(false);
				adjustItemDescInfoLeftPosition(m_iDescriptionLeftInSelection);
				break;
			case FrameOrderingBasket.SHOW_TYPE_UNSELECT:
				m_oImageSelected.setVisible(false);
				m_oImageDeselected.setVisible(true);
				adjustItemDescInfoLeftPosition(m_iDescriptionLeftInSelection);
				break;
			case FrameOrderingBasket.SHOW_TYPE_SELECTWITHADDMINUS:
				m_oImageSelected.setVisible(true);
				m_oImageDeselected.setVisible(false);
				adjustItemDescInfoLeftPosition(m_iDescriptionLeftInSelection);
				break;
			case FrameOrderingBasket.SHOW_TYPE_UNSELECTWITHQUANTITY:
				m_oImageSelected.setVisible(false);
				m_oImageDeselected.setVisible(true);
				adjustItemDescInfoLeftPosition(m_iDescriptionLeftInSelection);
				break;
			default:
				m_oImageSelected.setVisible(false);
				m_oImageDeselected.setVisible(false);
				adjustItemDescInfoLeftPosition(m_iDescriptionLeft);
				break;
		}
		
		showFunctionButton(false);
	}
	
	// adjust the description label left position
	public void adjustItemDescInfoLeftPosition(int iLeft) {
		m_oLabelDiscount.setLeft(iLeft);
		m_oFrameModifierList.setLeft(iLeft);
		m_oLabelAddTaxSc.setLeft(iLeft);
		m_oLabelWaiveTaxSc.setLeft(iLeft);
		m_oLabelDesc.setLeft(iLeft);
		m_oLabelInfo.setLeft(iLeft);
		m_oLabelPantryMessage.setLeft(iLeft);
		m_oFrameItemDisplayInformationList.setLeft(iLeft);
	}
	
	public void setUnderline(boolean bSet){
		if(m_oFrameUnderline != null)
			m_oFrameUnderline.setVisible(bSet);
	}
	
	public void setUnderlineTop(int iTop){
		if(m_oFrameUnderline != null)
			m_oFrameUnderline.setTop(iTop);
	}
	
	public void setItemSeq(int iItemSeq) {
		if(m_oLabelSeq != null)
			m_oLabelSeq.setValue(String.valueOf(iItemSeq));
	}
	
	public void setAddMinusVisible(boolean bVisible) {
		m_oLabelAddQty.setVisible(bVisible);
		m_oLabelMinusQty.setVisible(bVisible);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if(this.isChildId(iChildId)){
			if(m_bIsFunctionButtonShow){
				this.showFunctionButton(false);
				if(iChildId != m_oButtonDelete.getId()){
					bMatchChild = true;
					return bMatchChild;
				}
			}
		}
		
		if (iChildId == m_oLabelQty.getId()) {
			// Not under select mode
			if(!m_bSelectMode){
				for (FrameOrderingBasketCellListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_QtyClicked(this.getId(), sNote);
				}
			}
			bMatchChild = true;
		} else if (iChildId == m_oLabelTempQty.getId()) {
			for (FrameOrderingBasketCellListener listener : listeners) {
				// Raise the event to parent
				m_oLabelTempQty.setValue(listener.frameOrderingBasketCell_TempQtyClicked(this.getId(), sNote, m_oLabelTempQty.getValue()));
			}
			bMatchChild = true;
		} else if (iChildId == m_oLabelReceiveQty.getId()) {
			// Not under select mode
			if(!m_bSelectMode){
				for (FrameOrderingBasketCellListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_ReceiveQtyClicked(this.getId(), sNote);
				}
			}
			bMatchChild = true;
		}
		else if (iChildId == m_oLabelDesc.getId() || iChildId == m_oLabelInfo.getId() || iChildId == m_oLabelDiscount.getId() || iChildId == m_oFrameModifierList.getId() || iChildId == m_oLabelPantryMessage.getId() || iChildId == m_oLabelAddTaxSc.getId() || iChildId == m_oLabelWaiveTaxSc.getId()) {
			for (FrameOrderingBasketCellListener listener : listeners) {
				// Raise the event to parent
				listener.frameOrderingBasketCell_DescClicked(this.getId(), sNote);
			}
			bMatchChild = true;
		}
		else if (iChildId == m_oLabelPrice.getId()) {
			// Not under select mode
			if(!m_bSelectMode && !this.m_bIsConsolidateItemsBasket)
				this.showFunctionButton(true);
			bMatchChild = true;
		}
		else if(iChildId == m_oButtonDelete.getId()) {
			// Not under select mode
			if(!m_bSelectMode){
				for(FrameOrderingBasketCellListener listener : listeners) {
					//Raise the event to Section
					listener.frameOrderingBasketCell_DeleteClicked(this.getId(), sNote);
				}
			}
			bMatchChild = true;
		}
		else if (iChildId == m_oLabelAddQty.getId()) {
			// Not under select mode
			if (!m_bSelectMode) {
				for (FrameOrderingBasketCellListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_AddQtyClicked(this.getId(), sNote);
				}
			} else {
				boolean bResult = false;
				for (FrameOrderingBasketCellListener listener : listeners) {
					// Raise the event to parent
					bResult = listener.frameOrderingBasketCell_AddQtyClickedForOldItem(this.getId(), sNote, new BigDecimal(m_oLabelTempQty.getValue()).add(BigDecimal.ONE));
					if (bResult) 
						m_oLabelTempQty.setValue(new BigDecimal(m_oLabelTempQty.getValue()).add(BigDecimal.ONE).toString());
					else
						m_oLabelTempQty.setValue(new BigDecimal(m_oLabelTempQty.getValue()).toString());
				}
			}
			bMatchChild = true;
		} else if (iChildId == m_oLabelMinusQty.getId()) {
			// Not under select mode
			if(!m_bSelectMode){
				for (FrameOrderingBasketCellListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_MinusQtyClicked(this.getId(), sNote);
				}
			} else {
				boolean bResult = false;
				for (FrameOrderingBasketCellListener listener : listeners) {
					// Raise the event to parent
					bResult = listener.frameOrderingBasketCell_MinusQtyClickedForOldItem(this.getId(), sNote, new BigDecimal(m_oLabelTempQty.getValue()).subtract(BigDecimal.ONE));
					if (bResult) 
						m_oLabelTempQty.setValue(new BigDecimal(m_oLabelTempQty.getValue()).subtract(BigDecimal.ONE).toString());
					else
						m_oLabelTempQty.setValue(new BigDecimal(m_oLabelTempQty.getValue()).toString());
				}
			}
			bMatchChild = true;
		}
		else if (iChildId == m_oImageSelected.getId() || iChildId == m_oImageDeselected.getId()) {
			// Under select mode
			if(m_bSelectMode){
				for (FrameOrderingBasketCellListener listener : listeners) {
					// Raise the event to parent
					// Use the same event as click desc
					listener.frameOrderingBasketCell_DescClicked(this.getId(), sNote);
				}
			}
			bMatchChild = true;
		}
		return bMatchChild;
	}

	@Override
	public boolean longClicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		//if (iChildId == m_oLabelDesc.getId() || iChildId == m_oLabelInfo.getId()) {
		if (iChildId == m_oLabelDesc.getId() || iChildId == m_oLabelInfo.getId() || iChildId == m_oLabelPantryMessage.getId()) {
			for (FrameOrderingBasketCellListener listener : listeners) {
				// Raise the event to parent
				listener.frameOrderingBasketCell_DescLongClicked(this.getId(), sNote);
			}
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
	
	@Override
	public void frameOrderingBasketModifierCell_QtyClicked(int iCellId, String sNote) {
		for (FrameOrderingBasketModifierCell oFrameOrderingBasketModifierCell : m_oModifierCellList) {
			if (oFrameOrderingBasketModifierCell.getId() == iCellId) {
				for (FrameOrderingBasketCellListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketModifierCell_QtyClicked(this.getId(), m_oModifierCellList.indexOf(oFrameOrderingBasketModifierCell), sNote);
				}
				break;
			}
		}
	}

	@Override
	public void frameOrderingBasketModifierCell_DescClicked(int iCellId, String sNote) {
		for(FrameOrderingBasketModifierCell oFrameOrderingBasketModifierCell:m_oModifierCellList){
			if(oFrameOrderingBasketModifierCell.getId() == iCellId){
				for (FrameOrderingBasketCellListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketModifierCell_DescClicked(this.getId(), m_oModifierCellList.indexOf(oFrameOrderingBasketModifierCell), sNote);
				}
				break;
			}
		}
	}

	@Override
	public void frameOrderingBasketModifierCell_PriceClicked(int iCellId, String sNote) {
		for(FrameOrderingBasketModifierCell oFrameOrderingBasketModifierCell:m_oModifierCellList){
			if(oFrameOrderingBasketModifierCell.getId() == iCellId){
				for (FrameOrderingBasketCellListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketModifierCell_PriceClicked(this.getId(), m_oModifierCellList.indexOf(oFrameOrderingBasketModifierCell), sNote);
				}
				break;
			}
		}
	}

	@Override
	public void frameOrderingBasketChildItemCell_QtyClicked(int iCellId, String sNote) {
		for(FrameOrderingBasketChildItemCell oFrameOrderingBasketChildItemCell:m_oChildItemCellList){
			if(oFrameOrderingBasketChildItemCell.getId() == iCellId){
				for (FrameOrderingBasketCellListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketChildItemCell_QtyClicked(this.getId(), m_oChildItemCellList.indexOf(oFrameOrderingBasketChildItemCell), sNote);
				}
				break;
			}
		}
	}

	@Override
	public void frameOrderingBasketChildItemCell_DescClicked(int iCellId, String sNote) {
		for(FrameOrderingBasketChildItemCell oFrameOrderingBasketChildItemCell:m_oChildItemCellList){
			if(oFrameOrderingBasketChildItemCell.getId() == iCellId){
				for (FrameOrderingBasketCellListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketChildItemCell_DescClicked(this.getId(), m_oChildItemCellList.indexOf(oFrameOrderingBasketChildItemCell), sNote);
				}
				break;
			}
		}
	}

	@Override
	public void frameOrderingBasketChildItemCell_PriceClicked(int iCellId, String sNote) {
		for(FrameOrderingBasketChildItemCell oFrameOrderingBasketChildItemCell:m_oChildItemCellList){
			if(oFrameOrderingBasketChildItemCell.getId() == iCellId){
				for (FrameOrderingBasketCellListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketChildItemCell_PriceClicked(this.getId(), m_oChildItemCellList.indexOf(oFrameOrderingBasketChildItemCell), sNote);
				}
				break;
			}
		}
	}
	
}
