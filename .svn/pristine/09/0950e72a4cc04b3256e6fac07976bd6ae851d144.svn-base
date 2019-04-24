package app;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;
import app.FuncBenefit;
import app.FuncCheck;
import app.FuncCheckItem;
import om.InfInterface;
import om.InfVendor;
import om.MenuItemCategory;
import om.MenuItemCategoryList;
import om.MenuItemDept;
import om.MenuItemDeptList;
import om.PosCheckDiscount;
import om.PosCheckExtraInfo;
import om.PosInterfaceConfig;

public class FuncLoyalty {
	private String traceId;
	private String number;
	private String name;
	private BigDecimal oBalance;
	private BigDecimal oTotalBonusDebit;
	private String campaignCode;
	private String campaignName;
	private String campaignDesc;
	private String expiryDate;
	private BigDecimal bonusCredit;
	private BigDecimal checkBalanceBonusBalance;
	private String guestName;
	private HashMap<String, BigDecimal> bonusRecords;
	private List<FuncBenefit> oBenefitList;
	private boolean bSelected;
	private String sCertBalance;
	private String sCertExpiration;
	private String sCurrencyCode;
	
	private List<HashMap<String, HashMap<String, String>>> itemErrorReasonRecords;
	//private List<HashMap<String, HashMap<String, String>>> checkErrorReasonRecords;
	
	// Last error message
	private String m_sErrorMessage;
	
	// type
	public static String TYPE_NUMBER = "number";
	public static String TYPE_NUMBER_IN_EXTRAA_INFO = "number_extra";
	public static String TYPE_CARD_NUMBER = "cardNumber";
	
	public static int SEARCH_ALL_ACTIVE = 1;
	public static int SEARCH_ALL_SUSPENDED = 2;
	public static int SEARCH_ALL_NOT_DELETED = 3;
	
	public static String SWIPE_CARD_TYPE_LOYALTY_CARD = "0";
	public static String SWIPE_CARD_TYPE_SVC_CARD = "1";
	
	//init object with initialize value
	public FuncLoyalty () {
		this.init();
	}
	
	//init obejct with JSONObject
	public FuncLoyalty(JSONObject memberJSONObject) {
		this.init();
		readDataFromJson(memberJSONObject);
	}
	
	//init obejct with Extra Info
	public FuncLoyalty(String sMemberName, String sMemberNo, String sBonusBalance) {
		this.init();
		this.setMemberInfo(sMemberName, sMemberNo, sBonusBalance);
	}
	
	// Return the error message
	public String getLastErrorMessage(){
		String sErrorCode = m_sErrorMessage;
		String sErrorMessage = m_sErrorMessage;
			switch(sErrorCode){
			case "":
			case "0":	// OK (Request is performed without error)
				sErrorMessage = "";
				break;
			case "18000":
				sErrorMessage = AppGlobal.g_oLang.get()._("gateway_internal_error");
				break;
			case "18001":
				sErrorMessage = AppGlobal.g_oLang.get()._("error_from_loyalty_portal");
				break;
			case "18002":
				sErrorMessage = AppGlobal.g_oLang.get()._("network_error");
				break;
			case "18003":
				sErrorMessage = AppGlobal.g_oLang.get()._("invalid_member_number");
				break;
			case "18004":
				sErrorMessage = AppGlobal.g_oLang.get()._("member_login_failed");
				break;
			case "18005":
				sErrorMessage = AppGlobal.g_oLang.get()._("member_not_found");
				break;
			case "18006":
				sErrorMessage = AppGlobal.g_oLang.get()._("member_account_already_expired");
				break;
			case "18007":
				sErrorMessage = AppGlobal.g_oLang.get()._("member_account_disabled");
				break;
			case "18008":
				sErrorMessage = AppGlobal.g_oLang.get()._("member_account_in_use");
				break;
			case "18009":
				// The operation for this member is not allowed
				sErrorMessage = AppGlobal.g_oLang.get()._("operation_for_this_member_is_not_allowed");
				break;
			case "18015":
				// Transaction with the same member no. already started
				sErrorMessage = AppGlobal.g_oLang.get()._("transaction_with_same_member_number_already_started");
				break;
			case "18020":
				sErrorMessage = AppGlobal.g_oLang.get()._("duplicate_request");
				break;
			case "18021":
				sErrorMessage = AppGlobal.g_oLang.get()._("transaction_content_corrupted");
				break;
			case "18022":
				sErrorMessage = AppGlobal.g_oLang.get()._("reference_id_does_not_exist");
				break;
			case "18023":
				sErrorMessage = AppGlobal.g_oLang.get()._("transaction_already_settled");
				break;
			case "18024":
				sErrorMessage = AppGlobal.g_oLang.get()._("transaction_session_already_expired");
				break;
			case "18025":
				sErrorMessage = AppGlobal.g_oLang.get()._("past_transaction_date_is_not_allowed");
				break;
			case "18026":
				sErrorMessage = AppGlobal.g_oLang.get()._("future_transaction_date_is_not_allowed");
				break;
			case "18027":
				sErrorMessage = AppGlobal.g_oLang.get()._("invalid_request_format_in_merchant_group");
				break;
			case "18028":
				sErrorMessage = AppGlobal.g_oLang.get()._("invalid_request_value_in_merchant_group");
				break;
			case "18029":
				sErrorMessage = AppGlobal.g_oLang.get()._("duplicate_item_reference");
				break;
			case "18030":
				sErrorMessage = AppGlobal.g_oLang.get()._("missing_campaign_set");
				break;
			case "18031":
				sErrorMessage = AppGlobal.g_oLang.get()._("campaign_set_corrupted");
				break;
			case "18032":
				sErrorMessage = AppGlobal.g_oLang.get()._("failed_in_running_campaign");
				break;
			case "18033":
				sErrorMessage = AppGlobal.g_oLang.get()._("invalid_brc_code");
				break;
			case "18034":
				sErrorMessage = AppGlobal.g_oLang.get()._("invalid_benefit_code");
				break;
			case "18035":
				sErrorMessage = AppGlobal.g_oLang.get()._("member_bonus_not_enough");
				break;
			case "18040":
				sErrorMessage = AppGlobal.g_oLang.get()._("invalid_request");
				break;
			case "18041":
				sErrorMessage = AppGlobal.g_oLang.get()._("invalid_request_format");
				break;
			case "18042":
				sErrorMessage = AppGlobal.g_oLang.get()._("invalid_request_value");
				break;
			case "18043":
				sErrorMessage = AppGlobal.g_oLang.get()._("forbidden_access_gateway");
				break;
			case "18044":
				sErrorMessage = AppGlobal.g_oLang.get()._("pos_online_session_already_expired");
				break;
			case "18045":
				sErrorMessage = AppGlobal.g_oLang.get()._("unauthorized_access_gateway");
				break;
			case "18046":
				sErrorMessage = AppGlobal.g_oLang.get()._("pgp_version_not_supported");
				break;
			case "18050":
				sErrorMessage = AppGlobal.g_oLang.get()._("unauthorized_access_portal");
				break;
			case "18051":
				sErrorMessage = AppGlobal.g_oLang.get()._("loyalty_service_temporarily_unavailable");
				break;
			case "18052":
				sErrorMessage = AppGlobal.g_oLang.get()._("forbidden_access_portal");
				break;
			case "18053":
				sErrorMessage = AppGlobal.g_oLang.get()._("gateway_session_already_expired");
				break;
			case "18060":
				sErrorMessage = AppGlobal.g_oLang.get()._("internal_license_error");
				break;
			case "18061":
				sErrorMessage = AppGlobal.g_oLang.get()._("invalid_license_file");
				break;
			case "18062":
				sErrorMessage = AppGlobal.g_oLang.get()._("invalid_license_key");
				break;
			case "18063":
				sErrorMessage = AppGlobal.g_oLang.get()._("wrong_license_file");
				break;
			case "18064":
				sErrorMessage = AppGlobal.g_oLang.get()._("no_license_file_in_system");
				break;
			case "18065":
				sErrorMessage = AppGlobal.g_oLang.get()._("no_license_key_in_system");
				break;
			case "18066":
				sErrorMessage = AppGlobal.g_oLang.get()._("license_has_been_expired");
				break;
			case "18070":
				// The function has been disabled
				sErrorMessage = AppGlobal.g_oLang.get()._("function_has_been_disabled");
				break;
		}
		
		return sErrorMessage;
	}
	
