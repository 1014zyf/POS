//Database: pos_functions - POS function list
package om;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;

public class MemMember {
	private int membId;
	private String number;
	private String login;
	private String password;
	private String type;
	private String[] lastName;
	private String[] firstName;
	private String displayName;
	private String salutation;
	private String gender;
	private int birthYear;
	private int birthMonth;
	private int birthDay;
	private String email;
	private String[] address;
	private String mobile;
	private String mobileAreaCode;
	private String homePhone;
	private String homePhoneAreaCode;
	private String officePhone;
	private String officePhoneAreaCode;
	private String photoFilename;
	private String phoneMimeType;
	private String cardNumber;
	private int lang;
	private String[] landCode;
	private DateTime createTime;
	private int createByUserId;
	private DateTime modifyTime;
	private int modifyByUserId;
	private DateTime lastLoginTime;
	private DateTime failLoginTime;
	private int failLoginCount;
	private String[] lastPassword;
	private DateTime lastChangePasswordTime;
	private DateTime lastResetPasswordTime;
	private String forceChangePassword;
	private String status;
	
	//For loyalty member
	private String bonusBalance;
	private String lastVisitDate;
	
	//For swipe card member
	private String expiryDate;
	
	private List<MemMemberModuleInfo> memberModuleInfo;
	
	// type
	private static String TYPE_NORMAL_MEMBER = "";
	private static String TYPE_EMPLOYEE_MEMBER = "e";
	private static String TYPE_COMPANY_MEMBER = "c";
	// gender
	public static String GENDER_NOT_PROVIDED = "";
	public static String GENDER_MALE = "m";
	public static String GENDER_FEMALE = "f";
	
	// forceChangePassword
	public static String FORCE_CHANGE_PASSWORD_NO = "";
	public static String FORCE_CHANGE_PASSWORD_YES = "y";
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_SUSPENDED = "s";
	public static String STATUS_DELETED = "d";
	
	public static int SEARCH_ALL_ACTIVE = 1;
	public static int SEARCH_ALL_SUSPENDED = 2;
	public static int SEARCH_ALL_NOT_DELETED = 3;
	//init object with initialize value
	public MemMember () {
		this.init();
	}
	
	//init obejct with JSONObject
	public MemMember(JSONObject memberJSONObject) {
		readDataFromJson(memberJSONObject);
	}
	
	//init obejct with JSONObject
	public MemMember(String sNumber, String sExpiryDate) {
		this.init();
		this.number = sNumber;
		this.expiryDate = sExpiryDate;
	}
	
