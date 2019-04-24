package om;

import java.util.ArrayList;

import org.json.JSONObject;

public class OutFloorPlanMap {
	private int flrmId;
	private int flrpId;
	private String[] name;
	private String[] shortName;
	private int seq;
	private int medi_id;
	private int width;
	private int height;
	private String status;
	
	private MedMedia m_oFloorPlanMedia;
	private ArrayList<OutFloorPlanTable> m_oOutFloorPlanTableList;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init with initialized value
	public OutFloorPlanMap() {
		this.init();
	}
	
	public OutFloorPlanMap(JSONObject floorPlanMapJSONObject) {
		int i = 0;
		JSONObject resultFloorPlanMap = null;

		m_oFloorPlanMedia = null;
		m_oOutFloorPlanTableList = new ArrayList<OutFloorPlanTable>();
		
		resultFloorPlanMap = floorPlanMapJSONObject.optJSONObject("OutFloorPlanMap");
		if(resultFloorPlanMap == null)
			resultFloorPlanMap = floorPlanMapJSONObject;

		this.init();
		this.flrmId = resultFloorPlanMap.optInt("flrm_id");
		this.flrpId = resultFloorPlanMap.optInt("flrm_flrp_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultFloorPlanMap.optString("flrm_name_l"+i);
		
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultFloorPlanMap.optString("flrm_short_name_l"+i);
		this.seq = resultFloorPlanMap.optInt("flrm_seq");
		this.medi_id = resultFloorPlanMap.optInt("flrm_medi_id");
		this.width = resultFloorPlanMap.optInt("flrm_width");
		this.height = resultFloorPlanMap.optInt("flrm_height");
		this.status = resultFloorPlanMap.optString("flrm_status", OutFloorPlanMap.STATUS_ACTIVE);
		
	}
	
	//init object value
	public void init() {
		this.flrmId = 0;
		this.flrpId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(int i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(int i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.medi_id = 0;
		this.width = 0;
		this.height = 0;
		this.status = OutFloorPlanMap.STATUS_ACTIVE;
		
		this.m_oFloorPlanMedia = null;
		
		if(this.m_oOutFloorPlanTableList == null)
			m_oOutFloorPlanTableList = new ArrayList<OutFloorPlanTable>();
		else
			m_oOutFloorPlanTableList.clear();
	}
	
	public void addTable(OutFloorPlanTable oOutFloorPlanTable){	
		m_oOutFloorPlanTableList.add(oOutFloorPlanTable);
	}
	
	public void setName(int iIndex, String sName) {
		this.name[(iIndex-1)] = sName;
	}
	
	public void setShortName(int iIndex, String sShortName) {
		this.shortName[(iIndex-1)] = sShortName;
	}
	
	public int getTableCount(){
		return m_oOutFloorPlanTableList.size();
	}
	
	public int getMediaId() {
		return this.medi_id;
	}
	
	public String getMediaUrl() {
		if(this.m_oFloorPlanMedia == null)
			return "";
		else
			return this.m_oFloorPlanMedia.getUrl();
	}
	
	public OutFloorPlanTable getTable(int iIndex){
		return m_oOutFloorPlanTableList.get(iIndex);
	}
	
	public void setFloorPlanMapId(int iId){
		this.flrmId = iId;
	}
	
	public void setMediaId(int iMediaId) {
		this.medi_id = iMediaId;
	}
	
	public void setFloorPlanMedia(MedMedia oMedia) {
		this.m_oFloorPlanMedia = oMedia;
	}
	
	public void setFloorPlanId(int iFloorPlanId) {
		this.flrpId = iFloorPlanId;
	}
	
	public void setSeq(int iSeq) {
		this.seq = iSeq;
	}
	
	public void setWidth(int iWidth) {
		this.width = iWidth;
	}
	
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	public void setHeight(int iHeight) {
		this.height = iHeight;
	}
	
	public int getFloorPlanMapId(){
		return this.flrmId;
	}
	
	public int getFloorPlanId() {
		return this.flrpId;
	}
	
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	public String[] getName() {
		return this.name;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getSeq() {
		return this.seq;
	}
	
	public String getStatus() {
		return this.status;
	}
}
