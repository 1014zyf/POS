package app.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuMenuLookup {
	private int muluId;
	private int menuId;
	private String type;
	private int seq;
	private int itemId;
	private int subMenuId;
	
	private MenuItem menuItem;
	private MenuMenu menuMenu;
	
	public static String TYPE_ITEM = "";
	public static String TYPE_SUB_MENU = "m";
	
	//init object with initialize value
	public MenuMenuLookup() {
		this.init();
	}
	
	//init object with JSONObject 
	public MenuMenuLookup(JSONObject menuLookupJSONObject) {
		this.readDataFromJson(menuLookupJSONObject);
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("menu_lookup")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "",	OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("menu_lookup")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("menu_lookup");
			if(tempJSONObject.isNull("MenuMenuLookup")) {
				this.init();
				return false;
			}
			
			this.readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}
	
	//read data from POS API
	private JSONObject readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("menu_lookup")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("menu_lookup")) {
				return null;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("menu_lookup");
		}
		
		return tempJSONObject;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject menuLookupJSONObject) {
		JSONObject tempJSONObject = null;
		
		tempJSONObject = menuLookupJSONObject.optJSONObject("MenuMenuLookup");
		if(tempJSONObject == null)
			tempJSONObject = menuLookupJSONObject;
			
		this.init();
		this.muluId = tempJSONObject.optInt("mulu_id");
		this.menuId = tempJSONObject.optInt("mulu_menu_id");
		this.type = tempJSONObject.optString("mulu_type");
		this.seq = tempJSONObject.optInt("mulu_seq");
		this.itemId = tempJSONObject.optInt("mulu_item_id");
		this.subMenuId = tempJSONObject.optInt("mulu_sub_menu_id");
		
		if(this.isMenuItem()) {
			tempJSONObject = menuLookupJSONObject.optJSONObject("MenuItem");
			if(tempJSONObject != null) {
				this.menuItem = new MenuItem(tempJSONObject);
				
				JSONArray oMediaJSONArray = menuLookupJSONObject.optJSONArray("media_objects");
				if(oMediaJSONArray != null)
					this.menuItem.setMediaObjectList(oMediaJSONArray);
			}
		}else if (this.isSubMenu()) {
			tempJSONObject = menuLookupJSONObject.optJSONObject("MenuSubMenu");
			if(tempJSONObject != null) {
				this.menuMenu = new MenuMenu(tempJSONObject);
				
				JSONArray oMediaJSONArray = menuLookupJSONObject.optJSONArray("media_objects");
				if(oMediaJSONArray != null)
					this.menuMenu.setMediaObjectList(oMediaJSONArray);
			}
		}
	}
	
	//init object
	public void init() {
		this.muluId = 0;
		this.menuId = 0;
		this.type = MenuMenuLookup.TYPE_ITEM;
		this.seq = 0;
		this.itemId = 0;
		this.subMenuId = 0;
		
		this.menuItem = null;
		this.menuMenu = null;
	}

	// Read whole menu lookup by ID
	public JSONObject readMenuMenuLookupListById(int Id){
		JSONObject requestJSONObject = new JSONObject();
		JSONObject responseJsonObject = null;
		
		try {
			requestJSONObject.put("id", Id);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJsonObject = this.readDataListFromApi("gm", "menu", "getMenuContentByMenuId", requestJSONObject.toString());
		return responseJsonObject;
	}
	
	//get mulu ID
	public int getMuluId() {
		return this.muluId;
	}
	
	//get menu ID
	public int getMenuId() {
		return this.menuId;
	}
	
	//get type
	public String getType() {
		return this.type;
	}
	
	//get sequence
	public int getSeq() {
		return this.seq;
	}
	
	//get item ID
	public int getItemId() {
		return this.itemId;
	}
	
	//get sub menu ID
	public int getSubMenuId() {
		return this.subMenuId;
	}

	//get menu item
	public MenuItem getMenuItem() {
		return this.menuItem;
	}
	
	//get menu menu 
	public MenuMenu getMenuMenu() {
		return this.menuMenu;
	}
	
	//set menu item object
	public void setMenuItem(MenuItem oItem) {
		this.menuItem = oItem;
	}

	public boolean isMenuItem() {
		return this.type.equals(MenuMenuLookup.TYPE_ITEM);
	}
	
	public boolean isSubMenu() {
		return this.type.equals(MenuMenuLookup.TYPE_SUB_MENU);
	}
}
