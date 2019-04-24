package app;

import java.util.HashMap;
import java.util.LinkedList;

import commonui.FormDialogBox;
import core.Controller;
import externallib.StringLib;

import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormSmartCardOperation extends VirtualUIForm implements FrameSmartCardOperationListener {
	
	public enum FUNC_CALL_LIST{rf_init, rf_comm_check, rf_setbright, rf_load_key_hex, rf_card, rf_read_hex, rf_authentication, rf_halt, rf_exit, rf_beep};
	public enum OPERATION_TYPE_LIST{init, read_card, read_serial, close};
	
	static public final int SC_LOAD_MODE0 = 0;
	static public final int SC_ALL_MODE = 1;
	
	static public final int SC_BAUD_RATE = 115200;
	
	TemplateBuilder m_oTemplateBuilder;

	private FrameSmartCardOperation m_oFrameSmartCardOperation;
	
	static private int MAX_ARGS_NO = 10;
	private Object[] m_oCurrentStepArgs;
	
	private String m_sLastErrorMessage;
	
	// Internal usage
	private FuncSmartCard m_oFuncSmartCard;
	private int m_iLoadKeyIndex;
	private LinkedList<String> m_sReadField;
	private String m_sLastSerialNo;
	private int m_iLastSector;
	private HashMap<Integer, String> m_sLastReadBlockData;
	private boolean m_bNeedAuth;
	
	private boolean m_bCancelByUser;
	private boolean m_bProcessSuccess;
	private String m_sOperationType;		// Initial smart card; Close smart card
	private LinkedList<String> m_oFuncCallList;
	
	public FormSmartCardOperation(FuncSmartCard oFuncSmartCard, Controller oParentController){
		super(oParentController);
		
		m_bCancelByUser = false;
		m_bProcessSuccess = false;
		m_oFuncCallList = new LinkedList<String>();
		m_oCurrentStepArgs = new Object[MAX_ARGS_NO];	// Max no. of args for function = 10
				
		m_oFuncSmartCard = oFuncSmartCard;
		
		m_iLoadKeyIndex = 0;
		m_sReadField = new LinkedList<String>();
		m_sLastSerialNo = "";
		m_iLastSector = -1;
		m_sLastReadBlockData = new HashMap<Integer, String>();
		m_bNeedAuth = false;
	}
	
	public boolean initForSmartCardInitialization(){	
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmSmartCardOperation.xml");
		
		// Login Frame
		m_oFrameSmartCardOperation = new FrameSmartCardOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameSmartCardOperation, "fraSmartCardOperation");
		m_oFrameSmartCardOperation.init();
		m_oFrameSmartCardOperation.showInitScreen();
		// Add listener;
		m_oFrameSmartCardOperation.addListener(this);
		this.attachChild(m_oFrameSmartCardOperation);
		
		m_sOperationType = OPERATION_TYPE_LIST.init.name();
		
		// Add step
		m_oFuncCallList.add(FUNC_CALL_LIST.rf_init.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.rf_comm_check.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.rf_setbright.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.rf_load_key_hex.name());
		
		// Start process
		m_oCurrentStepArgs[0] = m_oFuncSmartCard.getReaderInf();
		m_oCurrentStepArgs[1] = SC_BAUD_RATE;
		stepControl(null, m_oCurrentStepArgs);
		
		return true;
	}
	
	public boolean initForReadCard(){	
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmSmartCardOperation.xml");
		
		// Login Frame
		m_oFrameSmartCardOperation = new FrameSmartCardOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameSmartCardOperation, "fraSmartCardOperation");
		m_oFrameSmartCardOperation.init();
		m_oFrameSmartCardOperation.showReadCardScreen();
		// Add listener;
		m_oFrameSmartCardOperation.addListener(this);
		this.attachChild(m_oFrameSmartCardOperation);
		
		m_sOperationType = OPERATION_TYPE_LIST.read_card.name();
		
		// Add step
		m_oFuncCallList.add(FUNC_CALL_LIST.rf_card.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.rf_card.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.rf_read_hex.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.rf_beep.name());
		
		// Add read card field list
		m_sReadField.add("issue_date");
		m_sReadField.add("expiry_date");
		m_sReadField.add("merchant_id");
		m_sReadField.add("card_type");
		m_sReadField.add("card_no");
		m_sReadField.add("card_status");
		m_sReadField.add("member_no1");
		m_sReadField.add("member_no2");
		m_sReadField.add("member_type");
		m_sReadField.add("member_name1");
		m_sReadField.add("member_name2");
		m_sReadField.add("cash_amt");
		m_sReadField.add("free_amt");
		m_sReadField.add("bonus");
		m_sReadField.add("bonus_exp_date");
		m_sReadField.add("last_earn");
		m_sReadField.add("last_redeem");
		m_sReadField.add("last_visit");
		m_sReadField.add("visit_cnt");
		
		// Start process
		// First function arguments
		m_oCurrentStepArgs[0] = m_oFuncSmartCard.getDevice();
		m_oCurrentStepArgs[1] = SC_ALL_MODE;
		m_oCurrentStepArgs[2] = true;	// Allow retry for first rf_read
		stepControl(null, m_oCurrentStepArgs);
		
		return true;
	}
	
	public void initForReadCardSerial(){
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmSmartCardOperation.xml");
		
		// Login Frame
		m_oFrameSmartCardOperation = new FrameSmartCardOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameSmartCardOperation, "fraSmartCardOperation_login");
		m_oFrameSmartCardOperation.init();
		m_oFrameSmartCardOperation.showReadCardForLoginScreen();
		// Add listener;
		m_oFrameSmartCardOperation.addListener(this);
		this.attachChild(m_oFrameSmartCardOperation);
		
		m_sOperationType = OPERATION_TYPE_LIST.read_serial.name();
		
		// Add step
		m_oFuncCallList.add(FUNC_CALL_LIST.rf_card.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.rf_beep.name());
		
		// Start process
		// First function arguments
		m_oCurrentStepArgs[0] = m_oFuncSmartCard.getDevice();
		m_oCurrentStepArgs[1] = SC_ALL_MODE;
		m_oCurrentStepArgs[2] = true;	// Allow retry for first rf_read
		stepControl(null, m_oCurrentStepArgs);
	}
	
	public boolean initForCloseSmartCard(){	
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmSmartCardOperation.xml");
		
		// Login Frame
		m_oFrameSmartCardOperation = new FrameSmartCardOperation();
		m_oTemplateBuilder.buildFrame(m_oFrameSmartCardOperation, "fraSmartCardOperation");
		m_oFrameSmartCardOperation.init();
		m_oFrameSmartCardOperation.showStopScreen();
		// Add listener;
		m_oFrameSmartCardOperation.addListener(this);
		this.attachChild(m_oFrameSmartCardOperation);
		
		m_sOperationType = OPERATION_TYPE_LIST.close.name();
		
		// Add step
		m_oFuncCallList.add(FUNC_CALL_LIST.rf_load_key_hex.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.rf_halt.name());
		m_oFuncCallList.add(FUNC_CALL_LIST.rf_exit.name());
		
		// Start process
		// First function arguments
		m_oCurrentStepArgs[0] = m_oFuncSmartCard.getDevice();
		m_oCurrentStepArgs[1] = SC_LOAD_MODE0;
		m_oCurrentStepArgs[2] = m_iLoadKeyIndex;
		// Dummy key
		m_oCurrentStepArgs[3] = "EFEFEFEFEFEF";
		stepControl(null, m_oCurrentStepArgs);
		
		return true;
	}
	
	private void stepControl(String sResponse, Object[] oNextStepFunctionArgs){
		
		if(m_oFuncCallList.isEmpty()){
			// Finish operation
			m_bProcessSuccess = true;
			
			if(m_sOperationType.equals(OPERATION_TYPE_LIST.read_card.name())) {
				// No need to quit the form
				
			} else {
				finishOperation();
			}
			
			return;
		}
		
		String sCurrentStep = m_oFuncCallList.getFirst();
		
		// Call function
		if(sCurrentStep.equals(FUNC_CALL_LIST.rf_init.name())){
			this.wrf_init((String)oNextStepFunctionArgs[0], (Integer)oNextStepFunctionArgs[1], sResponse);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.rf_comm_check.name())){
			this.wrf_comm_check((Integer)oNextStepFunctionArgs[0], (Integer)oNextStepFunctionArgs[1], sResponse);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.rf_setbright.name())){
			this.wrf_setbright((Integer)oNextStepFunctionArgs[0], (Integer)oNextStepFunctionArgs[1], sResponse);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.rf_load_key_hex.name())){
			this.wrf_load_key_hex((Integer)oNextStepFunctionArgs[0], (Integer)oNextStepFunctionArgs[1], (Integer)oNextStepFunctionArgs[2], (String)oNextStepFunctionArgs[3], sResponse);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.rf_card.name())){
			this.wrf_card((Integer)oNextStepFunctionArgs[0], (Integer)oNextStepFunctionArgs[1], (Boolean)oNextStepFunctionArgs[2], sResponse);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.rf_read_hex.name())){
			this.wrf_read_hex((Integer)oNextStepFunctionArgs[0], sResponse);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.rf_authentication.name())){
			this.wrf_authentication((Integer)oNextStepFunctionArgs[0], (Integer)oNextStepFunctionArgs[1], (Integer)oNextStepFunctionArgs[2], sResponse);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.rf_halt.name())){
			this.wrf_halt((Integer)oNextStepFunctionArgs[0], sResponse);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.rf_exit.name())){
			this.wrf_exit((Integer)oNextStepFunctionArgs[0], sResponse);
		}else
		if(sCurrentStep.equals(FUNC_CALL_LIST.rf_beep.name())){
			this.wrf_beep((Integer)oNextStepFunctionArgs[0], (Integer)oNextStepFunctionArgs[1], sResponse);
		}
	}
	
	private String createWinFcnMapString(String sType, String sArgString){
		return String.format("\001win_fcn_map\002%s\002%s\004", sType, sArgString);
	}
	
	private String createArgString(Object[] oParameters){
		StringBuilder sArgString = new StringBuilder();
		
		for(int i=0; i<oParameters.length; i++){
			if(oParameters[i] != null){
				if(i != 0){
					sArgString.append(";");
				}
				
				if(oParameters[i] instanceof String) {
					sArgString.append(StringLib.stringToHex((String) oParameters[i]));
				} else {
					sArgString.append(oParameters[i]);
				}
			}
		}
		sArgString.append(";");
		
		return sArgString.toString();
	}
	
	// sArgType : x1x2...
	// x1 : Argument 1
	//		- i : integer
	//		- s : string
	private String[] processArgString(String sResponse){

		// Check if the packet begins with SOH and ends with EOT
		byte[] oFullResponseBytes = sResponse.getBytes();
		int iLength = 0;
		for(int i=0; i<oFullResponseBytes.length; i++){
			if(oFullResponseBytes[i] == 0){
				// End of packet
				break;
			}
			iLength++;
		}
		
		if(iLength == 0)
			return null;
		
		byte[] oProcessedResponseBytes = new byte[iLength];
		// copy a to result
		System.arraycopy(oFullResponseBytes, 0, oProcessedResponseBytes, 0, iLength);
		
		// Convert the byte array to Object array
		String[] oReturnArgs = (new String(oProcessedResponseBytes)).split(";");
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
	
	private long getArgUnsignedLong(String[] sRetArgsString, int iIndex, int iLength){
		if(iIndex >= sRetArgsString.length)
			return 0;
		
		String sArg = sRetArgsString[iIndex];
		return Long.parseLong(sArg);
	}
	
	private void wrf_init(String sFilename, int iBaud, String sResponse){
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			// Error code : 0 - Success
			//				1 - Error code
			// Pre-process the response
			String[] sRetArgsString = processArgString(sResponse);
			
			int iErrorCode = Integer.parseInt(sRetArgsString[0]);
			if(iErrorCode > 0){
				// No error
				
				// Get the device ID
				m_oFuncSmartCard.setDevice(iErrorCode);
				
				// Go to next step
				m_oFuncCallList.removeFirst();
				
				// Next step - wrf_comm_check
				m_oCurrentStepArgs[0] = m_oFuncSmartCard.getDevice();
				m_oCurrentStepArgs[1] = 0;
				stepControl(null, m_oCurrentStepArgs);
			}else{
				// Error ocrur
				// Write error log
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("smart_card_reader_initialization_error");
				writeErrorLog("wrf_init", iErrorCode);
				
				// Finish showing this form
				// Prompt error
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(m_sLastErrorMessage);
				oFormDialogBox.show();
				
				// No retry, abort operation
				finishOperation();
			}
			
			return;
		}
		
		Object[] oParameters = new Object[2];
		oParameters[0] = sFilename;
		oParameters[1] = iBaud;
		String sArgString = createArgString(oParameters);
		String sValue = createWinFcnMapString(FUNC_CALL_LIST.rf_init.name(), sArgString);
		
		m_oFrameSmartCardOperation.createForwardEvent(sValue, m_oFuncSmartCard.getTimeout(), m_oFuncSmartCard.getSocketDelay());
	}
	
	private void wrf_comm_check(int iDevice, int iMode, String sResponse){
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			// Error code : 0 - Success
			//				1 - Error code
			// Pre-process the response
			String[] sRetArgsString = processArgString(sResponse);
			
			int iErrorCode = Integer.parseInt(sRetArgsString[0]);
			if(iErrorCode == 0){
				// No error
				// Go to next step
				m_oFuncCallList.removeFirst();
				
				// Next step - wrf_setbright
				m_oCurrentStepArgs[0] = m_oFuncSmartCard.getDevice();
				m_oCurrentStepArgs[1] = 15;
				stepControl(null, m_oCurrentStepArgs);
			}else{
				// Error ocrur
				// Write error log
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("smart_card_reader_communication_checking_fail") + "!"
						+ System.lineSeparator() + AppGlobal.g_oLang.get()._("please_check_if_smart_card_reader_is_power_off");
				writeErrorLog("wrf_comm_check", iErrorCode);
				
				// Finish showing this form
				// Prompt error
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(m_sLastErrorMessage);
				oFormDialogBox.show();
				
				// No retry, abort operation
				finishOperation();
			}
			
			return;
		}
		
		Object[] oParameters = new Object[2];
		oParameters[0] = iDevice;
		oParameters[1] = iMode;
		String sArgString = createArgString(oParameters);
		String sValue = createWinFcnMapString(FUNC_CALL_LIST.rf_comm_check.name(), sArgString);
		
		m_oFrameSmartCardOperation.createForwardEvent(sValue, m_oFuncSmartCard.getTimeout(), m_oFuncSmartCard.getSocketDelay());
	}
	
	private void wrf_setbright(int iDevice, int iBright, String sResponse){
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			
			// Go to next step
			m_oFuncCallList.removeFirst();
			
			// Next step - wrf_load_key_hex
			m_oCurrentStepArgs[0] = m_oFuncSmartCard.getDevice();
			m_oCurrentStepArgs[1] = SC_LOAD_MODE0;
			m_oCurrentStepArgs[2] = m_iLoadKeyIndex;
			m_oCurrentStepArgs[3] = m_oFuncSmartCard.getKeyA();
			stepControl(null, m_oCurrentStepArgs);
			
			return;
		}
		
		Object[] oParameters = new Object[2];
		oParameters[0] = iDevice;
		oParameters[1] = iBright;
		String sArgString = createArgString(oParameters);
		String sValue = createWinFcnMapString(FUNC_CALL_LIST.rf_setbright.name(), sArgString);
		
		m_oFrameSmartCardOperation.createForwardEvent(sValue, m_oFuncSmartCard.getTimeout(), m_oFuncSmartCard.getSocketDelay());
	}
	
	private void wrf_load_key_hex(int iDevice, int iMode, int iIndex, String sKeyA, String sResponse){
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			// Error code : 0 - Success
			//				1 - Error code
			// Pre-process the response
			String[] sRetArgsString = processArgString(sResponse);
			
			int iErrorCode = Integer.parseInt(sRetArgsString[0]);
			if(iErrorCode == 0){
				// No error
								
				if(iIndex < 15){
					m_iLoadKeyIndex++;
					
					// Run the same function with increment index
					m_oCurrentStepArgs[0] = m_oFuncSmartCard.getDevice();
					m_oCurrentStepArgs[1] = SC_LOAD_MODE0;
					m_oCurrentStepArgs[2] = m_iLoadKeyIndex;
					m_oCurrentStepArgs[3] = m_oFuncSmartCard.getKeyA();
				}else{
					// Next step - finish
					m_oFuncCallList.removeFirst();
				}
				stepControl(null, m_oCurrentStepArgs);
			}else{
				// Error ocrur
				// Write error log
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_load_key_to_device");
				writeErrorLog("wrf_load_key_hex", iErrorCode);
				
				// Add wrf_halt
				m_oFuncCallList.removeFirst();
				m_oFuncCallList.addFirst(FUNC_CALL_LIST.rf_halt.name());
				
				// Start process
				// First function arguments
				m_oCurrentStepArgs[0] = m_oFuncSmartCard.getDevice();
				
				stepControl(null, m_oCurrentStepArgs);
			}
			
			return;
		}
		
		Object[] oParameters = new Object[4];
		oParameters[0] = iDevice;
		oParameters[1] = iMode;
		oParameters[2] = iIndex;
		oParameters[3] = sKeyA;
		String sArgString = createArgString(oParameters);
		String sValue = createWinFcnMapString(FUNC_CALL_LIST.rf_load_key_hex.name(), sArgString);
		
		m_oFrameSmartCardOperation.createForwardEvent(sValue, m_oFuncSmartCard.getTimeout(), m_oFuncSmartCard.getSocketDelay());
	}
	
	private void wrf_card(int iDevice, int iMode, boolean bFirstReadCard, String sResponse){
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			// Error code : 0 - Success
			//				1 - Error code
			// Pre-process the response
			String[] sRetArgsString = processArgString(sResponse);
			
			int iErrorCode = Integer.parseInt(sRetArgsString[0]);
			if(iErrorCode == 0){
				// No error
				
				Long lSerial = getArgUnsignedLong(sRetArgsString, 1, 512);
				
				// Get the serial no.
				m_oFuncSmartCard.setSerialNo(Long.toHexString(lSerial).toUpperCase());
				
				if(m_sLastSerialNo.equals(m_oFuncSmartCard.getSerialNo()) == false)
					m_bNeedAuth = true;
				
				m_sLastSerialNo = m_oFuncSmartCard.getSerialNo();
				
				m_oFuncCallList.removeFirst();

				if(bFirstReadCard){
					if(m_sOperationType.equals(OPERATION_TYPE_LIST.read_serial.name())){
						// Next step - beep
						m_oCurrentStepArgs[1] = 10;
					}else{
						// Next step - wrf_card (Really read card)
						m_oCurrentStepArgs[2] = false;	// Not allow retry
					}
				}else{
					// Next step - read card
				}
				stepControl(null, m_oCurrentStepArgs);
			}else{
				// Error ocrur
				
				if(bFirstReadCard == false){
					// Cannot detect card again
					m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_detect_card_again");
					
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(m_sLastErrorMessage);
					oFormDialogBox.show();
					
					// No retry, abort operation
					finishOperation();
					
				}else{
					// Add a wrf_halt request before next wrf_card
					m_oFuncCallList.addFirst(FUNC_CALL_LIST.rf_halt.name());
				}
				stepControl(null, m_oCurrentStepArgs);
			}
			
			return;
		}
		
		Object[] oParameters = new Object[2];
		oParameters[0] = iDevice;
		oParameters[1] = iMode;
		String sArgString = createArgString(oParameters);
		String sValue = createWinFcnMapString(FUNC_CALL_LIST.rf_card.name(), sArgString);
		
		m_oFrameSmartCardOperation.createForwardEvent(sValue, m_oFuncSmartCard.getTimeout(), m_oFuncSmartCard.getSocketDelay());
	}
	
	private void wrf_read_hex(int iDevice, String sResponse){
		
		// Get the field name
		String sFieldName = m_sReadField.getFirst();
		int iSector = m_oFuncSmartCard.getFieldSector(sFieldName);
		int iBlock = m_oFuncSmartCard.getFieldBlock(sFieldName);
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			// Error code : 0 - Success
			//				1 - Error code
			// Pre-process the response
			String[] sRetArgsString = processArgString(sResponse);
			
			int iErrorCode = Integer.parseInt(sRetArgsString[0]);
			if(iErrorCode == 0){
				// No error
				
				// Get the value
				String sReturn = getArgString(sRetArgsString, 1, 512, true);
				m_oFuncSmartCard.setCardValue(sFieldName, sReturn);

				// Store the block data
				m_sLastReadBlockData.put(iBlock, sReturn);
				
				// Remove the first read field
				m_sReadField.removeFirst();
				
				if(m_sReadField.isEmpty()){
					if(m_sOperationType.equals(OPERATION_TYPE_LIST.read_card.name())){
						// Show read card result
						m_oFrameSmartCardOperation.showReadCardResultScreen(m_oFuncSmartCard);
					}					
					
					// Go to next step
					m_oFuncCallList.removeFirst();
					
					// Next step - beep
					m_oCurrentStepArgs[0] = m_oFuncSmartCard.getDevice();
					m_oCurrentStepArgs[1] = 10;
				}else{
					// Still have field to read, continue wrf_read_hex
				}
				
				// Next step
				stepControl(null, m_oCurrentStepArgs);
			}else{
				// Error ocrur
				// Write error log
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_read_card");
				writeErrorLog("wrf_read_hex", iErrorCode);
				
				// Finish showing this form
				// Prompt error
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(m_sLastErrorMessage);
				oFormDialogBox.show();
				
				// No retry, abort operation
				finishOperation();
			}
			
			return;
		}
		
		// Check if the block is loaded before
		if(m_bNeedAuth == false && m_sLastReadBlockData.containsKey(iBlock)){
			// Block is loaded before
			
			// Get the value
			String sBlockData = m_sLastReadBlockData.get(iBlock);
			m_oFuncSmartCard.setCardValue(sFieldName, sBlockData);
			
			//Remove the first read field
			m_sReadField.removeFirst();

			if(m_sReadField.isEmpty()){		
				// Show read card result
				if(m_sOperationType.equals(OPERATION_TYPE_LIST.read_card.name())){
					m_oFrameSmartCardOperation.showReadCardResultScreen(m_oFuncSmartCard);
				}
				
				// Go to next step
				m_oFuncCallList.removeFirst();
			}else{
				// Still have field to read, continue wrf_read_hex
			}
			
			// Next step
			stepControl(null, m_oCurrentStepArgs);
			
			return;
		}
		
		// Need authentication
		if(m_bNeedAuth || m_iLastSector != iSector){
			
			// Clear the flag
			m_bNeedAuth = false;
			
			m_oFuncCallList.addFirst(FUNC_CALL_LIST.rf_authentication.name());
			
			// Next step - wrf_authentication
			m_oCurrentStepArgs[0] = m_oFuncSmartCard.getDevice();
			m_oCurrentStepArgs[1] = SC_LOAD_MODE0;
			m_oCurrentStepArgs[2] = iSector;
			stepControl(null, m_oCurrentStepArgs);
			
			return;
		}
		
		Object[] oParameters = new Object[2];
		oParameters[0] = iDevice;
		oParameters[1] = iBlock;
		String sArgString = createArgString(oParameters);
		String sValue = createWinFcnMapString(FUNC_CALL_LIST.rf_read_hex.name(), sArgString);
		
		m_oFrameSmartCardOperation.createForwardEvent(sValue, m_oFuncSmartCard.getTimeout(), m_oFuncSmartCard.getSocketDelay());
	}
	
	private void wrf_authentication(int iDevice, int iMode, int iSector, String sResponse){
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			// Error code : 0 - Success
			//				1 - Error code
			// Pre-process the response
			String[] sRetArgsString = processArgString(sResponse);
			
			int iErrorCode = Integer.parseInt(sRetArgsString[0]);
			if(iErrorCode == 0){
				// No error
				
				// Save the last read card sector
				m_iLastSector = iSector;
				
				// Go to next step
				m_oFuncCallList.removeFirst();
				
				// Next step - wrf_read_hex
				m_oCurrentStepArgs[0] = m_oFuncSmartCard.getDevice();
				m_oCurrentStepArgs[1] = 15;
				stepControl(null, m_oCurrentStepArgs);
			}else{
				// Error ocrur
				// Write error log
				m_sLastErrorMessage = AppGlobal.g_oLang.get()._("fail_to_check_authentication");
				writeErrorLog("wrf_authentication", iErrorCode);
				
				// Finish showing this form
				// Prompt error
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(m_sLastErrorMessage);
				oFormDialogBox.show();
				
				// No retry, abort operation
				finishOperation();
			}
			
			return;
		}
		
		Object[] oParameters = new Object[3];
		oParameters[0] = iDevice;
		oParameters[1] = iMode;
		oParameters[2] = iSector;
		String sArgString = createArgString(oParameters);
		String sValue = createWinFcnMapString(FUNC_CALL_LIST.rf_authentication.name(), sArgString);
		
		m_oFrameSmartCardOperation.createForwardEvent(sValue, m_oFuncSmartCard.getTimeout(), m_oFuncSmartCard.getSocketDelay());
	}
	
	private void wrf_halt(int iDevice, String sResponse){
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			// No error
			// Go to next step
			m_oFuncCallList.removeFirst();
			
			// Next step - wrf_exit
			m_oCurrentStepArgs[0] = m_oFuncSmartCard.getDevice();
			stepControl(null, m_oCurrentStepArgs);
			
			return;
		}
		
		Object[] oParameters = new Object[1];
		oParameters[0] = iDevice;
		String sArgString = createArgString(oParameters);
		String sValue = createWinFcnMapString(FUNC_CALL_LIST.rf_halt.name(), sArgString);
		
		m_oFrameSmartCardOperation.createForwardEvent(sValue, m_oFuncSmartCard.getTimeout(), m_oFuncSmartCard.getSocketDelay());
	}
	
	private void wrf_beep(int iDevice, int iMsec, String sResponse){
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			// No error
			// Go to next step
			m_oFuncCallList.removeFirst();
			
			// Next step - finish
			stepControl(null, m_oCurrentStepArgs);
			
			return;
		}
		
		Object[] oParameters = new Object[2];
		oParameters[0] = iDevice;
		oParameters[1] = iMsec;
		String sArgString = createArgString(oParameters);
		String sValue = createWinFcnMapString(FUNC_CALL_LIST.rf_beep.name(), sArgString);
		
		m_oFrameSmartCardOperation.createForwardEvent(sValue, m_oFuncSmartCard.getTimeout(), m_oFuncSmartCard.getSocketDelay());
	}
	
	private void wrf_exit(int iDevice, String sResponse){
		
		// Check if the response is success or not
		// If success, go to next step
		// Otherwise, prompt error and exit
		if(sResponse != null){
			// Finish
			finishOperation();
			
			return;
		}
		
		Object[] oParameters = new Object[1];
		oParameters[0] = iDevice;
		String sArgString = createArgString(oParameters);
		String sValue = createWinFcnMapString(FUNC_CALL_LIST.rf_exit.name(), sArgString);
		
		m_oFrameSmartCardOperation.createForwardEvent(sValue, m_oFuncSmartCard.getTimeout(), m_oFuncSmartCard.getSocketDelay());
	}
	
	private void writeErrorLog(String sFunction, int iErrorCode){
		AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", AppGlobal.g_oFuncUser.get().getUserId()+"",
				"Smart Card Function: " + m_sOperationType + ", Protocol: " + sFunction + ", Error code: " + iErrorCode + ", Error message: " + m_sLastErrorMessage);
	}
	
	public boolean isProcessSuccess(){
		return m_bProcessSuccess;
	}
	
	public boolean isCancelByUser(){
		return m_bCancelByUser;
	}
	
	public void finishOperation(){		
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public void FrameSmartCardOperation_clickCancel() {
		// Cancel by user
		if(m_bProcessSuccess == false)
			m_bCancelByUser = true;
		
		finishOperation();
	}

	@Override
	public void FrameSmartCardOperation_forward(String sResponse) {
		// Set the connection timeout to device manager to prevent timeout loop
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(0);
		
		stepControl(sResponse, m_oCurrentStepArgs);		
	}

	@Override
	public void FrameSmartCardOperation_disconnect() {
		// Disconnect
		if(m_bProcessSuccess == false){
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("disconnect_from_smart_card_device"));
			oFormDialogBox.show();
			
			finishOperation();
		}
	}
	
	@Override
	public void FrameSmartCardOperation_timeout() {
		// Timeout
		if(m_bProcessSuccess == false){
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("timeout"));
			oFormDialogBox.show();

			finishOperation();
		}
	}
}
