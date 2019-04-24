package om;

import java.util.HashMap;

import org.json.JSONArray;

public class MenuItemPrintQueueList {
	private HashMap<Integer, MenuItemPrintQueue> m_oItemPrintQueueList;
	
	public MenuItemPrintQueueList() {
		this.m_oItemPrintQueueList = new HashMap<Integer, MenuItemPrintQueue>();
	}
	
	public boolean readItemQueueList() {
		MenuItemPrintQueue oItemPrintQueueList = new MenuItemPrintQueue();
		JSONArray oItemPrintQueueJSONArray = oItemPrintQueueList.readAll();
		if (oItemPrintQueueJSONArray != null) {
			for (int i = 0; i < oItemPrintQueueJSONArray.length(); i++) {
				if (oItemPrintQueueJSONArray.isNull(i))
					continue;
				
				MenuItemPrintQueue oItemPrintQueue = new MenuItemPrintQueue(oItemPrintQueueJSONArray.optJSONObject(i));
				m_oItemPrintQueueList.put(oItemPrintQueue.getItqpId(), oItemPrintQueue);
			}
		}
		
		return true;
	}
	
	public MenuItemPrintQueue getItemPrintQueueById(int iItpqId) {
		return this.m_oItemPrintQueueList.get(iItpqId);
	}
}
