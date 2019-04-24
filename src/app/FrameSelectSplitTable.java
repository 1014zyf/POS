package app;

import java.util.ArrayList;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameSelectSplitTableListener {
	void fraSelectSplitTable_clickBasketTable(int iSectionId, int iItemIndex, int iFieldIndex, String sNote);
    void fraSelectSplitTable_clickExit();
    void fraSelectSplitTable_clickNext();
	void fraSelectSplitTable_addNewTable();
}

public class FrameSelectSplitTable extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;

	private FrameTitleHeader m_oFrameTitleHeader;

	private FrameCommonBasket m_oFrameAvailableTableList;
	private VirtualUIFrame m_oFrameLeftContent;
	private VirtualUIFrame m_oFrameRightContent;
//KingsleyKwan20171020ByRay -- start
private VirtualUIFrame m_oTitleUnderLine;
//KingsleyKwan20171020ByRay -- end
	private FrameCommonBasket m_oFrameSelectedTableList;
	private VirtualUIButton m_oButtonAddTable;
	
	private VirtualUIButton m_oButtonNext;

	public static String BASKET_TYPE_AVAILABLE = "a";
	public static String BASKET_TYPE_SELECTED = "s";

	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameSelectSplitTableListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameSelectSplitTableListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameSelectSplitTableListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
	public FrameSelectSplitTable(){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameSelectSplitTableListener>();

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraSelectSplitTable.xml");
		
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(true);
		m_oFrameTitleHeader.addListener(this);
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("split_table"));
		this.attachChild(m_oFrameTitleHeader);
		
		// Left Content
		m_oFrameLeftContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftContent, "fraLeftContent");
		this.attachChild(m_oFrameLeftContent);
//KingsleyKwan20171020ByRay -- start
		//Create Tab Name label
		VirtualUILabel lblLeftPageTab = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(lblLeftPageTab, "lblLeftPageTab");
		lblLeftPageTab.setValue(AppGlobal.g_oLang.get()._("available_tables"));
		//Create single Tab base frame
		VirtualUIFrame fraLeftPageTabBase = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(fraLeftPageTabBase, "fraLeftPageTabBase");
		fraLeftPageTabBase.attachChild(lblLeftPageTab);
		m_oFrameLeftContent.attachChild(fraLeftPageTabBase);
//KingsleyKwan20171020ByRay -- end
		VirtualUIFrame oFrameLeftPanelPageSeparator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameLeftPanelPageSeparator, "fraLeftPageTabSeparator");
//JohnLiu 02112017 -- start
		oFrameLeftPanelPageSeparator.setVisible(false);
//JohnLiu 02112017 -- end
		m_oFrameLeftContent.attachChild(oFrameLeftPanelPageSeparator);
		
		// Available Table List
		m_oFrameAvailableTableList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFrameAvailableTableList, "fraAvailableTableList");
		m_oFrameAvailableTableList.init();
		m_oFrameAvailableTableList.addListener(this);
		m_oFrameLeftContent.attachChild(m_oFrameAvailableTableList);

		// Right Content
		m_oFrameRightContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameRightContent, "fraRightContent");
		this.attachChild(m_oFrameRightContent);
//KingsleyKwan20171020ByRay -- start
		//Create Tab Name label
		VirtualUILabel lblRightPageTab = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(lblRightPageTab, "lblRightPageTab");
		lblRightPageTab.setValue(AppGlobal.g_oLang.get()._("selected_tables"));
		
		//Create single Tab base frame
		VirtualUIFrame fraRightPageTabBase = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(fraRightPageTabBase, "fraRightPageTabBase");
		fraRightPageTabBase.attachChild(lblRightPageTab);
		m_oFrameRightContent.attachChild(fraRightPageTabBase);
