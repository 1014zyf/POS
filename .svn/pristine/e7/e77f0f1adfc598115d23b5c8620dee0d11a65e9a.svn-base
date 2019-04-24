package commonui;

import java.util.ArrayList;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIKeyboardReader;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameDialogBoxListener {
	void FrameDialogBox_click(String sResult);
}

public class FrameDialogBox extends VirtualUIFrame {

	TemplateBuilder m_oTemplateBuilder;

	private VirtualUILabel m_oLabelMessage;
	private ArrayList<VirtualUIButton> m_aButtonArray;
	private float m_fDelayTime;
	private VirtualUIKeyboardReader m_oKeyboardReaderForOK;

	private FrameTitleHeader m_oTitleHeader;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameDialogBoxListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameDialogBoxListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameDialogBoxListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameDialogBox() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameDialogBoxListener>();
		m_aButtonArray = new ArrayList<VirtualUIButton>();
		m_fDelayTime = 0;
	}

	public void init() {
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraDialogBox.xml");

		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(false);
		this.attachChild(m_oTitleHeader);

		// DialogBox Message
		m_oLabelMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMessage, "lblMessage");
		m_oLabelMessage.setTop(m_oTitleHeader.getHeight());
		m_oLabelMessage.setHeight(this.getHeight() - m_oTitleHeader.getHeight());
		this.attachChild(m_oLabelMessage);

		// Keyboard
		m_oKeyboardReaderForOK = new VirtualUIKeyboardReader();
		m_oKeyboardReaderForOK.addKeyboardKeyCode(13);
		this.attachChild(m_oKeyboardReaderForOK);
	}

	public void addSingleButton(String sBtnValue) {
		VirtualUIButton oNewBtn = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(oNewBtn, "singleBtn");
		oNewBtn.setValue(sBtnValue);
		oNewBtn.setClickServerRequestBlockUI(false);
		this.attachChild(oNewBtn);

		// Resize the message label
		m_oLabelMessage.setHeight(oNewBtn.getTop() - m_oTitleHeader.getHeight());
		m_aButtonArray.add(oNewBtn);
	}

	public void setTitle(String sTitle) {
		m_oTitleHeader.setTitle(sTitle);
	}

	public void setMessage(String sMessage) {
		m_oLabelMessage.setValue(sMessage);
	}

	public float getDelayTime() {
		return m_fDelayTime;
	}

	public void setDelayTime(float fDelayTime) {
		m_fDelayTime = fDelayTime;
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		for (FrameDialogBoxListener listener : listeners) {
			// Find the clicked button
			for (VirtualUIButton oBtn : m_aButtonArray) {
				if (oBtn.getId() == iChildId) {
					// Raise the event to parent
					listener.FrameDialogBox_click(oBtn.getValue());
					bMatchChild = true;
					break;
				}
			}
		}
		return bMatchChild;
	}

	@Override
	public boolean keyboard(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (iChildId == m_oKeyboardReaderForOK.getId()) {
			for (FrameDialogBoxListener listener : listeners) {
				// Raise the event to parent
				listener.FrameDialogBox_click("");
			}
			bMatchChild = true;
		}
		return bMatchChild;
	}
}
