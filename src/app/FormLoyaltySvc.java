package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import commonui.FormInputBox;
import commonui.FormListMessageBox;
import commonui.FormSelectionBox;
import core.Controller;
import om.InfInterface;
import om.InfVendor;
import om.MemMember;
import om.MemMemberList;
import om.PosCheckDiscount;
import om.PosCheckExtraInfo;
import om.PosInterfaceConfig;
import om.PosPaymentMethod;
import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FormLoyaltySvcListener {
	void formLoyaltySvc_hotItemClicked(String sId, ArrayList<PosCheckExtraInfo> oPosCheckExtraInfoList, BigDecimal oFixPrice); 
}

public class FormLoyaltySvc extends VirtualUIForm implements FrameLoyaltySvcListener {
	// User select option
	// 0: Cancel
	// 1: Set member
	// 2: Clear member
	public static final int SELECT_OPTION_CANCEL = 0;
	public static final int SELECT_OPTION_SET_MEMBER = 1;
	public static final int SELECT_OPTION_CLEAR_MEMBER = 2;
	public static final int SELECT_OPTION_CANCEL_SELECT_INTERFACE = 3;
	
	public static final String ACTION_ISSUE_CARD = "issue_card";
	public static final String ACTION_ADD_VALUE = "add_value";
	public static final String ACTION_CHECK_VALUE = "check_value";
	public static final String ACTION_SUSPEND_CARD = "suspend_card";
	public static final String ACTION_TRANSFER_CARD = "transfer_card";
	
	private TemplateBuilder m_oTemplateBuilder;
	private FrameLoyaltySvc m_oFrameLoyaltySvc;
	private ArrayList<MemMember> m_oResultMemberList;
	
	// Check information
	HashMap<String, String> m_oCheckInfo;
	
	private FuncLoyaltySvc m_oParentLoyaltySvc;	// For issue bonus card checking
	private FuncLoyaltySvc m_oAssignedLoyaltySvc;	// Member that is going to assign to check
	
	private FuncCheck m_oFuncCheck;
	private FuncPayment m_oFuncPayment;
	private boolean m_bSupportSVCMember;
	
	// Online member interface
	private PosInterfaceConfig m_oPosInterfaceConfig;
	
	// Internal setup
	private boolean m_bHaveMemberInterface;
	private boolean m_bAskMemberForPayment;
	private FuncMembershipInterface m_oFuncMembershipInterface;
	
	private int m_iUserSelectOptionType;
	
	// Max no. of member of search result
	private static Integer MAX_RESULT_COUNT = 100;
	
