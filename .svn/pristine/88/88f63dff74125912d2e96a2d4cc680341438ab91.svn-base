
package app;

import java.math.BigDecimal;
import java.util.ArrayList;

import commonui.FormDialogBox;
import commonui.FormSelectionBox;
import core.Controller;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormCreditCardOperation extends VirtualUIForm implements FrameCreditCardOperationListener {
	TemplateBuilder m_oTemplateBuilder;

	private FrameCreditCardOperation m_oFrameCreditCardOperation;
	
	// Internal usage
	private FuncCreditCardOperation m_oFuncCreditCardOperation;
	
	private String m_sSendPacketString;
	
	private int m_iRetransmitLimit;

	private boolean m_bCancelByUser;
	private boolean m_bProcessSuccess;
	
	public FormCreditCardOperation(FuncCreditCardOperation oFuncCreditCardOperation, Controller oParentController) {
		super(oParentController);

		m_bCancelByUser = false;
		m_bProcessSuccess = false;
		
		m_oFuncCreditCardOperation = oFuncCreditCardOperation;
		m_iRetransmitLimit = 0;
		
		m_sSendPacketString = "";
	}

	public boolean initForReadCard(String sCheckNo, String sPaymentAmount, String sTipsAmount) {
		m_oTemplateBuilder = new TemplateBuilder();

		m_oTemplateBuilder.loadTemplate("frmCreditCardOperation.xml");
		
// Add by King Cheung 2017-12-13 ---Start---
		VirtualUIFrame oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameCover, "fraCoverFrame");
		this.attachChild(oFrameCover);
// Add by King Cheung 2017-12-13 ---End---
		
		// Login Frame
		m_oFrameCreditCardOperation = new FrameCreditCardOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameCreditCardOperation, "fraCreditCardOperation");
		m_oFrameCreditCardOperation.init(m_oFuncCreditCardOperation.getTimeout());
		m_oFrameCreditCardOperation.showReadCardScreen(sCheckNo, sPaymentAmount, sTipsAmount);
		// Add listener;
		m_oFrameCreditCardOperation.addListener(this);
		this.attachChild(m_oFrameCreditCardOperation);		

		return true;
	}
	
	public boolean initForVoidPayment(String sTraceNo) {	
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmCreditCardOperation.xml");
		
// Add by King Cheung 2017-12-13 ---Start---
		VirtualUIFrame oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameCover, "fraCoverFrame");
		this.attachChild(oFrameCover);
// Add by King Cheung 2017-12-13 ---End---
		
		// Login Frame
		m_oFrameCreditCardOperation = new FrameCreditCardOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameCreditCardOperation, "fraCreditCardOperation");
		m_oFrameCreditCardOperation.init(m_oFuncCreditCardOperation.getTimeout());
		m_oFrameCreditCardOperation.showVoidPaymentScreen(sTraceNo);
		// Add listener;
		m_oFrameCreditCardOperation.addListener(this);
		this.attachChild(m_oFrameCreditCardOperation);
		
		return true;
	}
	
	public boolean initForAdjustTips(String sTraceNo, String sTipsAmount) {	
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmCreditCardOperation.xml");
		
// Add by King Cheung 2017-12-13 ---Start---
		VirtualUIFrame oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameCover, "fraCoverFrame");
		this.attachChild(oFrameCover);
// Add by King Cheung 2017-12-13 ---End---
				
		// Login Frame
		m_oFrameCreditCardOperation = new FrameCreditCardOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameCreditCardOperation, "fraCreditCardOperation");
		m_oFrameCreditCardOperation.init(m_oFuncCreditCardOperation.getTimeout());
		m_oFrameCreditCardOperation.showAdjustmentScreen(sTraceNo, sTipsAmount);
		// Add listener;
		m_oFrameCreditCardOperation.addListener(this);
		this.attachChild(m_oFrameCreditCardOperation);
		
		return true;
	}
	
	public boolean initForDccOptOut() {	
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmCreditCardOperation.xml");
		
// Add by King Cheung 2017-12-13 ---Start---
		VirtualUIFrame oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameCover, "fraCoverFrame");
		this.attachChild(oFrameCover);
