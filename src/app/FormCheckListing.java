package app;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import core.Controller;
import om.PosCheck;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

/** interface for the listeners/observers callback method */
interface FormCheckListingListener {
	void formCheckListing_CheckListRecordClicked(int iPanelId, PosCheck oCheck, int iListingtype);
}

public class FormCheckListing extends VirtualUIForm implements FrameCheckListingListener {
	private TemplateBuilder m_oTemplateBuilder;
	private FrameCheckListing m_oFrameCheckListing;
	private boolean m_bAdjustPaymentClick;
	private PosCheck m_oPosCheck;
	private boolean m_bContinueBtnClicked;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FormCheckListingListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FormCheckListingListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FormCheckListingListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FormCheckListing(Controller oParentController, AppGlobal.OPERATION_MODE eOperationMode) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FormCheckListingListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmCheckListing.xml");
		listeners = new ArrayList<FormCheckListingListener>();
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);

		// SelectionBox Frame
		m_oFrameCheckListing = new FrameCheckListing(eOperationMode);
		m_oTemplateBuilder.buildFrame(m_oFrameCheckListing, "fraCheckListing");

		m_oFrameCheckListing.addListener(this);
		this.attachChild(m_oFrameCheckListing);
		
		m_bAdjustPaymentClick = false;
		m_bContinueBtnClicked = false;
	}

    public void init(HashMap<String, String> oConditionInfo, int sType) {
		PosCheck oPosCheck = new PosCheck();

		if(oConditionInfo == null) {
			// for current date check listing
			if(sType == FrameCheckListing.TYPE_NORMAL){
				m_oFrameCheckListing.addCheckListingTab(FrameCheckListing.PANEL_PAID_CHECK_CURRENT_STATION, AppGlobal.g_oLang.get()._("paid_check_listing")+"("+ AppGlobal.g_oLang.get()._("current_station")+")");
				m_oFrameCheckListing.addCheckListingTab(FrameCheckListing.PANEL_PAID_CHECK, AppGlobal.g_oLang.get()._("paid_check_listing"));
				m_oFrameCheckListing.addCheckListingTab(FrameCheckListing.PANEL_OPEN_CHECK, AppGlobal.g_oLang.get()._("open_check_listing"));
				m_oFrameCheckListing.addCheckListingTab(FrameCheckListing.PANEL_VOID_CHECK, AppGlobal.g_oLang.get()._("void_check_listing"));
			}
			else if(sType == FrameCheckListing.TYPE_ACTIVE_CHECK || sType == FrameCheckListing.TYPE_DAILY_CLOSE_OPEN_CHECK){
				if (sType == FrameCheckListing.TYPE_ACTIVE_CHECK)
					m_oFrameCheckListing.setTitle(AppGlobal.g_oLang.get()._("payment_interface_transfer_card_authorization"));
				else
					m_oFrameCheckListing.setTitle(AppGlobal.g_oLang.get()._("open_check_listing"));
				m_oFrameCheckListing.setCheckListing(sType);
				m_oFrameCheckListing.addCheckListingTab(FrameCheckListing.PANEL_ACTIVE_CHECK, AppGlobal.g_oLang.get()._("open_check_listing"));
			}
			
			JSONArray oCheckHistoryJSONArray = new JSONArray();
			boolean bCheckTableFloorPlan = false;
			if(sType == FrameCheckListing.TYPE_ACTIVE_CHECK || sType == FrameCheckListing.TYPE_DAILY_CLOSE_OPEN_CHECK) {
				oCheckHistoryJSONArray = oPosCheck.getCheckListByBusinessDayPaid(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncStation.get().getStationId(), PosCheck.PAID_NOT_PAID, false);
				bCheckTableFloorPlan = true;
			} else
				oCheckHistoryJSONArray = oPosCheck.getCheckListByBusinessDayPaid(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncStation.get().getStationId(), PosCheck.PAID_FULL_PAID, false);
			if(oCheckHistoryJSONArray != null)
				m_oFrameCheckListing.addRecordToFrameCheckListPanel(FrameCheckListing.PANEL_ACTIVE_CHECK, oCheckHistoryJSONArray, bCheckTableFloorPlan);
		}else {
			// for past date check listing
			m_oFrameCheckListing.setCheckListing(FrameCheckListing.TYPE_PAST_DATE);
			m_oFrameCheckListing.addCheckListingTab(FrameCheckListing.PANEL_PAST_DATE_CHECK, AppGlobal.g_oLang.get()._("past_date_check"));
						
			JSONArray oCheckHistoryJSONArray = oPosCheck.getPastDateCheckList(oConditionInfo, AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getShop().getTimezone());
			if(oCheckHistoryJSONArray != null)
				m_oFrameCheckListing.addRecordToFrameCheckListPanel(FrameCheckListing.PANEL_PAST_DATE_CHECK, oCheckHistoryJSONArray, false);
		}
		// Show open check listing
		m_oFrameCheckListing.changeSelectedListingTab(0);
		m_bAdjustPaymentClick = false;
	}
	
	public PosCheck getSelectedPosCheck(){
		return m_oPosCheck;
	}
	
	public void clearCheckListPanelFrame() {
		this.finishShow();
	}
	
	public void bringCheckListingToTop() {
		m_oFrameCheckListing.bringToTop();
	}
	
	public void setAdjustPaymentClick(boolean bClick) {
		m_bAdjustPaymentClick = bClick;
	}
	
	public void setCheckListingByType(int iSetCheckListingByType) {
		m_oFrameCheckListing.setCheckListingByType(iSetCheckListingByType);
	}
	
	public boolean isAdjustPaymentClick() {
		return m_bAdjustPaymentClick;
	}
	
	public boolean isContinueButtonClick() {
		return m_bContinueBtnClicked;
	}
	
	@Override
	public void frameCheckListing_ButtonExitClicked() {
		clearCheckListPanelFrame();
	}
	
	@Override
	public void frameCheckListing_CheckListRecordClicked(int iPanelId, PosCheck oCheck, int iListingType) {
		for(FormCheckListingListener listener: listeners) {
			listener.formCheckListing_CheckListRecordClicked(iPanelId, oCheck, iListingType);
		}
		m_oPosCheck = oCheck;
	}

	@Override
	public void frameCheckListing_ButtonContinueClicked() {
		m_bContinueBtnClicked = true;
		clearCheckListPanelFrame();
	}
}

