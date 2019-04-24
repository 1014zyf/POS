package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import core.Controller;
import externallib.StringLib;
import om.PosCheckPayment;
import om.PosInterfaceConfig;
import om.PosPaymentGatewayTransactions;
import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FormPaymentCardAuthorizationListener {
	boolean formPaymentCardAuthorization_ClickTransferAuthorization(String sAuthorizationType, PosPaymentGatewayTransactions oSelectedTopupPaymentGatewayTranscation, PosInterfaceConfig m_oPosInterfaceConfig);
	boolean formPaymentCardAuthorization_PartialSendCheck();
	void formPaymentCardAuthorization_voidPayment(FuncCheck oFuncCheck, int iIndex);
	void formPaymentCardAuthorization_quitCheck();
}

public class FormPaymentCardAuthorization extends VirtualUIForm implements FramePaymentCardAuthorizationListener {
	private TemplateBuilder m_oTemplateBuilder;	
	private FramePaymentCardAuthorization m_oFramePaymentCardAuthorization;

	private FuncCheck m_oFuncCheck;
	private PosInterfaceConfig m_oPosInterfaceConfig;
	private boolean m_bIsSuccessToCancelUnattachAuthPayment;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FormPaymentCardAuthorizationListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FormPaymentCardAuthorizationListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FormPaymentCardAuthorizationListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
		
	public FormPaymentCardAuthorization(Controller oParentController) {
		super(oParentController);
		m_oTemplateBuilder = new TemplateBuilder();		
		m_oTemplateBuilder.loadTemplate("frmPaymentCardAuthorization.xml");
		
	}
	
	public boolean init(FuncCheck oFuncCheck, PosInterfaceConfig oPosInterfaceConfig, String sTitle, String sAuthType) {
		listeners = new ArrayList<FormPaymentCardAuthorizationListener>();
		this.m_bIsSuccessToCancelUnattachAuthPayment = false;
		this.m_oFuncCheck = oFuncCheck;
		this.m_oPosInterfaceConfig = oPosInterfaceConfig;
		
		// Get the request timeout
		int iRequestTimeout = m_oPosInterfaceConfig.getInterfaceExpiryTime() * 1000 + 10000;
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Frame Payment Topup Authorization Frame
		m_oFramePaymentCardAuthorization = new FramePaymentCardAuthorization();
		m_oTemplateBuilder.buildFrame(m_oFramePaymentCardAuthorization, "fraPaymentCardAuthorization");
		m_oFramePaymentCardAuthorization.init(sTitle, sAuthType, oFuncCheck, oPosInterfaceConfig, iRequestTimeout);
		
		// Add listener;
		m_oFramePaymentCardAuthorization.addListener(this);
		this.attachChild(m_oFramePaymentCardAuthorization);
		
		return true;
	}
	
	@Override
	public void framePaymentCardAuthorizationFunction_clickCancel() {
		// Finish showing this form
		this.finishShow();
	}

	@Override
	public void framePaymentCardAuthorizationFunction_clickCardAuthorization(String sAuthorizationType, String sValue, String sTotalAmount, String sRefNo, String sAuthCode, String sParentMaskedPan, PosInterfaceConfig m_oPosInterfaceConfig, String sToken) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
		
