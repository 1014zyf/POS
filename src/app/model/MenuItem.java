package app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuItem {
	private int itemId;
	private String code;
	private String[] name;
	private String[] shortName;
	private String[] desc1;
	private String[] desc2;
	private String[] info;
	private BigDecimal[] basicPrice;
	private BigDecimal basicMinPrice;
	private BigDecimal basicMaxPrice;
	private BigDecimal[] childPrice;
	private BigDecimal childMinPrice;
	private BigDecimal childMaxPrice;
	private String childGetRevenue;
	private BigDecimal[] modifierPrice;
	private BigDecimal modifierMinPrice;
	private BigDecimal modifierMaxPrice;
	private BigDecimal[] modifierRate;
	private BigDecimal modifierMinRate;
	private BigDecimal modifierMaxRate;
	private String modifierOperator;
	private String modifierGetRevenue;
	private BigDecimal cost;
	private int stsgId;
	private String[] chargeSc;
	private String[] chargeTax;
	private int spqgId;
	private int[] printQueue;
	private String noPrintItem;
	private String noPrintChild;
	private String noPrintModifier;
	private int smmgId;
	private int[] selectModifierMenuId;
	private String selectModifierMethod;
	private int icatId;
	private int idepId;
	private int icouId;
	private int digpId;
	private int oigpId;
	private int childCount;
	private String asBasic;
	private String asChild;
	private String asModifier;
	private String inputName;
	private BigDecimal minOrderQty;
	private BigDecimal maxOrderQty;
	private String hide;
	private String mode;
	private String status;
	
	private List<MenuSetMenuLookup> setMenuLookups;
	private List<MenuMenu> modifierMenuList;
	private List<MenuMediaObject> menuMediaObjectsList;
	
	// childGetRevenue
	public static String CHILD_GET_REVENUE_NO = "";
	public static String CHILD_GET_REVENUE_SPILT_CHILD_PRICE = "p";
	public static String CHILD_GET_REVENUE_SPILT_BASIC_PRICE = "b";
	
	// modifierOperator
	public static String MODIFIER_OPERATOR_ADD_UNIT_MODIFIER_PRICE_TO_UNIT_PRICE = "";
	public static String MODIFIER_OPERATOR_MULTIPLE_RATE = "x";
	public static String MODIFIER_OPERATOR_ADD_UNIT_MODIFIER_PRICE_TO_TOTAL = "+";

	// modifierOperator
	private static String MODIFIER_OPERATOR_ADD_TO_UNIT_PRICE = "";
	private static String MODIFIER_OPERATOR_MULTIPLE = "x";
	private static String MODIFIER_OPERATOR_ADD_TO_ITEM_TOTAL = "+";
	
	// modifierGetRevenue
	public static String MODIFIER_GET_REVENUE_NO = "";
	public static String MODIFIER_GET_REVENUE_SPILT_MODIFIER_PRICE = "p";
	public static String MODIFIER_GET_REVENUE_SPILT_BASIC_PRICE = "b";
	
	// noPrintItem
	public static String NO_PRINT_ITEM_NO = "";
	public static String NO_PRINT_ITEM_YES = "y";
	
	// noPrintChild
	public static String NO_PRINT_CHILD_NO = "";
	public static String NO_PRINT_CHILD_YES = "y";
	
	// noPrintModifier
	public static String NO_PRINT_MODIFIER_NO = "";
	public static String NO_PRINT_MODIFIER_YES = "y";
	
	// chargeSc
	public static String CHARGE_SC_NO = "";
	public static String CHARGE_SC_CHARGE = "c";
	public static String CHARGE_SC_ALREADY_CHARGED = "i";
	
	// chargeTax
	public static String CHARGE_TAX_NO = "";
	public static String CHARGE_TAX_CHARGE = "c";
	public static String CHARGE_TAX_ALREADY_CHARGED = "i";
	public static String CHARGE_TAX_INCLUSIVE_WITHOUT_BREAKDOWN = "n";

	// selectModifierMethod
	private static String SELECT_MODIFIER_METHOD_MANUALLY = "";
	private static String SELECT_MODIFIER_METHOD_FORCE_MODIFIER = "f";

	// asBasic
	public static String AS_BASIC_NOT_ALLOW = "";
	public static String AS_BASIC_ALLOW = "y";
	
	// asChild
	public static String AS_CHILD_NOT_ALLOW = "";
	public static String AS_CHILD_ALLOW = "y";
	
	// asModifier
	public static String AS_MODIFIER_NOT_ALLOW = "";
	public static String AS_MODIFIER_ALLOW ="y";
	
	// inputName
	private static String INPUT_NAME_NORMAL = "";
	private static String INPUT_NAME_OPEN_DESCRIPTION = "o";
	private static String INPUT_NAME_APPEND_DESCRIPTION = "a";
	
	// hide
	public static String HIDE_NO = "";
	public static String HIDE_YES = "y";

	// mode
	public static String MODE_NORMAL = "";
	private static String MODE_DISABLE = "d";
	private static String MODE_INACTIVE = "i";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_SUSPENDED = "s";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public MenuItem() {
		this.init();
	}

	public MenuItem(MenuItem oMenuItem) {
		int i=0;
		this.init();
		
		this.itemId = oMenuItem.itemId;
		this.code = oMenuItem.code;
		for(i=0; i<5; i++)
			this.name[i] = oMenuItem.name[i];
		for(i=0; i<5; i++)
			this.shortName[i] = oMenuItem.shortName[i];
		for(i=0; i<5; i++)
			this.desc1[i] = oMenuItem.desc1[i];
		for(i=0; i<5; i++)
			this.desc2[i] = oMenuItem.desc2[i];
		for(i=0; i<5; i++)
			this.info[i] = oMenuItem.info[i];
		for(i=0; i<=10; i++) 
			this.basicPrice[i] = oMenuItem.basicPrice[i];
		this.basicMinPrice = oMenuItem.basicMinPrice;
		this.basicMaxPrice = oMenuItem.basicMaxPrice;
		for(i=0; i<=10; i++)
			this.childPrice[i] = oMenuItem.childPrice[i];
		this.childMinPrice = oMenuItem.childMinPrice;
		this.childMaxPrice = oMenuItem.childMaxPrice;
		this.childGetRevenue = oMenuItem.childGetRevenue;
		for(i=0; i<=10; i++)
			this.modifierPrice[i] = oMenuItem.modifierPrice[i];
		this.modifierMinPrice = oMenuItem.modifierMinPrice;
		this.modifierMaxPrice = oMenuItem.modifierMaxPrice;
		for(i=0; i<=10; i++)
			this.modifierRate[i] = oMenuItem.modifierRate[i];
		this.modifierMinRate = oMenuItem.modifierMinRate;
		this.modifierMaxRate = oMenuItem.modifierMaxRate;
		this.modifierOperator = oMenuItem.modifierOperator;
		this.modifierGetRevenue = oMenuItem.modifierGetRevenue;
		this.cost = oMenuItem.cost;
		this.stsgId = oMenuItem.stsgId;
		for(i=1; i<=5; i++)
			this.chargeSc[i-1] = oMenuItem.chargeSc[i-1];
		for(i=1; i<=25; i++)
			this.chargeTax[i-1] = oMenuItem.chargeTax[i-1];
		this.spqgId = 0;
		for(i=1; i<=10; i++)
			this.printQueue[i-1] = oMenuItem.printQueue[i-1];
		this.noPrintItem = oMenuItem.noPrintItem;
		this.noPrintChild = oMenuItem.noPrintChild;
		this.noPrintModifier = oMenuItem.noPrintModifier;
		this.smmgId = oMenuItem.smmgId;
		for(i=1; i<=10; i++)
			this.selectModifierMenuId[i-1] = oMenuItem.selectModifierMenuId[i-1];
		this.selectModifierMethod = oMenuItem.selectModifierMethod;
		this.icatId = oMenuItem.icatId;
		this.idepId = oMenuItem.idepId;
		this.icouId = oMenuItem.icouId;
		this.digpId = oMenuItem.digpId;
		this.oigpId = oMenuItem.oigpId;
		this.childCount = oMenuItem.childCount;
		this.asBasic = oMenuItem.asBasic;
		this.asChild = oMenuItem.asChild;
		this.asModifier = oMenuItem.asModifier;
		this.inputName = oMenuItem.inputName;
		this.minOrderQty = oMenuItem.minOrderQty;
		this.maxOrderQty = oMenuItem.maxOrderQty;
		this.hide = oMenuItem.hide;
		this.mode = oMenuItem.mode;
		this.status = oMenuItem.status;
		
		for(i = 0; i < oMenuItem.setMenuLookups.size(); i++) {
			this.setMenuLookups.add(oMenuItem.setMenuLookups.get(i));
		}
		
		for(i = 0; i < oMenuItem.modifierMenuList.size(); i++) {
			this.modifierMenuList.add(oMenuItem.modifierMenuList.get(i));
		}
		
		for(i = 0; i < oMenuItem.menuMediaObjectsList.size(); i++) {
			this.menuMediaObjectsList.add(oMenuItem.menuMediaObjectsList.get(i));
		}
	}
	
	//init object by JSON Object
	public MenuItem(JSONObject menuItemJSONObject) {
		readDataFromJson(menuItemJSONObject);
	}
	
	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bResult = true;
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bResult = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("item")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("item")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("item");
			if(tempJSONObject.isNull("MenuItem")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
		
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject menuItemJSONObject) {
		JSONObject resultMenuItem = null;
		int i;
		
		resultMenuItem = menuItemJSONObject.optJSONObject("MenuItem");
		if(resultMenuItem == null)
			resultMenuItem = menuItemJSONObject;
			
		this.init();
		this.itemId = resultMenuItem.optInt("item_id");
		this.code = resultMenuItem.optString("item_code");
		
		for(i=1; i<=5; i++) {
			this.name[(i-1)] = resultMenuItem.optString("item_name_l"+i);
		}
		
		for(i=1; i<=5; i++) {
			this.shortName[(i-1)] = resultMenuItem.optString("item_short_name_l"+i);
		}
		
		for(i=1; i<=5; i++) {
			this.desc1[(i-1)] = resultMenuItem.optString("item_desc1_l"+i, null);
		}
		
		for(i=1; i<=5; i++) {
			this.desc2[(i-1)] = resultMenuItem.optString("item_desc2_l"+i, null);
		}
		
		for(i=1; i<=5; i++) {
			this.info[(i-1)] = resultMenuItem.optString("item_info_l"+i, null);
		}
		
		for(i=0; i<=10; i++) {
			String sBasicPrice = resultMenuItem.optString("item_basic_price"+i);
			if(!sBasicPrice.isEmpty())
				this.basicPrice[i] = new BigDecimal(sBasicPrice);
		}
		
		String sBasicMinPrice = resultMenuItem.optString("item_basic_min_price");
		if(!sBasicMinPrice.isEmpty())
			this.basicMinPrice = new BigDecimal(sBasicMinPrice);
		
		String sBasicMaxPrice = resultMenuItem.optString("item_basic_max_price");
		if(!sBasicMaxPrice.isEmpty())
			this.basicMaxPrice = new BigDecimal(sBasicMaxPrice);
		
		for(i=0; i<=10; i++) {
			String sChildPrice = resultMenuItem.optString("item_child_price"+i);
			if(!sChildPrice.isEmpty())
				this.childPrice[i] = new BigDecimal(sChildPrice);
		}
		
		String sChildMinPrice = resultMenuItem.optString("item_child_min_price");
		if(!sChildMinPrice.isEmpty())
			this.childMinPrice = new BigDecimal(sChildMinPrice);
		
		String sChildMaxPrice = resultMenuItem.optString("item_child_max_price");
		if(!sChildMaxPrice.isEmpty())
			this.childMaxPrice = new BigDecimal(sChildMaxPrice);
		
		this.childGetRevenue = resultMenuItem.optString("item_child_get_revenue", MenuItem.CHILD_GET_REVENUE_NO);
		
		for(i=0; i<=10; i++) {
			String sModifierPrice = resultMenuItem.optString("item_modifier_price"+i);
			if(!sModifierPrice.isEmpty())
				this.modifierPrice[i] = new BigDecimal(sModifierPrice);
		}
		
		String sModifierMinPrice = resultMenuItem.optString("item_modifier_min_price");
		if(!sModifierMinPrice.isEmpty())
			this.modifierMinPrice = new BigDecimal(sModifierMinPrice);
		
		String sModifierMaxPrice = resultMenuItem.optString("item_modifier_max_price");
		if(!sModifierMaxPrice.isEmpty())
			this.modifierMaxPrice = new BigDecimal(sModifierMaxPrice);
		
		for(i=0; i<=10; i++) {
			String sModifierRate = resultMenuItem.optString("item_modifier_rate"+i);
			if(!sModifierRate.isEmpty())
				this.modifierRate[i] = new BigDecimal(sModifierRate);
		}
		
		String sModifierMinRate = resultMenuItem.optString("item_modifier_min_rate");
		if(!sModifierMinRate.isEmpty())
			this.modifierMinRate = new BigDecimal(sModifierMinRate);
		
		String sModifierMaxRate = resultMenuItem.optString("item_modifier_max_rate");
		if(!sModifierMaxRate.isEmpty())
			this.modifierMaxRate = new BigDecimal(sModifierMaxRate);
		
		this.modifierOperator = resultMenuItem.optString("item_modifier_operator", MenuItem.MODIFIER_OPERATOR_ADD_UNIT_MODIFIER_PRICE_TO_UNIT_PRICE);
		this.modifierGetRevenue = resultMenuItem.optString("item_modifier_get_revenue", MenuItem.MODIFIER_GET_REVENUE_NO);
		this.cost = new BigDecimal(resultMenuItem.optString("item_cost", "0.0"));
		this.stsgId = resultMenuItem.optInt("item_stsg_id");
		
		for(i=1; i<=5; i++)
			this.chargeSc[i-1] = resultMenuItem.optString("item_charge_sc"+i, MenuItem.CHARGE_SC_NO);
		
		for(i=1; i<=25; i++)
			this.chargeTax[i-1] = resultMenuItem.optString("item_charge_tax"+i, MenuItem.CHARGE_TAX_NO);
		
		this.spqgId = resultMenuItem.optInt("item_spqg_id");
		
		for(i=1; i<=10; i++)
			this.printQueue[i-1] = resultMenuItem.optInt("item_print_queue"+i+"_itpq_id");
		
		this.noPrintItem = resultMenuItem.optString("item_no_print_item", MenuItem.NO_PRINT_ITEM_NO);
		this.noPrintChild = resultMenuItem.optString("item_no_print_child", MenuItem.NO_PRINT_CHILD_NO);
		this.noPrintModifier = resultMenuItem.optString("item_no_print_modifier", MenuItem.NO_PRINT_MODIFIER_NO);
		this.smmgId = resultMenuItem.optInt("item_smmg_id");
		
		for(i=1; i<=10; i++) {
			this.selectModifierMenuId[i-1] = resultMenuItem.optInt("item_select_modifier"+i+"_menu_id");
		}
		
		this.selectModifierMethod = resultMenuItem.optString("item_select_modifier_method", MenuItem.SELECT_MODIFIER_METHOD_MANUALLY);
		this.icatId = resultMenuItem.optInt("item_icat_id");
		this.idepId = resultMenuItem.optInt("item_idep_id");
		this.icouId = resultMenuItem.optInt("item_icou_id");
		this.digpId = resultMenuItem.optInt("item_digp_id");
		this.oigpId = resultMenuItem.optInt("item_oigp_id");
		this.childCount = resultMenuItem.optInt("item_child_count");
		this.asBasic = resultMenuItem.optString("item_as_basic", MenuItem.AS_BASIC_NOT_ALLOW);
		this.asChild = resultMenuItem.optString("item_as_child", MenuItem.AS_CHILD_NOT_ALLOW);
		this.asModifier = resultMenuItem.optString("item_as_modifier", MenuItem.AS_MODIFIER_NOT_ALLOW);
		this.inputName = resultMenuItem.optString("item_input_name", MenuItem.INPUT_NAME_NORMAL);
		this.minOrderQty = new BigDecimal(resultMenuItem.optString("item_min_order_qty", "0"));
		this.maxOrderQty = new BigDecimal(resultMenuItem.optString("item_max_order_qty", "0"));
		this.hide = resultMenuItem.optString("item_hide", MenuItem.HIDE_NO);
		this.mode = resultMenuItem.optString("item_mode", MenuItem.MODE_NORMAL);
		this.status = resultMenuItem.optString("item_status", MenuItem.STATUS_ACTIVE);
		
		//get menu set lookup
		JSONArray oSetMenuJSONArray = menuItemJSONObject.optJSONArray("self_select_menus");
		if(oSetMenuJSONArray != null)
			this.getSetMenuContent(oSetMenuJSONArray);
		
		//get modifier menu lookup
		JSONArray oModifierMenuJSONArray = menuItemJSONObject.optJSONArray("modifier_menus");
		if(oModifierMenuJSONArray != null) 
			this.getModifierMenusContent(oModifierMenuJSONArray);
		
		//get menu media object
		JSONArray oMediaObjectJSONArray = menuItemJSONObject.optJSONArray("media_objects");
		if(oMediaObjectJSONArray != null)
			this.getMenuMediaObjectContent(oMediaObjectJSONArray);
	}
	
	//read data from POS API
	public JSONArray readMenuItemDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray displayPanelLookupJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("menu_item_list")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("menu_item_list"))
				return null;
		
			displayPanelLookupJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("menu_item_list");				
		}
		
		return displayPanelLookupJSONArray;
	}
	
	//get set menu lookup
	private boolean getSetMenuContent(JSONArray setMenuContentJSONArray) {
		boolean bResult = true;
		
		for (int i = 0; i < setMenuContentJSONArray.length(); i++) {
			try {
				JSONObject tempSetMenuLookupJOSNObject = setMenuContentJSONArray.getJSONObject(i);
				this.setMenuLookups.add(new MenuSetMenuLookup(tempSetMenuLookupJOSNObject)); 
				
			}catch (JSONException setMenuLookupExcept) {
				bResult = false;
				break;
			}
			
		}
		
		return bResult;
	}
	
	//get menu media object
	private boolean getMenuMediaObjectContent(JSONArray mediaObjectJSONArray) {
		boolean bResult = true;
		JSONObject tempMediaObjectJSONObject = null;
		
		for (int i = 0; i < mediaObjectJSONArray.length(); i++) {
			try {
				tempMediaObjectJSONObject = mediaObjectJSONArray.getJSONObject(i);
				this.menuMediaObjectsList.add(new MenuMediaObject(tempMediaObjectJSONObject));
				
			}catch (JSONException jsone) {
				bResult = false;
				jsone.printStackTrace();
				break;
			}
			
		}
		
		return bResult;
	}
	
	//get set menu lookup
	private boolean getModifierMenusContent(JSONArray modifierMenusContentJSONArray) {
		boolean bResult = true;
		JSONObject tempModifierMenuJSONObject = null;
	
		for (int i = 0; i < modifierMenusContentJSONArray.length(); i++) {
			if (modifierMenusContentJSONArray.isNull(i))
				continue;
			
			tempModifierMenuJSONObject = modifierMenusContentJSONArray.optJSONObject(i);
			this.modifierMenuList.add(new MenuMenu(tempModifierMenuJSONObject));
		}
		
		return bResult;
	}
	
	//reset the object
	public void init() {
		int i=0;
		
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
		if(this.desc1 == null)
			this.desc1 = new String[5];
		for(i=0; i<5; i++)
			this.desc1[i] = null;
		if(this.desc2 == null)
			this.desc2 = new String[5];
		for(i=0; i<5; i++)
			this.desc2[i] = null;
		if(this.info == null)
			this.info = new String[5];
		for(i=0; i<5; i++)
			this.info[i] = null;
		if(this.basicPrice == null)
			this.basicPrice = new BigDecimal[11];
		for(i=0; i<=10; i++) 
			this.basicPrice[i] = null;
		this.basicMinPrice = null;
		this.basicMaxPrice = null;
		if(this.childPrice == null)
			this.childPrice = new BigDecimal[11];
		for(i=0; i<=10; i++)
			this.childPrice[i] = null;
		this.childMinPrice = null;
		this.childMaxPrice = null;
		this.childGetRevenue = MenuItem.CHILD_GET_REVENUE_NO;
		if(this.modifierPrice == null)
			this.modifierPrice = new BigDecimal[11];
		for(i=0; i<=10; i++)
			this.modifierPrice[i] = null;
		this.modifierMinPrice = null;
		this.modifierMaxPrice = null;
		if(this.modifierRate == null)
			this.modifierRate = new BigDecimal[11];
		for(i=0; i<=10; i++)
			this.modifierRate[i] = null;
		this.modifierMinRate = null;
		this.modifierMaxRate = null;
		this.modifierOperator = MenuItem.MODIFIER_OPERATOR_ADD_TO_UNIT_PRICE;
		this.modifierGetRevenue = MenuItem.MODIFIER_GET_REVENUE_NO;
		this.cost = BigDecimal.ZERO;
		this.stsgId = 0;
		if(this.chargeSc == null)
			this.chargeSc = new String[5];
		for(i=1; i<=5; i++)
			this.chargeSc[i-1] = MenuItem.CHARGE_SC_NO;
		if(this.chargeTax == null)
			this.chargeTax = new String[25];
		for(i=1; i<=25; i++)
			this.chargeTax[i-1] = MenuItem.CHARGE_TAX_NO;
		this.spqgId = 0;
		if(this.printQueue == null)
			this.printQueue = new int[10];
		for(i=1; i<=10; i++)
			this.printQueue[i-1] = 0;
		this.noPrintItem = MenuItem.NO_PRINT_ITEM_NO;
		this.noPrintChild = MenuItem.NO_PRINT_CHILD_NO;
		this.noPrintModifier = MenuItem.NO_PRINT_MODIFIER_NO;
		this.smmgId = 0;
		if(this.selectModifierMenuId == null)
			this.selectModifierMenuId = new int[10];
		for(i=1; i<=10; i++)
			this.selectModifierMenuId[i-1] = 0;
		this.selectModifierMethod = MenuItem.SELECT_MODIFIER_METHOD_MANUALLY;
		this.icatId = 0;
		this.idepId = 0;
		this.icouId = 0;
		this.digpId = 0;
		this.oigpId = 0;
		this.childCount = 0;
		this.asBasic = MenuItem.AS_BASIC_NOT_ALLOW;
		this.asChild = MenuItem.AS_CHILD_NOT_ALLOW;
		this.asModifier = MenuItem.AS_MODIFIER_NOT_ALLOW;
		this.inputName = MenuItem.INPUT_NAME_NORMAL;
		this.minOrderQty = BigDecimal.ZERO;
		this.maxOrderQty = BigDecimal.ZERO;
		this.hide = MenuItem.HIDE_NO;
		this.mode = MenuItem.MODE_NORMAL;
		this.status = MenuItem.STATUS_ACTIVE;
		
		if(this.setMenuLookups == null)
			this.setMenuLookups = new ArrayList<MenuSetMenuLookup>();
		else
			this.setMenuLookups.clear();
		
		if(this.modifierMenuList == null)
			this.modifierMenuList = new ArrayList<MenuMenu>();
		else
			this.modifierMenuList.clear();
		
		if(this.menuMediaObjectsList == null)
			this.menuMediaObjectsList = new ArrayList<MenuMediaObject>();
		else
			this.menuMediaObjectsList.clear();
		
	}
	
	//read data from database by item_id
	public boolean readById(int iItemId) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("id", Integer.toString(iItemId));
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "menu", "getItemFullInformationById", requestJSONObject.toString());
		
	}
	
	// find item by item code (case sensitive)
	public boolean readByItemCode(String sItemCode) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("item_code", sItemCode);
			requestJSONObject.put("recursive", 1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "menu", "getItemFullInformationByItemCode", requestJSONObject.toString());
	}

	// find item by item code (case insensitive)
	public JSONArray readItemListByItemCode(String sItemCode) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("item_code", sItemCode);
			requestJSONObject.put("recursive", 1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readMenuItemDataListFromApi("gm", "menu", "getItemListFullInformationByItemCode", requestJSONObject.toString());
	}
	
	// find item by item SKU (case insensitive)
		public JSONArray readItemListByItemSKU(String sItemSKU) {
			JSONObject requestJSONObject = new JSONObject();
			
			try {
				requestJSONObject.put("item_sku", sItemSKU);
				requestJSONObject.put("recursive", 1);
			}catch (JSONException jsone) {
				jsone.printStackTrace();
			}
			
			return this.readMenuItemDataListFromApi("gm", "menu", "getItemListFullInformationByItemSKU", requestJSONObject.toString());
		}

	public JSONArray readByPLUNameDepartmentCategory(String sSearchText, int sDepartment, int sCategory) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("search_text", sSearchText);
			requestJSONObject.put("department", sDepartment);
			requestJSONObject.put("category", sCategory);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readMenuItemDataListFromApi("gm", "menu", "getMenuItemListByPLUNameDeptCategory", requestJSONObject.toString());
	}

	//get item_id
	public int getItemId() {
		return this.itemId;
	}
	
	public String getCode() {
		return this.code;
	}
	//get name
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	public String getBilingualName(int iIndex, int iBilingualLangIndex) {
		String sName;
		if(this.getShortName(iIndex).isEmpty())
			sName = this.getName(iIndex);
		else
			sName = this.getShortName(iIndex);
		
		if (iBilingualLangIndex > 0 && iIndex != iBilingualLangIndex) {
			if(this.getShortName(iBilingualLangIndex).isEmpty())
				sName += "\n" + this.getName(iBilingualLangIndex);
			else
				sName += "\n" + this.getShortName(iBilingualLangIndex);
		}
		
		return sName;
	}
	
	//get short name
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get description 1
	public String getDescription1(int iIndex) {
		return this.desc1[(iIndex-1)];
	}
	
	//get description 2
	public String getDescription2(int iIndex) {
		return this.desc2[(iIndex-1)];
	}
	
	//get info
	public String getInfo(int iIndex) {
		return this.info[(iIndex-1)];
	}
	
	public BigDecimal getBasicPrice(int iIndex) {
		if(iIndex > 0){
			if(this.basicPrice[iIndex] == null)
				// If index > 0 and the value = null, follow basic price 0 setting
				return this.basicPrice[0];
		}
		
		return this.basicPrice[iIndex];
	}
	//get mode
	public String getMode() {
		return this.mode;
	}
	
	//get price with price level
	public BigDecimal getPriceByPriceLevel(int iPriceLevel) {
		BigDecimal price = BigDecimal.ZERO;
		
		if(this.basicPrice[iPriceLevel] != null)
			price = this.basicPrice[iPriceLevel];
		else if(iPriceLevel != 0 && this.basicPrice[0] != null)
			price = this.basicPrice[0];
		
		return price;
	}
	
	//get basic min price
	public BigDecimal getBasicMinPrice() {
		return this.basicMinPrice;
	}
	
	//get basic max price
	public BigDecimal getBasicMaxPrice() {
		return this.basicMaxPrice;
	}
	
	public BigDecimal getChildPrice(int iIndex) {
		return this.childPrice[iIndex];
	}
	//get child min price
	public BigDecimal getChildMinPrice() {
		return this.childMinPrice;
	}
	
	//get child max price
	public BigDecimal getChildMaxPrice() {
		return this.childMaxPrice;
	}
	
	public BigDecimal getModifierPrice(int iIndex) {
		return this.modifierPrice[iIndex];
	}
	
	public BigDecimal getModifierMinPrice() {
		return this.modifierMinPrice;
	}
	
	public BigDecimal getModifierMaxPrice() {
		return this.modifierMaxPrice;
	}
	
	public BigDecimal getModifierRate(int iIndex) {
		return this.modifierRate[iIndex];
	}
	
	public BigDecimal getModifierMinRate() {
		return this.modifierMinRate;
	}
	
	public BigDecimal getModifierMaxRate() {
		return this.modifierMaxRate;
	}
	
	public String getModifierOperator() {
		return this.modifierOperator;
	}
	
	//get charge SC
	public String getChargeSc(int iIndex) {
		return this.chargeSc[iIndex];
	}
	
	//get charge tax
	public String getChargeTax(int iIndex) {
		return this.chargeTax[iIndex];
	}
	
	public int getSelectPrintQueueId() {
		return this.spqgId;
	}

	public int getPrintQueue(int iIndex) {
		return this.printQueue[(iIndex-1)];
	}
	
	public String getNoPrintItem() {
		return this.noPrintItem;
	}
	
	public String getNoPrintChild() {
		return this.noPrintChild;
	}
	
	public String getNoPrintModifier() {
		return this.noPrintModifier;
	}

	public int getSelectModifierMenuGroupId() {
		return this.smmgId;
	}
	
	public int getSelectModifierMenuId(int iIndex) {
		return this.selectModifierMenuId[iIndex];
	}
	
	public String getSelectModifierMethod() {
		return this.selectModifierMethod;
	}
	
	public int getCategoryId() {
		return this.icatId;
	}
	
	public int getDeparmentId() {
		return this.idepId;
	}
	
	public int getCourseId() {
		return this.icouId;
	}
	
	public int getDiscountItemGroupId() {
		return this.digpId;
	}
	
	public int getOrderItemGroupId() {
		return this.oigpId;
	}
	
	public int getChildCount() {
		return this.childCount;
	}
	
	
	public BigDecimal getCost() {
		return this.cost;
	}
	
	public boolean isBasic() {
		return this.asBasic.equals(MenuItem.AS_BASIC_ALLOW);
	}
	
	public boolean isChild() {
		return this.asChild.equals(MenuItem.AS_CHILD_ALLOW);
	}
	
	public boolean isModifier() {
		return this.asModifier.equals(MenuItem.AS_MODIFIER_ALLOW);
	}
	
	public String getInputName() {
		return this.inputName;
	}
	
	public BigDecimal getMinOrderQty() {
		return this.minOrderQty;
	}
	
	public BigDecimal getMaxOrderQty() {
		return this.maxOrderQty;
	}
	
	public String getChildGetRevenue() {
		return this.childGetRevenue;
	}
	
	public String getModifierGetRevenue() {
		return this.modifierGetRevenue;
	}
	
	public String getHide() {
		return this.hide;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public List<MenuSetMenuLookup> getSetMenuLookup() {
		return this.setMenuLookups;
	}
	
	//get modifier menu list
	public List<MenuMenu> getModifierMenuList() {
		return this.modifierMenuList;
	}
	
	//get item media url
	public String getMediaUrl(String sType) {
		String mediaUrl = null;
		
		for(MenuMediaObject oMediaObject:this.menuMediaObjectsList) {
			if(!oMediaObject.getUsedFor().equals(sType))
				continue;
			
			if(oMediaObject.getMedia().getUrl().isEmpty())
				continue;
			else {
				mediaUrl = oMediaObject.getMedia().getUrl();
				break;
			}
		}
		
		return mediaUrl;
	}
	
	//set media object list
	public void setMediaObjectList(JSONArray oMediaObjectJSONArray) {
		this.getMenuMediaObjectContent(oMediaObjectJSONArray);
	}
	
	//set set menu lookups
	public void setSetMenuLookups(List<MenuSetMenuLookup> oMenuSetMenuLookups) {
		this.setMenuLookups = oMenuSetMenuLookups;
	}
	
	public boolean isActive() {
		return this.status.equals(MenuItem.STATUS_ACTIVE);
	}
	
	public boolean isDeleted() {
		return this.status.equals(MenuItem.STATUS_DELETED);
	}
	
	public boolean isMultipleRateModifierOperator() {
		return this.modifierOperator.equals(MenuItem.MODIFIER_OPERATOR_MULTIPLE_RATE);
	}
	
	public boolean isAddUnitModifierPriceToTotalModifierOperator() {
		return this.modifierOperator.equals(MenuItem.MODIFIER_OPERATOR_ADD_UNIT_MODIFIER_PRICE_TO_TOTAL);
	}
	
	public boolean isAddUnitModifierPriceToUnitPriceModifierOperator() {
		return this.modifierOperator.equals(MenuItem.MODIFIER_OPERATOR_ADD_UNIT_MODIFIER_PRICE_TO_UNIT_PRICE);
	}
	
	public boolean isInactiveMode() {
		return this.mode.equals(MenuItem.MODE_INACTIVE);
	}
	
	public boolean isDisableMode() {
		return this.mode.equals(MenuItem.MODE_DISABLE);
	}
	
	public boolean isOpenDescriptionInputName() {
		return this.inputName.equals(MenuItem.INPUT_NAME_OPEN_DESCRIPTION);
	}
	
	public boolean isAppendDescriptionInputName() {
		return this.inputName.equals(MenuItem.INPUT_NAME_APPEND_DESCRIPTION);
	}
	
	public boolean isForceModifierSelectModifierMethod() {
		return this.selectModifierMethod.equals(MenuItem.SELECT_MODIFIER_METHOD_FORCE_MODIFIER);
	}
}

