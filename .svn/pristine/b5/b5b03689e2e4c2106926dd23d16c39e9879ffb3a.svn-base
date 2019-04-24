package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import commonui.FormConfirmBox;
import om.PosInterfaceConfig;
import om.PosPaymentGatewayTransactions;
import om.PosPaymentGatewayTransactionsList;
import commonui.FormDialogBox;
import commonui.FrameNumberPad;
import commonui.FormListMessageBox;
import commonui.FrameNumberPadListener;
import commonui.FrameProcessBox;
import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.*;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;

/** interface for the listeners/observers callback method */
interface FramePaymentCardAuthorizationListener {
	void framePaymentCardAuthorizationFunction_clickCancel();
	void framePaymentCardAuthorizationFunction_clickCardAuthorization(String sAuthorizationType, String sValue, String sTotalAmount, String sRefNo, String sAuthCode, String sParentMaskedPan,PosInterfaceConfig m_oPosInterfaceConfig, String sToken);
	boolean framePaymentCardAuthorizationFunction_clickTransferAuthorization(String sAuthorizationType, PosPaymentGatewayTransactions oSelectedTopupPaymentGatewayTranscation, PosInterfaceConfig m_oPosInterfaceConfig);
	void framePaymentCardAuthorizationFunction_clickReprintAuthSlip(String sAuthorizationType, JSONObject oCardAuthJSON, BigDecimal oAuthAmount);
	void framePaymentCardAuthorizationFunction_clickCancelAuthorization(String sAuthorizationType, String sCancelType, PosPaymentGatewayTransactions oSelectedTopupPaymentGatewayTranscation, PosInterfaceConfig m_oPosInterfaceConfig, ArrayList<PosPaymentGatewayTransactions> oPosPaymentGatewayTransactionsAuthRecordsList);
	void framePaymentCardAuthorizationFunction_clickCancelToExitCashier();
	void framePaymentCardAuthorizationFunction_clickCancelAll();
}

public class FramePaymentCardAuthorization extends VirtualUIFrame implements FrameNumberPadListener, FrameCommonBasketListener, FrameTitleHeaderListener{
	private TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUIFrame m_oFrameLeftUpperContent;
	private VirtualUIFrame m_oFrameLeftLowerContent;
	private VirtualUIFrame m_oFrameRightContent;
	
	private VirtualUILabel m_oLabelSearchHeader;
	private FrameNumberPad m_oFrameNumberPad;
	FrameHorizontalTabList m_oFrameHorizontalTabList;
	private FrameCommonBasket m_oCardAuthorizationtListCommonBasket;
	private FrameCommonBasket m_oAuthRecordListCommonBasket;
	private VirtualUITextbox m_oTxtboxValue;
	
	private VirtualUILabel m_oLabelCardAuthorization;
	private VirtualUILabel m_oLabelAuthRecords;
	private VirtualUIFrame m_oFrameAuthRecordsUnderLineBottom;
	private VirtualUIButton m_oButtonCardAuthorization;
	private VirtualUIButton m_oButtonTopUpAuthorization;
	private VirtualUIButton m_oButtonReprintAuthorization;
	private VirtualUIButton m_oButtonManualAuthorization;
	private VirtualUIButton m_oButtonCancelAllCardAuthorization;
	private int m_iSelectedCardAuthItemIndex;
	private int m_iSelectedAuthRecordsItemIndex;
	private FrameTitleHeader m_oTitleHeader;
	private int m_iRequestTimeout;
	
	// Refresh screen
	private FrameProcessBox m_oFrameRefreshPanel;
	
	public PosPaymentGatewayTransactionsList m_oPosPaymentGatewayTransactionsCardAuthListByCheck;
	
	private PosInterfaceConfig m_oPosInterfaceConfig;
	private FuncCheck m_oFuncCheck;
	private ArrayList<PosPaymentGatewayTransactions> m_oPosPaymentGatewayTransactionsCardAuthList;
	private ArrayList<PosPaymentGatewayTransactions> m_oPosPaymentGatewayTransactionsAuthRecordsList;
	private PosPaymentGatewayTransactions m_oSelectedPosPaymentGatewayTransactions;
	
	private ArrayList<BigDecimal> m_oTotalAuthorizationAmt;
	
	private String m_sAuthorizationType;
	public static enum AUTHORIZATION_TYPE{card_authorization, top_up_authorization, manual_authorization, cancel_authorization, complete_authorization, cancel_complete_authorization, transfer_authorization, cancel_unattached_payment_auth, sale, void_sale, adjust_tip};
	public static enum AUTHORIZATION_PAYMENT_TYPE{standard, direct_sale};
	public static enum PAYMENT_MAPPING_TYPE{payment_mapping, payment_mapping_for_direct_sale};
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FramePaymentCardAuthorizationListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FramePaymentCardAuthorizationListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FramePaymentCardAuthorizationListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	//Constructor
	public FramePaymentCardAuthorization() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FramePaymentCardAuthorizationListener>();
		m_oPosPaymentGatewayTransactionsCardAuthList = new ArrayList<PosPaymentGatewayTransactions>();
		m_oPosPaymentGatewayTransactionsAuthRecordsList = new ArrayList<PosPaymentGatewayTransactions>();
		
