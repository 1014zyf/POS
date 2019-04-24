package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import om.*;

public class FuncCheckParty {
	private static int MAX_SEATS=99;
	
	// Check party
	private PosCheckParty m_oCheckParty;
	
	// Check discount list
	private List<PosCheckDiscount> m_oCheckDiscountList;
	
	// Check payment list
	private ArrayList<PosCheckPayment> m_oCheckPaymentList;
	
	// Item list
	private List<List<FuncCheckItem>> m_oItemListPerSeat;
	
	// Check party's incl sc ref values
	private BigDecimal[] m_dInclScRef;
	
	// Check party's incl tax ref values
	private BigDecimal[] m_dInclTaxRef;
	
	// Check party's discount on sc / tax
	private HashMap<String, BigDecimal[]> m_oDiscOnSc;
	private HashMap<String, BigDecimal[]> m_oDiscOnTax;
	
	// Last error message
	private String m_sErrorMessage;
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage;
	}
	
	public FuncCheckParty(){
		m_oCheckParty = new PosCheckParty();
		
		// Discount list
		m_oCheckDiscountList = new ArrayList<PosCheckDiscount>();
		// Payment list
		m_oCheckPaymentList = new ArrayList<PosCheckPayment>();
		
		// Item list for each seat
		m_oItemListPerSeat = new ArrayList<List<FuncCheckItem>>();
		// Create the max seat number item lists (e.g. Shared, 1, 2, ...)
		for(int i = 0; i <= MAX_SEATS; i++) {
			m_oItemListPerSeat.add(new ArrayList<FuncCheckItem>());
		}
		
		// Inclusive SC reference value
		m_dInclScRef = new BigDecimal[5];
		for(int i=1; i<=5; i++)
			m_dInclScRef[i-1] = BigDecimal.ZERO;
		
		// Inclusive TAX reference value
		m_dInclTaxRef = new BigDecimal[25];
		for(int i=1; i<=25; i++)
			m_dInclTaxRef[i-1] = BigDecimal.ZERO;
		
		// Discount on SC / Tax value
		m_oDiscOnSc = new HashMap<String, BigDecimal[]>();
		BigDecimal[] dDiscOnSc = new BigDecimal[AppGlobal.SC_COUNT];
		for(int i=1; i<=AppGlobal.SC_COUNT; i++)
			dDiscOnSc[i-1] = BigDecimal.ZERO;
		m_oDiscOnSc.put("post", dDiscOnSc);
		m_oDiscOnTax = new HashMap<String, BigDecimal[]>();
		BigDecimal[] dDiscOnTax = new BigDecimal[AppGlobal.TAX_COUNT];
		for(int i=1; i<=AppGlobal.TAX_COUNT; i++)
			dDiscOnTax[i-1] = BigDecimal.ZERO;
		m_oDiscOnTax.put("post", dDiscOnTax);
		
		m_sErrorMessage = "";
	}
	
	// Create FuncCheckItem from PosCheckItem loading from database
	public FuncCheckParty(PosCheckParty oCheckParty, List<PosCheckTaxScRef> oTaxScRefs) {
		int i = 0;
		
		m_oCheckParty = new PosCheckParty(oCheckParty);
		
		// Discount list
		m_oCheckDiscountList = new ArrayList<PosCheckDiscount>();
		// Payment list
		m_oCheckPaymentList = new ArrayList<PosCheckPayment>();
		
		// Item list for each seat
		m_oItemListPerSeat = new ArrayList<List<FuncCheckItem>>();
		// Create the max seat number item lists (e.g. Shared, 1, 2, ...)
		for(i = 0; i <= MAX_SEATS; i++) {
			m_oItemListPerSeat.add(new ArrayList<FuncCheckItem>());
		}
		
		// Assign inclusive TAX / SC reference
		m_dInclScRef = new BigDecimal[5];
		m_oDiscOnSc = new HashMap<String, BigDecimal[]>();
		BigDecimal[] dDiscOnSc = new BigDecimal[AppGlobal.SC_COUNT];
		
		m_dInclTaxRef = new BigDecimal[25];
		m_oDiscOnTax = new HashMap<String, BigDecimal[]>();
		BigDecimal[] dDiscOnTax = new BigDecimal[AppGlobal.TAX_COUNT];
		
		for(i=1; i<=AppGlobal.SC_COUNT; i++) {
			String sScInclTaxKey = String.format("incl_sc_ref%d", i);
			String sDiscOnScKey = String.format("round_post_disc_on_sc_ref%d", i);
			dDiscOnSc[i-1] = BigDecimal.ZERO;
			for(PosCheckTaxScRef oTaxScRef : oTaxScRefs) {
				if(oTaxScRef.getVariable().equals(sScInclTaxKey)) {
					try{
						m_dInclScRef[i-1] = new BigDecimal(oTaxScRef.getValue());
					}catch(Exception e){}
				}else if(oTaxScRef.getVariable().equals(sDiscOnScKey)) {
					try{
						dDiscOnSc[i-1] = new BigDecimal(oTaxScRef.getValue());
					}catch(Exception e){}
				}
			}
		}
		m_oDiscOnSc.put("post", dDiscOnSc);
		for(i=1; i<=AppGlobal.TAX_COUNT; i++) {
			String sTaxInclScKey = String.format("incl_tax_ref%d", i);
			String sDiscOnTaxKey = String.format("round_post_disc_on_tax_ref%d", i);
			dDiscOnTax[i-1] = BigDecimal.ZERO;
			for(PosCheckTaxScRef oTaxScRef : oTaxScRefs) {
				if(oTaxScRef.getVariable().equals(sTaxInclScKey)) {
					try{
						m_dInclTaxRef[i-1] = new BigDecimal(oTaxScRef.getValue());
					}catch(Exception e){}
				}else if(oTaxScRef.getVariable().equals(sDiscOnTaxKey)) {
					try{
						dDiscOnTax[i-1] = new BigDecimal(oTaxScRef.getValue());
					}catch(Exception e){}
				}
			}
		}
		m_oDiscOnTax.put("post", dDiscOnTax);
		
		// Item list
		this.addOldItemToItemList();
		
		// Discount list
		this.addOldCheckDiscountToList();
	}
	
	public void updateCheckItemSequence(boolean bAllSection, int iSection) {
		int iCurrentItemSeq = 0;
		
		if(bAllSection) {
			for(List<FuncCheckItem> oItemListForSingleSeat:m_oItemListPerSeat) {
				iCurrentItemSeq = 0;
				for(FuncCheckItem oFuncCheckItem:oItemListForSingleSeat) {
					if(oFuncCheckItem.getCheckItem().isDeleted())
						continue;
					
					iCurrentItemSeq++;
					if(oFuncCheckItem.getCheckItem().getSeq() == iCurrentItemSeq)
						continue;
					
					oFuncCheckItem.getCheckItem().setSeq(iCurrentItemSeq);
					oFuncCheckItem.getCheckItem().setModified(true);
				}
			}
		}
		else if(iSection <= MAX_SEATS) {
			iCurrentItemSeq = 0;
			for(FuncCheckItem oFuncCheckItem:m_oItemListPerSeat.get(iSection)) {
				if(oFuncCheckItem.getCheckItem().isDeleted())
					continue;
				
				iCurrentItemSeq++;
				if(oFuncCheckItem.getCheckItem().getSeq() == iCurrentItemSeq)
					continue;
				oFuncCheckItem.getCheckItem().setSeq(iCurrentItemSeq);
				oFuncCheckItem.getCheckItem().setModified(true);
			}
		}
	}
	
	public void updateCheckDiscountSequence() {
		int iCurrentItemSeq = 0;
		
		for(PosCheckDiscount oPosCheckDiscount: m_oCheckDiscountList) {
			if(oPosCheckDiscount.isDeleted())
				continue;
			
			iCurrentItemSeq++;
			if(oPosCheckDiscount.getSeq() == iCurrentItemSeq)
				continue;
			
			int iOriginalSeq = oPosCheckDiscount.getSeq();
			oPosCheckDiscount.setSeq(iCurrentItemSeq);
			for(FuncCheckItem oFuncCheckItem: this.getAppliedCheckDiscountItemList(iOriginalSeq)) {
				oFuncCheckItem.getCheckDiscountItemList(iOriginalSeq).setCdisSeq(iCurrentItemSeq);
				oFuncCheckItem.getCheckItem().setModified(true);
			}
			oPosCheckDiscount.setModified(true);
		}
	}
	
	//form PosCheckItem link list
	// *** iSendMode:	0 - Send new items only
	//					1 - Send old items only
	//					2 - Send both new and old items
	public PosCheckParty constructPosCheckPartyForSaving(int iSendMode) {
		//form the check item list
		List<PosCheckItem> oCheckItemList = new ArrayList<PosCheckItem>();
		
		for(List<FuncCheckItem> oItemListForSingleSeat:m_oItemListPerSeat){
			for(FuncCheckItem oFuncCheckItem:oItemListForSingleSeat) {
				
				// For new set menu child item, the item is updated by the child list of the parent item, skip to be included in this update list
				// For old set menu child item, the item is independent from the parent item and should be included in this update list
				// No need to send new item when iSendMode = 1; no need to send old item when iSendMode = 0
				if((iSendMode != 0 && oFuncCheckItem.isOldItem())
						|| (iSendMode != 1 && oFuncCheckItem.isSetMenuItem() == false)) {
					// Build the PosCheckItem object for saving (including child item list, modifier list, item discount list, extra info list)
					oFuncCheckItem.constructPosCheckItemForSaving();
					
					oCheckItemList.add(oFuncCheckItem.getCheckItem());
				}

			}
		}
		m_oCheckParty.setCheckItemList(oCheckItemList);
		
		//form the party check discount list
		List<PosCheckDiscount> oPartyCheckDiscountList = new ArrayList<PosCheckDiscount>();
		for(PosCheckDiscount oPosCheckDiscount: this.m_oCheckDiscountList) {
			oPartyCheckDiscountList.add(oPosCheckDiscount);
		}
		m_oCheckParty.setCheckDiscountList(oPartyCheckDiscountList);
		
		return m_oCheckParty;
	}
	
	// Update several information (e.g. outlet id, shop id, ...) to check item records
	public void updateMultipleCheckItemInfoForNewItem(String sBusinessDayId, String sBusinessPeriodId, String sChksId, int iShopId, int iOutletId, String sOrderTime, DateTime oOrderDateTime, int iAppliedUserId) {
		// Set new item period ID
		for(List<FuncCheckItem> oItemListForSingleSeat:m_oItemListPerSeat) {
			for(FuncCheckItem oFuncCheckItem:oItemListForSingleSeat) {
				if(!oFuncCheckItem.isOldItem()) {
					oFuncCheckItem.updateMultipleCheckItemInfoForNewItem(sBusinessDayId, sBusinessPeriodId, sChksId, iShopId, iOutletId, sOrderTime, oOrderDateTime, iAppliedUserId);
				}
			}
		}
	}
	
	// Add old item to item list
	public void addOldItemToItemList() {
		int iSeatNo = 0, iLineNo = 0;
		List<Integer> missingCheckItemArrayList = new ArrayList<Integer>();
		List<PosCheckItem> checkItemArrayList = m_oCheckParty.getCheckItemList();

		//***************************
		//	check sequence number
		//	make sure current item's sequence number do not duplicate sequence number of items in m_oItemListPerSeat
		//***************************
		HashMap<Integer, List<FuncCheckItem>> oSeatCheckItemList = new HashMap<Integer, List<FuncCheckItem>>();
		// Classify items to its related seat item list
		for (int i = 0; i < checkItemArrayList.size(); i++) {
			FuncCheckItem oFuncCheckItem = new FuncCheckItem(checkItemArrayList.get(i));
			iSeatNo = checkItemArrayList.get(i).getSeatNo();
			
			List<FuncCheckItem> oCheckItemList;
			if (!oSeatCheckItemList.containsKey(iSeatNo)) {
				oCheckItemList = new ArrayList<FuncCheckItem>();
				oSeatCheckItemList.put(iSeatNo, oCheckItemList);
			}
			
			oCheckItemList = oSeatCheckItemList.get(iSeatNo);
			oCheckItemList.add(oFuncCheckItem);
			
			if(oFuncCheckItem.hasChildItem()) {
				for(FuncCheckItem oChildCheckItem: oFuncCheckItem.getChildItemList()){
					int iChildSeatNo = oChildCheckItem.getCheckItem().getSeatNo();
					if (!oSeatCheckItemList.containsKey(iChildSeatNo)) {
						oCheckItemList = new ArrayList<FuncCheckItem>();
						oSeatCheckItemList.put(iChildSeatNo, oCheckItemList);
					}
					
					oCheckItemList = oSeatCheckItemList.get(iChildSeatNo);
					oCheckItemList.add(oChildCheckItem);
				}
			}
		}
		
		// Sort item's sequence number and assign correct sequence number
		for (List<FuncCheckItem> oCheckItemList: oSeatCheckItemList.values()) {
			// Insertion Sort
			for (int i = 1; i < oCheckItemList.size(); i++) {
				FuncCheckItem oTempCheckItem = oCheckItemList.get(i);
				int iSeq = oTempCheckItem.getCheckItem().getSeq();
				
				int j;
				for (j = i - 1; j >= 0 && iSeq < oCheckItemList.get(j).getCheckItem().getSeq(); j--) {
					oCheckItemList.set(j+1, oCheckItemList.get(j));
				}
				
				oCheckItemList.set(j+1, oTempCheckItem);
			}

			// Assign correct sequence number, in continuous number
			for (int i = 0; i < oCheckItemList.size(); i++) {
				PosCheckItem oCheckItem = oCheckItemList.get(i).getCheckItem();
				if (i+1 != oCheckItem.getSeq()) {
					AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", "Incorrect sequence number: "+oCheckItem.getSeq()+" => change to: "+(i+1)+" [citm_id="+oCheckItem.getCitmId()+", chks_id="+oCheckItem.getCheckId()+"]");
					
					oCheckItem.setSeq(i+1);
					oCheckItem.setModified(true);
				}
			}
		}
		
		// Add item to item list
		for (List<FuncCheckItem> oCheckItemList: oSeatCheckItemList.values()) {
			for(int i = 0; i < oCheckItemList.size(); i++) {
				FuncCheckItem oFuncCheckItem = oCheckItemList.get(i);
				iSeatNo = oFuncCheckItem.getCheckItem().getSeatNo();
				iLineNo = oFuncCheckItem.getCheckItem().getSeq();
			
				this.addItemToItemList(iSeatNo, iLineNo, oFuncCheckItem);
			}
		}
		
		for(List<FuncCheckItem> oItemListForSingleSeat:m_oItemListPerSeat) {
			int i = 0;
			missingCheckItemArrayList.clear();
			for(FuncCheckItem oCheckItem:oItemListForSingleSeat){
				if(oCheckItem == null){
					// Missing item in this seq
					missingCheckItemArrayList.add(0, i);
				}
				i++;
			}
			
			for(int iIndex:missingCheckItemArrayList)
				oItemListForSingleSeat.remove(iIndex);
		}
		
		m_oCheckParty.clearCheckItemList();
	}
	
	// Add new check discount to list
	public int addNewCheckDiscountToList(PosCheckDiscount oCheckDiscount) {
		m_oCheckDiscountList.add(oCheckDiscount);
		return m_oCheckDiscountList.size();
	}
	
	// Add old check discount to list
	public void addOldCheckDiscountToList() {
		List<PosCheckDiscount> oCheckDiscList = m_oCheckParty.getCheckDiscountList();
		
		for(PosCheckDiscount oCheckDiscount:oCheckDiscList) {
			m_oCheckDiscountList.add(oCheckDiscount);
			
			// Add the discount back to FuncCheckItem
			for(PosCheckDiscountItem oCheckDiscountItem: oCheckDiscount.getCheckDiscountItemList()) {
				boolean bItemFound = false;
				
				for(List<FuncCheckItem> oItemListForSingleSeat:m_oItemListPerSeat) {
					for(FuncCheckItem oCheckItem:oItemListForSingleSeat) {
						if(oCheckDiscountItem.getCitmId().equals(oCheckItem.getCheckItem().getCitmId())) {
							oCheckItem.addCheckDiscountItemToList(oCheckDiscount.getSeq(), oCheckDiscountItem);
							bItemFound = true;
							break;
						}
					}
					
					if(bItemFound)
						break;
				}
			}
		}
		
		m_oCheckParty.clearCheckDiscountList();
	}
	
	// Remove check discount from list
	public void removeCheckDiscountFromList(int iCheckDiscountIndex) {
		List<FuncCheckItem> oAppliedItemList = this.getAppliedCheckDiscountItemList(iCheckDiscountIndex);
		
		// remove the pos_check_discount_item from FuncCheckItem
		for(FuncCheckItem oAppliedItem:oAppliedItemList)
			oAppliedItem.removeCheckDiscountItemFromList(iCheckDiscountIndex);
		
		// remove check discount from list
		//m_oCheckDiscountList.remove(iCheckDiscountIndex);
		for (PosCheckDiscount oPosCheckDiscount: m_oCheckDiscountList) {
			if (iCheckDiscountIndex == oPosCheckDiscount.getSeq()) {
				m_oCheckDiscountList.remove(oPosCheckDiscount);
				break;
			}
		}
	}
	
	// Void check party discount
	public boolean voidOldCheckDiscount(int iCheckDiscountIndex, int iVoidCodeId) {
		PosCheckDiscount oPosCheckDiscount = null;
		
		if (!this.hasPartyCheckDiscount(iCheckDiscountIndex))
			return false;
		
		oPosCheckDiscount = this.getPartyCheckDiscount(iCheckDiscountIndex);//m_oCheckDiscountList.get(iCheckDiscountIndex);
		if(oPosCheckDiscount.getCdisId().compareTo("") > 0) {
			DateTime dtVoidDateTime = AppGlobal.getCurrentTime(false);
			DateTimeFormatter voidFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
			
			oPosCheckDiscount.setVoidLocTime(dtVoidDateTime);
			oPosCheckDiscount.setVoidTime(voidFormatter.print(AppGlobal.convertTimeToUTC(dtVoidDateTime)));
			oPosCheckDiscount.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
			oPosCheckDiscount.setVoidStatId(AppGlobal.g_oFuncStation.get().getStationId());
			oPosCheckDiscount.setVoidVdrsId(iVoidCodeId);
			oPosCheckDiscount.setStatus(PosCheckDiscount.STATUS_DELETED);
			oPosCheckDiscount.setSeq(-1);
			
			for(PosCheckExtraInfo oCheckExtraInfo: oPosCheckDiscount.getCheckExtraInfoList()) {
				oCheckExtraInfo.setStatus(PosCheckExtraInfo.STATUS_DELETED);
			}
			
			for(FuncCheckItem oFuncCheckItem: this.getAppliedCheckDiscountItemList(iCheckDiscountIndex)) {
				if(oFuncCheckItem.getCheckDiscountItemList(iCheckDiscountIndex).getCditId().compareTo("") > 0)
					oFuncCheckItem.getCheckDiscountItemList(iCheckDiscountIndex).setStatus(PosCheckDiscountItem.STATUS_DELETED);
				oFuncCheckItem.getCheckDiscountItemList(iCheckDiscountIndex).setCdisSeq(-1);
				oFuncCheckItem.getCheckItem().setModified(true);
			}
		}else
			return false;
		
		return true;
	}
	
	// Add new check payment to payment list
	public void addNewCheckPaymentToList(PosCheckPayment oCheckPayment) {
		m_oCheckPaymentList.add(oCheckPayment);
	}
	
	// Add old check payment to payment list
	public void addOldCheckPaymentToList() {
		List<PosCheckPayment> oCheckPaymentList = m_oCheckParty.getCheckPaymentList();
		
		for(PosCheckPayment oCheckPayment: oCheckPaymentList)
			m_oCheckPaymentList.add(oCheckPayment);
		
		m_oCheckParty.clearCheckPaymentList();
	}
	
	// Add item to item list
	public void addItemToItemList(int iSeatNo, int iLineNo, FuncCheckItem oFuncCheckItem){
		if(m_oItemListPerSeat.size() < iSeatNo)
			m_oItemListPerSeat.add(new ArrayList<FuncCheckItem>());
		
		oFuncCheckItem.getCheckItem().setSeatNo(iSeatNo);
		oFuncCheckItem.getCheckItem().setSeq(iLineNo);

		//set the seat number of it's modifier
		if(oFuncCheckItem.hasModifier()) {
			for(FuncCheckItem oModiFuncCheckItem:oFuncCheckItem.getModifierList())
				oModiFuncCheckItem.getCheckItem().setSeatNo(iSeatNo);
		}
		
		while((iLineNo - m_oItemListPerSeat.get(iSeatNo).size()) > 0){
			m_oItemListPerSeat.get(iSeatNo).add(null);
		}
		
		m_oItemListPerSeat.get(iSeatNo).set((iLineNo-1), oFuncCheckItem);
	}
	
	public void insertItemToItemList(int iSeatNo, int iLineNo, List<FuncCheckItem> oFuncCheckItems) {
		if(m_oItemListPerSeat.size() < iSeatNo)
			m_oItemListPerSeat.add(new ArrayList<FuncCheckItem>());

		int iInsertCnt = oFuncCheckItems.size();
		for (int i = 0; i < iInsertCnt; i++) {
			m_oItemListPerSeat.get(iSeatNo).add(null);
		}

		// adjust the sequence for the behind items
		for (int i = m_oItemListPerSeat.get(iSeatNo).size()-iInsertCnt-1; i >= iLineNo-1 && i >= 0 ; i--) {
			FuncCheckItem oFuncCheckItem = m_oItemListPerSeat.get(iSeatNo).get(i);
			
			oFuncCheckItem.getCheckItem().setSeq(oFuncCheckItem.getCheckItem().getSeq()+iInsertCnt);
			m_oItemListPerSeat.get(iSeatNo).set(i+iInsertCnt, oFuncCheckItem);
		}
		// insert the item list
		int iCnt = 0;
		for (FuncCheckItem oFuncCheckItem : oFuncCheckItems) {
			oFuncCheckItem.getCheckItem().setSeatNo(iSeatNo);
			oFuncCheckItem.getCheckItem().setSeq(iLineNo+iCnt);
			
			//set the seat number of it's modifier
			if(oFuncCheckItem.hasModifier()) {
				for(FuncCheckItem oModiFuncCheckItem:oFuncCheckItem.getModifierList())
					oModiFuncCheckItem.getCheckItem().setSeatNo(iSeatNo);
			}
			m_oItemListPerSeat.get(iSeatNo).set((iLineNo+iCnt-1), oFuncCheckItem);
			iCnt++;
		}
	}
	
	public boolean hasNewItem() {
		boolean bNewItem = false;
		
		for(List<FuncCheckItem> oItemListForSingleSeat:m_oItemListPerSeat) {
			for(FuncCheckItem oCheckItem:oItemListForSingleSeat)
				if(!oCheckItem.isOldItem())
					bNewItem = true;
		}
		
		return bNewItem;
	}
	
	public boolean hasModifiedItem() {
		boolean bModifiedItem = false;
		
		for (List<FuncCheckItem> oItemListForSingleSeat:m_oItemListPerSeat) {
			for (FuncCheckItem oCheckItem:oItemListForSingleSeat) {
				if (oCheckItem.isModified()) {
					bModifiedItem = true;
					break;
				}
			}
			
			if (bModifiedItem)
				break;
		}
		
		return bModifiedItem;
	}
	
	public boolean hasPartyCheckDiscount() {
		if(!m_oCheckDiscountList.isEmpty())
			return true;
		else
			return false;
	}
	
	public boolean hasPartyCheckDiscount(int iCheckDiscountIndex) {
		boolean bFound = false;
		for (PosCheckDiscount oPosCheckDiscount: m_oCheckDiscountList) {
			if (oPosCheckDiscount.getSeq() == iCheckDiscountIndex) {
				bFound = true;
				break;
			}
		}
		
		return bFound;
	}
	
	// Get item code list that is included in provided redeem item list
	public ArrayList<HashMap<String, String>> getOrderedRedeemItemCodeList(ArrayList<String> oDefinedRedeemItem) {
		ArrayList<HashMap<String, String>> oOrderedRedeemItems = new ArrayList<HashMap<String, String>>();
		ArrayList<String> oSelectedRedeemItems = new ArrayList<String>();
		
		for(List<FuncCheckItem> oItemListForSingleSeat:m_oItemListPerSeat) {
			for(FuncCheckItem oCheckItem:oItemListForSingleSeat) {
				if(oCheckItem.getCheckItem().getCode().equals(""))
					continue;
				
				if(oCheckItem.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_ONLINE_COUPON, PosCheckExtraInfo.VARIABLE_REDEEM_COUNT) != null) {
					BigDecimal dRedeemCount = new BigDecimal(oCheckItem.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_ONLINE_COUPON, PosCheckExtraInfo.VARIABLE_REDEEM_COUNT));
					if(oCheckItem.getCheckItem().getQty().equals(dRedeemCount))
						continue;
				}
				
				if(oDefinedRedeemItem.contains(oCheckItem.getCheckItem().getCode()) && !oSelectedRedeemItems.contains(oCheckItem.getCheckItem().getCode())) {
					HashMap<String, String> oRedeemItem = new HashMap<String, String>();
					oRedeemItem.put("code", oCheckItem.getCheckItem().getCode());
					oRedeemItem.put("name", oCheckItem.getCheckItem().getName(AppGlobal.g_oCurrentLangIndex.get()));
					
					oSelectedRedeemItems.add(oCheckItem.getCheckItem().getCode());
					oOrderedRedeemItems.add(oRedeemItem);
				}
			}
		}
		
		return oOrderedRedeemItems;
	}
	
	// Get the target FuncCheckItem as redeemed item
	public FuncCheckItem getTargetRedeemItemByItemCode(String sItemCode) {
		FuncCheckItem oTargetRedeemItem = null;
		
		for(List<FuncCheckItem> oItemListForSingleSeat:m_oItemListPerSeat) {
			for(FuncCheckItem oCheckItem:oItemListForSingleSeat) {
				if(oCheckItem.getCheckItem().getCode().equals(sItemCode)) {				
					if(oCheckItem.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_ONLINE_COUPON, PosCheckExtraInfo.VARIABLE_REDEEM_COUNT) != null) {
						BigDecimal dRedeemCount = new BigDecimal(oCheckItem.getExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_ONLINE_COUPON, PosCheckExtraInfo.VARIABLE_REDEEM_COUNT));
						if(oCheckItem.getCheckItem().getQty().compareTo(dRedeemCount) > 0) {
							oTargetRedeemItem = oCheckItem;
							break;
						}
					}else {
						oTargetRedeemItem = oCheckItem;
						break;
					}
				}
			}
		}
		
		return oTargetRedeemItem;
	}
	
	// Get target item
	public FuncCheckItem getCheckItem(int iSeatNo, int iItemIndex){
		if(iSeatNo < 0 || m_oItemListPerSeat.size() <= iSeatNo){
			return null;
		}
		
		if(m_oItemListPerSeat.get(iSeatNo).size() <= iItemIndex){
			return null;
		}

		if(iItemIndex < 0)
			return null;
		
		return m_oItemListPerSeat.get(iSeatNo).get(iItemIndex);
	}
	
	// Get no. of child in the item list
	public List<FuncCheckItem> getItemList(int iSeatNo){
		if(m_oItemListPerSeat.size() <= iSeatNo){
			return null;
		}
		
		return m_oItemListPerSeat.get(iSeatNo);
	}
	
	public void replaceItemToList(int iSeatNo, int iLineNo) {
		List<FuncCheckItem> oFuncCheckItemList = m_oItemListPerSeat.get(iSeatNo);
		if(iLineNo > oFuncCheckItemList.size()-1)
			return;
		
		int iLastLineNo = oFuncCheckItemList.size()-1;
		FuncCheckItem oFuncCheckItem = oFuncCheckItemList.get(iLastLineNo);
		PosCheckItem oCheckItem = oFuncCheckItem.getCheckItem();
		
		oCheckItem.setSeatNo(iSeatNo);
		oCheckItem.setSeq(iLineNo);
		
		oFuncCheckItemList.set(iLineNo, oFuncCheckItem);
		oFuncCheckItemList.remove(oFuncCheckItemList.size()-1);
	}
	
	// Get whole item list
	public List<List<FuncCheckItem>> getWholeItemList(){
		return m_oItemListPerSeat;
	}
	
	// Set whole item list
	public void setWholeItemList(List<List<FuncCheckItem>> oItemList){
		m_oItemListPerSeat = oItemList;
	}
	
	// Set check discount list
	public void setCheckDiscountList(List<PosCheckDiscount> oCheckDiscountList){
		m_oCheckDiscountList = oCheckDiscountList;
	}
	
	// Get no. of child in the item list
	public int getItemListCount(int iSeatNo){
		if(m_oItemListPerSeat.size() < iSeatNo) {
			m_oItemListPerSeat.add(new ArrayList<FuncCheckItem>());
			return 0;
		}
		
		return m_oItemListPerSeat.get(iSeatNo).size();
	}
	
	// Get total quantity of ordered item
	public BigDecimal getOrderedItemCount() {
		BigDecimal dTotalQtyBigDecimal = BigDecimal.ZERO;
				
		for(List<FuncCheckItem> oItemListForSingleSeat:m_oItemListPerSeat) {
			for(FuncCheckItem oCheckItem:oItemListForSingleSeat)
				if(oCheckItem.isOldItem())
					dTotalQtyBigDecimal = dTotalQtyBigDecimal.add(oCheckItem.getCheckItem().getQty());
		}
		
		return dTotalQtyBigDecimal;
	}
	
	// Get total quantity of new item
	public BigDecimal getNewItemCount() {
		BigDecimal dTotalQtyBigDecimal = BigDecimal.ZERO;
		
		for(List<FuncCheckItem> oItemListForSingleSeat:m_oItemListPerSeat) {
			for(FuncCheckItem oCheckItem:oItemListForSingleSeat)
				if(oCheckItem.isOldItem() == false)
					dTotalQtyBigDecimal = dTotalQtyBigDecimal.add(oCheckItem.getCheckItem().getQty());
		}
		
		return dTotalQtyBigDecimal;
	}
	
	// Get total line count of new unique item (i.e. item in different line with same menu item ID count 1)
	public int getNewUniqueItemCount() {
		HashMap<Integer, Integer> oUniqueItemList = new HashMap<Integer, Integer>();
		
		for(List<FuncCheckItem> oItemListForSingleSeat:m_oItemListPerSeat) {
			for(FuncCheckItem oCheckItem:oItemListForSingleSeat)
				if(oCheckItem.isOldItem() == false)
					oUniqueItemList.put(oCheckItem.getMenuItemId(), oCheckItem.getMenuItemId());
		}
		
		return oUniqueItemList.size();
	}
	
	// Get the applied party's check discount item list by index
	public List<FuncCheckItem> getAppliedCheckDiscountItemList(int iCheckDiscountIndex) {
		List<FuncCheckItem> oAppliedItemList = new ArrayList<FuncCheckItem>();
		
		for(List<FuncCheckItem> oItemListForSingleSeat:m_oItemListPerSeat) {
			for(FuncCheckItem oCheckItem:oItemListForSingleSeat) {
				if(oCheckItem.isCheckDiscountItemExist(iCheckDiscountIndex))
					oAppliedItemList.add(oCheckItem);
			}
		}
		
		return oAppliedItemList;
	}
		
	// Get no. of child item in a item
	public int getChildItemListCount(int iSeatNo, int iItemIndex){
		if(m_oItemListPerSeat.size() < iSeatNo){
			return 0;
		}
		
		if(m_oItemListPerSeat.get(iSeatNo).size()<iItemIndex)
			return 0;
		
		FuncCheckItem oFuncCheckItem = m_oItemListPerSeat.get(iSeatNo).get(iItemIndex);
		return oFuncCheckItem.getChildItemList().size();
	}
	
	// Get no. of modifier in a item
	public int getModifierListCount(int iSeatNo, int iItemIndex){
		if(m_oItemListPerSeat.size() < iSeatNo){
			return 0;
		}
		
		if(m_oItemListPerSeat.get(iSeatNo).size()<iItemIndex)
			return 0;
		
		FuncCheckItem oFuncCheckItem = m_oItemListPerSeat.get(iSeatNo).get(iItemIndex);
		return oFuncCheckItem.getModifierList().size();
	}
	
	public List<PosCheckDiscount> getPartyCheckDiscount() {
		return m_oCheckDiscountList;
	}
	
	public PosCheckDiscount getPartyCheckDiscount(int iCheckDiscountIndex) {
		PosCheckDiscount oPosCheckDiscount = null;
		for (PosCheckDiscount oTempPosCheckDiscount: m_oCheckDiscountList) {
			if (iCheckDiscountIndex == oTempPosCheckDiscount.getSeq()) {
				oPosCheckDiscount = oTempPosCheckDiscount;
				break;
			}
		}
		return oPosCheckDiscount;
	}
	
	public String getCptyId(){
		return m_oCheckParty.getCptyId();
	}
	
	public PosCheckParty getCheckParty(){
		return m_oCheckParty;
	}
	
	//get party's seat and item index pair list where item have applied check discount according to check discount index
	public List<HashMap<String, Integer>> getSeatItemIndexPairWithAppliedCheckDiscount(int iChkDiscIndex) {
		List<HashMap<String, Integer>> oSeatItemPairList = new ArrayList<HashMap<String, Integer>>();
		
		for(int iSeat=0; iSeat<m_oItemListPerSeat.size(); iSeat++) {
			for(int iItemIndex=0; iItemIndex<m_oItemListPerSeat.get(iSeat).size(); iItemIndex++) {
				if(m_oItemListPerSeat.get(iSeat).get(iItemIndex).hasAppliedCheckDiscountByIndex(iChkDiscIndex)) {
					HashMap<String, Integer> oSeatItemPair = new HashMap<String, Integer>();
					oSeatItemPair.put("partySeq", m_oCheckParty.getSeq());
					oSeatItemPair.put("sectionId", iSeat);
					oSeatItemPair.put("itemIndex", iItemIndex);
					oSeatItemPairList.add(oSeatItemPair);
				}
			}
		}
		
		return oSeatItemPairList;
	}
	
	public void cleanupPosCheckPartyItemList() {
		m_oCheckParty.clearCheckItemList();
	}
	
	public void cleanupPosCheckPartyCheckDiscountList() {
		m_oCheckParty.clearCheckDiscountList();
	}

	// Rollback Void check party discount
	public boolean rollBackVoidOldCheckDiscount(int iCheckDiscountIndex) {
		PosCheckDiscount oPosCheckDiscount = null;

		if (!this.hasPartyCheckDiscount(iCheckDiscountIndex))
			return false;

		oPosCheckDiscount = this.getPartyCheckDiscount(iCheckDiscountIndex);
		if (oPosCheckDiscount.getCdisId().compareTo("") > 0) {
			oPosCheckDiscount.setVoidLocTime(null);
			oPosCheckDiscount.setVoidTime(null);
			oPosCheckDiscount.setVoidUserId(0);
			oPosCheckDiscount.setVoidStatId(0);
			oPosCheckDiscount.setVoidVdrsId(0);
			oPosCheckDiscount.setStatus(PosCheckDiscount.STATUS_ACTIVE);
			oPosCheckDiscount.setSeq(0);

			for (PosCheckExtraInfo oCheckExtraInfo : oPosCheckDiscount.getCheckExtraInfoList()) {
				oCheckExtraInfo.setStatus(PosCheckExtraInfo.STATUS_ACTIVE);
			}

			for (FuncCheckItem oFuncCheckItem : this.getAppliedCheckDiscountItemList(iCheckDiscountIndex)) {
				oFuncCheckItem.getCheckDiscountItemList(iCheckDiscountIndex)
						.setStatus(PosCheckDiscountItem.STATUS_ACTIVE);
				oFuncCheckItem.getCheckDiscountItemList(iCheckDiscountIndex).setCdisSeq(0);
				oFuncCheckItem.getCheckItem().setModified(false);
			}
		} else
			return false;

		return true;
	}
	
	public void setPaid(String sStatus){
		m_oCheckParty.setPaid(sStatus);
	}
	
	public BigDecimal getInclScRef(int iIndex) {
		return m_dInclScRef[iIndex-1]; 
	}
	
	public BigDecimal getInclTaxRef(int iIndex) {
		return m_dInclTaxRef[iIndex-1]; 
	}
	
	public void setInclScRef(int iIndex, BigDecimal dValue) {
		m_dInclScRef[iIndex-1] = dValue; 
	}
	
	public void setInclTaxRef(int iIndex, BigDecimal dValue) {
		m_dInclTaxRef[iIndex-1] = dValue; 
	}
	
	private BigDecimal[] preHandleForDiscOnScTax(String sType, boolean bTax){
		String sDiscType = "pre";
		if(sType.equals(PosDiscountType.TYPE_MID_DISCOUNT))
			sDiscType = "mid";
		else if(sType.equals(PosDiscountType.TYPE_POST_DISCOUNT))
			sDiscType = "post";
		
		BigDecimal[] dDiscOnScTax = m_oDiscOnSc.get(sDiscType);
		if(bTax)
			dDiscOnScTax = m_oDiscOnTax.get(sDiscType);
		return dDiscOnScTax;
	}
	
	public BigDecimal getDiscOnScTax(String sType, boolean bTax, int iIndex){
		BigDecimal[] dDiscOnScTax = preHandleForDiscOnScTax(sType, bTax);
		return dDiscOnScTax[iIndex-1];
	}
	
	public void setDiscOnScTax(String sType, boolean bTax, int iIndex, BigDecimal dValue){
		BigDecimal[] dDiscOnScTax = preHandleForDiscOnScTax(sType, bTax);
		
		if(iIndex == 0) {
			int iCount = AppGlobal.SC_COUNT;
			if(bTax)
				iCount = AppGlobal.TAX_COUNT;
			for(int i=1; i<=iCount; i++)
				dDiscOnScTax[i-1] = dValue;
		}else
			dDiscOnScTax[iIndex-1] = dValue;
	}
	
	public void addDiscOnScTaxRoundingToItem(String sType, boolean bTax, int iIndex, BigDecimal dRounding) {
		if(!sType.equals(PosDiscountType.TYPE_POST_DISCOUNT))
			return;
		String sTaxScRefPrefix = "round_post_disc_on_sc_ref";
		if(bTax)
			sTaxScRefPrefix = "round_post_disc_on_tax_ref";
		
		for (List<FuncCheckItem> oItemList:m_oItemListPerSeat){
			boolean bHandled = false;
			for (FuncCheckItem oFuncCheckItem:oItemList){
				String sTaxScRefKey = sTaxScRefPrefix.concat(String.valueOf(iIndex));
				BigDecimal dTaxScRefValue = oFuncCheckItem.getTaxScRefByVariable(sTaxScRefKey);
				if(!dTaxScRefValue.equals(BigDecimal.ZERO)) {
					oFuncCheckItem.updateTaxScRefValue("item", sTaxScRefKey, dTaxScRefValue.add(dRounding).toPlainString());
					bHandled = true;
					break;
				}
			}
			
			if(bHandled)
				break;
		}
	}
	
	public void swipeBreakdownValue(boolean bBreakdown) {
		BigDecimal dNewItemTotal = BigDecimal.ZERO, dNewPartyTotal = BigDecimal.ZERO, dPreDiscTotal = BigDecimal.ZERO;
		BigDecimal[] dSc = new BigDecimal[AppGlobal.SC_COUNT], dTax = new BigDecimal[AppGlobal.TAX_COUNT];
		
		for(int i=1; i<=AppGlobal.SC_COUNT; i++)
			dSc[i-1] = BigDecimal.ZERO;
		for(int i=1; i<=AppGlobal.TAX_COUNT; i++)
			dTax[i-1] = BigDecimal.ZERO;
		
		for (List<FuncCheckItem> oItemList: getWholeItemList()){
			for (FuncCheckItem oFuncCheckItem: oItemList) {
				PosCheckItem oCheckItem = oFuncCheckItem.getCheckItem();
				dNewItemTotal = dNewItemTotal.add(oCheckItem.getRoundTotal());
				dNewPartyTotal = dNewPartyTotal.add(oCheckItem.getRoundTotal());
				for(int i=1; i<AppGlobal.SC_COUNT; i++) {
					dSc[i-1] = dSc[i-1].add(oCheckItem.getSc(i));
					dNewPartyTotal = dNewPartyTotal.add(oCheckItem.getSc(i));
				}for(int i=1; i<AppGlobal.TAX_COUNT; i++) {
					dTax[i-1] = dTax[i-1].add(oCheckItem.getTax(i));
					dNewPartyTotal = dNewPartyTotal.add(oCheckItem.getTax(i));
				}
				
				if(oFuncCheckItem.hasItemDiscount(false)) {
					for(PosCheckDiscount oItemDisc : oFuncCheckItem.getItemDiscountList()) {
						if(oItemDisc.isPreDiscountType())
							dPreDiscTotal = dPreDiscTotal.add(oItemDisc.getRoundTotal());
					}
				}
			}
		}
		
		// handle check pre discount
		for(PosCheckDiscount oCheckDiscount : m_oCheckDiscountList) {
			if(!oCheckDiscount.isPreDiscountType())
				continue;
			
			List<PosCheckDiscountItem> oCheckDiscItemList = new ArrayList<PosCheckDiscountItem>();
			for (List<FuncCheckItem> oItemList: getWholeItemList()){
				for (FuncCheckItem oFuncCheckItem: oItemList) {
					PosCheckDiscountItem oTmpDiscItem = oFuncCheckItem.getCheckDiscountItemList(oCheckDiscount.getSeq());
					if(oTmpDiscItem != null) {
						oTmpDiscItem.setItemSeatSeq(oFuncCheckItem.getCheckItem().getSeatNo(), oFuncCheckItem.getCheckItem().getSeq());
						oCheckDiscItemList.add(oTmpDiscItem);
					}
				}
			}
			oCheckDiscount.swipeBreakdownValue(bBreakdown, oCheckDiscItemList);
			dPreDiscTotal = dPreDiscTotal.add(oCheckDiscount.getRoundTotal());
		}
		
		dNewPartyTotal = m_oCheckParty.getItemTotal();
		m_oCheckParty.setItemTotal(AppGlobal.g_oFuncOutlet.get().roundItemAmountToBigDecimal(dNewItemTotal));
		for(int i=1; i<AppGlobal.SC_COUNT; i++) {
			m_oCheckParty.setSc(i, AppGlobal.g_oFuncOutlet.get().roundSCAmountToBigDecimal(dSc[i-1]));
			dNewPartyTotal = dNewPartyTotal.add(m_oCheckParty.getSc(i));
		}
		for(int i=1; i<AppGlobal.TAX_COUNT; i++) {
			m_oCheckParty.setTax(i, AppGlobal.g_oFuncOutlet.get().roundTaxAmountToBigDecimal(dTax[i-1]));
			dNewPartyTotal = dNewPartyTotal.add(m_oCheckParty.getTax(i));
		}
		for(int i=1; i<AppGlobal.INCL_TAX_COUNT; i++)
			m_oCheckParty.setInclusiveTaxRef(i, AppGlobal.g_oFuncOutlet.get().roundTaxAmountToBigDecimal(BigDecimal.ZERO));
		
		m_oCheckParty.setPartyTotal(AppGlobal.g_oFuncOutlet.get().roundCheckAmountToBigDecimal(dNewPartyTotal));
		m_oCheckParty.setRoundAmount(m_oCheckParty.getPartyTotal().subtract(dNewPartyTotal));
		
		if(dPreDiscTotal.compareTo(BigDecimal.ZERO) != 0)
			m_oCheckParty.setPreDisc(dPreDiscTotal);
	}
	
	public boolean isHundredPercentPostDiscForWholeParty() {
		boolean bResult = false;
		if(!m_oCheckParty.getItemTotal().equals(m_oCheckParty.getPostDisc().abs())) 
			return bResult;
		
		//	check for check discount
		for(PosCheckDiscount oTempDisc : m_oCheckDiscountList) {
			if(oTempDisc.isHundredPercentDisc(PosDiscountType.TYPE_POST_DISCOUNT)) {
				bResult = true;
				break;
			}
		}
		if(bResult)
			return bResult;
		
		//	check for item discount
		int iItemCount = 0, iItemDiscCount = 0;
		for (List<FuncCheckItem> oItemList: getWholeItemList()){
			for (FuncCheckItem oFuncCheckItem: oItemList) {
				iItemCount++;
				if(oFuncCheckItem.getCheckItem().getPostDisc().equals(BigDecimal.ZERO))
					continue;
				
				for(PosCheckDiscount oTempDisc : oFuncCheckItem.getItemDiscountList()) {
					if(oTempDisc.isHundredPercentDisc(PosDiscountType.TYPE_POST_DISCOUNT)) {
						iItemDiscCount++;
						break;
					}
				}
			}
		}
		
		if(iItemCount == iItemDiscCount)
			bResult = true;
		
		return bResult;
	}
}