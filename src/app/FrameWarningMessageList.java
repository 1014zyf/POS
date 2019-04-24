package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameWarningMessageListListener {
	void frameWarningMessageList_ButtonExitClicked();
	void frameWarningMessageList_ButtonClickRefresh(String sPanelType, int iWarningMessageListingType);
}

public class FrameWarningMessageList extends VirtualUIFrame implements FrameTitleHeaderListener, FrameWarningMessageListPanelListener  {
	// for normal check listing function
	public static int PANEL_PRINTER_STATUS = 0;
	
	// UI Element
	private TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oTitleHeader;
	private VirtualUIFrame m_oFrameContent;
	private FrameCommonPageContainer m_oCommonPageTabList;
	private VirtualUILabel m_oCommonPageSelectedTabList;
	private VirtualUILabel m_oCommonPageUnselectTabList;
	private FrameWarningMessageListPanel m_oFrameWarningMessageListPanel;
	private List<VirtualUIFrame> m_oFramePanelTab;
	private HashMap<Integer, FrameWarningMessageListPanel> m_oWarningMessageListPanelList;
	
	// Others
	private String m_sTabLabelBackground = "";
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameWarningMessageListListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameWarningMessageListListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameWarningMessageListListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameWarningMessageList () {
		m_oWarningMessageListPanelList = new HashMap<Integer, FrameWarningMessageListPanel>();
		m_oFramePanelTab = new ArrayList<VirtualUIFrame>();
		this.listeners = new ArrayList<FrameWarningMessageListListener>();
		
		m_oTemplateBuilder = new TemplateBuilder();

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraWarningMessageList.xml");

		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("warning_message_list"));
		m_oTitleHeader.addListener(this);
		this.attachChild(m_oTitleHeader);

