package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import commonui.FormConfirmBox;
import core.Controller;

import om.MenuItem;
import om.PosActionLog;
import om.PosBusinessDay;
import om.PosOutletItem;
import om.PosOutletItemList;
import om.PosStockTransaction;
import om.PosStockTransactionList;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

public class FormStockBalanceChange extends VirtualUIForm implements FrameStockBalanceChangeListener {

	TemplateBuilder m_oTemplateBuilder;
	private FrameStockBalanceChange m_oFrameStockBalChg;
	
	private TreeMap<Integer, Integer> m_oOperatingPairs;						//K:Sequence 	V:MenuItemId
	private TreeMap<Integer, MenuItem> m_oItemIDMenuItemPairs;					//K:MenuItemId 	V:MenuItem
	private TreeMap<Integer, PosStockTransaction> m_oItemIDStockTranPairs; 		//K:MenuItemId	V:PosStockTransaction
	private TreeMap<Integer, Integer> m_oSearchResultMenuItemPairs;			//Store search result pairs
	
	private TreeMap<Integer, BigDecimal[]> m_oItemIDStockValuesPairs;			//Store stock values of MenuItem
	private TreeMap<Integer, BigDecimal[]> m_oItemIDModifiedValuesPairs;
	
	private int m_iCurrentItemListSection;
	private int m_iCurrentItemListItemIndex;
	private int m_iCurrentItemListFieldIndex;
	private int m_iCurrentPage;
	private int m_iTotalPage;
	private String m_sBdayId;
	
	private String m_sCurrentSearchKeyWord;
	
	public FormStockBalanceChange(Controller oParentController){
		super(oParentController);
		
		m_oOperatingPairs = new TreeMap<Integer, Integer>();
		m_oItemIDStockTranPairs = new TreeMap<Integer, PosStockTransaction>();
		m_oItemIDMenuItemPairs = new TreeMap<Integer, MenuItem>();
		m_oSearchResultMenuItemPairs = new TreeMap<Integer, Integer>();
		
		m_oItemIDStockValuesPairs = new TreeMap<Integer, BigDecimal[]>();
		m_oItemIDModifiedValuesPairs = new TreeMap<Integer, BigDecimal[]>();
		
		m_iCurrentItemListSection = -1;
		m_iCurrentItemListItemIndex = -1;
		m_iCurrentItemListFieldIndex = -1;
		m_iCurrentPage = 0;
		m_iTotalPage = 0;
		m_sBdayId = "";
			
		m_sCurrentSearchKeyWord = "";
	}
	
	public boolean init(){	
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmStockBalanceChange.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameStockBalChg = new FrameStockBalanceChange();
		m_oTemplateBuilder.buildFrame(m_oFrameStockBalChg, "fraStockBalanceChange");
		m_oFrameStockBalChg.addListener(this);
		this.attachChild(m_oFrameStockBalChg);
		
		return true;
	}

	private void initOperationStock() {
		// Load stock
		m_oOperatingPairs.clear();
		createMenuItemStockPairs();
		if(!m_oOperatingPairs.isEmpty())
			//Calculate page number
			m_iTotalPage = calculateTotalPageOfPairs(m_oOperatingPairs);
	}
	
	private void resetValue() {	
		m_oItemIDStockTranPairs.clear();

		m_oItemIDStockValuesPairs.clear();
		m_oItemIDModifiedValuesPairs.clear();

		m_iCurrentItemListSection = -1;
		m_iCurrentItemListItemIndex = -1;
		m_iCurrentItemListFieldIndex = -1;
		m_iCurrentPage = 1;
		m_iTotalPage = 0;
	}
	
