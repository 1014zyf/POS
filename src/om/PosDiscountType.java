//Database: pos_discount_types Discount types
package om;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosDiscountType {
	private int dtypId;
	private String code;
	private String[] name;
	private String[] shortName;
	private int seq;
	private int dgrpId;
	private String method;
	private String type;
	private String applyTo;
	private String usedFor;
	private String classKey;
	private BigDecimal rate;
	private BigDecimal fixAmount;
	private String includeTaxScMask;
	private String includePreDisc;
	private String includeMidDisc;
	private int allowUserGrpId;
	private String rules;
	private String status;
	
	private int m_oAclItemDiscGrpId;
	
	private PosInterfaceConfigList m_oInterfaceConfigList;
	
	private boolean m_bAllow;		// check authority of discount type (permission of user group, matched discount group, matched type)
	
	// method
	public static String METHOD_FIX_AMOUNT_DISCOUNT_PER_ITEM = "";
	public static String METHOD_PERCENTAGE_DISCOUNT = "x";
	public static String METHOD_FIX_AMOUNT_DISCOUNT_PER_CHECK = "f";
	
	// type
	public static String TYPE_PRE_DISCOUNT = "b";
	public static String TYPE_MID_DISCOUNT = "m";
	public static String TYPE_POST_DISCOUNT = "a";
	
	// applyTo
	public static String APPLY_TO_CHECK_AND_ITEM = "";
	public static String APPLY_TO_CHECK = "c";
	public static String APPLY_TO_ITEM = "i";
	
	// usedFor
	public static String USED_FOR_DISCOUNT = "";
	public static String USED_FOR_EXTRA_CHARGE = "c";
	
	// class key
	public static String CLASS_KEY_EMPTY = "";
	public static String CLASS_KEY_MEMBER_DISC = "member_discount";
	public static String CLASS_KEY_EMPLOYEE_DISC = "employee_discount";
	
	// includePreDisc
	public static String INCLUDE_PRE_DISC_NO = "";
	public static String INCLUDE_PRE_DISC_YES = "y";
	
	// includeMidDisc
	public static String INCLUDE_MID_DISC_NO = "";
	public static String INCLUDE_MID_DISC_YES = "y";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	public static String STATUS_SUPENDED = "s";
	
	public static String ALLOWANCE_ALLOW = "";
	public static String ALLOWANCE_DISALLOW = "n";
	//init object with initialize value
	public PosDiscountType () {
		this.init();
	}
	
	//init obejct with JSONObject
	public PosDiscountType(JSONObject discountTypeJSONObject) {
		readDataFromJson(discountTypeJSONObject);
	}
	
	//init object from database by dtyp_id
	public PosDiscountType (int iDtypId) {
		this.init();
		
		this.dtypId = iDtypId;
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("discount_type")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}

			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("discount_type");
			if(tempJSONObject.isNull("PosDiscountType")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}
	
	//read data list from POS API
	private boolean readAllowanceFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bResult = true;
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bResult = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("discount_type")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("discount_type")) {
				this.init();
				bResult = false;
			}
		}
		
		return bResult;
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray discountTypeJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("discount_types")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("discount_types"))
				return null;
			
			discountTypeJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("discount_types");
		}
		
		return discountTypeJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject discountTypeJSONObject) {
		JSONObject resultDiscountType = null;
		JSONObject oDiscountAclJSONObject = null;
		int i;
		
		resultDiscountType = discountTypeJSONObject.optJSONObject("PosDiscountType");
		if(resultDiscountType == null)
			resultDiscountType = discountTypeJSONObject;
		
		this.init();
		this.dtypId = resultDiscountType.optInt("dtyp_id");
		this.code = resultDiscountType.optString("dtyp_code");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultDiscountType.optString("dtyp_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultDiscountType.optString("dtyp_short_name_l"+i);
		this.seq = resultDiscountType.optInt("dtyp_seq");
		this.dgrpId = resultDiscountType.optInt("dtyp_dgrp_id");
		this.method = resultDiscountType.optString("dtyp_method", PosDiscountType.METHOD_FIX_AMOUNT_DISCOUNT_PER_ITEM);
		this.type = resultDiscountType.optString("dtyp_type");
		this.applyTo = resultDiscountType.optString("dtyp_apply_to", PosDiscountType.APPLY_TO_CHECK_AND_ITEM);
		this.usedFor = resultDiscountType.optString("dtyp_used_for", PosDiscountType.USED_FOR_DISCOUNT);
		this.classKey = resultDiscountType.optString("dtyp_class_key");
		this.rate = new BigDecimal(resultDiscountType.optString("dtyp_rate", "0.0"));
		this.fixAmount = new BigDecimal(resultDiscountType.optString("dtyp_fix_amount", "0.0"));
		this.includeTaxScMask = resultDiscountType.optString("dtyp_include_tax_sc_mask");
		this.includePreDisc = resultDiscountType.optString("dtyp_include_pre_disc", PosDiscountType.INCLUDE_PRE_DISC_NO);
		this.includeMidDisc = resultDiscountType.optString("dtyp_include_mid_disc", PosDiscountType.INCLUDE_MID_DISC_NO);
		if(!resultDiscountType.isNull("dtyp_allow_ugrp_id"))
			this.allowUserGrpId = resultDiscountType.optInt("dtyp_allow_ugrp_id");
		this.rules = resultDiscountType.optString("dtyp_rules", null);
		this.status = resultDiscountType.optString("dtyp_status", PosDiscountType.STATUS_ACTIVE);
		
		oDiscountAclJSONObject = discountTypeJSONObject.optJSONObject("PosDiscountAcl");
		if(oDiscountAclJSONObject != null)
			this.m_oAclItemDiscGrpId = oDiscountAclJSONObject.optInt("dacl_digp_id");
		
		if(discountTypeJSONObject.has("discountConfigs"))
			m_oInterfaceConfigList = new PosInterfaceConfigList(discountTypeJSONObject.optJSONArray("discountConfigs"));
		
		oDiscountAclJSONObject = discountTypeJSONObject.optJSONObject("allowance");
		if(oDiscountAclJSONObject != null)
			if(oDiscountAclJSONObject.optString("allow").equals(PosDiscountType.ALLOWANCE_DISALLOW))
				this.m_bAllow = false;
	}
	
	//get active and interface by discount type id and outlet id
	public boolean readByIdOutlet(int iDtypId, int outletId, int shopId, List<Integer> oUserGrpIds) {
		JSONObject requestJSONObject = new JSONObject(), tmpJSONObject;
		JSONArray tmpJSONArray = new JSONArray();
		try {
			requestJSONObject.put("discountTypeId", iDtypId);
			requestJSONObject.put("outletId", outletId);
			requestJSONObject.put("recursive", -1);
			requestJSONObject.put("shopId", shopId);
			
			if(!oUserGrpIds.isEmpty()) {
				for(int i=0; i<oUserGrpIds.size(); i++) {
					tmpJSONObject = new JSONObject();
					tmpJSONObject.put("ugrpId", oUserGrpIds.get(i));
					tmpJSONArray.put(tmpJSONObject);
				}
			}
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataFromApi("gm", "pos", "getDiscountByIdAndOutletId", requestJSONObject.toString());
	}
	
	//get active by discount type id
	public boolean readById(int iDtypId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("discountTypeId", iDtypId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "getDiscountById", requestJSONObject.toString());
	}
	
	public boolean readByCode(String sCode) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("discountCode", sCode);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "getDiscountTypeByCode", requestJSONObject.toString());
	}
	
	//get all by item discount group and outlet Id
	public JSONArray readAllByItemDiscGrpAndOutletId(int itemDiscGrpId, int shopId, int outletId, int stationId, String sBusinessDay, boolean bIsHoliday, boolean bIsDayBeforeHoliday, boolean bIsSpecialDay, boolean bIsDayBeforeSpecialDay, int iWeekday, List<Integer> oUserGrpIds) {
		JSONObject requestJSONObject = new JSONObject(), tmpJSONObject;
		JSONArray responseJSONArray = null, tmpJSONArray = new JSONArray();	
		
		try {
			requestJSONObject.put("itemDiscountGroupId", itemDiscGrpId);
			requestJSONObject.put("outletId", outletId);
			requestJSONObject.put("recursive", -1);
			requestJSONObject.put("businessDay", sBusinessDay);
			requestJSONObject.put("isHoliday", bIsHoliday);
			requestJSONObject.put("isDayBeforeHoliday", bIsDayBeforeHoliday);
			requestJSONObject.put("isSpecialDay", bIsSpecialDay);
			requestJSONObject.put("isDayBeforeSpecialDay", bIsDayBeforeSpecialDay);
			requestJSONObject.put("weekday", iWeekday);
			requestJSONObject.put("stationId", stationId);
			requestJSONObject.put("shopId", shopId);
			
			if(!oUserGrpIds.isEmpty()) {
				for(int i=0; i<oUserGrpIds.size(); i++) {
					tmpJSONObject = new JSONObject();
					tmpJSONObject.put("ugrpId", oUserGrpIds.get(i));
					tmpJSONArray.put(tmpJSONObject);
				}
				requestJSONObject.put("ugrpIds", tmpJSONArray);
			}
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getDiscountByItemDiscGrpAndOutlet", requestJSONObject.toString());
		return responseJSONArray;
	}
		
	//get all by outlet Id
	public JSONArray readAllByIOutletId(String sDiscountType, int shopId, int outletId, int stationId, String sBusinessDay, boolean bIsHoliday, boolean bIsDayBeforeHoliday, boolean bIsSpecialDay, boolean bIsDayBeforeSpecialDay, int iWeekday, List<Integer> oUserGrpIds, boolean bShowAllWithAllowance) {
		JSONObject requestJSONObject = new JSONObject(), tmpJSONObject;
		JSONArray responseJSONArray = null, tmpJSONArray = new JSONArray();
		
		try {
			if(sDiscountType.equals("check"))
				requestJSONObject.put("getCheckDiscount", 1);
			requestJSONObject.put("outletId", outletId);
			requestJSONObject.put("recursive", -1);
			requestJSONObject.put("stationId", stationId);
			requestJSONObject.put("shopId", shopId);
			requestJSONObject.put("isShowAllWithAllowance", bShowAllWithAllowance);
			
			if (!oUserGrpIds.isEmpty()) {
				for(int i=0; i<oUserGrpIds.size(); i++) {
					tmpJSONObject = new JSONObject();
					tmpJSONObject.put("ugrpId", oUserGrpIds.get(i));
					tmpJSONArray.put(tmpJSONObject);
				}
				requestJSONObject.put("ugrpIds", tmpJSONArray);
			}
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getDiscountByOutlet", requestJSONObject.toString());
		return responseJSONArray;
	}

	public JSONArray readAllEmployeeDiscount() {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getDiscountWithEmployeeDiscount", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	//get discount allowance by item discount group id, outlet id
	/*public boolean getDiscountAllowance(int itemDiscGrpId, int iOutletId, String sBusinessDay, boolean bIsHoliday, boolean bIsDayBeforeHoliday, boolean bIsSpecialDay, boolean bIsDayBeforeSpecialDay, int iWeekday) {
		JSONObject requestJSONObject = new JSONObject();
		boolean bAllowance = false;
		
		try {
			requestJSONObject.put("discountId", this.dtypId);
			requestJSONObject.put("itemDiscountGroupId", itemDiscGrpId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("recursive", -1);
			requestJSONObject.put("businessDay", sBusinessDay);
			requestJSONObject.put("isHoliday", bIsHoliday);
			requestJSONObject.put("isDayBeforeHoliday", bIsDayBeforeHoliday);
			requestJSONObject.put("isSpecialDay", bIsSpecialDay);
			requestJSONObject.put("isDayBeforeSpecialDay", bIsDayBeforeSpecialDay);
			requestJSONObject.put("weekday", iWeekday);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		bAllowance = this.readAllowanceFromApi("gm", "pos", "getDiscountAllowance", requestJSONObject.toString());
		return bAllowance;
	}*/
	
	//get discount allowance by item discount group id list, outlet ID
//	public HashMap<Integer, Integer> getDiscountAllowanceByItemDiscGrpIdList(List<Integer> oItemDiscGrpIdList, int iOutletId, String sBusinessDay, boolean bIsHoliday, boolean bIsDayBeforeHoliday, boolean bIsSpecialDay, boolean bIsDayBeforeSpecialDay, int iWeekday) {
//		HashMap<Integer, Integer> oResultAllowance = new HashMap<Integer, Integer>();
//		JSONArray oItemDiscGrpIdArray = new JSONArray();
//		JSONObject requestJSONObject = new JSONObject(), tempJSONObject = null;
//		
//		try {
//			for(Integer iItemDiscGrpId:oItemDiscGrpIdList) {
//				tempJSONObject = new JSONObject();
//				tempJSONObject.put("id", iItemDiscGrpId);
//				oItemDiscGrpIdArray.put(tempJSONObject);
//			}
//			requestJSONObject.put("itemDiscGrpIds", oItemDiscGrpIdArray);
//			requestJSONObject.put("discountId", this.dtypId);
//			requestJSONObject.put("outletId", iOutletId);
//			requestJSONObject.put("recursive", -1);
//			requestJSONObject.put("businessDay", sBusinessDay);
//			requestJSONObject.put("isHoliday", bIsHoliday);
//			requestJSONObject.put("isDayBeforeHoliday", bIsDayBeforeHoliday);
//			requestJSONObject.put("isSpecialDay", bIsSpecialDay);
//			requestJSONObject.put("isDayBeforeSpecialDay", bIsDayBeforeSpecialDay);
//			requestJSONObject.put("weekday", iWeekday);
//		}catch (JSONException jsone) {
//			jsone.printStackTrace();
//		}
//		
//		System.out.println("getDiscountAllowanceWithItemDiscGrpIds: "+requestJSONObject.toString());
//		JSONArray oResultJSONArray = this.readDataListFromApi("gm", "pos", "getDiscountAllowanceWithItemDiscGrpIds", requestJSONObject.toString());
//		if (oResultJSONArray != null) {
//			for (int i = 0; i < oResultJSONArray.length(); i++) {
//				tempJSONObject = oResultJSONArray.optJSONObject(i);
//				if (tempJSONObject == null)
//					continue;
//				
//				if(tempJSONObject.has("discAllowance") && tempJSONObject.optBoolean("discAllowance"))
//					oResultAllowance.put(tempJSONObject.optInt("itemDiscountGroupId"), 1);
//				else
//					oResultAllowance.put(tempJSONObject.optInt("itemDiscountGroupId"), 0);
//			}
//		}
//		
//		System.out.println("oResultAllowance: "+oResultAllowance.toString());
//		return oResultAllowance;
//	}
	
	// init value
	public void init() {
		int i=0;
		
		this.dtypId = 0;
		this.code = "";
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.dgrpId = 0;
		this.method = PosDiscountType.METHOD_FIX_AMOUNT_DISCOUNT_PER_ITEM;
		this.type = "";
		this.applyTo = PosDiscountType.APPLY_TO_CHECK_AND_ITEM;
		this.usedFor = PosDiscountType.USED_FOR_DISCOUNT;
		this.classKey = "";
		this.rate = BigDecimal.ZERO;
		this.fixAmount = BigDecimal.ZERO;
		this.includeTaxScMask = "";
		this.includePreDisc = PosDiscountType.INCLUDE_PRE_DISC_NO;
		this.includeMidDisc = PosDiscountType.INCLUDE_MID_DISC_NO;
		this.allowUserGrpId = 0;
		this.rules = null;
		this.status = PosDiscountType.STATUS_ACTIVE;
		this.m_bAllow = true;
		
		this.m_oAclItemDiscGrpId = 0;
		this.m_oInterfaceConfigList = new PosInterfaceConfigList();
	}
	
	//get dtypId
	public int getDtypId() {
		return this.dtypId;
	}
	
	//get code
	public String getCode() {
		return this.code;
	}
	
	//get name by lang index
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}

	public String getBilingualName(int iIndex, int iBilingualLangIndex) {
		String sName;
		if(this.getShortName(iIndex).isEmpty())
			sName = this.getName(iIndex);
		else
			sName = this.getShortName(iIndex);
		
		if (iBilingualLangIndex > 0 && iIndex != iBilingualLangIndex) {
			if(this.getShortName(iBilingualLangIndex).isEmpty()){
				if(!sName.equals(this.getName(iBilingualLangIndex)))
					sName += "\n" + this.getName(iBilingualLangIndex);
			}	
			else{
				if(!sName.equals(this.getShortName(iBilingualLangIndex)))
					sName += "\n" + this.getShortName(iBilingualLangIndex);
			}
		}
		
		return sName;
	}
	
	//get short name by lang index
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get seq
	public int getSeq() {
		return this.seq;
	}
	
	//get dgrp id
	public int getDgrpId() {
		return this.dgrpId;
	}
	
	//get method
	public String getMethod() {
		return this.method;
	}
	
	//get type
	public String getType() {
		return this.type;
	}
	
	//get apply to
	public String getApplyTo() {
		return this.applyTo;
	}
	
	//get used for
	public String getUsedFor() {
		return this.usedFor;
	}
	
	//get class key
	public String getClassKey() {
		return this.classKey;
	}
	
	//get rate
	public BigDecimal getRate() {
		return this.rate;
	}
	
	//get fix amount
	public BigDecimal getFixAmount() {
		return this.fixAmount;
	}
	
	//get include tax sc mask
	public String getIncludeTaxScMask() {
		return this.includeTaxScMask;
	}
	
	//get include pre-disc
	public String getIncludePreDisc() {
		return this.includePreDisc;
	}
	
	//get include mid-disc
	public String getIncludeMidDisc() {
		return this.includeMidDisc;
	}
	
	public int getAllowUserGroupId() {
		return allowUserGrpId;
	}
	
	//get rules
	public String getRules() {
		return this.rules;
	}
	
	// get rules by key
	private String getRuleByKey(String sKey) {
		if(this.rules == null)
			return "";
		
		try {
			JSONObject oRules = new JSONObject(this.rules);
			if (oRules.has(sKey) && !oRules.isNull(sKey))
				return oRules.getString(sKey);
			else
				return "";
		}catch (JSONException jsone) {
			return "";
		}
	}
	
	public boolean isAllowReference() {
		return this.getRuleByKey("allow_reference").equals("1");
	}
	
	public BigDecimal getDiscountMaxLimit() {
		String sMaxLimit = this.getRuleByKey("max_limit");
		BigDecimal dMaxLimit = BigDecimal.ZERO;

		if (!"".equals(sMaxLimit)) {
			try {
				dMaxLimit =  new BigDecimal(sMaxLimit);
			} catch (Exception e) {
				//invalid format
			}
		}
		return dMaxLimit;
	}
	
	//get status
	protected String getStatus() {
		return this.status;
	}
	
	//get discount corresponding item discount group id
	public int getItemDiscGrpId() {
		return this.m_oAclItemDiscGrpId;
	}
	
	public PosInterfaceConfigList getInterfaceConfigList() {
		return m_oInterfaceConfigList;
	}
	
	//get payment configs
	public List<PosInterfaceConfig> getInterfaceConfig(String sInterfaceType) {
		if(this.m_oInterfaceConfigList == null)
			return null;
		else
			return this.m_oInterfaceConfigList.getInterfaceConfigListByInterfaceType(sInterfaceType);
	}
	
	public boolean isPercentageDiscountMethod() {
		return this.method.equals(PosDiscountType.METHOD_PERCENTAGE_DISCOUNT);
	}
	
	public boolean isFixAmountDiscountPerCheckMethod() {
		return this.method.equals(PosDiscountType.METHOD_FIX_AMOUNT_DISCOUNT_PER_CHECK);
	}
	
	public boolean isFixAmountDiscountPerItemMethod() {
		return this.method.equals(PosDiscountType.METHOD_FIX_AMOUNT_DISCOUNT_PER_ITEM);
	}
	
	public boolean isApplyToCheck() {
		return this.applyTo.equals(PosDiscountType.APPLY_TO_CHECK);
	}
	
	public boolean isApplyToItem() {
		return this.applyTo.equals(PosDiscountType.APPLY_TO_ITEM);
	}
	
	public boolean isUsedForDiscount() {
		return this.usedFor.equals(PosDiscountType.USED_FOR_DISCOUNT);
	}
	
	public boolean isUsedForExtraCharge() {
		return this.usedFor.equals(USED_FOR_EXTRA_CHARGE);
	}
	
	public boolean isEmployeeDiscount() {
		return this.classKey.equals(PosDiscountType.CLASS_KEY_EMPLOYEE_DISC);
	}
	
	public boolean isMemberDiscount() {
		return this.classKey.equals(PosDiscountType.CLASS_KEY_MEMBER_DISC);
	}
	
	public boolean isPostDiscount() {
		if(this.type.equals(PosDiscountType.TYPE_POST_DISCOUNT))
			return true;
		else
			return false;
	}
	
	public boolean isAllow(){
		return this.m_bAllow;
	}
	
	// do not allow to apply discount which is not match discount type / extra charge / incorrect user permission / incorrect discount group
	public boolean checkDiscountAvailability(String sApplyDiscountType) {
		if ((sApplyDiscountType.equals("item") && this.isApplyToCheck())
				|| (sApplyDiscountType.equals("check") && this.isApplyToItem())
				|| this.isUsedForExtraCharge()
				|| !this.isAllow())
			return false;
		else
			return true;
	}
}
