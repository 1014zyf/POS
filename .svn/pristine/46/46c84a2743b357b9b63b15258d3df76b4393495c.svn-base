package app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import om.PosCheck;


import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIFrame;

/** interface for the listeners/observers callback method */
interface FrameCheckHistoryListener {
    void frameCheckHistory_clickOK();
	void frameCheckHistory_selectedRecordClicked(String sNote);
}

public class FrameCheckHistory extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener {
	
	private TemplateBuilder m_oTemplateBuilder;
	private FrameTitleHeader m_oFrameTitleHeader;
	private FrameCommonBasket m_oCommonBasket;
	private HashMap<String, Integer> m_oColumnWidths;
	
	//private VirtualUIButton m_oButtonOK;
	
	private boolean m_bClickable;
	
	private ArrayList<PosCheck> m_oCheckList;

	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameCheckHistoryListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameCheckHistoryListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameCheckHistoryListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }

    public void init(boolean bRecordClickable) {
		m_oTemplateBuilder = new TemplateBuilder();
		m_bClickable = bRecordClickable;
		listeners = new ArrayList<FrameCheckHistoryListener>();
		
		m_oCheckList = new ArrayList<PosCheck>();
		m_oColumnWidths = new HashMap<String, Integer>();
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oColumnWidths.put("checkNo", 100);
			m_oColumnWidths.put("cover", 50);
			m_oColumnWidths.put("openTime", 100);
			m_oColumnWidths.put("closeTime", 100);
			m_oColumnWidths.put("total", 110);
		}else {
//KingsleyKwan20170918ByNick		-----Start-----
			m_oColumnWidths.put("checkNo", 218);
			m_oColumnWidths.put("cover", 168);
			m_oColumnWidths.put("openTime", 227);
			m_oColumnWidths.put("closeTime", 218);
			m_oColumnWidths.put("total", 135);
//KingsleyKwan20170918ByNick		----- End -----
		}
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraCheckHistory.xml");

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
	    m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
	    m_oFrameTitleHeader.init(true);
	    m_oFrameTitleHeader.addListener(this);
	    this.attachChild(m_oFrameTitleHeader);
		
		// Check Payment basket
		m_oCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oCommonBasket, "fraCheckHistory");
		m_oCommonBasket.init();
		m_oCommonBasket.addListener(this);
		this.attachChild(m_oCommonBasket);
		
		m_oFrameTitleHeader.bringToTop();
		
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
    	ArrayList<String> sFieldValues = new ArrayList<String>();
    	iFieldWidths.add(m_oColumnWidths.get("checkNo"));
    	sFieldValues.add(AppGlobal.g_oLang.get()._("check_no"));
    	iFieldWidths.add(m_oColumnWidths.get("cover"));
    	sFieldValues.add(AppGlobal.g_oLang.get()._("cover"));
    	iFieldWidths.add(m_oColumnWidths.get("openTime"));
    	sFieldValues.add(AppGlobal.g_oLang.get()._("open_time"));
    	iFieldWidths.add(m_oColumnWidths.get("closeTime"));
    	sFieldValues.add(AppGlobal.g_oLang.get()._("close_time"));
    	iFieldWidths.add(m_oColumnWidths.get("total"));
    	sFieldValues.add(AppGlobal.g_oLang.get()._("total"));
		m_oCommonBasket.addHeader(iFieldWidths, sFieldValues);
		m_oCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
    }
	
	public void addCheckHistoryRecord(ArrayList<PosCheck> oCheckList, int iCheckRoundDecimal) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		
		for(int i = 0; i < oCheckList.size(); i++) {
			PosCheck oCheck = oCheckList.get(i);
			
	    	ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
	    	ArrayList<String> sFieldValues = new ArrayList<String>();
	    	ArrayList<String> sFieldAligns = new ArrayList<String>();
//KingsleyKwan20170918ByNick		-----Start-----
			iFieldWidths.add(m_oColumnWidths.get("checkNo"));
			sFieldValues.add(oCheck.getCheckPrefixNo());
			sFieldAligns.add("");
			iFieldWidths.add(m_oColumnWidths.get("cover"));
			sFieldValues.add(Integer.toString(oCheck.getGuests()));
			sFieldAligns.add("");
			iFieldWidths.add(m_oColumnWidths.get("openTime"));
			sFieldValues.add(timeFormat.format(oCheck.getOpenLocTime().toDate()));
			sFieldAligns.add("");
			iFieldWidths.add(m_oColumnWidths.get("closeTime"));
			sFieldValues.add(timeFormat.format(oCheck.getCloseLocTime().toDate()));
			sFieldAligns.add("");
			iFieldWidths.add(m_oColumnWidths.get("total"));
			sFieldValues.add(StringLib.BigDecimalToString(oCheck.getCheckTotal(), iCheckRoundDecimal));
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
//KingsleyKwan20170918ByNick		----- End -----
			m_oCommonBasket.addItem(0, i, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
			for(int j=0; j<sFieldValues.size(); j++)
				m_oCommonBasket.setFieldTextSize(0, i, j, 24);
			m_oCheckList.add(oCheck);
		}
		
	}

    public void setTitle(String sValue) {
    	m_oFrameTitleHeader.setTitle(sValue);
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
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex,
			int iItemIndex, int iFieldIndex, String sNote) {
		if (!m_bClickable)
			return;
		
		PosCheck oCheck = m_oCheckList.get(iItemIndex);
		for(FrameCheckHistoryListener listener : listeners) {
			listener.frameCheckHistory_selectedRecordClicked(oCheck.getCheckId());
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
		for (FrameCheckHistoryListener listener : listeners) {
			// Raise the event to parent
			listener.frameCheckHistory_clickOK();
		}
	}
}
