package app.controller;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import app.model.PosActionLog;
import app.model.PosUserTimeInOutLog;

public class FuncUserTimeInOutLog {
	PosUserTimeInOutLog m_oUserTimeInOutLog;

	public FuncUserTimeInOutLog() {
		m_oUserTimeInOutLog = new PosUserTimeInOutLog();
	}
	
	public void addUserTimeInLog(int iBdayId, int iShopId, int iOletId, int iUserId) {
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime oOpenTime = new DateTime();
		
		m_oUserTimeInOutLog.init();
		
		m_oUserTimeInOutLog.setBusinessDayId(iBdayId);
		m_oUserTimeInOutLog.setShopId(iShopId);
		m_oUserTimeInOutLog.setOutletId(iOletId);
		m_oUserTimeInOutLog.setUserId(iUserId);
		m_oUserTimeInOutLog.setOpenLocTime(oOpenTime);
		m_oUserTimeInOutLog.setOpenTime(oFormatter.print(oOpenTime.withZone(DateTimeZone.UTC)));
		
		m_oUserTimeInOutLog.addUpdate(false);

		String sRemark = "Time In: "+oFormatter.print(oOpenTime.withZone(DateTimeZone.UTC));
		AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.user_time_in_out.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", iUserId, iShopId, iOletId, iBdayId, AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), 0, 0, 0, 0, 0, sRemark);
	}

	public DateTime getOpenLocTime() {
		return m_oUserTimeInOutLog.getOpenLocTime();
	}
	
	public DateTime getCloseLocTime() {
		return m_oUserTimeInOutLog.getCloseLocTime();
	}
	
	public void addUserTimeOutLog(int iPrintQueueId, int iLangIndex) {
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime oCloseTime = new DateTime();
		m_oUserTimeInOutLog.setCloseLocTime(oCloseTime);
		m_oUserTimeInOutLog.setCloseTime(oFormatter.print(oCloseTime.withZone(DateTimeZone.UTC)));
		
		m_oUserTimeInOutLog.addUpdate(true);

		String sRemark = "Time Out: "+oFormatter.print(oCloseTime.withZone(DateTimeZone.UTC));
		AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.user_time_in_out.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", m_oUserTimeInOutLog.getUserId(), m_oUserTimeInOutLog.getShopId(), m_oUserTimeInOutLog.getOutletId(), m_oUserTimeInOutLog.getBusinessDayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), 0, 0, 0, 0, 0, sRemark);
		
		m_oUserTimeInOutLog.printUserTimeOutSlip(m_oUserTimeInOutLog.getBusinessDayId(), m_oUserTimeInOutLog.getOutletId(), m_oUserTimeInOutLog.getUserId(), iPrintQueueId, iLangIndex);
	}
	
	public boolean checkUserTimeIn(int iBdayId, int iUserId) {
		m_oUserTimeInOutLog.readByUserId(iBdayId, iUserId);
		if(m_oUserTimeInOutLog.getUtioId() > 0 && m_oUserTimeInOutLog.getCloseTime() == null) // Logged In
			return true;
		else
			return false;
	}
	
	public int getUserTimeInOutCount() {
		return m_oUserTimeInOutLog.getTimeInOutCount();
	}
}
