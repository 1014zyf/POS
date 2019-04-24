package app.model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;

public class OutCalendarList {
	private ArrayList<OutCalendar> m_oCalendarList;
	
	public OutCalendarList() {
		m_oCalendarList = new ArrayList<OutCalendar>();
	}
	
	//read data from database by shopId, oletid, date
	public boolean readCalendarListByShopOutletDate(int iShopId, int iOutletId, String sDate) {
		boolean bResult = true;
		OutCalendar oCalendarList = new OutCalendar();
		JSONArray responseJSONArray = null;

		responseJSONArray = oCalendarList.readCalendarListByShopOutletDate(iShopId, iOutletId, sDate);
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				try {
					OutCalendar oCalendar = new OutCalendar(responseJSONArray.getJSONObject(i));
					m_oCalendarList.add(oCalendar);	
				}catch (JSONException jsone) {
					jsone.printStackTrace();
					bResult = false;
				}
			}
		}

		return bResult;
		
	}
	
	public ArrayList<OutCalendar> getCalendarList() {
		return m_oCalendarList;
	}
	
	public void clearCalendarList() {
		this.m_oCalendarList.clear();
	}
}
