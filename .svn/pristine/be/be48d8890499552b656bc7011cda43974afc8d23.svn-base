package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import commonui.FormSelectionBox;
import core.Controller;
import om.InfInterface;
import om.InfVendor;
import om.PosCheckExtraInfo;
import om.PosInterfaceConfig;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

public class FormPMS extends VirtualUIForm implements FramePMSEnquiryResultListener{
	TemplateBuilder m_oTemplateBuilder;
	
	private FramePMSEnquiryResult m_oFramePMSEnquiryResult;
	
	private FuncPMS m_oFuncPMS;
	private FuncPMS4700SerialPort m_oFuncPMS4700SerialPort;
	private boolean m_bFromTableFloorPlan;
	private int m_iInterfaceId;
	private String m_sInterfaceVendorKey;
	private JSONObject m_oInterfaceSetup;
	private String m_sCheckPrefixNo;
	private String m_sPaymentCodeForPostingEnquiry;
	private PosInterfaceConfig m_oPaymentPmsConfig;
	private FuncPMS.EnquiryResponse m_oEnquiryResponse;
	
	private FuncCheck m_oFuncCheck;
	private String m_sRoomNo;
	private boolean m_bUserCancelPosting;
	private int m_iChosenGuestIndexForPosting;
	private String m_sPostingType;
	protected int m_iChosenGuestIndexForEnquiry;
	protected int m_iChosenSubGuestIndexForEnquiry;
	private HashMap<String, String> m_oInputtedEnquiryInfo;
	private boolean m_bShowSetRoom;
	
	public FormPMS(Controller oParentController) {
		super(oParentController);
		
		m_oFuncPMS = new FuncPMS();
		m_oTemplateBuilder = new TemplateBuilder();
		m_oTemplateBuilder.loadTemplate("frmPMS.xml");
		
		m_bFromTableFloorPlan = false;
		m_iInterfaceId = 0;
		m_sInterfaceVendorKey = "";
		m_oFuncCheck = null;
		m_sRoomNo = "";
		m_oInterfaceSetup = null;
		m_sCheckPrefixNo = "";
		m_sPaymentCodeForPostingEnquiry = null;
		m_oPaymentPmsConfig = null;
		m_oEnquiryResponse = null;
		
		m_bUserCancelPosting = false;
		m_iChosenGuestIndexForPosting = -1;
		m_sPostingType = "";
		m_iChosenGuestIndexForEnquiry = -1;
		m_iChosenSubGuestIndexForEnquiry = -1;
		m_bShowSetRoom = false;
	}
	
	public void init(String sInterfaceKey, JSONObject oInterfaceSetup, boolean bShowAREnquiryButton, boolean bShowPackageEnquiryButton, boolean bShowExtendGuestDetail, boolean bPostingAskInfo, int iRequestTimeout, String sRoomNo) {
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFramePMSEnquiryResult = new FramePMSEnquiryResult();
		m_oTemplateBuilder.buildFrame(m_oFramePMSEnquiryResult, "fraPMSEnquiryResult");
		m_oFramePMSEnquiryResult.setPostingType(m_sPostingType);
		m_oFramePMSEnquiryResult.addListener(this);
		m_oFramePMSEnquiryResult.init(sInterfaceKey, oInterfaceSetup, bShowAREnquiryButton, false, bShowPackageEnquiryButton, bShowExtendGuestDetail, bPostingAskInfo, iRequestTimeout, sRoomNo, m_bShowSetRoom);
		
		this.attachChild(m_oFramePMSEnquiryResult);
	}
	
