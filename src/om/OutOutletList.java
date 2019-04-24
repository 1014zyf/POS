package om;

import java.util.ArrayList;

import org.json.JSONArray;

public class OutOutletList {
	private ArrayList<OutOutlet> m_oOutletList;

	public OutOutletList(){
		m_oOutletList = new ArrayList<OutOutlet>();
	}
	
	//read all function records
	public void readAll() {
		OutOutlet oOutletList = new OutOutlet();
		JSONArray responseJSONArray = null;
		
		responseJSONArray = oOutletList.readAll(-1);
		if(responseJSONArray == null)
			return;
			
		for(int i = 0; i < responseJSONArray.length(); i++) {
			if (responseJSONArray.isNull(i))
				continue;
			
			OutOutlet oOutlet = new OutOutlet(responseJSONArray.optJSONObject(i));
			
			// Add to function list
			m_oOutletList.add(oOutlet);
		}
	}
	
	//read all function records
	public void readPosOutletsByShopId(int iShopId) {
		OutOutlet oOutletList = new OutOutlet();
		JSONArray responseJSONArray = null;
		
		responseJSONArray = oOutletList.readPosOutletsByShopId(iShopId);
		if(responseJSONArray == null)
			return;
			
		for(int i = 0; i < responseJSONArray.length(); i++) {
			if (responseJSONArray.isNull(i))
				continue;
			
			OutOutlet oOutlet = new OutOutlet(responseJSONArray.optJSONObject(i));
			
			// Add to function list
			m_oOutletList.add(oOutlet);
		}
	}
	
	public ArrayList<OutOutlet> getOutletList() {
		return m_oOutletList;
	}
}
