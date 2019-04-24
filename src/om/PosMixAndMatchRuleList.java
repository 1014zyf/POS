package om;

import java.util.ArrayList;

import org.json.JSONArray;

public class PosMixAndMatchRuleList {

	private ArrayList<PosMixAndMatchRule> m_oMixAndMatchRuleList;
	
	public PosMixAndMatchRuleList(){
		m_oMixAndMatchRuleList = new ArrayList<PosMixAndMatchRule>();
	}
	
	//read all rule with all rule items
	public void readAll(int iShopId, int iOutletId) {
		PosMixAndMatchRule oPosMixAndMatchRuleList = new PosMixAndMatchRule(), oPosMixAndMatchRule = null;
		JSONArray responseJSONArray = oPosMixAndMatchRuleList.readAll(iShopId, iOutletId);
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				if (responseJSONArray.isNull(i))
					continue;
				
				oPosMixAndMatchRule = new PosMixAndMatchRule(responseJSONArray.optJSONObject(i));
				
				// Add to function list
				m_oMixAndMatchRuleList.add(oPosMixAndMatchRule);
			}
		}
	}
	
	public ArrayList<PosMixAndMatchRule> getRuleList(){
		return m_oMixAndMatchRuleList;
	}
	
}
