package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import commonui.FrameTitleHeader;
import externallib.StringLib;

import om.MenuMenu;
import om.MenuMenuLookup;
import om.PosItemRemindRule;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameItemRemindListener {
	void frameItemRemind_exitClicked();
	void frameItemRemind_addItem(int iItemId);
	void frameItemRemind_sendCheck(String sRemindMode,String sStatus);
}

public class FrameItemRemind extends VirtualUIFrame implements FrameCommonBasketListener {
	public final static String STATUS_SUGGEST = "suggest";
	public final static String STATUS_FORCE = "force";
	
	private TemplateBuilder m_oTemplateBuilder;
	private VirtualUIFrame m_oFrameCover;
	private FrameTitleHeader m_oFrameTitleHeader;
	
	private VirtualUIButton m_oButtonBackToOrder;
	private VirtualUIButton m_oButtonSendCheck;
	
	private FrameCommonBasket m_oFrameItemList;

	private VirtualUIFrame m_oFramePage;
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIImage m_oImgButtonNextPage;

	private HashMap<Integer, Integer> m_oItemRemindRuleList;
	private HashMap<String, Integer> m_oColumnWidths;
	private int m_iRowHeight;
	private int m_iCurrentPageStartNo;
	private int m_iListItemNumber = 12;
	private int m_iScrollIndex = 0;
	private String m_sFunctionKey = "";
	private String m_sStatus = "";
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameItemRemindListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameItemRemindListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameItemRemindListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
	public FrameItemRemind(String sTemplateFile, VirtualUIFrame oFrameCover){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameItemRemindListener>();
		m_oColumnWidths = new HashMap<String, Integer>();
		m_oItemRemindRuleList = new HashMap<Integer,Integer>();
		
		m_iCurrentPageStartNo = 0;
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_iListItemNumber = 9;
		
		// Set cover frame
		m_oFrameCover = oFrameCover;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate(sTemplateFile);
		
		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(false);
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("item_reminder", ""));
		this.attachChild(m_oFrameTitleHeader);
		
