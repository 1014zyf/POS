package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIHorizontalList;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;

/** interface for the listeners/observers callback method */
interface FrameSplitTableListener {
	void fraSplitTable_clickExit();
	void fraSplitTable_sendCheck();
	void fraSplitTable_printCheck();
	void fraSplitTable_clickColumnHeader(String sTable, boolean bShowCheckDetail);
	void fraSplitTable_equalSplitItem(String sTable, int iSectionId, int iItemIndex, boolean bAskQty);
	void frameCheckFunction_splitItem(String sFromTable, int iFromSectionIndex, int iFromItemIndex, String sToTable,
			int iTargetSectionIndex, BigDecimal oQty, boolean bAskQty);
}

public class FrameSplitTable extends VirtualUIFrame implements FrameColumnViewListener {
	private static int COLUMN_COUNT = 3;

	private TemplateBuilder m_oTemplateBuilder;

	private VirtualUIHorizontalList m_oHorizontalTableList;
	private VirtualUIFrame m_oFrameLeftContent;
	private FrameColumnView m_oFrameColumnView;
	private VirtualUIFrame m_oFrameFunction;
	private VirtualUIList m_oListFunction;
	
	private VirtualUIButton m_oButtonAskQty;
	private VirtualUIButton m_oButtonSameSeat;
	private VirtualUIButton m_oButtonClose;

	private List<VirtualUIFrame> m_oTableButtonList;
	private HashMap<Integer, String> m_oColumnTableList;
	private List<String> m_oTableList;
	private List<VirtualUIButton> m_oFunctionButtonList;
	private List<String> m_oFunctionList;

	private boolean m_bAskQty;

	private int m_iSelectedHorizontalListTable;
	private int m_iMaxSeats;

	private HashMap<String, String> m_oTableNameList;

	private static String UNSELECT_BACKGROUND_COLOR = "#E0E0E0";
	private static String UNSELECT_FOREGROUND_COLOR = "#015384";
	private static String UNSELECT_STROKE_COLOR = "#868686";
	private static String SELECT_BACKGROUND_COLOR = "#0461CD";
	private static String SELECT_FOREGROUND_COLOR = "#FFFFFF";
	private static String SELECT_STROKE_COLOR = "#005080";
	private static String TABLE_SELECT_BACKGROUND_COLOR = "#0461CD";
	private static String TABLE_SELECT_FOREGROUND_COLOR = "#FFFFFF";
	private static String TABLE_UNSELECT_BACKGROUND_COLOR = "#FFFFFF";
	private static String TABLE_UNSELECT_FOREGROUND_COLOR = "#0461CD";
	private static String TABLE_CLICK_STROKE_COLOR = "#0461CD";
	private static String TABLE_UNCLICKED_STROKE_COLOR = "#000461CD";

	private static String FUNCTION_TYPE_EQUAL_SPLIT = "equal_split";

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameSplitTableListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameSplitTableListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameSplitTableListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameSplitTable() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameSplitTableListener>();
		m_oColumnTableList = new HashMap<Integer, String>();
		m_oTableButtonList = new ArrayList<VirtualUIFrame>();
		m_oTableList = new ArrayList<String>();
		m_oFunctionButtonList = new ArrayList<VirtualUIButton>();
		m_oFunctionList = new ArrayList<String>();

		m_oTableNameList = new HashMap<String, String>();

		m_bAskQty = false;

		m_iSelectedHorizontalListTable = -1;

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraSplitTable.xml");

