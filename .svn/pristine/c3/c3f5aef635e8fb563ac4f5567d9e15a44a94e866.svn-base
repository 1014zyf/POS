package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;
import app.FuncCheck;
import om.InfInterface;
import om.InfVendor;
import om.MenuItemCategoryList;
import om.MenuItemDeptList;
import om.OmWsClientGlobal;
import om.PosCheckExtraInfo;
import om.PosInterfaceConfig;

public class FuncLoyaltySvc {
	private String traceId;
	private String number;
	private String cardNumber;
	private String name;
	private String displayName;
	private String lastName;
	private String firstName;
	private String type;
	private String status;
	private BigDecimal checkBalanceBonusBalance;
	private String memberValidThrough;
	private String issueDate;
	private String cardIssueDt;
	private String cardSeller;
	private String cardBuyerName;
	private String cardBuyerPassportId;
	private String cardBuyerContactNumber;
	private String assocMemberNo;
	private String assocLedgerAccount;
	private String firstSvcNumber;
	private String cardHistory;
	private String subMemberType;
	private String creditDate;
	private String remark;
	private String totalSpending;
	private HashMap<String, String> spendingVariable;
	private boolean bIsAllowAddValue;
	private boolean bIsAllowIssueBonusCard;
	private boolean bIsAllowIssueAsBonusCard;
	private int iProfileIndex;
	private int iLangIndex;
	private JSONObject sInterfaceConfig;
	private String sItemCode;
	private String sAddValueItemCode;
	private BigDecimal oMaxAllowedAmount;
	private String[][] sIssueValueDesc;
	private BigDecimal[] oIssueValue;
	private BigDecimal oPointEarn;
	
	private String[][] sAddValueDesc;
	private BigDecimal[] oAddValue;
	private String[] sAddValueDefaultPayment;
	
	private String[] sDefaultCardLevel;
	private String[] sDefaultPaymentCode;
	private String[] oAllowedPaymentCode;
	
	private String[][] sChildIssueValueDesc;
	private BigDecimal[] oChildIssueValue;
	private String[] sChildDefaultCardLevel;
	private String[] sChildDefaultPaymentCode;
	private String[] sAllowPaymentCode;
	private String sLoyaltyExpiryDate;
	private BigDecimal oSVCBalance;
	private BigDecimal oBalance;
	private HashMap<String, BigDecimal> balanceRecords;
	private HashMap<String, BigDecimal> bonusRecords;
	private String[] sCardTypeDesc;
	private String sTransReference;
	private String sCardTypeAssociateMemberControl;
	private String sAutoTopUpInformation;
		
	// Last error message
	private String m_sErrorMessage;
	
	// Last error code
	private String m_sErrorCode;
	
	// type
	public static String TYPE_NUMBER = "number";
	public static String TYPE_NUMBER_IN_EXTRAA_INFO = "number_extra";
	public static String TYPE_CARD_NUMBER = "cardNumber";
	
	// card type
	public static String CARD_TYPE_SVC = "svc";
	public static String CARD_TYPE_LOYALTY = "loyalty";
	
	// Status
	public static String STATUS_ACTIVE = "Activated";
	public static String STATUS_READY_FOR_SALE = "Ready for sale";
	public static String STATUS_SUSPENDED = "Suspended";
	public static String STATUS_TRANSFERRED = "Transferred";
	public static String STATUS_IN_STOCK = "In stock";
	
	public static int SEARCH_ALL_ACTIVE = 1;
	public static int SEARCH_ALL_SUSPENDED = 2;
	public static int SEARCH_ALL_NOT_DELETED = 3;
	
	public static int EXTENSION_EXPIRY_DATE = 1;
	public static int EXTENSION_SVC_VALID_THROUGH = 2;
	public static int EXTENSION_MEMBER_VALID_THROUGH = 3;
	
	//init object with initialize value
	public FuncLoyaltySvc () {
		this.init();
	}
	
	//init obejct with JSONObject
	public FuncLoyaltySvc(JSONObject memberJSONObject) throws JSONException {
		this.init();
		readDataFromJson(memberJSONObject);
	}
	
