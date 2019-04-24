package app;

import java.util.ArrayList;

import org.json.JSONObject;

import core.Controller;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

/** interface for the listeners/observers callback method */
interface FormAdminPanelListener {
	void formAdminPanel_functionClicked(int iId);
	void formAdminPanel_cancelClicked();
}

public class FormAdminPanel extends VirtualUIForm implements FrameAdminPanelListener {
	TemplateBuilder m_oTemplateBuilder;
		
	private FrameAdminPanel m_oFrameAdminPanel;
	private boolean m_bRestoreFastFoodMode;

	/** list of interested listeners (observers, same thing) */
    private ArrayList<FormAdminPanelListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FormAdminPanelListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FormAdminPanelListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
	
	public FormAdminPanel(Controller oParentController) {
		super(oParentController);
		
		m_bRestoreFastFoodMode = false;
		listeners = new ArrayList<FormAdminPanelListener>();
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmAdminPanel.xml");

		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Load form from template file
		m_oFrameAdminPanel = new FrameAdminPanel();
		m_oTemplateBuilder.buildFrame(m_oFrameAdminPanel, "fraAdminPanel");

		// Add listener
		m_oFrameAdminPanel.addListener(this);
		this.attachChild(m_oFrameAdminPanel);
	}
    
    public void createTabsWithPage(ArrayList<JSONObject> pagesArrayList) {
    	m_oFrameAdminPanel.createTabsWithPage(pagesArrayList);
    }
    
    public void createDisplayPanelsWithLookup(int iPage, ArrayList<FuncLookupButtonInfo> displayPanelLookupArrayList, double dWidthRatio, double dHeightRatio) {
    	m_oFrameAdminPanel.createDisplayPanelsWithLookup(iPage, displayPanelLookupArrayList, dWidthRatio, dHeightRatio);
    }
    
    public void showDisplayPanelAtPage(int page) {
    	m_oFrameAdminPanel.showDisplayPanelAtPage(page);
    }
    
    public boolean isRestoreFastFoodMode() {
    	return m_bRestoreFastFoodMode;
    }

	@Override
	public void frameAdminPanelLookup_functionClicked(int iId) {
		this.finishShow();
		for(FormAdminPanelListener listener: listeners) {
			listener.formAdminPanel_functionClicked(iId);
		}
	}
	
//KingsleyKwan20171109		-----Start-----
	@Override
	public void frameAdminPanelLookup_clickCancel() {
		for(FormAdminPanelListener listener: listeners) {
			listener.formAdminPanel_cancelClicked();
		}
	}
//KingsleyKwan20171109		----- end -----
	
}
