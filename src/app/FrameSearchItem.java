package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import commonui.FormSelectionBox;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import om.InfVendor;
import om.MenuItem;
import om.MenuItemCategory;
import om.MenuItemCategoryList;
import om.MenuItemDept;
import om.MenuItemDeptList;
import om.PosInterfaceConfig;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FrameSearchItemListener {
	void frameSearchItem_exitClicked();
	void frameSearchItem_addItem(int iItemId);
	void frameSearchItem_askQty(int iItemId, String sItemName, String sCode);
	void frameSearchItem_searchItemStock(int iItemId, String sItemName, String sCode);
	String frameSearchItem_searchByPanel(int iSearchType);
}

public class FrameSearchItem extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;
//KingsleyKwan20171018SearchItemByJack		-----Start-----
	private VirtualUIFrame m_oFrameCover;
	private FrameTitleHeader m_oFrameTitleHeader;
	private FrameCommonBasket m_oFrameItemList;
//KingsleyKwan20171018SearchItemByJack		----- End -----
	private VirtualUITextbox m_oTextboxSearchValue;
	private VirtualUIButton m_oButtonSearchByName;
	private VirtualUIButton m_oButtonSearchByCategory;
	private VirtualUIButton m_oButtonSearchByDepartment;
	private VirtualUIButton m_oButtonShowAllResult;
	private VirtualUIButton m_oButtonSearchByPanel;
	
	private VirtualUIFrame m_oFramePage;
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIImage m_oImgButtonNextPage;

	private ArrayList<Integer> m_oItemIdList;
	private ArrayList<MenuItem> m_oCurrentPageItemList;

	private ArrayList<String[]> m_oDepartmentOptionList;
	private ArrayList<String[]> m_oCategoryOptionList;
	
	private ArrayList<MenuItemDept> m_oDepartmentList;
	private ArrayList<MenuItemCategory> m_oCategoryList;
	private HashMap<String, Integer> m_oColumnWidths;
	private int m_iRowHeight;
	
	private int m_iDepartmentId;
	private int m_iCategoryId;
	private String m_sLastSearchString = "";
	private String m_sLastLastSearchString = "";
	private boolean m_bSearched = false;
	private boolean m_bFromFloorPlan = false;
	
	private int m_iCurrentPageStartNo;
//KingsleyKwan20171018SearchItemByJack		-----Start-----
	private int m_iListItemNumber = 9;
