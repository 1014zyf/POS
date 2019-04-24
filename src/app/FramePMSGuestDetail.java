package app;

import java.util.HashMap;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;

public class FramePMSGuestDetail extends VirtualUIFrame implements FrameCommonPageContainerListener{
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUIList m_oListBasicDetail;
	private VirtualUIFrame m_oFrameListBasicDetail;
	private VirtualUILabel m_oBasicLabel;
	
	private VirtualUIFrame m_oFrameListUserInfo;
	
	private VirtualUILabel m_oLabelUserInfo;
	private VirtualUIList m_oListUserInfo;
	
	private VirtualUIFrame m_oFrameImage;
	private VirtualUIImage m_oImageSignature;
	private FrameCommonPageContainer m_oCommonPageTabList;
	
	private VirtualUIFrame m_oFrameUnderline;
	
	public FramePMSGuestDetail() {
		m_oTemplateBuilder = new TemplateBuilder();
		m_oTemplateBuilder.loadTemplate("fraPMSGuestDetail.xml");
	}
	
	public void init(boolean bShowSignature) {
		// Load child elements from template
		/////////////////////////////////////////////////////////////////
		// Basic Description List
		m_oBasicLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oBasicLabel, "lblBasicInfoHeader");
		m_oBasicLabel.setValue(AppGlobal.g_oLang.get()._("basic_information"));
		
		if(!bShowSignature)
			this.attachChild(m_oBasicLabel);
		
