package app;

import commonui.FormDialogBox;

import core.Controller;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;

public class FormSmartStationControl extends VirtualUIForm implements FrameSmartStationControlListener {
	
	TemplateBuilder m_oTemplateBuilder;
	
	FrameSmartStationControl m_oFrameSmartStationControl;
	FrameCover m_oFrameWaitScreen;
	
	String m_sLastAction;
	
	public FormSmartStationControl(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmSmartStationControl.xml");
		
		// Background Cover Page
		m_oFrameWaitScreen = new FrameCover();
		m_oTemplateBuilder.buildFrame(m_oFrameWaitScreen, "fraCoverFrame");
		this.attachChild(m_oFrameWaitScreen);
		
		m_oFrameSmartStationControl = new FrameSmartStationControl();
		m_oTemplateBuilder.buildFrame(m_oFrameSmartStationControl, "fraSmartStationControlFrame");
		m_oFrameSmartStationControl.addListener(this);
		m_oFrameSmartStationControl.setTitle(AppGlobal.g_oLang.get()._("attention"));
		m_oFrameSmartStationControl.setVisible(false);
		this.attachChild(m_oFrameSmartStationControl);
		
		m_sLastAction = FuncSmartStation.FAILOVER_ACTION_NO_ACTION;
		
		detectNextAction();
	}

	private void detectNextAction() {
		if (!AppGlobal.g_oFuncSmartStation.getStationUdidShowingActionSelectionScreen().equals(AppGlobal.getActiveClient().getUDID())) {
			// Show wait other station select screen
			m_oFrameSmartStationControl.setVisible(false);
			m_oFrameWaitScreen.setMessage(AppGlobal.g_oLang.get()._("please_wait") + "...");
		} else
		if (m_sLastAction != AppGlobal.g_oFuncSmartStation.getFailoverActionType()) {
			// Action is changed
			
			m_sLastAction = AppGlobal.g_oFuncSmartStation.getFailoverActionType();
			
			// Update the UI
			switch (m_sLastAction) {
				case FuncSmartStation.FAILOVER_ACTION_SELECT_TO_SERVICE_MASTER:
					m_oFrameWaitScreen.setMessage("");
					m_oFrameSmartStationControl.setVisible(true);
					m_oFrameSmartStationControl.clearOption();
					
					m_oFrameSmartStationControl.setMessage(AppGlobal.g_oLang.get()._("server_master_is_out_of_service") + System.lineSeparator() + System.lineSeparator() + AppGlobal.g_oLang.get()._("please_select_the_action_type"));
					
					m_oFrameSmartStationControl.addOption(AppGlobal.g_oLang.get()._("switch_to_server_master"));
					m_oFrameSmartStationControl.addOption(AppGlobal.g_oLang.get()._("retry_to_connect_server"));
					
					break;
				case FuncSmartStation.FAILOVER_ACTION_WAIT_SERVICE_MASTER:
					m_oFrameSmartStationControl.setVisible(false);
					m_oFrameWaitScreen.setMessage(AppGlobal.g_oLang.get()._("please_wait_for_service_master_ready") + "...");
					
					break;
				case FuncSmartStation.FAILOVER_ACTION_SELECT_TO_STANDALONE:
					m_oFrameWaitScreen.setMessage("");
					m_oFrameSmartStationControl.setVisible(true);
					m_oFrameSmartStationControl.clearOption();
					
					m_oFrameSmartStationControl.setMessage(AppGlobal.g_oLang.get()._("your_machine_is_offline") + System.lineSeparator() + " " + System.lineSeparator() + AppGlobal.g_oLang.get()._("please_select_the_action_type"));
					
					m_oFrameSmartStationControl.addOption(AppGlobal.g_oLang.get()._("switch_to_standalone_mode"));
					m_oFrameSmartStationControl.addOption(AppGlobal.g_oLang.get()._("retry_to_connect_server"));
					
					break;
				case FuncSmartStation.FAILOVER_ACTION_SWITCH_TO_WORKSTATION:
					
					m_oFrameSmartStationControl.setVisible(false);
					m_oFrameWaitScreen.setMessage(AppGlobal.g_oLang.get()._("please_wait") + "...");
					
					// Role manager ask to switch to workstation
					if (!AppGlobal.g_oFuncSmartStation.switchToWorkstationRole()) {
						// Fail to switch to workstation
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(AppGlobal.g_oFuncSmartStation.getLastErrorMessage() + System.lineSeparator() + AppGlobal.g_oLang.get()._("fail_to_switch_to_work_station"));
						oFormDialogBox.show();
						oFormDialogBox = null;
					} else
						finishShow();
					
					break;
				case FuncSmartStation.FAILOVER_ACTION_NO_ACTION:
					finishShow();
					break;
			}
		}
	}
	
	@Override
	public void FrameSmartStationControl_LabelSelected(int iOptIndex) {
		switch (m_sLastAction) {
			case FuncSmartStation.FAILOVER_ACTION_SELECT_TO_SERVICE_MASTER:
				
				if (iOptIndex == 0) {
					// User select to switch to service master
					if (!AppGlobal.g_oFuncSmartStation.switchToServiceMasterRole()) {
						// Fail to switch to service master
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(AppGlobal.g_oFuncSmartStation.getLastErrorMessage() + System.lineSeparator() + AppGlobal.g_oLang.get()._("fail_to_switch_to_service_master"));
						oFormDialogBox.show();
						oFormDialogBox = null;
					} else
						finishShow();
				}
				
				break;
			case FuncSmartStation.FAILOVER_ACTION_SELECT_TO_STANDALONE:
				if (iOptIndex == 0) {
					// User select to switch to standalone mode
					if (!AppGlobal.g_oFuncSmartStation.switchToStandaloneRole()) {
						// Fail to switch to service master
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(AppGlobal.g_oFuncSmartStation.getLastErrorMessage() + System.lineSeparator() + AppGlobal.g_oLang.get()._("fail_to_switch_to_standalone_mode"));
						oFormDialogBox.show();
						oFormDialogBox = null;
					} else
						finishShow();
				}
				
				break;
		}
	}

	@Override
	public void FrameSmartStationControl_DetectRoleManagerNextAction() {
		detectNextAction();
	}
}
