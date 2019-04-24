package app;

import java.util.ArrayList;

import org.json.JSONArray;

import commonui.FormDialogBox;
import core.Controller;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

interface FormWarningMessageListListener {
	void formWarningMessageList_CheckListRecordClicked();
}

public class FormWarningMessageList extends VirtualUIForm implements FrameWarningMessageListListener  {
	private TemplateBuilder m_oTemplateBuilder;
	private FrameWarningMessageList m_oFrameWarningMessageList;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FormWarningMessageListListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FormWarningMessageListListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FormWarningMessageListListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FormWarningMessageList (Controller oParentController) {
		super(oParentController);

		// Load form from template file
		m_oTemplateBuilder = new TemplateBuilder();
		m_oTemplateBuilder.loadTemplate("frmWarningMessageList.xml");
		
		listeners = new ArrayList<FormWarningMessageListListener>();

		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);

		// WarningMessage Frame
		m_oFrameWarningMessageList = new FrameWarningMessageList();
		m_oTemplateBuilder.buildFrame(m_oFrameWarningMessageList, "fraWarningMessageList");

		m_oFrameWarningMessageList.addListener(this);
		this.attachChild(m_oFrameWarningMessageList);
	}
	
    public void init() {
    	m_oFrameWarningMessageList.setTitle(AppGlobal.g_oLang.get()._("warning_message_list"));
    	
    	//Printer Status List
    	m_oFrameWarningMessageList.addWarningMessageListingTab(FrameWarningMessageList.PANEL_PRINTER_STATUS, AppGlobal.g_oLang.get()._("printer_status"));
    	
		// Show the first page listing
    	m_oFrameWarningMessageList.changeSelectedListingTab(0);
	}
    
	public void clearWarningMessageListPanelFrame() {
		this.finishShow();
	}
	
	private void showDialogBox(String sTitle, String sMessage) {
		if (sMessage.isEmpty())
			return;
		
		FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
		oFormDialogBox.setTitle(sTitle);
		oFormDialogBox.setMessage(sMessage);
		oFormDialogBox.show();
	}
	
	//Call Back Function
	
	@Override
	public void frameWarningMessageList_ButtonClickRefresh(String sPanelType, int iWarningMessageListingType) {
		// Call API to renew the record based on the Listing Type
		/*JSONArray oWarningMessageListJSONArray = null;
		if (sPanelType.equals(FrameWarningMessageListPanel.TYPE_PRINTER_STATUS)) {
			try {
				oWarningMessageListJSONArray = new JSONArray("[{\"id\":1,\"printStatus\":{\"pdev_name\":\"Hot Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"o\"}}\r\n" + 
						",{\"id\":2,\"printStatus\":{\"pdev_name\":\"Cold Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"o\"}}\r\n" + 
						",{\"id\":3,\"printStatus\":{\"pdev_name\":\"Middle Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"o\"}}\r\n" + 
						",{\"id\":4,\"printStatus\":{\"pdev_name\":\"OMG Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"o\"}}\r\n" + 
						",{\"id\":5,\"printStatus\":{\"pdev_name\":\"HAL Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"o\"}}\r\n" + 
						",{\"id\":6,\"printStatus\":{\"pdev_name\":\"qwe Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"a\"}}\r\n" + 
						",{\"id\":7,\"printStatus\":{\"pdev_name\":\"asd Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"o\"}}\r\n" + 
						",{\"id\":8,\"printStatus\":{\"pdev_name\":\"cxv Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"o\"}}\r\n" + 
						",{\"id\":9,\"printStatus\":{\"pdev_name\":\"hgj Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"o\"}}\r\n" + 
						",{\"id\":10,\"printStatus\":{\"pdev_name\":\"fdg Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"f\"}}\r\n" + 
						",{\"id\":11,\"printStatus\":{\"pdev_name\":\"sdfsfdsdf Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"o\"}}\r\n" + 
						",{\"id\":12,\"printStatus\":{\"pdev_name\":\"dff Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"f\"}}\r\n" + 
						",{\"id\":13,\"printStatus\":{\"pdev_name\":\"uioy Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"a\"}}\r\n" + 
						",{\"id\":14,\"printStatus\":{\"pdev_name\":\"qwe Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"o\"}}\r\n" + 
						",{\"id\":15,\"printStatus\":{\"pdev_name\":\"yuhj Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"f\"}}\r\n" + 
						",{\"id\":16,\"printStatus\":{\"pdev_name\":\"gfhbvb Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"o\"}}\r\n" + 
						",{\"id\":17,\"printStatus\":{\"pdev_name\":\"sgfd Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"o\"}}\r\n" + 
						",{\"id\":18,\"printStatus\":{\"pdev_name\":\"ewradvfx Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"o\"}}\r\n" + 
						",{\"id\":19,\"printStatus\":{\"pdev_name\":\"fdgfdgs Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"a\"}}\r\n" + 
						",{\"id\":20,\"printStatus\":{\"pdev_name\":\"cbxcvb Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"a\"}}\r\n" + 
						",{\"id\":21,\"printStatus\":{\"pdev_name\":\"bbrfgg Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"o\"}}\r\n" + 
						",{\"id\":22,\"printStatus\":{\"pdev_name\":\"dfgfdgb Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"f\"}}\r\n" + 
						",{\"id\":23,\"printStatus\":{\"pdev_name\":\"asdsad Kitchen\",\"pdev_health\":\"L\",\"pdev_status\":\"o\"}}]");
			} catch (Exception e) {
				AppGlobal.stackToString(e);
			}
			// init the Original List Base on the Result
			if (oWarningMessageListJSONArray == null) {
				// Show Error Message
				this.showDialogBox(AppGlobal.g_oLang.get()._("error"), AppGlobal.g_oLang.get()._("printer_status_acquire_fail"));
				
				// Reset the display list by call the initWarningList with empty JSONArray
				oWarningMessageListJSONArray = new JSONArray();
			}
		}
		
		m_oFrameWarningMessageList.addRecordToFrameCheckListPanel(iWarningMessageListingType, oWarningMessageListJSONArray);*/
	}
	
	@Override
	public void frameWarningMessageList_ButtonExitClicked() {
		clearWarningMessageListPanelFrame();
	}
}
