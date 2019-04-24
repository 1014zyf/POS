//Database: pos_function_group_lookup - Grouping of the functions
package om;

import org.json.JSONObject;

public class PosFunctionGroupLookup {
	private int fgluId;
	private int fgrpId;
	private int funcId;
	
	//init object with initialize value
	public PosFunctionGroupLookup () {
		this.fgluId = 0;
		this.fgrpId = 0;
		this.funcId = 0;
		
	}
	
	//init object with JSONObject
	public PosFunctionGroupLookup(JSONObject funcGrpLookupJSONObject) {
		this.fgluId = funcGrpLookupJSONObject.optInt("fglu_id");
		this.fgrpId = funcGrpLookupJSONObject.optInt("fglu_fgrp_id");
		this.funcId = funcGrpLookupJSONObject.optInt("fglu_func_id");
	}
	
	//init object from database by fglu_id
	public PosFunctionGroupLookup (int iFgluId) {
		this.fgluId = iFgluId;
	}
	
	//read data from database by fglu_id
	public void readById (int iFgluId) {
		this.fgluId = iFgluId;
	}
	
	//add new function group lookup to database
	public boolean add() {
		return true;
	}
	
	//update function group lookup to database
	public boolean update() {
		return true;
	}
	
	//get fgluId
	protected int getFgluId() {
		return this.fgluId;
	}
	
	//get fgrp id
	protected int getFgrpId() {
		return this.fgrpId;
	}
	
	//get func id
	protected int getFuncId() {
		return this.funcId;
	}
}
