package om;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosCheckDiscountItemList {
	private List<PosCheckDiscountItem> m_oCheckDiscountItemList;
	
	public PosCheckDiscountItemList(JSONArray oCheckDiscItemJSONArray) {
		PosCheckDiscountItem oCheckDiscItem = null;
		JSONObject oCheckDiscItemJSONObject = null;
		
		m_oCheckDiscountItemList = new ArrayList<PosCheckDiscountItem>();
		for (int i = 0; i < oCheckDiscItemJSONArray.length(); i++) {
			if (oCheckDiscItemJSONArray.isNull(i))
				continue;
			
			if(!oCheckDiscItemJSONArray.optJSONObject(i).isNull("PosCheckDiscountItem"))
				oCheckDiscItemJSONObject = oCheckDiscItemJSONArray.optJSONObject(i).optJSONObject("PosCheckDiscountItem");
			else
				oCheckDiscItemJSONObject = oCheckDiscItemJSONArray.optJSONObject(i);
			oCheckDiscItem = new PosCheckDiscountItem(oCheckDiscItemJSONObject);
			m_oCheckDiscountItemList.add(oCheckDiscItem);
		}
	}
	
	public List<PosCheckDiscountItem> getCheckDiscountItemList() {
		return this.m_oCheckDiscountItemList;
	}
	
}
