package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import commonui.FormDialogBox;
import commonui.FrameNumberPad;
import commonui.FrameNumberPadListener;
import commonui.FrameQwertyKeyboard;
import commonui.FrameQwertyKeyboardListener;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import om.InfInterface;
import om.OutFloorPlan;
import om.OutFloorPlanMap;
import om.OutFloorPlanTable;
import om.PosInterfaceConfig;
import om.PosOutletTable;
import app.AppGlobal;
import app.FrameTableButton;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;
import virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FrameAskTableListener {
	void FrameAskTable_clickOK();
	void FrameAskTable_clickCancel();
	void FrameAskTable_longClicked(String sTableNo, String sTableExtension);
}

public class FrameAskTable extends VirtualUIFrame implements FrameNumberPadListener, FrameQwertyKeyboardListener, FrameTitleHeaderListener {

	TemplateBuilder m_oTemplateBuilder;
	
//KingsleyKwan20170918AskTable		-----Start-----
	private VirtualUIFrame m_oRightContent;
//KingsleyKwan20170918AskTable		----- End -----
	private VirtualUILabel m_oLabelMessage;
	private VirtualUILabel m_oLabelTableNoHeader;
	private VirtualUILabel m_oLabelTableExtensionHeader;
	private VirtualUIList m_oListTableName;
	private HashMap<String, VirtualUIFrame> m_oFrameTableNameList;
	private boolean m_bShowTableNameList;
	private VirtualUITextbox m_oTextboxTableNo;
	private VirtualUILabel m_oLabelExtension;
	private VirtualUILabel m_oLabelTableNo;
	private VirtualUIList m_oListTableExtension;
//KingsleyKwan20170918AskTable		-----Start-----
	private VirtualUIFrame m_oFrameTableExtensionCell;
//KingsleyKwan20170918AskTable		----- End -----
	private VirtualUIButton m_oButtonOK;
	private VirtualUIButton m_oButtonCancel;
	private FrameNumberPad m_oFrameNumberPad;

	private VirtualUIFrame m_oFrameListTableExtension; 
	private FrameQwertyKeyboard m_oFrameQwertyKeyboard;
//KingsleyKwan20170918AskTable		-----Start-----
	private VirtualUIFrame m_oFrameQwertyBackspace;
	private VirtualUIImage m_oImageQwertyBackspace;
//KingsleyKwan20170918AskTable		----- End -----
	private VirtualUIButton m_oButtonSwitchKeyboard;
	private int m_iCurrentInputKeyboard;

	private HashMap<String, FrameTableButton> m_oButtonTableExtension;

	private FrameTableButton m_oButtonSelectedTableExtension;
	private String m_sTableNo;
	private String m_sTableExtension;
	private int m_iTabelNameListOutletId = 0;

	private HashMap<String, String> m_sTableForegroundColor;
	private HashMap<String, String> m_sTableBackgroundColor;
	
	private FrameTitleHeader m_oTitleHeader;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameAskTableListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameAskTableListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameAskTableListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public void init(int iTableNo, boolean bNeedDefaultExtension, int iTargetTableNameListOutletId){ 
		if(AppGlobal.g_oFuncStation.get().getAskTableWithAdvanceMode() == 0){
			m_iCurrentInputKeyboard = FuncStation.OPEN_TABLE_SCREEN_KEYBOARD_NUMBER;
		}else{
			m_iCurrentInputKeyboard = FuncStation.OPEN_TABLE_SCREEN_KEYBOARD_QWERTY;
		}
		
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameAskTableListener>();
		m_sTableForegroundColor = new HashMap<String, String>();
		m_sTableBackgroundColor = new HashMap<String, String>();
		// Init extension list
		m_oButtonTableExtension = new HashMap<String, FrameTableButton>();

		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraAskTable.xml");
//KingsleyKwan20170918AskTable		-----Start-----
		
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		m_oTitleHeader.addListener(this);
		this.attachChild(m_oTitleHeader);
		
		m_oRightContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oRightContent, "fraRightContent");
		this.attachChild(m_oRightContent);
		