//KingsleyKwan20171020ByRay -- end	
		
		// Selected Table List
		m_oFrameSelectedTableList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFrameSelectedTableList, "fraSelectedTableList");
		m_oFrameSelectedTableList.init();
		m_oFrameSelectedTableList.addListener(this);
		m_oFrameRightContent.attachChild(m_oFrameSelectedTableList);
		
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
//KingsleyKwan20171020ByRay -- start
		iFieldWidths.add(99);
		sFieldValues.add(AppGlobal.g_oLang.get()._("check_no"));
		iFieldWidths.add(99);
		sFieldValues.add(AppGlobal.g_oLang.get()._("table"));
		iFieldWidths.add(88);
		sFieldValues.add(AppGlobal.g_oLang.get()._("cover"));
		iFieldWidths.add(88);
		sFieldValues.add(AppGlobal.g_oLang.get()._("open_time"));
		iFieldWidths.add(47);
		sFieldValues.add(AppGlobal.g_oLang.get()._("total"));
		m_oFrameAvailableTableList.addHeader(iFieldWidths, sFieldValues);
		m_oFrameAvailableTableList.setHeaderFormat(21, 15, "0,0,0,4");
		m_oFrameAvailableTableList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		m_oFrameSelectedTableList.addHeader(iFieldWidths, sFieldValues);
		m_oFrameSelectedTableList.setHeaderFormat(21, 15, "0,0,0,4");
		m_oFrameSelectedTableList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
//RayHuen20171103 -----Start Set BottomUnderline invisible -------
		m_oFrameSelectedTableList.setBottomUnderlineVisible(false);
//RayHuen20171103 -----End Set BottomUnderline invisible -------	
//KingsleyKwan20171020ByRay -- end
		// Add Table button
    	m_oButtonAddTable = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonAddTable, "butAddTable");
		m_oButtonAddTable.setValue(AppGlobal.g_oLang.get()._("add_new_table"));
		m_oFrameRightContent.attachChild(m_oButtonAddTable);
    	
		// Next button
    	m_oButtonNext = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonNext, "butNext");
		m_oButtonNext.setValue(AppGlobal.g_oLang.get()._("confirm"));
		m_oFrameRightContent.attachChild(m_oButtonNext);
	}
//KingsleyKwan20171020ByRay -- start  	
	public void init(int iItemIndex, String sCheckNo, String sTableNo ,String sGuest, String sOpenTime, String sCheckTotal) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		iFieldWidths.add(99);
		sFieldValues.add(sCheckNo);
		sFieldAligns.add("");
		iFieldWidths.add(99);
		sFieldValues.add(sTableNo);
		sFieldAligns.add("");
		iFieldWidths.add(88);
		sFieldValues.add(sGuest);
		sFieldAligns.add("");
		iFieldWidths.add(83);
		sFieldValues.add(sOpenTime);
		sFieldAligns.add("");
		iFieldWidths.add(77);
		sFieldValues.add(sCheckTotal);
// Edit by King Cheung 2017-11-02
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
		m_oFrameAvailableTableList.addItem(0, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
		m_oFrameAvailableTableList.setFieldTextSize(0, iItemIndex, 0, 15);
		m_oFrameAvailableTableList.setFieldTextSize(0, iItemIndex, 1, 15);
		m_oFrameAvailableTableList.setFieldTextSize(0, iItemIndex, 2, 15);
		m_oFrameAvailableTableList.setFieldTextSize(0, iItemIndex, 3, 15);
		m_oFrameAvailableTableList.setFieldTextSize(0, iItemIndex, 4, 15);
		m_oFrameAvailableTableList.setFieldPadding(0, iItemIndex, 0, "10,0,0,4");
		m_oFrameAvailableTableList.setFieldPadding(0, iItemIndex, 1, "10,0,0,4");
		m_oFrameAvailableTableList.setFieldPadding(0, iItemIndex, 2, "10,0,0,4");
		m_oFrameAvailableTableList.setFieldPadding(0, iItemIndex, 3, "10,0,0,4");
		m_oFrameAvailableTableList.setFieldPadding(0, iItemIndex, 4, "10,0,0,4");
		for(int j = 0; j < iFieldWidths.size(); j++) {
			m_oFrameAvailableTableList.setFieldClickServerRequestNote(0, iItemIndex, j, BASKET_TYPE_AVAILABLE);
		}
	}

	public void addSelectedTable(int iItemIndex, String sCheckNo, String sTableNo, String sGuest, String sOpenTime, String sCheckTotal) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		iFieldWidths.add(99);
		sFieldValues.add(sCheckNo);
		sFieldAligns.add("");
		iFieldWidths.add(99);
		sFieldValues.add(sTableNo);
		sFieldAligns.add("");
		iFieldWidths.add(88);
		sFieldValues.add(sGuest);
		sFieldAligns.add("");
		iFieldWidths.add(83);
		sFieldValues.add(sOpenTime);
		sFieldAligns.add("");
		iFieldWidths.add(77);
		sFieldValues.add(sCheckTotal);
