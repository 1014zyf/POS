package app;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import om.InfInterface;
import om.PosCheckPayment;
import om.PosInterfaceConfig;

public class FuncInventoryInterface {
	public class PostingInfo {
		public String 		sOutletCode;
		public int 			iInterfaceId;
		public String 		sItemId;
		public BigDecimal	dItemQty;
		public String 		sCheckId;
		public String		sShopCode;
		public String		sDate;
		public String		sCheckNumber;
		ArrayList<HashMap<String, String>> oItemList;
	}
	
	class PostingResponse {
		boolean					bSuccess;
		String					sResultCode;
		String					sErrorMessage;
		ArrayList<RecipeData>	oRecipeDatalist;
	}
	
	// Recipe Data for "Cooking the book" interface
	class RecipeData {
		BigDecimal 	dQty;
	}
	
	private PosInterfaceConfig m_oInventoryInterface;
	
	public String m_sLastErrorMessage;
	private int m_iLastErrorCode;
	private PostingResponse m_oPostingResponse;
	
	public FuncInventoryInterface(PosInterfaceConfig oInventoryInterface) {
		m_oInventoryInterface = oInventoryInterface;
		m_sLastErrorMessage = "";
		m_iLastErrorCode = 0;
		m_oPostingResponse = new PostingResponse();
		m_oPostingResponse.bSuccess = false;
		m_oPostingResponse.sResultCode = "";
		m_oPostingResponse.sErrorMessage = "";
		m_oPostingResponse.oRecipeDatalist = new ArrayList<RecipeData>();
	}
	
