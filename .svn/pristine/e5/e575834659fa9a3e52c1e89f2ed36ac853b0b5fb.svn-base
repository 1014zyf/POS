package core.virtualui;

import org.json.JSONException;
import org.json.JSONObject;

public class VirtualUIDoScrollTo {
	final static String POSITION_TOP = "top";
	final static String POSITION_BOTTOM = "bottom";
	final static String POSITION_INDEX = "index";
	
	private String m_sPosition;
	private int m_iIndex;
	
	public VirtualUIDoScrollTo() {
		
	}
	
	public void toBottom() {
		set(POSITION_BOTTOM, 0);
	}
	
	public void toTop() {
		set(POSITION_TOP, 0);
	}
	
	public void toIndex(int iIndex) {
		set(POSITION_INDEX, iIndex);
	}
	
	private void set(String sPosition, int iIndex) {
		m_sPosition = sPosition;
		m_iIndex = iIndex;
	}
	
	public JSONObject buildEventJsonObject() {
		JSONObject oDoReplaceValueJsonObject = new JSONObject();
		
		try {
			if (!m_sPosition.contentEquals(POSITION_INDEX))
				oDoReplaceValueJsonObject.put(HeroActionProtocol.View.Do.ScrollTo.Position.KEY, m_sPosition);
			else
				oDoReplaceValueJsonObject.put(HeroActionProtocol.View.Do.ScrollTo.Index.KEY, m_iIndex);
		}
		catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return oDoReplaceValueJsonObject;
	}
}