		m_oTotalAuthorizationAmt =  new ArrayList<BigDecimal>();
		m_sAuthorizationType = "";
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraPaymentCardAuthorization.xml");
	}
	
	public void init(String sTitle, String sAuthType, FuncCheck oFuncCheck, PosInterfaceConfig oPosInterfaceConfig, int iRequestTimeout) {
		m_sAuthorizationType = sAuthType;
		m_oPosInterfaceConfig = oPosInterfaceConfig;
		m_oFuncCheck = oFuncCheck;
		m_iRequestTimeout = iRequestTimeout;
		
		// init the local value
		m_iSelectedCardAuthItemIndex = -1;
		m_iSelectedAuthRecordsItemIndex = -1;

		if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_unattached_payment_auth.name()))
			m_oPosPaymentGatewayTransactionsCardAuthListByCheck = m_oFuncCheck.getUnattachPaymentPosPaymentGatewayTransactionsList();
		else
			m_oPosPaymentGatewayTransactionsCardAuthListByCheck = m_oFuncCheck.getPosPaymentGatewayTransactionsLists();
		
		// Load child elements from template
		/////////////////////////////////////////////////////////////////
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.init(true);
		m_oTitleHeader.setTitle(sTitle);
		m_oTitleHeader.addListener(this);
		this.attachChild(m_oTitleHeader);
		
		//Create Left Upper Content Frame
		m_oFrameLeftUpperContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftUpperContent, "fraLeftUpperContent");
		this.attachChild(m_oFrameLeftUpperContent);
		
		//Create Left Lower Content Frame
		m_oFrameLeftLowerContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftLowerContent, "fraLeftLowerContent");
		this.attachChild(m_oFrameLeftLowerContent);
		
		//Card Authorization Label
		m_oLabelCardAuthorization = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCardAuthorization, "lblCardAuthorization");
		m_oLabelCardAuthorization.setValue(AppGlobal.g_oLang.get()._("card_authorized"));
		if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_complete_authorization.name()))
			m_oLabelCardAuthorization.setVisible(false);
		this.attachChild(m_oLabelCardAuthorization);
		
		m_oCardAuthorizationtListCommonBasket = new FrameCommonBasket();
		
		if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_authorization.name()) ||
			m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.transfer_authorization.name())){
			m_oTemplateBuilder.buildFrame(m_oCardAuthorizationtListCommonBasket, "fraCardAuthorizationListForCancelAuth");
			m_oFrameLeftUpperContent.setWidth(m_oCardAuthorizationtListCommonBasket.getWidth());
			m_oFrameLeftUpperContent.setWidth(960);
			m_oFrameLeftUpperContent.setHeight(190);
			m_oFrameLeftLowerContent.setTop(392);
			m_oFrameLeftLowerContent.setWidth(960);
			m_oFrameLeftLowerContent.setHeight(219);
		}
		else if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_complete_authorization.name())){
			m_oFrameLeftUpperContent.setTop(80);
			m_oFrameLeftUpperContent.setHeight(530);
			m_oFrameLeftUpperContent.setWidth(960);
			m_oTemplateBuilder.buildFrame(m_oCardAuthorizationtListCommonBasket, "fraCardAuthorizationListForCancelCompleteAuth");
		}else if (m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_unattached_payment_auth.name())) {
			m_oFrameLeftUpperContent.setHeight(m_oFrameLeftUpperContent.getHeight() + m_oFrameLeftUpperContent.getHeight());
			m_oTemplateBuilder.buildFrame(m_oCardAuthorizationtListCommonBasket, "fraCardAuthorizationListForCancelCompleteAuth");
		}else
			m_oTemplateBuilder.buildFrame(m_oCardAuthorizationtListCommonBasket, "fraCardAuthorizationList");
		
		m_oCardAuthorizationtListCommonBasket.init();
		
		if (!m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_unattached_payment_auth.name()))
			m_oCardAuthorizationtListCommonBasket.addListener(this);
		m_oFrameLeftUpperContent.attachChild(m_oCardAuthorizationtListCommonBasket);
		
		if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.transfer_authorization.name())){
			m_oFrameLeftUpperContent.setWidth(960);
			m_oFrameLeftUpperContent.setHeight(150);
			m_oFrameLeftLowerContent.setTop(342);
			m_oFrameLeftLowerContent.setWidth(960);
			m_oFrameLeftLowerContent.setHeight(206);
		}
		
		//TopUp Authorization Label
		m_oLabelAuthRecords = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAuthRecords, "lblAuthRecords");
		m_oLabelAuthRecords.setValue(AppGlobal.g_oLang.get()._("authorization_records"));
		this.attachChild(m_oLabelAuthRecords);
		if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_complete_authorization.name()))
			m_oLabelAuthRecords.setVisible(false);
		
		m_oAuthRecordListCommonBasket = new FrameCommonBasket();
		if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_authorization.name()) ||
				m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.transfer_authorization.name()) ||
			m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_unattached_payment_auth.name()))
			m_oTemplateBuilder.buildFrame(m_oAuthRecordListCommonBasket, "fraAuthRecordListForCancelAuth");
		else
			m_oTemplateBuilder.buildFrame(m_oAuthRecordListCommonBasket, "fraAuthRecordList");
		
		m_oAuthRecordListCommonBasket.init();
		m_oAuthRecordListCommonBasket.addListener(this);
		
		m_oFrameLeftLowerContent.attachChild(m_oAuthRecordListCommonBasket);
		if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_complete_authorization.name()) || 
			m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_unattached_payment_auth.name()))
			m_oFrameLeftLowerContent.setVisible(false);
		
		if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name()) 
				|| m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.top_up_authorization.name())){
			//Create Right Content Frame
			m_oFrameRightContent = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(m_oFrameRightContent, "fraRightContent");
			this.attachChild(m_oFrameRightContent);
			
			//Number Pad
			m_oFrameNumberPad = new FrameNumberPad();
			m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
			m_oFrameNumberPad.init();
			m_oFrameNumberPad.setEnterBlockUI(true);
			m_oFrameNumberPad.clearEnterSubmitId();
			m_oFrameNumberPad.addListener(this);
			m_oFrameRightContent.attachChild(m_oFrameNumberPad);
			
			//Search Header
			m_oLabelSearchHeader = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelSearchHeader, "lblSearchHeader");
			m_oLabelSearchHeader.setValue(AppGlobal.g_oLang.get()._("amount")+":");
			m_oFrameRightContent.attachChild(m_oLabelSearchHeader);
			
			BigDecimal dDefaultInput = oFuncCheck.getCheckTotal().subtract(oFuncCheck.getPaymentRecordPayTotal());
			for(PosPaymentGatewayTransactions oPosPaymentGatewayTransaction : oFuncCheck.getPosPaymentGatewayTransactionsLists().getPosPaymentGatewayTransactionsList()){
				if(oPosPaymentGatewayTransaction.getStatus().equals(PosPaymentGatewayTransactions.STATUS_ACTIVE) &&
						(oPosPaymentGatewayTransaction.getType().equals(PosPaymentGatewayTransactions.TYPE_AUTH) ||
						oPosPaymentGatewayTransaction.getType().equals(PosPaymentGatewayTransactions.TYPE_TOPUP_AUTH)))
					dDefaultInput = dDefaultInput.subtract(oPosPaymentGatewayTransaction.getAmount());
			}
			
			//Searching field : Card Auth / Top Up Value
			m_oTxtboxValue = new VirtualUITextbox();
			m_oTemplateBuilder.buildTextbox(m_oTxtboxValue, "txtValue");
			if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name())){
				if(dDefaultInput.compareTo(BigDecimal.ZERO) > 0)
					m_oTxtboxValue.setValue(StringLib.BigDecimalToString(dDefaultInput, AppGlobal.g_oFuncOutlet.get().getBusinessDay().getCheckDecimal()));
				else
					m_oTxtboxValue.setValue("");
			}
			
			m_oTxtboxValue.setFocusWhenShow(true);
			m_oFrameRightContent.attachChild(m_oTxtboxValue);
			m_oFrameNumberPad.setEnterSubmitId(m_oTxtboxValue);
			
			//Search Button: Card Authorization
			m_oButtonCardAuthorization = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonCardAuthorization, "butCardAuthorization");
			m_oButtonCardAuthorization.addClickServerRequestSubmitElement(m_oButtonCardAuthorization);
			m_oButtonCardAuthorization.setValue(AppGlobal.g_oLang.get()._("card_authorization"));
			m_oButtonCardAuthorization.addClickServerRequestSubmitElement(m_oTxtboxValue);
			m_oFrameRightContent.attachChild(m_oButtonCardAuthorization);
			
			//Search Button: Top Up Authorization
			m_oButtonTopUpAuthorization = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonTopUpAuthorization, "butTopUpAuthorization");
			m_oButtonTopUpAuthorization.addClickServerRequestSubmitElement(m_oButtonTopUpAuthorization);
			m_oButtonTopUpAuthorization.setValue(AppGlobal.g_oLang.get()._("top_up_authorization"));
			m_oButtonTopUpAuthorization.addClickServerRequestSubmitElement(m_oTxtboxValue);
			m_oFrameRightContent.attachChild(m_oButtonTopUpAuthorization);
			
			m_oButtonReprintAuthorization = new VirtualUIButton();
			if(oFuncCheck.isOldCheck()){
				//Button: Reprint Authorization
				m_oTemplateBuilder.buildButton(m_oButtonReprintAuthorization, "butReprintAuthorization");
				m_oButtonReprintAuthorization.addClickServerRequestSubmitElement(m_oButtonReprintAuthorization);
				m_oButtonReprintAuthorization.setValue(AppGlobal.g_oLang.get()._("reprint_authorization"));
				m_oFrameRightContent.attachChild(m_oButtonReprintAuthorization);
			}
			
			//Manual Authorization
			m_oButtonManualAuthorization = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonManualAuthorization, "butManualAuthorization");
			m_oButtonManualAuthorization.addClickServerRequestSubmitElement(m_oButtonManualAuthorization);
			m_oButtonManualAuthorization.setValue(AppGlobal.g_oLang.get()._("manual_authorization"));
			
			m_oButtonManualAuthorization.addClickServerRequestSubmitElement(m_oTxtboxValue);
			m_oFrameRightContent.attachChild(m_oButtonManualAuthorization);
			
			this.updateButtonColor();
			m_oFrameRightContent.setVisible(true);
		}
		//reset the width for cancel authorization
		else if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_authorization.name()) ||
				m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.transfer_authorization.name()) ||
				m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_unattached_payment_auth.name())){
			
			m_oLabelAuthRecords.setWidth(357);
			if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.transfer_authorization.name()))
				m_oLabelAuthRecords.setTop(307);
			
			// cancel all card authorization
			if (m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_unattached_payment_auth.name())) {
				m_oButtonCancelAllCardAuthorization = new VirtualUIButton();
				m_oTemplateBuilder.buildButton(m_oButtonCancelAllCardAuthorization, "butCancelAllCardAuthorization");
				m_oButtonCancelAllCardAuthorization.addClickServerRequestSubmitElement(m_oButtonCancelAllCardAuthorization);
				m_oButtonCancelAllCardAuthorization.setValue(AppGlobal.g_oLang.get()._("cancel_all"));
				this.attachChild(m_oButtonCancelAllCardAuthorization);
			}
			
			// transfer card authorization
			if (m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.transfer_authorization.name())) {
				m_oButtonCancelAllCardAuthorization = new VirtualUIButton();
				m_oTemplateBuilder.buildButton(m_oButtonCancelAllCardAuthorization, "butCancelAllCardAuthorization");
				m_oButtonCancelAllCardAuthorization.addClickServerRequestSubmitElement(m_oButtonCancelAllCardAuthorization);
				m_oButtonCancelAllCardAuthorization.setValue(AppGlobal.g_oLang.get()._("transfer_authorization"));
				this.attachChild(m_oButtonCancelAllCardAuthorization);
			}
		}
		m_oAuthRecordListCommonBasket.setVisible(false);
		addCardAuthorizationList(m_oPosPaymentGatewayTransactionsCardAuthListByCheck);
		
		if (!m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_unattached_payment_auth.name())) {
			//highlight the first record
			if(m_oCardAuthorizationtListCommonBasket.getItemCount(0) > 0){
				m_iSelectedCardAuthItemIndex = 0;
				
				for(int i = 0; i<2; i++)
					m_oCardAuthorizationtListCommonBasket.setFieldBackgroundColor(0, 0, i, "#FFFFFF");
				//auto add the topup auth records
				m_oSelectedPosPaymentGatewayTransactions = m_oPosPaymentGatewayTransactionsCardAuthList.get(m_iSelectedCardAuthItemIndex);
				m_oPosPaymentGatewayTransactionsAuthRecordsList.clear();
				m_oPosPaymentGatewayTransactionsAuthRecordsList.add(m_oSelectedPosPaymentGatewayTransactions);
				
				updateAuthRecordListForSelectedCard();
			}
		}
		
		// Add refresh screen during click close button for emergency process
		m_oFrameRefreshPanel = new FrameProcessBox();
		m_oTemplateBuilder.buildFrame(m_oFrameRefreshPanel, "fraRefresh");
		m_oFrameRefreshPanel.init(false);
		m_oFrameRefreshPanel.setTitle(AppGlobal.g_oLang.get()._("attention"));
		m_oFrameRefreshPanel.setMessage(AppGlobal.g_oLang.get()._("please_click_several_times_to_refresh_screen"));
		m_oFrameRefreshPanel.addRefreshButton(AppGlobal.g_oLang.get()._("refresh"));
		m_oFrameRefreshPanel.setVisible(false);
		this.attachChild(m_oFrameRefreshPanel);
	}
	
	public ArrayList<BigDecimal> getTotalAuthAmount(){
		return m_oTotalAuthorizationAmt;
	}
	
	public void addCardAuthorizationList(PosPaymentGatewayTransactionsList oPosPaymentGatewayTransactionList){
		int iSectionId = 0;
		int iWdith = m_oCardAuthorizationtListCommonBasket.getWidth()/7;
		m_oCardAuthorizationtListCommonBasket.clearAllSections();
		
		//Add Card Authorization Records Title
		ArrayList<Integer> iFieldWidth = new ArrayList<Integer>();
		ArrayList<String> sFieldValue = new ArrayList<String>();
		
		iFieldWidth.add(iWdith * 3);
		sFieldValue.add(AppGlobal.g_oLang.get()._("card_number"));
		iFieldWidth.add(iWdith * 4);
		sFieldValue.add(AppGlobal.g_oLang.get()._("total_authorization_amount"));
		
		m_oCardAuthorizationtListCommonBasket.addHeader(iFieldWidth, sFieldValue);
		m_oCardAuthorizationtListCommonBasket.setHeaderTextAlign(1, HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
		m_oCardAuthorizationtListCommonBasket.setHeaderPadding(1, "15,0,0,0");
		m_oCardAuthorizationtListCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		int iRowHeight = 0;
		int iItemIndex = 0;
		for(PosPaymentGatewayTransactions oPaymentGatewayTransactions :oPosPaymentGatewayTransactionList.getPosPaymentGatewayTransactionsList()){
			BigDecimal dTotalAuthorization = BigDecimal.ZERO;
			// Show the relative authorization record.
			if((oPaymentGatewayTransactions.getType().equals(PosPaymentGatewayTransactions.TYPE_AUTH) && 
				(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name()) || m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_authorization.name()) || m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.transfer_authorization.name()) || m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_unattached_payment_auth.name()))) ||
				(oPaymentGatewayTransactions.getType().equals(PosPaymentGatewayTransactions.TYPE_COMPLETE_AUTH) && 
				m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_complete_authorization.name()))){
				if(oPaymentGatewayTransactions.getStatus().isEmpty()
						&& m_oPosInterfaceConfig.getInterfaceId() == oPaymentGatewayTransactions.getIntfId()
						&& oPaymentGatewayTransactions.getChksId().equals(m_oFuncCheck.getCheckId())){
					m_oPosPaymentGatewayTransactionsCardAuthList.add(oPaymentGatewayTransactions);
					dTotalAuthorization = dTotalAuthorization.add(oPaymentGatewayTransactions.getAmount());
					
					//Add Card Authorization Records Details
					ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
					ArrayList<String> sFieldValues = new ArrayList<String>();
					ArrayList<String> sFieldAligns = new ArrayList<String>();
					ArrayList<String> sFieldTypes = new ArrayList<String>();
					
					// Pan
					iFieldWidths.add(iWdith * 3);
					sFieldValues.add(oPaymentGatewayTransactions.getMaskedPan());
					sFieldAligns.add("");
					sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
					
					//Total Authorization Amount
					for(PosPaymentGatewayTransactions oTmpPaymentGatewayTransactions : oPosPaymentGatewayTransactionList.getPosPaymentGatewayTransactionsList()) {
						if(oTmpPaymentGatewayTransactions.getType().equals(PosPaymentGatewayTransactions.TYPE_TOPUP_AUTH)
								&& m_oPosInterfaceConfig.getInterfaceId() == oTmpPaymentGatewayTransactions.getIntfId()
								&& oTmpPaymentGatewayTransactions.getMaskedPan().equals(oPaymentGatewayTransactions.getMaskedPan())
								&& oTmpPaymentGatewayTransactions.getParentAuthCode().equals(oPaymentGatewayTransactions.getAuthCode())
								&& oTmpPaymentGatewayTransactions.getStatus().isEmpty()) {
							dTotalAuthorization = dTotalAuthorization.add(oTmpPaymentGatewayTransactions.getAmount());
						}
					}
					iFieldWidths.add(iWdith * 4);
					sFieldValues.add(AppGlobal.g_oFuncOutlet.get().getCurrencySign() + dTotalAuthorization.setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_UP).toString());
					sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
					sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
					
					//Total Authorization List
					m_oTotalAuthorizationAmt.add(dTotalAuthorization);
					iRowHeight = 44;
					
					m_oCardAuthorizationtListCommonBasket.addItem(iSectionId, iItemIndex, iRowHeight, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
					
					for(int i = 0; i < 2; i++) {
						m_oCardAuthorizationtListCommonBasket.setFieldTextSize(iSectionId, iItemIndex, i, 22);
						m_oCardAuthorizationtListCommonBasket.setFieldPadding(iSectionId, iItemIndex, 1, "15,0,0,0");
					}
					iItemIndex++;
				}
			}
			
		}
	}
	
	public void addCardAuthorizationRecord(PosPaymentGatewayTransactions oPosPaymentGatewayTransaction){
		int iSectionId = 0;
		m_oTotalAuthorizationAmt.clear();
		
		int iWdith = m_oCardAuthorizationtListCommonBasket.getWidth() / 7;
		
		//remove all sections
		m_oCardAuthorizationtListCommonBasket.clearAllSections();
		if(m_oCardAuthorizationtListCommonBasket.getChildCount() == 0){
			//Add Card Authorization Records Title
			ArrayList<Integer> iFieldWidth = new ArrayList<Integer>();
			ArrayList<String> sFieldValue = new ArrayList<String>();
			iFieldWidth.add(iWdith*3);
			sFieldValue.add(AppGlobal.g_oLang.get()._("pan"));
			iFieldWidth.add(iWdith*4);
			sFieldValue.add(AppGlobal.g_oLang.get()._("total_authorization_amount"));
			m_oCardAuthorizationtListCommonBasket.addHeader(iFieldWidth, sFieldValue);
			m_oCardAuthorizationtListCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		}
		int iRowHeight = 0;
		int iItemIndex = 0;
		m_oPosPaymentGatewayTransactionsCardAuthList.add(oPosPaymentGatewayTransaction);

		for(PosPaymentGatewayTransactions oPaymentGatewayTransactions :m_oFuncCheck.getPosPaymentGatewayTransactionsLists().getPosPaymentGatewayTransactionsList()){
			BigDecimal dTotalAuthorization = BigDecimal.ZERO;
			if(oPaymentGatewayTransactions.getType().equals(PosPaymentGatewayTransactions.TYPE_AUTH)
					&& m_oPosInterfaceConfig.getInterfaceId() == oPaymentGatewayTransactions.getIntfId()
					&& oPaymentGatewayTransactions.getStatus().equals(PosPaymentGatewayTransactions.STATUS_ACTIVE)){
				dTotalAuthorization = dTotalAuthorization.add(oPaymentGatewayTransactions.getAmount());
				//Add Card Authorization Records Details
				ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
				ArrayList<String> sFieldValues = new ArrayList<String>();
				ArrayList<String> sFieldAligns = new ArrayList<String>();
				ArrayList<String> sFieldTypes = new ArrayList<String>();
				// Pan
				iFieldWidths.add(iWdith*3);
				sFieldValues.add(oPaymentGatewayTransactions.getMaskedPan());
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				//Total Authorization Amount
				for(PosPaymentGatewayTransactions oTmpPaymentGatewayTransactions :m_oFuncCheck.getPosPaymentGatewayTransactionsLists().getPosPaymentGatewayTransactionsList()){
					if(oTmpPaymentGatewayTransactions.getType().equals(PosPaymentGatewayTransactions.TYPE_TOPUP_AUTH)
							&& m_oPosInterfaceConfig.getInterfaceId() == oTmpPaymentGatewayTransactions.getIntfId()
							&& oTmpPaymentGatewayTransactions.getStatus().equals(PosPaymentGatewayTransactions.STATUS_ACTIVE)
							&& oTmpPaymentGatewayTransactions.getParentAuthCode().equals(oPaymentGatewayTransactions.getAuthCode())){
						dTotalAuthorization = dTotalAuthorization.add(oTmpPaymentGatewayTransactions.getAmount());
					}
				}
				iFieldWidths.add(iWdith * 4);
				sFieldValues.add(AppGlobal.g_oFuncOutlet.get().getCurrencySign()+dTotalAuthorization.setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_UP).toString());
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				
				//Total Authorization List
				m_oTotalAuthorizationAmt.add(dTotalAuthorization);
				
				iRowHeight = 50;
				m_oCardAuthorizationtListCommonBasket.addItem(iSectionId, iItemIndex, iRowHeight, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
				for(int i = 0; i < 2; i++){
					m_oCardAuthorizationtListCommonBasket.setFieldPadding(iSectionId, iItemIndex, i+1, "15,0,0,0");
					m_oCardAuthorizationtListCommonBasket.setFieldTextSize(iSectionId, iItemIndex, i, 22);
				}
				
				iItemIndex++;
			}
		}
		m_oCardAuthorizationtListCommonBasket.moveScrollToItem(0, m_oCardAuthorizationtListCommonBasket.getItemCellCount(0));
	}
	
	public void addAuthRecordList(int iSectionId, ArrayList<PosPaymentGatewayTransactions> oPosPaymentGatewayTransactions){
		//remove all sections
		m_oAuthRecordListCommonBasket.clearAllSections();
		
		//Add Top Up Records Title
		ArrayList<Integer> iFieldWidth = new ArrayList<Integer>();
		ArrayList<String[]> sFieldValue = new ArrayList<String[]>();
		
		int iWidth = m_oCardAuthorizationtListCommonBasket.getWidth() / 7;
		
		if(m_oAuthRecordListCommonBasket.getItemCount(0) == 0) {
			iFieldWidth.add(iWidth * 2);
			sFieldValue.add(AppGlobal.g_oLang.get()._("action_time", ""));
			iFieldWidth.add(iWidth * 2);
			sFieldValue.add(AppGlobal.g_oLang.get()._("amount", ""));
			iFieldWidth.add(iWidth * 3);
			sFieldValue.add(AppGlobal.g_oLang.get()._("type", ""));
			
			m_oAuthRecordListCommonBasket.addHeader(iFieldWidth, sFieldValue);
			m_oAuthRecordListCommonBasket.setHeaderTextAlign(1, HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
			m_oAuthRecordListCommonBasket.setHeaderTextAlign(2, HeroActionProtocol.View.Attribute.TextAlign.CENTER);
			
			m_oAuthRecordListCommonBasket.setHeaderPadding(1, "15,0,0,0");

		}
		m_oAuthRecordListCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		int iRowHeight = 0;
		for(int iItemIndex=0; iItemIndex<oPosPaymentGatewayTransactions.size(); iItemIndex++){
			DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
			
			DateTime dt = oPosPaymentGatewayTransactions.get(iItemIndex).getActionLocTime();
			String sTime = formatter.print(dt);
			
			BigDecimal dValue = oPosPaymentGatewayTransactions.get(iItemIndex).getAmount().setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_UP);
			
			//Add Auth Records Details
			ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
			ArrayList<String> sFieldValues = new ArrayList<String>();
			ArrayList<String> sFieldAligns = new ArrayList<String>();
			
			// Auth Time
			iFieldWidths.add(iWidth * 2);
			sFieldValues.add(sTime);
			sFieldAligns.add("");
			
			// Auth Value
			iFieldWidths.add(iWidth * 2);
			sFieldValues.add(AppGlobal.g_oFuncOutlet.get().getCurrencySign()+dValue.toString());
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
			
			// Action Type
			iFieldWidths.add(iWidth * 3);
			if(oPosPaymentGatewayTransactions.get(iItemIndex).getType().equals(PosPaymentGatewayTransactions.TYPE_AUTH))
				sFieldValues.add(AppGlobal.g_oLang.get()._("authorization"));
			else if(oPosPaymentGatewayTransactions.get(iItemIndex).getType().equals(PosPaymentGatewayTransactions.TYPE_TOPUP_AUTH))
				sFieldValues.add(AppGlobal.g_oLang.get()._("top_up"));
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
			
			m_oAuthRecordListCommonBasket.addItem(iSectionId, iItemIndex, iRowHeight, iFieldWidths, sFieldValues, sFieldAligns, null, null);
			
			for(int i = 0; i < 3; i++) 
				m_oAuthRecordListCommonBasket.setFieldTextSize(iSectionId, iItemIndex, i, 22);
			m_oAuthRecordListCommonBasket.setFieldPadding(iSectionId,iItemIndex, 1, "15,0,0,0");
		}
		
		if(m_oAuthRecordListCommonBasket.getChildCount() > 1){
			m_oAuthRecordListCommonBasket.setVisible(true);
		}
	}
	
	public void updateAuthRecordList(int iSectionId, String sTopupTime, BigDecimal dTopupValue, String sAuthType){
		
		//Add Top Up Records Details
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		int iRowHeight = 0;
		int iWdith = m_oCardAuthorizationtListCommonBasket.getWidth() / 7;
		
		iFieldWidths.add(iWdith*2);
		sFieldValues.add(sTopupTime);
		sFieldAligns.add("");
		
		// Top Up Value
		iFieldWidths.add(iWdith * 2);
		sFieldValues.add(AppGlobal.g_oFuncOutlet.get().getCurrencySign()+dTopupValue.setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_UP).toString());
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
		
		// Action Type
		iFieldWidths.add(iWdith * 3);
		if(sAuthType.equals(PosPaymentGatewayTransactions.TYPE_AUTH))
			sFieldValues.add(AppGlobal.g_oLang.get()._("authorization"));
		else if(sAuthType.equals(PosPaymentGatewayTransactions.TYPE_TOPUP_AUTH))
			sFieldValues.add(AppGlobal.g_oLang.get()._("top_up"));
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		
		m_oAuthRecordListCommonBasket.addItem(iSectionId, m_oAuthRecordListCommonBasket.getItemCellCount(0), iRowHeight, iFieldWidths, sFieldValues, sFieldAligns, null, null);
		for(int i = 0; i< 3; i++) 
			m_oAuthRecordListCommonBasket.setFieldTextSize(iSectionId, m_oAuthRecordListCommonBasket.getItemCellCount(0), i, 22);
		
		for(int i= 0; i< m_oAuthRecordListCommonBasket.getChildCount(); i++) {
			m_oAuthRecordListCommonBasket.setFieldPadding(iSectionId,i, 1, "15,0,0,0");
			m_oAuthRecordListCommonBasket.setFieldPadding(iSectionId, i, 2, "15,0,0,0");
		}
		m_oAuthRecordListCommonBasket.moveScrollToItem(0, m_oAuthRecordListCommonBasket.getItemCellCount(0));
		
		//update the corresponding total authorization amount
		m_oTotalAuthorizationAmt.set(m_iSelectedCardAuthItemIndex, m_oTotalAuthorizationAmt.get(m_iSelectedCardAuthItemIndex).add(dTopupValue).setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_UP));
		m_oCardAuthorizationtListCommonBasket.setFieldValue(0, m_iSelectedCardAuthItemIndex, 1, 
				AppGlobal.g_oFuncOutlet.get().getCurrencySign()+m_oTotalAuthorizationAmt.get(m_iSelectedCardAuthItemIndex).toString());
	}
	
	public void removeCancelTopupRecord(){
		String sCancelTopupValue = m_oAuthRecordListCommonBasket.getFieldValue(0, m_iSelectedAuthRecordsItemIndex, 1);
		sCancelTopupValue = sCancelTopupValue.substring(1,sCancelTopupValue.length());
		
		m_oAuthRecordListCommonBasket.removeItem(0, m_iSelectedAuthRecordsItemIndex);
		
		m_oTotalAuthorizationAmt.set(m_iSelectedCardAuthItemIndex, m_oTotalAuthorizationAmt.get(m_iSelectedCardAuthItemIndex).subtract((new BigDecimal(sCancelTopupValue))).setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_UP));
		
		m_oCardAuthorizationtListCommonBasket.setFieldValue(0, m_iSelectedCardAuthItemIndex, 1, 
				AppGlobal.g_oFuncOutlet.get().getCurrencySign()+m_oTotalAuthorizationAmt.get(m_iSelectedCardAuthItemIndex).toString());
		
	}
	
	public void removeCancelCardAuthorizationRecord(){
		//remove the canceled card auth record
		m_oCardAuthorizationtListCommonBasket.removeItem(0, m_iSelectedCardAuthItemIndex);
		m_oPosPaymentGatewayTransactionsCardAuthList.remove(m_iSelectedCardAuthItemIndex);
		
		//clear the topup record common basket
		m_oAuthRecordListCommonBasket.clearAllSections();
	
	}
	
	public BigDecimal getSelectCardTotalAmount(){
		return m_oTotalAuthorizationAmt.get(m_iSelectedCardAuthItemIndex);
	}
	
	private void updateAuthRecordListForSelectedCard(){
		//check if has credit card topup auth record
		for(PosPaymentGatewayTransactions oPaymentGatewayTransactions :m_oPosPaymentGatewayTransactionsCardAuthListByCheck.getPosPaymentGatewayTransactionsList()){
			if(oPaymentGatewayTransactions.getParentAuthCode().equals(m_oSelectedPosPaymentGatewayTransactions.getAuthCode()) 
				&& oPaymentGatewayTransactions.getIntfId() == m_oPosInterfaceConfig.getInterfaceId() 
				&& oPaymentGatewayTransactions.getStatus().isEmpty()
				&& !oPaymentGatewayTransactions.getType().equals(PosPaymentGatewayTransactions.TYPE_COMPLETE_AUTH)
				&& oPaymentGatewayTransactions.getMaskedPan().equals(m_oSelectedPosPaymentGatewayTransactions.getMaskedPan())){
				m_oPosPaymentGatewayTransactionsAuthRecordsList.add(oPaymentGatewayTransactions);
			}
		}
		//sort the auth record list by action time
		Collections.sort(m_oPosPaymentGatewayTransactionsAuthRecordsList, new Comparator<PosPaymentGatewayTransactions>(){
			@Override
			public int compare(PosPaymentGatewayTransactions oPosPaymentGatewayTransactions1, PosPaymentGatewayTransactions oPosPaymentGatewayTransactions2) {
				//sort by time
				return oPosPaymentGatewayTransactions1.getActionTime().compareTo(oPosPaymentGatewayTransactions2.getActionTime());
			}
		});
		addAuthRecordList(0, m_oPosPaymentGatewayTransactionsAuthRecordsList);
	}
	
	public void updateButtonColor() {
		String sUnselectedBackgroundColor = "#FFFFFF";
		String sUnselectedForegroundColor = "#333333";
		String sUnselectedStrokeColor = "#868686";
		String sSelectedBackgroundColor = "#0055B8";
		String sSelectedForegroundColor = "#FFFFFF";
		String sSelectedStrokeColor = "#005080";

		m_oButtonCardAuthorization.setBackgroundColor(sUnselectedBackgroundColor);
		m_oButtonCardAuthorization.setForegroundColor(sUnselectedForegroundColor);
		m_oButtonCardAuthorization.setStrokeColor(sUnselectedStrokeColor);
		
		m_oButtonTopUpAuthorization.setBackgroundColor(sUnselectedBackgroundColor);
		m_oButtonTopUpAuthorization.setForegroundColor(sUnselectedForegroundColor);
		m_oButtonTopUpAuthorization.setStrokeColor(sUnselectedStrokeColor);
		
		m_oButtonManualAuthorization.setBackgroundColor(sUnselectedBackgroundColor);
		m_oButtonManualAuthorization.setForegroundColor(sUnselectedForegroundColor);
		m_oButtonManualAuthorization.setStrokeColor(sUnselectedStrokeColor);
				
		VirtualUIButton oButton = m_oButtonCardAuthorization;
		if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name()))
			oButton = m_oButtonCardAuthorization;
		else if (m_sAuthorizationType.equalsIgnoreCase(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.top_up_authorization.name()))
			oButton = m_oButtonTopUpAuthorization;
		else if (m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.manual_authorization.name()))
			oButton = m_oButtonManualAuthorization;
		
		oButton.setBackgroundColor(sSelectedBackgroundColor);
		oButton.setForegroundColor(sSelectedForegroundColor);
		oButton.setStrokeColor(sSelectedStrokeColor);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name())
				|| m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.top_up_authorization.name())
				|| m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.manual_authorization.name())){
			if(iChildId == m_oButtonCardAuthorization.getId()) {
				m_sAuthorizationType = FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name();
				this.updateButtonColor();
				
				FrameNumberPad_clickEnter();
				
				bMatchChild = true;
			}else if(iChildId == m_oButtonTopUpAuthorization.getId()) {
				m_sAuthorizationType = FramePaymentCardAuthorization.AUTHORIZATION_TYPE.top_up_authorization.name();
				this.updateButtonColor();
				
				FrameNumberPad_clickEnter();
				
				bMatchChild = true;
			}else if(iChildId == m_oButtonManualAuthorization.getId()){
				m_sAuthorizationType = FramePaymentCardAuthorization.AUTHORIZATION_TYPE.manual_authorization.name();
				this.updateButtonColor();
				
				FrameNumberPad_clickEnter();
				
				bMatchChild = true;
			}else if(iChildId == m_oButtonReprintAuthorization.getId()) {
				if(m_oPosPaymentGatewayTransactionsAuthRecordsList.size()>0){
					for (FramePaymentCardAuthorizationListener listener : listeners) {
						if(m_iSelectedAuthRecordsItemIndex >= 0)
							listener.framePaymentCardAuthorizationFunction_clickReprintAuthSlip(m_oAuthRecordListCommonBasket.getFieldValue(0, m_iSelectedAuthRecordsItemIndex, 2)
							, m_oPosPaymentGatewayTransactionsAuthRecordsList.get(m_iSelectedAuthRecordsItemIndex).constructAddSaveJSON(true), getSelectCardTotalAmount().setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_UP));
						else{
							FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
							oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
							oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("please_select_the_authorization_record"));
							oFormDialogBox.show();
							oFormDialogBox = null;
						}
						break;
					}
				}
				bMatchChild = true;
			}
		}
		else if (m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_unattached_payment_auth.name())) {
			if(iChildId == m_oButtonCancelAllCardAuthorization.getId()) {
																 
				for (FramePaymentCardAuthorizationListener listener : listeners) {
					listener.framePaymentCardAuthorizationFunction_clickCancelAll();
					break;
				}
				bMatchChild = true;
			}
		}
		else if (m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.transfer_authorization.name())) {
			if(iChildId == m_oButtonCancelAllCardAuthorization.getId()) {
				if(m_oSelectedPosPaymentGatewayTransactions != null){
					FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this.getParentForm());
					oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("confirmation"));
					oFormConfirmBox.setMessage( AppGlobal.g_oLang.get()._("confirm_to_transfer")+" ?");
					oFormConfirmBox.show();
					
					if (oFormConfirmBox.isOKClicked() != false){
						for (FramePaymentCardAuthorizationListener listener : listeners) {
							if(listener.framePaymentCardAuthorizationFunction_clickTransferAuthorization(
									m_sAuthorizationType, m_oSelectedPosPaymentGatewayTransactions, m_oPosInterfaceConfig)){
								listener.framePaymentCardAuthorizationFunction_clickCancel();
								break;
							}
						}
					}
				} else {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("please_select_the_authorization_record"));
					oFormDialogBox.show();
				}
				bMatchChild = true;
			}
		}
		return bMatchChild;
	}

	@Override
	public void FrameNumberPad_clickEnter() {
		boolean bInvalidInput = false;
		BigDecimal dInput = null;
		try {
			dInput = new BigDecimal(m_oTxtboxValue.getValue());
		}catch(Exception e) {
			bInvalidInput = true;
		}
		
		if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name()) || m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.manual_authorization.name())){
			if (bInvalidInput == true || dInput.compareTo(BigDecimal.ZERO) <= 0){
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("invalid_input"));
				oFormDialogBox.show();
				oFormDialogBox = null;
				return;
			} else {
				String sAuthAmount = new BigDecimal(m_oTxtboxValue.getValue()).setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_UP).toString();
				FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this.getParentForm());
				oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("confirmation"));
				if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name()))
					oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("authorization") + ": "
							+ AppGlobal.g_oFuncOutlet.get().getCurrencySign() + sAuthAmount + System.lineSeparator()
							+ System.lineSeparator() + AppGlobal.g_oLang.get()._("confirm_to_card_authorization")
							+ " ?");
				else if ((m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.manual_authorization.name())))
					oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("authorization") + ": "
							+ AppGlobal.g_oFuncOutlet.get().getCurrencySign() + sAuthAmount + System.lineSeparator()
							+ System.lineSeparator() + AppGlobal.g_oLang.get()._("confirm_to_manual_authorization")
							+ " ?");
				oFormConfirmBox.setConfirmTimeout(m_iRequestTimeout);
				//oFormConfirmBox.supportLoadingScreen();
				oFormConfirmBox.show();
				
				if (oFormConfirmBox.isOKClicked() == false) 
					return;
				else {
					// Add element action to visible the refresh screen when click close button
					m_oTitleHeader.getCloseButtonElement().addClickVisibleElements(m_oFrameRefreshPanel, true);
					
					for (FramePaymentCardAuthorizationListener listener : listeners) {
						//card authorization button
						listener.framePaymentCardAuthorizationFunction_clickCardAuthorization(m_sAuthorizationType,
								sAuthAmount, "", "", "", "", m_oPosInterfaceConfig, "");
					}
				}
			}
			
		} else if (m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.top_up_authorization.name())){
			if(m_oSelectedPosPaymentGatewayTransactions == null){
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("please_select_a_card_for_top_up"));
				oFormDialogBox.show();
				oFormDialogBox = null;
				return;
			} else if (bInvalidInput == true || dInput.compareTo(BigDecimal.ZERO) <= 0) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("invalid_input"));
				oFormDialogBox.show();
				oFormDialogBox = null;
				return;
			} else {
				String sOriginalTotalAuthAmt = "";
				sOriginalTotalAuthAmt = AppGlobal.g_oFuncOutlet.get().getCurrencySign()+m_oTotalAuthorizationAmt.get(m_iSelectedCardAuthItemIndex).setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_UP);
				
				String sTopupAmt = "";
				sTopupAmt = new BigDecimal(m_oTxtboxValue.getValue()).setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_UP).toString();
				
				String sNewTotalAuthAmt = "";
				sNewTotalAuthAmt = m_oTotalAuthorizationAmt.get(m_iSelectedCardAuthItemIndex).add(new BigDecimal(m_oTxtboxValue.getValue())).setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_UP).toString();
				
				FormListMessageBox oFormListMessageBox = new FormListMessageBox(true, AppGlobal.g_oLang.get()._("ok"),  this.getParentForm());
				oFormListMessageBox.setTitle(AppGlobal.g_oLang.get()._("top_up_information"));
				oFormListMessageBox.addColumnHeader(AppGlobal.g_oLang.get()._("general_information"), 300);
				oFormListMessageBox.addColumnHeader(AppGlobal.g_oLang.get()._("value"), 300);
				
				ArrayList<String> sMessage = new ArrayList<>();
				
				//pan
				sMessage = new ArrayList<>();
				sMessage.add(AppGlobal.g_oLang.get()._("card_number"));
				sMessage.add(m_oSelectedPosPaymentGatewayTransactions.getMaskedPan());
				oFormListMessageBox.addMessage(sMessage);
				
				//original auth amount
				sMessage = new ArrayList<>();
				sMessage.add(AppGlobal.g_oLang.get()._("original_authorization_amount"));
				sMessage.add(sOriginalTotalAuthAmt);
				oFormListMessageBox.addMessage(sMessage);
				
				//top up amount
				sMessage = new ArrayList<>();
				sMessage.add(AppGlobal.g_oLang.get()._("top_up_amount"));
				sMessage.add(AppGlobal.g_oFuncOutlet.get().getCurrencySign() + sTopupAmt);
				oFormListMessageBox.addMessage(sMessage);
				
				//new auth amount
				sMessage = new ArrayList<>();
				sMessage.add(AppGlobal.g_oLang.get()._("new_authorization_amount"));
				sMessage.add(AppGlobal.g_oFuncOutlet.get().getCurrencySign() + sNewTotalAuthAmt);
				oFormListMessageBox.addMessage(sMessage);
				oFormListMessageBox.show();
				oFormListMessageBox = null;
				
				FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this.getParentForm());
				oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("confirmation"));
				oFormConfirmBox.setMessage( AppGlobal.g_oLang.get()._("confirm_to_top_up")+"?");
				oFormConfirmBox.show();
				
				if (oFormConfirmBox.isOKClicked() == false){
					m_oTxtboxValue.setValue("");
					return;
				}
				else{
					String sRefNo = m_oSelectedPosPaymentGatewayTransactions.getRefNo();
					String sToken = m_oSelectedPosPaymentGatewayTransactions.getToken();
					PosPaymentGatewayTransactionsList oPosPaymentGatewayTransactionsCardAuthListByCheck = m_oFuncCheck.getPosPaymentGatewayTransactionsLists();
					ArrayList<PosPaymentGatewayTransactions> oPosPaymentGatewayTransactionsAuthRecordsList = new ArrayList<PosPaymentGatewayTransactions>();
					//check if has credit card topup auth record
					for(PosPaymentGatewayTransactions oPaymentGatewayTransactions :oPosPaymentGatewayTransactionsCardAuthListByCheck.getPosPaymentGatewayTransactionsList()){
						if(oPaymentGatewayTransactions.getParentAuthCode().equals(m_oSelectedPosPaymentGatewayTransactions.getAuthCode()) 
							&& oPaymentGatewayTransactions.getIntfId() == m_oPosInterfaceConfig.getInterfaceId() 
							&& oPaymentGatewayTransactions.getStatus().isEmpty()
							&& !oPaymentGatewayTransactions.getType().equals(PosPaymentGatewayTransactions.TYPE_COMPLETE_AUTH)
							&& oPaymentGatewayTransactions.getMaskedPan().equals(m_oSelectedPosPaymentGatewayTransactions.getMaskedPan())){
							oPosPaymentGatewayTransactionsAuthRecordsList.add(oPaymentGatewayTransactions);
						}
					}
					
					//sort the auth record list by action time
					Collections.sort(oPosPaymentGatewayTransactionsAuthRecordsList, new Comparator<PosPaymentGatewayTransactions>(){
						@Override
						public int compare(PosPaymentGatewayTransactions oPosPaymentGatewayTransactions1, PosPaymentGatewayTransactions oPosPaymentGatewayTransactions2) {
							//sort by time
							return oPosPaymentGatewayTransactions1.getActionTime().compareTo(oPosPaymentGatewayTransactions2.getActionTime());
						}
					});
					
					if(oPosPaymentGatewayTransactionsAuthRecordsList.size() > 0){
						sRefNo = oPosPaymentGatewayTransactionsAuthRecordsList.get(oPosPaymentGatewayTransactionsAuthRecordsList.size() -1).getRefNo();
						sToken = oPosPaymentGatewayTransactionsAuthRecordsList.get(oPosPaymentGatewayTransactionsAuthRecordsList.size() -1).getToken();
					}
					
					for (FramePaymentCardAuthorizationListener listener : listeners) {
						//card authorization button
						listener.framePaymentCardAuthorizationFunction_clickCardAuthorization(m_sAuthorizationType, sTopupAmt, sNewTotalAuthAmt, 
								sRefNo, m_oSelectedPosPaymentGatewayTransactions.getAuthCode(), m_oSelectedPosPaymentGatewayTransactions.getMaskedPan(), m_oPosInterfaceConfig, sToken);
					}
					
					// reload the memory payment gateway transaction id
					for(PosPaymentGatewayTransactions oPosPaymentGatewayTransactionsCardAuth : m_oPosPaymentGatewayTransactionsCardAuthList){
						
						if(oPosPaymentGatewayTransactionsCardAuth.getAuthCode().equals(m_oSelectedPosPaymentGatewayTransactions.getAuthCode())){
							oPosPaymentGatewayTransactionsCardAuth.setPgtxId(m_oSelectedPosPaymentGatewayTransactions.getPgtxId());
						}
					}
				}
			}
		}
		
		//clear the input box
		m_oTxtboxValue.setValue("");
	}

	@Override
	public void FrameNumberPad_clickCancel() {
		m_oTxtboxValue.setValue("");
	}

	@Override
	public void FrameNumberPad_clickNumber(String string) {
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId,
			String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		for (FramePaymentCardAuthorizationListener listener : listeners) {
			if(iBasketId == m_oCardAuthorizationtListCommonBasket.getId()){
				m_oSelectedPosPaymentGatewayTransactions = m_oPosPaymentGatewayTransactionsCardAuthList.get(iItemIndex);
				m_iSelectedCardAuthItemIndex = iItemIndex;
				
				m_oPosPaymentGatewayTransactionsAuthRecordsList.clear();
				
				for(int i=0; i<m_oCardAuthorizationtListCommonBasket.getItemCount(0); i++){
					if(i == iItemIndex)
						setCommonBasketBackgroudColor(iBasketId, i, "#E1ECF8");
					else
						setCommonBasketBackgroudColor(iBasketId, i, "#FFFFFF");
				}
				
				String sCancelAuthType = FramePaymentCardAuthorization.AUTHORIZATION_TYPE.complete_authorization.name(); 
				
				if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_complete_authorization.name())){
					
					FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this.getParentForm());
					oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("confirmation"));
					
					oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("pan") + ": "
							+ m_oSelectedPosPaymentGatewayTransactions.getMaskedPan() + System.lineSeparator()
							+ AppGlobal.g_oLang.get()._("authorization_amount") + ": "
							+ AppGlobal.g_oFuncOutlet.get().getCurrencySign()
							+ m_oPosPaymentGatewayTransactionsCardAuthList.get(iItemIndex).getAmount().setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_UP)
							+ System.lineSeparator() + System.lineSeparator()
							+ AppGlobal.g_oLang.get()._("confirm_to_cancel_complete_authorization") + " ?"
							+ System.lineSeparator());
					oFormConfirmBox.show();
					
					if(oFormConfirmBox.isOKClicked()){
						listener.framePaymentCardAuthorizationFunction_clickCancelAuthorization(m_sAuthorizationType, sCancelAuthType, m_oPosPaymentGatewayTransactionsCardAuthList.get(iItemIndex),
								m_oPosInterfaceConfig, m_oPosPaymentGatewayTransactionsAuthRecordsList);
					}else
						return;
				}
				else
					//add the card authorization to list
					m_oPosPaymentGatewayTransactionsAuthRecordsList.add(m_oSelectedPosPaymentGatewayTransactions);
				updateAuthRecordListForSelectedCard();
				
				//Update the authorization record list and select the first record
				m_iSelectedAuthRecordsItemIndex = 0;
				for(int i=0; i<m_oAuthRecordListCommonBasket.getItemCount(0); i++){
					if(i == m_iSelectedAuthRecordsItemIndex)
						setCommonBasketBackgroudColor(m_oAuthRecordListCommonBasket.getId(), i, "#E1ECF8");
					else
						setCommonBasketBackgroudColor(m_oAuthRecordListCommonBasket.getId(), i, "#FFFFFF");
				}
				break;
			}
			else if(iBasketId == m_oAuthRecordListCommonBasket.getId()){
				if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name()) || 
						m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.top_up_authorization.name()) || 
						m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_authorization.name())){
					m_iSelectedAuthRecordsItemIndex = iItemIndex;
					
					String sCancelAuthType = m_oAuthRecordListCommonBasket.getFieldValue(0, m_iSelectedAuthRecordsItemIndex, 2); 
					
					for(int i=0; i<m_oAuthRecordListCommonBasket.getItemCount(0); i++){
						if(i == iItemIndex)
							setCommonBasketBackgroudColor(iBasketId, i, "#E1ECF8");
						else
							setCommonBasketBackgroudColor(iBasketId, i, "#FFFFFF");
					}
					if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_authorization.name())){
						FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"), AppGlobal.g_oLang.get()._("no"), this.getParentForm());
						oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("confirmation"));
						if (sCancelAuthType.equals(AppGlobal.g_oLang.get()._("authorization"))){
							BigDecimal bTotalAuthAmt = m_oPosPaymentGatewayTransactionsAuthRecordsList.get(iItemIndex).getAmount();
							for(PosPaymentGatewayTransactions oPosPaymentGatewayTransactions: m_oPosPaymentGatewayTransactionsAuthRecordsList){
								if(oPosPaymentGatewayTransactions.getParentAuthCode().equals(m_oPosPaymentGatewayTransactionsAuthRecordsList.get(iItemIndex).getAuthCode()) 
										&& oPosPaymentGatewayTransactions.getIntfId() == m_oPosInterfaceConfig.getInterfaceId() 
										&& oPosPaymentGatewayTransactions.getStatus().isEmpty()
										&& oPosPaymentGatewayTransactions.getMaskedPan().equals(m_oPosPaymentGatewayTransactionsAuthRecordsList.get(iItemIndex).getMaskedPan()))
										bTotalAuthAmt = bTotalAuthAmt.add(oPosPaymentGatewayTransactions.getAmount());
							}		
							
							oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("pan") + ": "
									+ m_oSelectedPosPaymentGatewayTransactions.getMaskedPan() + System.lineSeparator()
									+ AppGlobal.g_oLang.get()._("authorization_amount") + ": "
									+ AppGlobal.g_oFuncOutlet.get().getCurrencySign()
									+ bTotalAuthAmt.setScale(AppGlobal.g_oFuncOutlet.get().getPayRoundDecimal(), BigDecimal.ROUND_HALF_UP)
									+ System.lineSeparator() + System.lineSeparator()
									+ AppGlobal.g_oLang.get()._("confirm_to_cancel_card_authorization") + " ?"
									+ System.lineSeparator());
							oFormConfirmBox.show();
							
							if(oFormConfirmBox.isOKClicked()) {
								sCancelAuthType = FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name(); 
								listener.framePaymentCardAuthorizationFunction_clickCancelAuthorization(m_sAuthorizationType, sCancelAuthType, m_oPosPaymentGatewayTransactionsAuthRecordsList.get(iItemIndex),
										m_oPosInterfaceConfig, m_oPosPaymentGatewayTransactionsAuthRecordsList);
							}
						}
						else
							return;
					}
				}
			}
		}
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId,
			int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
	}
	
	public void setCommonBasketBackgroudColor(int iBasketId,int iRecordIndex, String sBackgroundColor){
		if(iBasketId == m_oCardAuthorizationtListCommonBasket.getId()){
			for(int i = 0; i<2; i++)
				m_oCardAuthorizationtListCommonBasket.setFieldBackgroundColor(0, iRecordIndex, i, sBackgroundColor);
		}else if(iBasketId == m_oAuthRecordListCommonBasket.getId()){
			if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_authorization.name()) || 
					m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.top_up_authorization.name()) || 
					m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name())){
				for(int i = 0; i< 3; i++)
					m_oAuthRecordListCommonBasket.setFieldBackgroundColor(0, iRecordIndex, i, sBackgroundColor);
			}
		}
	}

	public void setCommonBasketForegroundColor(int iBasketId,int iRecordIndex, String sForegroundColor){
		if(iBasketId == m_oCardAuthorizationtListCommonBasket.getId()){
			for(int i = 0; i<2; i++)
				m_oCardAuthorizationtListCommonBasket.setFieldForegroundColor(0, iRecordIndex, i, sForegroundColor);
		}else if(iBasketId == m_oAuthRecordListCommonBasket.getId()){
			if(m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.cancel_authorization.name()) || 
					m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.top_up_authorization.name()) || 
					m_sAuthorizationType.equals(FramePaymentCardAuthorization.AUTHORIZATION_TYPE.card_authorization.name())){
				for(int i = 0; i< 3; i++)
					m_oAuthRecordListCommonBasket.setFieldForegroundColor(0, iRecordIndex, i, sForegroundColor);
			}
		}
	}
	
	@Override
	public void FrameTitleHeader_close() {
		for (FramePaymentCardAuthorizationListener listener : listeners){
			listener.framePaymentCardAuthorizationFunction_clickCancel();
		}
	}
}
