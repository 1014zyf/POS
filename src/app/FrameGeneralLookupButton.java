package app;

import java.util.ArrayList;

import om.PosDisplayPanelLookup;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method **/
interface FrameGeneralLookupButtonListener {
	void frameGeneralLookupButton_clicked(String sNote);
	void frameGeneralLookupButton_addQtyClicked(String sNote);
	void frameGeneralLookupButton_minusQtyClicked(String sNote);
}

public class FrameGeneralLookupButton extends VirtualUIFrame implements FrameLookupButtonListener {
	private TemplateBuilder m_oTemplateBuilder;
	
	private FrameLookupButton m_oFrameFunctionButton;
	private FrameLookupButton m_oFrameItemButton;
	private FrameLookupButton m_oFrameModifierButton;
	private FrameLookupButton m_oFramePaymentButton;
	private FrameLookupButton m_oFrameMenuLookupButton;
	private FrameLookupButton m_oFrameModifierLookupButton;
	private FrameLookupButton m_oFrameSubPanelButton;
	private FrameLookupButton m_oFrameDiscountButton;
	private FrameLookupButton m_oFrameNoFunctionButton;
	
	private FrameLookupButton m_oCurrentFrameLookupButton;

	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameGeneralLookupButtonListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameGeneralLookupButtonListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameGeneralLookupButtonListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
	public FrameGeneralLookupButton() {
		// Load form from template file
    	m_oTemplateBuilder = new TemplateBuilder();
		m_oTemplateBuilder.loadTemplate("fraGeneralLookupButton.xml");
		
		m_oFrameFunctionButton = null;
		m_oFrameItemButton = null;
		m_oFrameModifierButton = null;
		m_oFramePaymentButton = null;
		m_oFrameModifierLookupButton = null;
		m_oFrameSubPanelButton = null;
		m_oFrameDiscountButton = null;
		m_oFrameNoFunctionButton = null;
		
		m_oCurrentFrameLookupButton = null;
		
		listeners = new ArrayList<FrameGeneralLookupButtonListener>();
	}

