package app.controller;

import app.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import app.AppThreadManager;

public class FuncCheckItem {
	// Check item
	private PosCheckItem m_oCheckItem;
	
	// Child item list
	private ArrayList<FuncCheckItem> m_oChildItemList;
	
	// Modifier list
	private ArrayList<FuncCheckItem> m_oModifierList;
	private BigDecimal m_dOpenModiPriceRate;
	
	// Check discount item for check discount
	private HashMap<Integer, PosCheckDiscountItem> m_oCheckDiscountItemList;
	
	// Item discount list
	private ArrayList<PosCheckDiscount> m_oItemDiscountList;
	
	// Extra info list
	private ArrayList<PosCheckExtraInfo> m_oExtraInfoList;
	
	// Mix And Match Rule
	private int m_iMixAndMatchRuleId;
	// Mix and match child item list
	private ArrayList<FuncCheckItem> m_oMixAndMatchChildItemList;
	// Flag to determine if the item is new and applied mix and match rule
	private boolean m_bNewItemWithMixAndMatchRule;
	
	// Action log for new item
	private LinkedList<PosActionLog> m_oActionLogList;
	
	// Last error message
	private String m_sErrorMessage;
	
	// Index of Modifier List the item from
	private int m_iParentTabIndex;
	
	// Parent Func Check Item
	private FuncCheckItem m_oParentFuncCheckItem = null;
	
	// Receiving quantity of the item in Stock Delivery Mode
	private Boolean m_bStockDeliveryInvoice;
	private PosStockDeliveryInvoiceItem m_oPosStockDeliveryInvoiceItem;
	
	// Control item kitchen slip
	private Boolean m_bNoKitchenSlip = false;
	
	// Flag to determine item service charge is waived due to fast food mode or takeout, NOT by override
	private Boolean m_bWaiveServiceChargeNotByOverride;
	