	//login loyalty
	public String login(String sInterfaceId) {
		String sLoginSessionId = null;
		
		PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
		HashMap<String, String> oLoginInformation = new HashMap<String, String>();
		oLoginInformation.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oLoginInformation.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oLoginInformation.put("interfaceId", sInterfaceId);
		
		JSONObject oResult = oPosInterfaceConfig.doLoyaltyLogin(oLoginInformation);
		if(oResult != null && oResult.has("sessionId")) {
			sLoginSessionId = oResult.optString("sessionId");
			if(sLoginSessionId.isEmpty())
				return null;
			else
				return sLoginSessionId;
		}
		
		return null;
	}
	
	// get loyalty member
	public Boolean startRedeem(String sMemberNo, String sType, String sInterfaceId, String sSvcInterfaceId, FuncCheck oFuncCheck, int iMode){
		JSONObject requestJSONObject = new JSONObject();
		JSONObject itemJSONObject = new JSONObject(), benefitJSONObejct = new JSONObject();
		JSONArray itemJSONArray = new JSONArray(), benefitJSONArray = new JSONArray();
		String sDepartmentCode, sCatCode, sUseCount, sBenefitCode;
		int iItemSeq = 1, iDepartmentId;
		
		MenuItemDeptList oMenuItemDeptList = new MenuItemDeptList();
		oMenuItemDeptList.readItemDeptList();
		MenuItemCategoryList oItemCategoryList = new MenuItemCategoryList();
		oItemCategoryList.readItemCategoryList();
		
		try {
			DateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			DateTime today = AppGlobal.getCurrentTime(false);
			DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
			String oPickupDateFormat = fmt.print(today);
			
			requestJSONObject.put("interfaceId", sInterfaceId);
			requestJSONObject.put("sessionId", AppGlobal.g_oFuncStation.get().getLoyaltySessionId());
			if(sType.equals(FuncLoyalty.TYPE_NUMBER)){
				if(!sMemberNo.isEmpty())
					requestJSONObject.put("memberNumber", sMemberNo);
				else{
					requestJSONObject.put("memberNumber", oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER));
					requestJSONObject.put("referenceId", oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_TRACE_ID));
					requestJSONObject.put("mode", iMode);
				}
			}
			else if(sType.equals(FuncLoyalty.TYPE_CARD_NUMBER)){
				requestJSONObject.put("svcInterfaceId", sInterfaceId);
				requestJSONObject.put("svcSessionId", AppGlobal.g_oFuncStation.get().getLoyaltySessionId());
				requestJSONObject.put("svcCardNumber", sMemberNo);
			}
				