	private void createMenuItemStockPairs() {
		///////////////////////////////////////////////////////////////////////////////////////////
		// Load the stock transaction /////////////////////////////////////////////////////////////
		AppThreadManager oAppThreadManager = new AppThreadManager();
		
		// Get all transactions first
		PosStockTransactionList oPosStockTransactionList = new PosStockTransactionList();
		Object[] oParameters = new Object[4];
		oParameters[0] = m_sBdayId;
		oParameters[1] = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oParameters[2] = PosStockTransaction.TRANSACTION_TYPE_CLOSE_BALANCE;
		oParameters[3] = "current";//get current date transaction

		oAppThreadManager.addThread(1, oPosStockTransactionList, "searchStockTransactionByType", oParameters);
		
		// Run all of the threads
		oAppThreadManager.runThread();

		// Wait for the thread to finish
		oAppThreadManager.waitForThread();
		
		HashMap<String, PosStockTransaction> oPosStockTransactionsList = new HashMap<String, PosStockTransaction>(oPosStockTransactionList.getStockTransaction());
		List<PosStockTransaction> oPosStockTransactions = new ArrayList<PosStockTransaction>(oPosStockTransactionsList.values());
		
		// Form the itemIds list and itemIdsStockTransaction pair
		ArrayList<Integer> oMenuItemIds = new ArrayList<Integer>();
		for(PosStockTransaction oTempStockTransaction : oPosStockTransactions) {
			m_oItemIDStockTranPairs.put(Integer.valueOf(oTempStockTransaction.getItemId()), oTempStockTransaction);
			oMenuItemIds.add(oTempStockTransaction.getItemId());
		}
		///////////////////////////////////////////////////////////////////////////////////////////
		// Load the outletItem ////////////////////////////////////////////////////////////////////
		PosOutletItemList oOutletItemList = new PosOutletItemList();
		oOutletItemList.readOutletItemListByItemIds(AppGlobal.g_oFuncOutlet.get().getOutletId(), oMenuItemIds);
		List<PosOutletItem> oResultStockList = new ArrayList<PosOutletItem>(oOutletItemList.getOutletItemList());
		
		ArrayList<Integer> oMenuItemId2s = new ArrayList<Integer>();
		TreeMap<Integer, Integer> oMissingSeqPairs = new TreeMap<Integer, Integer>();
		for(PosOutletItem oPosOutletItem : oResultStockList) {
			oMenuItemId2s.add(oPosOutletItem.getItemId());
			// form the operation pairs with sequence as key
			if (oPosOutletItem.getSequence() != 0)
				m_oOperatingPairs.put(oPosOutletItem.getSequence(), oPosOutletItem.getItemId());
			else
				oMissingSeqPairs.put(oPosOutletItem.getItemId(), oPosOutletItem.getItemId());
		}
		// Add missing sequence outletItem to the list order by menuItemId
		if (!oMissingSeqPairs.isEmpty()) {
			for(Entry<Integer, Integer> entry : oMissingSeqPairs.entrySet()) {
				m_oOperatingPairs.put((m_oOperatingPairs.size()>0)?m_oOperatingPairs.lastKey()+1:1, entry.getValue().intValue());
			}
		}
		///////////////////////////////////////////////////////////////////////////////////////////
		// Add back the item without sequence /////////////////////////////////////////////////////
		ArrayList<Integer> oDupMenuItemIds;
		if (oMenuItemIds.size() > oMenuItemId2s.size()) {
			oDupMenuItemIds = new ArrayList<Integer>(oMenuItemIds);
			oDupMenuItemIds.removeAll(oMenuItemId2s);
			
			for(Integer iMenuItemId : oDupMenuItemIds) {
				m_oOperatingPairs.put((m_oOperatingPairs.size()>0)?m_oOperatingPairs.lastKey()+1:1, iMenuItemId);
			}
		}
		///////////////////////////////////////////////////////////////////////////////////////////
		// load the menuItem //////////////////////////////////////////////////////////////////////
		if(!oMenuItemIds.isEmpty()) {
			// Load the menu item 
			FuncMenu oFuncMenu = new FuncMenu();
			ArrayList<MenuItem> oMenuItemList = oFuncMenu.getMenuItemsByIds(oMenuItemIds, true);
			
			for(MenuItem oMenuItem:oMenuItemList){
				m_oItemIDMenuItemPairs.put(Integer.valueOf(oMenuItem.getItemId()), oMenuItem);
			}
		}
		///////////////////////////////////////////////////////////////////////////////////////////
	}
	
