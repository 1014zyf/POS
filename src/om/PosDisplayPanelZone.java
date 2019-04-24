package om;

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
	private JSONObject params;
	private int mediId;
	private String status;
	
	// key
	public static String KEY_ORDERING = "ordering";
	public static String KEY_ORDERING_BASKET = "ordering_basket";
	public static String KEY_ORDERING_FUNCTION = "ordering_function";
	public static String KEY_CASHIER = "cashier";
	public static String KEY_ITEM_SELECTED = "item_selected";
	public static String KEY_FLOOR_PLAN_FUNCITON = "floor_plan_function";
	
	// snapToGrid
	public static String SNAP_TO_GRID_NO = "";
	public static String SNAP_TO_GRID_YES = "y";
	
	// params
	private static final String PARAMS_SECTION_GENERAL 						= "general";
	private static final String PARAMS_WIDTH_PERCENT						= "width_percent";
	private static final String PARAMS_HEIGHT_PERCENT						= "height_percent";
	private static final String PARAMS_DISPLAY_BASKET_HEADER				= "display_basket_header";
	private static final String PARAMS_DISPLAY_CHECK_ORDERING_TYPE			= "display_check_ordering_type";
	private static final String PARAMS_DISPLAY_ITEM_PRICE					= "display_item_price";
	private static final String PARAMS_DISPLAY_ITEM_SEQUENCE				= "display_item_sequence";
	private static final String PARAMS_DISPLAY_ORDERING_BASKET_EXTENSION	= "display_ordering_basket_extension";
	private static final String PARAMS_DISPLAY_SEAT_NUMBER_SECTION			= "display_seat_number_section";
	private static final String PARAMS_NUMBER_OF_BASKET_ITEMS				= "number_of_basket_item";
	private static final String PARAMS_HEADER_HEIGHT_PERCENT				= "header_height_percent";
	
	private static final String PARAMS_SECTION_FONT_STYLE = "font_style";
	private static final String PARAMS_BASKET_ITEM_DESCRIPTION_FONT_SIZE		= "basket_item_description_font_size";
	private static final String PARAMS_BASKET_ITEM_QUANTITY_FONT_SIZE		= "basket_item_quantity_font_size";
	private static final String PARAMS_BASKET_ITEM_PRICE_FONT_SIZE			= "basket_item_price_font_size";
	private static final String PARAMS_BASKET_EXTENSION_FONT_SIZE			= "basket_extension_font_size";
	private static final String PARAMS_BASKET_CHECK_TOTAL_FONT_SIZE			= "basket_check_total_font_size";
	
	// ordering basket status
	public static String ORDERING_BASKET_ZONE_STATUS_DISPLAY = "y";
	public static String ORDERING_BASKET_ZONE_STATUS_NON_DISPLAY = "n";
	public static String ORDERING_BASKET_ZONE_STATUS_DEFAULT = "";
	
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
			if (this.params != null)
				addSaveJSONObject.put("dpzo_params", this.params.toString());
			if (this.mediId > 0)
				addSaveJSONObject.put("dpzo_bg_medi_id", this.mediId);
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
		try {
			this.params = new JSONObject(resultDisplayPanelZone.optString("dpzo_params"));
		} catch (JSONException e) {
			this.params = null;
		}
		this.mediId = resultDisplayPanelZone.optInt("dpzo_bg_medi_id");
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
		this.params = null;
		this.mediId = 0;
		this.status = PosDisplayPanelZone.STATUS_ACTIVE;
	}
	
	/*** Getter and Setter methods ***/
	public void setZoneId(int iZoneId) {
		this.dpzoId = iZoneId;
	}

	public void setKey(String sKey) {
		this.key = sKey;
	}
	
	public void setLookupPadding(int iLookupPadding) {
		this.lookupPadding = iLookupPadding;
	}

	//set params
	public void setParams(JSONObject oParams) {
		this.params = oParams;
	}
	
	//set status
	public void setStatus(String sStatus){
		this.status = sStatus;
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
	public int getBaseWidth() {
		return this.baseWidth;
	}
	
	public int getBaseHeight() {
		return this.baseHeight;
	}
	
	public int getLookupPadding() {
		return this.lookupPadding;
	}
	
	
	public int getMediaId() {
		return this.mediId;
	}
	
	public JSONObject getParams(){
		return this.params;
	}
	
	private String getParamsStringValue(String sSection, String sKey, String sDefaultValue) {
		String sValue = sDefaultValue;
		if(this.getParams() != null) {
			if (this.getParams().has(sSection))
				sValue = this.getParams().optJSONObject(sSection).optString(sKey, sDefaultValue);
			else
				sValue = this.getParams().optString(sKey, sDefaultValue);
		}
		return sValue;
	}
	
	private int getParamsIntValue(String sSection, String sKey) {
		int iValue = 0;
		if(this.getParams() != null) {
			if (this.getParams().has(sSection))
				iValue = this.getParams().optJSONObject(sSection).optInt(sKey);
			else
				iValue = this.getParams().optInt(sKey);
		}
		return iValue;
	}
	
	public int getWidthPercent() {
		return this.getParamsIntValue(PosDisplayPanelZone.PARAMS_SECTION_GENERAL, PosDisplayPanelZone.PARAMS_WIDTH_PERCENT);
	}
	
	public int getHeightPercent() {
		return this.getParamsIntValue(PosDisplayPanelZone.PARAMS_SECTION_GENERAL, PosDisplayPanelZone.PARAMS_HEIGHT_PERCENT);
	}

	public int getNumberOfBasketItem() {
		return this.getParamsIntValue(PosDisplayPanelZone.PARAMS_SECTION_GENERAL, PosDisplayPanelZone.PARAMS_NUMBER_OF_BASKET_ITEMS);
	}
	
	public boolean isDisplayBasketHeader() {
		String sDisplayBasketHeader = this.getParamsStringValue(PosDisplayPanelZone.PARAMS_SECTION_GENERAL, PosDisplayPanelZone.PARAMS_DISPLAY_BASKET_HEADER, PosDisplayPanelZone.ORDERING_BASKET_ZONE_STATUS_NON_DISPLAY);
		return sDisplayBasketHeader.equals(PosDisplayPanelZone.ORDERING_BASKET_ZONE_STATUS_DISPLAY);
	}
	
	public boolean isDisplayCheckOrderingType() {
		String sDisplayCheckOrderingType = this.getParamsStringValue(PosDisplayPanelZone.PARAMS_SECTION_GENERAL, PosDisplayPanelZone.PARAMS_DISPLAY_CHECK_ORDERING_TYPE, PosDisplayPanelZone.ORDERING_BASKET_ZONE_STATUS_NON_DISPLAY);
		return sDisplayCheckOrderingType.equals(PosDisplayPanelZone.ORDERING_BASKET_ZONE_STATUS_DISPLAY);
	}
	
	public boolean isDisplayItemPrice() {
		String sDisplayItemPrice = this.getParamsStringValue(PosDisplayPanelZone.PARAMS_SECTION_GENERAL, PosDisplayPanelZone.PARAMS_DISPLAY_ITEM_PRICE, PosDisplayPanelZone.ORDERING_BASKET_ZONE_STATUS_DISPLAY);
		return sDisplayItemPrice.equals(PosDisplayPanelZone.ORDERING_BASKET_ZONE_STATUS_DISPLAY);
	}
	
	public boolean isDisplayItemSequence() {
		String sDisplayItemSequence = this.getParamsStringValue(PosDisplayPanelZone.PARAMS_SECTION_GENERAL, PosDisplayPanelZone.PARAMS_DISPLAY_ITEM_SEQUENCE, PosDisplayPanelZone.ORDERING_BASKET_ZONE_STATUS_NON_DISPLAY);
		return sDisplayItemSequence.equals(PosDisplayPanelZone.ORDERING_BASKET_ZONE_STATUS_DISPLAY);
	}
	
	public boolean isDisplayOrderingBasketExtension() {
		String sDisplayOrderingBasketExtension = this.getParamsStringValue(PosDisplayPanelZone.PARAMS_SECTION_GENERAL, PosDisplayPanelZone.PARAMS_DISPLAY_ORDERING_BASKET_EXTENSION, PosDisplayPanelZone.ORDERING_BASKET_ZONE_STATUS_DISPLAY);
		return sDisplayOrderingBasketExtension.equals(PosDisplayPanelZone.ORDERING_BASKET_ZONE_STATUS_DISPLAY);
	}
	
	public boolean isDisplaySeatNumberSection() {
		String sDisplaySeatNumberSection= this.getParamsStringValue(PosDisplayPanelZone.PARAMS_SECTION_GENERAL, PosDisplayPanelZone.PARAMS_DISPLAY_SEAT_NUMBER_SECTION, PosDisplayPanelZone.ORDERING_BASKET_ZONE_STATUS_DISPLAY);
		return sDisplaySeatNumberSection.equals(PosDisplayPanelZone.ORDERING_BASKET_ZONE_STATUS_DISPLAY);
	}
	
	public int getHeaderHeightPercent() {
		return this.getParamsIntValue(PosDisplayPanelZone.PARAMS_SECTION_GENERAL, PosDisplayPanelZone.PARAMS_HEADER_HEIGHT_PERCENT);
	}
	
	public int getBasketItemDescriptionFontSize() {
		return this.getParamsIntValue(PosDisplayPanelZone.PARAMS_SECTION_FONT_STYLE, PosDisplayPanelZone.PARAMS_BASKET_ITEM_DESCRIPTION_FONT_SIZE);
	}
	
	public int getBasketItemQuantityFontSize() {
		return this.getParamsIntValue(PosDisplayPanelZone.PARAMS_SECTION_FONT_STYLE, PosDisplayPanelZone.PARAMS_BASKET_ITEM_QUANTITY_FONT_SIZE);
	}
	
	public int getBasketItemPriceFontSize() {
		return this.getParamsIntValue(PosDisplayPanelZone.PARAMS_SECTION_FONT_STYLE, PosDisplayPanelZone.PARAMS_BASKET_ITEM_PRICE_FONT_SIZE);
	}
	
	public int getBasketExtensionFontSize() {
		return this.getParamsIntValue(PosDisplayPanelZone.PARAMS_SECTION_FONT_STYLE, PosDisplayPanelZone.PARAMS_BASKET_EXTENSION_FONT_SIZE);
	}
	
	public int getBasketCheckTotalFontSize() {
		return this.getParamsIntValue(PosDisplayPanelZone.PARAMS_SECTION_FONT_STYLE, PosDisplayPanelZone.PARAMS_BASKET_CHECK_TOTAL_FONT_SIZE);
	}
}
