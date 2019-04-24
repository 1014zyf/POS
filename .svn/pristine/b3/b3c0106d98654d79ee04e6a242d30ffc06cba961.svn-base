package app;

import om.SignageScheduleList;

public class FuncSignage {
	
	// signage schedule List
	private SignageScheduleList m_oSignageScheduleList;
	
	public FuncSignage() {
		m_oSignageScheduleList = new SignageScheduleList();
	}
	
	// Load signage schedule
	public void loadSignageScheduleByShopId(int iShopId) {
		// Read signage schedule from OM
		m_oSignageScheduleList.readSignageScheduleByShopId(iShopId);
	}
	
	//check if signage schedule exist
	public boolean haveSignageSchedule(int iOutletId) {
		if(m_oSignageScheduleList != null)
			return m_oSignageScheduleList.haveSignageSchedule(iOutletId);
		return false;
	}
}
