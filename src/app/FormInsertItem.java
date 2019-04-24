package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import core.Controller;

import app.FrameInsertItem.CELL_TYPE;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

public class FormInsertItem extends VirtualUIForm implements FrameInsertItemListener{
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameInsertItem m_oFrameInsertItem;
	
	private FuncCheck m_oFuncCheck;
	private List<List<FuncCheckItem>> m_oPartyWholeItems;
	private List<List<HashMap<String, Integer>>> m_oItemsInfo;
	private HashMap<String, Integer> m_oSelItemInfo;
	
	public FormInsertItem(FuncCheck oFuncCheck, List<List<FuncCheckItem>> oPartyWholeItems, HashMap<String, Integer> oSelItemInfo, Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmInsertItem.xml");	
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Load form from template file
		m_oFrameInsertItem = new FrameInsertItem();
		m_oTemplateBuilder.buildFrame(m_oFrameInsertItem, "fraInsertItem");

		// Add listener
		m_oFrameInsertItem.addListener(this);
		this.attachChild(m_oFrameInsertItem);

		m_oFuncCheck = oFuncCheck;
		m_oSelItemInfo = oSelItemInfo;
		formIndexList(oPartyWholeItems);
	}

	private void formIndexList(List<List<FuncCheckItem>> oPartyWholeItems) {
		List<List<HashMap<String, Integer>>> oItemsInfo = new ArrayList<List<HashMap<String, Integer>>>();
		
		int iSeatNo = 0;
		for(List<FuncCheckItem> oItemListForSingleSeat:oPartyWholeItems) {
			String[] sSectionTitle = (iSeatNo == 0) ? AppGlobal.g_oLang.get()._("shared", "") : AppGlobal.g_oLang.get()._("seat", " ", iSeatNo);
			m_oFrameInsertItem.addSection(iSeatNo, sSectionTitle, true);
			
			int iItmIdx = 0;
			boolean bUnderParent = false;
			List<HashMap<String, Integer>> oSingleSeatItemsInfo = new ArrayList<HashMap<String, Integer>>();
			for(FuncCheckItem oFuncCheckItem:oItemListForSingleSeat) {
				// skip old item and child item under setMenu
				if (!oFuncCheckItem.isOldItem() && (!bUnderParent || !oFuncCheckItem.isSetMenuItem())) {					
					if (iSeatNo == m_oSelItemInfo.get("sectionNo") && iItmIdx == m_oSelItemInfo.get("itemIndex")) {
						// selected item
						String sCellTitle = ">>   " + oFuncCheckItem.getCheckItem().getName(AppGlobal.g_oCurrentLangIndex.get()) + "   <<";
						m_oFrameInsertItem.addItem(iSeatNo, m_oFrameInsertItem.getItemCount(iSeatNo), sCellTitle, CELL_TYPE.selected_item);
					}
					else
						// other items
						m_oFrameInsertItem.addItem(iSeatNo, m_oFrameInsertItem.getItemCount(iSeatNo), oFuncCheckItem.getCheckItem().getName(AppGlobal.g_oCurrentLangIndex.get()), CELL_TYPE.item);
					
					HashMap<String, Integer> oTempSelectedItem = new HashMap<String, Integer>();
					oTempSelectedItem.put("sectionNo", iSeatNo);
					oTempSelectedItem.put("itemIndex", iItmIdx);
					oSingleSeatItemsInfo.add(oTempSelectedItem);
					
					if(oFuncCheckItem.isSetMenu())
						bUnderParent = true;
					else if (!oFuncCheckItem.isSetMenu() && !oFuncCheckItem.isSetMenuItem())
						bUnderParent = false;
				}
				iItmIdx++;
			}
			m_oFrameInsertItem.addItem(iSeatNo, m_oFrameInsertItem.getItemCount(iSeatNo), AppGlobal.g_oLang.get()._("insert_here"), CELL_TYPE.insert_tab);
			// add the "insert here" tab at the end of the seat
			HashMap<String, Integer> oInsertTab = new HashMap<String, Integer>();
			oInsertTab.put("sectionNo", iSeatNo);
			oInsertTab.put("itemIndex", iItmIdx);
			oSingleSeatItemsInfo.add(oInsertTab);
			
			oItemsInfo.add(oSingleSeatItemsInfo);
			iSeatNo++;
		}
		
		m_oPartyWholeItems = oPartyWholeItems;
		m_oItemsInfo = oItemsInfo;
	}
	
