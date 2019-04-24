package app;

import java.util.ArrayList;

import commonui.FrameNumberPad;
import commonui.FrameNumberPadListener;
import commonui.FrameTitleHeader;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIKeyboardReader;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

interface FrameEnterItemPLUListener {
	void FrameEnterItemPLU_clickOK(boolean clientValueCleared);
	void FrameEnterItemPLU_clickCancel();
	void FrameEnterItemPLU_swipeCard(String sSwipeCardValue);
}

public class FrameEnterItemPLU extends VirtualUIFrame implements FrameNumberPadListener, FrameSeatPanelListener {

	private TemplateBuilder m_oTemplateBuilder;
	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUILabel m_oLabelItemCode;
	private VirtualUITextbox m_oTxtboxItemCode;
	private VirtualUILabel m_oLabelQuantity;
	private VirtualUITextbox m_oTxtBoxQuantity;
	private VirtualUIButton m_oButtonOK;
	private VirtualUIButton m_oButtonCancel;
	private VirtualUIKeyboardReader m_oKeyboardReaderForOK;
	private FrameSeatPanel m_oFrameSeatPanel;
	private FrameNumberPad m_oFrameNumberPad;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameEnterItemPLUListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameEnterItemPLUListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameEnterItemPLUListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public void init() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameEnterItemPLUListener>();
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraEnterItemPLU.xml");

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(false);
		this.attachChild(m_oFrameTitleHeader);

		// Number pad
		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.setCancelAndEnterToLeftAndRigth(true);
		m_oFrameNumberPad.setNumPadLeft(400);
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oFrameNumberPad.setNumPadLeft(45);
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.addListener(this);
		m_oFrameNumberPad.setVisible(true);
		this.attachChild(m_oFrameNumberPad);

