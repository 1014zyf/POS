//Database: pos_floor_plan_tables - Tables on floor plan map
package app.model;

import org.json.JSONException;
import org.json.JSONObject;

public class PosFloorPlanTable {
	private int flrtId;
	private int bdayId;
	private int bperId;
	private int shopId;
	private int oletId;
	private int flrmId;
	private int table;
	private String tableExt;
	private String[] name;
	private String[] shortName;
	private int size;
	private int sectId;
	private String shape;
	private String flag;
	private int left;
	private int top;
	private int width;
	private int height;
	private String remark;
	private String status;
	
	// shape
	public static String SHAPE_RECTANGLE = "";
	public static String SHAPE_CIRCLE = "c";
	public static String SHAPE_RHOMBUS= "r";
	
	// flag
	public static String FLAG_NORMAL = "";
	public static String FLAG_RESERVED = "r";
	public static String FLAG_BLOCKED = "b";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosFloorPlanTable () {
		this.init();
	}
	
	//init object from database by flrt_id
	public PosFloorPlanTable (int iFlrtId) {
		this.init();
		
		this.flrtId = iFlrtId;
	}

	//construct the save request JSON
	private JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("flrt_id", this.flrtId);
			addSaveJSONObject.put("flrt_bday_id", this.bdayId);
			addSaveJSONObject.put("flrt_bper_id", this.bperId);
			addSaveJSONObject.put("flrt_shop_id", this.shopId);
			addSaveJSONObject.put("flrt_olet_id", this.oletId);
			addSaveJSONObject.put("flrt_flrm_id", this.flrmId);
			addSaveJSONObject.put("flrt_table", this.table);
			addSaveJSONObject.put("flrt_table_ext", this.tableExt);
			for(int i=1; i<=5; i++)
				addSaveJSONObject.put("flrt_name_l"+i, this.name[(i-1)]);
			for(int i=1; i<=5; i++)
				addSaveJSONObject.put("flrt_short_name_l"+i, this.shortName[(i-1)]);
			addSaveJSONObject.put("flrt_size", this.size);
			addSaveJSONObject.put("flrt_sect_id", this.sectId);
			addSaveJSONObject.put("flrt_shape", this.shape);
			addSaveJSONObject.put("flrt_flag", this.flag);
			addSaveJSONObject.put("flrt_left", this.left);
			addSaveJSONObject.put("flrt_top", this.top);
			addSaveJSONObject.put("flrt_width", this.width);
			addSaveJSONObject.put("flrt_height", this.height);
			if(this.remark != null)
				addSaveJSONObject.put("flrt_remark", this.remark);
			else
				addSaveJSONObject.put("flrt_remark", "");
			addSaveJSONObject.put("flrt_status", this.status);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.flrtId = 0;
		this.bdayId = 0;
		this.bperId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.flrmId = 0;
		this.table = 0;
		this.tableExt = "";
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.size = 0;
		this.sectId = 0;
		this.shape = PosFloorPlanTable.SHAPE_RECTANGLE;
		this.flag = PosFloorPlanTable.FLAG_NORMAL;
		this.left = 0;
		this.top = 0;
		this.width = 0;
		this.height = 0;
		this.remark = null;
		this.status = PosFloorPlanTable.STATUS_ACTIVE;
	}
	
	//read data from database by flrt_id
	public void readById (int iFlrtId) {
		this.flrtId = iFlrtId;
	}
	
	//add new floor plan table record to database
	public boolean add() {
		return true;
	}
	
	//update floor plan table record to database
	public boolean update() {
		return true;
	}
	
	public void setBdayId(int iBdayId) {
		this.bdayId = iBdayId;
	}
	
	public void setBperId(int iBperId) {
		this.bperId = iBperId;
	}
	
	public void setShopId(int iShopId) {
		this.shopId = iShopId;
	}
	
	public void setOletId(int iOletId) {
		this.oletId = iOletId;
	}
	
	public void setFlrmId(int iFlrmId) {
		this.flrmId = iFlrmId;
	}
	
	public void setTable(int iTable) {
		this.table = iTable;
	}
	
	public void setTableExt(String sTableExt) {
		this.tableExt = sTableExt;
	}

	//set name by lang index
	public void setName(int iIndex, String sName) {
		this.name[(iIndex-1)] = sName;
	}
	
	//set short name by lang index
	public void setShortName(int iIndex, String sShortName) {
		this.shortName[(iIndex-1)] = sShortName;
	}
	
	
	public void setSize(int iSize) {
		this.size = iSize;
	}
	
	public void setSectId(int iSectId) {
		this.sectId = iSectId;
	}
	
	public void setShape(String sShape) {
		this.shape = sShape;
	}
	
	public void setFlag(String sFlag) {
		this.flag = sFlag;
	}
	
	public void setLeft(int iLeft) {
		this.left = iLeft;
	}
	
	public void setTop(int iTop) {
		this.top = iTop;
	}
	
	public void setWidth(int iWidth) {
		this.width = iWidth;
	}
	
	public void setHeight(int iHeight) {
		this.height = iHeight;
	}
	
	public void setRemark(String sRemark) {
		this.remark = sRemark;
	}

	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	//get flrtId
	protected int getFlrtId() {
		return this.flrtId;
	}
	
	//get bday id
	protected int getBdayId() {
		return this.bdayId;
	}
	
	//get bper id
	protected int getBperId() {
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
	
	//get flrm id
	protected int getFlrmId() {
		return this.flrmId;
	}
	
	//get table
	protected int getTable() {
		return this.table;
	}
	
	//get table ext
	protected String getTableExt() {
		return this.tableExt;
	}
	
	//get name by lang index
	protected String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get short name by lang index
	protected String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get size
	protected int getSize() {
		return this.size;
	}
	
	//get sect id
	protected int getSectId() {
		return this.sectId;
	}
	
	//get shape
	protected String getShape() {
		return this.shape;
	}
	
	//get flag
	protected String getFlag() {
		return this.flag;
	}
	
	//get left
	protected int getLeft() {
		return this.left;
	}
	
	//get top
	protected int getTop() {
		return this.top;
	}
	
	//get width
	protected int getWidth() {
		return this.width;
	}
	
	//get height
	protected int getHeight() {
		return this.height;
	}
	
	//get remark
	protected String getRemark() {
		return this.remark;
	}
	
	//get status
	protected String getStatus() {
		return this.status;
	}
	
}
