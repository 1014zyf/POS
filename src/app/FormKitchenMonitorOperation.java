package app;
import java.util.LinkedList;

import javax.xml.bind.DatatypeConverter;

import core.Controller;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;

public class FormKitchenMonitorOperation extends VirtualUIForm implements FrameKitchenMonitorOperationListener {
	public enum FUNC_CALL_LIST{QsrInitialConnection, QsrCloseConnection, QsrOpenCheck, QsrAddItem, QsrCalculateCheck, QsrPayCheck, QsrVoidItem, QsrVoidCheck, QsrChangeTable};
	
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameKitchenMonitorOperation m_oFrameKitchenMonitorOperation;
	private FuncKitchenMonitor m_oFuncKitchenMonitor;
	private String m_sSendPacketString;
	
	public static int ITEM_TYPE_NORMAL_ITEM = 1;
	public static int ITEM_TYPE_PIECE_DETAIL = 3;
	public static int ITEM_TYPE_CHILD_ITEM = 5;
	public static int ITEM_TYPE_MODIFIER_ITEM = 6;
	
	public static final int ITEM_DESCRIPTION_LENGTH = 20;
	public static final String NETWORK_SUCCESS = "success";
	
	private int m_iStationId;
	private LinkedList<String> m_oFuncCallList;
	
	public FormKitchenMonitorOperation(FuncKitchenMonitor oFuncKitchenMonitor, Controller oParentController) {
		super(oParentController);
		
		m_oFuncKitchenMonitor = oFuncKitchenMonitor;
		m_iStationId = AppGlobal.g_oFuncStation.get().getStationId();
		m_oFuncCallList = new LinkedList<String>();
	}
	
