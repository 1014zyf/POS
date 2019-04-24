package app.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosPaidIoReasonList {
	private List<PosPaidIoReason> m_oPaidIoReasonList;

	public PosPaidIoReasonList() {
		m_oPaidIoReasonList = new ArrayList<PosPaidIoReason>();
	}

	public PosPaidIoReasonList(JSONArray oPaidInOutReasonJSONArray) {
		JSONObject oPaidInOutReasonJSONObject = null;
		
		m_oPaidIoReasonList = new ArrayList<PosPaidIoReason>();
		for (int i = 0; i < oPaidInOutReasonJSONArray.length(); i++) {
			if (oPaidInOutReasonJSONArray.isNull(i))
				continue;
			
			if(!oPaidInOutReasonJSONArray.optJSONObject(i).isNull("PosPaidIoReason"))
				oPaidInOutReasonJSONObject = oPaidInOutReasonJSONArray.optJSONObject(i).optJSONObject("PosPaidIoReason");
			else
				oPaidInOutReasonJSONObject = oPaidInOutReasonJSONArray.optJSONObject(i);
			PosPaidIoReason oPaidInOutReason = new PosPaidIoReason(oPaidInOutReasonJSONObject);
			m_oPaidIoReasonList.add(oPaidInOutReason);
		}
	}

	//get all void reason
	public void readAll() {
		PosPaidIoReason oPaidInOutReason = new PosPaidIoReason();
		JSONArray responseJSONArray = oPaidInOutReason.readAll();
		
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				if (responseJSONArray.isNull(i))
					continue;
				JSONObject paidInOutReasonJSONObject = responseJSONArray.optJSONObject(i);
				PosPaidIoReason oTempPaidInOutReason = new PosPaidIoReason(paidInOutReasonJSONObject);
				m_oPaidIoReasonList.add(oTempPaidInOutReason);
			}
		}
		
	}

	// Get void reason by type
	public ArrayList<PosPaidIoReason> getPaidIoReasonListByType(String sType){
		ArrayList<PosPaidIoReason> oPaidInOutReasonList = new ArrayList<PosPaidIoReason>();
		
		for(PosPaidIoReason oPaidInOutReason: m_oPaidIoReasonList) {
			if(oPaidInOutReason.getType().equals(sType))
				oPaidInOutReasonList.add(oPaidInOutReason);
		}
		
		return oPaidInOutReasonList;
	}
	
	public List<PosPaidIoReason> getPaidIoReasonList() {
		return this.m_oPaidIoReasonList;
	}
}
