package app;

import java.util.ArrayList;

import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FrameBasketSelectPanelListener {
	void frameBasketSelectPanel_SelectAllClicked(boolean bSelectAll);

	void frameBasketSelectPanel_CancelClicked();

	void frameBasketSelectPanel_ConfirmClicked();
}

public class FrameBasketSelectPanel extends VirtualUIFrame {

	TemplateBuilder m_oTemplateBuilder;

	private boolean m_bSelectAll;
	private VirtualUILabel m_oDescLabel;
	private VirtualUIButton m_oSelectAllButton;
	private VirtualUIButton m_oCancelButton;
	private VirtualUIButton m_oConfirmButton;
	
	public static final int BASKET_SELECT_MODE_NO = 0;
	public static final int BASKET_SELECT_MODE_SINGLE = 1;
	public static final int BASKET_SELECT_MODE_MULTI = 2;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameBasketSelectPanelListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameBasketSelectPanelListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameBasketSelectPanelListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	//Constructor
	public FrameBasketSelectPanel() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameBasketSelectPanelListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraBasketSelectPanel.xml");
		
		// Set the default as select all
		m_bSelectAll = true;
		
		// Load child elements from template
		/////////////////////////////////////////////////////////////////
		// Description
		m_oDescLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oDescLabel, "lblDesc");
		this.attachChild(m_oDescLabel);
		
		/////////////////////////////////////////////////////////////////
		// Select all button
		m_oSelectAllButton = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oSelectAllButton, "butSelectAll");
		m_oSelectAllButton.allowClick(true);
		m_oSelectAllButton.setClickServerRequestBlockUI(false);
		m_oSelectAllButton.setValue(AppGlobal.g_oLang.get()._("select_all", ""));
		this.attachChild(m_oSelectAllButton);
		
		/////////////////////////////////////////////////////////////////
		// Confirm button
		m_oConfirmButton = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oConfirmButton, "butConfirm");
		m_oConfirmButton.allowClick(true);
		m_oConfirmButton.setValue(AppGlobal.g_oLang.get()._("confirm", ""));
		this.attachChild(m_oConfirmButton);
		
		/////////////////////////////////////////////////////////////////
		// Cancel button
		m_oCancelButton = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oCancelButton, "butCancel");
		m_oCancelButton.allowClick(true);
		m_oCancelButton.setClickServerRequestBlockUI(false);
		m_oCancelButton.setValue(AppGlobal.g_oLang.get()._("cancel", ""));
		this.attachChild(m_oCancelButton);
	}

	//reset all buttons value
	public void resetButtonsDesc() {
		// Set the default as select all
		m_bSelectAll = true;
		m_oSelectAllButton.setValue(AppGlobal.g_oLang.get()._("select_all", ""));
		m_oConfirmButton.setValue(AppGlobal.g_oLang.get()._("confirm", ""));
		m_oCancelButton.setValue(AppGlobal.g_oLang.get()._("cancel", ""));
	}
	
	public void hideButton(boolean bHideSelectAll, boolean bHideConfirm) {
		m_oSelectAllButton.setVisible(!bHideSelectAll);
		m_oConfirmButton.setVisible(!bHideConfirm);
	}
	
   	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (iChildId == m_oSelectAllButton.getId()) {
			for (FrameBasketSelectPanelListener listener : listeners) {
				// Raise the event to parent
				if (m_bSelectAll)
					m_oSelectAllButton.setValue(AppGlobal.g_oLang.get()._("unselect_all"));
				else
					m_oSelectAllButton.setValue(AppGlobal.g_oLang.get()._("select_all"));
				listener.frameBasketSelectPanel_SelectAllClicked(m_bSelectAll);
				break;
			}
			m_bSelectAll = !m_bSelectAll;
			bMatchChild = true;
		} else if (iChildId == m_oCancelButton.getId()) {
			for (FrameBasketSelectPanelListener listener : listeners) {
				// Raise the event to parent
				listener.frameBasketSelectPanel_CancelClicked();
				break;
			}
			bMatchChild = true;
		} else if (iChildId == m_oConfirmButton.getId()) {
			for (FrameBasketSelectPanelListener listener : listeners) {
				// Raise the event to parent
				listener.frameBasketSelectPanel_ConfirmClicked();
				break;
			}
			bMatchChild = true;
		}

		return bMatchChild;
	}
}
