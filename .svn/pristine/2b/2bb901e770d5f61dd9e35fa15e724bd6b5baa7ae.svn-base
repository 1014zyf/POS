package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import om.*;

public class FuncStation {
	// Station
	private PosStation m_oStation;
	
	// Station device
	private PosStationDevice m_oStationDevice;
	
	// Last used station's check number
	private String m_sLastCheckPrefix;
	private Integer m_iLastCheckNumber;
	
	// Under ordering
	private boolean m_bUnderOrdering;
	
	// Open table mode
	private int m_iOpenTableScreenMode;
	
	// Taiwan GUI information
	private PosTaiwanGuiConfigList m_oPosTaiwanGuiConfigs;
	private PosTaiwanGuiConfigList m_oAllPosTaiwanGuiConfigs;
	private String m_sTaiwanGUIType;
	

	
	static public int OPEN_TABLE_SCREEN_MODE_FLOOR_PLAN = 0;
	static public int OPEN_TABLE_SCREEN_MODE_ASK_TABLE = 1;
	static public int OPEN_TABLE_SCREEN_MODE_TABLE_MODE = 2;
	
	static public int ORDERING_TIMEOUT_OPTION_PROMPT_SELECT = 0;
	static public int ORDERING_TIMEOUT_OPTION_QUIT_CHECK_DIRECTLY = 1;
	
	private int m_iOpenTableScreenKeyboard;
	static public int OPEN_TABLE_SCREEN_KEYBOARD_NUMBER = 0;
	static public int OPEN_TABLE_SCREEN_KEYBOARD_QWERTY = 1;
	static public int ITEM_STOCK_INPUT_REPLACE = 0;
	static public int ITEM_STOCK_INPUT_ADD_ON = 1;
	
	static public String CHECK_LISTING_CALCULATION_METHOD_CHECK_TOTAL = "c";
	static public String CHECK_LISTING_CALCULATION_METHOD_TOTAL_DUE = "t";
	
	private UserUserList m_oUserUserList;
	
	//loyalty login
	private String m_sLoyaltySessionId = "";
	private String m_sLoyaltySvcSessionId = "";
	
	// Last error message
	private String m_sErrorMessage;
	
	// Ordering Basket Zone params
	static public String ORDERING_BASKET_ZONE_STATUS_DISPLAY = "y";
	static public String ORDERING_BASKET_ZONE_STATUS_NON_DISPLAY = "n";
	static public String ORDERING_BASKET_ZONE_STATUS_DEFAULT = "";
	static public int ORDERING_BASKET_ZONE_ITEM_DEFAULT = 0;
	
	// Do not Print Receipt for Fast Food / Fine Dining
	static public String DO_NOT_PRINT_RECEIPT_ASK_OPTIONS = "o";
	
	private GeneralStyleMap m_oHeroStyleMap = new GeneralStyleMap();

	public GeneralStyleMap getHeroStyleMap() {
		if(m_oHeroStyleMap != null )
			return m_oHeroStyleMap;
		else
			return null;
	}

	public void setHeroStyleMap(GeneralStyleMap m_oHeroStyleMap) {
		this.m_oHeroStyleMap = m_oHeroStyleMap;
	}
	
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
	
