package app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosCheckPaymentList {
	private HashMap<Integer, PosCheckPayment> m_oPosCheckPaymentList;
	
	public PosCheckPaymentList() {
		m_oPosCheckPaymentList = new HashMap<Integer, PosCheckPayment>();
	}
	
	public void readAllByOutletIdTypeEmpAndBDayIds(int iOutletId, String sPaymentType, int iEmployeeId, ArrayList<Integer> oBusinessDayIdList) {
		PosCheckPayment oCheckPayments = new PosCheckPayment(), oCheckPayment = null;
		JSONArray oCheckPaymentsJSONArray = oCheckPayments.readAllByOletIdTypeEmpAndBdayIds(iOutletId, sPaymentType, iEmployeeId, oBusinessDayIdList);
		if (oCheckPaymentsJSONArray != null) {
			for (int i = 0; i < oCheckPaymentsJSONArray.length(); i++) {
				if (oCheckPaymentsJSONArray.isNull(i) || !oCheckPaymentsJSONArray.optJSONObject(i).has("PosCheckPayment"))
					continue;
				JSONObject oCheckPaymentJSONObject = oCheckPaymentsJSONArray.optJSONObject(i).optJSONObject("PosCheckPayment");
				oCheckPayment = new PosCheckPayment(oCheckPaymentJSONObject);
				m_oPosCheckPaymentList.put(oCheckPayment.getCpayId(), oCheckPayment);
			}
		}
	}
	
	public BigDecimal getSumOfPayTotal() {
		BigDecimal dPayTotalSum = BigDecimal.ZERO;
		
		for(PosCheckPayment oCheckPayment:m_oPosCheckPaymentList.values()) 
			dPayTotalSum = dPayTotalSum.add(oCheckPayment.getPayTotal());
		
		return dPayTotalSum;
	}
}
