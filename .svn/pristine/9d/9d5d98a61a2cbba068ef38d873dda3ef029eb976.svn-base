package app.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosStockDeliveryInvoiceList {
	private ArrayList<PosStockDeliveryInvoice> m_oInvoiceList;

	public PosStockDeliveryInvoiceList(){
		m_oInvoiceList = new ArrayList<PosStockDeliveryInvoice>();
	}
	
	//read all function records
	public void readAll(int iOutletId) {	
		PosStockDeliveryInvoice oInvoiceList = new PosStockDeliveryInvoice(), oInvoice = null;
		JSONArray responseJSONArray = oInvoiceList.readAll(iOutletId, -1);		
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				JSONObject oInvoiceJSONObject = responseJSONArray.optJSONObject(i);
				if (oInvoiceJSONObject == null)
					continue;
				oInvoice = new PosStockDeliveryInvoice(oInvoiceJSONObject);
				
				// Add to function list
				m_oInvoiceList.add(oInvoice);
			}
		}
	}
	
	public ArrayList<PosStockDeliveryInvoice> getInvoiceList() {
		return m_oInvoiceList;
	}
}
