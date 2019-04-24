package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

import om.PosActionLog;
import om.PosCheck;
import om.PosCheckExtraInfo;
import om.PosCheckExtraInfoList;
import om.PosTableMessage;
import om.PosTableMessageList;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;
import commonui.FormSelectionBox;
import core.Controller;
import externallib.StringLib;

public class FormTableMsgReminder extends VirtualUIForm implements FrameTableMsgReminderListener, FrameCommonBasketListener {
	TemplateBuilder m_oTemplateBuilder;
	
	//UI element
	private FrameTableMsgReminder m_oFrameTableMsgReminder;
	private FrameCommonBasket m_oFrameCheckList;
	private VirtualUIFrame m_oFrameShadeMessage;
	private FrameCommonBasket m_oFrameMessageSelection;
	private VirtualUIFrame m_oFrameShadePrompt;
	private FrameCommonBasket m_oFramePromptOption;
	
	//Internal variables
	private ConcurrentSkipListMap<String, PosCheck> m_oTableCheckList;			//Table number and status
	private TreeMap<Integer, PosTableMessage> m_oTableMessageSet;				//Message Id and PosTableMessage
	private ArrayList<PosCheckExtraInfo> m_oCheckExtraInfoWithMsgList;			//PosCheckExtraInfo of Checks with Table Message
	private HashMap<String, PosCheck> m_oSelectedTableList;						//Table number and status with tick
	private ArrayList<Integer> m_oSelectedMessageId;							//Message Id with tick
	private ArrayList<String> m_oSelectedPromptOption; 							//Prompt string with tick
	private ArrayList<PosCheckExtraInfo> m_oCheckExtraInfoToRemove;
	
	//Prompt Type
	private static String PROMPT_NONE = "";
	private static String PROMPT_OPEN_TABLE = "o";
	private static String PROMPT_PRINT_CHECK = "p";
	private static String PROMPT_ALL = "x";
	
	/*** Constructor ****/
	//Constructor
	protected FormTableMsgReminder(ConcurrentSkipListMap<String, ClsTableStatus> oTableStatus, Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmTableMsgReminder.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Load form from template file
		m_oFrameTableMsgReminder = new FrameTableMsgReminder();
		m_oTemplateBuilder.buildFrame(m_oFrameTableMsgReminder, "fraTableMsgReminder");
		// Add listener
		m_oFrameTableMsgReminder.addListener(this);
		this.attachChild(m_oFrameTableMsgReminder);
		m_oFrameTableMsgReminder.m_oBtnSave.setVisible(false);
		
		VirtualUIFrame oFrameCheckListCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameCheckListCover, "fraCheckListCover");
		m_oFrameTableMsgReminder.attachChild(oFrameCheckListCover);
		
		VirtualUIFrame oFrameMessageOptionCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameMessageOptionCover, "fraMessageOptionCover");
		m_oFrameTableMsgReminder.attachChild(oFrameMessageOptionCover);
		
		VirtualUIFrame oFramePromptOptionCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFramePromptOptionCover, "fraPromptOptionCover");
		m_oFrameTableMsgReminder.attachChild(oFramePromptOptionCover);
		
		//Opened check list
		m_oFrameCheckList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFrameCheckList, "fraCheckList");
		m_oFrameCheckList.init();
		m_oFrameCheckList.addListener(this);
		oFrameCheckListCover.attachChild(m_oFrameCheckList);
		//Add Header "Table Num" and "Check Num"
		ArrayList<Integer> iHeaderWidthArray = new ArrayList<Integer>();
		ArrayList<String> sHeaderValueArray = new ArrayList<String>();
//KingsleyKwan20171016ByKing	-----Start-----
		iHeaderWidthArray.add(100);
		sHeaderValueArray.add(AppGlobal.g_oLang.get()._("select"));
		iHeaderWidthArray.add(166);
		sHeaderValueArray.add(AppGlobal.g_oLang.get()._("table_no"));
		iHeaderWidthArray.add(166);
		sHeaderValueArray.add(AppGlobal.g_oLang.get()._("check_no"));
		m_oFrameCheckList.addHeader(iHeaderWidthArray, sHeaderValueArray);
