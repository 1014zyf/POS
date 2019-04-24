package core.virtualui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import core.Core;

public class VirtualUIEvent {
	public static final int DEFAULT_TIME = 0;
	
	public final static String EVENT_ID_HIDE_KEYBOARD = "hide_keyboard";
	public final static String EVENT_ID_TRIGGER_KEYBOARD = "trigger_keyboard";
	public final static String EVENT_ID_REPLACE_VALUE = "replace_value";
	
	public final static String EVENT_CLICK = "click";
	public final static String EVENT_LONG_CLICK = "longClick";
	public final static String EVENT_SWIPE_RIGHT = "swipeRight";
	public final static String EVENT_SWIPE_LEFT = "swipeLeft";
	public final static String EVENT_SWIPE_TOP = "swipeTop";
	public final static String EVENT_SWIPE_BOTTOM = "swipeBottom";
	public final static String EVENT_VALUE_CHANGED = "valueChanged";
	public final static String EVENT_KEYBOARD = "keyboard";
	public final static String EVENT_TIMER = "timer";
	public final static String EVENT_IDLE = "idle";
	public final static String EVENT_FORWARD = "forward";
	
	public final static String[] EVENT_TYPES = new String[] {
			EVENT_CLICK, 
			EVENT_LONG_CLICK, 
			EVENT_SWIPE_RIGHT, 
			EVENT_SWIPE_LEFT, 
			EVENT_SWIPE_TOP, 
			EVENT_SWIPE_BOTTOM, 
			EVENT_VALUE_CHANGED, 
			EVENT_KEYBOARD,
			EVENT_TIMER,
			EVENT_IDLE,
			EVENT_FORWARD
		};
	
	protected boolean m_bIsEdited;
	protected boolean m_bIsFullEdited;
	
	protected int m_iId;
	protected int m_iElementId;
	protected final String m_sType;
	protected boolean m_bEnabled;						protected boolean m_bEnabledEdited;
	protected int m_iTime;								protected int m_iTimeEdited;
	protected boolean m_bRepeat;						protected boolean m_bRepeatEdited;
	
	// Server request
	protected ArrayList<Integer> m_oSubmitIdList;		protected ArrayList<Integer> m_oSubmitIdListEdited;
	protected boolean m_bBlockUi;						protected boolean m_bBlockUiEdited;
	protected boolean m_bAsync;							protected boolean m_bAsyncEdited;
	
	// Action list (Edit for list comparison)
	protected ArrayList<VirtualUIAction> m_oUIActionList;		protected ArrayList<VirtualUIAction> m_oUIActionListEdited;
	protected VirtualSystemAction m_oSystemAction;
	
	// For compatible
	protected String m_sNote;
	
	/** Create new event for element
	 * 
	 * @param sType			Event type
	 */
	public VirtualUIEvent(String sType) {
		// Get a unique ID
		m_sType = sType;
		m_bIsEdited = true;
		m_bIsFullEdited = true;
		m_iId = Core.g_oClientManager.getActiveClient().g_oUIManager.getElementId();
		
		m_bEnabled = true;										m_bEnabledEdited = true;
		m_iTime = 0;											m_iTimeEdited = 0;
		m_bRepeat = true;										m_bRepeatEdited = true;
		
		m_oSubmitIdList = new ArrayList<Integer>();				m_oSubmitIdListEdited = new ArrayList<Integer>();
		m_bBlockUi = true;										m_bBlockUiEdited = true;
		m_bAsync = false;										m_bAsyncEdited = false;
		
		m_oUIActionList = new ArrayList<VirtualUIAction>();		m_oUIActionListEdited = new ArrayList<VirtualUIAction>();
		m_oSystemAction = new VirtualSystemAction();
		
		m_sNote = "";
	}
	
	public void assignElement(int iElementId) {
		m_iElementId = iElementId;
	}
	
