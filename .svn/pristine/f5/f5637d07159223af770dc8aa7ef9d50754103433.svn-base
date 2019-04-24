package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//KingCheung20171101		-----Start-----
import java.util.Map.Entry;

import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//KingCheung20171101		----- End -----

import externallib.StringLib;
import om.PosDisplayPanelLookup;
import om.PosDisplayPanelPage;
import om.PosOutletItem;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameOrderingPanelListener {
	void frameOrderingPanel_hotItemClicked(int iId);
	void frameOrderingPanel_hotItemLongClicked(int iId);
	void frameOrderingPanel_functionClicked(int iId, String[] sName, String sParameter);
	void frameOrderingPanel_lookupClicked(int iId);
	void frameOrderingPanel_hotModifierClicked(int iId, String sParameter);
	void frameOrderingPanel_modifierLookupClicked(int iId, String sParameter);
	void frameOrderingPanel_paymentClicked(int iId, String sParameter);
	void frameOrderingPanel_subPanelPageClicked(int iId);
	void frameOrderingPanel_discountClicked(int iId, String sParameter);
	void frameOrderingPanel_tabClicked();
	void frameOrderingPanel_UpdateMenuItem();
	boolean frameOrderingPanel_OrderingTimeout();
	boolean frameOrderingPanel_CashierTimeout();
	void frameOrderingPanel_barcodeReaded(int iCurrentFrameId, String sValue);
	void frameOrderingPanel_clearOctopusDisplay();
}

public class FrameOrderingPanel extends VirtualUIFrame implements FrameGeneralLookupButtonListener, FrameCommonPageContainerListener {

	TemplateBuilder m_oTemplateBuilder;

	private FrameCommonPageContainer m_oCommonPageTabList;
	private VirtualUIFrame m_oCommonPageSelectedTabList;
	private VirtualUIFrame m_oCommonPageUnselectTabList;
	private HashMap<Integer, String[]> m_oCommonPageTabNameList;
	private List<VirtualUIFrame> m_oFramePageTabArray;
	private HashMap<Integer, Integer> m_oPageIdPageIndexPairs;
	private HashMap<Integer, String> m_oPageIndexPageTypePairs;
	private List<VirtualUIFrame> m_oFrameDisplayPanelArray;
	private List<FrameGeneralLookupButton> m_oButtonPanelLookupArray;
	private List<JSONObject> m_oPanelPages;
	private List<FuncLookupButtonInfo> m_oDisplayPanelLookups;
	
	private String m_sTabLabelBackground = "";
	
	private Boolean m_bDirectPaymentMode = false;
	private Boolean m_bUsePanelToSelectItemMode = false;
	private Boolean m_bUsePanelToSelectPaymentMethodMode = false;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameOrderingPanelListener> listeners;
	private HashMap<Integer, List<FrameGeneralLookupButton>> m_oPanelLookupButtonsHaveStockQtyList;
	
	public static String COLOR_SELECTED = "#66a1c1";
	
	public static String PAGE_NAME = "name";
	public static String PAGE_TYPE = "type";
	public static String PANEL_PAGE_ID = "panel_page_id";
	
	public static final int PANEL_TAB_HEIGHT = 55;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameOrderingPanelListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameOrderingPanelListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	/** remove all panel */
	public void removeAllPanelPage() {
		m_oCommonPageTabList.removeAllTag();
		m_oCommonPageTabList.removeAllChildren();
		m_oCommonPageTabList = new FrameCommonPageContainer();
		m_oCommonPageTabNameList.clear();
		m_oCommonPageTabNameList = new HashMap<Integer, String[]>();
		m_oPanelPages.clear();
		m_oFramePageTabArray.clear();
		m_oPageIdPageIndexPairs.clear();
		m_oPageIndexPageTypePairs.clear();
		m_oDisplayPanelLookups.clear();
		m_oButtonPanelLookupArray.clear();
		m_oPanelLookupButtonsHaveStockQtyList.clear();
		m_oFrameDisplayPanelArray.clear();
	}
	
	public FrameOrderingPanel() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameOrderingPanelListener>();
		m_oCommonPageTabList = new FrameCommonPageContainer();
		m_oCommonPageTabNameList = new HashMap<Integer, String[]>();
		m_oFramePageTabArray = new ArrayList<VirtualUIFrame>();
		m_oPageIdPageIndexPairs = new HashMap<Integer, Integer>();
		m_oPageIndexPageTypePairs = new HashMap<Integer, String>();
		m_oFrameDisplayPanelArray = new ArrayList<VirtualUIFrame>();
		m_oButtonPanelLookupArray = new ArrayList<FrameGeneralLookupButton>();
		m_oPanelLookupButtonsHaveStockQtyList = new HashMap<Integer, List<FrameGeneralLookupButton>>();
		m_oPanelPages = new ArrayList<JSONObject>();
		m_oDisplayPanelLookups = new ArrayList<FuncLookupButtonInfo>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraOrderingPanel.xml");
		
