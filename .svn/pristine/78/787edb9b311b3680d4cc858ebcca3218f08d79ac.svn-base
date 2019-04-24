package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import commonui.FrameTitleHeader;
import externallib.StringLib;
import om.PosInterfaceConfig;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

public class FrameMembershipInterfaceForSPC extends FrameMembershipInterface {
	
	public void init(PosInterfaceConfig oPosInterfaceConfig, String sDefaultValue, String sOperation, String sTitleName, boolean bShowInFloorPlan, String sTableNo) {
		m_oPosInterfaceConfig = oPosInterfaceConfig;
		m_sInterfaceKey = oPosInterfaceConfig.getInterfaceVendorKey();
		m_sTableNo = sTableNo;
		m_bShowInFloorPlan = bShowInFloorPlan;
		m_sCurrentOperation = sOperation;
		
		String sXmlFilename = "fraMembershipInterface_spc.xml";
		
		m_oTemplateBuilder.loadTemplate(sXmlFilename);
		
		// Load child elements from template
		/////////////////////////////////////////////////////////////////
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("search_membership_member"));
		if (m_sCurrentOperation.equals(OPERATION_SET_MEMBER_DISCOUNT))
			m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("set_member_discount"));
		m_oTitleHeader.addListener(this);
		this.attachChild(m_oTitleHeader);
		
		// Enquiry panel
		VirtualUIFrame oFrameEnquiry = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameEnquiry, "fraEnquiry");
		this.attachChild(oFrameEnquiry);
		
		// Enquiry textbox
		m_oTxtboxEnquiryNumber = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTxtboxEnquiryNumber, "txtboxEnquiry");
		m_oTxtboxEnquiryNumber.setFocusWhenShow(true);
		oFrameEnquiry.attachChild(m_oTxtboxEnquiryNumber);
		
		// Enquiry button
		m_oButtonEnquiry = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonEnquiry, "butMemberEnquiry");
		m_oButtonEnquiry.addClickServerRequestSubmitElement(m_oTxtboxEnquiryNumber);
		m_oButtonEnquiry.setValue(AppGlobal.g_oLang.get()._("enquiry"));
		if(m_iRequestTimeout > 0)	//use the default value if m_iRequestTimeout == 0
			m_oButtonEnquiry.setClickServerRequestTimeout((m_iRequestTimeout + 10000));
		m_oButtonEnquiry.setClickServerRequestBlockUI(true);
		oFrameEnquiry.attachChild(m_oButtonEnquiry);
		
		// Apply discount button
		m_oButtonSetMember = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSetMember, "butSetMember");
		m_oButtonSetMember.setValue(AppGlobal.g_oLang.get()._("apply_discount"));
		m_oButtonSetMember.setClickServerRequestBlockUI(true);
		m_oButtonSetMember.setVisible(false);
		oFrameEnquiry.attachChild(m_oButtonSetMember);
		
		// Member number tag
		VirtualUILabel oLabelTag = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelTag, "lblMemberNumberTag");
		oLabelTag.setValue(AppGlobal.g_oLang.get()._("member_number") + ": ");
		oFrameEnquiry.attachChild(oLabelTag);

		// Enquiry tag
		VirtualUILabel oLabelCouponListTag = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelCouponListTag, "lblEnquiryTag");
		oLabelCouponListTag.setValue(AppGlobal.g_oLang.get()._("enquiry"));
		oFrameEnquiry.attachChild(oLabelCouponListTag);
		
		// Enquiry Indicator
		VirtualUIFrame oEnquiryTagIndicator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oEnquiryTagIndicator, "fraEnquiryIndicator");
		oFrameEnquiry.attachChild(oEnquiryTagIndicator);
		
		// Member Detail Panel
		VirtualUIFrame oMemberDetailPanel = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oMemberDetailPanel, "fraMemberDetail");
		this.attachChild(oMemberDetailPanel);
		
		// Member details
		VirtualUILabel oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelTitle, "lblMemberRecordTitle");
		oLabelTitle.setValue(AppGlobal.g_oLang.get()._("member_detail"));
		this.attachChild(oLabelTitle);
		
		// Member details indicator
		VirtualUIFrame oMemberDetailTagIndicator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oMemberDetailTagIndicator, "fraMemberDetailTagIndicator");
		this.attachChild(oMemberDetailTagIndicator);
		
		// List common basket
		m_oMemberListCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oMemberListCommonBasket, "fraMemberListCommonBasket");
		m_oMemberListCommonBasket.init();
		m_oMemberListCommonBasket.addListener(this);
		oMemberDetailPanel.attachChild(m_oMemberListCommonBasket);
	}
	
	public void cleanupMemberDetail() {
		m_oMemberListCommonBasket.removeAllSections();
	}
	
	public void cleanupAllTextBox() {
		m_oTxtboxEnquiryNumber.setValue("");
	}
	
	public void updateMemberDetail(LinkedHashMap<String, String> oMemberDetials) {
		// Clear the member details
		cleanupMemberDetail();
		
		m_oMemberListCommonBasket.setVisible(true);
		m_oMemberListCommonBasket.bringToTop();
		m_oMemberListCommonBasket.clearAllSections();
		m_oMemberListCommonBasket.setHeaderVisible(false);
		m_oMemberListCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		m_oMemberListCommonBasket.setBottomUnderlineVisible(false);
		
		int iCount = 0;
		for (Entry<String, String> entry : oMemberDetials.entrySet()) {
			addMemberDetail(entry.getKey(), entry.getValue(), iCount);
			iCount++;
		}
	}

	private void addMemberDetail(String sTitle, String sContent, int iIndex) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		
		iFieldWidths.add(270);
		sFieldValues.add(sTitle);
		sFieldAligns.add("");
		
		iFieldWidths.add(340);
		sFieldValues.add(sContent);
		sFieldAligns.add("");
		
		m_oMemberListCommonBasket.addItem(0, iIndex, 70, iFieldWidths, sFieldValues, sFieldAligns, null, null);
	}
	
	public void updateAfterEnquiryButtonIsClicked() {
		if (m_sCurrentOperation.equals(FrameMembershipInterface.OPERATION_SET_MEMBER_DISCOUNT))
			m_oButtonSetMember.setVisible(m_bShowInFloorPlan ? false : true);
	}
	
	//Build the enquiry structure before posting
	private HashMap<String, String> formEnquiryInfo(String sCardNumber) {
		HashMap<String, String> oEnquiryInfo = new HashMap<String, String>();
		oEnquiryInfo.put("cardNumber", sCardNumber.trim());
		return oEnquiryInfo;
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (iChildId == m_oButtonEnquiry.getId()) {
			for (FrameMembershipInterfaceListener listener : listeners) {
				//Raise the event to parent
				AppGlobal.g_oTerm.get().hideKeyboard();
				listener.frameMembershipInterfaceResult_clickEnquiry(this.formEnquiryInfo(m_oTxtboxEnquiryNumber.getValue()));
				break;
			}
			bMatchChild = true;
		} else if (iChildId == m_oButtonSetMember.getId()) {
			for (FrameMembershipInterfaceListener listener : listeners) {
				// Raise the event to parent
				listener.frameMembershipInterfaceResult_clickApplyDisc();
				break;
			}
			bMatchChild = true;
		}
		return bMatchChild;
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
					boolean bProceedEnquiry = true;
					sSwipeCardValue = oActiveClient.getSwipeCardReaderElement().getValue().replace("\r", "").replace("\n", "");
					
					// MSR card format handling
					String sMSRCode = getMSRCode(m_oPosInterfaceConfig);
					if (sMSRCode != null && !sMSRCode.isEmpty()) {
						PosInterfaceConfig oMSRPosInterfaceConfig = getMSRConfigByMSRCode(sMSRCode);
						if (oMSRPosInterfaceConfig != null) {
							String sMsrCardNo = getMsrCardNo(sSwipeCardValue, oMSRPosInterfaceConfig);
							sSwipeCardValue = sMsrCardNo.isEmpty() ? sSwipeCardValue : sMsrCardNo;
						} else {
							// Special handling for MSR code set but no msr interface found / not attached to outlet, not proceed enquiry
							showDialogBox(AppGlobal.g_oLang.get()._("error"), AppGlobal.g_oLang.get()._("msr_setup_not_found"));
							bProceedEnquiry = false;
						}
					}
					
					if (bProceedEnquiry) {
						for (FrameMembershipInterfaceListener listener : listeners) {
							listener.frameMembershipInterfaceResult_clickEnquiry(this.formEnquiryInfo(sSwipeCardValue));
							break;
						}
					}
				}
				bMatchChild = true;
			}
		}
		return bMatchChild;
	}
}
