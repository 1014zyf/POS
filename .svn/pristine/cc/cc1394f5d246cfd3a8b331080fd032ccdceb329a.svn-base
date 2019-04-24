package app;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppGlobal;
import om.MenuItem;
import om.MenuItemDept;
import om.MenuItemDeptGroup;
import om.MenuItemDeptGroupList;
import om.MenuItemDeptGroupLookup;
import om.MenuItemDeptList;
import om.PosCheckItem;
import om.PosCheckPayment;
import om.PosInterfaceConfig;
import om.PosPaymentMethod;
import om.PosPaymentMethodList;

public class FuncVGS {
	class DepartmentToShopMapping {
		String sShopCode;
		String sShopName;
		String[] oDepartment;
	}

	class VgsInvertory {
		String sShopCode;
		String sShopName;
		BigDecimal dTaxRate;
		BigDecimal dGrossAmount;
	}

	private HashMap<String, DepartmentToShopMapping> m_oInventoryMapping;

	// Last error message
	private String m_sErrorMessage;

	// FuncCheck
	private FuncCheck m_oFuncCheck;
	private List<PosCheckPayment> m_oPaymentList;
	private PosPaymentMethodList m_oPaymentMethodList;
	private PosInterfaceConfig m_oVgsInterfaceConfig;
	
	private ArrayList<MenuItemDept> m_oDepartmentList;

	// init object with initialize value
	public FuncVGS() {
		this.init();
	}

	// init object with Extra Info
	public FuncVGS(PosInterfaceConfig oVgsInterfaceConfig, FuncCheck oFuncCheck, FuncPayment oPostedFuncPayments) {
		m_oVgsInterfaceConfig = oVgsInterfaceConfig;
		m_oFuncCheck = oFuncCheck;
		m_oPaymentList = oPostedFuncPayments.getCheckPaymentList();
		m_oPaymentMethodList = oPostedFuncPayments.getPaymentMethodList();

		this.init();
	}

