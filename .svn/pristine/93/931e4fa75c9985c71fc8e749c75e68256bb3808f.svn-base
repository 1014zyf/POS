package om;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OutSpecialHourList {
	private List<OutSpecialHour> m_oSpecialHourList;
	
	public OutSpecialHourList() {
		m_oSpecialHourList = new ArrayList<OutSpecialHour>();
	}
	
	public boolean readByOutletId(int iOletId, JSONObject oCheckCriteria) {
		boolean bResult = true;
		OutSpecialHour oCalendarList = new OutSpecialHour();
		JSONArray responseJSONArray = oCalendarList.readByOutletId(iOletId, oCheckCriteria);
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				try {
					JSONObject specialHourJSONObject = responseJSONArray.getJSONObject(i);
					OutSpecialHour oSpecialHour = new OutSpecialHour(specialHourJSONObject);
					m_oSpecialHourList.add(oSpecialHour);
					
				}catch (JSONException jsone) {
					jsone.printStackTrace();
					bResult = false;
				}
			}
		}
		
		return bResult;
		
	}
	
	public List<OutSpecialHour> getSpecialHourList() {
		return this.m_oSpecialHourList;
	}
	
}
