package om;

import org.json.JSONObject;

public class OutFloorPlanTable {
	private int flrtId;
	private int flrpId;
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
	public static String SHAPE_RHOMBUS = "r";
	
	// flag
	public static String FLAG_NORMAL = "";
	public static String FLAG_RESERVED = "r";
	public static String FLAG_BLOCKED = "b";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init with initialized value
	public OutFloorPlanTable() {
		this.init();
	}
	
	public OutFloorPlanTable(JSONObject floorPlanTableJSONObject) {
		int i = 0;
		JSONObject resultFloorPlanTable = null;

		resultFloorPlanTable = floorPlanTableJSONObject.optJSONObject("OutFloorPlanTable");
		if(resultFloorPlanTable == null)
			resultFloorPlanTable = floorPlanTableJSONObject;

		this.init();
		this.flrtId = resultFloorPlanTable.optInt("flrt_id");
		this.flrpId = resultFloorPlanTable.optInt("flrt_flrp_id");
		this.flrmId = resultFloorPlanTable.optInt("flrt_flrm_id");
		this.table = resultFloorPlanTable.optInt("flrt_table");
		this.tableExt = resultFloorPlanTable.optString("flrt_table_ext");
		
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultFloorPlanTable.optString("flrt_name_l"+i);
		
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultFloorPlanTable.optString("flrt_short_name_l"+i);
		
		this.size = resultFloorPlanTable.optInt("flrt_size");
		this.sectId = resultFloorPlanTable.optInt("flrt_sect_id");
		this.shape = resultFloorPlanTable.optString("flrt_shape", OutFloorPlanTable.SHAPE_RECTANGLE);
		this.flag = resultFloorPlanTable.optString("flrt_flag", OutFloorPlanTable.FLAG_NORMAL);
		this.left = resultFloorPlanTable.optInt("flrt_left");
		this.top = resultFloorPlanTable.optInt("flrt_top");
		this.width = resultFloorPlanTable.optInt("flrt_width");
		this.height = resultFloorPlanTable.optInt("flrt_height");
		this.remark = resultFloorPlanTable.optString("flrt_remark", null);
		this.status = resultFloorPlanTable.optString("flrt_status", OutFloorPlanTable.STATUS_ACTIVE);
	}
	
	private JSONObject readDataObjectFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;

			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("OutFloorPlanTable")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("OutFloorPlanTable")) 
				return null;
			
			return OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("OutFloorPlanTable"); 
		}	
	}
	
	//init object value
	public void init() {
		int i=0;
		
		this.flrtId = 0;
		this.flrpId = 0;
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
		this.shape = OutFloorPlanTable.SHAPE_RECTANGLE;
		this.flag = OutFloorPlanTable.FLAG_NORMAL;
		this.left = 0;
		this.top = 0;
		this.width = 0;
		this.height = 0;
		this.remark = null;
		this.status = OutFloorPlanTable.STATUS_ACTIVE;
	}
	
	public void setFloorPlanTableId(int iId){
		this.flrtId = iId;
	}
	
	public void setFloorPlanId(int iFloorPlanId) {
		this.flrpId = iFloorPlanId;
	}
	
	public void setFloorPlanMapId(int iFloorPlanMapId) {
		this.flrmId = iFloorPlanMapId;
	}
	
	public void setTable(int iTable) {
		this.table = iTable;
	}
	
	public void setTableExt(String sTableExt) {
		this.tableExt = sTableExt;
	}
	
	public void setName(int iIndex, String sName) {
		this.name[(iIndex-1)] = sName;
	}
	
	public void setShortName(int iIndex, String sShortName) {
		this.shortName[(iIndex-1)] = sShortName;
	}
	
	public void setSectionId(int iSectionId) {
		this.sectId = iSectionId;
	}
	
	public void setShape(String sShape) {
		this.shape = sShape;
	}
	
	public void setFlag(String sFlag) {
		this.flag = sFlag;
	}
	
	public void setTop(int iTop) {
		this.top = iTop;
	}
	
	public void setLeft(int iLeft) {
		this.left = iLeft;
	}
	
	public void setWidth(int iWidth) {
		this.width = iWidth;
	}
	
	public void setHeight(int iHeight) {
		this.height = iHeight;
	}
	
	public int getFloorPlanTableId(){
		return this.flrtId;
	}
	
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	public String[] getName() {
		return this.name;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public int getFloorPlanId() {
		return this.flrpId;
	}
	
	public int getFloorPlanMapId() {
		return this.flrmId;
	}
	
	public int getTable() {
		return this.table;
	}
	
	public String getTableExt() {
		return this.tableExt;
	}
	
	public int getSectionId() {
		return this.sectId;
	}
	
	public String getShape() {
		return this.shape;
	}
	
	public String getFlag() {
		return this.flag;
	}
	
	public int getTop() {
		return this.top;
	}
	
	public int getLeft() {
		return this.left;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public String getRemark() {
		return this.remark;
	}
	
	public String getStatus() {
		return this.status;
	}
}
