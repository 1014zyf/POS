package om;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class PosAttributeTypeList {
private List<PosAttributeType> m_oAttributeTypeList;
	
	public PosAttributeTypeList() {
		m_oAttributeTypeList = new ArrayList<PosAttributeType>();
	}
	
	public void readAttributeTypesByType(String sType) {
		int i = 0;
		PosAttributeType oAttributeTypeList = new PosAttributeType(), oAttributeType = null;
		JSONArray oAttributeTypeJSONArray = new JSONArray();
		
		oAttributeTypeJSONArray = oAttributeTypeList.readAllByType(sType);
		if(oAttributeTypeJSONArray != null) {
			for(i=0; i<oAttributeTypeJSONArray.length(); i++) {
				try {
					oAttributeType = new PosAttributeType(oAttributeTypeJSONArray.getJSONObject(i));
					m_oAttributeTypeList.add(oAttributeType);
				}catch(JSONException jsone) {
					jsone.printStackTrace();
				}
			}
		}
	}
	
	public boolean attExist(int iCode) {
		boolean bFound = false;
		
		for (int i = 0; i < m_oAttributeTypeList.size() && bFound == false; i++) {
			List<HashMap<String, String>> oAttributeOptionList = m_oAttributeTypeList.get(i).getAttributeOptionList();
			for (int j = 0; j < oAttributeOptionList.size(); j++) {
				if (Integer.parseInt(oAttributeOptionList.get(j).get("attoId")) == iCode) {
					bFound = true;
					break;
				}
			}
		}
		return bFound;
	}
	
	public List<PosAttributeType> getAttributeTypeList() {
		return this.m_oAttributeTypeList;
	}
}
