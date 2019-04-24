package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import om.InfInterface;
import om.OutFloorPlan;
import om.OutFloorPlanMap;
import om.OutFloorPlanTable;
import om.PosCheck;
import om.PosInterfaceConfig;
import om.PosOutletTable;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIHorizontalList;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;
import virtualui.VirtualUITextbox;
import commonui.FormDialogBox;
import commonui.FrameNumberPad;
import commonui.FrameNumberPadListener;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;

/** interface for the listeners/observers callback method */
interface FrameOpenTableListener {
	boolean FrameOpenTable_openTable(String sTableNo, String sTableExtension);
	void FrameOpenTable_showTableFunction(String sTableNo, String sTableExtension);
	void FrameOpenTable_switchKeyboard();
	void FrameOpenTable_logout();
	void FrameOpenTable_close();
	void frameOpenTable_FunctionBarButtonClicked(String sNote);
	void frameOpenTable_FunctionBarOpenPanelClicked();
}

public class FrameOpenTable extends VirtualUIFrame implements FrameNumberPadListener, FrameOpenedCheckListListener, FrameTitleHeaderListener, FrameTableFloorPlanFunctionBarListener {
	TemplateBuilder m_oTemplateBuilder;
	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUIFrame m_oContent;
	private VirtualUIFrame m_oFrameCover;
	private VirtualUIFrame m_oFrameTableExtensionCell;
	private VirtualUIButton m_oButtonOK;
	private VirtualUIButton m_oButtonCancel;
	private VirtualUILabel m_oLabelTable;
	private VirtualUITextbox m_otxtboxTable;
	private VirtualUILabel m_oLabelExtension;
	private VirtualUIButton m_oButtonOpenedChk;
	private VirtualUIButton m_oButtonLogout;
	private VirtualUIHorizontalList m_oHorListTableExt;
	private VirtualUIFrame m_oFrameListTableExtension;
	private VirtualUILabel m_oLabelTableExtensionHeader;
	private VirtualUIList m_oListTableExtension;
	private FrameNumberPad m_oFrameNumberPad;
	private FrameOpenedCheckList m_oFrameOpenedCheckList;
	private FrameTableFloorPlanFunctionBar m_oFunctionBar;
	private VirtualUIButton m_oButtonSwitchKeyboard;

	private HashMap<String, FrameTableButton> m_oButtonTableExtension;

	private String m_sTableExtension;
	private boolean m_bFunctionBarButtonClick;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameOpenTableListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameOpenTableListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameOpenTableListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	// constructor
	public FrameOpenTable(VirtualUIFrame oFrameCover, ArrayList<FuncLookupButtonInfo> oFloorPlanFunctionLookupButtonInfos, int iTotalPageNum, String sOpenCheckInfoListType) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameOpenTableListener>();
		// Init extension list
		m_oButtonTableExtension = new HashMap<String, FrameTableButton>();
		