	private void loadStockTransactions(ArrayList<Integer> oItemIds) {
		// Create thread to load information
		AppThreadManager oAppThreadManager = new AppThreadManager();
		
		// Get all transactions first
		PosStockTransactionList oPosStockTransactionList = new PosStockTransactionList();
		Object[] oParameters = new Object[4];
		oParameters[0] = m_sBdayId;
		oParameters[1] = AppGlobal.g_oFuncOutlet.get().getOutletId();
		oParameters[2] = oItemIds;
		oParameters[3] = PosStockTransaction.TRANSACTION_TYPE_CLOSE_BALANCE;

		oAppThreadManager.addThread(1, oPosStockTransactionList, "searchStockTransactionByItemIdAndType", oParameters);
		
		// Run all of the threads
		oAppThreadManager.runThread();

		// Wait for the thread to finish
		oAppThreadManager.waitForThread();
		
		HashMap<String, PosStockTransaction> oPosStockTransactionsList = new HashMap<String, PosStockTransaction>(oPosStockTransactionList.getStockTransaction());
		List<PosStockTransaction> oPosStockTransactions = new ArrayList<PosStockTransaction>(oPosStockTransactionsList.values());
		
		// Form the itemIds list
		for(PosStockTransaction oTempStockTransaction : oPosStockTransactions) {
			m_oItemIDStockTranPairs.put(Integer.valueOf(oTempStockTransaction.getItemId()), oTempStockTransaction);
		}
	}
	
	private void loadStockTransactionsAtPage(int iPage) {
		int iMaxRow = 15;
		int iStartIdx = (iPage-1) * iMaxRow;
		List<Integer> oMenuItemIdList = new ArrayList<Integer>((m_sCurrentSearchKeyWord.length()<=0)?m_oOperatingPairs.values():m_oSearchResultMenuItemPairs.values());
		
		// get the itemIds for loading stock transaction information
		ArrayList<Integer> oItemIds = new ArrayList<Integer>();
		for (int i=iStartIdx; i<oMenuItemIdList.size(); i++) {
			if((i-iStartIdx) == iMaxRow || (i-iStartIdx) == oMenuItemIdList.size())
				break;
			oItemIds.add(oMenuItemIdList.get(i));
		}
		loadStockTransactions(oItemIds);
		
		m_iCurrentItemListSection = 0;
		m_oItemIDStockValuesPairs.clear();
		for(int i = iStartIdx; i < m_oItemIDStockTranPairs.size(); i++) {
			//Load 15 items in a page
			if((i-iStartIdx) == iMaxRow || (i-iStartIdx) == oMenuItemIdList.size())
				break;
			
			int iItemId = oMenuItemIdList.get(i);
			BigDecimal dEndStock = BigDecimal.ZERO;
			BigDecimal dNewEndStock = BigDecimal.ZERO;
			
			dEndStock = dEndStock.add(m_oItemIDStockTranPairs.get(iItemId).getTransactionQty());
			dNewEndStock = dNewEndStock.add(m_oItemIDStockTranPairs.get(iItemId).getTransactionQty());

			//Store stock values into TreeMap
			BigDecimal[] oStockValues = {BigDecimal.ZERO, BigDecimal.ZERO};
			oStockValues[0] = dEndStock;
			oStockValues[1] = dNewEndStock;
			m_oItemIDStockValuesPairs.put(Integer.valueOf(iItemId), oStockValues);
		}
		
		reloadFrameStockCommonBasket();
	}
	
