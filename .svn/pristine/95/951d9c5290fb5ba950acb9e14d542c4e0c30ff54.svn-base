package om;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

public class PosStockTransactionList {

	private HashMap<String, PosStockTransaction> m_oPosStockTransactionList;
	
	public PosStockTransactionList(){
		m_oPosStockTransactionList = new HashMap<String, PosStockTransaction>();
	}
	
	public void addPosStockTransaction(String sTransactionId, PosStockTransaction oPosStockTransaction) {
		m_oPosStockTransactionList.put(sTransactionId, oPosStockTransaction);
	}
	
	//read all stock transaction records
	public void searchStockTransaction(String sBusinessDayId, int iOutletId, ArrayList<Integer> oItemIds, String sStatus) {
		PosStockTransaction oPosStockTransactionList = new PosStockTransaction(), oPosStockTransaction = null;
		JSONArray responseJSONArray = oPosStockTransactionList.searchStockTransaction(sBusinessDayId, iOutletId, oItemIds, sStatus);
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				if (responseJSONArray.isNull(i))
					continue;
				
				JSONArray oJSONArray = responseJSONArray.optJSONArray(i);
				for (int j = 0; j < oJSONArray.length(); j++) {
					if (oJSONArray.isNull(j))
						continue;
					
					oPosStockTransaction = new PosStockTransaction(oJSONArray.optJSONObject(j));
					
					// Add to function list
					m_oPosStockTransactionList.put(oPosStockTransaction.getTransactionId(), oPosStockTransaction);
				}
			}
		}
	}
	
	//read stock transaction records by type
	public void searchStockTransactionByItemIdAndType(String sBusinessDayId, int iOutletId, ArrayList<Integer> oItemIds, String sType) {
		PosStockTransaction oPosStockTransactionList = new PosStockTransaction(), oPosStockTransaction = null;
		JSONArray responseJSONArray = oPosStockTransactionList.searchStockTransactionByItemIdAndType(sBusinessDayId, iOutletId, oItemIds, sType);
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				JSONArray oJSONArray = responseJSONArray.optJSONArray(i);
				if (oJSONArray == null)
					continue;
				
				for (int j = 0; j < oJSONArray.length(); j++) {
					if (oJSONArray.isNull(j))
						continue;
					oPosStockTransaction = new PosStockTransaction(oJSONArray.optJSONObject(j));
					
					// Add to function list
					m_oPosStockTransactionList.put(oPosStockTransaction.getTransactionId(), oPosStockTransaction);
				}
			}
		}
	}
	
	//read stock transaction records by type
	public void searchStockTransactionByType(String sBusinessDayId, int iOutletId, String sType, String sDateMode) {
		PosStockTransaction oPosStockTransactionList = new PosStockTransaction(), oPosStockTransaction = null;
		JSONArray responseJSONArray = oPosStockTransactionList.searchStockTransactionByType(sBusinessDayId, iOutletId, sType, sDateMode);
		if (responseJSONArray != null) {
			for(int i = 0; i < responseJSONArray.length(); i++) {
				if (responseJSONArray.isNull(i) || !responseJSONArray.optJSONObject(i).has("PosStockTransaction"))
					continue;
				
				oPosStockTransaction = new PosStockTransaction(responseJSONArray.optJSONObject(i).optJSONObject("PosStockTransaction"));
				// Add to stock transaction list
				m_oPosStockTransactionList.put(oPosStockTransaction.getTransactionId(), oPosStockTransaction);
			}
		}
	}
	
	public boolean addUpdateWithMutlipleTransactions(ArrayList<PosStockTransaction> oPosMutliStockTransactions) {
		PosStockTransaction oPosStockTransactionList = new PosStockTransaction(), oPosStockTransaction = null;
		JSONArray responseJSONArray = oPosStockTransactionList.addUpdateWithMutlipleTransactions(oPosMutliStockTransactions);
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				try {
					oPosStockTransaction = new PosStockTransaction(responseJSONArray.getJSONObject(i));
					
					// Add to function list
					m_oPosStockTransactionList.put(oPosStockTransaction.getTransactionId(), oPosStockTransaction);
				}
				catch (JSONException e) {
					e.printStackTrace();
					return false;
				}
			}
			
			if(!m_oPosStockTransactionList.isEmpty())
				return true;
			else
				return false;
		}
		return false;
	}
	
	
	public HashMap<String, PosStockTransaction> getStockTransaction(){
		return m_oPosStockTransactionList;
	}
}
