package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import commonui.FormProcessBox;
import core.Controller;

import om.MenuItem;
import om.MenuMenu;
import om.MenuMenuLookup;
import om.PosActionLog;
import om.PosOutletItem;
import om.PosOutletItemList;

import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormStockSoldout extends VirtualUIForm implements FrameStockSoldoutFunctionListener {
	private TemplateBuilder m_oTemplateBuilder;
	private FrameStockSoldoutFunction m_oFrameStockSoldoutFunction;
	private boolean m_bAskItem;
	private boolean m_bSoldoutMenuEdited;										// Indicate whether records have set soldout / canceled soldout in soldout menu
																				// if yes, reload soldout item page
	private boolean m_bSoldoutByShop;											// Indicate whether set soldout item in shop level or outlet level

	private ArrayList<MenuItem> m_oDeleteMenuItems;								// Store the item that change from soldout to sell, update the soldout label in ordering panel
	private HashMap<Integer, PosOutletItem> m_oOutletItemList;					// Outlet Item list
	private List<MenuItem> m_oMenuItemList;										// Menu Item list
	private String m_sCurrentSearchKeyWord;										// Store search text
	private List<Integer> m_oSelectedItemIndexs;								// Store selected Item for remove soldout

	private List<MenuMenuLookup> m_oSoldoutMenuMenuLookupList;					// Store items or sub-menus under soldout menu
	private String m_sCurrentSearchMenuKeyword;									// Store search menu text
	private List<Integer> m_oSelectedMenuIndexs;								// Store selected Menu for set soldout / cancel soldout
	
	public FormStockSoldout(Controller oParentController, boolean bSoldoutByShop){
		super(oParentController);
		
		m_bAskItem = false;
		m_bSoldoutMenuEdited = false;
		m_bSoldoutByShop = bSoldoutByShop;
		m_oDeleteMenuItems = new ArrayList<MenuItem>();
		
		m_oOutletItemList = new HashMap<Integer, PosOutletItem>();
		m_oMenuItemList = new ArrayList<MenuItem>();
		m_sCurrentSearchKeyWord = "";
		m_oSelectedItemIndexs = new ArrayList<Integer>();
		
		m_oSoldoutMenuMenuLookupList = new ArrayList<MenuMenuLookup>();
		m_oSelectedMenuIndexs = new ArrayList<Integer>();
		m_sCurrentSearchMenuKeyword = "";
	}
	
	public boolean init(){		
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmStockSoldout.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameStockSoldoutFunction = new FrameStockSoldoutFunction(m_bSoldoutByShop);
		m_oTemplateBuilder.buildFrame(m_oFrameStockSoldoutFunction, "fraStockSoldout");
		m_oFrameStockSoldoutFunction.addListener(this);
		this.attachChild(m_oFrameStockSoldoutFunction);
		
		// Load stock
		this.loadSoldoutItemRecord();
		
		return true;
	}
	
	public void setSoldoutMenuLookupList(List<MenuMenuLookup> oMenuMenuLookupList) {
		m_oSoldoutMenuMenuLookupList = oMenuMenuLookupList;
		this.loadSoldoutMenuRecord();
	}

	private void loadSoldoutItemRecord(){
		// Create processing box
		FormProcessBox oFormProcessBox = new FormProcessBox(this);			
		oFormProcessBox.setMessage(AppGlobal.g_oLang.get()._("loading")+"...");
		oFormProcessBox.showWithoutRemoveUI();
		
		ArrayList<Integer> oMenuItemIds = new ArrayList<Integer>();
		// Get the item that with stock
		PosOutletItemList oItemStockList = new PosOutletItemList();
		if(m_bSoldoutByShop)
			oItemStockList.readShopOutletItemListBySoldout(AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), PosOutletItem.SOLDOUT_YES);
		else
			oItemStockList.readOutletItemListBySoldout(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosOutletItem.SOLDOUT_YES);
		for(PosOutletItem oOutletItem: oItemStockList.getOutletItemList()) {
			oMenuItemIds.add(oOutletItem.getItemId());
			m_oOutletItemList.put(oOutletItem.getItemId(), oOutletItem);
		}

		// Load item name
		if(!oMenuItemIds.isEmpty()){
			FuncMenu oFuncMenu = new FuncMenu();
			ArrayList<MenuItem> oMenuItemList = oFuncMenu.getMenuItemsByIds(oMenuItemIds, true);
			
			for(MenuItem oMenuItem:oMenuItemList){
				m_oMenuItemList.add(oMenuItem);
			}
			
			// Add the item to the menu item list
			for(int i = 0; i < m_oMenuItemList.size(); i++){
				 MenuItem oMenuItem = m_oMenuItemList.get(i);
				m_oFrameStockSoldoutFunction.addItemRecord(0, i, oMenuItem.getName(AppGlobal.g_oCurrentLangIndex.get()));
			}
		}

		// Close processing box
		oFormProcessBox.closeShowWithoutRemoveUI();
	}
	
	private void reloadSoldoutItemRecord() {
		m_oOutletItemList.clear();
		m_oMenuItemList.clear();
		m_sCurrentSearchKeyWord = "";
		m_oFrameStockSoldoutFunction.clearSearchValue();
		m_oFrameStockSoldoutFunction.clearAllItemRecords();
		
		loadSoldoutItemRecord();
	}
	
	private void loadSoldoutMenuRecord() {
		int iIndex = 0;
		for(MenuMenuLookup oMenuMenuLookup: m_oSoldoutMenuMenuLookupList) {
			String sName = "";
			String sType = "";
			if(oMenuMenuLookup.isMenuItem()) {
				MenuItem oMenuItem = oMenuMenuLookup.getMenuItem();
				sName = oMenuItem.getName(AppGlobal.g_oCurrentLangIndex.get());
				sType = AppGlobal.g_oLang.get()._("menu_item");
			} else if (oMenuMenuLookup.isSubMenu()) {
				MenuMenu oMenuMenu = oMenuMenuLookup.getMenuMenu();
				sName = oMenuMenu.getName(AppGlobal.g_oCurrentLangIndex.get());
				sType = AppGlobal.g_oLang.get()._("menu_menu");
			}
			
			m_oFrameStockSoldoutFunction.addMenuRecord(0, iIndex, sName, sType, false);
			iIndex++;
		}
	}

	public boolean isAskItem(){
		return m_bAskItem;
	}

	private void searchItemResult(){		
		ArrayList<Integer> oHiddenLineIndex = new ArrayList<Integer>();
		int iIndex = 0;
		for(MenuItem oMenuItem :m_oMenuItemList){
			boolean bHideItem = false;
			
			if(m_sCurrentSearchKeyWord.length() > 0){
				boolean bWordFound = false;
				for(int i=1; i<=5; i++){
					String sName = oMenuItem.getName(i);

					// search record by case-insensative
					if(Pattern.compile(Pattern.quote(m_sCurrentSearchKeyWord), Pattern.CASE_INSENSITIVE).matcher(sName).find()) {
						// Item found
						bWordFound = true;
						break;
					}
				}
				
				if(bWordFound == false)
					bHideItem = true;
			}
			
			if(bHideItem){
				// Add to the index list
				oHiddenLineIndex.add(iIndex);
			}
			
			iIndex++;
		}
		
		if(!oHiddenLineIndex.isEmpty()){
			m_oFrameStockSoldoutFunction.setItemResultLineVisible(oHiddenLineIndex, false);
		}
	}

	private void showAllItemResult() {
		ArrayList<Integer> oAllLineIndex = new ArrayList<Integer>();
		for(int i=0; i<m_oMenuItemList.size(); i++)
			oAllLineIndex.add(i);
		m_oFrameStockSoldoutFunction.setItemResultLineVisible(oAllLineIndex, true);
	}

	private void searchMenuResult(){		
		ArrayList<Integer> oHiddenLineIndex = new ArrayList<Integer>();
		int iIndex = 0;
		for(MenuMenuLookup oMenuMenuLookup :m_oSoldoutMenuMenuLookupList){
			boolean bHideItem = false;
			
			if(m_sCurrentSearchMenuKeyword.length() > 0){
				boolean bWordFound = false;
				for(int i=1; i<=5; i++){
					String sName;
					if(oMenuMenuLookup.isMenuItem())
						sName = oMenuMenuLookup.getMenuItem().getName(i);
					else
						sName = oMenuMenuLookup.getMenuMenu().getName(i);
					
					// search record by case-insensative
					if(Pattern.compile(Pattern.quote(m_sCurrentSearchMenuKeyword), Pattern.CASE_INSENSITIVE).matcher(sName).find()) {
						// Item found
						bWordFound = true;
						break;
					}
				}
				if(bWordFound == false)
					bHideItem = true;
			}
			
			if(bHideItem){
				// Add to the index list
				oHiddenLineIndex.add(iIndex);
			}
			
			iIndex++;
		}
		
		if (!oHiddenLineIndex.isEmpty())
			m_oFrameStockSoldoutFunction.setMenuResultLineVisible(oHiddenLineIndex, false);
	}

	private void showAllMenuResult() {
		ArrayList<Integer> oAllLineIndex = new ArrayList<Integer>();
		for(int i=0; i<m_oSoldoutMenuMenuLookupList.size(); i++)
			oAllLineIndex.add(i);
		m_oFrameStockSoldoutFunction.setMenuResultLineVisible(oAllLineIndex, true);
	}
	
	private void clearSelectedItems() {
		for(int i = 0; i < m_oSoldoutMenuMenuLookupList.size(); i++) 
			m_oFrameStockSoldoutFunction.setMenuBackgroundColor(i, false);
		
		m_oSelectedMenuIndexs.clear();
	}

	public ArrayList<MenuItem> getDeleteMenuItemList() {
		return m_oDeleteMenuItems;
	}
	
	@Override
	public void frameStockSoldoutFunction_clickAddSoldoutItem() {
		m_bAskItem = true;
		
		// Finish showing this form
		this.finishShow();
	}

	@Override
	public void frameStockSoldoutFunction_clickExit() {
		// Finish showing this form
		this.finishShow();		
	}

	@Override
	public void frameStockSoldoutFunction_clickBasketMenuItem(int iSectionId, int iItemIndex) {
		if(m_oSelectedItemIndexs.contains(iItemIndex)) {
			m_oFrameStockSoldoutFunction.setItemBackgroundColor(iItemIndex, false);
			m_oSelectedItemIndexs.remove(Integer.valueOf(iItemIndex));
		} else {
			m_oFrameStockSoldoutFunction.setItemBackgroundColor(iItemIndex, true);
			m_oSelectedItemIndexs.add(iItemIndex);
		}
	}

	@Override
	public void frameStockSoldoutFunction_clickSearchByName(String sValue) {
		// Set all item's visible = true first
		this.showAllItemResult();
		
		// Store the keyword
		sValue = sValue.trim();
		if(sValue.length() > 0)
			m_sCurrentSearchKeyWord = sValue;
		
		this.searchItemResult();
	}

	@Override
	public void frameStockSoldoutFunction_clickShowAllResult() {
		// Clear all search criteria
		m_sCurrentSearchKeyWord = "";
		
		// Clear search value
		m_oFrameStockSoldoutFunction.clearSearchValue();
		
		this.showAllItemResult();		
	}

	@Override
	public void frameStockSoldoutFunction_clickSearchMenuByName(String sValue) {
		// Set all menu's visible = true first
		this.showAllMenuResult();
		
		// Store the keyword
		sValue = sValue.trim();
		if(sValue.length() > 0)
			m_sCurrentSearchMenuKeyword = sValue;
		
		this.searchMenuResult();
	}

	@Override
	public void frameStockSoldoutFunction_clickShowAllMenuResult() {
		// Clear all search criteria
		m_sCurrentSearchMenuKeyword = "";
		
		// Clear search value
		m_oFrameStockSoldoutFunction.clearSearchMenuValue();
		
		this.showAllMenuResult();	
	}

	@Override
	public void frameStockSoldoutFunction_clickAvaiableBasketMenu(
			int iSectionId, int iItemIndex, String sNote) {
		boolean bSelected;
		if(m_oSelectedMenuIndexs.contains(iItemIndex)) {
			bSelected = false;
			m_oSelectedMenuIndexs.remove(Integer.valueOf(iItemIndex));
		}else {
			bSelected = true;
			m_oSelectedMenuIndexs.add(iItemIndex);
		}
		
		m_oFrameStockSoldoutFunction.setMenuBackgroundColor(iItemIndex, bSelected);
	}

	@Override
	public void frameStockSoldoutFunction_switchTag(int iTagIndex) {
		if(iTagIndex == 0) {
			if(m_bSoldoutMenuEdited)
				this.reloadSoldoutItemRecord();
		} else {
			m_bSoldoutMenuEdited = false;
			
			// Clear selected rows
			this.clearSelectedItems();
			
			// Clear all search criteria
			m_sCurrentSearchMenuKeyword = "";
			
			// Clear search value
			m_oFrameStockSoldoutFunction.clearSearchMenuValue();
			
			// Reset button description
			m_oFrameStockSoldoutFunction.resetSelectAllButton();
			
			// Show all records
			this.showAllMenuResult();
		}
	}

	@Override
	public void frameStockSoldoutFunction_clickRemoveSoldoutItem() {
		if(m_oSelectedItemIndexs.isEmpty()) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("please_select_at_least_one_item"));
			oFormDialogBox.show();
			return;
		}

		FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("ok"), AppGlobal.g_oLang.get()._("cancel"), this);
		oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
		oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("cancel_soldout") + "?");
		oFormConfirmBox.show();
		
		if(oFormConfirmBox.isOKClicked() == false)
			return;
		
		// Update items of soldout menu in PosOutletItem from soldout to sell
		ArrayList<PosOutletItem> oPosOutletItemList = new ArrayList<PosOutletItem>();
		for(Integer iItemIndex : m_oSelectedItemIndexs) {
			MenuItem oMenuItem = m_oMenuItemList.get(iItemIndex.intValue());
			
			if(!m_oOutletItemList.containsKey(oMenuItem.getItemId())) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_find")+" "+oMenuItem.getName(AppGlobal.g_oCurrentLangIndex.get()));
				oFormDialogBox.show();
				continue;
			}
			
			PosOutletItem oPosOutletItem = m_oOutletItemList.get(oMenuItem.getItemId());
			oPosOutletItem.setSoldout(PosOutletItem.SOLDOUT_NO);
			oPosOutletItemList.add(oPosOutletItem);
			
			// if outlet item has no check stock, store it (Ordering Panel need to hide quantity label of Lookup Button in FormMain)
			if(!oPosOutletItem.isCheckStock())
				m_oDeleteMenuItems.add(oMenuItem);

			// Add log to action log list
			String sLogRemark = "ItemId:" + oPosOutletItem.getItemId() + "[" + oMenuItem.getName(AppGlobal.g_oCurrentLangIndex.get()) + "]:soldout->sell";
			if(m_bSoldoutByShop)
				AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.stock_soldout_by_shop.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", sLogRemark);
			else
				AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.stock_soldout.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", sLogRemark);

		}
		
		//handle action log
		AppGlobal.g_oActionLog.get().handleActionLog(false);
		
		if (!oPosOutletItemList.isEmpty()) {
			PosOutletItem oPosOutletItem = new PosOutletItem();
			if(m_bSoldoutByShop)
				oPosOutletItem.addUpdateWithMultipleShopRecord(AppGlobal.g_oFuncOutlet.get().getShopId(), oPosOutletItemList);
			else
				oPosOutletItem.addUpdateWithMultipleRecord(oPosOutletItemList);
		}

		// Remove item record in soldout item basket
		Collections.sort(m_oSelectedItemIndexs);
		Collections.reverse(m_oSelectedItemIndexs);
		for(int iItemIndex: m_oSelectedItemIndexs) {
			MenuItem oMenuItem = m_oMenuItemList.get(iItemIndex);
			
			// Check wether it is in PosOutletItem update list (ignore the index that cannot find related PosOutletItem)
			boolean bFound = false;
			for(PosOutletItem oPosOutletItem: oPosOutletItemList) {
				if(oPosOutletItem.getItemId() == oMenuItem.getItemId()) {
					bFound = true;
					break;
				}
			}
			if(!bFound)
				continue;
			
			m_oFrameStockSoldoutFunction.removeItemRecord(0, iItemIndex);
			
			m_oMenuItemList.remove(iItemIndex);
			m_oOutletItemList.remove(oMenuItem.getItemId());
		}
		m_oSelectedItemIndexs.clear();
	}

	@Override
	public void frameStockSoldoutFunction_selectAll(boolean bSelectAll) {
		if(bSelectAll) {
			m_oSelectedMenuIndexs.clear();
			for(int i = 0; i < m_oSoldoutMenuMenuLookupList.size(); i++) {
				m_oFrameStockSoldoutFunction.setMenuBackgroundColor(i, true);
				m_oSelectedMenuIndexs.add(i);
			}
		} else {
			// Clear selected rows
			this.clearSelectedItems();
		}
	}

	@Override
	public void frameStockSoldoutFunction_setSoldout() {
		if(m_oSelectedMenuIndexs.isEmpty()) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("please_select_at_least_one_item"));
			oFormDialogBox.show();
			return;
		}

		List<Integer> oMenuIds = new ArrayList<Integer>();
		List<Integer> oItemIds = new ArrayList<Integer>();
		for(Integer iIndex: m_oSelectedMenuIndexs) {
			MenuMenuLookup oMenuMenuLookup = m_oSoldoutMenuMenuLookupList.get(iIndex);
			if (oMenuMenuLookup.isSubMenu())
				oMenuIds.add(oMenuMenuLookup.getSubMenuId());
			else
				oItemIds.add(oMenuMenuLookup.getItemId());
		}

		PosOutletItemList oOutletItemList = new PosOutletItemList();
		if(m_bSoldoutByShop)
			oOutletItemList.saveMultipleRecordWithMenuAndItemIdsAndShopId(AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), oMenuIds, oItemIds, PosOutletItem.SOLDOUT_YES);
		else
			oOutletItemList.saveMultipleRecordWithMenuAndItemIds(AppGlobal.g_oFuncOutlet.get().getOutletId(), oMenuIds, oItemIds, PosOutletItem.SOLDOUT_YES);
		
		for(PosOutletItem oPosOutletItem: oOutletItemList.getOutletItemList()) {
			// Add log to action log list
			String sLogRemark = "ItemId:" + oPosOutletItem.getItemId() + ":sell->soldout";
			if(m_bSoldoutByShop)
				AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.stock_soldout_by_shop.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", sLogRemark);
			else
				AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.stock_soldout.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", sLogRemark);
		}
		
		//handle action log
		AppGlobal.g_oActionLog.get().handleActionLog(false);

		// Clear selected rows
		this.clearSelectedItems();
		
		// Reset button description
		m_oFrameStockSoldoutFunction.resetSelectAllButton();
		
		m_bSoldoutMenuEdited = true;
		
		// Reload the items sold-out record
		this.reloadSoldoutItemRecord();
	}

	@Override
	public void frameStockSoldoutFunction_cancelSoldout() {
		if(m_oSelectedMenuIndexs.isEmpty()) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("please_select_at_least_one_item"));
			oFormDialogBox.show();
			return;
		}

		FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("ok"), AppGlobal.g_oLang.get()._("cancel"), this);
		oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
		oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("cancel_soldout") + "?");
		oFormConfirmBox.show();
		
		if(oFormConfirmBox.isOKClicked() == false)
			return;

		List<Integer> oMenuIds = new ArrayList<Integer>();
		List<Integer> oItemIds = new ArrayList<Integer>();
		for(Integer iIndex: m_oSelectedMenuIndexs) {
			MenuMenuLookup oMenuMenuLookup = m_oSoldoutMenuMenuLookupList.get(iIndex);
			if (oMenuMenuLookup.isSubMenu())
				oMenuIds.add(oMenuMenuLookup.getSubMenuId());
			else
				oItemIds.add(oMenuMenuLookup.getItemId());
		}

		PosOutletItemList oOutletItemList = new PosOutletItemList();
		if(m_bSoldoutByShop)
			oOutletItemList.saveMultipleRecordWithMenuAndItemIdsAndShopId(AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), oMenuIds, oItemIds, PosOutletItem.SOLDOUT_NO);
		else
			oOutletItemList.saveMultipleRecordWithMenuAndItemIds(AppGlobal.g_oFuncOutlet.get().getOutletId(), oMenuIds, oItemIds, PosOutletItem.SOLDOUT_NO);
		
		ArrayList<Integer> oMenuItemIds = new ArrayList<Integer>();
		for(PosOutletItem oOutletItem: oOutletItemList.getOutletItemList())
			oMenuItemIds.add(oOutletItem.getItemId());
		
		FuncMenu oFuncMenu = new FuncMenu();
		HashMap<Integer, MenuItem> oMenuItems = new HashMap<Integer, MenuItem>();
		ArrayList<MenuItem> oMenuItemList = oFuncMenu.getMenuItemsByIds(oMenuItemIds, true);
		for(MenuItem oMenuItem: oMenuItemList)
			oMenuItems.put(oMenuItem.getItemId(), oMenuItem);
		
		for(PosOutletItem oPosOutletItem: oOutletItemList.getOutletItemList()) {
			// if outlet item has no check stock, store it (Ordering Panel need to hide quantity label of Lookup Button in FormMain)
			MenuItem oMenuItem = oMenuItems.get(oPosOutletItem.getItemId());
			if(oMenuItem == null)
				continue;
			
			if(!oPosOutletItem.isCheckStock())
				m_oDeleteMenuItems.add(oMenuItem);
			
			// Add log to action log list
			String sLogRemark = "ItemId:" + oPosOutletItem.getItemId() + "[" + oMenuItem.getName(AppGlobal.g_oCurrentLangIndex.get()) + "]:soldout->sell";
			if(m_bSoldoutByShop)
				AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.stock_soldout_by_shop.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", sLogRemark);
			else
				AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.stock_soldout.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", sLogRemark);
		}
		
		//handle action log
		AppGlobal.g_oActionLog.get().handleActionLog(false);

		// Clear selected rows
		this.clearSelectedItems();
		
		// Reset button description
		m_oFrameStockSoldoutFunction.resetSelectAllButton();
		
		m_bSoldoutMenuEdited = true;
		
		// Reload the item sold-out record
		this.reloadSoldoutItemRecord();
	}
}