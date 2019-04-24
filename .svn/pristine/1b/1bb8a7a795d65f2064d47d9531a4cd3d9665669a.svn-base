package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIHorizontalList;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

import app.AppGlobal;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIButton;

interface FrameCommonPageContainerListener {
	void frameCommonPageContainer_changeFrame();
	boolean frameCommonPageContainer_swipe(boolean bLeft, String sNote, int iChildId);
	void frameCommonPageContainer_updateFrame(int iOrigIndex, int iIndex, String sFrameName);
	void frameCommonPageContainer_CloseImageClicked();
	void frameCommonPageContainer_ClickButtonIndex(int iIndex);
}

public class FrameCommonPageContainer extends VirtualUIFrame implements FrameTitleHeaderListener {

	private TemplateBuilder m_oTemplateBuilder;

	private VirtualUIFrame m_oFrame;
	private VirtualUIFrame m_oFrameLeftSwipe;
	private VirtualUIFrame m_oFrameRightSwipe;
	private VirtualUIFrame m_oFrameUpperTabSeaparator;
	private VirtualUIFrame m_oFrameTabSeparator;
	private VirtualUIImage m_oButtonLeftSwipe;
	private VirtualUIImage m_oButtonRightSwipe;
	private VirtualUIHorizontalList m_oHorizontalList;
	private VirtualUIHorizontalList m_oHorizontalChildList;

	private class TagButton<T> {
		// m_oFrameTakeOwnership.addRecord(m_iTotalStation++,iStationId,sStationName,iCount);
		private int tagIndex;
		private String[] tagName = new String[5];
		private T frame;

		TagButton(String[] sTitle, int iIndex, T oframe) {
			tagName = sTitle;
			tagIndex = iIndex;
			frame = oframe;
		}
	}

	private List<TagButton> m_oTagButtonList;

	private int m_iContainerWidth;
	private int m_iContainerHeight;
	private int m_iTagButtonWidth;
	private int m_iTagButtonHeight;
	private int m_iMaxTagButton;
	private int m_iTotalTagButton;
	private int m_iSelectedTagButton;
	private int m_iStartIndex;
	private int m_iMaxGapMargin;
	private int m_iGapMargin;

	private String m_sFontColorSelected;
	private String m_sFontColorUnselected;
	private String m_sBgColorSelected;
	private String m_sBgColorUnselected;
	private int	m_iTextSizeSelected;
	private int m_iTextSizeUnselected;
	private boolean m_bShowBgImage;
	private boolean m_bShowUpperline;
	private boolean m_bShowUnderline;
	private String m_oListOptionTextAlign;
	private FrameTitleHeader m_oFrameTitleHeader;
	private HashMap<Integer, Integer> m_oButtonIndexLookup;
	private ArrayList<String> m_oOptionList;
	
	public static int MAXIMUM_OPTION_COUNT = 36;
	public static int MAXIMUM_OPTION_COUNT_IN_ONE_TAG = 36;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameCommonPageContainerListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameCommonPageContainerListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameCommonPageContainerListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	FrameCommonPageContainer() {
		// init();
	}

	public void init(int iContainerWidth, int iContainerHeight, int iTagWidth, int iTagHeight, int iMaxTag,
			String sFontColorSelected, String sFontColorUnselected, String sBgColorSelected, String sBgColorUnselected,
			int iMaxMargin, boolean bShowUpperline, boolean bShowUnderline) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameCommonPageContainerListener>();
		m_oTagButtonList = new ArrayList<TagButton>();

		m_iContainerWidth = iContainerWidth;
		m_iContainerHeight = iContainerHeight;
		m_iTagButtonWidth = iTagWidth;
		m_iTagButtonHeight = iTagHeight;
		m_iMaxTagButton = iMaxTag;
		m_iTotalTagButton = 0;
		m_iSelectedTagButton = 0;
		m_iStartIndex = 0;
		m_iMaxGapMargin = iMaxMargin;

		m_sFontColorSelected = sFontColorSelected;
		m_sFontColorUnselected = sFontColorUnselected;
		m_sBgColorSelected = sBgColorSelected;
		m_sBgColorUnselected = sBgColorUnselected;

		m_bShowUpperline = bShowUpperline;
		m_bShowUnderline = bShowUnderline;
		
		m_iTextSizeSelected = 0;
		m_iTextSizeUnselected = 0;
		
		m_bShowBgImage = false;
		m_oListOptionTextAlign = "";
		
		m_oButtonIndexLookup = new HashMap<Integer, Integer>();
		
		m_oOptionList = new ArrayList<String>();
		
		// Load child elements from template
		m_oTemplateBuilder.loadTemplate("fraCommonPageContainer.xml");