	private BigDecimal m_oTotalDebit;
	private String m_sInterfaceId = "";
	private String m_sSvcInterfaceId = "";
	private boolean m_bIsSvcCheckValue = false;
	private String m_sAction = FormLoyaltySvc.ACTION_ISSUE_CARD;
	private String m_sTitle = "";
	private int m_iIssueCardCount = 1;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FormLoyaltySvcListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FormLoyaltySvcListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FormLoyaltySvcListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }

	public FormLoyaltySvc(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		m_oTemplateBuilder.loadTemplate("frmLoyaltySvc.xml");
		
		m_oResultMemberList = new ArrayList<MemMember>();
		m_oCheckInfo = new HashMap<String, String>();
		
		m_bHaveMemberInterface = false;
		m_bAskMemberForPayment = false;
		m_oPosInterfaceConfig = null;
		m_oFuncMembershipInterface = null;
		
		m_oAssignedLoyaltySvc = null;
		m_oParentLoyaltySvc = null;

		m_oFuncCheck = null;
		m_oTotalDebit = BigDecimal.ZERO;
		m_iUserSelectOptionType = SELECT_OPTION_CANCEL;
	}
	
	//initialize the object
	public boolean init(String sType, String sTitle, FuncCheck oFuncCheck, FuncPayment oFuncPayment) {
		listeners = new ArrayList<FormLoyaltySvcListener>();
		m_oAssignedLoyaltySvc = new FuncLoyaltySvc();
		m_oFuncCheck = oFuncCheck;
		m_oFuncPayment = oFuncPayment;
		m_sAction = sType;
		m_sTitle = sTitle;
		
		// To restore the member information which has assigned to the check
		if(m_sInterfaceId.isEmpty()){
			List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
			oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_LOYALTY_INTERFACE);
			for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList) {
				if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY)) {
					m_oPosInterfaceConfig = oPosInterfaceConfig;
					m_sInterfaceId = oPosInterfaceConfig.getInterfaceId() + "";
					break;
				}
			}
			if(m_sInterfaceId.isEmpty()){
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("no_interface_setup"));
				oFormDialogBox.show();
				return false;
			}
		}
		
		if(m_sSvcInterfaceId.isEmpty()){
			List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
			oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_LOYALTY_INTERFACE);
			for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList) {
				if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY_SVC)) {
					m_oPosInterfaceConfig = oPosInterfaceConfig;
					m_sSvcInterfaceId = oPosInterfaceConfig.getInterfaceId() + "";
					break;
				}
			}
			if(m_sSvcInterfaceId.isEmpty()){
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("no_interface_setup"));
				oFormDialogBox.show();
				return false;
			}
		}
		
		if (AppGlobal.g_oFuncStation.get().getLoyaltySvcSessionId().isEmpty()) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_connect_to_loyalty_svc_server") + System.lineSeparator() + AppGlobal.g_oLang.get()._("fail_to_perform_svc_bonus_transaction"));
			oFormDialogBox.show();
			return false;
		}
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// If there is no member number, show the number pad
		m_oFrameLoyaltySvc = new FrameLoyaltySvc();
		m_oTemplateBuilder.buildFrame(m_oFrameLoyaltySvc, "fraLoyaltySvcNumPad");
		m_oFrameLoyaltySvc.initNumberPad(m_sAction, sTitle);
		
		// Add listener;
		m_oFrameLoyaltySvc.addListener(this);
		this.attachChild(m_oFrameLoyaltySvc);
		
		if(m_oTotalDebit.equals(BigDecimal.ZERO)){
			for(int i = 0; i <= AppGlobal.MAX_SEATS; i++) {
				// Create ordering basket section for seat
				ArrayList<FuncCheckItem> oFuncCheckItemList = (ArrayList<FuncCheckItem>)oFuncCheck.getItemList(i);
				if(oFuncCheckItemList != null && oFuncCheckItemList.size() > 0) {					//Has ordered items in seat i
					for (int j=0; j<oFuncCheckItemList.size(); j++) {
						FuncCheckItem oFuncCheckItem = oFuncCheckItemList.get(j);
						for(PosCheckExtraInfo oPosCheckExtraInfo:oFuncCheckItem.getExtraInfoList()){
							if(oPosCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_LOYALTY_SVC) &&
									oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_BENEFIT_AMOUNT) &&
									oPosCheckExtraInfo.getValue() != null)
								m_oTotalDebit = m_oTotalDebit.add(new BigDecimal(oPosCheckExtraInfo.getValue()));
						}
					}
				}
			}
			
			for(PosCheckDiscount oPosCheckDiscount:m_oFuncCheck.getCurrentPartyAppliedCheckDiscount()){
				BigDecimal oBenefitAmount = BigDecimal.ZERO;
				BigDecimal oBenefitUsed = BigDecimal.ONE;
				for(PosCheckExtraInfo oPosCheckExtraInfo:oPosCheckDiscount.getCheckExtraInfoList()){
					if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_BENEFIT_QTY)){
						if(oPosCheckExtraInfo.getValue() == null || oPosCheckExtraInfo.getValue() == "")
							oBenefitUsed = BigDecimal.ONE;
						else
							oBenefitUsed = new BigDecimal(oPosCheckExtraInfo.getValue());
					}
					
					if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_BENEFIT_AMOUNT)){
						if(oPosCheckExtraInfo.getValue() == null || oPosCheckExtraInfo.getValue() == "")
							oBenefitAmount = BigDecimal.ZERO;
						else
							oBenefitAmount = new BigDecimal(oPosCheckExtraInfo.getValue());
					}
				}
				m_oTotalDebit = m_oTotalDebit.add(oBenefitAmount.multiply(oBenefitUsed));
			}
		}
		
		return true;
	}
	
	public boolean initGiveX(String sType, String sTitle, FuncCheck oFuncCheck, FuncPayment oFuncPayment){
		listeners = new ArrayList<FormLoyaltySvcListener>();
		m_oAssignedLoyaltySvc = new FuncLoyaltySvc();
		m_oFuncCheck = oFuncCheck;
		m_oFuncPayment = oFuncPayment;
		m_sAction = sType;
		m_sTitle = sTitle;
		
		m_oAssignedLoyaltySvc.setInterfaceConfig(InfVendor.KEY_GIVEX);
		
		// If there is activate card function
		if(sType.equals(FrameLoyaltySvc.ACTION_ISSUE_CARD)){
			m_oFrameLoyaltySvc = new FrameLoyaltySvc();
			m_oTemplateBuilder.buildFrame(m_oFrameLoyaltySvc, "fraLoyaltySvcNumPad");
			m_oFrameLoyaltySvc.initNumberPad(m_sAction, sTitle);
			
			// Add listener;
			m_oFrameLoyaltySvc.addListener(this);
			this.attachChild(m_oFrameLoyaltySvc);
		}
		return true;
	}
	
	public void setInterfaceConfig(PosInterfaceConfig oPosInterfaceConfig){
		m_oPosInterfaceConfig = oPosInterfaceConfig;
	}
	
	//search member
	private void searchMember(String sType, String sValue, String sPassword, FuncLoyaltySvc oAssignedLoyaltySvc){
		m_oResultMemberList.clear();
		if(oAssignedLoyaltySvc == null)
			oAssignedLoyaltySvc = new FuncLoyaltySvc();
		
		// Connect to master server to get SVC member information
		if (AppGlobal.g_oFuncStation.get().getLoyaltySessionId().isEmpty()) {
			if(m_sInterfaceId.isEmpty()){
				List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
				oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_LOYALTY_INTERFACE);
				
				for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList) {
					if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY)) {
						m_sInterfaceId = oPosInterfaceConfig.getInterfaceId() + "";
						break;
					}
				}
			}
			
			if (AppGlobal.g_oFuncStation.get().getLoyaltySessionId().isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_connect_to_loyalty_svc_server") + System.lineSeparator() + AppGlobal.g_oLang.get()._("fail_to_perform_loyalty_bonus_transaction"));
				oFormDialogBox.show();
				
				return;
			}
		}
		
		if (AppGlobal.g_oFuncStation.get().getLoyaltySvcSessionId().isEmpty()) {
			if(m_sSvcInterfaceId.isEmpty()){
				List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
				oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_LOYALTY_INTERFACE);
				
				for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList) {
					if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY_SVC)) {
						m_sSvcInterfaceId = oPosInterfaceConfig.getInterfaceId() + "";
						break;
					}
				}
			}
			
			if (AppGlobal.g_oFuncStation.get().getLoyaltySvcSessionId().isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_connect_to_loyalty_svc_server") + System.lineSeparator() + AppGlobal.g_oLang.get()._("fail_to_perform_svc_bonus_transaction"));
				oFormDialogBox.show();
				
				return;
			}
		}
		
		JSONObject oResultJSONObject = oAssignedLoyaltySvc.svcSearchCardNumber(sValue, sPassword, m_sInterfaceId, m_sSvcInterfaceId, m_oFuncCheck, 1);
		if(oResultJSONObject == null){
			String sErrorMessage = oAssignedLoyaltySvc.getLastErrorMessage();
			if(!sErrorMessage.isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(sErrorMessage);
				oFormDialogBox.show();
				oFormDialogBox = null;
			}
			return;
		}
		
		try {
			oAssignedLoyaltySvc.readDataFromJson(oResultJSONObject);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	//show member details
	private void showMemberDetail(FuncLoyaltySvc oFuncLoyaltySvc){
		m_oFrameLoyaltySvc.showMemberDetail(oFuncLoyaltySvc);
		
		// Update Total Debit
		m_oFrameLoyaltySvc.updateDebit(m_oTotalDebit, oFuncLoyaltySvc.getBalance());
		
		// Set the member to be assigned to check if set member button is clicked
		m_oAssignedLoyaltySvc = oFuncLoyaltySvc;
	}
	
	//do suspend card
	private boolean suspendCard(String sRemarks){
		// Connect to master server to get SVC member information
		if (AppGlobal.g_oFuncStation.get().getLoyaltySvcSessionId().isEmpty()) {
			if(m_sSvcInterfaceId.isEmpty()){
				List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
				oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_LOYALTY_INTERFACE);
				
				for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList) {
					if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY_SVC)) {
						m_sSvcInterfaceId = oPosInterfaceConfig.getInterfaceId() + "";
						break;
					}
				}
			}
			
			if (AppGlobal.g_oFuncStation.get().getLoyaltySvcSessionId().isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_connect_to_loyalty_svc_server") + System.lineSeparator() + AppGlobal.g_oLang.get()._("fail_to_perform_svc_bonus_transaction"));
				oFormDialogBox.show();
				
				return false;
			}
		}
		
		// preparing "suspend card" posting fields
		HashMap<String, String> oSuspendCardInfo = null;
		oSuspendCardInfo = new HashMap<String, String>();
		
		oSuspendCardInfo.put("svcInterfaceId", m_sSvcInterfaceId);
		oSuspendCardInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oSuspendCardInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oSuspendCardInfo.put("svcSessionId", AppGlobal.g_oFuncStation.get().getLoyaltySvcSessionId());
		
		oSuspendCardInfo.put("svcCardNumber", m_oAssignedLoyaltySvc.getCardNo());
		oSuspendCardInfo.put("cardStatus", "S");
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		oSuspendCardInfo.put("cardSuspendDate", fmt.print(today));
		oSuspendCardInfo.put("cardSuspendRemarks", sRemarks);
		oSuspendCardInfo.put("cardSuspendEmpId", AppGlobal.g_oFuncUser.get().getUserNumber());

		PosInterfaceConfig oTempPosInterfaceConfig = new PosInterfaceConfig();
		return oTempPosInterfaceConfig.doLoyaltySvcSuspendCard(oSuspendCardInfo);
	}
	
	//do 
	private void transferCard(FuncLoyaltySvc oAssignedLoyaltySvc, FuncLoyaltySvc oFuncLoyaltySvc, String sReason){
		// Connect to master server to get SVC member information
		if (AppGlobal.g_oFuncStation.get().getLoyaltySessionId().isEmpty()) {
			if(m_sInterfaceId.isEmpty()){
				List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
				oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_LOYALTY_INTERFACE);
				
				for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList) {
					if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY)) {
						m_sInterfaceId = oPosInterfaceConfig.getInterfaceId() + "";
						break;
					}
				}
			}
			
			if (AppGlobal.g_oFuncStation.get().getLoyaltySessionId().isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_connect_to_loyalty_server") + System.lineSeparator() + AppGlobal.g_oLang.get()._("fail_to_perform_loyalty_bonus_transaction"));
				oFormDialogBox.show();
				
				return;
			}
		}
		
		if (AppGlobal.g_oFuncStation.get().getLoyaltySvcSessionId().isEmpty()) {
			if(m_sSvcInterfaceId.isEmpty()){
				List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
				oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_LOYALTY_INTERFACE);
				
				for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList) {
					if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY_SVC)) {
						m_sSvcInterfaceId = oPosInterfaceConfig.getInterfaceId() + "";
						break;
					}
				}
			}
			
			if (AppGlobal.g_oFuncStation.get().getLoyaltySvcSessionId().isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_connect_to_loyalty_svc_server") + System.lineSeparator() + AppGlobal.g_oLang.get()._("fail_to_perform_svc_bonus_transaction"));
				oFormDialogBox.show();
				
				return;
			}
		}
		
		if (!oAssignedLoyaltySvc.transferCard(oAssignedLoyaltySvc, oFuncLoyaltySvc, m_sInterfaceId, m_sSvcInterfaceId, sReason)) {
			String sErrorMessage = oAssignedLoyaltySvc.getLastErrorMessage();
			if(!sErrorMessage.isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(sErrorMessage);
				oFormDialogBox.show();
				oFormDialogBox = null;
			}
		}else {
			String sMessage = AppGlobal.g_oLang.get()._("loyalty_svc_transfer_card") + System.lineSeparator()
					+ AppGlobal.g_oLang.get()._("success_to_transfer_card") + System.lineSeparator()
					+ AppGlobal.g_oLang.get()._("original_card_no") + " : " + m_oAssignedLoyaltySvc.getCardNo()
					+ System.lineSeparator() + AppGlobal.g_oLang.get()._("new_card_no") + " : "
					+ oFuncLoyaltySvc.getCardNo();
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormDialogBox.setMessage(sMessage);
			oFormDialogBox.show();
			this.finishShow();
		}
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
				m_iUserSelectOptionType = SELECT_OPTION_CANCEL_SELECT_INTERFACE;
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
	
	public PosInterfaceConfig frameLoyaltySvc_getLoyaltyInterfaceConfig(){
		return m_oPosInterfaceConfig;
	}
	
	//is swipe card
	public boolean checkSwipeCard() {
		return m_oFrameLoyaltySvc.isSwipeCard();
	}
	
	//set SVC check value display
	public void setSvcCheckValueDisplay(){
		m_bIsSvcCheckValue = true;
	}
	
	/***********************/
	/*	Helper Function    */
	/***********************/
	private void showDialogBox(String sTitle, String sMessage) {
		if (sMessage.isEmpty())
			return;
		
		FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
		oFormDialogBox.setTitle(sTitle);
		oFormDialogBox.setMessage(sMessage);
		oFormDialogBox.show();
	}
	
	@Override
	public void frameLoyaltySvc_clickCancel() {
		// Clear assigned member
		m_oAssignedLoyaltySvc = null;
		m_oTotalDebit = BigDecimal.ZERO;
		
		// Set the user select type
		m_iUserSelectOptionType = SELECT_OPTION_CANCEL;
		
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public void frameLoyaltySvc_clickConfirmAddValue() {
		FormSelectionBox oFormSelectionBox = new FormSelectionBox(this);
		m_oAssignedLoyaltySvc.selectCardType();
		JSONObject sInterfaceConfig = m_oAssignedLoyaltySvc.getInterfaceConfig();
		ArrayList<String> oCardTypeOptionList = new ArrayList<String>();
		ArrayList<Integer> oSelectionBoxResult;
		HashMap<Integer, Integer> oCardTypeMap = new HashMap<Integer, Integer>();
		ArrayList<String> oOptionList = new ArrayList<String>();
		String[] sLanguage;
		int iCounter = 0;
		
		if(m_oAssignedLoyaltySvc.getProfileIndex() == 0) {
			for(int i = 0; i < 10 ; i++) {
				String sCurrentProfile;
				sCurrentProfile = "card_type"+(i+1);
				if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("desc_lang")) {
					String sLangDesc = sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").optJSONObject("desc_lang").optString("value");
					if(!sLangDesc.equals("")) {
						sLanguage = sLangDesc.split(",");
						oCardTypeOptionList.add(sLanguage[AppGlobal.g_oCurrentLangIndex.get()-1]);	
						oCardTypeMap.put(iCounter, i+1);
						iCounter++;
					}
				}
			}
			
			FormSelectionBox oFormCardTypeSelectionBox = new FormSelectionBox(this);
			oFormCardTypeSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("svc_card_type"), oCardTypeOptionList, false);
			oFormCardTypeSelectionBox.showAndWait();
			oFormCardTypeSelectionBox.show();
			if (oFormCardTypeSelectionBox.isUserCancel())
				return;
			else {
				oSelectionBoxResult = oFormCardTypeSelectionBox.getResultList();
				m_oAssignedLoyaltySvc.setProfileIndex(oCardTypeMap.get(oSelectionBoxResult.get(0)));
			}
		}
				
		if(!m_oAssignedLoyaltySvc.isIsAllowAddValue()) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_add_value"));
			oFormDialogBox.show();
			oFormDialogBox = null;
			this.finishShow();
			return;
		}
		
		//Add extrainfo to list
		ArrayList<PosCheckExtraInfo> oPosCheckExtraInfoList = m_oAssignedLoyaltySvc.setAddValueExtraInfo(m_sSvcInterfaceId, m_oFuncCheck.getCheckId(), FuncLoyaltySvc.CARD_TYPE_SVC, "TU", true, -1, null, "");
		for(int i = 0; i < m_oAssignedLoyaltySvc.getAddValueDesc().length ; i++){
			if(m_oAssignedLoyaltySvc.getAddValueDesc()[i][m_oAssignedLoyaltySvc.getLangIndex()-1] != null && !m_oAssignedLoyaltySvc.getAddValueDesc()[i][m_oAssignedLoyaltySvc.getLangIndex()-1].equals(""))
				oOptionList.add(m_oAssignedLoyaltySvc.getAddValueDesc()[i][AppGlobal.g_oCurrentLangIndex.get()-1]);
		}
		
		oFormSelectionBox = new FormSelectionBox(this);
		oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("svc_add_value"), oOptionList, false);
		oFormSelectionBox.showAndWait();
		oFormSelectionBox.show();
		if (oFormSelectionBox.isUserCancel()) 
			return;
		
		oSelectionBoxResult = oFormSelectionBox.getResultList();
		BigDecimal oIssueValue;
		oIssueValue = m_oAssignedLoyaltySvc.getAddValue()[oSelectionBoxResult.get(0)];
		
		BigDecimal oValueAfterAdd = oIssueValue.add(m_oAssignedLoyaltySvc.getSVCBalance());
		if(m_oAssignedLoyaltySvc.getMaxAllowedAmount().compareTo(oValueAfterAdd) < 0) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("excess_maximum_amount"));
			oFormDialogBox.show();
			oFormDialogBox = null;
			return;
		}
		
		for (FormLoyaltySvcListener listener : listeners) {
			listener.formLoyaltySvc_hotItemClicked(m_oAssignedLoyaltySvc.getAddValueItemCode(), oPosCheckExtraInfoList, oIssueValue);
			break;
		}
		
		this.finishShow();
	}
	
	@Override
	public void frameLoyaltySvc_clickConfirmIssueCard() {
		m_oAssignedLoyaltySvc.selectCardType();
		JSONObject sInterfaceConfig = m_oAssignedLoyaltySvc.getInterfaceConfig();
		ArrayList<String> oCardTypeOptionList = new ArrayList<String>();
		HashMap<Integer, Integer> oCardTypeMap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> oIssueValueMap = new HashMap<Integer, Integer>();
		ArrayList<String> oOptionList = new ArrayList<String>();
		String[] sLanguage;
		int iCounter = 0;
		
		if(m_oAssignedLoyaltySvc.getProfileIndex() == 0) {
			for(int i = 0; i < 10 ; i++) {
				String sCurrentProfile;
				sCurrentProfile = "card_type"+(i+1);
				if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("desc_lang")) {
					String sLangDesc = sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").optJSONObject("desc_lang").optString("value");
					if(!sLangDesc.equals("")) {
						sLanguage = sLangDesc.split(",");
						oCardTypeOptionList.add(sLanguage[m_oAssignedLoyaltySvc.getLangIndex()-1]);	
						oCardTypeMap.put(iCounter, i+1);
						iCounter++;
					}
				}
			}
			
			FormSelectionBox oFormCardTypeSelectionBox = new FormSelectionBox(this);
			oFormCardTypeSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("svc_card_type"), oCardTypeOptionList, false);
			oFormCardTypeSelectionBox.showAndWait();
			oFormCardTypeSelectionBox.show();
			if (oFormCardTypeSelectionBox.isUserCancel())
				return;
			else {
				ArrayList<Integer> oSelectionBoxResult = oFormCardTypeSelectionBox.getResultList();
				m_oAssignedLoyaltySvc.setProfileIndex(oCardTypeMap.get(oSelectionBoxResult.get(0)));
			}
		}
		
		if(m_oAssignedLoyaltySvc.getProfileIndex() != 0) {
			if(m_iIssueCardCount == 2 && m_oParentLoyaltySvc != null) {
				if(!m_oAssignedLoyaltySvc.isbIsAllowIssueAsBonusCard()) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_issue_such_card_type"));
					oFormDialogBox.show();
					oFormDialogBox = null;
					this.finishShow();
					return;	
				}
			}
			
			iCounter = 0;
			oIssueValueMap.clear();
			for(int i = 0; i < m_oAssignedLoyaltySvc.getIssueValueDesc().length; i++) {
				if(m_oAssignedLoyaltySvc.getIssueValueDesc()[i][m_oAssignedLoyaltySvc.getLangIndex()-1] != null && !m_oAssignedLoyaltySvc.getIssueValueDesc()[i][m_oAssignedLoyaltySvc.getLangIndex()-1].equals(""))
				{
					oOptionList.add(m_oAssignedLoyaltySvc.getIssueValueDesc()[i][m_oAssignedLoyaltySvc.getLangIndex()-1]);	
					oIssueValueMap.put(iCounter, i);
					iCounter++;
				}
			}
			
			if(iCounter == 0) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_issue_such_card_type"));
				oFormDialogBox.show();
				oFormDialogBox = null;
				this.finishShow();
				return;	
			}
			
			FormSelectionBox oFormSelectionBox = new FormSelectionBox(this);
			oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("svc_issue_card"), oOptionList, false);
			oFormSelectionBox.showAndWait();
			oFormSelectionBox.show();
			if (oFormSelectionBox.isUserCancel())
				return;
			else {
				//Check if The card type need to set associate member
				String[] sGiftCardAssociateMemberMapping = m_oAssignedLoyaltySvc.getCardTypeAssociateMemberMapping();
				boolean bNeedAssociateMember = false;
				if (sGiftCardAssociateMemberMapping != null) {
					for (String sProfileIndex : sGiftCardAssociateMemberMapping)
						if (sProfileIndex.equals(String.valueOf(m_oAssignedLoyaltySvc.getProfileIndex()))) {
							bNeedAssociateMember = true;
							break;
						}
				}
				
				//If Yes, Get loyalty set up, do member enquiry
				if (bNeedAssociateMember) {
					PosInterfaceConfig oLoyaltyInterfaceConfig = null;
					List<PosInterfaceConfig> oLoyaltyInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
					oLoyaltyInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_LOYALTY_INTERFACE);
					
					for (PosInterfaceConfig oPosInterfaceConfig : oLoyaltyInterfaceConfigList) {
						if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY)) {
							oLoyaltyInterfaceConfig = oPosInterfaceConfig;
							break;
						}
					}
					
					if (oLoyaltyInterfaceConfig == null) {
						this.finishShow();
						return;
					}
					
					String sErrMsg = "";
					if (AppGlobal.g_oFuncStation.get().getLoyaltySessionId().isEmpty()) {
						if (oLoyaltyInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY))
							sErrMsg = AppGlobal.g_oLang.get()._("not_login_to_loyalty_server");
						showDialogBox(AppGlobal.g_oLang.get()._("error"), sErrMsg);
						this.finishShow();
						return;
					}
					
					//ask for member no to associate
					FormInputBox oFormInputBox = new FormInputBox(this);
					oFormInputBox.init();
					oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.DEFAULT);
					oFormInputBox.showKeyboard();
					oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("loyalty_svc_issue_card"));
					if(oLoyaltyInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY))
						oFormInputBox.setMessage(AppGlobal.g_oLang.get()._("please_input_member_number") + ":");
					oFormInputBox.show();
					
					if (oFormInputBox.isUserCancel()) {
						this.finishShow();
						return;
					}
					
					//validation on input
					String sAssociateMemberNumber = oFormInputBox.getInputValue();
					if (oFormInputBox.getInputValue().isEmpty()) {
						sErrMsg = "";
						if(oLoyaltyInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY))
							sErrMsg = AppGlobal.g_oLang.get()._("not_allow_blank_member_number");
						showDialogBox(AppGlobal.g_oLang.get()._("error"), sErrMsg);
						this.finishShow();
						return;
					}
					
					//GM loyalty MSR checking
					if (oLoyaltyInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY)) {
						String sCreditCardContent = oFormInputBox.getSwipeCardValue();
						boolean bSwipeCard = false;
						if (!sCreditCardContent.isEmpty()) {
							String sMsrCode = "";
							JSONObject oPosInterfaceConfigJson = new JSONObject();
							oPosInterfaceConfigJson = oLoyaltyInterfaceConfig.getInterfaceConfig();
							if (oPosInterfaceConfigJson.has("general_setup") && oPosInterfaceConfigJson.optJSONObject("general_setup").has("params") && oPosInterfaceConfigJson.optJSONObject("general_setup").optJSONObject("params").has("msr_code"))
								sMsrCode = oPosInterfaceConfigJson.optJSONObject("general_setup").optJSONObject("params").optJSONObject("msr_code").optString("value");
							// Get the configure from interface module
							List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
							oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PERIPHERAL_DEVICE);
							for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList) {
								if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_MSR) && oPosInterfaceConfig.getInterfaceCode().equals(sMsrCode)){
									if (!sCreditCardContent.isEmpty()) {
										// Capture information from swipe card
										FuncMSR oFuncMSR = new FuncMSR();
										if (oFuncMSR.processCardContent(sCreditCardContent, oPosInterfaceConfig.getInterfaceConfig()) == FuncMSR.ERROR_CODE_NO_ERROR) {
											// Get the necessary value
											sAssociateMemberNumber = oFuncMSR.getCardNo();
											bSwipeCard = true;
										} else {
											// Fail to process swipe card
											showDialogBox(AppGlobal.g_oLang.get()._("error"), oFuncMSR.getLastErrorMessage());
											this.finishShow();
											return;
										}
									}
								}
							}
							
							if (sAssociateMemberNumber.isEmpty()) {
								showDialogBox(AppGlobal.g_oLang.get()._("error"), AppGlobal.g_oLang.get()._("fail_to_capture_card"));
								this.finishShow();
								return;
							}
						}else{
							if (oLoyaltyInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY)) {
								String sKeyInControls[] = FuncLoyalty.getKeyInControls();
								if (!sAssociateMemberNumber.isEmpty() && sKeyInControls != null && sKeyInControls.length >= 2) {
									if (Integer.parseInt(sKeyInControls[1]) == 1) {
										showDialogBox(AppGlobal.g_oLang.get()._("error"), AppGlobal.g_oLang.get()._("support_swipe_card_only"));
										this.finishShow();
										return;
									}
								}
							}
						}
					}
					
					// Do member enquiry
					MemMemberList oMemberList = new MemMemberList();
					oMemberList.searchLoyaltyMember(oLoyaltyInterfaceConfig.getInterfaceId(), sAssociateMemberNumber, null);
					
					// Check if enquiry result have exactly one member return
					if (oMemberList.getLoyaltyMemberList().size() != 1) {
						showDialogBox(AppGlobal.g_oLang.get()._("error"), AppGlobal.g_oLang.get()._("fail_to_perform_gm_loyalty_member_search"));
						this.finishShow();
						return;
					}
					
					// Show member enquiry result
					FormListMessageBox oFormListMessageBox = new FormListMessageBox(true, AppGlobal.g_oLang.get()._("confirm"), this);
					ArrayList<String> sMessage = new ArrayList<>();
					
					oFormListMessageBox.setTitle(AppGlobal.g_oLang.get()._("member_detail"));
					oFormListMessageBox.addColumnHeader(AppGlobal.g_oLang.get()._("general_information"), 458);
					oFormListMessageBox.addColumnHeader(AppGlobal.g_oLang.get()._("value"), 458);
					
					oFormListMessageBox.setCloseButtonVisible(true);
					
					sMessage.add(AppGlobal.g_oLang.get()._("member_no"));
					sMessage.add(oMemberList.getLoyaltyMemberList().get(0).getMemberNo());
					oFormListMessageBox.addMessage(sMessage);
					
					sMessage.clear();
					sMessage.add(AppGlobal.g_oLang.get()._("member_name"));
					sMessage.add(oMemberList.getLoyaltyMemberList().get(0).getName());
					oFormListMessageBox.addMessage(sMessage);
					
					sMessage.clear();
					sMessage.add(AppGlobal.g_oLang.get()._("type"));
					sMessage.add(oMemberList.getLoyaltyMemberList().get(0).getType());
					oFormListMessageBox.addMessage(sMessage);
					
					sMessage.clear();
					sMessage.add(AppGlobal.g_oLang.get()._("bonus_balance"));
					sMessage.add(oMemberList.getLoyaltyMemberList().get(0).getBonusBalance());
					oFormListMessageBox.addMessage(sMessage);
					
					sMessage.clear();
					sMessage.add(AppGlobal.g_oLang.get()._("last_visit_date"));
					sMessage.add(oMemberList.getLoyaltyMemberList().get(0).getLastVisitDate());
					oFormListMessageBox.addMessage(sMessage);
					
					oFormListMessageBox.show();
					
					if (!AppGlobal.g_oLang.get()._("confirm").equals(oFormListMessageBox.getResult())) {
						this.finishShow();
						return;
					}
					
					m_oAssignedLoyaltySvc.setAssociateMemberNo(oMemberList.getLoyaltyMemberList().get(0).getMemberNo());
				}
				
				String sCardSellerId = "" ,sBuyerName = "", sBuyerPassport = "", sBuyerContactNo ="";
				
				ArrayList<Integer> oSelectionBoxResult = oFormSelectionBox.getResultList();
				//iChoice = oSelectionBoxResult.get(0);
				BigDecimal oIssueValue;
				String sIssueCardLevel;
				oIssueValue = m_oAssignedLoyaltySvc.getIssueValue()[oIssueValueMap.get(oSelectionBoxResult.get(0))];
				sIssueCardLevel = m_oAssignedLoyaltySvc.getDefaultCardLevel()[oIssueValueMap.get(oSelectionBoxResult.get(0))];
				BigDecimal oValueAfterAdd = oIssueValue.add(m_oAssignedLoyaltySvc.getSVCBalance());
				
				if(m_oAssignedLoyaltySvc.getMaxAllowedAmount().compareTo(oValueAfterAdd) < 0) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("excess_maximum_amount"));
					oFormDialogBox.show();
					this.finishShow();
					return;
				}
				FormInputBox oFormInputBox = new FormInputBox(this);
				if(m_iIssueCardCount == 2 && m_oParentLoyaltySvc != null) {
					m_oAssignedLoyaltySvc.setCardSeller(m_oParentLoyaltySvc.getCardSeller());	
					m_oAssignedLoyaltySvc.setCardBuyerName(m_oParentLoyaltySvc.getBuyerName());
					m_oAssignedLoyaltySvc.setCardBuyerPassportId(m_oParentLoyaltySvc.getBuyerPassportId());
					m_oAssignedLoyaltySvc.setCardBuyerContactNumber(m_oParentLoyaltySvc.getBuyerContactNumber());
				}
				else {
					oFormInputBox.init();
					oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("issue_svc_card"));
					oFormInputBox.setMessage(AppGlobal.g_oLang.get()._("please_input_card_seller_id"));
					oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.DEFAULT);
					oFormInputBox.showKeyboard();
					oFormInputBox.show();
					
					if(oFormInputBox.getInputValue() != null)
						sCardSellerId = oFormInputBox.getInputValue();
					if(!sCardSellerId.equals(""))
						m_oAssignedLoyaltySvc.setCardSeller(sCardSellerId);		
					

					oFormInputBox.init();
					oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("issue_svc_card"));
					oFormInputBox.setMessage(AppGlobal.g_oLang.get()._("please_input_buyer_name"));
					oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.DEFAULT);
					oFormInputBox.showKeyboard();
					oFormInputBox.show();
					
					if(oFormInputBox.getInputValue() != null)
						sBuyerName = oFormInputBox.getInputValue();
					if(!sBuyerName.equals(""))
						m_oAssignedLoyaltySvc.setCardBuyerName(sBuyerName);
					oFormInputBox.init();
					oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("issue_svc_card"));
					oFormInputBox.setMessage(AppGlobal.g_oLang.get()._("please_input_buyer_passport"));
					oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.DEFAULT);
					oFormInputBox.showKeyboard();
					oFormInputBox.show();
					
					if(oFormInputBox.getInputValue() != null)
						sBuyerPassport = oFormInputBox.getInputValue();
					if(!sBuyerPassport.equals(""))
						m_oAssignedLoyaltySvc.setCardBuyerPassportId(sBuyerPassport);
					oFormInputBox.init();
					oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("issue_svc_card"));
					oFormInputBox.setMessage(AppGlobal.g_oLang.get()._("please_input_buyer_contact_no"));
					oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.NUMBER);
					oFormInputBox.showKeyboard();
					oFormInputBox.show();
				}
				
				if(oFormInputBox.getInputValue() != null)
					sBuyerContactNo = oFormInputBox.getInputValue();
				if(!sBuyerContactNo.equals(""))
					m_oAssignedLoyaltySvc.setCardBuyerContactNumber(sBuyerContactNo);
				if(m_iIssueCardCount == 2 && m_oParentLoyaltySvc != null) {
					if(m_oAssignedLoyaltySvc.getMemberNo().equals("")) {
						m_oAssignedLoyaltySvc.setLoyaltyNumber(m_oParentLoyaltySvc.getMemberNo());
						m_oAssignedLoyaltySvc.setMemberValidThrough("BONUS_CARD");
					}
				}
				
				//Add extrainfo to list
				ArrayList<PosCheckExtraInfo> oPosCheckExtraInfoList = null;
				HashMap<Integer, PosPaymentMethod> oPaymentMethodList = m_oFuncPayment.getPaymentMethodList().getPaymentMethodList();
				int iPaymentId = -1;
				for(PosPaymentMethod oPaymentMethod : oPaymentMethodList.values()){
					if(m_iIssueCardCount == 2 && m_oParentLoyaltySvc != null){
						if(m_oAssignedLoyaltySvc.getChildDefaultPaymentCode().length > oIssueValueMap.get(oSelectionBoxResult.get(0))) {
							if(oPaymentMethod.getPaymentCode().equals(m_oAssignedLoyaltySvc.getChildDefaultPaymentCode()[oIssueValueMap.get(oSelectionBoxResult.get(0))])) {
								iPaymentId = oPaymentMethod.getPaymId();
								break;
							}
						}
					}
					else{
						if(m_oAssignedLoyaltySvc.getDefaultPaymentCode().length > oIssueValueMap.get(oSelectionBoxResult.get(0))) {
							if(oPaymentMethod.getPaymentCode().equals(m_oAssignedLoyaltySvc.getDefaultPaymentCode()[oIssueValueMap.get(oSelectionBoxResult.get(0))])) {
								iPaymentId = oPaymentMethod.getPaymId();
								break;
							}
						}
					}
				}
				
				oPosCheckExtraInfoList = m_oAssignedLoyaltySvc.setAddValueExtraInfo(m_sSvcInterfaceId, m_oFuncCheck.getCheckId(), FuncLoyaltySvc.CARD_TYPE_SVC, "IS", false, iPaymentId, oIssueValue, sIssueCardLevel);
				
				for (FormLoyaltySvcListener listener : listeners) {
					listener.formLoyaltySvc_hotItemClicked(m_oAssignedLoyaltySvc.getItemCode(), oPosCheckExtraInfoList, oIssueValue);
					break;
				}
				
				if(m_oAssignedLoyaltySvc.isIsAllowIssueBonusCard() && m_iIssueCardCount == 1) {
					FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
					oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
					oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("want_to_issue_bonus_card")+"?");
					oFormConfirmBox.show();
					if (oFormConfirmBox.isOKClicked()) {
						m_iIssueCardCount++;
						m_oParentLoyaltySvc = m_oAssignedLoyaltySvc;
						m_oAssignedLoyaltySvc = new FuncLoyaltySvc();
						m_oAssignedLoyaltySvc.init();
						m_oFrameLoyaltySvc.initNumberPad(m_sAction, m_sTitle);
					}
					else
						this.finishShow();
				}
				else
					this.finishShow();
				return;
			}
		}

	}
	
	@Override
	public void frameLoyaltySvc_clickConfirmSuspendCard() {
		// input suspend reason
		FormInputBox oFormInputBox = new FormInputBox(this);
		
		do {
			oFormInputBox.init();
			oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("suspend_svc_card"));
			oFormInputBox.setMessage(AppGlobal.g_oLang.get()._("please_input_reason"));
			oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.DEFAULT);
			oFormInputBox.showKeyboard();
			oFormInputBox.show();
			
			if(oFormInputBox.isUserCancel())
				return;
			
			if (oFormInputBox.getInputValue().isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("please_input_reason"));
				oFormDialogBox.show();
			}
		} while (oFormInputBox.getInputValue().isEmpty());
		
		// prompt user to confirm really suspend card
		FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
		oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
		oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("really_to_suspend_this_card")+"?");
		oFormConfirmBox.show();
		if (!oFormConfirmBox.isOKClicked()) {
			this.finishShow();
			return;
		}
		
		// do svc card suspend posting
		String sRemark = oFormInputBox.getInputValue();
		if (!suspendCard(sRemark)) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_suspend_svc_card"));
			oFormDialogBox.show();
		} else {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("success"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("card_suspended"));
			oFormDialogBox.show();
		}
		
		this.finishShow();
		return;
	}
	
	@Override
	public void frameLoyaltySvc_clickSetMember(String sType, String sValue, String sPassword) {
		//check if it is GiveX interface
		if(m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GIVEX)){
			if(m_sAction.equals(FormLoyaltySvc.ACTION_ISSUE_CARD)){
				HashMap<String, String> oHashMapExtraInfo = new HashMap<String, String>();
				
				//Add extrainfo to list
				ArrayList<PosCheckExtraInfo> oPosCheckExtraInfoList = null;
				oHashMapExtraInfo.put("By", PosCheckExtraInfo.BY_ITEM);
				oHashMapExtraInfo.put("Section", PosCheckExtraInfo.SECTION_LOYALTY);
				oHashMapExtraInfo.put(PosCheckExtraInfo.VARIABLE_CARD_NO, sValue);
				oHashMapExtraInfo.put(PosCheckExtraInfo.VARIABLE_REMARK, "IS");
				oHashMapExtraInfo.put(PosCheckExtraInfo.VARIABLE_INTERFACE_ID, String.valueOf(m_oPosInterfaceConfig.getInterfaceId()));
				oPosCheckExtraInfoList = m_oAssignedLoyaltySvc.setExtraInfo(oHashMapExtraInfo);
				
				this.finishShow();
				
				//add the item with open price
				for (FormLoyaltySvcListener listener : listeners) {
					if(!m_oAssignedLoyaltySvc.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").optJSONObject("activate_item_code").optString("value").isEmpty()){
						listener.formLoyaltySvc_hotItemClicked(m_oAssignedLoyaltySvc.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").optJSONObject("activate_item_code").optString("value"), oPosCheckExtraInfoList, null);
						
						break;
					}
					else{
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("missing_setup"));
						oFormDialogBox.show();
						oFormDialogBox = null;
						this.finishShow();
						return;
					}
				}
			}
		} else{
			// Show the result page and hide the member detail page
			if(sValue.isEmpty())
				return;
			
			if(m_iIssueCardCount == 2 && m_oParentLoyaltySvc != null) {
				if(sValue.equals(m_oParentLoyaltySvc.getCardNo())) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_input_same_card_no"));
					oFormDialogBox.show();
					oFormDialogBox = null;
					this.finishShow();
					return;
				}
			}
			
			searchMember(sType, sValue, sPassword, m_oAssignedLoyaltySvc);
			if(m_oAssignedLoyaltySvc.getMemberNo().isEmpty() && m_oAssignedLoyaltySvc.getCardNo().isEmpty()){
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("member_not_found"));
				oFormDialogBox.show();
				oFormDialogBox = null;
				return;
			}
			
			if((m_sAction.equals(FormLoyaltySvc.ACTION_ISSUE_CARD) && !m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_READY_FOR_SALE)) || (m_sAction.equals(FormLoyaltySvc.ACTION_ADD_VALUE) && !m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_ACTIVE))) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
				if(m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_READY_FOR_SALE))
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("the_card_is_not_activated"));
				else if(m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_ACTIVE))
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("the_card_is_activated"));
				else if(m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_SUSPENDED))
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("the_card_is_suspended"));
				else if(m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_TRANSFERRED))
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("the_card_is_transferred"));
				else if(m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_IN_STOCK))
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("the_card_is_in_stock"));
				else
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("the_card_is_missing"));
				oFormDialogBox.show();
				oFormDialogBox = null;
				this.finishShow();
				return;
			}
			
			if (m_sAction.equals(FormLoyaltySvc.ACTION_SUSPEND_CARD) && !m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_ACTIVE)) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
				if(m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_READY_FOR_SALE))
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("the_card_is_not_activated"));
				else if(m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_SUSPENDED))
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("the_card_is_suspended"));
				else if(m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_TRANSFERRED))
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("the_card_is_transferred"));
				else if(m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_IN_STOCK))
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("the_card_is_in_stock"));
				else
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("the_card_is_missing"));
				oFormDialogBox.show();
				oFormDialogBox = null;
				this.finishShow();
				return;
			}
			
			m_oTemplateBuilder.buildFrame(m_oFrameLoyaltySvc, "fraLoyaltySvc");
			
			m_oFrameLoyaltySvc.init(m_bIsSvcCheckValue);
			showMemberDetail(m_oAssignedLoyaltySvc);
			
			for(int i = 0; i <= AppGlobal.MAX_SEATS; i++) {
				// Create ordering basket section for seat
				ArrayList<FuncCheckItem> oFuncCheckItemList = (ArrayList<FuncCheckItem>)m_oFuncCheck.getItemList(i);
				if(oFuncCheckItemList != null && oFuncCheckItemList.size() > 0) {					//Has ordered items in seat i
					for (int j=0; j<oFuncCheckItemList.size(); j++) {
						FuncCheckItem oFuncCheckItem = oFuncCheckItemList.get(j);
						for(PosCheckExtraInfo oPosCheckExtraInfo:oFuncCheckItem.getExtraInfoList()){
							if(oPosCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_LOYALTY_SVC) &&
									oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_BENEFIT_AMOUNT) &&
									oPosCheckExtraInfo.getValue() != null)
								m_oTotalDebit = m_oTotalDebit.add(new BigDecimal(oPosCheckExtraInfo.getValue()));
						}
					}
				}
			}
			
			for(PosCheckDiscount oPosCheckDiscount:m_oFuncCheck.getCurrentPartyAppliedCheckDiscount()){
				BigDecimal oBenefitAmount = BigDecimal.ZERO;
				BigDecimal oBenefitUsed = BigDecimal.ONE;
				for(PosCheckExtraInfo oPosCheckExtraInfo:oPosCheckDiscount.getCheckExtraInfoList()){
					if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_BENEFIT_QTY)){
						if(oPosCheckExtraInfo.getValue() == null)
							oBenefitUsed = BigDecimal.ONE;
						else
							oBenefitUsed = new BigDecimal(oPosCheckExtraInfo.getValue());
					}
					
					if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_BENEFIT_AMOUNT)){
						if(oPosCheckExtraInfo.getValue() == null)
							oBenefitAmount = BigDecimal.ZERO;
						else
							oBenefitAmount = new BigDecimal(oPosCheckExtraInfo.getValue());
					}
				}
				m_oTotalDebit = m_oTotalDebit.add(oBenefitAmount.multiply(oBenefitUsed));
			}
			
			// Trim the value to 80 character long for "card number" type
			if(sType.equals(FrameLoyaltySvc.SEARCH_TYPE.cardNumber.name()) && sValue.length() > 80)
				sValue = sValue.substring(0, 80);
		}
	}

	@Override
	public void frameLoyaltySvc_clickClearMember() {
		// Set the user select type
		m_iUserSelectOptionType = SELECT_OPTION_CLEAR_MEMBER;
		
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public PosInterfaceConfig frameLoyaltySvc_getPosInterfaceConfig(String sInterfaceType, ArrayList<String> sVendors, String sTitle) {
		return this.getInterfaceConfig(sInterfaceType, sVendors, sTitle);
	}
	
	public FuncLoyaltySvc getParentLoyaltySvc() {
		return m_oParentLoyaltySvc;
	}
	
	@Override
	public void frameLoyaltySvc_clickTransferCard() {
		String sDestSvcCardNo = "";
		String sTransferReason = "";
		boolean bReinput = false;
		FuncLoyaltySvc oFuncLoyaltySvc = new FuncLoyaltySvc();
		FormInputBox oFormInputBox = new FormInputBox(this);

		if(!m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_ACTIVE)) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			if(m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_READY_FOR_SALE))
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("the_card_is_not_activated"));
			else if(m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_SUSPENDED))
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("the_card_is_suspended"));
			else if(m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_TRANSFERRED))
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("the_card_is_transferred"));
			else if(m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_IN_STOCK))
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("the_card_is_in_stock"));
			else
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("the_card_is_missing"));
			oFormDialogBox.show();
			
			// Allow suspend card to transfer card
			if(!m_oAssignedLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_SUSPENDED)){
				oFormDialogBox = null;
				this.finishShow();
				return;
			}
		}
		
		//get target card number
		do {
			bReinput = false;
			oFormInputBox.init();
			oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("loyalty_svc_transfer_card"));
			oFormInputBox.setMessage(AppGlobal.g_oLang.get()._("please_input_new_card_no"));
			oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.DEFAULT);
			oFormInputBox.showKeyboard();
			oFormInputBox.show();
			if(oFormInputBox.isUserCancel())
				return;
			
			if(oFormInputBox.getInputValue() != null)
				sDestSvcCardNo = oFormInputBox.getInputValue();	
			
			String sCreditCardContent = oFormInputBox.getSwipeCardValue();
			if(!sCreditCardContent.isEmpty())
			{
				List<PosInterfaceConfig> oLoyaltyInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
				// Get the configure from interface module
				oLoyaltyInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_LOYALTY_INTERFACE);
				JSONObject oLoyaltyInterfaceConfig = null;
				String sMsrCode = "";
				for (PosInterfaceConfig oPosInterfaceConfig:oLoyaltyInterfaceConfigList) {
					if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY_SVC))
					{
						oLoyaltyInterfaceConfig = oPosInterfaceConfig.getInterfaceConfig();
						if(oLoyaltyInterfaceConfig.has("general_setup") && oLoyaltyInterfaceConfig.optJSONObject("general_setup").has("params") &&oLoyaltyInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").has("msr_code"))
							sMsrCode = oLoyaltyInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").optJSONObject("msr_code").optString("value");
					}
				}
				List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
				// Get the configure from interface module
				oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PERIPHERAL_DEVICE);
				for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList) {
					if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_MSR) && oPosInterfaceConfig.getInterfaceCode().equals(sMsrCode))
					{
						if(!sCreditCardContent.isEmpty()){
							// Capture information from swipe card
							FuncMSR oFuncMSR = new FuncMSR();
							if(oFuncMSR.processCardContent(sCreditCardContent, oPosInterfaceConfig.getInterfaceConfig()) == FuncMSR.ERROR_CODE_NO_ERROR){
								// Get the necessary value
								sDestSvcCardNo = oFuncMSR.getCardNo();
							}else{
								// Fail to process swipe card
								FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
								oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
								oFormDialogBox.setMessage(oFuncMSR.getLastErrorMessage());
								oFormDialogBox.show();
								oFormDialogBox = null;
								return;
							}
						}
					}
				}
				
				if (sDestSvcCardNo.isEmpty()) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_capture_card"));
					oFormDialogBox.show();
					oFormDialogBox = null;
					bReinput = true;
					continue;
				}
			} else {
				String sKeyInControls[] = FuncLoyaltySvc.getKeyInControls();
				if(!sDestSvcCardNo.equals("") && sKeyInControls != null) {
					if(sKeyInControls.length >= 3) {
						if(Integer.parseInt(sKeyInControls[2]) == 1) {
							FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
							oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
							oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("support_swipe_card_only"));
							oFormDialogBox.show();
							oFormDialogBox = null;
							this.finishShow();
							bReinput = true;
							continue;
						}
					}
				}
			}
			
			if(sDestSvcCardNo.equals(m_oAssignedLoyaltySvc.getCardNo())) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_input_same_card_no"));
				oFormDialogBox.show();
				oFormDialogBox = null;
				this.finishShow();
				bReinput = true;
			}
			
			searchMember(FrameLoyaltySvc.SEARCH_TYPE.number.name(), sDestSvcCardNo,"" , oFuncLoyaltySvc);
			if(oFuncLoyaltySvc.getMemberNo().isEmpty() && oFuncLoyaltySvc.getCardNo().isEmpty()){
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("member_not_found"));
				oFormDialogBox.show();
				oFormDialogBox = null;
				bReinput = true;
			}
			
			if(oFuncLoyaltySvc.getStatus().equals(FuncLoyaltySvc.STATUS_ACTIVE)) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("the_card_is_activated"));
				oFormDialogBox.show();
				oFormDialogBox = null;
				this.finishShow();
				bReinput = true;
			}
		} while (bReinput);
		
		//get input reason
		do {
			oFormInputBox.init();
			oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("loyalty_svc_transfer_card"));
			oFormInputBox.setMessage(AppGlobal.g_oLang.get()._("please_input_reason"));
			oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.DEFAULT);
			oFormInputBox.showKeyboard();
			oFormInputBox.show();
			if(oFormInputBox.getInputValue() != null && oFormInputBox.getInputValue().length() > 0)
				sTransferReason = oFormInputBox.getInputValue();
			
		} while (oFormInputBox.getInputValue() == null || oFormInputBox.getInputValue().equals(""));
		
		//ask confirmation of transfer
		FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
		String sMessage = AppGlobal.g_oLang.get()._("original_card_no") + " : " + m_oAssignedLoyaltySvc.getCardNo()
				+ System.lineSeparator() + AppGlobal.g_oLang.get()._("new_card_no") + " : "
				+ oFuncLoyaltySvc.getCardNo();
		oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
		oFormConfirmBox.setMessage(sMessage);
		oFormConfirmBox.show();
		
		//do transfer card
		if(oFormConfirmBox.isOKClicked())
			transferCard(m_oAssignedLoyaltySvc, oFuncLoyaltySvc, sTransferReason);
		else
			return;
	}
	
	@Override
	public void frameLoyaltySvc_clickPrint(JSONObject oBalanceDetailJSON) {
		FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"),
				AppGlobal.g_oLang.get()._("no"), this);
		oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("confirm"));
		oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("confirm_to_print_action_slip") + "?");
		oFormConfirmBox.show();
		if (oFormConfirmBox.isOKClicked() == false)
			return;
		
		m_oFuncCheck.printLoyaltyBalanceSlip(oBalanceDetailJSON);
	}
}
