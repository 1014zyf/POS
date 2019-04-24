package om;

import java.util.HashMap;

import org.json.JSONArray;

public class OutTableSectionList {
	private HashMap<Integer, OutTableSection> m_oSectionList;
	
	public OutTableSectionList() {
		m_oSectionList = new HashMap<Integer, OutTableSection>();
	}
	
	//read all function records
	public void getAllTableSections(int iOutletId) {
		OutTableSection oOutTableSectionsList = new OutTableSection();
		JSONArray responseJSONArray = oOutTableSectionsList.getAllSections(iOutletId);
		if (responseJSONArray != null) {
			for (int i=0; i<responseJSONArray.length(); i++) {
				if (responseJSONArray.isNull(i))
					continue;
				
				OutTableSection oOutTableSections = new OutTableSection(responseJSONArray.optJSONObject(i));
				m_oSectionList.put(oOutTableSections.getSectId(), oOutTableSections);
			}
		}
	}
	
	public void getAllTableSectionsByJSONArray(JSONArray oJSONArray) {
		if (oJSONArray != null) {
			for (int i = 0; i < oJSONArray.length(); i++) {
				if (oJSONArray.isNull(i))
					continue;
				
				OutTableSection oOutTableSections = new OutTableSection(oJSONArray.optJSONObject(i));
				m_oSectionList.put(oOutTableSections.getSectId(), oOutTableSections);
			}
		}
	}
	
	public HashMap<Integer, OutTableSection> getSectionsList() {
		return m_oSectionList;
	}
}
