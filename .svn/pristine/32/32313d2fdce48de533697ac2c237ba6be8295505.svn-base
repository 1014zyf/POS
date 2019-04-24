package app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import om.PosCheck;
import om.PosCheckExtraInfo;
import om.PosOutletTable;

import org.json.JSONArray;
import commonui.FormDialogBox;
import commonui.FormInputBox;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;

import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIButton;


interface FrameOpenedCheckListListener {
	void FrameOpenedCheckList_ButtonExitClicked();
	void FrameOpenedCheckList_RecordClicked(int iTable, String sTableExtension);
	void FrameOpenedCheckList_RecordLongClicked(int iTable, String sTableExtension);
}

public class FrameOpenedCheckList extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener{
	class OutletTable{
		PosCheck oPosCheck;
		PosOutletTable oPosOutletTable;
		
		OutletTable(PosCheck oPosCheck, PosOutletTable oPosOutletTable) {
			this.oPosCheck = oPosCheck;
			this.oPosOutletTable = oPosOutletTable;
		}
	}
	
	private TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUIButton m_oButtonShowAll;
	private VirtualUIButton m_oButtonSearchTableRef;
	private FrameCommonBasket m_oCheckListCommonBasket;
	
	private List<Integer> m_oFieldsWidth;
	private HashMap<Integer, PosCheck> m_oCheckList;
	private HashMap<Integer, PosOutletTable> m_oOutletTableList;
	
	private String m_sOpenCheckInfoListType;
	public static final String TYPE_OPEN_CHECK_LIST_CHECK_STATUS = "check_status";
	public static final String TYPE_OPEN_CHECK_LIST_TABLE_REFERENCE = "table_reference";
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameOpenedCheckListListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameOpenedCheckListListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameOpenedCheckListListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FrameOpenedCheckList(String sOpenCheckInfoListType) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameOpenedCheckListListener>();
		
