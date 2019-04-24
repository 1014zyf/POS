package core.virtualui;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * Mark 2014/02/04
 * 
 * VirtualUIAction only used as event's instant response.
 * The response is not suppose to be great and significant since server may not handle the response.
 * 
 * By definition above, action, instead of output the difference, will be output as a whole.
 * The newly produced action will replace old action completely.
 */

public class VirtualUIAction {
	private boolean m_bIsEdited;
	
	private String m_sElementId;
	private VirtualUIDoReplaceValue m_oReplaceValue;
	private boolean m_bTop;		//	####### Can be optimize to unique level
	private boolean m_bFocus;	//	####### Can be optimize to unique level
	private VirtualUIDoScrollTo m_oScrollTo;	//	####### Can be optimize to ELEMENT unique level
	private VirtualUIDoTriggerEvent m_oTriggerEvent;
	private Boolean m_bEnabled;
	
	// Constructor
	public VirtualUIAction(String sElementId){
		m_sElementId = sElementId;

		reset();
    }
	
	private void reset() {
		m_bIsEdited = true;

		m_bEnabled = null;
		m_oReplaceValue = null;
		m_bTop = false;
		m_bFocus = false;
		m_oScrollTo = null;
		m_oTriggerEvent = null;
	}
	
	public void applyEdit() {
		m_bIsEdited = false;
	}
	
	public boolean isEdited() {
		return m_bIsEdited;
	}

	public void replaceValue(String sReplaceValueRegex, String sReplaceValue) {
		m_bIsEdited = true;
		
		if (sReplaceValueRegex == null || sReplaceValueRegex.isEmpty()) {
			m_oReplaceValue = null;
			return;
		}
		
		m_oReplaceValue = new VirtualUIDoReplaceValue();
		m_oReplaceValue.setRegex(sReplaceValueRegex);
		m_oReplaceValue.setValue(sReplaceValue);
	}
	
	public void top() {
		m_bIsEdited = true;
		
		m_bTop = true;
	}
	
	public void focus() {
		m_bIsEdited = true;
		
		m_bFocus = true;
	}
	
	public void scrollToTop() {
		m_bIsEdited = true;
		
		m_oScrollTo = new VirtualUIDoScrollTo();
		m_oScrollTo.toTop();
	}
	
	public void scrollToBottom() {
		m_bIsEdited = true;
		
		m_oScrollTo = new VirtualUIDoScrollTo();
		m_oScrollTo.toBottom();
	}
	
	public void scrollToIndex(int iIndex) {
		m_bIsEdited = true;
		
		m_oScrollTo = new VirtualUIDoScrollTo();
		m_oScrollTo.toIndex(iIndex);
	}
	
	public void setClickElementEnable(String sElementId, Boolean bEnable){
		m_bIsEdited = true;
		
		m_bEnabled = bEnable;
	}
	
	public void triggerClick(int iDelay) {
		triggerEvent(VirtualUIEvent.EVENT_CLICK, iDelay);
	}
	
	public void triggerLongClick(int iDelay) {
		triggerEvent(VirtualUIEvent.EVENT_LONG_CLICK, iDelay);
	}
	
	public void triggerSwipeRight(int iDelay) {
		triggerEvent(VirtualUIEvent.EVENT_SWIPE_RIGHT, iDelay);
	}
	
	public void triggerSwipeLeft(int iDelay) {
		triggerEvent(VirtualUIEvent.EVENT_SWIPE_LEFT, iDelay);
	}
	
	public void triggerSwipeTop(int iDelay) {
		triggerEvent(VirtualUIEvent.EVENT_SWIPE_TOP, iDelay);
	}
	
	public void triggerBottom(int iDelay) {
		triggerEvent(VirtualUIEvent.EVENT_SWIPE_BOTTOM, iDelay);
	}
	
	public void triggerValueChanged(int iDelay) {
		triggerEvent(VirtualUIEvent.EVENT_VALUE_CHANGED, iDelay);
	}
	
	public void triggerTimer(int iDelay) {
		triggerEvent(VirtualUIEvent.EVENT_TIMER, iDelay);
	}
	
	public void triggerIdle(int iDelay) {
		triggerEvent(VirtualUIEvent.EVENT_IDLE, iDelay);
	}
	
	public void triggerForward(int iDelay) {
		triggerEvent(VirtualUIEvent.EVENT_FORWARD, iDelay);
	}
	
	private void triggerEvent(String sEvent, int iDelay) {
		m_bIsEdited = true;
		
		m_oTriggerEvent = new VirtualUIDoTriggerEvent(sEvent, iDelay);
	}
	
	public JSONObject buildJsonObject(){
		JSONObject oViewJsonObject = new JSONObject();
		
		try {
			//	View
				
				if (m_bEnabled != null) {
					JSONObject oAttributeJsonObject = new JSONObject();
					oViewJsonObject.put(HeroActionProtocol.View.Attribute.KEY, oAttributeJsonObject);
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.Enabled.KEY, m_bEnabled);
				}
				
				JSONObject oDoJsonObject = new JSONObject();
				
				if (m_oReplaceValue != null) {
					JSONObject oDoReplaceValueJsonObject = m_oReplaceValue.buildEventJsonObject();
					
					if (oDoReplaceValueJsonObject.length() > 0)
						oDoJsonObject.put(HeroActionProtocol.View.Do.ReplaceValue.KEY, oDoReplaceValueJsonObject);
				}
				
				if (m_bTop) {
					oDoJsonObject.put(HeroActionProtocol.View.Do.Top.KEY, new JSONObject());
				}
				
				if (m_bFocus) {
					oDoJsonObject.put(HeroActionProtocol.View.Do.Focus.KEY, new JSONObject());
				}
				
				if (m_oScrollTo != null) {
					JSONObject oDoScrollToJsonObject = m_oScrollTo.buildEventJsonObject();
					
					if (oDoScrollToJsonObject.length() > 0)
						oDoJsonObject.put(HeroActionProtocol.View.Do.ScrollTo.KEY, oDoScrollToJsonObject);
				}
				
				if (m_oTriggerEvent != null) {
					JSONObject oDoTriggerEventJsonObject = m_oTriggerEvent.buildEventJsonObject();
					
					if (oDoTriggerEventJsonObject.length() > 0)
						oDoJsonObject.put(HeroActionProtocol.View.Do.TriggerEvent.KEY, oDoTriggerEventJsonObject);
				}
				
				if (oDoJsonObject.length() > 0)
					oViewJsonObject.put(HeroActionProtocol.View.Do.KEY, oDoJsonObject);
				
				if (oViewJsonObject.length() > 0)
					oViewJsonObject.put(HeroActionProtocol.View.Id.KEY, m_sElementId);
		}
		catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		return oViewJsonObject;
	}
}
