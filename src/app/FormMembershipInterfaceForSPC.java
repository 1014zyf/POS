package app;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import commonui.FormSelectionBox;
import core.Controller;
import om.MenuPriceLevelList;
import om.PosActionLog;
import om.PosBusinessDay;
import om.PosCheckDiscount;
import om.PosCheckExtraInfo;
import om.PosDiscountType;
import om.PosDiscountTypeList;
import om.PosInterfaceConfig;
import virtualui.VirtualUIFrame;

public class FormMembershipInterfaceForSPC extends FormMembershipInterface{
	FrameMembershipInterfaceForSPC m_oFrameMembershipInterfaceForSPC;
	
	public static final String ACTION_TYPE_ENQUIRY = "enquiry";
	public static final String ACTION_TYPE_DISCOUNT = "discount";
	
	public FormMembershipInterfaceForSPC(FuncCheck oFuncCheck, FuncPayment oFuncPayment, String sTitle, String oFunctionName, Controller oParentController) {
		super(oFuncCheck, null, sTitle, oParentController);
		m_sCurrentOperation = FrameMembershipInterface.OPERATION_ENQUIRY;
		if (oFunctionName.equals(AppGlobal.FUNC_LIST.set_member_discount.name()))
			m_sCurrentOperation = FrameMembershipInterface.OPERATION_SET_MEMBER_DISCOUNT;
	}

	public void init(PosInterfaceConfig oPosInterfaceConfig, String sDefaultValue, boolean bShowInFloorPlan, String sTableNo) {
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameMembershipInterfaceForSPC = new FrameMembershipInterfaceForSPC();

		m_oFrameMembershipInterfaceForSPC.init(oPosInterfaceConfig, sDefaultValue, m_sCurrentOperation, m_sFunctionName, bShowInFloorPlan, sTableNo);
		m_oFrameMembershipInterfaceForSPC.addListener(this);
		m_oTemplateBuilder.buildFrame(m_oFrameMembershipInterfaceForSPC, "fraMembershipInterface");
		
		this.attachChild(m_oFrameMembershipInterfaceForSPC);
	}
	
	@Override
	public void svcEnquiry(MenuPriceLevelList oMenuPriceLevelList, PosInterfaceConfig oInterfaceConfig, String sMemberNumber, String sLastName, boolean bShowInFloorPlan, String sTableNo, boolean bAutoFunction, boolean bHidePrintButton) {
		boolean bMemberAttached = false, bSkipAction = false;
		String sDefaultNumber = "", sDefaultName = "";
		
		// Not support mobile view
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			String sTitle = AppGlobal.g_oLang.get()._("warning");
			showDialogBox(sTitle, AppGlobal.g_oLang.get()._("vendor_is_not_supported_in_this_mode"));
			m_bIsActionAbort = true;
			return;
		}
		
		if (!bShowInFloorPlan && m_sCurrentOperation.equals(FrameMembershipInterface.OPERATION_SET_MEMBER_DISCOUNT)) {
			// check whether have other membership id attached
			String sInterfaceId = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID);
			
			if (sInterfaceId != null && !sInterfaceId.isEmpty() && !sInterfaceId.equals(Integer.toString(m_oPosInterfaceConfig.getInterfaceId()))) {
					showDialogBox(AppGlobal.g_oLang.get()._("warning"), AppGlobal.g_oLang.get()._("have_different_interface_member")
							+ System.lineSeparator() + AppGlobal.g_oLang.get()._("please_clear_current_member_first"));
					m_bIsActionAbort = true;
					return;
			}
			
			// pass default value
			if (m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER) != null)
				sDefaultNumber = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER);
		}
		
		init(m_oPosInterfaceConfig, sDefaultNumber, bShowInFloorPlan, sTableNo);
		m_oInterfaceSetup = m_oPosInterfaceConfig.getInterfaceConfig();
		m_oFuncMembershipInterface = new FuncMembershipInterface(m_oPosInterfaceConfig);
		
		// Auto function support
