package app;

import java.util.ArrayList;

import externallib.StringLib;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUITextbox;

interface FrameStockSoldoutByMenuListener {
	void frameStockSoldoutByMenu_setSoldout();

	void frameStockSoldoutByMenu_clickCancelSoldout();

	void frameStockSoldoutByMenu_clickShowAllResult();

	void frameStockSoldoutByMenu_clickSearchByName(String sValue);

	void frameStockSoldoutByMenu_selectAll(boolean bSelectAll);

	void frameStockSoldoutByMenu_clickAvailableBasketMenu(int iSectionId, int iItemIndex, String sNote);

	void frameStockSoldoutByMenu_clickSearchItem();
}

public class FrameStockSoldoutByMenu extends VirtualUIFrame implements FrameCommonBasketListener {
	private TemplateBuilder m_oTemplateBuilder;

	private VirtualUIFrame m_oFrameLeftContent;
	private VirtualUIFrame m_oFrameRightContent;

	private FrameCommonBasket m_oFrameMenuAndItemList;
	private VirtualUIButton m_oButtonSelectAll;
	private VirtualUIButton m_oButtonSetSoldout;
	private VirtualUIButton m_oButtonCancelSoldout;
	private VirtualUITextbox m_oTextboxSearchValue;
	private VirtualUIButton m_oButtonSearchByName;
	private VirtualUIButton m_oButtonShowAllResult;
	private VirtualUIButton m_oButtonSearchItem;

	public boolean m_bSelectAll;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameStockSoldoutByMenuListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameStockSoldoutByMenuListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameStockSoldoutByMenuListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameStockSoldoutByMenu() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameStockSoldoutByMenuListener>();
		m_bSelectAll = true;

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraStockSoldoutByMenu.xml");

