package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import externallib.TCPLib;

public class FuncCoupon {
	private TCPLib m_oTCPLib;
	private String m_sServerIPAddress;
	private int m_iPort;
	private String m_sServerLocale;
	private String m_sErrMsg;
	
	private List<HashMap<String, String>> m_oSendPacket;
	private HashMap<String, String> m_oReceivePacket;
	
	private int m_iCouponType;
	private String m_sStartCoupon;
	private String m_sEndCoupon;
	private String m_sCurrentStatus;
	private String m_sNextStatus;
	private BigDecimal m_dFacePrice;
	private BigDecimal m_dSellPrice;
	private String m_sSellDate;
	private String m_sSellTime;
	private String m_sSellCheck;
	private String[] m_sRedeemItems;
	private BigDecimal m_dRedeemPrice;
	private String m_sRedeemDate;
	private String m_sRedeemTime;
	private String m_sRedeemOutlet;
	private String m_sRedeemCheck;
	private String m_sRedeemItem;
	private String m_sCouponItem;
	private String m_sEmployeeId;
	private String m_sEmployeeName;
	private String m_sMember;
	
	private BigDecimal m_dCouponQty;
	private FuncMenuItem m_oCouponItem;
	
	private static char PACKET_SOH = 0x01;
	private static char PACKET_EOT = 0x04;
	private static char PACKET_SEP = 0x1C;
	
	public static String COUPON_STATUS_NEW = "A";
	public static String COUPON_STATUS_SOLD = "S";
	public static String COUPON_STATUS_USED = "U";
	public static String COUPON_STATUS_SUSPEND = "D";
	public static String COUPON_STATUS_LOCKED = "L";
	
	public static int UPDATE_COUPON_TYPE_ORDER_ITEM = 0;
	public static int UPDATE_COUPON_TYPE_USE_AS_PAYMENT = 1;
	
	public static int COUPON_TYPE_PAID_FIXED_AMOUNT = 1;
	public static int COUPON_TYPE_PAID_ITEM = 2; 
	
	public FuncCoupon() {
		m_oTCPLib = new TCPLib();
		if(AppGlobal.getPosConfig("coupon_system", "coupon_server") != null)
			m_sServerIPAddress = AppGlobal.getPosConfig("coupon_system", "coupon_server").getValue();
		else
			m_sServerIPAddress = "";
		if(AppGlobal.getPosConfig("coupon_system", "coupon_server_port") != null)
			m_iPort = Integer.valueOf(AppGlobal.getPosConfig("coupon_system", "coupon_server_port").getValue()).intValue();
		else
			m_iPort = 0;
		if(AppGlobal.getPosConfig("coupon_system", "coupon_server_locale") != null)
			m_sServerLocale = AppGlobal.getPosConfig("coupon_system", "coupon_server_locale").getValue();
		else
			m_sServerLocale = "";
		m_sErrMsg = "";
		
		m_oSendPacket = new ArrayList<HashMap<String, String>>();
		m_oReceivePacket = new HashMap<String, String>();
		
		m_iCouponType = 0;
		m_sStartCoupon = "";
		m_sEndCoupon = "";
		m_sCurrentStatus = " ";
		m_sNextStatus = " ";
		m_dFacePrice = BigDecimal.ZERO;
		m_dSellPrice = BigDecimal.ZERO;
		m_sSellDate = "";
		m_sSellTime = "";
		m_sSellCheck = "";
		m_sRedeemItems = new String[30];
		for(int i=0; i<30; i++)
			m_sRedeemItems[i] = "";
		m_dRedeemPrice = BigDecimal.ZERO;
		m_sRedeemDate = "";
		m_sRedeemTime = "";
		m_sRedeemOutlet = "";
		m_sRedeemCheck = "";
		m_sRedeemItem = "";
		m_sCouponItem = "";
		m_sEmployeeId = "";
		m_sEmployeeName = "";
		m_sMember = "";
		
		m_dCouponQty = BigDecimal.ZERO;
		m_oCouponItem = null;
	}
	
