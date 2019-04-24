package app;
import java.math.BigDecimal;
import java.math.RoundingMode;
import core.Controller;
import templatebuilder.TemplateBuilder;
import virtualui.*;
public class FormEnterItemPLU extends VirtualUIForm implements FrameEnterItemPLUListener{
	TemplateBuilder m_oTemplateBuilder;
	private String m_sItemCode;
	private BigDecimal m_dQuantity;
	private boolean m_bUserCancel;
	private FrameEnterItemPLU m_oFrameEnterItemPLU;
	
	public FormEnterItemPLU(Controller oParentController){
		super(oParentController);		
		m_sItemCode = "";
		m_dQuantity = BigDecimal.ZERO;
		m_bUserCancel = false;
	}

	public boolean init() {
		m_oTemplateBuilder = new TemplateBuilder();
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmEnterItemPLU.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameEnterItemPLU = new FrameEnterItemPLU();
		m_oTemplateBuilder.buildFrame(m_oFrameEnterItemPLU, "fraEnterItemPLU");
		m_oFrameEnterItemPLU.init();
		// Add listener;
		m_oFrameEnterItemPLU.addListener(this);
		m_oFrameEnterItemPLU.setVisible(true);		// Show this frame during first initial
		oCoverFrame.attachChild(m_oFrameEnterItemPLU);
		
		return true;
	}
		
	public String getItemCode() {
		return m_sItemCode;
	}
	
	public BigDecimal getItemQuantity(){
		return m_dQuantity;
	}
	
	public void setTitle(String sTitle){
		m_oFrameEnterItemPLU.setTitle(sTitle);
	}
	
	public void setKeyboardType(String sType) {
		// TODO Auto-generated method stub
		m_oFrameEnterItemPLU.setKeyboardType(sType);
	}
	
	public void showKeyboard() {
		m_oFrameEnterItemPLU.showKeyboard();
	}
	
	public boolean isUserCancel() {
		return m_bUserCancel;
	}
	
	@Override
	public void FrameEnterItemPLU_clickOK(boolean clientValueCleared) {
		// TODO Auto-generated method stub
		BigDecimal oQuantity = BigDecimal.ZERO;
		try{
			oQuantity = new BigDecimal(m_oFrameEnterItemPLU.getQuantity());
			oQuantity = oQuantity.setScale(0, RoundingMode.UNNECESSARY);
		}catch(Exception e){
			oQuantity = BigDecimal.ZERO;
		}
		
		m_sItemCode = m_oFrameEnterItemPLU.getItemCode();
		m_dQuantity = oQuantity;
		// Finish showing this form
		this.finishShow();
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}

	@Override
	public void FrameEnterItemPLU_clickCancel() {
		// TODO Auto-generated method stub
		m_bUserCancel = true;
		
		// Finish showing this form
		this.finishShow();
		
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}

	@Override
	public void FrameEnterItemPLU_swipeCard(String sSwipeCardValue) {
		// TODO Auto-generated method stub
		m_oFrameEnterItemPLU.setItemCode(sSwipeCardValue);
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}

}
