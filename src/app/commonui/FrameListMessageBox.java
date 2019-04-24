package app.commonui;
import java.util.ArrayList;

import core.templatebuilder.TemplateBuilder;
import core.virtualui.VirtualUIButton;
import core.virtualui.VirtualUIFrame;
import core.virtualui.VirtualUIKeyboardReader;
import core.virtualui.VirtualUILabel;
import core.virtualui.VirtualUIList;
/** interface for the listeners/observers callback method */
interface FrameListMessageBoxListener {
    void FrameListMessageBox_click(String sResult);
}

public class FrameListMessageBox extends VirtualUIFrame {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUILabel m_oLabelTitle;
	private VirtualUIFrame m_oFrameColumnHeader;
	private VirtualUIFrame m_oFrameMessage;
	private VirtualUIList m_oListMessage;
	private ArrayList<VirtualUIButton> m_aButtonArray;
	private ArrayList<VirtualUILabel> m_aColumnHeaderArray;
	private float m_fDelayTime;
	private VirtualUIKeyboardReader m_oKeyboardReaderForOK;

	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameListMessageBoxListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameListMessageBoxListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameListMessageBoxListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
    public FrameListMessageBox(){	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameListMessageBoxListener>();
		m_oFrameColumnHeader = new VirtualUIFrame();
		m_oListMessage = new VirtualUIList();
		m_aButtonArray = new ArrayList<VirtualUIButton>();
		m_aColumnHeaderArray = new ArrayList<VirtualUILabel>();
		m_fDelayTime = 0;		
	}
	
    public void init(){
    	// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraListMessageBox.xml");
		
		// DialogBox Title
		m_oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTitle, "lblTitle");
		m_oLabelTitle.setWidth(this.getWidth() - (m_oLabelTitle.getLeft() * 2));
		this.attachChild(m_oLabelTitle);
		
		// ColumnHeaders
		m_oFrameColumnHeader = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameColumnHeader, "fraColumnHeader");
		m_oFrameColumnHeader.setTop(m_oLabelTitle.getHeight());
		m_oFrameColumnHeader.setWidth(this.getWidth());
		m_oFrameColumnHeader.setBackgroundColor("#B2CBDA");
		m_oFrameColumnHeader.setForegroundColor("#015384");
		this.attachChild(m_oFrameColumnHeader);
			
		m_oListMessage = new VirtualUIList();
		m_oListMessage.setExist(true);
		m_oListMessage.setTop(m_oLabelTitle.getHeight()+m_oFrameColumnHeader.getHeight());
		m_oListMessage.setLeft(0);
		m_oListMessage.setWidth(this.getWidth());
		m_oListMessage.setHeight(this.getHeight() - m_oLabelTitle.getHeight()-m_oFrameColumnHeader.getHeight());
		this.attachChild(m_oListMessage);
				
		// Keyboard
		m_oKeyboardReaderForOK = new VirtualUIKeyboardReader();
		m_oKeyboardReaderForOK.addKeyboardKeyCode(13);
		this.attachChild(m_oKeyboardReaderForOK);
    }
    
    /*
     * width total can not be more than 600
     */
    public void addColumnHeder(String sColumnHeader , int iWidth){
    	int iLeft = 0;
    	if(m_aColumnHeaderArray.size() > 0){
    		for(VirtualUILabel oLabel :m_aColumnHeaderArray){
    			iLeft += oLabel.getWidth();
    		}
    	}
    	
    	VirtualUILabel m_oLabelHeader = new VirtualUILabel();  	
    	m_oLabelHeader.setExist(true);
    	m_oLabelHeader.setTop(0);	
    	m_oLabelHeader.setLeft(iLeft);
    	m_oLabelHeader.setWidth(iWidth);
    	m_oLabelHeader.setHeight(m_oFrameColumnHeader.getHeight());
    	m_oLabelHeader.setValue(sColumnHeader);
    	m_oLabelHeader.setTextAlign("center");
    	m_oFrameColumnHeader.attachChild(m_oLabelHeader);
    	m_aColumnHeaderArray.add(m_oLabelHeader);	
    }
    
    public void addMessage(ArrayList<String> sMessage){
		m_oFrameMessage = new VirtualUIFrame();
		m_oFrameMessage.setExist(true);
    	m_oFrameMessage.setTop(5);
    	m_oFrameMessage.setLeft(0);
    	m_oFrameMessage.setHeight(30);
    	m_oFrameMessage.setBackgroundColor("#F0FAFF");
    	m_oFrameMessage.setForegroundColor("#d34f00");
    	m_oFrameMessage.setWidth(this.getWidth());
    	
    	for(int i=0 ; i<m_aColumnHeaderArray.size() ; i++){
    		VirtualUILabel m_oLabelMessage = new VirtualUILabel();  	
    		m_oLabelMessage.setExist(true);
    		m_oLabelMessage.setTop(0);	
    		m_oLabelMessage.setLeft(m_aColumnHeaderArray.get(i).getLeft());
    		m_oLabelMessage.setWidth(m_aColumnHeaderArray.get(i).getWidth());
    		m_oLabelMessage.setHeight(m_oFrameMessage.getHeight());
    		m_oLabelMessage.setValue(sMessage.get(i));
    		m_oLabelMessage.setTextAlign("center");
    		m_oFrameMessage.attachChild(m_oLabelMessage);
    	}
    	
    	m_oListMessage.attachChild(m_oFrameMessage);
    }
    
    public void addSingleButton(String sBtnValue) {
		VirtualUIButton oNewBtn = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(oNewBtn, "singleBtn");
		oNewBtn.setValue(sBtnValue);
		oNewBtn.setTop(this.getHeight() - oNewBtn.getHeight() - 20);
		oNewBtn.setLeft((this.getWidth()/2) - (oNewBtn.getWidth()/2));
		oNewBtn.setClickServerRequestBlockUI(false);
		this.attachChild(oNewBtn);

		// Resize the message label
		m_oListMessage.setHeight(this.getHeight() - m_oLabelTitle.getHeight() - m_oFrameColumnHeader.getHeight() - (oNewBtn.getHeight() + 20));
		m_aButtonArray.add(oNewBtn);
    }
    
	public void setTitle(String sTitle){
		m_oLabelTitle.setValue(sTitle);
	}
	
	public float getDelayTime(){
		return m_fDelayTime;
	}
	
	public void setDelayTime(float fDelayTime){
		m_fDelayTime = fDelayTime;
	}
    
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
    	for (FrameListMessageBoxListener listener : listeners) {
    		// Find the clicked button
    		for (VirtualUIButton oBtn : m_aButtonArray) {
    			if (oBtn.getId() == iChildId) {
    				// Raise the event to parent
    	       		listener.FrameListMessageBox_click(oBtn.getValue());
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
        	for (FrameListMessageBoxListener listener : listeners) {        		
        		// Raise the event to parent
           		listener.FrameListMessageBox_click("");
            }
        	
        	bMatchChild = true;
        }
        
    	return bMatchChild;
	}
}

