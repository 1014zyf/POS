package app;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import commonui.FormDialogBox;
import commonui.FormInputBox;
import core.Controller;
import om.PosCheckGratuity;
import templatebuilder.TemplateBuilder;
import virtualui.*;
/** interface for the listeners/observers callback method */
interface FormAskGratuityListener {
	List<Integer> formAskGratuity_askAuth();
}

public class FormAskGratuity extends VirtualUIForm implements FrameAskGratuityListener {
	private TemplateBuilder m_oTemplateBuilder;
	private FrameAskGratuity m_oFrameAskGratuity;
	private boolean m_bIsUserConfirm;
	private List<PosCheckGratuity> m_oCheckGratuityList;
	 private String m_sCheckNo;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FormAskGratuityListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FormAskGratuityListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FormAskGratuityListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FormAskGratuity(Controller oParentController){
		super(oParentController);
	}
	
	public boolean init(List<PosCheckGratuity> oPosCheckGratuityList, BigDecimal dSubTotal, BigDecimal dCheckTotalWithoutGratuity,String sCheckNo){
		this.m_sCheckNo = sCheckNo;
		return init(oPosCheckGratuityList, dSubTotal, dCheckTotalWithoutGratuity); 
	}
	
	public boolean init(List<PosCheckGratuity> oPosCheckGratuityList, BigDecimal dSubTotal, BigDecimal dCheckTotalWithoutGratuity){	
		listeners = new ArrayList<FormAskGratuityListener>();
		m_bIsUserConfirm = false;
		
		m_oTemplateBuilder = new TemplateBuilder();
		m_oTemplateBuilder.loadTemplate("frmAskGratuity.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		m_oFrameAskGratuity = new FrameAskGratuity();
		m_oTemplateBuilder.buildFrame(m_oFrameAskGratuity, "frmAskGratuity");
		m_oFrameAskGratuity.addListener(this);
		this.attachChild(m_oFrameAskGratuity);
		
		m_oCheckGratuityList = new ArrayList<PosCheckGratuity>(oPosCheckGratuityList);
		
		// Load gratuity list to basket
		m_oFrameAskGratuity.init(dSubTotal, dCheckTotalWithoutGratuity, m_oCheckGratuityList);
		
		return true;
	}
	
	public boolean getIsUserConfirm(){
		return m_bIsUserConfirm;
	}
	
	
	public List<PosCheckGratuity> getCheckGratuityList() {
		return m_oCheckGratuityList;
	}
	
	@Override
	public void frameAskGratuity_cancelFrame(){
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public void frameAskGratuity_finishFrame() {
		// "Confirm" pressed and gratuity list will be passed to formMain for
		// saving
		m_bIsUserConfirm = true;
		m_oCheckGratuityList = m_oFrameAskGratuity.getCheckGratuityList();
		//this.callSurveillanceEventForTips();
		// Finish showing this form
		this.finishShow();
	}

	/*
	public boolean callSurveillanceEventForTips() {
		if (this.m_sCheckNo == null || this.m_sCheckNo.isEmpty()) {
			this.m_sCheckNo = AppGlobal.g_oFuncStation.get().getNextCheckPrefixNumber(false);
			// return false;
		}
		double m_oValueChange = 0;
		try {
			m_oValueChange = m_oFrameAskGratuity.getGratuityChange();
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

		if (m_oFrameAskGratuity.getGratuityTraList().size() == 0
				&& m_oFrameAskGratuity.getOrgGratuityTraList().size() == 0) {
			return true;
		} else if (m_oFrameAskGratuity.getGratuityTraList().size() == m_oFrameAskGratuity.getOrgGratuityTraList().size()
				&& m_oValueChange < 0) {
			ArrayList<Integer> m_oGratuityTransList = m_oFrameAskGratuity.getGratuityTraList();
			ArrayList<Integer> m_oOrGratuityTraList = m_oFrameAskGratuity.getOrgGratuityTraList();
			for (Integer oGratuityTrans : m_oGratuityTransList) {
				if (m_oOrGratuityTraList.indexOf(oGratuityTrans) == -1)
					break;
				if (m_oGratuityTransList.indexOf(oGratuityTrans) == (m_oGratuityTransList.size() - 1))
					return true;
			}
		}
		
		HashMap<String, String> oSurveillanceEventInfo = new HashMap<String, String>();
		oSurveillanceEventInfo.put("checkNo", this.m_sCheckNo);
		oSurveillanceEventInfo.put("amount", String.valueOf(m_oValueChange));
		if (m_oValueChange > 0) {
			oSurveillanceEventInfo.put("eventType", FuncSurveillance.SURVEILLANCE_TYPE_TIP_ADD);
			FuncSurveillance.funcSurveilSetup(oSurveillanceEventInfo);
		} else if (m_oValueChange < 0) {
			oSurveillanceEventInfo.put("eventType", FuncSurveillance.SURVEILLANCE_TYPE_TIP_VOID);
			FuncSurveillance.funcSurveilSetup(oSurveillanceEventInfo);
		} else if (m_oValueChange == 0) {
			ArrayList<Integer> m_oGratuityTraList = m_oFrameAskGratuity.getGratuityTraList();
			ArrayList<Integer> m_oOrgGratuityTraList = m_oFrameAskGratuity.getOrgGratuityTraList();
			if (m_oGratuityTraList.size() > 0) {
				for (Integer oGratuityTran : m_oGratuityTraList) {
					if (m_oOrgGratuityTraList.indexOf(oGratuityTran) == -1) {
						oSurveillanceEventInfo.put("eventType", FuncSurveillance.SURVEILLANCE_TYPE_TIP_ADD);
						FuncSurveillance.funcSurveilSetup(oSurveillanceEventInfo);
					}
				}
			}
			if (m_oOrgGratuityTraList.size() > 0) {
				for (Integer oOrgGratuityTran : m_oOrgGratuityTraList) {
					if (m_oGratuityTraList.indexOf(oOrgGratuityTran) == -1) {
						oSurveillanceEventInfo.put("eventType", FuncSurveillance.SURVEILLANCE_TYPE_TIP_VOID);
						FuncSurveillance.funcSurveilSetup(oSurveillanceEventInfo);
					}
				}
			}
		}

		return true;

	}
	*/
	
	@Override
	public BigDecimal frameAskGratuity_askAmount(boolean bIsPercentage){
		String sMsg = "";
		BigDecimal dInput = BigDecimal.ZERO;
		if (bIsPercentage == true)
			sMsg = AppGlobal.g_oLang.get()._("please_input_tips_percentage") + ": (%)";
		else 
			sMsg = AppGlobal.g_oLang.get()._("please_input_tips_amount") + ":";
		FormInputBox oFormInputBox = new FormInputBox(this);
		oFormInputBox.init();
		oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.DECIMAL);
		oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("open_tips"));
		oFormInputBox.setMessage(sMsg);
		oFormInputBox.show();
		
		if (oFormInputBox.getInputValue() != null && !oFormInputBox.getInputValue().isEmpty())
			dInput = new BigDecimal(oFormInputBox.getInputValue());
		
		return dInput;
	}
	
