package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.lang.model.type.NullType;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import om.PosOctopusTransaction;
import commonui.FormDialogBox;
import commonui.FormInputBox;
import core.Controller;
import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormOctopusOperation extends VirtualUIForm implements FrameOctopusOperationListener {

	public enum FUNC_CALL_LIST{RwlInitComm, RwlTxnAmt, RwlPoll, RwlAddValue, RwlDeduct, setOctopusDisplay, RwlPortClose, RwlXFile, ResendResponse, RwlGetExtraInfo, RwlTimeVer};
	public enum OPERATION_TYPE_LIST{init, add_value, deduct_value, read_card, set_display, close, xfile};
	public enum DISPLAY_MODE{clear_all, idle, not_avail};
	
	static public final int ERR_NO = 0;
	static public final int ERR_FIRST_ERROR = 100000;
	static public final int ERR_NOT_CONNECT = 100001;
	static public final int ERR_INVALID_RESPONSE = 100005;
	static public final int ERR_READ_CARD_ERROR = 100016;
	static public final int ERR_WRITE_CARD_ERROR = 100017;
	static public final int ERR_INVALID_CARD = 100019;
	static public final int ERR_NO_CARD_AFTER_POLL = 100020;
	static public final int ERR_INVALID_OCTOPUS = 100021;
	static public final int ERR_INCOMPLETE_TRANSACTION = 100022;
	static public final int ERR_CSC_INVALID_CARD = 100024;
	static public final int ERR_INCOMPLETE_TRANSACTION2 = 100025;
	static public final int ERR_NO_CARD_DURING_POLL = 100032;
	static public final int ERR_CARD_AUTH_ERROR = 100034;
	static public final int ERR_INSUFFICIENT_VALUE = 100048;
	static public final int ERR_REMAIN_VALUE_EXCEED_LIMIT = 100049;
	static public final int ERR_ADD_VALUE_EXCEEDED = 100050;
	static public final int ERR_INVALID_POS_CONTROLLER_ID = 100051;
	
	TemplateBuilder m_oTemplateBuilder;

	private FrameOctopusOperation m_oFrameOctopusOperation;
//JohnLiu 06112017 -- start
	private VirtualUIFrame m_oFrameCover;
//JohnLiu 06112017 -- end
	
	private FuncOctopus m_oFuncOctopus;
	
	private int m_iOctopusTransactionValueInCent;		// Transaction amount in cents
	private int m_iSubCom;		// For poll function
	
	static private int MAX_ARGS_NO = 10;
	private Object[] m_oCurrentStepArgs;
	
	private String m_sLastErrorMessage;
	private boolean m_bNeedRetryForLastOperation;
	private boolean m_bNeedHideCancelButton;
	private boolean m_bNeedPromptErrorForLastOperation;
	private boolean m_bNeedPromptTimeoutError;
	private boolean m_bTransactionLogFull;
	
	// For retry checking
	private String m_sPrevCardId;
	
	// Card information
	private String m_sCardId;
	private String m_sDeviceId;
	private long m_lUDSN;
	private BigDecimal m_dOrginalRemainAmount;
	private BigDecimal m_dCurrentRemainAmount;
	private String m_sType;
	private String m_sCustInfo;
	private String m_sManufactureID;
	private String m_sLastAddValueType;
	private String m_sLastAddValueDate;
	private String m_sOctopusAlertMessage;
	private String m_sTransactionTimeString;
	
	private boolean m_bCancelByUser;
	private boolean m_bProcessSuccess;
	private String m_sOperationType;		// Initial octopus; Add value; Deduce value; Ask octopus info; Set octopus display; Close octopus
	private LinkedList<String> m_oFuncCallList;
	
	// For last 10 Octopus transaction
	private ArrayList<ClsOctopusTransaction> m_oLastOctopusTransactions;
	
	private DateTime m_dtStartTime;
	
	public FormOctopusOperation(FuncOctopus oFuncOctopus, Controller oParentController){
		super(oParentController);
		
		m_bCancelByUser = false;
		m_bProcessSuccess = false;
		m_bNeedPromptTimeoutError = false;
		m_bTransactionLogFull = false;
		m_oFuncCallList = new LinkedList<String>();
		m_iOctopusTransactionValueInCent = 0;
		m_iSubCom = 0;
		m_oCurrentStepArgs = new Object[MAX_ARGS_NO];	// Max no. of args for function = 10
		
		m_sPrevCardId = "";
		m_sCardId = "";
		m_dOrginalRemainAmount = BigDecimal.ZERO;
		m_sType = "";
		m_sCustInfo = "";
		m_sManufactureID = "";
		m_sLastAddValueType = "";
		m_sLastAddValueDate = "";
		m_sOctopusAlertMessage = "";
		m_sTransactionTimeString = "";
		
		m_oLastOctopusTransactions = new ArrayList<ClsOctopusTransaction>();
		
		m_oFuncOctopus = oFuncOctopus;
		m_oFuncOctopus.setNeedAutoClearDisplay(false);
		
		m_dtStartTime = AppGlobal.getCurrentTime(false);
	}
	
	public boolean initForOctopusInitialization(){	
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmOctopusOperation.xml");
		
		// Login Frame
		m_oFrameOctopusOperation = new FrameOctopusOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameOctopusOperation, "fraOctopusOperation");
		m_oFrameOctopusOperation.init();
		m_oFrameOctopusOperation.showInitScreen();
		// Add listener;
		m_oFrameOctopusOperation.addListener(this);
		this.attachChild(m_oFrameOctopusOperation);
		
		m_sOperationType = OPERATION_TYPE_LIST.init.name();
		
		// Prompt timeout error
		m_bNeedPromptTimeoutError = true;
		
		// Timeout cannot be skipped
		m_oFrameOctopusOperation.setAllowSkipTimeoutHandling(false);
		
		// Add step
		m_oFuncCallList.add(FUNC_CALL_LIST.RwlInitComm.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.RwlTimeVer.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.setOctopusDisplay.name());
		
		// Start process
		stepControl(null, null);
		
		return true;
	}
	
	public boolean initForAddValue(int iAddValue){
		
		// Customer display
		m_oFuncOctopus.updateCustomerDisplayDataUpdateTimestamp();
		m_oFuncOctopus.generateCustomerDisplayInterfaceFiles(FuncOctopus.CUST_DISPLAY_ADD_VALUE_ASK_CARD, new BigDecimal(iAddValue), BigDecimal.ZERO);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmOctopusOperation.xml");
		
		// Login Frame
		m_oFrameOctopusOperation = new FrameOctopusOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameOctopusOperation, "fraOctopusOperation");
		m_oFrameOctopusOperation.init();
		m_oFrameOctopusOperation.showAddValueScreen(iAddValue + ".0");
		// Add listener;
		m_oFrameOctopusOperation.addListener(this);
		this.attachChild(m_oFrameOctopusOperation);
		
		m_sOperationType = OPERATION_TYPE_LIST.add_value.name();
		m_iOctopusTransactionValueInCent = iAddValue * 10;
		
		// Prompt timeout error
		m_bNeedPromptTimeoutError = true;
		
		// Timeout can be skipped
		m_oFrameOctopusOperation.setAllowSkipTimeoutHandling(true);
		
		// Add step
		m_oFuncCallList.add(FUNC_CALL_LIST.RwlTxnAmt.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.RwlPoll.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.RwlAddValue.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.RwlGetExtraInfo.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.RwlGetExtraInfo.name());
		
		// Start process
		// First function arguments
		m_oCurrentStepArgs[0] = m_iOctopusTransactionValueInCent;
		m_oCurrentStepArgs[1] = -30000;
		stepControl(null, m_oCurrentStepArgs);
		
		return true;
	}
	
	public boolean initForDeductValue(BigDecimal dDeductValue){	
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmOctopusOperation.xml");
		
//JohnLiu 06112017 -- start
		m_oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCover, "fraCoverFrame");
		m_oFrameCover.setVisible(true);
		this.attachChild(m_oFrameCover);