	public int getStationEndCheckNumber() {
		return m_oStation.getEndCheckNumber();
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
	
	//get name
	public String[] getName() {
		return m_oStation.getName();
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
	
	public int getOpenTableScreenKeyboard() {
		return m_iOpenTableScreenKeyboard;
	}
	
	public void setOpenTableScreenKeyboard(int iMode) {
		m_iOpenTableScreenKeyboard = iMode;
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
		
		if (AppGlobal.g_oFuncSmartStation.isStandaloneRole()) {
			// combine the prefix number and check number
			sNextCheckPrefixNum = sNextCheckPrefix+String.format("%09d", getOverrideCheckNo()+1);
			if(bHashtag)
				sNextCheckPrefixNum += "#";
		} else {
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
		}
			
		return sNextCheckPrefixNum;
	}
	
	// For Workstation 2.0 standalone mode ONLY
	// Check prefix format:
	// Hex value of station id from 3rd digits
	public String getOverrideCheckPrefix() {
		
		long lStationId = AppGlobal.g_oFuncStation.get().getStationId();
		String sTmp = String.format("%010d", lStationId);
		String sStationIdHexStringFrom3rdDigit = Integer.toHexString(Integer.parseInt(sTmp.substring(4))).toUpperCase();
		
		// Replace the first char to special character
		// e.g. First char of prefix 0 -> ! (value = 33), 1 --> " (value = 34) .. , E -> / (value = 47), F -> : (value = 58)
		switch (sStationIdHexStringFrom3rdDigit.charAt(0)) {
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			sStationIdHexStringFrom3rdDigit = (char)(sStationIdHexStringFrom3rdDigit.charAt(0)-'1'+'!') + sStationIdHexStringFrom3rdDigit.substring(1);
			break;
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
			sStationIdHexStringFrom3rdDigit = (char)(sStationIdHexStringFrom3rdDigit.charAt(0)-'A'+(char)(9)+'!') + sStationIdHexStringFrom3rdDigit.substring(1);
			break;
		}
		
		return sStationIdHexStringFrom3rdDigit;
	}
	
	// For Workstation 2.0 standalone mode ONLY
	// Check no. format:
	// [check no.: <station ID first 2 digits --> XX><Time: HHM><running no. 0-9999>]
	public long getOverrideCheckNo() {
		int iFormattedCheckNumber = 0;
		
		long lStationId = AppGlobal.g_oFuncStation.get().getStationId();		
		String sTmp = String.format("%010d", lStationId);
		String sStationIdFirst2Digit = sTmp.substring(2, 4);
		
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("HHmm");
		String sHHM = dateFormat.print(today).substring(0,3);
		
		String sNextCheckNo= "";
		synchronized (m_iLastCheckNumber) {
			sNextCheckNo = String.format("%04d", m_iLastCheckNumber);
		}
		
		String sFormattedCheckNo = sStationIdFirst2Digit + sHHM + sNextCheckNo;
		iFormattedCheckNumber = Integer.parseInt(sFormattedCheckNo);
		
		return iFormattedCheckNumber;
	}
	
	//check whether is ordering mode
	public boolean isFastFoodOrderingMode() {
		return m_oStation.isFastFoodOrderingMode();
	}
	
	//check whether is self-order kiosk ordering mode
	public boolean isSelfOrderKioskOrderingMode() {
		return m_oStation.isSelfOrderKioskOrderingMode();
	}
	
	//check whether is bar tab ordering mode
	public boolean isBarTabOrderingMode() {
		return m_oStation.isBarTabOrderingMode();
	}
	
	//get station group id
	public int getStationGroupId() {
		return m_oStation.getStgpId();
	}
	
	public int getFailoverStationId() {
		return m_oStation.getFailoverStatId();
	}
	
	public boolean loadStation(String sIP, boolean bAllowAutoAssign){
		m_sErrorMessage = "";
		
		// OM (pos_stations)
		m_oStation = new PosStation();
		
		if(!m_oStation.readByAddress(sIP, bAllowAutoAssign)){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("no_such_station");
			if (sIP != null)
				m_sErrorMessage += " (" + sIP + ")";
			return false;
		}
		
		if(m_oStation.getStatId() == 0){
			m_sErrorMessage = AppGlobal.g_oLang.get()._("no_such_station");
			if (sIP != null)
				m_sErrorMessage += " (" + sIP + ")";
			return false;
		}
		
		// OM (pos_station_devices)
		m_oStationDevice = m_oStation.getStationDevice();
		if (m_oStationDevice == null) {
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
		m_sTaiwanGUIType = PosTaiwanGuiConfig.TYPE_NORMAL;
		
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
		m_oStationDevice = m_oStation.getStationDevice();
		if (m_oStationDevice == null) {
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
		m_sTaiwanGUIType = PosTaiwanGuiConfig.TYPE_NORMAL;
		
		return true;
	}
	
	public boolean loadFirstAutoStation(int iOutletId) {
		m_sErrorMessage = "";
		
		// OM (pos_stations)
		boolean bHaveStation = false;
		m_oStation = new PosStation();
		JSONArray oStationJSONArray = m_oStation.getStationByOutletIdDeviceKey(iOutletId, PosStationDevice.KEY_AUTO_STATION);
		if (oStationJSONArray != null) {
			for (int i = 0; i < oStationJSONArray.length(); i++) {
				// Retrieve the first station
				if (!oStationJSONArray.isNull(i)) {
					m_oStation = new PosStation(oStationJSONArray.optJSONObject(i));
					m_sTaiwanGUIType = PosTaiwanGuiConfig.TYPE_NORMAL;
					bHaveStation = true;
					break;
				}
			}
		}
		
		return bHaveStation;
	}
	
	public boolean loadFirstPortalStation(int iOutletId) {
		m_sErrorMessage = "";
		
		// OM (pos_stations)
		boolean bHaveStation = false;
		m_oStation = new PosStation();
		JSONArray oStationJSONArray = m_oStation.getStationByOutletIdDeviceKey(iOutletId, PosStationDevice.KEY_PORTAL_STATION);
		if (oStationJSONArray != null) {
			for (int i = 0; i < oStationJSONArray.length(); i++) {
				// Retrieve the first station
				if (!oStationJSONArray.isNull(i)) {
					m_oStation = new PosStation(oStationJSONArray.optJSONObject(i));
					m_sTaiwanGUIType = PosTaiwanGuiConfig.TYPE_NORMAL;
					bHaveStation = true;
					break;
				}
			}
		}
		
		return bHaveStation;
	}
	
	public boolean loadTaiwanGuiConfig(String sDate, int iOutletId) {
		boolean bResult = false;
		
		m_sErrorMessage = "";
		
		m_oPosTaiwanGuiConfigs = new PosTaiwanGuiConfigList();
		if(m_oStation.getParams().optJSONObject("tgui").optString("generate_by").equals(PosTaiwanGuiConfig.GENERATED_BY_STATION))
			m_oPosTaiwanGuiConfigs.readAllByDateAndStation(m_oStation.getStatId());
		else{
			//try to activate the Taiwan GUI config
			PosTaiwanGuiConfig oPosTaiwanGuiConfig = new PosTaiwanGuiConfig();
			oPosTaiwanGuiConfig.activeTaiwanGuiConfigByOutletAndDate(AppGlobal.g_oFuncOutlet.get().getOutlet().getOletId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInStringWithFormat("yyyy-MM-dd"));
			
			if(iOutletId == 0)
				m_oPosTaiwanGuiConfigs.readAllByDateAndOutlet(sDate, m_oStation.getOletId());
			else
				m_oPosTaiwanGuiConfigs.readAllByDateAndOutlet(sDate, iOutletId);
		}
		
		if(m_oPosTaiwanGuiConfigs.getConfigList().isEmpty())
			// Load configuration error
			m_sErrorMessage = AppGlobal.g_oLang.get()._("missing_taiwan_gui_configuration");
		else{
			m_sTaiwanGUIType = PosTaiwanGuiConfig.TYPE_NORMAL;
			bResult = true;
		}
		
		return bResult;
	}
	
	//load all users
	public void loadAllUserList(){
		m_oUserUserList = new UserUserList();
		m_oUserUserList.getAllUsersNameAndId();
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
	public boolean checkToggleTaiwanGuiMode() {
		if (m_oPosTaiwanGuiConfigs.getConfigList().size() <= 1)
			return false;
		else
			return true;
	}
	
	//check TaiwanGui GeneratedMode
	public String getTaiwanGuiGeneratedMode(){
		if (!supportTaiwanGui() || !m_oStation.getParams().has("tgui"))
			return null;
		
		JSONObject oJsonObject = m_oStation.getParams().optJSONObject("tgui");
		if(oJsonObject != null && oJsonObject.has("mode"))
			return oJsonObject.optString("mode");
		else
			return null;
	}

	public String getTaiwanGuiGeneratedBy() {
		if (!supportTaiwanGui())
			return null;
		
		JSONObject oJsonObject = m_oStation.getParams().optJSONObject("tgui");
		if(oJsonObject != null && oJsonObject.has("generate_by"))
			return oJsonObject.optString("generate_by");
		else
			return null;
	}

	public String getTaiwanGuiNumPrefix() {
		if(!supportTaiwanGui())
			return "";
		
		if (m_sTaiwanGUIType.equals(PosTaiwanGuiConfig.TYPE_SPECIAL))
			return m_oPosTaiwanGuiConfigs.getConfigByType(m_sTaiwanGUIType).getPrefix();
		else
			return m_oPosTaiwanGuiConfigs.getConfigByType(PosTaiwanGuiConfig.TYPE_NORMAL).getPrefix();
	}
	
	public int getTaiwanGuiConfigId() {
		if(!supportTaiwanGui())
			return 0;
		
		if (m_sTaiwanGUIType.equals(PosTaiwanGuiConfig.TYPE_SPECIAL))
			return m_oPosTaiwanGuiConfigs.getConfigByType(m_sTaiwanGUIType).getTaiwanGuiConfigId();
		else
			return m_oPosTaiwanGuiConfigs.getConfigByType(PosTaiwanGuiConfig.TYPE_NORMAL).getTaiwanGuiConfigId();
	}
	
	public int getTaiwanGuiStartNum() {
		if(!supportTaiwanGui())
			return 0;
		
		if (m_sTaiwanGUIType.equals(PosTaiwanGuiConfig.TYPE_SPECIAL))
			return m_oPosTaiwanGuiConfigs.getConfigByType(m_sTaiwanGUIType).getStartNum();
		else
			return m_oPosTaiwanGuiConfigs.getConfigByType(PosTaiwanGuiConfig.TYPE_NORMAL).getStartNum();
	}
	
	public int getTaiwanGuiEndNum() {
		if(!supportTaiwanGui())
			return 0;
		
		if (m_sTaiwanGUIType.equals(PosTaiwanGuiConfig.TYPE_SPECIAL))
			return m_oPosTaiwanGuiConfigs.getConfigByType(m_sTaiwanGUIType).getEndNum();
		else
			return m_oPosTaiwanGuiConfigs.getConfigByType(PosTaiwanGuiConfig.TYPE_NORMAL).getEndNum();
	}
	
	public int getTaiwanGUIWarningLimit() {
		if(!supportTaiwanGui())
			return 0;
		
		if (m_sTaiwanGUIType.equals(PosTaiwanGuiConfig.TYPE_SPECIAL))
			return m_oPosTaiwanGuiConfigs.getConfigByType(m_sTaiwanGUIType).getWarningLimit();
		else
			return m_oPosTaiwanGuiConfigs.getConfigByType(PosTaiwanGuiConfig.TYPE_NORMAL).getWarningLimit();
	}
	
	public int getTaiwanGuiNormalTaxIndex() {
		if (!supportTaiwanGui())
			return 0;
		
		JSONObject oJsonObject = m_oStation.getParams().optJSONObject("tgui");
		if (oJsonObject != null && oJsonObject.has("normal_tax_index"))
			return oJsonObject.optInt("normal_tax_index");
		
		return 0;
	}
	
	public int getTaiwanGuiEntertainmentTaxIndex() {
		if (!supportTaiwanGui())
			return 0;
		
		JSONObject oJsonObject = m_oStation.getParams().optJSONObject("tgui");
		if (oJsonObject != null && oJsonObject.has("ent_tax_index"))
			return oJsonObject.optInt("ent_tax_index");
		
		return 0;
	}
	
	public int getTaiWanGuiPrintFormatId() {
		if(!supportTaiwanGui())
			return 0;
		
		JSONObject oJsonObject = m_oStation.getParams().optJSONObject("tgui");
		if (oJsonObject != null && oJsonObject.has("pfmt_id"))
			return oJsonObject.optInt("pfmt_id");
		
		return 0;
	}
	
	public int getTaiWanGuiPrintQueueId() {
		if(!supportTaiwanGui())
			return 0;
		
		JSONObject oJsonObject = m_oStation.getParams().optJSONObject("tgui");
		if (oJsonObject != null && oJsonObject.has("prtq_id"))
			return oJsonObject.optInt("prtq_id");
		
		return 0;
	}
	
	// Set Taiwan GUI type
	public void setTaiwanGuiType(String sTaiwanGuiType) {
		m_sTaiwanGUIType = sTaiwanGuiType;
	}
	
	// Get Taiwan GUI type
	public String getTaiwanGuiType() {
		return m_sTaiwanGUIType;
	}
	
	public void setUnderOrdering(boolean bUnderOrdering){
		m_bUnderOrdering = bUnderOrdering;
	}
	
	public boolean getUnderOrdering(){
		return m_bUnderOrdering;
	}
	
	// Get Default Display Panel Page Id
	public int getDefaultDisplayPanelPageId() {
		if(m_oStation.getParams() == null)
			return 0;
		
		JSONObject oJSONObject = m_oStation.getParams().optJSONObject("display_panel");
		if (oJSONObject != null && oJSONObject.has("display_panel_page_id"))
			return oJSONObject.optInt("display_panel_page_id");
		
		return 0;
	}
	
	public int getAskTableWithAdvanceMode(){
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "ask_table_with_advance_mode");
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

	public int getReprintGuestCheckTimes() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "reprint_guest_check_times");
		int iReprintGuestCheckTimes = -1;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try{
					iReprintGuestCheckTimes = Integer.parseInt(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}else
				iReprintGuestCheckTimes = 0;
		}
		return iReprintGuestCheckTimes;
	}
	
	public int getReprintReceiptTimes(){
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "reprint_receipt_times");
		int iReprintReceiptTimes = -1;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try{
					iReprintReceiptTimes = Integer.parseInt(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}else
				iReprintReceiptTimes = 0;
		}
		return iReprintReceiptTimes;
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

	public int getApplyDiscountRestriction() {
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

	public int getDoubleCheckDiscountAlert() {
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

	public int getOrderingModeForAutoCloseCashierPanel() {
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

	public Integer getBusinessHourWarnLevel() {
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

	public int getOrderingTimeout() {
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
	
	// get screen saver option
	public JSONObject getScreenSaverOption() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "screen_saver_option");
		JSONObject oValueJsonObject = null;
		
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try {
					oValueJsonObject = new JSONObject(oPosConfig.getValue());
				} catch (Exception e) {
					// Incorrect format
					oValueJsonObject = null;
				}
			}
		}
		return oValueJsonObject;
	}
	
	// get screen saver timeout
	public int getScreenSaverTimeout() {
		JSONObject oValueJSONObject = getScreenSaverOption();
		int iTimeout = 0;
		if (oValueJSONObject != null && oValueJSONObject.has("timeout"))
			iTimeout = oValueJSONObject.optInt("timeout");

		return iTimeout;
	}
	
	// get screen saver color
	public String getScreenSaverColor() {
		JSONObject oValueJSONObject = getScreenSaverOption();
		String sColor = "#FF000000", tmpValue = "";
		if (oValueJSONObject != null && oValueJSONObject.has("transparency"))
			tmpValue = oValueJSONObject.optString("transparency", "FF");
		if (oValueJSONObject != null && oValueJSONObject.has("color"))
			tmpValue += oValueJSONObject.optString("color", "000000");
		if (tmpValue.length() == 8)
			sColor = "#" + tmpValue;
		
		return sColor;
	}
	
	// Check show image or not
	public boolean getScreenSaverImage() {
		JSONObject oValueJSONObject = getScreenSaverOption();
		if (oValueJSONObject != null && oValueJSONObject.has("display_content")) {
			if(oValueJSONObject.optString("display_content", "c").equals("m"))
				return true;
		}
		
		return false;
	}
	
	public int getOpenTableScreenModeConfig() {
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

	public boolean getOrderingPanelInputNumpad() {
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

	public boolean getNotCheckStock() {
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

	public boolean getOrderingPanelShowPrice() {
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

	public boolean getFastFoodAutoTakeout() {
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

	public boolean getFastFoodNotAutoWaiveSerCharge() {
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

	public boolean getSupportNumericPluOnly() {
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
	
	public String getNotPrintReceipt(AppGlobal.OPERATION_MODE eOperationMode){
		String sSetupName = "fine_dining_not_print_receipt";
		if(eOperationMode.equals(AppGlobal.OPERATION_MODE.fast_food))
			sSetupName = "fast_food_not_print_receipt";
		String sValue = this.getPosConfigStringValue("system", sSetupName, "false");
		if(sValue.equals("y"))
			sValue = "true";
		else if (sValue.isEmpty())
			sValue = "false";
		return sValue;
	}

	public boolean getCalcInclusiveTaxRefByCheckTotal() {
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

	public String getCheckInfoSelfDefineDesc() {
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

	public String getVoidReasonForPaymentAutoDiscount() {
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

	public String getBarcodeOrderingFormat() {
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

	// *** For version 0.0.0.45, the feature of interface_url is no longer supported
	public String getInterfaceUrl(){
		/*
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
		*/
		return null;
	}

	public String getTenderAmount() {
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

	public int getCoverUpperBound() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "cover_limit");
		int iValue = 0;
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				// In old version, the cover_limit is only stored as integer
				iValue = Integer.parseInt(sValue);
			} catch (Exception e) {
				// If not integer value, try JSON object
				try {
					JSONObject oTempJSONObject = new JSONObject(sValue);
					if (oTempJSONObject.has("upper_bound"))
						iValue = oTempJSONObject.optInt("upper_bound", 0);
				} catch (JSONException e2) {
					// Incorrect format
				}
			}
		}
		return iValue;
	}
	
	public int getCoverWarningLimit() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "cover_limit");
		int iValue = 0;
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			try {
				JSONObject oTempJSONObject = new JSONObject(oPosConfig.getValue());
				if (oTempJSONObject.has("warning"))
					iValue = oTempJSONObject.optInt("warning", 0);
			} catch (JSONException e) {
				// Incorrect format
			}
		}
		return iValue;
	}

	public int getOrderingTimeoutOption() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "ordering_timeout_option");
		int iValue = 0;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try {
					iValue = Integer.parseInt(oPosConfig.getValue());
				} catch (Exception e) {
					// Incorrect format
				}
			}
		}
		return iValue;
	}

	public int getItemStockInputMode(){
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "item_stock_operation_input_mode");
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
	
	public boolean getOrderingPanelNotShowImage() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "ordering_panel_not_show_image");
		Boolean bValue = false;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bValue;
	}

	public boolean getSelfKioskSetMenuNoGuidance(){
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "self_kiosk_set_menu_no_gudiance");
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

	public String getCommonLookupButtonNumber() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "common_lookup_button_number");
		String sCommonButtonNumber = null;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					sCommonButtonNumber = oPosConfig.getValue();
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return sCommonButtonNumber;
	}

	public String getMenuLookupButtonNumber() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "menu_lookup_button_number");
		String sCommonButtonNumber = null;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					sCommonButtonNumber = oPosConfig.getValue();
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return sCommonButtonNumber;
	}

	public String getSetMenuButtonNumber() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "set_menu_button_number");
		String sCommonButtonNumber = null;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					sCommonButtonNumber = oPosConfig.getValue();
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return sCommonButtonNumber;
	}
	
	public boolean isPaymentSkipTips(String sPaymentCode) {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "skip_tips_payment_code");
		boolean bFound = false;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					String sValue = oPosConfig.getValue();
					String[] sSplits = sValue.split(",");
					for (int i=0; i<sSplits.length; i++) {
						if (sSplits[i].equals(sPaymentCode)) {
							bFound = true;
							break;
						}
					}
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bFound;
	}

	public boolean getNotAllowOpenNewCheck(){
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "not_allow_open_new_check");
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

	public boolean isSupportLoyaltyMember(){
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "loyalty_member");
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
	
	public boolean isSkipPrintCheckForPayment() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "skip_print_check_for_payment");
		Boolean bValue = false;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bValue;
	}
	
	public int getDutyMealOnCreditLimit(String sLimitType) {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", sLimitType);
		int iValue = 0;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null) {
				try{
					iValue = Integer.parseInt(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		} else
			return -1;
		
		return iValue;
	}
	
	public String getDutyMealLimitPeriod() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "dutymeal_limit_reset_period");
		String sValue = "m";
		
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null) {
				try{
					sValue = oPosConfig.getValue();
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		
		return sValue;
	}
	
	public boolean isVoidGuestCheckImage() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "void_guest_check_image");
		Boolean bValue = false;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bValue;
	}
	
	public int getTableModeRowColumn(String sType){
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "table_mode_row_and_column");
		int iRow = 4;
		int iColumn = 7;
		
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					String[] sParts = oPosConfig.getValue().split(":");
					if(sParts[0] != null) {
						if(Integer.parseInt(sParts[0]) != 0 && Integer.parseInt(sParts[0]) <= 10)
							iRow = Integer.parseInt(sParts[0]);
					}
					
					if(sParts[1] != null) {
						if(Integer.parseInt(sParts[1]) != 0 && Integer.parseInt(sParts[1]) <= 20)
							iColumn = Integer.parseInt(sParts[1]);
					}
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		
		if(sType.equals("row"))
			return iRow;
		else
			return iColumn;
	}
	
	public boolean isSupportTableSection(){
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "ask_table_section");
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
	
	public boolean isAllowChangeItemQuantityOnOldItem() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "allow_change_item_quantity_after_send");
		Boolean bChangeValue = true;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try{
					bChangeValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bChangeValue;
	}
	
	public boolean isEnlargeOrderingBasket(){
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "enlarge_ordering_basket");
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

	public boolean getNotAllowToOrderWhenZeroStock() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "not_allow_to_order_when_zero_stock");
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
	
	public boolean getSplitTableWithKeepingCover() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "split_table_with_keeping_cover");
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
	
	public boolean isAllowTurnOffTestingPrint() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "turn_off_testing_printer");
		Boolean bValue = false;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bValue;
	}
	
	public boolean isAllowShowTableSize() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "show_table_size");
		Boolean bValue = false;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bValue;
	}
	
	public String getNonTaiwanGuiItemDeptGroup() {
		if (!supportTaiwanGui())
			return "";
		
		JSONObject oJsonObject = m_oStation.getParams().optJSONObject("tgui");
		if (oJsonObject != null && oJsonObject.has("paid_out_dept_grp_ids"))
			return oJsonObject.optString("paid_out_dept_grp_ids");
		
		return "";
	}

	public boolean isShowTableFloorAfterSwitchUser() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "show_floor_plan_after_switch_user");
		Boolean bValue = false;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		
		return bValue;
	}
	
	public JSONArray getPaymentCheckTypes() {
		JSONArray oJSONArray = null;
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "payment_check_types");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				JSONObject oTempJSONObject = new JSONObject(sValue);
				if (oTempJSONObject.has("mapping"))
					oJSONArray = new JSONArray(oTempJSONObject.optString("mapping"));
			} catch (JSONException e) {
				// Incorrect format
			}
		}
		
		return oJSONArray;
	}
	
	public boolean includePreviousSameLevelDiscount() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "include_previous_same_level_discount");
		Boolean bValue = false;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		
		return bValue;
	}
	
	public boolean loadAllTaiwanGuiConfigByStation() {
		boolean bResult = true;
		m_sErrorMessage = "";
		
		m_oAllPosTaiwanGuiConfigs = new PosTaiwanGuiConfigList();
		m_oAllPosTaiwanGuiConfigs.loadAllByStation(m_oStation.getStatId());
		
		if(m_oAllPosTaiwanGuiConfigs.getConfigList().isEmpty())
			bResult = false;
		
		return bResult;
	}
	
	//get user list
	public UserUserList getUserUserList(){
		return m_oUserUserList;
	}
	
	public HashMap<String, String> changeTaiwanGUIConfig(String sWsInterface, String sModule, String sFcnName, String sParam) {
		PosTaiwanGuiConfig m_oPosTaiwanGuiConfig= new PosTaiwanGuiConfig();
		String sResult = m_oPosTaiwanGuiConfig.changeTaiwanGUIConfig(sWsInterface, sModule, sFcnName, sParam);
		HashMap<String, String> oResultHashMap = new HashMap<String, String>();
		if(sResult.length() > 0) {
			if(sResult.equals("fail_to_load_components") || sResult.equals("only_one_active_config_is_allowed") || sResult.equals("prefix_must_be_composed_of_2_characters") || sResult.equals("start_number_must_be_smaller_than_end_number") || sResult.equals("only_one_active_config_is_allowed") || sResult.equals("number_range_is_overlapped_with_another") || sResult.equals("fail_to_save_taiwan_gui_config_in_master_server")
					|| sResult.equals("missing_twcf_olet_id") || sResult.equals("missing_twcf_stat_id") || sResult.equals("missing_twcf_type") || sResult.equals("missing_twcf_prefix") || sResult.equals("missing_twcf_start_num") || sResult.equals("missing_twcf_end_num") || sResult.equals("missing_twcf_start_date") || sResult.equals("missing_twcf_end_date") || sResult.equals("missing_twcf_warning_limit") || sResult.equals("missing_twcf_status") 
					|| sResult.equals("invalid_twcf_olet_id") || sResult.equals("invalid_twcf_stat_id") || sResult.equals("invalid_twcf_type") || sResult.equals("invalid_twcf_prefix") || sResult.equals("invalid_twcf_start_num") || sResult.equals("invalid_twcf_end_num") || sResult.equals("invalid_twcf_start_date")|| sResult.equals("invalid_twcf_end_date") || sResult.equals("invalid_twcf_warning_limit") || sResult.equals("invalid_twcf_status")
					|| sResult.equals("start_num_must_be_larger_than_end_num") || sResult.equals("start_date_must_be_larger_than_end_date") || sResult.equals("record_not_exist") || sResult.equals("fail_to_login_master_server") || sResult.equals("fail_to_save_taiwan_gui_config"))
			{
				oResultHashMap.put("result", "false");
				switch(sResult) {
					case "fail_to_load_components":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("fail_to_load_components"));
						break;
					case "only_one_active_config_is_allowed":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("only_one_active_config_is_allowed"));
						break;
					case "prefix_must_be_composed_of_2_characters":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("prefix_must_be_composed_of_2_characters"));
						break;
					case "start_number_must_be_smaller_than_end_number":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("start_number_must_be_smaller_than_end_number"));
						break;
					case "number_range_is_overlapped_with_another":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("number_range_is_overlapped_with_another"));
						break;
					case "fail_to_save_taiwan_gui_config_in_master_server":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("fail_to_save_taiwan_gui_config_in_master_server"));
						break;
					case "missing_twcf_prefix":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("missing_prefix"));
						break;
					case "missing_twcf_start_num":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("missing_start_num"));
						break;
					case "missing_twcf_end_num":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("missing_end_num"));
						break;
					case "missing_twcf_warning_limit":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("missing_warning_limit"));
						break;
					case "invalid_twcf_prefix":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("invalid_prefix"));
						break;
					case "invalid_twcf_start_num":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("invalid_start_num"));
						break;
					case "invalid_twcf_end_num":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("invalid_end_num"));
						break;
					case "invalid_twcf_start_date":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("invalid_start_date"));
						break;
					case "invalid_twcf_end_date":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("invalid_end_date"));
						break;
					case "invalid_twcf_warning_limit":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("invalid_warning_limit"));
						break;
					case "start_num_must_be_larger_than_end_num":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("start_num_must_be_larger_than_end_num"));
						break;
					case "start_date_must_be_larger_than_end_date":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("start_date_must_be_larger_than_end_date"));
						break;
					case "record_not_exist":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("record_not_exist"));
						break;
					case "fail_to_login_master_server":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("fail_to_login_master_server"));
						break;
					case "fail_to_save_taiwan_gui_config":
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("fail_to_save_taiwan_gui_config"));
						break;
					default:
						oResultHashMap.put("message", AppGlobal.g_oLang.get()._("internal_error"));
						break;
				}
					
				return oResultHashMap;
			}
			else
			{
				oResultHashMap.put("result", "t");
				oResultHashMap.put("message", sResult);	
				return oResultHashMap;
			}
		}
		
		oResultHashMap.put("result", "false");
		oResultHashMap.put("message", "no_result");	
		return oResultHashMap;

	}
	
	public PosTaiwanGuiConfigList getAllPosTaiwanGuiConfigs() {
		return m_oAllPosTaiwanGuiConfigs;
	}

	public void removeConfigByType(String sType) {
		m_oPosTaiwanGuiConfigs.removeConfigByType(sType);
	}
	
	public void addTaiwanGuiConfig(PosTaiwanGuiConfig oPosTaiwanGuiConfig) {
		m_oPosTaiwanGuiConfigs.addConfig(oPosTaiwanGuiConfig);
	}

	public boolean isTaiwanGuiConfigExist() {
		int iConfigId = 0;
		if(!supportTaiwanGui())
			return false;
		
		if(m_sTaiwanGUIType.equals(PosTaiwanGuiConfig.TYPE_HAVE_GUI) || m_sTaiwanGUIType.equals(PosTaiwanGuiConfig.TYPE_NO_TAX) 
				|| m_sTaiwanGUIType.equals(PosTaiwanGuiConfig.TYPE_WAIVE_TAX) || m_sTaiwanGUIType.equals(PosTaiwanGuiConfig.TYPE_CHARITY))
			iConfigId = m_oPosTaiwanGuiConfigs.getConfigByType(PosTaiwanGuiConfig.TYPE_NORMAL).getTaiwanGuiConfigId();
		else 
			iConfigId = m_oPosTaiwanGuiConfigs.getConfigByType(m_sTaiwanGUIType).getTaiwanGuiConfigId();
			
		if(iConfigId == 0)
			return false;
		else
			return true;
	}
	
	//Login loyalty
	public boolean loyaltyLogin(String sInterfaceId){
		boolean bValue = false;
		FuncLoyalty oFuncLoyalty = new FuncLoyalty();
		String sSessionId = oFuncLoyalty.login(sInterfaceId);
		if(sSessionId != null) {
			m_sLoyaltySessionId = sSessionId;
			bValue = true;
		}
		
		return bValue;
	}
	
	//Login loyalty SVC
	public boolean loyaltySvcLogin(String sInterfaceId){
		boolean bValue = false;
		FuncLoyaltySvc oFuncSVC = new FuncLoyaltySvc();
		String sSessionId = oFuncSVC.login(sInterfaceId);
		if(sSessionId != null) {
			m_sLoyaltySvcSessionId = sSessionId;
			bValue = true;
		}
		
		return bValue;
	}
	
	// get Loyalty session ID
	public String getLoyaltySessionId(){
		return m_sLoyaltySessionId;
	}
	
	//get Loyalty SVC session ID
	public String getLoyaltySvcSessionId(){
		return m_sLoyaltySvcSessionId;
	}
	
	//set Loyalty SVC session ID
	public void setLoyaltySvcSessionId(String sSessionId){
		m_sLoyaltySvcSessionId = sSessionId;
	}
	
	// check whether support payment interface
	static boolean isSupportPortalInterface() {
		if(AppGlobal.isModuleSupport(AppGlobal.OPTIONAL_MODULE.pos_interface.name()) == false)
			return false;
		if(AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PORTAL_INTERFACE) == null || AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PORTAL_INTERFACE).isEmpty())
			return false;
		
		return true;
	}
	
	//check whether validate member discount
	public boolean isValidateMemberDiscount() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "member_discount_not_validate_member_module");
		boolean bValue = false;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		
		return bValue;
	}
	
	//check whether reprint receipt at adjust payment
	public boolean isAdjustPaymentReprintReceipt() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "adjust_payments_reprint_receipt");
		boolean bValue = false;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bValue;
	}
	
	//check whether reprint receipt at adjust tips
	public boolean isAdjustTipsReprintReceipt() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "adjust_tips_reprint_receipt");
		boolean bValue = false;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return bValue;
	}
	
	//check whether is portal station
	public boolean isPortalStation() {
		return m_oStationDevice.isPortalStation();
	}
	
	//check whether is auto cashier payment
	public boolean isAutoCashierPayment(){
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "enable_autopayment_by_default_payment");
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
	
	//get generate receipt with PDF format setup
	public JSONObject getGenerateReceiptPdfConfigure() {
		JSONObject oTempJSONObject = null;
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "generate_receipt_pdf");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				 oTempJSONObject = new JSONObject(sValue);
			} catch (JSONException e) {
				// Incorrect format
			}
		}
		return oTempJSONObject;
	}
	
	//get export E-Journal setup
	public JSONObject getExportEJournalConfigure() {
		JSONObject oTempJSONObject = null;
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "export_e_journal");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				 oTempJSONObject = new JSONObject(sValue);
			} catch (JSONException e) {
				// Incorrect format
			}
		}
		return oTempJSONObject;
	}
	
	//get settlement count
	public int getSettlementCount(){
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "settlement_count_interval_to_print_guest_questionnaire");
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					JSONObject oJsonObject = new JSONObject(oPosConfig.getValue());
					if (oJsonObject != null && oJsonObject.has("interval_count"))
						return oJsonObject.optInt("interval_count", 0);
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return 0;
	}
	
	//get mandatory key for table attribute setup
	public String[] getTableAttributeMandatoryKey(){
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "table_attribute_mandatory_key");
		String sValue[] = new String[10];
		
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					JSONObject oJsonObject = new JSONObject(oPosConfig.getValue());
					if (oJsonObject != null){
						for (int i = 1; i <= 10; i++){
							if (oJsonObject.has("key"+i))
								sValue[i-1] = oJsonObject.optString("key"+i, "");
						}
					}
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return sValue;
	}
	
	//get auto function for new check setup
	public JSONArray getCheckAutoFunctions() {
		JSONArray oJSONArray = null;
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "new_check_auto_functions");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				oJSONArray = new JSONArray(sValue);
			} catch (JSONException e) {
				try {
					JSONObject oJsonObject = new JSONObject(sValue);
					if(oJsonObject.has("function"))
						oJSONArray = oJsonObject.optJSONArray("function");
				} catch (JSONException e1) {
					// Incorrect format
				}
			}
		}
		return oJSONArray;
	}
	
	public boolean getCheckAutoFunctionsOption() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "new_check_auto_functions");
		Boolean bValue = false;
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				JSONObject oJSONObject = new JSONObject(sValue);
				if(oJSONObject.has("support_for_split_check") && oJSONObject.get("support_for_split_check").equals("y"))
					bValue = true;
			} catch (JSONException e) {
				// Incorrect format
			}
		}
		return bValue;
	}
	
	//get Ordering Basket Extra Info for setup
	public ArrayList<JSONObject> getOrderingBasketExtraInfo() {
		JSONArray oJSONArray = null;
		JSONObject oMap = null;
		ArrayList<JSONObject> oList = new ArrayList<JSONObject>();
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "display_check_extra_info_in_ordering_basket");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				JSONObject oJSONObject = new JSONObject(sValue);
				if(oJSONObject.has("check_extra_info_list")) {
					oJSONArray = oJSONObject.optJSONArray("check_extra_info_list");
					for (int i = 0; i < oJSONArray.length(); i++) {
						oMap = oJSONArray.optJSONObject(i);
						oList.add(oMap);
					}
					// sorting the target function in sequence
					Collections.sort(oList, new Comparator<JSONObject>() {
						public int compare(JSONObject oList1, JSONObject oList2) {
							return oList1.optInt("seq") - oList2.optInt("seq");
						}
					});
				}
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		return oList;
	}
	
	public boolean isAlwaysResetOrderingBasketExtraInfoWindowSize() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "display_check_extra_info_in_ordering_basket");
		boolean bValue = false;
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				JSONObject oJSONObject = new JSONObject(sValue);
				if(oJSONObject.has("always_reset_extra_info_window_size") && oJSONObject.get("always_reset_extra_info_window_size").equals("y"))
					bValue = true;
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		return bValue;
	}
	
	//check whether showing page up / down for listing
	public boolean isShowPageUpAndDownButtonForList() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "show_page_up_and_down_button_for_list");
		Boolean bValue = false;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		
		return bValue;
	}
	
	//whether ask quantity during apply discount
	public boolean isAskQuantityDuringApplyDiscount() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "ask_quantity_during_apply_discount");
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
	
	//get member validation setting
	public String getMemberValidationSetting(){
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "member_validation_setting");
		String sValue = "";
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
	
	public JSONObject getPrintCheckControl() {
		JSONObject oTempJSONObject = null;
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "print_check_control");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				 oTempJSONObject = new JSONObject(sValue);
			} catch (JSONException e) {
				// Incorrect format
			}
		}
		return oTempJSONObject;
	}
	
	public int getDefaultCover() {
		JSONObject oTableValidationSetting = this.getTableValidationSetting();
		if (oTableValidationSetting == null)
			return -1;
		
		int iDefaultCover = oTableValidationSetting.optInt("default_cover", -1);
		return iDefaultCover;
	}

	public String getTableValidationMsrCode() {
		JSONObject oTableValidationSetting = this.getTableValidationSetting();
		if (oTableValidationSetting == null)
			return "";
		
		String sMsrCode = oTableValidationSetting.optString("msr_interface_code", "");
		return sMsrCode;
	}
	
	private JSONObject getTableValidationSetting() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "table_validation_setting");
		if (oPosConfig == null)
			return null;
		if (oPosConfig.getValue() == null)
			return null;
		JSONObject oJSONObject;
		try {
			oJSONObject = new JSONObject(oPosConfig.getValue());
		} 
		catch (JSONException e) {
			AppGlobal.stack2Log(e);
			return null;
		}
		if (!oJSONObject.optString("support").equals("y"))
			return null;
		return oJSONObject;
	}
	
	public BigDecimal getTableValidationTableLimit() {
		JSONObject oTableValidationSetting = this.getTableValidationSetting();
		if (oTableValidationSetting == null)
			return BigDecimal.ZERO;
		
		String sMaxCheckTotal = oTableValidationSetting.optString("maximum_check_total_for_all_tables", "0");
		return new BigDecimal(sMaxCheckTotal);
	}

	// use to split the table extension and table number
	public HashMap<String, String> getTableNoAndTableExtension(String sTableNo) {
		String sTableExtension = "";
		HashMap<String, String> oResultMap = new HashMap<String, String>();
		int iAlphaIndex = -1;
		for (int iTableNo = 0; iTableNo < sTableNo.length(); iTableNo++) {
			char cCurrentChar = sTableNo.charAt(iTableNo);
			if (Character.isLetter(cCurrentChar)) {
				iAlphaIndex = iTableNo;
				break;
			}
		}
		if (iAlphaIndex != -1) {
			sTableExtension = sTableNo.substring(iAlphaIndex);
			sTableNo = sTableNo.substring(0, iAlphaIndex);
			if (sTableExtension.length() > 5)
				sTableExtension = sTableExtension.substring(0, 5);
		}
		if (sTableNo.length() > 9)
			sTableNo = sTableNo.substring(0, 9);
		else if (sTableNo.isEmpty() && !sTableExtension.isEmpty())
			sTableNo = "0";
		oResultMap.put("tableNo", sTableNo);
		oResultMap.put("tableExt", sTableExtension);
		return oResultMap;
	}
	
	//get order ownership setting
	public JSONObject getCheckOwnership() {
		JSONObject oTempJSONObject = null;
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "set_order_ownership");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				 oTempJSONObject = new JSONObject(sValue);
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		return oTempJSONObject;
	}
	
	//get advance order setting
	public JSONObject getAdvancedOrderSetting() {
		JSONObject oTempJSONObject = null;
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "advance_order_setting");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				 oTempJSONObject = new JSONObject(sValue);
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		return oTempJSONObject;
	}
	
	public int getDrawerLimitOwnedByUser(){
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "number_of_drawer_owned_by_user");
		int iNumberOfDrawerOwnedByUser = 0;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try{
					iNumberOfDrawerOwnedByUser = Integer.parseInt(oPosConfig.getValue());
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		return iNumberOfDrawerOwnedByUser;
	}
	
	public boolean getCheckDrawerOwnership() {
		boolean bResult = false;
		JSONObject oTempJSONObject = null;
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "payment_checking");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				oTempJSONObject = new JSONObject(sValue);
				if (oTempJSONObject.has("support") && oTempJSONObject.optString("support").equals("y")
						&& oTempJSONObject.has("check_drawer_ownership"))
					if (oTempJSONObject.optString("check_drawer_ownership").equals("y"))
						bResult = true;
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		return bResult;
	}
	
	public boolean getSupportContinuousPrinting(){
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "support_continuous_printing");
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
	
	public JSONObject getForceDailyClose() {
		JSONObject oTempJSONObject = null;
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "force_daily_close");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				 oTempJSONObject = new JSONObject(sValue);
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		return oTempJSONObject;
	}
	
	// Get cashier settlement mode
	// empty - by user, o - outlet
	public String getCashierSettlementMode() {
		String sValue = "";
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "cashier_settlement_mode");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			try{
				sValue = oPosConfig.getValue();
			}catch(Exception e){
				// Incorrect format
			}
		}
		return sValue;
	}
	
	//whether allow apply mixed payment
	public boolean isAllowMixedPayment() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "support_mixed_revenue_non_revenue_payment");
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
	
	public String getHeroStyle(){
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "hero_style");
		String sValue = null;
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null){
				try{
					sValue = oPosConfig.getValue();
				}catch(Exception e){
					e.printStackTrace();
					// Incorrect format
				}
			}
		}
		return sValue;
	}
	
	//get auto function for pay check setup
	public JSONArray getPayCheckAutoFunctions() {
		JSONArray oJSONArray = null;
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "pay_check_auto_functions");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				oJSONArray = new JSONArray(sValue);
			} catch (JSONException e) {
				// Incorrect format
			}
		}
		return oJSONArray;
	}
	
	//get auto function for pay check setup
	public JSONArray getItemFunctionList() {
		JSONArray oJSONArray = null;
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "item_function_list");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
					oJSONArray = new JSONArray(sValue);
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		return oJSONArray;
	}
	
	//get table floor plan setting
	public JSONObject getTableFloorPlanSetting() {
		JSONObject oTempJSONObject = null;
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "table_floor_plan_setting");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				oTempJSONObject = new JSONObject(sValue);
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		return oTempJSONObject;
	}
	
	//get support table status cleaning
	public boolean getSupportTableStatusCleaning() {
		boolean bIsSuportTableStatusCleaning = false;
		
		JSONObject oTempJSONObject = this.getTableFloorPlanSetting();
		if (oTempJSONObject != null && oTempJSONObject != null && oTempJSONObject.optString("support_table_status_cleaning").equals("y"))
			bIsSuportTableStatusCleaning = true;
		
		return bIsSuportTableStatusCleaning;
	}
	
	//get automatically change cleaning to vacant interval
	public int getAutomaticallyChangeCleaningToVacantInterval() {
		int iInterval = 0;
		
		JSONObject oTempJSONObject = this.getTableFloorPlanSetting();
		if (oTempJSONObject != null && oTempJSONObject != null && oTempJSONObject.optInt("automatically_change_cleaning_to_vacant_interval") > 0)
			iInterval = oTempJSONObject.optInt("automatically_change_cleaning_to_vacant_interval");
		
		return iInterval;
	}
	
	//get payment process setting
	public JSONObject getPaymentProcessSetting() {
		JSONObject oTempJSONObject = null;
		PosConfig oPosConfig = AppGlobal.getPosConfig("kiosk", "payment_process_setting");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				oTempJSONObject = new JSONObject(sValue);
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		return oTempJSONObject;
	}
	
	//get ordering basket item grouping method setting
	public String getGroupingMethodSetting() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("kiosk", "ordering_basket_item_grouping_method");
		String sValue = "l";
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			sValue = oPosConfig.getValue();
			try{
				sValue = oPosConfig.getValue();
			}catch(Exception e){
				// Incorrect format
			}
		}
		
		return sValue;
	}
	
	// get call number input method
	public HashMap<String, String> getCallNumberInputMethod() {
		HashMap<String, String> oResultMap = new HashMap<String, String>();
		JSONObject oTempJSONObject = null;
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "call_number_input_setting");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				oTempJSONObject = new JSONObject(sValue);
				oResultMap.put("method", oTempJSONObject.optString("method"));
				oResultMap.put("image_file_for_scan_mode", oTempJSONObject.optString("image_file_for_scan_mode"));
			} catch (JSONException e) {
				oResultMap.put("method", sValue);
				oResultMap.put("image_file_for_scan_mode", "");
				AppGlobal.stack2Log(e);
			}
		}
		else
			oResultMap = null;
		
		return oResultMap;
	}
	
	// get cover control for gratuity
	public int getCoverControlForGratuity(int iCover) {
		JSONObject oTempJSONObject = null;
		JSONArray oCoverControlJSONArray = null;
		int iMinCover = 0, iMaxCover = 0, iGratId = 0;
		
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "gratuity_setting");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				oTempJSONObject = new JSONObject(sValue);
				oCoverControlJSONArray = oTempJSONObject.getJSONArray("cover_control");
				for (int i = 0; i < oCoverControlJSONArray.length(); i++){
					JSONObject object = oCoverControlJSONArray.getJSONObject(i);
					iMinCover = object.optInt("min_cover", 0);
					iMaxCover = object.optInt("max_cover", 0);
					if ((iMinCover == 0 || iCover >= iMinCover) && (iMaxCover == 0 || iCover <= iMaxCover)) {
						iGratId = object.optInt("grat_id", 0);
						break;
					}
				}
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		return iGratId;
	}
	
	//whether remove check type for release payment
	public boolean isRemoveCheckTypeForReleasePayment() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "remove_check_type_for_release_payment");
		boolean bValue = true;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					AppGlobal.stack2Log(e);
				}
			}
		}
		return bValue;
	}
	
	public boolean isSkipAskCover() {
		JSONObject oTableValidationSetting = this.getTableValidationSetting();
		if(oTableValidationSetting != null && oTableValidationSetting.optString("skip_ask_cover", "").equals("y"))
			return true;
		return false;
	}
	
	//get duty meal / on credit limit period
	public String getDutyMealOnCreditLimitPeriod(boolean bDutyMeal) {
		String sConfigKey = "dutymeal_limit_reset_period";
		if(!bDutyMeal)
			sConfigKey = "on_credit_limit_reset_period";
		
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", sConfigKey);
		String sValue = "m";
		
		if(oPosConfig != null && oPosConfig.getValue() != null && !oPosConfig.getValue().isEmpty())
			sValue = oPosConfig.getValue();
		
		return sValue;
	}
	
	// get separate inclusive tax on display
	public boolean getSeparateInclusiveTaxOnDisplay() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "separate_inclusive_tax_on_display");
		boolean bValue = false;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try {
					JSONObject oJSONObject = new JSONObject(oPosConfig.getValue());
					if (oJSONObject.has("support_display_inclusive_tax_in_check")){
						if (oJSONObject.optString("support_display_inclusive_tax_in_check").equals("y"))
							bValue = true;
					}
				} catch (Exception e) {
					AppGlobal.stack2Log(e);
				}
			}
		}
		return bValue;
	}
		
	public boolean isHideCashierPanelNumpad() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("kiosk", "hide_cashier_panel_numpad");
		boolean bValue = false;
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
	
	// get to add tax in basket items
	public boolean IsDisplayTaxInItems() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "separate_inclusive_tax_on_display");
		boolean bValue = false;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try {
					JSONObject oJSONObject = new JSONObject(oPosConfig.getValue());
					if (oJSONObject.has("support_display_tax_in_items")){
						if (oJSONObject.optString("support_display_tax_in_items").equals("y"))
							bValue = true;
					}
				} catch (Exception e) {
					AppGlobal.stack2Log(e);
				}
			}
		}
		return bValue;
	}
	
	public boolean isDisplayAdminModeOnly() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("kiosk", "display_admin_mode_only");
		Boolean bValue = false;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try {
					bValue = Boolean.valueOf(oPosConfig.getValue());
				} catch (Exception e) {
					// Incorrect format
				}
			}
		}
		return bValue;
	}
	
	public boolean isHideCheckDetailBar() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("kiosk", "hide_check_detail_bar");
		Boolean bValue = false;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try {
					bValue = Boolean.valueOf(oPosConfig.getValue());
				} catch (Exception e) {
					// Incorrect format
				}
			}
		}
		return bValue;
	}
	
	public boolean isHideStationInfoBar() {
		return this.getPosConfigBooleanValue("kiosk", "hide_station_info_bar", false);
	}
	
	public boolean isHideAddWaiveScTaxInformation() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "hide_message_add_waive_tax_sc");
		boolean bValue = false;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try {
						bValue = Boolean.valueOf(oPosConfig.getValue());
				} catch (Exception e) {
					AppGlobal.stack2Log(e);
				}
			}
		}
		return bValue;
	}
	
	//whether re-sequence discount list, hide all unavailable buttons
	public boolean isResequenceDiscountList() {
		return this.getPosConfigBooleanValue("system", "resequence_discount_list", false);
	}
	
	//whether support partial payment
	public boolean isPartialPayment() {
		boolean bValue = false;
		JSONObject oTempJSONObject = this.getPosConfigJSONObjectValue("system", "support_partial_payment", null, false);
		if (oTempJSONObject != null && oTempJSONObject.has("support_partial_payment")) { 
			if(oTempJSONObject.optString("support_partial_payment").equals("y"))
				bValue = true;
		}else
			bValue = getPosConfigBooleanValue("system", "support_partial_payment", false); 
		return bValue;
	}
	
	//whether support continue payment after settling partial payment
	public boolean isContinueToPayAfterSettlingPartialPayment() {
		JSONObject oTempJSONObject = this.getPosConfigJSONObjectValue("system", "support_partial_payment", null, false);
		if (oTempJSONObject != null && oTempJSONObject.has("support_partial_payment") 
				&& oTempJSONObject.optString("support_partial_payment").equals("y")
				&& oTempJSONObject.has("continue_to_pay_after_settling_partial_payment")
				&& oTempJSONObject.optString("continue_to_pay_after_settling_partial_payment").equals("y"))
			return true;
		return false;
	}
	
	//whether partial payment support print receipt only after finish all payment
	public boolean isPartialPaymentPrintReceiptWhenFinishAllPayment() {
		JSONObject oTempJSONObject = this.getPosConfigJSONObjectValue("system", "support_partial_payment", null, false);
		if (oTempJSONObject != null && oTempJSONObject.has("support_partial_payment") 
				&& oTempJSONObject.optString("support_partial_payment").equals("y")
				&& oTempJSONObject.has("print_receipt_only_when_finish_all_payment")
				&& oTempJSONObject.optString("print_receipt_only_when_finish_all_payment").equals("y"))
			return true;
		return false;
	}
	
	//whether partial payment support to void all payment after release payment
	public boolean isPartialPaymentVoidAllPayment() {
		JSONObject oTempJSONObject = this.getPosConfigJSONObjectValue("system", "support_partial_payment", null, false);
		if (oTempJSONObject != null && oTempJSONObject.has("support_partial_payment")
				&& oTempJSONObject.optString("support_partial_payment").equals("y")
				&& oTempJSONObject.has("void_all_payment_after_release_payment")
				&& oTempJSONObject.optString("void_all_payment_after_release_payment").equals("y"))
			return true;
		return false;
	}
	
	public boolean isOrderingBasketShowAddWaiveScTaxInfo() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "ordering_basket_show_add_waive_tax_sc_info");
		boolean bValue = false;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try {
					bValue = Boolean.valueOf(oPosConfig.getValue());
				} catch (Exception e) {
					AppGlobal.stack2Log(e);
				}
			}
		}
		return bValue;
	}
	
	// is support change cover by item group's item selected
	public HashMap<String, String> getChangeCoverByItemSold(){
		HashMap<String, String> oResultMap = new HashMap<String, String>();
		JSONObject oTempJSONObject = null;
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "auto_track_cover_based_on_item_ordering");
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				oTempJSONObject = new JSONObject(sValue);
				oResultMap.put("support", oTempJSONObject.optString("support"));
				if(oTempJSONObject.has("period_ids"))
					oResultMap.put("period_ids", oTempJSONObject.optString("period_ids"));
				oResultMap.put("item_group_ids", oTempJSONObject.optString("item_group_ids"));
			} catch (JSONException e) {
				oResultMap.put("support", "n");
				oResultMap.put("period_ids", "");
				oResultMap.put("item_group_ids", "");
				AppGlobal.stack2Log(e);
			}
		}
		else
			oResultMap = null;
		
		return oResultMap;
	}
	
	public BigDecimal getTableValidationTableMinimumCharge() {
		JSONObject oTableValidationSetting = this.getTableValidationSetting();
		if (oTableValidationSetting == null)
			return BigDecimal.ZERO;
		
		String sMinimumCharge = oTableValidationSetting.optString("minimum_check_total_for_all_tables", "0");
		return new BigDecimal(sMinimumCharge);
	}
	
	public String getTableValidationTableMinimumChargeItemCode() {
		JSONObject oTableValidationSetting = this.getTableValidationSetting();
		if (oTableValidationSetting == null)
			return "";
		
		String sChargeItemCode = oTableValidationSetting.optString("minimum_charge_item_code", "");
		return sChargeItemCode;
	}	
	
	public boolean getTableValidationAskBypassMaximumCheckTotal() {
		JSONObject oTableValidationSetting = this.getTableValidationSetting();
		if(oTableValidationSetting != null && oTableValidationSetting.has("ask_for_bypass_max_check_total") && oTableValidationSetting.optString("ask_for_bypass_max_check_total", "").equals("y"))
			return true;
		return false;
	}
	
	public ArrayList<String> getRepeatRoundItemLimitation() {
		JSONObject oRepeatRoundItemsLimitation = this.getPosConfigJSONObjectValue("system", "repeat_round_items_limitation", null, true);
		ArrayList<String> sItemDepartmentList = new ArrayList<String>();
		if (oRepeatRoundItemsLimitation != null && oRepeatRoundItemsLimitation.has("support") && oRepeatRoundItemsLimitation.optString("support", "").equals("y")) {
			if (oRepeatRoundItemsLimitation.has("item_departments")) {
				JSONArray oTempJsonArray = oRepeatRoundItemsLimitation.optJSONArray("item_departments");
				for (int i = 0; i < oTempJsonArray.length(); i++)
					sItemDepartmentList.add(oTempJsonArray.optString(i));
			}
		}
		return sItemDepartmentList;
	}
	
	public JSONObject getOpenCheckSetting() {
		JSONObject oJSONObject = this.getPosConfigJSONObjectValue("bar_tab", "open_check_setting", null, true);
		if (oJSONObject != null && oJSONObject.has("period_ids")) {
			String[] sPeriodList = oJSONObject.optString("period_ids", "").split(",");
			try {
				oJSONObject.putOpt("period_ids", sPeriodList);
			} catch (Exception e) {
				AppGlobal.stack2Log(e);
			}
		}
		
		return oJSONObject;
	}
	
	public String getCheckTotalCalculationMethod() {
		JSONObject oTmpJSONObject = this.getPosConfigJSONObjectValue("system", "check_listing_total_calculation_method", null, true);
		if(oTmpJSONObject != null && oTmpJSONObject.has("support") && oTmpJSONObject.optString("support").equals("y")
				&& oTmpJSONObject.has("method"))
			return oTmpJSONObject.optString("method");
		
		// default use check total
		return CHECK_LISTING_CALCULATION_METHOD_CHECK_TOTAL;
	}
	
	public int getIdleTimeLogout(List<Integer> oCurrentUserGroupList) {
		boolean bNeedAutoLogout = false;
		JSONObject oValue = getPosConfigJSONObjectValue("system", "idle_time_logout", null, true);
		if (oValue == null)
			return -1;
		
		int iTimeoutValue = oValue.optInt("timeout", -1);
		JSONArray oUserGroupId = oValue.optJSONArray("user_group_ids");
		HashMap<Integer, Integer> oTimeoutMap = new HashMap<Integer, Integer>();
		if (oUserGroupId == null)
			return -1;
		
		for (int i = 0; i < oUserGroupId.length(); i++) {
			JSONObject oTemJsonObj = oUserGroupId.optJSONObject(i);
			int iId = oTemJsonObj.optInt("id", -1);
			int iUserGroupTimeOut = oTemJsonObj.optInt("timeout", -1);
			if (iId == -1 && iUserGroupTimeOut == -1)
				continue;
			
			oTimeoutMap.put(iId, iUserGroupTimeOut);
		}
		
		if (oTimeoutMap.isEmpty())
			return -1;
		
		for (Integer iUserGroupIndex : oCurrentUserGroupList) {
			if (oTimeoutMap.containsKey(iUserGroupIndex)) {
				if (oTimeoutMap.get(iUserGroupIndex) > 0) {
					iTimeoutValue = oTimeoutMap.get(iUserGroupIndex);
					bNeedAutoLogout = true;
				}
			}
		}
		
		// No matching for user groups of current user, no timeout value
		if (!bNeedAutoLogout)
			iTimeoutValue = -1;
		
		return iTimeoutValue;
	}
	
	public int getTimeControlToOpenNextCheckbyMember() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("kiosk", "time_control_to_open_next_check_by_member");
		if (oPosConfig == null || oPosConfig.getValue() == null)
			return -1;
		else 
			return Integer.parseInt(oPosConfig.getValue());
	}
	
	public JSONObject getSwitchCheckInfoSetting() {
		return this.getPosConfigJSONObjectValue("system", "switch_check_info_setting", null, true);
	}
	
	public boolean getBreakdownInclusiveScTaxAtFinalSettle() {
		JSONObject oValue = getPosConfigJSONObjectValue("system", "special_setup_for_inclusive_sc_tax", null, true);
		if (oValue == null)
			return false;
		
		boolean bSupport = oValue.optString("breakdown_at_check_settle", "").equals(PosConfig.BOOLEAN_VALUE_YES);
		return bSupport;
	}
	
	public boolean isStayInCashierWhenInterfacePaymentFailed() {
		return this.getPosConfigBooleanValue("system", "stay_in_cashier_when_interface_payment_failed", false);
	}
	
	public JSONArray getDummyPaymentMapping () {
		JSONObject oTmpJSONObject = null;
		JSONArray oJsonArray = null;
		oTmpJSONObject = this.getPosConfigJSONObjectValue("system", "payment_rounding_dummy_payment_mapping", null, true);
		if(oTmpJSONObject != null && oTmpJSONObject.has("mapping")){
			try {
				oJsonArray = new JSONArray(oTmpJSONObject.optString("mapping"));
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		return oJsonArray;
	}
	
	public String getOrderingBasketToggleConsolidateItemsGroupingMethod() {
		PosConfig oPosConfig = AppGlobal.getPosConfig("system", "ordering_basket_toggle_consolidate_items_grouping_method");
		String sValue = "o";
		
		if(oPosConfig != null){
			if(oPosConfig.getValue() != null) {
				try{
					sValue = oPosConfig.getValue();
				}catch(Exception e){
					// Incorrect format
				}
			}
		}
		
		return sValue;
	}
	
	//get support clean status function list
	public JSONArray getSupportCleaningStatusFunctionList() {
		JSONObject oTableFloorPlanSetting = this.getTableFloorPlanSetting();
		JSONArray oCleaningStatusFunctionList = null;
		if(oTableFloorPlanSetting != null && oTableFloorPlanSetting.has("support_table_status_cleaning") && oTableFloorPlanSetting.optString("support_table_status_cleaning").equals("y")) {
			if (oTableFloorPlanSetting.has("cleaning_status_function_list") && !oTableFloorPlanSetting.optString("cleaning_status_function_list").isEmpty()) {
				try {
					oCleaningStatusFunctionList = new JSONArray(oTableFloorPlanSetting.optString("cleaning_status_function_list"));
				} catch (JSONException e) {
					AppGlobal.stack2Log(e);
				}
			}
		}
		
		return oCleaningStatusFunctionList;
	}
	
	public JSONObject getTableStatusBackgroundColor(){
		return this.getPosConfigJSONObjectValue("system", "table_status_color", null, true);
	}
	
	/********************
	 * For internal use *
	 ********************/
	private boolean getPosConfigBooleanValue(String sSection, String sVariable, boolean bDefaultValue) {
		PosConfig oPosConfig = AppGlobal.getPosConfig(sSection, sVariable);
		Boolean bValue = bDefaultValue;
		if (oPosConfig != null) {
			if (oPosConfig.getValue() != null) {
				try{
					bValue = Boolean.valueOf(oPosConfig.getValue());
				}catch(Exception e){
					AppGlobal.stack2Log(e);
				}
			}
		}
		return bValue;
	}
	
	private JSONObject getPosConfigJSONObjectValue(String sSection, String sVariable, JSONObject oDefaultJSONObject, boolean bWriteLog) {
		JSONObject oTempJSONObject = oDefaultJSONObject;
		PosConfig oPosConfig = AppGlobal.getPosConfig(sSection, sVariable);
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			String sValue = oPosConfig.getValue();
			try {
				oTempJSONObject = new JSONObject(sValue);
			} catch (JSONException e) {
				if(bWriteLog)
					AppGlobal.stack2Log(e);
			}
		}
		return oTempJSONObject;
	}
	
	private String getPosConfigStringValue(String sSection, String sVariable, String sDefaultValue) {
		String sValue = sDefaultValue;
		PosConfig oPosConfig = AppGlobal.getPosConfig(sSection, sVariable);
		if (oPosConfig != null && oPosConfig.getValue() != null) {
			try{
				sValue = oPosConfig.getValue();
			}catch(Exception e){
				AppGlobal.stack2Log(e);
			}
		}
		return sValue;
	}
	
	// get the option that enable user to check print queue status if alert message
	public boolean isEnableUserCheckingPrintQueueIfAlertMessage() {
		return this.getPosConfigBooleanValue("kiosk", "enable_user_to_check_print_queue_status_if_alert_message", false);
	}
	
}