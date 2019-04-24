package app.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosMixAndMatchRule {
	private int mamrId;
	private int shopId;
	private int oletId;
	private String[] name;
	private int maxItemIndex;
	private int seq;
	private String status;
	
	private ArrayList<PosMixAndMatchItem> m_oMixAndMatchItemList;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_SUSPENDED = "s";
	public static String STATUS_DELETED = "d";
	
	//init with initialized value
	public PosMixAndMatchRule() {
		this.init();
	}
	
	public PosMixAndMatchRule(JSONObject oJSONObject) {
		readDataFromJson(oJSONObject);
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject mixAndMatchRuleJSONObject) {
		JSONObject tempJSONObject = null;
		JSONArray tempJSONArray = null;
		PosMixAndMatchItem oPosMixAndMatchItem;
		int i;
		
		tempJSONObject = mixAndMatchRuleJSONObject.optJSONObject("PosMixAndMatchRule");
		if(tempJSONObject == null)
			tempJSONObject = mixAndMatchRuleJSONObject;
			
		this.init();
		this.mamrId = tempJSONObject.optInt("mamr_id");
		this.shopId = tempJSONObject.optInt("mamr_shop_id");
		this.oletId = tempJSONObject.optInt("mamr_olet_id");
		for(i=1; i<=5; i++) 
			this.name[(i-1)] = tempJSONObject.optString("mamr_name_l"+i);
		this.maxItemIndex = tempJSONObject.optInt("mamr_max_item_index");
		this.seq = tempJSONObject.optInt("mamr_seq");
		this.status = tempJSONObject.optString("mamr_status", PosMixAndMatchRule.STATUS_ACTIVE);
		
		//check whether item record exist
		tempJSONArray = mixAndMatchRuleJSONObject.optJSONArray("PosMixAndMatchItem");
		if (tempJSONArray != null) {
			for(i=0; i<tempJSONArray.length(); i++) {
				JSONObject oMixAndMatchItem = tempJSONArray.optJSONObject(i);
				if(oMixAndMatchItem != null) {
					oPosMixAndMatchItem = new PosMixAndMatchItem(oMixAndMatchItem);
					this.m_oMixAndMatchItemList.add(oPosMixAndMatchItem);
				}
			}
		}
	}
	
	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bUpdate = true;
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bUpdate = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("PosMixAndMatchRule")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("PosMixAndMatchRule")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("PosMixAndMatchRule");
			readDataFromJson(tempJSONObject);
		}
		
		return bUpdate;
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray oJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("mixAndMatchRules")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("mixAndMatchRules"))
				return null;
			
			oJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("mixAndMatchRules");
		}
		
		return oJSONArray;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.mamrId = 0;
		this.shopId = 0;
		this.oletId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		this.maxItemIndex = 0;
		this.seq = 0;
		this.status = PosMixAndMatchRule.STATUS_ACTIVE;
		
		if(this.m_oMixAndMatchItemList == null)
			this.m_oMixAndMatchItemList = new ArrayList<PosMixAndMatchItem>();
		else
			this.m_oMixAndMatchItemList.clear();
	}
	
	//read data from database 
	public JSONArray readAll(int iShopId, int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		 
		try {
			requestJSONObject.put("shopId", iShopId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("recursive", 1);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllMixAndMatchRules", requestJSONObject.toString());

		return responseJSONArray;
	}
	
	public int getRuleId() {
		return this.mamrId;
	}
	
	public int getShopId() {
		return this.shopId;
	}
	
	public int getOletId () {
		return this.oletId;
	}
	
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	public int getMaxItemIndex() {
		return this.maxItemIndex;
	}
	
	public int getSequence() {
		return this.seq;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public ArrayList<PosMixAndMatchItem> getRuleItemList(){
		return this.m_oMixAndMatchItemList;
	}
}
