package om;

import java.math.BigDecimal;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PreorderCheck {
	private String ordrId;
	private int shopId;
	private int oletId;
	private int statId;
	private int refno;
	private String phone;
	private String source;
	private int guests;
	private int table;
	private String tableExt;
	private String orderingType;
	private String member;
	private String remark;
	private BigDecimal total;
	private int itemCount;
	private int printCount;
	private int readCount;
	private DateTime createTime;
	private DateTime modifyTime;
	private DateTime readTime;
	private DateTime expiryTime;	
	public String status;
	
	static public String ORDERING_TYPE_DINE_IN = "";
	static public String ORDERING_TYPE_TAKEOUT = "t";
	
	//init with initialized value
	public PreorderCheck() {
		this.ordrId = "";
		this.shopId = 0;
		this.oletId = 0;
		this.statId = 0;
		this.refno = 0;
		this.phone = "";
		this.source = "";
		this.guests = 0;
		this.table = 0;
		this.tableExt = "";
		this.orderingType = "";
		this.member = "";
		this.remark = null;
		this.total = BigDecimal.ZERO;
		this.itemCount = 0;
		this.printCount = 0;
		this.readCount = 0;
		this.createTime = null;
		this.modifyTime = null;
		this.readTime = null;
		this.expiryTime = null;
		this.status = "";
	}
	
	// Retrieve preorder
	public JSONObject retrievePreorderByRefNo(int iOutletId, String sRefno) {
		JSONObject requestJSONObject = new JSONObject();
		JSONObject orderJSONObject = new JSONObject();
		JSONObject responseJSONObject = null;
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("orderRefno", sRefno);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "preorder", "getOrderByOutletRefno", requestJSONObject.toString(), true))
			return null;
		else {
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("order"))
				// Pre-order is not found
				return null;
			
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("order"))
				return null;
			
			orderJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("order");
			responseJSONObject = orderJSONObject;

			return responseJSONObject;
		}
	}
	
	// Retrieve preorder
	public JSONObject retrievePreorderByOrderId(int iOutletId, int iOrderId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONObject orderJSONObject = new JSONObject();
		JSONObject responseJSONObject = null;
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("orderId", iOrderId);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "preorder", "getOrderById", requestJSONObject.toString(), true))
			return null;
		else {
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("order")) 	// Pre-order is not found
				return null;
			
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("order")) 
				return null;
			
			orderJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("order");
			responseJSONObject = orderJSONObject;

			return responseJSONObject;
		}
	}
	
	// Retrieve preorder by table number
	public JSONArray retrievePreorderByTable(int iOutletId, String sTableNo, String sTableExtension, int iMaxRecord, boolean bGetAlreadyReadRecord) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("tableNo", sTableNo);
			requestJSONObject.put("tableExt", sTableExtension);
			if(bGetAlreadyReadRecord)
				requestJSONObject.put("getAlreadyReadRecord", 1);
			else
				requestJSONObject.put("getAlreadyReadRecord", 0);
			if(iMaxRecord > 0)
				requestJSONObject.put("limit", iMaxRecord);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "preorder", "getOrderByOutletTable", requestJSONObject.toString(), true))
			return null;
		else {
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("orders"))
				return null;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("orders"))
				return null;
			
			responseJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("orders");
			
		}
		return responseJSONArray;
	}
	
	// Update preorder read count
	public boolean updatePreorderReadCount(int iOutletId, String sRefno) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("orderRefno", sRefno);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "preorder", "updateOrderReadCount", requestJSONObject.toString(), true))
			return false;
		else {
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("id"))
				return false;
			
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("id"))
				return false;
		}
		
		return true;
	}
	
	// Update all active record's status to purged
	public boolean purgeAllActiveOrders(int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("outletId", iOutletId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "preorder", "purgeOrdersByOutlet", requestJSONObject.toString(), true)) 
			return false;
		else {
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("result"))
				return false;
			
			if (OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("result"))
				return false;
			
			if (!OmWsClientGlobal.g_oWsClient.get().getResponse().optBoolean("result"))
				return false;
		}
		
		return true;
	}
}
