package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import om.PosCheck;

import org.json.JSONArray;
import org.json.JSONObject;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameCheckListingListener {
	void frameCheckListing_ButtonExitClicked();
	void frameCheckListing_CheckListRecordClicked(int iPanelId, PosCheck oCheck, int iListingType);
	void frameCheckListing_ButtonContinueClicked();
}

public class FrameCheckListing extends VirtualUIFrame implements FrameCheckListPanelListener, FrameTitleHeaderListener {
	private String m_sTabLabelBackground = "";
	private int m_iSelectedPanelTab;

	private TemplateBuilder m_oTemplateBuilder;

	private VirtualUIFrame m_oFrameContent;
	private FrameCommonPageContainer m_oCommonPageTabList;
	private VirtualUILabel m_oCommonPageSelectedTabList;
	private VirtualUILabel m_oCommonPageUnselectTabList;
	private FrameCheckListPanel m_oFrameCheckListPanel;
	private List<VirtualUIFrame> m_oFramePanelTab;
	private AppGlobal.OPERATION_MODE m_eOperationMode;
	private HashMap<Integer, FrameCheckListPanel> m_oCheckListPanelList;
	
	private int m_iCheckListingType;
	private boolean m_bIsShowTotalDue;
	
	// for normal check listing function
	public static int PANEL_PAST_DATE_CHECK = 0;
	public static int PANEL_PAID_CHECK_CURRENT_STATION = 0;
	public static int PANEL_PAID_CHECK = 1;
	public static int PANEL_OPEN_CHECK = 2;
	public static int PANEL_VOID_CHECK = 3;
	
	private FrameTitleHeader m_oTitleHeader;

	// for payment interface card transfer function
	public static int PANEL_ACTIVE_CHECK = 0;

	public static int TYPE_NORMAL = 0;
	public static int TYPE_PAST_DATE = 1;
	public static int TYPE_ACTIVE_CHECK = 2; // for payment interface card
												// transfer function
	public static int TYPE_DAILY_CLOSE_OPEN_CHECK = 3;

	public static String COLOR_SELECTED = "#66a1c1";
	
	// for Check Listing by which type
	private int m_iSetCheckListingByType;
	
	public static int TYPE_CHECK_LISTING_BY_NORMAL = 0;
	public static int TYPE_CHECK_LISTING_BY_TABLE_REFERENCE = 1;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameCheckListingListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameCheckListingListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameCheckListingListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameCheckListing(AppGlobal.OPERATION_MODE eOperationMode) {
		m_eOperationMode = eOperationMode;
		m_iCheckListingType = TYPE_NORMAL;
		m_iSelectedPanelTab = 0;
		m_oCheckListPanelList = new HashMap<Integer, FrameCheckListPanel>();
		m_oFramePanelTab = new ArrayList<VirtualUIFrame>();
		this.listeners = new ArrayList<FrameCheckListingListener>();
		
		// read calculation method
		if (AppGlobal.g_oFuncStation.get().getCheckTotalCalculationMethod().equals(FuncStation.CHECK_LISTING_CALCULATION_METHOD_TOTAL_DUE))
			m_bIsShowTotalDue = true;
		else
			m_bIsShowTotalDue = false;
		
		m_oTemplateBuilder = new TemplateBuilder();

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraCheckListing.xml");

		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("check_listing"));
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
			//m_oCommonPageTabList.init(m_oCommonPageTabList.getWidth(), m_oCommonPageTabList.getHeight(), m_oCommonPageSelectedTabList.getWidth(), 
			//		m_oCommonPageSelectedTabList.getHeight(), 5, m_oCommonPageSelectedTabList.getForegroundColor(), 
			//		m_oCommonPageUnselectTabList.getForegroundColor(), m_oCommonPageSelectedTabList.getBackgroundColor(), m_oCommonPageUnselectTabList.getBackgroundColor(), 0, false, true);
			m_oCommonPageTabList.init(m_oCommonPageTabList.getWidth(), m_oCommonPageTabList.getHeight(), m_oCommonPageSelectedTabList.getWidth(), 
					m_oCommonPageSelectedTabList.getHeight(), 5, m_oCommonPageSelectedTabList.getForegroundColor(), 
					m_oCommonPageUnselectTabList.getForegroundColor(), "", "", 0, false, true);
		
