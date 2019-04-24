package om;

import java.util.HashMap;

import org.json.JSONArray;

public class PrtPrintQueueList {
	HashMap <Integer, PrtPrintQueue> oPrintQueueList;
	
	public PrtPrintQueueList(){
		oPrintQueueList = new HashMap <Integer,PrtPrintQueue>();
	}
	
	public void readAll() {
		PrtPrintQueue oPrtPrintQueueList = new PrtPrintQueue();
		PrtPrintQueue oTempPrintQueue = null;
		JSONArray responseJSONArray = null;
		
		responseJSONArray = oPrtPrintQueueList.readAllActive();
		
		if(responseJSONArray == null)
			return;
		for(int i = 0; i < responseJSONArray.length(); i++) {
			if(responseJSONArray.isNull(i))
				continue;
			oTempPrintQueue = new PrtPrintQueue(responseJSONArray.optJSONObject(i));
			if (!oPrintQueueList.containsKey(oTempPrintQueue.getPrtPrintQueueId())) 
				oPrintQueueList.put(oTempPrintQueue.getPrtPrintQueueId(), oTempPrintQueue);
		}
	}
	
	public HashMap <Integer, PrtPrintQueue> readPrintQueueList(){
		return oPrintQueueList;
	}
}
