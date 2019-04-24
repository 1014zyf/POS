package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameStationInfoListener {
}

public class FrameStationInfo extends VirtualUIFrame implements FrameCommonBasketListener{
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUILabel m_oLabelStationNameHeader;
	private VirtualUILabel m_oLabelStationName;
	private VirtualUILabel m_oLabelPriceLevelHeader;
	private VirtualUILabel m_oLabelPriceLevel;
	private VirtualUILabel m_oLabelMenuPeriodHeader;
	private VirtualUILabel m_oLabelMenuPeriod;
	private VirtualUILabel m_oLabelSpecialPeriodHeader;
	private VirtualUILabel m_oLabelSpecialPeriod;
	private VirtualUILabel m_oLabelHolidayHeader;
	private VirtualUILabel m_oLabelHoliday;
	private VirtualUILabel m_oLabelSpecialDayHeader;
	private VirtualUILabel m_oLabelSpecialDay;
	private VirtualUIImage m_oImageClose;
	private VirtualUIImage m_oImageBackground;
	
	//Alert Message UI
	private VirtualUILabel m_oLabelStationInfo;
	private VirtualUIFrame m_oUnderLineFrame;
	private VirtualUILabel m_oLabelAlertMessageInfo;
	private VirtualUIFrame m_oUnderLineFrameForAlertMessage;
	private FrameCommonBasket m_oAlertMessageList;
	private VirtualUIFrame m_oFramePage;
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIImage m_oImgButtonNextPage;
	private VirtualUILabel m_oLblNewFramePositionForAlertMessage;
	
