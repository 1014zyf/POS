package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import externallib.StringLib;

import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FrameCommonBasketListener {
	void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote);
    void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote);
    void frameCommonBasketCell_FieldLongClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote);
	void frameCommonBasketCell_HeaderClicked(int iFieldIndex);
}

public class FrameCommonBasket<T> extends VirtualUIFrame implements FrameCommonBasketSectionListener{
	
	TemplateBuilder m_oTemplateBuilder;
	private int m_iCurrentSelectedSectionId = 0;
	private HashMap<Integer, VirtualUILabel> m_oLabelHeaders;
	private VirtualUIList m_oCommonBasketList;
	private ArrayList<FrameCommonBasketSection> m_oSectionList;
	
	private VirtualUIFrame m_oUpperlineFrame;
	private VirtualUIFrame m_oUnderlineFrame;
	private VirtualUIFrame m_oBottomUnderlineFrame;
	private VirtualUIFrame m_oHeaderFrame;
	private VirtualUIFrame m_oSelectedSectionFrame;
	
	// For edit field
	private int m_iCurrentEditSectionIndex = -1;
	private int m_iCurrentEditItemIndex = -1;
	private int m_iCurrentEditFieldIndex = -1;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameCommonBasketListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameCommonBasketListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameCommonBasketListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
	
    public int getSelectedSectionId(){
    	return this.m_iCurrentSelectedSectionId;
    }
    
	public void init(){	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameCommonBasketListener>();
		m_oLabelHeaders = new HashMap<Integer, VirtualUILabel>();
		m_oSectionList = new ArrayList<FrameCommonBasketSection>();
		m_oUpperlineFrame = new VirtualUIFrame();
		m_oUnderlineFrame = new VirtualUIFrame();
		m_oBottomUnderlineFrame = new VirtualUIFrame();
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraCommonBasket.xml");
		
		// Load child elements from template
		m_oCommonBasketList = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oCommonBasketList, "listCommonBasket");
		m_oCommonBasketList.setWidth(this.getWidth());
		m_oCommonBasketList.setHeight(this.getHeight());
		this.attachChild(m_oCommonBasketList);
		
