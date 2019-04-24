package app.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosCheckDiscountList {
	private List<PosCheckDiscount> m_oCheckDiscountList;
	
	public PosCheckDiscountList(JSONArray oCheckDiscountJSONArray) {
		if (oCheckDiscountJSONArray == null)
			return;
		
		m_oCheckDiscountList = new ArrayList<PosCheckDiscount>();
		for (int i = 0; i < oCheckDiscountJSONArray.length(); i++) {
			if (oCheckDiscountJSONArray.isNull(i))
				continue;
			
			JSONObject oCheckDiscJSONObject = oCheckDiscountJSONArray.optJSONObject(i);
			PosCheckDiscount oCheckDiscount = new PosCheckDiscount(oCheckDiscJSONObject);
			m_oCheckDiscountList.add(oCheckDiscount);
		}
	}
	
	public PosCheckDiscountList(List<PosCheckDiscount> oCheckDiscList) {
		this.m_oCheckDiscountList = oCheckDiscList;
	}
	
	public void setCheckDiscountList(HashMap<Integer, PosCheckDiscount> oCheckDiscountHashMap, List<PosCheckDiscount> oCheckDiscountList) {
		this.m_oCheckDiscountList = oCheckDiscountList;
	}
	
	//construct the save request JSON
	protected JSONArray constructAddSaveJSONArray() {
		JSONArray addSaveJSONArray = new JSONArray();
		JSONObject tempAddSaveJSONObject = null;
		
		for(PosCheckDiscount oCheckDiscount:m_oCheckDiscountList) {
			if(oCheckDiscount.getCdisId() == 0)
				tempAddSaveJSONObject = oCheckDiscount.constructAddSaveJSON(false);
			else
				tempAddSaveJSONObject = oCheckDiscount.constructAddSaveJSON(true);
			addSaveJSONArray.put(tempAddSaveJSONObject);
		}
		
		return addSaveJSONArray;
	}
	
	public List<PosCheckDiscount> getCheckDiscountList() {
		return m_oCheckDiscountList;
	}
}
