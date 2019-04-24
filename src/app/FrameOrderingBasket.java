 package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import om.PosTaxScType;
import om.PosDisplayPanelZone;
import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FrameOrderingBasketListener {
	void frameOrderingBasketSection_SectionClicked(int iSectionId, String sNote);
	void frameOrderingBasketCell_QtyClicked(int iSectionId, int iItemIndex, String sNote);
	String frameOrderingBasketCell_TempQtyClicked(int iSectionId, int iItemIndex, String sNote, String sOriQty);
	void frameOrderingBasketCell_ReceiveQtyClicked(int iSectionId, int iItemIndex, String sNote);
	void frameOrderingBasketCell_DescClicked(int iSectionId, int iItemIndex, String sNote);
	void frameOrderingBasketCell_DescLongClicked(int iSectionId, int iItemIndex, String sNote);
	void frameOrderingBasketCell_PriceClicked(int iSectionId, int iItemIndex, String sNote);
	void frameOrderingBasketCell_DeleteClicked(int iSectionId, int iItemIndex, String sNote);
	void frameOrderingBasketCell_AddQtyClicked(int iSectionId, int iItemIndex, String sNote);
	boolean frameOrderingBasketCell_AddQtyClickedForOldItem(int iSectionId, int iItemIndex, String sNote, BigDecimal dNewQty);
	void frameOrderingBasketCell_MinusQtyClicked(int iSectionId, int iItemIndex, String sNote);
	boolean frameOrderingBasketCell_MinusQtyClickedForOldItem(int iSectionId, int iItemIndex, String sNote, BigDecimal dNewQty);
	void frameOrderingBasketChildItemCell_QtyClicked(int iSectionId, int iItemIndex, int iChildItemIndex, String sNote);
	void frameOrderingBasketChildItemCell_DescClicked(int iSectionId, int iItemIndex, int iChildItemIndex, String sNote);
	void frameOrderingBasketChildItemCell_PriceClicked(int iSectionId, int iItemIndex, int iChildItemIndex, String sNote);
	void frameOrderingBasketModifierCell_QtyClicked(int iSectionId, int iItemIndex, int iModifierIndex, String sNote);
	void frameOrderingBasketModifierCell_DescClicked(int iSectionId, int iItemIndex, int iModifierIndex, String sNote);
	void frameOrderingBasketModifierCell_PriceClicked(int iSectionId, int iItemIndex, int iModifierIndex, String sNote);
	void frameOrderingBasket_UpdateBasket();
}