		// Message
		m_oLabelMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMessage, "lblMessage");
		this.attachChild(m_oLabelMessage);

		// Table No. Header
		m_oLabelTableNoHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableNoHeader, "lblTableNoHeader");
		m_oLabelTableNoHeader.setValue(AppGlobal.g_oLang.get()._("table_no"));
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oRightContent.attachChild(m_oLabelTableNoHeader);
		else
			this.attachChild(m_oLabelTableNoHeader);

		m_oFrameListTableExtension = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameListTableExtension, "fraListTableExtension");
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oRightContent.attachChild(m_oFrameListTableExtension);
		else
			this.attachChild(m_oFrameListTableExtension);

		// Table Extension Header
		m_oLabelTableExtensionHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableExtensionHeader, "lblTableExtensionHeader");
		m_oLabelTableExtensionHeader.setValue(AppGlobal.g_oLang.get()._("table_extension"));
		m_oFrameListTableExtension.attachChild(m_oLabelTableExtensionHeader);

		// Table Name List
		m_oFrameTableNameList = new HashMap<String, VirtualUIFrame>();
		m_bShowTableNameList = false;

		// Table No Input Box
		m_oTextboxTableNo = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxTableNo, "txtTableNo");
		m_oTextboxTableNo.setFocusWhenShow(true);
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oRightContent.attachChild(m_oTextboxTableNo);
		else
			this.attachChild(m_oTextboxTableNo);

		// Table Extension Label
		m_oLabelExtension = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelExtension, "lblExtension");
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oRightContent.attachChild(m_oLabelExtension);
		else
			this.attachChild(m_oLabelExtension);
		
		// Table No Label
		m_oLabelTableNo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableNo, "lblTableNo");
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oRightContent.attachChild(m_oLabelTableNo);
		else
			this.attachChild(m_oLabelTableNo);

		// Table Extension List
		m_oListTableExtension = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListTableExtension, "listTableExtension");
		m_oFrameListTableExtension.attachChild(m_oListTableExtension);
		
		// Table Extension Cell
		m_oFrameTableExtensionCell = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTableExtensionCell, "fraTableExtensionCell");
		this.attachChild(m_oFrameTableExtensionCell);
		// Resize the frame
		//m_oFrameListTableExtension.setHeight(m_oListTableExtension.getTop()+m_oListTableExtension.getHeight() + 10);
		//this.setHeight(m_oFrameListTableExtension.getTop() + m_oFrameListTableExtension.getHeight()+1);

		// Create OK Button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "btnOK");
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("ok"));
		m_oButtonOK.addClickServerRequestSubmitElement(this);
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oRightContent.attachChild(m_oButtonOK);
		else
			this.attachChild(m_oButtonOK);
		
		// Create Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "btnCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		m_oButtonCancel.setClickServerRequestBlockUI(false);
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oRightContent.attachChild(m_oButtonCancel);
		else
			this.attachChild(m_oButtonCancel);
		
		//create backspace button
		m_oFrameQwertyBackspace = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameQwertyBackspace, "fraBackspace");
		m_oFrameQwertyBackspace.allowClick(true);
		m_oFrameQwertyBackspace.setClickReplaceValue(null, "^(.*).<select></select>(.*)$", "$1<select></select>$2");
		m_oFrameQwertyBackspace.setClickReplaceValue(null, "^(.*)<select>.+</select>(.*)$", "$1<select></select>$2");
		m_oFrameQwertyBackspace.setClickServerRequestBlockUI(false);
		m_oFrameQwertyBackspace.setVisible(false);
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oRightContent.attachChild(m_oFrameQwertyBackspace);
		else
			this.attachChild(m_oFrameQwertyBackspace);
		
		m_oImageQwertyBackspace = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageQwertyBackspace, "ImgBackspace");
		m_oImageQwertyBackspace.setExist(true);
		m_oImageQwertyBackspace.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/keyboard_arrow_sh.png");
		m_oImageQwertyBackspace.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_TO_FIT);
		m_oImageQwertyBackspace.setClickServerRequestBlockUI(false);
		m_oFrameQwertyBackspace.attachChild(m_oImageQwertyBackspace);
		
//KingsleyKwan20170918AskTable		----- End -----
		//create switch keyboard button
		m_oButtonSwitchKeyboard = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSwitchKeyboard, "btnSwitchKeyboard");
		m_oButtonSwitchKeyboard.setClickServerRequestBlockUI(false);
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oRightContent.attachChild(m_oButtonSwitchKeyboard);
		else
			this.attachChild(m_oButtonSwitchKeyboard);
		
		// Get Default Status Background Color Codes
		JSONObject oTableStatusBackgroundColor = AppGlobal.g_oFuncStation.get().getTableStatusBackgroundColor();
		
		// Get table color
		VirtualUIFrame oFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrame, "fraNewTable");
		m_sTableForegroundColor.put(PosOutletTable.STATUS_NEW_TABLE, oFrame.getForegroundColor());
		m_sTableBackgroundColor.put(PosOutletTable.STATUS_NEW_TABLE, this.getTableStatusBackgroundColor(oFrame.getBackgroundColor(), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_VACANT));
		
		m_oTemplateBuilder.buildFrame(oFrame, "fraVacantTable");
		m_sTableForegroundColor.put(PosOutletTable.STATUS_VACANT, oFrame.getForegroundColor());
		m_sTableBackgroundColor.put(PosOutletTable.STATUS_VACANT, this.getTableStatusBackgroundColor(oFrame.getBackgroundColor(), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_VACANT));
		
		m_oTemplateBuilder.buildFrame(oFrame, "fraCleaningTable");
		m_sTableForegroundColor.put(PosOutletTable.STATUS_CLEANING_TABLE, oFrame.getForegroundColor());
		m_sTableBackgroundColor.put(PosOutletTable.STATUS_CLEANING_TABLE, this.getTableStatusBackgroundColor(oFrame.getBackgroundColor(), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_CLEANING));
		
		m_oTemplateBuilder.buildFrame(oFrame, "fraOccupiedTable");
		m_sTableForegroundColor.put(PosOutletTable.STATUS_OCCUPIED, oFrame.getForegroundColor());
		m_sTableBackgroundColor.put(PosOutletTable.STATUS_OCCUPIED, this.getTableStatusBackgroundColor(oFrame.getBackgroundColor(), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_OCCUPIED));
		
		m_oTemplateBuilder.buildFrame(oFrame, "fraPrintedTable");
		m_sTableForegroundColor.put(PosOutletTable.STATUS_CHECK_PRINTED, oFrame.getForegroundColor());
		m_sTableBackgroundColor.put(PosOutletTable.STATUS_CHECK_PRINTED, this.getTableStatusBackgroundColor(oFrame.getBackgroundColor(), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_PRINTED));
		
		m_oTemplateBuilder.buildFrame(oFrame, "fraOverTimeTable");
		m_sTableForegroundColor.put(PosOutletTable.STATUS_COOKING_OVERTIME, oFrame.getForegroundColor());
		m_sTableBackgroundColor.put(PosOutletTable.STATUS_COOKING_OVERTIME, oFrame.getBackgroundColor());
		
		if(bNeedDefaultExtension){
			addTableExtension("-", PosOutletTable.STATUS_NEW_TABLE, false, true, false);

			// Create extension list
			for(char alphabet = AppGlobal.TABLE_EXTENSION_START_LETTER; alphabet <= AppGlobal.TABLE_EXTENSION_END_LETTER;alphabet++){
				addTableExtension(String.valueOf(alphabet), PosOutletTable.STATUS_NEW_TABLE, false, false, false);
			}
		}
