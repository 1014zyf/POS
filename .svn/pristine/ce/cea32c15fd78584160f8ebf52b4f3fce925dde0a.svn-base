package app;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import om.PosCheck;
import om.PosCheckExtraInfo;
import om.PosCustomType;
import om.PosCustomTypeList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import commonui.FormInputBox;
import core.Controller;
import externallib.StringLib;
import externallib.Util;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

/** interface for the listeners/observers callback method */
interface FormSplitTableListener {
	void formSplitTable_printCheck(List<FuncCheck> oUpdateCheckList);
}

public class FormSplitTable extends VirtualUIForm implements FrameSplitTableListener {

	TemplateBuilder m_oTemplateBuilder;
	
	private FrameSplitTable m_oFrameSplitTable;
	
	private HashMap<String, FuncCheck> m_oFuncCheckList;
	private HashMap<String, FuncCheck> m_oFuncCheckListForCalc;

	private HashMap<String, List<List<FuncSplitItem>>> m_oSplitItemList;

	//private int m_iMaxSeats;
	private boolean m_bExit;
	
	private String m_sFirstOldTable;
	
	private String m_osOrderingMode;
	
	String m_sBaseCheckTableNo;
	String m_sBaseCheckTableExt;
	
	private PosCustomTypeList m_oPosCustomTypeList;
	
	private static String SPLIT_TYPE_NEW_ITEM = "";
	private static String SPLIT_TYPE_MERGE_BACK = "m";
	private static String SPLIT_TYPE_SPLIT_FROM_ORIGINAL = "o";
	private static String SPLIT_TYPE_SPLIT_FROM_SPLIT = "s";
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FormSplitTableListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FormSplitTableListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FormSplitTableListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FormSplitTable(Controller oParentController, String sOrderingMode, PosCustomTypeList oPosCustomTypeList, String sBaseCheckTableNo, String sBaseCheckTableExt) {
		super(oParentController);
		
		m_sBaseCheckTableNo = sBaseCheckTableNo;
		m_sBaseCheckTableExt = sBaseCheckTableExt;
		
		m_oTemplateBuilder = new TemplateBuilder();
		m_bExit = false;
		m_sFirstOldTable = null;
		m_osOrderingMode = sOrderingMode;
		
		if (oPosCustomTypeList != null)
			m_oPosCustomTypeList = oPosCustomTypeList;
		else {
			m_oPosCustomTypeList = new PosCustomTypeList();
			m_oPosCustomTypeList.getCustomTypesByType(PosCustomType.TYPE_CHECK);
		}

		listeners = new ArrayList<FormSplitTableListener>();
		m_oSplitItemList = new HashMap<String, List<List<FuncSplitItem>>>();
		m_oFuncCheckList = new HashMap<String, FuncCheck>();
		m_oFuncCheckListForCalc = new HashMap<String, FuncCheck>();
		
		m_oTemplateBuilder.loadTemplate("frmSplitTable.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		oCoverFrame.setVisible(false);
		this.attachChild(oCoverFrame);
		
		m_oFrameSplitTable = new FrameSplitTable();
		m_oTemplateBuilder.buildFrame(m_oFrameSplitTable, "fraSplitTable");
		m_oFrameSplitTable.addListener(this);
		this.attachChild(m_oFrameSplitTable);
	}

	public void init(List<FuncCheck> oFuncCheckList) {
		// find max seat number and initialize sections
		int iMaxSeats = 20;
		boolean bCheckFirstOldCheck = false;
		for(FuncCheck oFuncCheck: oFuncCheckList) {
			m_oFrameSplitTable.addTableToHorizontalList(oFuncCheck.getTableNoWithTableName()[AppGlobal.g_oCurrentLangIndex.get()-1]);
			if(!bCheckFirstOldCheck && oFuncCheck.isOldCheck()){
				m_sFirstOldTable = oFuncCheck.getTableNoWithExtensionForDisplay();
				bCheckFirstOldCheck = true;
			}
			
			for(int i = 0; i <= 99; i++) {
				List<FuncCheckItem> oCheckItemList = oFuncCheck.getItemList(i);
				
				if(!oCheckItemList.isEmpty()) {
					if(i > iMaxSeats)
						iMaxSeats = i;
				}
			}
		}
		m_oFrameSplitTable.initSectionList(iMaxSeats);
		
		// count the number of checks selected to decide whether show the horizontal list on the top
		boolean bShowHorizontalList = false;
		if(oFuncCheckList.size() > 3)
			bShowHorizontalList = true;
		m_oFrameSplitTable.displayTableHorizontalList(bShowHorizontalList);
		
		
		initSplitItemList(oFuncCheckList, iMaxSeats);
		m_oFrameSplitTable.resetAllTableListButtonColor();
	}

	public void initSplitItemList(List<FuncCheck> oFuncCheckList, int iMaxSeats) {
		int iColumnIndex = 0;
		for(FuncCheck oFuncCheck: oFuncCheckList) {
			String sTable = oFuncCheck.getTableNoWithExtensionForDisplay();
			List<List<FuncSplitItem>> oWholeItemList = new ArrayList<List<FuncSplitItem>>();

			// add column view header (Table No.)
			if(iColumnIndex < 3)
				m_oFrameSplitTable.addTable(iColumnIndex, sTable, oFuncCheck.getTableNoWithTableName()[AppGlobal.g_oCurrentLangIndex.get()-1]);
			
			for(int i = 0; i <= iMaxSeats; i++) {
				List<FuncCheckItem> oCheckItemList = oFuncCheck.getItemList(i);
				List<FuncSplitItem> oSeatCheckItemList = new ArrayList<FuncSplitItem>();
				
				if(!oCheckItemList.isEmpty()) {
					for (int j = 0; j < oCheckItemList.size(); j++) {
						FuncCheckItem oFuncCheckItem = oCheckItemList.get(j);
						if(oFuncCheckItem.getCheckItem().isSetMenuChildItem())
							continue;
						
						FuncSplitItem oSplitItem = new FuncSplitItem(oFuncCheckItem, sTable);
						if(iColumnIndex < 3)
							m_oFrameSplitTable.addItemToColumnView(iColumnIndex, i, oFuncCheckItem);
						
						oSeatCheckItemList.add(oSplitItem);
					}
				}
				
				oWholeItemList.add(oSeatCheckItemList);
			}

			m_oFuncCheckList.put(oFuncCheck.getTableNoWithExtensionForDisplay(), oFuncCheck);
			m_oSplitItemList.put(sTable, oWholeItemList);
			m_oFrameSplitTable.addTable(sTable, oFuncCheck.getTableNoWithTableName()[AppGlobal.g_oCurrentLangIndex.get()-1]);
			iColumnIndex++;
		}
		
		// Create dummy check list for calculation
		int i = 1;
		AppThreadManager oAppThreadManager = new AppThreadManager();
		for(FuncCheck oFuncCheck: m_oFuncCheckList.values()) {
			FuncCheck oFuncCheck2 = new FuncCheck();
			oFuncCheck2.setCover(oFuncCheck.getCover(), true);
			oFuncCheck2.initBusinessDaySetup(AppGlobal.g_oFuncOutlet.get());
			
			// Add the method to the thread manager
			// Thread : lock table
			Object[] oParameters = new Object[11];
			oParameters[0] = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId();
			oParameters[1] = AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId();
			oParameters[2] = AppGlobal.g_oFuncOutlet.get().getShopId();
			oParameters[3] = AppGlobal.g_oFuncOutlet.get().getOutletId();
			oParameters[4] = oFuncCheck.getTableNo();
			oParameters[5] = oFuncCheck.getTableExtension();
			oParameters[6] = true;
			oParameters[7] = false;
			oParameters[8] = m_osOrderingMode;
			oParameters[9] = "";
			oParameters[10] = false;
		
			oAppThreadManager.addThread(i, oFuncCheck2, "lockTable", oParameters);
			m_oFuncCheckListForCalc.put(oFuncCheck.getTableNoWithExtensionForDisplay(), oFuncCheck2);
			i++;
		}
		
		oAppThreadManager.runThread();
	}

	public boolean splitItem(String sFromTable, int iFromSectionIndex, int iFromItemIndex, String sToTable, int iToSectionIndex, BigDecimal oQty, boolean bAskQty) {
		List<List<FuncSplitItem>> oToSplitItemList = m_oSplitItemList.get(sToTable);

		FuncSplitItem oFromSplitItem = this.getSplitItem(sFromTable, iFromSectionIndex, iFromItemIndex);
		if(oFromSplitItem == null)
			return false;
		
		if(oFromSplitItem.getFuncCheckItem().isMinimumChargeItem()) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("not_allow_to_split_minimum_charge_item"));
			oFormDialogBox.show();
			return false;
		}
		
