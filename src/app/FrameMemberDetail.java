package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import externallib.StringLib;
import externallib.Util;
import om.InfVendor;
import om.MemMember;
import om.PosInterfaceConfig;
import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FrameMemberDetailListener {
	void frameMemberDetail_clickSetMember();
	void frameMemberDetail_clickClearMember();
}

public class FrameMemberDetail extends VirtualUIFrame implements FrameCommonPageContainerListener, FrameHorizontalTabListListener {
	private TemplateBuilder m_oTemplateBuilder;

	private FrameHorizontalTabList m_oFrameHorizontalTabList;
	private VirtualUIFrame m_oFrameoMemberInfo;
	private VirtualUIButton m_oButtonSetMember;
	private VirtualUIButton m_oButtonClearMember;
	private FrameCommonBasket m_oMemberDetailCommonBasket;
	
	private VirtualUIImage m_oImageMemberPhoto;
	private VirtualUIImage m_oImageMemberSign;
	
	// Tab "Signature"
	private VirtualUIFrame m_oFrameEsignatureInfo;
	private VirtualUIFrame m_oFraSeperateLine;
	private VirtualUILabel m_oLabelEsignature;
	private VirtualUILabel m_oLabelSignPhoto;
	private VirtualUIImage m_oImageEsignature;
	private VirtualUIImage m_oImageSignPhoto;
	
