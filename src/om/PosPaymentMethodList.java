package om;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosPaymentMethodList {
	private HashMap<Integer, PosPaymentMethod> m_oPaymentMethodList;
	private HashMap<Integer, PosPaymentMethod> m_oPaymentMethodListForDisplay;
	
	public PosPaymentMethodList(){
		m_oPaymentMethodList = new HashMap<Integer, PosPaymentMethod>();
		m_oPaymentMethodListForDisplay = new HashMap<Integer, PosPaymentMethod>();
	}

	//read all function records
	public void readAllWithAccessControl(int shopId, int outletId, String sBusinessDay, boolean bIsHoliday, boolean bIsDayBeforeHoliday, boolean bIsSpecialDay, boolean bIsDayBeforeSpecialDay, int iWeekday) {
		PosPaymentMethod oPosPaymentMethodList = new PosPaymentMethod();
		JSONArray responseJSONArray = oPosPaymentMethodList.readAllWithAccessRight(shopId, outletId, sBusinessDay, bIsHoliday, bIsDayBeforeHoliday, bIsSpecialDay, bIsDayBeforeSpecialDay, iWeekday);
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				if (responseJSONArray.isNull(i) || !responseJSONArray.optJSONObject(i).has("PosPaymentMethod"))
					continue;
				JSONObject responseJsonObject = responseJSONArray.optJSONObject(i).optJSONObject("PosPaymentMethod");

				PosPaymentMethod oPosPaymentMethod = new PosPaymentMethod(responseJsonObject);

				// Add to payment method list
				m_oPaymentMethodList.put(oPosPaymentMethod.getPaymId(), oPosPaymentMethod);

				// Add to display list
				m_oPaymentMethodListForDisplay.put(oPosPaymentMethod.getPaymentSequence(), oPosPaymentMethod);
			}
		}
	}
	
	// Get payment method
	public PosPaymentMethod getPaymentMethod(int iId){
		if(m_oPaymentMethodList.containsKey(iId) == false)
			return null;
		
		return m_oPaymentMethodList.get(iId);
	}
	
	// Get whole payment method list
	public HashMap<Integer, PosPaymentMethod> getPaymentMethodList(){
		return m_oPaymentMethodList;
	}
	
	// Get whole payment method list for display
	public HashMap<Integer, PosPaymentMethod> getPaymentMethodListForDisplay(){
		return m_oPaymentMethodListForDisplay;
	}
	
	// Get payment id by payment code
	public int getPayIdByPayCode(String sPosPayCode) {
		int iPayId = 0;
		
		for(Map.Entry<Integer, PosPaymentMethod> entry:m_oPaymentMethodList.entrySet()) {
			PosPaymentMethod sPosPaymentMethod = entry.getValue();
			if (sPosPaymentMethod.getPaymentCode().equals(sPosPayCode)) {
				iPayId = sPosPaymentMethod.getPaymId();
				break;
			}
		}
		
		return iPayId;
	}
	
	// Get payment method by payment code
	public PosPaymentMethod getPaymentMethodByCode(String sPosPayCode){
		PosPaymentMethod oPosPaymentMethod = this.getPaymentMethod(this.getPayIdByPayCode(sPosPayCode));
		return oPosPaymentMethod;
	}
	
}
