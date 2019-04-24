package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONObject;

import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import commonui.FormSelectionBox;
import core.Controller;
import om.InfInterface;
import om.InfVendor;
import om.MemMember;
import om.PosCheckDiscount;
import om.PosCheckExtraInfo;
import om.PosInterfaceConfig;
import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FormLoyaltyListener {
	String formLoyalty_useItemBenefit(FuncBenefit oFuncBenefit);
	BigDecimal formLoyalty_useCheckBenefit(FuncBenefit oFuncBenefit, BigDecimal oMaxUse, BigDecimal oQty);
}

public class FormLoyalty extends VirtualUIForm implements FrameLoyaltyListener, FrameLoyaltyCheckBalanceListener {
	private TemplateBuilder m_oTemplateBuilder;
	
	private FrameLoyalty m_oFrameLoyalty;
	
	private ArrayList<MemMember> m_oResultMemberList;
	
	// Check information
	HashMap<String, String> m_oCheckInfo;
	
	// Member that is going to assign to check
	private FuncLoyalty m_oAssignedLoyalty;
	
	private FuncCheck m_oFuncCheck;
	private boolean m_bSupportLoyaltyMember;
	
	// Online member interface
	private PosInterfaceConfig m_oPosInterfaceConfig;
	
	// Internal setup
	private boolean m_bHaveMemberInterface;
	private boolean m_bAskMemberForPayment;
	private FuncMembershipInterface m_oFuncMembershipInterface;
	
	// Used Benefit list
	private List<Integer> m_oSelectedBenefitList;
	
	// Used Benefit which fail to redeem
	private List<HashMap<String, HashMap<String, String>>> m_oFailRedeemBenefitList;
	
	// User select option
	// 0: Cancel
	// 1: Set member
	// 2: Clear member
	public static final int SELECT_CANCEL = 0;
	public static final int SELECT_SET_MEMBER = 1;
	public static final int SELECT_CLEAR_MEMBER = 2;
	public static final int SELECT_CANCEL_SELECT_INTERFACE = 3;
	
	// User select option
	// 0: Cancel
	// 1: Set member
	// 2: Clear member
	/*public static final int SELECT_CANCEL = 0;
	public static final int SELECT_SET_MEMBER = 1;
	public static final int SELECT_CLEAR_MEMBER = 2;
	public static final int SELECT_CANCEL_SELECT_INTERFACE = 3;*/
	
	private int m_iUserSelectOptionType;
	
	// Max no. of member of search result
	private static Integer MAX_RESULT_COUNT = 100;
	
