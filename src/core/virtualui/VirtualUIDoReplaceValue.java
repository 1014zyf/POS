package core.virtualui;

import org.json.JSONException;
import org.json.JSONObject;

public class VirtualUIDoReplaceValue {
	private String m_sRegex;
	private String m_sValue;
	
	public VirtualUIDoReplaceValue() {
		
	}
	
	public void setRegex(String sRegex) {
		m_sRegex = sRegex;
	}
	
	public void setValue(String sValue) {
		m_sValue = sValue;
	}
	
	public JSONObject buildEventJsonObject() {
		JSONObject oDoReplaceValueJsonObject = new JSONObject();
		
		try {
			oDoReplaceValueJsonObject.put(HeroActionProtocol.View.Do.ReplaceValue.Regex.KEY, m_sRegex);
			oDoReplaceValueJsonObject.put(HeroActionProtocol.View.Do.ReplaceValue.Value.KEY, m_sValue);
		}
		catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return oDoReplaceValueJsonObject;
	}
}