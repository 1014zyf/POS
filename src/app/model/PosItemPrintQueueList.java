package app.model;

import java.util.HashMap;

import org.json.JSONArray;

public class PosItemPrintQueueList {
	private HashMap<Integer, PosItemPrintQueue> m_oItemPrintQueueList;
	
	public PosItemPrintQueueList() {
		m_oItemPrintQueueList = new HashMap<Integer, PosItemPrintQueue>();
	}
	
	public boolean readItemQueueListByShopAndOutletId(int iShopId, int iOutletId) {
		PosItemPrintQueue oItemPrintQueueList = new PosItemPrintQueue(), oItemPrintQueue = null;
		JSONArray oItemPrintQueueJSONArray = oItemPrintQueueList.readAllByShopAndOutletId(iShopId, iOutletId);
		if (oItemPrintQueueJSONArray != null) {
			for (int i = 0; i < oItemPrintQueueJSONArray.length(); i++) {
				if (oItemPrintQueueJSONArray.isNull(i))
					continue;
				
				oItemPrintQueue = new PosItemPrintQueue(oItemPrintQueueJSONArray.optJSONObject(i));
				m_oItemPrintQueueList.put(oItemPrintQueue.getMenuItpqId(), oItemPrintQueue);
			}
		}
		
		return true;
	}
	
	public PosItemPrintQueue getItemPrintQueueByIndex(int iIndex){
		return this.m_oItemPrintQueueList.get(iIndex);
	}
	
	public HashMap<Integer, PosItemPrintQueue> getItemPrintQueueList() {
		return this.m_oItemPrintQueueList;
	}
}
