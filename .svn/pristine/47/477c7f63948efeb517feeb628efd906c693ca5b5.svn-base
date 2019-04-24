//Database: pos_check_extra_infos - Extra information of the check
package om;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosCheckExtraInfo {
	private String ckeiId;
	private String by;
	private int oletId;
	private String chksId;
	private String cptyId;
	private String citmId;
	private String cdisId;
	private String cpayId;
	private String section;
	private String variable;
	private int index;
	private String value;
	private String status;
	
	// by
	public static final String BY_CHECK = "check";
	public static final String BY_DISCOUNT = "discount";
	public static final String BY_ITEM = "item";
	public static final String BY_PAYMENT = "payment";
	
	// section
	public static final String SECTION_AUTO_FUNCTIONS			= "auto_functions";
	public static final String SECTION_ADVANCE_ORDER			= "advance_order";
	public static final String SECTION_CONTINUOUS_PRINT			= "continuous_print";
	public static final String SECTION_CARRY_FORWARD			= "carry_forward";
	public static final String SECTION_CREDIT_CARD				= "credit_card";
	public static final String SECTION_DEFAULT_PAYMENT			= "default_payment";
	public static final String SECTION_DISCOUNT					= "discount";
	public static final String SECTION_GAMING_INTERFACE			= "gaming_interface";
	
	public static final String SECTION_ITEM						= "item";
	public static final String SECTION_ITEM_TYPE				= "item_type";
	public static final String SECTION_KITCHEN_MONITOR 			= "kitchen_monitor";
	public static final String SECTION_LOYALTY					= "loyalty";
	public static final String SECTION_LOYALTY_SVC				= "loyalty_svc";
	public static final String SECTION_MEMBERSHIP_INTERFACE		= "membership_interface";
	public static final String SECTION_ONLINE_COUPON			= "online_coupon";
	public static final String SECTION_PAYMENT_INTERFACE		= "payment_interface";
	public static final String SECTION_PMS						= "pms";
	public static final String SECTION_PREORDER					= "preorder";
	public static final String SECTION_SVC						= "svc";
	public static final String SECTION_TMS						= "tms";
	public static final String SECTION_TABLE_INFORMATION		= "table_information";
	public static final String SECTION_VOUCHER_INTERFACE		= "voucher_interface";
	public static final String SECTION_DISPLAY_INFORMATION      = "display_information";
	
	//variable
	public static final String VARIABLE_ACCOUNT_NAME			= "account_name";
	public static final String VARIABLE_ACCOUNT_NUMBER			= "account_number";
	public static final String VARIABLE_ADD_WAIVE_TAX_SC		= "add_waive_tax_sc";
	public static final String VARIABLE_ADVANCE_ORDER_CHKS_ID	= "advance_order_check_id";
	public static final String VARIABLE_ALLOW_CONTINUOUS_PRINT	= "allow_continuous_print";
	public static final String VARIABLE_AMOUNT_USE				= "amount_use";
	public static final String VARIABLE_AMOUNT_FOR_EARN_POINT	= "amount_for_earn_point";
	public static final String VARIABLE_APPLY_USER_ID		 	= "apply_user_id";
	public static final String VARIABLE_APPROVED_AMOUNT			= "approved_amount";
	public static final String VARIABLE_APPROVAL_CODE			= "approval_code";
	public static final String VARIABLE_APPROVAL_USER_NUM		= "approval_user_num";
	public static final String VARIABLE_AR_ACCOUNT_NUMBER 		= "ar_account_number";
	public static final String VARIABLE_ASSOC_MEMBER_NO			= "assoc_member_no";
	public static final String VARIABLE_AUTH_CODE				= "auth_code";
	public static final String VARIABLE_AUTO_TOP_UP_CARD_BALANCE= "auto_top_up_card_balance";
	public static final String VARIABLE_AUTO_TOP_UP_INFORMATION	= "auto_top_up_information";
	public static final String VARIABLE_AVAILABLE_VOUCHER_LIST	= "available_voucher_list";
	public static final String VARIABLE_AWARD_CODE				= "award_code";
	public static final String VARIABLE_BATCH_NUMBER			= "batch_number";
	public static final String VARIABLE_BENEFIT_AMOUNT			= "benefit_amount";
	public static final String VARIABLE_BENEFIT_CODE			= "benefit_code";
	public static final String VARIABLE_BENEFIT_QTY		 		= "benefit_qty";
	public static final String VARIABLE_BENEFIT_TYPE			= "benefit_type";
	public static final String VARIABLE_BONUS_BALANCE 			= "bonus_balance";
	public static final String VARIABLE_BONUS_CODE				= "bonus_code";
	public static final String VARIABLE_BONUS_REDEMPTION_CODE 	= "bonus_redemption_code";
	public static final String VARIABLE_CALL_NO					= "call_no";
	public static final String VARIABLE_CANCEL_AWARD_NUMBER     = "cancel_award_number";
	public static final String VARIABLE_CARD_SELLER				= "card_seller";
	public static final String VARIABLE_CARD_BUYER_CONTACT_NUMBER	= "card_buyer_contact_number";
	public static final String VARIABLE_CARD_BUYER_NAME			= "card_buyer_name";
	public static final String VARIABLE_CARD_BUYER_PASSPORT_ID 	= "card_buyer_passport_id";
	public static final String VARIABLE_CARD_HISTORY			= "card_history";
	public static final String VARIABLE_CARD_HOLDER_NAME		= "holder_name";
	public static final String VARIABLE_CARD_ISSUE_DATE			= "card_issue_dt";
	public static final String VARIABLE_CARD_ISSUE_EMP_ID		= "card_issue_emp_id";
	public static final String VARIABLE_CARD_LEVEL_NAME			= "card_level_name";
	public static final String VARIABLE_CARD_NO					= "card_no";
	public static final String VARIABLE_CARD_SN					= "card_sn";
	public static final String VARIABLE_CARD_STATUS 			= "card_status";
	public static final String VARIABLE_CARD_STORE_VALUE		= "card_store_value";
	public static final String VARIABLE_CARD_STORE_VALUE_USED	= "card_store_value_used";
	public static final String VARIABLE_CARD_TYPE_NAME			= "card_type_name";
	public static final String VARIABLE_CHECK_INFO				= "check_info";
	public static final String VARIABLE_COOKING_INFO			= "cooking_info";
	public static final String VARIABLE_COMP_STATUS				= "comp_status";
	public static final String VARIABLE_COUPON					= "coupon";
	public static final String VARIABLE_COUPON_ITEM				= "coupon_item";
	public static final String VARIABLE_CREDIT_DATE				= "credit_date";
	public static final String VARIABLE_DATA 					= "data";
	public static final String VARIABLE_DATE_OF_BIRTH 			= "date_of_birth";
	public static final String VARIABLE_DCC_OPT_OUT  			= "opt_out";
	public static final String VARIABLE_DEFAULT_PAYMENT_DETAIL	= "detail";
	public static final String VARIABLE_DEPOSIT_AMOUNT			= "deposit_amount";
	public static final String VARIABLE_DISABLE_MIX_AND_MATCH_RULES		= "disable_mix_and_match_rules";
	public static final String VARIABLE_DISCOUNT_RATE = "discount_rate";
	public static final String VARIABLE_DISPLAY_SEQUENCE		= "display_sequence";
	public static final String VARIABLE_DUMMY_PAYMENT			= "dummy_payment";
	public static final String VARIABLE_E_SIGNATURE				= "e_signature";
	public static final String VARIABLE_EMV_APPLICATION_CRYPT	= "emv_transaction_crypt";
	public static final String VARIABLE_EMV_APPLICATION_ID		= "emv_application_id";
	public static final String VARIABLE_EMV_APPLICATION_NAME	= "emv_application_name";
	public static final String VARIABLE_ENGLISH_NAME			= "english_name";
	public static final String VARIABLE_ENTRY_MODE				= "entry_mode";
	public static final String VARIABLE_EVENT_ORDER_DEPOSIT_BALANCE			= "event_order_deposit_balance";
	public static final String VARIABLE_EVENT_ORDER_DEPOSIT_FOR_ADD			= "event_order_deposit_for_add";
	public static final String VARIABLE_EVENT_ORDER_NUMBER_FOR_ADD			= "event_order_number_for_add";
	public static final String VARIABLE_EVENT_ORDER_DEPOSIT_FOR_USE			= "event_order_deposit_for_use";
	public static final String VARIABLE_EVENT_ORDER_NUMBER_FOR_USE			= "event_order_number_for_use";
	public static final String VARIABLE_EXCHANGE_RATE_TEXT		= "exchange_rate_format";
	public static final String VARIABLE_EXCHANGE_RATE_FROM_AMOUNT_TO_POINTS		= "exchange_rate_from_amount_to_points";
	public static final String VARIABLE_EXCHANGE_RATE_FROM_POINTS_TO_AMOUNT		= "exchange_rate_from_points_to_amount";
	public static final String VARIABLE_EXPIRE_TIME				= "expireTime";
	public static final String VARIABLE_EXPIRY_DATE				= "exp_date";
	public static final String VARIABLE_FAX						= "fax";
	public static final String VARIABLE_FIELD_NO				= "field_no";
	public static final String VARIABLE_FINISH					= "finish";
	public static final String VARIABLE_FIRST_SVC_NUMBER		= "first_svc_number";
	public static final String VARIABLE_FOREIGN_CURRENCY_NAME	= "foreign_currency_name";
	public static final String VARIABLE_GIFT_CERT_ID				= "gift_cert_id";
	public static final String VARIABLE_GUEST_CHECK_CONTENT	 	= "guest_check_content";
	public static final String VARIABLE_GUEST_FILE_NO			= "guest_file_no";
	public static final String VARIABLE_GUEST_NAME				= "guest_name";
	public static final String VARIABLE_GUEST_NO				= "guest_no";
	public static final String VARIABLE_HT_ID					= "ht_id";
	public static final String VARIABLE_INPUT_TYPE 				= "input_type";
	public static final String VARIABLE_INTERFACE_ID			= "intf_id";
	public static final String VARIABLE_INTERFACE_STATUS 		= "interface_status";
	public static final String VARIABLE_INTERNAL_USE			= "internal_use";
	public static final String VARIABLE_INTERNAL_USE_ENQUIRY	= "internal_use_enquiry";
	public static final String VARIABLE_INVOICE_NUM				= "invoice_number";
	public static final String VARIABLE_ISSUE_DATE				= "issue_date";
	public static final String VARIABLE_ITEM					= "item";
	public static final String VARIABLE_LIMIT					= "limit";
	public static final String VARIABLE_LOCAL_BALANCE			= "local_balance";
	public static final String VARIABLE_LOCAL_CURRENCY_NAME  	= "local_currency_name";
	public static final String VARIABLE_LOOKUP_ID				= "lookup_id";
	public static final String VARIABLE_LOOKUP_ID_CONTEXT		= "lookup_id_context";
 	public static final String VARIABLE_LOOKUP_TYPE				= "lookup_type";
	public static final String VARIABLE_MARK_UP_RATE_TEXT		= "mark_up_rate_text";
	public static final String VARIABLE_MAX_CHARGE				= "max_charge";
	public static final String VARIABLE_MAX_REDEMPT_AMOUNT		= "max_redempt_amount";
	public static final String VARIABLE_MAX_REDEMPT_POINTS		= "max_redempt_points";
	public static final String VARIABLE_MEMBER_CODE				= "member_code";
	public static final String VARIABLE_MEMBER_INFO				= "member_info";
	public static final String VARIABLE_MEMBER_LAST_NAME		= "member_last_name";
	public static final String VARIABLE_MEMBER_NAME				= "member_name";
	public static final String VARIABLE_MEMBER_NUMBER			= "member_number";
	public static final String VARIABLE_MEMBER_TYPE				= "member_type";
	public static final String VARIABLE_MEMBER_VALID_THROUGH	= "member_valid_through";
	public static final String VARIABLE_MERCHANT_NUMBER			= "merchant_number";
	public static final String VARIABLE_MIN_CHARGE				= "min_charge";
	public static final String VARIABLE_MIN_CHARGE_ITEM			= "min_charge_item";
	public static final String VARIABLE_MIX_AND_MATCH_ORIGINAL_PRICE		= "mix_and_match_original_price";
	public static final String VARIABLE_MIX_AND_MATCH_RULE_ID		= "mix_and_match_rule_id";
	public static final String VARIABLE_MODE					= "mode";
	public static final String VARIABLE_NUMBER_OF_EMPLOYEES		= "number_of_employees";
	public static final String VARIABLE_NUMBER_OF_PATRONS		= "number_of_patrons";
	public static final String VARIABLE_NO_REDEMPTION			= "no_redemption";
	public static final String VARIABLE_NON_BREAKDOWN_DETAILS	= "non_breakdown_details";
	public static final String VARIABLE_NOTE1					= "note1";
	public static final String VARIABLE_NOTE2					= "note2";
	public static final String VARIABLE_NRIC_NUMBER 			= "nric_number";
	public static final String VARIABLE_ORIGINAL_BUSINESS_DAY	= "original_business_day";
	public static final String VARIABLE_ORIGINAL_BUSINESS_PERIOD	= "original_business_period";
	public static final String VARIABLE_ORIGINAL_POINTS			= "original_points";
	public static final String VARIABLE_OUT_TRADE_NUMBER		= "outTradeNumber";
	public static final String VARIABLE_PANTRY_MESSAGE			= "pantry_message";
	public static final String VARIABLE_PASSWORD				= "password";
	public static final String VARIABLE_PAYMENT_INFO			= "payment_info";
	public static final String VARIABLE_PAYMENT_ROUNDING		= "payment_rounding";
	public static final String VARIABLE_PAYTYPE					= "paytype";
	public static final String VARIABLE_PHONE					= "phone";
	public static final String VARIABLE_PICKUP_DATE				= "pickup_date";
	public static final String VARIABLE_POINTS_ACCOUNT_NUMBER		= "points_account_number";
	public static final String VARIABLE_POINTS_ACCOUNT_PASSWORD		= "points_account_password";
	public static final String VARIABLE_POINTS_AVAILABLE		= "points_available";
	public static final String VARIABLE_POINTS_BALANCE			= "points_balance";
	public static final String VARIABLE_POINTS_CURRENT			= "points_current";
	public static final String VARIABLE_POINTS_DEPARTMENT		= "points_department";
	public static final String VARIABLE_POINTS_EARN				= "points_earn";
	public static final String VARIABLE_POINTS_RETURNED			= "points_returned";
	public static final String VARIABLE_POINTS_USE				= "points_use";
	public static final String VARIABLE_POINT_REDEEM			= "point_redeem";
	public static final String VARIABLE_POSTED_AMOUNT_USE		= "posted_amount_use";
	public static final String VARIABLE_POSTED_POINTS_USE		= "posted_points_use";
	public static final String VARIABLE_POSTING_KEY				= "posting_key";
	public static final String VARIABLE_POSTING_STRING			= "posting_string";
	public static final String VARIABLE_POST_STATUS				= "post_status";
	public static final String VARIABLE_POST_TIME				= "post_time";
	public static final String VARIABLE_PRINT_LINE				= "print_line";
	public static final String VARIABLE_PRINT_POSITION			= "print_position";
	public static final String VARIABLE_PRINT_TEXT  			= "print_text";
	public static final String VARIABLE_PRINTED_STATUS			= "printed_status";
	public static final String VARIABLE_PROMOTION_CODE 			= "promotion_code";
	public static final String VARIABLE_QR_CODE 				= "qr_code";
	public static final String VARIABLE_REASON_CODE				= "reason_code";
	public static final String VARIABLE_RECEIPT_URL 			= "receipt_url";
	public static final String VARIABLE_REDEEM_COUNT			= "redeem_count";
	public static final String VARIABLE_REFERENCE				= "reference";
	public static final String VARIABLE_REFUND_AMOUNT			= "refund_amount";
	public static final String VARIABLE_REFUND_POINTS			= "refund_points";
	public static final String VARIABLE_REGISTER_NO				= "reg_no";
	public static final String VARIABLE_REMARK					= "remark";
	public static final String VARIABLE_REMAINING_PENDING_INFO	= "remaining_pending_info";
	public static final String VARIABLE_RETRIEVED				= "retrieved";
	public static final String VARIABLE_ROOM					= "room";
	public static final String VARIABLE_RUNNING_NUMBER 			= "running_number";
	public static final String VARIABLE_SECURITY_CODE			= "security_code";
	public static final String VARIABLE_SELL_END_COUPON			= "sell_end_coupon";
	public static final String VARIABLE_SELL_START_COUPON		= "sell_start_coupon";
	public static final String VARIABLE_SET_MEMBER				= "set_member";
	public static final String VARIABLE_SPA_STANDARD_MASKED_PAN			= "spa_standard_masked_pan";
	public static final String VARIABLE_SPA_STANDARD_ISSUER				= "spa_standard_issuer";
	public static final String VARIABLE_SPA_STANDARD_TIPS				= "spa_standard_tips";
	public static final String VARIABLE_SPA_STANDARD_PARENT_AUTH_CODE	= "spa_standard_parent_auth_code";
	public static final String VARIABLE_SPA_STANDARD_AUTH_CODE			= "spa_standard_auth_code";
	public static final String VARIABLE_SPA_STANDARD_TYPE_KEY			= "spa_standard_type_key";
	public static final String VARIABLE_SPA_STANDARD_PAY_AMOUNT			= "spa_standard_pay_amount";
	public static final String VARIABLE_SPA_STANDARD_PGTX_PAY_ID		= "spa_standard_pgtx_pay_id";
	public static final String VARIABLE_SPA_STANDARD_REF_NUM			= "spa_standard_ref_num";
	public static final String VARIABLE_SPA_STANDARD_TRACE_NUM			= "spa_standard_trace_num";
	public static final String VARIABLE_SPA_STANDARD_INVOICE_NUM		= "spa_standard_invoice_num";
	public static final String VARIABLE_SPA_STANDARD_TOKEN				= "spa_standard_token";
	public static final String VARIABLE_SPLIT_REVENUE_ITEM				= "split_revenue_item";
	public static final String VARIABLE_STAFF_ID				= "staff_id";
	public static final String VARIABLE_SUB_ACCOUNT_NUMBER				= "sub_account_number";
	public static final String VARIABLE_SUB_MEMBER_TYPE			= "sub_member_type";
	public static final String VARIABLE_SVC_CARD_NUMBER			= "svc_card_number";
	public static final String VARIABLE_SVC_COUPON_AMOUNT		= "svc_coupon_amount";
	public static final String VARIABLE_SVC_COUPON_COST			= "svc_coupon_cost";
	public static final String VARIABLE_SVC_COUPON_NO			= "svc_coupon_number";
	public static final String VARIABLE_SVC_REMAINING_BALANCE	= "svc_remaining_balance";
	public static final String VARIABLE_SVC_VALID_THROUGH		= "svc_valid_through";
	public static final String VARIABLE_TABLE_REFERENCE			= "table_reference";
	public static final String VARIABLE_TABLE_MESSAGE			= "table_message";
	public static final String VARIABLE_TARGET_OUTLET_ID	 	= "target_outlet_id";
	public static final String VARIABLE_TERMINAL_NUMBER			= "terminal_number";
	public static final String VARIABLE_TIER					= "tier";
	public static final String VARIABLE_TOTAL_POINTS_BALANCE	= "total_points_balance";
	public static final String VARIABLE_TRACE_ID				= "trace_id";
	public static final String VARIABLE_USER_ID				 	= "user_id";
	public static final String VARIABLE_UUID					= "uuid";
	public static final String VARIABLE_VOID_STATUS				= "void_status";
	public static final String VARIABLE_VOUCHER_ID				= "voucher_id";
	public static final String VARIABLE_VOUCHER_NAME			= "voucher_name";
	public static final String VARIABLE_VOUCHER_NUMBER 			= "voucher_number";
	public static final String VARIABLE_VOUCHER_TYPE 			= "voucher_type";
	public static final String VARIABLE_VOUCHER_USED_QTY 		= "voucher_used_qty";
	public static final String VARIABLE_VOUCHER_VALUE 			= "voucher_value";
	public static final String VARIABLE_USER_NAME				= "user_name";
	public static final String VARIABLE_ITEM_REFERENCE			= "item_reference";
	
	
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	public static String VALUE_TRUE = "true";
	public static String VALUE_FALSE = "false";
	
	//init object with initialize value
	public PosCheckExtraInfo () {
		this.ckeiId = "";
		this.by = "";
		this.oletId = 0;
		this.chksId = "";
		this.cptyId = "";
		this.citmId = "";
		this.cdisId = "";
		this.cpayId = "";
		this.section = "";
		this.variable = "";
		this.index = 0;
		this.value = "";
		this.status = PosCheckExtraInfo.STATUS_ACTIVE;
	}
	
	//init object with other PosCheckExtraInfo
	public PosCheckExtraInfo (PosCheckExtraInfo oPosCheckExtraInfo) {
		this();
		
		this.ckeiId = oPosCheckExtraInfo.ckeiId;
		this.by = oPosCheckExtraInfo.by;
		this.oletId = oPosCheckExtraInfo.oletId;
		this.chksId = oPosCheckExtraInfo.chksId;
		this.cptyId = oPosCheckExtraInfo.cptyId;
		this.citmId = oPosCheckExtraInfo.citmId;
		this.cdisId = oPosCheckExtraInfo.cdisId;
		this.cpayId = oPosCheckExtraInfo.cpayId;
		this.section = oPosCheckExtraInfo.section;
		this.variable = oPosCheckExtraInfo.variable;
		this.index = oPosCheckExtraInfo.index;
		this.value = oPosCheckExtraInfo.value;
		this.status = oPosCheckExtraInfo.status;
	}
	
	//init object with JSONObject
	public PosCheckExtraInfo (JSONObject oJSONObject) {
		this();
		
		JSONObject oCheckExtraInfoJSONObject = null;
		oCheckExtraInfoJSONObject = oJSONObject.optJSONObject("PosCheckExtraInfo");
		if(oCheckExtraInfoJSONObject == null)
			oCheckExtraInfoJSONObject = oJSONObject;
		
		this.ckeiId = oCheckExtraInfoJSONObject.optString("ckei_id");
		this.by = oCheckExtraInfoJSONObject.optString("ckei_by");
		this.oletId = oCheckExtraInfoJSONObject.optInt("ckei_olet_id");
		this.chksId = oCheckExtraInfoJSONObject.optString("ckei_chks_id");
		this.cptyId = oCheckExtraInfoJSONObject.optString("ckei_cpty_id");
		this.citmId = oCheckExtraInfoJSONObject.optString("ckei_citm_id");
		this.cdisId = oCheckExtraInfoJSONObject.optString("ckei_cdis_id");
		this.cpayId = oCheckExtraInfoJSONObject.optString("ckei_cpay_id");
		this.section = oCheckExtraInfoJSONObject.optString("ckei_section");
		this.variable = oCheckExtraInfoJSONObject.optString("ckei_variable");
		this.index = oCheckExtraInfoJSONObject.optInt("ckei_index");
		this.value = oCheckExtraInfoJSONObject.optString("ckei_value", null);
		this.status = oCheckExtraInfoJSONObject.optString("ckei_status", PosCheckExtraInfo.STATUS_ACTIVE);
	}
	
	//init object form database by ckei_id
	public PosCheckExtraInfo (String sCkeiId) {
		this();
		this.ckeiId = sCkeiId;
	}
	
	//construct the save request
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("ckei_id", this.ckeiId);
			addSaveJSONObject.put("ckei_by", this.by);
			addSaveJSONObject.put("ckei_olet_id", this.oletId);
			addSaveJSONObject.put("ckei_chks_id", this.chksId);
			addSaveJSONObject.put("ckei_cpty_id", this.cptyId);
			addSaveJSONObject.put("ckei_citm_id", this.citmId);
			addSaveJSONObject.put("ckei_cdis_id", this.cdisId);
			addSaveJSONObject.put("ckei_cpay_id", this.cpayId);
			addSaveJSONObject.put("ckei_section", this.section);
			addSaveJSONObject.put("ckei_variable", this.variable);
			addSaveJSONObject.put("ckei_index", this.index);
			if(this.value != null)
				addSaveJSONObject.put("ckei_value", this.value);
			addSaveJSONObject.put("ckei_status", this.status);
			
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray checkTableMessageJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
					return null;
				
				if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("newCheckExtraInfos"))
					return null;
					
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("newCheckExtraInfos"))
					return null;
				
				checkTableMessageJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("newCheckExtraInfos");
		}
		return checkTableMessageJSONArray;
	}
	
	public JSONArray addUpdateWithMutlipleRecords(ArrayList<PosCheckExtraInfo> oPosCheckExtraInfos) {
		JSONObject tempCheckExtraInfoJSONObject = null, checkExtraInfoJSONObject = new JSONObject();
		JSONArray checkExtraInfoJSONArray = new JSONArray();
		JSONArray oReturnJSONArray = null;
		
		for(PosCheckExtraInfo oPosCheckExtraInfo : oPosCheckExtraInfos) {
			if(oPosCheckExtraInfo.getCkeiId().compareTo("") == 0)
				tempCheckExtraInfoJSONObject = oPosCheckExtraInfo.constructAddSaveJSON(false);
			else
				tempCheckExtraInfoJSONObject = oPosCheckExtraInfo.constructAddSaveJSON(true);
			checkExtraInfoJSONArray.put(tempCheckExtraInfoJSONObject);
		}
		
		try {
			checkExtraInfoJSONObject.put("posCheckExtraInfo", checkExtraInfoJSONArray);
		}
		catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		oReturnJSONArray = this.readDataListFromApi("gm", "pos", "saveMultiCheckExtraInfos", checkExtraInfoJSONObject.toString());
		
		return oReturnJSONArray;
	}
	
	public JSONArray readAllByCheckIds(String sConfigBy, ArrayList<String> oCheckIds, String sStatus) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("configBy", sConfigBy);
			requestJSONObject.put("checkIds", oCheckIds);
			requestJSONObject.put("status", sStatus);
		}
		catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getCheckExtraInfoByCheckIds", requestJSONObject.toString());
		
		return responseJSONArray;
	}
	
	public JSONArray readAllByCheckId(String sCheckId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("checkId", sCheckId);
		}
		catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getCheckExtraInfoByCheckId", requestJSONObject.toString());
		
		return responseJSONArray;
	}
	
	public JSONArray readAllBySectionVariableOutletIdBdayId(String sSection, String sVariable, int iOutletId, String sBDayId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("section", sSection);
			requestJSONObject.put("variable", sVariable);
			requestJSONObject.put("bdayId", sBDayId);
			requestJSONObject.put("outletId", Integer.toString(iOutletId));
		}
		catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getCheckExtraInfoBySectionVariableOutletIdBdayId", requestJSONObject.toString());
		
		return responseJSONArray;
	}
	
	//retrieve multiple check extra information
	public JSONArray readAllAdvancedOrderBySearchingInfo(JSONObject oSearchingInfoJSONObject) {
		JSONArray responseJSONArray = this.readDataListFromApi("gm", "pos", "getCheckExtraInfosBySearchingInfo", oSearchingInfoJSONObject.toString());
		return responseJSONArray;
	}
	
	//update check extra information
	public void updateCheckExtraInfoByOtherIdSectionVariable(){
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("checkId", this.chksId);
			requestJSONObject.put("payId", this.cpayId);
			requestJSONObject.put("itemId", this.citmId);
			requestJSONObject.put("discId", this.cdisId);
			requestJSONObject.put("by", this.by);
			requestJSONObject.put("section", this.section);
			requestJSONObject.put("variable", this.variable);
			requestJSONObject.put("index", this.index);
			requestJSONObject.put("value", this.value);
		}
		catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		this.readDataListFromApi("gm", "pos", "updateCheckExtraInfoByOtherIdSectionVariable", requestJSONObject.toString());
	}
	
	//get ckeiId
	public String getCkeiId() {
		return this.ckeiId;
	}
	
	public String getBy() {
		return this.by;
	}
	
	public int getOutletId() {
		return this.oletId;
	}
	
	public String getChksId() {
		return this.chksId;
	}
	
	protected String getCptyId() {
		return this.cptyId;
	}
	
	protected String getCitmId() {
		return this.citmId;
	}
	
	protected String getCdisId() {
		return this.cdisId;
	}
	
	public String getCpayId() {
		return this.cpayId;
	}
	
	public String getSection() {
		return this.section;
	}
	
	public String getVariable() {
		return this.variable;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setCkeiId(String sCkeiId) {
		this.ckeiId = sCkeiId;
	}
	
	public void setBy(String sBy) {
		this.by = sBy;
	}
	
	public void setOutletId(int iOutletId) {
		this.oletId = iOutletId;
	}
	
	public void setCheckId(String sChksId) {
		this.chksId = sChksId;
	}
	
	public void setItemId(String sItemId) {
		this.citmId = sItemId;
	}
	
	public void setSection(String sSection) {
		this.section = sSection;
	}
	
	public void setVariable(String sVariable) {
		this.variable = sVariable;
	}
	
	public void setIndex(int iIndex) {
		this.index = iIndex;
	}
	
	public void setValue(String sValue) {
		this.value = sValue;
	}
	
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	//check whether pos check extra info is equal to provided value
	//when sBy / sSection / sVariable is null, system will ignore corresponding checking
	public boolean equalToBySectionIndexVariable(String sBy, String sSection, int iIndex, String sVariable) {
		if(sBy != null && !by.equals(sBy))
			return false;
		
		if(sSection != null && !section.equals(sSection))
			return false;
		
		if(iIndex > 0 && index != iIndex)
			return false;
		
		if(sVariable != null && !variable.equals(sVariable))
			return false;
		
		return true;
	}
}
