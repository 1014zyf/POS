package om;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OutCalendar {
	private int caldId;
	private int shopId;
	private int oletId;
	private String[] name;
	private String[] shortName;
	private DateTime date;
	private String type;
	private DateTimeFormatter m_oDTFormatterYMD;
	
	// type
	public static String TYPE_NORMAL_DAY = "";
	public static String TYPE_HOLIDAY = "h";
	public static String TYPE_SPECIAL_DAY = "s";
	
	public OutCalendar() {
		this.init();
	}

	//init obejct with JSONObject
	public OutCalendar(JSONObject calendarJSONObject) {
		this.readDataFromJson(calendarJSONObject);
	}
	
	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bUpdate = true;
		JSONObject tempJSONObject = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bUpdate = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("calendar")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("calendar")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("calendar");
			if(tempJSONObject.isNull("OutCalendar")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bUpdate;
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray outletTableJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("calendars")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("calendars")) 
				return null;
			
			outletTableJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("calendars");
		}
		
		return outletTableJSONArray;
	}
	
	//read data from database by shopId, oletid, date
	public JSONArray readCalendarListByShopOutletDate(int iShopId, int iOutletId, String sDate) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("shopId", Integer.toString(iShopId));
			requestJSONObject.put("outletId", Integer.toString(iOutletId));
			requestJSONObject.put("date", sDate);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}

		responseJSONArray = this.readDataListFromApi("gm", "outlet", "getCalendarsByShopOutletDate", requestJSONObject.toString());

		return responseJSONArray;
		
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject calendarJSONObject) {
		JSONObject resultOutletCalendar = null;
		int i;
		
		resultOutletCalendar = calendarJSONObject.optJSONObject("OutCalendar");
		if(resultOutletCalendar == null)
			resultOutletCalendar = calendarJSONObject;
			
		this.init();

		this.caldId = resultOutletCalendar.optInt("cald_id");
		this.shopId = resultOutletCalendar.optInt("cald_shop_id");
		this.oletId = resultOutletCalendar.optInt("cald_olet_id");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultOutletCalendar.optString("cald_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultOutletCalendar.optString("cald_short_name_l"+i);
		
		String sDate = resultOutletCalendar.optString("cald_date");
		if(!sDate.isEmpty())
			try {
				this.date = m_oDTFormatterYMD.parseDateTime(sDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		else
			this.date = null;
		
		this.type = resultOutletCalendar.optString("cald_type");
	}
	
	//init value
	public void init () {
		int i = 0;
		m_oDTFormatterYMD = DateTimeFormat.forPattern("yyyy-MM-dd");
		
		this.caldId = 0;
		this.shopId = 0;
		this.oletId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.date = null;
		this.type = OutCalendar.TYPE_NORMAL_DAY;
	}
	
	public int getCaldId() {
		return this.caldId;
	}
	
	public int getShopId() {
		return this.shopId;
	}
	
	public int getOutletId() {
		return this.oletId;
	}
	
	//get name with lang index
	protected String[] getName() {
		return this.name;
	}
	
	//get short name with lang index
	protected String[] getShortName() {
		return this.shortName;
	}
	
	public DateTime getDate() {
		return this.date;
	}
	
	public String getType() {
		return this.type;
	}
	
	public boolean isHoliday() {
		return this.type.equals(OutCalendar.TYPE_HOLIDAY);
	}
	
	public boolean isSpecialDay() {
		return this.type.equals(OutCalendar.TYPE_SPECIAL_DAY);
	}
}
