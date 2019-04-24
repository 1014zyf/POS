package app;

import java.math.BigDecimal;
import java.util.ArrayList;

import externallib.StringLib;
import om.PosTaxScType;
import om.PosDisplayPanelZone;
import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FrameOrderingBasketSectionListener {
	void frameOrderingBasketSection_SectionClicked(int iCellId, String sNote);
	void frameOrderingBasketCell_QtyClicked(int iCellId, int iItemIndex, String sNote);
	String frameOrderingBasketCell_TempQtyClicked(int iCellId, int iItemIndex, String sNote, String sOriQty);
	void frameOrderingBasketCell_ReceiveQtyClicked(int iCellId, int iItemIndex, String sNote);
	void frameOrderingBasketCell_DescClicked(int iCellId, int iItemIndex, String sNote);
	void frameOrderingBasketCell_DescLongClicked(int iCellId, int iItemIndex, String sNote);
	void frameOrderingBasketCell_PriceClicked(int iCellId, int iItemIndex, String sNote);
	void frameOrderingBasketCell_DeleteClicked(int iCellId, int iItemIndex, String sNote);
	void frameOrderingBasketCell_AddQtyClicked(int iCellId, int iItemIndex, String sNote);
	boolean frameOrderingBasketCell_AddQtyClickedForOldItem(int iCellId, int iItemIndex, String sNote, BigDecimal dNewQty);
	void frameOrderingBasketCell_MinusQtyClicked(int iCellId, int iItemIndex, String sNote);
	boolean frameOrderingBasketCell_MinusQtyClickedForOldItem(int iCellId, int iItemIndex, String sNote, BigDecimal dNewQty);
	void frameOrderingBasketChildItemCell_QtyClicked(int iCellId, int iItemIndex, int iChildItemIndex, String sNote);
	void frameOrderingBasketChildItemCell_DescClicked(int iCellId, int iItemIndex, int iChildItemIndex, String sNote);
	void frameOrderingBasketChildItemCell_PriceClicked(int iCellId, int iItemIndex, int iChildItemIndex, String sNote);
	void frameOrderingBasketModifierCell_QtyClicked(int iCellId, int iItemIndex, int iModifierIndex, String sNote);
	void frameOrderingBasketModifierCell_DescClicked(int iCellId, int iItemIndex, int iModifierIndex, String sNote);
	void frameOrderingBasketModifierCell_PriceClicked(int iCellId, int iItemIndex, int iModifierIndex, String sNote);
}

public class FrameOrderingBasketSection extends VirtualUIFrame implements FrameOrderingBasketCellListener{
	TemplateBuilder m_oTemplateBuilder;
	
	private ArrayList<FrameOrderingBasketCell> m_oCellList;
	
	// Basket Main List View
	private VirtualUIList m_oOrderingBasketList;
	
	// Section name
	private VirtualUILabel m_oLabelTitle;
	
	// Ordering Basket Display Panel Zone
	private PosDisplayPanelZone m_oPosDisplayPanelZone;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameOrderingBasketSectionListener> listeners;
	
	// Toggle mode of ordering basket in consolidate item
	private String m_sOrderingBasketToggleMode;
	
	// Basket Indicator
	private VirtualUILabel m_oLabelBasketIndicator;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameOrderingBasketSectionListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameOrderingBasketSectionListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public void init(VirtualUIList oOrderingBasketList, PosDisplayPanelZone oOrderingBasketDisplayPanelZone, String sOrderingBasketToggleMode) {
		this.m_sOrderingBasketToggleMode = sOrderingBasketToggleMode;
		
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameOrderingBasketSectionListener>();
		m_oCellList = new ArrayList<FrameOrderingBasketCell>();
		m_oPosDisplayPanelZone = oOrderingBasketDisplayPanelZone;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraOrderingBasketSection.xml");
		
		// Section Title
		m_oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTitle, "lblSectionTitle");
		m_oLabelTitle.setWidth(this.getWidth());
		m_oLabelTitle.setEnabled(true);
		m_oLabelTitle.allowClick(true);
		this.attachChild(m_oLabelTitle);
		