	// Return the error message
	public String getLastErrorMessage() {
		String sErrorCode = m_sErrorMessage;
		String sErrorMessage = m_sErrorMessage;
		switch (sErrorCode) {
		case "":
		case "0": // OK (Request is performed without error)
			sErrorMessage = "";
			break;
		case "18000":
			sErrorMessage = AppGlobal.g_oLang.get()._("gateway_internal_error");
			break;
		case "18001":
			sErrorMessage = AppGlobal.g_oLang.get()._("error_from_loyalty_portal");
			break;
		case "18002":
			sErrorMessage = AppGlobal.g_oLang.get()._("network_error");
			break;
		case "18003":
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_member_number");
			break;
		case "18004":
			sErrorMessage = AppGlobal.g_oLang.get()._("member_login_failed");
			break;
		case "18005":
			sErrorMessage = AppGlobal.g_oLang.get()._("member_not_found");
			break;
		case "18006":
			sErrorMessage = AppGlobal.g_oLang.get()._("member_account_already_expired");
			break;
		case "18007":
			sErrorMessage = AppGlobal.g_oLang.get()._("member_account_disabled");
			break;
		case "18008":
			sErrorMessage = AppGlobal.g_oLang.get()._("member_account_in_use");
			break;
		case "18009":
			// The operation for this member is not allowed
			sErrorMessage = AppGlobal.g_oLang.get()._("operation_for_this_member_is_not_allowed");
			break;
		case "18015":
			// Transaction with the same member no. already started
			sErrorMessage = AppGlobal.g_oLang.get()._("transaction_with_same_member_number_already_started");
			break;
		case "18020":
			sErrorMessage = AppGlobal.g_oLang.get()._("duplicate_request");
			break;
		case "18021":
			sErrorMessage = AppGlobal.g_oLang.get()._("transaction_content_corrupted");
			break;
		case "18022":
			sErrorMessage = AppGlobal.g_oLang.get()._("reference_id_does_not_exist");
			break;
		case "18023":
			sErrorMessage = AppGlobal.g_oLang.get()._("transaction_already_settled");
			break;
		case "18024":
			sErrorMessage = AppGlobal.g_oLang.get()._("transaction_session_already_expired");
			break;
		case "18025":
			sErrorMessage = AppGlobal.g_oLang.get()._("past_transaction_date_is_not_allowed");
			break;
		case "18026":
			sErrorMessage = AppGlobal.g_oLang.get()._("future_transaction_date_is_not_allowed");
			break;
		case "18027":
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_request_format_in_merchant_group");
			break;
		case "18028":
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_request_value_in_merchant_group");
			break;
		case "18029":
			sErrorMessage = AppGlobal.g_oLang.get()._("duplicate_item_reference");
			break;
		case "18030":
			sErrorMessage = AppGlobal.g_oLang.get()._("missing_campaign_set");
			break;
		case "18031":
			sErrorMessage = AppGlobal.g_oLang.get()._("campaign_set_corrupted");
			break;
		case "18032":
			sErrorMessage = AppGlobal.g_oLang.get()._("failed_in_running_campaign");
			break;
		case "18033":
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_brc_code");
			break;
		case "18034":
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_benefit_code");
			break;
		case "18035":
			sErrorMessage = AppGlobal.g_oLang.get()._("member_bonus_not_enough");
			break;
		case "18040":
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_request");
			break;
		case "18041":
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_request_format");
			break;
		case "18042":
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_request_value");
			break;
		case "18043":
			sErrorMessage = AppGlobal.g_oLang.get()._("forbidden_access_gateway");
			break;
		case "18044":
			sErrorMessage = AppGlobal.g_oLang.get()._("pos_online_session_already_expired");
			break;
		case "18045":
			sErrorMessage = AppGlobal.g_oLang.get()._("unauthorized_access_gateway");
			break;
		case "18046":
			sErrorMessage = AppGlobal.g_oLang.get()._("pgp_version_not_supported");
			break;
		case "18050":
			sErrorMessage = AppGlobal.g_oLang.get()._("unauthorized_access_portal");
			break;
		case "18051":
			sErrorMessage = AppGlobal.g_oLang.get()._("loyalty_service_temporarily_unavailable");
			break;
		case "18052":
			sErrorMessage = AppGlobal.g_oLang.get()._("forbidden_access_portal");
			break;
		case "18053":
			sErrorMessage = AppGlobal.g_oLang.get()._("gateway_session_already_expired");
			break;
		case "18060":
			sErrorMessage = AppGlobal.g_oLang.get()._("internal_license_error");
			break;
		case "18061":
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_license_file");
			break;
		case "18062":
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_license_key");
			break;
		case "18063":
			sErrorMessage = AppGlobal.g_oLang.get()._("wrong_license_file");
			break;
		case "18064":
			sErrorMessage = AppGlobal.g_oLang.get()._("no_license_file_in_system");
			break;
		case "18065":
			sErrorMessage = AppGlobal.g_oLang.get()._("no_license_key_in_system");
			break;
		case "18066":
			sErrorMessage = AppGlobal.g_oLang.get()._("license_has_been_expired");
			break;
		case "18070":
			// The function has been disabled
			sErrorMessage = AppGlobal.g_oLang.get()._("function_has_been_disabled");
			break;
		}

		return sErrorMessage;
	}

	// init object
	public void init() {
		m_oInventoryMapping = new HashMap<String, DepartmentToShopMapping>();
		m_sErrorMessage = "";
		
		initMapping();
	}
	
