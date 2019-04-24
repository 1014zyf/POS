package app.commonui;

import core.Controller;
import app.controller.AppGlobal;
import core.templatebuilder.TemplateBuilder;
import core.virtualui.*;

public class FormAskTable extends Controller implements FrameAskTableListener {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameAskTable m_oFrameAskTable;
	private Integer m_iTableNo;
	private String m_sTableExtension;
	private boolean m_bLongClick;
	
	public FormAskTable(Controller oParentController){
		super(oParentController);
		
		m_iTableNo = 0;
		m_sTableExtension = "";
		m_bLongClick = false;
	}
	
	public boolean init(int iTableNo, boolean bNeedDefaultExtension) {
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmAskTable.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameAskTable = new FrameAskTable();
		m_oTemplateBuilder.buildFrame(m_oFrameAskTable, "fraAskTable");
		m_oFrameAskTable.init(iTableNo, bNeedDefaultExtension);
		// Add listener;
		m_oFrameAskTable.addListener(this);
		m_oFrameAskTable.setVisible(true);		// Show this frame during first initial
		this.attachChild(m_oFrameAskTable);
		
		if(m_oFrameAskTable.isShowTableNameList())
			m_oFrameAskTable.setLeft((oCoverFrame.getWidth()-m_oFrameAskTable.getWidth()) / 2);
		
		return true;
	}
	
	public void addTableExtension(String alphabet, String sStatus, boolean bLocked, boolean bSelect, boolean bAllowLongClick){
		m_oFrameAskTable.addTableExtension(alphabet, sStatus, bLocked, bSelect, bAllowLongClick);
	}
	
	public void addTableDetailType(String sKey, String sIconURL){
		m_oFrameAskTable.addTableDetailType(sKey, sIconURL);
	}
	
	public void updateTableDetail(String alphabet, String sKey, String sDetail){
		m_oFrameAskTable.updateTableDetail(alphabet, sKey, sDetail);
	}
	
	public void setTableDetailByIndex(int iIndex){
		m_oFrameAskTable.setTableDetailByIndex(iIndex);
	}
	
	public void showTableDetail(){
		m_oFrameAskTable.showTableDetail();
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
	
	@Override
	public void FrameAskTable_clickOK() {
		
		String sTableNo = m_oFrameAskTable.getTableNo();
		if(sTableNo.isEmpty()){
			String sErrMsg = AppGlobal.g_oLang.get()._("please_input_the_table_no");
			FormDialogBox oDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oDialogBox.setMessage(sErrMsg);
			oDialogBox.showAndWait();
			
			return;
		}
		
		try{
			Integer.parseInt(sTableNo);
		}catch (NumberFormatException e) {
			String sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
			FormDialogBox oDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oDialogBox.setMessage(sErrMsg);
			oDialogBox.showAndWait();
			
			return;
		}
		
		if(Integer.parseInt(sTableNo) == 0){
			String sErrMsg = AppGlobal.g_oLang.get()._("cannot_input_zero_table_no");
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
            oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
            oFormDialogBox.setMessage(sErrMsg);
            oFormDialogBox.showAndWait();
            
            return ;
		}
		
		m_iTableNo = Integer.parseInt(sTableNo);
		m_sTableExtension = m_oFrameAskTable.getTableExtension();
	
		// Finish showing this form
		this.finishShow();
		
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}
	
	@Override
	public void FrameAskTable_clickCancel() {
		
		m_iTableNo = 0;
		m_sTableExtension = "";
		
		// Finish showing this form
		this.finishShow();
		
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}

	@Override
	public void FrameAskTable_longClicked(String sTableNo,
			String sTableExtension) {
		
		m_iTableNo = Integer.parseInt(sTableNo);
		m_sTableExtension = sTableExtension;
		
		m_bLongClick = true;
		
		// Finish showing this form
		this.finishShow();
		
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}
}
