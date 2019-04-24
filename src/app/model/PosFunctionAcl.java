//Database: pos_function_acls - Access control lists for the functions, users and outlets
package app.model;

import org.json.JSONObject;

public class PosFunctionAcl {
	private int faclId;
	private int ugrpId;
	private int userId;
	private int ogrpId;
	private int oletId;
	private int fgrpId;
	private int funcId;
	private String allow;
	
	// allow
	public static String ALLOW_NO = "";
	public static String ALLOW_YES = "y";
	
	//init object with initialize value
	public PosFunctionAcl () {
		this.init();
	}

	//init object from database by facl_id
	public PosFunctionAcl (int iFaclId) {
		this.init();
		
		this.faclId = iFaclId;
	}
	
	//init object with JSON object
	public PosFunctionAcl(JSONObject funcAclJSONObject) {
		JSONObject resultFunctionAclJSONObject = null;
		
		resultFunctionAclJSONObject = funcAclJSONObject.optJSONObject("PosFunctionAcl");
		if(resultFunctionAclJSONObject == null)
			resultFunctionAclJSONObject = funcAclJSONObject;
		
		this.init();
		
		this.faclId = resultFunctionAclJSONObject.optInt("facl_id");
		this.ugrpId = resultFunctionAclJSONObject.optInt("facl_ugrp_id");
		this.userId = resultFunctionAclJSONObject.optInt("facl_user_id");
		this.ogrpId = resultFunctionAclJSONObject.optInt("facl_ogrp_id");
		this.oletId = resultFunctionAclJSONObject.optInt("facl_olet_id");
		this.fgrpId = resultFunctionAclJSONObject.optInt("facl_fgrp_id");
		this.funcId = resultFunctionAclJSONObject.optInt("facl_func_id");
		this.allow = resultFunctionAclJSONObject.optString("facl_allow", PosFunctionAcl.ALLOW_NO);
	}
	
	// init value
	public void init() {
		this.faclId = 0;
		this.ugrpId = 0;
		this.userId = 0;
		this.ogrpId = 0;
		this.oletId = 0;
		this.fgrpId = 0;
		this.funcId = 0;
		this.allow = PosFunctionAcl.ALLOW_NO;
	}
	
	//read data from database by facl_id
	public void readById (int iFaclId) {
		this.faclId = iFaclId;
	}
	
	//add new function acl record to database
	public boolean add() {
		return true;
	}
	
	//update function acl record to database
	public boolean update() {
		return true;
	}
	
	//get faclId
	protected int getFaclId() {
		return this.faclId;
	}
	
	//get ugrp id
	protected int getUgrpId() {
		return this.ugrpId;
	}
	
	//get user id
	protected int getUserId() {
		return this.userId;
	}
	
	//get ogrp id
	protected int getOgrpId() {
		return this.ogrpId;
	}
	
	//get olet id
	protected int getOletId() {
		return this.oletId;
	}
	
	//get fgrp id
	protected int getFgrpId() {
		return this.fgrpId;
	}
	
	//get func id
	protected int getFuncId() {
		return this.funcId;
	}
	
	//get allow
	public String getAllow() {
		return this.allow;
	}
}
