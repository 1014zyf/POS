package om;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosBusinessDayList {
	private HashMap<String, PosBusinessDay> m_oBusinessDayList;
	
	public PosBusinessDayList() {
		m_oBusinessDayList = new HashMap<String, PosBusinessDay>();
	}
	
	//read a business day list between a period
	public void readBusinessDayByShopOrOutletPeriod(int iShopId, int iOutletId, String sStartDate, String sEndDate) {
		PosBusinessDay oBusinessDay = new PosBusinessDay();
		JSONArray responseJSONArray = oBusinessDay.readListByShopOrOutletWithPeriodOfTime(iShopId, iOutletId, sStartDate, sEndDate);
		m_oBusinessDayList = new HashMap<String, PosBusinessDay>();
		
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
	public ArrayList<String> getListOfBusinessDayId() {
		ArrayList<String> oBusinessDayIdList = new ArrayList<String>();
		
		for(PosBusinessDay oBusinessDay:m_oBusinessDayList.values()) {
			if (oBusinessDay.getBdayId().compareTo("") > 0)
				oBusinessDayIdList.add(oBusinessDay.getBdayId());
		}
		
		return oBusinessDayIdList;
	}
}
