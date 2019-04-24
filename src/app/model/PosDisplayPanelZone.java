package app.model;

import org.json.JSONException;
import org.json.JSONObject;

public class PosDisplayPanelZone {
	private int dpzoId;
	private int dpanId;
	private String key;
	private int seq;
	private int baseWidth;
	private int baseHeight;
	private int gridWidth;
	private int gridHeight;
	private String snapToGrid;
	private int lookupPadding;
	private String status;
	
	// key
	public static String KEY_ORDERING = "ordering";
	public static String KEY_ORDERING_FUNCTION = "ordering_function";
	public static String KEY_CASHIER = "cashier";
	public static String KEY_ITEM_SELECTED = "item_selected";
	public static String KEY_FLOOR_PLAN_FUNCITON = "floor_plan_function";
	// snapToGrid
	public static String SNAP_TO_GRID_NO = "";
	public static String SNAP_TO_GRID_YES = "y";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";

	//init object with initialize value
	public PosDisplayPanelZone () {
		this.init();
	}
	
	//init obejct with JSONObject
	public PosDisplayPanelZone(JSONObject displayPanelZoneJSONObject) {
		this.readDataFromJson(displayPanelZoneJSONObject);
	}
	
	//construct the save request JSON
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("dpzo_id", Integer.toString(this.dpzoId));
			if(this.dpanId > 0)
				addSaveJSONObject.put("dpzo_dpan_id", this.dpanId);
			if(!this.key.isEmpty())
					addSaveJSONObject.put("dpzo_key", this.key);
			if(this.seq > 0)
				addSaveJSONObject.put("dpzo_seq", this.seq);
			if(this.baseWidth > 0)
				addSaveJSONObject.put("dpzo_base_width", this.baseWidth);
			if(this.baseHeight > 0)
				addSaveJSONObject.put("dpzo_base_height", this.baseHeight);
			if(this.gridWidth > 0)
				addSaveJSONObject.put("dpzo_grid_width", this.gridWidth);
			if(this.gridHeight > 0)
				addSaveJSONObject.put("dpzo_grid_height", this.gridHeight);
			if(!this.snapToGrid.isEmpty())
				addSaveJSONObject.put("dpzo_snap_to_grid", this.snapToGrid);
			if(this.lookupPadding > 0)
				addSaveJSONObject.put("dpzo_lookup_padding", this.lookupPadding);
			if(!this.status.equals(PosDisplayPanelZone.STATUS_ACTIVE))
				addSaveJSONObject.put("dpzo_status", this.status);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	public void readDataFromJson(JSONObject displayPanelZoneJSONObject) {
		JSONObject resultDisplayPanelZone = null;

		resultDisplayPanelZone = displayPanelZoneJSONObject.optJSONObject("PosDisplayPanelZone");
		if(resultDisplayPanelZone == null)
			resultDisplayPanelZone = displayPanelZoneJSONObject;
		
		this.init();
		
		this.dpzoId = resultDisplayPanelZone.optInt("dpzo_id");
		this.dpanId = resultDisplayPanelZone.optInt("dpzo_dpan_id");
		this.key = resultDisplayPanelZone.optString("dpzo_key");
		this.seq = resultDisplayPanelZone.optInt("dpzo_seq");
		this.baseWidth = resultDisplayPanelZone.optInt("dpzo_base_width");
		this.baseHeight = resultDisplayPanelZone.optInt("dpzo_base_height");
		this.gridWidth = resultDisplayPanelZone.optInt("dpzo_grid_width");
		this.gridHeight = resultDisplayPanelZone.optInt("dpzo_grid_height");
		this.snapToGrid = resultDisplayPanelZone.optString("dpzo_snap_to_grid", PosDisplayPanelZone.SNAP_TO_GRID_NO);
		this.lookupPadding = resultDisplayPanelZone.optInt("dpzo_lookup_padding");
		this.status = resultDisplayPanelZone.optString("dpzo_status", PosDisplayPanelZone.STATUS_ACTIVE);		
	}
	
	// init value
	public void init() {
		this.dpzoId = 0;
		this.dpanId = 0;
		this.key = "";
		this.seq = 0;
		this.baseWidth = 0;
		this.baseHeight = 0;
		this.gridWidth = 0;
		this.gridHeight = 0;
		this.snapToGrid = PosDisplayPanelZone.SNAP_TO_GRID_NO;
		this.lookupPadding = 0;
		this.status = PosDisplayPanelZone.STATUS_ACTIVE;
	}
	
	/*** Getter and Setter methods ***/
	public void setZoneId(int iZoneId) {
		this.dpzoId = iZoneId;
	}
	
	public int getZoneId() {
		return this.dpzoId;
	}
	
	public int getPanelId() {
		return this.dpanId;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String sKey) {
		this.key = sKey;
	}
	
	public int getBaseWidth() {
		return this.baseWidth;
	}
	
	public int getBaseHeight() {
		return this.baseHeight;
	}
	
	public int getLookupPadding() {
		return this.lookupPadding;
	}
	
	public void setLookupPadding(int iLookupPadding) {
		this.lookupPadding = iLookupPadding;
	}
	
	//set status
	public void setStatus(String sStatus){
		this.status = sStatus;
	}
}
