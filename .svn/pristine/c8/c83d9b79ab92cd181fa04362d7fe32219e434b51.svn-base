package app;

import java.util.ArrayList;
import java.util.List;

import om.MemMember;
import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FrameLoyaltyMemberDetailListener {
	void frameLoyaltyMemberDetail_clickSetMember();
	void frameLoyaltyMemberDetail_clickClearMember();
}

// Edit by King Cheung 2017-11-27 ---Start---

public class FrameLoyaltyMemberDetail extends VirtualUIFrame implements FrameHorizontalTabListListener {
	private TemplateBuilder m_oTemplateBuilder;
	
	//private FrameHorizontalTabList m_oFrameHorizontalTabList;
	private FrameCommonBasket<String> m_oCommonBasket;
	//private VirtualUIFrame m_oFrameoMemberInfo;
	//private VirtualUIList m_oListMemberBasicInformation;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameLoyaltyMemberDetailListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameLoyaltyMemberDetailListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameLoyaltyMemberDetailListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	//Constructor
	public FrameLoyaltyMemberDetail() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameLoyaltyMemberDetailListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraMemberDetail.xml");
	}
	
	public void init(boolean bShowClearMemberButton) {
		// Load child elements from template
		// Horizontal List
		/*
		m_oFrameHorizontalTabList = new FrameHorizontalTabList();
		m_oTemplateBuilder.buildFrame(m_oFrameHorizontalTabList, "fraHorizontalList");
		m_oFrameHorizontalTabList.init();
		m_oFrameHorizontalTabList.addListener(this);
		this.attachChild(m_oFrameHorizontalTabList);
		
		List<String> oTabNameList = new ArrayList<String>();
		oTabNameList.add(AppGlobal.g_oLang.get()._("basic_information"));
		m_oFrameHorizontalTabList.addPageTabs(oTabNameList);

		m_oFrameoMemberInfo = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameoMemberInfo, "fraMemberInfo");
		this.attachChild(m_oFrameoMemberInfo);
		
		m_oListMemberBasicInformation = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListMemberBasicInformation, "listMemberBasicInfo");
		m_oListMemberBasicInformation.setHeight(400);

		m_oFrameoMemberInfo.attachChild(m_oListMemberBasicInformation);
		*/
		m_oCommonBasket = new FrameCommonBasket<String>();
		m_oTemplateBuilder.buildFrame(m_oCommonBasket, "fraLoyaltyCommonBasket");
		m_oCommonBasket.init();
		m_oCommonBasket.allowClick(false);
		m_oCommonBasket.setHeight(500);
		m_oCommonBasket.setWidth(588);
		this.attachChild(m_oCommonBasket);
		
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		
		iFieldWidths.add(250);
		sFieldValues.add(AppGlobal.g_oLang.get()._("basic_information"));
		
		iFieldWidths.add(338);
		sFieldValues.add("");

		m_oCommonBasket.addHeader(iFieldWidths, sFieldValues);
		m_oCommonBasket.setHeaderFormat(40, 18, "");
		m_oCommonBasket.setHeaderUnderlineColor("#999999");
		m_oCommonBasket.addSection(0, AppGlobal.g_oLang.get()._("basic_information", ""), false);
		
		initMemberDetail();
	}

	public void addCheckListingTab(int iTabIndex, String sTabName) {
		/*
		VirtualUILabel oLblPanelLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblPanelLabel, "lblTabName");
		oLblPanelLabel.setValue(sTabName);
		oLblPanelLabel.setEnabled(true);
		oLblPanelLabel.allowClick(false);

		//Create Underline frame
		VirtualUIFrame fraPageTabUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(fraPageTabUnderline, "fraTabUnderline");
		fraPageTabUnderline.setVisible(false);
		fraPageTabUnderline.setEnabled(false);
		fraPageTabUnderline.allowClick(false);
		
		initMemberDetail();
		*/
	}
	
	public void addHorizontalTabList(List<String> oTabNameList) {
	//	m_oFrameHorizontalTabList.addPageTabs(oTabNameList);
	}
	
	public void changePageTab(int iTagIndex, boolean bUpdateSelectedTabColor) {
		//if (bUpdateSelectedTabColor)
		//	m_oFrameHorizontalTabList.changePageTab(iTagIndex);
  
					 
										
		
										 
		
		//if (iTagIndex == 0)
			//m_oFrameoMemberInfo.setVisible(true);
		// else 
			//m_oFrameoMemberInfo.setVisible(false);
		//
	}

	private void addMemberInfo(String sTitle, String sContent) {
		/*
		VirtualUIFrame oFrameBasicDetail = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameBasicDetail, "fraMemberBasicInfo");
		
		VirtualUILabel oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelTitle, "lblTitle");
		oLabelTitle.setValue(sTitle);
		oFrameBasicDetail.attachChild(oLabelTitle);

		VirtualUILabel oLabelContent = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelContent, "lblContent");
		oLabelContent.setValue(sContent);
		oFrameBasicDetail.attachChild(oLabelContent);
		*/	
	//	m_oListMemberBasicInformation.attachChild(oFrameBasicDetail);
	}
	
	public void initMemberDetail() {

		m_oCommonBasket.clearAllSections();

		ArrayList<String> oFieldTitle = new ArrayList<String>();
		ArrayList<String> oFieldValue = new ArrayList<String>();
		
		oFieldTitle.add(AppGlobal.g_oLang.get()._("member_no"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("member_name"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("type"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("bonus_balance"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("last_visit_date"));
		oFieldValue.add("");
		oFieldValue.add("");
		oFieldValue.add("");
		oFieldValue.add("");
		oFieldValue.add("");
		
		for(int i = 0 ; i < oFieldTitle.size() ; i++) {
			ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
			ArrayList<String> sFieldValues = new ArrayList<String>();
			ArrayList<String> sFieldAligns = new ArrayList<String>();
			
			iFieldWidths.add(250);
			sFieldValues.add(oFieldTitle.get(i));
			sFieldAligns.add("");

			iFieldWidths.add(338);
			sFieldValues.add(oFieldValue.get(i));
			sFieldAligns.add("");

			m_oCommonBasket.addItem(0, i, 38, iFieldWidths, sFieldValues, sFieldAligns, null, null);
			m_oCommonBasket.setFieldTextSize(0, i, 0, 16);
			m_oCommonBasket.setFieldTextSize(0, i, 1, 16);
		}
		
		/*
		addMemberInfo(AppGlobal.g_oLang.get()._("member_no"), "");
		addMemberInfo(AppGlobal.g_oLang.get()._("member_name"), "");
		addMemberInfo(AppGlobal.g_oLang.get()._("type"), "");
		addMemberInfo(AppGlobal.g_oLang.get()._("bonus_balance"), "");
		addMemberInfo(AppGlobal.g_oLang.get()._("last_visit_date"), "");
		*/
	}

	public void updateDetail(MemMember oMember) {

		m_oCommonBasket.clearAllSections();

		ArrayList<String> oFieldTitle = new ArrayList<String>();
		ArrayList<String> oFieldValue = new ArrayList<String>();
		
		oFieldTitle.add(AppGlobal.g_oLang.get()._("member_no"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("member_name"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("type"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("bonus_balance"));
		oFieldTitle.add(AppGlobal.g_oLang.get()._("last_visit_date"));
		oFieldValue.add(oMember.getMemberNo());
		oFieldValue.add(oMember.getName());
		oFieldValue.add(oMember.getType());
		oFieldValue.add(oMember.getBonusBalance());
		oFieldValue.add(oMember.getLastVisitDate());
		
		for(int i = 0 ; i < oFieldTitle.size() ; i++) {
			ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
			ArrayList<String> sFieldValues = new ArrayList<String>();
			ArrayList<String> sFieldAligns = new ArrayList<String>();
			
			iFieldWidths.add(250);
			sFieldValues.add(oFieldTitle.get(i));
			sFieldAligns.add("");

			iFieldWidths.add(338);
			sFieldValues.add(oFieldValue.get(i));
			sFieldAligns.add("");

			m_oCommonBasket.addItem(0, i, 38, iFieldWidths, sFieldValues, sFieldAligns, null, null);
			m_oCommonBasket.setFieldTextSize(0, i, 0, 16);
			m_oCommonBasket.setFieldTextSize(0, i, 1, 16);
		}
		/*
		m_oListMemberBasicInformation.removeAllChildren();
		
		addMemberInfo(AppGlobal.g_oLang.get()._("member_no"), oMember.getMemberNo());
		addMemberInfo(AppGlobal.g_oLang.get()._("member_name"), oMember.getName());
		addMemberInfo(AppGlobal.g_oLang.get()._("type"), oMember.getType());

		addMemberInfo(AppGlobal.g_oLang.get()._("bonus_balance"), oMember.getBonusBalance());
		addMemberInfo(AppGlobal.g_oLang.get()._("last_visit_date"), oMember.getLastVisitDate());
		*/
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		return false;
	}
	
	@Override
	public void frameHorizontalTabList_clickTab(int iTabIndex, int iId) {
		changePageTab(iTabIndex, false);
	}
}

//Edit by King Cheung 2017-11-27 ---End---