package app;

import om.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import externallib.StringLib;
import externallib.Util;

public class FuncCheckItem {
	// Check item
	private PosCheckItem m_oCheckItem;
	
	// Child item list
	private ArrayList<FuncCheckItem> m_oChildItemList;
	
	// Modifier list
	private ArrayList<FuncCheckItem> m_oModifierList;
	private BigDecimal m_dOpenModiPriceRate;
	
	// Check discount item for check discount
	private List<PosCheckDiscountItem> m_oCheckDiscountItemList;
	
	// Item discount list
	private ArrayList<PosCheckDiscount> m_oItemDiscountList;
	
	// Post discount on sc / tax
	private HashMap<String, BigDecimal[]> m_oDiscOnSc;
	private HashMap<String, BigDecimal[]> m_oDiscOnTax;
	
	// Item breakdown price
	private BigDecimal m_dBreakdownPrice;
	
	// Extra info list
	private ArrayList<PosCheckExtraInfo> m_oExtraInfoList;
	
	// Tax Sc Ref List
	private ArrayList<PosCheckTaxScRef> m_oTaxScRefList;
	
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
	
	// Flag to determine this item is printed or not for continuous print
	//private Boolean m_bIsPrinted  = false;
	private Boolean m_bIsAddUpdatePrinted = false;
	private Boolean m_bIsVoidPrinted = false;
	
	// Flag to determine the item is split by which parent item
	private String m_sSplitItemParentItemId = "";
	
	// Flag to determine the new item can item grouping or not
	private Boolean m_bIsAllowItemGrouping = true;
	
	// Mark if the modifier need split
	private boolean m_bModiSplitFlag = false;
	
	// Store modifier old qty for split
	private BigDecimal m_dModiOldQtyForSplit = null;
	
	public static String partialPendingItem_PendingInfo = "itemList";
	public static String partialPendingItem_Delivery_Info = "deliveredItemList";
	
	// child sequence in set menu lookup
	private int m_iChildItemSeqInSetMenuLookup;
	
	// For set menu panel, modifier lookup
	class PanelLookupContent {
		int parentTabIndex;
		int parentTabItemSeq;
		List<Integer> panelMenuIdList;
		boolean newOrderItem = false;
		boolean predefinedItem = false;
		
		PanelLookupContent(int iParentTabIndex, int iParentTabItemSeq, List<Integer> oPanelMenuIdList){
			parentTabIndex = iParentTabIndex;
			parentTabItemSeq = iParentTabItemSeq;
			panelMenuIdList = oPanelMenuIdList;
		}
	}
	
	private PanelLookupContent m_oPanelLookupContent;

	// Return the error message
	public String getLastErrorMessage() {
		return m_sErrorMessage;
	}

	public FuncCheckItem(){
		m_oCheckItem = new PosCheckItem();
		m_oChildItemList = new ArrayList<FuncCheckItem>();
		m_oModifierList = new ArrayList<FuncCheckItem>();
		m_dOpenModiPriceRate = null;
		m_oCheckDiscountItemList = new ArrayList<PosCheckDiscountItem>();
		m_oItemDiscountList = new ArrayList<PosCheckDiscount>();
		m_oExtraInfoList = new ArrayList<PosCheckExtraInfo>();
		m_oTaxScRefList = new ArrayList<PosCheckTaxScRef>();
		m_oMixAndMatchChildItemList = new ArrayList<FuncCheckItem>();
		m_oActionLogList = new LinkedList<PosActionLog>();
		m_oPosStockDeliveryInvoiceItem = new PosStockDeliveryInvoiceItem();
		m_bStockDeliveryInvoice = false;
		m_iMixAndMatchRuleId = 0;
		m_bNewItemWithMixAndMatchRule = false;
		m_bWaiveServiceChargeNotByOverride = false;
		m_bChangePriceLevelManually = false;
		m_bIsAddUpdatePrinted = false;
		m_bIsVoidPrinted = false;
		m_sSplitItemParentItemId = "";
		m_oPanelLookupContent = new PanelLookupContent(-1, -1, null);
		m_dBreakdownPrice = BigDecimal.ZERO;
		m_oDiscOnSc = new HashMap<String, BigDecimal[]>();
		m_oDiscOnSc.put(PosDiscountType.TYPE_PRE_DISCOUNT, new BigDecimal[AppGlobal.SC_COUNT]);
		m_oDiscOnSc.put(PosDiscountType.TYPE_MID_DISCOUNT, new BigDecimal[AppGlobal.SC_COUNT]);
		m_oDiscOnSc.put(PosDiscountType.TYPE_POST_DISCOUNT, new BigDecimal[AppGlobal.SC_COUNT]);
		m_oDiscOnTax = new HashMap<String, BigDecimal[]>();
		m_oDiscOnTax.put(PosDiscountType.TYPE_PRE_DISCOUNT, new BigDecimal[AppGlobal.TAX_COUNT]);
		m_oDiscOnTax.put(PosDiscountType.TYPE_MID_DISCOUNT, new BigDecimal[AppGlobal.TAX_COUNT]);
		m_oDiscOnTax.put(PosDiscountType.TYPE_POST_DISCOUNT, new BigDecimal[AppGlobal.TAX_COUNT]);
		m_iChildItemSeqInSetMenuLookup = 0;
		
	}
	
