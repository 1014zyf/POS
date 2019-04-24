package app;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import org.json.JSONException;
import org.json.JSONObject;

import commonui.FormDialogBox;
import core.Controller;
import externallib.StringLib;
import app.AppGlobal;
import app.FrameTableButton;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormAskTable extends VirtualUIForm implements FrameAskTableListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameAskTable m_oFrameAskTable;
	private Integer m_iTableNo;
	private String m_sTableExtension;
	private boolean m_bLongClick;
	private boolean m_bCancelClick;
	
	public FormAskTable(Controller oParentController){
		super(oParentController);
		
		m_iTableNo = 0;
		m_sTableExtension = "";
		m_bLongClick = false;
		m_bCancelClick = false;
	}
	
	public boolean init(int iTableNo, boolean bNeedDefaultExtension, int iTargetTableNameListOutletId) {
		m_oTemplateBuilder = new TemplateBuilder();
		
//KingsleyKwan20170918AskTable		-----Start-----
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmAskTable.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameAskTable = new FrameAskTable();
		if(iTableNo > 0)
			m_oTemplateBuilder.buildFrame(m_oFrameAskTable, "fraAskOpenTable");
		else
			m_oTemplateBuilder.buildFrame(m_oFrameAskTable, "fraAskTable");
		m_oFrameAskTable.init(iTableNo, bNeedDefaultExtension, iTargetTableNameListOutletId);
		// Add listener;
		m_oFrameAskTable.addListener(this);
		m_oFrameAskTable.setVisible(true);		// Show this frame during first initial
		this.attachChild(m_oFrameAskTable);
		
//		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.horizontal_desktop.name())){
//			if(iTableNo > 0)
//				m_oFrameAskTable.setHeight(470);
//			else
//				m_oFrameAskTable.setHeight(520);
//			m_oFrameAskTable.setTop((oCoverFrame.getHeight()-m_oFrameAskTable.getHeight()) / 2);
//		}
		
		if(m_oFrameAskTable.isShowTableNameList())
			m_oFrameAskTable.setLeft((oCoverFrame.getWidth()-m_oFrameAskTable.getWidth()) / 2);
//KingsleyKwan20170918AskTable		----- End -----
		return true;
	}
	
	public void addTableExtension(String alphabet, String sStatus, boolean bLocked, boolean bSelect, boolean bAllowLongClick){
		m_oFrameAskTable.addTableExtension(alphabet, sStatus, bLocked, bSelect, bAllowLongClick);
	}
	
	// add open time, cover no, check total, svc member icon of each table button
	public void addTableDetailTypes() {
		JSONObject oSwitchCheckInfoSetting = AppGlobal.g_oFuncStation.get().getSwitchCheckInfoSetting();
		boolean bIsTurnOffSwitchInfo = this.isTurnOffAllSwitchCheckInfo(oSwitchCheckInfoSetting);
		
		if(bIsTurnOffSwitchInfo || (oSwitchCheckInfoSetting != null && oSwitchCheckInfoSetting.optString(FrameTableButton.STATUS_OPEN_TIME).equals("y")))
			m_oFrameAskTable.addTableDetailType(FrameTableButton.STATUS_OPEN_TIME, AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/floor_check_open_time.png");
		if(bIsTurnOffSwitchInfo || (oSwitchCheckInfoSetting != null && oSwitchCheckInfoSetting.optString(FrameTableButton.STATUS_COVER_NO).equals("y")))
			m_oFrameAskTable.addTableDetailType(FrameTableButton.STATUS_COVER_NO, AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/floor_cover_no.png");
		if(bIsTurnOffSwitchInfo || (oSwitchCheckInfoSetting != null && oSwitchCheckInfoSetting.optString(FrameTableButton.STATUS_CHECK_TOTAL).equals("y")))
			m_oFrameAskTable.addTableDetailType(FrameTableButton.STATUS_CHECK_TOTAL, "");
		if(bIsTurnOffSwitchInfo || (oSwitchCheckInfoSetting != null && oSwitchCheckInfoSetting.optString(FrameTableButton.STATUS_MEMBER_NUMBER).equals("y")))
			m_oFrameAskTable.addTableDetailType(FrameTableButton.STATUS_MEMBER_NUMBER, "");
		if(bIsTurnOffSwitchInfo || (oSwitchCheckInfoSetting != null && oSwitchCheckInfoSetting.optString(FrameTableButton.STATUS_MEMBER_NAME).equals("y")))
			m_oFrameAskTable.addTableDetailType(FrameTableButton.STATUS_MEMBER_NAME, "");
		if(bIsTurnOffSwitchInfo || (oSwitchCheckInfoSetting != null && oSwitchCheckInfoSetting.optString(FrameTableButton.STATUS_OWNER_NAME).equals("y")))
			m_oFrameAskTable.addTableDetailType(FrameTableButton.STATUS_OWNER_NAME, "");
		
		if(AppGlobal.g_oFuncStation.get().isAllowShowTableSize())
			m_oFrameAskTable.addTableDetailType(FrameTableButton.STATUS_TABLE_SIZE, "");
		
		if(oSwitchCheckInfoSetting != null){
			if(oSwitchCheckInfoSetting.optString(FrameTableButton.STATUS_CHECK_INFO_ONE).equals("y"))
				m_oFrameAskTable.addTableDetailType(FrameTableButton.STATUS_CHECK_INFO_ONE, "");
			if(oSwitchCheckInfoSetting.optString(FrameTableButton.STATUS_CHECK_INFO_TWO).equals("y"))
				m_oFrameAskTable.addTableDetailType(FrameTableButton.STATUS_CHECK_INFO_TWO, "");
			if(oSwitchCheckInfoSetting.optString(FrameTableButton.STATUS_CHECK_INFO_THREE).equals("y"))
				m_oFrameAskTable.addTableDetailType(FrameTableButton.STATUS_CHECK_INFO_THREE, "");
			if(oSwitchCheckInfoSetting.optString(FrameTableButton.STATUS_CHECK_INFO_FOUR).equals("y"))
				m_oFrameAskTable.addTableDetailType(FrameTableButton.STATUS_CHECK_INFO_FOUR, "");
			if(oSwitchCheckInfoSetting.optString(FrameTableButton.STATUS_CHECK_INFO_FIVE).equals("y"))
				m_oFrameAskTable.addTableDetailType(FrameTableButton.STATUS_CHECK_INFO_FIVE, "");
		}
	}
	
	// update open time, cover no, check total, svc member label of each table button
	public void updateTableDetails(String sAlphabet, String sOpenTime, String sGuest, String sCheckTotal, String sMemNo, String sMemName,String [] sCheckOwnerName, String sTableSize, LinkedHashMap<String, String> oCheckInfoList) {
		if(AppGlobal.g_oFuncStation.get().isAllowShowTableSize())
			m_oFrameAskTable.updateTableDetail(sAlphabet, FrameTableButton.STATUS_TABLE_SIZE, StringLib.createStringArray(5, sTableSize==null?"":"x"+sTableSize));
		JSONObject oSwitchCheckInfoSetting = AppGlobal.g_oFuncStation.get().getSwitchCheckInfoSetting();
		boolean bIsTurnOffSwitchInfo = this.isTurnOffAllSwitchCheckInfo(oSwitchCheckInfoSetting);
		
		if(bIsTurnOffSwitchInfo || (oSwitchCheckInfoSetting != null && oSwitchCheckInfoSetting.optString(FrameTableButton.STATUS_OPEN_TIME).equals("y")))
			m_oFrameAskTable.updateTableDetail(sAlphabet, FrameTableButton.STATUS_OPEN_TIME, StringLib.createStringArray(5, sOpenTime));
		if(bIsTurnOffSwitchInfo || (oSwitchCheckInfoSetting != null && oSwitchCheckInfoSetting.optString(FrameTableButton.STATUS_COVER_NO).equals("y")))
			m_oFrameAskTable.updateTableDetail(sAlphabet, FrameTableButton.STATUS_COVER_NO, StringLib.createStringArray(5, sGuest));
		if(bIsTurnOffSwitchInfo || (oSwitchCheckInfoSetting != null && oSwitchCheckInfoSetting.optString(FrameTableButton.STATUS_CHECK_TOTAL).equals("y")))
			m_oFrameAskTable.updateTableDetail(sAlphabet, FrameTableButton.STATUS_CHECK_TOTAL, StringLib.createStringArray(5, sCheckTotal));
		if(bIsTurnOffSwitchInfo || (oSwitchCheckInfoSetting != null && oSwitchCheckInfoSetting.optString(FrameTableButton.STATUS_MEMBER_NUMBER).equals("y")))
			m_oFrameAskTable.updateTableDetail(sAlphabet, FrameTableButton.STATUS_MEMBER_NUMBER, StringLib.createStringArray(5, sMemNo));
		if(bIsTurnOffSwitchInfo || (oSwitchCheckInfoSetting != null && oSwitchCheckInfoSetting.optString(FrameTableButton.STATUS_MEMBER_NAME).equals("y")))
			m_oFrameAskTable.updateTableDetail(sAlphabet, FrameTableButton.STATUS_MEMBER_NAME, StringLib.createStringArray(5, sMemName));
		if(bIsTurnOffSwitchInfo || (oSwitchCheckInfoSetting != null && oSwitchCheckInfoSetting.optString(FrameTableButton.STATUS_OWNER_NAME).equals("y")))
			m_oFrameAskTable.updateTableDetail(sAlphabet, FrameTableButton.STATUS_OWNER_NAME, sCheckOwnerName);
		
		for (Entry<String, String> entry : oCheckInfoList.entrySet()) {
			if(oSwitchCheckInfoSetting != null && oSwitchCheckInfoSetting.optString(entry.getKey()).equals("y"))
				m_oFrameAskTable.updateTableDetail(sAlphabet, entry.getKey(), StringLib.createStringArray(5, entry.getValue()));
		}
	}
	
	public void setTableDetailByIndex(int iIndex){
		m_oFrameAskTable.setTableDetailByIndex(iIndex);
	}
	
	public String getTableExtension() {
		return m_sTableExtension;
	}
	
	public Integer getTableNo(){
		return m_iTableNo;
	}
	
	public void setTitle(String sTitle){
		m_oFrameAskTable.setTitle(sTitle);
	}
	
	public void setMessage(String sMessage){
		m_oFrameAskTable.setMessage(sMessage);
	}

	public void setDefaultTableNo(String sTableNo) {
		m_oFrameAskTable.setDefaultTableNo(sTableNo);
	}

	public void setDefaultTableExtension(String sTableExtension) {
		m_oFrameAskTable.setDefaultTableExtension(sTableExtension);
	}
	
	public boolean isLongClickExtension(){
		return m_bLongClick;
	}
	
	public int getKeyboardMode(){
		return m_oFrameAskTable.getKeyboardMode();
	}
	
	public boolean isCanelClick(){
		return m_bCancelClick;
	}
	
	public boolean isTurnOffAllSwitchCheckInfo(JSONObject oSwitchCheckInfo) {
		if (oSwitchCheckInfo == null)
			return true;
		Iterator<String> keys = oSwitchCheckInfo.keys();
		while(keys.hasNext()){
			String sKey = keys.next();
			try {
				if(oSwitchCheckInfo.get(sKey).equals("y"))
					return false;
			} catch (JSONException e) {
				AppGlobal.stack2Log(e);
			}
		}
		return true;
	}
	
	@Override
	public void FrameAskTable_clickOK() {
		String sTableNo = m_oFrameAskTable.getTableNo();
		if(sTableNo.isEmpty()){
			String sErrMsg = AppGlobal.g_oLang.get()._("please_input_the_table_no");
			FormDialogBox oDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oDialogBox.setMessage(sErrMsg);
			oDialogBox.show();			
			return;
		}
		
		try{
			Integer.parseInt(sTableNo);
		}catch (NumberFormatException e) {
			String sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
			FormDialogBox oDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oDialogBox.setMessage(sErrMsg);
			oDialogBox.show();
			return;
		}
		
		if(Integer.parseInt(sTableNo) == 0 && m_oFrameAskTable.getTableExtension().isEmpty()){
			String sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
			FormDialogBox oDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oDialogBox.setMessage(sErrMsg);
			oDialogBox.show();
			return;
		}
		
		m_iTableNo = Integer.parseInt(sTableNo);
		m_sTableExtension = m_oFrameAskTable.getTableExtension().toUpperCase();
		
		// Finish showing this form
		this.finishShow();
		
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}
	
	@Override
	public void FrameAskTable_clickCancel() {
		m_bCancelClick = true;
		// Finish showing this form
		this.finishShow();
		
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}

	@Override
	public void FrameAskTable_longClicked(String sTableNo, String sTableExtension) {
		m_iTableNo = Integer.parseInt(sTableNo);
		m_sTableExtension = sTableExtension;
		
		m_bLongClick = true;
		
		// Finish showing this form
		this.finishShow();
		
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}
}
