package om;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class MemMemberList {

	private HashMap<Integer, MemMember> m_oMemberList;
	private HashMap<Integer, MemMember> m_oLoyaltyMemberList;
	
	public MemMemberList(){
		m_oMemberList = new HashMap<Integer, MemMember>();
		m_oLoyaltyMemberList = new HashMap<Integer, MemMember>();
	}
	
	//read all function records
	public void searchMember(String sType, String sValue, String sSearchMemberType , int iPage, int iLimit, int iSearchStatus) {
		MemMember oMemberList = new MemMember();
		JSONArray responseJSONArray = oMemberList.searchMember(sType, sValue, sSearchMemberType, iPage, iLimit, iSearchStatus);
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
	
	//search loyalty member
	public void searchLoyaltyMember(int iInterfaceId, String sMemberNo, ArrayList<String> oCondition) {
		MemMember oMemberList = new MemMember();
		JSONArray responseJSONArray = oMemberList.searchLoyaltyMember(iInterfaceId, sMemberNo, oCondition);
		JSONObject oJSONObject = responseJSONArray.optJSONObject(0);
		if(oJSONObject != null) {
			responseJSONArray = oJSONObject.optJSONArray("members");
		}else
			responseJSONArray = null;
		
		
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				if (responseJSONArray.isNull(i))
					continue;
				
				MemMember oMemMember = new MemMember(responseJSONArray.optJSONObject(i));
				// Add to function list
				m_oLoyaltyMemberList.put(i, oMemMember);
			}
		}
	}
	
	public HashMap<Integer, MemMember> getMemberList(){
		return m_oMemberList;
	}
	
	public HashMap<Integer, MemMember> getLoyaltyMemberList(){
		return m_oLoyaltyMemberList;
	}
}
