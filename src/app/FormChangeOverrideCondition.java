package app;

import java.util.ArrayList;
import java.util.List;

import core.Controller;
import om.PosOverrideCondition;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

public class FormChangeOverrideCondition extends VirtualUIForm implements FrameChangeOverrideConditionListener {
	private TemplateBuilder m_oTemplateBuilder;
	private FrameChangeOverrideCondition m_oFrameChangeOverrideCondition;
	
	private boolean m_bSaveClicked;
	
	private class OverrideConditionInfo {
		private PosOverrideCondition posOverrideCondition;
		private boolean modified;	// indicate whether override condition is modified
		
		OverrideConditionInfo(PosOverrideCondition oPosOverrideCondition, boolean bModified) {
			posOverrideCondition = oPosOverrideCondition;
			modified = bModified;
		}
	}
	private List<OverrideConditionInfo> m_oOverrideConditionInfoList;

	private int m_iCurrentPage;
	private int m_iTotalPage;
	
	public static final int MAX_RECORD_SIZE = 8;	// record show in each page

	public FormChangeOverrideCondition(Controller oParentController) {
		super(oParentController);
		m_oTemplateBuilder = new TemplateBuilder();
		m_oOverrideConditionInfoList = new ArrayList<OverrideConditionInfo>();
		m_iCurrentPage = 0;
		m_iTotalPage = 0;
		m_bSaveClicked = false;
	}
	
	public void init(List<PosOverrideCondition> oPosOverrideConditionList) {
		m_oTemplateBuilder.loadTemplate("frmChangeOverrideCondition.xml");

		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Check review frame
		m_oFrameChangeOverrideCondition = new FrameChangeOverrideCondition();
		m_oTemplateBuilder.buildFrame(m_oFrameChangeOverrideCondition, "fraChangeOverrideCondition");
		m_oFrameChangeOverrideCondition.addListener(this);
		this.attachChild(m_oFrameChangeOverrideCondition);
		
		// store override conditions and set modified to false
		for (int i = 0; i < oPosOverrideConditionList.size(); i++) {
			PosOverrideCondition oPosOverrideCondition = new PosOverrideCondition(oPosOverrideConditionList.get(i));
			OverrideConditionInfo oOverrideConditionInfo = new OverrideConditionInfo(oPosOverrideCondition, false);
			m_oOverrideConditionInfoList.add(oOverrideConditionInfo);
		}
		
		if (m_oOverrideConditionInfoList.size() > 0) {
			m_iCurrentPage = 1;
			
			m_iTotalPage = m_oOverrideConditionInfoList.size() / MAX_RECORD_SIZE;
			if (m_oOverrideConditionInfoList.size() % MAX_RECORD_SIZE > 0)
				m_iTotalPage++;

			showOverrideConditionsAtPage(m_iCurrentPage);
			m_oFrameChangeOverrideCondition.updatePageButton(m_iCurrentPage, m_iTotalPage);
		}
	}
	
	// update records of page
	private void showOverrideConditionsAtPage(int iPage) {
		int iStartIndex = (iPage - 1) * MAX_RECORD_SIZE;
		int iEndIndex = iPage * MAX_RECORD_SIZE;
		if (iEndIndex > m_oOverrideConditionInfoList.size())
			iEndIndex = m_oOverrideConditionInfoList.size();
		int iIndex = 0;
		for (int i = iStartIndex; i < iEndIndex; i++) {
			PosOverrideCondition oPosOverrideCondition = m_oOverrideConditionInfoList.get(i).posOverrideCondition;
			m_oFrameChangeOverrideCondition.addOverrideConditionRecord(iIndex, Integer.toString(oPosOverrideCondition.getPriority()), oPosOverrideCondition.getName(AppGlobal.g_oCurrentLangIndex.get()), oPosOverrideCondition.isActive());
			iIndex++;
		}
	}
	
