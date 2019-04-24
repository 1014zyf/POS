package commonui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;

public class FrameOptionBox extends VirtualUIFrame{
	/** interface for the listeners/observers callback method */
	public interface FrameOptionBoxListener {
		void frameOptionBox_LabelSelected(int iOptIndex);
		void frameOptionBox_ButtonClicked(int iId, String sValue);
	}
	
	TemplateBuilder m_oTemplateBuilder;
	private ArrayList<VirtualUIFrame> m_oLabelOptArray;
	private ArrayList<JSONObject> m_oOptKeyArray;
	private HashMap<String, VirtualUIButton> m_oAddedButtons;
	
	private VirtualUIFrame m_oBaseFrame;
	private VirtualUIList m_oListSelection;
	private String m_oListOptionTextAlign;
	
	private FrameTitleHeader m_oTitleHeader;
	
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
		m_oLabelOptArray = new ArrayList<VirtualUIFrame>();
		
		m_oOptKeyArray = new ArrayList<JSONObject>();
		m_oAddedButtons = new HashMap<String, VirtualUIButton>();
		
		m_oListOptionTextAlign = HeroActionProtocol.View.Attribute.TextAlign.CENTER;
		
		// Load child elements from template
		m_oTemplateBuilder.loadTemplate("fraOptionBox.xml");
		
		m_oBaseFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oBaseFrame, "fraBase");
		this.attachChild(m_oBaseFrame);
		
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(false);
		m_oBaseFrame.attachChild(m_oTitleHeader);
				
		// SelectionBox List
		m_oListSelection = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListSelection, "listTable");
		
		m_oListSelection.setWidth(m_oBaseFrame.getWidth() - 48);
		
		m_oBaseFrame.attachChild(m_oListSelection);
	}
	
	public void addButton(String sButtonDesc, String sKey) {
		VirtualUIButton oBtn = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(oBtn, "btn");
		oBtn.setValue(sButtonDesc);
		
		oBtn.setLeft((m_oBaseFrame.getWidth()/2) - (oBtn.getWidth()/2));
		oBtn.setClickServerRequestBlockUI(false);
		m_oBaseFrame.attachChild(oBtn);
		
		m_oAddedButtons.put(sKey, oBtn);
	}
	
	public void addHalfButton(String sButtonValue, String sKey) {
		int iBtnSpace = 4;
		VirtualUIButton oBtn = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(oBtn, "btnHalf");
		oBtn.setValue(sButtonValue);
		
		if (m_oAddedButtons.size() % 2 != 0)
			oBtn.setLeft(oBtn.getWidth() + iBtnSpace * 2);
		
		m_oAddedButtons.put(sKey, oBtn);
		oBtn.setClickServerRequestBlockUI(false);
		this.attachChild(oBtn);
	}
	
	public void addOption(String sOptionValue) {
		VirtualUIFrame oFrameOption = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameOption, "listOption");
		oFrameOption.setWidth(this.getWidth() - 48);
		
		VirtualUILabel oLblOptionLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblOptionLabel, "listlblOption");
		oLblOptionLabel.setWidth(oFrameOption.getWidth());
		oLblOptionLabel.setTextAlign(m_oListOptionTextAlign);
		oLblOptionLabel.setValue(sOptionValue);
		oFrameOption.attachChild(oLblOptionLabel);
		
		oFrameOption.setEnabled(true);
		oFrameOption.allowClick(true);
		
		m_oLabelOptArray.add(oFrameOption);
		m_oOptKeyArray.add(null);
		m_oListSelection.attachChild(oFrameOption);
	}
	
	public void addOption(String sOptionValue, JSONObject oKeyValueJSONObject) {
		VirtualUIFrame oFrameOption = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameOption, "listOption");
		oFrameOption.setWidth(this.getWidth() - 48);
		
		VirtualUILabel oLblOptionLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblOptionLabel, "listlblOption");
		oLblOptionLabel.setWidth(oFrameOption.getWidth());
		oLblOptionLabel.setTextAlign(m_oListOptionTextAlign);
		oLblOptionLabel.setValue(sOptionValue);
		oFrameOption.attachChild(oLblOptionLabel);
		
		oFrameOption.setEnabled(true);
		oFrameOption.allowClick(true);
		
		m_oLabelOptArray.add(oFrameOption);
		m_oOptKeyArray.add(oKeyValueJSONObject);
		m_oListSelection.attachChild(oFrameOption);
	}
	
	public void setTitle(String sValue) {
		m_oTitleHeader.setTitle(sValue);
	}
	
	public void setButtonDesc(int iId, String sValue) {
		for (Map.Entry<String, VirtualUIButton> entry : m_oAddedButtons.entrySet()) {
			VirtualUIButton oBtn = entry.getValue();
			if (oBtn.getId() == iId) {
				oBtn.setValue(sValue);
				break;
			}
		}
	}
	
	public void removeOptionList() {
		for (VirtualUIFrame oFraOptionLabel : m_oLabelOptArray)
			m_oListSelection.removeChild(oFraOptionLabel.getId());
		
		m_oLabelOptArray.clear();
		m_oOptKeyArray.clear();
	}
	
	public int getOptionListSize() {
		return m_oLabelOptArray.size();
	}
	
	public void selectAllOptions() {
		String sBackgroundColor = "#04FF00";
		
		for (VirtualUIFrame oFraLabel : m_oLabelOptArray)
			oFraLabel.setBackgroundColor(sBackgroundColor);
	}
	
	public void selectOption(int iOptIndex) {
		String sBackgroundColor = "#04FF00";
		
		VirtualUIFrame oFraLabel = m_oLabelOptArray.get(iOptIndex);
		oFraLabel.setBackgroundColor(sBackgroundColor);
	}
	
	public void unselectAllOptions() {
		String sBackgroundColor = "#817878";
		
		for (VirtualUIFrame oFraLabel : m_oLabelOptArray)
			oFraLabel.setBackgroundColor(sBackgroundColor);
	}
	
	public void unselectOption(int iOptIndex) {
		String sBackgroundColor = "#817878";
		
		VirtualUIFrame oFraLabel = m_oLabelOptArray.get(iOptIndex);
		oFraLabel.setBackgroundColor(sBackgroundColor);
		
	}
	
	public JSONObject getOptKey(int iIndex) {
		if (m_oOptKeyArray.size() <= iIndex)
			return null;
		else
			return m_oOptKeyArray.get(iIndex);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		// Find the clicked button
		for (Map.Entry<String, VirtualUIButton> entry : m_oAddedButtons.entrySet()) {
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
		if (bMatchChild == false) {
			// Find the clicked label
			for (int iCount = 0; iCount < m_oLabelOptArray.size(); iCount++) {
				VirtualUIFrame oFraLabel = m_oLabelOptArray.get(iCount);
				if (oFraLabel.getId() == iChildId) {
					// Raise the event to parent
					for (FrameOptionBoxListener listener : listeners)
						listener.frameOptionBox_LabelSelected(iCount);
					
					bMatchChild = true;
					break;
				}
			}
		}
		
		return bMatchChild;
	}
}
