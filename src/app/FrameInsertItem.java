package app;

import java.util.ArrayList;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIFrame;

/** interface for the listeners/observers callback method */
interface FrameInsertItemListener {
	void frameInsertItem_selectedCell(int iSectionIdx, int iItemIdx);
	void frameInsertItem_clickCancel();
}

public class FrameInsertItem extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener {
	public enum CELL_TYPE{item, insert_tab, selected_item};
	
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oFrameTitleHeader;
	private FrameCommonBasket m_oFrameItemList;
	private VirtualUIFrame m_oFrameSelectedItem;
	private VirtualUIFrame m_oFrameInsertTab;
	private VirtualUIFrame m_oFrameOtherItem;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameInsertItemListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameInsertItemListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameInsertItemListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FrameInsertItem() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameInsertItemListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraInsertItem.xml");

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(true);
		m_oFrameTitleHeader.addListener(this);
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("insert_item"));
		this.attachChild(m_oFrameTitleHeader);

		// Result list
		m_oFrameItemList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFrameItemList, "fraItemList");
		m_oFrameItemList.init();
		m_oFrameItemList.setHeaderVisible(false);
		m_oFrameItemList.addListener(this);
		this.attachChild(m_oFrameItemList);
		
		// Selected Item
		m_oFrameSelectedItem = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameSelectedItem, "fraSelectedItem");
		this.attachChild(m_oFrameSelectedItem);
		
		// Insert Tag
		m_oFrameInsertTab = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameInsertTab, "fraInsertTab");
		this.attachChild(m_oFrameInsertTab);
		
		// Other Item
		m_oFrameOtherItem = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameOtherItem, "fraOtherItem");
		this.attachChild(m_oFrameOtherItem);
	}
	
	public void addSection(int iSectionId, String[] sSectionTitle, boolean bIsShow) {
		m_oFrameItemList.addSection(iSectionId, sSectionTitle, bIsShow);
	}
	
	public void addItem(int iSectionId, int iItemIndex, String sCellTitle, CELL_TYPE eCellType) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		
		// Item Desc
		iFieldWidths.add(m_oFrameItemList.getWidth());
		sFieldValues.add(sCellTitle);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		
		m_oFrameItemList.addItem(iSectionId, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
		
		if (eCellType == CELL_TYPE.selected_item)
			m_oFrameItemList.setFieldBackgroundColor(iSectionId, iItemIndex, 0, m_oFrameSelectedItem.getBackgroundColor());
		else if (eCellType == CELL_TYPE.insert_tab) {
			m_oFrameItemList.setFieldBackgroundColor(iSectionId, iItemIndex, 0, m_oFrameInsertTab.getBackgroundColor());
			m_oFrameItemList.setFieldForegroundColor(iSectionId, iItemIndex, 0, m_oFrameInsertTab.getForegroundColor());
		}
		else
			m_oFrameItemList.setFieldBackgroundColor(iSectionId, iItemIndex, 0, m_oFrameOtherItem.getBackgroundColor());
	}
	
	public int getItemCount(int iSectionId) {
		return m_oFrameItemList.getItemCount(iSectionId);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		return bMatchChild;
	}
	
	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId,
			String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		for (FrameInsertItemListener listener : listeners) {
			// Raise the event to parent
			listener.frameInsertItem_selectedCell(iSectionIndex, iItemIndex);
			break;
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
		for (FrameInsertItemListener listener : listeners) {
			// Raise the event to parent
			listener.frameInsertItem_clickCancel();
		}
	}

}
