//Database: pos_check_items - Sales check item ordered
package app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosCheckItem {
	private int citmId;
	private int bdayId;
	private int bperId;
	private int shopId;
	private int oletId;
	private int chksId;
	private int cptyId;
	private int itemId;
	private String code;
	private String[] name;
	private String[] shortName;
	private int parentCitmId;
	private String role;
	private int childCount;
	private int modifierCount;
	private int seat;
	private int mixAndMatchCitmId;
	private int seq;
	private BigDecimal roundTotal;
	private BigDecimal total;
	private BigDecimal roundAmount;
	private BigDecimal carryTotal;
	private BigDecimal qty;
	private BigDecimal baseQty;
	private BigDecimal price;
	private BigDecimal originalPrice;
	private BigDecimal basicPrice;
	private BigDecimal[] sc;
	private BigDecimal[] tax;
	private BigDecimal[] inclTaxRef;
	private BigDecimal preDisc;
	private BigDecimal midDisc;
	private BigDecimal postDisc;
	private int originalPriceLevel;
	private int priceLevel;
	private BigDecimal revenue;
	private BigDecimal carryRevenue;
	private BigDecimal unitCost;
	private int[] printQueueItpqId;
	private String noPrint;
	private String[] chargeSc;
	private String[] chargeTax;
	private String hide;
	private int icatId;
	private int idepId;
	private int icouId;
	private int digpId;
	private String getRevenue;
	private String servingStatus;
	private String pending;
	private String orderingType;
	private String roundStatus;
	private String orderTime;
	private DateTime orderLocTime;
	private int orderUserId;
	private int orderStatId;
	private String rushTime;
	private DateTime rushLocTime;
	private int rushUserId;
	private int rushStatId;
	private int rushCount;
	private DateTime deliveryTime;
	private String voidTime;
	private DateTime voidLocTime;
	private int voidUserId;
	private int voidStatId;
	private int voidVdrsId;
	private String voidConsumed;
	private String status;
	
	private PosCheckItem oriPosCheckItem;
	private int splitFromCheckId;
	
	private boolean bModified;
	private List<PosCheckItem> childItemList;
	private List<PosCheckDiscountItem> checkDiscountItemList;
	private List<PosCheckDiscount> itemDiscountList;
	private List<PosCheckItem> modifierList;
	private List<PosCheckExtraInfo> extraInfoList;
	
	// role
	public static String ROLE_BASIC_ITEM = "";
	public static String ROLE_SET_MENU_CHILD_ITEM = "c";
	public static String ROLE_MODIFIER_ITEM = "m";
	
	// noPrint
	public static String NO_PRINT_NO = "";
	public static String NO_PRINT_YES = "y";
	
	// chargeSc
	public static String CHARGE_SC_NO = "";
	public static String CHARGE_SC_YES = "c";
	public static String CHARGE_SC_CHARGED_IN_ITEM_PRICE = "i";
	
	//chargeTax
	public static String CHARGE_TAX_NO = "";
	public static String CHARGE_TAX_YES = "c";
	public static String CHARGE_TAX_CHARGED_IN_ITEM_PRICE = "i";
	public static String CHARGE_TAX_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN = "n";
	
	// hide
	public static String HIDE_NO = "";
	public static String HIDE_YES = "y";
	
	// getRevenue
	public static String GET_REVENUE_NO_REVENUE = "";
	public static String GET_REVENUE_SPLIT_PRICE = "p";
	public static String GET_REVENUE_SPLIT_ORIGINAL_BASIC_PRICE = "b";
	
	// serving_status
	public static String SERVING_STATUS_ORDERED = "";
	public static String SERVING_STATUS_READY = "r";
	public static String SERVING_STATUS_HOLD = "h";
	public static String SERVING_STATUS_SERVED = "s";
	
	// pending
	public static String PENDING_NORMAL_ITEM = "";
	public static String PENDING_PENDING_ITEM = "y";
	
	// orderingType
	public static String ORDERING_TYPE_FINE_DINING = "";
	public static String ORDERING_TYPE_TAKEOUT = "t";
	
	// roundStatus
	public static String ROUND_STATUS_SAVED_BEFORE_ROUNDING = "";
	public static String ROUND_STATUS_ROUNDED = "t";
	
	// voidConsumed
	public static String VOID_CONSUMED_NO = "";
	public static String VOID_CONSUMED_YES = "y";
	
	// status
	public static String STATUS_NORMAL = "";
	public static String STATUS_DELETED = "d";
	
	//init with initialized value
	public PosCheckItem() {
		this.init();
	}
	
	public PosCheckItem(PosCheckItem oPosCheckItem) {
		int i=0;
		
		this.init();
		
		this.citmId = oPosCheckItem.citmId;
		this.bdayId = oPosCheckItem.bdayId;
		this.bperId = oPosCheckItem.bperId;
		this.shopId = oPosCheckItem.shopId;
		this.oletId = oPosCheckItem.oletId;
		this.chksId = oPosCheckItem.chksId;
		this.cptyId = oPosCheckItem.cptyId;
		this.itemId = oPosCheckItem.itemId;
		this.code = oPosCheckItem.code;
		for(i=0; i<5; i++)
			this.name[i] = oPosCheckItem.name[i];
		for(i=0; i<5; i++)
			this.shortName[i] = oPosCheckItem.shortName[i];
		this.parentCitmId = oPosCheckItem.parentCitmId;
		this.role = oPosCheckItem.role;
		this.childCount = oPosCheckItem.childCount;
		this.modifierCount = oPosCheckItem.modifierCount;
		this.seat = oPosCheckItem.seat;
		this.mixAndMatchCitmId = oPosCheckItem.mixAndMatchCitmId;
		this.seq = oPosCheckItem.seq;
		this.roundTotal = new BigDecimal(oPosCheckItem.roundTotal.toPlainString());
		this.total = new BigDecimal(oPosCheckItem.total.toPlainString());
		this.roundAmount = new BigDecimal(oPosCheckItem.roundAmount.toPlainString());
		this.carryTotal = new BigDecimal(oPosCheckItem.carryTotal.toPlainString());
		this.qty = new BigDecimal(oPosCheckItem.qty.toPlainString());
		this.baseQty = new BigDecimal(oPosCheckItem.baseQty.toPlainString());
		this.price = new BigDecimal(oPosCheckItem.price.toPlainString());
		this.originalPrice = new BigDecimal(oPosCheckItem.originalPrice.toPlainString());
		this.basicPrice = new BigDecimal(oPosCheckItem.basicPrice.toPlainString());
		for(i=0; i<5; i++)
			this.sc[i] = new BigDecimal(oPosCheckItem.sc[i].toPlainString());
		for(i=0; i<25; i++)
			this.tax[i] = new BigDecimal(oPosCheckItem.tax[i].toPlainString());
		for(i=0; i<4; i++)
			this.inclTaxRef[i] = new BigDecimal(oPosCheckItem.inclTaxRef[i].toPlainString());
		this.preDisc = new BigDecimal(oPosCheckItem.preDisc.toPlainString());
		this.midDisc = new BigDecimal(oPosCheckItem.midDisc.toPlainString());
		this.postDisc = new BigDecimal(oPosCheckItem.postDisc.toPlainString());
		this.originalPriceLevel = oPosCheckItem.originalPriceLevel;
		this.priceLevel = oPosCheckItem.priceLevel;
		this.revenue = new BigDecimal(oPosCheckItem.revenue.toPlainString());
		this.carryRevenue = new BigDecimal(oPosCheckItem.carryRevenue.toPlainString());
		this.unitCost = new BigDecimal(oPosCheckItem.unitCost.toPlainString());
		this.printQueueItpqId = new int[10];
		for(i=0; i<10; i++)
			this.printQueueItpqId[i] = oPosCheckItem.printQueueItpqId[i];
		this.noPrint = "";
		for(i=0; i<5; i++)
			this.chargeSc[i] = oPosCheckItem.chargeSc[i];
		for(i=0; i<25; i++)
			this.chargeTax[i] = oPosCheckItem.chargeTax[i];
		this.hide = "";
		this.icatId = oPosCheckItem.icatId;
		this.idepId = oPosCheckItem.idepId;
		this.icouId = oPosCheckItem.icouId;
		this.digpId = oPosCheckItem.digpId;
		this.getRevenue = oPosCheckItem.getRevenue;
		this.servingStatus = oPosCheckItem.servingStatus;
		this.pending = oPosCheckItem.pending;
		this.orderingType = oPosCheckItem.orderingType;
		this.roundStatus = oPosCheckItem.roundStatus;
		
		if (oPosCheckItem.orderLocTime != null)
			this.orderLocTime = new DateTime(oPosCheckItem.orderLocTime);
		
		if (oPosCheckItem.orderTime != null)
			this.orderTime = oPosCheckItem.orderTime;
		
		this.orderUserId = oPosCheckItem.orderUserId;
		this.orderStatId = oPosCheckItem.orderStatId;
		
		if (oPosCheckItem.rushTime != null)
			this.rushTime = oPosCheckItem.rushTime;
		
		if (oPosCheckItem.rushLocTime != null)
			this.rushLocTime = new DateTime(oPosCheckItem.rushLocTime);
		
		this.rushUserId = oPosCheckItem.rushUserId;
		this.rushStatId = oPosCheckItem.rushStatId;
		this.rushCount = oPosCheckItem.rushCount;
		
		if (oPosCheckItem.deliveryTime != null)
			this.deliveryTime = new DateTime(oPosCheckItem.deliveryTime);
		
		if (oPosCheckItem.voidTime != null)
			this.voidTime = oPosCheckItem.voidTime;
		
		if (oPosCheckItem.voidLocTime != null)
			this.voidLocTime = new DateTime(oPosCheckItem.voidLocTime);
		
		this.voidUserId = oPosCheckItem.voidUserId;
		this.voidStatId = oPosCheckItem.voidStatId;
		this.voidVdrsId = oPosCheckItem.voidVdrsId;
		this.voidConsumed = oPosCheckItem.voidConsumed;
		this.status = oPosCheckItem.status;

		this.bModified = false;
		
		for(int j = 0; j < oPosCheckItem.childItemList.size(); j++) {
			PosCheckItem tempCheckItem = oPosCheckItem.childItemList.get(j);
			this.childItemList.add(new PosCheckItem(tempCheckItem));
		}
		
		for(int j = 0; j < oPosCheckItem.itemDiscountList.size(); j++) {
			PosCheckDiscount tempCheckDiscount = oPosCheckItem.itemDiscountList.get(j);
			this.itemDiscountList.add(new PosCheckDiscount(tempCheckDiscount));
		}
		
		for(int j = 0; j < oPosCheckItem.modifierList.size(); j++) {
			PosCheckItem tempModifierCheckItem = oPosCheckItem.modifierList.get(j);
			this.modifierList.add(new PosCheckItem(tempModifierCheckItem));
		}
		
		for(int j = 0; j < oPosCheckItem.extraInfoList.size(); j++) {
			PosCheckExtraInfo tempExtraInfo = oPosCheckItem.extraInfoList.get(j);
			this.extraInfoList.add(new PosCheckExtraInfo(tempExtraInfo));
		}
		
		if(oPosCheckItem.oriPosCheckItem != null){
			this.oriPosCheckItem = new PosCheckItem(oPosCheckItem.oriPosCheckItem);
		}
		
	}
	
	//init object with JSON Object
	public PosCheckItem(JSONObject checkItemJSONObject) {
		readDataFromJson(checkItemJSONObject, true);
	}
	
	//read data list from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bResult = true;
		JSONObject tempJSONObject = null; 
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bResult = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("checkItem")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("checkItem")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("checkItem");
			if (tempJSONObject.isNull("PosCheckItem")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject, true);
		}
		
		return bResult;
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray checkItemJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("checkItems")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("checkItems"))
				return null;
			
			checkItemJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("checkItems");
		}
		
		return checkItemJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject checkItemJSONObject, boolean bCreateCloneObjectWithOriginalValue) {
		JSONObject resultCheckItem = null;
		int i;
		String sTempJSONName = "";
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"); 
		
		resultCheckItem = checkItemJSONObject.optJSONObject("PosCheckItem");
		if(resultCheckItem == null)
			resultCheckItem = checkItemJSONObject;
			
		this.init();
		
		this.citmId = resultCheckItem.optInt("citm_id");
		this.bdayId = resultCheckItem.optInt("citm_bday_id");
		this.bperId = resultCheckItem.optInt("citm_bper_id");
		this.shopId = resultCheckItem.optInt("citm_shop_id");
		this.oletId = resultCheckItem.optInt("citm_olet_id");
		this.chksId = resultCheckItem.optInt("citm_chks_id");
		this.cptyId = resultCheckItem.optInt("citm_cpty_id");
		this.itemId = resultCheckItem.optInt("citm_item_id");
		this.code = resultCheckItem.optString("citm_code");
		for(i=1; i<=5; i++)
			this.name[(i-1)] =resultCheckItem.optString("citm_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultCheckItem.optString("citm_short_name_l"+i);
		this.parentCitmId = resultCheckItem.optInt("citm_parent_citm_id");
		
		this.role = resultCheckItem.optString("citm_role", PosCheckItem.ROLE_BASIC_ITEM);
		
		this.childCount = resultCheckItem.optInt("citm_child_count");
		this.modifierCount = resultCheckItem.optInt("citm_modifier_count");
		this.seat = resultCheckItem.optInt("citm_seat");
		this.mixAndMatchCitmId = resultCheckItem.optInt("citm_mix_and_match_citm_id");
		this.seq = resultCheckItem.optInt("citm_seq");
		this.roundTotal = new BigDecimal(resultCheckItem.optString("citm_round_total", "0.0"));
		this.total = new BigDecimal(resultCheckItem.optString("citm_total", "0.0"));
		this.roundAmount = new BigDecimal(resultCheckItem.optString("citm_round_amount", "0.0"));
		this.carryTotal = new BigDecimal(resultCheckItem.optString("citm_carry_total", "0.0"));
		this.qty = new BigDecimal(resultCheckItem.optString("citm_qty", "0.0"));
		this.baseQty = new BigDecimal(resultCheckItem.optString("citm_base_qty", "0.0"));
		this.price = new BigDecimal(resultCheckItem.optString("citm_price", "0.0"));
		this.originalPrice = new BigDecimal(resultCheckItem.optString("citm_original_price", "0.0"));
		this.basicPrice = new BigDecimal(resultCheckItem.optString("citm_basic_price", "0.0"));
		
		for(i=1; i<=5; i++)
			this.sc[i-1] = new BigDecimal(resultCheckItem.optString("citm_sc" + i, "0.0"));
		for(i=1; i<=25; i++)
			this.tax[i-1] = new BigDecimal(resultCheckItem.optString("citm_tax" + i, "0.0"));
		for(i=1; i<=4; i++)
			this.inclTaxRef[i-1] = new BigDecimal(resultCheckItem.optString("citm_incl_tax_ref" + i, "0.0"));
		
		this.preDisc = new BigDecimal(resultCheckItem.optString("citm_pre_disc", "0.0"));
		this.midDisc = new BigDecimal(resultCheckItem.optString("citm_mid_disc", "0.0"));
		this.postDisc = new BigDecimal(resultCheckItem.optString("citm_post_disc", "0.0"));
		this.originalPriceLevel = resultCheckItem.optInt("citm_original_price_level");
		this.priceLevel = resultCheckItem.optInt("citm_price_level");
		this.revenue = new BigDecimal(resultCheckItem.optString("citm_revenue", "0.0"));
		this.carryRevenue = new BigDecimal(resultCheckItem.optString("citm_carry_revenue", "0.0"));
		this.unitCost = new BigDecimal(resultCheckItem.optString("citm_unit_cost", "0.0"));
		
		for(i=1; i<=10; i++) {
			sTempJSONName = "citm_print_queue"+i+"_itpq_id";
			this.printQueueItpqId[i-1] = resultCheckItem.optInt(sTempJSONName);
		}
		
		this.noPrint = resultCheckItem.optString("citm_no_print", PosCheckItem.NO_PRINT_NO);
		
		for(i=1; i<=5; i++) {
			sTempJSONName = "citm_charge_sc"+Integer.toString((i));
			this.chargeSc[i-1] = resultCheckItem.optString(sTempJSONName, PosCheckItem.CHARGE_SC_NO);
		}
		
		for(i=1; i<=25; i++) {
			sTempJSONName = "citm_charge_tax"+Integer.toString((i));
			this.chargeTax[i-1] = resultCheckItem.optString(sTempJSONName, PosCheckItem.CHARGE_TAX_NO);
		}
		
		this.hide = resultCheckItem.optString("citm_hide", PosCheckItem.HIDE_NO);
		this.icatId = resultCheckItem.optInt("citm_icat_id");
		this.idepId = resultCheckItem.optInt("citm_idep_id");
		this.icouId = resultCheckItem.optInt("citm_icou_id");
		this.digpId = resultCheckItem.optInt("citm_digp_id");
		this.getRevenue = resultCheckItem.optString("citm_get_revenue", PosCheckItem.GET_REVENUE_NO_REVENUE);
		this.servingStatus = resultCheckItem.optString("citm_serving_status", PosCheckItem.SERVING_STATUS_ORDERED);
		this.pending = resultCheckItem.optString("citm_pending", PosCheckItem.PENDING_NORMAL_ITEM);
		this.orderingType = resultCheckItem.optString("citm_ordering_type", PosCheckItem.ORDERING_TYPE_FINE_DINING);
		this.roundStatus = resultCheckItem.optString("citm_round_status", PosCheckItem.ROUND_STATUS_SAVED_BEFORE_ROUNDING);
		
		this.orderTime = resultCheckItem.optString("citm_order_time", null);
		
		String sOrderLocTime = resultCheckItem.optString("citm_order_loctime");
		if(!sOrderLocTime.isEmpty())
			this.orderLocTime = oFormatter.parseDateTime(sOrderLocTime);
		
		this.orderUserId = resultCheckItem.optInt("citm_order_user_id");
		this.orderStatId = resultCheckItem.optInt("citm_order_stat_id");
		
		this.rushTime = resultCheckItem.optString("citm_rush_time", null);
		
		String sRushLocTime = resultCheckItem.optString("citm_rush_loctime");
		if(!sRushLocTime.isEmpty())
			this.rushLocTime = oFormatter.parseDateTime(sRushLocTime);
		
		this.rushUserId = resultCheckItem.optInt("citm_rush_user_id");
		this.rushStatId = resultCheckItem.optInt("citm_rush_stat_id");
		this.rushCount = resultCheckItem.optInt("citm_rush_count");
		
		String sDeliveryTime = resultCheckItem.optString("citm_delivery_time");
		if(!sDeliveryTime.isEmpty())
			this.deliveryTime = oFormatter.parseDateTime(sDeliveryTime);
		
		this.voidTime = resultCheckItem.optString("citm_void_time", null);
		
		String sVoidLocTime = resultCheckItem.optString("citm_void_loctime");
		if(!sVoidLocTime.isEmpty())
			this.voidLocTime = oFormatter.parseDateTime(sVoidLocTime);
		
		this.voidUserId = resultCheckItem.optInt("citm_void_user_id");
		this.voidStatId = resultCheckItem.optInt("citm_void_stat_id");
		this.voidVdrsId = resultCheckItem.optInt("citm_void_vdrs_id");
		this.voidConsumed = resultCheckItem.optString("citm_void_consumed");
		this.status = resultCheckItem.optString("citm_status", PosCheckItem.STATUS_NORMAL);

		//handle item's modifier if exist
		JSONArray oModifierJSONArray = checkItemJSONObject.optJSONArray("ModifierList");
		if(oModifierJSONArray != null) {
			PosCheckItemList oModifierList = new PosCheckItemList(oModifierJSONArray);
			this.modifierList = oModifierList.getCheckItemList();
		}
		
		//handle item's child if exist
		JSONArray oChildItemJSONArray = checkItemJSONObject.optJSONArray("ChildItemList");
		if(oChildItemJSONArray != null) { 
			PosCheckItemList oChildItemList = new PosCheckItemList(oChildItemJSONArray);
			this.childItemList = oChildItemList.getCheckItemList();
		}
		
		//handle item's discount if exist
		JSONArray oItemDiscountJSONArray = checkItemJSONObject.optJSONArray("ItemDiscountList");
		if(oItemDiscountJSONArray != null) {
			PosCheckDiscountList oItemDiscountList = new PosCheckDiscountList(oItemDiscountJSONArray);
			this.itemDiscountList = oItemDiscountList.getCheckDiscountList();
		}
		
		//handle item's extra info if exist
		JSONArray oExtraInfoJSONArray = checkItemJSONObject.optJSONArray("ExtraInfoList");
		if(oExtraInfoJSONArray != null) {
			PosCheckExtraInfoList oExtraInfoList = new PosCheckExtraInfoList(oExtraInfoJSONArray);
			this.extraInfoList = oExtraInfoList.getCheckExtraInfoList();
		}
		
		if(bCreateCloneObjectWithOriginalValue){
			// Create a clone object to store original value
			oriPosCheckItem = new PosCheckItem();
			oriPosCheckItem.readDataFromJson(checkItemJSONObject, false);
		}
	}
	
	//construct the save request JSON
	protected JSONObject constructAddSaveJSON(boolean bUpdate, boolean bContructWithAllItemFields) {
		int i = 0;
		String sTempJSONName = "";
		JSONObject addSaveJSONObject = new JSONObject();
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		if(oriPosCheckItem == null || this.citmId == 0){
			// For new item
			// Create a new PosCheckItem with initial value for field comparation
			oriPosCheckItem = new PosCheckItem();
		}
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("citm_id", this.citmId);
			// MUST send
			addSaveJSONObject.put("citm_bday_id", this.bdayId);
			addSaveJSONObject.put("citm_bper_id", this.bperId);
			addSaveJSONObject.put("citm_shop_id", this.shopId);
			addSaveJSONObject.put("citm_olet_id", this.oletId);
			addSaveJSONObject.put("citm_chks_id", this.chksId);
			addSaveJSONObject.put("citm_cpty_id", this.cptyId);
			
			if(bContructWithAllItemFields || this.itemId != oriPosCheckItem.itemId)
				addSaveJSONObject.put("citm_item_id", this.itemId);
			if(bContructWithAllItemFields || !this.code.equals(oriPosCheckItem.code))
				addSaveJSONObject.put("citm_code", this.code);
			for(i=1; i<=5; i++) {
				if(bContructWithAllItemFields || !this.name[(i-1)].equals(oriPosCheckItem.name[(i-1)]))
					addSaveJSONObject.put("citm_name_l"+i, this.name[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(bContructWithAllItemFields || !this.shortName[(i-1)].equals(oriPosCheckItem.shortName[(i-1)]))
					addSaveJSONObject.put("citm_short_name_l"+i, this.shortName[(i-1)]);
			}
			// MUST send
			addSaveJSONObject.put("citm_parent_citm_id", this.parentCitmId);
			if(bContructWithAllItemFields || !this.role.equals(oriPosCheckItem.role))
				addSaveJSONObject.put("citm_role", this.role);
			if(bContructWithAllItemFields || this.childCount != oriPosCheckItem.childCount)
				addSaveJSONObject.put("citm_child_count", this.childCount);
			if(bContructWithAllItemFields || this.modifierCount != oriPosCheckItem.modifierCount)
				addSaveJSONObject.put("citm_modifier_count", this.modifierCount);
			// MUST send seat
			addSaveJSONObject.put("citm_seat", this.seat);
			if(bContructWithAllItemFields || this.mixAndMatchCitmId != oriPosCheckItem.mixAndMatchCitmId)
				addSaveJSONObject.put("citm_mix_and_match_citm_id", this.mixAndMatchCitmId);
			// MUST send seq
			addSaveJSONObject.put("citm_seq", this.seq);
			if(bContructWithAllItemFields || this.roundTotal.compareTo(oriPosCheckItem.roundTotal) != 0)
				addSaveJSONObject.put("citm_round_total", this.roundTotal);
			if(bContructWithAllItemFields || this.total.compareTo(oriPosCheckItem.total) != 0)
				addSaveJSONObject.put("citm_total", this.total);
			if(bContructWithAllItemFields || this.roundAmount.compareTo(oriPosCheckItem.roundAmount) != 0)
				addSaveJSONObject.put("citm_round_amount", this.roundAmount);
			if(bContructWithAllItemFields || this.carryTotal.compareTo(oriPosCheckItem.carryTotal) != 0)
				addSaveJSONObject.put("citm_carry_total", this.carryTotal);
			if(bContructWithAllItemFields || this.qty.compareTo(oriPosCheckItem.qty) != 0)
				addSaveJSONObject.put("citm_qty", this.qty);
			if(bContructWithAllItemFields || this.baseQty.compareTo(oriPosCheckItem.baseQty) != 0)
				addSaveJSONObject.put("citm_base_qty", this.baseQty);
			if(bContructWithAllItemFields || this.price.compareTo(oriPosCheckItem.price) != 0)
				addSaveJSONObject.put("citm_price", this.price);
			if(bContructWithAllItemFields || this.originalPrice.compareTo(oriPosCheckItem.originalPrice) != 0)
				addSaveJSONObject.put("citm_original_price", this.originalPrice);
			if(bContructWithAllItemFields || this.basicPrice.compareTo(oriPosCheckItem.basicPrice) != 0)
				addSaveJSONObject.put("citm_basic_price", this.basicPrice);
			for(i=1; i<=5; i++) {
				if(bContructWithAllItemFields || this.sc[i-1].compareTo(oriPosCheckItem.sc[i-1]) != 0){
					sTempJSONName = "citm_sc"+Integer.toString((i));
					addSaveJSONObject.put(sTempJSONName, this.sc[i-1]);
				}
			}
			for(i=1; i<=25; i++) {
				if(bContructWithAllItemFields || this.tax[i-1].compareTo(oriPosCheckItem.tax[i-1]) != 0){
					sTempJSONName = "citm_tax"+Integer.toString((i));
					addSaveJSONObject.put(sTempJSONName, this.tax[i-1]);
				}
			}
			for(i=1; i<=4; i++) {
				if(bContructWithAllItemFields || this.inclTaxRef[i-1].compareTo(oriPosCheckItem.inclTaxRef[i-1]) != 0){
					sTempJSONName = "citm_incl_tax_ref"+Integer.toString((i));
					addSaveJSONObject.put(sTempJSONName, this.inclTaxRef[i-1]);
				}
			}
			if(bContructWithAllItemFields || this.preDisc.compareTo(oriPosCheckItem.preDisc) != 0)
				addSaveJSONObject.put("citm_pre_disc", this.preDisc);
			if(bContructWithAllItemFields || this.midDisc.compareTo(oriPosCheckItem.midDisc) != 0)
				addSaveJSONObject.put("citm_mid_disc", this.midDisc);
			if(bContructWithAllItemFields || this.postDisc.compareTo(oriPosCheckItem.postDisc) != 0)
				addSaveJSONObject.put("citm_post_disc", this.postDisc);
			if(bContructWithAllItemFields || this.originalPriceLevel != oriPosCheckItem.originalPriceLevel)
				addSaveJSONObject.put("citm_original_price_level", this.originalPriceLevel);
			if(bContructWithAllItemFields || this.priceLevel != oriPosCheckItem.priceLevel)
				addSaveJSONObject.put("citm_price_level", this.priceLevel);
			if(bContructWithAllItemFields || this.revenue.compareTo(oriPosCheckItem.revenue) != 0)
				addSaveJSONObject.put("citm_revenue", this.revenue);
			if(bContructWithAllItemFields || this.carryRevenue.compareTo(oriPosCheckItem.carryRevenue) != 0)
				addSaveJSONObject.put("citm_carry_revenue", this.carryRevenue);
			if(bContructWithAllItemFields || this.unitCost.compareTo(oriPosCheckItem.unitCost) != 0)
				addSaveJSONObject.put("citm_unit_cost", this.unitCost);
			for(i=1; i<=10; i++) {
				if(bContructWithAllItemFields || this.printQueueItpqId[i-1] != oriPosCheckItem.printQueueItpqId[i-1]) {
					sTempJSONName = "citm_print_queue"+i+"_itpq_id";
					addSaveJSONObject.put(sTempJSONName, this.printQueueItpqId[i-1]);
				}
			}
			if(bContructWithAllItemFields || !this.noPrint.equals(oriPosCheckItem.noPrint))
				addSaveJSONObject.put("citm_no_print", this.noPrint);
			for(i=1; i<=5; i++) {
				if(bContructWithAllItemFields || !this.chargeSc[i-1].equals(oriPosCheckItem.chargeSc[i-1])){
					sTempJSONName = "citm_charge_sc"+i;
					addSaveJSONObject.put(sTempJSONName, this.chargeSc[i-1]);
				}
			}
			for(i=1; i<=25; i++) {
				if(bContructWithAllItemFields || !this.chargeTax[i-1].equals(oriPosCheckItem.chargeTax[i-1])){
					sTempJSONName = "citm_charge_tax"+i;
					addSaveJSONObject.put(sTempJSONName, this.chargeTax[i-1]);
				}
			}
			if(bContructWithAllItemFields || !this.hide.equals(oriPosCheckItem.hide))
				addSaveJSONObject.put("citm_hide", this.hide);
			if(bContructWithAllItemFields || this.icatId != oriPosCheckItem.icatId)
				addSaveJSONObject.put("citm_icat_id", this.icatId);
			if(bContructWithAllItemFields || this.idepId != oriPosCheckItem.idepId)
				addSaveJSONObject.put("citm_idep_id", this.idepId);
			if(bContructWithAllItemFields || this.icouId != oriPosCheckItem.icouId)
				addSaveJSONObject.put("citm_icou_id", this.icouId);
			if(bContructWithAllItemFields || this.digpId != oriPosCheckItem.digpId)
				addSaveJSONObject.put("citm_digp_id", this.digpId);
			if(bContructWithAllItemFields || !this.getRevenue.equals(oriPosCheckItem.getRevenue))
				addSaveJSONObject.put("citm_get_revenue", this.getRevenue);
			if(bContructWithAllItemFields || !this.servingStatus.equals(oriPosCheckItem.servingStatus))
				addSaveJSONObject.put("citm_serving_status", this.servingStatus);
			// MUST send pending
			addSaveJSONObject.put("citm_pending", this.pending);
			if(bContructWithAllItemFields || !this.orderingType.equals(oriPosCheckItem.orderingType))
				addSaveJSONObject.put("citm_ordering_type", this.orderingType);
			if(bContructWithAllItemFields || !this.roundStatus.equals(oriPosCheckItem.roundStatus))
				addSaveJSONObject.put("citm_round_status", this.roundStatus);
			
			if (this.orderLocTime != null && (bContructWithAllItemFields || !this.orderTime.equals(oriPosCheckItem.orderTime))) {
				addSaveJSONObject.put("citm_order_time", this.orderTime);
				addSaveJSONObject.put("citm_order_loctime", this.orderLocTime.toString(oFormatter));
			}
			
			if(bContructWithAllItemFields || this.orderUserId != oriPosCheckItem.orderUserId)
				addSaveJSONObject.put("citm_order_user_id", this.orderUserId);
			if(bContructWithAllItemFields || this.orderStatId != oriPosCheckItem.orderStatId)
				addSaveJSONObject.put("citm_order_stat_id", this.orderStatId);
			
			if (this.rushLocTime != null && (bContructWithAllItemFields || !this.rushTime.equals(oriPosCheckItem.rushTime))) {
				addSaveJSONObject.put("citm_rush_time", this.rushTime);
				addSaveJSONObject.put("citm_rush_loctime", this.rushLocTime.toString(oFormatter));
			}
			
			if(bContructWithAllItemFields || this.rushUserId != oriPosCheckItem.rushUserId)
				addSaveJSONObject.put("citm_rush_user_id", this.rushUserId);
			if(bContructWithAllItemFields || this.rushStatId != oriPosCheckItem.rushStatId)
				addSaveJSONObject.put("citm_rush_stat_id", this.rushStatId);
			if(bContructWithAllItemFields || this.rushCount != oriPosCheckItem.rushCount)
				addSaveJSONObject.put("citm_rush_count", this.rushCount);
			
			if (this.deliveryTime != null && (bContructWithAllItemFields || this.deliveryTime != oriPosCheckItem.deliveryTime || oriPosCheckItem.deliveryTime != null))
				addSaveJSONObject.put("citm_delivery_time", this.deliveryTime.toString(oFormatter));
			else
				addSaveJSONObject.put("citm_delivery_time", "");
			
			if (this.voidLocTime != null && (bContructWithAllItemFields || !this.voidTime.equals(oriPosCheckItem.voidTime))) {
				addSaveJSONObject.put("citm_void_time", this.voidTime);
				addSaveJSONObject.put("citm_void_loctime", this.voidLocTime.toString(oFormatter));
			}
			
			if(bContructWithAllItemFields || this.voidUserId != oriPosCheckItem.voidUserId)
				addSaveJSONObject.put("citm_void_user_id", this.voidUserId);
			if(bContructWithAllItemFields || this.voidStatId != oriPosCheckItem.voidStatId)
				addSaveJSONObject.put("citm_void_stat_id", this.voidStatId);
			if(bContructWithAllItemFields || this.voidVdrsId != oriPosCheckItem.voidVdrsId)
				addSaveJSONObject.put("citm_void_vdrs_id", this.voidVdrsId);
			if(bContructWithAllItemFields || !this.voidConsumed.equals(oriPosCheckItem.voidConsumed))
				addSaveJSONObject.put("citm_void_consumed", this.voidConsumed);
			if(bContructWithAllItemFields || !this.status.equals(oriPosCheckItem.status))
				addSaveJSONObject.putOnce("citm_status", this.status);
			
			//construct the applied check discount item list if exist
			if(this.checkDiscountItemList != null && !this.checkDiscountItemList.isEmpty()) {
				JSONArray appliedChkDiscItemJSONArray = new JSONArray();
				for(PosCheckDiscountItem oCheckDiscountItem:this.checkDiscountItemList) {
					if(oCheckDiscountItem.getCditId() == 0)
						appliedChkDiscItemJSONArray.put(oCheckDiscountItem.constructAddSaveJSON(false));
					else
						appliedChkDiscItemJSONArray.put(oCheckDiscountItem.constructAddSaveJSON(true));
				}
				addSaveJSONObject.put("PosCheckDiscountItem", appliedChkDiscItemJSONArray);
			}
			
			//construct the item discount list if exist
			if(this.itemDiscountList != null && !this.itemDiscountList.isEmpty()) {
				JSONArray itemDiscountJSONArray = new JSONArray();
				for(PosCheckDiscount oCheckDiscount:this.itemDiscountList) {
					if(oCheckDiscount.getCdisId() == 0)
						itemDiscountJSONArray.put(oCheckDiscount.constructAddSaveJSON(false));
					else
						itemDiscountJSONArray.put(oCheckDiscount.constructAddSaveJSON(true));
				}
				addSaveJSONObject.put("PosCheckDiscount", itemDiscountJSONArray);
			}
			
			//construct the extra info list if exist
			if(this.extraInfoList != null && !this.extraInfoList.isEmpty()) {
				JSONArray extraInfoJSONArray = new JSONArray();
				for(PosCheckExtraInfo oExtraInfo:this.extraInfoList){
					if(oExtraInfo.getCkeiId() == 0)
						extraInfoJSONArray.put(oExtraInfo.constructAddSaveJSON(false));
					else
						extraInfoJSONArray.put(oExtraInfo.constructAddSaveJSON(true));
				}
				addSaveJSONObject.put("PosCheckExtraInfo", extraInfoJSONArray);
			}
			
			if(this.splitFromCheckId != 0)
				addSaveJSONObject.put("splitFromCheckId", splitFromCheckId);
			
			this.bModified = false;
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	// *** iSendMode:	0 - Send new items only
	//					1 - Send old items only
	//					2 - Send both new and old items
	protected JSONArray constructMultipleItemAddSaveJSON(List<PosCheckItem> alCheckItems, int iSendMode, boolean bContructWithAllItemFields) {
		int i=0;
		PosCheckItem  oCheckItem = null;
		JSONObject tempCheckItemJSONObject = null;
		JSONArray checkItemJSONArray = new JSONArray();
		boolean bSendOldItemOnly = false;
		boolean bSendNewAndOldItem = false;
		if(iSendMode == 1){
			bSendOldItemOnly = true;
		}else if(iSendMode == 2){
			bSendNewAndOldItem = true;
		}
		
		for(i=0; i<alCheckItems.size(); i++) {
			oCheckItem = alCheckItems.get(i);
			
			if(!bSendNewAndOldItem) {	// by pass checking, if bSendNewAndOldItem which means send all existing items including new and old items
				// check whether item id of old item > 0, item id of new item = 0
				if ((bSendOldItemOnly && oCheckItem.getCitmId() == 0) || (!bSendOldItemOnly && oCheckItem.getCitmId() > 0))
					continue;
				
				// if old item is not modified, skip it
				if (bSendOldItemOnly && !oCheckItem.getModified())
					continue;
			}
			
			if (oCheckItem.getCitmId() == 0)
				tempCheckItemJSONObject = oCheckItem.constructAddSaveJSON(false, bContructWithAllItemFields);
			else
				tempCheckItemJSONObject = oCheckItem.constructAddSaveJSON(true, bContructWithAllItemFields);
			
			if(oCheckItem.modifierCount > 0) {
				JSONArray oModifierJSONArray = new JSONArray();
				JSONObject oModifierJSONObject = null;
				for(PosCheckItem oModifierCheckItem:oCheckItem.getModifierList()) {
					if(oModifierCheckItem.getCitmId() == 0)
						oModifierJSONObject = oModifierCheckItem.constructAddSaveJSON(false, bContructWithAllItemFields);
					else
						oModifierJSONObject = oModifierCheckItem.constructAddSaveJSON(true, bContructWithAllItemFields);
					
					oModifierJSONArray.put(oModifierJSONObject);
				}
				try {
					tempCheckItemJSONObject.put("ModifierList", oModifierJSONArray);
				}catch (JSONException jsone) {
					jsone.printStackTrace();
				}
			}
			
			// Only use this part to send new set menu child item
			if(bSendOldItemOnly == false && oCheckItem.childCount > 0) {
				JSONArray oChildItemJSONArray = new JSONArray();
				JSONObject oChildItemJSONObject = null;
				for(PosCheckItem oChildItemCheckItem:oCheckItem.getChildItemList()) {
					if(oChildItemCheckItem.getCitmId() == 0)
						oChildItemJSONObject = oChildItemCheckItem.constructAddSaveJSON(false, bContructWithAllItemFields);
					else
						oChildItemJSONObject = oChildItemCheckItem.constructAddSaveJSON(true, bContructWithAllItemFields);
					
					if(oChildItemCheckItem.getModifierCount() > 0) {
						JSONArray oModifierJSONArray = new JSONArray();
						JSONObject oModifierJSONObject = null;
						for(PosCheckItem oModifierCheckItem:oChildItemCheckItem.getModifierList()) {
							if(oModifierCheckItem.getCitmId() == 0)
								oModifierJSONObject = oModifierCheckItem.constructAddSaveJSON(false, bContructWithAllItemFields);
							else
								oModifierJSONObject = oModifierCheckItem.constructAddSaveJSON(true, bContructWithAllItemFields);
							
							oModifierJSONArray.put(oModifierJSONObject);
						}
						try {
							oChildItemJSONObject.put("ModifierList", oModifierJSONArray);
						}catch (JSONException jsone) {
							jsone.printStackTrace();
						}
					}
					
					oChildItemJSONArray.put(oChildItemJSONObject);
				}
				try {
					tempCheckItemJSONObject.put("ChildItemList", oChildItemJSONArray);
				}catch (JSONException jsone) {
					jsone.printStackTrace();
				}			
			}
						
			checkItemJSONArray.put(tempCheckItemJSONObject);
		}

		return checkItemJSONArray;
	}
	
	public void copyFromCheckItem (PosCheckItem oCheckItem) {
		int i=0;
		
		this.bdayId = oCheckItem.bdayId;
		this.bperId = oCheckItem.bperId;
		this.shopId = oCheckItem.shopId;
		this.oletId = oCheckItem.oletId;
		this.chksId = oCheckItem.chksId;
		this.cptyId = oCheckItem.cptyId;
		this.itemId = oCheckItem.itemId;
		this.code = oCheckItem.code;
		for(i=0; i<5; i++)
			this.name[i] = oCheckItem.name[i];
		for(i=0; i<5; i++)
			this.shortName[i] = oCheckItem.shortName[i];
		this.parentCitmId = oCheckItem.parentCitmId;
		this.role = oCheckItem.role;
		this.childCount = oCheckItem.childCount;
		this.modifierCount = oCheckItem.modifierCount;
		this.seat = oCheckItem.seat;
		this.mixAndMatchCitmId = oCheckItem.mixAndMatchCitmId;
		this.seq = oCheckItem.seq;
		this.roundTotal = oCheckItem.roundTotal;
		this.total = oCheckItem.total;
		this.roundAmount = oCheckItem.roundAmount;
		this.carryTotal = oCheckItem.carryTotal;
		this.qty = oCheckItem.qty;
		this.baseQty = oCheckItem.baseQty;
		this.price = oCheckItem.price;
		this.originalPrice = oCheckItem.originalPrice;
		this.basicPrice = oCheckItem.basicPrice;
		for(i=0; i<5; i++)
			this.sc[i] = oCheckItem.sc[i];
		for(i=0; i<25; i++)
			this.tax[i] = oCheckItem.tax[i];
		for(i=0; i<4; i++)
			this.inclTaxRef[i] = oCheckItem.inclTaxRef[i];
		this.preDisc = oCheckItem.preDisc;
		this.midDisc = oCheckItem.midDisc;
		this.postDisc = oCheckItem.postDisc;
		this.originalPriceLevel = oCheckItem.originalPriceLevel;
		this.priceLevel = oCheckItem.priceLevel;
		this.revenue = oCheckItem.revenue;
		this.carryRevenue = oCheckItem.carryRevenue;
		this.unitCost = oCheckItem.unitCost;
		for(i=0; i<10; i++)
			this.printQueueItpqId[i] = oCheckItem.printQueueItpqId[i];
		this.noPrint = "";
		for(i=0; i<5; i++)
			this.chargeSc[i] = oCheckItem.chargeSc[i];
		for(i=0; i<25; i++)
			this.chargeTax[i] = oCheckItem.chargeTax[i];
		this.hide = oCheckItem.hide;
		this.icatId = oCheckItem.icatId;
		this.idepId = oCheckItem.idepId;
		this.icouId = oCheckItem.icouId;
		this.digpId = oCheckItem.digpId;
		this.getRevenue = oCheckItem.getRevenue;
		this.servingStatus = oCheckItem.servingStatus;
		this.pending = oCheckItem.pending;
		this.orderingType = oCheckItem.orderingType;
		this.roundStatus = oCheckItem.roundStatus;
		
		if(oCheckItem.orderLocTime != null)
			this.orderLocTime = oCheckItem.orderLocTime;
		
		if(oCheckItem.orderTime != null)
			this.orderTime = oCheckItem.orderTime;
		
		this.orderUserId = oCheckItem.orderUserId;
		this.orderStatId = oCheckItem.orderStatId;
		
		if(oCheckItem.rushTime != null)
			this.rushTime = oCheckItem.rushTime;
		
		if(oCheckItem.rushLocTime != null)
			this.rushLocTime = oCheckItem.rushLocTime;
		
		this.rushUserId = oCheckItem.rushUserId;
		this.rushStatId = oCheckItem.rushStatId;
		this.rushCount = oCheckItem.rushCount;
		
		if(oCheckItem.deliveryTime != null)
			this.deliveryTime = oCheckItem.deliveryTime;
		
		if(oCheckItem.voidTime != null)
			this.voidTime = oCheckItem.voidTime;
		
		if(oCheckItem.voidLocTime != null)
			this.voidLocTime = oCheckItem.voidLocTime;
		
		this.voidUserId = oCheckItem.voidUserId;
		this.voidStatId = oCheckItem.voidStatId;
		this.voidVdrsId = oCheckItem.voidVdrsId;
		this.voidConsumed = oCheckItem.voidConsumed;
		this.status = oCheckItem.status;

		for(i = 0; i < oCheckItem.itemDiscountList.size(); i++) {
			this.itemDiscountList.add(oCheckItem.itemDiscountList.get(i));
		}
		
		for(i = 0; i < oCheckItem.extraInfoList.size(); i++) {
			this.extraInfoList.add(oCheckItem.extraInfoList.get(i));
		}
	}
	
	//reset value
	public void init () {
		int i=0;
		
		this.citmId = 0;
		this.bdayId = 0;
		this.bperId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.chksId = 0;
		this.cptyId = 0;
		this.itemId = 0;
		this.code = "";
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.parentCitmId = 0;
		this.role = PosCheckItem.ROLE_BASIC_ITEM;
		this.childCount = 0;
		this.modifierCount = 0;
		this.seat = 0;
		this.mixAndMatchCitmId = 0;
		this.seq = 0;
		this.roundTotal = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.roundAmount = BigDecimal.ZERO;
		this.carryTotal = BigDecimal.ZERO;
		this.qty = BigDecimal.ZERO;
		this.baseQty = BigDecimal.ZERO;
		this.price = BigDecimal.ZERO;
		this.originalPrice = BigDecimal.ZERO;
		this.basicPrice = BigDecimal.ZERO;
		if(this.sc == null)
			this.sc = new BigDecimal[5];
		for(i=1; i<=5; i++)
			this.sc[i-1] = BigDecimal.ZERO;
		if(this.tax == null)
			this.tax = new BigDecimal[25];
		for(i=1; i<=25; i++)
			this.tax[i-1] = BigDecimal.ZERO;
		if(this.inclTaxRef == null)
			this.inclTaxRef = new BigDecimal[4];
		for(i=1; i<=4; i++)
			this.inclTaxRef[i-1] = BigDecimal.ZERO;
		this.preDisc = BigDecimal.ZERO;
		this.midDisc = BigDecimal.ZERO;
		this.postDisc = BigDecimal.ZERO;
		this.originalPriceLevel = 0;
		this.priceLevel = 0;
		this.revenue = BigDecimal.ZERO;
		this.carryRevenue = BigDecimal.ZERO;
		this.unitCost = BigDecimal.ZERO;
		if(this.printQueueItpqId == null)
			this.printQueueItpqId = new int[10];
		for(i=0; i<10; i++)
			this.printQueueItpqId[i] = 0;
		this.noPrint = PosCheckItem.NO_PRINT_NO;
		if(this.chargeSc == null)
			this.chargeSc = new String[5];
		for (i=0; i<5; i++)
			this.chargeSc[i] = PosCheckItem.CHARGE_SC_NO;
		if(this.chargeTax == null)
			this.chargeTax = new String[25];
		for (i=0; i<25; i++)
			this.chargeTax[i] = PosCheckItem.CHARGE_TAX_NO;
		this.hide = PosCheckItem.HIDE_NO;
		this.icatId = 0;
		this.idepId = 0;
		this.icouId = 0;
		this.digpId = 0;
		this.getRevenue = PosCheckItem.GET_REVENUE_NO_REVENUE;
		this.servingStatus = PosCheckItem.SERVING_STATUS_ORDERED;
		this.pending = PosCheckItem.PENDING_NORMAL_ITEM;
		this.orderingType = PosCheckItem.ORDERING_TYPE_FINE_DINING;
		this.roundStatus = PosCheckItem.ROUND_STATUS_SAVED_BEFORE_ROUNDING;
		this.orderLocTime = null;
		this.orderTime = null;
		this.orderUserId = 0;
		this.orderStatId = 0;
		this.rushTime = null;
		this.rushLocTime = null;
		this.rushUserId = 0;
		this.rushStatId = 0;
		this.rushCount = 0;
		this.deliveryTime = null;
		this.voidTime = null;
		this.voidLocTime = null;
		this.voidUserId = 0;
		this.voidStatId = 0;
		this.voidVdrsId = 0;
		this.voidConsumed = PosCheckItem.VOID_CONSUMED_NO;
		this.status = PosCheckItem.STATUS_NORMAL;

		this.bModified = false;
		if(this.modifierList == null)
			this.modifierList = new ArrayList<PosCheckItem>();
		else
			this.modifierList.clear();
		
		if(this.childItemList == null)
			this.childItemList = new ArrayList<PosCheckItem>();
		else
			this.childItemList.clear();
		
		if(this.checkDiscountItemList == null)
			this.checkDiscountItemList = new ArrayList<PosCheckDiscountItem>();
		else
			this.checkDiscountItemList.clear();
		
		if(this.itemDiscountList == null)
			this.itemDiscountList = new ArrayList<PosCheckDiscount>();
		else
			this.itemDiscountList.clear();
		
		if(this.extraInfoList == null)
			this.extraInfoList = new ArrayList<PosCheckExtraInfo>();
		else
			this.extraInfoList.clear();
		
		this.splitFromCheckId = 0;
	}
	
	// Update original value after update to database
	public void updateOriginalValue(){
		if(this.oriPosCheckItem != null)
			this.oriPosCheckItem.copyFromCheckItem(this);
	}
	
	public boolean printSpecialSlip(int iCheckId, String sType, JSONObject oHeader, JSONObject oInformation, ArrayList<Integer> oItemIds, int iCurrentLang) {
		JSONObject requestJSONObject = new JSONObject(), oTempJSONObject;
		JSONArray oCitmIdArray = new JSONArray();
		
		try {
			requestJSONObject.put("type", sType);
			requestJSONObject.put("checkId", iCheckId);
			requestJSONObject.put("header", oHeader);
			if(oItemIds == null) {
				oTempJSONObject = new JSONObject();			
				oTempJSONObject.put("id", this.citmId);
				oCitmIdArray.put(oTempJSONObject);
			}else {
				for(Integer oItemId:oItemIds) {
					oTempJSONObject = new JSONObject();
					oTempJSONObject.put("id", oItemId.intValue());
					oCitmIdArray.put(oTempJSONObject);
				}
			}
			oInformation.put("citmIds", oCitmIdArray);
			requestJSONObject.put("info", oInformation);
			requestJSONObject.put("currentLang", iCurrentLang);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}

		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "printSpecialSlip", requestJSONObject.toString(), true))
			return false;
		else
			return true;
	}
	
	//add or update a check item to database
	public boolean addUpdate(boolean bUpdate, int iActionUserId, int iActionStationId, boolean bIsVoidCheck) {
		JSONObject requestJSONObject = new JSONObject();
		JSONObject responseJSONObject = null;
		
		requestJSONObject = this.constructAddSaveJSON(bUpdate, false);
		
		// Set action user id and station id
		try {
			requestJSONObject.put("actionUserId", iActionUserId);
			requestJSONObject.put("actionStationId", iActionStationId);
			requestJSONObject.put("isVoidCheck", bIsVoidCheck);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveCheckItem", requestJSONObject.toString(), false))
			return false; 
		else {
			responseJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse();
			if(bUpdate == false && !responseJSONObject.isNull("id")) 
				this.citmId = responseJSONObject.optInt("id");
			return true;
		}
	}
	
	//add or update check item with array list
	public boolean printUpdateWithMutliplePendingRecord(List<PosCheckItem> oCheckItemList) {
		JSONArray checkItemJSONArray = new JSONArray();
		
		for(int i = 0; i < oCheckItemList.size(); i++) {
			PosCheckItem oCheckItem = oCheckItemList.get(i);
			if (oCheckItem.getCitmId() == 0)
				continue;

			JSONObject tempCheckItemJSONObject = oCheckItem.constructAddSaveJSON(true, true);
			checkItemJSONArray.put(tempCheckItemJSONObject);
			
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "savePrintPendingCheckItem", checkItemJSONArray.toString(), false))
			return false;
		else
			return true;
	}
	
	//add item discount to list
	public void addItemDiscountToList(PosCheckDiscount oCheckDiscount) {
		this.itemDiscountList.add(oCheckDiscount);
	}
	
	public void addExtraInfoToList(PosCheckExtraInfo oCheckExtraInfo) {
		this.extraInfoList.add(oCheckExtraInfo);
	}
	
	//get citmId
	public int getCitmId() {
		return this.citmId;
	}
	
	//get cptrId
	public int getCptyId() {
		return this.cptyId;
	}
	
	//get name by lang index
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get short name by lang index
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get seat number
	public int getSeatNo() {
		return this.seat;
	}
	
	//get sequence
	public int getSeq() {
		return this.seq;
	}
	
	//get charge sc
	public String getChargeSc(int iIndex) {
		return this.chargeSc[(iIndex-1)];
	}
	
	//get charge tax
	public String getChargeTax(int iIndex) {
		return this.chargeTax[(iIndex-1)];
	}
	
	//get ordered user id
	public int getOrderUserId() {
		return this.orderUserId;
	}
	
	//get ordered stat id
	public int getOrderStatId() {
		return this.orderStatId;
	}
	
	//get int print queue with index
	public int getPrintQueueWithIndex(int iIndex) {
		if(iIndex >= 1 && iIndex <= 10)
			return this.printQueueItpqId[(iIndex-1)];
		else
			return 0;
	}

	public String getNoPrint() {
		return this.noPrint;
	}
	
	public String getHide() {
		return this.hide;
	}
	
	//get pending
	public String getPending() {
		return this.pending;
	}

	public String getOrderTime() {
		return this.orderTime;
	}
	
	public DateTime getOrderLocTime() {
		return this.orderLocTime;
	}
	
	//get void local time
	public DateTime getVoidLocalTime() {
		return this.voidLocTime;
	}
	
	//get delivery time
	public DateTime getDeliveryTime() {
		return this.deliveryTime;
	}
	
	public int getCheckId() {
		return this.chksId;
	}
	
	public int getItemId() {
		return this.itemId;
	}
	
	public int getBusinessDayId() {
		return this.bdayId;
	}
	
	public int getBusinessPeriodId() {
		return this.bperId;
	}
	
	public int getShopId() {
		return this.shopId;
	}
	
	public int getOutletId() {
		return this.oletId;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public int getParentItemId() {
		return this.parentCitmId;
	}
	
	public int getMixAndMatchItemId() {
		return this.mixAndMatchCitmId;
	}
	
	public String getRole() {
		return this.role;
	}
	
	public int getChildCount() {
		return this.childCount;
	}
	
	public int getModifierCount() {
		return this.modifierCount;
	}
	
	public BigDecimal getRoundTotal() {
		return this.roundTotal;
	}
	
	public BigDecimal getTotal() {
		return this.total;
	}
	
	public BigDecimal getRoundAmount() {
		return this.roundAmount;
	}
	
	public BigDecimal getCarryTotal() {
		return this.carryTotal;
	}
	
	public BigDecimal getQty() {
		return this.qty;
	}
	
	public BigDecimal getBaseQty() {
		return this.baseQty;
	}
	
	public BigDecimal getPrice() {
		return this.price;
	}

	public int getCategoryId() {
		return this.icatId;
	}
	
	public int getDepartmentId() {
		return this.idepId;
	}
	
	public int getCourseId() {
		return this.icouId;
	}
	
	public int getDiscountItemGroupId() {
		return this.digpId;
	}
	
	public String getGetRevenue() {
		return this.getRevenue;
	}
	
	public String getServingStatus() {
		return this.servingStatus;
	}

	public BigDecimal getOriginalPrice() {
		return this.originalPrice;
	}
	
	public BigDecimal getBasicPrice() {
		return this.basicPrice;
	}

	public BigDecimal getRevenue() {
		return this.revenue;
	}
	
	public BigDecimal getCarryRevenue() {
		return this.carryRevenue;
	}
	
	public BigDecimal getUnitCost() {
		return this.unitCost;
	}
	
	public BigDecimal getSc(int iIndex) {
		return this.sc[(iIndex-1)];
	}
	
	public BigDecimal getTax(int iIndex) {
		return this.tax[(iIndex-1)];
	}
	
	public BigDecimal getInclusiveTaxRef(int iIndex) {
		return this.inclTaxRef[(iIndex-1)];
	}
	
	public BigDecimal getPreDisc() {
		return this.preDisc;
	}
	
	public BigDecimal getMidDisc() {
		return this.midDisc;
	}
	
	public BigDecimal getPostDisc() {
		return this.postDisc;
	}
	
	public int getOriginalPriceLevel() {
		return this.originalPriceLevel;
	}
	
	public int getPriceLevel() {
		return this.priceLevel;
	}

	public String getOrderingType() {
		return this.orderingType;
	}
	
	public String getRoundStatus() {
		return this.roundStatus;
	}

	public String getRushTime() {
		return this.rushTime;
	}
	
	public DateTime getRushLocTime() {
		return this.rushLocTime;
	}
	
	public int getRushUserId() {
		return this.rushUserId;
	}
	
	public int getRushStatId() {
		return this.rushStatId;
	}
	
	public int getRushCount() {
		return this.rushCount;
	}
	
	public String getStatus(){
		return this.status;
	}
	
	public boolean getModified() {
		return this.bModified;
	}
	
	//check whether have modifiers
	public boolean hasModifier() {
		if(!this.modifierList.isEmpty())
			return true;
		else
			return false;
	}
	
	//get modifier list
	public List<PosCheckItem> getModifierList() {
		return this.modifierList;
	}
	
	//check whether have modifiers
	public boolean hasChildItem() {
		if(!this.childItemList.isEmpty())
			return true;
		else
			return false;
	}
	
	//get child item list
	public List<PosCheckItem> getChildItemList() {
		return this.childItemList;
	}
	
	//get item discount list
	public List<PosCheckDiscount> getItemDiscountList() {
		return this.itemDiscountList;
	}
	
	//check whether have extra infos
	public boolean hasExtraInfo() {
		if(!this.extraInfoList.isEmpty())
			return true;
		else
			return false;
	}
	
	//get item extra info list
	public List<PosCheckExtraInfo> getExtraInfoList() {
		return this.extraInfoList;
	}
	
	public boolean havePrintQueue() {
		boolean bFound = false;
		
		for(int i = 0; i < this.printQueueItpqId.length; i++) {
			if (this.printQueueItpqId[i] > 0) {
				bFound = true;
				break;
			}
		}
		
		return bFound;
	}
	
	public boolean haveTaxSc(boolean bTaxSc) {
		boolean bFound = false;
		if (bTaxSc) {
			for(int i = 0; i < this.tax.length; i++) {
				if (this.tax[i].compareTo(BigDecimal.ZERO) > 0) {
					bFound = true;
					break;
				}
			}
		} else {
			for(int i = 0; i < this.sc.length; i++) {
				if(this.sc[i].compareTo(BigDecimal.ZERO) > 0) {
					bFound = true;
					break;
				}
			}
		}
		
		return bFound;
	}
	
	public int getVoidVdrsId() {
		return voidVdrsId;
	}
	
	public void setItemId(int iItemId) {
		this.itemId = iItemId;
	}
	
	//set check item id
	public void setCheckItemId(int iCitmId) {
		this.citmId = iCitmId;
	}
	
	//set check id
	public void setCheckId(int iCheckId) {
		this.chksId = iCheckId;
	}
	
	//set check id
	public void setCheckPartyId(int iCheckPartyId) {
		this.cptyId = iCheckPartyId;
	}
	
	public void setBusinessDayId(int iBusinessDayId) {
		this.bdayId = iBusinessDayId;
	}
	
	public void setBusinessPeriodId(int iBusinessPeriodId) {
		this.bperId = iBusinessPeriodId;
	}
	
	public void setShopId(int iShopId) {
		this.shopId = iShopId;
	}
	
	//set outlet id
	public void setOutletId(int iOutletId) {
		this.oletId = iOutletId;
	}
	
	//set order user id
	public void setOrderUserId(int iUserId) {
		this.orderUserId = iUserId;
	}
	
	//set order station id
	public void setOrderStationId(int iStationId) {
		this.orderStatId = iStationId;
	}
	
	//set name by lang index
	public void setName(int iIndex, String sName) {
		this.name[(iIndex-1)] = sName;
	}
	
	//set short name by lang index
	public void setShortName(int iIndex, String sShortName) {
		this.shortName[(iIndex-1)] = sShortName;
	}
	
	//set seat number
	public void setSeatNo(int iSeatNo) {
		this.seat = iSeatNo;
	}
	
	//set seq
	public void setSeq(int iSeq) {
		this.seq = iSeq;
	}
	
	//set price
	public void setPrice(BigDecimal dPrice) {
		this.price = dPrice;
	}
	
	//set original price
	public void setOriginalPrice(BigDecimal dOriPrice) {
		this.originalPrice = dOriPrice;
	}
	
	//set total
	public void setTotal(BigDecimal dTotal) {
		this.total = dTotal;
	}

	public void setSc(int iIndex, BigDecimal dSc) {
		this.sc[(iIndex-1)] = dSc;
	}
	
	public void setTax(int iIndex, BigDecimal dTax) {
		this.tax[(iIndex-1)] = dTax;
	}
	
	public void setInclusiveTaxRef(int iIndex, BigDecimal dInclusiveTaxRef) {
		this.inclTaxRef[(iIndex-1)] = dInclusiveTaxRef;
	}
	
	public void setPreDisc(BigDecimal dPreDiscount) {
		this.preDisc = dPreDiscount;
	}
	
	public void setMidDisc(BigDecimal dMidDiscount) {
		this.midDisc = dMidDiscount;
	}
	
	public void setPosDisc(BigDecimal dPostDiscount) {
		this.postDisc = dPostDiscount;
	}

	
	//set print queue with index
	public void setPrintQueueWithIndex(int iIndex, int iPrintQueueId) {
		this.printQueueItpqId[(iIndex-1)] = iPrintQueueId;
	}
	
	public void setPending(String sPending) {
		this.pending = sPending;
	}
	
	public void setOrderTime(String sOrderTime) {
		this.orderTime = sOrderTime;
	}
	
	public void setOrderLocTime(DateTime oOrderLocTime) {
		this.orderLocTime = oOrderLocTime;
	}
	
	//set charge sc
	public void setChargeSc(int iIndex, String sChargeSc) {
		this.chargeSc[(iIndex-1)] = sChargeSc;
	}
	
	//set charge tax
	public void setChargeTax(int iIndex, String sChargeTax) {
		this.chargeTax[(iIndex-1)] = sChargeTax;
	}

	public void setNoPrint(String sNoPrint) {
		this.noPrint = sNoPrint;
	}
	
	public void setHide(String sHide) {
		this.hide = sHide;
	}
	
	public void setRevenue(BigDecimal dRevenue) {
		this.revenue = dRevenue;
	}
	
	public void setCarryRevenue(BigDecimal dCarryRevenue) {
		this.carryRevenue = dCarryRevenue;
	}
	
	public void setUnitCost(BigDecimal dUnitCost) {
		this.unitCost = dUnitCost;
	}
	
	//set delivery time
	public void setDeliveryTime(DateTime oDeliveryTime) {
		this.deliveryTime = oDeliveryTime;
	}
	
	//set void time
	public void setVoidTime(String sVoidTime) {
		this.voidTime = sVoidTime;
	}
	
	//set void local time
	public void setVoidLocalTime(DateTime oVoidLocalTime) {
		this.voidLocTime = oVoidLocalTime;
	}
	
	//set void user id
	public void setVoidUserId(int iUserId) {
		this.voidUserId = iUserId;
	}
	
	//set void station id
	public void setVoidStationId(int iStationId) {
		this.voidStatId = iStationId;
	}
	
	//set void reason id
	public void setVoidReasonId(int iVoidCodeId) {
		this.voidVdrsId = iVoidCodeId;
	}
	
	//set applied check discount list
	public void setCheckDiscountItemList(List<PosCheckDiscountItem> oCheckDiscountItemList) {
		this.checkDiscountItemList = oCheckDiscountItemList;
	}
	
	//cleanup applied check discount list
	public void cleanCheckDiscountItemList() {
		this.checkDiscountItemList.clear();
	}
	
	//set item discount list
	public void setItemDiscountList(List<PosCheckDiscount> oItemDiscList) {
		this.itemDiscountList = oItemDiscList;
	}
	
	//set modifier list
	public void setModifierList(List<PosCheckItem> oModifierList) {
		this.modifierList = oModifierList;
	}
	
	//set child item list
	public void setChildItemList(List<PosCheckItem> oChildItemList) {
		this.childItemList = oChildItemList;
	}
	
	//set extra info list
	public void setExtraInfoList(List<PosCheckExtraInfo> oExtraInfoList) {
		this.extraInfoList = oExtraInfoList;
	}
	
	public void setCode(String sCode) {
		this.code = sCode;
	}
	
	public void setParentItemId(int iParentCitmId) {
		this.parentCitmId = iParentCitmId;
	}
	
	public void setMixAndMatchItemId(int iMixAndMatchCitmId) {
		this.mixAndMatchCitmId = iMixAndMatchCitmId;
	}
	
	public void setRole(String sRole) {
		this.role = sRole;
	}
	
	public void setModifierCount(int iModifierCount) {
		this.modifierCount = iModifierCount;
	}
	
	public void setChildCount(int iChildCount) {
		this.childCount = iChildCount;
	}
	
	public void setRoundTotal(BigDecimal dRoundTotal) {
		this.roundTotal = dRoundTotal;
	}
	
	public void setRoundAmount(BigDecimal dRoundAmount) {
		this.roundAmount = dRoundAmount;
	}
	
	public void setCarryTotal(BigDecimal dCarryTotal) {
		this.carryTotal = dCarryTotal;
	}
	
	public void setQty(BigDecimal dQty) {
		this.qty = dQty;
	}
	
	public void setBaseQty(BigDecimal dBaseQty) {
		this.baseQty = dBaseQty;
	}

	public void setCategoryId(int iCatId) {
		this.icatId = iCatId;
	}
	
	public void setDepartmentId(int iDepId) {
		this.idepId = iDepId;
	}
	
	public void setCourseId(int iCouId) {
		this.icouId = iCouId;
	}
	
	public void setDiscountItemGroupId(int iDigpId) {
		this.digpId = iDigpId;
	}
	
	public void setGetRevenue(String sGetRevenue) {
		this.getRevenue = sGetRevenue;
	}
	
	public void setServingStatus(String sServingStatus) {
		this.servingStatus = sServingStatus;
	}

	public void setOrderingType(String sOrderingType) {
		this.orderingType = sOrderingType;
	}
	
	public void setRoundStatus(String sRoundStatus) {
		this.roundStatus = sRoundStatus;
	}
	
	public void setOrderStatId(int iOrderStatId) {
		this.orderStatId = iOrderStatId;
	}
	
	public void setBasicPrice(BigDecimal dBasicPrice) {
		this.basicPrice = dBasicPrice;
	}
	
	public void setOriginalPriceLevel(int iOriginalPriceLevel) {
		this.originalPriceLevel = iOriginalPriceLevel;
	}
	
	public void setPriceLevel(int iPriceLevel) {
		this.priceLevel = iPriceLevel;
	}

	public void setRushTime(String sRushTime) {
		this.rushTime = sRushTime;
	}
	
	public void setRushLocTime(DateTime oRushLocTime) {
		this.rushLocTime = oRushLocTime;
	}
	
	public void setRushUserId(int iRushUserId) {
		this.rushUserId = iRushUserId;
	}
	
	public void setRushStatId(int iRushStatId) {
		this.rushStatId = iRushStatId;
	}
	
	public void setRushCount(int iRushCount) {
		this.rushCount = iRushCount;
	}
	
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	public void setModified(boolean bModified) {
		this.bModified = bModified;
	}
	
	public void setSplitFromCheckId(int iSplitFromCheckId) {
		this.splitFromCheckId = iSplitFromCheckId;
	}
	
	public void addValueToTotal(BigDecimal dAddValue) {
		this.total = this.total.add(dAddValue);
	}
	
	public void multipleValueToTotal(BigDecimal dMultipleValue) {
		this.total = this.total.multiply(dMultipleValue);
	}
	public void addValueToPreDisc(BigDecimal dAddValue) {
		this.preDisc = this.preDisc.add(dAddValue);
	}
	
	public void addValueToMidDisc(BigDecimal dAddValue) {
		this.midDisc = this.midDisc.add(dAddValue);
	}

	public void addValueToPostDisc(BigDecimal dAddValue) {
		this.postDisc = this.postDisc.add(dAddValue);
	}

	public void increaseModifierCount() {
		this.modifierCount++;
	}
	
	public void decreaseModifierCount() {
		this.modifierCount--;
	}
	
	public void increaseChildCount() {
		this.childCount++;
	}
	
	public void decreaseChildCount() {
		this.childCount--;
	}

	public void resetItemDiscountList() {
		this.itemDiscountList.clear();
	}
	
	public void resetModifierList() {
		this.modifierList.clear();
	}
	
	public void resetChildItemList() {
		this.childItemList.clear();
	}
	
	public void resetExtraInfoList() {
		this.extraInfoList.clear();
	}
	
	public boolean isBasicItem() {
		return this.role.equals(PosCheckItem.ROLE_BASIC_ITEM);
	}
	
	public boolean isSetMenuChildItem() {
		return this.role.equals(PosCheckItem.ROLE_SET_MENU_CHILD_ITEM);
	}
	
	public boolean isModifierItem() {
		return this.role.equals(PosCheckItem.ROLE_MODIFIER_ITEM);
	}
	
	public boolean isDeleted() {
		return this.status.equals(PosCheckItem.STATUS_DELETED);
	}
	
	public boolean isTakeoutOrderingType() {
		return this.orderingType.equals(PosCheckItem.ORDERING_TYPE_TAKEOUT);
	}
	
	public boolean isFineDiningOrderingType() {
		return this.orderingType.equals(PosCheckItem.ORDERING_TYPE_FINE_DINING);
	}
	
	public boolean isPendingItem() {
		return this.pending.equals(PosCheckItem.PENDING_PENDING_ITEM);
	}
}