	//read record by member id
	public boolean readById(int iMemberId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try{
			requestJSONObject.put("memberId", iMemberId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "member", "getMemberById", requestJSONObject.toString());
	}
	
	//read not deleted record by member id
	public boolean readNotDeletedById(int iMemberId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try{
			requestJSONObject.put("memberId", iMemberId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "member", "getNotDeletedMemberById", requestJSONObject.toString());
	}
	
	//read not deleted record by member id
	public boolean readByMemberNo(String sMemberNo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try{
			requestJSONObject.put("memberNo", sMemberNo);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "member", "getMemberByNo", requestJSONObject.toString());
	}
	
	// get loyalty member
	public String searchLoyaltyMember(String sMemberNo) {
		JSONObject requestJSONObject = new JSONObject();
		String sErrMsg = "";
		
		try {
			requestJSONObject.put("memberNo", sMemberNo);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClientForHq.get().call("gm", "loyalty", "searchMember", requestJSONObject.toString(), true)) {
			sErrMsg = AppGlobal.g_oLang.get()._("cannot_connect_to_master_server") +"\n"+ AppGlobal.g_oLang.get()._("fail_to_perform_loyalty_bonus_transaction");
		} else {
			if (OmWsClientGlobal.g_oWsClientForHq.get().getResponse() == null)
				return AppGlobal.g_oLang.get()._("no_response");
			
			JSONObject oTempJSONObject = OmWsClientGlobal.g_oWsClientForHq.get().getResponse();
			if (!oTempJSONObject.has("loyaltyResult")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return AppGlobal.g_oLang.get()._("invalid_response");
			}

			boolean bLoyaltyResult = oTempJSONObject.optBoolean("loyaltyResult");
			if (!bLoyaltyResult) {
				this.init();
				int iErrCode = oTempJSONObject.optInt("errorCode");
				if (iErrCode > 0) {
					switch (iErrCode) {
					case 1:
						sErrMsg = AppGlobal.g_oLang.get()._("benefit_over_max_count");
						break;
					case 2:
						sErrMsg = AppGlobal.g_oLang.get()._("member_not_found");
						break;
					case 3:
						sErrMsg = AppGlobal.g_oLang.get()._("not_allow_to_redeem_this_benefit");
						break;
					case 4:
						sErrMsg = AppGlobal.g_oLang.get()._("not_enough_bonus_for_debit");
						break;
					default:	
					case 0:
						sErrMsg = null;
						break;
					}
				} else
					sErrMsg = oTempJSONObject.optString("errorMessage");
			} else {
				if(!oTempJSONObject.has("member")) {
					OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
					this.init();
					return AppGlobal.g_oLang.get()._("invalid_response");
				}
				
				if(oTempJSONObject.isNull("member")) {
					this.init();
					return AppGlobal.g_oLang.get()._("invalid_response");
				}
				
				JSONObject tempJSONObject = oTempJSONObject.optJSONObject("member");
				readDataFromJson(tempJSONObject);
			}
		}
		
		return sErrMsg;
	}
	
	// read all members
	public JSONArray searchMember(String sType, String sValue, String sSearchMemberType, int iPage, int iLimit, int iSearchStatus) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("searchType", sType);
			requestJSONObject.put("searchValue", sValue);
			requestJSONObject.put("searchMemeberType", sSearchMemberType);
			requestJSONObject.put("page", iPage);
			requestJSONObject.put("limit", iLimit);
			requestJSONObject.put("searchStatus", iSearchStatus);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "member", "getMemberList", requestJSONObject.toString());
		
		return responseJSONArray;
	}
	
