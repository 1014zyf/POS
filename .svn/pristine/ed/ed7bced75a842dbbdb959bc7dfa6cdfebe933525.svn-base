package app;

import java.util.ArrayList;

import core.Controller;
import templatebuilder.TemplateBuilder;
import om.PosCheck;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

/** interface for the listeners/observers callback method */
interface FormCheckHistoryListener {
	void formCheckHistory_selectedRecordClicked(String sNote);
}

public class FormCheckHistory extends VirtualUIForm implements FrameCheckHistoryListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameCheckHistory m_oFrameCheckHistory;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FormCheckHistoryListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FormCheckHistoryListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FormCheckHistoryListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
	
	public FormCheckHistory(boolean bRecordClickable, Controller oParentController) {
		super(oParentController);
		
		listeners = new ArrayList<FormCheckHistoryListener>();
		
		m_oTemplateBuilder = new TemplateBuilder();
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmCheckHistory.xml");
		
//KingsleyKwan20170918ByNick		-----Start-----
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// SelectionBox Frame
		m_oFrameCheckHistory = new FrameCheckHistory();
		m_oTemplateBuilder.buildFrame(m_oFrameCheckHistory, "fraCheckHistory");
		m_oFrameCheckHistory.init(bRecordClickable);
		
		// Add listener
		m_oFrameCheckHistory.addListener(this);
		this.attachChild(m_oFrameCheckHistory);	
//KingsleyKwan20170918ByNick		----- End -----
	}

	public void init(ArrayList<PosCheck> oCheckList, int iCheckRoundDecimal) {
		m_oFrameCheckHistory.addCheckHistoryRecord(oCheckList, iCheckRoundDecimal);
	}

	public void setTitle(String sTitle) {
		m_oFrameCheckHistory.setTitle(sTitle);
	}
	
	@Override
	public void frameCheckHistory_clickOK() {
		this.finishShow();
	}

	@Override
	public void frameCheckHistory_selectedRecordClicked(String sNote) {
		for(FormCheckHistoryListener listener : listeners) {
			listener.formCheckHistory_selectedRecordClicked(sNote);
			this.finishShow();
			break;
		}
	}
	
}

