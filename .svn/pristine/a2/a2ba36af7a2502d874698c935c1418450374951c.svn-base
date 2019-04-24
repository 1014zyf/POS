package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import om.InfInterface;
import om.InfVendor;
import om.PosInterfaceConfig;

import org.json.JSONObject;

import app.AppGlobal;
import commonui.FormDialogBox;

import core.Controller;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormElectronicScale extends VirtualUIForm implements FrameElectronicScaleListener {
	private boolean m_bOK;
	
	TemplateBuilder m_oTemplateBuilder;
	
	// Support flag
	private boolean m_bSupport;

	// Smart Card Device
	private String m_sDevice;
	
	private int m_iBuadRate;
	
	// Timeout setting
	private int m_iTimeout;
	
	// Flow Control
	private int m_iFlowControl;
	
	private FrameElectronicScale m_oFrameElectronicScale;
	private BigDecimal m_dWeight;
	
	public FormElectronicScale(Controller oParentController){
		super(oParentController);
		
		init();
	}
	
	private boolean init() {
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmElectronicScale.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameElectronicScale = new FrameElectronicScale();
		m_oTemplateBuilder.buildFrame(m_oFrameElectronicScale, "fraElectronicScale");
		m_oFrameElectronicScale.init();
		
		// Add listener
		m_oFrameElectronicScale.addListener(this);
		this.attachChild(m_oFrameElectronicScale);
		
		m_bSupport = false;
		m_iTimeout = 1000;
		m_sDevice = "";
		m_iBuadRate = 9600;
		m_iFlowControl = 0;
		m_dWeight = BigDecimal.ZERO;
		m_bOK = false;
		
		return true;
	}
	
	public boolean readSetup() {
		List<PosInterfaceConfig> oInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		
		// Get the configure from interface module
		oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(InfInterface.TYPE_PERIPHERAL_DEVICE);
		for (PosInterfaceConfig oPosInterfaceConfig:oInterfaceConfigList) {
			if (oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_DEVICE_MANAGER)) {
				try {
					JSONObject oInterfaceSetup = oPosInterfaceConfig.getInterfaceConfig();
					
					// Support flag
					if (oInterfaceSetup.has("electronic_scale_setup") && oInterfaceSetup.getJSONObject("electronic_scale_setup").getJSONObject("params").getJSONObject("support").getInt("value") == 1)
						m_bSupport = true;
					else
						break;

					// Wait timeout
					m_iTimeout = Integer.parseInt(oInterfaceSetup.getJSONObject("electronic_scale_setup").getJSONObject("params").getJSONObject("process_timeout").getString("value"));

					//read setup
					if (oInterfaceSetup.optJSONObject("electronic_scale_setup").optJSONObject("params").has("device_name"))
						m_sDevice = oInterfaceSetup.optJSONObject("electronic_scale_setup").optJSONObject("params").optJSONObject("device_name").optString("value", "");
					if (oInterfaceSetup.optJSONObject("electronic_scale_setup").optJSONObject("params").has("baud_rate"))
						m_iBuadRate = oInterfaceSetup.optJSONObject("electronic_scale_setup").optJSONObject("params").optJSONObject("baud_rate").optInt("value", 9600);
					
					m_iFlowControl = Integer.parseInt(oInterfaceSetup.getJSONObject("electronic_scale_setup").getJSONObject("params").getJSONObject("flow_control").getString("value"));
				} catch (Exception e) {
					AppGlobal.stack2Log(e);
				}
			}
		}
		
		return m_bSupport;
	}
	
	public void setTitle(String sTitle){
		m_oFrameElectronicScale.setTitle(sTitle);
	}
	
	public void setMessage(String sMessage){
		m_oFrameElectronicScale.setMessage(sMessage);
	}
	
	public boolean isOKClicked(){
		return m_bOK;
	}
	
	public BigDecimal getWeight() {
		return m_dWeight;
	}
	
	public void checkElectronicScaleValue() {
		
		setMessage(AppGlobal.g_oLang.get()._("processing")+"...");
		m_oFrameElectronicScale.showOKButton(false);
		m_oFrameElectronicScale.showRetryButton(false);
		m_oFrameElectronicScale.showCancelButton(false);
		
		Object[] oParameters = new Object[4];
		oParameters[0] = m_sDevice;
		oParameters[1] = String.valueOf(m_iBuadRate);
		oParameters[2] = m_iFlowControl;
		oParameters[3] = m_iTimeout;
		
		String sSendPacketString = createWinFcnMapString("casElectronicBalanceCheck", oParameters);
		
		m_oFrameElectronicScale.createForwardEvent(sSendPacketString, 5000, 0);
	}
	
	private String createWinFcnMapString(String sType, Object[] oParameters) {
		StringBuilder sArgString = new StringBuilder();
		
		for (int i = 0; i < oParameters.length; i++) {
			if (oParameters[i] != null) {
				if (i != 0)
					sArgString.append(";");

				if(oParameters[i].getClass().equals(String.class)){
					String sString = (String) oParameters[i];
					StringBuilder sb = new StringBuilder();
					for (int j=0; j<sString.length(); j++) {
						sb.append(String.format("%02x", (int)sString.charAt(j)));
					}
					sArgString.append(sb.toString());
				}else{
					sArgString.append(oParameters[i]);
				}
			}
		}
		sArgString.append((char)0x02);
		
		return String.format("\001win_fcn_map\002%s\002%s\004", sType, sArgString.toString());
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
			String[] oReturnArgs = sResponse.split(";");
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
	
	public void finishOperation() {
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public void FrameElectronicScale_clickOK() {
		m_bOK = true;
		this.finishShow();
	}
	
	@Override
	public void FrameElectronicScale_clickRetry() {
		m_bOK = false;
		checkElectronicScaleValue();
	}
	
	@Override
	public void FrameElectronicScale_clickCancel() {
		m_bOK = false;
		this.finishShow();
	}
	
	@Override
	public void FrameElectronicScale_forward(String sResponse) {
		// Set the connection timeout to device manager to prevent timeout loop
		AppGlobal.g_oDeviceManagerElement.get().setForwardForwardRequestTimeout(0);
		
		m_oFrameElectronicScale.showRetryButton(true);
		m_oFrameElectronicScale.showCancelButton(true);
		
		if (!sResponse.isEmpty()) {
			// Process the response
			String[] sRetArgsString = processArgString(sResponse);
			if (sRetArgsString != null) {
				int iErrorCode = Integer.parseInt(sRetArgsString[0]);
				if(iErrorCode > 0){
					// No device manager error
					String sReturn = getArgString(sRetArgsString, 1, 512, true);
					if (sReturn.length() > 0){
						if (sReturn.charAt(0) == '?') {
							// Electronic scale return error
							m_oFrameElectronicScale.setMessage(AppGlobal.g_oLang.get()._("error") + " : " + AppGlobal.g_oLang.get()._("retry_please"));
						} else {
							BigDecimal dTmp = new BigDecimal(sReturn);
							m_dWeight = dTmp.divide(new BigDecimal("1000.0"));
							m_oFrameElectronicScale.setMessage(AppGlobal.g_oLang.get()._("result") + " : " + m_dWeight.toPlainString() + "kg");
							m_oFrameElectronicScale.showOKButton(true);
						}
					} else {
						// No response
						m_oFrameElectronicScale.setMessage(AppGlobal.g_oLang.get()._("error") + " : " + AppGlobal.g_oLang.get()._("no_response"));
					}
				} else {
					// Device manager return error
					switch(iErrorCode){
					case -1:
						m_oFrameElectronicScale.setMessage(AppGlobal.g_oLang.get()._("error") + " : " + AppGlobal.g_oLang.get()._("no_response"));
						break;
					default:
						m_oFrameElectronicScale.setMessage(AppGlobal.g_oLang.get()._("error") + " : " + AppGlobal.g_oLang.get()._("fail_to_build_connection"));
						break;
					}
				}
			}
		} else {
			// No response
			m_oFrameElectronicScale.setMessage(AppGlobal.g_oLang.get()._("error") + " : " + AppGlobal.g_oLang.get()._("no_response"));
		}
	}

	@Override
	public void FrameElectronicScale_disconnect() {
		// Disconnect
		FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
		oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
		oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_build_connection"));
		oFormDialogBox.show();

		finishOperation();
	}

	@Override
	public void FrameElectronicScale_timeout() {
		// Timeout
		FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
		oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
		oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("timeout"));
		oFormDialogBox.show();
		
		finishOperation();
	}
}
