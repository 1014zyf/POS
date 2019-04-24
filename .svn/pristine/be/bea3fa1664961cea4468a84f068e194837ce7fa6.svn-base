package commonui;

import java.util.ArrayList;

import app.AppGlobal;
import core.Controller;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormCodeReaderPanel extends VirtualUIForm implements FrameCodeReaderPanelListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameCodeReaderPanel m_oFrameCodeReaderPanel;
	private VirtualUIFrame m_oFrameCover;
	private String m_sQrCodeValue;
	private boolean m_bUserCancel;
	private boolean m_bKeepAliveAfterScannerValueChanged;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FormCodeReaderPanelListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FormCodeReaderPanelListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FormCodeReaderPanelListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FormCodeReaderPanel(Controller oParentController) {
		super(oParentController);
		
		m_sQrCodeValue = "";
		m_bUserCancel = false;
		m_bKeepAliveAfterScannerValueChanged = false;
	}
	
	public boolean init() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FormCodeReaderPanelListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmCodeReaderPanel.xml");
		m_oFrameCodeReaderPanel = new FrameCodeReaderPanel();
		m_oTemplateBuilder.buildFrame(m_oFrameCodeReaderPanel, "fraCodeReaderPanel");
		m_oFrameCodeReaderPanel.init();
		
		// Background Cover Page
		m_oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCover, "fraCoverFrame");
		this.attachChild(m_oFrameCover);
		
		// Add listener
		m_oFrameCodeReaderPanel.addListener(this);
		m_oFrameCodeReaderPanel.setVisible(true);		// Show this frame during first initial
		m_oFrameCover.attachChild(m_oFrameCodeReaderPanel);
		
		return true;
	}
	
	public String getQrCodeValue() {
		return m_sQrCodeValue;
	}
	
	public void setTitle(String sTitle){
		m_oFrameCodeReaderPanel.setTitle(sTitle);
	}
	
	public void setMessage(String sMessage){
		m_oFrameCodeReaderPanel.setMessage(sMessage);
	}
	
	public boolean isUserCancel() {
		return m_bUserCancel;
	}
	
	public void setKeepAliveAfterScannerValueChanged(boolean bAlive){
		m_bKeepAliveAfterScannerValueChanged = bAlive;
	}

	@Override
	public void frameCodeReaderPanel_clickCancel() {
		m_sQrCodeValue = null;
		m_bUserCancel = true;
		
		// Finish showing this form
		this.finishShow();
		m_oFrameCover.setVisible(false);
	}

	@Override
	public void frameCodeReaderPanel_clickOK() {
		// Get the value by QR code
		if (!m_sQrCodeValue.isEmpty() && m_sQrCodeValue != null) {
			for (FormCodeReaderPanelListener listener : listeners) {
				// Raise the event to parent
				listener.formCodeReaderPanel_scanQrCode(m_sQrCodeValue);
			}
			// Finish showing this form
			this.finishShow();
		} else {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("empty_qr_code"));
			oFormDialogBox.show();
			return;
		}
	}

	@Override
	public void frameCodeReaderPanel_clickReset() {
		m_oFrameCodeReaderPanel.resetCodeReader();
	}

	@Override
	public void frameCodeReaderPanelListener_scanQrCode(String sQrCodeValue) {
		// Get QR code value
		m_sQrCodeValue = sQrCodeValue;
		
		if(!m_bKeepAliveAfterScannerValueChanged)
			frameCodeReaderPanel_clickOK();
		
	}
	
}
