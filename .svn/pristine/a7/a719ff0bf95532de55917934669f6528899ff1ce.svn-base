package app;

import java.util.List;

import core.Controller;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

public class FormItemDetail extends VirtualUIForm implements FrameItemDetailListener{
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameItemDetail m_oFrameItemDetail;
	private boolean m_bOrderItem;
	
	public FormItemDetail(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmItemDetail.xml");

		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Load form from template file
		m_oFrameItemDetail = new FrameItemDetail();
		m_oTemplateBuilder.buildFrame(m_oFrameItemDetail, "fraItemDetail");
		
		// Set whether order item
		m_bOrderItem = false;
		
		// Add listener
		m_oFrameItemDetail.addListener(this);
		this.attachChild(m_oFrameItemDetail);
	}
	
	public void initWithFuncCheckItem(FuncCheckItem oFuncCheckItem, int iPriceLevel, boolean bOrderingPanel, int iRoundItemDecimal, List<String> oMixAndMatchRuleAndItemList) {
		m_oFrameItemDetail.setupValue(oFuncCheckItem, iPriceLevel, bOrderingPanel, iRoundItemDecimal, oMixAndMatchRuleAndItemList);
	}
	
	public void setOrderButtonVisibility(boolean bVisible) {
		m_oFrameItemDetail.setOrderButtonVisibility(bVisible);
	}
	
	@Override
	public void FrameItemDetail_click() {
		this.finishShow();
	}

	@Override
	public void FrameItemDetail_orderClick() {
		this.finishShow();
		m_bOrderItem = true;
	}
	
	public boolean isOrderItem() {
		return m_bOrderItem;
	}
}
