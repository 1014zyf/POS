package app;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;
import commonui.FormDialogBox;
import commonui.FrameQwertyKeyboard;
import commonui.FrameQwertyKeyboardListener;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;

/** interface for the listeners/observers callback method */
interface FrameOpenTableWithQwertyKeyboardListener {
	void FrameOpenTableWithQwertyKeyboard_openTable(String sTableNo, String sTableExtension);
	void FrameOpenTableWithQwertyKeyboard_showTableFunction(String sTableNo, String sTableExtension);
	void FrameOpenTableWithQwertyKeyboard_switchKeyboard();
	void FrameOpenTableWithQwertyKeyboard_logout();
	void FrameOpenTableWithQwertyKeyboard_close();
	void frameOpenTableWithQwertyKeyboard_FunctionBarButtonClicked(String sNote);
	void frameOpenTableWithQwertyKeyboard_FunctionBarOpenPanelClicked();
}

public class FrameOpenTableWithQwertyKeyboard extends VirtualUIFrame
		implements FrameQwertyKeyboardListener, FrameOpenedCheckListListener, FrameTitleHeaderListener, FrameTableFloorPlanFunctionBarListener {

	TemplateBuilder m_oTemplateBuilder;
	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUIFrame m_oContent;
	private VirtualUIFrame m_oFrameCover;
	private VirtualUIButton m_oButtonOK;
	private VirtualUIButton m_oButtonCancel;
	private VirtualUILabel m_oLabelTable;
	private VirtualUITextbox m_otxtboxTable;
	private VirtualUIButton m_oButtonOpenedChk;
	private VirtualUIButton m_oButtonLogout;
	private VirtualUIFrame m_oFrameQwertyBackspace;
	private VirtualUIImage m_oImageQwertyBackspace;
	private FrameQwertyKeyboard m_oFrameQwertyKeyboard;
	private FrameOpenedCheckList m_oFrameOpenedCheckList;
	private VirtualUIButton m_oButtonSwitchKeyBoard;
	private FrameTableFloorPlanFunctionBar m_oFunctionBar;
	private boolean m_bFunctionBarButtonClick;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameOpenTableWithQwertyKeyboardListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameOpenTableWithQwertyKeyboardListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameOpenTableWithQwertyKeyboardListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	// constructor
	public FrameOpenTableWithQwertyKeyboard(VirtualUIFrame oFrameCover, ArrayList<FuncLookupButtonInfo> oFloorPlanFunctionLookupButtonInfos, int iTotalPageNum, String sOpenCheckInfoListType) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameOpenTableWithQwertyKeyboardListener>();

		// Set cover frame
		m_oFrameCover = oFrameCover;

		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraOpenTableWithQwertyKeyboard.xml");

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

		// QwertyKeyboard
		m_oFrameQwertyKeyboard = new FrameQwertyKeyboard();
		m_oTemplateBuilder.buildFrame(m_oFrameQwertyKeyboard, "fraQwertyKeyboard");
		m_oFrameQwertyKeyboard.init();
		m_oFrameQwertyKeyboard.addListener(this);
		m_oFrameQwertyKeyboard.setEnterSubmitId(m_otxtboxTable);
		m_oFrameQwertyKeyboard.setEnterDesc(AppGlobal.g_oLang.get()._("enter", ""));
		m_oFrameQwertyKeyboard.setEnterBlockUI(true);
		m_oContent.attachChild(m_oFrameQwertyKeyboard);

		// Opened check list
		m_oFrameOpenedCheckList = new FrameOpenedCheckList(sOpenCheckInfoListType);
		m_oTemplateBuilder.buildFrame(m_oFrameOpenedCheckList, "fraOpenedCheckList");
		m_oFrameOpenedCheckList.addListener(this);
		m_oFrameOpenedCheckList.setVisible(false);
		this.attachChild(m_oFrameOpenedCheckList);

		m_oButtonSwitchKeyBoard = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSwitchKeyBoard, "btnSwitchKeyboard");
		m_oButtonSwitchKeyBoard.setValue(AppGlobal.g_oLang.get()._("digit_table_number", ""));
		m_oContent.attachChild(m_oButtonSwitchKeyBoard);

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

		// create backspace button
		m_oFrameQwertyBackspace = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameQwertyBackspace, "fraBackspace");
		m_oFrameQwertyBackspace.allowClick(true);
		m_oFrameQwertyBackspace.setClickReplaceValue(null, "^(.*).<select></select>(.*)$", "$1<select></select>$2");
		m_oFrameQwertyBackspace.setClickReplaceValue(null, "^(.*)<select>.+</select>(.*)$", "$1<select></select>$2");
		m_oFrameQwertyBackspace.setClickServerRequestBlockUI(false);
		m_oContent.attachChild(m_oFrameQwertyBackspace);

		m_oImageQwertyBackspace = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageQwertyBackspace, "ImgBackspace");
		m_oImageQwertyBackspace.setExist(true);
		m_oImageQwertyBackspace
				.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/keyboard_arrow_sh.png");
		m_oImageQwertyBackspace.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_TO_FIT);
		m_oImageQwertyBackspace.setClickServerRequestBlockUI(false);
		m_oFrameQwertyBackspace.attachChild(m_oImageQwertyBackspace);

		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())
				&& oFloorPlanFunctionLookupButtonInfos != null) {
			// Initial the floor plan function bar
			m_oFunctionBar = new FrameTableFloorPlanFunctionBar();
			m_oTemplateBuilder.buildFrame(m_oFunctionBar, "fraFunctionBar");
			m_oFunctionBar.init(oFloorPlanFunctionLookupButtonInfos, iTotalPageNum);
			m_oFunctionBar.addListener(this);
			this.attachChild(m_oFunctionBar);
			
			m_oButtonSwitchKeyBoard.setTop(m_oButtonSwitchKeyBoard.getTop() - (int)(m_oFunctionBar.getHeight() * 0.8));
			
			m_oFrameQwertyKeyboard.setTop(m_oFrameQwertyKeyboard.getTop() - m_oFunctionBar.getHeight()/2);
		}
		
		m_bFunctionBarButtonClick = false;
	}

	public void resetFrame() {
		if (!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oFrameCover.setVisible(true);
			m_oFrameCover.bringToTop();
		}
		this.bringToTop();
	}

	public void resetTableNo() {
		m_otxtboxTable.setValue("");
	}

	public void setFocusOnTxtBox() {
		m_otxtboxTable.setFocus();
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
		m_oFrameOpenedCheckList.setVisible(false);
		m_oFrameOpenedCheckList.addListener(this);
		this.attachChild(m_oFrameOpenedCheckList);
	}
	
	public void setCloseButtonVisible(boolean bShow) {
		m_oFrameTitleHeader.setButtonShow(bShow);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (iChildId == m_oButtonOpenedChk.getId()) {
			PosCheck oPosCheck = new PosCheck();
			JSONArray oCheckListJSONArray = oPosCheck.getCheckListByBusinessDayPaid(
					AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(),
					AppGlobal.g_oFuncStation.get().getStationId(), PosCheck.PAID_NOT_PAID, false);
			m_oFrameOpenedCheckList.initCheckList(oCheckListJSONArray);
			m_oFrameOpenedCheckList.setVisible(true);
			m_oFrameOpenedCheckList.bringToTop();

			bMatchChild = true;
		} else if (iChildId == m_oButtonSwitchKeyBoard.getId()) {
			this.setVisible(false);
			for (FrameOpenTableWithQwertyKeyboardListener listener : listeners) {
				// Raise the event to parent
				listener.FrameOpenTableWithQwertyKeyboard_switchKeyboard();
			}
			bMatchChild = true;
		} else if (iChildId == m_oButtonLogout.getId()) {
			for (FrameOpenTableWithQwertyKeyboardListener listener : listeners) {
				// Raise the event to parent
				listener.FrameOpenTableWithQwertyKeyboard_logout();
			}
			bMatchChild = true;
		} else if (iChildId == m_oButtonOK.getId()) {
			this.FrameQwertyKeyboard_clickEnter();
			bMatchChild = true;
		} else if (iChildId == m_oButtonCancel.getId()) {
			this.FrameQwertyKeyboard_clickCancel();
			bMatchChild = true;
		}
		return bMatchChild;
	}

	@Override
	public void FrameQwertyKeyboard_clickEnter() {
		int iStrLength = m_otxtboxTable.getValue().length();
		String sErrMsg = "";
		boolean bShowErrorBox = false;

		if (m_otxtboxTable.getValue().contains(" ")) {
			bShowErrorBox = true;
			sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
		}

		FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
		oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
		if (iStrLength == 0) {
			bShowErrorBox = true;
			sErrMsg = AppGlobal.g_oLang.get()._("please_input_the_table_no");
		}
		if (iStrLength > 5) {
			bShowErrorBox = true;
			sErrMsg = AppGlobal.g_oLang.get()._("can_not_input_more_than_five_digits");
		}
		try {
			if(new BigDecimal(m_otxtboxTable.getValue()).compareTo(BigDecimal.ZERO) <= 0) {
				bShowErrorBox = true;
				sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
			}
		}catch(Exception e) {}
		
		if (bShowErrorBox) {
			oFormDialogBox.setMessage(sErrMsg);
			oFormDialogBox.show();
			oFormDialogBox = null;
			m_otxtboxTable.setValue("");
			return;
		}

		int iExtensionIndex = -1;
		String sTableExtension = "";
		String sTableNumWithoutExtension = "";

		boolean bOverride = true;

		if (bOverride) {
			for (Entry<Integer, OutFloorPlan> entry : AppGlobal.g_oFuncOutlet.get().getFloorPlanList().entrySet()) {
				// Build Floor Plan
				OutFloorPlan oOutFloorPlan = entry.getValue();
				if (oOutFloorPlan == null) {
					continue;
				}
				if (oOutFloorPlan.getMapCount() > 0) {
					for (int i = 0; i < oOutFloorPlan.getMapCount(); i++) {
						OutFloorPlanMap oOutFloorPlanMap = oOutFloorPlan.getMap(i);
						for (int j = 0; j < oOutFloorPlanMap.getTableCount(); j++) {
							OutFloorPlanTable oOutFloorPlanTable = oOutFloorPlanMap.getTable(j);
							if (oOutFloorPlanTable.getTable() == 0) {
								if (m_otxtboxTable.getValue().equals(oOutFloorPlanTable.getTableExt())) {
									sTableNumWithoutExtension = "0";
									sTableExtension = oOutFloorPlanTable.getTableExt();
									bOverride = false;
									break;
								}
							}
						}
					}
				}
			}
		}

		if (bOverride) {
			for (int i = 0; i < iStrLength; i++) {
				String sTableSubString = null;
				if (i == (iStrLength - 1))
					sTableSubString = m_otxtboxTable.getValue().substring(i);
				else
					sTableSubString = m_otxtboxTable.getValue().substring(i, (i + 1));

				try {
					Integer dNumeric = Integer.parseInt(sTableSubString);
				} catch (NumberFormatException exception) {
					iExtensionIndex = i;
				}
			}

			if (iExtensionIndex > 0) {
				sTableNumWithoutExtension = m_otxtboxTable.getValue().substring(0, iExtensionIndex);
				sTableExtension = m_otxtboxTable.getValue().substring(iExtensionIndex);
			} else
				sTableNumWithoutExtension = m_otxtboxTable.getValue().substring(0);

			try {
				Integer dNumeric = Integer.parseInt(sTableNumWithoutExtension);
			} catch (NumberFormatException exception) {
				sTableNumWithoutExtension = "0";
				sTableExtension = m_otxtboxTable.getValue();
			}
		}

		//Change the table extension value to upper case
		sTableExtension = sTableExtension.toUpperCase();
		
		//Change the table extension value to upper case
		sTableExtension = sTableExtension.toUpperCase();
		
		for (FrameOpenTableWithQwertyKeyboardListener listener : listeners) {
			// Raise the event to parent
			listener.FrameOpenTableWithQwertyKeyboard_openTable(sTableNumWithoutExtension, sTableExtension);
		}

	}

	@Override
	public void FrameQwertyKeyboard_clickCancel() {
		this.resetTableNo();
	}

	@Override
	public void FrameQwertyKeyboard_clickNumber(String string) {
	}

	@Override
	public void FrameOpenedCheckList_ButtonExitClicked() {
		m_oFrameOpenedCheckList.setVisible(false);
	}

	@Override
	public void FrameOpenedCheckList_RecordClicked(int iTable, String sTableExtension) {
		m_oFrameOpenedCheckList.setVisible(false);
		for (FrameOpenTableWithQwertyKeyboardListener listener : listeners) {
			// Raise the event to parent
			listener.FrameOpenTableWithQwertyKeyboard_openTable(String.valueOf(iTable), sTableExtension);
		}
	}

	@Override
	public void FrameOpenedCheckList_RecordLongClicked(int iTable, String sTableExtension) {
		m_oFrameOpenedCheckList.setVisible(false);
		for (FrameOpenTableWithQwertyKeyboardListener listener : listeners) {
			// Raise the event to parent
			listener.FrameOpenTableWithQwertyKeyboard_showTableFunction(String.valueOf(iTable), sTableExtension);
		}
	}

	public void setTableNo(String sTableNo) {
		m_otxtboxTable.setValue(sTableNo);
	}

	@Override
	public boolean valueChanged(int iChildId, String sNote) {
		boolean bMatchChild = false;
		String sCardNo = "";
		ClsActiveClient oActiveClient = AppGlobal.getActiveClient();
		if (oActiveClient != null && oActiveClient.getSwipeCardReaderElement().getValueChangedServerRequestNote()
				.equals(FuncMSR.FRAME_SWIPE_CARD_OPENTABLE_QWERTY)) {
			if (iChildId == oActiveClient.getSwipeCardReaderElement().getId()) {
				if (oActiveClient.getSwipeCardReaderElement().getValue().length() > 0)
					sCardNo = oActiveClient.getSwipeCardReaderElement().getValue().replace("\r", "").replace("\n", "");
				bMatchChild = true;
			}
			InfInterface oMsrPosInterfaceConfig = null;
			if (!AppGlobal.g_oFuncStation.get().getTableValidationMsrCode().isEmpty()) {
				if (FormMain.oExtraPosInterfaceConfig
						.containsKey(AppGlobal.g_oFuncStation.get().getTableValidationMsrCode()))
					oMsrPosInterfaceConfig = FormMain.oExtraPosInterfaceConfig
							.get(AppGlobal.g_oFuncStation.get().getTableValidationMsrCode());
				else {
					PosInterfaceConfig oPosInterfaceConfig = new PosInterfaceConfig();
					JSONArray oJSONArray = oPosInterfaceConfig.getInterfaceConfigsByInterfaceCode(
							AppGlobal.g_oFuncStation.get().getTableValidationMsrCode());
					if (oJSONArray != null) {
						for (int i = 0; i < oJSONArray.length(); i++) {
							try {
								if (oJSONArray.get(i) != null) {
									oMsrPosInterfaceConfig = new InfInterface(
											new JSONObject(oJSONArray.get(i).toString()));
									FormMain.oExtraPosInterfaceConfig.put(oMsrPosInterfaceConfig.getInterfaceCode(),
											oMsrPosInterfaceConfig);
									break;
								}
							} catch (JSONException e) {
								AppGlobal.stack2Log(e);
							}
						}
					}
				}
				if (oMsrPosInterfaceConfig != null) {
					// Capture information from swipe card
					FuncMSR oFuncMSR = new FuncMSR();
					int iErrorCode = oFuncMSR.processCardContent(sCardNo, oMsrPosInterfaceConfig.getSetting());

					// Get the necessary value
					sCardNo = oFuncMSR.getCardNo();
					if (iErrorCode == FuncMSR.ERROR_CODE_MISSING_SETUP) {
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
								this.getParentForm());
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(oFuncMSR.getLastErrorMessage());
						oFormDialogBox.show();
						oFormDialogBox = null;
						return bMatchChild;
					}
					if (sCardNo.isEmpty()) {
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
								this.getParentForm());
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_capture_card"));
						oFormDialogBox.show();
						oFormDialogBox = null;
						return bMatchChild;
					} else {
						if (sCardNo.length() > 5)
							sCardNo = sCardNo.substring(0, 5);
						m_otxtboxTable.setValue(sCardNo);
						FrameQwertyKeyboard_clickEnter();
					}
				}
			}
		}
		return bMatchChild;
	}

	@Override
	public void FrameTitleHeader_close() {
		this.resetTableNo();
		this.setVisible(false);

		for (FrameOpenTableWithQwertyKeyboardListener listener : listeners) {
			// Raise the event to parent
			listener.FrameOpenTableWithQwertyKeyboard_close();
		}
	}

	@Override
	public void frameFloorPlanFunctionBar_openPanelImageClicked() {
		for (FrameOpenTableWithQwertyKeyboardListener listener: listeners) {
			listener.frameOpenTableWithQwertyKeyboard_FunctionBarOpenPanelClicked();
		}
	}

	@Override
	public void frameFloorPlanFunctionBar_buttonClicked(String sNote) {
		m_bFunctionBarButtonClick = true;
		for (FrameOpenTableWithQwertyKeyboardListener listener: listeners) {
			listener.frameOpenTableWithQwertyKeyboard_FunctionBarButtonClicked(sNote);
		}
		m_bFunctionBarButtonClick = false;
	}
}