		if(m_sOrderingBasketToggleMode.equals(FrameOrderingBasket.ORDERING_BASKET_MODE_SHOW_CONSOLIDATE_ITEMS)){
			// Section Title
			m_oLabelBasketIndicator = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelBasketIndicator, "lblBasketIndicator");
			m_oLabelBasketIndicator.setWidth((int)(this.getWidth()*0.3));
			m_oLabelBasketIndicator.setEnabled(true);
			m_oLabelBasketIndicator.setValue(AppGlobal.g_oLang.get()._("consolidate_items", ""));
			this.attachChild(m_oLabelBasketIndicator);
			this.setVisible(true);
		}
		
		// Set Main List View
		m_oOrderingBasketList = oOrderingBasketList;
	}
	
	public String getTitle() {
		return m_oLabelTitle.getValue();
	}
	
	public void setTitle(String[] sSectionTitle) {
		m_oLabelTitle.setValue(sSectionTitle);
	}
	
	public void setTitleForegroundColor(String sColor) {
		m_oLabelTitle.setForegroundColor(sColor);
	}
	
	public void addItem(int iViewSeq, int iItemIndex, BigDecimal dQty, String[] sDesc, int iItemDecimal, BigDecimal dTotal, boolean bIsOldItem, boolean bIsChildItem, int iItemSeq) {
		FrameOrderingBasketCell oFrameCell = new FrameOrderingBasketCell();
		
		if(bIsOldItem)
			m_oTemplateBuilder.buildFrame(oFrameCell, "fraOrderingBasketOldCell");
		else{
			m_oTemplateBuilder.buildFrame(oFrameCell, "fraOrderingBasketCell");
			oFrameCell.setUnderline(true);
		}
		
		//oFrameCell.setCellWidth(this.getWidth());
		oFrameCell.setWidth(this.getWidth());
		
		//Init different basket cell based on basket type
		if(m_sOrderingBasketToggleMode.equals(FrameOrderingBasket.ORDERING_BASKET_MODE_SHOW_NORMAL))
			oFrameCell.init(m_oOrderingBasketList, bIsOldItem, bIsChildItem, m_oPosDisplayPanelZone, false);
		else
			oFrameCell.init(m_oOrderingBasketList, bIsOldItem, bIsChildItem, m_oPosDisplayPanelZone, true);
		
		//oFrameCell.updateFieldLabel();
		
		oFrameCell.addListener(this);
		
		oFrameCell.setQuantity(dQty.stripTrailingZeros().toPlainString(), false);
		oFrameCell.setDescription(sDesc);
		
		if(m_oPosDisplayPanelZone.isDisplayItemPrice())
			oFrameCell.setPrice(StringLib.BigDecimalToString(dTotal, iItemDecimal));
		
		if(m_oPosDisplayPanelZone.isDisplayItemSequence())
			oFrameCell.setItemSeq(iItemSeq);
		
		oFrameCell.setViewSeq(iViewSeq);
		m_oCellList.add(iItemIndex, oFrameCell);
		
		// Add to main list view
		m_oOrderingBasketList.attachChild(oFrameCell);
	}
	
	public void addItemInSimpleMode(int iViewSeq, int iItemIndex, BigDecimal dQty, String[] sDesc, int iItemDecimal, BigDecimal dTotal, boolean bIsOldItem) {
		FrameOrderingBasketCell oFrameCell = new FrameOrderingBasketCell();
		
		//Init different basket cell based on basket type
		if(m_sOrderingBasketToggleMode.equals(FrameOrderingBasket.ORDERING_BASKET_MODE_SHOW_NORMAL))
			oFrameCell.initForSimpleMode(m_oOrderingBasketList, bIsOldItem, false);
		else
			oFrameCell.initForSimpleMode(m_oOrderingBasketList, bIsOldItem, true);
		
		if(bIsOldItem)
			m_oTemplateBuilder.buildFrame(oFrameCell, "fraOrderingBasketOldCell");
		else{
			m_oTemplateBuilder.buildFrame(oFrameCell, "fraOrderingBasketCell");
			oFrameCell.setUnderline(true);
		}
		
		oFrameCell.setCellWidth(this.getWidth());
		oFrameCell.addListener(this);
		
		oFrameCell.setQuantity(dQty.stripTrailingZeros().toPlainString(), false);
		oFrameCell.setDescription(sDesc);
		oFrameCell.setPrice(StringLib.BigDecimalToString(dTotal, iItemDecimal));
		oFrameCell.setViewSeq(iViewSeq);

		m_oCellList.add(iItemIndex, oFrameCell);
		
		// Add to main list view
		m_oOrderingBasketList.attachChild(oFrameCell);
	}
	
	public void addItemInStockDeliveryMode(int iViewSeq, int iItemIndex, BigDecimal dDeliveryQty, BigDecimal dReceiveQty, String[] sDesc, boolean bIsOldItem, boolean bHideReceiveQty) {
		FrameOrderingBasketCell oFrameCell = new FrameOrderingBasketCell();
		
		//Init different basket cell based on basket type
		if(m_sOrderingBasketToggleMode.equals(FrameOrderingBasket.ORDERING_BASKET_MODE_SHOW_NORMAL))
			oFrameCell.initForStockDeliveryMode(m_oOrderingBasketList, bIsOldItem, bHideReceiveQty, false);
		else
			oFrameCell.initForStockDeliveryMode(m_oOrderingBasketList, bIsOldItem, bHideReceiveQty, true);
		
		if(bIsOldItem)
			m_oTemplateBuilder.buildFrame(oFrameCell, "fraOrderingBasketOldCell");
		else{
			m_oTemplateBuilder.buildFrame(oFrameCell, "fraOrderingBasketCell");
			oFrameCell.setUnderline(true);
		}
		
		oFrameCell.setCellWidth(this.getWidth());
		oFrameCell.addListener(this);
		
		oFrameCell.setQuantity(dDeliveryQty.stripTrailingZeros().toPlainString(), true);
		oFrameCell.setDescription(sDesc);
		oFrameCell.setNewReceiveQuantity(dReceiveQty.stripTrailingZeros().toPlainString());
		oFrameCell.setViewSeq(iViewSeq);

		m_oCellList.add(iItemIndex, oFrameCell);
		
		// Add to main list view
		m_oOrderingBasketList.attachChild(oFrameCell);
	}
	
	public void removeItem(int iItemIndex) {
		if(iItemIndex > m_oCellList.size()-1)
			return;
		
		FrameOrderingBasketCell oFrameCell = (FrameOrderingBasketCell)m_oCellList.get(iItemIndex);
		// Remove the object from the list view
		m_oOrderingBasketList.removeChild(oFrameCell.getId());
		
		// Remove from the child item list
		m_oCellList.remove(oFrameCell);
	}
	
	public void updateItem(int iItemIndex, String[] sDesc) {
		if(iItemIndex >= m_oCellList.size())
			return;
		
		m_oCellList.get(iItemIndex).setDescription(sDesc);
	}
	
	public void removeAllItems(){
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			// Remove all child items and modifiers
			oFrameOrderingBasketCell.removeAllChildItem();
			oFrameOrderingBasketCell.removeAllModifier();
			
			// Remove the object from the list view
			m_oOrderingBasketList.removeChild(oFrameOrderingBasketCell.getId());
		}
		
		// Clear the list
		m_oCellList.clear();
	}
	
	public void addChildItem(int iViewSeq, int iItemIndex, int iChildItemIndex, BigDecimal dQty, String[] sDesc, int iItemDecimal, BigDecimal dTotal, boolean bIsOldItem) {
		if(iItemIndex > m_oCellList.size()-1)
			return;
		
		FrameOrderingBasketCell oFrameCell = (FrameOrderingBasketCell)m_oCellList.get(iItemIndex);
		oFrameCell.addChildItem(iViewSeq, iChildItemIndex, dQty, sDesc, iItemDecimal, dTotal, bIsOldItem);
	}
	
	public void removeChildItem(int iItemIndex, int iChildItemIndex) {
		if(iItemIndex > m_oCellList.size()-1)
			return;
		
		FrameOrderingBasketCell oFrameCell = (FrameOrderingBasketCell)m_oCellList.get(iItemIndex);
		oFrameCell.removeChildItem(iChildItemIndex);
	}
	
	public void removeAllChildItem(int iItemIndex) {
		if(iItemIndex > m_oCellList.size()-1)
			return;
		
		FrameOrderingBasketCell oFrameCell = (FrameOrderingBasketCell)m_oCellList.get(iItemIndex);
		oFrameCell.removeAllChildItem();
	}
	
	public void updateChildItemInfo(int iItemIndex, int iChildItemIndex, String sInfo) {
		if(iItemIndex > m_oCellList.size()-1)
			return;
		
		FrameOrderingBasketCell oFrameCell = (FrameOrderingBasketCell)m_oCellList.get(iItemIndex);
		oFrameCell.updateChildItemInfo(iChildItemIndex, sInfo);
	}
	
	public void updateInfo(int iViewSeq, int iItemIndex, String[] sStatus, String[] sDiscount, ArrayList<FuncCheckItem> sModifiers, String[] sPantryMessage, String[] sDisplayInformation, ArrayList<PosTaxScType> sScTaxAddedInformation, ArrayList<PosTaxScType> sScTaxWaivedInformation, int iBasketHeight) {
		if(iItemIndex > m_oCellList.size()-1)
			return;
		
		FrameOrderingBasketCell oFrameCell = (FrameOrderingBasketCell)m_oCellList.get(iItemIndex);
		oFrameCell.setInformation(sStatus, sDiscount, sModifiers, sPantryMessage, sDisplayInformation, sScTaxAddedInformation, sScTaxWaivedInformation, iBasketHeight);
//KingsleyKwan20170918SetUnderlineTop		-----Start-----
		oFrameCell.setUnderlineTop(oFrameCell.getHeight() - 1 );
//KingsleyKwan20170918SetUnderlineTop		----- End -----
	}
	
	public void addModifier(int iViewSeq, int iItemIndex, int iModifierIndex, BigDecimal dQty, String[] sDesc, int iItemDecimal, BigDecimal dTotal) {
		if(iItemIndex > m_oCellList.size()-1)
			return;
		
		FrameOrderingBasketCell oFrameCell = (FrameOrderingBasketCell)m_oCellList.get(iItemIndex);
		oFrameCell.addModifier(iViewSeq, iModifierIndex, dQty, sDesc, iItemDecimal, dTotal);
	}
	
	public void removeModifier(int iItemIndex, int iModifierIndex) {
		if(iItemIndex > m_oCellList.size()-1)
			return;
		
		FrameOrderingBasketCell oFrameCell = (FrameOrderingBasketCell)m_oCellList.get(iItemIndex);
		oFrameCell.removeModifier(iModifierIndex);
	}
	
	public void removeAllModifier(int iItemIndex) {
		if(iItemIndex > m_oCellList.size()-1)
			return;
		
		FrameOrderingBasketCell oFrameCell = (FrameOrderingBasketCell)m_oCellList.get(iItemIndex);
		oFrameCell.removeAllModifier();
	}
	
	public int getItemCellCount(){
		return m_oCellList.size();
	}
	
	public int getChildCellCount(){
		int iChildCount = 0;
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			// Self line
			iChildCount++;
			
			// Child line
			iChildCount += oFrameOrderingBasketCell.getChildCellCount();
		}
		
		return iChildCount;
	}
	
	public int getChildCellCountBeforeItemIndex(int iItemIndex){
		int iChildCount = 0;
		int i = 0;
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			if(i >= iItemIndex)
				break;
				
			// Self line
			iChildCount++;
				
			// Child line
			iChildCount += oFrameOrderingBasketCell.getChildCellCount();

			i++;
		}
		
		return iChildCount;
	}
	
	public int getChildCellCountBeforeChildItemIndex(int iItemIndex, int iChildItemIndex){
		int iChildCount = 0;
		int i = 0;
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			if(i > iItemIndex)
				break;
			
			// Self line
			iChildCount++;

			if(i < iItemIndex)				
				// Child line
				iChildCount += oFrameOrderingBasketCell.getChildCellCount();
			else{
				// Child line
				iChildCount += oFrameOrderingBasketCell.getChildModifierCellCount();
				iChildCount += iChildItemIndex;
			}
			i++;
		}
		
		return iChildCount;
	}
	
	public int getChildCellCountBeforeModifierIndex(int iItemIndex, int iModifierIndex){
		int iChildCount = 0;
		int i = 0;
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			if(i > iItemIndex)
				break;
			
			// Self line
			iChildCount++;
			
			if(i < iItemIndex)				
				// Child line
				iChildCount += oFrameOrderingBasketCell.getChildCellCount();
			else{
				// Child line
				iChildCount += iModifierIndex;
			}
			i++;
		}
		
		return iChildCount;
	}
	
	public void setNewDesc(int iItemIndex, String[] sNewDesc) {
		FrameOrderingBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		oFrameCell.setDescription(sNewDesc);
	}
	
	public void setNewQty(int iItemIndex, String sNewQty, boolean bDeliveryMode) {
		FrameOrderingBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		oFrameCell.setNewQuantity(sNewQty, bDeliveryMode);
	}
	
	public void setNewReceiveQty(int iItemIndex, String sNewQty) {
		FrameOrderingBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		oFrameCell.setNewReceiveQuantity(sNewQty);
	}
	
	public void setNewPrice(int iItemIndex, String sNewPrice) {
		FrameOrderingBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		
		if(m_oPosDisplayPanelZone.isDisplayItemPrice())
			oFrameCell.setPrice(sNewPrice);
		
	}
	
	public boolean isOldItem(int iSectionId, int iItemIndex){
		if(iItemIndex > m_oCellList.size()-1)
			return false;
		
		FrameOrderingBasketCell oFrameCell = (FrameOrderingBasketCell)m_oCellList.get(iItemIndex);
		return oFrameCell.isOldItem();
	}
	
	public void setSelected(int iViewSeq, int iItemIndex, int iSelectType, int iShowType){
		if(iItemIndex > m_oCellList.size()-1)
			return;
		
		FrameOrderingBasketCell oFrameCell = (FrameOrderingBasketCell)m_oCellList.get(iItemIndex);
		oFrameCell.setSelected(iSelectType, iShowType);
	}
	
	public void setAllSelected(int iSelectType, int iShowType){
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			oFrameOrderingBasketCell.setSelected(iSelectType, iShowType);
		}
	}
	
	public boolean isCellVisible(int iItemIndex) {
		try {
			FrameOrderingBasketCell oFrameCell = m_oCellList.get(iItemIndex);
			return oFrameCell.getVisible();
		}
		catch (Exception e) {
			AppGlobal.stack2Log(e);
			return false;
		}
	}
	
	//Make the selected section be visible
	public void setCellVisible(int iItemIndex, boolean bShow) {
		try {
			FrameOrderingBasketCell oFrameCell = m_oCellList.get(iItemIndex);
			oFrameCell.setVisible(bShow);
		}
		catch (Exception e) {
			AppGlobal.stack2Log(e);
			return;
		}
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (iChildId == m_oLabelTitle.getId()) {
			for (FrameOrderingBasketSectionListener listener : listeners) {
				// Raise the event to parent
				listener.frameOrderingBasketSection_SectionClicked(this.getId(), sNote);
			}
			bMatchChild = true;
		}
		return bMatchChild;
	}
	
	@Override
	public void frameOrderingBasketCell_QtyClicked(int iCellId, String sNote) {
		for (FrameOrderingBasketCell oFrameOrderingBasketCell : m_oCellList) {
			if (oFrameOrderingBasketCell.getId() == iCellId) {
				for (FrameOrderingBasketSectionListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_QtyClicked(this.getId(), m_oCellList.indexOf(oFrameOrderingBasketCell), sNote);
				}
				break;
			}
		}
	}
	
	@Override
	public String frameOrderingBasketCell_TempQtyClicked(int iCellId, String sNote, String sOriQty) {
		String sNewQty = sOriQty;
		for (FrameOrderingBasketCell oFrameOrderingBasketCell : m_oCellList) {
			if (oFrameOrderingBasketCell.getId() == iCellId) {
				for (FrameOrderingBasketSectionListener listener : listeners) {
					// Raise the event to parent
					sNewQty = listener.frameOrderingBasketCell_TempQtyClicked(this.getId(), m_oCellList.indexOf(oFrameOrderingBasketCell), sNote, sOriQty);
				}
				break;
			}
		}
		return sNewQty;
	}

	@Override
	public void frameOrderingBasketCell_ReceiveQtyClicked(int iCellId,
			String sNote) {
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			if(oFrameOrderingBasketCell.getId() == iCellId){
				for (FrameOrderingBasketSectionListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_ReceiveQtyClicked(this.getId(), m_oCellList.indexOf(oFrameOrderingBasketCell), sNote);
				}
				break;
			}
		}
	}
	
	@Override
	public void frameOrderingBasketCell_DescClicked(int iCellId, String sNote) {
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			if(oFrameOrderingBasketCell.getId() == iCellId){
				for (FrameOrderingBasketSectionListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_DescClicked(this.getId(), m_oCellList.indexOf(oFrameOrderingBasketCell), sNote);
				}
				break;
			}
		}
	}
	
	@Override
	public void frameOrderingBasketCell_DescLongClicked(int iCellId, String sNote) {
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			if(oFrameOrderingBasketCell.getId() == iCellId){
				for (FrameOrderingBasketSectionListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_DescLongClicked(this.getId(), m_oCellList.indexOf(oFrameOrderingBasketCell), sNote);
				}
				break;
			}
		}
	}

	@Override
	public void frameOrderingBasketCell_PriceClicked(int iCellId, String sNote) {
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			if(oFrameOrderingBasketCell.getId() == iCellId){
				for (FrameOrderingBasketSectionListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_PriceClicked(this.getId(), m_oCellList.indexOf(oFrameOrderingBasketCell), sNote);
				}
				break;
			}
		}
	}
	
	@Override
	public void frameOrderingBasketCell_DeleteClicked(int iCellId, String sNote) {
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList) {
			if(oFrameOrderingBasketCell.getId() == iCellId) {
				for(FrameOrderingBasketSectionListener listener : listeners) {
					//Raise event to Basket
					listener.frameOrderingBasketCell_DeleteClicked(this.getId(), m_oCellList.indexOf(oFrameOrderingBasketCell), sNote);
				}
				break;
			}
		}
	}

	@Override
	public void frameOrderingBasketCell_AddQtyClicked(int iCellId, String sNote) {
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			if(oFrameOrderingBasketCell.getId() == iCellId){
				for (FrameOrderingBasketSectionListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_AddQtyClicked(this.getId(), m_oCellList.indexOf(oFrameOrderingBasketCell), sNote);
				}
				break;
			}
		}
	}

	@Override
	public boolean frameOrderingBasketCell_AddQtyClickedForOldItem(int iCellId, String sNote, BigDecimal dNewQty) {
		boolean bResult = false;
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			if(oFrameOrderingBasketCell.getId() == iCellId){
				for (FrameOrderingBasketSectionListener listener : listeners) {
					// Raise the event to parent
					bResult = listener.frameOrderingBasketCell_AddQtyClickedForOldItem(this.getId(), m_oCellList.indexOf(oFrameOrderingBasketCell), sNote, dNewQty);
				}
				break;
			}
		}
		return bResult;
	}

	@Override
	public void frameOrderingBasketCell_MinusQtyClicked(int iCellId, String sNote) {
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			if(oFrameOrderingBasketCell.getId() == iCellId){
				for (FrameOrderingBasketSectionListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketCell_MinusQtyClicked(this.getId(), m_oCellList.indexOf(oFrameOrderingBasketCell), sNote);
				}
				break;
			}
		}
	}
	
	@Override
	public boolean frameOrderingBasketCell_MinusQtyClickedForOldItem(int iCellId, String sNote, BigDecimal dNewQty) {
		boolean bResult = false;
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			if(oFrameOrderingBasketCell.getId() == iCellId){
				for (FrameOrderingBasketSectionListener listener : listeners) {
					// Raise the event to parent
					bResult = listener.frameOrderingBasketCell_MinusQtyClickedForOldItem(this.getId(), m_oCellList.indexOf(oFrameOrderingBasketCell), sNote, dNewQty);
				}
				break;
			}
		}
		return bResult;
	}

	@Override
	public void frameOrderingBasketChildItemCell_QtyClicked(int iCellId,
			int iIndex, String sNote) {
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			if(oFrameOrderingBasketCell.getId() == iCellId){
				for (FrameOrderingBasketSectionListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketChildItemCell_QtyClicked(this.getId(), m_oCellList.indexOf(oFrameOrderingBasketCell), iIndex, sNote);
				}
				break;
			}
		}
	}
	
	@Override
	public void frameOrderingBasketChildItemCell_DescClicked(int iCellId,
			int iIndex, String sNote) {
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			if(oFrameOrderingBasketCell.getId() == iCellId){
				for (FrameOrderingBasketSectionListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketChildItemCell_DescClicked(this.getId(), m_oCellList.indexOf(oFrameOrderingBasketCell), iIndex, sNote);
				}
				break;
			}
		}
	}

	@Override
	public void frameOrderingBasketChildItemCell_PriceClicked(int iCellId,
			int iIndex, String sNote) {
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			if(oFrameOrderingBasketCell.getId() == iCellId){
				for (FrameOrderingBasketSectionListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketChildItemCell_PriceClicked(this.getId(), m_oCellList.indexOf(oFrameOrderingBasketCell), iIndex, sNote);
				}
				break;
			}
		}
	}

	@Override
	public void frameOrderingBasketModifierCell_QtyClicked(int iCellId,
			int iIndex, String sNote) {
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			if(oFrameOrderingBasketCell.getId() == iCellId){
				for (FrameOrderingBasketSectionListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketModifierCell_QtyClicked(this.getId(), m_oCellList.indexOf(oFrameOrderingBasketCell), iIndex, sNote);
				}
				break;
			}
		}
	}

	@Override
	public void frameOrderingBasketModifierCell_DescClicked(int iCellId,
			int iIndex, String sNote) {
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			if(oFrameOrderingBasketCell.getId() == iCellId){
				for (FrameOrderingBasketSectionListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketModifierCell_DescClicked(this.getId(), m_oCellList.indexOf(oFrameOrderingBasketCell), iIndex, sNote);
				}
				break;
			}
		}
	}

	@Override
	public void frameOrderingBasketModifierCell_PriceClicked(int iCellId,
			int iIndex, String sNote) {
		for(FrameOrderingBasketCell oFrameOrderingBasketCell:m_oCellList){
			if(oFrameOrderingBasketCell.getId() == iCellId){
				for (FrameOrderingBasketSectionListener listener : listeners) {
					// Raise the event to parent
					listener.frameOrderingBasketModifierCell_PriceClicked(this.getId(), m_oCellList.indexOf(oFrameOrderingBasketCell), iIndex, sNote);
				}
				break;
			}
		}
	}
	
}
