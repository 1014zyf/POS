//Database: pos_payment_groups - Payment method grouping
package app.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosPaymentGroup {
	private int paygId;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String status;
	private List<PosPaymentMethod> paymentMethods;
	
	// status
	public static String STATUS_ACTIVE= "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosPaymentGroup () {
		this.init();
	}
	
	//Init object from database by payg_id
	public PosPaymentGroup (int iPaymentGroupId) {
		JSONObject requestJSONObject = new JSONObject();
		
		this.init();
		try {
			requestJSONObject.put("paymentGroupId", iPaymentGroupId);
			requestJSONObject.put("recursive", 0);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		this.readDataFromApi("gm", "pos", "getPaymentGroupById", requestJSONObject.toString());
	}

	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bResult = true;
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bResult = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("payment_group")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("payment_group")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("payment_group");
			if(tempJSONObject.isNull("PosPaymentGroup")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject paymentGroupJSONObject) {
		JSONObject resultPaymentGroup = null;
		int i;
		
		resultPaymentGroup = paymentGroupJSONObject.optJSONObject("PosPaymentGroup");
		if(resultPaymentGroup == null)
			resultPaymentGroup = paymentGroupJSONObject;
			
		this.init();

		this.paygId = resultPaymentGroup.optInt("payg_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultPaymentGroup.optString("payg_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultPaymentGroup.optString("payg_short_name_l"+i);
		this.seq = resultPaymentGroup.optInt("payg_seq");
		this.status = resultPaymentGroup.optString("payg_status", PosPaymentGroup.STATUS_ACTIVE);
		
		//get the corresponding payment method
		JSONArray tempJSONArray = paymentGroupJSONObject.optJSONArray("PosPaymentMethod");
		if(tempJSONArray != null) {
			for(i=0; i<tempJSONArray.length(); i++) {
				JSONObject paymentMethodJSONObject = tempJSONArray.optJSONObject(i);
				if(paymentMethodJSONObject != null) {
					PosPaymentMethod oPaymentMethod = new PosPaymentMethod(paymentMethodJSONObject);
					this.paymentMethods.add(oPaymentMethod);
				}
			}
		}
	}
	
	//construct the save request JSON
	private JSONObject constructAddSaveJSON(boolean bUpdate) {
		int i=0;
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("payg_id", this.paygId);
			for(i=1; i<=5; i++)
				addSaveJSONObject.put("payg_name_l"+i, this.name[(i-1)]);
			for(i=1; i<=5; i++)
				addSaveJSONObject.put("payg_short_name_l"+i, this.shortName[(i-1)]);
			addSaveJSONObject.put("payg_seq", this.seq);
			addSaveJSONObject.put("payg_status", this.status);

		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.paygId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.status = PosPaymentGroup.STATUS_ACTIVE;
		
		if(this.paymentMethods == null)
			this.paymentMethods = new ArrayList<PosPaymentMethod>();
		else
			this.paymentMethods.clear();
	}
	
	//read data from database by payg_id
	public boolean readById(int iPaymentGroupId) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("paymentGroupId", iPaymentGroupId);
			requestJSONObject.put("recursive", 1);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "getPaymentGroupById", requestJSONObject.toString());
		
	}
		
	//add or update a check item to database
	public boolean addUpdate(boolean bUpdate) {
		JSONObject requestJSONObject = new JSONObject();
		
		requestJSONObject = this.constructAddSaveJSON(bUpdate);
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "savePaymentGroup", requestJSONObject.toString(), false))
			return false;
		else
			return true;
	}
	
	//get paygId
	public int getPaygId() {
		return this.paygId;
	}
	
	//get name with index
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get short name with index
	public String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get sequence
	public int getSequence() {
		return this.seq;
	}
}