	public void applyEdit() {
		m_bIsEdited = false;
		m_bIsFullEdited = false;
		
		m_bEnabled = m_bEnabledEdited;
		m_iTime = m_iTimeEdited;
		m_bRepeat = m_bRepeatEdited;
		
		m_oSubmitIdList = (ArrayList<Integer>)m_oSubmitIdListEdited.clone();
		m_bBlockUi = m_bBlockUiEdited;
		m_bAsync = m_bAsyncEdited;
		
		m_oUIActionList = (ArrayList<VirtualUIAction>)m_oUIActionListEdited.clone();
		for (VirtualUIAction oUIAction : m_oUIActionList)
			oUIAction.applyEdit();
	}
	
	public boolean isEdited() {
		return m_bIsEdited;
	}
	
	public JSONObject buildEventJsonObject(boolean full) {
		JSONObject oEventJsonObject = new JSONObject();
		
		full |= m_bIsFullEdited;
		
		if (!full && !m_bIsEdited)
			return oEventJsonObject;
		
		try {
			oEventJsonObject.put(HeroActionProtocol.View.Event.Click.Id.KEY, m_iId);
			
			if ((full && !m_bEnabledEdited) || (!full && m_bEnabledEdited != m_bEnabled))
				oEventJsonObject.put(HeroActionProtocol.View.Event.Click.Enabled.KEY, m_bEnabledEdited);
			if ((full && m_iTimeEdited != DEFAULT_TIME) || (!full && m_iTimeEdited != m_iTime))
				oEventJsonObject.put(HeroActionProtocol.View.Event.Timer.Time.KEY, m_iTimeEdited);
			if ((full && !m_bRepeatEdited) || (!full && m_bRepeatEdited != m_bRepeat))
				oEventJsonObject.put(HeroActionProtocol.View.Event.Timer.Repeat.KEY, m_bRepeatEdited);
			
			// Event action
			//	Check if update is required
			boolean isEventActionEdited = false;
			while (true) {
				if (full) {
					isEventActionEdited = true;
					break;
				}
				
				//	Add or removed ui action
				if (m_oUIActionListEdited.size() != m_oUIActionList.size()) {
					isEventActionEdited = true;
					break;
				}

				for (VirtualUIAction oUIAction : m_oUIActionListEdited) {
					if (oUIAction.isEdited()) {
						isEventActionEdited = true;
						break;
					}
				}
				
				if (m_oSystemAction.isEdited()) {
					isEventActionEdited = true;
					break;
				}
				break;
			}
			
			if (full || isEventActionEdited) {
				JSONObject oEventActionJsonObject = new JSONObject();
				
				//	View
				JSONArray oEventUIActionJsonObject = new JSONArray();
				for (VirtualUIAction oUIAction : m_oUIActionListEdited)
					oEventUIActionJsonObject.put(oUIAction.buildJsonObject());
				if (oEventUIActionJsonObject.length() > 0)
					oEventActionJsonObject.put(HeroActionProtocol.View.KEY, oEventUIActionJsonObject);
				
				//	System
				JSONObject oEventSystemActionJSonObject = m_oSystemAction.buildJsonObject();
				if (oEventSystemActionJSonObject.length() > 0)
					oEventActionJsonObject.put(HeroActionProtocol.System.KEY, m_oSystemAction.buildJsonObject());
				
				if (oEventActionJsonObject.length() > 0)
					oEventJsonObject.put(HeroActionProtocol.View.Event.Click.Action.KEY, oEventActionJsonObject);
			}
			
			// Server request
			JSONObject oServerRequestJsonObject = new JSONObject();
			if ((full && !m_oSubmitIdListEdited.isEmpty()) || (!full && (!m_oSubmitIdListEdited.containsAll(m_oSubmitIdList) || m_oSubmitIdListEdited.size() != m_oSubmitIdList.size())))
				oServerRequestJsonObject.put(HeroActionProtocol.View.Event.Click.ServerRequest.SubmitId.KEY, m_oSubmitIdListEdited);
			if ((full && !m_bBlockUiEdited) || (!full && m_bBlockUiEdited != m_bBlockUi))
				oServerRequestJsonObject.put(HeroActionProtocol.View.Event.Click.ServerRequest.BlockUI.KEY, m_bBlockUiEdited);
			if ((full && m_bAsyncEdited) || (!full && m_bAsyncEdited != m_bAsync))
				oServerRequestJsonObject.put(HeroActionProtocol.View.Event.Click.ServerRequest.Async.KEY, m_bAsyncEdited);
			oEventJsonObject.put(HeroActionProtocol.View.Event.Click.ServerRequest.KEY, oServerRequestJsonObject);
			
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return oEventJsonObject;
	}
	
	public int getId(){
		return m_iId;
	}
	
	public String getType() {
		return m_sType;
	}
	
	public boolean isEnabled() {
		return m_bEnabledEdited;
	}
	
	public boolean setEnable(boolean bEnabled) {
		if (m_bEnabledEdited == bEnabled)
			return false;
		
		m_bEnabledEdited = bEnabled;
		
		// Set the event to be edited
		setEdited();
		
		return true;
	}
	
	public int getTime() {
		return m_iTimeEdited;
	}
	
	public boolean setTime(int iTime) {
		if (m_iTimeEdited == m_iTime)
			return false;
		
		m_iTimeEdited = m_iTime;
		
		// Set the event to be edited
		setEdited();
		
		return true;
	}
	
	public boolean isRepeat() {
		return m_bRepeatEdited;
	}
	
	public boolean setRepeat(boolean bRepeat) {
		if (m_bRepeatEdited == bRepeat)
			return false;
		
		m_bRepeatEdited = bRepeat;
		
		// Set the event to be edited
		setEdited();
		
		return true;
	}
	
	public boolean addSubmitId(int iId) {
		if (!m_oSubmitIdListEdited.add(iId))
			return false;
		
		// Set the event to be edited
		setEdited();
				
		return true;
	}
	
	public ArrayList<Integer> getSubmitIdList() {
		return m_oSubmitIdListEdited;
	}
	
	public boolean clearSubmitIdList() {
		m_oSubmitIdListEdited.clear();
		
		// Set the event to be edited
		setEdited();
				
		return true;
	}
	
	public boolean isBlockUi() {
		return m_bBlockUiEdited;
	}
	
	public boolean setBlockUI(boolean bBlockUi) {
		if (m_bBlockUiEdited == bBlockUi)
			return false;
		
		m_bBlockUiEdited = bBlockUi;
		
		// Set the event to be edited
		setEdited();
		
		return true;
	}
	
	public boolean isAsync() {
		return m_bAsyncEdited;
	}
	
	public boolean setAsync(boolean bAsync) {
		if (m_bAsyncEdited == bAsync)
			return false;
		
		m_bAsyncEdited = bAsync;
		
		// Set the event to be edited
		setEdited();
		
		return true;
	}
	
	private void setEdited() {
		setEdited(false);
	}
	
	private void setEdited(boolean full) {
		if (m_bIsEdited)
			return;
		
		m_bIsEdited = true;
		m_bIsFullEdited |= full;
		
		VirtualUIBasicElement oElement = Core.g_oClientManager.getActiveClient().g_oUIManager.getElement(m_iElementId);
		if (oElement == null)
			return;
		
		//	set element to edit
		oElement.setEdited();
	}
	
	public boolean isEdit() {
		return m_bIsEdited;
	}
	
	public boolean addVirtualUIAction(VirtualUIAction oAction) {
		if (!m_oUIActionListEdited.add(oAction))
			return false;
		
		// Set the event to be edited
		setEdited();
				
		return true;
	}
	
	public VirtualSystemAction getVirtualSystemAction() {
		return m_oSystemAction;
	}
	
	// For compatible
	public void setNote(String sNote) {
		m_sNote = sNote;
	}
	
	public String getNote() {
		return m_sNote;
	}
}