	// Return the error message
	public String getLastErrorMessage(){
		String sErrorCode = m_sErrorCode;
		String sErrorMessage = m_sErrorMessage;
		switch(sErrorCode){
			case "":
			case "0":	// OK (Request is performed without error)
				sErrorMessage = "";
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
	
	// if bNextMonth == false <=> get Bonus Expire in this month
	// if bNextMonth == true  <=> get Bonus Expire in next month
	public BigDecimal getBonusExpire(HashMap<String, BigDecimal> oBonusRecords, boolean bNextMonth){
		for(Entry<String, BigDecimal> entry: oBonusRecords.entrySet()){
			DateTime oExpireDate = null;
			String sExpiryDate = entry.getKey();
			if (sExpiryDate.length() == 10) {
				String sTmp = sExpiryDate.substring(0, 4) + sExpiryDate.substring(5, 7) + sExpiryDate.substring(8, 10);
				DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
				try {
					oExpireDate = formatter.parseDateTime(sTmp);
				} catch (Exception e) {
					AppGlobal.stack2Log(e);
				}
			}
			
			if (oExpireDate == null)
				return BigDecimal.ZERO;

			// Get this month bonus
			if(!bNextMonth){
				if(oExpireDate.getYear() != Integer.parseInt(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString().substring(0, 4)))
					continue;
				if(oExpireDate.getMonthOfYear() == Integer.parseInt(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString().substring(5, 7)))
					return entry.getValue();
			}
			else{	// Get next month bonus
				if(Integer.parseInt(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString().substring(5, 7)) != 12){
					if(oExpireDate.getYear() != Integer.parseInt(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString().substring(0, 4)))
						continue;
					if(oExpireDate.getMonthOfYear() == Integer.parseInt(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString().substring(5, 7)) + 1)
						return entry.getValue();
				}
				else{
					if(oExpireDate.getYear() != Integer.parseInt(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString().substring(0, 4)) + 1)
						continue;
					if(oExpireDate.getMonthOfYear() == 1)
						return entry.getValue();
				}
			}
		}
		return BigDecimal.ZERO;
	}
	
	//iType == 1 <=> expiry date
	//iType == 2 <=> SVC valid through
	//iType == 3 <=> member valid through
	public String dateExtension(String sExpireDate, Boolean bAddValue, int iType){
		String cardType = "card_type" + iProfileIndex;
		String[] sSkipControls = null;
		
		if(bAddValue){
			if(iType == 1){
				if(sInterfaceConfig.optJSONObject(cardType).optJSONObject("params").has("add_value_expiry_skip_date"))
					sSkipControls = sInterfaceConfig.optJSONObject(cardType).optJSONObject("params").optJSONObject("add_value_expiry_skip_date").optString("value").split(",");
			}
			else if(iType == 2){
				if(sInterfaceConfig.optJSONObject(cardType).optJSONObject("params").has("add_svc_value_valid_thru_date"))
					sSkipControls = sInterfaceConfig.optJSONObject(cardType).optJSONObject("params").optJSONObject("add_svc_value_valid_thru_date").optString("value").split(",");
			}
			else if(iType == 3){
				if(sInterfaceConfig.optJSONObject(cardType).optJSONObject("params").has("add_member_value_valid_thru_date"))
					sSkipControls = sInterfaceConfig.optJSONObject(cardType).optJSONObject("params").optJSONObject("add_member_value_valid_thru_date").optString("value").split(",");
			}
		}
		else{
			if(iType == 1){
				if(sInterfaceConfig.optJSONObject(cardType).optJSONObject("params").has("issue_card_expire_skip_date"))
					sSkipControls = sInterfaceConfig.optJSONObject(cardType).optJSONObject("params").optJSONObject("issue_card_expire_skip_date").optString("value").split(",");
			}
			else if(iType == 2){
				if(sInterfaceConfig.optJSONObject(cardType).optJSONObject("params").has("issue_svc_card_valid_thru_date"))
					sSkipControls = sInterfaceConfig.optJSONObject(cardType).optJSONObject("params").optJSONObject("issue_svc_card_valid_thru_date").optString("value").split(",");
			}
			else if(iType == 3){
				if(sInterfaceConfig.optJSONObject(cardType).optJSONObject("params").has("issue_member_valid_thru_date"))
					sSkipControls = sInterfaceConfig.optJSONObject(cardType).optJSONObject("params").optJSONObject("issue_member_valid_thru_date").optString("value").split(",");
			}
		}
		if(sSkipControls == null)
			return "";
		
		if(Integer.valueOf(sSkipControls[0]) < 0)
			return "";
		
		if(sExpireDate.isEmpty())
			sExpireDate = AppGlobal.g_oFuncOutlet.get().getFormat1BusinessDayInString();
		
		DateTime oExpireDate = null;
		if (sExpireDate.length() == 10) {
			String sTmp = sExpireDate.substring(0, 4) + sExpireDate.substring(5, 7) + sExpireDate.substring(8, 10);
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
			try {
				oExpireDate = formatter.parseDateTime(sTmp);
			} catch (Exception e) {
				AppGlobal.stack2Log(e);
			}
		}
		if (oExpireDate == null)
			return "";
		
		oExpireDate = oExpireDate.plusYears(Integer.valueOf(sSkipControls[0]));
		oExpireDate = oExpireDate.plusMonths(Integer.valueOf(sSkipControls[1]));
		oExpireDate = oExpireDate.plusDays(Integer.valueOf(sSkipControls[2]));
		
		if(sExpireDate.compareTo(AppGlobal.g_oFuncOutlet.get().getFormat1BusinessDayInString()) < 0 )
			sExpireDate = AppGlobal.g_oFuncOutlet.get().getFormat1BusinessDayInString();
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		return formatter.print(oExpireDate);
	}
	
	//select card type
	public boolean selectCardType() {
		String sTmpCardTyeValue = "";
		int iStart,iLength,iEnd;
		if(sInterfaceConfig.optJSONObject("card_type_position_setup").optJSONObject("params").has("start_position") && sInterfaceConfig.optJSONObject("card_type_position_setup").optJSONObject("params").has("length")) {
			iStart = sInterfaceConfig.optJSONObject("card_type_position_setup").optJSONObject("params").optJSONObject("start_position").optInt("value");
			iLength = sInterfaceConfig.optJSONObject("card_type_position_setup").optJSONObject("params").optJSONObject("length").optInt("value");
			iStart -= 1;
			iEnd = iStart + iLength;
			if(iStart < 0 || cardNumber.length() < iEnd)
				return false;
			
			sTmpCardTyeValue = cardNumber.substring(iStart, iEnd);
			for(int i = 0; i < 10 ; i++) {
				String sCurrentProfile;
				sCurrentProfile = "card_type"+(i+1);
				if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("card_type_value")) {
					if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").optJSONObject("card_type_value").optString("value").equals(sTmpCardTyeValue)) {
						this.setProfileIndex(i+1);
						return true;
					}
				}
			}
		}
		else
			return false;
		return true;
	}
	
	// Add Value ExtraInfo handling
	public ArrayList<PosCheckExtraInfo> setExtraInfo(HashMap<String, String> oHashMap){
		ArrayList<PosCheckExtraInfo> oPosCheckExtraInfos = new ArrayList<PosCheckExtraInfo>();
		String sBy = "";
		String sSection = "";
		for(Entry<String, String> entry: oHashMap.entrySet()) {
			if(entry.getKey().equals("By"))
				sBy = entry.getValue();
			else if(entry.getKey().equals("Section"))
				sSection = entry.getValue();
			if(!sBy.equals("") && !sSection.equals(""))
				break;
		}
		
		// if no sBy or sSection value was found, return a empty ArrayList<PosCheckExtraInfo>
		if(sBy.equals("") || sSection.equals(""))
			return oPosCheckExtraInfos;
		
		for(Entry<String, String> entry: oHashMap.entrySet()) {
			if(entry.getKey().equals("By") || entry.getKey().equals("Section"))
				continue;
			
			PosCheckExtraInfo oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
			oPosCheckExtraInfo.setBy(sBy);
			oPosCheckExtraInfo.setSection(sSection);
			oPosCheckExtraInfo.setIndex(0);
			oPosCheckExtraInfo.setVariable(entry.getKey());
			oPosCheckExtraInfo.setValue(entry.getValue());
			
			oPosCheckExtraInfos.add(oPosCheckExtraInfo);
		}
		
		return oPosCheckExtraInfos;
	}
	