	// Create FuncCheckItem from PosCheckItem loading from database
	public FuncCheckItem(PosCheckItem oCheckItem) {
		this();
		
		int i = 0;
		
		m_oCheckItem = new PosCheckItem(oCheckItem);
		
		//handle item's modifier items for each item
		if(m_oCheckItem.hasModifier()) {
			for (i = 0; i < m_oCheckItem.getModifierList().size(); i++) {
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
			for (i = 0; i < m_oCheckItem.getChildItemList().size(); i++) {
				FuncCheckItem oChildFuncCheckItem = new FuncCheckItem(m_oCheckItem.getChildItemList().get(i));
				
				// Set the parent func check item of the child
				oChildFuncCheckItem.setParentFuncCheckItem(this);
				
				this.addChildItemToList(oChildFuncCheckItem, false);
			}
		}
		
		//handle item's discount for each item
		if(!m_oCheckItem.getItemDiscountList().isEmpty()) {
			for(PosCheckDiscount oItemDiscount:m_oCheckItem.getItemDiscountList())
				this.addItemDiscountToList(new PosCheckDiscount(oItemDiscount));
		}
		
		//handle item's extra info for each item
		if (!m_oCheckItem.getExtraInfoList().isEmpty()) {
			for(PosCheckExtraInfo oExtraInfo:m_oCheckItem.getExtraInfoList()) {
				this.addExtraInfoToList(new PosCheckExtraInfo(oExtraInfo));
			}
		}
		
		//handle item's tax sc ref for each item
		if (!m_oCheckItem.getTaxScRefList().isEmpty()) {
			for (PosCheckTaxScRef oTaxScRef : m_oCheckItem.getTaxScRefList()) {
				this.addTaxScRefList(new PosCheckTaxScRef(oTaxScRef));
			}
		}
		
		//String sStatus = getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_CONTINUOUS_PRINT, PosCheckExtraInfo.VARIABLE_PRINTED_STATUS);
		//if (sStatus != null)
			//if (sStatus.equals(PosCheckExtraInfo.VALUE_TRUE))
			//	this.m_bIsPrinted = true;
		if (isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_CONTINUOUS_PRINT, PosCheckExtraInfo.VARIABLE_PRINTED_STATUS, 0)) {
			String sJsonStatus = getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_CONTINUOUS_PRINT, PosCheckExtraInfo.VARIABLE_PRINTED_STATUS);
			try {
				JSONObject oJSONObject = new JSONObject(sJsonStatus);
				boolean bIsAddUpdatePrint = false;
				boolean bIsVoidPrint = false;
				if (oJSONObject.has("addUpdate"))
					bIsAddUpdatePrint = oJSONObject.optBoolean("addUpdate");
				if (oJSONObject.has("void"))
					bIsVoidPrint = oJSONObject.optBoolean("void");
				this.m_bIsAddUpdatePrinted = bIsAddUpdatePrint;
				this.m_bIsVoidPrinted = bIsVoidPrint;
			} catch (JSONException e) {
			}
		}
	}
	
	// Clone to new item
	public FuncCheckItem(FuncCheckItem oFuncCheckItem, FuncCheckItem oParentFuncCheckItem) {
		this();
		
		this.m_oCheckItem = new PosCheckItem(oFuncCheckItem.getCheckItem());
		
		// Set parent item
		this.setParentFuncCheckItem(oParentFuncCheckItem);
		
		// Set kitchen slip setting
		this.setNoKitchenSlip(oFuncCheckItem.getNoKitchenSlip());
		
		for (int i = 0; i < oFuncCheckItem.m_oModifierList.size(); i++) {
			FuncCheckItem oModiFuncCheckItem = new FuncCheckItem(oFuncCheckItem.m_oModifierList.get(i), null);
			
			// Set the parent func check item of the child
			oModiFuncCheckItem.setParentFuncCheckItem(this);
			
			this.m_oModifierList.add(oModiFuncCheckItem);
		}
		this.m_dOpenModiPriceRate = oFuncCheckItem.getOpenModiPriceRate();
		
		// Clone applied check discount item
		for(PosCheckDiscountItem oPosCheckDiscountItem: oFuncCheckItem.m_oCheckDiscountItemList) {
			this.m_oCheckDiscountItemList.add(new PosCheckDiscountItem(oPosCheckDiscountItem));
		}
		
		// Clone item discount
		for (int i = 0; i < oFuncCheckItem.m_oItemDiscountList.size(); i++) {
			this.m_oItemDiscountList.add(new PosCheckDiscount(oFuncCheckItem.m_oItemDiscountList.get(i)));
		}
		
		// *** If this is set menu parent item, cannot clone child item, process by child item in the code afterward
		// if this is set menu child item
		if(this.isSetMenuItem() && oParentFuncCheckItem != null)
			oParentFuncCheckItem.addChildItemToList(this, true);
		
		if(!oFuncCheckItem.getExtraInfoList().isEmpty()) {
			for (int i = 0; i < oFuncCheckItem.getExtraInfoList().size(); i++)
				this.m_oExtraInfoList.add(new PosCheckExtraInfo(oFuncCheckItem.getExtraInfoList().get(i)));
		}
		
		if (!oFuncCheckItem.getTaxScRefList().isEmpty()) {
			for (PosCheckTaxScRef oCheckTaxScRef : oFuncCheckItem.getTaxScRefList()) {
				this.m_oTaxScRefList.add(new PosCheckTaxScRef(oCheckTaxScRef));
			}
		}
		
		m_bNewItemWithMixAndMatchRule = oFuncCheckItem.isNewItemWithMixAndMatchRule();
		m_bWaiveServiceChargeNotByOverride = oFuncCheckItem.isWaiveServiceChargeNotByOverride();
		m_bChangePriceLevelManually = oFuncCheckItem.isChangePriceLevelManually();
		m_bIsAddUpdatePrinted = oFuncCheckItem.isAddUpdatePrinted();
		m_bIsVoidPrinted = oFuncCheckItem.isVoidPrinted();
		m_sSplitItemParentItemId = oFuncCheckItem.getSplitItemParentItemId();
		m_oPanelLookupContent = oFuncCheckItem.getPanelLookupContent();
		m_dBreakdownPrice = oFuncCheckItem.getBreakdownPrice();
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
			if (!m_oCheckItem.getCitmId().equals(""))
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
	
	public boolean isSplitRevenueParent() {
		boolean bIsSplitRevenueParent = false;
		for (PosCheckExtraInfo oExtraInfo : m_oExtraInfoList) {
			if (oExtraInfo.getBy().equals(PosCheckExtraInfo.BY_ITEM)
					&& oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_ITEM_TYPE)
					&& oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SPLIT_REVENUE_ITEM))
				if (oExtraInfo.getValue().equals("y")) {
					bIsSplitRevenueParent = true;
					break;
				}
		}
		return bIsSplitRevenueParent;
	}
	public boolean isNoPrint() {
		return m_oCheckItem.isNoPrint();
	}
	
	public boolean isCouponItem() {
		boolean bIsCouponItem = false;
				
		if(!m_oExtraInfoList.isEmpty()) {
			for(PosCheckExtraInfo oExtraInfo:m_oExtraInfoList) {
				if(oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_ITEM_TYPE) && oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_COUPON_ITEM) && oExtraInfo.getValue() != null) {
					bIsCouponItem = true;
					break;
				}
			}
		}
		
		return bIsCouponItem;
	}
	
	public boolean isLoyaltyBenefitItem() {
		boolean bIsBennefitItem = false;
				
		if(!m_oExtraInfoList.isEmpty()) {
			for(PosCheckExtraInfo oExtraInfo:m_oExtraInfoList) {
				if(oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_LOYALTY) && oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_BENEFIT_CODE) && oExtraInfo.getValue() != null) {
					bIsBennefitItem = true;
					break;
				}
			}
		}
		return bIsBennefitItem;
	}
	
	public boolean isLoyaltyItem(){
		boolean bIsLoyaltyItem = false;
		for(PosCheckExtraInfo oExtraInfo:m_oExtraInfoList) {
			if((oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_LOYALTY) && oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SVC_CARD_NUMBER) && oExtraInfo.getValue() != null) ||
					(oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_LOYALTY) && oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER) && oExtraInfo.getValue() != null) ||
					(oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_LOYALTY) && oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_CREDIT_DATE) && oExtraInfo.getValue() != null) ||
					(oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_LOYALTY) && oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_BENEFIT_CODE) && oExtraInfo.getValue() != null) ||
					(oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_LOYALTY) && oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_CARD_NO) && oExtraInfo.getValue() != null) ||
					(oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_LOYALTY) && oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_CREDIT_DATE) && oExtraInfo.getValue() != null)) {
				bIsLoyaltyItem = true;
				break;
			}
		}
		return bIsLoyaltyItem;
	}
	
	public boolean isSVCCouponRedeemItem() {
		boolean bIsSVCCouponRedeemItem = false;
				
		if(!m_oExtraInfoList.isEmpty()) {
			for(PosCheckExtraInfo oExtraInfo:m_oExtraInfoList) {
				if(oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_ONLINE_COUPON) && oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SVC_COUPON_NO) && oExtraInfo.getValue() != null) {
					bIsSVCCouponRedeemItem = true;
					break;
				}
			}
		}
		
		return bIsSVCCouponRedeemItem;
	}
	
	public boolean isMembershipVoucherItem(){
		boolean bIsVoucherItem = false;
		
		if(!m_oExtraInfoList.isEmpty()) {
			for(PosCheckExtraInfo oExtraInfo: m_oExtraInfoList) {
				if(oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE) && oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER) &oExtraInfo.getValue() != null) {
					bIsVoucherItem = true;
					break;
				}
			}
		}
		
		return bIsVoucherItem;
	}
	
	public String getSVCCouponNumber() {
		String sSVCCouponNumber = "";
		
		if(!m_oExtraInfoList.isEmpty()) {
			for(PosCheckExtraInfo oExtraInfo:m_oExtraInfoList) {
				if(oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_ONLINE_COUPON) && oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_SVC_COUPON_NO) && oExtraInfo.getValue() != null) {
					sSVCCouponNumber = oExtraInfo.getValue();
					break;
				}
			}
		}
		
		return sSVCCouponNumber;
	}
	
	public boolean isRedeemCouponItem() {
		boolean bIsRedeemCouponItem = false;
		
		if(!m_oExtraInfoList.isEmpty()) {
			for(PosCheckExtraInfo oExtraInfo:m_oExtraInfoList) {
				if(oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_ONLINE_COUPON) && oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_REDEEM_COUNT) && oExtraInfo.getValue() != null) {
					bIsRedeemCouponItem = true;
					break;
				}
			}
		}
		
		return bIsRedeemCouponItem;
	}

	public boolean isPreorderItem() {
		boolean bIsPreorderItem = false;
		
		if(!m_oExtraInfoList.isEmpty()) {
			for(PosCheckExtraInfo oExtraInfo: m_oExtraInfoList) {
				if(oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_PREORDER) && oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_ITEM)) {
					bIsPreorderItem = true;
					break;
				}
			}
		}
		
		return bIsPreorderItem;
	}

	public boolean isCalculateMethodSumUp() {
		return m_oCheckItem.isCalculateMethodSumUp();
	}
	
	public boolean isSumUpChildItemToParent() {
		return (this.getParentFuncCheckItem() != null && this.getParentFuncCheckItem().isCalculateMethodSumUp());
	}
	
	public boolean hasModifier() {
		if (!m_oModifierList.isEmpty())
			return true;
		else
			return false;
	}
	
	public boolean hasChildItem() {
		if(!m_oChildItemList.isEmpty())
			return true;
		else
			return false;
	}
	
	public boolean hasAppliedCheckDiscount() {
		if (!m_oCheckDiscountItemList.isEmpty())
			return true;
		else
			return false;
	}
	
	public boolean hasAppliedCheckDiscountByIndex(int iIndex) {
		boolean bFound = false;
		for (PosCheckDiscountItem oPosCheckDiscountItem: m_oCheckDiscountItemList) {
			if (oPosCheckDiscountItem.getCdisSeq() == iIndex) {
				bFound = true;
				break;
			}
		}
		
		return bFound;
	}
	
	// if bUsedForDiscountOnly = true, check whether item has item discount
	// if bUsedForDiscountOnly = false, check whether item has both item discount and extra charge
	public boolean hasItemDiscount(boolean bUsedForDiscountOnly) {
		if (!m_oItemDiscountList.isEmpty()) {
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
		if (!m_oExtraInfoList.isEmpty())
			return true;
		else
			return false;
	}
	
	public boolean hasTaxScRef() {
		return (!m_oTaxScRefList.isEmpty());
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

	public boolean hasPantryMessage() {
		for (PosCheckExtraInfo oPosCheckExtraInfo: m_oExtraInfoList) {
			if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_PANTRY_MESSAGE))
				return true;
		}
		return false;
	}
	
	public boolean hasDisplayInformation(){
		for (PosCheckExtraInfo oPosCheckExtraInfo: m_oExtraInfoList) {
			if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_CARD_NO) && oPosCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_LOYALTY))
				return true;
		}
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
	
	public boolean isSuspended() {
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if(oFuncMenuItem == null)
			return true;
		
		if(oFuncMenuItem.getMenuItem().isSuspended()) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("unable_to_order_suspended_item");
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

	public boolean isAppendPanelButtonDescription() {
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if (oFuncMenuItem == null)
			return false;
		
		if (oFuncMenuItem.getMenuItem().isAppendDescriptionWithPanelButton())
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
			if(oMenuItem.isBasicForceOpenPrice())
				return true;
			else if(oMenuItem.getBasicPriceByPriceLevel(iPriceLevel) == null)
				return true;
		}else if(isSetMenuItem()){
			if(oMenuItem.isChildForceOpenPrice())
				return true;
			else if(oMenuItem.getChildPriceByPriceLevel(iPriceLevel) == null)
				return true;
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
			if(oMenuItem.isModifierForceOpenPrice())
				return true;
			else if(oFuncMenuItem.getMenuItem().isAddUnitModifierPriceToUnitPriceModifierOperator() || oFuncMenuItem.getMenuItem().isAddUnitModifierPriceToTotalModifierOperator()){
				if(oMenuItem.getModifierPriceByPriceLevel(iPriceLevel) == null)
					return true;
			}else if(oFuncMenuItem.getMenuItem().isMultipleRateModifierOperator()){
				if(oMenuItem.getModifierRateByPriceLevel(iPriceLevel) == null)
					return true;
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
	
	public boolean isPartialPendingItem() {
		if(this.isExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_REMAINING_PENDING_INFO, 0) 
				&& !this.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_REMAINING_PENDING_INFO).isEmpty()) 
			return true;
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
		
		if(!oFuncMenuItem.getMenuItem().getModifierMenuList().isEmpty())
			return true;
		else
			return false;
	}
	
	// Get setup modifier menu list for this item
	public List<MenuMenu> getModifierMenuList() {
		FuncMenuItem oFuncMenuItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId());
		if(oFuncMenuItem == null)
			return null;
		
		if(!oFuncMenuItem.getMenuItem().getModifierMenuList().isEmpty())
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
	
	// Check whether item is new order item
	public boolean isNewOrderItem(){
		return m_oPanelLookupContent.newOrderItem;
	}
	
	// Item net total : citm_round_total + citm_pre_disc(-ve) + citm_mid_disc(-ve) + citm_post_disc(-ve)
	public BigDecimal getNetItemTotal() {
		BigDecimal dItemTotal = m_oCheckItem.getRoundTotal();
		for(PosCheckDiscount oItemDiscount: m_oItemDiscountList) {
			dItemTotal = dItemTotal.add(oItemDiscount.getRoundTotal());
		}
		
		return dItemTotal;
	}
	
	// Item discount total : citm_pre_disc(-ve) + citm_mid_disc(-ve) + citm_post_disc(-ve)
	public BigDecimal getItemDiscountTotal(){
		BigDecimal dItemDiscTotal = BigDecimal.ZERO;
		for(PosCheckDiscount oItemDiscount: m_oItemDiscountList) {
			dItemDiscTotal = dItemDiscTotal.add(oItemDiscount.getRoundTotal());
		}
		return dItemDiscTotal;
	}
	
	// Get the first charge tax type from citm_charge_tax1 to citm_charge_tax25 
	// (c: charge, i: inclusive tax with breakdown, n: inclusive tax without breakdown)
	public String getChargeTaxType(){
		String sTaxType = "";
		for(int i = 1; i <= AppGlobal.TAX_COUNT; i++){
			if(m_oCheckItem.getChargeTax(i) == null || m_oCheckItem.getChargeTax(i).isEmpty())
				continue;
			sTaxType = m_oCheckItem.getChargeTax(i);
			break;
		}
		return sTaxType;
	}
	
	// Get the first service charge type from citm_charge_sc1 to citm_charge_sc5 
	// (c: charge, i: inclusive sc with breakdown, n: inclusive sc without breakdown)
	public String getServiceChargeType(){
		String sScType = "";
		for(int i = 1; i <= AppGlobal.SC_COUNT; i++){
			if(m_oCheckItem.getChargeSc(i) == null || m_oCheckItem.getChargeSc(i).isEmpty())
				continue;
			sScType = m_oCheckItem.getChargeSc(i);
			break;
		}
		return sScType;
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
	
	public String[] getItemShortDescription() {
		String[] sDescription = Arrays.copyOf(m_oCheckItem.getName(), m_oCheckItem.getName().length);
		for (int i=0; i<sDescription.length; i++) {
			if (!m_oCheckItem.getShortName(i+1).isEmpty())
				sDescription[i] = m_oCheckItem.getShortName(i+1);
		}
		
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
			if(m_oCheckItem.getShortName(iBilingualLangIndex).isEmpty()){
				if (!sDescription.equals(m_oCheckItem.getName(iBilingualLangIndex)))
					sDescription += System.lineSeparator() + m_oCheckItem.getName(iBilingualLangIndex);
			} else {
				if (!sDescription.equals(m_oCheckItem.getShortName(iBilingualLangIndex)))
					sDescription += System.lineSeparator() + m_oCheckItem.getShortName(iBilingualLangIndex);
			}
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
		boolean bFound = false;
		for (PosCheckDiscountItem oPosCheckDiscountItem: m_oCheckDiscountItemList) {
			if (oPosCheckDiscountItem.getCdisSeq() == iCheckDiscountIndex) {
				bFound = true;
				break;
			}
		}
		
		return bFound;
	}
	
	// Get check discount item by check discount index 
	public PosCheckDiscountItem getCheckDiscountItemList(int iCheckDiscountIndex) {
		PosCheckDiscountItem oPosCheckDiscountItem = null;
		for (PosCheckDiscountItem oTempPosCheckDiscountItem: m_oCheckDiscountItemList) {
			if (oTempPosCheckDiscountItem.getCdisSeq() == iCheckDiscountIndex) {
				oPosCheckDiscountItem = oTempPosCheckDiscountItem;
				break;
			}
		}
		return oPosCheckDiscountItem;
	}
	
	protected List<PosCheckDiscountItem> getAllCheckDiscountItemList() {
		return m_oCheckDiscountItemList;
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
		
		for(PosCheckDiscountItem oCheckDiscountItem:m_oCheckDiscountItemList) 
			dAppliedCheckDiscTotal = dAppliedCheckDiscTotal.add(oCheckDiscountItem.getTotal());
		
		return dAppliedCheckDiscTotal;
	}
	
	// Get applied check discount round total of item
	public BigDecimal getAppliedCheckDiscountRoundTotal() {
		BigDecimal dAppliedCheckDiscTotal = BigDecimal.ZERO;
		
		for(PosCheckDiscountItem oCheckDiscountItem:m_oCheckDiscountItemList) 
			dAppliedCheckDiscTotal = dAppliedCheckDiscTotal.add(oCheckDiscountItem.getRoundTotal());
		
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
	
	// Set the item as a new order item
	public void setNewOrderItem(boolean bNewOrder){
		m_oPanelLookupContent.newOrderItem = bNewOrder;
	}
	
	// Set panel lookup content
	public void setPanelLookupContent(int iParentTabIndex, int iParentTabItemSeq, List<Integer> oPanelMenuIdList){
		m_oPanelLookupContent = new PanelLookupContent(iParentTabIndex, iParentTabItemSeq, oPanelMenuIdList);
	}
	
	// Get existing extra info list
	public ArrayList<PosCheckExtraInfo> getExtraInfoList() {
		return m_oExtraInfoList;
	}
	
	public ArrayList<PosCheckTaxScRef> getTaxScRefList() {
		return m_oTaxScRefList;
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
	
	//get panel lookup content
	public PanelLookupContent getPanelLookupContent(){
		return m_oPanelLookupContent;
	}
	
	public void updatePanelMenuIdList(List<Integer> oList){
		m_oPanelLookupContent.panelMenuIdList = oList;
	}
	
	public void setPreDefinedItem(boolean bSet){
		m_oPanelLookupContent.predefinedItem = bSet;
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
		synchronized (this.getCheckItem()) {
			this.getCheckItem().cleanCheckDiscountItemList();
			if(hasAppliedCheckDiscount()) {
				List<PosCheckDiscountItem> oCheckDiscountItemList = new ArrayList<PosCheckDiscountItem>();
				
				for(PosCheckDiscountItem oPosCheckDiscountItem: m_oCheckDiscountItemList) {
					oCheckDiscountItemList.add(oPosCheckDiscountItem);
				}
				this.getCheckItem().setCheckDiscountItemList(oCheckDiscountItemList);
			}
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
		
		//form the tax sc ref list
		if (hasTaxScRef()) {
			List<PosCheckTaxScRef> oItemTaxScRefList = new ArrayList<PosCheckTaxScRef>();
			
			for (PosCheckTaxScRef oCheckTaxScRef : m_oTaxScRefList)
				oItemTaxScRefList.add(oCheckTaxScRef);
			this.getCheckItem().setTaxScRefList(oItemTaxScRefList);
		}
		
		//form the child list
		if(this.getCheckItem().getChildCount() > 0) {
			List<PosCheckItem> oChildItemList = new ArrayList<PosCheckItem>();
			
			for(FuncCheckItem oChildFuncCheckItem:this.getChildItemList()) {
				oChildItemList.add(oChildFuncCheckItem.constructPosCheckItemForSaving());
			}
			this.getCheckItem().setChildItemList(oChildItemList);
		}
		
		//form the modifier list
		if(this.getCheckItem().getModifierCount() > 0) {
			List<PosCheckItem> oModifierList = new ArrayList<PosCheckItem>();
			
			for(FuncCheckItem oModifierFuncCheckItem:this.getModifierList()) {
				oModifierList.add(oModifierFuncCheckItem.constructPosCheckItemForSaving());
			}
			this.getCheckItem().setModifierList(oModifierList);
		}
		
		return this.getCheckItem();
	}
	
	// Update several information (e.g. outlet id, shop id, ...) to check item records
	public void updateMultipleCheckItemInfoForNewItem(String sBusinessDayId, String sBusinessPeriodId, String sChksId, int iShopId, int iOutletId, String sOrderTime, DateTime oOrderDateTime, int iAppliedUserId) {
		PosCheckItem oCheckItem = m_oCheckItem;
		m_sErrorMessage = "";
		
		if (oCheckItem.getCitmId().equals("")) {
			// Update current item
			oCheckItem.setBusinessDayId(sBusinessDayId);
			oCheckItem.setBusinessPeriodId(sBusinessPeriodId);
			oCheckItem.setCheckId(sChksId);
			oCheckItem.setShopId(iShopId);
			oCheckItem.setOutletId(iOutletId);
			oCheckItem.setOriOutletId(iOutletId);
			if(iAppliedUserId == 0)
				oCheckItem.setOrderUserId(AppGlobal.g_oFuncUser.get().getUserId());
			else
				oCheckItem.setOrderUserId(iAppliedUserId);
			oCheckItem.setOrderStationId(AppGlobal.g_oFuncStation.get().getStationId());
			oCheckItem.setOrderTime(sOrderTime);
			oCheckItem.setOrderLocTime(oOrderDateTime);
			
			// Update item discount period ID
			ArrayList<PosCheckDiscount> oItemDiscountHashMap = this.getItemDiscountList();
			if(oItemDiscountHashMap != null && !oItemDiscountHashMap.isEmpty()) {
				for(PosCheckDiscount oCheckDiscount:oItemDiscountHashMap) 
					oCheckDiscount.setBperId(sBusinessPeriodId);
			}
			
			// Update modifier
			for(FuncCheckItem oFuncCheckItem:m_oModifierList){
				oFuncCheckItem.updateMultipleCheckItemInfoForNewItem(sBusinessDayId, sBusinessPeriodId, sChksId, iShopId, iOutletId, sOrderTime, oOrderDateTime, iAppliedUserId);
			}
			
			// Update child item
			for(FuncCheckItem oFuncCheckItem:m_oChildItemList){
				oFuncCheckItem.updateMultipleCheckItemInfoForNewItem(sBusinessDayId, sBusinessPeriodId, sChksId, iShopId, iOutletId, sOrderTime, oOrderDateTime, iAppliedUserId);
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
		int i=0;
		String sParentCitmId = "";
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
			sParentCitmId = oParentFuncCheckItem.getCheckItem().getCitmId();
		m_oCheckItem.setParentItemId(sParentCitmId);
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
		}else if(bModifier){
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
		m_oCheckItem.setPostDisc(BigDecimal.ZERO);
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
		m_oCheckItem.setBasicCalculateMethod(oMenuItem.getBasicCalculateMethod());
		m_oCheckItem.setStatus(PosCheckItem.STATUS_NORMAL);
	}
	
	private void setNewBasicItemPrice(int iPriceLevel, MenuItem oMenuItem){
		// Set Basic Price
		// Basic price = 0 for normal item
		m_oCheckItem.setBasicPrice(BigDecimal.ZERO);
		
		// Set Original Price
		if(oMenuItem.getBasicPriceByPriceLevel(iPriceLevel) != null)
			m_oCheckItem.setOriginalPrice(oMenuItem.getBasicPriceByPriceLevel(iPriceLevel));
		else
			m_oCheckItem.setOriginalPrice(BigDecimal.ZERO);
		
		// Set Price
		m_oCheckItem.setPrice(m_oCheckItem.getOriginalPrice());
	}
	
	private void setNewChildItemPrice(int iPriceLevel, MenuItem oMenuItem){
		// Set Basic Price
		if(oMenuItem.getChildGetRevenue().equals(MenuItem.CHILD_GET_REVENUE_SPILT_BASIC_PRICE)){
			if(oMenuItem.getBasicPriceByPriceLevel(iPriceLevel) != null)
				m_oCheckItem.setBasicPrice(oMenuItem.getBasicPriceByPriceLevel(iPriceLevel));
			else
				m_oCheckItem.setBasicPrice(BigDecimal.ZERO);
		}else if(oMenuItem.getChildGetRevenue().equals(MenuItem.CHILD_GET_REVENUE_SPILT_CHILD_PRICE))
			m_oCheckItem.setBasicPrice(BigDecimal.ZERO);
		else
			m_oCheckItem.setBasicPrice(BigDecimal.ZERO);
		
		// Set Original Price
		if(oMenuItem.getChildPriceByPriceLevel(iPriceLevel) != null)
			m_oCheckItem.setOriginalPrice(oMenuItem.getChildPriceByPriceLevel(iPriceLevel));
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
					if(oMenuItem.getBasicPriceByPriceLevel(iPriceLevel) != null) 
						oCheckItem.setBasicPrice(oMenuItem.getBasicPriceByPriceLevel(iPriceLevel));
					else 
						oCheckItem.setBasicPrice(BigDecimal.ZERO);
				}else if(oMenuItem.getChildGetRevenue().equals(MenuItem.MODIFIER_GET_REVENUE_SPILT_MODIFIER_PRICE))
					oCheckItem.setBasicPrice(BigDecimal.ZERO);
				else
					oCheckItem.setBasicPrice(BigDecimal.ZERO);
			}
			
			// Set Original Price
			if((bProcessOnOldItem || !isOldItem()) && !oFuncModiCheckItem.getCheckItem().isDeleted()) {
				if(oFuncModiCheckItem.isOpenModifier()) {
					if(oFuncModiCheckItem.getOpenModiPriceRate() != null && (oMenuItem.isAddUnitModifierPriceToUnitPriceModifierOperator() || oMenuItem.isAddUnitModifierPriceToTotalModifierOperator()))
						oCheckItem.setOriginalPrice(oFuncModiCheckItem.getOpenModiPriceRate());
					else
						oCheckItem.setOriginalPrice(BigDecimal.ZERO);
				}else {
					if(oMenuItem.getModifierPriceByPriceLevel(iPriceLevel) != null)
						oCheckItem.setOriginalPrice(oMenuItem.getModifierPriceByPriceLevel(iPriceLevel));
					else
						oCheckItem.setOriginalPrice(BigDecimal.ZERO);
					oFuncModiCheckItem.setOpenModiPriceRate(oCheckItem.getOriginalPrice());
				}
			}
			
			// Set Price
			if(oMenuItem.isAddUnitModifierPriceToUnitPriceModifierOperator())
				oCheckItem.setPrice(oCheckItem.getOriginalPrice());
			// whole item modifier type
			else if(oMenuItem.isAddUnitModifierPriceToTotalModifierOperator()){
				// Reverse to calculate the price
				BigDecimal dNewPrice = BigDecimal.ZERO;
				if(oCheckItem.getQty() != null && oCheckItem.getQty().compareTo(BigDecimal.ZERO) != 0){
					if(!oFuncModiCheckItem.getModiSplitFlag())
						//keep the original price as default setting
						dNewPrice = oFuncModiCheckItem.getOpenModiPriceRate().divide(oCheckItem.getQty(), 10, RoundingMode.HALF_UP);
					else {
						if(oFuncModiCheckItem.getModiOldQtyForSplit() == null)
							//keep the current price
							dNewPrice = oFuncModiCheckItem.getCheckItem().getPrice();
						else {
							//apply 1/oldQty ratio to the price
							dNewPrice = oFuncModiCheckItem.getCheckItem().getCarryTotal().divide(oFuncModiCheckItem.getModiOldQtyForSplit(), 10, RoundingMode.HALF_UP);
							oFuncModiCheckItem.setModiOldQtyForSplit(null);
						}
					}
					if(oCheckItem.getQty().signum() == -1)
						dNewPrice = dNewPrice.negate();
				}
				oCheckItem.setPrice(dNewPrice);
				oCheckItem.setOriginalPrice(dNewPrice);
			}else if(oMenuItem.isMultipleRateModifierOperator()){
				// Rate
				if((bProcessOnOldItem || !isOldItem()) && !oFuncModiCheckItem.getCheckItem().isDeleted()) {
					BigDecimal dRate = new BigDecimal("1.0");
					if(oFuncModiCheckItem.isOpenModifier()) {
						if(oFuncModiCheckItem.getOpenModiPriceRate() != null)
							dRate = oFuncModiCheckItem.getOpenModiPriceRate();
					}else {
						if(oMenuItem.getModifierRateByPriceLevel(iPriceLevel) != null)
							dRate = oMenuItem.getModifierRateByPriceLevel(iPriceLevel);
						else
							dRate = new BigDecimal("1.0");
					}
					
					BigDecimal dParentTotal = oFuncModiCheckItem.getParentFuncCheckItem().getCheckItem().getPrice().multiply(oFuncModiCheckItem.getParentFuncCheckItem().getCheckItem().getQty());
					if(oFuncModiCheckItem.getParentFuncCheckItem().getCheckItem().getQty().compareTo(BigDecimal.ZERO) != 0) {
						oCheckItem.setPrice((dParentTotal.divide(oFuncModiCheckItem.getParentFuncCheckItem().getCheckItem().getQty(), 10, RoundingMode.HALF_UP)).multiply((dRate.subtract(new BigDecimal("1.0")))));
						oCheckItem.setOriginalPrice(oCheckItem.getPrice());
					}else {
						oCheckItem.setPrice(BigDecimal.ZERO);
						oCheckItem.setOriginalPrice(BigDecimal.ZERO);
					}
				}
			}
			dRemainItemTotal = dRemainItemTotal.add(oCheckItem.getPrice().multiply(oCheckItem.getQty()));
		}
		
		// Prevent modifier cause position item total for negative price item
		BigDecimal dParentItemOriginalTotal = this.getCheckItem().getPrice().multiply(this.getCheckItem().getQty());
		if (dParentItemOriginalTotal.signum() >= 0) {
			// Original item total is positive / zero amount
			if (dRemainItemTotal.signum() < 0) {
				// Remain item total become negative, this mean the negative amount of modifiers excess the item total, need handle
				for(int j = m_oModifierList.size() - 1; j >= 0; j--){
					FuncCheckItem oFuncModiCheckItem = m_oModifierList.get(j);
					PosCheckItem oCheckItem = oFuncModiCheckItem.getCheckItem();
					BigDecimal dModifierTotal = oCheckItem.getPrice().multiply(oCheckItem.getQty());
					if (dModifierTotal.signum() < 0) {
						if (dModifierTotal.compareTo(dRemainItemTotal) <= 0) {
							BigDecimal dNewPrice = dModifierTotal.subtract(dRemainItemTotal);
							oCheckItem.setPrice(dNewPrice);
							oCheckItem.setOriginalPrice(dNewPrice);
							
							// Handle Finish
							break;
						} else {
							oCheckItem.setPrice(BigDecimal.ZERO);
							oCheckItem.setOriginalPrice(BigDecimal.ZERO);
							
							dRemainItemTotal = dRemainItemTotal.subtract(dModifierTotal);
						}
					}
				}
			}
		} else {
			// Original item total is negative
			if (dRemainItemTotal.signum() > 0) {
				// Remain item total become positive, this mean the positive amount of modifiers excess the item total, need handle
				for(int j = m_oModifierList.size() - 1; j >= 0; j--){
					FuncCheckItem oFuncModiCheckItem = m_oModifierList.get(j);
					PosCheckItem oCheckItem = oFuncModiCheckItem.getCheckItem();
					BigDecimal dModifierTotal = oCheckItem.getPrice().multiply(oCheckItem.getQty());
					if (dModifierTotal.signum() > 0) {
						if (dModifierTotal.compareTo(dRemainItemTotal) > 0) {
							BigDecimal dNewPrice = dModifierTotal.subtract(dRemainItemTotal);
							oCheckItem.setPrice(dNewPrice);
							oCheckItem.setOriginalPrice(dNewPrice);
							
							// Handle Finish
							break;
						} else {
							oCheckItem.setPrice(BigDecimal.ZERO);
							oCheckItem.setOriginalPrice(BigDecimal.ZERO);
							
							dRemainItemTotal = dRemainItemTotal.subtract(dModifierTotal);
						}
					}
				}
			}
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
		if (isOpenDescription() == false && isAppendOpenDescription() == false && isAppendPanelButtonDescription() == false) {
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
		// If price <> original price, the price is changed by some operations (e.g. mix and match, override item to open price), skip update price and total
		if(isOpenPrice() == false && (m_oCheckItem.getPrice().compareTo(m_oCheckItem.getOriginalPrice()) == 0) && !m_oCheckItem.isOverrideToOpenPriceItem()){
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
			}else if(m_oCheckItem.isModifierItem()){
				// Modifier
				// Set Basic Quantity
				m_oCheckItem.setBaseQty(new BigDecimal("1.0"));
				
				// Set Parent if not set
				if(m_oParentFuncCheckItem == null)
					m_oParentFuncCheckItem = this;
				
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
		m_oCheckItem.setPostDisc(BigDecimal.ZERO);
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
		//for negative items adding qty to zero
//		if (dNewQty.compareTo(BigDecimal.ZERO) <= 0) {
//			m_sErrorMessage = AppGlobal.g_oLang.get()._("this_function_does_not_allow_zero_item_quantity");
//			return false;
//		}
		
		if (dNewQty.compareTo(dOriQty) == 0) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("no_change_in_item_quantity");
			return false;
		}
		
		if (this.isMinimumChargeItem()) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("cannot_perform_this_function_on_minimum_charge_item");
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
					m_sErrorMessage = AppGlobal.g_oLang.get()._("items_modifier_is_missing_in_menu_setup")
							+ System.lineSeparator() + AppGlobal.g_oLang.get()._("cannot_change_the_quantity_for_the_item");
					
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
		if(!m_oCheckItem.getCitmId().equals("")){
			m_oCheckItem.setModified(true);
		}
		else {
			//Add "change_quantity" log to log list
			addActionLog(AppGlobal.FUNC_LIST.change_quantity_last.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), m_oCheckItem.getCheckId(), "", "", "", "", sOriQty+" -> "+sNewQty);
		}
		
		return true;
	}
	
	public void processPrintChangeQtyActionSlip(String sTable, String sTableExtension, String sOriQty, String sNewQty, JSONObject oNeedToUpdateItemObject){
		if(!m_oCheckItem.isSetMenuChildItem()) {
			JSONObject oHeaderJSONObject = new JSONObject(), oInfoJSONObject = new JSONObject(), oTempJSONObject = null;
			JSONArray oTempJSONArray = null;
			ArrayList<String> oItemIds = null;
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
				
				oTempJSONArray = new JSONArray();
				oTempJSONArray.put(oNeedToUpdateItemObject);
				oInfoJSONObject.put("partialSendQty", oTempJSONArray);
				
				if(hasChildItem()) {
					oItemIds = new ArrayList<String>();
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
				oParameters[1] = PosActionPrintQueue.KEY_CHANGE_QUANTITY;
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
		AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.change_quantity_last.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), m_oCheckItem.getCheckId(), "", m_oCheckItem.getCitmId(), "", "", sOriQty+" -> "+sNewQty);
		AppGlobal.g_oActionLog.get().handleActionLog(false);
	}
	
	// Function to change item quantity without all checking
	public void internalChangeQty(BigDecimal dNewQty){
		m_oCheckItem.setQty(dNewQty);
		
		if(hasModifier()) {
			for(FuncCheckItem oFuncModiCheckItem:m_oModifierList) {
				// prepare parameters of Modifier Old Qty for split
				if(oFuncModiCheckItem.getCheckItem().getQty().compareTo(BigDecimal.ZERO) != 0 && oFuncModiCheckItem.getModiOldQtyForSplit() == null) {
					oFuncModiCheckItem.setModiOldQtyForSplit(oFuncModiCheckItem.getCheckItem().getQty());
					oFuncModiCheckItem.setModiSplitFlag(true);
				}
				oFuncModiCheckItem.getCheckItem().setQty(oFuncModiCheckItem.getCheckItem().getBaseQty().multiply(m_oCheckItem.getQty()));
			}
			
			// Re-calculate all my modifiers' price
			this.setAllModifiersPrice(false);
		}
	}
	
	public boolean voidItem(FuncCheck oFuncCheck, BigDecimal dRemoveQty, int iVoidCodeId, String sVoidReasonName, String sTable, String sTableExtension, boolean bDeleteFromChangeQuantity) {
		BigDecimal dOriginalQty = m_oCheckItem.getQty();
		DateTime oVoidDateTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		m_sErrorMessage = "";
		
		if (dRemoveQty.compareTo(BigDecimal.ZERO) == 0)
			return true;
		
		if (dRemoveQty.compareTo(m_oCheckItem.getQty()) != 0 && m_oCheckItem.getPreDisc().compareTo(BigDecimal.ZERO) != 0) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("must_delete_the_maximum_quantity_for_item_with_discount");
			return false;
		}
		
		BigDecimal dNewQty = m_oCheckItem.getQty().subtract(dRemoveQty);
		if (dNewQty.compareTo(BigDecimal.ZERO) != 0){
			//Update the new quantity and total price
			m_oCheckItem.setQty(dNewQty);
			oFuncCheck.calcCheck();
		}

		//print special slip if it is old item and is called by "Delete Item" or "Delete Multiple Item" function
		//no need to save action log if it is delete item by calling "Change Quantity" function
		if(this.isOldItem() && !bDeleteFromChangeQuantity) {
			//Add "normal_delete_item" to global action log
			String sLogRemark = "Remove Qty:"+dRemoveQty + ", VoidCodeId:" + iVoidCodeId + ", VoidReason:" + sVoidReasonName;
			AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.delete_item.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), oFuncCheck.getCheckId(), "", m_oCheckItem.getCitmId(), "", "", sLogRemark);
			AppGlobal.g_oActionLog.get().handleActionLog(false);
		}
		
		//Delete the modifier
		if(this.hasModifier()){
			if(this.isOldItem() && dRemoveQty.compareTo(dOriginalQty) == 0) {
				for(FuncCheckItem oModifierCheckItem:m_oModifierList) {
					if(oModifierCheckItem.isOldItem()) {
						PosCheckItem oModifierItem = oModifierCheckItem.getCheckItem();
						oModifierItem.setVoidLocalTime(oVoidDateTime);
						oModifierItem.setVoidTime(oFormatter.print(AppGlobal.convertTimeToUTC(oVoidDateTime)));
						oModifierItem.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
						oModifierItem.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
						oModifierItem.setVoidReasonId(iVoidCodeId);
						oModifierItem.setStatus(PosCheckItem.STATUS_DELETED);
						
						if (oModifierCheckItem.hasExtraInfo()) {
							for(PosCheckExtraInfo oExtraInfo: oModifierCheckItem.getExtraInfoList())
								oExtraInfo.setStatus(PosCheckExtraInfo.STATUS_DELETED);
						}
						
						if (oModifierCheckItem.hasTaxScRef()) {
							for (PosCheckTaxScRef oTaxScRef: oModifierCheckItem.getTaxScRefList())
								oTaxScRef.setStatus(PosCheckTaxScRef.STATUS_DELETED);
						}
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
					oSetMenuCheckItem.setVoidTime(oFormatter.print(AppGlobal.convertTimeToUTC(oVoidDateTime)));
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
								oSetMenuModifierItem.setVoidTime(oFormatter.print(AppGlobal.convertTimeToUTC(oVoidDateTime)));
								oSetMenuModifierItem.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
								oSetMenuModifierItem.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
								oSetMenuModifierItem.setVoidReasonId(iVoidCodeId);
								oSetMenuModifierItem.setStatus(PosCheckItem.STATUS_DELETED);
								
								if (oSetMenuModifierCheckItem.hasExtraInfo()) {
									for(PosCheckExtraInfo oExtraInfo: oSetMenuModifierCheckItem.getExtraInfoList())
										oExtraInfo.setStatus(PosCheckExtraInfo.STATUS_DELETED);
								}
								
								if (oSetMenuModifierCheckItem.hasTaxScRef()) {
									for (PosCheckTaxScRef oTaxScRef: oSetMenuModifierCheckItem.getTaxScRefList())
										oTaxScRef.setStatus(PosCheckTaxScRef.STATUS_DELETED);
								}
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
				
				if (oItemDiscount.getCdisId().compareTo("") > 0) {
					oItemDiscount.setVoidTime(oFormatter.print(AppGlobal.convertTimeToUTC(oVoidDateTime)));
					oItemDiscount.setVoidLocTime(oVoidDateTime);
					oItemDiscount.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
					oItemDiscount.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
					oItemDiscount.setVoidVdrsId(iVoidCodeId);
					oItemDiscount.setStatus(PosCheckDiscount.STATUS_DELETED);
					
					for(PosCheckDiscountItem oCheckDiscountItem : oItemDiscount.getCheckDiscountItemList())
						oCheckDiscountItem.setStatus(PosCheckDiscountItem.STATUS_DELETED);
					
					for (PosCheckExtraInfo oPosCheckExtraInfo:oItemDiscount.getCheckExtraInfoList())
						oPosCheckExtraInfo.setStatus(PosCheckExtraInfo.STATUS_DELETED);
				}
			}
		}
		
		//Remove CheckDiscountItem if any
		if(this.isOldItem() && dRemoveQty.compareTo(dOriginalQty) == 0 &&  !m_oCheckDiscountItemList.isEmpty()) {
			for(PosCheckDiscountItem oPosCheckDiscountItem: m_oCheckDiscountItemList) {
				if(oPosCheckDiscountItem.getCditId().compareTo("") > 0)
					oPosCheckDiscountItem.setStatus(PosCheckDiscountItem.STATUS_DELETED);
			}
		}
		
		// Delete item
		////////////////////////////////
		if(this.isOldItem()){
			// Old item
			if (dNewQty.compareTo(BigDecimal.ZERO) > 0) {
				//add deleted item
				String sParentCitmId = "";
				FuncCheckItem oFuncDelCheckItem = new FuncCheckItem();
				PosCheckItem oDelCheckItem = oFuncDelCheckItem.getCheckItem();
				
				oDelCheckItem.copyFromCheckItem(this.m_oCheckItem);
				oDelCheckItem.setQty(dRemoveQty);
				oDelCheckItem.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
				oDelCheckItem.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
				oDelCheckItem.setVoidReasonId(iVoidCodeId);
				oDelCheckItem.setVoidLocalTime(oVoidDateTime);
				oDelCheckItem.setVoidTime(oFormatter.print(AppGlobal.convertTimeToUTC(oVoidDateTime)));
				oDelCheckItem.setStatus(PosCheckItem.STATUS_DELETED);
				
				oDelCheckItem.setTotal(BigDecimal.ZERO);
				oDelCheckItem.setTotal(oDelCheckItem.getPrice().multiply(oDelCheckItem.getQty()));
				oDelCheckItem.setRoundTotal(AppGlobal.g_oFuncOutlet.get().roundItemAmountToBigDecimal(oDelCheckItem.getTotal()));
				oDelCheckItem.setRoundAmount(oDelCheckItem.getRoundTotal().subtract(oDelCheckItem.getTotal()));
				
				if (AppGlobal.g_oFuncStation.get().getSupportContinuousPrinting()) {
					try {
						JSONObject oJSONObject = new JSONObject();
						oJSONObject.put("void", false);
						if (isAddUpdatePrinted())
							oJSONObject.put("addUpdate", true);
						else
							oJSONObject.put("addUpdate", false);
						PosCheckExtraInfo oItemExtraInfo = new PosCheckExtraInfo();
						oItemExtraInfo.setBy(PosCheckExtraInfo.BY_ITEM);
						oItemExtraInfo.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
						oItemExtraInfo.setSection(PosCheckExtraInfo.SECTION_CONTINUOUS_PRINT);
						oItemExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_PRINTED_STATUS);
						oItemExtraInfo.setValue(oJSONObject.toString());
						oItemExtraInfo.setStatus(PosCheckExtraInfo.STATUS_DELETED);
						oDelCheckItem.addExtraInfoToList(oItemExtraInfo);
						
						oDelCheckItem.setModified(true);
					} catch (JSONException e) {
					}
				}
				
				
				sParentCitmId = oDelCheckItem.getCitmId();
				if(this.hasModifier()) {
					for(FuncCheckItem oModiCheckItem:m_oModifierList) {
						FuncCheckItem oFuncCheckModi = new FuncCheckItem();
						PosCheckItem deleteModifier = oFuncCheckModi.getCheckItem();

						deleteModifier.copyFromCheckItem(oModiCheckItem.getCheckItem());
						deleteModifier.setParentItemId(sParentCitmId); 
						deleteModifier.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
						deleteModifier.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
						deleteModifier.setVoidReasonId(iVoidCodeId);
						deleteModifier.setVoidLocalTime(oVoidDateTime);
						deleteModifier.setVoidTime(oFormatter.print(AppGlobal.convertTimeToUTC(oVoidDateTime)));
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
				m_oCheckItem.setVoidTime(oFormatter.print(AppGlobal.convertTimeToUTC(oVoidDateTime)));
				m_oCheckItem.setStatus(PosCheckItem.STATUS_DELETED);
				
				for (FuncCheckItem oSetCheckItem: m_oChildItemList) {
					if (oSetCheckItem.isOldItem()) { //Old Item
						PosCheckItem oSetMenuCheckItem = oSetCheckItem.getCheckItem();
						oSetMenuCheckItem.setVoidLocalTime(oVoidDateTime);
						oSetMenuCheckItem.setVoidTime(oFormatter.print(AppGlobal.convertTimeToUTC(oVoidDateTime)));
						oSetMenuCheckItem.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
						oSetMenuCheckItem.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
						oSetMenuCheckItem.setVoidReasonId(iVoidCodeId);
						oSetMenuCheckItem.setStatus(PosCheckItem.STATUS_DELETED);
						oSetMenuCheckItem.setModified(true);
						
						if(!oSetCheckItem.getExtraInfoList().isEmpty()) {
							for(PosCheckExtraInfo oExtraInfo: oSetCheckItem.getExtraInfoList())
								oExtraInfo.setStatus(PosCheckExtraInfo.STATUS_DELETED);
						}
						
						if (!oSetCheckItem.getTaxScRefList().isEmpty()) {
							for (PosCheckTaxScRef oTaxScRef: oSetCheckItem.getTaxScRefList())
								oTaxScRef.setStatus(PosCheckTaxScRef.STATUS_DELETED);
						}
						
						if (!oSetCheckItem.getItemDiscountList().isEmpty()) {
							for (PosCheckDiscount oChildItemDiscount: oSetCheckItem.getItemDiscountList()) {
								if (oChildItemDiscount.getCdisId().compareTo("") > 0) {
									oChildItemDiscount.setVoidTime(oFormatter.print(AppGlobal.convertTimeToUTC(oVoidDateTime)));
									oChildItemDiscount.setVoidLocTime(oVoidDateTime);
									oChildItemDiscount.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
									oChildItemDiscount.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
									oChildItemDiscount.setVoidVdrsId(iVoidCodeId);
									oChildItemDiscount.setStatus(PosCheckDiscount.STATUS_DELETED);
									
									for(PosCheckDiscountItem oCheckDiscountItem : oChildItemDiscount.getCheckDiscountItemList())
										oCheckDiscountItem.setStatus(PosCheckDiscountItem.STATUS_DELETED);
									
				/** childItem missing in extraInfo, the following code keep for later testing - start **/
//									for(PosCheckExtraInfo oPosCheckExtraInfo : oChildItemDiscount.getCheckExtraInfoList())
//										oPosCheckExtraInfo.setStatus(PosCheckExtraInfo.STATUS_DELETED);
				/** end **/
								}
							}
						}
						
						//Delete child item's modifier
						if(oSetCheckItem.hasModifier()) {
							for(FuncCheckItem oSetMenuModifierCheckItem:oSetCheckItem.m_oModifierList) {
								if(oSetMenuModifierCheckItem.isOldItem()) {
									PosCheckItem oSetMenuModifierItem = oSetMenuModifierCheckItem.getCheckItem();
									oSetMenuModifierItem.setVoidLocalTime(oVoidDateTime);
									oSetMenuModifierItem.setVoidTime(oFormatter.print(AppGlobal.convertTimeToUTC(oVoidDateTime)));
									oSetMenuModifierItem.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
									oSetMenuModifierItem.setVoidStationId(AppGlobal.g_oFuncStation.get().getStationId());
									oSetMenuModifierItem.setVoidReasonId(iVoidCodeId);
									oSetMenuModifierItem.setStatus(PosCheckItem.STATUS_DELETED);
									
									if(!oSetMenuModifierCheckItem.getExtraInfoList().isEmpty()) {
										for(PosCheckExtraInfo oExtraInfo: oSetMenuModifierCheckItem.getExtraInfoList())
											oExtraInfo.setStatus(PosCheckExtraInfo.STATUS_DELETED);
									}
									
									if (!oSetMenuModifierCheckItem.getTaxScRefList().isEmpty()) {
										for (PosCheckTaxScRef oTaxScRef: oSetMenuModifierCheckItem.getTaxScRefList())
											oTaxScRef.setStatus(PosCheckTaxScRef.STATUS_DELETED);
									}
								}
							}
						}
					}
				}
				
				if(!m_oExtraInfoList.isEmpty()) {
					for(PosCheckExtraInfo oExtraInfo:m_oExtraInfoList) {
						oExtraInfo.setStatus(PosCheckExtraInfo.STATUS_DELETED);
						if (oExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_CONTINUOUS_PRINT) && oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_PRINTED_STATUS)) {
							if (!isVoidPrinted()) {
								try {
									JSONObject oJSONObject = new JSONObject(oExtraInfo.getValue());
									oJSONObject.put("void", false);
									oExtraInfo.setValue(oJSONObject.toString());
								} catch (JSONException e) {
								}
							}
						}
					}
				}
				
				// mark tax sc ref as deleted
				if (!m_oTaxScRefList.isEmpty()) {
					for (PosCheckTaxScRef oTaxScRef : m_oTaxScRefList) {
						oTaxScRef.setStatus(PosCheckTaxScRef.STATUS_DELETED);
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
		oModiFuncCheckItem.getCheckItem().setSeq(m_oModifierList.size()+1);
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
	
	public void deleteChildItemById(String sCheckItemId){
		for(FuncCheckItem oChildItem : m_oChildItemList){
			if(oChildItem.getCheckItem().getCitmId().equals(sCheckItemId)){
				m_oChildItemList.remove(oChildItem);
				break;
			}
		}
	}
	
	public void addMixAndMatchItemToList(FuncCheckItem oMixAndMatchFuncCheckItem) {
		m_oMixAndMatchChildItemList.add(oMixAndMatchFuncCheckItem);
	}
	
	public void addCheckDiscountItemToList(int iCheckDiscountIndex, PosCheckDiscountItem oCheckDiscountItem) {
		oCheckDiscountItem.setCdisSeq(iCheckDiscountIndex);
		m_oCheckDiscountItemList.add(oCheckDiscountItem);
		if (!(oCheckDiscountItem.getCditId().length() > 0 && this.isOldItem()))
			m_oCheckItem.setModified(true);
	}
	
	public void removeCheckDiscountItemFromList(int iCheckDiscountIndex) {
		for (PosCheckDiscountItem oPosCheckDiscountItem: m_oCheckDiscountItemList) {
			if (oPosCheckDiscountItem.getCdisSeq() == iCheckDiscountIndex) {
				if(oPosCheckDiscountItem.getCdisId().compareTo("") > 0)
					m_oCheckItem.setModified(true);
				
				m_oCheckDiscountItemList.remove(oPosCheckDiscountItem);
				break;
			}
		}
	}
	
	public void addNewItemDiscountToList(PosDiscountType oDiscountType, BigDecimal dDiscAmountRate, String sCheckId, String sCheckPartyId, ArrayList<PosCheckExtraInfo> oExtraInfoList, int iAppliedUserId) {
		int i=0;
		PosCheckDiscount oCheckDiscount = new PosCheckDiscount();
		DateTime oApplyTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		oCheckDiscount.setBdayId(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
		if(!m_oCheckItem.getBusinessPeriodId().isEmpty())
			oCheckDiscount.setBperId(m_oCheckItem.getBusinessPeriodId());
		else
			oCheckDiscount.setBperId(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId());
		
		oCheckDiscount.setShopId(AppGlobal.g_oFuncOutlet.get().getShopId());
		oCheckDiscount.setOletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oCheckDiscount.setChksId(sCheckId);
		oCheckDiscount.setCptyId(sCheckPartyId);
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
			if (oDiscountType.isUsedForDiscount() && oDiscountType.getDiscountMaxLimit().compareTo(BigDecimal.ZERO) > 0)
				dDiscAmountRate = dDiscAmountRate.negate().compareTo(oDiscountType.getDiscountMaxLimit()) > 0 ?  oDiscountType.getDiscountMaxLimit().negate() : dDiscAmountRate;
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
		oCheckDiscount.setApplyTime(oFormatter.print(AppGlobal.convertTimeToUTC(oApplyTime)));
		if(iAppliedUserId == 0)
			oCheckDiscount.setApplyUserId(AppGlobal.g_oFuncUser.get().getUserId());
		else
			oCheckDiscount.setApplyUserId(iAppliedUserId);
		oCheckDiscount.setApplyStationId(AppGlobal.g_oFuncStation.get().getStationId());
		
		//the corresponding pos check discount item
		PosCheckDiscountItem oCheckDiscountItem = new PosCheckDiscountItem();
		oCheckDiscountItem.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oCheckDiscountItem.setCitmId(m_oCheckItem.getCitmId());
		oCheckDiscount.addDiscItemToList(oCheckDiscountItem);
		
		if (oExtraInfoList != null) {
			for (PosCheckExtraInfo oPosCheckExtraInfo: oExtraInfoList)
				oCheckDiscount.addExtraInfoToList(new PosCheckExtraInfo(oPosCheckExtraInfo));
		}
		
		if (oDiscountType.isUsedForDiscount() && oDiscountType.getDiscountMaxLimit().compareTo(BigDecimal.ZERO) > 0)
			oCheckDiscount.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), "",
					PosCheckExtraInfo.VARIABLE_MAX_CHARGE, 0, oDiscountType.getDiscountMaxLimit().toPlainString());
		
		m_oItemDiscountList.add(oCheckDiscount);
	}
	
	public void voidOldItemDiscount(int iDiscountIndex, int iVoidCodeId, String sVoidReasonName) {
		DateTime dtVoidDateTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter voidFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		PosCheckDiscount oCheckDiscount = this.m_oItemDiscountList.get(iDiscountIndex);
		
		oCheckDiscount.setVoidLocTime(dtVoidDateTime);
		oCheckDiscount.setVoidTime(voidFormatter.print(AppGlobal.convertTimeToUTC(dtVoidDateTime)));
		oCheckDiscount.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
		oCheckDiscount.setVoidStatId(AppGlobal.g_oFuncStation.get().getStationId());
		oCheckDiscount.setVoidVdrsId(iVoidCodeId);
		oCheckDiscount.setStatus(PosCheckDiscount.STATUS_DELETED);
		
		for(PosCheckDiscountItem oCheckDiscountItem : oCheckDiscount.getCheckDiscountItemList())
			oCheckDiscountItem.setStatus(PosCheckDiscountItem.STATUS_DELETED);
		
		for (PosCheckExtraInfo oPosCheckExtraInfo:oCheckDiscount.getCheckExtraInfoList())
			oPosCheckExtraInfo.setStatus(PosCheckExtraInfo.STATUS_DELETED);
	}
	
	public int rushOrder(String sTableNo) {
		DateTime dtRushDateTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter rushFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		int iRushCount = m_oCheckItem.getRushCount();

		// set item's rush order info
		m_oCheckItem.setRushCount(iRushCount+1);
		m_oCheckItem.setRushLocTime(dtRushDateTime);
		m_oCheckItem.setRushTime(rushFormatter.print(AppGlobal.convertTimeToUTC(dtRushDateTime)));
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
			
			if(this.isPartialPendingItem()) {
				JSONObject oNeedToUpdateItemObject = new JSONObject();
				JSONObject oSendedItemObject = this.getPartialPendingItemInfo(FuncCheckItem.partialPendingItem_PendingInfo);
				if (oSendedItemObject != null) {
					Iterator<String> oKeys = oSendedItemObject.keys();
					while (oKeys.hasNext()) {
						String sKeyOfSentItem = oKeys.next();
						BigDecimal dTempQty = new BigDecimal(oSendedItemObject.getString(sKeyOfSentItem));
						oNeedToUpdateItemObject.put(m_oCheckItem.getCitmId() + "_" + sKeyOfSentItem, dTempQty.toPlainString());
					}
					JSONArray oTempJSONArray = new JSONArray();
					oTempJSONArray.put(oNeedToUpdateItemObject);
					oInfoJSONObject.put("partialSendQty", oTempJSONArray);
				}
			}
			// Print rush order special slip
			m_oCheckItem.printSpecialSlip(m_oCheckItem.getCheckId(), PosActionPrintQueue.KEY_RUSH_ORDER, oHeaderJSONObject, oInfoJSONObject, null, AppGlobal.g_oCurrentLangIndex.get());
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
	
	public void addTaxScRefList(PosCheckTaxScRef oCheckTaxScRef) {
		m_oTaxScRefList.add(oCheckTaxScRef);
	}
	
	public void removeExtraInfoFromList(String sBy, String sSection, String sVariable) {
		int iRemoveIndex = -1;
		PosCheckExtraInfo oExtraInfo = null;
		
		if(m_oExtraInfoList.isEmpty())
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
		DateTime oVoidDateTime = AppGlobal.getCurrentTime(false);
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
			oModifierCheckItem.setVoidTime(oFormatter.print(AppGlobal.convertTimeToUTC(oModifierCheckItem.getVoidLocalTime())));
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
		DateTime oVoidDateTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		PosCheckItem oModifierItem = null;
		m_sErrorMessage = "";
		
		for(FuncCheckItem oModifierCheckItem:m_oModifierList) {
			if(oModifierCheckItem.isOldItem()) {
				oModifierItem = oModifierCheckItem.getCheckItem();
				oModifierItem.setVoidLocalTime(oVoidDateTime);
				oModifierItem.setVoidTime(oFormatter.print(AppGlobal.convertTimeToUTC(oVoidDateTime)));
				oModifierItem.setVoidUserId(iUserId);
				oModifierItem.setVoidStationId(iStationId);
				oModifierItem.setVoidReasonId(iVoidCodeId);
				oModifierItem.setStatus(PosCheckItem.STATUS_DELETED);
				
				if(oModifierCheckItem.hasExtraInfo()) {
					for(PosCheckExtraInfo oExtraInfo: oModifierCheckItem.getExtraInfoList())
						oExtraInfo.setStatus(PosCheckExtraInfo.STATUS_DELETED);
				}
				
				if(oModifierCheckItem.hasTaxScRef()) {
					for(PosCheckTaxScRef oTaxScRef: oModifierCheckItem.getTaxScRefList())
						oTaxScRef.setStatus(PosCheckTaxScRef.STATUS_DELETED);
				}
			}
		}
		
		return true;
	}
	
	// Handle inclusive SC/Tax no breakdown add/waive SC/Tax pre-process
	// Return value - true : have inclusive tax no breakdown and need re-calculate check in FuncCheck
	public boolean addWaiveScTaxPreProcessForInclusiveTaxSCNoBreakdown(boolean bAddScTax, boolean[] bChosenSc, String[] sChosenTax) {
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
				if((sChosenTax[(i-1)].equals(PosOverrideCondition.CHARGE_TAX_CHARGE) || sChosenTax[(i-1)].equals(PosOverrideCondition.CHARGE_TAX_WAIVE)) && m_oCheckItem.getChargeTax(i).equals(PosCheckItem.CHARGE_TAX_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN)){
					// Convert from inclusive tax no breakdown to inclusive tax with breakdown for waive
					m_oCheckItem.setChargeTax(i, PosCheckItem.CHARGE_TAX_CHARGED_IN_ITEM_PRICE);
					bHaveInclusiveTaxNoBreakdown = true;
				}
			}
		}
		
		return bHaveInclusiveTaxNoBreakdown;
	}
	
	public boolean addWaiveScTax(boolean bAddScTax, boolean[] bChosenSc, String[] sChosenTax, String sTable) {
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
					AppGlobal.g_oActionLog.get().addActionLog(sLogKey, PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), m_oCheckItem.getCheckId(), "", m_oCheckItem.getCitmId(), "", "", sActionRemark);
				else
					addActionLog(sLogKey, PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), m_oCheckItem.getCheckId(), "", "", "", "", sActionRemark);
				if (!this.getMenuItem().getChargeSc(i - 1).equals(sNewChargeScStr))
					this.setAddWaiveScTaxExtraInfo(true, bAddScTax, i, "sc");
				else if (this.getMenuItem().getChargeSc(i - 1).equals(sNewChargeScStr))
					this.setAddWaiveScTaxExtraInfo(false, bAddScTax, i, "sc");
			}
		}
		
		for(i=1; i<=25; i++) {
			if(sChosenTax[(i-1)].equals(PosOverrideCondition.INCLUSIVE_TAX_NO_BREAKDOWN) && (m_oCheckItem.getChargeTax(i).equals(PosCheckItem.CHARGE_TAX_NO) || m_oCheckItem.getChargeTax(i).equals(PosCheckItem.CHARGE_SC_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN)))
				sNewChargeTaxStr = PosCheckItem.CHARGE_TAX_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN;
			else{
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
			}
			
			if(!sChosenTax[(i-1)].equals(PosOverrideCondition.CHARGE_TAX_NO_CHANGE) && m_oCheckItem.getChargeTax(i).equals(sNewChargeTaxStr) == false) {
				
				sActionRemark = sActionRemarkPrefix + "Item:"+this.getItemDescriptionByIndex(1)+", Change tax index:"+i+", Tax from '"+m_oCheckItem.getChargeTax(i)+"' to '"+sNewChargeTaxStr+"'";
				m_oCheckItem.setChargeTax(i, sNewChargeTaxStr);
				//Add "add_tax/waive_tax" to action log
				if(bAddScTax)
					sLogKey = AppGlobal.FUNC_LIST.add_sc_tax.name();
				else
					sLogKey = AppGlobal.FUNC_LIST.waive_sc_tax.name();
				AppGlobal.g_oActionLog.get().addActionLog(sLogKey, PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), m_oCheckItem.getCheckId(), "", m_oCheckItem.getCitmId(), "", "", sActionRemark); 
				
				if (!this.getMenuItem().getChargeTax(i - 1).equals(sNewChargeTaxStr))
					this.setAddWaiveScTaxExtraInfo(true, bAddScTax, i, "tax");
				else if (this.getMenuItem().getChargeTax(i - 1).equals(sNewChargeTaxStr))
					this.setAddWaiveScTaxExtraInfo(false, bAddScTax, i, "tax");
			}
		}
		
		return true;
	}
	
	// Set the item allow group or not
	public void setAllowItemGrouping(boolean bAllow) {
		m_bIsAllowItemGrouping = bAllow;
	}
	
	// Add add / waive service charge / tax extra info into item
	public void setAddWaiveScTaxExtraInfo(boolean bAppendRecord, boolean bAddScTax, int iIndex, String sType) {
		JSONObject oAddWaiveObject = new JSONObject();
		JSONObject oTempJSONObject = new JSONObject();
		
		String sIndex = Integer.toString(iIndex);
		if (this.isExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_ADD_WAIVE_TAX_SC, 0)) {
			try {
				oAddWaiveObject = new JSONObject(this.getExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_ADD_WAIVE_TAX_SC, 0).getValue());
				
				// Get the records from extra info
				if (oAddWaiveObject.optJSONObject(sType) != null)
					oTempJSONObject = oAddWaiveObject.optJSONObject(sType);
				
				// no need to append information at the end of extra info JSONObject value
				if (!bAppendRecord) {
					oTempJSONObject.remove(sIndex);
					oAddWaiveObject.put(sType, oTempJSONObject);
					
					// Update Extra Info
					this.updateExtraInfoValue(PosCheckExtraInfo.BY_ITEM, PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_ADD_WAIVE_TAX_SC, oAddWaiveObject.toString());
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		// Append new information at the end of extra info JSONObject value
		if (bAppendRecord) {
			try {
				if (bAddScTax)
					oTempJSONObject.put(sIndex, "a"); // new added service charge / tax
				else
					oTempJSONObject.put(sIndex, "w"); // waived service charge / tax

				oAddWaiveObject.put(sType, oTempJSONObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			// Add or Update Extra Info
			if(this.isExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_ADD_WAIVE_TAX_SC, 0))
				this.updateExtraInfoValue(PosCheckExtraInfo.BY_ITEM, PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_ADD_WAIVE_TAX_SC, oAddWaiveObject.toString());
			else
				this.addExtraInfo(PosCheckExtraInfo.BY_ITEM, PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_ADD_WAIVE_TAX_SC, oAddWaiveObject.toString());
		}
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
	
	public void setRole(String sRole){
		m_oCheckItem.setRole(sRole);
	}
	
	public void setParentItemId(String sParentID) {
		m_oCheckItem.setParentItemId(sParentID);
	}
	
	public void setBasicCalculateMethod(String sBasicCalculateMethod) {
		m_oCheckItem.setBasicCalculateMethod(sBasicCalculateMethod);
	}
	
	public void resetBasicCalculateMethod() {
		m_oCheckItem.resetCalculateMethod();
	}
	
	public boolean isChangePriceLevelManually(){
		return m_bChangePriceLevelManually;
	}
	
	public void setOverrideToOpenPriceItem(boolean bOverrideToOpenPriceItem){
		m_oCheckItem.setOverrideToOpenPriceItem(bOverrideToOpenPriceItem);
	}
	
	public void setIsAddUpdatePrinted(boolean bIsPrinted){
		m_bIsAddUpdatePrinted = bIsPrinted;
	}
	
	public boolean isAddUpdatePrinted(){
		return m_bIsAddUpdatePrinted;
	}
	
	public boolean isAllowItemGrouping(){
		return m_bIsAllowItemGrouping;
	}
	
	public void setIsVoidPrinted(boolean bIsPrinted){
		m_bIsVoidPrinted = bIsPrinted;
	}
	
	public void setSplitItemParentItemId(String sItemId){
		m_sSplitItemParentItemId = sItemId;
	}
	
	public void setModiOldQtyForSplit(BigDecimal dModiOldQtyForSplit) {
		this.m_dModiOldQtyForSplit = dModiOldQtyForSplit;
	}
	
	public void setModiSplitFlag(boolean bModiSplitFlag) {
		this.m_bModiSplitFlag = bModiSplitFlag;
	}
	
	public boolean getModiSplitFlag() {
		return this.m_bModiSplitFlag;
	}
	
	public boolean isVoidPrinted(){
		return m_bIsVoidPrinted;
	}
	
	public String getSplitItemParentItemId(){
		return m_sSplitItemParentItemId;
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
		
		if(m_oCheckItem.isSetMenuChildItem())
			this.setNewChildItemPrice(iTargetPriceLevel, oMenuItem);
		else if(m_oCheckItem.isModifierItem()) {
			// The modifier price is calculate with other modifier later
		}else
			this.setNewBasicItemPrice(iTargetPriceLevel, oMenuItem);
		
		
		m_oCheckItem.setPriceLevel(iTargetPriceLevel);
		if(!bByPriceOverrideFunction){
			m_oCheckItem.setOriginalPriceLevel(iTargetPriceLevel);
		}
	}
	
	// Change item price
	public void changeItemPrice(BigDecimal dPrice) {
		BigDecimal dTotal = m_oCheckItem.getQty().multiply(dPrice);
		m_oCheckItem.setPrice(dPrice);
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
	
	// Append description after item name
	public void setItemDescByIndex(boolean bAppend, String sDescription, int iIndex) {
		String sTempStr = sDescription;
		if (bAppend)
			sTempStr = m_oCheckItem.getName(iIndex) + sDescription;
		m_oCheckItem.setName(iIndex, sTempStr);
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

	// Append description after item short name
	public void setItemShortDescByIndex(boolean bAppend, String sDescription, int iIndex) {
		String sTempStr = sDescription;
		if (bAppend)
			sTempStr = m_oCheckItem.getShortName(iIndex) + sDescription;
		m_oCheckItem.setShortName(iIndex, sTempStr);
	}
	
	public boolean voidItemDiscount(int iVoidCodeId, int iUserId, int iStationId) {
		DateTime dtVoidDateTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter voidFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		for(int i = 0; i < m_oItemDiscountList.size(); i++) {
			PosCheckDiscount oItemDiscount = m_oItemDiscountList.get(i);
			oItemDiscount.setVoidTime(voidFormatter.print(AppGlobal.convertTimeToUTC(dtVoidDateTime)));
			oItemDiscount.setVoidLocTime(dtVoidDateTime);
			oItemDiscount.setVoidStationId(iStationId);
			oItemDiscount.setVoidUserId(iUserId);
			oItemDiscount.setVoidVdrsId(iVoidCodeId);
			oItemDiscount.setStatus(PosCheckDiscount.STATUS_DELETED);
			
			for(PosCheckDiscountItem oCheckDiscountItem : oItemDiscount.getCheckDiscountItemList())
				oCheckDiscountItem.setStatus(PosCheckDiscountItem.STATUS_DELETED);
			
			for (PosCheckExtraInfo oPosCheckExtraInfo:oItemDiscount.getCheckExtraInfoList())
				oPosCheckExtraInfo.setStatus(PosCheckExtraInfo.STATUS_DELETED);
		}
		
		return true;
	}
	
	// After using send check, update the check item return from the platform API 
	public void updateCheckItem(PosCheckItem oUpdatedCheckItem) {
		m_oCheckItem.init();
		m_oCheckItem = oUpdatedCheckItem;
		
		//update modifier list
		if(!m_oCheckItem.getModifierList().isEmpty()) {
			for(FuncCheckItem oFuncModifierItem:m_oModifierList) {
				for(PosCheckItem oModifier:m_oCheckItem.getModifierList()) {
					if(oModifier.getCitmId().equals(oFuncModifierItem.getCheckItem().getCitmId()))
						oFuncModifierItem.updateCheckItem(oModifier);
				}
			}
		}
		
		//update child item list
		if(!m_oCheckItem.getChildItemList().isEmpty()) {
			for(FuncCheckItem oFuncChildItem:m_oChildItemList) {
				for(PosCheckItem oChildItem:m_oCheckItem.getChildItemList()) {
					if(oChildItem.getCitmId().equals(oFuncChildItem.getCheckItem().getCitmId()))
						oFuncChildItem.updateCheckItem(oChildItem);
				}
			}
		}
		
		//update item discount list
		if(!m_oCheckItem.getItemDiscountList().isEmpty()) {
			m_oItemDiscountList.clear();
			for(PosCheckDiscount oItemDiscount:m_oCheckItem.getItemDiscountList())
				m_oItemDiscountList.add(oItemDiscount);
		}

		//update item extra info list
		if(!m_oCheckItem.getExtraInfoList().isEmpty()) {
			m_oExtraInfoList.clear();
			for(PosCheckExtraInfo oPosCheckExtraInfo:m_oCheckItem.getExtraInfoList())
				m_oExtraInfoList.add(oPosCheckExtraInfo);
		}
		
		// update item tax sc ref list
		if (!m_oCheckItem.getTaxScRefList().isEmpty()) {
			m_oTaxScRefList.clear();
			for (PosCheckTaxScRef oPosCheckTaxScRef : m_oCheckItem.getTaxScRefList())
				m_oTaxScRefList.add(oPosCheckTaxScRef);
		}
		
		m_oCheckItem.resetItemDiscountList();
		m_oCheckItem.resetModifierList();
		m_oCheckItem.resetChildItemList();
		m_oCheckItem.resetExtraInfoList();
		m_oCheckItem.resetTaxScRefList();
	}

	// After using send check, update the check item return from the platform API 
	public void updateOldCheckItem(PosCheckItem oUpdatedCheckItem) {
		m_oCheckItem.init();
		m_oCheckItem = oUpdatedCheckItem;
		
		if(!m_oCheckItem.getModifierList().isEmpty()) {
			m_oModifierList.clear();
			for(PosCheckItem oModifier:m_oCheckItem.getModifierList()) {
				FuncCheckItem oFuncModifierCheckItem = new FuncCheckItem(oModifier);
				m_oModifierList.add(oFuncModifierCheckItem);
			}
		}
		
		//update child item list
		if(!m_oCheckItem.getChildItemList().isEmpty()) {
			for(FuncCheckItem oFuncChildItem:m_oChildItemList) {
				for(PosCheckItem oChildItem:m_oCheckItem.getChildItemList()) {
					if(oChildItem.getSeatNo() == oFuncChildItem.getCheckItem().getSeatNo() && oChildItem.getSeq() == oFuncChildItem.getCheckItem().getSeq()) {
						oFuncChildItem.updateOldCheckItem(oChildItem);
					}
				}
			}
		}
		
		//update item discount list
		if(!m_oCheckItem.getItemDiscountList().isEmpty()) {
			m_oItemDiscountList.clear();
			for(PosCheckDiscount oItemDiscount:m_oCheckItem.getItemDiscountList())
				m_oItemDiscountList.add(oItemDiscount);
		}
		
		m_oCheckItem.resetItemDiscountList();
		m_oCheckItem.resetModifierList();
		m_oCheckItem.resetChildItemList();
		m_oCheckItem.resetExtraInfoList();
		m_oCheckItem.resetTaxScRefList();
	}
	
	// Change item's check id
	public void changeCheckAndCheckPartyId(String sChksId, String sCptyId) {
		m_oCheckItem.setCheckId(sChksId);
		
		if(!m_oModifierList.isEmpty()) {
			for(FuncCheckItem oModifier:m_oModifierList)
				oModifier.changeCheckAndCheckPartyId(sChksId, null);
		}
		
		if(!m_oItemDiscountList.isEmpty()) {
			for(PosCheckDiscount oItemDisc:m_oItemDiscountList)
				oItemDisc.setChksId(sChksId);
		}
		
		if(!m_oChildItemList.isEmpty()) {
			for(FuncCheckItem oChildItem:m_oChildItemList) {
				oChildItem.changeCheckAndCheckPartyId(sChksId, null);
			}
		}
		
		if(!m_oExtraInfoList.isEmpty()) {
			for(PosCheckExtraInfo oCheckExtraInfo:m_oExtraInfoList) {
				oCheckExtraInfo.setCheckId(sChksId);
			}
		}
		
		if (!m_oTaxScRefList.isEmpty()) {
			for (PosCheckTaxScRef oCheckTaxScRef : m_oTaxScRefList) {
				oCheckTaxScRef.setCheckId(sChksId);
			}
		}
		
		if (sCptyId != null)
			this.changeCheckPartyId(sCptyId);
	}
	
	// Change item's check party id
	private void changeCheckPartyId(String sCptyId) {
		m_oCheckItem.setCheckPartyId(sCptyId);
		
		if(!m_oModifierList.isEmpty()) {
			for(FuncCheckItem oModifier:m_oModifierList)
				oModifier.changeCheckPartyId(sCptyId);
		}
		
		if(!m_oItemDiscountList.isEmpty()) {
			for(PosCheckDiscount oItemDisc:m_oItemDiscountList)
				oItemDisc.setCptyId(sCptyId);
		}
		
		if(!m_oChildItemList.isEmpty()) {
			for(FuncCheckItem oChildItem:m_oChildItemList) {
				oChildItem.changeCheckPartyId(sCptyId);
			}
		}
	}
	
	// set original check id split from
	public void setSplitFromCheckId(String sSplitFromCheckId) {
		m_oCheckItem.setSplitFromCheckId(sSplitFromCheckId);
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
		oItemExtraInfo1.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oItemExtraInfo1.setBy(PosCheckExtraInfo.BY_ITEM);
		oItemExtraInfo1.setCheckId(m_oCheckItem.getCheckId());
		oItemExtraInfo1.setItemId(m_oCheckItem.getCitmId());
		oItemExtraInfo1.setSection(PosCheckExtraInfo.SECTION_ITEM_TYPE);
		oItemExtraInfo1.setVariable(PosCheckExtraInfo.VARIABLE_COUPON_ITEM);
		oItemExtraInfo1.setValue("y");
		m_oExtraInfoList.add(oItemExtraInfo1);
		
		PosCheckExtraInfo oItemExtraInfo2 = new PosCheckExtraInfo();
		oItemExtraInfo2.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oItemExtraInfo2.setBy(PosCheckExtraInfo.BY_ITEM);
		oItemExtraInfo2.setCheckId(m_oCheckItem.getCheckId());
		oItemExtraInfo2.setItemId(m_oCheckItem.getCitmId());
		oItemExtraInfo2.setSection(PosCheckExtraInfo.SECTION_ONLINE_COUPON);
		oItemExtraInfo2.setVariable(PosCheckExtraInfo.VARIABLE_SELL_START_COUPON);
		oItemExtraInfo2.setValue(sStartCoupon);
		m_oExtraInfoList.add(oItemExtraInfo2);
		
		PosCheckExtraInfo oItemExtraInfo3 = new PosCheckExtraInfo();
		oItemExtraInfo3.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
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
		oItemExtraInfo.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oItemExtraInfo.setCheckId(m_oCheckItem.getCheckId());
		oItemExtraInfo.setItemId(m_oCheckItem.getCitmId());
		oItemExtraInfo.setSection(sSection);
		oItemExtraInfo.setVariable(sVariable);
		oItemExtraInfo.setValue(sValue);
		m_oExtraInfoList.add(oItemExtraInfo);
		
		m_oCheckItem.setModified(true);
	}
	
	public void addTaxScRef(String sBy, String sVariable, String sValue) {
		PosCheckTaxScRef oItemTaxScRef = new PosCheckTaxScRef();
		oItemTaxScRef.setBy(sBy);
		oItemTaxScRef.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oItemTaxScRef.setBussinessPeriodId(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId());
		oItemTaxScRef.setCheckId(m_oCheckItem.getCheckId());
		oItemTaxScRef.setItemId(m_oCheckItem.getCitmId());
		oItemTaxScRef.setVariable(sVariable);
		oItemTaxScRef.setValue(sValue);
		m_oTaxScRefList.add(oItemTaxScRef);
		
		m_oCheckItem.setModified(true);
	}
	
	public void addUpdateExtraInfoValue(boolean bAddIfNotExist, String sBy, String sSection, String sVariable, String sValue, int iIndex){
		if(isCheckExtraInfoExistBySectionVariableAndIndex(sSection, sVariable, iIndex))
			updateExtraInfoValue(sBy, sSection, sVariable, sValue);
		else if(bAddIfNotExist) 
			addExtraInfo(sBy, sSection, sVariable, sValue);
	}
	
	public void updateExtraInfoValue(String sBy, String sSection, String sVariable, String sValue) {
		if(m_oExtraInfoList.isEmpty())
			return;
		
		for(PosCheckExtraInfo oCheckExtraInfo:m_oExtraInfoList) {
			if(oCheckExtraInfo.getBy().equals(sBy) && oCheckExtraInfo.getSection().equals(sSection) && oCheckExtraInfo.getVariable().equals(sVariable)) {
				oCheckExtraInfo.setValue(sValue);
				m_oCheckItem.setModified(true);
				return;
			}
		}
	}
	
	public void updateTaxScRefValue(String sBy, String sVariable, String sValue) {
		if (m_oTaxScRefList.isEmpty())
			return;
		
		for (PosCheckTaxScRef oCheckTaxScRef : m_oTaxScRefList) {
			if (oCheckTaxScRef.getBy().equals(sBy) && oCheckTaxScRef.getVariable().equals(sVariable)) {
				oCheckTaxScRef.setValue(sValue);
				m_oCheckItem.setModified(true);
				return;
			}
		}
	}
	
	public void updateExtraInfoStatus(String sBy, String sSection, String sVariable, String sStatus) {
		if(m_oExtraInfoList.isEmpty())
			return;
		
		for(PosCheckExtraInfo oCheckExtraInfo:m_oExtraInfoList) {
			if(oCheckExtraInfo.getBy().equals(sBy) && oCheckExtraInfo.getSection().equals(sSection) && oCheckExtraInfo.getVariable().equals(sVariable)) {
				oCheckExtraInfo.setStatus(sStatus);
				if(oCheckExtraInfo.getCkeiId().compareTo("") > 0)
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

	// Get the variable whatever below which section
	public String getExtraInfoByVariable(String sVariable) {
		String sValue = null;
		for(PosCheckExtraInfo oExtraInfo:m_oExtraInfoList) {
			if(oExtraInfo.getVariable().equals(sVariable)) {
				sValue = oExtraInfo.getValue();
				break;
			}
		}
		return sValue;
	}

	public boolean isCheckExtraInfoExistBySectionVariableAndIndex(String sSection, String sVariable, int iIndex) {
		boolean bFound = false;
		
		for(PosCheckExtraInfo oCheckExtraInfo:m_oExtraInfoList) {
			if(oCheckExtraInfo.getSection().equals(sSection) && oCheckExtraInfo.getVariable().equals(sVariable) && oCheckExtraInfo.getIndex() == iIndex) {
				bFound = true;
				break;
			}
		}
		return bFound;
	}

	// Get pantry message list
	public List<Integer> getPantryMessageList() {
		JSONObject oTempPantryMessageJSONObject = new JSONObject();
		JSONArray oPantryMessageIdJSONArray = new JSONArray();
		List<Integer> oPantryMessageIdMessageList = new ArrayList<Integer>();
		
		for (PosCheckExtraInfo oPosCheckExtraInfo: m_oExtraInfoList) {
			if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_PANTRY_MESSAGE)) {
				try {
					oPantryMessageIdJSONArray = new JSONArray(oPosCheckExtraInfo.getValue().trim());
					for (int i = 0; i < oPantryMessageIdJSONArray.length(); i++) {
						oTempPantryMessageJSONObject = oPantryMessageIdJSONArray.getJSONObject(i);
						oPantryMessageIdMessageList.add(oTempPantryMessageJSONObject.getInt("id"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return oPantryMessageIdMessageList;
	}
	
	// Get pantry message list
	public List<String> getDisplayInformationList() {
		List<String> oDisplayInformationList = new ArrayList<String>();
		
		for (PosCheckExtraInfo oPosCheckExtraInfo: m_oExtraInfoList) {
			if (oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_CARD_NO) && oPosCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_LOYALTY)) {
				oDisplayInformationList.add(AppGlobal.g_oLang.get()._("number") + " " + oPosCheckExtraInfo.getValue());
				break;
			}
		}
		return oDisplayInformationList;
	}
	
	public void resetAsNewItem() {
		String sOrderTime = "";
		DateTime oApplyTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		sOrderTime = oFormatter.print(AppGlobal.convertTimeToUTC(oApplyTime));
		
		// reset check item id, order time
		this.m_oCheckItem.setCheckItemId("");
		this.m_oCheckItem.setBusinessDayId(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
		this.m_oCheckItem.setBusinessPeriodId(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId());
		this.m_oCheckItem.setOrderTime(sOrderTime);
		this.m_oCheckItem.setOrderLocTime(oApplyTime);
		
		// reset modifier item id, order time
		for (FuncCheckItem oModiFuncCheckItem : this.m_oModifierList) {
			oModiFuncCheckItem.getCheckItem().setCheckItemId("");
			oModiFuncCheckItem.getCheckItem().setParentItemId("");
			oModiFuncCheckItem.getCheckItem().setBusinessDayId(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
			oModiFuncCheckItem.getCheckItem().setBusinessPeriodId(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId());
			oModiFuncCheckItem.getCheckItem().setOrderTime(sOrderTime);
			oModiFuncCheckItem.getCheckItem().setOrderLocTime(oApplyTime);
		}
		
		// reset check discount item id
		for (PosCheckDiscountItem oPosCheckDiscountItem: this.m_oCheckDiscountItemList) {
			oPosCheckDiscountItem.setCditId("");
			oPosCheckDiscountItem.setCitmId(this.m_oCheckItem.getCitmId());
		}
		
		// reset check tax sc ref id
		for (PosCheckTaxScRef oItemTaxScRef: this.m_oTaxScRefList) {
			oItemTaxScRef.setCtsrId("");
			oItemTaxScRef.setCheckId("");
			oItemTaxScRef.setItemId("");
		}
		
		// reset discount id, order time
		for (PosCheckDiscount oPosCheckDiscount : this.m_oItemDiscountList) {
			oPosCheckDiscount.setCdisId("");
			oPosCheckDiscount.setCitmId(this.m_oCheckItem.getCitmId());
			for (PosCheckDiscountItem oPosCheckDiscountItem : oPosCheckDiscount.getCheckDiscountItemList()) {
				oPosCheckDiscountItem.setCditId("");
				oPosCheckDiscountItem.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
				oPosCheckDiscountItem.setCdisId("");
				oPosCheckDiscountItem.setCitmId(this.m_oCheckItem.getCitmId());
			}
			oPosCheckDiscount.setBdayId(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
			oPosCheckDiscount.setBperId(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId());
			oPosCheckDiscount.setApplyLocalTime(oApplyTime);
			oPosCheckDiscount.setApplyTime(sOrderTime);
			oPosCheckDiscount.setApplyUserId(AppGlobal.g_oFuncUser.get().getUserId());
			oPosCheckDiscount.setApplyStationId(AppGlobal.g_oFuncStation.get().getStationId());
		}
		
		// reset ckei_id and ckei_citm_id of PosCheckExtraInfo record
		for (PosCheckExtraInfo oPosCheckExtraInfo: this.m_oExtraInfoList) {
			oPosCheckExtraInfo.setCkeiId("");
			oPosCheckExtraInfo.setItemId(m_oCheckItem.getCitmId());
		}
	}
	
	public LinkedList<PosActionLog> getActionLogList() {
		return m_oActionLogList;
	}
	
	public void addActionLog(String sKey, String sResult, String sTable, int iUserId, int iShopId, int iOletId, String sBdayId, String sBperId, int iStationId, String sChksId, String sCptyId, String sCitmId, String sCdisId, String sCpayId, String sRemark) {
		PosActionLog oNewActionLog = new PosActionLog();
		DateTime oCurrentDateTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		oNewActionLog.setActionLocalTime(oCurrentDateTime);
		oNewActionLog.setActionTime(fmt.print(AppGlobal.convertTimeToUTC(oCurrentDateTime)));
		
		oNewActionLog.setKey(sKey);
		oNewActionLog.setUserId(iUserId);
		oNewActionLog.setActionResult(sResult);
		oNewActionLog.setTable(sTable);
		oNewActionLog.setRecordId(0);
		oNewActionLog.setShopId(iShopId);
		oNewActionLog.setOletId(iOletId);
		oNewActionLog.setBdayId(sBdayId);
		oNewActionLog.setBperId(sBperId);
		oNewActionLog.setStatId(iStationId);
		oNewActionLog.setChksId(sChksId);
		oNewActionLog.setCptyId(sCptyId);
		oNewActionLog.setCitmId(sCitmId);
		oNewActionLog.setCdisId(sCdisId);
		oNewActionLog.setCpayId(sCpayId);
		oNewActionLog.setRemark(sRemark);

		try {
			m_oActionLogList.addLast(oNewActionLog);
		} catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	public void updateActionLog(String sChksId, String sCitmId, String sCdisId) {
		try {
			for (PosActionLog oActionLog:m_oActionLogList) {
				if (sChksId.compareTo("") != 0)
					oActionLog.setChksId(sChksId);
				if (sCitmId.compareTo("") != 0)
					oActionLog.setCitmId(sCitmId);
				if (sCdisId.compareTo("") != 0)
					oActionLog.setCdisId(sCdisId);
			}
		} catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	public void setStatus(String sStatus) {
		m_oCheckItem.setStatus(sStatus);
	}
	
	public void setItemAuthorizedUserNum(String sUserNum){
		m_oCheckItem.setItemAuthorizedUserNum(sUserNum);
	}
	
	public boolean hasMatchedMixAndMatchRule() {
		if(this.getMixAndMatchRuleId() > 0 || !this.getMixAndMatchItemList().isEmpty() || !m_oCheckItem.getMixAndMatchItemId().equals("")) {
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
	
	public boolean isModified() {
		return m_oCheckItem.getModified();
	}

	public void rollBackVoidOldItemDiscount(int iDiscountIndex) {
		PosCheckDiscount oCheckDiscount = this.m_oItemDiscountList.get(iDiscountIndex);

		oCheckDiscount.setVoidLocTime(null);
		oCheckDiscount.setVoidTime(null);
		oCheckDiscount.setVoidUserId(0);
		oCheckDiscount.setVoidStatId(0);
		oCheckDiscount.setVoidVdrsId(0);
		oCheckDiscount.setStatus(PosCheckDiscount.STATUS_ACTIVE);

		for (PosCheckDiscountItem oCheckDiscountItem : oCheckDiscount.getCheckDiscountItemList())
			oCheckDiscountItem.setStatus(PosCheckDiscountItem.STATUS_ACTIVE);

		for (PosCheckExtraInfo oPosCheckExtraInfo : oCheckDiscount.getCheckExtraInfoList())
			oPosCheckExtraInfo.setStatus(PosCheckExtraInfo.STATUS_ACTIVE);
	}
	
	public BigDecimal getItemNetTotal(boolean bIncludeCheckDiscount){
		BigDecimal dNetTotal = m_oCheckItem.getRoundTotal();
		
		//item discount
		for(int i=0; i<this.m_oItemDiscountList.size(); i++)
			dNetTotal = dNetTotal.add(this.m_oItemDiscountList.get(i).getRoundTotal());
		
		//check discount
		if(bIncludeCheckDiscount){
			for(int i=0; i<this.m_oCheckDiscountItemList.size();i++)
				dNetTotal = dNetTotal.add(this.m_oCheckDiscountItemList.get(i).getRoundTotal());
		}
		
		return dNetTotal;
	}
	
	// get the item original total without check and item discount
	public BigDecimal getItemNetTotalWithoutExtraCharge(ArrayList<Integer> oCheckDiscountSeqs){
		// item total
		BigDecimal dNetTotal = m_oCheckItem.getTotal();
		
		// item discount
		for(int i=0; i<this.m_oItemDiscountList.size(); i++)
			dNetTotal = dNetTotal.add(this.m_oItemDiscountList.get(i).getTotal());
		
		// check discount
		for(PosCheckDiscountItem oCheckDiscountItemList: m_oCheckDiscountItemList) {
			if(oCheckDiscountSeqs.contains(oCheckDiscountItemList.getCdisSeq()))
				dNetTotal = dNetTotal.add(oCheckDiscountItemList.getTotal());
		}
		
		return dNetTotal;
	}
	
	//set the item discount list for PMS
	public void setItemDiscountListForPmsPosting() {
		if(this.hasItemDiscount(true)) {
			List<PosCheckDiscount> oItemDiscountList = new ArrayList<PosCheckDiscount>();
			
			for(PosCheckDiscount oCheckDiscount:m_oItemDiscountList) 
				oItemDiscountList.add(oCheckDiscount);
			this.getCheckItem().setItemDiscountList(oItemDiscountList);
		}
	}

	//set business period
	public void setBusinessPeriod(String sBuniessPeriod){
		this.getCheckItem().setBusinessPeriodId(sBuniessPeriod);
	}
	
	public void setModified(boolean bModified) {
		m_oCheckItem.setModified(bModified);
	}
	
	//calculate the item's cooking time
	public void calculateCookingTime(DateTime oStartTime){
		if(AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId()).getMenuItem() == null 
				|| AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId()).getMenuItem().getCookingTime() == 0
				|| isPendingItem() && !isPartialPendingItem() || isNoKitchenSlip())
			return;
		
		DateTime oResultFinishTime = oStartTime.plusMinutes(AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemId(m_oCheckItem.getItemId()).getMenuItem().getCookingTime());
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		JSONObject oFinishTime = new JSONObject();
		try {
			oFinishTime.put("finish_time", dateFormat.print(oResultFinishTime));
		} catch (JSONException exception) {
			exception.printStackTrace();
			AppGlobal.stack2Log(exception);
		}
		
		if(!isExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_COOKING_INFO, 0))
			addExtraInfo(PosCheckExtraInfo.BY_ITEM, PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_COOKING_INFO, oFinishTime.toString());
		m_oCheckItem.setModified(true);
	}
	
	//is item's cooking time overdue
	public boolean isCookingTimeOverdue() {
		boolean bOverTime = false;
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		JSONObject oJSONValues;
		PosCheckExtraInfo oCheckExtraInfo = null;
		
		//return false if item is delivered or don't have such extra info - cooking_info
		if (hasDelivered() || !isExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_COOKING_INFO, 0))
			return bOverTime;
		
		oCheckExtraInfo = getExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_COOKING_INFO, 0);
		if (oCheckExtraInfo.getValue() == null || oCheckExtraInfo.getValue().isEmpty())
			return bOverTime;
		
		try {
			oJSONValues = new JSONObject(oCheckExtraInfo.getValue());
			if (!oJSONValues.has("finish_time") || oJSONValues.optString("finish_time", "").isEmpty())
				return bOverTime;
			
			DateTime dtFinishDateTime = formatter.parseDateTime(oJSONValues.optString("finish_time", ""));
			if (!dtFinishDateTime.isAfterNow())
				bOverTime = true;
		} catch (JSONException exception) {
			exception.printStackTrace();
			AppGlobal.stack2Log(exception);
		}
		
		return bOverTime;
	}
	
	//check whether a specific extra info exist or not
	public boolean isExtraInfoExistBySectionVariableAndIndex(String sSection, String sVariable, int iIndex){
		if(m_oExtraInfoList.isEmpty())
			return false;
		
		for(PosCheckExtraInfo oPosCheckExtraInfo : m_oExtraInfoList) {
			if (oPosCheckExtraInfo.getSection().equals(sSection) && oPosCheckExtraInfo.getVariable().equals(sVariable) 
					&& oPosCheckExtraInfo.getIndex() == iIndex)
				return true;
		}
		
		return false;
	}
	
	//check whether a specific tax sc ref exist or not
	public boolean isTaxScRefExistBySectionVariable(String sVariable) {
		if (m_oTaxScRefList.isEmpty())
			return false;
		
		boolean bFound = false;
		for (PosCheckTaxScRef oPosCheckTaxScRef : m_oTaxScRefList) {
			if (oPosCheckTaxScRef.getVariable().equals(sVariable)) {
				bFound = true;
				break;
			}
		}
		
		return bFound;
	}
	
	//get specific extra info by section, variable name and index
	public PosCheckExtraInfo getExtraInfoExistBySectionVariableAndIndex(String sSection, String sVariable, int iIndex) {
		PosCheckExtraInfo oPosCheckExtraInfo = null;
		for (PosCheckExtraInfo oCheckExtraInfo : m_oExtraInfoList) {
			if (oCheckExtraInfo.getSection().equals(sSection) && oCheckExtraInfo.getVariable().equals(sVariable)
					&& oCheckExtraInfo.getIndex() == iIndex) {
				oPosCheckExtraInfo = oCheckExtraInfo;
				break;
			}
		}
		
		return oPosCheckExtraInfo;
	}
	
	//get specific tax sc reference by variable name
	public BigDecimal getTaxScRefByVariable(String sVariable) {
		if(m_oTaxScRefList.isEmpty())
			return BigDecimal.ZERO;
		
		BigDecimal dValue = BigDecimal.ZERO;
		for (PosCheckTaxScRef oPosCheckTaxScRef : m_oTaxScRefList) {
			if (oPosCheckTaxScRef.getVariable().equals(sVariable)) {
				try {
					dValue = new BigDecimal(oPosCheckTaxScRef.getValue());
				}catch(Exception e) {}
				break;
			}
		}
		
		return dValue;
	}

	public int getChildItemCountInSameSection(){
		int iCount = 0;
		for(FuncCheckItem oCheckItem : this.getChildItemList()){
			if(this.getCheckItem().getSeatNo() == oCheckItem.getCheckItem().getSeatNo())
				iCount++;
		}
		return iCount;
	}
	
	//calculate the inclusive tax total
	public BigDecimal getInclusiveTaxTotal(boolean bRound) {
		BigDecimal dTotalInclusiveTaxRef = BigDecimal.ZERO;
		for(int i = 1; i <= 4; i++) {
			if(bRound)
				dTotalInclusiveTaxRef = dTotalInclusiveTaxRef.add(AppGlobal.g_oFuncOutlet.get().roundTaxAmountToBigDecimal(m_oCheckItem.getInclusiveTaxRef(i)));
			else
				dTotalInclusiveTaxRef = dTotalInclusiveTaxRef.add(m_oCheckItem.getInclusiveTaxRef(i));
		}
		return dTotalInclusiveTaxRef;
	}
	
	// Get Total Tax of Check Item
	public BigDecimal getTaxTotal(boolean bRound) {
		BigDecimal dTaxTotal = BigDecimal.ZERO;
		for(int i=1; i<=25; i++){
			if(bRound)
				dTaxTotal = dTaxTotal.add(AppGlobal.g_oFuncOutlet.get().roundTaxAmountToBigDecimal(m_oCheckItem.getTax(i)));
			else
				dTaxTotal = dTaxTotal.add(m_oCheckItem.getTax(i));
		}
		return dTaxTotal;
	}
	
	// Get specific tax value between 1-25
	public BigDecimal getTaxAmount(int iIndex, boolean bRound){
		BigDecimal dTaxAmt = BigDecimal.ZERO;
		if(bRound)
			dTaxAmt = dTaxAmt.add(AppGlobal.g_oFuncOutlet.get().roundTaxAmountToBigDecimal(m_oCheckItem.getTax(iIndex)));
		else
			dTaxAmt = dTaxAmt.add(m_oCheckItem.getTax(iIndex));
		return dTaxAmt;
	}
	
	// Get specific inclusive tax value between 1-4
	public BigDecimal getInclusiveTaxAmount(int iIndex, boolean bRound){
		BigDecimal dInclusiveTaxAmt = BigDecimal.ZERO;
		if(bRound)
			dInclusiveTaxAmt = dInclusiveTaxAmt.add(AppGlobal.g_oFuncOutlet.get().roundTaxAmountToBigDecimal(m_oCheckItem.getInclusiveTaxRef(iIndex)));
		else
			dInclusiveTaxAmt = dInclusiveTaxAmt.add(m_oCheckItem.getInclusiveTaxRef(iIndex));
		return dInclusiveTaxAmt;
	}
	
	// Get specific sc value
	public BigDecimal getScAmount(int iIndex, boolean bRound){
		BigDecimal dScAmt = BigDecimal.ZERO;
		if(bRound)
			dScAmt = dScAmt.add(AppGlobal.g_oFuncOutlet.get().roundSCAmountToBigDecimal(m_oCheckItem.getSc(iIndex)));
		else
			dScAmt = dScAmt.add(m_oCheckItem.getSc(iIndex));
		return dScAmt;
	}
	
	// Get sc total
	public BigDecimal getScTotal(boolean bRound){
		BigDecimal dScTotal = BigDecimal.ZERO;
		for(int i = 1; i <= 5; i++){
			if(bRound)
				dScTotal = dScTotal.add(AppGlobal.g_oFuncOutlet.get().roundSCAmountToBigDecimal(m_oCheckItem.getSc(i)));
			else
				dScTotal = dScTotal.add(m_oCheckItem.getSc(i));
		}
		return dScTotal;
	}
	
	public BigDecimal getModiOldQtyForSplit() {
		return this.m_dModiOldQtyForSplit;
	}
	
	// prepare parameters of Modifier Old Qty for split
	public void prepareParamOfModiOldQtyForSplit() {
		for (int i = 0; i < this.getModifierList().size(); i++) {
			FuncCheckItem oFuncModiCheckItem = this.getModifierList().get(i);
			if(m_oCheckItem.getModifierList().get(i).getQty().compareTo(BigDecimal.ZERO) != 0) {
				oFuncModiCheckItem.setModiOldQtyForSplit(m_oCheckItem.getModifierList().get(i).getQty());
				oFuncModiCheckItem.setModiSplitFlag(true);
			}
		}
	}
	
	// Get order local time
	public DateTime getOrderLocTime() {
		return m_oCheckItem.getOrderLocTime();
	}
	
	public boolean isMinimumChargeItem() {
		return this.isCheckExtraInfoExistBySectionVariableAndIndex("", PosCheckExtraInfo.VARIABLE_MIN_CHARGE_ITEM, 0);
	}
	
	public void removeAddWaivedScTaxExtraInfo() {
		JSONObject oAddWaiveObject;
		JSONObject oTempJSONObject;

		if (this.isExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_ADD_WAIVE_TAX_SC, 0)) {
			try {
				oAddWaiveObject = new JSONObject(this.getExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_ADD_WAIVE_TAX_SC, 0).getValue());
				oTempJSONObject = new JSONObject();
				
				oAddWaiveObject.put("sc", oTempJSONObject);
				oAddWaiveObject.put("tax", oTempJSONObject);

				// Update Extra Info
				this.updateExtraInfoValue(PosCheckExtraInfo.BY_ITEM, PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_ADD_WAIVE_TAX_SC, oAddWaiveObject.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public BigDecimal getBreakdownPrice() {
		return m_dBreakdownPrice;
	}
	
	public void setBreakdownPrice(BigDecimal dPrice) {
		m_dBreakdownPrice = dPrice; 
	}
	
	// set discount on sc or tax
	// If iIndex == 0, meaning update whole list with provided value
	public void setDiscOnScTax(boolean bTax, String sType, int iIndex, BigDecimal dValue, boolean bUpdateScTaxRefRecord) {
		BigDecimal[] dDiscOnScTax = null;
		String sScTaxRefKey = "round_";
		int iCount = AppGlobal.SC_COUNT;
		if(sType.equals(PosDiscountType.TYPE_POST_DISCOUNT))
			sScTaxRefKey = sScTaxRefKey.concat("post_disc_");
		else if(sType.equals(PosDiscountType.TYPE_MID_DISCOUNT))
			sScTaxRefKey = sScTaxRefKey.concat("mid_disc_");
		else
			sScTaxRefKey = sScTaxRefKey.concat("pre_disc_");
		
		if(!bTax) {
			dDiscOnScTax = m_oDiscOnSc.get(sType);
			sScTaxRefKey = sScTaxRefKey.concat("on_sc_ref");
		}else {
			dDiscOnScTax = m_oDiscOnTax.get(sType);
			sScTaxRefKey = sScTaxRefKey.concat("on_tax_ref");
			iCount = AppGlobal.TAX_COUNT;
		}
		
		if(iIndex == 0) {
			for(int i=1; i<=iCount; i++) {
				dDiscOnScTax[i-1] = dValue;
				String sTmpScTaxRefKey = sScTaxRefKey.concat(String.valueOf(i));
				if(bUpdateScTaxRefRecord && isTaxScRefExistBySectionVariable(sTmpScTaxRefKey))
					updateTaxScRefValue(PosCheckTaxScRef.BY_ITEM, sTmpScTaxRefKey, StringLib.BigDecimalToString(dValue, 10));
			}
		}else {
			dDiscOnScTax[iIndex-1] = dValue;
			sScTaxRefKey = sScTaxRefKey.concat(String.valueOf(iIndex));
			if(bUpdateScTaxRefRecord && isTaxScRefExistBySectionVariable(sScTaxRefKey))
				updateTaxScRefValue(PosCheckTaxScRef.BY_ITEM, sScTaxRefKey, StringLib.BigDecimalToString(dValue, 10));
		}
	}
	
	// get discount on sc or tax
	public BigDecimal getDiscOnScTax(boolean bTax, String sType, int iIndex) {
		BigDecimal[] dDiscOnScTax = null;
		if(!bTax)
			dDiscOnScTax = m_oDiscOnSc.get(sType);
		else
			dDiscOnScTax = m_oDiscOnTax.get(sType);
		
		if(dDiscOnScTax[iIndex-1] == null)
			return BigDecimal.ZERO;
		return dDiscOnScTax[iIndex-1];
	}
	
	public BigDecimal calculateScTaxRateBaseOnDiscount(String sType, String sDiscIncdeTasScMask, BigDecimal dBaseTotal, HashMap<Integer, PosTaxScType> oScTypes, HashMap<Integer, PosTaxScType> oTaxTypes, HashMap<String, BigDecimal[]> oScTaxInclusiveRateNoBreakdown) {
		String sDiscScTaxMask = StringLib.fillZero(sDiscIncdeTasScMask, 30);
		BigDecimal[] dRatesForInclusiveScNoBreakdown = oScTaxInclusiveRateNoBreakdown.get("sc");
		BigDecimal[] dRatesForInclusiveTaxNoBreakdown = oScTaxInclusiveRateNoBreakdown.get("tax");
		
		for(int i=1; i<=AppGlobal.SC_COUNT; i++){
			boolean bInclude = false;
			if(sType.equals(PosDiscountType.TYPE_PRE_DISCOUNT)) {
				if(m_oCheckItem.isScInclusiveNoBreakdown(i))
					bInclude = true;
			}else if(sDiscScTaxMask.substring((i-1), i).equals(PosCheckDiscount.INCLUDE_SC_TAX_YES))
				bInclude = true;
			
			dRatesForInclusiveScNoBreakdown[i-1] = BigDecimal.ZERO;
			if(bInclude){
				dBaseTotal = dBaseTotal.add(m_oCheckItem.getSc(i));
				if(m_oCheckItem.isScTaxInclusiveNoBreakdown()) {
					BigDecimal dRate = oScTypes.get(i-1).getRate();
					
					String sTaxScMask = StringLib.fillZero(oScTypes.get(i-1).getIncludeTaxScMask(), 30);
					for(int j=1; j<i; j++){
						if (sTaxScMask.substring((j-1), j).equals(PosTaxScType.INCLUDE_PREVIOUS_SC_TAX_YES)) {
							if(m_oCheckItem.getChargeSc(i).equals(PosCheckItem.CHARGE_SC_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN))
								dRate = dRate.add(oScTypes.get(j-1).getRateForInclusiveNoBreakdown().multiply(oScTypes.get(i-1).getRate()));
						}
					}
					dRatesForInclusiveScNoBreakdown[i-1] = dRatesForInclusiveScNoBreakdown[i-1].add(dRate); 
				}
			}
		}
		
		for(int i=1; i<=AppGlobal.TAX_COUNT; i++){
			boolean bInclude = false;
			if(sType.equals(PosDiscountType.TYPE_PRE_DISCOUNT)) {
				if(m_oCheckItem.isTaxInclusiveNoBreakdown(i))
					bInclude = true;
			}else if(sDiscScTaxMask.substring(4+i, 4+i+1).equals(PosCheckDiscount.INCLUDE_SC_TAX_YES))
				bInclude = true;
			
			dRatesForInclusiveTaxNoBreakdown[i-1] = BigDecimal.ZERO;
			if(bInclude){
				dBaseTotal = dBaseTotal.add(m_oCheckItem.getTax(i));
				if(m_oCheckItem.isScTaxInclusiveNoBreakdown()) {
					BigDecimal dRate = oTaxTypes.get(i-1).getRate();
					
					String sTaxScMask = StringLib.fillZero(oTaxTypes.get(i-1).getIncludeTaxScMask(), 30);
					for(int j=1; j<=5; j++){
						if (sTaxScMask.substring((j-1), j).equals(PosTaxScType.INCLUDE_PREVIOUS_SC_TAX_YES)) {
							if(m_oCheckItem.getChargeSc(j).equals(PosCheckItem.CHARGE_SC_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN))
								dRate = dRate.add(oScTypes.get(j-1).getRateForInclusiveNoBreakdown().multiply(oTaxTypes.get(i-1).getRate()));
						}
					}
					for (int j=1; j<i; j++) {
						if (sTaxScMask.substring(4+j, 4+j+1).equals(PosTaxScType.INCLUDE_PREVIOUS_SC_TAX_YES)) {
							if (m_oCheckItem.getChargeTax(j).equals(PosCheckItem.CHARGE_TAX_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN))
								dRate = dRate.add(oTaxTypes.get(j-1).getRateForInclusiveNoBreakdown().multiply(oTaxTypes.get(i-1).getRate()));
						}
					}
					dRatesForInclusiveTaxNoBreakdown[i-1] = dRatesForInclusiveTaxNoBreakdown[i-1].add(dRate); 
				}
			}
		}
		
		return dBaseTotal;
	}
	
	public void breakdownDiscountItemValue(PosCheckDiscount oCheckDiscount, PosCheckDiscountItem oCheckDiscountItem, BigDecimal dBaseRate, HashMap<String, BigDecimal[]> oScTaxInclusiveRateNoBreakdown) {
		BigDecimal[] dScRates = oScTaxInclusiveRateNoBreakdown.get("sc");
		BigDecimal[] dTaxRates = oScTaxInclusiveRateNoBreakdown.get("tax");
		
		String sDiscIncludeScTax =  StringLib.fillZero(oCheckDiscount.getIncludeTaxScMask(), 30);
		BigDecimal dBasePrice = oCheckDiscountItem.getTotal().divide(dBaseRate.add(BigDecimal.ONE), 10, RoundingMode.HALF_UP);
		BigDecimal dTmpRoundDisc = AppGlobal.g_oFuncOutlet.get().roundDiscAmountToBigDecimal(dBasePrice); 
		
		oCheckDiscountItem.setItemSeatSeq(m_oCheckItem.getSeatNo(), m_oCheckItem.getSeq());
		oCheckDiscountItem.setDiscOnSc(false, 0, BigDecimal.ZERO);
		oCheckDiscountItem.setDiscOnSc(true, 0, BigDecimal.ZERO);
		oCheckDiscountItem.setDiscOnTax(false, 0, BigDecimal.ZERO);
		oCheckDiscountItem.setDiscOnTax(true, 0, BigDecimal.ZERO);
		oCheckDiscountItem.setRoundDiscBase(dTmpRoundDisc);
		oCheckDiscountItem.setDiscBase(dBasePrice);
		
		// break down discount amount into sc
		for(int i=1; i<=AppGlobal.SC_COUNT; i++) {
			boolean bInclude = false;
			if(oCheckDiscount.getType().equals(PosDiscountType.TYPE_PRE_DISCOUNT)) {
				if(m_oCheckItem.isScInclusiveNoBreakdown(i))
					bInclude = true;
			}else if(sDiscIncludeScTax.substring((i-1), i).equals(PosCheckDiscount.INCLUDE_SC_TAX_YES) && !m_oCheckItem.getChargeSc(i).equals(PosCheckItem.CHARGE_SC_NO))
				bInclude = true;
			
			if(bInclude) {
				BigDecimal dDiscOnSc = dBasePrice.multiply(dScRates[i-1]);
				oCheckDiscountItem.setDiscOnSc(false, i, dDiscOnSc);
				oCheckDiscountItem.setDiscOnSc(true, i, AppGlobal.g_oFuncOutlet.get().roundDiscAmountToBigDecimal(dDiscOnSc.abs()).multiply(new BigDecimal("-1")));
				dTmpRoundDisc = dTmpRoundDisc.add(oCheckDiscountItem.getDiscOnSc(true, i));
			}
		}
		
		// break down discount amount into tax
		for(int i=1; i<=AppGlobal.TAX_COUNT; i++) {
			boolean bInclude = false;
			if(oCheckDiscount.getType().equals(PosDiscountType.TYPE_PRE_DISCOUNT)) {
				if(m_oCheckItem.isTaxInclusiveNoBreakdown(i))
					bInclude = true;
			}else if(sDiscIncludeScTax.substring(4+i, 4+i+1).equals(PosCheckDiscount.INCLUDE_SC_TAX_YES) && !m_oCheckItem.getChargeTax(i).equals(PosCheckItem.CHARGE_TAX_NO))
				bInclude = true;
			
			if(bInclude){
				BigDecimal dDiscOnTax = dBasePrice.multiply(dTaxRates[i-1]);
				oCheckDiscountItem.setDiscOnTax(false, i, dDiscOnTax);
				oCheckDiscountItem.setDiscOnTax(true, i, AppGlobal.g_oFuncOutlet.get().roundDiscAmountToBigDecimal(dDiscOnTax.abs()).multiply(new BigDecimal("-1")));
				dTmpRoundDisc = dTmpRoundDisc.add(oCheckDiscountItem.getDiscOnTax(true, i));
			}
		}
		
		// calculate the rounding
		/*if(oCheckDiscountItem.getRoundTotal().compareTo(dTmpRoundDisc) != 0) {
			BigDecimal dRounding = oCheckDiscountItem.getRoundTotal().subtract(dTmpRoundDisc);
			boolean bRoundingHandled = false;
			for(int j=1; j<=AppGlobal.SC_COUNT; j++) {
				if(oCheckDiscountItem.getDiscOnSc(true, j).compareTo(BigDecimal.ZERO) != 0) {
					oCheckDiscountItem.setDiscOnSc(true, j, oCheckDiscountItem.getDiscOnSc(true, j).add(dRounding));
					bRoundingHandled = true;
					break;
				}
			}
			
			if(!bRoundingHandled) {
				for(int j=1; j<=AppGlobal.TAX_COUNT; j++) {
					if(oCheckDiscountItem.getDiscOnTax(true, j).compareTo(BigDecimal.ZERO) != 0) {
						oCheckDiscountItem.setDiscOnTax(true, j, oCheckDiscountItem.getDiscOnTax(true, j).add(dRounding));
						bRoundingHandled = true;
					}
				}
			}
		}*/
		
		// update self stored value
		BigDecimal[] dDiscOnSc = m_oDiscOnSc.get(oCheckDiscount.getType());
		for(int i=1; i<=AppGlobal.SC_COUNT; i++)
			dDiscOnSc[i-1] = dDiscOnSc[i-1].add(oCheckDiscountItem.getDiscOnSc(true, i));
		
		BigDecimal[] dDiscOnTax = m_oDiscOnTax.get(oCheckDiscount.getType());
		for(int i=1; i<=AppGlobal.TAX_COUNT; i++)
			dDiscOnTax[i-1] = dDiscOnTax[i-1].add(oCheckDiscountItem.getDiscOnTax(true, i));
	}
	
	// swipe the inclusive sc or tax to breakdown value
	public void swipeBreakdownValue(boolean bBreakdown) {
		if(bBreakdown) {
			if((m_oCheckItem.isBasicItem() || (m_oCheckItem.isSetMenuChildItem() && isSumUpChildItemToParent()))
					&& !m_oCheckItem.isScTaxInclusiveNoBreakdown())
				return;
			
			// Process item modifiers
			BigDecimal dModifierCarryTotal = BigDecimal.ZERO;
			ArrayList<FuncCheckItem> oModifierList = getModifierList();
			for(FuncCheckItem oModifierFuncCheckItem: oModifierList) {
				oModifierFuncCheckItem.swipeBreakdownValue(bBreakdown);
				dModifierCarryTotal = dModifierCarryTotal.add(oModifierFuncCheckItem.getCheckItem().getCarryTotal());
			}
			
			// Process item child
			BigDecimal dChildCarryTotal = BigDecimal.ZERO;
			if (isCalculateMethodSumUp()) {
				ArrayList<FuncCheckItem> oChildList = getChildItemList();
				for(FuncCheckItem oChildFuncCheckItem: oChildList) {
					oChildFuncCheckItem.swipeBreakdownValue(bBreakdown);
					dChildCarryTotal = dChildCarryTotal.add(oChildFuncCheckItem.getCheckItem().getCarryTotal());
				}
			}
			
			addUpdateExtraInfoValue(true, PosCheckExtraInfo.BY_ITEM, PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_NON_BREAKDOWN_DETAILS, "", 0);
			BigDecimal dNewRoundTotal = m_oCheckItem.getRoundTotal(), dNewRoundAmt = BigDecimal.ZERO;
			BigDecimal dTotal = m_dBreakdownPrice.multiply(m_oCheckItem.getQty());
			
			dTotal = dTotal.add(dModifierCarryTotal);
			dTotal = dTotal.add(dChildCarryTotal);
			dNewRoundTotal = AppGlobal.g_oFuncOutlet.get().roundItemAmountToBigDecimal(dTotal);
			dNewRoundAmt = dNewRoundTotal.subtract(dTotal);
		
			// save extra info for non breakdown details
			addUpdateExtraInfoValue(true, PosCheckExtraInfo.BY_ITEM, PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_NON_BREAKDOWN_DETAILS, m_oCheckItem.getBasicNoBreakdownItemDetails(m_oTaxScRefList).toString(), 0);
			
			if(m_oCheckItem.isModifierItem() || (m_oCheckItem.isSetMenuChildItem() && isSumUpChildItemToParent()))
				m_oCheckItem.setCarryTotal(m_dBreakdownPrice.multiply(m_oCheckItem.getQty()));
			else {
				m_oCheckItem.setRoundTotal(dNewRoundTotal);
				m_oCheckItem.setTotal(m_dBreakdownPrice);
				m_oCheckItem.setRoundAmount(dNewRoundAmt);
			}
			m_oCheckItem.setPrice(m_dBreakdownPrice);
			m_oCheckItem.setOriginalPrice(m_dBreakdownPrice);
			for(int i=1; i<AppGlobal.SC_COUNT; i++) {
				m_oCheckItem.setSc(i, getTaxScRefByVariable("incl_sc_ref"+i));
				if(m_oCheckItem.getChargeSc(i).equals(PosCheckItem.CHARGE_SC_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN))
					m_oCheckItem.setChargeSc(i, PosCheckItem.CHARGE_SC_YES);
			}
			for(int i=1; i<AppGlobal.TAX_COUNT; i++) {
				if(i<=4 && m_oCheckItem.getInclusiveTaxRef(i).compareTo(BigDecimal.ZERO) != 0) {
					m_oCheckItem.setTax(i, m_oCheckItem.getInclusiveTaxRef(i));
					m_oCheckItem.setInclusiveTaxRef(i, BigDecimal.ZERO);
				}
				if(m_oCheckItem.getChargeTax(i).equals(PosCheckItem.CHARGE_TAX_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN))
					m_oCheckItem.setChargeTax(i, PosCheckItem.CHARGE_TAX_YES);
			}
			
			// breakdown pre discount value
			if(m_oItemDiscountList.size() > 0) {
				BigDecimal dNewPreDisc = BigDecimal.ZERO;
				for(PosCheckDiscount oItemDiscount: m_oItemDiscountList) {
					if(!oItemDiscount.getType().equals(PosDiscountType.TYPE_PRE_DISCOUNT))
						continue;
					
					List<PosCheckDiscountItem> oCheckDiscItemList = new ArrayList<PosCheckDiscountItem>();
					for (PosCheckDiscountItem oDiscItem: oItemDiscount.getCheckDiscountItemList()){
						oDiscItem.setItemSeatSeq(m_oCheckItem.getSeatNo(), m_oCheckItem.getSeq());
						oCheckDiscItemList.add(oDiscItem);
					}
					oItemDiscount.swipeBreakdownValue(true, oCheckDiscItemList);
					dNewPreDisc = dNewPreDisc.add(oItemDiscount.getTotal());
				}
				
				if(dNewPreDisc.compareTo(BigDecimal.ZERO) != 0)
					m_oCheckItem.setPreDisc(dNewPreDisc);
			}
		}else if(isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_NON_BREAKDOWN_DETAILS, 0)) {
			try {
				// Process item modifiers
				for(FuncCheckItem oModifierFuncCheckItem: getModifierList())
					oModifierFuncCheckItem.swipeBreakdownValue(bBreakdown);
				
				// Process child item
				if (isCalculateMethodSumUp()) {
					for(FuncCheckItem oChildFuncCheckItem: getChildItemList())
						oChildFuncCheckItem.swipeBreakdownValue(bBreakdown);
				}
				
				PosCheckExtraInfo oPosCheckExtraInfo = getExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_NON_BREAKDOWN_DETAILS, 0);
				JSONObject oBreakdownDetail = new JSONObject(oPosCheckExtraInfo.getValue());
				BigDecimal oTmpValue = null;
				if((oTmpValue = Util.jsonObjectKeyToBigDecimal(oBreakdownDetail, "citm_round_total")) != null)
					m_oCheckItem.setRoundTotal(oTmpValue);
				if((oTmpValue = Util.jsonObjectKeyToBigDecimal(oBreakdownDetail, "citm_total")) != null)
					m_oCheckItem.setTotal(oTmpValue);
				if((oTmpValue = Util.jsonObjectKeyToBigDecimal(oBreakdownDetail, "citm_round_amount")) != null)
					m_oCheckItem.setRoundAmount(oTmpValue);
				if((oTmpValue = Util.jsonObjectKeyToBigDecimal(oBreakdownDetail, "citm_price")) != null) {
					m_oCheckItem.setPrice(oTmpValue);
					m_oCheckItem.setOriginalPrice(oTmpValue);
				}
				if((oTmpValue = Util.jsonObjectKeyToBigDecimal(oBreakdownDetail, "citm_carry_total")) != null)
					m_oCheckItem.setCarryTotal(oTmpValue);
				for(int i=1; i<AppGlobal.SC_COUNT; i++) {
					m_oCheckItem.setSc(i, BigDecimal.ZERO);
					if(m_oCheckItem.getChargeSc(i).equals(PosCheckItem.CHARGE_SC_YES))
						m_oCheckItem.setChargeSc(i, PosCheckItem.CHARGE_SC_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN);
				}
				for(int i=1; i<AppGlobal.TAX_COUNT; i++) {
					String sKey = "citm_incl_tax_ref"+i;
					if(i<=4 && (oTmpValue = Util.jsonObjectKeyToBigDecimal(oBreakdownDetail, sKey)) != null) {
						m_oCheckItem.setTax(i, BigDecimal.ZERO);
						m_oCheckItem.setInclusiveTaxRef(i, oTmpValue);
					}
					if(m_oCheckItem.getChargeTax(i).equals(PosCheckItem.CHARGE_TAX_YES))
						m_oCheckItem.setChargeTax(i, PosCheckItem.CHARGE_TAX_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN);
				}
				if((oTmpValue = Util.jsonObjectKeyToBigDecimal(oBreakdownDetail, "citm_pre_disc")) != null)
					m_oCheckItem.setPreDisc(oTmpValue);
				if((oTmpValue = Util.jsonObjectKeyToBigDecimal(oBreakdownDetail, "citm_mid_disc")) != null)
					m_oCheckItem.setMidDisc(oTmpValue);
				if((oTmpValue = Util.jsonObjectKeyToBigDecimal(oBreakdownDetail, "citm_post_disc")) != null)
					m_oCheckItem.setPostDisc(oTmpValue);
				
				//for item discount
				if(m_oItemDiscountList.size() > 0) {
					for(PosCheckDiscount oItemDiscount: m_oItemDiscountList) {
						if(!oItemDiscount.getType().equals(PosDiscountType.TYPE_PRE_DISCOUNT))
							continue;
						List<PosCheckDiscountItem> oCheckDiscItemList = new ArrayList<PosCheckDiscountItem>();
						for (PosCheckDiscountItem oDiscItem: oItemDiscount.getCheckDiscountItemList()){
							oDiscItem.setItemSeatSeq(m_oCheckItem.getSeatNo(), m_oCheckItem.getSeq());
							oCheckDiscItemList.add(oDiscItem);
						}
						oItemDiscount.swipeBreakdownValue(false, oCheckDiscItemList);
					}
				}
				m_oCheckItem.setModified(true);
			}catch(JSONException e) {}
		}
	}
	
	public boolean remainingPendingInfoIsExistAndSplitByRatio(BigDecimal dSplitRatio) {
		// handle pending items (occur if partial printed pending item)
		if(this.isPartialPendingItem()) {
			JSONObject oOriRemainingPendingInfo = null;
			try {
				oOriRemainingPendingInfo = new JSONObject (this.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_REMAINING_PENDING_INFO));
				BigDecimal dOriRemainingPendingQty = new BigDecimal(oOriRemainingPendingInfo.optString("count"));
				
				JSONObject oUpdatedPendingInfo = new JSONObject();
				oUpdatedPendingInfo.put("count", dOriRemainingPendingQty.multiply(dSplitRatio).toPlainString());
				remainingPendingItemListSplitByRatioIfExist(oOriRemainingPendingInfo, oUpdatedPendingInfo, "itemList", dSplitRatio);
				remainingPendingItemListSplitByRatioIfExist(oOriRemainingPendingInfo, oUpdatedPendingInfo, "deliveredItemList", dSplitRatio);
				
				// Update extra info
				this.updateExtraInfoValue(PosCheckExtraInfo.BY_ITEM, PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_REMAINING_PENDING_INFO, oUpdatedPendingInfo.toString());
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
			return true;
		}
		return false;
	}
	
	private void remainingPendingItemListSplitByRatioIfExist(JSONObject oOriRemainingPendingInfo, JSONObject oUpdatedPendingInfo, String sListName, BigDecimal dSplitRatio) {
		if (oOriRemainingPendingInfo != null && oOriRemainingPendingInfo.optJSONObject(sListName) != null) {
			try {
				JSONObject oItemArray = new JSONObject();
				oItemArray = oOriRemainingPendingInfo.optJSONObject(sListName);
				Iterator<String> oKeys = oItemArray.keys();
				while(oKeys.hasNext()) {
					String skey = oKeys.next();
					BigDecimal dTempQty = new BigDecimal(oItemArray.getString(skey));
					oItemArray.put(skey, dTempQty.multiply(dSplitRatio).toPlainString());
				}
				oUpdatedPendingInfo.put(sListName, oItemArray);
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
	}
	
	public void updateCountForPartialSentPendingItem(BigDecimal dUpdatedCount, JSONObject oSendedItemJSON, JSONObject oDeliveredItemJSON) {
		// handle pending items (occur if partial printed pending item)
		if(this.isPartialPendingItem()) {
			JSONObject oUpdatedPendingInfo = new JSONObject();
			try {
				if(dUpdatedCount.compareTo(BigDecimal.ZERO) > -1) {
					oUpdatedPendingInfo.put("count", dUpdatedCount);
					if (dUpdatedCount.compareTo(BigDecimal.ZERO) == 0)
						setPendingItem(PosCheckItem.PENDING_NORMAL_ITEM);
				}
				if(oSendedItemJSON != null && oSendedItemJSON.length() > 0)
					oUpdatedPendingInfo.put("itemList", oSendedItemJSON);
				if(oDeliveredItemJSON != null && oDeliveredItemJSON.length() > 0)
					oUpdatedPendingInfo.put("deliveredItemList", oDeliveredItemJSON);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				AppGlobal.stack2Log(e);
			}
			// Update extra info
			if(oUpdatedPendingInfo.length() > 0)
				this.updateExtraInfoValue(PosCheckExtraInfo.BY_ITEM, PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_REMAINING_PENDING_INFO, oUpdatedPendingInfo.toString());
			else
				this.updateExtraInfoValue(PosCheckExtraInfo.BY_ITEM, PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_REMAINING_PENDING_INFO, "");
		}
	}
	
	public JSONObject getPartialPendingItemInfo(String sListType){
		JSONObject oRemainingPendingInfo = new JSONObject();
		JSONObject oResultPendingInfo = null;
		try {
			if(this.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_REMAINING_PENDING_INFO) != null && !this.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_REMAINING_PENDING_INFO).isEmpty())
				oRemainingPendingInfo = new JSONObject(this.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_REMAINING_PENDING_INFO));

			if(oRemainingPendingInfo.has(sListType) && oRemainingPendingInfo.optJSONObject(sListType) != null && oRemainingPendingInfo.optJSONObject(sListType).length() > 0)
				oResultPendingInfo = oRemainingPendingInfo.optJSONObject(sListType);
		} catch (JSONException e) {
			AppGlobal.stack2Log(e);
		}
		return oResultPendingInfo;
	}
	
	public BigDecimal getPartialPendingItemAmount(){
		BigDecimal dAmount = BigDecimal.ZERO;
		JSONObject oRemainingPendingInfo = new JSONObject();
		try {
			if(this.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_REMAINING_PENDING_INFO) != null)
				oRemainingPendingInfo = new JSONObject(this.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_REMAINING_PENDING_INFO));
			if(oRemainingPendingInfo.has("count") && !oRemainingPendingInfo.optString("count").isEmpty())
				dAmount = new BigDecimal(oRemainingPendingInfo.optString("count"));
		} catch (JSONException e) {
			AppGlobal.stack2Log(e);
		}
		return dAmount;
	}
	
	public void setChildItemSeqInSetMenuLookup(int iSeq){
		m_iChildItemSeqInSetMenuLookup = iSeq;
	}
	
	public int getChildItemSeqInSetMenuLookup(){
		return m_iChildItemSeqInSetMenuLookup;
	}
	
}