//KingsleyKwan20171018SearchItemByJack		----- End -----
	
	private int m_iSearchType;
	public static int SEARCH_TYPE_NORMAL		= 1;
	public static int SEARCH_TYPE_SKU			= 2;
	public static int SEARCH_TYPE_ADD_WASTAGE	= 3;
	public static int SEARCH_TYPE_WASTAGE_STOCK	= 4;
	
	private int m_iNumOfColumn;
	public static int SEARCH_ITEM_LIST_THREE_COLUMN		= 3;
	public static int SEARCH_ITEM_LIST_FOUR_COLUMN		= 4;
	
	private HashMap<Integer, String[]> m_oDefaultItemPoolForSearchList;
	private PosInterfaceConfig m_oPosInterfaceConfig = new PosInterfaceConfig();
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameSearchItemListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameSearchItemListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameSearchItemListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
	public FrameSearchItem(String sTemplateFile, VirtualUIFrame oFrameCover, int iNumOfColumn){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameSearchItemListener>();
		m_oItemIdList = new ArrayList<Integer>();
		m_oCurrentPageItemList = new ArrayList<MenuItem>();
		m_oDepartmentOptionList = new ArrayList<String[]>();
		m_oCategoryOptionList = new ArrayList<String[]>();
		
		m_oDepartmentList = new ArrayList<MenuItemDept>();
		m_oCategoryList = new ArrayList<MenuItemCategory>();
		m_oColumnWidths = new HashMap<String, Integer>();
		
		m_iDepartmentId = -1;
		m_iCategoryId = -1;
		m_sLastSearchString = "";
		m_sLastLastSearchString = "";
		m_bSearched = false;
		
		m_iSearchType = SEARCH_TYPE_NORMAL;
		m_iNumOfColumn = iNumOfColumn;
		
		m_iCurrentPageStartNo = 0;
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_iListItemNumber = 9;
		
		m_oDefaultItemPoolForSearchList = null;
		
		// Set cover frame
		m_oFrameCover = oFrameCover;
//KingsleyKwan20171018SearchItemByJack		-----Start-----
		// Load form from template file
		m_oTemplateBuilder.loadTemplate(sTemplateFile);

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(true);
		m_oFrameTitleHeader.addListener(this);
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("search_item", ""));
		this.attachChild(m_oFrameTitleHeader);
		
		// Search value box
		m_oTextboxSearchValue = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxSearchValue, "txtboxSearchValue");
		this.attachChild(m_oTextboxSearchValue);
		
		// Search button
		m_oButtonSearchByName = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSearchByName, "btnSearchByName");
		m_oButtonSearchByName.setValue(AppGlobal.g_oLang.get()._("search", ""));
		m_oButtonSearchByName.setVisible(true);
		m_oButtonSearchByName.addClickServerRequestSubmitElement(m_oTextboxSearchValue);
		this.attachChild(m_oButtonSearchByName);
		
		// Department button
		m_oButtonSearchByDepartment = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSearchByDepartment, "btnSearchByDepartment");
		m_oButtonSearchByDepartment.setVisible(true);
		// Further development
		this.attachChild(m_oButtonSearchByDepartment);
		
		// Category button
		m_oButtonSearchByCategory = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSearchByCategory, "btnSearchByCategory");
		m_oButtonSearchByCategory.setVisible(true);
		this.attachChild(m_oButtonSearchByCategory);
		
		// Show all result button
		m_oButtonShowAllResult = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonShowAllResult, "btnShowAllResult");
		m_oButtonShowAllResult.setValue(AppGlobal.g_oLang.get()._("show_all", ""));
		m_oButtonShowAllResult.setVisible(true);
		this.attachChild(m_oButtonShowAllResult);
		
		
		// Search by panel button
		m_oButtonSearchByPanel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSearchByPanel, "btnSearchByPanel");
		m_oButtonSearchByPanel.setValue(AppGlobal.g_oLang.get()._("search_by_panel", ""));
		m_oButtonSearchByPanel.setVisible(false);
		this.attachChild(m_oButtonSearchByPanel);
		
		// Result list
		m_oFrameItemList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFrameItemList, "fraItemList");
		m_oFrameItemList.init();
		m_oFrameItemList.addListener(this);
		this.attachChild(m_oFrameItemList);
//KingsleyKwan20171018SearchItemByJack		-----Start-----
		// Set column widths
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oColumnWidths.put("code", 85);
			m_oColumnWidths.put("name", 150);
			m_oColumnWidths.put("price", 95);
			if (iNumOfColumn == FrameSearchItem.SEARCH_ITEM_LIST_FOUR_COLUMN)
				m_oColumnWidths.put("action", 140);
			m_iRowHeight = 50;
		}else {
			m_oColumnWidths.put("code", 150);
			m_oColumnWidths.put("name", 320);
			m_oColumnWidths.put("price", 150);
			if (iNumOfColumn == FrameSearchItem.SEARCH_ITEM_LIST_FOUR_COLUMN)
				m_oColumnWidths.put("action", 160);
			m_iRowHeight = 0;
		}
//KingsleyKwan20171018SearchItemByJack		----- End -----
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
    	ArrayList<String[]> sFieldValues = new ArrayList<String[]>();
    	
    	iFieldWidths.add(m_oColumnWidths.get("code").intValue());
    	sFieldValues.add(AppGlobal.g_oLang.get()._("item_code", ""));
    	iFieldWidths.add(m_oColumnWidths.get("name").intValue());
    	sFieldValues.add(AppGlobal.g_oLang.get()._("item_name", ""));
    	iFieldWidths.add(m_oColumnWidths.get("price").intValue());
    	sFieldValues.add(AppGlobal.g_oLang.get()._("price", ""));
    	if (iNumOfColumn == FrameSearchItem.SEARCH_ITEM_LIST_FOUR_COLUMN) {
    		iFieldWidths.add(m_oColumnWidths.get("action").intValue());
    		sFieldValues.add(AppGlobal.g_oLang.get()._("action", ""));
    	}
    	
    	m_oFrameItemList.addHeader(iFieldWidths, sFieldValues);
    	m_oFrameItemList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
//KingsleyKwan20171018SearchItemByJack		-----Start-----
    	m_oFrameItemList.setHeaderFormat(40, 20, "10,0,0,0");
		m_oFrameItemList.setBottomUnderlineVisible(false);
