package app;

import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

import commonui.FormDialogBox;
import commonui.FormInputBox;
import commonui.FormSelectionBox;
import core.Controller;
import externallib.StringLib;

import om.*;
import templatebuilder.TemplateBuilder;
import virtualui.*;


public class FormCommonLookup extends VirtualUIForm implements FrameCommonLookupListener {
	private TemplateBuilder m_oTemplateBuilder;
	private boolean m_bSingleSelect;
	private boolean m_bHaveMinOrder;
	private int m_iTotalCountInAllTab;// Define the maximum selection count for this lookup. If maximum count reaches, auto close this lookup
	private int m_iCurrentTabIndex;
	private FrameCommonLookup m_oFraCommonLookup;
	private List<Integer> m_oListOfDeleteItem;
	private List<Integer> m_oLookupPathList;	//Array of LookupList Path
	private List<HashMap<String, Object>> m_oListOfLookupList; 					//LookupList array, i.e. Tabs, buttons info
	private HashMap<Integer, HashMap<String, Object>> m_oStoredLookupList;//Stored LookupList
	
	// Store selected item info
	private class SelectedLookupInfo {
		int tabIndex;
		int id;
		String name;
		boolean isExisted; // if Existing item, isExisted = true 
		
		SelectedLookupInfo(int iTabIndex, int iId, String sName, boolean bExisted) {
			tabIndex = iTabIndex;
			id = iId;
			name = sName;
			isExisted = bExisted;
		}
	}
	private List<SelectedLookupInfo> m_oSelectedLookupInfoList;

	// common lookup information
	public static String COMMON_LOOKUP_ID = "id";
	public static String COMMON_LOOKUP_NAME = "lookupName";
	public static String COMMON_LOOKUP_DATA_LIST = "dataList";
	public static String COMMON_LOOKUP_MAX = "max";
	public static String COMMON_LOOKUP_MIN = "min";
	
	// selected item information
	public static String SELECTED_ITEM_ID = "id";
	public static String SELECTED_ITEM_TAB_INDEX = "tabIndex";
	public static String SELECTED_ITEM_NAME = "name";
	