// Add by King Cheung 2017-12-13 ---End---
		
		m_oFrameCreditCardOperation = new FrameCreditCardOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameCreditCardOperation, "fraCreditCardOperation");
		m_oFrameCreditCardOperation.init(m_oFuncCreditCardOperation.getTimeout());
		m_oFrameCreditCardOperation.showDccOptOutScreen();
		// Add listener;
		m_oFrameCreditCardOperation.addListener(this);
		this.attachChild(m_oFrameCreditCardOperation);
		
		return true;
	}
	
	public void onlineCreditCardSpectraSalesRequest(String sInvoiceNum, BigDecimal oPaymentTotal, BigDecimal oTips, String sCreditCardMethodType) {
		Object[] oParameters = new Object[5];
		oParameters[0] = m_oFuncCreditCardOperation.getDevice();
		oParameters[1] = m_oFuncCreditCardOperation.getBaudRate();
		oParameters[2] = m_oFuncCreditCardOperation.getFlowControl();
		oParameters[3] = m_oFuncCreditCardOperation.getTimeout();
		oParameters[4] = m_oFuncCreditCardOperation.spectraSalesRequest(sInvoiceNum, oPaymentTotal.add(oTips), oTips, sCreditCardMethodType);
		
		m_sSendPacketString = createWinFcnMapString("extDeviceSerialPortIO", oParameters);

		m_oFuncCreditCardOperation.writeLog(m_oFuncCreditCardOperation.getInterfaceId(), "Online Credit Card (Spectra) - Send Data - "+m_sSendPacketString);
		m_oFrameCreditCardOperation.createForwardEvent(m_sSendPacketString, m_oFuncCreditCardOperation.getTimeout(), 0);
	}
	
	public void onlineCreditCardCtbcSalesRequest(String sInvoiceNum, BigDecimal oPaymentTotal, BigDecimal oTips, String sCreditCardMethodType) {
		Object[] oParameters = new Object[5];
		oParameters[0] = m_oFuncCreditCardOperation.getDevice();
		oParameters[1] = m_oFuncCreditCardOperation.getBaudRate();
		oParameters[2] = m_oFuncCreditCardOperation.getFlowControl();
		oParameters[3] = m_oFuncCreditCardOperation.getTimeout() / 1000;
		oParameters[4] = m_oFuncCreditCardOperation.ctbcSalesRequest(sInvoiceNum, oPaymentTotal.add(oTips), oTips, sCreditCardMethodType);

		m_sSendPacketString = createWinFcnMapString("ctbcCreditCardMachineRequest", oParameters);

		m_oFuncCreditCardOperation.writeLog(m_oFuncCreditCardOperation.getInterfaceId(), "Online Credit Card (CTBC) - Send Data - "+m_sSendPacketString);
		m_oFrameCreditCardOperation.createForwardEvent(m_sSendPacketString, m_oFuncCreditCardOperation.getTimeout(), 0);
	}
	
	public void onlineCreditCardSpectraVoidRequest(String sTraceNo, String sPassword, String sCreditCardMethodType) {
		Object[] oParameters = new Object[5];
		oParameters[0] = m_oFuncCreditCardOperation.getDevice();
		oParameters[1] = m_oFuncCreditCardOperation.getBaudRate();
		oParameters[2] = m_oFuncCreditCardOperation.getFlowControl();
		oParameters[3] = m_oFuncCreditCardOperation.getTimeout();
		oParameters[4] = m_oFuncCreditCardOperation.spectraVoidRequest(sTraceNo, sPassword, "", sCreditCardMethodType);

		m_sSendPacketString = createWinFcnMapString("extDeviceSerialPortIO", oParameters);

		m_oFuncCreditCardOperation.writeLog(m_oFuncCreditCardOperation.getInterfaceId(), "Online Credit Card (Spectra) - Send Data - "+m_sSendPacketString);
		m_oFrameCreditCardOperation.createForwardEvent(m_sSendPacketString, m_oFuncCreditCardOperation.getTimeout(), 0);
	}
	
	public void onlineCreditCardSpectraAdjustTips(String sTraceNo, String sPassword, BigDecimal oNewPaymentTotal, BigDecimal oNewTipsTotal) {
		Object[] oParameters = new Object[5];
		oParameters[0] = m_oFuncCreditCardOperation.getDevice();
		oParameters[1] = m_oFuncCreditCardOperation.getBaudRate();
		oParameters[2] = m_oFuncCreditCardOperation.getFlowControl();
		oParameters[3] = m_oFuncCreditCardOperation.getTimeout();
		oParameters[4] = m_oFuncCreditCardOperation.spectraAdjustTipsRequest(sTraceNo, sPassword, oNewPaymentTotal, oNewTipsTotal);
		
		m_sSendPacketString = createWinFcnMapString("extDeviceSerialPortIO", oParameters);

		m_oFuncCreditCardOperation.writeLog(m_oFuncCreditCardOperation.getInterfaceId(), "Online Credit Card (Spectra) - Send Data - "+m_sSendPacketString);
		m_oFrameCreditCardOperation.createForwardEvent(m_sSendPacketString, m_oFuncCreditCardOperation.getTimeout(), 0);
	}
	
	public void onlineCreditCardSpectraDccOptOut(String sCheckPrefixNum, String sTraceNum, String sPassword) {
		Object[] oParameters = new Object[5];
		oParameters[0] = m_oFuncCreditCardOperation.getDevice();
		oParameters[1] = m_oFuncCreditCardOperation.getBaudRate();
		oParameters[2] = m_oFuncCreditCardOperation.getFlowControl();
		oParameters[3] = m_oFuncCreditCardOperation.getTimeout();
		oParameters[4] = m_oFuncCreditCardOperation.spectraDccOptOutRequest(sCheckPrefixNum, sTraceNum, sPassword);
		
		m_sSendPacketString = createWinFcnMapString("extDeviceSerialPortIO", oParameters);
		m_oFuncCreditCardOperation.writeLog(m_oFuncCreditCardOperation.getInterfaceId(), "Online Credit Card (Spectra) - Send Data - "+m_sSendPacketString);
		m_oFrameCreditCardOperation.createForwardEvent(m_sSendPacketString, m_oFuncCreditCardOperation.getTimeout(), 0);
	}
	
	public String createWinFcnMapString(String sType, Object[] oParameters) {
		StringBuilder sArgString = new StringBuilder();
		
		for (int i = 0; i < oParameters.length; i++) {
			if (oParameters[i] != null) {
				if (i != 0)
					sArgString.append((char)0x1c);

				sArgString.append(oParameters[i]);
			}
		}
		sArgString.append((char)0x02);
		
		return String.format("\001win_fcn_map\002%s\002%s\004", sType, sArgString.toString());
	}
	
	public boolean isProcessSuccess() {
		return m_bProcessSuccess;
	}

	public boolean isCancelByUser() {
		return m_bCancelByUser;
	}

	public void finishOperation() {
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public void FrameCreditCardOperation_clickCancel() {
		// Cancel by user
		if (!m_bProcessSuccess)
			m_bCancelByUser = true;

		finishOperation();
	}

	@Override
	public void FrameCreditCardOperation_forward(String sResponse) {
		// Set the connection timeout to device manager to prevent timeout loop
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(0);
		
		if (!sResponse.isEmpty() && (m_oFuncCreditCardOperation.getModelType().equals(FuncCreditCardOperation.TYPE_MODEL_CTBC)?m_oFuncCreditCardOperation.breakPacketDataStringForCtbc(sResponse):m_oFuncCreditCardOperation.breakPacketDataString(sResponse)))
			m_bProcessSuccess = true;
		else {
			if (!m_oFuncCreditCardOperation.isReceivedPacket()) {
				m_iRetransmitLimit++;
				if (m_iRetransmitLimit > m_oFuncCreditCardOperation.getRetransmitLimit()) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_receive_reply_abort_waiting_response_packet"));
					oFormDialogBox.show();
					
					finishOperation();
				} else {
					m_oFuncCreditCardOperation.writeLog(m_oFuncCreditCardOperation.getInterfaceId(), "Online Credit Card (Spectra) - Send Data (Retransmit) - "+m_sSendPacketString);
					m_oFrameCreditCardOperation.createForwardEvent(m_sSendPacketString, m_oFuncCreditCardOperation.getTimeout(), m_oFuncCreditCardOperation.getSocketDelay());
				}
				return;
			}
			
			if (m_oFuncCreditCardOperation.isTerminalBusy()) {
				ArrayList<String> oOptionList = new ArrayList<String>();
				oOptionList.add(AppGlobal.g_oLang.get()._("retry_credit_card_action"));
				oOptionList.add(AppGlobal.g_oLang.get()._("abort_action"));
				
				FormSelectionBox oFormSelectionBox = new FormSelectionBox(this);
				oFormSelectionBox.initWithSingleSelection(m_oFuncCreditCardOperation.getLastErrorString() + System.lineSeparator()
										+ AppGlobal.g_oLang.get()._("please_select_the_action_type"), oOptionList, false);
				oFormSelectionBox.show();
				if (oFormSelectionBox.isUserCancel()) {
					// Back to login page
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cancel_to_continue_operation"));
					oFormDialogBox.show();
					return;
				} else {
					m_iRetransmitLimit++;
					m_oFuncCreditCardOperation.writeLog(m_oFuncCreditCardOperation.getInterfaceId(), "Online Credit Card (Spectra) - Send Data (Retransmit) - "+m_sSendPacketString);
					m_oFrameCreditCardOperation.createForwardEvent(m_sSendPacketString, m_oFuncCreditCardOperation.getTimeout(), m_oFuncCreditCardOperation.getSocketDelay());
					return;
				}
			} else {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("payment_fail") + ":" + System.lineSeparator()
						+ m_oFuncCreditCardOperation.getLastErrorString());
				oFormDialogBox.show();
				oFormDialogBox = null;
			}
		}
		
		finishOperation();
	}

	@Override
	public void FrameCreditCardOperation_disconnect() {
		// Disconnect
		if (m_bProcessSuccess == false) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("disconnect_from_smart_card_device"));
			oFormDialogBox.show();

			finishOperation();
		}
	}

	@Override
	public void FrameCreditCardOperation_timeout() {
		// Timeout
		if (m_bProcessSuccess == false) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("timeout"));
			oFormDialogBox.show();

			try {
				StringBuilder sb = new StringBuilder();
				sb.append("Timeout during operation");
				AppGlobal.writeDebugLog("FromCreditCardOperation", new Exception().getStackTrace()[0].getMethodName(), sb.toString());
			} catch(Exception e) {
				AppGlobal.stack2Log(e);
			}
			
			finishOperation();
		}
	}
}