		m_oCommonPageTabList.setUnderlineColor(m_oCommonPageUnselectTabList.getForegroundColor());
		m_oCommonPageTabList.setUpperlineColor(m_oCommonPageUnselectTabList.getForegroundColor());
		m_oFrameContent.attachChild(m_oCommonPageTabList);
		
		VirtualUIFrame oFramePanelLookupSeparator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFramePanelLookupSeparator, "fraPanelTabSeparator");
		oFramePanelLookupSeparator.setEnabled(false);
		m_oFrameContent.attachChild(oFramePanelLookupSeparator);
	}

	public void addCheckListingTab(int iPanelId, String sPanelListValue) {
		VirtualUILabel oLblPanelLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblPanelLabel, "lblPanelTab");
		oLblPanelLabel.setValue(sPanelListValue);
		oLblPanelLabel.setEnabled(true);
		oLblPanelLabel.allowClick(false);

		// Create Underline frame
		VirtualUIFrame fraPageTabUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(fraPageTabUnderline, "fraPanelTabUnderline");
		fraPageTabUnderline.setVisible(false);
		fraPageTabUnderline.setEnabled(false);
		fraPageTabUnderline.allowClick(false);

		// Create single Tab base frame
		VirtualUIFrame fraPageTabBase = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(fraPageTabBase, "fraPanelTabBase");
		fraPageTabBase.setEnabled(true);
		fraPageTabBase.allowClick(true);
		fraPageTabBase.setClickServerRequestNote(Integer.toString(iPanelId));
		fraPageTabBase.setClickServerRequestBlockUI(true);
		fraPageTabBase.attachChild(oLblPanelLabel);
		fraPageTabBase.attachChild(fraPageTabUnderline);

		addCheckListPanel(iPanelId);
		m_oFramePanelTab.add(fraPageTabBase);
		
		//prepare the common basket
		for (int iCount = 0; iCount < m_oFramePanelTab.size(); iCount++) {
			FrameCheckListPanel oCheckListPanel = m_oCheckListPanelList.get(iCount);
			
			PosCheck oPosCheck = new PosCheck();
			JSONArray oCheckListJSONArray;
			if(!oCheckListPanel.loadedRecord() && iCount != FrameCheckListing.PANEL_PAID_CHECK_CURRENT_STATION) {
				boolean bCheckFloorPlan;
				if (iCount == FrameCheckListing.PANEL_PAID_CHECK) {
					oCheckListJSONArray = oPosCheck.getCheckListByBusinessDayPaid(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), 0, PosCheck.PAID_FULL_PAID, false);
					bCheckFloorPlan = false;
				} else if (iCount == FrameCheckListing.PANEL_OPEN_CHECK) {
					oCheckListJSONArray = oPosCheck.getCheckListByBusinessDayPaid(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), 0, PosCheck.PAID_NOT_PAID, false);
					bCheckFloorPlan = true;
				} else {
					oCheckListJSONArray = oPosCheck.getVoidCheckListByBusinessDayOutlet(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getOutletId());
					bCheckFloorPlan = false;
				}
				if(oCheckListJSONArray != null)
					addRecordToFrameCheckListPanel(iCount, oCheckListJSONArray, bCheckFloorPlan);
			}
			changeSelectedListingTab(iCount);
		}
		m_oCommonPageTabList.addButton(sPanelListValue, m_oFrameCheckListPanel);
		
		m_sTabLabelBackground = oLblPanelLabel.getForegroundColor();
	}

	public void setCheckListing(int iType) {
		m_iCheckListingType = iType;
	}

	public void addCheckListPanel(int iPanelId) {
		String sPanelType;

		if (m_iCheckListingType == TYPE_PAST_DATE)
			sPanelType = FrameCheckListPanel.TYPE_PAST_DATE_CHECK;
		else if ((m_iCheckListingType == FrameCheckListing.TYPE_DAILY_CLOSE_OPEN_CHECK) || (m_iCheckListingType == FrameCheckListing.TYPE_ACTIVE_CHECK))
			sPanelType = FrameCheckListPanel.TYPE_OPEN_CHECK;
		else {
			if (iPanelId == FrameCheckListing.PANEL_OPEN_CHECK)
				sPanelType = FrameCheckListPanel.TYPE_OPEN_CHECK;
			else if (iPanelId == FrameCheckListing.PANEL_VOID_CHECK)
				sPanelType = FrameCheckListPanel.TYPE_VOID_CHECK;
			else
				sPanelType = FrameCheckListPanel.TYPE_PAID_CHECK;
		}
		
		m_oFrameCheckListPanel = new FrameCheckListPanel(sPanelType, m_iCheckListingType, m_eOperationMode);
		
		m_oFrameCheckListPanel.setCheckListingByType(m_iSetCheckListingByType);
		
		m_oFrameCheckListPanel.addCheckListPanelTitle(sPanelType);
		
		m_oTemplateBuilder.buildFrame(m_oFrameCheckListPanel, "scrfraCheckListing");
	
		m_oCheckListPanelList.put(iPanelId, m_oFrameCheckListPanel);
		
		m_oFrameCheckListPanel.setVisible(false);
		
		if (m_iCheckListingType == FrameCheckListing.TYPE_DAILY_CLOSE_OPEN_CHECK)
			m_oFrameCheckListPanel.setContinueButtonVisible(true);
		
		// Add listener
		m_oFrameCheckListPanel.addListener(this);
		
		m_oFrameContent.attachChild(m_oFrameCheckListPanel);
	}

	public void addRecordToFrameCheckListPanel(int iPanelId, JSONArray oCheckJSONArray, boolean checkFloorPlan) {
		FrameCheckListPanel oFrameCheckListPanel = m_oCheckListPanelList.get(iPanelId);
		oFrameCheckListPanel.initCheckList(oCheckJSONArray, checkFloorPlan);

		BigDecimal dCheckTotal = BigDecimal.ZERO;
		for (int i = 0; i < oCheckJSONArray.length(); i++) {
			JSONObject oCheckJSONObject = oCheckJSONArray.optJSONObject(i);
			if (oCheckJSONObject == null)
				continue;
			PosCheck oCheck = new PosCheck(oCheckJSONObject);
			if (m_bIsShowTotalDue)
				dCheckTotal = dCheckTotal.add(oCheck.getCheckTotal()).add(oCheck.getSurchargeTotal()).add(oCheck.getTipsTotal());
			else
				dCheckTotal = dCheckTotal.add(oCheck.getCheckTotal());
		}
		oFrameCheckListPanel.setTotalCheck(oCheckJSONArray.length());
		oFrameCheckListPanel.setTotalAmount(AppGlobal.g_oFuncOutlet.get().roundCheckAmountToString(dCheckTotal));
	}

	public void changeSelectedListingTab(int iSelectedPanelTab) {
		String sBackground;

		// change the lookup tab color
		for (VirtualUIFrame oLblPanelTabBaseFrame : m_oFramePanelTab) {
			for (VirtualUIBasicElement oChild : oLblPanelTabBaseFrame.getChilds()) {
				if (oChild.getUIType() == HeroActionProtocol.View.Type.LABEL) {
					VirtualUILabel oLabel = (VirtualUILabel) oChild;
					// Set Tab Label background color
					if (m_sTabLabelBackground.length() == 7) {
						sBackground = FrameCheckListing.COLOR_SELECTED;
						oLabel.setForegroundColor(sBackground);
					}
				} else if (oChild.getUIType() == HeroActionProtocol.View.Type.FRAME) {
					VirtualUIFrame oUnderlineFrame = (VirtualUIFrame) oChild;
					oUnderlineFrame.setVisible(false);
				}
			}
		}

		VirtualUIFrame oSelectedPanelTabBaseFrame = m_oFramePanelTab.get(iSelectedPanelTab);
		for (VirtualUIBasicElement oChild : oSelectedPanelTabBaseFrame.getChilds()) {
			if (oChild.getUIType() == HeroActionProtocol.View.Type.LABEL) {
				VirtualUILabel oLabel = (VirtualUILabel) oChild;
				// Set Tab Label background color
				if (m_sTabLabelBackground.length() == 7) {
					sBackground = "#FF" + m_sTabLabelBackground.substring(1, 7);
					oLabel.setForegroundColor(sBackground);
				}
			} else if (oChild.getUIType() == HeroActionProtocol.View.Type.FRAME) {
				VirtualUIFrame oUnderlineFrame = (VirtualUIFrame) oChild;
				oUnderlineFrame.setVisible(true);
			}
		}

		// hide all subMenu lookup
		for (Entry<Integer, FrameCheckListPanel> entry : m_oCheckListPanelList.entrySet()) {
			if (entry.getValue().getVisible()) {
				entry.getValue().setVisible(false);
			}
		}
		m_iSelectedPanelTab = iSelectedPanelTab;

		// show the selected setMenu lookup
		FrameCheckListPanel oSelectedCheckListing = m_oCheckListPanelList.get(iSelectedPanelTab);
		oSelectedCheckListing.setVisible(true);
		oSelectedCheckListing.setEnabled(true);
		
		oSelectedCheckListing.clearCheckNoLabel();
		oSelectedCheckListing.setDisplayCheckList();
		oSelectedCheckListing.updateCheckListRecord();
	}

	public void setTitle(String sTitle) {
		m_oTitleHeader.setTitle(sTitle);
	}
	
	public void setCheckListingByType(int iSetCheckListingByType) {
		m_iSetCheckListingByType = iSetCheckListingByType;
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		for (int iCount = 0; iCount < m_oFramePanelTab.size(); iCount++) {
			if (iChildId == m_oFramePanelTab.get(iCount).getId()) {
				if (m_iCheckListingType == TYPE_PAST_DATE) {
					bMatchChild = false;
					break;
				}

				FrameCheckListPanel oCheckListPanel = m_oCheckListPanelList.get(iCount);

				PosCheck oPosCheck = new PosCheck();
				JSONArray oCheckListJSONArray;
				if (!oCheckListPanel.loadedRecord() && iCount != FrameCheckListing.PANEL_PAID_CHECK_CURRENT_STATION) {
					boolean bCheckFloorPlan;
					if (iCount == FrameCheckListing.PANEL_PAID_CHECK) {
						oCheckListJSONArray = oPosCheck.getCheckListByBusinessDayPaid(
								AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), 0, PosCheck.PAID_FULL_PAID,
								false);
						bCheckFloorPlan = false;
					} else if (iCount == FrameCheckListing.PANEL_OPEN_CHECK) {
						oCheckListJSONArray = oPosCheck.getCheckListByBusinessDayPaid(
								AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), 0, PosCheck.PAID_NOT_PAID,
								false);
						bCheckFloorPlan = true;
					} else {
						oCheckListJSONArray = oPosCheck.getVoidCheckListByBusinessDayOutlet(
								AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(),
								AppGlobal.g_oFuncOutlet.get().getOutletId());
						bCheckFloorPlan = false;
					}

					if (oCheckListJSONArray != null)
						addRecordToFrameCheckListPanel(iCount, oCheckListJSONArray, bCheckFloorPlan);
				}

				changeSelectedListingTab(iCount);
				bMatchChild = true;
				break;
			}
		}
		return bMatchChild;
	}

	@Override
	public void frameCheckListPanel_CheckListRecordClicked(PosCheck oCheck, int iListingType) {
		m_iSelectedPanelTab = m_oCommonPageTabList.getCurrentIndex();
		for(FrameCheckListingListener listener: listeners)
			listener.frameCheckListing_CheckListRecordClicked(m_iSelectedPanelTab, oCheck, iListingType);
	}

	@Override
	public void frameCheckListPanel_ButtonContinueClicked() {
		for (FrameCheckListingListener listener : listeners)
			listener.frameCheckListing_ButtonContinueClicked();
	}

	@Override
	public void FrameTitleHeader_close() {
		for (FrameCheckListingListener listener : listeners) {
			listener.frameCheckListing_ButtonExitClicked();
		}
	}
}
