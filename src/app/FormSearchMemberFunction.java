package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import commonui.FormDialogBox;
import commonui.FormSelectionBox;
import core.Controller;
import om.InfInterface;
import om.InfVendor;
import om.MemMember;
import om.MemMemberList;
import om.OmWsClientGlobal;
import om.PosCheck;
import om.PosCheckExtraInfo;
import om.PosInterfaceConfig;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormSearchMemberFunction extends VirtualUIForm implements FrameSearchMemberFunctionListener {
	private TemplateBuilder m_oTemplateBuilder;
	
	private FrameSearchMemberFunction m_oFrameSearchMemberFunction;
	
	private ArrayList<MemMember> m_oResultMemberList;
	
	// Cache full detail member list
	private HashMap<Integer, MemMember> m_oCacheFullDetailMemberList;
	
	// Check information
	HashMap<String, String> m_oCheckInfo;
	
	// Member that is going to assign to check
	private MemMember m_oAssignedMember;
	private List<PosCheckExtraInfo> m_oAssignedInterfaceMember;
	
	private FuncCheck m_oFuncCheck;
	
	private String m_sMemberSearchMode;
	
	// Online member interface
	private PosInterfaceConfig m_oPosInterfaceConfig;
	
	// Internal setup
	private boolean m_bHaveMemberInterface;
	private boolean m_bAskMemberForPayment;
	private FuncMembershipInterface m_oFuncMembershipInterface;
	private boolean m_bValidateWithMembModule;
		
	// User select option
	// 0: Cancel
	// 1: Set member
	// 2: Clear member
	public static final int SELECT_CANCEL = 0;
	public static final int SELECT_SET_MEMBER = 1;
	public static final int SELECT_CLEAR_MEMBER = 2;
	public static final int SELECT_CANCEL_SELECT_INTERFACE = 3;
	private int m_iUserSelectOptionType;
	
	public static enum SEARCH_MODE{normal, employeeMember, loyaltyMember, companyAccount};
	
	// Max no. of member of search result
	private static Integer MAX_RESULT_COUNT = 100;

	public FormSearchMemberFunction(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmSearchMemberFunction.xml");
		
		m_oResultMemberList = new ArrayList<MemMember>();
		m_oCacheFullDetailMemberList = new HashMap<Integer, MemMember>();
		m_oCheckInfo = new HashMap<String, String>();
		
		m_bHaveMemberInterface = false;
		m_bAskMemberForPayment = false;
		m_oPosInterfaceConfig = null;
		m_oFuncMembershipInterface = null;
		
		m_oAssignedMember = null;

		m_oFuncCheck = null;
		m_sMemberSearchMode = FormSearchMemberFunction.SEARCH_MODE.normal.name();
		m_oAssignedInterfaceMember = null;
		
		m_iUserSelectOptionType = SELECT_CANCEL;
	}
	
	public boolean init(FuncCheck oFuncCheck, List<PosCheckExtraInfo> oInterfaceMemberInfos, HashMap<String, String> oCheckInfo, int iMembershipIntfIdForPayment,  boolean bShowOptions, boolean bBySetMember, boolean bShowClearMemberButton, String sTitle, String sEsignatureImg, String sMemberSearchMode) {
		m_oFuncCheck = oFuncCheck;
		m_sMemberSearchMode = sMemberSearchMode;
		
		
		//Get member validation setting
		String sMemberValidateSetting = AppGlobal.g_oFuncStation.get().getMemberValidationSetting();
		m_bValidateWithMembModule = true;
		if(!sMemberValidateSetting.isEmpty()) {
			try {
				JSONObject jsonObject = new JSONObject(sMemberValidateSetting);
				if(jsonObject.optString("no_member_validation_in_set_member", "").equals("y"))
					m_bValidateWithMembModule = false;
			} catch (JSONException e) {
				e.printStackTrace();
				AppGlobal.stack2Log(e);
			}
		}
		
		//Show interface for user to select
		if(bShowOptions){
			//check whether have member interface attached to outlet
			m_bHaveMemberInterface = false;
			
			ArrayList<String> oVendors = new ArrayList<String>();
			oVendors.add(InfVendor.KEY_GENERAL);
			oVendors.add(InfVendor.KEY_ASPEN);
			PosInterfaceConfig oPosInterfaceConfig = null;
			String sMembershipTitle = AppGlobal.g_oLang.get()._("please_select_the_membership_interface");
			oPosInterfaceConfig = this.getInterfaceConfig(InfInterface.TYPE_MEMBERSHIP_INTERFACE, oVendors, sMembershipTitle);
			if(oPosInterfaceConfig != null){
				m_bHaveMemberInterface = true;
				m_oPosInterfaceConfig = oPosInterfaceConfig;
				m_oFuncMembershipInterface = new FuncMembershipInterface(m_oPosInterfaceConfig);
			}else{
				if(m_iUserSelectOptionType == SELECT_CANCEL_SELECT_INTERFACE)
					return false;	
			}
		}
		
		if(iMembershipIntfIdForPayment > 0) {
			List<PosInterfaceConfig> oPosInterfaceConfigs = AppGlobal.g_oPosInterfaceConfigList.get().getInterfaceConfigListByInterfaceType(InfInterface.TYPE_MEMBERSHIP_INTERFACE);
			for(PosInterfaceConfig oPosInterfaceConfig : oPosInterfaceConfigs) {
				if(oPosInterfaceConfig.getInterfaceId() == iMembershipIntfIdForPayment) {
					m_bHaveMemberInterface = true;
					m_oPosInterfaceConfig = oPosInterfaceConfig;
					m_oFuncMembershipInterface = new FuncMembershipInterface(m_oPosInterfaceConfig);
					break;
				}
			}
		}
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Cover Corner Frame
		VirtualUIFrame oCoverConerFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverConerFrame, "fraSearchMemberFunctionCoverCorner");
		this.attachChild(oCoverConerFrame);
		
		// Search Member Frame
		m_oFrameSearchMemberFunction = new FrameSearchMemberFunction();
		m_oTemplateBuilder.buildFrame(m_oFrameSearchMemberFunction, "fraSearchMemberFunction");
		boolean bMultipleSearchType = true;
		if (m_sMemberSearchMode.equals(FormSearchMemberFunction.SEARCH_MODE.loyaltyMember.name()))
			bMultipleSearchType = false;
		
		m_oFrameSearchMemberFunction.init(sTitle, bShowClearMemberButton, bMultipleSearchType, m_oPosInterfaceConfig, sEsignatureImg);

		// Add listener;
		m_oFrameSearchMemberFunction.addListener(this);
		this.attachChild(m_oFrameSearchMemberFunction);
		
		//Assign check info
		m_oCheckInfo = oCheckInfo;
		
		if(bBySetMember == false)
			m_bAskMemberForPayment = true;
		
		// If member is assigned before, go to detail page directly
		MemMember oMember = new MemMember();
		if (m_sMemberSearchMode.equals(FormSearchMemberFunction.SEARCH_MODE.loyaltyMember.name())) {
			String sLoyaltyMemberNo = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER);
			if (!sLoyaltyMemberNo.isEmpty()) {
				String sResult = oMember.searchLoyaltyMember(sLoyaltyMemberNo);
				if (sResult.isEmpty()) {
					// Add to result list
					m_oFrameSearchMemberFunction.addMemberToResultList(0, 0, oMember.getMemberNo(), oMember.getName());
					m_oResultMemberList.add(oMember);
					
					// Add to cache
					m_oCacheFullDetailMemberList.put(oMember.getMemberId(), oMember);
					
					// Show member detail page
					showMemberDetail(0, false, 0);
				}
			}
		} else {
			int iAssignedMemberId = m_oFuncCheck.getMemberId();
			if(iAssignedMemberId > 0) {
				if(oMember.readById(iAssignedMemberId)){
					// No need to assign member id in set member of payment function if member attached in check is not employee member type
					if(m_sMemberSearchMode.equals(FormSearchMemberFunction.SEARCH_MODE.employeeMember.name()) && !oMember.isEmployeeMember())
						return true;
					if(m_sMemberSearchMode.equals(FormSearchMemberFunction.SEARCH_MODE.companyAccount.name()) && !oMember.isCompanyMember())
						return true;
					// Add to result list
					m_oFrameSearchMemberFunction.addMemberToResultList(0, 0, oMember.getMemberNo(), oMember.getName());
					m_oResultMemberList.add(oMember);
					
					// Add to cache
					m_oCacheFullDetailMemberList.put(oMember.getMemberId(), oMember);
					
					// Show member detail page
					showMemberDetail(0, false, iAssignedMemberId);
				}else if(m_oPosInterfaceConfig != null && oMember.readNotDeletedById(iAssignedMemberId)){
					// Add to result list
					m_oFrameSearchMemberFunction.addMemberToResultList(0, 0, oMember.getMemberNo(), oMember.getName());
					m_oResultMemberList.add(oMember);
					
					// Add to cache
					m_oCacheFullDetailMemberList.put(oMember.getMemberId(), oMember);
					
					// Show member detail page
					showMemberDetail(0, false, iAssignedMemberId);
				}
			}else if(oInterfaceMemberInfos != null && oInterfaceMemberInfos.size() > 0) {
				int iInterfaceId = 0;
				String sMemberNumber = "", sCardNumber = "";
				for(PosCheckExtraInfo oInterfaceMemberInfo:oInterfaceMemberInfos) {
					if(oInterfaceMemberInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERFACE_ID) && oInterfaceMemberInfo.getValue() != null && !oInterfaceMemberInfo.getValue().isEmpty())
						iInterfaceId = Integer.valueOf(oInterfaceMemberInfo.getValue()).intValue();
					if(oInterfaceMemberInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER))
						sMemberNumber = oInterfaceMemberInfo.getValue();
					if(oInterfaceMemberInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_CARD_NO))
						sCardNumber = oInterfaceMemberInfo.getValue();
				}
				if(iInterfaceId > 0) {
					List<PosInterfaceConfig> oPosInterfaceConfigs = AppGlobal.g_oPosInterfaceConfigList.get().getInterfaceConfigListByInterfaceType(InfInterface.TYPE_MEMBERSHIP_INTERFACE);
					for(PosInterfaceConfig oPosInterfaceConfig : oPosInterfaceConfigs){
						if(oPosInterfaceConfig.getInterfaceId() == iInterfaceId) {
							m_bHaveMemberInterface = true;
							m_oPosInterfaceConfig = oPosInterfaceConfig;
							m_oFuncMembershipInterface = new FuncMembershipInterface(m_oPosInterfaceConfig);
							m_oAssignedInterfaceMember = oInterfaceMemberInfos;
							//search member
							if(oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL)){
								if(!sMemberNumber.isEmpty())
									searchMember(FrameSearchMemberFunction.SEARCH_TYPE.number.name(), sMemberNumber, true, 1);
								break;
							}else if(oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN)){
								if(!sCardNumber.isEmpty()){
									searchMember(FrameSearchMemberFunction.SEARCH_TYPE.cardNumber.name(), sCardNumber, true, 1);
								}	
								break;
							}
							
						}
					}
				}else if(!m_bValidateWithMembModule){
					sMemberNumber = "";
					String sExpiryDate = "";
					for(PosCheckExtraInfo oInterfaceMemberInfo:oInterfaceMemberInfos) {
						if(oInterfaceMemberInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER))
							sMemberNumber = oInterfaceMemberInfo.getValue();
						if(oInterfaceMemberInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_EXPIRY_DATE))
							sExpiryDate = oInterfaceMemberInfo.getValue();
					}
					
					if(sMemberNumber != null && !sMemberNumber.isEmpty()) {
						oMember = new MemMember(sMemberNumber, sExpiryDate);
						
						// Add to result list
						m_oFrameSearchMemberFunction.addMemberToResultList(0, 0, oMember.getMemberNo(), oMember.getName());
						m_oResultMemberList.add(oMember);
						
						showMemberDetail(0, false, 0);
					}
				}
			}
		}		
		return true;
	}

	private void searchMember(String sType, String sValue, boolean bRetrieveAssignedMember, int iPage){
		m_oResultMemberList.clear();
		m_oCacheFullDetailMemberList.clear();
		m_oAssignedMember = null;
		boolean bHasCardEnquiry = false;
		if (m_sMemberSearchMode.equals(FormSearchMemberFunction.SEARCH_MODE.loyaltyMember.name())) {
			// Connect to master server to get loyalty member information
			if (!AppGlobal.g_sMasterServerUrl.isEmpty() && !AppGlobal.g_oFuncUser.get().isLoginHq()) {
				String sLoginId = OmWsClientGlobal.g_oWsClient.get().getLoginId();
				String sPassword = OmWsClientGlobal.g_oWsClient.get().getPassword();
				String sResult = AppGlobal.g_oFuncUser.get().loginHQ(sLoginId, sPassword, false);
				if (!sResult.isEmpty()) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_connect_to_master_server") + System.lineSeparator()
									+ AppGlobal.g_oLang.get()._("fail_to_perform_loyalty_bonus_transaction"));
					oFormDialogBox.show();
					
					return;
				}
			}
			
			MemMember oMemMember = new MemMember();
			String sResult = oMemMember.searchLoyaltyMember(sValue);
			if (!sResult.isEmpty()) {
				// Error to get member detail
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(sResult);
				oFormDialogBox.show();
				
				return;
			}
			m_oFrameSearchMemberFunction.addMemberToResultList(0, 0, oMemMember.getMemberNo(), oMemMember.getName());
			m_oResultMemberList.add(oMemMember);
			m_oCacheFullDetailMemberList.put(oMemMember.getMemberId(), oMemMember);
		} else {
			//don't call external interface if value is empty
			if(sValue.isEmpty())
				return;
		
			// Call external interface to search member
			DateTime oCurrentTime = AppGlobal.getCurrentTime(false);
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMddHHmmss");
			String sCurrntTimeStamp = formatter.print(AppGlobal.convertTimeToUTC(oCurrentTime));
			//DateTimeFormatter oDateFormat = DateTimeFormat.forPattern("yyyyMMddHHmmss");
			HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();

			if(!m_bHaveMemberInterface) {
				// Using HERO's mem_members table
				if(m_bValidateWithMembModule) {
					MemMemberList oMemberList = new MemMemberList();
					String sMemberType = "";
					if (m_sMemberSearchMode.equals(FormSearchMemberFunction.SEARCH_MODE.employeeMember.name()))
						sMemberType = "e";
					else if (m_sMemberSearchMode.equals(FormSearchMemberFunction.SEARCH_MODE.companyAccount.name()))
						sMemberType = "c";
					if(m_oPosInterfaceConfig != null)
						oMemberList.searchMember(sType, sValue, sMemberType, iPage, MAX_RESULT_COUNT, MemMember.SEARCH_ALL_NOT_DELETED);
					else
						oMemberList.searchMember(sType, sValue, sMemberType, iPage, MAX_RESULT_COUNT, MemMember.SEARCH_ALL_ACTIVE);
					HashMap<Integer, MemMember> oResultMemberList = oMemberList.getMemberList();
					
					int iIndex = 0;
					for(Entry<Integer, MemMember> entry:oResultMemberList.entrySet()){
						MemMember oMemMember = entry.getValue();
						m_oFrameSearchMemberFunction.addMemberToResultList(0, iIndex, oMemMember.getMemberNo(), oMemMember.getName());
						m_oResultMemberList.add(oMemMember);
						
						iIndex++;
					}
				}else{
					String sExpiryDate = "";
					if(!m_oFrameSearchMemberFunction.getSwipeCardExpiryDateString().isEmpty())
						sExpiryDate = m_oFrameSearchMemberFunction.getSwipeCardExpiryDateString();
					
					MemMember oMemMember = new MemMember(sValue, sExpiryDate);
					m_oFrameSearchMemberFunction.addMemberToResultList(0, 0, sValue, "");
					m_oResultMemberList.add(oMemMember);
				}
			}else {
				// Call external interface to search member
//				DateTime oCurrentTime = new DateTime();
//				DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMddHHmmss");
//				String sCurrntTimeStamp = formatter.print(oCurrentTime.withZone(DateTimeZone.UTC));
//				SimpleDateFormat oDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
//				HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();
				if(m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL)){
					oEnquiryInfo.put("businessDate", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInStringWithFormat("yyyy-MM-dd"));
					if(m_oCheckInfo.containsKey("checkId"))
						oEnquiryInfo.put("checkId", m_oCheckInfo.get("checkId"));
					else
						oEnquiryInfo.put("checkId",	"0");
					if(m_oCheckInfo.containsKey("checkNumber"))
						oEnquiryInfo.put("checkNumber", m_oCheckInfo.get("checkNumber"));
					else
						oEnquiryInfo.put("checkNumber", "");
					if(m_oCheckInfo.containsKey("checkAmount"))
						oEnquiryInfo.put("checkAmount", m_oCheckInfo.get("checkAmount"));
					else
						oEnquiryInfo.put("checkAmount", "");
					String sTaceId = formatter.print(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDate()).toString()+sCurrntTimeStamp.toString();
					oEnquiryInfo.put("traceId", sTaceId);
					if(sType.equals(FrameSearchMemberFunction.SEARCH_TYPE.cardNumber.name())) {
						oEnquiryInfo.put("memberNumber", "");
						oEnquiryInfo.put("cardNumber", sValue);
					}else if(sType.equals(FrameSearchMemberFunction.SEARCH_TYPE.number.name())) {
						oEnquiryInfo.put("memberNumber", sValue);
						oEnquiryInfo.put("cardNumber", "");
					}
				}else if(m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN)){
					oEnquiryInfo.put("memberNumber", "");
					oEnquiryInfo.put("surname", "");
					oEnquiryInfo.put("givenName", "");
					oEnquiryInfo.put("chineseName", "");
					oEnquiryInfo.put("cardNumber", "");
					if(sType.equals(FrameSearchMemberFunction.SEARCH_TYPE.number.name()))
						oEnquiryInfo.put("memberNumber", sValue);
					else if(sType.equals(FrameSearchMemberFunction.SEARCH_TYPE.name.name())){
						if(!sValue.isEmpty()){
							String [] sNameList = sValue.split(",", -1);
							oEnquiryInfo.put("surname", sNameList[0]);
							oEnquiryInfo.put("givenName", sNameList[1]);
							oEnquiryInfo.put("chineseName", sNameList[2]);
						}
					}
					else if(sType.equals(FrameSearchMemberFunction.SEARCH_TYPE.cardNumber.name()))
						oEnquiryInfo.put("cardNumber", sValue);
					else if(sType.equals(FrameSearchMemberFunction.SEARCH_TYPE.phone.name()))
						oEnquiryInfo.put("mobileNumber", sValue);
				}
				
				boolean bMemberEnquiryResult = true;
				if(m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL)){
					if(m_oFuncMembershipInterface.memberEnquiry(oEnquiryInfo)){
						if(m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberNumber.isEmpty() == false)
							m_oFrameSearchMemberFunction.addMemberToResultList(0, 0, 
								m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberNumber, 
								m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberName);
						else {
							FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
							oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
							oFormDialogBox.setMessage(m_oFuncMembershipInterface.getLastErrorMessage());
							oFormDialogBox.show();
							return;
						}
					}else
						bMemberEnquiryResult = false;
				}else if(m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN)){
					if(sType.equals(FrameSearchMemberFunction.SEARCH_TYPE.cardNumber.name())){
						bHasCardEnquiry = true;
						if(m_oFuncMembershipInterface.cardEnquiry(oEnquiryInfo)){
							if(m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberNumber.isEmpty() == false){
								if(m_oFrameSearchMemberFunction.getBasketResultList().getItemCount(0) == 0)
									m_oFrameSearchMemberFunction.addMemberToResultList(0, 0, 
										m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberNumber, 
										m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberName);
								//show info directly
								m_oFrameSearchMemberFunction.showOnlineMemberDetail(m_oFuncMembershipInterface.getLastMemberInfo());
							}else {
								FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
								oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
								oFormDialogBox.setMessage(m_oFuncMembershipInterface.getLastErrorMessage());
								oFormDialogBox.show();
								return;
							}
						}else
							bMemberEnquiryResult = false;
					}else{
						if(m_oFuncMembershipInterface.memberListEquiry(oEnquiryInfo)){
							if(m_oFuncMembershipInterface.getMemberList().size() > 0){
								for(int i=0; i<m_oFuncMembershipInterface.getMemberList().size(); i++){
									FuncMembershipInterface.MemberInterfaceResponseInfo oResponseInfo = m_oFuncMembershipInterface.getMemberList().get(i);
									m_oFrameSearchMemberFunction.addMemberToResultList(0, i, oResponseInfo.sMemberNumber,oResponseInfo.sMemberName);
								}
							}else {
								FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
								oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
								oFormDialogBox.setMessage(m_oFuncMembershipInterface.getLastErrorMessage());
								oFormDialogBox.show();
								return;
							}
						}else 
							bMemberEnquiryResult = false;
					}
				}
				
				if(!bMemberEnquiryResult){
					if(m_oFuncMembershipInterface.isConnectionError()) {
						//connection error
						//change to offline mode according setup
						if(!bRetrieveAssignedMember) {
							JSONObject oInterfaceSetup = m_oPosInterfaceConfig.getInterfaceConfig().optJSONObject("switch_offline_setting");
							if(m_bAskMemberForPayment){
								if(oInterfaceSetup != null && oInterfaceSetup.optJSONObject("params").optJSONObject("payment").optString("value", "0").equals("0")){
									FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
									oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
									oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("unable_to_connect_member_server"));
									oFormDialogBox.show();
								}else if(oInterfaceSetup != null && oInterfaceSetup.optJSONObject("params").optJSONObject("payment").optString("value", "0").equals("1")){
									m_bHaveMemberInterface = false;
									FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
									oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
									oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("unable_to_connect_member_server_change_to_offline_member_mode"));
									oFormDialogBox.show();
									m_oFrameSearchMemberFunction.switchSearchingMode(FrameSearchMemberFunction.SEARCH_MODE.offline.name());
									frameSearchMemberFunction_clickSearch(sType, sValue);
								}
								return;
							}
							
							if(oInterfaceSetup != null && oInterfaceSetup.optJSONObject("params").optJSONObject("enquiry").optString("value", "0").equals("1")) {
								m_bHaveMemberInterface = false;
								FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
								oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
								oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("unable_to_connect_member_server_change_to_offline_member_mode"));
								oFormDialogBox.show();
								m_oFrameSearchMemberFunction.switchSearchingMode(FrameSearchMemberFunction.SEARCH_MODE.offline.name());
								frameSearchMemberFunction_clickSearch(sType, sValue);
							}else {
								FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
								oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
								oFormDialogBox.setMessage(m_oFuncMembershipInterface.getLastErrorMessage());
								oFormDialogBox.show();
							}
							return;
						}
					}else {
						//other errors 
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(m_oFuncMembershipInterface.getLastErrorMessage());
						oFormDialogBox.show();
						return;
					}
				}
			}
		}
		
		boolean bShowFirstOnlineMemberDetail = false;
		if(m_bHaveMemberInterface){
			if(m_oFuncMembershipInterface.getMemberList() != null && !bHasCardEnquiry){
				if(m_oFuncMembershipInterface.getMemberList().size() == 1)
					if(!m_oFuncMembershipInterface.getMemberList().get(0).sCardNumber.isEmpty())
						bShowFirstOnlineMemberDetail = true;
			}else{
				if(m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo != null && !m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberNumber.isEmpty() && !bHasCardEnquiry)
					bShowFirstOnlineMemberDetail = true;
			}
		}
		
		// If only 1 record is found, go to detail page directly
		if(m_oResultMemberList.size() == 1 || bShowFirstOnlineMemberDetail)
			showMemberDetail(0, false, 0);
		else if(bRetrieveAssignedMember && m_oFuncMembershipInterface.isConnectionError())
			showMemberDetail(0, true, 0);
		
		//show clear member button
		if (!m_bAskMemberForPayment && bRetrieveAssignedMember)
			showClearMemberButton();
		
	}
	
	private void showMemberDetail(int iIndex, boolean bShowAssignedInterfaceMember, int iAssignedMemberId){
		if(m_bHaveMemberInterface && iAssignedMemberId == 0 && m_oResultMemberList.size() == 0) {
			if(bShowAssignedInterfaceMember) {
				HashMap<String, String> oMembeInfo = new HashMap<String, String>();
				//general info.
				oMembeInfo.put("memberNumber", "");
				oMembeInfo.put("memberName", "");
				oMembeInfo.put("memberType", "");
				if(m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL)){
					oMembeInfo.put("cardNumber", "");
					oMembeInfo.put("cardTypeName", "");
					oMembeInfo.put("arAccountNumber", "");
					oMembeInfo.put("creditLimit", "");
					oMembeInfo.put("creditUsage", "");
				}else if(m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN)){
					oMembeInfo.put("salutation", "");
					oMembeInfo.put("gender", "");
					oMembeInfo.put("status", "");
					oMembeInfo.put("birthday", "");
					oMembeInfo.put("memberStatus", "");
					oMembeInfo.put("expiryDate", "");
				}
				
				for(PosCheckExtraInfo oPosCheckExtraInfo:m_oAssignedInterfaceMember) {
					String sInfoValue = (oPosCheckExtraInfo.getValue() != null) ? oPosCheckExtraInfo.getValue() : "";
					if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER))
						oMembeInfo.put("memberNumber", sInfoValue);
					else if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_ACCOUNT_NAME))
						oMembeInfo.put("memberName", sInfoValue);
					else if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER))
						oMembeInfo.put("memberNumber", sInfoValue);
					else if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_MEMBER_NAME))
						oMembeInfo.put("memberName", sInfoValue);
					else if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_CARD_NO))
						oMembeInfo.put("cardNumber", sInfoValue);
					else if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_AR_ACCOUNT_NUMBER))
						oMembeInfo.put("arAccountNumber", sInfoValue);
				}
				
				m_oFrameSearchMemberFunction.showOnlineMemberDetail(oMembeInfo);
				
				if(m_bAskMemberForPayment) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("unable_to_connect_member_server"));
					oFormDialogBox.show();
				}
			}else{
				if(m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL))
					m_oFrameSearchMemberFunction.showOnlineMemberDetail(m_oFuncMembershipInterface.getLastMemberInfo());
				else if(m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN)){
					String sCardNumber = "";
					if(m_oFuncMembershipInterface.getMemberList() != null){
						if(!m_oFuncMembershipInterface.getMemberList().isEmpty())
							sCardNumber = m_oFuncMembershipInterface.getMemberList().get(iIndex).sCardNumber;
					}
					if(sCardNumber.isEmpty()){
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("missing_card_number")
								+ System.lineSeparator() + AppGlobal.g_oLang.get()._("cannot_find_member_detail"));
						oFormDialogBox.show();
						return;
					}
					frameSearchMemberFunction_clickSearch(FrameSearchMemberFunction.SEARCH_TYPE.cardNumber.name(), sCardNumber);
				}
			}	
		}else {
			if(iIndex >= m_oResultMemberList.size())
				return;
			
			MemMember oMember = null;
			
			// Check if there is only 1 record is found or not
			if(m_oResultMemberList.size() == 1){
				// If only 1 record is found, no need to retrieve the member detail again
				oMember = m_oResultMemberList.get(0);
			}else{
				// Retrieve the detail from OM
				oMember = m_oResultMemberList.get(iIndex);
			}
			
			// Try to read from cache
			if(this.m_bValidateWithMembModule && oMember.getMemberId() > 0) {
				if(m_oCacheFullDetailMemberList.containsKey(oMember.getMemberId())){
					oMember = m_oCacheFullDetailMemberList.get(oMember.getMemberId());
				}else{			
					if(m_oPosInterfaceConfig != null){
						if(oMember.readNotDeletedById(oMember.getMemberId()) == false){
							// Error to get member detail
							FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
							oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
							oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_retrieve_member_detail"));
							oFormDialogBox.show();
							
							return;
						}
					}else{
						if(oMember.readById(oMember.getMemberId()) == false){
							// Error to get member detail
							FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
							oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
							oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_retrieve_member_detail"));
							oFormDialogBox.show();
							
							return;
						}
					}
					
					m_oCacheFullDetailMemberList.put(oMember.getMemberId(), oMember);
				}
			}
			m_oFrameSearchMemberFunction.showMemberDetail(oMember);
			
			// Set the member to be assigned to check if set member button is clicked
			m_oAssignedMember = oMember;
			m_oAssignedInterfaceMember = null;
		}
	}
	
	
	public void showClearMemberButton(){
		m_oFrameSearchMemberFunction.showClearMemberButton();
	}
	
	public boolean isInterfaceConnectionError(){
		return m_oFuncMembershipInterface.isConnectionError();
	}
	
	public void setUserInputType(int iUserSelectOptionType) {
		this.m_iUserSelectOptionType = iUserSelectOptionType;
	}
	
	public int getUserInputType(){
		return m_iUserSelectOptionType;
	}
	
	public MemMember getAssignedMember(){
		return m_oAssignedMember;
	}
	
	public HashMap<String, String> getAssignedInterfaceMember(){
		if(m_oFuncMembershipInterface == null)
			return null;
		else
			return m_oFuncMembershipInterface.getLastMemberInfo();
	}
	
	public PosInterfaceConfig getMemberInterface() {
		return m_oPosInterfaceConfig;
	}
	
	public boolean isValidateMemberModule() {
		return this.m_bValidateWithMembModule;
	}
	
	public boolean compareLastCheckClosedTimeByMemberId(DateTime oDataTimeForNextCheck) {
		if(oDataTimeForNextCheck == null)
			return true;
		DateTime oCurrentDateTime = AppGlobal.getCurrentTime(true);
		if (oDataTimeForNextCheck.isAfter(oCurrentDateTime))
			return false;
		else
			return true;
	}
	
	public DateTime getLastCheckClosedTimeByMemberId(int iMemberId , int iOpenNextCheckInterval) {
		if (iOpenNextCheckInterval <= 0)
			return null;
		
		PosCheck oPervioudSettledCheck = new PosCheck();
		oPervioudSettledCheck.readLastClosedCheckByMemberId(iMemberId, AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
		if (oPervioudSettledCheck.getCloseTime() != null && !oPervioudSettledCheck.getCloseTime().isEmpty()) {
			DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
			DateTime oCheckCloseTimeTemp = oFormatter.parseDateTime(oPervioudSettledCheck.getCloseTime());
			DateTime oCheckCloseTime= new DateTime(oCheckCloseTimeTemp.getYear(),oCheckCloseTimeTemp.getMonthOfYear(),oCheckCloseTimeTemp.getDayOfMonth(),oCheckCloseTimeTemp.getHourOfDay(),oCheckCloseTimeTemp.getMinuteOfHour(),oCheckCloseTimeTemp.getSecondOfMinute(),oCheckCloseTimeTemp.getMillisOfSecond(),DateTimeZone.UTC); 
			DateTime oTargetTimeCanOpenNewCheck = new DateTime(oCheckCloseTime.plusMinutes(iOpenNextCheckInterval));
			
			return oTargetTimeCanOpenNewCheck;
		}
		return null;
	}
	
	//check Interface Config
	private PosInterfaceConfig getInterfaceConfig(String sInterfaceType, List<String> oVendors, String sTitle){
		List<PosInterfaceConfig> oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(sInterfaceType);
		List<PosInterfaceConfig> oPosInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		PosInterfaceConfig oTargetPosInterfaceConfig = null;
		if(!AppGlobal.isModuleSupport(AppGlobal.OPTIONAL_MODULE.pos_interface.name()))
			return null;
		
		if(oInterfaceConfigList.isEmpty())
			return null;

		for(PosInterfaceConfig oPosInterfaceConfig : oInterfaceConfigList) {
			for(String sVendor : oVendors){
				if(oPosInterfaceConfig.getInterfaceVendorKey().equals(sVendor))
					oPosInterfaceConfigList.add(oPosInterfaceConfig);
			}
		}
		
		if(oPosInterfaceConfigList.isEmpty())
			return null;
		
		if(oPosInterfaceConfigList.size() == 1)
			oTargetPosInterfaceConfig = oPosInterfaceConfigList.get(0);
		else {
			ArrayList<String> oOptionList = new ArrayList<String>();
			HashMap<Integer, HashMap<String, String>> oInterfaceIdList = new HashMap<Integer, HashMap<String, String>>();
			int iInterfaceCount = 0;
			
			for(PosInterfaceConfig oInterfaceConfig : oPosInterfaceConfigList) {
				oOptionList.add(oInterfaceConfig.getInterfaceName(AppGlobal.g_oCurrentLangIndex.get()));
				HashMap<String, String> oTempInterfaceInfo = new HashMap<String, String>();
				oTempInterfaceInfo.put("interfaceId", String.valueOf(oInterfaceConfig.getInterfaceId()));
				oInterfaceIdList.put(iInterfaceCount, oTempInterfaceInfo);
				iInterfaceCount++;
			}
			
			FormSelectionBox oFormSelectionBox = new FormSelectionBox(this);
			oFormSelectionBox.initWithSingleSelection(sTitle, oOptionList, false);
			oFormSelectionBox.show();
			
			if(oFormSelectionBox.isUserCancel()){
				m_iUserSelectOptionType = SELECT_CANCEL_SELECT_INTERFACE;
				return null;
			}
			else {
				HashMap<String, String> oTempInterfaceInfo = oInterfaceIdList.get(oFormSelectionBox.getResultList().get(0));
				int iInterfaceId = Integer.valueOf(oTempInterfaceInfo.get("interfaceId"));
				for(PosInterfaceConfig oInterfaceConfig : oPosInterfaceConfigList){
					if(oInterfaceConfig.getInterfaceId() == iInterfaceId)
						oTargetPosInterfaceConfig = oInterfaceConfig;
				}
			}
		}
		return oTargetPosInterfaceConfig;
	}

	public boolean checkSwipeCard() {
		return m_oFrameSearchMemberFunction.isSwipeCard();
	}
	
	@Override
	public void frameSearchMemberFunction_clickCancel() {
		// Clear assigned member
		m_oAssignedMember = null;
		m_oAssignedInterfaceMember = null;
		
		// Set the user select type
		m_iUserSelectOptionType = SELECT_CANCEL;
		
		// Finish showing this form
		this.finishShow();
	}

	@Override
	public void frameSearchMemberFunction_clickSetMember() {	
		// Set the user select type
		m_iUserSelectOptionType = SELECT_SET_MEMBER;
		
		// Finish showing this form
		this.finishShow();
	}

	@Override
	public void frameSearchMemberFunction_clickClearMember() {
		// Set the user select type
		m_iUserSelectOptionType = SELECT_CLEAR_MEMBER;
		
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public void frameSearchMemberFunction_clickSearch(String sType,	String sValue) {
		// Search member
		// In phase 1, always get the first page, so page = 1
		searchMember(sType, sValue, false, 1);
	}

	@Override
	public void frameSearchMemberFunction_clickSearchResultRecord(int iIndex) {
		// Show member detail
		showMemberDetail(iIndex, false, 0);
	}

	@Override
	public PosInterfaceConfig frameSearchMemberFunction_getPosInterfaceConfig(String sInterfaceType, ArrayList<String> sVendors, String sTitle) {
		// TODO Auto-generated method stub
		return this.getInterfaceConfig(sInterfaceType, sVendors, sTitle);
	}
}
