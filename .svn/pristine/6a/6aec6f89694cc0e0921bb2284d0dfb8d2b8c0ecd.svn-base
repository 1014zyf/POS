package app;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import om.PosOutletTable;

import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import core.Controller;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

public class FormSplitCheckByOptions extends VirtualUIForm implements FrameSplitCheckByOptionsListener {
	private TemplateBuilder m_oTemplateBuilder;
	
	private FrameSplitCheckByOptions m_oFrameSplitCheckByOptions;
	
	private FuncCheck m_oFuncCheck;
	private List<HashMap<String, Object>> m_oTableInfoList;

	private int m_iNumberOfTables;
	private List<String> m_oAvailableTableExtensionList;
	private boolean m_bAutoGenTable;
	private String m_sSplitCheckOption;
	
	private String m_sTargetTable;
	private String m_sTargetTableExtension;

	private boolean m_bConfirmSplit;

	private List<Integer> m_oSeatNoList;
	
	public static String SPLIT_CHECK_OPTION_EQUAL_SPLIT = "e";
	public static String SPLIT_CHECK_OPTION_SPECIFIC_AMOUNT = "a";
	public static String SPLIT_CHECK_OPTION_SPECIFIC_PERCENTAGE = "p";
	public static String SPLIT_CHECK_OPTION_SEAT_NO = "s";
	
	public static String TABLE_INFO_TABLE = "t";
	public static String TABLE_INFO_TABLE_EXT = "x";
	public static String TABLE_INFO_SPLIT_AMOUNT = "a";
	
	public FormSplitCheckByOptions(String sSplitCheckOption, Controller oParentController) {
		super(oParentController);
		
		m_oTableInfoList = new ArrayList<HashMap<String, Object>>();
		m_bConfirmSplit = false;

		m_sSplitCheckOption = sSplitCheckOption;
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmSplitCheckByOptions.xml");

		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Load form from template file
		m_oFrameSplitCheckByOptions = new FrameSplitCheckByOptions();
		m_oTemplateBuilder.buildFrame(m_oFrameSplitCheckByOptions, "fraSplitCheckByOptions");
		
		// Add listener
		m_oFrameSplitCheckByOptions.addListener(this);
		this.attachChild(m_oFrameSplitCheckByOptions);
	}
	
	public void setTitle(String sTitle) {
		m_oFrameSplitCheckByOptions.setTitle(sTitle);
	}

	public void setNumberOfTableDesc(String sValue) {
		m_oFrameSplitCheckByOptions.setNumberOfTableDesc(sValue);
	}
	
	public void setSplitTableInfo(FuncCheck oFuncCheck, int iNumberOfTables, List<String> oAvailableTableExtensionList, boolean bAutoGenTable, List<Integer> oSeatNoList, boolean bSetTimer) {
		m_iNumberOfTables = iNumberOfTables;
		m_oAvailableTableExtensionList = oAvailableTableExtensionList;
		m_bAutoGenTable = bAutoGenTable;
		m_oFuncCheck = oFuncCheck;
		m_oSeatNoList = oSeatNoList;
		
		if (bSetTimer == true){
			//For Android client, there will be a screen showing the selected table for "Equal Quantity Split" and waiting for asking table to be display.
			//In order to prevent user input anything wrongly, system will be show the selected table frame unit user input all table number
			m_oFrameSplitCheckByOptions.setVisible(false);
			m_oFrameSplitCheckByOptions.addFinishShowTimer();
			m_oFrameSplitCheckByOptions.startFinishShowTimer();
		}
		
		m_oFrameSplitCheckByOptions.setVisibleForInputFrame(true);
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		
		iFieldWidths.add(120);
		if(m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SEAT_NO))
			sFieldValues.add(AppGlobal.g_oLang.get()._("seat"));
		else
			sFieldValues.add(AppGlobal.g_oLang.get()._("order"));
		
		iFieldWidths.add(120);
		sFieldValues.add(AppGlobal.g_oLang.get()._("table"));
		
