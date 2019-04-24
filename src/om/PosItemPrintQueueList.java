package om;

import java.util.HashMap;

import org.json.JSONArray;

public class PosItemPrintQueueList {
	private HashMap<Integer, PosItemPrintQueue> m_oItemPrintQueueList;
	
	public PosItemPrintQueueList() {
		m_oItemPrintQueueList = new HashMap<Integer, PosItemPrintQueue>();
	}
	
	public boolean readItemQueueListByShopAndOutletId(int iShopId, int iOutletId) {
		PosItemPrintQueue oItemPrintQueueList = new PosItemPrintQueue();
		JSONArray oItemPrintQueueJSONArray = oItemPrintQueueList.readAllByShopAndOutletId(iShopId, iOutletId);
		
		this.readItemQueueListByJSONArray(oItemPrintQueueJSONArray);
		return true;
	}
	
	public void readItemQueueListByJSONArray(JSONArray oJsonArray) {
		if (oJsonArray == null)
			return;
		
		for (int i = 0; i < oJsonArray.length(); i++) {
			if (oJsonArray.isNull(i))
				continue;
			
			PosItemPrintQueue oItemPrintQueue = new PosItemPrintQueue(oJsonArray.optJSONObject(i));
			m_oItemPrintQueueList.put(oItemPrintQueue.getMenuItpqId(), oItemPrintQueue);
		}
	}
	
	public PosItemPrintQueue getItemPrintQueueByIndex(int iIndex){
		return this.m_oItemPrintQueueList.get(iIndex);
	}
	
	public HashMap<Integer, PosItemPrintQueue> getItemPrintQueueList() {
		return this.m_oItemPrintQueueList;
	}
}
