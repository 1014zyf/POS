package app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringEscapeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import externallib.StringLib;
import om.PosCheckExtraInfoList;
import om.PosCheckGratuity;
import om.InfInterface;
import om.InfVendor;
import om.PosActionPrintQueue;
import om.PosCheck;
import om.PosCheckDiscount;
import om.PosCheckExtraInfo;
import om.PosCheckItem;
import om.PosCheckPayment;
import om.PosInterfaceConfig;
import om.PosPaymentMethod;
import sun.misc.BASE64Encoder;

public class FuncMembershipInterface {
	class LpsSvcPostingInfo {
		int iInterfaceId;
		String sRequestCode;
		int iOutletId;
		String sOutletCode;
		String sHotelCode;
		String sTraceId;
		String sStationId;
		String sStationCode;
		String sMemberNumber;
		String sMemberName = "";
		String sMemberLastName;
		String sMemberType = "";
		String sCheckNumber;
		BigDecimal dLocalBalance;
		BigDecimal dAccountBalance;
		String sPassword;
		BigDecimal dPointsBalance;
		int iCover;
		HashMap<String, BigDecimal> oDiscountList;
		HashMap<String, String> oCouponList;
		String sResponseCode;
		BigDecimal dPaymentAmount;
		int iPaymentId;
		String sPaymentName;
		String sEmployeeNumber;
		BigDecimal dTips;
		String sDate;
		String sTime;
		BigDecimal dPreviousPaymentTotal;
		int iPointPosting;
		String sArAccountNumber = "";
		String sCardNo = "";
		String sCardStatus = "";
		String sPaymentSequence = "";
		String sPostingKey = "";
		String sPointUsed = "";
		String sPointUsedByDiscount = "";
		BigDecimal dPointPaymentTotal;
		ArrayList<HashMap<String, String>> oVoucherList;
		ArrayList<HashMap<String, String>> oItemVoucherList;
		ArrayList<HashMap<String, String>> oItemList;
		String sPomotionCodes = "";
		BigDecimal dStoreValuePaymentTotal;
		int iPaymentType;
		String sReferenceNo = "";
		String sVoucherAmount = "";
		String sVoucherType = "";
		String sPaymentType = "";
		String sBusinessDayId = "";
		String sEmployeeName = "";
		int iCheckPaymnetIndex = 0;
		String sCurrencyCode = "";
		String sCurrencySign = "";
		String sGuestNumber = "";
		String sGuestName = "";
		String sExpiryDate = "";
		String sFieldNumber = "";
		String sCheckGroupNumber = "";
		String sTable = "";
		String sServingPeriod = "";
		BigDecimal dPostAmount;
		BigDecimal dPreviousPaymentAmount;
		String sInterfaceVendorKey = "";
		String sBusinessDate = "";
		String sRoomNumber = "";
		boolean bIsFromPms = false;
		String sSessionId = "";
		ArrayList<PosCheckPayment> oPreviousCheckPayments;
		String sFacilityCode;
		String sFacilityTypeCode;
		String sPostingTimeZone;
		String sTransactionTime;
		String sRedemptionAmount;
		String sAwardCode = "";
		String sPointRedeem = "";
		String sCheckDateTime = "";
		int iAttributeId;
	}
	
	class MemberInterfaceResponseInfo {
		String sActivityCode = "";
		String sAddress = "";
		String sAwardCode = "";
		String sAllowCharge = "";
		String sHotelCode = "";
		String sTraceId = "";
		String sTerminalId = "";
		String sResponseCode = "";
		String sAmount = "";
		String sTipAmount = "";
		String sCustomerNumber = "";
		String sMemberNumber = "";
		String sMemberName = "";
		String sMemberFirstName = "";
		String sMemberLastName = "";
		String sMemberEthnicity = "";
		String sMemberStatus = "";
		String sMemberType = "";
		String sBirthday = "";
		String sEmail = "";
		String sGender = "";
		String sNationality = "";
		String[] sPrintLines;
		String sEnglishName = "";
		String sCardAge = "";
		String sCardAlias = "";
		String sCardTypeName = "";
		String sCardLevelName = "";
		String sCardNumber = "";
		String sCardStatus = "";
		String sArAccountNumber = "";
		String sPhotoFileName = "";
		String sSignatureFileName = "";
		String sExpiryDate = "";
		String sOverdarftSpend = "";
		String sPointsBalance = "";
		String sPoints = "";
		String sPointEarned = "";
		String sPointsType = "";
		String sPointsRate = "";
		String sPointsRedeem = "";
		String sPointsAmount = "";
		String sLocalBalance = "";
		String sAccountBalance = "";
		String sDisplayMessage = "";
		String sCreditAllowance = "";
		String sCreditUsage = "";
		String sCreditLimit = "";
		String sFoodDisc = "";
		String sBevDisc = "";
		String sMiscDisc = "";
		ArrayList<String> sCouponList;
		String sCouponAmount = "";
		String sCouponCost = "";
		String sCouponItemCode = "";
		String sPostingKey = "";
		String sTotalPointsBalance = "";
		String sNRIC = ""; 
		String sPassword = "";
		String sMobileNumber = "";
		ArrayList<String> sCardList;
		ArrayList<HashMap<Integer, BigDecimal>> oAwardMappingList;
		String sHotelCurrency = "";
		String sCardListNumber = "";
		String sCardListStatus = "";
		String sCardListExpiryDate = "";
		String sCardListPointBalance = "";
		String sCardListTotalPointBalance = "";
		ArrayList<String> sVoucherList;
		String sDollarToPointsRatio = "";
		String sTier = "";
		String sSalutation = "";
		String sRemark = "";
		String sEventOrderNumber = "";
		String sEventOrderDeposit = "";
		HashMap<String, String> oMemberInfoList;
		String sJoinDate = "";
		String sStoreValueBalance = "";
		String sReference = "";
		String sIdentiferType = "";
		String sSalesNumber = "";
		String sEnrolmentCode = "";
		ArrayList<MemberInterfacePointInfo> oPointInfoList;
		String sReceiptMessage = "";
		String sCashierMessage = "";
		String sBonusExpiryDate = "";
		String sDebCardBalance = "";
		String sPostingString = "";
		String sDiscountCode = "";
		ArrayList<String> sBenefitList;
		String sCity = "";
		String sStateRegion = "";
		String sPostalCode = "";
		String sCountry = "";
		String sPromotions = "";
		
		String sSessionId = "";
		ArrayList<MemberInterfaceVoucherListInfo> oGiftList;
		ArrayList<MemberInterfaceVoucherListInfo> oCouponList;

		String sAmountEarned = "";
		String sPointsRefunded = "";
		String sPointsReturned = "";
		String sAwardCancellationNumber = "";
	}
	
	class MemberInterfacePointEarnInfo {
		int iInterfaceId = 0;
		String sOutletCode = "";
		int iOutletId = 0;
		String sEmployeeNumber = "";
		String sStationCode = "";
		String sBusinessDate = "";
		String sTraceId = "";
		HashMap<Integer, String> oBonusCodes = null;
		String sOpenLocDateTime = "";
		String sCheckNumber = "";
		String sLastName = "";
		String sMemberNumber = "";
		String sFacilityCode = "";
		String sFacilityTypeCode = "";
		String sSessionId = "";
		String sTimeZone = "";
		ArrayList<PosCheckPayment> oPreviousCheckPayments;
	}
	
	class MemberInterfacePointInfo {
		String sPointType = "";
		BigDecimal dPointAmount = BigDecimal.ZERO;
		String sPointCurrency = "";
		BigDecimal dPointRate = BigDecimal.ZERO;
		String sPointDescription = "";
	}
	
	public class MemberInterfaceVoucherListInfo{
		String sId = "";
		boolean bCoupon = true;
		String sCode ="";
		String sName = "";
		BigDecimal dBonus = BigDecimal.ZERO;
		BigDecimal dCount = BigDecimal.ZERO;
		String sType = "";
		BigDecimal dValue = BigDecimal.ZERO;
		DateTime oExpiryDate;

		MemberInterfaceVoucherListInfo(JSONObject oInfoJSONObject, boolean coupon) {
			bCoupon = coupon;
			String sPrefix;
			if (bCoupon) {
				sPrefix = "coupon_";
				sName = oInfoJSONObject.optString("name_curlang");
				dCount = new BigDecimal(oInfoJSONObject.optDouble("count"));
				oExpiryDate = new DateTime(oInfoJSONObject.optString("expiry_date"));
			} else {
				sPrefix = "gift_";
				sName = oInfoJSONObject.optString("name_cht");
				dBonus = new BigDecimal(oInfoJSONObject.optDouble("bonus"));
				dCount = new BigDecimal(oInfoJSONObject.optDouble(sPrefix+"count"));
				oExpiryDate = null;
			
			}
			sId = oInfoJSONObject.optString(sPrefix+"id");
			sCode = oInfoJSONObject.optString("product_code");
			if(oInfoJSONObject.optString(sPrefix+"type").equals("item"))
				sType = AppGlobal.g_oLang.get()._("item");
			else if(oInfoJSONObject.optString(sPrefix+"type").equals("discount"))
				sType = AppGlobal.g_oLang.get()._("discount");
			else if(oInfoJSONObject.optString(sPrefix+"type").equals("cash"))
				sType = AppGlobal.g_oLang.get()._("cash");
			else 
				sType = oInfoJSONObject.optString(sPrefix+"type");
		
			dValue = new BigDecimal(oInfoJSONObject.optDouble(sPrefix+"value"));
		}
	}
	static public String PAY_CHECK_SUCCESS = "s";
	static public String PAY_CHECK_FAIL = "f";
	
	public static int MEMBERSHIP_VOUCHER_TYPE_ALL = 0;
	public static int MEMBERSHIP_VOUCHER_TYPE_CASH = 1;
	public static int MEMBERSHIP_VOUCHER_TYPE_NON_CASH = 2;
	
	public final static String MEMBERSHIP_VOUCHER_TYPE_GIFT = "g";
	public final static String MEMBERSHIP_VOUCHER_TYPE_COUPON = "c";
	// Concept interface type
	public static int CONCEPT_TYPE_BLOCKED 		= 0;
	public static int CONCEPT_TYPE_ACTIVE 		= 1;
	public static int CONCEPT_TYPE_CANCELLED 	= 2;
	public static int CONCEPT_TYPE_DISABLE 		= 3;
	public static int CONCEPT_TYPE_END_OF_MEMBERSHIP = 4;
	
	private PosInterfaceConfig m_oMembershipInterface;
	private String m_sLastErrorMessage;
	private String m_sSpecificCoupon; 
	private boolean m_bConnectError;
	
	/**************************
	 * 	Device Manager Setup
	 **************************/
	private int m_iDeviceManagerInterfaceId;
	
	// Support flag
	private boolean m_bSupportMemberCard;
	
	// Model Type
	private String m_sModelType;
	
	// Timeout setting
	private int m_iTimeout;
	
	public MemberInterfaceResponseInfo m_oLastLpsSvcResponseInfo;
	private List<MemberInterfaceResponseInfo> m_oResponseMemberList;
	
