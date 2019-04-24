package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.TreeMap;

import om.PosCheckDiscount;
import om.PosPantryMessage;
import om.PosPantryMessageList;
import externallib.StringLib;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;

/** interface for the listeners/observers callback method */
interface FrameColumnElementListener {
	void frameColumnElement_clickPlaceButton(int iTargetColumnIndex, int iTargetSectionIndex);
	boolean frameColumnElement_isMinimumChargeItem(FuncCheckItem oFuncCheckItem);
}

public class FrameColumnElement extends VirtualUIFrame {
	TemplateBuilder m_oTemplateBuilder;
	
	private static int PADDING = 2;
	
	private int m_iColumnIndex;
	private int m_iSectionIndex;
	private int m_iLinesHeight;
	
	private boolean m_bShowPlaceButton;

	private PosPantryMessageList m_oPantryMesgList;
	private boolean m_bLoadPantryMessage;
	
	private ArrayList<FrameColumnElementLine> m_oDisplayLineList;
	private ArrayList<FuncCheckItem> m_oDisplayLineFuncCheckItemList;
	private TreeMap<String, FrameColumnElementLine> m_oSelectedLine;
	
	private VirtualUIFrame m_oFramePlaceButton;
	private VirtualUIImage m_oImagePlaceButton;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameColumnElementListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameColumnElementListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameColumnElementListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
	public FrameColumnElement(int iColumnIndex, int iSectionIndex, boolean bShowPlaceButton){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameColumnElementListener>();
		
		m_oDisplayLineList = new ArrayList<FrameColumnElementLine>();
		m_oDisplayLineFuncCheckItemList = new ArrayList<FuncCheckItem>();
		m_oSelectedLine = new TreeMap<String, FrameColumnElementLine>();

		m_oPantryMesgList = new PosPantryMessageList();
		m_bLoadPantryMessage = true;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraColumnElement.xml");
		if(bShowPlaceButton){
			m_oFramePlaceButton = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(m_oFramePlaceButton, "fraPlaceButton");
			m_oFramePlaceButton.allowClick(true);
			m_oFramePlaceButton.setClickServerRequestBlockUI(false);
			
			m_oImagePlaceButton = new VirtualUIImage();
			m_oTemplateBuilder.buildImage(m_oImagePlaceButton, "fraPlaceButtonImage");
			m_oImagePlaceButton.setLeft((m_oFramePlaceButton.getWidth() - m_oImagePlaceButton.getWidth()) / 2);
			m_oImagePlaceButton.setTop((m_oFramePlaceButton.getHeight() - m_oImagePlaceButton.getHeight()) / 2);
//KingsleyKwan20170918ByPaul		-----Start-----
			m_oImagePlaceButton.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_o1_import.png");
//KingsleyKwan20170918ByPaul		----- End -----
			m_oFramePlaceButton.attachChild(m_oImagePlaceButton);
			
			this.attachChild(m_oFramePlaceButton);
		}

		m_iColumnIndex = iColumnIndex;
		m_iSectionIndex = iSectionIndex;
		m_bShowPlaceButton = bShowPlaceButton;
	}
	
	public void addSection(int iSectionIndex, String sSectionDesc, boolean bShow){
		int iLineIndex = 0;
		int iTargetLineIndex = 0;
		int iTargetTop = 0;
		boolean bTargetFound = false;
		for(FrameColumnElementLine oFrameColumnElementLine:m_oDisplayLineList){
			if(oFrameColumnElementLine.getSectionIndex() == iSectionIndex){
				// Section found
				return;
			}

			if(oFrameColumnElementLine.getSectionIndex() > iSectionIndex){
				// Find the place to insert section
				bTargetFound = true;
			}
			
			if(bTargetFound){
				// Move down the rest lines
				oFrameColumnElementLine.setTop(oFrameColumnElementLine.getTop() + oFrameColumnElementLine.getHeight());
			}else{
				iTargetTop = oFrameColumnElementLine.getTop() + oFrameColumnElementLine.getHeight();
				iTargetLineIndex = iLineIndex + 1;
			}
			
			iLineIndex++;
		}

		// Insert the line
		FrameColumnElementLine oFrameColumnElementLine = new FrameColumnElementLine();
		m_oTemplateBuilder.buildFrame(oFrameColumnElementLine, "line");
		oFrameColumnElementLine.setTop(iTargetTop);
		if(!bShow)
			oFrameColumnElementLine.setHeight(0);
		oFrameColumnElementLine.addSection(iSectionIndex, sSectionDesc);
		this.attachChild(oFrameColumnElementLine);
		
		m_oDisplayLineList.add(iTargetLineIndex, oFrameColumnElementLine);
		m_oDisplayLineFuncCheckItemList.add(iTargetLineIndex, null);
		
		updateScreen();
	}
	
