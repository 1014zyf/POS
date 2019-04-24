package app.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.model.*;

public class FuncStation {
	// Station
	private PosStation m_oStation;
	
	// Station device
	private PosStationDevice m_oStationDevice;
	
	// Taiwan GUI configuration
	private PosTaiwanGuiConfig m_oPosTaiwanGuiConfig;
	
	// Last used station's check number
	private String m_sLastCheckPrefix;
	private Integer m_iLastCheckNumber;
	
	// Under ordering
	private boolean m_bUnderOrdering;
	
	// Open table mode
	private int m_iOpenTableScreenMode;
	
	static public int OPEN_TABLE_SCREEN_MODE_FLOOR_PLAN = 0;
	static public int OPEN_TABLE_SCREEN_MODE_ASK_TABLE = 1;
	static public int OPEN_TABLE_SCREEN_MODE_TABLE_MODE = 2;
	
	static public int ORDERING_TIMEOUT_OPTION_PROMPT_SELECT = 0;
	static public int ORDERING_TIMEOUT_OPTION_QUIT_CHECK_DIRECTLY = 1;
	
	// Last error message
	private String m_sErrorMessage;
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage;
	}
	
	public PosStation getStation(){
		return m_oStation;
	}
	
	public int getStationId(){
		return m_oStation.getStatId();
	}
	
	public int getOutletId() {
		return m_oStation.getOletId();
	}
	
	public int getDisplayPanelId() {
		return m_oStation.getDpanId();
	}
	
	public int getDisplayStyleId() {
		return m_oStation.getSdevId();
	}
	
	public int getStationStartCheckNumber() {
		return m_oStation.getStartCheckNumber();
	}
	
	public String getCheckPrefix(){
		return m_oStation.getCheckPrefix();
	}
	
	public int getCheckPrtqId() {
		return m_oStation.getCheckPrtqId();
	}
	
	public String getPrintFormatNameByPfmtId(int iPfmtId, int iIndex) {
		return m_oStation.getPrintFormatName(iPfmtId, iIndex);
	}
	
	protected int getReportPrtqId() {
		return m_oStation.getReportPrtqId();
	}
	
	protected int getReportSlipPrtqId() {
		return m_oStation.getReportSlipPrtqId();
	}
	
	//get name by lang index
	public String getName(int iIndex) {
		return m_oStation.getName(iIndex);
	}
	
	//get short name by lang index
	public String getShortName(int iIndex) {
		return m_oStation.getShortName(iIndex);
	}
	
	//get code
	public String getCode() {
		return m_oStation.getCode();
	}
	
	//get address
	public String getAddress() {
		return m_oStation.getAddress();
	}
	
	//get auto sign out
	public String getAutoSignOut() {
		return m_oStation.getAutoSignOut();
	}
	
	//get ordering mode
	public String getOrderingMode() {
		return m_oStation.getOrderingMode();
	}
	
	public PosStationDevice getStationDevice(){
		return m_oStationDevice;
	}
	
	public String getStationDeviceKey() {
		return m_oStationDevice.getKey();
	}
	
	public int getOpenTableScreenMode() {
		return m_iOpenTableScreenMode;
	}
	
	public void setOpenTableScreenMode(int iMode) {
		m_iOpenTableScreenMode = iMode;
	}
	
	public void setLastCheckPrefixNumber(String sPrefix, int iLastCheckNum) {
		synchronized (m_sLastCheckPrefix) {
			m_sLastCheckPrefix = sPrefix;
		}
		synchronized (m_iLastCheckNumber) {
			m_iLastCheckNumber = Integer.valueOf(iLastCheckNum);
		}
	}
	
	public void addOneToLastCheckNumber() {
		synchronized (m_iLastCheckNumber) {
			m_iLastCheckNumber = Integer.valueOf((m_iLastCheckNumber.intValue()+1));
		}
	}
	
	// generate the next check number(generated by station)
	// **** the check number is reference only ****
	public String getNextCheckPrefixNumber(boolean bHashtag) {
		int iNextCheckNumber = 0;
		String sNextCheckPrefix = "";
		String sNextCheckPrefixNum = "";
		
		// add 1 to last check number
		synchronized (m_iLastCheckNumber) {
			iNextCheckNumber = m_iLastCheckNumber.intValue() + 1;
		}
		synchronized (m_sLastCheckPrefix) {
			sNextCheckPrefix = m_sLastCheckPrefix;
		}
		
		//reach the end check number, roll back to the start check number
		if(iNextCheckNumber > m_oStation.getEndCheckNumber()) 
			iNextCheckNumber = m_oStation.getStartCheckNumber();
		
		// packing leading zero
		double dCheckNumberLen = 4;		// for spare check number range
		if(sNextCheckPrefix.equals(m_oStation.getCheckPrefix()) && m_oStation.getEndCheckNumber() > 0)
			dCheckNumberLen = Math.floor(Math.log10(m_oStation.getEndCheckNumber())) + 1;
		
		// combine the prefix number and check number
		String sCheckStringFormat = "%0"+(int)dCheckNumberLen+"d";
		sNextCheckPrefixNum = sNextCheckPrefix+String.format(sCheckStringFormat, iNextCheckNumber);
		if(bHashtag)
			sNextCheckPrefixNum += "#";
			
		return sNextCheckPrefixNum;
	}
	
	//check whether is ordering mode
	public boolean isFastFoodOrderingMode() {
		return m_oStation.isFastFoodOrderingMode();
	}
	
	//check whether is self-order kiosk ordering mode
	public boolean isSelfOrderKioskOrderingMode() {
		return m_oStation.isSelfOrderKioskOrderingMode();
	}
	
	//get station group id
	public int getStationGroupId() {
		return m_oStation.getStgpId();
	}
	
	public boolean loadStation(String sIP){
		m_sErrorMessage = "";
		
		// OM (pos_stations)
		m_oStation = new PosStation();
		
		if(!m_oStation.readByAddress(sIP)){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("no_such_station");
			return false;
		}
		
		if(m_oStation.getStatId() == 0){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("no_such_station");
			return false;
		}
		
		// OM (pos_station_devices)
		m_oStationDevice = new PosStationDevice();
		
		if (!m_oStationDevice.readById(m_oStation.getSdevId())) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("no_such_station_device");
			return false;
		}
		
		if(m_oStationDevice.getSdevId() == 0){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("no_such_station_device");
			return false;
		}
		
		m_sLastCheckPrefix = new String();
		synchronized(m_sLastCheckPrefix) {
			m_sLastCheckPrefix = "";
		}
		m_iLastCheckNumber = new Integer(0);
		synchronized(m_iLastCheckNumber) {
			m_iLastCheckNumber = 0;
		}
		
		m_iOpenTableScreenMode = OPEN_TABLE_SCREEN_MODE_FLOOR_PLAN;
		
		return true;
	}
	
	public boolean loadStationById(int iStatId){
		m_sErrorMessage = "";
		
		// OM (pos_stations)
		m_oStation = new PosStation();
		
		if(!m_oStation.readById(iStatId)){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_load_station");
			return false;
		}
		
		if(m_oStation.getStatId() == 0){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("no_such_station");
			return false;
		}
		
		// OM (pos_station_devices)
		m_oStationDevice = new PosStationDevice();
		
		if (!m_oStationDevice.readById(m_oStation.getSdevId())) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("no_such_station_device");
			return false;
		}
		
		if(m_oStationDevice.getSdevId() == 0){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("no_such_station_device");
			return false;
		}
		m_sLastCheckPrefix = new String();
		synchronized(m_sLastCheckPrefix) {
			m_sLastCheckPrefix = "";
		}
		m_iLastCheckNumber = new Integer(0);
		synchronized(m_iLastCheckNumber) {
			m_iLastCheckNumber = 0;
		}
		
		m_iOpenTableScreenMode = OPEN_TABLE_SCREEN_MODE_FLOOR_PLAN;
		
		return true;
	}
	
	public boolean loadFirstAutoStation(int iOutletId) {
		m_sErrorMessage = "";
		
		// OM (pos_stations)
		boolean bHaveStation = false;
		m_oStation = new PosStation();
		JSONArray oStationJSONArray = m_oStation.getStationByOutletIdDeviceKey(iOutletId, PosStationDevice.KEY_AUTO_STATION);
		if(oStationJSONArray != null){
			for(int i=0; i<oStationJSONArray.length(); i++) {
				try {
					// Retrieve the first station
					m_oStation = new PosStation(oStationJSONArray.getJSONObject(i));
					bHaveStation = true;
					break;
				}catch(JSONException jsone) {
					jsone.printStackTrace();
				}
			}
		}
		
		return bHaveStation;
	}
	
	public boolean loadTaiwanGuiConfig(String sDate) {
		boolean bResult;
		
		m_sErrorMessage = "";
		
		m_oPosTaiwanGuiConfig = new PosTaiwanGuiConfig();
		if(m_oStation.getParams().optJSONObject("tgui").optString("generate_by").equals(PosTaiwanGuiConfig.GENERATED_BY_STATION))
			bResult = m_oPosTaiwanGuiConfig.readByDateAndStation(m_oStation.getStatId());
		else
			bResult = m_oPosTaiwanGuiConfig.readByDateAndOutlet(sDate, m_oStation.getOletId());
		
		if(!bResult){
			// Load configuration error
			m_sErrorMessage = AppGlobal.g_oLang.get()._("missing_taiwan_gui_configuration");
		}
		
		return bResult;
	}
	
	public boolean supportTaiwanGui() {
		if(m_oStation.getParams() == null)
			return false;

		if(m_oStation.getParams().has("tgui"))
			return true;

		return false;
	}

	// check whether taiwan gui number is generated by station
	public boolean isTaiwanGuiGeneratedByStation() {
		if(m_oStation.getParams() == null)
			return false;
		
		if(!m_oStation.getParams().has("tgui"))
			return false;
		
		String sGenerateBy = m_oStation.getParams().optJSONObject("tgui").optString("generate_by");
		
		if(sGenerateBy != null && sGenerateBy.equals(PosTaiwanGuiConfig.GENERATED_BY_STATION))
			return true;
		else
			return false;
	}

	public String getTaiwanGuiGeneratedBy() {
		if(!supportTaiwanGui())
			return null;
		
		try {
			JSONObject oJsonObject = m_oStation.getParams().getJSONObject("tgui");
			if(oJsonObject.has("generate_by"))
				return oJsonObject.optString("generate_by");
			else
				return null;
		} catch (JSONException e) {
			AppGlobal.stack2Log(e);
			return null;
		}
	}

	public int getTaiwanGuiConfigId() {
		if(m_oPosTaiwanGuiConfig != null)
			return m_oPosTaiwanGuiConfig.getTaiwanGuiConfigId();
		else
			return 0;
	}
	
	public int getTaiwanGuiStartNum() {
		if(m_oPosTaiwanGuiConfig != null)
			return m_oPosTaiwanGuiConfig.getStartNum();
		else
			return 0;
	}
	
	public int getTaiwanGuiEndNum() {
		if(m_oPosTaiwanGuiConfig != null)
			return m_oPosTaiwanGuiConfig.getEndNum();
		else
			return 0;
	}
	
	public int getTaiwanGUIWarningLimit() {
		if(m_oPosTaiwanGuiConfig != null)
			return m_oPosTaiwanGuiConfig.getWarningLimit();
		else
			return 0;
	}
	
	public int getTaiwanGuiNormalTaxIndex() {
		if(!supportTaiwanGui())
			return 0;
		
		try {
			JSONObject oJsonObject = m_oStation.getParams().getJSONObject("tgui");
			if(oJsonObject.has("normal_tax_index")){
				return oJsonObject.optInt("normal_tax_index");
			}
		} catch (JSONException e) {
			AppGlobal.stack2Log(e);
		}
		
		return 0;
	}
	
	public int getTaiwanGuiEntertainmentTaxIndex() {
		if(!supportTaiwanGui())
			return 0;
		
		try {
			JSONObject oJsonObject = m_oStation.getParams().getJSONObject("tgui");
			if(oJsonObject.has("ent_tax_index")){
				return oJsonObject.optInt("ent_tax_index");
			}			
		} catch (JSONException e) {
			AppGlobal.stack2Log(e);
		}
		
		return 0;
	}
	
	public int getTaiWanGuiPrintFormatId() {
		if(!supportTaiwanGui())
			return 0;
		
		try {
			JSONObject oJsonObject = m_oStation.getParams().getJSONObject("tgui");
			if(oJsonObject.has("pfmt_id")){
				return oJsonObject.optInt("pfmt_id");
			}			
		} catch (JSONException e) {
			AppGlobal.stack2Log(e);
		}
		
		return 0;
	}
	
	public void setUnderOrdering(boolean bUnderOrdering){
		m_bUnderOrdering = bUnderOrdering;
	}
	
	public boolean getUnderOrdering(){
		return m_bUnderOrdering;
	}
	
   	public int getDefaultTableNoForMenuMode(){
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "menu_mode");
		int iDefaultTableNo = 0;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					iDefaultTableNo = Integer.parseInt(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return iDefaultTableNo;
   	}

   	public int getPayResultAutoSwitchTimeControl(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "auto_switch_from_pay_result_to_starting_page_time_control");
   		int iValue = -1;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					iValue = Integer.parseInt(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return iValue;
   	}
  	
   	public int getApplyDiscountRestriction(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "apply_discount_restriction");
   		int iValue = -1;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					iValue = Integer.parseInt(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return iValue;
   	}

   	public int getDoubleCheckDiscountAlert(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "double_check_discount_alert");
   		int iValue = -1;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					iValue = Integer.parseInt(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return iValue;
   	}
	
   	public int getOrderingModeForAutoCloseCashierPanel(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "auto_close_cashier_panel");
   		int iValue = -1;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					iValue = Integer.parseInt(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return iValue;
   	}

   	public Integer getBusinessHourWarnLevel(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "business_hour_warn_level");
   		int iValue = -1;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					iValue = Integer.parseInt(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return iValue;
   	}
   	
   	public int getOrderingTimeout(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "ordering_timeout");
   		int iValue = -1;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					iValue = Integer.parseInt(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return iValue;
   	}

   	public int getOpenTableScreenModeConfig(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "open_table_screen_mode");
   		int iValue = -1;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					iValue = Integer.parseInt(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return iValue;
   	}

   	public boolean getOrderingPanelInputNumpad(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "ordering_panel_input_numpad");
   		Boolean bValue = false;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bValue;
   	}

   	public boolean getNotCheckStock(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "not_check_stock");
   		Boolean bValue = false;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bValue;
   	}

   	public boolean getOrderingPanelShowPrice(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "ordering_panel_show_price");
   		Boolean bValue = false;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bValue;
   	}

   	public boolean getFastFoodAutoTakeout(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "fast_food_auto_takeout");
   		Boolean bValue = false;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bValue;
   	}

   	public boolean getFastFoodNotAutoWaiveSerCharge(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "fast_food_not_auto_waive_service_charge");
   		Boolean bValue = false;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bValue;
   	}

   	public boolean getSupportNumericPluOnly(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "support_numeric_plu_only");
   		Boolean bValue = false;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bValue;
   	}

   	public boolean getFastFoodNotPrintReceipt(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "fast_food_not_print_receipt");
   		Boolean bValue = false;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bValue;
   	}

   	public boolean getFineDiningNotPrintReceipt(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "fine_dining_not_print_receipt");
   		Boolean bValue = false;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bValue;
   	}

   	public boolean getCalcInclusiveTaxRefByCheckTotal(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "calc_inclusive_tax_ref_by_check_total");
   		Boolean bValue = false;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bValue;
   	}
	
   	public String getCheckInfoSelfDefineDesc(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "check_info_self_define_description");
   		String sValue = null;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					sValue = oPosConfig.getValue();
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return sValue;
   	}

   	public String getVoidReasonForPaymentAutoDiscount(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "void_reason_for_payment_auto_discount");
   		String sValue = null;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					sValue = oPosConfig.getValue();
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return sValue;
   	}
   	
   	public String getBarcodeOrderingFormat(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "barcode_ordering_format");
   		String sValue = null;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					sValue = oPosConfig.getValue();
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return sValue;
   	}

   	public String getInterfaceUrl(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "interface_url");
   		String sValue = null;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					sValue = oPosConfig.getValue();
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return sValue;
   	}

   	public String getTenderAmount(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "tender_amount");
   		String sValue = null;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					sValue = oPosConfig.getValue();
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return sValue;
   	}
   	
   	public int getCoverLimitNumber(){
  		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "cover_limit");
  		int iValue = 0;
  		if(oPosConfig != null){
  			if(oPosConfig.getValue() != null){
  				try{
  					iValue = Integer.parseInt(oPosConfig.getValue());
  				}catch(Exception e){
  					// Incorrect format
  				}
  			}
  		}
  		return iValue;
  	}
   	
   	public int getOrderingTimeoutOption(){
   		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "ordering_timeout_option");
  		int iValue = 0;
  		if(oPosConfig != null){
  			if(oPosConfig.getValue() != null){
  				try{
  					iValue = Integer.parseInt(oPosConfig.getValue());
  				}catch(Exception e){
  					// Incorrect format
  				}
  			}
  		}
  		return iValue;
   	}
}