package app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import externallib.StringLib;
import app.model.MenuItem;
import app.model.MenuMenu;
import app.model.MenuMenuLookup;
import app.model.MenuMenuLookupList;
import app.model.PosBusinessDay;
import app.model.PosDisplayPanelLookup;
import app.model.PosOrderItemAcl;
import app.model.PosOrderItemAclList;

public class FuncMenu {
	
	// Cache the menu item and menu menu
	private HashMap<Integer, FuncMenuItem> m_oStoredMenuItems = new HashMap<Integer, FuncMenuItem>();
	private HashMap<Integer, MenuMenu> m_oStoredMenuMenus = new HashMap<Integer, MenuMenu>();
	
	// Order item permission rules
	private PosOrderItemAclList m_oPosOrderItemAclList;
	
	// Last update menu time
	private DateTime m_dtLastUpdateTime;
	
	// Last error message
	private String m_sErrorMessage;
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage;
	}
	
	public FuncMenu(){
		m_dtLastUpdateTime = new DateTime().withZone(DateTimeZone.UTC);
	}
	
	public FuncMenuItem addMenuItemToCache(MenuItem oMenuItem){
		FuncMenuItem oFuncMenuItem;
		
		oFuncMenuItem = new FuncMenuItem(oMenuItem);
		m_oStoredMenuItems.put(oMenuItem.getItemId(), oFuncMenuItem);
		
		return oFuncMenuItem;
	}
	
	public void addMenuMenuToCache(MenuMenu oMenuMenu){
		m_oStoredMenuMenus.put(oMenuMenu.getMenuId(), oMenuMenu);
	}
	
	public FuncMenuItem getFuncMenuItemByItemId(int iItemId){
		MenuItem oMenuItem;
		FuncMenuItem oFuncMenuItem;
		
		// Retrieve the item from the cached item list
		if(m_oStoredMenuItems.containsKey(iItemId)) {
			oFuncMenuItem = m_oStoredMenuItems.get(iItemId);
		}else{			
			oMenuItem = new MenuItem();
			// Read menu item from OM
			if(!oMenuItem.readById(iItemId)) {
				m_sErrorMessage = AppGlobal.g_oLang.get()._("item_cannot_be_found");
				return null;
			}
			
			// Add the menu item to cache
			oFuncMenuItem = addMenuItemToCache(oMenuItem);
		}
		
		return oFuncMenuItem;
	}

	// find item by item code (case sensitive)
	public FuncMenuItem getFuncMenuItemByItemCode(String sItemCode){
		MenuItem oMenuItem;
		FuncMenuItem oFuncMenuItem = null;
		boolean bItemFound = false;
		
		// Retrieve the item from the cached item list
		for(Entry<Integer, FuncMenuItem> entry: m_oStoredMenuItems.entrySet()) {
			if(sItemCode.equals(entry.getValue().getMenuItem().getCode())) {
				oFuncMenuItem = entry.getValue();
				bItemFound = true;
				break;
			}
		}
		
		if(!bItemFound) {
			oMenuItem = new MenuItem();
			// Read menu item from OM
			if(!oMenuItem.readByItemCode(sItemCode)) {
				m_sErrorMessage = AppGlobal.g_oLang.get()._("item_cannot_be_found");
				return null;
			}
			
			// Add the menu item to cache
			oFuncMenuItem = addMenuItemToCache(oMenuItem);
		}
		
		return oFuncMenuItem;
	}

	// find item by item code (case insensitive)
	public ArrayList<FuncMenuItem> getFuncMenuItemByItemCodeCaseInsensitive(String sItemCode){
		MenuItem oMenuItem1;
		ArrayList<FuncMenuItem> oFuncMenuItemList = new ArrayList<>();
		boolean bItemFound = false;
		
		// Retrieve the item from the cached item list
		for(Entry<Integer, FuncMenuItem> entry: m_oStoredMenuItems.entrySet()) {
			if(sItemCode.equals(entry.getValue().getMenuItem().getCode())) {
				oFuncMenuItemList.add(entry.getValue());
				bItemFound = true;
			}
		}
		
		if(!bItemFound) {
			oMenuItem1 = new MenuItem();
			// Read menu item from OM
			JSONArray resultItemJSONArray = oMenuItem1.readItemListByItemCode(sItemCode);
			if(resultItemJSONArray == null) {
				m_sErrorMessage = AppGlobal.g_oLang.get()._("item_cannot_be_found");
				return oFuncMenuItemList;
			}
			
			for(int i = 0; i < resultItemJSONArray.length(); i++) {
				try {
					JSONObject oItemJSONObject = resultItemJSONArray.getJSONObject(i);
					oFuncMenuItemList.add(addMenuItemToCache(new MenuItem(oItemJSONObject)));
				} catch (JSONException e) {
					AppGlobal.stack2Log(e);
				}
			}
		}
		
		return oFuncMenuItemList;
	}
	
	// find item by item SKU (case insensitive)
	public ArrayList<FuncMenuItem> getFuncMenuItemByItemSKUCaseInsensitive(String sItemSKU){
		MenuItem oMenuItem1;
		ArrayList<FuncMenuItem> oFuncMenuItemList = new ArrayList<>();
		
		oMenuItem1 = new MenuItem();
		// Read menu item from OM
		JSONArray resultItemJSONArray = oMenuItem1.readItemListByItemSKU(sItemSKU);
		if(resultItemJSONArray == null) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("item_cannot_be_found");
			return oFuncMenuItemList;
		}
		
		for(int i = 0; i < resultItemJSONArray.length(); i++) {
			try {
				JSONObject oItemJSONObject = resultItemJSONArray.getJSONObject(i);
				oFuncMenuItemList.add(addMenuItemToCache(new MenuItem(oItemJSONObject)));
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		
		return oFuncMenuItemList;
	}
	
	public MenuMenu getMenuAndContentById(int iMenuId){
		MenuMenu oMenuMenu;
		boolean bReadMenuList = true; 
		
		if(m_oStoredMenuMenus.containsKey(iMenuId)) {
			oMenuMenu = m_oStoredMenuMenus.get(iMenuId);
			if(oMenuMenu.getMenuLookupList() != null && oMenuMenu.getMenuLookupList().size() > 0)   
				bReadMenuList = false;
		}else{
			// read form DB if necessary
			oMenuMenu = new MenuMenu();
			if(oMenuMenu.readById(iMenuId) == false)
				// Menu not found
				return null;
			
			addMenuMenuToCache(oMenuMenu);
		}
		
		if(bReadMenuList) {
			MenuMenuLookupList oMenuLookupList = new MenuMenuLookupList();
			oMenuLookupList.readMenuMenuLookupListById(oMenuMenu.getMenuId());
			oMenuMenu.setMenuLookupList(oMenuLookupList.getLookupList());
			
			for(MenuMenuLookup oMenuLookup:oMenuMenu.getMenuLookupList()) {
				if(oMenuLookup.isMenuItem()) {
					addMenuItemToCache(oMenuLookup.getMenuItem());

					oMenuLookup.setMenuItem(m_oStoredMenuItems.get(oMenuLookup.getItemId()).getMenuItem());
				}else {
					addMenuMenuToCache(oMenuLookup.getMenuMenu());
				}
			}
		}
		
		return oMenuMenu;
	}
	
	// Update stored menu item list
	public void updateStoredMenuItemList(JSONArray itemJSONArray){

		// Check if there is item requiring to update
		if(itemJSONArray.length() == 0)
			// No item
			return;
		
		// Create a list of item id and update flag of the item going to be updated
		HashMap<Integer, Boolean> oUpdateItemIds = new HashMap<Integer, Boolean>();
		for(int i=0; i<itemJSONArray.length(); i++) {
			try {
				if(itemJSONArray.getJSONObject(i).has("dplu_item_id")){
					oUpdateItemIds.put(itemJSONArray.getJSONObject(i).getInt("dplu_item_id"), false);
				}
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		
		JSONObject requestJSONObject = new JSONObject();
		try {
			requestJSONObject.put("displayPanelItemList", itemJSONArray);
			
			MenuItem oMenuItem = new MenuItem();
			JSONArray menuItemJSONArray = oMenuItem.readMenuItemDataListFromApi("gm", "menu", "getMenuItemListByMenuItemIds", requestJSONObject.toString());
			if(menuItemJSONArray == null)
				return;
			
			for(int i=0; i<menuItemJSONArray.length(); i++) {
				if (menuItemJSONArray.getJSONObject(i).has("menu_item")) { // Hot Item / Hot Modifier
					JSONObject menuItemInfoJSONObject = menuItemJSONArray.getJSONObject(i).getJSONObject("menu_item");
	
					MenuItem oTempMenuItem = null;
					oTempMenuItem = new MenuItem(menuItemInfoJSONObject);
	
					// Check if the item is active
					if(!oTempMenuItem.isActive())
						continue;
					
					//set item's media object if exist
					if(!menuItemInfoJSONObject.isNull("media_objects")) 
						oTempMenuItem.setMediaObjectList(menuItemInfoJSONObject.getJSONArray("media_objects"));

					addMenuItemToCache(oTempMenuItem);
					
					// Set the item list update flag to true
					if(oUpdateItemIds.containsKey(oTempMenuItem.getItemId()))
						oUpdateItemIds.put(oTempMenuItem.getItemId(), true);
				}
			}
			
			for(Map.Entry<Integer, Boolean> entry: oUpdateItemIds.entrySet()){
				if(entry.getValue() == false){
					// Item cannot be updated, remove the item from the stored item list
					if(m_oStoredMenuItems.containsKey(entry.getKey()))
						m_oStoredMenuItems.remove(entry.getKey());
				}
			}
			
		} catch (JSONException e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	// Schedule job to update menu item
	public ArrayList<MenuItem> updateAllStoredMenuItemList(){
		
		JSONObject requestJSONObject = new JSONObject();
		ArrayList<MenuItem> resultMenuItemList = new ArrayList<MenuItem>();
		try {
			requestJSONObject.put("lastUpdateTime", StringLib.dateTimeToString(m_dtLastUpdateTime));
			
			MenuItem oMenuItem = new MenuItem();
			JSONArray menuItemJSONArray = oMenuItem.readMenuItemDataListFromApi("gm", "menu", "getUpdatedMenuItemList", requestJSONObject.toString());
			if(menuItemJSONArray == null)
				return resultMenuItemList;
			
			for(int i=0; i<menuItemJSONArray.length(); i++) {
				if (menuItemJSONArray.getJSONObject(i).has("menu_item")) { // Hot Item / Hot Modifier
					JSONObject menuItemInfoJSONObject = menuItemJSONArray.getJSONObject(i).getJSONObject("menu_item");
	
					MenuItem oTempMenuItem = null;
					oTempMenuItem = new MenuItem(menuItemInfoJSONObject);
					
					if(oTempMenuItem.isDeleted()){
						// Remove the item from cache
						m_oStoredMenuItems.remove(oTempMenuItem.getItemId());
					}else{
						//set item's media object if exist
						if(!menuItemInfoJSONObject.isNull("media_objects")) 
							oTempMenuItem.setMediaObjectList(menuItemInfoJSONObject.getJSONArray("media_objects"));
	
						addMenuItemToCache(oTempMenuItem);
						
						// For return
						resultMenuItemList.add(oTempMenuItem);
					}
				}
			}
			
		} catch (JSONException e) {
			AppGlobal.stack2Log(e);
		}
		
		m_dtLastUpdateTime = new DateTime().withZone(DateTimeZone.UTC);
		
		return resultMenuItemList;
	}
	
	// Get a list of menu item by menu item ids
	public ArrayList<MenuItem> getMenuItemsByIds(ArrayList<Integer> oMenuItemIds){
		
		JSONArray itemJSONArray = new JSONArray();
		ArrayList<MenuItem> resultMenuItemList = new ArrayList<MenuItem>();
		
		for(Integer iId:oMenuItemIds){
			JSONObject itemJsonObject = new JSONObject();
			try {
				itemJsonObject.put("dplu_menu_id", "0");
				itemJsonObject.put("dplu_item_id", iId + "");
				itemJsonObject.put("dplu_id", "0");
				itemJsonObject.put("dplu_type", PosDisplayPanelLookup.TYPE_HOT_ITEM);
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
			
			itemJSONArray.put(itemJsonObject);
		}
		
		JSONObject requestJSONObject = new JSONObject();
		try {
			requestJSONObject.put("displayPanelItemList", itemJSONArray);
			
			MenuItem oMenuItem = new MenuItem();
			JSONArray menuItemJSONArray = oMenuItem.readMenuItemDataListFromApi("gm", "menu", "getMenuItemListByMenuItemIds", requestJSONObject.toString());
			if(menuItemJSONArray == null)
				return resultMenuItemList;
			
			for(int i=0; i<menuItemJSONArray.length(); i++) {
				if (menuItemJSONArray.getJSONObject(i).has("menu_item")) { // Hot Item / Hot Modifier
					JSONObject menuItemInfoJSONObject = menuItemJSONArray.getJSONObject(i).getJSONObject("menu_item");
	
					MenuItem oMenuItem2 = null;
					oMenuItem2 = new MenuItem(menuItemInfoJSONObject);
	
					resultMenuItemList.add(oMenuItem2);
				}
			}			
		} catch (JSONException e) {
			AppGlobal.stack2Log(e);
		}
		
		return resultMenuItemList;
	}

	// construct content lookup list which is used in FormCommonLookup to display lookup buttons
	public void constructContentLookupList(MenuMenu oMenuMenu, String sType, List<FuncLookupButtonInfo> oContentLookupList, int iIndex) {
		int iBilingualLangIndex = AppGlobal.g_oFuncOutlet.get().getBilingualLangIndexByeLangIndex(AppGlobal.g_oCurrentLangIndex.get());
		//form the lookup data list
		for (MenuMenuLookup oMenuLookup: oMenuMenu.getMenuLookupList()) {
			// Check if the item can be the modifier or not
			if (oMenuLookup.isMenuItem() && !oMenuLookup.getMenuItem().isModifier())
				continue;
			
			if (!oMenuLookup.getType().equals(sType))
				continue;

			FuncLookupButtonInfo oLookupButtonInfo = new FuncLookupButtonInfo();
			oLookupButtonInfo.setSeq(iIndex+oMenuLookup.getSeq());
			if (oMenuLookup.isMenuItem()) {
				oLookupButtonInfo.setName(oMenuLookup.getMenuItem().getBilingualName(AppGlobal.g_oCurrentLangIndex.get(), iBilingualLangIndex));
				oLookupButtonInfo.setType(PosDisplayPanelLookup.TYPE_HOT_ITEM);
				oLookupButtonInfo.setId(oMenuLookup.getMenuItem().getItemId());
				if(oMenuLookup.getMenuItem().isOpenDescriptionInputName())
					oLookupButtonInfo.setParameter("askName");
			} else {
				oLookupButtonInfo.setName(oMenuLookup.getMenuMenu().getBilingualName(AppGlobal.g_oCurrentLangIndex.get(), iBilingualLangIndex));
				oLookupButtonInfo.setType(PosDisplayPanelLookup.TYPE_MENU_LOOKUP);
				oLookupButtonInfo.setId(oMenuLookup.getMenuMenu().getMenuId());
			}
			oContentLookupList.add(oLookupButtonInfo);
		}
	}
	
	// get all the order item permission rules
	public void loadMenuItemOrderPermissionRules() {
		if(m_oPosOrderItemAclList != null)
			return;
		
		m_oPosOrderItemAclList = new PosOrderItemAclList();
		m_oPosOrderItemAclList.readAllOrderItemAcl();
	}
	
	// check item's order permission
	public boolean getItemOrderAllowance(int iOrderItemGroupId) {
		int i=0, j=0;
		boolean bAllowance = false;
		List<Integer> oUserGrpIds = AppGlobal.g_oFuncUser.get().getUserGroupList();
		
		if(oUserGrpIds.size() == 0)
			return true;
		
		for(i=0; i<oUserGrpIds.size(); i++) {
			int iUserGrpId = oUserGrpIds.get(i);
			ArrayList<PosOrderItemAcl> oPermissionRules = m_oPosOrderItemAclList.getOrderItemAcls(AppGlobal.g_oFuncOutlet.get().getOutletId(), iUserGrpId, iOrderItemGroupId);
			if(oPermissionRules.size() == 0) {
				bAllowance = true;
				break;
			}
			
			boolean bRuleMatched = false;
			for(j=0; j<oPermissionRules.size(); j++) {
				PosOrderItemAcl oPermissionRule = oPermissionRules.get(j);
				if(oPermissionRule.getAllow().equals(PosOrderItemAcl.ALLOW_NO)) {
					bAllowance = false;
					bRuleMatched = true;
					break;
				}else {
					PosBusinessDay oBusinessDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay();
					bRuleMatched = oPermissionRule.checkRulesCondition(oBusinessDay.getDate(), oBusinessDay.getDayOfWeek(), oBusinessDay.isSpecialDay(), oBusinessDay.isDayBeforeSpecialDay(), oBusinessDay.isHoliday(), oBusinessDay.isDayBeforeHoliday());
					if(bRuleMatched) {
						bAllowance = true;
						break;
					}
				}
			}
			
			if(bRuleMatched)
				break;
		}
		
		return bAllowance;
	}
}