	// Flag to determine item price level is manually changed and so skip the override checking
	private Boolean m_bChangePriceLevelManually;
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage;
	}
	
	public FuncCheckItem(){
		m_oCheckItem = new PosCheckItem();
		m_oChildItemList = new ArrayList<FuncCheckItem>();
		m_oModifierList = new ArrayList<FuncCheckItem>();
		m_dOpenModiPriceRate = null;
		m_oCheckDiscountItemList = new HashMap<Integer, PosCheckDiscountItem>();
		m_oItemDiscountList = new ArrayList<PosCheckDiscount>();
		m_oExtraInfoList = new ArrayList<PosCheckExtraInfo>();
		m_oMixAndMatchChildItemList = new ArrayList<FuncCheckItem>();
		m_oActionLogList = new LinkedList<PosActionLog>();
		m_oPosStockDeliveryInvoiceItem = new PosStockDeliveryInvoiceItem();
		m_bStockDeliveryInvoice = false;
		m_iMixAndMatchRuleId = 0;
		m_bNewItemWithMixAndMatchRule = false;
		m_bWaiveServiceChargeNotByOverride = false;
		m_bChangePriceLevelManually = false;
	}
	
	// Create FuncCheckItem from PosCheckItem loading from database
	public FuncCheckItem(PosCheckItem oCheckItem) {
		int i=0;
		
		m_oCheckItem = new PosCheckItem(oCheckItem);
		m_oChildItemList = new ArrayList<FuncCheckItem>();
		m_oModifierList = new ArrayList<FuncCheckItem>();
		m_dOpenModiPriceRate = null;
		m_oCheckDiscountItemList = new HashMap<Integer, PosCheckDiscountItem>();
		m_oItemDiscountList = new ArrayList<PosCheckDiscount>();
		m_oExtraInfoList = new ArrayList<PosCheckExtraInfo>();
		m_oMixAndMatchChildItemList = new ArrayList<FuncCheckItem>();
		m_oActionLogList = new LinkedList<PosActionLog>();
		m_oPosStockDeliveryInvoiceItem = new PosStockDeliveryInvoiceItem();
		m_bStockDeliveryInvoice = false;
		m_iMixAndMatchRuleId = 0;
		m_bNewItemWithMixAndMatchRule = false;
		m_bWaiveServiceChargeNotByOverride = false;
		m_bChangePriceLevelManually = false;
		
		//handle item's modifier items for each item
		if(m_oCheckItem.hasModifier()) {
			for(i=0; i<m_oCheckItem.getModifierList().size(); i++) {
				FuncCheckItem oModiFuncCheckItem = new FuncCheckItem(m_oCheckItem.getModifierList().get(i));
				
				// Set the parent func check item of the child
				oModiFuncCheckItem.setParentFuncCheckItem(this);
				
				if(oModiFuncCheckItem.getMenuItem() != null){
					if(oModiFuncCheckItem.getMenuItem().isAddUnitModifierPriceToTotalModifierOperator()) 
						oModiFuncCheckItem.setOpenModiPriceRate(oModiFuncCheckItem.getCheckItem().getQty().multiply(oModiFuncCheckItem.getCheckItem().getOriginalPrice()));
				}
				
				this.addModifierToList(oModiFuncCheckItem, false);
			}
		}
		
		//handle item's child for each item
		if(m_oCheckItem.hasChildItem()) {
			for(i=0; i<m_oCheckItem.getChildItemList().size(); i++) {
				FuncCheckItem oChildFuncCheckItem = new FuncCheckItem(m_oCheckItem.getChildItemList().get(i));
				
				// Set the parent func check item of the child
				oChildFuncCheckItem.setParentFuncCheckItem(this);
				
				this.addChildItemToList(oChildFuncCheckItem, false);
			}
		}
		
		//handle item's discount for each item
		if(m_oCheckItem.getItemDiscountList().size() > 0) {
			for(PosCheckDiscount oItemDiscount:m_oCheckItem.getItemDiscountList())
				this.addItemDiscountToList(new PosCheckDiscount(oItemDiscount));
		}
		
		//handle item's extra info for each item
		if(m_oCheckItem.getExtraInfoList().size() > 0) {
			for(PosCheckExtraInfo oExtraInfo:m_oCheckItem.getExtraInfoList())
				this.addExtraInfoToList(new PosCheckExtraInfo(oExtraInfo));
		}
	}
	
	// Clone to new item
	public FuncCheckItem(FuncCheckItem oFuncCheckItem, FuncCheckItem oParentFuncCheckItem) {
		this.m_oCheckItem = new PosCheckItem(oFuncCheckItem.getCheckItem());
		
		// Set parent item
		this.setParentFuncCheckItem(oParentFuncCheckItem);
		
		// Set kitchen slip setting
		this.setNoKitchenSlip(oFuncCheckItem.getNoKitchenSlip());
		
		this.m_oModifierList = new ArrayList<FuncCheckItem>();
		for(int i = 0; i < oFuncCheckItem.m_oModifierList.size(); i++) {
			FuncCheckItem oModiFuncCheckItem = new FuncCheckItem(oFuncCheckItem.m_oModifierList.get(i), null);
			
			// Set the parent func check item of the child
			oModiFuncCheckItem.setParentFuncCheckItem(this);
			
			this.m_oModifierList.add(oModiFuncCheckItem);
		}
		this.m_dOpenModiPriceRate = oFuncCheckItem.getOpenModiPriceRate();
		
		// Clone applied check discount item
		this.m_oCheckDiscountItemList = new HashMap<Integer, PosCheckDiscountItem>();
		for(Entry<Integer, PosCheckDiscountItem> entry: oFuncCheckItem.m_oCheckDiscountItemList.entrySet()) {
			PosCheckDiscountItem oPosCheckDiscountItem = new PosCheckDiscountItem(entry.getValue());
			this.m_oCheckDiscountItemList.put(entry.getKey(), oPosCheckDiscountItem);
		}
		
		// Clone item discount
		this.m_oItemDiscountList = new ArrayList<PosCheckDiscount>();
		for(int i = 0; i < oFuncCheckItem.m_oItemDiscountList.size(); i++) {
			this.m_oItemDiscountList.add(new PosCheckDiscount(oFuncCheckItem.m_oItemDiscountList.get(i)));
		}
		
		// *** If this is set menu parent item, cannot clone child item, process by child item in the code afterward
		this.m_oChildItemList = new ArrayList<FuncCheckItem>();
		// if this is set menu child item
		if(this.isSetMenuItem() && oParentFuncCheckItem != null)
			oParentFuncCheckItem.addChildItemToList(this, true);
		
		// The mix and match child list will be updated for each send check, therefore, not clone here
		this.m_oMixAndMatchChildItemList = new ArrayList<FuncCheckItem>();
		
		this.m_oExtraInfoList = new ArrayList<PosCheckExtraInfo>();
		if(!oFuncCheckItem.getExtraInfoList().isEmpty()) {
			for(int i=0; i<oFuncCheckItem.getExtraInfoList().size(); i++)
				this.m_oExtraInfoList.add(new PosCheckExtraInfo(oFuncCheckItem.getExtraInfoList().get(i)));
		}

		m_oActionLogList = new LinkedList<PosActionLog>();
		m_oPosStockDeliveryInvoiceItem = new PosStockDeliveryInvoiceItem();
		m_bStockDeliveryInvoice = false;
		m_iMixAndMatchRuleId = 0;
		m_bNewItemWithMixAndMatchRule = oFuncCheckItem.isNewItemWithMixAndMatchRule();
		m_bWaiveServiceChargeNotByOverride = oFuncCheckItem.isWaiveServiceChargeNotByOverride();
		m_bChangePriceLevelManually = oFuncCheckItem.isChangePriceLevelManually();
	}
	
	public PosCheckItem getCheckItem(){
		return m_oCheckItem;
	}
	
	public boolean isOldItem() {
		if (m_bStockDeliveryInvoice){
			if (m_oPosStockDeliveryInvoiceItem.getSitmId() > 0)
				return true;
			else
				return false;
		}else{
			if (m_oCheckItem.getCitmId() > 0)
				return true;
			else
				return false;
		}
	}
	
	public boolean isSetMenuItem() {
		return m_oCheckItem.isSetMenuChildItem();
	}
	
	public boolean isModifierItem() {
		return m_oCheckItem.isModifierItem();
	}
	
	public boolean isCouponItem() {
		boolean bIsCouponItem = false;
				
		if(m_oExtraInfoList.size() > 0) {
			for(PosCheckExtraInfo oExtraInfo:m_oExtraInfoList) {
				if(oExtraInfo.getSection().equals("item_type") && oExtraInfo.getVariable().equals("coupon_item") && oExtraInfo.getValue() != null) {
					bIsCouponItem = true;
					break;
				}
			}
		}
		
		return bIsCouponItem;
	}
	
	public boolean isRedeemCouponItem() {
		boolean bIsRedeemCouponItem = false;
		
		if(m_oExtraInfoList.size() > 0) {
			for(PosCheckExtraInfo oExtraInfo:m_oExtraInfoList) {
				if(oExtraInfo.getSection().equals("online_coupon") && oExtraInfo.getVariable().equals("redeem_count") && oExtraInfo.getValue() != null) {
					bIsRedeemCouponItem = true;
					break;
				}
			}
		}
		
		return bIsRedeemCouponItem;
	}

	public boolean isPreorderItem() {
		boolean bIsPreorderItem = false;
		
		if(m_oExtraInfoList.size() > 0) {
			for(PosCheckExtraInfo oExtraInfo: m_oExtraInfoList) {
				if(oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_PREORDER) && oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_ITEM)) {
					bIsPreorderItem = true;
					break;
				}
			}
		}
		
		return bIsPreorderItem;
	}
	
	public boolean hasModifier() {
		if (m_oModifierList.size() > 0)
			return true;
		else
			return false;
	}
	
	public boolean hasChildItem() {
		if(this.m_oChildItemList.size() > 0)
			return true;
		else
			return false;
	}
	
	public boolean hasAppliedCheckDiscount() {
		if (m_oCheckDiscountItemList.size() > 0)
			return true;
		else
			return false;
	}
	
	public boolean hasAppliedCheckDiscountByIndex(int iIndex) {
		if (m_oCheckDiscountItemList.containsKey(iIndex))
			return true;
		else
			return false;
	}
	
	// if bUsedForDiscountOnly = true, check whether item has item discount
	// if bUsedForDiscountOnly = false, check whether item has both item discount and extra charge
	public boolean hasItemDiscount(boolean bUsedForDiscountOnly) {
		if (m_oItemDiscountList.size() > 0) {
			if(bUsedForDiscountOnly) {
				for(PosCheckDiscount oCheckDiscount: m_oItemDiscountList) {
					if(oCheckDiscount.isUsedForDiscount())
						return true;
				}
				return false;
			} else
				return true;
		} else
			return false;
	}
	
	public boolean hasExtraInfo() {
		if (m_oExtraInfoList.size() > 0)
			return true;
		else
			return false;
	}
	
	public boolean hasDelivered() {
		if(m_oCheckItem.getDeliveryTime() != null)
			return true;
		else
			return false;
	}
	
	public boolean hasRush() {
		if(m_oCheckItem.getOrderLocTime() != null)
			return true;
		else
			return false;
	}
	
	public boolean isDisable() {
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if(oFuncMenuItem == null)
			return true;
		
		if(oFuncMenuItem.getMenuItem().isDisableMode()) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("unable_to_order_disable_item");
			return true;
		}else
			return false;
	}
	
	public boolean isInactive() {
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if(oFuncMenuItem == null)
			return true;
		
		if(oFuncMenuItem.getMenuItem().isInactiveMode()) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("unable_to_order_inactive_item");
			return true;
		}else
			return false;
	}
	
	public boolean isOpenDescription(){
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if(oFuncMenuItem == null)
			return false;
		
		if(oFuncMenuItem.getMenuItem().isOpenDescriptionInputName())
			return true;
		else
			return false;
	}
	
	public boolean isAppendOpenDescription(){
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if(oFuncMenuItem == null)
			return false;
		
		if(oFuncMenuItem.getMenuItem().isAppendDescriptionInputName())
			return true;
		else
			return false;
	}
	
	public boolean isOpenPrice(){
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if(oFuncMenuItem == null)
			return false;
		
		MenuItem oMenuItem = oFuncMenuItem.getMenuItem();
		int iPriceLevel = this.getCheckItem().getPriceLevel();
		if(m_oCheckItem.isBasicItem()){
			if(iPriceLevel == 0){
				if(oMenuItem.getBasicPrice(0) == null){
					return true;
				}
			}else
			if(oMenuItem.getBasicPrice(iPriceLevel) == null){
				if(oMenuItem.getBasicPrice(0) == null){
					return true;
				}
			}
		}else
		if(isSetMenuItem()){
			if(iPriceLevel == 0){
				if(oMenuItem.getChildPrice(0) == null){
					return true;
				}
			}else
			if(oMenuItem.getChildPrice(iPriceLevel) == null){
				if(oMenuItem.getChildPrice(0) == null){
					return true;
				}
			}
		}	
		
		return false;
	}

	public boolean isOpenModifier() {
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if(oFuncMenuItem == null)
			return false;
		
		MenuItem oMenuItem = oFuncMenuItem.getMenuItem();
		int iPriceLevel = this.getCheckItem().getPriceLevel();
		if(oFuncMenuItem.getMenuItem().isModifier()) {
			if(oFuncMenuItem.getMenuItem().isAddUnitModifierPriceToUnitPriceModifierOperator() || oFuncMenuItem.getMenuItem().isAddUnitModifierPriceToTotalModifierOperator()){
				if(iPriceLevel == 0){
					if(oMenuItem.getModifierPrice(0) == null){
						return true;
					}
				}else
				if(oMenuItem.getModifierPrice(iPriceLevel) == null){
					if(oMenuItem.getModifierPrice(0) == null){
						return true;
					}
				}
			}else if(oFuncMenuItem.getMenuItem().isMultipleRateModifierOperator()){
				if(iPriceLevel == 0){
					if(oMenuItem.getModifierRate(0) == null){
						return true;
					}
				}else
				if(oMenuItem.getModifierRate(iPriceLevel) == null){
					if(oMenuItem.getModifierRate(0) == null){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean isForceModifier(){
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if(oFuncMenuItem == null)
			return false;
		
		if(oFuncMenuItem.getMenuItem().isForceModifierSelectModifierMethod())
			return true;
		else
			return false;
	}
	
	public boolean isSetMenu(){
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if(oFuncMenuItem == null)
			return false;
		
		if (oFuncMenuItem.getMenuItem().getChildCount() > 0)
			return true;
		else
			return false;
	}
	
	public boolean isTakeoutItem(){
		return m_oCheckItem.isTakeoutOrderingType();
	}
	
	public boolean isPendingItem() {
		if(m_oCheckItem.getPending().equals(PosCheckItem.PENDING_PENDING_ITEM))
			return true;
		else
			return false;
	}
	
	//get menu item
	public MenuItem getMenuItem() {
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if(oFuncMenuItem == null)
			return null;
		
		return oFuncMenuItem.getMenuItem();
	}
	
	public int getMenuItemId(){
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if(oFuncMenuItem == null)
			return 0;
		
		return oFuncMenuItem.getMenuItem().getItemId();
	}
	
	// Get default item name
	public String getMenuItemName(int iLangIndex) {
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if(oFuncMenuItem == null)
			return "";
		
		return oFuncMenuItem.getMenuItem().getName(iLangIndex);
	}
	
	public boolean hasModifierSetup(){
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if(oFuncMenuItem == null)
			return false;
		
		for(int i=0; i<10; i++){
			if(oFuncMenuItem.getMenuItem().getSelectModifierMenuId(i) > 0)
				return true;
		}
		
		// No setup
		return false;
	}
	
	public boolean hasModifierMenuList() {
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if(oFuncMenuItem == null)
			return false;
		
		if(oFuncMenuItem.getMenuItem().getModifierMenuList().size() > 0)
			return true;
		else
			return false;
	}
	
	// Get setup modifier menu list for this item
	public List<MenuMenu> getModifierMenuList() {
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if(oFuncMenuItem == null)
			return null;
		
		if(oFuncMenuItem.getMenuItem().getModifierMenuList().size() > 0)
			return oFuncMenuItem.getMenuItem().getModifierMenuList();
		else{
			// Modifier list is not loaded before
			for(int i=0; i<10; i++){
				if(oFuncMenuItem.getMenuItem().getSelectModifierMenuId(i) > 0){
					// Get the menu
					MenuMenu oMenuMenu = AppGlobal.g_oFuncMenu.get().getMenuAndContentById(oFuncMenuItem.getMenuItem().getSelectModifierMenuId(i));
					if(oMenuMenu != null)
						oFuncMenuItem.getMenuItem().getModifierMenuList().add(oMenuMenu);
				}
			}
			return oFuncMenuItem.getMenuItem().getModifierMenuList();
		}
	}
	
	// Set the check as stock delivery invoice
	public void setStockDeliveryInvoice(boolean bStockDeliveryInvoice) {
		this.m_bStockDeliveryInvoice = bStockDeliveryInvoice;
	}
	
	// Check whether check is stock delivery invoice
	public boolean isStockDeliveryInvoice() {
		return this.m_bStockDeliveryInvoice;
	}
	
	// Get whether the item needs kitchen slip
	public boolean getNoKitchenSlip() {
		return this.m_bNoKitchenSlip;
	}
	
	// Set whether the item needs kitchen slip
	public void setNoKitchenSlip(boolean bNoKitchenSlip) {
		this.m_bNoKitchenSlip = bNoKitchenSlip;
	}
	
	// Check whether item needs kitchen slip
	public boolean isNoKitchenSlip() {
		return this.m_bNoKitchenSlip;
	}
	
	// Item net total : citm_round_total + citm_pre_disc(-ve) + citm_mid_disc(-ve) + citm_post_disc(-ve)
	public BigDecimal getNetItemTotal() {
		BigDecimal dItemTotal = m_oCheckItem.getRoundTotal();
		for(PosCheckDiscount oItemDiscount: m_oItemDiscountList) {
			dItemTotal = dItemTotal.add(oItemDiscount.getRoundTotal());
		}
		
		return dItemTotal;
	}
	
	// Get item description by index (name may not equal to that in menu item, e.g. open description)
	public String getItemDescriptionByIndex(int iLangIndex) {
		String sDescription = "";
		sDescription = m_oCheckItem.getName(iLangIndex);
		
		return sDescription;
	}
	
	public String getItemShortDescriptionByIndex(int iLangIndex) {
		String sDescription = "";
		if (m_oCheckItem.getShortName(iLangIndex).isEmpty())
			sDescription = m_oCheckItem.getName(iLangIndex);
		else
			sDescription = m_oCheckItem.getShortName(iLangIndex);
		
		return sDescription;
	}
	
	public String getBilingualItemDescriptionByIndex(int iLangIndex) {
		String sDescription;
		int iBilingualLangIndex = AppGlobal.g_oFuncOutlet.get().getBilingualLangIndexByeLangIndex(iLangIndex); 
		
		if(m_oCheckItem.getShortName(iLangIndex).isEmpty())
			sDescription = m_oCheckItem.getName(iLangIndex);
		else
			sDescription = m_oCheckItem.getShortName(iLangIndex);
		
		if (iBilingualLangIndex > 0 && iLangIndex != iBilingualLangIndex) {
			if(m_oCheckItem.getShortName(iBilingualLangIndex).isEmpty())
				sDescription += "\n" + m_oCheckItem.getName(iBilingualLangIndex);
			else
				sDescription += "\n" + m_oCheckItem.getShortName(iBilingualLangIndex);
		}
		
		return sDescription;
	}
	
	// Get order item user name
	public String getOrderItemUserName(int iLangIndex) {
		// Retrieve order user
		UserUser oOrderedUser = new UserUser();
		if(oOrderedUser.readByUserId(m_oCheckItem.getOrderUserId()))
			return oOrderedUser.getFirstName(iLangIndex)+" "+oOrderedUser.getLastName(iLangIndex);
		else
			return "";
	}
	
	// Get applied item discount list
	public ArrayList<PosCheckDiscount> getItemDiscountList(){
		return m_oItemDiscountList;
	}
	
	// Check whether has corresponding check discount item by check discount index
	public boolean isCheckDiscountItemExist(int iCheckDiscountIndex) {
		return m_oCheckDiscountItemList.containsKey(iCheckDiscountIndex);
	}
	
	// Get check discount item by check discount index 
	public PosCheckDiscountItem getCheckDiscountItemList(int iCheckDiscountIndex) {
		return m_oCheckDiscountItemList.get(iCheckDiscountIndex);
	}
	
	// Get applied item discount total of item
	public BigDecimal getAppliedItemDiscountTotal() {
		BigDecimal dAppliedItemDiscTotal = BigDecimal.ZERO;
		
		for(PosCheckDiscount oItemCheckDiscount:m_oItemDiscountList) {
			for(PosCheckDiscountItem oItemCheckDiscountItem:oItemCheckDiscount.getCheckDiscountItemList())
				dAppliedItemDiscTotal = dAppliedItemDiscTotal.add(oItemCheckDiscountItem.getTotal());
		}
		
		return dAppliedItemDiscTotal;
	}
	
	// Get applied check discount total of item
	public BigDecimal getAppliedCheckDiscountTotal() {
		BigDecimal dAppliedCheckDiscTotal = BigDecimal.ZERO;
		
		for(PosCheckDiscountItem oCheckDiscountItem:m_oCheckDiscountItemList.values()) 
			dAppliedCheckDiscTotal = dAppliedCheckDiscTotal.add(oCheckDiscountItem.getTotal());
		
		return dAppliedCheckDiscTotal;
	}
	
	// Get applied modifiers list
	public ArrayList<FuncCheckItem> getModifierList() {
		return m_oModifierList;
	}
	
	// Get current child item list
	public ArrayList<FuncCheckItem> getChildItemList() {
		return m_oChildItemList;
	}
	
	// Set current child item list
	public void setChildItemList(ArrayList<FuncCheckItem> oNewChildItemList) {
		m_oChildItemList = oNewChildItemList;
		m_oCheckItem.setChildCount(oNewChildItemList.size());
	}
	
	// Get existing extra info list
	public ArrayList<PosCheckExtraInfo> getExtraInfoList() {
		return m_oExtraInfoList;
	}
	
	// Get current mix and match item list
	public ArrayList<FuncCheckItem> getMixAndMatchItemList() {
		return m_oMixAndMatchChildItemList;
	}
	
	public void clearupModifierList() {
		this.m_oModifierList.clear();
	}
	
	public void clearupDiscountList() {
		this.m_oItemDiscountList.clear();
	}
	
	public void cleanupExtraInfoList() {
		this.m_oExtraInfoList.clear();
	}
	
	public void cleanupMixAndMatchItemList() {
		this.m_oMixAndMatchChildItemList.clear();
	}
	
	//get parent func check item
	public FuncCheckItem getParentFuncCheckItem() {
		return m_oParentFuncCheckItem;
	}
	
	//set parent func check item
	public void setParentFuncCheckItem(FuncCheckItem oParentFuncCheckItem) {
		m_oParentFuncCheckItem = oParentFuncCheckItem;
	}
	
	//get Parent tab index
	public int getParentTabIndex() {
		return m_iParentTabIndex;
	}
	
	//set Parent tab index
	public void setParentTabIndex(int iIndex) {
		m_iParentTabIndex = iIndex;
	}
	
	////////////////////////////////////////////////////////////////////////////
	// Stock Delivery Control Function
	//get receiving quantity
	public PosStockDeliveryInvoiceItem getStockDeliveryInvoiceItem() {
		return this.m_oPosStockDeliveryInvoiceItem;
	}
	
	//set receiving quantity
	public void setStockDeliveryInvoiceItem(PosStockDeliveryInvoiceItem oPosStockDeliveryInvoiceItem) {
		m_bStockDeliveryInvoice = true;
		this.m_oPosStockDeliveryInvoiceItem = oPosStockDeliveryInvoiceItem;
	}

	////////////////////////////////////////////////////////////////////////////
	// Mix And Match Rule Function
	// get mix and match rule
	public int getMixAndMatchRuleId() {
		return this.m_iMixAndMatchRuleId;
	}
	
	// set mix and match rule
	public void setMixAndMatchRuleId(int iMixAndMatchRuleId) {
		this.m_iMixAndMatchRuleId = iMixAndMatchRuleId;
	}
	
	//form PosCheckItem link list
	public PosCheckItem constructPosCheckItemForSaving() {
		
		//form the modifier list
		if(this.hasModifier()) {
			List<PosCheckItem> oModifierItemList = new ArrayList<PosCheckItem>();
			
			for(FuncCheckItem oModiFuncCheckItem:this.getModifierList()) { 
				oModiFuncCheckItem.getCheckItem().setSeatNo(this.getCheckItem().getSeatNo());
				oModifierItemList.add(oModiFuncCheckItem.getCheckItem());
			}
			this.getCheckItem().setModifierList(oModifierItemList);
		}
		
		//form the applied check discount item list
		this.getCheckItem().cleanCheckDiscountItemList();
		if(hasAppliedCheckDiscount()) {
			List<PosCheckDiscountItem> oCheckDiscountItemList = new ArrayList<PosCheckDiscountItem>();
			
			for(Map.Entry<Integer, PosCheckDiscountItem> entry:m_oCheckDiscountItemList.entrySet()) {
				int iCheckDiscountIndex = entry.getKey();
				m_oCheckDiscountItemList.get(iCheckDiscountIndex).setCheckDiscountIndex(iCheckDiscountIndex);
				oCheckDiscountItemList.add(m_oCheckDiscountItemList.get(iCheckDiscountIndex));
			}
			this.getCheckItem().setCheckDiscountItemList(oCheckDiscountItemList);
		}
		
		//form the item discount list
		if(hasItemDiscount(false)) {
			List<PosCheckDiscount> oItemDiscountList = new ArrayList<PosCheckDiscount>();
			
			for(PosCheckDiscount oCheckDiscount:m_oItemDiscountList) 
				oItemDiscountList.add(oCheckDiscount);
			this.getCheckItem().setItemDiscountList(oItemDiscountList);
		}
		
		//from the extra info list
		if(hasExtraInfo()) {
			List<PosCheckExtraInfo> oItemExtraInfoList = new ArrayList<PosCheckExtraInfo>();
			
			for(PosCheckExtraInfo oCheckExtraInfo:m_oExtraInfoList)
				oItemExtraInfoList.add(oCheckExtraInfo);
			this.getCheckItem().setExtraInfoList(oItemExtraInfoList);
		}
		
		//form the child list
		if(this.getCheckItem().getChildCount() > 0) {
			List<PosCheckItem> oChildItemList = new ArrayList<PosCheckItem>();
			
			for(FuncCheckItem oChildFuncCheckItem:this.getChildItemList()) {
				oChildItemList.add(oChildFuncCheckItem.constructPosCheckItemForSaving());
			}
			this.getCheckItem().setChildItemList(oChildItemList);
		}
		
		return this.getCheckItem();
	}
	
	// Update several information (e.g. outlet id, shop id, ...) to check item records
	public void updateMultipleCheckItemInfoForNewItem(int iBusinessDayId, int iBusinessPeriodId, int iChksId, int iShopId, int iOutletId, String sOrderTime, DateTime oOrderDateTime) {
		PosCheckItem oCheckItem = m_oCheckItem;
		m_sErrorMessage = "";
		
		if (oCheckItem.getCitmId() == 0) {
			// Update current item
			oCheckItem.setBusinessDayId(iBusinessDayId);
			oCheckItem.setBusinessPeriodId(iBusinessPeriodId);
			oCheckItem.setCheckId(iChksId);
			oCheckItem.setShopId(iShopId);
			oCheckItem.setOutletId(iOutletId);
			oCheckItem.setOrderUserId(AppGlobal.g_oFuncUser.get().getUserId());
			oCheckItem.setOrderStationId(AppGlobal.g_oFuncStation.get().getStationId());
			oCheckItem.setOrderTime(sOrderTime);
			oCheckItem.setOrderLocTime(oOrderDateTime);
			
			// Update item discount period ID
			ArrayList<PosCheckDiscount> oItemDiscountHashMap = this.getItemDiscountList();
			if(oItemDiscountHashMap != null && oItemDiscountHashMap.size() > 0) {
				for(PosCheckDiscount oCheckDiscount:oItemDiscountHashMap) 
					oCheckDiscount.setBperId(iBusinessPeriodId);
			}
			
			// Update modifier
			for(FuncCheckItem oFuncCheckItem:m_oModifierList){
				oFuncCheckItem.updateMultipleCheckItemInfoForNewItem(iBusinessDayId, iBusinessPeriodId, iChksId, iShopId, iOutletId, sOrderTime, oOrderDateTime);
			}
			
			// Update child item
			for(FuncCheckItem oFuncCheckItem:m_oChildItemList){
				oFuncCheckItem.updateMultipleCheckItemInfoForNewItem(iBusinessDayId, iBusinessPeriodId, iChksId, iShopId, iOutletId, sOrderTime, oOrderDateTime);
			}
		}
	}
	
	// Retrieve item from menu
	public boolean retieveItemFromMenu(int iItemId, BigDecimal dQty, BigDecimal dBaseQty, FuncCheckItem oParentFuncCheckItem, boolean bModifier, boolean bChildItem, int iPriceLevel){
		m_sErrorMessage = "";
		
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(iItemId);
		if(oFuncMenuItem == null){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("item_cannot_be_found");
			return false;
		}
				
		// Assign value to m_oCheckItem
		this.assignItemFromMenuItem(oFuncMenuItem.getMenuItem(), dQty, dBaseQty, oParentFuncCheckItem, bModifier, bChildItem, iPriceLevel);
		
		return true;
	}
		
	//assign check item object from MenuItem object
	protected void assignItemFromMenuItem(MenuItem oMenuItem, BigDecimal dQty, BigDecimal dBaseQty, FuncCheckItem oParentFuncCheckItem, boolean bModifier, boolean bChildItem, int iPriceLevel) {
		int i=0, iParentCitmId = 0;

		// Set Parent Func check item
		m_oParentFuncCheckItem = oParentFuncCheckItem;
		
		// Assign value to m_oCheckItem
		m_oCheckItem.setItemId(oMenuItem.getItemId());
		m_oCheckItem.setCode(oMenuItem.getCode());
		for(i=1; i<=5; i++)
			m_oCheckItem.setName(i, oMenuItem.getName(i));
		for(i=1; i<=5; i++)
			m_oCheckItem.setShortName(i, oMenuItem.getShortName(i));
		if(oParentFuncCheckItem != null)
			iParentCitmId = oParentFuncCheckItem.getCheckItem().getCitmId();
		m_oCheckItem.setParentItemId(iParentCitmId);
		if(bChildItem)
			m_oCheckItem.setRole(PosCheckItem.ROLE_SET_MENU_CHILD_ITEM);
		else
		if(bModifier)
			m_oCheckItem.setRole(PosCheckItem.ROLE_MODIFIER_ITEM);
		m_oCheckItem.setChildCount(0);
		m_oCheckItem.setModifierCount(0);
		m_oCheckItem.setSeatNo(0);
		m_oCheckItem.setSeq(0);
		m_oCheckItem.setOriginalPriceLevel(iPriceLevel);
		m_oCheckItem.setPriceLevel(iPriceLevel);
		if(bChildItem){
			// Child Item
			// Set Basic Quantity
			m_oCheckItem.setBaseQty(dBaseQty);
			
			// Set Quantity
			m_oCheckItem.setQty(m_oCheckItem.getBaseQty().multiply(m_oParentFuncCheckItem.getCheckItem().getQty()));
			
			// Set Price
			this.setNewChildItemPrice(iPriceLevel, oMenuItem);
			
			// Set get revenue
			m_oCheckItem.setGetRevenue(oMenuItem.getChildGetRevenue());
		}else
		if(bModifier){
			// Modifier
			// Set Basic Quantity
			m_oCheckItem.setBaseQty(new BigDecimal("1.0"));
			
			// Set Quantity
			m_oCheckItem.setQty(m_oCheckItem.getBaseQty().multiply(m_oParentFuncCheckItem.getCheckItem().getQty()));
			
			// The modifier price is calculate with other modifier later
			
			// Set get revenue
			m_oCheckItem.setGetRevenue(oMenuItem.getModifierGetRevenue());
		}else{
			// Normal Item
			// Set Basic Quantity
			m_oCheckItem.setBaseQty(new BigDecimal("1.0"));
			
			// Set Quantity
			m_oCheckItem.setQty(dQty);
			
			// Set Price
			this.setNewBasicItemPrice(iPriceLevel, oMenuItem);
		}
		
		m_oCheckItem.setRoundTotal(m_oCheckItem.getTotal());
		m_oCheckItem.setRoundAmount(BigDecimal.ZERO);
		for(i=1; i<=5; i++)
			m_oCheckItem.setSc(i, BigDecimal.ZERO);
		for(i=1; i<=25; i++)
			m_oCheckItem.setTax(i, BigDecimal.ZERO);
		m_oCheckItem.setPreDisc(BigDecimal.ZERO);
		m_oCheckItem.setMidDisc(BigDecimal.ZERO);
		m_oCheckItem.setPosDisc(BigDecimal.ZERO);
		m_oCheckItem.setRevenue(BigDecimal.ZERO);
		m_oCheckItem.setCarryRevenue(BigDecimal.ZERO);
		m_oCheckItem.setUnitCost(oMenuItem.getCost());
		for(i=1; i<=10; i++)
			m_oCheckItem.setPrintQueueWithIndex(i, oMenuItem.getPrintQueue(i));
		if(bChildItem)
			m_oCheckItem.setNoPrint(oMenuItem.getNoPrintChild());
		else
		if(bModifier)
			m_oCheckItem.setNoPrint(oMenuItem.getNoPrintModifier());
		else
			m_oCheckItem.setNoPrint(oMenuItem.getNoPrintItem());
		for(i=1; i<=5; i++)
			m_oCheckItem.setChargeSc(i, oMenuItem.getChargeSc(i-1));
		for(i=1; i<=25; i++)
			m_oCheckItem.setChargeTax(i, oMenuItem.getChargeTax(i-1));
		m_oCheckItem.setHide(oMenuItem.getHide());
		m_oCheckItem.setCategoryId(oMenuItem.getCategoryId());
		m_oCheckItem.setDepartmentId(oMenuItem.getDeparmentId());
		m_oCheckItem.setCourseId(oMenuItem.getCourseId());
		m_oCheckItem.setServingStatus(PosCheckItem.SERVING_STATUS_ORDERED);
		m_oCheckItem.setPending(PosCheckItem.PENDING_NORMAL_ITEM);
		m_oCheckItem.setOrderingType(PosCheckItem.ORDERING_TYPE_FINE_DINING);
		m_oCheckItem.setRoundStatus(PosCheckItem.ROUND_STATUS_SAVED_BEFORE_ROUNDING);
		m_oCheckItem.setStatus(PosCheckItem.STATUS_NORMAL);
		
	}
	
	private void setNewBasicItemPrice(int iPriceLevel, MenuItem oMenuItem){
		// Set Basic Price
		// Basic price = 0 for normal item
		m_oCheckItem.setBasicPrice(BigDecimal.ZERO);
		
		// Set Original Price
		if(oMenuItem.getBasicPrice(iPriceLevel) != null)
			m_oCheckItem.setOriginalPrice(oMenuItem.getBasicPrice(iPriceLevel));
		else if(iPriceLevel != 0 && oMenuItem.getBasicPrice(0) != null)
			m_oCheckItem.setOriginalPrice(oMenuItem.getBasicPrice(0));
		else
			m_oCheckItem.setOriginalPrice(BigDecimal.ZERO);
		
		// Set Price
		m_oCheckItem.setPrice(m_oCheckItem.getOriginalPrice());
	}
	
	private void setNewChildItemPrice(int iPriceLevel, MenuItem oMenuItem){
		// Set Basic Price
		if(oMenuItem.getChildGetRevenue().equals(MenuItem.CHILD_GET_REVENUE_SPILT_BASIC_PRICE)){
			if(oMenuItem.getBasicPrice(iPriceLevel) != null)
				m_oCheckItem.setBasicPrice(oMenuItem.getBasicPrice(iPriceLevel));
			else if(iPriceLevel != 0 && oMenuItem.getBasicPrice(0) != null)
				m_oCheckItem.setBasicPrice(oMenuItem.getBasicPrice(0));
			else
				m_oCheckItem.setBasicPrice(BigDecimal.ZERO);
		}else
		if(oMenuItem.getChildGetRevenue().equals(MenuItem.CHILD_GET_REVENUE_SPILT_CHILD_PRICE)){
			m_oCheckItem.setBasicPrice(BigDecimal.ZERO);
		}else{
			m_oCheckItem.setBasicPrice(BigDecimal.ZERO);
		}
		
		// Set Original Price
		if(oMenuItem.getChildPrice(iPriceLevel) != null)
			m_oCheckItem.setOriginalPrice(oMenuItem.getChildPrice(iPriceLevel));
		else if(iPriceLevel != 0 && oMenuItem.getChildPrice(0) != null)
			m_oCheckItem.setOriginalPrice(oMenuItem.getChildPrice(0));
		else
			m_oCheckItem.setOriginalPrice(BigDecimal.ZERO);
		
		// Set Price
		m_oCheckItem.setPrice(m_oCheckItem.getOriginalPrice());
	}
	
	public void setAllModifiersPrice(boolean bProcessOnOldItem){
		
		// Process item's child item
		for (FuncCheckItem oFuncChildItem: m_oChildItemList) {
			oFuncChildItem.setAllModifiersPrice(bProcessOnOldItem);
		}
		
		BigDecimal dRemainItemTotal = this.getCheckItem().getPrice().multiply(this.getCheckItem().getQty());
		for(FuncCheckItem oFuncModiCheckItem:m_oModifierList) {
			int iPriceLevel = oFuncModiCheckItem.getCheckItem().getPriceLevel();
			MenuItem oMenuItem = oFuncModiCheckItem.getMenuItem();
			PosCheckItem oCheckItem = oFuncModiCheckItem.getCheckItem(); 
			
			// Set Basic Price
			if((bProcessOnOldItem || !isOldItem()) && !oFuncModiCheckItem.getCheckItem().isDeleted()) {
				if(oMenuItem.getChildGetRevenue().equals(MenuItem.MODIFIER_GET_REVENUE_SPILT_BASIC_PRICE)){
					if(oMenuItem.getBasicPrice(iPriceLevel) != null) 
						oCheckItem.setBasicPrice(oMenuItem.getBasicPrice(iPriceLevel));
					else if(iPriceLevel != 0 && oMenuItem.getBasicPrice(0) != null) {
						oCheckItem.setBasicPrice(oMenuItem.getBasicPrice(0));
					}else 
						oCheckItem.setBasicPrice(BigDecimal.ZERO);
				}else
				if(oMenuItem.getChildGetRevenue().equals(MenuItem.MODIFIER_GET_REVENUE_SPILT_MODIFIER_PRICE)){
					oCheckItem.setBasicPrice(BigDecimal.ZERO);
				}else{
					oCheckItem.setBasicPrice(BigDecimal.ZERO);
				}
			}
			
			// Set Original Price
			if((bProcessOnOldItem || !isOldItem()) && !oFuncModiCheckItem.getCheckItem().isDeleted()) {
				if(oFuncModiCheckItem.isOpenModifier()) {
					if(oFuncModiCheckItem.getOpenModiPriceRate() != null && (oMenuItem.isAddUnitModifierPriceToUnitPriceModifierOperator() || oMenuItem.isAddUnitModifierPriceToTotalModifierOperator()))
						oCheckItem.setOriginalPrice(oFuncModiCheckItem.getOpenModiPriceRate());
					else
						oCheckItem.setOriginalPrice(BigDecimal.ZERO);
				}else {
					if(oMenuItem.getModifierPrice(iPriceLevel) != null)
						oCheckItem.setOriginalPrice(oMenuItem.getModifierPrice(iPriceLevel));
					else if(iPriceLevel != 0 && oMenuItem.getModifierPrice(0) != null)
						oCheckItem.setOriginalPrice(oMenuItem.getModifierPrice(0));
					else
						oCheckItem.setOriginalPrice(BigDecimal.ZERO);
					oFuncModiCheckItem.setOpenModiPriceRate(oCheckItem.getOriginalPrice());
				}
			}
			
			// Set Price
			if(oMenuItem.isAddUnitModifierPriceToUnitPriceModifierOperator()){
				oCheckItem.setPrice(oCheckItem.getOriginalPrice());
			}else
			if(oMenuItem.isAddUnitModifierPriceToTotalModifierOperator()){
				// Reverse to calculate the price
				BigDecimal dNewPrice = BigDecimal.ZERO;
				if(oCheckItem.getQty().compareTo(BigDecimal.ZERO) != 0)
					dNewPrice = oFuncModiCheckItem.getOpenModiPriceRate().divide(oCheckItem.getQty(), 10, RoundingMode.HALF_UP);
				oCheckItem.setPrice(dNewPrice);
				oCheckItem.setOriginalPrice(dNewPrice);
			}else
			if(oMenuItem.isMultipleRateModifierOperator()){
				// Rate
				if((bProcessOnOldItem || !isOldItem()) && !oFuncModiCheckItem.getCheckItem().isDeleted()) {
					BigDecimal dRate = new BigDecimal("1.0");
					if(oFuncModiCheckItem.isOpenModifier()) {
						if(oFuncModiCheckItem.getOpenModiPriceRate() != null)
							dRate = oFuncModiCheckItem.getOpenModiPriceRate();
					}else {
						if(oMenuItem.getModifierRate(iPriceLevel) != null)
							dRate = oMenuItem.getModifierRate(iPriceLevel);
						else if(iPriceLevel != 0 && oMenuItem.getModifierRate(0) != null)
							dRate = oMenuItem.getModifierRate(0);
						else
							dRate = new BigDecimal("1.0");
					}
					
					BigDecimal dParentTotal = oFuncModiCheckItem.getParentFuncCheckItem().getCheckItem().getPrice().multiply(oFuncModiCheckItem.getParentFuncCheckItem().getCheckItem().getQty());
					if(oFuncModiCheckItem.getParentFuncCheckItem().getCheckItem().getQty().compareTo(BigDecimal.ZERO) > 0) {
						oCheckItem.setPrice((dParentTotal.divide(oFuncModiCheckItem.getParentFuncCheckItem().getCheckItem().getQty(), 10, RoundingMode.HALF_UP)).multiply((dRate.subtract(new BigDecimal("1.0")))));
						oCheckItem.setOriginalPrice(oCheckItem.getPrice());
					}else {
						oCheckItem.setPrice(BigDecimal.ZERO);
						oCheckItem.setOriginalPrice(BigDecimal.ZERO);
					}
				}
			}

			// Prevent modifier cause negative item total for positive price item
			// Prevent modifier cause position item total for negative price item
			BigDecimal dModifierTotal = oCheckItem.getPrice().multiply(oCheckItem.getQty());
			if(dRemainItemTotal.compareTo(BigDecimal.ZERO) >= 0){
				// Positive price item
				if(dModifierTotal.compareTo(BigDecimal.ZERO) < 0){
					if((dRemainItemTotal.add(dModifierTotal)).compareTo(BigDecimal.ZERO) < 0){
						BigDecimal dNewPrice = dRemainItemTotal.divide(oCheckItem.getQty(), 10, RoundingMode.HALF_UP);
						dNewPrice = dNewPrice.negate();
						oCheckItem.setPrice(dNewPrice);
						oCheckItem.setOriginalPrice(dNewPrice);
					}
				}
			}else{
				// Negative price item
				if(dModifierTotal.compareTo(BigDecimal.ZERO) > 0){
					if((dRemainItemTotal.add(dModifierTotal)).compareTo(BigDecimal.ZERO) > 0){
						BigDecimal dNewPrice = dRemainItemTotal.divide(oCheckItem.getQty(), 10, RoundingMode.HALF_UP);
						dNewPrice = dNewPrice.negate();
						oCheckItem.setPrice(dNewPrice);
						oCheckItem.setOriginalPrice(dNewPrice);
					}
				}
			}
			dRemainItemTotal = dRemainItemTotal.add(oCheckItem.getPrice().multiply(oCheckItem.getQty()));
		}
	}
	
	public void createJSONArrayForItemUpdate(JSONArray itemJSONArray){
		JSONObject itemJsonObject = new JSONObject();
		
		try {
			itemJsonObject.put("dplu_menu_id", "0");
			itemJsonObject.put("dplu_item_id", m_oCheckItem.getItemId() + "");
			itemJsonObject.put("dplu_id", "0");
			itemJsonObject.put("dplu_type", PosDisplayPanelLookup.TYPE_HOT_ITEM);
		} catch (JSONException e) {
			AppGlobal.stack2Log(e);
		}
		
		itemJSONArray.put(itemJsonObject);
		
		for(int i = 0; i < this.m_oChildItemList.size(); i++) {
			this.m_oChildItemList.get(i).createJSONArrayForItemUpdate(itemJSONArray);
		}
		
		for(int i = 0; i < this.m_oModifierList.size(); i++) {
			this.m_oModifierList.get(i).createJSONArrayForItemUpdate(itemJSONArray);
		}
	}
	
	// Update all check item from menu item array
	public boolean updateFromUpdateMenuItemList(){
		
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if(oFuncMenuItem == null){
			m_sErrorMessage = this.getItemShortDescriptionByIndex(AppGlobal.g_oCurrentLangIndex.get()) + " " + AppGlobal.g_oLang.get()._("is_deleted_from_system");
			return false;
		}
				
		this.updateCheckItemFromMenuItem(oFuncMenuItem);
		
		for(int i = 0; i < this.m_oChildItemList.size(); i++) {
			if(this.m_oChildItemList.get(i).updateFromUpdateMenuItemList() == false){
				m_sErrorMessage = this.m_oChildItemList.get(i).getLastErrorMessage();
				return false;
			}
		}
		
		for(int i = 0; i < this.m_oModifierList.size(); i++) {
			if(this.m_oModifierList.get(i).updateFromUpdateMenuItemList() == false){
				m_sErrorMessage = this.m_oChildItemList.get(i).getLastErrorMessage();
				return false;
			}
		}
		
		if(isModifierItem()){
			// Modifier item ==> Re-calculate the parent item's all modifiers' price
			m_oParentFuncCheckItem.setAllModifiersPrice(false);
		}else{
			// Normal item/child item ==> Re-calculate all my modifiers' price
			this.setAllModifiersPrice(false);
		}
		
		return true;
	}
	
	/*
	 * Cannot update variables which have been modified in operation
	 * e.g. icouId, orderingType
	 */
	// Update check item by update menu item
	public void updateCheckItemFromMenuItem(FuncMenuItem oFuncMenuItem){
		int i=0;

		MenuItem oMenuItem = oFuncMenuItem.getMenuItem();
		
		// Assign value to m_oCheckItem
		m_oCheckItem.setCode(oMenuItem.getCode());
		
		// If open description, skip update description
		if(isOpenDescription() == false && isAppendOpenDescription() == false){
			for(i=1; i<=5; i++)
				m_oCheckItem.setName(i, oMenuItem.getName(i));
			for(i=1; i<=5; i++)
				m_oCheckItem.setShortName(i, oMenuItem.getShortName(i));
		}

		// Get original price leve and quantity
		int iPriceLevel = m_oCheckItem.getPriceLevel();
		BigDecimal dQty = m_oCheckItem.getQty();
		BigDecimal dBaseQty = m_oCheckItem.getBaseQty();
		
		// If open price, skip update price and total
		// If price <> original price, the price is changed by some operations (e.g. mix and match), skip update price and total
		if(isOpenPrice() == false && (m_oCheckItem.getPrice().compareTo(m_oCheckItem.getOriginalPrice()) == 0)){
			if(isSetMenuItem()){
				// Child Item
				// Set Basic Quantity
				m_oCheckItem.setBaseQty(dBaseQty);
				
				// Set Quantity
				m_oCheckItem.setQty(m_oCheckItem.getBaseQty().multiply(m_oParentFuncCheckItem.getCheckItem().getQty()));
				
				// Set Price
				this.setNewChildItemPrice(iPriceLevel, oMenuItem);
				
				// Set get revenue
				m_oCheckItem.setGetRevenue(oMenuItem.getChildGetRevenue());
			}else
			if(m_oCheckItem.isModifierItem()){
				// Modifier
				// Set Basic Quantity
				m_oCheckItem.setBaseQty(new BigDecimal("1.0"));
				
				// Set Quantity
				m_oCheckItem.setQty(m_oCheckItem.getBaseQty().multiply(m_oParentFuncCheckItem.getCheckItem().getQty()));
				
				// The modifier price is calculate with other modifier later
				
				// Set get revenue
				m_oCheckItem.setGetRevenue(oMenuItem.getModifierGetRevenue());
			}else{
				// Normal Item
				// Set Basic Quantity
				m_oCheckItem.setBaseQty(new BigDecimal("1.0"));
				
				// Set Quantity
				m_oCheckItem.setQty(dQty);
				
				// Set Price
				this.setNewBasicItemPrice(iPriceLevel, oMenuItem);
			}
			
			m_oCheckItem.setRoundTotal(m_oCheckItem.getTotal());
			m_oCheckItem.setRoundAmount(BigDecimal.ZERO);
		}
		for(i=1; i<=5; i++)
			m_oCheckItem.setSc(i, BigDecimal.ZERO);
		for(i=1; i<=25; i++)
			m_oCheckItem.setTax(i, BigDecimal.ZERO);
		m_oCheckItem.setPreDisc(BigDecimal.ZERO);
		m_oCheckItem.setMidDisc(BigDecimal.ZERO);
		m_oCheckItem.setPosDisc(BigDecimal.ZERO);
		m_oCheckItem.setRevenue(BigDecimal.ZERO);
		m_oCheckItem.setCarryRevenue(BigDecimal.ZERO);
		m_oCheckItem.setUnitCost(oMenuItem.getCost());
		// *** Print queue override MUST perform after this
		for(i=1; i<=10; i++)
			m_oCheckItem.setPrintQueueWithIndex(i, oMenuItem.getPrintQueue(i));
		if(this.isSetMenuItem())
			m_oCheckItem.setNoPrint(oMenuItem.getNoPrintChild());
		else
		if(this.isModifierItem())
			m_oCheckItem.setNoPrint(oMenuItem.getNoPrintModifier());
		else
			m_oCheckItem.setNoPrint(oMenuItem.getNoPrintItem());
		
		m_oCheckItem.setHide(oMenuItem.getHide());
		m_oCheckItem.setCategoryId(oMenuItem.getCategoryId());
		m_oCheckItem.setDepartmentId(oMenuItem.getDeparmentId());
		m_oCheckItem.setServingStatus(PosCheckItem.SERVING_STATUS_ORDERED);
		m_oCheckItem.setRoundStatus(PosCheckItem.ROUND_STATUS_SAVED_BEFORE_ROUNDING);
		m_oCheckItem.setStatus(PosCheckItem.STATUS_NORMAL);
	}
	
	public boolean changeQty(String sTable, String sTableExtension, BigDecimal dOriQty, BigDecimal dNewQty){
		m_sErrorMessage = "";
	
		if (dNewQty.compareTo(BigDecimal.ZERO) <= 0) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("this_function_does_not_allow_zero_item_quantity");
			return false;
		}
		
		if (dNewQty.compareTo(dOriQty) == 0) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("no_change_in_item_quantity");
			return false;
		}
		
		if(m_oCheckItem.isSetMenuChildItem() || m_oCheckItem.isModifierItem()) 
			m_oCheckItem.setQty(m_oCheckItem.getBaseQty().multiply(dNewQty));
		else
			m_oCheckItem.setQty(dNewQty);
		
		if(hasModifier()) {
			// Pre-checking if the modifier is missing in menu
			for(FuncCheckItem oFuncModiCheckItem:m_oModifierList) {
				if(oFuncModiCheckItem.getMenuItem() == null){
					m_sErrorMessage = AppGlobal.g_oLang.get()._("items_modifier_is_missing_in_menu_setup")+"\n"+
										AppGlobal.g_oLang.get()._("cannot_change_the_quantity_for_the_item");
					
					// Change back the quantity
					m_oCheckItem.setQty(dOriQty);
					
					return false;
				}
			}

			for(FuncCheckItem oFuncModiCheckItem:m_oModifierList) {
				oFuncModiCheckItem.getCheckItem().setQty(oFuncModiCheckItem.getCheckItem().getBaseQty().multiply(m_oCheckItem.getQty()));
			}
			
			// Re-calculate all my modifiers' price
			this.setAllModifiersPrice(false);
		}
		
		String sOriQty = "";
		String sNewQty = "";
		
		sOriQty = dOriQty.stripTrailingZeros().toPlainString();
		sNewQty = dNewQty.stripTrailingZeros().toPlainString();
		if(m_oCheckItem.getCitmId() > 0){
			m_oCheckItem.setModified(true);
		}
		else {
			//Add "change_quantity" log to log list
			addActionLog(AppGlobal.FUNC_LIST.change_quantity_last.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), m_oCheckItem.getCheckId(), 0, 0, 0, 0, sOriQty+" -> "+sNewQty);
		}
		
		return true;
	}
	
	public void processPrintChangeQtyActionSlip(String sTable, String sTableExtension, String sOriQty, String sNewQty){
		if(!m_oCheckItem.isSetMenuChildItem()) {
			JSONObject oHeaderJSONObject = new JSONObject(), oInfoJSONObject = new JSONObject(), oTempJSONObject = null;
			JSONArray oTempJSONArray = null;
			ArrayList<Integer> oItemIds = null;
			try {
				//form the header
				oHeaderJSONObject.put("header", "Change Quantity");
				oTempJSONArray = new JSONArray();
				oTempJSONObject = new JSONObject();
				
				oTempJSONObject.put("message", sOriQty+" -> "+sNewQty);
				oTempJSONArray.put(oTempJSONObject);
				oHeaderJSONObject.put("messages", oTempJSONArray);
				
				//form the information of slip
				oInfoJSONObject.put("stationId", AppGlobal.g_oFuncStation.get().getStationId());
				oInfoJSONObject.put("userId", AppGlobal.g_oFuncUser.get().getUserId());
				oInfoJSONObject.put("userName", AppGlobal.g_oFuncUser.get().getUserName(AppGlobal.g_oCurrentLangIndex.get()));
				oInfoJSONObject.put("table", sTable + sTableExtension);
				
				if(hasChildItem()) {
					oItemIds = new ArrayList<Integer>();
					oItemIds.add(m_oCheckItem.getCitmId());
					for(FuncCheckItem oChildFuncCheckItem:m_oChildItemList) {
						if(oChildFuncCheckItem.isOldItem()) {
							oItemIds.add(oChildFuncCheckItem.getCheckItem().getCitmId());
						}
					}
				}
				
				// *****************************************************************
				// Create thread to print special slip
				AppThreadManager oAppThreadManager = new AppThreadManager();
				
				// Add the method to the thread manager
				// Thread 1 : Print special slip
				// Create parameter array
				Object[] oParameters = new Object[6];
				oParameters[0] = m_oCheckItem.getCheckId();
				oParameters[1] = "change_quantity";
				oParameters[2] = oHeaderJSONObject;
				oParameters[3] = oInfoJSONObject;
				oParameters[4] = oItemIds;
				oParameters[5] = AppGlobal.g_oCurrentLangIndex.get();
				oAppThreadManager.addThread(1, m_oCheckItem, "printSpecialSlip", oParameters);
				
				// Run the thread without wait
				oAppThreadManager.runThread();
			}catch(JSONException jsone) {
				AppGlobal.stack2Log(jsone);
			}
		}
		
		//Add "change_quantity" log to global action log list
		AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.change_quantity_last.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), m_oCheckItem.getCheckId(), 0, m_oCheckItem.getCitmId(), 0, 0, sOriQty+" -> "+sNewQty);
		AppGlobal.g_oActionLog.get().handleActionLog(false);
	}
	
	// Function to change item quantity without all checking
	public void internalChangeQty(BigDecimal dNewQty){
		m_oCheckItem.setQty(dNewQty);
		
		if(hasModifier()) {
			for(FuncCheckItem oFuncModiCheckItem:m_oModifierList) {
				oFuncModiCheckItem.getCheckItem().setQty(oFuncModiCheckItem.getCheckItem().getBaseQty().multiply(m_oCheckItem.getQty()));
			}
			
			// Re-calculate all my modifiers' price
			this.setAllModifiersPrice(false);
		}
	}
	
	public boolean voidItem(FuncCheck oFuncCheck, BigDecimal dRemoveQty, int iVoidCodeId, String sVoidReasonName, String sTable, String sTableExtension, ArrayList<Integer> oCheckDiscountItemIds){
		BigDecimal dOriginalQty = m_oCheckItem.getQty();
		DateTime oVoidDateTime = new DateTime();
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		m_sErrorMessage = "";
		
		if (dRemoveQty.compareTo(BigDecimal.ZERO) == 0)
			return true;
		
		if (dRemoveQty.compareTo(m_oCheckItem.getQty()) != 0 && m_oCheckItem.getPreDisc().compareTo(BigDecimal.ZERO) != 0) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("must_delete_the_maximum_quantity_for_item_with_discount");
			return false;
		}
		
		BigDecimal dNewQty = m_oCheckItem.getQty().subtract(dRemoveQty);
		if (dNewQty.compareTo(BigDecimal.ZERO) > 0){
			//Update the new quantity and total price
			m_oCheckItem.setQty(dNewQty);
			oFuncCheck.calcCheck();
		}

		//print special slip if it is old item
		if(this.isOldItem()) {
			//Add "normal_delete_item" to global action log
			String sLogRemark = "Remove Qty:"+dRemoveQty + ", VoidCodeId:" + iVoidCodeId + ", VoidReason:" + sVoidReasonName;
			AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.delete_item.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), oFuncCheck.getCheckId(), 0, m_oCheckItem.getCitmId(), 0, 0, sLogRemark);
			AppGlobal.g_oActionLog.get().handleActionLog(false);
		}
		
		//Delete the modifier
		if(this.hasModifier()){
			if(this.isOldItem() && dRemoveQty.compareTo(dOriginalQty) == 0) {
				for(FuncCheckItem oModifierCheckItem:m_oModifierList) {					
					if(oModifierCheckItem.isOldItem()) {
						PosCheckItem oModifierItem = oModifierCheckItem.getCheckItem();
						oModifierItem.setVoidLocalTime(oVoidDateTime);
						oModifierItem.setVoidTime(oFormatter.print(oVoidDateTime.withZone(DateTimeZone.UTC)));
						oModifierItem.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
						oModifierItem.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
						oModifierItem.setVoidReasonId(iVoidCodeId);
						oModifierItem.setStatus(PosCheckItem.STATUS_DELETED);
					}
				}
			} else{
				for(FuncCheckItem oFuncModiCheckItem:m_oModifierList) {
					oFuncModiCheckItem.getCheckItem().setQty(oFuncModiCheckItem.getCheckItem().getBaseQty().multiply(m_oCheckItem.getQty()));
				}
				
				// Re-calculate all my modifiers' price
				this.setAllModifiersPrice(false);
			}
		}
		
		// Delete child item
		if (this.isOldItem() && dRemoveQty.compareTo(dOriginalQty) == 0 && m_oCheckItem.getChildCount() > 0) {
			for (FuncCheckItem oSetCheckItem: m_oChildItemList) {				
				if (oSetCheckItem.isOldItem()) { //Old Item
					PosCheckItem oSetMenuCheckItem = oSetCheckItem.getCheckItem();
					oSetMenuCheckItem.setVoidLocalTime(oVoidDateTime);
					oSetMenuCheckItem.setVoidTime(oFormatter.print(oVoidDateTime.withZone(DateTimeZone.UTC)));
					oSetMenuCheckItem.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
					oSetMenuCheckItem.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
					oSetMenuCheckItem.setVoidReasonId(iVoidCodeId);
					oSetMenuCheckItem.setStatus(PosCheckItem.STATUS_DELETED);
					oSetMenuCheckItem.setModified(true);
					
					//Delete child item's modifier
					if(oSetCheckItem.hasModifier()) {
						for(FuncCheckItem oSetMenuModifierCheckItem:oSetCheckItem.m_oModifierList) {							
							if(oSetMenuModifierCheckItem.isOldItem()) {
								PosCheckItem oSetMenuModifierItem = oSetMenuModifierCheckItem.getCheckItem();
								oSetMenuModifierItem.setVoidLocalTime(oVoidDateTime);
								oSetMenuModifierItem.setVoidTime(oFormatter.print(oVoidDateTime.withZone(DateTimeZone.UTC)));
								oSetMenuModifierItem.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
								oSetMenuModifierItem.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
								oSetMenuModifierItem.setVoidReasonId(iVoidCodeId);
								oSetMenuModifierItem.setStatus(PosCheckItem.STATUS_DELETED);
							}
						}
					}
				}
			}
		}else {
			for (FuncCheckItem oSetCheckItem: m_oChildItemList) {
				BigDecimal dNewChildQty = oSetCheckItem.getCheckItem().getBaseQty().multiply(dNewQty) ;
				oSetCheckItem.getCheckItem().setQty(dNewChildQty);
				oSetCheckItem.getCheckItem().setModified(true);
					
					//Delete child item's modifier
					if(oSetCheckItem.hasModifier()) {
						for(FuncCheckItem oSetMenuModifierCheckItem:oSetCheckItem.m_oModifierList) {
							oSetMenuModifierCheckItem.getCheckItem().setQty(oSetMenuModifierCheckItem.getCheckItem().getBaseQty().multiply(m_oCheckItem.getQty()));
						}
						
						// Re-calculate child item's modifiers' price
						oSetCheckItem.setAllModifiersPrice(false);
					}
			}
		}
		
		//Remove ItemDiscount if any
		if (this.isOldItem() && dRemoveQty.compareTo(dOriginalQty) == 0 && hasItemDiscount(false)) {
			for(int i = 0; i < m_oItemDiscountList.size(); i++) {
				PosCheckDiscount oItemDiscount = m_oItemDiscountList.get(i);
				
				if (oItemDiscount.getCdisId() > 0) {
					oItemDiscount.setVoidTime(oFormatter.print(oVoidDateTime.withZone(DateTimeZone.UTC)));
					oItemDiscount.setVoidLocTime(oVoidDateTime);
					oItemDiscount.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
					oItemDiscount.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
					oItemDiscount.setVoidVdrsId(iVoidCodeId);
					oItemDiscount.setStatus(PosCheckDiscount.STATUS_DELETED);

					PosCheckDiscountItem oCheckDiscountItem = new PosCheckDiscountItem(oItemDiscount.getCdisId(), oItemDiscount.getCitmId());
					oCheckDiscountItemIds.add(oCheckDiscountItem.getCditId());
				}
			}
		}
		
		//Remove CheckDiscountItem if any
		if(this.isOldItem() && dRemoveQty.compareTo(dOriginalQty) == 0 &&  m_oCheckDiscountItemList.size() > 0) {
			List<Integer> oCheckDiscountItemIdList = new ArrayList<Integer>();
			for(Entry<Integer, PosCheckDiscountItem> entry: m_oCheckDiscountItemList.entrySet()) {
				if(entry.getValue().getCditId() > 0)
					oCheckDiscountItemIds.add(Integer.valueOf(entry.getValue().getCditId()));
			}
			
			m_oCheckDiscountItemList.clear();
		}
		
		// Delete item
		////////////////////////////////
		if(this.isOldItem()){
			// Old item
			if (dNewQty.compareTo(BigDecimal.ZERO) > 0) {
				//add deleted item
				int iParentCitmId = 0;
				FuncCheckItem oFuncDelCheckItem = new FuncCheckItem();
				PosCheckItem oDelCheckItem = oFuncDelCheckItem.getCheckItem();
				
				oDelCheckItem.copyFromCheckItem(this.m_oCheckItem);
				oDelCheckItem.setQty(dRemoveQty);
				oDelCheckItem.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
				oDelCheckItem.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
				oDelCheckItem.setVoidReasonId(iVoidCodeId);
				oDelCheckItem.setVoidLocalTime(oVoidDateTime);
				oDelCheckItem.setVoidTime(oFormatter.print(oVoidDateTime.withZone(DateTimeZone.UTC)));
				oDelCheckItem.setStatus(PosCheckItem.STATUS_DELETED);
				
				oDelCheckItem.setTotal(BigDecimal.ZERO);
				oDelCheckItem.setTotal(oDelCheckItem.getPrice().multiply(oDelCheckItem.getQty()));
				
				iParentCitmId = oDelCheckItem.getCitmId();
				if(this.hasModifier()) {
					for(FuncCheckItem oModiCheckItem:m_oModifierList) {
						FuncCheckItem oFuncCheckModi = new FuncCheckItem();
						PosCheckItem deleteModifier = oFuncCheckModi.getCheckItem();

						deleteModifier.copyFromCheckItem(oModiCheckItem.getCheckItem());
						deleteModifier.setParentItemId(iParentCitmId); 
						deleteModifier.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
						deleteModifier.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
						deleteModifier.setVoidReasonId(iVoidCodeId);
						deleteModifier.setVoidLocalTime(oVoidDateTime);
						deleteModifier.setVoidTime(oFormatter.print(oVoidDateTime.withZone(DateTimeZone.UTC)));
						deleteModifier.setStatus(PosCheckItem.STATUS_DELETED);
						deleteModifier.setQty(deleteModifier.getBaseQty().multiply(dRemoveQty));
						oFuncCheckModi.setOpenModiPriceRate(oModiCheckItem.getOpenModiPriceRate());
						deleteModifier.setCarryTotal(deleteModifier.getPrice().multiply(deleteModifier.getQty()));
						oDelCheckItem.addValueToTotal(deleteModifier.getPrice().multiply(deleteModifier.getQty()));
						
						oFuncDelCheckItem.addModifierToList(oFuncCheckModi, false);
					}
					
					// Re-calculate the delete item's modifiers' price
					oFuncDelCheckItem.setAllModifiersPrice(false);
				}
				
				oFuncCheck.calcCheck();
				oDelCheckItem.addUpdate(false, AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncStation.get().getStationId(), false);
				if(this.hasModifier()) {
					for(FuncCheckItem oModiCheckItem:oFuncDelCheckItem.m_oModifierList) {
						oModiCheckItem.getCheckItem().setParentItemId(oDelCheckItem.getCitmId());
						oModiCheckItem.getCheckItem().addUpdate(false, AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncStation.get().getStationId(), false);
					}
				}
				
			} else {
				m_oCheckItem.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
				m_oCheckItem.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
				m_oCheckItem.setVoidReasonId(iVoidCodeId);
				m_oCheckItem.setVoidLocalTime(oVoidDateTime);
				m_oCheckItem.setVoidTime(oFormatter.print(oVoidDateTime.withZone(DateTimeZone.UTC)));
				m_oCheckItem.setStatus(PosCheckItem.STATUS_DELETED);
								
				for (FuncCheckItem oSetCheckItem: m_oChildItemList) {					
					if (oSetCheckItem.isOldItem()) { //Old Item
						PosCheckItem oSetMenuCheckItem = oSetCheckItem.getCheckItem();
						oSetMenuCheckItem.setVoidLocalTime(oVoidDateTime);
						oSetMenuCheckItem.setVoidTime(oFormatter.print(oVoidDateTime.withZone(DateTimeZone.UTC)));
						oSetMenuCheckItem.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
						oSetMenuCheckItem.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
						oSetMenuCheckItem.setVoidReasonId(iVoidCodeId);
						oSetMenuCheckItem.setStatus(PosCheckItem.STATUS_DELETED);
						oSetMenuCheckItem.setModified(true);
						
						//Delete child item's modifier
						if(oSetCheckItem.hasModifier()) {
							for(FuncCheckItem oSetMenuModifierCheckItem:oSetCheckItem.m_oModifierList) {							
								if(oSetMenuModifierCheckItem.isOldItem()) {
									PosCheckItem oSetMenuModifierItem = oSetMenuModifierCheckItem.getCheckItem();
									oSetMenuModifierItem.setVoidLocalTime(oVoidDateTime);
									oSetMenuModifierItem.setVoidTime(oFormatter.print(oVoidDateTime.withZone(DateTimeZone.UTC)));
									oSetMenuModifierItem.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
									oSetMenuModifierItem.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
									oSetMenuModifierItem.setVoidReasonId(iVoidCodeId);
									oSetMenuModifierItem.setStatus(PosCheckItem.STATUS_DELETED);
								}
							}
						}
					}
				}
				
				if(m_oExtraInfoList.size() > 0) {
					for(PosCheckExtraInfo oExtraInfo:m_oExtraInfoList) {
						oExtraInfo.setStatus(PosCheckExtraInfo.STATUS_DELETED);
					}
				}

			}
			oFuncCheck.calcCheck();
			
			m_oCheckItem.setModified(true);
			if(m_bStockDeliveryInvoice)
				m_oPosStockDeliveryInvoiceItem.setModified(true);
		}
		
		return true;
	}
	
	public void addModifierToList(FuncCheckItem oModiFuncCheckItem, boolean bNewItem) {
		m_oModifierList.add(oModiFuncCheckItem);
		if(bNewItem)
			m_oCheckItem.increaseModifierCount();
	}
	
	public void addChildItemToList(FuncCheckItem oChildFuncCheckItem, boolean bNewItem) {
		m_oChildItemList.add(oChildFuncCheckItem);
		if(bNewItem)
			m_oCheckItem.increaseChildCount();
	}
	
	public void replaceChildItemToList(int iLineNo) {
		FuncCheckItem oChildFuncCheckItem = m_oChildItemList.get(m_oChildItemList.size()-1);
		m_oChildItemList.add(iLineNo, oChildFuncCheckItem);
		m_oChildItemList.remove(m_oChildItemList.size()-1);
	}
	
	public void deleteChildItemFromList(int iChildItemIndex){
		if(m_oChildItemList.size() <= iChildItemIndex)
			return;
		
		m_oChildItemList.remove(iChildItemIndex);
		m_oCheckItem.decreaseChildCount();
	}
	
	public void addMixAndMatchItemToList(FuncCheckItem oMixAndMatchFuncCheckItem) {
		m_oMixAndMatchChildItemList.add(oMixAndMatchFuncCheckItem);
	}
	
	public void addCheckDiscountItemToList(int iCheckDiscountIndex, PosCheckDiscountItem oCheckDiscountItem) {
		m_oCheckDiscountItemList.put(Integer.valueOf(iCheckDiscountIndex), oCheckDiscountItem);
		m_oCheckItem.setModified(true);
	}
	
	public void removeCheckDiscountItemFromList(int iCheckDiscountIndex) {
		PosCheckDiscountItem oPosCheckDiscountItem = null;
		
		if(m_oCheckDiscountItemList.containsKey(iCheckDiscountIndex)) {
			oPosCheckDiscountItem = m_oCheckDiscountItemList.get(Integer.valueOf(iCheckDiscountIndex));
			if(oPosCheckDiscountItem.getCdisId() > 0)
				m_oCheckItem.setModified(true);
			
			m_oCheckDiscountItemList.remove(Integer.valueOf(iCheckDiscountIndex));
		}
	}
	
	public void addNewItemDiscountToList(PosDiscountType oDiscountType, BigDecimal dDiscAmountRate, int iCheckId, int iCheckPartyId) {
		int i=0;
		PosCheckDiscount oCheckDiscount = new PosCheckDiscount();
		DateTime oApplyTime = new DateTime();
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		oCheckDiscount.setBdayId(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
		oCheckDiscount.setBperId(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId());
		oCheckDiscount.setShopId(AppGlobal.g_oFuncOutlet.get().getShopId());
		oCheckDiscount.setOletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oCheckDiscount.setChksId(iCheckId);
		oCheckDiscount.setCptyId(iCheckPartyId);
		oCheckDiscount.setCitmId(m_oCheckItem.getCitmId());
		oCheckDiscount.setDtypId(oDiscountType.getDtypId());
		for(i=1; i<=5; i++) {
			oCheckDiscount.setName(i, oDiscountType.getName(i));
			oCheckDiscount.setShortName(i, oDiscountType.getShortName(i));
		}
		oCheckDiscount.setDgrpId(oDiscountType.getDgrpId());
		oCheckDiscount.setMethod(oDiscountType.getMethod());
		oCheckDiscount.setType(oDiscountType.getType());
		oCheckDiscount.setUsedFor(oDiscountType.getUsedFor());
		oCheckDiscount.setClassKey(oDiscountType.getClassKey());
		if(oDiscountType.isFixAmountDiscountPerItemMethod()) {
			oCheckDiscount.setFixAmount(dDiscAmountRate);
			oCheckDiscount.setRate(BigDecimal.ZERO);
		}else if(oDiscountType.isPercentageDiscountMethod()) {
			oCheckDiscount.setFixAmount(BigDecimal.ZERO);
			oCheckDiscount.setRate(dDiscAmountRate);
		}
		oCheckDiscount.setIncludeTaxScMask(oDiscountType.getIncludeTaxScMask());
		oCheckDiscount.setIncludePreDisc(oDiscountType.getIncludePreDisc());
		oCheckDiscount.setIncludeMidDisc(oDiscountType.getIncludeMidDisc());
		oCheckDiscount.setApplyLocalTime(oApplyTime);
		oCheckDiscount.setApplyTime(oFormatter.print(oApplyTime.withZone(DateTimeZone.UTC)));
		oCheckDiscount.setApplyUserId(AppGlobal.g_oFuncUser.get().getUserId());
		oCheckDiscount.setApplyStationId(AppGlobal.g_oFuncStation.get().getStationId());
		
		//the corresponding pos check discount item
		PosCheckDiscountItem oCheckDiscountItem = new PosCheckDiscountItem();
		oCheckDiscountItem.setCitmId(m_oCheckItem.getCitmId());
		oCheckDiscount.addDiscItemToList(oCheckDiscountItem);
		
		m_oItemDiscountList.add(oCheckDiscount);
	}
	
	public void voidOldItemDiscount(int iDiscountIndex, int iVoidCodeId, String sVoidReasonName) {
		DateTime dtVoidDateTime = new DateTime();
		DateTimeFormatter voidFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		PosCheckDiscount oCheckDiscount = this.m_oItemDiscountList.get(iDiscountIndex);
		
		oCheckDiscount.setVoidLocTime(dtVoidDateTime);
		oCheckDiscount.setVoidTime(voidFormatter.print(dtVoidDateTime.withZone(DateTimeZone.UTC)));
		oCheckDiscount.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
		oCheckDiscount.setVoidStatId(AppGlobal.g_oFuncStation.get().getStationId());
		oCheckDiscount.setVoidVdrsId(iVoidCodeId);
		oCheckDiscount.setStatus(PosCheckDiscount.STATUS_DELETED);
		
		PosCheckDiscountItem oCheckDiscountItem = new PosCheckDiscountItem(oCheckDiscount.getCdisId(), oCheckDiscount.getCitmId());
		oCheckDiscountItem.deleteById();
		oCheckDiscount.cleanupCheckDiscountItemList();
	}
	
	public int rushOrder(String sTableNo) {
		DateTime dtRushDateTime = new DateTime();
		DateTimeFormatter rushFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		int iRushCount = m_oCheckItem.getRushCount();

		// set item's rush order info
		m_oCheckItem.setRushCount(iRushCount+1);
		m_oCheckItem.setRushLocTime(dtRushDateTime);
		m_oCheckItem.setRushTime(rushFormatter.print(dtRushDateTime.withZone(DateTimeZone.UTC)));
		m_oCheckItem.setRushUserId(AppGlobal.g_oFuncUser.get().getUserId());
		m_oCheckItem.setRushStatId(AppGlobal.g_oFuncStation.get().getStationId());
		m_oCheckItem.setModified(true);

		return iRushCount;
	}
	
	public void processPrintRushOrderActionSlip(String sTableNo) {
		// print slip
		JSONObject oHeaderJSONObject = new JSONObject(), oInfoJSONObject = new JSONObject();
		try {
			//form the header
			oHeaderJSONObject.put("header", "Rush Order");
			
			//form the information of slip
			oInfoJSONObject.put("stationId", AppGlobal.g_oFuncStation.get().getStationId());
			oInfoJSONObject.put("userId", AppGlobal.g_oFuncUser.get().getUserId());
			oInfoJSONObject.put("userName", AppGlobal.g_oFuncUser.get().getUserName(AppGlobal.g_oCurrentLangIndex.get()));
			oInfoJSONObject.put("table", sTableNo);

			// Print rush order special slip
			m_oCheckItem.printSpecialSlip(m_oCheckItem.getCheckId(), "rush_order", oHeaderJSONObject, oInfoJSONObject, null, AppGlobal.g_oCurrentLangIndex.get());
		}catch(JSONException jsone) {
			AppGlobal.stack2Log(jsone);
		}
	}
	
	public void removeItemDiscount(int iDiscountIndex) {
		m_oItemDiscountList.remove(iDiscountIndex);
	}
	
	public void addItemDiscountToList(PosCheckDiscount oCheckDiscount) {
		m_oItemDiscountList.add(oCheckDiscount);
	}
	
	public void addExtraInfoToList(PosCheckExtraInfo oCheckExtraInfo) {
		m_oExtraInfoList.add(oCheckExtraInfo);
	}
	
	public void removeExtraInfoFromList(String sBy, String sSection, String sVariable) {
		int iRemoveIndex = -1;
		PosCheckExtraInfo oExtraInfo = null;
		
		if(m_oExtraInfoList.size() == 0)
			return;
		
		for(int i=0; i<m_oExtraInfoList.size(); i++) {
			oExtraInfo = m_oExtraInfoList.get(i);
			if(oExtraInfo.getBy().equals(sBy) && oExtraInfo.getSection().equals(sSection) && oExtraInfo.getVariable().equals(sVariable)) {
				iRemoveIndex = i;
				break;
			}
		}
		
		if(iRemoveIndex >= 0)
			m_oExtraInfoList.remove(iRemoveIndex);
	}
	
	public boolean voidModifier(int iModifierIndex, BigDecimal dQty, int iVoidCodeId) {
		DateTime oVoidDateTime = new DateTime();
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		m_sErrorMessage = "";

		if(m_oModifierList.size() <= iModifierIndex)
			return false;
		
		//restore item price for item removing open modifier
		/*if (m_oModifierList.get(iModifierIndex).isOpenModifier()) {
			
		}*/
		
		if (this.isOldItem()) {	// Parent item is old item
			PosCheckItem oModifierCheckItem = m_oModifierList.get(iModifierIndex).getCheckItem();
			oModifierCheckItem.setVoidLocalTime(oVoidDateTime);
			oModifierCheckItem.setVoidTime(oFormatter.print(oModifierCheckItem.getVoidLocalTime().withZone(DateTimeZone.UTC)));
			oModifierCheckItem.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
			oModifierCheckItem.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
			oModifierCheckItem.setVoidReasonId(iVoidCodeId);
			oModifierCheckItem.setStatus(PosCheckItem.STATUS_DELETED);
			oModifierCheckItem.addUpdate(true, AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncStation.get().getStationId(), false);
		}

		m_oModifierList.remove(iModifierIndex);
		m_oCheckItem.decreaseModifierCount();
		
		return true;
	}
	
	public boolean voidAllModifier(int iVoidCodeId, int iUserId, int iStationId, boolean bIsVoidCheck) {
		DateTime oVoidDateTime = new DateTime();
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		PosCheckItem oModifierItem = null;
		m_sErrorMessage = "";
		
		for(FuncCheckItem oModifierCheckItem:m_oModifierList) {			
			if(oModifierCheckItem.isOldItem()) {
				oModifierItem = oModifierCheckItem.getCheckItem();
				oModifierItem.setVoidLocalTime(oVoidDateTime);
				oModifierItem.setVoidTime(oFormatter.print(oVoidDateTime.withZone(DateTimeZone.UTC)));
				oModifierItem.setVoidUserId(iUserId);
				oModifierItem.setVoidStationId(iStationId);
				oModifierItem.setVoidReasonId(iVoidCodeId);
				oModifierItem.setStatus(PosCheckItem.STATUS_DELETED);
			}
		}
		
		return true;
	}
	
	// Handle inclusive SC/Tax no breakdown add/waive SC/Tax pre-process
	// Return value - true : have inclusive tax no breakdown and need re-calculate check in FuncCheck
	public boolean addWaiveScTaxPreProcessForInclusiveTaxSCNoBreakdown(boolean bAddScTax, boolean[] bChosenSc, boolean[] bChosenTax) {
		int i=0;
		
		// Pre-process for inclusive tax No breakdown
		boolean bHaveInclusiveTaxNoBreakdown = false;
		if(bAddScTax){
			// Add
			if(this.getMenuItem() != null){
				for(i=1; i<=25; i++) {
					if(this.getMenuItem().getChargeTax(i-1).equals(PosCheckItem.CHARGE_TAX_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN)){
						bHaveInclusiveTaxNoBreakdown = true;
						break;
					}
				}
			}
			
			if(bHaveInclusiveTaxNoBreakdown){
				// Set back the item price to original item price
				// Include back the tax to item price
				m_oCheckItem.setPrice(m_oCheckItem.getOriginalPrice());
			}
		}else{
			// Waive
			for(i=1; i<=25; i++) {
				if(bChosenTax[(i-1)] && m_oCheckItem.getChargeTax(i).equals(PosCheckItem.CHARGE_TAX_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN)){
					// Convert from inclusive tax no breakdown to inclusive tax with breakdown for waive
					m_oCheckItem.setChargeTax(i, PosCheckItem.CHARGE_TAX_CHARGED_IN_ITEM_PRICE);
					bHaveInclusiveTaxNoBreakdown = true;
				}
			}
		}
		
		return bHaveInclusiveTaxNoBreakdown;
	}
	
	public boolean addWaiveScTax(boolean bAddScTax, boolean[] bChosenSc, boolean[] bChosenTax, String sTable) {
		int i=0;
		String sNewChargeScStr = "", sNewChargeTaxStr = "", sLogKey = "", sActionRemarkPrefix = "", sActionRemark = "";
		
		if(isOldItem())
			sActionRemarkPrefix = "[OLDITEM] - chks_item_id:"+m_oCheckItem.getCitmId()+", ";
		else
			sActionRemarkPrefix = "[NEWITEM] - table:"+sTable+", ";
		
		//Reset the item's charge sc/tax string
		for(i=1; i<=5; i++) {
			if(bAddScTax){
				// Add SC
				sNewChargeScStr = PosCheckItem.CHARGE_SC_YES;
				if(this.getMenuItem() != null){
					if(this.getMenuItem().getChargeSc(i-1).equals(PosCheckItem.CHARGE_SC_CHARGED_IN_ITEM_PRICE)){
						sNewChargeScStr = PosCheckItem.CHARGE_SC_CHARGED_IN_ITEM_PRICE;
					}
				}
			}else{
				// Waive SC
				sNewChargeScStr = PosCheckItem.CHARGE_SC_NO;
			}
			
			if(bChosenSc[(i-1)] && m_oCheckItem.getChargeSc(i).equals(sNewChargeScStr) == false) {				
				sActionRemark = sActionRemarkPrefix + "Item:"+this.getItemDescriptionByIndex(1)+", Change service charge index:"+i+", Service charge string from '"+m_oCheckItem.getChargeSc(i)+"' to '"+sNewChargeScStr+"'";
				m_oCheckItem.setChargeSc(i, sNewChargeScStr);
				
				//Add "add_service_charge/waive_service_charge" to action log list
				if(bAddScTax)
					sLogKey = AppGlobal.FUNC_LIST.add_sc_tax.name();
				else
					sLogKey = AppGlobal.FUNC_LIST.waive_sc_tax.name();
				
				if (isOldItem())
					AppGlobal.g_oActionLog.get().addActionLog(sLogKey, PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), m_oCheckItem.getCheckId(), 0, m_oCheckItem.getCitmId(), 0, 0, sActionRemark);
				else
					addActionLog(sLogKey, PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), m_oCheckItem.getCheckId(), 0, 0, 0, 0, sActionRemark);
			}
		}
		
		for(i=1; i<=25; i++) {
			if(bAddScTax) {
				// Add Tax
				sNewChargeTaxStr = PosCheckItem.CHARGE_TAX_YES;
				if(this.getMenuItem() != null){
					if(this.getMenuItem().getChargeTax(i-1).equals(PosCheckItem.CHARGE_TAX_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN)){
						sNewChargeTaxStr = PosCheckItem.CHARGE_TAX_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN;
					}else
					if(this.getMenuItem().getChargeTax(i-1).equals(PosCheckItem.CHARGE_TAX_CHARGED_IN_ITEM_PRICE)){
						sNewChargeTaxStr = PosCheckItem.CHARGE_TAX_CHARGED_IN_ITEM_PRICE;
					}
				}
			}else {
				// Waive Tax
				sNewChargeTaxStr = PosCheckItem.CHARGE_TAX_NO;
			}
			
			if(bChosenTax[(i-1)] && m_oCheckItem.getChargeTax(i).equals(sNewChargeTaxStr) == false) {				
				sActionRemark = sActionRemarkPrefix + "Item:"+this.getItemDescriptionByIndex(1)+", Change tax index:"+i+", Tax from '"+m_oCheckItem.getChargeTax(i)+"' to '"+sNewChargeTaxStr+"'";
				m_oCheckItem.setChargeTax(i, sNewChargeTaxStr);
				
				//Add "add_tax/waive_tax" to action log
				if(bAddScTax)
					sLogKey = AppGlobal.FUNC_LIST.add_sc_tax.name();
				else
					sLogKey = AppGlobal.FUNC_LIST.waive_sc_tax.name();
				AppGlobal.g_oActionLog.get().addActionLog(sLogKey, PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), m_oCheckItem.getCheckId(), 0, m_oCheckItem.getCitmId(), 0, 0, sActionRemark); 
				
			}
		}
		
		return true;
	}
	
	public void setWaiveServiceChargeFlagNotByOverride(boolean bWaiveServiceChargeNotByOverride){
		m_bWaiveServiceChargeNotByOverride = bWaiveServiceChargeNotByOverride;
	}
	
	public boolean isWaiveServiceChargeNotByOverride(){
		return m_bWaiveServiceChargeNotByOverride;
	}
	
	public void setChangePriceLevelManually(boolean bChangePriceLevelManually){
		m_bChangePriceLevelManually = bChangePriceLevelManually;
	}
	
	public boolean isChangePriceLevelManually(){
		return m_bChangePriceLevelManually;
	}
	
	public void takeout(boolean bTakeout) {
		if (bTakeout)
			m_oCheckItem.setOrderingType(PosCheckItem.ORDERING_TYPE_TAKEOUT);
		else
			m_oCheckItem.setOrderingType(PosCheckItem.ORDERING_TYPE_FINE_DINING);
	}
	
	//check whether the provided FuncCheckItem having same content with current object
	public boolean isSameFuncCheckItemContent(FuncCheckItem oProvidedCheckItem) {
		//return if item id not same
		if(m_oCheckItem.getItemId() != oProvidedCheckItem.getCheckItem().getItemId())
			return false;

		//return if item quantity not same
		if(m_oCheckItem.getQty().compareTo(oProvidedCheckItem.getCheckItem().getQty()) != 0)
			return false;
		
		//return if modifier count not same
		if(m_oCheckItem.getModifierCount() != oProvidedCheckItem.getCheckItem().getModifierCount())
			return false;
		
		//return if modifier item not same
		if(m_oCheckItem.getModifierCount() > 0) {
			boolean bModifierFind = false;
			
			for(FuncCheckItem oModiCheckItem:m_oModifierList) {
				bModifierFind = false;
				for(FuncCheckItem oProvidedModiCheckItem:oProvidedCheckItem.getModifierList()) {
					if(oModiCheckItem.isSameFuncCheckItemContent(oProvidedModiCheckItem)) {
						bModifierFind = true;
						break;
					}
				}
				
				if(!bModifierFind)
					return false;
			}
		}
		
		//return if child count not same
		if(m_oCheckItem.getChildCount() != oProvidedCheckItem.getCheckItem().getChildCount()) 
			return false;
		
		//return if child item not same
		if(m_oCheckItem.getChildCount() > 0) {
			boolean bChildFind = false;
			
			for(FuncCheckItem oChildCheckItem:m_oChildItemList) {
				 bChildFind = false;
				 for(FuncCheckItem oProvidedChildCheckItem:oProvidedCheckItem.getChildItemList()) {
					 if(oChildCheckItem.isSameFuncCheckItemContent(oProvidedChildCheckItem)) {
						 bChildFind = true;
						 break;
					 }
				 }
				 
				 if(!bChildFind)
					 return false;
			}
		}
		
		return true;
	}
	
	//change price level
	public void changePriceLevel(int iTargetPriceLevel, boolean bByPriceOverrideFunction) {
		MenuItem oMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId()).getMenuItem();
		
		if(m_oCheckItem.isSetMenuChildItem()) {
			this.setNewChildItemPrice(iTargetPriceLevel, oMenuItem);
		}else if(m_oCheckItem.isModifierItem()) {
			// The modifier price is calculate with other modifier later
		}else {
			this.setNewBasicItemPrice(iTargetPriceLevel, oMenuItem);
		}
		
		m_oCheckItem.setPriceLevel(iTargetPriceLevel);
		if(!bByPriceOverrideFunction){
			m_oCheckItem.setOriginalPriceLevel(iTargetPriceLevel);
		}
	}
	
	// Change item price
	public void changeItemPrice(BigDecimal dPrice) {
		BigDecimal dTotal = m_oCheckItem.getQty().multiply(dPrice);
		m_oCheckItem.setPrice(dPrice);
		m_oCheckItem.setOriginalPrice(dPrice);
		m_oCheckItem.setTotal(dTotal);
	}
	
	// Replace the default price with open price
	public void setOpenPriceToItem(BigDecimal dPrice) {
		BigDecimal dTotal = BigDecimal.ZERO;
		
		dTotal = m_oCheckItem.getQty().multiply(dPrice);
		m_oCheckItem.setPrice(dPrice);
		m_oCheckItem.setOriginalPrice(dPrice);
		m_oCheckItem.setTotal(dTotal);
	}
	
	// Replace the default description with a new description
	public void setItemDesc(boolean bAppend, String sDescription) {
		int i=0;
		String sTempStr = "";
		if(bAppend)
			sTempStr = sDescription;
		
		for(i=1; i<=5; i++) {
			if(bAppend)
				sDescription = m_oCheckItem.getName(i)+sTempStr;
			m_oCheckItem.setName(i, sDescription);
		}
	}
	
	// Replace the default short description with a new description
	public void setItemShortDesc(boolean bAppend, String sDescription) {
		int i=0;
		String sTempStr = "";
		if(bAppend)
			sTempStr = sDescription;
		
		for(i=1; i<=5; i++) {
			if(bAppend)
				sDescription = m_oCheckItem.getShortName(i)+sTempStr;
			m_oCheckItem.setShortName(i, sDescription);
		}
	}
	
	public boolean voidItemDiscount(int iVoidCodeId, int iUserId, int iStationId) {
		DateTime dtVoidDateTime = new DateTime();
		DateTimeFormatter voidFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		for(int i = 0; i < m_oItemDiscountList.size(); i++) {
			PosCheckDiscount oItemDiscount = m_oItemDiscountList.get(i);
			oItemDiscount.setVoidTime(voidFormatter.print(dtVoidDateTime.withZone(DateTimeZone.UTC)));
			oItemDiscount.setVoidLocTime(dtVoidDateTime);
			oItemDiscount.setVoidStationId(iStationId);
			oItemDiscount.setVoidUserId(iUserId);
			oItemDiscount.setVoidVdrsId(iVoidCodeId);
			oItemDiscount.setStatus(PosCheckDiscount.STATUS_DELETED);
		}
		
		return true;
	}
	
	// After using send check, update the check item return from the platform API 
	public void updateCheckItem(PosCheckItem oUpdatedCheckItem) {
		m_oCheckItem.init();
		m_oCheckItem = oUpdatedCheckItem;
		
		//update modifier list
		if(m_oCheckItem.getModifierList().size() > 0) {
			for(FuncCheckItem oFuncModifierItem:m_oModifierList) {
				for(PosCheckItem oModifier:m_oCheckItem.getModifierList()) {
					if(oModifier.getCitmId() == oFuncModifierItem.getCheckItem().getCitmId())
						oFuncModifierItem.updateCheckItem(oModifier);
				}
			}
		}
		
		//update child item list
		if(m_oCheckItem.getChildItemList().size() > 0) {
			for(FuncCheckItem oFuncChildItem:m_oChildItemList) {
				for(PosCheckItem oChildItem:m_oCheckItem.getChildItemList()) {
					if(oChildItem.getCitmId() == oFuncChildItem.getCheckItem().getCitmId())
						oFuncChildItem.updateCheckItem(oChildItem);
				}
			}
		}
		
		//update item discount list
		if(m_oCheckItem.getItemDiscountList().size() > 0) {
			m_oItemDiscountList.clear();
			for(PosCheckDiscount oItemDiscount:m_oCheckItem.getItemDiscountList())
				m_oItemDiscountList.add(oItemDiscount);
		}
		
		m_oCheckItem.resetItemDiscountList();
		m_oCheckItem.resetModifierList();
		m_oCheckItem.resetChildItemList();
		m_oCheckItem.resetExtraInfoList();
	}

	// After using send check, update the check item return from the platform API 
	public void updateOldCheckItem(PosCheckItem oUpdatedCheckItem) {
		m_oCheckItem.init();
		m_oCheckItem = oUpdatedCheckItem;
		
		if(m_oCheckItem.getModifierList().size() > 0) {
			m_oModifierList.clear();
			for(PosCheckItem oModifier:m_oCheckItem.getModifierList()) {
				FuncCheckItem oFuncModifierCheckItem = new FuncCheckItem(oModifier);
				m_oModifierList.add(oFuncModifierCheckItem);
			}
		}
		
		//update child item list
		if(m_oCheckItem.getChildItemList().size() > 0) {
			for(FuncCheckItem oFuncChildItem:m_oChildItemList) {
				for(PosCheckItem oChildItem:m_oCheckItem.getChildItemList()) {
					if(oChildItem.getSeatNo() == oFuncChildItem.getCheckItem().getSeatNo() && oChildItem.getSeq() == oFuncChildItem.getCheckItem().getSeq()) {
						oFuncChildItem.updateOldCheckItem(oChildItem);
					}
				}
			}
		}
		
		//update item discount list
		if(m_oCheckItem.getItemDiscountList().size() > 0) {
			m_oItemDiscountList.clear();
			for(PosCheckDiscount oItemDiscount:m_oCheckItem.getItemDiscountList())
				m_oItemDiscountList.add(oItemDiscount);
		}
		
		m_oCheckItem.resetItemDiscountList();
		m_oCheckItem.resetModifierList();
		m_oCheckItem.resetChildItemList();
		m_oCheckItem.resetExtraInfoList();
	}
	
	// Change item's check id
	public void changeCheckId(int iChksId) {
		m_oCheckItem.setCheckId(iChksId);
		
		if(m_oModifierList.size() > 0) {
			for(FuncCheckItem oModifier:m_oModifierList)
				oModifier.changeCheckId(iChksId);
		}
		
		if(m_oItemDiscountList.size() > 0) {
			for(PosCheckDiscount oItemDisc:m_oItemDiscountList)
				oItemDisc.setChksId(iChksId);
		}
		
		if(m_oChildItemList.size() > 0) {
			for(FuncCheckItem oChildItem:m_oChildItemList) {
				oChildItem.changeCheckId(iChksId);
			}
		}
	}
	
	// Change item's check party id
	public void changeCheckPartyId(int iCptyId) {
		m_oCheckItem.setCheckPartyId(iCptyId);
		
		if(m_oModifierList.size() > 0) {
			for(FuncCheckItem oModifier:m_oModifierList)
				oModifier.changeCheckPartyId(iCptyId);
		}
		
		if(m_oItemDiscountList.size() > 0) {
			for(PosCheckDiscount oItemDisc:m_oItemDiscountList)
				oItemDisc.setCptyId(iCptyId);
		}
		
		if(m_oChildItemList.size() > 0) {
			for(FuncCheckItem oChildItem:m_oChildItemList) {
				oChildItem.changeCheckPartyId(iCptyId);
			}
		}
	}
	
	// set original check id split from
	public void setSplitFromCheckId(int iSplitFromCheckId) {
		m_oCheckItem.setSplitFromCheckId(iSplitFromCheckId);
	}
	
	// Update current Func Check Item
	public void updateFuncCheckItemFromDB(){
		// Retrieve the ordered new item
		JSONArray itemJSONArray = new JSONArray();
		
		// Create posting JSON from Func Check Item
		createJSONArrayForItemUpdate(itemJSONArray);
		
		// Update the system menu item list
		AppGlobal.g_oFuncMenu.get().updateStoredMenuItemList(itemJSONArray);
		
		// Update Func Check Item
		updateFromUpdateMenuItemList();
	}
	
	public void updateCouponItemInformation(String sStartCoupon, String sEndCoupon, BigDecimal dNewPrice, BigDecimal dNewQty) {
		//update price and quantity
		m_oCheckItem.setBasicPrice(dNewPrice);
		m_oCheckItem.setPrice(m_oCheckItem.getOriginalPrice());
		m_oCheckItem.setOriginalPrice(m_oCheckItem.getOriginalPrice());
		m_oCheckItem.setQty(dNewQty);
		m_oCheckItem.setTotal(m_oCheckItem.getOriginalPrice().multiply(dNewQty));
		
		//add extra info
		PosCheckExtraInfo oItemExtraInfo1 = new PosCheckExtraInfo(); 
		oItemExtraInfo1.setBy(PosCheckExtraInfo.BY_ITEM);
		oItemExtraInfo1.setCheckId(m_oCheckItem.getCheckId());
		oItemExtraInfo1.setItemId(m_oCheckItem.getCitmId());
		oItemExtraInfo1.setSection(PosCheckExtraInfo.SECTION_ITEM_TYPE);
		oItemExtraInfo1.setVariable(PosCheckExtraInfo.VARIABLE_COUPON_ITEM);
		oItemExtraInfo1.setValue("y");
		m_oExtraInfoList.add(oItemExtraInfo1);
		
		PosCheckExtraInfo oItemExtraInfo2 = new PosCheckExtraInfo();
		oItemExtraInfo2.setBy(PosCheckExtraInfo.BY_ITEM);
		oItemExtraInfo2.setCheckId(m_oCheckItem.getCheckId());
		oItemExtraInfo2.setItemId(m_oCheckItem.getCitmId());
		oItemExtraInfo2.setSection(PosCheckExtraInfo.SECTION_ONLINE_COUPON);
		oItemExtraInfo2.setVariable(PosCheckExtraInfo.VARIABLE_SELL_START_COUPON);
		oItemExtraInfo2.setValue(sStartCoupon);
		m_oExtraInfoList.add(oItemExtraInfo2);
		
		PosCheckExtraInfo oItemExtraInfo3 = new PosCheckExtraInfo();
		oItemExtraInfo3.setBy(PosCheckExtraInfo.BY_ITEM);
		oItemExtraInfo3.setCheckId(m_oCheckItem.getCheckId());
		oItemExtraInfo3.setItemId(m_oCheckItem.getCitmId());
		oItemExtraInfo3.setSection(PosCheckExtraInfo.SECTION_ONLINE_COUPON);
		oItemExtraInfo3.setVariable(PosCheckExtraInfo.VARIABLE_SELL_END_COUPON);
		oItemExtraInfo3.setValue(sEndCoupon);
		m_oExtraInfoList.add(oItemExtraInfo3);
	}
	
	public void addExtraInfo(String sBy, String sSection, String sVariable, String sValue) {
		PosCheckExtraInfo oItemExtraInfo = new PosCheckExtraInfo();
		oItemExtraInfo.setBy(sBy);
		oItemExtraInfo.setCheckId(m_oCheckItem.getCheckId());
		oItemExtraInfo.setItemId(m_oCheckItem.getCitmId());
		oItemExtraInfo.setSection(sSection);
		oItemExtraInfo.setVariable(sVariable);
		oItemExtraInfo.setValue(sValue);
		m_oExtraInfoList.add(oItemExtraInfo);
		
		m_oCheckItem.setModified(true);
	}
	
	public void updateExtraInfoValue(String sBy, String sSection, String sVariable, String sValue) {
		if(m_oExtraInfoList.size() == 0)
			return;
		
		for(PosCheckExtraInfo oCheckExtraInfo:m_oExtraInfoList) {
			if(oCheckExtraInfo.getBy().equals(sBy) && oCheckExtraInfo.getSection().equals(sSection) && oCheckExtraInfo.getVariable().equals(sVariable)) {
				oCheckExtraInfo.setValue(sValue);
				m_oCheckItem.setModified(true);
				return;
			}
		}
	}
	
	public void updateExtraInfoStatus(String sBy, String sSection, String sVariable, String sStatus) {
		if(m_oExtraInfoList.size() == 0)
			return;
		
		for(PosCheckExtraInfo oCheckExtraInfo:m_oExtraInfoList) {
			if(oCheckExtraInfo.getBy().equals(sBy) && oCheckExtraInfo.getSection().equals(sSection) && oCheckExtraInfo.getVariable().equals(sVariable)) {
				oCheckExtraInfo.setStatus(sStatus);
				if(oCheckExtraInfo.getCkeiId() > 0)
					m_oCheckItem.setModified(true);
				return;
			}
		}
	}
	
	public String getExtraInfoBySectionAndVariable(String sSection, String sVariable) {
		String sValue = null;
		
		for(PosCheckExtraInfo oExtraInfo:m_oExtraInfoList) {
			if(oExtraInfo.getSection().equals(sSection) && oExtraInfo.getVariable().equals(sVariable)) {
				sValue = oExtraInfo.getValue();
				break;
			}
		}
		
		return sValue;
	}
	
	public void resetAsNewItem() {
		String sOrderTime = "";
		DateTime oApplyTime = new DateTime();
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		sOrderTime = oFormatter.print(oApplyTime.withZone(DateTimeZone.UTC));
		
		// reset check item id, order time
		this.m_oCheckItem.setCheckItemId(0);
		this.m_oCheckItem.setBusinessDayId(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
		this.m_oCheckItem.setBusinessPeriodId(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId());
		this.m_oCheckItem.setOrderTime(sOrderTime);
		this.m_oCheckItem.setOrderLocTime(oApplyTime);
		
		// reset modifier item id, order time
		for (FuncCheckItem oModiFuncCheckItem : this.m_oModifierList) {
			oModiFuncCheckItem.getCheckItem().setCheckItemId(0);
			oModiFuncCheckItem.getCheckItem().setParentItemId(0);
			oModiFuncCheckItem.getCheckItem().setBusinessDayId(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
			oModiFuncCheckItem.getCheckItem().setBusinessPeriodId(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId());
			oModiFuncCheckItem.getCheckItem().setOrderTime(sOrderTime);
			oModiFuncCheckItem.getCheckItem().setOrderLocTime(oApplyTime);
		}
		
		// reset check discount item id
		for (Entry<Integer, PosCheckDiscountItem> entry: this.m_oCheckDiscountItemList.entrySet()) {
			entry.getValue().setCditId(0);
			entry.getValue().setCitmId(this.m_oCheckItem.getCitmId());
		}
		
		// reset discount id, order time
		for (PosCheckDiscount oPosCheckDiscount : this.m_oItemDiscountList) {
			oPosCheckDiscount.setCdisId(0);
			oPosCheckDiscount.setCitmId(this.m_oCheckItem.getCitmId());
			for (PosCheckDiscountItem oPosCheckDiscountItem : oPosCheckDiscount.getCheckDiscountItemList()) {
				oPosCheckDiscountItem.setCditId(0);
				oPosCheckDiscountItem.setCdisId(0);
				oPosCheckDiscountItem.setCitmId(this.m_oCheckItem.getCitmId());
			}
			oPosCheckDiscount.setBdayId(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
			oPosCheckDiscount.setBperId(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId());
			oPosCheckDiscount.setApplyLocalTime(oApplyTime);
			oPosCheckDiscount.setApplyTime(sOrderTime);
			oPosCheckDiscount.setApplyUserId(AppGlobal.g_oFuncUser.get().getUserId());
			oPosCheckDiscount.setApplyStationId(AppGlobal.g_oFuncStation.get().getStationId());
		}
	}
	
	public LinkedList<PosActionLog> getActionLogList() {
		return m_oActionLogList;
	}
	
	public void addActionLog(String sKey, String sResult, String sTable, int iUserId, int iShopId, int iOletId, int iBdayId, int iBperId, int iStationId, int iChksId, int iCptyId, int iCitmId, int iCdisId, int iCpayId, String sRemark) {
		PosActionLog oNewActionLog = new PosActionLog();
		DateTime oCurrentDateTime = new DateTime();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		oNewActionLog.setActionLocalTime(oCurrentDateTime);
		oNewActionLog.setActionTime(fmt.print(oCurrentDateTime.withZone(DateTimeZone.UTC)));
		
		oNewActionLog.setKey(sKey);
		oNewActionLog.setUserId(iUserId);
		oNewActionLog.setActionResult(sResult);
		oNewActionLog.setTable(sTable);
		oNewActionLog.setRecordId(0);
		oNewActionLog.setShopId(iShopId);
		oNewActionLog.setOletId(iOletId);
		oNewActionLog.setBdayId(iBdayId);
		oNewActionLog.setBperId(iBperId);
		oNewActionLog.setStatId(iStationId);
		oNewActionLog.setChksId(iChksId);
		oNewActionLog.setCptyId(iCptyId);
		oNewActionLog.setCitmId(iCitmId);
		oNewActionLog.setCdisId(iCdisId);
		oNewActionLog.setCpayId(iCpayId);
		oNewActionLog.setRemark(sRemark);

		try {
			m_oActionLogList.addLast(oNewActionLog);
		} catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	public void updateActionLog(int iChksId, int iCitmId, int iCdisId) {
		try {
			for (PosActionLog oActionLog:m_oActionLogList) {
				if (iChksId != 0)
					oActionLog.setChksId(iChksId);
				if (iCitmId != 0)
					oActionLog.setCitmId(iCitmId);
				if (iCdisId != 0)
					oActionLog.setCdisId(iCdisId);
			}
		} catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	public void setStatus(String sStatus) {
		m_oCheckItem.setStatus(sStatus);
	}
	
	public boolean hasMatchedMixAndMatchRule() {
		if(this.getMixAndMatchRuleId() > 0 || this.getMixAndMatchItemList().size() > 0 || m_oCheckItem.getMixAndMatchItemId() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setNewItemWithMixAndMatchRule(boolean bNewItemWithMixAndMatchRule) {
		if(this.isOldItem() == false)
			m_bNewItemWithMixAndMatchRule = bNewItemWithMixAndMatchRule;
	}
	
	public boolean isNewItemWithMixAndMatchRule() {
		return m_bNewItemWithMixAndMatchRule;
	}
	
	public void setOpenModiPriceRate(BigDecimal dPriceRate) {
		m_dOpenModiPriceRate = dPriceRate;
	}
	
	public BigDecimal getOpenModiPriceRate() {
		return m_dOpenModiPriceRate;
	}
	
	public void convertInclusiveTaxNoBreakdownToInclusiveTax() {
		for(int i=1; i<=25; i++) {
			if(m_oCheckItem.getChargeTax(i).equals(PosCheckItem.CHARGE_TAX_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN)){
				m_oCheckItem.setChargeTax(i, PosCheckItem.CHARGE_TAX_CHARGED_IN_ITEM_PRICE);
			}
		}
	}
	
	public void convertInclusiveTaxToInclusiveTaxNoBreakdown() {
		if(this.getMenuItem() != null){
			for(int i=1; i<=25; i++)
				m_oCheckItem.setChargeTax(i, this.getMenuItem().getChargeTax(i-1));
		}
		
		// Set back the item price to original item price
		m_oCheckItem.setPrice(m_oCheckItem.getOriginalPrice());
	}
	
	public String getChargeSc() {
		String sChargeSc = "";
		for(int i=1; i<=5; i++) {
			if(m_oCheckItem.getChargeSc(i).equals(PosCheckItem.CHARGE_SC_NO) == false)
				sChargeSc = sChargeSc + m_oCheckItem.getChargeSc(i);
			else
				sChargeSc = sChargeSc + " ";
		}
		return sChargeSc;
	}
	
	public String getChargeTax() {
		String sChargeTax = "";
		for(int i=1; i<=25; i++) {
			if(m_oCheckItem.getChargeTax(i).equals(PosCheckItem.CHARGE_TAX_NO) == false)
				sChargeTax = sChargeTax + m_oCheckItem.getChargeTax(i);
			else
				sChargeTax = sChargeTax + " ";
		}
		return sChargeTax;
	}
	
	public void setPendingItem(String sPending) {
		m_oCheckItem.setPending(sPending);
		m_oCheckItem.setModified(true);
	}
	
	public int getRushCount(){
		return m_oCheckItem.getRushCount();
	}
}
