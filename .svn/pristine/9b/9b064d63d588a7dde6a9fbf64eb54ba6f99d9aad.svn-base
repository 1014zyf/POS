package app.commonui;

import java.util.ArrayList;
import java.util.List;

import app.controller.AppGlobal;

import core.templatebuilder.TemplateBuilder;
import core.virtualui.HeroActionProtocol;
import core.virtualui.VirtualUIButton;
import core.virtualui.VirtualUIFrame;
import core.virtualui.VirtualUIKeyboardReader;
import core.virtualui.VirtualUILabel;
import core.virtualui.VirtualUISwipeCardReader;
import core.virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FrameInputBoxListener {
    void FrameInputBox_clickOK();
    void FrameInputBox_clickCancel();
    void FrameInputBoxListener_swipeCard(String sSwipeCardValue);
}

public class FrameInputBox extends VirtualUIFrame implements FrameNumberPadListener {
	private TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUILabel m_oLabelTitle;
	private VirtualUILabel m_oLabelMessage;
	private VirtualUITextbox m_oInputTxtbox;
	private List<VirtualUILabel> m_oLblMessagesList;
	private List<VirtualUITextbox> m_oTxtboxInputsList;
	private VirtualUIButton m_oButtonOK;
	private VirtualUIButton m_oButtonCancel;
	private FrameNumberPad m_oFrameNumberPad;
	private VirtualUISwipeCardReader m_oSwipeCardReader;
	private VirtualUIKeyboardReader m_oKeyboardReaderForOK;

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
	
    public void init(){	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameInputBoxListener>();
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraInputBox.xml");
		
		// InputBox Title
		m_oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTitle, "lblTitle");
		m_oLabelTitle.setWidth(this.getWidth() - (m_oLabelTitle.getLeft() * 2));
		this.attachChild(m_oLabelTitle);
		