	private void searchMenuItems() {
		m_oSearchResultMenuItemPairs.clear();
		
		for(Entry<Integer, Integer> entry:m_oOperatingPairs.entrySet()) {
			MenuItem oMenuItem = m_oItemIDMenuItemPairs.get(entry.getValue());
			boolean bFound = false;
			
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
			/*
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
			*/
			if(bFound)
				m_oSearchResultMenuItemPairs.put(entry.getKey(), entry.getValue());
		}
		
		if(!m_oSearchResultMenuItemPairs.isEmpty()) {
			//Draw the result page
			m_iTotalPage = calculateTotalPageOfPairs(m_oSearchResultMenuItemPairs);
			m_iCurrentPage = 1;
			loadStockTransactionsAtPage(m_iCurrentPage);
		}
		else {
			//Clear the page
			m_iTotalPage = 0;
			m_iCurrentPage = 0;
			m_oFrameStockBalChg.clearAllRecords();
		}
		
		m_iCurrentItemListSection = -1;
		m_iCurrentItemListItemIndex = -1;
		m_iCurrentItemListFieldIndex = -1;
		updatePageButtonsVisibility();
	}
	
	private void reloadFrameStockCommonBasket() {
		//Clear common basket first
		m_oFrameStockBalChg.clearAllRecords();
		
		int iCnt = m_oItemIDStockValuesPairs.size();
		List<Integer> oMenuItemIdList = new ArrayList<Integer>((m_sCurrentSearchKeyWord.length()<=0)?m_oOperatingPairs.values():m_oSearchResultMenuItemPairs.values());
		for (int i=0; i<iCnt; i++) {
			MenuItem oMenuItem = null;
			int iMenuItemId = oMenuItemIdList.get((m_iCurrentPage-1)*15 + i);
			
			if(m_oItemIDMenuItemPairs.containsKey(iMenuItemId))
				oMenuItem = m_oItemIDMenuItemPairs.get(iMenuItemId);
			if(oMenuItem == null)
				continue;
			
			BigDecimal dEndStock = m_oItemIDStockValuesPairs.get(iMenuItemId)[0];
			BigDecimal dNewEndStock = m_oItemIDStockValuesPairs.get(iMenuItemId)[1];
			
			//Add record into common basket
			m_oFrameStockBalChg.addRecord(m_iCurrentItemListSection, i, oMenuItem.getCode(), oMenuItem.getName(AppGlobal.g_oCurrentLangIndex.get()),
					dEndStock, dNewEndStock);
		}
	}
	
