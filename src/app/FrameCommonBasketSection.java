package app;

import java.util.ArrayList;

import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FrameCommonBasketSectionListener {
	void frameCommonBasketSection_SectionClicked(int iCellId, String sNote);
	void frameCommonBasketCell_FieldClicked(int iSectionId, int iItemIndex, int iFieldIndex, String sNote);
	void frameCommonBasketCell_FieldLongClicked(int iSectionId, int iItemIndex, int iFieldIndex, String sNote);
}

public class FrameCommonBasketSection extends VirtualUIFrame implements FrameCommonBasketCellListener{
	TemplateBuilder m_oTemplateBuilder;
	
	private ArrayList<FrameCommonBasketCell> m_oCellList;
	
	// Basket Main List View
	private VirtualUIList m_oCommonBasketList;
	
	// Section name
	private VirtualUILabel m_oLabelTitle;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameCommonBasketSectionListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameCommonBasketSectionListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameCommonBasketSectionListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public void init(VirtualUIList oCommonBasketList) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameCommonBasketSectionListener>();
		m_oCellList = new ArrayList<FrameCommonBasketCell>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraCommonBasketSection.xml");
			
		// Section Title
		m_oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTitle, "sectionTitle");
		m_oLabelTitle.setWidth(this.getWidth());
		m_oLabelTitle.setHeight(this.getHeight());
		m_oLabelTitle.setEnabled(true);
		m_oLabelTitle.allowClick(true);
		this.attachChild(m_oLabelTitle);
		
		// Set Main List View
		m_oCommonBasketList = oCommonBasketList;
	}
	
	public String getTitle() {
		return m_oLabelTitle.getValue();
	}
	
	public void setTitle(String[] sSectionTitle) {
		m_oLabelTitle.setValue(sSectionTitle);
	}
	
	public void addItem(int iViewSeq, int iItemIndex, int iLineHeight, ArrayList<Integer> iFieldWidths, ArrayList<String[]> sFieldValues, ArrayList<String> sFieldAligns, ArrayList<String> sFieldTypes, ArrayList<VirtualUIBasicElement> oSubmitIdElements) {
		FrameCommonBasketCell oFrameCell = new FrameCommonBasketCell();
		
		int iLineWidth = this.getWidth();
		
		// Create fields
		oFrameCell.init(sFieldValues.size(), iLineHeight, iLineWidth, sFieldTypes, oSubmitIdElements);
		
		m_oTemplateBuilder.buildFrame(oFrameCell, "fraCommonBasketCell");
		oFrameCell.setWidth(this.getWidth());
		if(iLineHeight > 0)
			oFrameCell.setHeight(iLineHeight);
		else
			oFrameCell.setHeight(this.getHeight());	
		oFrameCell.addListener(this);
		
		int i = 0;
		int iLeft = 0;
		for(String[] sValue:sFieldValues){
			oFrameCell.setFieldValue(i, sValue);
			if(i < iFieldWidths.size()){
				//if(i > 0)
				//	iLeft += 1;
				oFrameCell.setFieldLeft(i, iLeft);
				oFrameCell.setFieldWidth(i, iFieldWidths.get(i));
				
				//text alignment
				if (sFieldAligns.get(i).isEmpty())
					oFrameCell.setFieldAlign(i, HeroActionProtocol.View.Attribute.TextAlign.LEFT + "," + HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
				else {
					oFrameCell.setFieldAlign(i, sFieldAligns.get(i));
					if(sFieldAligns.get(i).contains(HeroActionProtocol.View.Attribute.TextAlign.RIGHT)) {
						oFrameCell.setFieldPadding(i, "0,5,0,0");
					}else if(sFieldAligns.get(i).contains(HeroActionProtocol.View.Attribute.TextAlign.LEFT)){
						oFrameCell.setFieldPadding(i, "0,0,0,5");
					}	
				}
				
				
				iLeft += iFieldWidths.get(i);
				
			}
			i++;
		}
		
//testing start
	/*	
		int iCnt = 0;
		if(sFieldTypes != null){
			for(iCnt=0; iCnt<sFieldTypes.size(); iCnt++){
				if(sFieldTypes.get(iCnt).equals(HeroActionProtocol.View.Type.IMAGE)){
					oFrameCell.setLeft(30);
					oFrameCell.setBackgroundColor("#FF0000");
				}
			}
		}
	*/	
//testing end
		
		
		// Fill the rest header space with color
		if(iLeft < m_oCommonBasketList.getWidth()) {
			oFrameCell.setFieldLeft(i, iLeft);
			oFrameCell.setFieldWidth(i, m_oCommonBasketList.getWidth()-iLeft);
		}
			
		
		oFrameCell.setViewSeq(iViewSeq);

		m_oCellList.add(iItemIndex, oFrameCell);
		
		// Add to main list view
		m_oCommonBasketList.attachChild(oFrameCell);
	}
	
	public void removeItem(int iItemIndex) {
		if(iItemIndex > m_oCellList.size()-1)
			return;
		
		FrameCommonBasketCell oFrameCell = (FrameCommonBasketCell)m_oCellList.get(iItemIndex);
		// Remove the object from the list view
		m_oCommonBasketList.removeChild(oFrameCell.getId());
		
		// Remove from the child item list
		m_oCellList.remove(oFrameCell);
	}
	
	public void removeAllItems(){
		for(FrameCommonBasketCell oFrameCommonBasketCell:m_oCellList){						
			// Remove the object from the list view
			m_oCommonBasketList.removeChild(oFrameCommonBasketCell.getId());
		}
		
		// Clear the list
		m_oCellList.clear();
	}
	
	public int getChildCellCount(){
		int iChildCount = 0;
		for(FrameCommonBasketCell oFrameCommonBasketCell:m_oCellList){
			// Self line
			iChildCount++;
		}
		
		return iChildCount;
	}
	
	public int getChildCellCountBeforeItemIndex(int iItemIndex){
		int iChildCount = 0;
		int i = 0;
		for(FrameCommonBasketCell oFrameCommonBasketCell:m_oCellList){
			if(i >= iItemIndex)
				break;
				
			// Self line
			iChildCount++;

			i++;
		}
		
		return iChildCount;
	}
	
	public int getRowCount() {
		return m_oCellList.size();
	}
	
	public void setFieldValue(int iItemIndex, int iFieldIndex, String[] sValue) {
		if(iItemIndex >= m_oCellList.size())
			return;
		
		FrameCommonBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		oFrameCell.setFieldValue(iFieldIndex, sValue);
	}

	public void setFieldEnabled(int iItemIndex, int iFieldIndex, boolean bEnabled) {
		if(iItemIndex >= m_oCellList.size())
			return;
		
		FrameCommonBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		oFrameCell.setFieldEnabled(iFieldIndex, bEnabled);
	}
	
	public String getFieldValue(int iItemIndex, int iFieldIndex) {
		if(iItemIndex >= m_oCellList.size())
			return "";
		
		FrameCommonBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		return oFrameCell.getFieldValue(iFieldIndex);
	}
	
	public void setFieldInfo1(int iItemIndex, int iFieldIndex, String sValue) {
		if(iItemIndex >= m_oCellList.size())
			return;
		
		FrameCommonBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		oFrameCell.setFieldInfo1(iFieldIndex, sValue);
	}
	
	public void setFieldInfo2(int iItemIndex, int iFieldIndex, String sValue) {
		if(iItemIndex >= m_oCellList.size())
			return;
		
		FrameCommonBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		oFrameCell.setFieldInfo2(iFieldIndex, sValue);
	}
	
	public void setFieldInfo1BackgroundColor(int iItemIndex, int iFieldIndex, String sBackground) {
		FrameCommonBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		oFrameCell.setFieldInfo1BackgroundColor(iFieldIndex, sBackground);
	}
	
	public void setFieldInfo2BackgroundColor(int iItemIndex, int iFieldIndex, String sBackground) {
		FrameCommonBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		oFrameCell.setFieldInfo2BackgroundColor(iFieldIndex, sBackground);
	}
	
	// Set field to be edited
	public VirtualUIBasicElement setEditField(int iItemIndex, int iFieldIndex, boolean bEdit) {
		if(iItemIndex >= m_oCellList.size())
			return null;
		
		FrameCommonBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		return oFrameCell.setEditField(iFieldIndex, bEdit);
	}
	
	// Get edit field value
	public String getEditFieldValue(int iItemIndex) {
		if(iItemIndex >= m_oCellList.size())
			return "";
		
		FrameCommonBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		return oFrameCell.getEditFieldValue();
	}
	
	// Set field background color
	public void setFieldBackgroundColor(int iItemIndex, int iFieldIndex, String sBackgroundColor) {
		if(iItemIndex >= m_oCellList.size())
			return;
		
		FrameCommonBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		oFrameCell.setFieldBackgroundColor(iFieldIndex, sBackgroundColor);
	}
	
	// Set field background color
	public void setFieldForegroundColor(int iItemIndex, int iFieldIndex, String sForegroundColor) {
		if(iItemIndex >= m_oCellList.size())
			return;
		
		FrameCommonBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		oFrameCell.setFieldForegroundColor(iFieldIndex, sForegroundColor);
	}
	
	public void setAllFieldsForegroundColor(int iItemIndex, String sForegroundColor) {
		FrameCommonBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		for(Integer oElementKey: oFrameCell.getFieldList().keySet()) {
			oFrameCell.setFieldForegroundColor(oElementKey, sForegroundColor);
		}
	}
	
	public void setAllFieldsBackgroundColor(int iItemIndex, String sBackgroundColor) {
		FrameCommonBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		for(Integer oElementKey: oFrameCell.getFieldList().keySet()) {
			oFrameCell.setFieldBackgroundColor(oElementKey, sBackgroundColor);
		}
	}
	
	// Set field text size
	public void setFieldTextSize(int iItemIndex, int iFieldIndex, int iTextSize) {
		if(iItemIndex >= m_oCellList.size())
			return;
		
		FrameCommonBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		oFrameCell.setFieldTextSize(iFieldIndex, iTextSize);
	}
	
	// Set field padding
	public void setFieldPadding(int iItemIndex, int iFieldIndex, String sTextAlign) {
		if(iItemIndex >= m_oCellList.size())
			return;
		
		FrameCommonBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		oFrameCell.setFieldPadding(iFieldIndex, sTextAlign);
	}

	public void setFieldClickServerRequestNote(int iItemIndex, int iFieldIndex, String sNote) {
		if(iItemIndex >= m_oCellList.size())
			return;
		
		FrameCommonBasketCell oFrameCell = m_oCellList.get(iItemIndex);
		oFrameCell.setFieldClickServerRequestNote(iFieldIndex, sNote);
	}

	// Set title background color
	public void setTitleBackgroundColor(String sBackgroundColor) {
		m_oLabelTitle.setBackgroundColor(sBackgroundColor);
	}
	
	// Set title foreground color
	public void setTitleForegroundColor(String sForegroundColor) {
		m_oLabelTitle.setForegroundColor(sForegroundColor);
	}
	
	// Set title padding
	public void setTitlePadding(String sPaddingValue) {
		m_oLabelTitle.setPaddingValue(sPaddingValue);
	}
	
	
	// Set title text size
	public void setTitleTextSize(int iTextSize) {
		m_oLabelTitle.setTextSize(iTextSize);
	}
	
	
	// Set line visible
	public void setLineVisible(int iItemIndex, boolean bVisible){
		if(iItemIndex >= m_oCellList.size())
			return;
		
		FrameCommonBasketCell oFrameCommonBasketCell = m_oCellList.get(iItemIndex);
		oFrameCommonBasketCell.setVisible(bVisible);
	}
	
	public void setCellSubmitId(VirtualUIBasicElement oElement) {
		for(FrameCommonBasketCell oBasketCell:m_oCellList) {
			oBasketCell.setFieldSubmitId(oElement);
		}
	}
	
	public ArrayList<FrameCommonBasketCell> getCellList() {
		return m_oCellList;
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (iChildId == m_oLabelTitle.getId()) {
			for (FrameCommonBasketSectionListener listener : listeners) {
				// Raise the event to parent
				listener.frameCommonBasketSection_SectionClicked(this.getId(), sNote);
			}
			bMatchChild = true;
		}
		return bMatchChild;
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iCellId,
			int iFieldIndex, String sNote) {
		for(FrameCommonBasketCell oFrameCommonBasketCell:m_oCellList){
			if(oFrameCommonBasketCell.getId() == iCellId){
				for (FrameCommonBasketSectionListener listener : listeners) {
					// Raise the event to parent
					listener.frameCommonBasketCell_FieldClicked(this.getId(), m_oCellList.indexOf(oFrameCommonBasketCell), iFieldIndex, sNote);
				}
				break;
			}
		}
	}
	
	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iCellId,
			int iFieldIndex, String sNote) {
		for(FrameCommonBasketCell oFrameCommonBasketCell:m_oCellList){
			if(oFrameCommonBasketCell.getId() == iCellId){
				for (FrameCommonBasketSectionListener listener : listeners) {
					// Raise the event to parent
					listener.frameCommonBasketCell_FieldLongClicked(this.getId(), m_oCellList.indexOf(oFrameCommonBasketCell), iFieldIndex, sNote);
				}
				break;
			}
		}
	}
	
}
