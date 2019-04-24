package app;

import java.util.ArrayList;
import java.util.List;

import commonui.FormConfirmBox;
import commonui.FormSelectionBox;
import core.Controller;
import om.PosActionLog;
import om.PosStation;
import om.PosUserDrawerOwnership;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;


public class FormTakeDrawerOwnership extends VirtualUIForm implements FrameTakeDrawerOwnershipListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameTakeDrawerOwnership m_oFrameTakeDrawerOwnership;
	
	public FormTakeDrawerOwnership(Controller oParentController, List<PosStation> oPosStationList, FuncUser oAuthorityUser) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmTakeDrawerOwnership.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);

		m_oFrameTakeDrawerOwnership = new FrameTakeDrawerOwnership(oPosStationList, oAuthorityUser);
		m_oTemplateBuilder.buildFrame(m_oFrameTakeDrawerOwnership, "fraTakeDrawerOwnership");
		m_oFrameTakeDrawerOwnership.addListener(this);
		this.attachChild(m_oFrameTakeDrawerOwnership);
	}
	
	@Override
	public void frameTakeDrawerOwnership_clickClose() {
		// TODO Auto-generated method stub
		this.finishShow();	
	}
	
	@Override
	public void frameTakeDrawerOwnership_commonBasketFieldClicked(List<PosUserDrawerOwnership> oUserDrawerOwnershipList,
			int iItemIndex, int iFieldIndex, FuncUser oAuthorityFuncUser) {
		// TODO Auto-generated method stub
		int iCurrentUserId = AppGlobal.g_oFuncUser.get().getUserId();
		FuncUser oCurrentFuncUser = AppGlobal.g_oFuncUser.get();
		
		if (oAuthorityFuncUser != null && oAuthorityFuncUser.getUser() != null) {
			iCurrentUserId = oAuthorityFuncUser.getUserId();
			oCurrentFuncUser = oAuthorityFuncUser;
		}
		
		if (iCurrentUserId == oUserDrawerOwnershipList.get(iItemIndex).getUserId()) {
			FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"),
					AppGlobal.g_oLang.get()._("no"), this);
			oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("confirm_to_unassign_ownership") + "?");
			oFormConfirmBox.show();
			if (oFormConfirmBox.isOKClicked() == false)
				return;
			
			// unassign ownership
			int iDrawerNumber = oUserDrawerOwnershipList.get(iItemIndex).getDrawer();
			m_oFrameTakeDrawerOwnership.updateDrawerOwnershipUserList(iItemIndex, iFieldIndex, 0, "");
			
			String sLogRemark = "Unassign Drawer Ownership for drawer: "+iDrawerNumber;
			AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.take_drawer_ownership.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", oCurrentFuncUser.getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", sLogRemark);
			AppGlobal.g_oActionLog.get().handleActionLog(false);
			return;
		}
		
		ArrayList<String> oOptionList = new ArrayList<String>();
		oOptionList.add(AppGlobal.g_oLang.get()._("take_ownership"));
		oOptionList.add(AppGlobal.g_oLang.get()._("unassign_ownership"));
		
		FormSelectionBox oFormSelectionBox = new FormSelectionBox(this);
		oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("please_select_the_action_type"),
				oOptionList, false);
		oFormSelectionBox.show();
		
		if (oFormSelectionBox.isUserCancel()) {
			return;
		}
		
		int iSelectedAction = oFormSelectionBox.getResultList().get(0);
		if (iSelectedAction == 0) {
			// take ownership
			
			// ask to confirm assign ownership
			FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"),
					AppGlobal.g_oLang.get()._("no"), this);
			oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("confirm_to_take_ownership") + "?");
			oFormConfirmBox.show();
			if (oFormConfirmBox.isOKClicked() == false)
				return;
			
			//update drawer ownership user list
			m_oFrameTakeDrawerOwnership.updateDrawerOwnershipUserList(iItemIndex, iFieldIndex,
					iCurrentUserId, oCurrentFuncUser.getUserName(AppGlobal.g_oCurrentLangIndex.get()));
			
			String sLogRemark = "Take drawer ownership for drawer: "+oUserDrawerOwnershipList.get(iItemIndex).getDrawer();
			AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.take_drawer_ownership.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", oCurrentFuncUser.getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", sLogRemark);
			AppGlobal.g_oActionLog.get().handleActionLog(false);
		} else if (iSelectedAction == 1) {
			FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"),
					AppGlobal.g_oLang.get()._("no"), this);
			oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("confirm_to_unassign_ownership") + "?");
			oFormConfirmBox.show();
			if (oFormConfirmBox.isOKClicked() == false)
				return;
			
			// unassign ownership
			m_oFrameTakeDrawerOwnership.updateDrawerOwnershipUserList(iItemIndex, iFieldIndex, 0, "");
			
			String sLogRemark = "Unassign drawer ownership for drawer: "+oUserDrawerOwnershipList.get(iItemIndex).getDrawer();
			AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.take_drawer_ownership.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", oCurrentFuncUser.getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", sLogRemark);
			AppGlobal.g_oActionLog.get().handleActionLog(false);
		}
	}
}
