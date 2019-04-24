package app.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

public class PosStockTransactionList {

	private HashMap<Integer, PosStockTransaction> m_oPosStockTransactionList;
	
	public PosStockTransactionList(){
		m_oPosStockTransactionList = new HashMap<Integer, PosStockTransaction>();
	}
	
	public void addPosStockTransaction(int iTransactionId, PosStockTransaction oPosStockTransaction) {
		m_oPosStockTransactionList.put(iTransactionId, oPosStockTransaction);
	}
	
	//read all stock transaction records
	public void searchStockTransaction(int iBusinessDayId, int iOutletId, ArrayList<Integer> oItemIds, String sStatus) {
		PosStockTransaction oPosStockTransactionList = new PosStockTransaction(), oPosStockTransaction = null;
		JSONArray responseJSONArray = oPosStockTransactionList.searchStockTransaction(iBusinessDayId, iOutletId, oItemIds, sStatus);
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
	public void searchStockTransactionByItemIdAndType(int iBusinessDayId, int iOutletId, ArrayList<Integer> oItemIds, String sType) {
		PosStockTransaction oPosStockTransactionList = new PosStockTransaction(), oPosStockTransaction = null;
		JSONArray responseJSONArray = oPosStockTransactionList.searchStockTransactionByItemIdAndType(iBusinessDayId, iOutletId, oItemIds, sType);
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
	public void searchStockTransactionByType(int iBusinessDayId, int iOutletId, String sType) {
		PosStockTransaction oPosStockTransactionList = new PosStockTransaction(), oPosStockTransaction = null;
		JSONArray responseJSONArray = oPosStockTransactionList.searchStockTransactionByType(iBusinessDayId, iOutletId, sType);
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
	
	
	public HashMap<Integer, PosStockTransaction> getStockTransaction(){
		return m_oPosStockTransactionList;
	}
}
