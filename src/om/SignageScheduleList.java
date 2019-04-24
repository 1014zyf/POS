package om;

import java.util.HashMap;

import org.json.JSONArray;

import app.AppGlobal;

public class SignageScheduleList {
	private HashMap<Integer, SignageSchedule> m_oSignageScheduleList;
	
	public SignageScheduleList(){
		m_oSignageScheduleList = new HashMap<Integer, SignageSchedule>();
	}

	public void readSignageScheduleByShopId(int iShopId) {
		SignageSchedule oSignageScheduleList = new SignageSchedule();
		JSONArray oSignageScheduleJSONArray = oSignageScheduleList.readByShopId(iShopId);
		if (oSignageScheduleJSONArray != null) {
			for (int i = 0; i < oSignageScheduleJSONArray.length(); i++) {
				if (oSignageScheduleJSONArray.isNull(i))
					continue;
				
				SignageSchedule oSignageSchedule = new SignageSchedule(oSignageScheduleJSONArray.optJSONObject(i));
				int tempId = oSignageSchedule.getOutletId();
				//store 1 record for 1 outlet only
				if(!m_oSignageScheduleList.containsKey(tempId))
					m_oSignageScheduleList.put(tempId, oSignageSchedule);
			}
		}
	}
	
	//check if signage schedule exist
	public boolean haveSignageSchedule(int iOutletId){
		if(m_oSignageScheduleList != null && m_oSignageScheduleList.containsKey(iOutletId))
			return true;
		return false;
	}
}
