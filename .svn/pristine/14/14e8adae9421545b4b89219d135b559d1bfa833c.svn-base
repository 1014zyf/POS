package app;

import java.math.BigDecimal;
import java.util.ArrayList;

import om.PosDisplayPanelLookup;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method **/
interface FrameLookupButtonListener {
	void frameLookupButton_clicked(String sNote);
	void frameLookupButton_addQtyClicked(String sNote);
	void frameLookupButton_minusQtyClicked(String sNote);
}

public class FrameLookupButton extends VirtualUIFrame {
	TemplateBuilder m_oTemplateBuilder;

	private VirtualUILabel m_oLabelBtnDesc;
	private VirtualUILabel m_oLabelBtnPrice;
	private VirtualUIImage m_oImageBtnImg;
	private VirtualUIImage m_oIconBtnImg;
	private VirtualUILabel m_oLabelBtnStockQty;
	
	private VirtualUIFrame m_oFrameTransparentCover;

	private VirtualUIFrame m_oFrameQty;
	private VirtualUILabel m_oLabelAddQty;
	private VirtualUILabel m_oLabelMinusQty;
	private VirtualUILabel m_oLabelQty;
	private VirtualUIFrame m_oFrameCover;
	
	private int m_iItemId;
	private String m_sButtonType;

	public static String BUTTON_NOTE_ID = "item_id";
	public static String BUTTON_NOTE_TYPE = "type";
	public static String BUTTON_NOTE_SEQ = "seq";
	public static String BUTTON_NOTE_PARAMETER = "parameter";
	public static String BUTTON_NOTE_PANEL_TAB = "tab";
	public static String BUTTON_NOTE_MENU_ID = "menu_id";
	public static String BUTTON_NOTE_NAME = "name";
	
	public static final String QUANTITY_SOLDOUT = "X";

	public static final int BUTTON_DESC_FONT_SIZE = 16;
	private static final double RATIO = 0.75;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameLookupButtonListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameLookupButtonListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameLookupButtonListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameLookupButton() {
		m_oTemplateBuilder = new TemplateBuilder();

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraLookupButton.xml");

		m_oLabelBtnDesc = null;
		m_oImageBtnImg = null;
		m_oIconBtnImg = null;
		m_oLabelBtnStockQty = null;
		m_oLabelBtnPrice = null;
		m_oFrameQty = null;
		m_oLabelAddQty = null;
		m_oLabelQty = null;
		m_oLabelMinusQty = null;
		m_oFrameCover = null;

		listeners = new ArrayList<FrameLookupButtonListener>();
	}

	public void init(double dRatio, FuncLookupButtonInfo oButtonInfo, String sNote) {
		// if type of button is no, not allow click
		if (!oButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_NO)) {
			this.allowClick(true);
			this.allowLongClick(true);
			this.setClickServerRequestNote(sNote);
			this.setLongClickServerRequestNote(sNote);
		}

		// set button background color, font color, border color
		if (oButtonInfo.getBackgroundColor() != null)
			this.setBackgroundColor(oButtonInfo.getBackgroundColor());
		
		if (oButtonInfo.getBorderColor() != null)
			this.setStrokeColor(oButtonInfo.getBorderColor());