	private boolean m_bShowAlertMessageUI = false;
	private HashMap<Long , ClsAlertMessage> m_oMessageList ;
	private final int m_iListItemNumber = 5;
	private int m_iCurrentPage = 0;
	private int m_TotalPage = 0;
	private List <Long> m_oMessageSeqNumList;
	private int m_iOriginalFrameLeft = 0;
	private int m_iOriginalFrameHeight = 0;
	private int m_iOriginalFrameWidth = 0;


	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameStationInfoListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameStationInfoListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameStationInfoListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FrameStationInfo() {
		m_oMessageList = new HashMap<Long, ClsAlertMessage>();
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameStationInfoListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraStationInfo.xml");
		
		//if(AppGlobal.g_sDisplayMode.get() .equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			// Background image
			m_oImageBackground = new VirtualUIImage();
			m_oTemplateBuilder.buildImage(m_oImageBackground, "imgBackground");
			m_oImageBackground.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/backgrounds/station_info_background.png");
			m_oImageBackground.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_TO_FIT);
			this.attachChild(m_oImageBackground);
		//}
		// Station Name header
		m_oLabelStationNameHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelStationNameHeader, "lblStationNameHeader");
		m_oLabelStationNameHeader.setValue(AppGlobal.g_oLang.get()._("station_id", ""));
		this.attachChild(m_oLabelStationNameHeader);
		
		// Station Name
		m_oLabelStationName = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelStationName, "lblStationName");
		this.attachChild(m_oLabelStationName);
		
		// Price level header
		m_oLabelPriceLevelHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelPriceLevelHeader, "lblPriceLevelHeader");
		m_oLabelPriceLevelHeader.setValue(AppGlobal.g_oLang.get()._("price_level", ""));
		this.attachChild(m_oLabelPriceLevelHeader);
		
		// Price level
		m_oLabelPriceLevel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelPriceLevel, "lblPriceLevel");
		this.attachChild(m_oLabelPriceLevel);
		
		// Menu period header
		m_oLabelMenuPeriodHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMenuPeriodHeader, "lblMenuPeriodHeader");
		m_oLabelMenuPeriodHeader.setValue(AppGlobal.g_oLang.get()._("menu_period", ""));
		this.attachChild(m_oLabelMenuPeriodHeader);
		
		// Menu period
		m_oLabelMenuPeriod = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMenuPeriod, "lblMenuPeriod");
		this.attachChild(m_oLabelMenuPeriod);
		
		// Special period header
		m_oLabelSpecialPeriodHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSpecialPeriodHeader, "lblSpecialPeriodHeader");
		m_oLabelSpecialPeriodHeader.setValue(AppGlobal.g_oLang.get()._("special_menu", ""));
		this.attachChild(m_oLabelSpecialPeriodHeader);
		
		// Special period
		m_oLabelSpecialPeriod = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSpecialPeriod, "lblSpecialPeriod");
		this.attachChild(m_oLabelSpecialPeriod);
		
		// Holiday header
		m_oLabelHolidayHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelHolidayHeader, "lblHolidayHeader");
		m_oLabelHolidayHeader.setValue(AppGlobal.g_oLang.get()._("holiday", ""));
		this.attachChild(m_oLabelHolidayHeader);
		
		// Holiday
		m_oLabelHoliday = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelHoliday, "lblHoliday");
		this.attachChild(m_oLabelHoliday);
		
		// Special day header
		m_oLabelSpecialDayHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSpecialDayHeader, "lblSpecialDayHeader");
		m_oLabelSpecialDayHeader.setValue(AppGlobal.g_oLang.get()._("special_day", ""));
		this.attachChild(m_oLabelSpecialDayHeader);
		
		// Special day
		m_oLabelSpecialDay = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSpecialDay, "lblSpecialDay");
		this.attachChild(m_oLabelSpecialDay);
		
		// Close button
		m_oImageClose = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageClose, "imgClose");
		m_oImageClose.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_esc_sh.png");
		m_oImageClose.allowClick(true);
		m_oImageClose.setClickServerRequestBlockUI(false);
		this.attachChild(m_oImageClose);
		
		//station information label
		m_oLabelStationInfo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelStationInfo, "lblStationInfo");
		m_oLabelStationInfo.setValue(AppGlobal.g_oLang.get()._("station_information",""));
		m_oLabelStationInfo.setVisible(false);
		this.attachChild(m_oLabelStationInfo);
		
		//underline
		m_oUnderLineFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oUnderLineFrame, "fraUnderline");
		m_oUnderLineFrame.setVisible(false);
		this.attachChild(m_oUnderLineFrame);
		
		//Alert Message Header
		m_oLabelAlertMessageInfo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAlertMessageInfo, "lblAlertMesssageLabel");
		m_oLabelAlertMessageInfo.setValue(AppGlobal.g_oLang.get()._("alert_message",""));
		m_oLabelAlertMessageInfo.setVisible(false);
		this.attachChild(m_oLabelAlertMessageInfo);
		
		//underline for Alert Message
		m_oUnderLineFrameForAlertMessage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oUnderLineFrameForAlertMessage, "fraUnderlineForAlertMessage");
		m_oUnderLineFrameForAlertMessage.setVisible(false);
		this.attachChild(m_oUnderLineFrameForAlertMessage);
		
		//Create Page label
		m_oFramePage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePage, "fraPage");
	
		// Create prev page button
		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevPage");
		m_oImgButtonPrevPage.allowClick(true);
		m_oImgButtonPrevPage.setClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.allowLongClick(true);
		m_oImgButtonPrevPage.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oImgButtonPrevPage);
					
		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.allowLongClick(true);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oImgButtonNextPage);
					
		VirtualUIImage oImage = new VirtualUIImage();
		oImage.setWidth(m_oFramePage.getWidth());
		oImage.setHeight(m_oFramePage.getHeight());
		m_oFramePage.attachChild(oImage);
					
		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oLblPage.setWidth(m_oFramePage.getWidth());
		m_oLblPage.setHeight(m_oFramePage.getHeight());
		m_oFramePage.attachChild(m_oLblPage);
		this.attachChild(m_oFramePage);
		
		m_oFramePage.setVisible(false);
		m_oImgButtonPrevPage.setVisible(false);
		m_oImgButtonNextPage.setVisible(false);
		
		// new frame size for displaying alert message
		m_oLblNewFramePositionForAlertMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblNewFramePositionForAlertMessage, "lblNewFramePositionForAlertMessage");
		
	}
	
	
	public void addAlertMessageToList(ClsAlertMessage oMessage, long lSeqNum) {
		
		DateTimeFormatter oDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		int iIndex = m_oAlertMessageList.getItemCount(0);
		String sDateTime = "";
		String sAction = "";
		String sSubjectType = "";
		String sAdditionalInfo = "";
		
		// get Action text
		if (oMessage.getAction().equals(ClsAlertMessage.PRINT_QUEUE_STATUS_PAPER_OUT))
			sAction = AppGlobal.g_oLang.get()._("paper_out");
		else if (oMessage.getAction().equals(ClsAlertMessage.PRINT_QUEUE_STATUS_NORMAL))
			sAction = AppGlobal.g_oLang.get()._("normal");
		else if (oMessage.getAction().equals(ClsAlertMessage.PRINT_QUEUE_STATUS_OFFLINE))
			sAction = AppGlobal.g_oLang.get()._("offline");
		else if (oMessage.getAction().equals(ClsAlertMessage.PRINT_QUEUE_STATUS_PAPER_NEAR_END))
			sAction = AppGlobal.g_oLang.get()._("paper_near_end");
		
		// get Additional Info
		if (oMessage.getAdditionalInfo().equals(ClsAlertMessage.ADDITIONAL_INFO_REDIRECT_TO))
			sAdditionalInfo = AppGlobal.g_oLang.get()._("redirect_to");
		
		// get Subject Type text
		sSubjectType = oMessage.getSubjectType();
		
		String sSubject = oMessage.getSubjectName(AppGlobal.g_oCurrentLangIndex.get());
		String sObject = oMessage.getObjectName(AppGlobal.g_oCurrentLangIndex.get());
		String sMessage = sAction;
		DateTime oMessageArrivalTime = oMessage.getMessageArrivalTime();
		if (oMessageArrivalTime != null)
			sDateTime = oMessageArrivalTime.toString(oDateTimeFormatter);
		if (!sAdditionalInfo.isEmpty())
			sMessage +="("+ sAdditionalInfo +" "+sObject+")";
		
		this.addRecord(0, iIndex, sSubjectType, sSubject, sMessage, sDateTime, lSeqNum);
	}
	
	public void updateItemList() {
		m_oAlertMessageList.removeAllItems(0);
		// load page content 
		for (int i = (m_iListItemNumber*(m_iCurrentPage-1)); i< m_oMessageSeqNumList.size() ; i++ ) {
			if(m_oAlertMessageList.getItemCount(0) > (m_iListItemNumber-1)) 
				break;
			if (this.m_oMessageList.containsKey(m_oMessageSeqNumList.get(i))) {
				ClsAlertMessage oTempMessage = m_oMessageList.get(m_oMessageSeqNumList.get(i));
				if(oTempMessage != null) 
					addAlertMessageToList(oTempMessage, m_oMessageSeqNumList.get(i));
			}
		}
		// update page change
		if (m_iCurrentPage == 1) {
			this.showPageUp(false);
			this.showPageDown(true);
		}else if (m_iCurrentPage == m_TotalPage){
			this.showPageUp(true);
			this.showPageDown(false);
		}else {
			this.showPageUp(true);
			this.showPageDown(true);
		}
		m_oLblPage.setValue(m_iCurrentPage+"/"+m_TotalPage);
		
	}
	
	public void loadHeaderDescription() {
		// remove Alert Message list
		if (m_oAlertMessageList != null)
			this.removeChild(m_oAlertMessageList.getId());
		
		//Alert Message List 
		m_oAlertMessageList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oAlertMessageList, "fraAlertMessageList");
		m_oAlertMessageList.init();
		m_oAlertMessageList.addListener(this);
		this.attachChild(m_oAlertMessageList);
		
		//set Alert Message List Header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		
		iFieldWidths.add(m_oAlertMessageList.getWidth()/5);
		iFieldWidths.add(m_oAlertMessageList.getWidth()/5);
		iFieldWidths.add(m_oAlertMessageList.getWidth()/5*2);
		iFieldWidths.add(m_oAlertMessageList.getWidth()/5);
				
		sFieldValues.add(AppGlobal.g_oLang.get()._("category"));
		sFieldValues.add(AppGlobal.g_oLang.get()._("subject"));
		sFieldValues.add(AppGlobal.g_oLang.get()._("messages"));
		sFieldValues.add(AppGlobal.g_oLang.get()._("log_time"));
				
		m_oAlertMessageList.addHeader(iFieldWidths, sFieldValues);
		m_oAlertMessageList.setHeaderFormat(40, 20, "");
		m_oAlertMessageList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
	}
	
	public void initAlertMessageList(HashMap<Long,ClsAlertMessage> oMessageList) {
		if(m_oMessageList.size() != 0)
			 AppGlobal.getActiveClient().setLastAlertMessageReadSeqNum(this.m_oMessageSeqNumList.get(0));
		loadHeaderDescription();
		m_oAlertMessageList.removeAllItems(0);
		// get Message sequence number list 
		this.m_oMessageList = oMessageList;
		m_oMessageSeqNumList = new ArrayList(m_oMessageList.keySet());
		Collections.sort(m_oMessageSeqNumList);
		Collections.reverse(m_oMessageSeqNumList);
		//load First page record 
		for (int i = 0; i< m_oMessageSeqNumList.size() ; i++) {
			if(m_oAlertMessageList.getItemCount(0) > (m_iListItemNumber-1)) 
				break;
			if (this.m_oMessageList.containsKey(m_oMessageSeqNumList.get(i))) {
				ClsAlertMessage oTemMessage = m_oMessageList.get(m_oMessageSeqNumList.get(i));
				if(oTemMessage != null) 
					addAlertMessageToList(oTemMessage, m_oMessageSeqNumList.get(i));
			}
		}
		// handle page change
		if (m_oMessageList.size() > m_iListItemNumber) {
			m_iCurrentPage = 1;
			m_TotalPage = (int)Math.ceil(1.0*m_oMessageList.size()/m_iListItemNumber);
			m_oLblPage.setValue(m_iCurrentPage+"/"+m_TotalPage);
			m_oFramePage.setVisible(true);
			m_oImgButtonNextPage.setVisible(true);
			m_oImgButtonPrevPage.setVisible(true);
			this.showPageUp(false);
			this.showPageDown(true);
		}else {
			m_oFramePage.setVisible(false);
			m_oImgButtonNextPage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
		}

	}
	
	public void addRecord(int iSectionId, int iItemIndex, String sSubjectType, String sSubject, String sMessage, String  sTime ,long lMesSeq) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		ArrayList<String> sFieldTypes = new ArrayList<String>();
		
		iFieldWidths.add(m_oAlertMessageList.getWidth()/5);
		iFieldWidths.add(m_oAlertMessageList.getWidth()/5);
		iFieldWidths.add(m_oAlertMessageList.getWidth()/5*2);
		iFieldWidths.add(m_oAlertMessageList.getWidth()/5);
		
		
		sFieldValues.add(sSubjectType);
		sFieldValues.add(sSubject);
		sFieldValues.add(sMessage);
		sFieldValues.add(sTime);
		
		sFieldAligns.add("");
		sFieldAligns.add("");
		sFieldAligns.add("");
		sFieldAligns.add("");
		
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);

		ArrayList<VirtualUIBasicElement> oSubmitIdElements = new ArrayList<VirtualUIBasicElement>();
		m_oAlertMessageList.addItem(iSectionId, iItemIndex, 46, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, oSubmitIdElements);
		for (int i = 0 ; i < 4 ; i++)
			m_oAlertMessageList.setFieldTextSize(iSectionId, iItemIndex, i, 16);
		if(lMesSeq > AppGlobal.getActiveClient().getLastAlertMessageReadSeqNum()) {
			for (int i = 0 ; i < 4 ; i++)
				m_oAlertMessageList.setFieldForegroundColor(iSectionId, iItemIndex, i,ClsAlertMessage.UNREAD_MESSAGE_COLOR);
		}
		
	}
	
	public void setStationInfo(String[] sStationId, String sPriceLevel, String[] sMenuPeriod, String[] sSpecialPeriod, String[] sHoliday, String[] sSpecialDay) {
		m_oLabelStationName.setValue(sStationId);
		m_oLabelPriceLevel.setValue(sPriceLevel);
		m_oLabelMenuPeriod.setValue(sMenuPeriod);
		m_oLabelSpecialPeriod.setValue(sSpecialPeriod);
		m_oLabelHoliday.setValue(sHoliday);
		m_oLabelSpecialDay.setValue(sSpecialDay);
	}
	
	public void resetStationInfoHeader() {
		m_oLabelStationNameHeader.setValue(AppGlobal.g_oLang.get()._("station_id", ""));
		m_oLabelPriceLevelHeader.setValue(AppGlobal.g_oLang.get()._("price_level", ""));
		m_oLabelMenuPeriodHeader.setValue(AppGlobal.g_oLang.get()._("menu_period", ""));
		m_oLabelSpecialPeriodHeader.setValue(AppGlobal.g_oLang.get()._("special_menu", ""));
		m_oLabelHolidayHeader.setValue(AppGlobal.g_oLang.get()._("holiday", ""));
		m_oLabelSpecialDayHeader.setValue(AppGlobal.g_oLang.get()._("special_day", ""));
	}
	
	public void setUIForAlertMessage() {
		if (!this.m_bShowAlertMessageUI) {
			this.m_bShowAlertMessageUI = true;
			
			//store original frame size
			m_iOriginalFrameLeft = this.getLeft();
			m_iOriginalFrameWidth = this.getWidth();
			m_iOriginalFrameHeight = this.getHeight();
			
			//resize whole frame 
			this.setLeft(m_oLblNewFramePositionForAlertMessage.getLeft());
			this.setWidth(m_oLblNewFramePositionForAlertMessage.getWidth());
			this.setHeight(m_oLblNewFramePositionForAlertMessage.getHeight());
			
			m_oImageClose.setLeft(m_oImageClose.getLeft()+420);
			// show header
			m_oLabelStationInfo.setVisible(true);
			m_oUnderLineFrame.setVisible(true);
			m_oLabelAlertMessageInfo.setVisible(true);
			m_oUnderLineFrameForAlertMessage.setVisible(true);
			// resize station info 
			m_oLabelStationNameHeader.setTop(m_oLabelStationNameHeader.getTop()+48);
			m_oLabelStationName.setTop(m_oLabelStationName.getTop()+48);
			m_oLabelPriceLevelHeader.setTop(m_oLabelPriceLevelHeader.getTop()+48);
			m_oLabelPriceLevel.setTop(m_oLabelPriceLevel.getTop()+48);
			m_oLabelMenuPeriodHeader.setTop(m_oLabelMenuPeriodHeader.getTop()+48);
			m_oLabelMenuPeriod.setTop(m_oLabelMenuPeriod.getTop()+48);
			m_oLabelSpecialPeriodHeader.setTop(m_oLabelSpecialPeriodHeader.getTop()+48);
			m_oLabelSpecialPeriod.setTop(m_oLabelSpecialPeriod.getTop()+48);
			m_oLabelHolidayHeader.setTop(m_oLabelHolidayHeader.getTop()+48);
			m_oLabelHoliday.setTop(m_oLabelHoliday.getTop()+48);
			m_oLabelSpecialDayHeader.setTop(m_oLabelSpecialDayHeader.getTop()+48);
			m_oLabelSpecialDay.setTop(m_oLabelSpecialDay.getTop()+48);
		
			if (m_oAlertMessageList != null)
				m_oAlertMessageList.setVisible(true);
		}	
	}
	
	public void recoverToOriginalUI() {
		if (this.m_bShowAlertMessageUI) {
			this.m_bShowAlertMessageUI = false;
			//resize whole frame 
			this.setLeft(m_iOriginalFrameLeft);
			this.setWidth(m_iOriginalFrameWidth);
			this.setHeight(m_iOriginalFrameHeight);
			
			m_oImageClose.setLeft(m_oImageClose.getLeft() - 420);
			// show header
			m_oLabelAlertMessageInfo.setVisible(false);
			m_oUnderLineFrameForAlertMessage.setVisible(false);
			m_oLabelStationInfo.setVisible(false);
			m_oUnderLineFrame.setVisible(false);
			// resize station info 
			m_oLabelStationNameHeader.setTop(m_oLabelStationNameHeader.getTop() - 48);
			m_oLabelStationName.setTop(m_oLabelStationName.getTop() - 48);
			m_oLabelPriceLevelHeader.setTop(m_oLabelPriceLevelHeader.getTop() - 48);
			m_oLabelPriceLevel.setTop(m_oLabelPriceLevel.getTop() - 48);
			m_oLabelMenuPeriodHeader.setTop(m_oLabelMenuPeriodHeader.getTop() - 48);
			m_oLabelMenuPeriod.setTop(m_oLabelMenuPeriod.getTop() - 48);
			m_oLabelSpecialPeriodHeader.setTop(m_oLabelSpecialPeriodHeader.getTop() - 48);
			m_oLabelSpecialPeriod.setTop(m_oLabelSpecialPeriod.getTop() - 48);
			m_oLabelHolidayHeader.setTop(m_oLabelHolidayHeader.getTop() - 48);
			m_oLabelHoliday.setTop(m_oLabelHoliday.getTop() - 48);
			m_oLabelSpecialDayHeader.setTop(m_oLabelSpecialDayHeader.getTop() - 48);
			m_oLabelSpecialDay.setTop(m_oLabelSpecialDay.getTop() - 48);
			
			m_oFramePage.setVisible(false);
			this.showPageUp(false);
			this.showPageDown(false);
			//remove message list content 
			if (m_oAlertMessageList != null)
				m_oAlertMessageList.setVisible(false);
			m_oAlertMessageList.removeAllItems(0);
			
		}
	}
	
	public void showPageUp(boolean bShow) {
		if(bShow)
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
		else
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
		m_oImgButtonPrevPage.setEnabled(bShow);
	}
	
	public void showPageDown(boolean bShow) {
		if(bShow)
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
		else
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
		m_oImgButtonNextPage.setEnabled(bShow);
	}
	
	public HashMap<Long , ClsAlertMessage> getMessageListFromStationInfo() {
		return this.m_oMessageList;
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		// Find the clicked button
		if (m_oImageClose.getId() == iChildId) {
			this.setVisible(false);
			recoverToOriginalUI();
			bMatchChild = true;
		}else if (m_oImgButtonNextPage.getId() == iChildId) {
			if(m_iCurrentPage < m_TotalPage) {
				m_iCurrentPage++;
				updateItemList();
			}
			bMatchChild = true;
		}else if (m_oImgButtonPrevPage.getId() == iChildId) {
			if(m_iCurrentPage > 1) {
				m_iCurrentPage--;
				updateItemList();
			}
			bMatchChild = true;
		}
		
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
	
}
