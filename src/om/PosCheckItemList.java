package om;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosCheckItemList {
	private List<PosCheckItem> m_oCheckItemList;
	
	public PosCheckItemList(){
		m_oCheckItemList = new ArrayList<PosCheckItem>();
	}
	
	public PosCheckItemList(List<PosCheckItem> oCheckItemList) {
		m_oCheckItemList = oCheckItemList;
	}
	
	public PosCheckItemList(JSONArray oCheckItemJSONArray) {
		JSONObject oCheckItemJSONObject = null;
		
		m_oCheckItemList = new ArrayList<PosCheckItem>();
		for (int i = 0; i < oCheckItemJSONArray.length(); i++) {
			if (oCheckItemJSONArray.isNull(i))
				continue;
			
			if (!oCheckItemJSONArray.optJSONObject(i).isNull("PosCheckItem"))
				oCheckItemJSONObject = oCheckItemJSONArray.optJSONObject(i).optJSONObject("PosCheckItem");
			else
				oCheckItemJSONObject = oCheckItemJSONArray.optJSONObject(i);
			PosCheckItem oCheckItem = new PosCheckItem(oCheckItemJSONObject);
			m_oCheckItemList.add(oCheckItem);
		}
	}
	
	public PosCheckItemList(JSONObject oCheckItemJSONObject) {
		JSONArray oCheckItemJSONArray = null;
		
		oCheckItemJSONArray = oCheckItemJSONObject.optJSONArray("PosCheckItem");
		
		m_oCheckItemList = new ArrayList<PosCheckItem>();
		for (int i = 0; i < oCheckItemJSONArray.length(); i++) {
			if (oCheckItemJSONArray.isNull(i))
				continue;
			
			if (!oCheckItemJSONArray.optJSONObject(i).isNull("PosCheckItem"))
				oCheckItemJSONObject = oCheckItemJSONArray.optJSONObject(i).optJSONObject("PosCheckItem");
			else
				oCheckItemJSONObject = oCheckItemJSONArray.optJSONObject(i);
			PosCheckItem oCheckItem = new PosCheckItem(oCheckItemJSONObject);
			m_oCheckItemList.add(oCheckItem);
		}
	}
	
	//read void check items by checkid and status
	public void readCheckItemByCheckId(String sCheckId, String sStatus) {
		JSONObject oCheckItemJSONObject = null;
		PosCheckItem oPosCheckItem = new PosCheckItem();
		JSONArray oCheckItemJSONArray = oPosCheckItem.readCheckItemByCheckId(sCheckId, sStatus);
		if(oCheckItemJSONArray != null){
			for (int i = 0; i < oCheckItemJSONArray.length(); i++) {
				if (oCheckItemJSONArray.isNull(i))
					continue;
				
				if (!oCheckItemJSONArray.optJSONObject(i).isNull("PosCheckItem"))
					oCheckItemJSONObject = oCheckItemJSONArray.optJSONObject(i).optJSONObject("PosCheckItem");
				else
					oCheckItemJSONObject = oCheckItemJSONArray.optJSONObject(i);
				PosCheckItem oCheckItem = new PosCheckItem(oCheckItemJSONObject);
				m_oCheckItemList.add(oCheckItem);
			}
		}
	}
	
	//get check item list
	public List<PosCheckItem> getCheckItemList() {
		return this.m_oCheckItemList;
	}
}
