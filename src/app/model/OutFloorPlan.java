package app.model;

import java.util.ArrayList;

import org.json.JSONObject;

public class OutFloorPlan {
	private int flrpId;
	private int shopId;
	private int outletId;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String status;
	
	private ArrayList<OutFloorPlanMap> m_oOutFloorPlanMapList;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init with initialized value
	public OutFloorPlan() {
		this.init();
	}
	
	public OutFloorPlan(JSONObject floorPlanJSONObject) {
		int i = 0;
		JSONObject resultFloorPlan = null;

		this.init();
		
		resultFloorPlan = floorPlanJSONObject.optJSONObject("OutFloorPlan");
		if(resultFloorPlan == null)
			resultFloorPlan = floorPlanJSONObject;

		this.flrpId = resultFloorPlan.optInt("flrp_id");
		this.shopId = resultFloorPlan.optInt("flrp_shop_id");
		this.outletId = resultFloorPlan.optInt("flrp_olet_id");
		
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultFloorPlan.optString("flrp_name_l"+i);
		
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultFloorPlan.optString("flrp_short_name_l"+i);
		
		this.seq = resultFloorPlan.optInt("flrp_seq");
		this.status = resultFloorPlan.optString("flrp_status", OutFloorPlan.STATUS_ACTIVE);
	}
	
	//init object value
	public void init() {
		int i=0;
		
		this.flrpId = 0;
		this.shopId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.status = OutFloorPlan.STATUS_ACTIVE;
		
		if(this.m_oOutFloorPlanMapList == null)
			m_oOutFloorPlanMapList = new ArrayList<OutFloorPlanMap>();
		else
			this.m_oOutFloorPlanMapList.clear();
	}
	
	public void addMap(OutFloorPlanMap oOutFloorPlanMap){
		m_oOutFloorPlanMapList.add(oOutFloorPlanMap);
	}
	
	public int getMapCount(){
		return m_oOutFloorPlanMapList.size();
	}
	
	public OutFloorPlanMap getMap(int iIndex){
		return m_oOutFloorPlanMapList.get(iIndex);
	}
	
	protected void setFloorPlanId(int iId){
		this.flrpId = iId;
	}
	
	public void setShopId(int iShopId) {
		this.shopId = iShopId;
	}
	
	public void setOutletId(int iOutletId) {
		this.outletId = iOutletId;
	}
	
	public void setName(int iIndex, String sName) {
		this.name[(iIndex-1)] = sName;
	}
	
	public void setShortName(int iIndex, String sShortName) {
		this.shortName[(iIndex-1)] = sShortName;
	}
	
	public void setSeq(int iSeq) {
		this.seq = iSeq;
	}
	
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	protected int getFloorPlanId(){
		return this.flrpId;
	}
	
	public int getShopId() {
		return this.shopId;
	}
	
	public int getOutletId() {
		return this.outletId;
	}
	
	public int getSeq() {
		return this.seq;
	}
	
	public String getStatus() {
		return this.status;
	}
}
