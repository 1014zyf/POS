//Database: pos_outlet_settings - Outlet settings for POS operation
package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosOutletSetting {
	private int outsId;
	private int shopId;
	private int oletId;
	private String generateCheckNumBy;
	private String checkPrefix;
	private int startCheckNum;
	private int endCheckNum;
	private String resetCheckNum;
	private int priceLevel;
	private int soldoutMenuId;
	private int paidInOutPaymId;
	private String itemRound;
	private String scRound;
	private String taxRound;
	private String discRound;
	private String checkRound;
	private String payRound;
	private String gratuityRound;
	private int itemDecimal;
	private int scDecimal;
	private int taxDecimal;
	private int discDecimal;
	private int checkDecimal;
	private int payDecimal;
	private int gratuityDecimal;
	private int[] checkPfmtId;
	private int[] detailCheckPfmtId;
	private int servingCheckPfmtId;
	private int[] receiptPfmtId;
	private String[] bilingualLangCode;
	private int failoverStgpId;
	private String status;
	
	private PosPrintFormatList printFormatList;
	
	private OutOutlet outOutlet;
	private FailoverStationGroup failoverStationGroup;
	
	// generateCheckNumBy
	private static String GENERATE_CHECK_NUM_BY_OUTLET = "";
	private static String GENERATE_CHECK_NUM_BY_STATION = "g";
	
	// resetCheckNum
	private static String RESET_CHECK_NUM_CARRY = "c";
	private static String RESET_CHECK_NUM_RESET = "";
	
	// itemRound
	private static String ITEM_ROUND_ROUND_OFF = "";
	private static String ITEM_ROUND_ROUND_UP = "1";
	private static String ITEM_ROUND_ROUND_DOWN = "2";
	private static String ITEM_ROUND_TO_NEARESET_5 = "3";
	private static String ITEM_ROUND_UP_TO_NEAREST_5 = "4";
	private static String ITEM_ROUND_DOWN_TO_NEAREST_5 = "5";

	// scRound
	private static String SC_ROUND_ROUND_OFF = "";
	private static String SC_ROUND_ROUND_UP = "1";
	private static String SC_ROUND_ROUND_DOWN = "2";
	private static String SC_ROUND_TO_NEARESET_5 = "3";
	private static String SC_ROUND_UP_TO_NEAREST_5 = "4";
	private static String SC_ROUND_DOWN_TO_NEAREST_5 = "5";

	// taxRound
	private static String TAX_ROUND_ROUND_OFF = "";
	private static String TAX_ROUND_ROUND_UP = "1";
	private static String TAX_ROUND_ROUND_DOWN = "2";
	private static String TAX_ROUND_TO_NEARESET_5 = "3";
	private static String TAX_ROUND_UP_TO_NEAREST_5 = "4";
	private static String TAX_ROUND_DOWN_TO_NEAREST_5 = "5";

	// discRound
	private static String DISC_ROUND_ROUND_OFF = "";
	private static String DISC_ROUND_ROUND_UP = "1";
	private static String DISC_ROUND_ROUND_DOWN = "2";
	private static String DISC_ROUND_TO_NEARESET_5 = "3";
	private static String DISC_ROUND_UP_TO_NEAREST_5 = "4";
	private static String DISC_ROUND_DOWN_TO_NEAREST_5 = "5";

	// checkRound
	private static String CHECK_ROUND_ROUND_OFF = "";
	private static String CHECK_ROUND_ROUND_UP = "1";
	private static String CHECK_ROUND_ROUND_DOWN = "2";
	private static String CHECK_ROUND_TO_NEARESET_5 = "3";
	private static String CHECK_ROUND_UP_TO_NEAREST_5 = "4";
	private static String CHECK_ROUND_DOWN_TO_NEAREST_5 = "5";

	// payRound
	private static String PAY_ROUND_ROUND_OFF = "";
	private static String PAY_ROUND_ROUND_UP = "1";
	private static String PAY_ROUND_ROUND_DOWN = "2";
	private static String PAY_ROUND_TO_NEARESET_5 = "3";
	private static String PAY_ROUND_UP_TO_NEAREST_5 = "4";
	private static String PAY_ROUND_DOWN_TO_NEAREST_5 = "5";

	// gratuityRound
	private static String GRATUITY_ROUND_ROUND_OFF = "";
	private static String GRATUITY_ROUND_ROUND_UP = "1";
	private static String GRATUITY_ROUND_ROUND_DOWN = "2";
	private static String GRATUITY_ROUND_TO_NEARESET_5 = "3";
	private static String GRATUITY_ROUND_UP_TO_NEAREST_5 = "4";
	private static String GRATUITY_ROUND_DOWN_TO_NEAREST_5 = "5";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosOutletSetting () {
		this.init();
	}
	
	public PosOutletSetting (JSONObject oPosOutletSettingJSONObject) {
		this.readDataFromJson(oPosOutletSettingJSONObject);
	}
	
	//read data from POS API
	private boolean readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		boolean bResult = true;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			bResult = false;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return false;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("outlet_setting")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				this.init();
				return false;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("outlet_setting")) {
				this.init();
				return false;
			}
			
			JSONObject tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONObject("outlet_setting");
			if(tempJSONObject.isNull("PosOutletSetting")) {
				this.init();
				return false;
			}
			
			readDataFromJson(tempJSONObject);
		}
		
		return bResult;
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray checkItemJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("outletSettings")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("outletSettings"))
				return null;
			
			checkItemJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("outletSettings");
		}
		
		return checkItemJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject outletSettingJSONObject) {
		JSONObject resultOutletSetting = null;
		int i;
		
		resultOutletSetting = outletSettingJSONObject.optJSONObject("PosOutletSetting");
		if(resultOutletSetting == null)
			resultOutletSetting = outletSettingJSONObject;
		
		this.init();
		
		this.outsId = resultOutletSetting.optInt("outs_id");
		this.shopId = resultOutletSetting.optInt("outs_shop_id");
		this.oletId = resultOutletSetting.optInt("outs_olet_id");
		this.generateCheckNumBy = resultOutletSetting.optString("outs_generate_check_num_by", PosOutletSetting.GENERATE_CHECK_NUM_BY_OUTLET);
		this.checkPrefix = resultOutletSetting.optString("outs_check_prefix");
		this.startCheckNum = resultOutletSetting.optInt("outs_start_check_num");
		this.endCheckNum = resultOutletSetting.optInt("outs_end_check_num");
		this.resetCheckNum = resultOutletSetting.optString("outs_reset_check_num", PosOutletSetting.RESET_CHECK_NUM_RESET);
		this.priceLevel = resultOutletSetting.optInt("outs_price_level");
		this.soldoutMenuId = resultOutletSetting.optInt("outs_soldout_menu_id");
		this.paidInOutPaymId = resultOutletSetting.optInt("outs_paid_io_paym_id");
		this.itemRound = resultOutletSetting.optString("outs_item_round", PosOutletSetting.ITEM_ROUND_ROUND_OFF);
		this.scRound = resultOutletSetting.optString("outs_sc_round", PosOutletSetting.SC_ROUND_ROUND_OFF);
		this.taxRound = resultOutletSetting.optString("outs_tax_round", PosOutletSetting.TAX_ROUND_ROUND_OFF);
		this.discRound = resultOutletSetting.optString("outs_disc_round", PosOutletSetting.DISC_ROUND_ROUND_OFF);
		this.checkRound = resultOutletSetting.optString("outs_check_round", PosOutletSetting.CHECK_ROUND_ROUND_OFF);
		this.payRound = resultOutletSetting.optString("outs_pay_round", PosOutletSetting.PAY_ROUND_ROUND_OFF);
		this.gratuityRound = resultOutletSetting.optString("outs_gratuity_round", PosOutletSetting.GRATUITY_ROUND_ROUND_OFF);
		this.itemDecimal = resultOutletSetting.optInt("outs_item_decimal");
		this.scDecimal = resultOutletSetting.optInt("outs_sc_decimal");
		this.taxDecimal = resultOutletSetting.optInt("outs_tax_decimal");
		this.discDecimal = resultOutletSetting.optInt("outs_disc_decimal");
		this.checkDecimal = resultOutletSetting.optInt("outs_check_decimal");
		this.payDecimal = resultOutletSetting.optInt("outs_pay_decimal");
		this.gratuityDecimal = resultOutletSetting.optInt("outs_gratuity_decimal");
		for(i=1; i<=5; i++)
			this.checkPfmtId[(i-1)] = resultOutletSetting.optInt("outs_check"+i+"_pfmt_id");
		for(i=1; i<=5; i++)
			this.detailCheckPfmtId[(i-1)] = resultOutletSetting.optInt("outs_detail_check"+i+"_pfmt_id");
		this.servingCheckPfmtId = resultOutletSetting.optInt("outs_serving_check_pfmt_id");
		for(i=1; i<=5; i++)
			this.receiptPfmtId[(i-1)] = resultOutletSetting.optInt("outs_receipt"+i+"_pfmt_id");
		for(i=1; i<=5; i++)
			this.bilingualLangCode[(i-1)] = resultOutletSetting.optString("outs_bilingual_lang_code"+i);
		this.failoverStgpId = resultOutletSetting.optInt("outs_failover_stgp_id");
		this.status = resultOutletSetting.optString("outs_status", PosOutletSetting.STATUS_ACTIVE);
		
		//print format exist
		JSONArray oPrintFormat = outletSettingJSONObject.optJSONArray("print_format");
		if(oPrintFormat != null) 
			this.printFormatList.addMultiPrintFormatToList(oPrintFormat);
		
		// outlet
		JSONObject oOutletJSONObject = outletSettingJSONObject.optJSONObject("OutOutlet");
		if(oOutletJSONObject != null) 
			this.outOutlet = new OutOutlet(oOutletJSONObject);
		
		//failover station group
		JSONObject oFailoverstationGroupJSONObject = outletSettingJSONObject.optJSONObject("FailoverStationGroup");
		if(oFailoverstationGroupJSONObject != null) 
			this.failoverStationGroup = new FailoverStationGroup(oFailoverstationGroupJSONObject);
	}
	
	//read data from database by outs_id
	public boolean readByOutletId(int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("outletId", iOutletId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataFromApi("gm", "pos", "getOutletSettingByOutletId", requestJSONObject.toString());
	}
	
	//read data from database by outs_id
	public JSONArray readAllByShopId(int iShopId) {
		JSONObject requestJSONObject = new JSONObject();
		 
		try {
			requestJSONObject.put("shopId", iShopId);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return this.readDataListFromApi("gm", "pos", "getAllOutletSettingsByShopId", requestJSONObject.toString());
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.outsId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.generateCheckNumBy = PosOutletSetting.GENERATE_CHECK_NUM_BY_OUTLET;
		this.checkPrefix = "";
		this.startCheckNum = 0;
		this.endCheckNum = 0;
		this.resetCheckNum = PosOutletSetting.RESET_CHECK_NUM_RESET;
		this.priceLevel = 0;
		this.soldoutMenuId = 0;
		this.paidInOutPaymId = 0;
		this.itemRound = PosOutletSetting.ITEM_ROUND_ROUND_OFF;
		this.scRound = PosOutletSetting.SC_ROUND_ROUND_OFF;
		this.taxRound = PosOutletSetting.TAX_ROUND_ROUND_OFF;
		this.discRound = PosOutletSetting.DISC_ROUND_ROUND_OFF;
		this.checkRound = PosOutletSetting.CHECK_ROUND_ROUND_OFF;
		this.payRound = PosOutletSetting.PAY_ROUND_ROUND_OFF;
		this.gratuityRound = PosOutletSetting.GRATUITY_ROUND_ROUND_OFF;
		this.itemDecimal = 0;
		this.scDecimal = 0;
		this.taxDecimal = 0;
		this.discDecimal = 0;
		this.checkDecimal = 0;
		this.payDecimal = 0;
		this.gratuityDecimal = 0;
		if(this.checkPfmtId == null)
			this.checkPfmtId = new int[5];
		for(i=0; i<5; i++)
			this.checkPfmtId[i] = 0;
		if(this.detailCheckPfmtId == null)
			this.detailCheckPfmtId = new int[5];
		for(i=0; i<5; i++)
			this.detailCheckPfmtId[i] = 0;
		this.servingCheckPfmtId = 0;
		if(this.receiptPfmtId == null)
			this.receiptPfmtId = new int[5];
		for(i=0; i<5; i++)
			this.receiptPfmtId[i] = 0;
		if(this.bilingualLangCode == null)
			this.bilingualLangCode = new String[5];
		for(i=0; i<5; i++)
			this.bilingualLangCode[i] = "";
		this.failoverStgpId = 0;
		this.status = PosOutletSetting.STATUS_ACTIVE;
		
		if(this.printFormatList == null)
			this.printFormatList = new PosPrintFormatList();
		else
			this.printFormatList.clearPrintFormatList();
		
		this.outOutlet = null;
		this.failoverStationGroup = null;
	}
	
	//get outsId
	public int getOutsId() {
		return this.outsId;
	}
	
	//get shop id
	public int getShopId() {
		return this.shopId;
	}
	
	//get olet id
	public int getOletId() {
		return this.oletId;
	}
	
	//get generate check number by
	public String getGenerateCheckNumBy() {
		return this.generateCheckNumBy;
	}
	
	//get check prefix
	public String getCheckPrefix() {
		return this.checkPrefix;
	}
	
	//get start check number
	public int getStartCheckNumber() {
		return this.startCheckNum;
	}
	
	//get end check number
	public int getEndCheckNumber() {
		return this.endCheckNum;
	}
	
	//get reset check number
	public String getResetCheckNum() {
		return this.resetCheckNum;
	}
	
	//get price level
	public int getPriceLevel() {
		return this.priceLevel;
	}
	
	//get soldout menu id
	public int getSoldoutMenuId() {
		return this.soldoutMenuId;
	}
	
	//get paid in/out paym id
	public int getPaidInOutPaymId() {
		return this.paidInOutPaymId;
	}
	
	//get item round
	public String getItemRound() {
		return this.itemRound;
	}
	
	//get sc round
	public String getScRound() {
		return this.scRound;
	}
	
	//get tax round
	public String getTaxRound() {
		return this.taxRound;
	}
	
	//get disc round
	public String getDiscRound() {
		return this.discRound;
	}
	
	//get check round
	public String getCheckRound() {
		return this.checkRound;
	}
	
	//get pay round
	public String getPayRound() {
		return this.payRound;
	}
	
	//get gratuity round
	public String getGratuityRound() {
		return this.gratuityRound;
	}
	
	//get item decimal
	public int getItemDecimal() {
		return this.itemDecimal;
	}
	
	//get sc decimal
	public int getScDecimal() {
		return this.scDecimal;
	}
	
	//get tax decimal
	public int getTaxDecimal() {
		return this.taxDecimal;
	}
	
	//get disc decimal
	public int getDiscDecimal() {
		return this.discDecimal;
	}
	
	//get check decimal
	public int getCheckDecimal() {
		return this.checkDecimal;
	}
	
	//get pay decimal
	public int getPayDecimal() {
		return this.payDecimal;
	}
	
	//get gratuity decimal
	public int getGratuityDecimal() {
		return this.gratuityDecimal;
	}
	
	//get check print format id
	public int getCheckPfmtId(int iIndex) {
		return this.checkPfmtId[(iIndex-1)];
	}
	
	//get detail check print format id
	public int getDetailCheckPfmtId(int iIndex) {
		return this.detailCheckPfmtId[(iIndex-1)];
	}
	
	//get serving check print format id
	public int getServingCheckPfmtId() {
		return this.servingCheckPfmtId;
	}
	
	//get receipt print format id
	public int getReceiptPfmtId(int iIndex) {
		return this.receiptPfmtId[(iIndex-1)];
	}
	
	public String getBilingualLangCode(int iIndex) {
		return this.bilingualLangCode[(iIndex-1)];
	}
	
	public int getFailoverStgpId() {
		return this.failoverStgpId;
	}
	
	//get status
	protected String getStatus() {
		return this.status;
	}
	
	//get print format list
	public String getPrintFormatName(int iPfmtId, int iIndex) {
		PosPrintFormat oPrintFormat = this.printFormatList.getPrintFormatByPfmtId(iPfmtId);
		
		if(oPrintFormat == null)
			return null;
		return oPrintFormat.getName(iIndex);
	}
	
	public OutOutlet getOutlet() {
		return this.outOutlet;
	}
	
	public FailoverStationGroup getFailoverStaionGroup() {
		return this.failoverStationGroup;
	}
	
	public boolean isGenerateCheckNumByStation() {
		return this.generateCheckNumBy.equals(PosOutletSetting.GENERATE_CHECK_NUM_BY_STATION);
	}
	
	public boolean isResetCheckNum() {
		return this.resetCheckNum.equals(PosOutletSetting.RESET_CHECK_NUM_RESET);
	}
}
