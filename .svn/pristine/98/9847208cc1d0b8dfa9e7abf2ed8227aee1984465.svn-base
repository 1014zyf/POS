package app.model;

import java.util.HashMap;

import org.json.JSONArray;

public class MemMemberList {

	private HashMap<Integer, MemMember> m_oMemberList;
	
	public MemMemberList(){
		m_oMemberList = new HashMap<Integer, MemMember>();
	}
	
	//read all function records
	public void searchMember(String sType, String sValue, int iPage, int iLimit) {
		MemMember oMemberList = new MemMember();
		JSONArray responseJSONArray = oMemberList.searchMember(sType, sValue, iPage, iLimit);
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				if (responseJSONArray.isNull(i))
					continue;
				
				MemMember oMemMember = new MemMember(responseJSONArray.optJSONObject(i));
				
				// Add to function list
				m_oMemberList.put(oMemMember.getMemberId(), oMemMember);
			}
		}
	}
	
	public HashMap<Integer, MemMember> getMemberList(){
		return m_oMemberList;
	}
}
