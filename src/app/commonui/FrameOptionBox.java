package app.commonui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import core.templatebuilder.TemplateBuilder;
import core.virtualui.VirtualUIButton;
import core.virtualui.VirtualUIFrame;
import core.virtualui.VirtualUILabel;
import core.virtualui.VirtualUIList;

public class FrameOptionBox extends VirtualUIFrame{
	/** interface for the listeners/observers callback method */
	public interface FrameOptionBoxListener {
		void frameOptionBox_LabelSelected(int iOptIndex);
		void frameOptionBox_ButtonClicked(int iId, String sValue);
	}
	
	TemplateBuilder m_oTemplateBuilder;
	private ArrayList<VirtualUILabel> m_oLabelOptArray;
	private HashMap<String, VirtualUIButton> m_oAddedButtons;
	
	private VirtualUIFrame m_oBaseFrame;
	private VirtualUILabel m_oLabelTitle;
	private VirtualUIList m_oListSelection;
	
	/** list of interested listeners (observers, same thing) */
    public ArrayList<FrameOptionBoxListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameOptionBoxListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameOptionBoxListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
    public FrameOptionBox() {	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameOptionBoxListener>();
		m_oLabelOptArray = new ArrayList<VirtualUILabel>();
		m_oAddedButtons = new HashMap<String, VirtualUIButton>();
		
		// Load child elements from template
		m_oTemplateBuilder.loadTemplate("fraOptionBox.xml");
		
		m_oBaseFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oBaseFrame, "fraBase");
		this.attachChild(m_oBaseFrame);
		
		// SelectionBox Title
		m_oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTitle, "lblTitle");
		m_oLabelTitle.setWidth(m_oBaseFrame.getWidth() - (m_oLabelTitle.getLeft() * 2));
		m_oBaseFrame.attachChild(m_oLabelTitle);
		
		// SelectionBox List
		m_oListSelection = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListSelection, "listTable");
		m_oListSelection.setWidth(m_oBaseFrame.getWidth() - (m_oLabelTitle.getLeft() * 2));
		m_oBaseFrame.attachChild(m_oListSelection);
    }
    
    public void addButton(String sButtonDesc, String sKey) {
    	int iBtnSpace = 4;
    	VirtualUIButton oBtn = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(oBtn, "btn");
		oBtn.setValue(sButtonDesc);
		oBtn.setTop(m_oBaseFrame.getHeight());
		oBtn.setLeft((m_oBaseFrame.getWidth()/2) - (oBtn.getWidth()/2));
		oBtn.setClickServerRequestBlockUI(false);
		m_oBaseFrame.attachChild(oBtn);
		
		m_oBaseFrame.setHeight(m_oBaseFrame.getHeight()+oBtn.getHeight()+iBtnSpace);
		
		m_oAddedButtons.put(sKey, oBtn);
    }
    
    public void addHalfButton(String sButtonValue, String sKey) {
    	int iBtnSpace = 4;
		VirtualUIButton oBtn = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(oBtn, "btnHalf");
		oBtn.setValue(sButtonValue);
		oBtn.setTop(this.getHeight() + oBtn.getHeight()*(m_oAddedButtons.size()/2));
		
		if (m_oAddedButtons.size() % 2 != 0) {
			oBtn.setLeft(oBtn.getWidth()+iBtnSpace*2);
			this.setHeight(this.getHeight()+oBtn.getHeight()+iBtnSpace);
		}
		
		m_oAddedButtons.put(sKey, oBtn);
		oBtn.setClickServerRequestBlockUI(false);
		this.attachChild(oBtn);
    }
    
    public void addOption(String sOptionValue) {
    	VirtualUILabel oLblOptionLabel = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(oLblOptionLabel, "listOption");
    	oLblOptionLabel.setValue(sOptionValue);
    	oLblOptionLabel.setWidth(m_oListSelection.getWidth() - ((oLblOptionLabel.getLeft() + this.getStroke()) * 2));
    	oLblOptionLabel.setEnabled(true);
    	oLblOptionLabel.allowClick(true);
    	
    	m_oLabelOptArray.add(oLblOptionLabel);
    	m_oListSelection.attachChild(oLblOptionLabel);
    }
    
    public void setTitle(String sValue) {
    	m_oLabelTitle.setValue(sValue);
    }
    
    public void setButtonDesc(int iId, String sValue) {
    	for(Map.Entry<String, VirtualUIButton> entry:m_oAddedButtons.entrySet()){
    		VirtualUIButton oBtn = entry.getValue();
    		if (oBtn.getId() == iId) {
    			oBtn.setValue(sValue);
    			break;
    		}
    	}
    }
    
    public void removeOptionList() {
    	for(VirtualUILabel oLblOptionLabel:m_oLabelOptArray) 
    		m_oListSelection.removeChild(oLblOptionLabel.getId());
    	
    	m_oLabelOptArray.clear();
    }
    
    public int getOptionListSize() {
    	return m_oLabelOptArray.size();
    }
    
    public void selectAllOptions() {
    	String sBackgroundColor = "#04FF00";
    	
    	for (VirtualUILabel oLblLabel : m_oLabelOptArray) {
    		oLblLabel.setBackgroundColor(sBackgroundColor);
    	}
    }
    
    public void selectOption(int iOptIndex) {
    	String sBackgroundColor = "#04FF00";
    	
    	VirtualUILabel oLblLabel = m_oLabelOptArray.get(iOptIndex);
    	oLblLabel.setBackgroundColor(sBackgroundColor);
    }

    public void unselectAllOptions() {
    	String sBackgroundColor = "#817878";
    	
    	for (VirtualUILabel oLblLabel : m_oLabelOptArray) {
    		oLblLabel.setBackgroundColor(sBackgroundColor);
    	}
    }
    
    public void unselectOption(int iOptIndex) {
    	String sBackgroundColor = "#817878";
    	
    	VirtualUILabel oLblLabel = m_oLabelOptArray.get(iOptIndex);
    	oLblLabel.setBackgroundColor(sBackgroundColor);
    }
    
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		// Find the clicked button
		for(Map.Entry<String, VirtualUIButton> entry:m_oAddedButtons.entrySet()){
			VirtualUIButton oBtn = entry.getValue();
			if (oBtn.getId() == iChildId) {
				// Raise the event to parent
				for (FrameOptionBoxListener listener : listeners) {
					listener.frameOptionBox_ButtonClicked(iChildId, entry.getKey());
				}
	       		bMatchChild = true;
	       		break;
			}
		}
		if(bMatchChild == false){
			// Find the clicked label
			for (int iCount=0; iCount<m_oLabelOptArray.size(); iCount++) {
				VirtualUILabel oLbl = m_oLabelOptArray.get(iCount);
				if (oLbl.getId() == iChildId) {
					// Raise the event to parent
					for (FrameOptionBoxListener listener : listeners) {
						listener.frameOptionBox_LabelSelected(iCount);
					}
		       		bMatchChild = true;
		       		break;
				}
			}
		}
    	
    	return bMatchChild;
    }
}
