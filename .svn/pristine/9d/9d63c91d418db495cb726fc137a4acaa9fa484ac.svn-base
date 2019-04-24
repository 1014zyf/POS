package om;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;

public class PosUserDrawerOwnership {
	private String udrwId;
	private int shopId;
	private int oletId;
	private int statId;
	private int userId;
	private int drawer;
	private String takeTime;
	private DateTime takeLocTime;
	
	public PosUserDrawerOwnership() {
		this.init();
	}
	
	//init object with JSON object
	public PosUserDrawerOwnership(JSONObject oUserDrawerOwnershipJSONObject) {
		// TODO Auto-generated constructor stub
		this.readDataFromJson(oUserDrawerOwnershipJSONObject);
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject userDrawerOwnershipJSONObject) {
		JSONObject resultUserDrawerOwnership = null;
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		resultUserDrawerOwnership = userDrawerOwnershipJSONObject.optJSONObject("PosUserDrawerOwnership");
		if(resultUserDrawerOwnership == null)
			resultUserDrawerOwnership = userDrawerOwnershipJSONObject;
			
		this.init();
		this.udrwId = resultUserDrawerOwnership.optString("udrw_id");
		this.shopId = resultUserDrawerOwnership.optInt("udrw_shop_id");
		this.oletId = resultUserDrawerOwnership.optInt("udrw_olet_id");
		this.statId = resultUserDrawerOwnership.optInt("udrw_stat_id");
		this.userId = resultUserDrawerOwnership.optInt("udrw_user_id");
		this.drawer = resultUserDrawerOwnership.optInt("udrw_drawer");
		this.takeTime = resultUserDrawerOwnership.optString("udrw_take_time", null);
		
		String sTakeLocTime = resultUserDrawerOwnership.optString("udrw_take_loctime");
		if(!sTakeLocTime.isEmpty())
			this.takeLocTime = oFmt.parseDateTime(sTakeLocTime);
		
	}
	
	//construct the save request JSON
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("udrw_id", this.udrwId);
			addSaveJSONObject.put("udrw_shop_id", this.shopId);
			addSaveJSONObject.put("udrw_olet_id", this.oletId);
			addSaveJSONObject.put("udrw_stat_id", this.statId);
			addSaveJSONObject.put("udrw_user_id", this.userId);
			addSaveJSONObject.put("udrw_drawer", this.drawer);
			addSaveJSONObject.put("bdayId", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
			
			if(this.takeTime != null)
				addSaveJSONObject.put("udrw_take_time", this.takeTime);
			if(this.takeLocTime != null)
				addSaveJSONObject.put("udrw_take_loctime", dateTimeToString(this.takeLocTime));
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}

	public boolean addUpdate(boolean bUpdate) {
		JSONObject requestJSONObject = new JSONObject();
		
		requestJSONObject = this.constructAddSaveJSON(bUpdate);
		try {
			requestJSONObject.put("bdayId", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
			requestJSONObject.put("stationUserId", AppGlobal.g_oFuncUser.get().getUserId());
			requestJSONObject.put("timezoneOffset", AppGlobal.g_oFuncOutlet.get().getTimeZone());
			if(!AppGlobal.g_oFuncOutlet.get().getTimeZoneName().isEmpty())
				requestJSONObject.put("timezoneName", AppGlobal.g_oFuncOutlet.get().getTimeZoneName());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "saveUserDrawerOwnership", requestJSONObject.toString(), false))
			return false;
		else {
			
			//After insert record to database, update BusinessDay's bday_id
			if (!bUpdate) {
				if (!OmWsClientGlobal.g_oWsClient.get().getResponse().has("id"))
					return false;
				
				this.udrwId = OmWsClientGlobal.g_oWsClient.get().getResponse().optString("id");
			}
			
			return true;
		}
	}
	