//KingsleyKwan20170918AskTable		-----Start-----
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			if(iTableNo > 0){
				m_oButtonSwitchKeyboard.setVisible(false);
				m_oRightContent.setTop(64);
				m_oRightContent.setLeft(0);
				m_oRightContent.setWidth(768);
				m_oRightContent.setBackgroundColor("");
				m_oRightContent.setShadowRadius(0);
				m_oTitleHeader.setWidth(768);
				m_oTitleHeader.resetPostion();
				m_oTitleHeader.setButtonShow(false);
				m_oFrameListTableExtension.setLeft(560);
				m_oFrameListTableExtension.setHeight(376);
				m_oLabelTableNoHeader.setLeft(34);
				
				m_oButtonOK.setTop(336);
				m_oButtonOK.setLeft(358);
				m_oButtonCancel.setTop(336);
				m_oButtonCancel.setLeft(52);
				
				// Set Table No Input Box
				m_oTextboxTableNo.setValue(iTableNo + "");
				m_oTextboxTableNo.setVisible(false);
	
				m_oLabelTableNo.setValue(AppGlobal.g_oFuncOutlet.get().getTableNameWithTableNo(Integer.toString(iTableNo), ""));
				m_oLabelTableNo.setVisible(true);	
			}else{
				m_oLabelTableNo.setVisible(false);
				// Number pad
				m_oFrameNumberPad = new FrameNumberPad();
				m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
				m_oFrameNumberPad.setNumPadLeft(16);
				m_oFrameNumberPad.setWithCancelAndEnterButton(false);
				m_oFrameNumberPad.init();
				//m_oFrameNumberPad.setTop(m_oTextboxTableNo.getTop() + m_oTextboxTableNo.getHeight() + 5);
				m_oFrameNumberPad.addListener(this);
				m_oFrameNumberPad.setEnterBlockUI(true);
				m_oFrameNumberPad.setVisible(false);
				
				m_oRightContent.attachChild(m_oFrameNumberPad);
	
				m_oFrameNumberPad.clearEnterSubmitId();
				m_oFrameNumberPad.setEnterSubmitId(m_oTextboxTableNo);
	
				m_oFrameQwertyKeyboard = new FrameQwertyKeyboard();
				m_oTemplateBuilder.buildFrame(m_oFrameQwertyKeyboard, "fraQwertyKeyboard");
//KingsleyKwan20170918AskTableShowButton		-----Start-----
				m_oFrameQwertyKeyboard.init();
//KingsleyKwan20170918AskTableShowButton		----- End -----
				m_oFrameQwertyKeyboard.setTop(m_oTextboxTableNo.getTop() + m_oTextboxTableNo.getHeight() + 10);			
				m_oFrameQwertyKeyboard.addListener(this);
				m_oFrameQwertyKeyboard.setEnterBlockUI(true);
				m_oFrameQwertyKeyboard.setVisible(false);
				m_oRightContent.attachChild(m_oFrameQwertyKeyboard);
	
				m_oFrameQwertyKeyboard.clearEnterSubmitId();
				m_oFrameQwertyKeyboard.setEnterSubmitId(m_oTextboxTableNo);
	
				this.switchKeyboard(m_iCurrentInputKeyboard);
				
				//m_oButtonSwitchKeyboard.setTop(m_oFrameNumberPad.getTop() + m_oFrameNumberPad.getHeight() +5);
				m_oButtonSwitchKeyboard.setVisible(true);
//KingsleyKwan20170918AskTable		----- End -----
				//Check list common basket
				VirtualUIFrame oFrameListTableName = new VirtualUIFrame();
				m_oTemplateBuilder.buildFrame(oFrameListTableName, "fraListTableName");
				oFrameListTableName.setVisible(false);
				this.attachChild(oFrameListTableName);
	
				m_oListTableName = new VirtualUIList();
				m_oTemplateBuilder.buildList(m_oListTableName, "listTableName");
				m_oListTableName.setVisible(false);
				oFrameListTableName.attachChild(m_oListTableName);
	
				m_iTabelNameListOutletId = iTargetTableNameListOutletId; 
				if(this.setTableNamelist()){
					oFrameListTableName.setVisible(true);
					m_oListTableName.setVisible(true);
					m_bShowTableNameList = true;
				}
			}
		}else {
			m_oButtonCancel.setVisible(false);
			m_oButtonOK.setVisible(false);
			
				// Number pad
				m_oFrameNumberPad = new FrameNumberPad();
				m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
				m_oFrameNumberPad.setNumPadLeft(16);
				m_oFrameNumberPad.init();
				m_oFrameNumberPad.setTop(m_oTextboxTableNo.getTop() + m_oTextboxTableNo.getHeight() + 10);
				m_oFrameNumberPad.addListener(this);
				m_oFrameNumberPad.setEnterBlockUI(true);
				m_oFrameNumberPad.setVisible(false);
				
				this.attachChild(m_oFrameNumberPad);
	
				m_oFrameNumberPad.clearEnterSubmitId();
				m_oFrameNumberPad.setEnterSubmitId(m_oTextboxTableNo);
	
				m_oFrameQwertyKeyboard = new FrameQwertyKeyboard();
				m_oTemplateBuilder.buildFrame(m_oFrameQwertyKeyboard, "fraQwertyKeyboard");
//KingsleyKwan20170918AskTableShowButton		-----Start-----
				m_oFrameQwertyKeyboard.init();
//KingsleyKwan20170918AskTableShowButton		----- End -----
				m_oFrameQwertyKeyboard.setTop(m_oTextboxTableNo.getTop() + m_oTextboxTableNo.getHeight() + 10);			
				m_oFrameQwertyKeyboard.addListener(this);
				m_oFrameQwertyKeyboard.setEnterBlockUI(true);
				m_oFrameQwertyKeyboard.setVisible(false);
				this.attachChild(m_oFrameQwertyKeyboard);
	
				m_oFrameQwertyKeyboard.clearEnterSubmitId();
				m_oFrameQwertyKeyboard.setEnterSubmitId(m_oTextboxTableNo);
	
				this.switchKeyboard(m_iCurrentInputKeyboard);
				
				m_oButtonSwitchKeyboard.setVisible(true);
				//Check list common basket
				VirtualUIFrame oFrameListTableName = new VirtualUIFrame();
				m_oTemplateBuilder.buildFrame(oFrameListTableName, "fraListTableName");
				oFrameListTableName.setVisible(false);
				this.attachChild(oFrameListTableName);
	
				m_oListTableName = new VirtualUIList();
				m_oTemplateBuilder.buildList(m_oListTableName, "listTableName");
				m_oListTableName.setVisible(false);
				oFrameListTableName.attachChild(m_oListTableName);
	
				m_iTabelNameListOutletId = iTargetTableNameListOutletId; 
				if(this.setTableNamelist()){
					oFrameListTableName.setVisible(true);
					m_oListTableName.setVisible(true);
					m_bShowTableNameList = true;
				}
		}
	}
