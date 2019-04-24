package app;

import java.util.ArrayList;
import java.util.List;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIHorizontalList;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameHorizontalTabListListener {
	void frameHorizontalTabList_clickTab(int iTabIndex, int iId);
}

public class FrameHorizontalTabList extends VirtualUIFrame {
	TemplateBuilder m_oTemplateBuilder;
	private VirtualUIHorizontalList m_oHorizontalTabList;
	private List<VirtualUIFrame> m_oFramePageTabArray;
	
	private String m_sTabLabelSelectedBackgroundColor;
	private String m_sTabLabelUnselectedBackgroundColor;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameHorizontalTabListListener> listeners;
	// label background label
	public static String LABEL_BG_COLOR_SELECTED = "#FFFFFF";
	public static String LABEL_BG_COLOR_UNSELECTED = "#66a1c1";

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameHorizontalTabListListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameHorizontalTabListListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FrameHorizontalTabList() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameHorizontalTabListListener>();
		m_oHorizontalTabList = new VirtualUIHorizontalList();
		m_oFramePageTabArray = new ArrayList<VirtualUIFrame>();
		
		m_sTabLabelSelectedBackgroundColor = LABEL_BG_COLOR_SELECTED;
		m_sTabLabelUnselectedBackgroundColor = LABEL_BG_COLOR_UNSELECTED;
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraOrderingPanel.xml");
	}
	
	public void init() {
		// page separator
		VirtualUIFrame oFramePanelPageSeparator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFramePanelPageSeparator, "fraPageTabSeparator");
		oFramePanelPageSeparator.setWidth(this.getWidth() - (oFramePanelPageSeparator.getLeft()*2));
		oFramePanelPageSeparator.setTop(this.getHeight() - oFramePanelPageSeparator.getHeight());
		this.attachChild(oFramePanelPageSeparator);
		
		// horizontal list
		m_oTemplateBuilder.buildHorizontalList(m_oHorizontalTabList, "horListTab");
		m_oHorizontalTabList.setWidth(this.getWidth() - (m_oHorizontalTabList.getLeft()*2));
		m_oHorizontalTabList.setHeight(this.getHeight() - oFramePanelPageSeparator.getHeight() - 10);
		this.attachChild(m_oHorizontalTabList);
	}
	
	public void addPageTabs(List<String> oTabNameList) {
		for (String sTabName: oTabNameList) {
			addPageTab(sTabName);
		}
		
		changePageTab(0);
	}
	
	private void addPageTab(String sTabName) {
		//Create single Tab base frame
		VirtualUIFrame fraPageTabBase = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(fraPageTabBase, "fraPageTabBase");
		fraPageTabBase.setHeight(m_oHorizontalTabList.getHeight());
		fraPageTabBase.allowClick(true);
		fraPageTabBase.allowLongClick(true);
		
		//Create Underline frame
		VirtualUIFrame fraPageTabUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(fraPageTabUnderline, "fraPageTabUnderline");
		fraPageTabUnderline.setHeight(fraPageTabBase.getHeight());
		fraPageTabUnderline.setVisible(false);
		fraPageTabBase.attachChild(fraPageTabUnderline);

		//Create Tab Name label
		VirtualUILabel lblPageTab = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(lblPageTab, "lblPageTab");
		lblPageTab.setHeight(fraPageTabBase.getHeight());
		lblPageTab.setValue(sTabName);
		fraPageTabBase.attachChild(lblPageTab);
		
		//Add PanelName Listener
		m_oHorizontalTabList.attachChild(fraPageTabBase);
		m_oFramePageTabArray.add(fraPageTabBase);
		
		m_sTabLabelSelectedBackgroundColor = lblPageTab.getForegroundColor();
		
	}

	public void changePageTab(int iSelectedPageTab) {
		if(m_oFramePageTabArray.size() - 1 < iSelectedPageTab)
			iSelectedPageTab = m_oFramePageTabArray.size() - 1;
		
		for (int i = 0; i < m_oFramePageTabArray.size(); i++) {
			VirtualUIFrame oTabBaseFrame = m_oFramePageTabArray.get(i);
			if (i == iSelectedPageTab) {
				for (VirtualUIBasicElement oChild : oTabBaseFrame.getChilds()) {
					if (oChild.getUIType() == HeroActionProtocol.View.Type.LABEL) {
						VirtualUILabel oLabel = (VirtualUILabel)oChild;
						//Set Tab Label background color
						oLabel.setForegroundColor(m_sTabLabelSelectedBackgroundColor);
					} else if (oChild.getUIType() == HeroActionProtocol.View.Type.FRAME) {
						VirtualUIFrame oUnderlineFrame = (VirtualUIFrame)oChild;
						oUnderlineFrame.setVisible(true);
					}
				}
			} else {
				for (VirtualUIBasicElement oChild : oTabBaseFrame.getChilds()) {
					if (oChild.getUIType() == HeroActionProtocol.View.Type.LABEL) {
						VirtualUILabel oLabel = (VirtualUILabel)oChild;
						//Set Tab Label background color
						oLabel.setForegroundColor(m_sTabLabelUnselectedBackgroundColor);
					} else if (oChild.getUIType() == HeroActionProtocol.View.Type.FRAME) {
						VirtualUIFrame oUnderlineFrame = (VirtualUIFrame)oChild;
						oUnderlineFrame.setVisible(false);
					}
				}
			}
		}
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {

		boolean bMatchChild = false;

		for (int i = 0; i < m_oFramePageTabArray.size(); i++) {
			if (m_oFramePageTabArray.get(i).getId() == iChildId) {
				changePageTab(i);
				for (FrameHorizontalTabListListener listener: listeners) {
					listener.frameHorizontalTabList_clickTab(i, this.getId());
				}

				bMatchChild = true;
				break;
			}
		}
		
		return bMatchChild;
	}
}
