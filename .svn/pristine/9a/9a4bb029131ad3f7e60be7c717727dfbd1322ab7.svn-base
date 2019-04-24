//Database: pos_check_gratuities Check Gratuities
package om;

import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class PosCheckGratuity implements Cloneable {
	private String cgraId;
	private String bdayId;
	private String bperId;
	private int shopId;
	private int oletId;
	private String chksId;
	private String cptyId;
	private int gratId;
	private String[] name;
	private String[] shortName;
	private BigDecimal roundTotal;
	private BigDecimal total;
	private BigDecimal roundAmount;
	private String method;
	private BigDecimal rate;
	private String applyTime;
	private DateTime applyLocTime;
	private int applyUserId;
	private int applyStatId;
	private String voidTime;
	private DateTime voidLocTime;
	private int voidUserId;
	private int voidStatId;
	
	private String status;
	
	private DateTime slaveCreatedTime;
	private DateTime slaveModifiedTime;
	private int syncId;
	
	// method
	public static String METHOD_PERCENTAGE = "";
	public static String METHOD_FIX_AMOUNT = "f";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosCheckGratuity() {
		this.init();
	}
	
	//init obejct with JSONObject
	public PosCheckGratuity(JSONObject posCheckGratuityJSONObject) {
		readDataFromJson(posCheckGratuityJSONObject);
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject posCheckGratuityJSONObject) {
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		int i = 0;
		
		JSONObject oResultPosCheckGratuities = null;
		oResultPosCheckGratuities = posCheckGratuityJSONObject.optJSONObject("PosCheckGratuity");
		if(oResultPosCheckGratuities == null)
			oResultPosCheckGratuities = posCheckGratuityJSONObject;
		
		this.init();
		this.cgraId = oResultPosCheckGratuities.optString("cgra_id");
		this.bdayId = oResultPosCheckGratuities.optString("cgra_bday_id");
		this.bperId = oResultPosCheckGratuities.optString("cgra_bper_id");
		this.shopId = oResultPosCheckGratuities.optInt("cgra_shop_id");
		this.oletId = oResultPosCheckGratuities.optInt("cgra_olet_id");
		this.chksId = oResultPosCheckGratuities.optString("cgra_chks_id");
		this.cptyId = oResultPosCheckGratuities.optString("cgra_cpty_id");
		this.gratId = oResultPosCheckGratuities.optInt("cgra_grat_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = oResultPosCheckGratuities.optString("cgra_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = oResultPosCheckGratuities.optString("cgra_short_name_l"+i);
		
		this.roundTotal = new BigDecimal(oResultPosCheckGratuities.optString("cgra_round_total", "0.0"));
		this.total = new BigDecimal(oResultPosCheckGratuities.optString("cgra_total", "0.0"));
		this.roundAmount = new BigDecimal(oResultPosCheckGratuities.optString("cgra_round_amount", "0.0"));
		this.method = oResultPosCheckGratuities.optString("cgra_method");
		this.rate = new BigDecimal(oResultPosCheckGratuities.optString("cgra_rate", "0.0"));
		
		String sApplyLocTime = oResultPosCheckGratuities.optString("cgra_open_loctime");
		if(!sApplyLocTime.isEmpty())
			this.applyLocTime = oFmt.parseDateTime(sApplyLocTime);
		this.applyTime = oResultPosCheckGratuities.optString("cgra_open_time", oFmt.print(AppGlobal.convertTimeToUTC(applyLocTime)));
		this.applyUserId = oResultPosCheckGratuities.optInt("cgra_open_user_id");
		this.applyStatId = oResultPosCheckGratuities.optInt("cgra_open_stat_id");
		
		this.voidTime = oResultPosCheckGratuities.optString("cgra_void_time", null);
		String sVoidLocTime = oResultPosCheckGratuities.optString("cgra_void_loctime");
		if(!sVoidLocTime.isEmpty())
			this.voidLocTime = oFmt.parseDateTime(sVoidLocTime);
		this.voidUserId = oResultPosCheckGratuities.optInt("cgra_void_user_id");
		this.voidStatId = oResultPosCheckGratuities.optInt("cgra_void_stat_id");
		
		this.status = PosCheckGratuity.STATUS_ACTIVE;
		String sCattSlaveCreated = oResultPosCheckGratuities.optString("catt_slave_created");
		if(!sCattSlaveCreated.isEmpty())
			this.slaveCreatedTime = oFmt.parseDateTime(sCattSlaveCreated);
		
		String sCattModifiedCreated = oResultPosCheckGratuities.optString("catt_slave_modified");
		if(!sCattModifiedCreated.isEmpty())
			this.slaveModifiedTime = oFmt.parseDateTime(sCattModifiedCreated);
		
		this.syncId = oResultPosCheckGratuities.optInt("catt_sync_id");
	}
	
	// init value
	public void init () {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		int i=0;
		this.cgraId = "";
		this.bdayId = "";
		this.bperId = "";
		this.shopId = 0;
		this.oletId = 0;
		this.chksId = "";
		this.cptyId = "";
		this.gratId = 0;
		
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";	
		
		this.roundTotal = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.roundAmount = BigDecimal.ZERO;
		this.method = PosCheckGratuity.METHOD_PERCENTAGE;
		this.rate = BigDecimal.ZERO;
		
		this.applyLocTime = AppGlobal.getCurrentTime(false);
		this.applyTime = fmt.print(AppGlobal.convertTimeToUTC(applyLocTime));
		this.applyUserId = 0;
		this.applyStatId = 0;
		this.voidTime = null;
		this.voidLocTime = null;
		this.voidUserId = 0;
		this.voidStatId = 0;
		
		this.status = PosCheckGratuity.STATUS_ACTIVE;
		this.slaveCreatedTime = null;
		this.slaveModifiedTime = null;
		this.syncId = 0;
	}
	
	//construct the save request JSON
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("cgra_id", this.cgraId);
			addSaveJSONObject.put("cgra_bday_id", this.bdayId);
			addSaveJSONObject.put("cgra_bper_id", this.bperId);
			addSaveJSONObject.put("cgra_shop_id", this.shopId);
			addSaveJSONObject.put("cgra_olet_id", this.oletId);
			addSaveJSONObject.put("cgra_chks_id", this.chksId);
			addSaveJSONObject.put("cgra_cpty_id", this.cptyId);
			addSaveJSONObject.put("cgra_grat_id", this.gratId);
			for(int i = 1; i <= 5; i++)
				addSaveJSONObject.put("cgra_name_l"+i, this.name[(i-1)]);
			for(int i = 1; i <= 5; i++)
				addSaveJSONObject.put("cgra_short_name_l"+i, this.shortName[(i-1)]);
			
			addSaveJSONObject.put("cgra_round_total", this.roundTotal);
			addSaveJSONObject.put("cgra_total", this.total);
			addSaveJSONObject.put("cgra_round_amount", this.roundAmount);
			addSaveJSONObject.put("cgra_method", this.method);
			addSaveJSONObject.put("cgra_rate", this.rate);
			
			if (this.applyLocTime != null) {
				addSaveJSONObject.put("cgra_apply_loctime", dateTimeToString(this.applyLocTime));
				addSaveJSONObject.put("cgra_apply_time", this.applyTime);
			}
			if(this.applyUserId > 0)
				addSaveJSONObject.put("cgra_apply_user_id", this.applyUserId);
			if(this.applyStatId > 0)
				addSaveJSONObject.put("cgra_apply_stat_id", this.applyStatId);
			
			if (this.voidLocTime != null) {
				addSaveJSONObject.put("cgra_void_loctime", dateTimeToString(this.voidLocTime));
				addSaveJSONObject.put("cgra_void_time", this.voidTime);
			}
			if(this.voidUserId > 0)
			addSaveJSONObject.put("cgra_void_user_id", this.voidUserId);
			if(this.voidStatId > 0)
			addSaveJSONObject.put("cgra_void_stat_id", this.voidStatId);
			
			addSaveJSONObject.put("cgra_status", this.status);
			addSaveJSONObject.put("cgra_salves_created", dateTimeToString(this.slaveCreatedTime));
			addSaveJSONObject.put("cgra_salves_created", dateTimeToString(this.slaveModifiedTime));
			addSaveJSONObject.put("cgra_sync_id", this.syncId);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return addSaveJSONObject;
	}
	
	private String dateTimeToString (DateTime oDateTime) {
		if (oDateTime == null)
			return "";
		
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		String timeString = fmt.print(oDateTime);

		return timeString;
	}

	public String getCgraId() {
		return cgraId;
	}

	public String getBdayId() {
		return bdayId;
	}

	public String getBperId() {
		return bperId;
	}

	public int getShopId() {
		return shopId;
	}

	public int getOletId() {
		return oletId;
	}

	public String getChksId() {
		return chksId;
	}

	public String getCptyId() {
		return cptyId;
	}

	public int getGratId() {
		return gratId;
	}

	public String getName(int iIndex) {
		return name[(iIndex-1)];
	}

	public String getShortName(int iIndex) {
		return shortName[(iIndex-1)];
	}

	public BigDecimal getRoundTotal() {
		return roundTotal;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public BigDecimal getRoundAmount() {
		return roundAmount;
	}

	public String getMethod() {
		return method;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public DateTime getApplyLocTime() {
		return applyLocTime;
	}

	public int getApplyUserId() {
		return applyUserId;
	}

	public int getApplyStatId() {
		return applyStatId;
	}

	public String getVoidTime() {
		return voidTime;
	}

	public DateTime getVoidLocTime() {
		return voidLocTime;
	}

	public int getVoidUserId() {
		return voidUserId;
	}

	public int getVoidStatId() {
		return voidStatId;
	}

	public String getStatus() {
		return status;
	}

	public DateTime getSlaveCreatedTime() {
		return slaveCreatedTime;
	}

	public DateTime getSlaveModifiedTime() {
		return slaveModifiedTime;
	}

	public int getSyncId() {
		return syncId;
	}

	public void setCgraId(String cgraId) {
		this.cgraId = cgraId;
	}

	public void setBdayId(String bdayId) {
		this.bdayId = bdayId;
	}

	public void setBperId(String bperId) {
		this.bperId = bperId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public void setOletId(int oletId) {
		this.oletId = oletId;
	}

	public void setChksId(String chksId) {
		this.chksId = chksId;
	}

	public void setCptyId(String cptyId) {
		this.cptyId = cptyId;
	}

	public void setGratId(int gratId) {
		this.gratId = gratId;
	}

	public void setName(String[] name) {
		this.name = name;
	}

	public void setShortName(String[] shortName) {
		this.shortName = shortName;
	}

	public void setRoundTotal(BigDecimal roundTotal) {
		this.roundTotal = roundTotal;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public void setRoundAmount(BigDecimal roundAmount) {
		this.roundAmount = roundAmount;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public void setApplyLocTime(DateTime applyLocTime) {
		this.applyLocTime = applyLocTime;
	}

	public void setApplyUserId(int applyUserId) {
		this.applyUserId = applyUserId;
	}

	public void setApplyStatId(int applyStatId) {
		this.applyStatId = applyStatId;
	}

	public void setVoidTime(String voidTime) {
		this.voidTime = voidTime;
	}

	public void setVoidLocTime(DateTime voidLocTime) {
		this.voidLocTime = voidLocTime;
	}

	public void setVoidUserId(int voidUserId) {
		this.voidUserId = voidUserId;
	}

	public void setVoidStatId(int voidStatId) {
		this.voidStatId = voidStatId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSlaveCreatedTime(DateTime slaveCreatedTime) {
		this.slaveCreatedTime = slaveCreatedTime;
	}

	public void setSlaveModifiedTime(DateTime slaveModifiedTime) {
		this.slaveModifiedTime = slaveModifiedTime;
	}

	public void setSyncId(int syncId) {
		this.syncId = syncId;
	}
	
	//clone object
	public Object clone(){
		Object obj = null;
		
		try{
			 obj = super.clone();
		} catch (CloneNotSupportedException e){
			AppGlobal.stack2Log(e);
		}
		
		return obj;
	}
}
