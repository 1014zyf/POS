package app.commonui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import core.templatebuilder.TemplateBuilder;
import core.virtualui.HeroActionProtocol;
import core.virtualui.VirtualUIButton;
import core.virtualui.VirtualUIFrame;
import core.virtualui.VirtualUILabel;
import core.virtualui.VirtualUIList;

/** interface for the listeners/observers callback method */
interface FrameSelectionBoxListener {
	void FrameSelectionBox_LabelSelected(int iOptIndex);
	void FrameSelectionBox_ButtonClicked(int iId, String sValue);
}

public class FrameSelectionBox extends VirtualUIFrame {
	
	TemplateBuilder m_oTemplateBuilder;
	private ArrayList<VirtualUIFrame> m_oLabelOptArray;
	private HashMap<String, VirtualUIButton> m_oAddedButtons;
	
	private VirtualUILabel m_oLabelTitle;
	private VirtualUIList m_oListSelection;
	
	private String m_oListOptionTextAlign;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameSelectionBoxListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameSelectionBoxListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameSelectionBoxListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }

    public void init(){	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameSelectionBoxListener>();
		m_oLabelOptArray = new ArrayList<VirtualUIFrame>();
		m_oAddedButtons = new HashMap<String, VirtualUIButton>();
		
		m_oListOptionTextAlign = HeroActionProtocol.View.Attribute.TextAlign.CENTER;
		
		// Load child elements from template
		m_oTemplateBuilder.loadTemplate("fraSelectionBox.xml");
		
		// SelectionBox Title
		m_oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTitle, "lblTitle");
		m_oLabelTitle.setWidth(this.getWidth() - (m_oLabelTitle.getLeft() * 2));
		this.attachChild(m_oLabelTitle);
		
		// SelectionBox List
		m_oListSelection = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListSelection, "listTable");
		m_oListSelection.setWidth(this.getWidth()-10);
		m_oListSelection.setLeft(5);
		this.attachChild(m_oListSelection);
    }
    
    public void addButton(String sButtonDesc, String sKey) {
    	int iBtnSpace = 4;
    	VirtualUIButton oBtn = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(oBtn, "btn");
		oBtn.setValue(sButtonDesc);
		oBtn.setTop(this.getHeight());
		oBtn.setLeft((this.getWidth()/2) - (oBtn.getWidth()/2));
		oBtn.setClickServerRequestBlockUI(false);
		this.attachChild(oBtn);
		
		this.setHeight(this.getHeight()+oBtn.getHeight()+iBtnSpace);
		
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
    	VirtualUIFrame oFrameOption = new VirtualUIFrame();
    	m_oTemplateBuilder.buildFrame(oFrameOption, "listOption");
    	oFrameOption.setWidth(this.getWidth() - (oFrameOption.getLeft() * 2));
    	
    	VirtualUILabel oLblOptionLabel = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(oLblOptionLabel, "listlblOption");
    	oLblOptionLabel.setWidth(oFrameOption.getWidth() - oLblOptionLabel.getLeft()*2);
    	oLblOptionLabel.setTextAlign(m_oListOptionTextAlign);
    	oLblOptionLabel.setValue(sOptionValue);
    	oFrameOption.attachChild(oLblOptionLabel);
    	
    	oFrameOption.setEnabled(true);
    	oFrameOption.allowClick(true);
    	
    	m_oLabelOptArray.add(oFrameOption);
    	m_oListSelection.attachChild(oFrameOption);
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
    
    public void setOptionTextAlign(String sAlign) {
    	m_oListOptionTextAlign = sAlign;
    }
    
    public int getOptionListSize() {
    	return m_oLabelOptArray.size();
    }
    
    public void selectAllOptions() {
    	String sBackgroundColor = "#04FF00";
    	
    	for (VirtualUIFrame oFrame : m_oLabelOptArray) {
    		oFrame.setBackgroundColor(sBackgroundColor);
    	}
    }
    
    public void selectOption(int iOptIndex) {
    	String sBackgroundColor = "#04FF00";
    	
    	VirtualUIFrame oFrame = m_oLabelOptArray.get(iOptIndex);
    	oFrame.setBackgroundColor(sBackgroundColor);
    }

    public void unselectAllOptions() {
    	String sBackgroundColor = "#817878";
    	
    	for (VirtualUIFrame oFrame : m_oLabelOptArray) {
    		oFrame.setBackgroundColor(sBackgroundColor);
    	}
    }
    
    public void unselectOption(int iOptIndex) {
    	String sBackgroundColor = "#817878";
    	
    	VirtualUIFrame oFrame = m_oLabelOptArray.get(iOptIndex);
    	oFrame.setBackgroundColor(sBackgroundColor);
    }
    
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
    	
		// Find the clicked button
		for(Map.Entry<String, VirtualUIButton> entry:m_oAddedButtons.entrySet()){
			VirtualUIButton oBtn = entry.getValue();
			if (oBtn.getId() == iChildId) {
				// Raise the event to parent
				for (FrameSelectionBoxListener listener : listeners) {
					listener.FrameSelectionBox_ButtonClicked(iChildId, entry.getKey());
				}
	       		bMatchChild = true;
	       		break;
			}
		}
		if(bMatchChild == false){
			// Find the clicked label
			for (int iCount=0; iCount<m_oLabelOptArray.size(); iCount++) {
				VirtualUIFrame oFrame = m_oLabelOptArray.get(iCount);
				if (oFrame.getId() == iChildId) {
					// Raise the event to parent
					for (FrameSelectionBoxListener listener : listeners) {
						listener.FrameSelectionBox_LabelSelected(iCount);
					}
		       		bMatchChild = true;
		       		break;
				}
			}
		}
    	
    	return bMatchChild;
    }
}
