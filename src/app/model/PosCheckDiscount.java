//Database - pos_check_discounts - Sales discount applied
package app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosCheckDiscount {
	private int cdisId;
	private int bdayId;
	private int bperId;
	private int shopId;
	private int oletId;
	private int chksId;
	private int cptyId;
	private int citmId;
	private int dtypId;
	private String[] name;
	private String[] shortName;
	private int dgrpId;
	private BigDecimal roundTotal;
	private BigDecimal total;
	private BigDecimal roundAmount;
	private String method;
	private String type;
	private String usedFor;
	private String classKey;
	private BigDecimal rate;
	private BigDecimal fixAmount;
	private String includeTaxScMask;
	private String includePreDisc;
	private String includeMidDisc;
	private String applyTime;
	private DateTime applyLocTime;
	private int applyUserId;
	private int applyStatId;
	private String voidTime;
	private DateTime voidLocTime;
	private int voidUserId;
	private int voidStatId;
	private int voidVdrsId;
	private String status;
	
	private boolean bModified = false;
	private boolean isCheckDiscount = false;
	private int checkDiscountIndex = -1;
	private int checkDiscountItemCount = 0;
	private List<PosCheckDiscountItem> checkDiscountItemList;
	
	// type
	public static String TYPE_PRE_DISCOUNT = "b";
	public static String TYPE_MID_DISCOUNT = "m";
	public static String TYPE_POST_DISCOUNT = "a";
	
	// usedFor
	public static String USED_FOR_DISCOUNT = "";
	public static String USED_FOR_EXTRA_CHARGE = "c";
	
	// method 
	public static String METHOD_FIX_AMOUNT_DISCOUNT_PER_ITEM = "";
	public static String METHOD_PERCENTAGE_DISCOUNT = "x";
	public static String METHOD_FIX_AMOUNT_DISCOUNT_PER_CHECK = "f"; 
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	// includePreDisc
	public static String INCLUDE_PRE_DISC_NO = "";
	public static String INCLUDE_PRE_DISC_YES = "y";
	
	// includeMidDisc
	public static String INCLUDE_MID_DISC_NO = "";
	public static String INCLUDE_MID_DISC_YES = "y";

	//init object with initialize value
	public PosCheckDiscount() {
		this.init();
	}
	
	//init object with JSONObject
	public PosCheckDiscount (JSONObject oJSONObject) {
		this.readDataFromJson(oJSONObject);
	}
	
	public PosCheckDiscount (PosCheckDiscount oPosCheckDiscount) {
		int i=0;
		
		this.init();
		
		this.cdisId = oPosCheckDiscount.cdisId;
		this.bdayId = oPosCheckDiscount.bdayId;
		this.bperId = oPosCheckDiscount.bperId;
		this.shopId = oPosCheckDiscount.shopId;
		this.oletId = oPosCheckDiscount.oletId;
		this.chksId = oPosCheckDiscount.chksId;
		this.cptyId = oPosCheckDiscount.cptyId;
		this.citmId = oPosCheckDiscount.citmId;
		this.dtypId = oPosCheckDiscount.dtypId;
		for(i=0; i<5; i++)
			this.name[i] = oPosCheckDiscount.name[i];
		for(i=0; i<5; i++)
			this.shortName[i] = oPosCheckDiscount.shortName[i];
		this.dgrpId = oPosCheckDiscount.dgrpId;
		this.roundTotal = new BigDecimal(oPosCheckDiscount.roundTotal.toPlainString());
		this.total = new BigDecimal(oPosCheckDiscount.total.toPlainString());
		this.roundAmount = new BigDecimal(oPosCheckDiscount.roundAmount.toPlainString());
		this.method = oPosCheckDiscount.method;
		this.type = oPosCheckDiscount.type;
		this.usedFor = oPosCheckDiscount.usedFor;
		this.classKey = oPosCheckDiscount.classKey;
		this.rate = new BigDecimal(oPosCheckDiscount.rate.toPlainString());
		this.fixAmount = new BigDecimal(oPosCheckDiscount.fixAmount.toPlainString());
		this.includeTaxScMask = oPosCheckDiscount.includeTaxScMask;
		this.includePreDisc = oPosCheckDiscount.includePreDisc;
		this.includeMidDisc = oPosCheckDiscount.includeMidDisc;
		this.applyTime = oPosCheckDiscount.applyTime;
		if (oPosCheckDiscount.applyLocTime != null)
			this.applyLocTime = new DateTime(oPosCheckDiscount.applyLocTime);
		
		this.applyUserId = oPosCheckDiscount.applyUserId;
		this.applyStatId = oPosCheckDiscount.applyStatId;
		this.voidTime = oPosCheckDiscount.voidTime;
		if (oPosCheckDiscount.voidLocTime != null)
			this.voidLocTime = new DateTime(oPosCheckDiscount.voidLocTime);
		
		this.voidUserId = oPosCheckDiscount.voidUserId;
		this.voidStatId = oPosCheckDiscount.voidStatId;
		this.voidVdrsId = oPosCheckDiscount.voidVdrsId;
		this.status = oPosCheckDiscount.status;
		
		if(oPosCheckDiscount.getCitmId() == 0)
			this.isCheckDiscount = true;
		for(i=0; i<oPosCheckDiscount.checkDiscountItemList.size(); i++){
			this.checkDiscountItemList.add(new PosCheckDiscountItem(oPosCheckDiscount.checkDiscountItemList.get(i)));
		}
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray outletTableJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			try {
				outletTableJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().getJSONArray("outletTable");
			}catch(JSONException jsone) {
				try {
					if (!OmWsClientGlobal.g_oWsClient.get().getResponse().getBoolean("outletTable"))
						this.init();
					
				}catch (JSONException jsone2) {
					jsone.printStackTrace();
					jsone2.printStackTrace();
				}
			}
		}
		
		return outletTableJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject checkDiscountJSONObject) {
		JSONObject resultCheckDiscount = null;
		int i;
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		resultCheckDiscount = checkDiscountJSONObject.optJSONObject("PosCheckDiscount");
		if(resultCheckDiscount == null)
			resultCheckDiscount = checkDiscountJSONObject;
		
		this.init();
		
		this.cdisId = resultCheckDiscount.optInt("cdis_id");
		this.bdayId = resultCheckDiscount.optInt("cdis_bday_id");
		this.bperId = resultCheckDiscount.optInt("cdis_bper_id");
		this.shopId = resultCheckDiscount.optInt("cdis_shop_id");
		this.oletId = resultCheckDiscount.optInt("cdis_olet_id");
		this.chksId = resultCheckDiscount.optInt("cdis_chks_id");
		this.cptyId = resultCheckDiscount.optInt("cdis_cpty_id");
		this.citmId = resultCheckDiscount.optInt("cdis_citm_id");
		this.dtypId = resultCheckDiscount.optInt("cdis_dtyp_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultCheckDiscount.optString("cdis_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultCheckDiscount.optString("cdis_short_name_l"+i);
		this.dgrpId = resultCheckDiscount.optInt("cdis_dgrp_id");
		this.roundTotal = new BigDecimal(resultCheckDiscount.optString("cdis_round_total", "0.0"));
		this.total = new BigDecimal(resultCheckDiscount.optString("cdis_total", "0.0"));
		this.roundAmount = new BigDecimal(resultCheckDiscount.optString("cdis_round_amount", "0.0"));
		this.method = resultCheckDiscount.optString("cdis_method");
		this.type = resultCheckDiscount.optString("cdis_type");
		this.usedFor = resultCheckDiscount.optString("cdis_used_for");
		this.classKey = resultCheckDiscount.optString("cdis_class_key");
		this.rate = new BigDecimal(resultCheckDiscount.optString("cdis_rate", "0.0"));
		this.fixAmount = new BigDecimal(resultCheckDiscount.optString("cdis_fix_amount", "0.0"));
		this.includeTaxScMask = resultCheckDiscount.optString("cdis_include_tax_sc_mask");
		this.includePreDisc = resultCheckDiscount.optString("cdis_include_pre_disc", PosCheckDiscount.INCLUDE_PRE_DISC_NO);
		this.includeMidDisc = resultCheckDiscount.optString("cdis_include_mid_disc", PosCheckDiscount.INCLUDE_MID_DISC_NO);
		
		this.applyTime = resultCheckDiscount.optString("cdis_apply_time", null);
		
		String sApplyLocTime = resultCheckDiscount.optString("cdis_apply_loctime");
		if(!sApplyLocTime.isEmpty())
			this.applyLocTime = oFormatter.parseDateTime(sApplyLocTime);
		
		this.applyUserId = resultCheckDiscount.optInt("cdis_apply_user_id");
		this.applyStatId = resultCheckDiscount.optInt("cdis_apply_stat_id");
		
		this.voidTime = resultCheckDiscount.optString("cdis_void_time", null);
		
		String sVoidLocTime = resultCheckDiscount.optString("cdis_void_loctime");
		if(!sVoidLocTime.isEmpty())
			this.voidLocTime = oFormatter.parseDateTime(sVoidLocTime);
		
		this.voidUserId = resultCheckDiscount.optInt("cdis_void_user_id");
		this.voidStatId = resultCheckDiscount.optInt("cdis_void_stat_id");
		this.voidVdrsId = resultCheckDiscount.optInt("cdis_void_vdrs_id");
		this.status = resultCheckDiscount.optString("cdis_status", PosCheckDiscount.STATUS_ACTIVE);
		
		//check whether it is check discount or not
		if(this.citmId == 0)
			this.isCheckDiscount = true;
		
		//handle the pos_check_discount_items
		JSONArray oChkDiscItemJSONArray = checkDiscountJSONObject.optJSONArray("PosCheckDiscountItem");
		if(oChkDiscItemJSONArray != null) {
			PosCheckDiscountItemList oChkDiscList = new PosCheckDiscountItemList(oChkDiscItemJSONArray);
			this.checkDiscountItemList = oChkDiscList.getCheckDiscountItemList();
		}
	}

	// init value
	public void init() {
		int i=0;
		
		this.cdisId = 0;
		this.bdayId = 0;
		this.bperId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.chksId = 0;
		this.cptyId = 0;
		this.citmId = 0;
		this.dtypId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.dgrpId = 0;
		this.roundTotal = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.roundAmount = BigDecimal.ZERO;
		this.method = PosCheckDiscount.METHOD_FIX_AMOUNT_DISCOUNT_PER_ITEM;
		this.type = "";
		this.usedFor = PosCheckDiscount.USED_FOR_DISCOUNT;
		this.classKey = "";
		this.rate = BigDecimal.ZERO;
		this.fixAmount = BigDecimal.ZERO;
		this.includeTaxScMask = "";
		this.includePreDisc = PosCheckDiscount.INCLUDE_PRE_DISC_NO;
		this.includeMidDisc = PosCheckDiscount.INCLUDE_MID_DISC_NO;
		this.applyTime = null;
		this.applyLocTime = null;
		this.applyUserId = 0;
		this.applyStatId = 0;
		this.voidTime = null;
		this.voidLocTime = null;
		this.voidUserId = 0;
		this.voidStatId = 0;
		this.voidVdrsId = 0;
		this.status = PosCheckDiscount.STATUS_ACTIVE;
		
		this.isCheckDiscount = false;
		if(this.checkDiscountItemList == null)
			this.checkDiscountItemList = new ArrayList<PosCheckDiscountItem>();
		else
			this.checkDiscountItemList.clear();
	}
		
	//construct the save request JSON
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		int i=0;
		JSONObject addSaveJSONObject = new JSONObject();
		JSONArray checkDiscountItemJSONArray = new JSONArray();
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("cdis_id", this.cdisId);
			if(this.bdayId > 0)
				addSaveJSONObject.put("cdis_bday_id", this.bdayId);
			if(this.bperId > 0)
				addSaveJSONObject.put("cdis_bper_id", this.bperId);
			if(this.shopId > 0)
				addSaveJSONObject.put("cdis_shop_id", this.shopId);
			if(this.oletId > 0)
				addSaveJSONObject.put("cdis_olet_id", this.oletId);
			addSaveJSONObject.put("cdis_chks_id", this.chksId);
			addSaveJSONObject.put("cdis_cpty_id", this.cptyId);
			addSaveJSONObject.put("cdis_citm_id", this.citmId);
			if(this.dtypId > 0)
				addSaveJSONObject.put("cdis_dtyp_id", this.dtypId);
			for(i=1; i<=5; i++) {
				if(!this.name[i-1].isEmpty())
					addSaveJSONObject.put("cdis_name_l"+i, this.name[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(!this.shortName[i-1].isEmpty())
					addSaveJSONObject.put("cdis_short_name_l"+i, this.shortName[i-1]);
			}
			if(this.dgrpId > 0)
				addSaveJSONObject.put("cdis_dgrp_id", this.dgrpId);
			addSaveJSONObject.put("cdis_round_total", this.roundTotal);
			addSaveJSONObject.put("cdis_total", this.total);
			addSaveJSONObject.put("cdis_round_amount", this.roundAmount);
			if(!this.method.isEmpty())
				addSaveJSONObject.put("cdis_method", this.method);
			if(!this.type.isEmpty())
				addSaveJSONObject.put("cdis_type", this.type);
			if(!this.usedFor.isEmpty())
				addSaveJSONObject.put("cdis_used_for", this.usedFor);
			if(!this.classKey.isEmpty())
				addSaveJSONObject.put("cdis_class_key", this.classKey);
			if(this.rate.compareTo(BigDecimal.ZERO) != 0)
				addSaveJSONObject.put("cdis_rate", this.rate);
			if(this.fixAmount.compareTo(BigDecimal.ZERO) != 0)
				addSaveJSONObject.put("cdis_fix_amount", this.fixAmount);
			if(!this.includeTaxScMask.isEmpty())
				addSaveJSONObject.put("cdis_include_tax_sc_mask", this.includeTaxScMask);
			if(!this.includePreDisc.isEmpty())
				addSaveJSONObject.put("cdis_include_pre_disc", this.includePreDisc);
			if(!this.includeMidDisc.isEmpty())
				addSaveJSONObject.put("cdis_include_mid_disc", this.includeMidDisc);
			
			if(this.applyTime != null)
				addSaveJSONObject.put("cdis_apply_time", this.applyTime);
			
			if(this.applyLocTime != null)
				addSaveJSONObject.put("cdis_apply_loctime", oFormatter.print(this.applyLocTime));
			
			if(this.applyUserId > 0)
				addSaveJSONObject.put("cdis_apply_user_id", this.applyUserId);
			if(this.applyStatId > 0)
				addSaveJSONObject.put("cdis_apply_stat_id", this.applyStatId);
			
			if(this.voidTime != null)
				addSaveJSONObject.put("cdis_void_time", this.voidTime);
			
			if(this.voidLocTime != null)
				addSaveJSONObject.put("cdis_void_loctime", oFormatter.print(this.voidLocTime));
			
			if(this.voidUserId > 0)
				addSaveJSONObject.put("cdis_void_user_id", this.voidUserId);
			if(this.voidStatId > 0)
				addSaveJSONObject.put("cdis_void_stat_id", this.voidStatId);
			if(this.voidVdrsId > 0)
				addSaveJSONObject.put("cdis_void_vdrs_id", this.voidVdrsId);
			if(!this.status.isEmpty())
				addSaveJSONObject.put("cdis_status", this.status);
			
			if(this.checkDiscountIndex >= 0)
				addSaveJSONObject.put("checkDiscountIndex", this.checkDiscountIndex);
			
			if(this.checkDiscountItemList != null && !this.checkDiscountItemList.isEmpty()) {
				for(PosCheckDiscountItem oCheckDiscountItem:this.checkDiscountItemList) {
					if(oCheckDiscountItem.getCdisId() == 0)
						checkDiscountItemJSONArray.put(oCheckDiscountItem.constructAddSaveJSON(false));
					else
						checkDiscountItemJSONArray.put(oCheckDiscountItem.constructAddSaveJSON(true));
				}
				addSaveJSONObject.put("PosCheckDiscountItem", checkDiscountItemJSONArray);
			}
			
			this.bModified = true;
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}

	//add or update a check payment to database
	public boolean addUpdate(boolean update) {
		JSONObject requestJSONObject = new JSONObject();
		
		requestJSONObject = this.constructAddSaveJSON(update);
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveCheckDiscount", requestJSONObject.toString(), false))
			return false;
		else
			return true;
	}
	
	//read data from database by cdis_id
	public void readById (int iCdisId) {
		this.cdisId = iCdisId;		
	}
	
	//add or update check discount with HashMap
	public boolean addUpdateWithMutlipleRecord(HashMap<Integer, PosCheckDiscount> oCheckDiscountList) {
		JSONObject tempCheckDiscJSONObject = null;
		JSONArray checkDiscJSONArray = new JSONArray();
		
		for(PosCheckDiscount oCheckDiscount:oCheckDiscountList.values()) {
			
			if(oCheckDiscount.getCdisId() == 0)
				tempCheckDiscJSONObject = oCheckDiscount.constructAddSaveJSON(false);
			else
				tempCheckDiscJSONObject = oCheckDiscount.constructAddSaveJSON(true);
			checkDiscJSONArray.put(tempCheckDiscJSONObject);
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveMultiCheckDiscounts", checkDiscJSONArray.toString(), false))
			return false;
		else 
			return true;
	}
		
	//set modified
	public void setModified(boolean bModified) {
		this.bModified = bModified;
	}
	
	//set void time
	public void setVoidTime(String sVoidTime) {
		this.voidTime = sVoidTime;
	}
	
	//set void local time
	public void setVoidLocTime(DateTime oVoidLocalTime) {
		this.voidLocTime = oVoidLocalTime;
	}
	
	//set void user id
	public void setVoidUserId(int iUserId) {
		this.voidUserId = iUserId;
	}
	
	public void setVoidStationId(int iStationId) {
		this.voidStatId = iStationId;
	}
	//set void station id
	public void setVoidStatId(int iStationId) {
		this.voidStatId = iStationId;
	}
	
	//set void reason id
	public void setVoidVdrsId(int iVoidCodeId) {
		this.voidVdrsId = iVoidCodeId;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	//set discount id
	public void setCdisId(int iCdisId) {
		this.cdisId = iCdisId;
	}
	
	//set bday id
	public void setBdayId(int iBdayId) {
		this.bdayId = iBdayId;
	}
	
	//set bper id
	public void setBperId(int iBperId) {
		this.bperId = iBperId;
	}
	
	//set shop id
	public void setShopId(int iShopId) {
		this.shopId = iShopId;
	} 
	
	//set olet id
	public void setOletId(int iOletId) {
		this.oletId = iOletId;
	}
	
	//set chks id
	public void setChksId(int iChksId) {
		this.chksId = iChksId;
	}
	
	//set cpty id
	public void setCptyId(int iCptyId) {
		this.cptyId = iCptyId;
	}
	
	//set citm id
	public void setCitmId(int iCitmId) {
		this.citmId = iCitmId;
	}
	
	//set dtyp id
	public void setDtypId(int iDtypId) {
		this.dtypId = iDtypId;
	}
	
	//set name
	public void setName(int iIndex, String sName) {
		this.name[(iIndex-1)] = sName;
	}
	
	//set short name
	public void setShortName(int iIndex, String sShortName) {
		this.shortName[(iIndex-1)] = sShortName;
	}
	
	//set dgrp id
	public void setDgrpId(int iDgrpId) {
		this.dgrpId = iDgrpId;
	}
	
	//set method
	public void setMethod(String sMethod) {
		this.method = sMethod;
	}
	
	//set type
	public void setType(String sType) {
		this.type = sType;
	}
	
	//set used for
	public void setUsedFor(String sUsedFor) {
		this.usedFor = sUsedFor;
	}
	
	//set class key
	public void setClassKey(String sClassKey) {
		this.classKey = sClassKey;
	}
	
	//set rate
	public void setRate(BigDecimal dRate) {
		this.rate = dRate;
	}
	
	//set fix amount
	public void setFixAmount(BigDecimal dFixAmount) {
		this.fixAmount = dFixAmount;
	}
	
	//set include tax sc mask
	public void setIncludeTaxScMask(String sIncludeTaxScMask) {
		this.includeTaxScMask = sIncludeTaxScMask;
	}
	
	//set include pre disc
	public void setIncludePreDisc(String sIncludePreDisc) {
		this.includePreDisc = sIncludePreDisc;
	}
	
	//set include mid disc
	public void setIncludeMidDisc(String sIncludeMidDisc) {
		this.includeMidDisc = sIncludeMidDisc;
	}
	
	//set total
	public void setTotal(BigDecimal dTotal) {
		this.total = dTotal;
	}
	
	//set round total
	public void setRoundTotal(BigDecimal dRoundTotal) {
		this.roundTotal = dRoundTotal;
	}
	
	//set total
	public void setRoundAmount(BigDecimal dRoundAmount) {
		this.roundAmount = dRoundAmount;
	}
	
	//set apply time
	public void setApplyTime(String sApplyTime) {
		this.applyTime = sApplyTime;
	}
	
	//set local apply time
	public void setApplyLocalTime(DateTime oApplyLocTime) {
		this.applyLocTime = oApplyLocTime;
	}
	
	//set apply user id
	public void setApplyUserId(int iUserId) {
		this.applyUserId = iUserId;
	}
	
	//set apply station id
	public void setApplyStationId(int iStationId) {
		this.applyStatId = iStationId;
	}
	
	//set it is check discount
	public void setIsCheckDiscount() {
		this.isCheckDiscount = true;
	}
	
	//set check discount index
	public void setCheckDiscountIndex(int iIndex) {
		this.checkDiscountIndex = iIndex;
	}
	
	//set check discount item count
	public void addCheckDiscountItemCount(int iAddValue) {
		this.checkDiscountItemCount += iAddValue;
	}
	
	//add pos_check_discount_item to list
	public void addDiscItemToList(PosCheckDiscountItem oChkDiscItem) {
		this.checkDiscountItemList.add(oChkDiscItem);
	}
	
	//cleanup the check discount item list
	public void cleanupCheckDiscountItemList() {
		this.checkDiscountItemList.clear();
	}
	
	//get cdisId
	public int getCdisId() {
		return this.cdisId;
	}
	
	protected int getBdayId() {
		return this.bdayId;
	}
	
	protected int getBperId() {
		return this.bperId;
	}
	
	protected int getShopId() {
		return this.shopId;
	}
	
	protected int getOletId() {
		return this.oletId;
	}
	
	public int getChksId() {
		return this.chksId;
	}
	
	protected int getCptyId() {
		return this.cptyId;
	}
	
	public int getCitmId() {
		return this.citmId;
	}
	
	public int getDtypId() {
		return this.dtypId;
	}
	
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	protected int getDgrpId() {
		return this.dgrpId;
	}
	
	public BigDecimal getRoundTotal() {
		return this.roundTotal;
	}
	
	public BigDecimal getTotal() {
		return this.total;
	}
	
	public BigDecimal getRoundAmount() {
		return this.roundAmount;
	}
	
	public String getMethod() {
		return this.method;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getUsedFor() {
		return this.usedFor;
	}
	
	protected String getClassKey() {
		return this.classKey;
	}
	
	public BigDecimal getRate() {
		return this.rate;
	}
	
	public BigDecimal getFixAmount() {
		return this.fixAmount;
	}
	
	public String getIncludeTaxScMask() {
		return this.includeTaxScMask;
	}
	
	public String getIncludePreDisc() {
		return this.includePreDisc;
	}
	
	public String getIncludeMidDisc() {
		return this.includeMidDisc;
	}
	
	protected String getApplyTime() {
		return this.applyTime;
	}

	protected DateTime getApplyLocTime() {
		return this.applyLocTime;
	}
	
	protected int getApplyUserId() {
		return this.applyUserId;
	}
	
	protected int getApplyStatId() {
		return this.applyStatId;
	}
	
	protected String getVoidTime() {
		return this.voidTime;
	}
	
	protected DateTime getVoidLocTime() {
		return this.voidLocTime;
	}
	
	protected int getVoidUserId() {
		return this.voidUserId;
	}
	
	protected int getVoidStatId() {
		return this.voidStatId;
	}
	
	protected int getVoidVdrsId() {
		return this.voidVdrsId;
	}

	public String getStatus() {
		return this.status;
	}
	
	public List<PosCheckDiscountItem> getCheckDiscountItemList() {
		return this.checkDiscountItemList;
	}
	
	public int getCheckDiscountIndex() {
		return this.checkDiscountIndex;
	}
	
	public int getCheckDiscountItemCount() {
		return this.checkDiscountItemCount;
	}
	
	public boolean isModified() {
		return this.bModified;
	}
	
	public boolean isActive() {
		return this.status.equals(PosCheckDiscount.STATUS_ACTIVE);
	}
	
	public boolean isDeleted() {
		return this.status.equals(PosCheckDiscount.STATUS_DELETED);
	}
	
	public boolean isPreDiscountType() {
		return this.type.equals(PosCheckDiscount.TYPE_PRE_DISCOUNT);
	}
	
	public boolean isMidDiscountType() {
		return this.type.equals(PosCheckDiscount.TYPE_MID_DISCOUNT);
	}
	
	public boolean isPostDiscountType() {
		return this.type.equals(PosCheckDiscount.TYPE_POST_DISCOUNT);
	}
	
	public boolean isIncludePreDisc() {
		return this.includePreDisc.equals(PosCheckDiscount.INCLUDE_PRE_DISC_YES);
	}
	
	public boolean isIncludeMidDisc() {
		return this.includeMidDisc.equals(PosCheckDiscount.INCLUDE_MID_DISC_YES);
	}
	
	public boolean isPercentageDiscountMethod() {
		return this.method.equals(PosCheckDiscount.METHOD_PERCENTAGE_DISCOUNT);
	}
	
	public boolean isFixAmountDiscountMethod() {
		if(this.method.equals(PosCheckDiscount.METHOD_FIX_AMOUNT_DISCOUNT_PER_ITEM) || this.method.equals(PosCheckDiscount.METHOD_FIX_AMOUNT_DISCOUNT_PER_CHECK))
			return true;
		else
			return false;
	}
	
	public boolean isFixAmountPerItemDiscountMethod() {
		if(this.method.equals(PosCheckDiscount.METHOD_FIX_AMOUNT_DISCOUNT_PER_ITEM))
			return true;
		else
			return false;
	}
	
	public boolean isExcludePreviousDiscount() {
		return false;
	}
	
	public boolean isCheckDiscount() {
		return this.isCheckDiscount;
	}
	
	public boolean isUsedForDiscount() {
		return this.usedFor.equals(PosCheckDiscount.USED_FOR_DISCOUNT);
	}
	
	public boolean isUsedForExtraCharge() {
		return this.usedFor.equals(PosCheckDiscount.USED_FOR_EXTRA_CHARGE);
	}
}