	// check whether support payment interface
	static boolean isSupportInventInterface() {
		if(AppGlobal.isModuleSupport(AppGlobal.OPTIONAL_MODULE.pos_interface.name()) == false)
			return false;
		if(AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_INVENTORY_INTERFACE) == null || AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_INVENTORY_INTERFACE).isEmpty())
			return false;
		
		return true;
	}
	
	private void initPostingInfo(PostingInfo oPostingInfo) {
		oPostingInfo.iInterfaceId = m_oInventoryInterface.getInterfaceId();
		oPostingInfo.sItemId = "";
		oPostingInfo.dItemQty = BigDecimal.ZERO;
		oPostingInfo.sCheckId = "";
	}
	
	// do inventory interface add wastage posting
	public boolean doAddWastage(String sItemId, BigDecimal dItemQty) {
		PostingInfo oPostingInfo = new PostingInfo();
		
		initPostingInfo(oPostingInfo);
		oPostingInfo.sItemId = sItemId;
		oPostingInfo.dItemQty = dItemQty;
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.sShopCode = AppGlobal.g_oFuncOutlet.get().getShopCode();
		
		PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
		JSONObject oResponseJSONObject = oPosInterfaceConfig.doInventoryAddWastage(oPostingInfo);
		return handleResponseJSON(oResponseJSONObject);
	}
	
	// do inventory interface add sales posting

	public boolean doAddSales(FuncCheck oFuncCheck) {
		PostingInfo oPostingInfo = new PostingInfo();
		
		initPostingInfo(oPostingInfo);
		
		//oPostingInfo.sCheckId = sCheckId;
		oPostingInfo.sCheckId = oFuncCheck.getCheckId();
		
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.sShopCode = AppGlobal.g_oFuncOutlet.get().getShopCode();
		
		// get current business date
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		oPostingInfo.sDate = dateFormat.print(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDate());
		
		// get check number
		if (!AppGlobal.g_oFuncOutlet.get().getBusinessDay().isCheckNumGeneratedByOutlet() && !oFuncCheck.isOldCheck())
			oPostingInfo.sCheckNumber = AppGlobal.g_oFuncStation.get().getNextCheckPrefixNumber(false);
		else
			oPostingInfo.sCheckNumber = oFuncCheck.getCheckPrefixNo();
		
		//item list: citm_role + citm_order_stat_id + citm_seq + citm_item_id + item_inventory_code (from menuItem) + citm_code
		//			 + citm_name_l(x) + citm_price_level + citm_qty + citm_price + citm_round_total + citm_tax(1-25)
		//			 + citm_pre_disc / citm_mid_disc / citm_post_disc
		oPostingInfo.oItemList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> oItemInfo = new HashMap<String,String>();
		
		for (List<FuncCheckItem> oFuncCheckItemList: oFuncCheck.getWholeItemList()) {
			for (FuncCheckItem oFuncCheckItem: oFuncCheckItemList) {
				oItemInfo.put("itemRole", oFuncCheckItem.getCheckItem().getRole());
				oItemInfo.put("itemOrderStationId", Integer.toString(oFuncCheckItem.getCheckItem().getOrderStatId()));
				oItemInfo.put("itemSequence", Integer.toString(oFuncCheckItem.getCheckItem().getSeq()));
				oItemInfo.put("itemId", oFuncCheckItem.getCheckItem().getCitmId());
				
				oItemInfo.put("itemCode", oFuncCheckItem.getCheckItem().getCode());
				for(int i=1; i<=5; i++) 
					oItemInfo.put("itemName"+i, oFuncCheckItem.getCheckItem().getName(i));
				oItemInfo.put("itemPriceLevel", Integer.toString(oFuncCheckItem.getCheckItem().getPriceLevel()));
				oItemInfo.put("itemQuantity", oFuncCheckItem.getCheckItem().getQty().toString());
				oItemInfo.put("itemPrice", oFuncCheckItem.getCheckItem().getPrice().toString());
				oItemInfo.put("itemRoundTotal", oFuncCheckItem.getCheckItem().getRoundTotal().toString());
				BigDecimal dItemTaxAmount = BigDecimal.ZERO;
				for(int j=1; j<=25; j++)
					dItemTaxAmount = dItemTaxAmount.add(oFuncCheckItem.getCheckItem().getTax(j));
				oItemInfo.put("itemTaxAmount", dItemTaxAmount.toString());
				BigDecimal dItemDiscountAmount = oFuncCheckItem.getCheckItem().getPreDisc();
				dItemDiscountAmount = dItemDiscountAmount.add(oFuncCheckItem.getCheckItem().getMidDisc());
				dItemDiscountAmount = dItemDiscountAmount.add(oFuncCheckItem.getCheckItem().getPostDisc());
				oItemInfo.put("itemDiscountAmount", dItemDiscountAmount.toString());
				
				oPostingInfo.oItemList.add(oItemInfo);
				oItemInfo = new HashMap<String,String>();
				
			}
		}
		
		PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
		JSONObject oResponseJSONObject = oPosInterfaceConfig.doInventoryAddSales(fromInventoryPostingJSONObject(oPostingInfo));
		
		return handleResponseJSON(oResponseJSONObject);
	}
	
	// do inventory interface void sales posting
	public boolean doVoidSales(String sCheckId) {
		PostingInfo oPostingInfo = new PostingInfo();
		
		initPostingInfo(oPostingInfo);
		oPostingInfo.sCheckId = sCheckId;
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.sShopCode = AppGlobal.g_oFuncOutlet.get().getShopCode();
		PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
		JSONObject oResponseJSONObject = oPosInterfaceConfig.doInventoryVoidSales(oPostingInfo);
		
		return handleResponseJSON(oResponseJSONObject);
	}
	
	// do inventory interface void sales posting
	public boolean doSearchItemStock(String sItemId) {
		PostingInfo oPostingInfo = new PostingInfo();
		
		initPostingInfo(oPostingInfo);
		oPostingInfo.sItemId = sItemId;
		oPostingInfo.sOutletCode = AppGlobal.g_oFuncOutlet.get().getOutletCode();
		oPostingInfo.sShopCode = AppGlobal.g_oFuncOutlet.get().getShopCode();
		
		PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
		JSONObject oResponseJSONObject = oPosInterfaceConfig.doInventorySearchItemStock(oPostingInfo);
		
		return handleResponseJSON(oResponseJSONObject);
	}
	
	
	private JSONObject fromInventoryPostingJSONObject(PostingInfo oPostingInfo) {
		JSONObject oPostingJSONObject = new JSONObject();
		
		try {
			oPostingJSONObject.put("outletCode", oPostingInfo.sOutletCode);
			oPostingJSONObject.put("shopCode", oPostingInfo.sShopCode);
			oPostingJSONObject.put("interfaceId", oPostingInfo.iInterfaceId);
			oPostingJSONObject.put("checkId", oPostingInfo.sCheckId);
			oPostingJSONObject.put("dateTime", oPostingInfo.sDate);
			oPostingJSONObject.put("checkNumber", oPostingInfo.sCheckNumber);
			
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
		}catch (JSONException jsone) {
			jsone.printStackTrace();
			return null;
		}
		
		return oPostingJSONObject;
	}
	
	
	// handle response json
	private boolean handleResponseJSON(JSONObject oResponseJSONObject){
		if(oResponseJSONObject == null) {
			return false;
		}
		else {
			if(oResponseJSONObject.optBoolean("postingResult", false) == false) {
				if(oResponseJSONObject.has("errorCode") && oResponseJSONObject.optInt("errorCode") > 0) {
					m_iLastErrorCode = oResponseJSONObject.optInt("errorCode");
					m_sLastErrorMessage = getErrorMessage(oResponseJSONObject.optInt("errorCode"));
				}else {
					m_iLastErrorCode = 0;
					if(oResponseJSONObject.has("errorMessage") && oResponseJSONObject.optString("errorMessage") != null && !oResponseJSONObject.optString("errorMessage").isEmpty())
						m_sLastErrorMessage = oResponseJSONObject.optString("errorMessage");
					else
						m_sLastErrorMessage = AppGlobal.g_oLang.get()._("invalid_response");
				}
				return false;
			}else {
				//Success
				if(oResponseJSONObject.has("returnResult")){
					oResponseJSONObject = oResponseJSONObject.optJSONObject("returnResult");
					if(oResponseJSONObject.has("Data")){
						JSONObject oDataObject = oResponseJSONObject.optJSONObject("Data");
						if(oDataObject == null) {
							// invalid_response
							m_iLastErrorCode = 8;
							m_sLastErrorMessage = getErrorMessage(m_iLastErrorCode);
							return false;
						}
						// split the data json information
						RecipeData oRecipeData = new RecipeData();
						oRecipeData.dQty = BigDecimal.valueOf(oDataObject.optDouble("Quantity"));
						
						m_oPostingResponse.oRecipeDatalist.add(oRecipeData);
					}
				}
			}
		}
		return true;
	}
	
	// print void payment interface slip
	public void printVoidPostingSlip(String sType, List<PosCheckPayment> oCheckPayments) {
		JSONObject oPrintParams = new JSONObject();
		
		PosInterfaceConfig oInterfaceConfig = new PosInterfaceConfig();
		oInterfaceConfig.printPaymentInterfaceAlertSlip(AppGlobal.g_oFuncStation.get().getCheckPrtqId(), sType, AppGlobal.g_oCurrentLangIndex.get(), oPrintParams);
	}
	
	// Get posting response
	public PostingResponse getPostingResponse(){
		return m_oPostingResponse;
	}
	
	public String getErrorMessage(int iErrorCode) {
		String sErrorMessage = "";
		
		switch (iErrorCode) {
		case 0:
			sErrorMessage = AppGlobal.g_oLang.get()._("missing_params_format");
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
			sErrorMessage = AppGlobal.g_oLang.get()._("incorrect_posting_information");
			break;
		case 5:
			sErrorMessage = AppGlobal.g_oLang.get()._("fail_to_build_connection");
			break;
		case 6:
			sErrorMessage = AppGlobal.g_oLang.get()._("no_response");
			break;
		case 7:
			sErrorMessage = AppGlobal.g_oLang.get()._("empty_response");
			break;
		case 8:
			sErrorMessage = AppGlobal.g_oLang.get()._("invalid_response");
			break;
		case 9:
			sErrorMessage = AppGlobal.g_oLang.get()._("missing_interface_module");
			break;
		case 10:
			sErrorMessage = AppGlobal.g_oLang.get()._("missing_item_inventory_code");
			break;
		case 400:
			sErrorMessage = AppGlobal.g_oLang.get()._("bad_request");
			break;
		case 401:
			sErrorMessage = AppGlobal.g_oLang.get()._("unauthorized");
			break;
		case 404:
			sErrorMessage = AppGlobal.g_oLang.get()._("not_found");
			break;
		case 406:
			sErrorMessage = AppGlobal.g_oLang.get()._("not_acceptable");
			break;
		case 500:
			sErrorMessage = AppGlobal.g_oLang.get()._("internal_server_error");
			break;
		default:
			sErrorMessage = AppGlobal.g_oLang.get()._("error") + "(" + iErrorCode + ")";
			break;
		}
		
		return sErrorMessage;
	}
	
	public int getLastErrorCode() {
		return this.m_iLastErrorCode;
	}
	
	public String getLastErrorMessage() {
		return this.m_sLastErrorMessage;
	}
}