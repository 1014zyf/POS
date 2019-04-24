package commonui;

import java.util.ArrayList;

import app.AppGlobal;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUICodeReader;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameCodeReaderPanelListener {
	void frameCodeReaderPanel_clickCancel();
	void frameCodeReaderPanel_clickOK();
	void frameCodeReaderPanel_clickReset();
	void frameCodeReaderPanelListener_scanQrCode(String sQrCodeValue);
}

public class FrameCodeReaderPanel extends VirtualUIFrame implements FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;

	private FrameTitleHeader m_oTitleHeader;
	private VirtualUILabel m_oLabelMessage;
	private VirtualUICodeReader m_oCodeReader;
	private VirtualUIButton m_oButtonReset;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameCodeReaderPanelListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameCodeReaderPanelListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameCodeReaderPanelListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public void init() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameCodeReaderPanelListener>();

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraCodeReaderPanel.xml");

		// Title Header
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		m_oTitleHeader.addListener(this);
		this.attachChild(m_oTitleHeader);
		
		// InputBox Message
		m_oLabelMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMessage, "lblMessage");
		this.attachChild(m_oLabelMessage);
		
		// qr code scanner
		resetCodeReader();
		
		// Create Reset Button
		m_oButtonReset = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonReset, "btnReset");
		m_oButtonReset.setValue(AppGlobal.g_oLang.get()._("reset"));
		m_oButtonReset.addClickServerRequestSubmitElement(this);
		this.attachChild(m_oButtonReset);
	}
	
	public void setTitle(String sTitle){
		m_oTitleHeader.setTitle(sTitle);
	}
	
	public void setMessage(String sMessage){
		m_oLabelMessage.setValue(sMessage);
	}
	
	public String getInputValue(){
		return m_oCodeReader.getValue().replace("\r", "").replace("\n", "");
	}
	
	public void resetCodeReader(){
		//remove code reader
		if (m_oCodeReader != null) 
			m_oCodeReader.removeMyself();
		
		//new code reader
		m_oCodeReader = new VirtualUICodeReader();
		m_oTemplateBuilder.buildCodeReader(m_oCodeReader, "codeReader");
		m_oCodeReader.allowValueChanged(true);
		m_oCodeReader.allowClick(true);
		m_oCodeReader.addValueChangedServerRequestSubmitElement(m_oCodeReader);
		m_oCodeReader.setVisible(true);
		this.attachChild(m_oCodeReader);
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (iChildId == m_oButtonReset.getId()) {
			for (FrameCodeReaderPanelListener listener : listeners) {
				// Raise the event to parent
				listener.frameCodeReaderPanel_clickReset();
				break;
			}

			bMatchChild = true;
		}
		
		return bMatchChild;
	}
	
	@Override
	public boolean valueChanged(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		// Get the value by QR code
		if (m_oCodeReader != null && iChildId == m_oCodeReader.getId()) {
			String sQrCodeValue = m_oCodeReader.getValue().replace("\r", "").replace("\n", "");
			for (FrameCodeReaderPanelListener listener : listeners) {
				// Raise the event to parent
				listener.frameCodeReaderPanelListener_scanQrCode(sQrCodeValue);
			}
			bMatchChild = true;
		}
		
		return bMatchChild;
	}

	@Override
	public void FrameTitleHeader_close() {
		for (FrameCodeReaderPanelListener listener : listeners) {
			// Raise the event to parent
			listener.frameCodeReaderPanel_clickCancel();
		}
	}
}