		m_oFrameListBasicDetail = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameListBasicDetail, "fraListBasicDetail");
		if(!bShowSignature)
			this.attachChild(m_oFrameListBasicDetail);
		
		m_oListBasicDetail = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListBasicDetail, "listBasicDetail");
		m_oFrameListBasicDetail.attachChild(m_oListBasicDetail);
		
		// User Info List
		m_oLabelUserInfo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelUserInfo, "lblInfoHeader");
		m_oLabelUserInfo.setValue(AppGlobal.g_oLang.get()._("user_info"));
		this.attachChild(m_oLabelUserInfo);
		
		m_oFrameListUserInfo = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameListUserInfo, "fraListUserInfo");
		this.attachChild(m_oFrameListUserInfo);
		
		m_oListUserInfo = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListUserInfo, "listUserInfo");
		m_oFrameListUserInfo.attachChild(m_oListUserInfo);
		
		m_oFrameUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameUnderline, "fraBasicUnderline");
		this.attachChild(m_oFrameUnderline);
		
		m_oFrameUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameUnderline, "fraUserUnderline");
		m_oFrameListUserInfo.attachChild(m_oFrameUnderline);
		
		//Image Frame
		m_oFrameImage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameImage, "fraPhotoInfo");
			
		m_oImageSignature = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageSignature, "imageSignature");
		m_oImageSignature.setVisible(false);
		m_oFrameImage.attachChild(m_oImageSignature);
		
		m_oCommonPageTabList = new FrameCommonPageContainer();
		m_oTemplateBuilder.buildFrame(m_oCommonPageTabList, "fraListTab");
		m_oCommonPageTabList.init(m_oCommonPageTabList.getWidth(), m_oCommonPageTabList.getHeight(), 
				200, 50, 8, "#575757","#999999", "#FFFFFF", "#FFFFFF", 48, false, true);
		m_oCommonPageTabList.setUnderlineColor("#999999");
		m_oCommonPageTabList.setTagTextSize(m_oBasicLabel.getTextSize(), m_oBasicLabel.getTextSize());
		m_oCommonPageTabList.addListener(this);
		if(bShowSignature) {
			this.attachChild(m_oCommonPageTabList);
			m_oCommonPageTabList.addButton(AppGlobal.g_oLang.get()._("basic_information"), m_oFrameListBasicDetail);
			m_oCommonPageTabList.addButton(AppGlobal.g_oLang.get()._("signature"), m_oFrameImage);
		}
		
		addBasicDetail(AppGlobal.g_oLang.get()._("name"), "");
		addBasicDetail(AppGlobal.g_oLang.get()._("room_number"), "");
		addBasicDetail(AppGlobal.g_oLang.get()._("account_id"), "");
		addBasicDetail(AppGlobal.g_oLang.get()._("register_number"), "");
		addBasicDetail(AppGlobal.g_oLang.get()._("arrival_date"), "");
		addBasicDetail(AppGlobal.g_oLang.get()._("departure_date"), "");
		addBasicDetail(AppGlobal.g_oLang.get()._("guest_vip"), "");
		addBasicDetail(AppGlobal.g_oLang.get()._("account_type"), "");
		addBasicDetail(AppGlobal.g_oLang.get()._("remark"), "");
	}
	
	public void updateGuestDetail(HashMap<String, String> oGuestInfo) {
		String sUserInfo = "";
		
		m_oListBasicDetail.removeAllChildren();
		m_oListUserInfo.removeAllChildren();
		
		m_oImageSignature.setVisible(false);
		
		if (oGuestInfo.containsKey("packageCode")) {	// Pegasus iPMS package enquiry information
			//	Package Name
			addBasicDetail(AppGlobal.g_oLang.get()._("package_name"), oGuestInfo.get("packageName"));
			//	Package Code
			addBasicDetail(AppGlobal.g_oLang.get()._("package_code"), oGuestInfo.get("packageCode"));
			//	Account ID
			addBasicDetail(AppGlobal.g_oLang.get()._("account_id"), oGuestInfo.get("guestNumber"));
			//	Qty 
			addBasicDetail(AppGlobal.g_oLang.get()._("quantity"), oGuestInfo.get("packageQty"));
			//	Package Price
			addBasicDetail(AppGlobal.g_oLang.get()._("price"), oGuestInfo.get("packageAmount"));
			
			m_oLabelUserInfo.setValue(AppGlobal.g_oLang.get()._("package_item"));
		} else {
			// Guest name
			if(!oGuestInfo.containsKey("guestTitle") || oGuestInfo.get("guestTitle").equals(""))
				addBasicDetail(AppGlobal.g_oLang.get()._("name"), oGuestInfo.get("guestName"));
			else
				addBasicDetail(AppGlobal.g_oLang.get()._("name"), oGuestInfo.get("guestTitle")+" "+oGuestInfo.get("guestName"));
			// Room number
			if(oGuestInfo.containsKey("roomNumber"))
				addBasicDetail(AppGlobal.g_oLang.get()._("room_number"), oGuestInfo.get("roomNumber"));
			if (oGuestInfo.containsKey("accountId"))
				addBasicDetail(AppGlobal.g_oLang.get()._("account_id"), oGuestInfo.get("accountId"));
			// Register number
			if (oGuestInfo.containsKey("registerNumber"))
				addBasicDetail(AppGlobal.g_oLang.get()._("register_number"), oGuestInfo.get("registerNumber"));
			// Account number
			if(oGuestInfo.containsKey("accountNumber"))
				addBasicDetail(AppGlobal.g_oLang.get()._("account_number"), oGuestInfo.get("accountNumber"));
			// Arrival date
			if (oGuestInfo.containsKey("arrivalDate"))
				addBasicDetail(AppGlobal.g_oLang.get()._("arrival_date"), oGuestInfo.get("arrivalDate"));
			// Departure date
			if (oGuestInfo.containsKey("departureDate"))
				addBasicDetail(AppGlobal.g_oLang.get()._("departure_date"), oGuestInfo.get("departureDate"));
			
			// No Post
			if (oGuestInfo.containsKey("noPost")) {
				String sNoPostMsg = (oGuestInfo.get("noPost").equals("true")) ? AppGlobal.g_oLang.get()._("not_allow_to_post") : AppGlobal.g_oLang.get()._("allow_to_post");
				addBasicDetail(AppGlobal.g_oLang.get()._("no_post"), sNoPostMsg);
			}
			
			// VIP 
			if (oGuestInfo.containsKey("guestVip"))
				addBasicDetail(AppGlobal.g_oLang.get()._("vip"), oGuestInfo.get("guestVip"));
			// Account Type 
			if (oGuestInfo.containsKey("accountType"))
				addBasicDetail(AppGlobal.g_oLang.get()._("account_type"), oGuestInfo.get("accountType"));
			// Remark
			if (oGuestInfo.containsKey("remark"))
				addBasicDetail(AppGlobal.g_oLang.get()._("remark"), oGuestInfo.get("remark"));
			// User info
			for (int i = 1; i <= 10; i++) {
				if (oGuestInfo.containsKey("info" + i) && !oGuestInfo.get("info" + i).equals(""))
					sUserInfo += i + ". " + oGuestInfo.get("info" + i) + System.lineSeparator();
			}
			addUserInfo(sUserInfo);
		}
		
		if (oGuestInfo.containsKey("guestSignImage") && !oGuestInfo.get("guestSignImage").isEmpty()) {
			m_oImageSignature.setSource(oGuestInfo.get("guestSignImage").toString());
			m_oImageSignature.setVisible(true);
		}
	
	}
	
	public void clearGuestDetail() {
		m_oListBasicDetail.removeAllChildren();
		m_oListUserInfo.removeAllChildren();
		m_oImageSignature.setVisible(false);
	}
	
	private void addBasicDetail(String sTitle, String sContent) {
		VirtualUIFrame oFrameBasicDetail = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameBasicDetail, "fraBasicDetail");
		
		VirtualUILabel oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelTitle, "lblTitle");
		oLabelTitle.setValue(sTitle);
		oFrameBasicDetail.attachChild(oLabelTitle);

		VirtualUILabel oLabelContent = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelContent, "lblContent");
		oLabelContent.setValue(sContent);
		oFrameBasicDetail.attachChild(oLabelContent);
				
		m_oListBasicDetail.attachChild(oFrameBasicDetail);
	}
	
	private void addUserInfo(String sUserInfo) {
		VirtualUIFrame oFrameBasicDetail = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameBasicDetail, "fraUserInfo");
		
		VirtualUILabel oLabelUserInfo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelUserInfo, "lblUserInfo");
		oLabelUserInfo.setValue(sUserInfo);
		oFrameBasicDetail.attachChild(oLabelUserInfo);
				
		m_oListUserInfo.attachChild(oFrameBasicDetail);
	}
	
	//set the height of basic info frame and user info frame size (height)
	public void calibrateInfoFrameSize() {
		//int iSpacing = m_oLabelUserInfo.getTop() - (m_oFrameListBasicDetail.getTop() + m_oFrameListBasicDetail.getHeight());
		int iSpacing = 14;
		int iHalfTotalHeight = this.getHeight() / 2;
		
		m_oCommonPageTabList.setHeight(iHalfTotalHeight - m_oCommonPageTabList.getTop());
		m_oFrameListBasicDetail.setHeight(iHalfTotalHeight - m_oFrameListBasicDetail.getTop());
		m_oListBasicDetail.setHeight(m_oFrameListBasicDetail.getHeight());
		m_oFrameImage.setHeight(m_oFrameListBasicDetail.getHeight());
		m_oImageSignature.setHeight(m_oFrameImage.getHeight() - m_oImageSignature.getTop() * 2);//remark: set image_size < frame_size to prevent over-scanning of the image
		m_oLabelUserInfo.setTop(m_oFrameListBasicDetail.getTop() + m_oFrameListBasicDetail.getHeight() + iSpacing);
		m_oFrameListUserInfo.setHeight(iHalfTotalHeight - m_oLabelUserInfo.getHeight() - iSpacing);
		m_oFrameListUserInfo.setTop(m_oLabelUserInfo.getTop() + m_oLabelUserInfo.getHeight());
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
