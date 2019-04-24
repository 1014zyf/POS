package app;

import java.math.BigDecimal;
import java.util.ArrayList;

import app.AppGlobal;
import commonui.FormDialogBox;

import core.Controller;
import core.externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FormRewriteCardOperationListener {
    boolean FormRewriteCardOperation_askLogin();
}

public class FormRewriteCardOperation extends VirtualUIForm implements FrameRewriteCardOperationListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameRewriteCardOperation m_oFrameRewriteCardOperation;
	
	private String m_sSendPacketString;
	
	// Internal usage
	private FuncRewriteCard m_oFuncRewriteCard;
	
	// Step control for special handling for one card solution dll return -6 timeout error
	private int m_iHandleTimeoutStep;
	// Flag to check if user cancel during special handling
	private boolean m_bCancelByUser;
	
	private boolean m_bProcessSuccess;
	
	// Operation Type
	private String m_sOperationType;
	
	// definition of operation type 
	public static final String TYPE_OPERATION_READ_CARD = "read_card";
	public static final String TYPE_OPERATION_ASK_NEED_PASSWORD = "ask_need_password";
	public static final String TYPE_OPERATION_DEDUCT_CARD_VALUE = "deduct_card_value";
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FormRewriteCardOperationListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FormRewriteCardOperationListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FormRewriteCardOperationListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
	
	public FormRewriteCardOperation(FuncRewriteCard oFuncRewriteCard, Controller oParentController){
		super(oParentController);
		
		m_oFuncRewriteCard = oFuncRewriteCard;
		
		m_bProcessSuccess = false;
		m_sSendPacketString = "";
		
		m_sOperationType = "";
		
		m_iHandleTimeoutStep = 0;
		m_bCancelByUser = false;
		
		listeners = new ArrayList<FormRewriteCardOperationListener>();
	}
	public boolean initForReadCard(boolean bClear, String sDisplayMessage, String sMediaUrl, String sRemainingBalance){
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Init Frame
		m_oFrameRewriteCardOperation = new FrameRewriteCardOperation();
		if(sRemainingBalance.isEmpty()){
			m_oTemplateBuilder.loadTemplate("frmRewriteCardOperation.xml");
			m_oTemplateBuilder.buildFrame(m_oFrameRewriteCardOperation, "fraRewriteCardOperation");
		}
		else{
			m_oTemplateBuilder.loadTemplate("frmRewriteCardOperationLarge.xml");
			m_oTemplateBuilder.buildFrame(m_oFrameRewriteCardOperation, "fraRewriteCardOperationLarge");
		}
		
		m_oFrameRewriteCardOperation.init(sMediaUrl, sRemainingBalance);
		m_oFrameRewriteCardOperation.showReadCardScreen(sDisplayMessage);
		
		// Add listener;
		m_oFrameRewriteCardOperation.addListener(this);
		this.attachChild(m_oFrameRewriteCardOperation);
		
		// clear the card information list
		if(bClear)
			m_oFuncRewriteCard.getCardInfoList().clear();
		
		return true;
	}
	
	public boolean showCardInformation(String sContent) {
		m_oTemplateBuilder = new TemplateBuilder();
		m_oTemplateBuilder.loadTemplate("frmRewriteCardOperation.xml");
		
		// Init Frame
		m_oFrameRewriteCardOperation = new FrameRewriteCardOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameRewriteCardOperation, "fraRewriteCardOperation");
		
		m_oFrameRewriteCardOperation.init("", "");
		m_oFrameRewriteCardOperation.showCardInformationScreen(sContent, m_oFuncRewriteCard.getCardInfoList());
		
		// Add listener;
		m_oFrameRewriteCardOperation.addListener(this);
		this.attachChild(m_oFrameRewriteCardOperation);
		
		return true;
	}
	
	public void rewriteOneCardSlnRequest(String sOperation) {
		
		// Before sending the next comand to device manager
		// Check if user click Cancel to stop the process
		if (m_bCancelByUser) {
			// Reset the flag
			m_bCancelByUser = false;
			
			// Ask staff enter ID and password again
			if (listeners.size() > 0 && !listeners.get(0).FormRewriteCardOperation_askLogin()) {
				// No valid ID and Password
				// Need to continue the checking
			} else {
				// Quit the operation
				// Cancel by user
				if(!isProcessSuccess())
					m_oFuncRewriteCard.writeLog(0, "Rewrite Card (One Card Sln) - Cancel by user during special handling");
				
				// Cancel is clicked in result screen, so can close directly
				this.finishOperation();
				
				return;
			}
		}
		
		Object[] oParameters;
		if (!sOperation.equals(TYPE_OPERATION_DEDUCT_CARD_VALUE))
			oParameters = new Object[2];
		else 
			oParameters = new Object[5];
		String sCommand = "";
		
		m_sOperationType = sOperation;
		oParameters[0] = m_oFuncRewriteCard.getDevice();
		oParameters[1] = m_oFuncRewriteCard.getBaudRate();
		
		if (m_sOperationType.equals(TYPE_OPERATION_DEDUCT_CARD_VALUE)){
			oParameters[2] = StringLib.BigDecimalToString(m_oFuncRewriteCard.getPaymentAmount(), 
					AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal());
			oParameters[3] = m_oFuncRewriteCard.getCardNumber();
			oParameters[4] = m_oFuncRewriteCard.getCardPassword();
		}
		
		if (m_sOperationType.equals(TYPE_OPERATION_READ_CARD))
			sCommand = "SmtReadCard";
		else if (m_sOperationType.equals(TYPE_OPERATION_ASK_NEED_PASSWORD))
			sCommand = "SmtCheckCardNeedPassword";
		else if (m_sOperationType.equals(TYPE_OPERATION_DEDUCT_CARD_VALUE))
			sCommand = "SmtDeductValue";
		
		m_sSendPacketString = createWinFcnMapString(sCommand, oParameters);
		m_oFuncRewriteCard.writeLog(0, "Rewrite Card (One Card Sln) - Send Data - "+m_sSendPacketString);
		m_oFrameRewriteCardOperation.createForwardEvent(m_sSendPacketString, m_oFuncRewriteCard.getTimeout(), 0);
	}
	
	public void setMessage(String sMessage){
		m_oFrameRewriteCardOperation.setMessage(sMessage);
	}
	
	public void showErrorMessage(int iErrorCode) {
		String sMessage = "";
		
		switch (iErrorCode) {
		case 1:
			sMessage = AppGlobal.g_oLang.get()._("unrecognized_card_or_no_card");
			break;
		case 2:
			sMessage = AppGlobal.g_oLang.get()._("clock_failure");
			break;
		case 3:
			sMessage = AppGlobal.g_oLang.get()._("wallet_data_error");
			break;
		case 4:
			sMessage = AppGlobal.g_oLang.get()._("after_deduct") + ", " + AppGlobal.g_oLang.get()._("read_card_error");
			break;
		case 6:
			sMessage = AppGlobal.g_oLang.get()._("exceed_consume_amount");
			break;
		case 7:
			sMessage = AppGlobal.g_oLang.get()._("data_length_error");
			break;
		case 8:
			sMessage = AppGlobal.g_oLang.get()._("card_device_is_not_registered");
			break;
		case 9:
			sMessage = AppGlobal.g_oLang.get()._("card_remain_balance_not_enough");
			break;
		case 11:
			sMessage = AppGlobal.g_oLang.get()._("open_comport_error");
			break;
		case 12:
			sMessage = AppGlobal.g_oLang.get()._("close_comport_error");
			break;
		case 13:
			sMessage = AppGlobal.g_oLang.get()._("please_click_the_retry_and_reswipe_or_contact_our_staff");
			break;
		case 14:
			sMessage = AppGlobal.g_oLang.get()._("cannot_open_dll_file");
			break;
		case 15:
			sMessage = AppGlobal.g_oLang.get()._("unrecognized_card_or_no_card");
			break;
		case 16:
			sMessage = AppGlobal.g_oLang.get()._("blacklist");
			break;
		case 18:
			sMessage = AppGlobal.g_oLang.get()._("card_usage_is_exceed");
			break;
		case 19:
			sMessage = AppGlobal.g_oLang.get()._("card_is_expired") + ", " + AppGlobal.g_oLang.get()._("or_card_is_not_activated");
			break;
		case 20:
			sMessage = AppGlobal.g_oLang.get()._("card_amount_is_too_much");
			break;
		case 26:
			sMessage = AppGlobal.g_oLang.get()._("using_different_card_to_deduct");
			break;
		case 84:
			sMessage = AppGlobal.g_oLang.get()._("need_card_password");
			break;
		case 85:
			sMessage = AppGlobal.g_oLang.get()._("card_data_error");
			break;
		case 102:
			sMessage = AppGlobal.g_oLang.get()._("not_allow_the_card_type");
			break;
		case 999:
			sMessage = AppGlobal.g_oLang.get()._("fail_to_deduct_amount") + System.lineSeparator()
					+ AppGlobal.g_oLang.get()._("please_select_payment_method_again");
			break;
		default:
			sMessage = AppGlobal.g_oLang.get()._("read_card_error_retry_please");
			break;
		}
		
		m_oFrameRewriteCardOperation.setMessage(AppGlobal.g_oLang.get()._("error") + " : " + sMessage);
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
	
	public void setProcessSuccess(boolean bFlag) {
		m_bProcessSuccess = bFlag;
	}
	
	public String getOperationType() {
		return m_sOperationType;
	}
	
	public void finishOperation() {
		// Finish showing this form
		this.finishShow();
	}
	
	public void showOKButton(boolean bShow) {
		m_oFrameRewriteCardOperation.showOKButton(bShow);
	}
	
	public void showRetryButton(boolean bShow) {
		m_oFrameRewriteCardOperation.showRetryButton(bShow);
	}
	
	public void showErrorImage(boolean bShow){
		m_oFrameRewriteCardOperation.showErrorImage(bShow);
	}
	
	public void showCancelButton(boolean bShow) {
		m_oFrameRewriteCardOperation.showCancelButton(bShow);
	}
	
	// sArgType : x1x2...
	// x1 : Argument 1
	//		- i : integer
	//		- s : string
	private String[] processArgString(String sResponse){
		try {
			if (sResponse.isEmpty())
				return null;
			
			// Convert the byte array to Object array
			String[] oReturnArgs = sResponse.split("\034");
			return oReturnArgs;
		} catch(Exception e) {
			AppGlobal.stack2Log(e);
			return null;
		}
	}

	private String getArgString(String[] sRetArgsString, int iIndex, int iLength, boolean bDecode){
		try {
			if(iIndex >= sRetArgsString.length)
				return "";
			
			String sArg = sRetArgsString[iIndex];
			StringBuilder sBuilder = new StringBuilder();
			String sReturn = "";
			if(bDecode){
				for( int i=0; i<sArg.length()-1; i+=2 ){
					//grab the hex in pairs
					String sHex = sArg.substring(i, (i + 2));
					//convert hex to decimal
					int iDec = Integer.parseInt(sHex, 16);
					//convert the decimal to character
					sBuilder.append((char)iDec);
				}
				sReturn = sBuilder.toString();
			}else{
				sReturn = sArg;
			}
			
			return sReturn;
		} catch(Exception e) {
			AppGlobal.stack2Log(e);
			return "";
		}
	}

	private void breakPacketDataString(String[] sRetArgsString) {
		if (m_oFuncRewriteCard.getModelType().equals(FuncRewriteCard.TYPE_MODEL_ONE_CARD_SLN)) {
			// break packet data for one card solution
			breakOneCardSlnPacketDataString(sRetArgsString);
		}
	}
	
	private void breakOneCardSlnPacketDataString(String[] sRetArgsString) {
		if (m_sOperationType.equals(TYPE_OPERATION_READ_CARD)) {
			m_oFuncRewriteCard.setCardNumber(getArgString(sRetArgsString, 1, 10, false));
			m_oFuncRewriteCard.setCardNumber1(getArgString(sRetArgsString, 2, 7, false));
			m_oFuncRewriteCard.setCardID(getArgString(sRetArgsString, 4, 20, false));
			m_oFuncRewriteCard.setCardMemberName(getArgString(sRetArgsString, 5, 8, false));
			m_oFuncRewriteCard.setCardAmount(getArgString(sRetArgsString, 6, 20, false));
			m_oFuncRewriteCard.setAmountBeforeDeduct(new BigDecimal(m_oFuncRewriteCard.getCardAmount()));
			
			m_oFuncRewriteCard.addCardInfoToList(AppGlobal.g_oLang.get()._("card_name"), m_oFuncRewriteCard.getCardMemberName());
			m_oFuncRewriteCard.addCardInfoToList(AppGlobal.g_oLang.get()._("card_number"), m_oFuncRewriteCard.getCardNumber());
			m_oFuncRewriteCard.addCardInfoToList(AppGlobal.g_oLang.get()._("card_amount"), m_oFuncRewriteCard.getCardAmount());
		}
		else if (m_sOperationType.equals(TYPE_OPERATION_ASK_NEED_PASSWORD)) {
			String sIsNeedPassword = "false";
			m_oFuncRewriteCard.setIsNeedPassword(false);
			sIsNeedPassword = getArgString(sRetArgsString, 1, 5, false);
			if (sIsNeedPassword.equals("true"))
				m_oFuncRewriteCard.setIsNeedPassword(true);
			m_oFuncRewriteCard.addCardInfoToList(AppGlobal.g_oLang.get()._("need_card_password"), sIsNeedPassword);
		}
		else if (m_sOperationType.equals(TYPE_OPERATION_DEDUCT_CARD_VALUE)) {
			m_oFuncRewriteCard.m_oShowCardInfoList.remove(AppGlobal.g_oLang.get()._("need_card_password"));
//			m_oFuncRewriteCard.m_oShowCardInfoList.remove(AppGlobal.g_oLang.get()._("card_no"));
			m_oFuncRewriteCard.m_oShowCardInfoList.remove(AppGlobal.g_oLang.get()._("card_amount"));
			m_oFuncRewriteCard.setAmountBeforeDeduct(new BigDecimal(getArgString(sRetArgsString, 1, 10, false)));
			m_oFuncRewriteCard.setAmountAfterDeduct(new BigDecimal(getArgString(sRetArgsString, 2, 10, false)));
			m_oFuncRewriteCard.setTransactionNo(getArgString(sRetArgsString, 3, 8, false));
			
			m_oFuncRewriteCard.addCardInfoToList(AppGlobal.g_oLang.get()._("amount_before_payment"), AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(m_oFuncRewriteCard.getAmountBeforeDeduct()));
			m_oFuncRewriteCard.addCardInfoToList(AppGlobal.g_oLang.get()._("amount_after_payment"), AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(m_oFuncRewriteCard.getAmountAfterDeduct()));
			//m_oFuncRewriteCard.addCardInfoToList(AppGlobal.g_oLang.get()._("transaction_no"), m_oFuncRewriteCard.getTransactionNo());
			
			int iCardType = -1;
			try {
				iCardType = Integer.parseInt(getArgString(sRetArgsString, 4, 2, false), 16);
			} catch (NumberFormatException e) {
				// Incorrect format return from device manager
			}
			m_oFuncRewriteCard.setCardType(iCardType);
			if (!m_oFuncRewriteCard.getCardType().isEmpty())
				m_oFuncRewriteCard.addCardInfoToList(AppGlobal.g_oLang.get()._("card_type"), m_oFuncRewriteCard.getCardType());
			
			m_oFuncRewriteCard.setOutBuffInfo(getArgString(sRetArgsString, 5, 16, false));
		}
	}
	
	@Override
	public void FrameRewriteCardOperation_clickOK() {
		this.finishOperation();
	}
	
	@Override
	public void FrameRewriteCardOperation_clickRetry() {
		if (m_oFuncRewriteCard.getModelType().equals(FuncRewriteCard.TYPE_MODEL_ONE_CARD_SLN)){
			m_oFrameRewriteCardOperation.hideCancelButton();
			rewriteOneCardSlnRequest(m_sOperationType);
		}
	}
	
	@Override
	public void FrameRewriteCardOperation_clickCancel() {
		// Cancel during special handling for one card solution dll return -6 timeout error
		if (m_iHandleTimeoutStep == 1) {
			m_bCancelByUser = true;
		} else {
			// Cancel by user
			if(!isProcessSuccess())
				m_oFuncRewriteCard.writeLog(0, "Rewrite Card (One Card Sln) - Cancel by user");
			
			// Cancel is clicked in result screen, so can close directly
			this.finishOperation();
		}
	}
	
	@Override
	public void FrameRewriteCardOperation_forward(String sResponse) {
		int iErrorCode;
		// Set the connection timeout to device manager to prevent timeout loop
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(0);
		
		if (!sResponse.isEmpty()) {
			// Process the response
			String[] sRetArgsString = processArgString(sResponse);
			if (sRetArgsString != null) {
				iErrorCode = Integer.parseInt(sRetArgsString[0]);
				if(iErrorCode == 0){
					m_oFuncRewriteCard.writeLog(0, "Rewrite Card (One Card Sln) - Receive Data - "+sResponse);
					
					// Special handling for one card solution dll return -6 timeout error
					if (m_iHandleTimeoutStep == 1) {
						// Read card success
						// Store the original card number
						String sOriginalCardNo = m_oFuncRewriteCard.getCardNumber();
						
						// Store the amount before deduct
						BigDecimal dOriginalAmount = m_oFuncRewriteCard.getAmountBeforeDeduct();
						
						// Store the current FuncRewriteCard value
						FuncRewriteCard oOriginalRewriteCardValue = new FuncRewriteCard();
						oOriginalRewriteCardValue.copyFrom(m_oFuncRewriteCard);

						// Process the new read card response
						breakPacketDataString(sRetArgsString);
//System.out.println("testtest = " + dOriginalAmount.toPlainString() + ", " + m_oFuncRewriteCard.getPaymentAmount().toPlainString() + ", " + m_oFuncRewriteCard.getCardAmount() + ", " + sOriginalCardNo + ", " + m_oFuncRewriteCard.getCardNumber());
						
						// Check if the same card is read
						if (!sOriginalCardNo.equals(m_oFuncRewriteCard.getCardNumber())) {
							// Not the same card!!!
							
							// Re-send the read card request
							initForReadCard(true, AppGlobal.g_oLang.get()._("please_swipe_the_previous_card") + ", " + AppGlobal.g_oLang.get()._("complete_payment"), "", "");
							// Reload the original FuncRewriteCard
							m_oFuncRewriteCard = oOriginalRewriteCardValue;
							
							// Send read card request again to get the current balance
							rewriteOneCardSlnRequest(FormRewriteCardOperation.TYPE_OPERATION_READ_CARD);
						} else {
							// Determine if the previous deduct amount is success or not by checking the current balance
							BigDecimal dCurrentBalance = new BigDecimal(m_oFuncRewriteCard.getCardAmount());
							BigDecimal dDeductAmount = m_oFuncRewriteCard.getPaymentAmount();
							
							if (dOriginalAmount.subtract(dDeductAmount).compareTo(dCurrentBalance) != 0) {
								// Balance not correct
								setProcessSuccess(false);
								showCancelButton(true);
								showRetryButton(false);
								showErrorMessage(999);
								m_oFrameRewriteCardOperation.setRetryCancelButtonTimer(false);
							} else {
								// Balance correct
								setProcessSuccess(true);
								showCancelButton(false);
								showRetryButton(false);
								breakPacketDataString(sRetArgsString);
								finishShow();
							}
							
							// Complete the special handling
							m_iHandleTimeoutStep = 0;
						}
					} else {
						// No device manager error
						setProcessSuccess(true);
						showCancelButton(false);
						showRetryButton(false);
						breakPacketDataString(sRetArgsString);
						finishShow();
					}
				} else if (iErrorCode == 1) {
					// no card detected and not use click cancel
					rewriteOneCardSlnRequest(m_sOperationType);
				} else {
					m_oFuncRewriteCard.writeLog(0, "Rewrite Card (One Card Sln) - Receive Data - "+sResponse);
					
					iErrorCode = Integer.parseInt(sRetArgsString[0]);
					
					if (m_sOperationType.equals(TYPE_OPERATION_DEDUCT_CARD_VALUE) && iErrorCode == 13) {
						// Special handling for one card solution dll return -6 timeout error during deduct amount
						m_oFuncRewriteCard.writeLog(0, "Rewrite Card (One Card Sln) *** Deduct amount receives timeout error from dll and start the special handling");
						
						initForReadCard(true, AppGlobal.g_oLang.get()._("error") + " : " + AppGlobal.g_oLang.get()._("error_in_deduct_amount") + System.lineSeparator()
										+ AppGlobal.g_oLang.get()._("please_swipe_card_again_to_confirm"), "", "");
						
						// Send read card request again to get the current balance
						rewriteOneCardSlnRequest(FormRewriteCardOperation.TYPE_OPERATION_READ_CARD);
						
						// Start the special flow for timeout
						m_iHandleTimeoutStep = 1;
					} else {
						if (m_iHandleTimeoutStep == 1) {
							// During special handling, keep call read card if receive error
							// Re-send the read card request
							initForReadCard(true, AppGlobal.g_oLang.get()._("error") + " : " + AppGlobal.g_oLang.get()._("error_in_deduct_amount") + System.lineSeparator()
									+ AppGlobal.g_oLang.get()._("please_swipe_card_again_to_confirm"), "", "");

							// Send read card request again to get the current balance
							rewriteOneCardSlnRequest(FormRewriteCardOperation.TYPE_OPERATION_READ_CARD);
						} else {
							// Device manager return error
							setProcessSuccess(false);
							showCancelButton(true);
							showRetryButton(true);
							showErrorImage(true);
							showErrorMessage(iErrorCode);
							m_oFrameRewriteCardOperation.setRetryCancelButtonTimer(false);
						}
					}
				}
			}
			else {
				m_oFuncRewriteCard.writeLog(0, "Rewrite Card (One Card Sln) - Receive Data - "+sResponse);
				
				if (m_iHandleTimeoutStep == 1) {
					// During special handling, keep call read card if receive error
					// Re-send the read card request
					initForReadCard(true, AppGlobal.g_oLang.get()._("error") + " : " + AppGlobal.g_oLang.get()._("error_in_deduct_amount") + System.lineSeparator() +
							AppGlobal.g_oLang.get()._("please_swipe_card_again_to_confirm"), "", "");

					// Send read card request again to get the current balance
					rewriteOneCardSlnRequest(FormRewriteCardOperation.TYPE_OPERATION_READ_CARD);
				} else {
					iErrorCode = Integer.parseInt(sRetArgsString[0]);
					// Device manager return error
					setProcessSuccess(false);
					showCancelButton(true);
					showRetryButton(true);
					switch(iErrorCode){
					case -1:
						setMessage(AppGlobal.g_oLang.get()._("error") + " : " + AppGlobal.g_oLang.get()._("no_response"));
						break;
					default:
						showErrorMessage(iErrorCode);
						break;
					}
				}
			}
		}
	}
	
	@Override
	public void FrameRewriteCardOperation_disconnect() {
		// Disconnect
		FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
		oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
		oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_build_connection"));
		oFormDialogBox.show();

		finishOperation();
	}
	
	@Override
	public void FrameRewriteCardOperation_timeout() {
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
