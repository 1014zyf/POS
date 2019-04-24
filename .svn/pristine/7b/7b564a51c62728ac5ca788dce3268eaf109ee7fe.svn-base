package app;

import java.util.ArrayList;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;

/** interface for the listeners/observers callback method */
interface FrameTableMsgReminderListener {
    void frameTableMsgReminder_clickCancel();
    void frameTableMsgReminder_clickSave();
    void frameTableMsgReminder_clickRemoveMessage();
}

public class FrameTableMsgReminder extends VirtualUIFrame implements FrameTitleHeaderListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUIButton m_oBtnRemoveMessage;
	protected VirtualUIButton m_oBtnSave;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameTableMsgReminderListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    protected void addListener(FrameTableMsgReminderListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    protected void removeListener(FrameTableMsgReminderListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    protected void removeAllListener() {
    	listeners.clear();
    }
    
    //Constructor
    protected FrameTableMsgReminder() {
    	m_oTemplateBuilder = new TemplateBuilder();
    	listeners = new ArrayList<FrameTableMsgReminderListener>();
    	
    	// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraTableMsgReminder.xml");

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(true);
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("table_message_reminder"));
		m_oFrameTitleHeader.addListener(this);
		this.attachChild(m_oFrameTitleHeader);
		
		//Remove messages button
		m_oBtnRemoveMessage = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oBtnRemoveMessage, "btnRemoveMsg");
		m_oBtnRemoveMessage.setValue(AppGlobal.g_oLang.get()._("remove_message"));
		this.attachChild(m_oBtnRemoveMessage);
		
		//Save button
		m_oBtnSave = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oBtnSave, "btnSave");
		m_oBtnSave.setValue(AppGlobal.g_oLang.get()._("save"));
		this.attachChild(m_oBtnSave);
    }
    
    @Override
	public boolean clicked(int iChildId, String sNote) {
    	boolean bMatchChild = false;
		
		// Find the clicked button
		if(iChildId == m_oBtnSave.getId()) {
			for (FrameTableMsgReminderListener listener : listeners) {
    			// Raise the event to parent
    	       	listener.frameTableMsgReminder_clickSave();
    		}
			bMatchChild = true;
		}
		else if(iChildId == m_oBtnRemoveMessage.getId()) {
			for (FrameTableMsgReminderListener listener : listeners) {
    			// Raise the event to parent
    	       	listener.frameTableMsgReminder_clickRemoveMessage();
    		}
			bMatchChild = true;
		}
    	return bMatchChild;
    }

	@Override
	public void FrameTitleHeader_close() {
		for (FrameTableMsgReminderListener listener : listeners) {
			// Raise the event to parent
	       	listener.frameTableMsgReminder_clickCancel();
		}
	}
    
}