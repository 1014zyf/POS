package app;

import org.json.JSONObject;
import core.Controller;
import om.MemMember;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormLoyaltySearchMember extends VirtualUIForm implements FrameLoyaltySearchMemberListener {
	private TemplateBuilder m_oTemplateBuilder;	
	private FrameLoyaltySearchMember m_oFrameLoyaltySearchMember;
		
	public FormLoyaltySearchMember(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();		
		m_oTemplateBuilder.loadTemplate("frmLoyaltySearchMember.xml");
		
	}
	
	public boolean init(FuncCheck mFuncCheck, JSONObject oSearchSetupJSONObject) {
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Search Member Frame
		m_oFrameLoyaltySearchMember = new FrameLoyaltySearchMember();
		m_oTemplateBuilder.buildFrame(m_oFrameLoyaltySearchMember, "fraLoyaltySearchMember");

		m_oFrameLoyaltySearchMember.init(AppGlobal.g_oLang.get()._("loyalty_search_member"), oSearchSetupJSONObject);

		// Add listener;
		m_oFrameLoyaltySearchMember.addListener(this);
		this.attachChild(m_oFrameLoyaltySearchMember);
		
		return true;
	}

	private void showMemberDetail(int iIndex){
		if(iIndex >= m_oFrameLoyaltySearchMember.getLoyaltyMemberList().getLoyaltyMemberList().size())
			return;
		
		MemMember oMember = null;
		oMember = m_oFrameLoyaltySearchMember.getLoyaltyMemberList().getLoyaltyMemberList().get(iIndex);
		
		m_oFrameLoyaltySearchMember.showMemberDetail(oMember);
		}

	@Override
	public void frameSearchMemberFunction_clickCancel() {
		// Finish showing this form
		this.finishShow();
	}
	
	@Override
	public void frameSearchMemberFunction_clickSearchResultRecord(int iIndex) {
		// Show member detail
		showMemberDetail(iIndex);
	}

}