		BigDecimal oSplitQty;
		if(oQty != null) {
			oSplitQty = oQty;
		} else {
			oSplitQty = oFromSplitItem.getQty();
			if(bAskQty) {
				while(true) {
					FormInputBox oFormInputBox = new FormInputBox(this);
					oFormInputBox.init();
					oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.NUMBER);
					oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("quantity"));
					oFormInputBox.setMessage(AppGlobal.g_oLang.get()._("please_input_quantity_to_split")+":");
					oFormInputBox.setDefaultInputValue(StringLib.BigDecimalToStringWithoutZeroDecimal(oSplitQty)); // Set default delete quantity
					oFormInputBox.show();
					
					String sNewQty = oFormInputBox.getInputValue();
					if(sNewQty == null)
						return false;
					
					try{
						Double.valueOf(sNewQty);
					}catch(NumberFormatException e){
						String sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(sErrMsg);
						oFormDialogBox.show();
						continue;
					}
		
					if((new BigDecimal(sNewQty)).compareTo(BigDecimal.ZERO) == 0){
						String sErrMsg = AppGlobal.g_oLang.get()._("cannot_input_zero_quantity");
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(sErrMsg);
						oFormDialogBox.show();
						continue;
					}
					
					if ((new BigDecimal(sNewQty)).compareTo(oSplitQty) > 0) {
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("only")+" "+ oSplitQty.stripTrailingZeros().toPlainString() + " " + AppGlobal.g_oLang.get()._("items_are_ordered"));
						oFormDialogBox.show();
						oFormDialogBox = null;
						continue;
					}
					
