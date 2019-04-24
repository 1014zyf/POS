package app.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import app.model.*;

public class FuncCheckParty {
	private static int MAX_SEATS=99;
	
	// Check party
	private PosCheckParty m_oCheckParty;
	
	// Check discount list
	private HashMap<Integer, PosCheckDiscount> m_oCheckDiscountList;
	private int m_iCheckDiscountLastIndex;
	
	// Check payment list
	private ArrayList<PosCheckPayment> m_oCheckPaymentList;
	
	// Item list
	private List<List<FuncCheckItem>> m_oItemListPerSeat;
	
	// Last error message
	private String m_sErrorMessage;
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage;
	}
	
	public FuncCheckParty(){		
		m_oCheckParty = new PosCheckParty();
		
		// Discount list
		m_oCheckDiscountList = new HashMap<Integer, PosCheckDiscount>();
		m_iCheckDiscountLastIndex = 0;
		// Payment list
		m_oCheckPaymentList = new ArrayList<PosCheckPayment>();
		
		// Item list for each seat
		m_oItemListPerSeat = new ArrayList<List<FuncCheckItem>>();
		// Create the max seat number item lists (e.g. Shared, 1, 2, ...)
		for(int i = 0; i <= MAX_SEATS; i++) {
			m_oItemListPerSeat.add(new ArrayList<FuncCheckItem>());
		}
		
		m_sErrorMessage = "";
	}
	
	// Create FuncCheckItem from PosCheckItem loading from database
	public FuncCheckParty(PosCheckParty oCheckParty) {
		int i = 0;
		
		m_oCheckParty = new PosCheckParty(oCheckParty);
		
		// Discount list
		m_oCheckDiscountList = new HashMap<Integer, PosCheckDiscount>();
		m_iCheckDiscountLastIndex = 0;
		// Payment list
		m_oCheckPaymentList = new ArrayList<PosCheckPayment>();
		
		// Item list for each seat
		m_oItemListPerSeat = new ArrayList<List<FuncCheckItem>>();
		// Create the max seat number item lists (e.g. Shared, 1, 2, ...)
		for(i = 0; i <= MAX_SEATS; i++) {
			m_oItemListPerSeat.add(new ArrayList<FuncCheckItem>());
		}
		
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
		for(Entry<Integer, PosCheckDiscount> entry: this.m_oCheckDiscountList.entrySet()) {
			int iCheckDiscountIndex = entry.getKey();
			PosCheckDiscount oPosCheckDiscount = entry.getValue();
			List<FuncCheckItem> oAppliedFuncCheckItems = getAppliedCheckDiscountItemList(iCheckDiscountIndex);
			oPartyCheckDiscountList.add(oPosCheckDiscount);
		}
		m_oCheckParty.setCheckDiscountList(oPartyCheckDiscountList);
		
		return m_oCheckParty;
	}
	
	// Update several information (e.g. outlet id, shop id, ...) to check item records
	public void updateMultipleCheckItemInfoForNewItem(int iBusinessDayId, int iBusinessPeriodId, int iChksId, int iShopId, int iOutletId, String sOrderTime, DateTime oOrderDateTime) {
		// Set new item period ID
		for(List<FuncCheckItem> oItemListForSingleSeat:m_oItemListPerSeat) {
			for(FuncCheckItem oFuncCheckItem:oItemListForSingleSeat) {
				if(!oFuncCheckItem.isOldItem()) {
					oFuncCheckItem.updateMultipleCheckItemInfoForNewItem(iBusinessDayId, iBusinessPeriodId, iChksId, iShopId, iOutletId, sOrderTime, oOrderDateTime);
				}
			}
		}
	}
	
	// Add old item to item list
	public void addOldItemToItemList() {
		int i = 0, iSeatNo = 0, iLineNo = 0;
		FuncCheckItem oFuncCheckItem = null;
		List<PosCheckItem> checkItemArrayList = null;
		List<Integer> missingCheckItemArrayList = new ArrayList<Integer>();
		checkItemArrayList = m_oCheckParty.getCheckItemList();
		
		for(i=0; i<checkItemArrayList.size(); i++) {
			oFuncCheckItem = new FuncCheckItem(checkItemArrayList.get(i));
			iSeatNo = checkItemArrayList.get(i).getSeatNo();
			iLineNo = checkItemArrayList.get(i).getSeq();
			
			this.addItemToItemList(iSeatNo, iLineNo, oFuncCheckItem);
			
			if(oFuncCheckItem.hasChildItem()) {
				for(FuncCheckItem oChildCheckItem:oFuncCheckItem.getChildItemList()){
					iSeatNo = oChildCheckItem.getCheckItem().getSeatNo();
					iLineNo = oChildCheckItem.getCheckItem().getSeq();
					this.addItemToItemList(iSeatNo, iLineNo, oChildCheckItem);
				}
			}
		}
		
		for(List<FuncCheckItem> oItemListForSingleSeat:m_oItemListPerSeat) {
			i = 0;
			missingCheckItemArrayList.clear();
			for(FuncCheckItem oCheckItem:oItemListForSingleSeat){
				if(oCheckItem == null){
					// Missing item in this seq
					missingCheckItemArrayList.add(0, i);
				}
				i++;
			}
			
			for(int iIndex:missingCheckItemArrayList){
				oItemListForSingleSeat.remove(iIndex);
			}
		}
		
		m_oCheckParty.clearCheckItemList();
	}
	
	// Add new check discount to list
	public int addNewCheckDiscountToList(PosCheckDiscount oCheckDiscount) {
		m_iCheckDiscountLastIndex++;
		m_oCheckDiscountList.put(m_iCheckDiscountLastIndex, oCheckDiscount);
		
		return m_iCheckDiscountLastIndex;
	}
	
	// Add old check discount to list
	public void addOldCheckDiscountToList() {
		List<PosCheckDiscount> oCheckDiscList = m_oCheckParty.getCheckDiscountList();
		
		for(PosCheckDiscount oCheckDiscount:oCheckDiscList) {
			m_iCheckDiscountLastIndex++;
			oCheckDiscount.setCheckDiscountIndex(m_iCheckDiscountLastIndex);
			m_oCheckDiscountList.put(m_iCheckDiscountLastIndex, oCheckDiscount);
			
			// Add the discount back to FuncCheckItem
			for(PosCheckDiscountItem oCheckDiscountItem: oCheckDiscount.getCheckDiscountItemList()) {
				boolean bItemFound = false;
				
				for(List<FuncCheckItem> oItemListForSingleSeat:m_oItemListPerSeat) {
					for(FuncCheckItem oCheckItem:oItemListForSingleSeat) {
						if(oCheckDiscountItem.getCitmId() == oCheckItem.getCheckItem().getCitmId()) {
							oCheckItem.addCheckDiscountItemToList(m_iCheckDiscountLastIndex, oCheckDiscountItem);
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
		m_oCheckDiscountList.remove(Integer.valueOf(iCheckDiscountIndex));
	}
	
	// Void check party discount
	public boolean voidOldCheckDiscount(int iCheckDiscountIndex, int iVoidCodeId) {
		PosCheckDiscount oPosCheckDiscount = null;
		
		if(!m_oCheckDiscountList.containsKey(iCheckDiscountIndex))
			return false;
		
		oPosCheckDiscount = m_oCheckDiscountList.get(iCheckDiscountIndex);
		if(oPosCheckDiscount.getCdisId() > 0) {
			DateTime dtVoidDateTime = new DateTime();
			DateTimeFormatter voidFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
			
			oPosCheckDiscount.setVoidLocTime(dtVoidDateTime);
			oPosCheckDiscount.setVoidTime(voidFormatter.print(dtVoidDateTime.withZone(DateTimeZone.UTC)));
			oPosCheckDiscount.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
			oPosCheckDiscount.setVoidStatId(AppGlobal.g_oFuncStation.get().getStationId());
			oPosCheckDiscount.setVoidVdrsId(iVoidCodeId);
			oPosCheckDiscount.setStatus(PosCheckDiscount.STATUS_DELETED);
			
			List<Integer> oFuncCheckItemIds = new ArrayList<Integer>();
			for(FuncCheckItem oFuncCheckItem: this.getAppliedCheckDiscountItemList(iCheckDiscountIndex)) {
				if(oFuncCheckItem.getCheckDiscountItemList(iCheckDiscountIndex).getCditId() > 0)
					oFuncCheckItemIds.add(Integer.valueOf(oFuncCheckItem.getCheckDiscountItemList(iCheckDiscountIndex).getCditId()));
				oFuncCheckItem.getCheckItem().setModified(true);
			}
			if(oFuncCheckItemIds.size() > 0) {
				PosCheckDiscountItem oPosCheckDiscountItem = new PosCheckDiscountItem();
				oPosCheckDiscountItem.deleteByIds(oFuncCheckItemIds);
				for(FuncCheckItem oFuncCheckItem: this.getAppliedCheckDiscountItemList(iCheckDiscountIndex))
					oFuncCheckItem.removeCheckDiscountItemFromList(iCheckDiscountIndex);
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
		if(m_oItemListPerSeat.size() < iSeatNo) {
			m_oItemListPerSeat.add(new ArrayList<FuncCheckItem>());
		}
		
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
		for (int i = m_oItemListPerSeat.get(iSeatNo).size()-iInsertCnt-1; i >= iLineNo-1; i--) {
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
	
	public boolean hasPartyCheckDiscount() {
		if(m_oCheckDiscountList.size() > 0)
			return true;
		else
			return false;
	}
	
	public boolean hasPartyCheckDiscount(int iCheckDiscountIndex) {
		return m_oCheckDiscountList.containsKey(Integer.valueOf(iCheckDiscountIndex));
	}
	
	// Get item code list that is included in provided redeem item list
	public ArrayList<HashMap<String, String>> getOrderedRedeemItemCodeList(ArrayList<String> oDefinedRedeemItem) {
		ArrayList<HashMap<String, String>> oOrderedRedeemItems = new ArrayList<HashMap<String, String>>();
		ArrayList<String> oSelectedRedeemItems = new ArrayList<String>();
		
		for(List<FuncCheckItem> oItemListForSingleSeat:m_oItemListPerSeat) {
			for(FuncCheckItem oCheckItem:oItemListForSingleSeat) {
				if(oCheckItem.getCheckItem().getCode().equals(""))
					continue;
				
				if(oCheckItem.getExtraInfoBySectionAndVariable("online_coupon", "redeem_count") != null) {
					BigDecimal dRedeemCount = new BigDecimal(oCheckItem.getExtraInfoBySectionAndVariable("online_coupon", "redeem_count"));
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
					if(oCheckItem.getExtraInfoBySectionAndVariable("online_coupon", "redeem_count") != null) {
						BigDecimal dRedeemCount = new BigDecimal(oCheckItem.getExtraInfoBySectionAndVariable("online_coupon", "redeem_count"));
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
			for(FuncCheckItem oCheckItem:oItemListForSingleSeat)
				if(oCheckItem.isCheckDiscountItemExist(iCheckDiscountIndex))
					oAppliedItemList.add(oCheckItem);
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
	
	public HashMap<Integer, PosCheckDiscount> getPartyCheckDiscount() {
		return m_oCheckDiscountList;
	}
	
	public PosCheckDiscount getPartyCheckDiscount(int iCheckDiscountIndex) {
		return m_oCheckDiscountList.get(Integer.valueOf(iCheckDiscountIndex));
	}
	
	public int getCptyId(){
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
}