package om;

import org.json.JSONObject;

public class PosPrintFormat {
	private int pfmtId;
	private String[] name;
	private String[] shortName;
	private String type;
	private int seq;
	private int lang;
	private String[] sortItemBy;
	private String groupItemBy;
	private String status;
	
	// sortItemBy 
	public static String SORT_ITEM_BY_MENU_ITEM_ID = "";
	public static String SORT_ITEM_BY_CATEGORY = "c";
	public static String SORT_ITEM_BY_DEPARTMENT = "d";
	public static String SORT_ITEM_BY_COURSE = "u";
	public static String SORT_ITEM_BY_SEAT_NO = "s";
	public static String SORT_ITEM_BY_ORDER_TIME = "t";
	
	// groupItemBy
	public static String GROUP_ITEM_BY_INDIVIDUAL = "";
	public static String GROUP_ITEM_BY_COMBINE = "c";
	
	// status
	public static String STATUS_ACTIVE= "";
	public static String STATUS_DELETED ="d";
	
	public PosPrintFormat() {
		this.init();
	}
	
	public PosPrintFormat(JSONObject printFormatJSONObject) {
		this.readDataFromJson(printFormatJSONObject);
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject printFormatJSONObject) {
		JSONObject resultPrintFormat = null;
		int i=0;
		
		resultPrintFormat = printFormatJSONObject.optJSONObject("PosPrintFormat");
		if(resultPrintFormat == null)
			resultPrintFormat = printFormatJSONObject;
			
		this.init();

		this.pfmtId = resultPrintFormat.optInt("pfmt_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultPrintFormat.optString("pfmt_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultPrintFormat.optString("pfmt_short_name_l"+i);
		this.type = resultPrintFormat.optString("pfmt_type");
		this.seq = resultPrintFormat.optInt("pfmt_seq");
		this.lang = resultPrintFormat.optInt("pfmt_lang");
		for(i=1; i<=2; i++)
			this.sortItemBy[(i-1)] = resultPrintFormat.optString("pfmt_sort_item_by"+i, PosPrintFormat.SORT_ITEM_BY_MENU_ITEM_ID);
		this.groupItemBy = resultPrintFormat.optString("pfmt_group_item_by", PosPrintFormat.GROUP_ITEM_BY_INDIVIDUAL);
		this.status = resultPrintFormat.optString("pfmt_status", PosPrintFormat.STATUS_ACTIVE);
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.pfmtId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.type = "";
		this.seq = 0;
		this.lang = 0;
		if(this.sortItemBy == null)
			this.sortItemBy = new String[2];
		for(i=0; i<2; i++)
			this.sortItemBy[i] = PosPrintFormat.SORT_ITEM_BY_MENU_ITEM_ID;
		this.groupItemBy = PosPrintFormat.GROUP_ITEM_BY_INDIVIDUAL;
		this.status = PosPrintFormat.STATUS_ACTIVE;
	}
	
	protected int getPfmtId() {
		return this.pfmtId;
	}
	
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	protected String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	protected  String getType() {
		return this.type;
	}
	
	protected  int getSeq() {
		return this.seq;
	}
	
	protected int getLang() {
		return this.lang;
	}
	
	protected String getSortItemBy(int iIndex) {
		return this.sortItemBy[(iIndex-1)];
	}
	
	protected String getGroupItemBy() {
		return this.groupItemBy;
	}
	
	protected String getStatus() {
		return this.status;
	}
}
