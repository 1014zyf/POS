package om;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

public class PosCalendarPeriod {
	private int cperId;
	private int shopId;
	private int oletId;
	private DateTime date;
	private int perdId;
	private Time startTime;
	private Time endTime;
	
	OutPeriod m_oOutPeriod;
	
	//init object with initialized value
	public PosCalendarPeriod() {
		this.init();
	}
	
	//init object with JSON Object
	public PosCalendarPeriod(JSONObject oJSONObject) {
		readDataFromJson(oJSONObject);
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject periodsJSONObject) {
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		JSONObject tempJSONObject = null;
		
		tempJSONObject = periodsJSONObject.optJSONObject("PosCalendarPeriod");
		if(tempJSONObject == null)
			tempJSONObject = periodsJSONObject;
			
		this.init();
		this.cperId = tempJSONObject.optInt("cper_id");
		this.shopId = tempJSONObject.optInt("cper_shop_id");
		this.oletId = tempJSONObject.optInt("cper_olet_id");
		
		String sDate = tempJSONObject.optString("cper_date");
		if (!sDate.isEmpty()) {
			try {
				this.date = dateFormat.parseDateTime(sDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		this.perdId = tempJSONObject.optInt("cper_perd_id");
		
		String sStartTime = tempJSONObject.optString("cper_start_time");
		if (!sStartTime.isEmpty()) {
			try {
				this.startTime = new Time(timeFormat.parse(sStartTime).getTime());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		
		String sEndTime = tempJSONObject.optString("cper_end_time");
		if (!sEndTime.isEmpty()) {
			try {
				this.endTime = new Time(timeFormat.parse(sEndTime).getTime());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		
		tempJSONObject = periodsJSONObject.optJSONObject("OutPeriod");
		if (tempJSONObject != null) {
			this.m_oOutPeriod = new OutPeriod(tempJSONObject);
		}
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
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("PosCalendarPeriod")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("PosCalendarPeriod")) {
				this.init();
				return false;
			}
			
			tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("PosCalendarPeriod");
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}

	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray displayPanelLookupJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("periods")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("periods")) 
				return null;
			
			displayPanelLookupJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("periods");
		}
		
		return displayPanelLookupJSONArray;
	}
	
	// init value
	public void init() {
		this.cperId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.date = null;
		this.perdId = 0;
		this.startTime = null;
		this.endTime = null;
		
		this.m_oOutPeriod = null;
	}
	
	public int getCperId(){
		return this.cperId;
	}
	
	public int getShopId() {
		return this.shopId;
	}
	
	public int getOletId() {
		return this.oletId;
	}
	
	public DateTime getDate() {
		return this.date;
	}
	
	public int getPerdId(){
		return this.perdId;
	}
	
	public Time getStartTime() {
		return this.startTime;
	}
	
	public Time getEndTime() {
		return this.endTime;
	}
	
	public OutPeriod getOutPeriod() {
		return this.m_oOutPeriod;
	}
}
