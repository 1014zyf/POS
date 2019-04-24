package app;

import java.util.ArrayList;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUICanvas;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIWebView;

/** interface for the listeners/observers callback method */
interface FrameESignatureListener {
	void frameESignature_clickExit();
	void frameESignature_clickConfirm(String sESignature);
}

public class FrameESignature extends VirtualUIFrame implements FrameTitleHeaderListener{
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oTitleHeader;
	private VirtualUIButton m_oButtonToSignature;
	private VirtualUIButton m_oButtonToPreview;
	private VirtualUIButton m_oButtonClear;
	private VirtualUIButton m_oButtonConfirm;
	private VirtualUILabel	m_oLabelPaymentMethod;
	private VirtualUILabel	m_oLabelSignatureUnderline;
	private VirtualUILabel	m_oLabelSignatureBackground;
	private VirtualUILabel	m_oLabelSignHere;
	private VirtualUICanvas	m_oCanvasSignature;
	private VirtualUIFrame	m_oFraButtonClose;
	private VirtualUIFrame	m_oFrameLeftContent;
	private VirtualUIFrame	m_oFrameRightContent;
	
	private VirtualUIWebView m_oWebViewReceipt;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameESignatureListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameESignatureListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameESignatureListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FrameESignature(String sPaymentMethodName) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameESignatureListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraESignature.xml");
		
		// Title Bar Frame
        m_oTitleHeader = new FrameTitleHeader();
        m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
        m_oTitleHeader.init(true);
        m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("please_sign_your_signature"));
        m_oTitleHeader.addListener(this);
        this.attachChild(m_oTitleHeader);

		//Create Left Content Frame
		m_oFrameLeftContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftContent, "fraLeftContent");
		this.attachChild(m_oFrameLeftContent);
		
		// Signature Page button
		m_oButtonToSignature = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonToSignature, "btnSignature");
		m_oButtonToSignature.setValue(AppGlobal.g_oLang.get()._("signature_page"));
		m_oButtonToSignature.setVisible(false);
		m_oFrameLeftContent.attachChild(m_oButtonToSignature);
		
		// Check image
		m_oWebViewReceipt = new VirtualUIWebView();
		m_oTemplateBuilder.buildWebView(m_oWebViewReceipt, "wbvCheckPreview");
		m_oFrameLeftContent.attachChild(m_oWebViewReceipt);
		
		//Create Right Content Frame
		m_oFrameRightContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameRightContent, "fraRightContent");
		this.attachChild(m_oFrameRightContent);
		
		// Preview Page button
		m_oButtonToPreview = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonToPreview, "btnPrev");
		m_oButtonToPreview.setValue("<"+AppGlobal.g_oLang.get()._("back"));
		m_oButtonToPreview.setVisible(false);
		m_oFrameRightContent.attachChild(m_oButtonToPreview);
		
		// Clear button
		m_oButtonClear = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonClear, "btnClear");
		m_oButtonClear.setValue(AppGlobal.g_oLang.get()._("clear"));
		m_oButtonClear.addClickServerRequestSubmitElement(this);
		m_oButtonClear.setVisible(true);
		m_oFrameRightContent.attachChild(m_oButtonClear);
		
		// Confirm button
		m_oButtonConfirm = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonConfirm, "btnConfirm");
		m_oButtonConfirm.setValue(AppGlobal.g_oLang.get()._("confirm"));
		m_oButtonConfirm.addClickServerRequestSubmitElement(this);
		m_oButtonConfirm.setVisible(true);
		m_oFrameRightContent.attachChild(m_oButtonConfirm);
		
		// Payment Method Label
		m_oLabelPaymentMethod = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelPaymentMethod, "lblPaymentMethod");
		m_oLabelPaymentMethod.setValue(AppGlobal.g_oLang.get()._("payment_method") + ": " +sPaymentMethodName);
		m_oFrameRightContent.attachChild(m_oLabelPaymentMethod);
		
		// Signature Canvas Background
		m_oLabelSignatureBackground = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSignatureBackground, "lblCavasBackground");
		m_oFrameRightContent.attachChild(m_oLabelSignatureBackground);
		
		// Signature Canvas
		m_oCanvasSignature = new VirtualUICanvas();
		m_oTemplateBuilder.buildCanvas(m_oCanvasSignature, "cvsSignature");
		m_oCanvasSignature.setVisible(true);
		m_oCanvasSignature.addClickServerRequestSubmitElement(this);
		m_oFrameRightContent.attachChild(m_oCanvasSignature);
		
		// Signature Canvas Underline
		m_oLabelSignatureUnderline = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSignatureUnderline, "lblSignatureUnderline");
		m_oFrameRightContent.attachChild(m_oLabelSignatureUnderline);
		
		// Signature Here Lable
		m_oLabelSignHere = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSignHere, "lblSignHere");
		m_oLabelSignHere.setValue("( " + AppGlobal.g_oLang.get()._("please_sign_here") + " )");
		m_oLabelSignHere.setVisible(true);
		m_oFrameRightContent.attachChild(m_oLabelSignHere);
		
		// Vertical mobile view will show the m_oFrameLeftContent and m_oFrameRightContent one by one
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())){
			m_oButtonToSignature.setVisible(true);
			m_oButtonToPreview.setVisible(true);
		}
	}
	
	public void setCheckURL(String sURL){
		m_oWebViewReceipt.setSource(sURL);
	}
	
	public void switchDisplayFrame(boolean toPreviewCheckFrame){
		if(toPreviewCheckFrame){
			m_oFrameLeftContent.setVisible(true);
			m_oFrameRightContent.setVisible(false);
		}
		else {
			m_oFrameLeftContent.setVisible(false);
			m_oFrameRightContent.setVisible(true);
		}
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		// Find the clicked button
		
		if (m_oButtonToSignature.getId() == iChildId) {
			switchDisplayFrame(false);
			bMatchChild = true;
		}
		else if (m_oButtonToPreview.getId() == iChildId) {
			switchDisplayFrame(true);
			bMatchChild = true;
		}
		else if (m_oButtonClear.getId() == iChildId) {
			m_oCanvasSignature.setValue("");
			bMatchChild = true;
		}
		else if (m_oButtonConfirm.getId() == iChildId) {
			for (FrameESignatureListener listener : listeners) {
				// Raise the event to parent
				listener.frameESignature_clickConfirm(m_oCanvasSignature.getValue());
			}
			bMatchChild = true;
		}
		return bMatchChild;
	}

	@Override
	public void FrameTitleHeader_close() {
		for (FrameESignatureListener listener : listeners) {
			// Raise the event to parent
			listener.frameESignature_clickExit();
		}
	}

}
