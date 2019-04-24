package app;

import java.util.ArrayList;

import om.PosDisplayPanelZone;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameBasketExtendBarListener {
    void frameBasketExtendBar_ExtendBarClicked();
    void frameBasketExtendBar_DineInTakeoutClicked();
}

public class FrameBasketExtendBar extends VirtualUIFrame {
	TemplateBuilder m_oTemplateBuilder;
	boolean m_bBasketExtended;
	
	private VirtualUIFrame m_oFrameCheckInfo;
	private VirtualUIImage m_oImageBasketExtendBar;
	private VirtualUIFrame m_oFrameDivider;
	private VirtualUILabel m_oLabelCheckTotal;
	private VirtualUIImage m_oImageDineInTakeout;
//KingsleyKwan20170918		-----Start-----
	private VirtualUIFrame m_oFrameDineInTakeout;
	private VirtualUILabel m_oLabelDineInTakeout;
	private VirtualUIFrame m_oFrameDineInTakeoutAndRevenue;
	private VirtualUIFrame m_oFrameRevenueTotal;
	private VirtualUIFrame m_oFrameRevenue;
	private VirtualUILabel m_oLabelRevenue;
	private VirtualUIFrame m_oFrameRevenueButton;
	private VirtualUIImage m_oImageRevenueOn;
	private VirtualUIImage m_oImageRevenueOff;
//KingsleyKwan20170918		----- End -----
	private VirtualUIImage m_oImageRevenue;
	
	private VirtualUIFrame m_oFrameExtraInfo;
	private VirtualUIImage m_oImageItemQuantity;
	private VirtualUILabel m_oLabelItemQuantity;
	private VirtualUILabel m_oLabelItemLineCount;
	private VirtualUIImage m_oImageItemTotal;
	private VirtualUILabel m_oLabelItemTotal;
	private VirtualUIImage m_oImageItemDiscount;
	private VirtualUILabel m_oLabelItemDiscount;
//KingsleyKwan20170918		-----Start-----
	private VirtualUIImage m_oImageScTotal;
	private VirtualUILabel m_oLabelScTotal;
	private VirtualUIFrame m_oFrameScTaxUnderline;
	private VirtualUIImage m_oImageTaxTotal;
	private VirtualUILabel m_oLabelTaxTotal;
	private VirtualUIFrame m_oFrameExtraInfoUnderline;
	private String m_sButtonOffColor;
	private String m_sButtonOnColor;
//KingsleyKwan20170918		----- End -----
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameBasketExtendBarListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameBasketExtendBarListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameBasketExtendBarListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameBasketExtendBar(PosDisplayPanelZone oPosDisplayPanelZone) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameBasketExtendBarListener>();
		m_bBasketExtended = false;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraBasketExtendBar.xml");

		m_oFrameCheckInfo = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCheckInfo, "fraCheckInfo");
		
		m_oImageBasketExtendBar = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageBasketExtendBar, "ImgBasketExtendBar");
		m_oImageBasketExtendBar.allowClick(true);
		m_oImageBasketExtendBar.setClickServerRequestBlockUI(false);
		m_oFrameCheckInfo.attachChild(m_oImageBasketExtendBar);
		
		m_oFrameDivider = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameDivider, "fraDivider");
		m_oFrameCheckInfo.attachChild(m_oFrameDivider);
		
//KingsleyKwan20170918		-----Start-----
		// Dine-in Or Take-out
		m_oFrameDineInTakeout = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameDineInTakeout, "fraDineInTakeout");
		m_oFrameDineInTakeout.allowClick(true);
		m_oFrameCheckInfo.attachChild(m_oFrameDineInTakeout);
		
		m_oImageDineInTakeout = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageDineInTakeout, "ImgDineInTakeout");
//KingsleyKwan20171109		-----Start-----
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oImageDineInTakeout.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/dine_in_fine_dining.png");
		else
			m_oImageDineInTakeout.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_nav_arrowdown.png");
