package commonui;

import java.util.ArrayList;
import java.util.List;

import app.AppGlobal;
import commonui.FrameTitleHeader;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;

import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;
import commonui.FrameTitleHeaderListener;

interface FrameInputBoxListListener {
	void FrameInputBoxList_clickOK();
	void FrameInputBoxList_clickCancel();
}

public class FrameInputBoxList extends VirtualUIFrame implements FrameTitleHeaderListener{
	
	private TemplateBuilder m_oTemplateBuilder;
	private FrameTitleHeader m_oTitleHeader;
	private VirtualUITextbox m_oInputTxtbox;
	private List<VirtualUILabel> m_oLblMessagesList;
	private List<VirtualUITextbox> m_oTxtboxInputsList;
	private VirtualUIButton m_oButtonOK;
	private VirtualUIButton m_oButtonClear;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameInputBoxListListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameInputBoxListListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameInputBoxListListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public void init(int iInputNum) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameInputBoxListListener>();
		m_oLblMessagesList = new ArrayList<VirtualUILabel>();
		m_oTxtboxInputsList = new ArrayList<VirtualUITextbox>();
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraInputBoxList.xml");
		
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		m_oTitleHeader.addListener(this);
		this.attachChild(m_oTitleHeader);
		
		
		// InputBox Message
		VirtualUILabel oLblMsg = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblMsg, "lblMessage");
		
		oLblMsg.setLeft(25);
		oLblMsg.setTop(100);
		m_oLblMessagesList.add(oLblMsg);
		this.attachChild(oLblMsg);
		
		// Create InputBox textbox
		VirtualUITextbox oTxtboxInput = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(oTxtboxInput, "txtbox");
		oTxtboxInput.setLeft(200);
		oTxtboxInput.setTop(oLblMsg.getTop());
		oTxtboxInput.setFocusWhenShow(true);
		m_oTxtboxInputsList.add(oTxtboxInput);
		this.attachChild(oTxtboxInput);
		m_oInputTxtbox = oTxtboxInput;
		
		for(int i = 1; i < iInputNum; i++) {
			// InputBox Message
			VirtualUILabel oTempLbl = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oTempLbl, "lblMessage");
			if(i % 2 != 0) {
				oTempLbl.setLeft(520);
				oTempLbl.setTop(m_oLblMessagesList.get(m_oLblMessagesList.size() - 1).getTop());
			} else {
				oTempLbl.setLeft(25);
				oTempLbl.setTop(oTxtboxInput.getTop() + 80 + ((i - 2) / 2) * (80));
			}
			m_oLblMessagesList.add(oTempLbl);
			this.attachChild(oTempLbl);
			
			// Create InputBox textbox
			VirtualUITextbox oTempTxtbox = new VirtualUITextbox();
			m_oTemplateBuilder.buildTextbox(oTempTxtbox, "txtbox");
			if(i % 2 != 0) {
				oTempTxtbox.setLeft(700);
				oTempTxtbox.setTop(m_oTxtboxInputsList.get(m_oTxtboxInputsList.size() - 1).getTop());
			} else {
				oTempTxtbox.setLeft(200);
				oTempTxtbox.setTop(oTempLbl.getTop());
			}
			oTempTxtbox.setFocusWhenShow(false);
			m_oTxtboxInputsList.add(oTempTxtbox);
			this.attachChild(oTempTxtbox);
		}
		
		// Clear button
		m_oButtonClear = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonClear, "btnClear");
		m_oButtonClear.setValue(AppGlobal.g_oLang.get()._("clear"));
		m_oButtonClear.addClickServerRequestSubmitElement(this);
		m_oButtonClear.setVisible(true);
		this.attachChild(m_oButtonClear);
		
		// Create OK Button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "btnOK");
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("ok"));
		m_oButtonOK.addClickServerRequestSubmitElement(this);
		this.attachChild(m_oButtonOK);
		
		this.setHeight(m_oButtonOK.getTop() + m_oButtonOK.getHeight() + 27);
	}
	
	public void setTitle(String sTitle){
		m_oTitleHeader.setTitle(sTitle);
	}
	
	public String getInputValue(int iIndex) {
		try {
			VirtualUITextbox oInputbox = m_oTxtboxInputsList.get(iIndex);
			return oInputbox.getValue();
		}
		catch(Exception e) {
			return "";
		}
	}
	
	public void setInputValue(String[] InputValue) {
		if(InputValue.length > m_oTxtboxInputsList.size())
			return;
		for (int i = 0; i < InputValue.length; i++) {
			VirtualUITextbox oInputbox = m_oTxtboxInputsList.get(i);
			oInputbox.setValue(InputValue[i]);
		}
	}
	
	public int getInputTxtboxCount() {
		try {
			return m_oTxtboxInputsList.size();
		}
		catch(Exception e) {
			return 0;
		}
	}
	
	public void setMessages(List<String> oMessagesList) {
		int i = 0;
		for(VirtualUILabel oLblMessage:m_oLblMessagesList) {
			oLblMessage.setValue(oMessagesList.get(i));
			i++;
		}
	}
	
	public void setTxtBoxFocus(int iIndex, boolean bFocus) {
		VirtualUITextbox oFocusInputbox = m_oTxtboxInputsList.get(iIndex);
		oFocusInputbox.setFocusWhenShow(bFocus);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (iChildId == m_oButtonOK.getId()) {
			for (FrameInputBoxListListener listener : listeners) {
				// Raise the event to parent
				listener.FrameInputBoxList_clickOK();
			}
			bMatchChild = true;
		}
		else if (iChildId == m_oButtonClear.getId()) {
			for (VirtualUITextbox oTextbox:m_oTxtboxInputsList) {
				oTextbox.setValue("");
			}
			m_oInputTxtbox.setFocus();
			bMatchChild = true;
		}
		return bMatchChild;
	}
	
	@Override
	public void FrameTitleHeader_close() {
		// TODO Auto-generated method stub
		for (FrameInputBoxListListener listener : listeners) {
			// Raise the event to parent
			listener.FrameInputBoxList_clickCancel();
		}
	}

}

