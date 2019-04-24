package app.commonui;

import java.util.ArrayList;

import core.templatebuilder.TemplateBuilder;
import core.virtualui.VirtualUIButton;
import core.virtualui.VirtualUIFrame;
import core.virtualui.VirtualUIKeyboardReader;
import core.virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameConfirmBoxListener {
    void FrameConfirmBox_clickOK();
    void FrameConfirmBox_clickCancel();
}

public class FrameConfirmBox extends VirtualUIFrame {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUILabel m_oLabelTitle;
	private VirtualUILabel m_oLabelMessage;
	private VirtualUIButton m_oButtonOK;
	private VirtualUIButton m_oButtonCancel;
	private VirtualUIKeyboardReader m_oKeyboardReaderForOK;

	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameConfirmBoxListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameConfirmBoxListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameConfirmBoxListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
    public FrameConfirmBox(){	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameConfirmBoxListener>();
	}
	
    public void init(String sOKButtonValue, String sCancelButtonValue){
    	// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraConfirmBox.xml");
		
		// DialogBox Title
		m_oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTitle, "lblTitle");
		m_oLabelTitle.setWidth(this.getWidth() - (m_oLabelTitle.getLeft() * 2));
		this.attachChild(m_oLabelTitle);
		
		// DialogBox Message
		m_oLabelMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMessage, "lblMessage");
		m_oLabelMessage.setTop(m_oLabelTitle.getHeight());
		m_oLabelMessage.setWidth(this.getWidth());
		m_oLabelMessage.setHeight(this.getHeight() - m_oLabelTitle.getHeight());
		this.attachChild(m_oLabelMessage);
		
		// OK Button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "butOK");
		m_oButtonOK.setValue(sOKButtonValue);
		m_oButtonOK.setTop(this.getHeight() - m_oButtonOK.getHeight() - 20);
		m_oButtonOK.setLeft((this.getWidth()/2) - m_oButtonOK.getWidth() - 5);
		this.attachChild(m_oButtonOK);
		
		// Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "butCancel");
		m_oButtonCancel.setValue(sCancelButtonValue);
		m_oButtonCancel.setTop(this.getHeight() - m_oButtonCancel.getHeight() - 20);
		m_oButtonCancel.setLeft((this.getWidth()/2) + 5);
		m_oButtonCancel.setClickServerRequestBlockUI(false);
		this.attachChild(m_oButtonCancel);

		// Keyboard
		m_oKeyboardReaderForOK = new VirtualUIKeyboardReader();
		m_oKeyboardReaderForOK.addKeyboardKeyCode(13);
		this.attachChild(m_oKeyboardReaderForOK);
		
		// Resize the message label
		m_oLabelMessage.setHeight(this.getHeight() - m_oLabelTitle.getHeight() - (m_oButtonOK.getHeight() + 20));
    }
    
	public void setTitle(String sTitle){
		m_oLabelTitle.setValue(sTitle);
	}
	
	public void setMessage(String sMessage){
		m_oLabelMessage.setValue(sMessage);
	}
    
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if(m_oButtonOK.getId() == iChildId){
			for (FrameConfirmBoxListener listener : listeners) {
				// Raise the event to parent
	       		listener.FrameConfirmBox_clickOK();
	       		bMatchChild = true;
	       		break;
			}
		}else
		if(m_oButtonCancel.getId() == iChildId){
			for (FrameConfirmBoxListener listener : listeners) {
				// Raise the event to parent
	       		listener.FrameConfirmBox_clickCancel();
	       		bMatchChild = true;
	       		break;
			}
		}
    	
    	return bMatchChild;
    }
	
	@Override
    public boolean keyboard(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
        if (iChildId == m_oKeyboardReaderForOK.getId()) {
        	for (FrameConfirmBoxListener listener : listeners) {        		
        		// Raise the event to parent
           		listener.FrameConfirmBox_clickOK();
            }
        	
        	bMatchChild = true;
        }
        
    	return bMatchChild;
	}
}