	public boolean deleteById(String sOwnershipId, int iDrawer) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("ownershipId", sOwnershipId);
			requestJSONObject.put("drawer", iDrawer);
			//for audit log
			requestJSONObject.put("oletId", AppGlobal.g_oFuncOutlet.get().getOutletId());
			requestJSONObject.put("stationId", AppGlobal.g_oFuncStation.get().getStationId());
			requestJSONObject.put("userId", AppGlobal.g_oFuncUser.get().getUserId());
			requestJSONObject.put("shopId", AppGlobal.g_oFuncOutlet.get().getShopId());
			requestJSONObject.put("bdayId", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
			requestJSONObject.put("timezoneOffset", AppGlobal.g_oFuncOutlet.get().getTimeZone());
			if(!AppGlobal.g_oFuncOutlet.get().getTimeZoneName().isEmpty())
				requestJSONObject.put("timezoneName", AppGlobal.g_oFuncOutlet.get().getTimeZoneName());
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		if (!OmWsClientGlobal.g_oWsClient.get().call("gm", "pos", "deleteUserDrawerOwnership", requestJSONObject.toString(), false))
			return false;
		else {
			
			return true;
		}
	}
	
	public JSONArray readByOutletId(int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray resposneJSONArray = new JSONArray();
		
		try {
			requestJSONObject.put("outletId", iOutletId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		resposneJSONArray = this.readDataListFromApi("gm", "pos", "getUserDrawerOwnershipByOutletId", requestJSONObject.toString());		
		return resposneJSONArray;
	}
	
	public JSONArray readAllActiveByConditions(int iOutletId, int iStationId, int iUserId) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray resposneJSONArray = new JSONArray();
		
		try {
			if(iOutletId != 0)
				requestJSONObject.put("outletId", iOutletId);
			if(iStationId != 0)
				requestJSONObject.put("stationId", iStationId);
			if(iUserId != 0)
				requestJSONObject.put("userId", iUserId);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		resposneJSONArray = this.readDataListFromApi("gm", "pos", "getAllActiveByConditions", requestJSONObject.toString());
		return resposneJSONArray;
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("UserDrawerOwnerships")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("UserDrawerOwnerships")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("UserDrawerOwnerships");
			if(tempJSONObject.isNull("PosUserDrawerOwnership")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray functionJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().has("UserDrawerOwnerships")) {
				// Return a list of member
				
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("UserDrawerOwnerships"))
					return null;
				
				functionJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("UserDrawerOwnerships");
			} else if (OmWsClientGlobal.g_oWsClient.get().getResponse().has("UserDrawerOwnership")) {
				// Return a single member
				
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("UserDrawerOwnership"))
					return null;
				
				functionJSONArray = new JSONArray();
				functionJSONArray.put(OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("UserDrawerOwnerships"));
			}
		}
		return functionJSONArray;
	}
	
	//change DateTime object to string for database insertion/update
	public String dateTimeToString (DateTime oDateTime) {
		if (oDateTime == null)
			return null;
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		String timeString = fmt.print(oDateTime);

		return timeString;
	}
	
	//init value
	public void init() {
		this.udrwId = "";
		this.shopId = 0;
		this.oletId = 0;
		this.statId = 0;
		this.userId = 0;
		this.drawer = 0;
		this.takeTime = null;
		this.takeLocTime = null;
	}

	public String getUdrwId() {
		return this.udrwId;
	}

	public int getShopId() {
		return this.shopId;
	}

	public int getOletId() {
		return this.oletId;
	}

	public int getStatId() {
		return this.statId;
	}

	public int getUserId() {
		return this.userId;
	}

	public int getDrawer() {
		return this.drawer;
	}
	
	public String getTakeTime() {
		return this.takeTime;
	}
	
	public DateTime getTakeLocTime() {
		return this.takeLocTime;
	}

	public void setUdrwId(String udrwId) {
		this.udrwId = udrwId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public void setOletId(int oletId) {
		this.oletId = oletId;
	}

	public void setStatId(int statId) {
		this.statId = statId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setDrawer(int drawer) {
		this.drawer = drawer;
	}
	
	public void setTakeTime(String takeTime) {
		this.takeTime = takeTime;
	}
	
	public void setTakeLocTime(DateTime takeLocTime) {
		this.takeLocTime = takeLocTime;
	}
	
}