	// PMS enquiry
	public boolean pmsEnquiry(String sCheckNum, FuncCheck oFuncCheck) {
		m_oFuncCheck = oFuncCheck;
		String sDefaultNumber = "";
		List<PosInterfaceConfig> oPMSInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		
		// Show error if no PMS interface define
		oPMSInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PMS);
		if(oPMSInterfaceConfigList.isEmpty()) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("pms_enquiry"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("no_pms_interface_setup"));
			oFormDialogBox.show();
			m_bUserCancelPosting = true;
			return false;
		}
		
		if(sCheckNum == null)
			m_bFromTableFloorPlan = true;
		
		// Ask to select PMS interface as necessary
		if(oPMSInterfaceConfigList.size() > 1) {
			ArrayList<String> oOptionList = new ArrayList<String>();
			HashMap<Integer, HashMap<String, String>> oInterfaceIdList = new HashMap<Integer, HashMap<String, String>>();
			int iInterfaceCount = 0;
			
			for(PosInterfaceConfig oInterfaceConfig:oPMSInterfaceConfigList) {
				if(m_bFromTableFloorPlan && oInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_R8))
					continue;
								
				oOptionList.add(oInterfaceConfig.getInterfaceName(AppGlobal.g_oCurrentLangIndex.get()));
				HashMap<String, String> oTempInterfaceInfo = new HashMap<String, String>();
				oTempInterfaceInfo.put("interfaceId", String.valueOf(oInterfaceConfig.getInterfaceId()));
				oTempInterfaceInfo.put("vendorKey", oInterfaceConfig.getInterfaceVendorKey());
				
				if(oInterfaceConfig.getInterfaceConfig() != null)
					oTempInterfaceInfo.put("setup", oInterfaceConfig.getInterfaceConfig().toString());
				else
					oTempInterfaceInfo.put("setup", "");
				
				oInterfaceIdList.put(iInterfaceCount, oTempInterfaceInfo);
				iInterfaceCount++;
			}
			
			FormSelectionBox oFormSelectionBox = new FormSelectionBox(this);
			oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("please_select_the_pms_interface"), oOptionList, false);
			oFormSelectionBox.show();
			
			if(oFormSelectionBox.isUserCancel()){ 
				m_bUserCancelPosting = true;
				return false;
			}
			else {
				HashMap<String, String> oTempInterfaceInfo = oInterfaceIdList.get(oFormSelectionBox.getResultList().get(0));
				m_iInterfaceId = Integer.valueOf(oTempInterfaceInfo.get("interfaceId"));
				m_sInterfaceVendorKey = oTempInterfaceInfo.get("vendorKey");
				try {
					m_oInterfaceSetup = new JSONObject(oTempInterfaceInfo.get("setup"));
				}catch (JSONException jsone) {
					m_oInterfaceSetup = null;
				}
			}
		}else {
			m_iInterfaceId = oPMSInterfaceConfigList.get(0).getInterfaceId();
			m_sInterfaceVendorKey = oPMSInterfaceConfigList.get(0).getInterfaceVendorKey();
			m_oInterfaceSetup = oPMSInterfaceConfigList.get(0).getInterfaceConfig();
		}
		
		if(sCheckNum != null && m_sInterfaceVendorKey.equals(InfVendor.KEY_ASPEN_PMS))
			m_bShowSetRoom = true;
		if(sCheckNum != null)
			m_sCheckPrefixNo = sCheckNum;
		
		// show enquiry and result page
		String sIntfId = "", sRoomNo = "";
		boolean bIsSetRoom = false;
		if(m_oFuncCheck != null){
			if(m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0))
				sIntfId = m_oFuncCheck.getCheckExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0);
			if(m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_ROOM, 0)){
				sRoomNo = m_oFuncCheck.getCheckExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_ROOM, 0);
				if(!sRoomNo.isEmpty()){
					bIsSetRoom = true;
					m_sRoomNo = sRoomNo;
				}
			}
		}
		// if room is attached, ask clear room or not
		if(bIsSetRoom && sCheckNum != null){
			if(!sIntfId.isEmpty() && !sIntfId.equals(Integer.toString(m_iInterfaceId))){
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("pms_enquiry"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("have_different_interface_room")
						+ System.lineSeparator() + AppGlobal.g_oLang.get()._("please_clear_current_room_first"));
				oFormDialogBox.show();
				return false;
			}else{
				if(m_sInterfaceVendorKey.equals(InfVendor.KEY_ASPEN_PMS)){
					if (m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_ROOM) != null)
						sDefaultNumber = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_ROOM);
				}
				
				int iSelectedAction = 0;
				ArrayList<String> oOptionList = new ArrayList<String>();
				oOptionList.add(AppGlobal.g_oLang.get()._("pms_enquiry"));
				oOptionList.add(AppGlobal.g_oLang.get()._("clear_room"));
				
				FormSelectionBox oFormSelectionBox = new FormSelectionBox(this);
				oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("please_select_the_action_type"), oOptionList, false);
				oFormSelectionBox.show();
				
				if(oFormSelectionBox.isUserCancel())
					return false;
				iSelectedAction = oFormSelectionBox.getResultList().get(0);
				if(iSelectedAction == 1) {
					clearRoom(sRoomNo);
					return false;
				}else
					showEnquiryResult(false, sDefaultNumber);
			}
		}else
			showEnquiryResult(false, sDefaultNumber);
		return true;
		
	}
	
	// Restart PMS shell
	public void restartPMSShell() {
		List<PosInterfaceConfig> oPMSInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		ArrayList<String> oOptionList = new ArrayList<String>();
		HashMap<Integer, HashMap<String, String>> oInterfaceIdList = new HashMap<Integer, HashMap<String, String>>();
		int iInterfaceCount = 0;
		
		oPMSInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PMS);
		for(PosInterfaceConfig oPosInterfaceConfig:oPMSInterfaceConfigList) {
			if(!oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP)
					&& !oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_R8))
				continue;
			
			oOptionList.add(oPosInterfaceConfig.getInterfaceName(AppGlobal.g_oCurrentLangIndex.get()));
			HashMap<String, String> oTempInterfaceInfo = new HashMap<String, String>();
			oTempInterfaceInfo.put("interfaceId", String.valueOf(oPosInterfaceConfig.getInterfaceId()));
			oTempInterfaceInfo.put("vendorKey", oPosInterfaceConfig.getInterfaceVendorKey());
			oInterfaceIdList.put(iInterfaceCount, oTempInterfaceInfo);
			iInterfaceCount++;
		}
		
		FormSelectionBox oFormSelectionBox = new FormSelectionBox(this);
		oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("please_select_the_pms_interface"), oOptionList, false);
		oFormSelectionBox.show();
		
		if(oFormSelectionBox.isUserCancel())
			return;
		else {
			boolean bResult = m_oFuncPMS.restartPMSShell(Integer.parseInt(oInterfaceIdList.get(oFormSelectionBox.getResultList().get(0)).get("interfaceId")), oInterfaceIdList.get(oFormSelectionBox.getResultList().get(0)).get("vendorKey"));
			String sMessage = "";
			if(!bResult) 
				sMessage = m_oFuncPMS.getLastErrorMessage();
			else
				sMessage = AppGlobal.g_oLang.get()._("shell_is_start");
			
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("restart_pms_shell"));
			oFormDialogBox.setMessage(sMessage);
			oFormDialogBox.show();
			return;
		}
	}
	
	// Stop PMS shell
	public void stopPMSShell() {
		List<PosInterfaceConfig> oPMSInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		ArrayList<String> oOptionList = new ArrayList<String>();
		HashMap<Integer, HashMap<String, String>> oInterfaceIdList = new HashMap<Integer, HashMap<String, String>>();
		int iInterfaceCount = 0;
		
		oPMSInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PMS);
		for(PosInterfaceConfig oPosInterfaceConfig:oPMSInterfaceConfigList) {
			if(!oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_STANDARD_TCPIP)
					&& !oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_R8))
				continue;
			
			oOptionList.add(oPosInterfaceConfig.getInterfaceName(AppGlobal.g_oCurrentLangIndex.get()));
			HashMap<String, String> oTempInterfaceInfo = new HashMap<String, String>();
			oTempInterfaceInfo.put("interfaceId", String.valueOf(oPosInterfaceConfig.getInterfaceId()));
			oTempInterfaceInfo.put("vendorKey", oPosInterfaceConfig.getInterfaceVendorKey());
			oInterfaceIdList.put(iInterfaceCount, oTempInterfaceInfo);
			iInterfaceCount++;
		}
		
		FormSelectionBox oFormSelectionBox = new FormSelectionBox(this);
		oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("please_select_the_pms_interface"), oOptionList, false);
		oFormSelectionBox.show();
		
		if(oFormSelectionBox.isUserCancel())
			return;
		else {
			boolean bResult = m_oFuncPMS.stopPMSShell(Integer.parseInt(oInterfaceIdList.get(oFormSelectionBox.getResultList().get(0)).get("interfaceId")), oInterfaceIdList.get(oFormSelectionBox.getResultList().get(0)).get("vendorKey"));
			String sMessage = AppGlobal.g_oLang.get()._("shell_is_stopped");
			if(!bResult)
				sMessage = AppGlobal.g_oLang.get()._("stop_shell_fail");
			
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("stop_pms_shell"));
			oFormDialogBox.setMessage(sMessage);
			oFormDialogBox.show();
			return;
		}
	}
	
	// PMS ask info for posting
	public void pmsAskInfoForPosting(PosInterfaceConfig oPaymentPmsConfig, String sCheckPrefixNo, String sPaymentCode, String sPostingType, String sRoomNo) {
		m_iInterfaceId = oPaymentPmsConfig.getInterfaceId();
		m_sInterfaceVendorKey = oPaymentPmsConfig.getInterfaceVendorKey();
		m_oInterfaceSetup = oPaymentPmsConfig.getInterfaceConfig();
		m_sCheckPrefixNo = sCheckPrefixNo;
		m_sPaymentCodeForPostingEnquiry = sPaymentCode;
		m_oPaymentPmsConfig = oPaymentPmsConfig;
		m_sPostingType = sPostingType;
		
		// show enquiry and result page
		showEnquiryResult(true, sRoomNo);
		
		// setup the enquiry screen for posting
		m_oFramePMSEnquiryResult.setPostingTypeEnquiryPage(m_sPostingType);
	}
	
	public boolean isUserCancel() {
		return this.m_bUserCancelPosting;
	}
	
	public HashMap<String, String> getChosenGuestInfo() {
		return this.m_oEnquiryResponse.oGuestList.get(m_iChosenGuestIndexForPosting);
	}
	
	public HashMap<String, String> getInputtedEnquiryInfo() {
		return m_oInputtedEnquiryInfo;
	}
	
	public HashMap<String, String> getEnquiryResponseInfo() {
		HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();
		
		oEnquiryInfo.put("type", m_oEnquiryResponse.sLookupType);
		oEnquiryInfo.put("idContext", m_oEnquiryResponse.sLookupIdContext);
		oEnquiryInfo.put("id", m_oEnquiryResponse.sLookupId);
		
		return oEnquiryInfo;
	}
	
	// check no post criteria
	public boolean checkNoPost() {
		return m_oFuncPMS.checkNoPosting(m_sInterfaceVendorKey, m_oInterfaceSetup, m_oPaymentPmsConfig.getConfigValue(), this.m_oEnquiryResponse.oGuestList.get(m_iChosenGuestIndexForPosting));
	}
	
	// check have maximum credit limit
	public boolean haveMaximumCreditLimit() {
		return m_oFuncPMS.checkCreditLimit(m_sInterfaceVendorKey, m_oInterfaceSetup, m_oPaymentPmsConfig.getConfigValue());
	}
	
	public boolean clearRoom(String sRoomNo){
		String sMessage = "";
		//user clear room
		if(m_sInterfaceVendorKey.equals(InfVendor.KEY_ASPEN_PMS)){
			sMessage = AppGlobal.g_oLang.get()._("clear_current_room") + ": " + System.lineSeparator()
					+ AppGlobal.g_oLang.get()._("room_number") + ": " + sRoomNo + System.lineSeparator()
					+ AppGlobal.g_oLang.get()._("confirm_to_clear_room") + "?";
		}
		FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
		oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("clear_room"));
		oFormConfirmBox.setMessage(sMessage);
		oFormConfirmBox.show();
		if(oFormConfirmBox.isOKClicked() == false)
			return false;
		// really clear room 
		if(m_sInterfaceVendorKey.equals(InfVendor.KEY_ASPEN_PMS)){
			m_oFuncCheck.updateCheckExtraInfoValue(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_ROOM, 0, "");
			m_oFuncCheck.updateCheckExtraInfoValue(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_GUEST_NAME, 0, "");
			m_oFuncCheck.updateCheckExtraInfoValue(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0, "");
			m_oFuncCheck.updatePMSExtraInfo();
		}
		return true;
	}
	
	// ask room/account number
	private void doPMSEnquiry(HashMap<String, String> oEnquiryInfo, boolean bPostingAskInfo) {
		boolean bFromArEnquiry = false;
		FuncPMS.EnquiryResponse oTmpEnquiryResponse;
		String sEnquiryType = FuncPMS.ENQUIRY_TYPE_ROOM;
		if(oEnquiryInfo.containsKey("enquiryFromArEnquiry") && oEnquiryInfo.get("enquiryFromArEnquiry").equals("true"))
			bFromArEnquiry = true;
		if(oEnquiryInfo.containsKey("enquiryType") && !oEnquiryInfo.get("enquiryType").isEmpty())
			sEnquiryType = oEnquiryInfo.get("enquiryType");
		if(bPostingAskInfo)
			oEnquiryInfo.put("postingAskInfo", "1");
		
		oTmpEnquiryResponse = m_oFuncPMS.pmsEnquiry(m_iInterfaceId, oEnquiryInfo, m_sCheckPrefixNo, m_sPaymentCodeForPostingEnquiry);
		if(m_sInterfaceVendorKey.equals(InfVendor.KEY_XMS) && sEnquiryType.equals(FuncPMS.ENQUIRY_TYPE_SUB_AR) && bFromArEnquiry) {
			m_oFramePMSEnquiryResult.clearGuestList(false, false);
			for(HashMap<String, String> oTempGuest: oTmpEnquiryResponse.oGuestList) {
				if(!oTempGuest.containsKey("masterAccountNum") || oTempGuest.get("masterAccountNum").isEmpty())
					continue;
				
				for(HashMap<String, String> oGuest: m_oEnquiryResponse.oGuestList) {
					if(!oGuest.containsKey("accountNumber") || oGuest.get("accountNumber").isEmpty())
						continue;
					
					if(oGuest.get("accountNumber").equals(oTempGuest.get("masterAccountNum"))) {
						if(oTempGuest.containsKey("guestImage") && !oTempGuest.get("guestImage").isEmpty())
							oGuest.put("guestImage", oTempGuest.get("guestImage"));
						
						if(oTempGuest.containsKey("guestSignImage") && !oTempGuest.get("guestSignImage").isEmpty())
							oGuest.put("guestSignImage", oTempGuest.get("guestSignImage"));
						
						break;
					}
				}
			}
		}else {
			m_oFramePMSEnquiryResult.clearGuestList(true, true);
			m_oEnquiryResponse = oTmpEnquiryResponse;
		}
		if(m_oEnquiryResponse != null && m_oEnquiryResponse.oGuestList != null && !m_oEnquiryResponse.oGuestList.isEmpty())
			updateEnquiryGuestList(oEnquiryInfo);
		else {
			String sDialogBoxMessage = "";
			if (!m_oFuncPMS.getLastErrorMessage().isEmpty())
				sDialogBoxMessage = m_oFuncPMS.getLastErrorMessage();
			else
				sDialogBoxMessage = AppGlobal.g_oLang.get()._("vacant_room_account");
			
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("pms_enquiry"));
			oFormDialogBox.setMessage(sDialogBoxMessage);
			oFormDialogBox.show();
		}
	}
	
	public void blockSetAndClearRoomButton(){
		m_oFramePMSEnquiryResult.blockSetAndClearRoomButton();
	}
	
	// show enquiry result
	private void showEnquiryResult(boolean bPostingAskInfo, String sRoomNo) {
		int iRequestTimeout = 30000;
		if(m_sInterfaceVendorKey.equals(InfVendor.KEY_R8)) {
			// for pms type - R8
			if(bPostingAskInfo) {
				if(m_oPaymentPmsConfig != null){
					JSONObject oPaymentPmsSetup = m_oPaymentPmsConfig.getConfigValue();
					try {
						if(!oPaymentPmsSetup.getJSONObject("general").getJSONObject("params").getJSONObject("pms_payment_type").getString("value").equals(FuncPMS.R8_PAYMENT_TYPE_CASH)) {
							init(m_sInterfaceVendorKey, null, true, false, false, bPostingAskInfo, 0, sRoomNo);
						}
					}catch(JSONException jsone) {
						jsone.printStackTrace();
					}
				}
			}else {
				init(m_sInterfaceVendorKey, null, true, false, false, bPostingAskInfo, 0, sRoomNo);
			}
		}else if(m_sInterfaceVendorKey.equals(InfVendor.KEY_STANDARD_TCPIP) || m_sInterfaceVendorKey.equals(InfVendor.KEY_HTNG)) {
			// for pms type - standard (TCP/IP)
			if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_packet_request_timeout"))
				iRequestTimeout = (m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_packet_request_timeout").optInt("value")) * 1000;
			boolean bArEnquiryShow = false;
			if(m_oInterfaceSetup.optJSONObject("connection_setup").optJSONObject("params").has("pms_type") && m_oInterfaceSetup.optJSONObject("connection_setup").optJSONObject("params").optJSONObject("pms_type").optString("value").equals(FuncPMS.PMS_TYPE_SHIJI))
				bArEnquiryShow = true;
			init(m_sInterfaceVendorKey, m_oInterfaceSetup, bArEnquiryShow, false, false, bPostingAskInfo, iRequestTimeout, sRoomNo);
			
		}else if(m_sInterfaceVendorKey.equals(InfVendor.KEY_4700_TCPIP) || m_sInterfaceVendorKey.equals(InfVendor.KEY_4700_SERIAL_PORT)) {
			// for pms type - 4700
			if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_packet_request_timeout"))
				iRequestTimeout = (m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_packet_request_timeout").optInt("value")) * 1000;
			if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_server_retransmit") && m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_server_retransmit").optInt("value", 0) > 0)
				iRequestTimeout = (iRequestTimeout * m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_server_retransmit").optInt("value"));
			
			boolean bSupportExtendInfo = false;
			if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_extend_info_enquiry") && m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_extend_info_enquiry").optInt("value", 0) == 1)
				bSupportExtendInfo = true;
			
			if(bPostingAskInfo)
				init(m_sInterfaceVendorKey, m_oInterfaceSetup, false, false, false, bPostingAskInfo, iRequestTimeout, sRoomNo);
			else
				init(m_sInterfaceVendorKey, m_oInterfaceSetup, false, false, bSupportExtendInfo, bPostingAskInfo, iRequestTimeout, sRoomNo);
		} else if (m_sInterfaceVendorKey.equals(InfVendor.KEY_PEGASUS)) {
			// for pms type - pegasus
			if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_packet_request_timeout"))
				iRequestTimeout = (m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_packet_request_timeout").optInt("value")) * 1000;
			init(m_sInterfaceVendorKey, m_oInterfaceSetup, false, true, false, bPostingAskInfo, iRequestTimeout, sRoomNo);
		} else if(m_sInterfaceVendorKey.equals(InfVendor.KEY_XMS)) {
			// for pms type - XMS
			if(m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").has("pms_packet_request_timeout"))
				iRequestTimeout = (m_oInterfaceSetup.optJSONObject("pms_setting").optJSONObject("params").optJSONObject("pms_packet_request_timeout").optInt("value")) * 1000;
			boolean bArEnquiryShow = true;
			init(m_sInterfaceVendorKey, m_oInterfaceSetup, bArEnquiryShow, false, false, bPostingAskInfo, iRequestTimeout, sRoomNo);
		} else if(m_sInterfaceVendorKey.equals(InfVendor.KEY_ASPEN_PMS)) {
			if(m_oInterfaceSetup.optJSONObject("connection_setup").optJSONObject("params").has("pms_packet_request_timeout"))
				iRequestTimeout = (m_oInterfaceSetup.optJSONObject("connection_setup").optJSONObject("params").optJSONObject("pms_packet_request_timeout").optInt("value")) * 1000;
			boolean bArEnquiryShow = false;
			init(m_sInterfaceVendorKey, m_oInterfaceSetup, bArEnquiryShow, false, false, bPostingAskInfo, iRequestTimeout, sRoomNo);
		}
	}
	
	// update enquiry result
	private void updateEnquiryGuestList(HashMap<String, String> oEnquiryInfo) {
		int iIndex = 0;
		boolean bFromArEnquiry = false;
		String sEnquiryType = FuncPMS.ENQUIRY_TYPE_ROOM;
		if(oEnquiryInfo.containsKey("enquiryFromArEnquiry") && oEnquiryInfo.get("enquiryFromArEnquiry").equals("true"))
			bFromArEnquiry = true;
		if(oEnquiryInfo.containsKey("enquiryType") && !oEnquiryInfo.get("enquiryType").isEmpty())
			sEnquiryType = oEnquiryInfo.get("enquiryType");
		
		for(HashMap<String, String> oGuestInfo:m_oEnquiryResponse.oGuestList) {
			if (m_sInterfaceVendorKey.equals(InfVendor.KEY_PEGASUS)) {		// Pegasus iPMS package type checking
				if (m_oPaymentPmsConfig.getConfigValue() != null) {	// payment by amount package posting
					String sPostingType = m_oPaymentPmsConfig.getConfigValue().optJSONObject("general").optJSONObject("params").optJSONObject("posting_type").optString("value");
					if (sPostingType != null) {
						if (sPostingType.contains("package") && oGuestInfo.containsKey("packageType") && oGuestInfo.get("packageType").equals("Q"))
							continue;
					}
				} else {	// do not have interface config for quantity package posting
					if (oGuestInfo.containsKey("packageType") && oGuestInfo.get("packageType").equals("M"))
						continue;
				}
			}
			
			if(!sEnquiryType.equals(FuncPMS.ENQUIRY_TYPE_SUB_AR)) {
				m_oFramePMSEnquiryResult.addGuestToGuestList(0, iIndex, m_sInterfaceVendorKey, oGuestInfo);
				if(iIndex == 0)
					m_oFramePMSEnquiryResult.showGuestDetail(sEnquiryType, m_oEnquiryResponse.oGuestList.get(iIndex), m_oEnquiryResponse.oSubGuestList, false);
			}else {
				if(bFromArEnquiry) {
					String sMasterAccNumber = oEnquiryInfo.get("enquiryNumber");
					String sSubAccountNumber = "";
					if(oEnquiryInfo.containsKey("enquirySubAccountNumber") && !oEnquiryInfo.get("enquirySubAccountNumber").isEmpty())
						sSubAccountNumber = oEnquiryInfo.get("enquirySubAccountNumber");
						
					for(HashMap<String, String> oMasterGuest : m_oEnquiryResponse.oGuestList) {
						if(oMasterGuest.get("accountNumber").equals(sMasterAccNumber)) {
							Integer iGuestIndex = Integer.valueOf(oMasterGuest.get("guestIndex"));
							if(m_oEnquiryResponse.oSubGuestList.containsKey(iGuestIndex)) {
								for(HashMap<String, String> oSubGuest : m_oEnquiryResponse.oSubGuestList.get(iGuestIndex)) {
									if(oSubGuest.get("accountNumber").equals(sSubAccountNumber)) {
										if(oMasterGuest.containsKey("guestImage") && !oMasterGuest.get("guestImage").isEmpty())
											oSubGuest.put("guestImage", oMasterGuest.get("guestImage"));
										if(oMasterGuest.containsKey("guestSignImage") && !oMasterGuest.get("guestSignImage").isEmpty())
											oSubGuest.put("guestSignImage", oMasterGuest.get("guestSignImage"));
										m_oFramePMSEnquiryResult.showGuestDetail(sEnquiryType, oSubGuest, null, false);
										m_oFramePMSEnquiryResult.showImages(oSubGuest, true, true);
										break;
									}
								}
							}
								
							break;
						}
					}
				}else {
					m_oFramePMSEnquiryResult.addGuestToSubGuestList(0, iIndex, m_sInterfaceVendorKey, oGuestInfo, true);
					if(iIndex == 0)
						m_oFramePMSEnquiryResult.showGuestDetail(sEnquiryType, m_oEnquiryResponse.oGuestList.get(iIndex), m_oEnquiryResponse.oSubGuestList, false);
				}
			}
			iIndex++;
		}
	}
	
	public void changePackageButtonPosition() {
		m_oFramePMSEnquiryResult.changePackageButtonPosition();
	}
	
	/***********************/
	/*	Override Function  */
	/***********************/
	@Override
	public void framePMSEnquiryResult_clickBack() {
		this.finishShow();
		
		if(m_oPaymentPmsConfig != null)
			this.m_bUserCancelPosting = true;
	}
	
	@Override
	public void framePMSEnquiryResult_clickGuestRecord(int iGuestIndex, boolean bFromGuestList) {
		if(bFromGuestList)
			m_iChosenGuestIndexForEnquiry = iGuestIndex;
		m_oFramePMSEnquiryResult.showGuestDetail(null, m_oEnquiryResponse.oGuestList.get(iGuestIndex), m_oEnquiryResponse.oSubGuestList, true);
	}
	
	@Override
	public void framePMSEnquiryResult_clickGuestForPosting(int iGuestIndex, HashMap<String, String> oInputtedEnquiryInfo) {
		m_iChosenGuestIndexForPosting = iGuestIndex;
		m_oInputtedEnquiryInfo = oInputtedEnquiryInfo;
		this.finishShow();
		
	}
	
	@Override
	public void framePMSEnquiryResult_clickSubGuestForPosting(int iSubGuestIndex, HashMap<String, String> oInputtedEnquiryInfo) {
		m_iChosenGuestIndexForPosting = m_iChosenGuestIndexForEnquiry;
		if(iSubGuestIndex >= 0) {
			HashMap<String, String> oGuest = m_oEnquiryResponse.oGuestList.get(m_iChosenGuestIndexForPosting);	
			Integer iGuestIndex = Integer.valueOf(oGuest.get("guestIndex"));
			if(m_oEnquiryResponse.oSubGuestList.containsKey(iGuestIndex) && m_oEnquiryResponse.oSubGuestList.get(iGuestIndex).size() > iSubGuestIndex) {
				HashMap<String, String> oSubGuest = m_oEnquiryResponse.oSubGuestList.get(iGuestIndex).get(iSubGuestIndex);
				if(oSubGuest.containsKey("accountNumber") && !oSubGuest.get("accountNumber").isEmpty())
					oInputtedEnquiryInfo.put("enquirySubAccountNum", oSubGuest.get("accountNumber"));
			}
		}
		m_oInputtedEnquiryInfo = oInputtedEnquiryInfo;
		this.finishShow();
	}
	
	@Override
	public void framePMSEnquiryResult_clickEnquiry(HashMap<String, String> oEnquiryInfo, boolean bPostingAskInfo) {
		doPMSEnquiry(oEnquiryInfo, bPostingAskInfo);
		if(m_oEnquiryResponse != null && m_oEnquiryResponse.oGuestList.size() > 0)
			m_iChosenGuestIndexForEnquiry = 0;
	}
	
	@Override
	public String freamPMSEnquiryResult_getCurrentChosenGuestInformation(String sInfoName) {
		String sInformation = "";
		if(m_oEnquiryResponse.oGuestList.get(m_iChosenGuestIndexForEnquiry).containsKey(sInfoName) && m_oEnquiryResponse.oGuestList.get(m_iChosenGuestIndexForEnquiry).get(sInfoName) != null)
			sInformation = m_oEnquiryResponse.oGuestList.get(m_iChosenGuestIndexForEnquiry).get(sInfoName);
		return sInformation;
	}
	
	@Override
	public void framePMSSetRoom(){
		if(m_oEnquiryResponse != null){
			HashMap<String, String> oGuestInfoMap = m_oEnquiryResponse.oGuestList.get(m_iChosenGuestIndexForEnquiry);
			String sRoomNumber = "", sGuestName = "", sMessage = "";
			
			for(Map.Entry<String, String> entry :oGuestInfoMap.entrySet()){
				String sKey = entry.getKey(), sValue = entry.getValue();
				if(m_sInterfaceVendorKey.equals(InfVendor.KEY_ASPEN_PMS)){
					if(sKey.equals("guestName")){
						sGuestName = sValue;
					}else if(sKey.equals("roomNumber")){
						sRoomNumber = sValue;
					}
				}
			}
			sMessage = AppGlobal.g_oLang.get()._("set_current_room") + ": " + System.lineSeparator()
					+ AppGlobal.g_oLang.get()._("room_number") + ": " + sRoomNumber + System.lineSeparator()
					+ AppGlobal.g_oLang.get()._("confirm_to_set_room") + "?";
			FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
			oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("clear_room"));
			oFormConfirmBox.setMessage(sMessage);
			oFormConfirmBox.show();
			
			if(oFormConfirmBox.isOKClicked() == false)
				return;
			else {
				if(!m_sInterfaceVendorKey.isEmpty() && m_sInterfaceVendorKey.equals(InfVendor.KEY_ASPEN_PMS)){
					if(m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_GUEST_NAME, 0))
						m_oFuncCheck.updateCheckExtraInfoValue(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_GUEST_NAME, 0, sGuestName);
					else
						m_oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_GUEST_NAME, 0, sGuestName);
					
					if(m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_ROOM, 0))
						m_oFuncCheck.updateCheckExtraInfoValue(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_ROOM, 0, sRoomNumber);
					else
						m_oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_ROOM, 0, sRoomNumber);
					
					if(m_oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0))
						m_oFuncCheck.updateCheckExtraInfoValue(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0, Integer.toString(m_iInterfaceId));
					else
						m_oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_PMS, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0, Integer.toString(m_iInterfaceId));
					if(m_oFuncCheck.isOldCheck())
						m_oFuncCheck.updatePMSExtraInfo();
					m_sRoomNo = sRoomNumber;
				}
				// Set result flag = success for auto function
				AppGlobal.g_sResultForAutoFunction.set(AppGlobal.AUTO_FUNCTIONS_RESULT_LIST.success.name());
				this.finishShow();
				return;
			}
		}else{
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_set_empty_room"));
			oFormDialogBox.show();
			return;
		}
	}
	
	@Override
	public boolean framePMSClearRoom(){
		boolean bClear = false;
		bClear = this.clearRoom(m_sRoomNo);
		if(bClear)
			this.finishShow();
		return bClear;
	}
}
