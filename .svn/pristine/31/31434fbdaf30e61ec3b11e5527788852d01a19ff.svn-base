package om;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuMenu {
	private int menuId;
	private String code;
	private String[] name;
	private String[] shortName;
	private String[] desc1;
	private String[] desc2;
	private String[] info;
	private String role;
	private int childMinOrder;
	private int childMaxOrder;
	private String childOrderRule;
	private int modifierMinOrder;
	private int modifierMaxOrder;
	private String modifierOrderRule;
	private String status;
	
	private List<MenuMediaObject> menuMediaObjectList;
	private List<MenuMenuLookup> menuLookupList;
	
	// role
	public static String ROLE_GENERAL = "";
	public static String ROLE_MASTER_MENU = "k";
	public static String ROLE_SELF_SELECTION_SET_MENU = "s";
	public static String ROLE_MODIFIER_MENU = "m";
	
	// childOrderRule
	public static String CHILD_ORDER_RULE_NO_RESTRICTION = "";
	public static String CHILD_ORDER_RULE_NOT_REPEAT_ORDER_SAME_ITEM = "r";
	
	// modifierOrderRule
	public static String MODIFIER_ORDER_RULE_NO_RESTRICTION = "";
	public static String MODIFIER_ORDER_RULE_NOT_REPEAT_ORDER_SAME_ITEM = "r";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_SUSPENDED = "s";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public MenuMenu() {
		this.init();
	}
	
	//init object form JSON Object
	public MenuMenu(JSONObject menuJSONObject) {
		readDataFromJson(menuJSONObject);
	}
	
	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bResult = true;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bResult = false;	
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("menu")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("menu")) {
				this.init();
				return false;
			}
			
			JSONObject tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("menu");
			if(tempJSONObject.isNull("MenuMenu")) {
				this.init();
				return false;
			}
			
			this.readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject menuMenuJSONObject) {
		JSONObject resultMenuMenu = null;
		int i;
		
		resultMenuMenu = menuMenuJSONObject.optJSONObject("MenuMenu");
		if(resultMenuMenu == null)
			resultMenuMenu = menuMenuJSONObject;
						
		this.init();
		this.menuId = resultMenuMenu.optInt("menu_id");
		this.code = resultMenuMenu.optString("menu_code");
		
		for(i=1; i<=5; i++) {
			this.name[(i-1)] = resultMenuMenu.optString("menu_name_l"+i);
		}
		
		for(i=1; i<=5; i++) {
			this.shortName[(i-1)] = resultMenuMenu.optString("menu_short_name_l"+i);
		}
		
		for(i=1; i<=5; i++) {
			this.desc1[(i-1)] = resultMenuMenu.optString("menu_desc1_l"+i, null);
		}
		
		for(i=1; i<=5; i++) {
			this.desc2[(i-1)] = resultMenuMenu.optString("menu_desc2_l"+i, null);
		}
		
		for(i=1; i<=5; i++) {
			this.info[(i-1)] = resultMenuMenu.optString("menu_info_l"+i, null);
		}
		
		this.role = resultMenuMenu.optString("menu_role");
		this.childMinOrder = resultMenuMenu.optInt("menu_child_min_order");
		this.childMaxOrder = resultMenuMenu.optInt("menu_child_max_order");
		this.childOrderRule = resultMenuMenu.optString("menu_child_order_rule");
		this.modifierMinOrder = resultMenuMenu.optInt("menu_modifier_min_order");
		this.modifierMaxOrder = resultMenuMenu.optInt("menu_modifier_max_order");
		this.modifierOrderRule = resultMenuMenu.optString("menu_modifier_order_rule");
		this.status = resultMenuMenu.optString("menu_status", MenuMenu.STATUS_ACTIVE);

		//get menu media object
		JSONArray oMediaObjectJSONArray = resultMenuMenu.optJSONArray("media_objects");
		if(oMediaObjectJSONArray != null) {
			for(i=0; i<oMediaObjectJSONArray.length(); i++) {
				JSONObject temp2JSONObject = oMediaObjectJSONArray.optJSONObject(i);
				if(temp2JSONObject != null) {
					MenuMediaObject oMediaObject = new MenuMediaObject(temp2JSONObject);
					this.menuMediaObjectList.add(oMediaObject);
				}
			}
		}
					
		//get menu lookup content
		JSONObject oMenuMenuLookupJSONObject = resultMenuMenu.optJSONObject("MenuMenuLookup");
		if(oMenuMenuLookupJSONObject != null) {
			//handle item content
			JSONArray tempJSONArray = oMenuMenuLookupJSONObject.optJSONArray("items");
			if(tempJSONArray != null) {
				for(i=0; i<tempJSONArray.length(); i++) {
					JSONObject temp2JSONObject = tempJSONArray.optJSONObject(i);
					if(temp2JSONObject != null) {
						MenuMenuLookup oMenuLookup = new MenuMenuLookup(temp2JSONObject);
						this.menuLookupList.add(oMenuLookup);
					}
				}
			}
			
			//handle menu content
			tempJSONArray = oMenuMenuLookupJSONObject.optJSONArray("sub_menus");
			if(tempJSONArray != null) {
				for(i=0; i<tempJSONArray.length(); i++) {
					JSONObject temp2JSONObject = tempJSONArray.optJSONObject(i);
					if(temp2JSONObject != null) {
						MenuMenuLookup oMenuLookup = new MenuMenuLookup(temp2JSONObject);
						this.menuLookupList.add(oMenuLookup);
					}
				}
			}
		}
	}
	
	//get menu media object
	private boolean getMenuMediaObjectContent(JSONArray mediaObjectJSONArray) {
		boolean bResult = true;
		
		for (int i = 0; i < mediaObjectJSONArray.length(); i++) {
			JSONObject tempMediaObjectJSONObject = mediaObjectJSONArray.optJSONObject(i);
			if (tempMediaObjectJSONObject == null) {
				bResult = false;
				break;
			}
			this.menuMediaObjectList.add(new MenuMediaObject(tempMediaObjectJSONObject));
		}
		
		return bResult;
	}
	
	//read menu by menu id
	public boolean readById(int iMenuId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("id", iMenuId);
			requestJSONObject.put("recursive", 1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "menu", "getMenuById", requestJSONObject.toString());
	}
	
	//read menu by menu id
	public boolean readByCode(String sCode) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("code", sCode);
			requestJSONObject.put("recursive", 1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "menu", "getMenuByCode", requestJSONObject.toString());
	}


	//init the object
	public void init() {
		int i=0;
		
		this.menuId = 0;
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
		this.role = MenuMenu.ROLE_GENERAL;
		this.childMinOrder = 0;
		this.childMaxOrder = 0;
		this.childOrderRule = MenuMenu.CHILD_ORDER_RULE_NO_RESTRICTION;
		this.modifierMinOrder = 0;
		this.modifierMaxOrder = 0;
		this.modifierOrderRule = MenuMenu.MODIFIER_ORDER_RULE_NO_RESTRICTION;
		this.status = MenuMenu.STATUS_ACTIVE;
		
		if(this.menuLookupList == null)
			this.menuLookupList = new ArrayList<MenuMenuLookup>();
		else
			this.menuLookupList.clear();
		
		if(this.menuMediaObjectList == null)
			this.menuMediaObjectList = new ArrayList<MenuMediaObject>();
		else
			this.menuMediaObjectList.clear();
	}
	
	// set menu lookup list
	public void setMenuLookupList(List<MenuMenuLookup> oMenuLookupList) {
		this.menuLookupList = oMenuLookupList;
	}
	
	//set media object list
	public void setMediaObjectList(JSONArray oMediaObjectJSONArray) {
		this.getMenuMediaObjectContent(oMediaObjectJSONArray);
	}
	
	//get menu_id
	public int getMenuId() {
		return this.menuId;
	}
	
	//get menu code
	public String getMenuCode() {
		return this.code;
	}
	
	//get name by lang index
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
			if(this.getShortName(iBilingualLangIndex).isEmpty()){
				if(!sName.equals(this.getName(iBilingualLangIndex)))
					sName += "\n" + this.getName(iBilingualLangIndex);
			}	
			else{
				if(!sName.equals(this.getShortName(iBilingualLangIndex)))
					sName += "\n" + this.getShortName(iBilingualLangIndex);
			}
		}
		
		return sName;
	}
	
	//get short name by lang index
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get menu description 1 with lang index
	protected String getDesc1(int iIndex) {
		return this.desc1[(iIndex-1)];
	}
	
	//get menu description 2 with lang index
	protected String getDesc2(int iIndex) {
		return this.desc2[(iIndex-1)];
	}
	
	//get menu information with lang index
	protected String getInfo(int iIndex) {
		return this.info[(iIndex-1)];
	}
	
	//get role
	public String getRole() {
		return this.role;
	}
	
	//get child min order
	public int getChildMinOrder() {
		return this.childMinOrder;
	}
	
	//get child max order
	public int getChildMaxOrder() {
		return this.childMaxOrder;
	}
	
	//get child order role
	protected String getChildOrderRole() {
		return this.childOrderRule;
	}
	
	//get modifier min order
	public int getModifierMinOrder() {
		return this.modifierMinOrder;
	}
	
	//get modifier max order
	public int getModifierMaxOrder() {
		return this.modifierMaxOrder;
	}
	
	//get modifier order role
	public String getModifierOrderRole() {
		return this.modifierOrderRule;
	}
	
	//get status
	protected String getStatus() {
		return this.status;
	}
	
	//get menu media url
	public String getMediaUrl(String sType) {
		String mediaUrl = null;
		
		for(MenuMediaObject oMediaObject:this.menuMediaObjectList) {
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
	
	//get menu lookup list
	public List<MenuMenuLookup> getMenuLookupList() {
		return this.menuLookupList;
	}
	
	public boolean isModifierMenu() {
		return role.equals(MenuMenu.ROLE_MODIFIER_MENU);
	}
	
	public boolean isActive() {
		return this.status.equals(MenuMenu.STATUS_ACTIVE);
	}
}