//KingsleyKwan20171018SearchItemByJack		----- End -----

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
		
		VirtualUIImage oImage = new VirtualUIImage();
		oImage.setWidth(m_oFramePage.getWidth());
		oImage.setHeight(m_oFramePage.getHeight());
		m_oFramePage.attachChild(oImage);
		
		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oLblPage.setWidth(m_oFramePage.getWidth());
		m_oLblPage.setHeight(m_oFramePage.getHeight());
		m_oFramePage.attachChild(m_oLblPage);
		this.attachChild(m_oFramePage);
		
		this.updateButtonColor();
		
		addFinishShowTimer();
		addEveryPeriodShowTimer();
	}
	
	public void getMenuItemInfo(int iStartNo) {
		clearItemList();
		ArrayList<Integer> oMenuItemIds = new ArrayList<Integer>();
		for(int i = iStartNo; i < m_oItemIdList.size() && i < iStartNo+m_iListItemNumber; i++) {
			int iItemId = m_oItemIdList.get(i);
			oMenuItemIds.add(iItemId);
		}
		
		m_oCurrentPageItemList.clear();

		ArrayList<MenuItem> oMenuItemList = AppGlobal.g_oFuncMenu.get().getMenuItemsByIds(oMenuItemIds, false);
		for(int i = 0; i < oMenuItemList.size(); i++) {
			MenuItem oMenuItem = oMenuItemList.get(i);
			m_oCurrentPageItemList.add(oMenuItem);
			String sName = oMenuItem.getName(AppGlobal.g_oCurrentLangIndex.get());
			if (m_oDefaultItemPoolForSearchList != null && m_oDefaultItemPoolForSearchList.containsKey(oMenuItem.getItemId())) {
				sName = m_oDefaultItemPoolForSearchList.get(oMenuItem.getItemId())[AppGlobal.g_oCurrentLangIndex.get()-1];
			}
			addItem(0, i, oMenuItem.getCode(), sName, oMenuItem.getBasicPriceByPriceLevel(AppGlobal.g_oFuncOutlet.get().getPriceLevel()));
		}
	}

	public void addItem(int iSectionId, int iItemIndex, String sItemCode, String sItemName, BigDecimal oItemPrice) {
    	ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
    	ArrayList<String> sFieldValues = new ArrayList<String>();
    	ArrayList<String> sFieldAligns = new ArrayList<String>();
    	ArrayList<String> sFieldTypes = new ArrayList<String>();
//KingsleyKwan20171018SearchItemByJack		-----Start-----
		iFieldWidths.add(m_oColumnWidths.get("code").intValue());
		sFieldValues.add(sItemCode);
		sFieldAligns.add("");
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		iFieldWidths.add(m_oColumnWidths.get("name").intValue());
		sFieldValues.add(sItemName);
		sFieldAligns.add("");
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		iFieldWidths.add(m_oColumnWidths.get("price").intValue());
		BigDecimal oTmpPrice = BigDecimal.ZERO;
		if(oItemPrice != null)
			oTmpPrice = oItemPrice;
		String sPrice = StringLib.BigDecimalToString(oTmpPrice, AppGlobal.g_oFuncOutlet.get().getItemRoundDecimal());
		sFieldValues.add(sPrice);
		sFieldAligns.add("");
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		if (m_iNumOfColumn == FrameSearchItem.SEARCH_ITEM_LIST_FOUR_COLUMN) {
			iFieldWidths.add((m_oColumnWidths.get("action").intValue()/2));
			sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_view.png");
			sFieldAligns.add("");
			sFieldTypes.add(HeroActionProtocol.View.Type.IMAGE);
			iFieldWidths.add((m_oColumnWidths.get("action").intValue()/2));
			// If the function is clicked from floor plan, hide ordering button
			if(!m_bFromFloorPlan) {
				sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_add.png");
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.IMAGE);
			}
		}
		m_oFrameItemList.addItem(iSectionId, iItemIndex, m_iRowHeight, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
		m_oFrameItemList.moveScrollToItem(iSectionId, iItemIndex);
		for (int iIndex = 0; iIndex < 4; iIndex++)
			m_oFrameItemList.setFieldTextSize(iSectionId, iItemIndex, iIndex, 16);
	}
