package app.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosBusinessDayList {
	private HashMap<Integer, PosBusinessDay> m_oBusinessDayList;
	
	public PosBusinessDayList() {
		m_oBusinessDayList = new HashMap<Integer, PosBusinessDay>();
	}
	
	//read a business day list between a period
	public void readBusinessDayByOutletPeriod(int iOutletId, String sStartDate, String sEndDate) {
		PosBusinessDay oBusinessDay = new PosBusinessDay();
		JSONArray responseJSONArray = oBusinessDay.readListByOutletWithPeriodOfTime(iOutletId, sStartDate, sEndDate);
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				if (responseJSONArray.isNull(i) || !responseJSONArray.optJSONObject(i).has("PosBusinessDay"))
					continue;
				JSONObject responseJSONObject = responseJSONArray.optJSONObject(i).optJSONObject("PosBusinessDay");
				PosBusinessDay oTempBusinessDay = new PosBusinessDay(responseJSONObject);
				
				//Add to business day list
				m_oBusinessDayList.put(oTempBusinessDay.getBdayId(), oTempBusinessDay);
			}
		}
	}
	
	//get a array list of business day id
	public ArrayList<Integer> getListOfBusinessDayId() {
		ArrayList<Integer> oBusinessDayIdList = new ArrayList<Integer>();
		
		for(PosBusinessDay oBusinessDay:m_oBusinessDayList.values()) {
			if (oBusinessDay.getBdayId() > 0)
				oBusinessDayIdList.add(oBusinessDay.getBdayId());
		}
		
		return oBusinessDayIdList;
	}
}
