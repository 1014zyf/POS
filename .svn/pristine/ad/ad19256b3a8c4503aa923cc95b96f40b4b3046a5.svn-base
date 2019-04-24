package app;

import java.util.ArrayList;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIKeyboardReader;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameElectronicScaleListener {
    void FrameElectronicScale_clickOK();
    void FrameElectronicScale_clickRetry();
    void FrameElectronicScale_clickCancel();
    void FrameElectronicScale_forward(String sResponse);
	void FrameElectronicScale_disconnect();
	void FrameElectronicScale_timeout();
}

public class FrameElectronicScale extends VirtualUIFrame implements FrameTitleHeaderListener{
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oTitleHeader;
	private VirtualUILabel m_oLabelMessage;
	private VirtualUIButton m_oButtonOK;
	private VirtualUIButton m_oButtonRetry;
	private VirtualUIButton m_oButtonCancel;
	private VirtualUIKeyboardReader m_oKeyboardReaderForOK;

	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameElectronicScaleListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameElectronicScaleListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameElectronicScaleListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
    public FrameElectronicScale(){	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameElectronicScaleListener>();
	}
	
    public void init(){
    	// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraElectronicScale.xml");
		
		// Title Bar Frame
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(false);
		this.attachChild(m_oTitleHeader);
		
		// DialogBox Message
		m_oLabelMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMessage, "lblMessage");
		m_oLabelMessage.setTop(m_oTitleHeader.getHeight());
		
		m_oLabelMessage.setWidth(this.getWidth());
		//m_oLabelMessage.setHeight((this.getHeight() - m_oLabelTitle.getHeight()) / 3);
		this.attachChild(m_oLabelMessage);
		
		// OK Button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "butOK");
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("confirm"));
		
		m_oButtonOK.setTop(this.getHeight() - m_oButtonOK.getHeight() - 24);
		//m_oButtonOK.setLeft((this.getWidth()/3) - m_oButtonOK.getWidth() - 5);
		m_oButtonOK.setLeft((this.getWidth()/3)*2 + 5);
		this.attachChild(m_oButtonOK);
		
		// Retry Button
		m_oButtonRetry = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonRetry, "butRetry");
		m_oButtonRetry.setValue(AppGlobal.g_oLang.get()._("retry"));
		m_oButtonRetry.setTop(this.getHeight() - m_oButtonRetry.getHeight() - 24);
		m_oButtonRetry.setLeft((this.getWidth()/2) - (m_oButtonRetry.getWidth()/2));
		this.attachChild(m_oButtonRetry);
		
		// Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "butCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		m_oButtonCancel.setTop(this.getHeight() - m_oButtonCancel.getHeight() - 24);
		//m_oButtonCancel.setLeft((this.getWidth()/3)*2 + 5);
		m_oButtonCancel.setLeft((this.getWidth()/3) - m_oButtonCancel.getWidth() - 5);
		m_oButtonCancel.setClickServerRequestBlockUI(false);
		this.attachChild(m_oButtonCancel);
		
		// Keyboard
		m_oKeyboardReaderForOK = new VirtualUIKeyboardReader();
		m_oKeyboardReaderForOK.addKeyboardKeyCode(13);
		this.attachChild(m_oKeyboardReaderForOK);
		
		// Resize the message label
		m_oLabelMessage.setHeight(this.getHeight() - m_oTitleHeader.getHeight() - (m_oButtonOK.getHeight() + 20));
	}
	
	public void setTitle(String sTitle){
		m_oTitleHeader.setTitle(sTitle);
	}
	
	public void setMessage(String sMessage){
		m_oLabelMessage.setValue(sMessage);
	}
	
	public void createForwardEvent(String sValue, int iTimeout, int iDelay) {
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestValue(sValue);
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(iTimeout);
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestDelay(iDelay);
	}
	
	public void showOKButton(boolean bShow) {
		m_oButtonOK.setVisible(bShow);
	}
	
	public void showRetryButton(boolean bShow) {
		m_oButtonRetry.setVisible(bShow);
	}
	
	public void showCancelButton(boolean bShow) {
		m_oButtonCancel.setVisible(bShow);
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if(m_oButtonOK.getId() == iChildId){
			for (FrameElectronicScaleListener listener : listeners) {
				// Raise the event to parent
	       		listener.FrameElectronicScale_clickOK();
	       		bMatchChild = true;
	       		break;
			}
		}else
		if(m_oButtonRetry.getId() == iChildId){
			for (FrameElectronicScaleListener listener : listeners) {
				// Raise the event to parent
	       		listener.FrameElectronicScale_clickRetry();
	       		bMatchChild = true;
	       		break;
			}
		}else
		if(m_oButtonCancel.getId() == iChildId){
			for (FrameElectronicScaleListener listener : listeners) {
				// Raise the event to parent
	       		listener.FrameElectronicScale_clickCancel();
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
        	for (FrameElectronicScaleListener listener : listeners) {        		
        		// Raise the event to parent
           		listener.FrameElectronicScale_clickOK();
            }
        	
        	bMatchChild = true;
        }
        
    	return bMatchChild;
	}
	
	@Override
	public boolean forward(int iChildId, String sNote, String sStatus) {
		boolean bMatchChild = false;
		
		if (iChildId == AppGlobal.g_oDeviceManagerElement.get().getId()) {
			for (FrameElectronicScaleListener listener : listeners) {
				if (sNote.equals(AppGlobal.g_oDeviceManagerElement.get().getForwardServerRequestNote())) {
					// Current event's version is up-to-date
					
					// Raise the event to parent
					if (sStatus.equals("disconnected"))
						listener.FrameElectronicScale_disconnect();
					else
					if (sStatus.equals("time_out")) {
						// Ingore forward timeout
					} else
						listener.FrameElectronicScale_forward(AppGlobal.g_oDeviceManagerElement.get().getValue());
				}
			}
			
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
	
	@Override
	public void FrameTitleHeader_close() {
		// TODO Auto-generated method stub
	}
	
}
