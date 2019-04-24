package app.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OutOutlet {
	private int oletId;
	private int outShopId;
	private String code;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String[] addr;
	private String[] info;
	private String phone;
	private String fax;
	private String website;
	private String currencySign;
	private String currencyCode;
	private String dateFormat;
	private String status;
	
	private List<Integer> belongGroupIdList;
	
	private List<OutMediaObject> outMediaObjectList;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_SUSPEND = "s";
	public static String STATUS_DELETED = "d";
	
	//init with initialized value
	public OutOutlet() {
		this.init();
	}
	
	//init obejct with JSONObject
	public OutOutlet(JSONObject outletJSONObject) {
		this.readDataFromJson(outletJSONObject);
	}
	
	//construct the save request json
	protected JSONObject constructAddSaveJSON(boolean bUpdate) {
		int i = 0;
		JSONObject addSaveJSONObject = new JSONObject();
		
		try {
			if(bUpdate)
				addSaveJSONObject.put("olet_id", this.oletId);
			addSaveJSONObject.put("olet_shop_id", this.outShopId);
			addSaveJSONObject.put("olet_code", this.code);
			for(i=1; i<=5; i++) {
				if(!this.name[i-1].isEmpty())
					addSaveJSONObject.put("olet_name_l"+i, this.name[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(!this.shortName[i-1].isEmpty())
					addSaveJSONObject.put("olet_short_name_l"+i, this.shortName[(i-1)]);
			}
			addSaveJSONObject.put("olet_seq", this.seq);
			for(i=1; i<=5; i++) {
				if(this.addr[(i-1)] != null)
					addSaveJSONObject.put("olet_addr_l"+i, this.addr[(i-1)]);
			}
			for(i=1; i<=5; i++) {
				if(this.info[(i-1)] != null)
					addSaveJSONObject.put("olet_info_l"+i, this.info[(i-1)]);
			}
			if(!this.phone.isEmpty())
				addSaveJSONObject.put("olet_phone", this.phone);
			if(!this.fax.isEmpty())
				addSaveJSONObject.put("olet_fax", this.fax);
			if(!this.website.isEmpty())
				addSaveJSONObject.put("olet_website", this.website);
			if(!this.currencySign.isEmpty())
				addSaveJSONObject.put("olet_currency_sign", this.currencySign);
			if(!this.currencyCode.isEmpty())
				addSaveJSONObject.put("olet_currency_code", this.currencyCode);
			if(!this.dateFormat.isEmpty())
				addSaveJSONObject.put("olet_date_format", this.dateFormat);
			if(!this.status.isEmpty())
				addSaveJSONObject.put("olet_status", this.status);
			
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("outlet")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("outlet")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("outlet");
			if(tempJSONObject.isNull("OutOutlet")) {
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
	
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("outlets")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("outlets")) 
				return null;
			
			functionJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("outlets");
		}
		
		return functionJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject outletJSONObject) {
		JSONObject resultOutlet = null;
		int i;
		
		resultOutlet = outletJSONObject.optJSONObject("OutOutlet");
		if(resultOutlet == null)
			resultOutlet = outletJSONObject;

		this.init();
		this.oletId = resultOutlet.optInt("olet_id");
		this.outShopId = resultOutlet.optInt("olet_shop_id");
		this.code = resultOutlet.optString("olet_code");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultOutlet.optString("olet_name_l"+i);
		
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultOutlet.optString("olet_short_name_l"+i);		
		
		this.seq = resultOutlet.optInt("olet_seq");
		
		for(i=1; i<=5; i++) {
			this.addr[(i-1)] = resultOutlet.optString("olet_addr_l"+i, null); 
		}
		
		for(i=1; i<=5; i++) {
			this.info[(i-1)] = resultOutlet.optString("olet_info_l"+i, null);
		}
		
		this.phone = resultOutlet.optString("olet_phone");
		this.fax = resultOutlet.optString("olet_fax");
		this.website = resultOutlet.optString("olet_website");
		this.currencySign = resultOutlet.optString("olet_currency_sign");
		this.currencyCode = resultOutlet.optString("olet_currency_code");
		this.dateFormat = resultOutlet.optString("olet_date_format");
		this.status = resultOutlet.optString("olet_status", OutOutlet.STATUS_ACTIVE);
		
		JSONArray oOutletGroupJSONArray = outletJSONObject.optJSONArray("OutOutletGroup");
		if(oOutletGroupJSONArray != null) {
			for(i=0; i<oOutletGroupJSONArray.length(); i++){
				JSONObject oTempOutletJSONObject = oOutletGroupJSONArray.optJSONObject(i);
				if(oTempOutletJSONObject != null) {
					int iGroupId = oTempOutletJSONObject.optInt("ogrp_id");
					this.belongGroupIdList.add(iGroupId);
				}
			}
		}
		
		//get outlet media object
		JSONArray tempJSONArray = outletJSONObject.optJSONArray("media_objects");
		if(tempJSONArray != null) {
			for(i=0; i<tempJSONArray.length(); i++) {
				JSONObject temp2JSONObject = tempJSONArray.optJSONObject(i);
				if(temp2JSONObject != null) {
					OutMediaObject oMediaObject = new OutMediaObject(temp2JSONObject);
					this.outMediaObjectList.add(oMediaObject);
				}
			}
		}
	}

	//reset object value
	public void init() {
		int i=0;
		
		this.oletId = 0;
		this.outShopId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		if(this.addr == null)
			this.addr = new String[5];
		for(i=0; i<5; i++)
			this.addr[i] = null;
		if(this.info == null)
			this.info = new String[5];
		for(i=0; i<5; i++)
			this.info[i] = null;
		this.phone = "";
		this.fax = "";
		this.website = "";
		this.currencySign = "";
		this.currencyCode = "";
		this.status = OutOutlet.STATUS_ACTIVE;
		
		if(this.belongGroupIdList == null)
			this.belongGroupIdList = new ArrayList<Integer>();
		else
			this.belongGroupIdList.clear();
		
		if(this.outMediaObjectList == null)
			this.outMediaObjectList = new ArrayList<OutMediaObject>();
		else
			this.outMediaObjectList.clear();
	}
	
	//get outlet media url
	public String getMediaUrl(String sType) {
		String mediaUrl = "";
		
		for(OutMediaObject oMediaObject:this.outMediaObjectList) {
			if(!oMediaObject.getUsedFor().equals(sType))
				continue;
			
			if(oMediaObject.getMedia().getUrl().isEmpty())
				continue;
			else {
				mediaUrl = oMediaObject.getMedia().getUrl();
				break;
			}
		}
		
		return mediaUrl;
	}
	
	//get outlet media filename
	public String getMediaFilename(String sType) {
		String mediaFilename = "";
		
		for(OutMediaObject oMediaObject:this.outMediaObjectList) {
			if(!oMediaObject.getUsedFor().equals(sType))
				continue;
			
			if(oMediaObject.getMedia().getFilename().isEmpty())
				continue;
			else {
				mediaFilename = oMediaObject.getMedia().getFilename();
				break;
			}
		}
		
		return mediaFilename;
	}
	
	//get outlet id
	public int getOletId () {
		return this.oletId;
	}
	
	//get shop id
	public int getShopId() {
		return this.outShopId;
	}
	
	//get code
	public String getCode() {
		return this.code;
	}
	
	//get name
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get short name
	protected String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get address
	public String getAddr(int iIndex) {
		return this.addr[(iIndex-1)];
	}
	
	//get information
	public String getInfo(int iIndex) {
		return this.info[(iIndex-1)];
	}
	
	//get outlet shop id
	public int getOutletShopId(){
		return this.outShopId;
	}
	
	//get outlet group list
	public List<Integer> getOutletGroupList() {
		return this.belongGroupIdList;
	}
	
	//get seq
	public int getSeq() {
		return this.seq;
	}
	
	//get currency sign
	public String getCurrencySign() {
		return this.currencySign;
	}
	
	//read data by olet_id
	public boolean readById(int iOletId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("id", Integer.toString(iOletId));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		return this.readDataFromApi("gm", "outlet", "getOutletById", requestJSONObject.toString());
		
	}
	
	//read all outlet
	public JSONArray readAll(int iRecursive) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("recursive", Integer.toString(iRecursive));
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataListFromApi("gm", "outlet", "getAllOutlets", requestJSONObject.toString());	
	}
}
