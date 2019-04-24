package app;

import java.util.ArrayList;

import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameStockSoldoutListener {
	void frameStockSoldout_clickAddSoldoutItem();
	void frameStockSoldout_clickRemoveSoldoutItem();
	void frameStockSoldout_clickBasketMenuItem(int iSectionId, int iItemIndex);
	void frameStockSoldout_clickSearchByName(String sValue);
	void frameStockSoldout_clickShowAllResult();
	void frameStockSoldout_clickSearchItem();
}

public class FrameStockSoldout extends VirtualUIFrame implements FrameCommonBasketListener {
	private TemplateBuilder m_oTemplateBuilder;

	private VirtualUIFrame m_oFrameLeftContent;

	private FrameCommonBasket m_oFrameItemList;

	private VirtualUIButton m_oButtonAddSoldoutItem;
	private VirtualUIButton m_oButtonRemoveSoldoutItem;

	private VirtualUIFrame m_oFramePage;
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIImage m_oImgButtonNextPage;

	private int m_iCurrentPageStartNo;
	private int m_iPageRecordCount;
	private int m_iScrollIndex = 0;

	private VirtualUIButton m_oButtonSearchItem;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameStockSoldoutListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameStockSoldoutListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameStockSoldoutListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameStockSoldout() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameStockSoldoutListener>();

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraStockSoldout.xml");