		// Label PLU
		m_oLabelItemCode = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelItemCode, "lblItemCode");
		m_oLabelItemCode.setValue(AppGlobal.g_oLang.get()._("item_code"));
		this.attachChild(m_oLabelItemCode);

		// textbox PLU
		m_oTxtboxItemCode = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxItemCode, "txtboxItemCode");
		m_oTxtboxItemCode.setTop(m_oLabelItemCode.getTop() + m_oLabelItemCode.getHeight() + m_oTxtboxItemCode.getTop());
		m_oTxtboxItemCode.setFocusWhenShow(true);
		this.attachChild(m_oTxtboxItemCode);

		m_oFrameNumberPad.clearEnterSubmitId();
		m_oFrameNumberPad.setEnterSubmitId(m_oTxtboxItemCode);

		// Label Quantity
		m_oLabelQuantity = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelQuantity, "lblQuantity");
		m_oLabelQuantity.setTop(m_oTxtboxItemCode.getTop() + m_oTxtboxItemCode.getHeight() + m_oLabelQuantity.getTop());
		m_oLabelQuantity.setValue(AppGlobal.g_oLang.get()._("quantity"));
		this.attachChild(m_oLabelQuantity);

		// TextBox Quantity
		m_oTxtBoxQuantity = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtBoxQuantity, "txtboxQuantity");
		m_oTxtBoxQuantity.setTop(m_oLabelQuantity.getTop() + m_oLabelQuantity.getHeight() + m_oTxtBoxQuantity.getTop());
		m_oTxtBoxQuantity.setValue("1");
		this.attachChild(m_oTxtBoxQuantity);

		// Seat panel
		m_oFrameSeatPanel = new FrameSeatPanel(FrameSeatPanel.TYPE_SEAT_PANEL_NUMPAD);
		m_oTemplateBuilder.buildFrame(m_oFrameSeatPanel, "fraSeatPanel");
		m_oFrameSeatPanel.setShowHorizontalSeatListOnly(0, 0, m_oFrameSeatPanel.getHeight(), m_oFrameSeatPanel.getWidth());
		if (!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oFrameSeatPanel.setTop(m_oTxtBoxQuantity.getHeight() + m_oTxtBoxQuantity.getTop() + m_oFrameSeatPanel.getTop());
		m_oFrameSeatPanel.addListener(this);
		
		// Add Seat to Seat Panel
		m_oFrameSeatPanel.addNumberOfSeat(AppGlobal.MAX_SEATS);
		this.attachChild(m_oFrameSeatPanel);

		// Create OK Button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "btnOK");
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("ok"));
		m_oButtonOK.addClickServerRequestSubmitElement(this);
		m_oButtonOK.setVisible(false);
		this.attachChild(m_oButtonOK);

		// Create Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "btnCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		m_oButtonCancel.setClickServerRequestBlockUI(false);
		m_oButtonCancel.setVisible(false);
		this.attachChild(m_oButtonCancel);

		// Keyboard
		m_oKeyboardReaderForOK = new VirtualUIKeyboardReader();
		m_oKeyboardReaderForOK.addKeyboardKeyCode(13);
		m_oKeyboardReaderForOK.setKeyboardServerRequestNote("value_cleared");
		m_oKeyboardReaderForOK.addKeyboardServerRequestSubmitElement(m_oTxtboxItemCode);
		m_oKeyboardReaderForOK.setKeyboardReplaceValue(m_oTxtboxItemCode, ".*", "");
		this.attachChild(m_oKeyboardReaderForOK);
	}

	public void resetScreen() {
		// Clear the value
		setItemCode("");
		setQuantity("1");
		m_oFrameSeatPanel.resetSelectedSeat();
		focusOnItemCodeTxtBox();
	}

	public void setTitle(String sTitle) {
		m_oFrameTitleHeader.setTitle(sTitle);
	}

	public void setItemCode(String sPLUCode) {
		m_oTxtboxItemCode.setValue(sPLUCode);
	}

	public String getItemCode() {
		return m_oTxtboxItemCode.getValue();
	}

	public void setQuantity(String sQty) {
		m_oTxtBoxQuantity.setValue(sQty);
	}

	public String getQuantity() {
		return m_oTxtBoxQuantity.getValue();
	}

	public void setKeyboardType(String sType) {
		// TODO Auto-generated method stub
		m_oTxtboxItemCode.setKeyboardType(sType);
		if (sType.equals(HeroActionProtocol.View.Attribute.KeyboardType.NUMBER)
				|| sType.equals(HeroActionProtocol.View.Attribute.KeyboardType.DECIMAL)
				|| sType.equals(HeroActionProtocol.View.Attribute.KeyboardType.PHONE)) {
			if (!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				this.setTop(136);
				this.setHeight(m_oFrameNumberPad.getTop() + m_oFrameNumberPad.getHeight() + 10);
			}
			m_oFrameNumberPad.setVisible(true);
			m_oFrameNumberPad.setEnterBlockUI(true);
			m_oFrameNumberPad.clearEnterSubmitId();
			m_oFrameNumberPad.setEnterSubmitId(m_oTxtboxItemCode);
			m_oFrameNumberPad.setEnterSubmitId(m_oTxtBoxQuantity);
		}
	}

	public void showKeyboard() {
		// show the keyboard
		AppGlobal.g_oTerm.get().showKeyboard();
	}

	public void setEnterBlockUI(boolean bBlockUI) {
		m_oFrameNumberPad.setEnterBlockUI(bBlockUI);
	}

	public void focusOnItemCodeTxtBox() {
		m_oTxtboxItemCode.setFocus();
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (iChildId == m_oButtonOK.getId()) {
			for (FrameEnterItemPLUListener listener : listeners) {
				// Raise the event to parent
				listener.FrameEnterItemPLU_clickOK(false);
			}
			bMatchChild = true;
		} else if (iChildId == m_oButtonCancel.getId()) {
			for (FrameEnterItemPLUListener listener : listeners) {
				// Raise the event to parent
				listener.FrameEnterItemPLU_clickCancel();
			}
			bMatchChild = true;
		}
		return bMatchChild;
	}

	@Override
	public boolean keyboard(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (iChildId == m_oKeyboardReaderForOK.getId()) {
			for (FrameEnterItemPLUListener listener : listeners) {
				// Raise the event to parent
				boolean clientValueCleared = sNote.contentEquals("value_cleared");
				listener.FrameEnterItemPLU_clickOK(clientValueCleared);
			}
			bMatchChild = true;
		}
		return bMatchChild;
	}

	@Override
	public void frameSeatPanel_selectedSeat(int iSeatId) {
		m_oTxtBoxQuantity.setValue(Integer.toString(iSeatId + 1));
	}

	@Override
	public void frameSeatPanel_clickCourse() {
		// TODO Auto-generated method stub
	}

	@Override
	public void FrameNumberPad_clickEnter() {
		if (m_oTxtboxItemCode.getValue().length() > 0) {
			if (m_oTxtBoxQuantity.getValue().length() > 0) {
				for (FrameEnterItemPLUListener listener : listeners) {
					// Raise the event to parent
					listener.FrameEnterItemPLU_clickOK(false);
				}
			}
		} else
			m_oTxtboxItemCode.setFocus();
	}

	@Override
	public void FrameNumberPad_clickCancel() {
		for (FrameEnterItemPLUListener listener : listeners) {
			// Raise the event to parent
			listener.FrameEnterItemPLU_clickCancel();
		}
	}

	@Override
	public void FrameNumberPad_clickNumber(String string) {
		// TODO Auto-generated method stub
	}
}
