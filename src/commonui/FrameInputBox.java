package commonui;

import java.util.ArrayList;
import java.util.List;

import app.AppGlobal;
import app.ClsActiveClient;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUIKeyboardReader;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;

/** interface for the listeners/observers callback method */
interface FrameInputBoxListener {
	void FrameInputBox_clickOK();
	void FrameInputBox_clickCancel();
	void FrameInputBox_clickQrScan();
	void FrameInputBox_clickGetInformation();
	void FrameInputBoxListener_swipeCard(String sSwipeCardValue);
}

public class FrameInputBox extends VirtualUIFrame implements FrameNumberPadListener, FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;

	// private VirtualUILabel m_oLabelTitle;
	private VirtualUILabel m_oLabelMessage;
	private VirtualUITextbox m_oInputTxtbox;
	private List<VirtualUILabel> m_oLblMessagesList;
	private List<VirtualUITextbox> m_oTxtboxInputsList;
	private VirtualUIButton m_oButtonOK;
	private VirtualUIButton m_oButtonCancel;
	private VirtualUIButton m_oButtonQR;
	private VirtualUIButton m_oButtonGetInformation;
	
	private FrameNumberPad m_oFrameNumberPad;
	private VirtualUIKeyboardReader m_oKeyboardReaderForOK;
	private VirtualUIFrame m_oFrameImageFrame;
	private VirtualUIImage m_oImage;
	private VirtualUILabel m_oLabelDesc;
	private VirtualUILabel m_oLabelDesc1;
	
	private VirtualUIFrame m_oFrameInputImage;
	private VirtualUIImage m_oImageInput;
	
	private FrameTitleHeader m_oTitleHeader;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameInputBoxListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameInputBoxListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameInputBoxListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public void init(boolean bIsLarge) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameInputBoxListener>();
		// Load child elements from template
		// Load form from template file
		if(!bIsLarge)
		m_oTemplateBuilder.loadTemplate("fraInputBox.xml");
		else
			m_oTemplateBuilder.loadTemplate("fraInputBoxLarge.xml");
		
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.addListener(this);
		m_oTitleHeader.init(bIsLarge);
		this.attachChild(m_oTitleHeader);
		
		// Number pad
		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.setCancelAndEnterToLeftAndRigth(true);
		m_oFrameNumberPad.setNumPadLeft(400);
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.addListener(this);
		m_oFrameNumberPad.setVisible(false);
		this.attachChild(m_oFrameNumberPad);
		
		// InputBox Message
		m_oLabelMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMessage, "lblMessage");
		m_oLabelMessage.setWidth(this.getWidth() - ((m_oLabelMessage.getLeft() + this.getStroke()) * 2));
		this.attachChild(m_oLabelMessage);
		
		// Create InputBox textbox
		m_oInputTxtbox = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oInputTxtbox, "txtbox");
		m_oInputTxtbox.setTop(m_oLabelMessage.getTop() + m_oLabelMessage.getHeight());
		m_oInputTxtbox.setFocusWhenShow(true);
		this.attachChild(m_oInputTxtbox);
		
		// Create OK Button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "btnOK");
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("ok"));
		m_oButtonOK.addClickServerRequestSubmitElement(this);
		this.attachChild(m_oButtonOK);
		
		// Create Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "btnCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		m_oButtonCancel.setClickServerRequestBlockUI(false);
		this.attachChild(m_oButtonCancel);
		
		// Create Award Button
		m_oButtonGetInformation = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonGetInformation, "btnGetInformation");
		m_oButtonGetInformation.setVisible(false);
		this.attachChild(m_oButtonGetInformation);
		
		// Keyboard
		m_oKeyboardReaderForOK = new VirtualUIKeyboardReader();
		m_oKeyboardReaderForOK.addKeyboardKeyCode(13);
		m_oKeyboardReaderForOK.addKeyboardServerRequestSubmitElement(m_oInputTxtbox);
		this.attachChild(m_oKeyboardReaderForOK);
		
		// Image Frame
		m_oFrameImageFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameImageFrame, "fraImageFrame");
		m_oFrameImageFrame.setVisible(false);
		this.attachChild(m_oFrameImageFrame);
		
		// Image
		m_oImage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImage, "imgPaymentImage");
		m_oFrameImageFrame.attachChild(m_oImage);
		
		// Remaining Amount 
		m_oLabelDesc1 = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDesc1, "lblRemainingBalance");
		m_oLabelDesc1.setVisible(false);
		this.attachChild(m_oLabelDesc1);
		
		// Description
		m_oLabelDesc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDesc, "lblDesc");
		m_oLabelDesc.setValue(AppGlobal.g_oLang.get()._("please_follow_the_instructions") + System.lineSeparator() + AppGlobal.g_oLang.get()._("please_bring_the_ticket_along_to_the_window_to_get_the_meal_after_finish_the_payment"));
		m_oLabelDesc.setVisible(false);
		this.attachChild(m_oLabelDesc);
		
		// Input Image Frame
		int padding = m_oInputTxtbox.getHeight()/2;
		m_oFrameInputImage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameInputImage, "fraInputImageFrame");
		m_oFrameInputImage.setTop(m_oInputTxtbox.getTop() + m_oInputTxtbox.getHeight() + padding);
		m_oFrameInputImage.setLeft(m_oInputTxtbox.getLeft());
		m_oFrameInputImage.setWidth(this.getWidth() - 2*m_oInputTxtbox.getLeft());
		m_oFrameInputImage.setHeight(m_oButtonCancel.getTop() - m_oInputTxtbox.getTop() - m_oInputTxtbox.getHeight() - 2*padding);
		m_oFrameInputImage.setVisible(false);
		//m_oFrameInputImage.setBackgroundColor(getShadowColor());		// for visualizing the frame, can be comment
		this.attachChild(m_oFrameInputImage);
					
		// Input Image
		m_oImageInput = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageInput, "imgInputImage");
		m_oImageInput.setHeight(m_oFrameInputImage.getHeight());
		m_oImageInput.setWidth(m_oFrameInputImage.getWidth());
		m_oFrameInputImage.attachChild(m_oImageInput);
		
		// Create QR Scan Button
		m_oButtonQR = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonQR, "btnQR");
		m_oButtonQR.setVisible(false);
		m_oButtonQR.addClickServerRequestSubmitElement(this);
		this.attachChild(m_oButtonQR);
		
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oFrameNumberPad.setTop(m_oInputTxtbox.getTop() + m_oInputTxtbox.getHeight() + 12);
		}
		this.setHeight(m_oButtonOK.getTop() + m_oButtonOK.getHeight() + 24);
	}
	
	public void initWithInputNum(int iInputNum) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameInputBoxListener>();
		m_oLblMessagesList = new ArrayList<VirtualUILabel>();
		m_oTxtboxInputsList = new ArrayList<VirtualUITextbox>();
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraInputBox.xml");
		
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(false);
		this.attachChild(m_oTitleHeader);
		
		// Number pad
		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.setCancelAndEnterToLeftAndRigth(true);
		m_oFrameNumberPad.setNumPadLeft(400);
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.addListener(this);
		m_oFrameNumberPad.setVisible(false);
		this.attachChild(m_oFrameNumberPad);
		
		// InputBox Message
		VirtualUILabel oLblMsg = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblMsg, "lblMessage");
		
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			oLblMsg.setTop(64);
		m_oLblMessagesList.add(oLblMsg);
		this.attachChild(oLblMsg);
		
		// Create InputBox textbox
		VirtualUITextbox oTxtboxInput = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(oTxtboxInput, "txtbox");
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			oTxtboxInput.setTop(oLblMsg.getTop()+oLblMsg.getHeight() + 10);
		oTxtboxInput.setFocusWhenShow(true);
		m_oTxtboxInputsList.add(oTxtboxInput);
		this.attachChild(oTxtboxInput);
		m_oInputTxtbox = oTxtboxInput;
		
		for(int i = 1; i < iInputNum; i++) {
			// InputBox Message
			VirtualUILabel oTempLbl = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oTempLbl, "lblMessage");
			if(i % 2 != 0 && !AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				oTempLbl.setLeft(394);
				oTempLbl.setTop(m_oLblMessagesList.get(m_oLblMessagesList.size() - 1).getTop());
			} else if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
				oTempLbl.setTop(oTxtboxInput.getTop() + oTxtboxInput.getHeight() + 12 + (i - 1)*(oLblMsg.getHeight()+oTxtboxInput.getHeight() + 22));
			else
				oTempLbl.setTop(oTxtboxInput.getTop() + oTxtboxInput.getHeight() + ((i - 2) / 2) * (oLblMsg.getHeight() + oTxtboxInput.getHeight()));
			
			m_oLblMessagesList.add(oTempLbl);
			this.attachChild(oTempLbl);
			
			// Create InputBox textbox
			VirtualUITextbox oTempTxtbox = new VirtualUITextbox();
			m_oTemplateBuilder.buildTextbox(oTempTxtbox, "txtbox");
			if(i % 2 != 0 && !AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				oTempTxtbox.setLeft(392);
				oTempTxtbox.setTop(m_oTxtboxInputsList.get(m_oTxtboxInputsList.size() - 1).getTop());
			} else
				oTempTxtbox.setTop(oTempLbl.getTop() + oTempLbl.getHeight());
			oTempTxtbox.setFocusWhenShow(false);
			m_oTxtboxInputsList.add(oTempTxtbox);
			this.attachChild(oTempTxtbox);
		}
		
		// Create OK Button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "btnOK");
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("ok"));
		m_oButtonOK.addClickServerRequestSubmitElement(this);
		this.attachChild(m_oButtonOK);
		
		// Create Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "btnCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		m_oButtonCancel.setClickServerRequestBlockUI(false);
		this.attachChild(m_oButtonCancel);
		
		// Dummy button to prevent null pointer exception
		m_oButtonGetInformation = new VirtualUIButton();
		m_oButtonQR = new VirtualUIButton();
		
		//iFrameHeight += m_oButtonOK.getHeight() + 10;

		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			VirtualUITextbox oTemp = new VirtualUITextbox();
			if(m_oTxtboxInputsList.size() == 0)
				oTemp = m_oInputTxtbox;
			else
				oTemp = m_oTxtboxInputsList.get(m_oTxtboxInputsList.size() - 1);
			m_oFrameNumberPad.setTop(oTemp.getTop() + oTemp.getHeight() + 12);
			m_oButtonOK.setTop(oTemp.getTop() + oTemp.getHeight() + 12);
			m_oButtonCancel.setTop(oTemp.getTop() + oTemp.getHeight() + 12);
			
			int iInputFrameHeight = m_oTxtboxInputsList.get(m_oTxtboxInputsList.size()-1).getTop() + m_oTxtboxInputsList.get(m_oTxtboxInputsList.size()-1).getHeight() + m_oButtonOK.getHeight() + 10;
			int iInputFrameTop = (800- iInputFrameHeight) / 2;
			this.setTop(iInputFrameTop);
		}
		this.setHeight(m_oButtonOK.getTop() + m_oButtonOK.getHeight() + 27);
	}
	
	public void setTitle(String sTitle){
		m_oTitleHeader.setTitle(sTitle);
	}
	
	public void setMessage(String sMessage){
		m_oLabelMessage.setValue(sMessage);
	}
	
	public void setMessages(List<String> oMessagesList) {
		int i = 0;
		for(VirtualUILabel oLblMessage:m_oLblMessagesList) {
			oLblMessage.setValue(oMessagesList.get(i));
			i++;
		}
	}
    
	public void setDefaultInputValue(String sDefaultValue){
		m_oInputTxtbox.setValue(sDefaultValue);
	}
	
	public String getInputValue(){
		return m_oInputTxtbox.getValue();
	}
	
	//set focus on target textbox
	public void setFocusTextBox(int iIndex){
		VirtualUITextbox oInputbox = m_oTxtboxInputsList.get(iIndex);
		oInputbox.setFocus();
	}
	
	public void setDefaultInputValue(int iIndex, String sDefaultValue) {
		try {
			VirtualUITextbox oInputbox = m_oTxtboxInputsList.get(iIndex);
			oInputbox.setValue(sDefaultValue);
		}catch(Exception e) {
			return;
		}
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
	
	public int getInputTxtboxCount() {
		try {
			return m_oTxtboxInputsList.size();
		}
		catch(Exception e) {
			return 0;
		}
	}
	
	public void setKeyboardType(String sType){
		if(getInputTxtboxCount() > 1) {
			for(VirtualUITextbox oTxtbox:m_oTxtboxInputsList) {
				oTxtbox.setKeyboardType(sType);
			}
		}
		else {
			m_oInputTxtbox.setKeyboardType(sType);
		}
		
		if(sType.equals(HeroActionProtocol.View.Attribute.KeyboardType.NUMBER) ||
				sType.equals(HeroActionProtocol.View.Attribute.KeyboardType.DECIMAL) ||
				sType.equals(HeroActionProtocol.View.Attribute.KeyboardType.PHONE)){
			forceShowNumberPad();
		}
	}
	
	public void forceShowNumberPad(){
		if(getInputTxtboxCount() > 1) {
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				int iInputFrameHeight = m_oTxtboxInputsList.get(m_oTxtboxInputsList.size()-1).getTop() + m_oTxtboxInputsList.get(m_oTxtboxInputsList.size()-1).getHeight() + m_oFrameNumberPad.getHeight() + 10;
				int iInputFrameTop = (800- iInputFrameHeight) / 2;
				this.setTop(iInputFrameTop);
				
				m_oFrameNumberPad.setVisible(true);
				m_oFrameNumberPad.clearEnterSubmitId();
				for(VirtualUITextbox oInputTxtbox:m_oTxtboxInputsList)
					m_oFrameNumberPad.setEnterSubmitId(oInputTxtbox);
				
				this.setHeight(iInputFrameHeight);
				
			}else {
				this.setHeight(this.getHeight()-m_oButtonCancel.getHeight() + 10);
				
				if(this.getHeight() < m_oFrameNumberPad.getHeight() + m_oFrameNumberPad.getTop()) {
					this.setHeight(m_oFrameNumberPad.getHeight() + m_oFrameNumberPad.getTop() + 10);
				}
				
				m_oFrameNumberPad.setVisible(true);
				m_oFrameNumberPad.clearEnterSubmitId();
				for(VirtualUITextbox oInputTxtbox:m_oTxtboxInputsList)
					m_oFrameNumberPad.setEnterSubmitId(oInputTxtbox);
			}
		}
		else {
			this.setHeight(m_oFrameNumberPad.getTop() + m_oFrameNumberPad.getHeight() + 10);
			m_oFrameNumberPad.setVisible(true);
			m_oFrameNumberPad.clearEnterSubmitId();
			m_oFrameNumberPad.setEnterSubmitId(m_oInputTxtbox);
		}
		relocateInputBox();
		
		m_oButtonOK.setVisible(false);
		m_oButtonCancel.setVisible(false);
	}
	
	public void setKeyboardType(int iIndex, String sType){
		
		boolean bShowNumberPadBefore = false;
		for(VirtualUITextbox oTxtbox:m_oTxtboxInputsList) {
			String sDefinedType = oTxtbox.getKeyboardType();
			if(sDefinedType.equals(HeroActionProtocol.View.Attribute.KeyboardType.NUMBER) ||
					sDefinedType.equals(HeroActionProtocol.View.Attribute.KeyboardType.DECIMAL) ||
					sDefinedType.equals(HeroActionProtocol.View.Attribute.KeyboardType.PHONE)){
				bShowNumberPadBefore = true;
				break;
			}
		}
		
		try {
			VirtualUITextbox oInputbox = m_oTxtboxInputsList.get(iIndex);
			oInputbox.setKeyboardType(sType);
		}catch(Exception e) {
			return;
		}
		
		if(!bShowNumberPadBefore){
			if(sType.equals(HeroActionProtocol.View.Attribute.KeyboardType.NUMBER) ||
					sType.equals(HeroActionProtocol.View.Attribute.KeyboardType.DECIMAL) ||
					sType.equals(HeroActionProtocol.View.Attribute.KeyboardType.PHONE)){
				
				if(getInputTxtboxCount() > 1) {
					if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
						VirtualUIFrame oCoverFrame = (VirtualUIFrame)this.getParent();
						int iInputFrameHeight = m_oTxtboxInputsList.get(m_oTxtboxInputsList.size()-1).getTop() + m_oTxtboxInputsList.get(m_oTxtboxInputsList.size()-1).getHeight() + m_oFrameNumberPad.getHeight() + 10;
						int iInputFrameTop = (oCoverFrame.getHeight()- iInputFrameHeight) / 2;
						this.setTop(iInputFrameTop);
						
						m_oFrameNumberPad.setVisible(true);
						m_oFrameNumberPad.clearEnterSubmitId();
						for(VirtualUITextbox oInputTxtbox:m_oTxtboxInputsList)
							m_oFrameNumberPad.setEnterSubmitId(oInputTxtbox);
						
						this.setHeight(iInputFrameHeight);
						
					}else {
						this.setHeight(this.getHeight()-m_oButtonCancel.getHeight() + 10);
						
						if(this.getHeight() < m_oFrameNumberPad.getHeight() + m_oFrameNumberPad.getTop())
							this.setHeight(m_oFrameNumberPad.getHeight() + m_oFrameNumberPad.getTop() + 10);
						
						m_oFrameNumberPad.setVisible(true);
						m_oFrameNumberPad.clearEnterSubmitId();
						for(VirtualUITextbox oInputTxtbox:m_oTxtboxInputsList)
							m_oFrameNumberPad.setEnterSubmitId(oInputTxtbox);
					}
				}
				else {
					if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
						m_oFrameNumberPad.setTop(m_oInputTxtbox.getTop() + m_oInputTxtbox.getHeight() + 10);
					this.setHeight(m_oFrameNumberPad.getTop() + m_oFrameNumberPad.getHeight() + 10);
					m_oFrameNumberPad.setVisible(true);
					m_oFrameNumberPad.clearEnterSubmitId();
					m_oFrameNumberPad.setEnterSubmitId(m_oInputTxtbox);
				}
				
				relocateInputBox();
				
				m_oButtonOK.setVisible(false);
				m_oButtonCancel.setVisible(false);
			}
		}
	}
	
	public void relocateInputBox() {
		if(m_oLblMessagesList != null) {
			m_oLblMessagesList.get(0).setTop(m_oLblMessagesList.get(0).getTop());
			m_oTxtboxInputsList.get(0).setTop(m_oTxtboxInputsList.get(0).getTop());
			for(int i = 1; i < m_oLblMessagesList.size() ; i++) {
				// InputBox Message
				m_oLblMessagesList.get(i).setTop(m_oTxtboxInputsList.get(0).getTop() + m_oTxtboxInputsList.get(0).getHeight() + (i - 1) * (m_oLblMessagesList.get(0).getHeight()+m_oTxtboxInputsList.get(0).getHeight()));
				m_oLblMessagesList.get(i).setLeft(m_oLblMessagesList.get(0).getLeft());
				
				// InputBox TextBox
				m_oTxtboxInputsList.get(i).setTop(m_oLblMessagesList.get(i).getTop()+m_oLblMessagesList.get(i).getHeight());
				m_oTxtboxInputsList.get(i).setLeft(m_oTxtboxInputsList.get(0).getLeft());
			}
		}
		
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			int iInputFrameHeight = 0;
			if(m_oLblMessagesList != null) {
				m_oFrameNumberPad.setTop(m_oTxtboxInputsList.get(m_oTxtboxInputsList.size() - 1).getTop() + m_oTxtboxInputsList.get(m_oTxtboxInputsList.size() - 1).getHeight() + 12);
				iInputFrameHeight = m_oTxtboxInputsList.get(m_oTxtboxInputsList.size()-1).getTop() + m_oTxtboxInputsList.get(m_oTxtboxInputsList.size()-1).getHeight() + m_oFrameNumberPad.getHeight() + 10;
			} else {
				m_oFrameNumberPad.setTop(m_oInputTxtbox.getTop() + m_oInputTxtbox.getHeight() + 12);
				iInputFrameHeight = m_oInputTxtbox.getTop() + m_oInputTxtbox.getHeight() + m_oFrameNumberPad.getHeight() + 10;
			}
			int iInputFrameTop = (800- iInputFrameHeight) / 2;
			this.setTop(iInputFrameTop);
			this.setHeight(m_oFrameNumberPad.getTop() + m_oFrameNumberPad.getHeight() + 12);
		}
	}
	
	public void setEnterBlockUI(boolean bBlockUI){
		m_oFrameNumberPad.setEnterBlockUI(bBlockUI);
	}
	
	public void setInputBoxSecurity(int iIndex, boolean bSecurity) {
		if(bSecurity) {
			if(getInputTxtboxCount() > 1) {
				try {
					VirtualUITextbox oTxtbox = m_oTxtboxInputsList.get(iIndex);
					oTxtbox.setInputType(HeroActionProtocol.View.Attribute.InputType.PASSWORD);
				}
				catch(Exception e) {
					return;
				}
			} else
				m_oInputTxtbox.setInputType(HeroActionProtocol.View.Attribute.InputType.PASSWORD);
		}
		else {
			if(getInputTxtboxCount() > 1) {
				try {
					VirtualUITextbox oTxtbox = m_oTxtboxInputsList.get(iIndex);
					oTxtbox.setInputType(HeroActionProtocol.View.Attribute.InputType.DEFAULT);
				}
				catch(Exception e) {
					return;				
				}
			} else
				m_oInputTxtbox.setInputType(HeroActionProtocol.View.Attribute.InputType.DEFAULT);
		}
	}
	
	public void showKeyboard() {
		// show the keyboard
		AppGlobal.g_oTerm.get().showKeyboard();
	}
	
	public void showQrButton(String sButtonValue) {
		m_oButtonQR.setValue(sButtonValue);
		m_oButtonQR.setVisible(true);
		
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			int ibtnWidth = (this.getWidth() - 20) / 3;
			
			resizeButtonSize(m_oButtonCancel, ibtnWidth);
			resizeButtonSize(m_oButtonQR, ibtnWidth);
			resizeButtonSize(m_oButtonOK, ibtnWidth);
			
			m_oButtonCancel.setLeft(10);
			m_oButtonQR.setLeft(10 + ibtnWidth);
			m_oButtonOK.setLeft(10 + 2 * ibtnWidth);
		}
	}
	
	// reset button label
	public void resizeButtonSize(VirtualUIButton oButton, int iWidth) {
		oButton.setWidth(iWidth);
		oButton.setBackgroundColor(oButton.getBackgroundColor());
	}
	
	public void setPaymentImgSourceAndRemainingAmt(String sImageURL, String sRemainingBalance){
		// set image source
		if(!sImageURL.isEmpty()){
			m_oLabelMessage.setVisible(false);
			m_oImage.setSource(sImageURL);
			m_oFrameImageFrame.setVisible(true);
			m_oLabelDesc1.setValue(AppGlobal.g_oLang.get()._("remaining_balance")+": "+ AppGlobal.g_oFuncOutlet.get().getCurrencySign() + sRemainingBalance);
			m_oLabelDesc1.setVisible(true);
			m_oLabelDesc.setVisible(true);
		}
	}
	
	public void setInputScreenImage(String sMediaUrl){
		// set image source
		m_oImageInput.setSource(sMediaUrl);
		m_oFrameInputImage.setVisible(true);
	}
	
	public void setLargeInputImage (String sMediaUrl) {
		// set image source
		m_oImageInput.setSource(sMediaUrl);
		m_oFrameInputImage.setTop(m_oLabelMessage.getTop() + m_oInputTxtbox.getHeight()/2);
		m_oFrameInputImage.setHeight(m_oButtonCancel.getTop() - m_oLabelMessage.getTop() - m_oInputTxtbox.getHeight());
		m_oImageInput.setHeight(m_oFrameInputImage.getHeight());
		m_oFrameInputImage.setBackgroundColor(this.getBackgroundColor());
		m_oFrameInputImage.setVisible(true);
	}
	
	// Set client timeout to maximum for swipe card event
	public void setMaximumClientTimeoutForSwipeCardEvent() {
		ClsActiveClient oActiveClient = AppGlobal.getActiveClient();
		if (oActiveClient != null) {
			oActiveClient.setMaximumClientTimeoutForSwipeCardEvent();
		}
	}
	
	// Resume client timeout to default for swipe card event
	public void resumeClientTimeoutForSwipeCardEvent() {
		ClsActiveClient oActiveClient = AppGlobal.getActiveClient();
		if (oActiveClient != null) {
			oActiveClient.resumeClientTimeoutForSwipeCardEvent();
		}
	}
	
	public void hideAllCancelButton() {
		m_oButtonCancel.setVisible(false);
		m_oFrameNumberPad.setCancelButtonVisible(false);
	}
	
	public void setCloseButtonVisible(boolean bShow) {
		m_oTitleHeader.setButtonShow(bShow);
	}
	
	public void setGetInformationButtonVisible(boolean bShow) {
		m_oButtonGetInformation.setVisible(bShow);
	}
	
	public void setGetInformationButtonValue(String sValue) {
		m_oButtonGetInformation.setValue(sValue);
	}
	
	public void setHint(int iIndex, String sHint){
		try {
			VirtualUITextbox oInputbox = m_oTxtboxInputsList.get(iIndex);
			oInputbox.setHint(sHint);
		}catch(Exception e) {
			return;
		}
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (iChildId == m_oButtonOK.getId()) {
			for (FrameInputBoxListener listener : listeners) {
				// Raise the event to parent
				listener.FrameInputBox_clickOK();
			}
			
			bMatchChild = true;
		}
		else if (iChildId == m_oButtonCancel.getId()) {
			for (FrameInputBoxListener listener : listeners) {
				// Raise the event to parent
				listener.FrameInputBox_clickCancel();
			}
			
			bMatchChild = true;
		}
		else if (iChildId == m_oButtonGetInformation.getId()) {
			for (FrameInputBoxListener listener : listeners) {
				// Raise the event to parent
				listener.FrameInputBox_clickGetInformation();
			}
			bMatchChild = true;
		}
		else if (iChildId == m_oButtonQR.getId()) {
			// hide the keyboard
			AppGlobal.g_oTerm.get().hideKeyboard();
			
			for (FrameInputBoxListener listener : listeners) {
				// Raise the event to parent
				listener.FrameInputBox_clickQrScan();
			}
			
			bMatchChild = true;
		}
		
		return bMatchChild;
	}

	@Override
	public boolean valueChanged(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		ClsActiveClient oActiveClient = AppGlobal.getActiveClient();
		if (oActiveClient != null) {
			if (iChildId == oActiveClient.getSwipeCardReaderElement().getId()) {
				for (FrameInputBoxListener listener : listeners) {
					// Raise the event to parent
					listener.FrameInputBoxListener_swipeCard(oActiveClient.getSwipeCardReaderElement().getValue());
				}
				
				bMatchChild = true;
			}
		}
		
		return bMatchChild;
	}
	
	@Override
	public boolean keyboard(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (iChildId == m_oKeyboardReaderForOK.getId()) {
			for (FrameInputBoxListener listener : listeners) {
				// Raise the event to parent
				listener.FrameInputBox_clickOK();
			}
			
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
	
	@Override
	public void FrameNumberPad_clickEnter() {
		//Focus jump to next Input Textbox
		int iInputTxtboxCount = getInputTxtboxCount();
		if(iInputTxtboxCount > 1) {
			for(int i = 0; i < iInputTxtboxCount; i++) {
				VirtualUITextbox oInputTxtbox = m_oTxtboxInputsList.get(i);
				
				if(oInputTxtbox.getValue().length() == 0)
					return;
				else if((oInputTxtbox.getValue().length() > 0) && (i < iInputTxtboxCount-1)) {
					VirtualUITextbox oNextInputTxtbox = m_oTxtboxInputsList.get(i+1);
					oNextInputTxtbox.setFocus();
				}
			}
		}
		
		
		for (FrameInputBoxListener listener : listeners) {
			// Raise the event to parent
			listener.FrameInputBox_clickOK();
		}
	}

	@Override
	public void FrameNumberPad_clickCancel() {
		for (FrameInputBoxListener listener : listeners) {
			// Raise the event to parent
			listener.FrameInputBox_clickCancel();
		}
	}

	@Override
	public void FrameNumberPad_clickNumber(String string) {
	}
	
	@Override
	public void FrameTitleHeader_close() {
		// TODO Auto-generated method stub
		for (FrameInputBoxListener listener : listeners) {
			// Raise the event to parent
			listener.FrameInputBox_clickCancel();
		}
	}
}
