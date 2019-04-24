package app;

import java.util.ArrayList;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

interface FrameStockSoldoutFunctionListener {
	void frameStockSoldoutFunction_clickExit();
	void frameStockSoldoutFunction_clickAddSoldoutItem();
	void frameStockSoldoutFunction_clickRemoveSoldoutItem();
	void frameStockSoldoutFunction_clickShowAllResult();
	void frameStockSoldoutFunction_clickShowAllMenuResult();
	void frameStockSoldoutFunction_setSoldout();
	void frameStockSoldoutFunction_cancelSoldout();
	void frameStockSoldoutFunction_switchTag(int iTagIndex);
	void frameStockSoldoutFunction_clickSearchByName(String sValue);
	void frameStockSoldoutFunction_clickSearchMenuByName(String sValue);
	void frameStockSoldoutFunction_selectAll(boolean bSelectAll);
	void frameStockSoldoutFunction_clickBasketMenuItem(int iSectionId, int iItemIndex);
	void frameStockSoldoutFunction_clickAvaiableBasketMenu(int iSectionId, int iItemIndex, String sNote);
}

public class FrameStockSoldoutFunction extends VirtualUIFrame implements FrameStockSoldoutListener, FrameStockSoldoutByMenuListener, FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;

	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUILabel m_oLabelStockSoldoutTag;
	private VirtualUILabel m_oLabelStockSoldoutByMenuTag;
	private VirtualUIFrame m_oFrameTagIndicator;
	
	private FrameStockSoldout m_oFrameStockSoldout;
	private FrameStockSoldoutByMenu m_oFrameStockSoldoutByMenu;
//KingsleyKwan20170918StockSoldoutByKing		-----Start-----
	private FrameCommonPageContainer m_oFrameCommonTabList;
	private VirtualUIFrame m_oFrameRightContent;
	private VirtualUITextbox m_oTextboxSearchValue;
	private VirtualUIButton m_oButtonSearchByName;
	private VirtualUIButton m_oButtonShowAllResult;
//KingsleyKwan20170918StockSoldoutByKing		----- End -----
	private int m_iCurrentTagIndex;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameStockSoldoutFunctionListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameStockSoldoutFunctionListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameStockSoldoutFunctionListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	//Constructor
	public FrameStockSoldoutFunction(boolean bSoldOutByShop) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameStockSoldoutFunctionListener>();
		
		m_iCurrentTagIndex = 0;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraStockSoldoutFunction.xml");
		
		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(true);
		if(bSoldOutByShop)
			m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("item_soldout_by_shop"));
		else
			m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("item_soldout"));
		m_oFrameTitleHeader.addListener(this);
		this.attachChild(m_oFrameTitleHeader);
		
		m_oFrameCommonTabList = new FrameCommonPageContainer();
		m_oTemplateBuilder.buildFrame(m_oFrameCommonTabList, "fraCommonTabList");
		m_oFrameCommonTabList.init(m_oFrameCommonTabList.getWidth(), m_oFrameCommonTabList.getHeight(), 160, 90, 3, "#0055B8", "#999999", "", "", 26, false, true);
		m_oFrameCommonTabList.setUnderlineColor("#0055B8");
		this.attachChild(m_oFrameCommonTabList);
		
		//Create Right Content Frame
		m_oFrameRightContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameRightContent, "fraRightContent");
		this.attachChild(m_oFrameRightContent);
		
		// Search value box
		m_oTextboxSearchValue = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxSearchValue, "txtboxSearchValue");
		m_oFrameRightContent.attachChild(m_oTextboxSearchValue);
		
		// Search button
		m_oButtonSearchByName = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSearchByName, "btnSearchByName");
		m_oButtonSearchByName.setValue(AppGlobal.g_oLang.get()._("search_by_name"));
		m_oButtonSearchByName.setVisible(true);
		m_oButtonSearchByName.addClickServerRequestSubmitElement(m_oTextboxSearchValue);
		m_oFrameRightContent.attachChild(m_oButtonSearchByName);
		
		// Show all result button
		m_oButtonShowAllResult = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonShowAllResult, "btnShowAllResult");
		m_oButtonShowAllResult.setValue(AppGlobal.g_oLang.get()._("show_all"));
		m_oButtonShowAllResult.setVisible(true);
		m_oFrameRightContent.attachChild(m_oButtonShowAllResult);
		
		m_oFrameStockSoldout = new FrameStockSoldout();
		m_oFrameStockSoldout.addListener(this);
		
		m_oFrameStockSoldoutByMenu = new FrameStockSoldoutByMenu();
		m_oFrameStockSoldoutByMenu.addListener(this);
		
		m_oFrameCommonTabList.addButton(AppGlobal.g_oLang.get()._("soldout_item"), m_oFrameStockSoldout);
		m_oFrameCommonTabList.addButton(AppGlobal.g_oLang.get()._("soldout_menu"), m_oFrameStockSoldoutByMenu);