//KingsleyKwan20171016ByKing	----- End -----
		
		//Messages selection box
		m_oFrameMessageSelection = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFrameMessageSelection, "fraMessageOption");
		m_oFrameMessageSelection.init();
		m_oFrameMessageSelection.addListener(this);
		oFrameMessageOptionCover.attachChild(m_oFrameMessageSelection);
		//Add Header "Table Message"
		iHeaderWidthArray = new ArrayList<Integer>();
		sHeaderValueArray = new ArrayList<String>();
//KingsleyKwan20171016ByKing	-----Start-----
		iHeaderWidthArray.add(380);
//KingsleyKwan20171016ByKing	----- End -----
		sHeaderValueArray.add(AppGlobal.g_oLang.get()._("table_message"));
		m_oFrameMessageSelection.addHeader(iHeaderWidthArray, sHeaderValueArray);
		//Add shade
		m_oFrameShadeMessage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameShadeMessage, "fraShadeMessage");
		m_oFrameShadeMessage.setVisible(true);
		m_oFrameTableMsgReminder.attachChild(m_oFrameShadeMessage);
		m_oFrameShadeMessage.bringToTop();
		
		//Prompt option list
		m_oFramePromptOption = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFramePromptOption, "fraPromptOption");
		m_oFramePromptOption.init();
		m_oFramePromptOption.addListener(this);
		oFramePromptOptionCover.attachChild(m_oFramePromptOption);
		//Add Header "Show In"
		iHeaderWidthArray = new ArrayList<Integer>();
		sHeaderValueArray = new ArrayList<String>();
//KingsleyKwan20171016ByKing	-----Start-----
		iHeaderWidthArray.add(328);