//KingsleyKwan20171109		----- End -----		m_oImageDineInTakeout.allowClick(true);
		m_oImageDineInTakeout.setClickServerRequestBlockUI(false);
		m_oFrameDineInTakeout.attachChild(m_oImageDineInTakeout);
		
		m_oLabelDineInTakeout = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDineInTakeout, "lblDineInTakeout");
		m_oLabelDineInTakeout.setValue(AppGlobal.g_oLang.get()._("dine_in", ""));
		m_oFrameDineInTakeout.attachChild(m_oLabelDineInTakeout);
		
		m_oFrameDineInTakeoutAndRevenue = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameDineInTakeoutAndRevenue, "fraDineInTakeoutAndRevenue");
		m_oFrameDineInTakeout.attachChild(m_oFrameDineInTakeoutAndRevenue);
		
		// The Revenue frame
		m_oFrameRevenue = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameRevenue, "fraRevenue");
		m_oFrameCheckInfo.attachChild(m_oFrameRevenue);
		
		m_oLabelRevenue = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelRevenue, "lblRevenue");
		m_oLabelRevenue.setValue(AppGlobal.g_oLang.get()._("revenue", ""));
		m_oFrameRevenue.attachChild(m_oLabelRevenue);
		
//RayHuen20171025 Change Image -------Start--------		
		// Revenue Button
		m_oImageRevenue = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageRevenue, "ImgRevenueBn");
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oImageRevenue.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/check_listing_revenue.png");
		else
			m_oImageRevenue.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/toggle_on.png");	
		m_oFrameRevenue.attachChild(m_oImageRevenue);
//RayHuen20171025 Change Image -------End--------
		
		// Item Qty And Total Frame
		m_oFrameRevenueTotal = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameRevenueTotal, "fraItemQtyItemTotal");
		m_oFrameCheckInfo.attachChild(m_oFrameRevenueTotal);
		
		m_oImageItemQuantity = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageItemQuantity, "ImgItemQuantity");
//KingsleyKwan20171109		-----Start-----
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oImageItemQuantity.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/item_quantity.png");
		else
			m_oImageItemQuantity.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_orders_cart.png");
//KignsleyKwan20171109		----- End -----
		m_oFrameRevenueTotal.attachChild(m_oImageItemQuantity);
		
		m_oLabelItemQuantity = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelItemQuantity, "lblItemQuantity");
		m_oFrameRevenueTotal.attachChild(m_oLabelItemQuantity);

		m_oLabelItemLineCount = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelItemLineCount, "lblItemLineCount");
		m_oFrameRevenueTotal.attachChild(m_oLabelItemLineCount);
		
		m_oLabelCheckTotal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckTotal, "lblCheckTotal");
		if (oPosDisplayPanelZone != null && oPosDisplayPanelZone.getBasketCheckTotalFontSize() > 0)
			m_oLabelCheckTotal.setTextSize(oPosDisplayPanelZone.getBasketCheckTotalFontSize());
		m_oFrameRevenueTotal.attachChild(m_oLabelCheckTotal);
		
		m_oFrameCheckInfo.attachChild(m_oFrameRevenueTotal);
		
		this.attachChild(m_oFrameCheckInfo);
//KingsleyKwan20170918		----- End -----
		m_oFrameExtraInfo = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameExtraInfo, "fraExtraInfo");
		
		m_oImageItemTotal = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageItemTotal, "ImgItemTotal");
//KingsleyKwan20170918ItemTotalIcon		-----Start-----
		m_oImageItemTotal.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_orders_total.png");
//KingsleyKwan20170918ItemTotalIcon		----- End -----
		m_oFrameExtraInfo.attachChild(m_oImageItemTotal);
		
		int iLabelFontSize = 0;
		if (oPosDisplayPanelZone != null && oPosDisplayPanelZone.getBasketExtensionFontSize() > 0)
			iLabelFontSize = oPosDisplayPanelZone.getBasketExtensionFontSize();
		
		m_oLabelItemTotal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelItemTotal, "lblItemTotal");
		if (iLabelFontSize > 0)
			m_oLabelItemTotal.setTextSize(iLabelFontSize);
		m_oFrameExtraInfo.attachChild(m_oLabelItemTotal);
