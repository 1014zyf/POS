package om;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosUserDrawerOwnershipList {
	private List<PosUserDrawerOwnership> m_oUserDrawerOwnershipList;
	
	public PosUserDrawerOwnershipList() {
		m_oUserDrawerOwnershipList = new ArrayList<PosUserDrawerOwnership>();
	}
	
	public PosUserDrawerOwnershipList(JSONArray posUserDrawerOwnershipJSONArray) {
		m_oUserDrawerOwnershipList = new ArrayList<PosUserDrawerOwnership>();
		PosUserDrawerOwnership oPosUserDrawerOwnership = null;
		
		for (int i = 0; i < posUserDrawerOwnershipJSONArray.length(); i++) {
			if (posUserDrawerOwnershipJSONArray.isNull(i))
				continue;
		
			JSONObject oTmpJsonObject = posUserDrawerOwnershipJSONArray.optJSONObject(i);
			oPosUserDrawerOwnership = new PosUserDrawerOwnership(oTmpJsonObject);
			m_oUserDrawerOwnershipList.add(oPosUserDrawerOwnership);
		}
	}
	
	// Clear the User Drawer Ownership List
	public void clearList(){
		m_oUserDrawerOwnershipList.clear();
	}
	
	// Add drawer gateway transaction to list
	public void add(PosUserDrawerOwnership oPosUserDrawerOwnership){
		m_oUserDrawerOwnershipList.add(oPosUserDrawerOwnership);
	}
	
	//read all by User Id
	public void findAllByOutletId(int iOutletId){
		PosUserDrawerOwnership oDrawerOwnershipList = new PosUserDrawerOwnership();
		JSONArray responseJSONArray = oDrawerOwnershipList.readByOutletId(iOutletId);
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				if (responseJSONArray.isNull(i))
					continue;
				
				PosUserDrawerOwnership oPosUserDrawerOwnership = new PosUserDrawerOwnership(responseJSONArray.optJSONObject(i));
				
				// Add to function list
				m_oUserDrawerOwnershipList.add(oPosUserDrawerOwnership);
			}
		}
	}
	
	//read all by User Id
	public void findAllByConditions(int iOutletId, int iStationId, int iUserId){
		PosUserDrawerOwnership oDrawerOwnershipList = new PosUserDrawerOwnership();
		JSONArray responseJSONArray = oDrawerOwnershipList.readAllActiveByConditions(iOutletId, iStationId, iUserId);
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				if (responseJSONArray.isNull(i))
					continue;
				
				PosUserDrawerOwnership oPosUserDrawerOwnership = new PosUserDrawerOwnership(responseJSONArray.optJSONObject(i));
				
				// Add to function list
				m_oUserDrawerOwnershipList.add(oPosUserDrawerOwnership);
			}
		}
	}
	
	// Get payment gateway transactions by index
	public PosUserDrawerOwnership getPosUserDrawerOwnershipByIndex(int iIndex){
		return this.m_oUserDrawerOwnershipList.get(iIndex);
	}
	
	public List<PosUserDrawerOwnership> getPosUserDrawerOwnership(){
		return m_oUserDrawerOwnershipList;
	}
	
}
