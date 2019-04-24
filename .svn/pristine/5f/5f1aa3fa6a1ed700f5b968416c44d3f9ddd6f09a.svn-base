package commonui;

import java.util.ArrayList;
import java.util.List;

import core.Controller;
import app.AppGlobal;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormInputBox extends VirtualUIForm implements FrameInputBoxListener, FormCodeReaderPanelListener {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUIFrame m_oFrameCover;
	private FrameInputBox m_oFrameInputBox;
	private String m_sInputValue;
	private List<String> m_oInputValuesList;
	private String m_sSwipeCardValue;
	private String m_sQrCodeValue;
	private boolean m_bUserCancel;
	private boolean m_bKeepAliveAfterScannerValueChanged;
	private boolean m_bIsLarge;
	private boolean m_bGetInformation;
	
	public FormInputBox(Controller oParentController){
		super(oParentController);
		
		m_sInputValue = "";
		m_sSwipeCardValue = "";
		m_sQrCodeValue = "";
		m_bUserCancel = false;
		m_bKeepAliveAfterScannerValueChanged = false;
		m_bIsLarge = false;
		m_bGetInformation = false;
	}
	
	public boolean init() {
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmInputBox.xml");
		m_oFrameInputBox = new FrameInputBox();
		
		// Load form from template file
		if(!m_bIsLarge){
			m_oTemplateBuilder.loadTemplate("frmInputBox.xml");
			m_oTemplateBuilder.buildFrame(m_oFrameInputBox, "fraInputBox");
		}
		else{
			m_oTemplateBuilder.loadTemplate("frmInputBoxLarge.xml");
			m_oTemplateBuilder.buildFrame(m_oFrameInputBox, "fraInputBoxLarge");
		}
		m_oFrameInputBox.init(m_bIsLarge);
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Add listener;
		m_oFrameInputBox.addListener(this);
		m_oFrameInputBox.setVisible(true);		// Show this frame during first initial
		oCoverFrame.attachChild(m_oFrameInputBox);
		
		// Show keyboard
		//AppGlobal.g_oTerm.get().showKeyboard(true);
		
		m_bGetInformation = false;
		
		return true;
	}
	
	public boolean initWithInputNum(int iInputNum) {
		m_oTemplateBuilder = new TemplateBuilder();
		m_oInputValuesList = new ArrayList<String>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmInputBox.xml");
		
		// Background Cover Page
		m_oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCover, "fraCoverFrame");
		this.attachChild(m_oFrameCover);
		
		m_oFrameInputBox = new FrameInputBox();
		m_oTemplateBuilder.buildFrame(m_oFrameInputBox, "fraInputBox");
		m_oFrameInputBox.initWithInputNum(iInputNum);
		if(m_oFrameInputBox.getHeight() < m_oFrameCover.getHeight() && (m_oFrameInputBox.getTop() + m_oFrameInputBox.getHeight()) >= m_oFrameCover.getHeight()) {
			int iNewInputBoxTop = (m_oFrameCover.getHeight() - m_oFrameInputBox.getHeight()) / 2;
			m_oFrameInputBox.setTop(iNewInputBoxTop);
		}
		// Add listener;
		m_oFrameInputBox.addListener(this);
		m_oFrameInputBox.setVisible(true);		// Show this frame during first initial
		m_oFrameCover.attachChild(m_oFrameInputBox);
		
		// Show keyboard
		//AppGlobal.g_oTerm.get().showKeyboard(true);
		
		m_bGetInformation = false;
		
		return true;
	}
	
	public void setInputBoxTop(int iTop) {
		if(iTop > 0)
			m_oFrameInputBox.setTop(iTop);
	}

	public void setDefaultInputValue(String sDefaultValue){
		m_oFrameInputBox.setDefaultInputValue(sDefaultValue);
	}
	
	public String getInputValue() {
		return m_sInputValue;
	}
	
	public void setDefaultInputValue(int iIndex, String sDefaultValue) {
		m_oFrameInputBox.setDefaultInputValue(iIndex, sDefaultValue);
	}
	
	public String getInputValue(int iIndex) {
		try {
			return m_oInputValuesList.get(iIndex);
		}
		catch(Exception e) {
			return "";
		}
	}
	
	public String getSwipeCardValue() {
		return m_sSwipeCardValue;
	}
	
	public String getQrCodeValue() {
		return m_sQrCodeValue;
	}
	
	public void setTitle(String sTitle){
		m_oFrameInputBox.setTitle(sTitle);
	}
	
	public void setMessage(String sMessage){
		m_oFrameInputBox.setMessage(sMessage);
	}
	
	public void setMessages(List<String> oMsgList) {
		m_oFrameInputBox.setMessages(oMsgList);
	}
	
	public void setKeyboardType(String sType){
		m_oFrameInputBox.setKeyboardType(sType);
	}
	
	public void setKeyboardType(int iIndex, String sType){
		m_oFrameInputBox.setKeyboardType(iIndex, sType);
	}
	
	public void setEnterBlockUI(boolean bBlockUI){
		m_oFrameInputBox.setEnterBlockUI(bBlockUI);
	}
	
	public void setInputBoxSecurity(int iBoxIndex, boolean bSecurity) {
		m_oFrameInputBox.setInputBoxSecurity(iBoxIndex, bSecurity);
	}
	
	public void showKeyboard() {
		m_oFrameInputBox.showKeyboard();
	}
	
	public void showQrButton(String sButtonValue) {
		m_oFrameInputBox.showQrButton(sButtonValue);
	}
	
	public boolean isUserCancel() {
		return m_bUserCancel;
	}
	
	public void forceShowNumberPad(){
		m_oFrameInputBox.forceShowNumberPad();
	}
	
	public void setKeepAliveAfterScannerValueChanged(boolean bAlive){
		m_bKeepAliveAfterScannerValueChanged = bAlive;
	}
	
	public void setInputImageSource(String sMediaUrl){
		m_oFrameInputBox.setInputScreenImage(sMediaUrl);
	}
	
	public void setLargeInputImage(String sMediaUrl) {
		m_oFrameInputBox.setLargeInputImage(sMediaUrl);
	}
	
	public void setPaymentImageSourceAndRemainingAmount(String sImageURL, String sRemainingAmount){
		m_oFrameInputBox.setPaymentImgSourceAndRemainingAmt(sImageURL, sRemainingAmount);
	}
	
	public void setIsLarge(boolean bIsLarge){
		m_bIsLarge = bIsLarge;
	}
	
	public boolean getIsLarge(){
		return this.m_bIsLarge;
	}
	
	// Set client timeout to maximum for swipe card event (e.g. for SCANPAY)
	public void setMaximumClientTimeoutForSwipeCardEvent() {
		m_oFrameInputBox.setMaximumClientTimeoutForSwipeCardEvent();
	}
	
	public void hideAllCancelButton() {
		m_oFrameInputBox.hideAllCancelButton();
	}
	
	public void setCloseButtonVisible(boolean bShow) {
		m_oFrameInputBox.setCloseButtonVisible(bShow);
	}
	
	public void setGetInformationButtonVisible(boolean bShow) {
		m_oFrameInputBox.setGetInformationButtonVisible(bShow);
	}
	
	public void setGetInformationButtonValue(String sValue) {
		m_oFrameInputBox.setGetInformationButtonValue(sValue);
	}
	
	public boolean isGetInformation() {
		return m_bGetInformation;
	}
	
	public void setHint(int iIndex, String sHint){
		m_oFrameInputBox.setHint(iIndex, sHint);
	}
	
	@Override
	public void FrameInputBox_clickGetInformation() {
		m_bGetInformation = true;
		m_bUserCancel = true;
		this.finishShow();
	}
	
	@Override
	public void FrameInputBox_clickOK() {
		if(m_oFrameInputBox.getInputTxtboxCount() > 1) {
			for(int i = 0; i < m_oFrameInputBox.getInputTxtboxCount(); i++) {
				String sInput = m_oFrameInputBox.getInputValue(i);
				m_oInputValuesList.add(sInput);
			}
		}
		else {
			m_sInputValue = m_oFrameInputBox.getInputValue();
		}
		
		
		// Finish showing this form
		this.finishShow();
		
		// Resume client timeout to default for swipe card event
		m_oFrameInputBox.resumeClientTimeoutForSwipeCardEvent();
		
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}
	
	@Override
	public void FrameInputBox_clickCancel() {
		if(m_oFrameInputBox.getInputTxtboxCount() > 1) {
			m_oInputValuesList.clear();
		}
		else {
			m_sInputValue = null;
		}
		m_bUserCancel = true;
				
		// Finish showing this form
		this.finishShow();
		
		// Resume client timeout to default for swipe card event
		m_oFrameInputBox.resumeClientTimeoutForSwipeCardEvent();
		
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}
	
	@Override
	public void FrameInputBox_clickQrScan() {
		// Show QR Code Panel
		FormCodeReaderPanel oFormCodeReaderPanel = new FormCodeReaderPanel(this);
		oFormCodeReaderPanel.init();
		oFormCodeReaderPanel.setTitle(AppGlobal.g_oLang.get()._("qr_scan"));
		oFormCodeReaderPanel.setMessage(AppGlobal.g_oLang.get()._("please_place_the_code_inside_the_frame"));
		oFormCodeReaderPanel.addListener(this);
		oFormCodeReaderPanel.show();
		
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}
	
	@Override
	public void FrameInputBoxListener_swipeCard(String sSwipeCardValue) {
		// Get swipe card value
		m_sSwipeCardValue = sSwipeCardValue;
		if(m_oFrameInputBox.getInputTxtboxCount() > 1){
			this.setDefaultInputValue(0, sSwipeCardValue.replace("\r", "").replace("\n", ""));	
			m_oFrameInputBox.setFocusTextBox(1);
		}
		else
			this.setDefaultInputValue(sSwipeCardValue.replace("\r", "").replace("\n", ""));
		
		if(!m_bKeepAliveAfterScannerValueChanged)
			FrameInputBox_clickOK();
		
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}
	
	@Override
	public void formCodeReaderPanel_scanQrCode(String sQrCodeValue) {
		m_sQrCodeValue = sQrCodeValue;
		FrameInputBox_clickOK();
	}
}