//KingsleyKwan20171016ByKing	----- End -----
		sHeaderValueArray.add(AppGlobal.g_oLang.get()._("show_in"));
		m_oFramePromptOption.addHeader(iHeaderWidthArray, sHeaderValueArray);
		//Add shade
		m_oFrameShadePrompt = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameShadePrompt, "fraShadePrompt");
		m_oFrameShadePrompt.setVisible(true);
		m_oFrameTableMsgReminder.attachChild(m_oFrameShadePrompt);
		m_oFrameShadePrompt.bringToTop();
		
		this.m_oTableCheckList = new ConcurrentSkipListMap<String, PosCheck>();
		this.m_oTableMessageSet = new TreeMap<Integer, PosTableMessage>();
		this.m_oCheckExtraInfoWithMsgList = new ArrayList<PosCheckExtraInfo>();
		this.m_oSelectedTableList = new HashMap<String, PosCheck>();
		this.m_oSelectedMessageId = new ArrayList<Integer>();
		this.m_oSelectedPromptOption = new ArrayList<String>();
		this.m_oCheckExtraInfoToRemove = new ArrayList<PosCheckExtraInfo>();
		
		PosCheck oPosCheck = new PosCheck();
		JSONArray oCheckListJSONArray = oPosCheck.getCheckListByBusinessDayPaid(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), 0, PosCheck.PAID_NOT_PAID, false);
		if (oCheckListJSONArray != null && oCheckListJSONArray.length() > 0) {
			for (int j = 0; j < oCheckListJSONArray.length(); j++) {
				if (oCheckListJSONArray.isNull(j))
					continue;
				
				PosCheck oCheck = new PosCheck(oCheckListJSONArray.optJSONObject(j));
				String sKey = oCheck.getTable() + "_" + oCheck.getTableExtension();
				m_oTableCheckList.put(sKey, oCheck);
			}
		}
		
		this.formWillAppear();
	}
	
	/**** Internal functions ****/
	//Preparation before the frame is gonna show,
	//should be called immediately after constructor
	protected void formWillAppear() {
		//Implement the m_oFrameCheckList
		this.getAllCheckWithTableMessage();
		this.addCheckListRecords();
		
		//Implement the m_oFrameMessageSelection
		this.addMessageOptions();
		
		//Implement the m_oFramePromptOption
		this.addPromptOptions();
	}
	
	protected void getAllCheckWithTableMessage() {
		ArrayList<String> oCheckIds = new ArrayList<String>();
		for(Iterator<Map.Entry<String, PosCheck>> it = m_oTableCheckList.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, PosCheck> entry = it.next();
			PosCheck oPosCheck = entry.getValue();
			if(oPosCheck == null) {
				it.remove();
				continue;
			}
			
			oCheckIds.add(oPosCheck.getCheckId());
		}
		
		PosCheckExtraInfoList oPosCheckExtraInfoList = new PosCheckExtraInfoList();
		oPosCheckExtraInfoList.readAllByCheckIds(PosCheckExtraInfo.BY_CHECK, oCheckIds, PosCheckExtraInfo.STATUS_ACTIVE);
		this.m_oCheckExtraInfoWithMsgList.clear();
		this.m_oCheckExtraInfoWithMsgList = new ArrayList<PosCheckExtraInfo>(oPosCheckExtraInfoList.getCheckExtraInfoList());
		
	}
	
	protected boolean checkCheckHasMessage(String sCheckId) {
		for(PosCheckExtraInfo oPosCheckExtraInfo : m_oCheckExtraInfoWithMsgList) {
			if(oPosCheckExtraInfo.getChksId().equals(sCheckId) && oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_TABLE_MESSAGE))
				return true;
		}
		
		return false;
	}
	
	//create data source for m_oFrameCheckList
	protected void addCheckListRecords() {
		//Add section first
	m_oFrameCheckList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		int index = 0;
		
		for(Iterator<Map.Entry<String, PosCheck>> it = m_oTableCheckList.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, PosCheck> entry = it.next();
			PosCheck oPosCheck = entry.getValue();
			if(oPosCheck == null) {
				it.remove();
				continue;
			}
			
			ArrayList<Integer> iFieldWidthArray = new ArrayList<Integer>();
			ArrayList<String> sFieldValueArray = new ArrayList<String>();
			ArrayList<String> sFieldAlignArray = new ArrayList<String>();
			ArrayList<String> sFieldTypeArray = new ArrayList<String>();
			
			//Create the cell field values
			iFieldWidthArray.add(50);
			sFieldValueArray.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/checkbox_n.png");
			sFieldAlignArray.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.CENTER_HORIZONTAL);
			sFieldTypeArray.add(HeroActionProtocol.View.Type.IMAGE);
			
			iFieldWidthArray.add(50);
			if(checkCheckHasMessage(oPosCheck.getCheckId()))
				sFieldValueArray.add("M");
			else
				sFieldValueArray.add("");
			sFieldAlignArray.add("");
			sFieldTypeArray.add(HeroActionProtocol.View.Type.LABEL);
			//*****************************//
			iFieldWidthArray.add(166);
			sFieldValueArray.add(AppGlobal.g_oFuncOutlet.get().getTableNameWithTableNo(Integer.toString(oPosCheck.getTable()), oPosCheck.getTableExtension())[AppGlobal.g_oCurrentLangIndex.get()-1]);
			sFieldAlignArray.add("");
			sFieldTypeArray.add(HeroActionProtocol.View.Type.LABEL);
			iFieldWidthArray.add(166);
			sFieldValueArray.add(oPosCheck.getCheckPrefixNo());
			sFieldAlignArray.add("");
			sFieldTypeArray.add(HeroActionProtocol.View.Type.LABEL);
			
			m_oFrameCheckList.addItem(0, index, 0, iFieldWidthArray, sFieldValueArray, sFieldAlignArray, sFieldTypeArray, null);
			m_oFrameCheckList.setBottomUnderlineVisible(false);
			index++;
		}
	}
	
	//Create Message option frame
	protected void addMessageOptions() {
		//Add Section first
		m_oFrameMessageSelection.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		PosTableMessageList oPosTableMsgList = new PosTableMessageList();
		oPosTableMsgList.readAll();
		this.m_oTableMessageSet.clear();
		this.m_oTableMessageSet = new TreeMap<Integer, PosTableMessage>(oPosTableMsgList.getPosTableMessageList());
		
		int index = 0;
		for(Map.Entry<Integer, PosTableMessage> entry : m_oTableMessageSet.entrySet()) {
			PosTableMessage oPosTableMsg = entry.getValue();
			
			ArrayList<Integer> iFieldWidthArray = new ArrayList<Integer>();
			ArrayList<String> sFieldValueArray = new ArrayList<String>();
			ArrayList<String> sFieldAlignArray = new ArrayList<String>();
			ArrayList<String> sFieldTypeArray = new ArrayList<String>();
			
			//Create the cell field values
//KingsleyKwan20171016ByKing	-----Start-----
			iFieldWidthArray.add(380);
//KingsleyKwan20171016ByKing	----- End -----
			sFieldValueArray.add(oPosTableMsg.getName(AppGlobal.g_oCurrentLangIndex.get()));
			sFieldAlignArray.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
			sFieldTypeArray.add(HeroActionProtocol.View.Type.LABEL);
			
			m_oFrameMessageSelection.addItem(0, index, 0, iFieldWidthArray, sFieldValueArray, sFieldAlignArray, sFieldTypeArray, null);
//RayHuen20171024   ----- start Delete the underline-----			
			m_oFrameMessageSelection.setBottomUnderlineVisible(false);
//RayHuen20171024   ----- End Delete the underline-----	
			index++;
		}
	}
	
	//Create Prompt Option record
	protected void addPromptOptions() {
		//Add section first
		m_oFramePromptOption.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		//Add "Open table" and "Print check" option
		ArrayList<Integer> iFieldWidthArray = new ArrayList<Integer>();
		ArrayList<String> sFieldValueArray = new ArrayList<String>();
		ArrayList<String> sFieldAlignArray = new ArrayList<String>();
		ArrayList<String> sFieldTypeArray = new ArrayList<String>();
		iFieldWidthArray.add(34);
		sFieldValueArray.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/checkbox_n.png");
		sFieldAlignArray.add(HeroActionProtocol.View.Attribute.ImageAlign.CENTER_VERTICAL);
		sFieldTypeArray.add(HeroActionProtocol.View.Type.IMAGE);
		iFieldWidthArray.add(294);
		sFieldValueArray.add(AppGlobal.g_oLang.get()._("open_check"));
		sFieldAlignArray.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
		sFieldTypeArray.add(HeroActionProtocol.View.Type.LABEL);
		
		m_oFramePromptOption.addItem(0, 0, 0, iFieldWidthArray, sFieldValueArray, sFieldAlignArray, sFieldTypeArray, null);
		
		//"Print Check"
		iFieldWidthArray = new ArrayList<Integer>();
		sFieldValueArray = new ArrayList<String>();
		sFieldAlignArray = new ArrayList<String>();
		sFieldTypeArray = new ArrayList<String>();
		iFieldWidthArray.add(34);
		sFieldValueArray.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/checkbox_n.png");
		sFieldAlignArray.add(HeroActionProtocol.View.Attribute.ImageAlign.CENTER_VERTICAL);
		sFieldTypeArray.add(HeroActionProtocol.View.Type.IMAGE);
		iFieldWidthArray.add(294);
		sFieldValueArray.add(AppGlobal.g_oLang.get()._("print_check"));
		sFieldAlignArray.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
		sFieldTypeArray.add(HeroActionProtocol.View.Type.LABEL);
		m_oFramePromptOption.addItem(0, 1, 0, iFieldWidthArray, sFieldValueArray, sFieldAlignArray, sFieldTypeArray, null);
		m_oFramePromptOption.setBottomUnderlineVisible(false);
	}
	
	protected TreeMap<Integer, String> getAllAddedMessageStringsByCheckId(String sCheckId) {
		TreeMap<Integer, String> oReturnStrings = new TreeMap<Integer, String>();
		for(PosCheckExtraInfo oPosCheckExtraInfo : m_oCheckExtraInfoWithMsgList) {
			String sChkId = oPosCheckExtraInfo.getChksId();
			if(!sChkId.equals(sCheckId))
				continue;
			
			if(!oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_TABLE_MESSAGE))
				continue;
			
			try {
				JSONObject oJSONValues = new JSONObject(oPosCheckExtraInfo.getValue());
				int iMessageId = Integer.parseInt(oJSONValues.get("message_id").toString());
				if(!m_oTableMessageSet.containsKey(iMessageId))
					continue;
				
				String sPromptString = "";
				String sPromptType = oJSONValues.getString("prompt_type");
				if(sPromptType.equals(PROMPT_ALL)) {
					sPromptString = AppGlobal.g_oLang.get()._("open_check")+","+AppGlobal.g_oLang.get()._("print_check");
				}
				else if(sPromptType.equals(PROMPT_OPEN_TABLE)) {
					sPromptString = AppGlobal.g_oLang.get()._("open_check");
				}
				else if(sPromptType.equals(PROMPT_PRINT_CHECK)) {
					sPromptString = AppGlobal.g_oLang.get()._("print_check");
				}
				
				if(sPromptString.isEmpty())
					continue;
				
				PosTableMessage oPosTableMessage = m_oTableMessageSet.get(iMessageId);
				oReturnStrings.put(oPosTableMessage.getTblmId(), oPosTableMessage.getName(AppGlobal.g_oCurrentLangIndex.get())+" ("+sPromptString+")");
			}
			catch(JSONException jsone) {
				jsone.printStackTrace();
				continue;
			}
		}
		
		return oReturnStrings;
	}
	
	/**** All Listener ****/
	@Override
	public void frameTableMsgReminder_clickCancel() {
		//Close button clicked
		this.m_oTableCheckList.clear();
		this.m_oTableMessageSet.clear();
		this.m_oCheckExtraInfoWithMsgList.clear();
		this.m_oSelectedTableList.clear();
		this.m_oSelectedMessageId.clear();
		this.m_oSelectedPromptOption.clear();
		this.m_oCheckExtraInfoToRemove.clear();
		this.finishShow();
	}
	
	@Override
	public void frameTableMsgReminder_clickSave() {
		//Save button clicked
		if(m_oSelectedTableList.isEmpty() || m_oSelectedMessageId.isEmpty() || m_oSelectedPromptOption.isEmpty()) {
			//Pop alert message
			this.finishShow();
		}
		
		//Save record to database
		ArrayList<String> oSelectedCheckIds = new ArrayList<String>();
		for(Map.Entry<String, PosCheck> entry : m_oSelectedTableList.entrySet()) {
			PosCheck oPosCheck = entry.getValue();
			oSelectedCheckIds.add(oPosCheck.getCheckId());
		}

		PosCheckExtraInfoList oPosCheckExtraInfoList = new PosCheckExtraInfoList();
		ArrayList<PosCheckExtraInfo> oPosCheckExtraInfos = new ArrayList<PosCheckExtraInfo>();
		for(Map.Entry<String, PosCheck> entry : m_oSelectedTableList.entrySet()) {
			PosCheck oPosCheck = entry.getValue();
			String sCheckId = oPosCheck.getCheckId();
			String sTableNum = entry.getKey();
			
			for(int iMessageId : m_oSelectedMessageId) {
				String sPromptType = PROMPT_NONE;
				if(m_oSelectedPromptOption.size() == 1) {
					if(m_oSelectedPromptOption.contains(PROMPT_OPEN_TABLE)) {
						sPromptType = PROMPT_OPEN_TABLE;
					}
					else {
						sPromptType = PROMPT_PRINT_CHECK;
					}
				}
				else {
					sPromptType = PROMPT_ALL;
				}
				
				//Construct "ckei_value"
				JSONObject oJSONValues = new JSONObject();
				try {
					oJSONValues.put("create_by_user_id", AppGlobal.g_oFuncUser.get().getUserId());
					oJSONValues.put("create_time", AppGlobal.getCurrentTime(false));
					oJSONValues.put("prompt_type", sPromptType);
					oJSONValues.put("message_id", iMessageId);
					oJSONValues.put("table_num", sTableNum);
					oJSONValues.put("check_ids", oSelectedCheckIds);
				}
				catch(JSONException jsone) {
					jsone.printStackTrace();
				}
				
				PosCheckExtraInfo oPosCheckExtraInfo = new PosCheckExtraInfo();
				oPosCheckExtraInfo.setOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
				oPosCheckExtraInfo.setBy(PosCheckExtraInfo.BY_CHECK);
				oPosCheckExtraInfo.setCheckId(sCheckId);
				oPosCheckExtraInfo.setSection("");
				oPosCheckExtraInfo.setVariable(PosCheckExtraInfo.VARIABLE_TABLE_MESSAGE);
				oPosCheckExtraInfo.setIndex(0);
				oPosCheckExtraInfo.setValue(oJSONValues.toString());
				
				oPosCheckExtraInfos.add(oPosCheckExtraInfo);

				// Add log to action log list
				String sLogRemark = "Add Table Message: "+m_oTableMessageSet.get(iMessageId).getName(AppGlobal.g_oCurrentLangIndex.get());
				AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.table_message_reminder.name(), PosActionLog.ACTION_RESULT_SUCCESS, sTableNum.replace("_", ""), AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), oPosCheck.getCheckId(), "", "", "", "", sLogRemark);
			}
		}
		
		//Update records
		if(oPosCheckExtraInfoList.addUpdateWithMultipleRecords(oPosCheckExtraInfos)) {
			this.finishShow();
			//handle action log
			AppGlobal.g_oActionLog.get().handleActionLog(false);
		} else {
			//Save fail
		}
	}
	
	@Override
	public void frameTableMsgReminder_clickRemoveMessage() {
		//Remove button clicked
		if(m_oSelectedTableList.isEmpty())
			return;
		
		for(Map.Entry<String, PosCheck> entry : m_oSelectedTableList.entrySet()) {
			PosCheck oPosCheck = entry.getValue();
			String sTableNum = entry.getKey();
			if(!checkCheckHasMessage(oPosCheck.getCheckId()))
				continue;
			
			//<Message id, Message string>
			TreeMap<Integer, String> sTableMessages = this.getAllAddedMessageStringsByCheckId(oPosCheck.getCheckId());
			ArrayList<String> oOptionList = new ArrayList<String>();
			for(Map.Entry<Integer, String> tableMsgSet : sTableMessages.entrySet()) {
				oOptionList.add(tableMsgSet.getValue());
			}
			
			//Show the selection box of messages
			FormSelectionBox oFormMessageSelectionBox = new FormSelectionBox(this);
			oFormMessageSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("remove_message")+": "+AppGlobal.g_oLang.get()._("table")+" "+AppGlobal.g_oFuncOutlet.get().getTableName(Integer.toString(oPosCheck.getTable()), oPosCheck.getTableExtension())[AppGlobal.g_oCurrentLangIndex.get()-1], oOptionList, false);
			oFormMessageSelectionBox.show();
			if(oFormMessageSelectionBox.isUserCancel()) {
				continue;
			}
			else {
				ArrayList<Integer> oSelectionBoxResult = oFormMessageSelectionBox.getResultList();
				//Single selections
				int iResultIndex = oSelectionBoxResult.get(0);
				int i = 0;
				int iSelectedMsgId = -1;
				for(int iMessageId : sTableMessages.keySet()) {
					if(i == iResultIndex) {
						iSelectedMsgId = iMessageId;
						break;
					}
					i++;
				}
				
				if(iSelectedMsgId == -1)
					continue;
				
				for(PosCheckExtraInfo oPosCheckExtraInfo : m_oCheckExtraInfoWithMsgList) {
					if(!oPosCheckExtraInfo.getChksId().equals(oPosCheck.getCheckId()))
						continue;
					
					if(!oPosCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_TABLE_MESSAGE))
						continue;
					
					try {
						JSONObject oJSONValues = new JSONObject(oPosCheckExtraInfo.getValue());
						int iMessageId = Integer.parseInt(oJSONValues.get("message_id").toString());
						if(iMessageId != iSelectedMsgId)
							continue;
						else {
							oPosCheckExtraInfo.setStatus(PosCheckExtraInfo.STATUS_DELETED);
							m_oCheckExtraInfoToRemove.add(oPosCheckExtraInfo);
							// Add log to action log list
							String sLogRemark = "Remove Table Message: "+m_oTableMessageSet.get(iMessageId).getName(AppGlobal.g_oCurrentLangIndex.get());
							AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.table_message_reminder.name(), PosActionLog.ACTION_RESULT_SUCCESS, sTableNum.replace("_", ""), AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), oPosCheck.getCheckId(), "", "", "", "", sLogRemark);
						}
					}
					catch(JSONException jsone) {
						jsone.printStackTrace();
						continue;
					}
				}
				
				//Removal selection box for one table is shown
			}
			
		}
		
		if(!this.m_oCheckExtraInfoToRemove.isEmpty()) {
			//Update the db as deleted
			PosCheckExtraInfoList oPosCheckExtraInfoList = new PosCheckExtraInfoList();
			oPosCheckExtraInfoList.addUpdateWithMultipleRecords(m_oCheckExtraInfoToRemove);
			this.m_oCheckExtraInfoToRemove.clear();
			//Refresh the data and table view
			m_oFrameCheckList.removeAllSections();
			this.m_oSelectedTableList.clear();
			this.m_oFrameShadeMessage.setVisible(true);
			this.m_oFrameShadePrompt.setVisible(true);
			m_oFrameTableMsgReminder.m_oBtnSave.setVisible(false);
			this.getAllCheckWithTableMessage();
			this.addCheckListRecords();
			
			//handle action log
			AppGlobal.g_oActionLog.get().handleActionLog(false);
		}
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId,
			String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex,
			int iItemIndex, int iFieldIndex, String sNote) {
		if(iBasketId == m_oFrameCheckList.getId()) {
			//Opened Table selected
			if(iFieldIndex == 0) {		//Tick box
				int iCounter = 0;
				for(Map.Entry<String, PosCheck> entry:m_oTableCheckList.entrySet()) {
					if(iCounter == iItemIndex) {
						PosCheck oPosCheck = entry.getValue();
						if(oPosCheck == null) continue;
						String sTableNum = oPosCheck.getTable() + ((oPosCheck.getTableExtension().isEmpty()) ? "" : "_" + oPosCheck.getTableExtension());
//KingsleyKwan20171016ByKing	-----Start-----
						//Check the table is selected before or not
						if(m_oSelectedTableList.get(sTableNum) != null) {
							//the table is selected already
							m_oFrameCheckList.setFieldValue(iSectionIndex, iItemIndex, iFieldIndex, AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/checkbox_n.png");
							m_oSelectedTableList.remove(sTableNum);
						}
						else {
							//Not selected before
							m_oFrameCheckList.setFieldValue(iSectionIndex, iItemIndex, iFieldIndex, AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/checkbox_h.png");
							m_oSelectedTableList.put(sTableNum, oPosCheck);
						}
						break;
//KingsleyKwan20171016ByKing	----- End -----
					}
					iCounter++;
				}
			}
		}
		else if(iBasketId == m_oFrameMessageSelection.getId()) {
			//Table Message selected
			int iSelectedMsgId = -1;
			int i = 0;
			for (Integer id : m_oTableMessageSet.keySet()) {
				if(i == iItemIndex) {
					iSelectedMsgId = id;
					break;
				}
				i++;
			}
			
			if(iSelectedMsgId == -1)
				return;
			
			boolean bMatch = false;
			Iterator<Integer> iterator = m_oSelectedMessageId.iterator();
			while(iterator.hasNext()) {
				if(iSelectedMsgId == iterator.next()) {
					m_oFrameMessageSelection.setFieldBackgroundColor(iSectionIndex, iItemIndex, iFieldIndex, "#FFFFFF");
					iterator.remove();
					bMatch = true;
					break;
				}
			}
			if(!bMatch) {
//KingsleyKwan20171016ByKing	-----Start-----
				m_oFrameMessageSelection.setFieldBackgroundColor(iSectionIndex, iItemIndex, iFieldIndex, "#66A6F1");
//KingsleyKwan20171016ByKing	----- End -----
				m_oSelectedMessageId.add(iSelectedMsgId);
			}
		}
		else if(iBasketId == m_oFramePromptOption.getId()) {
			//Prompt Option selected
			String sOption;
			if(iItemIndex == 0) {
				//"Open Table"
				sOption = PROMPT_OPEN_TABLE;
			}
			else if(iItemIndex == 1){
				//"Print Check"
				sOption = PROMPT_PRINT_CHECK;
			}
			else {
				return;
			}
//KingsleyKwan20171016ByKing	-----Start-----
			boolean bMatch = false;
			Iterator<String> iterator = m_oSelectedPromptOption.iterator();
			while(iterator.hasNext()) {
				if(iterator.next().equals(sOption)) {
					m_oFramePromptOption.setFieldValue(iSectionIndex, iItemIndex, 0, AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/checkbox_n.png");
					iterator.remove();
					bMatch = true;
					break;
				}
			}
			
			if(!bMatch) {
			//Not yet select, set selected
				m_oFramePromptOption.setFieldValue(iSectionIndex, iItemIndex, 0, AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/checkbox_h.png");
				m_oSelectedPromptOption.add(sOption);
			}
//KingsleyKwan20171016ByKing	----- End -----
		}
		
		//Update UI
		//Update shades
		if(!m_oSelectedTableList.isEmpty()) {
			m_oFrameShadeMessage.setVisible(false);
			if(!m_oSelectedMessageId.isEmpty()) {
				m_oFrameShadePrompt.setVisible(false);
			}
			else {
				m_oFrameShadePrompt.setVisible(true);
			}
		}
		else {
			m_oFrameShadeMessage.setVisible(true);
			m_oFrameShadePrompt.setVisible(true);
		}
		
		//Show "Save" button
		if(!m_oSelectedTableList.isEmpty() && !m_oSelectedMessageId.isEmpty() && !m_oSelectedPromptOption.isEmpty()) {
			m_oFrameTableMsgReminder.m_oBtnSave.setVisible(true);
		}
		else {
			m_oFrameTableMsgReminder.m_oBtnSave.setVisible(false);
		}
		
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId,
			int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		
	}
	
}