		m_oCommonPageSelectedTabList = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oCommonPageSelectedTabList, "fraSelectedListTab");
		
		m_oCommonPageUnselectTabList = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oCommonPageUnselectTabList, "fraUnselectListTab");
	}
	
	// Generate the commonPageTabList with params
	public void initCommonPageTbList(int iTop, int iLeft, int iContainerWidth, int iContainerHeight, int iTagWidth, int iTagHeight, int iMaxTag,
			String sFontColorSelected, String sFontColorUnselected, String sBgColorSelected, String sBgColorUnselected, int iTagTextSizeSelected, int iTagTextSizeUnselected,
			int iMaxMargin, boolean bShowUpperline, boolean bShowUnderline){
		
		m_oTemplateBuilder.buildFrame(m_oCommonPageTabList, "fraListTab");
		m_oCommonPageTabList.setTop(iTop);
		m_oCommonPageTabList.setLeft(iLeft);
		m_oCommonPageTabList.setWidth(iContainerWidth);
		m_oCommonPageTabList.setHeight(iContainerHeight);
		m_oCommonPageTabList.init(iContainerWidth, iContainerHeight, iTagWidth, iTagHeight, iMaxTag, sFontColorSelected, 
				sFontColorUnselected, sBgColorSelected, sBgColorUnselected, iMaxMargin, bShowUpperline, bShowUnderline);
		m_oCommonPageTabList.setTagTextSize(iTagTextSizeSelected, iTagTextSizeUnselected);
		this.attachChild(m_oCommonPageTabList);
	}

	/** View Related Methods **/
	
	public void createTabsWithPage(ArrayList<JSONObject> oPageArray) {
		m_oPanelPages = oPageArray;

		if(!m_oCommonPageTabList.getExist()){
			m_oTemplateBuilder.buildFrame(m_oCommonPageTabList, "fraListTab");
			int iTabListWidth = this.getWidth();
			if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.horizontal_desktop.name())){
				m_oCommonPageTabList.init(iTabListWidth, this.getHeight(), m_oCommonPageSelectedTabList.getWidth(), m_oCommonPageSelectedTabList.getHeight(), 5, m_oCommonPageSelectedTabList.getForegroundColor(), m_oCommonPageUnselectTabList.getForegroundColor(), m_oCommonPageSelectedTabList.getBackgroundColor(), m_oCommonPageUnselectTabList.getBackgroundColor(), 16, false, false);
				if(m_oCommonPageSelectedTabList.getTextSize() > 0)
					m_oCommonPageTabList.setTagTextSize(m_oCommonPageSelectedTabList.getTextSize(), m_oCommonPageUnselectTabList.getTextSize());
			} else
				m_oCommonPageTabList.init(iTabListWidth, this.getHeight(), m_oCommonPageSelectedTabList.getWidth(), m_oCommonPageSelectedTabList.getHeight(), 3, m_oCommonPageSelectedTabList.getForegroundColor(), m_oCommonPageUnselectTabList.getForegroundColor(), m_oCommonPageSelectedTabList.getBackgroundColor(), m_oCommonPageUnselectTabList.getBackgroundColor(), 16, false, true);

			m_oCommonPageTabList.addListener(this);
			this.attachChild(m_oCommonPageTabList);
		}
		
		for(JSONObject oPageJSONObject : m_oPanelPages) {
			String[] sName = StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, "");
			try {
				 sName = (String[]) oPageJSONObject.opt(FrameOrderingPanel.PAGE_NAME);
			} catch (Exception e) {}

			//Create Tab Name label
			VirtualUILabel lblPageTab = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(lblPageTab, "lblPageTab");
			lblPageTab.setValue(sName);
			lblPageTab.allowClick(false);
			lblPageTab.setVisible(false);

			//Create single Tab base frame
			VirtualUIFrame fraPageTabBase = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(fraPageTabBase, "fraPageTabBase");
			fraPageTabBase.allowClick(true);
			fraPageTabBase.allowLongClick(true);
			fraPageTabBase.setClickServerRequestNote(Integer.toString(m_oPanelPages.indexOf(oPageJSONObject)+1));
			fraPageTabBase.setLongClickServerRequestNote(Integer.toString(m_oPanelPages.indexOf(oPageJSONObject)+1));
			fraPageTabBase.setClickServerRequestBlockUI(false);
			fraPageTabBase.setLongClickServerRequestBlockUI(false);