	// change if any override condition statuses are changed, enable save button if yes
	private void updateSaveButtonEaabled() {
		boolean bConditionModified = false;
		for (OverrideConditionInfo oOverrideConditionInfo: m_oOverrideConditionInfoList) {
			if (oOverrideConditionInfo.modified) {
				bConditionModified = true;
				break;
			}
		}
		m_oFrameChangeOverrideCondition.setSaveButtonEnabled(bConditionModified);
	}
	
	public boolean isSaved() {
		return this.m_bSaveClicked;
	}

	@Override
	public void fraChangeOverrideCondition_clickExit() {
		this.finishShow();
	}

	@Override
	public void fraChangeOverrideCondition_activeStatusClicked(int iItemIndex) {
		int iIndex = (m_iCurrentPage - 1) * MAX_RECORD_SIZE + iItemIndex;
		
		// for system default override condition ("Waive Service Charge For Takeout Item", "Auto Waive Service Charge For Fast Food Mode"), it does not allow to change status
		if (m_oOverrideConditionInfoList.get(iIndex).posOverrideCondition.getOverId() == 0)
			return;
		
//KingsleyKwan20170918ByNick		-----Start-----
		// update modified field
		
		m_oOverrideConditionInfoList.get(iIndex).modified = !m_oOverrideConditionInfoList.get(iIndex).modified;
		
		// update override condition status to active
		if(m_oOverrideConditionInfoList.get(iIndex).posOverrideCondition.getStatus().equals(PosOverrideCondition.STATUS_ACTIVE))
		{
			m_oOverrideConditionInfoList.get(iIndex).posOverrideCondition.setStatus(PosOverrideCondition.STATUS_SUSPENDED);
			m_oFrameChangeOverrideCondition.changeActionStatusIcon(iItemIndex, FrameChangeOverrideCondition.STATUS_ICON_SUSPENDED);
		} else {
			m_oOverrideConditionInfoList.get(iIndex).posOverrideCondition.setStatus(PosOverrideCondition.STATUS_ACTIVE);
			m_oFrameChangeOverrideCondition.changeActionStatusIcon(iItemIndex, FrameChangeOverrideCondition.STATUS_ICON_ACTIVE);
		}
		// update status icon
		
//KingsleyKwan20170918ByNick		-----End-----
		updateSaveButtonEaabled();
	}
	
	@Override
	public void fraChangeOverrideCondition_prevPage() {
		m_iCurrentPage--;
		m_oFrameChangeOverrideCondition.clearOverrideConditionRecords();
		this.showOverrideConditionsAtPage(m_iCurrentPage);
		m_oFrameChangeOverrideCondition.updatePageButton(m_iCurrentPage, m_iTotalPage);
	}

	@Override
	public void fraChangeOverrideCondition_nextPage() {
		m_iCurrentPage++;
		m_oFrameChangeOverrideCondition.clearOverrideConditionRecords();
		this.showOverrideConditionsAtPage(m_iCurrentPage);
		m_oFrameChangeOverrideCondition.updatePageButton(m_iCurrentPage, m_iTotalPage);
	}

	@Override
	public void fraChangeOverrideCondition_clickSave() {
		List<PosOverrideCondition> oPosOverrideConditionList = new ArrayList<PosOverrideCondition>();
		for (OverrideConditionInfo oOverrideConditionInfo: m_oOverrideConditionInfoList) {
			if (oOverrideConditionInfo.modified)
				oPosOverrideConditionList.add(oOverrideConditionInfo.posOverrideCondition);
		}
		
		if (!oPosOverrideConditionList.isEmpty()) {
			PosOverrideCondition oPosOverrideCondition = new PosOverrideCondition();
			String sErrMsg = oPosOverrideCondition.addUpdate(oPosOverrideConditionList);
			if (!sErrMsg.isEmpty())
				AppGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), AppGlobal.g_oFuncStation.get().getStationId()+"", "", sErrMsg);
		}
		
		m_bSaveClicked = true;
		this.finishShow();
	}
}
