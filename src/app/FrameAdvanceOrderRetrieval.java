package app;

import java.math.BigDecimal;
import java.util.ArrayList;

import commonui.FormDialogBox;
import commonui.FrameNumberPad;
import commonui.FrameNumberPadListener;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;
import virtualui.VirtualUITextbox;
import virtualui.VirtualUIWebView;

/** interface for the listeners/observers callback method */
interface FrameAdvanceOrderRetrievalListener {
	void FrameAdvanceOrderRetrieval_clickExit();
	void FrameAdvanceOrderRetrieval_clickEnter();
}

public class FrameAdvanceOrderRetrieval extends VirtualUIFrame implements FrameNumberPadListener, FrameTitleHeaderListener {
	TemplateBuilder m_oTemplateBuilder;

	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUIWebView m_oWebViewReceipt;
	private VirtualUIList m_oListFunction;
	private VirtualUILabel m_oLabelAdvanceOrderDepositTitle;
	private VirtualUITextbox m_oTextboxAdvanceOrderDeposit;	
	private FrameNumberPad m_oFrameNumberPad;
	private VirtualUILabel m_oLabelAdvanceOrderDetailTitle;
	private BigDecimal m_dInputDeposit;
	private BigDecimal m_dDefaultDeposit;	
	//private VirtualUIButton m_oButtonExit;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameAdvanceOrderRetrievalListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameAdvanceOrderRetrievalListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameAdvanceOrderRetrievalListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FrameAdvanceOrderRetrieval(){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameAdvanceOrderRetrievalListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraAdvanceOrderRetrieval.xml");
		
		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
	    m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
	    m_oFrameTitleHeader.init(true);
	    m_oFrameTitleHeader.addListener(this);
	    m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("retrieve_advance_order"));
	    this.attachChild(m_oFrameTitleHeader);
	    
		// Review Area
		m_oWebViewReceipt = new VirtualUIWebView();
		m_oTemplateBuilder.buildWebView(m_oWebViewReceipt, "wbvReceipt");
		this.attachChild(m_oWebViewReceipt);
		
		// Function list
		m_oListFunction = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListFunction, "listFunction");
		this.attachChild(m_oListFunction);
		
		//Advance Order Detail Title
		m_oLabelAdvanceOrderDetailTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAdvanceOrderDetailTitle, "lblAdvanceOrderDetailTitle");
		m_oLabelAdvanceOrderDetailTitle.setValue(AppGlobal.g_oLang.get()._("advance_order_detail")+":");
		m_oListFunction.attachChild(m_oLabelAdvanceOrderDetailTitle);
		
		//Deposit Label Title
		m_oLabelAdvanceOrderDepositTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAdvanceOrderDepositTitle, "lblAdvanceOrderDepositAmountTitle");
		m_oLabelAdvanceOrderDepositTitle.setValue(AppGlobal.g_oLang.get()._("please_input_deposit_amount"));
		
		//Deposit TextBox
		m_oTextboxAdvanceOrderDeposit = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxAdvanceOrderDeposit, "txtboxAdvanceOrderDepositAmount");
		m_oTextboxAdvanceOrderDeposit.setFocusWhenShow(true);
		
		
		//Number Pad
		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.setEnterBlockUI(true);
		m_oFrameNumberPad.clearEnterSubmitId();
		m_oFrameNumberPad.setEnterSubmitId(m_oTextboxAdvanceOrderDeposit);
		m_oFrameNumberPad.addListener(this);
		
	}
	
	public void setCheckURL(String sURL){
		m_oWebViewReceipt.setSource(sURL);
	}
	
	public void setCheckDeposit(String sCheckDeposit){
		m_oTextboxAdvanceOrderDeposit.setValue(sCheckDeposit);
		m_dDefaultDeposit = new BigDecimal(sCheckDeposit);
	}	
	
	public void setAdvanceOrderDetail(ArrayList<String> oAdvanceOrderDetail) {
		ArrayList<String> oAdvanceOrderDetailLabel = new ArrayList<String>();	
		oAdvanceOrderDetailLabel.add(AppGlobal.g_oLang.get()._("reference_no")+(":"));
		oAdvanceOrderDetailLabel.add(AppGlobal.g_oLang.get()._("name")+(":"));
		oAdvanceOrderDetailLabel.add(AppGlobal.g_oLang.get()._("phone_number")+(":"));
		oAdvanceOrderDetailLabel.add(AppGlobal.g_oLang.get()._("deposit_amount")+(":"));
		
		for(int i =0 ; i< oAdvanceOrderDetail.size(); i++)
			addAdvanceOrderDetail(oAdvanceOrderDetailLabel.get(i), oAdvanceOrderDetail.get(i));
		
		m_oListFunction.attachChild(m_oLabelAdvanceOrderDepositTitle);
		m_oListFunction.attachChild(m_oTextboxAdvanceOrderDeposit);
		m_oListFunction.attachChild(m_oFrameNumberPad);
	}
	
	private void addAdvanceOrderDetail(String sTitle, String sContent) {
		VirtualUIFrame oFrameBasicDetail = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameBasicDetail, "fraAdvanceOrderDetail");
		
		VirtualUILabel oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelTitle, "lblListTitle");
		oLabelTitle.setValue(sTitle);
		oFrameBasicDetail.attachChild(oLabelTitle);

		VirtualUILabel oLabelContent = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelContent, "lblContent");
		oLabelContent.setValue(sContent);
		oFrameBasicDetail.attachChild(oLabelContent);
				
		m_oListFunction.attachChild(oFrameBasicDetail);
	}
		
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		return bMatchChild;
	}
	
	public String getDepositNumber(){
		return m_oTextboxAdvanceOrderDeposit.getValue();
	}
	
	@Override
	public void FrameNumberPad_clickEnter() {
		if(!m_oTextboxAdvanceOrderDeposit.getValue().isEmpty())
			m_dInputDeposit = new BigDecimal(m_oTextboxAdvanceOrderDeposit.getValue());
		else
			m_dInputDeposit = BigDecimal.ZERO;
		if(m_oTextboxAdvanceOrderDeposit.getValue().isEmpty() || m_dInputDeposit.compareTo(BigDecimal.ZERO) < 0 || m_dDefaultDeposit.compareTo(m_dInputDeposit) < 0){
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("invalid_deposit_amount"));
			oFormDialogBox.show();
			return;
		}
		for (FrameAdvanceOrderRetrievalListener listener : listeners) {
			listener.FrameAdvanceOrderRetrieval_clickEnter();
		}
	}
	
	@Override
	public void FrameNumberPad_clickCancel() {
		m_oTextboxAdvanceOrderDeposit.setValue("");
	}

	@Override
	public void FrameNumberPad_clickNumber(String string) {
	}
	
	@Override
	public void FrameTitleHeader_close() {
		for (FrameAdvanceOrderRetrievalListener listener : listeners) {
			// Raise the event to parent
			listener.FrameAdvanceOrderRetrieval_clickExit();
		}
	}
}