	public Integer addItem(int iSectionIndex, FuncCheckItem oFuncCheckItem){
		int iLineIndex = 0;
		int iTargetLineIndex = 0;
		int iTargetTop = 0;
		boolean bSectionFound = false;
		int iItemIndex = -1;
		
		String sQty = oFuncCheckItem.getCheckItem().getQty().stripTrailingZeros().toPlainString();
		String sDesc = oFuncCheckItem.getBilingualItemDescriptionByIndex(AppGlobal.g_oCurrentLangIndex.get());
		
		for(FrameColumnElementLine oFrameColumnElementLine:m_oDisplayLineList){
			if(oFrameColumnElementLine.getSectionIndex() == iSectionIndex){
				// Section found
				bSectionFound = true;
			}

			if(bSectionFound){
				if(oFrameColumnElementLine.getSectionIndex() == iSectionIndex){
					iTargetTop = oFrameColumnElementLine.getTop() + oFrameColumnElementLine.getHeight();
					iTargetLineIndex = iLineIndex + 1;
					iItemIndex++;
				}else{
					// Move down the rest lines
					oFrameColumnElementLine.setTop(oFrameColumnElementLine.getTop() + oFrameColumnElementLine.getHeight());
					oFrameColumnElementLine.setItemIndex(oFrameColumnElementLine.getItemIndex() + 1);
				}
			}
			
			iLineIndex++;
		}

		if(bSectionFound == false){
			// Section is not found
			return -1;
		}

		// Insert the line
		FrameColumnElementLine oFrameColumnElementLine = new FrameColumnElementLine();
		m_oTemplateBuilder.buildFrame(oFrameColumnElementLine, "line");
		oFrameColumnElementLine.setTop(iTargetTop + PADDING);
		oFrameColumnElementLine.addItem(iSectionIndex, iItemIndex, sQty, sDesc);
		
		// Create modifier list for display
		if(oFuncCheckItem.hasModifier()) {
			StringBuilder sModifierList = new StringBuilder();
			for(int i=0; i<oFuncCheckItem.getModifierList().size(); i++){
				FuncCheckItem oModifierFuncCheckItem = oFuncCheckItem.getModifierList().get(i);
				
				if(sModifierList.length() > 0){
					sModifierList.append(", ");
				}
				sModifierList.append(oModifierFuncCheckItem.getItemShortDescriptionByIndex(AppGlobal.g_oCurrentLangIndex.get()));
			}
			oFrameColumnElementLine.setInformation(sModifierList.toString(), "");
		}
		
		// Build discount information
		StringBuilder sModifierDiscountList = new StringBuilder();
		if(oFuncCheckItem.hasItemDiscount(false)) {
			for(int i = 0; i < oFuncCheckItem.getItemDiscountList().size(); i++) {
				PosCheckDiscount oCheckDiscount = oFuncCheckItem.getItemDiscountList().get(i);
				
				if(sModifierDiscountList.length() > 0){
					sModifierDiscountList.append(", ");
				}
	        	if(oCheckDiscount.getShortName(AppGlobal.g_oCurrentLangIndex.get()).isEmpty())
	        		sModifierDiscountList.append(oCheckDiscount.getName(AppGlobal.g_oCurrentLangIndex.get()));
	        	else
	        		sModifierDiscountList.append(oCheckDiscount.getShortName(AppGlobal.g_oCurrentLangIndex.get()));
			}
		}
		String sDiscountInfo = sModifierDiscountList.toString();
		if(sDiscountInfo.length() > 0){
			oFrameColumnElementLine.setDiscount(sDiscountInfo, "");
		}

		// Create pantry message for display
		PosPantryMessage oSelectedPantryMessage = new PosPantryMessage();
		if(oFuncCheckItem.hasPantryMessage()) {
			if(m_bLoadPantryMessage == true){
				m_oPantryMesgList.readAll();
				m_bLoadPantryMessage = false;
			}
			StringBuilder sPantryMessage = new StringBuilder();
			for(int i=0; i < oFuncCheckItem.getPantryMessageList().size(); i++){
				int oPantryMessageFuncCheckItem = oFuncCheckItem.getPantryMessageList().get(i);
				
				if(sPantryMessage.length() > 0){
					sPantryMessage.append(", ");
				}
				oSelectedPantryMessage = m_oPantryMesgList.getPosPantryMessageByIndex(oPantryMessageFuncCheckItem);
				if(oSelectedPantryMessage.getName(AppGlobal.g_oCurrentLangIndex.get()) != null)
						sPantryMessage.append(oSelectedPantryMessage.getName(AppGlobal.g_oCurrentLangIndex.get()));
			}
			oFrameColumnElementLine.setPantryMessage(sPantryMessage.toString(), "");
		}
		
		// Create display Message for display
		if(oFuncCheckItem.hasDisplayInformation()) {
			StringBuilder sDisplayInformations = new StringBuilder();
			for(int i=0; i < oFuncCheckItem.getDisplayInformationList().size(); i++){
				String sDisplayInformation = oFuncCheckItem.getDisplayInformationList().get(i);
				
				if(sDisplayInformations.length() > 0){
					sDisplayInformations.append(", ");
				}
				sDisplayInformations.append(sDisplayInformation);
			}
			oFrameColumnElementLine.setDisplayInformation(sDisplayInformations.toString(), "");
		}
		
		oFrameColumnElementLine.allowClick(true);
		oFrameColumnElementLine.setClickServerRequestBlockUI(false);
		this.attachChild(oFrameColumnElementLine);
		
		m_oDisplayLineList.add(iTargetLineIndex, oFrameColumnElementLine);
		m_oDisplayLineFuncCheckItemList.add(iTargetLineIndex, oFuncCheckItem);
		
		updateScreen();
		
		return iItemIndex;
	}
	
