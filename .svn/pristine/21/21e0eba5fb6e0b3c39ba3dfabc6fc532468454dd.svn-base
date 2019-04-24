package app;

import java.util.ArrayList;

import org.json.JSONObject;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

interface FrameSelfOrderKioskListener {
	void frameSelfOrderKiosk_newOrder();
	void frameSelfOrderKiosk_quitPayResult(int iQuitMode);
	void frameSelfOrderKiosk_changeLanguage();
}

public class FrameSelfOrderKiosk extends VirtualUIFrame{
	TemplateBuilder m_oTemplateBuilder;
	VirtualUIImage m_oImageBackground;
	VirtualUIImage m_oImageNewOrderButton;
	VirtualUIImage m_oImageChgLangButton;
	VirtualUIFrame m_oFrameWholeCover;
	VirtualUIFrame m_oFramePanelCover;
	VirtualUIFrame m_oFrameLeftHeaderCover;
	VirtualUIFrame m_oFrameFuncPanelCover;
	VirtualUIFrame m_oFrameFuncPanelCoverForDirectPay;
	VirtualUILabel m_oLabelQuitPayResult;
	VirtualUILabel m_oLabelQuitPayResultForDirectPay;
	private int m_iQuitMode;
	private int m_iTimeForSwitchFrame;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameSelfOrderKioskListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameSelfOrderKioskListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameSelfOrderKioskListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	// Constructor
	public FrameSelfOrderKiosk(String sBackgroundImage) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameSelfOrderKioskListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraSelfOrderKiosk.xml");
		
