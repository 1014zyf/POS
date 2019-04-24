package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.json.JSONException;
import org.json.JSONObject;

import om.PosDisplayPanelLookup;

import templatebuilder.TemplateBuilder;
import virtualui.*;

interface FrameCommonLookupButtonsListener {
	void frameCommonLookupButtons_addItem(String sNote);
	void frameCommonLookupButtons_deleteItem(String sNote);
	void frameCommonLookupButtons_swipePage(boolean bLeft);
}

public class FrameCommonLookupButtons extends VirtualUIFrame implements FrameGeneralLookupButtonListener {
	private TemplateBuilder m_oTemplateBuilder;

	private VirtualUIFrame m_oFramePanel;
	private List<FrameGeneralLookupButton> m_oFrameLookupListButtons;
	private List<FuncLookupButtonInfo> m_oLookupButtonInfoList;
	private List<VirtualUIBasicElement> m_oSubmitElementList;

	private int m_iRows;
	private int m_iCols;
	private int m_iButtonFontSize;
	private int m_iPageCnt;
	private int m_iCurrentPage;

	private static final int CELL_SPACING = 10;

	private static final int ROW_NUM = 6;
	private static final int COLUMN_NUM = 10;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameCommonLookupButtonsListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameCommonLookupButtonsListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameCommonLookupButtonsListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameCommonLookupButtons() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameCommonLookupButtonsListener>();

		m_iRows = ROW_NUM;
		m_iCols = COLUMN_NUM;
		m_iButtonFontSize = 0;

		m_oFrameLookupListButtons = new ArrayList<FrameGeneralLookupButton>();
		m_oLookupButtonInfoList = new ArrayList<FuncLookupButtonInfo>();

