package app.model;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosPrintFormatList {
	private HashMap<Integer, PosPrintFormat> m_oPrintFormatList;
	
	public PosPrintFormatList() {
		m_oPrintFormatList = new HashMap<Integer, PosPrintFormat>();
	}
	
	public void addMultiPrintFormatToList(JSONArray oPrintFormatJSONArray) {
		m_oPrintFormatList = new HashMap<Integer, PosPrintFormat>();
		
		for (int i = 0; i < oPrintFormatJSONArray.length(); i++) {
			JSONObject oPrintFormatJSONObject = oPrintFormatJSONArray.optJSONObject(i);
			if (oPrintFormatJSONObject == null)
				continue;
			
			PosPrintFormat oPrintFormat = new PosPrintFormat(oPrintFormatJSONObject);
			m_oPrintFormatList.put(oPrintFormat.getPfmtId(), oPrintFormat);
		}
	}
	
	public void addPrintFormatToList(PosPrintFormat oPrintFormat) {
		m_oPrintFormatList.put(oPrintFormat.getPfmtId(), oPrintFormat);
	}
	
	public PosPrintFormat getPrintFormatByPfmtId(int iPfmtId) {
		return m_oPrintFormatList.get(iPfmtId);
	}

	public void clearPrintFormatList() {
		m_oPrintFormatList.clear();
	}
}