	private boolean sendPacketToServer() {
		String sSendData = "";
		int iParamCount = 0;
		
		if(m_oSendPacket.isEmpty())
			return false;
		
		for(HashMap<String, String> oData:m_oSendPacket) {
			if(iParamCount == 0)
				sSendData = sSendData + PACKET_SOH;
			else
				sSendData = sSendData + PACKET_SEP;
			sSendData = sSendData + oData.get("key") + "=" + oData.get("value");
			iParamCount++;
		}
		sSendData = sSendData + PACKET_EOT;
		
		m_oTCPLib.initClient(m_sServerIPAddress, m_iPort, false);
		if(!m_oTCPLib.isClientSocketConnected()) {
			setErrorMessage(1018);
			return false; 
		}
		
		//send and get data to coupon server
		if((m_oTCPLib.writeToServer(sSendData)) == false)
			return false;
		
		return true;
	}
	
	private boolean receivePacketFromServer() {
		int iReceiveDataLength = 0;
		String sReceiveData = "", sReceiveInfo = "", sKey = "", sValue = "";
		StringTokenizer oStrTok1 = null, oStrTok2 = null;
		
		m_oReceivePacket.clear();
		if(!m_oTCPLib.isClientSocketConnected()) {
			setErrorMessage(1018);
			return false;
		}
		
		try {
			sReceiveData = m_oTCPLib.readFromServer(m_sServerLocale);
			m_oTCPLib.closeClientSocket();
			
			if(sReceiveData.length() > 0) {
				if(sReceiveData.startsWith(String.valueOf(PACKET_SOH)) && (sReceiveData.endsWith(String.valueOf(PACKET_EOT)))) {
					iReceiveDataLength = sReceiveData.length();
					sReceiveData = sReceiveData.substring(1, (iReceiveDataLength-1));
					
					oStrTok1 = new StringTokenizer(sReceiveData, String.valueOf(PACKET_SEP));
					while(oStrTok1.hasMoreElements()) {
						sReceiveInfo = "";
						sKey = "";
						sValue = "";
						sReceiveInfo = (String) oStrTok1.nextElement();
												
						oStrTok2 = new StringTokenizer(sReceiveInfo, "=");
						if(oStrTok2.hasMoreElements()) {
							sKey = (String) oStrTok2.nextElement();
							if(oStrTok2.hasMoreElements()) { 
								sValue = (String) oStrTok2.nextElement();
								m_oReceivePacket.put(sKey, sValue);
							}
						}
					}
					
				}else {
					setErrorMessage(1017);
				}
			}
			
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
			AppGlobal.stack2Log(e);
			return false;
		}
		
		return true;
	}
	
	private void addSendPacketParam(String sKey, String sValue) {
		HashMap<String, String> oPacketParam = new HashMap<String, String>();
		
		oPacketParam.put("key", sKey);
		oPacketParam.put("value", sValue);
		m_oSendPacket.add(oPacketParam);
	}
		
	private void setErrorMessage(int iCase) {
		m_sErrMsg = "";
		
		switch (iCase) {
			case 1001:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_has_not_sold");
				break;
			case 1002:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_is_locked");
				break;
			case 1003:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_is_used");
				break;
			case 1004:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_is_not_allowed_to_use_in_this_outlet");
				break;
			case 1005:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_is_expired");
				break;
			case 1006:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_is_not_allowed_to_use_today");
				break;
			case 1007:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_is_only_valid_on_holiday");
				break;
			case 1008:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_is_not_valid_on_holiday");
				break;
			case 1009:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_is_not_valid_on_sunday");
				break;
			case 1010:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_is_not_valid_on_monday");
				break;
			case 1011:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_is_not_valid_on_tuesday");
				break;
			case 1012:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_is_not_valid_on_wednesday");
				break;
			case 1013:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_is_not_valid_on_thursday");
				break;
			case 1014:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_is_not_valid_on_friday");
				break;
			case 1015:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_is_not_valid_on_saturday");
				break;
			case 1016:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_does_not_exist");
				break;
			case 1017:
				m_sErrMsg = AppGlobal.g_oLang.get()._("incorrect_response");
				break;
			case 1018:
				m_sErrMsg = AppGlobal.g_oLang.get()._("fail_to_connect_to_coupon_server");
				break;
			case 1019:
				m_sErrMsg = AppGlobal.g_oLang.get()._("fail_to_receive_response_from_coupon_server");
				break;
			case 1020:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_cannot_be_sold");
				break;
			case 1021:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_number_must_be_in_ascending_order");
				break;
			case 1022:
				m_sErrMsg = AppGlobal.g_oLang.get()._("face_price_of_coupon_is_different");
				break;
			case 1023:
				m_sErrMsg = AppGlobal.g_oLang.get()._("server_database_error");
				break;
			case 1024:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_prefix_must_be_same");
				break;
			case 2001:
				m_sErrMsg = AppGlobal.g_oLang.get()._("missing_coupon_item");
				break;
			case 2002:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_item_cannot_be_found");
				break;
			case 2003:
				m_sErrMsg = AppGlobal.g_oLang.get()._("invalid_coupon_type");
				break;
			case 2004:
				m_sErrMsg = AppGlobal.g_oLang.get()._("coupon_item_not_available");
				break;
		}
	}
	
