//Database: pos_tax_sc_types -Tax & S.C. types
package app.model;

import java.util.HashMap;

import org.json.JSONArray;

public class PosTaxScTypeList {
	private HashMap<Integer, PosTaxScType> m_oPosTaxScTypeList;
	
	//init object with initialize value
	public PosTaxScTypeList () {
		m_oPosTaxScTypeList = new HashMap<Integer, PosTaxScType>();
	}
	
	// Get all taxes
	public void getAllTaxes() {
		PosTaxScType oPosTaxScTypeList = new PosTaxScType(), oPosTaxScType = null;
		JSONArray responseJSONArray = oPosTaxScTypeList.getAllTaxes();
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				if (responseJSONArray.isNull(i) || !responseJSONArray.optJSONObject(i).has("PosTaxScType"))
					continue;
				oPosTaxScType = new PosTaxScType(responseJSONArray.optJSONObject(i).optJSONObject("PosTaxScType"));
				m_oPosTaxScTypeList.put(oPosTaxScType.getNumber(), oPosTaxScType);
			}
		}
	}
	
	// Get all SCs
	public void getAllServiceCharges() {
		PosTaxScType oPosTaxScTypeList = new PosTaxScType(), oPosTaxScType = null;
		JSONArray responseJSONArray = oPosTaxScTypeList.getAllServiceCharges();
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				if (responseJSONArray.isNull(i) || !responseJSONArray.optJSONObject(i).has("PosTaxScType"))
					continue;
				oPosTaxScType = new PosTaxScType(responseJSONArray.optJSONObject(i).optJSONObject("PosTaxScType"));
				m_oPosTaxScTypeList.put(oPosTaxScType.getNumber(), oPosTaxScType);
			}
		}
	}
	
	public HashMap<Integer, PosTaxScType> getTaxScTypeList() {
		return this.m_oPosTaxScTypeList;
	}
}
