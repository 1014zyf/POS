package commonui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import app.AppGlobal;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameDatePickerListener {
    void FrameDatePicker_clickCancel();
	void FrameDatePickerListener_clickOK();
}

public class FrameDatePicker extends VirtualUIFrame implements FrameTitleHeaderListener{
	private TemplateBuilder m_oTemplateBuilder;

	//private VirtualUILabel m_oLabelTitle;
//KingsleyKwan20171013ByKing	-----Start-----
	//private VirtualUIImage m_oImageClose;
//KingsleyKwan20171013ByKing	----- End -----
	private VirtualUIImage m_oImageAddYear;
	private VirtualUILabel m_oLabelYear;
	private VirtualUIImage m_oImageMinusYear;
	
	private VirtualUIImage m_oImageAddMonth;
	private VirtualUILabel m_oLabelMonth;
	private VirtualUIImage m_oImageMinusMonth;
	
	private VirtualUIImage m_oImageAddDay;
	private VirtualUILabel m_oLabelDay;
	private VirtualUIImage m_oImageMinusDay;
	
	private VirtualUIButton m_oButtonOK;
	private VirtualUIButton m_oButtonCancel;
	
	private DateTime m_oDateTime;
	private DateTimeFormatter m_oDateTimeFormatter;
	
	private FrameTitleHeader m_oTitleHeader;
	
	private static int MINIMIUM_YEAR = 1970; 
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameDatePickerListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameDatePickerListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameDatePickerListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
	
	public FrameDatePicker(DateTime oDateTime) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameDatePickerListener>();

		if(oDateTime == null)
			oDateTime = AppGlobal.getCurrentTime(false);

		m_oDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd, EEE");
		String langCode = "en";
		for(HashMap<String, String> oLangInfo:AppGlobal.g_oSupportedLangList) {
			int langIndex = Integer.parseInt(oLangInfo.get("index"));
			if(langIndex == AppGlobal.g_oCurrentLangIndex.get()) {
				langCode = oLangInfo.get("code");
				break;
			}
		}
		
		if(langCode.equals("zh-hk"))
			m_oDateTimeFormatter = m_oDateTimeFormatter.withLocale(Locale.CHINESE);
		else
		if(langCode.equals("zh-cn"))
			m_oDateTimeFormatter = m_oDateTimeFormatter.withLocale(Locale.CHINA);
		else
		if(langCode.equals("jpn"))
			m_oDateTimeFormatter = m_oDateTimeFormatter.withLocale(Locale.JAPAN);
		else
		if(langCode.equals("kor"))
			m_oDateTimeFormatter = m_oDateTimeFormatter.withLocale(Locale.KOREA);
		else
			m_oDateTimeFormatter = m_oDateTimeFormatter.withLocale(Locale.ENGLISH);
		
		m_oDateTime = oDateTime;
		int iYear = oDateTime.getYear();
		int iMonth = oDateTime.getMonthOfYear();
		int iDay = oDateTime.getDayOfMonth();
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraDatePicker.xml");
		
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		m_oTitleHeader.addListener(this);
		this.attachChild(m_oTitleHeader);
		
