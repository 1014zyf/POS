package om;

import org.json.JSONObject;

public class MenuMediaObject {
	private int mmedId;
	private String by;
	private int recordId;
	private String usedFor;
	private int seq;
	private int mediId;
	private int lang;
	
	private MedMedia medMedia;
	
	// used for
	public static String USED_FOR_MAIN_PCITURE = "m";
	public static String USED_FOR_THUMBNAIL = "t";
	public static String USED_FOR_PHOTO_GALLERY = "p";
	
	//init object with default value
	public MenuMediaObject() {
		this.init();
	}
	
	//init object with JSONObject
	public MenuMediaObject(JSONObject mediaJSONObject) {
		JSONObject tempJSONObject = null;
		
		tempJSONObject = mediaJSONObject.optJSONObject("MenuMediaObject");
		if(tempJSONObject == null)
			tempJSONObject = mediaJSONObject;
			
		this.init();
		
		this.mmedId = tempJSONObject.optInt("mmed_id");
		this.by = tempJSONObject.optString("mmed_by");
		this.recordId = tempJSONObject.optInt("mmed_record_id");
		this.usedFor = tempJSONObject.optString("mmed_used_for");
		this.seq = tempJSONObject.optInt("mmed_seq");
		this.mediId = tempJSONObject.optInt("mmed_medi_id");
		this.lang = tempJSONObject.optInt("mmed_lang");
		
		JSONObject oMediaJSONObject = mediaJSONObject.optJSONObject("MedMedia");
		if(oMediaJSONObject != null)
			medMedia = new MedMedia(oMediaJSONObject);
	}
	
	// init value
	public void init() {
		this.mmedId = 0;
		this.by = "";
		this.recordId = 0;
		this.usedFor = "";
		this.seq = 0;
		this.mediId = 0;
		this.lang = 0;
		this.medMedia = null;
	}
	
	//get mmed id
	protected int getMmedId() {
		return this.mmedId;
	}
	
	//get mmed by
	protected String getMmedBy() {
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
