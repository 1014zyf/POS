//Database: pos_check_parties - Sales check parties
package app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosCheckParty {
	private int cptyId;
	private int chksId;
	private String name;
	private int seq;
	private String paid;
	private BigDecimal partyTotal;
	private BigDecimal itemTotal;
	private BigDecimal[] sc;
	private BigDecimal[] tax;
	private BigDecimal [] inclTaxRef;
	private BigDecimal preDisc;
	private BigDecimal midDisc;
	private BigDecimal postDisc;
	private BigDecimal roundAmount;
	private int membId;
	private String createTime;
	private DateTime createLocTime;
	private int createUserId;
	private int createStatId;
	private String printTime;
	private DateTime printLocTime;
	private int printUserId;
	private int printStatId;
	private String voidTime;
	private DateTime voidLocTime;
	private int voidUserId;
	private int voidStatId;
	private int voidVdrsId;
	private String status;
	
	private boolean bModified;
	private List<PosCheckItem> checkItems;
	private List<PosCheckPayment> checkPayments;
	private List<PosCheckDiscount> checkDiscountList;
	
	// paid
	public static String PAID_NOT_PAID = "";
	public static String PAID_PARTIAL_PAID = "p";
	
	// status
	public static String STATUS_NORMAL = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosCheckParty () {
		this.init();
	}
	
	// init value
	public void init() {
		int i=0;
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		this.cptyId = 0;
		this.chksId = 0;
		this.name = "";
		this.seq = 0;
		this.paid = null;
		this.partyTotal = BigDecimal.ZERO;
		this.itemTotal = BigDecimal.ZERO;
		this.sc = new BigDecimal[5];
		for(i=1; i<=5; i++)
			this.sc[i-1] = BigDecimal.ZERO;
		this.tax = new BigDecimal[25];
		for(i=1; i<=25; i++)
			this.tax[i-1] = BigDecimal.ZERO;
		if(this.inclTaxRef == null)
			this.inclTaxRef = new BigDecimal[4];
		for (i = 1; i <= 4; i++)
			this.inclTaxRef[i-1] = BigDecimal.ZERO;
		this.preDisc = BigDecimal.ZERO;
		this.midDisc = BigDecimal.ZERO;
		this.postDisc = BigDecimal.ZERO;
		this.roundAmount = BigDecimal.ZERO;
		this.membId = 0;
		this.createLocTime = new DateTime();
		this.createTime = oFmt.print(createLocTime.withZone(DateTimeZone.UTC));
		this.createUserId = 0;
		this.createStatId = 0;
		this.printTime = null;
		this.printLocTime = null;
		this.printUserId = 0;
		this.printStatId = 0;
		this.voidTime = null;
		this.voidLocTime = null;
		this.voidUserId = 0;
		this.voidStatId = 0;
		this.voidVdrsId = 0;
		this.status = "";
		
		this.bModified = false;
		
		if(this.checkItems == null)
			this.checkItems = new ArrayList<PosCheckItem>();
		else
			this.checkItems.clear();
		
		if(this.checkPayments == null)
			this.checkPayments = new ArrayList<PosCheckPayment>();
		else
			this.checkPayments.clear();
		
		if(this.checkDiscountList == null)
			this.checkDiscountList = new ArrayList<PosCheckDiscount>();
		else
			this.checkDiscountList.clear();

	}
	
	public PosCheckParty(PosCheckParty oPosCheckParty) {
		int i=0;
		
		this.init();
		
		this.cptyId = oPosCheckParty.cptyId;
		this.chksId = oPosCheckParty.chksId;
		this.name = oPosCheckParty.name;
		this.seq = oPosCheckParty.seq;
		this.paid = oPosCheckParty.paid;
		this.partyTotal = new BigDecimal(oPosCheckParty.partyTotal.toPlainString());
		this.itemTotal = new BigDecimal(oPosCheckParty.itemTotal.toPlainString());
		for(i=1; i<=5; i++)
			this.sc[i-1] = new BigDecimal(oPosCheckParty.sc[i-1].toPlainString());
		for(i=1; i<=25; i++)
			this.tax[i-1] = new BigDecimal(oPosCheckParty.tax[i-1].toPlainString());
		for (i = 1; i <= 4; i++)
			this.inclTaxRef[i-1] = new BigDecimal(oPosCheckParty.inclTaxRef[i-1].toPlainString());
		this.preDisc = new BigDecimal(oPosCheckParty.preDisc.toPlainString());
		this.midDisc = new BigDecimal(oPosCheckParty.midDisc.toPlainString());
		this.postDisc = new BigDecimal(oPosCheckParty.postDisc.toPlainString());
		this.roundAmount = new BigDecimal(oPosCheckParty.roundAmount.toPlainString());
		this.membId = oPosCheckParty.membId;
		
		if (oPosCheckParty.createLocTime != null)
			this.createLocTime = new DateTime(oPosCheckParty.createLocTime);
		if (oPosCheckParty.createTime != null)
			this.createTime = oPosCheckParty.createTime;
		this.createUserId = oPosCheckParty.createUserId;
		this.createStatId = oPosCheckParty.createStatId;
		
		if (oPosCheckParty.printLocTime != null)
			this.printLocTime = new DateTime(oPosCheckParty.printLocTime);
		if (oPosCheckParty.printTime != null)
			this.printTime = oPosCheckParty.printTime;
		this.printUserId = oPosCheckParty.printUserId;
		this.printStatId = oPosCheckParty.printStatId;
		
		if (oPosCheckParty.voidLocTime != null)
			this.voidLocTime = new DateTime(oPosCheckParty.voidLocTime);
		if (oPosCheckParty.voidTime != null)
			this.voidTime = oPosCheckParty.voidTime;
		this.voidUserId = oPosCheckParty.voidUserId;
		this.voidStatId = oPosCheckParty.voidStatId;
		this.voidVdrsId = oPosCheckParty.voidVdrsId;
		
		this.status = oPosCheckParty.status;

		this.bModified = false;
		
		for(int j = 0; j < oPosCheckParty.getCheckItemList().size(); j++) {
			PosCheckItem tempCheckItem = oPosCheckParty.getCheckItemList().get(j);
			this.getCheckItemList().add(new PosCheckItem(tempCheckItem));
		}
		
		for(int j = 0; j < oPosCheckParty.getCheckDiscountList().size(); j++) {
			PosCheckDiscount tempCheckDiscount = oPosCheckParty.getCheckDiscountList().get(j);
			this.getCheckDiscountList().add(new PosCheckDiscount(tempCheckDiscount));
		}
		
		for(int j = 0; j < oPosCheckParty.getCheckPaymentList().size(); j++) {
			PosCheckPayment tempCheckPayment = oPosCheckParty.getCheckPaymentList().get(j);
			this.getCheckPaymentList().add(new PosCheckPayment(tempCheckPayment));
		}
	}
	
	//init object form JSO
	public PosCheckParty(JSONObject checkPartyJSONObject) {
		readDataFromJson(checkPartyJSONObject);
	}
	
	//read data from response JSON
	private void readDataFromJson(JSONObject checkPartyJSONObject) {
		JSONObject resultCheckParty = null;
		JSONArray tempJSONArray = null;
		PosCheckItem oCheckItem = null;
		PosCheckPayment oCheckPayment = null;
		PosCheckDiscount oCheckDiscount = null;
		int i;
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		resultCheckParty = checkPartyJSONObject.optJSONObject("PosCheckParty");
		if(resultCheckParty == null)
			resultCheckParty = checkPartyJSONObject;

		this.init();
		this.cptyId = resultCheckParty.optInt("cpty_id");
		this.chksId = resultCheckParty.optInt("cpty_chks_id");
		this.name = resultCheckParty.optString("cpty_name");
		this.seq = resultCheckParty.optInt("cpty_seq");
		this.paid = resultCheckParty.optString("cpty_paid", PosCheckParty.PAID_NOT_PAID);
		this.partyTotal = new BigDecimal(resultCheckParty.optString("cpty_party_total", "0.0"));
		this.itemTotal = new BigDecimal(resultCheckParty.optString("cpty_item_total", "0.0"));
		for (i = 1; i <= 5; i++) {
			this.sc[i-1] = new BigDecimal(resultCheckParty.optString("cpty_sc"+i, "0.0"));
		}
		for (i = 1; i <= 25; i++) {
			this.tax[i-1] = new BigDecimal(resultCheckParty.optString("cpty_tax"+i, "0.0"));
		}
		for (i = 1; i <= 4; i++) {
			this.inclTaxRef[i-1] = new BigDecimal(resultCheckParty.optString("cpty_incl_tax_ref"+i, "0.0"));
		}
		this.preDisc = new BigDecimal(resultCheckParty.optString("cpty_pre_disc", "0.0"));
		this.midDisc = new BigDecimal(resultCheckParty.optString("cpty_mid_disc", "0.0"));
		this.postDisc = new BigDecimal(resultCheckParty.optString("cpty_post_disc", "0.0"));
		this.roundAmount = new BigDecimal(resultCheckParty.optString("cpty_round_amount", "0.0"));
		this.membId = resultCheckParty.optInt("cpty_memb_id");
		
		String sCreateLocTime = resultCheckParty.optString("cpty_create_loctime");
		if(!sCreateLocTime.isEmpty())
			this.createLocTime = oFmt.parseDateTime(sCreateLocTime);

		this.createTime = resultCheckParty.optString("cpty_create_time", oFmt.print(createLocTime.withZone(DateTimeZone.UTC)));
		
		this.createUserId = resultCheckParty.optInt("cpty_create_user_id");
		this.createStatId = resultCheckParty.optInt("cpty_create_stat_id");
		
		this.printTime = resultCheckParty.optString("cpty_print_time", null);
		String sPrintLocTime = resultCheckParty.optString("cpty_print_loctime");
		if(!sPrintLocTime.isEmpty())
			this.printLocTime = oFmt.parseDateTime(sPrintLocTime);
		
		this.printUserId = resultCheckParty.optInt("cpty_print_user_id");
		this.printStatId = resultCheckParty.optInt("cpty_print_stat_id");
		
		this.voidTime = resultCheckParty.optString("cpty_void_time", null);
		String sVoidLocTime = resultCheckParty.optString("cpty_void_loctime");
		if(!sVoidLocTime.isEmpty())
			this.voidLocTime = oFmt.parseDateTime(sVoidLocTime);
		
		this.voidUserId = resultCheckParty.optInt("cpty_void_user_id");
		this.voidStatId = resultCheckParty.optInt("cpty_void_stat_id");
		this.voidVdrsId = resultCheckParty.optInt("cpty_void_vdrs_id");
		this.status = resultCheckParty.optString("cpty_status", PosCheckParty.STATUS_NORMAL);
		
		//check whether pos check item record exist
		tempJSONArray = checkPartyJSONObject.optJSONArray("PosCheckItem");
		if (tempJSONArray != null) {
			for(i=0; i<tempJSONArray.length(); i++) {
				JSONObject oCheckItemJSONObject = tempJSONArray.optJSONObject(i);
				if(oCheckItemJSONObject != null) {
					oCheckItem = new PosCheckItem(oCheckItemJSONObject);
					this.checkItems.add(oCheckItem);
				}
			}
		}
		
		//check whether pos check discount record exist
		tempJSONArray = checkPartyJSONObject.optJSONArray("PosCheckDiscount");
		if(tempJSONArray != null) {
			for(i=0; i<tempJSONArray.length(); i++) {
				JSONObject oCheckDiscountJSONObject = tempJSONArray.optJSONObject(i);
				if(oCheckDiscountJSONObject != null) {
					oCheckDiscount = new PosCheckDiscount(oCheckDiscountJSONObject);
					this.checkDiscountList.add(oCheckDiscount);
				}
			}
		}
		
		//check whether pos check payment
		tempJSONArray = checkPartyJSONObject.optJSONArray("PosCheckPayment");
		if (tempJSONArray != null) {
			for(i=0; i<tempJSONArray.length(); i++) {
				JSONObject oCheckPaymentJSONObject = tempJSONArray.optJSONObject(i);
				if(oCheckPaymentJSONObject != null) {
					oCheckPayment = new PosCheckPayment(oCheckPaymentJSONObject);
					this.checkPayments.add(oCheckPayment);
				}
			}
		}
	}
	
	//construct the save request JSON
	// *** iSendMode:	0 - Send new items only
	//					1 - Send old items only
	//					2 - Send both new and old items
	protected JSONObject constructAddSaveJSON(boolean bUpdate, int iSendMode) {
		int i = 0;
		String sTempJSONName = "";
		JSONObject addSaveJSONObject = new JSONObject();
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		try {
			if (bUpdate)
				addSaveJSONObject.put("cpty_id", this.cptyId);
			addSaveJSONObject.put("cpty_chks_id", this.chksId);
			if(!this.name.isEmpty())
				addSaveJSONObject.put("cpty_name", this.name);
			addSaveJSONObject.put("cpty_seq", this.seq);
			addSaveJSONObject.put("cpty_paid", this.paid);
			addSaveJSONObject.put("cpty_party_total", this.partyTotal);
			addSaveJSONObject.put("cpty_item_total", this.itemTotal);
			for(i=1; i<=5; i++) {
				sTempJSONName = "cpty_sc"+Integer.toString((i));
				addSaveJSONObject.put(sTempJSONName, this.sc[i-1]);
			}
			for(i=1; i<=25; i++) {
				sTempJSONName = "cpty_tax"+Integer.toString((i));
				addSaveJSONObject.put(sTempJSONName, this.tax[i-1]);
			}
			for(i=1; i<=4; i++) {
				sTempJSONName = "cpty_incl_tax_ref"+Integer.toString((i));
				addSaveJSONObject.put(sTempJSONName, this.inclTaxRef[i-1]);
			}
			addSaveJSONObject.put("cpty_pre_disc", this.preDisc);
			addSaveJSONObject.put("cpty_mid_disc", this.midDisc);
			addSaveJSONObject.put("cpty_post_disc", this.postDisc);
			addSaveJSONObject.put("cpty_round_amount", this.roundAmount);
			addSaveJSONObject.put("cpty_memb_id", this.membId);
			if (this.createLocTime != null) {
				addSaveJSONObject.put("cpty_create_time", this.createTime);
				addSaveJSONObject.put("cpty_create_loctime", this.createLocTime.toString(oFormatter));
			}
			
			addSaveJSONObject.put("cpty_create_user_id", this.createUserId);
			addSaveJSONObject.put("cpty_create_stat_id", this.createStatId);
			
			if (this.printLocTime != null) {
				addSaveJSONObject.put("cpty_print_time", this.printTime);
				addSaveJSONObject.put("cpty_print_loctime", this.printLocTime.toString(oFormatter));
			}
			
			addSaveJSONObject.put("cpty_print_user_id", this.printUserId);
			addSaveJSONObject.put("cpty_print_stat_id", this.printStatId);
			
			if (this.voidLocTime != null) {
				addSaveJSONObject.put("cpty_void_time", this.voidTime);
				addSaveJSONObject.put("cpty_void_loctime", this.voidLocTime.toString(oFormatter));
			}
			
			if(this.voidUserId > 0)
				addSaveJSONObject.put("cpty_void_user_id", this.voidUserId);
			if(this.voidStatId > 0)
				addSaveJSONObject.put("cpty_void_stat_id", this.voidStatId);
			if(this.voidVdrsId > 0)
				addSaveJSONObject.put("cpty_void_vdrs_id", this.voidVdrsId);
			
			addSaveJSONObject.putOnce("cpty_status", this.status);
			
			//construct the item list if exist
			if(this.checkItems != null && !this.checkItems.isEmpty()) {
				PosCheckItem oCheckItem = new PosCheckItem();
				addSaveJSONObject.put("items", oCheckItem.constructMultipleItemAddSaveJSON(this.checkItems, iSendMode, false));
			}
			
			//construct the party check discount list if exist
			if(this.checkDiscountList != null && !this.checkDiscountList.isEmpty()) {
				JSONArray oCheckDiscountJSONArray = new JSONArray();
				for(PosCheckDiscount oCheckDiscount: this.checkDiscountList) {
					if(oCheckDiscount.getCdisId() > 0)
						oCheckDiscountJSONArray.put(oCheckDiscount.constructAddSaveJSON(true));
					else
						oCheckDiscountJSONArray.put(oCheckDiscount.constructAddSaveJSON(false));
				}
				addSaveJSONObject.put("checkDiscounts", oCheckDiscountJSONArray);
			}
			
			this.bModified = false;
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return addSaveJSONObject;
	}
	
	//get cptyId
	public int getCptyId() {
		return this.cptyId;
	}
	
	protected int getChksId() {
		return this.chksId;
	}
	
	protected String getName() {
		return this.name;
	}
	
	protected int getSeq() {
		return this.seq;
	}
	
	protected String getPaid() {
		return this.paid;
	}
	
	public BigDecimal getPartyTotal() {
		return this.partyTotal;
	}
	
	public BigDecimal getItemTotal() {
		return this.itemTotal;
	}
	
	public BigDecimal getSc(int iIndex) {
		return this.sc[(iIndex-1)];
	}
	
	public BigDecimal getTax(int iIndex) {
		return this.tax[(iIndex-1)];
	}
	
	public BigDecimal getInclusiveTaxRef(int iIndex) {
		return this.inclTaxRef[(iIndex-1)];
	}
	
	public BigDecimal getPreDisc() {
		return this.preDisc;
	}
	
	public BigDecimal getMidDisc() {
		return this.midDisc;
	}
	
	public BigDecimal getPostDisc() {
		return this.postDisc;
	}
	
	public BigDecimal getRoundAmount() {
		return this.roundAmount;
	}
	
	protected String getCreateTime() {
		return this.createTime;
	}
	
	protected DateTime getCreateLocTime() {
		return this.createLocTime;
	}
	
	protected int getCreateUserId() {
		return this.createUserId;
	}
	
	protected int getCreateStatId() {
		return this.createStatId;
	}
	
	protected String getPrintTime() {
		return this.printTime;
	}
	
	protected DateTime getPrintLocTime() {
		return this.printLocTime;
	}
	
	protected int getPrintUserId() {
		return this.printUserId;
	}
	
	protected int getPrintStatId() {
		return this.printStatId;
	}
	
	protected String getVoidTime() {
		return this.voidTime;
	}
	
	protected DateTime getVoidLocTime() {
		return this.voidLocTime;
	}
	
	protected int getVoidUserId() {
		return this.voidUserId;
	}
	
	protected int getVoidStatId() {
		return this.voidStatId;
	}
	
	protected int getVoidVdrsId() {
		return this.voidVdrsId;
	}
	
	protected String getStatus() {
		return this.status;
	}
	
	//get check item array list 
	public List<PosCheckItem> getCheckItemList() {
		return this.checkItems;
	}

	//get check payment array list
	public List<PosCheckPayment> getCheckPaymentList() {
		return this.checkPayments;
	}
	
	//add check discount to list
	public void addCheckDiscountList(PosCheckDiscount oPosCheckDiscount) {
		this.checkDiscountList.add(oPosCheckDiscount);
	}
	
	//get check discount array list
	public List<PosCheckDiscount> getCheckDiscountList() {
		return this.checkDiscountList;
	}
	
	//set id
	public void setPartyId(int iCptyId) {
		this.cptyId = iCptyId;
	}
	
	//set check id
	public void setCheckId(int iChksId) {
		this.chksId = iChksId;
	}
	
	//set seq
	public void setSeq(int iSeq) {
		this.seq = iSeq;
	}
	
	//set party total
	public void setPartyTotal(BigDecimal dPartyTotal) {
		this.partyTotal = dPartyTotal;
	}
	
	//set item total
	public void setItemTotal(BigDecimal dItemTotal) {
		this.itemTotal = dItemTotal;
		this.bModified = true;
	}
	
	//set sc
	public void setSc(int iIndex, BigDecimal dScAmount) {
		this.sc[(iIndex-1)] = dScAmount;
		this.bModified = true;
	}
	
	//set tax
	public void setTax(int iIndex, BigDecimal dTaxAmount) {
		this.tax[(iIndex-1)] = dTaxAmount;
		this.bModified = true;
	}
	
	//set inclusive tax reference
	public void setInclusiveTaxRef(int iIndex, BigDecimal dInclusiveTaxRef) {
		this.inclTaxRef[(iIndex-1)] = dInclusiveTaxRef;
		this.bModified = true;
	}
	
	//set pre-discount
	public void setPreDisc(BigDecimal dPreDisc) {
		this.preDisc = dPreDisc;
		this.bModified = true;
	}
	
	//set mid-discount
	public void setMidDisc(BigDecimal dMidDisc) {
		this.midDisc = dMidDisc;
		this.bModified = true;
	}
	
	//set post-discount
	public void setPostDisc(BigDecimal dPostDisc) {
		this.postDisc = dPostDisc;
		this.bModified = true;
	}
	
	//set round amount
	public void setRoundAmount(BigDecimal dRoundAmt) {
		this.roundAmount = dRoundAmt;
		this.bModified = true;
	}
	
	//set item list
	public void setCheckItemList(List<PosCheckItem> oItemList) {
		this.checkItems = oItemList;
	}
	
	//set party check discount
	public void setCheckDiscountList(List<PosCheckDiscount> oCheckDiscountList) {
		this.checkDiscountList = oCheckDiscountList;
	}
	
	//set void time
	public void setVoidTime(String sVoidTime) {
		this.voidTime = sVoidTime;
	}
	
	//set void local time
	public void setVoidLocalTime(DateTime oVoidLocalTime) {
		this.voidLocTime = oVoidLocalTime;
	}
	
	//set void user id
	public void setVoidUserId(int iUserId) {
		this.voidUserId = iUserId;
	}
	
	//set void station id
	public void setVoidStationId(int iStationId) {
		this.voidStatId = iStationId;
	}
	
	//set void reason id
	public void setVoidReasonId(int iVoidCodeId) {
		this.voidVdrsId = iVoidCodeId;
	}
	
	public void setStatus(String sStatus) {
		this.status = sStatus;
	}
	
	//clear check item array list
	public void clearCheckItemList() {
		this.checkItems.clear();
	}
	
	//clear check discount array list
	public void clearCheckDiscountList() {
		this.checkDiscountList.clear();
	}
	
	//clear check payment array list
	public void clearCheckPaymentList() {
		this.checkPayments.clear();
	}
	
	public boolean isNotPaid() {
		return this.paid.equals(PosCheckParty.PAID_NOT_PAID);
	}
}
