//Database: pos_floor_plan_maps - Maps of floor plan
package om;

import org.json.JSONException;
import org.json.JSONObject;

public class PosFloorPlanMap {
	private String flrmId;
	private String bdayId;
	private String bperId;
	private int shopId;
	private int oletId;
	private String[] name;
	private String[] shortName;
	private int seq;
	private int mediId;
	private int width;
	private int height;
	private String status;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosFloorPlanMap () {
		this.init();
	}
	
	//init object from database by flrm_id
	public PosFloorPlanMap (String sFlrmId) {
		this.init();
		
		this.flrmId = sFlrmId;
	}

	//construct the save request JSON
	private JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("flrm_id", this.flrmId);
			addSaveJSONObject.put("flrm_bday_id", this.bdayId);
			addSaveJSONObject.put("flrm_bper_id", this.bperId);
			addSaveJSONObject.put("flrm_shop_id", this.shopId);
			addSaveJSONObject.put("flrm_olet_id", this.oletId);
			for(int i=1; i<=5; i++)
				addSaveJSONObject.put("flrm_name_l"+i, this.name[(i-1)]);
			for(int i=1; i<=5; i++)
				addSaveJSONObject.put("flrm_short_name_l"+i, this.shortName[(i-1)]);
			addSaveJSONObject.put("flrm_seq", this.seq);
			addSaveJSONObject.put("flrm_medi_id", this.mediId);
			addSaveJSONObject.put("flrm_width", this.width);
			addSaveJSONObject.put("flrm_height", this.height);
			addSaveJSONObject.put("flrm_status", this.status);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.flrmId = "";
		this.bdayId = "";
		this.bperId = "";
		this.shopId = 0;
		this.oletId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.mediId = 0;
		this.width = 0;
		this.height = 0;
		this.status = PosFloorPlanMap.STATUS_ACTIVE;
	}
	
	//read object from database by flrm_id
	public void readById (String sFlrmId) {
		this.flrmId = sFlrmId;
	}
	
	//add new floor plan map record to database
	public boolean add() {
		return true;
	}
	
	//update floor plan map record to database
	public boolean update() {
		return true;
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

	//set name by lang index
	public void setName(int iIndex, String sName) {
		this.name[(iIndex-1)] = sName;
	}
	
	//set short name by lang index
	public void setShortName(int iIndex, String sShortName) {
		this.shortName[(iIndex-1)] = sShortName;
	}
	
	public void setSeq(int iSeq) {
		this.seq = iSeq;
	}
	
	public void setMediId(int iMediId) {
		this.mediId = iMediId;
	}
	
	public void setWidth(int iWidth) {
		this.width = iWidth;
	}
	
	public void setHeight(int iHeight) {
		this.height = iHeight;
	}
	
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	//get flrmId
	protected String getFlrmId() {
		return this.flrmId;
	}
	
	//get bday id
	protected String getBdayId() {
		return this.bdayId;
	}
	
	//get bper id
	protected String getBperId() {
		return this.bperId;
	}
	
	//get shop id
	protected int getShopId() {
		return this.shopId;
	}
	
	//get olet id
	protected int getOletId() {
		return this.oletId;
	}
	
	//get name by lang index
	protected String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get short name lang index
	protected String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get seq
	protected int getSeq() {
		return this.seq;
	}
	
	//get medi id
	protected int getMediId() {
		return this.mediId;
	}
	
	//get width
	protected int getWidth() {
		return this.width;
	}
		
	//get height
	protected int getHeight() {
		return this.height;
	}
	
	//get status
	protected String getStatus() {
		return this.status;
	}
}
