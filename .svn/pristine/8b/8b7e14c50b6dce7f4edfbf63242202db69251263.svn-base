package app;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import commonui.FormDialogBox;
import commonui.FormSelectionBox;
import core.Controller;
import om.InfInterface;
import om.InfVendor;
import om.PosActionPrintQueue;
import om.PosCheck;
import om.PosInterfaceConfig;
import om.PosOutletTable;
import om.PosOutletTableList;
import om.UserUser;
import templatebuilder.TemplateBuilder;
import virtualui.*;

interface FormMembershipRegistrationListener {
	boolean FormMembershipRegistration_openTable(int iTableNo, String sTableExtension, String sMemberNumber, String sSurname, int iInterfaceId);
}

public class FormMembershipRegistration extends VirtualUIForm implements FrameMembershipRegistrationListener, FrameOpenedCheckListListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private boolean m_bUserCancel;
	private PosInterfaceConfig m_oPosInterfaceConfig;
	private FuncMembershipInterface m_oFuncMembershipInterface;
	private FrameMembershipRegistration m_oFrameMembershipRegistration;
	private FrameOpenedCheckList m_oFrameOpenedCheckList;
	private boolean m_bForwardToMemberEnquiry;
	private PosInterfaceConfig m_oInterfaceConfig;
	private String m_sMemberNumber;
	private String m_sLastName;
	private String m_sFunctionName;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FormMembershipRegistrationListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FormMembershipRegistrationListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FormMembershipRegistrationListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FormMembershipRegistration(String sTitleName, Controller oParentController){
		super(oParentController);
		m_bUserCancel = false;
		m_bForwardToMemberEnquiry = false;
		m_oInterfaceConfig = null;
		m_sMemberNumber = "";
		m_sLastName = "";
		m_sFunctionName = sTitleName;
	}
	
	public boolean init(boolean bFromOrderingPanel, String sOpenCheckInfoListType) {
		m_oTemplateBuilder = new TemplateBuilder();
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmMembershipRegistration.xml");
		
		listeners = new ArrayList<FormMembershipRegistrationListener>();
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		//Opened check list
		m_oFrameOpenedCheckList = new FrameOpenedCheckList(sOpenCheckInfoListType);
		
		m_oTemplateBuilder.buildFrame(m_oFrameOpenedCheckList, "fraOpenedCheckList");
		m_oFrameOpenedCheckList.addListener(this);
		m_oFrameOpenedCheckList.setVisible(false);
		this.attachChild(m_oFrameOpenedCheckList);
		
		//Check if there is any CRM interface attached first
		List<PosInterfaceConfig> oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_MEMBERSHIP_INTERFACE);
		List<PosInterfaceConfig> oSvcPosInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		PosInterfaceConfig oTargetPosInterfaceConfig = null;
		
		if (AppGlobal.isModuleSupport(AppGlobal.OPTIONAL_MODULE.pos_interface.name()) == false) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("interface_module_is_not_supported"));
			oFormDialogBox.show();
			m_bUserCancel = true;
			return false;
		}
		
		if(oInterfaceConfigList.isEmpty()) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("membership_registration"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("no_membership_interface_setup"));
			oFormDialogBox.show();
			m_bUserCancel = true;
			return false;
		}

		// Support Ascentis & GC & General_V2
		for(PosInterfaceConfig oTempPosInterfaceConfig : oInterfaceConfigList) {
			if (oTempPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASCENTIS_CRM)
					|| oTempPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GOLDEN_CIRCLE)
					|| oTempPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2))
				oSvcPosInterfaceConfigList.add(oTempPosInterfaceConfig);
		}
		if(oSvcPosInterfaceConfigList.isEmpty()) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("membership_registration"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("no_interface_setup"));
			oFormDialogBox.show();
			m_bUserCancel = true;
			return false;
		}	

		if(oSvcPosInterfaceConfigList.size() == 1)
			oTargetPosInterfaceConfig = oSvcPosInterfaceConfigList.get(0);
		else {
			ArrayList<String> oOptionList = new ArrayList<String>();
			HashMap<Integer, HashMap<String, String>> oInterfaceIdList = new HashMap<Integer, HashMap<String, String>>();
			int iInterfaceCount = 0;
			
			for(PosInterfaceConfig oInterfaceConfig : oSvcPosInterfaceConfigList) {
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
			oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("please_select_the_membership_interface"), oOptionList, false);
			oFormSelectionBox.show();
			
			if(oFormSelectionBox.isUserCancel()) {
				m_bUserCancel = true;
				return false;
			}else {
				HashMap<String, String> oTempInterfaceInfo = oInterfaceIdList.get(oFormSelectionBox.getResultList().get(0));
				int iInterfaceId = Integer.valueOf(oTempInterfaceInfo.get("interfaceId"));
				for(PosInterfaceConfig oInterfaceConfig : oSvcPosInterfaceConfigList) {
					if(oInterfaceConfig.getInterfaceId() == iInterfaceId)
						oTargetPosInterfaceConfig = oInterfaceConfig;
				}
			}
		}
		
		// init global variable
		m_oPosInterfaceConfig = oTargetPosInterfaceConfig;
		
		m_oFrameMembershipRegistration = new FrameMembershipRegistration();
		m_oTemplateBuilder.buildFrame(m_oFrameMembershipRegistration, "fraMembershipRegistration");
		m_oFrameMembershipRegistration.init(m_oPosInterfaceConfig, m_sFunctionName, bFromOrderingPanel);
		if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GOLDEN_CIRCLE)){
			try {
				String sDefaultCountryCode = m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("enrolment_setup")
						.optJSONObject("params").optJSONObject("default_country_code").optString("value", "");
				m_oFrameMembershipRegistration.setDefaultCountryCode(sDefaultCountryCode);
			} catch (Exception e) {
			}
		}
		m_oFrameMembershipRegistration.addListener(this);
		m_oFrameMembershipRegistration.setVisible(true);		// Show this frame during first initial
		oCoverFrame.attachChild(m_oFrameMembershipRegistration);
		return true;
	}
	
	public boolean isUserCancel() {
		return m_bUserCancel;
	}
	
	public boolean isForwardToMemberEnquiry(){
		return m_bForwardToMemberEnquiry;
	}
	
	public PosInterfaceConfig getInterfaceConfig() {
		return m_oInterfaceConfig;
	}
	
	public String getMemberNumber() {
		return m_sMemberNumber;
	}
	
	public String getLastName() {
		return m_sLastName;
	}
	
	@Override
	public void FrameMembershipRegistration_clickOK(HashMap<String, String> oEnquiryInfo) {
		m_oFuncMembershipInterface = new FuncMembershipInterface(m_oPosInterfaceConfig);
		
		if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2)) {
			// set the language as EN if the field is empty and no setup
			if(oEnquiryInfo.get("preferredLanguage").isEmpty()
					&& m_oPosInterfaceConfig.getInterfaceConfig().has("general_setup")
					&& m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").has("default_language")
					&& m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").optJSONObject("default_language").optString("value").isEmpty()) {
				oEnquiryInfo.put("preferredLanguage", "EN");
			}
			
			// get contest number from db for validation
			UserUser oEmployee = new UserUser();
			oEmployee.readByUserNumber(AppGlobal.g_oFuncUser.get().getUserNumber());
			
			if(oEmployee.getModuleInfoByModuleAliasAndVariable("woh", "contest_number") != null 
					&& !oEnquiryInfo.get("contestNumber").isEmpty()
					&& !oEnquiryInfo.get("contestNumber").equals(oEmployee.getModuleInfoByModuleAliasAndVariable("woh", "contest_number"))) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("incorrect_contest_number"));
				oFormDialogBox.show();
				return;
			}
		}
		
		if (m_oFuncMembershipInterface.membershipRegistration(oEnquiryInfo) == false){
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._(m_oFuncMembershipInterface.getLastErrorMessage()));
			oFormDialogBox.show();
			
			AppGlobal.g_oTerm.get().hideKeyboard();
			return;
		}

		// Prompt message box to notice user that registration is successful (except golden cricle member ship interface mobile view)
		boolean bPromptSuccessMessage = true;
		String sMessage = AppGlobal.g_oLang.get()._("membership_registration_succeeded");
		if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GOLDEN_CIRCLE)) {
			if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
				bPromptSuccessMessage = false;
			else
				sMessage = AppGlobal.g_oLang.get()._("successfully_enrolled");
		} else if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2)) {
			JSONObject oJSONObject = new JSONObject();
			try {
				oJSONObject.put("memberNumber", m_oFuncMembershipInterface.getLastMemberInfo().get("memberNumber"));
				oJSONObject.put("memberName", m_oFuncMembershipInterface.getLastMemberInfo().get("memberName"));
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
				sMessage = e.getMessage();
			}
			m_oFuncMembershipInterface.processPrintMembershipActionSlip(oJSONObject, PosActionPrintQueue.KEY_MEMBER_ENROLLMENT);
		}

		FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
		if (bPromptSuccessMessage) {
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("notice"));
			oFormDialogBox.setMessage(sMessage);
			oFormDialogBox.show();
		}
		
		// Only close frame for Ascentis
		//if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASCENTIS_CRM)){
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())
				|| (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GOLDEN_CIRCLE) && m_oFrameMembershipRegistration.isFromOrderingPanel())) { 
			this.finishShow();
			AppGlobal.g_oTerm.get().hideKeyboard();
		}
		//}
		if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GOLDEN_CIRCLE)){
			if (m_oFrameMembershipRegistration.isFromOrderingPanel())
				m_bForwardToMemberEnquiry = true;
			m_oInterfaceConfig = m_oPosInterfaceConfig;
			m_sLastName = m_oFuncMembershipInterface.getLastMemberInfo().get("lastName");
			m_sMemberNumber = m_oFuncMembershipInterface.getLastMemberInfo().get("memberNumber");
			
			if (!m_oFrameMembershipRegistration.isFromOrderingPanel() && AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				//do member list enquiry
				HashMap<String, String> oMemberListEnquiryInfo = new HashMap<String, String>();
				oMemberListEnquiryInfo.put("memberNumber", m_sMemberNumber);
				oMemberListEnquiryInfo.put("surname", m_sLastName);
				if (m_oFuncMembershipInterface.memberListEquiry(oMemberListEnquiryInfo) == false){
					oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(m_oFuncMembershipInterface.getLastErrorMessage());
					oFormDialogBox.show();
					return;
				}
				
				//get member number then do card enquiry
				HashMap<String, String> oCardListInfo = new HashMap<String, String>();
				oCardListInfo.put("memberNumber", m_oFuncMembershipInterface.getMemberList().get(0).sMemberNumber);
				if (!m_oFuncMembershipInterface.cardEnquiry(oCardListInfo)) {
					oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(m_oFuncMembershipInterface.getLastErrorMessage());
					oFormDialogBox.show();
					return;
				}
				
				m_oFrameMembershipRegistration.showMemberResult_GoldenCircle(m_oFuncMembershipInterface.getLastMemberInfo());
			}
		} else if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2)){
			m_bForwardToMemberEnquiry = true;
			m_oInterfaceConfig = m_oPosInterfaceConfig;
			m_sMemberNumber = m_oFuncMembershipInterface.getLastMemberInfo().get("memberNumber");
			m_sLastName = m_oFuncMembershipInterface.getLastMemberInfo().get("lastName");
		}
	}

	@Override
	public void FrameMembershipRegistration_clickCancel() {
		// TODO Auto-generated method stub
		m_bUserCancel = true;
		
		// Finish showing this form
		this.finishShow();
		
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}
	
	@Override
	public void FrameMembershipRegistration_valueChanged(String sValue) {
		// TODO Auto-generated method stub
		m_oFrameMembershipRegistration.updateMemberNumber(sValue);
	}
	
	@Override
	public void FrameMembershipRegistration_clickPrint(HashMap<String, String> m_oCurrentMemberInfo) {
		// TODO Auto-generated method stub
		JSONObject oJSONObject = new JSONObject();
		try {
			oJSONObject.put("memberNumber", m_oCurrentMemberInfo.get("memberNumber"));
			oJSONObject.put("memberName", m_oCurrentMemberInfo.get("lastName") + ", " + m_oCurrentMemberInfo.get("firstName"));
			oJSONObject.put("memberTier", m_oCurrentMemberInfo.get("memberType"));
			
			if (m_oCurrentMemberInfo.get("status").equals("false")) {
				oJSONObject.put("memberCurrentPoint", m_oCurrentMemberInfo.get("pointsBalance"));
				oJSONObject.put("memberEquivCurrentPoint", m_oCurrentMemberInfo.get("pointsLocal"));
				oJSONObject.put("memberExpiringPoint", m_oCurrentMemberInfo.get("pointsExpiring"));
				oJSONObject.put("memberEquivExpiringPoint", m_oCurrentMemberInfo.get("pointsExpiringLocal"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if (m_oFuncMembershipInterface.processPrintMembershipActionSlip(oJSONObject, PosActionPrintQueue.KEY_MEMBER_BALANCE)) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("notice"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("successfully_printed"));
			oFormDialogBox.show();
		}
	}
	
	@Override
	public void FrameMembershipRegistration_clickAttachToTable() {
		PosOutletTableList oPosOutletTableList = AppGlobal.getOutletTableList(AppGlobal.g_oFuncOutlet.get().getOutletId());
		//ArrayList<Pair<PosCheck, PosOutletTable>> oTableList = new ArrayList<Pair<PosCheck, PosOutletTable>>();
		ArrayList<FrameOpenedCheckList.OutletTable> oOutletTableList = new ArrayList<FrameOpenedCheckList.OutletTable>();
		ArrayList<PosCheck> oPosChecks = new ArrayList<PosCheck>();
		ArrayList<PosOutletTable> oPosOutletTables = new ArrayList<PosOutletTable>();
		
		for (Map.Entry<PosOutletTable, PosCheck> entry : oPosOutletTableList.getOutletTableList().entrySet()) {
			PosCheck oPosCheck = entry.getValue();
			PosOutletTable oPosOutletTable = entry.getKey();
			if(oPosCheck != null) {
				FrameOpenedCheckList.OutletTable oOuteletTable = m_oFrameOpenedCheckList.getOutletTable(oPosCheck, oPosOutletTable);
				oOutletTableList.add(oOuteletTable);
				oPosChecks.add(oPosCheck);
				oPosOutletTables.add(oPosOutletTable);
				//Pair<PosCheck, PosOutletTable> oTempList = new Pair<PosCheck, PosOutletTable>(oPosCheck, oPosOutletTable);
				//oTableList.add(oTempList);
			}
		}
		// sorting the target function in sequence
		
		Collections.sort(oOutletTableList, new Comparator<FrameOpenedCheckList.OutletTable>() {
			public int compare(FrameOpenedCheckList.OutletTable oTableListArray1, FrameOpenedCheckList.OutletTable oTableListArray2) {
				return oTableListArray1.oPosOutletTable.getTable() - oTableListArray2.oPosOutletTable.getTable();
			}
		});
		
		m_oFrameOpenedCheckList.initCheckListForAttachTable(oOutletTableList);
		m_oFrameOpenedCheckList.setVisible(true);
		m_oFrameOpenedCheckList.bringToTop();
	}
	
	@Override
	public void FrameOpenedCheckList_ButtonExitClicked() {
		m_oFrameOpenedCheckList.setVisible(false);
	}
	
	@Override
	public void FrameOpenedCheckList_RecordClicked(int iTable, String sTableExtension) {
		for (FormMembershipRegistrationListener listener : listeners) {
			// Raise the event to parent
			if(listener.FormMembershipRegistration_openTable(iTable, sTableExtension, m_oFrameMembershipRegistration.getRegisteredMemberNumber(), m_oFrameMembershipRegistration.getRegisteredMemberSurname(), m_oPosInterfaceConfig.getInterfaceId())) {
				this.finishShow();
			}
		}
	}
	
	@Override
	public void FrameOpenedCheckList_RecordLongClicked(int iTable, String sTableExtension) {
	}
}
