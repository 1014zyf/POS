package om;

import java.util.HashMap;

import org.json.JSONArray;

public class PosCustomTypeList {
	private HashMap<Integer, PosCustomType> m_oCustomTypeList;
	
	public PosCustomTypeList() {
		m_oCustomTypeList = new HashMap<Integer, PosCustomType>();
	}
	
	//read all function records
	public void getCustomTypesByType(String sType) {
		PosCustomType oPosCustomTypeList = new PosCustomType();
		JSONArray responseJSONArray = oPosCustomTypeList.getCustomTypesByType(sType);	
		if (responseJSONArray != null) {
			for (int i=0; i<responseJSONArray.length(); i++) {
				if (responseJSONArray.isNull(i))
					continue;
				
				PosCustomType oPosCustomTypes = new PosCustomType(responseJSONArray.optJSONObject(i));	
				m_oCustomTypeList.put(oPosCustomTypes.getCtypId(), oPosCustomTypes);
			}
		}
	}
	
	public HashMap<Integer, PosCustomType> getTypeList() {
		return m_oCustomTypeList;
	}
}
