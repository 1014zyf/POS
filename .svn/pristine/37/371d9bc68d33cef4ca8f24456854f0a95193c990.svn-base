package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import core.Controller;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

/** interface for the listeners/observers callback method */
interface FormMemberBonusRedemptionListener {
	void formMemberBonusRedemption_addBenefit(FuncBenefit oFuncBenefit);
}

public class FormMemberBonusRedemption extends VirtualUIForm implements FrameMemberBonusRedemptionListener {
	private class BenefitInfo {
		FuncBenefit funcBenefit;
		int selectCount;
		
		BenefitInfo(FuncBenefit oFuncBenefit, int iSelectCount) {
			funcBenefit = oFuncBenefit;
			selectCount = iSelectCount;
		}
	}
	
	private TemplateBuilder m_oTemplateBuilder;
	
	private FrameMemberBonusRedemption m_oFrameMemberBonusRedemption;
	private LinkedHashMap<String, String> m_oMemberInfoList;
	private List<BenefitInfo> m_oBenefitInfoList;

	private int m_iCurrentPage;
	private int m_iTotalPage;
	
	private int m_iBonusBalance;
	private int m_iCurrentBonusUsed;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FormMemberBonusRedemptionListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FormMemberBonusRedemptionListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FormMemberBonusRedemptionListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public static final int MAX_RECORD_SIZE = 10;	// record show in each page

	public FormMemberBonusRedemption(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FormMemberBonusRedemptionListener>();
		m_oMemberInfoList = new LinkedHashMap<String, String>();
		m_oBenefitInfoList = new ArrayList<BenefitInfo>();
		m_iCurrentPage = 0;
		m_iTotalPage = 0;
		m_iBonusBalance = 0;
		m_iCurrentBonusUsed = 0;

		m_oMemberInfoList.put(AppGlobal.g_oLang.get()._("member_name"), "");
		m_oMemberInfoList.put(AppGlobal.g_oLang.get()._("campaign_name"), "");
		m_oMemberInfoList.put(AppGlobal.g_oLang.get()._("bonus_balance"), "");
	}
	
	public void init(String sMemberName, String sCampaignName, int iBonusBalance, List<FuncBenefit> oBenefitList, HashMap<String, Integer> oBenefitItemQtyList) {
		m_oTemplateBuilder.loadTemplate("frmMemberBonusRedemption.xml");

		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Check review frame
		m_oFrameMemberBonusRedemption = new FrameMemberBonusRedemption();
		m_oTemplateBuilder.buildFrame(m_oFrameMemberBonusRedemption, "fraMemberBonusRedemption");
		m_oFrameMemberBonusRedemption.addListener(this);
		this.attachChild(m_oFrameMemberBonusRedemption);

		m_iBonusBalance = iBonusBalance;
		setBenefitList(sMemberName, sCampaignName, oBenefitList);
		setBenefitItemQtyList(oBenefitItemQtyList);
		checkBenefitAvailability(m_iCurrentPage);
	}
	
	private void setBenefitList(String sMemberName, String sCampaignName, List<FuncBenefit> oBenefitList) {
		m_oMemberInfoList.put(AppGlobal.g_oLang.get()._("member_name"), sMemberName);
		m_oMemberInfoList.put(AppGlobal.g_oLang.get()._("campaign_name"), sCampaignName);
		m_oMemberInfoList.put(AppGlobal.g_oLang.get()._("bonus_balance"), 0+"/"+m_iBonusBalance);
		m_oFrameMemberBonusRedemption.addBonusInfo(m_oMemberInfoList);

		for (FuncBenefit oFuncBenefit: oBenefitList) {
			if (oFuncBenefit.isFailCondition())
				continue;
			
			if (oFuncBenefit.getMaxCount() > 0 && oFuncBenefit.getUseCount() >= oFuncBenefit.getMaxCount())
				continue;
			
			BenefitInfo oBenefitInfo = new BenefitInfo(oFuncBenefit, 0);
			m_oBenefitInfoList.add(oBenefitInfo);
		}
		
		if (m_oBenefitInfoList.size() > 0) {
			m_iCurrentPage = 1;
			
			m_iTotalPage = m_oBenefitInfoList.size() / MAX_RECORD_SIZE;
			if (m_oBenefitInfoList.size() % MAX_RECORD_SIZE > 0)
				m_iTotalPage++;

			showBenefitListAtPage(m_iCurrentPage);
			m_oFrameMemberBonusRedemption.updatePageButton(m_iCurrentPage, m_iTotalPage);
		}
	}
	