		VirtualUIFrame oFrameYear = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameYear, "fraYear");
		this.attachChild(oFrameYear);
		
		// Create add year button
		m_oImageAddYear = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageAddYear, "imgAddYear");
		m_oImageAddYear.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_nav_arrowup.png");
		m_oImageAddYear.allowClick(true);
		m_oImageAddYear.setClickServerRequestBlockUI(false);
		oFrameYear.attachChild(m_oImageAddYear);

		// Create year label
		m_oLabelYear = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelYear, "lblYear");
		m_oLabelYear.setValue(String.valueOf(iYear));
		oFrameYear.attachChild(m_oLabelYear);

		// Create minus year button
		m_oImageMinusYear = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageMinusYear, "imgMinusYear");
		m_oImageMinusYear.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_nav_arrowdown.png");
		m_oImageMinusYear.allowClick(true);
		m_oImageMinusYear.setClickServerRequestBlockUI(false);
		oFrameYear.attachChild(m_oImageMinusYear);
		
		VirtualUIFrame oFrameMonth = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameMonth, "fraMonth");
		this.attachChild(oFrameMonth);
		
		// Create add month button
		m_oImageAddMonth = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageAddMonth, "imgAddMonth");
		m_oImageAddMonth.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_nav_arrowup.png");
		m_oImageAddMonth.allowClick(true);
		m_oImageAddMonth.setClickServerRequestBlockUI(false);
		oFrameMonth.attachChild(m_oImageAddMonth);

		// Create month label
		m_oLabelMonth = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMonth, "lblMonth");
		m_oLabelMonth.setValue(String.valueOf(iMonth));
		oFrameMonth.attachChild(m_oLabelMonth);

		// Create minus month button
		m_oImageMinusMonth = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageMinusMonth, "imgMinusMonth");
		m_oImageMinusMonth.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_nav_arrowdown.png");
		m_oImageMinusMonth.allowClick(true);
		m_oImageMinusMonth.setClickServerRequestBlockUI(false);
		oFrameMonth.attachChild(m_oImageMinusMonth);
		
		VirtualUIFrame oFrameDay = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameDay, "fraDay");
		this.attachChild(oFrameDay);
		
		// Create add day button
		m_oImageAddDay = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageAddDay, "imgAddDay");
		m_oImageAddDay.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_nav_arrowup.png");
		m_oImageAddDay.allowClick(true);
		m_oImageAddDay.setClickServerRequestBlockUI(false);
		oFrameDay.attachChild(m_oImageAddDay);

		// Create day label
		m_oLabelDay = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDay, "lblDay");
		m_oLabelDay.setValue(String.valueOf(iDay));
		oFrameDay.attachChild(m_oLabelDay);

		// Create minus day button
		m_oImageMinusDay = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageMinusDay, "imgMinusDay");
		m_oImageMinusDay.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_nav_arrowdown.png");
		m_oImageMinusDay.allowClick(true);
		m_oImageMinusDay.setClickServerRequestBlockUI(false);
		oFrameDay.attachChild(m_oImageMinusDay);