		m_oCheckList = new HashMap<Integer, PosCheck>();
		m_oOutletTableList = new HashMap<Integer, PosOutletTable>();
		m_oFieldsWidth = new ArrayList<Integer>();
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oFieldsWidth.add(120);
			m_oFieldsWidth.add(80);
			m_oFieldsWidth.add(100);
			m_oFieldsWidth.add(100);
			m_oFieldsWidth.add(58);
		}else {
			m_oFieldsWidth.add(144);
			m_oFieldsWidth.add(200);
			m_oFieldsWidth.add(200);
			m_oFieldsWidth.add(250);
			m_oFieldsWidth.add(200);
		}
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraOpenedCheckList.xml");

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(true);
		m_oFrameTitleHeader.addListener(this);
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("opened_check", ""));
		this.attachChild(m_oFrameTitleHeader);
		
		// Search Table reference
		m_oButtonSearchTableRef = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSearchTableRef, "btnTableRef");
		m_oButtonSearchTableRef.setValue(AppGlobal.g_oLang.get()._("search_by_table_reference_for_opened_check", ""));
		m_oButtonSearchTableRef.addClickServerRequestSubmitElement(this);
		this.attachChild(m_oButtonSearchTableRef);
		
		// Show All
		m_oButtonShowAll = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonShowAll, "btnShowAll");
		m_oButtonShowAll.setValue(AppGlobal.g_oLang.get()._("show_all", ""));
		m_oButtonShowAll.addClickServerRequestSubmitElement(this);
		this.attachChild(m_oButtonShowAll);
			
		this.m_sOpenCheckInfoListType = sOpenCheckInfoListType;
		
		//Check list common basket
		m_oCheckListCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oCheckListCommonBasket, "fraCheckListCommonBasket");
		m_oCheckListCommonBasket.init();
		m_oCheckListCommonBasket.addListener(this);
		this.attachChild(m_oCheckListCommonBasket);
		
		//Add the check list title
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String[]> sFieldValues = new ArrayList<String[]>();
		iFieldWidths.add(m_oFieldsWidth.get(0));
		sFieldValues.add(AppGlobal.g_oLang.get()._("table_no", ""));
		iFieldWidths.add(m_oFieldsWidth.get(1));
		sFieldValues.add(AppGlobal.g_oLang.get()._("check_no", ""));
		iFieldWidths.add(m_oFieldsWidth.get(2));
		sFieldValues.add(AppGlobal.g_oLang.get()._("open_time", ""));
		iFieldWidths.add(m_oFieldsWidth.get(3));
		sFieldValues.add(AppGlobal.g_oLang.get()._("check_total", ""));
		iFieldWidths.add(m_oFieldsWidth.get(4));
		
		if(this.m_sOpenCheckInfoListType.equals(FrameOpenedCheckList.TYPE_OPEN_CHECK_LIST_TABLE_REFERENCE))
			sFieldValues.add(AppGlobal.g_oLang.get()._("table_reference", ""));
		else
			sFieldValues.add(AppGlobal.g_oLang.get()._("status", ""));
		
		sFieldValues.add(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""));
		m_oCheckListCommonBasket.addHeader(iFieldWidths, sFieldValues);
		m_oCheckListCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		m_oCheckListCommonBasket.setHeaderFormat(55, 24, "");
		
		if(this.m_sOpenCheckInfoListType.equals(FrameOpenedCheckList.TYPE_OPEN_CHECK_LIST_CHECK_STATUS)) {
			m_oButtonShowAll.setVisible(false);
			m_oButtonSearchTableRef.setVisible(false);
			m_oCheckListCommonBasket.setTop(65);
			m_oCheckListCommonBasket.setHeight(552);
		}
		
		this.updateButtonColor(true);
	}
	
	public void initCheckList(JSONArray oCheckJSONArray) {
		int i;
		PosCheck oCheck;
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		
		m_oCheckListCommonBasket.removeAllItems(0);
		
		if(oCheckJSONArray == null)
			return;
		
		if(oCheckJSONArray.length() == 0)
			return;
		
		for (i = 0; i<oCheckJSONArray.length(); i++) {
			if (oCheckJSONArray.isNull(i))
				continue;
			oCheck = new PosCheck(oCheckJSONArray.optJSONObject(i));
			//String sTable = Integer.toString(oCheck.getTable());
			m_oCheckList.put(i, oCheck);
			
			ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
			ArrayList<String> sFieldValues = new ArrayList<String>();
			ArrayList<String> sFieldAligns = new ArrayList<String>();
			ArrayList<String> sFieldTypes = new ArrayList<String>();
			
//	if(oCheck.getTableExtension() != null && !oCheck.getTableExtension().isEmpty())		//Have Table Extension
//		sTable += oCheck.getTableExtension();
			
			iFieldWidths.add(m_oFieldsWidth.get(0));
			String sTable = AppGlobal.g_oFuncOutlet.get().getTableNameWithTableNo(Integer.toString(oCheck.getTable()), oCheck.getTableExtension())[AppGlobal.g_oCurrentLangIndex.get()-1];
			if(this.m_sOpenCheckInfoListType.equals(FrameOpenedCheckList.TYPE_OPEN_CHECK_LIST_TABLE_REFERENCE) && oCheck.getTableExtension().length() > 0 && oCheck.getTableExtension().substring(oCheck.getTableExtension().length() - 1).compareTo(AppGlobal.BAR_TAB_TABLE_EXTENSION) == 0)
				sTable = AppGlobal.g_oLang.get()._("auto");
			sFieldValues.add(sTable);
			sFieldAligns.add("");
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			iFieldWidths.add(m_oFieldsWidth.get(1));
			sFieldValues.add(oCheck.getCheckPrefixNo());
			sFieldAligns.add("");
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			iFieldWidths.add(m_oFieldsWidth.get(2));
			sFieldValues.add(timeFormat.format(oCheck.getOpenLocTime().toDate()));
			sFieldAligns.add("");
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			iFieldWidths.add(m_oFieldsWidth.get(3));
			sFieldValues.add(StringLib.BigDecimalToString(oCheck.getCheckTotal(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getCheckDecimal()));
			
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.RIGHT + "," + HeroActionProtocol.View.Attribute.ImageAlign.CENTER_VERTICAL);
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			iFieldWidths.add(m_oFieldsWidth.get(4));
			
			String sTableReferenceInfo = "";
			
			if(this.m_sOpenCheckInfoListType.equals(FrameOpenedCheckList.TYPE_OPEN_CHECK_LIST_TABLE_REFERENCE)) {
				List<PosCheckExtraInfo> checkExtraInfoArrayList = oCheck.getCheckExtraInfoArrayList();
				for (PosCheckExtraInfo checkExtraInfo: checkExtraInfoArrayList) {
					if (checkExtraInfo.getBy().equals(PosCheckExtraInfo.BY_CHECK)
						&& checkExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_TABLE_INFORMATION)
						&& checkExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_TABLE_REFERENCE)) {
						if (checkExtraInfo.getValue() != null) {
							sTableReferenceInfo = checkExtraInfo.getValue();
							break;
						}
					}
				}
				
				sFieldValues.add(sTableReferenceInfo);
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);

			} else {
				if(oCheck.getLockStationId() > 0 && oCheck.getPrintCount() == 0)
					sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_lock.png");
				else if(oCheck.getLockStationId() == 0 && oCheck.getPrintCount() > 0)
					sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_printer.png");
				else if(oCheck.getLockStationId() > 0 && oCheck.getPrintCount() > 0)
					sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_printer.png");
				else
					sFieldValues.add("");
				
				sFieldAligns.add(HeroActionProtocol.View.Attribute.ImageAlign.CENTER_VERTICAL);
				
				sFieldTypes.add(HeroActionProtocol.View.Type.IMAGE);
			}
			
			m_oCheckListCommonBasket.addItem(0, i, 48, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
			
		}
	}
	
	public void initCheckListForSearchTableRef(JSONArray oCheckJSONArray, String sReference) {
		int i;
		int iResultRowIndex = 0;
		PosCheck oCheck;
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		
		m_oCheckListCommonBasket.removeAllItems(0);
		
		if(oCheckJSONArray == null)
			return;
		
		if(oCheckJSONArray.length() == 0)
			return;
		
		for (i = 0; i<oCheckJSONArray.length(); i++) {
			if (oCheckJSONArray.isNull(i))
				continue;
			
			oCheck = new PosCheck(oCheckJSONArray.optJSONObject(i));
			
			boolean bTableRef = false;
			String sTableReferenceInfo = "";
			
			List<PosCheckExtraInfo> checkExtraInfoArrayList = oCheck.getCheckExtraInfoArrayList();
			for (PosCheckExtraInfo checkExtraInfo: checkExtraInfoArrayList) {
				if (checkExtraInfo.getBy().equals(PosCheckExtraInfo.BY_CHECK)
					&& checkExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_TABLE_INFORMATION)
					&& checkExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_TABLE_REFERENCE)) {
					if (checkExtraInfo.getValue() != null && checkExtraInfo.getValue() != "" && checkExtraInfo.getValue().contains(sReference)) {
						bTableRef = true;
						sTableReferenceInfo = checkExtraInfo.getValue();
						break;
					}
				}
			}	
			
			if(!bTableRef)
				continue;
			
			m_oCheckList.put(iResultRowIndex, oCheck);
			
			ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
			ArrayList<String> sFieldValues = new ArrayList<String>();
			ArrayList<String> sFieldAligns = new ArrayList<String>();
			ArrayList<String> sFieldTypes = new ArrayList<String>();
			
			iFieldWidths.add(m_oFieldsWidth.get(0));
			String sTable = AppGlobal.g_oFuncOutlet.get().getTableNameWithTableNo(Integer.toString(oCheck.getTable()), oCheck.getTableExtension())[AppGlobal.g_oCurrentLangIndex.get()-1];
			if(this.m_sOpenCheckInfoListType.equals(FrameOpenedCheckList.TYPE_OPEN_CHECK_LIST_TABLE_REFERENCE) && oCheck.getTableExtension().length() > 0 && oCheck.getTableExtension().substring(oCheck.getTableExtension().length() - 1).compareTo(AppGlobal.BAR_TAB_TABLE_EXTENSION) == 0)
				sTable = AppGlobal.g_oLang.get()._("auto");
			sFieldValues.add(sTable);
			sFieldAligns.add("");
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			iFieldWidths.add(m_oFieldsWidth.get(1));
			sFieldValues.add(oCheck.getCheckPrefixNo());
			sFieldAligns.add("");
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			iFieldWidths.add(m_oFieldsWidth.get(2));
			sFieldValues.add(timeFormat.format(oCheck.getOpenLocTime().toDate()));
			sFieldAligns.add("");
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			iFieldWidths.add(m_oFieldsWidth.get(3));
			sFieldValues.add(StringLib.BigDecimalToString(oCheck.getCheckTotal(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getCheckDecimal()));
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.RIGHT + "," + HeroActionProtocol.View.Attribute.ImageAlign.CENTER_VERTICAL);
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);

			iFieldWidths.add(m_oFieldsWidth.get(4));
			sFieldValues.add(sTableReferenceInfo);
			sFieldAligns.add("");
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			m_oCheckListCommonBasket.addItem(0, iResultRowIndex, 48, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
			iResultRowIndex++;
		}
	}
	
	
	public void initCheckListForAttachTable(ArrayList<OutletTable> oOutletTable) {
		PosCheck oCheck;
		PosOutletTable oPosOutletTable;
		SimpleDateFormat oTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		
		m_oCheckListCommonBasket.removeAllItems(0);
		
		if(oOutletTable == null || oOutletTable.size() == 0)
			return;
		
		int iItemCount = 0;
		for (int i = 0; i<oOutletTable.size(); i++) {
			if (oOutletTable.get(i) == null)
				continue;
			
			String sCheckSeq = String.valueOf(i);
			
			oCheck = oOutletTable.get(i).oPosCheck;
			oPosOutletTable = oOutletTable.get(i).oPosOutletTable;
			m_oCheckList.put(i, oCheck);
			m_oOutletTableList.put(i, oPosOutletTable);
			
			ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
			ArrayList<String> sFieldValues = new ArrayList<String>();
			ArrayList<String> sFieldAligns = new ArrayList<String>();
			ArrayList<String> sFieldTypes = new ArrayList<String>();
			String sTableName = AppGlobal.g_oFuncOutlet.get().getTableNameWithTableNo(Integer.toString(oPosOutletTable.getTable()), oPosOutletTable.getTableExtension())[AppGlobal.g_oCurrentLangIndex.get()-1];
			
			iFieldWidths.add(m_oFieldsWidth.get(0));
			sFieldValues.add(sTableName);
			sFieldAligns.add("");
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			iFieldWidths.add(m_oFieldsWidth.get(1));
			sFieldValues.add(oCheck.getCheckPrefixNo());
			sFieldAligns.add("");
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			iFieldWidths.add(m_oFieldsWidth.get(2));
			sFieldValues.add(oTimeFormat.format(oCheck.getOpenLocTime().toDate()));
			sFieldAligns.add("");
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			iFieldWidths.add(m_oFieldsWidth.get(3));
			sFieldValues.add(StringLib.BigDecimalToString(oCheck.getCheckTotal(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getCheckDecimal()));
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.RIGHT + "," + HeroActionProtocol.View.Attribute.ImageAlign.CENTER_VERTICAL);
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			iFieldWidths.add(m_oFieldsWidth.get(4));
			if(oCheck.getLockStationId() > 0 && oCheck.getPrintCount() == 0)
				sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_lock.png");
			else if(oCheck.getLockStationId() == 0 && oCheck.getPrintCount() > 0)
				sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_printer.png");
			else if(oCheck.getLockStationId() > 0 && oCheck.getPrintCount() > 0)
				sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_printer.png");
			else
				sFieldValues.add("");
			
			sFieldAligns.add(HeroActionProtocol.View.Attribute.ImageAlign.CENTER_VERTICAL);
			sFieldTypes.add(HeroActionProtocol.View.Type.IMAGE);
			m_oCheckListCommonBasket.addItem(0, iItemCount, 48, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
			m_oCheckListCommonBasket.setFieldClickServerRequestNote(0, iItemCount, 0, sCheckSeq);
			m_oCheckListCommonBasket.setFieldClickServerRequestNote(0, iItemCount, 1, sCheckSeq);
			m_oCheckListCommonBasket.setFieldClickServerRequestNote(0, iItemCount, 2, sCheckSeq);
			m_oCheckListCommonBasket.setFieldClickServerRequestNote(0, iItemCount, 3, sCheckSeq);
			m_oCheckListCommonBasket.setFieldClickServerRequestNote(0, iItemCount, 4, sCheckSeq);
			iItemCount++;
			
			for(PosCheckExtraInfo oPosCheckExtraInfo : oCheck.getCheckExtraInfoArrayList()) {
				if(oPosCheckExtraInfo.getSection().equals(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE)
						&& oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER)) {
					if(oPosCheckExtraInfo.getValue() != null) {
						iFieldWidths = new ArrayList<Integer>();
						sFieldValues = new ArrayList<String>();
						sFieldAligns = new ArrayList<String>();
						sFieldTypes = new ArrayList<String>();
						
						iFieldWidths.add(m_oFieldsWidth.get(0) + m_oFieldsWidth.get(1) + m_oFieldsWidth.get(2) + m_oFieldsWidth.get(3) + m_oFieldsWidth.get(4));
						sFieldValues.add(oPosCheckExtraInfo.getValue());
						sFieldAligns.add("");
						sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
						
						m_oCheckListCommonBasket.addItem(0, iItemCount, 48, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
						m_oCheckListCommonBasket.setFieldClickServerRequestNote(0, iItemCount, 0, sCheckSeq);
						iItemCount++;
						break;
					}
				}
			}
		}
	}
	
	public OutletTable getOutletTable(PosCheck oPosCheck, PosOutletTable oPosOutletTable) {
		return new OutletTable(oPosCheck, oPosOutletTable);
	}
	
	public void updateButtonColor(boolean bShowAll) {
		String sUnselectedBackgroundColor = "#A0B3B7";
		String sSelectedBackgroundColor = "#0055B8";
		
		if(bShowAll) {
			m_oButtonShowAll.setBackgroundColor(sSelectedBackgroundColor);
			m_oButtonSearchTableRef.setBackgroundColor(sUnselectedBackgroundColor);
		} else {
			m_oButtonSearchTableRef.setBackgroundColor(sSelectedBackgroundColor);
			m_oButtonShowAll.setBackgroundColor(sUnselectedBackgroundColor);
		}
	}
	
	 @Override
	 public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if(iChildId == m_oButtonShowAll.getId()) {
			PosCheck oPosCheck = new PosCheck();
			JSONArray oCheckListJSONArray = oPosCheck.getCheckListByBusinessDayPaid(
					AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(),
					AppGlobal.g_oFuncStation.get().getStationId(), PosCheck.PAID_NOT_PAID, false);
			initCheckList(oCheckListJSONArray);
			
			updateButtonColor(true);
			bMatchChild = true;
		}
		else if(iChildId == m_oButtonSearchTableRef.getId()) {
			
			FormInputBox oFormInputBox = new FormInputBox(null);
			oFormInputBox.init();
			oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.DEFAULT);
			oFormInputBox.showKeyboard();
			oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("input_table_reference"));
			oFormInputBox.show();
			
			String sTableReference = "";
			if(oFormInputBox.getInputValue()!= null)
				sTableReference = oFormInputBox.getInputValue();	
			
			if(!oFormInputBox.isUserCancel()) {	
				
				if(sTableReference.isEmpty()) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), null);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("invalid_input"));
					oFormDialogBox.show();
				} else {
					PosCheck oPosCheck = new PosCheck();
					JSONArray oCheckListJSONArray = oPosCheck.getCheckListByBusinessDayPaid(
					AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(),
					AppGlobal.g_oFuncStation.get().getStationId(), PosCheck.PAID_NOT_PAID, false);
					initCheckListForSearchTableRef(oCheckListJSONArray, sTableReference);
				}
			}
			updateButtonColor(false);
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
	
	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
	}
	
	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		for(FrameOpenedCheckListListener listener: listeners) {
			if(m_oOutletTableList.size() > 0) {
				PosOutletTable oPosOutletTable = m_oOutletTableList.get(Integer.parseInt(sNote));
				listener.FrameOpenedCheckList_RecordClicked(oPosOutletTable.getTable(), oPosOutletTable.getTableExtension());
			}else {
				PosCheck oPosCheck = m_oCheckList.get(iItemIndex);
				listener.FrameOpenedCheckList_RecordClicked(oPosCheck.getTable(), oPosCheck.getTableExtension());
			}
		}
	}
	
	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}
	
	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId,
			int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		for(FrameOpenedCheckListListener listener: listeners) {
			if(m_oOutletTableList.size() > 0) {
				PosOutletTable oPosOutletTable = m_oOutletTableList.get(Integer.parseInt(sNote));
				listener.FrameOpenedCheckList_RecordClicked(oPosOutletTable.getTable(), oPosOutletTable.getTableExtension());
			}else {
				PosCheck oPosCheck = m_oCheckList.get(iItemIndex);
				listener.FrameOpenedCheckList_RecordLongClicked(oPosCheck.getTable(), oPosCheck.getTableExtension());
			}
		}
	}
	
	@Override
	public void FrameTitleHeader_close() {
		for(FrameOpenedCheckListListener listener: listeners) {
			listener.FrameOpenedCheckList_ButtonExitClicked();
		}
	}
}
