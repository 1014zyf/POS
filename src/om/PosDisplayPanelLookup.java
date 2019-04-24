//Database: pos_display_panel_lookup - Content of the screen display panels
package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosDisplayPanelLookup {
	private int dpluId;
	private int dppgId;
	private String[] name;
	private String[] tips;
	private int seq;
	private String type;
	private int itemId;
	private int menuId;
	private int funcId;
	private int dtypId;
	private int paymId;
	private int subDppgId;
	private String character;
	private int left;
	private int top;
	private int width;
	private int height;
	private String param;
	private int dstyId;
	
	private MenuItem menuItem;
	private MenuMenu menuMenu;
	private PosFunction function;
	private PosDiscountType discountType;
	private PosPaymentMethod paymentMethod;
	
	// type
	public static String TYPE_NO = "";
	public static String TYPE_ALPHABET_NUMBERIC ="a";
	public static String TYPE_FUNCTION = "f";
	public static String TYPE_HOT_ITEM = "i";
	public static String TYPE_MENU_LOOKUP = "m";
	public static String TYPE_HOT_MODIFIER = "+";
	public static String TYPE_MODIFIER_LOOKUP = "x";
	public static String TYPE_DIRECT_DISCOUNT = "d";
	public static String TYPE_DIRECT_PAYMENT = "p";
	public static String TYPE_DIRECT_REPORT = "r";
	public static String TYPE_SUB_PANEL_PAGE = "s";
	
	public static String PARAM_NOT_SHOW_IMAGE = "not_show_image";
	public static String PARAM_COMMON_MODIFIER = "common_modifier";
	public static String PARAM_COMMON_MODIFIER_LOOKUP = "common_modifier_lookup";
	public static String PARAM_FULL_PAYMENT = "full_payment";
	public static String PARAM_REFERENCE_NO = "reference_no";
	public static String PARAM_DEFAULT_AMOUNT = "default_amount";
	public static String PARAM_AUTO_SEND_CHECK = "auto_send_check";
	
	public static String PARAM_NOT_SHOW_IMAGE_TRUE = "true";
	public static String PARAM_COMMON_MODIFIER_LOOKUP_TRUE = "true";
	public static String PARAM_COMMON_MODIFIER_TRUE = "true";
	
	//init object with initialize value
	public PosDisplayPanelLookup () {
		this.init();
	}
	
	//init object with JSON Object
	public PosDisplayPanelLookup(JSONObject displayPanelLookupsJSONObject) {
		readDataFromJson(displayPanelLookupsJSONObject);
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("display_panel_lookup")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("display_panel_lookup")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("display_panel_lookup");
			if(tempJSONObject.isNull("PosDisplayPanelLookup")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray displayPanelLookupJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("displayPanelLookup")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("displayPanelLookup"))
				return null;
			
			displayPanelLookupJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("displayPanelLookup");
		}
		
		return displayPanelLookupJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject displayPanelLookupJSONObject) {
		JSONObject resultDisplayPanelLookup = null;
		int i;
		
		resultDisplayPanelLookup = displayPanelLookupJSONObject.optJSONObject("PosDisplayPanelLookup");
		if(resultDisplayPanelLookup == null)
			resultDisplayPanelLookup = displayPanelLookupJSONObject;
		
		this.init();
		
		this.dpluId = resultDisplayPanelLookup.optInt("dplu_id");
		for(i=1; i<=5; i++) {
			this.name[(i-1)] = resultDisplayPanelLookup.optString("dplu_name_l"+i, null);
		}
		for(i=1; i<=5; i++) {
			this.tips[(i-1)] = resultDisplayPanelLookup.optString("dplu_tips_l"+i, null);
		}
		this.seq = resultDisplayPanelLookup.optInt("dplu_seq");
		this.type = resultDisplayPanelLookup.optString("dplu_type", PosDisplayPanelLookup.TYPE_NO);
		this.itemId = resultDisplayPanelLookup.optInt("dplu_item_id");
		this.menuId = resultDisplayPanelLookup.optInt("dplu_menu_id");
		this.funcId = resultDisplayPanelLookup.optInt("dplu_func_id");
		this.dtypId = resultDisplayPanelLookup.optInt("dplu_dtyp_id");
		this.paymId = resultDisplayPanelLookup.optInt("dplu_paym_id");
		this.subDppgId = resultDisplayPanelLookup.optInt("dplu_sub_dppg_id");
		this.character = resultDisplayPanelLookup.optString("dplu_char");
		this.left = resultDisplayPanelLookup.optInt("dplu_left");
		this.top = resultDisplayPanelLookup.optInt("dplu_top");
		this.width = resultDisplayPanelLookup.optInt("dplu_width");
		this.height = resultDisplayPanelLookup.optInt("dplu_height");
		this.param = resultDisplayPanelLookup.optString("dplu_param", null);
		this.dstyId = resultDisplayPanelLookup.optInt("dplu_dsty_id");

		//get hot item / hot modifier / menu lookup / modifier lookup data
		if (this.type.equals(PosDisplayPanelLookup.TYPE_HOT_ITEM) || this.type.equals(PosDisplayPanelLookup.TYPE_HOT_MODIFIER)) {

		} else if (this.type.equals(PosDisplayPanelLookup.TYPE_MENU_LOOKUP) || this.type.equals(PosDisplayPanelLookup.TYPE_MODIFIER_LOOKUP)) {
			
		} else if (this.type.equals(PosDisplayPanelLookup.TYPE_FUNCTION)) {
			JSONObject functionJSONObject = displayPanelLookupJSONObject.optJSONObject("PosFunction");
			if(functionJSONObject != null)
				setFunctionByJSONObject(functionJSONObject);
		} else if (this.type.equals(PosDisplayPanelLookup.TYPE_DIRECT_DISCOUNT)) {
			JSONObject discountTypeJSONObject = displayPanelLookupJSONObject.optJSONObject("PosDiscountType");
			if(discountTypeJSONObject != null)
				setDiscountTypeByJSONObject(discountTypeJSONObject);
		} else if (this.type.equals(PosDisplayPanelLookup.TYPE_DIRECT_PAYMENT)) {
			JSONObject paymentMethodJSONObject = displayPanelLookupJSONObject.optJSONObject("PosPaymentMethod");
			if(paymentMethodJSONObject != null)
				setPaymentMethodByJSONObject(paymentMethodJSONObject);
		} else if (this.type.equals(PosDisplayPanelLookup.TYPE_SUB_PANEL_PAGE)) {
			
		}
	}

	//construct the save request JSON
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		int i=0;
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("dplu_id", Integer.toString(this.dpluId));
			if(this.dppgId > 0)
				addSaveJSONObject.put("dplu_dppg_id", this.dppgId);
			for(i=1; i<=5; i++) {
				if (this.name[(i-1)] != null)
					addSaveJSONObject.put("dplu_name_l"+i, this.name[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(this.tips[(i-1)] != null)
					addSaveJSONObject.put("dplu_tips_l"+i, this.tips[(i-1)]);
			}
			if(this.seq > 0)
				addSaveJSONObject.put("dplu_seq", this.seq);
			if(!this.type.isEmpty())
				addSaveJSONObject.put("dplu_type", this.type);
			if(this.itemId > 0)
				addSaveJSONObject.put("dplu_item_id", this.itemId);
			if(this.menuId > 0)
				addSaveJSONObject.put("dplu_menu_id", this.menuId);
			if(this.funcId > 0)
				addSaveJSONObject.put("dplu_func_id", this.funcId);
			if(this.dtypId > 0)
				addSaveJSONObject.put("dplu_dtyp_id", this.dtypId);
			if(this.paymId > 0)
				addSaveJSONObject.put("dplu_paym_id", this.paymId);
			if(this.subDppgId > 0)
				addSaveJSONObject.put("dplu_sub_dppg_id", this.subDppgId);
			if(!this.character.isEmpty())
				addSaveJSONObject.put("dplu_char", this.character);
			if(this.left > 0)
				addSaveJSONObject.put("dplu_left", this.left);
			if(this.top > 0)
				addSaveJSONObject.put("dplu_top", this.top);
			if(this.width > 0)
				addSaveJSONObject.put("dplu_width", this.width);
			if(this.height > 0)
				addSaveJSONObject.put("dplu_height", this.height);
			if(this.param != null)
				addSaveJSONObject.put("dplu_param", this.param);
			if(this.dstyId > 0)
				addSaveJSONObject.put("dplu_dsty_id", this.dstyId);

		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	private void setPaymentMethodByJSONObject(JSONObject paymentMethodJSONObject) {
		this.paymentMethod = new PosPaymentMethod(paymentMethodJSONObject);
	}
	
	private void setFunctionByJSONObject(JSONObject functionJSONObject) {
		this.function = new PosFunction(functionJSONObject);
	}
	
	private void setDiscountTypeByJSONObject(JSONObject discountTypeJSONObject) {
		this.discountType = new PosDiscountType(discountTypeJSONObject);
	}

	public void setMenuItemByJSONObject(JSONObject menuItemJSONObject) {
		this.menuItem = new MenuItem(menuItemJSONObject);
	}

	public void setMenuMenuByJSONObject(JSONObject menuMenuJSONObject) {
		this.menuMenu = new MenuMenu(menuMenuJSONObject);
	}
		
	// init value
	public void init() {
		int i=0;
		
		this.dpluId = 0;
		this.dppgId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = null;
		if(this.tips == null)
			this.tips = new String[5];
		for(i=0; i<5; i++)
			this.tips[i] = null;
		this.seq = 0;
		this.type = PosDisplayPanelLookup.TYPE_NO;
		this.itemId = 0;
		this.menuId = 0;
		this.funcId = 0;
		this.dtypId = 0;
		this.paymId = 0;
		this.subDppgId = 0;
		this.character = "";
		this.left = 0;
		this.top = 0;
		this.width = 0;
		this.height = 0;
		this.param = null;
		this.dstyId = 0;
		
		this.menuItem = null;
		this.menuMenu = null;
		this.function = null;
		this.discountType = null;
		this.paymentMethod = null;
	}
	 
	/*** Getter and Setter methods ***/
	//get dpluId
	protected int getDpluId() {
		 return this.dpluId;
	 }
	
	public int getPageId() {
		return this.dppgId;
	}
	
	//get name by lang index
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	public String getBilingualName(int iIndex, int iBilingualLangIndex) {
		String sName = this.name[(iIndex-1)];
		
		if (iBilingualLangIndex > 0 && iIndex != iBilingualLangIndex)
			if(!sName.equals(this.name[(iBilingualLangIndex-1)]))
				sName += "\n" + this.name[(iBilingualLangIndex-1)];
		
		return sName;
	}
	
	//get tips by lang index
	public String getTips(int iIndex) {
		return this.tips[(iIndex-1)];
	}
	
	public int getSequence() {
		return this.seq;
	}
	
	public String getType() {
		return this.type;
	}
	
	public int getItemId() {
		return this.itemId;
	}
	
	public int getMenuId() {
		return this.menuId;
	}
	
	public int getFuncId() {
		return this.funcId;
	}
	
	public int getDiscountTypeId() {
		return this.dtypId;
	}
	
	public int getPaymentId() {
		return this.paymId;
	}
	
	public int getSubPanelPageId() {
		return this.subDppgId;
	}
	
	public String getCharacter() {
		return this.character;
	}
	
	public int getLeft() {
		return this.left;
	}

	public int getTop() {
		return this.top;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public String getParam() {
		return this.param;
	}
	
	public String getParamByVariableName(String sVariableName) {
		String sVariableValue = "";
		JSONObject oParamJSONObject = null;
		
		if(this.param == null || this.param.isEmpty())
			return sVariableValue;
		
		try {
			oParamJSONObject = new JSONObject(this.param);
			if(oParamJSONObject.has(sVariableName) && !oParamJSONObject.isNull(sVariableName))
				sVariableValue = oParamJSONObject.getString(sVariableName);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return sVariableValue;
	}
	
	public int getDstyId() {
		return this.dstyId;
	}
	
	public MenuMenu getMenuMenu() {
		return this.menuMenu;
	}
	
	//set name by lang index
	public void setName(int iIndex, String sName) {
		this.name[(iIndex-1)] = sName;
	}
	
	//set tips by lang index
	public void setTips(int iIndex, String sTips) {
		this.tips[(iIndex-1)] = sTips;
	}
	
	public void setSequence(int iSequence) {
		this.seq = iSequence;
	}
	
	public void setType(String sType) {
		this.type = sType;
	}

	public void setCharacter(String sCharacter) {
		this.character = sCharacter;
	}
	
	public void setLeft(int iLeft) {
		this.left = iLeft;
	}
	
	public void setTop(int iTop) {
		this.top = iTop;
	}
	
	public void setWidth(int iWidth) {
		this.width = iWidth;
	}
	
	public void setHeight(int iHeight) {
		this.height = iHeight;
	}
	
	public void setParam(String sParam) {
		this.param = sParam;
	}
	
	public void setFunctionId(int iFuncId) {
		this.funcId = iFuncId;
	}
	
	public void setPaymentId(int iPaymId) {
		this.paymId = iPaymId;
	}
	
	public void setSubPanelPageId(int iSubDppgId) {
		this.subDppgId = iSubDppgId;
	}
	
	public boolean isHotItem() {
		return this.type.equals(PosDisplayPanelLookup.TYPE_HOT_ITEM);
	}
	
	public boolean isHotModifier() {
		return this.type.equals(PosDisplayPanelLookup.TYPE_HOT_MODIFIER);
	}
	
	public boolean isMenuLookup() {
		return this.type.equals(PosDisplayPanelLookup.TYPE_MENU_LOOKUP);
	}
	
	public boolean isModifierLookup() {
		return this.type.equals(PosDisplayPanelLookup.TYPE_MODIFIER_LOOKUP);
	}
	
	public boolean isFunction() {
		return this.type.equals(PosDisplayPanelLookup.TYPE_FUNCTION);
	}
	
	public boolean isDirectPayment() {
		return this.type.equals(PosDisplayPanelLookup.TYPE_DIRECT_PAYMENT);
	}
	
	public boolean isDirectDiscount() {
		return this.type.equals(PosDisplayPanelLookup.TYPE_DIRECT_DISCOUNT);
	}
	
	public boolean isDirectReport() {
		return this.type.equals(PosDisplayPanelLookup.TYPE_DIRECT_REPORT);
	}
	
	public boolean isAlphabetNumberic() {
		return this.type.equals(PosDisplayPanelLookup.TYPE_ALPHABET_NUMBERIC);
	}
	
	public boolean isSubPanelPage() {
		return this.type.equals(PosDisplayPanelLookup.TYPE_SUB_PANEL_PAGE);
	}
}
