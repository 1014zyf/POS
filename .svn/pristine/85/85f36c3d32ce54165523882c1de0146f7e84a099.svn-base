package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import commonui.FormDialogBox;
import commonui.FormInputBox;
import commonui.FormSelectionBox;
import core.Controller;
import om.InfVendor;
import om.PosInterfaceConfig;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIForm;

public class FormVoucherInterface extends VirtualUIForm {

	TemplateBuilder m_oTemplateBuilder;
	private FuncVoucherInterface m_oFuncVoucherInterface;
	private String m_sVoucherNumber;
	private PosInterfaceConfig m_oPosInterfaceConfig;
	
	// get the voucher Number
	public String getVoucherNumber() {
		return m_sVoucherNumber;
	}

	// get the voucher Amount and voucher type
	public HashMap<String, String> getLastVoucherInfo(){
		return m_oFuncVoucherInterface.getLastVoucherInfo();
	}
	
	public String getLastErrorMessage(){
		return m_oFuncVoucherInterface.getLastErrorMessage();
	}
	
	public FormVoucherInterface(Controller oParentController) {
		super(oParentController);
		m_oTemplateBuilder = new TemplateBuilder();
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
	
	public void initForProcess(PosInterfaceConfig oPosInterfaceConfig) {
		m_oFuncVoucherInterface = new FuncVoucherInterface(oPosInterfaceConfig);
		m_oPosInterfaceConfig = oPosInterfaceConfig;
	}

	public boolean voucherEnquiry(FuncCheck oFuncCheck) {
		if (m_oPosInterfaceConfig.getInterfaceVendorKey().equals(InfVendor.KEY_GALAXY)) {
			FormInputBox oFormInputBox = new FormInputBox(this);
			oFormInputBox.init();
			oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.DEFAULT);
			oFormInputBox.showKeyboard();
			oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("voucher_number"));
			oFormInputBox.setMessage(AppGlobal.g_oLang.get()._("please_input_the_voucher_number") + ":");
			oFormInputBox.show();

			if(oFormInputBox.isUserCancel())
				return false;

			// Check whether the input box is empty or not 
			if(oFormInputBox.getInputValue().isEmpty()){
				showAttentionDialogBox(AppGlobal.g_oLang.get()._("not_allow_blank_voucher_number"));
				return false;
			}

			// Get the Voucher Number from user input
			m_sVoucherNumber = oFormInputBox.getInputValue();
		}
/*
		sResult = m_oFuncVoucherInterface.voucherEnquiry(oEnquiryInfo);

		if (!sResult) 
			return false;
*/
		return true;
	}

	// get InterfaceConfig
	private PosInterfaceConfig getInterfaceConfig(String sInterfaceType, List<String> oVendors, String sTitle) {
		List<PosInterfaceConfig> oInterfaceConfigList = AppGlobal.getPosInterfaceConfigByInfType(sInterfaceType);
		List<PosInterfaceConfig> oPosInterfaceConfigList = new ArrayList<PosInterfaceConfig>();
		PosInterfaceConfig oTargetPosInterfaceConfig = null;
		if (!AppGlobal.isModuleSupport(AppGlobal.OPTIONAL_MODULE.pos_interface.name())) {
			showErrorDialogBox(AppGlobal.g_oLang.get()._("interface_module_is_not_supported"));
			return null;
		}
		if (oInterfaceConfigList.isEmpty()) {
			showErrorDialogBox(AppGlobal.g_oLang.get()._("no_voucher_interface_setup"));
			return null;
		}
		for (PosInterfaceConfig oPosInterfaceConfig : oInterfaceConfigList) {
			for (String sVendor : oVendors) {
				if (oPosInterfaceConfig.getInterfaceVendorKey().equals(sVendor))
					oPosInterfaceConfigList.add(oPosInterfaceConfig);
			}
		}

		if (oPosInterfaceConfigList.isEmpty()) {
			showDialogBox(AppGlobal.g_oLang.get()._("enquiry"), AppGlobal.g_oLang.get()._("no_voucher_interface_setup"));
			return null;
		}

		if (oPosInterfaceConfigList.size() == 1)
			oTargetPosInterfaceConfig = oPosInterfaceConfigList.get(0);
		else {
			ArrayList<String> oOptionList = new ArrayList<String>();
			HashMap<Integer, HashMap<String, String>> oInterfaceIdList = new HashMap<Integer, HashMap<String, String>>();
			int iInterfaceCount = 0;

			for (PosInterfaceConfig oInterfaceConfig : oPosInterfaceConfigList) {
				oOptionList.add(oInterfaceConfig.getInterfaceName(AppGlobal.g_oCurrentLangIndex.get()));
				HashMap<String, String> oTempInterfaceInfo = new HashMap<String, String>();
				oTempInterfaceInfo.put("interfaceId", String.valueOf(oInterfaceConfig.getInterfaceId()));
				oInterfaceIdList.put(iInterfaceCount, oTempInterfaceInfo);
				iInterfaceCount++;
			}

			FormSelectionBox oFormSelectionBox = new FormSelectionBox(this);
			oFormSelectionBox.initWithSingleSelection(sTitle, oOptionList, false);
			oFormSelectionBox.show();

			if (oFormSelectionBox.isUserCancel())
				return null;
			else {
				HashMap<String, String> oTempInterfaceInfo = oInterfaceIdList
						.get(oFormSelectionBox.getResultList().get(0));
				int iInterfaceId = Integer.valueOf(oTempInterfaceInfo.get("interfaceId"));
				for (PosInterfaceConfig oInterfaceConfig : oPosInterfaceConfigList) {
					if (oInterfaceConfig.getInterfaceId() == iInterfaceId)
						oTargetPosInterfaceConfig = oInterfaceConfig;
				}
			}
		}
		return oTargetPosInterfaceConfig;
	}
}