	@Override
	public void frameInsertItem_selectedCell(int iSectionIdx, int iItemIdx) {
		// do nothing if press selected item
		if (iSectionIdx == m_oSelItemInfo.get("sectionNo") && m_oItemsInfo.get(iSectionIdx).get(iItemIdx).get("itemIndex") == m_oSelItemInfo.get("itemIndex"))
			return;
		// skip if press 1 below the selected item
		if (iItemIdx > 0 && iSectionIdx == m_oSelItemInfo.get("sectionNo") && m_oItemsInfo.get(iSectionIdx).get(iItemIdx-1).get("itemIndex") == m_oSelItemInfo.get("itemIndex")) {
			this.finishShow();
			return;
		}

		FuncCheckItem oFuncCheckItem = m_oFuncCheck.getCheckItem(m_oSelItemInfo.get("sectionNo"), m_oSelItemInfo.get("itemIndex"));
		// get the selected item info
		List<HashMap<String, Integer>> oRemoveItems = new ArrayList<HashMap<String,Integer>>();
		oRemoveItems.add(m_oSelItemInfo);
		// get the child items info if need
		if (!oFuncCheckItem.getChildItemList().isEmpty()) {
			boolean bSelItemFound = false;
			int iChildItemIndex = 0;
			int iChildSeatNo = oFuncCheckItem.getCheckItem().getSeatNo();
			// loop the seat items
			for(FuncCheckItem oChildFuncCheckItem:m_oFuncCheck.getItemList(iChildSeatNo)){
				// stop looping if finish scanning the selected setMenu
				if (oChildFuncCheckItem == oFuncCheckItem)
					bSelItemFound = true;
				else if (bSelItemFound && !oChildFuncCheckItem.isSetMenuItem())
					break;
				// only handle child item if under the setMenu
				if (oChildFuncCheckItem.isSetMenuItem() && (m_oSelItemInfo.get("itemIndex") < iChildItemIndex)) {
					for (FuncCheckItem oChildFuncCheckItem2: oFuncCheckItem.getChildItemList()) {
						if (oChildFuncCheckItem == oChildFuncCheckItem2) {
							HashMap<String, Integer> oChildItemInfo = new HashMap<String, Integer>();
							oChildItemInfo.put("sectionNo", iChildSeatNo);
							oChildItemInfo.put("itemIndex", iChildItemIndex);

							oRemoveItems.add(oChildItemInfo);
							break;
						}
					}
				}
				iChildItemIndex++;
			}
		}
		// find the items need to insert
		List<FuncCheckItem> oFuncCheckItems = new ArrayList<FuncCheckItem>();
		for (HashMap<String, Integer> oRemoveItemInfo : oRemoveItems) {
			oFuncCheckItems.add(m_oPartyWholeItems.get(oRemoveItemInfo.get("sectionNo")).get(oRemoveItemInfo.get("itemIndex")));
		}

		if (m_oSelItemInfo.get("sectionNo") == iSectionIdx) {
			// ***** if insert to same seat *****
			int iStartIdx = m_oSelItemInfo.get("itemIndex");
			int	iEndIdx =  m_oItemsInfo.get(iSectionIdx).get(iItemIdx).get("itemIndex");
			
			if (iEndIdx < iStartIdx) {
				// move up selected item
				iStartIdx += (oFuncCheckItems.size()-1);
				Collections.rotate(m_oPartyWholeItems.get(m_oSelItemInfo.get("sectionNo")).subList(iEndIdx, iStartIdx+1), oFuncCheckItems.size());
			}
			else {
				// move down selected item
				Collections.rotate(m_oPartyWholeItems.get(m_oSelItemInfo.get("sectionNo")).subList(iStartIdx, (iEndIdx-1)+1), -oFuncCheckItems.size());
			}
			
			// reset sequence
			m_oFuncCheck.resetItemSequenceForSeat(m_oSelItemInfo.get("sectionNo"));
		}
		else {
			// ***** if insert to different seat *****
			int iInsertItemIdx;		
			if (m_oItemsInfo.get(iSectionIdx).get(iItemIdx) != null)
				iInsertItemIdx = m_oItemsInfo.get(iSectionIdx).get(iItemIdx).get("itemIndex");
			else if (m_oItemsInfo.get(iSectionIdx).isEmpty())
				iInsertItemIdx = iItemIdx;
			else 
				iInsertItemIdx = m_oItemsInfo.get(iSectionIdx).get(iItemIdx-1).get("itemIndex") + 1;
			// insert selected items to target position
			m_oFuncCheck.insertItemToItemList(iSectionIdx, (iInsertItemIdx+1), oFuncCheckItems);
			// remove the selected items from original position
			int iCnt = 0;
			for (HashMap<String, Integer> oRemoveItemInfo : oRemoveItems) {
				m_oFuncCheck.removeItemFromList(oRemoveItemInfo.get("sectionNo"), oRemoveItemInfo.get("itemIndex")-iCnt);
				iCnt++;
			}
		}
		this.finishShow();
	}

	@Override
	public void frameInsertItem_clickCancel() {
		// Finish showing this form
		this.finishShow();
	}
}
