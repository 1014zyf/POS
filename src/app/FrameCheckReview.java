package app;

import java.util.ArrayList;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import virtualui.VirtualUIButton;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIWebView;

/** interface for the listeners/observers callback method */
interface FrameCheckReviewListener {
    void frameCheckReview_clickOK();
    void frameCheckReview_clickPrint(String sPrintType, int iPrintFormatId);
}

public class FrameCheckReview extends VirtualUIFrame implements FrameTitleHeaderListener {
	TemplateBuilder m_oTemplateBuilder;

	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUIWebView m_oWebViewCheck;
	private VirtualUIButton m_oButtonPrintCheck;
	private int m_iPrintFormatId;
	private String m_sPrintType;
	public static String PRINT_TYPE_NORMAL_CHECK = "normal";
	public static String PRINT_TYPE_DETAIL_CHECK = "detail";
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameCheckReviewListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameCheckReviewListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameCheckReviewListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
	public FrameCheckReview(){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameCheckReviewListener>();
		
		m_sPrintType = FrameCheckReview.PRINT_TYPE_DETAIL_CHECK;
		m_iPrintFormatId = -1;
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraCheckReview.xml");

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(true);
		m_oFrameTitleHeader.addListener(this);
		this.attachChild(m_oFrameTitleHeader);

		// Review Area
		m_oWebViewCheck = new VirtualUIWebView();
		m_oTemplateBuilder.buildWebView(m_oWebViewCheck, "wbvCheck");
		this.attachChild(m_oWebViewCheck);	
		
		//print Check Button
		m_oButtonPrintCheck = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonPrintCheck, "btnPrintCheck");
		m_oButtonPrintCheck.allowClick(true);
		m_oButtonPrintCheck.setValue(AppGlobal.g_oLang.get()._("print"));
		m_oButtonPrintCheck.setVisible(true);
		this.attachChild(m_oButtonPrintCheck);
	}
	
	public void printCheck(String sPrintType, int iPrintFormatId){
		m_sPrintType = sPrintType;
		m_iPrintFormatId = iPrintFormatId;
	}
	
	public void showPrintButton(boolean bVisible){
//KingsleyKwan20171016ByKing	-----Start-----
		if(bVisible){
			m_oWebViewCheck.setHeight(498);
			m_oButtonPrintCheck.setTop(m_oWebViewCheck.getTop() + m_oWebViewCheck.getHeight() + 20);
			m_oButtonPrintCheck.setLeft((this.getWidth() - m_oButtonPrintCheck.getWidth()) / 2);
		}
		else
			m_oWebViewCheck.setHeight(570);
//KingsleyKwan20171016ByKing	----- End -----
		m_oButtonPrintCheck.setVisible(bVisible);
	}
	
	public void setCheckURL(String sURL){
		m_oWebViewCheck.setSource(sURL);
	}
	
	// Set header desc
	public void setTitle(String sTitle){
		m_oFrameTitleHeader.setTitle(sTitle);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
    	for (FrameCheckReviewListener listener : listeners) {
    		// Find the clicked button
    		
    		if(m_oButtonPrintCheck.getId() == iChildId){
    			listener.frameCheckReview_clickPrint(m_sPrintType, m_iPrintFormatId);
    	       	bMatchChild = true;
    	       	break;
    		}
    	}
    	return bMatchChild;
	}

	@Override
	public void FrameTitleHeader_close() {
		for (FrameCheckReviewListener listener : listeners) {
			// Raise the event to parent
			listener.frameCheckReview_clickOK();
		}
	}
	
}
