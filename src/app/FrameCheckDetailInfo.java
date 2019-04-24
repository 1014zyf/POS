package app;

import java.util.ArrayList;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;

/** interface for the listeners/observers callback method */
interface FrameCheckDetailInfoListener {
	void FrameCheckDetailInfo_closeClick();
}

public class FrameCheckDetailInfo extends VirtualUIFrame{
	static final int TYPE_FRAME_CHECK_DETAIL_INFO_BY_CHECK_FUNCTION = 1;
	static final int TYPE_FRAME_CHECK_DETAIL_INFO_BY_SPLIT_TABLE = 2;
	
	static final int TYPE_BASIC_INFORMATION = 1;
	static final int TYPE_OTHER_INFORMATION = 2;
	static final int TYPE_CHECK_TOTAL_INFORMATION = 3;

	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUIFrame m_oFrameTitleBar;
	private VirtualUILabel m_oLabelTitleName;
	private VirtualUIFrame m_oFraButtonClose;
	private VirtualUIFrame m_oFrameCheckDetailInfo_Left;
	private VirtualUIFrame m_oFrameCheckDetailInfo_Right;
	
	private VirtualUIFrame m_oFrameLeftFrame;
	private VirtualUIFrame m_oFrameRightFrame;
	private VirtualUILabel m_oLabelOtherDetail;
	private VirtualUIFrame m_oFrameOtherDetailUnderLineTop;
	private VirtualUIFrame m_oFrameOtherDetailUnderLineBottom;
	private VirtualUILabel m_oLabelChkTotalDetail;
	private VirtualUIFrame m_oFrameChkTotalDetailUnderLineTop;
	private VirtualUIFrame m_oFrameChkTotalDetailUnderLineBottom;
	
	private VirtualUIFrame m_oFrameListBasicDetail;
	private VirtualUIFrame m_oFrameListOtherInfomation;
	private VirtualUIFrame m_oFrameListCheckTotalInformation;
	private VirtualUIList m_oListBasicDetail;
	private VirtualUIList m_oListOtherInformation;
	private VirtualUIList m_oListCheckTotalInformation;
	private VirtualUIFrame m_oFrameUnderline;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameCheckDetailInfoListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameCheckDetailInfoListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameCheckDetailInfoListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FrameCheckDetailInfo(int iFrameDetailInfoType) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameCheckDetailInfoListener>();
		if(iFrameDetailInfoType == TYPE_FRAME_CHECK_DETAIL_INFO_BY_SPLIT_TABLE ){
		// Load form from template file
			m_oTemplateBuilder.loadTemplate("fraCheckDetailInfoBySplitCheck.xml");
		}else if(iFrameDetailInfoType == TYPE_FRAME_CHECK_DETAIL_INFO_BY_CHECK_FUNCTION){
			// Load form from template file
			m_oTemplateBuilder.loadTemplate("fraCheckDetailInfo.xml");
		}
		