	private int m_iCurrentPage;
	private int m_iTotalPage;
	private BigDecimal m_oTotalDebit;
	// Edit by King Cheung 2017-10-18 from 5 to 4
	public static final int MAX_RECORD_SIZE = 4;	// record show in each page
	private String m_sInterfaceId = "";
	private String m_sSvcInterfaceId = "";
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FormLoyaltyListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FormLoyaltyListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FormLoyaltyListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }

	public FormLoyalty(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmLoyalty.xml");
		
		m_oResultMemberList = new ArrayList<MemMember>();
		m_oCheckInfo = new HashMap<String, String>();
		
		m_bHaveMemberInterface = false;
		m_bAskMemberForPayment = false;
		m_oPosInterfaceConfig = null;
		m_oFuncMembershipInterface = null;
		
		m_oAssignedLoyalty = null;

		m_oFuncCheck = null;
		m_oTotalDebit = BigDecimal.ZERO;
		
		m_oSelectedBenefitList = new ArrayList<Integer>();
		m_iUserSelectOptionType = SELECT_CANCEL;
	}
	
	public boolean init(String sTitle, FuncCheck oFuncCheck) {
		listeners = new ArrayList<FormLoyaltyListener>();
		m_oAssignedLoyalty = new FuncLoyalty(oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_MEMBER_NAME), oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER), oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_BONUS_BALANCE));
		
		m_oFuncCheck = oFuncCheck;
		
		// To restore the member information which has assigned to the check
		if(m_sInterfaceId.isEmpty()){
			List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
			oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_LOYALTY_INTERFACE);
			
			for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList) {
				if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY)) {
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
		
		if (!AppGlobal.g_sMasterServerUrl.isEmpty() && AppGlobal.g_oFuncStation.get().getLoyaltySessionId().isEmpty()) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_connect_to_loyalty_server") + System.lineSeparator() + AppGlobal.g_oLang.get()._("fail_to_perform_loyalty_bonus_transaction"));
			oFormDialogBox.show();
			return false;
		}
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// If there is no member number, show the number pad
		
		m_oFrameLoyalty = new FrameLoyalty();
		if(m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER) == null || m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER).isEmpty()){
			m_oTemplateBuilder.buildFrame(m_oFrameLoyalty, "fraLoyaltyNumPad");
			m_oFrameLoyalty.initNumberPad(sTitle);
		}
		else{
			m_oTemplateBuilder.buildFrame(m_oFrameLoyalty, "fraLoyalty");
			m_oFrameLoyalty.init(sTitle);
		}
		
		// Add listener;
		m_oFrameLoyalty.addListener(this);
		this.attachChild(m_oFrameLoyalty);
		
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
		
		m_oTotalDebit = m_oAssignedLoyalty.getTotalBonusDebit();
		if(m_oTotalDebit.equals(BigDecimal.ZERO)){
			for(int i = 0; i <= AppGlobal.MAX_SEATS; i++) {					
				// Create ordering basket section for seat
				ArrayList<FuncCheckItem> oFuncCheckItemList = (ArrayList<FuncCheckItem>)oFuncCheck.getItemList(i);
				
				if(oFuncCheckItemList != null && oFuncCheckItemList.size() > 0) {					//Has ordered items in seat i
					for (int j=0; j<oFuncCheckItemList.size(); j++) {
						FuncCheckItem oFuncCheckItem = oFuncCheckItemList.get(j);
						for(PosCheckExtraInfo oPosCheckExtraInfo:oFuncCheckItem.getExtraInfoList()){
							if(oPosCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_LOYALTY) &&
									oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_BENEFIT_AMOUNT) &&
									oPosCheckExtraInfo.getValue() != null && oPosCheckExtraInfo.getValue() != "")
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
		
		if(m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_TRACE_ID) != null && !oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_TRACE_ID).isEmpty()){
			m_oFuncCheck.calcLoyaltyBaseTotal();
			if(!m_oAssignedLoyalty.startRedeem("", FuncLoyalty.TYPE_NUMBER, m_sInterfaceId, m_sSvcInterfaceId, m_oFuncCheck, 1)){
				String sErrorMessage = m_oAssignedLoyalty.getLastErrorMessage();
				if(!sErrorMessage.isEmpty()) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(sErrorMessage);
					oFormDialogBox.show();
					oFormDialogBox = null;
				}
				return false;
			}
			showMemberDetail(m_oAssignedLoyalty);
		}
		m_oSelectedBenefitList = new ArrayList<Integer>();
		
		return true;
	}
	
	public void initFailRedemption(String sTitle, FuncCheck oFuncCheck, List<HashMap<String, HashMap<String, String>>> oFailRedeemBenefitList){
		listeners = new ArrayList<FormLoyaltyListener>();
		m_oAssignedLoyalty = new FuncLoyalty(oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_MEMBER_NAME), oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER), oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_BONUS_BALANCE));
		
		m_oFailRedeemBenefitList = oFailRedeemBenefitList;

		// Add by King Cheung 2017-10-18 ---Start---
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		// Add by King Cheung 2017-10-18 ---End---
		
		m_oFrameLoyalty = new FrameLoyalty();
		m_oTemplateBuilder.buildFrame(m_oFrameLoyalty, "fraLoyaltyFailRedemption");
		m_oFrameLoyalty.showFailedRedeemBenefit(oFailRedeemBenefitList);
		
		// Add listener;
		m_oFrameLoyalty.addListener(this);
		this.attachChild(m_oFrameLoyalty);
		
		showFailRedeemptionListAtPage(1);
		if (oFailRedeemBenefitList.size() > 0) {
			m_iCurrentPage = 1;
			
			m_iTotalPage = oFailRedeemBenefitList.size() / MAX_RECORD_SIZE;
			if (oFailRedeemBenefitList.size() % MAX_RECORD_SIZE > 0)
				m_iTotalPage++;

			m_oFrameLoyalty.updatePageButton(m_iCurrentPage, m_iTotalPage);
		}
	}
	
	public void initLoyaltyBalance(FuncCheck oFuncCheck, LinkedHashMap<String , String> oLoyaltyCheckBalance){
		listeners = new ArrayList<FormLoyaltyListener>();
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		FrameLoyaltyCheckBalance oFrameLoyaltyCheckBalance = new FrameLoyaltyCheckBalance(oFuncCheck, oLoyaltyCheckBalance);
		m_oTemplateBuilder.buildFrame(oFrameLoyaltyCheckBalance, "fraLoyaltyCheckBalance");
		oFrameLoyaltyCheckBalance.setVisible(true);
		oFrameLoyaltyCheckBalance.bringToTop();
		
		// Add listener;
		oFrameLoyaltyCheckBalance.addListener(this);
		this.attachChild(oFrameLoyaltyCheckBalance);
	}
	
	private void searchMember(String sType, String sValue){
		m_oResultMemberList.clear();
		m_oAssignedLoyalty = new FuncLoyalty();
		if(m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER) != null && !m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER).isEmpty())
			return;
		
		// Connect to master server to get loyalty member information
		if (!AppGlobal.g_sMasterServerUrl.isEmpty() && AppGlobal.g_oFuncStation.get().getLoyaltySessionId().isEmpty()) {
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
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_connect_to_master_server") + System.lineSeparator() + AppGlobal.g_oLang.get()._("fail_to_perform_loyalty_bonus_transaction"));
				oFormDialogBox.show();
				
				return;
			}
		}
		
		m_oFuncCheck.calcLoyaltyBaseTotal();
		if(!m_oAssignedLoyalty.startRedeem(sValue, sType, m_sInterfaceId, m_sSvcInterfaceId, m_oFuncCheck, 1)){
			String sErrorMessage = m_oAssignedLoyalty.getLastErrorMessage();
			if(!sErrorMessage.isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(sErrorMessage);
				oFormDialogBox.show();
				oFormDialogBox = null;
			}
			return;
		}
		
		m_oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0, m_sInterfaceId);
		m_oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_TRACE_ID, 0, m_oAssignedLoyalty.getTraceId());
		m_oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0, m_oAssignedLoyalty.getMemberNo());
		m_oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_MEMBER_NAME, 0, m_oAssignedLoyalty.getName());
		m_oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_BONUS_REDEMPTION_CODE, 0, m_oAssignedLoyalty.getCampaignCode());
		m_oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_BONUS_BALANCE, 0, m_oAssignedLoyalty.getBalance().toString());
		m_oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_POINTS_BALANCE, 0, m_oAssignedLoyalty.getBalance().toString());
		
		if(m_oAssignedLoyalty.getCampaignCode().isEmpty()){
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("no_campaign_benefit_is_available_for_redemption"));
			oFormDialogBox.show();
			
			// Error to no campaign benefit is available for redemption
			this.finishShow();
			
			return;
		}
	}
	
	public boolean calculateLoyaltyTransaction(FuncCheck oFuncCheck){
		m_oResultMemberList.clear();
		m_oAssignedLoyalty = new FuncLoyalty();
		/*if(!m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER).isEmpty())
			return false;*/
		// Connect to master server to get loyalty member information
		if (!AppGlobal.g_sMasterServerUrl.isEmpty() && AppGlobal.g_oFuncStation.get().getLoyaltySessionId().isEmpty()) {
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
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_connect_to_master_server") + System.lineSeparator() + AppGlobal.g_oLang.get()._("fail_to_perform_loyalty_bonus_transaction"));
				oFormDialogBox.show();
				
				return false;
			}
		}
		
		oFuncCheck.calcLoyaltyBaseTotal();
		if(!m_oAssignedLoyalty.startRedeem("", FuncLoyalty.TYPE_NUMBER, m_sInterfaceId, m_sSvcInterfaceId, oFuncCheck, 1)){
			String sErrorMessage = m_oAssignedLoyalty.getLastErrorMessage();
			if(!sErrorMessage.isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(sErrorMessage);
				oFormDialogBox.show();
				oFormDialogBox = null;
			}
			return false;
		}
		return true;
	}
	
	private void showMemberDetail(FuncLoyalty oFuncGmLoyalty){
		if (oFuncGmLoyalty.getBenefitList().size() > 0) {
			m_iCurrentPage = 1;
			
			m_iTotalPage = oFuncGmLoyalty.getBenefitList().size() / MAX_RECORD_SIZE;
			if (oFuncGmLoyalty.getBenefitList().size() % MAX_RECORD_SIZE > 0)
				m_iTotalPage++;

			m_oFrameLoyalty.updatePageButton(m_iCurrentPage, m_iTotalPage);
		}
		
		m_oFrameLoyalty.showMemberDetail(oFuncGmLoyalty);
		
		// Update Total Debit
		m_oFrameLoyalty.updateDebit(m_oTotalDebit, oFuncGmLoyalty.getBalance());
		showOverrideConditionsAtPage(1);
		
		// Set the member to be assigned to check if set member button is clicked
		m_oAssignedLoyalty = oFuncGmLoyalty;
			
		m_oSelectedBenefitList = new ArrayList<Integer>();
	}
	