//KingsleyKwan20170918AskTable		-----Start-----
	public boolean setTableNamelist(){
		List<HashMap<String, String>> m_oListTablesInfo = new ArrayList<HashMap<String, String>>();
		if(m_iTabelNameListOutletId == AppGlobal.g_oFuncOutlet.get().getOutletId())
			m_oListTablesInfo = AppGlobal.g_oFuncOutlet.get().getTableNameList();
		else{
			FuncOutlet oFuncOutlet = new FuncOutlet();
			oFuncOutlet.loadFloorPlan(m_iTabelNameListOutletId);
			m_oListTablesInfo = oFuncOutlet.getTableNameList();
		}

		if(m_oListTablesInfo.isEmpty()){
			return false;
		}
		
		VirtualUIFrame oFrameListTableNameItem = new VirtualUIFrame();
		oFrameListTableNameItem.setExist(true);
		oFrameListTableNameItem.setTop(0);
		oFrameListTableNameItem.setLeft(0);
		oFrameListTableNameItem.setHeight(48);
		oFrameListTableNameItem.setWidth(316);
		oFrameListTableNameItem.allowClick(false);
		//oFrameListTableNameItem.setBackgroundColor("#B2CBDA");
		oFrameListTableNameItem.setForegroundColor("#333333");
		m_oListTableName.attachChild(oFrameListTableNameItem);
		
		// Header Underline
		VirtualUIFrame oFrameHeaderUnderline = new VirtualUIFrame();
		oFrameHeaderUnderline.setExist(true);
		oFrameHeaderUnderline.setTop(0);
		oFrameHeaderUnderline.setLeft(0);
		oFrameHeaderUnderline.setHeight(1);
		oFrameHeaderUnderline.setWidth(316);
		oFrameHeaderUnderline.allowClick(false);
		oFrameHeaderUnderline.setBackgroundColor("#FF999999");
		m_oListTableName.attachChild(oFrameHeaderUnderline);

		VirtualUILabel oLabelTableNameHeader = new VirtualUILabel();
		oLabelTableNameHeader.setExist(true);
		oLabelTableNameHeader.setTop(1);
		oLabelTableNameHeader.setLeft(0);
		oLabelTableNameHeader.setHeight(40);
		oLabelTableNameHeader.setWidth(158);
		oLabelTableNameHeader.allowClick(false);
		oLabelTableNameHeader.setForegroundColor("#333333");
		oLabelTableNameHeader.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		oLabelTableNameHeader.setValue(AppGlobal.g_oLang.get()._("table_name"));
		oFrameListTableNameItem.attachChild(oLabelTableNameHeader);

		VirtualUILabel oLabelTabelNoHeader = new VirtualUILabel();
		oLabelTabelNoHeader.setExist(true);
		oLabelTabelNoHeader.setTop(1);
		oLabelTabelNoHeader.setLeft(158);
		oLabelTabelNoHeader.setHeight(40);
		oLabelTabelNoHeader.setWidth(158);
		oLabelTabelNoHeader.allowClick(false);
		oLabelTabelNoHeader.setForegroundColor("#333333");
		oLabelTabelNoHeader.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		oLabelTabelNoHeader.setValue(AppGlobal.g_oLang.get()._("table_no"));
		oFrameListTableNameItem.attachChild(oLabelTabelNoHeader);

		for(HashMap<String, String> oFloorPlanTable:m_oListTablesInfo) {
			if(!(oFloorPlanTable.get("tableName").isEmpty())) {

				oFrameListTableNameItem = new VirtualUIFrame();
				oFrameListTableNameItem.setExist(true);
				oFrameListTableNameItem.setTop(0);
				oFrameListTableNameItem.setLeft(0);
				oFrameListTableNameItem.setHeight(48);
				oFrameListTableNameItem.setWidth(316);
				oFrameListTableNameItem.allowClick(true);
				oFrameListTableNameItem.setForegroundColor("#333333");
				m_oListTableName.attachChild(oFrameListTableNameItem);
				
				// Cell Underline
				VirtualUIFrame oFrameCellUnderline = new VirtualUIFrame();
				oFrameCellUnderline.setExist(true);
				oFrameCellUnderline.setTop(0);
				oFrameCellUnderline.setLeft(0);
				oFrameCellUnderline.setHeight(1);
				oFrameCellUnderline.setWidth(316);
				oFrameCellUnderline.allowClick(false);
				oFrameCellUnderline.setBackgroundColor("#FFD8D8D8");
				m_oListTableName.attachChild(oFrameCellUnderline);

				oLabelTableNameHeader = new VirtualUILabel();
				oLabelTableNameHeader.setExist(true);
				oLabelTableNameHeader.setTop(0);
				oLabelTableNameHeader.setLeft(0);
				oLabelTableNameHeader.setHeight(48);
				oLabelTableNameHeader.setWidth(158);
				oLabelTableNameHeader.allowClick(false);
				oLabelTableNameHeader.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				String[] sTableNameArray = new String[AppGlobal.LANGUAGE_COUNT];
				for (int i=0; i<AppGlobal.LANGUAGE_COUNT; i++) {
					sTableNameArray[i] = oFloorPlanTable.get("tableName"+(i+1));
				}
				oLabelTableNameHeader.setValue(sTableNameArray);
				oFrameListTableNameItem.attachChild(oLabelTableNameHeader);

				oLabelTabelNoHeader = new VirtualUILabel();
				oLabelTabelNoHeader.setExist(true);
				oLabelTabelNoHeader.setTop(0);
				oLabelTabelNoHeader.setLeft(158);
				oLabelTabelNoHeader.setHeight(48);
				oLabelTabelNoHeader.setWidth(158);
				oLabelTabelNoHeader.allowClick(false);
				oLabelTabelNoHeader.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				String sValue = "";
				if(oFloorPlanTable.get("table").equals("0"))
					sValue = oFloorPlanTable.get("tableExt");
				else
					sValue = oFloorPlanTable.get("table")+oFloorPlanTable.get("tableExt");
				oLabelTabelNoHeader.setValue(sValue);
				oFrameListTableNameItem.attachChild(oLabelTabelNoHeader);

				m_oFrameTableNameList.put(oFloorPlanTable.get("table")+"_"+oFloorPlanTable.get("tableExt"), oFrameListTableNameItem);
			}
		}
		return true;
	}