	public void reset() {
		m_oSendPacket.clear();
		m_oReceivePacket.clear();
		
		m_iCouponType = 0;
		m_sStartCoupon = "";
		m_sEndCoupon = "";
		m_sCurrentStatus = " ";
		m_sNextStatus = " ";
		m_dFacePrice = BigDecimal.ZERO;
		m_dSellPrice = BigDecimal.ZERO;
		m_sSellDate = "";
		m_sSellTime = "";
		m_sSellCheck = "";
		m_sRedeemItems = new String[30];
		for(int i=0; i<30; i++)
			m_sRedeemItems[i] = "";
		m_dRedeemPrice = BigDecimal.ZERO;
		m_sRedeemDate = "";
		m_sRedeemTime = "";
		m_sRedeemOutlet = "";
		m_sRedeemCheck = "";
		m_sRedeemItem = "";
		m_sCouponItem = "";
		m_sEmployeeId = "";
		m_sEmployeeName = "";
		m_sMember = "";
		
		m_dCouponQty = BigDecimal.ZERO;
		m_oCouponItem = null;
	}
	
	public boolean isCouponServerSetupExist() {
		if(m_sServerIPAddress.isEmpty() || m_iPort == 0 || m_sServerLocale.isEmpty())
			return false;
		
		return true;
	}
	
	public boolean getCouponType() {
		m_oSendPacket.clear();
		addSendPacketParam("FN", "GetCouponType");
		addSendPacketParam("SN", m_sStartCoupon);
		
		//send to server
		if(!sendPacketToServer()) {
			m_oTCPLib.closeClientSocket();
			if(m_sErrMsg.length() <= 0)
				setErrorMessage(1018);
			return false;
		}
		
		//receive from server
		if(!receivePacketFromServer()) {
			m_oTCPLib.closeClientSocket();
			if(m_sErrMsg.length() <= 0)
				setErrorMessage(1018);
			return false;
		}
		
		if(m_oReceivePacket.containsKey("ER")) {
			setErrorMessage(Integer.valueOf(m_oReceivePacket.get("ER")));
			return false;
		}
		
		if(!m_oReceivePacket.containsKey("FN") || (m_oReceivePacket.containsKey("FN") && !m_oReceivePacket.get("FN").equals("GetCouponTypeResponse"))) {
			setErrorMessage(1017);
			return false;
		}
		
		if(!m_oReceivePacket.containsKey("SN") || (m_oReceivePacket.containsKey("SN") && !m_oReceivePacket.get("SN").equals(m_sStartCoupon))){
			setErrorMessage(1017);
			return false;
		}
		
		if(m_oReceivePacket.containsKey("CT"))
			m_iCouponType = Integer.valueOf(m_oReceivePacket.get("CT"));
		
		if(m_oReceivePacket.containsKey("CI"))
			m_sCouponItem = m_oReceivePacket.get("CI");
		
		return true;
	}
	
