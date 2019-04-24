package app.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.model.InfInterface;
import app.model.InfVendor;
import app.model.PosCheckExtraInfo;
import app.model.PosInterfaceConfig;

public class FuncPaymentInterface {
	static public String PAY_CHECK_SUCCESS = "s";
	static public String PAY_CHECK_FAIL = "f";
	
	private PosInterfaceConfig m_oPaymentInterface;
	public String m_sLastErrorMessage;
	
	public FuncPaymentInterface(PosInterfaceConfig oPaymentInterface) {
		m_oPaymentInterface = oPaymentInterface;
	}
	
	public List<JSONObject> checkPaymentResult() {
		// for OGS interface
		if(m_oPaymentInterface.getInterfaceVendorKey().equals(InfVendor.KEY_OGS)) {
			List<JSONObject> oPaidCheckList = checkOgsPaymentResult();
			return oPaidCheckList;
		}
		
		return null;
	}
	
	
	// check whether support payment interface
	static boolean isSupportPaymentInterface() {
		if(AppGlobal.isModuleSupport(AppGlobal.OPTIONAL_MODULE.pos_interface.name()) == false)
			return false;
		if(AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PAYMENT_INTERFACE) == null || AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PAYMENT_INTERFACE).size() == 0)
			return false;
		
		return true;
	}
	
	// check payment result with OGS interface
	private List<JSONObject> checkOgsPaymentResult() {
		List<JSONObject> oPaidCheckList = new ArrayList<JSONObject>();
		if(!AppGlobal.g_oCheckListForPaymentInterface.containsKey(AppGlobal.g_oFuncOutlet.get().getOutletId()))
			return oPaidCheckList;
		
		if(AppGlobal.g_oCheckListForPaymentInterface.get(AppGlobal.g_oFuncOutlet.get().getOutletId()).size() == 0)
			return oPaidCheckList;
		
		//Filter out the corresponding interface printed check with same interface ID and interface paytype
		List<HashMap<String,String>> oCheckToRemoveFromList = new ArrayList<HashMap<String,String>>();
		HashMap<Integer, List<HashMap<String, String>>> oPrintedCheckList = new HashMap<Integer, List<HashMap<String, String>>>();
		for(HashMap<String, String> oPrintedCheckInfo: AppGlobal.g_oCheckListForPaymentInterface.get(AppGlobal.g_oFuncOutlet.get().getOutletId())) {
			if(!oPrintedCheckInfo.containsKey("interfaceId") || (oPrintedCheckInfo.containsKey("interfaceId") && Integer.valueOf(oPrintedCheckInfo.get("interfaceId")) != m_oPaymentInterface.getInterfaceId()))
				continue;
			
			//check expired check
			if(oPrintedCheckInfo.containsKey("expireTime")) {
				DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMddHHmmss");
				DateTime dExpiryTime = formatter.parseDateTime(oPrintedCheckInfo.get("expireTime"));
				DateTime dCurrentTime = new DateTime();
				if (dCurrentTime.compareTo(dExpiryTime) >= 0) {
					HashMap<String, String> oTempInfo = new HashMap<String, String>();
					oTempInfo.put("checkId", oPrintedCheckInfo.get("checkId"));
					oTempInfo.put("outTradeNumber",	oPrintedCheckInfo.get("outTradeNumber"));
					
					oCheckToRemoveFromList.add(oTempInfo);
					continue;
				}
			}
			
			Integer iPayType = Integer.valueOf(oPrintedCheckInfo.get("paytype"));
			if(!oPrintedCheckList.containsKey(iPayType))
				oPrintedCheckList.put(iPayType, new ArrayList<HashMap<String, String>>());
			oPrintedCheckList.get(iPayType).add(oPrintedCheckInfo);
		}
		
		//remove expired check
		if(oCheckToRemoveFromList.size() > 0) {
			for(HashMap<String, String> oCheckInfo: oCheckToRemoveFromList) {
				AppGlobal.removePrintedCheckToPaymentInterfaceCheckList(AppGlobal.g_oFuncOutlet.get().getOutletId(), Integer.valueOf(oCheckInfo.get("checkId")).intValue(), oCheckInfo.get("outTradeNumber"));
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId() + "", AppGlobal.g_oFuncUser.get().getUserId() + "", "Check expired, remove from list. CheckID:"+oCheckInfo.get("checkId")+", outTradeNumber:"+oCheckInfo.get("outTradeNumber"));
			}
		}
		
		if(oPrintedCheckList.size() == 0)
			return oPaidCheckList;
		
		JSONArray oPrintedCheckJSONArray = new JSONArray();
		for(Map.Entry<Integer, List<HashMap<String, String>>> entry : oPrintedCheckList.entrySet()) {
			try {
				JSONObject oPayTypeWithChecksJSONObject = new JSONObject();
				JSONArray oCheckInfos = new JSONArray();
				
				int iPayType = entry.getKey().intValue();
				List<HashMap<String, String>> oPrintedCheckListByType = entry.getValue();
				
				for(HashMap<String, String> oPrintedCheckInfo: oPrintedCheckListByType) {
					JSONObject oCheckInfoJSONObject = new JSONObject();
					oCheckInfoJSONObject.put("outTradeNumber", oPrintedCheckInfo.get("outTradeNumber"));
					oCheckInfos.put(oCheckInfoJSONObject);
				}
				
				oPayTypeWithChecksJSONObject.put("paytype", iPayType);
				oPayTypeWithChecksJSONObject.put("outTradeNumbers", oCheckInfos);
				oPrintedCheckJSONArray.put(oPayTypeWithChecksJSONObject);
			}catch(JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		
		//checking the payment result
		JSONArray oPaymentResults = null;
		oPaymentResults = m_oPaymentInterface.checkPaymentResults(AppGlobal.g_oFuncOutlet.get().getOutletCode(), oPrintedCheckJSONArray);
		
		//handle return
		if(oPaymentResults != null) {
			for(int i=0; i<oPaymentResults.length(); i++) {
				JSONObject oPaymentResult = oPaymentResults.optJSONObject(i);
				
				//check whether return is error
				 if (oPaymentResult != null && oPaymentResult.has("errorCode") && !oPaymentResult.optString("errorCode").isEmpty()) {
					String sOutTradeNumber = oPaymentResult.optString("outTradeNumber", "");
					
					if(oPaymentResult.has("errorMessage") && oPaymentResult.optString("errorMessage").equals("URL unavailable"))
						continue;
					
					for(HashMap<String, String> oPrintedCheckInfo: AppGlobal.g_oCheckListForPaymentInterface.get(AppGlobal.g_oFuncOutlet.get().getOutletId())) {
						if(oPrintedCheckInfo.containsKey("outTradeNumber") && oPrintedCheckInfo.get("outTradeNumber").equals(sOutTradeNumber)) {
							JSONObject oTempInfo = new JSONObject();
							if(oPrintedCheckInfo.containsKey("checkId")) {
								try {
									oTempInfo.put("checkId", String.valueOf(oPrintedCheckInfo.get("checkId")));
									oTempInfo.put("outTradeNumber", oPrintedCheckInfo.get("outTradeNumber"));
									oTempInfo.put("result", PAY_CHECK_FAIL);
									oTempInfo.put("errorCode", oPaymentResult.optString("errorCode"));
									oTempInfo.put("errorMessage", oPaymentResult.optString("errorMessage"));
									oTempInfo.put("errorMemo", oPaymentResult.optString("errorMemo"));
								}catch(JSONException e) {}
							}
							oPaidCheckList.add(oTempInfo);
							break;
						}
					}
				} 
				//check whether check is paid
				else if(oPaymentResult != null && oPaymentResult.has("tradeStatus") && oPaymentResult.optInt("tradeStatus", -1) == 0) {
					String sOutTradeNumber = oPaymentResult.optString("outTradeNumber", "");
					
					for(HashMap<String, String> oPrintedCheckInfo: AppGlobal.g_oCheckListForPaymentInterface.get(AppGlobal.g_oFuncOutlet.get().getOutletId())) {
						if(oPrintedCheckInfo.containsKey("outTradeNumber") && oPrintedCheckInfo.get("outTradeNumber").equals(sOutTradeNumber)) {
							JSONObject oTempInfo = new JSONObject();
							if(oPrintedCheckInfo.containsKey("checkId")) {
								try {
									oTempInfo.put("checkId", String.valueOf(oPrintedCheckInfo.get("checkId")));
									oTempInfo.put("outTradeNumber", oPrintedCheckInfo.get("outTradeNumber"));
									oTempInfo.put("result", PAY_CHECK_SUCCESS);
									BigDecimal dCheckTotal = new BigDecimal(oPaymentResult.optString("payTotal"));
									dCheckTotal = dCheckTotal.add(new BigDecimal(oPaymentResult.optString("discountTotal")));
									oTempInfo.put("checkTotal", dCheckTotal.toPlainString());
									oTempInfo.put("payTotal", oPaymentResult.optString("payTotal"));
									oTempInfo.put("discountTotal", oPaymentResult.optString("discountTotal"));
								}catch (JSONException e) {}
							}
							oPaidCheckList.add(oTempInfo);
							break;
						}
					}
				}
			}
		}
		return oPaidCheckList;
	}

	// get release payment result with OGS interface
	public boolean releaseOgsPayment(FuncCheck oFuncCheck) {
		if(!m_oPaymentInterface.getInterfaceVendorKey().equals(InfVendor.KEY_OGS))
			return false;

		boolean bResult = false;
		if(!oFuncCheck.getCheckExtraInfoList().isEmpty()) {
			int iPayType = 0;
			String sOutTradeNumber = "";
			for(PosCheckExtraInfo oCheckExtraInfo: oFuncCheck.getCheckExtraInfoList()) {
				if(oCheckExtraInfo.getBy().equals(PosCheckExtraInfo.BY_PAYMENT) &&
						oCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_PAYMENT_INTERFACE)){
					if(oCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_PAYTYPE)){
						if(oCheckExtraInfo.getValue() != null)
							iPayType = Integer.parseInt(oCheckExtraInfo.getValue());
					}else if(oCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_OUT_TRADE_NUMBER)){
						if(oCheckExtraInfo.getValue() != null)
							sOutTradeNumber =  oCheckExtraInfo.getValue();
					}
				}
			}
			JSONObject oCheckInfo = new JSONObject();
			try {
				oCheckInfo.put("checkTotal", oFuncCheck.getCheckTotal());
				oCheckInfo.put("outTradeNumber", sOutTradeNumber);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			bResult = m_oPaymentInterface.releaseOgsPayment(AppGlobal.g_oFuncOutlet.get().getOutletCode(), iPayType, oCheckInfo);
			if(!bResult) {
				if(m_oPaymentInterface.getLastErrorCode() > 0)
					m_sLastErrorMessage = getErrorMessage(m_oPaymentInterface.getLastErrorCode());
				else 
					m_sLastErrorMessage = m_oPaymentInterface.getLastErrorMessage();
			}
		}
		
		return bResult;		
	}
	
	// restart payment interface shell
	public boolean restartPaymentInterfaceShell(int iInterfaceId) {
		PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
		boolean bResult = oInterfaceConfig.restartPaymentInterfaceShell(iInterfaceId);
		if(!bResult) {
			if(oInterfaceConfig.getLastErrorMessage().equals("shell_is_not_alive"))
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_open_shell");
			else if(oInterfaceConfig.getLastErrorCode() > 0)
				m_sLastErrorMessage = getErrorMessage(oInterfaceConfig.getLastErrorCode());
			else 
				m_sLastErrorMessage = oInterfaceConfig.getLastErrorMessage();
		}
		
		return bResult;
	}
	
	// stop payment interface shell
	public boolean stopPaymentInterfaceShell(int iInterfaceId) {
		PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
		boolean bResult = oInterfaceConfig.stopPaymentInterfaceShell(iInterfaceId);
		m_sLastErrorMessage = AppGlobal.g_oLang.get()._("shell_is_stopped");
		if(!bResult)
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("stop_shell_fail");
		
		return bResult;
	}
	
	public String getErrorMessage(int iErrorCode) {
		String sErrorMessage = "";
		
		switch (iErrorCode) {
		case 0:
			sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_build_connection");
			break;
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
			sErrorMessage = AppGlobal.g_oLang.get()._("empty_response");
			break;
		default:
			sErrorMessage = null;
			break;
		}
		
		return sErrorMessage;
	}
	
	public String getLastErrorMessage() {
		return this.m_sLastErrorMessage;
	}
}