	public boolean initializeKitchenMonitorOperation() { 
		m_oTemplateBuilder = new TemplateBuilder();
		
		//no need to show ui
		//m_oTemplateBuilder.loadTemplate("frmKitchenMonitorOperation.xml");
		
		// Login Frame
		m_oFrameKitchenMonitorOperation = new FrameKitchenMonitorOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameKitchenMonitorOperation, "fraKitchenMonitorOperation");
		m_oFrameKitchenMonitorOperation.init();
		m_oFrameKitchenMonitorOperation.showInitScreen();
		// Add listener;
		m_oFrameKitchenMonitorOperation.addListener(this);
		this.attachChild(m_oFrameKitchenMonitorOperation);
		return true;
	}
	
	public void kitchenMonitorQsrInitConnection() {
		Object[] oParameters = new Object[2];
		oParameters[0] = m_oFuncKitchenMonitor.getServerIP();
		oParameters[1] = m_oFuncKitchenMonitor.getServerPort();
		
		m_sSendPacketString = createWinFcnMapString(FUNC_CALL_LIST.QsrInitialConnection.name(), oParameters);
		m_oFuncCallList.add(m_sSendPacketString);
	}
	
	public void kitchenMonitorQsrCloseConnection() {
		Object[] oParameters = new Object[0];
		
		m_sSendPacketString = createWinFcnMapString(FUNC_CALL_LIST.QsrCloseConnection.name(), oParameters);
		m_oFuncCallList.add(m_sSendPacketString);
	}
	
	public void kitchenMonitorQsrOpenCheck(int iCheckNumber, String sStationCode, String sDestinationId, int iGuest, String sTableNo, int iUserId, String sUserName) {
		//convert string to integer
		int iStationCode = 0;
		int iDestinationId = 0;
		try {
			iStationCode = Integer.parseInt(sStationCode);
		} catch (Exception e) {
			iStationCode = 0;
		}
		
		try{
			iDestinationId = Integer.parseInt(sDestinationId);
		} catch (Exception e) {
			iDestinationId = 0;
		}
		
		Object[] oParameters = new Object[9];
		oParameters[0] = iCheckNumber;
		oParameters[1] = iStationCode; //terminal id
		oParameters[2] = iDestinationId; //print queue sequence
		oParameters[3] = iGuest;
		oParameters[4] = sTableNo;
		oParameters[5] = iUserId;
		oParameters[6] = DatatypeConverter.printBase64Binary(sUserName.getBytes());
		oParameters[7] = m_oFuncKitchenMonitor.getServerIP();
		oParameters[8] = m_oFuncKitchenMonitor.getServerPort();
		
		m_sSendPacketString = createWinFcnMapString(FUNC_CALL_LIST.QsrOpenCheck.name(), oParameters);
		m_oFuncCallList.add(m_sSendPacketString);
	}
	
	public void kitchenMonitorQsrAddItem(int iCheckNumber, String sCourseCode, int iItemType, int iItemNumber, int iParentItemNumber, String sItemCode, String sDepartmentCode, String sCategoryCode, int iQty, int iCookTime, String sItemDescription, int iSeatNo, int iRushPercent) {
		//convert string to integer
		int iItemCode = 0;
		int iCourseCode = 0;
		int iDepartmentCode = 0;
		int iCategoryCode = 0;
		try {
			iItemCode = Integer.parseInt(sItemCode);
		} catch (Exception e) {
			iItemCode = 0;
		}
		
		try{
			iCourseCode = Integer.parseInt(sCourseCode);
		} catch (Exception e) {
			iCourseCode = 0;
		}
		
		try{
			iDepartmentCode = Integer.parseInt(sDepartmentCode);
		} catch (Exception e) {
			iDepartmentCode = 0;
		}
		
		try{
			iCategoryCode = Integer.parseInt(sCategoryCode);
		} catch (Exception e) {
			iCategoryCode = 0;
		}
		
		//check description size
		if(sItemDescription.length() > FormKitchenMonitorOperation.ITEM_DESCRIPTION_LENGTH)
			sItemDescription = sItemDescription.substring(0, ITEM_DESCRIPTION_LENGTH-1);
		
		Object[] oParameters = new Object[15];
		oParameters[0] = iCheckNumber;
		oParameters[1] = iCourseCode;
		oParameters[2] = iItemType;
		oParameters[3] = iItemNumber;
		oParameters[4] = iParentItemNumber;
		oParameters[5] = iItemCode;
		oParameters[6] = iDepartmentCode;
		oParameters[7] = iCategoryCode;
		oParameters[8] = iQty;
		oParameters[9] = iCookTime;
		oParameters[10] = DatatypeConverter.printBase64Binary(sItemDescription.getBytes());
		oParameters[11] = iSeatNo;
		oParameters[12] = iRushPercent;
		oParameters[13] = m_oFuncKitchenMonitor.getServerIP();
		oParameters[14] = m_oFuncKitchenMonitor.getServerPort();
		
		m_sSendPacketString = createWinFcnMapString(FUNC_CALL_LIST.QsrAddItem.name(), oParameters);
		m_oFuncCallList.add(m_sSendPacketString);
	}
	
	public void kitchenMonitorQsrCalculateCheck(int iCheckNumber, double dItemTotal, double dTaxTotal, double dCheckTotal) {
		Object[] oParameters = new Object[6];
		oParameters[0] = iCheckNumber;
		oParameters[1] = dItemTotal;
		oParameters[2] = dTaxTotal;
		oParameters[3] = dCheckTotal;
		oParameters[4] = m_oFuncKitchenMonitor.getServerIP();
		oParameters[5] = m_oFuncKitchenMonitor.getServerPort();
		
		m_sSendPacketString = createWinFcnMapString(FUNC_CALL_LIST.QsrCalculateCheck.name(), oParameters);
		m_oFuncCallList.add(m_sSendPacketString);
	}
	
	public void kitchenMonitorQsrPayCheck(int iCheckNumber, double dItemTotal, double dTaxTotal, double dCheckTotal, double dPaymentAmount, double dPaymentChange) {
		Object[] oParameters = new Object[8];

		oParameters[0] = iCheckNumber;
		oParameters[1] = dItemTotal;
		oParameters[2] = dTaxTotal;
		oParameters[3] = dCheckTotal;
		oParameters[4] = dPaymentAmount;
		oParameters[5] = dPaymentChange;
		oParameters[6] = m_oFuncKitchenMonitor.getServerIP();
		oParameters[7] = m_oFuncKitchenMonitor.getServerPort();
		
		m_sSendPacketString = createWinFcnMapString(FUNC_CALL_LIST.QsrPayCheck.name(), oParameters);
		m_oFuncCallList.add(m_sSendPacketString);
	}
	
	public void kitchenMonitorQsrVoidItem(int iCheckNumber, String sCourseCode, int iItemNumber, int iQty) {
		int iCourseCode = 0;
		try{
			iCourseCode = Integer.parseInt(sCourseCode);
		} catch (Exception e) {
			iCourseCode = 0;
		}
		
		Object[] oParameters = new Object[6];
		oParameters[0] = iCheckNumber;
		oParameters[1] = iCourseCode;
		oParameters[2] = iItemNumber;
		oParameters[3] = iQty;
		oParameters[4] = m_oFuncKitchenMonitor.getServerIP();
		oParameters[5] = m_oFuncKitchenMonitor.getServerPort();
		
		m_sSendPacketString = createWinFcnMapString(FUNC_CALL_LIST.QsrVoidItem.name(), oParameters);
		m_oFuncCallList.add(m_sSendPacketString);
	}
	
	public void kitchenMonitorQsrVoidCheck(int iCheckNumber) {
		Object[] oParameters = new Object[3];
		oParameters[0] = iCheckNumber;
		oParameters[1] = m_oFuncKitchenMonitor.getServerIP();
		oParameters[2] = m_oFuncKitchenMonitor.getServerPort();
		
		m_sSendPacketString = createWinFcnMapString(FUNC_CALL_LIST.QsrVoidCheck.name(), oParameters);
		m_oFuncCallList.add(m_sSendPacketString);
	}
	
	public void kitchenMonitorQsrChangeTable(int iCheckNumber, int iCourseNumber, String sTable) {
		Object[] oParameters = new Object[5];
		oParameters[0] = iCheckNumber;
		oParameters[1] = iCourseNumber;
		oParameters[2] = sTable;
		oParameters[3] = m_oFuncKitchenMonitor.getServerIP();
		oParameters[4] = m_oFuncKitchenMonitor.getServerPort();
		
		m_sSendPacketString = createWinFcnMapString(FUNC_CALL_LIST.QsrChangeTable.name(), oParameters);
		m_oFuncCallList.add(m_sSendPacketString);
	}
	
	public String createWinFcnMapString(String sType, Object[] oParameters) {
		StringBuilder sArgString = new StringBuilder();
		
		for(int i = 0; i < oParameters.length; i++) {
			if (oParameters[i] != null) {
				if (i != 0)
					sArgString.append(";");
				
				if (oParameters[i].getClass().equals(String.class)) {
					String sString = (String) oParameters[i];
					StringBuilder sb = new StringBuilder();
					for (int j=0; j<sString.length(); j++) {
						sb.append(String.format("%02x", (int)sString.charAt(j)));
					}
					sArgString.append(sb.toString());
				} else
					sArgString.append(oParameters[i]);
			}
		}
		
		sArgString.append(";");
		
		return String.format("\001win_fcn_map\002%s\002%s\004", sType, sArgString.toString());
	}
	
	public void stepControl(){
		if(!m_oFuncCallList.isEmpty()){
			String sFirstSendString = m_oFuncCallList.getFirst();
			m_oFuncCallList.pop();
			m_oFuncKitchenMonitor.writeLog(m_oFuncKitchenMonitor.getInterfaceId(), "Kitchen Monitor (QSR) - Send Data - "+sFirstSendString, m_iStationId);
			m_oFrameKitchenMonitorOperation.createForwardEvent(sFirstSendString, m_oFuncKitchenMonitor.getTimeout(), 0);
		}else
			this.finishOperation();
	}
	
	public void finishOperation(){
		// Set the connection timeout to device manager to prevent timeout loop
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(0);	
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public void FrameKitchenMonitorOperation_forward(String sResponse) {
		if(!sResponse.equals(FormKitchenMonitorOperation.NETWORK_SUCCESS))
			m_oFuncKitchenMonitor.writeLog(m_oFuncKitchenMonitor.getInterfaceId(), "Kitchen Monitor (QSR) - Error - "+sResponse, m_iStationId);
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(0);	
		this.stepControl();
	}

	@Override
	public void FrameKitchenMonitorOperation_disconnect() {
		m_oFuncKitchenMonitor.writeLog(m_oFuncKitchenMonitor.getInterfaceId(), "Kitchen Monitor (QSR) - Error - Device Manager Disconnect", m_iStationId);
		this.finishOperation();
	}
	
	@Override
	public void FrameKitchenMonitorOperation_timeout(){
		m_oFuncKitchenMonitor.writeLog(m_oFuncKitchenMonitor.getInterfaceId(), "Kitchen Monitor (QSR) - Error - Device Manager Timeout", m_iStationId);
		this.finishOperation();
	}
}