	public boolean checkCoupon() {
		addSendPacketParam("FN", "CouponCheck");
		addSendPacketParam("SN", m_sStartCoupon);
		addSendPacketParam("SO", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		
		//send to server
		if(!sendPacketToServer()) {
			m_oTCPLib.closeClientSocket();
			if(m_sErrMsg.length() <= 0)
				setErrorMessage(1018);
			return false;
		}
		
		//receive from server
		if(!receivePacketFromServer()) {
			m_oTCPLib.closeClientSocket();
			if(m_sErrMsg.length() <= 0)
				setErrorMessage(1018);
			return false;
		}
		
		if(m_oReceivePacket.containsKey("ER")) {
			setErrorMessage(Integer.valueOf(m_oReceivePacket.get("ER")));
			return false;
		}
		
		if(!m_oReceivePacket.containsKey("FN") || (m_oReceivePacket.containsKey("FN") && !m_oReceivePacket.get("FN").equals("CouponCheckResponse"))) {
			setErrorMessage(1017);
			return false;
		}
		
		if(!m_oReceivePacket.containsKey("SN") || (m_oReceivePacket.containsKey("SN") && !m_oReceivePacket.get("SN").equals(m_sStartCoupon))) {
			setErrorMessage(1017);
			return false;
		}
		
		if(m_oReceivePacket.containsKey("CT"))
			m_iCouponType = Integer.valueOf(m_oReceivePacket.get("CT"));
		
		if(m_oReceivePacket.containsKey("CI"))
			m_sCouponItem = m_oReceivePacket.get("CI");
		
		if(m_oReceivePacket.containsKey("R1"))
			m_sRedeemItems[0] = m_oReceivePacket.get("R1");
		
		if(m_oReceivePacket.containsKey("R2"))
			m_sRedeemItems[1] = m_oReceivePacket.get("R2");
		
		if(m_oReceivePacket.containsKey("R3"))
			m_sRedeemItems[2] = m_oReceivePacket.get("R3");
		
		if(m_oReceivePacket.containsKey("R4"))
			m_sRedeemItems[3] = m_oReceivePacket.get("R4");
		
		if(m_oReceivePacket.containsKey("R5"))
			m_sRedeemItems[4] = m_oReceivePacket.get("R5");
		
		if(m_oReceivePacket.containsKey("R6"))
			m_sRedeemItems[5] = m_oReceivePacket.get("R6");
		
		if(m_oReceivePacket.containsKey("R7"))
			m_sRedeemItems[6] = m_oReceivePacket.get("R7");
		
		if(m_oReceivePacket.containsKey("R8"))
			m_sRedeemItems[7] = m_oReceivePacket.get("R8");
		
		if(m_oReceivePacket.containsKey("R9"))
			m_sRedeemItems[8] = m_oReceivePacket.get("R9");
		
		if(m_oReceivePacket.containsKey("RA"))
			m_sRedeemItems[9] = m_oReceivePacket.get("RA");
		
		if(m_oReceivePacket.containsKey("RB"))
			m_sRedeemItems[10] = m_oReceivePacket.get("RB");
		
		if(m_oReceivePacket.containsKey("RC"))
			m_sRedeemItems[11] = m_oReceivePacket.get("RC");
		
		if(m_oReceivePacket.containsKey("RD"))
			m_sRedeemItems[12] = m_oReceivePacket.get("RD");
		
		if(m_oReceivePacket.containsKey("RE"))
			m_sRedeemItems[13] = m_oReceivePacket.get("RE");
		
		if(m_oReceivePacket.containsKey("RF"))
			m_sRedeemItems[14] = m_oReceivePacket.get("RF");
		
		if(m_oReceivePacket.containsKey("RG"))
			m_sRedeemItems[15] = m_oReceivePacket.get("RG");
		
		if(m_oReceivePacket.containsKey("RH"))
			m_sRedeemItems[16] = m_oReceivePacket.get("RH");
		
		if(m_oReceivePacket.containsKey("RI"))
			m_sRedeemItems[17] = m_oReceivePacket.get("RI");
		
		if(m_oReceivePacket.containsKey("RJ"))
			m_sRedeemItems[18] = m_oReceivePacket.get("RJ");
		
		if(m_oReceivePacket.containsKey("RK"))
			m_sRedeemItems[19] = m_oReceivePacket.get("RK");
		
		if(m_oReceivePacket.containsKey("RL"))
			m_sRedeemItems[20] = m_oReceivePacket.get("RL");
		
		if(m_oReceivePacket.containsKey("RM"))
			m_sRedeemItems[21] = m_oReceivePacket.get("RM");
		
		if(m_oReceivePacket.containsKey("RN"))
			m_sRedeemItems[22] = m_oReceivePacket.get("RN");
		
		if(m_oReceivePacket.containsKey("RO"))
			m_sRedeemItems[23] = m_oReceivePacket.get("RO");
		
		if(m_oReceivePacket.containsKey("RP"))
			m_sRedeemItems[24] = m_oReceivePacket.get("RP");
		
		if(m_oReceivePacket.containsKey("RQ"))
			m_sRedeemItems[25] = m_oReceivePacket.get("RQ");
		
		if(m_oReceivePacket.containsKey("RR"))
			m_sRedeemItems[26] = m_oReceivePacket.get("RR");
		
		if(m_oReceivePacket.containsKey("RS"))
			m_sRedeemItems[27] = m_oReceivePacket.get("RS");
		
		if(m_oReceivePacket.containsKey("RT"))
			m_sRedeemItems[28] = m_oReceivePacket.get("RT");
		
		if(m_oReceivePacket.containsKey("RU"))
			m_sRedeemItems[29] = m_oReceivePacket.get("RU");
		
		if(m_oReceivePacket.containsKey("FP"))
			m_dFacePrice = new BigDecimal(m_oReceivePacket.get("FP"));
		
		return true;
	}
	
	public boolean getSellCoupon(String sStartCoupon, String sEndCoupon) {
		m_sStartCoupon = sStartCoupon;
		m_sEndCoupon = sEndCoupon;
		
		//check coupon type
		if(getCouponType() == false)
			return false;
		
		if(m_sCouponItem.equals("")) {
			setErrorMessage(2001);
			return false;
		}
		
		//check coupon type
		if(m_iCouponType != 1 && m_iCouponType != 2) {
			setErrorMessage(2003);
			return false;
		}
		
		//check existence of coupon item
		m_oCouponItem = AppGlobal.g_oFuncMenu.get().getFuncMenuItemByItemCode(m_sCouponItem);
		if(m_oCouponItem == null) {
			setErrorMessage(2002);
			return false;
		}
		
		if(!m_oCouponItem.isItemAvailable()) {
			setErrorMessage(2004);
			return false;
		}
		
		return true;
	}
	
	public boolean updateCoupon(int iType) {
		m_dCouponQty = BigDecimal.ZERO;
		
		m_oSendPacket.clear();
		addSendPacketParam("FN", "UpdateCouponStatus");
		addSendPacketParam("FT", String.valueOf(iType));
		addSendPacketParam("SN", m_sStartCoupon);
		addSendPacketParam("EN", m_sEndCoupon);
		addSendPacketParam("CS", m_sCurrentStatus);
		addSendPacketParam("NS", m_sNextStatus);
		addSendPacketParam("SP", m_dSellPrice.toString());
		addSendPacketParam("SD", m_sSellDate);
		addSendPacketParam("ST", m_sSellTime);
		addSendPacketParam("SO", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		addSendPacketParam("SC", m_sSellCheck);
		addSendPacketParam("RP", m_dRedeemPrice.toString());
		addSendPacketParam("RD", m_sRedeemDate);
		addSendPacketParam("RT", m_sRedeemTime);
		addSendPacketParam("RO", m_sRedeemOutlet);
		addSendPacketParam("RC", m_sRedeemCheck);
		addSendPacketParam("RI", m_sRedeemItem);
		addSendPacketParam("EM", m_sEmployeeId);
		addSendPacketParam("NA", m_sEmployeeName);
		
		//send to server
		if(!sendPacketToServer()) {
			m_oTCPLib.closeClientSocket();
			if(m_sErrMsg.length() <= 0)
				setErrorMessage(1018);
			return false;
		}
		
		//receive from server
		if(!receivePacketFromServer()) {
			m_oTCPLib.closeClientSocket();
			if(m_sErrMsg.length() <= 0)
				setErrorMessage(1018);
			return false;
		}
		
		if(m_oReceivePacket.containsKey("ER")) {
			setErrorMessage(Integer.valueOf(m_oReceivePacket.get("ER")));
			return false;
		}
		
		if(!m_oReceivePacket.containsKey("FN") || (m_oReceivePacket.containsKey("FN") && !m_oReceivePacket.get("FN").equals("UpdateCouponStatusResponse"))) {
			setErrorMessage(1017);
			return false;
		}
		
		if(!m_oReceivePacket.containsKey("SN") || (m_oReceivePacket.containsKey("SN") && !m_oReceivePacket.get("SN").equals(m_sStartCoupon))) {
			setErrorMessage(1017);
			return false;
		}
		
		if(m_oReceivePacket.containsKey("FP"))
			m_dFacePrice = new BigDecimal(m_oReceivePacket.get("FP"));
		
		if(m_oReceivePacket.containsKey("CN"))
			m_dCouponQty = new BigDecimal(m_oReceivePacket.get("CN"));
		
		if(m_oReceivePacket.containsKey("MN"))
			m_sMember = m_oReceivePacket.get("MN");
		
		return true;
	}
	
	public String getLastErrorMessage() {
		return m_sErrMsg;
	}
	
	public int getType() {
		return m_iCouponType;
	}
	
	public String getStartCoupon() {
		return m_sStartCoupon;
	}
	
	public String getEndCoupon() {
		return m_sEndCoupon;
	}
	
	public int getCouponItemId() {
		return m_oCouponItem.getMenuItem().getItemId();
	}
	
	public BigDecimal getFacePrice() {
		return m_dFacePrice;
	}
	
	public BigDecimal getCouponQty() {
		return m_dCouponQty;
	}
	
	public String getRedeemItem(int iIndex) {
		return m_sRedeemItems[(iIndex-1)];
	}
	
	public void setStartCoupon(String sStartCoupon) {
		m_sStartCoupon = sStartCoupon;
	}
	
	public void setEndCoupon(String sEndCoupon) {
		m_sEndCoupon = sEndCoupon;
	}
	
	public void setCouponCurrentStatus(String sCurrentStatus) {
		m_sCurrentStatus = sCurrentStatus;
	}
	
	public void setCouponNextStatus(String sNextStatus) {
		m_sNextStatus = sNextStatus;
	}
	
	public void setSellPrice(BigDecimal dSellPrice) {
		m_dSellPrice = dSellPrice;
	}
	
	public void setSellDate(String sSellDate) {
		m_sSellDate = sSellDate;
	}
	
	public void setSellTime(String sSellTime) {
		m_sSellTime = sSellTime;
	}
	
	public void setSellCheck(String sCheckNum) {
		m_sSellCheck = sCheckNum;
	}
	
	public void setRedeemPrice(BigDecimal dRedeemPrice) {
		m_dRedeemPrice = dRedeemPrice;
	}
	
	public void setRedeemDate(String sRedeemDate) {
		m_sRedeemDate = sRedeemDate;
	}
	
	public void setRedeemTime(String sRedeemTime) {
		m_sRedeemTime = sRedeemTime;
	}
	
	public void setRedeemOutlet(String sOutletCode) {
		m_sRedeemOutlet = sOutletCode;
	}
	
	public void setRedeemCheck(int iCheckNum) {
		m_sRedeemCheck = String.valueOf(iCheckNum);
	}
	
	public void setRedeemItem(String sRedeemItem) {
		m_sRedeemItem = sRedeemItem;
	}
	
	public void setEmployeeId(int iEmployeeId) {
		m_sEmployeeId = String.valueOf(iEmployeeId);
	}
	
	public void setEmployeeName(String sEmployeeName) {
		m_sEmployeeName = sEmployeeName;
	}
}
