package app;

import java.util.ArrayList;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FrameSearchUserListener {
    void frameSearchUser_clickCancel();
    void frameSearchUser_clickSearchResultRecord(int iItemidex);
}

public class FrameSearchUser extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oFrameTitleHeader;
	FrameHorizontalTabList m_oFrameHorizontalTabList;
	private FrameCommonBasket m_oBasketResultList;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameSearchUserListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameSearchUserListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameSearchUserListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
	//Constructor
	public FrameSearchUser() {		
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameSearchUserListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraSearchUser.xml");
	}
	
    public void init(String sTitle){	
    	// Load child elements from template
		/////////////////////////////////////////////////////////////////
		// Title bar
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(true);
		m_oFrameTitleHeader.addListener(this);
		m_oFrameTitleHeader.setTitle(sTitle);
		this.attachChild(m_oFrameTitleHeader);
		
		m_oBasketResultList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oBasketResultList, "fraResultList");
		m_oBasketResultList.init();
		m_oBasketResultList.addListener(this);
		this.attachChild(m_oBasketResultList);
		
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();

    	ArrayList<String> sFieldValues = new ArrayList<String>();
//KingsleyKwan20170918ByNick		-----Start-----
		iFieldWidths.add(220);
		sFieldValues.add(AppGlobal.g_oLang.get()._("employee_number"));
		iFieldWidths.add(730);
    	sFieldValues.add(AppGlobal.g_oLang.get()._("name"));
    	m_oBasketResultList.addHeader(iFieldWidths, sFieldValues);
    	m_oBasketResultList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
//KingsleyKwan20170918ByNick		----- End -----
		/*int iSearchHeaderLabelTop = m_oLabelSearchHeader.getTop();
		int iSearchTextBoxTop = m_oTxtboxSearchValue.getTop();
		
		m_oTxtboxSearchValue.setTop(iSearchHeaderLabelTop);
		m_oLabelSearchHeader.setTop(iSearchTextBoxTop);
		m_oLabelSearchHeader.setValue(AppGlobal.g_oLang.get()._("user_no")+":");*/
		
		m_oBasketResultList.setVisible(true);	
	}
    
    public void addUserToResultList(int iSectionId, int iItemIndex, String sUserNo, String sUserName){
   		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
    	ArrayList<String> sFieldValues = new ArrayList<String>();
    	ArrayList<String> sFieldAligns = new ArrayList<String>();
    	int iRowHeight = 0;
//KingsleyKwan20170918ByNick		-----Start-----
		// User no.
		iFieldWidths.add(220);
		sFieldValues.add(sUserNo);
		sFieldAligns.add("");
		
		// User name
		iFieldWidths.add(730);
		sFieldValues.add(sUserName);
		sFieldAligns.add("");
//KingsleyKwan20170918ByNick		----- End -----
    	if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
    		iRowHeight = 50;
    	m_oBasketResultList.addItem(iSectionId, iItemIndex, iRowHeight, iFieldWidths, sFieldValues, sFieldAligns, null, null);
    	m_oBasketResultList.moveScrollToItem(iSectionId, iItemIndex);
   	}
	
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		return bMatchChild;
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex,
			String sNote) {
		// TODO Auto-generated method stub
		for (FrameSearchUserListener listener : listeners) {
			listener.frameSearchUser_clickSearchResultRecord(iItemIndex);
			break;
		}
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId, int iSectionIndex, int iItemIndex,
			int iFieldIndex, String sNote) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void FrameTitleHeader_close() {
		// TODO Auto-generated method stub
		for (FrameSearchUserListener listener : listeners) {
			// Raise the event to parent
			listener.frameSearchUser_clickCancel();
		}
	}
}
