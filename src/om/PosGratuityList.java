package om;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class PosGratuityList {
private List<PosGratuity> m_oGratuityList;
	
	public PosGratuityList() {
		m_oGratuityList = new ArrayList<PosGratuity>();
	}
	
	public void readAllWithAccessControl(int iOutletId, String sBusinessDay, boolean bIsHoliday, boolean bIsDayBeforeHoliday, boolean bIsSpecialDay, boolean bIsDayBeforeSpecialDay, int iWeekday) {
		int i = 0;
		PosGratuity oGratuityList = new PosGratuity(), oGratuity = null;
		JSONArray oGratuityJSONArray = new JSONArray();
		
		oGratuityJSONArray = oGratuityList.readAllWithAccessRight(iOutletId, sBusinessDay, bIsHoliday, bIsDayBeforeHoliday, bIsSpecialDay, bIsDayBeforeSpecialDay, iWeekday);
		if(oGratuityJSONArray != null) {
			for(i=0; i<oGratuityJSONArray.length(); i++) {
				try {
					oGratuity = new PosGratuity(oGratuityJSONArray.getJSONObject(i));
					m_oGratuityList.add(oGratuity);
				}catch(JSONException jsone) {
					jsone.printStackTrace();
				}
			}
		}
	}
	
	public List<PosGratuity> getGratuityList() {
		return this.m_oGratuityList;
	}
}