	// set button property, button can act as function, or item, or menu lookup, etc.
	public void setButtonProperty(FuncLookupButtonInfo oButtonInfo, double dRatio, String sNote) {
		if (m_oCurrentFrameLookupButton != null) {
			m_oCurrentFrameLookupButton.setVisible(false);
		}
		
		if (oButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_HOT_ITEM)) {
			if (m_oFrameItemButton == null) {
				m_oFrameItemButton = new FrameLookupButton();
				m_oTemplateBuilder.buildFrame(m_oFrameItemButton, "fraButHotItem");
				m_oFrameItemButton.addListener(this);
				this.attachChild(m_oFrameItemButton);
			} else {
				// Initialize the button position and size
				m_oTemplateBuilder.buildFrame(m_oFrameItemButton, "fraButHotItem");
			}
			m_oFrameItemButton.setWidth(this.getWidth());
			m_oFrameItemButton.setHeight(this.getHeight());
			m_oFrameItemButton.resizeButtonSizeByRation(0.55);
			
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblButHotItem");
			if (oButtonInfo.getFontSize() == 0)
				oButtonInfo.setFontSize(oLabel.getTextSize());
			if (oButtonInfo.getFontColor() == null)
				oButtonInfo.setFontColor(oLabel.getForegroundColor());
			
			m_oCurrentFrameLookupButton = m_oFrameItemButton;
		} else if (oButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_FUNCTION)) {
			if (m_oFrameFunctionButton == null) {
				m_oFrameFunctionButton = new FrameLookupButton();
				m_oTemplateBuilder.buildFrame(m_oFrameFunctionButton, "fraButFunction");
				m_oFrameFunctionButton.addListener(this);
				this.attachChild(m_oFrameFunctionButton);
			} else {
				// Initialize the button position and size
				m_oTemplateBuilder.buildFrame(m_oFrameFunctionButton, "fraButFunction");
			}
			m_oFrameFunctionButton.setWidth(this.getWidth());
			m_oFrameFunctionButton.setHeight(this.getHeight());
			m_oFrameFunctionButton.resizeButtonSizeByRation(0.55);
			
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblButFunction");
			if (oButtonInfo.getFontSize() == 0)
				oButtonInfo.setFontSize(oLabel.getTextSize());
			if (oButtonInfo.getFontColor() == null)
				oButtonInfo.setFontColor(oLabel.getForegroundColor());
			
			m_oCurrentFrameLookupButton = m_oFrameFunctionButton;
		} else if (oButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_MODIFIER_LOOKUP)) {
			if (m_oFrameModifierLookupButton == null) {
				m_oFrameModifierLookupButton = new FrameLookupButton();
				m_oTemplateBuilder.buildFrame(m_oFrameModifierLookupButton, "fraButModifierLookup");
				m_oFrameModifierLookupButton.addListener(this);
				this.attachChild(m_oFrameModifierLookupButton);
			} else {
				// Initialize the button position and size
				m_oTemplateBuilder.buildFrame(m_oFrameModifierLookupButton, "fraButModifierLookup");
			}
			m_oFrameModifierLookupButton.setWidth(this.getWidth());
			m_oFrameModifierLookupButton.setHeight(this.getHeight());
			m_oFrameModifierLookupButton.resizeButtonSizeByRation(0.55);
			
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblButModifierLookup");
			if (oButtonInfo.getFontSize() == 0)
				oButtonInfo.setFontSize(oLabel.getTextSize());
			if (oButtonInfo.getFontColor() == null)
				oButtonInfo.setFontColor(oLabel.getForegroundColor());
			
			m_oCurrentFrameLookupButton = m_oFrameModifierLookupButton;
		} else if (oButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_MENU_LOOKUP)) {
			if (m_oFrameMenuLookupButton == null) {
				m_oFrameMenuLookupButton = new FrameLookupButton();
				m_oTemplateBuilder.buildFrame(m_oFrameMenuLookupButton, "fraButLookup");
				m_oFrameMenuLookupButton.addListener(this);
				this.attachChild(m_oFrameMenuLookupButton);
			} else {
				// Initialize the button position and size
				m_oTemplateBuilder.buildFrame(m_oFrameMenuLookupButton, "fraButLookup");
			}
			m_oFrameMenuLookupButton.setWidth(this.getWidth());
			m_oFrameMenuLookupButton.setHeight(this.getHeight());
			m_oFrameMenuLookupButton.resizeButtonSizeByRation(0.55);
			
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblButLookup");
			if (oButtonInfo.getFontSize() == 0)
				oButtonInfo.setFontSize(oLabel.getTextSize());
			if (oButtonInfo.getFontColor() == null)
				oButtonInfo.setFontColor(oLabel.getForegroundColor());
			
			m_oCurrentFrameLookupButton = m_oFrameMenuLookupButton;
		} else if (oButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_HOT_MODIFIER)) {
			if (m_oFrameModifierButton == null) {
				m_oFrameModifierButton = new FrameLookupButton();
				m_oTemplateBuilder.buildFrame(m_oFrameModifierButton, "fraButHotModifier");
				m_oFrameModifierButton.addListener(this);
				this.attachChild(m_oFrameModifierButton);
			} else {
				// Initialize the button position and size
				m_oTemplateBuilder.buildFrame(m_oFrameModifierButton, "fraButHotModifier");
			}
			m_oFrameModifierButton.setWidth(this.getWidth());
			m_oFrameModifierButton.setHeight(this.getHeight());
			m_oFrameModifierButton.resizeButtonSizeByRation(0.55);
			
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblButHotModifier");
			if (oButtonInfo.getFontSize() == 0)
				oButtonInfo.setFontSize(oLabel.getTextSize());
			if (oButtonInfo.getFontColor() == null)
				oButtonInfo.setFontColor(oLabel.getForegroundColor());
			
			m_oCurrentFrameLookupButton = m_oFrameModifierButton;
		} else if (oButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_DIRECT_PAYMENT)) {
			if (m_oFramePaymentButton == null) {
				m_oFramePaymentButton = new FrameLookupButton();
				m_oTemplateBuilder.buildFrame(m_oFramePaymentButton, "fraButPayment");
				m_oFramePaymentButton.addListener(this);
				this.attachChild(m_oFramePaymentButton);
			} else {
				// Initialize the button position and size
				m_oTemplateBuilder.buildFrame(m_oFramePaymentButton, "fraButPayment");
			}
			m_oFramePaymentButton.setWidth(this.getWidth());
			m_oFramePaymentButton.setHeight(this.getHeight());
			m_oFramePaymentButton.resizeButtonSizeByRation(0.55);
			
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblButPayment");
			if (oButtonInfo.getFontSize() == 0)
				oButtonInfo.setFontSize(oLabel.getTextSize());
			if (oButtonInfo.getFontColor() == null)
				oButtonInfo.setFontColor(oLabel.getForegroundColor());
			
			m_oCurrentFrameLookupButton = m_oFramePaymentButton;
		} else if (oButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_SUB_PANEL_PAGE)) {
			if (m_oFrameSubPanelButton == null) {
				m_oFrameSubPanelButton = new FrameLookupButton();
				m_oTemplateBuilder.buildFrame(m_oFrameSubPanelButton, "fraButSubPanel");
				m_oFrameSubPanelButton.addListener(this);
				this.attachChild(m_oFrameSubPanelButton);
			} else {
				// Initialize the button position and size
				m_oTemplateBuilder.buildFrame(m_oFrameSubPanelButton, "fraButSubPanel");
			}
			m_oFrameSubPanelButton.setWidth(this.getWidth());
			m_oFrameSubPanelButton.setHeight(this.getHeight());
			m_oFrameSubPanelButton.resizeButtonSizeByRation(0.55);
			
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblButSubPanel");
			if (oButtonInfo.getFontSize() == 0)
				oButtonInfo.setFontSize(oLabel.getTextSize());
			if (oButtonInfo.getFontColor() == null)
				oButtonInfo.setFontColor(oLabel.getForegroundColor());
			
			m_oCurrentFrameLookupButton = m_oFrameSubPanelButton;
		} else if (oButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_DIRECT_DISCOUNT)) {
			if (m_oFrameDiscountButton == null) {
				m_oFrameDiscountButton = new FrameLookupButton();
				m_oTemplateBuilder.buildFrame(m_oFrameDiscountButton, "fraButDiscount");
				m_oFrameDiscountButton.addListener(this);
				this.attachChild(m_oFrameDiscountButton);
			} else {
				// Initialize the button position and size
				m_oTemplateBuilder.buildFrame(m_oFrameDiscountButton, "fraButDiscount");
			}
			m_oFrameDiscountButton.setWidth(this.getWidth());
			m_oFrameDiscountButton.setHeight(this.getHeight());
			m_oFrameDiscountButton.resizeButtonSizeByRation(0.55);
			
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblButDiscount");
			if (oButtonInfo.getFontSize() == 0)
				oButtonInfo.setFontSize(oLabel.getTextSize());
			if (oButtonInfo.getFontColor() == null)
				oButtonInfo.setFontColor(oLabel.getForegroundColor());
			
			m_oCurrentFrameLookupButton = m_oFrameDiscountButton;
		} else {
			if (m_oFrameNoFunctionButton == null) {
				m_oFrameNoFunctionButton = new FrameLookupButton();
				m_oTemplateBuilder.buildFrame(m_oFrameNoFunctionButton, "fraButNoFunction");
				m_oFrameNoFunctionButton.addListener(this);
				this.attachChild(m_oFrameNoFunctionButton);
			} else {
				// Initialize the button position and size
				m_oTemplateBuilder.buildFrame(m_oFrameNoFunctionButton, "fraButNoFunction");
			}
			m_oFrameNoFunctionButton.setWidth(this.getWidth());
			m_oFrameNoFunctionButton.setHeight(this.getHeight());
			m_oFrameNoFunctionButton.resizeButtonSizeByRation(0.55);
			
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblButNoFunction");
			if (oButtonInfo.getFontSize() == 0)
				oButtonInfo.setFontSize(oLabel.getTextSize());
			if (oButtonInfo.getFontColor() == null)
				oButtonInfo.setFontColor(oLabel.getForegroundColor());
			
			m_oCurrentFrameLookupButton = m_oFrameNoFunctionButton;
		}
		
		m_oCurrentFrameLookupButton.init(dRatio, oButtonInfo, sNote);
		m_oCurrentFrameLookupButton.setVisible(true);
	}
	
	public void setButtonStockLabelVisible(boolean bShow) {
		m_oCurrentFrameLookupButton.setButtonStockLabelVisible(bShow);
	}
	
	public void setButtonStockQty(String sQty) {
		m_oCurrentFrameLookupButton.setVisible(true);
		m_oCurrentFrameLookupButton.setButtonStockQty(sQty);
	}
	
	public void setButtonQty(String sQty) {
		m_oCurrentFrameLookupButton.setButtonQty(sQty);
	}
	
	public void setButtonOtherInformation(String sInformation, boolean bLeftTopCorner) {
		m_oCurrentFrameLookupButton.setButtonOtherInformation(sInformation, bLeftTopCorner);
	}
	
	public String getButtonStockQtyValue() {
		return m_oCurrentFrameLookupButton.getButtonStockQtyValue();
	}

	// Add by Jonnie 2017-10-20 ---Start---
	public String getButtonInformation() {
		if (m_oCurrentFrameLookupButton == null)
			return "";
		return m_oCurrentFrameLookupButton.getButtonInformation();
	}
	// Add by Jonnie 2017-10-20 ---End---
	
	public String getButtonQtyValue() {
		return m_oCurrentFrameLookupButton.getButtonQtyValue();
	}
	
	public String getButtonClickServerRequestNote() {
		return m_oCurrentFrameLookupButton.getClickServerRequestNote();
	}
	
	public boolean isNoFunctionButton() {
		return (m_oCurrentFrameLookupButton == m_oFrameNoFunctionButton);
	}
	
	public void setButtonClickBlockUI(boolean bBlockUI) {
		if (m_oCurrentFrameLookupButton != null) {
			m_oCurrentFrameLookupButton.setClickServerRequestBlockUI(bBlockUI);
		}
	}
	
	public void setButtonLongClickBlockUI(boolean bBlockUI) {
		if (m_oCurrentFrameLookupButton != null) {
			m_oCurrentFrameLookupButton.setLongClickServerRequestBlockUI(bBlockUI);
		}
	}
	
	public void setTransparentVisible(boolean bVisible){
		if (m_oCurrentFrameLookupButton != null){
			m_oCurrentFrameLookupButton.setTranVisible(bVisible);
		}
	}

	public FrameLookupButton getCurrentButton() {
		return m_oCurrentFrameLookupButton;
	}
	
	@Override
	public void frameLookupButton_clicked(String sNote) {
		for (FrameGeneralLookupButtonListener listener: listeners) {
			listener.frameGeneralLookupButton_clicked(sNote);
		}
	}

	@Override
	public void frameLookupButton_addQtyClicked(String sNote) {
		for (FrameGeneralLookupButtonListener listener: listeners) {
			listener.frameGeneralLookupButton_addQtyClicked(sNote);
		}
	}

	@Override
	public void frameLookupButton_minusQtyClicked(String sNote) {
		for (FrameGeneralLookupButtonListener listener: listeners) {
			listener.frameGeneralLookupButton_minusQtyClicked(sNote);
		}
	}
}