// Edit by King Cheung 2017-11-02
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
		m_oFrameSelectedTableList.addItem(0, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
		m_oFrameSelectedTableList.setFieldTextSize(0, iItemIndex, 0, 15);
		m_oFrameSelectedTableList.setFieldTextSize(0, iItemIndex, 1, 15);
		m_oFrameSelectedTableList.setFieldTextSize(0, iItemIndex, 2, 15);
		m_oFrameSelectedTableList.setFieldTextSize(0, iItemIndex, 3, 15);
		m_oFrameSelectedTableList.setFieldTextSize(0, iItemIndex, 4, 15);
		m_oFrameSelectedTableList.setFieldPadding(0, iItemIndex, 0, "10,0,0,5");
		m_oFrameSelectedTableList.setFieldPadding(0, iItemIndex, 1, "10,0,0,5");
		m_oFrameSelectedTableList.setFieldPadding(0, iItemIndex, 2, "10,0,0,5");
		m_oFrameSelectedTableList.setFieldPadding(0, iItemIndex, 3, "10,0,0,5");
		m_oFrameSelectedTableList.setFieldPadding(0, iItemIndex, 4, "10,0,0,5");
		for(int j = 0; j < iFieldWidths.size(); j++) {
			m_oFrameSelectedTableList.setFieldClickServerRequestNote(0, iItemIndex, j, BASKET_TYPE_SELECTED);
		}
	}
//KingsleyKwan20171020ByRay -- end
	public void removeSelectedTable(int iItemIndex) {
		m_oFrameSelectedTableList.removeItem(0, iItemIndex);
	}

	public void setAvailableTableRowColor(int iSectionId, int iItemIndex, String sBackgroundColor) {
		m_oFrameAvailableTableList.setAllFieldsForegroundColor(iSectionId, iItemIndex, sBackgroundColor);
	}
	
	public void updateSelectedTableCover(int iItemIndex, int iCover) {
		m_oFrameSelectedTableList.setFieldValue(0, iItemIndex, 2, String.valueOf(iCover));
	}

    @Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (iChildId == m_oButtonAddTable.getId()) {
        	for (FrameSelectSplitTableListener listener : listeners) {
        		// Raise the event to parent
        		listener.fraSelectSplitTable_addNewTable();
            }
        	bMatchChild = true;
		} else if (iChildId == m_oButtonNext.getId()) {
        	for (FrameSelectSplitTableListener listener : listeners) {
        		// Raise the event to parent
        		listener.fraSelectSplitTable_clickNext();
            }
        	bMatchChild = true;
		}
		
        return bMatchChild;
    }

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId,
			String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex,
			int iItemIndex, int iFieldIndex, String sNote) {
    	for (FrameSelectSplitTableListener listener : listeners) {
    		// Raise the event to parent
    		listener.fraSelectSplitTable_clickBasketTable(iSectionIndex, iItemIndex, iFieldIndex, sNote);
        }
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId,
			int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
	}

	@Override
	public void FrameTitleHeader_close() {
		for (FrameSelectSplitTableListener listener : listeners) {
			// Raise the event to parent
			listener.fraSelectSplitTable_clickExit();
		}
	}
}