	// update records of page
	public void showBenefitListAtPage(int iPage) {
		int iStartIndex = (iPage - 1) * MAX_RECORD_SIZE;
		int iEndIndex = iPage * MAX_RECORD_SIZE;
		if (iEndIndex > m_oBenefitInfoList.size())
			iEndIndex = m_oBenefitInfoList.size();
		int iIndex = 0;
		for (int i = iStartIndex; i < iEndIndex; i++) {
			BenefitInfo oBenefitInfo = m_oBenefitInfoList.get(i);
			FuncBenefit oFuncBenefit = oBenefitInfo.funcBenefit;
			m_oFrameMemberBonusRedemption.addBonusToBasket(iIndex, oFuncBenefit.getCode(), oFuncBenefit.getDesc(), Integer.toString(oFuncBenefit.getMaxCount()), Integer.toString(oFuncBenefit.getBonusDebit()));
			if (oFuncBenefit.getBonusDebit() + m_iCurrentBonusUsed >= m_iBonusBalance)
				m_oFrameMemberBonusRedemption.setActionButtonEnabled(i, false);
			iIndex++;
		}
	}
	
	public void setBenefitItemQtyList(HashMap<String, Integer> oBenefitItemQtyList) {
		for (Entry<String, Integer> entry: oBenefitItemQtyList.entrySet()) {
			for (int i = 0; i < m_oBenefitInfoList.size(); i++) {
				BenefitInfo oBenefitInfo = m_oBenefitInfoList.get(i);
				if (oBenefitInfo.funcBenefit.getCode().equals(entry.getKey())) {
					oBenefitInfo.selectCount += entry.getValue();
					break;
				}
			}
		}
	}
	
	public void checkBenefitAvailability(int iPage) {
		m_iCurrentBonusUsed = 0;
		for (BenefitInfo oBenefitInfo: m_oBenefitInfoList)
			m_iCurrentBonusUsed += oBenefitInfo.funcBenefit.getBonusDebit() * oBenefitInfo.selectCount;
		
		m_oMemberInfoList.put(AppGlobal.g_oLang.get()._("bonus_balance"), m_iCurrentBonusUsed+"/"+m_iBonusBalance);
		m_oFrameMemberBonusRedemption.addBonusInfo(m_oMemberInfoList);

		int iStartIndex = (iPage - 1) * MAX_RECORD_SIZE;
		int iEndIndex = iPage * MAX_RECORD_SIZE;
		if (iEndIndex > m_oBenefitInfoList.size())
			iEndIndex = m_oBenefitInfoList.size();
		int iIndex = 0;
		for (int i = iStartIndex; i < iEndIndex; i++) {
			BenefitInfo oBenefitInfo = m_oBenefitInfoList.get(i);
			// Already reach available count, block the button to click
			// Or, remain bonus is not enough to add some benefit
			if ((oBenefitInfo.selectCount > 0 && oBenefitInfo.selectCount >= oBenefitInfo.funcBenefit.getMaxCount())
					|| oBenefitInfo.funcBenefit.getBonusDebit() + m_iCurrentBonusUsed > m_iBonusBalance)
				m_oFrameMemberBonusRedemption.setActionButtonEnabled(iIndex, false);
			
			m_oFrameMemberBonusRedemption.setAvailableCount(iIndex, Integer.toString(oBenefitInfo.funcBenefit.getMaxCount() - oBenefitInfo.selectCount));
			iIndex++;
		}
	}
	
	public void addBenefitItemSelectCount(String sBenefitCode, int oQty) {
		for (int i = 0; i < m_oBenefitInfoList.size(); i++) {
			BenefitInfo oBenefitInfo = m_oBenefitInfoList.get(i);
			if (oBenefitInfo.funcBenefit.getCode().equals(sBenefitCode)) {
				oBenefitInfo.selectCount += oQty;
				break;
			}
		}
		
		checkBenefitAvailability(m_iCurrentPage);
	}

	@Override
	public void frameMemberBonusRedemption_clickCancel() {
	}

	@Override
	public void frameMemberBonusRedemption_clickConfirm() {
		
	}

	@Override
	public void frameMemberBonusRedemption_clickExit() {
		this.finishShow();
	}

	@Override
	public void frameMemberBonusRedemption_addBenefit(int iItemIndex) {
		int iStartIndex = (m_iCurrentPage - 1) * MAX_RECORD_SIZE;
		BenefitInfo oBenefitInfo = m_oBenefitInfoList.get(iStartIndex+iItemIndex);
		
		for (FormMemberBonusRedemptionListener listener: listeners)
			listener.formMemberBonusRedemption_addBenefit(oBenefitInfo.funcBenefit);
	}

	@Override
	public void fraMemberBonusRedemption_prevPage() {
		m_iCurrentPage--;
		m_oFrameMemberBonusRedemption.clearOverrideConditionRecords();
		this.showBenefitListAtPage(m_iCurrentPage);
		this.checkBenefitAvailability(m_iCurrentPage);
		m_oFrameMemberBonusRedemption.updatePageButton(m_iCurrentPage, m_iTotalPage);
	}

	@Override
	public void fraMemberBonusRedemption_nextPage() {
		m_iCurrentPage++;
		m_oFrameMemberBonusRedemption.clearOverrideConditionRecords();
		this.showBenefitListAtPage(m_iCurrentPage);
		this.checkBenefitAvailability(m_iCurrentPage);
		m_oFrameMemberBonusRedemption.updatePageButton(m_iCurrentPage, m_iTotalPage);
	}
	
}