	public FormCommonLookup(Controller oParentController) {
		super(oParentController);
		
		m_bSingleSelect = false;
		m_bHaveMinOrder = true;
		m_iTotalCountInAllTab = 0;
		m_iCurrentTabIndex = 0;
		m_oTemplateBuilder = new TemplateBuilder();
		m_oFraCommonLookup = new FrameCommonLookup();
		m_oListOfDeleteItem = new ArrayList<Integer>();
		m_oLookupPathList = new ArrayList<Integer>();
		m_oListOfLookupList = new ArrayList<HashMap<String, Object>>();
		m_oStoredLookupList = new HashMap<Integer, HashMap<String,Object>>();
		m_oSelectedLookupInfoList = new ArrayList<SelectedLookupInfo>();
		
		m_oTemplateBuilder.loadTemplate("frmCommonLookup.xml");

		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oTemplateBuilder.buildFrame(m_oFraCommonLookup, "fraCommonLookup");
		//Add listener
		m_oFraCommonLookup.addListener(this);
		this.attachChild(m_oFraCommonLookup);
		
		//Invisible the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}

	public void setSingleSelection(boolean bSingleSelect) {
		m_bSingleSelect = bSingleSelect;
		
		// Update the screen
		m_oFraCommonLookup.setSingleSelection(bSingleSelect);
	}
	
	public void setTotalCountInAllTab(int iTotalCountInAllTab){
		m_iTotalCountInAllTab = iTotalCountInAllTab;
	}
	
	public void setCurrentTabIndex(int iCurrentTabIndex) {
		m_iCurrentTabIndex = iCurrentTabIndex;
	}
	
	//Display Lookup
	@SuppressWarnings("unchecked")
	public void createTabsAndLookupButtons(List<HashMap<String, Object>> oLookupArray) {
		m_oListOfLookupList = oLookupArray;
		for (HashMap<String, Object> oSingleLookup : oLookupArray) {
			int iLookupId = 0;
			if (oSingleLookup.containsKey(FormCommonLookup.COMMON_LOOKUP_ID))
				iLookupId = (int) oSingleLookup.get(FormCommonLookup.COMMON_LOOKUP_ID);
			String sLookupName = (String) oSingleLookup.get(FormCommonLookup.COMMON_LOOKUP_NAME);
			m_oFraCommonLookup.addLookupTab(iLookupId, sLookupName);
			
			if(iLookupId > 0) { // Store the lookup content list, for modifier lookup use
				int iMenuId = (Integer) oSingleLookup.get(FormCommonLookup.COMMON_LOOKUP_ID);
				m_oStoredLookupList.put(iMenuId, oSingleLookup);
			}
		}
		changeSelectedLookupTab(0);
	}
	
	public void addExistingItems(List<JSONObject> oExistingItemsArray) {
		for(JSONObject oSingleItem : oExistingItemsArray) {
			int iModifierListIndex = oSingleItem.optInt(FormCommonLookup.SELECTED_ITEM_TAB_INDEX);
			m_oFraCommonLookup.addSelectedItem(iModifierListIndex, oSingleItem.optString(FormCommonLookup.SELECTED_ITEM_NAME));
			
			SelectedLookupInfo oSelectedLookupInfo = new SelectedLookupInfo(iModifierListIndex, oSingleItem.optInt(FormCommonLookup.SELECTED_ITEM_ID), oSingleItem.optString(FormCommonLookup.SELECTED_ITEM_NAME), true);
			m_oSelectedLookupInfoList.add(oSelectedLookupInfo);
		}

		//Update Min/Max item count label
		updateMinMaxSelection();
	}
	
	public void addSelectedItem(FuncLookupButtonInfo oLookupButtonInfo) {
		HashMap<String, String> oTempHashMap = new HashMap<String, String>();
		oTempHashMap.put(FormCommonLookup.SELECTED_ITEM_ID, Integer.toString(oLookupButtonInfo.getId()));
		oTempHashMap.put(FormCommonLookup.SELECTED_ITEM_TAB_INDEX, Integer.toString(m_iCurrentTabIndex));

		String sNewName = oLookupButtonInfo.getName()[AppGlobal.g_oCurrentLangIndex.get()-1];
		if (oLookupButtonInfo.getParameter() != null && oLookupButtonInfo.getParameter().equals("askName")) {
			//Ask name
			boolean bBreak = false;
			FormInputBox oFormInputBox;
			FormDialogBox oFormDialogBox;
			
			do {
				bBreak = false;
				oFormInputBox = new FormInputBox(this);
				oFormInputBox.init();
				oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.DEFAULT);
				oFormInputBox.showKeyboard();
				oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("new_description"));
				oFormInputBox.setMessage(AppGlobal.g_oLang.get()._("please_input_the_description")+":");
				oFormInputBox.show();
				
				sNewName = oFormInputBox.getInputValue();
				if (sNewName == null)
					return;
				else if(sNewName.isEmpty() || sNewName.trim().isEmpty()) {
					oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("not_allow_blank_description"));
					oFormDialogBox.show();
					oFormDialogBox = null;
				} else
					bBreak = true;
				
			} while(!bBreak);
			oLookupButtonInfo.setName(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, sNewName)); // update open description name
			oTempHashMap.put(FormCommonLookup.SELECTED_ITEM_NAME, sNewName);
		}
		SelectedLookupInfo oSelectedLookupInfo = new SelectedLookupInfo(m_iCurrentTabIndex, oLookupButtonInfo.getId(), sNewName, false);
		m_oSelectedLookupInfoList.add(oSelectedLookupInfo);
		
		// Add to selected item Horizontal List
		m_oFraCommonLookup.addSelectedItem(m_iCurrentTabIndex, sNewName);
	}
	
	public boolean haveMinOrder() {
		return m_bHaveMinOrder;
	}
	
	public int getAllTabSelectedItemCount() {
		int iSelectedItemCount = 0;
		for (SelectedLookupInfo oSelectedLookupInfo: m_oSelectedLookupInfoList)
			iSelectedItemCount++;
		return iSelectedItemCount;
	}
	
	public int getSelectedItemCount(int iTabIndex) {
		int iSelectedItemCount = 0;
		for (SelectedLookupInfo oSelectedLookupInfo: m_oSelectedLookupInfoList) {
			if (oSelectedLookupInfo.tabIndex == iTabIndex)
				iSelectedItemCount++;
		}
		return iSelectedItemCount;
	}
	
	public int checkTabMinOrder() {
		for (int i = 0; i < m_oListOfLookupList.size(); i++) {
			HashMap<String, Object> oTabLookupList = m_oListOfLookupList.get(i);
			if (oTabLookupList.get(FormCommonLookup.COMMON_LOOKUP_MIN) == null) // Not Modifier
				continue;
			
			int iMinOrder = (int) oTabLookupList.get(FormCommonLookup.COMMON_LOOKUP_MIN);
			int iItemCount = getSelectedItemCount(i);
			if (iMinOrder > 0 && iItemCount < iMinOrder)
				return i;
		}
		
		return -1;
	}
	
	public List<JSONObject> getSelectedItems() {
		List<JSONObject> oSelectedItemJSONObjectList = new ArrayList<JSONObject>();
		for (SelectedLookupInfo oSelectedLookupInfo: m_oSelectedLookupInfoList) {
			if(oSelectedLookupInfo.isExisted)
				continue;

			JSONObject oJSONObject = new JSONObject();
			try {
				oJSONObject.put(FormCommonLookup.SELECTED_ITEM_ID, oSelectedLookupInfo.id);
				oJSONObject.put(FormCommonLookup.SELECTED_ITEM_TAB_INDEX, oSelectedLookupInfo.tabIndex);
				oJSONObject.put(FormCommonLookup.SELECTED_ITEM_NAME, oSelectedLookupInfo.name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			oSelectedItemJSONObjectList.add(oJSONObject);
		}
		return oSelectedItemJSONObjectList;
	}
	
	public List<Integer> getDeletedItems() {
		return m_oListOfDeleteItem;
	}
	
	// update label bread crumbs
	private void updateLookupPath() {
		String sLookupPath = new String();
		if (m_bSingleSelect) { // For discount, pantry message, paid in/out reason, void reason and octopus
			HashMap<String, Object> oLookupList = m_oListOfLookupList.get(0);
			sLookupPath = (String)oLookupList.get(FormCommonLookup.COMMON_LOOKUP_NAME);
		} else {
			for (int iLookupMenuId : m_oLookupPathList) {
				HashMap<String, Object> oLookupList = m_oStoredLookupList.get(iLookupMenuId);
				String sLookupName = (String)oLookupList.get(FormCommonLookup.COMMON_LOOKUP_NAME); 
	
				sLookupPath = sLookupPath + sLookupName;
				if (m_oLookupPathList.indexOf(iLookupMenuId) < m_oLookupPathList.size()-1)
					sLookupPath += " > ";
			}
		} 
		
		m_oFraCommonLookup.setLabelBreadcrumbs(sLookupPath);
	}
	
	private HashMap<String, Object> prepareMenuLookupArray(int iMenuId) { 
		MenuMenu oMenuMenu = AppGlobal.g_oFuncMenu.get().getMenuAndContentById(iMenuId);
		if (oMenuMenu == null)
			return null;
			
		String sLookupName;
		if (oMenuMenu.getShortName(AppGlobal.g_oCurrentLangIndex.get()).isEmpty())
			sLookupName = oMenuMenu.getName(AppGlobal.g_oCurrentLangIndex.get());
		else
			sLookupName = oMenuMenu.getShortName(AppGlobal.g_oCurrentLangIndex.get());
		List<FuncLookupButtonInfo> oContentLookupList = new ArrayList<FuncLookupButtonInfo>();
		HashMap<String, Object> oLookupData = new HashMap<String, Object>();
		
		//form the lookup data list
		FuncMenu oFuncMenu = new FuncMenu();
		oFuncMenu.constructContentLookupList(oMenuMenu, MenuMenuLookup.TYPE_SUB_MENU, oContentLookupList, true);
		oFuncMenu.constructContentLookupList(oMenuMenu, MenuMenuLookup.TYPE_ITEM, oContentLookupList, true);
		
		oLookupData.put(FormCommonLookup.COMMON_LOOKUP_ID, oMenuMenu.getMenuId());
		oLookupData.put(FormCommonLookup.COMMON_LOOKUP_NAME, sLookupName);
		oLookupData.put(FormCommonLookup.COMMON_LOOKUP_DATA_LIST, oContentLookupList);
		oLookupData.put(FormCommonLookup.COMMON_LOOKUP_MAX, oMenuMenu.getModifierMaxOrder());
		oLookupData.put(FormCommonLookup.COMMON_LOOKUP_MIN, oMenuMenu.getModifierMinOrder());
		
		return oLookupData;
	}
	
	public void setLabelTitleBar(String sTitleBar) {
		m_oFraCommonLookup.setLabelTitleBar(sTitleBar);
	}
	
	public void setLabelDescBar(String sDescBar) {
		m_oFraCommonLookup.setLabelDescBar(sDescBar);
	}
	
	public void setLookupImage(String sImageUrl) {
		m_oFraCommonLookup.setLookupImage(sImageUrl);
	}
	
	public void hideCloseButton(boolean bHide) {
		m_oFraCommonLookup.hideCloseButton(bHide);
	}
	
	@Override
	public void frameCommonLookup_tabClicked(int iIndex) {
		HashMap<String, Object> oTempList = new HashMap<String, Object>(m_oListOfLookupList.get(iIndex));
		if (oTempList.get(FormCommonLookup.COMMON_LOOKUP_ID) == null)		//Not Modifier
			return;

		changeSelectedLookupTab(iIndex);
	}

	public void changeSelectedLookupTab(int iTabIndex) {
		m_iCurrentTabIndex = iTabIndex;
		
		//update lookup buttons in Frame
		HashMap<String, Object> oTempList = new HashMap<String, Object>(m_oListOfLookupList.get(iTabIndex));
		List<FuncLookupButtonInfo> oLookupContentList = (List<FuncLookupButtonInfo>) oTempList.get(FormCommonLookup.COMMON_LOOKUP_DATA_LIST);
		m_oFraCommonLookup.updateLookupButtons(oLookupContentList);
		//m_oFraCommonLookup.updateTabColor(m_iCurrentTabIndex);
		
		//Reset LookupList Path
		m_oLookupPathList.clear();
		if(oTempList.containsKey(FormCommonLookup.COMMON_LOOKUP_ID)) {
			int iMenuId = (int) oTempList.get(FormCommonLookup.COMMON_LOOKUP_ID);
			m_oLookupPathList.add(iMenuId);
		}
		
		updateLookupPath();
		updateMinMaxSelection();
		
		// hide back button
		m_oFraCommonLookup.showPreviousButton(false);
	}
	
	public void updateMinMaxSelection() {
		//Update Min/Max item count label
		int iMinOrder = 0;
		int iMaxOrder = 0;
		int iItemCount = getSelectedItemCount(m_iCurrentTabIndex);
		HashMap<String, Object> oCurrentLookupList = m_oListOfLookupList.get(m_iCurrentTabIndex);
		if (oCurrentLookupList.get(FormCommonLookup.COMMON_LOOKUP_MAX) != null)
			iMaxOrder = (Integer) oCurrentLookupList.get(FormCommonLookup.COMMON_LOOKUP_MAX);
		if (oCurrentLookupList.get(FormCommonLookup.COMMON_LOOKUP_MIN) != null)
			iMinOrder = (Integer) oCurrentLookupList.get(FormCommonLookup.COMMON_LOOKUP_MIN);
		m_oFraCommonLookup.updateMinMaxSelection(iMaxOrder, iMinOrder, iItemCount);
	}
	
	@Override
	public void frameCommonLookup_lookupClicked(int iIndex) {
		//Retrieve clicked Lookup
		List<FuncLookupButtonInfo> oCurrentContentLookupList = null;
		if (m_bSingleSelect) // For discount, pantry message, paid in/out reason, void reason and octopus
			oCurrentContentLookupList = (List<FuncLookupButtonInfo>) m_oListOfLookupList.get(0).get(FormCommonLookup.COMMON_LOOKUP_DATA_LIST);
		else {
			int iCurrentMenuId = m_oLookupPathList.get(m_oLookupPathList.size()-1);
			oCurrentContentLookupList = (List<FuncLookupButtonInfo>) m_oStoredLookupList.get(iCurrentMenuId).get(FormCommonLookup.COMMON_LOOKUP_DATA_LIST);
		}
		
		// Re-sequence the button list according to defined sequence
		TreeMap<Integer, FuncLookupButtonInfo> oTmpLookupButtonInfoList = new TreeMap<Integer, FuncLookupButtonInfo>();
		for (FuncLookupButtonInfo oLookupButtonInfo : oCurrentContentLookupList) {
			oTmpLookupButtonInfoList.put(oLookupButtonInfo.getSeq(), oLookupButtonInfo);
		}

		FuncLookupButtonInfo oSelectedLookup = (FuncLookupButtonInfo) oTmpLookupButtonInfoList.values().toArray()[iIndex];
		
		if (oSelectedLookup.getType().equals(PosDisplayPanelLookup.TYPE_HOT_ITEM)) {
			//Check duplicate
			boolean bDuplicate = false;
			for (SelectedLookupInfo oSelectedLookupInfo: m_oSelectedLookupInfoList) {
				if (oSelectedLookupInfo.tabIndex == m_iCurrentTabIndex && oSelectedLookupInfo.id == oSelectedLookup.getId()) {
					bDuplicate = true;
					break;
				}
			}
			
			//Show option box
			if (bDuplicate) {
				ArrayList<String> oOptionList = new ArrayList<String>();
				oOptionList.add(AppGlobal.g_oLang.get()._("duplicate"));
				oOptionList.add(AppGlobal.g_oLang.get()._("delete"));

				FormSelectionBox oFormSelectionBox = new FormSelectionBox(this);
				oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("please_select_the_action_type"), oOptionList, false);
				oFormSelectionBox.show();
				
				if (oFormSelectionBox.isUserCancel())
					return;
				
				ArrayList<Integer> oSelectionBoxResult = oFormSelectionBox.getResultList();
				if (oSelectionBoxResult.get(0) == 1) {		//Delete
					int iIndexOfSelected = -1;
					for (SelectedLookupInfo oSelectedLookupInfo: m_oSelectedLookupInfoList) {
						if (oSelectedLookupInfo.tabIndex == m_iCurrentTabIndex && oSelectedLookupInfo.id == oSelectedLookup.getId()) {
							iIndexOfSelected = m_oSelectedLookupInfoList.indexOf(oSelectedLookupInfo);
						
							if (oSelectedLookupInfo.isExisted)
								m_oListOfDeleteItem.add(oSelectedLookup.getId());
							m_oSelectedLookupInfoList.remove(oSelectedLookupInfo);
							break;
						}
					}

					// remove the item from UI
					if (iIndexOfSelected >= 0) {
						// remove the item from UI
						m_oFraCommonLookup.removeSelectedItem(iIndexOfSelected);
						
						//Update Min/Max item count label
						updateMinMaxSelection();
					}
					
					return;
				}
			}
			
			//Check Min/Max order
			if (m_bSingleSelect) { 	//Exit if single Selection
				addSelectedItem(oSelectedLookup);
				this.finishShow();
			} else {
				HashMap<String, Object> oTabLookupList = m_oListOfLookupList.get(m_iCurrentTabIndex);
				if (oTabLookupList.get(FormCommonLookup.COMMON_LOOKUP_MAX) != null) {
					int iMaxOrder = (Integer) oTabLookupList.get(FormCommonLookup.COMMON_LOOKUP_MAX);
					int iItemCount = getSelectedItemCount(m_iCurrentTabIndex);
					if (iMaxOrder > 0) {
						if (iItemCount < iMaxOrder) {
							//add MenuItem into m_oListOfSelectedItem
							addSelectedItem(oSelectedLookup);
							iItemCount++;
							
							// Check if the selected item count = max
							if (iItemCount == iMaxOrder) {
								// Finish lookup if total item count exceeds total max count for all tabs
								if (m_iTotalCountInAllTab > 0 && getAllTabSelectedItemCount() >= m_iTotalCountInAllTab)
									this.finishShow();
								
								// Check if there is any next page
								if (m_oListOfLookupList.size() > (m_iCurrentTabIndex+1)) {	// Go to next page if yes
									//frameCommonLookup_tabClicked(m_iCurrentTabIndex+1);
									m_oFraCommonLookup.clickTab(m_iCurrentTabIndex+1);
									return;
								} else {	// Last page
									// Check whether other tabs have min order, go to that page if yes
									int iTabIndex = checkTabMinOrder();
									if (iTabIndex > -1) {
										//frameCommonLookup_tabClicked(iTabIndex);
										m_oFraCommonLookup.clickTab(iTabIndex);
										return;
									}
									// Finish lookup
									this.finishShow();
								}
							}
						}
					} else	//No limit, just add the item
						addSelectedItem(oSelectedLookup);
					
					//Update Min/Max item count label
					updateMinMaxSelection();
				}
			}
		} else if (oSelectedLookup.getType().equals(PosDisplayPanelLookup.TYPE_MENU_LOOKUP)) {
			int iMenuId = oSelectedLookup.getId();
			
			HashMap<String, Object> oMenuLookupArray = new HashMap<String,Object>();
			if (m_oStoredLookupList.containsKey(iMenuId))
				oMenuLookupArray = m_oStoredLookupList.get(iMenuId);
			else
				oMenuLookupArray = prepareMenuLookupArray(iMenuId);
			
			if (oMenuLookupArray != null && !oMenuLookupArray.isEmpty()) {
				//Create new buttons in Frame
				List<FuncLookupButtonInfo> oLookupContentList = (List<FuncLookupButtonInfo>) oMenuLookupArray.get(FormCommonLookup.COMMON_LOOKUP_DATA_LIST);
				m_oFraCommonLookup.updateLookupButtons(oLookupContentList);
				
				//Update LookupList Path
				m_oLookupPathList.add(iMenuId);
				
				// Store the lookup content list
				if (oMenuLookupArray.get(FormCommonLookup.COMMON_LOOKUP_ID) != null) {	// Store the current page and menu ID map					
					if (m_oStoredLookupList.containsKey(iMenuId) == false)
						m_oStoredLookupList.put(iMenuId, oMenuLookupArray);
				}
				updateLookupPath();
				m_oFraCommonLookup.showPreviousButton(true);
			}
		}
	}
	
	@Override
	public void frameCommonLookup_selectedItemClicked(int iSelectedItemIndex) {
		SelectedLookupInfo oSelectedLookupInfo = m_oSelectedLookupInfoList.get(iSelectedItemIndex);
		
		// existing item is deleted, mark down for further use after common lookup form closed
		if (oSelectedLookupInfo.isExisted)
			m_oListOfDeleteItem.add(oSelectedLookupInfo.id);

		// remove the item from UI
		m_oSelectedLookupInfoList.remove(iSelectedItemIndex);
		m_oFraCommonLookup.removeSelectedItem(iSelectedItemIndex);

		//Update Min/Max item count label
		if (oSelectedLookupInfo.tabIndex == m_iCurrentTabIndex)
			updateMinMaxSelection();
	}
	
	@Override
	public void frameCommonLookup_buttonExit() {
		if (!m_oListOfDeleteItem.isEmpty())
			m_oListOfDeleteItem.clear();

		// for first time choose, check whether tabs have min order, cannot add item if a tab has min order
		for (int i = 0; i < m_oListOfLookupList.size(); i++) {
			HashMap<String, Object> oTabLookupList = m_oListOfLookupList.get(i);
			if (oTabLookupList.get(FormCommonLookup.COMMON_LOOKUP_MIN) == null)
				continue;
			
			int iMinOrder = (int) oTabLookupList.get(FormCommonLookup.COMMON_LOOKUP_MIN);
			if (iMinOrder > 0) {
				m_bHaveMinOrder = false;
				break;
			}
		}
		
		m_oSelectedLookupInfoList.clear();
		this.finishShow();
	}

	@Override
	public void frameCommonLookup_buttonFinish() {
		int iTabIndex = checkTabMinOrder();
		if (iTabIndex > -1) {
			HashMap<String, Object> oTabLookupList = m_oListOfLookupList.get(iTabIndex);
			m_oFraCommonLookup.showErrorFrame(AppGlobal.g_oLang.get()._("missing_item_at")+" "+oTabLookupList.get(FormCommonLookup.COMMON_LOOKUP_NAME));
			//frameCommonLookup_tabClicked(iTabIndex);
			m_oFraCommonLookup.clickTab(iTabIndex);
			return;
		}
		
		if (!m_bSingleSelect)
			this.finishShow();
	}

	@Override
	public void frameCommonLookup_buttonPrevious() {
		if (m_oLookupPathList.size() == 1) // in the top page
			return;
		
		//Remove the last lookup
		m_oLookupPathList.remove(m_oLookupPathList.size()-1);
		
		int iPrevLookupMenuId = m_oLookupPathList.get(m_oLookupPathList.size()-1);
		HashMap<String, Object> oPreviousLookupInfo = m_oStoredLookupList.get(iPrevLookupMenuId);
		//Update lookup buttons in Frame
		List<FuncLookupButtonInfo> oPreviousLookupContent = (List<FuncLookupButtonInfo>) oPreviousLookupInfo.get(FormCommonLookup.COMMON_LOOKUP_DATA_LIST);
		m_oFraCommonLookup.updateLookupButtons(oPreviousLookupContent);
		
		updateLookupPath();
		
		// back to top page, hide back button
		m_oFraCommonLookup.showPreviousButton(false);
	}
}