		// Set cover frame
		m_oFrameCover = oFrameCover;
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraOpenTable.xml");

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(true);
		m_oFrameTitleHeader.addListener(this);
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("search_table", ""));
		this.attachChild(m_oFrameTitleHeader);
		
		m_oContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oContent, "fraContent");
		this.attachChild(m_oContent);

		// Table label
		m_oLabelTable = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTable, "lblTableNoHeader");
		m_oLabelTable.setValue(AppGlobal.g_oLang.get()._("table", ""));
		m_oContent.attachChild(m_oLabelTable);

		// Table textbox
		m_otxtboxTable = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_otxtboxTable, "txtTableNo");
		m_otxtboxTable.setClickHideKeyboard(true);
		m_oContent.attachChild(m_otxtboxTable);

		// Extension label
		m_oLabelExtension = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelExtension, "lblExtension");
		m_oContent.attachChild(m_oLabelExtension);
		m_oLabelExtension.setValue("Q");

		// Opened check list
		m_oButtonOpenedChk = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOpenedChk, "btnChkList");
		m_oButtonOpenedChk.setValue(AppGlobal.g_oLang.get()._("opened_check", ""));
		m_oContent.attachChild(m_oButtonOpenedChk);

		// Logout
		m_oButtonLogout = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonLogout, "btnLogout");
		m_oButtonLogout.setValue(AppGlobal.g_oLang.get()._("logout", ""));
		m_oContent.attachChild(m_oButtonLogout);

		// SetMenu lookup list Horizontal List
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oHorListTableExt = new VirtualUIHorizontalList();
			m_oTemplateBuilder.buildHorizontalList(m_oHorListTableExt, "horListTab");
			m_oContent.attachChild(m_oHorListTableExt);

		} else {
			m_oFrameListTableExtension = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(m_oFrameListTableExtension, "fraListTableExtension");
			m_oContent.attachChild(m_oFrameListTableExtension);
			
			// Table Extension Header
			m_oLabelTableExtensionHeader = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelTableExtensionHeader, "lblTableExtensionHeader");
			m_oLabelTableExtensionHeader.setValue(AppGlobal.g_oLang.get()._("table_extension", ""));
			m_oFrameListTableExtension.attachChild(m_oLabelTableExtensionHeader);
			
			// Table Extension List
			m_oListTableExtension = new VirtualUIList();
			m_oTemplateBuilder.buildList(m_oListTableExtension, "listTableExtension");
			m_oFrameListTableExtension.attachChild(m_oListTableExtension);
			
			// Table Extension Cell
			m_oFrameTableExtensionCell = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(m_oFrameTableExtensionCell, "fraTableExtensionCell");
		}
		
		// Create OK Button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "btnOK");
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("ok", ""));
		m_oButtonOK.addClickServerRequestSubmitElement(m_otxtboxTable);
		m_oContent.attachChild(m_oButtonOK);
		
		// Create Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "btnCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel", ""));
		m_oButtonCancel.setClickServerRequestBlockUI(false);
		m_oContent.attachChild(m_oButtonCancel);
		
		// Add table extension
		addTableExtension("-", PosOutletTable.STATUS_NEW_TABLE, true, false);
		// 	Create extension list
		for(char alphabet = AppGlobal.TABLE_EXTENSION_START_LETTER; alphabet <= AppGlobal.TABLE_EXTENSION_END_LETTER; alphabet++){
			addTableExtension(String.valueOf(alphabet), PosOutletTable.STATUS_NEW_TABLE, false, false);
		}
		
		m_oButtonSwitchKeyboard = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSwitchKeyboard, "btnSwitchKeyboard");
		m_oButtonSwitchKeyboard.setValue(AppGlobal.g_oLang.get()._("alphanumeric_table_number", ""));
		m_oContent.attachChild(m_oButtonSwitchKeyboard);
		
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()) && oFloorPlanFunctionLookupButtonInfos != null) {
			// Initial the floor plan function bar
			m_oFunctionBar = new FrameTableFloorPlanFunctionBar();
			m_oTemplateBuilder.buildFrame(m_oFunctionBar, "fraFunctionBar");
			m_oFunctionBar.init(oFloorPlanFunctionLookupButtonInfos, iTotalPageNum);
			m_oFunctionBar.addListener(this);
			this.attachChild(m_oFunctionBar);
			
			m_oButtonOK.setTop(m_oButtonOK.getTop() - m_oFunctionBar.getHeight());
			m_oButtonCancel.setTop(m_oButtonCancel.getTop() - m_oFunctionBar.getHeight());
			m_oButtonSwitchKeyboard.setTop(m_oButtonSwitchKeyboard.getTop() - (int)(m_oFunctionBar.getHeight() * 0.8));
		}
		
		// Numpad
		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.setWithCancelAndEnterButton(false);
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) 
			m_oFrameNumberPad.setCustomHeight(m_oFrameNumberPad.getHeight());
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()) && oFloorPlanFunctionLookupButtonInfos != null)
			m_oFrameNumberPad.setHeight(m_oFrameNumberPad.getHeight() - m_oFunctionBar.getHeight());
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.addListener(this);
		m_oFrameNumberPad.setEnterSubmitId(m_otxtboxTable);
		m_oFrameNumberPad.setEnterDesc(AppGlobal.g_oLang.get()._("enter", ""));
		m_oFrameNumberPad.setEnterBlockUI(true);
		m_oContent.attachChild(m_oFrameNumberPad);
		
		//Opened check list
		m_oFrameOpenedCheckList = new FrameOpenedCheckList(sOpenCheckInfoListType);
		
		m_oTemplateBuilder.buildFrame(m_oFrameOpenedCheckList, "fraOpenedCheckList");
		m_oFrameOpenedCheckList.addListener(this);
		m_oFrameOpenedCheckList.setVisible(false);
		this.attachChild(m_oFrameOpenedCheckList);
		m_bFunctionBarButtonClick = false;
	}

	public void resetFrame() {
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oFrameCover.setVisible(true);
			m_oFrameCover.bringToTop();
		}
		this.bringToTop();
	}

	public void addTableExtension(String alphabet, String sStatus, boolean bSelect, boolean bAllowLongClick) {
		FrameTableButton oFrameTableButton = new FrameTableButton();
		oFrameTableButton.setExist(true);

		if(m_oButtonTableExtension.isEmpty())
			oFrameTableButton.setLeft(0);
		else {
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
				oFrameTableButton.setLeft(5);
			else
				oFrameTableButton.setTop(8);
		}

		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			oFrameTableButton.setWidth(m_oHorListTableExt.getHeight());
			oFrameTableButton.setHeight(m_oHorListTableExt.getHeight());
		} else {
			oFrameTableButton.setWidth(m_oFrameTableExtensionCell.getWidth());
			oFrameTableButton.setHeight(m_oFrameTableExtensionCell.getHeight());
			oFrameTableButton.setLeft(m_oFrameTableExtensionCell.getLeft());
			oFrameTableButton.setShadowTop(3);
			oFrameTableButton.setLeft(0);
			oFrameTableButton.setShadowRadius(5);
			oFrameTableButton.setBackgroundColor("#FFFFFF");
			oFrameTableButton.setShadowColor("#17000000");
		}

		oFrameTableButton.setupTableButton(StringLib.createStringArray(5, alphabet), OutFloorPlanTable.SHAPE_RHOMBUS, 0, "");
		oFrameTableButton.setCornerRadius("2");
		oFrameTableButton.allowClick(true);
		if(bAllowLongClick){
			oFrameTableButton.allowLongClick(true);
			oFrameTableButton.setLongClickServerRequestBlockUI(false);
		}
		VirtualUIFrame oFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrame, "fraNewTable");
		
		oFrameTableButton.setLabelForegroundColor(oFrame.getForegroundColor());
		oFrameTableButton.setTableColor(oFrame.getBackgroundColor(), PosOutletTable.STATUS_NEW_TABLE, AppGlobal.g_oLang.get()._("vacant", ""));
		oFrameTableButton.setPrinted(false);
		oFrameTableButton.setLocked(false);
		oFrameTableButton.setViewSeq(m_oButtonTableExtension.size() + 1);

		m_oButtonTableExtension.put(alphabet, oFrameTableButton);
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oHorListTableExt.attachChild(oFrameTableButton);
		else
			m_oListTableExtension.attachChild(oFrameTableButton);

		if(bSelect){
			selectTableExtensionButton(alphabet);
		}
	}

	public void selectTableExtensionButton(String sSelectTableExtension) {
		if(m_oButtonTableExtension.containsKey(m_sTableExtension)) {
			// Un-select previous selected button
			FrameTableButton oFrameTableButton = m_oButtonTableExtension.get(m_sTableExtension);
			oFrameTableButton.setStroke(0);
		}

		m_sTableExtension = sSelectTableExtension;

		if(m_oButtonTableExtension.containsKey(sSelectTableExtension)) {
			// Select the target button
			FrameTableButton oFrameTableButton = m_oButtonTableExtension.get(m_sTableExtension);
			oFrameTableButton.setStroke(1);
			oFrameTableButton.setStrokeColor("#FF0000");
		}

		if(m_sTableExtension.equals("-"))
			m_oLabelExtension.setValue("");
		else
			m_oLabelExtension.setValue(m_sTableExtension);
	}

	public void resetTableNo() {
		m_otxtboxTable.setValue("");
		m_oLabelExtension.setValue("");
	}

	public void setFocusOnTxtBox() {
		m_otxtboxTable.setFocus();
		m_otxtboxTable.setFocusWhenShow(true);
	}

	public void setTableTextBoxFocus() {
		m_otxtboxTable.setFocusWhenShow(true);
	}

	public boolean isFunctionBarButtonClicked() {
		return m_bFunctionBarButtonClick;
	}
	
	public void updateCheckListUIbyOperationMode(String sOpenCheckInfoListType){
		if(m_oFrameOpenedCheckList != null)
			m_oFrameOpenedCheckList.removeMyself();
		
		m_oFrameOpenedCheckList = new FrameOpenedCheckList(sOpenCheckInfoListType);
		m_oTemplateBuilder.buildFrame(m_oFrameOpenedCheckList, "fraOpenedCheckList");
		m_oFrameOpenedCheckList.addListener(this);
		m_oFrameOpenedCheckList.setVisible(false);
		this.attachChild(m_oFrameOpenedCheckList);
	}
	
	public void setCloseButtonVisible(boolean bShow) {
		m_oFrameTitleHeader.setButtonShow(bShow);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if(iChildId == m_oButtonOpenedChk.getId()) {
			resetFrame();
			PosCheck oPosCheck = new PosCheck();
			JSONArray oCheckListJSONArray = oPosCheck.getCheckListByBusinessDayPaid(
					AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(),
					AppGlobal.g_oFuncStation.get().getStationId(), PosCheck.PAID_NOT_PAID, false);
			m_oFrameOpenedCheckList.initCheckList(oCheckListJSONArray);
			m_oFrameOpenedCheckList.setVisible(true);
			m_oFrameOpenedCheckList.bringToTop();
			
			bMatchChild = true;
		} else if (iChildId == m_oButtonSwitchKeyboard.getId()) {
			this.setVisible(false);
			for (FrameOpenTableListener listener : listeners) {
				// Raise the event to parent
				listener.FrameOpenTable_switchKeyboard();
			}
			
			bMatchChild = true;
		} else if (iChildId == m_oButtonLogout.getId()) {
			for (FrameOpenTableListener listener : listeners) {
				// Raise the event to parent
				listener.FrameOpenTable_logout();
			}
			bMatchChild = true;
		} else if (iChildId == m_oButtonOK.getId()) {
			this.FrameNumberPad_clickEnter();
			bMatchChild = true;
		} else if (iChildId == m_oButtonCancel.getId()) {
			this.FrameNumberPad_clickCancel();
			bMatchChild = true;
		} else {
			for (Map.Entry<String, FrameTableButton> entry : m_oButtonTableExtension.entrySet()) {
				FrameTableButton oButton = entry.getValue();
				if (iChildId == oButton.getId()){
					selectTableExtensionButton(entry.getKey());
					bMatchChild = true; 
				}
			}
		}
		return bMatchChild;
	}

	@Override
	public void FrameOpenedCheckList_ButtonExitClicked() {
		m_oFrameOpenedCheckList.setVisible(false);
	}

	@Override
	public void FrameOpenedCheckList_RecordClicked(int iTable, String sTableExtension) {
		m_oFrameOpenedCheckList.setVisible(false);
		for (FrameOpenTableListener listener : listeners) {
			// Raise the event to parent
			listener.FrameOpenTable_openTable(String.valueOf(iTable), sTableExtension);
		}
	}

	@Override
	public void FrameNumberPad_clickEnter() {
		boolean bFindAtFloorPlan = false, bInvalidTable = false;
		BigDecimal dTableNumber = null;
		
		try {
			dTableNumber = new BigDecimal(m_otxtboxTable.getValue());
		}catch(Exception e) {
			bInvalidTable = true;
		}
		
		if(bInvalidTable || dTableNumber.compareTo(BigDecimal.ZERO) < 0) {
			String sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
			FormDialogBox oDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), super.getParentForm());
			oDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oDialogBox.setMessage(sErrMsg);
			oDialogBox.show();
			
			return;
		}
		
		if (dTableNumber.compareTo(BigDecimal.ZERO) == 0) {
			String sErrMsg = AppGlobal.g_oLang.get()._("cannot_input_zero_table_no");
			sErrMsg += (System.lineSeparator() + AppGlobal.g_oLang.get()._("please_use_alphanumeric_table"));
			FormDialogBox oDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), super.getParentForm());
			oDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oDialogBox.setMessage(sErrMsg);
			oDialogBox.show();
			
			return;
		}
		
		String sTableNo = m_otxtboxTable.getValue();
		String sTableExt = (m_sTableExtension.equals("-"))?"":m_sTableExtension;
		
		for(Entry<Integer, OutFloorPlan> entry:AppGlobal.g_oFuncOutlet.get().getFloorPlanList().entrySet()){
			// Build Floor Plan
			OutFloorPlan oOutFloorPlan = entry.getValue();
			if(oOutFloorPlan == null){
				continue;
			}
			if(oOutFloorPlan.getMapCount() > 0) {
				for(int i = 0; i<oOutFloorPlan.getMapCount(); i++){
					OutFloorPlanMap oOutFloorPlanMap = oOutFloorPlan.getMap(i);
					for(int j=0; j<oOutFloorPlanMap.getTableCount(); j++){
						OutFloorPlanTable oOutFloorPlanTable = oOutFloorPlanMap.getTable(j);
						if(oOutFloorPlanTable.getTable() == 0){
							if((sTableNo + sTableExt).equals(oOutFloorPlanTable.getTableExt())){
								sTableExt = sTableNo + sTableExt;
								sTableNo = "0";
								bFindAtFloorPlan = true;
								break;
							}	
						}
					}
				}
			}
		}
		
		for (FrameOpenTableListener listener : listeners) {
			// Raise the event to parent
			listener.FrameOpenTable_openTable(sTableNo, sTableExt);
		}
	}

	@Override
	public void FrameNumberPad_clickCancel() {
		this.resetTableNo();
	}

	@Override
	public void FrameNumberPad_clickNumber(String string) {
	}

	@Override
	public void FrameOpenedCheckList_RecordLongClicked(int iTable, String sTableExtension) {
		m_oFrameOpenedCheckList.setVisible(false);
		for(FrameOpenTableListener listener : listeners) {
			// Raise the event to parent
			listener.FrameOpenTable_showTableFunction(String.valueOf(iTable), sTableExtension);
		}
	}

	public void setTableNoAndExtension(String sTableNo, String sTableExtension) {
		m_otxtboxTable.setValue(sTableNo);
		m_sTableExtension = sTableExtension;
		m_oLabelExtension.setValue(sTableExtension);
	}

	@Override
	public boolean valueChanged(int iChildId, String sNote) {
		boolean bMatchChild = false;
		String sCardNo = "";
		String sTableExtension = "";
		ClsActiveClient oActiveClient = AppGlobal.getActiveClient();
		if (oActiveClient != null && oActiveClient.getSwipeCardReaderElement().getValueChangedServerRequestNote()
				.equals(FuncMSR.FRAME_SWIPE_CARD_OPENTABLE)) {
			if (iChildId == oActiveClient.getSwipeCardReaderElement().getId()) {
				if (oActiveClient.getSwipeCardReaderElement().getValue().length() > 0) {
					sCardNo = oActiveClient.getSwipeCardReaderElement().getValue().replace("\r", "").replace("\n", "");
				}
				bMatchChild = true;
			}
			InfInterface oMsrPosInterfaceConfig = null;
			if (!AppGlobal.g_oFuncStation.get().getTableValidationMsrCode().isEmpty()) {
				if (FormMain.oExtraPosInterfaceConfig.containsKey(AppGlobal.g_oFuncStation.get().getTableValidationMsrCode()))
					oMsrPosInterfaceConfig = FormMain.oExtraPosInterfaceConfig.get(AppGlobal.g_oFuncStation.get().getTableValidationMsrCode());
				else {
					PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
					JSONArray oJSONArray = oPosInterfaceConfig.getInterfaceConfigsByInterfaceCode(
							AppGlobal.g_oFuncStation.get().getTableValidationMsrCode());
					if (oJSONArray != null) {
						for (int i = 0; i < oJSONArray.length(); i++) {
							try {
								if (oJSONArray.get(i) != null) {
									oMsrPosInterfaceConfig = new InfInterface(new JSONObject(oJSONArray.get(i).toString()));
									FormMain.oExtraPosInterfaceConfig.put(oMsrPosInterfaceConfig.getInterfaceCode(), oMsrPosInterfaceConfig);
									break;
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								AppGlobal.stack2Log(e);
							}
						}
					} else {
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("msr_setup_not_found"));
						oFormDialogBox.show();
						oFormDialogBox = null;
					}
				}
				if (oMsrPosInterfaceConfig != null) {
					// Capture information from swipe card
					FuncMSR oFuncMSR = new FuncMSR();
					int iErrorCode = oFuncMSR.processCardContent(sCardNo, oMsrPosInterfaceConfig.getSetting());
					
					// Get the necessary value
					sCardNo = oFuncMSR.getCardNo();
					if (iErrorCode == FuncMSR.ERROR_CODE_MISSING_SETUP) {
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(oFuncMSR.getLastErrorMessage());
						oFormDialogBox.show();
						oFormDialogBox = null;
						
						return bMatchChild;
					}
					if (sCardNo.isEmpty()) {
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_capture_card"));
						oFormDialogBox.show();
						oFormDialogBox = null;
						return bMatchChild;
					} else {
						HashMap<String, String> oTableInfo = AppGlobal.g_oFuncStation.get().getTableNoAndTableExtension(sCardNo);
						if (oTableInfo.containsKey("tableNo"))
							sCardNo = oTableInfo.get("tableNo");
						if (oTableInfo.containsKey("tableExt"))
							sTableExtension = oTableInfo.get("tableExt");
						try {
							int iTableNo = Integer.parseInt(sCardNo.trim());
						} catch (NumberFormatException e) {
							String sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
							FormDialogBox oDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
							oDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
							oDialogBox.setMessage(sErrMsg);
							oDialogBox.show();
							
							return bMatchChild;
						}
						m_otxtboxTable.setValue(sCardNo);
						selectTableExtensionButton(sTableExtension);
						FrameNumberPad_clickEnter();
					}
				}
			}
		}
		return bMatchChild;
	}

	@Override
	public void FrameTitleHeader_close() {
		this.setVisible(false);
		
		for (FrameOpenTableListener listener : listeners) {
			// Raise the event to parent
			listener.FrameOpenTable_close();
		}
	}
	
	@Override
	public void frameFloorPlanFunctionBar_openPanelImageClicked() {
		for (FrameOpenTableListener listener: listeners) {
			listener.frameOpenTable_FunctionBarOpenPanelClicked();
		}
	}

	@Override
	public void frameFloorPlanFunctionBar_buttonClicked(String sNote) {
		m_bFunctionBarButtonClick = true;
		for (FrameOpenTableListener listener: listeners) {
			listener.frameOpenTable_FunctionBarButtonClicked(sNote);
		}
		m_bFunctionBarButtonClick = false;
	}
}
