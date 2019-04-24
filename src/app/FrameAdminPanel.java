package app;

import java.util.ArrayList;

import org.json.JSONObject;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameAdminPanelListener {
	void frameAdminPanelLookup_functionClicked(int iId);
//KingsleyKwan20171109		-----Start-----
	void frameAdminPanelLookup_clickCancel();
}

public class FrameAdminPanel extends VirtualUIFrame implements FrameOrderingPanelListener, FrameCommonPageContainerListener, FrameTitleHeaderListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameOrderingPanel m_oFrameAdminOrderingPanel;
	private VirtualUIFrame m_oFrameFooter;
	private VirtualUIFrame m_oFrameCover;
	private VirtualUILabel m_oCommonPageSelectedTabList;
	private VirtualUILabel m_oCommonPageUnselectTabList;
	private VirtualUILabel m_oLabelVersion;

//KingsleyKwan20171108		-----Start-----
	private FrameTitleHeader m_oTitleHeader;
//KingsleyKwan20171108		----- End -----
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameAdminPanelListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameAdminPanelListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameAdminPanelListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
	
    // constructor
    public FrameAdminPanel() {
    	m_oTemplateBuilder = new TemplateBuilder();
    	listeners = new ArrayList<FrameAdminPanelListener>();
    	
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraAdminPanel.xml");
		
		// Cover
		m_oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCover, "fraCover");
		this.attachChild(m_oFrameCover);
		
		m_oFrameAdminOrderingPanel = new FrameOrderingPanel();
		m_oTemplateBuilder.buildFrame(m_oFrameAdminOrderingPanel, "fraAdminOrderingPanel");
		m_oFrameAdminOrderingPanel.addListener(this);
		this.attachChild(m_oFrameAdminOrderingPanel);
		
		m_oFrameFooter = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameFooter, "fraFooter");
		this.attachChild(m_oFrameFooter);
		
		m_oLabelVersion = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelVersion, "lblVersion");
		m_oFrameFooter.attachChild(m_oLabelVersion);
		
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("admin"));
		m_oTitleHeader.addListener(this);
		this.attachChild(m_oTitleHeader);
		
		m_oCommonPageSelectedTabList = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oCommonPageSelectedTabList, "fraSelectedListTab");
		this.attachChild(m_oCommonPageSelectedTabList);
		
		m_oCommonPageUnselectTabList = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oCommonPageUnselectTabList, "fraUnselectListTab");
		this.attachChild(m_oCommonPageUnselectTabList);
    }
    
    public void createTabsWithPage(ArrayList<JSONObject> pagesArrayList) {
    	if(m_oCommonPageSelectedTabList != null && m_oCommonPageUnselectTabList != null)
    		m_oFrameAdminOrderingPanel.setTagTextSize(m_oCommonPageSelectedTabList.getTextSize(), m_oCommonPageUnselectTabList.getTextSize());
    	m_oFrameAdminOrderingPanel.createTabsWithPage(pagesArrayList);
    }

	public void initCommonPageContainer(int iTop, int iLeft, int iContainerWidth, int iContainerHeight, int iTagWidth, int iTagHeight, int iMaxTag,
			String sFontColorSelected, String sFontColorUnselected, String sBgColorSelected, String sBgColorUnselected,
			int iMaxMargin, boolean bShowUpperline, boolean bShowUnderline) {
		m_oFrameAdminOrderingPanel.initCommonPageTbList(iTop, iLeft, iContainerWidth, iContainerHeight, iTagWidth, iTagHeight, iMaxTag, sFontColorSelected, sFontColorUnselected, sBgColorSelected, sBgColorUnselected, m_oCommonPageSelectedTabList.getTextSize(), m_oCommonPageUnselectTabList.getTextSize(), iMaxMargin, bShowUpperline, bShowUnderline);
	}

    public void createDisplayPanelsWithLookup(int iPage, ArrayList<FuncLookupButtonInfo> displayPanelLookupArrayList, double dWidthRatio, double dHeightRatio) {
		m_oFrameAdminOrderingPanel.createDisplayPanelsWithLookup(iPage, displayPanelLookupArrayList, dWidthRatio, dHeightRatio, null);
    }
    
    public void showDisplayPanelAtPage(int page) {
    	m_oFrameAdminOrderingPanel.showDisplayPanelAtPage(page);
    }
    
//KingsleyKwan20171107shadow		-----Start-----
  	public void showFirstTag(){
  		m_oFrameAdminOrderingPanel.showFirstTag();
  	}
//KingsleyKwan20171107shadow		----- End -----
    
    @Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		
        return bMatchChild;
	}

	@Override
	public void frameOrderingPanel_hotItemClicked(int iId) {
	}

	@Override
	public void frameOrderingPanel_hotItemLongClicked(int iId) {

	}

	@Override
	public void frameOrderingPanel_functionClicked(int iId, String[] sName, String sParameter) {
		for(FrameAdminPanelListener listener: listeners) {
			listener.frameAdminPanelLookup_functionClicked(iId);
		}
	}

	@Override
	public void frameOrderingPanel_lookupClicked(int iId) {
	}

	@Override
	public void frameOrderingPanel_hotModifierClicked(int iId, String sParameter) {
	}

	@Override
	public void frameOrderingPanel_modifierLookupClicked(int iId, String sParameter) {
	}

	@Override
	public void frameOrderingPanel_paymentClicked(int iId, String sParameter) {
	}

	@Override
	public void frameOrderingPanel_subPanelPageClicked(int iId) {
	}
	
	@Override
	public void frameOrderingPanel_discountClicked(int iId, String sParameter) {
	}
	
	@Override
	public void frameOrderingPanel_tabClicked() {
	}

	@Override
	public void frameOrderingPanel_UpdateMenuItem() {
	}

	@Override
	public boolean frameOrderingPanel_OrderingTimeout() {
		return false;
	}
	
	@Override
	public boolean frameOrderingPanel_CashierTimeout() {
		return false;
	}

	@Override
	public void frameOrderingPanel_barcodeReaded(int iCurrentFrameId,
			String sValue) {
	}
	
	@Override
	public void frameOrderingPanel_clearOctopusDisplay() {
	}

	@Override
	public void frameCommonPageContainer_changeFrame() {
		// TODO Auto-generated method stub
		
	}

//KingsleyKwan20171031		-----start-----
	@Override
	public boolean frameCommonPageContainer_swipe(boolean bLeft, String sNote, int iChildId) {
		boolean bMatchChild = false;
//KingsleyKwan20171031		----- End -----
		
		if(Integer.parseInt(sNote) < m_oFrameAdminOrderingPanel.getCommonPageContainer().getTotalIndex()){
			m_oFrameAdminOrderingPanel.getCommonPageContainer().clickTag(Integer.parseInt(sNote));
			bMatchChild = true;
		}
		
		return bMatchChild;
	}

	@Override
	public void frameCommonPageContainer_updateFrame(int iOrigIndex, int iIndex, String sFrameName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void FrameTitleHeader_close() {
//KingsleyKwan20171109		-----Start-----
		for(FrameAdminPanelListener listener: listeners) {
			listener.frameAdminPanelLookup_clickCancel();
		}
//KingsleyKwan20171109		----- End -----
	}
	
//KingsleyKwan20171120		-----Start-----
	@Override
	public void frameCommonPageContainer_CloseImageClicked() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void frameCommonPageContainer_ClickButtonIndex(int iIndex) {
		// TODO Auto-generated method stub
		
	}
//KingsleyKwan20171120		----- End -----
}