		// Load selected section from template
		m_oSelectedSectionFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oSelectedSectionFrame, "fraSelectedBasketSection");
		this.attachChild(m_oSelectedSectionFrame);
		
		// Current selected section id (0 -> shared)
		m_iCurrentSelectedSectionId = 0;
	}
	
	public void addHeader(ArrayList<Integer> iFieldWidths, ArrayList<T> sFieldValues) {		
		int i = 0;
		int iLeft = 0;
		int iHeaderHeight = 0;
		m_oUpperlineFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oUpperlineFrame, "fraUpperline");
		m_oUpperlineFrame.setWidth(this.getWidth());
		m_oUpperlineFrame.setVisible(false);
		this.attachChild(m_oUpperlineFrame);
		
		m_oUnderlineFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oUnderlineFrame, "fraUnderline");
		m_oUnderlineFrame.setWidth(this.getWidth());
		this.attachChild(m_oUnderlineFrame);
		m_oHeaderFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oHeaderFrame, "fraHeader");
		m_oHeaderFrame.setWidth(this.getWidth());
		this.attachChild(m_oHeaderFrame);
		
		for(T sValue:sFieldValues){
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblHeader");	
			oLabel.allowClick(true);
			
			if (sValue instanceof String)
				oLabel.setValue((String)sValue);
			else
				oLabel.setValue((String[])sValue);
			if(i<iFieldWidths.size()){
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
		
		m_oCommonBasketList.setHeight(this.getHeight() - iHeaderHeight);
	}
	
	public void setHeaderUnderlineColor(String sColor) {
		m_oUnderlineFrame.setBackgroundColor(sColor);
	}
	
	public void setHeaderTextAlign(int iHeaderId, String sTextAlign) {
		VirtualUILabel oLabel = m_oLabelHeaders.get(iHeaderId);
		oLabel.setTextAlign(sTextAlign);
		
		if(sTextAlign.contains(HeroActionProtocol.View.Attribute.TextAlign.LEFT))
			oLabel.setPaddingValue("0,0,0,5");
		else if(sTextAlign.contains(HeroActionProtocol.View.Attribute.TextAlign.RIGHT))
			oLabel.setPaddingValue("0,5,0,0");
	}
	
	public void setHeaderPadding(int iHeaderId, String sPadding) {
		VirtualUILabel oLabel = m_oLabelHeaders.get(iHeaderId);
		oLabel.setPaddingValue(sPadding);
	}
	
	public void setHeaderVisible(boolean bShow) {
		if (!bShow)
			m_oCommonBasketList.setTop(0);
	}
	
	//Set header format : line Height, text size, padding value
	public void setHeaderFormat(int iHeight, int iTextSize, String sPadding){
		for(int i = 0; i < m_oLabelHeaders.size(); i++){
			VirtualUILabel oLabel = m_oLabelHeaders.get(i);
			
			if(iHeight > 0 )
				oLabel.setHeight(iHeight);
			if(iTextSize > 0)
				oLabel.setTextSize(iTextSize);
			if(!sPadding.isEmpty())
				oLabel.setPaddingValue(sPadding);
			oLabel.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.LEFT + "," + HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		}
		
		if(iHeight != 0) {
			m_oHeaderFrame.setHeight(iHeight);
			m_oCommonBasketList.setTop(m_oHeaderFrame.getTop() + iHeight + 2);
			m_oUnderlineFrame.setTop(m_oCommonBasketList.getTop());
		}
	}
	
	public void setUnderlineFrameVisible(boolean bValue) {
		m_oUnderlineFrame.setVisible(bValue);
		return;
	}
	
	public void addSection(int iSectionId, String[] sSectionTitle, boolean bIsShow) {
		FrameCommonBasketSection oFrameSection = new FrameCommonBasketSection();
		m_oTemplateBuilder.buildFrame(oFrameSection, "fraCommonBasketSection");
		oFrameSection.setWidth(m_oCommonBasketList.getWidth());
		
		oFrameSection.init(m_oCommonBasketList);
		oFrameSection.setTitle(sSectionTitle);
		oFrameSection.addListener(this);
		oFrameSection.setEnabled(true);
		oFrameSection.setVisible(bIsShow);
		
		m_oBottomUnderlineFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oBottomUnderlineFrame, "fraBottomUnderline");
		m_oBottomUnderlineFrame.setTop(this.getHeight()-1);
		m_oBottomUnderlineFrame.setWidth(this.getWidth());
		this.attachChild(m_oBottomUnderlineFrame);
		
		// Get the view sequence for this new add section
		int iViewSeq = 0;
		int i = 0;
		for(FrameCommonBasketSection oFrameCommonBasketSection: m_oSectionList){
			if(i >= iSectionId){
				break;
			}
			// Count the section line
			iViewSeq++;
			
			// Count all line in previous section
			iViewSeq += oFrameCommonBasketSection.getChildCellCount();
		}
		iViewSeq++;
		
		oFrameSection.setViewSeq(iViewSeq);

		m_oSectionList.add(iSectionId, oFrameSection);
		
		// Add to main list view
		m_oCommonBasketList.attachChild(oFrameSection);
	}

	public void removeSection(int iSectionId) {
		if (m_oSectionList.size() <= iSectionId)
			// No such section
			return;
		
		// Remove the target section
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		// Remove the object from the list view
		m_oCommonBasketList.removeChild(oFrameSection.getId());
		
		// Remove from the section list
		m_oSectionList.remove(iSectionId);
		if(m_iCurrentSelectedSectionId == iSectionId){
			// If the removed section is current selected, move back to 1st section
			setSelectedSection(0);
		}
	}
	
	public void removeAllSections() {
		// clear all sections
		m_oCommonBasketList.removeAllChildren();
		m_oSectionList.clear();
	}

	public void clearAllSections(){
		int i = 0;
		for(FrameCommonBasketSection oFrameCommonBasketSection: m_oSectionList){
			if(i > 0){
				if(oFrameCommonBasketSection.getVisible())
					oFrameCommonBasketSection.setVisible(false);
			}
			oFrameCommonBasketSection.removeAllItems();
			i++;
		}
	}
	
	public void removeAllItems(int iSectionId) {
		FrameCommonBasketSection oFrameCommonBasketSection = m_oSectionList.get(iSectionId);
		oFrameCommonBasketSection.removeAllItems();
	}
	
	/**
	 * removeAllItems
	 * 
	 */
	public void removeAllItems() {
		for (FrameCommonBasketSection cc : m_oSectionList) {
			cc.removeAllItems();
		}
	}
	
	public void addItem(int iSectionId, int iItemIndex, int iLineHeight, ArrayList<Integer> iFieldWidths, ArrayList<T> sFieldValues, ArrayList<String> sFieldAligns, ArrayList<String> sFieldTypes, ArrayList<VirtualUIBasicElement> oSubmitIdElements) {
		if (m_oSectionList.size() <= iSectionId)
			// No such section
			return;
		
		// Get the view sequence for this new add section
		int iViewSeq = 0;
		int i = 0;
		for(FrameCommonBasketSection oFrameCommonBasketSection: m_oSectionList){
			if(i > iSectionId){
				break;
			}
			// Count the section line
			iViewSeq++;
			
			if(i < iSectionId){
				// Count all line in previous section
				iViewSeq += oFrameCommonBasketSection.getChildCellCount();
			}else{
				// Count the self section line before the item index
				iViewSeq += oFrameCommonBasketSection.getChildCellCountBeforeItemIndex(iItemIndex);
			}
			i++;
		}
		iViewSeq++;

		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		
		ArrayList<String[]> oFieldValuesArray = new ArrayList<>();
		for (T oFieldValue:sFieldValues) {
			if (oFieldValue instanceof String)
				oFieldValuesArray.add(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, (String)oFieldValue));
			else
				oFieldValuesArray.add(Arrays.copyOf((String[]) oFieldValue, ((String[]) oFieldValue).length));
		}
		oFrameSection.addItem(iViewSeq, iItemIndex, iLineHeight, iFieldWidths, oFieldValuesArray, sFieldAligns, sFieldTypes, oSubmitIdElements);
		
		// Clear edit field
		m_iCurrentEditSectionIndex = -1;
		m_iCurrentEditItemIndex = -1;
		m_iCurrentEditFieldIndex = -1;
	}

	public void removeItem(int iSectionId, int iItemIndex) {
		if (m_oSectionList.size() <= iSectionId)
			// No such section
			return;
		
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.removeItem(iItemIndex);
	}
	
	// Select section and change section color
	public void setSelectedSection(int iSectionId) {
		if (iSectionId != m_iCurrentSelectedSectionId) {
			if (m_oSectionList.size() > m_iCurrentSelectedSectionId){
				FrameCommonBasketSection frmSection = m_oSectionList.get(m_iCurrentSelectedSectionId);
				frmSection.setTitleForegroundColor(frmSection.getForegroundColor());
			}
		}
		
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setTitleForegroundColor(m_oSelectedSectionFrame.getForegroundColor());
		m_iCurrentSelectedSectionId = iSectionId;
	}
	
	//Make the selected section be visible
	public void setSectionVisible(int iSectionId, boolean bShow) {
		try {
			FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
			oFrameSection.setVisible(bShow);
		}
		catch (Exception e) {
			AppGlobal.stack2Log(e);
			return;
		}
	}
	
	public int getNumberOfVisibleSections() {
		int iVisibleSection = 1;			//The "Shared" section
		
		for(int i = 1; i < m_oSectionList.size(); i++) {
			FrameCommonBasketSection oFrameSection = m_oSectionList.get(i);
			if(oFrameSection.getVisible()) {
				iVisibleSection++;
			}
		}
		
		return iVisibleSection;
	}
	
	public boolean isSectionVisible(int iSectionId) {
		try {
			FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
			return oFrameSection.getVisible();
		}
		catch (Exception e) {
			AppGlobal.stack2Log(e);
			return false;
		}
	}
	
	// Set field value visible
	public void setLineVisible(int iSectionId, int iItemIndex, boolean bVisible) {
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setLineVisible(iItemIndex, bVisible);
	}
	
	// Set field value
	public void setFieldValue(int iSectionId, int iItemIndex, int iFieldIndex, String sValue) {
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		String[] sFieldValueArray = StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, sValue);
		oFrameSection.setFieldValue(iItemIndex, iFieldIndex, sFieldValueArray);
	}
	
	// Get field value
	public String getFieldValue(int iSectionId, int iItemIndex, int iFieldIndex) {
		if(iSectionId >= m_oSectionList.size())
			return "";
		
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		return oFrameSection.getFieldValue(iItemIndex, iFieldIndex);
	}
	
	// Set field information 1
	public void setFieldInfo1(int iSectionId, int iItemIndex, int iFieldIndex, String sValue) {
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setFieldInfo1(iItemIndex, iFieldIndex, sValue);
	}
	
	// Set field information 2
	public void setFieldInfo2(int iSectionId, int iItemIndex, int iFieldIndex, String sValue) {
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setFieldInfo2(iItemIndex, iFieldIndex, sValue);
	}
	
	// Set field information 1 background color 
	public void setFieldInfo1BackgroundColor(int iSectionId, int iItemIndex, int iFieldIndex, String sBackgroundColor) {
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setFieldInfo1BackgroundColor(iItemIndex, iFieldIndex, sBackgroundColor);
	}
	
	// Set field information 2 background color 
	public void setFieldInfo2BackgroundColor(int iSectionId, int iItemIndex, int iFieldIndex, String sBackgroundColor) {
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setFieldInfo2BackgroundColor(iItemIndex, iFieldIndex, sBackgroundColor);
	}
	
	// Set field enabled
	public void setFieldEnabled(int iSectionId, int iItemIndex, int iFieldIndex, boolean bEnabled) {
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setFieldEnabled(iItemIndex, iFieldIndex, bEnabled);
	}
	
	// Set field to be edited
	public VirtualUIBasicElement setEditField(int iSectionId, int iItemIndex, int iFieldIndex) {
		// Set the old field to not edit
		if(m_iCurrentEditSectionIndex > -1){
			FrameCommonBasketSection oFrameSection = m_oSectionList.get(m_iCurrentEditSectionIndex);
			oFrameSection.setEditField(m_iCurrentEditItemIndex, m_iCurrentEditFieldIndex, false);
		}
		
		m_iCurrentEditSectionIndex = iSectionId;
		m_iCurrentEditItemIndex = iItemIndex;
		m_iCurrentEditFieldIndex = iFieldIndex;

		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		VirtualUIBasicElement oTextField = oFrameSection.setEditField(iItemIndex, iFieldIndex, true);
		
		for(FrameCommonBasketSection oBasketSection:m_oSectionList) {
			oBasketSection.setCellSubmitId(oTextField);
		}
		
		return oTextField;
	}
	
	// Get edit field value
	public String getEditFieldValue() {
		String sValue = "";
		
		// Set the old field to not edit
		if(m_iCurrentEditSectionIndex > -1){
			FrameCommonBasketSection oFrameSection = m_oSectionList.get(m_iCurrentEditSectionIndex);			
			sValue = oFrameSection.getEditFieldValue(m_iCurrentEditItemIndex);
		}
		
		return sValue;
	}
	
	public void clearEditField(){
		// Set the old field to not edit
		if(m_iCurrentEditSectionIndex > -1){
			FrameCommonBasketSection oFrameSection = m_oSectionList.get(m_iCurrentEditSectionIndex);
			oFrameSection.setEditField(m_iCurrentEditItemIndex, m_iCurrentEditFieldIndex, false);
		}
		
		m_iCurrentEditSectionIndex = -1;
		m_iCurrentEditItemIndex = -1;
		m_iCurrentEditFieldIndex = -1;
	}
	
	// Move the basket item scroll view position
	// iPosition:	1 - To top
	//				2 - To bottom
	public void moveScrollToItem(int iSectionId, int iItemIndex) {		
		// Get the view sequence for this new add section
		int iViewSeq = 0;
		int i = 0;
		for(FrameCommonBasketSection oFrameCommonBasketSection: m_oSectionList){
			if(i > iSectionId){
				break;
			}
			// Count the section line
			if (oFrameCommonBasketSection.getVisible())
				iViewSeq++;
			
			if(i < iSectionId){
				// Count all line in previous section
				iViewSeq += oFrameCommonBasketSection.getChildCellCount();
			}else{
				// Count the self section line before the item index
				iViewSeq += oFrameCommonBasketSection.getChildCellCountBeforeItemIndex(iItemIndex);
			}
			i++;
		}
		
		m_oCommonBasketList.scrollToIndex(iViewSeq);
	}
	
	// Move the basket to target section last line
	public void moveScrollToSectionBottom(int iSectionId) {
		// Get the view sequence for this new add section
		int iViewSeq = 0;
		int i = 0;
		for(FrameCommonBasketSection oFrameCommonBasketSection: m_oSectionList){
			if(i > iSectionId){
				break;
			}
			// Count the section line
			iViewSeq++;
			
			if(i <= iSectionId){
				// Count all line in previous section
				iViewSeq += oFrameCommonBasketSection.getChildCellCount();
			}
			i++;
		}
		
		m_oCommonBasketList.scrollToIndex(iViewSeq-1);
	}
	
	// Move the basket to last line
	public void moveScrollToBottom() {
		m_oCommonBasketList.scrollToPosition(2);
	}
	
	// Move the basket to first line
	public void moveScrollToTop() {
		m_oCommonBasketList.scrollToPosition(1);
	}
	
	// Get number of item under the section
	public int getItemCellCount(int iSectionId){
		if (m_oSectionList.size() <= iSectionId)
			// No such section
			return 0;
		
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		return oFrameSection.getChildCellCount();
	}

	// Get number of item under the section
	public int getItemCount(int iSectionId){
		if (m_oSectionList.size() <= iSectionId)
			// No such section
			return 0;
		
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		return oFrameSection.getRowCount();
	}
	
	// Set section background color
	public void setSectionBackgroundColor(int iSectionId, String sBackgroundColor) {
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setTitleBackgroundColor(sBackgroundColor);
	}
	
	// Set section foreground color
	public void setSectionForegroundColor(int iSectionId, String sForegroundColor) {
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setTitleForegroundColor(sForegroundColor);
	}
	
	// Set field background color
	public void setFieldBackgroundColor(int iSectionId, int iItemIndex, int iFieldIndex, String sBackgroundColor) {		
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setFieldBackgroundColor(iItemIndex, iFieldIndex, sBackgroundColor);
	}

	// Set field foreground color
	public void setFieldForegroundColor(int iSectionId, int iItemIndex, int iFieldIndex, String sForegroundColor) {		
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setFieldForegroundColor(iItemIndex, iFieldIndex, sForegroundColor);
	}
	
	public void setAllFieldsForegroundColor(int iSectionId, int iItemIndex, String sBackgroundColor) {
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setAllFieldsForegroundColor(iItemIndex, sBackgroundColor);
	}

	public void setAllFieldsBackgroundColor(int iSectionId, int iItemIndex, String sBackgroundColor) {
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setAllFieldsBackgroundColor(iItemIndex, sBackgroundColor);
	}
	
	// Set field text size
	public void setFieldTextSize(int iSectionId, int iItemIndex, int iFieldIndex, int iTextSize) {		
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setFieldTextSize(iItemIndex, iFieldIndex, iTextSize);
	}
	
	// Set section padding
	public void setSectionPadding(int iSection, String sPaddingValue) {
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSection);
		oFrameSection.setTitlePadding(sPaddingValue);
	}
	
	// Set field text align
	public void setFieldPadding(int iSectionId, int iItemIndex, int iFieldIndex, String sPadding) {
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setFieldPadding(iItemIndex, iFieldIndex, sPadding);
	}

	// Set field text align
	public void setFieldClickServerRequestNote(int iSectionId, int iItemIndex, int iFieldIndex, String sNote) {
		FrameCommonBasketSection oFrameSection = m_oSectionList.get(iSectionId);
		oFrameSection.setFieldClickServerRequestNote(iItemIndex, iFieldIndex, sNote);
	}
	
	public void setUpperUnderLineShow(boolean bShow) {
		int iFrameHeightCount = 40;
		
		for(FrameCommonBasketSection oSection : m_oSectionList) {
			ArrayList<FrameCommonBasketCell> oCellList = oSection.getCellList();
			for(FrameCommonBasketCell oCell : oCellList)
				iFrameHeightCount += oCell.getHeight();
		}
		if(iFrameHeightCount <= m_oCommonBasketList.getHeight() || this.getHeight() <= m_oCommonBasketList.getHeight())
			this.setHeight(iFrameHeightCount);
		else
			this.setHeight(m_oCommonBasketList.getHeight() + 40);
		m_oBottomUnderlineFrame.setVisible(bShow);
	}
	
	public void setUpperlineVisible(boolean bShown){
		m_oUpperlineFrame.setVisible(bShown);
	}
	
	public void setCashierCommonBasketHeight(int iMaxHeight) {
		int iFrameHeightCount = 43;
		for(FrameCommonBasketSection oSection : m_oSectionList) {
			ArrayList<FrameCommonBasketCell> oCellList = oSection.getCellList();
			for(FrameCommonBasketCell oCell : oCellList)
				iFrameHeightCount += oCell.getHeight() + oCell.getFieldInfo1Size() * 5 + oCell.getFieldInfo2Size() * 5;
		}
		
		if(iFrameHeightCount <= iMaxHeight)
			this.setHeight(iFrameHeightCount);
		else
			this.setHeight(m_oCommonBasketList.getHeight() + 40);
	}
	
	public void setBottomUnderlineVisible(boolean bShown){
		m_oBottomUnderlineFrame.setVisible(bShown);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		int iFieldIndex = 0;
		for(Entry<Integer, VirtualUILabel> entry: m_oLabelHeaders.entrySet()){
			VirtualUIBasicElement oLabel = entry.getValue();
			if (iChildId == oLabel.getId()) {
				for (FrameCommonBasketListener listener : listeners) {
					// Raise the event to parent
					listener.frameCommonBasketCell_HeaderClicked(iFieldIndex);
				}
				
				bMatchChild = true;
				break;
			}
			iFieldIndex++;
		}
		return bMatchChild;
	}
	
	// Section line is clicked
	@Override
	public void frameCommonBasketSection_SectionClicked(int iCellId, String sNote){
		for(FrameCommonBasketSection oFrameCommonBasketSection: m_oSectionList){
			if (iCellId == oFrameCommonBasketSection.getId()) {
				// Section is clicked, set current selected section
				int iSectionId = m_oSectionList.indexOf(oFrameCommonBasketSection);
				this.setSelectedSection(iSectionId);
				
				for(FrameCommonBasketListener listener: listeners) {
					listener.frameCommonBasketSection_SectionClicked(iSectionId, sNote);
				}
				break;
			}
		}
	}
	
	// Item qty is clicked
	@Override
	public void frameCommonBasketCell_FieldClicked(int iSectionId, int iItemIndex, int iFieldIndex, String sNote) {
		for(FrameCommonBasketSection oFrameCommonBasketSection: m_oSectionList){
			if (iSectionId == oFrameCommonBasketSection.getId()) {
				int iSectionIndex = m_oSectionList.indexOf(oFrameCommonBasketSection);
				for (FrameCommonBasketListener listener : listeners) {
					// Raise the event to parent
					listener.frameCommonBasketCell_FieldClicked(this.getId(), iSectionIndex, iItemIndex, iFieldIndex, sNote);
				}
				break;
			}
		}
	}
	
	// Item qty is long clicked
	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iSectionId, int iItemIndex, int iFieldIndex, String sNote) {
		for(FrameCommonBasketSection oFrameCommonBasketSection: m_oSectionList){
			if (iSectionId == oFrameCommonBasketSection.getId()) {
				int iSectionIndex = m_oSectionList.indexOf(oFrameCommonBasketSection);
				for (FrameCommonBasketListener listener : listeners) {
					// Raise the event to parent
					listener.frameCommonBasketCell_FieldLongClicked(this.getId(), iSectionIndex, iItemIndex, iFieldIndex, sNote);
				}
				break;
			}
		}
	}
}
