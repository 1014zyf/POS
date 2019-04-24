package om;

import org.json.JSONException;
import org.json.JSONObject;

public class PosCheckTaxScRef {
	private String ctsrId;
	private String by;
	private int oletId;
	private String bperId;
	private String chksId;
	private String citmId;
	private String variable;
	private String value;
	private String status;
	
	public static final String BY_CHECK = "check";
	public static final String BY_ITEM = "item";
	
	public static final String STATUS_ACTIVE = "";
	public static final String STATUS_DELETED = "d";

	public PosCheckTaxScRef() {
		this.init();
	}
	
	public PosCheckTaxScRef (JSONObject oJSONObject) {
		init();
		
		JSONObject oCheckTaxScRefJSONObject = null;
		oCheckTaxScRefJSONObject = oJSONObject.optJSONObject("PosCheckTaxScRef");
		if(oCheckTaxScRefJSONObject == null)
			oCheckTaxScRefJSONObject = oJSONObject;
		
		this.ctsrId = oCheckTaxScRefJSONObject.optString("ctsr_id");
		this.by = oCheckTaxScRefJSONObject.optString("ctsr_by");
		this.oletId = oCheckTaxScRefJSONObject.optInt("ctsr_olet_id");
		this.bperId = oCheckTaxScRefJSONObject.optString("ctsr_bper_id");
		this.chksId = oCheckTaxScRefJSONObject.optString("ctsr_chks_id");
		this.citmId = oCheckTaxScRefJSONObject.optString("ctsr_citm_id");
		this.variable = oCheckTaxScRefJSONObject.optString("ctsr_variable");
		this.value = oCheckTaxScRefJSONObject.optString("ctsr_value", null);
		this.status = oCheckTaxScRefJSONObject.optString("ctsr_status", PosCheckTaxScRef.STATUS_ACTIVE);
	}
	
	//init object with other PosCheckTaxScRef
	public PosCheckTaxScRef (PosCheckTaxScRef oPosCheckTaxScRef) {
		init();
		
		this.ctsrId = oPosCheckTaxScRef.ctsrId;
		this.by = oPosCheckTaxScRef.by;
		this.oletId = oPosCheckTaxScRef.oletId;
		this.bperId = oPosCheckTaxScRef.bperId;
		this.chksId = oPosCheckTaxScRef.chksId;
		this.citmId = oPosCheckTaxScRef.citmId;
		this.variable = oPosCheckTaxScRef.variable;
		this.value = oPosCheckTaxScRef.value;
		this.status = oPosCheckTaxScRef.status;
	}
	
	//construct the save request
	public JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("ctsr_id", this.ctsrId);
			addSaveJSONObject.put("ctsr_by", this.by);
			addSaveJSONObject.put("ctsr_olet_id", this.oletId);
			addSaveJSONObject.put("ctsr_bper_id", this.bperId);
			addSaveJSONObject.put("ctsr_chks_id", this.chksId);
			addSaveJSONObject.put("ctsr_citm_id", this.citmId);
			addSaveJSONObject.put("ctsr_variable", this.variable);
			if (this.value != null)
				addSaveJSONObject.put("ctsr_value", this.value);
			addSaveJSONObject.put("ctsr_status", this.status);
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	private void init() {
		ctsrId = "";
		by = "";
		oletId = 0;
		bperId = "";
		chksId = "";
		citmId = "";
		variable = "";
		value = null;
		status = PosCheckTaxScRef.STATUS_ACTIVE;
	}
	
	public void setCtsrId(String sId) {
		this.ctsrId = sId;
	}
	
	public void setBy(String sBy) {
		this.by = sBy;
	}
	
	public void setOutletId(int iOletId) {
		this.oletId = iOletId;
	}
	
	public void setBussinessPeriodId(String sBperId) {
		this.bperId = sBperId;
	}
	
	public void setCheckId(String sChksId) {
		this.chksId = sChksId;
	}
	
	public void setItemId(String sItemId) {
		this.citmId = sItemId;
	}
	
	public void setVariable(String sVariable) {
		this.variable = sVariable;
	}
	
	public void setValue(String sValue) {
		this.value = sValue;
	}
	
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	public String getCtsrId() {
		return this.ctsrId;
	}
	
	public String getBy() {
		return this.by;
	}
	
	public String getVariable() {
		return this.variable;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public boolean isDeleted() {
		return this.status.equals(PosCheckTaxScRef.STATUS_DELETED);
	}
}