	private void updatePageButtonsVisibility() {
		m_oFrameStockBalChg.setPageNumber(m_iTotalPage, m_iCurrentPage);
		
		if(m_iTotalPage <= 1) {
			m_oFrameStockBalChg.showNextPageButton(false);
			m_oFrameStockBalChg.showPrevPageButton(false);
			return;
		}
		
		m_oFrameStockBalChg.showNextPageButton(true);
		m_oFrameStockBalChg.showPrevPageButton(true);
		
		if(m_iCurrentPage == 1)
			m_oFrameStockBalChg.showPrevPageButton(false);
		if(m_iCurrentPage == m_iTotalPage)
			m_oFrameStockBalChg.showNextPageButton(false);
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
				if(updateEndAndOpenBalance()) {
					m_oItemIDModifiedValuesPairs.clear();
				}
			}
			else {
				m_oItemIDModifiedValuesPairs.clear();
			}
		}
	}
	
	private boolean updateEndAndOpenBalance() {
		// update the end balance for stock transactions
		if (processSave(m_oItemIDStockTranPairs)) {
			// Update the open balance for next business date
			// Get all transactions first
			AppThreadManager oAppThreadManager = new AppThreadManager();
			PosStockTransactionList oPosStockTransactionList = new PosStockTransactionList();
			Object[] oParameters = new Object[4];
			oParameters[0] = m_sBdayId;
			oParameters[1] = AppGlobal.g_oFuncOutlet.get().getOutletId();
			oParameters[2] = PosStockTransaction.TRANSACTION_TYPE_OPEN_BALANCE;
			oParameters[3] = "next";//get next date stock transaction

			oAppThreadManager.addThread(1, oPosStockTransactionList, "searchStockTransactionByType", oParameters);

			// Run all of the threads
			oAppThreadManager.runThread();

			// Wait for the thread to finish
			oAppThreadManager.waitForThread();
			
			HashMap<String, PosStockTransaction> oPosStockTransactionsList = new HashMap<String, PosStockTransaction>(oPosStockTransactionList.getStockTransaction());
			List<PosStockTransaction> oPosStockTransactions = new ArrayList<PosStockTransaction>(oPosStockTransactionsList.values());
			
			TreeMap<Integer, PosStockTransaction> oItemIDNextStockTranPairs = new TreeMap<Integer, PosStockTransaction>();
			for(PosStockTransaction oTempStockTransaction : oPosStockTransactions) {
				oItemIDNextStockTranPairs.put(Integer.valueOf(oTempStockTransaction.getItemId()), oTempStockTransaction);
			}
			
			// update the open balance for stock transactions next business date
			if (!processSave(oItemIDNextStockTranPairs))
				return false;
		}
		
		loadStockTransactionsAtPage(m_iCurrentPage);
		return true;
	}
	
	private boolean processSave(TreeMap<Integer, PosStockTransaction> oItemIDStockTranPairs) {
		if (!m_oItemIDModifiedValuesPairs.isEmpty()) {
			AppThreadManager oAppThreadManager = new AppThreadManager();
			ArrayList<PosStockTransaction> oPosMultiStockTransactions = new ArrayList<PosStockTransaction>();
			
			for(Entry<Integer, BigDecimal[]> entry : m_oItemIDModifiedValuesPairs.entrySet()) {
				int iMenuItemId = entry.getKey().intValue();
				String sLogRemark = "MenuItemId:"+iMenuItemId;
				
				// New value
				BigDecimal[] oValues = entry.getValue();
				BigDecimal dNewStockInQty = oValues[1];
				
				PosStockTransaction oPosStockTransaction = oItemIDStockTranPairs.get(iMenuItemId);
				oPosStockTransaction.setTransactionQty(dNewStockInQty);
				
				oPosMultiStockTransactions.add(oPosStockTransaction);
				
				// Add log to action log list
				sLogRemark += "[bdayId:"+oPosStockTransaction.getBusinessDayId()+"]";
				sLogRemark += "[TransactionType:"+oPosStockTransaction.getTransactionType()+"]";
				sLogRemark += "[NewStockInQty:"+dNewStockInQty+"]";
				AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.stock_balance_chg.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", sLogRemark);
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
			}
			
			//Update stock values in memory
			if(((Boolean)oAppThreadManager.getResult(1)).booleanValue()) {
				return true;
			}
			else {
				return false;
			}
			
		}
		
		return true;
	}
	
	//Calculate page number
	private int calculateTotalPageOfPairs(TreeMap<Integer, Integer> oNameIDMenuItemPairs) {
		if(oNameIDMenuItemPairs.isEmpty())
			return 0;
		
		int iMaxRecord = 15;
		int iTotalPage = 0;
		iTotalPage = oNameIDMenuItemPairs.size() / iMaxRecord;
		if(oNameIDMenuItemPairs.size() % iMaxRecord > 0) {
			iTotalPage++;
		}
		
		return iTotalPage;
	}
	
	@Override
	public void FrameStockBalanceChange_clickSave() {
		if (updateEndAndOpenBalance()) {
			m_oItemIDModifiedValuesPairs.clear();
			
			//Redraw common basket
			reloadFrameStockCommonBasket();
		}
	}

	@Override
	public void FrameStockBalanceChange_clickExit() {
		// Finish showing this form
		this.finishShow();
	}

	@Override
	public void FrameStockBalanceChange_clickBasketMenuItem(int iNewSectionId, int iNewItemIndex, int iNewFieldIndex, String sPrevValue) {
		//Check if previous edit is modified
		if((m_iCurrentItemListItemIndex >= 0) && (m_iCurrentItemListFieldIndex == 3)) {
			boolean bNeedSave = false;
			boolean bSetBackgroundColor = false;
			List<Integer> oMenuItemIdList = new ArrayList<Integer>((m_sCurrentSearchKeyWord.length()<=0)?m_oOperatingPairs.values():m_oSearchResultMenuItemPairs.values());
			int iItemId = oMenuItemIdList.get((m_iCurrentPage-1)*15 + m_iCurrentItemListItemIndex);
			
				BigDecimal[] oMenuItemOrgValues = m_oItemIDStockValuesPairs.get(Integer.valueOf(iItemId));
				
				if(sPrevValue != null){
					BigDecimal dPrevValue = new BigDecimal(sPrevValue);
					if(oMenuItemOrgValues[1].compareTo(dPrevValue) != 0) {
						bNeedSave = true;
						bSetBackgroundColor = true;
					}
					
					if(bNeedSave && m_oItemIDModifiedValuesPairs.containsKey(Integer.valueOf(iItemId)))
						m_oItemIDModifiedValuesPairs.remove(Integer.valueOf(iItemId));
					
					if(bNeedSave) {
						BigDecimal[] oMenuItemNewValues = {BigDecimal.ZERO, BigDecimal.ZERO};
						oMenuItemNewValues[0] = oMenuItemOrgValues[0];
						oMenuItemNewValues[1] = dPrevValue;
						
						m_oItemIDModifiedValuesPairs.put(Integer.valueOf(iItemId), oMenuItemNewValues);
					}
					else {
						if(m_oItemIDModifiedValuesPairs.containsKey(Integer.valueOf(iItemId)))
							m_oItemIDModifiedValuesPairs.remove(Integer.valueOf(iItemId));
					}
					//Tell FrameCommonBasketCell to set bg color
					m_oFrameStockBalChg.setCellFieldBackgroundColorEdited(m_iCurrentItemListSection, m_iCurrentItemListItemIndex, m_iCurrentItemListFieldIndex, bSetBackgroundColor);
				}
//			}
		}
		
		m_iCurrentItemListSection = iNewSectionId;
		m_iCurrentItemListItemIndex = iNewItemIndex;
		m_iCurrentItemListFieldIndex = iNewFieldIndex;
	}

	@Override
	public void FrameStockBalanceChange_clickSearchByName(String sValue) {
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
	public void FrameStockBalanceChange_clickSearchByDepartment() {
		
	}

	@Override
	public void FrameStockBalanceChange_clickSearchByCategory() {
		
	}

	@Override
	public void FrameStockBalanceChange_clickShowAllResult() {
		m_sCurrentSearchKeyWord = "";
		// Clear search value
		m_oFrameStockBalChg.clearSearchValue();
		
		checkUnsavedItems();
		
		m_iTotalPage = calculateTotalPageOfPairs(m_oOperatingPairs);
		m_iCurrentPage = 1;
		m_iCurrentItemListSection = -1;
		m_iCurrentItemListItemIndex = -1;
		m_iCurrentItemListFieldIndex = -1;
		loadStockTransactionsAtPage(m_iCurrentPage);
		updatePageButtonsVisibility();
	}

	@Override
	public void FrameStockBalanceChange_clickPrevPage() {
		checkUnsavedItems();
		
		if (m_iCurrentPage < 2)
			return;
		loadStockTransactionsAtPage(--m_iCurrentPage);
		updatePageButtonsVisibility();
	}

	@Override
	public void FrameStockBalanceChange_clickNextPage() {
		checkUnsavedItems();
		
		if (m_iCurrentPage == m_iTotalPage)
			return;
		loadStockTransactionsAtPage(++m_iCurrentPage);
		updatePageButtonsVisibility();
	}

	@Override
	public void FrameStockBalanceChange_clickDate(String sValue) {
		PosBusinessDay oBusinessDay = new PosBusinessDay();
		if (oBusinessDay.readByDateOutletId(sValue, AppGlobal.g_oFuncOutlet.get().getOutletId())) {
			m_sBdayId = oBusinessDay.getBdayId();
			
			resetValue();
			initOperationStock();
			loadStockTransactionsAtPage(m_iCurrentPage);
			updatePageButtonsVisibility();
		}
	}
}
