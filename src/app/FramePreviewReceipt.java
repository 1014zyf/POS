package app;

import java.util.ArrayList;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIWebView;

/** interface for the listeners/observers callback method */
interface FramePreviewReceiptListener {
	void framePreviewReceipt_clickExit();
	void framePreviewReceipt_clickReprintReceipt();
	void framePreviewReceipt_clickReleasePayment();
	void framePreviewReceipt_clickVoidPaidCheck();
	void framePreviewReceipt_clickAdjustTips();
	void framePreviewReceipt_clickAdjustPayments();
	void framePreviewReceipt_clickSetMemberForClosedCheck();
}

public class FramePreviewReceipt extends VirtualUIFrame implements FrameTitleHeaderListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUIWebView m_oWebViewReceipt;
	private VirtualUIFrame m_oListFunction;
	private VirtualUIButton m_oButtonReprintReceipt;
	private VirtualUIButton m_oButtonReleasePayment;
	private VirtualUIButton m_oButtonVoidPaidCheck;
	private VirtualUIButton m_oButtonAdjustTips;
	private VirtualUIButton m_oButtonAdjustPayments;
	private VirtualUIButton m_oButtonSetMemberForClosedCheck;
	
	
	private FrameTitleHeader m_oTitleHeader;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FramePreviewReceiptListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FramePreviewReceiptListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FramePreviewReceiptListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FramePreviewReceipt() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FramePreviewReceiptListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraPreviewReceipt.xml");
		
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("receipt_preview"));
		m_oTitleHeader.addListener(this);
		this.attachChild(m_oTitleHeader);
		
		VirtualUIFrame oFrameReceipt = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameReceipt, "fraReceipt");
		this.attachChild(oFrameReceipt);

		// Review Area
		m_oWebViewReceipt = new VirtualUIWebView();
		m_oTemplateBuilder.buildWebView(m_oWebViewReceipt, "wbvReceipt");
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oWebViewReceipt.setLeft((oFrameReceipt.getWidth() - m_oWebViewReceipt.getWidth()) / 2);
		oFrameReceipt.attachChild(m_oWebViewReceipt);
		
		// Function list
		m_oListFunction = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oListFunction, "fraFunctionList");
		this.attachChild(m_oListFunction);
		
		// Reprint receipt button
		m_oButtonReprintReceipt = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonReprintReceipt, "btnReprintReceipt");
		m_oButtonReprintReceipt.setValue(AppGlobal.g_oLang.get()._("reprint_receipt"));
		m_oButtonReprintReceipt.setVisible(true);
		m_oListFunction.attachChild(m_oButtonReprintReceipt);
		
		// Release payment button
		m_oButtonReleasePayment = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonReleasePayment, "btnReleasePayment");
		m_oButtonReleasePayment.setValue(AppGlobal.g_oLang.get()._("release_payment"));
		m_oButtonReleasePayment.setVisible(true);
		m_oListFunction.attachChild(m_oButtonReleasePayment);
		
		// Void paid check button
		m_oButtonVoidPaidCheck = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonVoidPaidCheck, "btnVoidPaidCheck");
		m_oButtonVoidPaidCheck.setValue(AppGlobal.g_oLang.get()._("void_paid_check"));
		m_oButtonVoidPaidCheck.setVisible(true);
		m_oListFunction.attachChild(m_oButtonVoidPaidCheck);
		
		// Adjust tips
		m_oButtonAdjustTips = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonAdjustTips, "btnAdjustTips");
		m_oButtonAdjustTips.setValue(AppGlobal.g_oLang.get()._("adjust_tips"));
		m_oButtonAdjustTips.setVisible(true);
		m_oListFunction.attachChild(m_oButtonAdjustTips);
		
		// Adjust payments
		m_oButtonAdjustPayments = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonAdjustPayments, "btnAdjustPayments");
		m_oButtonAdjustPayments.setValue(AppGlobal.g_oLang.get()._("adjust_payments"));
		m_oButtonAdjustPayments.setVisible(true);
		m_oListFunction.attachChild(m_oButtonAdjustPayments);
		
		// Set member for closed check
		m_oButtonSetMemberForClosedCheck = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSetMemberForClosedCheck, "btnSetMemberForClosedCheck");
		m_oButtonSetMemberForClosedCheck.setValue(AppGlobal.g_oLang.get()._("set_online_member") + System.lineSeparator() + "(" + AppGlobal.g_oLang.get()._("paid_check") + ")");
		m_oButtonSetMemberForClosedCheck.setVisible(false);
		m_oListFunction.attachChild(m_oButtonSetMemberForClosedCheck);
	}
	
	public void setCheckURL(String sURL){
		m_oWebViewReceipt.setSource(sURL);
	}
	
	public void setFunctionButtonVisible(boolean bPastDateCheck) {
		m_oButtonReprintReceipt.setVisible(true);
		if (bPastDateCheck) {
			m_oButtonAdjustPayments.setVisible(false);
			m_oButtonAdjustTips.setVisible(false);
			m_oButtonReleasePayment.setVisible(false);
			m_oButtonVoidPaidCheck.setVisible(false);
		} else {
			m_oButtonAdjustPayments.setVisible(true);
			m_oButtonAdjustTips.setVisible(true);
			m_oButtonReleasePayment.setVisible(true);
			m_oButtonVoidPaidCheck.setVisible(true);
		}
	}
	
	public void setSetMemberButtonVisible(boolean bVisible) {
		m_oButtonSetMemberForClosedCheck.setVisible(bVisible);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (m_oButtonReprintReceipt.getId() == iChildId) {
			for (FramePreviewReceiptListener listener : listeners) {
				// Raise the event to parent
				listener.framePreviewReceipt_clickReprintReceipt();
			}
			bMatchChild = true;
		} else if (m_oButtonReleasePayment.getId() == iChildId) {
			for (FramePreviewReceiptListener listener : listeners) {
				// Raise the event to parent
				listener.framePreviewReceipt_clickReleasePayment();
			}
			bMatchChild = true;
		} else if (m_oButtonVoidPaidCheck.getId() == iChildId) {
			for (FramePreviewReceiptListener listener : listeners) {
				// Raise the event to parent
				listener.framePreviewReceipt_clickVoidPaidCheck();
			}
			bMatchChild = true;
		} else if (m_oButtonAdjustTips.getId() == iChildId) {
			for (FramePreviewReceiptListener listener : listeners) {
				// Raise the event to parent
				listener.framePreviewReceipt_clickAdjustTips();
			}
			bMatchChild = true;
		} else if (m_oButtonAdjustPayments.getId() == iChildId) {
			for (FramePreviewReceiptListener listener : listeners) {
				// Raise the event to parent
				listener.framePreviewReceipt_clickAdjustPayments();
			}
			bMatchChild = true;
		}else if (m_oButtonSetMemberForClosedCheck.getId() == iChildId) {
			for (FramePreviewReceiptListener listener : listeners) {
				// Raise the event to parent
				listener.framePreviewReceipt_clickSetMemberForClosedCheck();
			}
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
	
	@Override
	public void FrameTitleHeader_close() {
		for (FramePreviewReceiptListener listener : listeners) {
			// Raise the event to parent
			listener.framePreviewReceipt_clickExit();
		}
	}
}
