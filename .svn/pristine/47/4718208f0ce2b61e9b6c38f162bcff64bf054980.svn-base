package om;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuSetMenuLookup {
	private int smluId;
	private int itemId;
	private String type;
	private int seq;
	private int childItemId;
	private BigDecimal childItemBaseQty;
	private int selectMenuId;
	private String changePriceLevel;
	private int priceLevel;
	
	private MenuItem childMenuItem;
	private MenuMenu childSelfMenuMenu;
	
	// type
	public static String TYPE_CHILD_ITEM = "";
	public static String TYPE_SELF_SELECT_MENU = "s";
	
	// changePriceLevel
	public static String CHANGE_PRICE_LEVEL_NO = "";
	public static String CHANGE_PRICE_LEVEL_YES = "y";
	
	//init object with initialize value
	public MenuSetMenuLookup() {
		this.init();

	}
	
	//init object with JSONObject
	public MenuSetMenuLookup(JSONObject setMenuLookupJSONObject) {
		this.readDataFromJson(setMenuLookupJSONObject);
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("set_menu")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("set_menu")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("set_menu");
			if(tempJSONObject.isNull("MenuSetMenuLookup")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}
	
	//read data list from POS API
	private JSONObject readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("set_menu")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("set_menu").isNull("self_select_menus"))
				return null;
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("set_menu");
		}

		return tempJSONObject;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject setMenuLookupJSONObject) {
		JSONObject resultSetMenuLookup = null;
		
		resultSetMenuLookup = setMenuLookupJSONObject.optJSONObject("MenuSetMenuLookup");
		if(resultSetMenuLookup == null)
			resultSetMenuLookup = setMenuLookupJSONObject;
		
		this.init();
		this.smluId = resultSetMenuLookup.optInt("smlu_id");
		this.itemId = resultSetMenuLookup.optInt("smlu_item_id");
		this.type = resultSetMenuLookup.optString("smlu_type");
		this.seq = resultSetMenuLookup.optInt("smlu_seq");
		
		this.childItemId = resultSetMenuLookup.optInt("smlu_child_item_id");
		this.childItemBaseQty = new BigDecimal(resultSetMenuLookup.optString("smlu_child_item_base_qty", "0.0"));
		this.selectMenuId = resultSetMenuLookup.optInt("smlu_select_menu_id");
		this.changePriceLevel = resultSetMenuLookup.optString("smlu_change_price_level");
		this.priceLevel = resultSetMenuLookup.optInt("smlu_price_level");
		
		//get the set menu lookup corresponding menu item or self menu menu
		if(this.isChildItem()) {
			resultSetMenuLookup = setMenuLookupJSONObject.optJSONObject("MenuChildItem");
			if(resultSetMenuLookup != null) {
				this.childMenuItem = new MenuItem(resultSetMenuLookup);
				
				JSONArray oMediaObject = setMenuLookupJSONObject.optJSONArray("media_objects");
				if(oMediaObject != null)
					this.childMenuItem.setMediaObjectList(oMediaObject);
			}
			
		}else if(this.isSelfSelectMenu()) {
			resultSetMenuLookup = setMenuLookupJSONObject.optJSONObject("MenuSelectMenu");
			if(resultSetMenuLookup != null) {
				JSONObject oMenuMenuLookup = setMenuLookupJSONObject.optJSONObject("MenuMenuLookup");
				if(oMenuMenuLookup != null) {
					try {
						resultSetMenuLookup.put("MenuMenuLookup", oMenuMenuLookup);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
				this.childSelfMenuMenu = new MenuMenu(resultSetMenuLookup);
				JSONArray oMediaObject = setMenuLookupJSONObject.optJSONArray("media_objects");
				if(oMediaObject != null)
					this.childSelfMenuMenu.setMediaObjectList(oMediaObject);
			}
		}
	}
	
	//reset object value
	public void init() {
		this.smluId = 0;
		this.itemId = 0;
		this.type = MenuSetMenuLookup.TYPE_CHILD_ITEM;
		this.seq = 0;
		this.childItemId = 0;
		this.childItemBaseQty = BigDecimal.ZERO;
		this.selectMenuId = 0;
		this.changePriceLevel = MenuSetMenuLookup.CHANGE_PRICE_LEVEL_NO;
		this.priceLevel = 0;
		
		this.childMenuItem = null;
		this.childSelfMenuMenu = null;
	}

	//get menu set lookup by item ID
	public JSONObject readByItemId(int iItemId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("ids", iItemId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataListFromApi("gm", "menu", "getSetMenuContentByItemIds", requestJSONObject.toString());
		
	}
	
	//get set menu lookup id
	public int getSmluID() {
		return this.smluId;
	}

	//get item id
	protected int getItemId() {
		return this.itemId;
	}
	
	//get type
	public String getType() {
		return this.type;
	}
	
	//get seq
	public int getSeq() {
		return this.seq;
	}
	
	//get child item id
	public int getChildItemId() {
		return this.childItemId;
	}
	
	//get child item base quantity
	public BigDecimal getChildItemBaseQty() {
		return this.childItemBaseQty;
	}
	
	//get select menu id
	public int getSelectMenuId() {
		return this.selectMenuId;
	}
	
	//get change price level
	public String getChangePriceLevel() {
		return this.changePriceLevel;
	}
	
	//get price level
	public int getPriceLevel() {
		return this.priceLevel;
	}
	
	public MenuItem getChildItem() {
		return this.childMenuItem;
	}
	
	public MenuMenu getSelfSelectMenu() {
		return this.childSelfMenuMenu;
	}
	
	public boolean isChildItem() {
		return this.type.equals(MenuSetMenuLookup.TYPE_CHILD_ITEM);
	}
	
	public boolean isSelfSelectMenu() {
		return this.type.equals(MenuSetMenuLookup.TYPE_SELF_SELECT_MENU);
	}
	
	public boolean isChangePriceLevel() {
		return this.changePriceLevel.equals(MenuSetMenuLookup.CHANGE_PRICE_LEVEL_YES);
	}
}
