package app.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;

import externallib.StringLib;

import app.model.*;

public class FuncMixAndMatch {
	// Mix and match rule list
	private ArrayList<PosMixAndMatchRule> m_oMixAndMatchRuleList;
	private HashMap<Integer, ArrayList<PosMixAndMatchItem>> m_oMixAndMatchItemList;
	private HashMap<Integer, ArrayList<PosMixAndMatchItem>> m_oMixAndMatchParentItemList;
	
	// Last error message
	private String m_sErrorMessage;
	
	public FuncMixAndMatch() {
		m_oMixAndMatchRuleList = new ArrayList<PosMixAndMatchRule>();
		m_oMixAndMatchItemList = new HashMap<Integer, ArrayList<PosMixAndMatchItem>>();
		m_oMixAndMatchParentItemList = new HashMap<Integer, ArrayList<PosMixAndMatchItem>>();
	}
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage;
	}
	
	public boolean loadRuleList(int iShopId, int iOutletId){
		m_sErrorMessage = "";
		
		m_oMixAndMatchRuleList.clear();
		m_oMixAndMatchItemList.clear();
		m_oMixAndMatchParentItemList.clear();
		
		PosMixAndMatchRuleList oPosMixAndMatchRuleList = new PosMixAndMatchRuleList();
		oPosMixAndMatchRuleList.readAll(iShopId, iOutletId);
		
		m_oMixAndMatchRuleList = oPosMixAndMatchRuleList.getRuleList();
		
		for(PosMixAndMatchRule oPosMixAndMatchRule:m_oMixAndMatchRuleList){
			
			ArrayList<PosMixAndMatchItem> oPosMixAndMatchItemList = new ArrayList<PosMixAndMatchItem>();
			ArrayList<PosMixAndMatchItem> oPosMixAndMatchParentItemList = new ArrayList<PosMixAndMatchItem>();
			
			for(PosMixAndMatchItem oPosMixAndMatchItem:oPosMixAndMatchRule.getRuleItemList()){
				if(oPosMixAndMatchItem.getIndex() == 0){
					oPosMixAndMatchParentItemList.add(oPosMixAndMatchItem);
				}
				oPosMixAndMatchItemList.add(oPosMixAndMatchItem);
			}
			
			m_oMixAndMatchItemList.put(oPosMixAndMatchRule.getRuleId(), oPosMixAndMatchItemList);
			m_oMixAndMatchParentItemList.put(oPosMixAndMatchRule.getRuleId(), oPosMixAndMatchParentItemList);
		}
		
		return true;
	}
	
	public PosMixAndMatchRule getRule(int iRuleId) {
		
		for(PosMixAndMatchRule oPosMixAndMatchRule:m_oMixAndMatchRuleList){
			if(oPosMixAndMatchRule.getRuleId() == iRuleId)
				return oPosMixAndMatchRule;
		}
		
		return null;
	}
	
	public boolean isSupportMixAndMatch(){
		if(m_oMixAndMatchParentItemList.size() > 0)
			return true;
		else
			return false;
	}
	
	public HashMap<Integer, ArrayList<PosMixAndMatchItem>> getMixAndMatchItemList(){
		return m_oMixAndMatchItemList;
	}
	
	public void processMixAndMatch(FuncCheck oFuncCheck){
		m_sErrorMessage = "";
		
		HashMap<Integer, Integer>oRuleIdList = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer>oItemIdList = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer>oDeptIdList = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer>oCatIdList = new HashMap<Integer, Integer>();
		
		JSONArray oPostRuleIdList = new JSONArray();
		JSONArray oPostItemIdList = new JSONArray();
		JSONArray oPostDeptIdList = new JSONArray();
		JSONArray oPostCatIdList = new JSONArray();
		
		TreeMap<String, Integer>oItemMatchedRuleIdPairList = new TreeMap<String, Integer>();
		HashMap<String, PosMixAndMatchItem>oItemMatchedMixAndMatchItemPairList = new HashMap<String, PosMixAndMatchItem>();
		HashMap<Integer, Integer>oRuleParentCountList = new HashMap<Integer, Integer>();
		
		// Create the rule ID list, item ID list, department ID list, category ID list
		for(int i = 0; i < oFuncCheck.getCheckPartyList().size(); i++) {
			for(int j = 0; j <= AppGlobal.MAX_SEATS; j++) {
				ArrayList<FuncCheckItem> oFuncCheckItemList = (ArrayList<FuncCheckItem>)oFuncCheck.getCheckPartyBySeq(i).getItemList(j);
				int iItemIndex = 0;
				for(FuncCheckItem oFuncCheckItem:oFuncCheckItemList){
					
					// Skip set menu child item
					if(oFuncCheckItem.isSetMenuItem()){
						iItemIndex++;
						continue;
					}
					
					// Check if item is deleted in database or not
					if(oFuncCheckItem.getMenuItem() == null){
						iItemIndex++;
						continue;
					}

					boolean bHaveRuleBefore = false;
					if(oFuncCheckItem.hasMatchedMixAndMatchRule() || oFuncCheckItem.isNewItemWithMixAndMatchRule()){
						// Roll back the item to original price level
						oFuncCheckItem.changePriceLevel(oFuncCheckItem.getCheckItem().getOriginalPriceLevel(), false);
						
						// Turn off the mix and match flag for new item
						oFuncCheckItem.setNewItemWithMixAndMatchRule(false);
						
						bHaveRuleBefore = true;
					}
					
					// Add checking to skip the item that override is applied
					if(!bHaveRuleBefore && oFuncCheckItem.getCheckItem().getOriginalPriceLevel() != oFuncCheckItem.getCheckItem().getPriceLevel()) {
						iItemIndex++;
						continue;
					}
					
					// Clear the mix and match relationship
					oFuncCheckItem.setMixAndMatchRuleId(0);
					oFuncCheckItem.cleanupMixAndMatchItemList();
					
					// Retrieve the possible rules by checking the cached rules parent item from the ordered item
					for(Entry<Integer, ArrayList<PosMixAndMatchItem>> entry:m_oMixAndMatchParentItemList.entrySet()){
						ArrayList<PosMixAndMatchItem> oPosMixAndMatchItemList = entry.getValue();
						int iRuleId = entry.getKey();
						for(PosMixAndMatchItem oPosMixAndMatchItem:oPosMixAndMatchItemList){
							boolean bMatch = false;
							if(oPosMixAndMatchItem.isMenuItem()){
								if(oPosMixAndMatchItem.getRecordId() == oFuncCheckItem.getMenuItemId()){
									bMatch = true;								
								}
							}else
							if(oPosMixAndMatchItem.isDepartment()){
								if(oPosMixAndMatchItem.getRecordId() == oFuncCheckItem.getMenuItem().getDeparmentId()){
									bMatch = true;
								}
							}else
							if(oPosMixAndMatchItem.isCategory()){
								if(oPosMixAndMatchItem.getRecordId() == oFuncCheckItem.getMenuItem().getCategoryId()){
									bMatch = true;
								}
							}
							
							if(bMatch){
								oRuleIdList.put(iRuleId, 1);
								if(oRuleParentCountList.containsKey(iRuleId)){
									int iCount = oRuleParentCountList.get(iRuleId);
									iCount++;
									oRuleParentCountList.put(iRuleId, iCount);
								}else{
									oRuleParentCountList.put(iRuleId, 1);
								}
							}
						}
					}
					
					oItemIdList.put(oFuncCheckItem.getMenuItemId(), 1);
					oDeptIdList.put(oFuncCheckItem.getMenuItem().getDeparmentId(), 1);
					oCatIdList.put(oFuncCheckItem.getMenuItem().getCategoryId(), 1);
					
					// Prepare the item and rule id pair list
					String sKey = StringLib.IntToStringWithLeadingZero(i, 3) + "_" + StringLib.IntToStringWithLeadingZero(j, 3) + "_" + StringLib.IntToStringWithLeadingZero(iItemIndex, 5);
					oItemMatchedRuleIdPairList.put(sKey, 0);
					
					iItemIndex++;
				}
			}
		}
		
		if(oRuleIdList.size() == 0){
			// No rule is matched
			for(int i = 0; i < oFuncCheck.getCheckPartyList().size(); i++) {
				for(int j = 0; j <= AppGlobal.MAX_SEATS; j++) {
					ArrayList<FuncCheckItem> oFuncCheckItemList = (ArrayList<FuncCheckItem>)oFuncCheck.getCheckPartyBySeq(i).getItemList(j);
					for(FuncCheckItem oFuncCheckItem:oFuncCheckItemList){
						
						// Skip set menu child item
						if(oFuncCheckItem.isSetMenuItem()){
							continue;
						}
	
						// Set modified flag if old item
						if(oFuncCheckItem.isOldItem()){
							oFuncCheckItem.getCheckItem().setModified(true);
						}
					}
				}
			}
			
			return;
		}
		
		for(PosMixAndMatchRule oMixAndMatchRule:m_oMixAndMatchRuleList){
			if(oRuleIdList.containsKey(oMixAndMatchRule.getRuleId()))
				oPostRuleIdList.put(oMixAndMatchRule.getRuleId());
		}
		for(Entry<Integer, Integer> entry:oItemIdList.entrySet())
			oPostItemIdList.put(entry.getKey());
		for(Entry<Integer, Integer> entry:oDeptIdList.entrySet())
			oPostDeptIdList.put(entry.getKey());
		for(Entry<Integer, Integer> entry:oCatIdList.entrySet())
			oPostCatIdList.put(entry.getKey());
		
		// Retrieve all matched rule items from HERO API
		PosMixAndMatchItemList oPosMixAndMatchItemList = new PosMixAndMatchItemList();
		oPosMixAndMatchItemList.readAllValidItems(oPostRuleIdList, oPostItemIdList, oPostDeptIdList, oPostCatIdList);
		HashMap<Integer, PosMixAndMatchItem>oResultItemList = oPosMixAndMatchItemList.getItemList();
		
		// Each rule's child list
		HashMap<Integer, ArrayList<PosMixAndMatchItem>> oMixAndMatchChildItemList = new HashMap<Integer, ArrayList<PosMixAndMatchItem>>();
		// Filtered rule id list
		ArrayList<Integer> oFilteredRuleList = new ArrayList<Integer>();
		
		// Check if all index (0, 1, ..., mamr_child_count of the rule) are existing from the result set
		for(int i=0; i<oPostRuleIdList.length(); i++) {
			try {
				int iRuleId = oPostRuleIdList.getInt(i);
				
				// Create a index mask for checking all index are existing or not
				int iChildCount = this.getRule(iRuleId).getMaxItemIndex();
				Integer[] iIndexMask = new Integer[iChildCount+1];
				for(int j=0; j<=iChildCount; j++)
					iIndexMask[j] = 0;
				
				ArrayList<PosMixAndMatchItem> oPosMixAndMatchList = new ArrayList<PosMixAndMatchItem>();
				
				for(Entry<Integer, PosMixAndMatchItem> entry:oResultItemList.entrySet()){
					PosMixAndMatchItem oPosMixAndMatchItem = entry.getValue();
					if(oPosMixAndMatchItem.getRuleId() == iRuleId){
						oPosMixAndMatchList.add(oPosMixAndMatchItem);
						if(oPosMixAndMatchItem.getIndex() <= iChildCount)
							iIndexMask[oPosMixAndMatchItem.getIndex()] = 1;
					}
				}
				
				// Check if all index are existing or not
				boolean bAllExist = true;
				for(int j=1; j<=iChildCount; j++){
					if(iIndexMask[j] == 0){
						bAllExist = false;
						break;
					}
				}
				
				if(bAllExist){
					// All indexes are existing, the rule is valid
					oMixAndMatchChildItemList.put(iRuleId, oPosMixAndMatchList);
					
					// Add the rule to filtered rules list
					oFilteredRuleList.add(iRuleId);
				}
				
			} catch (JSONException e) {
				AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", AppGlobal.stackToString(e));
			}
		}
		
		// Apply mix and match
		for(Integer iRuleId:oFilteredRuleList){
			
			PosMixAndMatchRule oPosMixAndMatchRule = this.getRule(iRuleId);
			ArrayList<PosMixAndMatchItem> oPosMixAndMatchList = oMixAndMatchChildItemList.get(iRuleId);
			
			// Because no. of parent in the item list = rule apply count, loop the item list to apply the rule
			int iCount = oRuleParentCountList.get(iRuleId);
			for(int i=0; i<iCount; i++){
				// Loop all matched rule items
				int iMatchCount = 0;
				
				// Create a index mask for checking all index are existing or not
				int iChildCount = oPosMixAndMatchRule.getMaxItemIndex();
				Integer[] iIndexMask = new Integer[iChildCount+1];
				for(int j=0; j<=iChildCount; j++)
					iIndexMask[j] = 0;
				
				for(PosMixAndMatchItem oPosMixAndMatchItem:oPosMixAndMatchList){
					for(Map.Entry<String, Integer>entry2:oItemMatchedRuleIdPairList.entrySet()){
						String sKey = entry2.getKey();
						String split[] = sKey.split("_");
						int iPartySeq = Integer.parseInt(split[0]);
						int iSeatNo = Integer.parseInt(split[1]);
						int iItemIndex = Integer.parseInt(split[2]);
						
						FuncCheckItem oFuncCheckItem = oFuncCheck.getCheckItem(iPartySeq, iSeatNo, iItemIndex);
						int iMatchedRuleId = entry2.getValue();
						
						if(iMatchedRuleId != 0)
							continue;
						
						if(oFuncCheckItem.getMenuItem() == null)
							continue;
						
						boolean bMatch = false;
						if(oPosMixAndMatchItem.isMenuItem()){
							if(oPosMixAndMatchItem.getRecordId() == oFuncCheckItem.getMenuItemId()){
								bMatch = true;
							}
						}else
						if(oPosMixAndMatchItem.isDepartment()){
							if(oPosMixAndMatchItem.getRecordId() == oFuncCheckItem.getMenuItem().getDeparmentId()){
								bMatch = true;
							}
						}else
						if(oPosMixAndMatchItem.isCategory()){
							if(oPosMixAndMatchItem.getRecordId() == oFuncCheckItem.getMenuItem().getCategoryId()){
								bMatch = true;
							}
						}
						if(bMatch){
							
							if(iIndexMask[oPosMixAndMatchItem.getIndex()] != 0)
								// Index is matched already, skip
								continue;
							
							// Turn on the index mask
							iIndexMask[oPosMixAndMatchItem.getIndex()] = 1;
							
							// Set the rule ID to the ordered item
							oItemMatchedRuleIdPairList.put(sKey, iRuleId);
							
							// Set the Mix and match item to the ordered item
							oItemMatchedMixAndMatchItemPairList.put(sKey, oPosMixAndMatchItem);
							
							iMatchCount++;
							break;
						}
					}
					if(iMatchCount == (oPosMixAndMatchRule.getMaxItemIndex()+1))
						break;
				}
				
				FuncCheckItem oMasterFuncCheckItem = null;
				ArrayList<FuncCheckItem> oSlaveFuncCheckItemList = new ArrayList<FuncCheckItem>();
				// All item in the rule is found in the item list
				if(iMatchCount == (oPosMixAndMatchRule.getMaxItemIndex()+1)){
					// All matched
					for(Map.Entry<String, Integer>entry2:oItemMatchedRuleIdPairList.entrySet()){
						int iMatchedRuleId = entry2.getValue();
						
						if(iMatchedRuleId == iRuleId){
							String sMatchedKey = entry2.getKey();
							String split2[] = sMatchedKey.split("_");
							int iMatchedPartySeq = Integer.parseInt(split2[0]);
							int iMatchedSeatNo = Integer.parseInt(split2[1]);
							int iMatchedItemIndex = Integer.parseInt(split2[2]);
							
							FuncCheckItem oMatchedFuncCheckItem = oFuncCheck.getCheckItem(iMatchedPartySeq, iMatchedSeatNo, iMatchedItemIndex);
							PosMixAndMatchItem oMatchedMixAndMatchItem = oItemMatchedMixAndMatchItemPairList.get(sMatchedKey);
							
							// Change item price
							if(oMatchedMixAndMatchItem.isChangeFixPrice() && oMatchedMixAndMatchItem.getFixAmount() != null){
//System.out.println("testtest1 ========f============ " + iRuleId + ", " + iMatchedSeatNo + "_" + iMatchedItemIndex + ", " + oMatchedFuncCheckItem.getItemDescriptionByIndex(2) + ", " + oMatchedMixAndMatchItem.getFixAmount().toPlainString());
								oMatchedFuncCheckItem.changeItemPrice(oMatchedMixAndMatchItem.getFixAmount());
							}else
							if(oMatchedMixAndMatchItem.isAddPrice() && oMatchedMixAndMatchItem.getFixAmount() != null){
//System.out.println("testtest1 ========a============ " + iRuleId + ", " + iMatchedSeatNo + "_" + iMatchedItemIndex + ", " + oMatchedFuncCheckItem.getItemDescriptionByIndex(2) + ", " + oMatchedMixAndMatchItem.getFixAmount().toPlainString());
								BigDecimal oOriginalPrice = oMatchedFuncCheckItem.getCheckItem().getOriginalPrice();
								oMatchedFuncCheckItem.changeItemPrice(oOriginalPrice.add(oMatchedMixAndMatchItem.getFixAmount()));
							}else
							if(oMatchedMixAndMatchItem.isChangePriceLevel() && oMatchedMixAndMatchItem.getPriceLevel() != null){
//System.out.println("testtest1 ========p============ " + iRuleId + ", " + iMatchedSeatNo + "_" + iMatchedItemIndex + ", " + oMatchedFuncCheckItem.getItemDescriptionByIndex(2) + ", " + oMatchedMixAndMatchItem.getPriceLevel());
								oMatchedFuncCheckItem.changePriceLevel(oMatchedMixAndMatchItem.getPriceLevel(), false);
							}else
							if(oMatchedMixAndMatchItem.isChangeByRate() && oMatchedMixAndMatchItem.getRate() != null){
//System.out.println("testtest1 ========d============ " + iRuleId + ", " + iMatchedSeatNo + "_" + iMatchedItemIndex + ", " + oMatchedFuncCheckItem.getItemDescriptionByIndex(2) + ", " + oMatchedMixAndMatchItem.getRate().toPlainString());
								BigDecimal oOriginalPrice = oMatchedFuncCheckItem.getCheckItem().getOriginalPrice();
								BigDecimal oAddedRateTotal = oOriginalPrice.multiply(oMatchedMixAndMatchItem.getRate());
								oMatchedFuncCheckItem.changeItemPrice(oOriginalPrice.add(oAddedRateTotal));
								//oMatchedFuncCheckItem.changeItemPrice(oOriginalPrice.multiply(oMatchedMixAndMatchItem.getRate()));
							}
							
							// Re-calculate all my modifiers' price
							oMatchedFuncCheckItem.setAllModifiersPrice(false);
							
							// Set the flag of mix and match for new item
							oMatchedFuncCheckItem.setNewItemWithMixAndMatchRule(true);
							
							// Mark the item is performed mix and match
							oItemMatchedRuleIdPairList.put(sMatchedKey, -1);
							
							// Set modified flag if old item
							if(oMatchedFuncCheckItem.isOldItem()){
								oMatchedFuncCheckItem.getCheckItem().setModified(true);
							}
							
							if(oMatchedMixAndMatchItem.getIndex() == 0)
								// Master Item
								oMasterFuncCheckItem = oMatchedFuncCheckItem;
							else
								oSlaveFuncCheckItemList.add(oMatchedFuncCheckItem);
						}
					}
					
					iMatchCount = 0;
				}else{
					// Not process, clear the flag
					for(Map.Entry<String, Integer>entry2:oItemMatchedRuleIdPairList.entrySet()){
						String sKey = entry2.getKey();
						int iMatchedRuleId = entry2.getValue();
						if(iMatchedRuleId == iRuleId){
							oItemMatchedRuleIdPairList.put(sKey, 0);
						}
					}
				}
				
				// Create the relationship of master item and slave item
				if(oMasterFuncCheckItem != null){
					oMasterFuncCheckItem.cleanupMixAndMatchItemList();
					for(FuncCheckItem oSlaveFuncCheckItem:oSlaveFuncCheckItemList){
						oMasterFuncCheckItem.addMixAndMatchItemToList(oSlaveFuncCheckItem);
					}
				}
			}
		}
		
		for(int i = 0; i < oFuncCheck.getCheckPartyList().size(); i++) {
			for(int j = 0; j <= AppGlobal.MAX_SEATS; j++) {
				ArrayList<FuncCheckItem> oFuncCheckItemList = (ArrayList<FuncCheckItem>)oFuncCheck.getCheckPartyBySeq(i).getItemList(j);
				for(FuncCheckItem oFuncCheckItem:oFuncCheckItemList){
					
					// Skip set menu child item
					if(oFuncCheckItem.isSetMenuItem()){
						continue;
					}
					
					// Set modified flag if old item
					if(oFuncCheckItem.isOldItem()){
						oFuncCheckItem.getCheckItem().setModified(true);
					}
				}
			}
		}
		
//System.out.println("----------------------------------------------------------------");
	}
	
	public void getMixAndMatchRule(FuncCheck oFuncCheck){
		m_sErrorMessage = "";
		
		HashMap<Integer, Integer>oRuleIdList = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer>oItemIdList = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer>oDeptIdList = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer>oCatIdList = new HashMap<Integer, Integer>();
		
		JSONArray oPostRuleIdList = new JSONArray();
		JSONArray oPostItemIdList = new JSONArray();
		JSONArray oPostDeptIdList = new JSONArray();
		JSONArray oPostCatIdList = new JSONArray();
		
		TreeMap<String, Integer>oItemMatchedRuleIdPairList = new TreeMap<String, Integer>();
		HashMap<String, PosMixAndMatchItem>oItemMatchedMixAndMatchItemPairList = new HashMap<String, PosMixAndMatchItem>();
		HashMap<Integer, Integer>oRuleParentCountList = new HashMap<Integer, Integer>();
		
		// Create master item id list
		HashMap<Integer, Integer> oPreviousMasterItemIdList = new HashMap<Integer, Integer>();
		for(int i = 0; i < oFuncCheck.getCheckPartyList().size(); i++) {
			for(int j = 0; j <= AppGlobal.MAX_SEATS; j++) {
				ArrayList<FuncCheckItem> oFuncCheckItemList = (ArrayList<FuncCheckItem>)oFuncCheck.getCheckPartyBySeq(i).getItemList(j);
				for(FuncCheckItem oFuncCheckItem:oFuncCheckItemList){
					if(oFuncCheckItem.getCheckItem().getMixAndMatchItemId() > 0){
						oPreviousMasterItemIdList.put(oFuncCheckItem.getCheckItem().getMixAndMatchItemId(), oFuncCheckItem.getCheckItem().getMixAndMatchItemId());
					}
				}
			}
		}
		
		
		// Create the rule ID list, item ID list, department ID list, category ID list
		for(int i = 0; i < oFuncCheck.getCheckPartyList().size(); i++) {
			for(int j = 0; j <= AppGlobal.MAX_SEATS; j++) {
				ArrayList<FuncCheckItem> oFuncCheckItemList = (ArrayList<FuncCheckItem>)oFuncCheck.getCheckPartyBySeq(i).getItemList(j);
				int iItemIndex = 0;
				for(FuncCheckItem oFuncCheckItem:oFuncCheckItemList){
					
					// Skip set menu child item
					if(oFuncCheckItem.isSetMenuItem()){
						iItemIndex++;
						continue;
					}
					
					// Check if item is deleted in database or not
					if(oFuncCheckItem.getMenuItem() == null){
						iItemIndex++;
						continue;
					}
					
					// Roll back the price of all items to original price
					/*oFuncCheckItem.changeItemPrice(oFuncCheckItem.getCheckItem().getOriginalPrice());
					
					// Roll back the price level of all items to original price level
					oFuncCheckItem.getCheckItem().setPriceLevel(oFuncCheckItem.getCheckItem().getOriginalPriceLevel());*/

					// Check if the item has rule before
					// If yes, continue the following process to retrieve the rule information
					// If no, skip the item
					if(oPreviousMasterItemIdList.containsKey(oFuncCheckItem.getCheckItem().getCitmId()) == false && oFuncCheckItem.hasMatchedMixAndMatchRule() == false){
						iItemIndex++;
						continue;
					}

					// Retrieve the possible rules by checking the cached rules parent item from the ordered item
					for(Entry<Integer, ArrayList<PosMixAndMatchItem>> entry:m_oMixAndMatchParentItemList.entrySet()){
						ArrayList<PosMixAndMatchItem> oPosMixAndMatchItemList = entry.getValue();
						int iRuleId = entry.getKey();
						for(PosMixAndMatchItem oPosMixAndMatchItem:oPosMixAndMatchItemList){
							boolean bMatch = false;
							if(oPosMixAndMatchItem.isMenuItem()){
								if(oPosMixAndMatchItem.getRecordId() == oFuncCheckItem.getMenuItemId()){
									bMatch = true;								
								}
							}else
							if(oPosMixAndMatchItem.isDepartment()){
								if(oPosMixAndMatchItem.getRecordId() == oFuncCheckItem.getMenuItem().getDeparmentId()){
									bMatch = true;
								}
							}else
							if(oPosMixAndMatchItem.isCategory()){
								if(oPosMixAndMatchItem.getRecordId() == oFuncCheckItem.getMenuItem().getCategoryId()){
									bMatch = true;
								}
							}
							
							if(bMatch){
								oRuleIdList.put(iRuleId, 1);
								if(oRuleParentCountList.containsKey(iRuleId)){
									int iCount = oRuleParentCountList.get(iRuleId);
									iCount++;
									oRuleParentCountList.put(iRuleId, iCount);
								}else{
									oRuleParentCountList.put(iRuleId, 1);
								}
							}
						}
					}
					
					oItemIdList.put(oFuncCheckItem.getMenuItemId(), 1);
					oDeptIdList.put(oFuncCheckItem.getMenuItem().getDeparmentId(), 1);
					oCatIdList.put(oFuncCheckItem.getMenuItem().getCategoryId(), 1);
					
					// Prepare the item and rule id pair list
					String sKey = StringLib.IntToStringWithLeadingZero(i, 3) + "_" + StringLib.IntToStringWithLeadingZero(j, 3) + "_" + StringLib.IntToStringWithLeadingZero(iItemIndex, 5);
					oItemMatchedRuleIdPairList.put(sKey, 0);
					
					iItemIndex++;
				}
			}
		}
				
		for(PosMixAndMatchRule oMixAndMatchRule:m_oMixAndMatchRuleList){
			if(oRuleIdList.containsKey(oMixAndMatchRule.getRuleId()))
				oPostRuleIdList.put(oMixAndMatchRule.getRuleId());
		}
		for(Entry<Integer, Integer> entry:oItemIdList.entrySet())
			oPostItemIdList.put(entry.getKey());
		for(Entry<Integer, Integer> entry:oDeptIdList.entrySet())
			oPostDeptIdList.put(entry.getKey());
		for(Entry<Integer, Integer> entry:oCatIdList.entrySet())
			oPostCatIdList.put(entry.getKey());
		
		// Retrieve all matched rule items from HERO API
		PosMixAndMatchItemList oPosMixAndMatchItemList = new PosMixAndMatchItemList();
		oPosMixAndMatchItemList.readAllValidItems(oPostRuleIdList, oPostItemIdList, oPostDeptIdList, oPostCatIdList);
		HashMap<Integer, PosMixAndMatchItem>oResultItemList = oPosMixAndMatchItemList.getItemList();
		
		// Each rule's child list
		HashMap<Integer, ArrayList<PosMixAndMatchItem>> oMixAndMatchChildItemList = new HashMap<Integer, ArrayList<PosMixAndMatchItem>>();
		// Filtered rule id list
		ArrayList<Integer> oFilteredRuleList = new ArrayList<Integer>();
		
		// Check if all index (0, 1, ..., mamr_child_count of the rule) are existing from the result set
		for(int i=0; i<oPostRuleIdList.length(); i++) {
			try {
				int iRuleId = oPostRuleIdList.getInt(i);
				
				// Create a index mask for checking all index are existing or not
				int iChildCount = this.getRule(iRuleId).getMaxItemIndex();
				Integer[] iIndexMask = new Integer[iChildCount+1];
				for(int j=0; j<=iChildCount; j++)
					iIndexMask[j] = 0;
				
				ArrayList<PosMixAndMatchItem> oPosMixAndMatchList = new ArrayList<PosMixAndMatchItem>();
				
				for(Entry<Integer, PosMixAndMatchItem> entry:oResultItemList.entrySet()){
					PosMixAndMatchItem oPosMixAndMatchItem = entry.getValue();
					if(oPosMixAndMatchItem.getRuleId() == iRuleId){
						oPosMixAndMatchList.add(oPosMixAndMatchItem);
						if(oPosMixAndMatchItem.getIndex() <= iChildCount)
							iIndexMask[oPosMixAndMatchItem.getIndex()] = 1;
					}
				}
				
				// Check if all index are existing or not
				boolean bAllExist = true;
				for(int j=1; j<=iChildCount; j++){
					if(iIndexMask[j] == 0){
						bAllExist = false;
						break;
					}
				}
				
				if(bAllExist){
					// All indexes are existing, the rule is valid
					oMixAndMatchChildItemList.put(iRuleId, oPosMixAndMatchList);
					
					// Add the rule to filtered rules list
					oFilteredRuleList.add(iRuleId);
				}
				
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		
		// Apply mix and match
		for(Integer iRuleId:oFilteredRuleList){
			
			PosMixAndMatchRule oPosMixAndMatchRule = this.getRule(iRuleId);
			ArrayList<PosMixAndMatchItem> oPosMixAndMatchList = oMixAndMatchChildItemList.get(iRuleId);
			
			// Because no. of parent in the item list = rule apply count, loop the item list to apply the rule
			int iCount = oRuleParentCountList.get(iRuleId);
			for(int i=0; i<iCount; i++){
				// Loop all matched rule items
				int iMatchCount = 0;
				
				// Create a index mask for checking all index are existing or not
				int iChildCount = oPosMixAndMatchRule.getMaxItemIndex();
				Integer[] iIndexMask = new Integer[iChildCount+1];
				for(int j=0; j<=iChildCount; j++)
					iIndexMask[j] = 0;
				
				for(PosMixAndMatchItem oPosMixAndMatchItem:oPosMixAndMatchList){
					for(Map.Entry<String, Integer>entry2:oItemMatchedRuleIdPairList.entrySet()){
						String sKey = entry2.getKey();
						String split[] = sKey.split("_");
						int iPartySeq = Integer.parseInt(split[0]);
						int iSeatNo = Integer.parseInt(split[1]);
						int iItemIndex = Integer.parseInt(split[2]);
						
						FuncCheckItem oFuncCheckItem = oFuncCheck.getCheckItem(iPartySeq, iSeatNo, iItemIndex);
						int iMatchedRuleId = entry2.getValue();
						
						if(iMatchedRuleId != 0)
							continue;
						
						if(oFuncCheckItem.getMenuItem() == null)
							continue;
						
						boolean bMatch = false;
						if(oPosMixAndMatchItem.isMenuItem()){
							if(oPosMixAndMatchItem.getRecordId() == oFuncCheckItem.getMenuItemId()){
								bMatch = true;
							}
						}else
						if(oPosMixAndMatchItem.isDepartment()){
							if(oPosMixAndMatchItem.getRecordId() == oFuncCheckItem.getMenuItem().getDeparmentId()){
								bMatch = true;
							}
						}else
						if(oPosMixAndMatchItem.isCategory()){
							if(oPosMixAndMatchItem.getRecordId() == oFuncCheckItem.getMenuItem().getCategoryId()){
								bMatch = true;
							}
						}
						if(bMatch){
							
							if(iIndexMask[oPosMixAndMatchItem.getIndex()] != 0)
								// Index is matched already, skip
								continue;
							
							// Turn on the index mask
							iIndexMask[oPosMixAndMatchItem.getIndex()] = 1;
							
							// Set the rule ID to the ordered item
							oItemMatchedRuleIdPairList.put(sKey, iRuleId);
							
							// Set the Mix and match item to the ordered item
							oItemMatchedMixAndMatchItemPairList.put(sKey, oPosMixAndMatchItem);
							
							iMatchCount++;
							break;
						}
					}
					if(iMatchCount == (oPosMixAndMatchRule.getMaxItemIndex()+1))
						break;
				}
				
				FuncCheckItem oMasterFuncCheckItem = null;
				ArrayList<FuncCheckItem> oSlaveFuncCheckItemList = new ArrayList<FuncCheckItem>();
				// All item in the rule is found in the item list
				if(iMatchCount == (oPosMixAndMatchRule.getMaxItemIndex()+1)){
					// All matched
					for(Map.Entry<String, Integer>entry2:oItemMatchedRuleIdPairList.entrySet()){
						int iMatchedRuleId = entry2.getValue();
						
						if(iMatchedRuleId == iRuleId){
							String sMatchedKey = entry2.getKey();
							String split2[] = sMatchedKey.split("_");
							int iMatchedPartySeq = Integer.parseInt(split2[0]);
							int iMatchedSeatNo = Integer.parseInt(split2[1]);
							int iMatchedItemIndex = Integer.parseInt(split2[2]);
							
							FuncCheckItem oMatchedFuncCheckItem = oFuncCheck.getCheckItem(iMatchedPartySeq, iMatchedSeatNo, iMatchedItemIndex);
							PosMixAndMatchItem oMatchedMixAndMatchItem = oItemMatchedMixAndMatchItemPairList.get(sMatchedKey);
							
							// Change item price
							/*if(oMatchedMixAndMatchItem.isChangeFixPrice() && oMatchedMixAndMatchItem.getFixAmount() != null){
								oMatchedFuncCheckItem.changeItemPrice(oMatchedMixAndMatchItem.getFixAmount());
							}else
							if(oMatchedMixAndMatchItem.isAddPrice() && oMatchedMixAndMatchItem.getFixAmount() != null){
								BigDecimal oOriginalPrice = oMatchedFuncCheckItem.getCheckItem().getOriginalPrice();
								oMatchedFuncCheckItem.changeItemPrice(oOriginalPrice.add(oMatchedMixAndMatchItem.getFixAmount()));
							}else
							if(oMatchedMixAndMatchItem.isChangePriceLevel() && oMatchedMixAndMatchItem.getPriceLevel() != null){
								oMatchedFuncCheckItem.changePriceLevel(oMatchedMixAndMatchItem.getPriceLevel());
							}else
							if(oMatchedMixAndMatchItem.isChangeByRate() && oMatchedMixAndMatchItem.getRate() != null){
								BigDecimal oOriginalPrice = oMatchedFuncCheckItem.getCheckItem().getOriginalPrice();
								BigDecimal oAddedRateTotal = oOriginalPrice.multiply(oMatchedMixAndMatchItem.getRate());
								oMatchedFuncCheckItem.changeItemPrice(oOriginalPrice.add(oAddedRateTotal));
							}*/
							
							// Mark the item is performed mix and match
							oItemMatchedRuleIdPairList.put(sMatchedKey, -1);
							
							if(oMatchedMixAndMatchItem.getIndex() == 0)
								// Master Item
								oMasterFuncCheckItem = oMatchedFuncCheckItem;
							else
								oSlaveFuncCheckItemList.add(oMatchedFuncCheckItem);
						}
					}
					
					iMatchCount = 0;
				}else{
					// Not process, clear the flag
					for(Map.Entry<String, Integer>entry2:oItemMatchedRuleIdPairList.entrySet()){
						String sKey = entry2.getKey();
						int iMatchedRuleId = entry2.getValue();
						if(iMatchedRuleId == iRuleId){
							oItemMatchedRuleIdPairList.put(sKey, 0);
						}
					}
				}
				
				// Create the relationship of master item and slave item
				if(oMasterFuncCheckItem != null){
					oMasterFuncCheckItem.setMixAndMatchRuleId(oPosMixAndMatchRule.getRuleId());
					
					oMasterFuncCheckItem.cleanupMixAndMatchItemList();
					for(FuncCheckItem oSlaveFuncCheckItem:oSlaveFuncCheckItemList){
						oMasterFuncCheckItem.addMixAndMatchItemToList(oSlaveFuncCheckItem);
					}
				}
			}
		}
	}

}