	// Add Value ExtraInfo handling
	public ArrayList<PosCheckExtraInfo> setAddValueExtraInfo(String sInterfaceId, String sCheckId, String sCardType, String sRemark, Boolean bAddValue, int iPaymentId, BigDecimal oIssueValue, String sCardLevel){
		ArrayList<PosCheckExtraInfo> oPosCheckExtraInfos = new ArrayList<PosCheckExtraInfo>();
		
		for(int i = 0 ; i < 17 ; i++){
			PosCheckExtraInfo oPosCheckExtraInfo = new PosCheckExtraInfo();
			oPosCheckExtraInfo.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
			oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_ITEM);
			oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_LOYALTY);
			oPosCheckExtraInfo.setIndex(0);
			String sValues = "", sVariable = "";
			switch(i){
				case 0:
					if(this.getCardNo().isEmpty())
						continue;
					sValues = this.getCardNo();
					sVariable = PosCheckExtraInfo.VARIABLE_SVC_CARD_NUMBER;
					break;
				case 1:
					if(this.getMemberNo().isEmpty())
						continue;
					sValues = this.getMemberNo();
					sVariable = PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER;
					break;
				case 2:
					sValues = dateExtension("", bAddValue, EXTENSION_SVC_VALID_THROUGH);
					sVariable = PosCheckExtraInfo.VARIABLE_SVC_VALID_THROUGH;
					break;
				case 3:
					if(this.getMemberValidThrough().equals("BONUS_CARD"))
						sValues = "BONUS_CARD";
					else
						sValues = dateExtension("", bAddValue, EXTENSION_MEMBER_VALID_THROUGH);
					sVariable = PosCheckExtraInfo.VARIABLE_MEMBER_VALID_THROUGH;
					break;
				case 4:
					DateTime today = AppGlobal.getCurrentTime(false);
					DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
					sValues = fmt.print(today);
					if(sCardType.equals(CARD_TYPE_LOYALTY))
						sVariable = PosCheckExtraInfo.VARIABLE_ISSUE_DATE;
					else if(sCardType.equals(CARD_TYPE_SVC))
						sVariable = PosCheckExtraInfo.VARIABLE_CARD_ISSUE_DATE;
					break;
				case 5:
					sValues = AppGlobal.g_oFuncUser.get().getLoginId();
					sVariable = PosCheckExtraInfo.VARIABLE_CARD_ISSUE_EMP_ID;
					break;
				case 6:
					if(this.getBuyerName().isEmpty())
						continue;
					sValues = this.getBuyerName();
					sVariable = PosCheckExtraInfo.VARIABLE_CARD_BUYER_NAME;
					break;
				case 7:
					if(this.getBuyerPassportId().isEmpty())
						continue;
					sValues = this.getBuyerPassportId();
					sVariable = PosCheckExtraInfo.VARIABLE_CARD_BUYER_PASSPORT_ID;
					break;
				case 8:
					if(this.getBuyerContactNumber().isEmpty())
						continue;
					sValues = this.getBuyerContactNumber();
					sVariable = PosCheckExtraInfo.VARIABLE_CARD_BUYER_CONTACT_NUMBER;
					break;
				case 9:
					if(!sCardLevel.isEmpty())
						sValues = sCardLevel;
					else
					if(!this.getType().isEmpty())
						sValues = this.getType();
					
					sVariable = PosCheckExtraInfo.VARIABLE_MEMBER_TYPE;
					break;
				case 10:
					if(iPaymentId < 0)
						continue;
					JSONObject tempJSONObject = new JSONObject();
					try {
						tempJSONObject.put("amount", oIssueValue.toString());
						tempJSONObject.put("paym_id", iPaymentId);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					sValues = tempJSONObject.toString();
					oPosCheckExtraInfo.setSection(PosCheckExtraInfo.SECTION_DEFAULT_PAYMENT);
					sVariable = PosCheckExtraInfo.VARIABLE_DEFAULT_PAYMENT_DETAIL;
					break;
				case 11:
					if(this.getCardSeller().isEmpty())
						continue;
					sValues = this.getCardSeller();
					sVariable = PosCheckExtraInfo.VARIABLE_CARD_SELLER;
					break;
				case 12:
					if(this.getCardHistory().isEmpty())
						continue;
					sValues = this.getCardHistory();
					sVariable = PosCheckExtraInfo.VARIABLE_CARD_HISTORY;
					break;
				case 13:
					sValues = sInterfaceId;
					sVariable = PosCheckExtraInfo.VARIABLE_INTERFACE_ID;
					break;
				case 14:
					sValues = dateExtension("", bAddValue, EXTENSION_EXPIRY_DATE );
					sVariable = PosCheckExtraInfo.VARIABLE_CREDIT_DATE;
					break;
				case 15:
					sValues = sRemark;
					sVariable = PosCheckExtraInfo.VARIABLE_REMARK;
					break;
				case 16:
					if (this.assocMemberNo == null)
						continue;
					sValues = this.assocMemberNo;
					sVariable = PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER;
					break;
				default:
					break;
			}
			oPosCheckExtraInfo.setVariable(sVariable);
			oPosCheckExtraInfo.setValue(sValues);
			
			oPosCheckExtraInfos.add(oPosCheckExtraInfo);
		}
		
