package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import commonui.FormSelectionBox;
//JackHO20171108
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
//end JackHO20171108
import externallib.StringLib;
import om.PosTaiwanGuiConfig;
import om.PosTaiwanGuiConfigList;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FrameEditTaiwanGUIConfigListener {
    void frameEditTaiwanGUIConfig_clickExit();
}

public class FrameEditTaiwanGUIConfig extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private List<PosTaiwanGuiConfig> oPosTaiwanGuiConfigs;
	
	private VirtualUILabel m_oAddConfigLabelTitle;
	private FrameTitleHeader m_oTitleHeader;
	private FrameTitleHeader m_oAddConfigTitleHeader;
	private FrameCommonBasket m_oFrameItemList;
	private VirtualUIFrame m_oFrameAddConfig;
	
	private VirtualUIFrame m_oFrameLeftContent;
	private VirtualUIFrame m_oFrameRightContent;
	private FrameHorizontalTabList m_oFrameHorizontalTabList;
	
	private VirtualUIButton m_oButtonAddTaiwanGUIConfig;
	private VirtualUILabel m_oLblType;
	private VirtualUILabel m_oLblStartYear;
	private VirtualUILabel m_oLblStartMonth;
	private VirtualUILabel m_oLblStartDay;
	private VirtualUILabel m_oLblEndYear;
	private VirtualUILabel m_oLblEndMonth;
	private VirtualUILabel m_oLblEndDay;
	private VirtualUILabel m_oLblDateFormat;
	private VirtualUILabel m_oLblPrefix;
	private VirtualUILabel m_oLblStartNumber;
	private VirtualUILabel m_oLblEndNumber;
	private VirtualUILabel m_oLblWarning;
	private VirtualUILabel m_oLblStatus;
	private VirtualUILabel m_oLblTypeInput;
	private VirtualUILabel m_oLblStatusInput;
	
	private VirtualUITextbox m_oTextboxStartYear;
	private VirtualUITextbox m_oTextboxStartMonth;
	private VirtualUITextbox m_oTextboxStartDay;
	private VirtualUITextbox m_oTextboxEndYear;
	private VirtualUITextbox m_oTextboxEndMonth;
	private VirtualUITextbox m_oTextboxEndDay;
	private VirtualUITextbox m_oTextboxPrefix;
	private VirtualUITextbox m_oTextboxStartNumber;
	private VirtualUITextbox m_oTextboxEndNumber;
	private VirtualUITextbox m_oTextboxWarning;
	
	private List<VirtualUIFrame> m_oFramePanelTab;

	private VirtualUIButton m_oButtonConfirmAddTaiwanGUI;
	private VirtualUIButton m_oButtonCancelAddTaiwanGUI;
	
	private String m_sTypeInputValue;
	private String m_sStatusInputValue;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameEditTaiwanGUIConfigListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameEditTaiwanGUIConfigListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameEditTaiwanGUIConfigListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
	public FrameEditTaiwanGUIConfig(){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameEditTaiwanGUIConfigListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraEditTaiwanGUIConfig.xml");

		oPosTaiwanGuiConfigs = new ArrayList<PosTaiwanGuiConfig>();

		// Title Header
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		m_oTitleHeader.addListener(this);
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("edit_taiwan_gui_config"));
		this.attachChild(m_oTitleHeader);
		
		// Add Config
		m_oFrameAddConfig = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameAddConfig, "fraAddConfig");
		m_oFrameAddConfig.setVisible(false);
		
		// Add Config Title Header
		m_oAddConfigTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oAddConfigTitleHeader, "fraAddConfigTitleHeader");
		m_oAddConfigTitleHeader.init(false);
		m_oAddConfigTitleHeader.setTitle(AppGlobal.g_oLang.get()._("add_taiwan_gui_config"));
		m_oFrameAddConfig.attachChild(m_oAddConfigTitleHeader);
		
		m_oTextboxStartYear = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxStartYear, "txtboxStartYear");
		m_oFrameAddConfig.attachChild(m_oTextboxStartYear);

		m_oTextboxStartMonth = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxStartMonth, "txtboxStartMonth");
		m_oFrameAddConfig.attachChild(m_oTextboxStartMonth);
		
		m_oTextboxStartDay = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxStartDay, "txtboxStartDay");
		m_oFrameAddConfig.attachChild(m_oTextboxStartDay);
		
		m_oTextboxEndYear = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxEndYear, "txtboxEndYear");
		m_oFrameAddConfig.attachChild(m_oTextboxEndYear);
		
		m_oTextboxEndMonth = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxEndMonth, "txtboxEndMonth");
		m_oFrameAddConfig.attachChild(m_oTextboxEndMonth);
		
		m_oTextboxEndDay = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxEndDay, "txtboxEndDay");
		m_oFrameAddConfig.attachChild(m_oTextboxEndDay);
		
		m_oTextboxPrefix = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxPrefix, "txtboxPrefix");
		m_oFrameAddConfig.attachChild(m_oTextboxPrefix);
		
		m_oTextboxStartNumber = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxStartNumber, "txtboxStartNumber");
		m_oFrameAddConfig.attachChild(m_oTextboxStartNumber);
		
		m_oTextboxEndNumber = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxEndNumber, "txtboxEndNumber");
		m_oFrameAddConfig.attachChild(m_oTextboxEndNumber);
		
		m_oTextboxWarning = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxWarning, "txtboxWarning");
		m_oFrameAddConfig.attachChild(m_oTextboxWarning);
		
		m_oLblTypeInput = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblTypeInput, "lblTypeInput");
		m_oLblTypeInput.allowClick(true);
		m_oFrameAddConfig.attachChild(m_oLblTypeInput);		

		m_oLblStatusInput = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblStatusInput, "lblStatusInput");
		m_oLblStatusInput.allowClick(true);
		m_oFrameAddConfig.attachChild(m_oLblStatusInput);
		
		m_oLblType = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLblType, "lblType");
    	m_oLblType.setValue(AppGlobal.g_oLang.get()._("type"));
    	m_oFrameAddConfig.attachChild(m_oLblType);

		m_oLblStartYear = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLblStartYear, "lblStartYear");
    	m_oLblStartYear.setValue(AppGlobal.g_oLang.get()._("start_date"));
    	m_oFrameAddConfig.attachChild(m_oLblStartYear);

		m_oLblStartMonth = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLblStartMonth, "lblStartMonth");
    	m_oLblStartMonth.setValue("-");
    	m_oFrameAddConfig.attachChild(m_oLblStartMonth);
    	
		m_oLblStartDay = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLblStartDay, "lblStartDay");
    	m_oLblStartDay.setValue("-");
    	m_oFrameAddConfig.attachChild(m_oLblStartDay);
    	
		m_oLblEndYear = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLblEndYear, "lblEndYear");
    	m_oLblEndYear.setValue(AppGlobal.g_oLang.get()._("end_date"));
    	m_oFrameAddConfig.attachChild(m_oLblEndYear);
    	
		m_oLblEndMonth = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLblEndMonth, "lblEndMonth");
    	m_oLblEndMonth.setValue("-");
    	m_oFrameAddConfig.attachChild(m_oLblEndMonth);
    	
		m_oLblEndDay = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLblEndDay, "lblEndDay");
    	m_oLblEndDay.setValue("-");
    	m_oFrameAddConfig.attachChild(m_oLblEndDay);
    	
		m_oLblDateFormat = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLblDateFormat, "lblDateFormat");
    	m_oLblDateFormat.setValue("("+AppGlobal.g_oLang.get()._("format")+": YYYY-MM-DD)");
    	m_oFrameAddConfig.attachChild(m_oLblDateFormat);
    	
		m_oLblPrefix = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLblPrefix, "lblPrefix");
    	m_oLblPrefix.setValue(AppGlobal.g_oLang.get()._("prefix"));
    	m_oFrameAddConfig.attachChild(m_oLblPrefix);
    	
		m_oLblStartNumber = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLblStartNumber, "lblStartNumber");
    	m_oLblStartNumber.setValue(AppGlobal.g_oLang.get()._("start_number"));
    	m_oFrameAddConfig.attachChild(m_oLblStartNumber);
    	
		m_oLblEndNumber = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLblEndNumber, "lblEndNumber");
    	m_oLblEndNumber.setValue(AppGlobal.g_oLang.get()._("end_number"));
    	m_oFrameAddConfig.attachChild(m_oLblEndNumber);
    	
		m_oLblWarning = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLblWarning, "lblWarning");
    	m_oLblWarning.setValue(AppGlobal.g_oLang.get()._("warning_limit"));
    	m_oFrameAddConfig.attachChild(m_oLblWarning);
		
		m_oLblStatus = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLblStatus, "lblStatus");
    	m_oLblStatus.setValue(AppGlobal.g_oLang.get()._("status"));
    	m_oFrameAddConfig.attachChild(m_oLblStatus);
		
    	// Confirm add taiwan GUI
		m_oButtonConfirmAddTaiwanGUI = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonConfirmAddTaiwanGUI, "btnConfirmAddTaiwanGUI");
		m_oButtonConfirmAddTaiwanGUI.setValue(AppGlobal.g_oLang.get()._("add"));
		m_oButtonConfirmAddTaiwanGUI.setVisible(true);
		m_oButtonConfirmAddTaiwanGUI.allowClick(true);
		m_oButtonConfirmAddTaiwanGUI.addClickServerRequestSubmitElement(m_oFrameAddConfig);
		m_oFrameAddConfig.attachChild(m_oButtonConfirmAddTaiwanGUI);

    	// Cancel add taiwan GUI
		m_oButtonCancelAddTaiwanGUI = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancelAddTaiwanGUI, "btnCancelAddTaiwanGUI");
		m_oButtonCancelAddTaiwanGUI.setValue(AppGlobal.g_oLang.get()._("cancel"));
		m_oButtonCancelAddTaiwanGUI.setVisible(true);
		m_oFrameAddConfig.attachChild(m_oButtonCancelAddTaiwanGUI);
		
		this.attachChild(m_oFrameAddConfig);
		
		//Create Left Content Frame
    	m_oFrameLeftContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftContent, "fraLeftContent");
		this.attachChild(m_oFrameLeftContent);
		
		m_oFramePanelTab = new ArrayList<VirtualUIFrame>();
		
		// Result list
		m_oFrameItemList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFrameItemList, "fraItemList");
		m_oFrameItemList.init();
		m_oFrameItemList.addListener(this);
		m_oFrameLeftContent.attachChild(m_oFrameItemList);
		
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
    	ArrayList<String> sFieldValues = new ArrayList<String>();
    	
    	iFieldWidths.add(100);
    	sFieldValues.add(AppGlobal.g_oLang.get()._("prefix"));
		iFieldWidths.add(250);
    	sFieldValues.add(AppGlobal.g_oLang.get()._("number_range"));
    	iFieldWidths.add(250);
    	sFieldValues.add(AppGlobal.g_oLang.get()._("date_range"));
    	iFieldWidths.add(250);
    	sFieldValues.add(AppGlobal.g_oLang.get()._("type"));
    	iFieldWidths.add(140);
    	sFieldValues.add(AppGlobal.g_oLang.get()._("status"));

    	m_oFrameItemList.addHeader(iFieldWidths, sFieldValues);
    	m_oFrameItemList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		//Create Right Content Frame
		m_oFrameRightContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameRightContent, "fraRightContent");
		this.attachChild(m_oFrameRightContent);
		m_oLblTypeInput.setValue(AppGlobal.g_oLang.get()._("normal"));
		m_sTypeInputValue = PosTaiwanGuiConfig.CONFIGURE_TYPE_NORMAL;
		m_oLblStatusInput.setValue(AppGlobal.g_oLang.get()._("active"));
		m_sStatusInputValue = PosTaiwanGuiConfig.STATUS_ACTIVE;
		
    	// Add add new configure button
		m_oButtonAddTaiwanGUIConfig = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonAddTaiwanGUIConfig, "btnAddTaiwanGUIConfig");
		m_oButtonAddTaiwanGUIConfig.setValue(AppGlobal.g_oLang.get()._("add_new_configure"));
		m_oButtonAddTaiwanGUIConfig.setVisible(true);
		m_oFrameRightContent.attachChild(m_oButtonAddTaiwanGUIConfig);
		
		m_oFrameHorizontalTabList = new FrameHorizontalTabList();
		m_oTemplateBuilder.buildFrame(m_oFrameHorizontalTabList, "horListTab");
		m_oFrameHorizontalTabList.init();
		m_oFrameLeftContent.attachChild(m_oFrameHorizontalTabList);
		
		List<String> oTabNameList = new ArrayList<String>();
		oTabNameList.add(AppGlobal.g_oLang.get()._("configure_list"));
		m_oFrameHorizontalTabList.addPageTabs(oTabNameList);	
		this.loadTaiwaiGuiConfigByStation();
	}
	
	public void setPosTaiwanGuiConfigs(List<PosTaiwanGuiConfig> list) {
		this.oPosTaiwanGuiConfigs = list;
	}

	public void addRecord(int iSectionId, int iItemIndex, String sPrefix, String sNumberRange, String sDateRange, String sType, String sStatus){
    	ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
    	ArrayList<String> sFieldValues = new ArrayList<String>();
    	ArrayList<String> sFieldAligns = new ArrayList<String>();
    	
    	iFieldWidths.add(100);
    	sFieldValues.add(sPrefix);
    	sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);

		iFieldWidths.add(250);
    	sFieldValues.add(sNumberRange);
    	sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
    	
    	iFieldWidths.add(250);
    	sFieldValues.add(sDateRange);
    	sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);

    	iFieldWidths.add(250);
    	sFieldValues.add(sType);
    	sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
    	
    	iFieldWidths.add(140);
    	sFieldValues.add(sStatus);
    	sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);

    	m_oFrameItemList.addItem(iSectionId, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
    }
	
	public void setResultLineVisible(ArrayList<Integer> oLineIndex, boolean bVisible){
		for(Integer iLineIndex:oLineIndex){
			m_oFrameItemList.setLineVisible(0, iLineIndex, bVisible);
		}
	}

	public void clearAllRecords() {
		m_oFrameItemList.removeAllItems(0);
	}
	
	public void hiddenItemList(boolean bHidden) {
		m_oFrameItemList.setVisible(!bHidden);
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;	

    	if (m_oButtonAddTaiwanGUIConfig.getId() == iChildId) {
			m_oFrameAddConfig.setVisible(true);
			m_oFrameAddConfig.bringToTop();
			bMatchChild = true;
        }
    	else if(iChildId == m_oButtonConfirmAddTaiwanGUI.getId()) {
			JSONObject requestJSONObject = new JSONObject();
			try {
				requestJSONObject.put("twcf_olet_id", 0);
				requestJSONObject.put("twcf_stat_id", AppGlobal.g_oFuncStation.get().getStationId());
				requestJSONObject.put("twcf_type", m_sTypeInputValue);
				requestJSONObject.put("twcf_prefix", m_oTextboxPrefix.getValue());
				requestJSONObject.put("twcf_start_num", m_oTextboxStartNumber.getValue());
				requestJSONObject.put("twcf_end_num", m_oTextboxEndNumber.getValue());
				requestJSONObject.put("twcf_start_date", m_oTextboxStartYear.getValue() + "-"+m_oTextboxStartMonth.getValue() + "-"+m_oTextboxStartDay.getValue());
				requestJSONObject.put("twcf_end_date", m_oTextboxEndYear.getValue() + "-"+m_oTextboxEndMonth.getValue() + "-"+m_oTextboxEndDay.getValue());
				requestJSONObject.put("twcf_warning_limit", m_oTextboxWarning.getValue());
				requestJSONObject.put("twcf_status", m_sStatusInputValue);
			}catch (JSONException jsone) {
				jsone.printStackTrace();
			}
			HashMap<String, String> oResultHashMap = AppGlobal.g_oFuncStation.get().changeTaiwanGUIConfig("gm", "pos", "saveTaiwanGuiConfig", requestJSONObject.toString());

			if(oResultHashMap.get("result").equals("false")) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), super.getParentForm());
			    oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			    oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._(oResultHashMap.get("message")));
			    oFormDialogBox.show();
			    oFormDialogBox = null;
			}else {
				HashMap<String, String> oTmpMap = new HashMap<String, String>();
				oTmpMap.put("twcfId", oResultHashMap.get("message"));
				oTmpMap.put("statId", ""+AppGlobal.g_oFuncStation.get().getStationId());
				oTmpMap.put("shopId", ""+AppGlobal.g_oFuncOutlet.get().getShopId());
				oTmpMap.put("oletId", ""+0);
				oTmpMap.put("type", m_sTypeInputValue);
				oTmpMap.put("status", m_sStatusInputValue);
				oTmpMap.put("startDate", m_oTextboxStartYear.getValue()+"-"+m_oTextboxStartMonth.getValue()+"-"+m_oTextboxStartDay.getValue());
				oTmpMap.put("endDate", m_oTextboxEndYear.getValue()+"-"+m_oTextboxEndMonth.getValue()+"-"+m_oTextboxEndDay.getValue());
				oTmpMap.put("prefix", m_oTextboxPrefix.getValue().toUpperCase());
				oTmpMap.put("startNumber", m_oTextboxStartNumber.getValue());
				oTmpMap.put("endNumber", m_oTextboxEndNumber.getValue());
				oTmpMap.put("warning", m_oTextboxWarning.getValue());
				oTmpMap.put("created", AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString());
				PosTaiwanGuiConfig oNewPosTaiwanGuiConfig = new PosTaiwanGuiConfig(oTmpMap);
				oPosTaiwanGuiConfigs.add(oNewPosTaiwanGuiConfig);
				if(oNewPosTaiwanGuiConfig.getStatus().equals(PosTaiwanGuiConfig.STATUS_ACTIVE))
					AppGlobal.g_oFuncStation.get().addTaiwanGuiConfig(oNewPosTaiwanGuiConfig);
			}
			resetAddConfigFrame();
			refreshTaiwanGuiConfigList();
			m_oFrameAddConfig.setVisible(false); 		
			bMatchChild = true;
			loadTaiwaiGuiConfigByStation();
 			
    	}
    	else if(iChildId == m_oButtonCancelAddTaiwanGUI.getId()) {
 			m_oFrameAddConfig.setVisible(false);
			resetAddConfigFrame();
			bMatchChild = true;
    	}
    	else if(iChildId == m_oLblTypeInput.getId()) {
    		ArrayList<String> oOptionList = new ArrayList<String>();
    		oOptionList.add(AppGlobal.g_oLang.get()._("normal"));
    		oOptionList.add(AppGlobal.g_oLang.get()._("special_invoice"));    			

    		FormSelectionBox oFormSelectionBox = new FormSelectionBox(super.getParentForm());
    		oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("please_select_the_type"), oOptionList, false);
    		oFormSelectionBox.show();
    		if (oFormSelectionBox.isUserCancel())
				return false;
    		else {
    			ArrayList<Integer> oSelectionBoxResult = oFormSelectionBox.getResultList(); 			
    			if(oSelectionBoxResult.get(0) == 0) {
    				m_oLblTypeInput.setValue(oOptionList.get(0));
    				m_sTypeInputValue = PosTaiwanGuiConfig.CONFIGURE_TYPE_NORMAL;
    			}else {
    				m_oLblTypeInput.setValue(oOptionList.get(1));
    				m_sTypeInputValue = PosTaiwanGuiConfig.CONFIGURE_TYPE_SPECIAL;
    			}
    		}
    	}
		else if(iChildId == m_oLblStatusInput.getId()) {
    		ArrayList<String> oOptionList = new ArrayList<String>();
    		oOptionList.add(AppGlobal.g_oLang.get()._("active"));
    		oOptionList.add(AppGlobal.g_oLang.get()._("suspended"));   			

    		FormSelectionBox oFormSelectionBox = new FormSelectionBox(super.getParentForm());
    		oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("please_select_the_status"), oOptionList, false);
    		oFormSelectionBox.show();
    		if (oFormSelectionBox.isUserCancel())
				return false;
    		else {
    			ArrayList<Integer> oSelectionBoxResult = oFormSelectionBox.getResultList(); 			
    			if(oSelectionBoxResult.get(0) == 0) {
    				m_oLblStatusInput.setValue(oOptionList.get(0));
    				m_sStatusInputValue = PosTaiwanGuiConfig.STATUS_ACTIVE;
    			}else {
    				m_oLblStatusInput.setValue(oOptionList.get(1));
    				m_sStatusInputValue = PosTaiwanGuiConfig.STATUS_SUSPENDED;
    			}
    		}
    	}
    	return bMatchChild;
    }
	
	private void resetAddConfigFrame() {
		m_oLblTypeInput.setValue(AppGlobal.g_oLang.get()._("normal"));
		m_oLblStatusInput.setValue(AppGlobal.g_oLang.get()._("active"));
		m_oTextboxStartYear.setValue("");
		m_oTextboxStartMonth.setValue("");
		m_oTextboxStartDay.setValue("");
		m_oTextboxEndYear.setValue("");
		m_oTextboxEndMonth.setValue("");
		m_oTextboxEndDay.setValue("");
		m_oTextboxPrefix.setValue("");
		m_oTextboxStartNumber.setValue("");
		m_oTextboxEndNumber.setValue("");
		m_oTextboxWarning.setValue("");
	}
	
	public String getTextValue(){
		return m_oTextboxPrefix.getValue();
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		PosTaiwanGuiConfigList oAllPosTaiwanGuiConfigs = AppGlobal.g_oFuncStation.get().getAllPosTaiwanGuiConfigs();
		oPosTaiwanGuiConfigs = oAllPosTaiwanGuiConfigs.getConfigList();
	
		FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), super.getParentForm());
		String sDisplayMsg = "";
		if(oPosTaiwanGuiConfigs.get(iItemIndex).getStatus().equals(PosTaiwanGuiConfig.STATUS_ACTIVE))
			sDisplayMsg += AppGlobal.g_oLang.get()._("confirm_to_change_status_to_suspended_for_gui_config");
		else
			sDisplayMsg += AppGlobal.g_oLang.get()._("confirm_to_change_status_to_active_for_gui_config");
		
		sDisplayMsg += System.lineSeparator() + System.lineSeparator();
		sDisplayMsg += AppGlobal.g_oLang.get()._("prefix") + ": " + oPosTaiwanGuiConfigs.get(iItemIndex).getPrefix()
				+ System.lineSeparator();
		sDisplayMsg += oPosTaiwanGuiConfigs.get(iItemIndex).getStartNum() + " ~ "
				+ oPosTaiwanGuiConfigs.get(iItemIndex).getEndNum();
		oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("change_status"));
		oFormConfirmBox.setMessage(sDisplayMsg);
		oFormConfirmBox.show();
		if(oFormConfirmBox.isOKClicked() == false)
			return;		
		else {
			JSONObject requestJSONObject = new JSONObject();
			try {
				requestJSONObject.put("twcf_id", this.oPosTaiwanGuiConfigs.get(iItemIndex).getTaiwanGuiConfigId());
				if(oPosTaiwanGuiConfigs.get(iItemIndex).getStatus().equals(PosTaiwanGuiConfig.STATUS_ACTIVE))
					requestJSONObject.put("twcf_status", PosTaiwanGuiConfig.STATUS_SUSPENDED);
				else
					requestJSONObject.put("twcf_status", PosTaiwanGuiConfig.STATUS_ACTIVE);		
					
			}catch (JSONException jsone) {
				jsone.printStackTrace();
			}
			
			HashMap<String, String> oResultHashMap = AppGlobal.g_oFuncStation.get().changeTaiwanGUIConfig("gm", "pos", "saveTaiwanGuiConfig", requestJSONObject.toString());
	
			if(oResultHashMap.get("result").equals("false")) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), super.getParentForm());
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._(oResultHashMap.get("message")));
				oFormDialogBox.show();
				oFormDialogBox = null;
			}
			else {
				if(oPosTaiwanGuiConfigs.get(iItemIndex).getStatus().equals(PosTaiwanGuiConfig.STATUS_ACTIVE)) {
					AppGlobal.g_oFuncStation.get().removeConfigByType(oPosTaiwanGuiConfigs.get(iItemIndex).getType());
					oPosTaiwanGuiConfigs.get(iItemIndex).setStatus(PosTaiwanGuiConfig.STATUS_SUSPENDED);
				}
				else {
					AppGlobal.g_oFuncStation.get().removeConfigByType(oPosTaiwanGuiConfigs.get(iItemIndex).getType());
					AppGlobal.g_oFuncStation.get().addTaiwanGuiConfig(oPosTaiwanGuiConfigs.get(iItemIndex));
					oPosTaiwanGuiConfigs.get(iItemIndex).setStatus(PosTaiwanGuiConfig.STATUS_ACTIVE);
				}
				refreshTaiwanGuiConfigList();
			}
		}
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		// TODO Auto-generated method stub
		
	}

	public List<PosTaiwanGuiConfig> getPosTaiwanGuiConfigs() {
		return oPosTaiwanGuiConfigs;
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
		// TODO Auto-generated method stub
		
	}
    
	void refreshTaiwanGuiConfigList(){
		this.clearAllRecords();
		//sort the list for display in order
		Collections.sort(this.getPosTaiwanGuiConfigs(), new Comparator<PosTaiwanGuiConfig>(){
			@Override
			public int compare(PosTaiwanGuiConfig oPosTaiwanGuiConfig1, PosTaiwanGuiConfig oPosTaiwanGuiConfig2) {
				//sort by status
				if(oPosTaiwanGuiConfig1.getStatus().equals(PosTaiwanGuiConfig.STATUS_ACTIVE) && !oPosTaiwanGuiConfig2.getStatus().equals(PosTaiwanGuiConfig.STATUS_ACTIVE))
					return -1;
				else if(!oPosTaiwanGuiConfig1.getStatus().equals(PosTaiwanGuiConfig.STATUS_ACTIVE) && oPosTaiwanGuiConfig2.getStatus().equals(PosTaiwanGuiConfig.STATUS_ACTIVE))
					return 1;
				else {
					//sort by start date
					if(oPosTaiwanGuiConfig1.getStartDate() != null && oPosTaiwanGuiConfig1.getEndDate() != null && oPosTaiwanGuiConfig2.getStartDate() != null && oPosTaiwanGuiConfig2.getEndDate() != null)
						return oPosTaiwanGuiConfig1.getStartDate().compareTo(oPosTaiwanGuiConfig2.getStartDate()) * -1;
					else if(oPosTaiwanGuiConfig1.getStartDate() != null && oPosTaiwanGuiConfig1.getEndDate() != null && oPosTaiwanGuiConfig2.getStartDate() == null && oPosTaiwanGuiConfig2.getEndDate() == null)
						return -1;
					else if(oPosTaiwanGuiConfig1.getStartDate() == null && oPosTaiwanGuiConfig1.getEndDate() == null && oPosTaiwanGuiConfig2.getStartDate() != null && oPosTaiwanGuiConfig2.getEndDate() != null)
						return 1;
					else
						//sort by create time
						return oPosTaiwanGuiConfig1.getCreateDate().compareTo(oPosTaiwanGuiConfig2.getCreateDate()) * -1;
				}
			}
		});
		
		for(int i = 0; i < this.getPosTaiwanGuiConfigs().size(); i++) {
			String sStartNumber = ""+this.getPosTaiwanGuiConfigs().get(i).getStartNum();
			String sEndNumber = ""+this.getPosTaiwanGuiConfigs().get(i).getEndNum();
			String sNumberRange = ("00000000" + sStartNumber).substring(sStartNumber.length()) +" ~ "+ ("00000000" + sEndNumber).substring(sEndNumber.length());
			String sDateRange = ((this.getPosTaiwanGuiConfigs().get(i).getStartDate() == null) ? "" : this.getPosTaiwanGuiConfigs().get(i).getStartDate()) + " ~ " + ((this.getPosTaiwanGuiConfigs().get(i).getEndDate() == null) ? "" : this.getPosTaiwanGuiConfigs().get(i).getEndDate());
			String sType = (this.getPosTaiwanGuiConfigs().get(i).getType().equals(PosTaiwanGuiConfig.CONFIGURE_TYPE_NORMAL) ? AppGlobal.g_oLang.get()._("normal") : AppGlobal.g_oLang.get()._("special_invoice"));
			String sStatus = (this.getPosTaiwanGuiConfigs().get(i).getStatus().equals(PosTaiwanGuiConfig.STATUS_ACTIVE) ? AppGlobal.g_oLang.get()._("active") : AppGlobal.g_oLang.get()._("suspended"));
			this.addRecord(0, i, this.getPosTaiwanGuiConfigs().get(i).getPrefix(), sNumberRange, sDateRange, sType, sStatus);
		}
	}
	
	public void loadTaiwaiGuiConfigByStation(){
		clearAllRecords();
		AppGlobal.g_oFuncStation.get().loadAllTaiwanGuiConfigByStation();
		setPosTaiwanGuiConfigs(AppGlobal.g_oFuncStation.get().getAllPosTaiwanGuiConfigs().getConfigList());
		refreshTaiwanGuiConfigList();
	}

//JackHO20171108
	@Override
	public void FrameTitleHeader_close() {
		// TODO Auto-generated method stub
		for (FrameEditTaiwanGUIConfigListener listener : listeners) {
			// Raise the event to parent
			listener.frameEditTaiwanGUIConfig_clickExit();
		}
	}
//end JackHO20171108
}
