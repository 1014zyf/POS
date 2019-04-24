package app;

import java.util.ArrayList;

import commonui.FrameTitleHeader;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;

/** interface for the listeners/observers callback method */
interface FrameSmartStationControlListener {
	void FrameSmartStationControl_LabelSelected(int iOptIndex);
	void FrameSmartStationControl_DetectRoleManagerNextAction();
}

public class FrameSmartStationControl extends VirtualUIFrame {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private ArrayList<VirtualUIFrame> m_oLabelOptArray;
	
	private VirtualUIList m_oListSelection;
	
	private FrameTitleHeader m_oTitleHeader;
	private VirtualUILabel m_oLabelMessage;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameSmartStationControlListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameSmartStationControlListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameSmartStationControlListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FrameSmartStationControl() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameSmartStationControlListener>();
		m_oLabelOptArray = new ArrayList<VirtualUIFrame>();
		
		// Load child elements from template
		m_oTemplateBuilder.loadTemplate("fraSmartStationControl.xml");
		
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(false);
		this.attachChild(m_oTitleHeader);
		
		// Message
		m_oLabelMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMessage, "lblMessage");
		this.attachChild(m_oLabelMessage);
		
		// SelectionBox List
		m_oListSelection = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListSelection, "listTable");
		this.attachChild(m_oListSelection);
		
		// Add checking timer
		this.addTimer("detect_role_manager_next_action", 1000, true, "detect_role_manager_next_action", false, true, null);
		this.controlTimer("detect_role_manager_next_action", true);
	}
	
	public void addOption(String sOptionValue) {
		VirtualUIFrame oFrameOption = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameOption, "listOption");
		oFrameOption.setWidth(this.getWidth() - 48);
		
		VirtualUILabel oLblOptionLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblOptionLabel, "listlblOption");
		oLblOptionLabel.setWidth(oFrameOption.getWidth());
		oLblOptionLabel.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		oLblOptionLabel.setValue(sOptionValue);
		oFrameOption.attachChild(oLblOptionLabel);
		
		oFrameOption.setEnabled(true);
		oFrameOption.allowClick(true);
		
		m_oLabelOptArray.add(oFrameOption);
		m_oListSelection.attachChild(oFrameOption);
	}
	
	public void clearOption() {
		m_oListSelection.removeAllChildren();
	}
	
	public void setTitle(String sValue) {
		m_oTitleHeader.setTitle(sValue);
	}
	
	public void setMessage(String sMessage) {
		m_oLabelMessage.setValue(sMessage);
	}
	
	public int getOptionListSize() {
		return m_oLabelOptArray.size();
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		// Find the clicked label
		for (int iCount=0; iCount<m_oLabelOptArray.size(); iCount++) {
			VirtualUIFrame oFrame = m_oLabelOptArray.get(iCount);
			if (oFrame.getId() == iChildId) {
				// Raise the event to parent
				for (FrameSmartStationControlListener listener : listeners) {
					listener.FrameSmartStationControl_LabelSelected(iCount);
				}
				bMatchChild = true;
				break;
			}
		}
		
		return bMatchChild;
	}
	
	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if(iId == super.getIDForPosting().hashCode()) {
			if (sNote.equals("detect_role_manager_next_action")){
				// Detect role manager next action
				
				//Set the last client socket ID
				AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
				
				for (FrameSmartStationControlListener listener : listeners) {
					listener.FrameSmartStationControl_DetectRoleManagerNextAction();
				}
				
				// Send the UI packet to client and the thread is finished
				super.getParentForm().finishUI(true);
			}
			return true;
		}
		return false;
	}
}