		// Create Left Content Frame
		m_oFrameLeftContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftContent, "fraLeftContent");
		this.attachChild(m_oFrameLeftContent);

		// Result list
		m_oFrameItemList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFrameItemList, "fraItemList");
		m_oFrameItemList.init();
		m_oFrameItemList.addListener(this);
		m_oFrameLeftContent.attachChild(m_oFrameItemList);

		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();

		iFieldWidths.add(m_oFrameItemList.getWidth());
		sFieldValues.add(AppGlobal.g_oLang.get()._("item_name"));

		m_oFrameItemList.addHeader(iFieldWidths, sFieldValues);
		m_oFrameItemList.setHeaderPadding(0, "0,0,0,16");
		m_oFrameItemList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);

		m_oFrameItemList.setBottomUnderlineVisible(false);

		// Add Item to Stock button
		m_oButtonAddSoldoutItem = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonAddSoldoutItem, "btnAddSoldoutItem");
		m_oButtonAddSoldoutItem.setValue(AppGlobal.g_oLang.get()._("add_item"));
		m_oButtonAddSoldoutItem.setVisible(true);
		this.attachChild(m_oButtonAddSoldoutItem);

		// Remove Item from Stock button
		m_oButtonRemoveSoldoutItem = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonRemoveSoldoutItem, "btnRemoveSoldoutItem");
		m_oButtonRemoveSoldoutItem.setValue(AppGlobal.g_oLang.get()._("delete_item"));
		m_oButtonRemoveSoldoutItem.setVisible(true);
		this.attachChild(m_oButtonRemoveSoldoutItem);

		// Create Page label
		m_oFramePage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePage, "fraPage");

		// Create previous page button
		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevPage");
		m_oImgButtonPrevPage.allowClick(true);
		m_oImgButtonPrevPage.setClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.allowLongClick(true);
		m_oImgButtonPrevPage.setLongClickServerRequestBlockUI(false);
		m_oFrameLeftContent.attachChild(m_oImgButtonPrevPage);
		this.attachChild(m_oImgButtonPrevPage);

		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.allowLongClick(true);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		m_oFrameLeftContent.attachChild(m_oImgButtonNextPage);
		this.attachChild(m_oImgButtonNextPage);

		VirtualUIImage oImage = new VirtualUIImage();
		oImage.setWidth(m_oFramePage.getWidth());
		oImage.setHeight(m_oFramePage.getHeight());
		oImage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_page_bg.png");
		m_oFramePage.attachChild(oImage);

		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oLblPage.setWidth(m_oFramePage.getWidth());
		m_oLblPage.setHeight(m_oFramePage.getHeight());
		m_oFramePage.attachChild(m_oLblPage);
		m_oFrameLeftContent.attachChild(m_oFramePage);
		this.attachChild(m_oFramePage);

		// Show page up and down button
		if (AppGlobal.g_oFuncStation.get().isShowPageUpAndDownButtonForList()) {
			if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				m_oFrameItemList.setHeight(561);
				m_iPageRecordCount = 13;
				;
			} else
				m_iPageRecordCount = 7;
		} else {
			m_oFramePage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
			m_oImgButtonNextPage.setVisible(false);
		}

		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oButtonSearchItem = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonSearchItem, "btnSearchItem");
			m_oButtonSearchItem.setValue(AppGlobal.g_oLang.get()._("search_item"));
			m_oFrameLeftContent.attachChild(m_oButtonSearchItem);
		}
	}

	public void addRecord(int iSectionId, int iItemIndex, String sItemDesc) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();

		// Item Desc
		iFieldWidths.add(m_oFrameItemList.getWidth());
		sFieldValues.add(sItemDesc);
		sFieldAligns.add("");
		ArrayList<VirtualUIBasicElement> oSubmitIdElements = new ArrayList<VirtualUIBasicElement>();
		m_oFrameItemList.addItem(iSectionId, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null,
				oSubmitIdElements);
		m_oFrameItemList.moveScrollToItem(iSectionId, iItemIndex);
		m_oFrameItemList.setFieldPadding(iSectionId, iItemIndex, 0, "0,0,0,16");
		if (AppGlobal.g_oFuncStation.get().isShowPageUpAndDownButtonForList())
			updatePageUpDownVisibility();
	}

	public void removeRecord(int iSectionId, int iItemIndex) {
		m_oFrameItemList.removeItem(iSectionId, iItemIndex);
		m_oFrameItemList.moveScrollToTop();

		if (AppGlobal.g_oFuncStation.get().isShowPageUpAndDownButtonForList())
			updatePageUpDownVisibility();
	}

	public void setResultLineVisible(ArrayList<Integer> oLineIndex, boolean bVisible) {
		for (Integer iLineIndex : oLineIndex) {
			m_oFrameItemList.setLineVisible(0, iLineIndex, bVisible);
		}
	}

	public void setItemBackgroundColor(int iItemIndex, boolean bSelected) {
		if (bSelected)
			m_oFrameItemList.setFieldBackgroundColor(0, iItemIndex, 0, "#66A6F1");
		else
			m_oFrameItemList.setFieldBackgroundColor(0, iItemIndex, 0, "#FFFFFF");
	}

	public void updatePageUpDownVisibility() {
		boolean bShowPageUp = false;
		boolean bShowPageDown = false;
		int iPage = 0;
		int iCurrentPanelRecordCount = 0;

		iCurrentPanelRecordCount = m_oFrameItemList.getItemCellCount(0); // iSectionId
																			// pending

		if (iCurrentPanelRecordCount > m_iPageRecordCount)
			iPage = (m_iCurrentPageStartNo / m_iPageRecordCount) + 1;

		if (m_iCurrentPageStartNo > 0)
			bShowPageUp = true;

		if (iCurrentPanelRecordCount > m_iPageRecordCount
				&& m_iCurrentPageStartNo + m_iPageRecordCount < iCurrentPanelRecordCount)
			bShowPageDown = true;

		setPageNumber(iPage);
		showPageUp(bShowPageUp);
		showPageDown(bShowPageDown);
	}

	public void showPageUp(boolean bShow) {
		if (bShow)
			m_oImgButtonPrevPage.setSource(
					AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
		else
			m_oImgButtonPrevPage.setSource(
					AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
		m_oImgButtonPrevPage.setEnabled(bShow);
	}

	public void showPageDown(boolean bShow) {
		if (bShow)
			m_oImgButtonNextPage.setSource(
					AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
		else
			m_oImgButtonNextPage.setSource(
					AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
		m_oImgButtonNextPage.setEnabled(bShow);
	}

	public void setPageNumber(int iNumber) {
		int iTotalPage = 0;
		if (iNumber > 0) {
			iTotalPage = (int) Math.ceil(1.0 * m_oFrameItemList.getItemCount(0) / m_iPageRecordCount);
			m_oFramePage.setVisible(true);
			m_oLblPage.setValue(iNumber + "/" + iTotalPage);
			m_oLblPage.setVisible(true);
			m_oImgButtonPrevPage.setVisible(true);
			m_oImgButtonNextPage.setVisible(true);
		} else {
			m_oFramePage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
			m_oImgButtonNextPage.setVisible(false);
		}
	}

	public void clearAllRecords() {
		m_oFrameItemList.removeAllItems(0);
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (iChildId == m_oImgButtonPrevPage.getId()) {
			// PAGE UP
			if (m_iCurrentPageStartNo - m_iPageRecordCount >= 0) {
				m_iCurrentPageStartNo -= m_iPageRecordCount;
				updatePageUpDownVisibility();
				m_iScrollIndex -= m_iPageRecordCount;
				m_oFrameItemList.moveScrollToItem(0, m_iScrollIndex);
			}
			bMatchChild = true;
		} else if (iChildId == m_oImgButtonNextPage.getId()) {
			// PAGE DOWN
			if (m_iCurrentPageStartNo + m_iPageRecordCount < m_oFrameItemList.getItemCount(0)) {
				m_iCurrentPageStartNo += m_iPageRecordCount;
				updatePageUpDownVisibility();
				m_iScrollIndex += m_iPageRecordCount;
				m_oFrameItemList.moveScrollToItem(0, m_iScrollIndex);
			}
			bMatchChild = true;
		} else if (m_oButtonAddSoldoutItem.getId() == iChildId) {
			for (FrameStockSoldoutListener listener : listeners) {
				// Raise the event to parent
				listener.frameStockSoldout_clickAddSoldoutItem();
			}
			bMatchChild = true;
		} else if (m_oButtonRemoveSoldoutItem.getId() == iChildId) {
			for (FrameStockSoldoutListener listener : listeners) {
				// Raise the event to parent
				listener.frameStockSoldout_clickRemoveSoldoutItem();
			}
			bMatchChild = true;
		}

		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			if (m_oButtonSearchItem.getId() == iChildId) {
				for (FrameStockSoldoutListener listener : listeners) {
					// Raise the event to parent
					listener.frameStockSoldout_clickSearchItem();
				}
				bMatchChild = true;
			}
		}
		return bMatchChild;
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex,
			String sNote) {
		for (FrameStockSoldoutListener listener : listeners) {
			// Raise the event to parent
			listener.frameStockSoldout_clickBasketMenuItem(iSectionIndex, iItemIndex);
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
