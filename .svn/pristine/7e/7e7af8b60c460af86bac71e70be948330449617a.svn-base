package om;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class PosItemRemindRuleList {
private List<PosItemRemindRule> m_oItemRemindRuleList;
	
	public PosItemRemindRuleList() {
		m_oItemRemindRuleList = new ArrayList<PosItemRemindRule>();
	}
	
	public void readItemRemindRulesByShopAndOutletId(int iShopId, int iOutletId) {
		//int i = 0;
		PosItemRemindRule oItemRemindRuleList = new PosItemRemindRule();//, oItemRemindRule = null;
		JSONArray oItemRemindRuleJSONArray = new JSONArray();
		
		oItemRemindRuleJSONArray = oItemRemindRuleList.readAllByShopAndOutletId(iShopId, iOutletId);
		this.readItemRemindRulesByJSONArray(oItemRemindRuleJSONArray);
	}
	
	public void readItemRemindRulesByJSONArray(JSONArray oJsonArray) {
		PosItemRemindRule oItemRemindRule = null;
		if (oJsonArray == null)
			return;
		
		for(int i = 0 ; i < oJsonArray.length() ; i++) {
			try {
				oItemRemindRule = new PosItemRemindRule(oJsonArray.getJSONObject(i));
				m_oItemRemindRuleList.add(oItemRemindRule);
			}catch(JSONException jsone) {
				jsone.printStackTrace();
			}
		}
	}
	
	public List<PosItemRemindRule> getItemRemindRuleList() {
		return this.m_oItemRemindRuleList;
	}
}