//	public boolean isInterfaceConnectionError(){
//		return m_oFuncMembershipInterface.isConnectionError();
//	}
//	
//	public int getUserInputType(){
//		return m_iUserSelectOptionType;
//	}
//	
//	public PosLoyalty getAssignedLoyalty(){
//		return m_oAssignedLoyalty;
//	}
//	
//	public HashMap<String, String> getAssignedInterfaceMember(){
//		if(m_oFuncMembershipInterface == null)
//			return null;
//		else
//			return m_oFuncMembershipInterface.getLastMemberInfo();
//	}
//	
//	public PosInterfaceConfig getMemberInterface() {
//		return m_oPosInterfaceConfig;
//	}
	
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
		return m_oFrameLoyalty.isSwipeCard();
	}
	
	// update records of page
	private void showOverrideConditionsAtPage(int iPage) {
		
		if(m_oFailRedeemBenefitList != null && m_oFailRedeemBenefitList.size() > 0){
			showFailRedeemptionListAtPage(iPage);
			return;
		}
		
		int iStartIndex = (iPage - 1) * MAX_RECORD_SIZE;
		int iEndIndex = iPage * MAX_RECORD_SIZE;
		if (iEndIndex > m_oAssignedLoyalty.getBenefitList().size())
			iEndIndex = m_oAssignedLoyalty.getBenefitList().size();
		int iIndex = 0;
		for (int i = iStartIndex; i < iEndIndex; i++) {
			boolean bUsed = false;
			FuncBenefit oBenefit = m_oAssignedLoyalty.getBenefitList().get(i);
			m_oFrameLoyalty.addMemberToResultList(0, iIndex, oBenefit.getBenefitType(), oBenefit.getDesc(), new BigDecimal(oBenefit.getBonusDebit()), oBenefit.isPercentDiscount(), bUsed);
			
			// Change the color if selected
			for(int j = m_oSelectedBenefitList.size() - 1 ; j >= 0 ; j--){
				if(m_oSelectedBenefitList.get(j) == i){
					m_oFrameLoyalty.getBenefitList().setAllFieldsBackgroundColor(0, iIndex, "#000000");
				}
			}
			iIndex++;
		}
		
	}
	
	private void showFailRedeemptionListAtPage(int iPage) {
		int iStartIndex = (iPage - 1) * MAX_RECORD_SIZE;
		int iEndIndex = iPage * MAX_RECORD_SIZE;
		if (iEndIndex > m_oFailRedeemBenefitList.size())
			iEndIndex = m_oFailRedeemBenefitList.size();
		int iIndex = 0, iBenefitCounting = 0;
		for(HashMap<String, HashMap<String, String>>oFailRedeemBenefit : m_oFailRedeemBenefitList){
			if(iBenefitCounting == iEndIndex)
				break;
			if(iBenefitCounting < iStartIndex){
				iBenefitCounting ++ ;
				continue;
			}
			
			for(Entry<String, HashMap<String, String>> entry : oFailRedeemBenefit.entrySet()) {
				for(Entry<String, String> entryDetail : entry.getValue().entrySet()) {
					m_oFrameLoyalty.addMemberToResultList(0, iIndex, FuncBenefit.TYPE_ITEM, entryDetail.getKey(), new BigDecimal(entryDetail.getValue()), false, false);
				}
			}
			
			iIndex++;
			iBenefitCounting ++ ;
		}
	}
	
	@Override
	public void frameLoyalty_clickCancel() {
		// Clear assigned member
		m_oAssignedLoyalty = null;
//		m_oSelectedBenefitList = new ArrayList<Integer>();
		m_oTotalDebit = BigDecimal.ZERO;
		
		// Set the user select type
		m_iUserSelectOptionType = SELECT_CANCEL;
		
		// Finish showing this form
		this.finishShow();
	}

	@Override
	public void frameLoyalty_clickSetMember(String sType, String sValue) {	
		// Show the result page and hide the member detail page
		
		if(sValue.isEmpty())
			return;
		
		searchMember(sType, sValue);
		if(m_oAssignedLoyalty.getMemberNo().isEmpty())
			return;
		
		m_oTemplateBuilder.buildFrame(m_oFrameLoyalty, "fraLoyalty");
		m_oFrameLoyalty.init(AppGlobal.g_oLang.get()._("loyalty_benefit_list"));
		
		if(!m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_TRACE_ID).isEmpty()){
			/*String sErrorMessage = m_oAssignedLoyalty.startRedeem("", PosLoyalty.TYPE_NUMBER, m_sInterfaceId, m_oFuncCheck);
			displayErrorMessage(sErrorMessage);
			if (!sErrorMessage.isEmpty())
				return;*/
			showMemberDetail(m_oAssignedLoyalty);
		}
		
		m_oTotalDebit = m_oAssignedLoyalty.getTotalBonusDebit();
		for(int i = 0; i <= AppGlobal.MAX_SEATS; i++) {
			// Create ordering basket section for seat
			ArrayList<FuncCheckItem> oFuncCheckItemList = (ArrayList<FuncCheckItem>)m_oFuncCheck.getItemList(i);
			
			if(oFuncCheckItemList != null && oFuncCheckItemList.size() > 0) {					//Has ordered items in seat i
				for (int j=0; j<oFuncCheckItemList.size(); j++) {
					FuncCheckItem oFuncCheckItem = oFuncCheckItemList.get(j);
					for(PosCheckExtraInfo oPosCheckExtraInfo:oFuncCheckItem.getExtraInfoList()){
						if(oPosCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_LOYALTY) &&
								oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_BENEFIT_AMOUNT) &&
								oPosCheckExtraInfo.getValue() != null && oPosCheckExtraInfo.getValue() != "")
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
		
		m_oFrameLoyalty.switchTag(0, true);
		
		// Update Button color
		m_oFrameLoyalty.updateButtonColor(sType);
		
		// Trim the value to 80 character long for "card number" type
		if(sType.equals(FrameLoyalty.SEARCH_TYPE.cardNumber.name()) && sValue.length() > 80)
			sValue = sValue.substring(0, 80);
		
		/*if(!frameLoyalty_clickSearch(sType, sValue)){
				// Clear the result page
				m_oBasketResultList.clearAllSections();
		}*/
	}

	@Override
	public void frameLoyalty_clickClearMember() {
		// Set the user select type
		m_iUserSelectOptionType = SELECT_CLEAR_MEMBER;
		
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public boolean frameLoyalty_clickSearch(String sType,	String sValue) {
		// Search member
		// In phase 1, always get the first page, so page = 1
		if(!sValue.isEmpty() && m_oAssignedLoyalty.getMemberNo().isEmpty()){
			searchMember(sType, sValue);
			return true;
		}
		else if(!m_oAssignedLoyalty.getMemberNo().isEmpty()){
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("this_check_had_already_start_redeem_by_other"));
			oFormDialogBox.show();
			oFormDialogBox = null;
			return true;
		}
		return false;
	}

	@Override
	public void frameLoyalty_clickUseBenefit(int iIndex) {
		String sErrMsg = "";
		BigDecimal oTotalDebitUse = BigDecimal.ZERO;

		FuncBenefit oFuncBenefit = m_oAssignedLoyalty.getBenefitList().get((m_iCurrentPage-1)*MAX_RECORD_SIZE+iIndex);
		if(oFuncBenefit.getBonusDebit()+m_oTotalDebit.intValue() <= m_oAssignedLoyalty.getBalance().intValue()){

			// Check the Benefit Type
			if(oFuncBenefit.getBenefitType().equals(FuncBenefit.TYPE_ITEM)){
				// If it is the item benefit
				for (FormLoyaltyListener listener: listeners)
					sErrMsg = listener.formLoyalty_useItemBenefit(oFuncBenefit);
				
				if (!sErrMsg.equals("")) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(sErrMsg);
					oFormDialogBox.show();
					oFormDialogBox = null;
					return;
				}
				// Update the Total Debit
				m_oTotalDebit = m_oTotalDebit.add(new BigDecimal(oFuncBenefit.getBonusDebit()));
			}
			else if(oFuncBenefit.getBenefitType().equals(FuncBenefit.TYPE_CASH) || oFuncBenefit.getBenefitType().equals(FuncBenefit.TYPE_PERCENT)){
				if(oFuncBenefit.getBenefitType().equals(FuncBenefit.TYPE_PERCENT)){
					for(PosCheckDiscount oPosCheckDiscount:m_oFuncCheck.getCurrentPartyAppliedCheckDiscount()){
						for(PosCheckExtraInfo oPosCheckExtraInfo:oPosCheckDiscount.getCheckExtraInfoList()){
							if(oPosCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_LOYALTY) &&
								oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_BENEFIT_TYPE) &&
								oPosCheckExtraInfo.getValue() != null && oPosCheckExtraInfo.getValue().equals("P")){
									FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
									oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
									oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("check_discount_had_been_applied_before") + System.lineSeparator() + AppGlobal.g_oLang.get()._("please_void_the_previous_discount_first_before_add_new_discount"));
									oFormDialogBox.show();
									oFormDialogBox = null;
									return;
							}
						}
					}
				}
				
				// If it is the check discount benefit whatever fix or percentage discount
				BigDecimal oMaxUse = BigDecimal.ZERO;
				BigDecimal oAvaCount = new BigDecimal(oFuncBenefit.getAvaCount());
				if(oFuncBenefit.getCondition().equals("N") && oFuncBenefit.getMaxCount() > 0)
					oAvaCount = new BigDecimal(oFuncBenefit.getMaxCount());
				if(oFuncBenefit.getBenefitType().equals(FuncBenefit.TYPE_CASH)){
					if(oFuncBenefit.getBonusDebit() == 0)
						oMaxUse = oAvaCount;
					else if(oFuncBenefit.getBonusDebit() > 0)
						oMaxUse = (m_oAssignedLoyalty.getBalance().subtract(m_oTotalDebit)).divide(new BigDecimal(oFuncBenefit.getBonusDebit()), BigDecimal.ROUND_DOWN);
					if(oMaxUse.compareTo(oAvaCount) > 0)
						oMaxUse = oAvaCount;
					for (FormLoyaltyListener listener: listeners)
						oTotalDebitUse = listener.formLoyalty_useCheckBenefit(oFuncBenefit, oMaxUse, null);
				}
				else{
					for (FormLoyaltyListener listener: listeners)
						oTotalDebitUse = listener.formLoyalty_useCheckBenefit(oFuncBenefit, oMaxUse, BigDecimal.ONE);
				}
				
				// Update the Total Debit
				m_oTotalDebit = m_oTotalDebit.add(oTotalDebitUse);
			}
			
			// Update the Total Debit which display on screen
			m_oFrameLoyalty.updateDebit(m_oTotalDebit, m_oAssignedLoyalty.getBalance());
		} else {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("benefit_over_max_count"));
			oFormDialogBox.show();
			oFormDialogBox = null;
			return;
		}
		//showMemberDetail(iIndex, false, 0);
	}

	@Override
	public PosInterfaceConfig frameLoyalty_getPosInterfaceConfig(String sInterfaceType, ArrayList<String> sVendors, String sTitle) {
		// TODO Auto-generated method stub
		
		return this.getInterfaceConfig(sInterfaceType, sVendors, sTitle);
	}

	@Override
	public void frameLoyalty_prevPage() {
		// TODO Auto-generated method stub
		m_iCurrentPage--;
		m_oFrameLoyalty.clearOverrideConditionRecords();
		this.showOverrideConditionsAtPage(m_iCurrentPage);	
		m_oFrameLoyalty.updatePageButton(m_iCurrentPage, m_iTotalPage);
	}

	@Override
	public void frameLoyalty_nextPage() {
		// TODO Auto-generated method stub
		m_iCurrentPage++;
		m_oFrameLoyalty.clearOverrideConditionRecords();
		this.showOverrideConditionsAtPage(m_iCurrentPage);
		m_oFrameLoyalty.updatePageButton(m_iCurrentPage, m_iTotalPage);
	}
	@Override
	public void frameLoyaltyCheckBalance_clickOK() {
		// TODO Auto-generated method stub
		this.finishShow();	
	}
	
	@Override
	public void frameLoyaltyCheckBalance_clickPrint(FuncCheck oFuncCheck, JSONObject oBalanceDetailJSON) {
		// TODO Auto-generated method stub
		FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"),
				AppGlobal.g_oLang.get()._("no"), this);
		oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("confirm"));
		oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("confirm_to_print_action_slip") + "?");
		oFormConfirmBox.show();
		if (oFormConfirmBox.isOKClicked() == false)
			return;
		
		oFuncCheck.printLoyaltyBalanceSlip(oBalanceDetailJSON);
	}
	
}