//KingsleyKwan20170918ItemDiscountAndTSIcon		-----Start-----
		m_oImageItemDiscount = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageItemDiscount, "ImgItemDiscount");
		m_oImageItemDiscount.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_orders_discount.png");
		m_oFrameExtraInfo.attachChild(m_oImageItemDiscount);
		
		m_oLabelItemDiscount = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelItemDiscount, "lblItemDiscount");
		if (iLabelFontSize > 0)
			m_oLabelItemDiscount.setTextSize(iLabelFontSize);
		m_oFrameExtraInfo.attachChild(m_oLabelItemDiscount);
		
		// Service Charge
		m_oImageScTotal = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageScTotal, "ImgScTotal");
		m_oImageScTotal.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_orders_sc.png");
		m_oFrameExtraInfo.attachChild(m_oImageScTotal);
		
		m_oLabelScTotal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelScTotal, "lblScTotal");
		if (iLabelFontSize > 0)
			m_oLabelScTotal.setTextSize(iLabelFontSize);
		m_oFrameExtraInfo.attachChild(m_oLabelScTotal);
		
		m_oFrameScTaxUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameScTaxUnderline, "fraScTaxUnderline");
		m_oFrameExtraInfo.attachChild(m_oFrameScTaxUnderline);
		
		// Service Charge
		m_oImageTaxTotal = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageTaxTotal, "ImgTaxTotal");
		m_oImageTaxTotal.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_orders_tax.png");
		m_oFrameExtraInfo.attachChild(m_oImageTaxTotal);
		
		m_oLabelTaxTotal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTaxTotal, "lblTaxTotal");
		if (iLabelFontSize > 0)
			m_oLabelTaxTotal.setTextSize(iLabelFontSize);
		m_oFrameExtraInfo.attachChild(m_oLabelTaxTotal);
		
		m_oFrameExtraInfoUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameExtraInfoUnderline, "fraExtraInfoUnderline");
		m_oFrameExtraInfo.attachChild(m_oFrameExtraInfoUnderline);
		
//KingsleyKwan20170918ItemDiscountAndTSIcon		----- End -----
		this.attachChild(m_oFrameExtraInfo);
    }
    
	public void setBasketExtendBarImage(String sImageUrl) {
		m_oImageBasketExtendBar.setSource(sImageUrl);
	}
	
	public String getDineInTakeoutImage() {
		return m_oImageDineInTakeout.getSource();
	}
	
	public void setDineInTakeoutImage(String sImageUrl) {
		m_oImageDineInTakeout.setSource(sImageUrl);
	}
	
	int getRevenueHeight(){
		if(	m_oFrameRevenueTotal != null)
			return 	m_oFrameRevenueTotal.getHeight();
		else
			return 0;
	}
	
//KingsleyKwan20170918SwitchTakeoutDinein		-----Start-----
	public void switchDineInTakeout(String sMode) {
		m_oLabelDineInTakeout.setValue(sMode);
		
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())){
			if(sMode.equals(AppGlobal.g_oLang.get()._("dine_in")))
				m_oImageDineInTakeout.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/dine_in_fine_dining.png");
			else if(sMode.equals(AppGlobal.g_oLang.get()._("fast_food")))
				m_oImageDineInTakeout.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/dine_in_fast_food_mode.png");
			else if(sMode.equals(AppGlobal.g_oLang.get()._("takeout")))
				m_oImageDineInTakeout.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/takeout.png");
		}
	}
	
	public void setRevenue(boolean bRevenue) {
		if (!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			if(bRevenue)
				m_oImageRevenue.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/toggle_on.png");
			else
				m_oImageRevenue.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/toggle_off.png");
		} else {
			if(bRevenue)
				m_oImageRevenue.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/check_listing_revenue.png");
			else
				m_oImageRevenue.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/check_listing_non_revenue.png");
		}
	}
	
