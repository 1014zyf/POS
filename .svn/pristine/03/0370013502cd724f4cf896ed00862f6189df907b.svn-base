//Database: pos_function_groups - POS function groups
package om;

import org.json.JSONObject;

public class PosFunctionGroup {
	private int fgrpId;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String status;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosFunctionGroup () {
		this.init();
	}
	
	//init object with JSONObject
	public PosFunctionGroup (JSONObject funcGrpJSONObject) {
		int i=0;
		
		this.init();
		
		this.fgrpId = funcGrpJSONObject.optInt("fgrp_id");
		
		for(i=1; i<=5; i++)
			this.name[(i-1)] = funcGrpJSONObject.optString("fgrp_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = funcGrpJSONObject.optString("fgrp_short_name_l"+i);
		this.seq = funcGrpJSONObject.optInt("fgrp_seq");
		this.status = funcGrpJSONObject.optString("fgrp_status", PosFunctionGroup.STATUS_ACTIVE);
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.fgrpId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.status = PosFunctionGroup.STATUS_ACTIVE;
	}
	
	//init object from database by fgrp_id
	public PosFunctionGroup (int iFgrpId) {
		this.fgrpId = iFgrpId;
	}
	
	//read data from database by fgrp_id
	public void readById (int iFgrpId) {
		this.fgrpId = iFgrpId;
	}
	
	//add new function group to database
	public boolean add() {
		return true;
	}
	
	//update function group to database
	public boolean update() {
		return true;
	}
	
	//get fgrpId
	protected int getFgrpId() {
		return this.fgrpId;
	}
	
	//get name by lang index
	protected String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get short name by lang index
	protected String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get seq
	protected int getSeq() {
		return this.seq;
	}
	
	//get status
	protected String getStatus() {
		return this.status;
	}
}
