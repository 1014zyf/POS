package app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import om.ResvResv;
import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FrameReservationDetailListener {

	void frameReservationDetail_clickOK();
}

public class FrameReservationDetail extends VirtualUIFrame implements FrameTitleHeaderListener, FrameCommonBasketListener{

	TemplateBuilder m_oTemplateBuilder;

	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUIList m_oListResvDetail;
	private FrameCommonBasket m_oResvDetailBasket;
	private int m_iResvDetailContent;

	VirtualUIButton m_oButtonOK;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameReservationDetailListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameReservationDetailListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameReservationDetailListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	//Constructor
	public FrameReservationDetail() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameReservationDetailListener>();

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraReservationDetail.xml");

		// Title Bar Frame
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(true);
		m_oFrameTitleHeader.addListener(this);
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("reservation_information"));
		this.attachChild(m_oFrameTitleHeader);

		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())){
			m_oResvDetailBasket = new FrameCommonBasket();
			m_oTemplateBuilder.buildFrame(m_oResvDetailBasket, "fraResvDetail");
			m_oResvDetailBasket.init();
			m_oResvDetailBasket.addListener(this);
			this.attachChild(m_oResvDetailBasket);
		} else {
			m_oListResvDetail = new VirtualUIList();
			m_oTemplateBuilder.buildList(m_oListResvDetail, "listResvDetail");
			this.attachChild(m_oListResvDetail);
		}

		// Create OK button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "btnOK");
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("ok"));
		this.attachChild(m_oButtonOK);	
		
		m_iResvDetailContent = 0;
	}

	public void updateDetail(ResvResv oResv) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
		//Add the check list title
		ArrayList<Integer> iFieldWidth = new ArrayList<Integer>();
		ArrayList<String[]> sFieldValue = new ArrayList<String[]>();

		String langCode = "en";
		int langIndex = 1;
		for(HashMap<String, String> oLangInfo:AppGlobal.g_oSupportedLangList) {
			langIndex = Integer.parseInt(oLangInfo.get("index"));
			if(langIndex == AppGlobal.g_oCurrentLangIndex.get()) {
				langCode = oLangInfo.get("code");
				break;
			}
		}
		SimpleDateFormat timeFormat;
		if(langCode.equals("zh-hk"))
			timeFormat = new SimpleDateFormat("hh:mm a", Locale.CHINESE);
		else
			if(langCode.equals("zh-cn"))
				timeFormat = new SimpleDateFormat("hh:mm a", Locale.CHINA);
			else
				if(langCode.equals("jpn"))
					timeFormat = new SimpleDateFormat("hh:mm a", Locale.JAPAN);
				else
					if(langCode.equals("kor"))
						timeFormat = new SimpleDateFormat("hh:mm a", Locale.KOREA);
					else
						timeFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
		
		VirtualUILabel oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelTitle, "lblListTitle");

		VirtualUILabel oLabelContent = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelContent, "lblContent");
		
		//Fields Width
  		List<Integer> oFieldsWidth = new ArrayList<Integer>();
		oFieldsWidth.add(oLabelTitle.getWidth());
		oFieldsWidth.add(oLabelContent.getWidth());
		
		//Add Check Balance Result Common Basket Header
		iFieldWidth.add(oFieldsWidth.get(0));
		sFieldValue.add(AppGlobal.g_oLang.get()._("confirmation_number", ""));
		iFieldWidth.add(oFieldsWidth.get(1));
		String[] sRefNoWithPrefix = new String[5];
		for (int i = 0; i < AppGlobal.LANGUAGE_COUNT; i++)
			sRefNoWithPrefix[i] = oResv.getRefNoWithPrefix();
		sFieldValue.add(sRefNoWithPrefix);
		
		// Confirmation number
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())){
			m_oResvDetailBasket.addHeader(iFieldWidth, sFieldValue);
			m_oResvDetailBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		} else 
			addResvDetail(AppGlobal.g_oLang.get()._("confirmation_number"), oResv.getRefNoWithPrefix());
		
		// Booking Date
		addResvDetail(AppGlobal.g_oLang.get()._("booking_date"), dateTimeFormat.print(oResv.getBookDate()));
		// Booking Time
		addResvDetail(AppGlobal.g_oLang.get()._("booking_time"), timeFormat.format(oResv.getBookTime()));
		// Payment Amount
		addResvDetail(AppGlobal.g_oLang.get()._("total_payment"), AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(oResv.getPaymentTotal()));
		// Guest
		String sGuest;
		int iAdultNum = oResv.getGuests() - oResv.getChildren();
		int iChildrenNum = oResv.getChildren();
		if(iAdultNum > 1)
			sGuest = iAdultNum + " " + AppGlobal.g_oLang.get()._("adults");
		else
			sGuest = iAdultNum + " " + AppGlobal.g_oLang.get()._("adult");

		if(iChildrenNum > 0) {
			sGuest += ", ";
			if(iChildrenNum > 1)
				sGuest += oResv.getChildren() + " " + AppGlobal.g_oLang.get()._("children");
			else
				sGuest += oResv.getChildren() + " " + AppGlobal.g_oLang.get()._("child");
		}
		addResvDetail(AppGlobal.g_oLang.get()._("cover"), sGuest);
		// Name
		addResvDetail(AppGlobal.g_oLang.get()._("name"), oResv.getLastName()+", "+oResv.getFirstName());
		// Salutation
		String sSalutation;
		if(oResv.getSalutation().equals("mr"))
			sSalutation = AppGlobal.g_oLang.get()._("mr.");
		else if(oResv.getSalutation().equals("mrs"))
			sSalutation = AppGlobal.g_oLang.get()._("mrs.");
		else if(oResv.getSalutation().equals("miss"))
			sSalutation = AppGlobal.g_oLang.get()._("miss");
		else if(oResv.getSalutation().equals("dr"))
			sSalutation = AppGlobal.g_oLang.get()._("dr");
		else if(oResv.getSalutation().equals("prof"))
			sSalutation = AppGlobal.g_oLang.get()._("prof");
		else if(oResv.getSalutation().equals("phd"))
			sSalutation = AppGlobal.g_oLang.get()._("phd");
		else    		
			sSalutation = "";
		addResvDetail(AppGlobal.g_oLang.get()._("salutation"), sSalutation);
		// Phone Number
		addResvDetail(AppGlobal.g_oLang.get()._("phone_number"), oResv.getPhone());
		// Request
		addResvDetail(AppGlobal.g_oLang.get()._("request"), oResv.getRequest());
		// Remark
		addResvDetail(AppGlobal.g_oLang.get()._("remark"), oResv.getRemark());
	}

	private void addResvDetail(String sTitle, String sContent) {
		
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())){
			ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
			ArrayList<String> sFieldValues = new ArrayList<String>();
			ArrayList<String> sFieldAligns = new ArrayList<String>();
			ArrayList<String> sFieldTypes = new ArrayList<String>();
			
			VirtualUILabel oLabelTitle = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabelTitle, "lblListTitle");
	
			VirtualUILabel oLabelContent = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabelContent, "lblContent");
			
			//Fields Width
			iFieldWidths.add(oLabelTitle.getWidth());
			iFieldWidths.add(oLabelContent.getWidth());
			
			//Fields Value
			sFieldValues.add(sTitle);
			sFieldValues.add(sContent);
			
			//Fields Aligns
			sFieldAligns.add("");
			sFieldAligns.add("");
			
			//Fields
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			m_oResvDetailBasket.addItem(0, m_iResvDetailContent, oLabelTitle.getHeight(), iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
			
			m_iResvDetailContent++;
		} else {
			VirtualUIFrame oFrameBasicDetail = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(oFrameBasicDetail, "fraResvDetail");

			VirtualUILabel oLabelTitle = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabelTitle, "lblListTitle");
			oLabelTitle.setValue(sTitle);
			oFrameBasicDetail.attachChild(oLabelTitle);

			VirtualUILabel oLabelContent = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabelContent, "lblContent");
			oLabelContent.setValue(sContent);
			oFrameBasicDetail.attachChild(oLabelContent);

			m_oListResvDetail.attachChild(oFrameBasicDetail);
		}
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (iChildId == m_oButtonOK.getId()) {
			for (FrameReservationDetailListener listener : listeners) {
				// Raise the event to parent
				listener.frameReservationDetail_clickOK();
				break;
			}
			bMatchChild = true;
		}

		return bMatchChild;
	}

	@Override
	public void FrameTitleHeader_close() {
		for (FrameReservationDetailListener listener : listeners) {
			// Raise the event to parent
			listener.frameReservationDetail_clickOK();
			break;
		}
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
