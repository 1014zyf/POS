//Database: pos_check_attributes Check Attributes
package om;

import org.json.JSONException;
import org.json.JSONObject;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class PosCheckAttribute {
	private String attrId;
	private int outletId;
	private String chksId;
	private int[] attrAttoId;
	private String attrStatus;
	private DateTime slaveCreatedTime;
	private DateTime slaveModifiedTime;
	private int syncId;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	public static int TABLE_ATTRIBUTE_SIZE = 10;
	
	//init object with initialize value
	public PosCheckAttribute() {
		this.init();
	}
	
	//init obejct with JSONObject
	public PosCheckAttribute(JSONObject posCheckAttributeJSONObject) {
		readDataFromJson(posCheckAttributeJSONObject);
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject posCheckAttributeJSONObject) {
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		int i = 0;
		
		JSONObject oResultPosCheckAttributes = null;
		oResultPosCheckAttributes = posCheckAttributeJSONObject.optJSONObject("PosCheckAttribute");
		if(oResultPosCheckAttributes == null)
			oResultPosCheckAttributes = posCheckAttributeJSONObject;
		
		this.init();
		this.attrId = oResultPosCheckAttributes.optString("catt_id");
		this.outletId = oResultPosCheckAttributes.optInt("catt_olet_id");
		this.chksId = oResultPosCheckAttributes.optString("catt_chks_id");
		for(i=1; i<=10; i++)
			this.attrAttoId[(i-1)] = oResultPosCheckAttributes.optInt("catt_check_attr"+i+"_atto_id", 0);
		
		this.attrStatus = oResultPosCheckAttributes.optString("ctyp_status", PosCheckAttribute.STATUS_ACTIVE);
		
		String sCattSlaveCreated = oResultPosCheckAttributes.optString("catt_slave_created");
		if(!sCattSlaveCreated.isEmpty())
			this.slaveCreatedTime = oFmt.parseDateTime(sCattSlaveCreated);
		
		String sCattModifiedCreated = oResultPosCheckAttributes.optString("catt_slave_modified");
		if(!sCattModifiedCreated.isEmpty())
			this.slaveModifiedTime = oFmt.parseDateTime(sCattModifiedCreated);
		
		this.syncId = oResultPosCheckAttributes.optInt("catt_sync_id");
	}
	
	// init value
	public void init () {
		int i=0;
		this.attrId = "";
		this.outletId = 0;
		this.chksId = "";
		
		if(this.attrAttoId == null)
			this.attrAttoId = new int[10];
		for(i=0; i<10; i++)
			this.attrAttoId[i] = 0;	
		
		this.attrStatus = PosCheckAttribute.STATUS_ACTIVE;
		this.slaveCreatedTime = null;
		this.slaveModifiedTime = null;
		this.syncId = 0;
	}
	
	//construct the save request JSON
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("catt_id", this.attrId);
			addSaveJSONObject.put("catt_olet_id", this.outletId);
			addSaveJSONObject.put("catt_chks_id", this.chksId);
			for(int i = 1; i <= 10; i++)
				addSaveJSONObject.put("catt_check_attr"+i+"_atto_id", this.attrAttoId[(i-1)]);
			
			addSaveJSONObject.put("catt_status", this.attrStatus);
			addSaveJSONObject.put("catt_salves_created", dateTimeToString(this.slaveCreatedTime));
			addSaveJSONObject.put("catt_salves_created", dateTimeToString(this.slaveModifiedTime));
			addSaveJSONObject.put("catt_sync_id", this.syncId);
			
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
	
	//getter
	public String getAttrId() {
		return attrId;
	}

	public int getOutletId() {
		return outletId;
	}

	public String getChksId() {
		return chksId;
	}

	public int[] getAttrAttoId() {
		return attrAttoId;
	}

	public String getStatus() {
		return attrStatus;
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
	
	//setter
	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}
	
	public void setOutletId(int outletId) {
		this.outletId = outletId;
	}

	public void setChksId(String chksId) {
		this.chksId = chksId;
	}

	public void setAttrAttoId(int[] attrAttoId) {
		this.attrAttoId = attrAttoId;
	}

	public void setStatus(String attrStatus) {
		this.attrStatus = attrStatus;
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
}
