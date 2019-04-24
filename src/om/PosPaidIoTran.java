package om;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;

public class PosPaidIoTran {
	private String piotId;
	private String bdayId;
	private String bperId;
	private int shopId;
	private int oletId;
	private String type;
	private int piorId;
	private String[] name;
	private String[] shortName;
	private int piogId;
	private int paymId;
	private BigDecimal total;
	private String remark;
	private int userId;
	private int statId;
	private String actionTime;
	private DateTime actionLocalTime;
	
	public static String TYPE_PAID_IN = "i";
	public static String TYPE_PAID_OUT = "o";
	
	public PosPaidIoTran() {
		this.init();
	}
	
	//construct the save request JSON
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		int i=0;
		JSONObject addSaveJSONObject = new JSONObject();
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("piot_id", this.piotId);
			if(this.bdayId.compareTo("") > 0)
				addSaveJSONObject.put("piot_bday_id", this.bdayId);
			if(this.bperId.compareTo("") > 0)
				addSaveJSONObject.put("piot_bper_id", this.bperId);
			if(this.shopId > 0)
				addSaveJSONObject.put("piot_shop_id", this.shopId);
			if(this.oletId > 0)
				addSaveJSONObject.put("piot_olet_id", this.oletId);
			addSaveJSONObject.put("piot_type", this.type);
			addSaveJSONObject.put("piot_pior_id", this.piorId);
			for(i=1; i<=5; i++) {
				if(!this.name[i-1].isEmpty())
					addSaveJSONObject.put("piot_reason_name_l"+i, this.name[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(!this.shortName[i-1].isEmpty())
					addSaveJSONObject.put("piot_reason_short_name_l"+i, this.shortName[i-1]);
			}
			if(this.piogId > 0)
				addSaveJSONObject.put("piot_piog_id", this.piogId);
			addSaveJSONObject.put("piot_paym_id", this.paymId);
			addSaveJSONObject.put("piot_total", this.total);
			if(!this.remark.isEmpty())
				addSaveJSONObject.put("piot_remark", this.remark);
			addSaveJSONObject.put("piot_user_id", this.userId);
			addSaveJSONObject.put("piot_stat_id", this.statId);

			if(this.actionTime != null)
				addSaveJSONObject.put("piot_action_time", this.actionTime);
			
			if(this.actionLocalTime != null)
				addSaveJSONObject.put("piot_action_loctime", oFormatter.print(this.actionLocalTime));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}

	//add or update a outlet item to pos_outlet_item
	public boolean addUpdate(boolean bUpdate) {
		JSONObject requestJSONObject = new JSONObject();
		
		requestJSONObject = this.constructAddSaveJSON(bUpdate);
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "savePaidIoTran", requestJSONObject.toString(), false))
			return false;
		else 
			return true;
	}
	
	private void init() {
		this.piotId = "";
		this.bdayId = "";
		this.bperId = "";
		this.shopId = 0;
		this.oletId = 0;
		this.type = "";
		this.piorId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(int i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(int i=0; i<5; i++)
			this.shortName[i] = "";
		this.piogId = 0;
		this.paymId = 0;
		this.total = BigDecimal.ZERO;
		this.remark = "";
		this.userId = 0;
		this.statId = 0;
		this.actionTime = null;
		this.actionLocalTime = null;
	}
	
	public void setBdayId(String sBdayId) {
		this.bdayId = sBdayId;
	}
	
	public void setBperId(String sBperId) {
		this.bperId = sBperId;
	}
	
	public void setShopId(int iShopId) {
		this.shopId = iShopId;
	}
	
	public void setOletId(int iOletId) {
		this.oletId = iOletId;
	}
	
	public void setType(String sType) {
		this.type = sType;
	}

	public void setPiorId(int iPiorId) {
		this.piorId = iPiorId;
	}
	
	public void setName(int iIndex, String sName) {
		this.name[iIndex-1] = sName;
	}
	
	public void setShortName(int iIndex, String sShortName) {
		this.shortName[iIndex-1] = sShortName;
	}
	
	public void setPiogId(int iPiogId) {
		this.piogId = iPiogId;
	}
	
	public void setPaymId(int iPaymId) {
		this.paymId = iPaymId;
	}
	
	public void setTotal(BigDecimal dTotal) {
		this.total = dTotal;
	}
	
	public void setRemark(String sRemark) {
		this.remark = sRemark;
	}
	
	public void setUserId(int iUserId) {
		this.userId = iUserId;
	}
	
	public void setStatId(int iStatId) {
		this.statId = iStatId;
	}
	
	public void setActionTime(String sActionTime) {
		this.actionTime = sActionTime;
	}
	
	public void setActionLocTime(DateTime oActionLocalTime) {
		this.actionLocalTime = oActionLocalTime;
	}
}