	public FuncMembershipInterface(PosInterfaceConfig oMembershipInterface) {
		m_oMembershipInterface = oMembershipInterface;
		m_sLastErrorMessage = "";
		m_sSpecificCoupon = "";
		m_bConnectError = false;
		m_oResponseMemberList = null;
		
		m_iDeviceManagerInterfaceId = 0;
		m_bSupportMemberCard = false;
		m_sModelType = "";
		m_iTimeout = 15000;
		
		if (oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_CHINETEK))
			readDeviceManagerSetup();
	}
	
	public PosInterfaceConfig getMembershipInterface() {
		return m_oMembershipInterface;
	}
	
	// member Enquiry
	public boolean memberEnquiry(HashMap<String, String> oEnquiryInfo) {
		m_bConnectError = false;
		JSONObject oResultJSONObject = new JSONObject();
		
		//Generate the enquiry information
		oEnquiryInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oEnquiryInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oEnquiryInfo.put("stationCode", AppGlobal.g_oFuncStation.get().getStation().getCode());
		oEnquiryInfo.put("employeeNum", AppGlobal.g_oFuncUser.get().getUserNumber());
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_LPS_SVC)) {
			String sEncryptedPwd = this.getMd5String("UTF-8", oEnquiryInfo.get("password"));
			oEnquiryInfo.put("password", String.valueOf(sEncryptedPwd));
		}else if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2))
			oEnquiryInfo.put("sessionId", AppGlobal.getInterfaceSession(m_oMembershipInterface.getInterfaceId()));
		oEnquiryInfo.put("timeZone", String.valueOf(AppGlobal.g_oFuncOutlet.get().getShop().getTimezone()));
		
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_2700)) {
			//Generate the enquiry information
			String sUserNumber = AppGlobal.g_oFuncUser.get().getUserNumber();
			oEnquiryInfo.put("employeeNumber", sUserNumber);
			
			//2700 membership enquiry
			FuncMembershipInterfaceSerialPort oFuncMembershipInterfaceSerialPort = new FuncMembershipInterfaceSerialPort();
			oFuncMembershipInterfaceSerialPort.init(m_oMembershipInterface);
			oResultJSONObject = oFuncMembershipInterfaceSerialPort.doMembershipEnquiry(oEnquiryInfo);
			
			if (oResultJSONObject == null || !oResultJSONObject.has("enquiryResult"))
				return false;
			if (!oResultJSONObject.optBoolean("enquiryResult")) {
				if (oResultJSONObject.optInt("errorCode") > 0 && oResultJSONObject.optInt("errorCode") < 25)
					m_sLastErrorMessage = getErrorMessage(oResultJSONObject.optInt("errorCode", 0));
				else if(oResultJSONObject.optInt("errorCode") > 24)
					m_sLastErrorMessage = oResultJSONObject.optString("errorMessage", "");
				return false;
			} else {
				if (oResultJSONObject.isNull("memberInfo"))
					return false;
				
				if(oResultJSONObject.has("memberInfo")) {
					m_oLastLpsSvcResponseInfo = new MemberInterfaceResponseInfo();
					m_oLastLpsSvcResponseInfo.oMemberInfoList = new HashMap<String, String>();
					JSONObject oInfoJSONObject = oResultJSONObject.optJSONObject("memberInfo");
					
					for(int i = 1; i <= oInfoJSONObject.length(); i++)
						m_oLastLpsSvcResponseInfo.oMemberInfoList.put("memberInfo"+i, oInfoJSONObject.optString("memberInfo"+i));
					
					if (m_oMembershipInterface.getInterfaceConfig().has("share_folder_setup")) {
						String sFileName = trimTargetCharacter(oEnquiryInfo.get("memberNumber"), "/.\\");
						
						// no member photo file name --> default use card number
						// have photo file name but no member number index --> use memberInfo2
						// have both --> use memberInfo + iMemberNumberIndex
						int iMemberNumberIndex = 2;
						if(m_oMembershipInterface.getInterfaceConfig().has("general_setup") && 
								m_oMembershipInterface.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").has("member_info_index")) {
							iMemberNumberIndex = m_oMembershipInterface.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").optJSONObject("member_info_index").optInt("value");
						}
						
						if (iMemberNumberIndex != 0 && oInfoJSONObject.has("memberInfo" + iMemberNumberIndex)
								&& m_oMembershipInterface.getInterfaceConfig().optJSONObject("share_folder_setup").optJSONObject("params").has("member_photo_file_name")) {
							String sMemberPhotoSetting = m_oMembershipInterface.getInterfaceConfig().optJSONObject("share_folder_setup").optJSONObject("params").optJSONObject("member_photo_file_name").optString("value");
							if (!sMemberPhotoSetting.isEmpty() && sMemberPhotoSetting.equals("server"))
								sFileName = oInfoJSONObject.optString("memberInfo" + iMemberNumberIndex);
						}
						
						if(sFileName == null || sFileName.trim().isEmpty())
							return true;
						String sMemberImage = imageReader(sFileName, "member_photo_share_folder_path");
						if (!sMemberImage.isEmpty())
							m_oLastLpsSvcResponseInfo.oMemberInfoList.put("memberPhoto", sMemberImage);
					}
					return true;
				}
			}
		}
		
		oResultJSONObject = m_oMembershipInterface.memberEnquiry(oEnquiryInfo);
		m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();	
		if(oResultJSONObject == null) {
			m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
			return false;
		}
		
		if(!oResultJSONObject.has("enquiryResult")) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("result_error");
			return false;
		}
		
		if(oResultJSONObject.optBoolean("enquiryResult", false) == false) {
			if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL) &&
				(oResultJSONObject.optInt("errorCode") == 6 || oResultJSONObject.optInt("errorCode") == 8))
				m_bConnectError = true;
			if(oResultJSONObject.optInt("errorCode", 0) == 0)
				m_sLastErrorMessage = oResultJSONObject.optString("errorMessage");
			else
				m_sLastErrorMessage = getErrorMessage(oResultJSONObject.optInt("errorCode", 0));
			return false;
		}
		
		// Get and update session id if needed
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2) && oResultJSONObject.has("sessionId")) {
			String sSession = oResultJSONObject.optString("sessionId", "");
			if(!sSession.isEmpty())
				AppGlobal.generateUpdateInterfaceSession(m_oMembershipInterface.getInterfaceId(), sSession);
		}
		JSONArray oResultJSONArray = oResultJSONObject.optJSONArray("memberDetails");
		if(oResultJSONArray == null || oResultJSONArray.length() == 0) {
			m_oLastLpsSvcResponseInfo = new MemberInterfaceResponseInfo();
			m_oLastLpsSvcResponseInfo.sPrintLines = new String[2];
			m_oLastLpsSvcResponseInfo.sCouponList = new ArrayList<String>();
			m_oLastLpsSvcResponseInfo.oMemberInfoList = new HashMap<String, String>();
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_such_member");
			return false;
		}
			
		JSONObject oMemberJSONObject = oResultJSONArray.optJSONObject(0);
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_LPS_SVC)) {
			if(!oMemberJSONObject.optString("responseCode", "").equals("A")) {
				if(!oMemberJSONObject.optString("displayMessage", "").isEmpty())
					m_sLastErrorMessage = oMemberJSONObject.optString("displayMessage");
				else
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("membership_system_error");
				return false;
			}
		} else if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_LPS_SVC_COUPON)) {
			if(!oMemberJSONObject.optString("responseCode", "").equals("A")) {
				if(!oMemberJSONObject.optString("displayMessage", "").isEmpty())
					m_sLastErrorMessage = oMemberJSONObject.optString("displayMessage");
				else
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("membership_system_error");
				return false;
			}
		}
		
		m_oLastLpsSvcResponseInfo = new MemberInterfaceResponseInfo();
		m_oLastLpsSvcResponseInfo.sPrintLines = new String[2];
		m_oLastLpsSvcResponseInfo.sCouponList = new ArrayList<String>();
		m_oLastLpsSvcResponseInfo.oMemberInfoList = new HashMap<String, String>();
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_SMART_INTEGRAL) ) {
			m_oLastLpsSvcResponseInfo.sCardNumber = oMemberJSONObject.optString("cardNumber");
			m_oLastLpsSvcResponseInfo.sMemberNumber = oMemberJSONObject.optString("memberNumber");
			m_oLastLpsSvcResponseInfo.sMemberName = oMemberJSONObject.optString("memberName", "");
			m_oLastLpsSvcResponseInfo.sMemberType = oMemberJSONObject.optString("memberType", "");
			m_oLastLpsSvcResponseInfo.sExpiryDate = oMemberJSONObject.optString("expiryDate", "");
			m_oLastLpsSvcResponseInfo.sMemberStatus = oMemberJSONObject.optString("status", "");
			m_oLastLpsSvcResponseInfo.sRemark = oMemberJSONObject.optString("sRemark", "");
			m_oLastLpsSvcResponseInfo.sAccountBalance = oMemberJSONObject.optString("accountBalance", "");
			m_oLastLpsSvcResponseInfo.sMiscDisc = oMemberJSONObject.optString("miscDisc", "100");
			m_oLastLpsSvcResponseInfo.sPointsBalance = oMemberJSONObject.optString("pointBalance", "");
			m_oLastLpsSvcResponseInfo.sLocalBalance = oMemberJSONObject.optString("localBalance", "");
		} else if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_HUARUNTONG)){
			// Hua Run Tong
			m_oLastLpsSvcResponseInfo.sMemberNumber = oMemberJSONObject.optString("memberNumber", "");
			m_oLastLpsSvcResponseInfo.sMemberName = oMemberJSONObject.optString("memberName", "");
			m_oLastLpsSvcResponseInfo.sCardNumber = oMemberJSONObject.optString("cardNumber", "");
			m_oLastLpsSvcResponseInfo.sTier = oMemberJSONObject.optString("memberTier", "");
			m_oLastLpsSvcResponseInfo.sMemberStatus = oMemberJSONObject.optString("memberStatus", "");
			m_oLastLpsSvcResponseInfo.sPointsBalance = oMemberJSONObject.optString("pointsBalance", "");
		} else if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2)){
			if(!oEnquiryInfo.containsKey("memberIndex"))
				m_oResponseMemberList = new ArrayList<MemberInterfaceResponseInfo>();
			m_oLastLpsSvcResponseInfo.sAddress = oMemberJSONObject.optString("address");
			m_oLastLpsSvcResponseInfo.sPointsBalance = oMemberJSONObject.optString("pointsBalance");
			m_oLastLpsSvcResponseInfo.sMemberNumber = oMemberJSONObject.optString("memberNumber");
			m_oLastLpsSvcResponseInfo.sMemberFirstName = oMemberJSONObject.optString("firstName");
			m_oLastLpsSvcResponseInfo.sMemberLastName = oMemberJSONObject.optString("lastName");
			m_oLastLpsSvcResponseInfo.sTier = oMemberJSONObject.optString("tier");
			m_oLastLpsSvcResponseInfo.sExpiryDate = oMemberJSONObject.optString("tierExpiryDate");
			m_oLastLpsSvcResponseInfo.sEmail = oMemberJSONObject.optString("email");
			
			JSONArray oTmpJsonArray = null;
			JSONObject oTmpJsonObj = null;
			String sDisplay = "name";
			m_oLastLpsSvcResponseInfo.oMemberInfoList = new HashMap<>();
			
			if(oMemberJSONObject.has("address") && oMemberJSONObject.optJSONObject("address") != null) {
				oTmpJsonObj = oMemberJSONObject.optJSONObject("address");
				if(oTmpJsonObj.has("Line1") && !oTmpJsonObj.optString("Line1").isEmpty())
					m_oLastLpsSvcResponseInfo.oMemberInfoList.put("address1", oTmpJsonObj.optString("Line1"));
				if(oTmpJsonObj.has("Line2") && !oTmpJsonObj.optString("Line2").isEmpty())
					m_oLastLpsSvcResponseInfo.oMemberInfoList.put("address2", oTmpJsonObj.optString("Line2"));
				if(oTmpJsonObj.has("Line3") && !oTmpJsonObj.optString("Line3").isEmpty())
					m_oLastLpsSvcResponseInfo.oMemberInfoList.put("address3", oTmpJsonObj.optString("Line3"));
				if(oTmpJsonObj.has("city") && !oTmpJsonObj.optString("city").isEmpty())
					m_oLastLpsSvcResponseInfo.oMemberInfoList.put("address4", oTmpJsonObj.optString("city"));
				if(oTmpJsonObj.has("stateRegion") && !oTmpJsonObj.optString("stateRegion").isEmpty())
					m_oLastLpsSvcResponseInfo.oMemberInfoList.put("address5", oTmpJsonObj.optString("stateRegion"));
				if(oTmpJsonObj.has("postalCode") && !oTmpJsonObj.optString("postalCode").isEmpty())
					m_oLastLpsSvcResponseInfo.oMemberInfoList.put("address6", oTmpJsonObj.optString("postalCode"));
				if(oTmpJsonObj.has("country") && !oTmpJsonObj.optString("country").isEmpty())
					m_oLastLpsSvcResponseInfo.oMemberInfoList.put("address7", oTmpJsonObj.optString("country"));
			}
			
			if(oMemberJSONObject.has("facilities") && oMemberJSONObject.optJSONArray("facilities") != null){
				oTmpJsonArray = oMemberJSONObject.optJSONArray("facilities");
				for(int i = 0; i < oTmpJsonArray.length(); i++){
					if(oTmpJsonArray.optJSONObject(i).has("name") && !oTmpJsonArray.optJSONObject(i).optString("name").isEmpty())
						sDisplay = "name";
					else if(oTmpJsonArray.optJSONObject(i).has("code") && !oTmpJsonArray.optJSONObject(i).optString("code").isEmpty())
						sDisplay = "code";
					else
						continue;
					//m_oLastLpsSvcResponseInfo.oMemberInfoList.put("affiliationCode"+i, oTmpJsonArray.optJSONObject(i).optString("code"));
					m_oLastLpsSvcResponseInfo.oMemberInfoList.put("affiliationName"+i,oTmpJsonArray.optJSONObject(i).optString(sDisplay));
					//m_oLastLpsSvcResponseInfo.oMemberInfoList.put("affiliationType"+i, oTmpJsonArray.optJSONObject(i).optString("type"));
					//m_oLastLpsSvcResponseInfo.oMemberInfoList.put("affiliationPropertyCode"+i,oTmpJsonArray.optJSONObject(i).optString("propertyCode"));
				}
			}
			
			if(oMemberJSONObject.has("promotions") && oMemberJSONObject.optJSONArray("promotions") != null){
				oTmpJsonArray = oMemberJSONObject.optJSONArray("promotions");
				for(int i = 0; i < oTmpJsonArray.length(); i++){
					if(oTmpJsonArray.optJSONObject(i).has("name") && !oTmpJsonArray.optJSONObject(i).optString("name").isEmpty())
						sDisplay = "name";
					else if(oTmpJsonArray.optJSONObject(i).has("code") && !oTmpJsonArray.optJSONObject(i).optString("code").isEmpty())
						sDisplay = "code";
					else
						continue;
					//m_oLastLpsSvcResponseInfo.oMemberInfoList.put("promotionCode"+i, oTmpJsonArray.optJSONObject(i).optString("code"));
					m_oLastLpsSvcResponseInfo.oMemberInfoList.put("promotionName"+i, oTmpJsonArray.optJSONObject(i).optString(sDisplay));
				}
			}
			if(!oEnquiryInfo.containsKey("memberIndex"))
				m_oResponseMemberList.add(m_oLastLpsSvcResponseInfo);

		} else if(!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASCENTIS_CRM) ) {
			m_oLastLpsSvcResponseInfo.sTraceId = oMemberJSONObject.optString("traceID", "");
			m_oLastLpsSvcResponseInfo.sTerminalId = oMemberJSONObject.optString("terminalID", "");
			m_oLastLpsSvcResponseInfo.sMemberNumber = oMemberJSONObject.optString("memberNumber", "");
			m_oLastLpsSvcResponseInfo.sMemberStatus = oMemberJSONObject.optString("memberStatus", "");
			m_oLastLpsSvcResponseInfo.sMemberName = oMemberJSONObject.optString("memberName", "");
			m_oLastLpsSvcResponseInfo.sMemberType = oMemberJSONObject.optString("memberType", "");
			m_oLastLpsSvcResponseInfo.sCardNumber = oMemberJSONObject.optString("cardNumber", "");
			m_oLastLpsSvcResponseInfo.sCardStatus = oMemberJSONObject.optString("cardStatus", "");
			m_oLastLpsSvcResponseInfo.sCardTypeName = oMemberJSONObject.optString("cardType", "");
			m_oLastLpsSvcResponseInfo.sCardLevelName = oMemberJSONObject.optString("cardLevelName", "");
			m_oLastLpsSvcResponseInfo.sArAccountNumber = oMemberJSONObject.optString("arAccountNumber", "");
			m_oLastLpsSvcResponseInfo.sBirthday = oMemberJSONObject.optString("birthday", "");
			m_oLastLpsSvcResponseInfo.sExpiryDate = oMemberJSONObject.optString("expiryDate", "");
			m_oLastLpsSvcResponseInfo.sLocalBalance = oMemberJSONObject.optString("localBalance", "");
			m_oLastLpsSvcResponseInfo.sAccountBalance = oMemberJSONObject.optString("accountBalance", "");
			m_oLastLpsSvcResponseInfo.sPointsBalance = oMemberJSONObject.optString("pointsBalance", "");
			m_oLastLpsSvcResponseInfo.sCreditAllowance = oMemberJSONObject.optString("creditAllowance", "");
			m_oLastLpsSvcResponseInfo.sCreditLimit = oMemberJSONObject.optString("creditLimit", "");
			m_oLastLpsSvcResponseInfo.sCreditUsage = oMemberJSONObject.optString("creditUsage", "");
			m_oLastLpsSvcResponseInfo.sCouponAmount = oMemberJSONObject.optString("couponAmount", "");
			m_oLastLpsSvcResponseInfo.sCouponCost = oMemberJSONObject.optString("couponCost", "");
			m_oLastLpsSvcResponseInfo.sCouponItemCode = oMemberJSONObject.optString("couponItemCode", "");
			if(oMemberJSONObject.has("discount")) {
				m_oLastLpsSvcResponseInfo.sFoodDisc = oMemberJSONObject.optJSONObject("discount").optString("foodDiscount", "");
				m_oLastLpsSvcResponseInfo.sBevDisc = oMemberJSONObject.optJSONObject("discount").optString("beveDiscount", "");
				m_oLastLpsSvcResponseInfo.sMiscDisc = oMemberJSONObject.optJSONObject("discount").optString("miscDiscount", "");
			}
				
			//photo handling
			if(oMemberJSONObject.has("photoFileName"))
				m_oLastLpsSvcResponseInfo.sPhotoFileName = imageReader(oMemberJSONObject.optString("photoFileName", ""), "member_photo_share_folder_path");
			if(oMemberJSONObject.has("signatureFileName"))
				m_oLastLpsSvcResponseInfo.sSignatureFileName = imageReader(oMemberJSONObject.optString("signatureFileName", ""), "signature_share_folder_path");
				
			//coupon list
			if(oResultJSONObject.has("coupons")) {
				for(int i=0; i<oResultJSONObject.optJSONArray("coupons").length(); i++) {
					JSONObject oCouponJSON = oResultJSONObject.optJSONArray("coupons").optJSONObject(i);
					m_oLastLpsSvcResponseInfo.sCouponList.add(oCouponJSON.optString("name")+"("+oCouponJSON.optString("type")+") X "+oCouponJSON.optString("numberOfCoupon"));
				}
			}
				
			//member info list
			if(oResultJSONObject.has("memberInfo")) {
				for(int i=0; i<oResultJSONObject.optJSONArray("memberInfo").length(); i++) {
					JSONObject oMemberInfoJSON = oResultJSONObject.optJSONArray("memberInfo").optJSONObject(i);
					m_oLastLpsSvcResponseInfo.oMemberInfoList.put("memberInfo"+i, oMemberInfoJSON.optString("memberInfo"));
				}
			}
		}else {
			//Ascentis CRM
			m_oLastLpsSvcResponseInfo.sMemberNumber = oMemberJSONObject.optString("memberNumber", "");
			m_oLastLpsSvcResponseInfo.sMemberName = oMemberJSONObject.optString("memberName", "");
			m_oLastLpsSvcResponseInfo.sNRIC = oMemberJSONObject.optString("nric", "");
			m_oLastLpsSvcResponseInfo.sPassword = oMemberJSONObject.optString("password", "");
			m_oLastLpsSvcResponseInfo.sBirthday = oMemberJSONObject.optString("birthday", "");
			m_oLastLpsSvcResponseInfo.sMobileNumber = oMemberJSONObject.optString("mobileNumber", "");
			m_oLastLpsSvcResponseInfo.sCardList = new ArrayList<String>();
				
			//card list
			if(oMemberJSONObject.has("cardLists")) {
				for(int i=0; i<oMemberJSONObject.optJSONArray("cardLists").length(); i++) {
					JSONObject oCardJSON = oMemberJSONObject.optJSONArray("cardLists").optJSONObject(i);
					String stmpFormat = String.format("%s,%s,%s,%s,%s", oCardJSON.optString("cardNumber", " "), 
							oCardJSON.optString("cardStatus", " "), oCardJSON.optString("cardExpiryDate", " "),
							oCardJSON.optString("cardPointsBalance", " "), oCardJSON.optString("cardTotalPointsBalance", " "));
					m_oLastLpsSvcResponseInfo.sCardList.add(stmpFormat);
				}
			}
		}
		return true;
	}
	
	public boolean cardEnquiry(HashMap<String, String> oEnquiryInfo) {
		m_bConnectError = false;
		
		//Generate the enquiry information
		oEnquiryInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oEnquiryInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oEnquiryInfo.put("stationCode", AppGlobal.g_oFuncStation.get().getStation().getCode());
		oEnquiryInfo.put("employeeNum", AppGlobal.g_oFuncUser.get().getUserNumber());
		
			JSONObject oResultJSONObject = m_oMembershipInterface.cardEnquiry(oEnquiryInfo);

			m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();	
			if(oResultJSONObject == null) {
				m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
				return false;
			}
			
			if(!oResultJSONObject.has("enquiryResult")) {
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("result_error");
				return false;
			}
			
			if(oResultJSONObject.optBoolean("enquiryResult", false) == false) {
				if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN) &&
						(oResultJSONObject.optInt("errorCode") == 6 || oResultJSONObject.optInt("errorCode") == 8))
					m_bConnectError = true;
				if(oResultJSONObject.optInt("errorCode", 0) == 0)
					m_sLastErrorMessage = oResultJSONObject.optString("errorMessage");
				else
					m_sLastErrorMessage = getErrorMessage(oResultJSONObject.optInt("errorCode", 0));
				return false;
			}
			
			JSONObject oMemberDetailObject = oResultJSONObject.optJSONObject("memberDetails");
			
			if(oMemberDetailObject == null || oMemberDetailObject.length() == 0) {
				m_oLastLpsSvcResponseInfo = new MemberInterfaceResponseInfo();
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_such_member");
				return true;
			}

		m_oLastLpsSvcResponseInfo = new MemberInterfaceResponseInfo();
		m_oLastLpsSvcResponseInfo.sActivityCode = oMemberDetailObject.optString("activityCode", "");
		m_oLastLpsSvcResponseInfo.sAddress = oMemberDetailObject.optString("address", "");
		m_oLastLpsSvcResponseInfo.sAllowCharge= oMemberDetailObject.optString("allowCharge", "");
		m_oLastLpsSvcResponseInfo.sMemberEthnicity = oMemberDetailObject.optString("memberEthnicity", "");
		m_oLastLpsSvcResponseInfo.sMemberNumber = oMemberDetailObject.optString("memberNumber", "");
		m_oLastLpsSvcResponseInfo.sMemberLastName = oMemberDetailObject.optString("memberLastName", "");
		m_oLastLpsSvcResponseInfo.sMemberName = oMemberDetailObject.optString("memberName", "");
		m_oLastLpsSvcResponseInfo.oMemberInfoList = new HashMap<String, String>();
		if(oMemberDetailObject.has("memberInfo")){
			for(int i=1; i<=oMemberDetailObject.optJSONArray("memberInfo").length(); i++) {
				String sMemberInfo = "";
				try {
					sMemberInfo = oMemberDetailObject.optJSONArray("memberInfo").getString(i-1);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				m_oLastLpsSvcResponseInfo.oMemberInfoList.put("memberInfo"+i, sMemberInfo);
			}
		}
		m_oLastLpsSvcResponseInfo.sNRIC = oMemberDetailObject.optString("nric", "");
		m_oLastLpsSvcResponseInfo.sPassword = oMemberDetailObject.optString("password", "");
		m_oLastLpsSvcResponseInfo.sBirthday = oMemberDetailObject.optString("birthday", "");
		m_oLastLpsSvcResponseInfo.sMobileNumber = oMemberDetailObject.optString("mobileNumber", "");
		m_oLastLpsSvcResponseInfo.sCardAge = oMemberDetailObject.optString("cardAge", "");
		m_oLastLpsSvcResponseInfo.sCardAlias = oMemberDetailObject.optString("cardAlias", "");
		m_oLastLpsSvcResponseInfo.sCardNumber = oMemberDetailObject.optString("cardNumber", "");
		m_oLastLpsSvcResponseInfo.sPointsBalance = oMemberDetailObject.optString("cardPointsBalance", "");
		m_oLastLpsSvcResponseInfo.sTotalPointsBalance = oMemberDetailObject.optString("cardTotalPointsBalance", "");
		m_oLastLpsSvcResponseInfo.sDollarToPointsRatio = oMemberDetailObject.optString("dollarToPointsRatio", "");
		m_oLastLpsSvcResponseInfo.sGender = oMemberDetailObject.optString("gender", "");
		m_oLastLpsSvcResponseInfo.sMemberStatus = oMemberDetailObject.optString("memberStatus", "");
		m_oLastLpsSvcResponseInfo.sMemberType = oMemberDetailObject.optString("memberType", "");
		m_oLastLpsSvcResponseInfo.sExpiryDate = oMemberDetailObject.optString("expiryDate", "");
		m_oLastLpsSvcResponseInfo.sSalutation = oMemberDetailObject.optString("salutation", "");
		m_oLastLpsSvcResponseInfo.sDiscountCode = oMemberDetailObject.optString("discountCode", "");

		m_oLastLpsSvcResponseInfo.sVoucherList = new ArrayList<String>();
		m_oLastLpsSvcResponseInfo.sBenefitList = new ArrayList<String>();

		//last Name
		if(oMemberDetailObject.has("memberLastName"))
			m_oLastLpsSvcResponseInfo.sMemberLastName = oMemberDetailObject.optString("memberLastName", "");
		
		if (m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GOLDEN_CIRCLE)) {
			int iRoundingDecimal = 0;
			if(m_oMembershipInterface.getInterfaceConfig().has("currency_setup") && m_oMembershipInterface.getInterfaceConfig().optJSONObject("currency_setup").optJSONObject("params").has("rounding_decimal"))
				iRoundingDecimal = m_oMembershipInterface.getInterfaceConfig().optJSONObject("currency_setup").optJSONObject("params").optJSONObject("rounding_decimal").optInt("value", 0);
			BigDecimal dLocalBalance = new BigDecimal(oMemberDetailObject.optString("convertedAmountLocalCurrency", "0"));
			BigDecimal dTotalPointsBalance = new BigDecimal(oMemberDetailObject.optString("convertedAmountLocalCurrencyInCurrYear", "0"));
			dLocalBalance = dLocalBalance.setScale(iRoundingDecimal, BigDecimal.ROUND_HALF_UP);
			dTotalPointsBalance = dTotalPointsBalance.setScale(iRoundingDecimal, BigDecimal.ROUND_HALF_UP);
			
			m_oLastLpsSvcResponseInfo.sMemberFirstName = oMemberDetailObject.optString("firstName", "");
			m_oLastLpsSvcResponseInfo.sMemberLastName = oMemberDetailObject.optString("lastName", "");
			m_oLastLpsSvcResponseInfo.sMemberNumber = oMemberDetailObject.optString("memberNumber", "");
			m_oLastLpsSvcResponseInfo.sTier = oMemberDetailObject.optString("memberType", "");
			m_oLastLpsSvcResponseInfo.sEnrolmentCode = oMemberDetailObject.optString("identiferType", "");
			m_oLastLpsSvcResponseInfo.sExpiryDate = oMemberDetailObject.optString("expiryDate", "");
			m_oLastLpsSvcResponseInfo.sPointsBalance = oMemberDetailObject.optString("pointsBalance", "");
			m_oLastLpsSvcResponseInfo.sPoints = oMemberDetailObject.optString("pointsExpiringInCurrYear", "");
			m_oLastLpsSvcResponseInfo.sLocalBalance = StringLib.BigDecimalToStringWithoutZeroDecimal(dLocalBalance);
			m_oLastLpsSvcResponseInfo.sTotalPointsBalance = StringLib.BigDecimalToStringWithoutZeroDecimal(dTotalPointsBalance);
			m_oLastLpsSvcResponseInfo.sRemark = oMemberDetailObject.optString("reference", "");
			m_oLastLpsSvcResponseInfo.sMemberStatus = oMemberDetailObject.optString("memberStatus", "");
			m_oLastLpsSvcResponseInfo.sDisplayMessage = oMemberDetailObject.optString("alertMessage", "");
		}
		
		if (m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN)) {
			if (oMemberDetailObject.has("debitBalance"))
				m_oLastLpsSvcResponseInfo.sDebCardBalance = oMemberDetailObject.optString("debBalance", "");
			if (oMemberDetailObject.has("creditLimit"))
				m_oLastLpsSvcResponseInfo.sCreditLimit = oMemberDetailObject.optString("creditLimit", "");
			if (oMemberDetailObject.has("creditUsage"))
				m_oLastLpsSvcResponseInfo.sCreditUsage = oMemberDetailObject.optString("creditUsage", "");
		}
		
		//photo handling
		if(oMemberDetailObject.has("photoFileName"))
			m_oLastLpsSvcResponseInfo.sPhotoFileName = oMemberDetailObject.optString("photoFileName", "");
		if(oMemberDetailObject.has("signatureFileName"))
			m_oLastLpsSvcResponseInfo.sSignatureFileName = oMemberDetailObject.optString("signatureFileName", "");
		
		//card list
		m_oLastLpsSvcResponseInfo.sCardList = new ArrayList<String>();
		if(oMemberDetailObject.has("cardLists")) {
			for(int i=0; i<oMemberDetailObject.optJSONArray("cardLists").length(); i++) {
				String stmpFormat;
				JSONObject oCardJSON = oMemberDetailObject.optJSONArray("cardLists").optJSONObject(i);
				if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_YAZUO_CRM)){
					stmpFormat = String.format("%s,%s,%s,%s,%s,%s,%s", oCardJSON.optString("cardNumber", " "), 
							oCardJSON.optString("expiryDate", " "), oCardJSON.optString("accountBalance", " "),
							oCardJSON.optString("pointsBalance", " "), oCardJSON.optString("poinsAvailable", " "),
							oCardJSON.optString("activeDate", " "), oCardJSON.optString("cardType", " "));
				}else{
					 	stmpFormat = String.format("%s,%s,%s,%s,%s", oCardJSON.optString("cardNumber", " "), 
							oCardJSON.optString("cardStatus", " "), oCardJSON.optString("cardExpiryDate", " "),
							oCardJSON.optString("cardPointsBalance", " "), oCardJSON.optString("cardTotalPointsBalance", " "));
				}
				m_oLastLpsSvcResponseInfo.sCardList.add(stmpFormat);
			}
		}
		
		//voucher list
		if(oMemberDetailObject.has("voucherList")) {
			for(int i=0; i<oMemberDetailObject.optJSONArray("voucherList").length(); i++) {
				JSONObject oCardJSON = oMemberDetailObject.optJSONArray("voucherList").optJSONObject(i);
				if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_YAZUO_CRM)){
					m_oLastLpsSvcResponseInfo.sVoucherList.add(
							oCardJSON.optString("voucherCode", " ")	+	","
							+oCardJSON.optString("voucherType", " ")	+	","
							+oCardJSON.optString("voucherName", " ")	+	","
							+oCardJSON.optString("voucherAmount", " ")	+	","
							+oCardJSON.optString("voucherQty", " ")	+	","
							+oCardJSON.optString("voucherExpireDate", " ")+	","
							+oCardJSON.optString("voucherItemCode", " "));
				}else{
						m_oLastLpsSvcResponseInfo.sVoucherList.add(
							oCardJSON.optString("voucherCode", " ")	+	","
							+oCardJSON.optString("voucherType", " ")	+	","
							+oCardJSON.optString("voucherTypeValue", " ")	+	","
							+oCardJSON.optString("voucherExpireDate", " ")	+	","
							+oCardJSON.optString("voucherTenderMode", " "));
				}
			}
		}

		if (oMemberDetailObject.has("benefitList")) {
			for (int i=0; i<oMemberDetailObject.optJSONArray("benefitList").length(); i++) {
				String sBenefit = oMemberDetailObject.optJSONArray("benefitList").optString(i);
				if (m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_SPC))
					m_oLastLpsSvcResponseInfo.sBenefitList.add(sBenefit);
			}
		}

		return true;
	}
	
	public boolean membershipPrepaidCardEnquiry(HashMap<String, String> oEnquiryInfo){
		m_bConnectError = false;
		
		//Generate the enquiry information
		oEnquiryInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oEnquiryInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oEnquiryInfo.put("stationCode", AppGlobal.g_oFuncStation.get().getStation().getCode());
		
		JSONObject oResultJSONObject = m_oMembershipInterface.membershipPrepaidCardEnquiry(oEnquiryInfo);
		m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
		if (oResultJSONObject == null) {
			m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
			return false;
		}
		
		if(!oResultJSONObject.has("enquiryResult")) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("result_error");
			return false;
		}
		
		if(oResultJSONObject.optBoolean("enquiryResult", false) == false) {
			if(oResultJSONObject.optInt("errorCode") == 10)
				m_sLastErrorMessage = oResultJSONObject.optString("errorMessage");
			else
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("membership_system_error");
			return false;
		}
		
		JSONObject oPrepaidCardDetailObject = oResultJSONObject.optJSONObject("prepaidCardDetail");
		
		m_oLastLpsSvcResponseInfo = new MemberInterfaceResponseInfo();
		m_oLastLpsSvcResponseInfo.sCardNumber = oPrepaidCardDetailObject.optString("prepaidCardNumber", "");
		m_oLastLpsSvcResponseInfo.sCardLevelName = oPrepaidCardDetailObject.optString("cardName", "");
		m_oLastLpsSvcResponseInfo.sRemark = oPrepaidCardDetailObject.optString("remark", "");
		m_oLastLpsSvcResponseInfo.sPointsBalance = oPrepaidCardDetailObject.optString("balance", "");
		m_oLastLpsSvcResponseInfo.sExpiryDate = oPrepaidCardDetailObject.optString("expiryDate", "");
		
		return true;
	}
	
	public boolean membershipPointEnquiry(int iInterfaceId){
		m_bConnectError = false;
		
		JSONObject oResultJSONObject = m_oMembershipInterface.doMembershipPointEnquiry(iInterfaceId);
		m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
		if (oResultJSONObject == null) {
			m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
			return false;
		}
		
		if(!oResultJSONObject.has("enquiryResult")) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("result_error");
			return false;
		}
		
		if(oResultJSONObject.optBoolean("enquiryResult", false) == false) {
			if(oResultJSONObject.optInt("errorCode") == 10)
				m_sLastErrorMessage = oResultJSONObject.optString("errorMessage");
			else
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("membership_system_error");
			return false;
		}
		
		JSONArray oConversionRateList = oResultJSONObject.optJSONArray("conversionRateList");
		m_oLastLpsSvcResponseInfo = new MemberInterfaceResponseInfo();
		m_oLastLpsSvcResponseInfo.oPointInfoList = new ArrayList<MemberInterfacePointInfo>();
		for(int i = 0 ; i < oConversionRateList.length() ; i++) {
			try {
				MemberInterfacePointInfo oMemberInterfacePointInfo = new MemberInterfacePointInfo();
				oMemberInterfacePointInfo.dPointAmount = BigDecimal.valueOf(oConversionRateList.getJSONObject(i).optDouble("pointAmount"));
				oMemberInterfacePointInfo.dPointRate = BigDecimal.valueOf(oConversionRateList.getJSONObject(i).optDouble("pointRate"));
				oMemberInterfacePointInfo.sPointCurrency = oConversionRateList.getJSONObject(i).optString("pointCurrency");
				oMemberInterfacePointInfo.sPointDescription = oConversionRateList.getJSONObject(i).optString("pointDescription");
				oMemberInterfacePointInfo.sPointType = oConversionRateList.getJSONObject(i).optString("pointType");
				
				m_oLastLpsSvcResponseInfo.oPointInfoList.add(oMemberInterfacePointInfo);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	public boolean memberListEquiry(HashMap<String, String> oEnquiryInfo){
		m_bConnectError = false;
		//Generate the enquiry information
		oEnquiryInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oEnquiryInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oEnquiryInfo.put("stationCode", AppGlobal.g_oFuncStation.get().getStation().getCode());
		oEnquiryInfo.put("employeeNum", AppGlobal.g_oFuncUser.get().getUserNumber());
		
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2))
			oEnquiryInfo.put("sessionId", AppGlobal.getInterfaceSession(m_oMembershipInterface.getInterfaceId()));
		
		JSONObject oResultJSONObject = m_oMembershipInterface.memberEnquiry(oEnquiryInfo);
		
		m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
		if(oResultJSONObject == null) {
			m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
			return false;
		}
		
		if(!oResultJSONObject.has("enquiryResult")) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("result_error");
			return false;
		}
		
		if(oResultJSONObject.optBoolean("enquiryResult", false) == false) {
			if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN) &&
					(oResultJSONObject.optInt("errorCode") == 6 || oResultJSONObject.optInt("errorCode") == 8))
					m_bConnectError = true;
			if(oResultJSONObject.optInt("errorCode", 0) == 0)
				m_sLastErrorMessage = oResultJSONObject.optString("errorMessage");
			else {
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("member_enquiry_failed") + System.lineSeparator()
						+ AppGlobal.g_oLang.get()._("message") + ": " + getErrorMessage(oResultJSONObject.optInt("errorCode", 0));
			}
			return false;
		}
		
		// Get and update session id if needed
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2) && oResultJSONObject.has("sessionId")) {
			String sSession = oResultJSONObject.optString("sessionId", "");
		if(!sSession.isEmpty())
			AppGlobal.generateUpdateInterfaceSession(m_oMembershipInterface.getInterfaceId(), sSession);
		}
		
		JSONArray oMemberDetailArray = oResultJSONObject.optJSONArray("memberDetails");
		
		if(oMemberDetailArray.length() == 0){
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_such_member");
			return false;
		}
		
		m_oResponseMemberList = new ArrayList<MemberInterfaceResponseInfo>();
		for(int i=0; i<oMemberDetailArray.length(); i++){
			JSONObject oMemberDetailObject = oMemberDetailArray.optJSONObject(i);
			MemberInterfaceResponseInfo oTempMember = new MemberInterfaceResponseInfo();
				
			if(oMemberDetailObject == null)
				continue;
				
			if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_YAZUO_CRM)){
				oTempMember.sMemberNumber = oMemberDetailObject.optString("memberNumber", "");
				oTempMember.sMemberName = oMemberDetailObject.optString("memberName", "");
				oTempMember.sBirthday = oMemberDetailObject.optString("birthday", "");
				oTempMember.sGender = oMemberDetailObject.optString("gender", "");
				oTempMember.sMobileNumber = oMemberDetailObject.optString("mobile", "");
				oTempMember.sEmail = oMemberDetailObject.optString("email", "");
				oTempMember.sJoinDate = oMemberDetailObject.optString("joinDate", "");
					
			}else if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_CONCEPT)){
				oTempMember.sMemberNumber = oMemberDetailObject.optString("guestMembershipNumber", "");
				oTempMember.sMemberName = oMemberDetailObject.optString("memberName", "");
				oTempMember.sCardNumber = oMemberDetailObject.optString("cardNumber");
				oTempMember.sPointsBalance = oMemberDetailObject.optString("pointsBalance", "");
				oTempMember.sCreditLimit = oMemberDetailObject.optString("creditLimit", "");
				oTempMember.sMemberStatus = this.getStatus(Integer.parseInt(oMemberDetailObject.optString("guestStatus", "")));
				oTempMember.sArAccountNumber = oMemberDetailObject.optString("guestNumber", "");
					
			}else if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_VIENNA_CRM)){
				oTempMember.sMemberNumber = oMemberDetailObject.optString("memberNumber", "");
				oTempMember.sMemberName = oMemberDetailObject.optString("memberName", "");
				oTempMember.sMemberType = oMemberDetailObject.optString("memberType", "");
				oTempMember.sGender = oMemberDetailObject.optString("gender", "");
				oTempMember.sIdentiferType = oMemberDetailObject.optString("identiferType", "");
				oTempMember.sNRIC = oMemberDetailObject.optString("nric", "");
				oTempMember.sMobileNumber = oMemberDetailObject.optString("mobileNumber", "");
				oTempMember.sEmail = oMemberDetailObject.optString("email", "");
				oTempMember.sCardNumber = oMemberDetailObject.optString("cardNumber", "");
				oTempMember.sPointsBalance = oMemberDetailObject.optString("pointBalance", "0");
				oTempMember.sBirthday = oMemberDetailObject.optString("birthday", "");
				oTempMember.sJoinDate = oMemberDetailObject.optString("joinDate", "");
				oTempMember.sTier = oMemberDetailObject.optString("tier", "");
				oTempMember.sSalesNumber = oMemberDetailObject.optString("salesNumber", "");
			}else if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GOLDEN_CIRCLE)){
				oTempMember.sMemberFirstName = oMemberDetailObject.optString("firstName", "");
				oTempMember.sMemberLastName = oMemberDetailObject.optString("lastName", "");
				oTempMember.sMemberNumber = oMemberDetailObject.optString("memberNumber", "");
				oTempMember.sTier = oMemberDetailObject.optString("tier", "");
				oTempMember.sEnrolmentCode = oMemberDetailObject.optString("enrolmentCode", "");
				oTempMember.sExpiryDate = oMemberDetailObject.optString("expiryDate", "");
				oTempMember.sPointsBalance = oMemberDetailObject.optString("pointsBalance", "");
				oTempMember.sPoints = oMemberDetailObject.optString("pointsExpiringInCurrYear", "");
				oTempMember.sLocalBalance = oMemberDetailObject.optString("convertedAmountLocalCurrency", "");
				oTempMember.sTotalPointsBalance = oMemberDetailObject.optString("convertedAmountLocalCurrencyInCurrYear", "");
				oTempMember.sRemark = oMemberDetailObject.optString("preference", "");
				oTempMember.sMemberStatus = oMemberDetailObject.optString("status", "");
				oTempMember.sCustomerNumber = oMemberDetailObject.optString("memberNumber", "");
			}else if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2)){
				oTempMember.sMemberNumber = oMemberDetailObject.optString("memberNumber", "");
				oTempMember.sMemberFirstName = oMemberDetailObject.optString("firstName", "");
				oTempMember.sMemberLastName = oMemberDetailObject.optString("lastName", "");
				oTempMember.sTier = oMemberDetailObject.optString("tier", "");
				oTempMember.sAddress = oMemberDetailObject.optString("address", "");
				oTempMember.sCity = oMemberDetailObject.optString("city", "");
				oTempMember.sStateRegion = oMemberDetailObject.optString("stateRegion", "");
				oTempMember.sPostalCode = oMemberDetailObject.optString("postalCode", "");
				oTempMember.sCountry = oMemberDetailObject.optString("country", "");

			}else{
				oTempMember.sCustomerNumber = oMemberDetailObject.optString("customerNumber", "");
				oTempMember.sMemberName = oMemberDetailObject.optString("memberName", "");
				oTempMember.sNRIC = oMemberDetailObject.optString("nric", "");
				oTempMember.sMobileNumber = oMemberDetailObject.optString("mobileNumber", "");
				oTempMember.sEmail = oMemberDetailObject.optString("email", "");
				oTempMember.sBirthday = oMemberDetailObject.optString("birthday", "");
				oTempMember.sMemberFirstName = oMemberDetailObject.optString("firstName", "");
				oTempMember.sMemberLastName = oMemberDetailObject.optString("lastName", "");
				oTempMember.sGender = oMemberDetailObject.optString("gender", "");
				oTempMember.sNationality = oMemberDetailObject.optString("nationality", "");
				oTempMember.sPointsBalance = oMemberDetailObject.optString("pointsBalance", "");
				oTempMember.sMemberType = oMemberDetailObject.optString("memberType", "");
				oTempMember.sTier = oMemberDetailObject.optString("tier", "");
				oTempMember.sMemberNumber = oMemberDetailObject.optString("memberNumber", "");
				oTempMember.sMemberStatus = oMemberDetailObject.optString("memberStatus", "");
				oTempMember.sCardNumber = oMemberDetailObject.optString("cardNumber");
			}
			if(oMemberDetailObject.has("cardLists")){
				ArrayList<String> oCardList = new ArrayList<String>();
				for(int j=0; j<oMemberDetailObject.optJSONArray("cardLists").length(); j++){
					JSONObject oCardObject = oMemberDetailObject.optJSONArray("cardLists").optJSONObject(j);
					String stmpFormat = String.format("%s,%s", oCardObject.optString("cardNumber", " "), 
							oCardObject.optString("cardExpiryDate", " "));
					oCardList.add(stmpFormat);
				}
				oTempMember.sCardList = oCardList;
			}
			m_oResponseMemberList.add(oTempMember);
		}
		return true;
	}
	
	public boolean awardRetrieval(HashMap<String, String> oEnquiryInfo) {
		m_bConnectError = false;
		
		oEnquiryInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oEnquiryInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oEnquiryInfo.put("stationCode", AppGlobal.g_oFuncStation.get().getStation().getCode());
		oEnquiryInfo.put("employeeNum", AppGlobal.g_oFuncUser.get().getUserNumber());
		oEnquiryInfo.put("timeZone", String.valueOf(AppGlobal.g_oFuncOutlet.get().getShop().getTimezone()));

		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2))
			oEnquiryInfo.put("sessionId", AppGlobal.getInterfaceSession(m_oMembershipInterface.getInterfaceId()));
		
		JSONObject oResultJSONObject = m_oMembershipInterface.awardRetrieval(oEnquiryInfo);
		
		m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
		if(oResultJSONObject == null) {
			if (m_oMembershipInterface.getLastErrorCode() > 0 && m_oMembershipInterface.getLastErrorCode() < 25)
				m_sLastErrorMessage = getErrorMessage(m_oMembershipInterface.getLastErrorCode());
			else if(m_oMembershipInterface.getLastErrorCode() > 24)
				m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
			return false;
		}
		
		// Get and update session id if needed
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2) && oResultJSONObject.has("sessionId")) {
			String sSession = oResultJSONObject.optString("sessionId", "");
			if(!sSession.isEmpty())
				AppGlobal.generateUpdateInterfaceSession(m_oMembershipInterface.getInterfaceId(), sSession);
		}
		
		if(!oResultJSONObject.optBoolean("enquiryResult", false)) {
			m_sLastErrorMessage = oResultJSONObject.optString("errorMessage");
			return false;
		}
		
		JSONObject oResultObject = oResultJSONObject.optJSONObject("result");
		m_oLastLpsSvcResponseInfo = new MemberInterfaceResponseInfo();
		
		if(oResultObject == null || oResultObject.length() == 0) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_response");
			return false;
		}
		
		m_oLastLpsSvcResponseInfo.sHotelCurrency = oResultObject.optString("currency", "");
		ArrayList<HashMap<Integer, BigDecimal>> oTempListForSorting = new ArrayList<HashMap<Integer, BigDecimal>>();
		m_oLastLpsSvcResponseInfo.oAwardMappingList = new ArrayList<HashMap<Integer, BigDecimal>>();
		if(oResultObject.has("award")) {
			for(int i=0; i < oResultObject.optJSONArray("award").length(); i++) {
				JSONObject oAwardJSON = oResultObject.optJSONArray("award").optJSONObject(i);
				HashMap<Integer, BigDecimal> oAward = new HashMap<Integer, BigDecimal>();
				if(oAwardJSON == null)
					continue;
				if(oAwardJSON.has("amount") && oAwardJSON.has("points")) {
					oAward.put(Integer.parseInt(oAwardJSON.optString("points", "0")), new BigDecimal(oAwardJSON.optString("amount", "0")));
					oTempListForSorting.add(oAward);
				}
			}
		}
		
		if(!oTempListForSorting.isEmpty()) {
			//define type Comparator for sorting
			Comparator<HashMap<Integer, BigDecimal>> oAmountComparator = new Comparator<HashMap<Integer, BigDecimal>>() {
				@Override
				//define comparison
				public int compare(HashMap<Integer, BigDecimal> o1, HashMap<Integer, BigDecimal> o2) {
					BigDecimal oAmount1 = new BigDecimal(o1.values().toArray()[0].toString());
					BigDecimal oAmount2 = new BigDecimal(o2.values().toArray()[0].toString());
					return oAmount1.compareTo(oAmount2);
				}
			};
			//do the sorting
			Collections.sort(oTempListForSorting, oAmountComparator);
			
			m_oLastLpsSvcResponseInfo.oAwardMappingList = oTempListForSorting;
		}
		
		return true;
	}
	
	
	
	// membership posting
	public boolean membershipPosting(HashMap<String, String> oPostingInfoMap, FuncCheck oFuncCheck, List<PosCheckPayment> oCheckPayments,FuncPayment oFuncPayment, boolean bPointPosting, boolean bIsLastPayment) {
		if(!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_LPS_SVC) &&
				!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_LPS_SVC_COUPON) &&
				!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL) &&
				!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASCENTIS_CRM) &&
				!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_EPOS) &&
				!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_YAZUO_CRM) &&
				!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN) &&
				!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_CONCEPT) &&
				!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_VIENNA_CRM) &&
				!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_SMART_INTEGRAL) &&
				!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN_PMS) &&
				!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2))
			return false;
		
		boolean bFailToPost = false;
		List<LpsSvcPostingInfo> oPostingInfoList = new ArrayList<LpsSvcPostingInfo>();
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HHmmss");
		String sCurrentTime = timeFmt.print(today);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyyMMdd");
		String sCurrentDate = dateFmt.print(today);
		BigDecimal dCrmPointPayTotal = BigDecimal.ZERO;
		String sCrmPointToDeduct = "";
		
		BigDecimal dPointPaymentTotal = BigDecimal.ZERO;
		BigDecimal dStoreValuePaymentTotal = BigDecimal.ZERO;
		
		LpsSvcPostingInfo oPostingInfo = new LpsSvcPostingInfo();
		oPostingInfo.iInterfaceId = m_oMembershipInterface.getInterfaceId();
		oPostingInfo.iOutletId = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		
		if(oPostingInfoMap.containsKey("memberNo"))
			oPostingInfo.sMemberNumber = oPostingInfoMap.get("memberNo");
		if(oPostingInfoMap.containsKey("cardNo"))
			oPostingInfo.sCardNo = oPostingInfoMap.get("cardNo");
		if(oPostingInfoMap.containsKey("cardStatus"))
			oPostingInfo.sCardStatus = oPostingInfoMap.get("cardStatus");
		if(oPostingInfoMap.containsKey("memberName"))
			oPostingInfo.sMemberName = oPostingInfoMap.get("memberName");
		if(oPostingInfoMap.containsKey("memberLastName"))
			oPostingInfo.sMemberLastName = oPostingInfoMap.get("memberLastName");
		if(oPostingInfoMap.containsKey("arAccountNumber"))
			oPostingInfo.sArAccountNumber = oPostingInfoMap.get("arAccountNumber");
		if(oPostingInfoMap.containsKey("traceId"))
			oPostingInfo.sTraceId = oPostingInfoMap.get("traceId");
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN) || m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN_PMS)){
			if(oPostingInfoMap.containsKey("isFromPms")){
				if(oPostingInfoMap.get("isFromPms").equals("true"))
					oPostingInfo.bIsFromPms = true;
				else
					oPostingInfo.bIsFromPms = false;
			}
		}
		
		
		if (m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_LPS_SVC)) {
			oPostingInfo.sPassword = this.getMd5String("UTF-8", oPostingInfoMap.get("password"));
			if (oFuncCheck.hasActiveCheckAttribute() && m_oMembershipInterface.getInterfaceConfig()
					.optJSONObject("svc_setting").optJSONObject("params").optJSONObject("terminal_type_classification") != null) {
				String sTerminalType = m_oMembershipInterface.getInterfaceConfig().optJSONObject("svc_setting")
						.optJSONObject("params").optJSONObject("terminal_type_classification").optString("value");
				if (sTerminalType.length() >= 15 && sTerminalType.substring(0, 15).equals("attribute_type_"))
					oPostingInfo.iAttributeId = oFuncCheck.getCheckAttribute()
							.getAttrAttoId()[Integer.parseInt(sTerminalType.substring(15)) - 1];
			}
		}

		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2))
			oPostingInfo.sSessionId = AppGlobal.getInterfaceSession(m_oMembershipInterface.getInterfaceId());
		
		oPostingInfo.sStationId = AppGlobal.g_oFuncStation.get().getStation().getCode();
		if (!AppGlobal.g_oFuncOutlet.get().getBusinessDay().isCheckNumGeneratedByOutlet() && !oFuncCheck.isOldCheck())
			oPostingInfo.sCheckNumber = AppGlobal.g_oFuncStation.get().getNextCheckPrefixNumber(false);
		else
			oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
		oPostingInfo.iCover = oFuncCheck.getCover();
		//oPostingInfo.dPaymentAmount = oCheckPayment.getPayTotal();
		//oPostingInfo.iPaymentId = oCheckPayment.getPaymentMethodId();
		//oPostingInfo.sPaymentName = oCheckPayment.getName(1);
		oPostingInfo.sEmployeeNumber = AppGlobal.g_oFuncUser.get().getUserNumber();
		//oPostingInfo.dTips = oCheckPayment.getPayTips();
		oPostingInfo.sDate = sCurrentDate;
		oPostingInfo.sTime = sCurrentTime;
		if(bPointPosting)
			oPostingInfo.iPointPosting = 1;
		else
			oPostingInfo.iPointPosting = 0;
		if(oPostingInfoMap.containsKey("paymentSequence"))
			oPostingInfo.sPaymentSequence = oPostingInfoMap.get("paymentSequence");
		
		//For KEY_ASCENTIS_CRM, get Points and Voucher Information
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASCENTIS_CRM)){
			if(oPostingInfoMap.containsKey("crm_point_payment_total"))
				dCrmPointPayTotal = new BigDecimal(oPostingInfoMap.get("crm_point_payment_total"));
			if(oPostingInfoMap.containsKey("crm_point_to_deduct"))
				sCrmPointToDeduct = oPostingInfoMap.get("crm_point_to_deduct");
			
			//to calculate the point used for discount for posting
			if(oPostingInfoMap.containsKey("crm_point_to_deduct_for_discount")){
				oPostingInfo.sPointUsedByDiscount = oPostingInfoMap.get("crm_point_to_deduct_for_discount");
			}
			
			oPostingInfo.dPointPaymentTotal = dCrmPointPayTotal;
			oPostingInfo.sPointUsed = sCrmPointToDeduct;
			//get vouchers from database
			PosCheckExtraInfoList oPosCheckExtraInfoList = new PosCheckExtraInfoList();
			oPosCheckExtraInfoList.readAllByCheckId(oFuncCheck.getCheckId());
			HashMap<String, String> oVoucherInfo = new HashMap<String,String>();
			oPostingInfo.oVoucherList = new ArrayList<HashMap<String, String>>();
			if(oPosCheckExtraInfoList.getCheckExtraInfoList().size() > 0){
				for(PosCheckExtraInfo oPosCheckExtraInfo : oPosCheckExtraInfoList.getCheckExtraInfoList()){
					if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER))
						oVoucherInfo.put("voucherNumber", (oPosCheckExtraInfo.getValue() != null && !oPosCheckExtraInfo.getValue().isEmpty())? oPosCheckExtraInfo.getValue() : "");
					if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE))
						oVoucherInfo.put("voucherType", (oPosCheckExtraInfo.getValue() != null && !oPosCheckExtraInfo.getValue().isEmpty())? oPosCheckExtraInfo.getValue() : "");
					if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_VOUCHER_VALUE))
						oVoucherInfo.put("voucherValue", (oPosCheckExtraInfo.getValue() != null && !oPosCheckExtraInfo.getValue().isEmpty())? oPosCheckExtraInfo.getValue() : "");
					if(oVoucherInfo.get("voucherNumber") != null && oVoucherInfo.get("voucherType") != null && oVoucherInfo.get("voucherValue") != null){
						oPostingInfo.oVoucherList.add(oVoucherInfo);
						oVoucherInfo = new HashMap<String,String>();
					}
				}
			}
			for(PosCheckPayment oPayment : oCheckPayments){
				String sVoucherNumber = "", sVoucherType = "", sVoucherValue = "";
				PosPaymentMethod oPaymentMethod = oFuncPayment.getPaymentMethodList().getPaymentMethod(oPayment.getPaymentMethodId());
				if(oPaymentMethod.isCashVoucherPaymentType()){
					sVoucherNumber = oPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER, 0).getValue();
					sVoucherType = oPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_TYPE, 0).getValue();
					sVoucherValue = oPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_VALUE, 0).getValue();
					oVoucherInfo.put("voucherNumber", sVoucherNumber);
					oVoucherInfo.put("voucherType", sVoucherType);
					oVoucherInfo.put("voucherValue", sVoucherValue);
					if(oVoucherInfo.get("voucherNumber") != null && oVoucherInfo.get("voucherType") != null && oVoucherInfo.get("voucherValue") != null){
						oPostingInfo.oVoucherList.add(oVoucherInfo);
						oVoucherInfo = new HashMap<String,String>();
					}
				}
			}
		}
		
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_EPOS)) {
			oPostingInfo.sPomotionCodes = oPostingInfoMap.get("promotionCodes");
			if(oPostingInfoMap.containsKey("pointPaymentTotal"))
				oPostingInfo.dPointPaymentTotal = new BigDecimal(oPostingInfoMap.get("pointPaymentTotal"));
			if(oPostingInfoMap.containsKey("paymentType"))
				oPostingInfo.sPaymentType = oPostingInfoMap.get("paymentType");
			if(oPostingInfoMap.containsKey("currencyCode"))
				oPostingInfo.sCurrencyCode = oPostingInfoMap.get("currencyCode");
			if(oPostingInfoMap.containsKey("currencySign"))
				oPostingInfo.sCurrencySign = oPostingInfoMap.get("currencySign");
			if(oPostingInfoMap.containsKey("amountPaymentTotal"))
				oPostingInfo.dPaymentAmount = new BigDecimal(oPostingInfoMap.get("amountPaymentTotal"));
		}
		
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_YAZUO_CRM)) {
			if(oPostingInfoMap.containsKey("storeValuePaymentTotal"))
				dStoreValuePaymentTotal = new BigDecimal(oPostingInfoMap.get("storeValuePaymentTotal"));
			if(oPostingInfoMap.containsKey("pointPaymentTotal"))
				dPointPaymentTotal = new BigDecimal(oPostingInfoMap.get("pointPaymentTotal"));
			oPostingInfo.dStoreValuePaymentTotal = dStoreValuePaymentTotal;
			oPostingInfo.dPointPaymentTotal = dPointPaymentTotal;
			oPostingInfo.iPaymentType = Integer.parseInt(oPostingInfoMap.get("paymentType"));
			
			PosCheckExtraInfoList oPosCheckExtraInfoList = new PosCheckExtraInfoList();
			oPosCheckExtraInfoList.readAllByCheckId(oFuncCheck.getCheckId());
			HashMap<String, String> oVoucherInfo = new HashMap<String,String>();
			oPostingInfo.oVoucherList = new ArrayList<HashMap<String, String>>();
			oPostingInfo.oItemVoucherList = new ArrayList<HashMap<String, String>>();
			
			oPostingInfo.sPassword = oPostingInfoMap.get("password");
			for(PosCheckPayment oPayment : oCheckPayments){
				PosPaymentMethod oPaymentMethod = oFuncPayment.getPaymentMethodList().getPaymentMethod(oPayment.getPaymentMethodId());
				if(!oPaymentMethod.isMembershipInterfacePayment(InfVendor.KEY_YAZUO_CRM))
					continue;
				PosInterfaceConfig oPosInterfaceConfig = oPaymentMethod.getInterfaceConfig(InfInterface.TYPE_MEMBERSHIP_INTERFACE).get(0);
				if(oPosInterfaceConfig.getConfigValue().optJSONObject("type").optJSONObject("params").optJSONObject("payment_type").optString("value").equals("voucher_redeem")){
					PosCheckExtraInfo oPosCheckExtraInfo = oPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER, 0);
					if (oPosCheckExtraInfo != null && !oPosCheckExtraInfo.getValue().isEmpty()) {
						if(oVoucherInfo.containsKey(oPosCheckExtraInfo.getValue())) {
							int iVoucherUsage = Integer.parseInt(oVoucherInfo.get(oPosCheckExtraInfo.getValue()));
							oVoucherInfo.put(oPosCheckExtraInfo.getValue(), String.valueOf(iVoucherUsage++));
						} else
							oVoucherInfo.put(oPosCheckExtraInfo.getValue(), "1");
					}
				}
			}
			oPostingInfo.oVoucherList.add(oVoucherInfo);
			oVoucherInfo = new HashMap<String,String>();
			
			//retreive check discount applied if any
			for(PosCheckDiscount oPosCheckDiscount:oFuncCheck.getCurrentPartyAppliedCheckDiscount()){
				String sVoucherNumber = "";
				String sVoucherQtyUsed = "";
				//voucher number
				PosCheckExtraInfo oPosCheckExtraInfo = oPosCheckDiscount.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER, 0);
				if (oPosCheckExtraInfo != null && !oPosCheckExtraInfo.getValue().isEmpty())
					sVoucherNumber = oPosCheckExtraInfo.getValue();
				
				//voucher used qty
				oPosCheckExtraInfo = oPosCheckDiscount.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_USED_QTY, 0);
				if (oPosCheckExtraInfo != null && !oPosCheckExtraInfo.getValue().isEmpty())
					sVoucherQtyUsed = oPosCheckExtraInfo.getValue();
				
				if(!sVoucherNumber.isEmpty() && !sVoucherNumber.isEmpty()){
					oVoucherInfo.put(sVoucherNumber, sVoucherQtyUsed);
					oPostingInfo.oVoucherList.add(oVoucherInfo);
					oVoucherInfo = new HashMap<String,String>();
				}
			}

			//retreive item applied if any
			for(List<FuncCheckItem> oFuncCheckItems : oFuncCheck.getWholeItemList()){
				for(FuncCheckItem oFuncCheckItem :oFuncCheckItems){
					String sVoucheCode = oFuncCheckItem.getCheckItem().getCode();
					if (sVoucheCode == null)
						sVoucheCode = "";
					String sVoucherItemName = oFuncCheckItem.getMenuItemName(1);
					if (sVoucherItemName == null)
						sVoucherItemName = "";
					
					if(!sVoucheCode.isEmpty() && !sVoucherItemName.isEmpty()){
						oVoucherInfo.put(sVoucheCode, sVoucherItemName);
						oPostingInfo.oItemVoucherList.add(oVoucherInfo);
						oVoucherInfo = new HashMap<String,String>();
					}
				}
			}
		}
		
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN)) {
			if(oPostingInfoMap.containsKey("referenceNo"))
				oPostingInfo.sReferenceNo = oPostingInfoMap.get("referenceNo");
			if(oPostingInfoMap.containsKey("voucherAmount"))
				oPostingInfo.sVoucherAmount = oPostingInfoMap.get("voucherAmount");
			if(oPostingInfoMap.containsKey("voucherType"))
				oPostingInfo.sVoucherType = oPostingInfoMap.get("voucherType");
		}
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_VIENNA_CRM))
			oPostingInfo.sEmployeeName = oPostingInfoMap.get("employeeName");
		
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_CONCEPT)){
			if(oPostingInfoMap.containsKey("memberNo"))
				oPostingInfo.sMemberNumber = oPostingInfoMap.get("memberNo");
			if(oPostingInfoMap.containsKey("paymentType"))
				oPostingInfo.sPaymentType = oPostingInfoMap.get("paymentType");
			if(oPostingInfoMap.containsKey("pointUsed"))
				oPostingInfo.sPointUsed = oPostingInfoMap.get("pointUsed");
			if(AppGlobal.g_oFuncStation.get().isPartialPayment() && oFuncPayment != null && bIsLastPayment) {
				ArrayList<PosCheckPayment> oPosPaymentList = new ArrayList<PosCheckPayment>();
				for(int i = 0 ; i < oFuncPayment.getCheckPaymentListCount() ; i++) {
					if(i != oFuncPayment.getCheckPaymentListCount() - 1)
						oPosPaymentList.add(oFuncPayment.getCheckPaymentList().get(i));
				}
				oPostingInfo.oPreviousCheckPayments = oPosPaymentList;
			}
		}
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_SMART_INTEGRAL)) {
			oPostingInfo.dPostAmount = new BigDecimal(oPostingInfoMap.get("postAmount"));
			oPostingInfo.sEmployeeName = oPostingInfoMap.get("employeeName");
		}
		
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2)) {
			if(oPostingInfoMap.containsKey("facilityCode"))
				oPostingInfo.sFacilityCode = oPostingInfoMap.get("facilityCode");
			if(oPostingInfoMap.containsKey("facilityTypeCode"))
				oPostingInfo.sFacilityTypeCode = oPostingInfoMap.get("facilityTypeCode");
			if(oPostingInfoMap.containsKey("transactionTime"))
				oPostingInfo.sTransactionTime = oPostingInfoMap.get("transactionTime");
			if(oPostingInfoMap.containsKey("redemptionAmount"))
				oPostingInfo.dPostAmount = new BigDecimal(oPostingInfoMap.get("redemptionAmount"));
			if(oPostingInfoMap.containsKey("businessDate"))
				oPostingInfo.sBusinessDate = oPostingInfoMap.get("businessDate");

			oPostingInfo.sEmployeeName = AppGlobal.g_oFuncUser.get().getUserName(1);
			oPostingInfo.sSessionId = AppGlobal.getInterfaceSession(m_oMembershipInterface.getInterfaceId());
			
			DateTimeFormatter dateTimeFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
			oPostingInfo.sCheckDateTime = dateTimeFmt.print(oFuncCheck.getOpenLocTime());
			
			oPostingInfo.sPostingTimeZone = String.valueOf(AppGlobal.g_oFuncOutlet.get().getShop().getTimezone());
			oPostingInfo.sStationCode = AppGlobal.g_oFuncStation.get().getStation().getCode();
		}
		
		oPostingInfoList.add(oPostingInfo);
		
		// no svc posting is needed
		if(oPostingInfoList.isEmpty())
			return true;
		
		// do posting
		for (int i = 0; i < oPostingInfoList.size(); i++) {
			//JSONObject oCheckInformationJSONObject = oFuncCheck.contrustPmsPostingCheckInformation(oCheckPayment);
			JSONObject oCheckInformationJSONObject = oFuncCheck.contrustSvcPostingCheckInformation(oCheckPayments, oPostingInfoList.get(i).oPreviousCheckPayments, null);
			PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
			boolean bResult = oInterfaceConfig.doMembershipPosting(oCheckInformationJSONObject, fromSvcPostingJSONObject(oPostingInfoList.get(i)));
			
			if(bResult == false) {
				if(oInterfaceConfig.getLastErrorType() != null && oInterfaceConfig.getLastErrorType().equals(FuncPMS.ERROR_TYPE_PMS))
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("svc_return_error") + ":[" + oInterfaceConfig.getLastErrorMessage() + "]";
				else if(oInterfaceConfig.getLastErrorCode() != 0)
					m_sLastErrorMessage = getErrorMessage(oInterfaceConfig.getLastErrorCode());
				else
					m_sLastErrorMessage = oInterfaceConfig.getLastErrorMessage();
				
				bFailToPost = true;
			}else {
				JSONObject oResultJSON = oInterfaceConfig.getLastSuccessResult();
				if(!oResultJSON.optString("responseCode", "").equals("A")) {
					if(!oResultJSON.optString("displayMessage", "").isEmpty())
						m_sLastErrorMessage = oResultJSON.optString("displayMessage");
					else
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("membership_system_error");
					return false;
				}
				
				m_oLastLpsSvcResponseInfo = new MemberInterfaceResponseInfo();
				m_oLastLpsSvcResponseInfo.sPrintLines = new String[2];
				m_oLastLpsSvcResponseInfo.sPrintLines[0] = "";
				m_oLastLpsSvcResponseInfo.sPrintLines[1] = "";
				
				m_oLastLpsSvcResponseInfo.sHotelCode = oResultJSON.optString("hotelCode", "");
				m_oLastLpsSvcResponseInfo.sTraceId = oResultJSON.optString("traceID", "");
				m_oLastLpsSvcResponseInfo.sTerminalId = oResultJSON.optString("terminalID", "");
				m_oLastLpsSvcResponseInfo.sAmount = oResultJSON.optString("amount", "");
				m_oLastLpsSvcResponseInfo.sTipAmount = oResultJSON.optString("tipAmount", "");
				m_oLastLpsSvcResponseInfo.sMemberNumber = oResultJSON.optString("memberNumber", "");
				m_oLastLpsSvcResponseInfo.sMemberName = oResultJSON.optString("memberName", "");
				m_oLastLpsSvcResponseInfo.sPointsBalance = oResultJSON.optString("pointsBalance", "");
				m_oLastLpsSvcResponseInfo.sPoints = oResultJSON.optString("points", "");
				m_oLastLpsSvcResponseInfo.sLocalBalance = oResultJSON.optString("localBalance", "");
				m_oLastLpsSvcResponseInfo.sAccountBalance = oResultJSON.optString("accountBalance", "");
				m_oLastLpsSvcResponseInfo.sResponseCode = oResultJSON.optString("responseCode", "");
				if(oResultJSON.optJSONArray("printLines") != null && oResultJSON.optJSONArray("printLines").length() > 0) {
					for(int j=0; j<oResultJSON.optJSONArray("printLines").length() && j<2; j++)
						m_oLastLpsSvcResponseInfo.sPrintLines[j] = oResultJSON.optJSONArray("printLines").optJSONObject(j).optString("printLine");
				}
				m_oLastLpsSvcResponseInfo.sEnglishName = oResultJSON.optString("englishName", "");
				m_oLastLpsSvcResponseInfo.sCardTypeName = oResultJSON.optString("cardTypeName", "");
				m_oLastLpsSvcResponseInfo.sCardLevelName = oResultJSON.optString("cardLevelName", "");
				m_oLastLpsSvcResponseInfo.sCardNumber = oResultJSON.optString("cardNumber", "");
				m_oLastLpsSvcResponseInfo.sExpiryDate = oResultJSON.optString("expiryDate", "");
				m_oLastLpsSvcResponseInfo.sDisplayMessage = oResultJSON.optString("displayMessage", "");
				
				//For CRM interface only
				if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASCENTIS_CRM)){
					m_oLastLpsSvcResponseInfo.sPostingKey = oResultJSON.optString("transRefId", "");
					m_oLastLpsSvcResponseInfo.sPointEarned = oResultJSON.optString("pointEarned", "");
				}
				
				if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_EPOS)){
					m_oLastLpsSvcResponseInfo.sPostingKey = oResultJSON.optString("postingKey", "");
					
					if(!oResultJSON.isNull("postingResult")) {
						m_oLastLpsSvcResponseInfo.sTraceId = oResultJSON.optJSONObject("postingResult").optString("ReferenceNumber", "");
						m_oLastLpsSvcResponseInfo.sExpiryDate = oResultJSON.optJSONObject("postingResult").optString("ExpiryDate", "");
					}
				}
				
				if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN)){
					m_oLastLpsSvcResponseInfo.sPointsBalance = oResultJSON.optString("pointsBalance", "0");
					m_oLastLpsSvcResponseInfo.sLocalBalance = oResultJSON.optString("localBalance", "0");
				}
				
				if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_YAZUO_CRM)){
					m_oLastLpsSvcResponseInfo.sTraceId = oResultJSON.optString("traceId", "");
					m_oLastLpsSvcResponseInfo.sPointsBalance = oResultJSON.optString("pointsBalance", "");
					m_oLastLpsSvcResponseInfo.sStoreValueBalance = oResultJSON.optString("storeValueBalance", "");
					m_oLastLpsSvcResponseInfo.sPointEarned  = oResultJSON.optString("pointsEarned", "");
					
					//coupon list
					if(oResultJSON.has("couponBonus")) {
						m_oLastLpsSvcResponseInfo.sCouponList = new ArrayList<>();
						for(int j=0; j<oResultJSON.optJSONArray("couponBonus").length(); j++) {
							JSONObject oCouponJSON = oResultJSON.optJSONArray("couponBonus").optJSONObject(i);
							if(oCouponJSON != null && oCouponJSON.has("id"))
								m_oLastLpsSvcResponseInfo.sCouponList.add(oCouponJSON.optString("id"));
						}
					}
				}
				
				if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_CONCEPT)){
					m_oLastLpsSvcResponseInfo.sPointsBalance = oResultJSON.optString("newBalance", "");
					m_oLastLpsSvcResponseInfo.sReference = oResultJSON.optString("reference", "");
					m_oLastLpsSvcResponseInfo.sPostingString = oResultJSON.optString("postingString", "");
				}
				if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_VIENNA_CRM))
					m_oLastLpsSvcResponseInfo.sTraceId = oResultJSON.optString("traceId", "");
				if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_SMART_INTEGRAL)){
					//JSONObject oResultDetails = oResultJSON.optJSONObject("resultDetails");
					m_oLastLpsSvcResponseInfo.sPointsBalance  = oResultJSON.optString("pointBalance", "");
					m_oLastLpsSvcResponseInfo.sPointEarned  = oResultJSON.optString("pointEarned", "");
				}
				if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2)){
					m_oLastLpsSvcResponseInfo.sAwardCode  = oResultJSON.optString("issuedAwardCode", "");
					m_oLastLpsSvcResponseInfo.sPointsRedeem  = oResultJSON.optString("pointsRedeemed", "");
					m_oLastLpsSvcResponseInfo.sSessionId  = oResultJSON.optString("sessionId", "");
					if(!m_oLastLpsSvcResponseInfo.sSessionId.isEmpty())
						AppGlobal.generateUpdateInterfaceSession(m_oMembershipInterface.getInterfaceId(), m_oLastLpsSvcResponseInfo.sSessionId);
				}
			}
			if(bFailToPost)
				break;
		}
		
		if(bFailToPost)
			return false;
		else
			return true;
	}
	
	// membership point earn
	public boolean membershipPointEarn(PosInterfaceConfig oPosInterfaceConfig, FuncCheck oFuncCheck, FuncPayment oFuncPayment) {
		boolean bFailToPost = false;
		MemberInterfacePointEarnInfo oPostingInfo = new MemberInterfacePointEarnInfo();
		
		if(!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2))
			return false;
		
		//check facility code exist
		JSONObject oParam = oPosInterfaceConfig.getConfigValue().optJSONObject("general_setup").optJSONObject("params");
		if(!oParam.has("facility_code") || !oParam.optJSONObject("facility_code").has("value") || oParam.optJSONObject("facility_code").optString("value").isEmpty())
			return false;
		
		//check member exist
		String sMemberNo = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER);
		if (sMemberNo == null || sMemberNo.isEmpty())
			return false;
		
		String sFacilityCode = "";
		String sFacilityTypeCode = "";
		sFacilityCode = oParam.optJSONObject("facility_code").optString("value");
		sFacilityTypeCode = oParam.optJSONObject("facility_type_code").optString("value");
		String sMemberLastName = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_LAST_NAME);
		HashMap<Integer, String> oBonusCodes = oFuncCheck.getCheckExtraInfoValueListWithIndexBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_BONUS_CODE);
		
		oPostingInfo.iInterfaceId = m_oMembershipInterface.getInterfaceId();
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.iOutletId = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oPostingInfo.sEmployeeNumber = AppGlobal.g_oFuncUser.get().getUserNumber();
		oPostingInfo.sStationCode = AppGlobal.g_oFuncStation.get().getStation().getCode();
		oPostingInfo.sBusinessDate = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString();
		oPostingInfo.sMemberNumber = sMemberNo;
		oPostingInfo.sFacilityCode = sFacilityCode;
		oPostingInfo.sFacilityTypeCode = sFacilityTypeCode;
		oPostingInfo.sLastName = sMemberLastName;
		oPostingInfo.oBonusCodes = oBonusCodes;
		oPostingInfo.sSessionId = AppGlobal.getInterfaceSession(m_oMembershipInterface.getInterfaceId());
		
		if (!AppGlobal.g_oFuncOutlet.get().getBusinessDay().isCheckNumGeneratedByOutlet() && !oFuncCheck.isOldCheck())
			oPostingInfo.sCheckNumber = AppGlobal.g_oFuncStation.get().getNextCheckPrefixNumber(false);
		else
			oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
		
		DateTime oOpenLocTime = AppGlobal.getCurrentTime(false);
		oOpenLocTime = oFuncCheck.getOpenLocTime();
		DateTimeFormatter dateTimeFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		String sOpenLocDateTime = dateTimeFmt.print(oOpenLocTime);
		oPostingInfo.sOpenLocDateTime = sOpenLocDateTime;
		oPostingInfo.sTimeZone = String.valueOf(AppGlobal.g_oFuncOutlet.get().getShop().getTimezone());
		
		// do posting
		JSONObject oCheckInformationJSONObject = oFuncCheck.contrustSvcPostingCheckInformation(oFuncPayment.getCheckPaymentList(), oPostingInfo.oPreviousCheckPayments, oFuncCheck.getCheckGratuityList());
		PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
		boolean bResult = oInterfaceConfig.doMembershipPointEarn(oCheckInformationJSONObject, formMembershipEarnPointPostingJSONObject(oPostingInfo));
		
		if(bResult == false) {
			if(oInterfaceConfig.getLastErrorType() != null && oInterfaceConfig.getLastErrorType().equals(FuncPMS.ERROR_TYPE_PMS))
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("svc_return_error") + ":[" + oInterfaceConfig.getLastErrorMessage() + "]";
			else if(oInterfaceConfig.getLastErrorCode() != 0)
				m_sLastErrorMessage = getErrorMessage(oInterfaceConfig.getLastErrorCode());
			else
				m_sLastErrorMessage = oInterfaceConfig.getLastErrorMessage();
			bFailToPost = true;
		}else {
			JSONObject oResultJSON = oInterfaceConfig.getLastSuccessResult();
			m_oLastLpsSvcResponseInfo = new MemberInterfaceResponseInfo();
			m_oLastLpsSvcResponseInfo.sMemberNumber = oResultJSON.optString("memberNumber", "");
			m_oLastLpsSvcResponseInfo.sPointEarned = oResultJSON.optString("pointsEarned", "");
			m_oLastLpsSvcResponseInfo.sPointsBalance = oResultJSON.optString("pointsBalance", "");
			m_oLastLpsSvcResponseInfo.sAmountEarned = oResultJSON.optString("amountEarned", "");

			// Get and update session id if needed
			if(oResultJSON.has("sessionId")) {
				String sSession = oResultJSON.optString("sessionId", "");
				if(!sSession.isEmpty())
					AppGlobal.generateUpdateInterfaceSession(m_oMembershipInterface.getInterfaceId(), sSession);
			}
		}
		
		if(bFailToPost)
			return false;
		else
			return true;
	}
	
	//membership detail posting
	public boolean membershipDetailPosting(HashMap<String, String> oPostingInfoMap, FuncCheck oFuncCheck){
		if(!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN)) 
			return false;
		
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HHmmss");
		String sCurrentTime = timeFmt.print(today);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyyMMdd");
		String sCurrentDate = dateFmt.print(today);
		
		LpsSvcPostingInfo oPostingInfo = new LpsSvcPostingInfo();
		
		//interface id + outlet id + outlet code
		oPostingInfo.iInterfaceId = m_oMembershipInterface.getInterfaceId();
		oPostingInfo.iOutletId = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		
		//station id
		oPostingInfo.sStationId = AppGlobal.g_oFuncStation.get().getStation().getCode();
		
		//business day id
		oPostingInfo.sBusinessDayId = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId();
		
		//check number
		if (!AppGlobal.g_oFuncOutlet.get().getBusinessDay().isCheckNumGeneratedByOutlet() && !oFuncCheck.isOldCheck())
			oPostingInfo.sCheckNumber = AppGlobal.g_oFuncStation.get().getNextCheckPrefixNumber(false);
		else
			oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
		
		//date
		oPostingInfo.sDate = sCurrentDate;
		
		//time
		oPostingInfo.sTime = sCurrentTime;
		
		//item list: item code + item qty + net amount
		oPostingInfo.oItemList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> oItemInfo = new HashMap<String,String>();
		
		for (List<FuncCheckItem> oFuncCheckItemList: oFuncCheck.getWholeItemList()) {
			for (FuncCheckItem oFuncCheckItem: oFuncCheckItemList) {
				oItemInfo.put("itemCode", oFuncCheckItem.getCheckItem().getCode());
				oItemInfo.put("itemQty", oFuncCheckItem.getCheckItem().getQty().toString());
				oItemInfo.put("netAmount", oFuncCheckItem.getItemNetTotal(false).toPlainString());
				//oItemInfo.put("itemDesc", oFuncCheckItem.getCheckItem().getName(AppGlobal.g_oCurrentLangIndex.get()));
				String sChargeTaxType = oFuncCheckItem.getChargeTaxType();
				String sScType = oFuncCheckItem.getServiceChargeType();
				BigDecimal dTmpNetAmt = BigDecimal.ZERO;
				if(sChargeTaxType.equals(PosCheckItem.CHARGE_TAX_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN)){
					// item contains inclusive tax
					//oItemInfo.put("vat", "Y");
					
					for(int i = 1; i <= 4; i++)
						oItemInfo.put("tax"+i, oFuncCheckItem.getInclusiveTaxAmount(i, true).toPlainString());
				}else{
					// item does not contain inclusive tax or inclusive tax with breakdown
					if(sChargeTaxType.equals(PosCheckItem.CHARGE_TAX_CHARGED_IN_ITEM_PRICE))
						dTmpNetAmt = oFuncCheckItem.getTaxTotal(false);
					
					/*	oItemInfo.put("vat", "Y");
					else
						oItemInfo.put("vat", "N");*/
					
					BigDecimal dTmpTaxAmt = BigDecimal.ZERO;
					for(int i = 1; i <= 25; i++){
						if(i <= 3)
							oItemInfo.put("tax"+i, oFuncCheckItem.getTaxAmount(i, true).toPlainString());
						else
							// tax4 will store tax value from 4-25
							dTmpTaxAmt = dTmpTaxAmt.add( oFuncCheckItem.getTaxAmount(i, true));
					}
					oItemInfo.put("tax4", dTmpTaxAmt.toPlainString());
				}
				BigDecimal dTmpScAmt = BigDecimal.ZERO;
				if(sScType.equals(PosCheckItem.CHARGE_SC_INCLUSIVE_IN_ITEM_PRICE_WITHOUT_BREAKDOWN)){
					for(int i = 1; i <= 5; i++){
						String sScInclTaxKey = String.format("incl_sc_ref%d", i); 
						if(i == 1)
							oItemInfo.put("serviceCharge"+i, AppGlobal.g_oFuncOutlet.get().roundSCAmountToBigDecimal(oFuncCheckItem.getTaxScRefByVariable(sScInclTaxKey)).toPlainString());
						else
							dTmpScAmt = dTmpScAmt.add(AppGlobal.g_oFuncOutlet.get().roundSCAmountToBigDecimal(oFuncCheckItem.getTaxScRefByVariable(sScInclTaxKey)));
					}
					oItemInfo.put("serviceCharge2", AppGlobal.g_oFuncOutlet.get().roundSCAmountToBigDecimal(dTmpScAmt).toPlainString());
				}else{
					// item does not contain inclusive sc or inclusive sc with breakdown
					for(int i = 1; i <= 5; i++){
						if(i == 1)
							oItemInfo.put("serviceCharge"+i, oFuncCheckItem.getScAmount(i, true).toPlainString());
						else
							dTmpScAmt = dTmpScAmt.add(oFuncCheckItem.getScAmount(i, true));
					}
					oItemInfo.put("serviceCharge2", AppGlobal.g_oFuncOutlet.get().roundSCAmountToBigDecimal(dTmpScAmt).toPlainString());
					if(sScType.equals(PosCheckItem.CHARGE_SC_CHARGED_IN_ITEM_PRICE))
						dTmpNetAmt = dTmpNetAmt.add(oFuncCheckItem.getScTotal(false));
				}
				
				oItemInfo.put("netAmount", oFuncCheckItem.getItemNetTotal(false).add(dTmpNetAmt).toPlainString());
				oItemInfo.put("discount", oFuncCheckItem.getItemDiscountTotal().toPlainString());
				//oItemInfo.put("revenueAmount", oFuncCheckItem.getItemNetTotal(false).subtract(oFuncCheckItem.getTaxTotal())
						//.subtract(oFuncCheckItem.getScTotal()).subtract(oFuncCheckItem.getInclusiveTaxTotal()).toPlainString());
				oPostingInfo.oItemList.add(oItemInfo);
				oItemInfo = new HashMap<String,String>();
			}
		}
		
		PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
		boolean bResult = oInterfaceConfig.doMembershipDetailPosting(fromSvcPostingJSONObject(oPostingInfo));
		
		return bResult;
	}
	
	//membership serial port posting
	public boolean memberSerialPortPosting(FuncCheck oFuncCheck, PosCheckPayment oCheckPayment, PosPaymentMethod oPaymentMethod, int iCurrentPaymentSeq, BigDecimal dPreviousPaymentTotal, List<PosCheckPayment> oAllPaymentList, boolean bVoidPosting){
		if(!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_2700))
			return false;
		
		int i=0, iCurrentPostingIndex = 0;
		boolean bFailToPost = false;
		List<LpsSvcPostingInfo> oPostingInfoList = new ArrayList<LpsSvcPostingInfo>();
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm:ss");
		String sCurrentTime = timeFmt.print(today);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String sCurrentDate = dateFmt.print(today);
		
		LpsSvcPostingInfo oPostingInfo = new LpsSvcPostingInfo();
		
		oPostingInfo.iCheckPaymnetIndex = i;
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.sEmployeeNumber = AppGlobal.g_oFuncUser.get().getUserNumber();
		oPostingInfo.sTraceId = "";
		oPostingInfo.sPostingKey = "";
		oPostingInfo.sCurrencyCode = AppGlobal.g_oFuncOutlet.get().getCurrencyCode();
		oPostingInfo.sCurrencySign = AppGlobal.g_oFuncOutlet.get().getCurrencySign();
		oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
		if (!AppGlobal.g_oFuncOutlet.get().getBusinessDay().isCheckNumGeneratedByOutlet() && !oFuncCheck.isOldCheck())
			oPostingInfo.sCheckNumber = AppGlobal.g_oFuncStation.get().getNextCheckPrefixNumber(false);
		oPostingInfo.sCheckGroupNumber = "";
		oPostingInfo.iCover = oFuncCheck.getCover();
		if(oFuncCheck.getCheckBusinessPeriodId().compareTo("") == 0)
			oPostingInfo.sServingPeriod = AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getCode();
		else
			oPostingInfo.sServingPeriod = AppGlobal.g_oFuncOutlet.get().getBusinessPeriodById(oFuncCheck.getCheckBusinessPeriodId()).getCode();
		oPostingInfo.dPaymentAmount = oCheckPayment.getPayTotal();
		oPostingInfo.dTips = oCheckPayment.getPayTips();
		oPostingInfo.dPostAmount = oCheckPayment.getPayTotal().add(oCheckPayment.getPayTips());
		oPostingInfo.dPreviousPaymentAmount = dPreviousPaymentTotal;
		oPostingInfo.sCardNo = "";
		oPostingInfo.sBusinessDate = AppGlobal.g_oFuncOutlet.get().getFormat1BusinessDayInString();
		oPostingInfo.sDate = sCurrentDate+"T00:00:00";
		oPostingInfo.sTime = sCurrentTime;
		
		oPostingInfo.sInterfaceVendorKey = "";
		oPostingInfo.sRoomNumber = "";
		oPostingInfo.sGuestNumber = "";
		oPostingInfo.sGuestName = "";
		oPostingInfo.sExpiryDate = "";
		oPostingInfo.sFieldNumber = "";
		oPostingInfo.sTable = oFuncCheck.getTableNoWithExtensionForDisplay();
		oPostingInfo.sPaymentType = oPaymentMethod.getPaymentCode();
		
		for(PosCheckExtraInfo oPosCheckExtraInfo:oCheckPayment.getCheckExtraInfoArrayList()){
			if(oPosCheckExtraInfo.getBy().equals(PosCheckExtraInfo.BY_PAYMENT) &&
					oPosCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE)){
				
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_INTERFACE_ID)){
					oPostingInfo.iInterfaceId = Integer.parseInt(oPosCheckExtraInfo.getValue());
					
					for(PosInterfaceConfig oPosInterfaceConfig:AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_MEMBERSHIP_INTERFACE)){
						if(oPosInterfaceConfig.getInterfaceId() == oPostingInfo.iInterfaceId){
							oPostingInfo.sInterfaceVendorKey = oPosInterfaceConfig.getInterfaceVendorKey();
							break;
						}
					}
				}
				if(oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER)){
					if(oPosCheckExtraInfo.getValue() != null){
						oPostingInfo.sRoomNumber = oPosCheckExtraInfo.getValue();
					}
				}
			}
		}
		oPostingInfoList.add(oPostingInfo);
		
		// no posting is needed
		if(oPostingInfoList.isEmpty())
			return true;
		
		PosInterfaceConfig oMembershipSerialPortInterfaceConfig = null;
		
		// do posting
		for(i=0; i<oPostingInfoList.size(); i++) {
			PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
			boolean bResult = false;
			if(oPostingInfoList.get(i).sInterfaceVendorKey.equals(InfVendor.KEY_2700)) {
				//get interface setup
				for(PosInterfaceConfig oPosInterfaceConfig:AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_MEMBERSHIP_INTERFACE)){
					if(oPosInterfaceConfig.getInterfaceId() == oPostingInfoList.get(i).iInterfaceId){
						oMembershipSerialPortInterfaceConfig = oPosInterfaceConfig;
						break;
					}
				}
				
				FuncMembershipInterfaceSerialPort oFuncMembershipInterfaceSerialPort = new FuncMembershipInterfaceSerialPort();
				oFuncMembershipInterfaceSerialPort.init(oMembershipSerialPortInterfaceConfig);
				bResult = oFuncMembershipInterfaceSerialPort.doMembershipPosting(oFuncCheck, oCheckPayment, formSerialPortPostingJSONObject(oPostingInfoList.get(i)), bVoidPosting);
			}
			
			if(bResult == false) {
				if(oInterfaceConfig.getLastErrorType() != null && oInterfaceConfig.getLastErrorType().equals(FuncPMS.ERROR_TYPE_PMS))
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("pms_return_error") + ":[" + oInterfaceConfig.getLastErrorMessage() + "]";
				else if(oInterfaceConfig.getLastErrorCode() != 0)
					m_sLastErrorMessage = getErrorMessage(oInterfaceConfig.getLastErrorCode());
				else
					m_sLastErrorMessage = oInterfaceConfig.getLastErrorMessage();
				bFailToPost = true;
				iCurrentPostingIndex = i;
			}
			
			if(bFailToPost)
				break;
		}
		
		// void posting if fail to Post
		if(bFailToPost) {
			if(!bVoidPosting){
				// do void posting
				for(i=0; i<iCurrentPostingIndex; i++) {
					FuncMembershipInterfaceSerialPort oFuncMembershipInterfaceSerialPort = new FuncMembershipInterfaceSerialPort();
					oFuncMembershipInterfaceSerialPort.init(oMembershipSerialPortInterfaceConfig);
					if(oFuncMembershipInterfaceSerialPort.doMembershipPosting(oFuncCheck, oCheckPayment, formSerialPortPostingJSONObject(oPostingInfoList.get(i)), true) == false)
						return false;
				}
			}
			return false;
		}
		
		return true;
		
	}
	
	public boolean membershipRegistration(HashMap<String, String> oEnquiryInfo) {
		m_bConnectError = false;
		
		//Generate the membership registration information
		oEnquiryInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oEnquiryInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oEnquiryInfo.put("stationCode", AppGlobal.g_oFuncStation.get().getStation().getCode());
		oEnquiryInfo.put("employeeNum", AppGlobal.g_oFuncUser.get().getUserNumber());
		
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2)) {
			oEnquiryInfo.put("sessionId", AppGlobal.getInterfaceSession(m_oMembershipInterface.getInterfaceId()));
			
			JSONObject oInterfaceConfig = null;
			// get facility_code, facility type_code and enrollment_source_code from interface
			oInterfaceConfig = m_oMembershipInterface.getConfigValue();
			if (oInterfaceConfig != null && (oInterfaceConfig.has("general_setup") && 
					oInterfaceConfig.optJSONObject("general_setup").has("params") && 
					oInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").has("facility_code") &&
					oInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").has("facility_type_code") &&
					oInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").has("enrollment_source_code"))) {
				oEnquiryInfo.put("sourceCode", oInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").optJSONObject("enrollment_source_code").optString("value"));
				oEnquiryInfo.put("facilityCode", oInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").optJSONObject("facility_code").optString("value"));
				oEnquiryInfo.put("facilityType", oInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").optJSONObject("facility_type_code").optString("value"));
			}
		}
		JSONObject oResultJSONObject = m_oMembershipInterface.membershipRegistration(oEnquiryInfo);
		m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
		if(oResultJSONObject == null) {
			m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
			return false;
		}
		
		if(!oResultJSONObject.has("enquiryResult")) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("result_error");
			return false;
		}
		
		// Get and update session id if needed
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2) && oResultJSONObject.has("sessionId")) {
			String sSession = oResultJSONObject.optString("sessionId", "");
			if(!sSession.isEmpty())
				AppGlobal.generateUpdateInterfaceSession(m_oMembershipInterface.getInterfaceId(), sSession);
		}
		
		if(oResultJSONObject.optBoolean("enquiryResult", false) == false) {
			if (oResultJSONObject.optInt("errorCode", 0) == 0)
				m_sLastErrorMessage = oResultJSONObject.optString("errorMessage");
			else {
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("member_registration_failed") + System.lineSeparator()
						+ AppGlobal.g_oLang.get()._("message") + ": "
						+ getErrorMessage(oResultJSONObject.optInt("errorCode", 0));
			}
			return false;
		}
		m_oLastLpsSvcResponseInfo = new MemberInterfaceResponseInfo();
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2)) {
			m_oLastLpsSvcResponseInfo.sMemberNumber = oResultJSONObject.optJSONObject("memberDetails").optString("cardNumber");
			m_oLastLpsSvcResponseInfo.sMemberName = oResultJSONObject.optJSONObject("memberDetails").optString("surname") + ", " + oResultJSONObject.optJSONObject("memberDetails").optString("firstName") ;
		}
		else{
			m_oLastLpsSvcResponseInfo.sMemberNumber = oEnquiryInfo.get("cardNumber");
			m_oLastLpsSvcResponseInfo.sMemberLastName = oEnquiryInfo.get("surname");
		}
		return true;
	}
	
	private JSONObject fromSvcPostingJSONObject(LpsSvcPostingInfo oPostingInfo) {
		JSONObject oPostingJSONObject = new JSONObject();
		try {
			if(this.m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL) && isOfflinePosting())
				oPostingJSONObject.put("preparePosting", 1);
			if(this.m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASCENTIS_CRM)){
				oPostingJSONObject.put("postingKey", oPostingInfo.sPostingKey);
				oPostingJSONObject.put("pointUsed", oPostingInfo.sPointUsed);
				if(!oPostingInfo.sPointUsedByDiscount.isEmpty())
					oPostingJSONObject.put("pointUsedByDiscount", oPostingInfo.sPointUsedByDiscount);
				if(oPostingInfo.dPointPaymentTotal != null)
					oPostingJSONObject.put("pointPaymentTotal", oPostingInfo.dPointPaymentTotal.toPlainString());
				JSONArray oVoucherArray = new JSONArray();
				if(oPostingInfo.oVoucherList != null && oPostingInfo.oVoucherList.size() > 0){
					for(HashMap<String, String> oHashMap : oPostingInfo.oVoucherList){
						JSONObject oVoucherObject = new JSONObject();
						oVoucherObject.put("voucherCode", oHashMap.get("voucherNumber"));
						oVoucherObject.put("voucherType", oHashMap.get("voucherType"));
						oVoucherObject.put("voucherValue", oHashMap.get("voucherValue"));
						oVoucherArray.put(oVoucherObject);
					}
					oPostingJSONObject.put("voucherList", oVoucherArray);
				}
			}
			if(this.m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_EPOS)){
				oPostingJSONObject.put("promotionCodes", oPostingInfo.sPomotionCodes);
				oPostingJSONObject.put("postingKey", oPostingInfo.sPostingKey);
				oPostingJSONObject.put("paymentType", oPostingInfo.sPaymentType);
				oPostingJSONObject.put("pointPaymentTotal", oPostingInfo.dPointPaymentTotal);
				oPostingJSONObject.put("amountPaymentTotal", oPostingInfo.dPaymentAmount);
				oPostingJSONObject.put("currencyCode", oPostingInfo.sCurrencyCode);
				oPostingJSONObject.put("currencySign", oPostingInfo.sCurrencySign);
			}
			
			if(this.m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_YAZUO_CRM)){
				if(oPostingInfo.dPointPaymentTotal != null)
					oPostingJSONObject.put("pointValue", oPostingInfo.dPointPaymentTotal.toPlainString());
				if(oPostingInfo.dStoreValuePaymentTotal != null)
					oPostingJSONObject.put("accountValue", oPostingInfo.dStoreValuePaymentTotal.toPlainString());
				oPostingJSONObject.put("paymentType", oPostingInfo.iPaymentType);
				if(oPostingInfo.dPaymentAmount == null)
					oPostingJSONObject.put("paymentAmount", 0);
				else
					oPostingJSONObject.put("paymentAmount", oPostingInfo.dPaymentAmount);
				if(oPostingInfo.dTips == null)
					oPostingJSONObject.put("tips", 0);
				else
					oPostingJSONObject.put("tips", oPostingInfo.dTips);
				if(oPostingInfo.sPassword == null)
					oPostingJSONObject.put("password", "000000");
				else
					oPostingJSONObject.put("password", oPostingInfo.sPassword);
				
				JSONArray oVoucherArrayList = new JSONArray();
				JSONArray oVoucherArray = new JSONArray();
				if (oPostingInfo.oVoucherList != null && !oPostingInfo.oVoucherList.isEmpty()) {
					for(HashMap<String, String> oHashMap : oPostingInfo.oVoucherList){
						oVoucherArray = new JSONArray();
						for(String sKey: oHashMap.keySet()){
							oVoucherArray.put(sKey);
							oVoucherArray.put(oHashMap.get(sKey));
						}
						oVoucherArrayList.put(oVoucherArray);
					}
					oPostingJSONObject.put("voucherCheckDisList", oVoucherArrayList);
				}
				
				JSONArray oVoucherItemArrayList = new JSONArray();
				JSONArray oVoucherItemArray = new JSONArray();
				if (oPostingInfo.oItemVoucherList  != null && !oPostingInfo.oItemVoucherList.isEmpty()) {
					for(HashMap<String, String> oHashMap : oPostingInfo.oItemVoucherList){
						oVoucherItemArray = new JSONArray();
						for(String sKey: oHashMap.keySet()){
							oVoucherItemArray.put(sKey);
							oVoucherItemArray.put(oHashMap.get(sKey));
						}
						oVoucherItemArrayList.put(oVoucherItemArray);
					}
					oPostingJSONObject.put("voucherItemList", oVoucherItemArrayList);
				}
			}
			
			if(this.m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN) || this.m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN_PMS))
					oPostingJSONObject.put("isFromPms", oPostingInfo.bIsFromPms);
			if(this.m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN)) {
				if(!oPostingInfo.sReferenceNo.isEmpty())
					oPostingJSONObject.put("referenceNo", oPostingInfo.sReferenceNo);
				if(!oPostingInfo.sVoucherAmount.isEmpty())
					oPostingJSONObject.put("voucherAmount", oPostingInfo.sVoucherAmount);
				if(!oPostingInfo.sVoucherAmount.isEmpty())
					oPostingJSONObject.put("voucherType", oPostingInfo.sVoucherType);
				if(!oPostingInfo.sBusinessDayId.isEmpty())
					oPostingJSONObject.put("businessDayId", oPostingInfo.sBusinessDayId);
				
				JSONArray oItemArrayList = new JSONArray();
				JSONObject oItemJSONObject = new JSONObject();
				
				//for pos detail
				if(oPostingInfo.oItemList != null && !oPostingInfo.oItemList.isEmpty()){
					for(HashMap<String, String> oHashMap : oPostingInfo.oItemList){
						oItemJSONObject = new JSONObject();
						for(String sKey: oHashMap.keySet())
							oItemJSONObject.put(sKey, oHashMap.get(sKey));
						oItemArrayList.put(oItemJSONObject);
					}
					oPostingJSONObject.put("itemList", oItemArrayList);
				}
			}
			if(this.m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_CONCEPT)){
				oPostingJSONObject.put("paymentType", oPostingInfo.sPaymentType);
				oPostingJSONObject.put("pointUsed", oPostingInfo.sPointUsed);
			}
			if(this.m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2)) {
				JSONObject oJsonObject = new JSONObject();
				oJsonObject.put("code", oPostingInfo.sFacilityCode);
				oJsonObject.put("type", oPostingInfo.sFacilityTypeCode);
				oPostingJSONObject.put("facility", oJsonObject);
				oPostingJSONObject.put("businessDate", oPostingInfo.sBusinessDate);
				oPostingJSONObject.put("transactionTime", oPostingInfo.sTransactionTime);
				oPostingJSONObject.put("redemptionAmount", oPostingInfo.sRedemptionAmount);
				
				//Award Cancellation
				if(!oPostingInfo.sAwardCode.isEmpty())
					oPostingJSONObject.put("postingKey", oPostingInfo.sAwardCode);
				if(!oPostingInfo.sPointRedeem.isEmpty())
					oPostingJSONObject.put("redemptionAmount", oPostingInfo.sPointRedeem);

			}
			
			oPostingJSONObject.put("interfaceId", oPostingInfo.iInterfaceId);
			oPostingJSONObject.put("requestCode", oPostingInfo.sRequestCode);
			oPostingJSONObject.put("hotelCode", oPostingInfo.sHotelCode);
			oPostingJSONObject.put("outletId", oPostingInfo.iOutletId);
			oPostingJSONObject.put("outletCode", oPostingInfo.sOutletCode);
			oPostingJSONObject.put("stationCode", oPostingInfo.sStationCode);
			oPostingJSONObject.put("memberNumber", oPostingInfo.sMemberNumber);
			oPostingJSONObject.put("memberName", oPostingInfo.sMemberName);
			oPostingJSONObject.put("memberLastName", oPostingInfo.sMemberLastName);
			oPostingJSONObject.put("cardNumber", oPostingInfo.sCardNo);
			oPostingJSONObject.put("cardStatus", oPostingInfo.sCardStatus);
			oPostingJSONObject.put("arAccountNumber", oPostingInfo.sArAccountNumber);
			if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_LPS_SVC)) {
				oPostingJSONObject.put("password", oPostingInfo.sPassword);
				if (oPostingInfo.iAttributeId != 0)
					oPostingJSONObject.put("attributeId", oPostingInfo.iAttributeId);
			}
			oPostingJSONObject.put("checkNumber", oPostingInfo.sCheckNumber);
			oPostingJSONObject.put("cover", oPostingInfo.iCover);
			//oPostingJSONObject.put("paymentAmount", oPostingInfo.dPaymentAmount.toPlainString());
			//oPostingJSONObject.put("paymentId", oPostingInfo.iPaymentId);
			//oPostingJSONObject.put("paymentName", oPostingInfo.sPaymentName);
			oPostingJSONObject.put("employeeNumber", oPostingInfo.sEmployeeNumber);
			oPostingJSONObject.put("employeeName", oPostingInfo.sEmployeeName);
			//oPostingJSONObject.put("tips", oPostingInfo.dTips.toPlainString());
			oPostingJSONObject.put("traceId", oPostingInfo.sTraceId);
			oPostingJSONObject.put("terminalId", oPostingInfo.sStationId);
			oPostingJSONObject.put("localBalance", oPostingInfo.dLocalBalance);
			oPostingJSONObject.put("accountBalance", oPostingInfo.dAccountBalance);
			oPostingJSONObject.put("pointBalance", oPostingInfo.dPointsBalance);
			oPostingJSONObject.put("date", oPostingInfo.sDate);
			oPostingJSONObject.put("time", oPostingInfo.sTime);
			oPostingJSONObject.put("sessionId", oPostingInfo.sSessionId);
			oPostingJSONObject.put("checkDateTime", oPostingInfo.sCheckDateTime);
			oPostingJSONObject.put("timeZone", oPostingInfo.sPostingTimeZone);

			if(oPostingInfo.dPreviousPaymentTotal != null)
				oPostingJSONObject.put("previousAmount", oPostingInfo.dPreviousPaymentTotal.toPlainString());
			oPostingJSONObject.put("pointPosting", oPostingInfo.iPointPosting);
			oPostingJSONObject.put("paymentSequence", oPostingInfo.sPaymentSequence);
			if(oPostingInfo.dPostAmount != null)
				oPostingJSONObject.put("dPostAmount", oPostingInfo.dPostAmount.toPlainString());
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		return oPostingJSONObject;
	}
	
	private JSONObject formMembershipEarnPointPostingJSONObject(MemberInterfacePointEarnInfo oPostingInfo) {
		JSONObject oPostingJSONObject = new JSONObject();
		try {
			oPostingJSONObject.put("interfaceId", oPostingInfo.iInterfaceId);
			oPostingJSONObject.put("outletCode", oPostingInfo.sOutletCode);
			oPostingJSONObject.put("outletId", oPostingInfo.iOutletId);
			oPostingJSONObject.put("employeeNum", oPostingInfo.sEmployeeNumber);
			oPostingJSONObject.put("stationCode", oPostingInfo.sStationCode);
			oPostingJSONObject.put("businessDate", oPostingInfo.sBusinessDate);
			oPostingJSONObject.put("traceId", oPostingInfo.sTraceId);
			
			if(oPostingInfo.oBonusCodes != null && !oPostingInfo.oBonusCodes.isEmpty())
				oPostingJSONObject.put("bonusCodes", oPostingInfo.oBonusCodes);
			
			oPostingJSONObject.put("sessionId", oPostingInfo.sSessionId);
			oPostingJSONObject.put("checkDateTime", oPostingInfo.sOpenLocDateTime);
			oPostingJSONObject.put("timeZone", oPostingInfo.sTimeZone);
			oPostingJSONObject.put("checkNumber", oPostingInfo.sCheckNumber);
			oPostingJSONObject.put("lastName", oPostingInfo.sLastName);
			oPostingJSONObject.put("memberNumber", oPostingInfo.sMemberNumber);
			
			JSONObject oFacility = new JSONObject();
			
			if(oPostingInfo.sFacilityCode != null && !oPostingInfo.sFacilityCode.isEmpty())
				oFacility.put("code", oPostingInfo.sFacilityCode);
			if(oPostingInfo.sFacilityTypeCode != null && !oPostingInfo.sFacilityTypeCode.isEmpty())
				oFacility.put("type", oPostingInfo.sFacilityTypeCode);
			oPostingJSONObject.put("facility", oFacility);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		return oPostingJSONObject;
	}
	
	//prepare serial port posting JSON object
	private JSONObject formSerialPortPostingJSONObject(LpsSvcPostingInfo lpsSvcPostingInfo) {
		JSONObject oPostingJSONobject = new JSONObject();
		
		try {
			oPostingJSONobject.put("interfaceId", lpsSvcPostingInfo.iInterfaceId);
			oPostingJSONobject.put("interfaceVendorKey", m_oMembershipInterface.getInterfaceVendorKey());
			oPostingJSONobject.put("outlet", lpsSvcPostingInfo.sOutletCode);
			oPostingJSONobject.put("employee", lpsSvcPostingInfo.sEmployeeNumber);
			oPostingJSONobject.put("roomNumber", lpsSvcPostingInfo.sRoomNumber);
			oPostingJSONobject.put("guestNumber", lpsSvcPostingInfo.sGuestNumber);
			oPostingJSONobject.put("guestName", lpsSvcPostingInfo.sGuestName);
			oPostingJSONobject.put("expiryDate", "");
			oPostingJSONobject.put("fieldNumber", "");
			oPostingJSONobject.put("checkNumber", lpsSvcPostingInfo.sCheckNumber);
			oPostingJSONobject.put("checkGroupNumber", lpsSvcPostingInfo.sCheckGroupNumber);
			oPostingJSONobject.put("table", lpsSvcPostingInfo.sTable);
			oPostingJSONobject.put("cover", lpsSvcPostingInfo.iCover);
			oPostingJSONobject.put("servingPeriod", lpsSvcPostingInfo.sServingPeriod);
			oPostingJSONobject.put("paymentType", lpsSvcPostingInfo.sPaymentType);
			oPostingJSONobject.put("paymentAmount", lpsSvcPostingInfo.dPaymentAmount.toPlainString());
			oPostingJSONobject.put("tips", lpsSvcPostingInfo.dTips.toPlainString());
			oPostingJSONobject.put("postAmount", lpsSvcPostingInfo.dPostAmount.toPlainString());
			oPostingJSONobject.put("previousPaymentAmount", lpsSvcPostingInfo.dPreviousPaymentAmount.toPlainString());
			oPostingJSONobject.put("businessDate", lpsSvcPostingInfo.sBusinessDate);
			oPostingJSONobject.put("date", lpsSvcPostingInfo.sDate);
			oPostingJSONobject.put("time", lpsSvcPostingInfo.sTime);
			
		}catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		
		return oPostingJSONobject;
	}
	
	
	public void addSvcPostingResult(PosCheckPayment oPosCheckPayment) {
		//trace id
		oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_TRACE_ID, 0, m_oLastLpsSvcResponseInfo.sTraceId);
		//local balance
		oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_LOCAL_BALANCE, 0, m_oLastLpsSvcResponseInfo.sLocalBalance);
		//account name
		oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NAME, 0, m_oLastLpsSvcResponseInfo.sMemberName);
		//english name
		oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_ENGLISH_NAME, 0, m_oLastLpsSvcResponseInfo.sEnglishName);
		//card type name
		oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_CARD_TYPE_NAME, 0, m_oLastLpsSvcResponseInfo.sCardTypeName);
		//print line 1-2
		for(int i=1; i<=2; i++) {
			oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_PRINT_LINE, i, m_oLastLpsSvcResponseInfo.sPrintLines[(i-1)]);
		}
		
		//points balance
		oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_POINTS_BALANCE, 0, m_oLastLpsSvcResponseInfo.sPointsBalance);
		//points earn
		oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_POINTS_EARN, 0, m_oLastLpsSvcResponseInfo.sPoints);
		//card sn
		oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_CARD_SN, 0, m_oLastLpsSvcResponseInfo.sCardNumber);
		//expiry date
		oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_EXPIRY_DATE, 0, m_oLastLpsSvcResponseInfo.sExpiryDate);
		//card level name
		oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_CARD_LEVEL_NAME, 0, m_oLastLpsSvcResponseInfo.sCardLevelName);
	}
	
	// Add extra info for SVC point function
	public void updateSvcPointFunctionResult(FuncCheck oFuncCheck, String sAccountNumber, String sPassword) {
		if(oFuncCheck == null)
			return;
		
		oFuncCheck.addExtraInfoForSVCPointFunction(sAccountNumber, sPassword, m_oLastLpsSvcResponseInfo.sPointsBalance, m_oLastLpsSvcResponseInfo.sPoints);
	}
	
	// Add extra info for Ascentis CRM point function
	public void updateAscentisCrmPointFunctionResult(FuncCheck oFuncCheck, String sPostingKey, String sPointEarned) {
		if(oFuncCheck == null)
			return;
		
		oFuncCheck.addExtraInfoForAscentisCrmPointFunction(m_oLastLpsSvcResponseInfo.sPointsBalance, sPointEarned, sPostingKey, m_oLastLpsSvcResponseInfo.sTotalPointsBalance);
	}
	
	// Add extra info for Yazuo CRM  function
	public void updateYazuoCrmFunctionResult(FuncCheck oFuncCheck,String sCardNo,String sMemberNo,String sMemberName, String sTraceId, String sPointBalance, String sPointEarned, String sStoreValueBalance) {
		if(oFuncCheck == null)
			return;
		
		oFuncCheck.addExtraInfoForYazuoCrmFunction(sCardNo, sMemberNo, sMemberName, sTraceId, sPointBalance, sPointEarned, sStoreValueBalance);
	}
	
	
	// Add extra info for Concept Posting
	public void addConceptPostingExtraInfo(PosCheckPayment oPosCheckPayment){
		String sPostingString = m_oLastLpsSvcResponseInfo.sPostingString;
		byte[] sPostingStringByte = sPostingString.getBytes();
		
		BASE64Encoder encoder = new BASE64Encoder();
		String sEncryptedStr = encoder.encode(sPostingStringByte);
		
		oPosCheckPayment.addExtraInfoToList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_POSTING_STRING, 0, sEncryptedStr);
	}
	
	// svc void posting
	public boolean membershipVoidPosting(FuncCheck oFuncCheck, List<PosCheckPayment> oCheckPayments, BigDecimal dPreviousPaymentTotal, boolean bPointPosting, int iPaymentSequence) {
		boolean bFailToPost = false;

		List<LpsSvcPostingInfo> oPostingInfoList = new ArrayList<LpsSvcPostingInfo>();
		PosCheckExtraInfo oTempExtraInfo = null;
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HHmmss");
		String sCurrentTime = timeFmt.print(today);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyyMMdd");
		String sCurrentDate = dateFmt.print(today);
		String sMemberNumber = "", sMemberName = "", sCardNumber = "", sTraceId = "", sArAccNumber = "", sPassword = "", sPostingKey = "", sPaymentType = "", sPointUsed = "";
		String sAwardCode = "", sMemberLastName = "", sPointRedeem = "", sFacilityCode = "", sFacilityTypeCode = "", sEmployeeName = "", sTimeZone = "", sCheckDateTime = "", sSessionId = "", sCheckNo = "", sCheckDate = "";
		
		if (bPointPosting) {
			// Void SVC point function
			String sTmp = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_POINTS_ACCOUNT_NUMBER);
			if (sTmp != null)
				sMemberNumber = sTmp;
			sTmp = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_POINTS_ACCOUNT_PASSWORD);
			if (sTmp != null)
				sPassword = sTmp;
			
			for(PosCheckPayment oCheckPayment: oCheckPayments) {
				if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_EPOS)) {
					oTempExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0);
					if(oTempExtraInfo != null)
						sMemberNumber = oTempExtraInfo.getValue();
					sPostingKey = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_TRACE_ID);
				}
			}
		} else {
			// Get Payment Level Extra Info
			for(PosCheckPayment oCheckPayment: oCheckPayments) {
				if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_LPS_SVC)) {
					oTempExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_ACCOUNT_NUMBER, 0);
					if(oTempExtraInfo != null)
						sMemberNumber = oTempExtraInfo.getValue();
					
					oTempExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_PASSWORD, 0);
					if(oTempExtraInfo != null)
						sPassword = oTempExtraInfo.getValue();
				}else if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_LPS_SVC_COUPON)) {
					oTempExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_SVC_COUPON_NO, 0);
					if(oTempExtraInfo != null)
						sMemberNumber = oTempExtraInfo.getValue();
				}else if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL)) {
					oTempExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0);
					if(oTempExtraInfo != null)
						sMemberNumber = oTempExtraInfo.getValue();
					
					oTempExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NAME, 0);
					if(oTempExtraInfo != null)
						sMemberName = oTempExtraInfo.getValue();
					
					oTempExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_CARD_NO, 0);
					if(oTempExtraInfo != null)
						sCardNumber = oTempExtraInfo.getValue();
					
					oTempExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_AR_ACCOUNT_NUMBER, 0);
					if(oTempExtraInfo != null)
						sArAccNumber = oTempExtraInfo.getValue();
					
					oTempExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_TRACE_ID, 0);
					if(oTempExtraInfo != null)
						sTraceId = oTempExtraInfo.getValue()+"_V";
				}
				else if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_CONCEPT)) {
					oTempExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_POINTS_USE, 0);
					if(oTempExtraInfo != null)
						sPointUsed = oTempExtraInfo.getValue();
				}
				else if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2)){
					oTempExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID, 0);
					//check current payment is general_v2 or not
					if(oTempExtraInfo != null && oTempExtraInfo.getValue().equals(Integer.toString(m_oMembershipInterface.getInterfaceId()))) {
						oTempExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER, 0);
						if(oTempExtraInfo != null)
							sMemberNumber = oTempExtraInfo.getValue();
						
						oTempExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_LAST_NAME, 0);
						if(oTempExtraInfo != null)
							sMemberLastName = oTempExtraInfo.getValue();
						
						oTempExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_AWARD_CODE, 0);
						if(oTempExtraInfo != null)
							sAwardCode = oTempExtraInfo.getValue();
						
						oTempExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_POINT_REDEEM, 0);
						if(oTempExtraInfo != null)
							sPointRedeem = oTempExtraInfo.getValue();
						
						PosInterfaceConfig oPosInterfaceConfig = AppGlobal.getPosInterfaceConfigById(m_oMembershipInterface.getInterfaceId());
						JSONObject oParam = oPosInterfaceConfig.getConfigValue().optJSONObject("general_setup").optJSONObject("params");
						sFacilityCode = oParam.optJSONObject("facility_code").optString("value");
						sFacilityTypeCode = oParam.optJSONObject("facility_type_code").optString("value");
						
						timeFmt = DateTimeFormat.forPattern("HH:mm:ss");
						dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
						sCurrentDate = timeFmt.print(today);
						sCurrentTime = dateFmt.print(today);
						
						DateTimeFormatter dateTimeFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
						sCheckDateTime = dateTimeFmt.print(oFuncCheck.getOpenLocTime());
						
						sTimeZone = Integer.toString(AppGlobal.g_oFuncOutlet.get().getShop().getTimezone());
						sEmployeeName = AppGlobal.g_oFuncUser.get().getUserName(1);
						sSessionId = AppGlobal.getInterfaceSession(m_oMembershipInterface.getInterfaceId());
					}
				}
			}
			
			// Get Check Level Extra Info
			if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASCENTIS_CRM)){
				sCardNumber = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_CARD_NO); 
				sPostingKey = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_POSTING_KEY);
			}
			if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_EPOS)){
				if(!bPointPosting)
				sMemberNumber = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER);
				sPostingKey = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_POSTING_KEY);
			}
			if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN)){
				sMemberNumber = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER);
				sCardNumber = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_CARD_NO);
			}
			if (m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_YAZUO_CRM)) {
				sCardNumber = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_CARD_NO);
				sTraceId = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_TRACE_ID);
			}
			if (m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_CONCEPT)){
				sMemberNumber = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_GUEST_NO);
				sPaymentType = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_PAYTYPE);
			}
			if (m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_VIENNA_CRM))
				sTraceId = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_TRACE_ID); 
			if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_SMART_INTEGRAL)) {
				sTraceId = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_TRACE_ID);
				sMemberNumber = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER);
			}
			if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2)){
				if(oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_CHECK_INFO, 0))
					sCheckNo = oFuncCheck.getCheckExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_CHECK_INFO, 0);
				if(oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_REFERENCE, 0)) {
					sCheckDate = oFuncCheck.getCheckExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_REFERENCE, 0);
					sCheckDateTime = sCheckDate + " 00:00:00";
				}
			}
			
		}
		
		LpsSvcPostingInfo oPostingInfo = new LpsSvcPostingInfo();
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN))
			oPostingInfo.bIsFromPms = false;
		else if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN_PMS))
			oPostingInfo.bIsFromPms = true;
		else if (m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_LPS_SVC)){
			if (oFuncCheck.hasActiveCheckAttribute() && m_oMembershipInterface.getInterfaceConfig()
					.optJSONObject("svc_setting").optJSONObject("params").optJSONObject("terminal_type_classification") != null) {
				String sTerminalType = m_oMembershipInterface.getInterfaceConfig().optJSONObject("svc_setting")
						.optJSONObject("params").optJSONObject("terminal_type_classification").optString("value");
				if (sTerminalType.length() >= 15 && sTerminalType.substring(0, 15).equals("attribute_type_"))
					oPostingInfo.iAttributeId = oFuncCheck.getCheckAttribute()
							.getAttrAttoId()[Integer.parseInt(sTerminalType.substring(15)) - 1];
			}
		}
		oPostingInfo.iInterfaceId = m_oMembershipInterface.getInterfaceId();
		oPostingInfo.iOutletId = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.sMemberNumber = sMemberNumber;
		oPostingInfo.sMemberName = sMemberName;
		oPostingInfo.sCardNo = sCardNumber;
		oPostingInfo.sArAccountNumber = sArAccNumber;
		oPostingInfo.sTraceId = sTraceId;
		if(!sPassword.isEmpty())
			oPostingInfo.sPassword = this.getMd5String("UTF-8", sPassword);
		oPostingInfo.sStationId = AppGlobal.g_oFuncStation.get().getStation().getCode();
		if (!sCheckNo.isEmpty())
			oPostingInfo.sCheckNumber = sCheckNo;
		else
			oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
		oPostingInfo.iCover = oFuncCheck.getCover();
		//oPostingInfo.dPaymentAmount = oCheckPayment.getPayTotal();
		//oPostingInfo.iPaymentId = oCheckPayment.getPaymentMethodId();
		// oPostingInfo.sPaymentName = oCheckPayment.getName(1);
		oPostingInfo.sEmployeeNumber = AppGlobal.g_oFuncUser.get().getUserNumber();
		//oPostingInfo.dTips = oCheckPayment.getPayTips();
		oPostingInfo.sDate = sCurrentDate;
		oPostingInfo.sTime = sCurrentTime;
		oPostingInfo.dPreviousPaymentTotal = dPreviousPaymentTotal;
		if(bPointPosting)
			oPostingInfo.iPointPosting = 1;
		else
			oPostingInfo.iPointPosting = 0;
		oPostingInfo.sPaymentSequence = String.valueOf(iPaymentSequence);
		
		oPostingInfo.sPostingKey = sPostingKey;
		oPostingInfo.sPaymentType = sPaymentType;
		oPostingInfo.sPointUsed = sPointUsed;
		
		oPostingInfo.sMemberLastName = sMemberLastName;
		oPostingInfo.sPointRedeem = sPointRedeem;
		oPostingInfo.sAwardCode = sAwardCode;
		oPostingInfo.sFacilityCode = sFacilityCode;
		oPostingInfo.sFacilityTypeCode = sFacilityTypeCode;
		oPostingInfo.sEmployeeName = sEmployeeName;
		oPostingInfo.sPostingTimeZone = sTimeZone;
		oPostingInfo.sCheckDateTime = sCheckDateTime;
		oPostingInfo.sSessionId = sSessionId;
		oPostingInfoList.add(oPostingInfo);
		
		// no svc posting is needed
		if(oPostingInfoList.isEmpty())
			return true;

		// do voiding posting
		JSONObject oCheckInformationJSONObject = oFuncCheck.contrustSvcPostingCheckInformation(oCheckPayments, oPostingInfoList.get(0).oPreviousCheckPayments, null);
		PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
		if(oInterfaceConfig.doMembershipVoidPosting(oCheckInformationJSONObject, fromSvcPostingJSONObject(oPostingInfo)) == false) {
			bFailToPost = true;

			if(oInterfaceConfig.getLastErrorCode() != 0)
				m_sLastErrorMessage = getErrorMessage(oInterfaceConfig.getLastErrorCode());
			else
				m_sLastErrorMessage = oInterfaceConfig.getLastErrorMessage();
		}
		
		if(bFailToPost)
			return false;
		else{
			if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2)){
				//update member point balance
			
			if(oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_POINTS_BALANCE, 0)) {
					String sPointBalance = "";
					if(oInterfaceConfig.getLastSuccessResult().has("pointsBalance"))
						sPointBalance = oInterfaceConfig.getLastSuccessResult().optString("pointsBalance");
					if(!sPointBalance.isEmpty())
						oFuncCheck.updateCheckExtraInfoValue(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_POINTS_BALANCE, 0, sPointBalance);
				}
			
				JSONObject oResultJSON = oInterfaceConfig.getLastSuccessResult();
				m_oLastLpsSvcResponseInfo = new MemberInterfaceResponseInfo();
				m_oLastLpsSvcResponseInfo.sPointsBalance = oResultJSON.optString("pointsBalance");
				m_oLastLpsSvcResponseInfo.sPointsReturned = oResultJSON.optString("pointsReturned");
				m_oLastLpsSvcResponseInfo.sAwardCancellationNumber = oResultJSON.optString("postingKey");
				
			}
			return true;
		}
	}
	
	// Void SVC Coupon Redeem Item Posting
	public boolean membershipVoidSVCRedeemPosting(FuncCheck oFuncCheck, FuncCheckItem oFuncCheckItem) {
		boolean bFailToPost = false;
		
		List<LpsSvcPostingInfo> oPostingInfoList = new ArrayList<LpsSvcPostingInfo>();
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HHmmss");
		String sCurrentTime = timeFmt.print(today);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyyMMdd");
		String sCurrentDate = dateFmt.print(today);
		String sMemberNumber = "", sMemberName = "", sCardNumber = "", sTraceId = "", sArAccNumber = "", sPassword = "";
		
		sMemberNumber = oFuncCheckItem.getSVCCouponNumber();
		
		LpsSvcPostingInfo oPostingInfo = new LpsSvcPostingInfo();
		oPostingInfo.iInterfaceId = m_oMembershipInterface.getInterfaceId();
		oPostingInfo.iOutletId = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.sMemberNumber = sMemberNumber;
		oPostingInfo.sMemberName = sMemberName;
		oPostingInfo.sCardNo = sCardNumber;
		oPostingInfo.sArAccountNumber = sArAccNumber;
		oPostingInfo.sTraceId = sTraceId;
		if(!sPassword.isEmpty())
			oPostingInfo.sPassword = this.getMd5String("UTF-8", sPassword);
		oPostingInfo.sStationId = AppGlobal.g_oFuncStation.get().getStation().getCode();
		oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
		oPostingInfo.iCover = oFuncCheck.getCover();
		oPostingInfo.sEmployeeNumber = AppGlobal.g_oFuncUser.get().getUserNumber();
		oPostingInfo.sDate = sCurrentDate;
		oPostingInfo.sTime = sCurrentTime;
		oPostingInfo.dPreviousPaymentTotal = BigDecimal.ZERO;
		oPostingInfo.sPaymentSequence = "";
		
		oPostingInfoList.add(oPostingInfo);
		
		// no svc posting is needed
		if(oPostingInfoList.isEmpty())
			return true;

		// do voiding posting
		JSONObject oCheckInformationJSONObject = oFuncCheck.contrustSvcPostingCheckInformation(new ArrayList<PosCheckPayment>(), new ArrayList<PosCheckPayment>(), null);
		PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
		if(oInterfaceConfig.doMembershipVoidPosting(oCheckInformationJSONObject, fromSvcPostingJSONObject(oPostingInfo)) == false) {
			bFailToPost = true;
		}
		
		if(bFailToPost)
			return false;
		else
			return true;
	}
	
	// add deposit 
	public boolean membershipDepositEnquiry(HashMap<String, String> oEnquiryInfo) {
		if(!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN))
			return false;
		
		//basic info
		oEnquiryInfo.put("interfaceId", String.valueOf(m_oMembershipInterface.getInterfaceId()));
		oEnquiryInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oEnquiryInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oEnquiryInfo.put("stationCode", AppGlobal.g_oFuncStation.get().getStation().getCode());
		JSONObject oResultJSONObject = m_oMembershipInterface.membershipDepositEnquiry(oEnquiryInfo);
		m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
		if(oResultJSONObject == null) {
			m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
			return false;
		}
		
		if(!oResultJSONObject.has("enquiryResult")) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("result_error");
			return false;
		}
		
		if(oResultJSONObject.optBoolean("enquiryResult", false) == false) {
			if(oResultJSONObject.optInt("errorCode", 0) == 0)
				m_sLastErrorMessage = oResultJSONObject.optString("errorMessage");
			else
				m_sLastErrorMessage = getErrorMessage(oResultJSONObject.optInt("errorCode", 0));
			return false;
		}
		
		JSONObject oDepositDetailObject = oResultJSONObject.optJSONObject("depositDetail");
		m_oLastLpsSvcResponseInfo = new MemberInterfaceResponseInfo();
		m_oLastLpsSvcResponseInfo.sEventOrderNumber = oDepositDetailObject.optString("eventOrderNumber");
		m_oLastLpsSvcResponseInfo.sEventOrderDeposit = oDepositDetailObject.optString("eventOrderDeposit");
		
		return true;
	}
	
	// Add Deposit Posting
	public boolean membershipDepositPosting(HashMap<String, String> oPostingInfo) {
		if(!m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN))
			return false;
		
		//basic info
		oPostingInfo.put("interfaceId", String.valueOf(m_oMembershipInterface.getInterfaceId()));
		oPostingInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oPostingInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oPostingInfo.put("stationCode", AppGlobal.g_oFuncStation.get().getStation().getCode());
		if(!m_oMembershipInterface.membershipAddDepositPosting(oPostingInfo))
			return false;
		
		return true;
	}
	
	public boolean cardCouponListingEnquiry(HashMap<String, String> oEnquiryInfo) {
		m_bConnectError = false;
		
		//Generate the card coupon listing enquiry information
		oEnquiryInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oEnquiryInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oEnquiryInfo.put("stationCode", AppGlobal.g_oFuncStation.get().getStation().getCode());
		oEnquiryInfo.put("employeeNum", AppGlobal.g_oFuncUser.get().getUserNumber());
		oEnquiryInfo.put("cardNumber", oEnquiryInfo.get("cardNumber"));
		oEnquiryInfo.put("timeZone", String.valueOf(AppGlobal.g_oFuncOutlet.get().getShop().getTimezone()));
		JSONObject oResultJSONObject = m_oMembershipInterface.doMembershipCardCouponListEnquiry(oEnquiryInfo);
		m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
		if(oResultJSONObject == null) {
			m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
			return false;
		}
		
		if(!oResultJSONObject.has("enquiryResult")) {
			if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_VIENNA_CRM)){
				m_sLastErrorMessage = oResultJSONObject.optString("errorMessage");
				return false;
			}
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("result_error");
			return false;
		}
		
		if(oResultJSONObject.optBoolean("enquiryResult", false) == false) {
			if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN) &&
					(oResultJSONObject.optInt("errorCode") == 6 || oResultJSONObject.optInt("errorCode") == 8))
				m_bConnectError = true;
			if(oResultJSONObject.optInt("errorCode", 0) == 0)
				m_sLastErrorMessage = oResultJSONObject.optString("errorMessage");
			else
				m_sLastErrorMessage = getErrorMessage(oResultJSONObject.optInt("errorCode", 0));
			return false;
		}
		
		JSONObject oCouponListObject = oResultJSONObject.optJSONObject("couponDetail");
		if(oCouponListObject == null || oCouponListObject.length() == 0) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("no_coupon_found");
			return false;
		}
		
		//voucher list
		m_oLastLpsSvcResponseInfo = new MemberInterfaceResponseInfo();
		m_oLastLpsSvcResponseInfo.sVoucherList = new ArrayList<String>();
		for(int i=0; i<oCouponListObject.optJSONArray("coupons").length(); i++) {
			JSONObject oCardJSON = oCouponListObject.optJSONArray("coupons").optJSONObject(i);
			if (m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_VIENNA_CRM)) {
				m_oLastLpsSvcResponseInfo.sVoucherList.add(oCardJSON.optString("number", " ") + ","
						+ oCardJSON.optString("type", " ") + "," + oCardJSON.optString("amount", " ") + ","
						+ oCardJSON.optString("useBeginDate", " ") + "," + oCardJSON.optString("useEndDate", " ")
						+ "," + oCardJSON.optString("status", " ") + "," + oCardJSON.optString("name", " "));
			} else {
				m_oLastLpsSvcResponseInfo.sVoucherList.add(oCardJSON.optString("vouchno", " ") + ","
						+ oCardJSON.optString("pcatcode", " ") + "," + oCardJSON.optString("voucamt", " ") + ","
						+ oCardJSON.optString("voucissda", " ") + "," + oCardJSON.optString("voucexpda", " ") + ","
						+ oCardJSON.optString("voucrmmk", " "));
			}
		}
		
		for(int i = 0; i<m_oLastLpsSvcResponseInfo.sVoucherList.size(); i++){
			String [] parts = m_oLastLpsSvcResponseInfo.sVoucherList.get(i).split(",");
			String sCouponNumber = parts[0];
			if(sCouponNumber.equals(oEnquiryInfo.get("couponNumber"))){
				m_sSpecificCoupon = m_oLastLpsSvcResponseInfo.sVoucherList.get(i);
				return true;
			}
		}
		return true;
	}
	
	public boolean couponRedeem(HashMap<String, String> oEnquiryInfo) {
		m_bConnectError = false;
		
		//Generate the card coupon listing enquiry information
		oEnquiryInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oEnquiryInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oEnquiryInfo.put("stationCode", AppGlobal.g_oFuncStation.get().getStation().getCode());
		oEnquiryInfo.put("employeeNum", AppGlobal.g_oFuncUser.get().getUserNumber());
		oEnquiryInfo.put("timezone", String.valueOf(AppGlobal.g_oFuncOutlet.get().getShop().getTimezone()));
		
		JSONObject oResultJSONObject = m_oMembershipInterface.doMembershipVoucherRedeem(oEnquiryInfo);
		m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
		if(oResultJSONObject == null) {
			m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
			return false;
		}
		
		if(!oResultJSONObject.has("postingResult")) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("result_error");
			return false;
		}
		
		if(oResultJSONObject.optBoolean("postingResult", false) == false) {
			if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_VIENNA_CRM) &&
					(oResultJSONObject.optInt("errorCode") == 6 || oResultJSONObject.optInt("errorCode") == 8))
				m_bConnectError = true;
			if(oResultJSONObject.optInt("errorCode", 0) == 0)
				m_sLastErrorMessage = oResultJSONObject.optString("errorMessage");
			else
				m_sLastErrorMessage = getErrorMessage(oResultJSONObject.optInt("errorCode", 0));
			return false;
		}
		
		return true; 
	}
	
	public JSONObject membershipExchangeRateEnquiry(HashMap<String, String> oEnquiryInfo) {
		m_bConnectError = false;
		
		//Generate the enquiry information
		oEnquiryInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oEnquiryInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oEnquiryInfo.put("stationCode", AppGlobal.g_oFuncStation.get().getStation().getCode());
		
		JSONObject oResultJSONObject = m_oMembershipInterface.membershipExchangeRateEnquiry(oEnquiryInfo);
		m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
		if(oResultJSONObject == null) {
			m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
			return null;
		}
		
		if(!oResultJSONObject.has("exchangeRate")) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("result_error");
			return null;
		}
		
		if(oResultJSONObject.optBoolean("enquiryResult", false) == false) {
			if(oResultJSONObject.optInt("errorCode", 0) == 0)
				m_sLastErrorMessage = oResultJSONObject.optString("errorMessage");
			else
				m_sLastErrorMessage = getErrorMessage(oResultJSONObject.optInt("errorCode", 0));
			return null;
		}
		
		return oResultJSONObject; 
	}
	
	public BigDecimal getExchangeRate(boolean bIsAmountToPoints, String sTier, BigDecimal dDefaultExchangeValue) {
		// Get the exchange rate
		HashMap<String, String> oExchangeRateEnquiryInfo = new HashMap<String, String>();
		String sTempExchangeValue = "";
		if(bIsAmountToPoints)
			oExchangeRateEnquiryInfo.put("amount", StringLib.BigDecimalToStringWithoutZeroDecimal(dDefaultExchangeValue));
		else
			oExchangeRateEnquiryInfo.put("points", StringLib.BigDecimalToStringWithoutZeroDecimal(dDefaultExchangeValue));
		
		if (sTier != null && !sTier.isEmpty())
			oExchangeRateEnquiryInfo.put("tierLevel", sTier);
		else
			oExchangeRateEnquiryInfo.put("tierLevel", m_oLastLpsSvcResponseInfo.sTier);
		
		JSONObject oTempExchangeRate = membershipExchangeRateEnquiry(oExchangeRateEnquiryInfo);
		if(oTempExchangeRate != null) {
			if(bIsAmountToPoints)
				sTempExchangeValue = oTempExchangeRate.optJSONObject("exchangeRate").optString("points", "0");
			else
				sTempExchangeValue = oTempExchangeRate.optJSONObject("exchangeRate").optString("amount", "0");
			
			if(dDefaultExchangeValue.compareTo(BigDecimal.ZERO) == 0)
				return BigDecimal.ONE;
			else
				return (new BigDecimal(sTempExchangeValue)).divide(dDefaultExchangeValue, 20, RoundingMode.UP);
		}
		
		return BigDecimal.ONE;
	}
	
	public boolean membershipAffiliation(HashMap<String, String> oEnquiryInfo) {
		oEnquiryInfo.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oEnquiryInfo.put("stationCode", AppGlobal.g_oFuncStation.get().getStation().getCode());
		oEnquiryInfo.put("employeeNum", AppGlobal.g_oFuncUser.get().getUserNumber());
		DateTime oCurrentDate = AppGlobal.getCurrentTime(false);
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm:ss");
		String sTime = timeFmt.print(oCurrentDate);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String sDate = dateFmt.print(oCurrentDate);
		oEnquiryInfo.put("date", sDate);
		oEnquiryInfo.put("time", sTime);
		oEnquiryInfo.put("timeZone", String.valueOf(AppGlobal.g_oFuncOutlet.get().getShop().getTimezone()));
		
		if (m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL_V2)) {
			oEnquiryInfo.put("sessionId", AppGlobal.getInterfaceSession(m_oMembershipInterface.getInterfaceId()));
			JSONObject oInterfaceConfig = null;
			String sFacilityCode = "", sFacilityTypeCode = "";
			// get facility_code and facility type_code from interface
			oInterfaceConfig = m_oMembershipInterface.getConfigValue();
			if (oInterfaceConfig != null && (oInterfaceConfig.has("general_setup")
					&& oInterfaceConfig.optJSONObject("general_setup").has("params")
					&& oInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").has("facility_code")
					&& oInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").has("facility_type_code"))) {
				sFacilityCode = oInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").optJSONObject("facility_code").optString("value");
				sFacilityTypeCode = oInterfaceConfig.optJSONObject("general_setup").optJSONObject("params").optJSONObject("facility_type_code").optString("value");
			}
			
			if (sFacilityCode.isEmpty()) {
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("missing_setup");
				return false;
			}
			oEnquiryInfo.put("facilityCode", sFacilityCode);
			oEnquiryInfo.put("facilityType", sFacilityTypeCode);
			oEnquiryInfo.put("type", "E");
		}
		
		JSONObject oResultJSONObject = new JSONObject();
		oResultJSONObject = m_oMembershipInterface.membershipAffiliation(oEnquiryInfo);
		if (oResultJSONObject == null || !oResultJSONObject.has("enquiryResult")) {
			m_sLastErrorMessage = m_oMembershipInterface.getLastErrorMessage();
			return false;
		}
		
		if (oResultJSONObject.optBoolean("enquiryResult", false) == false) {
			m_sLastErrorMessage = oResultJSONObject.optString("errorMessage");
			if (oResultJSONObject.optInt("errorCode") > 0 && oResultJSONObject.optInt("errorCode") < 25)
				m_sLastErrorMessage = getErrorMessage(oResultJSONObject.optInt("errorCode", 0));
			else if (oResultJSONObject.optInt("errorCode") > 24)
				m_sLastErrorMessage = oResultJSONObject.optString("errorMessage", "");
			return false;
		}
		return true;
	}
	
	public HashMap<String, String> getLastMemberInfo() {
		HashMap<String, String> oMemberInfo = new HashMap<String, String>();
		if(m_oLastLpsSvcResponseInfo == null)
			return null;
		
		oMemberInfo.put("accountBalance", m_oLastLpsSvcResponseInfo.sAccountBalance);
		oMemberInfo.put("activityCode", m_oLastLpsSvcResponseInfo.sActivityCode);
		oMemberInfo.put("address", m_oLastLpsSvcResponseInfo.sAddress);
		oMemberInfo.put("allowCharge", m_oLastLpsSvcResponseInfo.sAllowCharge);
		oMemberInfo.put("amount", m_oLastLpsSvcResponseInfo.sAmount);
		oMemberInfo.put("arAccountNumber", m_oLastLpsSvcResponseInfo.sArAccountNumber);
		oMemberInfo.put("bevDisc", m_oLastLpsSvcResponseInfo.sBevDisc);
		oMemberInfo.put("birthday", m_oLastLpsSvcResponseInfo.sBirthday);
		oMemberInfo.put("cardAge", m_oLastLpsSvcResponseInfo.sCardAge);
		oMemberInfo.put("cardAlias", m_oLastLpsSvcResponseInfo.sCardAlias);
		oMemberInfo.put("cardLevelName", m_oLastLpsSvcResponseInfo.sCardLevelName);
		oMemberInfo.put("cardNumber", m_oLastLpsSvcResponseInfo.sCardNumber);
		oMemberInfo.put("cardStatus", m_oLastLpsSvcResponseInfo.sCardStatus);
		oMemberInfo.put("cardTypeName", m_oLastLpsSvcResponseInfo.sCardTypeName);
		oMemberInfo.put("couponAmount", m_oLastLpsSvcResponseInfo.sCouponAmount);
		oMemberInfo.put("couponCost", m_oLastLpsSvcResponseInfo.sCouponCost);
		oMemberInfo.put("couponItemCode", m_oLastLpsSvcResponseInfo.sCouponItemCode);
		oMemberInfo.put("creditAllowance", m_oLastLpsSvcResponseInfo.sCreditAllowance);
		oMemberInfo.put("creditLimit", m_oLastLpsSvcResponseInfo.sCreditLimit);
		oMemberInfo.put("creditUsage", m_oLastLpsSvcResponseInfo.sCreditUsage);
		oMemberInfo.put("displayMessage", m_oLastLpsSvcResponseInfo.sDisplayMessage);
		oMemberInfo.put("dollarToPointsRation", m_oLastLpsSvcResponseInfo.sDollarToPointsRatio);
		oMemberInfo.put("englishName", m_oLastLpsSvcResponseInfo.sEnglishName);
		oMemberInfo.put("eventOrderNumber", m_oLastLpsSvcResponseInfo.sEventOrderNumber);
		oMemberInfo.put("eventOrderDeposit", m_oLastLpsSvcResponseInfo.sEventOrderDeposit);
		oMemberInfo.put("expiryDate", m_oLastLpsSvcResponseInfo.sExpiryDate);
		oMemberInfo.put("foodDisc", m_oLastLpsSvcResponseInfo.sFoodDisc);
		oMemberInfo.put("gender", m_oLastLpsSvcResponseInfo.sGender);
		oMemberInfo.put("hotelCode", m_oLastLpsSvcResponseInfo.sHotelCode);
		oMemberInfo.put("localBalance", m_oLastLpsSvcResponseInfo.sLocalBalance);
		oMemberInfo.put("memberEthnicity", m_oLastLpsSvcResponseInfo.sMemberEthnicity);
		oMemberInfo.put("memberLastName", m_oLastLpsSvcResponseInfo.sMemberLastName);
		oMemberInfo.put("memberName", m_oLastLpsSvcResponseInfo.sMemberName);
		oMemberInfo.put("memberNumber", m_oLastLpsSvcResponseInfo.sMemberNumber);
		oMemberInfo.put("memberStatus", m_oLastLpsSvcResponseInfo.sMemberStatus);
		oMemberInfo.put("memberType", m_oLastLpsSvcResponseInfo.sMemberType);
		oMemberInfo.put("miscDisc", m_oLastLpsSvcResponseInfo.sMiscDisc);
		oMemberInfo.put("mobileNumber", m_oLastLpsSvcResponseInfo.sMobileNumber);
		oMemberInfo.put("overdarftSpend", m_oLastLpsSvcResponseInfo.sOverdarftSpend);
		oMemberInfo.put("photoFileName", m_oLastLpsSvcResponseInfo.sPhotoFileName);
		oMemberInfo.put("pointEarned", m_oLastLpsSvcResponseInfo.sPointEarned);
		oMemberInfo.put("points", m_oLastLpsSvcResponseInfo.sPointsBalance);
		oMemberInfo.put("pointsBalance", m_oLastLpsSvcResponseInfo.sPointsBalance);
		oMemberInfo.put("postingKey", m_oLastLpsSvcResponseInfo.sPostingKey);
		oMemberInfo.put("responseCode", m_oLastLpsSvcResponseInfo.sResponseCode);
		oMemberInfo.put("salutation", m_oLastLpsSvcResponseInfo.sSalutation);
		oMemberInfo.put("signatureFileName", m_oLastLpsSvcResponseInfo.sSignatureFileName);
		oMemberInfo.put("terinalId", m_oLastLpsSvcResponseInfo.sTerminalId);
		oMemberInfo.put("tipAmount", m_oLastLpsSvcResponseInfo.sTipAmount);
		oMemberInfo.put("totalPointBalance", m_oLastLpsSvcResponseInfo.sTotalPointsBalance);
		oMemberInfo.put("traceId", m_oLastLpsSvcResponseInfo.sTraceId);
		oMemberInfo.put("eventOrderNumber", m_oLastLpsSvcResponseInfo.sEventOrderNumber);
		oMemberInfo.put("eventOrderDeposit", m_oLastLpsSvcResponseInfo.sEventOrderDeposit);
		oMemberInfo.put("reference", m_oLastLpsSvcResponseInfo.sReference);
		oMemberInfo.put("pointsType", m_oLastLpsSvcResponseInfo.sPointsType);
		oMemberInfo.put("pointsRate", m_oLastLpsSvcResponseInfo.sPointsRate);
		oMemberInfo.put("pointsAmount", m_oLastLpsSvcResponseInfo.sPointsAmount);
		oMemberInfo.put("firstName", m_oLastLpsSvcResponseInfo.sMemberFirstName);
		oMemberInfo.put("lastName", m_oLastLpsSvcResponseInfo.sMemberLastName);
		oMemberInfo.put("tier", m_oLastLpsSvcResponseInfo.sTier);
		oMemberInfo.put("enrolmentCode", m_oLastLpsSvcResponseInfo.sEnrolmentCode);
		oMemberInfo.put("pointsExpiring", m_oLastLpsSvcResponseInfo.sPoints);
		oMemberInfo.put("pointsLocal", m_oLastLpsSvcResponseInfo.sLocalBalance);
		oMemberInfo.put("pointsExpiringLocal", m_oLastLpsSvcResponseInfo.sTotalPointsBalance);
		oMemberInfo.put("preference", m_oLastLpsSvcResponseInfo.sRemark);
		oMemberInfo.put("status", m_oLastLpsSvcResponseInfo.sMemberStatus);
		oMemberInfo.put("receiptMessage", m_oLastLpsSvcResponseInfo.sReceiptMessage);
		oMemberInfo.put("screenMessage", m_oLastLpsSvcResponseInfo.sCashierMessage);
		oMemberInfo.put("bonusExpiryDate", m_oLastLpsSvcResponseInfo.sBonusExpiryDate);
		oMemberInfo.put("debCardBalance", m_oLastLpsSvcResponseInfo.sDebCardBalance);
		oMemberInfo.put("city", m_oLastLpsSvcResponseInfo.sCity);
		oMemberInfo.put("stateRegion", m_oLastLpsSvcResponseInfo.sStateRegion);
		oMemberInfo.put("postalCode", m_oLastLpsSvcResponseInfo.sPostalCode);
		oMemberInfo.put("country", m_oLastLpsSvcResponseInfo.sCountry);
		oMemberInfo.put("phone", m_oLastLpsSvcResponseInfo.sMobileNumber);
		oMemberInfo.put("tierExpiryDate", m_oLastLpsSvcResponseInfo.sExpiryDate);
		oMemberInfo.put("promotions", m_oLastLpsSvcResponseInfo.sPromotions);
		
		if (m_oLastLpsSvcResponseInfo.oMemberInfoList != null) {
			for(Map.Entry<String, String> entry : m_oLastLpsSvcResponseInfo.oMemberInfoList.entrySet())
				oMemberInfo.put(entry.getKey(), entry.getValue());
		}
		
		return oMemberInfo;
	}
	
	public List<MemberInterfaceResponseInfo> getMemberList(){
		if(m_oResponseMemberList == null)
			return null;
		else 
			return m_oResponseMemberList;
	}
	
	public String getMd5String(String sCharSet, String sStr) {
		MessageDigest oMessageDigest;
		String sEncryptedStr = "";
		String sEncryptionType = "";
		
		if(sStr == null || sStr.isEmpty())
			return sEncryptedStr;
		
		if(m_oMembershipInterface.getInterfaceConfig() != null && m_oMembershipInterface.getInterfaceConfig().optJSONObject("svc_setting").optJSONObject("params").has("svc_encryption"))
			if(m_oMembershipInterface.getInterfaceConfig().has("svc_setting") && m_oMembershipInterface.getInterfaceConfig().optJSONObject("svc_setting").optJSONObject("params").has("svc_encryption"))
				sEncryptionType = m_oMembershipInterface.getInterfaceConfig().optJSONObject("svc_setting").optJSONObject("params").optJSONObject("svc_encryption").optString("value", "");
		
		try {
			oMessageDigest = MessageDigest.getInstance("MD5");
			oMessageDigest.reset();
			if(sEncryptionType.equals("triple_md5")) {
				String reverse = new StringBuffer(sStr).reverse().toString();
				oMessageDigest.update(reverse.getBytes(sCharSet));
				byte[] digest = oMessageDigest.digest();
				String sFirstStepEncryptedStr = String.format("%032x", new BigInteger(1, digest));
				sFirstStepEncryptedStr = sFirstStepEncryptedStr.toUpperCase();
				String sSecondStepEncryptedStr = sFirstStepEncryptedStr.substring(0, 3) + sFirstStepEncryptedStr.substring(21, 27) + sFirstStepEncryptedStr.substring(1, 7) + sFirstStepEncryptedStr.substring(19, 22) + sFirstStepEncryptedStr.substring(13, 15);
				oMessageDigest.update(sSecondStepEncryptedStr.getBytes(sCharSet));
				digest = oMessageDigest.digest();
				String sThirdStepEncryptedStr = String.format("%032x", new BigInteger(1, digest));
				sThirdStepEncryptedStr = sThirdStepEncryptedStr.toUpperCase();
				String sForthStepEncryptedStr = sThirdStepEncryptedStr.substring(21, 27) + sThirdStepEncryptedStr.substring(0, 3) + sThirdStepEncryptedStr.substring(19, 22) + sThirdStepEncryptedStr.substring(1, 7) + sThirdStepEncryptedStr.substring(13, 15);
				oMessageDigest.update(sForthStepEncryptedStr.getBytes(sCharSet));
				digest = oMessageDigest.digest();
				String sFifthStepEncryptedStr = String.format("%032x", new BigInteger(1, digest));
				sFifthStepEncryptedStr = sFifthStepEncryptedStr.toUpperCase();
				sEncryptedStr = sFifthStepEncryptedStr.substring(14, 16) + sFifthStepEncryptedStr.substring(2, 4) + sFifthStepEncryptedStr.substring(6, 8)  + sFifthStepEncryptedStr.substring(23, 25);
			}else {
				oMessageDigest.update(sStr.getBytes(sCharSet));
				byte[] digest = oMessageDigest.digest();
				
				BASE64Encoder encoder = new BASE64Encoder();
				sEncryptedStr = encoder.encode(digest);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sEncryptedStr;
	}
	
	public String imageReader(String sFileName, String sSetupName){
		String sImageString = "";
		String sImageType = "";
		JSONObject oInterfaceConfig = m_oMembershipInterface.getInterfaceConfig();
		String sVendorKey = m_oMembershipInterface.getInterfaceVendorKey();
		
		if (!sVendorKey.equals(InfVendor.KEY_GENERAL) && !sVendorKey.equals(InfVendor.KEY_2700))
			return "";
		
		String sFilePath = oInterfaceConfig.optJSONObject("share_folder_setup").optJSONObject("params").optJSONObject(sSetupName).optString("value");
		//checking file path string
		if(sFilePath.isEmpty())
			return "";
		if(sFilePath.substring(sFilePath.length()-1, sFilePath.length()).equals("\\") == false)
			sFilePath = sFilePath+"\\";
		
		switch(sVendorKey) {
			case InfVendor.KEY_GENERAL:
				String[] sStringArray = sFileName.split("\\.");
				if(sStringArray.length == 2)
					sImageType = sStringArray[1];
				else 
					return "";
				break;
			case InfVendor.KEY_2700:
				sImageType = oInterfaceConfig.optJSONObject("share_folder_setup").optJSONObject("params").optJSONObject("image_format").optString("value");
				sFileName = String.format("%s.%s", sFileName, sImageType);
				break;
		}
		
		File file = new File(sFilePath + sFileName);
		try {
			// Reading a Image file from file system
			FileInputStream imageInFile = new FileInputStream(file);
			byte imageData[] = new byte[(int) file.length()];
			imageInFile.read(imageData);
			imageInFile.close();
			
			//Converting Image byte array into Base64 String
			BASE64Encoder encoder = new BASE64Encoder();
			String imageDataString = encoder.encode(imageData);
			sImageString = "data:image/"+sImageType+";base64,"+imageDataString;
		} catch (FileNotFoundException e) {
			//Add log to action log list
			AppGlobal.stack2Log(e);
			return "";
		} catch (IOException ioe) {
			AppGlobal.stack2Log(ioe);
			return "";
		}
		return sImageString;
	}
	
	public String getLastErrorMessage() {
		return this.m_sLastErrorMessage;
	}
	
	public boolean isConnectionError() {
		return this.m_bConnectError;
	}
	
	public String getSpecificCoupon(){
		return m_sSpecificCoupon;
	}
	
	public boolean isOfflinePosting() {
		boolean isOfflinePosting = false;
		
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_GENERAL)) {
			JSONObject oInterfaceConfig = m_oMembershipInterface.getInterfaceConfig();
			if(oInterfaceConfig.optJSONObject("posting_setup").optJSONObject("params").optJSONObject("mode").optString("value", "").equals("offline_periodically"))
				isOfflinePosting = true;
		}
		
		return isOfflinePosting;
	}
	
	public String membershipGetSession(int iInterfaceId){
		return m_oMembershipInterface.membershipGetSession(iInterfaceId);
	}
	
	public String getErrorMessage(int iErrorCode) {
		String sErrorMessage = "";

		switch (iErrorCode) {
		case 1:
			sErrorMessage = AppGlobal.g_oLang.get()._("no_such_interface");
			break;
		case 2:
			sErrorMessage = AppGlobal.g_oLang.get()._("missing_interface_setup");
			break;
		case 3:
			sErrorMessage = AppGlobal.g_oLang.get()._("missing_setup");
			break;
		case 4:
			sErrorMessage = AppGlobal.g_oLang.get()._("interface_shell_has_not_been_built");
			break;
		case 5:
			sErrorMessage = AppGlobal.g_oLang.get()._("shell_is_not_alive");
			break;
		case 6:
			sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_build_connection");
			break;
		case 7:
			sErrorMessage = AppGlobal.g_oLang.get()._("incorrect_posting_information");
			break;
		case 8:
			sErrorMessage = AppGlobal.g_oLang.get()._("no_response");
			break;
		case 9:
			sErrorMessage = AppGlobal.g_oLang.get()._("empty_response");
			break;
		case 10:
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_response");
			break;
		case 11:
			sErrorMessage = AppGlobal.g_oLang.get()._("missing_interface_module");
			break;
		case 12:
			sErrorMessage = AppGlobal.g_oLang.get()._("zero_itemizer_setup");
			break;
		case 13:
			sErrorMessage = AppGlobal.g_oLang.get()._("no_itemizer_configuration");
			break;
		case 14:
			sErrorMessage = AppGlobal.g_oLang.get()._("missing_itemizer_setup");
			break;
		case 15:
			sErrorMessage = AppGlobal.g_oLang.get()._("pms_port_is_locked_by_other_station");
			break;
		case 16:
			sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_lock_pms_port");
			break;
		case 17:
			sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_send_request_packet");
			break;
		case 18:
			sErrorMessage = AppGlobal.g_oLang.get()._("enquiry_is_rejected");
			break;
		case 19:
			sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_receive_response_packet");
			break;
		case 20:
			sErrorMessage = AppGlobal.g_oLang.get()._("incorrect_response_message_type");
			break;
		case 21:
			sErrorMessage = AppGlobal.g_oLang.get()._("posting_is_rejected");
			break;
		case 22:
			sErrorMessage = AppGlobal.g_oLang.get()._("incorrect_posting_packet");
			break;
		case 23:
			sErrorMessage = AppGlobal.g_oLang.get()._("incorrect_receiving_packet_structure");
			break;
		case 24:
			sErrorMessage = AppGlobal.g_oLang.get()._("timeout_to_wait_receiving_packet");
			break;
		case 25:
			sErrorMessage = AppGlobal.g_oLang.get()._("bad_request");
			break;
		case 26:
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_user_data");
			break;
		case 27:
			sErrorMessage = AppGlobal.g_oLang.get()._("internal_server_error");
			break;
		case 28:
			sErrorMessage = AppGlobal.g_oLang.get()._("session_expired");
			break;
		default:
		case 0:
			sErrorMessage = null;
			break;
		}
		
		return sErrorMessage;
	}
	
	public String getStatus(int iStatus){
		String sStatus = "";
		
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_CONCEPT)){
			switch(iStatus){
				case 0:
					sStatus = AppGlobal.g_oLang.get()._("blocked");
					break;
				case 1:
					sStatus = AppGlobal.g_oLang.get()._("active");
					break;
				case 2:
					sStatus = AppGlobal.g_oLang.get()._("cancelled");
					break;
				case 3:
					sStatus = AppGlobal.g_oLang.get()._("disable");
					break;
				case 4:
					sStatus = AppGlobal.g_oLang.get()._("end_of_membership");
					break;
			}
		}
		return sStatus;
	}
	
	public boolean processPrintMembershipActionSlip(JSONObject oInfoJSONObject, String sKey) {
		// print slip
		JSONObject oHeaderJSONObject = new JSONObject();
		try {
			//form the header
			oHeaderJSONObject.put("header", "Membership");
			
			//form the information of slip
			oInfoJSONObject.put("stationId", AppGlobal.g_oFuncStation.get().getStationId());
			oInfoJSONObject.put("userId", AppGlobal.g_oFuncUser.get().getUserId());
			oInfoJSONObject.put("userName", AppGlobal.g_oFuncUser.get().getUserName(AppGlobal.g_oCurrentLangIndex.get()));
			oInfoJSONObject.put("table", "");
			oInfoJSONObject.put("outletId", AppGlobal.g_oFuncOutlet.get().getOutletId());
			// Print rush order special slip
			PosCheck oPosCheck = new PosCheck();
			return oPosCheck.printSpecialSlip(sKey, oHeaderJSONObject, oInfoJSONObject, AppGlobal.g_oCurrentLangIndex.get(), 0);
			
		}catch(JSONException jsone) {
			AppGlobal.stack2Log(jsone);
			return false;
		}
	}
	
	public boolean printAwardListActionSlip(JSONObject oInfoJSONObject) {
		// print slip
		JSONObject oHeaderJSONObject = new JSONObject();
		try {
			// form the header
			oHeaderJSONObject.put("header", "awardlist");

			// form the information of slip
			oInfoJSONObject.put("stationId", AppGlobal.g_oFuncStation.get().getStationId());
			oInfoJSONObject.put("userId", AppGlobal.g_oFuncUser.get().getUserId());
			oInfoJSONObject.put("userName", AppGlobal.g_oFuncUser.get().getUserName(AppGlobal.g_oCurrentLangIndex.get()));
			oInfoJSONObject.put("table", "");
			oInfoJSONObject.put("outletId", AppGlobal.g_oFuncOutlet.get().getOutletId());

			PosCheck oPosCheck = new PosCheck();
			return oPosCheck.printSpecialSlip(PosActionPrintQueue.KEY_AWARD_LIST, oHeaderJSONObject, oInfoJSONObject,
					AppGlobal.g_oCurrentLangIndex.get(), 0);
		} catch (JSONException jsone) {
			AppGlobal.stack2Log(jsone);
			return false;
		}
	}
	
	public void readDeviceManagerSetup() {
		List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		JSONObject oInterfaceConfig = m_oMembershipInterface.getInterfaceConfig();
		String sDeviceManagerInterfaceCode = "";
		
		try {
			if(oInterfaceConfig.has("general_setup") && oInterfaceConfig.getJSONObject("general_setup").getJSONObject("params").has("device_manager_code"))
				sDeviceManagerInterfaceCode = oInterfaceConfig.getJSONObject("general_setup").getJSONObject("params").getJSONObject("device_manager_code").getString("value");
		} catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
		
		if(sDeviceManagerInterfaceCode.isEmpty())
			return;
		
		// Get the configure from interface module
		oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PERIPHERAL_DEVICE);
		for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList) {
			if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_DEVICE_MANAGER) && oPosInterfaceConfig.getInterfaceCode().equals(sDeviceManagerInterfaceCode)) {
				try {
					JSONObject oInterfaceSetup = oPosInterfaceConfig.getInterfaceConfig();
					
					m_iDeviceManagerInterfaceId = oPosInterfaceConfig.getInterfaceId();
					
					// Support flag
					if (oInterfaceSetup.has("member_card_setup") && oInterfaceSetup.getJSONObject("member_card_setup").getJSONObject("params").getJSONObject("support").getInt("value") == 1)
						m_bSupportMemberCard = true;
					else
						break;
					
					// Model Type
					m_sModelType = oInterfaceSetup.getJSONObject("member_card_setup").getJSONObject("params").getJSONObject("model").getString("value");
					
					// Wait timeout
					m_iTimeout = Integer.parseInt(oInterfaceSetup.getJSONObject("member_card_setup").getJSONObject("params").getJSONObject("process_timeout").getString("value"));
				} catch (Exception e) {
					AppGlobal.stack2Log(e);
				}
			}
		}
	}
	public int getDeviceManagerInterfaceId() {
		return m_iDeviceManagerInterfaceId;
	}
	
	public boolean isSupporMemberCard() {
		return m_bSupportMemberCard;
	}
	
	public String getModelType() {
		return m_sModelType;
	}
	
	public int getTimeout() {
		return m_iTimeout;
	}
	
	// Trim the string with target character 
	public String trimTargetCharacter(String sString, String sTargetCharacter) {
		String sReturn = sString;
		
		if(sReturn.length() == 0)
			return sReturn;
		for(int iIndex = 0; iIndex < sTargetCharacter.length(); iIndex ++) {
			if(sReturn.charAt(sReturn.length() - 1) == sTargetCharacter.charAt(iIndex)) {
				sReturn = sReturn.substring(0, sReturn.length() - 1);
				break;
			}
		}
		if(sReturn.length() == 0)
			return sReturn;
		
		for(int iIndex = 0; iIndex < sTargetCharacter.length(); iIndex ++) {
			if(sReturn.charAt(0) == sTargetCharacter.charAt(iIndex)) {
				sReturn = sReturn.substring(1);
				break;
			}
		}
		return sReturn;
	}
	
	private void writeInfoLog(String sFunction, String sMessage) {
		writeLog(m_iDeviceManagerInterfaceId, "Membership Card (Chinetek) - Success - Function: " + sFunction + ", Return message: " + sMessage);
	}

	private void writeErrorLog(String sFunction, String sErrorCode, String sErrorMessage) {
		writeLog(m_iDeviceManagerInterfaceId, "Membership Card (Chinetek) - Error - Function: " + sFunction + ", Error code: " + sErrorCode + ", Error message: " + sErrorMessage);
	}
	
	public void writeLog(int iInterfaceId, String sLog) {
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter monthFormat = DateTimeFormat.forPattern("MM");
		String sCurrentTime = dateFormat.print(today);
		String sCurrentMonth = monthFormat.print(today);

		StringBuilder sContent = new StringBuilder();
		String sFilename = "log/membership_interface_card_log." + sCurrentMonth;

		try{
			// Create file 
			FileWriter fstream = new FileWriter(sFilename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			sContent.append(sCurrentTime);
			sContent.append(" [s:");
			sContent.append(AppGlobal.g_oFuncStation.get().getStationId());
			sContent.append(" i:");
			sContent.append(iInterfaceId);
			sContent.append("] ");
			sContent.append(sLog);
			sContent.append("\n");
			
			out.write(sContent.toString());
			
			//Close the output stream
			out.close();
			fstream.close();
		}catch (Exception e){}//Catch exception if any
	}
	
	public String[] breakPacketDataString(String sFunction, String sResponse) {
		try {
			if (sResponse.isEmpty()) {
				this.writeErrorLog(sFunction, "", "No Response Packet Content");
				return null;
			}
			
			this.writeInfoLog(sFunction, sResponse);
			
			// Convert the byte array to Object array
			String[] oReturnArgs = sResponse.split("\035");
			if (oReturnArgs.length <= 1 || oReturnArgs.length < 30) {
				this.writeErrorLog(sFunction, "", "Incorrect Packet Size: "+oReturnArgs.length);
				return null;
			}
			
			m_oLastLpsSvcResponseInfo = new MemberInterfaceResponseInfo();
			m_oLastLpsSvcResponseInfo.sPointsBalance = oReturnArgs[1];
			m_oLastLpsSvcResponseInfo.sEnglishName = oReturnArgs[29] + oReturnArgs[5];		//English last name + first name
			m_oLastLpsSvcResponseInfo.sMemberFirstName = oReturnArgs[6];	//Chinese first name
			m_oLastLpsSvcResponseInfo.sExpiryDate = oReturnArgs[10];
			m_oLastLpsSvcResponseInfo.sMiscDisc = oReturnArgs[13];
			m_oLastLpsSvcResponseInfo.sMobileNumber = oReturnArgs[8];
			m_oLastLpsSvcResponseInfo.sBonusExpiryDate = oReturnArgs[14];
			m_oLastLpsSvcResponseInfo.sMemberNumber = oReturnArgs[17];
			m_oLastLpsSvcResponseInfo.sDisplayMessage = oReturnArgs[18];
			m_oLastLpsSvcResponseInfo.sReceiptMessage = oReturnArgs[19];
			m_oLastLpsSvcResponseInfo.sCashierMessage  = oReturnArgs[20];
			m_oLastLpsSvcResponseInfo.sCardTypeName = oReturnArgs[25];
			m_oLastLpsSvcResponseInfo.sMemberLastName = oReturnArgs[30];					//Chinese last name
			
			//Gift List
			String sUnescapeResponse = StringEscapeUtils.unescapeJava(oReturnArgs[33]);
			JSONObject oResponseJSONObject = new JSONObject(sUnescapeResponse);
//			JSONArray oGiftJSONArray = oResponseJSONObject.optJSONArray("gifts");
			
			m_oLastLpsSvcResponseInfo.oCouponList = new ArrayList<MemberInterfaceVoucherListInfo>();
			m_oLastLpsSvcResponseInfo.oGiftList = new ArrayList<MemberInterfaceVoucherListInfo>();
			
			for(int j = 0; j < oResponseJSONObject.optJSONArray("gifts").length(); j++) {
				JSONObject oGiftJSON = oResponseJSONObject.optJSONArray("gifts").optJSONObject(j);
				MemberInterfaceVoucherListInfo oVoucherInfo = new MemberInterfaceVoucherListInfo(oGiftJSON, false);
				m_oLastLpsSvcResponseInfo.oGiftList.add(oVoucherInfo);
			}
			
			//Coupon List
			sUnescapeResponse = StringEscapeUtils.unescapeJava(oReturnArgs[34]);
			oResponseJSONObject = new JSONObject(sUnescapeResponse);
			//JSONArray oCouponJSONArray = oResponseJSONObject.optJSONArray("coupons");
			
			for(int j = 0; j < oResponseJSONObject.optJSONArray("coupons").length(); j++) {
				JSONObject oCouponJSON = oResponseJSONObject.optJSONArray("coupons").optJSONObject(j);
				MemberInterfaceVoucherListInfo oVoucherInfo = new MemberInterfaceVoucherListInfo(oCouponJSON, true);
				
				m_oLastLpsSvcResponseInfo.oCouponList.add(oVoucherInfo);
			}
			
			return oReturnArgs;
		} catch(Exception e) {
			AppGlobal.stack2Log(e);
			return null;
		}
	}
	
	//membership point refund to void earned point for general_v2 membership
	public boolean membershipPointRefund(FuncCheck oFuncCheck, String sCheckNumber, String sCheckDate, List<PosCheckPayment> oCheckPaymentList) {
		LpsSvcPostingInfo oPostingInfo = new LpsSvcPostingInfo();
		oPostingInfo.iInterfaceId = m_oMembershipInterface.getInterfaceId();
		oPostingInfo.iOutletId = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.sStationId = AppGlobal.g_oFuncStation.get().getStation().getCode();
		oPostingInfo.sSessionId = AppGlobal.getInterfaceSession(m_oMembershipInterface.getInterfaceId());
		
		DateTime today = AppGlobal.getCurrentTime(false);
		DateTimeFormatter timeFmt = DateTimeFormat.forPattern("HH:mm:ss");
		String sCurrentTime = timeFmt.print(today);
		DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String sCurrentDate = dateFmt.print(today);
		
		String sFacilityCode = "", sFacilityTypeCode = "", sMemberNumber = "", sMemberLastName = "";
		
		sMemberNumber = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER);
		sMemberLastName = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_LAST_NAME);
		
		PosInterfaceConfig oPosInterfaceConfig = AppGlobal.getPosInterfaceConfigById(m_oMembershipInterface.getInterfaceId());
		JSONObject oParam = oPosInterfaceConfig.getConfigValue().optJSONObject("general_setup").optJSONObject("params");
		sFacilityCode = oParam.optJSONObject("facility_code").optString("value");
		sFacilityTypeCode = oParam.optJSONObject("facility_type_code").optString("value");
		
		oPostingInfo.sEmployeeNumber = AppGlobal.g_oFuncUser.get().getUserNumber();
		oPostingInfo.sEmployeeName = AppGlobal.g_oFuncUser.get().getUserName(1);
		oPostingInfo.sMemberNumber = sMemberNumber;
		oPostingInfo.sMemberLastName = sMemberLastName;
		oPostingInfo.sFacilityCode = sFacilityCode;
		oPostingInfo.sFacilityTypeCode = sFacilityTypeCode;
		
		oPostingInfo.sDate = sCurrentDate;
		oPostingInfo.sTime = sCurrentTime;
		oPostingInfo.sPostingTimeZone = String.valueOf(AppGlobal.g_oFuncOutlet.get().getShop().getTimezone());
		
		if(sCheckNumber != null && !sCheckNumber.isEmpty())
			oPostingInfo.sCheckNumber = sCheckNumber;
		else
			oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
		
		DateTimeFormatter dateTimeFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		if(sCheckDate != null && !sCheckDate.isEmpty())
			oPostingInfo.sCheckDateTime = sCheckDate+" 00:00:00";
		else
			oPostingInfo.sCheckDateTime = dateTimeFmt.print(oFuncCheck.getOpenLocTime());
		
		//do posting
		JSONObject oCheckInformationJSONObject = oFuncCheck.contrustSvcPostingCheckInformation(oCheckPaymentList, oPostingInfo.oPreviousCheckPayments, oFuncCheck.getCheckGratuityList());
		PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
		boolean bResult = oInterfaceConfig.doMembershipPointRefund(oCheckInformationJSONObject, fromSvcPostingJSONObject(oPostingInfo));
		
		if(!bResult) {
			if(oInterfaceConfig.getLastErrorType() != null && oInterfaceConfig.getLastErrorType().equals(FuncPMS.ERROR_TYPE_PMS))
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("svc_return_error") + ":[" + oInterfaceConfig.getLastErrorMessage() + "]";
			else if(oInterfaceConfig.getLastErrorCode() != 0)
				m_sLastErrorMessage = getErrorMessage(oInterfaceConfig.getLastErrorCode());
			else
				m_sLastErrorMessage = oInterfaceConfig.getLastErrorMessage();
			return false;
		}
		if(sCheckNumber == null){
			// clear the earned point
			oFuncCheck.updateCheckExtraInfoValue(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_POINTS_EARN, 0, "");
			// clear the earned amount
			oFuncCheck.updateCheckExtraInfoValue(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_AMOUNT_FOR_EARN_POINT, 0, "");
			// update current member point after point refund
			if(oInterfaceConfig.getLastSuccessResult().has("pointsBalance"))
				oFuncCheck.updateCheckExtraInfoValue(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_POINTS_BALANCE, 0,
						oInterfaceConfig.getLastSuccessResult().optString("pointsBalance"));
		}
		JSONObject oResultJSON = oInterfaceConfig.getLastSuccessResult();
		m_oLastLpsSvcResponseInfo = new MemberInterfaceResponseInfo();
		m_oLastLpsSvcResponseInfo.sMemberNumber = oResultJSON.optString("memberNumber", "");
		m_oLastLpsSvcResponseInfo.sPointsBalance = oResultJSON.optString("pointsBalance", "");
		m_oLastLpsSvcResponseInfo.sPointsRefunded = oResultJSON.optString("pointsRefunded", "");
		m_oLastLpsSvcResponseInfo.sAmountEarned = oResultJSON.optString("amountEarned", "");
		
		return true;
	}
	
	public HashMap<String, String> getSetupAndValidateForMembershipDiscount(FuncCheck oFunCheck){
		HashMap<String, String> oResult = new HashMap<String, String>();
		if(m_oMembershipInterface.getInterfaceVendorKey().equals(InfVendor.KEY_ASCENTIS_CRM)){
			BigDecimal dMaxAmount = BigDecimal.ZERO, dMaxPoint = BigDecimal.ZERO;
			BigDecimal dDiscountToPointRatio = BigDecimal.ZERO;
			BigDecimal dCheckTotalBeforeSCTax = oFunCheck.getItemTotal().add(oFunCheck.getRoundAmount()).add(oFunCheck.getItemDiscountTotal());
			int iPointDecimal = 2;
			
			//get card Number and Member Number
			String sCardNo = oFunCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_CARD_NO);
			String sMemberNo = oFunCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER);
			if (sCardNo == null || sCardNo.isEmpty()) {
				oResult.put("errorMsg", AppGlobal.g_oLang.get()._("no_crm_member_attached"));
				return oResult;
			}
			
			List<PosCheckDiscount> oCheckDiscountList = oFunCheck.getCheckDiscountList();
			for (PosCheckDiscount oPosCheckDiscount:oCheckDiscountList) {
				String oDiscountInterfaceId = oPosCheckDiscount.getCheckExtraInfoValueBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_INTERFACE_ID);
				if (oDiscountInterfaceId.equals(String.valueOf(m_oMembershipInterface.getInterfaceId()))) {
					oResult.put("errorMsg", AppGlobal.g_oLang.get()._("not_allow_multiple_point_redemption_discount"));
					return oResult;
				}
			}
			
			JSONObject oInterfaceSetupPointCalculation = m_oMembershipInterface.getInterfaceConfig()
					.optJSONObject("point_calculation").optJSONObject("params");
			JSONObject oInterfaceSetupRedemption = m_oMembershipInterface.getInterfaceConfig()
					.optJSONObject("redemption_setup").optJSONObject("params");
			
			// do card enquiry
			HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();
			oEnquiryInfo.put("cardNumber", sCardNo);
			oEnquiryInfo.put("memberNumber", sMemberNo);
			
			if (!cardEnquiry(oEnquiryInfo)) {
				// show CRM error message
				oResult.put("errorMsg", getLastErrorMessage());
				return oResult;
			}
			String sTotalPointBalance = getLastMemberInfo().get("totalPointBalance");
			try {
				if (new BigDecimal(sTotalPointBalance).compareTo(BigDecimal.ZERO) == 0) {
					oResult.put("errorMsg", AppGlobal.g_oLang.get()._("total_point_balance_is_zero"));
					return oResult;
				}
			} catch (Exception e) {
				AppGlobal.stack2Log(e);
				return oResult;
			}
			
			BigDecimal dCurrentCheckBalance = oFunCheck.getCheckTotal();
			
			String sDollarToPointRatio = "";
			if (getLastMemberInfo().get("dollarToPointsRation") != null
					&& !getLastMemberInfo().get("dollarToPointsRation").isEmpty()) {
				sDollarToPointRatio = getLastMemberInfo().get("dollarToPointsRation");
			}
			
			if (sDollarToPointRatio == null || sDollarToPointRatio.isEmpty()) {
				if (oInterfaceSetupPointCalculation.optJSONObject("dollar_to_point_ratio")
						.optString("value") != null
						&& !oInterfaceSetupPointCalculation.optJSONObject("dollar_to_point_ratio")
						.optString("value").isEmpty())
					sDollarToPointRatio = oInterfaceSetupPointCalculation.optJSONObject("dollar_to_point_ratio")
							.optString("value");
			}
			if (sDollarToPointRatio == null || sDollarToPointRatio.isEmpty())
				sDollarToPointRatio = "1:1";
			
			iPointDecimal = oInterfaceSetupPointCalculation.optJSONObject("point_decimal").optInt("value",
					2);
			String sMaxRedemptionPercentageForPcard = oInterfaceSetupRedemption
					.optJSONObject("max_redemption_percentage_for_pcard").optString("value");
			if (sMaxRedemptionPercentageForPcard.isEmpty())
				sMaxRedemptionPercentageForPcard = "100";
			BigDecimal dDollarRatio = BigDecimal.ZERO, dPointRatio = BigDecimal.ZERO;
			BigDecimal	dMaxRedemptionPercentageForPcard = BigDecimal.ZERO;
			BigDecimal dDivisor = new BigDecimal("100");
			String[] sTempArray = sDollarToPointRatio.split(":");
			try {
				dDollarRatio = new BigDecimal(sTempArray[0]);
				dPointRatio = new BigDecimal(sTempArray[1]);
				dDiscountToPointRatio = dDollarRatio.divide(dPointRatio);
				if (sMaxRedemptionPercentageForPcard != null && !sMaxRedemptionPercentageForPcard.isEmpty())
					dMaxRedemptionPercentageForPcard = new BigDecimal(sMaxRedemptionPercentageForPcard);
			} catch (Exception e) {
				AppGlobal.stack2Log(e);
				oResult.put("errorMsg", AppGlobal.g_oLang.get()._("setup_format_error"));
				return oResult;
			}
			
			if (dDiscountToPointRatio.compareTo(BigDecimal.ZERO) == 0) {
				// show CRM error message
				oResult.put("errorMsg", AppGlobal.g_oLang.get()._("dollar_to_point_ration_is_zero"));
				return oResult;
			}
			dMaxAmount = dCheckTotalBeforeSCTax;
			if (!sCardNo.substring(0, 1).equals("C"))
				dMaxAmount = dCheckTotalBeforeSCTax.multiply(dMaxRedemptionPercentageForPcard.divide(dDivisor));
			if (dMaxAmount.compareTo(dCurrentCheckBalance) > 0)
				dMaxAmount = dCurrentCheckBalance;
			
			if (!sCardNo.substring(0, 1).equals("C"))
				dMaxPoint = dMaxAmount.divide(dDiscountToPointRatio).setScale(iPointDecimal, RoundingMode.DOWN);
			else
				dMaxPoint = dMaxAmount.divide(dDiscountToPointRatio).setScale(iPointDecimal, RoundingMode.UP);
			
			if (dMaxPoint.compareTo(new BigDecimal(sTotalPointBalance)) > 0)
				dMaxPoint = new BigDecimal(sTotalPointBalance).setScale(iPointDecimal, RoundingMode.DOWN);
			// information for display
			oResult.put("pointBalance", sTotalPointBalance);
			oResult.put("pointDecimal", String.valueOf(iPointDecimal));
			oResult.put("checkBalance", dCurrentCheckBalance.toPlainString());
			oResult.put("maxAmount", dMaxAmount.toPlainString());
			oResult.put("maxPoint", dMaxPoint.setScale(0, RoundingMode.HALF_UP).toPlainString());
			oResult.put("discountToPointRatio", dDiscountToPointRatio.toPlainString());
			
			if (dMaxPoint.compareTo(BigDecimal.ZERO) == 0) {
				oResult.put("errorMsg", AppGlobal.g_oLang.get()._("point_to_redempt_is_zero"));
				return oResult;
			}
		}
		return oResult;
	}
}