	private VirtualUIFrame m_oFrameImage;
	private FrameCommonPageContainer m_oCommonPageTabList;
	private VirtualUIFrame m_oCommonPageSelectedTabList;
	private VirtualUIFrame m_oCommonPageUnselectTabList;
	private boolean m_oClearMemberButtonVisible;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameMemberDetailListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameMemberDetailListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameMemberDetailListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	//Constructor
	public FrameMemberDetail() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameMemberDetailListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraMemberDetail.xml");
	}
	
	public void init(boolean bShowClearMemberButton, String sEsignatureImg, PosInterfaceConfig oPosInterfaceConfig) {
		// Load child elements from template
		// Horizontal List
		m_oFrameHorizontalTabList = new FrameHorizontalTabList();
		m_oTemplateBuilder.buildFrame(m_oFrameHorizontalTabList, "fraHorizontalList");
		m_oFrameHorizontalTabList.init();
		m_oFrameHorizontalTabList.addListener(this);
		
		m_oCommonPageSelectedTabList = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oCommonPageSelectedTabList, "fraSelectedListTab");
		this.attachChild(m_oCommonPageSelectedTabList);
		
		m_oCommonPageUnselectTabList = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oCommonPageUnselectTabList, "fraUnselectListTab");
		this.attachChild(m_oCommonPageUnselectTabList);
		
		m_oCommonPageTabList = new FrameCommonPageContainer();
		m_oTemplateBuilder.buildFrame(m_oCommonPageTabList, "fraListTab");
		m_oCommonPageTabList.init(m_oCommonPageTabList.getWidth(), m_oCommonPageTabList.getHeight(), 
				m_oCommonPageSelectedTabList.getWidth(), m_oCommonPageSelectedTabList.getHeight(), 4, 
				m_oCommonPageSelectedTabList.getForegroundColor(), m_oCommonPageUnselectTabList.getForegroundColor(), 
				m_oCommonPageSelectedTabList.getBackgroundColor(), m_oCommonPageUnselectTabList.getBackgroundColor(), 
				48, false, true);
		m_oCommonPageTabList.setUnderlineColor("#0055B8");
		m_oCommonPageTabList.addListener(this);
		this.attachChild(m_oCommonPageTabList);
		
		
		List<String> oTabNameList = new ArrayList<String>();
		oTabNameList.add(AppGlobal.g_oLang.get()._("basic_information"));
		
		if(sEsignatureImg != null && !sEsignatureImg.isEmpty())
			oTabNameList.add(AppGlobal.g_oLang.get()._("signature"));
		
		m_oFrameHorizontalTabList.addPageTabs(oTabNameList);

		m_oFrameoMemberInfo = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameoMemberInfo, "fraMemberInfo");

		m_oButtonSetMember = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSetMember, "butSetMember");
		m_oButtonSetMember.setValue(AppGlobal.g_oLang.get()._("set_member"));
		m_oButtonSetMember.setVisible(false);
		m_oFrameoMemberInfo.attachChild(m_oButtonSetMember);
		
		m_oButtonClearMember = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonClearMember, "butClearMember");
		m_oButtonClearMember.setValue(AppGlobal.g_oLang.get()._("clear_member"));
		m_oButtonClearMember.setVisible(bShowClearMemberButton);
		m_oFrameoMemberInfo.attachChild(m_oButtonClearMember);
		this.attachChild(m_oFrameoMemberInfo);
		m_oFrameoMemberInfo.setBackgroundColor("#FF0000");
		
		m_oFrameEsignatureInfo = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameEsignatureInfo, "fraEsignatureInfo");
		
		//frame common basket
		m_oMemberDetailCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oMemberDetailCommonBasket, "fraCommonBasket");
		m_oMemberDetailCommonBasket.init();
		m_oFrameoMemberInfo.attachChild(m_oMemberDetailCommonBasket);
		
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		iFieldWidths.add(m_oMemberDetailCommonBasket.getWidth() / 2);
		sFieldValues.add(AppGlobal.g_oLang.get()._("information_type"));
		iFieldWidths.add(m_oMemberDetailCommonBasket.getWidth() / 2);
		sFieldValues.add(AppGlobal.g_oLang.get()._("information_detail"));
		
		m_oMemberDetailCommonBasket.addHeader(iFieldWidths, sFieldValues);
		m_oMemberDetailCommonBasket.setHeaderFormat(40, 0, "");
		m_oMemberDetailCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		//E-Signature label
		m_oLabelEsignature = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelEsignature, "lblSignature");
		m_oLabelEsignature.setValue(AppGlobal.g_oLang.get()._("user_signed_e_signtaure")+": ");
		m_oLabelEsignature.setVisible(true);
		m_oFrameEsignatureInfo.attachChild(m_oLabelEsignature);
		
		//E-signature Image
		if(sEsignatureImg != null && !sEsignatureImg.isEmpty()) {
			m_oImageEsignature = new VirtualUIImage();
			m_oTemplateBuilder.buildImage(m_oImageEsignature, "imageEsignature");
			m_oImageEsignature.setSource(sEsignatureImg);
			m_oImageEsignature.setVisible(true);
			m_oFrameEsignatureInfo.attachChild(m_oImageEsignature);
		}
		
		//Create Underline frame
		m_oFraSeperateLine = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFraSeperateLine, "fraSeperateLine");
		m_oFraSeperateLine.setVisible(true);
		m_oFrameEsignatureInfo.attachChild(m_oFraSeperateLine);
		
		//Sign label
		m_oLabelSignPhoto = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSignPhoto, "lblSignPhoto");
		m_oLabelSignPhoto.setValue(AppGlobal.g_oLang.get()._("member_signature")+": ");
		m_oLabelSignPhoto.setVisible(true);
		m_oFrameEsignatureInfo.attachChild(m_oLabelSignPhoto);
		
		//Sign Image from server
		m_oImageSignPhoto = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageSignPhoto, "imageSignPhoto");
		m_oImageSignPhoto.setVisible(true);
		m_oFrameEsignatureInfo.attachChild(m_oImageSignPhoto);
		
		//Image Frame
		m_oFrameImage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameImage, "fraPhotoInfo");
		
		m_oImageMemberPhoto = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageMemberPhoto, "imagePhoto");
		m_oImageMemberPhoto.setVisible(false);
		m_oFrameImage.attachChild(m_oImageMemberPhoto);
			
		m_oImageMemberSign = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageMemberSign, "imageSign");
		m_oImageMemberSign.setVisible(false);
		m_oFrameImage.attachChild(m_oImageMemberSign);

		m_oCommonPageTabList.addButton(AppGlobal.g_oLang.get()._("basic_information"), m_oFrameoMemberInfo);
		
		if (oPosInterfaceConfig!= null && oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_ASPEN)) {
			m_oCommonPageTabList.addButton(AppGlobal.g_oLang.get()._("photo"), m_oFrameImage);
			if (sEsignatureImg != null) 
				m_oCommonPageTabList.addButton(AppGlobal.g_oLang.get()._("signature"), m_oFrameEsignatureInfo);
		}
	}
	
	public void changePageTab(int iTagIndex, boolean bUpdateSelectedTabColor) {
		if (bUpdateSelectedTabColor)
			m_oFrameHorizontalTabList.changePageTab(iTagIndex);
		
		if (iTagIndex == 0) {
			m_oFrameoMemberInfo.setVisible(true);
			if(m_oImageMemberPhoto.getSource() != null && !m_oImageMemberPhoto.getSource().isEmpty())
				m_oImageMemberPhoto.setVisible(true);
			
			if(m_oImageMemberSign.getSource() != null && !m_oImageMemberSign.getSource().isEmpty())
				m_oImageMemberSign.setVisible(true);
			
			m_oFrameEsignatureInfo.setVisible(false);
		} else {
			m_oFrameoMemberInfo.setVisible(false);
			m_oImageMemberPhoto.setVisible(false);
			m_oImageMemberSign.setVisible(false);
			m_oFrameEsignatureInfo.setVisible(true);
			
			//for offline member
			if(m_oImageMemberSign.getSource() == null || m_oImageMemberSign.getSource().isEmpty()){
				m_oFraSeperateLine.setVisible(false);
				m_oLabelSignPhoto.setVisible(false);
				m_oImageSignPhoto.setVisible(false);
			}
		}
	}
		
	public void updateDetail(MemMember oMember) {
		ArrayList<String> sMessage = new ArrayList<>();
		ArrayList<String> sMessageTitle = new ArrayList<>();
		
		if(!oMember.getMemberNo().isEmpty()) {
			sMessageTitle.add(AppGlobal.g_oLang.get()._("member_no"));
			sMessage.add(oMember.getMemberNo());
		}
		
		if(!oMember.getLastName(AppGlobal.g_oCurrentLangIndex.get()).isEmpty() 
				&& !oMember.getFirstName(AppGlobal.g_oCurrentLangIndex.get()).isEmpty()) {
			sMessageTitle.add(AppGlobal.g_oLang.get()._("member_name"));
			sMessage.add( oMember.getLastName(AppGlobal.g_oCurrentLangIndex.get())+" "+oMember.getFirstName(AppGlobal.g_oCurrentLangIndex.get()));
		}
		m_oMemberDetailCommonBasket.removeAllItems(0);
		m_oImageMemberPhoto.setVisible(false);
		m_oImageMemberSign.setVisible(false);
		
		if (oMember.isNormalMember()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("type"));
			sMessage.add(AppGlobal.g_oLang.get()._("normal_member"));
		}
		else if(oMember.isEmployeeMember()) {
			sMessageTitle.add(AppGlobal.g_oLang.get()._("type"));
			sMessage.add(AppGlobal.g_oLang.get()._("employee_member"));
		}else if(oMember.isCompanyMember()) {
			sMessageTitle.add(AppGlobal.g_oLang.get()._("type"));
			sMessage.add(AppGlobal.g_oLang.get()._("company_account"));
		}
		
		if(!oMember.getSalutation().isEmpty()) {
			sMessageTitle.add(AppGlobal.g_oLang.get()._("salutation"));
			sMessage.add(this.getSalutationLang(oMember.getSalutation()));
		}
		
		if(!oMember.getGender().isEmpty()) {
			sMessageTitle.add(AppGlobal.g_oLang.get()._("gender"));
			sMessage.add(this.getGenderLang(oMember.getGender()));
		}
		
		if(!oMember.getDateOfBirth().isEmpty()) {
			sMessageTitle.add(AppGlobal.g_oLang.get()._("date_of_birth"));
			sMessage.add(oMember.getDateOfBirth());
		}
		
		if(!oMember.getEmail().isEmpty()) {
			sMessageTitle.add(AppGlobal.g_oLang.get()._("email"));
			sMessage.add(oMember.getEmail());
		}
		
		if(!oMember.getMobile().isEmpty()) {
			sMessageTitle.add(AppGlobal.g_oLang.get()._("mobile"));
			sMessage.add(oMember.getMobile());
		}
		
		if(!oMember.getHomePhone().isEmpty()) {
			sMessageTitle.add(AppGlobal.g_oLang.get()._("home_phone_no"));
			sMessage.add(oMember.getHomePhone());
		}
		
		if(!oMember.getOfficePhone().isEmpty()) {
			sMessageTitle.add(AppGlobal.g_oLang.get()._("office_phone_no"));
			sMessage.add(oMember.getOfficePhone());
		}
		
		BigDecimal dSpending = (oMember.getMemModuleInfoByVariable("life_time_spending") == null)? BigDecimal.ZERO: new BigDecimal(oMember.getMemModuleInfoByVariable("life_time_spending"));
		sMessageTitle.add(AppGlobal.g_oLang.get()._("member_spending"));
		sMessage.add(Util.HERORound(dSpending, AppGlobal.g_oFuncOutlet.get().getCheckRoundMethod(), AppGlobal.g_oFuncOutlet.get().getCheckRoundDecimal()).toPlainString());
		
		// Card number
		if(!oMember.getCardNumber().isEmpty()) {
			sMessageTitle.add(AppGlobal.g_oLang.get()._("card_number"));
			sMessage.add(oMember.getCardNumber());
		}
		
		HashMap<String, String> oMemberAliasInfoList = oMember.getMemModuleInfoListByAlias("member");
		if(oMemberAliasInfoList.size() > 0) {
			// Card Surname
			if(oMemberAliasInfoList.containsKey("CARD_SUR") && !oMemberAliasInfoList.get("CARD_SUR").isEmpty()) {
				sMessageTitle.add(AppGlobal.g_oLang.get()._("card_surname"));
				sMessage.add(oMemberAliasInfoList.get("CARD_SUR"));
			}
			
			// Card Given name
			if(oMemberAliasInfoList.containsKey("CARD_GIV") && !oMemberAliasInfoList.get("CARD_GIV").isEmpty()) {
				sMessageTitle.add(AppGlobal.g_oLang.get()._("card_given_name"));
				sMessage.add(oMemberAliasInfoList.get("CARD_GIV"));
			}
			
			// Card Alias
			if(oMemberAliasInfoList.containsKey("CARD_ALIAS") && !oMemberAliasInfoList.get("CARD_ALIAS").isEmpty()) {
				sMessageTitle.add(AppGlobal.g_oLang.get()._("card_alias"));
				sMessage.add(oMemberAliasInfoList.get("CARD_ALIAS"));
			}
			
			// Membership type code
			if(oMemberAliasInfoList.containsKey("MTYP_CODE") && !oMemberAliasInfoList.get("MTYP_CODE").isEmpty()) {
				sMessageTitle.add(AppGlobal.g_oLang.get()._("member_type_code"));
				sMessage.add(oMemberAliasInfoList.get("MTYP_CODE"));
			}
			
			// Membership status
			if(oMemberAliasInfoList.containsKey("MEMB_STAT") && !oMemberAliasInfoList.get("MEMB_STAT").isEmpty()) {
				sMessageTitle.add(AppGlobal.g_oLang.get()._("member_status"));
				sMessage.add(oMemberAliasInfoList.get("MEMB_STAT"));
			}
			
			// Member info
			if(oMemberAliasInfoList.containsKey("MEMB_INFO1") && !oMemberAliasInfoList.get("MEMB_INFO1").isEmpty()) {
				sMessageTitle.add(AppGlobal.g_oLang.get()._("member_information"));
				sMessage.add(oMemberAliasInfoList.get("MEMB_INFO1"));
			}
			
			// Card mobile
			if(oMemberAliasInfoList.containsKey("CARD_MOBIL") && !oMemberAliasInfoList.get("CARD_MOBIL").isEmpty()) {
				sMessageTitle.add(AppGlobal.g_oLang.get()._("card_mobile"));
				sMessage.add(oMemberAliasInfoList.get("CARD_MOBIL"));
			}
			
			// Card salutation
			if(oMemberAliasInfoList.containsKey("CARD_SAL") && !oMemberAliasInfoList.get("CARD_SAL").isEmpty()) {
				sMessageTitle.add(AppGlobal.g_oLang.get()._("card_salutation"));
				sMessage.add(oMemberAliasInfoList.get("CARD_SAL"));
			}
			
			// Card credit allowance
			if(oMemberAliasInfoList.containsKey("CARD_CHG") && !oMemberAliasInfoList.get("CARD_CHG").isEmpty()) {
				sMessageTitle.add(AppGlobal.g_oLang.get()._("allow_charge"));
				if(oMemberAliasInfoList.get("CARD_CHG").toLowerCase().equals("y"))
					sMessage.add(AppGlobal.g_oLang.get()._("yes"));
				else
					sMessage.add(AppGlobal.g_oLang.get()._("no"));
			}
			
			// Card credit limit
			if(oMemberAliasInfoList.containsKey("CARD_LIMIT") && !oMemberAliasInfoList.get("CARD_LIMIT").isEmpty()) {
				sMessageTitle.add(AppGlobal.g_oLang.get()._("card_limit"));
				sMessage.add(Util.HERORound(new BigDecimal(oMemberAliasInfoList.get("CARD_LIMIT")), AppGlobal.g_oFuncOutlet.get().getCheckRoundMethod(), AppGlobal.g_oFuncOutlet.get().getCheckRoundDecimal()).toPlainString());
			}
			
			// Credit usage
			if(oMemberAliasInfoList.containsKey("MEMB_OS") && !oMemberAliasInfoList.get("MEMB_OS").isEmpty()) {
				sMessageTitle.add(AppGlobal.g_oLang.get()._("credit_usage"));
				sMessage.add(oMemberAliasInfoList.get("MEMB_OS"));
			}
		}
		
		// member address
		String sMemberAddress = oMember.getAddress(AppGlobal.g_oCurrentLangIndex.get());
		if(!sMemberAddress.isEmpty()) {
			sMessageTitle.add(AppGlobal.g_oLang.get()._("member_address"));
			sMessage.add(sMemberAddress);
		}
		
		addMemberDetailToCommonBasket(sMessageTitle, sMessage);
		
		m_oButtonSetMember.setVisible(true);
		
	}
	
	public void updateOnlineDetail(HashMap<String, String> oMemberInfo) {
		m_oImageMemberPhoto.setVisible(false);
		m_oImageMemberSign.setVisible(false);
		
		m_oMemberDetailCommonBasket.removeAllItems(0);
		
		//check image first
		if(oMemberInfo.containsKey("photoFileName") && !oMemberInfo.get("photoFileName").isEmpty()){
			m_oImageMemberPhoto.setSource(oMemberInfo.get("photoFileName"));
			m_oImageMemberPhoto.setVisible(true);
		}
		if(oMemberInfo.containsKey("signatureFileName") && !oMemberInfo.get("signatureFileName").isEmpty()){
			m_oImageMemberSign.setSource(oMemberInfo.get("signatureFileName"));
			m_oImageMemberSign.setVisible(true);
			m_oImageSignPhoto.setSource(oMemberInfo.get("signatureFileName"));
		}
		
		ArrayList<String> sMessage = new ArrayList<>();
		ArrayList<String> sMessageTitle = new ArrayList<>();
		
		// common information
		sMessageTitle.add(AppGlobal.g_oLang.get()._("member_no"));
		sMessage.add(oMemberInfo.get("memberNumber"));
		
		sMessageTitle.add(AppGlobal.g_oLang.get()._("member_name"));
		sMessage.add(oMemberInfo.get("memberName"));
		
		// optional information
		if(oMemberInfo.containsKey("memberType") && !oMemberInfo.get("memberType").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("member_type"));
			sMessage.add(oMemberInfo.get("memberType"));
		}
		
		if(oMemberInfo.containsKey("cardTypeName") && !oMemberInfo.get("cardTypeName").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("card_type"));
			sMessage.add(oMemberInfo.get("cardTypeName"));
		}
		
		if(oMemberInfo.containsKey("arAccountNumber") && !oMemberInfo.get("arAccountNumber").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("ar_account_number"));
			sMessage.add(oMemberInfo.get("arAccountNumber"));
		}
		
		if(oMemberInfo.containsKey("creditLimit") && !oMemberInfo.get("creditLimit").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("credit_limit"));
			sMessage.add(oMemberInfo.get("creditLimit"));
		}
		
		if(oMemberInfo.containsKey("creditUsage") && !oMemberInfo.get("creditUsage").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("credit_usage"));
			sMessage.add(oMemberInfo.get("creditUsage"));
		}
		
		if(oMemberInfo.containsKey("cardNumber") && !oMemberInfo.get("cardNumber").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("card_no"));
			sMessage.add(oMemberInfo.get("cardNumber"));
		}
		
		if(oMemberInfo.containsKey("cardAlias") && !oMemberInfo.get("cardAlias").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("card_alias"));
			sMessage.add(oMemberInfo.get("cardAlias"));
		}
		
		if(oMemberInfo.containsKey("cardAge") && !oMemberInfo.get("cardAge").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("card_age"));
			sMessage.add(oMemberInfo.get("cardAge"));
		}
		
		if(oMemberInfo.containsKey("debCardBalance") && !oMemberInfo.get("debCardBalance").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("debit_card_balance"));
			sMessage.add(oMemberInfo.get("debCardBalance"));
		}
			
		if(oMemberInfo.containsKey("salutation") && !oMemberInfo.get("salutation").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("salutation"));
			sMessage.add(this.getSalutationLang(oMemberInfo.get("salutation")));
		}
		
		if(oMemberInfo.containsKey("gender") && !oMemberInfo.get("gender").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("gender"));
			sMessage.add(oMemberInfo.get("gender"));
		}
		
		if(oMemberInfo.containsKey("memberStatus") && !oMemberInfo.get("memberStatus").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("status"));
			sMessage.add(oMemberInfo.get("memberStatus"));
		}
		
		if(oMemberInfo.containsKey("birthday") && !oMemberInfo.get("birthday").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("birthday"));
			sMessage.add(oMemberInfo.get("birthday"));
		}
		
		if(oMemberInfo.containsKey("expiryDate") && !oMemberInfo.get("expiryDate").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("expiry_date"));
			sMessage.add(oMemberInfo.get("expiryDate"));
		}
		
		if(oMemberInfo.containsKey("address") && !oMemberInfo.get("address").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("address"));
			sMessage.add(oMemberInfo.get("address"));
		}
		
		if(oMemberInfo.containsKey("mobileNumber") && !oMemberInfo.get("mobileNumber").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("mobile"));
			sMessage.add(oMemberInfo.get("mobileNumber"));
		}
		
		if(oMemberInfo.containsKey("memberEthnicity") && !oMemberInfo.get("memberEthnicity").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("ethnicity"));
			sMessage.add(oMemberInfo.get("memberEthnicity"));
		}
		
		if(oMemberInfo.containsKey("allowCharge") && !oMemberInfo.get("allowCharge").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("allow_charge"));
			if(oMemberInfo.get("allowCharge").equals("true"))
				sMessage.add(AppGlobal.g_oLang.get()._("yes"));
			else
				sMessage.add(AppGlobal.g_oLang.get()._("no"));
		}
		
		if(oMemberInfo.containsKey("activityCode") && !oMemberInfo.get("activityCode").isEmpty()){
			sMessageTitle.add(AppGlobal.g_oLang.get()._("sub_member_of"));
			sMessage.add(oMemberInfo.get("activityCode"));
		}
		
		for(int i=1; i<100 ; i++){
			if(oMemberInfo.containsKey("memberInfo"+i) && !oMemberInfo.get("memberInfo"+i).isEmpty()) {
				sMessageTitle.add(AppGlobal.g_oLang.get()._("member_information"));
				sMessage.add(oMemberInfo.get("memberInfo"+i));
			}
		}
		
		addMemberDetailToCommonBasket(sMessageTitle, sMessage);
		
		m_oButtonSetMember.setVisible(true);
	}
		
	private String getSalutationLang(String sSource){
		String sSalutation = "";
		if(sSource.equals("mr"))
			sSalutation = AppGlobal.g_oLang.get()._("mr.");
		else if(sSource.equals("mrs"))
			sSalutation = AppGlobal.g_oLang.get()._("mrs.");
		else if(sSource.equals("miss"))
			sSalutation = AppGlobal.g_oLang.get()._("miss");
		else if(sSource.equals("dr"))
			sSalutation = AppGlobal.g_oLang.get()._("dr");
		else if(sSource.equals("prof"))
			sSalutation = AppGlobal.g_oLang.get()._("prof");
		else if(sSource.equals("phd"))
			sSalutation = AppGlobal.g_oLang.get()._("phd");
		return sSalutation;
	}
	private String getGenderLang(String sSource){
		String sGender = "";
		if(sSource.equals(MemMember.GENDER_MALE))
			sGender = AppGlobal.g_oLang.get()._("male");
		else if (sSource.equals(MemMember.GENDER_FEMALE))
			sGender = AppGlobal.g_oLang.get()._("female");
		return sGender;
	}
	public void showMemberBonus(HashMap<String, String> oBonusInfoList, HashMap<String, BigDecimal> oBonusList) {
		changePageTab(1, true);
	}
		
	private void addMemberDetailToCommonBasket(ArrayList<String> sMessageTitle, ArrayList<String> sMessage) {
		for(int i = 0 ; i < sMessage.size() ; i++) {
			ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
			ArrayList<String> sFieldValues = new ArrayList<String>();
			ArrayList<String> sFieldAligns= new ArrayList<String>();
			
			iFieldWidths.add(m_oMemberDetailCommonBasket.getWidth() / 2);
			sFieldValues.add(sMessageTitle.get(i));
			sFieldAligns.add("");
			iFieldWidths.add(m_oMemberDetailCommonBasket.getWidth() / 2);
			sFieldValues.add(sMessage.get(i));
			sFieldAligns.add("");
			
			m_oMemberDetailCommonBasket.addItem(0, i, 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
		}
	}
	
	public void showClearMemberButton() {
		m_oButtonClearMember.setVisible(true);
		m_oButtonSetMember.setVisible(false);
	}
		
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (iChildId == m_oButtonSetMember.getId()) {
			for (FrameMemberDetailListener listener : listeners) {
				listener.frameMemberDetail_clickSetMember();
				break;
			}
			bMatchChild = true;
		} else if (iChildId == m_oButtonClearMember.getId()) {
			for (FrameMemberDetailListener listener : listeners) {
				listener.frameMemberDetail_clickClearMember();
				break;
			}
			bMatchChild = true;
		}

		return bMatchChild;
	}
	
	@Override
	public void frameHorizontalTabList_clickTab(int iTabIndex, int iId) {
		changePageTab(iTabIndex, false);
	}
	
	@Override
	public void frameCommonPageContainer_changeFrame() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean frameCommonPageContainer_swipe(boolean bLeft, String sNote, int iChildId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void frameCommonPageContainer_updateFrame(int iOrigIndex, int iIndex, String sFrameName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void frameCommonPageContainer_CloseImageClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void frameCommonPageContainer_ClickButtonIndex(int iIndex) {
		// TODO Auto-generated method stub
		
	}
}
