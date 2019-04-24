package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import core.Controller;
import om.UserUser;
import om.UserUserList;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormSearchUser extends VirtualUIForm implements FrameSearchUserListener {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameSearchUser m_oFrameSearchUser;
	private ArrayList<UserUser> m_oResultUserList;
	private int m_iEmployeeId;
	private String m_sEmployeeNum;
	private boolean m_bUserCancel;
	
	public FormSearchUser(Controller oParentController){
		super(oParentController);
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmSearchUser.xml");
		m_iEmployeeId = 0;
		m_bUserCancel = false;
		m_oResultUserList = new ArrayList<UserUser>();
	}
	
	public boolean init(String iAssignedUserId) {
				
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
		// Search User Frame
		m_oFrameSearchUser = new FrameSearchUser();
		m_oTemplateBuilder.buildFrame(m_oFrameSearchUser, "fraSearchUser");

		m_oFrameSearchUser.init(AppGlobal.g_oLang.get()._("employee_list"));
		// Add listener;
		m_oFrameSearchUser.addListener(this);
		this.attachChild(m_oFrameSearchUser);
		
		searchUser(iAssignedUserId);

		return true;
	}

	private void searchUser(String sValue) {
		UserUserList oUserList = new UserUserList();
		oUserList.searchUser(sValue);
		HashMap<Integer, UserUser> oResultUserList = oUserList.getUserList();
		
		int iIndex = 0;
		for (Entry<Integer, UserUser> entry:oResultUserList.entrySet()) {
			UserUser oUserUser = entry.getValue();
			m_oFrameSearchUser.addUserToResultList(0, iIndex, oUserUser.getNumber(), oUserUser.getFirstName(AppGlobal.g_oCurrentLangIndex.get())+" "+oUserUser.getLastName(AppGlobal.g_oCurrentLangIndex.get()));
			m_oResultUserList.add(oUserUser);
			
			iIndex++;
		}
	}	

	public int getEmployeeId(){
		return m_iEmployeeId;
	}
	
	public String getEmployeeNum() {
		return m_sEmployeeNum;
	}
	
	public boolean isUserCancel(){
		return m_bUserCancel;
	}
	
	@Override
	public void frameSearchUser_clickCancel() {
		m_bUserCancel = true;
				
		// Finish showing this form
		this.finishShow();
		
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}
	
	@Override
	public void frameSearchUser_clickSearchResultRecord(int iIndex) {
		// Show member detail
		m_oResultUserList.get(iIndex);

		m_iEmployeeId = m_oResultUserList.get(iIndex).getUserId();
		m_sEmployeeNum = m_oResultUserList.get(iIndex).getNumber();
		// Finish showing this form
		this.finishShow();
		
		// Hide the keyboard
		AppGlobal.g_oTerm.get().hideKeyboard();
	}
}