		VirtualUILabel oFrame = new VirtualUILabel();

		m_oTemplateBuilder.buildLabel(oFrame, "lblTab");
		if (m_iTagButtonWidth == 0)
			m_iTagButtonWidth = oFrame.getWidth();

		if (m_iTagButtonHeight == 0)
			m_iTagButtonHeight = oFrame.getHeight();

		m_oTemplateBuilder.buildLabel(oFrame, "selectedTab");
		if (m_sBgColorSelected == null || m_sBgColorSelected.isEmpty())
			m_sBgColorSelected = oFrame.getBackgroundColor();

		if (m_sFontColorSelected == null || m_sFontColorSelected.isEmpty())
			m_sFontColorSelected = oFrame.getForegroundColor();
		
		m_oTemplateBuilder.buildLabel(oFrame, "unselectedTab");
		if (m_sBgColorUnselected == null || m_sBgColorUnselected.isEmpty())
			m_sBgColorUnselected = oFrame.getBackgroundColor();

		if (m_sFontColorUnselected == null || m_sFontColorUnselected.isEmpty())
			m_sFontColorUnselected = oFrame.getForegroundColor();

		if (m_iContainerWidth - 96 < m_iTagButtonWidth * m_iMaxTagButton)
			m_iMaxTagButton = (m_iContainerWidth - 96) / m_iTagButtonWidth;

		this.setWidth(iContainerWidth);
		this.setHeight(iContainerHeight);

