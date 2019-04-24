package om;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PosAlertStatusList {
	private ArrayList<PosAlertStatus> m_oPosAlertStatusList;
	
	public PosAlertStatusList() {
		m_oPosAlertStatusList = new ArrayList<>();
	}
	
	public void readPrinterDevicesStatusByPrintQueueIds(ArrayList<Integer> oPrintQueueIds) {
		PosAlertStatus oTempPosAlertStatus = new PosAlertStatus(), oPosAlertStatus;
		JSONArray oAlertStatusJSONArray = new JSONArray();
		
		oAlertStatusJSONArray = oTempPosAlertStatus.readPrintQueueIds(oPrintQueueIds);
		if (oAlertStatusJSONArray != null) {
			for (int i = 0; i < oAlertStatusJSONArray.length(); i++) {
				try {
					oPosAlertStatus = new PosAlertStatus(oAlertStatusJSONArray.getJSONObject(i));
					this.m_oPosAlertStatusList.add(oPosAlertStatus);
				} catch (JSONException jsone) {
					jsone.printStackTrace();
				}
			}
		}
	}
	
	public ArrayList<PosAlertStatus> getAlertStatusList(){
		return this.m_oPosAlertStatusList;
	}
	
	public boolean isAllPrinterDevicesStatusNormal(){
		for(PosAlertStatus oPrinterStatus: m_oPosAlertStatusList) {
			if (!oPrinterStatus.getStatus().equals(PosAlertStatus.PRINT_DEVICE_STATUS_NORMAL))
				return false;
		}
		return true;
	}
}
