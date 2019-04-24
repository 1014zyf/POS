package app;

import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import om.InfInterface;
import om.InfVendor;
import om.PosActionLog;
import om.PosInterfaceConfig;
import om.PosUserTimeInOutLog;
import om.UserUser;

public class FuncUserTimeInOutLog {
	PosUserTimeInOutLog m_oUserTimeInOutLog;

	public FuncUserTimeInOutLog() {
		m_oUserTimeInOutLog = new PosUserTimeInOutLog();
	}
	
	public void addUserTimeInLog(String sBdayId, int iShopId, int iOletId, int iUserId) {
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime oOpenTime = AppGlobal.getCurrentTime(false);
		
		m_oUserTimeInOutLog.init();
		
		m_oUserTimeInOutLog.setBusinessDayId(sBdayId);
		m_oUserTimeInOutLog.setShopId(iShopId);
		m_oUserTimeInOutLog.setOutletId(iOletId);
		m_oUserTimeInOutLog.setUserId(iUserId);
		m_oUserTimeInOutLog.setOpenLocTime(oOpenTime);
		m_oUserTimeInOutLog.setOpenTime(oFormatter.print(AppGlobal.convertTimeToUTC(oOpenTime)));
		
		m_oUserTimeInOutLog.addUpdate(false);

		String sRemark = "Time In: "+oFormatter.print(oOpenTime);
		AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.user_time_in_out.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", iUserId, iShopId, iOletId, sBdayId, AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", sRemark);
		// surveillance event: ClockIn
		HashMap<String, String> oSurveillanceEventInfo = new HashMap<String, String>();
		this.doSurveillanceEventForTimeInOutLog(FuncSurveillance.SURVEILLANCE_TYPE_CLOCK_IN, iUserId, oSurveillanceEventInfo);
		/*
		List<PosInterfaceConfig> oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_SURVEILLANCE_INTERFACE);
		if (!oInterfaceConfigList.isEmpty()) {
			for (PosInterfaceConfig oPosInterfaceConfig : oInterfaceConfigList) {
				if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ECONNECT)) {
					FuncSurveillance oFuncSurveillance = new FuncSurveillance(oPosInterfaceConfig);
					HashMap<String, String> oSurveillanceEventInfo = new HashMap<String, String>();
					oSurveillanceEventInfo.put("eventType", FuncSurveillance.SURVEILLANCE_TYPE_CLOCK_IN);
					// get the ClockIn employee number
					UserUser oUser = new UserUser();
					oUser.readByUserId(iUserId);
					oSurveillanceEventInfo.put("employeeNum", oUser.getNumber());
					oFuncSurveillance.surveillanceEvent(oSurveillanceEventInfo);
				}
			}
		}
		*/
	}
	
	public DateTime getOpenLocTime() {
		return m_oUserTimeInOutLog.getOpenLocTime();
	}
	
	public DateTime getCloseLocTime() {
		return m_oUserTimeInOutLog.getCloseLocTime();
	}
	
	public void addUserTimeOutLog(int iPrintQueueId, int iLangIndex) {
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime oCloseTime = AppGlobal.getCurrentTime(false);
		m_oUserTimeInOutLog.setCloseLocTime(oCloseTime);
		m_oUserTimeInOutLog.setCloseTime(oFormatter.print(AppGlobal.convertTimeToUTC(oCloseTime)));
		
		m_oUserTimeInOutLog.addUpdate(true);

		String sRemark = "Time Out: "+oFormatter.print(oCloseTime);
		AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.user_time_in_out.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", m_oUserTimeInOutLog.getUserId(), m_oUserTimeInOutLog.getShopId(), m_oUserTimeInOutLog.getOutletId(), m_oUserTimeInOutLog.getBusinessDayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", sRemark);
		
		m_oUserTimeInOutLog.printUserTimeOutSlip(m_oUserTimeInOutLog.getBusinessDayId(), m_oUserTimeInOutLog.getOutletId(), m_oUserTimeInOutLog.getUserId(), iPrintQueueId, iLangIndex);
		// surveillance event: ClockIn
		HashMap<String, String> oSurveillanceEventInfo = new HashMap<String, String>();
		this.doSurveillanceEventForTimeInOutLog(FuncSurveillance.SURVEILLANCE_TYPE_CLOCK_OUT, m_oUserTimeInOutLog.getUserId(), oSurveillanceEventInfo);
		/*
		List<PosInterfaceConfig> oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_SURVEILLANCE_INTERFACE);
		if (!oInterfaceConfigList.isEmpty()) {
			for (PosInterfaceConfig oPosInterfaceConfig : oInterfaceConfigList) {
				if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ECONNECT)) {
					FuncSurveillance oFuncSurveillance = new FuncSurveillance(oPosInterfaceConfig);
					HashMap<String, String> oSurveillanceEventInfo = new HashMap<String, String>();
					oSurveillanceEventInfo.put("eventType", FuncSurveillance.SURVEILLANCE_TYPE_CLOCK_OUT);
					// get the ClockIn employee number
					UserUser oUser = new UserUser();
					oUser.readByUserId(m_oUserTimeInOutLog.getUserId());
					oSurveillanceEventInfo.put("employeeNum", oUser.getNumber());
					oFuncSurveillance.surveillanceEvent(oSurveillanceEventInfo);
				}
			}
		}
		*/
	
	}
	
	public boolean checkUserTimeIn(String sBdayId, int iUserId) {
		m_oUserTimeInOutLog.readByUserId(sBdayId, iUserId);
		if(!m_oUserTimeInOutLog.getUtioId().equals("") && m_oUserTimeInOutLog.getCloseTime() == null) // Logged In
			return true;
		else
			return false;
	}
	
	public int getUserTimeInOutCount() {
		return m_oUserTimeInOutLog.getTimeInOutCount();
	}
	
	private void doSurveillanceEventForTimeInOutLog(String sEventType, int iUserId, HashMap<String, String> oSurveillanceEventInfo) {
		List<PosInterfaceConfig> oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_SURVEILLANCE_INTERFACE);
		if (!oInterfaceConfigList.isEmpty()) {
			for (PosInterfaceConfig oPosInterfaceConfig : oInterfaceConfigList) {
				if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ECONNECT)) {
					FuncSurveillance oFuncSurveillance = new FuncSurveillance(oPosInterfaceConfig);
					oSurveillanceEventInfo.put("eventType", sEventType);
					// get the ClockIn employee number
					UserUser oUser = new UserUser();
					oUser.readByUserId(iUserId);
					oSurveillanceEventInfo.put("employeeNum", oUser.getNumber());
					oFuncSurveillance.surveillanceEvent(oSurveillanceEventInfo, null);
				}
			}
		}
	}
}