/*		if (bAutoFunction) {
			HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();
			oEnquiryInfo.put("cardNumber", sMemberNumber);
			oEnquiryInfo.put("surname", sLastName);
			frameMembershipInterfaceResult_clickEnquiry(oEnquiryInfo);
			frameMembershipInterfaceResult_clickSetMember(sMemberNumber);
			return;
		}*/
		
		if (!bShowInFloorPlan && m_sCurrentOperation.equals(FrameMembershipInterface.OPERATION_SET_MEMBER_DISCOUNT)) {
			//check whether having existing member attached
			if (!sDefaultNumber.isEmpty())
				bMemberAttached = true;
		}
		
		if (bMemberAttached && m_oFuncPayment == null && m_sCurrentOperation.equals(FrameMembershipInterface.OPERATION_SET_MEMBER_DISCOUNT)) {
			// Check if member have been set before
			if (sDefaultNumber != null && !sDefaultNumber.isEmpty()) {
				if (!getUserConfirm(AppGlobal.g_oLang.get()._("warning"), AppGlobal.g_oLang.get()._("discount_had_been_applied_before") + "," + System.lineSeparator() + AppGlobal.g_oLang.get()._("confirm_to_continue") + "?")) {
					m_bIsActionAbort = true;
					return ;
				}
			}

			// Do inquiry for attached member
			HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();
			oEnquiryInfo.put("cardNumber", sDefaultNumber);
			
			enquiry(oEnquiryInfo, ACTION_TYPE_DISCOUNT);
		}
	}
	
	public boolean enquiry(HashMap<String, String> oEnquiryInfo, String sActionType) {
		// Check if input valid
		if(!oEnquiryInfo.containsKey("cardNumber") || oEnquiryInfo.get("cardNumber") == null || oEnquiryInfo.get("cardNumber").isEmpty()) {
			showDialogBox(AppGlobal.g_oLang.get()._("member_enquiry"), AppGlobal.g_oLang.get()._("please_fill_in_enquiry_info"));
			return false;
		}
		
		// Check if input lager than 10
		if(oEnquiryInfo.get("cardNumber").length() > 10) {
			showDialogBox(AppGlobal.g_oLang.get()._("member_enquiry"), AppGlobal.g_oLang.get()._("card_number_exceed_maximum_length"));
			return false;
		}
		
		//Generate the enquiry information
		HashMap<String, String> oEnquiryInfos = new HashMap<String, String>();
		oEnquiryInfos.put("cardNumber", oEnquiryInfo.get("cardNumber"));
		oEnquiryInfos.put("actionType", sActionType);

		if (m_oFuncMembershipInterface.cardEnquiry(oEnquiryInfos) == false) {
			showDialogBox(AppGlobal.g_oLang.get()._("member_enquiry"), m_oFuncMembershipInterface.getLastErrorMessage());
			return false;
		}
		
		if (sActionType.equals("discount") &&  m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sDiscountCode.isEmpty()) {
			showDialogBox(AppGlobal.g_oLang.get()._("error"), AppGlobal.g_oLang.get()._("not_allowed_due_to_the_customer_does_not_have_the_staff_privilege"));
			return false;
		}
		
		m_sEnquiryAccNumber = oEnquiryInfo.get("cardNumber");
		
		// Phase response
		String sExpiryDate = m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sExpiryDate;
		try {
			Date oTradeDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(sExpiryDate);
			sExpiryDate = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH).format(oTradeDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		LinkedHashMap<String, String> oEnquiryResponse = new LinkedHashMap<>();
		oEnquiryResponse.put(AppGlobal.g_oLang.get()._("member_name"),	m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberName);
		oEnquiryResponse.put(AppGlobal.g_oLang.get()._("spc_id")+" #",	m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberNumber);
		oEnquiryResponse.put(AppGlobal.g_oLang.get()._("expiry_date"),	sExpiryDate);
		
		for (int i = 0; i < 9; i++)
			oEnquiryResponse.put(AppGlobal.g_oLang.get()._("benefit_description") + " " + (i+1), m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sBenefitList.get(i));
		
		// Update member detail
		m_oFrameMembershipInterfaceForSPC.updateMemberDetail(oEnquiryResponse);
		
		// Reset textbox value
		m_oFrameMembershipInterfaceForSPC.cleanupAllTextBox();
		
		return true;
	}
	
	public boolean setMember(String sMemberNo) {
		// Check if enquiry result exist
		if (m_sEnquiryAccNumber.isEmpty()) {
			showDialogBox(AppGlobal.g_oLang.get()._("set_member"), AppGlobal.g_oLang.get()._("please_do_enquiry_first"));
			return false;
		}
		
		// Ask confirmation to set / reset member
		String sMessage = AppGlobal.g_oLang.get()._("confirm_to_apply_discount") + "?";
		String sCurrentMemberNumber = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER);
		
		if (sCurrentMemberNumber != null && !sCurrentMemberNumber.isEmpty()) {
			showDialogBox( AppGlobal.g_oLang.get()._("error"), AppGlobal.g_oLang.get()._("discount_had_been_applied_before"));
			return false;
		}
		
		if (!getUserConfirm(AppGlobal.g_oLang.get()._("apply_discount"), sMessage))
			return false;
		
		// Ready to set member
		m_sEnquiryAccNumber = m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberNumber;

		// Apply discount
		if (!applyDiscount())
			return false;
		
		updateCheckMembershipInterfaceExtraInfo(false);
		finishShow();
		
		return true;
	}
	
	public void updateCheckMembershipInterfaceExtraInfo(boolean bIsClearMember) {
		ArrayList<HashMap<String, String>> oCheckExtraInfos = new ArrayList<HashMap<String, String>>();
		oCheckExtraInfos = addCheckExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, "0", bIsClearMember ? "" : Integer.toString(m_oPosInterfaceConfig.getInterfaceId()));
		oCheckExtraInfos = addCheckExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, "0", bIsClearMember ? "" : m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberNumber);
		oCheckExtraInfos = addCheckExtraInfoToList(oCheckExtraInfos, PosCheckExtraInfo.VARIABLE_MEMBER_NAME, "0", bIsClearMember ? "" : m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberName);
		
		m_oFuncCheck.saveMembershipInterfaceExtraInfo(true, oCheckExtraInfos);
	}
	
	// Temporary.......
	public boolean applyDiscount() {
		// Get Discount Code from response
		String sDiscountCode = m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sDiscountCode;
		
		if (sDiscountCode.isEmpty()) {
			showDialogBox(AppGlobal.g_oLang.get()._("warning"), AppGlobal.g_oLang.get()._("not_allowed_due_to_the_customer_does_not_have_the_staff_privilege"));
			return false;
		}
		
		// discount restriction checking
		if(!m_oFuncCheck.checkDiscountApplyRestriction(true)) {
			showDialogBox(AppGlobal.g_oLang.get()._("error"), AppGlobal.g_oLang.get()._("discount_had_been_applied_before"));
			return false;
		}
		
		List<HashMap<String, Integer>> oSelectedItems = m_oFuncCheck.getSectionItemIndexForCurrentOrderedItem();
		PosDiscountTypeList oDiscTypeList = new PosDiscountTypeList();
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		PosBusinessDay oBusinessDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay();
		PosDiscountType oSelectedDiscType;
		
		if (oSelectedItems.size() <= 0) {
			showDialogBox(AppGlobal.g_oLang.get()._("error"), AppGlobal.g_oLang.get()._("no_items_for_applying_discount"));
			return false;
		}
		
		// form the lookup description
		oDiscTypeList.readDiscountListByOutletId("check", AppGlobal.g_oFuncOutlet.get().getShopId(),
				AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncStation.get().getStationId(),
				dateFormat.print(oBusinessDay.getDate()), oBusinessDay.isHoliday(),
				oBusinessDay.isDayBeforeHoliday(), oBusinessDay.isSpecialDay(),
				oBusinessDay.isDayBeforeSpecialDay(), oBusinessDay.getDayOfWeek(),
				AppGlobal.g_oFuncUser.get().getUserGroupList(), false);
		
		// check discount existence
		oSelectedDiscType = oDiscTypeList.getDiscountTypeByCode(sDiscountCode);
		
		if (oSelectedDiscType == null) {
			showDialogBox(AppGlobal.g_oLang.get()._("error"), AppGlobal.g_oLang.get()._("discount_not_exist"));
			return false;
		}
		
		// check whether is used for discount
		if (oSelectedDiscType.isUsedForExtraCharge()) {
			showDialogBox(AppGlobal.g_oLang.get()._("error"), AppGlobal.g_oLang.get()._("fail_to_apply_extra_charge_as_discount"));
			return false;
		}
		
		// check whether is check discount
		if (!oSelectedDiscType.isApplyToCheck()) {
			showDialogBox(AppGlobal.g_oLang.get()._("error"), AppGlobal.g_oLang.get()._("invalid_setup"));
			return false;
		}
		
		//check whether the discount is open check discount
		if ((oSelectedDiscType.isPercentageDiscountMethod() && oSelectedDiscType.getRate().compareTo(BigDecimal.ZERO) == 0) ||
				(!oSelectedDiscType.isPercentageDiscountMethod() && oSelectedDiscType.getFixAmount().compareTo(BigDecimal.ZERO) == 0)){
			showDialogBox(AppGlobal.g_oLang.get()._("error"), AppGlobal.g_oLang.get()._("invalid_setup"));
			return false;
		}
		
		// get discount presentage & construct check extra info
		BigDecimal dDiscountAmt = oSelectedDiscType.isPercentageDiscountMethod() ? oSelectedDiscType.getRate() : oSelectedDiscType.getFixAmount();
		
		ArrayList <PosCheckExtraInfo> oDiscountCheckExtraInfoList = new ArrayList<PosCheckExtraInfo>();
		PosCheckExtraInfo oTempExtraInfo = new PosCheckExtraInfo();
		oTempExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.BY_DISCOUNT, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, Integer.toString(m_oPosInterfaceConfig.getInterfaceId()));
		oDiscountCheckExtraInfoList.add(oTempExtraInfo);
		
		oTempExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.BY_DISCOUNT, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_REFERENCE, m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberNumber);
		oDiscountCheckExtraInfoList.add(oTempExtraInfo);
		
		oTempExtraInfo = constructCheckExtraInfo(PosCheckExtraInfo.BY_DISCOUNT, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberNumber);
		oDiscountCheckExtraInfoList.add(oTempExtraInfo);
		
		List<Integer> oItemDiscountGrpList = new ArrayList<Integer>();
		HashMap<Integer, Boolean> oDiscountAllowance = new HashMap<Integer, Boolean>();
		for (HashMap<String, Integer> oSelectedItem : oSelectedItems) {
			FuncCheckItem oParentFuncCheckItem = m_oFuncCheck.getCheckItem(oSelectedItem.get("partySeq"),
					oSelectedItem.get("sectionId"), oSelectedItem.get("itemIndex"));
			
			// Pre-checking if the item is missing in menu
			if (oParentFuncCheckItem.getMenuItem() == null) {
				String sErrMsg = AppGlobal.g_oLang.get()._("item_is_missing_in_menu_setup") + " ("
						+ oParentFuncCheckItem.getItemDescriptionByIndex(AppGlobal.g_oCurrentLangIndex.get()) + ")";
				showDialogBox(AppGlobal.g_oLang.get()._("error"), sErrMsg);
				return false;
			}
			
			if (oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId() != 0
					&& !oItemDiscountGrpList.contains(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId())) {
				boolean bDiscountAllowance = AppGlobal.g_oFuncDiscountAclList
						.get(AppGlobal.g_oFuncOutlet.get().getOutletId())
						.checkDiscountAcl(oParentFuncCheckItem.getMenuItem(), oSelectedDiscType);
				oDiscountAllowance.put(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId(), bDiscountAllowance);
			}
		}
		
		// check and prepare the item list for applying discount
		List<HashMap<String, Integer>> oItemIndexList = new ArrayList<HashMap<String, Integer>>();
		for (HashMap<String, Integer> oSelectedItem : oSelectedItems) {
			FuncCheckItem oParentFuncCheckItem = m_oFuncCheck.getCheckItem(oSelectedItem.get("partySeq"),
					oSelectedItem.get("sectionId"), oSelectedItem.get("itemIndex"));
			
			if (oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId() == 0)
				continue;
			if (!oDiscountAllowance.containsKey(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId()))
				continue;
			if (oDiscountAllowance.get(oParentFuncCheckItem.getMenuItem().getDiscountItemGroupId()) == false)
				continue;
			
			// check whether the selected item is available for applying discount
			if (oParentFuncCheckItem.getCheckItem().getTotal().compareTo(BigDecimal.ZERO) == 0)
				continue;
			
			oItemIndexList.add(oSelectedItem);
		}
		
		if (!m_oFuncCheck.applyDiscount("check", PosDiscountType.USED_FOR_DISCOUNT,
				oItemIndexList, oSelectedDiscType, dDiscountAmt, oDiscountCheckExtraInfoList, 0)) {
			showDialogBox(AppGlobal.g_oLang.get()._("error"), AppGlobal.g_oLang.get()._("fail_to_apply_discount"));
			return false;
		}
		
		//add action log
		AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.svc_enquiry.name(),
				PosActionLog.ACTION_RESULT_SUCCESS, m_oFuncCheck.getTableNoWithExtensionForDisplay(),
				AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(),
				AppGlobal.g_oFuncOutlet.get().getOutletId(),
				AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(),
				AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(),
				AppGlobal.g_oFuncStation.get().getStationId(), m_oFuncCheck.getCheckId(), "", "", "", "",
				"account number: " + m_sEnquiryAccNumber + " discount code: " + sDiscountCode);
		
		// handle action log
		AppGlobal.g_oActionLog.get().handleActionLog(false);
		
		return true;
	}
	
	@Override
	public void frameMembershipInterfaceResult_clickEnquiry(HashMap<String, String> oEnquiryInfo) {
		String sActionType = ACTION_TYPE_ENQUIRY;
		if (m_sCurrentOperation.equals(FrameMembershipInterface.OPERATION_SET_MEMBER_DISCOUNT))
			 sActionType = ACTION_TYPE_DISCOUNT;

		if (enquiry(oEnquiryInfo, sActionType)) {
			// Set Member Button visible
			m_oFrameMembershipInterfaceForSPC.updateAfterEnquiryButtonIsClicked();
		}
	}

	@Override
	public void frameMembershipInterfaceResult_clickApplyDisc() {
		setMember(m_oFuncMembershipInterface.m_oLastLpsSvcResponseInfo.sMemberNumber);
	}
}