//KingsleyKwan20171013ByKing	----- End -----
		// Create OK button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "btnOK");
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("ok"));
		m_oButtonOK.setVisible(true);
		this.attachChild(m_oButtonOK);	

		// Create Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "btnCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		m_oButtonCancel.setClickServerRequestBlockUI(false);
		this.attachChild(m_oButtonCancel);

		this.updateTitle();
	}
	
	public void updateTitle() {
		m_oTitleHeader.setTitle(m_oDateTimeFormatter.print(m_oDateTime));
	}
	
	public String getDate() {
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		return oFmt.print(m_oDateTime);
	}
	
	public void setTitle(String sTitle){
		m_oTitleHeader.setTitle(sTitle);
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
//KingsleyKwan20171013ByKing	-----Start-----
        if (iChildId == m_oButtonCancel.getId()) {
//KingsleyKwan20171013ByKing	----- End -----
        	for (FrameDatePickerListener listener : listeners) {
        		// Raise the event to parent
           		listener.FrameDatePicker_clickCancel();
            }
        	
        	bMatchChild = true;       	
        }
        else if (iChildId == this.m_oButtonOK.getId()) {
        	for (FrameDatePickerListener listener : listeners) {
        		// Raise the event to parent
           		listener.FrameDatePickerListener_clickOK();
            }
        	
        	bMatchChild = true;
        }
        else if (iChildId == this.m_oImageAddYear.getId()) {
        	m_oDateTime = m_oDateTime.plusYears(1);
        	m_oLabelYear.setValue(String.valueOf(m_oDateTime.getYear()));

        	// Handle last day of February
        	// If old year is leap year, last day of February in updated year is changed, update day text label
    		DateTime oEndOfMonth = m_oDateTime.dayOfMonth().withMaximumValue();
        	if(Integer.parseInt(m_oLabelDay.getValue()) > oEndOfMonth.getDayOfMonth())
        		m_oLabelDay.setValue(String.valueOf(oEndOfMonth.getDayOfMonth()));
    		
        	this.updateTitle();
    		
        	bMatchChild = true;
        }
        else if (iChildId == this.m_oImageMinusYear.getId()) {
        	if(m_oDateTime.getYear() == MINIMIUM_YEAR)
        		return false;
        	
        	m_oDateTime = m_oDateTime.minusYears(1);
        	m_oLabelYear.setValue(String.valueOf(m_oDateTime.getYear()));

        	// Handle last day of February
        	// If old year is leap year, last day of February in updated year is changed, update day text label
    		DateTime oEndOfMonth = m_oDateTime.dayOfMonth().withMaximumValue();
        	if(Integer.parseInt(m_oLabelDay.getValue()) > oEndOfMonth.getDayOfMonth())
        		m_oLabelDay.setValue(String.valueOf(oEndOfMonth.getDayOfMonth()));
    		
        	this.updateTitle();
    		
        	bMatchChild = true;
        }
        else if (iChildId == this.m_oImageAddMonth.getId()) {
        	m_oDateTime = m_oDateTime.plusMonths(1);
        	m_oLabelMonth.setValue(String.valueOf(m_oDateTime.getMonthOfYear()));

        	// Check whether the current day is greater than last day of the month, update the day text label if yes
    		DateTime oEndOfMonth = m_oDateTime.dayOfMonth().withMaximumValue();
        	if(Integer.parseInt(m_oLabelDay.getValue()) > oEndOfMonth.getDayOfMonth())
        		m_oLabelDay.setValue(String.valueOf(oEndOfMonth.getDayOfMonth()));

        	// Check whether the year is changed
        	if(m_oDateTime.getYear() != Integer.parseInt(m_oLabelYear.getValue()))
        		m_oLabelYear.setValue(String.valueOf(m_oDateTime.getYear()));
    		
        	this.updateTitle();
    		
        	bMatchChild = true;
        }
        else if (iChildId == this.m_oImageMinusMonth.getId()) {
        	m_oDateTime = m_oDateTime.minusMonths(1);
        	m_oLabelMonth.setValue(String.valueOf(m_oDateTime.getMonthOfYear()));

        	// Check whether the current day is greater than last day of the month, update the day text label if yes
    		DateTime oEndOfMonth = m_oDateTime.dayOfMonth().withMaximumValue();
        	if(Integer.parseInt(m_oLabelDay.getValue()) > oEndOfMonth.getDayOfMonth())
    			m_oLabelDay.setValue(String.valueOf(oEndOfMonth.getDayOfMonth()));

        	// Check whether the year is changed
        	if(m_oDateTime.getYear() != Integer.parseInt(m_oLabelYear.getValue()))
        		m_oLabelYear.setValue(String.valueOf(m_oDateTime.getYear()));
    		
        	this.updateTitle();
        	bMatchChild = true;
        }
        else if (iChildId == this.m_oImageAddDay.getId()) {
        	m_oDateTime = m_oDateTime.plusDays(1);
        	m_oLabelDay.setValue(String.valueOf(m_oDateTime.getDayOfMonth()));
        
        	// Check whether the day is greater than last day of the month, update the month text label if yes
        	if(m_oDateTime.getMonthOfYear() != Integer.parseInt(m_oLabelMonth.getValue()))
        		m_oLabelMonth.setValue(String.valueOf(m_oDateTime.getMonthOfYear()));

        	// Check whether the year is changed
        	if(m_oDateTime.getYear() != Integer.parseInt(m_oLabelYear.getValue()))
        		m_oLabelYear.setValue(String.valueOf(m_oDateTime.getYear()));
    		
        	this.updateTitle();
        	
        	bMatchChild = true;
        }
        else if (iChildId == this.m_oImageMinusDay.getId()) {
        	m_oDateTime = m_oDateTime.minusDays(1);
        	m_oLabelDay.setValue(String.valueOf(m_oDateTime.getDayOfMonth()));

        	// Check whether the day is less than 1, update the month text label if yes
        	if(m_oDateTime.getMonthOfYear() != Integer.parseInt(m_oLabelMonth.getValue()))
        		m_oLabelMonth.setValue(String.valueOf(m_oDateTime.getMonthOfYear()));

        	// Check whether the year is changed
        	if(m_oDateTime.getYear() != Integer.parseInt(m_oLabelYear.getValue()))
        		m_oLabelYear.setValue(String.valueOf(m_oDateTime.getYear()));
    		    		
        	this.updateTitle();
        	
        	bMatchChild = true;
        }
        
		return bMatchChild;
	}

	@Override
	public void FrameTitleHeader_close() {
    	for (FrameDatePickerListener listener : listeners) {
    		// Raise the event to parent
       		listener.FrameDatePicker_clickCancel();
        }
	}
}
