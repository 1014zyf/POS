package app.model;

import org.json.JSONObject;

public class MenuItemDeptGroupLookup {
	private int idluId;
	private int idgpId;
	private int idepId;
	
	//init object with initialize value
	public MenuItemDeptGroupLookup() {
		this.init();
	}
	
	//init object with JSONObject 
	public MenuItemDeptGroupLookup(JSONObject deptGroupLookupJSONObject) {
		this.readDataFromJson(deptGroupLookupJSONObject);
	}
	
	//init object
	public void init() {
		this.idluId = 0;
		this.idgpId = 0;
		this.idepId = 0;
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject deptGroupLookupJSONObject) {
		JSONObject tempJSONObject = null;
		
		tempJSONObject = deptGroupLookupJSONObject.optJSONObject("MenuItemDeptGroupLookup");
		if(tempJSONObject == null)
			tempJSONObject = deptGroupLookupJSONObject;
			
		this.init();
		this.idluId = tempJSONObject.optInt("idlu_id");
		this.idgpId = tempJSONObject.optInt("idlu_idgp_id");
		this.idepId = tempJSONObject.optInt("idlu_idep_id");
	}
	
	public int getDeptId() {
		return this.idepId;
	}
}