		DateTime oCurrentTime = AppGlobal.getCurrentTime(false);
		String sTopUpTime = formatter.print(oCurrentTime);
		FuncPaymentInterface oFuncPaymentInterface = new FuncPaymentInterface(m_oPosInterfaceConfig);
		if(!oFuncPaymentInterface.cardAuthorization(m_oFuncCheck, sAuthorizationType, "", sRefNo, sValue, sTotalAmount, "", "", "", "", "", sToken)){
			showErrorDialogBox(oFuncPaymentInterface.getLastErrorMessage());
		}else{
			if(oFuncPaymentInterface.getPosPaymentGatewayTransactions() != null){
				BigDecimal dAuthAmount = new BigDecimal(sValue);
				// handle partial approval
				// return empty for no adjustment from 3rd party device
				// otherwise, trust the return amount as top-up amount
				if(!oFuncPaymentInterface.getPosPaymentGatewayTransactions().getTxnAmount().equals(dAuthAmount)){
					BigDecimal dResponseAmount = oFuncPaymentInterface.getPosPaymentGatewayTransactions().getTxnAmount();
					// response transition amount is less than input amount --> input amount = transition amount
					if(dResponseAmount.compareTo(dAuthAmount) < 0) {
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("partial_approval"));
						if (sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name()))
							oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("new_authorization_amount") + ": "+ AppGlobal.g_oFuncOutlet.get().getCurrencySign() + dResponseAmount.toPlainString());
						else if (sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.top_up_authorization.name()))
							oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("new_top_up_amount") + ": "+ AppGlobal.g_oFuncOutlet.get().getCurrencySign() + dResponseAmount.toPlainString());
						oFormDialogBox.show();
						oFormDialogBox = null;
						dAuthAmount = dResponseAmount;
					}
				}
				
				PosPaymentGatewayTransactions oTopupPosPaymentGatewayTransactions = oFuncPaymentInterface.getPosPaymentGatewayTransactions();
				if(sRefNo.isEmpty()){
					oTopupPosPaymentGatewayTransactions.setType(PosPaymentGatewayTransactions.TYPE_AUTH);
					oTopupPosPaymentGatewayTransactions.setAmount(dAuthAmount);
					
					m_oFuncCheck.addPaymentGatewayTransToList(oTopupPosPaymentGatewayTransactions, m_oPosInterfaceConfig.getInterfaceId());
					m_oFramePaymentCardAuthorization.addCardAuthorizationRecord(oTopupPosPaymentGatewayTransactions);
				}
				else{
					oTopupPosPaymentGatewayTransactions.setType(PosPaymentGatewayTransactions.TYPE_TOPUP_AUTH);
					oTopupPosPaymentGatewayTransactions.setAmount(dAuthAmount);
					oTopupPosPaymentGatewayTransactions.setParentAuthCode(sAuthCode);
					oTopupPosPaymentGatewayTransactions.setMaskedPan(sParentMaskedPan);
					
					m_oFuncCheck.addPaymentGatewayTransToList(oTopupPosPaymentGatewayTransactions, m_oPosInterfaceConfig.getInterfaceId());
					m_oFramePaymentCardAuthorization.updateAuthRecordList(0, sTopUpTime, dAuthAmount, PosPaymentGatewayTransactions.TYPE_TOPUP_AUTH);
				}
				//print slip when card authorization OR Top-up authorization
				if(m_oFuncCheck.isOldCheck()){
					if(sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name())
							|| sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.manual_authorization.name()))
						m_oFuncCheck.printCardAuthorizationSlip(sAuthorizationType, oTopupPosPaymentGatewayTransactions.constructAddSaveJSON(true), dAuthAmount.setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_UP));
				}
				
				// direct send check
				for(FormPaymentCardAuthorizationListener listener: listeners) {
					if(!listener.formPaymentCardAuthorization_PartialSendCheck()){
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_save_check"));
						oFormDialogBox.show();
						oFormDialogBox = null;
					}
				}
			}
		}
	}
	
	@Override
	public void framePaymentCardAuthorizationFunction_clickCancelAuthorization(String sAuthorizationType, String sCancelAuthType ,PosPaymentGatewayTransactions oSelectedTopupPaymentGatewayTranscation, PosInterfaceConfig m_oPosInterfaceConfig, ArrayList<PosPaymentGatewayTransactions> oPosPaymentGatewayTransactionsAuthRecordsList) {
		FuncPaymentInterface oFuncPaymentInterface = new FuncPaymentInterface(m_oPosInterfaceConfig);
		String sAmount = oSelectedTopupPaymentGatewayTranscation.getAmount().toPlainString();
		String sRefNo = oSelectedTopupPaymentGatewayTranscation.getRefNo();
		String sTraceNo = oSelectedTopupPaymentGatewayTranscation.getTraceNo();
		String sInvoieNo = oSelectedTopupPaymentGatewayTranscation.getInvoiceNo();
		String sIssuer = oSelectedTopupPaymentGatewayTranscation.getIssuer();
		String sAuthCode = oSelectedTopupPaymentGatewayTranscation.getAuthCode();
		String sToken = oSelectedTopupPaymentGatewayTranscation.getToken();
		String sMaskedPan = oSelectedTopupPaymentGatewayTranscation.getMaskedPan();
		if(sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_authorization.name()) && sCancelAuthType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name())){
			BigDecimal oTotalAuthAmt = BigDecimal.ZERO;
			for(PosPaymentGatewayTransactions oPosPaymentGatewayTransactions: oPosPaymentGatewayTransactionsAuthRecordsList){
				if(oPosPaymentGatewayTransactions.getParentAuthCode().equals(sAuthCode) 
						&& oPosPaymentGatewayTransactions.getIntfId() == m_oPosInterfaceConfig.getInterfaceId() 
						&& oPosPaymentGatewayTransactions.getStatus().isEmpty()
						&& oPosPaymentGatewayTransactions.getMaskedPan().equals(sMaskedPan)){
						oTotalAuthAmt = oTotalAuthAmt.add(oPosPaymentGatewayTransactions.getAmount());
						sRefNo = oPosPaymentGatewayTransactions.getRefNo();
						sTraceNo = oPosPaymentGatewayTransactions.getTraceNo();
						sInvoieNo = oPosPaymentGatewayTransactions.getInvoiceNo();
					}
			}
			sAmount = oSelectedTopupPaymentGatewayTranscation.getAmount().add(oTotalAuthAmt).toPlainString();
		}
		
		// Get the surchargeAmt from the selected complete auth record to do cancel complete
		String sSurchargeAmt = "";
		if(sCancelAuthType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.complete_authorization.name())){
			String sCpayId = oSelectedTopupPaymentGatewayTranscation.getCpayId();
			PosCheckPayment oCheckPayment = new PosCheckPayment();
			oCheckPayment.readById(sCpayId, 1);
			sSurchargeAmt = StringLib.BigDecimalToString(oCheckPayment.getSurcharge(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getPayDecimal());
		}
		
		if(!oFuncPaymentInterface.cardAuthorization(m_oFuncCheck, sAuthorizationType, sCancelAuthType ,sRefNo, sAmount, "", 
				sSurchargeAmt, sTraceNo, sInvoieNo, sIssuer, sAuthCode, sToken)){
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(oFuncPaymentInterface.getLastErrorMessage());
			oFormDialogBox.show();
			oFormDialogBox = null;
		}else{
			DateTime oCurrentTime = AppGlobal.getCurrentTime(false);
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
			
			//cancel card authorization
			if(sCancelAuthType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name())){
				List<PosPaymentGatewayTransactions> oPosPaymentGatewayTransactionsList = new ArrayList<PosPaymentGatewayTransactions>();
				//Retrieve all card auth and topup auth records, update their status + void information
				for(PosPaymentGatewayTransactions oPaymentGatewayTransactions :m_oFuncCheck.getPosPaymentGatewayTransactionsLists().getPosPaymentGatewayTransactionsList()){
					if(!oPaymentGatewayTransactions.getType().equals(PosPaymentGatewayTransactions.TYPE_COMPLETE_AUTH)
							&& oPaymentGatewayTransactions.getStatus().isEmpty()
							&& (oPaymentGatewayTransactions.getParentAuthCode().equals(oSelectedTopupPaymentGatewayTranscation.getAuthCode()) 
									|| oPaymentGatewayTransactions.getAuthCode().equals(oSelectedTopupPaymentGatewayTranscation.getAuthCode()))){
						oPaymentGatewayTransactions.setStatus(PosPaymentGatewayTransactions.STATUS_VOIDED);
						oPaymentGatewayTransactions.setVoidLocTime(oCurrentTime);
						oPaymentGatewayTransactions.setVoidTime(formatter.print(AppGlobal.convertTimeToUTC(oCurrentTime)));
						oPaymentGatewayTransactions.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
						
						//perform action, if the item list is empty
						if(m_oFuncCheck.isNewCheckWithNoItem()){
							oPaymentGatewayTransactions.setChksId("");
							oPosPaymentGatewayTransactionsList.add(oPaymentGatewayTransactions);
						}
					}
				}
				if(m_oFuncCheck.isNewCheckWithNoItem()){
					PosPaymentGatewayTransactions oPosPaymentGatewayTransaction = new PosPaymentGatewayTransactions();
					oPosPaymentGatewayTransaction.addUpdate(oPosPaymentGatewayTransactionsList);
				}
				
				//remove the voided card authorization record from card authorized list and authorization records list
				m_oFramePaymentCardAuthorization.removeCancelCardAuthorizationRecord();
				
			}
			//cancel top up authorization
			else if(sCancelAuthType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.top_up_authorization.name())){
				//retreive the top up record, update the status + void information
				for(PosPaymentGatewayTransactions oPaymentGatewayTransactions :m_oFuncCheck.getPosPaymentGatewayTransactionsLists().getPosPaymentGatewayTransactionsList()){
					if(oPaymentGatewayTransactions.getType().equals(PosPaymentGatewayTransactions.TYPE_TOPUP_AUTH)
							&& oPaymentGatewayTransactions.getToken().equals(oSelectedTopupPaymentGatewayTranscation.getToken())
							&& oPaymentGatewayTransactions.getTraceNo().equals(oSelectedTopupPaymentGatewayTranscation.getTraceNo())
							&& oPaymentGatewayTransactions.getInvoiceNo().equals(oSelectedTopupPaymentGatewayTranscation.getInvoiceNo())
							&& oPaymentGatewayTransactions.getRefNo().equals(oSelectedTopupPaymentGatewayTranscation.getRefNo()) 
							&& (oPaymentGatewayTransactions.getParentAuthCode().equals(oSelectedTopupPaymentGatewayTranscation.getAuthCode()) 
									|| oPaymentGatewayTransactions.getAuthCode().equals(oSelectedTopupPaymentGatewayTranscation.getAuthCode()))){
						
						oPaymentGatewayTransactions.setStatus(PosPaymentGatewayTransactions.STATUS_VOIDED);
						oPaymentGatewayTransactions.setVoidLocTime(oCurrentTime);
						oPaymentGatewayTransactions.setVoidTime(formatter.print(AppGlobal.convertTimeToUTC(oCurrentTime)));
						oPaymentGatewayTransactions.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
						
						//perform action, if the item list is empty
						if(m_oFuncCheck.isNewCheckWithNoItem()){
							oPaymentGatewayTransactions.setChksId("");
							List<PosPaymentGatewayTransactions> oPosPaymentGatewayTransactionsList = new ArrayList<PosPaymentGatewayTransactions>();
							oPosPaymentGatewayTransactionsList.add(oPaymentGatewayTransactions);
							PosPaymentGatewayTransactions oPosPaymentGatewayTransaction = new PosPaymentGatewayTransactions();
							oPosPaymentGatewayTransaction.addUpdate(oPosPaymentGatewayTransactionsList);
						}
					}
				}
				
				//remove the deleted top up record from list
				m_oFramePaymentCardAuthorization.removeCancelTopupRecord();
			}
			
			//cancel complete card authorization
			if(sCancelAuthType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.complete_authorization.name())){
				List<PosPaymentGatewayTransactions> oPosPaymentGatewayTransactionsList = new ArrayList<PosPaymentGatewayTransactions>();
				//Retrieve all card auth and topup auth records, update their status + void information
				for(PosPaymentGatewayTransactions oPaymentGatewayTransactions :m_oFuncCheck.getPosPaymentGatewayTransactionsLists().getPosPaymentGatewayTransactionsList()){
					if(oPaymentGatewayTransactions.getType().equals(PosPaymentGatewayTransactions.TYPE_COMPLETE_AUTH)
							&& oPaymentGatewayTransactions.getParentAuthCode().equals(oSelectedTopupPaymentGatewayTranscation.getParentAuthCode())){
						oPaymentGatewayTransactions.setStatus(PosPaymentGatewayTransactions.STATUS_VOIDED);
						oPaymentGatewayTransactions.setVoidLocTime(oCurrentTime);
						oPaymentGatewayTransactions.setVoidTime(formatter.print(AppGlobal.convertTimeToUTC(oCurrentTime)));
						oPaymentGatewayTransactions.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
						
						if(AppGlobal.g_oFuncStation.get().isPartialPayment()){
							// Void payment if the complete authorization's payment still active
							for(int iIndex = 0 ; iIndex < m_oFuncCheck.getCheckPaymentList().size() ; iIndex++){
								PosCheckPayment oCheckPayment = m_oFuncCheck.getCheckPaymentList().get(iIndex);
								if(oCheckPayment.getCpayId().equals(oPaymentGatewayTransactions.getCpayId()) && !oPaymentGatewayTransactions.getCpayId().isEmpty()){
									for(FormPaymentCardAuthorizationListener listener: listeners)
										listener.formPaymentCardAuthorization_voidPayment(m_oFuncCheck, iIndex);
								}
							}
						}
					}
					
					if (oPaymentGatewayTransactions.getStatus().equals(PosPaymentGatewayTransactions.STATUS_AUTHORIZE_COMPLETE)
						 && (oPaymentGatewayTransactions.getAuthCode().equals(oSelectedTopupPaymentGatewayTranscation.getParentAuthCode()) || oPaymentGatewayTransactions.getParentAuthCode().equals(oSelectedTopupPaymentGatewayTranscation.getParentAuthCode()))) {
						oPaymentGatewayTransactions.setStatus(PosPaymentGatewayTransactions.STATUS_ACTIVE);
					}
					
					//perform action, if the item list is empty
					if(m_oFuncCheck.isNewCheckWithNoItem()){
						oPaymentGatewayTransactions.setChksId("");
						oPosPaymentGatewayTransactionsList.add(oPaymentGatewayTransactions);
					}
				}
				if(m_oFuncCheck.isNewCheckWithNoItem()){
					PosPaymentGatewayTransactions oPosPaymentGatewayTransaction = new PosPaymentGatewayTransactions();
					oPosPaymentGatewayTransaction.addUpdate(oPosPaymentGatewayTransactionsList);
				}
				//remove the voided card authorization record from card authorized list and authorization records list
				m_oFramePaymentCardAuthorization.removeCancelCardAuthorizationRecord();
				
				// Print the authorization slip
				PosPaymentGatewayTransactions oReceivedPosPaymentGatewayTransactions = oFuncPaymentInterface.getPosPaymentGatewayTransactions();
				if(oReceivedPosPaymentGatewayTransactions.getType().equals(PosPaymentGatewayTransactions.TYPE_COMPLETE_AUTH))
					m_oFuncCheck.printCardAuthorizationSlip(sAuthorizationType, oReceivedPosPaymentGatewayTransactions.constructAddSaveJSON(true), oReceivedPosPaymentGatewayTransactions.getAmount().setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_UP));
				
			}
			
			//cancel authorization successfully
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cancel_successfully"));
			oFormDialogBox.show();
			oFormDialogBox = null;
			
			// direct send check
			for(FormPaymentCardAuthorizationListener listener: listeners) {
				if(!listener.formPaymentCardAuthorization_PartialSendCheck()){
					oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_save_check"));
					oFormDialogBox.show();
					oFormDialogBox = null;
				}
				// quit check if in partial payment flow
				if(AppGlobal.g_oFuncStation.get().isPartialPayment()) {
					listener.formPaymentCardAuthorization_quitCheck();
					this.finishShow();
				}
			}
		}
	}
	
	@Override
	public boolean framePaymentCardAuthorizationFunction_clickTransferAuthorization(String sAuthorizationType, PosPaymentGatewayTransactions oSelectedTopupPaymentGatewayTranscation, PosInterfaceConfig m_oPosInterfaceConfig) {
		for(FormPaymentCardAuthorizationListener listener: listeners) {
			if(!listener.formPaymentCardAuthorization_ClickTransferAuthorization(sAuthorizationType, oSelectedTopupPaymentGatewayTranscation, m_oPosInterfaceConfig))
				return false;
		}
		List<PosPaymentGatewayTransactions> oPosPaymentGatewayTransactionsList = new ArrayList<PosPaymentGatewayTransactions>();
		oPosPaymentGatewayTransactionsList.add(oSelectedTopupPaymentGatewayTranscation);
		
		for(PosPaymentGatewayTransactions oPosPaymentGatewayTransactions : m_oFuncCheck.getPosPaymentGatewayTransactionsLists().getPosPaymentGatewayTransactionsList()){
			if(oPosPaymentGatewayTransactions.getType().equals(PosPaymentGatewayTransactions.TYPE_TOPUP_AUTH) && m_oPosInterfaceConfig.getInterfaceId() == oPosPaymentGatewayTransactions.getIntfId()
				&& oPosPaymentGatewayTransactions.getParentAuthCode().equals(oSelectedTopupPaymentGatewayTranscation.getAuthCode())){
				oPosPaymentGatewayTransactionsList.add(oPosPaymentGatewayTransactions);
			}
		}
		
		PosPaymentGatewayTransactions oPosPaymentGatewayTransaction = new PosPaymentGatewayTransactions();
		oPosPaymentGatewayTransaction.addUpdate(oPosPaymentGatewayTransactionsList);
		return true;
	}
	
	@Override
	public void framePaymentCardAuthorizationFunction_clickReprintAuthSlip(String sAuthorizationType,
			JSONObject oCardAuthJSON, BigDecimal oAuthAmount) {
		//Ask confirmation for action
		FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("confirm"), AppGlobal.g_oLang.get()._("cancel"), this);
		oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
		oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("confirm_to_reprint")+"?");
		oFormConfirmBox.show();
		if(oFormConfirmBox.isOKClicked() == false)
			return;
		
		m_oFuncCheck.printCardAuthorizationSlip(sAuthorizationType, oCardAuthJSON, oAuthAmount);
	}
	
	@Override
	public void framePaymentCardAuthorizationFunction_clickCancelToExitCashier() {
		FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
		oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
		oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("no_cancellation_for_unattached_authorization")
				+ System.lineSeparator() + AppGlobal.g_oLang.get()._("abort_to_continue_payment"));
		oFormDialogBox.show();
		this.setIsSuccessToCancelUnattachAuthPayment(false);
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public void framePaymentCardAuthorizationFunction_clickCancelAll() {
		FuncPaymentInterface oFuncPaymentInterface = new FuncPaymentInterface(m_oPosInterfaceConfig);
		boolean bFail = false;
		
		// post cancel auth for each un-attached authorization in the cashier
		for (PosPaymentGatewayTransactions oCancelTrans : m_oFuncCheck.getUnattachPaymentPosPaymentGatewayTransactionsList().getPosPaymentGatewayTransactionsList()) {
			String sAuthorizationType = FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_authorization.name();
			String sCancelAuthType = FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name();
			if(!oFuncPaymentInterface.cardAuthorization(m_oFuncCheck, sAuthorizationType, sCancelAuthType ,oCancelTrans.getRefNo(), oCancelTrans.getAmount().toString(), "",
					"", oCancelTrans.getTraceNo(), oCancelTrans.getInvoiceNo(), "", "", oCancelTrans.getToken())){
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(oFuncPaymentInterface.getLastErrorMessage());
				oFormDialogBox.show();
				oFormDialogBox = null;
				
				bFail = true;
				break;
			}
			else {
				DateTime oCurrentTime = AppGlobal.getCurrentTime(false);
				DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
				
				//cancel card authorization
				for(PosPaymentGatewayTransactions oPaymentGatewayTransactions :m_oFuncCheck.getPosPaymentGatewayTransactionsLists().getPosPaymentGatewayTransactionsList()){
					if(!oPaymentGatewayTransactions.getType().equals(PosPaymentGatewayTransactions.TYPE_COMPLETE_AUTH)
							&& (oPaymentGatewayTransactions.getParentAuthCode().isEmpty() && oPaymentGatewayTransactions.getAuthCode().equals(oCancelTrans.getParentAuthCode())) 
							|| (!oPaymentGatewayTransactions.getParentAuthCode().isEmpty() && oPaymentGatewayTransactions.getParentAuthCode().equals(oCancelTrans.getParentAuthCode()))){
							oPaymentGatewayTransactions.setStatus(PosPaymentGatewayTransactions.STATUS_VOIDED);
							oPaymentGatewayTransactions.setVoidLocTime(oCurrentTime);
							oPaymentGatewayTransactions.setVoidTime(formatter.print(AppGlobal.convertTimeToUTC(oCurrentTime)));
							oPaymentGatewayTransactions.setVoidUserId(AppGlobal.g_oFuncUser.get().getUserId());
						}
				}
			}
		}
		
		if (bFail) {
			framePaymentCardAuthorizationFunction_clickCancelToExitCashier();
			this.setIsSuccessToCancelUnattachAuthPayment(false);
		}
		else {
			this.finishShow();
			this.setIsSuccessToCancelUnattachAuthPayment(true);
		}
	}
	
	public void setIsSuccessToCancelUnattachAuthPayment(boolean bIsSuccessToCancelUnattachAuthPayment){
		this.m_bIsSuccessToCancelUnattachAuthPayment = bIsSuccessToCancelUnattachAuthPayment;
	}
	
	public boolean getIsSuccessToCancelUnattachAuthPayment() {
		return this.m_bIsSuccessToCancelUnattachAuthPayment;
	}
	
	private void showDialogBox(String sTitle, String sMessage) {
		if (sMessage.isEmpty())
			return;
		
		FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
		oFormDialogBox.setTitle(sTitle);
		oFormDialogBox.setMessage(sMessage);
		oFormDialogBox.show();
	}
	
	private void showErrorDialogBox(String sErrMsg) {
		showDialogBox(AppGlobal.g_oLang.get()._("error"), sErrMsg);
	}
	
	private void showWarningDialogBox(String sWarningMsg) {
		showDialogBox(AppGlobal.g_oLang.get()._("warning"), sWarningMsg);
	}
	
	private void showAttentionDialogBox(String sAttentionMsg) {
		showDialogBox(AppGlobal.g_oLang.get()._("attention"), sAttentionMsg);
	}
	
}
