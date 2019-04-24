package om;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosCheckTaxScRefList {
	private ArrayList<PosCheckTaxScRef> m_oCheckTaxScRefList;
	
	public PosCheckTaxScRefList(JSONArray oTaxScRefJSONArray) {
		if (oTaxScRefJSONArray == null)
			return;
		
		m_oCheckTaxScRefList = new ArrayList<PosCheckTaxScRef>();
		for (int i = 0; i < oTaxScRefJSONArray.length(); i++) {
			if (oTaxScRefJSONArray.isNull(i))
				continue;
			
			JSONObject oCheckTaxScRefJSONObject = oTaxScRefJSONArray.optJSONObject(i);
			PosCheckTaxScRef oCheckTaxScRef = new PosCheckTaxScRef(oCheckTaxScRefJSONObject);
			m_oCheckTaxScRefList.add(oCheckTaxScRef);
		}
	}
	
	public ArrayList<PosCheckTaxScRef> getCheckTaxScRefList() {
		return m_oCheckTaxScRefList;
	}
}