		m_oFrameContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameContent, "fraContent");
		this.attachChild(m_oFrameContent);
		
		m_oCommonPageSelectedTabList = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oCommonPageSelectedTabList, "fraSelectedListTab");
		
		m_oCommonPageUnselectTabList = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oCommonPageUnselectTabList, "fraUnselectListTab");
		
		// CheckListPanel lookup list - Frame Common Page Container
		m_oCommonPageTabList = new FrameCommonPageContainer();
		m_oTemplateBuilder.buildFrame(m_oCommonPageTabList, "fraListTab");
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.horizontal_desktop.name())){
			m_oCommonPageTabList.init(m_oCommonPageTabList.getWidth(), m_oCommonPageTabList.getHeight(), m_oCommonPageSelectedTabList.getWidth(), 
				m_oCommonPageSelectedTabList.getHeight(), 5, m_oCommonPageSelectedTabList.getForegroundColor(), 
				m_oCommonPageUnselectTabList.getForegroundColor(), m_oCommonPageSelectedTabList.getBackgroundColor(), 
				m_oCommonPageUnselectTabList.getBackgroundColor(), 0, true, true);
			m_oCommonPageTabList.setTagTextSize(m_oCommonPageSelectedTabList.getTextSize(), m_oCommonPageUnselectTabList.getTextSize());
		}
		else
			m_oCommonPageTabList.init(m_oCommonPageTabList.getWidth(), m_oCommonPageTabList.getHeight(), m_oCommonPageSelectedTabList.getWidth(), 
					m_oCommonPageSelectedTabList.getHeight(), 5, m_oCommonPageSelectedTabList.getForegroundColor(), 
					m_oCommonPageUnselectTabList.getForegroundColor(), "#015384", "#00000000", 0, false, false);
		
		m_oCommonPageTabList.setUnderlineColor(m_oCommonPageUnselectTabList.getForegroundColor());
		m_oCommonPageTabList.setUpperlineColor(m_oCommonPageUnselectTabList.getForegroundColor());
		m_oFrameContent.attachChild(m_oCommonPageTabList);
	}
	
	public void addWarningMessageListingTab(int iPanelId, String sPanelListValue) {
		VirtualUILabel oLblPanelLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblPanelLabel, "lblPanelTab");
		oLblPanelLabel.setValue(sPanelListValue);
		oLblPanelLabel.setEnabled(true);
		oLblPanelLabel.allowClick(false);

		addWarningMessageListPanel(iPanelId);
		
		//prepare the common basket
		for (int iListPanelIndex = 0; iListPanelIndex < m_oWarningMessageListPanelList.size(); iListPanelIndex++) {
			FrameWarningMessageListPanel oWarningMessageListPanel = m_oWarningMessageListPanelList.get(iListPanelIndex);

			JSONArray oWarningMessageListJSONArray = null;
			if(!oWarningMessageListPanel.loadedRecord()) {
				if (iListPanelIndex == FrameWarningMessageList.PANEL_PRINTER_STATUS) {
					try {
						oWarningMessageListJSONArray = new JSONArray("");
						} catch (Exception e) {
							AppGlobal.stackToString(e);
						}
				}
				if(oWarningMessageListJSONArray != null)
					addRecordToFrameCheckListPanel(iListPanelIndex, oWarningMessageListJSONArray);
			}
			changeSelectedListingTab(iListPanelIndex);
		}
		m_oCommonPageTabList.addButton(sPanelListValue, m_oFrameWarningMessageListPanel);
		
		m_sTabLabelBackground = oLblPanelLabel.getForegroundColor();
	}
	
	public void addWarningMessageListPanel(int iPanelId) {
		String sPanelType = FrameWarningMessageListPanel.TYPE_PRINTER_STATUS;

		if (iPanelId == FrameWarningMessageList.PANEL_PRINTER_STATUS)
			sPanelType = FrameWarningMessageListPanel.TYPE_PRINTER_STATUS;

		m_oFrameWarningMessageListPanel = new FrameWarningMessageListPanel(sPanelType, iPanelId);
		
		m_oFrameWarningMessageListPanel.addWarningMessageListPanelTitle(sPanelType);
		
		m_oTemplateBuilder.buildFrame(m_oFrameWarningMessageListPanel, "scrfraWarningMessageList");
	
		m_oWarningMessageListPanelList.put(iPanelId, m_oFrameWarningMessageListPanel);
		
		m_oFrameWarningMessageListPanel.setVisible(false);
		
		// Add listener
		m_oFrameWarningMessageListPanel.addListener(this);
		
		m_oFrameContent.attachChild(m_oFrameWarningMessageListPanel);
	}

	public void addRecordToFrameCheckListPanel(int iPanelId, JSONArray oWarningMessageListJSONArray) {
		FrameWarningMessageListPanel oFrameWarningMessageListPanel = m_oWarningMessageListPanelList.get(iPanelId);
		oFrameWarningMessageListPanel.initWarningList(iPanelId, oWarningMessageListJSONArray);
	}

	public void changeSelectedListingTab(int iSelectedPanelTab) {
		// hide all subMenu lookup
		for (Entry<Integer, FrameWarningMessageListPanel> entry : m_oWarningMessageListPanelList.entrySet()) {
			if (entry.getValue().getVisible()) {
				entry.getValue().setVisible(false);
			}
		}

		// show the selected setMenu lookup
		FrameWarningMessageListPanel oSelectedFrameWarningMessageList = m_oWarningMessageListPanelList.get(iSelectedPanelTab);
		oSelectedFrameWarningMessageList.setVisible(true);
		oSelectedFrameWarningMessageList.setEnabled(true);
		
		oSelectedFrameWarningMessageList.setDisplayList(iSelectedPanelTab, false);
		oSelectedFrameWarningMessageList.updateWarningMessageListRecord();
	}
	
	// Get set Function
	public void setTitle(String sTitle) {
		m_oTitleHeader.setTitle(sTitle);
	}


	@Override
	public void frameWarningMessageListPanel_ButtonClickRefresh(String sPanelType, int iWarningMessageListingType) {
		for (FrameWarningMessageListListener listener : listeners) {
			listener.frameWarningMessageList_ButtonClickRefresh(sPanelType, iWarningMessageListingType);
		}
	}
	
	// Call Back Function
	@Override
	public void FrameTitleHeader_close() {
		for (FrameWarningMessageListListener listener : listeners) {
			listener.frameWarningMessageList_ButtonExitClicked();
		}
	}
}