		iFieldWidths.add(120);
		if(m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SPECIFIC_AMOUNT)){
			sFieldValues.add(AppGlobal.g_oLang.get()._("amount"));
			// Add Subtitle and Description
			m_oFrameSplitCheckByOptions.setLabelSubTitle(AppGlobal.g_oLang.get()._("specific_amount"));
			m_oFrameSplitCheckByOptions.setLabelDescription(AppGlobal.g_oLang.get()._("please_input_split_amount"));
		}else if(m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SPECIFIC_PERCENTAGE)){
			sFieldValues.add(AppGlobal.g_oLang.get()._("percentage"));
			// Add Subtitle and Description
			m_oFrameSplitCheckByOptions.setLabelSubTitle(AppGlobal.g_oLang.get()._("specific_percentage"));
			m_oFrameSplitCheckByOptions.setLabelDescription(AppGlobal.g_oLang.get()._("please_input_split_percentage"));
		}
		m_oFrameSplitCheckByOptions.addHeader(iFieldWidths, sFieldValues);
		m_oFrameSplitCheckByOptions.setFocusTextboxValue();
		m_oFrameSplitCheckByOptions.setTextboxValue(this.generateDefaultSplitAmount());
	}
	
	public void addSplitTableInfo() {
		boolean bResult = true;
		boolean bAskTable = false;
		boolean bAskAmount = false;
		
		// Set bAskTable and bAskAmount
		if (m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_EQUAL_SPLIT) || m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SEAT_NO)) {
			bAskTable = true;
		}else {
			if(!m_bAutoGenTable)
				bAskTable = true;
			bAskAmount = true;
		}
		
		int j = 0;
		int iBlankSeatNo = 0;
		String sTableNo = "";
		String sTableExtension = "";
		
		// When the option specific amount or specific percentage is chosen, call addTableInfo once
		if(m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SPECIFIC_AMOUNT) || m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SPECIFIC_PERCENTAGE)) {
			if(m_bAutoGenTable) {
				sTableNo = m_oFuncCheck.getTableNo();
				sTableExtension = m_oAvailableTableExtensionList.get(0);
			}
			
			if(this.addTableInfo(0, sTableNo, sTableExtension, bAskTable, bAskAmount, iBlankSeatNo) == false)
				bResult = false;
		} else {
			if(m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SEAT_NO)){		
				for (int iSeatNo : m_oSeatNoList) {
					if(iSeatNo !=0){				
						if (this.addTableInfo(j, m_oFuncCheck.getTableNo(), m_oAvailableTableExtensionList.get(j), bAskTable, bAskAmount, iSeatNo) == false) {
							bResult = false;
							break;
						}
					j++;
					}
				}
			} else {
				for(int i = 0; i < m_iNumberOfTables-1; i++) {
					if(m_bAutoGenTable) {
						if(this.addTableInfo(i, m_oFuncCheck.getTableNo(), m_oAvailableTableExtensionList.get(i), bAskTable, bAskAmount, iBlankSeatNo) == false) {
							bResult = false;
							break;
						}
					} else {
						if(this.addTableInfo(i, "", "", bAskTable, bAskAmount, iBlankSeatNo) == false) {
							bResult = false;
							break;
						}
					}
				}
			}
			
			if(!bResult)
				this.finishShow();
			else {
				//Show back the selected table list for user to view before confirm to split check
				m_oFrameSplitCheckByOptions.setVisible(true);
				
				FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
				oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
				oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("continue_to_split_check")+"?");
				oFormConfirmBox.show();
				if(oFormConfirmBox.isOKClicked())
					m_bConfirmSplit = true;
				
				this.finishShow();
			}
		}
	}
	
	public boolean addTableInfo(int iIndex, String sTable, String sTableExt, boolean bAskTable, boolean bAskAmount, int iSeatNo) {
		HashMap<String, Object> oTableInfo = new HashMap<String, Object>();
		String sErrMsg = "";
		m_sTargetTable = sTable;
		m_sTargetTableExtension = sTableExt;
		
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		ArrayList<String> sFieldTypes = new ArrayList<String>();
		
		if(m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SEAT_NO)) {
			iFieldWidths.add(120);
			sFieldValues.add(Integer.toString(iSeatNo));
		} else {
			iFieldWidths.add(120);
			sFieldValues.add(Integer.toString(iIndex+1));
		}
		
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.LEFT);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		
		iFieldWidths.add(120);
		sFieldValues.add("");
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.LEFT);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		iFieldWidths.add(120);
		sFieldValues.add("-");
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.LEFT);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		
		m_oFrameSplitCheckByOptions.addTableInfo(0, iIndex, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes);
		
		if(bAskTable) {
			// Ask new table
			while(true){
				FormAskTable oFormAskTable = new FormAskTable(this);
				oFormAskTable.init(0, true, AppGlobal.g_oFuncOutlet.get().getOutletId());
				if(!sTable.isEmpty()) {
					if(oFormAskTable.getKeyboardMode() == FuncStation.OPEN_TABLE_SCREEN_KEYBOARD_NUMBER){
						if(!sTable.equals("0")){
							oFormAskTable.setDefaultTableNo(sTable);
							oFormAskTable.setDefaultTableExtension(sTableExt);
						}
					}else{
						if(sTable.equals("0")){
							oFormAskTable.setDefaultTableNo("");
						}
					}
				}
				oFormAskTable.setTitle(AppGlobal.g_oLang.get()._("split_to_table"));

				oFormAskTable.show();
				
				if(oFormAskTable.isCanelClick()){
					FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
					oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
					oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("abort_split_check")+"?");
					oFormConfirmBox.show();
					
					if(oFormConfirmBox.isOKClicked()) {
						this.finishShow();
						return false;
					} else
						continue;
				}
				
				m_sTargetTable = Integer.toString(oFormAskTable.getTableNo());
				m_sTargetTableExtension = oFormAskTable.getTableExtension();
				
				if(m_sTargetTable.equals(m_oFuncCheck.getTableNo()) && m_sTargetTableExtension.equals(m_oFuncCheck.getTableExtension())) {
					sErrMsg = AppGlobal.g_oLang.get()._("cannot_split_to_current_table");
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(sErrMsg);
					oFormDialogBox.show();
					oFormDialogBox = null;
					continue;
				} else {
					boolean bTableInUse = false;
					PosOutletTable oPosOutletTable = new PosOutletTable();
					oPosOutletTable.readByOutletIdTable(AppGlobal.g_oFuncOutlet.get().getOutletId(), Integer.parseInt(m_sTargetTable), m_sTargetTableExtension);
					if(!oPosOutletTable.getOtblId().equals("")) {
						if (oPosOutletTable.getStationId() > 0 && oPosOutletTable.getStationId() != AppGlobal.g_oFuncStation.get().getStationId()) {
							FuncStation oStation = new FuncStation();
							if(oStation.loadStationById(oPosOutletTable.getStationId())){
								String sStationName = oStation.getName(AppGlobal.g_oCurrentLangIndex.get());
								sErrMsg = AppGlobal.g_oLang.get()._("table_is_locked_by_station")+" "+sStationName;
							}else
								sErrMsg = AppGlobal.g_oLang.get()._("table_is_locked_by_station")+" "+oPosOutletTable.getStationId();
							bTableInUse = true;
						}
						else if(oPosOutletTable.isCleaningTable()){
							sErrMsg = AppGlobal.g_oLang.get()._("target_table_is_cleaning");
							bTableInUse = true;
						}
						
						if(!oPosOutletTable.getCheckId().equals("")){
							sErrMsg = AppGlobal.g_oLang.get()._("table")+ " " + AppGlobal.g_oFuncOutlet.get().getTableName(Integer.toString(oPosOutletTable.getTable()), oPosOutletTable.getTableExtension())[AppGlobal.g_oCurrentLangIndex.get()-1] + " " + AppGlobal.g_oLang.get()._("is_occupied_please_select_other_table");
							bTableInUse = true;
						}
					}
					
					if(bTableInUse) {
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(sErrMsg);
						oFormDialogBox.show();
						oFormDialogBox = null;
						continue;
					} else {
						boolean bDuplicateTable = false;
						for(HashMap<String, Object> oTableInfo1: m_oTableInfoList) {
							String sTableNo = (String) oTableInfo1.get(TABLE_INFO_TABLE);
							String sTableExtension = (String) oTableInfo1.get(TABLE_INFO_TABLE_EXT);
							
							if(m_sTargetTable.equals(sTableNo) && m_sTargetTableExtension.equals(sTableExtension)) {
								bDuplicateTable = true;
								break;
							}
						}
						
						if(bDuplicateTable) {
							sErrMsg = AppGlobal.g_oLang.get()._("duplicate_table_no");
							FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
							oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
							oFormDialogBox.setMessage(sErrMsg);
							oFormDialogBox.show();
							oFormDialogBox = null;
							continue;
						}
					}
				}
				oFormAskTable.finishShow();
				m_oFrameSplitCheckByOptions.setVisible(true);
				break;
			}
		}
		oTableInfo.put(TABLE_INFO_TABLE, m_sTargetTable);
		oTableInfo.put(TABLE_INFO_TABLE_EXT, m_sTargetTableExtension);
		m_oFrameSplitCheckByOptions.updateTableInfo(0, iIndex, 1, AppGlobal.g_oFuncOutlet.get().getTableNameWithTableNo(m_sTargetTable, m_sTargetTableExtension)[AppGlobal.g_oCurrentLangIndex.get()-1]);
		
		if(m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SPECIFIC_AMOUNT) || m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SPECIFIC_PERCENTAGE)) {
			sErrMsg = "";
			
			// filter all the cases which is required to ask amount or percentage when specific amount or percentage
			if(m_oFrameSplitCheckByOptions.getTimer().length() == 0) {
				
				while(true){
					if ((m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SPECIFIC_AMOUNT) || m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SPECIFIC_PERCENTAGE))
							&& !sErrMsg.isEmpty()){
						m_oFrameSplitCheckByOptions.removeLastTableInfo(0, iIndex);
						return false;
					}
					
					// Calculate default split amount
					BigDecimal dDefaultSplitAmount = BigDecimal.ZERO;
					for(HashMap<String, Object> oTableInfo1: m_oTableInfoList) {
						BigDecimal dPrevTableSplitAmount = (BigDecimal) oTableInfo1.get(TABLE_INFO_SPLIT_AMOUNT);
						dDefaultSplitAmount = dDefaultSplitAmount.add(dPrevTableSplitAmount);
					}
					
					String sSplitAmount = m_oFrameSplitCheckByOptions.getTextboxValue();
					if(m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SPECIFIC_AMOUNT))
						dDefaultSplitAmount = m_oFuncCheck.getCheckTotal().subtract(dDefaultSplitAmount);
					else
						dDefaultSplitAmount = (new BigDecimal("100")).subtract(dDefaultSplitAmount);
					
					try{
						Double.valueOf(sSplitAmount);
					}catch(NumberFormatException e){
						sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(sErrMsg);
						oFormDialogBox.show();
						continue;
					}
					
					BigDecimal dSplitAmount = new BigDecimal(sSplitAmount);
					if(dSplitAmount.compareTo(BigDecimal.ZERO) == 0){
						sErrMsg = AppGlobal.g_oLang.get()._("split_check_amount_should_be_greater_than_zero");
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(sErrMsg);
						oFormDialogBox.show();
						continue;
					}
					
					BigDecimal dSplitAmountTotal = BigDecimal.ZERO;
					for(HashMap<String, Object> oTableInfo1: m_oTableInfoList) {
						BigDecimal dPrevTableSplitAmount = (BigDecimal) oTableInfo1.get(TABLE_INFO_SPLIT_AMOUNT);
						dSplitAmountTotal = dSplitAmountTotal.add(dPrevTableSplitAmount);
					}
					dSplitAmountTotal = dSplitAmountTotal.add(dSplitAmount);
					
					if(m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SPECIFIC_PERCENTAGE)) {
						dSplitAmountTotal = dSplitAmountTotal.divide(new BigDecimal("100"), 4, RoundingMode.DOWN);
						dSplitAmountTotal = dSplitAmountTotal.multiply(m_oFuncCheck.getCheckTotal());
					}
					
					if (dSplitAmountTotal.compareTo(m_oFuncCheck.getCheckTotal()) > 0) {
						sErrMsg = AppGlobal.g_oLang.get()._("split_check_total_invalid") + System.lineSeparator()
								+ AppGlobal.g_oLang.get()._("original_check_total_will_become_negative");
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(sErrMsg);
						oFormDialogBox.show();
						continue;
					} else if(dSplitAmountTotal.compareTo(m_oFuncCheck.getCheckTotal()) == 0) {
						sErrMsg = AppGlobal.g_oLang.get()._("original_check_total_will_become_zero");
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(sErrMsg);
						oFormDialogBox.show();
						continue;
					}
					
					oTableInfo.put(TABLE_INFO_SPLIT_AMOUNT, dSplitAmount);
					
					m_oFrameSplitCheckByOptions.updateTableInfo(0, iIndex, 2, dSplitAmount.toPlainString());
					break;
				}
			}
		}
		
		// Add table info to table info list if amount is set
		if(m_oFrameSplitCheckByOptions.getTimer().length() == 0)
			m_oTableInfoList.add(oTableInfo);
		else if(bAskTable && !(m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SPECIFIC_AMOUNT) || m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SPECIFIC_PERCENTAGE)))
			m_oTableInfoList.add(oTableInfo);
		else
			m_oFrameSplitCheckByOptions.setFocusTextboxValue();
		return true;
	}

	public boolean isConfirmSplit() {
		return m_bConfirmSplit;
	}
	
	public List<HashMap<String, Object>> getTableInfoList() {
		return m_oTableInfoList;
	}
	
	// Calculate default split amount
	public String generateDefaultSplitAmount(){
		BigDecimal dDefaultSplitAmount = BigDecimal.ZERO;
		String sDefaultSplitAmount = "";
		for(HashMap<String, Object> oTableInfo1: m_oTableInfoList) {
			BigDecimal dPrevTableSplitAmount = (BigDecimal) oTableInfo1.get(TABLE_INFO_SPLIT_AMOUNT);
			dDefaultSplitAmount = dDefaultSplitAmount.add(dPrevTableSplitAmount);
		}
		if(m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SPECIFIC_AMOUNT))
			sDefaultSplitAmount = (m_oFuncCheck.getCheckTotal().subtract(dDefaultSplitAmount)).stripTrailingZeros().toPlainString();
		else
			sDefaultSplitAmount = (new BigDecimal("100")).subtract(dDefaultSplitAmount).stripTrailingZeros().toPlainString();
		
		return sDefaultSplitAmount;
	}
	
	@Override
	public void FrameSplitCheckByOptions_clickOK() {
		boolean bResult = true;
		int iIndex = m_oFrameSplitCheckByOptions.getFrameTableInforListSize();
		
		// required ask table and ask amount when specific amount or percentage
		if(m_oFrameSplitCheckByOptions.getTimer().length() != 0) {
			
			HashMap<String, Object> oTableInfo = new HashMap<String, Object>();	
			String sSplitAmount = m_oFrameSplitCheckByOptions.getTextboxValue();
			
			m_oFrameSplitCheckByOptions.setTextboxValue(this.generateDefaultSplitAmount());
			
			try{
				Double.valueOf(sSplitAmount);
			}catch(NumberFormatException e){
				String sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(sErrMsg);
				oFormDialogBox.show();
				return ;
			}
			
			BigDecimal dSplitAmount = new BigDecimal(sSplitAmount);
			if(dSplitAmount.compareTo(BigDecimal.ZERO) == 0){
				String sErrMsg = AppGlobal.g_oLang.get()._("split_check_amount_should_be_greater_than_zero");
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(sErrMsg);
				oFormDialogBox.show();
				return ;
			}
			
			BigDecimal dSplitAmountTotal = BigDecimal.ZERO;
			for(HashMap<String, Object> oTableInfo1: m_oTableInfoList) {
				BigDecimal dPrevTableSplitAmount = (BigDecimal) oTableInfo1.get(TABLE_INFO_SPLIT_AMOUNT);
				dSplitAmountTotal = dSplitAmountTotal.add(dPrevTableSplitAmount);
			}
			dSplitAmountTotal = dSplitAmountTotal.add(dSplitAmount);
			
			if(m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SPECIFIC_PERCENTAGE)) {
				dSplitAmountTotal = dSplitAmountTotal.divide(new BigDecimal("100"), 4, RoundingMode.DOWN);
				dSplitAmountTotal = dSplitAmountTotal.multiply(m_oFuncCheck.getCheckTotal());
			}
			
			if(dSplitAmountTotal.compareTo(m_oFuncCheck.getCheckTotal()) > 0) {
				String sErrMsg = AppGlobal.g_oLang.get()._("split_check_total_invalid") + System.lineSeparator()
						+ AppGlobal.g_oLang.get()._("original_check_total_will_become_negative");
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(sErrMsg);
				oFormDialogBox.show();
				return ;
			} else if(dSplitAmountTotal.compareTo(m_oFuncCheck.getCheckTotal()) == 0) {
				String sErrMsg = AppGlobal.g_oLang.get()._("original_check_total_will_become_zero");
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(sErrMsg);
				oFormDialogBox.show();
				return ;
			}
			
			oTableInfo.put(TABLE_INFO_TABLE, m_sTargetTable);
			oTableInfo.put(TABLE_INFO_TABLE_EXT, m_sTargetTableExtension);
			oTableInfo.put(TABLE_INFO_SPLIT_AMOUNT, dSplitAmount);
			m_oFrameSplitCheckByOptions.updateTableInfo(0, iIndex - 1, 2, dSplitAmount.toPlainString());
			m_oTableInfoList.add(oTableInfo);
			
			boolean bAskTable = false;
			boolean bAskAmount = false;
			String sTableNo = "";
			String sTableExtension = "";
			
			if (m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_EQUAL_SPLIT) || m_sSplitCheckOption.equals(SPLIT_CHECK_OPTION_SEAT_NO)) {
				bAskTable = true;
			}else {
				if(!m_bAutoGenTable)
					bAskTable = true;
			}
			
			if(this.getTableInfoList().size() < m_iNumberOfTables - 1) {
				m_oFrameSplitCheckByOptions.clearTextboxValue();
				m_oFrameSplitCheckByOptions.setTextboxValue(this.generateDefaultSplitAmount());
				m_oFrameSplitCheckByOptions.setFocusTextboxValue();
				
				if(m_bAutoGenTable) {
					sTableNo = m_oFuncCheck.getTableNo();
					sTableExtension = m_oAvailableTableExtensionList.get(getTableInfoList().size());
				}
				
				// Call add table info again
				if(this.addTableInfo(getTableInfoList().size(), sTableNo, sTableExtension, bAskTable, bAskAmount, 0) == false)
					bResult = false;
			}else {
				if(!bResult)
					this.finishShow();
				else {
					FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
					oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
					oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("continue_to_split_check")+"?");
					oFormConfirmBox.show();
					if(oFormConfirmBox.isOKClicked())
						m_bConfirmSplit = true;
					
					this.finishShow();
				}
			}
		} else {
			if (iIndex >= m_iNumberOfTables - 1)
				return;
			
			if(m_bAutoGenTable) {
				if(this.addTableInfo(iIndex, m_oFuncCheck.getTableNo(), m_oAvailableTableExtensionList.get(iIndex), false, false, 0) == false) {
					bResult = false;
				}
			} else {
				if(this.addTableInfo(iIndex, "", "", true, false, 0) == false) {
					bResult = false;
				}
			}
			
			m_oFrameSplitCheckByOptions.setTextboxValue(this.generateDefaultSplitAmount());
			
			if (bResult){
				if (iIndex >= m_iNumberOfTables - 2){
					FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
					oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
					oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("continue_to_split_check")+"?");
					oFormConfirmBox.show();
					if(oFormConfirmBox.isOKClicked())
						m_bConfirmSplit = true;
					
					this.finishShow();
				}
				
				m_oFrameSplitCheckByOptions.setVisible(false);
				m_oFrameSplitCheckByOptions.setVisible(true);
				m_oFrameSplitCheckByOptions.setFocusTextboxValue();
				
			}
		}
	}
	
	@Override
	public void FrameSplitCheckByOptions_clickExit() {
		FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this);
		oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
		oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("abort_split_check")+"?");
		oFormConfirmBox.show();
		
		if(oFormConfirmBox.isOKClicked())
			this.finishShow();
		
		m_oFrameSplitCheckByOptions.clearTextboxValue();
		m_oFrameSplitCheckByOptions.setVisible(false);
		m_oFrameSplitCheckByOptions.setVisible(true);
		m_oFrameSplitCheckByOptions.setTextboxValue(this.generateDefaultSplitAmount());
		m_oFrameSplitCheckByOptions.setFocusTextboxValue();
	}

	@Override
	public void FrameSplitCheckByOptions_askTableInfo() {
		this.addSplitTableInfo();
	}
}
