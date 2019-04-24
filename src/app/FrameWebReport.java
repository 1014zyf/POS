package app;

import java.util.ArrayList;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIWebView;

/** interface for the listeners/observers callback method */
interface FrameWebReportListener {
    void frameWebReport_clickOK();
}

public class FrameWebReport extends VirtualUIFrame implements FrameTitleHeaderListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUIWebView m_oWebViewCheck;
	private VirtualUIButton m_oButtonOK;
	
	private FrameTitleHeader m_oTitleHeader;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameWebReportListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameWebReportListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameWebReportListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
	public FrameWebReport(){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameWebReportListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraWebReport.xml");
		
		m_oTitleHeader = new FrameTitleHeader();
        m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
        m_oTitleHeader.init(true);
        m_oTitleHeader.addListener(this);
        m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("report"));
        this.attachChild(m_oTitleHeader);
		
		// Review Area
		m_oWebViewCheck = new VirtualUIWebView();
		m_oTemplateBuilder.buildWebView(m_oWebViewCheck, "wbvReport");
		this.attachChild(m_oWebViewCheck);
		
		// OK button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "btnOK");
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("exit"));
		m_oButtonOK.setVisible(true);
		this.attachChild(m_oButtonOK);		
	}
	
	public void setCheckURL(String sURL){
		m_oWebViewCheck.setSource(sURL);
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
    	for (FrameWebReportListener listener : listeners) {
    		// Find the clicked button
    		if (m_oButtonOK.getId() == iChildId) {
    			// Raise the event to parent
    	       	listener.frameWebReport_clickOK();
    	       	bMatchChild = true;
    	       	break;
    		}
        }
    	
    	return bMatchChild;
    }

	@Override
	public void FrameTitleHeader_close() {
    	for (FrameWebReportListener listener : listeners) {
    		// Find the clicked button
    		// Raise the event to parent
    		listener.frameWebReport_clickOK();
    	}
	}
	
}