		m_oFrameLeftSwipe = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftSwipe, "fraLeftSwipe");
		m_oFrameLeftSwipe.allowClick(true);
		m_oFrameLeftSwipe.setWidth(24);
		m_oFrameLeftSwipe.setHeight(m_iTagButtonHeight);
		this.attachChild(m_oFrameLeftSwipe);

		// Left Swipe Button
		m_oButtonLeftSwipe = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oButtonLeftSwipe, "imgLeftSwipe");
		m_oButtonLeftSwipe.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
		m_oButtonLeftSwipe.allowClick(true);
		m_oButtonLeftSwipe.setTop((m_iTagButtonHeight - 24) / 2);
		m_oFrameLeftSwipe.attachChild(m_oButtonLeftSwipe);

		// Right Swipe
		m_oFrameRightSwipe = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameRightSwipe, "fraRightSwipe");
		m_oFrameRightSwipe.allowClick(true);
		m_oFrameRightSwipe.setWidth(24);
		m_oFrameRightSwipe.setHeight(m_iTagButtonHeight);
		m_oFrameRightSwipe.setLeft(m_iContainerWidth - 24);
		this.attachChild(m_oFrameRightSwipe);

		// Right Swipe Button
		m_oButtonRightSwipe = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oButtonRightSwipe, "imgRightSwipe");
		m_oButtonRightSwipe.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
		m_oButtonRightSwipe.allowClick(true);
		m_oButtonRightSwipe.setTop((m_iTagButtonHeight - 24) / 2);
		m_oFrameRightSwipe.attachChild(m_oButtonRightSwipe);

		// Horizontal line
		m_oHorizontalList = new VirtualUIHorizontalList();
		m_oTemplateBuilder.buildHorizontalList(m_oHorizontalList, "horListTab");
		m_oHorizontalList.setWidth(m_iContainerWidth - 96);
		m_oHorizontalList.setHeight(m_iTagButtonHeight);
		m_oHorizontalList.setLeft(48);
		this.attachChild(m_oHorizontalList);

		m_oHorizontalChildList = new VirtualUIHorizontalList();
		m_oTemplateBuilder.buildHorizontalList(m_oHorizontalChildList, "horListChildTab");
		m_oHorizontalChildList.setWidth(m_oHorizontalList.getWidth());
		m_oHorizontalChildList.setHeight(m_oHorizontalList.getHeight());
		m_oHorizontalChildList.setLeft(0);
		m_oHorizontalChildList.allowSwipeLeft(true);
		m_oHorizontalChildList.allowSwipeRight(true);
		m_oHorizontalList.attachChild(m_oHorizontalChildList);

		m_oFrameUpperTabSeaparator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameUpperTabSeaparator, "fraUpperTabSeparator");
		m_oFrameUpperTabSeaparator.setVisible(m_bShowUpperline);
		m_oFrameUpperTabSeaparator.setWidth(m_iContainerWidth);
		this.attachChild(m_oFrameUpperTabSeaparator);
		
		m_oFrameTabSeparator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTabSeparator, "fraTabSeparator");
		m_oFrameTabSeparator.setVisible(m_bShowUnderline);
		m_oFrameTabSeparator.setTop(m_iTagButtonHeight);
		m_oFrameTabSeparator.setWidth(m_iContainerWidth);
		this.attachChild(m_oFrameTabSeparator);
		
		m_oFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrame, "fraCommonPageFrame");
		m_oFrame.setWidth(m_iContainerWidth);
		m_oFrame.setHeight(m_iContainerHeight - m_iTagButtonHeight);
		m_oFrame.setTop(m_iTagButtonHeight + 1);
		this.attachChild(m_oFrame);
	}

	public <T> void addButton(String sButtonValue, T oFrame) {
		String[] sName = new String[1];
		sName[0] = sButtonValue;
		addButton(sName, oFrame);
	}

	// Add a button tag
	public <T> void addButton(String[] sButtonValue, T oFrame) {

		if (oFrame == null) {
			m_oFrame = (VirtualUIFrame) m_oTagButtonList.get(m_oTagButtonList.size() - 1).frame;
		} else {
			m_oFrame = (VirtualUIFrame) oFrame;
			m_oTemplateBuilder.buildFrame(m_oFrame, "fraCommonPageFrame");
			m_oFrame.setVisible(false);
			m_oFrame.setWidth(m_iContainerWidth);
			m_oFrame.setHeight(m_iContainerHeight - m_iTagButtonHeight - 2);
			if(m_oOptionList.size() > MAXIMUM_OPTION_COUNT)
				m_oFrame.setTop(m_iTagButtonHeight + m_oFrameTitleHeader.getHeight() + 1);
			else
				m_oFrame.setTop(m_iTagButtonHeight + 1);
			this.attachChild(m_oFrame);
		}

		TagButton<T> oNewTag = new TagButton(sButtonValue, m_iTotalTagButton, m_oFrame);
		m_oTagButtonList.add(oNewTag);
		m_iTotalTagButton++;

		int iTagCount = 0;
		if (m_iTotalTagButton >= m_iMaxTagButton)
			iTagCount = m_iMaxTagButton;
		else
			iTagCount = m_iTotalTagButton;

		if (iTagCount > 1) {
			int iTempWidth = m_iContainerWidth - 96;
			if (m_iMaxGapMargin == 0)
				m_iGapMargin = (iTempWidth - iTagCount * m_iTagButtonWidth) / (iTagCount - 1);
			else if ((iTempWidth - iTagCount * m_iTagButtonWidth) / (iTagCount - 1) >= m_iMaxGapMargin)
				m_iGapMargin = m_iMaxGapMargin;
			else
				m_iGapMargin = (iTempWidth - iTagCount * m_iTagButtonWidth) / (iTagCount - 1);

			if (m_iMaxGapMargin != 0) {
				m_oHorizontalList.setWidth(iTagCount * m_iTagButtonWidth + (iTagCount - 1) * m_iGapMargin);
				m_oHorizontalChildList.setWidth(m_oHorizontalList.getWidth());
				m_oHorizontalList.setLeft(
						(this.getWidth() - iTagCount * m_iTagButtonWidth - (iTagCount - 1) * m_iGapMargin) / 2);
			}
		}

		constructTagList();

		if(m_iTotalTagButton > 0)
			showFirstTag();
	}

	public void hideHorizontalChild(String sName){
		HashMap<Integer, VirtualUIBasicElement> oHorizontalChildList = new HashMap<Integer, VirtualUIBasicElement>();
		for(VirtualUIBasicElement oObject : m_oHorizontalChildList.getChilds())
			oHorizontalChildList.put(oHorizontalChildList.size() + 1, oObject);
		
		for (Entry<Integer, VirtualUIBasicElement> oHorizontalChild : oHorizontalChildList.entrySet()){
			if (oHorizontalChild.getValue().getValueByIndex(0).equals(sName)) {
				m_oHorizontalChildList.removeChild(oHorizontalChild.getKey() - 1);
				if(oHorizontalChildList.size() != 0)
					m_oHorizontalChildList.removeChild(oHorizontalChild.getKey() - 2);
				m_oTagButtonList.remove((oHorizontalChild.getKey() - 1) / 2);
				m_iTotalTagButton--;
				break;
			}
		}
		constructTagList();
	}

	public void removeAllTag(){
		if(m_oTagButtonList != null){
			for(int i = m_oTagButtonList.size()-1 ; i >= 0; i--){
				VirtualUIFrame oFrame = (VirtualUIFrame) m_oTagButtonList.get(i).frame;
				oFrame.removeAllChildren();
				oFrame.removeMyself();
			}
		} else
			// No init
			return;
		
		m_oFrame.removeAllChildren();
		m_oFrame.removeMyself();
		m_oTagButtonList.clear();
		m_iTotalTagButton = 0;
		
		if(m_oHorizontalChildList != null)
			m_oHorizontalChildList.removeAllChildren();
		
		constructTagList();
	}

	public void setTitle(String sTitle, boolean bHiddenCloseButton){
		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(!bHiddenCloseButton);
		m_oFrameTitleHeader.addListener(this);
		m_oFrameTitleHeader.setTitle(sTitle);
		this.attachChild(m_oFrameTitleHeader);
		
		//Resize other Object Top
		m_oFrameLeftSwipe.setTop(m_oFrameLeftSwipe.getTop() + m_oFrameTitleHeader.getHeight());
		m_oFrameRightSwipe.setTop(m_oFrameRightSwipe.getTop() + m_oFrameTitleHeader.getHeight());
		
		m_oFrameUpperTabSeaparator.setTop(m_oFrameUpperTabSeaparator.getTop() + m_oFrameTitleHeader.getHeight());
		m_oFrameTabSeparator.setTop(m_oFrameTabSeparator.getTop() + m_oFrameTitleHeader.getHeight());
		m_oHorizontalList.setTop(m_oHorizontalList.getTop() + m_oFrameTitleHeader.getHeight());
		
		m_oFrame.setTop(m_oFrame.getTop() + m_oFrameTitleHeader.getHeight());
		m_oFrame.setHeight(m_oFrame.getHeight() - m_oFrameTitleHeader.getHeight());
	}
	
	public void setTagTextSize(int iSelectedTagFontSize, int iUnselectedTagFontSize){
		m_iTextSizeSelected = iSelectedTagFontSize;
		m_iTextSizeUnselected = iUnselectedTagFontSize;
	}
	
	public void setButtonTextAlign(String sTextAlign){
		m_oListOptionTextAlign = sTextAlign;
	}
	
	public void addButtonInSingleTag(ArrayList<String> oOptionList){
		if(m_oTagButtonList != null){
			int iTagOptionCount = MAXIMUM_OPTION_COUNT_IN_ONE_TAG;
			if(oOptionList.size() > MAXIMUM_OPTION_COUNT_IN_ONE_TAG){
				this.setTagbuttonVisible(true);
				iTagOptionCount = MAXIMUM_OPTION_COUNT;
			} else
				this.setTagbuttonVisible(false);
			
			int iLeftPadding = 16;
			int iTopPadding = 16;
			int iRowItemCount = 0;
			int iColumnItemCount = 0;
			
			int iButtonIndex = 0;
			
			m_oOptionList = oOptionList;
			
			for(int iPage = 0 ; iPage <= (m_oOptionList.size() / (iTagOptionCount + 1)) ; iPage++){
				VirtualUIFrame oTmpFrame = new VirtualUIFrame();
				
				iColumnItemCount = 0;
				
				this.addButton(AppGlobal.g_oLang.get()._("page") + " " + (iPage + 1), oTmpFrame);
				
				for(int iOptionIndex = iPage * iTagOptionCount ; iOptionIndex < (iPage + 1) * iTagOptionCount && iOptionIndex < oOptionList.size(); iOptionIndex++){
					VirtualUIButton m_oButton = new VirtualUIButton();
					m_oTemplateBuilder.buildButton(m_oButton, "btnButton");
					
					m_oButton.setValue(m_oOptionList.get(iOptionIndex));
					m_oButton.setLeft(m_oButton.getLeft() + iRowItemCount * (m_oButton.getWidth() + iLeftPadding));
					m_oButton.setTop(m_oButton.getTop() + iColumnItemCount * (m_oButton.getHeight() + iTopPadding));
					oTmpFrame.attachChild(m_oButton);
					m_oButtonIndexLookup.put(m_oButton.getId(), iButtonIndex);
					iRowItemCount ++;
					iButtonIndex ++;
					if(iRowItemCount == 6){
						iRowItemCount = 0;
						iColumnItemCount ++;
					}
				}
			}
		}
	}
	
	// construct a tag list
	private void constructTagList() {
		m_oHorizontalChildList.removeAllChildren();

		if (m_oTagButtonList.size() == 1) {
			VirtualUILabel oLblPanelLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLblPanelLabel, "lblTab");
			oLblPanelLabel.setValue(m_oTagButtonList.get(0).tagName);
			oLblPanelLabel.setEnabled(true);
			oLblPanelLabel.setWidth(m_iTagButtonWidth);
			oLblPanelLabel.setHeight(m_iTagButtonHeight);
			oLblPanelLabel.setLeft((m_oHorizontalChildList.getWidth() - m_iTagButtonWidth) / 2);
			oLblPanelLabel.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
			oLblPanelLabel.allowClick(true);
			
			oLblPanelLabel.setCornerRadius("5");

			m_oHorizontalChildList.attachChild(oLblPanelLabel);
		} else {
			for (int i = 0; i < m_oTagButtonList.size(); i++) {
				if (i != 0) {
					VirtualUILabel oLblBlankLabel = new VirtualUILabel();
					m_oTemplateBuilder.buildLabel(oLblBlankLabel, "lblTab");
					oLblBlankLabel.setWidth(m_iGapMargin);
					oLblBlankLabel.setHeight(m_iTagButtonHeight);
					oLblBlankLabel.allowClick(false);
					oLblBlankLabel.setEnabled(false);
					
					m_oHorizontalChildList.attachChild(oLblBlankLabel);
				}

				VirtualUILabel oLblPanelLabel = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(oLblPanelLabel, "lblTab");
				oLblPanelLabel.setValue(m_oTagButtonList.get(m_iStartIndex + i).tagName);
				oLblPanelLabel.setEnabled(true);

				oLblPanelLabel.setWidth(m_iTagButtonWidth);
				oLblPanelLabel.setHeight(m_iTagButtonHeight);
				oLblPanelLabel.setLeft(0);
				oLblPanelLabel.setForegroundColor(m_sFontColorUnselected);
				if (m_iTextSizeUnselected > 0)
					oLblPanelLabel.setTextSize(m_iTextSizeUnselected);
				oLblPanelLabel.setBackgroundColor(m_sBgColorUnselected);
				oLblPanelLabel.setCornerRadius("5");
				oLblPanelLabel.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				oLblPanelLabel.allowClick(true);
				
				if(m_bShowBgImage){
					//oLblPanelLabel.setTextSize(16);
					
					//Background Image
					VirtualUIImage oBackGroundImage = new VirtualUIImage();
					m_oTemplateBuilder.buildImage(oBackGroundImage, "imgBackground");
					oBackGroundImage.setTop(oLblPanelLabel.getTop());
					oBackGroundImage.setWidth(oLblPanelLabel.getWidth());
					oBackGroundImage.setHeight(oLblPanelLabel.getHeight());
					oBackGroundImage.setLeft(oLblPanelLabel.getLeft());
					oBackGroundImage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "icons/icon_seat_indicator_blue.png");
					oBackGroundImage.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_ASPECT_FIT_CENTER);
					oBackGroundImage.setVisible(false);
					oBackGroundImage.allowClick(true);
					oBackGroundImage.setExist(true);
					
					//Background Frame
					VirtualUIFrame oBackgroundFrame = new VirtualUIFrame();
					m_oTemplateBuilder.buildFrame(oBackgroundFrame, "fraBackground");
					oBackgroundFrame.setWidth(m_iTagButtonWidth);
					oBackgroundFrame.allowClick(true);
					
					//Attach background image + label to frame
					oBackgroundFrame.attachChild(oBackGroundImage);
					oBackgroundFrame.attachChild(oLblPanelLabel);
					
					//Attach background frame to horizontal list
					m_oHorizontalChildList.attachChild(oBackgroundFrame);
				}else
					m_oHorizontalChildList.attachChild(oLblPanelLabel);
			}
		}

		checkSwipeButton();
	}
	
	public void setTagbuttonVisible(boolean bSet){
		if(m_oHorizontalList.getVisible() != bSet){
			if(!bSet) {
				for(int i = 0 ; i < m_oTagButtonList.size() ; i++) {
					VirtualUIFrame oFrame = (VirtualUIFrame) m_oTagButtonList.get(i).frame;
					oFrame.setHeight(m_iContainerHeight + m_iTagButtonHeight);
				}
			}
		}
		m_oButtonLeftSwipe.setVisible(bSet);
		m_oButtonRightSwipe.setVisible(bSet);
		m_oHorizontalList.setVisible(bSet);

		if(m_bShowUpperline && bSet)
			m_oFrameUpperTabSeaparator.setVisible(true);
		else
			m_oFrameUpperTabSeaparator.setVisible(false);
		
		if(m_bShowUnderline && bSet)
			m_oFrameTabSeparator.setVisible(true);
		else
			m_oFrameTabSeparator.setVisible(false);
		
		
		m_oHorizontalChildList.setVisible(bSet);
		for(VirtualUIBasicElement oElement : m_oHorizontalChildList.getChilds())
			oElement.setVisible(bSet);
	}

	public void showBackGroundImage(boolean iVisible){
		m_bShowBgImage = iVisible;
	}

	// Set the swipe button to visible or not
	private void checkSwipeButton() {
		// Set the left button
		if (m_iStartIndex == 0) {
			m_oFrameLeftSwipe.setEnabled(false);
			m_oButtonLeftSwipe.setEnabled(false);
			m_oButtonLeftSwipe.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
		} else {
			m_oFrameLeftSwipe.setEnabled(true);
			m_oButtonLeftSwipe.setEnabled(true);
			m_oButtonLeftSwipe.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
		}
		
		// Set the right button
		if (m_iStartIndex >= m_iTotalTagButton - m_iMaxTagButton) {
			m_oFrameRightSwipe.setEnabled(false);
			m_oButtonRightSwipe.setEnabled(false);
			m_oButtonRightSwipe.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
		} else {
			m_oFrameRightSwipe.setEnabled(true);
			m_oButtonRightSwipe.setEnabled(true);
			m_oButtonRightSwipe.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
		}

		// Set the left
		if(m_oFrameLeftSwipe.getWidth() + m_iGapMargin < m_oHorizontalList.getLeft()) {
			m_oFrameLeftSwipe.setLeft(m_oHorizontalList.getLeft() - m_iGapMargin - m_oFrameLeftSwipe.getWidth());
			m_oFrameRightSwipe.setLeft(m_oHorizontalList.getLeft() + m_oHorizontalList.getWidth() + m_iGapMargin);
		}
		
		if(m_iTotalTagButton <= m_iMaxTagButton){
			m_oFrameLeftSwipe.setVisible(false);
			m_oFrameRightSwipe.setVisible(false);
		}else{
			m_oFrameLeftSwipe.setVisible(true);
			m_oFrameRightSwipe.setVisible(true);
		}
	}

	public <T> T getCurrentFrame() {
		return (T) m_oTagButtonList.get((int)(m_iSelectedTagButton / 2)).frame;
	}

	public int getTotalIndex() {
		return  m_oTagButtonList.size();
	}

	public int getCurrentIndex() {
		return  m_iSelectedTagButton;
	}

	public int getCurrentButtonHeight() {
		return  m_iTagButtonHeight;
	}
	
	public void setUnderlineColor(String sColor){
		m_oFrameTabSeparator.setBackgroundColor(sColor);
	}
	
	public void setUpperlineColor(String sColor){
		m_oFrameUpperTabSeaparator.setBackgroundColor(sColor);
	}
	
	@Override
	public boolean swipeLeft(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		for (FrameCommonPageContainerListener listener : listeners) {
			bMatchChild = listener.frameCommonPageContainer_swipe(true, sNote, iChildId);
		}
		
		return bMatchChild;
	}
	
	@Override
	public boolean swipeRight(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		for (FrameCommonPageContainerListener listener : listeners) {
			bMatchChild = listener.frameCommonPageContainer_swipe(false, sNote, iChildId);
		}
		
		return bMatchChild;
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if ((iChildId == m_oFrameLeftSwipe.getId() && m_oFrameLeftSwipe.isAllowClick()) || (iChildId == m_oButtonLeftSwipe.getId() && m_oButtonLeftSwipe.isAllowClick())) {
			if (m_iStartIndex - 1 == 0) {
				m_oFrameLeftSwipe.setEnabled(false);
				m_oButtonLeftSwipe.setEnabled(false);
				m_oButtonLeftSwipe.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
			} else {
				m_oFrameLeftSwipe.setEnabled(true);
				m_oButtonLeftSwipe.setEnabled(true);
				m_oButtonLeftSwipe.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
			}
			
			if (m_iStartIndex - 1 == m_iTotalTagButton - m_iMaxTagButton) {
				m_oFrameRightSwipe.setEnabled(false);
				m_oButtonRightSwipe.setEnabled(false);
				m_oButtonRightSwipe.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
			} else {
				m_oFrameRightSwipe.setEnabled(true);
				m_oButtonRightSwipe.setEnabled(true);
				m_oButtonRightSwipe.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
			}
			
			m_iStartIndex--;
			
			m_oHorizontalChildList.scrollToIndex(m_iStartIndex * 2);
			bMatchChild = true;
		} else if ((iChildId == m_oFrameRightSwipe.getId() &&  m_oFrameRightSwipe.isAllowClick()) || (iChildId == m_oButtonRightSwipe.getId() && m_oButtonRightSwipe.isAllowClick())) {
			if (m_iStartIndex + 1 == 0) {
				m_oFrameLeftSwipe.setEnabled(false);
				m_oButtonLeftSwipe.setEnabled(false);
				m_oButtonLeftSwipe.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
			} else {
				m_oFrameLeftSwipe.setEnabled(true);
				m_oButtonLeftSwipe.setEnabled(true);
				m_oButtonLeftSwipe.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
			}
			
			if (m_iStartIndex + 1 == m_iTotalTagButton - m_iMaxTagButton) {
				m_oFrameRightSwipe.setEnabled(false);
				m_oButtonRightSwipe.setEnabled(false);
				m_oButtonRightSwipe.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
			} else {
				m_oFrameRightSwipe.setEnabled(true);
				m_oButtonRightSwipe.setEnabled(true);
				m_oButtonRightSwipe.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
			}
			
			m_iStartIndex++;
			m_oHorizontalChildList.scrollToIndex(m_iStartIndex * 2);
			bMatchChild = true;
		}
		
		// Check does the tag is clicked
		for (int i = 0; i < m_oHorizontalChildList.getChildCount(); i++) {
			if (m_oHorizontalChildList.getChilds().get(i).getUIType().equals(HeroActionProtocol.View.Type.FRAME)) {
				for(int j = 0; j < m_oHorizontalChildList.getChilds().get(i).getChildCount(); j++){
					if(iChildId == m_oHorizontalChildList.getChilds().get(i).getChilds().get(j).getId()){
						clickTag(i / 2);
						bMatchChild = true;
						break;
					}
				}
			}else if (iChildId == m_oHorizontalChildList.getChilds().get(i).getId()) {
				clickTag(i / 2);
				bMatchChild = true;
				break;
			}
		}

		for(Entry<Integer, Integer> entry : m_oButtonIndexLookup.entrySet()){
			if(entry.getKey() == iChildId){
				for (FrameCommonPageContainerListener listener : listeners) {
					listener.frameCommonPageContainer_ClickButtonIndex(entry.getValue());
				}
				bMatchChild = true;
				break;
			}
		}

		return bMatchChild;
	}
	
	public void setTagButton(int iIndex) {
		if(m_bShowBgImage){
			// Reset the previous selected tag button's image
			for(int j = 0; j<m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton*2).getChildCount(); j++){
				//set background image visible
				if(m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton*2).getChilds().get(j).getUIType().equals(HeroActionProtocol.View.Type.IMAGE))
					m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton*2).getChilds().get(j).setVisible(false);
				//set text foreground color
				else{
					m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton*2).getChilds().get(j).setForegroundColor(m_sFontColorUnselected);
					if(m_iTextSizeUnselected > 0)
						m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton*2).getChilds().get(j).setTextSize(m_iTextSizeUnselected);
				}
			}
			
			// Set the selected button's image
			for(int j=0; j<m_oHorizontalChildList.getChilds().get(iIndex*2).getChildCount(); j++){
				//set background image visible
				if(m_oHorizontalChildList.getChilds().get(iIndex*2).getChilds().get(j).getUIType().equals(HeroActionProtocol.View.Type.IMAGE))
					m_oHorizontalChildList.getChilds().get(iIndex*2).getChilds().get(j).setVisible(true);
				//set text foreground color
				else{
					m_oHorizontalChildList.getChilds().get(iIndex*2).getChilds().get(j).setForegroundColor(m_sFontColorSelected);
					if(m_iTextSizeSelected > 0)
						m_oHorizontalChildList.getChilds().get(iIndex*2).getChilds().get(j).setTextSize(m_iTextSizeSelected);
				}
			}
		}else{
			m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton * 2).setBackgroundColor(m_sBgColorUnselected);
			m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton * 2).setForegroundColor(m_sFontColorUnselected);
			
			m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton * 2).setTextStyle("");
			m_oHorizontalChildList.getChilds().get(iIndex * 2).setTextStyle(HeroActionProtocol.View.Attribute.TextStyle.BOLD);
			
			if(m_iTextSizeUnselected > 0)
				m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton * 2).setTextSize(m_iTextSizeUnselected);
			if(m_iTextSizeSelected > 0)
				m_oHorizontalChildList.getChilds().get(iIndex * 2).setTextSize(m_iTextSizeSelected);

			m_oHorizontalChildList.getChilds().get(iIndex * 2).setBackgroundColor(m_sBgColorSelected);
			m_oHorizontalChildList.getChilds().get(iIndex * 2).setForegroundColor(m_sFontColorSelected);
		}

		m_iSelectedTagButton = iIndex;

	}
	
	public void clickTag(int iIndex) {
		int iOrigTag = m_iSelectedTagButton;
		setTagButton(iIndex);
		addFrame(iOrigTag , iIndex);

		for (FrameCommonPageContainerListener listener : listeners)
			listener.frameCommonPageContainer_changeFrame();
	}
	
	// change to another tag's frame
	private <T> void addFrame(int iOrigIndex, int iIndex) {

		VirtualUIFrame oOrigFrame = (VirtualUIFrame) m_oTagButtonList.get(iOrigIndex).frame;
		VirtualUIFrame oNewFrame = (VirtualUIFrame) m_oTagButtonList.get(iIndex).frame;

		if (oOrigFrame.getClass().getName().equals(oNewFrame.getClass().getName())) {
			for (FrameCommonPageContainerListener listener : listeners) {
				listener.frameCommonPageContainer_updateFrame(iOrigIndex, iIndex, oNewFrame.getClass().getName());
			}
		}

		oOrigFrame.setVisible(false);
		oNewFrame.setVisible(true);

	}
	
	// show the first tag content
	public void showFirstTag() {

		for(int i = 0 ; i < m_oTagButtonList.size() ; i++) {
			VirtualUIFrame oFrame = (VirtualUIFrame) m_oTagButtonList.get(i).frame;
			if(oFrame.getParentTerm() != null)
				oFrame.setVisible(false);
		}

		VirtualUIFrame oNewFrame = (VirtualUIFrame) m_oTagButtonList.get(0).frame;
		oNewFrame.setVisible(true);

		if(m_bShowBgImage){
			// Reset the current page to unselected status
			if(m_oHorizontalChildList.getChildCount() > m_iSelectedTagButton * 2){
				for(int j = 0; j < m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton * 2).getChildCount(); j++){
					//set background image visible
					if(m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton * 2).getChilds().get(j).getUIType().equals(HeroActionProtocol.View.Type.IMAGE))
						m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton * 2).getChilds().get(j).setVisible(false);
					//set text foreground color
					else{
						m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton * 2).getChilds().get(j).setForegroundColor(m_sFontColorUnselected);
						if(m_iTextSizeUnselected > 0)
							m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton * 2).getChilds().get(j).setTextSize(m_iTextSizeUnselected);
					}
				}
			}
			if(m_oHorizontalChildList.getChildCount() > 0){
				m_oHorizontalChildList.scrollToIndex(0);
				for(int i = 0; i < m_oHorizontalChildList.getChilds().get(0).getChildCount(); i++){
					//set background image visible
					if(m_oHorizontalChildList.getChilds().get(0).getChilds().get(i).getUIType().equals(HeroActionProtocol.View.Type.IMAGE))
						m_oHorizontalChildList.getChilds().get(0).getChilds().get(i).setVisible(true);
		 
					//set text foreground color
					else{
						
						if(m_iTextSizeSelected > 0)
							m_oHorizontalChildList.getChilds().get(0).getChilds().get(i).setTextSize(m_iTextSizeSelected);
						m_oHorizontalChildList.getChilds().get(0).getChilds().get(i).setForegroundColor(m_sFontColorSelected);
						m_oHorizontalChildList.getChilds().get(0).getChilds().get(i).bringToTop();
					}
				}
			}
		}else{
			if(m_oHorizontalChildList.getChildCount() > m_iSelectedTagButton * 2 && m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton) != null){
				m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton * 2).setForegroundColor(m_sFontColorUnselected);
				m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton * 2).setBackgroundColor(m_sBgColorUnselected);
				if(m_iTextSizeUnselected > 0)
					m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton * 2).setTextSize(m_iTextSizeUnselected);

				m_oHorizontalChildList.getChilds().get(m_iSelectedTagButton * 2).setTextStyle(HeroActionProtocol.View.Attribute.TextStyle.BOLD);
			}
			if(m_oHorizontalChildList.getChildCount() > 0){
				m_oHorizontalChildList.scrollToIndex(0);
				m_oHorizontalChildList.getChilds().get(0).setBackgroundColor(m_sBgColorSelected);
				m_oHorizontalChildList.getChilds().get(0).setForegroundColor(m_sFontColorSelected);
				if(m_iTextSizeSelected > 0) {
					m_oHorizontalChildList.getChilds().get(0).setTextSize(m_iTextSizeSelected);
					m_oHorizontalChildList.getChilds().get(0).setTextStyle(HeroActionProtocol.View.Attribute.TextStyle.BOLD);
				}
				for(int i=1;i<m_oHorizontalChildList.getChildCount();i++)
					m_oHorizontalChildList.getChilds().get(i).setTextStyle("");
			}
		}
		m_iSelectedTagButton = 0;
		
		// reset the "<" and ">" button to the original situation
		m_iStartIndex = 0;
		checkSwipeButton();
	}
	
	public void showPageByIndex(int iCommonPageTabListIndex) {
		m_oHorizontalChildList.scrollToIndex(iCommonPageTabListIndex * 2);
		clickTag(iCommonPageTabListIndex);
		m_iStartIndex = iCommonPageTabListIndex;
		checkSwipeButton();
	}
	
	@Override
	public void FrameTitleHeader_close() {
		for (FrameCommonPageContainerListener listener : listeners) {
			// Raise the event to parent
			listener.frameCommonPageContainer_CloseImageClicked();
		}
	}
}