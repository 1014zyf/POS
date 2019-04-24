package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;

/** interface for the listeners/observers callback method */
interface FrameColumnViewListener {
	void frameColumnViewListener_clickColumnHeader(int iColumnIndex);
	void frameColumnViewListener_clickPlaceButton(int iTargetColumnIndex, int iTargetSectionIndex);
	boolean frameColumnViewListener_isMinimumChargeItem(FuncCheckItem oFuncCheckItem);
}

public class FrameColumnView extends VirtualUIFrame implements FrameColumnElementListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private static int PADDING = 0;
	private int m_iColumnCount;
	
	private VirtualUIList m_oScrollArea;
	private HashMap<Integer, VirtualUILabel> m_oLabelColumnHeader;
	private HashMap<Integer, VirtualUIFrame> m_oSectionFrames;
	private HashMap<String, FrameColumnElement> m_oColumnElements;
	private HashMap<Integer, VirtualUIFrame> m_oFrameColumnBackground;
	private HashMap<Integer, VirtualUIImage> m_oFrameColumnHeaderImage;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameColumnViewListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameColumnViewListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameColumnViewListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameColumnView(){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameColumnViewListener>();
		m_oLabelColumnHeader = new HashMap<Integer, VirtualUILabel>();
		m_oSectionFrames = new HashMap<Integer, VirtualUIFrame>();
		m_oColumnElements = new HashMap<String, FrameColumnElement>();
		m_oFrameColumnBackground = new HashMap<Integer, VirtualUIFrame>();
		m_oFrameColumnHeaderImage = new HashMap<Integer, VirtualUIImage>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraColumnView.xml");
	}
	
	public void init(int iColumnCount, boolean bHideHeaderButton){
		
		m_iColumnCount = iColumnCount;
		//int iBottonHeight=0;
		
		// Calculate the width
		FrameColumnElement oFrameColumnElement = new FrameColumnElement(0, 0, false);
		m_oTemplateBuilder.buildFrame(oFrameColumnElement, "columnElement");
		
		// Resize the view
		int iWidth = (oFrameColumnElement.getWidth() * m_iColumnCount) + (PADDING * (m_iColumnCount - 1));
		this.setWidth(iWidth);
		
		// Create the column header button
		int iLeft = 0;
		int iHeight = 0;
		if(bHideHeaderButton == false){
			for(int i=0; i<m_iColumnCount; i++){
				
				VirtualUIFrame oButton = new VirtualUIFrame();
				m_oTemplateBuilder.buildFrame(oButton, "columnHeader");
				oButton.setLeft(iLeft);
				// enlarge the width of the column view button to fit the size
				oButton.setWidth(oFrameColumnElement.getWidth()+1);
				
				VirtualUILabel oLabel = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(oLabel, "columnHeaderLabel");
				oLabel.setWidth(oFrameColumnElement.getWidth());
				oLabel.allowClick(true);
				oButton.attachChild(oLabel);
				
				VirtualUIImage oImage = new VirtualUIImage();
				m_oTemplateBuilder.buildImage(oImage, "columnHeaderImage");
				oImage.setLeft(oButton.getWidth() - oImage.getWidth() - 16);
				oImage.setTop((oButton.getHeight() - oImage.getHeight())/2);
				oImage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_o1_setting.png");
				oImage.allowClick(true);
				
				oImage.setClickServerRequestBlockUI(true);
				oButton.attachChild(oImage);
				
				// Create a backgound frame
				if((i%2) == 1){
					VirtualUIFrame oBackgroundFrame = new VirtualUIFrame();
					m_oTemplateBuilder.buildFrame(oBackgroundFrame, "columnBackground");
					oBackgroundFrame.setLeft(iLeft);
					oBackgroundFrame.setWidth(oButton.getWidth());
					oBackgroundFrame.setHeight(this.getHeight() - this.getStroke());
					this.attachChild(oBackgroundFrame);
					m_oFrameColumnBackground.put(i, oBackgroundFrame);
				}
				
				this.attachChild(oButton);
				
				iHeight = oButton.getTop() + oButton.getHeight();
				//iBottonHeight = oButton.getHeight();
				
				iLeft += (oFrameColumnElement.getWidth() + PADDING);
				
				m_oLabelColumnHeader.put(i, oLabel);
				m_oFrameColumnHeaderImage.put(i, oImage);
			}
		}
		
		// Create the list view
		m_oScrollArea = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oScrollArea, "scrollArea");
		if(bHideHeaderButton)
			m_oScrollArea.setTop(0);
		else
			m_oScrollArea.setTop(iHeight);
		m_oScrollArea.setWidth(iWidth);
		m_oScrollArea.setHeight(this.getHeight() - iHeight);
		this.attachChild(m_oScrollArea);
	}
	
	public void extendColumnViewScrollAreaHeight(int iHeight) {
		m_oScrollArea.setHeight(iHeight);
	}
	
	public void updateColumnHeader(int iColumnIndex, String sColumnHeader){
		if(m_oLabelColumnHeader.containsKey(iColumnIndex) == false)
			return;
		
		VirtualUILabel oLabel = m_oLabelColumnHeader.get(iColumnIndex);
		oLabel.setValue(sColumnHeader);
	}
	
	public void updateColumnBackgroundHeight(int iColumnIndex, int iHeight) {
		if(m_oFrameColumnBackground.containsKey(iColumnIndex) == false)
			return;
		
		VirtualUIFrame oBackgroundFrame = m_oFrameColumnBackground.get(iColumnIndex);
		oBackgroundFrame.setHeight(iHeight);
	}
	
	public void addSection(int iSectionIndex, String sSectionDesc, boolean bShowPlaceButton){
		int iFrameHeight = 0;
		VirtualUIFrame oFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrame, "section");
		oFrame.setWidth(this.getWidth());
		
		// Section label
		VirtualUILabel oLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabel, "sectionDesc");
		oLabel.setWidth(this.getWidth());
		oLabel.setValue(sSectionDesc);
		oFrame.attachChild(oLabel);
		iFrameHeight += oLabel.getHeight();
		
		int iLeft = 0;
		for(int i=0; i<m_iColumnCount; i++){
			FrameColumnElement oFrameColumnElement = new FrameColumnElement(i, iSectionIndex, bShowPlaceButton);
			m_oTemplateBuilder.buildFrame(oFrameColumnElement, "columnElement");
			oFrameColumnElement.setLeft(iLeft);
			oFrameColumnElement.addListener(this);
			oFrame.attachChild(oFrameColumnElement);
			if(i == 0)
				iFrameHeight += oFrameColumnElement.getHeight();
			
			iLeft += (oFrameColumnElement.getWidth() + PADDING);
			
			String sKey = i + "_" + iSectionIndex;
			m_oColumnElements.put(sKey, oFrameColumnElement);
		}
		
		oFrame.setViewSeq(iSectionIndex + 1);
		oFrame.setHeight(iFrameHeight);
		m_oScrollArea.attachChild(oFrame);
		
		m_oSectionFrames.put(iSectionIndex, oFrame);
		
		updateScreen(iSectionIndex);
	}
	
	public void addSectionToElement(int iColumnIndex, int iSectionIndex, int iElementSectionIndex, String sElementSectionDesc, boolean bShow){
		String sKey = iColumnIndex + "_" + iSectionIndex;
		if(m_oColumnElements.containsKey(sKey) == false)
			return;
		
		FrameColumnElement oFrameColumnElement = m_oColumnElements.get(sKey);
		oFrameColumnElement.addSection(iElementSectionIndex, sElementSectionDesc, bShow);
			
		updateScreen(iSectionIndex);
	}
	
	public Integer addItemToElement(int iColumnIndex, int iSectionIndex, int iElementSectionIndex, FuncCheckItem oFuncCheckItem){
		String sKey = iColumnIndex + "_" + iSectionIndex;
		if(m_oColumnElements.containsKey(sKey) == false)
			return -1;
		
		FrameColumnElement oFrameColumnElement = m_oColumnElements.get(sKey);
		int iItemIndex = oFrameColumnElement.addItem(iElementSectionIndex, oFuncCheckItem);
		
		updateScreen(iSectionIndex);
		
		return iItemIndex; 
	}
	
	public FuncCheckItem removeItemFromElement(int iColumnIndex, int iSectionIndex, int iElementSectionIndex, int iElementItemIndex){
		FuncCheckItem oRemoveFuncCheckItem = null;
		
		String sKey = iColumnIndex + "_" + iSectionIndex;
		if(m_oColumnElements.containsKey(sKey) == false)
			return null;
		
		FrameColumnElement oFrameColumnElement = m_oColumnElements.get(sKey);
		oRemoveFuncCheckItem = oFrameColumnElement.removeItem(iElementSectionIndex, iElementItemIndex);
		
		updateScreen(iSectionIndex);
		
		return oRemoveFuncCheckItem;
	}
	
	public void removeAllItemFromElement(int iColumnIndex, int iSectionIndex){
		String sKey = iColumnIndex + "_" + iSectionIndex;
		if(m_oColumnElements.containsKey(sKey) == false)
			return;
		
		FrameColumnElement oFrameColumnElement = m_oColumnElements.get(sKey);
		oFrameColumnElement.removeAllItem();
		
		updateScreen(iSectionIndex);
	}
	
	public void updateScreen(int iSectionIndex){
		int iMaxHeight = 0;
		for(int i=0; i<m_iColumnCount; i++){
			String sKey = i + "_" + iSectionIndex;
			if(m_oColumnElements.containsKey(sKey)){
				FrameColumnElement oFrameColumnElement = m_oColumnElements.get(sKey);
				if(oFrameColumnElement.getLinesHeight() > iMaxHeight)
					iMaxHeight = oFrameColumnElement.getLinesHeight();
			}
		}
		if(iMaxHeight < 50){
			iMaxHeight = 50;
		}
		
		if(m_oSectionFrames.containsKey(iSectionIndex)){
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "sectionDesc");
			
			VirtualUIFrame oFrame = m_oSectionFrames.get(iSectionIndex);
			oFrame.setHeight(iMaxHeight + oLabel.getHeight());
		}
		
		for(int i=0; i<m_iColumnCount; i++){
			String sKey = i + "_" + iSectionIndex;
			if(m_oColumnElements.containsKey(sKey)){
				FrameColumnElement oFrameColumnElement = m_oColumnElements.get(sKey);
				oFrameColumnElement.updateScreenByParent(iMaxHeight);
			}
		}
	}
	
	public TreeMap<String, Integer> getSelectedLine(){
		TreeMap<String, Integer> oSelectedLine = new TreeMap<String, Integer>(Collections.reverseOrder());
		
		for(Entry<String, FrameColumnElement> entry: m_oColumnElements.entrySet()){
			TreeMap<String, Integer> oItemList = entry.getValue().getSelectedLine();
			for(Entry<String, Integer> entry2: oItemList.entrySet()){
				oSelectedLine.put(entry2.getKey(), 0);
			}
		}
		
		return oSelectedLine;
	}
	
	public void deSelectAllLine(){
		for(Entry<String, FrameColumnElement> entry: m_oColumnElements.entrySet()){
			FrameColumnElement oFrameColumnElement = entry.getValue();
			oFrameColumnElement.deSelectAllLine();
		}
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		//click event of Seat number label
		for(Entry<Integer, VirtualUILabel> entry:m_oLabelColumnHeader.entrySet()){
			VirtualUILabel oLabel = entry.getValue();
			if (oLabel.getId() == iChildId) {
				for (FrameColumnViewListener listener : listeners) {
					// Raise the event to parent
					listener.frameColumnViewListener_clickColumnHeader(entry.getKey());
				}
				bMatchChild = true;
				break;
			}
		}
		
		//click event of setting button 
		for (Entry<Integer, VirtualUIImage> entry : m_oFrameColumnHeaderImage.entrySet()) {
			VirtualUIImage oImage = entry.getValue();
			if (oImage.getId() == iChildId) {
				for (FrameColumnViewListener listener : listeners) {
					// Raise the event to parent
					listener.frameColumnViewListener_clickColumnHeader(entry.getKey());
				}
				bMatchChild = true;
				break;
			}
		}
		return bMatchChild;
	}

	@Override
	public void frameColumnElement_clickPlaceButton(int iTargetColumnIndex, int iTargetSectionIndex) {
		for (FrameColumnViewListener listener : listeners) {
			// Raise the event to parent
			listener.frameColumnViewListener_clickPlaceButton(iTargetColumnIndex, iTargetSectionIndex);
		}
	}
	
	@Override
	public boolean frameColumnElement_isMinimumChargeItem(FuncCheckItem oFuncCheckItem) {
		boolean bIsMinimumChargeItem = false;
		for (FrameColumnViewListener listener : listeners) {
			// Raise the event to parent
			if(listener.frameColumnViewListener_isMinimumChargeItem(oFuncCheckItem))
				bIsMinimumChargeItem = true;
			break;
		}
		
		return bIsMinimumChargeItem;
	}
}