	public JSONArray searchLoyaltyMember(int iInterfaceId, String sMemberNo, ArrayList<String> oCondition) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("outletId", AppGlobal.g_oFuncOutlet.get().getOutletId());
			requestJSONObject.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
			requestJSONObject.put("interfaceId", iInterfaceId);
			requestJSONObject.put("memberNumber", sMemberNo);
			requestJSONObject.put("sessionId", AppGlobal.g_oFuncStation.get().getLoyaltySessionId());
			requestJSONObject.put("multiSearch", 1);
			if(oCondition != null && oCondition.size() > 0){
				requestJSONObject.put("condition1", oCondition.get(0));
				if (oCondition.size() > 1)
					requestJSONObject.put("condition2", oCondition.get(1));
			}
		} catch (JSONException e) {
		
		}
		responseJSONArray = this.readDatLoyaltyMemberListFromApi("gm", "interface", "loyaltySearchMember", requestJSONObject.toString());
		
		return responseJSONArray;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject memberJSONObject) {
		JSONObject tempJSONObject = null;
		JSONArray tempJSONArray = null;
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		tempJSONObject = memberJSONObject.optJSONObject("MemMember");
		if(tempJSONObject == null)
			tempJSONObject = memberJSONObject;
			
		this.init();
		this.membId = tempJSONObject.optInt("memb_id");
		this.number = tempJSONObject.optString("memb_number");
		this.login = tempJSONObject.optString("memb_login");
		this.password = tempJSONObject.optString("memb_password");
		this.type = tempJSONObject.optString("memb_type", MemMember.TYPE_NORMAL_MEMBER);
		
		if(tempJSONObject.has("lastVisit"))
			this.type = tempJSONObject.optString("memb_type");
		else
			this.type = tempJSONObject.optString("memb_type", MemMember.TYPE_NORMAL_MEMBER);
		
		this.lastName[0] = tempJSONObject.optString("memb_last_name_l1");
		this.lastName[1] = tempJSONObject.optString("memb_last_name_l2");
		this.lastName[2] = this.lastName[1];
		this.lastName[3] = this.lastName[1];
		this.lastName[4] = this.lastName[1];
		
		this.firstName[0] = tempJSONObject.optString("memb_first_name_l1");
		this.firstName[1] = tempJSONObject.optString("memb_first_name_l2");
		this.firstName[2] = this.firstName[1];
		this.firstName[3] = this.firstName[1];
		this.firstName[4] = this.firstName[1];
		
		this.displayName = tempJSONObject.optString("memb_display_name");
		this.salutation = tempJSONObject.optString("memb_salutation");
		this.gender = tempJSONObject.optString("memb_gender", MemMember.GENDER_NOT_PROVIDED);
		this.birthYear = tempJSONObject.optInt("memb_birth_year");
		this.birthMonth = tempJSONObject.optInt("memb_birth_month");
		this.birthDay = tempJSONObject.optInt("memb_birth_day");
		this.email = tempJSONObject.optString("memb_email");
		
		this.address[0] = tempJSONObject.optString("memb_address_l1");
		this.address[1] = tempJSONObject.optString("memb_address_l2");
		this.address[2] = this.address[1];
		this.address[3] = this.address[1];
		this.address[4] = this.address[1];
		
		this.mobile = tempJSONObject.optString("memb_mobile");
		this.mobileAreaCode = tempJSONObject.optString("memb_mobile_area_code");
		this.homePhone = tempJSONObject.optString("memb_home_phone");
		this.homePhoneAreaCode = tempJSONObject.optString("memb_home_phone_area_code");
		this.officePhone = tempJSONObject.optString("memb_office_phone");
		this.officePhoneAreaCode = tempJSONObject.optString("memb_office_phone_area_code");
		this.photoFilename = tempJSONObject.optString("memb_photo_filename");
		this.phoneMimeType = tempJSONObject.optString("memb_photo_mime_type");
		this.cardNumber = tempJSONObject.optString("memb_card_number");
		this.lang = tempJSONObject.optInt("memb_lang");
		
		this.landCode[0] = tempJSONObject.optString("memb_lang_code1");
		this.landCode[1] = tempJSONObject.optString("memb_lang_code2");
		this.landCode[2] = this.landCode[1];
		this.landCode[3] = this.landCode[1];
		this.landCode[4] = this.landCode[1];
		
		String sCreateTime = tempJSONObject.optString("memb_create_time");
		if(!sCreateTime.isEmpty())
			this.createTime = oFmt.parseDateTime(sCreateTime);
		
		this.createByUserId = tempJSONObject.optInt("memb_create_by_user_id");
		
		String sModifyTime = tempJSONObject.optString("memb_modify_time");
		if(!sModifyTime.isEmpty())
			this.modifyTime = oFmt.parseDateTime(sModifyTime);
		
		this.modifyByUserId = tempJSONObject.optInt("memb_modify_by_user_id");
		
		String sLastLoginTime = tempJSONObject.optString("memb_last_login_time");
		if(!sLastLoginTime.isEmpty())
			this.lastLoginTime = oFmt.parseDateTime(sLastLoginTime);
		
		String sFailLoginTime = tempJSONObject.optString("memb_fail_login_time");
		if(!sFailLoginTime.isEmpty())
			this.failLoginTime = oFmt.parseDateTime(sFailLoginTime);
		
		this.failLoginCount = tempJSONObject.optInt("memb_fail_login_count");
		
		this.lastPassword[0] = tempJSONObject.optString("memb_last_password1");
		this.lastPassword[1] = tempJSONObject.optString("memb_last_password2");
		this.lastPassword[2] = this.lastPassword[1];
		this.lastPassword[3] = this.lastPassword[1];
		this.lastPassword[4] = this.lastPassword[1];
		
		String sLastChangePasswordTime = tempJSONObject.optString("memb_last_change_password_time");
		if(!sLastChangePasswordTime.isEmpty())
			this.lastChangePasswordTime = oFmt.parseDateTime(sLastChangePasswordTime);
		
		String sLastResetPasswordTime = tempJSONObject.optString("memb_last_reset_password_time");
		if(!sLastResetPasswordTime.isEmpty())
			this.lastResetPasswordTime = oFmt.parseDateTime(sLastResetPasswordTime);
		this.forceChangePassword = tempJSONObject.optString("memb_force_change_password", MemMember.FORCE_CHANGE_PASSWORD_NO);
		this.status = tempJSONObject.optString("memb_status", MemMember.STATUS_ACTIVE);
		
		this.bonusBalance = tempJSONObject.optString("balance");
		this.lastVisitDate = tempJSONObject.optString("lastVisit");
		
		//check whether member module info record exist
		tempJSONArray = memberJSONObject.optJSONArray("MemMemberModuleInfo");
		if (tempJSONArray != null) {
			for(int i=0; i<tempJSONArray.length(); i++) {
				JSONObject memberModuleInfoJSONObject = tempJSONArray.optJSONObject(i);
				MemMemberModuleInfo oMemMemberModuleInfo = new MemMemberModuleInfo(memberModuleInfoJSONObject);
				this.memberModuleInfo.add(oMemMemberModuleInfo);
			}
		}
	}
	
	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bResult = true;
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bResult = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("member")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("member")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("member");
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray functionJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("members")) {
				// Return a list of member
				
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("members"))
					return null;
				
				functionJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("members");
			} else if (OmWsClientGlobal.g_oWsClient.get().getResponse().has("member")) {
				// Return a single member
				
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("member"))
					return null;
				
				functionJSONArray = new JSONArray();
				functionJSONArray.put(OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("member"));
			}
		}
		
		return functionJSONArray;
	}
	
	//read data from POS API
	private JSONArray readDatLoyaltyMemberListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray functionJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			else{
				functionJSONArray = new JSONArray();
				functionJSONArray.put(OmWsClientGlobal.g_oWsClient.get().getResponse());
			}
		}
		return functionJSONArray;
	}
	
	//init object
	public void init() {
		int i=0;
		
		this.membId = 0;
		this.number = "";
		this.login = "";
		this.password = "";
		this.type = MemMember.TYPE_NORMAL_MEMBER;
		if(this.lastName == null)
			this.lastName = new String[5];
		for(i=0; i<5; i++)
			this.lastName[i] = "";
		if(this.firstName == null)
			this.firstName = new String[5];
		for(i=0; i<5; i++)
			this.firstName[i] = "";
		this.displayName = "";
		this.salutation = "";
		this.gender = MemMember.GENDER_NOT_PROVIDED;
		this.birthYear = 0;
		this.birthMonth = 0;
		this.birthDay = 0;
		this.email = "";
		if(this.address == null)
			this.address = new String[5];
		for(i=0; i<5; i++)
			this.address[i] = null;
		this.mobile = "";
		this.mobileAreaCode = "";
		this.homePhone = "";
		this.homePhoneAreaCode = "";
		this.officePhone = "";
		this.officePhoneAreaCode = "";
		this.photoFilename = "";
		this.phoneMimeType = "";
		this.cardNumber = "";
		this.lang = 0;
		if(this.landCode == null)
			this.landCode = new String[5];
		for(i=0; i<5; i++)
			this.landCode[i] = "";
		this.createTime = null;
		this.createByUserId = 0;
		this.modifyTime = null;
		this.modifyByUserId = 0;
		this.lastLoginTime = null;
		this.failLoginTime = null;
		this.failLoginCount = 0;
		if(this.lastPassword == null)
			this.lastPassword = new String[10];
		for(i=0; i<10; i++)
			this.lastPassword[i] = "";
		this.lastChangePasswordTime = null;
		this.lastResetPasswordTime = null;
		this.forceChangePassword = MemMember.FORCE_CHANGE_PASSWORD_NO;
		this.status = MemMember.STATUS_ACTIVE;
		
		this.bonusBalance = "";
		this.lastVisitDate = null;
		
		this.expiryDate = "";
		
		if(this.memberModuleInfo == null)
			this.memberModuleInfo = new ArrayList<MemMemberModuleInfo>();
		else
			this.memberModuleInfo.clear();
	}
	
	//get member Id
	public int getMemberId() {
		return this.membId;
	}
	
	public String getType() {
		return this.type;
	}
	
	//get member no.
	public String getMemberNo() {
		return this.number;
	}
	
	//get last name
	public String getLastName(int iIndex) {
		return this.lastName[(iIndex-1)];
	}
	
	//get first name
	public String getFirstName(int iIndex) {
		return this.firstName[(iIndex-1)];
	}
	
	//get display name
	public String getName() {
		return this.displayName;
	}
	
	//get salutation
	public String getSalutation() {
		return this.salutation;
	}
	
	//get gender
	public String getGender() {
		return this.gender;
	}
	
	//get date of birth
	public String getDateOfBirth() {
		return String.valueOf(this.birthYear)+"-"+String.valueOf(this.birthMonth)+"-"+String.valueOf(this.birthDay);
	}
	
	//get mobile
	public String getMobile() {
		return this.mobile;
	}
	
	//get email
	public String getEmail() {
		return this.email;
	}
	
	//get home phone
	public String getHomePhone() {
		return this.homePhone;
	}
	
	//get office phone
	public String getOfficePhone() {
		return this.officePhone;
	}
	
	//get status
	public String getStatus() {
		return this.status;
	}
	
	//get card number
	public String getCardNumber(){
		return this.cardNumber;
	}
	
	//get bonus balance
	public String getBonusBalance(){
		return this.bonusBalance;
	}
	
	//get last visit date
	public String getLastVisitDate(){
		return this.lastVisitDate;
	}
	
	//get expiry date
	public String getExpiryDate(){
		return this.expiryDate;
	}	
	
	//get address
	public String getAddress(int iIndex) {
		return this.address[(iIndex - 1)];
	}
	
	//get member info
	public String getMemModuleInfoByVariable(String sVariable){
		for(MemMemberModuleInfo oMemberModuleInfo:this.memberModuleInfo){
			if(oMemberModuleInfo.getVariable().equals(sVariable)){
				return oMemberModuleInfo.getValue();
			}
		}
		
		return null;
	}
	
	//get member info by variable and alias
	public String getMemModuleInfoByVariableAndAlias(String sVariable, String sAlias){
		for(MemMemberModuleInfo oMemberModuleInfo:this.memberModuleInfo){
			if(oMemberModuleInfo.getVariable().equals(sVariable) && oMemberModuleInfo.getModuleAlias().equals(sAlias)){
				return oMemberModuleInfo.getValue();
			}
		}
		return null;
	}
	
	//get member info by variable and alias
	public HashMap<String, String> getMemModuleInfoListByAlias(String sAlias){
		HashMap<String, String> oMemModuleInfoList = new HashMap<String, String>();
		for(MemMemberModuleInfo oMemberModuleInfo:this.memberModuleInfo){
			if(oMemberModuleInfo.getModuleAlias().equals(sAlias))
				oMemModuleInfoList.put(oMemberModuleInfo.getVariable(), oMemberModuleInfo.getValue());
		}
		return oMemModuleInfoList;
	}

	public boolean isNormalMember() {
		return this.type.equals(MemMember.TYPE_NORMAL_MEMBER);
	}
	
	public boolean isEmployeeMember() {
		return this.type.equals(MemMember.TYPE_EMPLOYEE_MEMBER);
	}
	
	public boolean isCompanyMember() {
		return this.type.equals(MemMember.TYPE_COMPANY_MEMBER);
	}
}
