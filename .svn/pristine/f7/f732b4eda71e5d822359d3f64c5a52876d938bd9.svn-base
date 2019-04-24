package core.virtualui;

import org.json.JSONException;
import org.json.JSONObject;

public class VirtualUIDoTriggerEvent {
	private String m_sType;
	private int m_iDelay;
	
	public VirtualUIDoTriggerEvent(String sType, int iDelay) {
		set(sType, iDelay);
	}
	
	private void set(String sType, int iDelay) {
		m_sType = sType;
		m_iDelay = iDelay;
	}
	
	public JSONObject buildEventJsonObject() {
		JSONObject oDoReplaceValueJsonObject = new JSONObject();
		
		try {
			if (m_sType != null && !m_sType.isEmpty()) {
				oDoReplaceValueJsonObject.put(HeroActionProtocol.View.Do.TriggerEvent.Type.KEY, m_sType);
				if (m_iDelay > 0)
					oDoReplaceValueJsonObject.put(HeroActionProtocol.View.Do.TriggerEvent.Delay.KEY, m_iDelay);
			}
		}
		catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return oDoReplaceValueJsonObject;
	}
}