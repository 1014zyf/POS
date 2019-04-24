package app;

import java.util.ArrayList;

import commonui.FormConfirmBox;
import core.Controller;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;


/** interface for the listeners/observers callback method */
interface FormConfirmOrderDialogListener {
	void formConfirmOrderDialog_Timeout();
}

public class FormConfirmOrderDialog extends VirtualUIForm implements FrameConfirmOrderDialogListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameConfirmOrderDialog m_oFrameConfirmOrderDialog;
	
	private boolean m_bUserCancel;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FormConfirmOrderDialogListener> listeners;
	
	private FuncCheck m_oFuncCheck;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FormConfirmOrderDialogListener listener) {
		listeners.add(listener);
	}
	
	public FormConfirmOrderDialog(Controller oParentController, FuncCheck o_FuncCheck, int iCashierTimer) {
		super(oParentController);
		m_oTemplateBuilder = new TemplateBuilder();
		m_bUserCancel = false;
		
		listeners = new ArrayList<FormConfirmOrderDialogListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmConfirmOrderDialog.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameConfirmOrderDialog = new FrameConfirmOrderDialog(o_FuncCheck);
		m_oTemplateBuilder.buildFrame(m_oFrameConfirmOrderDialog, "fraConfirmOrderDialog");
		m_oFrameConfirmOrderDialog.addListener(this);
		this.attachChild(m_oFrameConfirmOrderDialog);
		
		if (iCashierTimer > 0){
			m_oFrameConfirmOrderDialog.setConfirmOrderDialogTimeout(iCashierTimer);
			m_oFrameConfirmOrderDialog.setConfirmOrderDialogTimeoutTimer(true);
		}
		m_oFuncCheck = o_FuncCheck;
	}
	
	public boolean isUserCancel() {
		return m_bUserCancel;
	}
	
	@Override
	public void frameConfirmOrderDialog_clickClose() {
		this.finishShow();	
	}
	
	@Override
	public void frameConfirmOrderDialog_clickBack(String sTable, String sTableExtension) {
		m_bUserCancel = true;
		this.finishShow();
	}
	
	@Override
	public void frameConfirmOrderDialog_timeout() {
		for (FormConfirmOrderDialogListener listener : listeners) {
			m_oFrameConfirmOrderDialog.setConfirmOrderDialogTimeoutTimer(false);
			
			if (AppGlobal.g_oFuncStation.get().getOrderingTimeoutOption() == FuncStation.ORDERING_TIMEOUT_OPTION_QUIT_CHECK_DIRECTLY
					&& m_oFuncCheck != null) {
				listener.formConfirmOrderDialog_Timeout();
				this.finishShow();
			} else {
				// Show the option
				FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("continue_process"),
						AppGlobal.g_oLang.get()._("new_order"), this);
				oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
				oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("order_time_out_is_reached"));
				oFormConfirmBox.setTimeout(30 * 1000);
				oFormConfirmBox.setTimeoutChecking(true);
				oFormConfirmBox.show();
				
				oFormConfirmBox.setTimeoutChecking(false);
				if (oFormConfirmBox.isOKClicked()) {
					// Continue order and restart the ordering timeout
					m_oFrameConfirmOrderDialog.setConfirmOrderDialogTimeoutTimer(true);
				} else {
					listener.formConfirmOrderDialog_Timeout();
					this.finishShow();
				}
			}
		}
	}
}
