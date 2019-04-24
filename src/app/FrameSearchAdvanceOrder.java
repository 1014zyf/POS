package app;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import commonui.FormConfirmBox;
import commonui.FormDatePicker;
import commonui.FormDialogBox;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import om.PosCheckExtraInfo;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FrameSearchAdvanceOrderListener {
	void frameSearchAdvanceOrder_clickClose();
	void frameSearchAdvanceOrder_clickReset();
	void frameSearchAdvanceOrder_clickRetreive(JSONObject oSearchInfo);
	void frameSearchAdvanceOrder_clickRecord(String sReferenceNo);
	void frameSearchAdvanceOrder_clickNextPage();
	void frameSearchAdvanceOrder_clickPrevPage();
}

//public class FrameSearchAdvanceOrder extends VirtualUIFrame implements FrameCommonBasketListener {
public class FrameSearchAdvanceOrder extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;

	//private VirtualUIFrame m_oFrameTitleBar;
	//private VirtualUILabel m_oLabelTitle;
	//private VirtualUIFrame m_oFraButtonClose;
	private FrameTitleHeader m_oTitleHeader;
	private VirtualUIFrame m_oFrameSearchInput;
	private VirtualUILabel m_oLabelAdvanceOrderDateTitle;
	private VirtualUILabel m_oLabelAdvanceOrderDate;
	private VirtualUILabel m_oLabelAdvanceOrderNameTitle;
	private VirtualUITextbox m_oTextboxAdvanceOrderName;
	private VirtualUILabel m_oLabelAdvanceOrderPhoneTitle;
	private VirtualUITextbox m_oTextboxAdvanceOrderPhone;
	private VirtualUILabel m_oLabelDateRangeTitle;
	private VirtualUILabel m_oLabelDateRangeStartDate;
	private VirtualUILabel m_oLabelDateRangeToTitle;
	private VirtualUILabel m_oLabelDateRangeEndDate;
	private VirtualUILabel m_oLabelDepositDetail;
	//private VirtualUIFrame m_oFrameDepositDetailUnderLineTop;
	//private VirtualUIFrame m_oFrameDepositDetailUnderLineBottom;
	private VirtualUIFrame m_oFramePage;
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIImage m_oImgButtonNextPage;
	
	private VirtualUIButton m_oButtonSearch;
	private VirtualUIButton m_oButtonReset;
	
	private FrameCommonBasket m_oAdvanceOrderListCommonBasket;
	
	private String m_sAdvanceOrderDate;
	
	private DateTimeFormatter m_oFormatter;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameSearchAdvanceOrderListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameSearchAdvanceOrderListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameSearchAdvanceOrderListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameSearchAdvanceOrder(String sTitle) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameSearchAdvanceOrderListener>();
		
		DateTime oDateTime = AppGlobal.getCurrentTime(false);
		m_oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		m_sAdvanceOrderDate = m_oFormatter.print(oDateTime);
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraSearchAdvanceOrder.xml");
		
		// Title Header
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		m_oTitleHeader.setTitle(sTitle);
		m_oTitleHeader.addListener(this);
		this.attachChild(m_oTitleHeader);
		
		// Title bar
		/*m_oFrameTitleBar = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleBar, "fraTitleBar");
		
		// Title
		m_oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTitle, "lblTitle");
		m_oLabelTitle.setValue(sTitle);
		m_oFrameTitleBar.attachChild(m_oLabelTitle);
		
		// Close button
		m_oFraButtonClose = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFraButtonClose, "fraButClose");
		VirtualUIImage oImage = new VirtualUIImage();
		oImage.setWidth(m_oFraButtonClose.getWidth());
		oImage.setHeight(m_oFraButtonClose.getHeight());
		oImage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/close_icon.png");
		m_oFraButtonClose.attachChild(oImage);
		
		m_oFraButtonClose.setClickServerRequestNote("butClose");
		m_oFraButtonClose.allowClick(true);
		m_oFraButtonClose.setClickServerRequestBlockUI(false);
		m_oFrameTitleBar.attachChild(m_oFraButtonClose);
		
		this.attachChild(m_oFrameTitleBar);*/
		
		// Search Input Frame
		m_oFrameSearchInput = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameSearchInput, "fraSearchInput");
		this.attachChild(m_oFrameSearchInput);
		
		// AdvanceOrder Date Title
		m_oLabelAdvanceOrderDateTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAdvanceOrderDateTitle, "lblAdvanceOrderDateTitle");
		m_oLabelAdvanceOrderDateTitle.setValue(AppGlobal.g_oLang.get()._("pickup_date"));
		m_oFrameSearchInput.attachChild(m_oLabelAdvanceOrderDateTitle);
		//this.attachChild(m_oLabelAdvanceOrderDateTitle);
		
		// AdvanceOrder Date
		m_oLabelAdvanceOrderDate = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAdvanceOrderDate, "lblAdvanceOrderDate");
		m_oLabelAdvanceOrderDate.allowClick(true);
		m_oLabelAdvanceOrderDate.setClickServerRequestBlockUI(false);
		//m_oLabelAdvanceOrderDate.setValue(m_sAdvanceOrderDate);
		m_oFrameSearchInput.attachChild(m_oLabelAdvanceOrderDate);
		//this.attachChild(m_oLabelAdvanceOrderDate);
		
		// AdvanceOrder Name Title
		m_oLabelAdvanceOrderNameTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAdvanceOrderNameTitle, "lblAdvanceOrderNameTitle");
		m_oLabelAdvanceOrderNameTitle.setValue(AppGlobal.g_oLang.get()._("name"));
		m_oFrameSearchInput.attachChild(m_oLabelAdvanceOrderNameTitle);
		//this.attachChild(m_oLabelAdvanceOrderNameTitle);
		
		// AdvanceOrder Name
		m_oTextboxAdvanceOrderName = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxAdvanceOrderName, "txtboxAdvanceOrderName");
		m_oTextboxAdvanceOrderName.setFocusWhenShow(true);
		m_oFrameSearchInput.attachChild(m_oTextboxAdvanceOrderName);
		//this.attachChild(m_oTextboxAdvanceOrderName);
		
		// AdvanceOrder Phone Title
		m_oLabelAdvanceOrderPhoneTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAdvanceOrderPhoneTitle, "lblAdvanceOrderPhoneTitle");
		m_oLabelAdvanceOrderPhoneTitle.setValue(AppGlobal.g_oLang.get()._("phone_number"));
		m_oFrameSearchInput.attachChild(m_oLabelAdvanceOrderPhoneTitle);
		//this.attachChild(m_oLabelAdvanceOrderPhoneTitle);

		// AdvanceOrder Phone
		m_oTextboxAdvanceOrderPhone = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxAdvanceOrderPhone, "txtboxAdvanceOrderPhone");
		//m_oTextboxAdvanceOrderPhone.setFocusWhenShow(true);
		m_oFrameSearchInput.attachChild(m_oTextboxAdvanceOrderPhone);
		//this.attachChild(m_oTextboxAdvanceOrderPhone);
		
		// Date Range From Date Title
		m_oLabelDateRangeTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDateRangeTitle, "lblAdvanceOrderDateRangeFromTitle");
		m_oLabelDateRangeTitle.setValue(AppGlobal.g_oLang.get()._("create_date"));
		m_oFrameSearchInput.attachChild(m_oLabelDateRangeTitle);
		//this.attachChild(m_oLabelDateRangeTitle);
		
		//Date Range From Date
		m_oLabelDateRangeStartDate = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDateRangeStartDate, "lblAdvanceOrderFromDate");
		m_oLabelDateRangeStartDate.allowClick(true);
		m_oLabelDateRangeStartDate.setClickServerRequestBlockUI(false);
		m_oLabelDateRangeStartDate.setValue(m_oFormatter.print(m_oFormatter.parseDateTime(m_sAdvanceOrderDate).minusMonths(3)));
		m_oFrameSearchInput.attachChild(m_oLabelDateRangeStartDate);
		//this.attachChild(m_oLabelDateRangeStartDate);
		
		//Date Range To Date Title
		m_oLabelDateRangeToTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDateRangeToTitle, "lblAdvanceOrderDateRangeToTitle");
		m_oLabelDateRangeToTitle.setValue(AppGlobal.g_oLang.get()._("to"));
		m_oFrameSearchInput.attachChild(m_oLabelDateRangeToTitle);
		//this.attachChild(m_oLabelDateRangeToTitle);
		
		//Date Range To Date
		m_oLabelDateRangeEndDate = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDateRangeEndDate, "lblAdvanceOrderToDate");
		m_oLabelDateRangeEndDate.allowClick(true);
		m_oLabelDateRangeEndDate.setClickServerRequestBlockUI(false);
		m_oLabelDateRangeEndDate.setFocusWhenShow(true);
		m_oLabelDateRangeEndDate.setValue(m_oFormatter.print(m_oFormatter.parseDateTime(m_sAdvanceOrderDate)));
		m_oFrameSearchInput.attachChild(m_oLabelDateRangeEndDate);
		//this.attachChild(m_oLabelDateRangeEndDate);
		
		// Create OK button
		m_oButtonSearch = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSearch, "btnSearch");
		m_oButtonSearch.setValue(AppGlobal.g_oLang.get()._("search"));
		m_oButtonSearch.addClickServerRequestSubmitElement(this);
		m_oButtonSearch.setClickServerRequestBlockUI(true);
		this.attachChild(m_oButtonSearch);
		
		// Create Reset Button
		m_oButtonReset = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonReset, "btnReset");
		m_oButtonReset.setValue(AppGlobal.g_oLang.get()._("reset"));
		this.attachChild(m_oButtonReset);
		
		// Deposit Detail Title
		m_oLabelDepositDetail = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDepositDetail, "lblDepositDetail");
		m_oLabelDepositDetail.setValue(AppGlobal.g_oLang.get()._("deposit_details", ""));
		this.attachChild(m_oLabelDepositDetail);
		/*m_oFrameDepositDetailUnderLineTop = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameDepositDetailUnderLineTop, "lblDepositDetailUnderLineTop");
		this.attachChild(m_oFrameDepositDetailUnderLineTop);
		m_oFrameDepositDetailUnderLineBottom = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameDepositDetailUnderLineBottom, "lblDepositDetailUnderLineBottom");
		this.attachChild(m_oFrameDepositDetailUnderLineBottom);*/
		
		//Check Balance common basket
		m_oAdvanceOrderListCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oAdvanceOrderListCommonBasket, "fraAdvanceOrderListCommonBasket");
		m_oAdvanceOrderListCommonBasket.init();
		m_oAdvanceOrderListCommonBasket.addListener(this);
		this.attachChild(m_oAdvanceOrderListCommonBasket);
		
		//Add the check list title
		ArrayList<Integer> iFieldWidth = new ArrayList<Integer>();
		ArrayList<String[]> sFieldValue = new ArrayList<String[]>();
		
		//Add Advance Order Result Common Basket Header
		iFieldWidth.add(235);
		sFieldValue.add(AppGlobal.g_oLang.get()._("reference_no", ""));
		iFieldWidth.add(186);
		sFieldValue.add(AppGlobal.g_oLang.get()._("pickup_date", ""));
		iFieldWidth.add(196);
		sFieldValue.add(AppGlobal.g_oLang.get()._("name", ""));
		iFieldWidth.add(191);
		sFieldValue.add(AppGlobal.g_oLang.get()._("mobile", ""));
		iFieldWidth.add(187);
		sFieldValue.add(AppGlobal.g_oLang.get()._("deposit", ""));
		
		m_oAdvanceOrderListCommonBasket.addHeader(iFieldWidth, sFieldValue);
		for(int i = 0;i<sFieldValue.size();i++)
			m_oAdvanceOrderListCommonBasket.setHeaderTextAlign(i, HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.LEFT);
		m_oAdvanceOrderListCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
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
		//m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_prev_page_button.png");
		m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
		m_oImgButtonPrevPage.setVisible(false);
		this.attachChild(m_oImgButtonPrevPage);
		
		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.allowLongClick(true);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		//m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_next_page_button.png");
		m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
		m_oImgButtonNextPage.setVisible(false);
		this.attachChild(m_oImgButtonNextPage);

		/*VirtualUIImage oImagePage = new VirtualUIImage();
		oImagePage.setWidth(m_oFramePage.getWidth());
		oImagePage.setHeight(m_oFramePage.getHeight());
		oImagePage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_page_bg.png");
		m_oFramePage.attachChild(oImagePage);*/
		
		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oLblPage.setWidth(m_oFramePage.getWidth());
		m_oLblPage.setHeight(m_oFramePage.getHeight());
		m_oFramePage.setVisible(false);
		m_oFramePage.attachChild(m_oLblPage);
		this.attachChild(m_oFramePage);
	}

	public void showDefaultRecords(){
		// Default display advance order records within 3 months
		DateTime oCreateDateTime = m_oFormatter.parseDateTime(m_sAdvanceOrderDate).minusMonths(3);
		DateTime oToDateTime = m_oFormatter.parseDateTime(m_sAdvanceOrderDate);
		
		// Create Searching Info JSON
		JSONObject oSearchInfoJSON = new JSONObject();
		JSONObject oVariableJSON = new JSONObject();
		try {
			oVariableJSON.put("start_date", m_oFormatter.print(oCreateDateTime));
			oVariableJSON.put("end_date",  m_oFormatter.print(oToDateTime));
			oSearchInfoJSON.put("variables", oVariableJSON);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getAdvanceOrderRecords(oSearchInfoJSON);
	}
	
	public void addDepositRecord(String sReference, String sPickupDate, String sName, String sMobile, String sDeposit) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		ArrayList<String> sFieldTypes = new ArrayList<String>();

		iFieldWidths.add(235);
		sFieldValues.add(sReference);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.LEFT);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		
		iFieldWidths.add(186);
		sFieldValues.add(sPickupDate);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.LEFT);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);

		iFieldWidths.add(196);
		sFieldValues.add(sName);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.LEFT);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		
		iFieldWidths.add(191);
		sFieldValues.add(sMobile);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.LEFT);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		
		int iRounding = AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal();
		BigDecimal oDeposit = new BigDecimal(sDeposit);
		iFieldWidths.add(187);
		sFieldValues.add(StringLib.BigDecimalToString(oDeposit, iRounding));
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		m_oAdvanceOrderListCommonBasket.addItem(0, 0, 40, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
	}
	
	public void clearSearchField(){
		m_oLabelAdvanceOrderDate.setValue("");
		m_oTextboxAdvanceOrderName.setValue("");
		m_oTextboxAdvanceOrderPhone.setValue("");
		m_oLabelDateRangeEndDate.setValue(m_oFormatter.print(m_oFormatter.parseDateTime(m_sAdvanceOrderDate)));
		m_oLabelDateRangeStartDate.setValue(m_oFormatter.print(m_oFormatter.parseDateTime(m_sAdvanceOrderDate).minusMonths(3)));
	}
	
	public void clearRecords() {
		m_oAdvanceOrderListCommonBasket.clearAllSections();
	}
	
	private void getAdvanceOrderRecords(JSONObject oSearchInfoJSON){
		for (FrameSearchAdvanceOrderListener listener : listeners) {
			// Raise the event to parent
			listener.frameSearchAdvanceOrder_clickRetreive(oSearchInfoJSON);
		}
	}
	
	private boolean dateRangeValidation(DateTime oStartDate, DateTime oEndDate){
		DateTime oMaxEndDate = oStartDate.plusMonths(3);
		if (oEndDate.isAfter(oMaxEndDate)) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), super.getParentForm());
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("date_range_must_be_within_3_months"));
			oFormDialogBox.show();
			this.finishShow();
			return false;
		}
		return true;
	}
	
	public void updatePageButton(int iCurrentPage, int iTotalPage) {
		m_oLblPage.setValue(iCurrentPage + "/" + iTotalPage);
		
		if (iTotalPage > 1) {
			m_oFramePage.setVisible(true);
			m_oImgButtonPrevPage.setVisible(true);
			m_oImgButtonNextPage.setVisible(true);
			if (iCurrentPage > 1) {
				m_oImgButtonPrevPage.setEnabled(true);
				m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
				
			}else {
				m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
				m_oImgButtonPrevPage.setEnabled(false);
			}
			
			if (iCurrentPage < iTotalPage) {
				m_oImgButtonNextPage.setEnabled(true);
				m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
			}else {
				m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
				m_oImgButtonNextPage.setEnabled(false);
			}
		} else {
			m_oFramePage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
			m_oImgButtonNextPage.setVisible(false);
		}
	}
	
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (iChildId == m_oLabelAdvanceOrderDate.getId()) {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			DateTime oDateTime = formatter.parseDateTime(m_sAdvanceOrderDate);
			
			FormDatePicker oFormDatePicker = new FormDatePicker(oDateTime, this.getParentForm());
			oFormDatePicker.show();
			
			if(oFormDatePicker.getDate() != null) {
				m_sAdvanceOrderDate = oFormDatePicker.getDate();
				m_oLabelAdvanceOrderDate.setValue(m_sAdvanceOrderDate);
				
				m_oLabelDateRangeStartDate.setValue("");
				m_oLabelDateRangeEndDate.setValue("");
			}
			bMatchChild = true;
		} else if (iChildId == m_oLabelDateRangeStartDate.getId()) {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			DateTime oDateTime = formatter.parseDateTime(m_oLabelDateRangeStartDate.getValue());
			
			FormDatePicker oFormDatePicker = new FormDatePicker(oDateTime, this.getParentForm());
			oFormDatePicker.show();
			
			if(oFormDatePicker.getDate() != null) {
				String sStartDate = oFormDatePicker.getDate();
				DateTime oStartDateTime = formatter.parseDateTime(sStartDate);
				DateTime oEndDateTime = formatter.parseDateTime(m_oLabelDateRangeEndDate.getValue());
				
				if (oStartDateTime.isAfter(oEndDateTime)) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), super.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("start_date_must_be_before_end_date"));
					oFormDialogBox.show();
					this.finishShow();
					return false;
				}
				
				if(!dateRangeValidation(oStartDateTime, oEndDateTime))
					return false;
				
				m_oLabelDateRangeStartDate.setValue(sStartDate);
			}
			bMatchChild = true;
		} else if (iChildId == m_oLabelDateRangeEndDate.getId()) {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			DateTime oDateTime = formatter.parseDateTime(m_oLabelDateRangeEndDate.getValue());
			
			FormDatePicker oFormDatePicker = new FormDatePicker(oDateTime, this.getParentForm());
			oFormDatePicker.show();
			
			if (oFormDatePicker.getDate() != null) {
				String sEndDate = oFormDatePicker.getDate();
				DateTime oEndDateTime = formatter.parseDateTime(sEndDate);
				DateTime oStartDateTime = formatter.parseDateTime(m_oLabelDateRangeStartDate.getValue());
				
				if (oEndDateTime.isBefore(oStartDateTime)) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), super.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("end_date_must_be_after_start_date"));
					oFormDialogBox.show();
					this.finishShow();
					return false;
				}
				
				if(!dateRangeValidation(oStartDateTime, oEndDateTime))
					return false;
				
				m_oLabelDateRangeEndDate.setValue(sEndDate);
			}
			bMatchChild = true;
		/*} else if(iChildId == m_oFraButtonClose.getId()){
			for (FrameSearchAdvanceOrderListener listener : listeners) {
				listener.frameSearchAdvanceOrder_clickClose();
			}
			bMatchChild = true;
			*/
		} else if (iChildId == m_oButtonReset.getId()) {
			for (FrameSearchAdvanceOrderListener listener : listeners) {
				listener.frameSearchAdvanceOrder_clickReset();
			}
			bMatchChild = true;
		} else if (iChildId == m_oButtonSearch.getId()) {
			if (m_oLabelAdvanceOrderDate.getValue().isEmpty() && m_oTextboxAdvanceOrderName.getValue().isEmpty()
					&& m_oTextboxAdvanceOrderPhone.getValue().isEmpty()
					&& m_oLabelDateRangeStartDate.getValue().isEmpty()
					&& m_oLabelDateRangeEndDate.getValue().isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), super.getParentForm());
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("please_fill_in_enquiry_info"));
				oFormDialogBox.show();
				this.finishShow();
				return false;
			}
			
			// Date Validation
			if (!m_oLabelDateRangeStartDate.getValue().isEmpty() && !m_oLabelDateRangeEndDate.getValue().isEmpty()) {
				String sStartDate = m_oLabelDateRangeStartDate.getValue();
				String sEndDate = m_oLabelDateRangeEndDate.getValue();
				
				DateTime oEndDate = AppGlobal.getCurrentTime(false);
				oEndDate =  m_oFormatter.parseDateTime(sEndDate);
				
				DateTime oMaxEndDate = m_oFormatter.parseDateTime(sStartDate).plusMonths(3);
				if (oEndDate.isAfter(oMaxEndDate)) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), super.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("date_range_must_be_within_3_months"));
					oFormDialogBox.show();
					this.finishShow();
					return false;
				}
			}
			
			// Create Searching Info JSON
			JSONObject oSearchInfoJSON = new JSONObject();
			JSONObject oVariableJSON = new JSONObject();
			try {
				if(!m_oLabelAdvanceOrderDate.getValue().isEmpty())
					oVariableJSON.put(PosCheckExtraInfo.VARIABLE_PICKUP_DATE, m_oLabelAdvanceOrderDate.getValue());
				if(!m_oTextboxAdvanceOrderName.getValue().isEmpty())
					oVariableJSON.put(PosCheckExtraInfo.VARIABLE_GUEST_NAME, m_oTextboxAdvanceOrderName.getValue());
				if(!m_oTextboxAdvanceOrderPhone.getValue().isEmpty())
					oVariableJSON.put(PosCheckExtraInfo.VARIABLE_PHONE, m_oTextboxAdvanceOrderPhone.getValue());
				if(!m_oLabelDateRangeStartDate.getValue().isEmpty())
					oVariableJSON.put("start_date", m_oLabelDateRangeStartDate.getValue());
				else
					oVariableJSON.put("start_date", m_oFormatter.print(m_oFormatter.parseDateTime(m_sAdvanceOrderDate).minusMonths(3)));
				if(!m_oLabelDateRangeEndDate.getValue().isEmpty())
					oVariableJSON.put("end_date", m_oLabelDateRangeEndDate.getValue());
				else
					oVariableJSON.put("end_date", m_oFormatter.print(m_oFormatter.parseDateTime(m_sAdvanceOrderDate)));
				oSearchInfoJSON.put("variables", oVariableJSON);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			getAdvanceOrderRecords(oSearchInfoJSON);
			
			bMatchChild = true;
		} else if (iChildId == m_oImgButtonPrevPage.getId()) {
			for (FrameSearchAdvanceOrderListener listener : listeners) {
				// Raise the event to parent
				listener.frameSearchAdvanceOrder_clickPrevPage();
			}
			bMatchChild = true;
		} else if (iChildId == m_oImgButtonNextPage.getId()) {
			for (FrameSearchAdvanceOrderListener listener : listeners) {
				// Raise the event to parent
				listener.frameSearchAdvanceOrder_clickNextPage();
			}
			bMatchChild = true;
		}
		return bMatchChild;
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex,
			String sNote) {
		if(m_oAdvanceOrderListCommonBasket != null && iBasketId == m_oAdvanceOrderListCommonBasket.getId()) {
			for (int i = 0; i < m_oAdvanceOrderListCommonBasket.getItemCount(0); i++) {
				if (i == iItemIndex)
					setCommonBasketBackgroudColor(i, "#FFFF00");
				else
					setCommonBasketBackgroudColor(i, "#FFFFFF");
			}
		}
		
		FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this.getParentForm());
		oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("confirmation"));
		oFormConfirmBox.setMessage( AppGlobal.g_oLang.get()._("confirm_to_retrieve_order")+" ?");
		oFormConfirmBox.show();
		
		if (oFormConfirmBox.isOKClicked() == false)
			return;
		
		for (FrameSearchAdvanceOrderListener listener : listeners) {
			listener.frameSearchAdvanceOrder_clickRecord(m_oAdvanceOrderListCommonBasket.getFieldValue(0, iItemIndex, 0));
		}
		
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId, int iSectionIndex, int iItemIndex,
			int iFieldIndex, String sNote) {
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}
	
	public void setCommonBasketBackgroudColor(int iRecordIndex, String sBackgroundColor) {
		for (int i = 0; i < 5; i++)
			m_oAdvanceOrderListCommonBasket.setFieldBackgroundColor(0, iRecordIndex, i, sBackgroundColor);
	}
	
	@Override
	public void FrameTitleHeader_close() {
		// TODO Auto-generated method stub
		for(FrameSearchAdvanceOrderListener listener : listeners)
			listener.frameSearchAdvanceOrder_clickClose();
	}
}
