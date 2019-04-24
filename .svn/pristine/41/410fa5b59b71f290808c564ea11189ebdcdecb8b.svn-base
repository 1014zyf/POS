package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import commonui.FormDatePicker;
import commonui.FormDialogBox;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import om.InfVendor;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FramePMSEnquiryResultListener {
	void framePMSEnquiryResult_clickBack();
	void framePMSEnquiryResult_clickGuestRecord(int iGuestIndex, boolean bFromGuestList);
	void framePMSSetRoom();
	boolean framePMSClearRoom();
	void framePMSEnquiryResult_clickGuestForPosting(int iGuestIndex, HashMap<String, String> oInputtedEnquiryInfo);
	void framePMSEnquiryResult_clickSubGuestForPosting(int iSubGuestIndex, HashMap<String, String> oInputtedEnquiryInfo);
	void framePMSEnquiryResult_clickEnquiry(HashMap<String, String> oEnquiryInfo, boolean bPostingAskInfo);
	String freamPMSEnquiryResult_getCurrentChosenGuestInformation(String sInfoName);
}

public class FramePMSEnquiryResult extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oTitleHeader;
	
	private VirtualUITextbox m_oTxtboxEnquiryNumber;
	private VirtualUIButton m_oButtonRoomEnquiry;
	private VirtualUIButton m_oButtonAREnquiry;
	private VirtualUIButton m_oButtonRegisterEnquiry;
	private VirtualUIButton m_oButtonPackageEnquiry;
	private VirtualUIButton m_oButtonGetExtendDetails;
	private VirtualUIButton m_oButtonSubAREnquiry;
	private VirtualUIButton m_oButtonGroupEnquiry;
	private VirtualUIFrame  m_oFrameSearchInfo;
	private VirtualUIFrame m_oFrameGuestDetailBackground;
	private FrameCommonBasket m_oBasketGuestList;
	private FrameCommonBasket m_oBasketSubGuestList;
	private FramePMSGuestDetail m_oFrameGuestDetail;
	private VirtualUITextbox m_oTextboxGivenName;
	private VirtualUITextbox m_oTextboxSurname;
	private VirtualUITextbox m_oTextboxAccountNumber;
	private VirtualUILabel m_olblArrivalDate;
	private VirtualUILabel m_olblDepartureDate;
	private VirtualUIImage m_oImgGuestImage;
	private VirtualUIImage m_oImgSignImage;
	private VirtualUIButton m_oButtonSetRoom;
	private VirtualUIButton m_oButtonClearRoom;
	
	private int m_iBasketRowHeight;
	private String m_sInterfaceVendorKey;
	private JSONObject m_oInterfaceSetup;
	private String m_sEnquiryType;
	private String m_sPostingType;
	private boolean m_bGuestExtendDetails;
	private boolean m_bPostingAskInfo;
	private boolean m_bShowAREnquiryButton;
	private boolean m_bShowRegisterEnquiryButton;
	private boolean m_bShowPackageEnquiryButton;
	private boolean m_bShowGetExtendDetailButton;
	private int m_iRequestTimeout;
	
	private static final String COLOR_BACKGROUND_SELECTED = "#0055B8";
	private static final String COLOR_FOREGROUND_SELECTED = "#FFFFFF";
	private static final String COLOR_BACKGROUND_UNSELECTED = "#E0E0E0";
	private static final String COLOR_FOREGROUND_UNSELECTED = "#015384";
	
	private static final String PHOTO_TYPE_GUEST = "guest";
	private static final String PHOTO_TYPE_SIGN = "sign";
	
	private static final String BUTTON_ROOM_ENQUIRY = "room";
	private static final String BUTTON_AR_ENQUIRY = "ar";
	private static final String BUTTON_SUB_AR_ENQUIRY = "sub_ar";
	private static final String BUTTON_GROUP_ENQUIRY = "group";
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FramePMSEnquiryResultListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FramePMSEnquiryResultListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FramePMSEnquiryResultListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FramePMSEnquiryResult() {
		listeners = new ArrayList<FramePMSEnquiryResultListener>();
		
		m_sInterfaceVendorKey = "";
		m_bGuestExtendDetails = false;
		m_bPostingAskInfo = false;
		m_bShowAREnquiryButton = false;
		m_bShowRegisterEnquiryButton = false;
		m_bShowPackageEnquiryButton = false;
		m_iBasketRowHeight = 0;
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_iBasketRowHeight = 50;
		m_oInterfaceSetup = null;
	}
	
	public void init(String sInterfaceKey, JSONObject oInterfaceSetup, boolean bShowAREnquiryButton, boolean bShowRegisterEnquiryButton, boolean bShowPackageEnquiryButton, boolean bShowGetExtendDetailButton, boolean bPostingAskInfo, int iRequestTimeout, String sRoomNo, boolean bShowSetRoom) {
		m_sInterfaceVendorKey = sInterfaceKey;
		m_bPostingAskInfo = bPostingAskInfo;
		m_bShowAREnquiryButton = bShowAREnquiryButton;
		m_bShowRegisterEnquiryButton = bShowRegisterEnquiryButton;
		m_bShowPackageEnquiryButton = bShowPackageEnquiryButton;
		m_bShowGetExtendDetailButton = bShowGetExtendDetailButton;
		m_iRequestTimeout = iRequestTimeout;
		m_oInterfaceSetup = oInterfaceSetup;
		
		m_oTemplateBuilder = new TemplateBuilder();
		if(sInterfaceKey.equals(InfVendor.KEY_ASPEN_PMS))
			m_oTemplateBuilder.loadTemplate("fraPMSEnquiryResult_aspen_pms_xml.xml");
		else
			m_oTemplateBuilder.loadTemplate("fraPMSEnquiryResult.xml");
		
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("pms_enquiry"));
		m_oTitleHeader.addListener(this);
		this.attachChild(m_oTitleHeader);
		
		//Create Info frame
		m_oFrameSearchInfo = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameSearchInfo, "fraInputInfo");
		this.attachChild(m_oFrameSearchInfo);
		
		// enquiry number textbox
		m_oTxtboxEnquiryNumber = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxEnquiryNumber, "lblInputBox");
		m_oTxtboxEnquiryNumber.setFocusWhenShow(true);
		if(sInterfaceKey.equals(InfVendor.KEY_ASPEN_PMS) || sInterfaceKey.equals(InfVendor.KEY_STANDARD_TCPIP) || sInterfaceKey.equals(InfVendor.KEY_4700_TCPIP) )
			m_oTxtboxEnquiryNumber.setHint(AppGlobal.g_oLang.get()._("room_number"));
		m_oFrameSearchInfo.attachChild(m_oTxtboxEnquiryNumber);
		
		// room enquiry button
		m_oButtonRoomEnquiry = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonRoomEnquiry, "butRoomEnquiry");
		m_oButtonRoomEnquiry.addClickServerRequestSubmitElement(m_oTxtboxEnquiryNumber);
		m_oButtonRoomEnquiry.setValue(AppGlobal.g_oLang.get()._("room_enquiry"));
		if(m_iRequestTimeout > 0)	//use the default value if m_iRequestTimeout == 0
			m_oButtonRoomEnquiry.setClickServerRequestTimeout((m_iRequestTimeout + 10000));
		m_oButtonRoomEnquiry.setClickServerRequestBlockUI(true);
		m_oFrameSearchInfo.attachChild(m_oButtonRoomEnquiry);
		if(sInterfaceKey.equals(InfVendor.KEY_ASPEN_PMS)){
			// set room button
			m_oButtonSetRoom = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonSetRoom, "butSetRoom");
			m_oButtonSetRoom.setValue(AppGlobal.g_oLang.get()._("set_room"));
			m_oButtonSetRoom.setVisible(false);
			m_oFrameSearchInfo.attachChild(m_oButtonSetRoom);
			
			// clear room button
			m_oButtonClearRoom = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonClearRoom, "butClearRoom");
			m_oButtonClearRoom.setValue(AppGlobal.g_oLang.get()._("clear_room"));
			m_oButtonClearRoom.setVisible(false);
			m_oFrameSearchInfo.attachChild(m_oButtonClearRoom);
			
			if(!bShowSetRoom && !bPostingAskInfo)
				blockSetAndClearRoomButton();
			else if(!sRoomNo.isEmpty() && !bPostingAskInfo){
					m_oTxtboxEnquiryNumber.setValue(sRoomNo);
					showClearRoomButton();
			}else if(!bPostingAskInfo)
				showSetRoomButton();
			else
				m_oTxtboxEnquiryNumber.setValue(sRoomNo);
		}
		
		if(m_bShowAREnquiryButton) {
			// AR enquiry button
			m_oButtonAREnquiry = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonAREnquiry, "butAREnquiry");
			m_oButtonAREnquiry.addClickServerRequestSubmitElement(m_oTxtboxEnquiryNumber);
			m_oButtonAREnquiry.setValue(AppGlobal.g_oLang.get()._("ar_enquiry"));
			m_oFrameSearchInfo.attachChild(m_oButtonAREnquiry);
		}
		
		if(m_bShowRegisterEnquiryButton) {
			// Register enquiry button
			m_oButtonRegisterEnquiry = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonRegisterEnquiry, "butRegisterEnquiry");
			m_oButtonRegisterEnquiry.addClickServerRequestSubmitElement(m_oTxtboxEnquiryNumber);
			m_oButtonRegisterEnquiry.setValue(AppGlobal.g_oLang.get()._("register_enquiry"));
			m_oFrameSearchInfo.attachChild(m_oButtonRegisterEnquiry);
		}
		
		if(m_bShowPackageEnquiryButton) {
			// Register enquiry button
			m_oButtonPackageEnquiry = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonPackageEnquiry, "butPackageEnquiry");
			m_oButtonPackageEnquiry.addClickServerRequestSubmitElement(m_oTxtboxEnquiryNumber);
			m_oButtonPackageEnquiry.setValue(AppGlobal.g_oLang.get()._("package_enquiry"));
			m_oFrameSearchInfo.attachChild(m_oButtonPackageEnquiry);
		}
		
		if(m_bShowGetExtendDetailButton) {
			// Get extend detail button
			m_oButtonGetExtendDetails = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonGetExtendDetails, "butGetExtendDetail");
			m_oButtonGetExtendDetails.setValue(AppGlobal.g_oLang.get()._("get_extend_detail"));
			m_oFrameSearchInfo.attachChild(m_oButtonGetExtendDetails);
		}
		
		// guest list common basket
		m_oBasketGuestList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oBasketGuestList, "fraGuestList");
		m_oBasketGuestList.init();
		m_oBasketGuestList.addListener(this);
		
		m_oFrameGuestDetailBackground = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameGuestDetailBackground, "fraGuestListBackground");
		this.attachChild(m_oFrameGuestDetailBackground);
		m_oFrameGuestDetailBackground.attachChild(m_oBasketGuestList);
		
		// add header for guest list
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		iFieldWidths.add(100);
		sFieldValues.add(AppGlobal.g_oLang.get()._("number"));
		iFieldWidths.add(250);
		sFieldValues.add(AppGlobal.g_oLang.get()._("name"));
		String sTempString = AppGlobal.g_oLang.get()._("room_number");
		if(this.m_bPostingAskInfo && m_sPostingType.equals(FuncPMS.POSTING_TYPE_AR) && isSubGuestListSupported())
			sTempString = AppGlobal.g_oLang.get()._("sub_list");
		iFieldWidths.add(150);
		sFieldValues.add(sTempString);
		
		m_oBasketGuestList.addHeader(iFieldWidths, sFieldValues);
		m_oBasketGuestList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		m_oBasketGuestList.setHeaderFormat(0, 24, "");
		m_oBasketGuestList.setBottomUnderlineVisible(false);
		
		if(isSubGuestListSupported()) {
			// sub guest list common basket
			m_oBasketSubGuestList = new FrameCommonBasket();
			m_oTemplateBuilder.buildFrame(m_oBasketSubGuestList, "fraSubGuestList");
			m_oBasketSubGuestList.init();
			m_oBasketSubGuestList.addListener(this);
			m_oFrameGuestDetailBackground.attachChild(m_oBasketSubGuestList);
			
			// add header for sub guest list
			iFieldWidths = new ArrayList<Integer>();
			sFieldValues = new ArrayList<String>();
			iFieldWidths.add(100);
			sFieldValues.add(AppGlobal.g_oLang.get()._("number"));
			iFieldWidths.add(250);
			sFieldValues.add(AppGlobal.g_oLang.get()._("name"));
			m_oBasketSubGuestList.addHeader(iFieldWidths, sFieldValues);
			m_oBasketSubGuestList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
			
			if(m_bPostingAskInfo && !m_sPostingType.equals(FuncPMS.POSTING_TYPE_AR))
				m_oBasketSubGuestList.setVisible(false);
				
			m_oBasketGuestList.setHeight(m_oBasketGuestList.getHeight() - m_oBasketSubGuestList.getHeight());
		}
		
		if(!bPostingAskInfo) {
			// guest details
			m_oFrameGuestDetail = new FramePMSGuestDetail();
			m_oTemplateBuilder.buildFrame(m_oFrameGuestDetail, "fraGuestDetail");
			
			if(oInterfaceSetup.optJSONObject("connection_setup").optJSONObject("params").has("pms_type")
					&& oInterfaceSetup.optJSONObject("connection_setup").optJSONObject("params").optJSONObject("pms_type").optString("value").equals(FuncPMS.PMS_TYPE_SHIJI))
				m_oFrameGuestDetail.init(true);
			else 
				m_oFrameGuestDetail.init(false);
			
			this.attachChild(m_oFrameGuestDetail);
		}
		
		// for HTNG (iMagine) PMS only
		if(m_sInterfaceVendorKey.equals(InfVendor.KEY_HTNG)) {
			m_oTextboxGivenName = new VirtualUITextbox();
			m_oTemplateBuilder.buildTextbox(m_oTextboxGivenName, "txtGivenName");
			m_oTextboxGivenName.setHint(AppGlobal.g_oLang.get()._("given_name"));
			m_oFrameSearchInfo.attachChild(m_oTextboxGivenName);
			
			m_oTextboxSurname = new VirtualUITextbox();
			m_oTemplateBuilder.buildTextbox(m_oTextboxSurname, "txtSurname");
			m_oTextboxSurname.setHint(AppGlobal.g_oLang.get()._("surname"));
			m_oFrameSearchInfo.attachChild(m_oTextboxSurname);
			
			m_olblArrivalDate = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_olblArrivalDate, "lblArrivalDate");
			m_olblArrivalDate.setValue(AppGlobal.g_oLang.get()._("arrival_date"));
			m_olblArrivalDate.allowClick(true);
			
			m_olblDepartureDate = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_olblDepartureDate, "lblDepartDate");
			m_olblDepartureDate.setValue(AppGlobal.g_oLang.get()._("departure_date"));
			m_olblDepartureDate.allowClick(true);
			
			if(oInterfaceSetup.optJSONObject("connection_setup").optJSONObject("params").has("pms_type")){
				if(!oInterfaceSetup.optJSONObject("connection_setup").optJSONObject("params").optJSONObject("pms_type").optString("value").equals(FuncPMS.PMS_TYPE_SHIJI)){
					m_oFrameSearchInfo.attachChild(m_olblArrivalDate);
					m_oFrameSearchInfo.attachChild(m_olblDepartureDate);
					
					if(m_oButtonAREnquiry != null) {
						m_oButtonAREnquiry.addClickServerRequestSubmitElement(m_oTextboxGivenName);
						m_oButtonAREnquiry.addClickServerRequestSubmitElement(m_oTextboxSurname);
					}
				}
			}
			else {
				this.attachChild(m_olblArrivalDate);
				this.attachChild(m_olblDepartureDate);
			}
			
			m_oTxtboxEnquiryNumber.setHint(AppGlobal.g_oLang.get()._("room_number"));
			
			//add click event to room enquiry button
			m_oButtonRoomEnquiry.addClickServerRequestSubmitElement(m_oTextboxGivenName);
			m_oButtonRoomEnquiry.addClickServerRequestSubmitElement(m_oTextboxSurname);
			int iOffset = m_oTextboxGivenName.getHeight() + 16;
			if(this.getChilds().contains(m_olblArrivalDate) || m_oFrameSearchInfo.getChilds().contains(m_olblArrivalDate))
				iOffset += m_olblArrivalDate.getHeight() + 16;
			m_oFrameSearchInfo.setHeight(m_oFrameSearchInfo.getHeight() + iOffset);
			
			m_oFrameGuestDetailBackground.setTop(m_oFrameGuestDetailBackground.getTop() + iOffset);
			m_oBasketGuestList.setHeight(m_oBasketGuestList.getHeight() - iOffset);
			m_oFrameGuestDetailBackground.setHeight(m_oFrameGuestDetailBackground.getHeight() - iOffset);
			if(!bPostingAskInfo) {
				m_oFrameGuestDetail.setTop(m_oFrameGuestDetail.getTop() + iOffset);
				m_oFrameGuestDetail.setHeight(m_oFrameGuestDetail.getHeight() - iOffset);
				//set the height of basic info frame and user info frame equal
				m_oFrameGuestDetail.calibrateInfoFrameSize();
			}
		}
		
		// For XMS PMS only
		if(m_sInterfaceVendorKey.equals(InfVendor.KEY_XMS)) {
			m_oTextboxAccountNumber = new VirtualUITextbox();
			m_oTemplateBuilder.buildTextbox(m_oTextboxAccountNumber, "txtAccountNumber");
			m_oTextboxAccountNumber.setHint(AppGlobal.g_oLang.get()._("account_number"));
			m_oFrameSearchInfo.attachChild(m_oTextboxAccountNumber);
			
			m_oTextboxSurname = new VirtualUITextbox();
			m_oTemplateBuilder.buildTextbox(m_oTextboxSurname, "txtSurname");
			m_oTextboxSurname.setHint(AppGlobal.g_oLang.get()._("name"));
			m_oFrameSearchInfo.attachChild(m_oTextboxSurname);
			
			m_oButtonGroupEnquiry = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonGroupEnquiry, "butGroupEnquiry");
			m_oButtonGroupEnquiry.setValue(AppGlobal.g_oLang.get()._("group_enquiry"));
			m_oFrameSearchInfo.attachChild(m_oButtonGroupEnquiry);
			
			m_oButtonSubAREnquiry = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonSubAREnquiry, "butSubAREnquiry");
			m_oButtonSubAREnquiry.setValue(AppGlobal.g_oLang.get()._("sub_ar_enquiry"));
			m_oFrameSearchInfo.attachChild(m_oButtonSubAREnquiry);
			
			m_oTxtboxEnquiryNumber.setHint(AppGlobal.g_oLang.get()._("room_number"));
			
			//add click event to room enquiry button
			m_oButtonRoomEnquiry.addClickServerRequestSubmitElement(m_oTextboxAccountNumber);
			m_oButtonRoomEnquiry.addClickServerRequestSubmitElement(m_oTextboxSurname);
			m_oButtonGroupEnquiry.addClickServerRequestSubmitElement(m_oTextboxAccountNumber);
			m_oButtonGroupEnquiry.addClickServerRequestSubmitElement(m_oTextboxSurname);
			m_oButtonAREnquiry.addClickServerRequestSubmitElement(m_oTextboxAccountNumber);
			m_oButtonAREnquiry.addClickServerRequestSubmitElement(m_oTextboxSurname);
			m_oButtonSubAREnquiry.addClickServerRequestSubmitElement(m_oTextboxAccountNumber);
			m_oButtonSubAREnquiry.addClickServerRequestSubmitElement(m_oTextboxSurname);
			
			int iOffset = m_oTextboxAccountNumber.getHeight() + 16;
			m_oFrameSearchInfo.setHeight(m_oFrameSearchInfo.getHeight() + iOffset);
			
			m_oFrameGuestDetailBackground.setTop(m_oFrameGuestDetailBackground.getTop() + iOffset);
			m_oFrameGuestDetailBackground.setHeight(m_oFrameGuestDetailBackground.getHeight() - iOffset);
			m_oBasketGuestList.setHeight(m_oFrameGuestDetailBackground.getHeight() / 2);
			m_oBasketSubGuestList.setTop(m_oBasketGuestList.getTop() + m_oBasketGuestList.getHeight());
			m_oBasketSubGuestList.setHeight(m_oFrameGuestDetailBackground.getHeight() / 2);
			if(!bPostingAskInfo) {
				m_oFrameGuestDetail.setTop(m_oFrameGuestDetail.getTop() + iOffset);
				m_oFrameGuestDetail.setHeight(m_oFrameGuestDetail.getHeight() - iOffset);
				//set the height of basic info frame and user info frame equal
				m_oFrameGuestDetail.calibrateInfoFrameSize();
			}
		}
		
		//for support showing image
		if(!bPostingAskInfo && isPhotoSupported(FramePMSEnquiryResult.PHOTO_TYPE_GUEST)) {
			m_oImgGuestImage = new VirtualUIImage();
			m_oTemplateBuilder.buildImage(m_oImgGuestImage, "imgGuestImage");
			m_oImgGuestImage.setVisible(false);
			m_oImgGuestImage.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_ASPECT_FIT_CENTER);
			this.attachChild(m_oImgGuestImage);
		}
		if(!bPostingAskInfo && isPhotoSupported(FramePMSEnquiryResult.PHOTO_TYPE_SIGN)) {
			m_oImgSignImage = new VirtualUIImage();
			m_oTemplateBuilder.buildImage(m_oImgSignImage, "imgGuestSignImage");
			m_oImgSignImage.setVisible(false);
			m_oImgSignImage.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_ASPECT_FIT_CENTER);
			this.attachChild(m_oImgSignImage);
		}
		if(sInterfaceKey.equals(InfVendor.KEY_ASPEN_PMS)){
			if(!m_oTxtboxEnquiryNumber.getValue().isEmpty()){
				HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();
				oEnquiryInfo.put("enquiryNumber", m_oTxtboxEnquiryNumber.getValue());
				for (FramePMSEnquiryResultListener listener : listeners) {
					// Raise the event to parent
					listener.framePMSEnquiryResult_clickEnquiry(oEnquiryInfo, m_bPostingAskInfo);
					break;
				}
			}
		}
	}
	
	public void setPostingType(String sPostingType) {
		m_sPostingType = sPostingType;
	}
	
	public void setPostingTypeEnquiryPage(String sPostingType) {
		m_sPostingType = sPostingType;
		
		// for room posting
		if(m_sPostingType.equals(FuncPMS.POSTING_TYPE_ROOM)) {
			//Button
			m_oButtonRoomEnquiry.setVisible(true);
			if(isButtonSupported(FramePMSEnquiryResult.BUTTON_GROUP_ENQUIRY))
				m_oButtonGroupEnquiry.setVisible(false);
			if(m_oButtonGroupEnquiry != null)
				m_oButtonAREnquiry.setVisible(false);
			if(isButtonSupported(FramePMSEnquiryResult.BUTTON_SUB_AR_ENQUIRY))
				m_oButtonSubAREnquiry.setVisible(false);
			
			// Input box
			m_oTxtboxEnquiryNumber.setVisible(true);
		}
		
		if(m_sPostingType.equals(FuncPMS.POSTING_TYPE_GROUP)) {
			// Button
			m_oButtonRoomEnquiry.setVisible(false);
			if(isButtonSupported(FramePMSEnquiryResult.BUTTON_GROUP_ENQUIRY))
				m_oButtonGroupEnquiry.setVisible(true);
			m_oButtonAREnquiry.setVisible(false);
			if(isButtonSupported(FramePMSEnquiryResult.BUTTON_SUB_AR_ENQUIRY))
				m_oButtonSubAREnquiry.setVisible(false);
			
			// Input box
			m_oTxtboxEnquiryNumber.setVisible(false);
			m_oTextboxSurname.setTop(m_oTextboxAccountNumber.getTop());
			m_oTextboxSurname.setLeft(m_oTextboxAccountNumber.getLeft());
			m_oTextboxAccountNumber.setTop(m_oTxtboxEnquiryNumber.getTop());
			m_oTextboxAccountNumber.setLeft(m_oTxtboxEnquiryNumber.getLeft());
			m_oTextboxAccountNumber.setFocusWhenShow(true);
			
			m_oButtonGroupEnquiry.setLeft(m_oButtonRoomEnquiry.getLeft());
		}
		
		if(m_sPostingType.equals(FuncPMS.POSTING_TYPE_AR)) {
			// Button
			m_oButtonRoomEnquiry.setVisible(false);
			if(isButtonSupported(FramePMSEnquiryResult.BUTTON_GROUP_ENQUIRY))
				m_oButtonGroupEnquiry.setVisible(false);
			m_oButtonAREnquiry.setVisible(true);
			if(isButtonSupported(FramePMSEnquiryResult.BUTTON_SUB_AR_ENQUIRY))
				m_oButtonSubAREnquiry.setVisible(false);
			
			// Input box
			m_oTxtboxEnquiryNumber.setVisible(false);
			m_oTextboxSurname.setTop(m_oTextboxAccountNumber.getTop());
			m_oTextboxSurname.setLeft(m_oTextboxAccountNumber.getLeft());
			m_oTextboxAccountNumber.setTop(m_oTxtboxEnquiryNumber.getTop());
			m_oTextboxAccountNumber.setLeft(m_oTxtboxEnquiryNumber.getLeft());
			m_oTextboxAccountNumber.setFocusWhenShow(true);
			
			m_oButtonSubAREnquiry.setLeft(m_oButtonGroupEnquiry.getLeft());
		}
	}
	
	public void clearGuestList(boolean bGuestList, boolean bSubGuestList) {
		if(bSubGuestList && m_oBasketSubGuestList != null)
			m_oBasketSubGuestList.clearAllSections();
		if(bGuestList)
			m_oBasketGuestList.clearAllSections();
		if(m_oFrameGuestDetail != null)
			m_oFrameGuestDetail.clearGuestDetail();
		
		if(isPhotoSupported(FramePMSEnquiryResult.PHOTO_TYPE_GUEST) && m_oImgGuestImage != null && m_oImgGuestImage.getVisible()) {
			m_oFrameGuestDetail.setWidth(m_oFrameGuestDetail.getWidth() + m_oImgGuestImage.getWidth() + 50);
			m_oImgGuestImage.setVisible(false);
		}
		
		if(isPhotoSupported(FramePMSEnquiryResult.PHOTO_TYPE_SIGN) && m_oImgSignImage != null && m_oImgSignImage.getVisible()) {
			m_oFrameGuestDetail.setWidth(m_oFrameGuestDetail.getWidth() + m_oImgSignImage.getWidth() + 50);
			m_oImgSignImage.setVisible(false);
		}
	}
	
	public void addGuestToGuestList(int iSectionId, int iItemIndex, String sVendorKey, HashMap<String, String> oGuestInfo) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldTypes = new ArrayList<String>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		
		// No.
		iFieldWidths.add(100);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		if(sVendorKey.equals(InfVendor.KEY_4700_TCPIP) || sVendorKey.equals(InfVendor.KEY_4700_SERIAL_PORT))
			sFieldValues.add(oGuestInfo.get("line"));
		else
			sFieldValues.add(String.valueOf((iItemIndex+1)));
		sFieldAligns.add("");
		
		if(sVendorKey.equals(InfVendor.KEY_PEGASUS) && oGuestInfo.containsKey("packageCode")) {	// Pegasus package enquiry
			// Guest Name
			iFieldWidths.add(250);
			sFieldValues.add(oGuestInfo.get("packageName"));
			sFieldAligns.add("");
			// Room Number
			iFieldWidths.add(150);
			sFieldValues.add(oGuestInfo.get("packageCode"));
			sFieldAligns.add("");
		} else {
			// Guest Name
			iFieldWidths.add(250);
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			sFieldValues.add(oGuestInfo.get("guestName"));
			sFieldAligns.add("");
			
			// Room Number / Search sub list
			if(m_bPostingAskInfo && isSubGuestListSupported() && m_sPostingType.equals(FuncPMS.POSTING_TYPE_AR)) {
				sFieldTypes.add(HeroActionProtocol.View.Type.IMAGE);
				sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/search_item_detail_icon.png");
				
			}else {
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				sFieldValues.add(oGuestInfo.get("roomNumber"));
			}
			iFieldWidths.add(150);
			sFieldAligns.add("");
		}
		
		// Remark for aspen pms
		if(sVendorKey.equals(InfVendor.KEY_ASPEN_PMS)){
			iFieldWidths.add(250);
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			sFieldValues.add(oGuestInfo.get("remark"));
			sFieldAligns.add("");
		}
		
		// Show image
		showImages(oGuestInfo, false, false);
		
		m_oBasketGuestList.addItem(iSectionId, iItemIndex, m_iBasketRowHeight, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
	}
	
	public void addGuestToSubGuestList(int iSectionId, int iItemIndex, String sVendorKey, HashMap<String, String> oSubGuest, boolean bShowImage) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		
		// No.
		iFieldWidths.add(100);
		sFieldValues.add(String.valueOf(iItemIndex + 1));
		sFieldAligns.add("");
		
		// Guest Name
		iFieldWidths.add(400);
		sFieldValues.add(oSubGuest.get("guestName"));
		sFieldAligns.add("");
		
		// Show image
		showImages(oSubGuest, bShowImage, bShowImage);
		
		m_oBasketSubGuestList.addItem(0, iItemIndex, m_iBasketRowHeight, iFieldWidths, sFieldValues, sFieldAligns, null, null);
	}
	
	public void showImages(HashMap<String, String> oGuestInfo, boolean sSubAccountEnquiry, boolean bShow){
		if(bShow) {
			if(m_oImgGuestImage != null && isPhotoSupported(FramePMSEnquiryResult.PHOTO_TYPE_GUEST) && oGuestInfo.containsKey("guestImage") && !oGuestInfo.get("guestImage").toString().isEmpty())
				m_oImgGuestImage.setSource("data:image/png;base64," + oGuestInfo.get("guestImage").toString());
			
			if(m_oImgSignImage != null && isPhotoSupported(FramePMSEnquiryResult.PHOTO_TYPE_SIGN) && oGuestInfo.containsKey("guestSignImage") && oGuestInfo.get("guestSignImage").toString().isEmpty())
				m_oImgSignImage.setSource("data:image/png;base64," + oGuestInfo.get("guestSignImage").toString());
		}
		
		// Guest image
		if(isPhotoSupported(FramePMSEnquiryResult.PHOTO_TYPE_GUEST) && m_oImgGuestImage != null) {
			if(!sSubAccountEnquiry) {
				if(m_oImgGuestImage.getVisible())
					m_oFrameGuestDetail.setWidth(m_oFrameGuestDetail.getWidth() + m_oImgGuestImage.getWidth() + 50);
				m_oImgGuestImage.setVisible(false);
			}else {
				if(!m_oImgGuestImage.getVisible())
					m_oFrameGuestDetail.setWidth(m_oFrameGuestDetail.getWidth() - m_oImgGuestImage.getWidth() - 50);
				m_oImgGuestImage.setVisible(true);
			}
		}
		
		// Guest sign image
		if(isPhotoSupported(FramePMSEnquiryResult.PHOTO_TYPE_SIGN) && m_oImgSignImage != null) {
			if(!sSubAccountEnquiry) {
				if(m_oImgSignImage.getVisible())
					m_oFrameGuestDetail.setWidth(m_oFrameGuestDetail.getWidth() + m_oImgSignImage.getWidth() + 50);
				m_oImgSignImage.setVisible(false);
			}else {
				if(!m_oImgSignImage.getVisible())
					m_oFrameGuestDetail.setWidth(m_oFrameGuestDetail.getWidth() - m_oImgSignImage.getWidth() - 50);
				m_oImgSignImage.setVisible(true);
			}
		}
	}
	
	public void showGuestDetail(String sEnquiryType, HashMap<String, String> oGuestInfo, HashMap<Integer, List<HashMap<String, String>>> oSubGuestList, boolean bByClickField) {
		// update sub guest list if necessary
		if(bByClickField && isSubGuestListSupported() && oSubGuestList != null && m_sEnquiryType.equals(FuncPMS.ENQUIRY_TYPE_AR)){
			m_oBasketSubGuestList.clearAllSections();
			
			Integer iGuestIndex = Integer.valueOf(oGuestInfo.get("guestIndex"));
			if(oSubGuestList.containsKey(iGuestIndex)) {
				List<HashMap<String, String>> oTempList = oSubGuestList.get(iGuestIndex);
				for(int i=0 ; i<oTempList.size(); i++) {
					HashMap<String, String> oSubGuest = oTempList.get(i);
					this.addGuestToSubGuestList(0, i, m_sInterfaceVendorKey, oSubGuest, false);
				}
			}
		}
		
		/*
		// Show sub guest list if is sub AR account enquiry
		if((sEnquiryType != null && sEnquiryType.equals(FuncPMS.ENQUIRY_TYPE_SUB_AR)) || (sEnquiryType == null && m_sEnquiryType.equals(FuncPMS.ENQUIRY_TYPE_SUB_AR))) {
			m_oBasketGuestList.setVisible(false);
			m_oBasketSubGuestList.setVisible(true);
		}
		*/
		
		if(!m_bPostingAskInfo)
			m_oFrameGuestDetail.updateGuestDetail(oGuestInfo);
	}
	
	public void changeButtonColor(VirtualUIButton oSelectedButton) {
		HashMap<VirtualUIButton, Boolean> oVirtualUIButtonList = new HashMap<VirtualUIButton, Boolean> ();
		oVirtualUIButtonList.put(m_oButtonRoomEnquiry, true);
		oVirtualUIButtonList.put(m_oButtonAREnquiry, m_bShowAREnquiryButton);
		if(isButtonSupported(FramePMSEnquiryResult.BUTTON_SUB_AR_ENQUIRY))
			oVirtualUIButtonList.put(m_oButtonSubAREnquiry, true);
		else
			oVirtualUIButtonList.put(m_oButtonSubAREnquiry, false);
		if(isButtonSupported(FramePMSEnquiryResult.BUTTON_GROUP_ENQUIRY))
			oVirtualUIButtonList.put(m_oButtonGroupEnquiry, true);
		else
			oVirtualUIButtonList.put(m_oButtonGroupEnquiry, false);
		oVirtualUIButtonList.put(m_oButtonRegisterEnquiry, m_bShowRegisterEnquiryButton);
		oVirtualUIButtonList.put(m_oButtonGetExtendDetails, m_bShowGetExtendDetailButton);
		oVirtualUIButtonList.put(m_oButtonPackageEnquiry, m_bShowPackageEnquiryButton);
		
		for (Entry<VirtualUIButton, Boolean> entry: oVirtualUIButtonList.entrySet()) {
			boolean bShow = entry.getValue();
			VirtualUIButton oVirtualUIButton = entry.getKey();
			if (bShow) {
				if (oVirtualUIButton == oSelectedButton) {
					oSelectedButton.setForegroundColor(FramePMSEnquiryResult.COLOR_FOREGROUND_SELECTED);
					oSelectedButton.setBackgroundColor(FramePMSEnquiryResult.COLOR_BACKGROUND_SELECTED);
				} else {
					oVirtualUIButton.setForegroundColor(FramePMSEnquiryResult.COLOR_FOREGROUND_UNSELECTED);
					oVirtualUIButton.setBackgroundColor(FramePMSEnquiryResult.COLOR_BACKGROUND_UNSELECTED);
				}
			}
		}
		
	}
	
	public void changePackageButtonPosition() {
		m_oButtonPackageEnquiry.setLeft(m_oButtonRoomEnquiry.getLeft());
		m_oButtonPackageEnquiry.setTop(m_oButtonRoomEnquiry.getTop());
		m_oButtonRoomEnquiry.setVisible(false);
		
		updateGuestListTagAndHeader(FuncPMS.ENQUIRY_TYPE_PACKAGE);
	}
	
	public void updateGuestListTagAndHeader(String sType) {
		//String sTag = (sType.equals(FuncPMS.ENQUIRY_TYPE_ROOM))? AppGlobal.g_oLang.get()._("guest_list"): AppGlobal.g_oLang.get()._("package_list");
		this.clearGuestList(true, false);
		
		// add header for guest list
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		if (sType.equals(FuncPMS.ENQUIRY_TYPE_ROOM)) {
			iFieldWidths.add(100);
			sFieldValues.add(AppGlobal.g_oLang.get()._("number"));
			iFieldWidths.add(250);
			sFieldValues.add(AppGlobal.g_oLang.get()._("name"));
			iFieldWidths.add(150);
			sFieldValues.add(AppGlobal.g_oLang.get()._("room_number"));
		} else {
			iFieldWidths.add(100);
			sFieldValues.add(AppGlobal.g_oLang.get()._("number"));
			iFieldWidths.add(250);
			sFieldValues.add(AppGlobal.g_oLang.get()._("name"));
			iFieldWidths.add(150);
			sFieldValues.add(AppGlobal.g_oLang.get()._("package_code"));
		}
		
		m_oBasketGuestList.addHeader(iFieldWidths, sFieldValues);
		m_oBasketGuestList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
	}
	
	public void showClearRoomButton() {
		m_oButtonClearRoom.setVisible(true);
		m_oButtonSetRoom.setVisible(false);
	}
	
	public void showSetRoomButton(){
		m_oButtonClearRoom.setVisible(false);
		m_oButtonSetRoom.setVisible(true);
	}
	
	public void blockSetAndClearRoomButton(){
		m_oButtonClearRoom.setVisible(false);
		m_oButtonSetRoom.setVisible(false);
	}
	
	private boolean roomEnquiry(){
		HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();
		m_bGuestExtendDetails = false;
		m_oButtonRoomEnquiry.setClickHideKeyboard(true);
		m_oBasketGuestList.setVisible(true);
		if(isSubGuestListSupported() && m_bPostingAskInfo)
			m_oBasketSubGuestList.setVisible(false);
		if(!this.m_sInterfaceVendorKey.equals(InfVendor.KEY_ASPEN_PMS))
			this.changeButtonColor(m_oButtonRoomEnquiry);
		if (this.m_sInterfaceVendorKey.equals(InfVendor.KEY_PEGASUS)) {
			if(isEnquiryNumberEmpty()) {
				showWarningDialog(AppGlobal.g_oLang.get()._("room_number_is_empty"));
				return false;
			}
			
			this.updateGuestListTagAndHeader(FuncPMS.ENQUIRY_TYPE_ROOM);
		}
		
		if(m_sInterfaceVendorKey.equals(InfVendor.KEY_HTNG)) {
			String sPmsType = getInterfaceSetupBySecionAndName("connection_setup", "pms_type", "imagine");
			if(sPmsType.equals(FuncPMS.PMS_TYPE_IMAGINE)) {
				if(isEnquiryNumberEmpty()) {
					showWarningDialog(AppGlobal.g_oLang.get()._("room_number_is_empty"));
					return false;
				}
				
				if (m_oTextboxGivenName.getValue().isEmpty() && m_oTextboxSurname.getValue().isEmpty()) {
					showWarningDialog(AppGlobal.g_oLang.get()._("given_name_or_surname_is_empty"));
					return false;
				}
				
				if (m_olblArrivalDate.getValue().equals(AppGlobal.g_oLang.get()._("arrival_date"))
					|| m_olblDepartureDate.getValue().equals(AppGlobal.g_oLang.get()._("departure_date"))) {
					showWarningDialog(AppGlobal.g_oLang.get()._("arrival_or_departure_date_is_empty"));
					return false;
				}
				
				DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
				DateTime oArrivalDate = formatter.parseDateTime(m_olblArrivalDate.getValue());
				DateTime oDepartDate = formatter.parseDateTime(m_olblDepartureDate.getValue());
				if (oDepartDate.isBefore(oArrivalDate)) {
					showWarningDialog(AppGlobal.g_oLang.get()._("departure_date_is_early_than_arrival_date"));
					return false;
				}
			}
			else {
				String sWarningMessage = AppGlobal.g_oLang.get()._("room_number_or_surname_is_empty"); 
				if(sPmsType.equals(FuncPMS.PMS_TYPE_SHIJI))
					sWarningMessage = AppGlobal.g_oLang.get()._("room_number_is_empty");
				if(isEnquiryNumberEmpty() && this.isSurnameEmpty()) {
					showWarningDialog(sWarningMessage);
					return false;
				}
			}
			
			if (m_oTextboxGivenName.getValue().isEmpty())
				oEnquiryInfo.put("givenName", "");
			else
				oEnquiryInfo.put("givenName", m_oTextboxGivenName.getValue());
			
			if (m_oTextboxSurname.getValue().isEmpty())
				oEnquiryInfo.put("surname", "");
			else
				oEnquiryInfo.put("surname", m_oTextboxSurname.getValue());
			
			if (m_olblArrivalDate.getValue().isEmpty() || m_olblArrivalDate.getValue().equals(AppGlobal.g_oLang.get()._("arrival_date")))
				oEnquiryInfo.put("arrivalDate", "");
			else
				oEnquiryInfo.put("arrivalDate", m_olblArrivalDate.getValue());
			
			if (m_olblDepartureDate.getValue().isEmpty() || m_olblDepartureDate.getValue().equals(AppGlobal.g_oLang.get()._("departure_date")))
				oEnquiryInfo.put("departureDate", "");
			else
				oEnquiryInfo.put("departureDate", m_olblDepartureDate.getValue());
		}
		
		// For XMS PMS
		if(m_sInterfaceVendorKey.equals(InfVendor.KEY_XMS)) {
			if (isAccountNumberEmpty() && isSurnameEmpty() && isEnquiryNumberEmpty()) {
				showWarningDialog(AppGlobal.g_oLang.get()._("please_input_at_least_one_informaion"));
				return false;
			}
			
			if (isSurnameEmpty())
				oEnquiryInfo.put("surname", "");
			else
				oEnquiryInfo.put("surname", m_oTextboxSurname.getValue());
			
			if (isAccountNumberEmpty())
				oEnquiryInfo.put("enquiryAccountNumber", "");
			else
				oEnquiryInfo.put("enquiryAccountNumber", m_oTextboxAccountNumber.getValue());
		}
		
		if (isEnquiryNumberEmpty()) {
			String sAllowEmptyEnquiry = getInterfaceSetupBySecionAndName("pms_setting", "pms_allow_empty_enquiry_number", "");
			if(!sAllowEmptyEnquiry.isEmpty() && sAllowEmptyEnquiry.equals("0")) {
				showWarningDialog(AppGlobal.g_oLang.get()._("room_number_is_empty"));
				return false;
			}
			oEnquiryInfo.put("enquiryNumber", "");
		}else
			oEnquiryInfo.put("enquiryNumber", m_oTxtboxEnquiryNumber.getValue());
		oEnquiryInfo.put("enquiryType", FuncPMS.ENQUIRY_TYPE_ROOM);
		
		for (FramePMSEnquiryResultListener listener : listeners) {
			// Raise the event to parent
			listener.framePMSEnquiryResult_clickEnquiry(oEnquiryInfo, m_bPostingAskInfo);
			break;
		}
		return true;
	}
	
	//enquiry criteria checking
	private boolean enquiryCriteriaChecking(String sButtonType) {
		boolean bResult = true;
		
		switch (sButtonType) {
		case FramePMSEnquiryResult.BUTTON_AR_ENQUIRY:
			if(m_sInterfaceVendorKey.equals(InfVendor.KEY_XMS) && isAccountNumberEmpty() && isSurnameEmpty())
				bResult = false;
			break;
		case FramePMSEnquiryResult.BUTTON_GROUP_ENQUIRY:
			if(m_sInterfaceVendorKey.equals(InfVendor.KEY_XMS) && isAccountNumberEmpty() && isSurnameEmpty())
				bResult = false;
			break;
		case FramePMSEnquiryResult.BUTTON_SUB_AR_ENQUIRY:
			if(m_sInterfaceVendorKey.equals(InfVendor.KEY_XMS) && isAccountNumberEmpty())
				bResult = false;
			break;
		}
		
		return bResult;
	}
	
	//check whether support button
	private boolean isButtonSupported(String sButtonType) {
		boolean bResult = false;
		
		if(sButtonType.equals(FramePMSEnquiryResult.BUTTON_GROUP_ENQUIRY)) {
			if(this.m_sInterfaceVendorKey.equals(InfVendor.KEY_XMS))
				bResult = true;
		}else if(sButtonType.equals(FramePMSEnquiryResult.BUTTON_SUB_AR_ENQUIRY)) {
			if(this.m_sInterfaceVendorKey.equals(InfVendor.KEY_XMS))
				bResult = true;
		}
		
		return bResult;
	}
	
	//check whether support sub guest list
	private boolean isSubGuestListSupported() {
		if(m_sInterfaceVendorKey.equals(InfVendor.KEY_XMS))
			return true;
		else
			return false;
	}
	
	//check whether support sub guest list
	private boolean isPhotoSupported(String sPhotoType) {
		boolean bResult = false;
		
		if(sPhotoType.equals(FramePMSEnquiryResult.PHOTO_TYPE_GUEST)) {
			if(this.m_sInterfaceVendorKey.equals(InfVendor.KEY_XMS))
				bResult = true;
		}else if(sPhotoType.equals(FramePMSEnquiryResult.PHOTO_TYPE_GUEST)) {
			if(this.m_sInterfaceVendorKey.equals(InfVendor.KEY_XMS))
				bResult = true;
		}
		
		return bResult;
	}
	
	//check whether account text box is empty or not
	private boolean isAccountNumberEmpty() {
		boolean bEmpty = true;
		
		if(m_oTextboxAccountNumber != null && !m_oTextboxAccountNumber.getValue().isEmpty())
			bEmpty = false;
		
		return bEmpty;
	}
	
	//check whether account text box is empty or not
	private boolean isSurnameEmpty() {
		boolean bEmpty = true;
		
		if(m_oTextboxSurname != null && !m_oTextboxSurname.getValue().isEmpty())
			bEmpty = false;
		
		return bEmpty;
	}
	
	//check whether account text box is empty or not
	private boolean isEnquiryNumberEmpty() {
		boolean bEmpty = true;
		
		if(m_oTxtboxEnquiryNumber != null && !m_oTxtboxEnquiryNumber.getValue().isEmpty())
			bEmpty = false;
		
		return bEmpty;
	}
	
	//get interface setup with section and name
	private String getInterfaceSetupBySecionAndName(String sSection, String sName, String sDefaultValue) {
		String sSetup = sDefaultValue;
		if(m_oInterfaceSetup != null && m_oInterfaceSetup.has(sSection) && m_oInterfaceSetup.optJSONObject(sSection).optJSONObject("params").has(sName))
			sSetup = m_oInterfaceSetup.optJSONObject(sSection).optJSONObject("params").optJSONObject(sName).optString("value");
		return sSetup;
	}
	
	//show warning dialog message
	private void showWarningDialog(String sMessage) {
		FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
		oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
		oFormDialogBox.setMessage(sMessage);
		oFormDialogBox.show();
		return;
	}
	
	/***********************/
	/*  Override Function  */
	/***********************/
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false, bPassCriteriaCheck = true;
		HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();
		String sErrorMessage = "";
		
		if (iChildId == m_oButtonRoomEnquiry.getId()) {
			m_sEnquiryType = FuncPMS.ENQUIRY_TYPE_ROOM;
			bMatchChild = roomEnquiry();
		}else if (m_oButtonSetRoom != null && iChildId == m_oButtonSetRoom.getId()){
			for(FramePMSEnquiryResultListener listener: listeners) {
				listener.framePMSSetRoom();
				break;
			}
			bMatchChild = true;
		}else if (m_oButtonClearRoom != null && iChildId == m_oButtonClearRoom.getId()){
			for(FramePMSEnquiryResultListener listener: listeners) {
				listener.framePMSClearRoom();
				break;
			}
			bMatchChild = true;
		}else if (m_oButtonAREnquiry != null && iChildId == m_oButtonAREnquiry.getId()) {
			m_bGuestExtendDetails = false;
			m_oButtonAREnquiry.setClickHideKeyboard(true);
			m_sEnquiryType = FuncPMS.ENQUIRY_TYPE_AR;
			
			m_oBasketGuestList.setVisible(true);
			if(!isSubGuestListSupported() && m_oBasketSubGuestList != null)
				m_oBasketSubGuestList.setVisible(false);
			
			if(m_sInterfaceVendorKey.equals(InfVendor.KEY_XMS) && !enquiryCriteriaChecking(FramePMSEnquiryResult.BUTTON_AR_ENQUIRY)) {
				bPassCriteriaCheck = false;
				sErrorMessage = AppGlobal.g_oLang.get()._("please_input_account_number_or_name");
			}
			
			if(!bPassCriteriaCheck) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
				oFormDialogBox.setMessage(sErrorMessage);
				oFormDialogBox.show();
				return false;
			}
			
			if (m_oTextboxGivenName == null || m_oTextboxGivenName.getValue().isEmpty() || m_oTextboxGivenName.getValue().equals(AppGlobal.g_oLang.get()._("given_name")))
				oEnquiryInfo.put("givenName", "");
			else
				oEnquiryInfo.put("givenName", m_oTextboxGivenName.getValue());
			
			if (m_oTextboxSurname == null || isSurnameEmpty())
				oEnquiryInfo.put("surname", "");
			else
				oEnquiryInfo.put("surname", m_oTextboxSurname.getValue());
			
			if (m_oTextboxAccountNumber != null && !isAccountNumberEmpty())
				oEnquiryInfo.put("enquiryNumber", m_oTextboxAccountNumber.getValue());
			
			this.changeButtonColor(m_oButtonAREnquiry);
			
			for (FramePMSEnquiryResultListener listener : listeners) {
				// Raise the event to parent
				if(!oEnquiryInfo.containsKey("enquiryNumber") && !m_sInterfaceVendorKey.equals(InfVendor.KEY_XMS))
					oEnquiryInfo.put("enquiryNumber", m_oTxtboxEnquiryNumber.getValue());
				oEnquiryInfo.put("enquiryType", FuncPMS.ENQUIRY_TYPE_AR);
				listener.framePMSEnquiryResult_clickEnquiry(oEnquiryInfo, m_bPostingAskInfo);
				break;
			}
			bMatchChild = true;
		}else if (m_oButtonSubAREnquiry != null && iChildId == m_oButtonSubAREnquiry.getId()) {
			m_bGuestExtendDetails = false;
			m_oButtonSubAREnquiry.setClickHideKeyboard(true);
			m_sEnquiryType = FuncPMS.ENQUIRY_TYPE_SUB_AR;
			
			if(m_sInterfaceVendorKey.equals(InfVendor.KEY_XMS) && !enquiryCriteriaChecking(FramePMSEnquiryResult.BUTTON_SUB_AR_ENQUIRY)) {
				bPassCriteriaCheck = false;
				sErrorMessage = AppGlobal.g_oLang.get()._("please_input_account_number");
			}
			
			if(!bPassCriteriaCheck) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
				oFormDialogBox.setMessage(sErrorMessage);
				oFormDialogBox.show();
				return false;
			}
			
			clearGuestList(true, true);
			if (m_oTextboxAccountNumber == null || isAccountNumberEmpty())
				oEnquiryInfo.put("enquiryNumber", "");
			else
				oEnquiryInfo.put("enquiryNumber", m_oTextboxAccountNumber.getValue());
			oEnquiryInfo.put("enquiryType", FuncPMS.ENQUIRY_TYPE_SUB_AR);
			
			this.changeButtonColor(m_oButtonSubAREnquiry);
			
			for (FramePMSEnquiryResultListener listener : listeners) {
				// Raise the event to parent
				listener.framePMSEnquiryResult_clickEnquiry(oEnquiryInfo, m_bPostingAskInfo);
				break;
			}
			bMatchChild = true;
		}else if (m_oButtonGroupEnquiry != null && iChildId == m_oButtonGroupEnquiry.getId()) {
			m_bGuestExtendDetails = false;
			m_oButtonGroupEnquiry.setClickHideKeyboard(true);
			m_sEnquiryType = FuncPMS.ENQUIRY_TYPE_GROUP;
			
			m_oBasketGuestList.setVisible(true);
			if(!isSubGuestListSupported())
				m_oBasketSubGuestList.setVisible(false);
			
			if(m_sInterfaceVendorKey.equals(InfVendor.KEY_XMS) && !enquiryCriteriaChecking(FramePMSEnquiryResult.BUTTON_GROUP_ENQUIRY)) {
				bPassCriteriaCheck = false;
				sErrorMessage = AppGlobal.g_oLang.get()._("please_input_account_number_or_name");
			}
			
			if(!bPassCriteriaCheck) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
				oFormDialogBox.setMessage(sErrorMessage);
				oFormDialogBox.show();
				return false;
			}
			
			if (m_oTextboxSurname == null || isSurnameEmpty())
				oEnquiryInfo.put("surname", "");
			else
				oEnquiryInfo.put("surname", m_oTextboxSurname.getValue());

			if (m_oTextboxAccountNumber == null || isAccountNumberEmpty())
				oEnquiryInfo.put("enquiryNumber", "");
			else
				oEnquiryInfo.put("enquiryNumber", m_oTextboxAccountNumber.getValue());
			oEnquiryInfo.put("enquiryType", FuncPMS.ENQUIRY_TYPE_GROUP);
			
			this.changeButtonColor(m_oButtonGroupEnquiry);
			
			for (FramePMSEnquiryResultListener listener : listeners) {
				// Raise the event to parent
				listener.framePMSEnquiryResult_clickEnquiry(oEnquiryInfo, m_bPostingAskInfo);
				break;
			}
			bMatchChild = true;
		}else if (m_oButtonRegisterEnquiry != null && iChildId == m_oButtonRegisterEnquiry.getId()) {
			m_bGuestExtendDetails = false;
			m_oButtonRegisterEnquiry.setClickHideKeyboard(true);
			m_sEnquiryType = FuncPMS.ENQUIRY_TYPE_REGISTER;
			
			this.changeButtonColor(m_oButtonRegisterEnquiry);
			
			for (FramePMSEnquiryResultListener listener : listeners) {
				// Raise the event to parent
				oEnquiryInfo.put("enquiryNumber", m_oTxtboxEnquiryNumber.getValue());
				oEnquiryInfo.put("enquiryType", FuncPMS.ENQUIRY_TYPE_REGISTER);
				listener.framePMSEnquiryResult_clickEnquiry(oEnquiryInfo, m_bPostingAskInfo);
				break;
			}
			bMatchChild = true;
		} else if (m_oButtonPackageEnquiry != null && iChildId == m_oButtonPackageEnquiry.getId()) {
			m_bGuestExtendDetails = false;
			m_oButtonPackageEnquiry.setClickHideKeyboard(true);
			m_sEnquiryType = FuncPMS.ENQUIRY_TYPE_PACKAGE;
			
			this.changeButtonColor(m_oButtonPackageEnquiry);
			
			if (m_oTxtboxEnquiryNumber.getValue().isEmpty()) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("room_number_is_empty"));
				oFormDialogBox.show();
				
				return false;
			}
			
			this.updateGuestListTagAndHeader(FuncPMS.ENQUIRY_TYPE_PACKAGE);
			for (FramePMSEnquiryResultListener listener : listeners) {
				// Raise the event to parent
				oEnquiryInfo.put("enquiryNumber", m_oTxtboxEnquiryNumber.getValue());
				oEnquiryInfo.put("enquiryType", FuncPMS.ENQUIRY_TYPE_PACKAGE);
				listener.framePMSEnquiryResult_clickEnquiry(oEnquiryInfo, m_bPostingAskInfo);
				break;
			}
			bMatchChild = true;
		}else if (m_oButtonGetExtendDetails != null && iChildId == m_oButtonGetExtendDetails.getId()) {
			m_bGuestExtendDetails = true;
			m_oButtonGetExtendDetails.setClickHideKeyboard(true);
			m_sEnquiryType = FuncPMS.ENQUIRY_TYPE_ROOM;
			
			this.changeButtonColor(m_oButtonGetExtendDetails);
			
			for (FramePMSEnquiryResultListener listener : listeners) {
				// Raise the event to parent
				String sNewEnquiryNumber = "I/"+m_oTxtboxEnquiryNumber.getValue();
				m_oTxtboxEnquiryNumber.setValue(sNewEnquiryNumber);
				oEnquiryInfo.put("enquiryNumber", m_oTxtboxEnquiryNumber.getValue());
				oEnquiryInfo.put("enquiryType", FuncPMS.ENQUIRY_TYPE_ROOM);
				listener.framePMSEnquiryResult_clickEnquiry(oEnquiryInfo, m_bPostingAskInfo);
				break;
			}
			bMatchChild = true;
		/*
		}else if(iChildId == m_oLabelGuestListTag.getId()) {
			for (FramePMSEnquiryResultListener listener : listeners) {
				// Raise the event to parent
				String sMasterAccountNum = listener.freamPMSEnquiryResult_getCurrentChosenGuestInformation("accountNumber");
				
				m_oBasketGuestList.setVisible(true);
				if(isSubGuestListSupported())
					m_oBasketSubGuestList.setVisible(false);
				if(!sMasterAccountNum.isEmpty()) {
					if(m_oTextboxAccountNumber != null && !isAccountNumberEmpty())
						oEnquiryInfo.put("enquiryNumber", m_oTextboxAccountNumber.getValue());
					else
						oEnquiryInfo.put("enquiryNumber", "");
						//oEnquiryInfo.put("enquiryNumber", sMasterAccountNum);
					if(m_oTextboxSurname != null && !isSurnameEmpty())
						oEnquiryInfo.put("surname", m_oTextboxSurname.getValue());
					else
						oEnquiryInfo.put("surname", "");
					oEnquiryInfo.put("enquiryType", FuncPMS.ENQUIRY_TYPE_AR);
					listener.framePMSEnquiryResult_clickEnquiry(oEnquiryInfo, m_bPostingAskInfo);
				}
				break;
			}
			
			bMatchChild = true;
		*/
		}else if (m_sInterfaceVendorKey.equals(InfVendor.KEY_HTNG) && iChildId == m_olblArrivalDate.getId()){
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			DateTime oArrivalDate = null;
			
			if (m_olblArrivalDate.getValue() != null && !m_olblArrivalDate.getValue().equals(AppGlobal.g_oLang.get()._("arrival_date")))
				oArrivalDate = formatter.parseDateTime(m_olblArrivalDate.getValue());
			
			FormDatePicker oFormDatePicker = new FormDatePicker(oArrivalDate, this.getParentForm());
			oFormDatePicker.show();
			
			if (oFormDatePicker.getDate() != null)
				m_olblArrivalDate.setValue(oFormDatePicker.getDate());
			
			bMatchChild = true;
		}
		else if(m_sInterfaceVendorKey.equals(InfVendor.KEY_HTNG) && iChildId == m_olblDepartureDate.getId()){
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			DateTime oDepartureDate = null;
			
			if (m_olblDepartureDate.getValue() != null && !m_olblDepartureDate.getValue().equals(AppGlobal.g_oLang.get()._("departure_date")))
				oDepartureDate = formatter.parseDateTime(m_olblDepartureDate.getValue());
			
			FormDatePicker oFormDatePicker = new FormDatePicker(oDepartureDate, this.getParentForm());
			oFormDatePicker.show();
			
			if (oFormDatePicker.getDate() != null)
				m_olblDepartureDate.setValue(oFormDatePicker.getDate());
			
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
	
	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
		
	}
	
	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();
		
		for(FramePMSEnquiryResultListener listener: listeners) {
			// Rasie the event to parent
			if(m_bPostingAskInfo) {
				if(isSubGuestListSupported() && this.m_sPostingType.equals(FuncPMS.POSTING_TYPE_AR)) {
					if(m_oBasketSubGuestList.getId() == iBasketId ) {
						oEnquiryInfo.put("enquiryNumber", "");
						oEnquiryInfo.put("enquirySubAccountNum", "");
						listener.framePMSEnquiryResult_clickSubGuestForPosting(iItemIndex, oEnquiryInfo);
					}else if(this.m_oBasketGuestList.getId() == iBasketId && iFieldIndex == 2) {
						// Search sub list
						listener.framePMSEnquiryResult_clickGuestRecord(iItemIndex, true);
					}else {
						oEnquiryInfo.put("enquiryNumber", "");
						oEnquiryInfo.put("enquirySubAccountNum", "");
						listener.framePMSEnquiryResult_clickGuestForPosting(iItemIndex, oEnquiryInfo);
					}
					
				}else {
					if(!isEnquiryNumberEmpty())
						oEnquiryInfo.put("enquiryNumber", m_oTxtboxEnquiryNumber.getValue());
					else
						oEnquiryInfo.put("enquiryNumber", "");
					if(m_oTextboxAccountNumber != null && !isAccountNumberEmpty())
						oEnquiryInfo.put("enquiryAccountNum", m_oTextboxAccountNumber.getValue());
					if(m_oTextboxSurname != null && !isSurnameEmpty())
						oEnquiryInfo.put("enquirySurname", m_oTextboxSurname.getValue());
					listener.framePMSEnquiryResult_clickGuestForPosting(iItemIndex, oEnquiryInfo);
				}
			}else if(m_bGuestExtendDetails) {
				m_bGuestExtendDetails = false;
				String sNewEnquiryNumber = m_oTxtboxEnquiryNumber.getValue()+"/"+m_oBasketGuestList.getFieldValue(iSectionIndex, iItemIndex, 0);
				m_oTxtboxEnquiryNumber.setValue(sNewEnquiryNumber);
				oEnquiryInfo.put("enquiryNumber", m_oTxtboxEnquiryNumber.getValue());
				oEnquiryInfo.put("enquiryType", FuncPMS.ENQUIRY_TYPE_ROOM);
				listener.framePMSEnquiryResult_clickEnquiry(oEnquiryInfo, m_bPostingAskInfo);
			}else {
				if(isSubGuestListSupported() && m_oBasketSubGuestList.getId() == iBasketId) {
					if(m_sEnquiryType.equals(FuncPMS.ENQUIRY_TYPE_SUB_AR)) {
						listener.framePMSEnquiryResult_clickGuestRecord(iItemIndex, false);
					}else {
						String sEnquiryNumber = m_oBasketSubGuestList.getFieldValue(iSectionIndex, iItemIndex, 2);
						if(m_sInterfaceVendorKey.equals(InfVendor.KEY_XMS)) {
							oEnquiryInfo.put("enquirySubAccountNumber", sEnquiryNumber);
							sEnquiryNumber = listener.freamPMSEnquiryResult_getCurrentChosenGuestInformation("accountNumber");
						}
						oEnquiryInfo.put("enquiryNumber", sEnquiryNumber);
						oEnquiryInfo.put("enquiryType", FuncPMS.ENQUIRY_TYPE_SUB_AR);
						if(m_sEnquiryType.equals(FuncPMS.ENQUIRY_TYPE_AR))
							oEnquiryInfo.put("enquiryFromArEnquiry", "true");
						
						listener.framePMSEnquiryResult_clickEnquiry(oEnquiryInfo, m_bPostingAskInfo);
					}
				}else
					listener.framePMSEnquiryResult_clickGuestRecord(iItemIndex, true);
			}
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
	public boolean valueChanged(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		String sSwipeCardValue = "";
		ClsActiveClient oActiveClient = AppGlobal.getActiveClient();
		if (oActiveClient != null) {
			if (iChildId == oActiveClient.getSwipeCardReaderElement().getId()
					&& oActiveClient.getSwipeCardReaderElement().getValueChangedServerRequestNote()
							.equals(FuncMSR.FRAME_SWIPE_CARD_DEFAULT)) {
				if (oActiveClient.getSwipeCardReaderElement().getValue().length() > 0) {
					sSwipeCardValue = oActiveClient.getSwipeCardReaderElement().getValue().replace("\r", "")
							.replace("\n", "");
				}
				bMatchChild = true;
			}
		}
		m_oTxtboxEnquiryNumber.setValue(sSwipeCardValue);
		bMatchChild = roomEnquiry();
		return bMatchChild;
	}
	
	@Override
	public void FrameTitleHeader_close() {
		for (FramePMSEnquiryResultListener listener : listeners) {
			// Raise the event to parent
			listener.framePMSEnquiryResult_clickBack();
		}
	}
}
