package app.model;

import org.json.JSONException;
import org.json.JSONObject;

public class OutShop {
	private int shopId;
	private String code;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String[] addr;
	private String[] info;
	private String phone;
	private String fax;
	private String website;
	private int timezone;
	private String status;
	
	static public String STATUS_ACTIVE = "";
	static public String STATUS_SUSPENDED = "s";
	static public String STATUS_DELETED = "d";
		
	//init with initialized value
	public OutShop() {
		this.init();
		
	}
	
	//construct the save request json
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		int i=0;
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("shop_id", this.shopId);
			if(this.code != null)
				addSaveJSONObject.put("shop_code", this.code);
			for(i=1; i<=5; i++) {
				if(!this.name[i-1].isEmpty())
					addSaveJSONObject.put("shop_name_l"+i, this.name[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(!this.shortName[i-1].isEmpty())
					addSaveJSONObject.put("shop_short_name_l"+i, this.shortName[(i-1)]);
			}
			if(this.seq > 0)
				addSaveJSONObject.put("shop_seq", this.seq);
			for(i=1; i<=5; i++) {
				if(this.addr[(i-1)] != null)
					addSaveJSONObject.put("shop_addr_l"+i, this.addr[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(this.info[(i-1)] != null)
					addSaveJSONObject.put("shop_info_l"+i, this.info[(i-1)]);
			}
			if(!this.phone.isEmpty())
				addSaveJSONObject.put("shop_phone", this.phone);
			if(!this.fax.isEmpty())
				addSaveJSONObject.put("shop_fax", this.fax);
			if(!this.website.isEmpty())
				addSaveJSONObject.put("shop_website", this.website);
			if(this.timezone > 0)
				addSaveJSONObject.put("shop_timezone", this.timezone);
			if(!this.status.isEmpty())
				addSaveJSONObject.put("shop_status", this.status);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("shop")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("shop")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("shop");
			if (tempJSONObject.isNull("OutShop")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject shopJSONObject) {
		JSONObject resultShop = null;
		int i;
		
		resultShop = shopJSONObject.optJSONObject("OutShop");
		if(resultShop == null)
			resultShop = shopJSONObject;
			
		this.init();

		this.shopId = resultShop.optInt("shop_id");
		this.code = resultShop.optString("shop_code", null);
		for(i=1; i<=5; i++) {
			this.name[(i-1)] = resultShop.optString("shop_name_l"+i);
			this.shortName[(i-1)] = resultShop.optString("shop_short_name_l"+i);
		}
		this.seq = resultShop.optInt("shop_seq");
		for(i=1; i<=5; i++) {
			this.addr[(i-1)] = resultShop.optString("shop_addr_l"+i, null);
			this.info[(i-1)] = resultShop.optString("shop_info_l"+i, null);
		}
		this.phone = resultShop.optString("shop_phone");
		this.fax = resultShop.optString("shop_fax");
		this.website = resultShop.optString("shop_website");
		this.timezone = resultShop.optInt("shop_timezone");
		this.status = resultShop.optString("shop_status", OutShop.STATUS_ACTIVE);
	}
	
	//init value
	public void init() {
		int i=0;
		
		this.shopId = 0;
		this.code = "";
		if(this.name == null)
			this.name = new String[5];
		for(i=1; i<=5; i++)
			this.name[(i-1)] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = "";
		this.seq = 0;
		if(this.addr == null)
			this.addr = new String[5];
		for(i=1; i<=5; i++)
			this.addr[(i-1)] = null;
		if(this.info == null)
			this.info = new String[5];
		for(i=1; i<=5; i++)
			this.info[(i-1)] = null;
		this.phone = "";
		this.fax = "";
		this.website = "";
		this.timezone = 0;
		this.status = OutShop.STATUS_ACTIVE;
	}
	
	//read data by shop_id
	public boolean readById(int iShopId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("id", iShopId);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "outlet", "getShopById", requestJSONObject.toString());
	}
	
	//get shop id
	protected int getShopId() {
		return this.shopId;
	}
	
	//get shop code
	protected String getShopCode() {
		return this.code;
	}
	
	//get shop name
	public String getShopName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get shop short name
	protected String getShopShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get seq
	protected int getShopSeq() {
		return this.seq;
	}
	
	//get addr
	protected String getShopAddr(int iIndex) {
		return this.addr[(iIndex-1)];
	}
	
	//get info
	protected String getShopInfo(int iIndex) {
		return this.info[(iIndex-1)];
	}
	
	//get phone
	protected String getShopPhone() {
		return this.phone;
	}
	
	//get fax
	protected String getShopFax() {
		return this.fax;
	}
	
	//get website
	protected String getShopWebsite() {
		return this.website;
	}
	
	//get timezone
	public int getTimezone() {
		return this.timezone;
	}
	
	//get status
	protected String getStatus() {
		return this.status;
	}
}