//KingsleyKwan20170918AskTable		----- End -----
	
	public void addTableExtension(String alphabet, String sStatus, boolean bLocked, boolean bSelect, boolean bAllowLongClick){
		FrameTableButton oFrameTableButton = new FrameTableButton();
		
		// get default table color code
		JSONObject oTableStatusBackgroundColor = AppGlobal.g_oFuncStation.get().getTableStatusBackgroundColor();
		
		if(m_oButtonTableExtension.isEmpty())
			oFrameTableButton.setTop(0);
		else
			oFrameTableButton.setTop(8);
		oFrameTableButton.setWidth(m_oFrameTableExtensionCell.getWidth());
		oFrameTableButton.setHeight(m_oFrameTableExtensionCell.getHeight());
		oFrameTableButton.setLeft(m_oFrameTableExtensionCell.getLeft());
		oFrameTableButton.setExist(true);
		oFrameTableButton.setupTableButton(StringLib.createStringArray(5, alphabet), OutFloorPlanTable.SHAPE_RHOMBUS, 0, "");
		oFrameTableButton.setCornerRadius("2");
		oFrameTableButton.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		oFrameTableButton.allowClick(true);
		oFrameTableButton.addClickServerRequestSubmitElement(this);
////RayHuen20171023AddShadowToTheButton -----Start------
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.horizontal_desktop.name())) {
			oFrameTableButton.setShadowTop(3);
			oFrameTableButton.setLeft(0);
			oFrameTableButton.setShadowRadius(5);
			oFrameTableButton.setBackgroundColor("#FFFFFF");
			oFrameTableButton.setShadowColor("#17000000");
		}
