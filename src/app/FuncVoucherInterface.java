package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import om.InfVendor;
import om.PosCheckExtraInfo;
import om.PosCheckPayment;
import om.PosInterfaceConfig;

public class FuncVoucherInterface {
	class VoucherPostingInfo {
		String sOutletCode;
		int iShopCode;
		String sEmployeeName;
		String sStationCode;
		int iOutletId;
		String sCheckNumber;
		String sEmployeeNumber;
		int iInterfaceId;
		String sTraceID;
		BigDecimal dUnpaidTotal;
		boolean bNegativeCheck;
		ArrayList<String> oVoucherList = new ArrayList<String>();
		ArrayList<PosCheckPayment> oPreviousCheckPayments;
	}
	
	class VoucherInterfaceResponseInfo {
		String sTraceID = "";
		String sAmount = "";
		String sType = "";
		String sPostingString = "";
	}
	static public String ERROR_CODE_GALAXY_VOUCHER_VOIDED_BEFORE = "3000";

	private PosInterfaceConfig m_oVoucherInterfaceConfig;
	public String m_sLastErrorMessage;
	public VoucherInterfaceResponseInfo m_oLastVoucherResponseInfo;

	public FuncVoucherInterface(PosInterfaceConfig oVoucherInterface) {
		m_oVoucherInterfaceConfig = oVoucherInterface;
		m_sLastErrorMessage = "";
		m_oLastVoucherResponseInfo = new VoucherInterfaceResponseInfo();
	}
	
	public HashMap<String, String> getLastVoucherInfo() {
		HashMap<String, String> oVoucherInfo = new HashMap<String, String>();
		if(m_oLastVoucherResponseInfo == null)
			return null;
		
		oVoucherInfo.put("traceID", m_oLastVoucherResponseInfo.sTraceID);
		oVoucherInfo.put("voucherAmount", m_oLastVoucherResponseInfo.sAmount);
		oVoucherInfo.put("type", m_oLastVoucherResponseInfo.sType);
		
		return oVoucherInfo;
	}
	
	public String getLastErrorMessage() {
		return this.m_sLastErrorMessage;
	}
	
	public boolean voucherEnquiry(HashMap<String, String> oEnquiryInfo) {
		/*
		if(m_oVoucherInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GALAXY)) {
			// Generate the enquiry information
			oEnquiryInfo.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		}
		*/
		JSONObject oResultJSONObject = m_oVoucherInterfaceConfig.voucherEnquiry(oEnquiryInfo);

		m_sLastErrorMessage = m_oVoucherInterfaceConfig.getLastErrorMessage();

		if (!oResultJSONObject.has("enquiryResult")) {
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("result_error");
			return false;
		}

		if (oResultJSONObject.optBoolean("enquiryResult", false) == false) {
			m_sLastErrorMessage = oResultJSONObject.optString("errorMessage", "");
			return false;
		}
		/*
		if(m_oVoucherInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GALAXY)) {
			// Get Voucher type and Amount from JSON 
			m_oLastVoucherResponseInfo.sType = oResultJSONObject.optJSONObject("response").optString("type", "");
			m_oLastVoucherResponseInfo.sAmount = oResultJSONObject.optJSONObject("response").optString("amount", "");
		}
		*/
		return true;
	}
	
	// voucher posting
	public boolean voucherPosting(HashMap<String, String> oEnquiryInfo, FuncCheck oFuncCheck, BigDecimal dUnPaidTotal) {
		if (!m_oVoucherInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GALAXY))
			return false;

		VoucherPostingInfo oPostingInfo = new VoucherPostingInfo();
		boolean bFailToPost = false;
		
