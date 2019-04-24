//Database: pos_tax_sc_types -Tax & S.C. types
package app.model;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosTaxScType {
	private int txscId;
	private String type;
	private int number;
	private String[] name;
	private String[] shortName;
	private BigDecimal rate;
	private String includeTaxScMask;
	private String includePreDisc;
	private String includeMidDisc;
	private String status;
	
	// type
	public static String TYPE_TAX = "t";
	public static String TYPE_SC = "s";
	
	// includePreDisc
	public static String INCLUDE_PRE_DISC_NO = "";
	public static String INCLUDE_PRE_DISC_YES = "y";
	
	// includeMidDisc
	public static String INCLUDE_MID_DISC_NO = "";
	public static String INCLUDE_MID_DISC_YES = "y";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosTaxScType () {
		this.init();
	}
	
	//init object with passing JSONObject
	public PosTaxScType(JSONObject taxScTypeJSONObject) {
		this.readDataFromJson(taxScTypeJSONObject);
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray taxScTypeJSONArray = null;
		
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return null;
		else {
			if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
				return null;
			
			if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("taxScTypes")) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
				return null;
			}
			
			if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("taxScTypes"))
				return null;
			
			taxScTypeJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().optJSONArray("taxScTypes");
		}
		
		return taxScTypeJSONArray;
	}

	//read data from response JSON
	private void readDataFromJson(JSONObject taxScTypeJSONObject) {
		int i=0;
		JSONObject resultTaxScType = null;

		resultTaxScType = taxScTypeJSONObject.optJSONObject("PosUserTimeInOutLog");
		if(resultTaxScType == null)
			resultTaxScType = taxScTypeJSONObject;
			
		this.init();
		
		this.txscId = resultTaxScType.optInt("txsc_id");
		this.type = resultTaxScType.optString("txsc_type");
		this.number = resultTaxScType.optInt("txsc_number");
		for(i=1; i<=5; i++)
			this.name[(i-1)] = resultTaxScType.optString("txsc_name_l"+i);
		for(i=1; i<=5; i++)
			this.shortName[(i-1)] = resultTaxScType.optString("txsc_short_name_l"+i);
		this.rate = new BigDecimal(resultTaxScType.optString("txsc_rate", "0.0"));
		this.includeTaxScMask = resultTaxScType.optString("txsc_include_tax_sc_mask");
		this.includePreDisc = resultTaxScType.optString("txsc_include_pre_disc", PosTaxScType.INCLUDE_PRE_DISC_NO);
		this.includeMidDisc = resultTaxScType.optString("txsc_include_mid_disc", PosTaxScType.INCLUDE_MID_DISC_NO);
		this.status = resultTaxScType.optString("txsc_status", PosTaxScType.STATUS_ACTIVE);
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.txscId = 0;
		this.type = "";
		this.number = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.rate = BigDecimal.ZERO;
		this.includeTaxScMask = "";
		this.includePreDisc = PosTaxScType.INCLUDE_PRE_DISC_NO;
		this.includeMidDisc = PosTaxScType.INCLUDE_MID_DISC_NO;
		this.status = PosTaxScType.STATUS_ACTIVE;
		
	}
	
	//get all taxes
	public JSONArray getAllTaxes() {
		JSONArray taxesJSONArray = null;
		taxesJSONArray = this.readDataListFromApi("gm", "pos", "getAllTaxes", "");
		return taxesJSONArray;
		
	}
	
	//get all service charges
	public JSONArray getAllServiceCharges() {
		JSONArray scJSONArray = null;
		scJSONArray = this.readDataListFromApi("gm", "pos",	"getAllServiceCharges", "");
		return scJSONArray;
		
	}
	
	//get txscId
	protected int getTxscId() {
		return this.txscId;
	}
	
	//get type
	protected String getType() {
		return this.type;
	}
	
	//get number
	protected int getNumber() {
		return this.number;
	}
	
	//get name by lang index
	public String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get short name by lang index
	protected String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get rate
	public BigDecimal getRate() {
		return this.rate;
	}
	
	//get include tax sc mask
	public String getIncludeTaxScMask() {
		return this.includeTaxScMask;
	}
	
	//get include pre disc
	public String getIncludePreDisc() {
		return this.includePreDisc;
	}
	
	//get include mid disc
	public String getIncludeMidDisc() {
		return this.includeMidDisc;
	}
	
	//get status
	protected String getStatus() {
		return this.status;
	}

	//set rate
	public void setRate(BigDecimal dRate) {
		this.rate = dRate;
	}
	
	//set include tax sc mask
	public void setIncludeTaxScMask(String sIncludeTaxScMask) {
		this.includeTaxScMask = sIncludeTaxScMask;
	}
	
	//set include pre disc
	public void setIncludePreDisc(String sIncludePreDisc) {
		this.includePreDisc = sIncludePreDisc;
	}
	
	//set include mid disc
	public void setIncludeMidDisc(String sIncludeMidDisc) {
		this.includeMidDisc = sIncludeMidDisc;
	}
	
	public boolean isIncludePreDisc() {
		return this.includePreDisc.equals(PosTaxScType.INCLUDE_PRE_DISC_YES);
	}
	
	public boolean isIncludeMidDisc() {
		return this.includeMidDisc.equals(PosTaxScType.INCLUDE_MID_DISC_YES);
	}
}
