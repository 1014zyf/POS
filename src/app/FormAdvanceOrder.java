package app;

import core.Controller;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

public class FormAdvanceOrder extends VirtualUIForm implements FrameAdvanceOrderListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameAdvanceOrder m_oFrameAdvanceOrder;
	
	private String m_sAdvanceOrderNum;
	private String m_sAdvanceOrderDate;
	private String m_sAdvanceOrderName = null;
	private String m_sAdvanceOrderPhone = null;
	private String m_sAdvanceOrderFax = null;
	private String m_sAdvanceOrderNote1 = null;
	private String m_sAdvanceOrderNote2 = null;
	private boolean m_bUserCancel = false;
	
	public FormAdvanceOrder(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		m_sAdvanceOrderNum = null;
		m_sAdvanceOrderDate = null;
		m_sAdvanceOrderName = null;
		m_sAdvanceOrderPhone = null;
		m_sAdvanceOrderFax = null;
		m_sAdvanceOrderNote1 = null;
		m_sAdvanceOrderNote2 = null;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmAdvanceOrder.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameAdvanceOrder = new FrameAdvanceOrder();
		m_oTemplateBuilder.buildFrame(m_oFrameAdvanceOrder, "fraAdvanceOrder");
		m_oFrameAdvanceOrder.addListener(this);
		this.attachChild(m_oFrameAdvanceOrder);
	}
	
	public String getAdvanceOrderNum() {
		return m_sAdvanceOrderNum;
	}
	
	public String getAdvanceOrderDate() {
		return m_sAdvanceOrderDate;
	}
	
	public String getAdvanceOrderName() {
		return m_sAdvanceOrderName;
	}
	
	public String getAdvanceOrderPhone() {
		return m_sAdvanceOrderPhone;
	}
	
	public String getAdvanceOrderFax() {
		return m_sAdvanceOrderFax;
	}
	
	public String getAdvanceOrderNote1() {
		return m_sAdvanceOrderNote1;
	}
	
	public String getAdvanceOrderNote2() {
		return m_sAdvanceOrderNote2;
	}
	
	public void setAdvanceOrderDetail(String sAdvanceOrderDate, String sAdvanceOrderName, String sAdvanceOrderPhone, String sAdvanceOrderFax, String sAdvanceOrderNote1, String sAdvanceOrderNote2) {
		m_oFrameAdvanceOrder.setAdvanceOrderDetail(sAdvanceOrderDate, sAdvanceOrderName, sAdvanceOrderPhone, sAdvanceOrderFax, sAdvanceOrderNote1, sAdvanceOrderNote2);
	}
	
	@Override
	public void FrameAdvanceOrder_clickCancel() {
		m_bUserCancel = true;
		this.finishShow();
	}

	@Override
	public void FrameAdvanceOrder_clickConfirm() {
		m_sAdvanceOrderDate = m_oFrameAdvanceOrder.getAdvanceOrderDate();
		m_sAdvanceOrderName = m_oFrameAdvanceOrder.getAdvanceOrderName();
		m_sAdvanceOrderPhone = m_oFrameAdvanceOrder.getAdvanceOrderPhone();
		m_sAdvanceOrderFax = m_oFrameAdvanceOrder.getAdvanceOrderFax();
		m_sAdvanceOrderNote1 = m_oFrameAdvanceOrder.getAdvanceOrderNote1();
		m_sAdvanceOrderNote2 = m_oFrameAdvanceOrder.getAdvanceOrderNote2();
	
		this.finishShow();
	}
	
	public boolean isUserCancel() {
		return m_bUserCancel;
	}
}