		if(m_oVoucherInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GALAXY)) {
			int iLangIndex = m_oVoucherInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").optInt("language_index");
			// Add InterfaceID, OutletID, ShopCode, StationCode, EmployeeNum, EmployeeName, VoucherNum and checkNumber to Hashmap
			oPostingInfo.iInterfaceId = m_oVoucherInterfaceConfig.getInterfaceId();
			oPostingInfo.iOutletId = AppGlobal.g_oFuncOutlet.get().getOutletId();
			oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
			oPostingInfo.iShopCode = AppGlobal.g_oFuncOutlet.get().getShopId();
			oPostingInfo.sStationCode = AppGlobal.g_oFuncStation.get().getStation().getCode();
			oPostingInfo.sEmployeeNumber = AppGlobal.g_oFuncUser.get().getUserNumber();
			oPostingInfo.sEmployeeName = AppGlobal.g_oFuncUser.get().getUserName(iLangIndex);
			oPostingInfo.dUnpaidTotal = dUnPaidTotal;
			oPostingInfo.oVoucherList.add(oEnquiryInfo.get("voucherNumber"));
			/*
			PosCheckExtraInfo oPosCheckExtraInfo = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_VOUCHER_INTERFACE,
					PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER, 0);
			if (oPosCheckExtraInfo.getBy().equals(PosCheckExtraInfo.BY_PAYMENT) && oPosCheckExtraInfo.getValue() != null)
				oPostingInfo.oVoucherList.add(oPosCheckExtraInfo.getValue());
			 */
			if (!AppGlobal.g_oFuncOutlet.get().getBusinessDay().isCheckNumGeneratedByOutlet() && !oFuncCheck.isOldCheck())
				oPostingInfo.sCheckNumber = AppGlobal.g_oFuncStation.get().getNextCheckPrefixNumber(false);
			else
				oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
/*
			boolean bAllGalaxyPayment = true;
			for(PosCheckPayment oPosCheckPayment:oCheckPayments) {
				if(oPosCheckPayment.getVoucherInterfaceId() != m_oVoucherInterfaceConfig.getInterfaceId()) {
					bAllGalaxyPayment = false;
					break;
				}
			}
			
			if(bIsLastPayment && bAllGalaxyPayment) {
				ArrayList<PosCheckPayment> oPosPaymentList = new ArrayList<PosCheckPayment>();
				for(int i = 0 ; i < oCheckPayments.size() ; i++) {
					if(i != oCheckPayments.size() - 1)
						oPosPaymentList.add(oCheckPayments.get(i));
				}
				oPostingInfo.oPreviousCheckPayments = oPosPaymentList;
			}
*/
		}
		JSONObject oCheckInformationJSONObject = oFuncCheck.contrustPmsPostingCheckInformation(null);	