////RayHuen20171023AddShadowToTheButton -----End------
		if(bAllowLongClick){
			oFrameTableButton.allowLongClick(true);
			oFrameTableButton.setLongClickServerRequestBlockUI(false);
		}
		
		if(sStatus.equals(PosOutletTable.STATUS_NEW_TABLE)){
			if(m_sTableForegroundColor.containsKey(sStatus)){
				oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
//KingsleyKwan20170918ByKing		-----Start-----
				oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#FFFFFF", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_VACANT), PosOutletTable.STATUS_NEW_TABLE, AppGlobal.g_oLang.get()._("vacant", ""));
			}else
				oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#FFFFFF", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_VACANT), PosOutletTable.STATUS_NEW_TABLE, AppGlobal.g_oLang.get()._("vacant", ""));
//KingsleyKwan20170918ByKing		----- End -----
			oFrameTableButton.setPrinted(false);
			oFrameTableButton.setLocked(false);
		}else
			if(sStatus.equals(PosOutletTable.STATUS_VACANT)){
				if(m_sTableForegroundColor.containsKey(sStatus)){
					oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
					oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor(m_sTableBackgroundColor.get(sStatus), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_VACANT), PosOutletTable.STATUS_VACANT, AppGlobal.g_oLang.get()._("vacant", ""));
				}else
					oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#FFFFFF", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_VACANT), PosOutletTable.STATUS_VACANT, AppGlobal.g_oLang.get()._("vacant", ""));
				oFrameTableButton.setPrinted(false);
				oFrameTableButton.setLocked(false);
			}else
				if(sStatus.equals(PosOutletTable.STATUS_OCCUPIED)){
					if(m_sTableForegroundColor.containsKey(sStatus)){
						oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
						oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor(m_sTableBackgroundColor.get(sStatus), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_OCCUPIED), PosOutletTable.STATUS_OCCUPIED, AppGlobal.g_oLang.get()._("send_check", ""));
					}else
						oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#FF0000", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_OCCUPIED), PosOutletTable.STATUS_OCCUPIED, AppGlobal.g_oLang.get()._("send_check", ""));
					oFrameTableButton.setPrinted(false);
					oFrameTableButton.setLocked(false);
				}else
					if(sStatus.equals(PosOutletTable.STATUS_CHECK_PRINTED)){
						if(m_sTableForegroundColor.containsKey(sStatus)){
							oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
							oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor(m_sTableBackgroundColor.get(sStatus), oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_PRINTED), PosOutletTable.STATUS_CHECK_PRINTED, AppGlobal.g_oLang.get()._("print_check", ""));
						}else
							oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#00FF00", oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_PRINTED), PosOutletTable.STATUS_CHECK_PRINTED, AppGlobal.g_oLang.get()._("print_check", ""));
						oFrameTableButton.setPrinted(true);
						oFrameTableButton.setLocked(false);
					}
		else 
		if(sStatus.equals(PosOutletTable.STATUS_COOKING_OVERTIME)){
			JSONObject oTempJSONObject = AppGlobal.g_oFuncStation.get().getTableFloorPlanSetting();
			if(oTempJSONObject != null && oTempJSONObject.optString("support_cooking_overtime").equals("y")
					&& !oTempJSONObject.optString("cooking_overtime_status_color", "").isEmpty()) {
				oFrameTableButton.setLabelForegroundColor("#FFFFFF");
				oFrameTableButton.setTableColor(oTempJSONObject.optString("cooking_overtime_status_color", "#66A6F1"), PosOutletTable.STATUS_COOKING_OVERTIME, AppGlobal.g_oLang.get()._("cooking_overtime", ""));
			}else if(m_sTableForegroundColor.containsKey(sStatus)){
				oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
				oFrameTableButton.setTableColor(m_sTableBackgroundColor.get(sStatus), PosOutletTable.STATUS_COOKING_OVERTIME, AppGlobal.g_oLang.get()._("cooking_overtime", ""));
			}else
				oFrameTableButton.setTableColor("#66A6F1", PosOutletTable.STATUS_COOKING_OVERTIME, AppGlobal.g_oLang.get()._("cooking_overtime", ""));
			
			oFrameTableButton.setPrinted(false);
			oFrameTableButton.setLocked(false);
		}
		else 
		if(sStatus.equals(PosOutletTable.STATUS_CLEANING_TABLE)){
			if(m_sTableForegroundColor.containsKey(sStatus)){
				oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
				oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor(m_sTableBackgroundColor.get(sStatus),oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_CLEANING), PosOutletTable.STATUS_CLEANING_TABLE, AppGlobal.g_oLang.get()._("cleaning", ""));
			}else
				oFrameTableButton.setTableColor(this.getTableStatusBackgroundColor("#C68C53",oTableStatusBackgroundColor, PosOutletTable.TABLE_BACKGROUND_COLOR_STATUS_CLEANING), PosOutletTable.STATUS_CLEANING_TABLE, AppGlobal.g_oLang.get()._("cleaning", ""));
			oFrameTableButton.setPrinted(false);
			oFrameTableButton.setLocked(false);
		}
		
		if(bLocked){
			oFrameTableButton.setLocked(true);
		}

		oFrameTableButton.setViewSeq(m_oButtonTableExtension.size() + 1);

		m_oButtonTableExtension.put(alphabet, oFrameTableButton);
		m_oListTableExtension.attachChild(oFrameTableButton);

		if(bSelect){
			m_oButtonSelectedTableExtension = oFrameTableButton;	
			selectTableExtensionButton(m_oButtonSelectedTableExtension, true);
			m_sTableExtension = alphabet;
		}

	}

	public void addTableDetailType(String sKey, String sIconURL){
		for(FrameTableButton oFrameTableButton:m_oButtonTableExtension.values()){
			oFrameTableButton.addTableDetailType(sKey, sIconURL, true);
		}
	}

	public void updateTableDetail(String alphabet, String sKey, String[] sDetail){
		FrameTableButton oFrameTableButton;
		boolean bForceIconVisible = true;

		if(m_oButtonTableExtension.containsKey(alphabet)){
			oFrameTableButton = m_oButtonTableExtension.get(alphabet);
			oFrameTableButton.setIconVisible(bForceIconVisible);
			oFrameTableButton.setTableDetail(sKey, sDetail);
			oFrameTableButton.setLabelForegroundColor(oFrameTableButton.getLabelForegroundColor());
		}
	}

	public void setTableDetailByIndex(int iIndex){
		for(FrameTableButton oFrameTableButton:m_oButtonTableExtension.values()){
			oFrameTableButton.setTableDetailByIndex(iIndex);
		}
	}

	private void selectTableExtensionButton(FrameTableButton oFrameTableButton, boolean bSelected){
		if(bSelected){
			oFrameTableButton.setStroke(1);
			oFrameTableButton.setStrokeColor("#0055B8");
		}else{
			oFrameTableButton.setStroke(0);
		}
	}

	public void setTitle(String sTitle){
		m_oTitleHeader.setTitle(sTitle);
	}

	public void setMessage(String sMessage){
		m_oLabelMessage.setValue(sMessage);
	}

	public void setDefaultTableNo(String sTableNo) {
		m_oTextboxTableNo.setValue(sTableNo);
	}

	public void setDefaultTableExtension(String sTableExtension) {
		for(Map.Entry<String, FrameTableButton> entry:m_oButtonTableExtension.entrySet()){
			if (sTableExtension.equals(entry.getKey())) {
				m_sTableExtension = entry.getKey();
				FrameTableButton oButton = entry.getValue();

				if(m_oButtonSelectedTableExtension != null){
					// Resume the color
					selectTableExtensionButton(m_oButtonSelectedTableExtension, false);
				}

				m_oButtonSelectedTableExtension = oButton;
				selectTableExtensionButton(m_oButtonSelectedTableExtension, true);

				break;
			}
		}

		m_oLabelExtension.setValue(sTableExtension);
	}

	public String getTableNo(){
		return m_sTableNo;
	}

	public String getTableExtension(){
		if(m_sTableExtension.equals("-"))
			return "";
		else
			return m_sTableExtension;
	}

	public boolean isShowTableNameList(){
		return m_bShowTableNameList;
	}

	public int getKeyboardMode(){
		return m_iCurrentInputKeyboard;
	}
	
	public String getTableStatusBackgroundColor(String sOriginalColor, JSONObject oTableStatusBackgroundColor, String sStatus) {
		String sTableStatusBgColor = sOriginalColor;
		if (oTableStatusBackgroundColor == null)
			return sTableStatusBgColor;
		
		if(oTableStatusBackgroundColor.has(sStatus) && !oTableStatusBackgroundColor.optString(sStatus).isEmpty())
			sTableStatusBgColor = "#"+oTableStatusBackgroundColor.optString(sStatus);
		
		return sTableStatusBgColor;
	}
	
