package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import core.Controller;

import om.MenuItem;
import om.PosActionLog;
import om.PosOutletItem;
import om.PosOutletItemList;
import om.PosStockTransaction;
import om.PosStockTransactionList;

import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormStockOperation extends VirtualUIForm implements FrameStockOperationListener, FrameStockEditSequenceListener {

	TemplateBuilder m_oTemplateBuilder;
		
	private FrameStockOperation m_oFrameStockOperation;
	private FrameStockEditSequence m_oFrameStockEditSequence;
	VirtualUIFrame m_oFrameCover;
	
	private TreeMap<Integer, Integer> m_oOperatingPairs;					//Use this as current pairs
	private TreeMap<Integer, Integer> m_oSeqItemIDPairs;					//Keep original pairs
	private TreeMap<Integer, Integer> m_oSeqEditPairs;						//Check any seq change
	
	private TreeMap<Integer, PosOutletItem> m_oItemIdOutletItemPairs;		//outlet item pairs
	private TreeMap<Integer, MenuItem> m_oItemIDMenuItemPairs;				//Store stock pairs
	private TreeMap<Integer, BigDecimal[]> m_oItemIDStockValuesPairs;		//Store stock values of MenuItem
	private TreeMap<Integer, BigDecimal[]> m_oItemIDModifiedValuesPairs;	//Store modified values of MenuItem
	private TreeMap<Integer, Integer> m_oSearchResultMenuItemIDPairs;		//Store search result pairs
	private List<Integer> m_oDeleteMenuItemIDs;								//Store MenuItem IDs to be deleted
	private TreeMap<Integer, List<PosStockTransaction>> m_oItemIDTransactionsPairs;		//For updating records
	private TreeMap<Integer, String> m_oItemIDCloseBalanceTransactionIDPairs;		//Store the MenuItem ID and Close Balance transaction ID 
	
	private MenuItem m_oCurrentEditMenuItem;
	private int m_iCurrentItemListSection;
	private int m_iCurrentItemListItemIndex;
	private int m_iCurrentItemListFieldIndex;
	private int m_iCurrentPage;
	private int m_iTotalPage;
	private PosOutletItemList m_oPosOutletItemList;
	
	private FuncCheck m_oFuncCheck;
	
	private boolean m_bAskItem;
	private boolean m_bAskItemSearchByPanel;
	private List<Integer> m_oSearchMenuItemIdList;
	
	private String m_sCurrentSearchKeyWord;
	private int m_iCurrentSelectDepartmentId;
	private int m_iCurrentSelectCategoryId;
	
	public final static String SEARCH_MODE_SHOW_ALL = "show_all";
	public final static String SEARCH_MODE_BY_NAME = "by_name";
	public final static String SEARCH_MODE_BY_PANEL = "by_panel";
	public final static String OPERATION_BY_ADD_ITEM = "add_item";
	
//KingsleyKwan20171016StockOperationByJack		-----Start-----
	// m_iMaxItemPerPage = 11 for horizontal view
	// m_iMaxItemPerPage = 16 for mobile view
	private int m_iMaxItemPerPage;
//KingsleyKwan20171016StockOperationByJack		----- End -----
	
	public FormStockOperation(Controller oParentController){
		super(oParentController);
		
		m_oOperatingPairs = new TreeMap<Integer, Integer>();
		m_oSeqItemIDPairs = new TreeMap<Integer, Integer>();
		m_oSeqEditPairs = new TreeMap<Integer, Integer>();
		m_oItemIdOutletItemPairs = new TreeMap<Integer, PosOutletItem>();
		m_oItemIDMenuItemPairs = new TreeMap<Integer, MenuItem>();
		m_oItemIDStockValuesPairs = new TreeMap<Integer, BigDecimal[]>();
		m_oItemIDModifiedValuesPairs = new TreeMap<Integer, BigDecimal[]>();
		m_oSearchResultMenuItemIDPairs = new TreeMap<Integer, Integer>();
		m_oDeleteMenuItemIDs = new ArrayList<Integer>();
		m_oItemIDTransactionsPairs = new TreeMap<Integer, List<PosStockTransaction>>();
		m_oItemIDCloseBalanceTransactionIDPairs = new TreeMap<Integer, String>();
		m_oCurrentEditMenuItem = null;
		m_oPosOutletItemList = new PosOutletItemList();
		m_oFuncCheck = new FuncCheck();
		
		// Init as -ve
		m_iCurrentItemListSection = -1;
		m_iCurrentItemListItemIndex = -1;
		m_iCurrentItemListFieldIndex = -1;
		m_iCurrentPage = 0;
		m_iTotalPage = 0;
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_iMaxItemPerPage = 16;
		else
			m_iMaxItemPerPage = 11;
		
		m_bAskItem = false;
		m_bAskItemSearchByPanel = false;
		
		m_sCurrentSearchKeyWord = "";
		m_iCurrentSelectDepartmentId = 0;
		m_iCurrentSelectCategoryId = 0;
		m_oSearchMenuItemIdList = null;
	}
	
	public boolean init(){	
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmStockOperation.xml");
		
		// Background Cover Page
		m_oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCover, "fraCoverFrame");
		if (!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oFrameCover.setVisible(false);
		this.attachChild(m_oFrameCover);
		
		m_oFrameStockOperation = new FrameStockOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameStockOperation, "fraStockOperation");
		m_oFrameStockOperation.addListener(this);
		this.attachChild(m_oFrameStockOperation);
		
		m_oFrameStockEditSequence = new FrameStockEditSequence();
		m_oTemplateBuilder.buildFrame(m_oFrameStockEditSequence, "fraStockEditSequence");
		m_oFrameStockEditSequence.addListener(this);
		m_oFrameStockEditSequence.setVisible(false);
		this.attachChild(m_oFrameStockEditSequence);
//KingsleyKwan20171016StockOperationByJack		-----Start-----
		// Load stock
		loadStockRecord();
		return true;
//KingsleyKwan20171016StockOperationByJack		----- End -----
	}
	
	private void loadStockRecord(){
		// Clear the previous result
		m_oOperatingPairs.clear();
		m_oItemIDMenuItemPairs.clear();
		
		//Get Stock MenuItem that had been ordered during current business date
		m_oOperatingPairs = createMenuItemStockPairs();
		
		if(!m_oOperatingPairs.isEmpty()) {
			//Calculate page number
			m_iTotalPage = calculateTotalPageOfPairs(m_oOperatingPairs);
			
			if(m_iTotalPage >= 1)
				m_iCurrentPage = 1;
			else if(m_iTotalPage <= 0)
				return;
			
			loadStockTransactionsAtPage(1);
		}
		m_oCurrentEditMenuItem = null;
		updatePageButtonsVisibility();
	}
	
	private TreeMap<Integer, Integer> createMenuItemStockPairs() {
		ArrayList<Integer> oMenuItemIds = new ArrayList<Integer>();
		m_oSeqItemIDPairs.clear();
		m_oItemIDMenuItemPairs.clear();
		m_oItemIdOutletItemPairs.clear();
		
		// *** NOT LOAD THE ORDERED ITEMS
		// Get the item that had been ordered during current business date
		// Add a record to stock
		//PosCheckItemList oPosCheckItemList = new PosCheckItemList();
		//oMenuItemIds = oPosCheckItemList.getAllDistinctMenuItemIdByBusinessDayIdShopIdOutletId(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId());

		// Get the item that with stock
		// Load current stock
		m_oFuncCheck = new FuncCheck();
		m_oFuncCheck.getCurrentItemStockList(AppGlobal.g_oFuncOutlet.get().getOutletId(), PosOutletItem.CHECK_STOCK_YES, PosOutletItem.SOLDOUT_YES);
		
		TreeMap<Integer, Integer> oMissingSeqPairs = new TreeMap<Integer, Integer>();
		for(PosOutletItem oOutletItem: m_oFuncCheck.getItemStockList()) {
			if(oOutletItem.isCheckStock()) { // Only show items which have stock quantity
				oMenuItemIds.add(oOutletItem.getItemId());
				m_oItemIdOutletItemPairs.put(oOutletItem.getItemId(), oOutletItem);				
				// form the operation pairs with sequence as key
				if (oOutletItem.getSequence() != 0)
					m_oSeqItemIDPairs.put(oOutletItem.getSequence(), oOutletItem.getItemId());
				else
					oMissingSeqPairs.put(oOutletItem.getItemId(), oOutletItem.getItemId());
			}
		}
		
		// Add missing sequence outletItem to the list order by menuItemId
		if (!oMissingSeqPairs.isEmpty()) {
			for(Entry<Integer, Integer> entry : oMissingSeqPairs.entrySet()) {
				m_oSeqItemIDPairs.put((!m_oSeqItemIDPairs.isEmpty())?m_oSeqItemIDPairs.lastKey()+1:1, entry.getValue().intValue());
			}
		}
		
		if(!oMenuItemIds.isEmpty()) {
			// Load the menu item 
			FuncMenu oFuncMenu = new FuncMenu();
			ArrayList<MenuItem> oMenuItemList = oFuncMenu.getMenuItemsByIds(oMenuItemIds, true);
			
			for(MenuItem oMenuItem:oMenuItemList){
				m_oItemIDMenuItemPairs.put(Integer.valueOf(oMenuItem.getItemId()), oMenuItem);
			}

			// Check whether menu item in stock item list is deleted, if true, remove deleted these stock menu items
			TreeMap<Integer, Integer> oResultSeqItemIDPairs = new TreeMap<Integer, Integer>();
			for(Entry<Integer, Integer> entry: m_oSeqItemIDPairs.entrySet()) {
				for(MenuItem oMenuItem:oMenuItemList){
					if(oMenuItem.getItemId() == entry.getValue()) {
						oResultSeqItemIDPairs.put(entry.getKey(), entry.getValue());
						break;
					}
				}
			}
			if(!oResultSeqItemIDPairs.isEmpty()) {
				m_oSeqItemIDPairs.clear();
				m_oSeqItemIDPairs = oResultSeqItemIDPairs;
			}
		}
		
		return m_oSeqItemIDPairs;
	}
	
	//Calculate page number
	private int calculateTotalPageOfPairs(TreeMap<Integer, Integer> oSeqItemIDPairs) {
		if(oSeqItemIDPairs.size() <= 0)
			return 0;
//KingsleyKwan20171016StockOperationByJack		-----Start-----
		int iTotalPage = 0;
		iTotalPage = oSeqItemIDPairs.size() / m_iMaxItemPerPage;
		if(oSeqItemIDPairs.size() % m_iMaxItemPerPage > 0)
			iTotalPage++;
//KingsleyKwan20171016StockOperationByJack		----- End -----
		
		return iTotalPage;
	}
	
	private void loadStockTransactionsAtPage(int iPageNumber) {
		if(iPageNumber > m_iTotalPage) {
			// Cleanup the list
			m_oFrameStockOperation.clearAllRecords();
			return;
		}
		
		//Clear stock memory
		m_oItemIDStockValuesPairs.clear();
		m_oItemIDTransactionsPairs.clear();
		m_oItemIDCloseBalanceTransactionIDPairs.clear();
		m_iCurrentItemListSection = 0;
		m_oCurrentEditMenuItem = null;

		// Add the item to the menu item list
		if(iPageNumber < 1){
			// No item
			return;
		}
//KingsleyKwan20171016StockOperationByJack		-----Start-----
		int iStartIndex = (iPageNumber-1) * m_iMaxItemPerPage;
		MenuItem oMenuItem = null;
		List<Integer> oMenuItemIDList = new ArrayList<Integer>(m_oOperatingPairs.values());
		
		List<MenuItem> oPageItemList = new ArrayList<MenuItem>();
		for(int i = iStartIndex; i < oMenuItemIDList.size(); i++) {
			//Load 10 items in a page
			if((i-iStartIndex) == m_iMaxItemPerPage)
				break;
//KingsleyKwan20171016StockOperationByJack		----- End -----
			oMenuItem = m_oItemIDMenuItemPairs.get(oMenuItemIDList.get(i));
			if(oMenuItem == null) // if menu item is not found, skip to add into oPageItemList
				continue;
			
			oPageItemList.add(oMenuItem);
		}
		if(!oPageItemList.isEmpty())
			loadItemStockTransactions(oPageItemList);
		reloadFrameStockCommonBasket();
	}
	
	//Request 15 menu items stock value in a page
	private void loadItemStockTransactions(List<MenuItem> oPageItemList) {
		List<Integer> oMenuItemIds = new ArrayList<Integer>();
		for(MenuItem oMenuItem : oPageItemList) {
			oMenuItemIds.add(Integer.valueOf(oMenuItem.getItemId()));
		}
		
		// *****************************************************************
		// Create thread to load information
		AppThreadManager oAppThreadManager = new AppThreadManager();
		
		// Thread 1 : Get outlet items stock
		m_oPosOutletItemList = new PosOutletItemList();
		// Create parameter array
		Object[] oParameters = new Object[2];
		oParameters[0] = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oParameters[1] = oMenuItemIds;
		oAppThreadManager.addThread(1, m_oPosOutletItemList, "readOutletItemListByItemIds", oParameters);
		
		// Thread 2 : Get all transactions first
		PosStockTransactionList oPosStockTransactionList = new PosStockTransactionList();
		Object[] oParameter2s = new Object[4];
		oParameter2s[0] = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId();
		oParameter2s[1] = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oParameter2s[2] = oMenuItemIds;
		oParameter2s[3] = "";
		oAppThreadManager.addThread(2, oPosStockTransactionList, "searchStockTransaction", oParameter2s);
		
		// Run all of the threads
		oAppThreadManager.runThread();

		// Wait for the thread to finish
		oAppThreadManager.waitForThread();
		
		//Get result
		List<PosOutletItem> oPosOutletItems = new ArrayList<PosOutletItem>(m_oPosOutletItemList.getOutletItemList());
		HashMap<String, PosStockTransaction> oPosStockTransactionsList = new HashMap<String, PosStockTransaction>(oPosStockTransactionList.getStockTransaction());
		List<PosStockTransaction> oPosStockTransactions = new ArrayList<PosStockTransaction>(oPosStockTransactionsList.values());
		for(MenuItem oMenuItem : oPageItemList) {
			BigDecimal dCurrentStock = BigDecimal.ZERO;
			BigDecimal dPrevSelfStockIn = BigDecimal.ZERO;
			BigDecimal dStockIn = BigDecimal.ZERO;
			BigDecimal dPrevWastage = BigDecimal.ZERO;
			BigDecimal dPreviousDamage = BigDecimal.ZERO;
			
			//Find value in PosOutletItem
			for(PosOutletItem oTempOutletItem : oPosOutletItems) {
				if(oTempOutletItem.getItemId() == oMenuItem.getItemId()) {
					dCurrentStock = oTempOutletItem.getStockQty();
					break;
				}
			}
			
			//Calcuate the rest 4 values and store transaction id
			List<PosStockTransaction> oTransactions = new ArrayList<PosStockTransaction>();
			for(PosStockTransaction oTempStockTransaction : oPosStockTransactions) {
				if(oTempStockTransaction.getItemId() == oMenuItem.getItemId()) {
					if(oTempStockTransaction.getTransactionType().equals(PosStockTransaction.TRANSACTION_TYPE_SELF_STOCK_IN)){
						dPrevSelfStockIn = dPrevSelfStockIn.add(oTempStockTransaction.getTransactionQty());
					}
					else if(oTempStockTransaction.getTransactionType().equals(PosStockTransaction.TRANSACTION_TYPE_STOCK_IN)){
						dStockIn = dStockIn.add(oTempStockTransaction.getTransactionQty());
					}
					else if(oTempStockTransaction.getTransactionType().equals(PosStockTransaction.TRANSACTION_TYPE_WASTAGE)){
						dPrevWastage = dPrevWastage.add(oTempStockTransaction.getTransactionQty());
					}
					// If stock is updated during operation, the stock operation allow input the damage amount
					else if(AppGlobal.g_bNotCheckStock == false && oTempStockTransaction.getTransactionType().equals(PosStockTransaction.TRANSACTION_TYPE_DAMAGE)){
						dPreviousDamage = dPreviousDamage.add(oTempStockTransaction.getTransactionQty());
					}
					// If stock is NOT updated during operation, the stock operation allow input the close balance
					else if(AppGlobal.g_bNotCheckStock == true && oTempStockTransaction.getTransactionType().equals(PosStockTransaction.TRANSACTION_TYPE_CLOSE_BALANCE)){
						dPreviousDamage = dPreviousDamage.add(oTempStockTransaction.getTransactionQty());
						
						// Store the pair of menu item id and close balance transaction id
						m_oItemIDCloseBalanceTransactionIDPairs.put(oMenuItem.getItemId(), oTempStockTransaction.getTransactionId());
					}
					
					//Store transaction id
					if(m_oItemIDTransactionsPairs.containsKey(Integer.valueOf(oMenuItem.getItemId()))) {
						oTransactions = m_oItemIDTransactionsPairs.get(Integer.valueOf(oMenuItem.getItemId()));
					}
					oTransactions.add(oTempStockTransaction);
					m_oItemIDTransactionsPairs.put(Integer.valueOf(oMenuItem.getItemId()), oTransactions);
				}
			}
			
			//Store stock values into TreeMap
			BigDecimal[] oStockValues = {BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO};
			oStockValues[0] = dCurrentStock;
			oStockValues[1] = dPrevSelfStockIn;
			oStockValues[2] = dStockIn;
			oStockValues[3] = dPrevWastage;
			oStockValues[4] = dPreviousDamage;
			m_oItemIDStockValuesPairs.put(Integer.valueOf(oMenuItem.getItemId()), oStockValues);
		}
	}
	
	private PosStockTransaction constructPosStockTransactionObject(int iItemId, String sTransType, BigDecimal dTransQty) {
		PosStockTransaction oPosStockTransaction = new PosStockTransaction();
		DateTime oCurrentDateTime = AppGlobal.getCurrentTime(false);
		
		oPosStockTransaction.setBusinessDayId(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId());
		oPosStockTransaction.setShopId(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getShopId());
		oPosStockTransaction.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
		oPosStockTransaction.setItemId(iItemId);
		oPosStockTransaction.setTransactionType(sTransType);
		oPosStockTransaction.setTransactionQty(dTransQty);
		oPosStockTransaction.setUserId(AppGlobal.g_oFuncUser.get().getUserId());
		oPosStockTransaction.setActionTime(AppGlobal.convertTimeToUTC(oCurrentDateTime));
		oPosStockTransaction.setActionLocTime(oCurrentDateTime);
		
		return oPosStockTransaction;
	}
	
	private void reloadFrameStockCommonBasket() {
		//Clear common basket first
		m_oFrameStockOperation.clearAllRecords();
		
		int iCnt = m_oItemIDStockValuesPairs.size();
		List<Integer> oMenuItemIdList = new ArrayList<Integer>(m_oOperatingPairs.values());
		
		for (int i=0; i<iCnt; i++) {
			MenuItem oMenuItem = null;
			int iMenuItemId = oMenuItemIdList.get((m_iCurrentPage - 1) * m_iMaxItemPerPage + i);
			if(m_oOperatingPairs.containsValue(iMenuItemId))
				oMenuItem = m_oItemIDMenuItemPairs.get(iMenuItemId);
			if(oMenuItem == null)
				continue;
			
			BigDecimal dCurrentStock = m_oItemIDStockValuesPairs.get(iMenuItemId)[0];
			BigDecimal dPrevSelfStockIn = m_oItemIDStockValuesPairs.get(iMenuItemId)[1];
			BigDecimal dStockIn = m_oItemIDStockValuesPairs.get(iMenuItemId)[2];
			BigDecimal dPrevWastage = m_oItemIDStockValuesPairs.get(iMenuItemId)[3];
			BigDecimal dPreviousDamage = m_oItemIDStockValuesPairs.get(iMenuItemId)[4];
			
			//Add record into common basket
			m_oFrameStockOperation.addRecord(m_iCurrentItemListSection, i, oMenuItem.getCode(), oMenuItem.getName(AppGlobal.g_oCurrentLangIndex.get()),
					dCurrentStock, dPrevSelfStockIn, dStockIn, dPrevWastage, dPreviousDamage);
		}
	}
	
	private boolean processSave() {
		if(m_oItemIDModifiedValuesPairs.isEmpty())
			return false;
		
		int iThreadId = 2;
		List<PosStockTransaction> oPosMultiStockTransactions = new ArrayList<PosStockTransaction>();
		AppThreadManager oAppThreadManager = new AppThreadManager();
		
		for(Entry<Integer, BigDecimal[]> entry : m_oItemIDModifiedValuesPairs.entrySet()) {
			int iMenuItemId = entry.getKey().intValue();

			// New value
			BigDecimal[] oValues = entry.getValue();
			BigDecimal dNewSelfStockInQty = oValues[1];
			BigDecimal dNewStockInQty = oValues[2];
			BigDecimal dNewWastageQty = oValues[3];
			// If stock is updated during operation, the damage amount is inputed
			// If stock is NOT updated during operation, the close balance is inputed
			BigDecimal dNewDamageQty = oValues[4];
			
			// Previous value
			BigDecimal dPrevSelfStockInQty = BigDecimal.ZERO;
			BigDecimal dPrevStockInQty = BigDecimal.ZERO;
			BigDecimal dPrevWastageQty = BigDecimal.ZERO;
			BigDecimal dPrevDamageQty = BigDecimal.ZERO;
			if(m_oItemIDStockValuesPairs.containsKey(iMenuItemId)){
				BigDecimal[] oPrevValues = m_oItemIDStockValuesPairs.get(iMenuItemId);
				dPrevSelfStockInQty = oPrevValues[1];
				dPrevStockInQty = oPrevValues[2];
				dPrevWastageQty = oPrevValues[3];
				dPrevDamageQty = oPrevValues[4];
			}
			
			BigDecimal dSelfStockInQty = dNewSelfStockInQty.subtract(dPrevSelfStockInQty);
			BigDecimal dStockInQty = dNewStockInQty.subtract(dPrevStockInQty);
			BigDecimal dWastageQty = dNewWastageQty.subtract(dPrevWastageQty);
			BigDecimal dDamageQty;
			if(AppGlobal.g_bNotCheckStock == false)
				// If stock is updated during operation, the damage amount is inputed
				dDamageQty = dNewDamageQty.subtract(dPrevDamageQty);
			else
				// If stock is NOT updated during operation, the close balance is inputed
				dDamageQty = dNewDamageQty;
			BigDecimal dAdjustItemStockQty = BigDecimal.ZERO;
			String sLogRemark = "MenuItemId:"+iMenuItemId;
			
			if(dSelfStockInQty.compareTo(BigDecimal.ZERO) != 0) {
				PosStockTransaction oPosStockTransaction = constructPosStockTransactionObject(iMenuItemId, PosStockTransaction.TRANSACTION_TYPE_SELF_STOCK_IN, dSelfStockInQty);
				oPosMultiStockTransactions.add(oPosStockTransaction);
				dAdjustItemStockQty = dAdjustItemStockQty.add(dSelfStockInQty);
				sLogRemark += "[SelfStockIn:"+dSelfStockInQty+"]";
			}
			
			if(dStockInQty.compareTo(BigDecimal.ZERO) != 0) {
				PosStockTransaction oPosStockTransaction = constructPosStockTransactionObject(iMenuItemId, PosStockTransaction.TRANSACTION_TYPE_STOCK_IN, dStockInQty);
				oPosMultiStockTransactions.add(oPosStockTransaction);
				dAdjustItemStockQty = dAdjustItemStockQty.add(dStockInQty);
				sLogRemark += "[StockIn:"+dStockInQty+"]";
			}
			
			if(dWastageQty.compareTo(BigDecimal.ZERO) != 0) {
				PosStockTransaction oPosStockTransaction = constructPosStockTransactionObject(iMenuItemId, PosStockTransaction.TRANSACTION_TYPE_WASTAGE, dWastageQty);
				oPosMultiStockTransactions.add(oPosStockTransaction);
				dAdjustItemStockQty = dAdjustItemStockQty.subtract(dWastageQty);
				sLogRemark += "[Wastage:"+dWastageQty+"]";
			}
			
			if(AppGlobal.g_bNotCheckStock == false){
				// If stock is updated during operation, the damage amount is inputed
				if(dDamageQty.compareTo(BigDecimal.ZERO) != 0) {
					PosStockTransaction oPosStockTransaction = constructPosStockTransactionObject(iMenuItemId, PosStockTransaction.TRANSACTION_TYPE_DAMAGE, dDamageQty);
					oPosMultiStockTransactions.add(oPosStockTransaction);
					dAdjustItemStockQty = dAdjustItemStockQty.subtract(dDamageQty);
					sLogRemark += "[Damage:"+dDamageQty+"]";
				}
			}else{
				// If stock is NOT updated during operation, the close balance is inputed
				PosStockTransaction oPosStockTransaction = constructPosStockTransactionObject(iMenuItemId, PosStockTransaction.TRANSACTION_TYPE_CLOSE_BALANCE, dDamageQty);
				
				if(m_oItemIDCloseBalanceTransactionIDPairs.containsKey(iMenuItemId)){
					oPosStockTransaction.setTransactionId(m_oItemIDCloseBalanceTransactionIDPairs.get(iMenuItemId));
				}
				
				oPosMultiStockTransactions.add(oPosStockTransaction);
			}
			
			if(AppGlobal.g_bNotCheckStock == false && dAdjustItemStockQty.compareTo(BigDecimal.ZERO) != 0) {
				// Always add because subtract value will use -ve to represent
				Boolean bSubtract = false;
				
				// Thread: Update item count per item
				// Create parameter array
				Object[] oParameters = new Object[5];
				oParameters[0] = iMenuItemId;
				oParameters[1] = dAdjustItemStockQty;
				oParameters[2] = bSubtract;
				oParameters[3] = true;
				oParameters[4] = true;
				oAppThreadManager.addThread(iThreadId, m_oFuncCheck, "updateItemCount", oParameters);
				iThreadId++;
				
				// Add log to action log list
				sLogRemark += "[AdjustItemStockQty:"+dAdjustItemStockQty+"]";
				AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.stock_operation.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", sLogRemark);
			}
		}
		//handle action log
		AppGlobal.g_oActionLog.get().handleActionLog(false);
		
		//Update Transactions and run threads
		if(!oPosMultiStockTransactions.isEmpty()) {
			PosStockTransactionList oPosStockTransactionList = new PosStockTransactionList();
			Object[] oParameters = new Object[1];
			oParameters[0] = oPosMultiStockTransactions;
			oAppThreadManager.addThread(1, oPosStockTransactionList, "addUpdateWithMutlipleTransactions", oParameters);
			oAppThreadManager.runThread();
			oAppThreadManager.waitForThread();
			
			//Update stock values in memory
			if(((Boolean)oAppThreadManager.getResult(1)).booleanValue()) {
				loadStockTransactionsAtPage(m_iCurrentPage);
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
	
	public boolean isAskItem(){
		return m_bAskItem;
	}
	
	public boolean isAskItemSearchByPanel(){
		return m_bAskItemSearchByPanel;
	}
	
	private void searchMenuItems() {
		m_oSearchResultMenuItemIDPairs.clear();
		m_oCurrentEditMenuItem = null;
		
		for(Entry<Integer, Integer> entry:m_oSeqItemIDPairs.entrySet()) {
			MenuItem oMenuItem = m_oItemIDMenuItemPairs.get(entry.getValue());
			
			boolean bFound = false;
			
			if (m_oSearchMenuItemIdList != null) {
				for(Integer iSearchMenuItemId : m_oSearchMenuItemIdList) {
					if (iSearchMenuItemId == oMenuItem.getItemId()) {
						bFound = true;
						break;
					}
				}
			}
			
			if(m_sCurrentSearchKeyWord.length() > 0) {
				//Five languages
				for(int i = 1; i < 5; i++) {
					String sItemName = oMenuItem.getName(i);
					int iStringIndex = sItemName.indexOf(m_sCurrentSearchKeyWord);
					if(iStringIndex >= 0) {
						bFound = true;
						break;
					}
				}
			}
			
			//TODO: Further development
			if(bFound == false && m_iCurrentSelectDepartmentId > 0) {
				if(oMenuItem.getDeparmentId() != m_iCurrentSelectDepartmentId){
					bFound = true;
				}
			}
			//TODO: Further development
			if(bFound == false && m_iCurrentSelectCategoryId > 0){
				if(oMenuItem.getCategoryId() != m_iCurrentSelectCategoryId){
					bFound = true;
				}
			}
			
			if(bFound) {
				m_oSearchResultMenuItemIDPairs.put(entry.getKey(), entry.getValue());
			}
		}
		
		if(!m_oSearchResultMenuItemIDPairs.isEmpty()) {
			m_oOperatingPairs = m_oSearchResultMenuItemIDPairs;
			//Draw the result page
			m_iTotalPage = calculateTotalPageOfPairs(m_oOperatingPairs);
			m_iCurrentPage = 1;
			loadStockTransactionsAtPage(m_iCurrentPage);
		}
		else {
			m_oOperatingPairs = m_oSeqItemIDPairs;
			//Clear the page
			m_iTotalPage = 0;
			m_iCurrentPage = 0;
			m_oCurrentEditMenuItem = null;
			m_oFrameStockOperation.clearAllRecords();
		}
		updatePageButtonsVisibility();
	}
	
	private void showAllItemResult(){
		checkUnsavedItems();
		
		//Clear memory
		m_oItemIDStockValuesPairs.clear();
		m_oItemIDTransactionsPairs.clear();
		m_oItemIDCloseBalanceTransactionIDPairs.clear();
		m_oSearchResultMenuItemIDPairs.clear();
		
		//Restore Stock Pairs data
		m_oOperatingPairs = m_oSeqItemIDPairs;
		
		//Redraw the page
		m_iTotalPage = calculateTotalPageOfPairs(m_oOperatingPairs);
		if(m_iTotalPage > 0)
			m_iCurrentPage = 1;
		loadStockTransactionsAtPage(m_iCurrentPage);
		updatePageButtonsVisibility();
	}
	
	private void updatePageButtonsVisibility() {
		m_oFrameStockOperation.setPageNumber(m_iTotalPage, m_iCurrentPage);
		
		if(m_iTotalPage <= 1) {
			m_oFrameStockOperation.showNextPageButton(false);
			m_oFrameStockOperation.showPrevPageButton(false);
			return;
		}
		
		m_oFrameStockOperation.showNextPageButton(true);
		m_oFrameStockOperation.showPrevPageButton(true);
		
		if(m_iCurrentPage == 1)
			m_oFrameStockOperation.showPrevPageButton(false);
		if(m_iCurrentPage == m_iTotalPage)
			m_oFrameStockOperation.showNextPageButton(false);
	}
	
	private void checkUnsavedItems() {
		//Check if not save yet
		if(!m_oItemIDModifiedValuesPairs.isEmpty()) {
			// If yes, ask for save or abort
			FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
			oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("record_is_edited") + ", " + AppGlobal.g_oLang.get()._("save_the_changes")+"?");
			oFormConfirmBox.show();
			if(oFormConfirmBox.isOKClicked()){
				// Save the record
				if(processSave()) {
					m_oItemIDModifiedValuesPairs.clear();
				}
			}
			else {
				m_oItemIDModifiedValuesPairs.clear();
			}
		}
	}
	
	private void showEditSequenceView() {
		m_oSeqEditPairs.clear();
		m_oFrameStockEditSequence.clearAllRecords();
		
		int iIndex = 0;
		for(Entry<Integer, Integer> entry : m_oSeqItemIDPairs.entrySet()) {
			m_oSeqEditPairs.put(entry.getKey().intValue(), entry.getKey().intValue());
			
			MenuItem oMenuItem = m_oItemIDMenuItemPairs.get(entry.getValue().intValue());
			//Add record into common basket
			m_oFrameStockEditSequence.addRecord(m_iCurrentItemListSection, iIndex, ""+(iIndex+1), oMenuItem.getName(AppGlobal.g_oCurrentLangIndex.get()));
			
			iIndex++;
		}
		
		m_oFrameCover.setVisible(true);
		m_oFrameCover.bringToTop();
		m_oFrameStockEditSequence.setVisible(true);
		m_oFrameStockEditSequence.bringToTop();
	}
	
	private void saveSequence(TreeMap<Integer, Integer> oSeqEditPairs) {
		// Setup a menuItemId and OutletItem pairs for checking
		TreeMap<Integer, PosOutletItem> oItemIdOutletItemPairs = new TreeMap<Integer, PosOutletItem>();
		for(PosOutletItem oOutletItem: m_oFuncCheck.getItemStockList()) {
			if(oOutletItem.isCheckStock()) { // Only show items which have stock quantity
				oItemIdOutletItemPairs.put(oOutletItem.getItemId(), oOutletItem);
			}
		}
		// Check for update outletItem sequence
		for(Entry<Integer, Integer> entry : oSeqEditPairs.entrySet()) {
			// update if sequence changed or not yet set
			PosOutletItem oPosOutletItem = oItemIdOutletItemPairs.get(m_oSeqItemIDPairs.get(entry.getValue().intValue()));		
			if (entry.getKey().intValue() != entry.getValue().intValue() || oPosOutletItem.getSequence() == 0) {
				oPosOutletItem = new PosOutletItem();
				if(oPosOutletItem.readByMenuItemId(AppGlobal.g_oFuncOutlet.get().getOutletId(), m_oSeqItemIDPairs.get(entry.getValue().intValue()))) {
					oPosOutletItem.setSequence(entry.getKey().intValue());
					oPosOutletItem.addUpdate(true);
				}				
			}
		}
	}
	
	// direction 0: down 1 : up
	private void moveSequence(int iOitmId, int iDirection) {
		AppThreadManager oAppThreadManager = new AppThreadManager();
		PosOutletItem oPosOutletItem = new PosOutletItem();
		// Create parameter array
		Object[] oParameters = new Object[2];
		oParameters[0] = iOitmId;
		oParameters[1] = iDirection;
		oAppThreadManager.addThread(1, oPosOutletItem, "moveSequence", oParameters);
		
		// Run all of the threads
		oAppThreadManager.runThread();
		// Wait for the thread to finish
		oAppThreadManager.waitForThread();
	}
//KingsleyKwan20171016StockOperationByJack		-----Start-----
	//search item by itemIds
	public void searchItemByIds(List<Integer> oItemIds){
		m_oSearchMenuItemIdList = oItemIds;
		if (m_oSearchMenuItemIdList != null) {
			if (!m_oSearchMenuItemIdList.isEmpty())
				searchMenuItems();
		}
		
		//cleanup the search item list after finish searching
		m_oSearchMenuItemIdList.clear();
	}
//KingsleyKwan20171016StockOperationByJack		----- End -----
	@Override
	public void frameStockOperation_clickSave() {
		// Save the record
		if(processSave()) {
			//Reload m_oFrameStockOperation
			m_oItemIDModifiedValuesPairs.clear();
			m_oCurrentEditMenuItem = null;
			//Redraw common basket
			reloadFrameStockCommonBasket();
		}
	}

	@Override
	public void frameStockOperation_clickExit() {
		checkUnsavedItems();
		
		// Finish showing this form
		this.finishShow();
	}

	@Override
	public boolean frameStockOperation_clickBasketMenuItem(int iNewSectionId, int iNewItemIndex, int iNewFieldIndex, String sPrevEditValue) {
		boolean bSetNextField = true;
		
		// If the new item index and field index are -1, no need to set the next field further
		if ((iNewItemIndex == -1) && (iNewFieldIndex == -1))
			bSetNextField = false;
		
		//Check if previous edit is modified
		if((m_iCurrentItemListItemIndex >= 0) && (m_iCurrentItemListFieldIndex == 3 || m_iCurrentItemListFieldIndex == 4 || m_iCurrentItemListFieldIndex == 5 || m_iCurrentItemListFieldIndex == 6) && m_oCurrentEditMenuItem != null) {
			boolean bNeedSave = false;
			boolean bSetBackgroundColor = false;
			
			//Check new values
			BigDecimal[] oMenuItemNewValues = {BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO};
			BigDecimal[] oOriginalValues = {BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO};
			if(m_oItemIDStockValuesPairs.containsKey(Integer.valueOf(m_oCurrentEditMenuItem.getItemId()))){
				oOriginalValues = m_oItemIDStockValuesPairs.get(Integer.valueOf(m_oCurrentEditMenuItem.getItemId()));
			}
			
			if(m_oItemIDModifiedValuesPairs.containsKey(Integer.valueOf(m_oCurrentEditMenuItem.getItemId()))) {
				oMenuItemNewValues = m_oItemIDModifiedValuesPairs.get(Integer.valueOf(m_oCurrentEditMenuItem.getItemId()));
			}else{
				oMenuItemNewValues[1] = oOriginalValues[1];
				oMenuItemNewValues[2] = oOriginalValues[2];
				oMenuItemNewValues[3] = oOriginalValues[3];
				oMenuItemNewValues[4] = oOriginalValues[4];
			}
			
			int iValueIndex = -1;
			if(sPrevEditValue != null){
				BigDecimal dPrevValue = new BigDecimal(sPrevEditValue);
				switch(m_iCurrentItemListFieldIndex){
				case 3:
					iValueIndex = 1;
					break;
				case 4:
					iValueIndex = 2;
					break;
				case 5:
					iValueIndex = 3;
					break;
				case 6:
					iValueIndex = 4;
					break;
				}
				
				if(iValueIndex > 0){
					oMenuItemNewValues[iValueIndex] = dPrevValue;
				}
			}
			
			if(oMenuItemNewValues[1].compareTo(oOriginalValues[1]) != 0) {
				bNeedSave = true;
				if(iValueIndex == 1)
					bSetBackgroundColor = true;
			}
			if(oMenuItemNewValues[2].compareTo(oOriginalValues[2]) != 0) {
				bNeedSave = true;
				if(iValueIndex == 2)
					bSetBackgroundColor = true;
			}
			if(oMenuItemNewValues[3].compareTo(oOriginalValues[3]) != 0) {
				bNeedSave = true;
				if(iValueIndex == 3)
					bSetBackgroundColor = true;
			}
			if(oMenuItemNewValues[4].compareTo(oOriginalValues[4]) != 0) {
				bNeedSave = true;
				if(iValueIndex == 4)
					bSetBackgroundColor = true;
			}
			if(bNeedSave) {
				m_oItemIDModifiedValuesPairs.put(Integer.valueOf(m_oCurrentEditMenuItem.getItemId()), oMenuItemNewValues);
			}
			else {
				if(m_oItemIDModifiedValuesPairs.containsKey(Integer.valueOf(m_oCurrentEditMenuItem.getItemId())))
					m_oItemIDModifiedValuesPairs.remove(Integer.valueOf(m_oCurrentEditMenuItem.getItemId()));
			}
			//Tell FrameCommonBasketCell to set bg color
			if(sPrevEditValue != null)
				m_oFrameStockOperation.setCellFieldBackgroundColorEdited(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, m_iCurrentItemListFieldIndex, bSetBackgroundColor);
		}
		
		m_iCurrentItemListSection = iNewSectionId;
		m_iCurrentItemListItemIndex = iNewItemIndex;
		m_iCurrentItemListFieldIndex = iNewFieldIndex;
		
		//Locate the menu item in pairs according to page number
		if(iNewItemIndex >= 0) {
//KingsleyKwan20171016StockOperationByJack		-----Start-----
			int iTargetIndex = ((m_iCurrentPage - 1) * m_iMaxItemPerPage) + iNewItemIndex;
//KingsleyKwan20171016StockOperationByJack		----- End -----
			List<Integer> oMenuItemIDList = new ArrayList<Integer>(m_oOperatingPairs.values());
			m_oCurrentEditMenuItem = m_oItemIDMenuItemPairs.get(oMenuItemIDList.get(iTargetIndex));
			
			//Highlight name field
			if(iNewFieldIndex == 0) {			//Name field clicked
				int iCurrentMenuItemID = m_oCurrentEditMenuItem.getItemId();
				if(!m_oDeleteMenuItemIDs.contains(iCurrentMenuItemID)) {
					m_oDeleteMenuItemIDs.add(iCurrentMenuItemID);
					m_oFrameStockOperation.setCellFieldBackgroundColorEdited(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, 0, true);
				}
				else {
					for(Iterator<Integer> i = m_oDeleteMenuItemIDs.iterator(); i.hasNext();) {
						Integer iMenuItemID = i.next();
						
						if(iMenuItemID.intValue() == iCurrentMenuItemID)
							i.remove();
					}
					m_oFrameStockOperation.setCellFieldBackgroundColorEdited(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, 0, false);
					
					// if no item is selected, set Save button to disable
					if(m_oDeleteMenuItemIDs.isEmpty())
						m_oFrameStockOperation.setSaveButtonEnabled(false);
				}
			}
		}
		else {
			m_oCurrentEditMenuItem = null;
		}
		
		return bSetNextField;
	}

	@Override
	public void frameStockOperation_clickAddItemToStockControl() {
		m_bAskItem = true;
		
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public void frameStockOperation_clickDeleteItemInStockControl() {
		checkUnsavedItems();
		
		if(!m_oDeleteMenuItemIDs.isEmpty()){
			// If yes, ask for save or abort
			FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
			oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("delete_selected_items") + "?");
			oFormConfirmBox.show();
			if(oFormConfirmBox.isOKClicked()) {
				AppThreadManager oAppThreadManager = new AppThreadManager();
				
				//Prepare parameters
				List<Integer> oDeleteOutletItemIds = new ArrayList<Integer>();
				List<PosStockTransaction> oUpdateStockTransactions = new ArrayList<PosStockTransaction>();
				for(Integer oItemId:m_oDeleteMenuItemIDs) {
					//Delete Outlet Items
					for(PosOutletItem oTempOutletItem : m_oPosOutletItemList.getOutletItemList()) {
						if(oTempOutletItem.getItemId() == oItemId.intValue()) {
							oDeleteOutletItemIds.add(Integer.valueOf(oTempOutletItem.getOitmId()));
							break;
						}
					}
					
					//Mark Transactions as delete
					if(m_oItemIDTransactionsPairs.containsKey(oItemId)) {
						List<PosStockTransaction> oTransactions = m_oItemIDTransactionsPairs.get(oItemId);
						for(PosStockTransaction oTempStockTransaction : oTransactions) {
							oTempStockTransaction.setStatus(PosStockTransaction.STATUS_DELETED);
							oUpdateStockTransactions.add(oTempStockTransaction);
						}
					}
				}
				
				//Delete OutletItems record
				PosOutletItemList oPosOutletItemList = new PosOutletItemList();
				// Create parameter array
				Object[] oParameters = new Object[1];
				oParameters[0] = oDeleteOutletItemIds;
				oAppThreadManager.addThread(1, oPosOutletItemList, "deleteOutletItemListByItemIds", oParameters);
				
				//Mark all Transactions as delete
				PosStockTransactionList oPosStockTransactionList = new PosStockTransactionList();
				Object[] oParameter2s = new Object[1];
				oParameter2s[0] = oUpdateStockTransactions;
				oAppThreadManager.addThread(2, oPosStockTransactionList, "addUpdateWithMutlipleTransactions", oParameter2s);
				
				// Run all of the threads
				oAppThreadManager.runThread();

				// Wait for the thread to finish
				oAppThreadManager.waitForThread();
				
				m_oDeleteMenuItemIDs.clear();
				m_oOperatingPairs.clear();
				m_oOperatingPairs = createMenuItemStockPairs();
				m_iTotalPage = calculateTotalPageOfPairs(m_oOperatingPairs);
				if(m_iTotalPage > 0 && m_iTotalPage < m_iCurrentPage)
					m_iCurrentPage--;
				loadStockTransactionsAtPage(m_iCurrentPage);
				updatePageButtonsVisibility();

				// if Search bar has string, show found results
				if(!m_sCurrentSearchKeyWord.isEmpty())
					searchMenuItems();
				
				// After deleted selected items, set Save button to disable
				m_oFrameStockOperation.setSaveButtonEnabled(false);
			}
		}
	}

	@Override
	public void frameStockOperation_clickEditStockSequence() {
		//Check if not save yet
		checkUnsavedItems();
		
		//Show edit sequence page
		showEditSequenceView();
	}
	
	@Override
	public void frameStockOperation_clickSearchByName(String sValue) {
		//Check if not save yet
		checkUnsavedItems();
		
		// Store the keyword
		sValue = sValue.trim();
		if(sValue.length() > 0)
			m_sCurrentSearchKeyWord = sValue;
		
		//Do Search here
		searchMenuItems();
	}

	@Override
	public void frameStockOperation_clickSearchByDepartment() {
		//Check if not save yet
		checkUnsavedItems();
		
		//TODO: Further development
		// Select department
		
		// Set button desc
		m_oFrameStockOperation.setDepartmentButtonDesc(AppGlobal.g_oLang.get()._("all_departments"));
		
		searchMenuItems();
	}

	@Override
	public void frameStockOperation_clickSearchByCategory() {
		//Check if not save yet
		checkUnsavedItems();
		
		//TODO: Further development
		// Select category
		
		// Set button desc
		m_oFrameStockOperation.setCategoryButtonDesc(AppGlobal.g_oLang.get()._("all_categories"));
		
		searchMenuItems();
	}
	
	@Override
	public void frameStockOperation_clickSearchByPanel() {
		//Check if not save yet
		checkUnsavedItems();
		
		m_bAskItemSearchByPanel = true;
		
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public void frameStockOperation_clickShowAllResult() {
		// Clear all search criteria
		m_sCurrentSearchKeyWord = "";
		m_iCurrentSelectDepartmentId = 0;
		m_iCurrentSelectCategoryId = 0;
		
		// Clear search value
		m_oFrameStockOperation.clearSearchValue();
		
		showAllItemResult();
	}
	
	@Override
	public void frameStockOperation_clickNextPage() {
		checkUnsavedItems();
		
		if((m_iCurrentPage+1) <= m_iTotalPage) {
			m_iCurrentPage++;
			loadStockTransactionsAtPage(m_iCurrentPage);
			
			updatePageButtonsVisibility();
		}
	}
	
	@Override
	public void frameStockOperation_clickPrevPage() {
		checkUnsavedItems();
		
		if((m_iCurrentPage-1) >= 1) {
			m_iCurrentPage--;
			loadStockTransactionsAtPage(m_iCurrentPage);
			
			updatePageButtonsVisibility();
		}
	}

	@Override
	public void FrameStockEditSequence_clickExit() {
		m_oFrameCover.setVisible(false);
		m_oFrameStockEditSequence.setVisible(false);
		
		// Load stock
		loadStockRecord();
	}

	@Override
	public void FrameStockEditSequence_clickBasketMenuItem(int iNewSectionId, int iNewItemIndex, int iNewFieldIndex) {
		m_oFrameStockEditSequence.setCellFieldBackgroundColorEdited(iNewSectionId, iNewItemIndex, iNewFieldIndex, true);
	}

	@Override
	public void FrameStockEditSequence_clickUp(int iCurrSectionId, int iCurrItemIndex) {
		//Do nothing if not selected any items
		if (m_oFrameStockEditSequence.getCurrItemIdx() == -1)
			return;
		
		if (iCurrItemIndex > 0) {
			List<Integer> oSeqList = new ArrayList<Integer>(m_oSeqEditPairs.keySet());
			
			int iCurrSeq = m_oSeqEditPairs.get(oSeqList.get(iCurrItemIndex));
			int iSwapSeq = m_oSeqEditPairs.get(oSeqList.get(iCurrItemIndex-1));
					
			MenuItem oCurrMenuItem = m_oItemIDMenuItemPairs.get(m_oSeqItemIDPairs.get(iCurrSeq));
			MenuItem oSwapMenuItem = m_oItemIDMenuItemPairs.get(m_oSeqItemIDPairs.get(iSwapSeq));
			
			m_oSeqEditPairs.put(oSeqList.get(iCurrItemIndex), iSwapSeq);
			m_oSeqEditPairs.put(oSeqList.get(iCurrItemIndex-1), iCurrSeq);
			
			m_oFrameStockEditSequence.changeValue(iCurrSectionId, iCurrItemIndex, oSwapMenuItem.getName(AppGlobal.g_oCurrentLangIndex.get()));
			m_oFrameStockEditSequence.changeValue(iCurrSectionId, iCurrItemIndex-1, oCurrMenuItem.getName(AppGlobal.g_oCurrentLangIndex.get()));
			
			m_oFrameStockEditSequence.setCellFieldBackgroundColorEdited(iCurrSectionId, iCurrItemIndex, 1, false);
			m_oFrameStockEditSequence.setCellFieldBackgroundColorEdited(iCurrSectionId, iCurrItemIndex-1, 1, true);
			
			m_oFrameStockEditSequence.setCurrItemIdx(iCurrItemIndex-1);

			// swap the sequence
			moveSequence(m_oItemIdOutletItemPairs.get(m_oSeqItemIDPairs.get(iCurrSeq)).getOitmId(), 1);
			
			m_oFrameStockEditSequence.itemListScrollToIdx(iCurrItemIndex-1);
		}
	}

	@Override
	public void FrameStockEditSequence_clickDown(int iCurrSectionId, int iCurrItemIndex) {
		//Do nothing if not selected any items
		if (m_oFrameStockEditSequence.getCurrItemIdx() == -1)
			return;
		
		if (iCurrItemIndex < m_oSeqEditPairs.size()-1) {
			List<Integer> oSeqList = new ArrayList<Integer>(m_oSeqEditPairs.keySet());
			
			int iCurrSeq = m_oSeqEditPairs.get(oSeqList.get(iCurrItemIndex));
			int iSwapSeq = m_oSeqEditPairs.get(oSeqList.get(iCurrItemIndex+1));
			
			MenuItem oCurrMenuItem = m_oItemIDMenuItemPairs.get(m_oSeqItemIDPairs.get(iCurrSeq));
			MenuItem oSwapMenuItem = m_oItemIDMenuItemPairs.get(m_oSeqItemIDPairs.get(iSwapSeq));
			
			m_oSeqEditPairs.put(oSeqList.get(iCurrItemIndex), iSwapSeq);
			m_oSeqEditPairs.put(oSeqList.get(iCurrItemIndex+1), iCurrSeq);
			
			m_oFrameStockEditSequence.changeValue(iCurrSectionId, iCurrItemIndex, oSwapMenuItem.getName(AppGlobal.g_oCurrentLangIndex.get()));
			m_oFrameStockEditSequence.changeValue(iCurrSectionId, iCurrItemIndex+1, oCurrMenuItem.getName(AppGlobal.g_oCurrentLangIndex.get()));
			
			m_oFrameStockEditSequence.setCellFieldBackgroundColorEdited(iCurrSectionId, iCurrItemIndex, 1, false);
			m_oFrameStockEditSequence.setCellFieldBackgroundColorEdited(iCurrSectionId, iCurrItemIndex+1, 1, true);
			
			m_oFrameStockEditSequence.setCurrItemIdx(iCurrItemIndex+1);
			
			// swap the sequence
			moveSequence(m_oItemIdOutletItemPairs.get(m_oSeqItemIDPairs.get(iCurrSeq)).getOitmId(), 0);
			
			m_oFrameStockEditSequence.itemListScrollToIdx(iCurrItemIndex+1);
		}
	}

	@Override
	public void FrameStockEditSequence_clickToSeq(int iCurrSectionId, int iCurrItemIndex, String sTargetSeq) {
		//Do nothing if not selected any items
		if (m_oFrameStockEditSequence.getCurrItemIdx() == -1)
			return;
		
		//Prompt error box if invalid input
		String sErrMsg = "";
		try{
			Integer.valueOf(sTargetSeq);
		}catch(NumberFormatException e){
			sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
            oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
            oFormDialogBox.setMessage(sErrMsg);
            oFormDialogBox.show();
            return;
		}
		
		List<Integer> oSeqList = new ArrayList<Integer>(m_oSeqEditPairs.keySet());
		int iCurrSeq = m_oSeqEditPairs.get(oSeqList.get(iCurrItemIndex));
		int iTargetSeq = Integer.valueOf(sTargetSeq);
		
		// do nothing if same sequence
		if (iTargetSeq == oSeqList.get(iCurrItemIndex))
			return;
		
		/////////// modify the sequence ///////////////////////////////////////////////
		int iLoopIdx = iCurrItemIndex;
		do {
			if (oSeqList.get(iLoopIdx) > iTargetSeq) {
				m_oSeqEditPairs.put(oSeqList.get(iLoopIdx),m_oSeqEditPairs.get(oSeqList.get(iLoopIdx-1)));
				iLoopIdx--;
			}
			else {
				m_oSeqEditPairs.put(oSeqList.get(iLoopIdx),m_oSeqEditPairs.get(oSeqList.get(iLoopIdx+1)));
				iLoopIdx++;
			}
			// break the loop if reach min and max index
			if (iLoopIdx == m_oSeqEditPairs.size()-1 || iLoopIdx == 0)
				break;
			
		} while (oSeqList.get(iLoopIdx) != iTargetSeq);
		m_oSeqEditPairs.put(oSeqList.get(iLoopIdx),iCurrSeq);
		///////////////////////////////////////////////////////////////////////////////
		
		/////////// update the basket list ////////////////////////////////////////////
		m_oFrameStockEditSequence.clearAllRecords();
		int iIndex = 0;
		for(Entry<Integer, Integer> entry : m_oSeqEditPairs.entrySet()) {			
			MenuItem oMenuItem = m_oItemIDMenuItemPairs.get(m_oSeqItemIDPairs.get(entry.getValue().intValue()));
			//Add record into common basket
			m_oFrameStockEditSequence.addRecord(iCurrSectionId, iIndex, ""+(iIndex+1), oMenuItem.getName(AppGlobal.g_oCurrentLangIndex.get()));
			iIndex++;
		}
		//////////////////////////////////////////////////////////////////////////////
		
		// Update the related outlet item sequence 
		AppThreadManager oAppThreadManager = new AppThreadManager();
		PosOutletItem oPosOutletItem = new PosOutletItem();
		// Create parameter array
		Object[] oParameters = new Object[2];
		oParameters[0] = m_oItemIdOutletItemPairs.get(m_oSeqItemIDPairs.get(iCurrSeq)).getOitmId();
		oParameters[1] = iTargetSeq;
		oAppThreadManager.addThread(1, oPosOutletItem, "resequence", oParameters);
		
		// Run all of the threads
		oAppThreadManager.runThread();
		// Wait for the thread to finish
		oAppThreadManager.waitForThread();
		
		m_oFrameStockEditSequence.setCurrItemIdx(-1);
		m_oFrameStockEditSequence.clearToSeqValue();
		
		m_oFrameStockEditSequence.itemListScrollToIdx(iTargetSeq-1);
	}
}