public class FrameOrderingBasket<T> extends VirtualUIFrame 
		implements FrameOrderingBasketSectionListener, FrameOrderingBasketExtraInfoListener{
	TemplateBuilder m_oTemplateBuilder;
	private int m_iCurrentSelectedSectionId = 0;
	private String m_sCurrentSelectedNewQuantity;
	private VirtualUIList m_oOrderingBasketList;
	private FrameOrderingBasketExtraInfo m_oOrderingBasketExtraInfoFrame;
	private boolean m_bAlwaysResetOrderingBasketExtraInfoWindowSize = false;
	private VirtualUILabel m_oLabelLoading;
	private VirtualUILabel m_oLabelOrderType;
	private VirtualUIFrame m_oSelectSection;
	private VirtualUIFrame m_oHeaderFrame;
	private VirtualUIFrame m_oUpperlineFrame;
	private VirtualUIFrame m_oUnderlineFrame;
	private HashMap<Integer, VirtualUILabel> m_oLabelHeaders;
	private ArrayList<FrameOrderingBasketSection> m_oSectionList;
	private PosDisplayPanelZone m_oPosDisplayPanelZone;
	// Toggle mode of ordering basket in consolidate item
	private String m_sOrderingBasketToggleMode;
	private boolean m_bShowPantryMessage;
	
	public final static int SELECT_TYPE_ALL = 0;
	public final static int SELECT_TYPE_NEW_ITEM = 1;
	public final static int SELECT_TYPE_OLD_ITEM = 2;
	
	public final static int SHOW_TYPE_HIDDEN = 0;
	public final static int SHOW_TYPE_SELECT = 1;
	public final static int SHOW_TYPE_UNSELECT = 2;
	public final static int SHOW_TYPE_UNSELECTWITHQUANTITY = 3;
	public final static int SHOW_TYPE_SELECTWITHADDMINUS = 4;
	
	// Toggle mode of ordering basket in consolidate item
	public static String ORDERING_BASKET_MODE_SHOW_NORMAL = "show_normal";
	public static String ORDERING_BASKET_MODE_SHOW_CONSOLIDATE_ITEMS = "show_consolidate_items";
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameOrderingBasketListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameOrderingBasketListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameOrderingBasketListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public int getSelectedSectionId() {
		return this.m_iCurrentSelectedSectionId;
	}

	public String getSelectedNewQuantity() {
		return this.m_sCurrentSelectedNewQuantity;
	}
	
	public void init(PosDisplayPanelZone oOrderingBasketDisplayPanelZone, String sOrderingBasketToggleMode) {
		// Determine which basket mode
		this.m_sOrderingBasketToggleMode = sOrderingBasketToggleMode;
		
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameOrderingBasketListener>();
		m_oSectionList = new ArrayList<FrameOrderingBasketSection>();
		m_bShowPantryMessage = false;
		m_oPosDisplayPanelZone = oOrderingBasketDisplayPanelZone;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraOrderingBasket.xml");
		
		m_oUpperlineFrame = new VirtualUIFrame();
		m_oUnderlineFrame = new VirtualUIFrame();
		m_oLabelOrderType = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOrderType, "lblOrderType");
		m_oLabelOrderType.setEnabled(true);
		m_oLabelOrderType.allowClick(false);
		m_oLabelOrderType.setValue(AppGlobal.g_oLang.get()._("dine_in", ""));
		
		m_oLabelHeaders = new HashMap<Integer, VirtualUILabel>();
		/////////////////////////////////////////////////////////////////
		
		// Load child elements from template
		m_oOrderingBasketList = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oOrderingBasketList, "listOrderingBasket");
		
		if(m_oPosDisplayPanelZone.isDisplayCheckOrderingType()){
			this.attachChild(m_oLabelOrderType);
			m_oUpperlineFrame = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(m_oUpperlineFrame, "fraUpperline");
			m_oUpperlineFrame.setWidth(this.getWidth());
			m_oUpperlineFrame.setTop(m_oLabelOrderType.getTop() + m_oLabelOrderType.getHeight());
			this.attachChild(m_oUpperlineFrame);
			m_oOrderingBasketList.setTop(m_oUpperlineFrame.getTop()+m_oUpperlineFrame.getHeight());
		}
		m_oOrderingBasketList.setWidth(this.getWidth());
		m_oOrderingBasketList.setHeight(this.getHeight());
		this.attachChild(m_oOrderingBasketList);
		
		if(m_oPosDisplayPanelZone.isDisplayBasketHeader()){
			ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
			ArrayList<String> sFieldValues = new ArrayList<String>();
			TemplateBuilder oTemplateBuilder = new TemplateBuilder();
			oTemplateBuilder.loadTemplate("fraOrderingBasketCell.xml");
			VirtualUILabel oChecking = new VirtualUILabel();
			oTemplateBuilder.buildLabel(oChecking, "itemSeq");
			int iOffset = 0;
			
			
			if(m_oPosDisplayPanelZone.isDisplayItemSequence()){
				iFieldWidths.add(oChecking.getWidth());
				sFieldValues.add("");
			}
			iOffset += oChecking.getWidth();
			iFieldWidths.add(180);
			sFieldValues.add(AppGlobal.g_oLang.get()._("description"));
			iOffset += 180;
			oTemplateBuilder.buildLabel(oChecking, "lblQuantity");
			iFieldWidths.add(oChecking.getLeft() - iOffset - 34);
			iOffset = 225;
			sFieldValues.add("");
			iFieldWidths.add(100);
			iOffset += 20;
			sFieldValues.add(AppGlobal.g_oLang.get()._("quantity"));
			this.addHeader(iFieldWidths, sFieldValues);
		}

		// Loading label
		m_oLabelLoading = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelLoading, "lblLoading");
		m_oLabelLoading.setValue(AppGlobal.g_oLang.get()._("loading", "..."));
		m_oLabelLoading.setVisible(false);
		this.attachChild(m_oLabelLoading);
		// Select Section
		m_oSelectSection = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oSelectSection, "fraOrderingBasketSelectSection");
		m_oSelectSection.setVisible(false);
		this.attachChild(m_oSelectSection);
		// Add update basket timer
		addUpdateBasketTimer();
		
		// Current selected section id (0 -> shared)
		m_iCurrentSelectedSectionId = 0;
	}
	
	public void createExtraInfoSection(ArrayList<JSONObject> oOrderingBasketExtraInfoContentList) {
		// Create / update the ordering basket extra info frame detail
		if (m_oOrderingBasketExtraInfoFrame == null) {
			m_oOrderingBasketExtraInfoFrame = new FrameOrderingBasketExtraInfo();
			m_oTemplateBuilder.buildFrame(m_oOrderingBasketExtraInfoFrame, "fraOrderingBasketExtraInfoSection");
			m_oOrderingBasketExtraInfoFrame.setWidth(this.getWidth());
			m_oOrderingBasketExtraInfoFrame.init(oOrderingBasketExtraInfoContentList);
			if (m_oOrderingBasketExtraInfoFrame.getListSize() > 1)
				m_bAlwaysResetOrderingBasketExtraInfoWindowSize = AppGlobal.g_oFuncStation.get()
						.isAlwaysResetOrderingBasketExtraInfoWindowSize();
			m_oOrderingBasketExtraInfoFrame.addListener(this);
			this.attachChild(m_oOrderingBasketExtraInfoFrame);
		} else
			m_oOrderingBasketExtraInfoFrame.updateList(oOrderingBasketExtraInfoContentList);

		// Reset the ordering basket height and top
		m_oOrderingBasketList.setTop(m_oOrderingBasketExtraInfoFrame.getTop() + m_oOrderingBasketExtraInfoFrame.getHeight());
		m_oOrderingBasketList.setHeight(this.getHeight() - m_oOrderingBasketExtraInfoFrame.getHeight());
		
		if (m_oPosDisplayPanelZone.isDisplayCheckOrderingType()) {
			m_oLabelOrderType.setTop(m_oOrderingBasketList.getTop());
			this.attachChild(m_oLabelOrderType);
			m_oUpperlineFrame = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(m_oUpperlineFrame, "fraUpperline");
			m_oUpperlineFrame.setWidth(this.getWidth());
			m_oUpperlineFrame.setTop(m_oLabelOrderType.getTop() + m_oLabelOrderType.getHeight());
			this.attachChild(m_oUpperlineFrame);
			m_oOrderingBasketList.setTop(m_oUpperlineFrame.getTop() + m_oUpperlineFrame.getHeight());
		}
	}
	
	/**
	 * @param iSectionId
	 * @param sSectionTitle
	 * @param bIsShow
	 */
	public void addSection(int iSectionId, String[] sSectionTitle, boolean bIsShow) {
		
		// Hide the loading
		if(m_oLabelLoading.getVisible())
			m_oLabelLoading.setVisible(false);
		
		FrameOrderingBasketSection oFrameSection = new FrameOrderingBasketSection();
		m_oTemplateBuilder.buildFrame(oFrameSection, "fraOrderingBasketSection");
		oFrameSection.setWidth(this.getWidth());
		oFrameSection.init(m_oOrderingBasketList, m_oPosDisplayPanelZone, m_sOrderingBasketToggleMode);
		oFrameSection.addListener(this);
		
		oFrameSection.setTitle(sSectionTitle);
		oFrameSection.setEnabled(true);
		oFrameSection.setVisible(bIsShow);
		
		// Get the view sequence for this new add section
		int iViewSeq = 0;
		int i = 0;
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if(i >= iSectionId){
				break;
			}
			// Count the section line
			iViewSeq++;
			
			// Count all line in previous section
			iViewSeq += oFrameOrderingBasketSection.getChildCellCount();
		}
		iViewSeq++;
		
		oFrameSection.setViewSeq(iViewSeq);

		m_oSectionList.add(iSectionId, oFrameSection);
		
		// Add to main list view
		m_oOrderingBasketList.attachChild(oFrameSection);
	}

	public void removeSection(int iSectionId) {
		if (m_oSectionList.size() <= iSectionId)
			// No such section
			return;
		
		// Remove the target section
		FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		// Remove the object from the list view
		m_oOrderingBasketList.removeChild(oFrameSection.getId());
		
		// Remove from the section list
		m_oSectionList.remove(iSectionId);
		
		if(m_iCurrentSelectedSectionId == iSectionId){
			// If the removed section is current selected, move back to 1st section
			setSelectedSection(0);
		}
	}
	
	public void removeAllSections() {
		// clear all sections
		m_oOrderingBasketList.removeAllChildren();
		m_oSectionList.clear();
	}

	public void clearAllSections() {
		int i = 0;
		
		// Stop update basket timer
		this.stopUpdateBasket();
		
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if(i > 0){
				if(oFrameOrderingBasketSection.getVisible())
					oFrameOrderingBasketSection.setVisible(false);
			}
			oFrameOrderingBasketSection.removeAllItems();
			i++;
		}
		
		if (m_oOrderingBasketExtraInfoFrame != null && m_oOrderingBasketExtraInfoFrame.getVisible() && m_bAlwaysResetOrderingBasketExtraInfoWindowSize) {
			m_oOrderingBasketExtraInfoFrame.shrink();
			resizeOrderingBasketHeightAndTop();
		}
		
		// Hide the loading
		m_oLabelLoading.setVisible(false);
	}
	
	// iMode	0: Normal item basket
	//			1: Simple mode
	//			2: Stock delivery mode
	public boolean addItem(int iSectionId, int iItemIndex, BigDecimal dQty, BigDecimal dReceiveQty, String[] sDesc, int iItemDecimal, BigDecimal dTotal, boolean bIsOldItem, boolean bIsChildItem, int iMode, boolean bHideReceiveQty, int iItemSeq) {
		try{
			// Hide the loading
			if(m_oLabelLoading.getVisible())
				m_oLabelLoading.setVisible(false);
			
			if (m_oSectionList.size() <= iSectionId)
				// No such section
				return false;
			
			// Get the view sequence for this new add section
			int iViewSeq = 0;
			int i = 0;
			for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
				if(i > iSectionId){
					break;
				}
				// Count the section line
				iViewSeq++;
				
				if(i < iSectionId){
					// Count all line in previous section
					iViewSeq += oFrameOrderingBasketSection.getChildCellCount();
				}else{
					// Count the self section line before the item index
					iViewSeq += oFrameOrderingBasketSection.getChildCellCountBeforeItemIndex(iItemIndex);
				}
				i++;
			}
			iViewSeq++;
	
			FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
			if(iMode == 2)
				oFrameSection.addItemInStockDeliveryMode(iViewSeq, iItemIndex, dQty, dReceiveQty, sDesc, bIsOldItem, bHideReceiveQty);
			else if(iMode == 1)
				oFrameSection.addItemInSimpleMode(iViewSeq, iItemIndex, dQty, sDesc, iItemDecimal, dTotal, bIsOldItem);
			else
				oFrameSection.addItem(iViewSeq, iItemIndex, dQty, sDesc, iItemDecimal, dTotal, bIsOldItem, bIsChildItem, iItemSeq);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public void removeItem(int iSectionId, int iItemIndex) {
		if (m_oSectionList.size() <= iSectionId)
			// No such section
			return;
		
		FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.removeItem(iItemIndex);
	}
	
	public void addChildItem(int iSectionId, int iItemIndex, int iChildItemIndex, BigDecimal dQty, String[] sDesc, int iItemDecimal, BigDecimal dTotal, boolean bIsOldItem) {
		if (m_oSectionList.size() <= iSectionId)
			// No such section
			return;
		
		// Get the view sequence for this new add section
		int iViewSeq = 0;
		int i = 0;
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if(i > iSectionId){
				break;
			}
			// Count the section line
			iViewSeq++;

			if(i < iSectionId){
				// Count all line in previous section
				iViewSeq += oFrameOrderingBasketSection.getChildCellCount();
			}else{
				// Count the self section line before the item index
				iViewSeq += oFrameOrderingBasketSection.getChildCellCountBeforeChildItemIndex(iItemIndex, iChildItemIndex);
			}
			i++;
		}
		iViewSeq++;
		
		FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.addChildItem(iViewSeq, iItemIndex, iChildItemIndex, dQty, sDesc, iItemDecimal, dTotal, bIsOldItem);
	}

	public void removeChildItem(int iSectionId, int iItemIndex, int iChildItemIndex) {
		if (m_oSectionList.size() <= iSectionId)
			// No such section
			return;
		
		FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.removeChildItem(iItemIndex, iChildItemIndex);
	}
	
	public void removeAllChildItem(int iSectionId, int iItemIndex){
		if (m_oSectionList.size() <= iSectionId)
			// No such section
			return;
		
		FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.removeAllChildItem(iItemIndex);
	}
	
	public void updateChildItemInfo(int iSectionId, int iItemIndex, int iChildItemIndex, String sInfo){
		if (m_oSectionList.size() <= iSectionId)
			// No such section
			return;
		
		FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.updateChildItemInfo(iItemIndex, iChildItemIndex, sInfo);
	}

	public void updateInfo(int iSectionId, int iItemIndex, String[] sStatus, String[] sDiscount, ArrayList<FuncCheckItem> sModifiers, String[] sPantryMessage, String[] sDisplayInformation, ArrayList<PosTaxScType> sScTaxAddedInformation, ArrayList<PosTaxScType> sScTaxWaivedInformation) {
		if(m_oSectionList.size() <= iSectionId){
			return;
		}
		FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		int iHeightOfheader = 0;
		
		if(m_oPosDisplayPanelZone.isDisplayCheckOrderingType())
			iHeightOfheader += m_oLabelOrderType.getHeight();
		if(m_oPosDisplayPanelZone.isDisplayBasketHeader())
			iHeightOfheader += m_oHeaderFrame.getHeight();
		oFrameSection.updateInfo(iSectionId, iItemIndex, sStatus, sDiscount, sModifiers, sPantryMessage, sDisplayInformation, sScTaxAddedInformation, sScTaxWaivedInformation, this.getHeight() - iHeightOfheader);
	}
	
	public void addModifier(int iSectionId, int iItemIndex, int iModifierIndex, BigDecimal dQty, String[] sDesc, int iItemDecimal, BigDecimal dTotal) {
		if (m_oSectionList.size() <= iSectionId)
			// No such section
			return;
	
		// Get the view sequence for this new add section
		int iViewSeq = 0;
		int i = 0;
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if(i > iSectionId){
				break;
			}
			// Count the section line
			iViewSeq++;
			
			if(i < iSectionId){
				// Count all line in previous section
				iViewSeq += oFrameOrderingBasketSection.getChildCellCount();
			}else{
				// Count the self section line before the item index
				iViewSeq += oFrameOrderingBasketSection.getChildCellCountBeforeModifierIndex(iItemIndex, iModifierIndex);
			}
			i++;
		}
		iViewSeq++;

		FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.addModifier(iViewSeq, iItemIndex, iModifierIndex, dQty, sDesc, iItemDecimal, dTotal);
	}

	public void removeModifier(int iSectionId, int iItemIndex, int iModifierIndex) {
		if (m_oSectionList.size() <= iSectionId)
			// No such section
			return;
		
		FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.removeModifier(iItemIndex, iModifierIndex);
	}
	
	public void removeAllModifier(int iSectionId, int iItemIndex) {
		if (m_oSectionList.size() <= iSectionId)
			// No such section
			return;
		
		FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.removeAllModifier(iItemIndex);
	}
	
	// Select section and change section color
	public void setSelectedSection(int iSectionId) {
		if (iSectionId != m_iCurrentSelectedSectionId) {
			if (m_oSectionList.size() > m_iCurrentSelectedSectionId){
				FrameOrderingBasketSection frmSection = m_oSectionList.get(m_iCurrentSelectedSectionId);
				frmSection.setTitleForegroundColor(frmSection.getForegroundColor());
			}
		}
		
		FrameOrderingBasketSection frmSection = m_oSectionList.get(iSectionId);
		frmSection.setTitleForegroundColor(m_oSelectSection.getForegroundColor());
		m_iCurrentSelectedSectionId = iSectionId;
	}
	
	//Make the selected section be visible
	public void setSectionVisible(int iSectionId, boolean bShow) {
		try {
			FrameOrderingBasketSection frmSection = m_oSectionList.get(iSectionId);
			frmSection.setVisible(bShow);
		}
		catch (Exception e) {
			AppGlobal.stack2Log(e);
			return;
		}
	}
	
	//Make the selected section cell be visible
	public void setSectionCellVisible(int iSectionId, int iItemIndex, boolean bShow) {
		try {
			FrameOrderingBasketSection frmSection = m_oSectionList.get(iSectionId);
			frmSection.setCellVisible(iItemIndex, bShow);
		}
		catch (Exception e) {
			AppGlobal.stack2Log(e);
			return;
		}
	}
	
	public void setSelectedNewQuantity(BigDecimal dDefaultQty) {
		m_sCurrentSelectedNewQuantity = dDefaultQty.toString();
	}
	
	/*
	public int getNumberOfVisibleSections() {
		int iVisibleSection = 1;			//The "Shared" section
		
		for(int i = 1; i < m_oSectionList.size(); i++) {
			FrameOrderingBasketSection frmSection = m_oSectionList.get(i);
			if(frmSection.getVisible()) {
				iVisibleSection++;
			}
		}
		
		return iVisibleSection;
	}
	*/
	
	public boolean isSectionVisible(int iSectionId) {
		try {
			FrameOrderingBasketSection frmSection = m_oSectionList.get(iSectionId);
			return frmSection.getVisible();
		}
		catch (Exception e) {
			AppGlobal.stack2Log(e);
			return false;
		}
	}
	
	public boolean isSectionCellVisible(int iSectionId, int iItemIndex) {
		try {
			FrameOrderingBasketSection frmSection = m_oSectionList.get(iSectionId);
			return frmSection.isCellVisible(iItemIndex);
		}
		catch (Exception e) {
			AppGlobal.stack2Log(e);
			return false;
		}
	}
	
	public void setNewDesc(int iSectionId, int iItemIndex, String[] sNewDesc) {
		FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setNewDesc(iItemIndex, sNewDesc);
	}
	
	// Set new quantity for a cell
	public void setNewQty(int iSectionId, int iItemIndex, String sNewQty, AppGlobal.OPERATION_MODE m_eOperationMode) {
		FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		//Differentiate which operation mode for different style in ordering basket
		if(m_eOperationMode != AppGlobal.OPERATION_MODE.stock_delivery)
			oFrameSection.setNewQty(iItemIndex, sNewQty, false);
		else 
			oFrameSection.setNewQty(iItemIndex, sNewQty, true);
	}
	
	// Set new receive quantity for a cell
	public void setNewReceiveQty(int iSectionId, int iItemIndex, String sNewQty) {
		FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setNewReceiveQty(iItemIndex, sNewQty);
	}
	
	public void setNewPrice(int iSectionId, int iItemIndex, String sNewPrice) {
		FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setNewPrice(iItemIndex, sNewPrice);
	}
	
	// Move the basket item scroll view position
	// iPosition:	1 - To top
	//				2 - To bottom
	public void moveScrollToItem(int iSectionId, int iItemIndex) {
		// Get the view sequence for this new add section
		int iViewSeq = 0;
		int i = 0;
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if(i > iSectionId){
				break;
			}
			// Count the section line
			iViewSeq++;
			
			if(i < iSectionId){
				// Count all line in previous section
				iViewSeq += oFrameOrderingBasketSection.getChildCellCount();
			}else{
				// Count the self section line before the item index
				iViewSeq += oFrameOrderingBasketSection.getChildCellCountBeforeItemIndex(iItemIndex);
			}
			i++;
		}
		
		m_oOrderingBasketList.scrollToIndex(iViewSeq);
	}
	
	// Move the basket child item scroll view position
	// iPosition:	1 - To top
	//				2 - To bottom
	public void moveScrollToChildItem(int iSectionId, int iItemIndex, int iChildItemIndex) {
		// Get the view sequence for this new add section
		int iViewSeq = 0;
		int i = 0;
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if(i > iSectionId){
				break;
			}
			// Count the section line
			iViewSeq++;

			if(i < iSectionId){
				// Count all line in previous section
				iViewSeq += oFrameOrderingBasketSection.getChildCellCount();
			}else{
				// Count the self section line before the item index
				iViewSeq += oFrameOrderingBasketSection.getChildCellCountBeforeChildItemIndex(iItemIndex, iChildItemIndex);
			}
			i++;
		}
		
		m_oOrderingBasketList.scrollToIndex(iViewSeq);
	}
	
	// Move the basket modifier scroll view position
	// iPosition:	1 - To top
	//				2 - To bottom
	public void moveScrollToModifier(int iSectionId, int iItemIndex, int iModifierIndex) {
		// Get the view sequence for this new add section
		int iViewSeq = 0;
		int i = 0;
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if(i > iSectionId){
				break;
			}
			// Count the section line
			iViewSeq++;
			
			if(i < iSectionId){
				// Count all line in previous section
				iViewSeq += oFrameOrderingBasketSection.getChildCellCount();
			}else{
				// Count the self section line before the item index
				iViewSeq += oFrameOrderingBasketSection.getChildCellCountBeforeModifierIndex(iItemIndex, iModifierIndex);
			}
			i++;
		}
		
		m_oOrderingBasketList.scrollToIndex(iViewSeq);
	}
	
	// Move the basket to target section last line
	public void moveScrollToSectionBottom(int iSectionId) {
		// Get the view sequence for this new add section
		int iViewSeq = 0;
		int i = 0;
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if(i > iSectionId){
				break;
			}
			// Count the section line
			iViewSeq++;
			
			if(i <= iSectionId){
				// Count all line in previous section
				iViewSeq += oFrameOrderingBasketSection.getChildCellCount();
			}
			i++;
		}
		
		m_oOrderingBasketList.scrollToIndex(iViewSeq-1);
	}
	
	// Move the basket to last line
	public void moveScrollToBottom() {
		if(m_oSectionList.isEmpty())
			return;
		
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if(oFrameOrderingBasketSection.getChildCellCount() == 0)
				return;
		}
		
		m_oOrderingBasketList.scrollToPosition(2);
	}
	
	// Move the basket to first line
	public void moveScrollToTop() {
		m_oOrderingBasketList.scrollToPosition(1);
	}
	
	// Extend the basket
	public void extendBasket(int iExtendedHeight) {
		m_oOrderingBasketList.setHeight(m_oOrderingBasketList.getHeight()+iExtendedHeight);
	}
	
	// Minimize the basket
	public void minimizeBasket(int iExtendedHeight) {
		m_oOrderingBasketList.setHeight(m_oOrderingBasketList.getHeight()-iExtendedHeight);
	}
	
	// Set the basket height
	public void setBasketHeight(int iHeight) {
		m_oOrderingBasketList.setHeight(iHeight);
	}
	
	// Check if the item is old or not
	public boolean isOldItem(int iSectionId, int iItemIndex){
		if (m_oSectionList.size() <= iSectionId)
			// No such section
			return false;
		
		FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		return oFrameSection.isOldItem(iSectionId, iItemIndex);
	}
	
	// Set if the item is selected or not
	// iSelectType	0: no restriction
	//				1: Select new item only
	//				2: Select old item only
	// iShowType	0 - Not show
	//				1 - Show selected
	//				2 - Show deselected
	public void setSelected(int iSectionId, int iItemIndex, int iSelectType, int iShowType){
		if (m_oSectionList.size() <= iSectionId)
			// No such section
			return;
		
		FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setSelected(iSectionId, iItemIndex, iSelectType, iShowType);
	}
	
	// Set all item if the item is selected or not
	// iSelectType	0: no restriction
	//				1: Select new item only
	//				2: Select old item only
	// iShowType	0 - Not show
	//				1 - Show selected
	//				2 - Show deselected
	public void setAllSelected(int iSelectType, int iShowType){
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			oFrameOrderingBasketSection.setAllSelected(iSelectType, iShowType);
		}
	}
	
	public void setShowPantryMessage(boolean bShowPantryMessage) {
		this.m_bShowPantryMessage = bShowPantryMessage;
	}
	
	// Get number of item under the section
	public int getItemCellCount(int iSectionId){
		if (m_oSectionList.size() <= iSectionId)
			// No such section
			return 0;
		
		FrameOrderingBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		return oFrameSection.getItemCellCount();
	}
	
	// Create update basket timer
	private void addUpdateBasketTimer(){
		this.addTimer("update_basket_timer", 0, false, "", false, false, null);
	}
	
	// Start to update basket
	public void startUpdateBasket(){
		
		m_oLabelLoading.setVisible(true);
		m_oLabelLoading.bringToTop();
		
		this.controlTimer("update_basket_timer", true);
	}
	
	// Stop update basket
	public void stopUpdateBasket(){
		this.controlTimer("update_basket_timer", false);
	}
	
	public boolean isShowPantryMessage() {
		return this.m_bShowPantryMessage;
	}
	
	public int getNumberOfBasketItem(){
		int iBasketItemNumber = m_oPosDisplayPanelZone.getNumberOfBasketItem();
		return iBasketItemNumber;
	}
	
	public boolean isDisplayBasketHeader(){
		return m_oPosDisplayPanelZone.isDisplayBasketHeader();
	}
	
	public boolean isDisplayCheckOrderingType(){
		return m_oPosDisplayPanelZone.isDisplayCheckOrderingType();
	}
	
	public boolean isDisplayItemPrice(){
		return m_oPosDisplayPanelZone.isDisplayItemPrice();
	}
	
	public boolean isDisplayItemSequence(){
		return m_oPosDisplayPanelZone.isDisplayItemSequence();
	}
	
	public boolean isDisplayOrderingBasketExtension(){
		return m_oPosDisplayPanelZone.isDisplayOrderingBasketExtension();
	}
	
	public boolean isDisplaySeatNumberSection(){
		return m_oPosDisplayPanelZone.isDisplaySeatNumberSection();
	}
	
	public void showOrderingBasketExtraInfoFrame(boolean bVisible) {
		if(m_oOrderingBasketExtraInfoFrame != null && m_oOrderingBasketExtraInfoFrame.getVisible() == !bVisible) {
			m_oOrderingBasketExtraInfoFrame.setVisible(bVisible);
			if (bVisible == false) {
				m_oOrderingBasketList.setTop(m_oOrderingBasketList.getTop() - m_oOrderingBasketExtraInfoFrame.getHeight());
				m_oOrderingBasketList.setHeight(m_oOrderingBasketList.getHeight() + m_oOrderingBasketExtraInfoFrame.getHeight());
			} else {
				m_oOrderingBasketList.setTop(m_oOrderingBasketList.getTop() + m_oOrderingBasketExtraInfoFrame.getHeight());
				m_oOrderingBasketList.setHeight(m_oOrderingBasketList.getHeight() - m_oOrderingBasketExtraInfoFrame.getHeight());
			}
		}
	}
	
	public void resizeOrderingBasketHeightAndTop(){
		m_oOrderingBasketList.setTop(m_oOrderingBasketExtraInfoFrame.getTop() + m_oOrderingBasketExtraInfoFrame.getHeight());
		if (m_oPosDisplayPanelZone.isDisplayCheckOrderingType()) {
			m_oLabelOrderType.setTop(m_oOrderingBasketList.getTop());
			m_oUpperlineFrame.setTop(m_oLabelOrderType.getTop() + m_oLabelOrderType.getHeight());
			m_oOrderingBasketList.setTop(m_oUpperlineFrame.getTop() + m_oUpperlineFrame.getHeight());
			m_oOrderingBasketList.setHeight(this.getHeight() - m_oOrderingBasketExtraInfoFrame.getHeight());
		}
	}
	
	@Override
	public void frameOrderingBasketExtraInfoPull_Clicked() {
		resizeOrderingBasketHeightAndTop();
	}
	
	// Section line is clicked
	@Override
	public void frameOrderingBasketSection_SectionClicked(int iCellId, String sNote){
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if (iCellId == oFrameOrderingBasketSection.getId()) {
				// Section is clicked, set current selected section
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				this.setSelectedSection(iSectionId);
				
				for(FrameOrderingBasketListener listener: listeners) {
					listener.frameOrderingBasketSection_SectionClicked(iSectionId, sNote);
				}
				break;
			}
		}
	}
	
	// Item qty is clicked
	@Override
	public void frameOrderingBasketCell_QtyClicked(int iCellId, int iItemIndex, String sNote) {
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if (iCellId == oFrameOrderingBasketSection.getId()) {
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				for (FrameOrderingBasketListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_QtyClicked(iSectionId, iItemIndex, sNote);
				}
				break;
			}
		}
	}

	// Item temp qty is clicked
	@Override
	public String frameOrderingBasketCell_TempQtyClicked(int iCellId, int iItemIndex, String sNote, String sOriQty) {
		String sNewQty = sOriQty;
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if (iCellId == oFrameOrderingBasketSection.getId()) {
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				for (FrameOrderingBasketListener listener : listeners) {
					// Raise the event to parent
					sNewQty = listener.frameOrderingBasketCell_TempQtyClicked(iSectionId, iItemIndex, sNote, sOriQty);
				}
				break;
			}
		}
		m_sCurrentSelectedNewQuantity = sNewQty;
		return sNewQty;
	}

	@Override
	public void frameOrderingBasketCell_ReceiveQtyClicked(int iCellId,
			int iItemIndex, String sNote) {
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if (iCellId == oFrameOrderingBasketSection.getId()) {
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				for (FrameOrderingBasketListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_ReceiveQtyClicked(iSectionId, iItemIndex, sNote);
				}
				break;
			}
		}
	}
	
	// Item desc is clicked
	@Override
	public void frameOrderingBasketCell_DescClicked(int iCellId, int iItemIndex, String sNote) {
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if (iCellId == oFrameOrderingBasketSection.getId()) {
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				for (FrameOrderingBasketListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_DescClicked(iSectionId, iItemIndex, sNote);
				}
				break;
			}
		}
	}
	
	// Item desc is clicked
	@Override
	public void frameOrderingBasketCell_DescLongClicked(int iCellId, int iItemIndex, String sNote) {
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if (iCellId == oFrameOrderingBasketSection.getId()) {
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				for (FrameOrderingBasketListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_DescLongClicked(iSectionId, iItemIndex, sNote);
				}
				break;
			}
		}
	}
	
	// Item price is clicked
	@Override
	public void frameOrderingBasketCell_PriceClicked(int iCellId, int iItemIndex, String sNote) {
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if (iCellId == oFrameOrderingBasketSection.getId()) {
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				for (FrameOrderingBasketListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_PriceClicked(iSectionId, iItemIndex, sNote);
				}
				break;
			}
		}
	}
	
	//Item Desc is swiped, and delete button is clicked
	@Override
	public void frameOrderingBasketCell_DeleteClicked(int iCellId, int iItemIndex, String sNote) {
		for(FrameOrderingBasketSection oFrameOrderingBasketSection : m_oSectionList) {
			if(iCellId == oFrameOrderingBasketSection.getId()) {
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				for(FrameOrderingBasketListener listener : listeners) {
					//Raise the event to FormMain
					listener.frameOrderingBasketCell_DeleteClicked(iSectionId, iItemIndex, sNote);
				}
				break;
			}
		}
	}

	@Override
	public void frameOrderingBasketCell_AddQtyClicked(int iCellId, int iItemIndex, String sNote) {
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if (iCellId == oFrameOrderingBasketSection.getId()) {
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				for (FrameOrderingBasketListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_AddQtyClicked(iSectionId, iItemIndex, sNote);
				}
				break;
			}
		}
	}

	@Override
	public boolean frameOrderingBasketCell_AddQtyClickedForOldItem(int iCellId, int iItemIndex, String sNote, BigDecimal dNewQty) {
		boolean bResult = false;
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if (iCellId == oFrameOrderingBasketSection.getId()) {
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				for (FrameOrderingBasketListener listener : listeners) {
					// Raise the event to parent
					bResult = listener.frameOrderingBasketCell_AddQtyClickedForOldItem(iSectionId, iItemIndex, sNote, dNewQty);
				}
				break;
			}
		}
		if(bResult)
			m_sCurrentSelectedNewQuantity = dNewQty.toPlainString();
		return bResult;
	}

	@Override
	public void frameOrderingBasketCell_MinusQtyClicked(int iCellId, int iItemIndex, String sNote) {
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if (iCellId == oFrameOrderingBasketSection.getId()) {
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				for (FrameOrderingBasketListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_MinusQtyClicked(iSectionId, iItemIndex, sNote);
				}
				break;
			}
		}
	}
	
	@Override
	public boolean frameOrderingBasketCell_MinusQtyClickedForOldItem(int iCellId, int iItemIndex, String sNote, BigDecimal dNewQty) {
		boolean bResult = false;
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if (iCellId == oFrameOrderingBasketSection.getId()) {
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				for (FrameOrderingBasketListener listener : listeners) {
					// Raise the event to parent
					bResult = listener.frameOrderingBasketCell_MinusQtyClickedForOldItem(iSectionId, iItemIndex, sNote, dNewQty);
				}
				break;
			}
		}
		if(bResult)
			m_sCurrentSelectedNewQuantity = dNewQty.toPlainString();
		return bResult;
	}

	@Override
	public void frameOrderingBasketChildItemCell_QtyClicked(int iCellId,
			int iItemIndex, int iChildItemIndex, String sNote) {
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if (iCellId == oFrameOrderingBasketSection.getId()) {
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				for (FrameOrderingBasketListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketChildItemCell_QtyClicked(iSectionId, iItemIndex, iChildItemIndex, sNote);
				}
				break;
			}
		}
	}

	@Override
	public void frameOrderingBasketChildItemCell_DescClicked(int iCellId,
			int iItemIndex, int iChildItemIndex, String sNote) {
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if (iCellId == oFrameOrderingBasketSection.getId()) {
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				for (FrameOrderingBasketListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketChildItemCell_DescClicked(iSectionId, iItemIndex, iChildItemIndex, sNote);
				}
				break;
			}
		}
	}

	@Override
	public void frameOrderingBasketChildItemCell_PriceClicked(int iCellId,
			int iItemIndex, int iChildItemIndex, String sNote) {
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if (iCellId == oFrameOrderingBasketSection.getId()) {
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				for (FrameOrderingBasketListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketChildItemCell_PriceClicked(iSectionId, iItemIndex, iChildItemIndex, sNote);
				}
				break;
			}
		}
	}

	@Override
	public void frameOrderingBasketModifierCell_QtyClicked(int iCellId,
			int iItemIndex, int iModifierIndex, String sNote) {
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if (iCellId == oFrameOrderingBasketSection.getId()) {
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				for (FrameOrderingBasketListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketChildItemCell_QtyClicked(iSectionId, iItemIndex, iModifierIndex, sNote);
				}
				break;
			}
		}
	}

	@Override
	public void frameOrderingBasketModifierCell_DescClicked(int iCellId,
			int iItemIndex, int iModifierIndex, String sNote) {
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if (iCellId == oFrameOrderingBasketSection.getId()) {
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				for (FrameOrderingBasketListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketChildItemCell_DescClicked(iSectionId, iItemIndex, iModifierIndex, sNote);
				}
				break;
			}
		}
	}

	@Override
	public void frameOrderingBasketModifierCell_PriceClicked(int iCellId,
			int iItemIndex, int iModifierIndex, String sNote) {
		for(FrameOrderingBasketSection oFrameOrderingBasketSection: m_oSectionList){
			if (iCellId == oFrameOrderingBasketSection.getId()) {
				int iSectionId = m_oSectionList.indexOf(oFrameOrderingBasketSection);
				for (FrameOrderingBasketListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketChildItemCell_PriceClicked(iSectionId, iItemIndex, iModifierIndex, sNote);
				}
				break;
			}
		}
	}
	
	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if(iId == super.getIDForPosting().hashCode()) {
			// Ask drawing basket
			//Set the last client socket ID
			AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
			
			// Hide the loading
			if(m_oLabelLoading.getVisible())
				m_oLabelLoading.setVisible(false);
			
			for (FrameOrderingBasketListener listener : listeners) {
				// Raise the event to parent
				listener.frameOrderingBasket_UpdateBasket();
			}
			
			// Send the UI packet to client and the thread is finished
			super.getParentForm().finishUI(true);

			return true;
		}
		
		return false;
	}
	
	public void setOrderType(String sOrderType){
		m_oLabelOrderType.setValue(sOrderType);
	}
	
	public void addHeader(ArrayList<Integer> iFieldWidths, ArrayList<String> sFieldValues) {
		int i = 0;
		int iLeft = 14;
		int iHeaderHeight = 0;
		
		m_oUnderlineFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oUnderlineFrame, "fraUnderline");
		m_oUnderlineFrame.setWidth(this.getWidth());
		this.attachChild(m_oUnderlineFrame);
		
		m_oHeaderFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oHeaderFrame, "fraHeader");
		m_oHeaderFrame.setWidth(this.getWidth());
		
		if(m_oPosDisplayPanelZone.isDisplayCheckOrderingType())
			m_oHeaderFrame.setTop(m_oUpperlineFrame.getTop() + m_oUpperlineFrame.getHeight());
		this.attachChild(m_oHeaderFrame);
		
		for(String sValue:sFieldValues){
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblHeader");
			
			if (sValue instanceof String)
				oLabel.setValue((String)sValue);
			
			if(i < iFieldWidths.size()){
				oLabel.setLeft(iLeft);
				oLabel.setWidth(iFieldWidths.get(i));
				iLeft += iFieldWidths.get(i);
			}
			oLabel.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.LEFT + "," + HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
			m_oHeaderFrame.attachChild(oLabel);
			m_oLabelHeaders.put(i, oLabel);
			
			iHeaderHeight = oLabel.getHeight();
			
			i++;
		}
		
		// fill rest space of header
		if(iLeft < m_oHeaderFrame.getWidth()) {
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblHeader");
			oLabel.setLeft(iLeft);
			oLabel.setWidth(m_oHeaderFrame.getWidth()-iLeft);
			m_oHeaderFrame.attachChild(oLabel);
		}
		
		m_oUnderlineFrame.setTop(m_oHeaderFrame.getTop() + m_oHeaderFrame.getHeight());
		m_oOrderingBasketList.setTop(m_oUnderlineFrame.getTop() + m_oUnderlineFrame.getHeight());
		m_oOrderingBasketList.setHeight(this.getHeight() - iHeaderHeight);
	}
}
