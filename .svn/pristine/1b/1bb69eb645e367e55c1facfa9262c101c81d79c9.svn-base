package core.virtualui;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * Mark 2014/02/05
 * 
 * Application Cases:
 * 1. There will only be ONE VirtualSystemAction in VirtualUI. Once Action is passed to client, all action should reset all status.
 * 2. VirtualUIAction used as event's instant response.
 */

public class VirtualSystemAction {
	private boolean m_bIsEdited;
	
	private Boolean m_bLogout;
	private Boolean m_bShowKeyboard;
	private Boolean m_bHideKeyboard;
	
	// Constructor
	public VirtualSystemAction(){
		m_bIsEdited = true;
		
		reset();
    }
	
	//	For case 1
	private void reset() {
		m_bLogout = null;
		m_bShowKeyboard = null;
		m_bHideKeyboard = null;
	}
	
	public boolean isEdited() {
		return m_bIsEdited;
	}
	
	public void applyEdit() {
		m_bIsEdited = false;
		
		reset();
	}
	
	public void setLogout(Boolean bAction){
		m_bIsEdited = true;
		
		m_bLogout = bAction;
	}
		
	public boolean getLogout(){
		return m_bLogout;
	}
	
	public void setShowKeyboard(Boolean bAction){
		m_bIsEdited = true;
		
		m_bShowKeyboard = bAction;
	}
		
	public boolean getShowKeyboard(){
		return m_bShowKeyboard;
	}
	
	public void setHideKeyboard(Boolean bAction){
		m_bIsEdited = true;
		
		m_bHideKeyboard = bAction;
	}
		
	public boolean getHideKeyboard(){
		return m_bHideKeyboard;
	}
	
	public JSONObject buildJsonObject(){
		JSONObject oSystemJsonObject = new JSONObject();
		
		try {
			//	System
			if (m_bLogout != null && m_bLogout) {
				JSONObject oLogoutJsonObject = new JSONObject();
				oSystemJsonObject.put(HeroActionProtocol.System.Logout.KEY, oLogoutJsonObject);
			}
			
			if (m_bShowKeyboard != null && m_bShowKeyboard) {
				JSONObject oShowKeyboardJsonObject = new JSONObject();
				oSystemJsonObject.put(HeroActionProtocol.System.ShowKeyboard.KEY, oShowKeyboardJsonObject);
			}
			
			if (m_bHideKeyboard != null && m_bHideKeyboard) {
				JSONObject oHideKeyboardJsonObject = new JSONObject();
				oSystemJsonObject.put(HeroActionProtocol.System.HideKeyboard.KEY, oHideKeyboardJsonObject);
			}
		}
		catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return oSystemJsonObject;
	}
}
