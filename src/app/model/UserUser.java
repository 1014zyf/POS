package app.model;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserUser {
	private int userId;
	private String number;
	private String login;
	private String password;
	private String[] lastName;
	private String[] firstName;
	private String[] nickName;
	private String role;
	private String[] title;
	private int deptId;
	private String cardNumber;
	private String gender;
	private Date birth;
	private String email;
	private String[] address;
	private String phone1;
	private String phone2;
	private String photoFilename;
	private String photoMimeType;
	private int lang;
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
	
	private List<Integer> belongGroupIdList;
	private UserUserModuleInfoList userModuleInfoList;
	
	// role
	public static String ROLE_NORMAL = "";
	public static String ROLE_SYSTEM_ADMIN = "a";
	
	// forceChangePassword
	public static String FORCE_CHANGE_PASSWORD_NO = "";
	public static String FORCE_CHANGE_PASSWORD_YES = "y";
	
	// status 
	public static String STATUS_ACTIVE = "";
	public static String STATUS_SUSPENDED = "s";
	public static String STATUS_DELETED = "d";
	
	//init with initialized value
	public UserUser() {
		this.init();
	}
	
	//init with initialized value
	public UserUser(UserUser oUserUser) {
		int i = 0;
				
		this.init();
		this.userId = oUserUser.userId;
		this.number = oUserUser.number;
		this.login = oUserUser.login;
		this.password = oUserUser.password;
		for(i=0; i<5; i++)
			this.lastName[i] = oUserUser.lastName[i];
		for(i=0; i<5; i++)
			this.firstName[i] = oUserUser.firstName[i];
		for(i=0; i<5; i++)
			this.nickName[i] = oUserUser.nickName[i];
		this.role = oUserUser.role;
		for(i=0; i<5; i++)
			this.title[i] = oUserUser.title[i];
		this.deptId = oUserUser.deptId;
		if(oUserUser.cardNumber != null)
			this.cardNumber = oUserUser.cardNumber;
		this.gender = oUserUser.gender;
		
		if (oUserUser.birth != null)
			this.birth = new Date(oUserUser.birth.getTime());
		
		this.email = oUserUser.email;
		
		for(i=0; i<5; i++) {
			if (oUserUser.address[i] != null)
				this.address[i] = oUserUser.address[i];
		}
		
		this.phone1 = oUserUser.phone1;
		this.phone2 = oUserUser.phone2;
		this.photoFilename = oUserUser.photoFilename;
		this.photoMimeType = oUserUser.photoMimeType;
		this.lang = oUserUser.lang;
		
		if (oUserUser.createTime != null)
			this.createTime = new DateTime(oUserUser.createTime);
		
		this.createByUserId = oUserUser.createByUserId;
		
		if (oUserUser.modifyTime != null)
			this.modifyTime = new DateTime(oUserUser.modifyTime);
		
		this.modifyByUserId =oUserUser.modifyByUserId;
		
		if (oUserUser.lastLoginTime != null)
			this.lastLoginTime = new DateTime(oUserUser.lastLoginTime);
		
		if (oUserUser.failLoginTime != null)
			this.failLoginTime = new DateTime(oUserUser.failLoginTime);
		
		this.failLoginCount = oUserUser.failLoginCount;
		this.lastPassword = new String[10];
		for(i=1; i<=10; i++)
			this.lastPassword[i-1] = oUserUser.lastPassword[i-1];
		
		if (oUserUser.lastChangePasswordTime != null)
			this.lastChangePasswordTime = new DateTime(oUserUser.lastChangePasswordTime);
		
		if (oUserUser.lastResetPasswordTime != null)
			this.lastResetPasswordTime = new DateTime(oUserUser.lastResetPasswordTime);
		
		this.forceChangePassword = oUserUser.forceChangePassword;
		this.status = oUserUser.status;
		
		for(i = 0; i < oUserUser.belongGroupIdList.size(); i++) {
			this.belongGroupIdList.add(oUserUser.belongGroupIdList.get(i));
		}
		
		this.userModuleInfoList = oUserUser.userModuleInfoList;

	}
	
	//init object with JSONObject
	public UserUser(JSONObject oUserJSONObject) {
		this.readDataFromJson(oUserJSONObject);
	}
	
	// construct the save request JSON
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		int i=0;
		JSONObject addSaveJSONObject = new JSONObject();
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("user_id", this.userId);
			addSaveJSONObject.put("user_number", this.number);
			addSaveJSONObject.put("user_login", this.login);
			addSaveJSONObject.put("user_password", this.password);
			for(i=1; i<=5; i++) {
				if(!this.lastName[i-1].isEmpty())
					addSaveJSONObject.put("user_last_name_l"+i, this.lastName[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(!this.firstName[i-1].isEmpty())
					addSaveJSONObject.put("user_first_name_l"+i, this.firstName[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(!this.nickName[i-1].isEmpty())
					addSaveJSONObject.put("user_nickname_l"+i, this.nickName[(i-1)]);
			}
			addSaveJSONObject.put("user_role", this.role);
			for(i=1; i<=5; i++) {
				if(!this.title[i-1].isEmpty())
					addSaveJSONObject.put("user_title_l"+i, this.title[(i-1)]);
			}
			if(this.deptId > 0)
				addSaveJSONObject.put("user_dept_id", this.deptId);
			if(this.cardNumber != null)
				addSaveJSONObject.put("user_card_number", this.cardNumber);
			if(!this.gender.isEmpty())
				addSaveJSONObject.put("user_gender", this.gender);
			if(this.birth != null)
				addSaveJSONObject.put("user_birth", this.birth.toString());
			if(!this.email.isEmpty())
				addSaveJSONObject.put("user_email", this.email);
			for(i=1; i<=5; i++) {
				if(this.address[(i-1)] != null)
					addSaveJSONObject.put("user_address_l"+i, this.address[(i-1)]);
			}
			if(!this.phone1.isEmpty())
				addSaveJSONObject.put("user_phone1", this.phone1);
			if(!this.phone2.isEmpty())
				addSaveJSONObject.put("user_phone2", this.phone2);
			addSaveJSONObject.put("user_photo_filename", this.photoFilename);
			addSaveJSONObject.put("user_photo_mime_type", this.photoMimeType);
			addSaveJSONObject.put("user_lang", this.lang);
			if(this.createTime != null)
				addSaveJSONObject.put("user_create_time", this.createTime.toString(oFormatter));
			if(this.createByUserId > 0)
				addSaveJSONObject.put("user_create_by_user_id", this.createByUserId);
			if(this.modifyTime != null)
				addSaveJSONObject.put("user_modify_time", this.modifyTime.toString(oFormatter));
			addSaveJSONObject.put("user_modify_by_user_id", this.modifyByUserId);
			if(this.lastLoginTime != null)
				addSaveJSONObject.put("user_last_login_time", this.lastLoginTime.toString(oFormatter));
			if(this.failLoginTime != null)
				addSaveJSONObject.put("user_fail_login_time", this.failLoginTime.toString(oFormatter));
			if(this.failLoginCount > 0)
				addSaveJSONObject.put("user_fail_login_count", this.failLoginCount);
			for(i=1; i<=10; i++) {
				if(!this.lastPassword[i-1].isEmpty())
					addSaveJSONObject.put("user_last_password"+i, this.lastPassword[(i-1)]);
			}
			if(this.lastChangePasswordTime != null)
				addSaveJSONObject.put("user_last_change_password_time", this.lastChangePasswordTime.toString(oFormatter));
			if(this.lastResetPasswordTime != null)
				addSaveJSONObject.put("user_last_reset_password_time", this.lastResetPasswordTime.toString(oFormatter));
			if(!this.forceChangePassword.isEmpty())
				addSaveJSONObject.put("user_force_change_password", this.forceChangePassword);
			addSaveJSONObject.put("user_status", this.status);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bResult = true;
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, true))
			bResult = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("user")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("user")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("user");
			if(tempJSONObject.isNull("UserUser")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}
	
	//read user from database by login and password
	public String readByLoginPassword(String sLogin, String sPassword, PosLicenseControlModel oPosLicenseControlModel) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("login", sLogin);
			requestJSONObject.put("password", sPassword);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().login(sLogin, sPassword, true)) {
			// Error return from API
			this.init();
			return OmWsClientGlobal.g_oWsClient.get().getLastErrorMessage();
		} else {
			// Set the license warning message if any
			oPosLicenseControlModel.setLicenseWarning(OmWsClientGlobal.g_oWsClient.get().getLastWarningMessage());
			
			// Set the license cert
			oPosLicenseControlModel.setLicenseCert(OmWsClientGlobal.g_oWsClient.get().getLicenseCert());
			
			if(this.readDataFromApi("gm", "user", "loginByLoginPassword", requestJSONObject.toString())) 	// No error
				return "";
			else
				return "internal_error";
		}
	}
	
	//login by card no
	public boolean loginByUserCardNumber(String sCardNo) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("cardNumber", sCardNo);			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		boolean bSuccess = this.readDataFromApi("gm", "user", "loginByUserCardNumber", requestJSONObject.toString());
		if(bSuccess){
			// Login success
			// Update OM last login card no.
			OmWsClientGlobal.g_oWsClient.get().setLastLoginCardNo(sCardNo);
		}
		
		return bSuccess;
	}
	
	public boolean readByUserId(int iUserId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("userId", iUserId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "user", "getUserByUserId", requestJSONObject.toString());
	}
	
	public boolean readByUserLoginPassword(String sLogin, String sPassword) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("login", sLogin);
			requestJSONObject.put("password", sPassword);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "user", "getUserByLoginPassword", requestJSONObject.toString());
	}
	
	//read user from database by user number
	public boolean readByUserNumber(String sUserNum) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("userNumber", sUserNum);
			requestJSONObject.put("recursive", 1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "user", "getUserByUserNumber", requestJSONObject.toString());
	}
	
	//read user from database by user card number
	public boolean readByUserCardNumber(String sCardNum) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("cardNumber", sCardNum);
			requestJSONObject.put("recursive", 1);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "user", "getUserByUserCardNumber", requestJSONObject.toString());
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject oUserJSONObject) {
		JSONObject resultUser = null;
		int i;
		SimpleDateFormat oDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		DateTimeFormatter oDateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		resultUser = oUserJSONObject.optJSONObject("UserUser");
		if(resultUser == null)
			resultUser = oUserJSONObject;
			
		this.init();

		this.userId = resultUser.optInt("user_id");
		this.number = resultUser.optString("user_number");
		this.login = resultUser.optString("user_login");
		this.password = resultUser.optString("user_password");
		
		for(i=1; i<=5; i++)
			this.lastName[(i-1)] = resultUser.optString("user_last_name_l"+i);
		
		for(i=1; i<=5; i++)
			this.firstName[(i-1)] = resultUser.optString("user_first_name_l"+i);
		
		for(i=1; i<=5; i++)
			this.nickName[(i-1)] = resultUser.optString("user_nickname_l"+i);
		
		this.role = resultUser.optString("user_role", UserUser.ROLE_NORMAL);
		
		for(i=1; i<=5; i++)
			this.title[(i-1)] = resultUser.optString("user_title_l"+i);
		
		this.deptId = resultUser.optInt("user_dept_id");
		this.cardNumber = resultUser.optString("user_card_number", null);
		this.gender = resultUser.optString("user_gender");
		
		String sBirth = resultUser.optString("user_birth");
		if(!sBirth.isEmpty()) {
			try {
				this.birth = new Date(oDateFormat.parse(sBirth).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		this.email = resultUser.optString("user_email");
		for(i=1; i<=5; i++)
			this.address[(i-1)] = resultUser.optString("user_address_l"+i, null);
		this.phone1 = resultUser.optString("user_phone1");
		this.phone2 = resultUser.optString("user_phone2");
		this.photoFilename = resultUser.optString("user_photo_filename");
		this.photoMimeType = resultUser.optString("user_photo_mime_type");
		this.lang = resultUser.optInt("user_lang");
		
		String sCreateTime = resultUser.optString("user_create_time");
		if(!sCreateTime.isEmpty())
			this.createTime = oDateTimeFormat.parseDateTime(sCreateTime);
		
		this.createByUserId = resultUser.optInt("user_create_by_user_id");
		
		String sModifyTime = resultUser.optString("user_modify_time");
		if(!sModifyTime.isEmpty())
			this.modifyTime = oDateTimeFormat.parseDateTime(sModifyTime);
		
		this.modifyByUserId = resultUser.optInt("user_modify_by_user_id");
		
		String sLastLoginTime = resultUser.optString("user_last_login_time"); 
		if(!sLastLoginTime.isEmpty())
			this.lastLoginTime = oDateTimeFormat.parseDateTime(sLastLoginTime);
		
		String sFailLoginTime = resultUser.optString("user_fail_login_time");
		if(!sFailLoginTime.isEmpty())
			this.failLoginTime = oDateTimeFormat.parseDateTime(sFailLoginTime);
		
		this.failLoginCount = resultUser.optInt("user_fail_login_count");
		
		for(i=1; i<=10; i++) {
			this.lastPassword[i-1] = resultUser.optString("user_last_password"+i);
		}
		
		String sLastChangePasswordTime = resultUser.optString("user_last_change_password_time");
		if(!sLastChangePasswordTime.isEmpty())
			this.lastChangePasswordTime = oDateTimeFormat.parseDateTime(sLastChangePasswordTime);
		
		String sLastResetPasswordTime = resultUser.optString("user_last_reset_password_time");
		if(!sLastResetPasswordTime.isEmpty())
			this.lastResetPasswordTime = oDateTimeFormat.parseDateTime(sLastResetPasswordTime);
		
		this.forceChangePassword = resultUser.optString("user_force_change_password");
		this.status = resultUser.optString("user_status", UserUser.STATUS_ACTIVE);
		
		JSONArray userGroupJSONArray = oUserJSONObject.optJSONArray("UserUserGroup");
		if(userGroupJSONArray != null) {
			for(i=0; i<userGroupJSONArray.length(); i++) {
				JSONObject oTempUgrpJSONObject = userGroupJSONArray.optJSONObject(i);
				if(oTempUgrpJSONObject != null)
					this.belongGroupIdList.add(oTempUgrpJSONObject.optInt("ugrp_id"));
			}
		}
		
		JSONArray oUserUserModule = oUserJSONObject.optJSONArray("UserUserModuleInfo");
		if(oUserUserModule != null)
			userModuleInfoList.initWithJSONArray(oUserUserModule);
	}
	
	//init value
	public void init() {
		int i = 0;
		
		this.userId = 0;
		this.number = "";
		this.login = "";
		this.password = "";
		if(this.lastName == null)
			this.lastName = new String[5];
		for(i=0; i<5; i++)
			this.lastName[i] = "";
		if(this.firstName == null)
			this.firstName = new String[5];
		for(i=0; i<5; i++)
			this.firstName[i] = "";
		if(this.nickName == null)
			this.nickName = new String[5];
		for(i=0; i<5; i++)
			this.nickName[i] = "";
		this.role = UserUser.ROLE_NORMAL;
		if(this.title == null)
			this.title = new String[5];
		for(i=0; i<5; i++)
			this.title[i] = "";
		this.deptId = 0;
		this.cardNumber = null;
		this.gender = "";
		this.birth = null;
		this.email = "";
		if(this.address == null)
			this.address = new String[5];
		for(i=0; i<5; i++)
			this.address[i] = null;
		this.phone1 = "";
		this.phone2 = "";
		this.photoFilename = "";
		this.photoMimeType = "";
		this.lang = 0;
		this.createTime = null;
		this.createByUserId = 0;
		this.modifyTime = null;
		this.modifyByUserId = 0;
		this.lastLoginTime = null;
		this.failLoginTime = null;
		this.failLoginCount = 0;
		if(this.lastPassword == null)
			this.lastPassword = new String[10];
		for(i=1; i<=10; i++)
			this.lastPassword[i-1] = "";
		this.lastChangePasswordTime = null;
		this.lastResetPasswordTime = null;
		this.forceChangePassword = UserUser.FORCE_CHANGE_PASSWORD_NO;
		this.status = UserUser.STATUS_ACTIVE;
		
		if(this.belongGroupIdList == null)
			this.belongGroupIdList = new ArrayList<Integer>();
		else
			this.belongGroupIdList.clear();
		
		if(this.userModuleInfoList == null)
			this.userModuleInfoList = new UserUserModuleInfoList();
		else
			this.userModuleInfoList.clearUserModuleInfoList();
	}
	
	//get user id
	public int getUserId() {
		return this.userId;
	}
	
	//get user number
	public String getNumber() {
		return this.number;
	}
	
	//get user login 
	public String getLogin() {
		return this.login;
	}
	
	//get user password
	public String getPassword() {
		return this.password;
	}
	
	//get last name by lang index
	public String getLastName(int iIndex) {
		return this.lastName[(iIndex-1)];
	}
	
	//get first name by lang index
	public String getFirstName(int iIndex) {
		return this.firstName[(iIndex-1)];
	}
	
	//get nick name by lang index
	public String getNickName(int iIndex) {
		return this.nickName[(iIndex-1)];
	}
	
	//get role
	public String getRole() {
		return this.role;
	}
	
	//get title by lang index
	public String getTitle(int iIndex) {
		return this.title[(iIndex-1)];
	}
	
	//get user dept id
	public int getUserDeptId() {
		return this.deptId;
	}
	
	//get user card number
	public String getCardNumber() {
		return this.cardNumber;
	}
	
	//get gender
	public String getGender() {
		return this.gender;
	}
	
	//get birth
	public Date getBirth() {
		return this.birth;
	}
	
	//get email
	public String getEmail() {
		return this.email;
	}

	//get address by lang index
	public String getAddress(int iIndex) {
		return this.address[(iIndex-1)];
	}
	
	//get phone 1
	public String getPhone1() {
		return this.phone1;
	}
	
	//get phone 2
	public String getPhone2() {
		return this.phone2;
	}
	
	//get photo filename
	public String getPhotoFilename() {
		return this.photoFilename;
	}
	
	//get photo mime type
	public String getPhotoMimeType() {
		return this.photoMimeType;
	}
	
	//get lang
	public int getLang() {
		return this.lang;
	}
	
	//get create time
	public DateTime getCreateTime() {
		return this.createTime;
	}

	//get create by user id
	public int getCreateUserId() {
		return this.createByUserId;
	}
	
	//get modify time
	public DateTime getModifyTime() {
		return this.modifyTime;
	}
	
	//get modify by user id
	public int getModifyByUserId() {
		return this.modifyByUserId;
	}
	
	//get last login time
	public DateTime getLastLoginTime() {
		return this.lastLoginTime;
	}
	
	//get fail login time
	public DateTime getFailLoginTime() {
		return this.failLoginTime;
	}
	
	//get modify time
	public int getFailLoginCount() {
		return this.failLoginCount;
	}
	
	//get last password with index
	public String getLastPassword(int iIndex) {
		return this.lastPassword[iIndex];
	}
	
	//get last change password time
	public DateTime getLastChangePasswordTime() {
		return this.lastChangePasswordTime;
	}
	
	//get last reset password time
	public DateTime getLastResetPasswordTime() {
		return this.lastResetPasswordTime;
	}
	
	//get fail login time
	public String getForceChangePassword() {
		return this.forceChangePassword;
	}
	
	//get status
	public String getStatus() {
		return this.status;
	}
	
	//get belong user group id list
	public List<Integer> getBelongGroupIdList() {
		return this.belongGroupIdList;
	}
	
	//get module info by module alias and variable
	public String getModuleInfoByModuleAliasAndVariable(String sModuleAlias, String sVariable) {
		return this.userModuleInfoList.getValueByModuleAliasAndVariable(sModuleAlias, sVariable);
				
	}

}