//			fraPageTabBase.attachChild(fraPageTabUnderline);		//KingsleyKwan20170918Underline
			fraPageTabBase.attachChild(lblPageTab);
			
			if(oPageJSONObject.optString(FrameOrderingPanel.PAGE_TYPE).equals(PosDisplayPanelPage.TYPE_SUB_PANEL))
				// Hidden the sub panel
				fraPageTabBase.setVisible(false);
			
			m_oCommonPageTabNameList.put(m_oCommonPageTabNameList.size(), sName);
			m_oFramePageTabArray.add(fraPageTabBase);
			
			// Add the page ID and page index pair
			m_oPageIdPageIndexPairs.put(oPageJSONObject.optInt(FrameOrderingPanel.PANEL_PAGE_ID), m_oFramePageTabArray.size());
			m_oPageIndexPageTypePairs.put(m_oFramePageTabArray.size(), oPageJSONObject.optString(FrameOrderingPanel.PAGE_TYPE));

			m_sTabLabelBackground = lblPageTab.getForegroundColor();
		}
		
		// Add update menu item timer
		addUpdateMenuItemTimer();
		
		// Add auto clear Octopus display timer
		addAutoClearOctopusDisplayTimer();
	}
	
	public void createDisplayPanelsWithLookup(int iPage, ArrayList<FuncLookupButtonInfo> oDisplayPanelLookup, double dWidthRatio, double dHeightRatio, String sMediaSource) {
		m_oDisplayPanelLookups = oDisplayPanelLookup;
		
		VirtualUIFrame fraDisplayPanel = getPanelFrameAtPage(iPage);
		
		if (m_oPanelPages.size() <= 1) {
			m_oCommonPageTabList.setTagbuttonVisible(false);

			// move the display panel to top if only 1 page
			for(VirtualUIFrame frameDisplayPanel : m_oFrameDisplayPanelArray) {
				frameDisplayPanel.setTop(0);
				frameDisplayPanel.setLeft(0);
			}
		} else {
			m_oCommonPageTabList.setTagbuttonVisible(true);
		}
		
		// Set Background Image to Ordering Panel 
		if(sMediaSource != null && !sMediaSource.isEmpty()) {
			VirtualUIImage oImage = new VirtualUIImage();
			oImage.setHeight(fraDisplayPanel.getHeight());
			oImage.setWidth(fraDisplayPanel.getWidth());
			oImage.setSource(sMediaSource);
			oImage.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_TO_FIT);
			fraDisplayPanel.attachChild(oImage);
		}
		
		for(FuncLookupButtonInfo oButtonInfo : m_oDisplayPanelLookups) {
			String sType = oButtonInfo.getType();
			int iId = oButtonInfo.getId();
			
			// *** Version 0.0.0.38, use back the ratio to show ordering panel according to product team
			int iTop = (int)(oButtonInfo.getTop() * dHeightRatio);
			int iLeft = (int)(oButtonInfo.getLeft() * dWidthRatio);
			int iWidth = (int)(oButtonInfo.getWidth() * dWidthRatio);
			int iHeight = (int)(oButtonInfo.getHeight() * dHeightRatio);
			int iPadding = oButtonInfo.getPadding();
			
			boolean bClickBlockUI = oButtonInfo.isBlockUI();
			
			FrameGeneralLookupButton oFrameGeneralLookupButton = new FrameGeneralLookupButton();
			m_oTemplateBuilder.buildFrame(oFrameGeneralLookupButton, "fraLooupButton");
			oFrameGeneralLookupButton.setTop(iTop+iPadding);
			oFrameGeneralLookupButton.setLeft(iLeft+iPadding);
			int iButtonWidth = iWidth - iPadding * 2;
			if ((oFrameGeneralLookupButton.getLeft() + iButtonWidth + iPadding) > (fraDisplayPanel.getWidth() - iPadding))
				iButtonWidth = (fraDisplayPanel.getWidth() - iPadding) - oFrameGeneralLookupButton.getLeft();
			oFrameGeneralLookupButton.setWidth(iButtonWidth);
			int iButtonHeight = iHeight - iPadding * 2;
			if ((oFrameGeneralLookupButton.getTop() + iButtonHeight + iPadding) > (fraDisplayPanel.getHeight() - iPadding))
				iButtonHeight = (fraDisplayPanel.getHeight() - iPadding) - oFrameGeneralLookupButton.getTop();
			oFrameGeneralLookupButton.setHeight(iButtonHeight);
			oFrameGeneralLookupButton.addListener(this);
			try {
				JSONObject oJSONObject = new JSONObject();
				oJSONObject.put(FrameLookupButton.BUTTON_NOTE_TYPE, sType);
				oJSONObject.put(FrameLookupButton.BUTTON_NOTE_ID, iId);
				oJSONObject.put(FrameLookupButton.BUTTON_NOTE_NAME, oButtonInfo.getName());
				if(oButtonInfo.getParameter() != null){
					String sParameter = oButtonInfo.getParameter();
					JSONObject oParameter = new JSONObject(sParameter);
					oJSONObject.put(FrameLookupButton.BUTTON_NOTE_PARAMETER, oParameter);
				}
				
				oFrameGeneralLookupButton.setButtonProperty(oButtonInfo, 0.26, oJSONObject.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			if (!bClickBlockUI) {
				oFrameGeneralLookupButton.setButtonClickBlockUI(false);
				oFrameGeneralLookupButton.setButtonLongClickBlockUI(false);
			}
			
			fraDisplayPanel.attachChild(oFrameGeneralLookupButton);
			m_oButtonPanelLookupArray.add(oFrameGeneralLookupButton);
		}
	}
	
	/** Normal functions **/
	public void showDisplayPanelAtPage(int page) {
		VirtualUIFrame oFrame = null;
		VirtualUIFrame oTabBaseFrame = null;
		String sBackground;

		for (int i = 1; i <= m_oFrameDisplayPanelArray.size(); i++) {
			oFrame = m_oFrameDisplayPanelArray.get(i-1);
			oTabBaseFrame = m_oFramePageTabArray.get(i-1);
			
			if (NumberUtils.toInt(oFrame.getSwipeLeftServerRequestNote(), -1) == page) {
				oFrame.setVisible(true);
				oFrame.bringToTop();
				
				for (VirtualUIBasicElement oChild : oTabBaseFrame.getChilds()) {
					if (oChild.getUIType() == HeroActionProtocol.View.Type.LABEL) {
						VirtualUILabel oLabel = (VirtualUILabel)oChild;
						//Set Tab Label background color
						if (m_sTabLabelBackground.length() == 7) {
							sBackground = "#FF" + m_sTabLabelBackground.substring(1, 7);
							oLabel.setForegroundColor(sBackground);
						}
					} else if (oChild.getUIType() == HeroActionProtocol.View.Type.FRAME) {
						VirtualUIFrame oUnderlineFrame = (VirtualUIFrame)oChild;
						oUnderlineFrame.setVisible(true);
					}
				}
			} else {
				oFrame.setVisible(false);
				
				for (VirtualUIBasicElement oChild : oTabBaseFrame.getChilds()) {
					if (oChild.getUIType() == HeroActionProtocol.View.Type.LABEL) {
						VirtualUILabel oLabel = (VirtualUILabel)oChild;
						//Set Tab Label background color
						if (m_sTabLabelBackground.length() == 7) {
							sBackground = FrameOrderingPanel.COLOR_SELECTED;
							oLabel.setForegroundColor(sBackground);
						}
					} else if(oChild.getUIType() == HeroActionProtocol.View.Type.FRAME) {
						VirtualUIFrame oUnderlineFrame = (VirtualUIFrame)oChild;
						oUnderlineFrame.setVisible(false);
					}
				}
			}
		}
	}

	// Show sub panel page by panel page ID
	public void showSubDisplayPanelByPageId(int iPageId) {
		if (m_oPageIdPageIndexPairs.containsKey(iPageId) == false)
			return;
		
		closeMenuLookup();
		
		int iPageIndex = m_oPageIdPageIndexPairs.get(iPageId);
		this.showDisplayPanelAtPage(iPageIndex);
	}
	
	//Return a frame of panel at page
	private VirtualUIFrame getPanelFrameAtPage(int iPage) {
		for (VirtualUIFrame frame : m_oFrameDisplayPanelArray) {
			if (NumberUtils.toInt(frame.getSwipeLeftServerRequestNote(), -1) == iPage)
				return frame;
		}
		
		//Create a new frame if don't have
		VirtualUIFrame fraDisplayPanel = new VirtualUIFrame();
		fraDisplayPanel.setWidth(this.getWidth());
		fraDisplayPanel.setHeight(this.getHeight());
		fraDisplayPanel.setVisible(false);
		fraDisplayPanel.allowSwipeLeft(true);
		fraDisplayPanel.allowSwipeRight(true);
		fraDisplayPanel.setSwipeLeftServerRequestBlockUI(false);
		fraDisplayPanel.setSwipeRightServerRequestBlockUI(false);
		fraDisplayPanel.setClickServerRequestNote(Integer.toString(iPage));
		fraDisplayPanel.setSwipeLeftServerRequestNote(Integer.toString(iPage));
		fraDisplayPanel.setSwipeRightServerRequestNote(Integer.toString(iPage));
		
		m_oFrameDisplayPanelArray.add(fraDisplayPanel);
		m_oCommonPageTabList.addButton(m_oCommonPageTabNameList.get(iPage-1), fraDisplayPanel);
		
		return fraDisplayPanel;
	}
	
	public void setItemStockQty(int iId, BigDecimal dQty) {
		if (m_oPanelLookupButtonsHaveStockQtyList.containsKey(iId)) {
			for (FrameGeneralLookupButton oFrameLookupButton: m_oPanelLookupButtonsHaveStockQtyList.get(iId)) {
				String sQty = FrameLookupButton.QUANTITY_SOLDOUT;
				
				if (dQty != null)
					sQty = dQty.stripTrailingZeros().toPlainString();
				oFrameLookupButton.setButtonStockQty(sQty);
				if (sQty.equals(FrameLookupButton.QUANTITY_SOLDOUT) || dQty.compareTo(BigDecimal.ZERO) == 0)
					oFrameLookupButton.setTransparentVisible(true);
				else
					oFrameLookupButton.setTransparentVisible(false);
			}
		}
	}
	
	public void removeItemStockQty(int iId) {
		if (m_oPanelLookupButtonsHaveStockQtyList.containsKey(iId)) {
			for(FrameGeneralLookupButton oFrameLookupButton: m_oPanelLookupButtonsHaveStockQtyList.get(iId)){
				oFrameLookupButton.setButtonStockLabelVisible(false);
				oFrameLookupButton.setTransparentVisible(false);
			}
			m_oPanelLookupButtonsHaveStockQtyList.remove(iId);
		}
	}

	public void resetAllItemStockQty() {
		for (FrameGeneralLookupButton oFrameLookupButton: m_oButtonPanelLookupArray)
			oFrameLookupButton.setButtonStockLabelVisible(false);
	}
	
	public void initPanelLookupButtonsStockQtyList(int iId, BigDecimal dQty) {
		for (int i = 0; i < m_oButtonPanelLookupArray.size(); i++) {
			FrameGeneralLookupButton oLookupButton = m_oButtonPanelLookupArray.get(i);
			String sNote = m_oButtonPanelLookupArray.get(i).getButtonClickServerRequestNote();
			
			try {
				if(sNote.isEmpty()) // button type is no function, no need to update stock qty
					continue;
				
				JSONObject oJSONObject = new JSONObject(sNote);
				String sType = oJSONObject.optString(FrameLookupButton.BUTTON_NOTE_TYPE);
				int iButtonId = oJSONObject.optInt(FrameLookupButton.BUTTON_NOTE_ID);
				if(!sType.equals(PosDisplayPanelLookup.TYPE_HOT_ITEM))
						continue;
				
				if (iId == iButtonId) {
					if (m_oPanelLookupButtonsHaveStockQtyList.containsKey(iId)) {
						boolean bFound = false;
						for (FrameGeneralLookupButton oTmpLookupButton : m_oPanelLookupButtonsHaveStockQtyList.get(iId)) {
							if (oLookupButton == oTmpLookupButton) {
								bFound = true;
								break;
							}
						}
						
						if (!bFound)
							m_oPanelLookupButtonsHaveStockQtyList.get(iId).add(oLookupButton);
					} else {
						List<FrameGeneralLookupButton> oFrameLookupButtonList = new ArrayList<FrameGeneralLookupButton>();
						oFrameLookupButtonList.add(oLookupButton);
						m_oPanelLookupButtonsHaveStockQtyList.put(iId, oFrameLookupButtonList);
					}
					setItemStockQty(iId, dQty);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	//update quantity button by comparing the latest outlet item list and existing panel lookup buttons with stock quantity 
	public void updateStockQtyButton(List<PosOutletItem> oOutletItemList) {
		//Checking deleted outlet item
		List<Integer> oExistingItemIds = new ArrayList<Integer>();
		for (Map.Entry<Integer, List<FrameGeneralLookupButton>> entry : m_oPanelLookupButtonsHaveStockQtyList.entrySet())
			oExistingItemIds.add(entry.getKey());
		
		for (Integer oItemId: oExistingItemIds) {
			boolean bFound = false;
			for (PosOutletItem oItemStock: oOutletItemList) {
				if (oItemStock.getItemId() == oItemId.intValue()) {
					bFound = true;
					break;
				}
			}
			
			if (!bFound)
				removeItemStockQty(oItemId);
		}
		
		//Checking added and changed stock list
		for (PosOutletItem oItemStock: oOutletItemList) {
			int iItemId = oItemStock.getItemId();
			
			if (m_oPanelLookupButtonsHaveStockQtyList.containsKey(iItemId)) {
				//Have stock indicator
				for (FrameGeneralLookupButton oFrameLookupButton: m_oPanelLookupButtonsHaveStockQtyList.get(iItemId)) {
					if (!oFrameLookupButton.getButtonStockQtyValue().equals(FrameLookupButton.QUANTITY_SOLDOUT)) {
						BigDecimal oOriQty = new BigDecimal(oFrameLookupButton.getButtonStockQtyValue());
						if (oItemStock.isSoldout()) {
							// not soldout originally and now change to soldout
							removeItemStockQty(iItemId);
							initPanelLookupButtonsStockQtyList(iItemId, null);
							break;
						} else if(oOriQty.compareTo(oItemStock.getStockQty()) != 0) {
							//not soldout originally and now the value is changed
							removeItemStockQty(iItemId);
							initPanelLookupButtonsStockQtyList(iItemId, oItemStock.getStockQty());
							break;
						}
					} else {
						if(!oItemStock.isSoldout()) {
							//soldout originally and now change to having stock value
							removeItemStockQty(iItemId);
							initPanelLookupButtonsStockQtyList(iItemId, oItemStock.getStockQty());
							break;
						}
					}
				}
			} else {
				if(oItemStock.isSoldout())
					initPanelLookupButtonsStockQtyList(iItemId, null);
				else
					initPanelLookupButtonsStockQtyList(iItemId, oItemStock.getStockQty());
			}
		}
	}
	
	public void setItemPrice(int iId, String sPrice) {
		for (int i = 0; i < m_oButtonPanelLookupArray.size(); i++) {
			FrameGeneralLookupButton oLookupButton = m_oButtonPanelLookupArray.get(i);
			if (oLookupButton.isNoFunctionButton()) // No Function, no need to handle click server request note
				continue;
			
			String sNote = m_oButtonPanelLookupArray.get(i).getButtonClickServerRequestNote();
			try {
				JSONObject oJSONObject = new JSONObject(sNote);
				String sType = oJSONObject.optString(FrameLookupButton.BUTTON_NOTE_TYPE);
				int iButtonId = oJSONObject.optInt(FrameLookupButton.BUTTON_NOTE_ID);
				if (!sType.equals(PosDisplayPanelLookup.TYPE_HOT_ITEM))
					continue;
				
				if (iId == iButtonId)
					oLookupButton.setButtonOtherInformation(sPrice, false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Set the mode to direct payment mode, cannot click any button except payment button
	public void setDirectPaymentMode(boolean bStart) {
		m_bDirectPaymentMode = bStart;
	}
	
	// Set the mode to use panel to select item mode, cannot click any button except hot item and lookup button
	public void setUsePanelToSelectItemMode(boolean bStart) {
		m_bUsePanelToSelectItemMode = bStart;
	}
	
	// Set the mode to use panel to select payment method mode, cannot click any button except payment button
	public void setUsePanelToSelectPaymentMethodMode(boolean bStart) {
		m_bUsePanelToSelectPaymentMethodMode = bStart;
	}
	
	public void closeMenuLookup() {
		for(FrameOrderingPanelListener listener : listeners)
			listener.frameOrderingPanel_tabClicked();
	}
	
	public void addUpdateMenuItemTimer(){
		// TODO : setup interval
		this.addTimer("update_menu_item", 10000, true, "update_menu_item", false, true, null);
	}
	
	public void setUpdateMenuItemTimer(boolean bStart){
		this.controlTimer("update_menu_item", bStart);
	}
	
	public void addAutoClearOctopusDisplayTimer(){
		// 5s to clear Octopus Display
		this.addTimer("auto_clear_octopus_display", 5000, false, "auto_clear_octopus_display", false, true, null);
	}
	
	public void setAutoClearOctopusDisplayTimer(boolean bStart){
		this.controlTimer("auto_clear_octopus_display", bStart);
	}
	
	// Thread function - update menu item
	public void updateMenuItem(int iClientSockId){
		// Set the last client socket ID
		AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);

		for (FrameOrderingPanelListener listener : listeners) {
			listener.frameOrderingPanel_UpdateMenuItem();
		}
		
		// Send the UI packet to client and the thread is finished
		super.getParentForm().finishUI(true);
	}
	
	public void setCheckOrderingTimeout(int iTimeout){
		this.addTimer("ordering_timeout", iTimeout, false, "ordering_timeout", true, true, null);
	}
	
	public void setCheckOrderingTimeoutTimer(boolean bStart){
		this.controlTimer("ordering_timeout", bStart);
	}
	
	// Manual click check ordering timeout
	public boolean isOrderingTimeout(){
		boolean bTimeout = false;
		
		for (FrameOrderingPanelListener listener : listeners) {
			bTimeout = listener.frameOrderingPanel_OrderingTimeout();
			break;
		}
		
		return bTimeout;
	}
	
	// Create update basket timer
	public void setCheckCashierTimeout(int iTimeout){
		this.addTimer("cashier_timeout", iTimeout, false, "cashier_timeout", true, true, null);
	}
	
	// Start to update basket
	public void setCheckCashierTimeoutTimer(boolean bStart){
		this.controlTimer("cashier_timeout", bStart);
	}
	
	public FrameCommonPageContainer getCommonPageContainer(){
		return m_oCommonPageTabList;
	}
	
	// Thread function - check ordering timeout
	public void orderingTimeout(int iClientSockId){
		// Set the last client socket ID
		AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);

		for (FrameOrderingPanelListener listener : listeners) {
			listener.frameOrderingPanel_OrderingTimeout();
		}
		
		// Send the UI packet to client and the thread is finished
		super.getParentForm().finishUI(true);
	}
	
	public void setTagTextSize(int iSelectedTagTextSize, int iUnselectTagTextSize){
		m_oCommonPageSelectedTabList.setTextSize(iSelectedTagTextSize);
		m_oCommonPageUnselectTabList.setTextSize(iUnselectTagTextSize);
	}
	
	public void setTagHeightByPercentage(int iPercentage){
		int iHeight = this.getHeight() * iPercentage / 100;
		m_oCommonPageSelectedTabList.setHeight(iHeight);
		m_oCommonPageUnselectTabList.setHeight(iHeight);
	}
	
	public void showDisplayPanelById(int iPageId) {
		int iSubPanelCnt = 0;
		int iPageIndex = 0;
		if (m_oPageIdPageIndexPairs.containsKey(iPageId) == false)
			return;
		else {
			iPageIndex = m_oPageIdPageIndexPairs.get(iPageId) - 1;
			// Skip the sub panel index
			for (int i = 1; i <= iPageIndex; i++ ) {
				if(m_oPageIndexPageTypePairs.get(i).equals("s"))
					iSubPanelCnt++;
			}
			m_oCommonPageTabList.showPageByIndex(iPageIndex - iSubPanelCnt);
		}
	}
	
	public ArrayList<FrameGeneralLookupButton> getButtonFromListByIdAndType(int iId, String sType){
		ArrayList<FrameGeneralLookupButton> oFrameGeneralLookupButtonList = new ArrayList<>();
		for (FrameGeneralLookupButton oFrameGeneralLookupButton : m_oButtonPanelLookupArray) {
			if (oFrameGeneralLookupButton.getCurrentButton().getButtonItemId() == iId && oFrameGeneralLookupButton.getCurrentButton().getButtonType().equals(sType))
				oFrameGeneralLookupButtonList.add(oFrameGeneralLookupButton);
		}
		return oFrameGeneralLookupButtonList;
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		//Panel Name Button clicked
		for (Entry<Integer, String[]> entry : m_oCommonPageTabNameList.entrySet()) {
			if(entry.getKey() == ((iChildId-1)/2))
				showDisplayPanelAtPage(0);
		}
		
		for (VirtualUIFrame tabBaseFrame : m_oFramePageTabArray) {
			if (tabBaseFrame.getId() == iChildId) {
				// Check if ordering is timeout
				if (isOrderingTimeout())
					return true;

				//showDisplayPanelAtPage(Integer.parseInt(sNote));
				showDisplayPanelAtPage(0);
				m_oCommonPageTabList.clickTag((Integer.parseInt(sNote) - 1) * 2 );
				
				closeMenuLookup();
				return true;
			}
		}
				
		return false;
	}

	public void showFirstTag(){
		m_oCommonPageTabList.showFirstTag();
	}
	
	@Override
	public boolean longClicked(int iChildId, String sNote) {

		// Version 0.0.0.7 , according to Product team, all panel long click event = click event
		/*
		//Display Panel Lookup button long clicked
		for(FrameLookupButton oFrameLookupButton : m_oButtonPanelLookupArray) {
			if(oFrameLookupButton.getId() == iChildId) {
				String splitedNote[] = sNote.split("_");
				if(splitedNote.length > 0) {
					String type = splitedNote[0];
					FrameOrderingPanelListener listener = listeners.get(0);
					if(type.equals("i")) {
						listener.frameOrderingPanel_hotItemLongClicked(splitedNote[1]);
					}
					return true;
				}
				
			}
		}
		
		return false;
		*/
		
		// Same behavior as click
		return clicked(iChildId, sNote);
	}

	@Override
	public boolean swipeRight(int iChildId, String sNote) {
		boolean bMatchChild = false;

		int iCurrentTableFloorPlanIndex = m_oCommonPageTabList.getCurrentIndex();
		for(VirtualUIFrame oFrameDisplayPanel : m_oFrameDisplayPanelArray){
			if(oFrameDisplayPanel.getId() == iChildId){
				if(iCurrentTableFloorPlanIndex < m_oCommonPageTabList.getTotalIndex() - 1) {
					showDisplayPanelAtPage(0);
					m_oCommonPageTabList.clickTag(iCurrentTableFloorPlanIndex + 1);
				}
				
				bMatchChild = true;
				break;
			}
		}
		return bMatchChild;
	}
	
	@Override
	public boolean swipeLeft(int iChildId, String sNote) {
		boolean bMatchChild = false;

		int iCurrentTableFloorPlanIndex = m_oCommonPageTabList.getCurrentIndex();
		for(VirtualUIFrame oFrameDisplayPanel : m_oFrameDisplayPanelArray){
			if(oFrameDisplayPanel.getId() == iChildId){
				if(iCurrentTableFloorPlanIndex > 0) {
					showDisplayPanelAtPage(0);
					m_oCommonPageTabList.clickTag(iCurrentTableFloorPlanIndex - 1);
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
			if (sNote.equals("update_menu_item")){
				// Update menu item
				
				//Create parameter array
				Object[] oParameters = new Object[1];
				oParameters[0] = iClientSockId;
				
				this.getParentForm().addTimerThread(iClientSockId, this, "updateMenuItem", oParameters);
			} else if(sNote.equals("ordering_timeout")){
				// Ordering timeout
				AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
				
				for (FrameOrderingPanelListener listener : listeners) {
					listener.frameOrderingPanel_OrderingTimeout();
				}
				
				// Send the UI packet to client and the thread is finished
				super.getParentForm().finishUI(true);
			} else if(sNote.equals("auto_clear_octopus_display")){
				//Set the last client socket ID
				AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
				
				for (FrameOrderingPanelListener listener : listeners) {
					listener.frameOrderingPanel_clearOctopusDisplay();
				}
				
				// Send the UI packet to client and the thread is finished
				super.getParentForm().finishUI(true);
			} else if (sNote.equals("cashier_timeout")){
				// Cashier timeout
				AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
				
				for (FrameOrderingPanelListener listener : listeners) {
					listener.frameOrderingPanel_CashierTimeout();
				}
				
				// Send the UI packet to client and the thread is finished
				super.getParentForm().finishUI(true);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean valueChanged(int iChildId, String sNote) {
		boolean bMatchChild = false;

		ClsActiveClient oActiveClient = AppGlobal.getActiveClient();
		if (oActiveClient != null && oActiveClient.getSwipeCardReaderElement().getValueChangedServerRequestNote()
				.equals(FuncMSR.FRAME_SWIPE_CARD_DEFAULT)) {
			if (iChildId == oActiveClient.getSwipeCardReaderElement().getId()) {
				for (FrameOrderingPanelListener listener : listeners) {
					listener.frameOrderingPanel_barcodeReaded(this.getId(),
							oActiveClient.getSwipeCardReaderElement().getValue());
				}
				bMatchChild = true;
			}
		}
		
		return bMatchChild;
	}
	
	// ***** DEBUG *****
	public void autoClickItem(){
		if (AppGlobal.g_iDebugMode != 1)
			return;
		
		for (FrameGeneralLookupButton oFrameLookupButton: m_oButtonPanelLookupArray) {
			if (!oFrameLookupButton.getButtonClickServerRequestNote().isEmpty()) {
				try {
					JSONObject oJSONObject = new JSONObject(oFrameLookupButton.getButtonClickServerRequestNote());
					String type = oJSONObject.optString(FrameLookupButton.BUTTON_NOTE_TYPE);
					if(type.equals(PosDisplayPanelLookup.TYPE_HOT_ITEM)){
						oFrameLookupButton.doAutoClick(100);
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void frameGeneralLookupButton_clicked(String sNote) {
		if (!sNote.isEmpty()) {
			// Check if ordering is timeout
			if(isOrderingTimeout())
				return;
			
			try {
				JSONObject oJSONObject = new JSONObject(sNote);
				String type = oJSONObject.optString(FrameLookupButton.BUTTON_NOTE_TYPE);
				int iButtonId = oJSONObject.optInt(FrameLookupButton.BUTTON_NOTE_ID);
				JSONArray sNameJSONArray = new JSONArray(oJSONObject.optString(FrameLookupButton.BUTTON_NOTE_NAME));
				String[] sName = null;
				if (sNameJSONArray.length() > 0) {
					sName = new String[sNameJSONArray.length()];
					for (int i = 0; i < sNameJSONArray.length(); i++) {
						sName[i] = sNameJSONArray.getString(i);
					}
				}
				String sParameter = oJSONObject.optString(FrameLookupButton.BUTTON_NOTE_PARAMETER);
				
				if( m_bDirectPaymentMode){
					// Under direct payment mode, only payment key can be pressed
					if(type.equals(PosDisplayPanelLookup.TYPE_DIRECT_PAYMENT) == false)
						return;
				}
				
				if (m_bUsePanelToSelectItemMode){
					// Under use panel to select item mode, only hot item key and item lookup can be pressed
					if (type.equals(PosDisplayPanelLookup.TYPE_HOT_ITEM) == false
							&& type.equals(PosDisplayPanelLookup.TYPE_MENU_LOOKUP) == false
							&& type.equals(PosDisplayPanelLookup.TYPE_SUB_PANEL_PAGE) == false)
						return;
				}
				
				if (m_bUsePanelToSelectPaymentMethodMode) {
					// Under use panel to select payment method mode, only payment can be pressed
					if (type.equals(PosDisplayPanelLookup.TYPE_DIRECT_PAYMENT) == false)
						return;
				}
				
				FrameOrderingPanelListener listener = listeners.get(0);
				if (type.equals(PosDisplayPanelLookup.TYPE_HOT_ITEM)) {
					listener.frameOrderingPanel_hotItemClicked(iButtonId);
					
					// ***** DEBUG *****
					// Auto click the first direct payment key to go to cashier screen to do payment
					if (AppGlobal.g_iDebugMode == 1){
						for(FrameGeneralLookupButton oFrameLookupButton1 : m_oButtonPanelLookupArray) {
							String splitedNote1[] = oFrameLookupButton1.getClickServerRequestNote().split("_");
							if(splitedNote1.length > 0) {
								String type1 = splitedNote1[0];
								if(type1.equals(PosDisplayPanelLookup.TYPE_DIRECT_PAYMENT)){
									oFrameLookupButton1.doAutoClick(100);
									break;
								}
							}
						}
					}
					// *****************
				} else if (type.equals(PosDisplayPanelLookup.TYPE_FUNCTION))
					listener.frameOrderingPanel_functionClicked(iButtonId, sName, sParameter);
				else if (type.equals(PosDisplayPanelLookup.TYPE_MENU_LOOKUP))
					listener.frameOrderingPanel_lookupClicked(iButtonId);
				else if (type.equals(PosDisplayPanelLookup.TYPE_HOT_MODIFIER))
					listener.frameOrderingPanel_hotModifierClicked(iButtonId, sParameter);
				else if (type.equals(PosDisplayPanelLookup.TYPE_MODIFIER_LOOKUP))
					listener.frameOrderingPanel_modifierLookupClicked(iButtonId, sParameter);
				else if (type.equals(PosDisplayPanelLookup.TYPE_DIRECT_PAYMENT))
					listener.frameOrderingPanel_paymentClicked(iButtonId, sParameter);
				else if (type.equals(PosDisplayPanelLookup.TYPE_SUB_PANEL_PAGE)) 
					listener.frameOrderingPanel_subPanelPageClicked(iButtonId);
				else if (type.equals(PosDisplayPanelLookup.TYPE_DIRECT_DISCOUNT))
					listener.frameOrderingPanel_discountClicked(iButtonId, sParameter);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void frameGeneralLookupButton_addQtyClicked(String sNOte) {
	}

	@Override
	public void frameGeneralLookupButton_minusQtyClicked(String sNote) {
	}

	@Override
	public void frameCommonPageContainer_changeFrame() {
		closeMenuLookup();
	}

	@Override
	public boolean frameCommonPageContainer_swipe(boolean bLeft, String sNote, int iChildId) {
		boolean bMatchChild = false;
		for(VirtualUIFrame oFrameDisplayPanel : m_oFrameDisplayPanelArray){
			if(oFrameDisplayPanel.getId() == iChildId){
				if(bLeft){
					if(this.getEnabled() && this.getVisible()){
						if(Integer.parseInt(sNote) >= 2)
							m_oCommonPageTabList.clickTag((Integer.parseInt(sNote) - 2) * 2 );
						bMatchChild = true;
					}
				} else {
					if(this.getEnabled() && this.getVisible()){
						if(Integer.parseInt(sNote) < m_oCommonPageTabList.getTotalIndex())
							m_oCommonPageTabList.clickTag(Integer.parseInt(sNote) * 2);
						bMatchChild = true;
					}
				}
			}
		}
		return bMatchChild;
	}
	
	@Override
	public void frameCommonPageContainer_updateFrame(int iOrigIndex, int iIndex, String sFrameName) {
		// TODO Auto-generated method stub
		VirtualUIFrame oFrame = null;
		for (int i = 1; i <= m_oFrameDisplayPanelArray.size(); i++) {
			oFrame = m_oFrameDisplayPanelArray.get(i-1);
			oFrame.setVisible(false);
		}
	}

	@Override
	public void frameCommonPageContainer_CloseImageClicked() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void frameCommonPageContainer_ClickButtonIndex(int iIndex) {
		// TODO Auto-generated method stub
		
	}
}