		// Title bar
		m_oFrameTitleBar = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleBar, "fraTitleBar");
		m_oFrameTitleBar.setVisible(false);
		this.attachChild(m_oFrameTitleBar);
		
		VirtualUIFrame oTitleUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oTitleUnderline, "fraTitleBarUnderline");
		m_oFrameTitleBar.attachChild(oTitleUnderline);
		
		// Title Name
		m_oLabelTitleName = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTitleName, "lblTitle");
		m_oFrameTitleBar.attachChild(m_oLabelTitleName);
		
		//Create Close button
		m_oFraButtonClose = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFraButtonClose, "fraButClose");
		VirtualUIImage oImage = new VirtualUIImage();
		oImage.setWidth(m_oFraButtonClose.getWidth());
		oImage.setHeight(m_oFraButtonClose.getHeight());
		oImage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_esc_sh.png");
		m_oFraButtonClose.attachChild(oImage);
		m_oFraButtonClose.setClickServerRequestNote("butClose");
		m_oFraButtonClose.allowClick(true);
		m_oFraButtonClose.setClickServerRequestBlockUI(false);
		m_oFrameTitleBar.attachChild(m_oFraButtonClose);
		
		//Add the Big Left Frame for adding the shadow   
		m_oFrameCheckDetailInfo_Left = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCheckDetailInfo_Left, "fraCheckDetailInfo_Left");
		m_oFrameCheckDetailInfo_Left.setVisible(true);
		this.attachChild(m_oFrameCheckDetailInfo_Left);
		
		//Add the Big Right Frame for adding the shadow  
		m_oFrameCheckDetailInfo_Right = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCheckDetailInfo_Right, "fraCheckDetailInfo_Right");
		m_oFrameCheckDetailInfo_Right.setVisible(true);
		this.attachChild(m_oFrameCheckDetailInfo_Right);
		
		//Right Frame for Check Total Information
		m_oFrameRightFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameRightFrame, "fraRightFrame");
		m_oFrameTitleBar.setVisible(true);

		//Left Frame for Basic Info and Other Info
		m_oFrameLeftFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftFrame, "fraLeftFrame");
		m_oFrameTitleBar.setVisible(true);
		
		this.attachChild(m_oFrameRightFrame);
		this.attachChild(m_oFrameLeftFrame);
		
		// Basic Description Label
		VirtualUILabel oBasicLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oBasicLabel, "lblBasicInfoHeader");
		oBasicLabel.setValue(AppGlobal.g_oLang.get()._("basic_information", ""));
		m_oFrameLeftFrame.attachChild(oBasicLabel);
		
		// Basic Information Label Underline
		m_oFrameUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameUnderline, "fraBasicUnderline");
		
		// Scroll Frame for Basic Information
		m_oFrameListBasicDetail = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameListBasicDetail, "fraListBasicDetail");
		m_oFrameLeftFrame.attachChild(m_oFrameListBasicDetail);
		m_oFrameLeftFrame.attachChild(m_oFrameUnderline);
		
		// Basic Description List
		m_oListBasicDetail = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListBasicDetail, "listBasicDetail");
		m_oFrameListBasicDetail.attachChild(m_oListBasicDetail);
		
		//Other description label
		m_oLabelOtherDetail = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOtherDetail, "lblOtherDetail");
		m_oLabelOtherDetail.setValue(AppGlobal.g_oLang.get()._("other_information", ""));
		m_oFrameLeftFrame.attachChild(m_oLabelOtherDetail);
		m_oFrameOtherDetailUnderLineTop = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameOtherDetailUnderLineTop, "lblOtherDetailUnderLineTop");
		m_oFrameLeftFrame.attachChild(m_oFrameOtherDetailUnderLineTop);
		m_oFrameOtherDetailUnderLineBottom = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameOtherDetailUnderLineBottom, "lblOtherDetailUnderLineBottom");
		m_oFrameLeftFrame.attachChild(m_oFrameOtherDetailUnderLineBottom);
		
		// Scroll Frame for Other Information
		m_oFrameListOtherInfomation = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameListOtherInfomation, "fraListOtherInformation");
		m_oFrameLeftFrame.attachChild(m_oFrameListOtherInfomation);
		
		// Other Information List
		m_oListOtherInformation = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListOtherInformation, "listOtherInformation");
		m_oFrameListOtherInfomation.attachChild(m_oListOtherInformation);
		
		//Check total label
		m_oLabelChkTotalDetail = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelChkTotalDetail, "lblChkTotalDetail");
		m_oLabelChkTotalDetail.setValue(AppGlobal.g_oLang.get()._("check_total_information", ""));
		m_oFrameChkTotalDetailUnderLineTop = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameChkTotalDetailUnderLineTop, "lblChkTotalDetailUnderLineTop");
		m_oFrameChkTotalDetailUnderLineBottom = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameChkTotalDetailUnderLineBottom, "lblChkTotalDetailUnderLineBottom");
		
		// Scroll Frame for Check Total Information
		m_oFrameListCheckTotalInformation = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameListCheckTotalInformation, "fraListCheckTotalInformation");
		
		// Check Total Information List
		m_oListCheckTotalInformation = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListCheckTotalInformation, "listCheckTotalInformation");
		m_oFrameListCheckTotalInformation.attachChild(m_oListCheckTotalInformation);
		
		if(iFrameDetailInfoType == TYPE_FRAME_CHECK_DETAIL_INFO_BY_CHECK_FUNCTION){
			m_oFrameLeftFrame.attachChild(m_oLabelChkTotalDetail);
			m_oFrameRightFrame.attachChild(m_oFrameChkTotalDetailUnderLineTop);
			m_oFrameLeftFrame.attachChild(m_oFrameChkTotalDetailUnderLineBottom);
			m_oFrameLeftFrame.attachChild(m_oFrameListCheckTotalInformation);
		}
		else if (iFrameDetailInfoType == TYPE_FRAME_CHECK_DETAIL_INFO_BY_SPLIT_TABLE){
			m_oFrameRightFrame.attachChild(m_oLabelChkTotalDetail);
			m_oFrameRightFrame.attachChild(m_oFrameChkTotalDetailUnderLineTop);
			m_oFrameRightFrame.attachChild(m_oFrameChkTotalDetailUnderLineBottom);
			m_oFrameRightFrame.attachChild(m_oFrameListCheckTotalInformation);
		}
	}
	
	public void setTitleBarDisable() {
		m_oFrameTitleBar.setVisible(false);
		m_oLabelTitleName.setVisible(false);
		m_oFraButtonClose.setVisible(false);
	}
	
	public void setTitleName(String sTitleName) {
		m_oFrameTitleBar.setWidth(this.getWidth());
		m_oLabelTitleName.setValue(sTitleName);
		m_oLabelTitleName.setWidth(this.getWidth() - 2);
		m_oFraButtonClose.setLeft(this.getWidth()-m_oFraButtonClose.getWidth()-2);
		m_oFrameTitleBar.setVisible(true);
	}
	
	public void setTableNumber(String[] sTable) {
		addListsDetail(AppGlobal.g_oLang.get()._("table_no"), sTable, 1);
	}
	
	public void setCheckType(String[] sCheckType) {
		addListsDetail(AppGlobal.g_oLang.get()._("check_type"), sCheckType, 1);
	}
	
	private void addListsDetail(String sTitle, String [] sContent, int iType) {
		VirtualUIFrame oFrameBasicDetail = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameBasicDetail, "fraBasicDetail");
		
		VirtualUILabel oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelTitle, "lblDetailTitle");
		oLabelTitle.setValue(sTitle);
		oFrameBasicDetail.attachChild(oLabelTitle);

		VirtualUILabel oLabelContent = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelContent, "lblContent");
		oLabelContent.setValue(sContent);
		oFrameBasicDetail.attachChild(oLabelContent);
		if(iType == TYPE_BASIC_INFORMATION)
			m_oListBasicDetail.attachChild(oFrameBasicDetail);
		else if(iType == TYPE_OTHER_INFORMATION)
			m_oListOtherInformation.attachChild(oFrameBasicDetail);
		else
			m_oListCheckTotalInformation.attachChild(oFrameBasicDetail);
	}
	
	public void setCheckNumber(String sCheckNumber) {
		String[] sArray = new String[] {sCheckNumber};
		addListsDetail(AppGlobal.g_oLang.get()._("check_no"), sArray, 1);
	}
	
	public void setCover(String sCover) {
		String[] sArray = new String[] {sCover};
		addListsDetail(AppGlobal.g_oLang.get()._("cover"), sArray, 1);
	}
	
	public void setOpenEmployee(String[] sEmployee) {
		addListsDetail(AppGlobal.g_oLang.get()._("open_check_employee"), sEmployee, 1);
	}
	
	public void setOpenTime(String sOpenTime) {
		String[] sArray = new String[] {sOpenTime};
		addListsDetail(AppGlobal.g_oLang.get()._("open_time"), sArray, 1);
	}
	
	public void setCheckMember(String[] sMember, String sMemberNumber) {
		addListsDetail(AppGlobal.g_oLang.get()._("member_name"), sMember, 1);
		String[] sArray = new String[] {sMemberNumber};
		addListsDetail(AppGlobal.g_oLang.get()._("member_number"), sArray, 1);
	}
	
	public void setPrintCount(String sPrintCount) {
		String[] sArray = new String[] {sPrintCount};
		addListsDetail(AppGlobal.g_oLang.get()._("print_count"), sArray, 2);
	}
	
	public void setItemCount(String sItemCount) {
		String[] sArray = new String[] {sItemCount};
		addListsDetail(AppGlobal.g_oLang.get()._("item_count"), sArray, 2);
	}
	
	public void setNewItemCount(String sNewItemCount) {
		String[] sArray = new String[] {sNewItemCount};
		addListsDetail(AppGlobal.g_oLang.get()._("new_item_count"), sArray, 2);
	}
	
	public void setSubTotal(String sSubTotal) {
		String[] sArray = new String[] {sSubTotal};
		addListsDetail(AppGlobal.g_oLang.get()._("sub_total"), sArray, 3);
	}
	
	public void setSC(String sSCTotal) {
		String[] sArray = new String[] {sSCTotal};
		addListsDetail(AppGlobal.g_oLang.get()._("sc_total"), sArray, 3);
	}
	
	public void setTax(String sTaxTotal) {
		String[] sArray = new String[] {sTaxTotal};
		addListsDetail(AppGlobal.g_oLang.get()._("tax_total"), sArray, 3);
	}
	
	public void setGratuityTotal(String sGratuityTotal) {
		String[] sArray = new String[] {sGratuityTotal};
		addListsDetail(AppGlobal.g_oLang.get()._("gratuity_total"), sArray, 3);
	}
	
	public void setRoundAmount(String sRoundAmount) {
		String[] sArray = new String[] {sRoundAmount};
		addListsDetail(AppGlobal.g_oLang.get()._("round_amount"), sArray, 3);
	}
	
	public void setCheckTotal(String sCheckTotal) {
		String[] sArray = new String[] {sCheckTotal};
		addListsDetail(AppGlobal.g_oLang.get()._("check_total"), sArray, 3);
	}
	
	public void setDiscountTotal(String sDiscountTotal) {
		String[] sArray = new String[] {sDiscountTotal};
		addListsDetail(AppGlobal.g_oLang.get()._("discount_total"), sArray, 3);
	}
	
	public void setExtraChargeTotal(String sExtraChargeTotal) {
		String[] sArray = new String[] {sExtraChargeTotal};
		addListsDetail(AppGlobal.g_oLang.get()._("extra_charge_total"), sArray, 3);
	}
	
	public void setMealPeriod(String sPeriodName) {
		String[] sArray = new String[] {sPeriodName};
		addListsDetail(AppGlobal.g_oLang.get()._("check_meal_period"), sArray, 1);
	}
	
	public void clearDetailList(){
		m_oListBasicDetail.removeAllChildren();
		m_oListOtherInformation.removeAllChildren();
		m_oListCheckTotalInformation.removeAllChildren();
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		for (FrameCheckDetailInfoListener listener : listeners) {
			// Find the clicked button
			if (m_oFraButtonClose.getId() == iChildId) {
				// Raise the event to parent
				listener.FrameCheckDetailInfo_closeClick();
				bMatchChild = true;
				break;
			}/*else if (m_oButtonOrder.getId() == iChildId) {
				// Raise the event to parent
				listener.FrameItemDetail_orderClick();
				break;
			}*/
		}
		return bMatchChild;
	}
}
