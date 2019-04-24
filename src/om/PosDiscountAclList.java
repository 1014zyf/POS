package om;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class PosDiscountAclList {
	private List<PosDiscountAcl> m_oPosDiscountAclList;
	
	public PosDiscountAclList() {
		m_oPosDiscountAclList = new ArrayList<PosDiscountAcl>();
	}
	
	public void readAllDiscountAclByOutlet(int iOutletId) {
		PosDiscountAcl oPosDiscountAclList = new PosDiscountAcl();
		JSONArray responseArray = null;
		
		responseArray = oPosDiscountAclList.readAllByOutletId(iOutletId);
		if(responseArray == null) 
			return;
		
		for (int i = 0; i < responseArray.length(); i++) {
			if (responseArray.isNull(i) || !responseArray.optJSONObject(i).has("PosDiscountAcl"))
				continue;
			
			PosDiscountAcl oPosDiscountAcl = new PosDiscountAcl(responseArray.optJSONObject(i).optJSONObject("PosDiscountAcl"));
			m_oPosDiscountAclList.add(oPosDiscountAcl);
		}
	}
	
	public List<PosDiscountAcl> getDiscountAclList() {
		return m_oPosDiscountAclList;
	}
}
