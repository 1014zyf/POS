package om;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class PosCoverDiscountList {
	private ArrayList<PosCoverDiscount> m_oCoverDiscountsList;
	
	public PosCoverDiscountList() {
		m_oCoverDiscountsList = new ArrayList<PosCoverDiscount>();
	}
	
	//read cover discounts
	public boolean readCoverDiscountsListByShopOutletSeq(int iShopId, int iOutletId, int iSeq, String sApplyTo) {
		boolean bResult = true;
		PosCoverDiscount oCoverDiscountsList = new PosCoverDiscount(), oCoverDiscounts = null;
		JSONArray oCoverDiscounsJSONArray = new JSONArray();
		
		oCoverDiscounsJSONArray = oCoverDiscountsList.readAllByShopOutletSeq(iShopId, iOutletId, iSeq, sApplyTo);
		if (oCoverDiscounsJSONArray != null) {
			for (int i = 0; i < oCoverDiscounsJSONArray.length(); i++) {
				try {
					oCoverDiscounts = new PosCoverDiscount(oCoverDiscounsJSONArray.getJSONObject(i));
					m_oCoverDiscountsList.add(oCoverDiscounts);
				}catch(JSONException jsone) {
					jsone.printStackTrace();
					bResult = false;
				}
			}
		}
		
		return bResult;
	}
		
	public ArrayList<PosCoverDiscount> getPosCoverDiscountList() {
		return this.m_oCoverDiscountsList;
	}
}