		// Left frame to contain column frame and table list
		m_oFrameLeftContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftContent, "fraLeftContent");
		this.attachChild(m_oFrameLeftContent);

		// SetMenu selected item list Horizontal List
		m_oHorizontalTableList = new VirtualUIHorizontalList();
		m_oTemplateBuilder.buildHorizontalList(m_oHorizontalTableList, "horListTable");
		m_oHorizontalTableList.setVisible(false);
		m_oFrameLeftContent.attachChild(m_oHorizontalTableList);

		// Set the no. of column
		m_oFrameColumnView = new FrameColumnView();
		m_oTemplateBuilder.buildFrame(m_oFrameColumnView, "columnView");
		m_oFrameColumnView.init(COLUMN_COUNT, false);
		m_oFrameColumnView.addListener(this);
		m_oFrameLeftContent.attachChild(m_oFrameColumnView);

		// Function Frame
		m_oFrameFunction = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameFunction, "fraFunction");
		this.attachChild(m_oFrameFunction);

		m_oListFunction = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListFunction, "listFunction");
		m_oFrameFunction.attachChild(m_oListFunction);
		initFunctionList();

		m_oButtonClose = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonClose, "btnClose");
		m_oButtonClose.setValue(AppGlobal.g_oLang.get()._("close"));
		m_oFrameFunction.attachChild(m_oButtonClose);

		// Ask Qty Button
		m_oButtonAskQty = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonAskQty, "btnAskQty");
		m_oButtonAskQty.setValue(AppGlobal.g_oLang.get()._("ask_quantity"));
		m_oFrameFunction.attachChild(m_oButtonAskQty);

		// Same Seat Button
		m_oButtonSameSeat = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSameSeat, "btnSameSeat");
		m_oButtonSameSeat.setValue(AppGlobal.g_oLang.get()._("same_seat"));
		m_oFrameFunction.attachChild(m_oButtonSameSeat);
	}

	public void addTableToHorizontalList(String sTable) {
		// Table Icon
		VirtualUIFrame oFrameTableButton = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameTableButton, "fraSelectedTable");
		oFrameTableButton.setEnabled(true);
		oFrameTableButton.allowClick(true);

		oFrameTableButton.setClickServerRequestBlockUI(true);

		VirtualUIImage oImageTableIcon = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(oImageTableIcon, "imgTableIcon");
		oImageTableIcon.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/floor_table_icon.png");
		oImageTableIcon.setTop(12);
		oImageTableIcon.setLeft(15);
		oImageTableIcon.setWidth(15);
		oImageTableIcon.setHeight(20);
		oImageTableIcon.setVisible(false);
		oFrameTableButton.attachChild(oImageTableIcon);

		VirtualUILabel oLabelTable = new VirtualUILabel();
		oLabelTable.setExist(true);
		oLabelTable.setWidth(oFrameTableButton.getWidth());
		oLabelTable.setHeight(oFrameTableButton.getHeight());
		oLabelTable.setLeft(45);

		oLabelTable.setTop(0);
		oLabelTable.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		oLabelTable.setForegroundColor("#FFFFFF");
		oLabelTable.setValue(sTable);
		oFrameTableButton.attachChild(oLabelTable);

		// Add the Table Text
		VirtualUILabel oLabelTableText = new VirtualUILabel();
		oLabelTableText.setExist(true);
		oLabelTableText.setWidth(oFrameTableButton.getWidth());
		oLabelTableText.setHeight(oFrameTableButton.getHeight());
		oLabelTableText.setLeft(10);
		oLabelTableText.setTop(0);
		oLabelTableText.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + ", "
				+ HeroActionProtocol.View.Attribute.TextAlign.LEFT);
		oLabelTableText.setForegroundColor("#666666");
		oLabelTableText.setValue(AppGlobal.g_oLang.get()._("table") + ": ");
		oFrameTableButton.attachChild(oLabelTableText);

		m_oHorizontalTableList.attachChild(oFrameTableButton);
		m_oTableButtonList.add(oFrameTableButton);
	}

	public void displayTableHorizontalList(boolean bShow) {
		if (bShow) {
			m_oHorizontalTableList.setVisible(true);
			m_oFrameColumnView.setTop(m_oHorizontalTableList.getTop() + m_oHorizontalTableList.getHeight());
		} else {
			m_oFrameColumnView.setTop(m_oHorizontalTableList.getTop());
			m_oFrameColumnView.setHeight(m_oFrameColumnView.getHeight() + m_oHorizontalTableList.getHeight());
			m_oFrameColumnView.extendColumnViewScrollAreaHeight(m_oFrameColumnView.getHeight());
			m_oFrameColumnView.updateColumnBackgroundHeight(1, m_oFrameColumnView.getHeight());
		}
	}

	public void initSectionList(int iSeatCount) {
		m_iMaxSeats = iSeatCount;
		// Initialize section list
		for (int j = 0; j <= iSeatCount; j++) {
			String sSectionName = AppGlobal.g_oLang.get()._("shared");
			if (j > 0)
				sSectionName = AppGlobal.g_oLang.get()._("seat") + " " + j;
			
			m_oFrameColumnView.addSection(j, sSectionName, true);
			
			for (int i = 0; i < COLUMN_COUNT; i++)
				m_oFrameColumnView.addSectionToElement(i, j, 0, "", false);
		}
	}

	public void addTable(int iColumnIndex, String sTable, String sTableName) {
		m_oFrameColumnView.updateColumnHeader(iColumnIndex, AppGlobal.g_oLang.get()._("table") + ": " + sTableName);
		m_oColumnTableList.put(iColumnIndex, sTable);
	}

	public void addItemToColumnView(int iColumnIndex, int iSectionId, FuncCheckItem oFuncCheckItem) {
		m_oFrameColumnView.addItemToElement(iColumnIndex, iSectionId, 0, oFuncCheckItem);
	}

	public void initFunctionList() {
		ArrayList<String> oOptionList = new ArrayList<String>();
		oOptionList.add(AppGlobal.g_oLang.get()._("send_check"));
		m_oFunctionList.add(AppGlobal.FUNC_LIST.send_check.name());
		oOptionList.add(AppGlobal.g_oLang.get()._("print_check"));
		m_oFunctionList.add(AppGlobal.FUNC_LIST.print_check.name());
		oOptionList.add(AppGlobal.g_oLang.get()._("equal_split"));
		m_oFunctionList.add(FUNCTION_TYPE_EQUAL_SPLIT);

		for (String sFunctionName : oOptionList) {
			VirtualUIButton oBtnFunction = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(oBtnFunction, "btnFunction");
			oBtnFunction.setValue(sFunctionName);
			m_oListFunction.attachChild(oBtnFunction);
			m_oFunctionButtonList.add(oBtnFunction);
		}
	}

	public void addTable(String sTable, String sTableName) {
		m_oTableList.add(sTable);
		m_oTableNameList.put(sTable, sTableName);
	}

	public List<String> getTableList() {
		return m_oTableList;
	}

	public int getColumnIndex(String sTable) {
		int iColumnIndex = -1;
		for (Entry<Integer, String> entry : m_oColumnTableList.entrySet()) {
			if (entry.getValue().equals(sTable)) {
				iColumnIndex = entry.getKey();
				break;
			}
		}

		return iColumnIndex;
	}

	public void updateTableListButtonColor(int iIndex) {
		String sTable = m_oTableList.get(iIndex);
		if (m_oColumnTableList.containsValue(sTable))
			return;

		resetAllTableListButtonColor();

		VirtualUIFrame oButtonSelectedTable = m_oTableButtonList.get(iIndex);
		oButtonSelectedTable.setStrokeColor(TABLE_CLICK_STROKE_COLOR);
		
		m_iSelectedHorizontalListTable = iIndex;
	}

	public void resetAllTableListButtonColor() {
		for (int i = 0; i < m_oTableButtonList.size(); i++) {
			VirtualUIFrame oButtonTable = m_oTableButtonList.get(i);
			oButtonTable.setStrokeColor(TABLE_UNCLICKED_STROKE_COLOR);
			String sTable = m_oTableList.get(i);
			if(m_oColumnTableList.containsValue(sTable)) {
				oButtonTable.setBackgroundColor(TABLE_SELECT_BACKGROUND_COLOR);
				oButtonTable.getChilds().get(1).setForegroundColor(TABLE_SELECT_FOREGROUND_COLOR);
				oButtonTable.getChilds().get(2).setForegroundColor(TABLE_SELECT_FOREGROUND_COLOR);
			} else {
				oButtonTable.setBackgroundColor(TABLE_UNSELECT_BACKGROUND_COLOR);
				oButtonTable.getChilds().get(1).setForegroundColor(TABLE_UNSELECT_FOREGROUND_COLOR);
				oButtonTable.getChilds().get(2).setForegroundColor(TABLE_UNSELECT_FOREGROUND_COLOR);
			}
		}
	}

	public void removeColumnViewAllItem(int iColumnIndex) {
		for (int i = 0; i <= m_iMaxSeats; i++)
			m_oFrameColumnView.removeAllItemFromElement(iColumnIndex, i);
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (iChildId == m_oButtonClose.getId()) {
			for (FrameSplitTableListener listener : listeners)
				listener.fraSplitTable_clickExit();
			
			bMatchChild = true;
		} else if (iChildId == m_oButtonAskQty.getId()) {
			if (m_bAskQty) {
				m_bAskQty = false;
				m_oButtonAskQty.setBackgroundColor(UNSELECT_BACKGROUND_COLOR);
				m_oButtonAskQty.setForegroundColor(UNSELECT_FOREGROUND_COLOR);
				m_oButtonAskQty.setStrokeColor(UNSELECT_STROKE_COLOR);
			} else {
				m_bAskQty = true;
				m_oButtonAskQty.setBackgroundColor(SELECT_BACKGROUND_COLOR);
				m_oButtonAskQty.setForegroundColor(SELECT_FOREGROUND_COLOR);
				m_oButtonAskQty.setStrokeColor(SELECT_STROKE_COLOR);
			}
			bMatchChild = true;
		}
		else { // click table horizontal list
			for (int i = 0; i < m_oTableButtonList.size(); i++) {
				VirtualUIFrame oButtonTable = m_oTableButtonList.get(i);
				if (iChildId == oButtonTable.getId()) {
					updateTableListButtonColor(i);
					bMatchChild = true;
					break;
				}
			}

			if (!bMatchChild) {
				for (int i = 0; i < m_oFunctionButtonList.size(); i++) {
					VirtualUIButton oButtonFunction = m_oFunctionButtonList.get(i);
					if (iChildId == oButtonFunction.getId()) {
						String sSelectedFunction = m_oFunctionList.get(i);
						if (sSelectedFunction.equals(AppGlobal.FUNC_LIST.send_check.name())) {
							for (FrameSplitTableListener listener : listeners) {
								listener.fraSplitTable_sendCheck();
								bMatchChild = true;
								break;
							}
						} else if (sSelectedFunction.equals(AppGlobal.FUNC_LIST.print_check.name())) {
							for (FrameSplitTableListener listener : listeners) {
								listener.fraSplitTable_printCheck();
								bMatchChild = true;
								break;
							}
						} else if (sSelectedFunction.equals(FUNCTION_TYPE_EQUAL_SPLIT)) {
							boolean bAskQty = false;
							TreeMap<String, Integer> oItemList = m_oFrameColumnView.getSelectedLine();
							if (oItemList.size() > 1 && m_bAskQty)
								bAskQty = true;

							for (Entry<String, Integer> entry : oItemList.entrySet()) {
								String sKey = entry.getKey();
								String split[] = sKey.split("_");
								int iColumnIndex = Integer.parseInt(split[0]);
								int iCourseIndex = Integer.parseInt(split[1]);
								int iSelectedSectionIndex = Integer.parseInt(split[2]);
								int iSelectedItemIndex = Integer.parseInt(split[3]);

								for (FrameSplitTableListener listener : listeners) {
									// Raise the event to parent
									listener.fraSplitTable_equalSplitItem(m_oColumnTableList.get(iColumnIndex),
											iCourseIndex, iSelectedItemIndex, bAskQty);
									bMatchChild = true;
									break;
								}
							}
							m_oFrameColumnView.deSelectAllLine();
						}
						break;
					}
				}
			}
		}

		return bMatchChild;
	}

	@Override
	public void frameColumnViewListener_clickColumnHeader(int iColumnIndex) {
		if (m_iSelectedHorizontalListTable == -1) {
			String sSelectedColumnTable = m_oColumnTableList.get(iColumnIndex);
			for (FrameSplitTableListener listener : listeners) {
				listener.fraSplitTable_clickColumnHeader(sSelectedColumnTable, true);
				break;
			}
			return;
		}
		
		// remove all items display
		this.removeColumnViewAllItem(iColumnIndex);

		String sSelectedTable = m_oTableList.get(m_iSelectedHorizontalListTable);
		String sSelectedTableName = m_oTableNameList.get(sSelectedTable);
		m_oFrameColumnView.updateColumnHeader(iColumnIndex,
				AppGlobal.g_oLang.get()._("table") + ": " + sSelectedTableName);

		// update 3 column tables index
		m_oColumnTableList.put(iColumnIndex, sSelectedTable);

		// add items of new selected table
		for (FrameSplitTableListener listener : listeners) {
			listener.fraSplitTable_clickColumnHeader(sSelectedTable, false);
			break;
		}

		// reset table color in horizontal table list
		resetAllTableListButtonColor();

		// Reset to no table is selected
		m_iSelectedHorizontalListTable = -1;
	}

	@Override
	public void frameColumnViewListener_clickPlaceButton(int iTargetColumnIndex, int iTargetSectionIndex) {
		if (!m_oColumnTableList.containsKey(iTargetColumnIndex))
			return;

		// Loop the selected lines
		TreeMap<String, Integer> oItemList = m_oFrameColumnView.getSelectedLine();
		boolean bAskQty = false;
		if (oItemList.size() == 1 && m_bAskQty)
			bAskQty = true;
		for (Entry<String, Integer> entry : oItemList.entrySet()) {
			String sKey = entry.getKey();
			String split[] = sKey.split("_");
			int iColumnIndex = Integer.parseInt(split[0]);
			int iCourseIndex = Integer.parseInt(split[1]);
			int iSelectedSectionIndex = Integer.parseInt(split[2]);
			int iSelectedItemIndex = Integer.parseInt(split[3]);

			if (iColumnIndex == iTargetColumnIndex && iCourseIndex == iTargetSectionIndex) // same check and same section, no need to do splitting
				continue;

			if (m_oColumnTableList.containsKey(iColumnIndex)) {
				String sFromTable = m_oColumnTableList.get(iColumnIndex);
				String sToTable = m_oColumnTableList.get(iTargetColumnIndex);

				for (FrameSplitTableListener listener : listeners) {
					listener.frameCheckFunction_splitItem(sFromTable, iCourseIndex, iSelectedItemIndex, sToTable,
							iTargetSectionIndex, null, bAskQty);
				}
			}
		}

		// De-select all items
		m_oFrameColumnView.deSelectAllLine();
	}

	@Override
	public boolean frameColumnViewListener_isMinimumChargeItem(FuncCheckItem oFuncCheckItem) {
		return false;
	}
}