					oSplitQty = new BigDecimal(sNewQty);
					break;
				}
			}
		}
		
		FuncSplitItem oMatchSplitItem = null;
		String sSplitType = SPLIT_TYPE_NEW_ITEM;
		if(!oToSplitItemList.get(iToSectionIndex).isEmpty()) {
			for(FuncSplitItem oMergeSplitItem: oToSplitItemList.get(iToSectionIndex)) {
				if(oMergeSplitItem.isOldItem()) {
					if(!oFromSplitItem.isOldItem()
							&& oFromSplitItem.haveSameOriItem(oMergeSplitItem.getTable(), oMergeSplitItem.getSeatNo(), oMergeSplitItem.getSeq())) {
						sSplitType = SPLIT_TYPE_MERGE_BACK;
						oMatchSplitItem = oMergeSplitItem;
						break;
					}
				} else {
					if(oFromSplitItem.isOldItem()) {
						if(oMergeSplitItem.haveSameOriItem(oFromSplitItem.getTable(), oFromSplitItem.getSeatNo(), oFromSplitItem.getSeq())) {
							sSplitType = SPLIT_TYPE_SPLIT_FROM_ORIGINAL;
							oMatchSplitItem = oMergeSplitItem;
							break;
						}
					} else {
						if(oMergeSplitItem.haveSameOriItem(oFromSplitItem.getOriTable(), oFromSplitItem.getOriSeatNo(), oFromSplitItem.getOriSeq())) {
							sSplitType = SPLIT_TYPE_SPLIT_FROM_SPLIT;
							oMatchSplitItem = oMergeSplitItem;
							break;
						}
					}
				}
			}
		}
		
		// new item
		if(sSplitType.equals(SPLIT_TYPE_NEW_ITEM)) {
			FuncSplitItem oNewSplitItem = new FuncSplitItem(oFromSplitItem);
			oNewSplitItem.setTable(sToTable);
			oNewSplitItem.setSeatNo(iToSectionIndex);
			
			int iItemCount = oToSplitItemList.get(iToSectionIndex).size();
			int iNewSeq = 1;
			if(iItemCount > 0) {
				FuncSplitItem oSectionLastItem = oToSplitItemList.get(iToSectionIndex).get(iItemCount-1);

				iNewSeq = iItemCount+1;
				if(iNewSeq <= oSectionLastItem.getSeq())
					iNewSeq = oSectionLastItem.getSeq()+1;
			}
			
			oNewSplitItem.setSeq(iNewSeq);
			oNewSplitItem.setOriItemId(oFromSplitItem.getOriItemId());
			
			if(oFromSplitItem.isOldItem() && oFromSplitItem.isOldItemWithNoChange()) {
				oNewSplitItem.setOriSeatNo(oFromSplitItem.getSeatNo());
				oNewSplitItem.setOriSeq(oFromSplitItem.getSeq());
				oNewSplitItem.setOriTable(oFromSplitItem.getTable());
			} else {
				oNewSplitItem.setOriSeatNo(oFromSplitItem.getOriSeatNo());
				oNewSplitItem.setOriSeq(oFromSplitItem.getOriSeq());
				oNewSplitItem.setOriTable(oFromSplitItem.getOriTable());
			}
			oNewSplitItem.setQty(oSplitQty);
			oNewSplitItem.setTotal(oFromSplitItem.getOriTotal().multiply(oSplitQty).divide(oFromSplitItem.getOriQty(), 10, RoundingMode.HALF_UP));
			
			oToSplitItemList.get(iToSectionIndex).add(oNewSplitItem);
		} else { // have matched item  in target check, update qty
			oMatchSplitItem.setQty(oMatchSplitItem.getQty().add(oSplitQty));
			
			if(oMatchSplitItem.getQty().compareTo(oMatchSplitItem.getOriQty()) == 0) {
				oMatchSplitItem.setTotal(oMatchSplitItem.getOriTotal());
			} else {
				oMatchSplitItem.setTotal((oMatchSplitItem.getOriTotal().multiply(oMatchSplitItem.getQty())).divide(oMatchSplitItem.getOriQty(), 10, RoundingMode.HALF_UP));
			}
			
			if(oMatchSplitItem.isSplitted())
				oMatchSplitItem.setType(FuncSplitItem.TYPE_ORIGINAL);
		}
		
		// update the qty of item in original check
		if(oFromSplitItem.getQty().compareTo(oSplitQty) == 0) {
			oFromSplitItem.setQty(BigDecimal.ZERO);
			oFromSplitItem.setType(FuncSplitItem.TYPE_SPLITTED);
			oFromSplitItem.setTotal((oFromSplitItem.getOriTotal().multiply(oFromSplitItem.getQty())).divide(oFromSplitItem.getOriQty(), 10, RoundingMode.HALF_UP));
		} else {
			oFromSplitItem.setQty(oFromSplitItem.getQty().subtract(oSplitQty));
			oFromSplitItem.setTotal((oFromSplitItem.getOriTotal().multiply(oFromSplitItem.getQty())).divide(oFromSplitItem.getOriQty(), 10, RoundingMode.HALF_UP));
		}
		
		return true;
	}
	
	public void equalSplitItem(String sTable, int iFromSectionIndex, int iFromItemIndex, boolean bAskQty) {
		FuncSplitItem oFromSplitItem = getSplitItem(sTable, iFromSectionIndex, iFromItemIndex);
		
		if(!oFromSplitItem.getFuncCheckItem().getItemDiscountList().isEmpty()) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(oFromSplitItem.getName(AppGlobal.g_oCurrentLangIndex.get()) + " "
					+ AppGlobal.g_oLang.get()._("has_applied_item_discount") + System.lineSeparator()
					+ AppGlobal.g_oLang.get()._("not_allow_to_do_equal_split_on_item"));
			oFormDialogBox.show();
			oFormDialogBox = null;
			return;
		}

		BigDecimal oTotalCheck = new BigDecimal(Integer.toString(m_oSplitItemList.size()));
		BigDecimal oSplitQty = oFromSplitItem.getQty().divide(oTotalCheck, 10, RoundingMode.FLOOR);
		
		for(Entry<String, List<List<FuncSplitItem>>> entry: m_oSplitItemList.entrySet()) {
			String sTargetTable = entry.getKey();
			if(sTable.equals(sTargetTable)) // Same Table, no need to split
				continue;

			if(splitItem(sTable, iFromSectionIndex, iFromItemIndex, sTargetTable, iFromSectionIndex, oSplitQty, bAskQty)) {
				this.updateItemInfo(sTable);
				this.updateItemInfo(sTargetTable);
			}
		}
		
	}
	
	// get checks which have split items (ignore new check which have no new items)
	public HashMap<String, List<List<FuncSplitItem>>> getAvailableCheckList() {
		HashMap<String, List<List<FuncSplitItem>>> oUpdateTableSplitItemList = new HashMap<String, List<List<FuncSplitItem>>>();

		for(Entry<String, List<List<FuncSplitItem>>> entry: m_oSplitItemList.entrySet()) {
			List<List<FuncSplitItem>> oSplitItemList = entry.getValue();
			boolean bHaveSplittedItem = false;
			
			for(List<FuncSplitItem> oSectionSplitItemList: oSplitItemList) {
				for(FuncSplitItem oSplitItem: oSectionSplitItemList) {
					if(oSplitItem.isSplitted())
						continue;
					
					if(!oSplitItem.isOldItemWithNoChange()) {
						bHaveSplittedItem = true;
						break;
					}
				}
				
				if(bHaveSplittedItem)
					break;
			}
			
			FuncCheck oFuncCheck = m_oFuncCheckList.get(entry.getKey());
			if(!oFuncCheck.isOldCheck()) {
				if(!bHaveSplittedItem) {
					oFuncCheck.unlockTable(false, false);
					
					String sErrMsg = AppGlobal.g_oLang.get()._("table") + " "
							+ oFuncCheck.getTableNoWithExtensionForDisplay() + " "
							+ AppGlobal.g_oLang.get()._("has_no_split_item") + System.lineSeparator()
							+ AppGlobal.g_oLang.get()._("cancel_to_create_this_table");
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
					oFormDialogBox.setMessage(sErrMsg);
					oFormDialogBox.show();
					oFormDialogBox = null;
					
					continue;
				}
			}
			
			//Determine to add extra info for auto function or not, referencing support_for_split_check
			if (AppGlobal.g_oFuncStation.get().getCheckAutoFunctionsOption() && !oFuncCheck.isCheckExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_AUTO_FUNCTIONS, PosCheckExtraInfo.VARIABLE_FINISH, 0))
				oFuncCheck.addCheckExtraInfo(PosCheckExtraInfo.BY_CHECK, PosCheckExtraInfo.SECTION_AUTO_FUNCTIONS, PosCheckExtraInfo.VARIABLE_FINISH, 0, PosCheckExtraInfo.VALUE_FALSE);
			
			oUpdateTableSplitItemList.put(entry.getKey(), oSplitItemList);
		}
		
		return oUpdateTableSplitItemList;
	}

	// Send Check
	public HashMap<String, List<List<FuncSplitItem>>> sendSplitChecks() {
		HashMap<String, List<List<FuncSplitItem>>> oUpdateTableSplitItemList = getAvailableCheckList();
		
		if(!this.updateSplitItem(m_oFuncCheckList, oUpdateTableSplitItemList))
			return null;
		
		return oUpdateTableSplitItemList;
	}

	// update the splitted / new added items
	public boolean updateSplitItem(HashMap<String, FuncCheck> oFuncCheckList, HashMap<String, List<List<FuncSplitItem>>> oResultSplitItemList) {
		
		HashMap<FuncCheck, HashMap<FuncCheck, List<FuncCheckItem>>> oSplitTablePairItemIndexList = new HashMap<FuncCheck, HashMap<FuncCheck, List<FuncCheckItem>>>();
		HashMap<String, List<String>> oTableRemoveItemList = new HashMap<String, List<String>>();
		HashMap<String, List<String>> oTableAddItemList = new HashMap<String, List<String>>();
		ArrayList<HashMap<String, HashMap<String, String>>> oItemsInfo = new ArrayList<HashMap<String, HashMap<String, String>>>();
		
		for(Entry<String, List<List<FuncSplitItem>>> entry: oResultSplitItemList.entrySet()) {
			oTableRemoveItemList.put(entry.getKey(), new ArrayList<String>());
			oTableAddItemList.put(entry.getKey(), new ArrayList<String>());
		}
		
		//update Cover
		int iNewTableCovers = 0;
		if(AppGlobal.g_oFuncStation.get().getSplitTableWithKeepingCover() == false){
			for(Entry<String, List<List<FuncSplitItem>>> entry: oResultSplitItemList.entrySet()) {
				String sTable = entry.getKey();
				FuncCheck oFuncCheck = oFuncCheckList.get(sTable);
				List<List<FuncSplitItem>> oWholeSplitItemList = entry.getValue();
				if(!oFuncCheck.isOldCheck() && oWholeSplitItemList.size() > 0)
					iNewTableCovers += oFuncCheck.getCover();
				else
					continue;
			}
		}
		
		//iState:	0: for split destination checks; 
		//			1: for base check 
		for(int iState = 0; iState < 2; iState++) {
			for(Entry<String, List<List<FuncSplitItem>>> entry: oResultSplitItemList.entrySet()) {
				
				if(iState == 0 && entry.getKey().compareTo(m_sBaseCheckTableNo + m_sBaseCheckTableExt) == 0
					|| iState == 1 && entry.getKey().compareTo(m_sBaseCheckTableNo + m_sBaseCheckTableExt) != 0)
					continue;
				
				String sTable = entry.getKey();
				FuncCheck oFuncCheck = oFuncCheckList.get(sTable);
				//update first old table cover
				if(sTable.equals(m_sFirstOldTable) && iNewTableCovers != 0){
					int iCover = oFuncCheck.getCover();
					if(iCover - iNewTableCovers <= 0)
						iCover = 0;
					else
						iCover = iCover - iNewTableCovers;
					oFuncCheck.setCover(iCover, true);
				}
				
				List<List<FuncSplitItem>> oWholeSplitItemList = entry.getValue();
				
				HashMap<FuncCheck, List<FuncCheckItem>> oTargetTableItemList = new HashMap<FuncCheck, List<FuncCheckItem>>();
				
				for(List<FuncSplitItem> oSplitItemList: oWholeSplitItemList) {
					if(oSplitItemList.isEmpty())
						continue;
					
					for(FuncSplitItem oSplitItem: oSplitItemList) {
						if(!oSplitItem.isOriginal())
							continue;
						
						if(!oSplitItem.isOldItem()) {
							FuncSplitItem oOriSplitItem = null;
							
							for(FuncSplitItem oSplitItem1: oResultSplitItemList.get(oSplitItem.getOriTable()).get(oSplitItem.getOriSeatNo())) {
								if(oSplitItem1.getOriSeq() == oSplitItem.getOriSeq()) {
									oOriSplitItem = oSplitItem1;
									break;
								}
							}
							
							if(oOriSplitItem == null)
								continue;
	
							FuncCheck oOriFuncCheck = oFuncCheckList.get(oSplitItem.getOriTable());
							List<FuncCheckItem> oNewItemList = new ArrayList<FuncCheckItem>();
							if(oOriSplitItem.isSplitted()) { // All quantity of item split to other table, change check id
								List<String> oRemoveItemList = oTableRemoveItemList.get(oSplitItem.getOriTable());
	
								// Child item
								FuncCheckItem oOriCheckItem = this.getCheckOriginalItem(oOriFuncCheck, oOriSplitItem.getSeatNo(), oOriSplitItem.getSeq());
								
								HashMap<String, HashMap<String, String>> oItemInfo = new HashMap<String, HashMap<String, String>>();
								HashMap<String, String> oOriItemInfo = new HashMap<String, String>();
								
								oOriItemInfo.put("ItemQuantity", "0");
								oOriItemInfo.put("ItemOriginalQuantity", oOriCheckItem.getCheckItem().getQty().toPlainString());
								oOriItemInfo.put("ItemId", oOriCheckItem.getCheckItem().getCitmId());
								oOriItemInfo.put("CheckId", oOriCheckItem.getCheckItem().getCheckId());
								oOriItemInfo.put("ItemCode", oOriCheckItem.getCheckItem().getCode());
								oOriItemInfo.put("ItemName", oOriCheckItem.getCheckItem().getName(AppGlobal.g_oCurrentLangIndex.get()));
								for(int i = 1 ; i <= 5 ; i++){
									oOriItemInfo.put("ItemNameL"+i, oOriCheckItem.getCheckItem().getName(i));
								}
								
								oItemInfo.put("SpliteOriItemInfo", oOriItemInfo);
								oItemsInfo.add(oItemInfo);
								
								FuncCheckItem oNewCheckItem = new FuncCheckItem(oOriCheckItem, null);
								oNewCheckItem.internalChangeQty(oSplitItem.getQty());
								oNewCheckItem.changeCheckAndCheckPartyId(oFuncCheck.getCheckId(), "");
								oNewCheckItem.getCheckItem().setModified(true);
								
								int iCurrentItemCount = oFuncCheck.getItemListCount(oSplitItem.getSeatNo());
								oFuncCheck.addItemToItemList(oSplitItem.getSeatNo(), iCurrentItemCount+1, oNewCheckItem);
								if(!oSplitItem.getTable().equals(oSplitItem.getOriTable())) {
									int iItemCount = oFuncCheck.getItemList(oSplitItem.getSeatNo()).size();
									FuncCheckItem oAddedFuncCheckItem = oFuncCheck.getItemList(oSplitItem.getSeatNo()).get(iItemCount-1);
									oNewItemList.add(oAddedFuncCheckItem);
								}
								
								for (FuncCheckItem oChildFuncCheckItem: oOriCheckItem.getChildItemList()) {
									int iChildSeatNo = oChildFuncCheckItem.getCheckItem().getSeatNo();
									int iChildItemIndex = 0;
									for(FuncCheckItem oChildFuncCheckItem2:oOriFuncCheck.getItemList(iChildSeatNo)){
										if(oChildFuncCheckItem == oChildFuncCheckItem2){
											FuncCheckItem oNewChildCheckItem = new FuncCheckItem(oChildFuncCheckItem2, oNewCheckItem);
											oNewChildCheckItem.internalChangeQty(oNewChildCheckItem.getCheckItem().getBaseQty().multiply(oSplitItem.getQty()));
											oNewChildCheckItem.changeCheckAndCheckPartyId(oFuncCheck.getCheckId(), "");
											oNewChildCheckItem.getCheckItem().setModified(true);
											
											iCurrentItemCount = oFuncCheck.getItemListCount(oSplitItem.getSeatNo());
											oFuncCheck.addItemToItemList(oSplitItem.getSeatNo(), iCurrentItemCount+1, oNewChildCheckItem);
											if(!oSplitItem.getTable().equals(oSplitItem.getOriTable())) {
												int iItemCount = oFuncCheck.getItemList(oSplitItem.getSeatNo()).size();
												FuncCheckItem oAddedChildFuncCheckItem = oFuncCheck.getItemList(oSplitItem.getSeatNo()).get(iItemCount-1);
												oNewItemList.add(oAddedChildFuncCheckItem);
											}
											
											String sKey = StringLib.IntToStringWithLeadingZero(iChildSeatNo, 3) + "_" + StringLib.IntToStringWithLeadingZero(iChildItemIndex, 5);
											oRemoveItemList.add(sKey);
											
											break;
										}
										iChildItemIndex++;
									}
								}
	
								String sKey = StringLib.IntToStringWithLeadingZero(oSplitItem.getOriSeatNo(), 3) + "_" + StringLib.IntToStringWithLeadingZero((oSplitItem.getOriSeq()-1), 5);
								oRemoveItemList.add(sKey);
								oOriSplitItem.setType(FuncSplitItem.TYPE_REPLACED);
							}else { // partial split, add new item with related split quantity
								// Original item
								int iSeatNo = oSplitItem.getSeatNo();
								BigDecimal dNewQty = oSplitItem.getQty();
								FuncCheckItem oOriCheckItem = this.getCheckOriginalItem(oOriFuncCheck, oOriSplitItem.getSeatNo(), oOriSplitItem.getSeq());
								BigDecimal dSplitRatio = dNewQty.divide(oSplitItem.getOriQty(), 4, RoundingMode.HALF_UP);
								
								
								// Clone a new item
								FuncCheckItem oNewFuncCheckItem = new FuncCheckItem(oOriCheckItem, null);
								oNewFuncCheckItem.prepareParamOfModiOldQtyForSplit();
								oNewFuncCheckItem.resetAsNewItem();
								
								oNewFuncCheckItem.remainingPendingInfoIsExistAndSplitByRatio(dSplitRatio);
								
								// Set the quantity
								oNewFuncCheckItem.internalChangeQty(dNewQty);
								oNewFuncCheckItem.setSplitFromCheckId(oNewFuncCheckItem.getCheckItem().getCheckId()); // Store the check id split from
								oNewFuncCheckItem.changeCheckAndCheckPartyId(oFuncCheck.getCheckId(), "");
								
								// Add to item list with given seat no.
								int iCurrentItemCount = oFuncCheck.getItemListCount(iSeatNo);
								oFuncCheck.addItemToItemList(oSplitItem.getSeatNo(), iCurrentItemCount+1, oNewFuncCheckItem);
								if(!oSplitItem.getTable().equals(oSplitItem.getOriTable())) {
									int iItemCount = oFuncCheck.getItemList(oSplitItem.getSeatNo()).size();
									FuncCheckItem oAddedFuncCheckItem = oFuncCheck.getItemList(oSplitItem.getSeatNo()).get(iItemCount-1);
									oAddedFuncCheckItem.setSplitItemParentItemId(oSplitItem.getOriItemId());
									oNewItemList.add(oAddedFuncCheckItem);
								}
								
								// Process child item
								for (FuncCheckItem oOriginalChildFuncCheckItem: oOriCheckItem.getChildItemList()) {
									// Clone a new item
									FuncCheckItem oNewChildFuncCheckItem = new FuncCheckItem(oOriginalChildFuncCheckItem, oNewFuncCheckItem);
									oNewChildFuncCheckItem.resetAsNewItem();
									
									// Set the quantity
									oNewChildFuncCheckItem.internalChangeQty(oNewChildFuncCheckItem.getCheckItem().getBaseQty().multiply(dNewQty));
									oNewChildFuncCheckItem.changeCheckAndCheckPartyId(oFuncCheck.getCheckId(), "");
									oNewChildFuncCheckItem.remainingPendingInfoIsExistAndSplitByRatio(dSplitRatio);
									
									// Add to item list with given seat no.
									iCurrentItemCount = oFuncCheck.getItemListCount(iSeatNo);
									oFuncCheck.addItemToItemList(iSeatNo, iCurrentItemCount+1, oNewChildFuncCheckItem);
									if(!oSplitItem.getTable().equals(oSplitItem.getOriTable())) {
										int iItemCount = oFuncCheck.getItemList(oSplitItem.getSeatNo()).size();
										FuncCheckItem oAddedChildFuncCheckItem = oFuncCheck.getItemList(oSplitItem.getSeatNo()).get(iItemCount-1);
										oNewItemList.add(oAddedChildFuncCheckItem);
									}
								}
							}
							
							if(!oTargetTableItemList.containsKey(oOriFuncCheck))
								oTargetTableItemList.put(oOriFuncCheck, new ArrayList<FuncCheckItem>());
							
							for(FuncCheckItem oNewCheckItem: oNewItemList) {
								oTargetTableItemList.get(oOriFuncCheck).add(oNewCheckItem);
							}
						} else {  // item stay at original table, has not been splitted, update its quantity
							if(!oSplitItem.isOldItemWithNoChange()) {
								BigDecimal dNewQty = oSplitItem.getQty();
								BigDecimal dSplitRatio = dNewQty.divide(oSplitItem.getOriQty(), 4, RoundingMode.HALF_UP);
								FuncCheckItem oFuncCheckItem = this.getCheckOriginalItem(oFuncCheck, oSplitItem.getSeatNo(), oSplitItem.getSeq());
								
								if (oSplitItem.getOriQty().compareTo(oSplitItem.getQty()) != 0) {
									oFuncCheckItem.internalChangeQty(oSplitItem.getQty());
									HashMap<String, HashMap<String, String>> oItemInfo = new HashMap<String, HashMap<String, String>>();
									HashMap<String, String> oOriItemInfo = new HashMap<String, String>();
									
									oOriItemInfo.put("ItemOriginalItemId", oSplitItem.getOriItemId());
									oOriItemInfo.put("ItemOriginalQuantity", oSplitItem.getOriQty().toString());
									oOriItemInfo.put("ItemQuantity", oSplitItem.getQty().toString());
									oOriItemInfo.put("ItemId", oSplitItem.getFuncCheckItem().getCheckItem().getCitmId());
									oOriItemInfo.put("CheckId", oSplitItem.getFuncCheckItem().getCheckItem().getCheckId());
									oOriItemInfo.put("ItemCode", oSplitItem.getFuncCheckItem().getCheckItem().getCode());
									oOriItemInfo.put("ItemName", oSplitItem.getFuncCheckItem().getCheckItem().getName(AppGlobal.g_oCurrentLangIndex.get()));
									for(int i = 1 ; i <= 5 ; i++){
										oOriItemInfo.put("ItemNameL"+i, oSplitItem.getFuncCheckItem().getCheckItem().getName(i));
									}
									
									oItemInfo.put("SpliteOriItemInfo", oOriItemInfo);
									oItemsInfo.add(oItemInfo);
									for(FuncCheckItem oChildFuncCheckItem: oFuncCheckItem.getChildItemList()) {
										oChildFuncCheckItem.internalChangeQty(oChildFuncCheckItem.getCheckItem().getBaseQty().multiply(dNewQty));
										oChildFuncCheckItem.getCheckItem().setModified(true);
										oChildFuncCheckItem.remainingPendingInfoIsExistAndSplitByRatio(dSplitRatio);
									}
								}
								oFuncCheckItem.getCheckItem().setModified(true);
								oFuncCheckItem.remainingPendingInfoIsExistAndSplitByRatio(dSplitRatio);
							}
						}
					}
				}
				oSplitTablePairItemIndexList.put(oFuncCheck, oTargetTableItemList);
			}
		}

		// Remove items which have already splitted to other check
		for(Entry<String, List<String>> entry: oTableRemoveItemList.entrySet()) {
			FuncCheck oFuncCheck = oFuncCheckList.get(entry.getKey());
			List<String> oRemoveItemList = entry.getValue();

			Collections.sort(oRemoveItemList);
			Collections.reverse(oRemoveItemList);

			for(String sSortedString: oRemoveItemList) {
				int iSlashIndex = sSortedString.indexOf("_");
				int iSectionId = Integer.parseInt(sSortedString.substring(0, iSlashIndex));
				int iItemIndex = Integer.parseInt(sSortedString.substring(iSlashIndex+1));
				
				oFuncCheck.removeItemFromList(iSectionId, iItemIndex);
			}
		}
		
		// Get the continuous printing flag
		boolean isContPrint = true;
		if (isContPrint){
			for(Entry<String, List<List<FuncSplitItem>>> entry: oResultSplitItemList.entrySet()) {
				FuncCheck oFuncCheck = oFuncCheckList.get(entry.getKey());
				// Set disallow for continuous print (original check and target check)
				oFuncCheck.setAllowContinuousPrint(FuncCheck.NOT_ALLOW_CONTINUOUS_PRINT);
			}
		}
		
		// Get the processing time
		DateTime oCurrentTime = AppGlobal.getCurrentTime(false);
		
		// Create thread to update old check first
		AppThreadManager oAppThreadManager = new AppThreadManager();
		int iCount = 0;
		for(Entry<String, List<List<FuncSplitItem>>> entry: oResultSplitItemList.entrySet()) {
			FuncCheck oFuncCheck = oFuncCheckList.get(entry.getKey());
			if(!oFuncCheck.isOldCheck())
				continue;

			// Perform mix and match
			if(AppGlobal.g_oFuncMixAndMatch.get().isSupportMixAndMatch()){
				AppGlobal.g_oFuncMixAndMatch.get().processMixAndMatch(oFuncCheck);
			}
			
			// Add the method to the thread manager
			Object[] oParameters = new Object[5];
			oParameters[0] = AppGlobal.FUNC_LIST.split_table.name();
			oParameters[1] = oSplitTablePairItemIndexList.get(oFuncCheck);
			oParameters[2] = oFuncCheckList.get(m_sBaseCheckTableNo + m_sBaseCheckTableExt);
			oParameters[3] = oCurrentTime;
			oParameters[4] = oItemsInfo;
			oAppThreadManager.addThread(iCount+1, oFuncCheck, "updateSplitItems", oParameters);
			iCount++;
		}
		// Run all of the threads
		oAppThreadManager.runThread();
		oAppThreadManager.waitForThread();

		for(int j = 1; j <= iCount; j++) {
			boolean bResult = (boolean) oAppThreadManager.getResult(j);
			if(!bResult)
				return false;
		}
		
		// Send new check
		// Since the check number get from check number field in pos_station, can't use threading to handle multiple send check, otherwise the check number will be wrong
		int iNewCheckCount = 0;
		for(Entry<String, List<List<FuncSplitItem>>> entry: oResultSplitItemList.entrySet()) {
			FuncCheck oFuncCheck = oFuncCheckList.get(entry.getKey());
			if(oFuncCheck.isOldCheck())
				continue;
			
			iNewCheckCount++;
			
			// Perform mix and match
			if(AppGlobal.g_oFuncMixAndMatch.get().isSupportMixAndMatch()){
				AppGlobal.g_oFuncMixAndMatch.get().processMixAndMatch(oFuncCheck);
			}
			
			// *** For all new checks, the open time MUST NOT the same because of the duplicate send check checking
			// *** So, the open time is incremented by 1 second for each check
			if(!oFuncCheck.updateSplitItems(AppGlobal.FUNC_LIST.split_table.name(), oSplitTablePairItemIndexList.get(oFuncCheck), oFuncCheckList.get(m_sBaseCheckTableNo + m_sBaseCheckTableExt), oCurrentTime.plusSeconds(iNewCheckCount), oItemsInfo))
				return false;
		}
		
		return true;
	}
	
	public FuncCheckItem getCheckOriginalItem(FuncCheck oFuncCheck, int iSeatNo, int iSeq) {
		FuncCheckItem oOriginalCheckItem = null;
		for(FuncCheckItem oFuncCheckItem: oFuncCheck.getItemList(iSeatNo)) {
			if(oFuncCheckItem.getCheckItem().getSeq() == iSeq) {
				oOriginalCheckItem = oFuncCheckItem;
				break;
			}
		}
		return oOriginalCheckItem;
	}
	
	public FuncSplitItem getSplitItem(String sTable, int iSectionIndex, int iItemIndex) {
		if(!m_oSplitItemList.containsKey(sTable))
			return null;
		
		List<List<FuncSplitItem>> oWholeSplitItemList = m_oSplitItemList.get(sTable);
		if(iSectionIndex > oWholeSplitItemList.size())
			return null;
		
		List<FuncSplitItem> oSectionSplitItemList = oWholeSplitItemList.get(iSectionIndex);
		if(iItemIndex > oSectionSplitItemList.size())
			return null;
		
		int iIndex = 0;
		FuncSplitItem oResultSplitItem = null;
		for(FuncSplitItem oSplitItem: oSectionSplitItemList) {
			if(oSplitItem.isSplitted())
				continue;
			
			if (iIndex == iItemIndex) {
				oResultSplitItem = oSplitItem;
				break;
			}
			
			iIndex++;
		}
		return oResultSplitItem;
	}

	public void updateItemInfo(String sTable) {
		int iColumnIndex = m_oFrameSplitTable.getColumnIndex(sTable);
		m_oFrameSplitTable.removeColumnViewAllItem(iColumnIndex);

		for(List<FuncSplitItem> oSplitItemList: getSplitItemListByTable(sTable)) {
			if(oSplitItemList.isEmpty())
				continue;
			
			for(FuncSplitItem oSplitItem: oSplitItemList) {
				if(oSplitItem.isSplitted())
					continue;
				
				m_oFrameSplitTable.addItemToColumnView(iColumnIndex, oSplitItem.getSeatNo(), oSplitItem.getFuncCheckItem());
			}
		}
	}
	
	public void showCheckDetail(String sTable) {
		FuncCheck oFuncCheck = m_oFuncCheckListForCalc.get(sTable);
		List<List<FuncSplitItem>> oSplitItemList = m_oSplitItemList.get(sTable);
		DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm:ss");

		for(List<FuncCheckItem> oCheckItemList: oFuncCheck.getWholeItemList()) {
			oCheckItemList.clear();
		}

		BigDecimal dOldItemCount = BigDecimal.ZERO;
		BigDecimal dNewItemCount = BigDecimal.ZERO;
		for(List<FuncSplitItem> oSplitItemListInSeat: oSplitItemList) {
			if(oSplitItemListInSeat.isEmpty())
				continue;
			
			for(FuncSplitItem oSplitItem: oSplitItemListInSeat) {
				if(oSplitItem.isSplitted())
					continue;
				
				if(oSplitItem.isOldItem())
					dOldItemCount = dOldItemCount.add(oSplitItem.getQty());
				else
					dNewItemCount = dNewItemCount.add(oSplitItem.getQty());
			}
		}
		
		this.calcTargetTable(sTable);
		
		//update cover
		String sCover = String.valueOf(oFuncCheck.getCover());
		if(AppGlobal.g_oFuncStation.get().getSplitTableWithKeepingCover() == false){
			if(sTable.equals(m_sFirstOldTable)){
				String sTempCover = this.getNewCover();
				if(!sTempCover.isEmpty())
					sCover = sTempCover;
			}
		}
		
		FormCheckDetailInfo oFormCheckDetailInfo = new FormCheckDetailInfo(oFuncCheck.isOldCheck(), this);
		oFormCheckDetailInfo.setTitle(AppGlobal.g_oLang.get()._("table_detail"));
		
		String sCheckTable = oFuncCheck.getTableNo();
		
		if(oFuncCheck.getTableExtension() != null && !oFuncCheck.getTableExtension().isEmpty())
			sCheckTable += oFuncCheck.getTableExtension();
		
		String[] sTableName = oFuncCheck.getTableNoWithTableName();
		String sTableRef = oFuncCheck.getCheckExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_TABLE_INFORMATION,
				PosCheckExtraInfo.VARIABLE_TABLE_REFERENCE, 0);
		if (m_osOrderingMode.equals(PosCheck.ORDERING_MODE_BAR_TAB) && sTableRef != null && !sTableRef.isEmpty())
			sTableName = StringLib.appendStringArray(sTableName, " (", sTableRef, ")");
		oFormCheckDetailInfo.setTable(sTableName);
		oFormCheckDetailInfo.setCover(sCover);
		
		// Get check type
		if(oFuncCheck.getCustomTypeId() != 0) {
			if(m_oPosCustomTypeList.getTypeList().size() > 0){
				PosCustomType oPosCustomType = m_oPosCustomTypeList.getTypeList().get(oFuncCheck.getCustomTypeId());
				if(oPosCustomType.getShortName(AppGlobal.g_oCurrentLangIndex.get()).isEmpty())
					oFormCheckDetailInfo.setCheckType(oPosCustomType.getNameArray()[AppGlobal.g_oCurrentLangIndex.get()-1]);
			}
		}
		
		if(oFuncCheck.isOldCheck()) {
			oFormCheckDetailInfo.setCheckNumber(oFuncCheck.getCheckPrefixNo());
			oFormCheckDetailInfo.setOpenEmployee(oFuncCheck.getOpenUserName());
			oFormCheckDetailInfo.setOpenTime(timeFormatter.print(oFuncCheck.getOpenLocTime()));
		} else
			oFormCheckDetailInfo.setCheckNumber("--"+AppGlobal.g_oLang.get()._("new")+"--");
		
		if(oFuncCheck.hasMember()) {
			oFormCheckDetailInfo.setCheckMember(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, oFuncCheck.getMemberDisplayName()), oFuncCheck.getMemberNumber());
		}else{
			// For interface member
			String sMemberName = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NAME);
			String sEnglishName = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_ENGLISH_NAME);
			String sMemberNumber = oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER);
			
			String sDisplayName = sMemberName;
			if (sDisplayName == null || sDisplayName.isEmpty())
				sDisplayName = sEnglishName;
			else if (sEnglishName != null && !sEnglishName.isEmpty())
				sDisplayName += ", " + sEnglishName;
			
			if(sDisplayName !=null && sMemberNumber != null && !sDisplayName.isEmpty() && !sMemberNumber.isEmpty())
				oFormCheckDetailInfo.setCheckMember(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, sDisplayName), sMemberNumber);
		}
		
		if(!oFuncCheck.getCheckBusinessPeriodId().isEmpty())
			oFormCheckDetailInfo.setMealPeriod(AppGlobal.g_oFuncOutlet.get().getBusinessPeriodById(oFuncCheck.getCheckBusinessPeriodId()).getName(AppGlobal.g_oCurrentLangIndex.get()));
		else 
			oFormCheckDetailInfo.setMealPeriod(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getName(AppGlobal.g_oCurrentLangIndex.get()));
		
		if(!oFuncCheck.getCheckBusinessPeriodId().isEmpty())
			oFormCheckDetailInfo.setMealPeriod(AppGlobal.g_oFuncOutlet.get().getBusinessPeriodById(oFuncCheck.getCheckBusinessPeriodId()).getName(AppGlobal.g_oCurrentLangIndex.get()));
		else 
			oFormCheckDetailInfo.setMealPeriod(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getName(AppGlobal.g_oCurrentLangIndex.get()));
		
		oFormCheckDetailInfo.setPrintCount(String.valueOf(oFuncCheck.getPrintCount()));
		oFormCheckDetailInfo.setItemCount(dOldItemCount.stripTrailingZeros().toPlainString());
		oFormCheckDetailInfo.setNewItemCount(dNewItemCount.stripTrailingZeros().toPlainString());
		oFormCheckDetailInfo.setGratuityTotal(AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToString(oFuncCheck.getGratuityTotal()));
		if(AppGlobal.g_oFuncStation.get().getSeparateInclusiveTaxOnDisplay()){
			BigDecimal dTaxTotal = new BigDecimal(AppGlobal.g_oFuncOutlet.get().roundTaxAmountToString(oFuncCheck.getTaxTotal().add(oFuncCheck.getInclusiveTaxTotal())));
			BigDecimal dInclusiveTax = new BigDecimal(AppGlobal.g_oFuncOutlet.get().roundTaxAmountToString(oFuncCheck.getInclusiveTaxTotal()));
			BigDecimal dSubTotal = new BigDecimal(AppGlobal.g_oFuncOutlet.get().roundItemAmountToString(oFuncCheck.getNetItemTotal().subtract(oFuncCheck.getInclusiveTaxTotal())));
			BigDecimal dOriSubTotal = new BigDecimal(AppGlobal.g_oFuncOutlet.get().roundItemAmountToString(oFuncCheck.getNetItemTotal()));
			
			oFormCheckDetailInfo.setTaxTotal(AppGlobal.g_oFuncOutlet.get().roundTaxAmountToString(dTaxTotal));
			
			BigDecimal dTmpRound = dOriSubTotal.subtract(dInclusiveTax).subtract(dSubTotal).add(oFuncCheck.getRoundAmount());
			
			BigDecimal oCheckTaxScRefTotal = oFuncCheck.getCheckTaxScRefTotal();
			BigDecimal dScTotal = oFuncCheck.getServiceChargeTotal();
			if (oCheckTaxScRefTotal.compareTo(BigDecimal.ZERO) > 0) {
				dSubTotal = dSubTotal.add(oCheckTaxScRefTotal);
				dScTotal = dScTotal.subtract(oCheckTaxScRefTotal);
			}
			oFormCheckDetailInfo.setSubTotal(AppGlobal.g_oFuncOutlet.get().roundItemAmountToString(dSubTotal));
			oFormCheckDetailInfo.setSCTotal(AppGlobal.g_oFuncOutlet.get().roundSCAmountToString(dScTotal));
			
			if(dTmpRound.compareTo(BigDecimal.ZERO) == 0)
				oFormCheckDetailInfo.setRoundAmount(Util.HERORound(dTmpRound, AppGlobal.g_oFuncOutlet.get().getCheckRoundMethod(), AppGlobal.g_oFuncOutlet.get().getCheckRoundDecimal()).toPlainString());
			else
				oFormCheckDetailInfo.setRoundAmount(dTmpRound.stripTrailingZeros().toPlainString());
		}else{
			oFormCheckDetailInfo.setSCTotal(AppGlobal.g_oFuncOutlet.get().roundSCAmountToString(oFuncCheck.getServiceChargeTotal()));
			oFormCheckDetailInfo.setSubTotal(AppGlobal.g_oFuncOutlet.get().roundItemAmountToString(oFuncCheck.getNetItemTotal()));
			oFormCheckDetailInfo.setTaxTotal(AppGlobal.g_oFuncOutlet.get().roundTaxAmountToString(oFuncCheck.getTaxTotal()));
			if(oFuncCheck.getRoundAmount().compareTo(BigDecimal.ZERO) == 0)
				oFormCheckDetailInfo.setRoundAmount(Util.HERORound(oFuncCheck.getRoundAmount(), AppGlobal.g_oFuncOutlet.get().getCheckRoundMethod(), AppGlobal.g_oFuncOutlet.get().getCheckRoundDecimal()).toPlainString());
			else
				oFormCheckDetailInfo.setRoundAmount(oFuncCheck.getRoundAmount().stripTrailingZeros().toPlainString());
		}
		oFormCheckDetailInfo.setCheckTotal(AppGlobal.g_oFuncOutlet.get().roundCheckAmountToString(oFuncCheck.getCheckTotal()));
		oFormCheckDetailInfo.setDiscountTotal(AppGlobal.g_oFuncOutlet.get().roundDiscAmountToString(oFuncCheck.getCheckDiscountTotal().add(oFuncCheck.getItemDiscountTotal())));
		
		oFormCheckDetailInfo.show();
	}

	public void calcTargetTable(String sTable) {
		FuncCheck oFuncCheck = m_oFuncCheckListForCalc.get(sTable);
		List<List<FuncSplitItem>> oWholeSplitItemList = m_oSplitItemList.get(sTable);

		for(List<FuncSplitItem> oSplitItemList: oWholeSplitItemList) {
			if(oSplitItemList.isEmpty())
				continue;
			
			for(FuncSplitItem oSplitItem: oSplitItemList) {
				if(oSplitItem.isSplitted())
					continue;
				
				int iCurrentItemCount = oFuncCheck.getItemListCount(oSplitItem.getSeatNo());
				FuncCheckItem oFuncCheckItem = new FuncCheckItem(oSplitItem.getFuncCheckItem(), null);
				oFuncCheck.addItemToItemList(oSplitItem.getSeatNo(), iCurrentItemCount+1, oFuncCheckItem);
				
				if(oSplitItem.getFuncCheckItem().hasChildItem()) {
					for(FuncCheckItem oChildFuncCheckItem: oSplitItem.getFuncCheckItem().getChildItemList()) {
						FuncCheckItem oChildFuncCheckItem2 = new FuncCheckItem(oChildFuncCheckItem, oFuncCheckItem);
						iCurrentItemCount = oFuncCheck.getItemListCount(oSplitItem.getSeatNo());
						oFuncCheck.addItemToItemList(oSplitItem.getSeatNo(), iCurrentItemCount+1, oChildFuncCheckItem2);
					}
				}
			}
		}
		
		oFuncCheck.calcCheck();
	}
	
	public List<List<FuncSplitItem>> getSplitItemListByTable(String sTable) {
		return m_oSplitItemList.get(sTable);
	}
	
	public boolean isExit() {
		return this.m_bExit;
	}
	
	public void unlockAllTables() {
		for(FuncCheck oFuncCheck: m_oFuncCheckList.values()) {
			oFuncCheck.unlockTable(true, false);
		}
	}
	
	@Override
	public void fraSplitTable_clickExit() {
		m_bExit = true;
		this.finishShow();
	}

	@Override
	public void fraSplitTable_clickColumnHeader(String sTable, boolean bShowCheckDetail) {
		if(bShowCheckDetail)
			this.showCheckDetail(sTable);
		else
			this.updateItemInfo(sTable);
	}

	@Override
	public void fraSplitTable_equalSplitItem(String sTable, int iSectionId,
			int iItemIndex, boolean bAskQty) {
		this.equalSplitItem(sTable, iSectionId, iItemIndex, bAskQty);
	}

	@Override
	public void frameCheckFunction_splitItem(String sFromTable,
			int iFromSectionIndex, int iFromItemIndex, String sToTable,
			int iTargetSectionIndex, BigDecimal oQty, boolean bAskQty) {
		if(splitItem(sFromTable, iFromSectionIndex, iFromItemIndex, sToTable, iTargetSectionIndex, oQty, bAskQty)) {
			this.updateItemInfo(sFromTable);
			this.updateItemInfo(sToTable);
		}
		//instantly update check
		for(FuncCheck oFuncCheck: m_oFuncCheckListForCalc.values()) {
			for(List<FuncCheckItem> oCheckItemList: oFuncCheck.getWholeItemList()) {
				oCheckItemList.clear();
			}
		}
		for(String sTable: m_oSplitItemList.keySet()) {
			this.calcTargetTable(sTable);
		}
	}

	@Override
	public void fraSplitTable_sendCheck() {
		if(sendSplitChecks() != null)
			this.unlockAllTables();
		
		this.finishShow();
	}

	@Override
	public void fraSplitTable_printCheck() {
		ArrayList<Integer> iHeaderFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sHeaderFieldValues = new ArrayList<String>();

		for(FuncCheck oFuncCheck: m_oFuncCheckListForCalc.values()) {
			FuncCheck oOriginalCheck = m_oFuncCheckList.get(oFuncCheck.getTableNoWithExtensionForDisplay());
			oFuncCheck.setCover(oOriginalCheck.getCover(), true);
			for(List<FuncCheckItem> oCheckItemList: oFuncCheck.getWholeItemList()) {
				oCheckItemList.clear();
			}
		}

		for(String sTable: m_oSplitItemList.keySet()) {
			this.calcTargetTable(sTable);
		}
		
		iHeaderFieldWidths.add(183);
		sHeaderFieldValues.add(AppGlobal.g_oLang.get()._("check_no"));
		iHeaderFieldWidths.add(151);
		sHeaderFieldValues.add(AppGlobal.g_oLang.get()._("table"));
		iHeaderFieldWidths.add(366);
		sHeaderFieldValues.add(AppGlobal.g_oLang.get()._("cover"));
		iHeaderFieldWidths.add(260);
		sHeaderFieldValues.add(AppGlobal.g_oLang.get()._("total"));
		
		List<String> oSelectionCheckList = new ArrayList<String>();
		FormCommonBasketSelection oFormCommonBasketSelection = new FormCommonBasketSelection(this);
		oFormCommonBasketSelection.initWithMultiSelection(AppGlobal.g_oLang.get()._("select_tables_to_print_check"), iHeaderFieldWidths, sHeaderFieldValues);
		int iItemIndex = 0;
		
		for(String sTable: m_oFrameSplitTable.getTableList()) { // according to the order of table list to display check information
			FuncCheck oFuncCheck = m_oFuncCheckListForCalc.get(sTable);
			
			String sCheckPrefixNo = "--"+AppGlobal.g_oLang.get()._("new")+"--";
			if(oFuncCheck.isOldCheck())
				sCheckPrefixNo = oFuncCheck.getCheckPrefixNo();
			String sCover = Integer.toString(oFuncCheck.getCover());
			String sTotal = StringLib.BigDecimalToString(oFuncCheck.getCheckTotal(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getCheckDecimal());

			//update cover
			if(AppGlobal.g_oFuncStation.get().getSplitTableWithKeepingCover() == false){
				if(sTable.equals(m_sFirstOldTable)){
					String sTempCover = this.getNewCover();
					if(!sTempCover.isEmpty())
						sCover = sTempCover;
				}
			}
			
			ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
			ArrayList<String> sFieldValues = new ArrayList<String>();
			ArrayList<String> sFieldAligns = new ArrayList<String>();
			ArrayList<String> sFieldTypes = new ArrayList<String>();
			
			iFieldWidths.add(183);
			sFieldValues.add(sCheckPrefixNo);
			sFieldAligns.add("");
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			iFieldWidths.add(151);
			sFieldValues.add(oFuncCheck.getTableNoWithTableName()[AppGlobal.g_oCurrentLangIndex.get()-1]);
			sFieldAligns.add("");
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			iFieldWidths.add(366);
			sFieldValues.add(sCover);
			sFieldAligns.add("");
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			iFieldWidths.add(260);
			sFieldValues.add(sTotal);
			sFieldAligns.add("");
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			oFormCommonBasketSelection.addItem(iItemIndex, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
			oSelectionCheckList.add(oFuncCheck.getTableNoWithExtensionForDisplay());
			iItemIndex++;
		}
		oFormCommonBasketSelection.show();

		if (oFormCommonBasketSelection.isUserCancel())
			return;

		// Send Check
		HashMap<String, List<List<FuncSplitItem>>> oSplitItemList = sendSplitChecks();
		if(oSplitItemList == null) {
			this.finishShow();
			return;
		}

		/*for(FuncCheck oFuncCheck: m_oFuncCheckList.values()) {
			if(!oSplitItemList.containsKey(oFuncCheck.getTableNoWithExtensionForDisplay()))
				continue;
			
			oFuncCheck.lockTable(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId(), oFuncCheck.getTableNo(), oFuncCheck.getTableExtension(), true, false);
		}*/
		
		
		// Get selected checks to ask user for confirmation
		List<Integer> oSelectionBoxResult = oFormCommonBasketSelection.getResultList();
		List<FuncCheck> oCheckListForPrint = new ArrayList<FuncCheck>();
		for(Integer iSelectedIndex: oSelectionBoxResult) {
			FuncCheck oFuncCheck = m_oFuncCheckList.get(oSelectionCheckList.get(iSelectedIndex.intValue()));
			
			if(!oSplitItemList.containsKey(oFuncCheck.getTableNoWithExtensionForDisplay()))
				continue;

			FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
			String sMessage = AppGlobal.g_oLang.get()._("ready_to_print_the_following_check") + "?"
					+ System.lineSeparator() + AppGlobal.g_oLang.get()._("check_no") + ": "
					+ oFuncCheck.getCheckPrefixNo() + System.lineSeparator() + AppGlobal.g_oLang.get()._("table_no")
					+ ": " + oFuncCheck.getTableNoWithTableName()[AppGlobal.g_oCurrentLangIndex.get() - 1];
			oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormConfirmBox.setMessage(sMessage);
			oFormConfirmBox.show();
			
			if(oFormConfirmBox.isOKClicked() == false) {
				oFuncCheck.unlockTable(false, false);
				continue;
			}
			
			oCheckListForPrint.add(oFuncCheck);
		}

		if(!oCheckListForPrint.isEmpty()) {
			for(FormSplitTableListener listener: listeners) {
				listener.formSplitTable_printCheck(oCheckListForPrint);
			}
		}

		this.unlockAllTables();
		this.finishShow();
	}
	
	private String getNewCover(){
		//get first old check available cover
		int iNewTableCovers = 0;
		String sCover = "";		
		for(String sTable: m_oFrameSplitTable.getTableList()){
			FuncCheck oFuncCheck = m_oFuncCheckListForCalc.get(sTable);
			if(!oFuncCheck.isOldCheck() && oFuncCheck.getCheckTotal().compareTo(BigDecimal.ZERO) > 0)
				iNewTableCovers += oFuncCheck.getCover();
			else
				continue;
		}
		
		for(String sTable: m_oFrameSplitTable.getTableList()) {
			FuncCheck oFuncCheck = m_oFuncCheckListForCalc.get(sTable);
			if(sTable.equals(m_sFirstOldTable) && iNewTableCovers != 0){
				int iCover = oFuncCheck.getCover();
				if(iCover - iNewTableCovers <= 0)
					iCover = 0;
				else
					iCover = iCover - iNewTableCovers;
				sCover = Integer.toString(iCover);
			}
		}
		
		return sCover;
	}
}