//JohnLiu 06112017 -- end
		
		// Login Frame
		m_oFrameOctopusOperation = new FrameOctopusOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameOctopusOperation, "fraOctopusOperation");
		m_oFrameOctopusOperation.init();
		m_oFrameOctopusOperation.showDeductValueScreen(StringLib.BigDecimalToString(dDeductValue, 1));
		// Add listener;
		m_oFrameOctopusOperation.addListener(this);
		this.attachChild(m_oFrameOctopusOperation);
		
		m_sOperationType = OPERATION_TYPE_LIST.deduct_value.name();
		m_iOctopusTransactionValueInCent = dDeductValue.multiply(new BigDecimal("10")).intValue();
		
		// Prompt timeout error
		m_bNeedPromptTimeoutError = true;
		
		// Timeout can be skipped
		m_oFrameOctopusOperation.setAllowSkipTimeoutHandling(true);
		
		// Add step
		m_oFuncCallList.add(FUNC_CALL_LIST.RwlTxnAmt.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.RwlPoll.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.RwlDeduct.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.RwlGetExtraInfo.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.RwlGetExtraInfo.name());
		
		// Start process
		// First function arguments
		m_oCurrentStepArgs[0] = m_iOctopusTransactionValueInCent;
		m_oCurrentStepArgs[1] = -30000;
		stepControl(null, m_oCurrentStepArgs);
		
		return true;
	}
	
	public boolean initForReadCard(){	
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmOctopusOperation.xml");
		
		// Login Frame
		m_oFrameOctopusOperation = new FrameOctopusOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameOctopusOperation, "fraOctopusOperation");
		m_oFrameOctopusOperation.init();
		m_oFrameOctopusOperation.showReadCardScreen();
		// Add listener;
		m_oFrameOctopusOperation.addListener(this);
		this.attachChild(m_oFrameOctopusOperation);
		
		m_sOperationType = OPERATION_TYPE_LIST.read_card.name();
		m_iSubCom = 2;
		
		// Prompt timeout error
		m_bNeedPromptTimeoutError = true;
		
		// Timeout can be skipped
		m_oFrameOctopusOperation.setAllowSkipTimeoutHandling(true);
		
		// Add step
		m_oFuncCallList.add(FUNC_CALL_LIST.setOctopusDisplay.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.RwlPoll.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.RwlTxnAmt.name());
		
		// Start process
		// First function arguments
		m_oCurrentStepArgs[0] = DISPLAY_MODE.clear_all.name();
		stepControl(null, m_oCurrentStepArgs);
		
		return true;
	}
	
	// iDisplayMode:	0 - Clear all
	//					1 - Idle
	//					2 - Not available
	public boolean initForSetOctopusDisplay(int iDisplayMode){

		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmOctopusOperation.xml");
		
		// Login Frame
		m_oFrameOctopusOperation = new FrameOctopusOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameOctopusOperation, "fraOctopusOperation");
		m_oFrameOctopusOperation.init();
		m_oFrameOctopusOperation.setVisible(false);
		// Add listener;
		m_oFrameOctopusOperation.addListener(this);
		this.attachChild(m_oFrameOctopusOperation);
		
		m_sOperationType = OPERATION_TYPE_LIST.set_display.name();
		
		// Not prompt timeout error
		m_bNeedPromptTimeoutError = false;
		
		// Timeout can be skipped
		m_oFrameOctopusOperation.setAllowSkipTimeoutHandling(true);
		
		// Add step
		m_oFuncCallList.add(FUNC_CALL_LIST.setOctopusDisplay.name());
		
		// Start process
		switch (iDisplayMode) {
		case 0:
			m_oCurrentStepArgs[0] = DISPLAY_MODE.clear_all.name();
			break;
		case 1:
			m_oCurrentStepArgs[0] = DISPLAY_MODE.idle.name();
			break;
		case 2:
			m_oCurrentStepArgs[0] = DISPLAY_MODE.not_avail.name();
			break;
		}
		stepControl(null, m_oCurrentStepArgs);
		
		return true;
	}
	
	public boolean initForCloseOctopus(){	
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmOctopusOperation.xml");
		
		// Login Frame
		m_oFrameOctopusOperation = new FrameOctopusOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameOctopusOperation, "fraOctopusOperation");
		m_oFrameOctopusOperation.init();
		m_oFrameOctopusOperation.showStopScreen();
		// Add listener;
		m_oFrameOctopusOperation.addListener(this);
		this.attachChild(m_oFrameOctopusOperation);
		
		m_sOperationType = OPERATION_TYPE_LIST.close.name();
		
		// Not prompt timeout error
		m_bNeedPromptTimeoutError = false;
		
		// Timeout cannot be skipped
		m_oFrameOctopusOperation.setAllowSkipTimeoutHandling(false);
		
		// Add step
		m_oFuncCallList.add(FUNC_CALL_LIST.setOctopusDisplay.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.RwlPortClose.name());
		
		// Start process
		// First function arguments
		m_oCurrentStepArgs[0] = DISPLAY_MODE.not_avail.name();
		stepControl(null, m_oCurrentStepArgs);
		
		return true;
	}
	
	public boolean initForXFile(String sFilename){	
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmOctopusOperation.xml");
		
		// Login Frame
		m_oFrameOctopusOperation = new FrameOctopusOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameOctopusOperation, "fraOctopusOperation");
		m_oFrameOctopusOperation.init();
		m_oFrameOctopusOperation.showXFileScreen();
		// Add listener;
		m_oFrameOctopusOperation.addListener(this);
		this.attachChild(m_oFrameOctopusOperation);
		
		m_sOperationType = OPERATION_TYPE_LIST.xfile.name();
		
		// Not prompt timeout error
		m_bNeedPromptTimeoutError = false;
		
		// Timeout cannot be skipped
		m_oFrameOctopusOperation.setAllowSkipTimeoutHandling(false);
		
		// Add step
		m_oFuncCallList.add(FUNC_CALL_LIST.RwlXFile.name());
		
		// Start process
		// First function arguments
		m_oCurrentStepArgs[0] = sFilename;
		stepControl(null, m_oCurrentStepArgs);
		
		return true;
	}
	
	private void stepControl(String sResponse, Object[] oNextStepFunctionArgs){
		
		// Skip the version checking
		m_oFrameOctopusOperation.setSkipVersionChecking(false);
		
		if(m_oFuncCallList.isEmpty()){
			// Finish operation
			m_bProcessSuccess = true;
			
			if(m_sOperationType.equals(OPERATION_TYPE_LIST.add_value.name()) ||
					// According to customer request, no need to show result page for deduct Octopus amount
					m_sOperationType.equals(OPERATION_TYPE_LIST.read_card.name())) {
				// No need to quit the form
				
			} else {
				finishOperation();
			}
			
			return;
		}else{
			// Check if Cancel is clicked before process next function
			if(sResponse == null && m_bCancelByUser){
				
				if(m_sOperationType.equals(OPERATION_TYPE_LIST.read_card.name()) == false && m_bCancelByUser){
					try{
						AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId()+"", AppGlobal.g_oFuncUser.get().getUserId()+"",
								"OCTOPUS - Cancel - Function: " + m_sOperationType + ", Error message: cancel octopus operation by user");
					}catch(Exception e){
						AppGlobal.stack2Log(e);
					}
				}
				
				finishOperation();
				
				return;
			}
		}
		
		String sCurrentStep = m_oFuncCallList.getFirst();
		
		// Call function
		if(sCurrentStep.equals(FUNC_CALL_LIST.RwlInitComm.name())){
			this.RwlInitComm(sResponse);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.RwlTimeVer.name())){
			this.RwlTimeVer(sResponse);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.RwlTxnAmt.name())){
			this.RwlTxnAmt((Integer)oNextStepFunctionArgs[0], (Integer)oNextStepFunctionArgs[1], 0, 0, sResponse);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.RwlPoll.name())){
			this.RwlPoll(m_iSubCom, m_oFuncOctopus.getOctopusPollTimeout(), sResponse);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.RwlAddValue.name())){
			this.RwlAddValue(m_iOctopusTransactionValueInCent, 0, sResponse);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.RwlDeduct.name())){
			this.RwlDeduct(m_iOctopusTransactionValueInCent, sResponse);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.RwlGetExtraInfo.name())){
			this.RwlGetExtraInfo(sResponse, (Integer)oNextStepFunctionArgs[0], (Integer)oNextStepFunctionArgs[1]);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.RwlPortClose.name())){
			this.RwlPortClose(sResponse);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.setOctopusDisplay.name())){
			this.setOctopusDisplay((String)oNextStepFunctionArgs[0], sResponse);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.RwlXFile.name())){
			this.RwlXFile((String)oNextStepFunctionArgs[0], sResponse);
		}
	}
	
	// sArgType : x1x2...
	// x1 : Argument 1
	//		- i : integer
	//		- s : string
	private String[] processArgString(String sResponse){
		if (sResponse.isEmpty())
			return null;
		
		// Convert the byte array to Object array
		String[] oReturnArgs = sResponse.split(";");
		return oReturnArgs;
	}
	
	private String getArgString(String[] sRetArgsString, int iIndex, int iLength, boolean bDecode){
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
	}
	
	private void RwlInitComm(String sResponse){
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			// Error code : 0 - Success
			//				1 - Error code
			// Pre-process the response
			String[] sRetArgsString = processArgString(sResponse);
			
			int iErrorCode = checkOctopusError(Integer.parseInt(sRetArgsString[0]));
			if(iErrorCode == 0){
				// No error
				
				// Write info log
				writeInfoLog("RwlInitComm", iErrorCode, "");
				
				// Go to next step
				m_oFuncCallList.removeFirst();
				
				// Next step - RwlTimeVer
				m_oCurrentStepArgs[0] = DISPLAY_MODE.idle.name();
				stepControl(null, m_oCurrentStepArgs);
			}else{
				// Error ocrur
				// Write error log
				writeErrorLog("RwlInitComm", iErrorCode, "Error Message: " + m_sLastErrorMessage);
				
				// Finish showing this form
				if(m_bNeedPromptErrorForLastOperation){
					// Prompt error
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(m_sLastErrorMessage);
					oFormDialogBox.show();
				}
				
				if(m_bNeedRetryForLastOperation){
					// Retry
					stepControl(null, m_oCurrentStepArgs);
					return;
				}
				
				// No retry, abort operation
				finishOperation();
			}
			
			return;
		}
		
		Object[] oParameters = new Object[2];
		oParameters[0] = 0;
		oParameters[1] = 0;
		String sValue = m_oFuncOctopus.createWinFcnMapString(FUNC_CALL_LIST.RwlInitComm.name(), oParameters);
		
		m_oFrameOctopusOperation.createForwardEvent(sValue, m_oFuncOctopus.getOctopusWaitTimeout(), m_oFuncOctopus.getSocketDelay());
	}
	
	private void RwlTimeVer(String sResponse){
		
		// Check if the response is success or not
		if(sResponse != null){
			// Error code : 0 - Success
			//				1 - Error code
			// Pre-process the response
			String[] sRetArgsString = processArgString(sResponse);
			
			int iErrorCode = checkOctopusError(Integer.parseInt(sRetArgsString[0]));
			if(iErrorCode == 0){
				// No error
				parseOctopusTimeVer(decodeString(decodeString(sRetArgsString[1], 64), 64));
			}else{
				// Error ocrur
				// Write error log
				writeErrorLog("RwlTimeVer", iErrorCode, "Error Message: " + m_sLastErrorMessage);
			}
			
			// Go to next step
			m_oFuncCallList.removeFirst();
			
			// Next step - setOctopusDisplay
			m_oCurrentStepArgs[0] = DISPLAY_MODE.idle.name();
			stepControl(null, m_oCurrentStepArgs);
			
			return;
		}
		
		Object[] oParameters = new Object[1];
		oParameters[0] = 0;
		String sValue = m_oFuncOctopus.createWinFcnMapString(FUNC_CALL_LIST.RwlTimeVer.name(), oParameters);
		
		m_oFrameOctopusOperation.createForwardEvent(sValue, m_oFuncOctopus.getOctopusWaitTimeout(), m_oFuncOctopus.getSocketDelay());
	}
	
	private void RwlTxnAmt(Integer V, Integer VR, Integer Sound, Integer LED, String sResponse){
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			// Error code : 0 - Success
			//				1 - Error code
			// Pre-process the response
			String[] sRetArgsString = processArgString(sResponse);
			
			int iErrorCode = checkOctopusError(Integer.parseInt(sRetArgsString[0]));
			if(iErrorCode == 0){
				// No error
				if(m_sOperationType.equals(OPERATION_TYPE_LIST.add_value.name()) || m_sOperationType.equals(OPERATION_TYPE_LIST.deduct_value.name())){
					if(m_sPrevCardId.length() == 0){
						// Not under the 20s loop for error code 100022
						m_oFrameOctopusOperation.setShowCancelButtonTimer(true);
					}
				}else if(m_sOperationType.equals(OPERATION_TYPE_LIST.read_card.name())){
					m_oFrameOctopusOperation.showCancelButton(true);
				}
				
				// Next step - RwlPoll
				m_oFuncCallList.removeFirst();
				stepControl(null, m_oCurrentStepArgs);
			}else{
				// Error ocrur
				// Write error log
				writeErrorLog("RwlTxnAmt", iErrorCode, "Error Message: " + m_sLastErrorMessage);
				
				// Finish showing this form
				if(m_bNeedPromptErrorForLastOperation){
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(m_sLastErrorMessage);
					oFormDialogBox.show();
				}
				
				if(m_bNeedRetryForLastOperation){
					stepControl(null, m_oCurrentStepArgs);
					return;
				}
				
				// No retry, abort operation
				finishOperation();
			}
			
			return;
		}
		
		Object[] oParameters = new Object[4];
		oParameters[0] = V;
		oParameters[1] = VR;
		oParameters[2] = Sound;
		oParameters[3] = LED;
		String sValue = m_oFuncOctopus.createWinFcnMapString(FUNC_CALL_LIST.RwlTxnAmt.name(), oParameters);
		
		m_oFrameOctopusOperation.createForwardEvent(sValue, m_oFuncOctopus.getOctopusWaitTimeout(), m_oFuncOctopus.getSocketDelay());
	}
	
	private void RwlPoll(int SubCom, int TimeOut, String sResponse){
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			// Error code : 0 - Success
			//				1 - Error code
			// Pre-process the response
			String[] sRetArgsString = processArgString(sResponse);
			
			int iErrorCode = checkOctopusError(Integer.parseInt(sRetArgsString[0]));
			if(iErrorCode < ERR_FIRST_ERROR){
				// No error
				
				String sReturn = getArgString(sRetArgsString, 1, 512, true);
				
				// Get the Card ID
				try{
					m_sCardId = sReturn.split(",")[0];
				}catch(Exception e){
					// Incorrect return
					return;
				}
				
				if(m_sPrevCardId.length() > 0){
					// Check if the card = previous card or not
					if(m_sCardId.equals(m_sPrevCardId) == false){
						// Not the same card
						// Write error log
						writeErrorLog("RwlPoll", iErrorCode, "Error Message: Incorrect Card, Orginal Card ID: " + m_sPrevCardId + ", Current Card ID: " + m_sCardId);
						
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("retry_please") + " (" + AppGlobal.g_oLang.get()._("octopus_no") + " " + m_sPrevCardId + ")");
						oFormDialogBox.show();
						
						m_oFrameOctopusOperation.hideCancelButton();
						
						if(m_sOperationType.equals(OPERATION_TYPE_LIST.read_card.name())){
							// First function, no need to perform any function again
						}else{
							// Retry is needed, need to perform RwlTxnAmt --> RwlPoll again
							// Add back RwlTxnAmt
							m_oFuncCallList.addFirst(FUNC_CALL_LIST.RwlTxnAmt.name());
							
							// Start process
							// First function arguments
							m_oCurrentStepArgs[0] = m_iOctopusTransactionValueInCent;
							m_oCurrentStepArgs[1] = -30000;
						}
						stepControl(null, m_oCurrentStepArgs);

						return;
					}
				}
				
				// Retrieve original remain amount
				m_dOrginalRemainAmount = new BigDecimal(iErrorCode).divide(new BigDecimal("10"));
				
				// Retrieve type
				m_sType = "";
				String sCustomerInfo = sReturn.split(",")[1];
//System.out.println("testtest ----------------------------------- " + sReturn);
				String[] sInfo = sCustomerInfo.split("-");
				if(sInfo.length > 0){
					m_sType = sInfo[0].trim();
//m_sType="1";
				}
				
				// Parse the data
				parseOctopusCardInfo(sReturn);

				// Write info log
				writeInfoLog("RwlPoll", iErrorCode, "Card ID: " + m_sCardId + ", Octopus Type: " + m_sType + ", Remaining value: " + m_dOrginalRemainAmount.toPlainString());
				
				m_oFuncCallList.removeFirst();
				if(m_sOperationType.equals(OPERATION_TYPE_LIST.read_card.name())){
					// For read card
					// 20190211, according to new Approval Requirement v20.1, add Octopus Type handling
					String sRemainAmount = "";
					if (!m_sType.equals("1"))
						sRemainAmount = m_dOrginalRemainAmount.setScale(1).toPlainString();
					m_oFrameOctopusOperation.showReadCardResultScreen(m_oFuncOctopus.getDeviceId(), m_sCardId, sRemainAmount, m_oLastOctopusTransactions);
						
					// Customer display
					m_oFuncOctopus.updateCustomerDisplayDataUpdateTimestamp();
					// 20190211, according to new Approval Requirement v20.1, add Octopus Type handling
					if (m_sType.equals("1"))
						m_oFuncOctopus.generateCustomerDisplayInterfaceFiles(FuncOctopus.CUST_DISPLAY_SHOW_AMOUNT_RESULT, null, BigDecimal.ZERO);
					else
						m_oFuncOctopus.generateCustomerDisplayInterfaceFiles(FuncOctopus.CUST_DISPLAY_SHOW_AMOUNT_RESULT, m_dOrginalRemainAmount, BigDecimal.ZERO);
					
					// Next step - RwlTxnAmt
					// 20190211, according to new Approval Requirement v20.1, add Octopus Type handling
					if (m_sType.equals("1")) {
						m_oCurrentStepArgs[0] = -30000;
						m_oCurrentStepArgs[1] = -30001;
					} else {
						m_oCurrentStepArgs[0] = -30000;
						m_oCurrentStepArgs[1] = iErrorCode;
					}
				}else{
					// Next step - RwlAddValue / RwlDeduct
					m_oCurrentStepArgs[0] = 0;
				}
				stepControl(null, m_oCurrentStepArgs);
			}else{
				// Error ocrur
				// Write error log
				if(iErrorCode != ERR_NO_CARD_DURING_POLL)
					writeErrorLog("RwlPoll", iErrorCode, "Error Message: " + m_sLastErrorMessage);
				
				// Finish showing this form
				if(m_bNeedPromptErrorForLastOperation){
					String sErrorString = m_sLastErrorMessage;
					if(m_sPrevCardId.length() > 0){
						sErrorString = sErrorString + System.lineSeparator() + System.lineSeparator() + "("
								+ AppGlobal.g_oLang.get()._("octopus_no") + " " + m_sPrevCardId + ")";
					}
					
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(sErrorString);
					oFormDialogBox.show();
				}

				// Check if previous card is existing
				// If yes, this is within the 20s loop for Error Code 100022
				if(m_bNeedRetryForLastOperation || m_sPrevCardId.length() > 0){
					if(m_sOperationType.equals(OPERATION_TYPE_LIST.read_card.name())){
						// First function, no need to perform any function again
					}else{
						// Retry is needed, need to perform RwlTxnAmt --> RwlPoll again, except Error Code = 100032
						if(iErrorCode != ERR_NO_CARD_DURING_POLL){
							// Add back RwlTxnAmt
							m_oFuncCallList.addFirst(FUNC_CALL_LIST.RwlTxnAmt.name());
						}
						
						// Start process
						// First function arguments
						m_oCurrentStepArgs[0] = m_iOctopusTransactionValueInCent;
						m_oCurrentStepArgs[1] = -30000;
					}
					stepControl(null, m_oCurrentStepArgs);
					
					return;
				}
				
				// No retry, abort operation
				finishOperation();
			}
			
			return;
		}
		
		if (!m_oFrameOctopusOperation.isCancelButtonHidden() && m_dtStartTime != null) {
			long diffMinutes = (AppGlobal.getCurrentTime(false).getMillis() - m_dtStartTime.getMillis()) / (60 * 1000) % 60;
			long iInterval = diffMinutes * 60 + (AppGlobal.getCurrentTime(false).getMillis() - m_dtStartTime.getMillis()) / 1000 % 60;
			// Only wait for 60s to read card
			if (iInterval > 60) {
				finishOperation();
				return;
			}
		}
		
		Object[] oParameters = new Object[2];
		oParameters[0] = SubCom;
		oParameters[1] = TimeOut;
		String sValue = m_oFuncOctopus.createWinFcnMapString(FUNC_CALL_LIST.RwlPoll.name(), oParameters);
		
		m_oFrameOctopusOperation.createForwardEvent(sValue, m_oFuncOctopus.getOctopusWaitTimeout() + m_oFuncOctopus.getOctopusPollTimeout(), m_oFuncOctopus.getSocketDelay());
	}
	
	private void RwlAddValue(Integer V, Integer AVType, String sResponse){
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			// Error code : 0 - Success
			//				1 - Error code
			// Pre-process the response
			String[] sRetArgsString = processArgString(sResponse);
			
			int iErrorCode = checkOctopusError(Integer.parseInt(sRetArgsString[0]));
			if(iErrorCode < ERR_FIRST_ERROR){
				// No error
				try{
					StringBuilder sb = new StringBuilder();
					sb.append("Succ:" + iErrorCode);
					sb.append(" Ret1:" + sRetArgsString[1]);
					AppGlobal.writeDebugLog("FromOctopusOperation", new Exception().getStackTrace()[0].getMethodName(), sb.toString());
				}catch(Exception e){
					AppGlobal.stack2Log(e);
				}
				
				m_oFrameOctopusOperation.setEmergencyPageTimer(false);
				
				// Retrieve current remain amount
				m_dCurrentRemainAmount = new BigDecimal(iErrorCode).divide(new BigDecimal("10"));
				
				// Retrieve transaction information
				parseOctopusTxnInfo(decodeString(decodeString(sRetArgsString[1], 64), 64));
				
				// Retrieve transaction time
				if(sRetArgsString.length > 2){
					m_sTransactionTimeString = decodeString(sRetArgsString[2], 19);
				}else{
					DateTime oCurrentDateTime = AppGlobal.getCurrentTime(false);
					DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
					m_sTransactionTimeString = fmt.print(oCurrentDateTime);
				}
				
				BigDecimal dTranAmount = new BigDecimal(m_iOctopusTransactionValueInCent);
				dTranAmount = dTranAmount.divide(new BigDecimal("10"));

				// Customer display
				m_oFuncOctopus.updateCustomerDisplayDataUpdateTimestamp();
				// 20190211, according to new Approval Requirement v20.1, add Octopus Type handling
				if (m_sType.equals("1"))
					m_oFuncOctopus.generateCustomerDisplayInterfaceFiles(FuncOctopus.CUST_DISPLAY_ADD_VALUE_RESULT, dTranAmount, null);
				else
					m_oFuncOctopus.generateCustomerDisplayInterfaceFiles(FuncOctopus.CUST_DISPLAY_ADD_VALUE_RESULT, dTranAmount, m_dCurrentRemainAmount);
				
				// Save transaction log
				this.writeTransactionLog("RwlAddValue");
				
				// Next step - RwlGetExtraInfo
				m_oFuncCallList.removeFirst();
				m_oCurrentStepArgs[0] = 0;
				m_oCurrentStepArgs[1] = 1;
				stepControl(null, m_oCurrentStepArgs);
			}else{
				// Error ocrur
				// Write error log
				BigDecimal dTranAmount = new BigDecimal(m_iOctopusTransactionValueInCent);
				dTranAmount = dTranAmount.divide(new BigDecimal("10"));
				writeErrorLog("RwlAddValue", iErrorCode, "Error Message: " + m_sLastErrorMessage + ", Card ID: " + this.getCardId() + ", Amount: " + dTranAmount.toPlainString());
				
				try{
					StringBuilder sb = new StringBuilder();
					sb.append("Err:" + iErrorCode);
					sb.append(" Ret1:" + sRetArgsString[1]);
					AppGlobal.writeDebugLog("FromOctopusOperation", new Exception().getStackTrace()[0].getMethodName(), sb.toString());
				}catch(Exception e){
					AppGlobal.stack2Log(e);
				}
				
				// Finish showing this form
				if(m_bNeedPromptErrorForLastOperation){
					String sErrorString = "";
					// Special Handling for Error Code = 100022
					// Enter 20s retry loop
					if(m_bNeedHideCancelButton){
						sErrorString = "+++ " + AppGlobal.g_oLang.get()._("do_not_cancel_the_txn") + " +++"
								+ System.lineSeparator() + m_sLastErrorMessage + System.lineSeparator()
								+ AppGlobal.g_oLang.get()._("please_request_customer_to_present_the_same_card")
								+ System.lineSeparator() + AppGlobal.g_oLang.get()._("again_to_complete_the_transaction");
						
						if(m_sPrevCardId.length() == 0)
							m_sPrevCardId = m_sCardId;
					}else{
						sErrorString = m_sLastErrorMessage;
					}
					
					if (m_sPrevCardId.length() > 0) {
						sErrorString = sErrorString + System.lineSeparator() + System.lineSeparator() + "("
								+ AppGlobal.g_oLang.get()._("octopus_no") + " " + m_sPrevCardId + ")";
					}
					
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(sErrorString);
					oFormDialogBox.show();
				}
				
				if(m_bTransactionLogFull){
					// Transaction log is full, force to RwlXFile and abort function
					m_oFuncCallList.clear();
					
					// Not prompt timeout error
					m_bNeedPromptTimeoutError = false;
					
					// Add step
					m_oFuncCallList.add(FUNC_CALL_LIST.RwlXFile.name());
					
					// Start process
					// First function arguments
					m_oCurrentStepArgs[0] = "";
					stepControl(null, m_oCurrentStepArgs);
					
					return;
				}else
				if(m_bNeedRetryForLastOperation){
					// Retry is needed, need to perform RwlTxnAmt --> RwlPoll --> RwlAddValue
					// Add back RwlTxnAmt and RwlPoll
					m_oFuncCallList.addFirst(FUNC_CALL_LIST.RwlPoll.name());
					m_oFuncCallList.addFirst(FUNC_CALL_LIST.RwlTxnAmt.name());
					
					// Hide the cancel button to FORCE retry with Octopus
					if(m_bNeedHideCancelButton){
						m_oFrameOctopusOperation.hideCancelButton();
						m_oFrameOctopusOperation.setEmergencyPageTimer(false);
					}
					
					// Start process
					// First function arguments
					m_oCurrentStepArgs[0] = m_iOctopusTransactionValueInCent;
					m_oCurrentStepArgs[1] = -30000;
					stepControl(null, m_oCurrentStepArgs);
					
					// Restart the auto quit timer
					m_dtStartTime = AppGlobal.getCurrentTime(false);
					
					return;
				}
				
				// No retry, abort operation
				finishOperation();
			}
			
			return;
		}
		
		// Hide the cancel button and start the timer to show the emergency page for this procedure
		m_oFrameOctopusOperation.hideCancelButton();
		m_oFrameOctopusOperation.setEmergencyPageTimer(true);
		
		// Skip the version checking
		m_oFrameOctopusOperation.setSkipVersionChecking(true);
		
		Object[] oParameters = new Object[3];
		oParameters[0] = V;
		oParameters[1] = AVType;
		// No reference for add value
		oParameters[2] = createAV();
		String sValue = m_oFuncOctopus.createWinFcnMapString(FUNC_CALL_LIST.RwlAddValue.name(), oParameters);
		
		try{
			StringBuilder sb = new StringBuilder();
			sb.append("Func:" + FUNC_CALL_LIST.RwlAddValue.name());
			AppGlobal.writeDebugLog("FromOctopusOperation", new Exception().getStackTrace()[0].getMethodName(), sb.toString());
		}catch(Exception e){
			AppGlobal.stack2Log(e);
		}
		
		m_oFrameOctopusOperation.createForwardEvent(sValue, m_oFuncOctopus.getOctopusWaitTimeout() + m_oFuncOctopus.getOctopusPollTimeout(), m_oFuncOctopus.getSocketDelay());
	}
	
	private void RwlDeduct(Integer V, String sResponse){
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			// Error code : 0 - Success
			//				1 - Error code
			// Pre-process the response
			String[] sRetArgsString = processArgString(sResponse);
			
			int iErrorCode = checkOctopusError(Integer.parseInt(sRetArgsString[0]));
			if(iErrorCode < ERR_FIRST_ERROR){
				// No error
				try{
					StringBuilder sb = new StringBuilder();
					sb.append("Succ:" + iErrorCode);
					sb.append(" Ret1:" + sRetArgsString[1]);
					AppGlobal.writeDebugLog("FromOctopusOperation", new Exception().getStackTrace()[0].getMethodName(), sb.toString());
				}catch(Exception e){
					AppGlobal.stack2Log(e);
				}
				
				m_oFrameOctopusOperation.setEmergencyPageTimer(false);
				
				// Retrieve current remain amount
				m_dCurrentRemainAmount = new BigDecimal(iErrorCode).divide(new BigDecimal("10"));
				
				// Retrieve transaction information
				parseOctopusTxnInfo(decodeString(decodeString(sRetArgsString[1], 64), 64));
				
				// Retrieve transaction time
				if(sRetArgsString.length > 2){
					m_sTransactionTimeString = decodeString(sRetArgsString[2], 19);
				}else{
					DateTime oCurrentDateTime = AppGlobal.getCurrentTime(false);
					DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
					m_sTransactionTimeString = fmt.print(oCurrentDateTime);
				}
				
				// According to customer request, no need to show result page for deduct Octopus amount
				/*
				BigDecimal dTranAmount = new BigDecimal(m_iOctopusTransactionValueInCent);
				dTranAmount = dTranAmount.divide(new BigDecimal("10"));
				m_oFrameOctopusOperation.showDeductValueResultScreen(dTranAmount.toPlainString(), m_dCurrentRemainAmount.toPlainString());
				*/
				
				// Customer display
				m_oFuncOctopus.updateCustomerDisplayDataUpdateTimestamp();
				// 20190211, according to new Approval Requirement v20.1, add Octopus Type handling
				if (m_sType.equals("1"))
					m_oFuncOctopus.generateCustomerDisplayInterfaceFiles(FuncOctopus.CUST_DISPLAY_SHOW_AMOUNT_RESULT, null, BigDecimal.ZERO);
				else
					m_oFuncOctopus.generateCustomerDisplayInterfaceFiles(FuncOctopus.CUST_DISPLAY_SHOW_AMOUNT_RESULT, m_dCurrentRemainAmount, BigDecimal.ZERO);
				
				// Save transaction log
				this.writeTransactionLog("RwlDeduct");
				
				// Next step - RwlGetExtraInfo
				m_oFuncCallList.removeFirst();
				m_oCurrentStepArgs[0] = 0;
				m_oCurrentStepArgs[1] = 1;
				stepControl(null, m_oCurrentStepArgs);
			}else{
				// Error ocrur
				// Write error log
				BigDecimal dTranAmount = new BigDecimal(m_iOctopusTransactionValueInCent);
				dTranAmount = dTranAmount.divide(new BigDecimal("10"));
				writeErrorLog("RwlDeduct", iErrorCode, "Error Message: " + m_sLastErrorMessage + ", Card ID: " + this.getCardId() + ", Amount: " + dTranAmount.toPlainString());

				try{
					StringBuilder sb = new StringBuilder();
					sb.append("Err:" + iErrorCode);
					sb.append(" Ret1:" + sRetArgsString[1]);
					AppGlobal.writeDebugLog("FromOctopusOperation", new Exception().getStackTrace()[0].getMethodName(), sb.toString());
				}catch(Exception e){
					AppGlobal.stack2Log(e);
				}
				
				// Finish showing this form
				if(m_bNeedPromptErrorForLastOperation){
					String sErrorString = "";
					// Special Handling for Error Code = 100022
					// Enter 20s retry loop
					if(m_bNeedHideCancelButton){
						sErrorString = "+++ " + AppGlobal.g_oLang.get()._("do_not_cancel_the_txn") + " +++"
								+ System.lineSeparator() + m_sLastErrorMessage + System.lineSeparator()
								+ AppGlobal.g_oLang.get()._("please_request_customer_to_present_the_same_card")
								+ System.lineSeparator()
								+ AppGlobal.g_oLang.get()._("again_to_complete_the_transaction");
						
						if(m_sPrevCardId.length() == 0)
							m_sPrevCardId = m_sCardId;
					}else{
						sErrorString = m_sLastErrorMessage;
					}
					
					if (m_sPrevCardId.length() > 0) {
						sErrorString = sErrorString + System.lineSeparator() + System.lineSeparator() + "("
								+ AppGlobal.g_oLang.get()._("octopus_no") + " " + m_sPrevCardId + ")";
					}
					
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(sErrorString);
					oFormDialogBox.show();
				}
				
				if(m_bTransactionLogFull){
					// Transaction log is full, force to RwlXFile and abort function
					m_oFuncCallList.clear();
					
					// Not prompt timeout error
					m_bNeedPromptTimeoutError = false;
					
					// Add step
					m_oFuncCallList.add(FUNC_CALL_LIST.RwlXFile.name());
					
					// Start process
					// First function arguments
					m_oCurrentStepArgs[0] = "";
					stepControl(null, m_oCurrentStepArgs);
					
					return;
				}else
				if(m_bNeedRetryForLastOperation){
					// Retry is needed, need to perform RwlTxnAmt --> RwlPoll --> RwlAddValue
					// Add back RwlTxnAmt and RwlPoll
					m_oFuncCallList.addFirst(FUNC_CALL_LIST.RwlPoll.name());
					m_oFuncCallList.addFirst(FUNC_CALL_LIST.RwlTxnAmt.name());
					
					// Hide the cancel button to FORCE retry with Octopus
					if(m_bNeedHideCancelButton){
						m_oFrameOctopusOperation.hideCancelButton();
						m_oFrameOctopusOperation.setEmergencyPageTimer(false);
					}
					
					// Start process
					// First function arguments
					m_oCurrentStepArgs[0] = m_iOctopusTransactionValueInCent;
					m_oCurrentStepArgs[1] = -30000;
					stepControl(null, m_oCurrentStepArgs);
					
					// Restart the auto quit timer
					m_dtStartTime = AppGlobal.getCurrentTime(false);
					
					return;
				}
				
				// No retry, abort operation
				finishOperation();
			}
			
			return;
		}
		
		// Hide the cancel button and start the timer to show the emergency page for this procedure
		m_oFrameOctopusOperation.hideCancelButton();
		m_oFrameOctopusOperation.setEmergencyPageTimer(true);
		
		// Skip the version checking
		m_oFrameOctopusOperation.setSkipVersionChecking(true);
		
		Object[] oParameters = new Object[2];
		oParameters[0] = V;
		oParameters[1] = createAV();
		String sValue = m_oFuncOctopus.createWinFcnMapString(FUNC_CALL_LIST.RwlDeduct.name(), oParameters);

		try{
			StringBuilder sb = new StringBuilder();
			sb.append("Func:" + FUNC_CALL_LIST.RwlDeduct.name());
			AppGlobal.writeDebugLog("FromOctopusOperation", new Exception().getStackTrace()[0].getMethodName(), sb.toString());
		}catch(Exception e){
			AppGlobal.stack2Log(e);
		}
		
		m_oFrameOctopusOperation.createForwardEvent(sValue, m_oFuncOctopus.getOctopusWaitTimeout() + m_oFuncOctopus.getOctopusPollTimeout(), m_oFuncOctopus.getSocketDelay());
	}
	
	private void RwlGetExtraInfo(String sResponse, int uiCom, int uiParam){
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			// Error code : 0 - Success
			//				1 - Error code
			// Pre-process the response
			String[] sRetArgsString = processArgString(sResponse);
			
			// For compatible issue, old device manager cannot support GetExtraInfo function.
			// So, cannot call checkOctopusError() function to ignore 100001 error return from old device manager
			//int iErrorCode = checkOctopusError(Integer.parseInt(sRetArgsString[0]));
			int iErrorCode = Integer.parseInt(sRetArgsString[0]);
			if(iErrorCode < ERR_FIRST_ERROR){
				// No error
				String sReturn = getArgString(sRetArgsString, 1, 512, true);
				parseOctopusExtraInfo(uiCom, sReturn);
				
				// Write info log
				writeInfoLog("RwlGetExtraInfo", iErrorCode, "uiCom: " + uiCom + ", uiParam: " + uiParam + ", ucResult: " + sReturn);
			}else{
				// Error ocrur
				// Write error log
				writeErrorLog("RwlGetExtraInfo", iErrorCode, "Error Message: " + m_sLastErrorMessage);
			}
			
			if(uiCom == 1 && m_sOperationType.equals(OPERATION_TYPE_LIST.add_value.name())){
				BigDecimal dTranAmount = new BigDecimal(m_iOctopusTransactionValueInCent);
				dTranAmount = dTranAmount.divide(new BigDecimal("10"));
				// 20190211, according to new Approval Requirement v20.1, add Octopus Type handling
				String sRemainAmount = "";
				if (!m_sType.equals("1"))
					sRemainAmount = StringLib.BigDecimalToString(m_dCurrentRemainAmount, 1);
				m_oFrameOctopusOperation.showAddValueResultScreen(StringLib.BigDecimalToString(dTranAmount, 1), sRemainAmount);
				
				// Save Octopus transaction
				m_oFuncOctopus.addOctopusTransaction(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getShopId(),
						AppGlobal.g_oFuncOutlet.get().getOutletId(), "", "", PosOctopusTransaction.TYPE_ADD_VALUE, m_sTransactionTimeString, dTranAmount,
						m_oFuncOctopus.getPaymentMethodId(), this.getDeviceId(), this.getUdsn()+"", this.getCardId(),
						this.getCardType(), this.getOriginalRemainAmount(), this.getCurrentRemainAmount(),
						AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncStation.get().getStationId(),
						m_sLastAddValueType, m_sLastAddValueDate);
			}
			
			// Next step - RwlGetExtraInfo / finish
			m_oFuncCallList.removeFirst();
			m_oCurrentStepArgs[0] = 1;
			int iCurrentParam = 1;
			for(HashMap<String, String> oLangInfo:AppGlobal.g_oSupportedLangList) {
				if(Integer.parseInt(oLangInfo.get("index")) == AppGlobal.g_oCurrentLangIndex.get()){
					if(oLangInfo.get("code") == "zh-hk"){
						iCurrentParam = 3;
					}
					break;
				}
			}
			m_oCurrentStepArgs[1] = iCurrentParam;
			stepControl(null, m_oCurrentStepArgs);
			
			return;
		}
		
		Object[] oParameters = new Object[2];
		oParameters[0] = uiCom;
		oParameters[1] = uiParam;
		String sValue = m_oFuncOctopus.createWinFcnMapString(FUNC_CALL_LIST.RwlGetExtraInfo.name(), oParameters);
		
		m_oFrameOctopusOperation.createForwardEvent(sValue, m_oFuncOctopus.getOctopusWaitTimeout() + m_oFuncOctopus.getOctopusPollTimeout(), m_oFuncOctopus.getSocketDelay());
	}
	
	private void setOctopusDisplay(String sMode, String sResponse){
		if(sMode.equals(DISPLAY_MODE.clear_all.name())){
			RwlTxnAmt(-30000, -30000, 0, 0, sResponse);
		}else
		if(sMode.equals(DISPLAY_MODE.idle.name())){
			RwlTxnAmt(0, -30000, 0, 0, sResponse);
		}else
		if(sMode.equals(DISPLAY_MODE.not_avail.name())){
			RwlTxnAmt(-30001, -30001, 0, 0, sResponse);
		}
	}
	
	private void RwlPortClose(String sResponse){
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			// Error code : 0 - Success
			//				1 - Error code
			// Pre-process the response
			String[] sRetArgsString = processArgString(sResponse);
			
			int iErrorCode = checkOctopusError(Integer.parseInt(sRetArgsString[0]));
			if(iErrorCode == 0){
				// No error
				// Go to next step
				m_oFuncCallList.removeFirst();
				
				// Next step - finish
				stepControl(null, m_oCurrentStepArgs);
			}else{
				// Error ocrur
				// Write error log
				writeErrorLog("RwlPortClose", iErrorCode, "Error Message: " + m_sLastErrorMessage);
				
				// Finish showing this form
				if(m_bNeedPromptErrorForLastOperation){
					// Prompt error
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(m_sLastErrorMessage);
					oFormDialogBox.show();
				}
				
				if(m_bNeedRetryForLastOperation){
					// Retry
					stepControl(null, m_oCurrentStepArgs);
					return;
				}
				
				// No retry, abort operation
				finishOperation();
			}
			
			return;
		}
		
		String sValue = m_oFuncOctopus.createWinFcnMapString(FUNC_CALL_LIST.RwlPortClose.name(), new Object[0]);
		
		// Only wait for 3 seconds for close port
		m_oFrameOctopusOperation.createForwardEvent(sValue, 3000, m_oFuncOctopus.getSocketDelay());
	}
	
	private void RwlXFile(String sFilename, String sResponse){
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			// Error code : 0 - Success
			//				1 - Error code
			// Pre-process the response
			String[] sRetArgsString = processArgString(sResponse);
			
			int iErrorCode = checkOctopusError(Integer.parseInt(sRetArgsString[0]));
			
			if(iErrorCode == 0){
				// No error
				
				StringBuilder sMessage = new StringBuilder();
				if(sRetArgsString.length > 1){
					sMessage.append("Filename: ");
					sMessage.append(getArgString(sRetArgsString, 1, 512, true));
				}
				writeInfoLog("RwlXFile", iErrorCode, sMessage.toString());
				
				if(sRetArgsString.length > 2){
					// Check if the file is uploaded successfully or not
					int iUploadRet = Integer.parseInt(sRetArgsString[2]);
					if(iUploadRet == 0){
						// Upload failed
						writeErrorLog("XXX", 999999, "Error Message: Fail to upload file to server");
						
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
						oFormDialogBox.setMessage(
								AppGlobal.g_oLang.get()._("transaction_file_is_generated") + System.lineSeparator()
										+ AppGlobal.g_oLang.get()._("but_fail_to_upload_file_to_server"));
						oFormDialogBox.show();
					}else{
						// Upload success
						writeInfoLog("XXX", 999999, "Message: Upload file successfully");
						
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
						oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("transaction_file_is_generated_and_uploaded"));
						oFormDialogBox.show();
					}
				}else{
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("transaction_file_is_generated"));
					oFormDialogBox.show();
				}
				
				m_bProcessSuccess = true;
			}else{
				// Error ocrur
				// Write error log
				writeErrorLog("RwlXFile", iErrorCode, "Error Message: " + m_sLastErrorMessage);
				
				// Finish showing this form
				if(m_bNeedPromptErrorForLastOperation){
					// Prompt error
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(m_sLastErrorMessage);
					oFormDialogBox.show();
				}
				
				m_bProcessSuccess = false;
			}
			
			// Must be the last function
			finishOperation();
			
			return;
		}
		
		Object[] oParameters = new Object[1];
		oParameters[0] = "";
		String sValue = m_oFuncOctopus.createWinFcnMapString(FUNC_CALL_LIST.RwlXFile.name(), oParameters);
		
		m_oFrameOctopusOperation.createForwardEvent(sValue, m_oFuncOctopus.getOctopusWaitTimeout() + m_oFuncOctopus.getOctopusPollTimeout(), m_oFuncOctopus.getSocketDelay());
	}
	
	private void sendResendLastResponseRequest(String sFuncCall){
		Object[] oParameters = new Object[1];
		oParameters[0] = sFuncCall;
		String sValue = m_oFuncOctopus.createWinFcnMapString(FUNC_CALL_LIST.ResendResponse.name(), oParameters);
		
		m_oFrameOctopusOperation.createForwardEvent(sValue, m_oFuncOctopus.getOctopusWaitTimeout() + m_oFuncOctopus.getOctopusPollTimeout(), m_oFuncOctopus.getSocketDelay());
	}
	
	private String createAV(){
		StringBuilder sb = new StringBuilder();
		
		try{
			// The follow calculation is from Gourmate, may be incorrect according to the spec of Octopus
			/*
			String sSrc = StringLib.fillZeroAtBegin(m_sCheckPrefixNo, 4);
			sSrc = sSrc.substring(sSrc.length()-4);
			int iAddinfo = Integer.parseInt(sSrc) & 0xffff;
			
			if(m_sOperationType.equals(OPERATION_TYPE_LIST.add_value.name())){
				char[] bAns = new char[6];
				
				bAns[1] = (char) (iAddinfo >> 8);
				bAns[2] = (char) (iAddinfo & 0xff);
				
				for (int j=0; j<bAns.length; j++) {
					sb.append(String.format("%02x", (int)bAns[j]));
				}
			}else
			if(m_sOperationType.equals(OPERATION_TYPE_LIST.deduct_value.name())){
				char[] bAns = new char[7];
				
				bAns[0] = (char) (iAddinfo >> 8);
				bAns[1] = (char) (iAddinfo & 0xff);
				
				bAns[5] = bAns[0];
				bAns[6] = bAns[1];
				
				for (int j=0; j<bAns.length; j++) {
					sb.append(String.format("%02x", (int)bAns[j]));
				}
			}
			*/
			String sSrc = StringLib.fillZeroAtBegin(m_oFuncOctopus.getNextStationTransactionNumber()+"", 4);
			String sFirstByte = sSrc.substring(sSrc.length()-4, sSrc.length()-2);
			String sSecondByte = sSrc.substring(sSrc.length()-2);
			if(m_sOperationType.equals(OPERATION_TYPE_LIST.add_value.name())){
				char[] bAns = new char[6];
				
				bAns[1] = (char) (Integer.parseInt(sFirstByte) & 0xff);
				bAns[2] = (char) (Integer.parseInt(sSecondByte) & 0xff);
				
				for (int j=0; j<bAns.length; j++) {
					sb.append(String.format("%02x", (int)bAns[j]));
				}
			}else
			if(m_sOperationType.equals(OPERATION_TYPE_LIST.deduct_value.name())){
				char[] bAns = new char[7];
				
				bAns[0] = (char) (Integer.parseInt(sFirstByte) & 0xff);
				bAns[1] = (char) (Integer.parseInt(sSecondByte) & 0xff);
				
				bAns[5] = bAns[0];
				bAns[6] = bAns[1];
				
				for (int j=0; j<bAns.length; j++) {
					sb.append(String.format("%02x", (int)bAns[j]));
				}
			}

		}catch(Exception e){
			AppGlobal.stack2Log(e);
		}
		
		return sb.toString();
	}
	
	private void parseOctopusTxnInfo(String sTxnInfo){
		String sTmp = "";
		
		try{
			// find OCTOPUS card id
			sTmp = String.format("%02x%02x%02x%02x", (int)sTxnInfo.charAt(12), (int)sTxnInfo.charAt(13), (int)sTxnInfo.charAt(14), (int)sTxnInfo.charAt(15));
			m_sCardId = Long.valueOf(sTmp, 16) + "";
	
			// find OCTOPUS device id
			m_sDeviceId = String.format("%02X%02X%02X", (int)sTxnInfo.charAt(8), (int)sTxnInfo.charAt(9), (int)sTxnInfo.charAt(10));
	
			// find OCTOPUS UDSN (usage data sequence number)
			sTmp = String.format("%02x%02x", (int)sTxnInfo.charAt(2), (int)sTxnInfo.charAt(3));
			m_lUDSN = Long.valueOf(sTmp, 16);
	
			// find remaining value
			int iRemainValue = 0;
			sTmp = String.format("%02x%02x", (int)sTxnInfo.charAt(26), (int)sTxnInfo.charAt(27));
			iRemainValue = Integer.valueOf(sTmp, 16);
	
			if (iRemainValue > 0x7FFF) { 
				iRemainValue *= -1;
				iRemainValue = iRemainValue & 0xFFFF;
				iRemainValue *= -1;
			}
			
			// Not use this value, use the return error code / 10 as remain value
			//m_dCurrentRemainAmount = (new BigDecimal(iRemainValue)).divide(new BigDecimal("10.0"));
			
		}catch(Exception e){
			AppGlobal.stack2Log(e);
		}
	}
	
	private void parseOctopusTimeVer(String sTimeVer){
		String sTmp = "";
		
		try{
			// find OCTOPUS device id
			sTmp = String.format("%02x%02x%02x", (int)sTimeVer.charAt(2), (int)sTimeVer.charAt(1), (int)sTimeVer.charAt(0));
			m_oFuncOctopus.setDeviceId(sTmp.toUpperCase());
		}catch(Exception e){
			AppGlobal.stack2Log(e);
		}
	}
	
	private String decodeString(String sSource, int iDecodeLen){
		StringBuilder sResult = new StringBuilder();
		String sTmp = "";
		int iTmp = 0;
		
		try{
			sSource = StringLib.fillZero(sSource, iDecodeLen*2+2);
			
			for (int i=0; i<iDecodeLen; i++) {
				sTmp = sSource.substring(i*2, (i*2)+2);
				if(sTmp.length() == 0)
					break;
				iTmp = Integer.valueOf(sTmp, 16);
				char c = (char)(iTmp & 0xFF);
				sResult.append(c);
			}
		}catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
		
		return sResult.toString();
	}
	
	private void parseOctopusCardInfo(String sReturn){
		
		String sValue = "";
		int iTmp;

		m_oLastOctopusTransactions.clear();
		
		try{
			StringTokenizer pch = new StringTokenizer(sReturn, ",");
			if(pch.hasMoreTokens()){
				//Card ID
				sValue = pch.nextToken();
				m_sCardId = sValue.substring(0, Math.min(20, sValue.length()));
				
				if(pch.hasMoreTokens()){
					//Customer Info
					sValue = pch.nextToken();
					m_sCustInfo = sValue.substring(0, Math.min(64, sValue.length()));
					
					if(pch.hasMoreTokens()){
						//Manufacture ID
						sValue = pch.nextToken();
						m_sManufactureID = sValue.substring(0, Math.min(20, sValue.length()));
						for(int i=0; i<4; i++){
							if(pch.hasMoreTokens() == false)
								break;
							
							ClsOctopusTransaction oClsOctopusTransaction = new ClsOctopusTransaction();
							
							// Service Provider ID
							sValue = pch.nextToken();
							oClsOctopusTransaction.setProviderId(sValue.substring(0, Math.min(3, sValue.length())));
							
							if(pch.hasMoreTokens() == false)
								break;
							
							// Transaction amount
							sValue = pch.nextToken();
							BigDecimal dAmount = new BigDecimal(sValue);
							dAmount = dAmount.divide(new BigDecimal("10.0"));
							oClsOctopusTransaction.setTransactionAmount(dAmount);
							
							if(pch.hasMoreTokens() == false)
								break;
							
							// Transaction time
							sValue = pch.nextToken();
							Long lTransTime = Long.parseLong(sValue);
							
							//The time is related to 1/1/2000 midnight GMT
							String sRefTime = "1/1/00 00:00:00";
							DateTimeFormatter df = DateTimeFormat.forPattern("MM/dd/yy HH:mm:ss");
							DateTime dtRefDate = df.withZoneUTC().parseDateTime(sRefTime); 
							long t = dtRefDate.getMillis();
							t = t + (lTransTime * 1000);
							DateTime dtTransactionDate = AppGlobal.convertTimeToLocal(new DateTime(t, DateTimeZone.UTC));
							oClsOctopusTransaction.setTransactionTime(df.print(dtTransactionDate));
							
							if(pch.hasMoreTokens() == false)
								break;
							
							// Machine ID
							sValue = pch.nextToken();
							iTmp = Integer.parseInt(sValue.substring(0, Math.min(5, sValue.length())));
							oClsOctopusTransaction.setMachineId(Integer.toHexString(iTmp).toUpperCase());
							
							if(pch.hasMoreTokens() == false)
								break;
							
							// Service Info
							sValue = pch.nextToken();
							oClsOctopusTransaction.setServiceInfo(sValue.substring(0, Math.min(10, sValue.length())));
							
							m_oLastOctopusTransactions.add(oClsOctopusTransaction);
						}
					}
				}
			}
		}catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	private void parseOctopusExtraInfo(int uiCom, String sReturn){
		
		String sValue = "";
		
		try{
			if(uiCom == 0){
				m_sLastAddValueType = "";
				m_sLastAddValueDate = "";
				
				StringTokenizer pch = new StringTokenizer(sReturn, ",");
				if(pch.hasMoreTokens()){
					// Last Add Value Date
					sValue = pch.nextToken();
					m_sLastAddValueDate = sValue.substring(0, Math.min(10, sValue.length()));
					
					if(pch.hasMoreTokens()){
						// Last Add Value Type
						sValue = pch.nextToken();
						int iAddValueType = 0;
						try{
							iAddValueType = Integer.parseInt(sValue.substring(0, Math.min(1, sValue.length())));
						}catch(NumberFormatException e){
							AppGlobal.stack2Log(e);
						}
						
						switch (iAddValueType) {
						case 1:
							m_sLastAddValueType = "CASH";
							break;
						case 2:
							m_sLastAddValueType = "ONLINE";
							break;
						case 4:
							m_sLastAddValueType = "AAVS";
							break;
						default:
							break;
						}
					}
				}
			}else{
				// Get Octopus alert message
				m_sOctopusAlertMessage = sReturn;
			}
		}catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	private int checkOctopusError(int iErrorCode){
		
		m_sLastErrorMessage = "";
		m_bNeedRetryForLastOperation = false;
		m_bNeedHideCancelButton = false;
		m_bNeedPromptErrorForLastOperation = false;
		m_bTransactionLogFull = false;
		
		switch (iErrorCode) {
		case ERR_NOT_CONNECT:
			// Communication error, R/W not connected
			m_oFuncOctopus.setOctopusConnected(false);

			m_bNeedRetryForLastOperation = false;
			m_bNeedPromptErrorForLastOperation = true;
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("mop_connection_failure");
			break;
		case ERR_INVALID_RESPONSE:
			// Invalid response from R/W
			m_oFuncOctopus.setOctopusConnected(false);

			m_bNeedRetryForLastOperation = false;
			m_bNeedPromptErrorForLastOperation = true;
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("mop_connection_failure");
			break;
		case ERR_READ_CARD_ERROR:
			// Card read error
			m_bNeedRetryForLastOperation = true;
			m_bNeedPromptErrorForLastOperation = true;
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("read_card_error_retry_please");
			break;
		case ERR_WRITE_CARD_ERROR:
			// Card write error
			m_bNeedRetryForLastOperation = true;
			m_bNeedPromptErrorForLastOperation = true;
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("read_card_error_retry_please");
			break;
		case ERR_INVALID_CARD:
			// Card blocked
			m_bNeedRetryForLastOperation = false;
			m_bNeedPromptErrorForLastOperation = true;
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("invalid_card");
			break;
		case ERR_NO_CARD_AFTER_POLL:
			// No Card found after Poll (deduct/add)
			m_bNeedRetryForLastOperation = true;
			m_bNeedPromptErrorForLastOperation = true;
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("present_card_again_retry_please");
			break;
		case ERR_INVALID_OCTOPUS:
			// Invalid Octopus
			m_bNeedRetryForLastOperation = false;
			m_bNeedPromptErrorForLastOperation = true;
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("invalid_octopus") + System.lineSeparator()
					+ AppGlobal.g_oLang.get()._("please_contact_mtr_customer_service_center");
			break;
		case ERR_INCOMPLETE_TRANSACTION:
			// Must Retry, Incomplete transaction
			m_bNeedRetryForLastOperation = true;
			m_bNeedPromptErrorForLastOperation = true;
			m_bNeedHideCancelButton = true;
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("transaction_incomplete");
			break;
		case ERR_CSC_INVALID_CARD:
			// CSC blocked by this call
			m_bNeedRetryForLastOperation = false;
			m_bNeedPromptErrorForLastOperation = true;
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("invalid_card");
			break;
		case ERR_INCOMPLETE_TRANSACTION2:
			// Must Retry, Incomplete transaction
			m_bNeedRetryForLastOperation = true;
			m_bNeedPromptErrorForLastOperation = true;
			m_bNeedHideCancelButton = true;
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("incomplete_transaction") + ", "
					+ AppGlobal.g_oLang.get()._("retry_please");
			break;
		case ERR_NO_CARD_DURING_POLL:
			// Time out polling Card/No card found
			m_bNeedRetryForLastOperation = true;
			m_bNeedPromptErrorForLastOperation = false;
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("present_card_again_retry_please");
			break;
		case ERR_CARD_AUTH_ERROR:
			// Card authentication error
			m_bNeedRetryForLastOperation = true;
			m_bNeedPromptErrorForLastOperation = true;
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("read_card_error_retry_please");
			break;
		case ERR_INSUFFICIENT_VALUE:
			// Insufficient fund
			m_bNeedRetryForLastOperation = false;
			m_bNeedPromptErrorForLastOperation = true;
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("insufficient_value");
			break;
		case ERR_REMAIN_VALUE_EXCEED_LIMIT:
			// Remaining value exceed limit
			m_bNeedRetryForLastOperation = false;
			m_bNeedPromptErrorForLastOperation = true;
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("stored_value_exceed_limit");
			break;
		case ERR_ADD_VALUE_EXCEEDED:
			m_bNeedRetryForLastOperation = false;
			m_bNeedPromptErrorForLastOperation = true;
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("add_value_quota_exceeded");
			break;
		case ERR_INVALID_POS_CONTROLLER_ID:
			m_bNeedRetryForLastOperation = false;
			m_bNeedPromptErrorForLastOperation = true;
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("invalid_pos_controller_id");
			break;
		default:
			// Error with code
			m_bNeedRetryForLastOperation = false;
			m_bNeedPromptErrorForLastOperation = true;
			m_sLastErrorMessage = AppGlobal.g_oLang.get()._("octopus_other_error") + " ("
					+ AppGlobal.g_oLang.get()._("octopus_error_code") + " " + iErrorCode + ")";
			break;
		}
		
		return iErrorCode;
	}
	
	private void writeInfoLog(String sFunction, int iErrorCode, String sMessage){
		try{
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId()+"", AppGlobal.g_oFuncUser.get().getUserId()+"",
					"OCTOPUS - Success - Function: " + m_sOperationType + ", Protocol: " + sFunction + ", Return code: " + iErrorCode + ", " + sMessage);
		}catch(Exception e){
			AppGlobal.stack2Log(e);
		}
	}
	
	private void writeErrorLog(String sFunction, int iErrorCode, String sMessage){
		try{
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId()+"", AppGlobal.g_oFuncUser.get().getUserId()+"",
					"OCTOPUS - Error - Function: " + m_sOperationType + ", Protocol: " + sFunction + ", Return code: " + iErrorCode + ", " + sMessage);
		}catch(Exception e){
			AppGlobal.stack2Log(e);
		}
	}
	
	private void writeTransactionLog(String sFunction){
		try{
			BigDecimal dTranAmount = new BigDecimal(m_iOctopusTransactionValueInCent);
			dTranAmount = dTranAmount.divide(new BigDecimal("10"));
			if(m_sOperationType.equals(OPERATION_TYPE_LIST.deduct_value.name())){
				// Deduct value
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId()+"", AppGlobal.g_oFuncUser.get().getUserId()+"",
						"OCTOPUS - Success - Function: " + m_sOperationType + ", " +
						"Protocol: " + sFunction + ", " +
						"Transaction time: " + m_sTransactionTimeString + ", " +
						"Card ID: " + this.getCardId() + ", " +
						"Card Type: " + this.getCardType() + ", " +
						"Transaction no.: " + m_oFuncOctopus.getNextStationTransactionNumber() + ", " +
						"Old balance: " + this.getOriginalRemainAmount() + ", " +
						"Deduct value: " + dTranAmount.toPlainString() + ", " +
						"Remaining value: " + this.getCurrentRemainAmount() + ", " +
						"Device ID: " + this.getDeviceId() + ", " +
						"UDSN: " + this.getUdsn());
			}else{
				// Add value
				AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId()+"", AppGlobal.g_oFuncUser.get().getUserId()+"",
						"OCTOPUS - Success - Function: " + m_sOperationType + ", " +
						"Protocol: " + sFunction + ", " +
						"Transaction time: " + m_sTransactionTimeString + ", " +
						"Card ID: " + this.getCardId() + ", " +
						"Card Type: " + this.getCardType() + ", " +
						"Transaction no.: " + m_oFuncOctopus.getNextStationTransactionNumber() + ", " +
						"Old balance: " + this.getOriginalRemainAmount() + ", " +
						"Add value: " + dTranAmount.toPlainString() + ", " +
						"Remaining value: " + this.getCurrentRemainAmount() + ", " +
						"Device ID: " + this.getDeviceId() + ", " +
						"UDSN: " + this.getUdsn());
			}
		}catch(Exception e){
			AppGlobal.stack2Log(e);
		}
	}
	
	public boolean isProcessSuccess(){
		return m_bProcessSuccess;
	}
	
	public String getCardId(){
		return m_sCardId;
	}
	
	public String getDeviceId(){
		if(m_oFuncOctopus.getDeviceId().length() > 0)
			return m_oFuncOctopus.getDeviceId();
		else
			return m_sDeviceId;
	}
	
	public long getUdsn(){
		return m_lUDSN;
	}
	
	public BigDecimal getOriginalRemainAmount(){
		return m_dOrginalRemainAmount;
	}
	
	public BigDecimal getCurrentRemainAmount(){
		return m_dCurrentRemainAmount;
	}
	
	public String getCardType(){
		return m_sType;
	}
	
	public String getLastAddValueType(){
		return m_sLastAddValueType;
	}
	
	public String getLastAddValueDate(){
		return m_sLastAddValueDate;
	}
	
	public String getTransactionTime(){
		return m_sTransactionTimeString;
	}
	
	public void finishOperation(){

		if(m_sOperationType.equals(OPERATION_TYPE_LIST.close.name()) == false &&
				m_sOperationType.equals(OPERATION_TYPE_LIST.set_display.name()) == false &&
				m_sOperationType.equals(OPERATION_TYPE_LIST.deduct_value.name()) == false){
			setOctopusDisplay(DISPLAY_MODE.idle.name(), null);
		}
		
		// Set the flag to auto clear Octopus display for deduct value
		if(m_sOperationType.equals(OPERATION_TYPE_LIST.deduct_value.name())){
			m_oFuncOctopus.setNeedAutoClearDisplay(true);
		}
		
		// Set the connection timeout to device manager to prevent timeout loop
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(0);
		
		// Finish showing this form
		this.finishShow();
	}
	
	private FuncUser askUserPassword(String sTitle) {
		List<String> oMsgList = new ArrayList<String>();
		oMsgList.add(AppGlobal.g_oLang.get()._("user_id"));
		oMsgList.add(AppGlobal.g_oLang.get()._("password"));

		FormInputBox oInputBox = new FormInputBox(this);
		oInputBox.initWithInputNum(oMsgList.size());
		oInputBox.setTitle(sTitle);
		oInputBox.setMessages(oMsgList);
		oInputBox.setInputBoxSecurity(1, true);
		oInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.NUMBER);
		oInputBox.show();
		
		//Retrieve Manager ID and PW
		String sTempLogin = oInputBox.getInputValue(0);
		String sTempPw = oInputBox.getInputValue(1);
		String sTempCardNo = oInputBox.getSwipeCardValue();
		
		if((sTempLogin.isEmpty() || sTempPw.isEmpty()) && sTempCardNo.isEmpty())
			return null;
		
		FuncUser oTempUser = new FuncUser();
		if(sTempCardNo.length() > 0){
			// Remove \r and \n in the return card no.
			sTempCardNo = sTempCardNo.replace("\r", "").replace("\n", "");
			
			// Validate the card no.
			//Check User Login and Password
			if(oTempUser.isValidUserByCardNum(sTempCardNo) == false) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
	            oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
	            oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_login"));
	            oFormDialogBox.show();
	            oFormDialogBox = null;
				return null;
			}			
		}else{
			//Check User Login and Password
			if(oTempUser.isValidUser(sTempLogin, sTempPw) == false) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
	            oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
	            oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_login"));
	            oFormDialogBox.show();
	            oFormDialogBox = null;
				return null;
			}
		}
		
		return oTempUser;
	}
	
	@Override
	public void FrameOctopusOperation_clickCancel(boolean bLoginRequired) {
		// Cancel by user
		if(m_bProcessSuccess == false)
			m_bCancelByUser = true;
		else{
			// Cancel is clicked in result screen, so can close directly
			finishOperation();
		}
		
		if(bLoginRequired){
			FuncUser oFuncUser = null;
			do{
				oFuncUser = askUserPassword(AppGlobal.g_oLang.get()._("ask_approval"));
			}while(oFuncUser == null);
			
			AppGlobal.writeActionLog(AppGlobal.g_oFuncStation.get().getStationId()+"", oFuncUser.getUserId()+"",
					"OCTOPUS - Cancel - Function: " + m_sOperationType + ", Error message: no octopus response received");
			
			finishOperation();
		}
	}

	@Override
	public void FrameOctopusOperation_forward(String sResponse) {
		// Set the connection timeout to device manager to prevent timeout loop
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(0);
		
		stepControl(sResponse, m_oCurrentStepArgs);
	}

	@Override
	public void FrameOctopusOperation_disconnect() {
		// Disconnect
		if(m_bProcessSuccess == false){
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("disconnect_from_device"));
			oFormDialogBox.show();
			
			try{
				StringBuilder sb = new StringBuilder();
				sb.append("Disconnect during operation");
				AppGlobal.writeDebugLog("FromOctopusOperation", new Exception().getStackTrace()[0].getMethodName(), sb.toString());
			}catch(Exception e){
				AppGlobal.stack2Log(e);
			}
			
			finishOperation();
		}
	}
	
	@Override
	public void FrameOctopusOperation_timeout() {
		// Timeout
		if(m_bProcessSuccess == false){
			if(m_bNeedPromptTimeoutError){
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("timeout"));
				oFormDialogBox.show();
			}
			
			try{
				StringBuilder sb = new StringBuilder();
				sb.append("Timeout during operation");
				AppGlobal.writeDebugLog("FromOctopusOperation", new Exception().getStackTrace()[0].getMethodName(), sb.toString());
			}catch(Exception e){
				AppGlobal.stack2Log(e);
			}
			
			finishOperation();
		}
	}

	@Override
	public void FrameOctopusOperation_handleNoResponse() {
		if(m_oFuncCallList.isEmpty())
			return;
		
		// Send re-send last response request
		String sCurrentStep = m_oFuncCallList.getFirst();
		this.sendResendLastResponseRequest(sCurrentStep);
		
		m_oFrameOctopusOperation.setEmergencyPageTimer(true);
	}
}
