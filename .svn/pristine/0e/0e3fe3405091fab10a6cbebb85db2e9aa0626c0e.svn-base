package app;

import java.util.ArrayList;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

interface FrameMenuModeListener {
    void frameTableFloorPlan_MenuModeNewOrder(int iDefaultTableNo);
    void frameTableFloorPlan_MenuModeChangeLanguage();
    void frameTableFloorPlan_MenuModeCheckReview(int iDefaultTableNo);
}

public class FrameMenuMode extends VirtualUIFrame{
	TemplateBuilder m_oTemplateBuilder;
	
	VirtualUIImage m_oImageNewOrder;
	VirtualUIImage m_oImageNewOrderButton;
	VirtualUIImage m_oImageCheckReviewButton;
	VirtualUIImage m_oImageChgLangButton;
	VirtualUILabel m_oLabelNewOrder;
	VirtualUILabel m_oLabelCheckReview;
	VirtualUIFrame m_oFrameWholeCover;
	
	private int m_iDefaultTableNo;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameMenuModeListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameMenuModeListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameMenuModeListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
	
	//Constructor
   	public FrameMenuMode(int iDefaultTableNo) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameMenuModeListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraMenuMode.xml");
		
		// Whole cover frame
		m_oFrameWholeCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameWholeCover, "fraWholeCover");
		this.attachChild(m_oFrameWholeCover);
		
		m_oImageNewOrder = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageNewOrder, "imgNewOrderBack");
		m_oImageNewOrder.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/backgrounds/welcome_page_bg.png");
		m_oImageNewOrder.setEnabled(true);
		m_oFrameWholeCover.attachChild(m_oImageNewOrder);
		
		m_oImageNewOrderButton = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageNewOrderButton, "imgNewOrderButton");
		m_oImageNewOrderButton.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/self_new_order.png");
		m_oImageNewOrderButton.setEnabled(true);
		m_oImageNewOrderButton.allowClick(true);
		m_oFrameWholeCover.attachChild(m_oImageNewOrderButton);
		
		m_oImageCheckReviewButton = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageCheckReviewButton, "imgCheckReviewButton");
		m_oImageCheckReviewButton.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/self_new_order.png");
		m_oImageCheckReviewButton.setEnabled(true);
		m_oImageCheckReviewButton.allowClick(true);
		m_oFrameWholeCover.attachChild(m_oImageCheckReviewButton);
		
		m_oImageChgLangButton = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageChgLangButton, "imgChgLangButton");
		m_oImageChgLangButton.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/change_language.png");
		m_oImageChgLangButton.setEnabled(true);
		m_oImageChgLangButton.allowClick(true);
		m_oFrameWholeCover.attachChild(m_oImageChgLangButton);
		
		m_oLabelNewOrder = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelNewOrder, "lblNewOrder");
		m_oLabelNewOrder.setEnabled(true);
		m_oLabelNewOrder.allowClick(true);
		m_oLabelNewOrder.setValue(AppGlobal.g_oLang.get()._("new_order", ""));
		m_oFrameWholeCover.attachChild(m_oLabelNewOrder);
		
		m_oLabelCheckReview = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckReview, "lblCheckReivew");
		m_oLabelCheckReview.setEnabled(true);
		m_oLabelCheckReview.allowClick(true);
		m_oLabelCheckReview.setValue(AppGlobal.g_oLang.get()._("check_review", ""));
		m_oFrameWholeCover.attachChild(m_oLabelCheckReview);
		
		m_iDefaultTableNo = iDefaultTableNo;
   	}
   	
   	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		// Find the clicked button
		if (m_oImageNewOrderButton.getId() == iChildId || m_oLabelNewOrder.getId() == iChildId) {
			for (FrameMenuModeListener listener : listeners) {
    			// Raise the event to parent
    	       	listener.frameTableFloorPlan_MenuModeNewOrder(m_iDefaultTableNo);
    		}
			bMatchChild = true;
        }else
    	if (m_oImageCheckReviewButton.getId() == iChildId || m_oLabelCheckReview.getId() == iChildId) {
    		for (FrameMenuModeListener listener : listeners) {
    			// Raise the event to parent
    	       	listener.frameTableFloorPlan_MenuModeCheckReview(m_iDefaultTableNo);
    		}
			bMatchChild = true;
        }
		else
		if (m_oImageChgLangButton.getId() == iChildId) {
        	for (FrameMenuModeListener listener : listeners) {
    			// Raise the event to parent
    	       	listener.frameTableFloorPlan_MenuModeChangeLanguage();
    		}
			bMatchChild = true;
        }
    	return bMatchChild;
    }
}

