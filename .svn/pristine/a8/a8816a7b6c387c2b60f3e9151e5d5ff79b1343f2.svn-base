package app;

import java.util.ArrayList;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

interface FrameSelectOrderingTypeListener {
    void FrameSelectOrderingType_fineDining();
    void FrameSelectOrderingType_takeaway();
    void FrameSelectOrderingType_changeLanguage();
}

public class FrameSelectOrderingType extends VirtualUIFrame{
	TemplateBuilder m_oTemplateBuilder;
	VirtualUIImage m_oImageBackground;
	VirtualUIFrame m_oFrameWholeCover;
	VirtualUIFrame m_oFrameFineDining;
	VirtualUIImage m_oImageFineDining;
	VirtualUILabel m_oLabelFineDining;
	VirtualUIFrame m_oFrameTakeAway;
	VirtualUIImage m_oImageTakeAway;
	VirtualUILabel m_oLabelTakeAway;
	VirtualUIFrame m_oFrameChgLang;
	VirtualUIImage m_oImageChgLang;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameSelectOrderingTypeListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameSelectOrderingTypeListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameSelectOrderingTypeListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	//Constructor
	public FrameSelectOrderingType() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameSelectOrderingTypeListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraSelectOrderingType.xml");
		
		// Whole cover frame
		m_oFrameWholeCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameWholeCover, "fraWholeCover");
		this.attachChild(m_oFrameWholeCover);
		
		// Background Image
		VirtualUIImage oImageBackground = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(oImageBackground, "fraBackgroundImage");
		oImageBackground.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/backgrounds/menu_mode_bg.png");
		oImageBackground.setEnabled(true);
		m_oFrameWholeCover.attachChild(oImageBackground);
		
		// Select Serving Type Label
		VirtualUILabel oLabelSelectOrdering = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelSelectOrdering, "lblSelectType");
		oLabelSelectOrdering.setEnabled(true);
		oLabelSelectOrdering.setValue(AppGlobal.g_oLang.get()._("please_select_serving_type") + ":");
				
		//	AppGlobal.g_oLang.get()._("please_select_serving_type", ""));
		m_oFrameWholeCover.attachChild(oLabelSelectOrdering);
		
		// Fine Dining Frame
		m_oFrameFineDining = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameFineDining, "fraFineDining");
		m_oFrameFineDining.allowClick(true);
		
		// Fine Dining Image
		m_oImageFineDining = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageFineDining, "imgFineDining");
		m_oImageFineDining.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/new_order.png");
		m_oImageFineDining.setEnabled(true);
		m_oImageFineDining.allowClick(true);
		
		m_oFrameFineDining.attachChild(m_oImageFineDining);
		
		// Fine Dining Label
		m_oLabelFineDining = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelFineDining, "lblFineDining");
		m_oLabelFineDining.setEnabled(true);
		m_oLabelFineDining.allowClick(true);
		m_oLabelFineDining.setValue(AppGlobal.g_oLang.get()._("fine_dining", ""));
		m_oFrameFineDining.attachChild(m_oLabelFineDining);
		
		m_oFrameWholeCover.attachChild(m_oFrameFineDining);
		
		// Take Away Button
		m_oFrameTakeAway = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTakeAway, "fraTakeAway");
		m_oFrameTakeAway.setEnabled(true);
		m_oFrameTakeAway.allowClick(true);
		
		// Take Away Image
		m_oImageTakeAway = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageTakeAway, "imgTakeAway");
		m_oImageTakeAway.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/new_order.png");
		m_oImageTakeAway.setEnabled(true);
		m_oImageTakeAway.allowClick(true);
		m_oFrameTakeAway.attachChild(m_oImageTakeAway);
		
		// Take Away Label
		m_oLabelTakeAway = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTakeAway, "lblTakeAway");
		m_oLabelTakeAway.setEnabled(true);
		m_oLabelTakeAway.allowClick(true);
		m_oLabelTakeAway.setValue(AppGlobal.g_oLang.get()._("takeaway", ""));
		m_oFrameTakeAway.attachChild(m_oLabelTakeAway);
		
		m_oFrameWholeCover.attachChild(m_oFrameTakeAway);
		
		
		//Change Language Frame
		m_oFrameChgLang = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameChgLang, "fraChgLang");
		m_oFrameChgLang.setEnabled(true);
		m_oFrameChgLang.allowClick(true);
		
		//Change Language Button
		m_oImageChgLang = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageChgLang, "imgChgLang");
		m_oImageChgLang.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/change_language.png");
		m_oImageChgLang.setEnabled(true);
		m_oImageChgLang.allowClick(true);
		
		m_oFrameChgLang.attachChild(m_oImageChgLang);
		
		m_oFrameWholeCover.attachChild(m_oFrameChgLang);
		
	}
	
	public void changeLanguage(int iIndex) {
		
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		
		// Find the clicked button
		if (m_oFrameFineDining.getId() == iChildId || m_oImageFineDining.getId() == iChildId || m_oLabelFineDining.getId() == iChildId) {
			for (FrameSelectOrderingTypeListener listener : listeners) {
				// Raise the event to parent
				listener.FrameSelectOrderingType_fineDining();
			}
			bMatchChild = true;
		}else if (m_oFrameTakeAway.getId() == iChildId || m_oImageTakeAway.getId() == iChildId || m_oLabelTakeAway.getId() == iChildId) {
			for (FrameSelectOrderingTypeListener listener : listeners) {
				// Raise the event to parent
				listener.FrameSelectOrderingType_takeaway();
			}
			bMatchChild = true;
		}else if (m_oFrameChgLang.getId() == iChildId || m_oImageChgLang.getId() == iChildId) {
			for (FrameSelectOrderingTypeListener listener : listeners) {
				// Raise the event to parent
				listener.FrameSelectOrderingType_changeLanguage();
			}
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
}
