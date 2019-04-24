package app;

import java.util.ArrayList;
import java.util.HashMap;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FramePreOrderListListener {
	void framePreOrderList_clickBack();
	void framePreOrderList_clickPreOrder(int iPreOrderIndex);
	void framePreOrderList_updatePreOrderList(boolean bUsedRecord);
}

public class FramePreOrderList extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener{
	TemplateBuilder m_oTemplateBuilder;
	
	//private VirtualUIFrame m_oFrameButtonClose;
	private VirtualUILabel m_oLabelTableHeader;
	private FrameTitleHeader m_oTitleHeader;
	private VirtualUILabel m_oLabelNewListTag;
	private VirtualUILabel m_oLabelUsedListTag;
	private VirtualUIFrame m_oFrameTagUnderline;
	private FrameCommonBasket m_oBasketPreOrderList;
		
	private HashMap<String, Integer> m_oBasketColumnWidths;
	private int m_iBasketRowHeight;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FramePreOrderListListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FramePreOrderListListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FramePreOrderListListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FramePreOrderList() {
		m_oTemplateBuilder = new TemplateBuilder();
		m_oTemplateBuilder.loadTemplate("fraPreOrderList.xml");
		
		listeners = new ArrayList<FramePreOrderListListener>();
		m_oBasketColumnWidths = new HashMap<String, Integer>();
		
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oBasketColumnWidths.put("number", 50);
			m_oBasketColumnWidths.put("createTime", 150);
			m_oBasketColumnWidths.put("refno", 140);
			m_oBasketColumnWidths.put("takeout", 100);
			m_iBasketRowHeight = 50;
		}else {
			m_oBasketColumnWidths.put("number", 84);
			m_oBasketColumnWidths.put("createTime", 362);
			m_oBasketColumnWidths.put("refno", 345);
			m_oBasketColumnWidths.put("takeout", 185);
			m_iBasketRowHeight = 36;
		}
	}
	
	public void init(String sTable, String sTableExtension) {
		// Load child elements from template
		/////////////////////////////////////////////////////////////////
		
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		m_oTitleHeader.addListener(this);
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("pre_order_list"));
		this.attachChild(m_oTitleHeader);
		
		// Table header
		m_oLabelTableHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableHeader, "lblTableHeader");
		m_oLabelTableHeader.setValue(AppGlobal.g_oLang.get()._("table")+": "+AppGlobal.g_oFuncOutlet.get().getTableNameWithTableNo(sTable, sTableExtension)[AppGlobal.g_oCurrentLangIndex.get()-1]);
		this.attachChild(m_oLabelTableHeader);
		
		// New list tag
		m_oLabelNewListTag = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelNewListTag, "lblNewListTag");
		m_oLabelNewListTag.setValue(AppGlobal.g_oLang.get()._("new_pre_order"));
		m_oLabelNewListTag.allowClick(true);
		this.attachChild(m_oLabelNewListTag);
		
		// Used list tag
		m_oLabelUsedListTag = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelUsedListTag, "lblUsedListTag");
		m_oLabelUsedListTag.setValue(AppGlobal.g_oLang.get()._("used_pre_order"));
		m_oLabelUsedListTag.allowClick(true);
		this.attachChild(m_oLabelUsedListTag);
		
		// Tag underline
		m_oFrameTagUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTagUnderline, "fraTagUnderline");
		this.attachChild(m_oFrameTagUnderline);
		
		// Pre Order List
		m_oBasketPreOrderList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oBasketPreOrderList, "fraPreOrderList");
		m_oBasketPreOrderList.init();
		m_oBasketPreOrderList.addListener(this);
		this.attachChild(m_oBasketPreOrderList);
		
		// Add header to list
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		iFieldWidths.add(m_oBasketColumnWidths.get("number"));
		sFieldValues.add(AppGlobal.g_oLang.get()._("number"));
		iFieldWidths.add(m_oBasketColumnWidths.get("createTime"));
		sFieldValues.add(AppGlobal.g_oLang.get()._("create_time"));
		iFieldWidths.add(m_oBasketColumnWidths.get("refno"));
		sFieldValues.add(AppGlobal.g_oLang.get()._("reference_no"));
		iFieldWidths.add(m_oBasketColumnWidths.get("takeout"));
		sFieldValues.add(AppGlobal.g_oLang.get()._("takeout"));
		m_oBasketPreOrderList.addHeader(iFieldWidths, sFieldValues);
		m_oBasketPreOrderList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
	}
	
	public void addPreOrderToList(int iSectionId, int iItemIndex, HashMap<String, String> oPreOrderInfo) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		
		// No.
		iFieldWidths.add(m_oBasketColumnWidths.get("number"));
		sFieldValues.add(String.valueOf((iItemIndex+1)));
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.LEFT);
		
		// Create Time
		iFieldWidths.add(m_oBasketColumnWidths.get("createTime"));
		sFieldValues.add(oPreOrderInfo.get("createTime"));
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.LEFT);
		
		// Reference No.
		iFieldWidths.add(m_oBasketColumnWidths.get("refno"));
		sFieldValues.add(oPreOrderInfo.get("refno"));
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.LEFT);
		
		// Takeout
		iFieldWidths.add(m_oBasketColumnWidths.get("takeout"));
		sFieldValues.add(oPreOrderInfo.get("takeout"));
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.LEFT);
		

		m_oBasketPreOrderList.addItem(iSectionId, iItemIndex, m_iBasketRowHeight, iFieldWidths, sFieldValues, sFieldAligns, null, null);
	}
	
	public void cleanupPreOrderList() {
		m_oBasketPreOrderList.clearAllSections();
	}
	
	public void switchTag(int iTagIndex) {
		switch (iTagIndex) {
		case 2:
			m_oLabelNewListTag.setForegroundColor("#999999");
			m_oLabelUsedListTag.setForegroundColor("#0055B8");
			break;
		default:
		case 1:
			m_oLabelNewListTag.setForegroundColor("#0055B8");
			m_oLabelUsedListTag.setForegroundColor("#999999");
			break;
		}
	}
	
	/***********************/
	/*  Override Function  */
	/***********************/
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (iChildId == m_oLabelNewListTag.getId()) {
			switchTag(1);
			
			for(FramePreOrderListListener listener: listeners) {
				// Raise the event to parent
				listener.framePreOrderList_updatePreOrderList(false);
				break;
			}
			bMatchChild = true;
		}else if (iChildId == m_oLabelUsedListTag.getId()) {
			switchTag(2);
			
			for(FramePreOrderListListener listener: listeners) {
				// Raise the event to parent
				listener.framePreOrderList_updatePreOrderList(true);
				break;
			}
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
	
	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
		
	}
	
	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSeciontId, int iItemIndex, int iFieldIndex, String sNote) {
		for(FramePreOrderListListener listener: listeners) {
			// Raise the event to parent
			listener.framePreOrderList_clickPreOrder(iItemIndex);
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
		for(FramePreOrderListListener listener: listeners) {
			// Raise the event to parent
			listener.framePreOrderList_clickBack();
			break;
		}
	}	
	
}
