//Database: pos_payment_classes - Payment method classes
package app.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosPaymentClass {
	private int paycId;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String status;
	private List<PosPaymentMethod> paymentMethods;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosPaymentClass () {
		this.init();
	}
	
	//Init object from database by payc_id
	public PosPaymentClass (int iPaymentClassId) {
		JSONObject requestJSONObject = new JSONObject();
		
		this.init();
		
		try {
			requestJSONObject.put("paymentClassId", iPaymentClassId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		this.readDataFromApi("gm", "pos", "getPaymentClassById", requestJSONObject.toString());
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("payment_class")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("payment_class")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("payment_class");
			if(tempJSONObject.isNull("PosPaymentClass")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject paymentClassJSONObject) {
		JSONObject resultPaymentClass = null;
		int i;
		
		resultPaymentClass = paymentClassJSONObject.optJSONObject("PosPaymentClass");
		if(resultPaymentClass == null)
			resultPaymentClass = paymentClassJSONObject;
			
		this.init();

		this.paycId = resultPaymentClass.optInt("payc_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultPaymentClass.optString("payc_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultPaymentClass.optString("payc_short_name_l"+i);
		this.seq = resultPaymentClass.optInt("payc_seq");
		this.status = resultPaymentClass.optString("payc_status", PosPaymentClass.STATUS_ACTIVE);
		
		//get corresponding payment method
		JSONArray tempJSONArray = paymentClassJSONObject.optJSONArray("PosPaymentMethod");
		if(tempJSONArray != null) {
			for(i=0; i<tempJSONArray.length(); i++) {
				JSONObject tempJSONObject = tempJSONArray.optJSONObject(i);
				if(tempJSONObject != null) {
					PosPaymentMethod paymentMethod = new PosPaymentMethod(tempJSONObject);
					this.paymentMethods.add(paymentMethod);
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
				addSaveJSONObject.put("payc_id", this.paycId);
			for(i=1; i<=5; i++)
				addSaveJSONObject.put("payc_name_l"+i, this.name[(i-1)]);
			for(i=1; i<=5; i++)
				addSaveJSONObject.put("payc_short_name_l"+i, this.shortName[(i-1)]);
			addSaveJSONObject.put("payc_seq", this.seq);
			addSaveJSONObject.put("payc_status", this.status);

		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.paycId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.status = PosPaymentClass.STATUS_ACTIVE;
		
		if(this.paymentMethods == null)
			this.paymentMethods = new ArrayList<PosPaymentMethod>();
		else
			this.paymentMethods.clear();
	}
	
	//read data from database by payc_id
	public boolean readById(int iPaymentClassId) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("paymentClassId", iPaymentClassId);
			requestJSONObject.put("recursive", 1);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		this.readDataFromApi("gm", "pos", "getPaymentClassById", requestJSONObject.toString());
		
		return true;
	}
	
	//add or update a check item to database
	public boolean addUpdate(boolean bUpdate) {
		JSONObject requestJSONObject = new JSONObject();
		
		requestJSONObject = this.constructAddSaveJSON(bUpdate);
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "savePaymentClass", requestJSONObject.toString(), false))
			return false;
		else
			return true;
	}
	
	//get paycId
	public int getPaycId() {
		return this.paycId;
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