//KingsleyKwan20170918StockSoldoutByKing		----- End -----
	}

	public void addItemRecord(int iSectionId, int iItemIndex, String sItemDesc){
		m_oFrameStockSoldout.addRecord(iSectionId, iItemIndex, sItemDesc);
	}

	public void addMenuRecord(int iSectionId, int iItemIndex, String sItemDesc, String sType, boolean bScroll){
		m_oFrameStockSoldoutByMenu.addRecord(iSectionId, iItemIndex, sItemDesc, sType, bScroll);
	}

	public void removeItemRecord(int iSectionId, int iItemIndex) {
		m_oFrameStockSoldout.removeRecord(iSectionId, iItemIndex);
	}
	
	public void clearAllItemRecords() {
		m_oFrameStockSoldout.clearAllRecords();
	}

	public void clearSearchValue(){
		m_oTextboxSearchValue.setValue("");
	}
	
	public void clearSearchMenuValue() {
		m_oTextboxSearchValue.setValue("");
		//m_oFrameStockSoldoutByMenu.clearSearchValue();
	}
	public void setItemResultLineVisible(ArrayList<Integer> oLineIndex, boolean bVisible){
		m_oFrameStockSoldout.setResultLineVisible(oLineIndex, bVisible);
	}

	public void setMenuResultLineVisible(ArrayList<Integer> oLineIndex, boolean bVisible){
		m_oFrameStockSoldoutByMenu.setResultLineVisible(oLineIndex, bVisible);
	}

	public void resetSelectAllButton() {
		m_oFrameStockSoldoutByMenu.resetSelectAllButton();
	}
	
	private void switchTag(int iTagIndex){
		if(m_iCurrentTagIndex == iTagIndex)
			return;
		
		switch(iTagIndex){
			case 0:
				m_oFrameStockSoldout.setVisible(true);
				m_oFrameStockSoldoutByMenu.setVisible(false);
				
				m_oLabelStockSoldoutTag.setForegroundColor("#015384");
				m_oLabelStockSoldoutByMenuTag.setForegroundColor("#66a1c1");
				
				m_oFrameTagIndicator.setTop(m_oLabelStockSoldoutTag.getTop() + m_oLabelStockSoldoutTag.getHeight());
				m_oFrameTagIndicator.setLeft(m_oLabelStockSoldoutTag.getLeft());
				m_oFrameTagIndicator.setWidth(m_oLabelStockSoldoutTag.getWidth());
				
				break;
			case 1:
				m_oFrameStockSoldout.setVisible(false);
				m_oFrameStockSoldoutByMenu.setVisible(true);
				
				m_oLabelStockSoldoutTag.setForegroundColor("#66a1c1");
				m_oLabelStockSoldoutByMenuTag.setForegroundColor("#015384");
				
				m_oFrameTagIndicator.setTop(m_oLabelStockSoldoutByMenuTag.getTop() + m_oLabelStockSoldoutByMenuTag.getHeight());
				m_oFrameTagIndicator.setLeft(m_oLabelStockSoldoutByMenuTag.getLeft());
				m_oFrameTagIndicator.setWidth(m_oLabelStockSoldoutByMenuTag.getWidth());
				
				break;
		}
		
		m_iCurrentTagIndex = iTagIndex;
		
		for (FrameStockSoldoutFunctionListener listener : listeners) {
			// Raise the event to parent
			listener.frameStockSoldoutFunction_switchTag(iTagIndex);
		}
	}
	
	public void setItemBackgroundColor(int iItemIndex, boolean bSelected) {
		m_oFrameStockSoldout.setItemBackgroundColor(iItemIndex, bSelected);
	}

	public void setMenuBackgroundColor(int iItemIndex, boolean bSelected) {
		m_oFrameStockSoldoutByMenu.setAvailableBasketBackgroundColor(iItemIndex, bSelected);
	}

	public void removeAvailableMenu(int iItemIndex) {
		m_oFrameStockSoldoutByMenu.removeAvailableMenu(iItemIndex);
	}
	
	public void updateSearchButtonColor() {
		String sUnselectedBackgroundColor = "#FFFFFF";
		String sUnselectedForegroundColor = "#666666";
		String sSelectedBackgroundColor = "#02428B";
		String sSelectedForegroundColor = "#FFFFFF";
		
		if(!m_oTextboxSearchValue.getValue().isEmpty()) {
			m_oButtonSearchByName.setBackgroundColor(sSelectedBackgroundColor);
			m_oButtonSearchByName.setForegroundColor(sSelectedForegroundColor);
			m_oButtonShowAllResult.setBackgroundColor(sUnselectedBackgroundColor);
			m_oButtonShowAllResult.setForegroundColor(sUnselectedForegroundColor);
		} else {
			m_oButtonSearchByName.setBackgroundColor(sUnselectedBackgroundColor);
			m_oButtonSearchByName.setForegroundColor(sUnselectedForegroundColor);
			m_oButtonShowAllResult.setBackgroundColor(sSelectedBackgroundColor);
			m_oButtonShowAllResult.setForegroundColor(sSelectedForegroundColor);
		}
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (m_oButtonSearchByName.getId() == iChildId) {
			if(m_oFrameCommonTabList.getCurrentIndex() == 0)
				frameStockSoldout_clickSearchByName(m_oTextboxSearchValue.getValue());
			else
				frameStockSoldoutByMenu_clickSearchByName(m_oTextboxSearchValue.getValue());
			
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()) && m_oFrameRightContent.getVisible()){
				m_oTextboxSearchValue.setValue("");
				m_oFrameRightContent.setVisible(false);
			}
			
			this.updateSearchButtonColor();
			bMatchChild = true;
		}
		else
		if (m_oButtonShowAllResult.getId() == iChildId) {
			if(m_oFrameCommonTabList.getCurrentIndex() == 0)
				frameStockSoldout_clickShowAllResult();
			else
				frameStockSoldoutByMenu_clickShowAllResult();
			
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()) && m_oFrameRightContent.getVisible()){				
				m_oTextboxSearchValue.setValue("");
				m_oFrameRightContent.setVisible(false);
			}
			
			this.updateSearchButtonColor();
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
	
	@Override
	public void frameStockSoldout_clickAddSoldoutItem() {

		for (FrameStockSoldoutFunctionListener listener : listeners) {
			// Raise the event to parent
			listener.frameStockSoldoutFunction_clickAddSoldoutItem();
		}
	}

	@Override
	public void frameStockSoldout_clickRemoveSoldoutItem() {
		for (FrameStockSoldoutFunctionListener listener : listeners) {
			// Raise the event to parent
			listener.frameStockSoldoutFunction_clickRemoveSoldoutItem();
		}
	}

	@Override
	public void frameStockSoldout_clickBasketMenuItem(int iSectionId,
			int iItemIndex) {
		for (FrameStockSoldoutFunctionListener listener : listeners) {
			// Raise the event to parent
			listener.frameStockSoldoutFunction_clickBasketMenuItem(iSectionId, iItemIndex);
		}
	}

	@Override
	public void frameStockSoldout_clickSearchByName(String sValue) {
		for (FrameStockSoldoutFunctionListener listener : listeners) {
			// Raise the event to parent
			listener.frameStockSoldoutFunction_clickSearchByName(sValue);
		}
	}

	@Override
	public void frameStockSoldout_clickShowAllResult() {
		for (FrameStockSoldoutFunctionListener listener : listeners) {
			// Raise the event to parent
			listener.frameStockSoldoutFunction_clickShowAllResult();
		}
	}

	@Override
	public void frameStockSoldoutByMenu_clickAvailableBasketMenu(
			int iSectionId, int iItemIndex, String sNote) {
		for (FrameStockSoldoutFunctionListener listener : listeners) {
			// Raise the event to parent
			listener.frameStockSoldoutFunction_clickAvaiableBasketMenu(iSectionId, iItemIndex, sNote);
		}
	}

	@Override
	public void frameStockSoldoutByMenu_setSoldout() {
		for (FrameStockSoldoutFunctionListener listener : listeners) {
			// Raise the event to parent
			listener.frameStockSoldoutFunction_setSoldout();
		}
	}

	@Override
	public void frameStockSoldoutByMenu_clickCancelSoldout() {
		for (FrameStockSoldoutFunctionListener listener : listeners) {
			// Raise the event to parent
			listener.frameStockSoldoutFunction_cancelSoldout();
		}
	}

	@Override
	public void frameStockSoldoutByMenu_selectAll(boolean bSelectAll) {
		for (FrameStockSoldoutFunctionListener listener : listeners) {
			// Raise the event to parent
			listener.frameStockSoldoutFunction_selectAll(bSelectAll);
		}
	}

	@Override
	public void frameStockSoldoutByMenu_clickSearchByName(String sValue) {
		for (FrameStockSoldoutFunctionListener listener : listeners) {
			// Raise the event to parent
			listener.frameStockSoldoutFunction_clickSearchMenuByName(sValue);
		}
	}

	@Override
	public void frameStockSoldoutByMenu_clickShowAllResult() {
		for (FrameStockSoldoutFunctionListener listener : listeners) {
			// Raise the event to parent
			listener.frameStockSoldoutFunction_clickShowAllMenuResult();
		}
	}

	@Override
	public void FrameTitleHeader_close() {
		for (FrameStockSoldoutFunctionListener listener : listeners) {
			// Raise the event to parent
			listener.frameStockSoldoutFunction_clickExit();
		}
	}

	@Override
	public void frameStockSoldoutByMenu_clickSearchItem() {
		// TODO Auto-generated method stub
		m_oFrameRightContent.setLeft((this.getWidth() - m_oFrameRightContent.getWidth()) / 2);
		m_oFrameRightContent.setTop((this.getHeight() - m_oFrameRightContent.getHeight()) / 2);
		m_oFrameRightContent.setVisible(true);
	}

	@Override
	public void frameStockSoldout_clickSearchItem() {
		// TODO Auto-generated method stub
		m_oFrameRightContent.setLeft((this.getWidth() - m_oFrameRightContent.getWidth()) / 2);
		m_oFrameRightContent.setTop((this.getHeight() - m_oFrameRightContent.getHeight()) / 2);
		m_oFrameRightContent.setVisible(true);
		
	}

}
