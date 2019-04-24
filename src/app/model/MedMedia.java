package app.model;

import java.util.HashMap;

import om.OmWsClientGlobal;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MedMedia {
	private int mediId;
	private int albmId;
	private String[] name;
	private String filename;
	private String mimeType;
	private int fileSize;
	private DateTime createTime;
	private String status;
	
	private String url;
	
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with 
	public MedMedia() {
		this.init();
	}
	
	//init object with JSONObject
	public MedMedia(JSONObject oMediaJSONObject) {
		this.readDataFromJson(oMediaJSONObject);
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("media")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("media")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("media");

			if(tempJSONObject.isNull("MedMedia")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject medMediaJSONObject) {
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		JSONObject resultMedia = null;
		int i;
		resultMedia = medMediaJSONObject.optJSONObject("MedMedia");
		if(resultMedia == null)
			resultMedia = medMediaJSONObject;

		this.init();
		this.mediId = resultMedia.optInt("medi_id");
		this.albmId = resultMedia.optInt("medi_albm_id");
		for(i=1; i<=5; i++) {
			this.name[(i-1)] = resultMedia.optString("medi_name_l"+i);
		}
		this.filename = resultMedia.optString("medi_filename");
		this.mimeType = resultMedia.optString("medi_mime_type");
		this.fileSize = resultMedia.optInt("medi_filesize");
		String sCreateTime = resultMedia.optString("medi_create_time");
		if(!sCreateTime.isEmpty())
			this.createTime = oFormatter.parseDateTime(sCreateTime);
		else
			this.createTime = null;
		this.status = resultMedia.optString("medi_status", MedMedia.STATUS_ACTIVE);
		
		this.url = resultMedia.optString("url");
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray mediaJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("medias")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("medias")) {
				return null;
			}
			mediaJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("medias");
		}
		
		return mediaJSONArray;
	}
	
	//init value
	public void init() {
		int i=0;
		this.mediId = 0;
		this.albmId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		this.filename = "";
		this.mimeType = "";
		this.fileSize = 0;
		this.createTime = null;
		this.status = MedMedia.STATUS_ACTIVE;
		this.url = "";
	}
	
	//read record by media id
	public boolean readById(int iMediaId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try{
			requestJSONObject.put("id", iMediaId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "media", "getMediaById", requestJSONObject.toString());
		
	}
	
	//read record and media url by id, width and height
	public boolean readInfoAndUrlById(int iMediaId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try{
			requestJSONObject.put("id", iMediaId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "media", "getMediaAndUrlById", requestJSONObject.toString());
		
	}
	
	//read record by media ids
	public JSONArray readMediasByIdList(HashMap<Integer, Integer> oMediaIdList) {
		int iIdCount = 0;
		JSONObject requestJSONObject = new JSONObject(), tempJSONObject = null;
		JSONArray responseJSONArray = null;
		
		try {
			tempJSONObject = new JSONObject();
			for(Integer iMediaId:oMediaIdList.values()) {
				tempJSONObject.put(String.valueOf(iIdCount), iMediaId);
				iIdCount++;
			}
			
			requestJSONObject.put("ids", tempJSONObject);
			requestJSONObject.put("recursive", -1);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "media", "getMultiMediasWithUrlByIds", requestJSONObject.toString());
		return responseJSONArray;
	}
	
	//get url
	public String getUrl() {
		return this.url;
	}

	protected int getMediId() {
		return this.mediId;
	}
	
	protected int getAlbmId() {
		return this.albmId;
	}
	
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	protected String getFilename() {
		return this.filename;
	}
	
	protected String getMimeType() {
		return this.mimeType;
	}
	
	protected int getFileSize() {
		return this.fileSize;
	}
	
	protected DateTime getCreateTime() {
		return this.createTime;
	}
	
	protected String getStatus() {
		return this.status;
	}
}
