//Database: pos_check_discounts_items - Sales discounts applied on item
package om;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;

public class PosCheckDiscountItem {
	private String cditId;
	private int oletId;
	private String cdisId;
	private String citmId;
	private int cdisSeq;
	private BigDecimal total;
	private BigDecimal roundTotal;
	private String status;
	
	// corresponding item indicator
	private int iItemSeat;
	private int iItemSeq;
	
	// discount breakdown on tax / sc value
	BigDecimal dRoundDiscBase;
	BigDecimal dDiscBase;
	BigDecimal dDiscOnSc[];
	BigDecimal dRoundDiscOnSc[];
	BigDecimal dDiscOnTax[];
	BigDecimal dRoundDiscOnTax[];
	
	private boolean bModified;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosCheckDiscountItem () {
		this.init();
	}
	
	//init object with another PosCheckDiscountItem
	public PosCheckDiscountItem (PosCheckDiscountItem oPosCheckDiscountItem) {
		this.init();
		
		this.cditId = oPosCheckDiscountItem.cditId;
		this.oletId = oPosCheckDiscountItem.oletId;
		this.cdisId = oPosCheckDiscountItem.cdisId;
		this.citmId = oPosCheckDiscountItem.citmId;
		this.cdisSeq = oPosCheckDiscountItem.cdisSeq;
		this.total = new BigDecimal(oPosCheckDiscountItem.total.toPlainString());
		this.roundTotal = new BigDecimal(oPosCheckDiscountItem.roundTotal.toPlainString());
		this.status = PosCheckDiscountItem.STATUS_ACTIVE;
	}
	
	//init object with JSON Object
	public PosCheckDiscountItem (JSONObject oChkDiscItemJSONObject) {
		readDataFromJson(oChkDiscItemJSONObject);
	}
	
	//init object from database by cdis_id
	public PosCheckDiscountItem (String sCdisId) {
		this.init();
		
		this.cdisId = sCdisId;
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
		this.cditId = resultCheckDiscountItem.optString("cdit_id");
		this.oletId = resultCheckDiscountItem.optInt("cdit_olet_id");
		this.cdisId = resultCheckDiscountItem.optString("cdit_cdis_id");
		this.citmId = resultCheckDiscountItem.optString("cdit_citm_id");
		this.cdisSeq = resultCheckDiscountItem.optInt("cdis_seq");
		this.total = new BigDecimal(resultCheckDiscountItem.optString("cdit_total", "0.0"));
		this.status = resultCheckDiscountItem.optString("cdit_status", "");
		this.roundTotal = new BigDecimal(resultCheckDiscountItem.optString("cdit_round_total", "0.0"));
		
		dRoundDiscOnTax = new BigDecimal[25];
		for(int i=1; i<=25; i++)
			dRoundDiscOnTax[i-1] = BigDecimal.ZERO;
		
		dRoundDiscOnSc = new BigDecimal[5];
		for(int i=1; i<=5; i++)
			dRoundDiscOnSc[i-1] = BigDecimal.ZERO;
	}

	//construct the save request JSON
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("cdit_id", this.cditId);
			addSaveJSONObject.put("cdit_olet_id", this.oletId);
			addSaveJSONObject.put("cdit_cdis_id", this.cdisId);
			addSaveJSONObject.put("cdit_citm_id", this.citmId);
			addSaveJSONObject.put("cdit_total", this.total);
			addSaveJSONObject.put("cdit_round_total", this.roundTotal);
			addSaveJSONObject.put("cdit_status", this.status);
			addSaveJSONObject.put("cdis_seq", this.cdisSeq);
			
			this.bModified = true;
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	//construct the save request JSON
	protected JSONArray constructMultipleAddSaveJSON(List<PosCheckDiscountItem> oCheckDiscountItems, boolean bUpdate) {
		JSONArray addSaveJSONArray = new JSONArray();
		
		for(PosCheckDiscountItem oTemp : oCheckDiscountItems) {
			JSONObject oTempJSON = new JSONObject();
			
			try {
				if(bUpdate)
					oTempJSON.put("cdit_id", oTemp.cditId);
				oTempJSON.put("cdit_olet_id", oTemp.oletId);
				oTempJSON.put("cdit_cdis_id", oTemp.cdisId);
				oTempJSON.put("cdit_citm_id", oTemp.citmId);
				oTempJSON.put("cdit_total", oTemp.total);
				oTempJSON.put("cdit_round_total", oTemp.roundTotal);
				oTempJSON.put("cdit_status", oTemp.status);
				oTempJSON.put("cdis_seq", oTemp.cdisSeq);
			}catch(JSONException jsone) {
				jsone.printStackTrace();
			}
			addSaveJSONArray.put(oTempJSON);
		}
		
		return addSaveJSONArray;
	}
	