/*
		if(oPostingInfo.oPreviousCheckPayments != null && !oPostingInfo.oPreviousCheckPayments.isEmpty()) {
			JSONArray oCheckPaymentJSONArray = new JSONArray();
			for(int i=0; i<oPostingInfo.oPreviousCheckPayments.size(); i++) 
				oCheckPaymentJSONArray.put(oPostingInfo.oPreviousCheckPayments.get(i).constructAddSaveJSON(true));
			try {
				oCheckInformationJSONObject.put("previousPayments", oCheckPaymentJSONArray);
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
				return false;
			}
		}
		
*/
		// Pass JSON to PHP and get the response
		boolean bResult = m_oVoucherInterfaceConfig.doVoucherPosting(oCheckInformationJSONObject, formVoucherPostingJSONObject(oPostingInfo));

		if (bResult == false) {
			m_sLastErrorMessage = m_oVoucherInterfaceConfig.getLastErrorMessage();
			bFailToPost = true;
		} 
		JSONObject oResultJSONObject = m_oVoucherInterfaceConfig.getLastSuccessResult();

		if(!oResultJSONObject.has("response")) {
			m_sLastErrorMessage = oResultJSONObject.optString("errorMessage");
			return false;
		}
		
		if(m_oVoucherInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GALAXY)) {
			// Get Voucher type and Amount from JSON 
			try {
				m_oLastVoucherResponseInfo = new VoucherInterfaceResponseInfo();
				m_oLastVoucherResponseInfo.sType = oResultJSONObject.optJSONObject("response").optJSONArray("results").getJSONObject(0).optString("type", "");
				m_oLastVoucherResponseInfo.sAmount = oResultJSONObject.optJSONObject("response").optJSONArray("results").getJSONObject(0).optString("amount", "");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		JSONObject oTempResultJSONObject = null;
		try {
			if(oResultJSONObject.has("response") && oResultJSONObject.optJSONObject("response").has("results"))
				oTempResultJSONObject = new JSONObject(oResultJSONObject.optJSONObject("response").optJSONArray("results").optString(0));
		} catch (JSONException e) {
			AppGlobal.stack2Log(e);
			return false;
		}
		
		// If not success
		if(oTempResultJSONObject == null || (oTempResultJSONObject != null && !oTempResultJSONObject.optString("result").equals("Success"))) {
			if(oTempResultJSONObject != null && oTempResultJSONObject.has("message"))
				m_sLastErrorMessage = oTempResultJSONObject.optString("message");
			else
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_post_voucher") + ":" + oTempResultJSONObject.optString("voucher");
			return false;
		}
		else {
			if(m_oVoucherInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GALAXY)) 
				m_oLastVoucherResponseInfo.sTraceID = oResultJSONObject.optJSONObject("response").optString("traceId", "");
				if(oResultJSONObject.optJSONObject("response").has("postingString"))
					m_oLastVoucherResponseInfo.sPostingString = oResultJSONObject.optJSONObject("response").optString("postingString");
			}
		
		if(bFailToPost)
			return false;
		else
			return true;
	}
	
	public boolean voucherVoidPosting (PosCheckPayment oCheckPayment, HashMap<String, String> oVoucherInfo, boolean bNegativeCheck) {
		boolean bFailToPost = false;
		VoucherPostingInfo oPostingInfo = new VoucherPostingInfo();
		if(m_oVoucherInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GALAXY)) {
			int iLangIndex = m_oVoucherInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup").optJSONObject("params").optInt("language_index");
			oPostingInfo.iInterfaceId = m_oVoucherInterfaceConfig.getInterfaceId();
			oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
			oPostingInfo.iShopCode = AppGlobal.g_oFuncOutlet.get().getShopId();
			oPostingInfo.sStationCode = AppGlobal.g_oFuncStation.get().getStation().getCode();
			oPostingInfo.sEmployeeName = AppGlobal.g_oFuncUser.get().getUserName(iLangIndex);
			oPostingInfo.sEmployeeNumber = AppGlobal.g_oFuncUser.get().getUserNumber();
			oPostingInfo.bNegativeCheck = bNegativeCheck;
		
			ArrayList<String> sVoucherNumber = new ArrayList<String> ();
			if(oCheckPayment != null) {
				if(oCheckPayment.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_VOUCHER_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER, 0))
					sVoucherNumber.add(oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_VOUCHER_INTERFACE, PosCheckExtraInfo.VARIABLE_VOUCHER_NUMBER, 0).getValue());
				oPostingInfo.oVoucherList = sVoucherNumber;
				if(!bNegativeCheck) {
					String sTraceID = "";
					if(oCheckPayment.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_VOUCHER_INTERFACE, PosCheckExtraInfo.VARIABLE_TRACE_ID, 0))
						sTraceID = oCheckPayment.getExtraInfoFromList(PosCheckExtraInfo.SECTION_VOUCHER_INTERFACE, PosCheckExtraInfo.VARIABLE_TRACE_ID, 0).getValue();
					oPostingInfo.sTraceID = sTraceID;
				}
			}
			else {
				sVoucherNumber.add(oVoucherInfo.get("voucherNumber"));
				oPostingInfo.oVoucherList = sVoucherNumber;
				if(!bNegativeCheck) 
					oPostingInfo.sTraceID = oVoucherInfo.get("TraceId");
			}
		}
		boolean bResult = m_oVoucherInterfaceConfig.doVoucherVoidPosting(formVoucherPostingJSONObject(oPostingInfo));
		if(bResult == false) {
			if(m_oVoucherInterfaceConfig.getLastErrorCode() != 0) 
				m_sLastErrorMessage = getErrorMessage(m_oVoucherInterfaceConfig.getLastErrorCode());
			else
				m_sLastErrorMessage = m_oVoucherInterfaceConfig.getLastErrorMessage();
			bFailToPost = true;
		}
		JSONObject oResponseJSONObject = m_oVoucherInterfaceConfig.getLastSuccessResult();

		if (oResponseJSONObject == null) {
			m_sLastErrorMessage = m_oVoucherInterfaceConfig.getLastErrorMessage();
			return false;
		} else {
			if (oResponseJSONObject.optBoolean("postingResult", false) == false) {
				if (oResponseJSONObject.has("errorMessage"))
					m_sLastErrorMessage = oResponseJSONObject.optString("errorMessage");
				else
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_void_voucher");
				return false;
			} else {
				if(m_oVoucherInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GALAXY)) {
					JSONObject oTempResultJSONObject = null;
					try {
						if(oResponseJSONObject.has("response") && oResponseJSONObject.optJSONObject("response").has("results"))
							oTempResultJSONObject = new JSONObject(oResponseJSONObject.optJSONObject("response").optJSONArray("results").optString(0));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if(oTempResultJSONObject == null
							|| oTempResultJSONObject != null
							&& !oTempResultJSONObject.optString("result").equals("Voided")) {
						if(oTempResultJSONObject != null && oTempResultJSONObject.has("message"))
							m_sLastErrorMessage = oTempResultJSONObject.optString("message");
						else
							m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_void_voucher");
						return false;
					} else {
						if(bNegativeCheck) {
							m_oLastVoucherResponseInfo = new VoucherInterfaceResponseInfo();
							m_oLastVoucherResponseInfo.sType = oTempResultJSONObject.optString("type", "");
							m_oLastVoucherResponseInfo.sAmount = oTempResultJSONObject.optString("amount", "");
							m_oLastVoucherResponseInfo.sTraceID = oResponseJSONObject.optJSONObject("response").optString("traceId", "");
						}
					}
				}
			}
		}
		if(bFailToPost)
			return false;
		else
			return true;
	}
	
	private JSONObject formVoucherPostingJSONObject(VoucherPostingInfo oPostingInfo) {
		JSONObject oPostingJSONObject = new JSONObject();
		try {
			if (this.m_oVoucherInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GALAXY)) {

				oPostingJSONObject.put("outletCode", oPostingInfo.sOutletCode);
				oPostingJSONObject.put("shopCode", oPostingInfo.iShopCode);
				oPostingJSONObject.put("employeeName", oPostingInfo.sEmployeeName);
				oPostingJSONObject.put("stationCode", oPostingInfo.sStationCode);
				oPostingJSONObject.put("outletId", oPostingInfo.iOutletId);
				oPostingJSONObject.put("checkNumber", oPostingInfo.sCheckNumber);
				oPostingJSONObject.put("employeeNumber", oPostingInfo.sEmployeeNumber);
				oPostingJSONObject.put("interfaceId", oPostingInfo.iInterfaceId);
				oPostingJSONObject.put("traceId", oPostingInfo.sTraceID);
				oPostingJSONObject.put("paymentAmount", oPostingInfo.dUnpaidTotal);
				oPostingJSONObject.put("negativeCheck", oPostingInfo.bNegativeCheck);

				if (oPostingInfo.oVoucherList != null && !oPostingInfo.oVoucherList.isEmpty()) {
					JSONArray oVoucherArray = new JSONArray(oPostingInfo.oVoucherList);
					oPostingJSONObject.put("vouchers", oVoucherArray);
				}
			}
		} catch (JSONException e) {
			AppGlobal.stack2Log(e);
			return null;
		}

		return oPostingJSONObject;
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
		default:
		case 0:
			sErrorMessage = null;
			break;
		}
		return sErrorMessage;
	}
}