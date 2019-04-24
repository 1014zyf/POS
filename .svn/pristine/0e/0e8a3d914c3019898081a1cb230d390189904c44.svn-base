//Database: pos_check_discounts_items - Sales discounts applied on item
package app.model;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosCheckDiscountItem {
	private int cditId;
	private int cdisId;
	private int citmId;
	private BigDecimal total;
	private BigDecimal roundTotal;
	
	private int checkDiscountIndex;
	private boolean bModified;
	
	//init object with initialize value
	public PosCheckDiscountItem () {
		this.init();
	}
	
	//init object with another PosCheckDiscountItem
	public PosCheckDiscountItem (PosCheckDiscountItem oPosCheckDiscountItem) {
		this.init();
		
		this.cditId = oPosCheckDiscountItem.cditId;
		this.cdisId = oPosCheckDiscountItem.cdisId;
		this.citmId = oPosCheckDiscountItem.citmId;
		this.total = new BigDecimal(oPosCheckDiscountItem.total.toPlainString());
		this.roundTotal = new BigDecimal(oPosCheckDiscountItem.roundTotal.toPlainString());
		this.checkDiscountIndex = oPosCheckDiscountItem.checkDiscountIndex;
		
	}
	
	//init object with JSON Object
	public PosCheckDiscountItem (JSONObject oChkDiscItemJSONObject) {
		readDataFromJson(oChkDiscItemJSONObject);
	}
	
	//init object from database by cdis_id
	public PosCheckDiscountItem (int iCdisId) {
		this.init();
		
		this.cdisId = iCdisId;
	}
	
	public PosCheckDiscountItem (int iCdisId, int iCitmId) {
		JSONObject requestJSONObject = new JSONObject();

		this.init();
		
		try {
			requestJSONObject.put("checkDiscountId", Integer.toString(iCdisId));
			requestJSONObject.put("checkItemId", Integer.toString(iCitmId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		this.readDataFromApi("gm", "pos", "getCheckDiscountItemByDiscIdItemId", requestJSONObject.toString());
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("checkDiscountItem")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("checkDiscountItem")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("checkDiscountItem");
			if (tempJSONObject.isNull("PosCheckDiscountItem")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
			bResult = true;
		}
		
		return bResult;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject oChkDiscItemJSONObject) {
		JSONObject resultCheckDiscountItem = null;
		resultCheckDiscountItem = oChkDiscItemJSONObject.optJSONObject("PosCheckDiscountItem");
		if(resultCheckDiscountItem == null)
			resultCheckDiscountItem = oChkDiscItemJSONObject;
		
		this.init();
		this.cditId = resultCheckDiscountItem.optInt("cdit_id");
		this.cdisId = resultCheckDiscountItem.optInt("cdit_cdis_id");
		this.citmId = resultCheckDiscountItem.optInt("cdit_citm_id");
		this.total = new BigDecimal(resultCheckDiscountItem.optString("cdit_total", "0.0"));
		this.roundTotal = new BigDecimal(resultCheckDiscountItem.optString("cdit_round_total", "0.0"));
	}

	//construct the save request JSON
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("cdit_id", this.cditId);
			addSaveJSONObject.put("cdit_cdis_id", this.cdisId);
			addSaveJSONObject.put("cdit_citm_id", this.citmId);
			addSaveJSONObject.put("cdit_total", this.total);
			addSaveJSONObject.put("cdit_round_total", this.roundTotal);
			if(checkDiscountIndex >= 0)
				addSaveJSONObject.put("checkDiscountIndex", checkDiscountIndex);
			
			this.bModified = true;
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	//read data from database by cdis_id
	public void readById (int iCdisId) {
		this.cdisId = iCdisId;
	}

	//read data from database by cdit_id
	public boolean deleteById() {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("id", this.cditId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "deleteCheckDiscountItemById", requestJSONObject.toString());

	}
	
	//read data from database by otbl_id
	public boolean deleteByIds(List<Integer> oDiscountItemIdList) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray discountItemIdsJSONArray = new JSONArray();
		
		if(oDiscountItemIdList == null || oDiscountItemIdList.size() == 0)
			return false;
		
		try {
			for(Integer iCditId: oDiscountItemIdList)
				discountItemIdsJSONArray.put(iCditId.intValue());
			
			requestJSONObject.put("ids", discountItemIdsJSONArray);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "deleteCheckDiscountItemByIds", requestJSONObject.toString());

	}

	// init value
	public void init() {
		this.cditId = 0;
		this.cdisId = 0;
		this.citmId = 0;
		this.total = BigDecimal.ZERO;
		this.roundTotal = BigDecimal.ZERO;
		this.checkDiscountIndex = -1;
		this.bModified = false;
	}
	
	//add new check discount item to database
	public boolean add() {
		return true;
				
	}
	
	//update check discount item to database
	public boolean update() {
		return true;
	}
	
	//set discount item id
	public void setCditId(int iCditId) {
		this.cditId = iCditId;
	}
	
	//set discount id
	public void setCdisId(int iCdisId) {
		this.cdisId = iCdisId;
	}
	
	//set item id
	public void setCitmId(int iCitmId) {
		this.citmId = iCitmId;
	}
	
	//set total
	public void setTotal(BigDecimal dTotal) {
		this.total = dTotal;
	}
	
	//set round total
	public void setRoundTotal(BigDecimal dRoundTotal) {
		this.roundTotal = dRoundTotal;
	}
	
	//set discount index
	public void setCheckDiscountIndex(int iDiscountIndex) {
		this.checkDiscountIndex = iDiscountIndex;
	}
	
	//get cditId
	public int getCditId() {
		return this.cditId;
	}

	public int getCdisId() {
		return this.cdisId;
	}
	
	public int getCitmId() {
		return this.citmId;
	}
	
	public BigDecimal getTotal() {
		return this.total;
	}
	
	public BigDecimal getRoundTotal() {
		return this.roundTotal;
	}
	
}
