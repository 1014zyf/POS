package om;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class PosGeneral {
	PosBusinessDay m_oPosBusinessDay;
	PosOutletSetting m_oPosOutletSetting;
	PosItemPrintQueueList m_oPosItemPrintQueueList;
	PosConfigList m_oPosConfigList;
	PosInterfaceConfigList m_oPosInterfaceConfigList;
	PosTableMessageList m_oPosTableMessageList;
	PosItemRemindRuleList m_oPosItemRemindRuleList;
	PosFunctionList m_oPosFunctionList;
	PosTableSettingList m_oPosTableSettingList;
	JSONObject m_oResponseJSONObject;
	
	public PosGeneral() {
		m_oPosBusinessDay = null;
		m_oPosOutletSetting = null;
		m_oPosItemPrintQueueList = null;
		m_oPosConfigList = null;
		m_oPosInterfaceConfigList = null;
		m_oPosItemRemindRuleList = null;
		m_oPosFunctionList = null;
		m_oPosTableSettingList = null;
	}
	
	//read business day, outlet setting and item print queue by shop_id and outlet_id
	public void readBusinessDayOutletSettingItemPrintQueue(int iShopId, int iOutletId) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("shopId", iShopId);
			requestJSONObject.put("outletId", iOutletId);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		this.readDataFromApi("gm", "pos", "getBusinessDayOutletSettingItemPrintQueue", requestJSONObject.toString());
	}
	
	//read business day, outlet setting and item print queue by shop_id and outlet_id
	public void readConfigListTableMessageItemRemindRule(int iShopId, int iOutletId, int iStationId, String sBy) {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("shopId", iShopId);
			requestJSONObject.put("outletId", iOutletId);
			requestJSONObject.put("stationId", iStationId);
			requestJSONObject.put("by", sBy);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		this.readDataFromApi("gm", "pos", "getConfigListTableMessageItemRemindRule", requestJSONObject.toString());
	}
	
	//read data from POS API
	private void readDataFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		if (!OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false))
			return;
		
		JSONObject tempJSONObject = OmWsClientGlobal.g_oWsClient.get().getResponse();
		if (tempJSONObject == null)
			return;
		
		if (sFcnName.equals("getBusinessDayOutletSettingItemPrintQueue")) {
			if (!tempJSONObject.isNull("businessDay")) {
				JSONObject tempJSONObject2 = tempJSONObject.optJSONObject("businessDay");
				if (!tempJSONObject2.isNull("PosBusinessDay"))
					m_oPosBusinessDay = new PosBusinessDay(tempJSONObject.optJSONObject("businessDay"));
			}
			
			if (tempJSONObject.has("outletSetting") && !tempJSONObject.isNull("outletSetting")) {
				JSONObject tempJSONObject2 = tempJSONObject.optJSONObject("outletSetting");
				if (!tempJSONObject2.isNull("PosOutletSetting")) {
					m_oPosOutletSetting = new PosOutletSetting(tempJSONObject.optJSONObject("outletSetting"));
				}
			}
			
			if (tempJSONObject.has("itemPrintQueues") && !tempJSONObject.isNull("itemPrintQueues")) {
				m_oPosItemPrintQueueList = new PosItemPrintQueueList();
				m_oPosItemPrintQueueList.readItemQueueListByJSONArray(tempJSONObject.optJSONArray("itemPrintQueues"));
			}
		} else if(sFcnName.equals("getConfigListTableMessageItemRemindRule")) {
			if (tempJSONObject.has("configs") && !tempJSONObject.isNull("configs")) {
				m_oPosConfigList = new PosConfigList();
				m_oPosConfigList.readDataFromJsonArray(tempJSONObject.optJSONArray("configs"));
			}
			
			if (tempJSONObject.has("interfaceConfigs") && !tempJSONObject.isNull("interfaceConfigs"))
				m_oPosInterfaceConfigList = new PosInterfaceConfigList(tempJSONObject.optJSONArray("interfaceConfigs"));
			
			if (tempJSONObject.has("table_messages") && !tempJSONObject.isNull("table_messages")) {
				m_oPosTableMessageList = new PosTableMessageList();
				m_oPosTableMessageList.readAllByJSONArray(tempJSONObject.optJSONArray("table_messages"));
			}
			
			if (tempJSONObject.has("itemRemindRules") && !tempJSONObject.isNull("itemRemindRules")) {
				m_oPosItemRemindRuleList = new PosItemRemindRuleList();
				m_oPosItemRemindRuleList.readItemRemindRulesByJSONArray(tempJSONObject.optJSONArray("itemRemindRules"));
			}
			
			if (tempJSONObject.has("tableSettings") && !tempJSONObject.isNull("tableSettings")) {
				m_oPosTableSettingList = new PosTableSettingList();
				m_oPosTableSettingList.updateTableSettingList(tempJSONObject.optJSONArray("tableSettings"));
			}
		} else
			m_oResponseJSONObject = tempJSONObject;
	}
	
	public void readActiveBusinessDayAutoAndPortalStations() {
		JSONObject requestJSONObject = new JSONObject();
		
		this.readDataFromApi("gm", "pos", "getAllActiveAutoAndPortalStations", requestJSONObject.toString());
	}
	
	// auto restart pms shell
	public void performAutoRestartPMSShell() {
		JSONObject requestJSONObject = new JSONObject();
		
		try {
			requestJSONObject.put("by", "system");
			requestJSONObject.put("interfaceType", InfVendor.TYPE_PMS);
			requestJSONObject.put("autoRestart", true);
		}catch(JSONException jsone) {
			jsone.printStackTrace();
		}
		
		this.readDataFromApi("gm", "pos", "performAutoRestartPMSShell", requestJSONObject.toString());
	}
	
	public PosBusinessDay getPosBusinessDay() {
		return m_oPosBusinessDay;
	}
	
	public PosOutletSetting getPosOutletSetting() {
		return m_oPosOutletSetting;
	}
	
	public PosItemPrintQueueList getPosItemPrintQueueList() {
		return m_oPosItemPrintQueueList;
	}
	
	public PosConfigList getPosConfigList() {
		return m_oPosConfigList;
	}
	
	public PosInterfaceConfigList getPosInterfaceConfigList() {
		return m_oPosInterfaceConfigList;
	}
	
	public PosTableMessageList getPosTableMessageList() {
		return m_oPosTableMessageList;
	}
	
	public PosItemRemindRuleList getPosItemRemindRuleList() {
		return m_oPosItemRemindRuleList;
	}
	
	public PosFunctionList getPosFunctionList() {
		return m_oPosFunctionList;
	}
	
	public PosTableSettingList getPosTableSettingList() {
		return m_oPosTableSettingList;
	}
	
	public JSONObject getResponseJSONObject() {
		return m_oResponseJSONObject;
	}
}