//KingsleyKwan20170918AskTable		-----Start-----
	private void switchKeyboard(int iMode){
		if(iMode == FuncStation.OPEN_TABLE_SCREEN_KEYBOARD_NUMBER){
			//m_oButtonSwitchKeyboard.setWidth(m_oFrameNumberPad.getWidth());
			m_oLabelTableExtensionHeader.setVisible(true);
			m_oFrameNumberPad.setVisible(true);
			m_oFrameListTableExtension.setVisible(true);
			m_oLabelExtension.setVisible(true);
			m_oFrameQwertyKeyboard.setVisible(false);
			m_oButtonSwitchKeyboard.setValue(AppGlobal.g_oLang.get()._("alphanumeric_table_number", ""));
			m_oButtonSwitchKeyboard.setTop(395);
			m_oFrameQwertyBackspace.setVisible(false);
			m_iCurrentInputKeyboard = FuncStation.OPEN_TABLE_SCREEN_KEYBOARD_NUMBER;
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())){			
				m_oLabelTableNoHeader.setWidth(380);
				m_oTextboxTableNo.setHeight(50);
				m_oTextboxTableNo.setTop(m_oLabelTableNoHeader.getTop() + m_oLabelTableNoHeader.getHeight() + 5);
				m_oTextboxTableNo.setLeft(10);
				m_oFrameQwertyKeyboard.setTop(m_oTextboxTableNo.getTop()+ m_oTextboxTableNo.getHeight() + 10);
				m_oButtonSwitchKeyboard.setTop(420);
				m_oButtonSwitchKeyboard.setWidth(m_oFrameNumberPad.getWidth());
				//m_oButtonSwitchKeyboard.setTop(m_oFrameNumberPad.getTop() + m_oFrameNumberPad.getHeight()+5);
			}
		}
		else{
			//m_oButtonSwitchKeyboard.setWidth(m_oFrameQwertyKeyboard.getWidth());
			m_oLabelTableExtensionHeader.setVisible(false);
			m_oFrameNumberPad.setVisible(false);
			m_oFrameListTableExtension.setVisible(false);
			m_oLabelExtension.setVisible(false);
			m_oFrameQwertyKeyboard.setVisible(true);
			m_oButtonSwitchKeyboard.setValue(AppGlobal.g_oLang.get()._("digital_table_number", ""));
			m_oButtonSwitchKeyboard.setTop(351);
			m_oFrameQwertyBackspace.setVisible(true);
			m_iCurrentInputKeyboard = FuncStation.OPEN_TABLE_SCREEN_KEYBOARD_QWERTY;

			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())){			
				m_oLabelTableNoHeader.setWidth(50);
				m_oTextboxTableNo.setHeight(40);
				m_oTextboxTableNo.setTop(m_oLabelTableNoHeader.getTop() + 5);
				m_oTextboxTableNo.setLeft(m_oLabelTableNoHeader.getWidth() + 20);
				m_oFrameQwertyKeyboard.setTop(m_oTextboxTableNo.getTop()+ m_oTextboxTableNo.getHeight() + 10);
				m_oButtonSwitchKeyboard.setTop(425);
				m_oButtonSwitchKeyboard.setWidth(m_oFrameQwertyKeyboard.getWidth());
				//m_oButtonSwitchKeyboard.setTop(m_oFrameQwertyKeyboard.getTop() + m_oFrameQwertyKeyboard.getHeight()+5);
			}
		}
	}