	//read data from database by cdis_id
	public void readById (String sCdisId) {
		this.cdisId = sCdisId;
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
		this.cditId = "";
		this.oletId = 0;
		this.cdisId = "";
		this.citmId = "";
		this.cdisSeq = -1;
		this.total = BigDecimal.ZERO;
		this.status = PosCheckDiscountItem.STATUS_ACTIVE;
		this.roundTotal = BigDecimal.ZERO;
		this.bModified = false;
		
		this.iItemSeat = 0;
		this.iItemSeq = 0;
		this.dRoundDiscBase = BigDecimal.ZERO;
		this.dDiscBase = BigDecimal.ZERO;
		this.dDiscOnSc = new BigDecimal[5];
		this.dRoundDiscOnSc = new BigDecimal[5];
		for(int i=1; i<=5; i++) {
			this.dDiscOnSc[i-1] = BigDecimal.ZERO;
			this.dRoundDiscOnSc[i-1] = BigDecimal.ZERO;
		}
		this.dDiscOnTax = new BigDecimal[25];
		this.dRoundDiscOnTax = new BigDecimal[25];
		for(int i=1; i<=25; i++) {
			this.dDiscOnTax[i-1] = BigDecimal.ZERO;
			this.dRoundDiscOnTax[i-1] = BigDecimal.ZERO;
		}
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
	public void setCditId(String sCditId) {
		this.cditId = sCditId;
	}
	
	//set outlet id
	public void setOutletId(int iOutletId) {
		this.oletId = iOutletId;
	}
	
	//set discount id
	public void setCdisId(String sCdisId) {
		this.cdisId = sCdisId;
	}
	
	//set item id
	public void setCitmId(String sCitmId) {
		this.citmId = sCitmId;
	}
	
	//set total
	public void setTotal(BigDecimal dTotal) {
		this.total = dTotal;
	}
	
	//set round total
	public void setRoundTotal(BigDecimal dRoundTotal) {
		this.roundTotal = dRoundTotal;
	}
	
	//set status
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	public void setCdisSeq(int iSeq) {
		this.cdisSeq = iSeq;
	}
	
	//set item seat and seq
	public void setItemSeatSeq(int iSeat, int iSeq) {
		this.iItemSeat = iSeat;
		this.iItemSeq = iSeq;
	}
	
	//set round discount base
	public void setRoundDiscBase(BigDecimal dValue) {
		this.dRoundDiscBase = dValue;
	}
	
	//set discount base
	public void setDiscBase(BigDecimal dValue) {
		this.dDiscBase = dValue;
	}
	
	//set discount on sc
	//iIndex = 0, set all as provided value
	public void setDiscOnSc(boolean bRound, int iIndex, BigDecimal dValue) {
		BigDecimal[] dTargetDiscOnSc = this.dRoundDiscOnSc;
		if(!bRound)
			dTargetDiscOnSc = this.dDiscOnSc;
		
		if(iIndex == 0) {
			for(int i=1; i<=5; i++)
				dTargetDiscOnSc[i-1] = dValue;
		}else
			dTargetDiscOnSc[iIndex-1] = dValue;
	}
	
	//set discount on tax
	public void setDiscOnTax(boolean bRound, int iIndex, BigDecimal dValue) {
		BigDecimal[] dTargetDiscOnTax = this.dRoundDiscOnTax;
		if(!bRound)
			dTargetDiscOnTax = this.dDiscOnTax;
		
		if(iIndex == 0) {
			for(int i=1; i<=25; i++)
				dTargetDiscOnTax[i-1] = dValue;
		}else
			dTargetDiscOnTax[iIndex-1] = dValue;
	}
	
	//get cditId
	public String getCditId() {
		return this.cditId;
	}
	
	public int getOutletId() {
		return this.oletId;
	}

	public String getCdisId() {
		return this.cdisId;
	}
	
	public String getCitmId() {
		return this.citmId;
	}
	
	public int getCdisSeq() {
		return this.cdisSeq;
	}
	
	public BigDecimal getTotal() {
		return this.total;
	}
	
	public BigDecimal getRoundTotal() {
		return this.roundTotal;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public int getItemSeat() {
		return this.iItemSeat;
	}
	
	public int getItemSeq() {
		return this.iItemSeq;
	}
	
	public BigDecimal getRoundDiscBase() {
		return this.dRoundDiscBase;
	}
	
	public BigDecimal getDiscBase() {
		return this.dDiscBase;
	}
	
	public BigDecimal getDiscOnSc(boolean bRound, int iIndex) {
		BigDecimal[] dTargetDiscOnSc = this.dRoundDiscOnSc;
		if(!bRound)
			dTargetDiscOnSc = this.dDiscOnSc;
		
		if(iIndex == 0) {
			BigDecimal dDiscOnScTotal = BigDecimal.ZERO;
			for(int i=1; i<=5; i++)
				dDiscOnScTotal = dDiscOnScTotal.add(dTargetDiscOnSc[i-1]);
			return dDiscOnScTotal;
		}else
			return dTargetDiscOnSc[iIndex-1];
	}
	
	public BigDecimal getDiscOnTax(boolean bRound, int iIndex) {
		BigDecimal[] dTargetDiscOnTax = this.dRoundDiscOnTax;
		if(!bRound)
			dTargetDiscOnTax = this.dDiscOnTax;
		
		if(iIndex == 0){
			BigDecimal dDiscOnTaxTotal = BigDecimal.ZERO;
			for(int i=1; i<=5; i++)
				dDiscOnTaxTotal = dDiscOnTaxTotal.add(dTargetDiscOnTax[i-1]);
			return dDiscOnTaxTotal;
		}else
			return dTargetDiscOnTax[iIndex-1];
	}
}