		return oPosCheckExtraInfos;
	}
	
	
	//read data from response JSON
	public void readDataFromJson(JSONObject memberJSONObject) throws JSONException {
		JSONObject tempJSONObject = null, tempCheckBalanceJSONObject = null, tempSearchJSONObject = null;
		JSONArray tempJSONArray = null;
		
		if(memberJSONObject.has("checkBalance")){
			tempCheckBalanceJSONObject = memberJSONObject.getJSONObject("checkBalance");
			if(tempCheckBalanceJSONObject.has("bonusBalance") && !tempCheckBalanceJSONObject.optString("bonusBalance").isEmpty())
				this.oSVCBalance = new BigDecimal(tempCheckBalanceJSONObject.optString("bonusBalance", "0.00")).divide(new BigDecimal("100.00").setScale(2, BigDecimal.ROUND_HALF_UP));
			else{
				this.oSVCBalance = new BigDecimal("0.00");
			}
		}
		
		if(memberJSONObject.has("searchSvc")){
			tempSearchJSONObject = memberJSONObject.getJSONObject("searchSvc");
			if(tempSearchJSONObject.has("memb_number"))
				this.cardNumber = tempSearchJSONObject.optString("memb_number");
			
			if(tempSearchJSONObject.has("bonusRecords")){
				tempJSONArray = tempSearchJSONObject.getJSONArray("bonusRecords");
				for (int i = 0; i < tempJSONArray.length(); i++) {
					tempJSONObject = tempJSONArray.optJSONObject(i);
					this.balanceRecords.put(tempJSONObject.optString("expiryMonth"), BigDecimal.valueOf(tempJSONObject.optDouble("bonus")).divide(new BigDecimal("100.00")).setScale(2, BigDecimal.ROUND_HALF_UP));
				}
			}
			if(tempSearchJSONObject.has("card_status")){
				if(tempSearchJSONObject.optString("card_status").equals("A"))
					this.status = STATUS_ACTIVE;
				else if(tempSearchJSONObject.optString("card_status").equals("R"))
					this.status = STATUS_READY_FOR_SALE;
				else if(tempSearchJSONObject.optString("card_status").equals("S"))
					this.status = STATUS_SUSPENDED;
				else if(tempSearchJSONObject.optString("card_status").equals("T"))
					this.status = STATUS_TRANSFERRED;
				else if(tempSearchJSONObject.optString("card_status").equals("I"))
					this.status = STATUS_IN_STOCK;
			}
			if(tempSearchJSONObject.has("memb_valid_through"))
				this.memberValidThrough = tempSearchJSONObject.optString("memb_valid_through");
			if(tempSearchJSONObject.has("card_seller"))
				this.cardSeller = tempSearchJSONObject.optString("card_seller");
			if(tempSearchJSONObject.has("buyer_name"))
				this.cardBuyerName = tempSearchJSONObject.optString("buyer_name");
			if(tempSearchJSONObject.has("buyer_passport_id"))
				this.cardBuyerPassportId = tempSearchJSONObject.optString("buyer_passport_id");
			if(tempSearchJSONObject.has("buyer_contact_name"))
				this.cardBuyerContactNumber = tempSearchJSONObject.optString("buyer_contact_name");
		}
		
		if(memberJSONObject.has("searchLoyalty")){
			tempSearchJSONObject = memberJSONObject.getJSONObject("searchLoyalty");
			if(tempSearchJSONObject.has("member_type"))
				this.type = tempSearchJSONObject.optString("member_type");
			if(tempSearchJSONObject.has("memb_number"))
				this.number = tempSearchJSONObject.optString("memb_number");
			if(tempSearchJSONObject.has("memb_last_name_l1"))
				this.lastName = tempSearchJSONObject.optString("memb_last_name_l1");
			if(tempSearchJSONObject.has("memb_first_name_l1"))
				this.firstName = tempSearchJSONObject.optString("memb_first_name_l1");
			if(tempSearchJSONObject.has("balance") && !tempSearchJSONObject.optString("balance").isEmpty())
				this.oBalance = new BigDecimal(tempSearchJSONObject.optString("balance", "0"));
			else{
				this.oBalance = BigDecimal.ZERO;
			}
			
			if(tempSearchJSONObject.has("bonusRecords")){
				tempJSONArray = tempSearchJSONObject.getJSONArray("bonusRecords");
				for (int i = 0; i < tempJSONArray.length(); i++) {
					tempJSONObject = tempJSONArray.optJSONObject(i);
					this.bonusRecords.put(tempJSONObject.optString("expiryMonth"), BigDecimal.valueOf(tempJSONObject.optDouble("bonus")));
				}
			}
			if(tempSearchJSONObject.has("card_history"))
				this.cardHistory = tempSearchJSONObject.optString("card_history");
			if(tempSearchJSONObject.has("totalSpending"))
				this.cardHistory = tempSearchJSONObject.optString("totalSpending");
			if(tempSearchJSONObject.has("spendingVariable")){
				tempJSONArray = tempSearchJSONObject.getJSONArray("spendingVariable");
				for (int i = 0; i < tempJSONArray.length(); i++) {
					tempJSONObject = tempJSONArray.optJSONObject(i);
					String sFromTo = readMemeberType(tempJSONObject.optString("from")) +" "+ AppGlobal.g_oLang.get()._("to", "") + " " + readMemeberType(tempJSONObject.optString("to"));
					this.spendingVariable.put(tempJSONObject.optString("updateDT"), sFromTo);
				}
			}
		}
		
		if(memberJSONObject.has("pointsAdded"))
			this.oPointEarn = new BigDecimal(memberJSONObject.optString("pointsAdded", "0.00"));
		if(memberJSONObject.has("pointsBalance"))
			this.oSVCBalance = new BigDecimal(memberJSONObject.optString("pointsBalance", "0.00"));
		
		//cert balance
		if(memberJSONObject.has("certificateBalance"))
			this.oBalance = new BigDecimal(memberJSONObject.optString("certificateBalance"));
		
		//transaction reference
		if(memberJSONObject.has("transactionReference"))
			this.sTransReference = memberJSONObject.optString("transactionReference");
		
	}
	
	//read member type
	public String readMemeberType(String sMemberType){
		String[] sSkipControls = null;
		
		for(int i = 1 ; i <= 10 ; i ++){
			String cardType = "card_level_" + i;
			if(!sInterfaceConfig.optJSONObject("card_level_setup").optJSONObject("params").optJSONObject(cardType).optString("value").isEmpty()){
				sSkipControls = sInterfaceConfig.optJSONObject("card_level_setup").optJSONObject("params").optJSONObject(cardType).optString("value").split(",");
				if(sSkipControls[0].equals(sMemberType))
					return sSkipControls[Integer.valueOf(sInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").optJSONObject("lang_index").optString("value"))];
			}
		}
		return sMemberType;
	}
	
	//login svc
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
	
	// get gourmate loyalty member
	public JSONObject svcSearchCardNumber(String sMemberNo, String sPassword, String sInterfaceId, String sSvcInterfaceId, FuncCheck oFuncCheck, int iMode){
		JSONObject requestJSONObject = new JSONObject();
		JSONObject oMemberDetailJSONObject = null;
		MenuItemDeptList oMenuItemDeptList = new MenuItemDeptList();
		oMenuItemDeptList.readItemDeptList();
		MenuItemCategoryList oItemCategoryList = new MenuItemCategoryList();
		oItemCategoryList.readItemCategoryList();
		
		try {
			requestJSONObject.put("interfaceId", sInterfaceId);
			requestJSONObject.put("sessionId", AppGlobal.g_oFuncStation.get().getLoyaltySessionId());
			if(!sMemberNo.isEmpty())
				requestJSONObject.put("memberNumber", sMemberNo);
			else{
				requestJSONObject.put("memberNumber", oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER));
				requestJSONObject.put("referenceId", oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_LOYALTY, PosCheckExtraInfo.VARIABLE_TRACE_ID));
				requestJSONObject.put("mode", iMode);
			}
			requestJSONObject.put("svcInterfaceId", sSvcInterfaceId);
			requestJSONObject.put("svcSessionId", AppGlobal.g_oFuncStation.get().getLoyaltySvcSessionId());
			requestJSONObject.put("svcCardNumber", sMemberNo);
				
			requestJSONObject.put("outletId", AppGlobal.g_oFuncOutlet.get().getOutletId());
			requestJSONObject.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
			requestJSONObject.put("password", sPassword);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "loyalSvcSearchCard", requestJSONObject.toString(), true)) {
			m_sErrorMessage = AppGlobal.g_oLang.get()._("cannot_connect_to_loyalty_svc_server") + System.lineSeparator()
					+ AppGlobal.g_oLang.get()._("fail_to_perform_svc_bonus_transaction");
		} else {
			if (OmWsClientGlobal.g_oWsClient.get().getResponse() == null){
				m_sErrorMessage = AppGlobal.g_oLang.get()._("no_response");
				return null;
			}
			
			JSONObject oTempJSONObject = OmWsClientGlobal.g_oWsClientForHq.get().getResponse();
			if (oTempJSONObject.has("checkBalance")) {
				JSONObject tempJSONObject = new JSONObject(); 
				try {
					tempJSONObject = oTempJSONObject.getJSONObject("checkBalance");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				if (!tempJSONObject.has("loyaltyResult")) {
					OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
					this.init();
					if(oTempJSONObject.has("errorCode"))
						m_sErrorMessage = oTempJSONObject.optString("errorCode");
					else
						m_sErrorMessage = AppGlobal.g_oLang.get()._("invalid_response");
					return null;
				}
			}
			oMemberDetailJSONObject = oTempJSONObject;
		}
		
		return oMemberDetailJSONObject;
	}
		
	//Add value or issue card
	public Boolean addValueOrIssueCard(HashMap<String, String> oAddValueInfo){
		PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
		JSONObject oResponseJSON = new JSONObject();
		
		oResponseJSON = oPosInterfaceConfig.doLoyaltySvcAddValueOrIssueCard(oAddValueInfo);
		if (oResponseJSON == null) {
			if (oPosInterfaceConfig.getLastErrorCode() != 0)
				m_sErrorMessage = oPosInterfaceConfig.getLastErrorMessage();
			return false;
		} else{
			try {
				readDataFromJson(oResponseJSON);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	
	// suspend svc card function
	public JSONObject svcSuspendCard(String sCardNo, String sSvcInterfaceId){
		JSONObject requestJSONObject = new JSONObject();
		JSONObject responseJSONObject = null;
		
		try {
			requestJSONObject.put("svcInterfaceId", sSvcInterfaceId);
			requestJSONObject.put("outletId", AppGlobal.g_oFuncOutlet.get().getOutletId());
			requestJSONObject.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
			requestJSONObject.put("svcSessionId", AppGlobal.g_oFuncStation.get().getLoyaltySvcSessionId());
			requestJSONObject.put("svcCardNumber", sCardNo);
			requestJSONObject.put("cardStatus", "S");
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return responseJSONObject;
	}
	
	// loyalty SVC Transfer Card
	public boolean transferCard(FuncLoyaltySvc oAssignedLoyaltySvc, FuncLoyaltySvc oLoyaltySvc, String sInterfaceId, String sSvcInterfaceId, String sReason){
		HashMap<String, String> oTransferCardInfo = new HashMap<String, String>();
		oTransferCardInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oTransferCardInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oTransferCardInfo.put("interfaceId", sInterfaceId);
		oTransferCardInfo.put("svcInterfaceId", sSvcInterfaceId);
		oTransferCardInfo.put("sessionId", AppGlobal.g_oFuncStation.get().getLoyaltySessionId());
		oTransferCardInfo.put("svcSessionId", AppGlobal.g_oFuncStation.get().getLoyaltySvcSessionId());
		oTransferCardInfo.put("originalSvcNo", oAssignedLoyaltySvc.getCardNo());
		oTransferCardInfo.put("destSvcNo", oLoyaltySvc.getCardNo());
		
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		oTransferCardInfo.put("cardIssueDate", fmt.print(today));
		oTransferCardInfo.put("businessDate", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInStringWithFormat("yyyyMMdd"));

		oTransferCardInfo.put("cardIssueEmpID", AppGlobal.g_oFuncUser.get().getLoginId());
		if(!oAssignedLoyaltySvc.getCardSeller().isEmpty())
			oTransferCardInfo.put("cardSeller", oAssignedLoyaltySvc.getCardSeller());
		if(!oAssignedLoyaltySvc.getBuyerName().isEmpty())
			oTransferCardInfo.put("cardBuyerName", oAssignedLoyaltySvc.getBuyerName());
		if(!oAssignedLoyaltySvc.getBuyerPassportId().isEmpty())
			oTransferCardInfo.put("cardBuyerPassportID", oAssignedLoyaltySvc.getBuyerPassportId());
		if(!oAssignedLoyaltySvc.getBuyerContactNumber().isEmpty())
			oTransferCardInfo.put("cardBuyerContactNumber", oAssignedLoyaltySvc.getBuyerContactNumber());
		if(!oAssignedLoyaltySvc.getMemberValidThrough().isEmpty())
			oTransferCardInfo.put("memberValidThrough", oAssignedLoyaltySvc.getMemberValidThrough());
		if(!oAssignedLoyaltySvc.getMemberNo().isEmpty())
			oTransferCardInfo.put("assocMemberNo", oAssignedLoyaltySvc.getMemberNo());
		if(!oAssignedLoyaltySvc.getAssocLedgerAccount().isEmpty())
			oTransferCardInfo.put("assocLedgerAccount", oAssignedLoyaltySvc.getAssocLedgerAccount());
		if(!sReason.isEmpty())
			oTransferCardInfo.put("transferReason", sReason);
		oTransferCardInfo.put("cardTransferEmpID", AppGlobal.g_oFuncUser.get().getLoginId());
		
		oTransferCardInfo.put("bonusBalance", oAssignedLoyaltySvc.getSVCBalance().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		
		oAssignedLoyaltySvc.selectCardType();
		oTransferCardInfo.put("cardType", sCardTypeDesc[AppGlobal.g_oCurrentLangIndex.get()-1]);
		if(!oAssignedLoyaltySvc.getMemberNo().isEmpty())
			oTransferCardInfo.put("loyaltyPoint", oAssignedLoyaltySvc.getBalance().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
		else
			oTransferCardInfo.put("loyaltyPoint", "");
		oTransferCardInfo.put("printQueue", AppGlobal.g_oFuncStation.get().getStation().getCheckPrtqId()+"");
		oTransferCardInfo.put("languageIndex", AppGlobal.g_oCurrentLangIndex.get()+"");
		
		PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
		return oPosInterfaceConfig.doLoyaltySvcTransferCard(oTransferCardInfo, oAssignedLoyaltySvc.getBalanceRecords());
	}
	
	//Search Card
	public Boolean searchCard(HashMap<String, String> oSearchCardInfo){
		JSONObject requestJSONObject = new JSONObject();
		try{
			requestJSONObject.put("outletId", oSearchCardInfo.get("outletId"));
			requestJSONObject.put("outletCode", oSearchCardInfo.get("outletCode"));
			requestJSONObject.put("svcInterfaceId", oSearchCardInfo.get("svcInterfaceId"));
			requestJSONObject.put("svcSessionId", oSearchCardInfo.get("svcSessionId"));
			requestJSONObject.put("svcCardNumber", oSearchCardInfo.get("svcCardNumber"));
			requestJSONObject.put("memberNumber", oSearchCardInfo.get("memberNumber"));
			requestJSONObject.put("supportLedgerAutoTopUp", oSearchCardInfo.get("supportLedgerAutoTopUp"));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
		JSONObject oTempJSONObject = oPosInterfaceConfig.doLoyaltySvcSearchCard(requestJSONObject);
		if (oTempJSONObject == null || oPosInterfaceConfig.getLastErrorCode() != 0){
			m_sErrorMessage = String.valueOf(oPosInterfaceConfig.getLastErrorCode());
			return false;
		} else {
			this.checkBalanceBonusBalance = new BigDecimal("0.0");
			if (oTempJSONObject.has("searchSvc") && !oTempJSONObject.optString("searchSvc").isEmpty()){
				JSONObject oSearchSvcJSONObject = oTempJSONObject.optJSONObject("searchSvc");
				
				if (oSearchSvcJSONObject.has("memb_number"))
					this.cardNumber = oSearchSvcJSONObject.optString("memb_number");
				
				if (oSearchSvcJSONObject.has("card_status")){
					String sCardStatus = oSearchSvcJSONObject.optString("card_status");
					switch(sCardStatus){
					case "R":
						m_sErrorMessage = AppGlobal.g_oLang.get()._("card_is_not_activated");
						return false;
					case "S":
						m_sErrorMessage = AppGlobal.g_oLang.get()._("card_is_suspended");
						return false;
					case "T":
						m_sErrorMessage = AppGlobal.g_oLang.get()._("card_is_transferred");
						return false;
					case "I":
						m_sErrorMessage = AppGlobal.g_oLang.get()._("card_is_in_stock");
						return false;
					case "A":
						String sMembValidThrough = oSearchSvcJSONObject.optString("memb_valid_through");
						DateTime oMembValidThrough = null;
						if (sMembValidThrough.length() == 10) {
							String sTmp = sMembValidThrough.substring(0, 4) + sMembValidThrough.substring(5, 7) + sMembValidThrough.substring(8, 10);
							DateTimeFormatter oDateFormat = DateTimeFormat.forPattern("yyyyMMdd");
							try {
								oMembValidThrough = oDateFormat.parseDateTime(sTmp);
							} catch (Exception e) {
								AppGlobal.stack2Log(e);
							}
						}
						if (oMembValidThrough == null){
							// No checking if no member valid through is returned
							//m_sErrorMessage = AppGlobal.g_oLang.get()._("missing_card_expiry_date") ;
							//return false;
						}else if (AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDate().compareTo(oMembValidThrough) > 0){
							m_sErrorMessage = AppGlobal.g_oLang.get()._("card_was_expired_at") + " : " + sMembValidThrough;
							return false;
						}
						break;
					default:
						m_sErrorMessage = AppGlobal.g_oLang.get()._("missing_card_status");
						return false;
					}
				}
				
				if (oSearchSvcJSONObject.has("associate_ledger_account"))
					this.assocLedgerAccount = oSearchSvcJSONObject.optString("associate_ledger_account");
			}
			
			// Search Loyalty SVC information
			if (oTempJSONObject.has("checkBalance") && !oTempJSONObject.optString("checkBalance").isEmpty()){
				JSONObject oCheckBalanceJSONObject = oTempJSONObject.optJSONObject("checkBalance");
				if (oCheckBalanceJSONObject.has("bonusBalance") && !oCheckBalanceJSONObject.optString("bonusBalance").isEmpty()){
					BigDecimal dCurrentBalance = new BigDecimal(oCheckBalanceJSONObject.optString("bonusBalance", "0.0"));
					this.checkBalanceBonusBalance = dCurrentBalance.divide(new BigDecimal("100"));
				}
			}
			
			// Search Loyalty member information to find the member who's card number is the target card number
			this.oBalance = BigDecimal.ZERO;
			if(oTempJSONObject.has("searchLedger") && !oTempJSONObject.optString("searchLedger").isEmpty()){
				JSONObject oLedgerAccountJSONObject = oTempJSONObject.optJSONObject("searchLedger");
				if (oLedgerAccountJSONObject.has("memb_number") && !oLedgerAccountJSONObject.optString("memb_number").isEmpty())
					this.assocLedgerAccount = oLedgerAccountJSONObject.optString("memb_number");
				if (oLedgerAccountJSONObject.has("balance") && !oLedgerAccountJSONObject.optString("balance").isEmpty()){
					BigDecimal dCurrentBalance = new BigDecimal(oLedgerAccountJSONObject.optString("balance", "0.0"));
					this.oBalance = dCurrentBalance.divide(new BigDecimal("100"));
				}
			}
			return true;
		}
	}
	
	//SVC Payment
	public Boolean svcPayment(HashMap<String, String> oSvcPaymentInfo){
		PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
		if (!oPosInterfaceConfig.doLoyaltySvcPayment(oSvcPaymentInfo)) {
			if (oPosInterfaceConfig.getLastErrorCode() != 0)
				m_sErrorMessage = String.valueOf(oPosInterfaceConfig.getLastErrorCode());
			return false;
		}
		
		this.checkBalanceBonusBalance = BigDecimal.ZERO;
		if (oPosInterfaceConfig.getLastSuccessResult().has("svcBonusTransaction") && !oPosInterfaceConfig.getLastSuccessResult().optString("svcBonusTransaction").isEmpty()){
			JSONObject oSvcBonusTransaction = oPosInterfaceConfig.getLastSuccessResult().optJSONObject("svcBonusTransaction");
			BigDecimal dCurrentBalance = new BigDecimal(oSvcBonusTransaction.optString("BonusBalance", "0.0"));
			this.checkBalanceBonusBalance = dCurrentBalance.divide(new BigDecimal("100"));
			
			if (oSvcBonusTransaction.has("$autoTopUpInformation") && !oSvcBonusTransaction.optString("$autoTopUpInformation").isEmpty())
				this.sAutoTopUpInformation = oSvcBonusTransaction.optString("$autoTopUpInformation");
			
		}
		
		return true;
	}
	
	// reverse the transaction of auto top-up of loyalty card and loyalty ledger account for release payment
	public Boolean reverseAutoTopUpAndDeduction(HashMap<String, String> oPostingInfo){
		PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
		JSONObject oResponseJSON = new JSONObject();
		
		oResponseJSON = oPosInterfaceConfig.reverseLoyaltySvcAutoTopUpAndDeduction(oPostingInfo);
		if (oResponseJSON == null) {
			if (!oPosInterfaceConfig.getLastErrorMessage().isEmpty())
				m_sErrorMessage = oPosInterfaceConfig.getLastErrorMessage();
			else if(oPosInterfaceConfig.getLastErrorCode() != 0){
				m_sErrorCode = Integer.toString(oPosInterfaceConfig.getLastErrorCode());
				m_sErrorMessage = this.getLastErrorMessage();
			}
			return false;
		} else{
			try {
				readDataFromJson(oResponseJSON);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	//init object
	public void init() {
		this.number = "";
		this.cardNumber = "";
		this.traceId = "";
		this.name = "";
		this.displayName = "";
		this.lastName = "";
		this.firstName = "";
		this.type = "";
		this.status = "";
		this.sLoyaltyExpiryDate = "";
		this.totalSpending = "0";
		this.spendingVariable = new HashMap<String, String>();
		this.memberValidThrough = "";
		this.cardHistory = "";
		this.cardBuyerName = "";
		this.cardBuyerPassportId = "";
		this.cardBuyerContactNumber = "";
		this.cardSeller = "";
		this.assocLedgerAccount = "";
		this.bIsAllowAddValue = true;
		this.bIsAllowIssueBonusCard = false;
		this.iProfileIndex = 0;
		this.sItemCode = "";
		this.iLangIndex = 1;
		this.sAddValueItemCode = "";
		this.oMaxAllowedAmount = BigDecimal.ZERO;
		this.oPointEarn = BigDecimal.ZERO;
		this.bIsAllowIssueAsBonusCard = true;
		this.sIssueValueDesc = new String[10][5];
		this.sChildIssueValueDesc = new String[10][5];
		this.oIssueValue = new BigDecimal[10];
		
		this.sAddValueDesc = new String[10][5];
		this.oAddValue = new BigDecimal[10];
		this.sAddValueDefaultPayment = new String[10];
		
		this.oChildIssueValue = new BigDecimal[10];
		this.sDefaultCardLevel = new String[10];
		this.sDefaultPaymentCode = new String[10];
		this.sChildDefaultCardLevel = new String[10];
		this.sChildDefaultPaymentCode = new String[10];
		this.sAllowPaymentCode = new String[10];
		for (int j = 0; j < 10; j++) {
			for(int k = 0; k < 5; k++) {
				this.sIssueValueDesc[j][k] = null;
				this.sChildIssueValueDesc[j][k] = null;
				this.sAddValueDesc[j][k] = null;
			}
		}
				
		for (int j = 0; j < 10; j++) {
			this.oIssueValue[j] = null;
			this.oAddValue[j] = null;
			this.sDefaultCardLevel[j] = null;
			this.sDefaultPaymentCode[j] = null;
			this.oChildIssueValue[j] = null;
			this.sChildDefaultCardLevel[j] = null;
			this.sChildDefaultPaymentCode[j] = null;
			this.sAllowPaymentCode[j] = null;
			this.sAddValueDefaultPayment[j] = null;
		}
		this.sInterfaceConfig = new JSONObject();
		setInterfaceConfig(InfVendor.KEY_GM_LOYALTY_SVC);
		this.oBalance = BigDecimal.ZERO;
		this.oSVCBalance = BigDecimal.ZERO;
		this.bonusRecords = new HashMap<String, BigDecimal>();
		this.balanceRecords = new HashMap<String, BigDecimal>();
		this.sCardTypeDesc = new String[5];
		for(int j = 0 ; j < 5 ; j++)
			this.sCardTypeDesc[j] = null;
		
		this.sTransReference = "";
		
		this.sCardTypeAssociateMemberControl = "";
		
		m_sErrorMessage = "";

		m_sErrorCode = "";

	}
	
	//get profile index
	public int getProfileIndex() {
		return iProfileIndex;
	}
	
	//get member no.
	public String getMemberNo() {
		return this.number;
	}

	//get card no.
	public String getCardNo(){
		return this.cardNumber;
	}
	
	//get trace id
	public String getTraceId(){
		return this.traceId;
	}
	
	//get display name
	public String getDisplayName() {
		return this.displayName;
	}
	
	//get SVC member name
	public String getName() {
		return this.name;
	}
	
	//get member last name
	public String getLastName(){
		return this.lastName;
	}
	
	//get member first name
	public String getFirstName(){
		return this.firstName;
	}
	
	//get the SVC memb type
	public String getType(){
		return this.type;
	}
	
	//get the SVC card status
	public String getStatus(){
		return this.status;
	}
	
	//get account balance
	public BigDecimal getBalance(){
		return this.oBalance;
	}
	
	//get loyalty account balance
	public BigDecimal getSVCBalance(){
		return this.oSVCBalance;
	}
	
	//get the user balance expire
	public HashMap<String, BigDecimal> getBalanceRecords(){
		return this.balanceRecords;
	}
	
	//get the user bouns expire
	public HashMap<String, BigDecimal> getBounsRecords(){
		return this.bonusRecords;
	}
	
	//get bonus balance
	public BigDecimal getcheckBalanceBonusBalance(){
		return this.checkBalanceBonusBalance;
	}
	
	//get the loyalty account memberValidThrough
	public String getMemberValidThrough(){
		return this.memberValidThrough;
	}
	
	//get the svc buyer name
	public String getBuyerName(){
		return this.cardBuyerName;
	}
	
	//get the svc buyer Passport Id
	public String getBuyerPassportId(){
		return this.cardBuyerPassportId;
	}
	
	//get the svc buyer Contact Number
	public String getBuyerContactNumber(){
		return this.cardBuyerContactNumber;
	}
	
	//get loyalty Card History
	public String getCardHistory(){
		return this.cardHistory;
	}
	
	public void setCardBuyerName(String cardBuyerName) {
		this.cardBuyerName = cardBuyerName;
	}

	public void setCardBuyerPassportId(String cardBuyerPassportId) {
		this.cardBuyerPassportId = cardBuyerPassportId;
	}

	public void setCardBuyerContactNumber(String cardBuyerContactNumber) {
		this.cardBuyerContactNumber = cardBuyerContactNumber;
	}
	
	public String getCardSeller() {
		return cardSeller;
	}
	
	public String getAssocMemberNo() {
		return assocMemberNo;
	}
	
	// get Associate Ledger account
	public String getAssocLedgerAccount(){
		return assocLedgerAccount;
	}
	
	public void setCardSeller(String cardSeller) {
		this.cardSeller = cardSeller;
	}
	
	public void setCardNumber(String sCardNumber){
		this.cardNumber = sCardNumber;
	}
	
	public void setAssociateMemberNo (String sAssociateMemberNo) {
		this.assocMemberNo = sAssociateMemberNo;
	}
	
	//get the loyalty account memberValidThrough
	public void setMemberValidThrough(String sMemberValidThrough){
		this.memberValidThrough = sMemberValidThrough;
	}
	
	public void setInterfaceConfig(String sKey){
		List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_LOYALTY_INTERFACE);
		PosInterfaceConfig oSelectInterfaceConfig = null;
		
		for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList) {
			if (oPosInterfaceConfig.getInterfaceVendorKey().equals(sKey)) {
				oSelectInterfaceConfig = oPosInterfaceConfig;
				break;
			}
		}
		if(oSelectInterfaceConfig == null)
			return;
		sInterfaceConfig = oSelectInterfaceConfig.getInterfaceConfig();
	}
	
	public JSONObject getInterfaceConfig() {
		return sInterfaceConfig;
	}
	
	// get the auto top up information
	public String getAutoTopUpInformation(){
		return sAutoTopUpInformation;
	}
	
	public void setProfileIndex(int iProfileIndex) {
		this.iProfileIndex = iProfileIndex;
		String sCurrentProfile;
		String[] sResultList;
		
		sCurrentProfile = "card_type"+(iProfileIndex);
		if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("card_type_value")) {
			if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("allow_add_value")) {
				if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").optJSONObject("allow_add_value").optInt("value") == 0)
					this.bIsAllowAddValue = false;
				else
					this.bIsAllowAddValue = true;
			}
		}
		
		if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("card_type_value")) {
			if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("allow_issue_bonus_card")) {
				if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").optJSONObject("allow_issue_bonus_card").optInt("value") == 0)
					this.bIsAllowIssueBonusCard = false;
				else
					this.bIsAllowIssueBonusCard = true;
			}
		}
		
		if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("card_type_value")) {
			if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("allow_issue")) {
				if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").optJSONObject("allow_issue").optInt("value") == 0)
					this.bIsAllowIssueAsBonusCard = false;
				else
					this.bIsAllowIssueAsBonusCard = true;
			}
		}
		
		for(int i = 1 ; i <= 10 ; i++) {
			String addValue = "add_value_" + i ;
			if(sInterfaceConfig.optJSONObject("add_value_setup").optJSONObject("params").has(addValue)){
				String[] sListofAddValue = sInterfaceConfig.optJSONObject("add_value_setup").optJSONObject("params").optJSONObject(addValue).optString("value").split(",");
				if(sListofAddValue.length >= 6) {
					for(int j = 0; j < 5; j++)
						this.sAddValueDesc[i-1][j] = sListofAddValue[j+1];
					this.oAddValue[i-1] = new BigDecimal(sListofAddValue[0]);
					if(sListofAddValue.length == 7)
						this.sAddValueDefaultPayment[i-1] = sListofAddValue[6];
				}
			}
		}
		
		if(sInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").has("lang_index"))
			this.iLangIndex = sInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").optJSONObject("lang_index").optInt("value");
		
		if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("issue_card_item_code"))
			this.sItemCode = sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").optJSONObject("issue_card_item_code").optString("value");
		if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("add_value_item_code"))
			this.sAddValueItemCode = sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").optJSONObject("add_value_item_code").optString("value");
		if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("max_amount"))
			this.oMaxAllowedAmount = new BigDecimal(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").optJSONObject("max_amount").optString("value"));

		if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("issue_value_information")) {
			String sIssueValueInfo = sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").optJSONObject("issue_value_information").optString("value");
			if(!sIssueValueInfo.equals("")) {
				String[] sListofChoice = sIssueValueInfo.split("[\\r\\n]+");
				for(int i = 0; i < sListofChoice.length; i++) {
					sResultList = sListofChoice[i].split(",");
					if(sResultList.length == 8 || sResultList.length == 7) {
						for(int j = 0; j < 5; j++)
							this.sIssueValueDesc[i][j] = sResultList[j];
						this.oIssueValue[i] = new BigDecimal(sResultList[5]);
						this.sDefaultCardLevel[i] = sResultList[6];
						if(sResultList.length == 8)
							this.sDefaultPaymentCode[i] = sResultList[7];
					}
				}
			}
		}
		
		if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("child_issue_value_information")) {
			String sIssueValueInfo = sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").optJSONObject("child_issue_value_information").optString("value");
			if(!sIssueValueInfo.equals("")) {
				String[] sListofChoice = sIssueValueInfo.split("\r\n");
				for(int i = 0; i < sListofChoice.length; i++) {
					sResultList = sListofChoice[i].split(",");
					if(sResultList.length == 8  || sResultList.length == 7) {
						for(int j = 0; j < 5; j++)
							this.sChildIssueValueDesc[i][j] = sResultList[j];
						this.oChildIssueValue[i] = new BigDecimal(sResultList[5]);
						this.sChildDefaultCardLevel[i] = sResultList[6];
						if(sResultList.length == 8)
							this.sChildDefaultPaymentCode[i] = sResultList[7];
					}
				}
			}
		}
		
		if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("payment_method_limit")) {
			String sPaymentAllowed = sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").optJSONObject("payment_method_limit").optString("value");
			if(!sPaymentAllowed.equals(""))
				oAllowedPaymentCode = sPaymentAllowed.split(",");
		}
		
		if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("desc_lang")) {
			String sIssueValueInfo = sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").optJSONObject("desc_lang").optString("value");
			if(!sIssueValueInfo.equals("")) {
				if(sIssueValueInfo.split(",").length <= 5)
					this.sCardTypeDesc = sIssueValueInfo.split(",");
			}
		}
		
		if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("allow_payment_method")) {
			String sIssueValueInfo = sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").optJSONObject("allow_payment_method").optString("value");
			if(!sIssueValueInfo.equals("")) {
				this.sAllowPaymentCode = sIssueValueInfo.split(",");
			}
		}
		if(sInterfaceConfig.has("connection_setup")){
			if(sInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").has("activate_item_code"))
				this.sItemCode = sInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").optJSONObject("activate_item_code").optString("value");
			
		}
		
		if(sInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").has("card_type_associate_member_mapping"))
			this.sCardTypeAssociateMemberControl = sInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").optJSONObject("card_type_associate_member_mapping").optString("value");
		
	}
	
	// Get the reminder alert type for start redeem
	// Return value:	-1 - no control
	//					0 - Alert prompt and allow override
	//					1 - Alert prompt and cannot continue
	public int outletAlertTypeForStartRedeemReminder(String sCardNo){
		String sTmpCardTyeValue = "";
		int iStart,iLength,iEnd;
		if(sInterfaceConfig.optJSONObject("card_type_position_setup").optJSONObject("params").has("start_position") && sInterfaceConfig.optJSONObject("card_type_position_setup").optJSONObject("params").has("length")) {
			iStart = sInterfaceConfig.optJSONObject("card_type_position_setup").optJSONObject("params").optJSONObject("start_position").optInt("value");
			iLength = sInterfaceConfig.optJSONObject("card_type_position_setup").optJSONObject("params").optJSONObject("length").optInt("value");
			iStart -= 1;
			iEnd = iStart + iLength;
			if(iStart < 0 || sCardNo.length() < iEnd)
				return -1;
			
			sTmpCardTyeValue = sCardNo.substring(iStart, iEnd);
			for(int i = 0; i < 10 ; i++) {
				String sCurrentProfile;
				sCurrentProfile = "card_type"+(i+1);
				if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").has("card_type_value")) {
					if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").optJSONObject("card_type_value").optString("value").equals(sTmpCardTyeValue)) {
						// Found matched card type
						
						// Check if need to show reminder or not
						if(sInterfaceConfig.optJSONObject(sCurrentProfile).optJSONObject("params").optJSONObject("check_start_redeem_at_svc_payment").optInt("value") == 1) {
							// Need to show reminder for this card type
							
							// Check if need to show reminder in this outlet
							if(sInterfaceConfig.has("general_setup") && sInterfaceConfig.optJSONObject("general_setup").has("params") && sInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").has("start_redeem_reminder")) {
								int iAlertType = -1;
								String[] sCheckOutletAlertTypes = sInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").optJSONObject("start_redeem_reminder").optString("value").split(",");
								
								if(sCheckOutletAlertTypes != null && sCheckOutletAlertTypes.length > 0){
									String[] sCheckAlertType = null;
									for(String sCheckOutletAlertType : sCheckOutletAlertTypes){
										if(!sCheckOutletAlertType.contains("-"))
											continue;
										
										sCheckAlertType = sCheckOutletAlertType.split("-");
										if(sCheckAlertType.length != 2  || sCheckAlertType[0].isEmpty() || sCheckAlertType[1].isEmpty())
											continue;
										
										if(sCheckAlertType[0].equals(AppGlobal.g_oFuncOutlet.get().getOutletCode())) {
											iAlertType = Integer.parseInt(sCheckAlertType[1]);
											break;
										}
									}
								}
								
								return iAlertType;
							}
						}
						
						break;
					}
				}
			}
		}
		
		return -1;
	}
	
	public boolean isIsAllowAddValue() {
		return bIsAllowAddValue;
	}

	public boolean isIsAllowIssueBonusCard() {
		return bIsAllowIssueBonusCard;
	}

	public String getItemCode() {
		return sItemCode;
	}
	
	public int getLangIndex(){
		return iLangIndex;
	}
	
	public String getAddValueItemCode() {
		return sAddValueItemCode;
	}

	public BigDecimal getMaxAllowedAmount() {
		return oMaxAllowedAmount;
	}
	
	public BigDecimal getPointEarn() {
		return oPointEarn;
	}
	
	public BigDecimal[] getIssueValue() {
		return oIssueValue;
	}
	
	public BigDecimal[] getAddValue(){
		return oAddValue;
	}

	public String[] getDefaultCardLevel() {
		return sDefaultCardLevel;
	}

	public String[] getDefaultPaymentCode() {
		return sDefaultPaymentCode;
	}

	public String[][] getIssueValueDesc() {
		return sIssueValueDesc;
	}
	
	public String[][] getAddValueDesc(){
		return sAddValueDesc;
	}

	public String[][] getChildIssueValueDesc() {
		return sChildIssueValueDesc;
	}
	
	public String[] getAddValueDefaultPayment(){
		return sAddValueDefaultPayment;
	}

	public String[] getChildDefaultCardLevel() {
		return sChildDefaultCardLevel;
	}

	public String[] getChildDefaultPaymentCode() {
		return sChildDefaultPaymentCode;
	}
	
	public String[] getAllowPaymentCode(){
		return sAllowPaymentCode;
	}
	public void setLoyaltyNumber(String number) {
		this.number = number;
	}
	
	public String getLoyaltyExpiryDate() {
		return sLoyaltyExpiryDate;
	}
	
	public void setLoyaltyExpiryDate(String sLoyaltyExpiryDate) {
		this.sLoyaltyExpiryDate = sLoyaltyExpiryDate;
	}
	
	public boolean isbIsAllowIssueAsBonusCard() {
		return bIsAllowIssueAsBonusCard;
	}
	
	public BigDecimal[] getChildIssueValue() {
		return oChildIssueValue;
	}
	
	public String getTotalSpending(){
		return this.totalSpending;
	}
	
	//get the spending variable
	public HashMap<String, String> getSpendingVariable(){
		return this.spendingVariable;
	}
	
	public String getRemainForUpgrade(){
		
		String sSkipControls[] = null;
		int iRemainForUpgrade = 0;
		
		for(int i = 1; i <= 10 ; i++){
			String sCardType = "card_level_" + i;
			sSkipControls = sInterfaceConfig.optJSONObject("card_level_setup").optJSONObject("params").optJSONObject(sCardType).optString("value").split(",");
			if(sSkipControls.length != 7)
				break;
			if(Integer.valueOf(sSkipControls[6]) > Integer.valueOf(totalSpending)){
				iRemainForUpgrade = Integer.valueOf(sSkipControls[6]) - Integer.valueOf(totalSpending);
				break;
			}
		}
		return ""+iRemainForUpgrade;
	}
	
	public String[] getAllowedPaymentCode() {
		return oAllowedPaymentCode;
	}
	
	// Get manual key-in control setting
	public static String[] getKeyInControls() {
		String sKeyInControls[] = null;
		List<PosInterfaceConfig> oLoyaltyInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		// Get the configure from interface module
		oLoyaltyInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_LOYALTY_INTERFACE);
		JSONObject oLoyaltyInterfaceConfig = null;
		for (PosInterfaceConfig oPosInterfaceConfig : oLoyaltyInterfaceConfigList) {
			if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GM_LOYALTY_SVC)) {
				oLoyaltyInterfaceConfig = oPosInterfaceConfig.getInterfaceConfig();
				if(oLoyaltyInterfaceConfig.has("general_setup") && oLoyaltyInterfaceConfig.optJSONObject("general_setup").has("params") && oLoyaltyInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").has("swipe_card_control"))
					sKeyInControls = oLoyaltyInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").optJSONObject("swipe_card_control").optString("value").split(",");
			}
		}
		return sKeyInControls;
	}
	
	//Get Card Type Associate Member Setting
	public String[] getCardTypeAssociateMemberMapping() {
		String sCardTypeAssociateMemberMapping[] = null;
		if(!sCardTypeAssociateMemberControl.isEmpty())
			sCardTypeAssociateMemberMapping = sCardTypeAssociateMemberControl.split(",");
		return sCardTypeAssociateMemberMapping;
	}
	
	//get Transaction Reference
	public String getTransReference(){
		return this.sTransReference;
	}
}