			requestJSONObject.put("outletId", AppGlobal.g_oFuncOutlet.get().getOutletId());
			requestJSONObject.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
			requestJSONObject.put("password", "");
			requestJSONObject.put("tableNo", oFuncCheck.getTableNo());
			requestJSONObject.put("openTime", dt1.format(oFuncCheck.getOpenLocTime().toDate()));
			if(oFuncCheck.getCloseLocTime() != null)
				requestJSONObject.put("closeTime", dt1.format(oFuncCheck.getCloseLocTime().toDate()));
			else
				requestJSONObject.put("closeTime", dt1.format(oFuncCheck.getOpenLocTime().toDate()));
			requestJSONObject.put("traceId", oFuncCheck.getCheckPrefixNo());
			requestJSONObject.put("brcCode", oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_BONUS_REDEMPTION_CODE));
			requestJSONObject.put("businessDate", oPickupDateFormat);
			requestJSONObject.put("takeout", oFuncCheck.getOrderingType());
			requestJSONObject.put("cover", oFuncCheck.getCover());
			requestJSONObject.put("checkTotal", oFuncCheck.getCheckTotal());
			requestJSONObject.put("baseTotal", oFuncCheck.getLoyaltyBaseTotal());
			for(int i = 0; i <= AppGlobal.MAX_SEATS; i++) {
				// Create ordering basket section for seat
				ArrayList<FuncCheckItem> oFuncCheckItemList = (ArrayList<FuncCheckItem>)oFuncCheck.getItemList(i);
				if(oFuncCheckItemList != null && oFuncCheckItemList.size() > 0) {					//Has ordered items in seat i
					for (int j=0; j<oFuncCheckItemList.size(); j++) {
						FuncCheckItem oFuncCheckItem = oFuncCheckItemList.get(j);
						itemJSONObject = new JSONObject();
						itemJSONObject.put("sequence", iItemSeq);
						iItemSeq++;
						itemJSONObject.put("code", oFuncCheckItem.getCheckItem().getCode());
						itemJSONObject.put("quantity", oFuncCheckItem.getCheckItem().getQty());
						itemJSONObject.put("total", oFuncCheckItem.getCheckItem().getTotal());
						if(oFuncCheckItem.getCheckItem().getOrderLocTime() != null)
							itemJSONObject.put("orderTime", dt1.format(oFuncCheckItem.getCheckItem().getOrderLocTime().toDate()));
						else
							itemJSONObject.put("orderTime", oPickupDateFormat);
						itemJSONObject.put("takeout", oFuncCheckItem.getCheckItem().getOrderingType());
						itemJSONObject.put("class", "");
						iDepartmentId = oFuncCheckItem.getCheckItem().getDepartmentId();
						sDepartmentCode = "";
						if(iDepartmentId > 0){
							for(MenuItemDept iDept : oMenuItemDeptList.getItemDeptList()){
								if(iDept.getIdepId() == iDepartmentId){
									sDepartmentCode = iDept.getCode();
									break;
								}
							}
						}
						itemJSONObject.put("departmentCode", sDepartmentCode);
						sCatCode = "";
						for(MenuItemCategory oItemCategory: oItemCategoryList.getItemCategoryList()) {
							if(oItemCategory.getIcatId() == oFuncCheckItem.getCheckItem().getCategoryId())
								sCatCode = oItemCategory.getCode();
						}
						itemJSONObject.put("categoryCode", sCatCode);
						sUseCount = "1";
						sBenefitCode = "";
						for(PosCheckExtraInfo oExtraInfo:oFuncCheckItem.getExtraInfoList()){
							if(oExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_BENEFIT_CODE))
								sBenefitCode = oExtraInfo.getValue();
						}
						itemJSONObject.put("benefitCode", sBenefitCode);
						itemJSONObject.put("useCount", sUseCount);
						itemJSONObject.put("desc", oFuncCheckItem.getCheckItem().getName(AppGlobal.g_oCurrentLangIndex.get()));
						itemJSONArray.put(itemJSONObject);
					}
				}
			}
			requestJSONObject.put("items", itemJSONArray);
			for(PosCheckDiscount oPosCheckDiscount:oFuncCheck.getCurrentPartyAppliedCheckDiscount()){
				benefitJSONObejct = new JSONObject();
				benefitJSONObejct.put("desc", oPosCheckDiscount.getName(AppGlobal.g_oCurrentLangIndex.get()));
				for(PosCheckExtraInfo oPosCheckExtraInfo:oPosCheckDiscount.getCheckExtraInfoList()){
					if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_BENEFIT_CODE)){
						benefitJSONObejct.put("method", oPosCheckDiscount.getMethod());
						benefitJSONObejct.put("benefitCode", oPosCheckExtraInfo.getValue());
					}
					if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_BENEFIT_QTY) && oPosCheckDiscount.getMethod().equals(PosCheckDiscount.METHOD_FIX_AMOUNT_DISCOUNT_PER_CHECK))
						benefitJSONObejct.put("qty", oPosCheckExtraInfo.getValue());
					if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_BENEFIT_AMOUNT))
						benefitJSONObejct.put("amount", oPosCheckExtraInfo.getValue());
				}
				if(benefitJSONObejct.has("benefitCode"))
					benefitJSONArray.put(benefitJSONObejct);
			}
			requestJSONObject.put("checkDiscounts", benefitJSONArray);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
		JSONObject oTempJSONObject = oPosInterfaceConfig.doLoyaltyStartRedeem(requestJSONObject);
		if(oTempJSONObject == null) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("cannot_connect_to_loyalty_server") + System.lineSeparator() + AppGlobal.g_oLang.get()._("fail_to_perform_loyalty_bonus_transaction");
			return false;
		}else {
			if (!oTempJSONObject.has("traceId")) {
				this.init();
				if(oTempJSONObject.has("errorCode"))
					m_sErrorMessage = oTempJSONObject.optString("errorCode");
				else
					m_sErrorMessage = AppGlobal.g_oLang.get()._("invalid_response");
				return false;
			}
			
			if(iMode != 1){
				itemErrorReasonRecords = new ArrayList<HashMap<String, HashMap<String, String>>>();
				try {
					itemErrorReasonRecords = checkItemBenefitRedemption(itemJSONArray, benefitJSONArray, oTempJSONObject);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(itemErrorReasonRecords.size() > 0){
					m_sErrorMessage = AppGlobal.g_oLang.get()._("redemption_failed");
					return false;
				}
			}
			
			JSONObject oMemberDetailJSONObject = oTempJSONObject;
			this.init();
			setMemberInfo(oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_MEMBER_NAME), oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER), oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_BONUS_BALANCE));
			readDataFromJson(oMemberDetailJSONObject);
		}
		
		return true;
	}
	
	// close transaction
	public Boolean closeLoyaltyTransaction(String sReferenceId, String sInterfaceId, BigDecimal oBonusRedeemed){
		HashMap<String, String> oCloseTransInfo = new HashMap<String, String>();
		m_sErrorMessage = "";
		oCloseTransInfo.put("sessionId", AppGlobal.g_oFuncStation.get().getLoyaltySessionId());
		oCloseTransInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oCloseTransInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oCloseTransInfo.put("referenceId", sReferenceId);
		oCloseTransInfo.put("interfaceId", sInterfaceId);
		oCloseTransInfo.put("bonusRedeemed", oBonusRedeemed.toPlainString());
		
		PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
		boolean bResult = oPosInterfaceConfig.doLoyaltyCloseTransaction(oCloseTransInfo);
		if(!bResult) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("cannot_connect_to_master_server") + System.lineSeparator() + AppGlobal.g_oLang.get()._("fail_to_perform_loyalty_bonus_transaction");
			return false;
		}
		
		return true;
	}
	
	// check balance
	public Boolean checkBalance(String sEnquiryNumber, PosInterfaceConfig oLoyaltyInterfaceConfig, int iCheckNo){
		HashMap<String, String> oCheckingInfo = new HashMap<String, String>();
		
		oCheckingInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oCheckingInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oCheckingInfo.put("interfaceId", String.valueOf(oLoyaltyInterfaceConfig.getInterfaceId()));
		oCheckingInfo.put("interfaceCode", oLoyaltyInterfaceConfig.getInterfaceCode());
		
		if(oLoyaltyInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY)){
			oCheckingInfo.put("sessionId", AppGlobal.g_oFuncStation.get().getLoyaltySessionId());
			oCheckingInfo.put("memberNumber", sEnquiryNumber);
			oCheckingInfo.put("password", "");
		}else if(oLoyaltyInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GIVEX)){
			JSONObject itemsJson = new JSONObject();
			DateTime today = AppGlobal.getCurrentTime(false);
			DateTimeFormatter fmt = DateTimeFormat.forPattern("HHmmss");
			String sCurrntTimeStamp = fmt.print(today);
			try {
				itemsJson.put("svcCardNumber", sEnquiryNumber);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			oCheckingInfo.put("transactionCode", iCheckNo+sCurrntTimeStamp);
			oCheckingInfo.put("items", itemsJson.toString());
			
			//lang code from setup
			if(!oLoyaltyInterfaceConfig.getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").optJSONObject("lang_code").optString("value").isEmpty()){
				oCheckingInfo.put("langCode", oLoyaltyInterfaceConfig.getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").optJSONObject("lang_code").optString("value"));
			}else{
				m_sErrorMessage = AppGlobal.g_oLang.get()._("missing_setup");
				return false;
			}
			
			//login name
			if(!oLoyaltyInterfaceConfig.getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").optJSONObject("external_log_in").optString("value").isEmpty()){
				oCheckingInfo.put("externalLogin", oLoyaltyInterfaceConfig.getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").optJSONObject("external_log_in").optString("value"));
			}else{
				m_sErrorMessage = AppGlobal.g_oLang.get()._("missing_setup");
				return false;
			}
			
			//login password
			if(!oLoyaltyInterfaceConfig.getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").optJSONObject("external_password").optString("value").isEmpty()){
				oCheckingInfo.put("externalPassword", oLoyaltyInterfaceConfig.getInterfaceConfig().optJSONObject("connection_setup").optJSONObject("params").optJSONObject("external_password").optString("value"));
			}else{
				m_sErrorMessage = AppGlobal.g_oLang.get()._("missing_setup");
				return false;
			}
		}
		
		PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
		JSONObject oTempJSONObject = oPosInterfaceConfig.doLoyaltyCheckBalance(oCheckingInfo, oLoyaltyInterfaceConfig.getInterfaceVendorKey());
		if(oTempJSONObject == null)
			return false;
		else {
			if(oLoyaltyInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY)){
				this.checkBalanceBonusBalance =  new BigDecimal("0.0");
				if (oTempJSONObject.has("bonusBalance")){
					if (!oTempJSONObject.optString("bonusBalance").isEmpty())
						this.checkBalanceBonusBalance =  new BigDecimal(oTempJSONObject.optString("bonusBalance", "0.0"));
				}
				oTempJSONObject = oTempJSONObject.optJSONObject("member");
				this.readDataFromJson(oTempJSONObject);
			}else if(oLoyaltyInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GIVEX)){
				if (!oTempJSONObject.optBoolean("loyaltyResult"))
					return false;	
				else
					if(oTempJSONObject.has("pointsBalance"))
						this.oBalance = new BigDecimal(oTempJSONObject.optString("pointsBalance"));
				this.readDataFromJson(oTempJSONObject);
			}
		}
		return true;
	}
	
	//Cancel redeem
	public Boolean cancelRedeem(String sInterfaceId, String sInterfaceCode, String sTraceId){
		HashMap<String, String> oCancelRedeemInfo = new HashMap<String, String>();
		oCancelRedeemInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oCancelRedeemInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oCancelRedeemInfo.put("interfaceId", sInterfaceId);
		oCancelRedeemInfo.put("interfaceCode", sInterfaceCode);
		oCancelRedeemInfo.put("sessionId", AppGlobal.g_oFuncStation.get().getLoyaltySessionId());
		oCancelRedeemInfo.put("referenceId", sTraceId);
		
		PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
		return oPosInterfaceConfig.doLoyaltyCancelRedeem(oCancelRedeemInfo);
	}
	
	//Release transaction
	public Boolean releaseTrans(HashMap<String, String> oReleaseTransRedeemInfo){
		oReleaseTransRedeemInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oReleaseTransRedeemInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
		return oPosInterfaceConfig.doLoyaltyReleaseTrans(oReleaseTransRedeemInfo);
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject memberJSONObject) {
		JSONObject tempJSONObject = null;
		JSONArray tempJSONArray = null;
		
		if(this.number.isEmpty())
			this.number = memberJSONObject.optString("memberNo");
		this.traceId = memberJSONObject.optString("traceId");
		if(this.name.isEmpty())
			this.name = memberJSONObject.optString("memberName");
		
		if(memberJSONObject.has("bonusBalance") && !memberJSONObject.optString("bonusBalance").isEmpty())
			this.oBalance = new BigDecimal(memberJSONObject.optString("bonusBalance", "0.0"));
		else{
			if(this.oBalance.equals(BigDecimal.ZERO))
				this.oBalance = BigDecimal.ZERO;
		}
		
		this.oTotalBonusDebit = new BigDecimal(memberJSONObject.optString("totalBonusDebit", "0.0"));
		this.campaignCode = memberJSONObject.optString("brcCode");
		this.campaignName = memberJSONObject.optString("brcName");
		this.campaignDesc = memberJSONObject.optString("brcDesc");
		this.expiryDate = memberJSONObject.optString("expiryDate");
		if(memberJSONObject.has("bonusCredit") && !memberJSONObject.optString("bonusCredit").isEmpty())
			this.bonusCredit = new BigDecimal(memberJSONObject.optString("bonusCredit", "0.0"));
		else
			this.bonusCredit = BigDecimal.ZERO;
		this.oBenefitList = new ArrayList<FuncBenefit>();
		
		if (memberJSONObject.has("certificateBalance"))
			this.sCertBalance = memberJSONObject.optString("certificateBalance", " ");
		
		if (memberJSONObject.has("certificateExpirationDate"))
			this.sCertExpiration = memberJSONObject.optString("certificateExpirationDate", " ");
		
		if(memberJSONObject.has("currencyCode"))
			this.sCurrencyCode = memberJSONObject.optString("currencyCode", " ");
		
		try {
			if(memberJSONObject.has("bonusRecord")){
				tempJSONArray = memberJSONObject.getJSONArray("bonusRecord");
				for (int i = 0; i < tempJSONArray.length(); i++) {
					tempJSONObject = tempJSONArray.optJSONObject(i);
					this.bonusRecords.put(tempJSONObject.optString("expiryMonth"), BigDecimal.valueOf(tempJSONObject.optDouble("monthlyBonus")));
				}
			}
			
			if (memberJSONObject.has("benefits")) {
				JSONArray oBenefitJSONArray = memberJSONObject.optJSONArray("benefits");
				for (int i = 0; i < oBenefitJSONArray.length(); i++) {
					JSONObject oBenefitJSONObject = new JSONObject();
					oBenefitJSONObject.put("benefit", oBenefitJSONArray.optJSONObject(i));
					FuncBenefit oFuncBenefit = new FuncBenefit(oBenefitJSONObject);
					
					if ((!oFuncBenefit.isNoCondition() || oFuncBenefit.getMaxCount() <= 0 ) && oFuncBenefit.getAvaCount() == 0 && oFuncBenefit.getBonusDebit() > 0)
						continue;
					
					this.oBenefitList.add(oFuncBenefit);
				}
			}
			
			if (memberJSONObject.has("memb_display_name"))
				this.guestName = memberJSONObject.optString("memb_display_name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.bSelected = false;
	}
	
	//init object
	public void init() {
		this.number = "";
		this.traceId = "";
		this.name = "";
		this.campaignCode = "";
		this.campaignName = "";
		this.campaignDesc = "";
		this.expiryDate = "";
		this.bonusCredit = BigDecimal.ZERO;
		this.oBalance = BigDecimal.ZERO;
		this.oTotalBonusDebit = BigDecimal.ZERO;
		this.bonusRecords = new HashMap<String, BigDecimal>();
		this.oBenefitList = new ArrayList<FuncBenefit>();
		this.bSelected = false;
		this.guestName = "";
		this.sCertBalance = "";
		this.sCertExpiration = "";
		this.sCurrencyCode = "";
	}
	
	//set member info by extrainfo
	public void setMemberInfo(String sMemberName, String sMemberNo, String sBonusBalance){
		if(sMemberNo == null || sMemberNo.isEmpty())
			this.number = "";
		else
			this.number = sMemberNo;
		
		if(sMemberName == null || sMemberName.isEmpty())
			this.name = "";
		else
			this.name = sMemberName;
		
		if(sBonusBalance == null || sBonusBalance.isEmpty())
			this.oBalance = BigDecimal.ZERO;
		else
			this.oBalance = new BigDecimal(sBonusBalance);
	}
	
	public List<HashMap<String, HashMap<String, String>>> checkItemBenefitRedemption(JSONArray itemJson, JSONArray checkJson, JSONObject responseJson) throws JSONException {
		List<HashMap<String, HashMap<String, String>>> failRedeemBenefitRecords = new ArrayList<HashMap<String, HashMap<String, String>>>();
		
		for (int i = 0; i < itemJson.length(); i++) {
			JSONObject oBenefitJSONObject = new JSONObject();
			oBenefitJSONObject = itemJson.optJSONObject(i);
			if(oBenefitJSONObject.has("benefitCode") && oBenefitJSONObject.optString("benefitCode") != ""){
				HashMap<String, HashMap<String, String>> failRedeemBenefit = new HashMap<String, HashMap<String, String>>();
				HashMap<String, String> failRedeemBenefitDetail = new HashMap<String, String>();
				failRedeemBenefitDetail.put(oBenefitJSONObject.optString("desc"), oBenefitJSONObject.optString("total"));
				failRedeemBenefit.put(oBenefitJSONObject.optString("benefitCode"), failRedeemBenefitDetail);
				failRedeemBenefitRecords.add(failRedeemBenefit);
			}
		}
		
		if(responseJson.has("items")){
			JSONArray oItemBenenfitJSONArray = new JSONArray();
			oItemBenenfitJSONArray = responseJson.getJSONArray("items");
			for (int i = 0; i < oItemBenenfitJSONArray.length(); i++) {
				JSONObject oBenefitJSONObject = new JSONObject();
				oBenefitJSONObject = oItemBenenfitJSONArray.optJSONObject(i);
				if(oBenefitJSONObject.has("benefitCode") && oBenefitJSONObject.optString("benefitCode") != ""){
					for(int y = 0; y < failRedeemBenefitRecords.size(); y++){
						if(failRedeemBenefitRecords.get(y).containsKey(oBenefitJSONObject.optString("benefitCode"))){
							failRedeemBenefitRecords.remove(y);
							break;
						}
					}
				}
			}
		}
		
		return failRedeemBenefitRecords;
	}
	
	//get member no.
	public String getMemberNo() {
		return this.number;
	}
	
	public String getTraceId(){
		return this.traceId;
	}
	
	//get display name
	public String getName() {
		return this.name;
	}
	
	//get account balance
	public BigDecimal getBalance(){
		return this.oBalance;
	}
	
	public BigDecimal getTotalBonusDebit(){
		return this.oTotalBonusDebit;
	}
	
	
	//get Campaign Code
	public String getCampaignCode(){
		return this.campaignCode;
	}
	
	//get Campaign Name
	public String getCampaignName(){
		return this.campaignName;
	}
	
	//get Campaign Desc
	public String getCampaignDesc(){
		return this.campaignDesc;
	}
	
	//get Expiry Date
	public String getExpiryDate(){
		return this.expiryDate;
	}
	
	//get Bonus Credit (Loyalty Bonus Use)
	public BigDecimal getBonusCredit(){
		return this.bonusCredit;
	}
	
	//get the user bouns expire
	public HashMap<String, BigDecimal> getBounsRecords(){
		return this.bonusRecords;
	}
	
	//get guest name
	public String getGuestName(){
		return this.guestName;
	}
	
	//get bonus balance
	public BigDecimal getcheckBalanceBonusBalance(){
		return this.checkBalanceBonusBalance;
	}
	
	//get the user benefit balance
	public int getBenefitBalance(){
		int iBenefitBalance = 0;
		if(!this.oBenefitList.isEmpty()){
			for(FuncBenefit oBenefit:this.oBenefitList){
				iBenefitBalance += oBenefit.getBonusDebit();
			}
		}
		return iBenefitBalance;
	}
	
	public List<FuncBenefit> getBenefitList(){
		return this.oBenefitList;
	}
	
	//get the Error Reason Records
	public List<HashMap<String, HashMap<String, String>>> getItemErrorReasonRecords(){
		return this.itemErrorReasonRecords;
	}
	
	// Get manual key-in control setting
	public static String[] getKeyInControls() {
		String sKeyInControls[] = null;
		List<PosInterfaceConfig> oLoyaltyInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		// Get the configure from interface module
		oLoyaltyInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_LOYALTY_INTERFACE);
		JSONObject oLoyaltyInterfaceConfig = null;
		for (PosInterfaceConfig oPosInterfaceConfig : oLoyaltyInterfaceConfigList) {
			if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY)) {
				oLoyaltyInterfaceConfig = oPosInterfaceConfig.getInterfaceConfig();
				if(oLoyaltyInterfaceConfig.has("general_setup") && oLoyaltyInterfaceConfig.optJSONObject("general_setup").has("params") && oLoyaltyInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").has("swipe_card_control"))
					sKeyInControls = oLoyaltyInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").optJSONObject("swipe_card_control").optString("value").split(",");
			}
		}
		return sKeyInControls;
	}
	
	//get certificate balance with currency code
	public String getCertBalance(){
		return this.sCertBalance;
	}
	
	//get currency code
	public String getCurrencyCode(){
		return this.sCurrencyCode;
	}
	//get cert expiration
	public String getsCertExpiration() {
		return sCertExpiration;
	}
}