		// Whole cover frame
		m_oFrameWholeCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameWholeCover, "fraWholeCover");
		this.attachChild(m_oFrameWholeCover);

		m_oImageBackground = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageBackground, "imgBackground");
		if (sBackgroundImage != null && !sBackgroundImage.isEmpty()) {
			m_oImageBackground.setSource(sBackgroundImage);
			m_oImageBackground.allowClick(true);
		} else
			m_oImageBackground.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/backgrounds/welcome_page_bg.png");
		m_oImageBackground.setEnabled(true);
		m_oImageBackground.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_TO_FIT);
		m_oFrameWholeCover.attachChild(m_oImageBackground);
		
		m_oImageNewOrderButton = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageNewOrderButton, "imgNewOrderButton");
		m_oImageNewOrderButton.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/self_new_order.png");
		m_oImageNewOrderButton.setEnabled(true);
		m_oImageNewOrderButton.allowClick(true);
		m_oFrameWholeCover.attachChild(m_oImageNewOrderButton);
		
		m_oImageChgLangButton = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageChgLangButton, "imgChgLangButton");
		m_oImageChgLangButton.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/change_language.png");
		m_oImageChgLangButton.setEnabled(true);
		m_oImageChgLangButton.allowClick(true);
		m_oFrameWholeCover.attachChild(m_oImageChgLangButton);
		
		// Panel cover frame
		m_oFramePanelCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePanelCover, "fraPanelCover");
		m_oFramePanelCover.allowClick(true);
		this.attachChild(m_oFramePanelCover);
		
		// Left header cover frame
		m_oFrameLeftHeaderCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftHeaderCover, "fraLeftHeaderCover");
		this.attachChild(m_oFrameLeftHeaderCover);
		
		// Func panel cover frame
		m_oFrameFuncPanelCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameFuncPanelCover, "fraFuncPanelCover");
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oFrameFuncPanelCover.allowClick(true);
		this.attachChild(m_oFrameFuncPanelCover);
		
		// Func panel cover frame for direct pay
		m_oFrameFuncPanelCoverForDirectPay = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameFuncPanelCoverForDirectPay, "fraFuncPanelCoverForDirectPay");
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oFrameFuncPanelCoverForDirectPay.allowClick(true);
		this.attachChild(m_oFrameFuncPanelCoverForDirectPay);
		
		// Quit pay result result
		m_oLabelQuitPayResult = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelQuitPayResult, "lblQuitPayResult");
		m_oLabelQuitPayResult.setValue(AppGlobal.g_oLang.get()._("click_screen_to_quit", ""));
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oFrameFuncPanelCover.attachChild(m_oLabelQuitPayResult);
			
			m_oLabelQuitPayResultForDirectPay = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelQuitPayResultForDirectPay, "lblQuitPayResult");
			m_oLabelQuitPayResultForDirectPay.setValue(AppGlobal.g_oLang.get()._("click_screen_to_quit", ""));
			m_oFrameFuncPanelCoverForDirectPay.attachChild(m_oLabelQuitPayResultForDirectPay);
		}else
			m_oFramePanelCover.attachChild(m_oLabelQuitPayResult);
		
		String sPaymentCompletionMessage = "";
		JSONObject oPaymentProcessSetting = AppGlobal.g_oFuncStation.get().getPaymentProcessSetting();
		if (oPaymentProcessSetting != null && oPaymentProcessSetting.has("payment_completion_message"))
			sPaymentCompletionMessage = oPaymentProcessSetting.optString("payment_completion_message", "");
		
		if (!sPaymentCompletionMessage.isEmpty()){
			// By using timer to quick close the default payment result page
			m_iTimeForSwitchFrame = 0;
		} else {
			// Set the time for switch from pay result to new check
			m_iTimeForSwitchFrame = 3000;
			int iAuotSwitch  = AppGlobal.g_oFuncStation.get().getPayResultAutoSwitchTimeControl();
			if(iAuotSwitch > -1) 
				m_iTimeForSwitchFrame = 1000 * iAuotSwitch;
		}
		
		addFinishShowTimer();
		
		addDeviceMgrKeepAliveTimer();
	}

	public void showNewOrderButton() {
		m_iQuitMode = 0;
		m_oFrameWholeCover.setVisible(true);
		m_oFramePanelCover.setVisible(false);
		m_oFrameFuncPanelCover.setVisible(false);
		m_oFrameLeftHeaderCover.setVisible(false);
		m_oFrameFuncPanelCoverForDirectPay.setVisible(false);
		
		// Start device manager keep alive
		startDeviceMgrKeepAliveTimer(true);
	}
	
	public void showQuitPayResultButton(int iQuitMode) {
		m_iQuitMode = iQuitMode;
		m_oFrameWholeCover.setVisible(false);
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oFramePanelCover.setVisible(false);
		else
			m_oFramePanelCover.setVisible(true);
		if (m_iQuitMode == 1 || m_iQuitMode == 3) {
			m_oFrameFuncPanelCover.setVisible(true);
			m_oFrameLeftHeaderCover.setVisible(true);
			m_oFrameFuncPanelCoverForDirectPay.setVisible(false);
		} else {
			m_oFrameFuncPanelCover.setVisible(false);
			m_oFrameLeftHeaderCover.setVisible(false);
			m_oFrameFuncPanelCoverForDirectPay.setVisible(true);
		}
		startFinishShowTimer(true);
	}

	public void skipShowQuitPayResultPage(int iQuitMode) {
		m_iQuitMode = iQuitMode;
		startFinishShowTimer(true);
	}
	
	// Create auto quit pay result timer
	private void addFinishShowTimer() {
		this.addTimer("self_order_quit_pay_result", m_iTimeForSwitchFrame, false, "self_order_quit_pay_result", true, false, m_oFramePanelCover);
	}

	// Start auto quit pay result
	public void startFinishShowTimer(boolean bStart) {
		this.controlTimer("self_order_quit_pay_result", bStart);
	}

	// Create device manager keep alive timer
	private void addDeviceMgrKeepAliveTimer() {
		this.addTimer("device_mgr_keep_alive", 30000, true, "device_mgr_keep_alive", false, true, null);
	}

	// Start device manager keep alive
	private void startDeviceMgrKeepAliveTimer(boolean bStart) {
		this.controlTimer("device_mgr_keep_alive", bStart);
	}
	
	// Reset the language of description
	public void resetButtonsDesc() {
		m_oLabelQuitPayResult.setValue(AppGlobal.g_oLang.get()._("click_screen_to_quit", ""));
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oLabelQuitPayResultForDirectPay.setValue(AppGlobal.g_oLang.get()._("click_screen_to_quit", ""));
	}

	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if (iId == super.getIDForPosting().hashCode()) {
			// Set the last client socket ID
			AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
			
			if (sNote.equals("device_mgr_keep_alive")){
				AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestValue(String.format("\001win_fcn_map\002%s\004", "ack"));
				AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(0);
				AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestDelay(0);
			} else {
				// Ask drawing basket
				for (FrameSelfOrderKioskListener listener : listeners) {
					// Raise the event to parent
					listener.frameSelfOrderKiosk_quitPayResult(m_iQuitMode);
				}
			}
			
   			// *** MUST disable this one time timer to prevent call after client reconnect
			startFinishShowTimer(false);
   			
			// Send the UI packet to client and the thread is finished
			super.getParentForm().finishUI(true);
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		// Find the clicked button
		if (m_oImageNewOrderButton.getId() == iChildId || m_oImageBackground.getId() == iChildId) {
			for (FrameSelfOrderKioskListener listener : listeners) {
				// Raise the event to parent
				listener.frameSelfOrderKiosk_newOrder();
				
				// Stop device manager keep alive
				startDeviceMgrKeepAliveTimer(false);
			}
			bMatchChild = true;
		} else if (m_oImageChgLangButton.getId() == iChildId) {
			for (FrameSelfOrderKioskListener listener : listeners) {
				// Raise the event to parent
				startFinishShowTimer(false);
				listener.frameSelfOrderKiosk_changeLanguage();
			}
			bMatchChild = true;
		} else if (m_oFramePanelCover.getId() == iChildId) {
			for (FrameSelfOrderKioskListener listener : listeners) {
				// Raise the event to parent
				startFinishShowTimer(false);
				listener.frameSelfOrderKiosk_quitPayResult(m_iQuitMode);
			}
			bMatchChild = true;
		} else if (m_oFrameFuncPanelCover.getId() == iChildId) {
			for (FrameSelfOrderKioskListener listener : listeners) {
				// Raise the event to parent
				startFinishShowTimer(false);
				listener.frameSelfOrderKiosk_quitPayResult(m_iQuitMode);
			}
			bMatchChild = true;
		} else if (m_oFrameFuncPanelCoverForDirectPay.getId() == iChildId) {
			for (FrameSelfOrderKioskListener listener : listeners) {
				// Raise the event to parent
				startFinishShowTimer(false);
				listener.frameSelfOrderKiosk_quitPayResult(m_iQuitMode);
			}
			bMatchChild = true;
		}

		return bMatchChild;
	}
	
	public void resizeNewOrderPage(int iStationBarInfoHeight) {
		m_oFrameWholeCover.setTop(0);
		m_oFrameWholeCover.setHeight(m_oFrameWholeCover.getHeight() + iStationBarInfoHeight);
		m_oImageBackground.setHeight(m_oFrameWholeCover.getHeight());
		m_oImageNewOrderButton.setHeight(m_oImageNewOrderButton.getHeight() + iStationBarInfoHeight);
		m_oImageChgLangButton.setTop(m_oImageChgLangButton.getTop() + iStationBarInfoHeight);
	}
	
}