		// Create Left Content Frame
		m_oFrameLeftContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftContent, "fraLeftContent");
		this.attachChild(m_oFrameLeftContent);

		// Available Menu list
		m_oFrameMenuAndItemList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFrameMenuAndItemList, "fraMenuAndItemList");
		m_oFrameMenuAndItemList.init();
		m_oFrameMenuAndItemList.addListener(this);
		m_oFrameLeftContent.attachChild(m_oFrameMenuAndItemList);

		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();

		iFieldWidths.add(m_oFrameMenuAndItemList.getWidth() / 5 * 4);
		sFieldValues.add(AppGlobal.g_oLang.get()._("name"));
		iFieldWidths.add(m_oFrameMenuAndItemList.getWidth() / 5);
		sFieldValues.add(AppGlobal.g_oLang.get()._("type"));

		m_oFrameMenuAndItemList.addHeader(iFieldWidths, sFieldValues);
		m_oFrameMenuAndItemList.setHeaderPadding(0, "0,0,0,16");
		m_oFrameMenuAndItemList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		m_oFrameMenuAndItemList.setBottomUnderlineVisible(false);

		// Select All button
		m_oButtonSelectAll = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSelectAll, "btnSelectAll");
		m_oButtonSelectAll.setValue(AppGlobal.g_oLang.get()._("select_all"));
		// KingsleyKwan20171019Large
		this.attachChild(m_oButtonSelectAll);

		// Soldout button
		m_oButtonSetSoldout = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSetSoldout, "btnSetSoldout");
		m_oButtonSetSoldout.setValue(AppGlobal.g_oLang.get()._("set_soldout"));
		m_oButtonSetSoldout.setClickServerRequestBlockUI(false);
		// KingsleyKwan20171019Large
		this.attachChild(m_oButtonSetSoldout);

		// Cancel soldout result button
		m_oButtonCancelSoldout = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancelSoldout, "btnCancelSoldout");
		m_oButtonCancelSoldout.setValue(AppGlobal.g_oLang.get()._("cancel_soldout"));
		m_oButtonSetSoldout.setClickServerRequestBlockUI(false);
		// KingsleyKwan20171019Large
		this.attachChild(m_oButtonCancelSoldout);

		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oButtonSearchItem = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonSearchItem, "btnSearchItem");
			m_oButtonSearchItem.setValue(AppGlobal.g_oLang.get()._("search_item"));
			m_oFrameLeftContent.attachChild(m_oButtonSearchItem);
		}
	}

	public void addRecord(int iSectionId, int iItemIndex, String sItemDesc, String sType, boolean bScroll) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();

		// Item Desc
		iFieldWidths.add(m_oFrameMenuAndItemList.getWidth() / 5 * 4);
		sFieldValues.add(sItemDesc);
		sFieldAligns.add("");
		iFieldWidths.add(m_oFrameMenuAndItemList.getWidth() / 5);
		sFieldValues.add(sType);
		sFieldAligns.add("");
		ArrayList<VirtualUIBasicElement> oSubmitIdElements = new ArrayList<VirtualUIBasicElement>();
		m_oFrameMenuAndItemList.addItem(iSectionId, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null,
				oSubmitIdElements);
		m_oFrameMenuAndItemList.setFieldPadding(iSectionId, iItemIndex, 0, "0,0,0,16");
		if (bScroll)
			m_oFrameMenuAndItemList.moveScrollToItem(iSectionId, iItemIndex);
	}

	public void setResultLineVisible(ArrayList<Integer> oLineIndex, boolean bVisible) {
		for (Integer iLineIndex : oLineIndex) {
			m_oFrameMenuAndItemList.setLineVisible(0, iLineIndex, bVisible);
		}
	}

	public void setAvailableBasketBackgroundColor(int iItemIndex, boolean bSelected) {
		if (bSelected)
			m_oFrameMenuAndItemList.setAllFieldsBackgroundColor(0, iItemIndex, "#66A6F1");
		else
			m_oFrameMenuAndItemList.setAllFieldsBackgroundColor(0, iItemIndex, "#FFFFFF");
	}

	public void removeAvailableMenu(int iItemIndex) {
		m_oFrameMenuAndItemList.removeItem(0, iItemIndex);
	}

	public void resetSelectAllButton() {
		m_bSelectAll = true;
		m_oButtonSelectAll.setValue(AppGlobal.g_oLang.get()._("select_all"));
	}

	public void updateSearchButtonColor() {
		String sUnselectedBackgroundColor = "#E0E0E0";
		String sUnselectedForegroundColor = "#015384";
		String sUnselectedStrokeColor = "#868686";
		String sSelectedBackgroundColor = "#3481B0";
		String sSelectedForegroundColor = "#FFFFFF";
		String sSelectedStrokeColor = "#005080";

		if (!m_oTextboxSearchValue.getValue().isEmpty()) {
			m_oButtonSearchByName.setBackgroundColor(sSelectedBackgroundColor);
			m_oButtonSearchByName.setForegroundColor(sSelectedForegroundColor);
			m_oButtonSearchByName.setStrokeColor(sSelectedStrokeColor);
			m_oButtonShowAllResult.setBackgroundColor(sUnselectedBackgroundColor);
			m_oButtonShowAllResult.setForegroundColor(sUnselectedForegroundColor);
			m_oButtonShowAllResult.setStrokeColor(sUnselectedStrokeColor);
		} else {
			m_oButtonSearchByName.setBackgroundColor(sUnselectedBackgroundColor);
			m_oButtonSearchByName.setForegroundColor(sUnselectedForegroundColor);
			m_oButtonSearchByName.setStrokeColor(sUnselectedStrokeColor);
			m_oButtonShowAllResult.setBackgroundColor(sSelectedBackgroundColor);
			m_oButtonShowAllResult.setForegroundColor(sSelectedForegroundColor);
			m_oButtonShowAllResult.setStrokeColor(sSelectedStrokeColor);
		}
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (m_oButtonSetSoldout.getId() == iChildId) {
			for (FrameStockSoldoutByMenuListener listener : listeners) {
				// Raise the event to parent
				listener.frameStockSoldoutByMenu_setSoldout();
			}
			bMatchChild = true;
		} else if (m_oButtonCancelSoldout.getId() == iChildId) {

			for (FrameStockSoldoutByMenuListener listener : listeners) {
				// Raise the event to parent
				listener.frameStockSoldoutByMenu_clickCancelSoldout();
			}
			bMatchChild = true;
		} else if (m_oButtonSelectAll.getId() == iChildId) {
			for (FrameStockSoldoutByMenuListener listener : listeners) {
				if (m_bSelectAll)
					m_oButtonSelectAll.setValue(AppGlobal.g_oLang.get()._("unselect_all"));
				else
					m_oButtonSelectAll.setValue(AppGlobal.g_oLang.get()._("select_all"));
				listener.frameStockSoldoutByMenu_selectAll(m_bSelectAll);
				break;
			}
			m_bSelectAll = !m_bSelectAll;

			bMatchChild = true;
		}

		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			if (m_oButtonSearchItem.getId() == iChildId) {
				for (FrameStockSoldoutByMenuListener listener : listeners) {
					listener.frameStockSoldoutByMenu_clickSearchItem();
					break;
				}
				bMatchChild = true;
			}
		}
		/*
		 * else if (m_oButtonSearchByName.getId() == iChildId) { for
		 * (FrameStockSoldoutByMenuListener listener : listeners) { // Raise the
		 * event to parent listener.frameStockSoldoutByMenu_clickSearchByName(
		 * m_oTextboxSearchValue.getValue()); }
		 * 
		 * if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.
		 * vertical_mobile.name()) && m_oFrameRightContent.getVisible()){
		 * m_oTextboxSearchValue.setValue("");
		 * m_oFrameRightContent.setVisible(false); }
		 * 
		 * this.updateSearchButtonColor(); bMatchChild = true; } else if
		 * (m_oButtonShowAllResult.getId() == iChildId) { for
		 * (FrameStockSoldoutByMenuListener listener : listeners) { // Raise the
		 * event to parent
		 * listener.frameStockSoldoutByMenu_clickShowAllResult(); }
		 * 
		 * if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.
		 * vertical_mobile.name()) && m_oFrameRightContent.getVisible()){
		 * m_oTextboxSearchValue.setValue("");
		 * m_oFrameRightContent.setVisible(false); }
		 * 
		 * this.updateSearchButtonColor(); bMatchChild = true; } else if
		 * (m_oButtonSearchItem.getId() == iChildId) {
		 * m_oFrameRightContent.setVisible(true); bMatchChild = true; }
		 */

		return bMatchChild;
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex,
			String sNote) {
		for (FrameStockSoldoutByMenuListener listener : listeners) {
			// Raise the event to parent
			listener.frameStockSoldoutByMenu_clickAvailableBasketMenu(iSectionIndex, iItemIndex, sNote);
		}
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId, int iSectionIndex, int iItemIndex,
			int iFieldIndex, String sNote) {
	}

}