/*package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import om.OmWsClientGlobal;
import om.PosCheckDiscount;
import om.PosCheckExtraInfo;
import om.PosCheckItem;

public class FuncLoyalty {
	class LoyaltyInfo {
		int iShopId;
		int iOutletId;
		int iLangIndex;
		String sMemberNo;
		String sUuid;
		String sCheckNo;
		String sTableNo;
		int iGuestNum;
		int iUserId;
		BigDecimal oTotal;
		BigDecimal oBaseTotal;
		String sTransTime;
		String sOpenTime;
		String sCloseTime;
		String sRemark;
		String sBonusCredit;
		String sBonusDebit;
		List<PosCheckItem> oCheckItemList;
		List<PosCheckDiscount> oCheckDiscountList;
	}

	class LoyaltyResponse {
		boolean bSuccess;
		int iErrorCode;
		String sErrorMessage;
		String sUuid;
		String sMemberName;
		String sMemberNo;
		String sCampaignCode;
		String sCampaignName;
		String sCampaignDesc;
		int iBonusCredit;
		int iBonusDebit;
		int iBonusBalance;
		String sValidThrough;
		List<FuncBenefit> oBenefitList;
	}
	private LoyaltyResponse m_oLoyaltyResponse;
	
	FuncLoyalty() {
		m_oLoyaltyResponse = new LoyaltyResponse();
		m_oLoyaltyResponse.bSuccess = false;
		m_oLoyaltyResponse.iErrorCode = 0;
		m_oLoyaltyResponse.sErrorMessage = "";
		m_oLoyaltyResponse.sUuid = "";
		m_oLoyaltyResponse.sMemberName = "";
		m_oLoyaltyResponse.sMemberNo = "";
		m_oLoyaltyResponse.sCampaignCode = "";
		m_oLoyaltyResponse.sCampaignName = "";
		m_oLoyaltyResponse.sCampaignDesc = "";
		m_oLoyaltyResponse.iBonusCredit = 0;
		m_oLoyaltyResponse.iBonusDebit = 0;
		m_oLoyaltyResponse.iBonusBalance = 0;
		m_oLoyaltyResponse.sValidThrough = "";
		m_oLoyaltyResponse.oBenefitList = new ArrayList<FuncBenefit>();
	}
	
	private JSONObject formLoyaltyJSONObject(LoyaltyInfo oLoyaltyInfo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("shopId", oLoyaltyInfo.iShopId);
			requestJSONObject.put("outletId", oLoyaltyInfo.iOutletId);
			requestJSONObject.put("langIndex", oLoyaltyInfo.iLangIndex);
			requestJSONObject.put("memberNo", oLoyaltyInfo.sMemberNo);
			requestJSONObject.put("uuid", oLoyaltyInfo.sUuid);
			requestJSONObject.put("transNo", oLoyaltyInfo.sCheckNo);
			requestJSONObject.put("transDT", oLoyaltyInfo.sTransTime);
			requestJSONObject.put("openTime", oLoyaltyInfo.sOpenTime);
			requestJSONObject.put("closeTime", oLoyaltyInfo.sCloseTime);
			requestJSONObject.put("cover", oLoyaltyInfo.iGuestNum);
			requestJSONObject.put("tableNo", oLoyaltyInfo.sTableNo);
			requestJSONObject.put("total", oLoyaltyInfo.oTotal.toString());
			requestJSONObject.put("baseTotal", oLoyaltyInfo.oBaseTotal.toString());
			requestJSONObject.put("remark", oLoyaltyInfo.sRemark);
			requestJSONObject.put("userId", oLoyaltyInfo.iUserId);

			if (oLoyaltyInfo.oCheckItemList != null && !oLoyaltyInfo.oCheckItemList.isEmpty()) {
				PosCheckItem oCheckItem = new PosCheckItem();
				JSONArray oCheckItemJSONArray = oCheckItem.constructMultipleItemJSONForLoyalty(oLoyaltyInfo.oCheckItemList);
				requestJSONObject.put("items", oCheckItemJSONArray);
			}

			if (oLoyaltyInfo.oCheckDiscountList != null && !oLoyaltyInfo.oCheckDiscountList.isEmpty()) {
				PosCheckDiscount oCheckDiscount = new PosCheckDiscount();
				JSONArray oCheckItemJSONArray = oCheckDiscount.constructMultipleDiscountJSONForLoyalty(oLoyaltyInfo.oCheckDiscountList);
				for (int i = 0; i < oCheckItemJSONArray.length(); i++)
					requestJSONObject.append("items", oCheckItemJSONArray.get(i));
			}
		} catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		
		return requestJSONObject;
	}
	
	public boolean beginTrans(String sMemberNo, FuncCheck oFuncCheck, List<PosCheckItem> oCheckItemList) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		// Get current time
		DateTime oCurrentTime = new DateTime();
		String sCurrentTime = formatter.print(oCurrentTime);

		String sOpenTime = (oFuncCheck.getOpenLocTime() == null)? "": formatter.print(oFuncCheck.getOpenLocTime());
		String sCloseTime = (oFuncCheck.getCloseLocTime() == null)? "": formatter.print(oFuncCheck.getCloseLocTime());
		
		LoyaltyInfo oLoyaltyInfo = new LoyaltyInfo();
		oLoyaltyInfo.iShopId = AppGlobal.g_oFuncOutlet.get().getShopId();
		oLoyaltyInfo.iOutletId = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oLoyaltyInfo.iLangIndex = AppGlobal.g_oCurrentLangIndex.get();
		oLoyaltyInfo.sMemberNo = sMemberNo;
		oLoyaltyInfo.sCheckNo = oFuncCheck.getCheckPrefixNo();
		oLoyaltyInfo.oTotal = oFuncCheck.getCheckTotal();
		oLoyaltyInfo.sTableNo = oFuncCheck.getTableNoWithExtensionForDisplay();
		oLoyaltyInfo.iGuestNum = oFuncCheck.getCover();
		oLoyaltyInfo.oTotal = oFuncCheck.getCheckTotal();
		oLoyaltyInfo.oBaseTotal = oFuncCheck.getItemTotal();
		oLoyaltyInfo.sTransTime = sCurrentTime;
		oLoyaltyInfo.sOpenTime = sOpenTime;
		oLoyaltyInfo.sCloseTime = sCloseTime;
		oLoyaltyInfo.oCheckItemList = oCheckItemList;
		JSONObject requestJSONObject = formLoyaltyJSONObject(oLoyaltyInfo);
		
		if (!OmWsClientGlobal.g_oWsClientForHq.get().call("gm", "loyalty", "beginTrans", requestJSONObject.toString(), true)) {
			this.m_oLoyaltyResponse.sErrorMessage = AppGlobal.g_oLang.get()._("cannot_connect_to_master_server") +"\n"+ AppGlobal.g_oLang.get()._("fail_to_perform_loyalty_bonus_transaction");
			return false;
		} else {
			JSONObject oLoyaltyResultJSON = OmWsClientGlobal.g_oWsClient.get().getResponse();
			readDataFromJson(oLoyaltyResultJSON);
		}
		
		return true;
	}

	public boolean calcTrans(String sMemberNo, FuncCheck oFuncCheck, List<PosCheckItem> oCheckItemList, List<PosCheckDiscount> oCheckDiscountList) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		// Get current time
		DateTime oCurrentTime = new DateTime();
		String sCurrentTime = formatter.print(oCurrentTime);

		String sOpenTime = (oFuncCheck.getOpenLocTime() == null)? "": formatter.print(oFuncCheck.getOpenLocTime());
		String sCloseTime = (oFuncCheck.getCloseLocTime() == null)? "": formatter.print(oFuncCheck.getCloseLocTime());
		
		LoyaltyInfo oLoyaltyInfo = new LoyaltyInfo();
		oLoyaltyInfo.iShopId = AppGlobal.g_oFuncOutlet.get().getShopId();
		oLoyaltyInfo.iOutletId = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oLoyaltyInfo.iLangIndex = AppGlobal.g_oCurrentLangIndex.get();
		String sUuid = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_UUID);
		oLoyaltyInfo.sUuid = sUuid;
		oLoyaltyInfo.sCheckNo = oFuncCheck.getCheckPrefixNo();
		oLoyaltyInfo.oTotal = oFuncCheck.getCheckTotal();
		oLoyaltyInfo.sTableNo = oFuncCheck.getTableNoWithExtensionForDisplay();
		oLoyaltyInfo.iGuestNum = oFuncCheck.getCover();
		oLoyaltyInfo.oTotal = oFuncCheck.getCheckTotal();
		oLoyaltyInfo.oBaseTotal = oFuncCheck.getItemTotal();
		oLoyaltyInfo.sTransTime = sCurrentTime;
		oLoyaltyInfo.sOpenTime = sOpenTime;
		oLoyaltyInfo.sCloseTime = sCloseTime;
		oLoyaltyInfo.sMemberNo = sMemberNo;
		oLoyaltyInfo.oCheckItemList = oCheckItemList;
		oLoyaltyInfo.oCheckDiscountList = oCheckDiscountList;
		JSONObject requestJSONObject = this.formLoyaltyJSONObject(oLoyaltyInfo);
		
		if (!OmWsClientGlobal.g_oWsClientForHq.get().call("gm", "loyalty", "calcTrans", requestJSONObject.toString(), true)) {
			this.m_oLoyaltyResponse.sErrorMessage = AppGlobal.g_oLang.get()._("cannot_connect_to_master_server") +"\n"+ AppGlobal.g_oLang.get()._("fail_to_perform_loyalty_bonus_transaction");
			return false;
		} else {
			JSONObject oLoyaltyResultJSON = OmWsClientGlobal.g_oWsClient.get().getResponse();
			readDataFromJson(oLoyaltyResultJSON);
		}
		
		return true;
	}
	
	public boolean closeTrans(FuncCheck oFuncCheck, String sRemark) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		// Get current time
		DateTime oCurrentTime = new DateTime();
		String sCurrentTime = formatter.print(oCurrentTime);

		String sOpenTime = (oFuncCheck.getOpenLocTime() == null)? "": formatter.print(oFuncCheck.getOpenLocTime());
		String sCloseTime = (oFuncCheck.getCloseLocTime() == null)? "": formatter.print(oFuncCheck.getCloseLocTime());
		
		LoyaltyInfo oLoyaltyInfo = new LoyaltyInfo();
		oLoyaltyInfo.iShopId = AppGlobal.g_oFuncOutlet.get().getShopId();
		oLoyaltyInfo.iOutletId = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oLoyaltyInfo.iLangIndex = AppGlobal.g_oCurrentLangIndex.get();
		String sUuid = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_UUID);
		oLoyaltyInfo.sUuid = sUuid;
		oLoyaltyInfo.iUserId = AppGlobal.g_oFuncUser.get().getUserId();
		oLoyaltyInfo.sMemberNo = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER);
		oLoyaltyInfo.sCheckNo = oFuncCheck.getCheckPrefixNo();
		oLoyaltyInfo.oTotal = oFuncCheck.getCheckTotal();
		oLoyaltyInfo.sTableNo = oFuncCheck.getTableNoWithExtensionForDisplay();
		oLoyaltyInfo.iGuestNum = oFuncCheck.getCover();
		oLoyaltyInfo.oTotal = oFuncCheck.getCheckTotal();
		oLoyaltyInfo.oBaseTotal = oFuncCheck.getItemTotal();
		oLoyaltyInfo.sTransTime = sCurrentTime;
		oLoyaltyInfo.sOpenTime = sOpenTime;
		oLoyaltyInfo.sCloseTime = sCloseTime;
		oLoyaltyInfo.sRemark = sRemark;
		String sBonusDebit = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_POINTS_EARN);
		oLoyaltyInfo.sBonusDebit = sBonusDebit;
		JSONObject requestJSONObject = this.formLoyaltyJSONObject(oLoyaltyInfo);
		
		if (!OmWsClientGlobal.g_oWsClientForHq.get().call("gm", "loyalty", "closeTrans", requestJSONObject.toString(), true)) {
			this.m_oLoyaltyResponse.sErrorMessage = AppGlobal.g_oLang.get()._("cannot_connect_to_master_server") +"\n"+ AppGlobal.g_oLang.get()._("fail_to_perform_loyalty_bonus_transaction");
			return false;
		} else {
			JSONObject oLoyaltyResultJSON = OmWsClientGlobal.g_oWsClient.get().getResponse();
			readDataFromJson(oLoyaltyResultJSON);
		}
		
		return true;
	}
	
	public void readDataFromJson(JSONObject oLoyaltyResultJSON) {
		if (!oLoyaltyResultJSON.optBoolean("loyaltyResult")) {
			m_oLoyaltyResponse.iErrorCode = oLoyaltyResultJSON.optInt("errorCode");

			if (m_oLoyaltyResponse.iErrorCode != 0)
				m_oLoyaltyResponse.sErrorMessage = getErrorMessage(m_oLoyaltyResponse.iErrorCode);
			else
				m_oLoyaltyResponse.sErrorMessage = oLoyaltyResultJSON.optString("errorMessage");
		} else {
			if (oLoyaltyResultJSON.has("uuid"))
				m_oLoyaltyResponse.sUuid = oLoyaltyResultJSON.optString("uuid");
			if (oLoyaltyResultJSON.has("memberName"))
				m_oLoyaltyResponse.sMemberName = oLoyaltyResultJSON.optString("memberName");
			if (oLoyaltyResultJSON.has("memberNo"))
				m_oLoyaltyResponse.sMemberNo = oLoyaltyResultJSON.optString("memberNo");
			if (oLoyaltyResultJSON.has("brcName"))
				m_oLoyaltyResponse.sCampaignName = oLoyaltyResultJSON.optString("brcName");
			if (oLoyaltyResultJSON.has("brcDesc"))
				m_oLoyaltyResponse.sCampaignDesc = oLoyaltyResultJSON.optString("brcDesc");
			if (oLoyaltyResultJSON.has("brcCode"))
				m_oLoyaltyResponse.sCampaignCode = oLoyaltyResultJSON.optString("brcCode");
			if (oLoyaltyResultJSON.has("totalBonusDebit"))
				m_oLoyaltyResponse.iBonusDebit = oLoyaltyResultJSON.optInt("totalBonusDebit");
			if (oLoyaltyResultJSON.has("totalBonusCredit"))
				m_oLoyaltyResponse.iBonusCredit = oLoyaltyResultJSON.optInt("totalBonusCredit");
			if (oLoyaltyResultJSON.has("bonusBalance"))
				m_oLoyaltyResponse.iBonusBalance = oLoyaltyResultJSON.optInt("bonusBalance");
			if (oLoyaltyResultJSON.has("validThrough"))
				m_oLoyaltyResponse.sValidThrough = oLoyaltyResultJSON.optString("validThrough");
			if (oLoyaltyResultJSON.has("benefits")) {
				JSONArray oBenefitJSONArray = oLoyaltyResultJSON.optJSONArray("benefits");
				for (int i = 0; i < oBenefitJSONArray.length(); i++) {
					JSONObject oBenefitJSONObject = oBenefitJSONArray.optJSONObject(i);
					FuncBenefit oFuncBenefit = new FuncBenefit(oBenefitJSONObject);
					
					if (oFuncBenefit.isFailCondition())
						continue;
					
					if (oFuncBenefit.getMaxCount() > 0 && oFuncBenefit.getUseCount() >= oFuncBenefit.getMaxCount())
						continue;
					
					m_oLoyaltyResponse.oBenefitList.add(oFuncBenefit);
				}
			}
		}
	}

	
	private String getErrorMessage(int iErrorCode) {
		String sErrorMessage = "";
		
		switch (iErrorCode) {
		case 1:
			sErrorMessage = AppGlobal.g_oLang.get()._("benefit_over_max_count");
			break;
		case 2:
			sErrorMessage = AppGlobal.g_oLang.get()._("member_not_found");
			break;
		case 3:
			sErrorMessage = AppGlobal.g_oLang.get()._("not_allow_to_redeem_this_benefit");
			break;
		case 4:
			sErrorMessage = AppGlobal.g_oLang.get()._("not_enough_bonus_for_debit");
			break;
		default:	
		case 0:
			sErrorMessage = null;
			break;
		}
		
		return sErrorMessage;
	}
	
	public String getUuid() {
		return m_oLoyaltyResponse.sUuid;
	}
	
	public String getMemberName() {
		return m_oLoyaltyResponse.sMemberName;
	}
	
	public String getCampaignName() {
		return m_oLoyaltyResponse.sCampaignName;
	}
	
	public int getBonusDebit() {
		return m_oLoyaltyResponse.iBonusDebit;
	}
	
	public int getBonusCredit() {
		return m_oLoyaltyResponse.iBonusCredit;
	}
	
	public int getBonusBalance() {
		return m_oLoyaltyResponse.iBonusBalance;
	}
	
	public String getValidThrough() {
		return m_oLoyaltyResponse.sValidThrough;
	}
	
	public String getErrorMessage() {
		return m_oLoyaltyResponse.sErrorMessage;
	}
	
	public List<FuncBenefit> getBenefitList() {
		return m_oLoyaltyResponse.oBenefitList;
	}
}*/