	private void initMapping() {
		String[] sMappingConfigs = null;
		String[] sConfig = null;
		if (m_oVgsInterfaceConfig.getInterfaceConfig().has("menu_department_setup")
				&& m_oVgsInterfaceConfig.getInterfaceConfig().optJSONObject("menu_department_setup")
						.optJSONObject("params").has("menu_department_to_shop_mapping")) {
			sMappingConfigs = m_oVgsInterfaceConfig.getInterfaceConfig().optJSONObject("menu_department_setup")
					.optJSONObject("params").optJSONObject("menu_department_to_shop_mapping").optString("value")
					.split("\r\n");
			
			m_oDepartmentList = new ArrayList<MenuItemDept>();
			initDepartmentList();
			
			for (int i = 0; i < sMappingConfigs.length; i++) {
				sConfig = sMappingConfigs[i].split("=");
				
				if (sConfig.length == 2) {
					String[] sDeptGroupList = null;
					ArrayList<String> sDeptConfigs = new ArrayList<String>();
					String[] sShopConfig = null;
					sDeptGroupList = sConfig[0].split(",");
					ArrayList<String> oDeptCodeList = new ArrayList<String>();
					Collections.addAll(oDeptCodeList, sDeptGroupList);
					sShopConfig = sConfig[1].split(",");
					ArrayList<Integer> oDeptIdList = new ArrayList<Integer>();

					// get department group content
					MenuItemDeptGroupList oMenuItemDeptGroupList = new MenuItemDeptGroupList();
					oMenuItemDeptGroupList.readItemDeptGroupListByCode(oDeptCodeList);

					MenuItemDeptGroup oItemDeptGroup = oMenuItemDeptGroupList.getByCode(sDeptGroupList[0]);
					if (oItemDeptGroup != null) {
						ArrayList<MenuItemDeptGroupLookup> oDeptGroupLookups = oItemDeptGroup
								.getItemDeptGroupLookupList();
						if (oDeptGroupLookups != null) {
							for (int j = 0; j < oDeptGroupLookups.size(); j++) {
								if (oDeptGroupLookups.get(j).getDeptId() != 0)
									oDeptIdList.add(oDeptGroupLookups.get(j).getDeptId());
							}
						}
					}
					
					if (!oDeptIdList.isEmpty()) {
						for(MenuItemDept oMenuItemDept : m_oDepartmentList){
							if (oDeptIdList.contains(oMenuItemDept.getIdepId()))
								sDeptConfigs.add(oMenuItemDept.getCode());
						}
						for (String sDeptConfig : sDeptConfigs) {
							if (m_oInventoryMapping.get(sDeptConfig) == null) {
								DepartmentToShopMapping oDepartmentToShopMapping = new DepartmentToShopMapping();
								oDepartmentToShopMapping.sShopCode = sShopConfig[0];
								oDepartmentToShopMapping.sShopName = sShopConfig[1];
								m_oInventoryMapping.put(sDeptConfig, oDepartmentToShopMapping);
							}
						}
					}
				}
			}
		}
	}
	
	public void initDepartmentList() {
		MenuItemDeptList oItemDeptList = new MenuItemDeptList();
		oItemDeptList.readItemDeptList();
		
		for(MenuItemDept oItemDept: oItemDeptList.getItemDeptList()) {
			m_oDepartmentList.add(oItemDept);
			getSubMenuItemDeptList(oItemDept);
		}
	}
	
	public void getSubMenuItemDeptList(MenuItemDept oItemDept) {
		if(!oItemDept.getChildDeptList().isEmpty()) {
			for(MenuItemDept oSubDept: oItemDept.getChildDeptList()) {
				m_oDepartmentList.add(oSubDept);
				getSubMenuItemDeptList(oSubDept);
			}
		}
	}
	