	public FuncCheckItem removeItem(int iSectionIndex, int iItemIndex){
		int iIndex = 0;
		int iTargetLineIndex = 0;
		int iLoopIndex = 0;
		int iTargetLineHeight = 0;
		boolean bSectionFound = false;
		boolean bItemFound = false;
		FuncCheckItem oTargetFuncCheckItem = null;
		for(FrameColumnElementLine oFrameColumnElementLine:m_oDisplayLineList){
			if(bSectionFound == false && oFrameColumnElementLine.getSectionIndex() == iSectionIndex){
				// Section found
				bSectionFound = true;
				iIndex++;
				continue;
			}

			if(bSectionFound && !bItemFound && oFrameColumnElementLine.getSectionIndex() == iSectionIndex){
				if(iLoopIndex == iItemIndex){
					bItemFound = true;
					iTargetLineIndex = iIndex;
					
					// Check FuncCheckItem whether is minimum charge item
					for (FrameColumnElementListener listener : listeners) {
						// Raise the event to parent
						if(listener.frameColumnElement_isMinimumChargeItem(m_oDisplayLineFuncCheckItemList.get(iTargetLineIndex)))
							return null;
						break;
					}
					
					iTargetLineHeight = oFrameColumnElementLine.getHeight();
					this.removeChild(oFrameColumnElementLine.getId());
				}
				iLoopIndex++;
			}
			
			if(bItemFound && iIndex > iTargetLineIndex){
				// Move up the rest lines
				oFrameColumnElementLine.setTop(oFrameColumnElementLine.getTop() - iTargetLineHeight - PADDING);
				oFrameColumnElementLine.setItemIndex(oFrameColumnElementLine.getItemIndex() - 1);
			}
			
			iIndex++;
		}
		
		if(bItemFound){
			m_oDisplayLineList.remove(iTargetLineIndex);
			oTargetFuncCheckItem = m_oDisplayLineFuncCheckItemList.remove(iTargetLineIndex);
		}
		
		updateScreen();
		
		return oTargetFuncCheckItem;
	}
	
