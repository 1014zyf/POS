package om;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class PosCoverDiscountMappingList {
	private ArrayList<PosCoverDiscountMapping> m_oMappingsList;
	
	public PosCoverDiscountMappingList() {
		m_oMappingsList = new ArrayList<PosCoverDiscountMapping>();
	}
	
	//read cover discounts
	public boolean readAllMappingsByCoverDiscountId(int iCoverDiscountId, int iCover) {
		boolean bResult = true;
		PosCoverDiscountMapping oMappingsList = new PosCoverDiscountMapping(), oCoverDiscounts = null;
		JSONArray oMappingsJSONArray = new JSONArray();
		
		oMappingsJSONArray = oMappingsList.readAllMappingsByCoverDiscountId(iCoverDiscountId, iCover);
		if (oMappingsJSONArray != null) {
			for (int i = 0; i < oMappingsJSONArray.length(); i++) {
				try {
					oCoverDiscounts = new PosCoverDiscountMapping(oMappingsJSONArray.getJSONObject(i));
					m_oMappingsList.add(oCoverDiscounts);
				} catch(JSONException jsone) {
					jsone.printStackTrace();
					bResult = false;
				}
			}
		}
		return bResult;
	}
	
	public ArrayList<PosCoverDiscountMapping> getPosCoverDiscountMappingList() {
		return this.m_oMappingsList;
	}
}
