package app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import commonui.FormInputBox;
import core.Controller;
import om.PosCheck;
import om.PosOutletTable;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

public class FormSelectSplitTable extends VirtualUIForm implements FrameSelectSplitTableListener {
	TemplateBuilder m_oTemplateBuilder;
	private FrameSelectSplitTable m_oFrameSelectSplitTable;
	
	private List<PosCheck> m_oAvailableCheckList;
	private List<HashMap<String, Object>> m_oSelectedLineList;
	
	public static String LIST_KEY_TABLE_NUMBER = "n";
	public static String LIST_KEY_TABLE_EXTENSION = "x";
	public static String LIST_KEY_GUEST = "g";
	public static String LIST_KEY_ORIGINAL_GUEST = "og";
		
	public static String COLOR_SELECTED = "#FF7F27";
	public static String COLOR_UNSELECTED = "#575757";
	
	private boolean m_bExit;
	
	private boolean m_bHasNewTable;
	private boolean m_bHasOldTable;
	
	public FormSelectSplitTable(Controller oParentController){
		super(oParentController);
		
		m_oAvailableCheckList = new ArrayList<PosCheck>();
		m_oSelectedLineList = new ArrayList<HashMap<String, Object>>();
		m_bExit = false;
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmSelectSplitTable.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameSelectSplitTable = new FrameSelectSplitTable();
		m_oTemplateBuilder.buildFrame(m_oFrameSelectSplitTable, "fraSelectSplitTable");
		m_oFrameSelectSplitTable.addListener(this);
		this.attachChild(m_oFrameSelectSplitTable);
	}
	
