package app;

import java.util.List;

import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import core.Controller;
import om.PosActionLog;
import om.PosUserDrawerOwnership;
import om.PosUserDrawerOwnershipList;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;



public class FormAssignDrawerOwnership extends VirtualUIForm implements FrameAssignDrawerOwnershipListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameAssignDrawerOwnership m_oFrameAssignDrawerOwnership;
	
	public FormAssignDrawerOwnership(Controller oParentController, int iMaxDrawerOwnership) {
		super(oParentController);
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmAssignDrawerOwnership.xml");
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);

		m_oFrameAssignDrawerOwnership = new FrameAssignDrawerOwnership(iMaxDrawerOwnership);
		m_oTemplateBuilder.buildFrame(m_oFrameAssignDrawerOwnership, "fraAssignDrawerOwnership");
		m_oFrameAssignDrawerOwnership.addListener(this);
		this.attachChild(m_oFrameAssignDrawerOwnership);
	}
	
	@Override
	public void frameAssignDrawerOwnership_clickClose() {
		this.finishShow();
	}
	
	@Override
	public void frameAssignDrawerOwnership_commonBasketClicked(List<PosUserDrawerOwnership> oUserDrawerOwnershipListByOrder, int iItemIndex, int iFieldIndex) {
		// TODO Auto-generated method stub
		// get current outlet level drawer ownership owned by user
		PosUserDrawerOwnershipList oTmpUserDrawerOwnershipList = new PosUserDrawerOwnershipList();
		oTmpUserDrawerOwnershipList.findAllByConditions(AppGlobal.g_oFuncOutlet.get().getOutletId(), 0,
				AppGlobal.g_oFuncUser.get().getUserId());
		int iDrawerOwned = oTmpUserDrawerOwnershipList.getPosUserDrawerOwnership().size();
		// get current user Id
		int iCurrentUserId = AppGlobal.g_oFuncUser.get().getUserId();
		
		// check user
		if (oUserDrawerOwnershipListByOrder.get(iItemIndex).getUserId() != 0
				&& oUserDrawerOwnershipListByOrder.get(iItemIndex).getUserId() != iCurrentUserId) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("action_not_allowed"));
			oFormDialogBox.show();
			return;
		} else if (oUserDrawerOwnershipListByOrder.get(iItemIndex).getUserId() == iCurrentUserId) {
			FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"),
					AppGlobal.g_oLang.get()._("no"), this);
			oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("confirm_to_unassign_ownership") + "?");
			oFormConfirmBox.show();
			if (oFormConfirmBox.isOKClicked() == false)
				return;

			int iDrawerNumber = oUserDrawerOwnershipListByOrder.get(iItemIndex).getDrawer();
			m_oFrameAssignDrawerOwnership.setUpdateAction(true);
			m_oFrameAssignDrawerOwnership.updateDrawerOwnershipUserList(iItemIndex, iFieldIndex, 0, "");
			
			String sLogRemark = "Unassign drawer ownership for drawer: "+iDrawerNumber;
			AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.assign_drawer_ownership.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", sLogRemark);
			AppGlobal.g_oActionLog.get().handleActionLog(false);
		} else {
			// TODO Auto-generated method stub
			if (m_oFrameAssignDrawerOwnership.getMaxDrawerOwnership() != 0
					&& m_oFrameAssignDrawerOwnership.getMaxDrawerOwnership() == iDrawerOwned) {
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("number_of_user_drawer_ownership_exceeded")
						+ System.lineSeparator() + AppGlobal.g_oLang.get()._("maximum_ownership ") + ": "
						+ m_oFrameAssignDrawerOwnership.getMaxDrawerOwnership());
				
				oFormDialogBox.show();
				return;
			}
			
			FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"),
					AppGlobal.g_oLang.get()._("no"), this);
			oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("confirm_to_assign_ownership") + "?");
			oFormConfirmBox.show();
			if (oFormConfirmBox.isOKClicked() == false)
				return;
			
			String sUserName = AppGlobal.g_oFuncUser.get().getUserName(AppGlobal.g_oCurrentLangIndex.get());
			m_oFrameAssignDrawerOwnership.updateDrawerOwnershipUserList(iItemIndex, iFieldIndex, iCurrentUserId,
					AppGlobal.g_oFuncUser.get().getUserName(AppGlobal.g_oCurrentLangIndex.get()));
			
			String sLogRemark = "Assign drawer ownership for drawer: "+oUserDrawerOwnershipListByOrder.get(iItemIndex).getDrawer()+", User: "+sUserName;
			AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.assign_drawer_ownership.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", sLogRemark);
			AppGlobal.g_oActionLog.get().handleActionLog(false);
		}
	}
}
