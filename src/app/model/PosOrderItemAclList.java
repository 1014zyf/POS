package app.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

public class PosOrderItemAclList {
	private ArrayList<PosOrderItemAcl> m_oPosOrderItemAclList;
	
	public PosOrderItemAclList() {
		m_oPosOrderItemAclList = null;
	}
	
	//get the list of order item acls
	public void readAllOrderItemAcl() {
		if(m_oPosOrderItemAclList != null)
			return;
		
		m_oPosOrderItemAclList = new ArrayList<PosOrderItemAcl>();
		PosOrderItemAcl oPosOrderItemAcl = new PosOrderItemAcl();
		JSONArray responseJSONArray = oPosOrderItemAcl.readAll();
		if (responseJSONArray == null)
			return;
		for (int i = 0; i < responseJSONArray.length(); i++) {
			if (responseJSONArray.isNull(i) || !responseJSONArray.optJSONObject(i).has("PosOrderItemAcl"))
				continue;
			
			oPosOrderItemAcl = new PosOrderItemAcl(responseJSONArray.optJSONObject(i).optJSONObject("PosOrderItemAcl"));
			m_oPosOrderItemAclList.add(oPosOrderItemAcl);
		}
	}
	
	//get the list of order item acls according to outlet id, user group id and order item group id
	public ArrayList<PosOrderItemAcl> getOrderItemAcls(int iOutletId, int iUserGrpId, int iOrderItemGrpId) {
		int i=0;
		ArrayList<PosOrderItemAcl> oFinalPermissionRules = new ArrayList<PosOrderItemAcl>();
		HashMap<Integer, PosOrderItemAcl> oOrderItemPermissionRules = new HashMap<Integer, PosOrderItemAcl>();
		if(m_oPosOrderItemAclList.size() == 0)
			return oFinalPermissionRules;
		
		for(i=1; i<=8; i++)
			oOrderItemPermissionRules.put(i, null);
		
		for(i=0; i<m_oPosOrderItemAclList.size(); i++) {
			PosOrderItemAcl oPosOrderItemAcl = m_oPosOrderItemAclList.get(i);
			
			if (oPosOrderItemAcl.getOletId() == iOutletId && oPosOrderItemAcl.getUgrpId() == iUserGrpId && oPosOrderItemAcl.getOigpId() == iOrderItemGrpId) 
				oOrderItemPermissionRules.put(1, oPosOrderItemAcl);
			else if (oPosOrderItemAcl.getOletId() == 0 && oPosOrderItemAcl.getUgrpId() == iUserGrpId && oPosOrderItemAcl.getOigpId() == iOrderItemGrpId)
				oOrderItemPermissionRules.put(2, oPosOrderItemAcl);
			else if (oPosOrderItemAcl.getOletId() == iOutletId && oPosOrderItemAcl.getUgrpId() == iUserGrpId && oPosOrderItemAcl.getOigpId() == 0)
				oOrderItemPermissionRules.put(3, oPosOrderItemAcl);
			else if (oPosOrderItemAcl.getOletId() == 0 && oPosOrderItemAcl.getUgrpId() == iUserGrpId && oPosOrderItemAcl.getOigpId() == 0)
				oOrderItemPermissionRules.put(4, oPosOrderItemAcl);
			else if (oPosOrderItemAcl.getOletId() == iOutletId && oPosOrderItemAcl.getUgrpId() == 0 && oPosOrderItemAcl.getOigpId() == iOrderItemGrpId)
				oOrderItemPermissionRules.put(5, oPosOrderItemAcl);
			else if (oPosOrderItemAcl.getOletId() == 0 && oPosOrderItemAcl.getUgrpId() == 0 && oPosOrderItemAcl.getOigpId() == iOrderItemGrpId)
				oOrderItemPermissionRules.put(6, oPosOrderItemAcl);
			else if (oPosOrderItemAcl.getOletId() == iOutletId && oPosOrderItemAcl.getUgrpId() == 0 && oPosOrderItemAcl.getOigpId() == 0)
				oOrderItemPermissionRules.put(7, oPosOrderItemAcl);
			else if (oPosOrderItemAcl.getOletId() == 0 && oPosOrderItemAcl.getUgrpId() == 0 && oPosOrderItemAcl.getOigpId() == 0)
				oOrderItemPermissionRules.put(8, oPosOrderItemAcl);
		}
		
		for(i=1; i<=8; i++) {
			if(oOrderItemPermissionRules.get(i) != null)
				oFinalPermissionRules.add(oOrderItemPermissionRules.get(i));
		}
		
		return oFinalPermissionRules;
	}
}
