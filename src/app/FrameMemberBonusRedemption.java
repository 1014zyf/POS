package app;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;

/** interface for the listeners/observers callback method */
interface FrameMemberBonusRedemptionListener {
	void frameMemberBonusRedemption_clickCancel();
	void frameMemberBonusRedemption_clickConfirm();
	void frameMemberBonusRedemption_clickExit();
	void frameMemberBonusRedemption_addBenefit(int iItemIndex);
	void fraMemberBonusRedemption_prevPage();
	void fraMemberBonusRedemption_nextPage();
}

public class FrameMemberBonusRedemption extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUIList m_oListBasicInformation;
	private FrameCommonBasket m_oFrameBonusOrderingBasket;
	
	private VirtualUIFrame m_oFramePage;
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIImage m_oImgButtonNextPage;
	
	private List<Integer> m_oSelectedItemList;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameMemberBonusRedemptionListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameMemberBonusRedemptionListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameMemberBonusRedemptionListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FrameMemberBonusRedemption() {
		m_oTemplateBuilder = new TemplateBuilder();
		m_oSelectedItemList = new ArrayList<Integer>();
		listeners = new ArrayList<FrameMemberBonusRedemptionListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraMemberBonusRedemption.xml");

		m_oFrameTitleHeader = new FrameTitleHeader();
        m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
        m_oFrameTitleHeader.init(true);
        m_oFrameTitleHeader.addListener(this);
        m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("member_benefit_redemption"));
        this.attachChild(m_oFrameTitleHeader);

		// Basic Information
		m_oListBasicInformation = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListBasicInformation, "listMemberBasicInfo");
		this.attachChild(m_oListBasicInformation);
		
		// Item Basket
		m_oFrameBonusOrderingBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFrameBonusOrderingBasket, "fraBonusBasket");
		m_oFrameBonusOrderingBasket.init();
		m_oFrameBonusOrderingBasket.addListener(this);
		this.attachChild(m_oFrameBonusOrderingBasket);

		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
    	ArrayList<String> sFieldValues = new ArrayList<String>();

    	iFieldWidths.add(150);
    	sFieldValues.add(AppGlobal.g_oLang.get()._("benefit_code"));
    	iFieldWidths.add(350);
    	sFieldValues.add(AppGlobal.g_oLang.get()._("name"));
    	iFieldWidths.add(120);
    	sFieldValues.add(AppGlobal.g_oLang.get()._("bonus"));
    	iFieldWidths.add(120);
    	sFieldValues.add(AppGlobal.g_oLang.get()._("available"));
    	iFieldWidths.add(110);
    	sFieldValues.add(AppGlobal.g_oLang.get()._("action"));
    	
    	m_oFrameBonusOrderingBasket.addHeader(iFieldWidths, sFieldValues);
    	m_oFrameBonusOrderingBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);

		// Create prev page button
		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevPage");
		m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_prev_page_button.png");
		m_oImgButtonPrevPage.allowClick(true);
		m_oImgButtonPrevPage.setClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.allowLongClick(true);
		m_oImgButtonPrevPage.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oImgButtonPrevPage);
		
		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_next_page_button.png");
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.allowLongClick(true);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oImgButtonNextPage);
		//Create Page label
		m_oFramePage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePage, "fraPage");

		VirtualUIImage oImage = new VirtualUIImage();
		oImage.setWidth(m_oFramePage.getWidth());
		oImage.setHeight(m_oFramePage.getHeight());
		oImage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_page_bg.png");
		m_oFramePage.attachChild(oImage);
		
		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oLblPage.setWidth(m_oFramePage.getWidth());
		m_oLblPage.setHeight(m_oFramePage.getHeight());
		m_oFramePage.attachChild(m_oLblPage);
		
		this.attachChild(m_oFramePage);
	}
	
	public void addBonusInfo(LinkedHashMap<String, String> oInfoList) {
		if (m_oListBasicInformation.getChildCount() > 0)
			m_oListBasicInformation.removeAllChildren();
		
		for (Entry<String, String> oInfo: oInfoList.entrySet()) {
			VirtualUIFrame oFrameBasicDetail = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(oFrameBasicDetail, "fraMemberBasicInfo");
			
			VirtualUILabel oLabelTitle = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabelTitle, "lblInfoTitle");
			oLabelTitle.setValue(oInfo.getKey());
			oFrameBasicDetail.attachChild(oLabelTitle);
	
			VirtualUILabel oLabelContent = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabelContent, "lblInfoContent");
			oLabelContent.setValue(oInfo.getValue());
			oFrameBasicDetail.attachChild(oLabelContent);
			
			m_oListBasicInformation.attachChild(oFrameBasicDetail);
		}
	}

	public void addBonusToBasket(int iItemIndex, String sCode, String sBenefitName, String sMaxCount, String sBonus) {
    	ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
    	ArrayList<String> sFieldValues = new ArrayList<String>();
    	ArrayList<String> sFieldAligns = new ArrayList<String>();
    	ArrayList<String> sFieldTypes = new ArrayList<String>();

    	iFieldWidths.add(150);
    	sFieldValues.add(sCode);
    	sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
    	sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
    	iFieldWidths.add(350);
    	sFieldValues.add(sBenefitName);
    	sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
    	sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
    	iFieldWidths.add(120);
    	sFieldValues.add(sBonus);
    	sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
    	sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
    	iFieldWidths.add(120);
    	sFieldValues.add(sMaxCount);
    	sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
    	sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
    	iFieldWidths.add(110);
    	sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/search_item_order_icon.png");
    	sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
    	sFieldTypes.add(HeroActionProtocol.View.Type.IMAGE);
    	m_oFrameBonusOrderingBasket.addItem(0, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
    	m_oFrameBonusOrderingBasket.moveScrollToItem(0, iItemIndex);
	}

	public void clearOverrideConditionRecords() {
		m_oFrameBonusOrderingBasket.removeAllItems(0);
	}

	public void updatePageButton(int iCurrentPage, int iTotalPage) {
		m_oLblPage.setValue(iCurrentPage + "/" + iTotalPage);
		
		if (iTotalPage > 1) {
			m_oFramePage.setVisible(true);
			if (iCurrentPage > 1)
				m_oImgButtonPrevPage.setVisible(true);
			else
				m_oImgButtonPrevPage.setVisible(false);
			
			if (iCurrentPage < iTotalPage)
				m_oImgButtonNextPage.setVisible(true);
			else
				m_oImgButtonNextPage.setVisible(false);
		} else {
			m_oFramePage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
			m_oImgButtonNextPage.setVisible(false);
		}
	}
	
	public void setAvailableCount(int iItemIndex, String sAvailableCount) {
		m_oFrameBonusOrderingBasket.setFieldValue(0, iItemIndex, 3, sAvailableCount);
	}
	
	public void setActionButtonEnabled(int iItemIndex, boolean bEnabled) {
		m_oFrameBonusOrderingBasket.setFieldEnabled(0, iItemIndex, 4, bEnabled);
	}
	
	public List<Integer> getSelectedItemList() {
		return m_oSelectedItemList;
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (iChildId == m_oImgButtonPrevPage.getId()) {
			for (FrameMemberBonusRedemptionListener listener : listeners)
				listener.fraMemberBonusRedemption_prevPage();
			bMatchChild = true;
		} else if (iChildId == m_oImgButtonNextPage.getId()) {
			for (FrameMemberBonusRedemptionListener listener : listeners)
				listener.fraMemberBonusRedemption_nextPage();
			bMatchChild = true;
		}
		return bMatchChild;
	}
	
	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		if (iFieldIndex == 4) {
			for(FrameMemberBonusRedemptionListener listener: listeners) {
				listener.frameMemberBonusRedemption_addBenefit(iItemIndex);
				break;
			}
		}
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId, int iSectionIndex, int iItemIndex,
			int iFieldIndex, String sNote) {
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}
	
	@Override
	public void FrameTitleHeader_close() {
		for (FrameMemberBonusRedemptionListener listener : listeners) {
			// Raise the event to parent
			listener.frameMemberBonusRedemption_clickExit();
		}
	}

}