	@Override
	public String frameAskGratuity_askName(){
		String sName = "", sMsg = "";
		
		sMsg = AppGlobal.g_oLang.get()._("please_input_the_name") + ":";
		FormInputBox oFormInputBox = new FormInputBox(this);
		oFormInputBox.init();
		oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.DEFAULT);
		oFormInputBox.showKeyboard();
		oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("open_name"));
		oFormInputBox.setMessage(sMsg);
		oFormInputBox.show();
		
		if (oFormInputBox.getInputValue() != null && !oFormInputBox.getInputValue().isEmpty())
			sName = oFormInputBox.getInputValue();
		
		return sName;
	}
	@Override
	public boolean frameAskGratuity_askAuth(int iUserGroupId) {
		// No need to ask authority
		if(iUserGroupId == 0)
			return true;
		
		// Check does the user include in the target user group
		if(AppGlobal.g_oFuncUser.get().getUserGroupList().contains(iUserGroupId))
			return true;
		
		// Ask another user to assign gratuity
		List<Integer> oUserGroupIdList;
		for (FormAskGratuityListener listener : listeners) {
			oUserGroupIdList =  listener.formAskGratuity_askAuth();
			if(oUserGroupIdList != null && oUserGroupIdList.contains(iUserGroupId))
				return true;
		}
		
		// The user cannot assign the gratuity
		FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
		oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
		oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("do_not_have_authority_to_perform_the_function"));
		oFormDialogBox.show();
		oFormDialogBox = null;
		
		return false;
	}
}