//KingsleyKwan20170918AskTable		----- End -----
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (iChildId == m_oButtonOK.getId()) {
			if(m_iCurrentInputKeyboard == FuncStation.OPEN_TABLE_SCREEN_KEYBOARD_NUMBER) {
				// Number pad view
				m_sTableNo = m_oTextboxTableNo.getValue();
	
				for (FrameAskTableListener listener : listeners) {
					// Raise the event to parent
					listener.FrameAskTable_clickOK();
				}
			} else {
				// Keyboard view
				int iStrLength = m_oTextboxTableNo.getValue().length();
				int iExtensionIndex = -1;
				String sTableExtension = "";
				String sTableNumWithoutExtension = "";

				boolean bOverride = true;

				if(bOverride){
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
										if(m_oTextboxTableNo.getValue().equals(oOutFloorPlanTable.getTableExt())){
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

				if(bOverride){
					for(int i=0; i<iStrLength; i++) {
						String sTableSubString = null;
						if(i == (iStrLength - 1))
							sTableSubString = m_oTextboxTableNo.getValue().substring(i);
						else
							sTableSubString = m_oTextboxTableNo.getValue().substring(i, (i+1));

						try {
							double dNumeric = Double.parseDouble(sTableSubString);
						}catch(NumberFormatException exception) {
							iExtensionIndex = i;
						}
					}

					if(iExtensionIndex > 0) {
						sTableNumWithoutExtension = m_oTextboxTableNo.getValue().substring(0, iExtensionIndex);
						sTableExtension = m_oTextboxTableNo.getValue().substring(iExtensionIndex);
					}else
						sTableNumWithoutExtension = m_oTextboxTableNo.getValue().substring(0);

					//table name by wai
					try {
						double dNumeric = Double.parseDouble(sTableNumWithoutExtension);
					}catch(NumberFormatException exception) {
						sTableNumWithoutExtension = "0";
						sTableExtension = m_oTextboxTableNo.getValue();
					}
				}

				m_sTableNo = sTableNumWithoutExtension;
				m_sTableExtension = sTableExtension;

				if(m_sTableExtension.length() > 5){
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("can_not_input_more_than_five_digits"));
					oFormDialogBox.show();
					oFormDialogBox = null;
					m_oTextboxTableNo.setValue("");
					return true;
				}

				for (FrameAskTableListener listener : listeners) {
					// Raise the event to parent
					listener.FrameAskTable_clickOK();
				}
			}

			bMatchChild = true;
		}
		else if (iChildId == m_oButtonCancel.getId()) {
			for (FrameAskTableListener listener : listeners) {
				// Raise the event to parent
				listener.FrameAskTable_clickCancel();
			}

			bMatchChild = true;
		}
		else if (iChildId == m_oButtonSwitchKeyboard.getId()) {
			if(m_iCurrentInputKeyboard == FuncStation.OPEN_TABLE_SCREEN_KEYBOARD_NUMBER)
				this.switchKeyboard(FuncStation.OPEN_TABLE_SCREEN_KEYBOARD_QWERTY);
			else
				this.switchKeyboard(FuncStation.OPEN_TABLE_SCREEN_KEYBOARD_NUMBER);
			bMatchChild = true;
		} else {
			for(Map.Entry<String, FrameTableButton> entry:m_oButtonTableExtension.entrySet()){
				FrameTableButton oButton = entry.getValue();
				if (iChildId == oButton.getId()){
					m_sTableNo = m_oTextboxTableNo.getValue();
					if (m_oTextboxTableNo.getValue().equals("0")) {
						String sErrMsg = AppGlobal.g_oLang.get()._("cannot_input_zero_table_no");
						sErrMsg += (System.lineSeparator() + AppGlobal.g_oLang.get()._("please_use_alphanumeric_table"));
						FormDialogBox oDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
								super.getParentForm());
						oDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oDialogBox.setMessage(sErrMsg);
						oDialogBox.show();
						m_oTextboxTableNo.setValue("");
						return false;
					}

					m_sTableExtension = entry.getKey();

					if(m_oButtonSelectedTableExtension != null){
						// Resume the color
						selectTableExtensionButton(m_oButtonSelectedTableExtension, false);
					}
					m_oButtonSelectedTableExtension = oButton;
					selectTableExtensionButton(m_oButtonSelectedTableExtension, true);

					if(m_sTableExtension.equals("-"))
						m_oLabelExtension.setValue("");
					else
						m_oLabelExtension.setValue(m_sTableExtension);

					if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
						if(m_oLabelTableNo.getVisible()){
							if(m_sTableExtension.equals("-"))
								m_oLabelTableNo.setValue(m_oTextboxTableNo.getValue());
							else
								m_oLabelTableNo.setValue(m_oTextboxTableNo.getValue() + m_sTableExtension);
						}
					}

					m_sTableNo = m_oTextboxTableNo.getValue();
					if(m_sTableNo.length() > 0){
						for (FrameAskTableListener listener : listeners) {
							// Raise the event to parent
							listener.FrameAskTable_clickOK();
						}
					}

					bMatchChild = true; 
				}
			}

			for(Map.Entry<String, VirtualUIFrame> entry:m_oFrameTableNameList.entrySet()){
				VirtualUIFrame oFrame = entry.getValue();
				String [] sTable = entry.getKey().split("_");
				if (iChildId == oFrame.getId()){
					if(sTable.length>0){
						String sTableNo = sTable[0];
						String sTableExt = "";
						if(sTable.length == 2)
							sTableExt = sTable[1];
						m_sTableNo = sTableNo;
						m_sTableExtension = sTableExt;

						if(m_sTableNo.length() > 0){
							for (FrameAskTableListener listener : listeners) {
								// Raise the event to parent
								listener.FrameAskTable_clickOK();
							}
						}
					}

					bMatchChild = true; 
				}
			}
		}

		return bMatchChild;
	}

	@Override
	public boolean longClicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		for(Map.Entry<String, FrameTableButton> entry:m_oButtonTableExtension.entrySet()){
			FrameTableButton oButton = entry.getValue();
			if (iChildId == oButton.getId()){
				String sTableNo = m_oTextboxTableNo.getValue();
				String sTableExtension = entry.getKey();
				if(sTableExtension.equals("-")){
					for (FrameAskTableListener listener : listeners) {
						// Raise the event to parent
						listener.FrameAskTable_longClicked(sTableNo, "");
					}
				}else{
					for (FrameAskTableListener listener : listeners) {
						// Raise the event to parent
						listener.FrameAskTable_longClicked(sTableNo, sTableExtension);
					}
				}
			}
		}

		return bMatchChild;
	}

	@Override
	public void FrameNumberPad_clickEnter() {

		m_sTableNo = m_oTextboxTableNo.getValue();

		for (FrameAskTableListener listener : listeners) {
			// Raise the event to parent
			listener.FrameAskTable_clickOK();
		}
	}

	@Override
	public void FrameNumberPad_clickCancel() {
		for (FrameAskTableListener listener : listeners) {
			// Raise the event to parent
			listener.FrameAskTable_clickCancel();
		}
	}

	@Override
	public void FrameNumberPad_clickNumber(String string) {
	}

	@Override
	public void FrameQwertyKeyboard_clickEnter() {
	}

	@Override
	public void FrameQwertyKeyboard_clickCancel() {
		// TODO Auto-generated method stub
		for (FrameAskTableListener listener : listeners) {
			// Raise the event to parent
			listener.FrameAskTable_clickCancel();
		}
	}

	@Override
	public void FrameQwertyKeyboard_clickNumber(String string) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean valueChanged(int iChildId, String sNote) {
		boolean bMatchChild = false;
		String sTableExtension = "";
		String sCardNo = "";

		ClsActiveClient oActiveClient = AppGlobal.getActiveClient();
		if (oActiveClient != null) {
			if (iChildId == oActiveClient.getSwipeCardReaderElement().getId()
					&& oActiveClient.getSwipeCardReaderElement().getValueChangedServerRequestNote()
					.equals(FuncMSR.FRAME_SWIPE_CARD_DEFAULT)) {
				if (oActiveClient.getSwipeCardReaderElement().getValue().length() > 0) {
					sCardNo = oActiveClient.getSwipeCardReaderElement().getValue().replace("\r", "").replace("\n", "");
				}
				bMatchChild = true;
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
									// TODO Auto-generated catch block
									AppGlobal.stack2Log(e);
								}
							}
						} else {
							FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
									this.getParentForm());
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
							HashMap<String, String> oTableInfo = AppGlobal.g_oFuncStation.get()
									.getTableNoAndTableExtension(sCardNo);
							if (oTableInfo.containsKey("tableNo"))
								sCardNo = oTableInfo.get("tableNo");
							if (oTableInfo.containsKey("tableExt"))
								sTableExtension = oTableInfo.get("tableExt");
							try {
								int iTableNo = Integer.parseInt(sCardNo.trim());
							} catch (NumberFormatException e) {
								String sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
								FormDialogBox oDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"),
										this.getParentForm());
								oDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
								oDialogBox.setMessage(sErrMsg);
								oDialogBox.show();
								return bMatchChild;
							}
							m_sTableNo = sCardNo;
							m_sTableExtension = sTableExtension;
							m_oTextboxTableNo.setValue(sCardNo);
							m_oLabelExtension.setValue(sTableExtension);
						}
						for (FrameAskTableListener listener : listeners) {
							// Raise the event to parent
							listener.FrameAskTable_clickOK();
						}
					}
				}
			}
		}
		return bMatchChild;
	}

	@Override
	public void FrameTitleHeader_close() {
		for (FrameAskTableListener listener : listeners) {
			// Raise the event to parent
			listener.FrameAskTable_clickCancel();
		}
	}
}