//KingsleyKwan20170918SwitchTakeoutDinein		----- End -----
	public void setCheckTotal(String[] sCheckTotal) {
		m_oLabelCheckTotal.setValue(sCheckTotal);
	}
	
	public void setItemQuantity(String[] sQuantity) {
		m_oLabelItemQuantity.setValue(sQuantity);
	}

	public void setCheckInfoWidth(){
		m_oFrameCheckInfo.setWidth(this.getWidth());
		m_oFrameExtraInfo.setWidth(this.getWidth());
		m_oFrameRevenueTotal.setWidth(this.getWidth());
		m_oFrameDineInTakeout.setWidth((int)(this.getWidth() / 2));
		m_oFrameDineInTakeout.setLeft(0);
		
		m_oFrameDineInTakeoutAndRevenue.setLeft((int) (this.getWidth() / 2));
		
		m_oFrameRevenue.setWidth((int) (this.getWidth() / 2));
		m_oFrameRevenue.setLeft((int) (this.getWidth() / 2));

		double dImgDineInTakeOutLeftRatio = 0.43;
		m_oImageDineInTakeout.setLeft((int)(dImgDineInTakeOutLeftRatio * this.getWidth()));
		
		double dImgRevLeftRatio = 0.37;
		m_oImageRevenue.setLeft((int)(dImgRevLeftRatio * this.getWidth()));
		
		double dImgItemDisLeftRatio = 0.37;
		m_oImageItemDiscount.setLeft((int)(dImgItemDisLeftRatio * this.getWidth()));
		
		double dLabelItemDisLeftRatio = 0.42;
		m_oLabelItemDiscount.setLeft((int)(dLabelItemDisLeftRatio * this.getWidth()));
		
		double dImgScTotalLeftRatio = 0.71;
		m_oImageScTotal.setLeft((int)(dImgScTotalLeftRatio * this.getWidth()));
		
		double dLabelScTotalLeftRatio = 0.77;
		double dLabelScTotalWidthRatio = 0.197;
		m_oLabelScTotal.setLeft((int)(dLabelScTotalLeftRatio * this.getWidth()));
		m_oLabelScTotal.setWidth((int)(dLabelScTotalWidthRatio * this.getWidth()));
		
		m_oLabelItemTotal.setWidth((int)(dLabelScTotalWidthRatio * this.getWidth()));
		m_oLabelTaxTotal.setWidth((int)(dLabelScTotalWidthRatio * this.getWidth()));
		
		double dFraScTaxUnderlineWidthRatio = 0.96;
		m_oFrameScTaxUnderline.setWidth((int)(dFraScTaxUnderlineWidthRatio * this.getWidth()));
		
		double dFraExtraInfoUnderlineWidthRatio = 0.96;
		m_oFrameExtraInfoUnderline.setWidth((int)(dFraExtraInfoUnderlineWidthRatio * this.getWidth()));
		
		double dLabelChkTotalLeftRatio = 0.54;
		m_oLabelCheckTotal.setLeft((int)(dLabelChkTotalLeftRatio * this.getWidth()));
		
	}
	
	public void setItemLineCount(String[] sLineCount) {
		m_oLabelItemLineCount.setValue(sLineCount);
	}
	
	public void setItemTotal(String[] sItemTotal) {
		m_oLabelItemTotal.setValue(sItemTotal);
	}
	
	public void setItemDiscount(String[] sDiscount) {
		m_oLabelItemDiscount.setValue(sDiscount);
	}
//KingsleyKwan20170918ScTax		-----Start-----
	public void setScTaxTotal(String[] sScTotal, String[] sTaxTotal) {
		m_oLabelScTotal.setValue(sScTotal);
		m_oLabelTaxTotal.setValue(sTaxTotal);
	}
//KingsleyKwan20170918ScTax		----- End -----
	
	public boolean checkBasketExtended() {
		return m_bBasketExtended;
	}
	
	public void setBasketExtended(boolean bBasketExtended) {
		m_bBasketExtended = bBasketExtended; 
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		// Find the clicked button
		if (m_oImageBasketExtendBar.getId() == iChildId) {
			for (FrameBasketExtendBarListener listener : listeners) {
				// Raise the event to parent
			   	listener.frameBasketExtendBar_ExtendBarClicked();
			}
			bMatchChild = true;
		} else if (m_oFrameDineInTakeout.getId() == iChildId) {
			for (FrameBasketExtendBarListener listener : listeners) {
				listener.frameBasketExtendBar_DineInTakeoutClicked();
			}
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
	
}