		//exit button
		m_oButtonBackToOrder = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonBackToOrder, "btnBackToOrder");
		m_oButtonBackToOrder.setValue(AppGlobal.g_oLang.get()._("back_to_order", ""));
		m_oButtonBackToOrder.setVisible(true);
		this.attachChild(m_oButtonBackToOrder);
		
		//send check button
		m_oButtonSendCheck = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSendCheck, "btnSendCheck");
		m_oButtonSendCheck.setVisible(true);
		this.attachChild(m_oButtonSendCheck);
		
		// Result list
		m_oFrameItemList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFrameItemList, "fraItemList");
		m_oFrameItemList.init();
		m_oFrameItemList.addListener(this);
		this.attachChild(m_oFrameItemList);
		
		// Set column widths
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oColumnWidths.put("code", 85);
			m_oColumnWidths.put("name", 150);
			m_oColumnWidths.put("price", 95);
			m_oColumnWidths.put("order", 140);
			m_iRowHeight = 50;
		}else {
			m_oColumnWidths.put("code", 200);
			m_oColumnWidths.put("name", 350);
			m_oColumnWidths.put("price", 150);
			m_oColumnWidths.put("order", 160);
			m_iRowHeight = 0;
		}
		
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
    	ArrayList<String[]> sFieldValues = new ArrayList<String[]>();
    	
    	iFieldWidths.add(m_oColumnWidths.get("code").intValue());
    	sFieldValues.add(AppGlobal.g_oLang.get()._("item_code", ""));
    	iFieldWidths.add(m_oColumnWidths.get("name").intValue());
    	sFieldValues.add(AppGlobal.g_oLang.get()._("item_name", ""));
    	iFieldWidths.add(m_oColumnWidths.get("price").intValue());
    	sFieldValues.add(AppGlobal.g_oLang.get()._("price", ""));
    	iFieldWidths.add(m_oColumnWidths.get("order").intValue());
    	sFieldValues.add(AppGlobal.g_oLang.get()._("order", ""));
    	
    	m_oFrameItemList.addHeader(iFieldWidths, sFieldValues);
    	m_oFrameItemList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
    	m_oFrameItemList.setHeaderFormat(0, 20, "");

		//Create Page label
		m_oFramePage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePage, "fraPage");

		// Create prev page button
		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevPage");
		m_oImgButtonPrevPage.allowClick(true);
		m_oImgButtonPrevPage.setClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.allowLongClick(true);
		m_oImgButtonPrevPage.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oImgButtonPrevPage);
		
		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.allowLongClick(true);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oImgButtonNextPage);
	
    	m_oLblPage = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
    	m_oLblPage.setWidth(m_oFramePage.getWidth());
    	m_oLblPage.setHeight(m_oFramePage.getHeight());
    	m_oFramePage.attachChild(m_oLblPage);
    	this.attachChild(m_oFramePage);
    	
	}	
	public void addItemList(ArrayList<PosItemRemindRule> oItemSuggest, ArrayList<PosItemRemindRule> oItemForce,
			ArrayList<PosItemRemindRule> oMenuSuggest, ArrayList<PosItemRemindRule> oMenuForce) {
		int iCount = 0;
		//set send check button value
		//suggest only
		if((oItemSuggest.size() > 0 || oMenuSuggest.size() > 0) && (oItemForce.size() == 0 && oMenuForce.size() == 0)) {
			m_oButtonSendCheck.setValue(AppGlobal.g_oLang.get()._("ignore_and_send", ""));
			m_sStatus = FrameItemRemind.STATUS_SUGGEST;
		//suggest & force
		}else {
			m_oButtonSendCheck.setValue(AppGlobal.g_oLang.get()._("send_or_print_check", ""));
			m_sStatus = FrameItemRemind.STATUS_FORCE;
		}
		FuncMenu oFuncMenu = new FuncMenu();
		FuncMenuItem oFuncMenuItem = new FuncMenuItem(null);
		
		//add Item Force and Menu Force first
		if(oItemForce.size() > 0) {
			addItemRemindSection(iCount++, AppGlobal.g_oLang.get()._("must_select_all", ""));
			for(PosItemRemindRule oItemRemindRule : oItemForce){
				oFuncMenuItem = oFuncMenu.getFuncMenuItemByItemId(oItemRemindRule.getItemId());
				addItem(0, iCount, oFuncMenuItem.getMenuItem().getCode(), 
						oFuncMenuItem.getMenuItem().getName(AppGlobal.g_oCurrentLangIndex.get()),
						oFuncMenuItem.getMenuItem().getBasicPriceByPriceLevel(AppGlobal.g_oFuncOutlet.get().getPriceLevel()));
				m_oItemRemindRuleList.put(iCount, oItemRemindRule.getItemId());
				iCount++;
			}
		}
		
		if(oMenuForce.size() > 0){
			int iMinOrder = 0;
			MenuMenu oMenu = new MenuMenu();
			for(PosItemRemindRule oPosItemRemindRule : oMenuForce) {
				int iMenuLookupItemTotal = 0;
				oMenu = AppGlobal.g_oFuncMenu.get().getMenuAndContentById(oPosItemRemindRule.getMenuId());
				for(MenuMenuLookup oMenuLookup : oMenu.getMenuLookupList()) { 
					if(oMenuLookup.isMenuItem())
						iMenuLookupItemTotal++;
				}
				
				if(oPosItemRemindRule.getMinOrder() == 0)
					iMinOrder = iMenuLookupItemTotal;
				else if(oPosItemRemindRule.getMinOrder() > 0)
					iMinOrder = oPosItemRemindRule.getMinOrder();
				addItemRemindSection(iCount++, AppGlobal.g_oLang.get()._("must_select", 
											" ( ", iMinOrder, " ", AppGlobal.g_oLang.get()._("from"), " ", iMenuLookupItemTotal, " )"));
				
				for(MenuMenuLookup oMenuLookup : oMenu.getMenuLookupList()) {
					if(oMenuLookup.isSubMenu())
						continue;
					
					addItem(0, iCount, oMenuLookup.getMenuItem().getCode(), 
							oMenuLookup.getMenuItem().getName(AppGlobal.g_oCurrentLangIndex.get()),
							oMenuLookup.getMenuItem().getBasicPriceByPriceLevel(AppGlobal.g_oFuncOutlet.get().getPriceLevel()));
					m_oItemRemindRuleList.put(iCount, oMenuLookup.getMenuItem().getItemId());
					iCount++;
				}
			}
		}
		
		//add item/menu suggest second
		if(oItemSuggest.size() > 0){
			addItemRemindSection(iCount++, AppGlobal.g_oLang.get()._("advice_to_select", ""));
			for(PosItemRemindRule oItemRemindRule : oItemSuggest){
				oFuncMenuItem = oFuncMenu.getFuncMenuItemByItemId(oItemRemindRule.getItemId());
				addItem(0, iCount, oFuncMenuItem.getMenuItem().getCode(), 
						oFuncMenuItem.getMenuItem().getName(AppGlobal.g_oCurrentLangIndex.get()),
						oFuncMenuItem.getMenuItem().getBasicPriceByPriceLevel(AppGlobal.g_oFuncOutlet.get().getPriceLevel()));
				m_oItemRemindRuleList.put(iCount, oItemRemindRule.getItemId());
				iCount++;
			}
		}
		
		if(oMenuSuggest.size() > 0){
			int iMinOrder = 0;
			MenuMenu oMenu = new MenuMenu();
			for(PosItemRemindRule oPosItemRemindRule : oMenuSuggest) {
				int iMenuLookupItemTotal = 0;
				oMenu = AppGlobal.g_oFuncMenu.get().getMenuAndContentById(oPosItemRemindRule.getMenuId());
				for(MenuMenuLookup oMenuLookup : oMenu.getMenuLookupList()) { 
					if(oMenuLookup.isMenuItem())
						iMenuLookupItemTotal++;
				}
				
				if(oPosItemRemindRule.getMinOrder() == 0)
					iMinOrder = iMenuLookupItemTotal;
				else if(oPosItemRemindRule.getMinOrder() > 0)
					iMinOrder = oPosItemRemindRule.getMinOrder();
				addItemRemindSection(iCount++, AppGlobal.g_oLang.get()._("advice_to_select",
											" ( ", iMinOrder, " ", AppGlobal.g_oLang.get()._("from"), " ", iMenuLookupItemTotal, " ) "));
				
				for(MenuMenuLookup oMenuLookup : oMenu.getMenuLookupList()) {
					if(oMenuLookup.isSubMenu())
						continue;
					
					addItem(0, iCount, oMenuLookup.getMenuItem().getCode(), 
							oMenuLookup.getMenuItem().getName(AppGlobal.g_oCurrentLangIndex.get()),
							oMenuLookup.getMenuItem().getBasicPriceByPriceLevel(AppGlobal.g_oFuncOutlet.get().getPriceLevel()));
					m_oItemRemindRuleList.put(iCount, oMenuLookup.getMenuItem().getItemId());
					iCount++;
				}
			}
		}
		
		//update next/prev button value
		updatePage();
	}
	
	public void addItem(int iSectionId, int iItemIndex, String sItemCode, String sItemName, BigDecimal oItemPrice) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		ArrayList<String> sFieldTypes = new ArrayList<String>();

		iFieldWidths.add(m_oColumnWidths.get("code").intValue());
		sFieldValues.add(sItemCode);