		m_oTemplateBuilder.loadTemplate("fraCommonLookupButtons.xml");
	}

	public void init() {
		// Create Frame panel
		m_oFramePanel = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePanel, "scrfraPanel");
		m_oFramePanel.setWidth(this.getWidth());
		m_oFramePanel.setHeight(this.getHeight());
		m_oFramePanel.allowSwipeLeft(true);
		m_oFramePanel.setSwipeLeftServerRequestBlockUI(false);
		m_oFramePanel.allowSwipeRight(true);
		m_oFramePanel.setSwipeRightServerRequestBlockUI(false);
		this.attachChild(m_oFramePanel);

		for (int i = 0; i < ROW_NUM * COLUMN_NUM; i++) {
			FrameGeneralLookupButton fraLookupButton = new FrameGeneralLookupButton();
			m_oTemplateBuilder.buildFrame(fraLookupButton, "fraLooupButton");
			fraLookupButton.addListener(this);

			m_oFramePanel.attachChild(fraLookupButton);
			m_oFrameLookupListButtons.add(fraLookupButton);
		}

		m_iCurrentPage = 1;
		m_iPageCnt = 1;
	}

	public void init(int iRows, int iColumns, int iFontSize) {
		// Create Frame panel
		m_oFramePanel = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePanel, "scrfraPanel");
		m_oFramePanel.setWidth(this.getWidth());
		m_oFramePanel.setHeight(this.getHeight());
		m_oFramePanel.allowSwipeLeft(true);
		m_oFramePanel.setSwipeLeftServerRequestBlockUI(false);
		m_oFramePanel.allowSwipeRight(true);
		m_oFramePanel.setSwipeRightServerRequestBlockUI(false);
		this.attachChild(m_oFramePanel);

		if (iRows * iColumns > FrameCommonLookupButtons.ROW_NUM * FrameCommonLookupButtons.COLUMN_NUM) {
			if (iRows > FrameCommonLookupButtons.ROW_NUM)
				iRows = FrameCommonLookupButtons.ROW_NUM;

			if (iColumns > FrameCommonLookupButtons.COLUMN_NUM)
				iColumns = FrameCommonLookupButtons.COLUMN_NUM;
		}
		
		this.setConfig(iRows, iColumns, iFontSize);

		// *** As common lookup is a Form, so no need to add buttons until
		// change to Frame
		for (int i = 0; i < m_iRows * m_iCols; i++) {
			FrameGeneralLookupButton fraLookupButton = new FrameGeneralLookupButton();
			m_oTemplateBuilder.buildFrame(fraLookupButton, "fraLooupButton");
			fraLookupButton.addListener(this);
			
			m_oFramePanel.attachChild(fraLookupButton);
			m_oFrameLookupListButtons.add(fraLookupButton);
		}
	

		m_iCurrentPage = 1;
		m_iPageCnt = 1;
	}

	public void setConfig(int iRows, int iColumns, int iFontSize) {
		if (iRows * iColumns > FrameCommonLookupButtons.ROW_NUM * FrameCommonLookupButtons.COLUMN_NUM) {
			if (iRows > FrameCommonLookupButtons.ROW_NUM)
				iRows = FrameCommonLookupButtons.ROW_NUM;

			if (iColumns > FrameCommonLookupButtons.COLUMN_NUM)
				iColumns = FrameCommonLookupButtons.COLUMN_NUM;
		}
		
		setRows(iRows);
		setColumns(iColumns);
		setButtonFontSize(iFontSize);
	}

	private void setRows(int iRows) {
		if (iRows <= 0)
			return;
		
		m_iRows = iRows;
	}

	private void setColumns(int iColumns) {
		if (iColumns <= 0)
			return;
		
		m_iCols = iColumns;
	}

	private void setButtonFontSize(int iFontSize) {
		if (iFontSize <= 0)
			return;
		
		m_iButtonFontSize = iFontSize;
	}

	public void updateLookupButtons(List<FuncLookupButtonInfo> oLookupButtonInfoList,
			List<VirtualUIBasicElement> oSubmitElementList) {
		if (oLookupButtonInfoList != null) {
			int iCount = 0;
			int iTotalSeq = 0;
			HashMap<Integer, FuncLookupButtonInfo> oTmpLookupButtonInfoList = new HashMap<Integer, FuncLookupButtonInfo>();
			for (FuncLookupButtonInfo oLookupButtonInfo : oLookupButtonInfoList) {
				if (oLookupButtonInfo.getSeq() < 0)
					oLookupButtonInfo.setSeq(iCount);

				if (oLookupButtonInfo.getSeq() > iTotalSeq)
					iTotalSeq = oLookupButtonInfo.getSeq();
				oTmpLookupButtonInfoList.put(oLookupButtonInfo.getSeq(), oLookupButtonInfo);
				iCount++;
			}

			// Sort lookup button list by ascending lookup sequence order
			SortedSet<Integer> oSeqList = new TreeSet<Integer>(oTmpLookupButtonInfoList.keySet());
			List<FuncLookupButtonInfo> oResultLookupButtonInfoList = new ArrayList<FuncLookupButtonInfo>();
			for (Integer iSeq : oSeqList) {
				FuncLookupButtonInfo oFuncLookupButtonInfo = oTmpLookupButtonInfoList.get(iSeq);
				oResultLookupButtonInfoList.add(oFuncLookupButtonInfo);
			}
			m_oLookupButtonInfoList = oResultLookupButtonInfoList;
			m_iCurrentPage = 1;
			m_iPageCnt = ((iTotalSeq - 1) / (m_iCols * m_iRows)) + 1;
		}
		if (oSubmitElementList != null)
			m_oSubmitElementList = oSubmitElementList;

		int iStart = (m_iCurrentPage - 1) * (m_iCols * m_iRows);
		int iEnd = iStart + (m_iCols * m_iRows);
		// Sometimes, last item seq is not equal to size of
		// m_oLookupButtonInfoList
		// to calculate page count, should use last item seq
		int iLastSeq = 0;
		if (!m_oLookupButtonInfoList.isEmpty()) {
			for (FuncLookupButtonInfo oButtonInfo : m_oLookupButtonInfoList) {
				if (oButtonInfo.getSeq() > iLastSeq)
					iLastSeq = oButtonInfo.getSeq();
			}
		}
		if (iEnd > iLastSeq)
			iEnd = iLastSeq;

//JohnLiu 03112017 -- start
		for (int i = 0; i < m_oFrameLookupListButtons.size(); i++) {
			FrameGeneralLookupButton oGeneralLookupButton = m_oFrameLookupListButtons.get(i);
			// set all lookup buttons to invisible
			oGeneralLookupButton.setVisible(false);
			
			// clear all transparent frame for lookup buttons
			oGeneralLookupButton.setTransparentVisible(false);
		}
//JohnLiu 03112017 -- end

		int iWidth = (this.getWidth() - (CELL_SPACING * (m_iCols + 1))) / m_iCols;
		int iHeight = (this.getHeight() - (CELL_SPACING * (m_iRows + 1))) / m_iRows;
		int iAvailableLookupButtonCount = 0; // index of current use
												// m_oFrameLookupListButtons
												// element (Display button)
		int iDisplayLookupButtonCount = 0; // index of current use
											// m_oLookupButtonInfoList element
											// (Lookup button info)
		for (int i = 0; i < m_oLookupButtonInfoList.size(); i++) {
			FuncLookupButtonInfo oLookupButtonInfo = m_oLookupButtonInfoList.get(i);
			if (m_iButtonFontSize > 0)
				oLookupButtonInfo.setFontSize(m_iButtonFontSize);
			int iSeq = oLookupButtonInfo.getSeq() - 1;
			if (iSeq < iStart || iSeq >= iEnd)
				continue;

			FrameGeneralLookupButton oGeneralLookupButton;
			// Exceed m_oFrameLookupListButtons size, need to add new button
			// into m_oFrameLookupListButtons
			if (iDisplayLookupButtonCount >= m_oFrameLookupListButtons.size()) {
				oGeneralLookupButton = new FrameGeneralLookupButton();
				m_oTemplateBuilder.buildFrame(oGeneralLookupButton, "fraLooupButton");
				oGeneralLookupButton.addListener(this);

				m_oFramePanel.attachChild(oGeneralLookupButton);
				m_oFrameLookupListButtons.add(oGeneralLookupButton);
			} else {
				oGeneralLookupButton = m_oFrameLookupListButtons.get(iAvailableLookupButtonCount);
				iAvailableLookupButtonCount++;
			}

			oGeneralLookupButton.setWidth(iWidth);
			oGeneralLookupButton.setHeight(iHeight);
			oGeneralLookupButton.setTop(CELL_SPACING
					+ ((iSeq % (m_iCols * m_iRows)) / m_iCols) * (oGeneralLookupButton.getHeight() + CELL_SPACING));
			oGeneralLookupButton
					.setLeft(CELL_SPACING + (iSeq % m_iCols) * (oGeneralLookupButton.getWidth() + CELL_SPACING));
			oGeneralLookupButton.setVisible(true);

			if (!oLookupButtonInfo.getType().equals(PosDisplayPanelLookup.TYPE_MENU_LOOKUP))
				oGeneralLookupButton.setClickServerRequestBlockUI(false);

			try {
				JSONObject oJSONObject = new JSONObject();
				oJSONObject.put(FrameLookupButton.BUTTON_NOTE_ID, oLookupButtonInfo.getId());
				oJSONObject.put(FrameLookupButton.BUTTON_NOTE_TYPE, oLookupButtonInfo.getType());
				oJSONObject.put(FrameLookupButton.BUTTON_NOTE_SEQ, i);
				oGeneralLookupButton.setButtonProperty(oLookupButtonInfo, 0.2, oJSONObject.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			oGeneralLookupButton.setButtonClickBlockUI(oLookupButtonInfo.isBlockUI());
			oGeneralLookupButton.setButtonQty("0");
			
			if (m_oSubmitElementList != null) {
				// Add submit element for the button
				if (m_oSubmitElementList.size() > i) {
					VirtualUIBasicElement oSubmitElement = m_oSubmitElementList.get(i);
					oGeneralLookupButton.addClickServerRequestSubmitElement(oSubmitElement);
				}
			}
			iDisplayLookupButtonCount++;
	  
		}
	}

	public void nextPage() {
		if (m_iCurrentPage < m_iPageCnt)
			m_iCurrentPage++;
		else
			return;

		updateLookupButtons(null, null);
	}

	public void prevPage() {
		if (m_iCurrentPage > 1)
			m_iCurrentPage--;
		else
			return;

		updateLookupButtons(null, null);
	}

	public int getCurrentPage() {
		return m_iCurrentPage;
	}

	public int getPageCount() {
		return m_iPageCnt;
	}

	public int getCurrentStartIndex() {
		return (m_iCurrentPage - 1) * (m_iCols * m_iRows);
	}

	public int getNumberOfButtons() {
		return m_iCols * m_iRows;
	}

	public String getItemQunantity(int iItemIndex) {
		FrameGeneralLookupButton oGeneralLookupButton = m_oFrameLookupListButtons.get(iItemIndex);
		return oGeneralLookupButton.getButtonQtyValue();
	}

	// get button info in current page
	public List<FuncLookupButtonInfo> getCurrentLookupButtonInfos() {
		List<FuncLookupButtonInfo> oLookupButtonInfos = new ArrayList<FuncLookupButtonInfo>();
		int iStart = (m_iCurrentPage - 1) * (m_iCols * m_iRows);
		int iEnd = iStart + (m_iCols * m_iRows);
		if (iEnd > m_oLookupButtonInfoList.size())
			iEnd = m_oLookupButtonInfoList.size();

		for (int i = iStart; i < iEnd; i++) {
			FuncLookupButtonInfo oButtonInfo = m_oLookupButtonInfoList.get(i);
			oLookupButtonInfos.add(oButtonInfo);
		}

		return oLookupButtonInfos;
	}

	public void setItemQuantity(int iItemIndex, String sNewQty) {
		FrameGeneralLookupButton oGeneralLookupButton = m_oFrameLookupListButtons.get(iItemIndex);
		oGeneralLookupButton.setButtonQty(sNewQty);
	}

	public void setItemStockQty(int iItemIndex, BigDecimal dQty) {
		String sQty = FrameLookupButton.QUANTITY_SOLDOUT;
		
		if (dQty != null)
			sQty = dQty.stripTrailingZeros().toPlainString();
		m_oFrameLookupListButtons.get(iItemIndex).setButtonStockQty(sQty);
//JohnLiu 30102017 -- start
		if (sQty.equals(FrameLookupButton.QUANTITY_SOLDOUT) || dQty.compareTo(BigDecimal.ZERO) == 0) {
			m_oFrameLookupListButtons.get(iItemIndex).getButtonInformation();
			m_oFrameLookupListButtons.get(iItemIndex).setTransparentVisible(true);
		} else
			m_oFrameLookupListButtons.get(iItemIndex).setTransparentVisible(false);
//JohnLiu 30102017 -- end
	}

	public void clearLookupList() {
		this.m_oFrameLookupListButtons.clear();
	}

	// Set to support single selection
	public void setSingleSelection(boolean bSingleSelect) {
		if (bSingleSelect) {
			for (VirtualUIFrame oFrame : m_oFrameLookupListButtons) {
				oFrame.setClickServerRequestBlockUI(true);
			}
		}
	}

	@Override
	public boolean swipeRight(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (m_oFramePanel.getId() == iChildId) {
			for (FrameCommonLookupButtonsListener listener : listeners) {
				listener.frameCommonLookupButtons_swipePage(false);
			}
			bMatchChild = true;
		}
		return bMatchChild;
	}

	@Override
	public boolean swipeLeft(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (m_oFramePanel.getId() == iChildId) {
			for (FrameCommonLookupButtonsListener listener : listeners) {
				listener.frameCommonLookupButtons_swipePage(true);
			}
			bMatchChild = true;
		}
		return bMatchChild;
	}

	@Override
	public void frameGeneralLookupButton_clicked(String sNote) {
		for (FrameCommonLookupButtonsListener listener : listeners) {
			listener.frameCommonLookupButtons_addItem(sNote);
		}
	}

	@Override
	public void frameGeneralLookupButton_addQtyClicked(String sNote) {
		for (FrameCommonLookupButtonsListener listener : listeners) {
			listener.frameCommonLookupButtons_addItem(sNote);
		}
	}

	@Override
	public void frameGeneralLookupButton_minusQtyClicked(String sNote) {
		for (FrameCommonLookupButtonsListener listener : listeners) {
			listener.frameCommonLookupButtons_deleteItem(sNote);
		}
	}
}
