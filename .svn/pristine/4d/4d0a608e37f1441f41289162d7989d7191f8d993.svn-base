package app.model;

import org.json.JSONObject;

public class OutMediaObject {
	private int omedId;
	private String by;
	private int recordId;
	private String usedFor;
	private int seq;
	private int mediId;
	private int lang;
	
	private MedMedia medMedia;
	
	// used for
	public static String USED_FOR_MAIN_PICTURE = "m";
	public static String USED_FOR_THUMBNAIL = "t";
	public static String USED_FOR_PHOTO_GALLERY = "p";
	public static String USED_FOR_LOGO = "l";
	
	//init object with default value
	public OutMediaObject() {
		this.init();
	}
	
	//init object with JSONObject
	public OutMediaObject(JSONObject mediaJSONObject) {
		JSONObject tempJSONObject = null;
		
		tempJSONObject = mediaJSONObject.optJSONObject("OutMediaObject");
		if(tempJSONObject == null)
			tempJSONObject = mediaJSONObject;
			
		this.init();
		this.omedId = tempJSONObject.optInt("omed_id");
		this.by = tempJSONObject.optString("omed_by");
		this.recordId = tempJSONObject.optInt("omed_record_id");
		this.usedFor = tempJSONObject.optString("omed_used_for");
		this.seq = tempJSONObject.optInt("omed_seq");
		this.mediId = tempJSONObject.optInt("omed_medi_id");
		this.lang = tempJSONObject.optInt("omed_lang");
		
		JSONObject tempJSONObject1 = mediaJSONObject.optJSONObject("MedMedia");
		if(tempJSONObject1 != null)
			medMedia = new MedMedia(tempJSONObject1);
	}
	
	public void init() {
		this.omedId = 0;
		this.by = "";
		this.recordId = 0;
		this.usedFor = "";
		this.seq = 0;
		this.mediId = 0;
		this.lang = 0;
		this.medMedia = null;
	}
	
	//get omed id
	protected int getOmedId() {
		return this.omedId;
	}
	
	//get omed by
	protected String getOmedBy() {
		return this.by;
	}
	
	//get record id
	protected int getRecordId() {
		return this.recordId;
	}
	
	//get used for
	protected String getUsedFor() {
		return this.usedFor;
	}
	
	//get seq
	protected int getSeq() {
		return this.seq;
	}
	
	//get media id
	protected int getMediaId() {
		return this.mediId;
	}
	
	//get lang
	protected int getLang() {
		return this.lang;
	}
	
	//get media object
	protected MedMedia getMedia() {
		return this.medMedia;
	}
}
