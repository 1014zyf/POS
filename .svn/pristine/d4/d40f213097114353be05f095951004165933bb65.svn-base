package app;

import core.Controller;
import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

public class FormCheckDetailInfo extends VirtualUIForm implements FrameCheckDetailInfoListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameCheckDetailInfo m_oFrameCheckDetailInfo;
	private VirtualUIFrame m_oFrameCover;
	
	public FormCheckDetailInfo(boolean bIsOldCheck, Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmCheckDetailInfo.xml");
		
		// Background Cover Page
		m_oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCover, "fraCoverFrame");
		this.attachChild(m_oFrameCover);
		
		// Attach check detail info. frame
		m_oFrameCheckDetailInfo = new FrameCheckDetailInfo(FrameCheckDetailInfo.TYPE_FRAME_CHECK_DETAIL_INFO_BY_SPLIT_TABLE);
		m_oTemplateBuilder.buildFrame(m_oFrameCheckDetailInfo, "fraCheckDetailInfo");
		m_oFrameCheckDetailInfo.clearDetailList();
		
		// Add listener
		m_oFrameCheckDetailInfo.addListener(this);
		this.attachChild(m_oFrameCheckDetailInfo);
	}
	
	public void setTitle(String sTitleName) {
		m_oFrameCheckDetailInfo.setTitleName(sTitleName);
	}
	
	public void setTable(String[] sTable) {
		m_oFrameCheckDetailInfo.setTableNumber(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, sTable));
	}
	
	public void setCheckType(String sCheckType) {
		m_oFrameCheckDetailInfo.setCheckType(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, sCheckType));
	}
	
	public void setCheckNumber(String sCheckNumber) {
		m_oFrameCheckDetailInfo.setCheckNumber(sCheckNumber);
	}
	
	public void setCover(String sCover) {
		m_oFrameCheckDetailInfo.setCover(sCover);
	}
	
	public void setOpenEmployee(String[] sEmployee) {
		m_oFrameCheckDetailInfo.setOpenEmployee(sEmployee);
	}
	
	public void setOpenTime(String sOpenTime) {
		m_oFrameCheckDetailInfo.setOpenTime(sOpenTime);
	}
	
	public void setPrintCount(String sPrintCount) {
		m_oFrameCheckDetailInfo.setPrintCount(sPrintCount);
	}
	
	public void setItemCount(String sItemCount) {
		m_oFrameCheckDetailInfo.setItemCount(sItemCount);
	}
	
	public void setNewItemCount(String sNewItemCount) {
		m_oFrameCheckDetailInfo.setNewItemCount(sNewItemCount);
	}
	
	public void setSubTotal(String sSubTotal) {
		m_oFrameCheckDetailInfo.setSubTotal(sSubTotal);
	}
	
	public void setSCTotal(String sSCTotal) {
		m_oFrameCheckDetailInfo.setSC(sSCTotal);
	}
	
	public void setTaxTotal(String sTaxTotal) {
		m_oFrameCheckDetailInfo.setTax(sTaxTotal);
	}
	
	public void setGratuityTotal(String sGratuityTotal) {
		m_oFrameCheckDetailInfo.setGratuityTotal(sGratuityTotal);
	}
	
	public void setRoundAmount(String sRoundAmount) {
		m_oFrameCheckDetailInfo.setRoundAmount(sRoundAmount);
	}
	
	public void setCheckTotal(String sCheckTotal) {
		m_oFrameCheckDetailInfo.setCheckTotal(sCheckTotal);
	}
	
	public void setDiscountTotal(String sDiscountTotal) {
		m_oFrameCheckDetailInfo.setDiscountTotal(sDiscountTotal);
	}
	
	public void setExtraChargeTotal(String sExtraChargeTotal) {
		m_oFrameCheckDetailInfo.setExtraChargeTotal(sExtraChargeTotal);
	}
	
	public void setCheckMember(String[] sMember, String sMemberNumber) {
		m_oFrameCheckDetailInfo.setCheckMember(sMember, sMemberNumber);
	}
	
	public void setMealPeriod(String sMealPeriod) {
		m_oFrameCheckDetailInfo.setMealPeriod(sMealPeriod);
	}
	
	@Override
	public void FrameCheckDetailInfo_closeClick() {
		this.finishShow();
		m_oFrameCover.setVisible(false);
	}
}