		// Button Image
		if (oButtonInfo.haveImage()) {
			if (m_oImageBtnImg == null) {
				m_oImageBtnImg = new VirtualUIImage();
				m_oTemplateBuilder.buildImage(m_oImageBtnImg, "btnImage");
				m_oImageBtnImg.setWidth(this.getWidth());

				m_oImageBtnImg.setVisible(false);
				this.attachChild(m_oImageBtnImg);
			} else 
				m_oImageBtnImg.setWidth(this.getWidth());
			
			if ((oButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_DIRECT_PAYMENT) || oButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_FUNCTION)) && AppGlobal.g_oFuncStation.get().getStationDevice().isSelfOrderKiosk())
				m_oImageBtnImg.setHeight(this.getHeight());
			else
				m_oImageBtnImg.setHeight((int) (this.getHeight() * FrameLookupButton.RATIO));
		}

		// Button Icon Image
		if (oButtonInfo.haveIconImage()) {
			if (m_oIconBtnImg == null) {
				m_oIconBtnImg = new VirtualUIImage();
				m_oTemplateBuilder.buildImage(m_oIconBtnImg, "btnIconImage");
				
				m_oIconBtnImg.setVisible(false);
				this.attachChild(m_oIconBtnImg);
			}
			
			m_oIconBtnImg.setWidth((int)(this.getWidth() * 0.4));
			m_oIconBtnImg.setHeight((int)(this.getHeight() * 0.4));
			m_oIconBtnImg.setTop(this.getHeight() - m_oIconBtnImg.getHeight());
			m_oIconBtnImg.setLeft(this.getWidth() - m_oIconBtnImg.getWidth());
		}
		
		// Button Description
		if (m_oLabelBtnDesc == null) {
			m_oLabelBtnDesc = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelBtnDesc, "btnDesc");
			if (oButtonInfo.getFontSize() > 0)
				m_oLabelBtnDesc.setTextSize(oButtonInfo.getFontSize());

			if (this.getTextAlign() != null && !this.getTextAlign().isEmpty())
				m_oLabelBtnDesc.setTextAlign(this.getTextAlign());
			this.attachChild(m_oLabelBtnDesc);
		}
		m_oLabelBtnDesc.setWidth(this.getWidth());
		m_oLabelBtnDesc.setValue(oButtonInfo.getName());
		
		if (oButtonInfo.getFontColor() != null)
			m_oLabelBtnDesc.setForegroundColor(oButtonInfo.getFontColor());

		// Change Desc label position and color if button has image
		int iDescLabelTop = 0;
		int iDescLabelHeight = 0;
		int iTransparentCoverHeight = 0;
		if (oButtonInfo.getImage() != null) {
			m_oImageBtnImg.setSource(oButtonInfo.getImage());
			m_oImageBtnImg.setVisible(true);

			if ((oButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_DIRECT_PAYMENT) || oButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_FUNCTION)) && AppGlobal.g_oFuncStation.get().getStationDevice().isSelfOrderKiosk()) {
				iDescLabelHeight = (int) (this.getHeight() * (1 - FrameLookupButton.RATIO));
				iDescLabelTop = this.getHeight() - iDescLabelHeight;
				
				m_oLabelBtnDesc.setVisible(false);
			} else {
				iDescLabelTop = m_oImageBtnImg.getHeight();
				iDescLabelHeight = this.getHeight() - m_oImageBtnImg.getHeight();
			}
			m_oLabelBtnDesc.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
			
			iTransparentCoverHeight = (int) (this.getHeight() * FrameLookupButton.RATIO);
		} else {
			if (oButtonInfo.haveImage())
				m_oImageBtnImg.setVisible(false);

			m_oLabelBtnDesc.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
			iDescLabelHeight = this.getHeight();
			
			iTransparentCoverHeight = this.getHeight();
		}
		
		//transparent for buttons that don't have image
		if (m_oFrameTransparentCover == null) {
			m_oFrameTransparentCover = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(m_oFrameTransparentCover, "fraTransparentCover");
			this.attachChild(m_oFrameTransparentCover);
		}
		m_oFrameTransparentCover.setWidth(this.getWidth());
		m_oFrameTransparentCover.setHeight(iTransparentCoverHeight);
		m_oFrameTransparentCover.setVisible(false);
		
		//if the button is not able to click, it should be dim
		if(!oButtonInfo.getAbleToClick()){
			oButtonInfo.setDim(true);
			m_oFrameTransparentCover.setEnabled(false);
		}
		
		if(oButtonInfo.getDim())
			m_oFrameTransparentCover.setVisible(true);
		
		if (oButtonInfo.getIconImage() != null && oButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_DIRECT_PAYMENT)) {
			m_oIconBtnImg.setSource(oButtonInfo.getIconImage());
			m_oIconBtnImg.setVisible(true);
		} else {
			if (oButtonInfo.haveIconImage())
				m_oIconBtnImg.setVisible(false);
		}
		
		// Button Information
		int iPriceLabelHeight = 0;
		if (oButtonInfo.getPrice() != null) {
			if (m_oLabelBtnPrice == null) {
				m_oLabelBtnPrice = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(m_oLabelBtnPrice, "btnInformation");

				if (oButtonInfo.getPriceFontColor() != null)
					m_oLabelBtnPrice.setForegroundColor(oButtonInfo.getPriceFontColor());

				this.attachChild(m_oLabelBtnPrice);
			}

			m_oLabelBtnPrice.setWidth(this.getWidth() - m_oLabelBtnPrice.getLeft());
			iPriceLabelHeight = (int) (this.getHeight() * (1 - FrameLookupButton.RATIO));

			if (oButtonInfo.haveImage() && oButtonInfo.getImage() != null) {
				iDescLabelHeight = (this.getHeight() - m_oImageBtnImg.getHeight()) / 2;
				iPriceLabelHeight = iDescLabelHeight;
				iDescLabelTop = this.getHeight() - iDescLabelHeight - iPriceLabelHeight;
			}

			m_oLabelBtnPrice.setValue(oButtonInfo.getPrice());
			if (oButtonInfo.getPriceInLeftTopCorner())
				m_oLabelBtnPrice.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.TOP + ","
						+ HeroActionProtocol.View.Attribute.TextAlign.LEFT);
		}

		// Button Stock Qty
		if (oButtonInfo.haveStockQty()) {
			if (m_oLabelBtnStockQty == null) {
				m_oLabelBtnStockQty = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(m_oLabelBtnStockQty, "lblStockQty");
				this.attachChild(m_oLabelBtnStockQty);
			}
			int iPadding = 2;
			int iMinHeight = 14;
			double StockQtyWidthHeightRatio = 0.75;

			//int iBtnWidth = (int) (this.getWidth() * dRatio);
			int iBtnHeight = (int) (this.getHeight() * dRatio * StockQtyWidthHeightRatio);
			if (iBtnHeight < iMinHeight) {
				iBtnHeight = iMinHeight;
				//iBtnWidth = (int) (iMinHeight / StockQtyWidthHeightRatio);
			}
			
			//  m_oLabelBtnStockQty.setWidth(iBtnWidth);
			m_oLabelBtnStockQty.setLeft(this.getWidth() - m_oLabelBtnStockQty.getWidth() - iPadding);
			//	m_oLabelBtnStockQty.setHeight((int) (this.getHeight() * (1 - FrameLookupButton.RATIO)));

			int iStockQtyLabelTop = this.getHeight() - m_oLabelBtnStockQty.getHeight() - iPadding;
			if (oButtonInfo.getImage() != null && !oButtonInfo.haveAddMinusBtn())
				iStockQtyLabelTop = m_oImageBtnImg.getHeight() - m_oLabelBtnStockQty.getHeight() - iPadding;
			m_oLabelBtnStockQty.setTop(iStockQtyLabelTop);

			m_oLabelBtnStockQty.setVisible(false);
		}

		// add, minus button and item count label
		if (oButtonInfo.haveAddMinusBtn()) {
			// Create Qty Frame
			double dFrameQtyHeightRatio = 1 - FrameLookupButton.RATIO;
			int iItemFrameWidth = this.getWidth() / 3;
			int iItemFrameHeight = (int) (this.getHeight() * dFrameQtyHeightRatio);

			if (m_oFrameQty == null) {
				m_oFrameQty = new VirtualUIFrame();
				m_oTemplateBuilder.buildFrame(m_oFrameQty, "frmQty");
				m_oFrameQty.setLeft(0);
				m_oFrameQty.setWidth(this.getWidth());
				m_oFrameQty.setHeight(iItemFrameHeight);
				this.attachChild(m_oFrameQty);
			}

			// Create Add button
			if (m_oLabelAddQty == null) {
				m_oLabelAddQty = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(m_oLabelAddQty, "lblAddQty");
				m_oLabelAddQty.allowClick(true);
				m_oLabelAddQty.setClickServerRequestBlockUI(false);
				m_oLabelAddQty.setValue("+");
				m_oLabelAddQty.setLeft(iItemFrameWidth * 2);

				m_oLabelAddQty.setWidth(iItemFrameWidth);
				m_oLabelAddQty.setHeight(m_oFrameQty.getHeight());
				m_oFrameQty.attachChild(m_oLabelAddQty);
			} else
				m_oLabelAddQty.setVisible(true);
			m_oLabelAddQty.setClickServerRequestNote(sNote);

			// Create Item qty label
			if (m_oLabelQty == null) {
				int iPadding = 0;
				if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.horizontal_desktop.name()))
						iPadding = 4;
				m_oLabelQty = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(m_oLabelQty, "lblQty");
				m_oLabelQty.setValue("0");
				m_oLabelQty.setTop(iPadding);
				m_oLabelQty.setLeft(m_oLabelAddQty.getWidth() + iPadding);
				m_oLabelQty.setWidth(iItemFrameWidth - (iPadding * 2));
				m_oLabelQty.setHeight(m_oFrameQty.getHeight() - (iPadding * 2));

				m_oFrameQty.attachChild(m_oLabelQty);
			} else
				m_oLabelQty.setVisible(true);

			// Create Minus button
			if (m_oLabelMinusQty == null) {
				m_oLabelMinusQty = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(m_oLabelMinusQty, "lblMinusQty");
				m_oLabelMinusQty.allowClick(true);
				m_oLabelMinusQty.setClickServerRequestBlockUI(false);
				m_oLabelMinusQty.setValue("-");
				m_oLabelMinusQty.setLeft(0);

				m_oLabelMinusQty.setWidth(iItemFrameWidth);
				m_oLabelMinusQty.setHeight(m_oFrameQty.getHeight());
				m_oFrameQty.attachChild(m_oLabelMinusQty);
			} else
				m_oLabelMinusQty.setVisible(true);
			m_oLabelMinusQty.setClickServerRequestNote(sNote);
			
			if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.horizontal_desktop.name())) {
				if (oButtonInfo.haveImage() && oButtonInfo.getImage() != null) {
					// Show lookup name and price below +/- button
					m_oFrameQty.setTop(m_oImageBtnImg.getHeight() - m_oFrameQty.getHeight());
					iDescLabelTop = m_oFrameQty.getTop() + m_oFrameQty.getHeight();
					iDescLabelHeight = this.getHeight() - iDescLabelTop;
					// For button need to show price
					if (oButtonInfo.getPrice() != null)
						iPriceLabelHeight = m_oFrameQty.getTop() + m_oFrameQty.getHeight() / 2;
					// For button need to show stock quantity
					if (m_oLabelBtnStockQty != null) {
						m_oLabelBtnStockQty.setTop(this.getHeight() - m_oLabelBtnStockQty.getHeight());
						m_oLabelBtnDesc.setWidth(m_oLabelBtnStockQty.getLeft());
					}
					m_oLabelBtnDesc.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
				} else {
					m_oFrameQty.setTop(this.getHeight() - m_oFrameQty.getHeight());
					iDescLabelHeight = this.getHeight() - m_oFrameQty.getHeight();
					iDescLabelTop = 0;
					if (oButtonInfo.getPrice() != null)
						iPriceLabelHeight = m_oFrameQty.getHeight() / 2 + m_oFrameQty.getHeight();
					if (m_oLabelBtnStockQty != null)
						m_oLabelBtnStockQty.setTop(this.getHeight() - m_oFrameQty.getHeight() - m_oLabelBtnStockQty.getHeight());
					m_oLabelBtnDesc.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				}
			} else {
				m_oFrameQty.setTop(this.getHeight() - m_oFrameQty.getHeight());
				iDescLabelTop = 0;
				iDescLabelHeight = m_oFrameQty.getHeight();
				
				m_oLabelBtnDesc.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				m_oLabelBtnDesc.setBackgroundColor(m_oFrameQty.getBackgroundColor());
				m_oLabelBtnDesc.setForegroundColor("#FFFFFF");
				
				if (oButtonInfo.getPrice() != null)
					iPriceLabelHeight = m_oFrameQty.getHeight() / 2 + m_oFrameQty.getHeight();
			}
		} else {
			if (m_oLabelAddQty != null)
				m_oLabelAddQty.setVisible(false);

			if (m_oLabelMinusQty != null)
				m_oLabelMinusQty.setVisible(false);

			if (m_oLabelQty != null)
				m_oLabelQty.setVisible(false);
		}

		if(m_oLabelBtnDesc.getVisible()) {
			m_oLabelBtnDesc.setTop(iDescLabelTop);
			m_oLabelBtnDesc.setHeight(iDescLabelHeight);
		}
		
		if (oButtonInfo.getPrice() != null) {
			m_oLabelBtnPrice.setHeight(iPriceLabelHeight);
			m_oLabelBtnPrice.setTop(this.getHeight() - iPriceLabelHeight);
		}
		
		m_iItemId = oButtonInfo.getId();
		m_sButtonType = oButtonInfo.getType();
	}

	public void setButtonOtherInformation(String sInformation, boolean bLeftTopCorner) {
		m_oLabelBtnPrice.setValue(sInformation);
	}

	public void setButtonForegroundColor(String sBtnForegroundColor) {
		m_oLabelBtnDesc.setForegroundColor(sBtnForegroundColor);
		if (m_oLabelBtnPrice != null)
			m_oLabelBtnPrice.setForegroundColor(sBtnForegroundColor);
	}
	
	public void setButtonStockQty(String sQty) {
		m_oLabelBtnStockQty.setVisible(true);
		m_oLabelBtnStockQty.setValue(sQty);
		
		if(!sQty.equals(FrameLookupButton.QUANTITY_SOLDOUT)){
			BigDecimal dQty = new BigDecimal(sQty);
			
			// Set the transparent frame if stock qty equal to zero
			if(m_oFrameCover != null)
				m_oFrameCover.setVisible(false);
			
			if(dQty.compareTo(BigDecimal.ZERO) <= 0){
				if(m_oFrameCover == null){
					m_oFrameCover = new VirtualUIFrame();
					m_oTemplateBuilder.buildFrame(m_oFrameCover, "frmCover");
					if(m_oImageBtnImg != null){
						m_oFrameCover.setTop(m_oImageBtnImg.getTop());
						m_oFrameCover.setLeft(m_oImageBtnImg.getLeft());
						m_oFrameCover.setHeight(m_oImageBtnImg.getHeight());
						m_oFrameCover.setWidth(m_oImageBtnImg.getWidth());
					} else {
						m_oFrameCover.setTop(this.getTop());
						m_oFrameCover.setLeft(this.getLeft());
						m_oFrameCover.setHeight(this.getHeight());
						m_oFrameCover.setWidth(this.getWidth());
					}
					this.attachChild(m_oFrameCover);
				}
				m_oFrameCover.setVisible(true);
			}
		}
		
		this.setClickServerRequestBlockUI(true);
	}
	
	public String getButtonStockQtyValue() {
		return m_oLabelBtnStockQty.getValue();
	}
	
	public String getButtonDesc() {
		return m_oLabelBtnDesc.getValue();
	}
	
	public int getButtonItemId() {
		return m_iItemId;
	}
	
	public String getButtonType() {
		return m_sButtonType;
	}
	
	public void resizeButtonSizeByRation(double dRatio){
		int iShadowRadius = (int) (this.getShadowRadius() * dRatio);
		this.setWidth(this.getWidth() - (2 * iShadowRadius));
		this.setHeight(this.getHeight() - (2 * iShadowRadius));
		this.setTop(this.getTop() + iShadowRadius - (int)(this.getShadowTop() * dRatio));
		this.setLeft(this.getLeft() + iShadowRadius - (int)(this.getShadowLeft() * dRatio));
	}
	
	public void setButtonDesc(String sValue) {
		m_oLabelBtnDesc.setValue(sValue);
	}
	
	public void setButtonDescByLangIndex(int iIndex, String sValue) {
		m_oLabelBtnDesc.assignValueByIndex(iIndex, sValue);
	}
	
	public void setTranVisible(boolean bVisible){
		m_oFrameTransparentCover.setVisible(bVisible);
	}

	public void setButtonQty(String sQty) {
		if (m_oLabelQty != null)
			m_oLabelQty.setValue(sQty);
	}

	public String getButtonQtyValue() {
		return m_oLabelQty.getValue();
	}

	public String getButtonInformation() {
		if (m_oLabelBtnPrice == null)
			return "";

		return this.m_oLabelBtnPrice.getValue();
	}

	public void setButtonStockLabelVisible(boolean bShow) {
		m_oLabelBtnStockQty.setVisible(bShow);

		this.setClickServerRequestBlockUI(bShow);
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (this.getId() == iChildId) {
			for (FrameLookupButtonListener listener : listeners) {
				listener.frameLookupButton_clicked(sNote);
			}
			bMatchChild = true;
		} else if (m_oLabelAddQty != null && m_oLabelAddQty.getId() == iChildId) {
			for (FrameLookupButtonListener listener : listeners) {
				listener.frameLookupButton_addQtyClicked(sNote);
			}
			bMatchChild = true;
		} else if (m_oLabelMinusQty != null && m_oLabelMinusQty.getId() == iChildId) {
			for (FrameLookupButtonListener listener : listeners) {
				listener.frameLookupButton_minusQtyClicked(sNote);
			}
			bMatchChild = true;
		}

		return bMatchChild;
	}

	@Override
	public boolean longClicked(int iChildId, String sNote) {
		return false;
	}
}