	public void init(List<PosCheck> oCheckList, String sSelectedCheckPrefixNo) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		
		for(int i = 0 ; i < oCheckList.size(); i++) {
			PosCheck oCheck = oCheckList.get(i);
			
			String sCheckPrefixNo = oCheck.getCheckPrefixNo();
			String sTableNo = AppGlobal.g_oFuncOutlet.get().getTableNameWithTableNo(Integer.toString(oCheck.getTable()), oCheck.getTableExtension())[AppGlobal.g_oCurrentLangIndex.get()-1];
			String sGuest = Integer.toString(oCheck.getGuests());
    		String sOpenTime = timeFormat.format(oCheck.getOpenLocTime().toDate());
    		String sCheckTotal = AppGlobal.g_oFuncOutlet.get().roundCheckAmountToString(oCheck.getCheckTotal());
    		
			m_oFrameSelectSplitTable.init(i, sCheckPrefixNo, sTableNo, sGuest, sOpenTime, sCheckTotal);
			
			m_oAvailableCheckList.add(oCheck);
			
			if(!sSelectedCheckPrefixNo.isEmpty() && oCheck.getCheckPrefixNo().compareTo(sSelectedCheckPrefixNo) == 0) {
				m_oFrameSelectSplitTable.addSelectedTable(0, sCheckPrefixNo, sTableNo, sGuest, sOpenTime, sCheckTotal);

	    		this.addItemToSelectedList(oCheck.getTable(), oCheck.getTableExtension(), oCheck.getGuests(), oCheck.getGuests());
	    		m_oFrameSelectSplitTable.setAvailableTableRowColor(0, i, COLOR_SELECTED);
			}
		}
	}

	public void addItemToSelectedList(int iTableNo, String sTableExtension, int iGuest, int iOriginalGuest) {
		HashMap<String, Object> oTableInfo = new HashMap<String, Object>();
    	oTableInfo.put(LIST_KEY_TABLE_NUMBER, iTableNo);
    	oTableInfo.put(LIST_KEY_TABLE_EXTENSION, sTableExtension);
    	oTableInfo.put(LIST_KEY_GUEST, iGuest);
    	oTableInfo.put(LIST_KEY_ORIGINAL_GUEST, iOriginalGuest);
		m_oSelectedLineList.add(oTableInfo);
	}

	public int getAvailableTableListIndex(int iTableNo, String sTableExtension) {
		int iIndex = -1;
		for(int i = 0; i < m_oAvailableCheckList.size(); i++) {
			PosCheck oCheck = m_oAvailableCheckList.get(i);
			if(iTableNo == oCheck.getTable() && sTableExtension.equals(oCheck.getTableExtension())) {
				iIndex = i;
				break;
			}	
		}
		return iIndex;
	}
	
	public int getSelectedTableListIndex(int iTableNo, String sTableExtension) {
		int iIndex = -1;
		for(int i = 0; i < m_oSelectedLineList.size(); i++) {
			HashMap<String, Object> oTableInfo = m_oSelectedLineList.get(i);
			int iCurrentTableNo = ((Integer) oTableInfo.get(LIST_KEY_TABLE_NUMBER)).intValue();
			String sCurrentTableExtension = (String) oTableInfo.get(LIST_KEY_TABLE_EXTENSION);
			
			if(iCurrentTableNo == iTableNo && sCurrentTableExtension.equals(sTableExtension)) {
				iIndex = i;
				break;
			}
		}
		
		return iIndex;
	}
	
	public boolean checkSelectedList() {
		boolean bHaveOldCheck = false;
		for(HashMap<String, Object> oTableInfo: m_oSelectedLineList) {
			int iTableNo = ((Integer) oTableInfo.get(LIST_KEY_TABLE_NUMBER)).intValue();
			String sTableExtension = (String) oTableInfo.get(LIST_KEY_TABLE_EXTENSION);
			int iIndex = this.getAvailableTableListIndex(iTableNo, sTableExtension);
			if(iIndex > -1) {
				bHaveOldCheck = true;
				break;
			}
		}
		
		if(!bHaveOldCheck) {
			String sErrMsg = AppGlobal.g_oLang.get()._("must_select_at_least_one_existing_table")
					+ System.lineSeparator() + AppGlobal.g_oLang.get()._("please_select_tables_again");
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(sErrMsg);
			oFormDialogBox.show();
			oFormDialogBox = null;
			return false;
		}
		
		if(m_oSelectedLineList.size() < 2) {
			String sErrMsg = AppGlobal.g_oLang.get()._("must_have_at_least_two_tables_for_split_table")
					+ System.lineSeparator() + AppGlobal.g_oLang.get()._("please_select_tables_again");
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(sErrMsg);
			oFormDialogBox.show();
			oFormDialogBox = null;
			return false;
		}
		
		return true;
	}
	
	public List<HashMap<String, Object>> getSplitTableList() {
		return m_oSelectedLineList;
	}
	
	public boolean isExit() {
		return this.m_bExit;
	}

	private int getAvailableSelectedTableCover(){
		boolean bHasOldTable = false;
		int iAvailableCover = 0;
		m_bHasNewTable = false;
		m_bHasOldTable = false;
		for(HashMap<String, Object> oTableInfo: m_oSelectedLineList) {
			int iTableNo = ((Integer) oTableInfo.get(LIST_KEY_TABLE_NUMBER)).intValue();
			String sTableExtension = (String) oTableInfo.get(LIST_KEY_TABLE_EXTENSION);
			int iIndex = this.getAvailableTableListIndex(iTableNo, sTableExtension);
			if(iIndex > -1){
				if(bHasOldTable == false){
					iAvailableCover += (Integer) oTableInfo.get(LIST_KEY_ORIGINAL_GUEST);
					bHasOldTable = true;
				}
				m_bHasOldTable = true;
			}	
			else{
				iAvailableCover -= (Integer) oTableInfo.get(LIST_KEY_ORIGINAL_GUEST);
				m_bHasNewTable = true;
			}
		}
		return iAvailableCover;
	}
	
	private void updateOldTableCover(){
		int iAvailableCover = getAvailableSelectedTableCover();
		int iFinalCover = 0;
		
		for(int i=0; i<m_oSelectedLineList.size(); i++){
			int iTableNo = ((Integer) m_oSelectedLineList.get(i).get(LIST_KEY_TABLE_NUMBER)).intValue();
			String sTableExtension = (String) m_oSelectedLineList.get(i).get(LIST_KEY_TABLE_EXTENSION);
			int iIndex = this.getAvailableTableListIndex(iTableNo, sTableExtension);
			if(iIndex > -1){
				if(m_bHasNewTable){
					if(iAvailableCover <= 0)
						iFinalCover = 0;
					else
						iFinalCover = iAvailableCover;
					m_oSelectedLineList.get(i).put(LIST_KEY_GUEST, iFinalCover);
					m_oFrameSelectSplitTable.updateSelectedTableCover(i, iFinalCover);
					//only split cover from the first old table
					break;
				}else{
					m_oSelectedLineList.get(i).put(LIST_KEY_GUEST, (Integer) m_oSelectedLineList.get(i).get(LIST_KEY_ORIGINAL_GUEST));
					m_oFrameSelectSplitTable.updateSelectedTableCover(i, (Integer) m_oSelectedLineList.get(i).get(LIST_KEY_ORIGINAL_GUEST));
				}
			}
		}
	}
	
	@Override
	public void fraSelectSplitTable_clickExit() {
		// Finish showing this form
		m_bExit = true;
		this.finishShow();
	}

	@Override
	public void fraSelectSplitTable_clickNext() {
		if(!checkSelectedList())
			return;
		
		this.finishShow();
	}

	@Override
	public void fraSelectSplitTable_clickBasketTable(int iSectionId,
			int iItemIndex, int iFieldIndex, String sNote) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		String sBackgroundColor = COLOR_SELECTED;

		if(sNote.equals(FrameSelectSplitTable.BASKET_TYPE_AVAILABLE)) { // item in available table list is clicked
			PosCheck oCheck = m_oAvailableCheckList.get(iItemIndex);
			String sTableNo = AppGlobal.g_oFuncOutlet.get().getTableNameWithTableNo(Integer.toString(oCheck.getTable()), oCheck.getTableExtension())[AppGlobal.g_oCurrentLangIndex.get()-1];
			
			int iIndex = getSelectedTableListIndex(oCheck.getTable(), oCheck.getTableExtension()); 
			if(iIndex > -1) {
				sBackgroundColor = COLOR_UNSELECTED;
				
				m_oFrameSelectSplitTable.removeSelectedTable(iIndex);
				m_oSelectedLineList.remove(iIndex);
			} else {
	    		String sCheckPrefixNo = oCheck.getCheckPrefixNo();
	    		String sGuest = Integer.toString(oCheck.getGuests());
	    		String sOpenTime = timeFormat.format(oCheck.getOpenLocTime().toDate());
	    		String sCheckTotal = AppGlobal.g_oFuncOutlet.get().roundCheckAmountToString(oCheck.getCheckTotal());
	    		m_oFrameSelectSplitTable.addSelectedTable(m_oSelectedLineList.size(), sCheckPrefixNo, sTableNo, sGuest, sOpenTime, sCheckTotal);
				
				this.addItemToSelectedList(oCheck.getTable(), oCheck.getTableExtension(), oCheck.getGuests(), oCheck.getGuests());
			}
			//update the color of list row in available table list
			m_oFrameSelectSplitTable.setAvailableTableRowColor(iSectionId, iItemIndex, sBackgroundColor);
		} else { // item in selected table list is clicked, unclicked selected table
			HashMap<String, Object> oTableInfo = m_oSelectedLineList.get(iItemIndex);
			int iTableNo = ((Integer) oTableInfo.get(LIST_KEY_TABLE_NUMBER)).intValue();
			String sTableExtension = (String) oTableInfo.get(LIST_KEY_TABLE_EXTENSION);
			
			m_oFrameSelectSplitTable.removeSelectedTable(iItemIndex);
			m_oSelectedLineList.remove(iItemIndex);
			
			//update the color of list row in available table list
			int iAvailableListIndex = this.getAvailableTableListIndex(iTableNo, sTableExtension);
			if(iAvailableListIndex > -1)
				m_oFrameSelectSplitTable.setAvailableTableRowColor(iSectionId, iAvailableListIndex, COLOR_UNSELECTED);
		}
		
		if(AppGlobal.g_oFuncStation.get().getSplitTableWithKeepingCover() == false)
			updateOldTableCover();
	}

	@Override
	public void fraSelectSplitTable_addNewTable() {
		String sErrMsg;
		
		// Ask new table
		FormAskTable oFormAskTable = new FormAskTable(this);
		oFormAskTable.init(0, true, AppGlobal.g_oFuncOutlet.get().getOutletId());
		oFormAskTable.setTitle(AppGlobal.g_oLang.get()._("create_new_table"));
		oFormAskTable.show();

		if(oFormAskTable.isCanelClick())
			return ;
		
		Integer oNewTable = oFormAskTable.getTableNo();
		String sNewTableExtension = oFormAskTable.getTableExtension();

		if(oNewTable == null)
			return;
		
		PosOutletTable oOutletTable = new PosOutletTable();
		oOutletTable.readByOutletIdTable(AppGlobal.g_oFuncOutlet.get().getOutletId(), oFormAskTable.getTableNo(), sNewTableExtension);
		if(!oOutletTable.getOtblId().equals("")) {
			// The table is occupied
			if(!oOutletTable.getCheckId().equals("")){
				sErrMsg = AppGlobal.g_oLang.get()._("table") + " " + oNewTable.intValue() + sNewTableExtension + " " + AppGlobal.g_oLang.get()._("is_occupied");
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
				oFormDialogBox.setMessage(sErrMsg);
				oFormDialogBox.show();
				oFormDialogBox = null;
				return;
			}
			
			if(oOutletTable.isCleaningTable()){
				sErrMsg = AppGlobal.g_oLang.get()._("target_table_is_cleaning");
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
				oFormDialogBox.setMessage(sErrMsg);
				oFormDialogBox.show();
				oFormDialogBox = null;
				return;
			}
		}
		
		// check whether the table is already in selected table list
		if(this.getSelectedTableListIndex(oNewTable.intValue(), sNewTableExtension) > -1) {
			sErrMsg = AppGlobal.g_oLang.get()._("table") + " " + oNewTable.intValue() + sNewTableExtension + " " + AppGlobal.g_oLang.get()._("is_duplicated");
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormDialogBox.setMessage(sErrMsg);
			oFormDialogBox.show();
			oFormDialogBox = null;
			return;
		}

		// Ask new cover
		FormInputBox oFormInputBox = new FormInputBox(this);
		oFormInputBox.init();
		oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.NUMBER);
		oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("cover"));
		oFormInputBox.setMessage(AppGlobal.g_oLang.get()._("please_input_the_new_cover")+":");
		oFormInputBox.show();
		
		String sNewCover = oFormInputBox.getInputValue();
		
		if(sNewCover == null)
			return;
		
		try{
			Integer.valueOf(sNewCover);
		}catch(NumberFormatException e){
			sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(sErrMsg);
			oFormDialogBox.show();
			return;
		}
		
		//Check if new cover exceed cover limitation
		int iCoverLimitNumber = AppGlobal.g_oFuncStation.get().getCoverUpperBound();
		if (iCoverLimitNumber > 0) {
			if(Integer.valueOf(sNewCover) > iCoverLimitNumber){
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage( AppGlobal.g_oLang.get()._("cannot_input_cover_more_than") + " " + iCoverLimitNumber);
				oFormDialogBox.show();
				return;
			}
		}
		
		// Checking the cover warning
		int iCoverWarning = AppGlobal.g_oFuncStation.get().getCoverWarningLimit();
		if (iCoverWarning > 0) {
			if (Integer.valueOf(sNewCover) > iCoverWarning) {
				FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"),
						AppGlobal.g_oLang.get()._("no"), this);
				oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
				oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("exceed_the_cover_limit") + " : " + iCoverWarning + 
						System.lineSeparator() + AppGlobal.g_oLang.get()._("continue") + "?");
				oFormConfirmBox.show();
				if (oFormConfirmBox.isOKClicked() == false)
					return;
			}
		}
		
		//by default "split_table_with_keeping_cover" is false
		//if it is true, continue the old flow to keep cover when splitting table
		//if it is false, do new flow
		if(AppGlobal.g_oFuncStation.get().getSplitTableWithKeepingCover() == false){
			int iCover = getAvailableSelectedTableCover();
			if(m_bHasOldTable){
				if(Integer.valueOf(sNewCover) > iCover){
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cover_number_is_bigger_than_original_table_cover"));
					oFormDialogBox.show();
				}
			}
		}
		
		String sTableName = AppGlobal.g_oFuncOutlet.get().getTableNameWithTableNo(oNewTable.toString(), sNewTableExtension)[AppGlobal.g_oCurrentLangIndex.get()-1];
		m_oFrameSelectSplitTable.addSelectedTable(this.m_oSelectedLineList.size(), "--"+AppGlobal.g_oLang.get()._("new")+"--", sTableName, sNewCover, "--:--:--", "---");

		this.addItemToSelectedList(oNewTable.intValue(), sNewTableExtension, Integer.parseInt(sNewCover), Integer.parseInt(sNewCover));
		
		if(AppGlobal.g_oFuncStation.get().getSplitTableWithKeepingCover() == false)
			updateOldTableCover();
	}
}
