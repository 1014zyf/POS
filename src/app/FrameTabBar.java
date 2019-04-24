package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIHorizontalList;
import virtualui.VirtualUIImage;

interface FrameTabBarListener {
	void frameTabBar_tabClicked(int iTabIndex);
}

public class FrameTabBar extends VirtualUIFrame {
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUIHorizontalList m_oHorizontalTabList;
	private List<HashMap<String, String>> m_oTabBtns;
	private List<VirtualUIFrame> m_oFrameTabArray;
	
	// Tab index 0:order item list, 1:ordering item list, 2:function panel
	int m_iSelectedTabIdx;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameTabBarListener> listeners;
    
    /** add a new ModelListener observer for this Model */
    public void addListener(FrameTabBarListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameTabBarListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
    // constructor
    public FrameTabBar() {
    	m_oTemplateBuilder = new TemplateBuilder();
    	listeners = new ArrayList<FrameTabBarListener>();
    	
    	m_oHorizontalTabList = new VirtualUIHorizontalList();
    	m_oTabBtns = new ArrayList<HashMap<String, String>>();
    	m_oFrameTabArray = new ArrayList<VirtualUIFrame>();
    	
    	// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraTabBar.xml");
		
		// Create the TabBar buttons
		ArrayList<HashMap<String, String>> oBtnArray = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> oBtn = new HashMap<String, String>();
		
		oBtn.put("path", "/icons/ordered_items_icon.png");
		oBtnArray.add(oBtn);
		oBtn = new HashMap<String, String>();
		oBtn.put("path", "/icons/menu_icon.png");
		oBtnArray.add(oBtn);
		oBtn = new HashMap<String, String>();
		oBtn.put("path", "/icons/function_icon.png");
		oBtnArray.add(oBtn);
		createTabsWithPage(oBtnArray);
		
		m_iSelectedTabIdx = -1;
    }
    
	public void createTabsWithPage(ArrayList<HashMap<String, String>> oBtnArray) {
		m_oTabBtns = oBtnArray;
		m_oTemplateBuilder.buildHorizontalList(m_oHorizontalTabList, "horListTab");
		
		for(HashMap<String, String> btn : m_oTabBtns) {
			
			//Create single Tab base frame
			VirtualUIFrame fraBtnTabBase = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(fraBtnTabBase, "fraBtnTabBase");
			fraBtnTabBase.setEnabled(true);
			fraBtnTabBase.allowClick(true);
			fraBtnTabBase.allowLongClick(true);
			fraBtnTabBase.setClickServerRequestNote(Integer.toString(m_oTabBtns.indexOf(btn)+1));
			fraBtnTabBase.setLongClickServerRequestNote(Integer.toString(m_oTabBtns.indexOf(btn)+1));
			fraBtnTabBase.setClickServerRequestBlockUI(false);
			fraBtnTabBase.setLongClickServerRequestBlockUI(false);
			//Create Tab Name Image	
			VirtualUIImage imgPageTab = new VirtualUIImage();
			imgPageTab.setWidth(fraBtnTabBase.getWidth());
			imgPageTab.setHeight(fraBtnTabBase.getHeight());
			imgPageTab.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_ASPECT_FIT_CENTER);
			imgPageTab.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + (String)btn.get("path"));
			fraBtnTabBase.attachChild(imgPageTab);
			
			//Add PanelName Listener
			m_oHorizontalTabList.attachChild(fraBtnTabBase);
			m_oFrameTabArray.add(fraBtnTabBase);
		}
		
		this.attachChild(m_oHorizontalTabList);
	}
	
	public int getSelectedTabIdx() {
		return m_iSelectedTabIdx;
	}
	
	public void setSelectedTabIdx(int iSelectedTabIdx) {
		m_iSelectedTabIdx = iSelectedTabIdx;
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		for (int iCount=0; iCount<m_oFrameTabArray.size(); iCount++) {
			if (iChildId == m_oFrameTabArray.get(iCount).getId() && m_iSelectedTabIdx != iCount) {
	        	for (FrameTabBarListener listener : listeners) {
	        		m_iSelectedTabIdx = iCount;
	        		// Raise the event to parent
	           		listener.frameTabBar_tabClicked(iCount);
	            }
				bMatchChild = true;
				break;
			}
		}
				
        return bMatchChild;
	}
}