//RayHuen 23/10/2017 ------Start---------
		sFieldAligns.add("");
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		iFieldWidths.add(m_oColumnWidths.get("name").intValue());
		sFieldValues.add(sItemName);
		sFieldAligns.add("");
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		iFieldWidths.add(m_oColumnWidths.get("price").intValue());
		BigDecimal oTmpPrice;
		if(oItemPrice != null)
			oTmpPrice = oItemPrice;
		else
			oTmpPrice = BigDecimal.ZERO;
		String sPrice = StringLib.BigDecimalToString(oTmpPrice, AppGlobal.g_oFuncOutlet.get().getItemRoundDecimal());
		sFieldValues.add(sPrice);
		sFieldAligns.add("");
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		iFieldWidths.add((m_oColumnWidths.get("order").intValue()));
		sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/search_item_order_icon.png");
		sFieldAligns.add("");
//RayHuen 23/10/2017 ------End---------
		sFieldTypes.add(HeroActionProtocol.View.Type.IMAGE);
		m_oFrameItemList.addItem(iSectionId, iItemIndex, m_iRowHeight, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
		m_oFrameItemList.setFieldTextSize(iSectionId, iItemIndex, 0, 16);
		m_oFrameItemList.setFieldTextSize(iSectionId, iItemIndex, 1, 16);
		m_oFrameItemList.setFieldTextSize(iSectionId, iItemIndex, 2, 16);
	}

	public void addItemRemindSection(int iItemIndex, String[] sSectionName) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String[]> sFieldValues = new ArrayList<String[]>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		iFieldWidths.add(1220);
		sFieldValues.add(sSectionName);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.LEFT + "," + HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		m_oFrameItemList.addItem(0, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
		m_oFrameItemList.setFieldBackgroundColor(0, iItemIndex, 0, "#E1ECF8");
		m_oFrameItemList.setFieldForegroundColor(0, iItemIndex, 0, "#333333");
		m_oFrameItemList.setFieldTextSize(0, iItemIndex, 0, 16);
		m_oFrameItemList.setFieldTextSize(0, iItemIndex, 1, 16);
		m_oFrameItemList.setFieldTextSize(0, iItemIndex, 2, 16);
		m_oFrameItemList.allowClick(false);
	}
	
	public void clearItemList() {
		m_oFrameItemList.removeAllItems(0);
	}
	
	public void updatePage() {
		boolean bShowPageUp = false;
		boolean bShowPageDown = false;
		int iPage = 0;
		int iCurrentPanelRecordCount = 0;
		
		iCurrentPanelRecordCount = m_oFrameItemList.getItemCount(0);
		if(iCurrentPanelRecordCount > m_iListItemNumber)
			iPage = (m_iCurrentPageStartNo/m_iListItemNumber)+1;
		
		if(m_iCurrentPageStartNo > 0)
			bShowPageUp = true;
		
		if(iCurrentPanelRecordCount > m_iListItemNumber && m_iCurrentPageStartNo+m_iListItemNumber < iCurrentPanelRecordCount)
			bShowPageDown = true;

		setPageNumber(iPage);
		showPageUp(bShowPageUp);
		showPageDown(bShowPageDown);
	}
	
	public void resetButtonDesc() {
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("item_reminder", ""));
		m_oFrameItemList.removeAllSections();   	
    	// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
    	ArrayList<String[]> sFieldValues = new ArrayList<String[]>();
    	iFieldWidths.add(m_oColumnWidths.get("code").intValue());
    	sFieldValues.add(AppGlobal.g_oLang.get()._("item_code", ""));
    	iFieldWidths.add(m_oColumnWidths.get("name").intValue());
    	sFieldValues.add(AppGlobal.g_oLang.get()._("item_name", ""));
    	iFieldWidths.add(m_oColumnWidths.get("price").intValue());
    	sFieldValues.add(AppGlobal.g_oLang.get()._("price", ""));
    	iFieldWidths.add(m_oColumnWidths.get("order").intValue());
    	sFieldValues.add(AppGlobal.g_oLang.get()._("order", ""));
    	m_oFrameItemList.addHeader(iFieldWidths, sFieldValues);
    	m_oFrameItemList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
    	m_oFrameItemList.setHeaderFormat(0, 20, "");
	}
	
	public void resetFrame() {
		m_oFrameCover.setVisible(true);
		m_oFrameCover.bringToTop();
		this.bringToTop();
		
		m_oFrameItemList.moveScrollToTop();
		m_iCurrentPageStartNo = 0;
		m_iScrollIndex = 0;
	}

    public void setPageNumber(int iNumber) {
    	if(iNumber > 0) {
    		int iTotalPage = (int)Math.ceil(1.0*m_oFrameItemList.getItemCount(0)/m_iListItemNumber);
    		m_oFramePage.setVisible(true);
    		m_oLblPage.setValue(iNumber+"/"+iTotalPage);
    		m_oLblPage.setVisible(true);
    		
    		m_oImgButtonPrevPage.setVisible(true);
    		m_oImgButtonNextPage.setVisible(true);
    	} else {
    		m_oFramePage.setVisible(false);
    		m_oImgButtonPrevPage.setVisible(false);
    		m_oImgButtonNextPage.setVisible(false);
    	}
    }

    public void showPageUp(boolean bShow) {
		if(bShow)
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_prev_page_button.png");
		else
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_prev_page_button_unclick.png");
		m_oImgButtonPrevPage.setEnabled(bShow);
    }
    
    public void showPageDown(boolean bShow) {
		if(bShow)
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_next_page_button.png");
		else
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_next_page_button_unclick.png");
		m_oImgButtonNextPage.setEnabled(bShow);
    }
    
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		// Find the clicked button
		if (m_oButtonBackToOrder.getId() == iChildId) {
			for(FrameItemRemindListener listener: listeners) {
				resetFrame();
				this.m_oFrameCover.setVisible(false);
				listener.frameItemRemind_exitClicked();
			}
			bMatchChild = true;
		} else if (m_oButtonSendCheck.getId() == iChildId) {
			for(FrameItemRemindListener listener: listeners) {
				resetFrame();
				this.m_oFrameCover.setVisible(false);
				listener.frameItemRemind_sendCheck(m_sFunctionKey,m_sStatus);
			}
			bMatchChild = true;
        } else if (m_oImgButtonNextPage.getId() == iChildId) {
			if(m_iCurrentPageStartNo+m_iListItemNumber < m_oFrameItemList.getItemCount(0)) {
				m_iCurrentPageStartNo += m_iListItemNumber;
				updatePage();
				m_iScrollIndex += m_iListItemNumber;
				m_oFrameItemList.moveScrollToItem(0, m_iScrollIndex);
			}
			
			bMatchChild = true;
        } else if (m_oImgButtonPrevPage.getId() == iChildId) {
			 // PAGE UP
			if(m_iCurrentPageStartNo-m_iListItemNumber >= 0) {
				m_iCurrentPageStartNo -= m_iListItemNumber;
				updatePage();
				m_iScrollIndex -= m_iListItemNumber;
				m_oFrameItemList.moveScrollToItem(0, m_iScrollIndex);
			}
			bMatchChild = true;
        } 
		return bMatchChild;
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		int iItemId = m_oItemRemindRuleList.get(iItemIndex);
		if(iFieldIndex == 3) {
			for(FrameItemRemindListener listener: listeners) {
				listener.frameItemRemind_addItem(iItemId);
			}
		}
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}

	public String getFunctionKey() {
		return m_sFunctionKey;
	}

	public void setFunctionKey(String sFunctionKey) {
		this.m_sFunctionKey = sFunctionKey;
	}

	public String getStatus() {
		return m_sStatus;
	}

	public void setStatus(String sStatus) {
		this.m_sStatus = sStatus;
	}
}