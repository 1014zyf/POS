package app;

import java.math.BigDecimal;
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
import om.MenuItem;
import om.MenuMenu;
import om.MenuMenuLookup;
import om.MenuMenuLookupList;
import om.MenuSetMenuLookup;
import om.PosBusinessDay;
import om.PosDisplayPanelLookup;
import om.PosOrderItemAcl;
import om.PosOrderItemAclList;
import om.PosOrderItemControl;

public class FuncMenu {
	// Cache the menu item and menu menu
	private HashMap<Integer, FuncMenuItem> m_oStoredMenuItems = new HashMap<Integer, FuncMenuItem>();
	private HashMap<Integer, MenuMenu> m_oStoredMenuMenus = new HashMap<Integer, MenuMenu>();
	
	// Order item permission rules
	private PosOrderItemAclList m_oPosOrderItemAclList;
	
	// Boolean for checking have set order item control or not
	private boolean m_bIsSetOrderItemControl = false;
	
	// Last update menu time
	private DateTime m_dtLastUpdateTime;
	
	// Last error message
	private String m_sErrorMessage;
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage;
	}
	
	public FuncMenu(){
		m_dtLastUpdateTime = AppGlobal.getCurrentTime(true);
	}
	
	public FuncMenuItem addMenuItemToCache(MenuItem oMenuItem){
		if (oMenuItem == null)
			return null;
		
		FuncMenuItem oFuncMenuItem = new FuncMenuItem(oMenuItem);
		// set the flag of orderItemControl
		if(!oFuncMenuItem.getMenuItem().getOitcId().isEmpty())
			AppGlobal.g_oFuncMenu.get().setIsOrderItemControl(true);
		m_oStoredMenuItems.put(oMenuItem.getItemId(), oFuncMenuItem);
		
		for (MenuSetMenuLookup oSetMenuLookup: oMenuItem.getSetMenuLookup()) {
			if (oSetMenuLookup.isChildItem())
				addMenuItemToCache(oSetMenuLookup.getChildItem());
			else
				addMenuMenuToCache(oSetMenuLookup.getSelfSelectMenu());
		}
		return oFuncMenuItem;
	}
	
	public void addMenuMenuToCache(MenuMenu oMenuMenu){
		if (oMenuMenu == null)
			return;

		m_oStoredMenuMenus.put(oMenuMenu.getMenuId(), oMenuMenu);

		for (MenuMenuLookup oMenuMenuLookup: oMenuMenu.getMenuLookupList()) {
			if (oMenuMenuLookup.isMenuItem())
				addMenuItemToCache(oMenuMenuLookup.getMenuItem());
			else
				this.addMenuMenuToCache(oMenuMenuLookup.getMenuMenu());
		}
	}

	
	public FuncMenuItem getFuncMenuItemByItemId(int iItemId) {
		FuncMenuItem oFuncMenuItem;
		
		// Retrieve the item from the cached item list
		if (m_oStoredMenuItems.containsKey(iItemId))
			oFuncMenuItem = m_oStoredMenuItems.get(iItemId);
		else {
			MenuItem oMenuItem = new MenuItem();
			// Read menu item from OM
			if (!oMenuItem.readById(iItemId, AppGlobal.g_oFuncOutlet.get().getOutletId())) {
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
			if(sItemCode.equalsIgnoreCase(entry.getValue().getMenuItem().getCode())) {
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
			for (int i = 0; i < resultItemJSONArray.length(); i++) {
				if (resultItemJSONArray.isNull(i))
					continue;
				
				JSONObject oItemJSONObject = resultItemJSONArray.optJSONObject(i);
				oFuncMenuItemList.add(addMenuItemToCache(new MenuItem(oItemJSONObject)));
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
		
		for (int i = 0; i < resultItemJSONArray.length(); i++) {
			JSONObject oItemJSONObject = resultItemJSONArray.optJSONObject(i);
			if (oItemJSONObject != null)
				oFuncMenuItemList.add(addMenuItemToCache(new MenuItem(oItemJSONObject)));
		}
		
		return oFuncMenuItemList;
	}
	
	public MenuMenu getMenuAndContentById(int iMenuId){
		MenuMenu oMenuMenu;
		boolean bReadMenuList = true; 
		
		if(m_oStoredMenuMenus.containsKey(iMenuId)) {
			oMenuMenu = m_oStoredMenuMenus.get(iMenuId);
			if(oMenuMenu.getMenuLookupList() != null && !oMenuMenu.getMenuLookupList().isEmpty())   
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
			oMenuLookupList.readMenuMenuLookupListById(oMenuMenu.getMenuId(), AppGlobal.g_oFuncOutlet.get().getOutletId());
			oMenuMenu.setMenuLookupList(oMenuLookupList.getLookupList());
			for(MenuMenuLookup oMenuLookup:oMenuMenu.getMenuLookupList()) {
				if(oMenuLookup.isMenuItem()) {
					addMenuItemToCache(oMenuLookup.getMenuItem());
					oMenuLookup.setMenuItem(m_oStoredMenuItems.get(oMenuLookup.getItemId()).getMenuItem());
				} else
					addMenuMenuToCache(oMenuLookup.getMenuMenu());
			}
		}
		
		return oMenuMenu;
	}
	
	public MenuMenu getMenuAndContentByCode(String sMenuCode) {
		MenuMenu oMenuMenu = null;
		boolean bReadMenuList = true; 
		boolean bMenuFound = false;
		
		// Retrieve the item from the cached item list
		for(Entry<Integer, MenuMenu> entry: m_oStoredMenuMenus.entrySet()) {
			if(sMenuCode.equals(entry.getValue().getMenuCode())) {
				oMenuMenu = entry.getValue();
				bMenuFound = true;
				bReadMenuList = false;
				break;
			}
		}
		
		if(!bMenuFound) {
			// read form DB if necessary
			oMenuMenu = new MenuMenu();
			if(oMenuMenu.readByCode(sMenuCode) == false)
				// Menu not found
				return null;
			
			addMenuMenuToCache(oMenuMenu);
		}
		
		if(bReadMenuList) {
			MenuMenuLookupList oMenuLookupList = new MenuMenuLookupList();
			oMenuLookupList.readMenuMenuLookupListById(oMenuMenu.getMenuId(), AppGlobal.g_oFuncOutlet.get().getOutletId());
			oMenuMenu.setMenuLookupList(oMenuLookupList.getLookupList());
			for(MenuMenuLookup oMenuLookup:oMenuMenu.getMenuLookupList()) {
				if(oMenuLookup.isMenuItem()) {
					addMenuItemToCache(oMenuLookup.getMenuItem());
					oMenuLookup.setMenuItem(m_oStoredMenuItems.get(oMenuLookup.getItemId()).getMenuItem());
				} else
					addMenuMenuToCache(oMenuLookup.getMenuMenu());
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
		for (int i = 0; i < itemJSONArray.length(); i++) {
			if (!itemJSONArray.isNull(i) && itemJSONArray.optJSONObject(i).has("dplu_item_id"))
				oUpdateItemIds.put(itemJSONArray.optJSONObject(i).optInt("dplu_item_id"), false);
		}
		
		JSONObject requestJSONObject = new JSONObject();
	
		try {
			requestJSONObject.put("outletId", AppGlobal.g_oFuncOutlet.get().getOutletId());
			requestJSONObject.put("displayPanelItemList", itemJSONArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MenuItem oMenuItem = new MenuItem();
		JSONArray menuItemJSONArray = oMenuItem.readMenuItemDataListFromApi("gm", "menu", "getMenuItemListByMenuItemIds", requestJSONObject.toString());
		if(menuItemJSONArray == null)
			return;
		
		for(int i = 0; i < menuItemJSONArray.length(); i++) {
			if (menuItemJSONArray.optJSONObject(i).has("menu_item")) { // Hot Item / Hot Modifier
				JSONObject menuItemInfoJSONObject = menuItemJSONArray.optJSONObject(i).optJSONObject("menu_item");
				if (menuItemInfoJSONObject == null)
					continue;
				
				MenuItem oTempMenuItem = null;
				oTempMenuItem = new MenuItem(menuItemInfoJSONObject);

				// Check if the item is active
				if(!oTempMenuItem.isActive())
					continue;
				//set item's media object if exist
				if(!menuItemInfoJSONObject.isNull("media_objects")) 
					oTempMenuItem.setMediaObjectList(menuItemInfoJSONObject.optJSONArray("media_objects"));

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
	}
	
	// Schedule job to update menu item
	public ArrayList<MenuItem> updateAllStoredMenuItemList(){
		JSONObject requestJSONObject = new JSONObject();
		ArrayList<MenuItem> resultMenuItemList = new ArrayList<MenuItem>();
		try {
			requestJSONObject.put("outletId", AppGlobal.g_oFuncOutlet.get().getOutletId());
			requestJSONObject.put("lastUpdateTime", StringLib.dateTimeToString(m_dtLastUpdateTime));
		} catch (JSONException e) {
			AppGlobal.stack2Log(e);
		}
			
		MenuItem oMenuItem = new MenuItem();
		JSONArray menuItemJSONArray = oMenuItem.readMenuItemDataListFromApi("gm", "menu", "getUpdatedMenuItemList", requestJSONObject.toString());
		if(menuItemJSONArray == null)
			return resultMenuItemList;
		
		for(int i=0; i<menuItemJSONArray.length(); i++) {
			if (!menuItemJSONArray.isNull(i) && menuItemJSONArray.optJSONObject(i).has("menu_item")) { // Hot Item / Hot Modifier
				JSONObject menuItemInfoJSONObject = menuItemJSONArray.optJSONObject(i).optJSONObject("menu_item");
				if (menuItemInfoJSONObject == null)
					continue;
				
				MenuItem oTempMenuItem = null;
				oTempMenuItem = new MenuItem(menuItemInfoJSONObject);
				
				if(oTempMenuItem.isDeleted()){
					// Remove the item from cache
					m_oStoredMenuItems.remove(oTempMenuItem.getItemId());
				}else{
					//set item's media object if exist
					if(!menuItemInfoJSONObject.isNull("media_objects")) 
						oTempMenuItem.setMediaObjectList(menuItemInfoJSONObject.optJSONArray("media_objects"));
					addMenuItemToCache(oTempMenuItem);
					
					// For return
					resultMenuItemList.add(oTempMenuItem);
				}
			}
		}
		
		m_dtLastUpdateTime = AppGlobal.getCurrentTime(true);
		
		return resultMenuItemList;
	}
	
	// Get a list of menu item by menu item ids
	public ArrayList<MenuItem> getMenuItemsByIds(ArrayList<Integer> oMenuItemIds, boolean bNotCheckStatus){
		JSONArray itemJSONArray = new JSONArray();
		JSONArray menuItemJSONArray = new JSONArray();
		ArrayList<MenuItem> resultMenuItemList = new ArrayList<MenuItem>();
		
		for(Integer iId:oMenuItemIds){
			if (m_oStoredMenuItems.containsKey(iId)) {
				FuncMenuItem oFuncMenuItem = m_oStoredMenuItems.get(iId);
				resultMenuItemList.add(oFuncMenuItem.getMenuItem());
			} else {
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
		}
		
		if (itemJSONArray.length() > 0) {
			JSONObject requestJSONObject = new JSONObject();
			try {
				requestJSONObject.put("displayPanelItemList", itemJSONArray);
				requestJSONObject.put("notCheckStatus", bNotCheckStatus);
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
				
			MenuItem oMenuItem = new MenuItem();
			menuItemJSONArray = oMenuItem.readMenuItemDataListFromApi("gm", "menu", "getMenuItemListByMenuItemIds", requestJSONObject.toString());
			if (menuItemJSONArray == null)
				return resultMenuItemList;
		}
		for (int i = 0; i < menuItemJSONArray.length(); i++) {
			if (!menuItemJSONArray.isNull(i) || menuItemJSONArray.optJSONObject(i).has("menu_item")) { // Hot Item / Hot Modifier
				JSONObject menuItemInfoJSONObject = menuItemJSONArray.optJSONObject(i).optJSONObject("menu_item");
				MenuItem oMenuItem2 = new MenuItem(menuItemInfoJSONObject);
				addMenuItemToCache(oMenuItem2);
				resultMenuItemList.add(oMenuItem2);
			}
		}
		
		return resultMenuItemList;
	}

	// construct content lookup list which is used in FormCommonLookup to display lookup buttons
	public void constructContentLookupList(MenuMenu oMenuMenu, String sType, List<FuncLookupButtonInfo> oContentLookupList, boolean bModifierChecking) {
		int iIndex = oContentLookupList.size();
		int iBilingualLangIndex = AppGlobal.g_oFuncOutlet.get().getBilingualLangIndexByeLangIndex(AppGlobal.g_oCurrentLangIndex.get());
		//form the lookup data list
		for (MenuMenuLookup oMenuLookup: oMenuMenu.getMenuLookupList()) {
			// Check if the item can be the modifier or not
			if (oMenuLookup.isMenuItem() && (bModifierChecking && !oMenuLookup.getMenuItem().isModifier()))
				continue;
			
			if (!oMenuLookup.getType().equals(sType))
				continue;

			FuncLookupButtonInfo oLookupButtonInfo = new FuncLookupButtonInfo();
			oLookupButtonInfo.setSeq(iIndex+oMenuLookup.getSeq());
			if (oMenuLookup.isMenuItem()) {
				oLookupButtonInfo.setName(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, oMenuLookup.getMenuItem().getBilingualName(AppGlobal.g_oCurrentLangIndex.get(), iBilingualLangIndex)));
				oLookupButtonInfo.setType(PosDisplayPanelLookup.TYPE_HOT_ITEM);
				oLookupButtonInfo.setId(oMenuLookup.getMenuItem().getItemId());
				if(oMenuLookup.getMenuItem().isOpenDescriptionInputName())
					oLookupButtonInfo.setParameter("askName");
			} else {
				oLookupButtonInfo.setName(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, oMenuLookup.getMenuMenu().getBilingualName(AppGlobal.g_oCurrentLangIndex.get(), iBilingualLangIndex)));
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
	
	// get bIsSetOrderItemControl
	public boolean getIsSetOrderItemControl(){
		return this.m_bIsSetOrderItemControl;
	}
	
	// set m_bIsSetOrderItemControl
	public void setIsOrderItemControl(boolean bIsOrderItemControl){
		this.m_bIsSetOrderItemControl = bIsOrderItemControl;
	}
	
	// clear m_oStoredMenuMenu and m_oStoredMenuItem
	public void clearStoredMenuMenusAndMenuItemsList(){
		this.m_oStoredMenuItems.clear();
		this.m_oStoredMenuMenus.clear();
	}
	
	// check item's order permission
	public String getItemOrderAllowance(int iOrderItemGroupId, List<Integer> oUserGrpIds) {
		if(oUserGrpIds.isEmpty())
			return PosOrderItemAcl.ALLOW_YES;
			
		String sAllowance = PosOrderItemAcl.ALLOW_NO;
		for(int i = 0; i < oUserGrpIds.size(); i++) {
			int iUserGrpId = oUserGrpIds.get(i);
			ArrayList<PosOrderItemAcl> oPermissionRules = m_oPosOrderItemAclList.getOrderItemAcls(AppGlobal.g_oFuncOutlet.get().getOutletId(), iUserGrpId, iOrderItemGroupId);
			if(oPermissionRules.isEmpty()) {
				sAllowance = PosOrderItemAcl.ALLOW_YES;
				break;
			}
			
			boolean bRuleMatched = false;
			for(int j = 0; j < oPermissionRules.size(); j++) {
				PosOrderItemAcl oPermissionRule = oPermissionRules.get(j);
				if(oPermissionRule.getAllow().equals(PosOrderItemAcl.ALLOW_NO) || oPermissionRule.getAllow().equals(PosOrderItemAcl.ALLOW_NO_ASK_FOR_ANOTHER_USER_AUTHORITY)) {
					sAllowance = oPermissionRule.getAllow();
					bRuleMatched = true;
					break;
				}else {
					PosBusinessDay oBusinessDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay();
					bRuleMatched = oPermissionRule.checkRulesCondition(oBusinessDay.getDate(), oBusinessDay.getDayOfWeek(), oBusinessDay.isSpecialDay(), oBusinessDay.isDayBeforeSpecialDay(), oBusinessDay.isHoliday(), oBusinessDay.isDayBeforeHoliday());
					if(bRuleMatched) {
						sAllowance = PosOrderItemAcl.ALLOW_YES;
						break;
					}
				}
			}
			
			if(bRuleMatched)
				break;
		}
		
		return sAllowance;
	}
}