	public void removeAllItem(){
		
		TreeMap<String, Integer> oRemoveIndexPairs = new TreeMap<String, Integer>(Collections.reverseOrder());
		
		for(FrameColumnElementLine oFrameColumnElementLine:m_oDisplayLineList){
			String sKey = StringLib.IntToStringWithLeadingZero(oFrameColumnElementLine.getSectionIndex(), 3) + "_" + StringLib.IntToStringWithLeadingZero(oFrameColumnElementLine.getItemIndex(), 5);
			oRemoveIndexPairs.put(sKey, 0);
		}

		for(Entry<String, Integer> entry:oRemoveIndexPairs.entrySet()){
			String split[] = entry.getKey().split("_");
			removeItem(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
		}
	}
	
	private void updateScreen(){
		m_iLinesHeight = 0;
		for(FrameColumnElementLine oFrameColumnElementLine:m_oDisplayLineList){
			m_iLinesHeight += (oFrameColumnElementLine.getHeight() + PADDING);
		}
		this.setHeight(m_iLinesHeight);
	}
	
	public void updateScreenByParent(int iElementHeight){
		this.setHeight(iElementHeight);
		
		if(m_bShowPlaceButton){
			m_oFramePlaceButton.setHeight(this.getHeight() - (2 * m_oFramePlaceButton.getTop()));
			m_oImagePlaceButton.setTop((m_oFramePlaceButton.getHeight() - m_oImagePlaceButton.getHeight()) / 2);
			m_oFramePlaceButton.bringToTop();
		}
	}
	
	public Integer getColumnIndex(){
		return m_iColumnIndex;
	}
	
	public Integer getSectionIndex(){
		return m_iSectionIndex;
	}
	
	public Integer getLinesHeight(){
		return m_iLinesHeight;
	}
	
	public TreeMap<String, Integer> getSelectedLine(){
		TreeMap<String, Integer> oReturnList = new TreeMap<String, Integer>(Collections.reverseOrder());
		
		for(Entry<String, FrameColumnElementLine> entry:m_oSelectedLine.entrySet()){
			FrameColumnElementLine oFrameColumnElementLine = entry.getValue();
			
			String sKey = StringLib.IntToStringWithLeadingZero(m_iColumnIndex, 5) + "_" + StringLib.IntToStringWithLeadingZero(m_iSectionIndex, 5) + "_" + StringLib.IntToStringWithLeadingZero(oFrameColumnElementLine.getSectionIndex(), 5) + "_" + StringLib.IntToStringWithLeadingZero(oFrameColumnElementLine.getItemIndex(), 5);
			oReturnList.put(sKey, 0);
		}
		
		return oReturnList;
	}
	
	public void deSelectAllLine(){

		for(Entry<String, FrameColumnElementLine> entry:m_oSelectedLine.entrySet()){
			FrameColumnElementLine oFrameColumnElementLine = entry.getValue();
			oFrameColumnElementLine.setBackgroundColor("#00000000");
			oFrameColumnElementLine.setStrokeColor("#00000000");
		}
		
		m_oSelectedLine.clear();
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (m_bShowPlaceButton && m_oFramePlaceButton.getId() == iChildId) {
			for (FrameColumnElementListener listener : listeners) {
				// Raise the event to parent
				listener.frameColumnElement_clickPlaceButton(m_iColumnIndex, m_iSectionIndex);
				bMatchChild = true;
				break;
			}
		}else{		
			for(FrameColumnElementLine oFrameColumnElementLine:m_oDisplayLineList){
				if(oFrameColumnElementLine.getId() == iChildId){
					
					String sKey = oFrameColumnElementLine.getSectionIndex() + "_" + oFrameColumnElementLine.getItemIndex(); 
					if(m_oSelectedLine.containsKey(sKey)){
						// De-select the item
						m_oSelectedLine.remove(sKey);
						oFrameColumnElementLine.setBackgroundColor("#00000000");
						oFrameColumnElementLine.setStrokeColor("#00000000");
					}else{
						// Select the item
						m_oSelectedLine.put(sKey, oFrameColumnElementLine);
						oFrameColumnElementLine.setBackgroundColor("#E1ECF8");
						oFrameColumnElementLine.setStrokeColor("#D1D19F");
						
						if(!m_bShowPlaceButton)
							oFrameColumnElementLine.setWidth(277);
					}
					
					bMatchChild = true;
					break;
				}
			}
		}
		return bMatchChild;
	}
}
