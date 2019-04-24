//Database: pos_display_panels - Screen display panels
package om;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosDisplayPanel {
	private int dpanId;
	private int sdevId;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String status;
	
	private int outletIdForOrderItemControl = 0;		// For order item control

	private List<PosDisplayPanelPage> displayPanelPages;
	private List<PosDisplayPanelZone> displayPanelPageZones;
	private HashMap<Integer, List<PosDisplayPanelLookup>> displayPanelLookups;
	private HashMap<Integer, MenuItem> displayPanelMenuItems;
	private HashMap<Integer, MenuMenu> displayPanelMenuMenus;

	private JSONArray m_oReturnDisplayStyleJSONArray;

	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	public static String STATUS_SUSPENDED = "s";

	//init object with initialize value
	public PosDisplayPanel () {
		this.init();
	}
	
	//init obejct with JSONObject
	public PosDisplayPanel(JSONObject displayPanelJSONObject) {
		readDataFromJson(displayPanelJSONObject);
	}
	
	//read data from database by dpan_id
	public void readById (int iDpanId, int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();

		this.init();
		
		this.outletIdForOrderItemControl = iOutletId;
		
		try {
			requestJSONObject.put("displayPanelId", Integer.toString(iDpanId));
		}
		catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		this.readDataFromApi("gm", "pos", "getDisplayPanelById", requestJSONObject.toString());
	}

	//construct the save request JSON
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		int i=0;
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("dpan_id", Integer.toString(this.dpanId));
			addSaveJSONObject.put("dpan_sdev_id", this.sdevId);
			for(i=1; i<=5; i++) {
				if(!this.name[i-1].isEmpty())
					addSaveJSONObject.put("dpan_name_l"+i, this.name[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(!this.shortName[i-1].isEmpty())
					addSaveJSONObject.put("dpan_short_name_l"+i, this.shortName[(i-1)]);
			}
			addSaveJSONObject.put("dpan_seq", this.seq);
			addSaveJSONObject.put("dpan_status", this.status);

		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("displayPanel")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("displayPanel")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("displayPanel");
			if(tempJSONObject.isNull("PosDisplayPanel")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}

		return bResult;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject displayPanelJSONObject) {
		JSONObject resultDisplayPanel = null;
		int i;
		resultDisplayPanel = displayPanelJSONObject.optJSONObject("PosDisplayPanel");
		if(resultDisplayPanel == null)
			resultDisplayPanel = displayPanelJSONObject;
		
		this.init();
		this.dpanId = resultDisplayPanel.optInt("dpan_id");
		this.sdevId = resultDisplayPanel.optInt("dpan_sdev_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultDisplayPanel.optString("dpan_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultDisplayPanel.optString("dpan_short_name_l"+i);
		this.seq = resultDisplayPanel.optInt("dpan_seq");
		this.status = resultDisplayPanel.optString("dpan_status", PosDisplayPanel.STATUS_ACTIVE);

		HashMap<String, JSONObject> oMenuItemList = new HashMap<String, JSONObject>();
		HashMap<String, JSONObject> oMenuMenuList = new HashMap<String, JSONObject>();
		//get hot item/ hot modifier/ menu lookup/ modifier lookup id list
		JSONArray itemJSONArray = resultDisplayPanel.optJSONArray("menu_item_list");
		if(itemJSONArray != null) {
			JSONObject requestJSONObject = new JSONObject();
			try {
				requestJSONObject.put("displayPanelItemList", itemJSONArray);
				requestJSONObject.put("outletId", this.outletIdForOrderItemControl);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			MenuItem oMenuItem = new MenuItem();
			JSONArray menuItemJSONArray = oMenuItem.readMenuItemDataListFromApi("gm", "menu", "getMenuItemListByMenuItemIds", requestJSONObject.toString());

			if(menuItemJSONArray != null) {
				for(i=0; i<menuItemJSONArray.length(); i++) {
					JSONObject tempJSONObject = menuItemJSONArray.optJSONObject(i);
					if(tempJSONObject == null)
						continue;
					
					int iDisplayPanelId = tempJSONObject.optInt("dplu_id");
					JSONObject menuItemInfoJSONObject = tempJSONObject.optJSONObject("menu_item");
					
					if(menuItemInfoJSONObject != null) { // Hot Item / Hot Modifier
	
						MenuItem oTempMenuItem = null;
						oTempMenuItem = new MenuItem(menuItemInfoJSONObject);
						
						// Check if the item is active
						if(!oTempMenuItem.isActive())
							continue;
						
						
						oMenuItemList.put(Integer.toString(iDisplayPanelId), menuItemInfoJSONObject);
						
						//set item's media object if exist
						JSONArray mediaObjectJSONArray = menuItemInfoJSONObject.optJSONArray("media_objects");
						if(mediaObjectJSONArray != null)
							oTempMenuItem.setMediaObjectList(mediaObjectJSONArray);
						
						this.displayPanelMenuItems.put(oTempMenuItem.getItemId(), oTempMenuItem);
					} else { // Menu Lookup / Modifier Lookup
						JSONObject menuMenuInfoJSONObject = tempJSONObject.optJSONObject("menu_menu");
						if(menuMenuInfoJSONObject == null)
							continue;
						
						MenuMenu oTempMenuMenu = null;
						oTempMenuMenu = new MenuMenu(menuMenuInfoJSONObject);
						
						// Check if the menu is active
						if(!oTempMenuMenu.isActive())
							continue;
						
						oMenuMenuList.put(Integer.toString(iDisplayPanelId), menuMenuInfoJSONObject);
						
						this.displayPanelMenuMenus.put(oTempMenuMenu.getMenuId(), oTempMenuMenu);
					}
				}
			}
		}

		// Get the page
		JSONArray displayPanelPageJSONArray = resultDisplayPanel.optJSONArray("display_panel_pages");
		if(displayPanelPageJSONArray != null) {
			for(i=0; i<displayPanelPageJSONArray.length(); i++) {
				JSONObject oTempJSONObject = displayPanelPageJSONArray.optJSONObject(i);
				if(oTempJSONObject == null)
					continue;
				
				JSONObject displayPanelPageJSONObject = oTempJSONObject.optJSONObject("PosDisplayPanelPage");
				PosDisplayPanelPage oPosDisplayPanelPage = new PosDisplayPanelPage(displayPanelPageJSONObject);
				this.displayPanelPages.add(oPosDisplayPanelPage);
	
				JSONObject displayPanelZoneJSONObject = oTempJSONObject.optJSONObject("PosDisplayPanelZone");
				PosDisplayPanelZone oPosDisplayPanelZone = new PosDisplayPanelZone(displayPanelZoneJSONObject);
				this.displayPanelPageZones.add(oPosDisplayPanelZone);
				
				// Get the content of each page
				if(oTempJSONObject.isNull("display_panel_lookups"))
					continue;
				
				ArrayList<PosDisplayPanelLookup> oPosDisplayPanelLookupList = new ArrayList<PosDisplayPanelLookup>();
				JSONArray displayPanelLookupJSONArray = oTempJSONObject.optJSONArray("display_panel_lookups");
				for(int j=0; j<displayPanelLookupJSONArray.length(); j++) {
					JSONObject displayPanelLookupsJSONObject = displayPanelLookupJSONArray.optJSONObject(j);
					if(displayPanelLookupsJSONObject == null)
						continue;
					
					PosDisplayPanelLookup oDisplayPanelLookup = new PosDisplayPanelLookup(displayPanelLookupsJSONObject);
					JSONObject displayLookupJSONObject = displayPanelLookupsJSONObject.optJSONObject("PosDisplayPanelLookup");
					if(displayLookupJSONObject == null)
						continue;
					
					String sDisplayType = displayLookupJSONObject.optString("dplu_type");
					if (sDisplayType.equals(PosDisplayPanelLookup.TYPE_HOT_ITEM)
							|| sDisplayType.equals(PosDisplayPanelLookup.TYPE_HOT_MODIFIER)) {
						int diplayPanelLookupItemId = displayLookupJSONObject.optInt("dplu_id");
						if(oMenuItemList.containsKey(oMenuItemList.get(Integer.toString(diplayPanelLookupItemId))))
							oDisplayPanelLookup.setMenuItemByJSONObject(oMenuItemList.get(Integer.toString(diplayPanelLookupItemId)));
					} else if (sDisplayType.equals(PosDisplayPanelLookup.TYPE_MENU_LOOKUP)
								|| sDisplayType.equals(PosDisplayPanelLookup.TYPE_MODIFIER_LOOKUP)) {
						int diplayPanelLookupMenuId = displayLookupJSONObject.optInt("dplu_id");
						if(oMenuMenuList.containsKey(Integer.toString(diplayPanelLookupMenuId)))
							oDisplayPanelLookup.setMenuMenuByJSONObject(oMenuMenuList.get(Integer.toString(diplayPanelLookupMenuId)));
					}
					
					oPosDisplayPanelLookupList.add(oDisplayPanelLookup);
				}
				this.displayPanelLookups.put(oPosDisplayPanelPage.getPageId(), oPosDisplayPanelLookupList);
			}
		}
		m_oReturnDisplayStyleJSONArray  = resultDisplayPanel.optJSONArray("display_styles");
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.dpanId = 0;
		this.sdevId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.status = PosDisplayPanel.STATUS_ACTIVE;
		
		if(this.displayPanelLookups == null)
			this.displayPanelLookups = new HashMap<Integer, List<PosDisplayPanelLookup>>();
		else
			this.displayPanelLookups.clear();
		
		if(this.displayPanelPages == null)
			this.displayPanelPages = new ArrayList<PosDisplayPanelPage>();
		else
			this.displayPanelPages.clear();
		
		if(this.displayPanelPageZones == null)
			this.displayPanelPageZones = new ArrayList<PosDisplayPanelZone>();
		else
			this.displayPanelPageZones.clear();
		
		if(this.displayPanelMenuItems == null)
			this.displayPanelMenuItems = new HashMap<Integer, MenuItem>();
		else
			this.displayPanelMenuItems.clear();
		
		if(this.displayPanelMenuMenus == null)
			this.displayPanelMenuMenus = new HashMap<Integer, MenuMenu>();
		else
			this.displayPanelMenuMenus.clear();
	}

	//get display panel lookup list by panel id and zone
	public List<PosDisplayPanelLookup> filterDisplayPanelLookupByPageId(Integer iPageId) {
		List<PosDisplayPanelLookup> oPosDisplayPanelLookups = new ArrayList<PosDisplayPanelLookup>();
		
		if(this.displayPanelLookups.containsKey(iPageId) == false)
			return oPosDisplayPanelLookups;
		
		for(Iterator<PosDisplayPanelLookup> i = this.displayPanelLookups.get(iPageId).iterator(); i.hasNext(); ) {
			PosDisplayPanelLookup lookup = i.next();
			oPosDisplayPanelLookups.add(lookup);
		}
		
		return oPosDisplayPanelLookups;
	}

	//get display panel page by panel id and zone
	public List<PosDisplayPanelPage> filterDisplayPanelPageByZone(String sZone) {
		List<PosDisplayPanelPage> oPosDisplayPanelPages = new ArrayList<PosDisplayPanelPage>();
		
		int iIndex = 0;
		for(PosDisplayPanelZone oPosDisplayPanelZone:this.displayPanelPageZones){
			if(oPosDisplayPanelZone.getKey().equals(sZone)) {
				PosDisplayPanelPage oPanelPage = this.displayPanelPages.get(iIndex);
				oPosDisplayPanelPages.add(oPanelPage);
			}
			iIndex++;
		}
		
		return oPosDisplayPanelPages;
	}

	
	public void clearDisplayPanelMenusAndMenuItems(){
		this.displayPanelMenuItems.clear();
		this.displayPanelMenuMenus.clear();
	}
	
	//get display panel page by panel id and zone
	public int getPanelPageNumberByZone(String sZone) {
		int iPage = 0;
		for(PosDisplayPanelZone oPosDisplayPanelZone:this.displayPanelPageZones){
			if(oPosDisplayPanelZone.getKey().equals(sZone))
				iPage++;
		}
		
		return iPage;
	}
	
	//get display panel zone by panel id and zone
	public List<PosDisplayPanelZone> filterDisplayPanelZoneByZone(String sZone) {
		List<PosDisplayPanelZone> oPosDisplayPanelZones = new ArrayList<PosDisplayPanelZone>();
		
		for(PosDisplayPanelZone oPosDisplayPanelZone:this.displayPanelPageZones){
			if(oPosDisplayPanelZone.getKey().equals(sZone)) {
				oPosDisplayPanelZones.add(oPosDisplayPanelZone);
			}
		}
		
		return oPosDisplayPanelZones;
	}
	
	/*** Getter and Setter methods ***/
	//get dpanId
	public int getDpanId() {
		return this.dpanId;
	}

	//get name by lang index
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get short name by lang index
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	public int getSequence() {
		return this.seq;
	}
	
	public MenuItem getMenuItemFromDisplayPanelMenuItemList(int iItemId) {
		if(this.displayPanelMenuItems.containsKey(iItemId))
			return this.displayPanelMenuItems.get(iItemId);
		else
			return null;
	}
	
	public MenuMenu getMenuMenuFromDisplayPanelMenuMenuList(int iMenuId) {
		if(this.displayPanelMenuMenus.containsKey(iMenuId))
			return this.displayPanelMenuMenus.get(iMenuId);
		else
			return null;
	}

	public JSONArray getDisplayStyleJSONArray() {
		return m_oReturnDisplayStyleJSONArray;
	}

	//set name by lang index
	public void setName(int iIndex, String sName) {
		this.name[(iIndex-1)] = sName;
	}
	
	//set short name by lang index
	public void setShortName(int iIndex, String sShortName) {
		this.shortName[(iIndex-1)] = sShortName;
	}
 	
	public void setSequence(int iSequence) {
		this.seq = iSequence;
	}
	
	//set status
	public void setStatus(String sStatus){
		this.status = sStatus;
	}
	
}
