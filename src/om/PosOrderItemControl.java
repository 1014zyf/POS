package om;

import java.math.BigDecimal;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;

public class PosOrderItemControl {
	private String oitcId;
	private int shopId;
	private int oletId;
	private int itemId;
	private int icatId;
	private int idepId;
	private String orderType;
	private String value;
	private String status;
	
	// item control type
	public static final String ORDER_CONTROL_TYPE_ITEM = "i";
	public static final String ORDER_CONTROL_TYPE_CATEGORY = "c";
	public static final String ORDER_CONTROL_TYPE_DEPARTMENT = "d";
	
	// init object with initialize value
	public PosOrderItemControl () {
		this.init();
	}
	
	// init value
	public void init() {
		this.oitcId = "";
		this.shopId = 0;
		this.oletId = 0;
		this.itemId = 0;
		this.icatId = 0;
		this.orderType = null;
		this.value = null;
	}
	
	//init object from JSONObject
	public PosOrderItemControl (JSONObject orderItemControlJSONObject) {
		this.readDataFromJson(orderItemControlJSONObject);
	}
	
/*
	// read a list of data from API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray orderItemAclJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("orderItemControls")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("orderItemControls")) {
				return null;
			}
			
			orderItemAclJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("orderItemControls");
		}
		
		return orderItemAclJSONArray;
	}
*/
	
	// real data from response JSON
	public void readDataFromJson(JSONObject orderItemControlJSONObject) {
		JSONObject resultOrderItemControl = null;
		
		resultOrderItemControl = orderItemControlJSONObject.optJSONObject("PosOrderItemControl");
		if(resultOrderItemControl == null)
			resultOrderItemControl = orderItemControlJSONObject;
		this.init();
		
		this.oitcId = resultOrderItemControl.optString("oitc_id");
		this.shopId = resultOrderItemControl.optInt("oitc_shop_id");
		this.oletId = resultOrderItemControl.optInt("oitc_olet_id");
		this.itemId = resultOrderItemControl.optInt("oitc_item_id");
		this.icatId = resultOrderItemControl.optInt("oitc_icat_id");
		this.orderType = resultOrderItemControl.optString("oitc_order_type", PosOrderItemControl.ORDER_CONTROL_TYPE_ITEM);
		this.value = resultOrderItemControl.optString("oitc_order_control", null);
	}
/*
	// read all order item acls
	public JSONArray readAll() {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try{
			requestJSONObject.put("recursive", -1);		
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getAllOrderItemControl", requestJSONObject.toString());
		return responseJSONArray;
	}
*/
	// get oitcId
	public String getOitcId() {
		return this.oitcId;
	}
		
	// get shop id
	public int getShopId() {
		return this.shopId;
	}
		
	// get olet id
	public int getOletId() {
		return this.oletId;
	}
		
	// get idep id
	public int getIdepId() {
		return this.idepId;
	}
	
	// get icat id
	public int getIcatId() {
		return this.icatId;
	}
	
	// get item id
	public int getItemId() {
		return this.itemId;
	}
		
	// get order type
	public String getOrderType() {
		return this.orderType;
	}
		
	// get order control
	protected String getOrderControl() {
		return this.value;
	}
		
	// get order max limit
	public BigDecimal getOrderMaxLimit(){
		BigDecimal dMaxQty = BigDecimal.ZERO;
		String sMaxQty = "";
		if(this.value != null){
			try {
				JSONObject oItemOrderLimitObject = new JSONObject(this.value);
				if(oItemOrderLimitObject.has("item_order_kiosk_max_limit"))
					sMaxQty = oItemOrderLimitObject.optString("item_order_kiosk_max_limit");
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
			if(!sMaxQty.isEmpty())
				dMaxQty = new BigDecimal(sMaxQty);
		}
		return dMaxQty;
	}
	
	//get status
	public String getStatus(){
		return this.status;
	}
}