//KingsleyKwan20171018SearchItemByJack		----- End -----
	public void clearItemList() {
		m_oFrameItemList.removeAllItems(0);
	}
	
	public void initDepartmentList(MenuItemDeptList oItemDeptList) {
		m_oDepartmentOptionList.add( AppGlobal.g_oLang.get()._("all_departments", ""));
		MenuItemDept oMenuItemDept = new MenuItemDept();
		oMenuItemDept.setIdepId(-1);
		for(int i = 1; i <=5; i++)
			oMenuItemDept.setName(i, AppGlobal.g_oLang.get()._("all_departments", "")[i-1]);
		m_oDepartmentList.add(oMenuItemDept);
		for(MenuItemDept oItemDept: oItemDeptList.getItemDeptList()) {
			m_oDepartmentOptionList.add(StringLib.appendStringArray("--", oItemDept.getName()));
			m_oDepartmentList.add(oItemDept);
			
			getSubMenuItemDeptList(oItemDept, "--");
		}
		m_oDepartmentOptionList.add(StringLib.appendStringArray("<<<", AppGlobal.g_oLang.get()._("not_defined", ""), ">>>"));
		oMenuItemDept = new MenuItemDept();
		oMenuItemDept.setIdepId(0);
		for(int i = 1; i <=5; i++) {
			oMenuItemDept.setName(i, "<<<"+AppGlobal.g_oLang.get()._("not_defined", "")[i-1]+">>>");
		}
		m_oDepartmentList.add(oMenuItemDept);
	}
	
	public void initCategoryList(MenuItemCategoryList oItemCategoryList) {
		m_oCategoryOptionList.add(AppGlobal.g_oLang.get()._("all_categories", ""));
		MenuItemCategory oMenuItemCategory = new MenuItemCategory();
		oMenuItemCategory.setIcatId(-1);
		for(int i = 1; i <=5; i++)
			oMenuItemCategory.setName(i, AppGlobal.g_oLang.get()._("all_categories", "")[i-1]);
		m_oCategoryList.add(oMenuItemCategory);
		for(MenuItemCategory oItemCategory: oItemCategoryList.getItemCategoryList()) {
			m_oCategoryOptionList.add(StringLib.appendStringArray("--", oItemCategory.getName()));
			m_oCategoryList.add(oItemCategory);
			
			getSubMenuItemCategoryList(oItemCategory, "--");
		}
		m_oCategoryOptionList.add(StringLib.appendStringArray("<<<", AppGlobal.g_oLang.get()._("not_defined", ""), ">>>"));
		oMenuItemCategory = new MenuItemCategory();
		oMenuItemCategory.setIcatId(0);
		for(int i = 1; i <=5; i++) {
			oMenuItemCategory.setName(i, "<<<"+AppGlobal.g_oLang.get()._("not_defined", "")[i-1]+">>>");
		}
		m_oCategoryList.add(oMenuItemCategory);
	}
	
	public void openDepartmentSelectionBox() {
		FormSelectionBox oFormSelectionBox = new FormSelectionBox(this.getParentForm());
		oFormSelectionBox.setOptionTextAlign(HeroActionProtocol.View.Attribute.TextAlign.LEFT+", "+HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		ArrayList<String> sOptionList = new ArrayList<>();
		for (String[] sOptions:m_oDepartmentOptionList) {
			sOptionList.add(sOptions[AppGlobal.g_oCurrentLangIndex.get()-1]);
		}
		oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("please_select_item_department"), sOptionList, false);
		oFormSelectionBox.show();
		if (oFormSelectionBox.isUserCancel()) {
			
		} else {
			ArrayList<Integer> oSelectionBoxResult = oFormSelectionBox.getResultList();
			MenuItemDept oItemDept = m_oDepartmentList.get(oSelectionBoxResult.get(0));
			String[] sItemDeptName = m_oDepartmentOptionList.get(oSelectionBoxResult.get(0));
			m_oButtonSearchByDepartment.setValue(sItemDeptName);
			this.m_iDepartmentId = oItemDept.getIdepId();
			
			startFinishShowTimer();
		}
	}
	
	public void openCategorySelectionBox() {
		FormSelectionBox oFormSelectionBox = new FormSelectionBox(this.getParentForm());
		oFormSelectionBox.setOptionTextAlign(HeroActionProtocol.View.Attribute.TextAlign.LEFT+", "+HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		ArrayList<String> sOptionList = new ArrayList<>();
		for (String[] sOptions:m_oCategoryOptionList) {
			sOptionList.add(sOptions[AppGlobal.g_oCurrentLangIndex.get()-1]);
		}
		oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("please_select_item_category"), sOptionList, false);
		oFormSelectionBox.show();
		if (oFormSelectionBox.isUserCancel()) {
			
		} else {
			ArrayList<Integer> oSelectionBoxResult = oFormSelectionBox.getResultList();
			MenuItemCategory oItemCategory = m_oCategoryList.get(oSelectionBoxResult.get(0));
			String[] sItemCategoryName = m_oCategoryOptionList.get(oSelectionBoxResult.get(0));
			m_oButtonSearchByCategory.setValue(sItemCategoryName);
			this.m_iCategoryId = oItemCategory.getIcatId();

			startFinishShowTimer();
		}
	}
	
	
	public int getInterfaceId(){
		return m_oPosInterfaceConfig.getInterfaceId();
	}
	
	public void getSubMenuItemDeptList(MenuItemDept oItemDept, String sPrefix) {
		if(!oItemDept.getChildDeptList().isEmpty()) {
			for(MenuItemDept oSubDept: oItemDept.getChildDeptList()) {
				m_oDepartmentOptionList.add(StringLib.appendStringArray(sPrefix, "--", oSubDept.getName()));
				m_oDepartmentList.add(oSubDept);
				
				getSubMenuItemDeptList(oSubDept, sPrefix+"--");
			}
		}
	}
	
	public void getSubMenuItemCategoryList(MenuItemCategory oItemCategory, String sPrefix) {
		if(!oItemCategory.getChildCategoryList().isEmpty()) {
			for(MenuItemCategory oSubCategory: oItemCategory.getChildCategoryList()) {
				m_oCategoryOptionList.add(StringLib.appendStringArray(sPrefix, "--", oSubCategory.getName()));
				m_oCategoryList.add(oSubCategory);
				
				getSubMenuItemCategoryList(oSubCategory, sPrefix+"--");
			}
		}
	}
	
	public void searchItem() {
		String sSearchText = m_oTextboxSearchValue.getValue().trim();
		
		m_oItemIdList.clear();
		if (m_oDefaultItemPoolForSearchList != null) {
			// Have default search pool
			if (m_oDefaultItemPoolForSearchList.size() > 0) {
				ArrayList<Integer> oMenuItemIds = new ArrayList<Integer>(m_oDefaultItemPoolForSearchList.keySet());
				ArrayList<MenuItem> oMenuItemList = AppGlobal.g_oFuncMenu.get().getMenuItemsByIds(oMenuItemIds, false);
				for(MenuItem oMenuItem:oMenuItemList) {
					if (sSearchText.length() > 0 || m_iDepartmentId >= 0 || m_iCategoryId >= 0) {
						// Filter item name
						if (m_iDepartmentId >= 0 && oMenuItem.getDeparmentId() != m_iDepartmentId)
							// Department not match
							continue;
						
						if (m_iCategoryId >= 0 && oMenuItem.getCategoryId() != m_iCategoryId)
							// Category not match
							continue;
						
						if (sSearchText.length() > 0) {
							boolean bFound = false;
							for (int i=0; i<AppGlobal.LANGUAGE_COUNT; i++) {
								if (m_oDefaultItemPoolForSearchList.get(oMenuItem.getItemId())[i].toUpperCase()
										.contains(sSearchText.toUpperCase())) {
									bFound = true;
									break;
								}
							}
							if (!bFound)
								// Name not match
								continue;
						}
					}
					m_oItemIdList.add(oMenuItem.getItemId());
				}
				
				m_iCurrentPageStartNo = 0;
				updateItemList();
			} else {
				m_oItemIdList.clear();
				clearItemList();
				m_iCurrentPageStartNo = 0;
				updatePage();
			}
		} else {
			MenuItem oMenuItem = new MenuItem();
			JSONArray oItemArray = null;
			
			if (m_iSearchType != SEARCH_TYPE_SKU){
				if(m_iSearchType == FrameSearchItem.SEARCH_TYPE_WASTAGE_STOCK || m_iSearchType == FrameSearchItem.SEARCH_TYPE_ADD_WASTAGE)
					oItemArray = oMenuItem.readByPLUNameDepartmentCategory(sSearchText, m_iDepartmentId, m_iCategoryId,	48, true);
				else
					oItemArray = oMenuItem.readByPLUNameDepartmentCategory(sSearchText, m_iDepartmentId, m_iCategoryId,	48, false);
			}
			else
				oItemArray = oMenuItem.readByItemSKU(sSearchText, m_iDepartmentId, m_iCategoryId, 48);
			
			int iItemId = 0;
			if(oItemArray != null) {
				for(int i = 0; i < oItemArray.length(); i++) {
					JSONObject oItemJSONObject = oItemArray.optJSONObject(i);
					if(oItemJSONObject != null) {	
						if(iItemId != oItemJSONObject.optInt("item_id")){
							iItemId = oItemJSONObject.optInt("item_id");
							m_oItemIdList.add(Integer.valueOf(iItemId));
						}
					}
				}			
				m_iCurrentPageStartNo = 0;
				updateItemList();
			} else {				
				m_oItemIdList.clear();
				clearItemList();
				m_iCurrentPageStartNo = 0;
				updatePage();
			}
    	}
		
		this.updateButtonColor();
	}
	
	// Update "Search", "Show All", Department and Category button color
	public void updateButtonColor() {
		List<VirtualUIButton> oButtonList = new ArrayList<VirtualUIButton>();
		setUnselectedButtonColor(m_oButtonSearchByName);
		setUnselectedButtonColor(m_oButtonShowAllResult);
		setUnselectedButtonColor(m_oButtonSearchByCategory);
		setUnselectedButtonColor(m_oButtonSearchByDepartment);
		
		if(m_iDepartmentId != -1)
			oButtonList.add(this.m_oButtonSearchByDepartment);
		
		if(m_iCategoryId != -1)
			oButtonList.add(m_oButtonSearchByCategory);

		if(!m_oTextboxSearchValue.getValue().isEmpty())
			oButtonList.add(this.m_oButtonSearchByName);
		
		if(oButtonList.isEmpty())
			oButtonList.add(this.m_oButtonShowAllResult);
		
		for(VirtualUIButton oButton: oButtonList) 
			setSelectedButtonColor(oButton);
	}
	
	public void setSelectedButtonColor(VirtualUIButton oButton) {
		oButton.setBackgroundColor("#0055B8");
		oButton.setForegroundColor("#FFFFFF");
		oButton.setStrokeColor("#005080");
	}
	
	public void setUnselectedButtonColor(VirtualUIButton oButton) {
		oButton.setBackgroundColor("#FFFFFF");
		oButton.setForegroundColor("#000000");
		oButton.setStrokeColor("#868686");
	}
	
	public void updateItemList() {
		getMenuItemInfo(m_iCurrentPageStartNo);
		updatePage();
	}
	
	public void updatePage() {
		boolean bShowPageUp = false;
		boolean bShowPageDown = false;
		int iPage = 0;
		int iCurrentPanelRecordCount = m_oItemIdList.size();
		
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
	
	// if oDefaultItemPoolForSearchList is not null, use default pool for search
	public void initSearch(HashMap<Integer, String[]> oDefaultItemPoolForSearchList, int iSearchType, PosInterfaceConfig oInventoryInterfaceConfig, boolean bFromFloorPlan) {
		m_bFromFloorPlan = bFromFloorPlan;
		m_oFrameCover.setVisible(true);
		m_oFrameCover.bringToTop();
		this.setVisible(true);
		this.bringToTop();
		
		m_oButtonSearchByCategory.setVisible(true);
		m_oButtonSearchByDepartment.setVisible(true);
		m_oButtonShowAllResult.setVisible(true);
		m_oButtonSearchByPanel.setVisible(false);
		
		//reset the button color
		setSelectedButtonColor(m_oButtonShowAllResult);
		setUnselectedButtonColor(m_oButtonSearchByName);
		setUnselectedButtonColor(m_oButtonSearchByCategory);
		setUnselectedButtonColor(m_oButtonSearchByDepartment);
		
		m_iCurrentPageStartNo = 0;
		m_iDepartmentId = -1;
		m_iCategoryId = -1;
		m_oTextboxSearchValue.setValue("");
		m_sLastSearchString = "";
		m_sLastLastSearchString = "";
		m_oPosInterfaceConfig = null;
		m_iSearchType = iSearchType;
		if(iSearchType == FrameSearchItem.SEARCH_TYPE_NORMAL || iSearchType == FrameSearchItem.SEARCH_TYPE_SKU){
			if(iSearchType == FrameSearchItem.SEARCH_TYPE_NORMAL)
				m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("search_item", ""));
			else if(iSearchType == FrameSearchItem.SEARCH_TYPE_SKU)
				m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("search_item_by_sku", ""));
			
			MenuItemDept oItemDept = m_oDepartmentList.get(0);
			m_oButtonSearchByDepartment.setValue(oItemDept.getName(AppGlobal.g_oCurrentLangIndex.get()));
			
			MenuItemCategory oItemCategory = m_oCategoryList.get(0);
			m_oButtonSearchByCategory.setValue(oItemCategory.getName(AppGlobal.g_oCurrentLangIndex.get()));
			
			m_oDefaultItemPoolForSearchList = oDefaultItemPoolForSearchList;
			
			if (m_oDefaultItemPoolForSearchList != null) {
				searchItem();
				m_oButtonShowAllResult.setValue(AppGlobal.g_oLang.get()._("show_all", ""));
			} else {
				m_oButtonShowAllResult.setValue(AppGlobal.g_oLang.get()._("clear", ""));
				
				m_oItemIdList.clear();
				clearItemList();
				m_iCurrentPageStartNo = 0;
				updatePage();
			}
		} else {
			m_oPosInterfaceConfig = oInventoryInterfaceConfig;
			
			// Search item by interface
			if(oInventoryInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_COOKING_THE_BOOK))
				this.initCookingTheBookSearch();
		}
	}

	private void initCookingTheBookSearch(){
		
		m_oButtonSearchByCategory.setVisible(false);
		m_oButtonSearchByDepartment.setVisible(false);
		m_oButtonSearchByPanel.setVisible(true);
		m_oButtonSearchByPanel.setValue(AppGlobal.g_oLang.get()._("search_by_panel", ""));
		m_oButtonShowAllResult.setVisible(false);
		
		setSelectedButtonColor(m_oButtonSearchByName);
		setUnselectedButtonColor(m_oButtonSearchByPanel);
		
		if(m_iSearchType == SEARCH_TYPE_ADD_WASTAGE)
			m_oFrameTitleHeader.setValue(AppGlobal.g_oLang.get()._("wastage_operation", ""));
		else if (m_iSearchType == SEARCH_TYPE_WASTAGE_STOCK)
			m_oFrameTitleHeader.setValue(AppGlobal.g_oLang.get()._("search_item_stock", ""));
		m_oItemIdList.clear();
		clearItemList();
		
		m_iCurrentPageStartNo = 0;
		updatePage();
		
	}
	
	public void setPageNumber(int iNumber) {
		if(iNumber > 0) {
			int iTotalPage = (int)Math.ceil(1.0*m_oItemIdList.size()/m_iListItemNumber);
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
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
		else
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
		m_oImgButtonPrevPage.setEnabled(bShow);
	}
	
	public void showPageDown(boolean bShow) {
		if(bShow)
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
		else
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
		m_oImgButtonNextPage.setEnabled(bShow);
}	
//KingsleyKwan20171018SearchItemByJack		----- End -----
    // Create update basket timer
	private void addFinishShowTimer(){
		this.addTimer("search_item", 0, false, "", false, false, m_oTextboxSearchValue);
	}
	
	// Create update basket timer in every time period
	private void addEveryPeriodShowTimer(){
		this.addTimer("search_item_every_period", 300, true, "everyPeriod", false, false, m_oTextboxSearchValue);
	}
	
	// Start to update basket in every time period
	public void startEveryPeriodShowTimer(){
		this.controlTimer("search_item_every_period", true);
		MenuItemDept oItemDept = m_oDepartmentList.get(0);
		m_oButtonSearchByDepartment.setValue(oItemDept.getName(AppGlobal.g_oCurrentLangIndex.get()));
	
		MenuItemCategory oItemCategory = m_oCategoryList.get(0);
		m_oButtonSearchByCategory.setValue(oItemCategory.getName(AppGlobal.g_oCurrentLangIndex.get()));
	}
	
	// Stop to update basket in every time period
	public void stopEveryPeriodShowTimer(){
		this.controlTimer("search_item_every_period", false);
	}
	
	// Start to update basket
	public void startFinishShowTimer(){
		this.controlTimer("search_item", true);
	}
	
	
	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if(iId == super.getIDForPosting().hashCode()) {
			// Ask drawing basket
			//Set the last client socket ID
			AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
			
			if(sNote.equals("everyPeriod")) {
				if((m_oTextboxSearchValue.getValue().trim().equals(m_sLastSearchString)) && (m_sLastSearchString.equals(m_sLastLastSearchString))){
					if(!m_bSearched) {
						if (!m_oTextboxSearchValue.getValue().trim().isEmpty())
							this.searchItem();
						else {
							m_oItemIdList.clear();
							clearItemList();
							m_iCurrentPageStartNo = 0;
							updatePage();
						}
						m_bSearched = true;
					}
				}else
					m_bSearched = false;
				m_sLastLastSearchString = m_sLastSearchString;
				m_sLastSearchString = m_oTextboxSearchValue.getValue().trim();
				
			}else
				this.searchItem();
			
			// Send the UI packet to client and the thread is finished
			super.getParentForm().finishUI(true);
	
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		// Find the clicked button
		if (m_oImgButtonNextPage.getId() == iChildId) {
			if(m_iCurrentPageStartNo+m_iListItemNumber < m_oItemIdList.size()) {
				m_iCurrentPageStartNo += m_iListItemNumber;
				updateItemList();
			}
			
			bMatchChild = true;
		} else if (m_oImgButtonPrevPage.getId() == iChildId) {
			 // PAGE UP
			if(m_iCurrentPageStartNo-m_iListItemNumber >= 0) {
				m_iCurrentPageStartNo -= m_iListItemNumber;
				updateItemList();
			}
			
			bMatchChild = true;
		} else if (m_oButtonSearchByDepartment.getId() == iChildId) {
			openDepartmentSelectionBox();
			bMatchChild = true;
		} else if (m_oButtonSearchByCategory.getId() == iChildId) {
			openCategorySelectionBox();
			bMatchChild = true;
		} else if (m_oButtonSearchByName.getId() == iChildId) {
			searchItem();
			bMatchChild = true;
		} else if (m_oButtonShowAllResult.getId() == iChildId) {
			initSearch(m_oDefaultItemPoolForSearchList, this.m_iSearchType, null, this.m_bFromFloorPlan);
			bMatchChild = true;
		} else if (m_oButtonSearchByPanel.getId() == iChildId) {
			m_oTextboxSearchValue.setValue("");
			this.clearItemList();
			for(FrameSearchItemListener listener: listeners)
				listener.frameSearchItem_searchByPanel(m_iSearchType);
			bMatchChild = true;
		}
		
		return bMatchChild;
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
	}
	
	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		AppGlobal.g_oTerm.get().hideKeyboard();
		if(m_oPosInterfaceConfig == null){
			if (iFieldIndex == 3) {
				FuncCheckItem oFuncCheckItem = new FuncCheckItem();
				if(oFuncCheckItem.retieveItemFromMenu(this.m_oItemIdList.get(m_iCurrentPageStartNo+iItemIndex).intValue(), BigDecimal.ZERO, new BigDecimal("1.0"), null, false, false, AppGlobal.g_oFuncOutlet.get().getPriceLevel()) == false)
					return;
				
				m_oFrameCover.bringToTop();
				this.bringToTop();
				
				FormItemDetail oFormItemDetail = new FormItemDetail(this.getParentForm());
				oFormItemDetail.initWithFuncCheckItem(oFuncCheckItem, AppGlobal.g_oFuncOutlet.get().getPriceLevel(), true, AppGlobal.g_oFuncOutlet.get().getItemRoundDecimal(), null);
				oFormItemDetail.setOrderButtonVisibility(!m_bFromFloorPlan);
				oFormItemDetail.show();
				
				if(oFormItemDetail.isOrderItem()) {
					for(FrameSearchItemListener listener: listeners) {
						listener.frameSearchItem_addItem(oFuncCheckItem.getMenuItem().getItemId());
					}
				}
				m_oFrameCover.setVisible(true);
			} else if(iFieldIndex == 4) {
				for(FrameSearchItemListener listener: listeners) {
					MenuItem oMenuItem = m_oCurrentPageItemList.get(iItemIndex);
					listener.frameSearchItem_addItem(oMenuItem.getItemId());
				}	
				
				this.m_oTextboxSearchValue.setValue("");
			}
		} else {
			if(m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_COOKING_THE_BOOK)){
				MenuItem oMenuItem = m_oCurrentPageItemList.get(iItemIndex);
				if(m_iSearchType == SEARCH_TYPE_ADD_WASTAGE){
					for(FrameSearchItemListener listener: listeners)
						listener.frameSearchItem_askQty(oMenuItem.getItemId(), oMenuItem.getName(AppGlobal.g_oCurrentLangIndex.get()), oMenuItem.getCode());
				} else if (m_iSearchType == SEARCH_TYPE_WASTAGE_STOCK){
					for(FrameSearchItemListener listener: listeners)
						listener.frameSearchItem_searchItemStock(oMenuItem.getItemId(), oMenuItem.getName(AppGlobal.g_oCurrentLangIndex.get()), oMenuItem.getCode());
				}
			}
		}
	}
	
	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
	}

	@Override
	public void FrameTitleHeader_close() {
		for(FrameSearchItemListener listener: listeners) {
			this.m_oFrameCover.setVisible(false);
			stopEveryPeriodShowTimer();
			AppGlobal.g_oTerm.get().hideKeyboard();
			listener.frameSearchItem_exitClicked();
		}
	}
}