		// InputBox Message
		m_oLabelMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMessage, "lblMessage");
		m_oLabelMessage.setTop(m_oLabelTitle.getHeight());
		m_oLabelMessage.setWidth(this.getWidth() - ((m_oLabelMessage.getLeft() + this.getStroke()) * 2));
		this.attachChild(m_oLabelMessage);
		
		// Create InputBox textbox
		m_oInputTxtbox = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oInputTxtbox, "txtbox");
		m_oInputTxtbox.setTop(m_oLabelMessage.getTop() + m_oLabelMessage.getHeight());
		m_oInputTxtbox.setWidth(this.getWidth() - ((m_oInputTxtbox.getLeft() + this.getStroke()) * 2));
		m_oInputTxtbox.setLeft((this.getWidth()-m_oInputTxtbox.getWidth())/2);
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
		
		// Number pad
		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.addListener(this);
		m_oFrameNumberPad.setVisible(false);
		this.attachChild(m_oFrameNumberPad);
		
		// Swipe card element
		m_oSwipeCardReader = new VirtualUISwipeCardReader();
		m_oSwipeCardReader.allowValueChanged(true);
		m_oSwipeCardReader.addValueChangedServerRequestSubmitElement(m_oSwipeCardReader);
		this.attachChild(m_oSwipeCardReader);
		
		// Keyboard
		m_oKeyboardReaderForOK = new VirtualUIKeyboardReader();
		m_oKeyboardReaderForOK.addKeyboardKeyCode(13);
		m_oKeyboardReaderForOK.addKeyboardServerRequestSubmitElement(m_oInputTxtbox);
		this.attachChild(m_oKeyboardReaderForOK);
	}
    
    public void initWithInputNum(int iInputNum) {	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameInputBoxListener>();
		m_oLblMessagesList = new ArrayList<VirtualUILabel>();
		m_oTxtboxInputsList = new ArrayList<VirtualUITextbox>();
		
		int iFrameHeight = 0;
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraInputBox.xml");
		
		// InputBox Title
		m_oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTitle, "lblTitle");
		m_oLabelTitle.setWidth(this.getWidth() - ((m_oLabelTitle.getLeft() + this.getStroke()) * 2));
		this.attachChild(m_oLabelTitle);
		
		// InputBox Message
		VirtualUILabel oLblMsg = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblMsg, "lblMessage");
		oLblMsg.setTop(m_oLabelTitle.getHeight());
		oLblMsg.setWidth(this.getWidth() - ((oLblMsg.getLeft() + this.getStroke()) * 2));
		m_oLblMessagesList.add(oLblMsg);
		this.attachChild(oLblMsg);
		
		// Create InputBox textbox
		VirtualUITextbox oTxtboxInput = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(oTxtboxInput, "txtbox");
		oTxtboxInput.setTop(oLblMsg.getTop() + oLblMsg.getHeight());
		oTxtboxInput.setWidth(this.getWidth() - ((oTxtboxInput.getLeft() + this.getStroke()) * 2));
		oTxtboxInput.setFocusWhenShow(true);
		m_oTxtboxInputsList.add(oTxtboxInput);
		this.attachChild(oTxtboxInput);
		
		iFrameHeight = oTxtboxInput.getTop()+oTxtboxInput.getHeight();
		
		for(int i = 1; i < iInputNum; i++) {
			// InputBox Message
			VirtualUILabel oTempLbl = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oTempLbl, "lblMessage");
			oTempLbl.setTop(oTxtboxInput.getTop()+oTxtboxInput.getHeight() + (i-1)*(oLblMsg.getHeight()+oTxtboxInput.getHeight()));
			oTempLbl.setWidth(this.getWidth() - ((oLblMsg.getLeft() + this.getStroke()) * 2));
			m_oLblMessagesList.add(oTempLbl);
			this.attachChild(oTempLbl);
			
			// Create InputBox textbox
			VirtualUITextbox oTempTxtbox = new VirtualUITextbox();
			m_oTemplateBuilder.buildTextbox(oTempTxtbox, "txtbox");
			oTempTxtbox.setTop(oTempLbl.getTop()+oTempLbl.getHeight());
			oTempTxtbox.setWidth(this.getWidth() - ((oTxtboxInput.getLeft() + this.getStroke()) * 2));
			oTempTxtbox.setFocusWhenShow(false);
			m_oTxtboxInputsList.add(oTempTxtbox);
			this.attachChild(oTempTxtbox);
			
			iFrameHeight += oTempLbl.getHeight() + oTempTxtbox.getHeight();
		}
		
		iFrameHeight += 40;
		
		// Create OK Button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "btnOK");
		m_oButtonOK.setTop(iFrameHeight);
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("ok"));
		m_oButtonOK.addClickServerRequestSubmitElement(this);
		this.attachChild(m_oButtonOK);
		
		// Create Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "btnCancel");
		m_oButtonCancel.setTop(iFrameHeight);
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		m_oButtonCancel.setClickServerRequestBlockUI(false);
		this.attachChild(m_oButtonCancel);
		
		iFrameHeight += m_oButtonOK.getHeight() + 10;
		
		// Number pad
		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.addListener(this);
		m_oFrameNumberPad.setVisible(false);
		this.attachChild(m_oFrameNumberPad);
		
		// Swipe card element
		m_oSwipeCardReader = new VirtualUISwipeCardReader();
		m_oSwipeCardReader.allowValueChanged(true);
		m_oSwipeCardReader.addValueChangedServerRequestSubmitElement(m_oSwipeCardReader);
		this.attachChild(m_oSwipeCardReader);
		
		this.setHeight(iFrameHeight);
	}
    
	public void setTitle(String sTitle){
		m_oLabelTitle.setValue(sTitle);
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
		m_oInputTxtbox.setValue(m_oInputTxtbox.getId(), sDefaultValue);
	}
	
	public String getInputValue(){
		return m_oInputTxtbox.getValue();
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
	
	public boolean setKeyboardType(String sType){
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
		
		return true;
	}
	
	public void forceShowNumberPad(){
		if(getInputTxtboxCount() > 1) {
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				VirtualUIFrame oCoverFrame = (VirtualUIFrame)this.getParent();
				int iInputFrameHeight = m_oTxtboxInputsList.get(m_oTxtboxInputsList.size()-1).getTop() + m_oTxtboxInputsList.get(m_oTxtboxInputsList.size()-1).getHeight() + m_oFrameNumberPad.getHeight() + 10;
				int iInputFrameTop = (oCoverFrame.getHeight()- iInputFrameHeight) / 2;
				this.setTop(iInputFrameTop);
				
				m_oFrameNumberPad.setTop(m_oTxtboxInputsList.get(m_oTxtboxInputsList.size()-1).getTop() + m_oTxtboxInputsList.get(m_oTxtboxInputsList.size()-1).getHeight() + 10);
				m_oFrameNumberPad.setVisible(true);
				m_oFrameNumberPad.clearEnterSubmitId();
				for(VirtualUITextbox oInputTxtbox:m_oTxtboxInputsList)
					m_oFrameNumberPad.setEnterSubmitId(oInputTxtbox);
				
				this.setHeight(iInputFrameHeight);
				
			}else {
				this.setHeight(this.getHeight()-m_oButtonCancel.getHeight() + 10);
				
				VirtualUIFrame oCoverFrame = (VirtualUIFrame)this.getParent();
				int iInputFrameLeft = (oCoverFrame.getWidth()-this.getWidth()-m_oFrameNumberPad.getWidth()) / 2;
				this.setLeft(iInputFrameLeft);
				
				m_oFrameNumberPad.setTop(m_oLabelTitle.getHeight() + 10);
				m_oFrameNumberPad.setLeft(this.getWidth());
				
				this.setWidth(this.getWidth() + m_oFrameNumberPad.getWidth());
				m_oLabelTitle.setWidth(this.getWidth() - ((m_oLabelTitle.getLeft() + this.getStroke()) * 2));
				if(this.getHeight() < m_oFrameNumberPad.getHeight()+m_oFrameNumberPad.getTop()) {
					this.setHeight(m_oFrameNumberPad.getHeight()+m_oFrameNumberPad.getTop()+10);
				}
				
				m_oFrameNumberPad.setVisible(true);
				m_oFrameNumberPad.clearEnterSubmitId();
				for(VirtualUITextbox oInputTxtbox:m_oTxtboxInputsList)
					m_oFrameNumberPad.setEnterSubmitId(oInputTxtbox);
			}
		}
		else {
			m_oFrameNumberPad.setTop(m_oInputTxtbox.getTop() + m_oInputTxtbox.getHeight() + 10);
			this.setHeight(m_oFrameNumberPad.getTop() + m_oFrameNumberPad.getHeight() + 10);
			m_oFrameNumberPad.setVisible(true);
			m_oFrameNumberPad.clearEnterSubmitId();
			m_oFrameNumberPad.setEnterSubmitId(m_oInputTxtbox);
		}
		
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
						
						m_oFrameNumberPad.setTop(m_oTxtboxInputsList.get(m_oTxtboxInputsList.size()-1).getTop() + m_oTxtboxInputsList.get(m_oTxtboxInputsList.size()-1).getHeight() + 10);
						m_oFrameNumberPad.setVisible(true);
						m_oFrameNumberPad.clearEnterSubmitId();
						for(VirtualUITextbox oInputTxtbox:m_oTxtboxInputsList)
							m_oFrameNumberPad.setEnterSubmitId(oInputTxtbox);
						
						this.setHeight(iInputFrameHeight);
						
					}else {
						this.setHeight(this.getHeight()-m_oButtonCancel.getHeight() + 10);
						
						VirtualUIFrame oCoverFrame = (VirtualUIFrame)this.getParent();
						int iInputFrameLeft = (oCoverFrame.getWidth()-this.getWidth()-m_oFrameNumberPad.getWidth()) / 2;
						this.setLeft(iInputFrameLeft);
						
						m_oFrameNumberPad.setTop(m_oLabelTitle.getHeight() + 10);
						m_oFrameNumberPad.setLeft(this.getWidth());
						
						this.setWidth(this.getWidth() + m_oFrameNumberPad.getWidth());
						m_oLabelTitle.setWidth(this.getWidth() - ((m_oLabelTitle.getLeft() + this.getStroke()) * 2));
						if(this.getHeight() < m_oFrameNumberPad.getHeight()+m_oFrameNumberPad.getTop()) {
							this.setHeight(m_oFrameNumberPad.getHeight()+m_oFrameNumberPad.getTop()+10);
						}
						
						m_oFrameNumberPad.setVisible(true);
						m_oFrameNumberPad.clearEnterSubmitId();
						for(VirtualUITextbox oInputTxtbox:m_oTxtboxInputsList)
							m_oFrameNumberPad.setEnterSubmitId(oInputTxtbox);
					}
				}
				else {
					m_oFrameNumberPad.setTop(m_oInputTxtbox.getTop() + m_oInputTxtbox.getHeight() + 10);
					this.setHeight(m_oFrameNumberPad.getTop() + m_oFrameNumberPad.getHeight() + 10);
					m_oFrameNumberPad.setVisible(true);
					m_oFrameNumberPad.clearEnterSubmitId();
					m_oFrameNumberPad.setEnterSubmitId(m_oInputTxtbox);
				}
				
				m_oButtonOK.setVisible(false);
				m_oButtonCancel.setVisible(false);
			}
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
        
    	return bMatchChild;
	}

	@Override
    public boolean valueChanged(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (iChildId == m_oSwipeCardReader.getId()) {       
        	for (FrameInputBoxListener listener : listeners) {
        		// Raise the event to parent
           		listener.FrameInputBoxListener_swipeCard(m_oSwipeCardReader.getValue());
            }
        	
        	bMatchChild = true;
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

}
