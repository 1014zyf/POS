package commonui;

import java.util.ArrayList;

import app.AppGlobal;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

public class FrameAlertMessageBox extends VirtualUIFrame{
	public interface FrameAlertMessageBoxListener{
		void frameAlertMessageBox_click();
	}
	
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUIFrame m_oFrameAlertMessageBox;

	private VirtualUILabel m_oLabelMessage;
	private FrameTitleHeader m_oTitleHeader;
	private VirtualUIButton m_oButton;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameAlertMessageBoxListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameAlertMessageBoxListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameAlertMessageBoxListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FrameAlertMessageBox() {
		listeners = new ArrayList<FrameAlertMessageBoxListener>();
		m_oTemplateBuilder = new TemplateBuilder();
	}
	public void init(boolean bHaveBtn) {
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmDialogBox.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameAlertMessageBox = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameAlertMessageBox, "fraDialogBox");
		this.attachChild(m_oFrameAlertMessageBox);
		
		m_oTemplateBuilder.loadTemplate("fraDialogBox.xml");
		
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(false);
		m_oFrameAlertMessageBox.attachChild(m_oTitleHeader);
		
		m_oLabelMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMessage, "lblMessage");
		m_oLabelMessage.setTop(m_oTitleHeader.getHeight());
		m_oLabelMessage.setHeight(m_oFrameAlertMessageBox.getHeight() - m_oTitleHeader.getHeight());
		m_oFrameAlertMessageBox.attachChild(m_oLabelMessage);
		
		if(bHaveBtn) {
			m_oButton = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButton, "singleBtn");
			m_oButton.setTop(m_oFrameAlertMessageBox.getHeight() - m_oButton.getHeight() - 20);
			m_oButton.setLeft((m_oFrameAlertMessageBox.getWidth()/2) - (m_oButton.getWidth()/2));
			m_oButton.setClickServerRequestBlockUI(false);
			m_oFrameAlertMessageBox.attachChild(m_oButton);
			
			// Resize the message label
			m_oLabelMessage.setHeight(m_oFrameAlertMessageBox.getHeight() - m_oTitleHeader.getHeight() - (m_oButton.getHeight() + 20));
		}
	}
	
	public void setButtonValue(String sValue) {
		if(m_oButton != null)
			this.m_oButton.setValue(sValue);
	}
	
	public void setTitle(String sTitle) {
		this.m_oTitleHeader.setTitle(sTitle);
	}
	
	public void setMessage(String sMessage) {
		this.m_oLabelMessage.setValue(sMessage);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if(m_oButton != null && m_oButton.getId() == iChildId) {
			for (FrameAlertMessageBoxListener listener : listeners) {
				bMatchChild = true;
				listener.frameAlertMessageBox_click();
				break;
			}
		}
		return bMatchChild;
	}

}