	public String getPayUrl() {
		MessageDigest oMessageDigest;
		DateTime oToday = AppGlobal.getCurrentTime(false);

		DateTimeFormatter fmtDate = DateTimeFormat.forPattern("yyyyMMdd");
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddHHmmss");

		PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
		HashMap<String, String> oVgsGetPayUrlInformation = new HashMap<String, String>();

		JSONObject oBwyParam = new JSONObject();
		JSONObject oContent = new JSONObject();

		String sBillCheckVal = "";
		String sDateTime = "";
		String sInvoiceNumber = "";
		String sPayUrl = "";
		String sCheckPrefixNo = m_oFuncCheck.getCheckPrefixNo();

		if (m_oFuncCheck.isFastFoodCheck()
				&& (!AppGlobal.g_oFuncOutlet.get().getBusinessDay().isCheckNumGeneratedByOutlet() || AppGlobal.g_oFuncSmartStation.isStandaloneRole())
				&& !m_oFuncCheck.isOldCheck()) {
					sCheckPrefixNo = AppGlobal.g_oFuncStation.get().getNextCheckPrefixNumber(false);
					m_oFuncCheck.setReferenceCheckPrefix(sCheckPrefixNo);
		}
		sDateTime = fmt.print(oToday);
		sInvoiceNumber = AppGlobal.g_oFuncOutlet.get().getOutletCode() + fmtDate.print(oToday) + sCheckPrefixNo;

		sBillCheckVal = m_oVgsInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup")
				.optJSONObject("params").optJSONObject("vgs_app_key").optString("value");
		sBillCheckVal = sBillCheckVal.concat(sDateTime);
		sBillCheckVal = sBillCheckVal.concat(m_oVgsInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup")
				.optJSONObject("params").optJSONObject("bill_app_key").optString("value"));
		sBillCheckVal = sBillCheckVal.concat(sInvoiceNumber);
		sBillCheckVal = sBillCheckVal.concat(m_oVgsInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup")
				.optJSONObject("params").optJSONObject("store_id").optString("value"));
		sBillCheckVal = sBillCheckVal.toUpperCase();
		try {
			oMessageDigest = MessageDigest.getInstance("MD5");
			oMessageDigest.reset();
			oMessageDigest.update(sBillCheckVal.getBytes("UTF-8"));
			byte[] digest = oMessageDigest.digest();

			sBillCheckVal = String.format("%032x", new BigInteger(1, digest));
		} catch (NoSuchAlgorithmException jsone) {
			AppGlobal.stack2Log(jsone);
		} catch (UnsupportedEncodingException jsone) {
			AppGlobal.stack2Log(jsone);
		}
		sBillCheckVal = new StringBuilder().append(sBillCheckVal.charAt(9)).append(sBillCheckVal.charAt(4))
				.append(sBillCheckVal.charAt(30)).append(sBillCheckVal.charAt(13)).toString();

		oVgsGetPayUrlInformation.put("outletId", String.valueOf(AppGlobal.g_oFuncOutlet.get().getOutletId()));
		oVgsGetPayUrlInformation.put("outletCode", AppGlobal.g_oFuncOutlet.get().getOutletCode());
		oVgsGetPayUrlInformation.put("interfaceId", Integer.toString(m_oVgsInterfaceConfig.getInterfaceId()));

		try {
			oContent.put("vgsAppId", m_oVgsInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup")
					.optJSONObject("params").optJSONObject("vgs_app_id").optString("value"));
			oContent.put("dateTime", sDateTime);
			oContent.put("billCheckVal", sBillCheckVal);

			oBwyParam.put("billAppId", m_oVgsInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup")
					.optJSONObject("params").optJSONObject("bill_app_id").optString("value"));
			oBwyParam.put("invoiceNumber", sInvoiceNumber);
			oBwyParam.put("storeId", m_oVgsInterfaceConfig.getInterfaceConfig().optJSONObject("general_setup")
					.optJSONObject("params").optJSONObject("store_id").optString("value"));
			JSONArray oGroupItemJsonArray = groupItemToInventory();
			if(oGroupItemJsonArray == null)
				return "";
			else
				oBwyParam.put("inventory", oGroupItemJsonArray);
			oContent.put("bwyParam", oBwyParam);
		} catch (JSONException jsone) {
			AppGlobal.stack2Log(jsone);
		}

		oVgsGetPayUrlInformation.put("content", oContent.toString());

		JSONObject oResult = oPosInterfaceConfig.getVgsPayUrl(oVgsGetPayUrlInformation);
		if (oResult != null && oResult.has("payurl")) {
			sPayUrl = oResult.optString("payurl");
		} else
			try {
				m_sErrorMessage = oResult.has("errorMessage") ? oResult.getString("errorMessage") : "";
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return sPayUrl;
	}

	public JSONArray groupItemToInventory() {
		JSONArray oInvertoriesJSON = new JSONArray();
		HashMap<String, HashMap<BigDecimal, VgsInvertory>> oInventory = new HashMap<String, HashMap<BigDecimal, VgsInvertory>>();

		MenuItemDeptList oMenuItemDeptList = new MenuItemDeptList();
		oMenuItemDeptList.readItemDeptList();
		HashMap<String, BigDecimal> oPaymentRatioAndPaymentIncludeAmt = calInclusivePaymentRatio();
		if(oPaymentRatioAndPaymentIncludeAmt == null)
			return null;
		
		BigDecimal dIncludePaymentRatio = oPaymentRatioAndPaymentIncludeAmt.get("PaymentRatio");
		BigDecimal dPaymentIncludeAmount = oPaymentRatioAndPaymentIncludeAmt.get("PaymentIncludedAmount");
		
		for (int i = 0; i <= AppGlobal.MAX_SEATS; i++) {
			ArrayList<FuncCheckItem> oFuncCheckItemList = (ArrayList<FuncCheckItem>) m_oFuncCheck.getItemList(i);
			if (oFuncCheckItemList != null && !oFuncCheckItemList.isEmpty()) {
				for (int j = 0; j < oFuncCheckItemList.size(); j++) {
					BigDecimal dTaxRate = BigDecimal.ZERO;
					BigDecimal dItemGrossAmount = BigDecimal.ZERO;

					FuncCheckItem oFuncCheckItem = oFuncCheckItemList.get(j);
					// Return error when the menuItem of the target item cannot be found
					MenuItem oMenuItem = oFuncCheckItem.getMenuItem();
					if(oMenuItem == null){
						m_sErrorMessage = AppGlobal.g_oLang.get()._("item_cannot_be_found");
						return null;
					}
					int iDepartmentId = oMenuItem.getDeparmentId();
					String sDepartmentCode = "";
					if (iDepartmentId > 0) {
						for (MenuItemDept iDept : oMenuItemDeptList.getItemDeptList()) {
							if (iDept.getIdepId() == iDepartmentId) {
								sDepartmentCode = iDept.getCode();
								break;
							}
						}
					}

					dItemGrossAmount = dItemGrossAmount.add(oFuncCheckItem.getItemNetTotal(true));
					for (int iScIndex = 1; iScIndex <= 5; iScIndex++)
						dItemGrossAmount = dItemGrossAmount.add(oFuncCheckItem.getCheckItem().getSc(iScIndex));
					for (int iTaxIndex = 1; iTaxIndex <= 25; iTaxIndex++)
						dItemGrossAmount = dItemGrossAmount.add(oFuncCheckItem.getCheckItem().getTax(iTaxIndex));
					dItemGrossAmount = dItemGrossAmount.multiply(dIncludePaymentRatio);

					dItemGrossAmount.setScale(2, RoundingMode.HALF_UP);

					// Calculate the Rounding
					dPaymentIncludeAmount = dPaymentIncludeAmount.subtract(dItemGrossAmount);

					for (int iIndex = 1; iIndex <= 25; iIndex++) {
						if (!oFuncCheckItem.getMenuItem().getChargeTax(iIndex - 1).equals(PosCheckItem.CHARGE_TAX_NO))
							dTaxRate = dTaxRate.add(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getTax(iIndex));
					}

					VgsInvertory oVgsInvertory = new VgsInvertory();
					oVgsInvertory.dTaxRate = dTaxRate;
					// Check the oInventory Hashmap include that item shop code
					// If found
					if (m_oInventoryMapping.containsKey(sDepartmentCode)
							&& oInventory.get(m_oInventoryMapping.get(sDepartmentCode).sShopCode) != null) {
						oVgsInvertory.sShopCode = m_oInventoryMapping.get(sDepartmentCode).sShopCode;
						oVgsInvertory.sShopName = m_oInventoryMapping.get(sDepartmentCode).sShopName;

						// Check has the tax rate was add in the Hashmap
						// If Found
						if (oInventory.get(m_oInventoryMapping.get(sDepartmentCode).sShopCode).get(dTaxRate) != null) {
							VgsInvertory updateVgsInvertory = oInventory
									.get(m_oInventoryMapping.get(sDepartmentCode).sShopCode).get(dTaxRate);
							updateVgsInvertory.dGrossAmount = updateVgsInvertory.dGrossAmount.add(dItemGrossAmount);
							oInventory.get(m_oInventoryMapping.get(sDepartmentCode).sShopCode).put(dTaxRate, updateVgsInvertory);
						} else {
							oVgsInvertory.dGrossAmount = dItemGrossAmount;
							oInventory.get(m_oInventoryMapping.get(sDepartmentCode).sShopCode).put(dTaxRate, oVgsInvertory);
						}
						
					} else {
						oVgsInvertory.sShopCode = "";
						oVgsInvertory.sShopName = "";
						if (m_oInventoryMapping.containsKey(sDepartmentCode)) {
							oVgsInvertory.sShopCode = m_oInventoryMapping.get(sDepartmentCode).sShopCode;
							oVgsInvertory.sShopName = m_oInventoryMapping.get(sDepartmentCode).sShopName;
						}

						if (oInventory.get(oVgsInvertory.sShopCode) != null) {
							if (oInventory.get(oVgsInvertory.sShopCode).get(dTaxRate) != null) {
								VgsInvertory updateVgsInvertory = oInventory.get(oVgsInvertory.sShopCode).get(dTaxRate);
								updateVgsInvertory.dGrossAmount = updateVgsInvertory.dGrossAmount.add(dItemGrossAmount);
								oInventory.get(oVgsInvertory.sShopCode).put(dTaxRate, updateVgsInvertory);
							} else {
								oVgsInvertory.dGrossAmount = dItemGrossAmount;
								oInventory.get(oVgsInvertory.sShopCode).put(dTaxRate, oVgsInvertory);
							}
						} else {
							oVgsInvertory.dGrossAmount = dItemGrossAmount;

							HashMap<BigDecimal, VgsInvertory> newVgsInvertory = new HashMap<BigDecimal, VgsInvertory>();
							newVgsInvertory.put(dTaxRate, oVgsInvertory);
							oInventory.put(oVgsInvertory.sShopCode, newVgsInvertory);
						}
					}
				}
			}
		}

		// Transfer Hashmap to JSON format
		for (Map.Entry<String, HashMap<BigDecimal, FuncVGS.VgsInvertory>> oInventoryChildren : oInventory.entrySet()) {
			for (Map.Entry<BigDecimal, FuncVGS.VgsInvertory> oInventoryChild : oInventoryChildren.getValue()
					.entrySet()) {
				JSONObject oInventoryGroupJSON = new JSONObject();
				try {
					oInventoryGroupJSON.put("shopCode", oInventoryChild.getValue().sShopCode);
					oInventoryGroupJSON.put("shopName", oInventoryChild.getValue().sShopName);
					oInventoryGroupJSON.put("taxRate", oInventoryChild.getValue().dTaxRate);
					// Rounding Handling
					if (dPaymentIncludeAmount.compareTo(BigDecimal.ZERO) != 0) {
						oInventoryChild.getValue().dGrossAmount = oInventoryChild.getValue().dGrossAmount
								.add(dPaymentIncludeAmount);
						dPaymentIncludeAmount = BigDecimal.ZERO;
					}
					oInventoryGroupJSON.put("grossAmount",
							oInventoryChild.getValue().dGrossAmount.setScale(2, RoundingMode.HALF_UP));
				} catch (JSONException jsone) {
					AppGlobal.stack2Log(jsone);
				}
				oInvertoriesJSON.put(oInventoryGroupJSON);
			}
		}
		return oInvertoriesJSON;
	}

	private HashMap<String, BigDecimal> calInclusivePaymentRatio() {
		String[] sExcludePaymentCodes = null;
		BigDecimal dExcludePaymentAmount = BigDecimal.ZERO;
		BigDecimal dTotalPaymentAmount = BigDecimal.ZERO;
		PosPaymentMethodList oPaymentMethodList = m_oPaymentMethodList;
		PosPaymentMethod oPaymentMethod = null;
		HashMap<String, BigDecimal> oPaymentRatioAndIncludeTotalAmount = new HashMap<String, BigDecimal>();

		sExcludePaymentCodes = m_oVgsInterfaceConfig.getInterfaceConfig().optJSONObject("payment_setup")
				.optJSONObject("params").optJSONObject("exclude_payment_code").optString("value").split(",");

		for (PosCheckPayment oPosCheckPayment : m_oPaymentList) {
			for (String sExcludePaymentCode : sExcludePaymentCodes) {
				oPaymentMethod = oPaymentMethodList.getPaymentMethod(oPosCheckPayment.getPaymentMethodId());
				if (sExcludePaymentCode.equals(oPaymentMethod.getPaymentCode())) {
					dExcludePaymentAmount = dExcludePaymentAmount.add(oPosCheckPayment.getPayTotal());
					break;
				}
			}
			dTotalPaymentAmount = dTotalPaymentAmount.add(oPosCheckPayment.getPayTotal());
		}
		
		// return null if payment total is zero or excluded payment amount is same as payment total
		if(dTotalPaymentAmount.compareTo(BigDecimal.ZERO) == 0 || dTotalPaymentAmount.compareTo(dExcludePaymentAmount) == 0)
			return null;
		
		oPaymentRatioAndIncludeTotalAmount.put("PaymentRatio", dTotalPaymentAmount.subtract(dExcludePaymentAmount)
				.divide(dTotalPaymentAmount, 6, BigDecimal.ROUND_HALF_DOWN));
		oPaymentRatioAndIncludeTotalAmount.put("PaymentIncludedAmount",
				dTotalPaymentAmount.subtract(dExcludePaymentAmount));

		return oPaymentRatioAndIncludeTotalAmount;
	}
}
